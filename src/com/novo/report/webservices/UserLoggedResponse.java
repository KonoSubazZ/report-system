/**
 * UserLoggedResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.novo.report.webservices;

public class UserLoggedResponse  implements java.io.Serializable {
    private boolean userLoggedResult;

    public UserLoggedResponse() {
    }

    public UserLoggedResponse(
           boolean userLoggedResult) {
           this.userLoggedResult = userLoggedResult;
    }


    /**
     * Gets the userLoggedResult value for this UserLoggedResponse.
     * 
     * @return userLoggedResult
     */
    public boolean isUserLoggedResult() {
        return userLoggedResult;
    }


    /**
     * Sets the userLoggedResult value for this UserLoggedResponse.
     * 
     * @param userLoggedResult
     */
    public void setUserLoggedResult(boolean userLoggedResult) {
        this.userLoggedResult = userLoggedResult;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof UserLoggedResponse)) return false;
        UserLoggedResponse other = (UserLoggedResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.userLoggedResult == other.isUserLoggedResult();
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
        _hashCode += (isUserLoggedResult() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(UserLoggedResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.starlims.com/webservices/", ">UserLoggedResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("userLoggedResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.starlims.com/webservices/", "UserLoggedResult"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
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
