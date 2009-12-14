/**
 * XACMLAuthorizationPortTypeSOAPBindingSkeleton.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.3 Oct 05, 2005 (05:23:37 EDT) WSDL2Java emitter.
 */

package org.opensciencegrid.authz.xacml.stubs;

public class XACMLAuthorizationPortTypeSOAPBindingSkeleton implements org.opensciencegrid.authz.xacml.stubs.XACMLAuthorizationPortType, org.apache.axis.wsdl.Skeleton {
    private org.opensciencegrid.authz.xacml.stubs.XACMLAuthorizationPortType impl;
    private static java.util.Map _myOperations = new java.util.Hashtable();
    private static java.util.Collection _myOperationsList = new java.util.ArrayList();

    /**
    * Returns List of OperationDesc objects with this name
    */
    public static java.util.List getOperationDescByName(java.lang.String methodName) {
        return (java.util.List)_myOperations.get(methodName);
    }

    /**
    * Returns Collection of OperationDescs
    */
    public static java.util.Collection getOperationDescs() {
        return _myOperationsList;
    }

    static {
        org.apache.axis.description.OperationDesc _oper;
        org.apache.axis.description.FaultDesc _fault;
        org.apache.axis.description.ParameterDesc [] _params;
        _params = new org.apache.axis.description.ParameterDesc [] {
            new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("urn:oasis:names:tc:xacml:2.0:profile:saml2.0:v2:schema:protocol", "XACMLAuthzDecisionQuery"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:oasis:names:tc:xacml:2.0:profile:saml2.0:v2:schema:protocol", ">XACMLAuthzDecisionQuery"), org.opensciencegrid.authz.xacml.stubs.XACMLAuthzDecisionQuery.class, false, false), 
        };
        _oper = new org.apache.axis.description.OperationDesc("authorize", _params, new javax.xml.namespace.QName("urn:oasis:names:tc:SAML:2.0:protocol", "Response"));
        _oper.setReturnType(new javax.xml.namespace.QName("urn:oasis:names:tc:SAML:2.0:protocol", ">Response"));
        _oper.setElementQName(new javax.xml.namespace.QName("", "Authorize"));
        _oper.setSoapAction("http://www.globus.org/security/XACMLAuthorization/XACMLAuthorizationPortType/AuthorizeRequest");
        _myOperationsList.add(_oper);
        if (_myOperations.get("authorize") == null) {
            _myOperations.put("authorize", new java.util.ArrayList());
        }
        ((java.util.List)_myOperations.get("authorize")).add(_oper);
    }

    public XACMLAuthorizationPortTypeSOAPBindingSkeleton() {
        this.impl = new org.opensciencegrid.authz.xacml.stubs.XACMLAuthorizationPortTypeSOAPBindingImpl();
    }

    public XACMLAuthorizationPortTypeSOAPBindingSkeleton(org.opensciencegrid.authz.xacml.stubs.XACMLAuthorizationPortType impl) {
        this.impl = impl;
    }
    public org.opensciencegrid.authz.xacml.stubs.Response authorize(org.opensciencegrid.authz.xacml.stubs.XACMLAuthzDecisionQuery parameters) throws java.rmi.RemoteException
    {
        org.opensciencegrid.authz.xacml.stubs.Response ret = impl.authorize(parameters);
        return ret;
    }

}
