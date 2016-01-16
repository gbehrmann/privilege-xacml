package org.opensciencegrid.authz.xacml.common;


import eu.emi.security.authn.x509.X509CertChainValidatorExt;
import eu.emi.security.authn.x509.X509Credential;
import eu.emi.security.authn.x509.impl.OpensslNameUtils;
import eu.emi.security.authn.x509.impl.PEMCredential;
import eu.emi.security.authn.x509.proxy.ProxyUtils;
import org.italiangrid.voms.VOMSAttribute;
import org.italiangrid.voms.VOMSValidators;
import org.italiangrid.voms.ac.VOMSACValidator;
import org.italiangrid.voms.store.VOMSTrustStore;
import org.italiangrid.voms.store.VOMSTrustStores;
import org.italiangrid.voms.util.CertificateValidatorBuilder;

import java.io.IOException;
import java.security.KeyStoreException;
import java.security.cert.CRLException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;

import static java.util.Arrays.asList;

/**
 * X509CertUtil.java
 * User: tdh
 * Date: Sep 15, 2008
 * Time: 5:06:59 PM
 * To change this template use File | Settings | File Templates.
 */
public class X509CertUtil {

    public static String default_service_cert          = "/etc/grid-security/hostcert.pem";
    public static String default_service_key           = "/etc/grid-security/hostkey.pem";

    private static X509CertChainValidatorExt certChainValidator;
    private static VOMSACValidator vomsValidator;
    private static PEMCredential hostCredential;

    private static int REFRESH_TIME_MS=20000;

    public static final String capnull = "/Capability=NULL";
    public static final int capnulllen = capnull.length();
    public static final String rolenull ="/Role=NULL";
    public static final int rolenulllen = rolenull.length();

    /**
     * Converts the certificate dn into globus dn representation:
     * 'cn=proxy, o=globus' into '/o=globus/cn=proxy'
     *
     * @param  certDN regural dn
     * @return globus dn representation
     */
    public static String toGlobusDN(String certDN) {
        return OpensslNameUtils.convertFromRfc2253(certDN, true);
    }

    public static String getSubjectFromX509Chain(X509Certificate[] chain, boolean omitEmail) throws Exception {
        return toGlobusDN(ProxyUtils.getOriginalUserDN(chain).getName());
    }

    public static X509Certificate getUserCertFromX509Chain(X509Certificate[] chain) throws Exception {
        return ProxyUtils.getEndUserCertificate(chain);
    }

    public static Date getLatestNotBefore(X509Certificate[] chain) throws Exception {
        Date not_before=null;
        for (X509Certificate testcert : chain) {
            Date test_not_before = testcert.getNotBefore();
            if (not_before == null || test_not_before.after(not_before)) {
                not_before = test_not_before;
            }

            // No need to test certificate chain beyond the user cert
            if (!ProxyUtils.isProxy(testcert)) {
                break;
            }
        }

        if(not_before == null) {
            throw new Exception("could not find any not-before time in the certificate chain.");
        }

        return not_before;
    }

    public static Date getEarliestNotAfter(X509Certificate[] chain) throws Exception {
        Date not_after=null;
        for (X509Certificate testcert : chain) {
            Date test_not_after = testcert.getNotAfter();
            if (not_after == null || test_not_after.before(not_after)) {
                not_after = test_not_after;
            }

            // No need to test certificate chain beyond the user cert
            if (!ProxyUtils.isProxy(testcert)) {
                break;
            }
        }

        if(not_after == null) {
            throw new Exception("could not find any not-after time in the certificate chain.");
        }

        return not_after;
    }

    public static String getSubjectX509Issuer(X509Certificate[] chain) throws Exception {
       X509Certificate	clientcert = getUserCertFromX509Chain(chain);
       return getSubjectX509Issuer(clientcert);
    }

    public static String getSubjectX509Issuer(X509Certificate cert) throws Exception {
       return toGlobusDN(cert.getIssuerDN().toString());
    }

    public static Collection <String> getFQANsFromX509Chain(X509Certificate[] chain, boolean validate) throws Exception {
        Collection <String> fqans=null;
        try {
            List<VOMSAttribute> listOfAttributes = getVOMSAttributes(chain, validate);
            fqans = getFQANSfromVOMSAttributes(listOfAttributes);
        } catch(Exception ae ) {
            throw new Exception(ae.toString());
        }

        return fqans;
    }

    /**
   *  We want to keep different roles but discard subroles. For example,
attribute : /cms/uscms/Role=cmssoft/Capability=NULL
attribute : /cms/uscms/Role=NULL/Capability=NULL
attribute : /cms/Role=NULL/Capability=NULL
attribute : /cms/uscms/Role=cmsprod/Capability=NULL

   should yield the roles

   /cms/uscms/Role=cmssoft/Capability=NULL
   /cms/uscms/Role=cmsprod/Capability=NULL
*/

    public static LinkedHashSet<String> getFQANSfromVOMSAttributes(List<VOMSAttribute> listOfAttributes) {
        LinkedHashSet<String> fqans = new LinkedHashSet <String> ();

        for (VOMSAttribute vomsAttribute : listOfAttributes) {
            for (String attr : vomsAttribute.getFQANs()) {
                if (attr.endsWith(capnull))
                    attr = attr.substring(0, attr.length() - capnulllen);
                if (attr.endsWith(rolenull))
                    attr = attr.substring(0, attr.length() - rolenulllen);
                boolean issubrole = false;
                for (String fqanattr : fqans) {
                    if (fqanattr.startsWith(attr)) {
                        issubrole = true;
                        break;
                    }
                }
                if (!issubrole) fqans.add(attr);
            }
        }

        return fqans;
    }

    public static VOMSAttribute getVOMSAttribute(X509Certificate[] chain, String fqan) throws Exception {

        if(fqan.endsWith(capnull))
                fqan = fqan.substring(0, fqan.length() - capnulllen);
        if(fqan.endsWith(rolenull))
                fqan = fqan.substring(0, fqan.length() - rolenulllen);

        List<VOMSAttribute> listOfAttributes = getVOMSAttributes(chain, false);

        for (VOMSAttribute vomsAttribute : listOfAttributes) {
            List<String> listOfFqans = vomsAttribute.getFQANs();
            for (String attr : listOfFqans) {
                if (attr.endsWith(capnull))
                    attr = attr.substring(0, attr.length() - capnulllen);
                if (attr.endsWith(rolenull))
                    attr = attr.substring(0, attr.length() - rolenulllen);
                if (attr.equals(fqan)) return vomsAttribute;
            }
        }

        return null;
    }

    public static List<VOMSAttribute> getVOMSAttributes(X509Certificate[] chain, boolean validate) throws Exception {
        try {
            VOMSACValidator validator = getVOMSValidatorInstance();
            if (validate) {
                return validator.validate(chain);
            } else {
                return validator.parse(chain);
            }
        } catch (IOException ioe) {
            throw new Exception("Could not read trust stores " + ioe.getMessage() + "\n" + ioe.getCause());
        } catch (CertificateException ce) {
            throw new Exception("Could not read certificate " + ce.getMessage() + "\n" + ce.getCause());
        } catch (CRLException crle) {
            throw new Exception("Could not read CRL " + crle.getMessage() + "\n" + crle.getCause());
        }
    }

    public static synchronized VOMSACValidator getVOMSValidatorInstance() throws IOException, CertificateException, CRLException {
        if (vomsValidator == null) {
            String vomsDir = System.getProperty("VOMSDIR");
            VOMSTrustStore vomsTrustStore =
                    (vomsDir == null)
                            ? VOMSTrustStores.newTrustStore()
                            : VOMSTrustStores.newTrustStore(asList(vomsDir));
            X509CertChainValidatorExt certChainValidator = getCertChainValidator();
            vomsValidator = VOMSValidators.newValidator(vomsTrustStore, certChainValidator);
        }
        return vomsValidator;
    }

    public static synchronized X509CertChainValidatorExt getCertChainValidator()
    {
        if (certChainValidator == null) {
            String caDir = System.getProperty("CADIR");
            CertificateValidatorBuilder certificateValidatorBuilder = new CertificateValidatorBuilder();
            if (caDir != null) {
                certificateValidatorBuilder.trustAnchorsDir(caDir);
            }
            certChainValidator = certificateValidatorBuilder.build();
        }
        return certChainValidator;
    }

    public static synchronized X509Credential getHostCredential() throws CertificateException, KeyStoreException, IOException
    {
        if (hostCredential == null) {
            String hostkey = System.getProperty("HOSTKEY", default_service_key);
            String hostcert = System.getProperty("HOSTCERT", default_service_cert);
            hostCredential = new PEMCredential(hostkey, hostcert, null);
        }
        return hostCredential;
    }
}

