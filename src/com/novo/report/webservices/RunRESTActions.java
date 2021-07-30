/**
 * RunRESTActions.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.novo.report.webservices;

public class RunRESTActions  implements java.io.Serializable {
    private java.lang.String[] actionUrls;

    public RunRESTActions() {
    }

    public RunRESTActions(
           java.lang.String[] actionUrls) {
           this.actionUrls = actionUrls;
    }


    /**
     * Gets the actionUrls value for this RunRESTActions.
     * 
     * @return actionUrls
     */
    public java.lang.String[] getActionUrls() {
        return actionUrls;
    }


    /**
     * Sets the actionUrls value for this RunRESTActions.
     * 
     * @param actionUrls
     */
    public void setActionUrls(java.lang.String[] actionUrls) {
        this.actionUrls = actionUrls;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof RunRESTActions)) return false;
        RunRESTActions other = (RunRESTActions) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.actionUrls==null && other.getActionUrls()==null) || 
             (this.actionUrls!=null &&
              java.util.Arrays.equals(this.actionUrls, other.getActionUrls())));
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
        if (getActionUrls() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getActionUrls());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getActionUrls(), i);
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
        new org.apache.axis.description.TypeDesc(RunRESTActions.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.starlims.com/webservices/", ">RunRESTActions"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("actionUrls");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.starlims.com/webservices/", "actionUrls"));
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
