/**
 * GenericServicesLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.novo.report.webservices;

public class GenericServicesLocator extends org.apache.axis.client.Service implements com.novo.report.webservices.GenericServices {

/**
 * <b>Standard Web Services interface for STARLIMS v10 R2.<br><br>
 * 	<a href='http://www.starlims.com'>STARLIMS</a> is a powerful, full-featured,
 * highly extensible and scalable laboratory information management system.
 * STARLIMS Corp. primary goal has been to deliver leading edge, flexible
 * and easy-to-use, collaborative LIMS solutions to organizations worldwide,
 * comprising corporate, government, municipal, and private laboratories,
 * within the chemical, clinical, environmental, food, forensics, petrochemical
 * and pharmaceutical industries.</b>
 */

    public GenericServicesLocator() {
    }


    public GenericServicesLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public GenericServicesLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for GenericServicesSoap
    private java.lang.String GenericServicesSoap_address = "http://47.94.217.167/starlims11.nov/Services/generic.asmx";

    public java.lang.String getGenericServicesSoapAddress() {
        return GenericServicesSoap_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String GenericServicesSoapWSDDServiceName = "GenericServicesSoap";

    public java.lang.String getGenericServicesSoapWSDDServiceName() {
        return GenericServicesSoapWSDDServiceName;
    }

    public void setGenericServicesSoapWSDDServiceName(java.lang.String name) {
        GenericServicesSoapWSDDServiceName = name;
    }

    public com.novo.report.webservices.GenericServicesSoap getGenericServicesSoap() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(GenericServicesSoap_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getGenericServicesSoap(endpoint);
    }

    public com.novo.report.webservices.GenericServicesSoap getGenericServicesSoap(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.novo.report.webservices.GenericServicesSoapStub _stub = new com.novo.report.webservices.GenericServicesSoapStub(portAddress, this);
            _stub.setPortName(getGenericServicesSoapWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setGenericServicesSoapEndpointAddress(java.lang.String address) {
        GenericServicesSoap_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (com.novo.report.webservices.GenericServicesSoap.class.isAssignableFrom(serviceEndpointInterface)) {
                com.novo.report.webservices.GenericServicesSoapStub _stub = new com.novo.report.webservices.GenericServicesSoapStub(new java.net.URL(GenericServicesSoap_address), this);
                _stub.setPortName(getGenericServicesSoapWSDDServiceName());
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
        if ("GenericServicesSoap".equals(inputPortName)) {
            return getGenericServicesSoap();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://www.starlims.com/webservices/", "GenericServices");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://www.starlims.com/webservices/", "GenericServicesSoap"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("GenericServicesSoap".equals(portName)) {
            setGenericServicesSoapEndpointAddress(address);
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
