//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-558 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.08.25 at 10:23:45 AM COT 
//


package com.princetonsa.autorizaciones.solicitudAutorizacionServicios;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for UbicacionGeografica complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="UbicacionGeografica">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="DireccionResidenciaHabitual" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="TelefonoFijo" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="Departamento" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Ciudad" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "UbicacionGeografica", propOrder = {
    "direccionResidenciaHabitual",
    "telefonoFijo",
    "departamento",
    "ciudad"
})
public class UbicacionGeografica {

    @XmlElement(name = "DireccionResidenciaHabitual", required = true)
    protected String direccionResidenciaHabitual;
    @XmlElement(name = "TelefonoFijo")
    protected int telefonoFijo;
    @XmlElement(name = "Departamento", required = true)
    protected String departamento;
    @XmlElement(name = "Ciudad", required = true)
    protected String ciudad;

    /**
     * Gets the value of the direccionResidenciaHabitual property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDireccionResidenciaHabitual() {
        return direccionResidenciaHabitual;
    }

    /**
     * Sets the value of the direccionResidenciaHabitual property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDireccionResidenciaHabitual(String value) {
        this.direccionResidenciaHabitual = value;
    }

    /**
     * Gets the value of the telefonoFijo property.
     * 
     */
    public int getTelefonoFijo() {
        return telefonoFijo;
    }

    /**
     * Sets the value of the telefonoFijo property.
     * 
     */
    public void setTelefonoFijo(int value) {
        this.telefonoFijo = value;
    }

    /**
     * Gets the value of the departamento property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDepartamento() {
        return departamento;
    }

    /**
     * Sets the value of the departamento property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDepartamento(String value) {
        this.departamento = value;
    }

    /**
     * Gets the value of the ciudad property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCiudad() {
        return ciudad;
    }

    /**
     * Sets the value of the ciudad property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCiudad(String value) {
        this.ciudad = value;
    }

}
