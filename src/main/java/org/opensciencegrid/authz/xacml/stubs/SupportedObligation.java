/**
 * SupportedObligation.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.3 Oct 05, 2005 (05:23:37 EDT) WSDL2Java emitter.
 */

package org.opensciencegrid.authz.xacml.stubs;

public class SupportedObligation  implements java.io.Serializable {
    private java.lang.String obligationId;

    private java.lang.String FQClassName;

    public SupportedObligation() {
    }

    public SupportedObligation(
           java.lang.String obligationId,
           java.lang.String FQClassName) {
           this.obligationId = obligationId;
           this.FQClassName = FQClassName;
    }


    /**
     * Gets the obligationId value for this SupportedObligation.
     * 
     * @return obligationId
     */
    public java.lang.String getObligationId() {
        return obligationId;
    }


    /**
     * Sets the obligationId value for this SupportedObligation.
     * 
     * @param obligationId
     */
    public void setObligationId(java.lang.String obligationId) {
        this.obligationId = obligationId;
    }


    /**
     * Gets the FQClassName value for this SupportedObligation.
     * 
     * @return FQClassName
     */
    public java.lang.String getFQClassName() {
        return FQClassName;
    }


    /**
     * Sets the FQClassName value for this SupportedObligation.
     * 
     * @param FQClassName
     */
    public void setFQClassName(java.lang.String FQClassName) {
        this.FQClassName = FQClassName;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof SupportedObligation)) return false;
        SupportedObligation other = (SupportedObligation) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.obligationId==null && other.getObligationId()==null) || 
             (this.obligationId!=null &&
              this.obligationId.equals(other.getObligationId()))) &&
            ((this.FQClassName==null && other.getFQClassName()==null) || 
             (this.FQClassName!=null &&
              this.FQClassName.equals(other.getFQClassName())));
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
        if (getObligationId() != null) {
            _hashCode += getObligationId().hashCode();
        }
        if (getFQClassName() != null) {
            _hashCode += getFQClassName().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(SupportedObligation.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.globus.org/security/XACMLAuthorization", ">SupportedObligation"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("obligationId");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.globus.org/security/XACMLAuthorization", "ObligationId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("FQClassName");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.globus.org/security/XACMLAuthorization", "FQClassName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
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
