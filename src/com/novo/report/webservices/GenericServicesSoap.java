/**
 * GenericServicesSoap.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.novo.report.webservices;

public interface GenericServicesSoap extends java.rmi.Remote {

    /**
     * Run any STARLIMS action. This web service method is here to
     * provide the maximum flexibility in accessing STARLIMS functionality.
     */
    public java.lang.Object runAction(java.lang.String actionID, java.lang.Object[] parameters) throws java.rmi.RemoteException;

    /**
     * Run any STARLIMS action in one single call passing all information
     * it requires together with STARLIMS Credentials. This web service method
     * is here to provide the maximum flexibility in accessing STARLIMS functionality
     * and is intended to be used from very rare calls to STARLIMS.
     */
    public java.lang.Object runActionDirect(java.lang.String actionID, java.lang.Object[] parameters, java.lang.String userName, java.lang.String password) throws java.rmi.RemoteException;

    /**
     * Run a collection of REST style actions.
     */
    public java.lang.String[] runRESTActions(java.lang.String[] actionUrls) throws java.rmi.RemoteException;

    /**
     * Authenticate a user based on login information. You need to
     * have a cookie capable client in order to make authentication
     */
    public boolean userLogin(java.lang.String userName, java.lang.String password) throws java.rmi.RemoteException;

    /**
     * Logout user from StarLIMS system
     */
    public void userLogout() throws java.rmi.RemoteException;

    /**
     * Returns true is the user is logged to the system
     */
    public boolean userLogged() throws java.rmi.RemoteException;
}
