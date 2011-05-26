package org.opensciencegrid.authz.xacml.client;

import org.opensaml.DefaultBootstrap;
import org.opensaml.xml.XMLObjectBuilderFactory;
import org.opensaml.xacml.profile.saml.XACMLAuthzDecisionStatementType;
import org.opensaml.xacml.ctx.*;
import org.opensaml.Configuration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xml.security.Init;
import org.opensciencegrid.authz.xacml.stubs.Response;

import org.glite.voms.PKIStore;

import java.util.LinkedHashMap;

public class XACMLClientTest
{
    private static Log logger = LogFactory.getLog(XACMLClientTest.class.getName());
    static XMLObjectBuilderFactory builderFactory;

    public static void main(String[] args)
    {
        String X509Subject = System.getenv(args[0].substring(1));
        String condorName = System.getenv(args[1].substring(1));
        String X509Issuer = System.getenv(args[2].substring(1));
        String VO = System.getenv(args[3].substring(1));
        String VOMSSigningSubject = System.getenv(args[4].substring(1));
        String VOMSSigningIssuer = System.getenv(args[5].substring(1));
        String VOMSFQAN = System.getenv(args[6].substring(1));
        String VOMSPrimaryFQAN = System.getenv(args[7].substring(1));
        String resourceType = System.getenv(args[8].substring(1));
        String resourceDNSName = System.getenv(args[9].substring(1));
        String resourceX509Name = System.getenv(args[10].substring(1));
        String resourceX509Issuer = System.getenv(args[11].substring(1));
        String authzServiceUrlStr = System.getenv(args[12].substring(1));

        XACMLClient client = new XACMLClient();
        String issuer = null;

        LinkedHashMap<String,String> subjectAttribs = new LinkedHashMap<String,String>();

        subjectAttribs.put("http://authz-interop.org/xacml/subject/subject-x509-id", X509Subject);
        subjectAttribs.put("http://authz-interop.org/xacml/subject/subject-condor-canonical-name-id", condorName);
        subjectAttribs.put("http://authz-interop.org/xacml/subject/subject-x509-issuer", X509Issuer);
        subjectAttribs.put("http://authz-interop.org/xacml/subject/vo", VO);
        subjectAttribs.put("http://authz-interop.org/xacml/subject/voms-signing-subject", VOMSSigningSubject);
        subjectAttribs.put("http://authz-interop.org/xacml/subject/voms-signing-issuer", VOMSSigningIssuer);
        subjectAttribs.put("http://authz-interop.org/xacml/subject/voms-fqan", VOMSFQAN);
        subjectAttribs.put("http://authz-interop.org/xacml/subject/voms-primary-fqan", VOMSPrimaryFQAN);

        LinkedHashMap<String,String> resourceAttribs = new LinkedHashMap<String,String>();
        resourceAttribs.put("urn:oasis:names:tc:xacml:1.0:resource:resource-id", resourceType);
        resourceAttribs.put("http://authz-interop.org/xacml/resource/dns-host-name", resourceDNSName);
        resourceAttribs.put("http://authz-interop.org/xacml/resource/resource-x509-id", resourceX509Name);
        resourceAttribs.put("http://authz-interop.org/xacml/resource/resource-x509-issuer", resourceX509Issuer);

        LinkedHashMap<String,String> actionAttribs = new LinkedHashMap<String,String>();
        actionAttribs.put("urn:oasis:names:tc:xacml:1.0:action:action-id", "http://authz-interop.org/xacml/action/action-type/access");

        for (int i=0; i<3; i++) {

            SubjectType subject = XACMLClient.getSubjectType(subjectAttribs, issuer);
            ResourceType resource = XACMLClient.getResourceType(resourceAttribs, issuer);
            ActionType action = XACMLClient.getActionType(actionAttribs, issuer);
            EnvironmentType env = XACMLClient.getEnvironmentType();

            logger.info("Trying " + authzServiceUrlStr);

            Response resp;
            XACMLAuthzDecisionStatementType statement;
            try {
                resp = client.authorize(subject, resource, action, env, authzServiceUrlStr);
                statement = client.convertToXACML(resp);
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }

            ResultType result = statement.getResponse().getResult();
            logger.info("Obtained result.");
            if (result.getDecision().getDecision() == DecisionType.DECISION.Indeterminate) {
                StatusType status = result.getStatus();
                if ((status != null) && (status.getStatusCode().getValue().equals("urn:oasis:names:tc:xacml:1.0:status:processing-error"))) {
                    String msg = status.getStatusMessage().getValue();
                    logger.info(msg);
                }
            }

            if (result.getStatus().getStatusMessage() != null) {
                logger.info("Result Status: " + result.getStatus().getStatusMessage().getValue());
            }
            if (result.getDecision().getDecision() == DecisionType.DECISION.Permit) {
                String decision = "Service authorized this user";
                logger.info("Decision: " + decision);
            } else {
                String decision = "Service did not authorize this user";
                logger.info("Decision: " + decision);
            }
        }
    }

    static
    {
        try
        {
            Init.init();
            DefaultBootstrap.bootstrap();
        } catch (Exception e) {
            String err = "XACML init failed.";
            logger.error(err, e);
            throw new RuntimeException(err, e);
        }
        builderFactory = Configuration.getBuilderFactory();

        /*
        try
        {
            PKIStore caStore;
            String caDir = System.getProperty( "CADIR" );
            caDir = (caDir == null) ? PKIStore.DEFAULT_CADIR : caDir;
            caStore = new PKIStore( caDir, PKIStore.TYPE_CADIR, true );
            caStore.rescheduleRefresh(900000);
        } catch (Exception e) {
            String err = "TrustStore init failed.";
            logger.error(err, e);
            throw new RuntimeException(err, e);
        }
        */
    }

}
