/**
 * RunActionDirectResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.novo.report.webservices;

public class RunActionDirectResponse  implements java.io.Serializable {
    private java.lang.Object runActionDirectResult;

    public RunActionDirectResponse() {
    }

    public RunActionDirectResponse(
           java.lang.Object runActionDirectResult) {
           this.runActionDirectResult = runActionDirectResult;
    }


    /**
     * Gets the runActionDirectResult value for this RunActionDirectResponse.
     * 
     * @return runActionDirectResult
     */
    public java.lang.Object getRunActionDirectResult() {
        return runActionDirectResult;
    }


    /**
     * Sets the runActionDirectResult value for this RunActionDirectResponse.
     * 
     * @param runActionDirectResult
     */
    public void setRunActionDirectResult(java.lang.Object runActionDirectResult) {
        this.runActionDirectResult = runActionDirectResult;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof RunActionDirectResponse)) return false;
        RunActionDirectResponse other = (RunActionDirectResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.runActionDirectResult==null && other.getRunActionDirectResult()==null) || 
             (this.runActionDirectResult!=null &&
              this.runActionDirectResult.equals(other.getRunActionDirectResult())));
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
        if (getRunActionDirectResult() != null) {
            _hashCode += getRunActionDirectResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(RunActionDirectResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.starlims.com/webservices/", ">RunActionDirectResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("runActionDirectResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.starlims.com/webservices/", "RunActionDirectResult"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "anyType"));
        elemField.setMinOccurs(0);
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
