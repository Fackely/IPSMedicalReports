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
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for General complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="General">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Numero" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="Fecha" type="{http://www.w3.org/2001/XMLSchema}date"/>
 *         &lt;element name="Hora" type="{http://www.w3.org/2001/XMLSchema}time"/>
 *         &lt;element name="Prestador" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="TipoIdPrestador" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="IDPrestador" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="DigVerif" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="CodPrestador" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="DireccionPrestador" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="IndicTelefPrestador" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="TelefonoPrestador" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="DepartamentoPrestador" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="MunicipioPrestador" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "General", propOrder = {
    "numero",
    "fecha",
    "hora",
    "prestador",
    "tipoIdPrestador",
    "idPrestador",
    "digVerif",
    "codPrestador",
    "direccionPrestador",
    "indicTelefPrestador",
    "telefonoPrestador",
    "departamentoPrestador",
    "municipioPrestador"
})
public class General {

    @XmlElement(name = "Numero")
    protected int numero;
    @XmlElement(name = "Fecha", required = true)
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar fecha;
    @XmlElement(name = "Hora", required = true)
    @XmlSchemaType(name = "time")
    protected XMLGregorianCalendar hora;
    @XmlElement(name = "Prestador", required = true)
    protected String prestador;
    @XmlElement(name = "TipoIdPrestador", required = true)
    protected String tipoIdPrestador;
    @XmlElement(name = "IDPrestador", required = true)
    protected String idPrestador;
    @XmlElement(name = "DigVerif")
    protected int digVerif;
    @XmlElement(name = "CodPrestador", required = true)
    protected String codPrestador;
    @XmlElement(name = "DireccionPrestador", required = true)
    protected String direccionPrestador;
    @XmlElement(name = "IndicTelefPrestador")
    protected int indicTelefPrestador;
    @XmlElement(name = "TelefonoPrestador")
    protected int telefonoPrestador;
    @XmlElement(name = "DepartamentoPrestador", required = true)
    protected String departamentoPrestador;
    @XmlElement(name = "MunicipioPrestador", required = true)
    protected String municipioPrestador;

    /**
     * Gets the value of the numero property.
     * 
     */
    public int getNumero() {
        return numero;
    }

    /**
     * Sets the value of the numero property.
     * 
     */
    public void setNumero(int value) {
        this.numero = value;
    }

    /**
     * Gets the value of the fecha property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getFecha() {
        return fecha;
    }

    /**
     * Sets the value of the fecha property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setFecha(XMLGregorianCalendar value) {
        this.fecha = value;
    }

    /**
     * Gets the value of the hora property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getHora() {
        return hora;
    }

    /**
     * Sets the value of the hora property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setHora(XMLGregorianCalendar value) {
        this.hora = value;
    }

    /**
     * Gets the value of the prestador property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPrestador() {
        return prestador;
    }

    /**
     * Sets the value of the prestador property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPrestador(String value) {
        this.prestador = value;
    }

    /**
     * Gets the value of the tipoIdPrestador property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipoIdPrestador() {
        return tipoIdPrestador;
    }

    /**
     * Sets the value of the tipoIdPrestador property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipoIdPrestador(String value) {
        this.tipoIdPrestador = value;
    }

    /**
     * Gets the value of the idPrestador property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIDPrestador() {
        return idPrestador;
    }

    /**
     * Sets the value of the idPrestador property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIDPrestador(String value) {
        this.idPrestador = value;
    }

    /**
     * Gets the value of the digVerif property.
     * 
     */
    public int getDigVerif() {
        return digVerif;
    }

    /**
     * Sets the value of the digVerif property.
     * 
     */
    public void setDigVerif(int value) {
        this.digVerif = value;
    }

    /**
     * Gets the value of the codPrestador property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodPrestador() {
        return codPrestador;
    }

    /**
     * Sets the value of the codPrestador property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodPrestador(String value) {
        this.codPrestador = value;
    }

    /**
     * Gets the value of the direccionPrestador property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDireccionPrestador() {
        return direccionPrestador;
    }

    /**
     * Sets the value of the direccionPrestador property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDireccionPrestador(String value) {
        this.direccionPrestador = value;
    }

    /**
     * Gets the value of the indicTelefPrestador property.
     * 
     */
    public int getIndicTelefPrestador() {
        return indicTelefPrestador;
    }

    /**
     * Sets the value of the indicTelefPrestador property.
     * 
     */
    public void setIndicTelefPrestador(int value) {
        this.indicTelefPrestador = value;
    }

    /**
     * Gets the value of the telefonoPrestador property.
     * 
     */
    public int getTelefonoPrestador() {
        return telefonoPrestador;
    }

    /**
     * Sets the value of the telefonoPrestador property.
     * 
     */
    public void setTelefonoPrestador(int value) {
        this.telefonoPrestador = value;
    }

    /**
     * Gets the value of the departamentoPrestador property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDepartamentoPrestador() {
        return departamentoPrestador;
    }

    /**
     * Sets the value of the departamentoPrestador property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDepartamentoPrestador(String value) {
        this.departamentoPrestador = value;
    }

    /**
     * Gets the value of the municipioPrestador property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMunicipioPrestador() {
        return municipioPrestador;
    }

    /**
     * Sets the value of the municipioPrestador property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMunicipioPrestador(String value) {
        this.municipioPrestador = value;
    }

}
