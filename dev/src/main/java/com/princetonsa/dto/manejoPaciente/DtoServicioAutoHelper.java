package com.princetonsa.dto.manejoPaciente;

import java.util.List;

import com.princetonsa.dto.facturacion.DtoViewFinalidadesServ;

public class DtoServicioAutoHelper {
	
	
	private String codigoAxiomaJS;
	private String descripcionCupsJS;
	private String codigoEspecialidadJS;
	private String descripcionEspecialidadJS;
	private String esPosJS;
	private String codigoCupsJS;
	private String esNivelServicioContratadoJS;
	private String tipoServicioJS;
	private String justificarJS;
	private String coberturaJS;
	private String subcuentaJS; 
	private String atencionodonJS;
	private String minutosJS;
	private String codigoGrupoServicio;
	private String nombreGrupoServicio;
	
	private String cantidad;
	private String finalidad;
	private String urgente;
	
	private List<DtoViewFinalidadesServ> listaFinalidades;
	
	
	
	public DtoServicioAutoHelper() {
		super();
		this.codigoAxiomaJS = "";
		this.descripcionCupsJS = "";
		this.codigoEspecialidadJS = "";
		this.descripcionEspecialidadJS = "";
		this.esPosJS = "";
		this.codigoCupsJS = "";
		this.esNivelServicioContratadoJS = "";
		this.tipoServicioJS = "";
		this.justificarJS = "";
		this.coberturaJS = "";
		this.subcuentaJS = "";
		this.atencionodonJS = "";
		this.minutosJS = "";
		
		this.cantidad="";
		this.urgente="";
		this.finalidad="";
	}
	
	/**
	 * @return the codigoAxiomaJS
	 */
	public String getCodigoAxiomaJS() {
		return codigoAxiomaJS;
	}
	/**
	 * @param codigoAxiomaJS the codigoAxiomaJS to set
	 */
	public void setCodigoAxiomaJS(String codigoAxiomaJS) {
		this.codigoAxiomaJS = codigoAxiomaJS;
	}
	/**
	 * @return the descripcionCupsJS
	 */
	public String getDescripcionCupsJS() {
		return descripcionCupsJS;
	}
	/**
	 * @param descripcionCupsJS the descripcionCupsJS to set
	 */
	public void setDescripcionCupsJS(String descripcionCupsJS) {
		this.descripcionCupsJS = descripcionCupsJS;
	}
	/**
	 * @return the codigoEspecialidadJS
	 */
	public String getCodigoEspecialidadJS() {
		return codigoEspecialidadJS;
	}
	/**
	 * @param codigoEspecialidadJS the codigoEspecialidadJS to set
	 */
	public void setCodigoEspecialidadJS(String codigoEspecialidadJS) {
		this.codigoEspecialidadJS = codigoEspecialidadJS;
	}
	/**
	 * @return the descripcionEspecialidadJS
	 */
	public String getDescripcionEspecialidadJS() {
		return descripcionEspecialidadJS;
	}
	/**
	 * @param descripcionEspecialidadJS the descripcionEspecialidadJS to set
	 */
	public void setDescripcionEspecialidadJS(String descripcionEspecialidadJS) {
		this.descripcionEspecialidadJS = descripcionEspecialidadJS;
	}
	/**
	 * @return the esPosJS
	 */
	public String getEsPosJS() {
		return esPosJS;
	}
	/**
	 * @param esPosJS the esPosJS to set
	 */
	public void setEsPosJS(String esPosJS) {
		this.esPosJS = esPosJS;
	}
	/**
	 * @return the codigoCupsJS
	 */
	public String getCodigoCupsJS() {
		return codigoCupsJS;
	}
	/**
	 * @param codigoCupsJS the codigoCupsJS to set
	 */
	public void setCodigoCupsJS(String codigoCupsJS) {
		this.codigoCupsJS = codigoCupsJS;
	}
	/**
	 * @return the esNivelServicioContratadoJS
	 */
	public String getEsNivelServicioContratadoJS() {
		return esNivelServicioContratadoJS;
	}
	/**
	 * @param esNivelServicioContratadoJS the esNivelServicioContratadoJS to set
	 */
	public void setEsNivelServicioContratadoJS(String esNivelServicioContratadoJS) {
		this.esNivelServicioContratadoJS = esNivelServicioContratadoJS;
	}
	/**
	 * @return the tipoServicioJS
	 */
	public String getTipoServicioJS() {
		return tipoServicioJS;
	}
	/**
	 * @param tipoServicioJS the tipoServicioJS to set
	 */
	public void setTipoServicioJS(String tipoServicioJS) {
		this.tipoServicioJS = tipoServicioJS;
	}
	/**
	 * @return the justificarJS
	 */
	public String getJustificarJS() {
		return justificarJS;
	}
	/**
	 * @param justificarJS the justificarJS to set
	 */
	public void setJustificarJS(String justificarJS) {
		this.justificarJS = justificarJS;
	}
	/**
	 * @return the coberturaJS
	 */
	public String getCoberturaJS() {
		return coberturaJS;
	}
	/**
	 * @param coberturaJS the coberturaJS to set
	 */
	public void setCoberturaJS(String coberturaJS) {
		this.coberturaJS = coberturaJS;
	}
	/**
	 * @return the subcuentaJS
	 */
	public String getSubcuentaJS() {
		return subcuentaJS;
	}
	/**
	 * @param subcuentaJS the subcuentaJS to set
	 */
	public void setSubcuentaJS(String subcuentaJS) {
		this.subcuentaJS = subcuentaJS;
	}
	/**
	 * @return the atencionodonJS
	 */
	public String getAtencionodonJS() {
		return atencionodonJS;
	}
	/**
	 * @param atencionodonJS the atencionodonJS to set
	 */
	public void setAtencionodonJS(String atencionodonJS) {
		this.atencionodonJS = atencionodonJS;
	}
	/**
	 * @return the minutosJS
	 */
	public String getMinutosJS() {
		return minutosJS;
	}
	/**
	 * @param minutosJS the minutosJS to set
	 */
	public void setMinutosJS(String minutosJS) {
		this.minutosJS = minutosJS;
	}

	/**
	 * @return the cantidad
	 */
	public String getCantidad() {
		return cantidad;
	}

	/**
	 * @param cantidad the cantidad to set
	 */
	public void setCantidad(String cantidad) {
		this.cantidad = cantidad;
	}

	/**
	 * @return the finalidad
	 */
	public String getFinalidad() {
		return finalidad;
	}

	/**
	 * @param finalidad the finalidad to set
	 */
	public void setFinalidad(String finalidad) {
		this.finalidad = finalidad;
	}

	/**
	 * @return the urgente
	 */
	public String getUrgente() {
		return urgente;
	}

	/**
	 * @param urgente the urgente to set
	 */
	public void setUrgente(String urgente) {
		this.urgente = urgente;
	}

	/**
	 * @return the listaFinalidades
	 */
	public List<DtoViewFinalidadesServ> getListaFinalidades() {
		return listaFinalidades;
	}

	/**
	 * @param listaFinalidades the listaFinalidades to set
	 */
	public void setListaFinalidades(List<DtoViewFinalidadesServ> listaFinalidades) {
		this.listaFinalidades = listaFinalidades;
	}
	
	public String getCodigoGrupoServicio() {
		return codigoGrupoServicio;
	}

	public void setCodigoGrupoServicio(String codigoGrupoServicio) {
		this.codigoGrupoServicio = codigoGrupoServicio;
	}

	public String getNombreGrupoServicio() {
		return nombreGrupoServicio;
	}

	public void setNombreGrupoServicio(String nombreGrupoServicio) {
		this.nombreGrupoServicio = nombreGrupoServicio;
	}
	
	
	
	
	

}
