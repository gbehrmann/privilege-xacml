package org.opensciencegrid.authz.xacml.client;

import org.opensaml.DefaultBootstrap;
import org.opensaml.xacml.ctx.ResourceType;
import org.opensaml.xacml.ctx.ActionType;
import org.opensaml.xacml.ctx.EnvironmentType;
import org.opensaml.xacml.ctx.SubjectType;
import org.opensaml.xacml.ctx.DecisionType;
import org.opensaml.xacml.ctx.ResultType;
import org.opensaml.xacml.ctx.StatusType;
import org.opensaml.xml.XMLObjectBuilderFactory;
import org.opensaml.xacml.profile.saml.XACMLAuthzDecisionStatementType;

import org.opensaml.Configuration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xml.security.Init;

import org.opensciencegrid.authz.xacml.common.XACMLConstants;
import org.opensciencegrid.authz.xacml.stubs.Response;

import java.util.LinkedHashMap;

public class XACMLClientTest
{
    private static Log logger = LogFactory.getLog(XACMLClientTest.class.getName());
    static XMLObjectBuilderFactory builderFactory;

    public static void main(String[] args)
    {
        XACMLClient client = new XACMLClient();
        String issuer = null;

        String authzServiceUrlStr = System.getenv("AUTHZ_SERVICE_URL");

        LinkedHashMap<String,String> mySubjectAttribs = new LinkedHashMap<String,String>();

        mySubjectAttribs.put(XACMLConstants.SUBJECT_X509_ID, System.getenv("SUBJECT_X509"));
        mySubjectAttribs.put(XACMLConstants.SUBJECT_CONDOR_CANONICAL_NAME_ID, System.getenv("SUBJECT_CONDOR_CANONICAL_NAME"));
	    mySubjectAttribs.put(XACMLConstants.SUBJECT_X509_ISSUER, System.getenv("SUBJECT_X509_ISSUER"));
	    mySubjectAttribs.put(XACMLConstants.SUBJECT_VO_ID, System.getenv("SUBJECT_VO"));
	    mySubjectAttribs.put(XACMLConstants.SUBJECT_VOMS_SIGNING_SUBJECT_ID, System.getenv("SUBJECT_VOMS_SIGNING_SUBJECT"));
	    mySubjectAttribs.put(XACMLConstants.SUBJECT_VOMS_SIGNING_ISSUER_ID, System.getenv("SUBJECT_VOMS_SIGNING_ISSUER"));
	    mySubjectAttribs.put(XACMLConstants.SUBJECT_VOMS_PRIMARY_FQAN_ID, System.getenv("SUBJECT_VOMS_PRIMARY_FQAN"));
	    //SUBJECT_VOMS_FQAN_ID is added in the client code
	    mySubjectAttribs.put(XACMLConstants.SUBJECT_CERTIFICATE_SERIAL_NUMBER_ID, System.getenv("SUBJECT_CERTIFICATE_SERIAL_NUMBER"));
	    mySubjectAttribs.put(XACMLConstants.SUBJECT_CERTIFICATE_NOT_BEFORE_ID, System.getenv("SUBJECT_CERTIFICATE_NOT_BEFORE"));
	    mySubjectAttribs.put(XACMLConstants.SUBJECT_CERTIFICATE_NOT_AFTER_ID, System.getenv("SUBJECT_CERTIFICATE_NOT_AFTER"));
	    mySubjectAttribs.put(XACMLConstants.SUBJECT_CA_SERIAL_NUMBER_ID, System.getenv("SUBJECT_CA_SERIAL_NUMBER"));
	    mySubjectAttribs.put(XACMLConstants.SUBJECT_VOMS_DNS_PORT_ID, System.getenv("SUBJECT_VOMS_DNS_PORT"));
	    mySubjectAttribs.put(XACMLConstants.SUBJECT_CA_POLICY_OID_ID, System.getenv("SUBJECT_CA_POLICY_OID"));
	    mySubjectAttribs.put(XACMLConstants.SUBJECT_CERT_CHAIN_ID, System.getenv("SUBJECT_CERT_CHAIN"));

        LinkedHashMap<String,String> myResourceAttribs = new LinkedHashMap<String,String>();
        if("CE".equals(System.getenv("RESOURCE"))) {
            myResourceAttribs.put(XACMLConstants.RESOURCE_ID, XACMLConstants.RESOURCE_CE);
        } else if("WN".equals(System.getenv("RESOURCE"))) {
            myResourceAttribs.put(XACMLConstants.RESOURCE_ID, XACMLConstants.RESOURCE_WN);
        } else if("SE".equals(System.getenv("RESOURCE"))) {
            myResourceAttribs.put(XACMLConstants.RESOURCE_ID, XACMLConstants.RESOURCE_SE);
        }
        myResourceAttribs.put(XACMLConstants.RESOURCE_DNS_HOST_NAME_ID, System.getenv("RESOURCE_DNS_HOST_NAME"));
        myResourceAttribs.put(XACMLConstants.RESOURCE_X509_ID, System.getenv("RESOURCE_X509"));
        myResourceAttribs.put(XACMLConstants.RESOURCE_X509_ISSUER_ID, System.getenv("RESOURCE_X509_ISSUER"));

        LinkedHashMap<String,String> myActionAttribs = new LinkedHashMap<String,String>();
        myActionAttribs.put(XACMLConstants.ACTION_ID, "http://authz-interop.org/xacml/action/action-type/access");
        myActionAttribs.put(XACMLConstants.ACTION_RSL_STRING, System.getenv("ACTION_RSL_STRING"));

        for (int i=0; i<1; i++) {

        SubjectType subject = XACMLClient.getSubjectType(mySubjectAttribs, issuer);
        //Add FQANS
        String fqan = System.getenv("SUBJECT_VOMS_PRIMARY_FQAN");
        if(fqan!=null) {
            XACMLClient.addSubjectFQAN(fqan, subject, issuer);
        }
        String secondary_fqan = System.getenv("SUBJECT_VOMS_SECONDARY_FQAN");
        if(secondary_fqan!=null) {
            XACMLClient.addSubjectFQAN(secondary_fqan, subject, issuer);
        }
        String tertiary_fqan = System.getenv("SUBJECT_VOMS_TERTIARY_FQAN");
        if(tertiary_fqan!=null) {
            XACMLClient.addSubjectFQAN(tertiary_fqan, subject, issuer);
        }

        ResourceType resource = XACMLClient.getResourceType(myResourceAttribs, issuer);
        ActionType action = XACMLClient.getActionType(myActionAttribs, issuer);
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
