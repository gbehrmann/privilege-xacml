package org.opensciencegrid.authz.xacml.client;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.glite.voms.VOMSAttribute;
import org.globus.gsi.GlobusCredential;
import org.globus.gsi.gssapi.GSSConstants;
import org.gridforum.jgss.ExtendedGSSContext;
import org.ietf.jgss.GSSContext;
import org.ietf.jgss.GSSException;
import org.opensciencegrid.authz.xacml.common.LocalId;
import org.opensciencegrid.authz.xacml.common.X509CertUtil;
import org.opensciencegrid.authz.xacml.common.XACMLConstants;

import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.*;

public class XACMLX509Test {

    private static Log logger = LogFactory.getLog(XACMLX509Test.class.getName());

    public static void main(String[] args) {

        GSSContext context = null;
        GlobusCredential cred = null;
        try {
            cred = new GlobusCredential(getProxyFile());
            System.setProperty("X509_PROXY_FILE", getProxyFile());
        } catch (Exception e) {
            logger.error("Caught exception in context creation. " + e.getMessage());
        }

        X509Certificate[] chain = null;
        try {
            chain = cred.getCertificateChain();
        } catch (Exception e) {
            logger.error("Could not extract certificate chain from context " + e.getMessage() + "\n" + e.getCause());
        }

        String X509Subject = null;
        try {
            X509Subject = X509CertUtil.getSubjectFromX509Chain(chain, false);
        } catch (Exception e) {
            logger.error("Could not get subjectname from proxy. " + e.getMessage());
        }
        logger.info("The subject name is " + X509Subject);

        Collection<String> fqans = null;
        try {
            fqans = X509CertUtil.getFQANsFromX509Chain(chain, false);
        } catch (Exception e)  {
            fqans = new LinkedHashSet();
            fqans.add(null);
        }

        Iterator <String> fqans_itr = fqans.iterator();
        while (fqans_itr.hasNext()) {
            String fqan = fqans_itr.next();
            String username = null;

            try {
                LocalId id = authorize(X509Subject, fqan, chain);
                username = id.getUserName();
            } catch (Exception e) {
                logger.error("Caught exception in authorization." + e.getMessage());
            }

            if (fqan==null) fqan = "null";

            if(username!=null) {
                logger.info("xacml-vo-mapping service returned Username: " + username + " for " + fqan);
            } else {
                logger.info("xacml-vo-mapping service did not return a username");
            }
        }

    }

    public static LocalId authorize(String X509Subject, String fqan, X509Certificate[] chain)
            throws Exception {

        String CondorCanonicalNameID=null;
        String X509SubjectIssuer=null;
        String VO=null;
        String VOMSSigningSubject=null;
        String VOMSSigningIssuer=null;

        String CertificateSerialNumber=null; //todo make Integer
        String CASerialNumber=null; //todo make Integer
        String VOMS_DNS_Port=null;
        String CertificatePoliciesOIDs=null;
        String CertificateChain=null; //todo make byte[]
        String resourceType=null;
        String resourceDNSHostName;
        String resourceX509ID;
        String resourceX509Issuer;
        String requestedaction=XACMLConstants.ACTION_ACCESS;
        String RSL_string=null;
        MapCredentialsClient xacmlClient;
        LocalId localId;
        String key = X509Subject;

        try {
            X509SubjectIssuer = X509CertUtil.getSubjectX509Issuer(chain);
        } catch (Exception e) {
            logger.warn("Could not determine subject-x509-issuer : " + e.getMessage());
        }

        VOMSAttribute vomsAttr=null;
        if (chain !=null && fqan !=null) {
            vomsAttr = X509CertUtil.getVOMSAttribute(chain, fqan);
        }
        if (vomsAttr!=null) {
            VO = vomsAttr.getVO();
            String X500IssuerName = vomsAttr.getAC().getIssuer().toString();
            VOMSSigningSubject = X509CertUtil.toGlobusDN(X500IssuerName);
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        String not_before = sdf.format(X509CertUtil.getLatestNotBefore(chain));
        String not_after =  sdf.format(X509CertUtil.getEarliestNotAfter(chain));

        try {
            resourceX509ID = getResourceX509ID();
        }
        catch (Exception e) {
            logger.error("Exception in finding targetServiceName : " + e);
            throw new Exception(e.toString());
        }


        try {
            resourceType = getResourceType();
        } catch (Exception e) {
            logger.error("Exception in finding resource type : " + e);
            throw new Exception(e.toString());
        }

        try {
            resourceDNSHostName = getResourceHostName();
        } catch (Exception e) {
            logger.error("Exception in finding targetServiceName : " + e);
            throw new Exception(e.toString());
        }

        try {
            resourceX509Issuer = getTargetServiceIssuer();
        }
        catch (Exception e) {
            logger.error("Exception in finding targetServiceIssuer : " + e);
            throw new Exception(e.toString());
        }


        logger.info("Requesting mapping for User with DN: " + X509Subject + " and Role " + fqan);

        logger.debug("Mapping Service URL configuration: " + getMappingServiceURL());
        try {
            xacmlClient = new MapCredentialsClient();
            xacmlClient.setX509Subject(X509Subject);
            xacmlClient.setCondorCanonicalNameID(CondorCanonicalNameID);
            xacmlClient.setX509SubjectIssuer(X509SubjectIssuer);
            xacmlClient.setVO(VO);
            xacmlClient.setVOMSSigningSubject(VOMSSigningSubject);
            xacmlClient.setVOMSSigningIssuer(VOMSSigningIssuer);
            xacmlClient.setFqan(fqan);
            xacmlClient.setCertificateSerialNumber(CertificateSerialNumber); //todo make Integer
            xacmlClient.setCertificateChainNotBefore(not_before);
            xacmlClient.setCertificateChainNotAfter(not_after);
            xacmlClient.setCASerialNumber(CASerialNumber); //todo make Integer
            xacmlClient.setVOMS_DNS_Port(VOMS_DNS_Port);
            xacmlClient.setCertificatePoliciesOIDs(CertificatePoliciesOIDs);
            xacmlClient.setCertificateChain(CertificateChain); //todo make byte[]
            xacmlClient.setResourceType(resourceType);
            xacmlClient.setResourceDNSHostName(resourceDNSHostName);
            xacmlClient.setResourceX509ID(resourceX509ID);
            xacmlClient.setResourceX509Issuer(resourceX509Issuer);
            xacmlClient.setRequestedaction(requestedaction);
            xacmlClient.setRSL_string(RSL_string);
        } catch (Exception e) {
            logger.error("Exception in XACML mapping client instantiation: " + e);
            throw new Exception(e.toString());
        }

        try {
            localId = xacmlClient.mapCredentials(getMappingServiceURL());
        }
        catch (Exception e ) {
            logger.error(" Exception occurred in mapCredentials: " + e);
            throw new Exception(e.toString());
        }

        if (localId == null) {
            String denied = "Authorization denied: No XACML mapping retrieved service for DN " + X509Subject + " and role " + fqan;
            logger.warn(denied);
            throw new Exception(denied);
        }

        return localId;
    }

    public static String getProxyFile() throws Exception {
        String val = System.getenv("X509_PROXY_FILE");
        if (val==null) throw new Exception();
        return val;
    }

    public static String getMappingServiceURL() throws Exception {
        String val = System.getenv("AUTHZ_SERVICE_URL");
        if (val==null) throw new Exception();
        return val;
    }

    public static String getResourceX509ID() throws Exception {
        String val = System.getenv("RESOURCE_X509");
        if (val==null) throw new Exception();
        return val;
    }

    public static String getResourceType() throws Exception {
        String resource = System.getenv("RESOURCE");
        if("CE".equals(resource)) {
            return System.getenv(XACMLConstants.RESOURCE_CE);
        } else if("WN".equals(resource)) {
            return System.getenv(XACMLConstants.RESOURCE_WN);
        } else if("SE".equals(resource)) {
            return System.getenv(XACMLConstants.RESOURCE_SE);
        }

        throw new Exception();
    }

    public static String getResourceHostName() throws Exception {
        String val = System.getenv("RESOURCE_DNS_HOST_NAME_ID");
        if (val==null) throw new Exception();
        return val;
    }

    public static String getTargetServiceIssuer() throws Exception {
        String val = System.getenv("RESOURCE_X509_ISSUER_ID");
        if (val==null) throw new Exception();
        return val;
    }
}
