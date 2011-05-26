package org.opensciencegrid.authz.xacml.client;

import org.opensaml.xacml.ctx.provider.ObligationProcessingContext;
import org.opensaml.xacml.ctx.provider.ObligationProcessingException;
import org.opensaml.xacml.ctx.provider.BaseObligationHandler;
import org.opensaml.xacml.policy.ObligationType;
import org.opensaml.xacml.policy.AttributeAssignmentType;
import org.opensciencegrid.authz.xacml.common.LocalId;
import org.opensciencegrid.authz.xacml.common.XACMLConstants;

import java.util.List;

public class LocalIDObligationHandler {

    LocalId id = new LocalId();

    public enum ATTR_ID
    {
        ATTRIBUTE_USERNAME_ID, ATTRIBUTE_POSIX_UID_ID, ATTRIBUTE_POSIX_GID_ID, NOVALUE;

        public static ATTR_ID toAttrID(String str) throws ObligationProcessingException
        {

            /* Will throw an exception if attribute is not supported, causing NOVALUE to be returned */
            try {
                if (str.startsWith(XACMLConstants.ATTRIBUTE_BASE_NS)) {
                    String attrhandle = str.substring(XACMLConstants.ATTRIBUTE_BASE_NS_STR_LENGTH);
                    return valueOf(XACMLConstants.AttrIDHash.get(attrhandle));
                }
            }
            catch (Exception ex) {
                String msg = "Attribute ID " + str + " unexpected.";
                    throw new ObligationProcessingException(msg);
            }

            return NOVALUE;
        }
    }

    BaseObligationHandler subjecthandler = new BaseObligationHandler(XACMLConstants.OBLIGATION_USERNAME, 1) {
        public void evaluateObligation(ObligationProcessingContext context, ObligationType obligation)
                throws ObligationProcessingException {
            List<AttributeAssignmentType> attrs = obligation.getAttributeAssignments();
            for (AttributeAssignmentType attr : attrs) {
                String attrid = attr.getAttributeId();
                String attrval = attr.getValue();
                if (ATTR_ID.toAttrID(attrid).equals(ATTR_ID.ATTRIBUTE_USERNAME_ID))
                {
                    id.setUserName(attrval);
                } else {
                    String msg = "Attribute ID " + attrid + " unexpected for "  + XACMLConstants.OBLIGATION_USERNAME;
                    throw new ObligationProcessingException(msg);
                }
            }
        }
    };

    BaseObligationHandler uidgidhandler = new BaseObligationHandler(XACMLConstants.OBLIGATION_UIDGID, 2) {
        public void evaluateObligation(ObligationProcessingContext context, ObligationType obligation)
                throws ObligationProcessingException {
            List<AttributeAssignmentType> attrs = obligation.getAttributeAssignments();
            for (AttributeAssignmentType attr : attrs) {
                String attrid = attr.getAttributeId();
                String attrval = attr.getValue();
                switch (ATTR_ID.toAttrID(attrid))
                {
                    case ATTRIBUTE_POSIX_UID_ID:
                        id.setUID(attrval);
                        break;
                    case ATTRIBUTE_POSIX_GID_ID:
                        id.setGID(attrval);
                        break;
                    default:
                        String msg = "Attribute ID " + attrid + " unexpected for "  + XACMLConstants.OBLIGATION_UIDGID;
                        throw new ObligationProcessingException(msg);
                }
            }
        }
    };

    BaseObligationHandler seconarygidshandler = new BaseObligationHandler(XACMLConstants.OBLIGATION_SECONDARY_GIDS, 3) {
        public void evaluateObligation(ObligationProcessingContext context, ObligationType obligation)
                throws ObligationProcessingException {
            List<AttributeAssignmentType> attrs = obligation.getAttributeAssignments();
            String[] gids = new String[attrs.size()];
            int i=0;
            for (AttributeAssignmentType attr : attrs) {
                String attrid = attr.getAttributeId();
                String attrval = attr.getValue();
                switch (ATTR_ID.toAttrID(attrid))
                {
                    case ATTRIBUTE_POSIX_GID_ID:
                        gids[i++] = attrval;
                        break;
                    default:
                        String msg = "Attribute ID " + attrid + " unexpected for "  + XACMLConstants.OBLIGATION_SECONDARY_GIDS;
                        throw new ObligationProcessingException(msg);
                }
            }
            id.setSecondaryGIDs(gids);
        }
    };

    public LocalId getLocalID() {
        return id;
    }

    public BaseObligationHandler getSubjectHandler() {
        return subjecthandler;
    }

    public BaseObligationHandler getUIDGIDHandler() {
        return uidgidhandler;
    }

    public BaseObligationHandler getSecondaryGIDSHandler() {
        return seconarygidshandler;
    }
}
