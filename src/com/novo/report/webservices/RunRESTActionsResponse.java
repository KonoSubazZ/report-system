/**
 * RunRESTActionsResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.novo.report.webservices;

public class RunRESTActionsResponse  implements java.io.Serializable {
    private java.lang.String[] runRESTActionsResult;

    public RunRESTActionsResponse() {
    }

    public RunRESTActionsResponse(
           java.lang.String[] runRESTActionsResult) {
           this.runRESTActionsResult = runRESTActionsResult;
    }


    /**
     * Gets the runRESTActionsResult value for this RunRESTActionsResponse.
     * 
     * @return runRESTActionsResult
     */
    public java.lang.String[] getRunRESTActionsResult() {
        return runRESTActionsResult;
    }


    /**
     * Sets the runRESTActionsResult value for this RunRESTActionsResponse.
     * 
     * @param runRESTActionsResult
     */
    public void setRunRESTActionsResult(java.lang.String[] runRESTActionsResult) {
        this.runRESTActionsResult = runRESTActionsResult;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof RunRESTActionsResponse)) return false;
        RunRESTActionsResponse other = (RunRESTActionsResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.runRESTActionsResult==null && other.getRunRESTActionsResult()==null) || 
             (this.runRESTActionsResult!=null &&
              java.util.Arrays.equals(this.runRESTActionsResult, other.getRunRESTActionsResult())));
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
        if (getRunRESTActionsResult() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getRunRESTActionsResult());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getRunRESTActionsResult(), i);
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
        new org.apache.axis.description.TypeDesc(RunRESTActionsResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.starlims.com/webservices/", ">RunRESTActionsResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("runRESTActionsResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.starlims.com/webservices/", "RunRESTActionsResult"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("http://www.starlims.com/webservices/", "string"));
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
