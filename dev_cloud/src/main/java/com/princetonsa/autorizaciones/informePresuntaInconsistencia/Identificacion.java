//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-558 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.08.25 at 10:21:32 AM COT 
//


package com.princetonsa.autorizaciones.informePresuntaInconsistencia;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Identificacion complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Identificacion">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="TipoIdentificacion" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="NumeroIdentificacion" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Identificacion", propOrder = {
    "tipoIdentificacion",
    "numeroIdentificacion"
})
public class Identificacion {

    @XmlElement(name = "TipoIdentificacion", required = true)
    protected String tipoIdentificacion;
    @XmlElement(name = "NumeroIdentificacion", required = true)
    protected String numeroIdentificacion;

    /**
     * Gets the value of the tipoIdentificacion property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipoIdentificacion() {
        return tipoIdentificacion;
    }

    /**
     * Sets the value of the tipoIdentificacion property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipoIdentificacion(String value) {
        this.tipoIdentificacion = value;
    }

    /**
     * Gets the value of the numeroIdentificacion property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumeroIdentificacion() {
        return numeroIdentificacion;
    }

    /**
     * Sets the value of the numeroIdentificacion property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumeroIdentificacion(String value) {
        this.numeroIdentificacion = value;
    }

}
