/**
 * RunAction.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.novo.report.webservices;

public class RunAction  implements java.io.Serializable {
    private java.lang.String actionID;

    private java.lang.Object[] parameters;

    public RunAction() {
    }

    public RunAction(
           java.lang.String actionID,
           java.lang.Object[] parameters) {
           this.actionID = actionID;
           this.parameters = parameters;
    }


    /**
     * Gets the actionID value for this RunAction.
     * 
     * @return actionID
     */
    public java.lang.String getActionID() {
        return actionID;
    }


    /**
     * Sets the actionID value for this RunAction.
     * 
     * @param actionID
     */
    public void setActionID(java.lang.String actionID) {
        this.actionID = actionID;
    }


    /**
     * Gets the parameters value for this RunAction.
     * 
     * @return parameters
     */
    public java.lang.Object[] getParameters() {
        return parameters;
    }


    /**
     * Sets the parameters value for this RunAction.
     * 
     * @param parameters
     */
    public void setParameters(java.lang.Object[] parameters) {
        this.parameters = parameters;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof RunAction)) return false;
        RunAction other = (RunAction) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.actionID==null && other.getActionID()==null) || 
             (this.actionID!=null &&
              this.actionID.equals(other.getActionID()))) &&
            ((this.parameters==null && other.getParameters()==null) || 
             (this.parameters!=null &&
              java.util.Arrays.equals(this.parameters, other.getParameters())));
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
        if (getActionID() != null) {
            _hashCode += getActionID().hashCode();
        }
        if (getParameters() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getParameters());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getParameters(), i);
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
        new org.apache.axis.description.TypeDesc(RunAction.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.starlims.com/webservices/", ">RunAction"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("actionID");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.starlims.com/webservices/", "actionID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("parameters");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.starlims.com/webservices/", "parameters"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "anyType"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("http://www.starlims.com/webservices/", "anyType"));
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
