package org.opensciencegrid.authz.xacml.client;

import org.opensciencegrid.authz.xacml.stubs.Response;
import org.opensciencegrid.authz.xacml.common.LocalId;
import org.opensciencegrid.authz.xacml.common.XACMLConstants;
import org.opensaml.xacml.profile.saml.XACMLAuthzDecisionStatementType;
import org.opensaml.xacml.ctx.*;
import org.opensaml.xacml.ctx.provider.ObligationProcessingContext;
import org.opensaml.xacml.ctx.provider.ObligationService;
import org.opensaml.xacml.ctx.provider.ObligationProcessingException;
import org.opensaml.xacml.ctx.provider.BaseObligationHandler;
import org.opensaml.xacml.policy.ObligationsType;
import org.opensaml.xacml.policy.ObligationType;

import java.rmi.RemoteException;
import java.util.*;

import com.sun.xacml.ctx.Status;

public class MapCredentialsClient extends XACMLClient {

  public LocalId mapCredentials(String authzServiceUrlStr) throws Exception {

    Response resp=null;
    try {
      resp = authorize(authzServiceUrlStr);
    } catch (RemoteException exp) {
      //todo  throw an exception
    }

    XACMLAuthzDecisionStatementType statement = convertToXACML(resp);
    ResultType result = statement.getResponse().getResult();
    if (result.getDecision().getDecision() == DecisionType.DECISION.Indeterminate) {
      StatusType status = result.getStatus();
      if (status !=null && status.getStatusCode().getValue().equals(Status.STATUS_PROCESSING_ERROR)) {
         String msg = status.getStatusMessage().getValue();
         throw new Exception("XACML server error: " + msg);
      }
    }
    ObligationProcessingContext context = new ObligationProcessingContext(result);
    ObligationService obligserv = new ObligationService();
    LocalIDObligationHandler localIDhandler = new LocalIDObligationHandler();

    obligserv.addObligationhandler(localIDhandler.getSubjectHandler());
    obligserv.addObligationhandler(localIDhandler.getUIDGIDHandler());
    obligserv.addObligationhandler(localIDhandler.getSecondaryGIDSHandler());

    Set<String> obligationHandlerIDs = new TreeSet<String>();
    Iterator<BaseObligationHandler> handlerItr = obligserv.getObligationHandlers().iterator();
    BaseObligationHandler handler;
    while (handlerItr.hasNext()) {
        handler = handlerItr.next();
        obligationHandlerIDs.add(handler.getObligationId());
    }

    ObligationsType obligations = context.getAuthorizationDecisionResult().getObligations();
    for (ObligationType obligation : obligations.getObligations()) {
        if (!obligationHandlerIDs.contains(obligation.getObligationId())) {
            throw new ObligationProcessingException("Unknown obligation in response: " + obligation.getObligationId());
        }
    }

    try {
      obligserv.processObligations(context);
    } catch (ObligationProcessingException ope) {
      logger.error("Exception in obligation handling " + ope.getMessage());
      throw ope;
    }

    return localIDhandler.getLocalID();
  }

  Response authorize (String authzServiceUrlStr) throws RemoteException {
    String issuer=null;

    SubjectType subject = getSubjectType(issuer);
    ResourceType resource = getResourceType(issuer);
    ActionType action = getActionType(issuer);

    Vector<String> obligationIds = new Vector();
    obligationIds.add(XACMLConstants.OBLIGATION_USERNAME);
    obligationIds.add(XACMLConstants.OBLIGATION_UIDGID);
    obligationIds.add(XACMLConstants.OBLIGATION_SECONDARY_GIDS);
    EnvironmentType env = getEnvironmentType(obligationIds, issuer);

    return authorize(subject, resource, action, env, authzServiceUrlStr);
  }

}
