/*
 * XACMLMappingService.java
 *
 * Created on May 30, 2008
 */

package org.opensciencegrid.authz.xacml.service;

import org.opensaml.xacml.profile.saml.XACMLAuthzDecisionQueryType;
import org.opensaml.xacml.profile.saml.XACMLAuthzDecisionStatementType;

/** A service that maps a xacml query to an xacml statement
 *
 * @author Jay Packard 
 */
public interface XACMLMappingService {
	public XACMLAuthzDecisionStatementType mapCredentials(XACMLAuthzDecisionQueryType xacmlQuery) throws Exception;
}
