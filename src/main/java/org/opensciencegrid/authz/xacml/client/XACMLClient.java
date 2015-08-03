package org.opensciencegrid.authz.xacml.client;

import static org.opensciencegrid.authz.xacml.common.XACMLConstants.SUBJECT_VOMS_SIGNING_SUBJECT_ID;

import org.apache.axis.message.MessageElement;
import org.opensaml.xml.XMLObjectBuilderFactory;
import org.opensaml.xml.XMLObjectBuilder;
import org.opensaml.xml.util.XMLConstants;
import org.opensaml.xml.util.XMLHelper;
import org.opensaml.xml.io.MarshallingException;
import org.opensaml.xml.io.MarshallerFactory;
import org.opensaml.xml.io.Marshaller;
import org.opensaml.xml.io.UnmarshallingException;
import org.opensaml.xml.io.Unmarshaller;
import org.opensaml.xml.io.UnmarshallerFactory;
import org.opensaml.xml.schema.XSBooleanValue;
import org.opensaml.xml.schema.XSString;
import org.opensaml.xml.schema.impl.XSStringBuilder;
import org.opensaml.xacml.profile.saml.XACMLAuthzDecisionQueryType;
import org.opensaml.xacml.profile.saml.SAMLProfileConstants;
import org.opensaml.xacml.profile.saml.XACMLAuthzDecisionStatementType;
import org.opensaml.xacml.profile.saml.impl.XACMLAuthzDecisionQueryTypeImplBuilder;
import org.opensaml.xacml.profile.saml.impl.XACMLAuthzDecisionStatementTypeImplBuilder;
import org.opensaml.xacml.ctx.ResourceType;
import org.opensaml.xacml.ctx.ActionType;
import org.opensaml.xacml.ctx.EnvironmentType;
import org.opensaml.xacml.ctx.SubjectType;
import org.opensaml.xacml.ctx.AttributeType;
import org.opensaml.xacml.ctx.AttributeValueType;
import org.opensaml.xacml.ctx.RequestType;
import org.opensaml.xacml.ctx.DecisionType;
import org.opensaml.xacml.ctx.ResponseType;
import org.opensaml.xacml.ctx.ResultType;
import org.opensaml.xacml.ctx.StatusType;
import org.opensaml.xacml.ctx.StatusCodeType;
import org.opensaml.xacml.ctx.StatusMessageType;
import org.opensaml.xacml.ctx.impl.AttributeTypeImplBuilder;
import org.opensaml.xacml.ctx.impl.AttributeValueTypeImplBuilder;
import org.opensaml.xacml.ctx.impl.SubjectTypeImplBuilder;
import org.opensaml.xacml.ctx.impl.ResourceTypeImplBuilder;
import org.opensaml.xacml.ctx.impl.ActionTypeImplBuilder;
import org.opensaml.xacml.ctx.impl.EnvironmentTypeImplBuilder;
import org.opensaml.xacml.ctx.impl.RequestTypeImplBuilder;
import org.opensaml.xacml.ctx.impl.DecisionTypeImplBuilder;
import org.opensaml.xacml.ctx.impl.ResponseTypeImplBuilder;
import org.opensaml.xacml.ctx.impl.ResultTypeImplBuilder;
import org.opensaml.xacml.ctx.impl.StatusTypeImplBuilder;
import org.opensaml.xacml.ctx.impl.StatusCodeTypeImplBuilder;
import org.opensaml.xacml.ctx.impl.StatusMessageTypeImplBuilder;
import org.opensaml.Configuration;
import org.opensaml.common.impl.SecureRandomIdentifierGenerator;
import org.opensaml.common.SAMLException;
import org.opensaml.saml2.core.Issuer;
import org.opensaml.saml2.core.Statement;
import org.opensaml.saml2.core.Assertion;
import org.opensaml.saml2.core.impl.IssuerBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.axis.types.URI;
import org.apache.axis.types.Id;
import org.apache.axis.types.NCName;
import org.apache.axis.message.PrefixedQName;
import org.apache.axis.utils.XMLUtils;
import com.sun.xacml.ctx.Status;
import javax.xml.namespace.QName;
import javax.xml.soap.Name;

import org.opensciencegrid.authz.xacml.common.XACMLConstants;
import org.opensciencegrid.authz.xacml.common.OSGSAMLBootstrap;
import org.opensciencegrid.authz.xacml.common.FQAN;
import org.opensciencegrid.authz.xacml.stubs.*;

import org.w3c.dom.*;

//import org.globus.gsi.gssapi.auth.NoAuthorization;

import javax.xml.rpc.ServiceException;

import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.rmi.RemoteException;
import java.util.*;


public class XACMLClient {
    public static Log logger = LogFactory.getLog(XACMLClient.class.getName());
    public static final PrefixedQName classname = new PrefixedQName(new QName("urn:oasis:names:tc:SAML:2.0:assertion", "Assertion"));

    protected String X509Subject;
    protected String CondorCanonicalNameID;
    protected String X509SubjectIssuer;
    protected String VO;
    protected String VOMSSigningSubject;
    protected String VOMSSigningIssuer;
    protected String fqan;
    protected String CertificateSerialNumber; //todo make Integer
    protected String CertificateChainNotBefore; // YYYY-MM-DDThh:mm:ss, any of 2002-05-30T09:00:00,
    protected String CertificateChainNotAfter;  // 2002-05-30T09:30:10.5,2002-05-30T09:30:10Z,2002-05-30T09:30:10-06:00
    protected String CASerialNumber; //todo make Integer
    protected String VOMS_DNS_Port;
    protected String CertificatePoliciesOIDs;
    protected String CertificateChain; //todo make byte[]
    protected String resourceType;
    protected String resourceDNSHostName;
    protected String resourceX509ID;
    protected String resourceX509Issuer;
    protected String requestedaction;
    protected String RSL_string;

    static XMLObjectBuilderFactory builderFactory;

    static {

        try {
            org.apache.xml.security.Init.init();
            OSGSAMLBootstrap.bootstrap();
        } catch (Exception e) {
            String err = "xacmlInitFailed";
            logger.error(err, e);
            throw new RuntimeException(err, e);
        }
        builderFactory = Configuration.getBuilderFactory();

        System.setProperty("axis.socketSecureFactory","org.glite.security.trustmanager.axis.AXISSocketFactory");
    }

    Response authorize (String authzServiceUrlStr) throws RemoteException {
        String issuer=null;

        SubjectType subject = getSubjectType(issuer);
        ResourceType resource = getResourceType(issuer);
        ActionType action = getActionType(issuer);
        EnvironmentType env = getEnvironmentType();

        return authorize(authzServiceUrlStr);
    }

    Response authorize (SubjectType subject, ResourceType resource, ActionType action, EnvironmentType env, String authzServiceUrlStr) throws RemoteException {

        // Create query
        boolean inputContextOnly = false;
        boolean returnContext = false;
        boolean combineLocalPolicy = true;

        String issuer=null;

        XACMLAuthzDecisionQueryType xacmlAuthzQueryType=null;
        try {
            xacmlAuthzQueryType = getQuery(subject, resource, action, env,
                    inputContextOnly, returnContext, combineLocalPolicy, issuer);
        } catch (NoSuchAlgorithmException e) {
            logger.error(e);
            return getErrorResponse(xacmlAuthzQueryType, e);
        }

        logger.trace("XACML Authz Query is " + xacmlAuthzQueryType);

        URL authzServiceUrl=null;
        try {authzServiceUrl = new URL(authzServiceUrlStr);} catch (Exception e) {
            logger.error(e);
            return getErrorResponse(xacmlAuthzQueryType, e);
        }

        logger.debug("Invoke query on authz service " + authzServiceUrlStr);
        // Invoke remote methods
        XACMLAuthorizationServiceLocator locator = new XACMLAuthorizationServiceLocator();
        XACMLAuthorizationPortType xacmlPort;
        try {
            xacmlPort = locator.getXACMLAuthorizationPortTypePort(authzServiceUrl);
            if (xacmlPort instanceof XACMLAuthorizationPortTypeSOAPBindingStub)
                ((XACMLAuthorizationPortTypeSOAPBindingStub) xacmlPort).setTimeout(30000);
            //org.apache.axis.client.Stub stub = (org.apache.axis.client.Stub) xacmlPort;
            //stub._setProperty("org.globus.security.authorization", NoAuthorization.getInstance());
        } catch (ServiceException e) {
            logger.error(e);
            return getErrorResponse(xacmlAuthzQueryType, e);
        }

        logger.debug("XACMLAuthorizationPort received");

        XACMLAuthzDecisionQuery encodedQuery=null;
        try {
            encodedQuery = convertQuery(xacmlAuthzQueryType);
        } catch (MarshallingException me) {
            logger.error(me);
            return getErrorResponse(xacmlAuthzQueryType, me);
        } catch (URI.MalformedURIException mue) {
            logger.error(mue);
            return getErrorResponse(xacmlAuthzQueryType, mue);
        }

        Response response;

        // SAML 2 response
        try {
            response = xacmlPort.authorize(encodedQuery);
        } catch (RemoteException re) {
            logger.error(re);
            return getErrorResponse(xacmlAuthzQueryType, re);
        }

        return response;
    }


    static void addAttribs(List<AttributeType> attrs, String issuer, Map<String, String> attributes) {
        Set<String> keys = attributes.keySet();
        for (String key: keys) {
            AttributeType attributeType = getAttributeType(issuer, key, attributes.get(key));
            attrs.add(attributeType);
        }
    }

    static AttributeType getAttributeType(String issuer, String name, String value) {
        AttributeValueTypeImplBuilder attributeValueBuilder =
                (AttributeValueTypeImplBuilder) builderFactory
                        .getBuilder(AttributeValueType.DEFAULT_ELEMENT_NAME);

        AttributeTypeImplBuilder attributeBuilder = (AttributeTypeImplBuilder)
                builderFactory.getBuilder(AttributeType.DEFAULT_ELEMENT_NAME);

        AttributeValueType attributeValue = attributeValueBuilder.buildObject();
        attributeValue.setValue(value);
        AttributeType attributeType = attributeBuilder
                .buildObject(
                        org.opensaml.xacml.XACMLConstants.XACML20CTX_NS,
                        AttributeType.DEFAULT_ELEMENT_LOCAL_NAME,
                        org.opensaml.xacml.XACMLConstants.XACMLCONTEXT_PREFIX);
        attributeType.setAttributeID(name);
        attributeType.setIssuer(issuer);
        attributeType.setDataType(XACMLConstants.STRING_DATATYPE);
        attributeType.getAttributeValues().add(attributeValue);

        return attributeType;
    }


    private static XACMLAuthzDecisionQuery
    convertQuery(XACMLAuthzDecisionQueryType xacmlQueryType)
            throws MarshallingException, URI.MalformedURIException {

        MarshallerFactory factory = Configuration.getMarshallerFactory();
        Marshaller marshaller =
                factory.getMarshaller(XACMLAuthzDecisionQueryType.
                        TYPE_NAME_XACML20);
        Element authzQueryElement = marshaller.marshall(xacmlQueryType);

        logger.trace("The XACML element string:\n" +
                XMLUtils.ElementToString(authzQueryElement));

        NodeList responseChildren = authzQueryElement.getChildNodes();
        List<MessageElement> responseElements = new ArrayList<MessageElement>();
        for (int i = 0; i < responseChildren.getLength(); i++) {
            Node child = responseChildren.item(i);
            if (child instanceof Element) {
                responseElements.add(
                        new MessageElement((Element) child));
            }
        }

        XACMLAuthzDecisionQuery query = new XACMLAuthzDecisionQuery();
        query.set_any((MessageElement[]) responseElements.toArray(
                new MessageElement[responseElements.size()]));

        if (xacmlQueryType.getCombinePoliciesXSBooleanValue() != null) {
            query.setCombinePolicies(xacmlQueryType.
                    getCombinePoliciesXSBooleanValue().getValue());
        }
        if ((xacmlQueryType.getConsent() != null) &&
                (!xacmlQueryType.getConsent().trim().equals(""))) {
            query.setConsent(new URI(xacmlQueryType.getConsent()));
        }
        if ((xacmlQueryType.getDestination() != null &&
                (!xacmlQueryType.getDestination().trim().equals("")))) {
            query.setDestination(new URI(xacmlQueryType.getDestination()));
        }
        query.setID(new Id(xacmlQueryType.getID()));
        if (xacmlQueryType.getInputContextOnlyXSBooleanValue() != null) {
            query.setInputContextOnly(xacmlQueryType.
                    getInputContextOnlyXSBooleanValue().getValue());
        }

        if (xacmlQueryType.getIssueInstant() != null) {
            Calendar calendar = xacmlQueryType.getIssueInstant().
                    toCalendar(Locale.getDefault());
            query.setIssueInstant(calendar);
        }

        if (xacmlQueryType.getReturnContextXSBooleanValue() != null) {
            query.setReturnContext(xacmlQueryType.
                    getReturnContextXSBooleanValue().getValue());
        }

        if (xacmlQueryType.getVersion() != null) {
            query.setVersion(xacmlQueryType.getVersion().toString());
        }
        return query;
    }

    /**
     * Method to create an Attribute Value with a string value.
     *
     * @param value String to set as attribute values
     * @return Attribute Value Type with the string.
     */
    public static AttributeValueType getStringAttributeValue(String value) {

        AttributeValueTypeImplBuilder attributeValueBuilder =
                (AttributeValueTypeImplBuilder) builderFactory
                        .getBuilder(AttributeValueType.DEFAULT_ELEMENT_NAME);

        AttributeValueType attributeValue = attributeValueBuilder
                .buildObject();
        attributeValue.setValue(value);
        return attributeValue;
    }


    /**
     * Method to get an attribute type.
     *
     * @param id attribute id
     * @param issuer Issuer of attribute
     * @param dataType Attribute value data type
     * @param attributes Array of Attribute Value Type.
     *
     * @return Attribute Type
     */
    public static AttributeType
    getAttributeType(String id, String issuer, String dataType,
                     Vector<AttributeValueType> attributes) {

        AttributeTypeImplBuilder attributeBuilder = (AttributeTypeImplBuilder)
                builderFactory.getBuilder(AttributeType.DEFAULT_ELEMENT_NAME);
        AttributeType attributeType = attributeBuilder
                .buildObject(org.opensaml.xacml.XACMLConstants.XACML20CTX_NS,
                        AttributeType.DEFAULT_ELEMENT_LOCAL_NAME,
                        org.opensaml.xacml.XACMLConstants.
                                XACMLCONTEXT_PREFIX);

        attributeType.setAttributeID(id);
        attributeType.setIssuer(issuer);
        attributeType.setDataType(dataType);


        attributeType.getAttributeValues().addAll(attributes);

        return attributeType;
    }

    /**
     * Constructs the subject piece of the XACML Authz Query.
     *
     * @return SubjectType
     */
    public static SubjectType getSubjectType() {
        // Subject Type
        SubjectTypeImplBuilder builder =
                (SubjectTypeImplBuilder) builderFactory.
                        getBuilder(SubjectType.DEFAULT_ELEMENT_NAME);
        return  builder.buildObject();
    }

    /**
     * Constructs the subject piece of the XACML Authz Query, including the specified string attributes.
     *
     * @param issuer Issuer of attribute
     * @param subjectAttribs Array of Attribute Value Type.
     *
     * @return SubjectType
     */
    public static SubjectType getSubjectType(Map<String, String> subjectAttribs, String issuer) {
        // Construct the subject object
        logger.debug("Adding subject attributes");
        SubjectType subject = getSubjectType();
        List<AttributeType> subjAttrs = subject.getAttributes();
        addAttribs(subjAttrs, issuer, subjectAttribs);
        return subject;
    }

    /**
     * Constructs the subject piece of the XACML Authz Query, with attributes of subject and VO.
     *
     * @param issuer Issuer of attribute
     *
     * @return SubjectType
     */
    public SubjectType getSubjectType(String issuer) {
        LinkedHashMap<String,String> subjectAttribs = new LinkedHashMap<String, String>();
        if(X509Subject!=null) {
            subjectAttribs.put(XACMLConstants.SUBJECT_X509_ID, X509Subject);
        }
        if(CondorCanonicalNameID!=null) {
            subjectAttribs.put(XACMLConstants.SUBJECT_CONDOR_CANONICAL_NAME_ID, CondorCanonicalNameID);
        }
        if(X509SubjectIssuer!=null) {
            subjectAttribs.put(XACMLConstants.SUBJECT_X509_ISSUER, X509SubjectIssuer);
        }
        if(VO!=null) {
            subjectAttribs.put(XACMLConstants.SUBJECT_VO_ID, VO);
        }
        if(VOMSSigningSubject!=null) {
            subjectAttribs.put(SUBJECT_VOMS_SIGNING_SUBJECT_ID, VOMSSigningSubject);
        }
        if(VOMSSigningIssuer!=null) {
            subjectAttribs.put(XACMLConstants.SUBJECT_VOMS_SIGNING_ISSUER_ID, VOMSSigningIssuer);
        }
        if(fqan!=null) {
            subjectAttribs.put(XACMLConstants.SUBJECT_VOMS_PRIMARY_FQAN_ID, fqan);
        }
        if(CertificateSerialNumber!=null) {
            subjectAttribs.put(XACMLConstants.SUBJECT_CERTIFICATE_SERIAL_NUMBER_ID, CertificateSerialNumber);
        }
        if(CertificateChainNotBefore!=null) {
            subjectAttribs.put(XACMLConstants.SUBJECT_CERTIFICATE_NOT_BEFORE_ID, CertificateChainNotBefore);
        }
        if(CertificateChainNotAfter!=null) {
            subjectAttribs.put(XACMLConstants.SUBJECT_CERTIFICATE_NOT_AFTER_ID, CertificateChainNotAfter);
        }
        if(CASerialNumber!=null) {
            subjectAttribs.put(XACMLConstants.SUBJECT_CA_SERIAL_NUMBER_ID, CASerialNumber);
        }
        if(VOMS_DNS_Port!=null) {
            subjectAttribs.put(XACMLConstants.SUBJECT_VOMS_DNS_PORT_ID, VOMS_DNS_Port);
        }
        if(CertificatePoliciesOIDs!=null) {
            subjectAttribs.put(XACMLConstants.SUBJECT_CA_POLICY_OID_ID, CertificatePoliciesOIDs);
        }
        if(CertificateChain!=null) {
            subjectAttribs.put(XACMLConstants.SUBJECT_CERT_CHAIN_ID, CertificateChain);
        }

        SubjectType subject = getSubjectType(subjectAttribs, issuer);
        if(fqan!=null) {
            addSubjectFQAN(fqan, subject, issuer);
        }
        return subject;
    }

    /**
     * Add FQAN to the subject attributes, including subgroups
     *
     */
    public static void addSubjectFQAN(String fqan, SubjectType subject, String issuer) {
        if(fqan==null) return;

        List<AttributeType> subjAttrs = subject.getAttributes();

        // Gather the values already written
        Set<String> fqanVals = new LinkedHashSet<String>();
        for (AttributeType attr : subjAttrs) {
            if(attr.getAttributeID().equals(XACMLConstants.SUBJECT_VOMS_FQAN_ID)) {
                for(AttributeValueType fqanVal : attr.getAttributeValues()) {
                    fqanVals.add(fqanVal.getValue());
                }
            }
        }

        // Work with normalized fqan
        FQAN myFQAN = new FQAN(fqan);
        String group = myFQAN.getGroup();
        String role = myFQAN.getRole();
        role = ("".equals(role)) ? "NULL" : role;
        String capability = myFQAN.getCapability();
        capability = ("".equals(capability)) ? "NULL" : capability;
        String value = myFQAN.getGroup() + "/Role=" + role + "/Capability=" + capability;

        // Add the FQAN if not already added
        if(!fqanVals.contains(value)) {
            AttributeType attributeType = getAttributeType(issuer, XACMLConstants.SUBJECT_VOMS_FQAN_ID, value);
            subjAttrs.add(attributeType);
            fqanVals.add(value);
        }

        // Add the implied subgroups as voms-fqan's
        Stack <String> groupStack = new Stack <String> ();
        String subgroup = "";
        StringTokenizer st = new StringTokenizer(group, "/");
        while (st.hasMoreTokens()) {
            subgroup = subgroup + "/" + st.nextToken();
            groupStack.add(subgroup);
        }

        while (!groupStack.empty()) {
            subgroup = groupStack.pop();
            value = subgroup + "/Role=NULL/Capability=NULL";
            if(!fqanVals.contains(value)) {
                AttributeType attributeType = getAttributeType(issuer, XACMLConstants.SUBJECT_VOMS_FQAN_ID, value);
                subjAttrs.add(attributeType);
                fqanVals.add(value);
            }
        }
    }

    /**
     * Constructs the resource piece of the XACML Authz Query, including the specified string attributes.
     *
     * @return ResourceType
     */
    public static ResourceType getResourceType(Map resourceAttribs, String issuer) {
        // Construct the resource object
        logger.debug("Adding resource attributes");
        ResourceTypeImplBuilder builder =
                (ResourceTypeImplBuilder) builderFactory.
                        getBuilder(ResourceType.DEFAULT_ELEMENT_NAME);
        ResourceType resource = builder.buildObject();
        List<AttributeType> resAttrs = resource.getAttributes();
        addAttribs(resAttrs, issuer, resourceAttribs);
        return resource;
    }

    /**
     * Constructs the resource piece of the XACML Authz Query, with attribute of resource host name.
     *
     * @return ResourceType
     */
    public ResourceType getResourceType(String issuer) {
        LinkedHashMap resourceAttribs = new LinkedHashMap<String, String>();
        if (resourceType!=null) {
            resourceAttribs.put(XACMLConstants.RESOURCE_ID, resourceType);
        }
        if (resourceDNSHostName!=null) {
            resourceAttribs.put(XACMLConstants.RESOURCE_DNS_HOST_NAME_ID, resourceDNSHostName);
        }
        if (resourceX509ID!=null) {
            resourceAttribs.put(XACMLConstants.RESOURCE_X509_ID, resourceX509ID);
        }
        if (resourceType!=null) {
            resourceAttribs.put(XACMLConstants.RESOURCE_X509_ISSUER_ID, resourceX509Issuer);
        }
        return  getResourceType(resourceAttribs, issuer);
    }

    /**
     * Method to construct action type
     *
     * @return  ActionType
     */
    public static ActionType getActionType() {

        return ((ActionTypeImplBuilder) builderFactory.
                getBuilder(ActionType.DEFAULT_ELEMENT_NAME)).buildObject();
    }
    /**
     * Constructs the action piece of the XACML Authz Query, including the specified string attributes.
     *
     * @return ActionType
     */
    public static ActionType getActionType(Map actionAttribs, String issuer) {
        // Construct the action object
        logger.debug("Adding action attributes");
        ActionType action = getActionType();
        List<AttributeType> actAttrs = action.getAttributes();
        addAttribs(actAttrs, issuer, actionAttribs);
        return action;
    }

    /**
     * Constructs the resource piece of the XACML Authz Query, with the specified action as an attribute.
     *
     * @return v
     */
    public ActionType getActionType(String issuer) {
        LinkedHashMap actionAttribs = new LinkedHashMap<String, String>();
        if (requestedaction!=null) {
            actionAttribs.put(XACMLConstants.ACTION_ID, requestedaction);
        }
        if (RSL_string!=null) {
            actionAttribs.put(XACMLConstants.ACTION_RSL_STRING, RSL_string);
        }
        return getActionType(actionAttribs, issuer);
    }

    /**
     * Creates a EnvironmentType
     *
     * @return EnvironmentType
     */
    public static EnvironmentType getEnvironmentType() {

        EnvironmentTypeImplBuilder builder =
                (EnvironmentTypeImplBuilder) builderFactory.
                        getBuilder(EnvironmentType.DEFAULT_ELEMENT_NAME);
        return builder.buildObject();
    }

    /**
     * Constructs the environment piece of the XACML Authz Query, including the specified string attributes.
     *
     * @return EnvironmentType
     */
    public static EnvironmentType getEnvironmentType(Map environmentAttribs, String issuer) {
        // Construct the environment object
        logger.debug("Adding environment attributes");
        EnvironmentType environment = getEnvironmentType();
        List<AttributeType> envAttrs = environment.getAttributes();
        addAttribs(envAttrs, issuer, environmentAttribs);
        return environment;
    }

    /**
     * Constructs the environment piece of the XACML Authz Query with supported obligations as attribute vector
     * EnvironmentType
     *
     * @param obligationIds Obligation ids to add as attribute
     * @param issuer Identity of issuer of attributes
     */
    public static EnvironmentType getEnvironmentType( Vector<String> obligationIds, String issuer) {

        // Construct the environment object
        logger.debug("Adding environment attributes");
        EnvironmentType environment = getEnvironmentType();

        if ((obligationIds == null) || (obligationIds.size() < 1)) {
            logger.warn("Obligation ids are null or empty");
            return environment;
        }

        for (int i = 0; i < obligationIds.size(); i++) {
            Vector<AttributeValueType> attributeValues = new Vector(1);
            attributeValues.add(getStringAttributeValue(obligationIds.get(i)));

            AttributeType envAttribute =
                    getAttributeType(XACMLConstants.SUPPORTED_OBLIGATIONS, issuer,
                            XACMLConstants.STRING_DATATYPE,
                            attributeValues);

            environment.getAttributes().add(envAttribute);
        }

        return environment;
    }

    /**
     * Create RequestType given subject, resource, action ad environemnt.
     *
     * @param subject
     * @param resource
     * @param action
     * @param environmentType
     * @return
     */
    public static RequestType getRequest(SubjectType subject,
                                         ResourceType resource,
                                         ActionType action,
                                         EnvironmentType environmentType) {

        RequestTypeImplBuilder requestBuilder = (RequestTypeImplBuilder)
                builderFactory.getBuilder(RequestType.DEFAULT_ELEMENT_NAME);
        RequestType request = requestBuilder.buildObject();
        request.getSubjects().add(subject);
        request.getResources().add(resource);
        request.setAction(action);
        request.setEnvironment(environmentType);
        return request;
    }

    /**
     * Create XACMLAuthz DecisionQuery Type
     *
     * @param subjectType
     * @param resourceType
     * @param actionType
     * @param envType
     * @param inputContextOnly
     * @param returnContext
     * @param combineLocalPolicy
     * @param issuer
     * @return
     * @throws java.security.NoSuchAlgorithmException
     */
    public static XACMLAuthzDecisionQueryType
    getQuery(SubjectType subjectType, ResourceType resourceType,
             ActionType actionType, EnvironmentType envType,
             boolean inputContextOnly, boolean returnContext,
             boolean combineLocalPolicy, String issuer)
            throws NoSuchAlgorithmException {

        XMLObjectBuilderFactory builderFactory =
                Configuration.getBuilderFactory();

        XACMLAuthzDecisionQueryTypeImplBuilder xacmlDecisionQueryBuilder =
                (XACMLAuthzDecisionQueryTypeImplBuilder)
                        builderFactory.getBuilder(XACMLAuthzDecisionQueryType
                                .DEFAULT_ELEMENT_NAME_XACML20);

        XACMLAuthzDecisionQueryType xacmlQuery =
                xacmlDecisionQueryBuilder
                        .buildObject(SAMLProfileConstants.SAML20XACML20P_NS,
                                XACMLAuthzDecisionQueryType.DEFAULT_ELEMENT_LOCAL_NAME,
                                SAMLProfileConstants.SAML20XACMLPROTOCOL_PREFIX);

        //Set the needed elements
        XSBooleanValue inputContextXS = new XSBooleanValue();
        inputContextXS.setValue(inputContextOnly);
        xacmlQuery.setInputContextOnly(inputContextXS);

        XSBooleanValue returnContextXS = new XSBooleanValue();
        returnContextXS.setValue(returnContext);
        xacmlQuery.setInputContextOnly(returnContextXS);

        XSBooleanValue combineLocalPolicyXS = new XSBooleanValue();
        combineLocalPolicyXS.setValue(combineLocalPolicy);
        xacmlQuery.setCombinePolicies(combineLocalPolicyXS);

        // create request
        RequestType request = getRequest(subjectType,
                resourceType,
                actionType, envType);

        IssuerBuilder issuerBuilder = (IssuerBuilder) builderFactory.
                getBuilder(Issuer.DEFAULT_ELEMENT_NAME);
        Issuer issuerType = issuerBuilder.buildObject();
        issuerType.setValue(issuer);

        // ID
        SecureRandomIdentifierGenerator generator =
                new SecureRandomIdentifierGenerator();
        xacmlQuery.setID(generator.generateIdentifier());
        xacmlQuery.setIssuer(issuerType);
        xacmlQuery.setVersion(org.opensaml.common.SAMLVersion.VERSION_20);
        xacmlQuery.setRequest(request);

        return xacmlQuery;
    }

    public Response getErrorResponse(XACMLAuthzDecisionQueryType xacmlAuthzQueryType, Exception e) {
        Response response=null;
        XACMLAuthzDecisionStatementType indeterminate = getIndeterminateDecision();

        setXACMLStatementResponse(indeterminate, xacmlAuthzQueryType);
        setXACMLStatementStatus(indeterminate, Status.STATUS_PROCESSING_ERROR, e.getMessage());
        try {
            response = convertToAuthz(indeterminate);
        }  catch (SAMLException se) {
            logger.error(se);
        }

        return response;
    }

    XACMLAuthzDecisionStatementType getIndeterminateDecision() {
        XACMLAuthzDecisionStatementType xacmlAuthz=null;

        // build Decision type
        DecisionTypeImplBuilder decisionBuilder =
                (DecisionTypeImplBuilder) builderFactory.getBuilder(DecisionType.DEFAULT_ELEMENT_NAME);
        DecisionType decision = decisionBuilder.buildObject();
        decision.setDecision(DecisionType.DECISION.Indeterminate);

        // Construct XAML Authz Decision Statement
        XACMLAuthzDecisionStatementTypeImplBuilder xacmlAuthzBuilder =
                (XACMLAuthzDecisionStatementTypeImplBuilder) builderFactory.
                        getBuilder(XACMLAuthzDecisionStatementType.TYPE_NAME_XACML20);
        xacmlAuthz = xacmlAuthzBuilder.buildObject(Statement.DEFAULT_ELEMENT_NAME,
                XACMLAuthzDecisionStatementType.TYPE_NAME_XACML20);

        // Construct XACML Response
        ResponseTypeImplBuilder builder =
                (ResponseTypeImplBuilder) builderFactory.getBuilder(ResponseType.DEFAULT_ELEMENT_NAME);
        ResponseType response = builder.buildObject();

        // build Result type
        ResultTypeImplBuilder resultBuilder =
                (ResultTypeImplBuilder) builderFactory.getBuilder(ResultType.DEFAULT_ELEMENT_NAME);
        ResultType result = resultBuilder.buildObject();
        result.setDecision(decision);

        response.setResult(result);
        xacmlAuthz.setResponse(response);

        return xacmlAuthz;
    }

    void setXACMLStatementResponse(XACMLAuthzDecisionStatementType xacmlAuthz, XACMLAuthzDecisionQueryType xacmlQuery) {
        if( xacmlQuery!=null) {
            RequestType request = xacmlQuery.getRequest();
            if ((xacmlQuery.getReturnContextXSBooleanValue() != null)
                    && (xacmlQuery.getReturnContextXSBooleanValue().getValue())) {
                logger.debug("Adding query request");
                request.releaseDOM();
                request.detach();
                xacmlAuthz.setRequest(request);
            }
        }
    }

    void setXACMLStatementStatus(XACMLAuthzDecisionStatementType xacmlAuthz, String code, String msg) {

        // Construct XACML Status
        StatusTypeImplBuilder statusbuilder =
                (StatusTypeImplBuilder) builderFactory.getBuilder(StatusType.DEFAULT_ELEMENT_NAME);
        StatusType status = statusbuilder.buildObject();

        // Construct XACML StatusCode
        StatusCodeTypeImplBuilder statuscodebuilder =
                (StatusCodeTypeImplBuilder) builderFactory.getBuilder(StatusCodeType.DEFAULT_ELEMENT_NAME);
        StatusCodeType statuscode = statuscodebuilder.buildObject();

        StatusMessageTypeImplBuilder statusmessagebuilder = new StatusMessageTypeImplBuilder();
        StatusMessageType statusmessage = statusmessagebuilder.buildObject(
                org.opensaml.xacml.XACMLConstants.XACML20CTX_NS,
                StatusMessageType.DEFAULT_ELEMENT_LOCAL_NAME,
                org.opensaml.xacml.XACMLConstants.XACMLCONTEXT_PREFIX);
        // Construct XACML StatusCode
        XSStringBuilder stringbuilder =
                (XSStringBuilder) builderFactory.getBuilder(XSString.TYPE_NAME);

        statuscode.setValue(code);
        status.setStatusCode(statuscode);
        statusmessage.setValue(msg);
        status.setStatusMessage(statusmessage);

        xacmlAuthz.getResponse().getResult().setStatus(status);
    }


    public Response convertToAuthz(XACMLAuthzDecisionStatementType statement) throws SAMLException {
        XMLObjectBuilder assertionBuilder = org.opensaml.xml.Configuration.getBuilderFactory().getBuilder(Assertion.DEFAULT_ELEMENT_NAME);
        Assertion assertion = (Assertion)assertionBuilder.buildObject(Assertion.DEFAULT_ELEMENT_NAME);
        assertion.getStatements().add(statement);

        XMLObjectBuilder responseBuilder = org.opensaml.xml.Configuration.getBuilderFactory().getBuilder(org.opensaml.saml2.core.Response.DEFAULT_ELEMENT_NAME);
        org.opensaml.saml2.core.Response saml2Response = (org.opensaml.saml2.core.Response)responseBuilder.buildObject(org.opensaml.saml2.core.Response.DEFAULT_ELEMENT_NAME);
        saml2Response.getAssertions().add(assertion);

        // Convert response to Element
        MarshallerFactory factory = org.opensaml.xml.Configuration.getMarshallerFactory();
        Marshaller marshaller =
                factory.getMarshaller(org.opensaml.saml2.core.Response.DEFAULT_ELEMENT_NAME);
        Element saml2ResponseElement;
        try {
            saml2ResponseElement = marshaller.marshall(saml2Response);
        } catch (MarshallingException e) {
            logger.error("marshalling exception", e);
            throw new SAMLException("Marshalling exception", e);
        }

        // Convert to AuthzResponseType
        NodeList responseChildren = saml2ResponseElement.getChildNodes();
        List responseElements = new ArrayList();
        for (int i = 0; i < responseChildren.getLength(); i++) {
            Node child = responseChildren.item(i);
            if (child instanceof Element) {
                responseElements.add(new MessageElement((Element) child));
            }
        }

        org.opensciencegrid.authz.xacml.stubs.Response returnResponse = new org.opensciencegrid.authz.xacml.stubs.Response();
        returnResponse.set_any((MessageElement[])responseElements.toArray(new MessageElement[responseElements.size()]));
        try {
            if ((saml2Response.getConsent() != null) && (!saml2Response.getConsent().equals(""))) {
                returnResponse.setConsent(new URI(saml2Response.getConsent()));
            }
            if ((saml2Response.getDestination() != null) && (!saml2Response.getDestination().equals(""))) {
                returnResponse.setDestination(new URI(saml2Response.getDestination()));
            }
        } catch (URI.MalformedURIException e) {
            logger.error(e);
            throw new SAMLException("Error converting to URI", e);
        }

        if ((saml2Response.getID() != null) && (!saml2Response.getID().equals(""))) {
            returnResponse.setID(new Id(saml2Response.getID()));
        }
        if ((saml2Response.getInResponseTo() != null) && (!saml2Response.getInResponseTo().equals(""))) {
            returnResponse.setInResponseTo(new NCName(saml2Response.getInResponseTo()));
        }
        if (saml2Response.getIssueInstant() != null) {
            returnResponse.setIssueInstant(saml2Response.getIssueInstant().toCalendar(Locale.getDefault()));
        }
        if (saml2Response.getVersion() != null) {
            returnResponse.setVersion(saml2Response.getVersion().toString());
        }

        return returnResponse;
    }


    public XACMLAuthzDecisionStatementType convertToXACML(Response resp) throws Exception {
        Element samlassertion=null;
        MessageElement messelt=null;
        try {
            MessageElement[] resparray = resp.get_any();
            for (MessageElement melt : resparray) {
                Name meltname = melt.getElementName();
                if (!(meltname instanceof PrefixedQName)) continue;
                if(meltname.getLocalName().equals(classname.getLocalName()) &&
                        meltname.getURI().equals(classname.getURI())) {
                    messelt = melt;
                    samlassertion = messelt.getAsDOM();
                    break;
                }
            }
        } catch (Exception e) {
            Exception ex = new Exception("Error converting to query element ", e);
            logger.error(e);
            throw ex;
        }

        if(samlassertion==null) return null;//todo throw exception
        Element samlstmt = (Element) samlassertion.getElementsByTagNameNS("urn:oasis:names:tc:SAML:2.0:assertion", "Statement").item(0);

        if (XMLHelper.hasXSIType(samlstmt)) {
            Attr attribute = samlstmt.getAttributeNodeNS(XMLConstants.XSI_NS, "type");
            String attributeValue = attribute.getTextContent().trim();
            StringTokenizer tokenizer = new StringTokenizer(attributeValue, ":");
            String prefix = null;
            String localPart;
            if (tokenizer.countTokens() > 1) {
                prefix = tokenizer.nextToken();
                localPart = tokenizer.nextToken();
            } else {
                localPart = tokenizer.nextToken();
            }

            if(prefix!=null) {

                String ns = samlstmt.lookupNamespaceURI(prefix);
                if(ns==null) {
                    String nsuri = messelt.getDeserializationContext().getNamespaceURI(prefix);
                    if(nsuri!=null) {
                        samlstmt.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:"+prefix, nsuri);
                    }

                }
            }
        }

        String specifiedPrefix="XACMLassertion";
        String namespace;
        if (samlstmt.hasAttributes()) {
            NamedNodeMap map = samlstmt.getAttributes();
            int length = map.getLength();
            for (int i=0;i<length;i++) {
                Node attr = map.item(i);
                String attrPrefix = attr.getPrefix();
                String value = attr.getNodeValue();
                namespace = attr.getNamespaceURI();
                if (namespace !=null && namespace.equals("http://www.w3.org/2000/xmlns/")) {
                    // at this point we are dealing with DOM Level 2 nodes only
                    if (specifiedPrefix == null &&
                            attr.getNodeName().equals("xmlns")) {
                        // default namespace
                        System.out.println(value);
                    } else if (attrPrefix !=null &&
                            attrPrefix.equals("xmlns") &&
                            attr.getLocalName().equals(specifiedPrefix)) {
                        // non default namespace
                        System.out.println(value);
                    }
                }
            }
        }

        Element samlresp = (Element) samlstmt.getElementsByTagNameNS("urn:oasis:names:tc:xacml:2.0:context:schema:os", "Response").item(0);


        // Construct XACMLAuthorizationDecisionQuery from Element
        UnmarshallerFactory marshallerFactory = org.opensaml.xml.Configuration.getUnmarshallerFactory();
        Unmarshaller responseUnmarshaller =
                marshallerFactory.getUnmarshaller(XACMLAuthzDecisionStatementType.DEFAULT_ELEMENT_NAME_XACML20);
        XACMLAuthzDecisionStatementType statement=null;
        try {
            statement = (XACMLAuthzDecisionStatementType)responseUnmarshaller.unmarshall(samlstmt);
        } catch (UnmarshallingException e) {
            Exception ex = new Exception("Unmarshall failed ", e);
            logger.error(e);
            throw ex;
        }

        return statement;
    }

    public String getX509Subject() {
        return X509Subject;
    }

    public void setX509Subject(String x509Subject) {
        X509Subject = x509Subject;
    }

    public String getCondorCanonicalNameID() {
        return CondorCanonicalNameID;
    }

    public void setCondorCanonicalNameID(String condorCanonicalNameID) {
        CondorCanonicalNameID = condorCanonicalNameID;
    }

    public String getX509SubjectIssuer() {
        return X509SubjectIssuer;
    }

    public void setX509SubjectIssuer(String x509SubjectIssuer) {
        X509SubjectIssuer = x509SubjectIssuer;
    }

    public String getVO() {
        return VO;
    }

    public void setVO(String VO) {
        this.VO = VO;
    }

    public String getVOMSSigningSubject() {
        return VOMSSigningSubject;
    }

    public void setVOMSSigningSubject(String VOMSSigningSubject) {
        this.VOMSSigningSubject = VOMSSigningSubject;
    }

    public String getVOMSSigningIssuer() {
        return VOMSSigningIssuer;
    }

    public void setVOMSSigningIssuer(String VOMSSigningIssuer) {
        this.VOMSSigningIssuer = VOMSSigningIssuer;
    }

    public String getFqan() {
        return fqan;
    }

    public void setFqan(String fqan) {
        this.fqan = fqan;
    }

    public String getCertificateSerialNumber() {
        return CertificateSerialNumber;
    }

    public void setCertificateSerialNumber(String certificateSerialNumber) {
        CertificateSerialNumber = certificateSerialNumber;
    }

    public String getCertificateChainNotBefore() {
        return CertificateChainNotBefore;
    }

    public void setCertificateChainNotBefore(String certificateChainNotBefore) {
        CertificateChainNotBefore = certificateChainNotBefore;
    }

    public String getCertificateChainNotAfter() {
        return CertificateChainNotAfter;
    }

    public void setCertificateChainNotAfter(String certificateChainNotAfter) {
        CertificateChainNotAfter = certificateChainNotAfter;
    }

    public String getCASerialNumber() {
        return CASerialNumber;
    }

    public void setCASerialNumber(String CASerialNumber) {
        this.CASerialNumber = CASerialNumber;
    }

    public String getVOMS_DNS_Port() {
        return VOMS_DNS_Port;
    }

    public void setVOMS_DNS_Port(String VOMS_DNS_Port) {
        this.VOMS_DNS_Port = VOMS_DNS_Port;
    }

    public String getCertificatePoliciesOIDs() {
        return CertificatePoliciesOIDs;
    }

    public void setCertificatePoliciesOIDs(String certificatePoliciesOIDs) {
        CertificatePoliciesOIDs = certificatePoliciesOIDs;
    }

    public String getCertificateChain() {
        return CertificateChain;
    }

    public void setCertificateChain(String certificateChain) {
        CertificateChain = certificateChain;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public String getResourceDNSHostName() {
        return resourceDNSHostName;
    }

    public void setResourceDNSHostName(String resourceDNSHostName) {
        this.resourceDNSHostName = resourceDNSHostName;
    }

    public String getResourceX509ID() {
        return resourceX509ID;
    }

    public void setResourceX509ID(String resourceX509ID) {
        this.resourceX509ID = resourceX509ID;
    }

    public String getResourceX509Issuer() {
        return resourceX509Issuer;
    }

    public void setResourceX509Issuer(String resourceX509Issuer) {
        this.resourceX509Issuer = resourceX509Issuer;
    }

    public String getRequestedaction() {
        return requestedaction;
    }

    public void setRequestedaction(String requestedaction) {
        this.requestedaction = requestedaction;
    }

    public String getRSL_string() {
        return RSL_string;
    }

    public void setRSL_string(String RSL_string) {
        this.RSL_string = RSL_string;
    }

}