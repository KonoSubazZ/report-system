/**
 * GenericServices.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.novo.report.webservices;

public interface GenericServices extends javax.xml.rpc.Service {

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
    public java.lang.String getGenericServicesSoapAddress();

    public com.novo.report.webservices.GenericServicesSoap getGenericServicesSoap() throws javax.xml.rpc.ServiceException;

    public com.novo.report.webservices.GenericServicesSoap getGenericServicesSoap(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
