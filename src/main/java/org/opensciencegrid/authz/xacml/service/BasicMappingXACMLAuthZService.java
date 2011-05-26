/*
 * BasicMappingAuthZService.java
 *
 */

package org.opensciencegrid.authz.xacml.service;

import java.util.ArrayList;
import java.util.Locale;
import java.util.List;

import java.rmi.RemoteException;

import org.opensaml.xml.io.Marshaller;
import org.opensaml.xml.io.MarshallerFactory;
import org.opensaml.xml.io.MarshallingException;
import org.opensaml.xml.io.Unmarshaller;
import org.opensaml.xml.io.UnmarshallerFactory;
import org.opensaml.xml.io.UnmarshallingException;
import org.opensaml.xml.Configuration;
import org.opensaml.xml.XMLObjectBuilder;
import org.opensaml.xacml.ctx.RequestType;
import org.opensaml.xacml.ctx.StatusMessageType;
import org.opensaml.xacml.ctx.impl.StatusMessageTypeImplBuilder;
import org.opensaml.xacml.profile.saml.XACMLAuthzDecisionQueryType;
import org.opensaml.xacml.profile.saml.XACMLAuthzDecisionStatementType;
import org.opensaml.saml2.core.Response;
import org.opensaml.saml2.core.Assertion;

import org.apache.axis.types.URI;
import org.apache.axis.types.Id;
import org.apache.axis.types.NCName;
import org.apache.axis.message.MessageElement;
import org.apache.axis.utils.XMLUtils;
import org.apache.axis.AxisFault;

import org.apache.log4j.Logger;

import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

import org.opensciencegrid.authz.xacml.stubs.XACMLAuthorizationPortTypeSOAPBindingImpl;
import org.opensciencegrid.authz.xacml.stubs.XACMLAuthzDecisionQuery;
import org.opensciencegrid.authz.xacml.common.OSGSAMLBootstrap;

/** Implements the XACML AuthZ service as a simple mapping service.
 * Receives a SAML authorization requests and queries an XACML
 * Mapping Service to decide how to perform the GRID identity to local
 * identity mapping.
 *
 * @author Jay Packard
 */
public class BasicMappingXACMLAuthZService extends XACMLAuthorizationPortTypeSOAPBindingImpl {
    //##########################################################################################
    // Members

    /** the mapService used to perform the identity mapping*/
    XACMLMappingService mapService;

    /** the identity/name/dn of this service */
    protected String serviceIdentity = "BasicMappingXACMLAuthZService";

    /** Log4Java logger */
    static Logger logger = Logger.getLogger(BasicMappingXACMLAuthZService.class.getName() );

    private static UnmarshallerFactory unMarshallerFactory = null;
    private static MarshallerFactory marshallerFactory = null;

    static {
        try {
            org.apache.xml.security.Init.init();
            OSGSAMLBootstrap.bootstrap();
        } catch (Exception e) {
            String err = "xacmlInitFailed";
            logger.error(err, e);
            throw new RuntimeException(err, e);
        }
    }

    // Methods

    /** Creates a SAML AuthZ service from a GRID Identity Mapping Service.*/
    public BasicMappingXACMLAuthZService(XACMLMappingService mapService) {
        this.mapService = mapService;
        unMarshallerFactory = Configuration.getUnmarshallerFactory();
        marshallerFactory = Configuration.getMarshallerFactory();
    }

    public org.opensciencegrid.authz.xacml.stubs.Response authorize(XACMLAuthzDecisionQuery authzDecisionQuery) throws RemoteException {
        // Convert input into Element
        Element queryElement;
        try {
            queryElement = ((MessageElement)authzDecisionQuery.get_any()[0].getParentElement()).getAsDOM();
        } catch (Exception e) {
            logger.error(e);
            throw new RemoteException("Error converting to query element", e);
        }

        // Construct XACMLAuthorizationDecisionQuery from Element
        UnmarshallerFactory marshallerFactory = Configuration.getUnmarshallerFactory();
        Unmarshaller requestUnmarshaller = marshallerFactory.getUnmarshaller(XACMLAuthzDecisionQueryType.DEFAULT_ELEMENT_NAME_XACML20);
        XACMLAuthzDecisionQueryType queryRequest;
        try {
            queryRequest = (XACMLAuthzDecisionQueryType)requestUnmarshaller.unmarshall(queryElement);
            logger.debug("XACMLAuthzDecisionQueryType object received: "+XMLUtils.ElementToString(queryRequest.getDOM()));
        } catch (UnmarshallingException e) {
            logger.error(e);
            throw new RemoteException("Unmarshall failed", e);
        }

        // Get statement from mapping service
        XACMLAuthzDecisionStatementType xacmlAuthz=null;
        try {
            xacmlAuthz = mapService.mapCredentials(queryRequest);
        } catch (Exception e) {
            logger.error(e);

            // Build StatusMessageType
            StatusMessageTypeImplBuilder statusMessageBuilder = (StatusMessageTypeImplBuilder)Configuration.getBuilderFactory().getBuilder(StatusMessageType.DEFAULT_ELEMENT_NAME);
            StatusMessageType statusMessage = statusMessageBuilder.buildObject();
            statusMessage.setValue(e.getMessage());

            // Marshal to Element
            MarshallerFactory factory = Configuration.getMarshallerFactory();
            Marshaller marshaller = factory.getMarshaller(StatusMessageType.DEFAULT_ELEMENT_NAME);
            Element statusMessageElement;
            try {
                statusMessageElement = marshaller.marshall(statusMessage);
            } catch (MarshallingException e2) {
                logger.error("marshalling exception", e2);
                throw new RemoteException(e.getMessage());
            }

            // Create and throw AxisFault
            AxisFault axisFault = new AxisFault(
                    StatusMessageType.DEFAULT_ELEMENT_NAME,
                    e.getLocalizedMessage(),
                    "GUMS",
                    new Element[]{statusMessageElement});
            throw axisFault;
        }

        RequestType request = queryRequest.getRequest();
        if ((queryRequest.getReturnContextXSBooleanValue() != null)
                && (queryRequest.getReturnContextXSBooleanValue().getValue())) {
            request.releaseDOM();
            request.detach();
            xacmlAuthz.setRequest(request);
        }

        XMLObjectBuilder assertionBuilder = Configuration.getBuilderFactory().getBuilder(Assertion.DEFAULT_ELEMENT_NAME);
        Assertion assertion = (Assertion)assertionBuilder.buildObject(Assertion.DEFAULT_ELEMENT_NAME);
        assertion.getStatements().add(xacmlAuthz);

        XMLObjectBuilder responseBuilder = Configuration.getBuilderFactory().getBuilder(Response.DEFAULT_ELEMENT_NAME);
        Response saml2Response = (Response)responseBuilder.buildObject(Response.DEFAULT_ELEMENT_NAME);
        saml2Response.getAssertions().add(assertion);
        saml2Response.setInResponseTo(queryRequest.getID());

        // Convert SAML response to Element
        MarshallerFactory factory = Configuration.getMarshallerFactory();
        Marshaller marshaller = factory.getMarshaller(Response.DEFAULT_ELEMENT_NAME);
        Element saml2ResponseElement;
        try {
            saml2ResponseElement = marshaller.marshall(saml2Response);
            logger.debug("Response object returned: "+XMLUtils.ElementToString(saml2ResponseElement));
        } catch (MarshallingException e) {
            logger.error("marshalling exception", e);
            throw new RemoteException("Marshalling exception", e);
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

        // Build return Response
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
            throw new RemoteException("Error converting to URI", e);
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

}

