package com.sysmedica.actionform;

import java.util.Collection;
import java.util.HashMap;
import org.apache.struts.validator.ValidatorForm;

public class ParamLaboratoriosForm extends ValidatorForm {

	private int evento;
	private int tipoSolicitud;
	private HashMap opcionesMuestra;
	private HashMap opcionesPrueba;
	private HashMap opcionesAgente;
	private HashMap opcionesResultado;
	
	private String estado;
	
	private Collection coleccion;
	private int codigoCups;
	private int codigoServicioEliminar;
	private HashMap mapaServicios;
	private int numeroElementos;
	
	private int codigoAxiomaNueva;
	private int codigoEspecialidadNueva;
	private String descripcionNueva;
	
	private String codigosServiciosInsertados;
	
	private int maxPageItems;
	
	private boolean guardoParametros;
	
	/**
     * Metodo para resetear los atributos 
     *
     */
    public void reset() {
    	
    	evento = 0;
    	tipoSolicitud = 0;
    	numeroElementos = 0;
    	codigoCups = 0;
    	
    	guardoParametros = false;
    	
    	try {
    		coleccion.clear();
    	}
    	catch (NullPointerException npe) {
    		
    	}
    }

	public int getEvento() {
		return evento;
	}

	public void setEvento(int evento) {
		this.evento = evento;
	}

	public HashMap getOpcionesAgente() {
		return opcionesAgente;
	}

	public void setOpcionesAgente(HashMap opcionesAgente) {
		this.opcionesAgente = opcionesAgente;
	}

	public HashMap getOpcionesMuestra() {
		return opcionesMuestra;
	}

	public void setOpcionesMuestra(HashMap opcionesMuestra) {
		this.opcionesMuestra = opcionesMuestra;
	}

	public HashMap getOpcionesPrueba() {
		return opcionesPrueba;
	}

	public void setOpcionesPrueba(HashMap opcionesPrueba) {
		this.opcionesPrueba = opcionesPrueba;
	}

	public HashMap getOpcionesResultado() {
		return opcionesResultado;
	}

	public void setOpcionesResultado(HashMap opcionesResultado) {
		this.opcionesResultado = opcionesResultado;
	}

	public int getTipoSolicitud() {
		return tipoSolicitud;
	}

	public void setTipoSolicitud(int tipoSolicitud) {
		this.tipoSolicitud = tipoSolicitud;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public Collection getColeccion() {
		return coleccion;
	}

	public void setColeccion(Collection coleccion) {
		this.coleccion = coleccion;
	}

	public int getCodigoCups() {
		return codigoCups;
	}

	public void setCodigoCups(int codigoCups) {
		this.codigoCups = codigoCups;
	}

	public int getCodigoServicioEliminar() {
		return codigoServicioEliminar;
	}

	public void setCodigoServicioEliminar(int codigoServicioEliminar) {
		this.codigoServicioEliminar = codigoServicioEliminar;
	}

	public HashMap getMapaServicios() {
		return mapaServicios;
	}

	public void setMapaServicios(HashMap mapaServicios) {
		this.mapaServicios = mapaServicios;
	}

	public int getNumeroElementos() {
		return numeroElementos;
	}

	public void setNumeroElementos(int numeroElementos) {
		this.numeroElementos = numeroElementos;
	}

	public String getCodigosServiciosInsertados() {
		return codigosServiciosInsertados;
	}

	public void setCodigosServiciosInsertados(String codigosServiciosInsertados) {
		this.codigosServiciosInsertados = codigosServiciosInsertados;
	}

	public int getCodigoAxiomaNueva() {
		return codigoAxiomaNueva;
	}

	public void setCodigoAxiomaNueva(int codigoAxiomaNueva) {
		this.codigoAxiomaNueva = codigoAxiomaNueva;
	}

	public int getCodigoEspecialidadNueva() {
		return codigoEspecialidadNueva;
	}

	public void setCodigoEspecialidadNueva(int codigoEspecialidadNueva) {
		this.codigoEspecialidadNueva = codigoEspecialidadNueva;
	}

	public String getDescripcionNueva() {
		return descripcionNueva;
	}

	public void setDescripcionNueva(String descripcionNueva) {
		this.descripcionNueva = descripcionNueva;
	}

	public int getMaxPageItems() {
		return maxPageItems;
	}

	public void setMaxPageItems(int maxPageItems) {
		this.maxPageItems = maxPageItems;
	}

	public boolean isGuardoParametros() {
		return guardoParametros;
	}

	public void setGuardoParametros(boolean guardoParametros) {
		this.guardoParametros = guardoParametros;
	}

    
    
}
