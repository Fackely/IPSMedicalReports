//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-558 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.08.25 at 10:23:45 AM COT 
//


package com.princetonsa.autorizaciones.solicitudAutorizacionServicios;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.princetonsa.autorizaciones.solicitudAutorizacionServicios package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _SolicitudAutorizacionServicios_QNAME = new QName("", "SolicitudAutorizacionServicios");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.princetonsa.autorizaciones.solicitudAutorizacionServicios
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ProfesionalSalud }
     * 
     */
    public ProfesionalSalud createProfesionalSalud() {
        return new ProfesionalSalud();
    }

    /**
     * Create an instance of {@link InformacionPersonal }
     * 
     */
    public InformacionPersonal createInformacionPersonal() {
        return new InformacionPersonal();
    }

    /**
     * Create an instance of {@link Paciente }
     * 
     */
    public Paciente createPaciente() {
        return new Paciente();
    }

    /**
     * Create an instance of {@link ServicioSalud }
     * 
     */
    public ServicioSalud createServicioSalud() {
        return new ServicioSalud();
    }

    /**
     * Create an instance of {@link General }
     * 
     */
    public General createGeneral() {
        return new General();
    }

    /**
     * Create an instance of {@link Diagnostico }
     * 
     */
    public Diagnostico createDiagnostico() {
        return new Diagnostico();
    }

    /**
     * Create an instance of {@link EstructuraSolicitudAutorizacion }
     * 
     */
    public EstructuraSolicitudAutorizacion createEstructuraSolicitudAutorizacion() {
        return new EstructuraSolicitudAutorizacion();
    }

    /**
     * Create an instance of {@link NombreCompleto }
     * 
     */
    public NombreCompleto createNombreCompleto() {
        return new NombreCompleto();
    }

    /**
     * Create an instance of {@link Pagador }
     * 
     */
    public Pagador createPagador() {
        return new Pagador();
    }

    /**
     * Create an instance of {@link Identificacion }
     * 
     */
    public Identificacion createIdentificacion() {
        return new Identificacion();
    }

    /**
     * Create an instance of {@link UbicacionGeografica }
     * 
     */
    public UbicacionGeografica createUbicacionGeografica() {
        return new UbicacionGeografica();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EstructuraSolicitudAutorizacion }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "SolicitudAutorizacionServicios")
    public JAXBElement<EstructuraSolicitudAutorizacion> createSolicitudAutorizacionServicios(EstructuraSolicitudAutorizacion value) {
        return new JAXBElement<EstructuraSolicitudAutorizacion>(_SolicitudAutorizacionServicios_QNAME, EstructuraSolicitudAutorizacion.class, null, value);
    }

}
