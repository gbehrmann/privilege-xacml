package org.opensciencegrid.authz.xacml.service;


import org.opensaml.xacml.profile.saml.XACMLAuthzDecisionStatementType;
import org.opensaml.xacml.profile.saml.XACMLAuthzDecisionQueryType;
import org.opensaml.xacml.profile.saml.impl.XACMLAuthzDecisionStatementTypeImplBuilder;

import org.opensaml.xacml.ctx.impl.DecisionTypeImplBuilder;
import org.opensaml.xacml.ctx.impl.ResponseTypeImplBuilder;
import org.opensaml.xacml.ctx.impl.ResultTypeImplBuilder;
import org.opensaml.xacml.ctx.impl.AttributeValueTypeImplBuilder;
import org.opensaml.xacml.ctx.RequestType;
import org.opensaml.xacml.ctx.DecisionType;
import org.opensaml.xacml.ctx.ResourceType;
import org.opensaml.xacml.ctx.AttributeType;
import org.opensaml.xacml.ctx.SubjectType;
import org.opensaml.xacml.ctx.ActionType;
import org.opensaml.xacml.ctx.AttributeValueType;
import org.opensaml.xacml.ctx.ResponseType;
import org.opensaml.xacml.ctx.ResultType;
import org.opensaml.xacml.policy.ObligationsType;
import org.opensaml.xacml.policy.AttributeAssignmentType;
import org.opensaml.xacml.policy.ObligationType;
import org.opensaml.xacml.policy.EffectType;
import org.opensaml.xacml.policy.impl.ObligationTypeImplBuilder;
import org.opensaml.xacml.policy.impl.AttributeAssignmentTypeImplBuilder;
import org.opensaml.xacml.policy.impl.ObligationsTypeImplBuilder;


import org.opensaml.saml2.core.Statement;
import org.opensaml.xml.XMLObjectBuilderFactory;
import org.opensaml.Configuration;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.axis.utils.XMLUtils;

import org.opensciencegrid.authz.xacml.common.XACMLConstants;

import java.util.*;
import java.rmi.RemoteException;

public class TestMappingService implements XACMLMappingService {

    public static final String AUTHZ_SERVICE = "TestMappingService";

    private static Log logger =
            LogFactory.getLog(TestMappingService.class.getName());

    static XMLObjectBuilderFactory builderFactory;

    static {
        try {
            org.apache.xml.security.Init.init();
            org.opensaml.DefaultBootstrap.bootstrap();
        } catch (Exception e) {
            String err = "xacmlInitFailed";
            logger.error(err, e);
            throw new RuntimeException(err, e);
        }
        builderFactory = Configuration.getBuilderFactory();
    }

    public XACMLAuthzDecisionStatementType mapCredentials(XACMLAuthzDecisionQueryType xacmlQuery) throws Exception {
        XACMLAuthzDecisionStatementType xacmlAuthz=null;

        // Request sent by the PDP
        RequestType request = xacmlQuery.getRequest();

        // Check against policy and determine decision

        // build Decision type
        DecisionTypeImplBuilder decisionBuilder =
                (DecisionTypeImplBuilder) builderFactory.
                        getBuilder(DecisionType.DEFAULT_ELEMENT_NAME);
        DecisionType decision = decisionBuilder.buildObject();
        decision.setDecision(DecisionType.DECISION.Indeterminate);

        // Parse the request
        List resources = request.getResources();
        logger.debug("Resource list size: " + resources.size());
        Iterator resIter = resources.iterator();
        while (resIter.hasNext()) {
            ResourceType resourceType = (ResourceType) resIter.next();
            logger.trace("Resource:" +
                    XMLUtils.ElementToString(resourceType.getDOM()));
            Iterator resValue = resourceType.getAttributes().iterator();
            logger.debug("Resource Attribute size: " +
                    resourceType.getAttributes().size());
            while (resValue.hasNext()) {
                AttributeType attrType = (AttributeType) resValue.next();
                logger.debug("Resource Attribute value size: " +
                        attrType.getAttributeValues().size());
            }
        }

        List subjects = request.getSubjects();
        logger.debug("Subject list size: " + subjects.size());
        Iterator subIter = subjects.iterator();
        while (subIter.hasNext()) {
            SubjectType subjectType = (SubjectType) subIter.next();
            logger.trace("Subject:" +
                    XMLUtils.ElementToString(subjectType.getDOM()));
            Iterator subValue = subjectType.getAttributes().iterator();
            logger.debug("Subject Attribute size: " +
                    subjectType.getAttributes().size());
            while (subValue.hasNext()) {
                AttributeType attrType = (AttributeType) subValue.next();
                logger.debug("Subject Attribute value size: " +
                        attrType.getAttributeValues().size());
            }
        }

        ActionType action = request.getAction();
        logger.trace("Action " + XMLUtils.ElementToString(action.getDOM()));
        Iterator actionAttributes = action.getAttributes().iterator();
        String actionStr = null;
        while (actionAttributes.hasNext()) {
            AttributeType attr = (AttributeType) actionAttributes.next();
            // Expected type is string, so using that .
            if (attr.getAttributeID().
                    equals(XACMLConstants.ACTION_ID)) {
                // expecting string
                logger.debug("Action attribute size: " +
                        attr.getAttributeValues().size());
                Iterator valuesIter = attr.getAttributeValues().iterator();
                if (valuesIter.hasNext()) {
                    AttributeValueType valueObj =
                            (AttributeValueType) valuesIter.next();
                    actionStr = valueObj.getValue();
                    break;
                }
            }
        }

        logger.info("Action string is " + actionStr);

        if (actionStr == null) {
            logger.info("Indeterminate");
            decision.setDecision(DecisionType.DECISION.Indeterminate);
        } else if (actionStr.equals(XACMLConstants.ACTION_ACCESS)) {
            logger.info("Permit");
            decision.setDecision(DecisionType.DECISION.Permit);
        } else {
            logger.info("Deny");
            decision.setDecision(DecisionType.DECISION.Deny);
        }

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
        try {
            ObligationsType obligations = createObligations();
            if (obligations != null) {
                System.out.println("Setting obligations");
                result.setObligations(obligations);
            }
        } catch (Exception exp) {
            String err = "Unable to obtain obligations";
            logger.error(err, exp);
            throw new RemoteException(err, exp);
        }

        response.setResult(result);
        xacmlAuthz.setResponse(response);


        return xacmlAuthz;
    }

    private ObligationsType createObligations() throws Exception {

        System.out.println("ceating permit obligations");
        String localUserAccount = "cmsprod";//todo hardcoded
        System.out.println("Local user account is " + localUserAccount);

        if (localUserAccount != null) {

            ObligationsTypeImplBuilder builder =
                    (ObligationsTypeImplBuilder) builderFactory.
                            getBuilder(ObligationsType.DEFAULT_ELEMENT_QNAME);
            ObligationsType obligations = builder.buildObject();

            System.out.println("Creating obligation");
            ObligationTypeImplBuilder obligationBuilder =
                    (ObligationTypeImplBuilder) builderFactory.
                            getBuilder(ObligationType.DEFAULT_ELEMENT_QNAME);
            ObligationType obligation = obligationBuilder.buildObject();
            obligation.setFulfillOn(EffectType.Permit);
            obligation.setObligationId(XACMLConstants.OBLIGATION_USERNAME);

            AttributeAssignmentTypeImplBuilder attributeAssignmentBuilder =
                    (AttributeAssignmentTypeImplBuilder) builderFactory.
                            getBuilder(AttributeAssignmentType.DEFAULT_ELEMENT_NAME);
            AttributeAssignmentType attributeAssignment =
                    attributeAssignmentBuilder.buildObject();
            attributeAssignment.setAttributeId(XACMLConstants.OBLIGATION_USERNAME);
            attributeAssignment.setDataType(XACMLConstants.STRING_DATATYPE);

            AttributeValueTypeImplBuilder attributeValueBuilder =
                    (AttributeValueTypeImplBuilder) builderFactory.
                            getBuilder(AttributeValueType.DEFAULT_ELEMENT_NAME);
            AttributeValueType attributeValue = attributeValueBuilder.buildObject();
            attributeValue.setValue(localUserAccount);

            attributeAssignment.getUnknownXMLObjects().add(attributeValue);
            obligation.getAttributeAssignments().add(attributeAssignment);
            System.out.println("Added local user name");
            obligations.getObligations().add(obligation);
            return obligations;
        }

        return null;
    }

}

