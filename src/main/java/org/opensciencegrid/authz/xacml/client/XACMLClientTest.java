package org.opensciencegrid.authz.xacml.client;

import org.opensaml.xml.XMLObjectBuilderFactory;
import org.opensaml.xml.io.MarshallingException;
import org.opensaml.xml.io.MarshallerFactory;
import org.opensaml.xml.io.Marshaller;
import org.opensaml.xml.schema.XSBooleanValue;
import org.opensaml.xacml.ctx.*;
import org.opensaml.xacml.ctx.impl.*;
import org.opensaml.xacml.profile.saml.XACMLAuthzDecisionQueryType;
import org.opensaml.xacml.profile.saml.SAMLProfileConstants;
import org.opensaml.xacml.profile.saml.impl.XACMLAuthzDecisionQueryTypeImplBuilder;
import org.opensaml.Configuration;
import org.opensaml.common.impl.SecureRandomIdentifierGenerator;
import org.opensaml.saml2.core.Issuer;
import org.opensaml.saml2.core.impl.IssuerBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.axis.types.URI;
import org.apache.axis.types.Id;
import org.apache.axis.message.MessageElement;
import org.opensciencegrid.authz.xacml.stubs.XACMLAuthorizationServiceLocator;
import org.opensciencegrid.authz.xacml.stubs.XACMLAuthorizationPortType;
import org.opensciencegrid.authz.xacml.stubs.XACMLAuthzDecisionQuery;
import org.opensciencegrid.authz.xacml.stubs.Response;
import org.opensciencegrid.authz.xacml.common.XACMLConstants;
import org.opensciencegrid.authz.xacml.common.LocalId;
import org.opensciencegrid.authz.xacml.service.BasicMappingXACMLAuthZService;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
//import org.globus.wsrf.utils.XmlUtils;

import javax.xml.rpc.ServiceException;
import java.util.*;
import java.security.NoSuchAlgorithmException;
import java.rmi.RemoteException;
import java.net.URL;

public class XACMLClientTest {
    private static Log logger = LogFactory.getLog(XACMLClientTest.class.getName());

    //private static I18n i18n = I18n;//.getI18n(Resources.class.getName());

    static XMLObjectBuilderFactory builderFactory;

    static {
        try {
            org.apache.xml.security.Init.init();
            org.opensaml.DefaultBootstrap.bootstrap();
        } catch (Exception e) {
            //String err = i18n.getExceptionMessage("xacmlInitFailed");
            String err = "xacmlInitFailed";
            logger.error(err, e);
            throw new RuntimeException(err, e);
        }
        builderFactory = Configuration.getBuilderFactory();
    }


  public static void main (String[] args) {
    String X509Subject = args[0];
    String condorName = args[1];
    String X509Issuer = args[2];
    String VO = args[3];
    String VOMSSigningSubject = args[4];
    String VOMSSigningIssuer = args[5];
    String VOMSFQAN = args[6];
    String VOMSPrimaryFQAN = args[7];
    String resourceType = args[8];
    String resourceDNSName = args[9];
    String resourceX509Name = args[10];
    String resourceX509Issuer = args[11];
    String authzServiceUrlStr = args[12];

    XACMLClient client = new XACMLClient();
    String issuer = null;

    LinkedHashMap subjectAttribs = new LinkedHashMap<String, String>();

    subjectAttribs.put(XACMLConstants.SUBJECT_X509_ID, X509Subject);
    subjectAttribs.put(XACMLConstants.SUBJECT_COMDOR_CANONICAL_NAME_ID, condorName);
    subjectAttribs.put(XACMLConstants.SUBJECT_X509_ISSUER, X509Issuer);
    subjectAttribs.put(XACMLConstants.SUBJECT_VO_ID, VO);
    subjectAttribs.put(XACMLConstants.SUBJECT_VOMS_SIGNING_SUBJECT_ID, VOMSSigningSubject);
    subjectAttribs.put(XACMLConstants.SUBJECT_VOMS_SIGNING_ISSUER_ID, VOMSSigningIssuer);
    subjectAttribs.put(XACMLConstants.SUBJECT_VOMS_FQAN_ID, VOMSFQAN);
    subjectAttribs.put(XACMLConstants.SUBJECT_VOMS_PRIMARY_FQAN_ID, VOMSPrimaryFQAN);

    SubjectType subject = client.getSubjectType(subjectAttribs, issuer);
    ResourceType resource = client.getResourceType(issuer);
    ActionType action = client.getActionType(issuer);
    EnvironmentType env = client.getEnvironmentType();

    //Response resp = client.authorize(subject, resource, action, env, authzServiceUrlStr);

    //LocalId id = client.convertToLocalID(resp);
  }



}

