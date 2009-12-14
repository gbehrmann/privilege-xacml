/**
 * XacmlAuthzParameters.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.3 Oct 05, 2005 (05:23:37 EDT) WSDL2Java emitter.
 */

package org.opensciencegrid.authz.xacml.stubs;

public class XacmlAuthzParameters  implements java.io.Serializable {
    private org.opensciencegrid.authz.xacml.stubs.AuthzServiceType authzService;

    private org.opensciencegrid.authz.xacml.stubs.InputContextOnly inputContextOnly;

    private org.opensciencegrid.authz.xacml.stubs.ReturnContext returnContext;

    private org.opensciencegrid.authz.xacml.stubs.CombinePolicies combinePolicies;

    private org.opensciencegrid.authz.xacml.stubs.SupportedObligation[] obligationHandlers;

    public XacmlAuthzParameters() {
    }

    public XacmlAuthzParameters(
           org.opensciencegrid.authz.xacml.stubs.AuthzServiceType authzService,
           org.opensciencegrid.authz.xacml.stubs.InputContextOnly inputContextOnly,
           org.opensciencegrid.authz.xacml.stubs.ReturnContext returnContext,
           org.opensciencegrid.authz.xacml.stubs.CombinePolicies combinePolicies,
           org.opensciencegrid.authz.xacml.stubs.SupportedObligation[] obligationHandlers) {
           this.authzService = authzService;
           this.inputContextOnly = inputContextOnly;
           this.returnContext = returnContext;
           this.combinePolicies = combinePolicies;
           this.obligationHandlers = obligationHandlers;
    }


    /**
     * Gets the authzService value for this XacmlAuthzParameters.
     * 
     * @return authzService
     */
    public org.opensciencegrid.authz.xacml.stubs.AuthzServiceType getAuthzService() {
        return authzService;
    }


    /**
     * Sets the authzService value for this XacmlAuthzParameters.
     * 
     * @param authzService
     */
    public void setAuthzService(org.opensciencegrid.authz.xacml.stubs.AuthzServiceType authzService) {
        this.authzService = authzService;
    }


    /**
     * Gets the inputContextOnly value for this XacmlAuthzParameters.
     * 
     * @return inputContextOnly
     */
    public org.opensciencegrid.authz.xacml.stubs.InputContextOnly getInputContextOnly() {
        return inputContextOnly;
    }


    /**
     * Sets the inputContextOnly value for this XacmlAuthzParameters.
     * 
     * @param inputContextOnly
     */
    public void setInputContextOnly(org.opensciencegrid.authz.xacml.stubs.InputContextOnly inputContextOnly) {
        this.inputContextOnly = inputContextOnly;
    }


    /**
     * Gets the returnContext value for this XacmlAuthzParameters.
     * 
     * @return returnContext
     */
    public org.opensciencegrid.authz.xacml.stubs.ReturnContext getReturnContext() {
        return returnContext;
    }


    /**
     * Sets the returnContext value for this XacmlAuthzParameters.
     * 
     * @param returnContext
     */
    public void setReturnContext(org.opensciencegrid.authz.xacml.stubs.ReturnContext returnContext) {
        this.returnContext = returnContext;
    }


    /**
     * Gets the combinePolicies value for this XacmlAuthzParameters.
     * 
     * @return combinePolicies
     */
    public org.opensciencegrid.authz.xacml.stubs.CombinePolicies getCombinePolicies() {
        return combinePolicies;
    }


    /**
     * Sets the combinePolicies value for this XacmlAuthzParameters.
     * 
     * @param combinePolicies
     */
    public void setCombinePolicies(org.opensciencegrid.authz.xacml.stubs.CombinePolicies combinePolicies) {
        this.combinePolicies = combinePolicies;
    }


    /**
     * Gets the obligationHandlers value for this XacmlAuthzParameters.
     * 
     * @return obligationHandlers
     */
    public org.opensciencegrid.authz.xacml.stubs.SupportedObligation[] getObligationHandlers() {
        return obligationHandlers;
    }


    /**
     * Sets the obligationHandlers value for this XacmlAuthzParameters.
     * 
     * @param obligationHandlers
     */
    public void setObligationHandlers(org.opensciencegrid.authz.xacml.stubs.SupportedObligation[] obligationHandlers) {
        this.obligationHandlers = obligationHandlers;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof XacmlAuthzParameters)) return false;
        XacmlAuthzParameters other = (XacmlAuthzParameters) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.authzService==null && other.getAuthzService()==null) || 
             (this.authzService!=null &&
              this.authzService.equals(other.getAuthzService()))) &&
            ((this.inputContextOnly==null && other.getInputContextOnly()==null) || 
             (this.inputContextOnly!=null &&
              this.inputContextOnly.equals(other.getInputContextOnly()))) &&
            ((this.returnContext==null && other.getReturnContext()==null) || 
             (this.returnContext!=null &&
              this.returnContext.equals(other.getReturnContext()))) &&
            ((this.combinePolicies==null && other.getCombinePolicies()==null) || 
             (this.combinePolicies!=null &&
              this.combinePolicies.equals(other.getCombinePolicies()))) &&
            ((this.obligationHandlers==null && other.getObligationHandlers()==null) || 
             (this.obligationHandlers!=null &&
              java.util.Arrays.equals(this.obligationHandlers, other.getObligationHandlers())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getAuthzService() != null) {
            _hashCode += getAuthzService().hashCode();
        }
        if (getInputContextOnly() != null) {
            _hashCode += getInputContextOnly().hashCode();
        }
        if (getReturnContext() != null) {
            _hashCode += getReturnContext().hashCode();
        }
        if (getCombinePolicies() != null) {
            _hashCode += getCombinePolicies().hashCode();
        }
        if (getObligationHandlers() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getObligationHandlers());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getObligationHandlers(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(XacmlAuthzParameters.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.globus.org/security/XACMLAuthorization", ">xacmlAuthzParameters"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("authzService");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.globus.org/security/XACMLAuthorization", "authzService"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.globus.org/security/XACMLAuthorization", "authzServiceType"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("inputContextOnly");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.globus.org/security/XACMLAuthorization", "inputContextOnly"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.globus.org/security/XACMLAuthorization", ">inputContextOnly"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("returnContext");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.globus.org/security/XACMLAuthorization", "returnContext"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.globus.org/security/XACMLAuthorization", ">returnContext"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("combinePolicies");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.globus.org/security/XACMLAuthorization", "combinePolicies"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.globus.org/security/XACMLAuthorization", ">combinePolicies"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("obligationHandlers");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.globus.org/security/XACMLAuthorization", "ObligationHandlers"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.globus.org/security/XACMLAuthorization", ">ObligationHandlers"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
