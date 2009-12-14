/**
 * XACMLAuthorizationServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.3 Oct 05, 2005 (05:23:37 EDT) WSDL2Java emitter.
 */

package org.opensciencegrid.authz.xacml.stubs;

public class XACMLAuthorizationServiceLocator extends org.apache.axis.client.Service implements org.opensciencegrid.authz.xacml.stubs.XACMLAuthorizationService {

    public XACMLAuthorizationServiceLocator() {
    }


    public XACMLAuthorizationServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public XACMLAuthorizationServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for XACMLAuthorizationPortTypePort
    private java.lang.String XACMLAuthorizationPortTypePort_address = "http://localhost:8080/wsrf/services/";

    public java.lang.String getXACMLAuthorizationPortTypePortAddress() {
        return XACMLAuthorizationPortTypePort_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String XACMLAuthorizationPortTypePortWSDDServiceName = "XACMLAuthorizationPortTypePort";

    public java.lang.String getXACMLAuthorizationPortTypePortWSDDServiceName() {
        return XACMLAuthorizationPortTypePortWSDDServiceName;
    }

    public void setXACMLAuthorizationPortTypePortWSDDServiceName(java.lang.String name) {
        XACMLAuthorizationPortTypePortWSDDServiceName = name;
    }

    public org.opensciencegrid.authz.xacml.stubs.XACMLAuthorizationPortType getXACMLAuthorizationPortTypePort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(XACMLAuthorizationPortTypePort_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getXACMLAuthorizationPortTypePort(endpoint);
    }

    public org.opensciencegrid.authz.xacml.stubs.XACMLAuthorizationPortType getXACMLAuthorizationPortTypePort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            org.opensciencegrid.authz.xacml.stubs.XACMLAuthorizationPortTypeSOAPBindingStub _stub = new org.opensciencegrid.authz.xacml.stubs.XACMLAuthorizationPortTypeSOAPBindingStub(portAddress, this);
            _stub.setPortName(getXACMLAuthorizationPortTypePortWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setXACMLAuthorizationPortTypePortEndpointAddress(java.lang.String address) {
        XACMLAuthorizationPortTypePort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (org.opensciencegrid.authz.xacml.stubs.XACMLAuthorizationPortType.class.isAssignableFrom(serviceEndpointInterface)) {
                org.opensciencegrid.authz.xacml.stubs.XACMLAuthorizationPortTypeSOAPBindingStub _stub = new org.opensciencegrid.authz.xacml.stubs.XACMLAuthorizationPortTypeSOAPBindingStub(new java.net.URL(XACMLAuthorizationPortTypePort_address), this);
                _stub.setPortName(getXACMLAuthorizationPortTypePortWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("XACMLAuthorizationPortTypePort".equals(inputPortName)) {
            return getXACMLAuthorizationPortTypePort();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://www.globus.org/security/XACMLAuthorization/service", "XACMLAuthorizationService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://www.globus.org/security/XACMLAuthorization/service", "XACMLAuthorizationPortTypePort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("XACMLAuthorizationPortTypePort".equals(portName)) {
            setXACMLAuthorizationPortTypePortEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
