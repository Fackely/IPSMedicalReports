//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-558 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.08.25 at 10:21:32 AM COT 
//


package com.princetonsa.autorizaciones.informePresuntaInconsistencia;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for VariableInconsistencia.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="VariableInconsistencia">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="PrimerApellido"/>
 *     &lt;enumeration value="SegundoApellido"/>
 *     &lt;enumeration value="PrimerNombre"/>
 *     &lt;enumeration value="SegundoNombre"/>
 *     &lt;enumeration value="TipoIdentificacion"/>
 *     &lt;enumeration value="NumeroIdentificacion"/>
 *     &lt;enumeration value="FechaNacimiento"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "VariableInconsistencia")
@XmlEnum
public enum VariableInconsistencia {

    @XmlEnumValue("PrimerApellido")
    PRIMER_APELLIDO("PrimerApellido"),
    @XmlEnumValue("SegundoApellido")
    SEGUNDO_APELLIDO("SegundoApellido"),
    @XmlEnumValue("PrimerNombre")
    PRIMER_NOMBRE("PrimerNombre"),
    @XmlEnumValue("SegundoNombre")
    SEGUNDO_NOMBRE("SegundoNombre"),
    @XmlEnumValue("TipoIdentificacion")
    TIPO_IDENTIFICACION("TipoIdentificacion"),
    @XmlEnumValue("NumeroIdentificacion")
    NUMERO_IDENTIFICACION("NumeroIdentificacion"),
    @XmlEnumValue("FechaNacimiento")
    FECHA_NACIMIENTO("FechaNacimiento");
    private final String value;

    VariableInconsistencia(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static VariableInconsistencia fromValue(String v) {
        for (VariableInconsistencia c: VariableInconsistencia.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
