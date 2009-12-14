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
import org.apache.axis.MessageContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xml.security.keys.KeyInfo;
import org.apache.xml.security.keys.content.X509Data;
import org.apache.xml.security.keys.content.x509.XMLX509Certificate;
import org.apache.xml.security.signature.XMLSignature;
import org.globus.wsrf.security.SecurityManager;
//import org.globus.wsrf.impl.security.util.CredentialUtil;
import org.globus.wsrf.utils.XmlUtils;
import org.globus.wsrf.ResourceContext;
import org.globus.wsrf.Resource;
import org.globus.wsrf.impl.ResourceContextImpl;
import org.globus.wsrf.config.ConfigException;
import org.globus.util.I18n;
import org.globus.axis.gsi.GSIConstants;
import org.globus.gsi.jaas.GlobusPrincipal;
import org.globus.gsi.CertUtil;
import org.opensciencegrid.authz.xacml.common.XACMLConstants;
import org.ietf.jgss.GSSCredential;

import javax.security.auth.Subject;
import javax.xml.namespace.QName;
import java.util.*;
import java.rmi.RemoteException;
import java.io.IOException;
import java.io.ByteArrayInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.security.cert.X509Certificate;
import java.security.Principal;

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
			//String err = i18n.getExceptionMessage("xacmlInitFailed");
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

		// Policy for the service
		//Vector deniedList;
		try {
			//deniedList = getResource().getDeclinedMethods();
		} catch (Exception e) {
			String err = "Get declined methods failed.";
			logger.error(err, e);
			throw new RemoteException(err, e);
		}

		Subject systemSubject = null;
		String issuer = null;
		//try {
		//    MessageContext ctx = MessageContext.getCurrentContext();
		//    SecurityManager manager = SecurityManager.getManager(ctx);
		//    systemSubject = manager.getServiceSubject();
		//    issuer = CredentialUtil.getPrincipal(systemSubject).getName();
		//} catch (org.globus.wsrf.security.SecurityException exp) {
		//    String err = "Unable to obtain service credentials";
		//    logger.error(err, exp);
		//    throw new RemoteException(err, exp);
		//}

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
					XmlUtils.toString(resourceType.getDOM()));
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
					XmlUtils.toString(subjectType.getDOM()));
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
		logger.trace("Action " + XmlUtils.toString(action.getDOM()));
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
			//} else if (deniedList.contains(actionStr)) {
		} else if (actionStr.equals(XACMLConstants.ACTION_ACCESS)) {
			//} else if (true) {
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
		//String localUserAccount = getResource().getLocalUserAccount();
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

	private SampleXACMLAuthzResource getResource() throws Exception {
		//ResourceContext ctx = ResourceContext.getResourceContext();
		ResourceContext ctx = new ResourceContextImpl(org.apache.axis.MessageContext.getCurrentContext());
		return (SampleXACMLAuthzResource) ctx.getResource();
	}

}


/*
 * Copyright 1999-2006 University of Chicago
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


class SampleXACMLAuthzResource implements Resource {

	Vector declinedMethods = null;
	String localUserAccount = null;

	public SampleXACMLAuthzResource() {

	}

	public SampleXACMLAuthzResource(String methodList,
			String localUserAccount) {
		this.declinedMethods = new Vector();
		parseList(methodList, this.declinedMethods, false);
		this.localUserAccount = localUserAccount;
	}

	protected void parseList(String list, Vector vector, boolean qname) {

		if ((list != null) && (!list.trim().equals(""))) {
			StringTokenizer strTok = new StringTokenizer(list, ",");
			//            int length = strTok.countTokens();
			while (strTok.hasMoreTokens()) {
				String methName = strTok.nextToken().trim();
				if (qname) {
					QName qNameVal = QName.valueOf(methName);
					vector.add(qNameVal);
				} else {
					vector.add(methName);
				}
			}
		}
	}

	public void addDeclinedMethod(String methodName) {
		if (declinedMethods == null) {
			declinedMethods = new Vector();
		}
		declinedMethods.add(methodName);
	}

	public Vector getDeclinedMethods() {
		return declinedMethods;
	}

	public String getLocalUserAccount() {
		return this.localUserAccount;
	}
}


class CredentialUtil {

	private static I18n i18n =
		I18n.getI18n("org.globus.wsrf.impl.security.error",
				CredentialUtil.class.getClassLoader());


	public static GSSCredential getCredential(MessageContext ctx)
	throws SecurityException {
		Object tmp = ctx.getProperty(GSIConstants.GSI_CREDENTIALS);
		if (tmp == null || tmp instanceof GSSCredential) {
			return (GSSCredential)tmp;
		} else {
			throw new SecurityException(
					i18n.getMessage("invalidType",
							new Object[] {GSIConstants.GSI_CREDENTIALS,
							GSSCredential.class.getName()}));
		}
	}

	public static String getIdentity(Subject subject) {
		if (subject == null) {
			return null;
		}

		Set principals = subject.getPrincipals(GlobusPrincipal.class);

		if ((principals == null) || principals.isEmpty()) {
			return null;
		}

		Iterator iter = principals.iterator();
		GlobusPrincipal principal = (GlobusPrincipal) iter.next();

		return principal.toString();
	}

	public static X509Certificate[] getCertificates(XMLSignature sig)
	throws Exception {

		KeyInfo info = sig.getKeyInfo();
		return getCertificatesX509Data(info);
	}

	public static X509Certificate[] getCertificatesX509Data(KeyInfo info)
	throws Exception {
		int len = info.lengthX509Data();

		if (len != 1) {
			throw new ConfigException(i18n.getMessage("invalidX509Data",
					new Object[]
					           { new Integer(len) }));
		}

		X509Data data = info.itemX509Data(0);
		int certLen = data.lengthCertificate();

		if (certLen <= 0) {
			throw new ConfigException(i18n.getMessage("invalidCertData",
					new Object[]
					           { new Integer(certLen) }));
		}

		X509Certificate[] certs = new X509Certificate[certLen];
		XMLX509Certificate xmlCert;
		ByteArrayInputStream input;

		for (int i = 0; i < certLen; i++) {
			xmlCert = data.itemCertificate(i);
			input = new ByteArrayInputStream(xmlCert.getCertificateBytes());
			certs[i] = CertUtil.loadCertificate(input);
		}

		return certs;
	}

	// first element in array is "valid till" and second element is
	// "valid from"
	public static Calendar[] getValidity(X509Certificate[] certArray){

		if ((certArray == null) || (certArray.length < 1)) {
			return null;
		}
		Date notAfter = certArray[0].getNotAfter();
		Date notBefore = certArray[0].getNotBefore();
		for (int i=1; i<certArray.length; i++) {
			Date notAfteri = certArray[i].getNotAfter();
			if (notAfteri.before(notAfter)) {
				notAfter = notAfteri;
			}
			Date notBeforei = certArray[i].getNotBefore();
			if (notBeforei.after(notBefore)) {
				notBefore = notBeforei;
			}
		}

		Calendar validTill = Calendar.getInstance();
		validTill.setTime(notAfter);
		Calendar validFrom = Calendar.getInstance();
		validFrom.setTime(notBefore);

		return new Calendar[] { validTill, validFrom };
	}

	/**
	 * Extracts the principal from caller subject
	 *
	 * @param subject subject
	 *
	 * @return principal associated with subject
	 */
	public static Principal getPrincipal(Subject subject) {

		if (subject == null) {
			return null;
		}

		Set principals = subject.getPrincipals();

		if ((principals == null) || principals.isEmpty()) {
			return null;
		}

		return (Principal) principals.iterator().next();
	}

	public static String getIdentity(X509Certificate[] certs) {

		if ((certs != null) && (certs.length > 0)) {
			return certs[certs.length-1].getSubjectDN().getName();
		}
		return null;
	}

	// Note about closing the stream
	public static void writeSubject(Subject subject, ObjectOutputStream oos)
	throws SecurityException {

		try {
			// serialize subject object (does not serialize credentials)
			oos.writeObject(subject);
			if (subject == null) {
				return;
			}
			Set publicCreds = subject.getPublicCredentials();
			if ((publicCreds != null) && (!publicCreds.isEmpty())) {
				oos.writeObject(Boolean.TRUE);
				Vector vector = new Vector(publicCreds);
				// Will definitely work if instance of
				// X509Certificate[], X509Certificate, EncryptionCredentials
				oos.writeObject(vector);
			} else {
				oos.writeObject(Boolean.FALSE);
			}

			Set privateCreds = subject.getPrivateCredentials();
			if ((privateCreds != null) && (!privateCreds.isEmpty())) {
				oos.writeObject(Boolean.TRUE);
				Vector vector = new Vector(privateCreds);

				// Will definitely work if instance of
				// PasswordCredential and GlobusGSSCredentialImpl
				oos.writeObject(vector);
			} else {
				oos.writeObject(Boolean.FALSE);
			}
		} catch (IOException exp) {
			throw new SecurityException(exp);
		}
	}

	// If it is a read only subject object, new Subject object is created
	public static Subject readSubject(ObjectInputStream ois)
	throws SecurityException, ClassNotFoundException {

		try {
			Subject subject = (Subject)ois.readObject();
			if (subject == null) {
				return subject;
			}

			Subject newSubject = null;

			if (subject.isReadOnly()) {
				newSubject = new Subject();
				newSubject.getPrincipals().addAll(subject.getPrincipals());
			} else {
				newSubject = subject;
			}

			Boolean publicCred = (Boolean)ois.readObject();
			if (Boolean.TRUE.equals(publicCred)) {
				Vector publicCreds = (Vector)ois.readObject();
				newSubject.getPublicCredentials().addAll(publicCreds);
			}

			Boolean privateCred = (Boolean)ois.readObject();
			if (Boolean.TRUE.equals(privateCred)) {
				Vector privateCreds = (Vector)ois.readObject();
				newSubject.getPrivateCredentials().addAll(privateCreds);
			}

			if (subject.isReadOnly()) {
				newSubject.setReadOnly();
			}

			return newSubject;
		} catch (IOException exp) {
			throw new SecurityException(exp);
		}
	}

}