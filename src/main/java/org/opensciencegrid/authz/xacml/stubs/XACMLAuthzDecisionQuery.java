/**
 * XACMLAuthzDecisionQuery.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.3 Oct 05, 2005 (05:23:37 EDT) WSDL2Java emitter.
 */

package org.opensciencegrid.authz.xacml.stubs;

public class XACMLAuthzDecisionQuery  implements java.io.Serializable, org.apache.axis.encoding.AnyContentType {
    private org.apache.axis.message.MessageElement [] _any;

    private org.apache.axis.types.Id ID;  // attribute

    private java.lang.String version;  // attribute

    private java.util.Calendar issueInstant;  // attribute

    private org.apache.axis.types.URI destination;  // attribute

    private org.apache.axis.types.URI consent;  // attribute

    private java.lang.Boolean inputContextOnly;  // attribute

    private java.lang.Boolean returnContext;  // attribute

    private java.lang.Boolean combinePolicies;  // attribute

    public XACMLAuthzDecisionQuery() {
    }

    public XACMLAuthzDecisionQuery(
           org.apache.axis.message.MessageElement [] _any,
           org.apache.axis.types.Id ID,
           java.lang.String version,
           java.util.Calendar issueInstant,
           org.apache.axis.types.URI destination,
           org.apache.axis.types.URI consent,
           java.lang.Boolean inputContextOnly,
           java.lang.Boolean returnContext,
           java.lang.Boolean combinePolicies) {
           this._any = _any;
           this.ID = ID;
           this.version = version;
           this.issueInstant = issueInstant;
           this.destination = destination;
           this.consent = consent;
           this.inputContextOnly = inputContextOnly;
           this.returnContext = returnContext;
           this.combinePolicies = combinePolicies;
    }


    /**
     * Gets the _any value for this XACMLAuthzDecisionQuery.
     * 
     * @return _any
     */
    public org.apache.axis.message.MessageElement [] get_any() {
        return _any;
    }


    /**
     * Sets the _any value for this XACMLAuthzDecisionQuery.
     * 
     * @param _any
     */
    public void set_any(org.apache.axis.message.MessageElement [] _any) {
        this._any = _any;
    }


    /**
     * Gets the ID value for this XACMLAuthzDecisionQuery.
     * 
     * @return ID
     */
    public org.apache.axis.types.Id getID() {
        return ID;
    }


    /**
     * Sets the ID value for this XACMLAuthzDecisionQuery.
     * 
     * @param ID
     */
    public void setID(org.apache.axis.types.Id ID) {
        this.ID = ID;
    }


    /**
     * Gets the version value for this XACMLAuthzDecisionQuery.
     * 
     * @return version
     */
    public java.lang.String getVersion() {
        return version;
    }


    /**
     * Sets the version value for this XACMLAuthzDecisionQuery.
     * 
     * @param version
     */
    public void setVersion(java.lang.String version) {
        this.version = version;
    }


    /**
     * Gets the issueInstant value for this XACMLAuthzDecisionQuery.
     * 
     * @return issueInstant
     */
    public java.util.Calendar getIssueInstant() {
        return issueInstant;
    }


    /**
     * Sets the issueInstant value for this XACMLAuthzDecisionQuery.
     * 
     * @param issueInstant
     */
    public void setIssueInstant(java.util.Calendar issueInstant) {
        this.issueInstant = issueInstant;
    }


    /**
     * Gets the destination value for this XACMLAuthzDecisionQuery.
     * 
     * @return destination
     */
    public org.apache.axis.types.URI getDestination() {
        return destination;
    }


    /**
     * Sets the destination value for this XACMLAuthzDecisionQuery.
     * 
     * @param destination
     */
    public void setDestination(org.apache.axis.types.URI destination) {
        this.destination = destination;
    }


    /**
     * Gets the consent value for this XACMLAuthzDecisionQuery.
     * 
     * @return consent
     */
    public org.apache.axis.types.URI getConsent() {
        return consent;
    }


    /**
     * Sets the consent value for this XACMLAuthzDecisionQuery.
     * 
     * @param consent
     */
    public void setConsent(org.apache.axis.types.URI consent) {
        this.consent = consent;
    }


    /**
     * Gets the inputContextOnly value for this XACMLAuthzDecisionQuery.
     * 
     * @return inputContextOnly
     */
    public java.lang.Boolean getInputContextOnly() {
        return inputContextOnly;
    }


    /**
     * Sets the inputContextOnly value for this XACMLAuthzDecisionQuery.
     * 
     * @param inputContextOnly
     */
    public void setInputContextOnly(java.lang.Boolean inputContextOnly) {
        this.inputContextOnly = inputContextOnly;
    }


    /**
     * Gets the returnContext value for this XACMLAuthzDecisionQuery.
     * 
     * @return returnContext
     */
    public java.lang.Boolean getReturnContext() {
        return returnContext;
    }


    /**
     * Sets the returnContext value for this XACMLAuthzDecisionQuery.
     * 
     * @param returnContext
     */
    public void setReturnContext(java.lang.Boolean returnContext) {
        this.returnContext = returnContext;
    }


    /**
     * Gets the combinePolicies value for this XACMLAuthzDecisionQuery.
     * 
     * @return combinePolicies
     */
    public java.lang.Boolean getCombinePolicies() {
        return combinePolicies;
    }


    /**
     * Sets the combinePolicies value for this XACMLAuthzDecisionQuery.
     * 
     * @param combinePolicies
     */
    public void setCombinePolicies(java.lang.Boolean combinePolicies) {
        this.combinePolicies = combinePolicies;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof XACMLAuthzDecisionQuery)) return false;
        XACMLAuthzDecisionQuery other = (XACMLAuthzDecisionQuery) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this._any==null && other.get_any()==null) || 
             (this._any!=null &&
              java.util.Arrays.equals(this._any, other.get_any()))) &&
            ((this.ID==null && other.getID()==null) || 
             (this.ID!=null &&
              this.ID.equals(other.getID()))) &&
            ((this.version==null && other.getVersion()==null) || 
             (this.version!=null &&
              this.version.equals(other.getVersion()))) &&
            ((this.issueInstant==null && other.getIssueInstant()==null) || 
             (this.issueInstant!=null &&
              this.issueInstant.equals(other.getIssueInstant()))) &&
            ((this.destination==null && other.getDestination()==null) || 
             (this.destination!=null &&
              this.destination.equals(other.getDestination()))) &&
            ((this.consent==null && other.getConsent()==null) || 
             (this.consent!=null &&
              this.consent.equals(other.getConsent()))) &&
            ((this.inputContextOnly==null && other.getInputContextOnly()==null) || 
             (this.inputContextOnly!=null &&
              this.inputContextOnly.equals(other.getInputContextOnly()))) &&
            ((this.returnContext==null && other.getReturnContext()==null) || 
             (this.returnContext!=null &&
              this.returnContext.equals(other.getReturnContext()))) &&
            ((this.combinePolicies==null && other.getCombinePolicies()==null) || 
             (this.combinePolicies!=null &&
              this.combinePolicies.equals(other.getCombinePolicies())));
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
        if (get_any() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(get_any());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(get_any(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getID() != null) {
            _hashCode += getID().hashCode();
        }
        if (getVersion() != null) {
            _hashCode += getVersion().hashCode();
        }
        if (getIssueInstant() != null) {
            _hashCode += getIssueInstant().hashCode();
        }
        if (getDestination() != null) {
            _hashCode += getDestination().hashCode();
        }
        if (getConsent() != null) {
            _hashCode += getConsent().hashCode();
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
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(XACMLAuthzDecisionQuery.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("urn:oasis:names:tc:xacml:2.0:profile:saml2.0:v2:schema:protocol", ">XACMLAuthzDecisionQuery"));
        org.apache.axis.description.AttributeDesc attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("ID");
        attrField.setXmlName(new javax.xml.namespace.QName("", "ID"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "ID"));
        typeDesc.addFieldDesc(attrField);
        attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("version");
        attrField.setXmlName(new javax.xml.namespace.QName("", "Version"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(attrField);
        attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("issueInstant");
        attrField.setXmlName(new javax.xml.namespace.QName("", "IssueInstant"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        typeDesc.addFieldDesc(attrField);
        attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("destination");
        attrField.setXmlName(new javax.xml.namespace.QName("", "Destination"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "anyURI"));
        typeDesc.addFieldDesc(attrField);
        attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("consent");
        attrField.setXmlName(new javax.xml.namespace.QName("", "Consent"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "anyURI"));
        typeDesc.addFieldDesc(attrField);
        attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("inputContextOnly");
        attrField.setXmlName(new javax.xml.namespace.QName("", "InputContextOnly"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        typeDesc.addFieldDesc(attrField);
        attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("returnContext");
        attrField.setXmlName(new javax.xml.namespace.QName("", "ReturnContext"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        typeDesc.addFieldDesc(attrField);
        attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("combinePolicies");
        attrField.setXmlName(new javax.xml.namespace.QName("", "CombinePolicies"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        typeDesc.addFieldDesc(attrField);
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
