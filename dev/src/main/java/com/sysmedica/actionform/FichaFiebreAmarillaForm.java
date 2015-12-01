/*
 * Creado en 14-jul-2005
 * 
 * Autor Santiago Salamanca
 * SysMédica
 */
package com.sysmedica.actionform;

import java.util.Vector;

import org.apache.struts.action.ActionForm;

/**
 * @author santiago
 *
 */
public class FichaFiebreAmarillaForm extends ActionForm {

    private String fechaVacunacion;
    private String fechaHospitalizacion;
    private String lugarHospitalizacion;
    private String hematoxilina;
    private String inmunoHistoquimica;
    private String estadoPaciente;
    private String fechaDefuncion;
    private String lugarInfeccion;
    private String clasificacionFinal;
    private String criterioConfirmacion;
    private Vector desplazamientos;
    private Vector sintomas;
    private Vector examenesLaboratorio;
    /**
     * @return Returns the clasificacionFinal.
     */
    public String getClasificacionFinal() {
        return clasificacionFinal;
    }
    /**
     * @param clasificacionFinal The clasificacionFinal to set.
     */
    public void setClasificacionFinal(String clasificacionFinal) {
        this.clasificacionFinal = clasificacionFinal;
    }
    /**
     * @return Returns the criterioConfirmacion.
     */
    public String getCriterioConfirmacion() {
        return criterioConfirmacion;
    }
    /**
     * @param criterioConfirmacion The criterioConfirmacion to set.
     */
    public void setCriterioConfirmacion(String criterioConfirmacion) {
        this.criterioConfirmacion = criterioConfirmacion;
    }
    /**
     * @return Returns the desplazamientos.
     */
    public Vector getDesplazamientos() {
        return desplazamientos;
    }
    /**
     * @param desplazamientos The desplazamientos to set.
     */
    public void setDesplazamientos(Vector desplazamientos) {
        this.desplazamientos = desplazamientos;
    }
    /**
     * @return Returns the estadoPaciente.
     */
    public String getEstadoPaciente() {
        return estadoPaciente;
    }
    /**
     * @param estadoPaciente The estadoPaciente to set.
     */
    public void setEstadoPaciente(String estadoPaciente) {
        this.estadoPaciente = estadoPaciente;
    }
    /**
     * @return Returns the examenesLaboratorio.
     */
    public Vector getExamenesLaboratorio() {
        return examenesLaboratorio;
    }
    /**
     * @param examenesLaboratorio The examenesLaboratorio to set.
     */
    public void setExamenesLaboratorio(Vector examenesLaboratorio) {
        this.examenesLaboratorio = examenesLaboratorio;
    }
    /**
     * @return Returns the fechaDefuncion.
     */
    public String getFechaDefuncion() {
        return fechaDefuncion;
    }
    /**
     * @param fechaDefuncion The fechaDefuncion to set.
     */
    public void setFechaDefuncion(String fechaDefuncion) {
        this.fechaDefuncion = fechaDefuncion;
    }
    /**
     * @return Returns the fechaHospitalizacion.
     */
    public String getFechaHospitalizacion() {
        return fechaHospitalizacion;
    }
    /**
     * @param fechaHospitalizacion The fechaHospitalizacion to set.
     */
    public void setFechaHospitalizacion(String fechaHospitalizacion) {
        this.fechaHospitalizacion = fechaHospitalizacion;
    }
    /**
     * @return Returns the fechaVacunacion.
     */
    public String getFechaVacunacion() {
        return fechaVacunacion;
    }
    /**
     * @param fechaVacunacion The fechaVacunacion to set.
     */
    public void setFechaVacunacion(String fechaVacunacion) {
        this.fechaVacunacion = fechaVacunacion;
    }
    /**
     * @return Returns the hematoxilina.
     */
    public String getHematoxilina() {
        return hematoxilina;
    }
    /**
     * @param hematoxilina The hematoxilina to set.
     */
    public void setHematoxilina(String hematoxilina) {
        this.hematoxilina = hematoxilina;
    }
    /**
     * @return Returns the inmunoHistoquimica.
     */
    public String getInmunoHistoquimica() {
        return inmunoHistoquimica;
    }
    /**
     * @param inmunoHistoquimica The inmunoHistoquimica to set.
     */
    public void setInmunoHistoquimica(String inmunoHistoquimica) {
        this.inmunoHistoquimica = inmunoHistoquimica;
    }
    /**
     * @return Returns the lugarHospitalizacion.
     */
    public String getLugarHospitalizacion() {
        return lugarHospitalizacion;
    }
    /**
     * @param lugarHospitalizacion The lugarHospitalizacion to set.
     */
    public void setLugarHospitalizacion(String lugarHospitalizacion) {
        this.lugarHospitalizacion = lugarHospitalizacion;
    }
    /**
     * @return Returns the lugarInfeccion.
     */
    public String getLugarInfeccion() {
        return lugarInfeccion;
    }
    /**
     * @param lugarInfeccion The lugarInfeccion to set.
     */
    public void setLugarInfeccion(String lugarInfeccion) {
        this.lugarInfeccion = lugarInfeccion;
    }
    /**
     * @return Returns the sintomas.
     */
    public Vector getSintomas() {
        return sintomas;
    }
    /**
     * @param sintomas The sintomas to set.
     */
    public void setSintomas(Vector sintomas) {
        this.sintomas = sintomas;
    }
}
