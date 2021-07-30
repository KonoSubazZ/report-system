package com.novo.report.webservices;

public class GenericServicesSoapProxy implements com.novo.report.webservices.GenericServicesSoap {
  private String _endpoint = null;
  private com.novo.report.webservices.GenericServicesSoap genericServicesSoap = null;
  
  public GenericServicesSoapProxy() {
    _initGenericServicesSoapProxy();
  }
  
  public GenericServicesSoapProxy(String endpoint) {
    _endpoint = endpoint;
    _initGenericServicesSoapProxy();
  }
  
  private void _initGenericServicesSoapProxy() {
    try {
      genericServicesSoap = (new com.novo.report.webservices.GenericServicesLocator()).getGenericServicesSoap();
      if (genericServicesSoap != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)genericServicesSoap)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)genericServicesSoap)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (genericServicesSoap != null)
      ((javax.xml.rpc.Stub)genericServicesSoap)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public com.novo.report.webservices.GenericServicesSoap getGenericServicesSoap() {
    if (genericServicesSoap == null)
      _initGenericServicesSoapProxy();
    return genericServicesSoap;
  }
  
  public java.lang.Object runAction(java.lang.String actionID, java.lang.Object[] parameters) throws java.rmi.RemoteException{
    if (genericServicesSoap == null)
      _initGenericServicesSoapProxy();
    return genericServicesSoap.runAction(actionID, parameters);
  }
  
  public java.lang.Object runActionDirect(java.lang.String actionID, java.lang.Object[] parameters, java.lang.String userName, java.lang.String password) throws java.rmi.RemoteException{
    if (genericServicesSoap == null)
      _initGenericServicesSoapProxy();
    return genericServicesSoap.runActionDirect(actionID, parameters, userName, password);
  }
  
  public java.lang.String[] runRESTActions(java.lang.String[] actionUrls) throws java.rmi.RemoteException{
    if (genericServicesSoap == null)
      _initGenericServicesSoapProxy();
    return genericServicesSoap.runRESTActions(actionUrls);
  }
  
  public boolean userLogin(java.lang.String userName, java.lang.String password) throws java.rmi.RemoteException{
    if (genericServicesSoap == null)
      _initGenericServicesSoapProxy();
    return genericServicesSoap.userLogin(userName, password);
  }
  
  public void userLogout() throws java.rmi.RemoteException{
    if (genericServicesSoap == null)
      _initGenericServicesSoapProxy();
    genericServicesSoap.userLogout();
  }
  
  public boolean userLogged() throws java.rmi.RemoteException{
    if (genericServicesSoap == null)
      _initGenericServicesSoapProxy();
    return genericServicesSoap.userLogged();
  }
  
  
}