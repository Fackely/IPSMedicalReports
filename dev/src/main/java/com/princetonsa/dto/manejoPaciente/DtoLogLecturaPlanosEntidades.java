/*
 * Enero 17, 2008
 */
package com.princetonsa.dto.manejoPaciente;

import java.io.Serializable;

import util.ConstantesBD;
import util.InfoDatos;
import util.InfoDatosInt;

/**
 * Data Transfer Object: para almacenar la información del log de ejecucion de la lectura de planos entidades subcontratadas
 * @author Sebastián Gómez
 *
 */
public class DtoLogLecturaPlanosEntidades implements Serializable
{
	/**
	 * Consecutivo del log de ejecucion de la lectura de planos entidades
	 */
	private String consecutivo;
	
	/**
	 * Usuario ejecucion
	 */
	private InfoDatos usuarioEjecucion;
	
	/**
	 * Fecha de ejecucion
	 */
	private String fechaEjecucion;
	
	/**
	 * Hora de ejecucion
	 */
	private String horaEjecucion;
	
	/**
	 * Manual
	 */
	private InfoDatosInt manual;
	
	/**
	 * Entidad subcontratada
	 */
	private InfoDatos entidadSubcontratada;
	
	/**
	 * Número de factura
	 */
	private String numeroFactura;
	
	/**
	 * Indica si se deben validar los archivos del carnet
	 */
	private Boolean validarArchivosCarnet;
	
	/**
	 * Integridad dominio de la ubicacion de los planos : Cliente => CLI, Servidor => SER
	 */
	private String ubicacionPlanos;
	
	/**
	 * Archivo de inconsistencias
	 */
	private String archivoInconsistencias;
	
	/**
	 * Codigo de la institucion
	 */
	private int codigoInstitucion;
	
	/**
	 * Centro de atención
	 */
	private InfoDatosInt centroAtencion;
	
	/**
	 * Directorio de los archivos
	 */
	private String directorioArchivos;
	
	
	/**
	 * Resetea los atributos
	 *
	 */
	public void clean()
	{
		this.consecutivo = "";
		this.usuarioEjecucion = new InfoDatos("","");
		this.fechaEjecucion = "";
		this.horaEjecucion = "";
		this.manual = new InfoDatosInt(ConstantesBD.codigoNuncaValido,"");
		this.entidadSubcontratada = new InfoDatos("","");
		this.numeroFactura = "";
		this.validarArchivosCarnet = null;
		this.ubicacionPlanos = "";
		this.archivoInconsistencias = "";
		this.codigoInstitucion = ConstantesBD.codigoNuncaValido;
		this.centroAtencion = new InfoDatosInt(ConstantesBD.codigoNuncaValido,"");
		this.directorioArchivos = "";
	}
	
	/**
	 * Constructor
	 *
	 */
	public DtoLogLecturaPlanosEntidades()
	{
		this.clean();
	}

	/**
	 * @return the archivoInconsistencias
	 */
	public String getArchivoInconsistencias() {
		return archivoInconsistencias;
	}

	/**
	 * @param archivoInconsistencias the archivoInconsistencias to set
	 */
	public void setArchivoInconsistencias(String archivoInconsistencias) {
		this.archivoInconsistencias = archivoInconsistencias;
	}

	/**
	 * @return the centroAtencion
	 */
	public InfoDatosInt getCentroAtencion() {
		return centroAtencion;
	}

	/**
	 * @param centroAtencion the centroAtencion to set
	 */
	public void setCentroAtencion(InfoDatosInt centroAtencion) {
		this.centroAtencion = centroAtencion;
	}
	
	/**
	 * @return the centroAtencion
	 */
	public int getCodigoCentroAtencion() {
		return centroAtencion.getCodigo();
	}

	/**
	 * @param centroAtencion the centroAtencion to set
	 */
	public void setCodigoCentroAtencion(int centroAtencion) {
		this.centroAtencion.setCodigo(centroAtencion);
	}
	
	/**
	 * @return the centroAtencion
	 */
	public String getNombreCentroAtencion() {
		return centroAtencion.getNombre();
	}

	/**
	 * @param centroAtencion the centroAtencion to set
	 */
	public void setNombreCentroAtencion(String centroAtencion) {
		this.centroAtencion.setNombre(centroAtencion);
	}

	/**
	 * @return the codigoInstitucion
	 */
	public int getCodigoInstitucion() {
		return codigoInstitucion;
	}

	/**
	 * @param codigoInstitucion the codigoInstitucion to set
	 */
	public void setCodigoInstitucion(int codigoInstitucion) {
		this.codigoInstitucion = codigoInstitucion;
	}

	/**
	 * @return the consecutivo
	 */
	public String getConsecutivo() {
		return consecutivo;
	}

	/**
	 * @param consecutivo the consecutivo to set
	 */
	public void setConsecutivo(String consecutivo) {
		this.consecutivo = consecutivo;
	}

	/**
	 * @return the directorioArchivos
	 */
	public String getDirectorioArchivos() {
		return directorioArchivos;
	}

	/**
	 * @param directorioArchivos the directorioArchivos to set
	 */
	public void setDirectorioArchivos(String directorioArchivos) {
		this.directorioArchivos = directorioArchivos;
	}

	/**
	 * @return the entidadSubcontratada
	 */
	public InfoDatos getEntidadSubcontratada() {
		return entidadSubcontratada;
	}

	/**
	 * @param entidadSubcontratada the entidadSubcontratada to set
	 */
	public void setEntidadSubcontratada(InfoDatos entidadSubcontratada) {
		this.entidadSubcontratada = entidadSubcontratada;
	}
	
	/**
	 * @return the entidadSubcontratada
	 */
	public String getCodigoEntidadSubcontratada() {
		return entidadSubcontratada.getId();
	}

	/**
	 * @param entidadSubcontratada the entidadSubcontratada to set
	 */
	public void setCodigoEntidadSubcontratada(String entidadSubcontratada) {
		this.entidadSubcontratada.setId(entidadSubcontratada);
	}
	
	/**
	 * @return the entidadSubcontratada
	 */
	public String getNombreEntidadSubcontratada() {
		return entidadSubcontratada.getValue();
	}

	/**
	 * @param entidadSubcontratada the entidadSubcontratada to set
	 */
	public void setNombreEntidadSubcontratada(String entidadSubcontratada) {
		this.entidadSubcontratada.setValue(entidadSubcontratada);
	}

	/**
	 * @return the fechaEjecucion
	 */
	public String getFechaEjecucion() {
		return fechaEjecucion;
	}

	/**
	 * @param fechaEjecucion the fechaEjecucion to set
	 */
	public void setFechaEjecucion(String fechaEjecucion) {
		this.fechaEjecucion = fechaEjecucion;
	}

	/**
	 * @return the horaEjecucion
	 */
	public String getHoraEjecucion() {
		return horaEjecucion;
	}

	/**
	 * @param horaEjecucion the horaEjecucion to set
	 */
	public void setHoraEjecucion(String horaEjecucion) {
		this.horaEjecucion = horaEjecucion;
	}

	/**
	 * @return the manual
	 */
	public InfoDatosInt getManual() {
		return manual;
	}

	/**
	 * @param manual the manual to set
	 */
	public void setManual(InfoDatosInt manual) {
		this.manual = manual;
	}
	
	/**
	 * @return the manual
	 */
	public int getCodigoManual() {
		return manual.getCodigo();
	}

	/**
	 * @param manual the manual to set
	 */
	public void setCodigoManual(int manual) {
		this.manual.setCodigo(manual);
	}
	
	/**
	 * @return the manual
	 */
	public String getNombreManual() {
		return manual.getNombre();
	}

	/**
	 * @param manual the manual to set
	 */
	public void setNombreManual(String manual) {
		this.manual.setNombre(manual);
	}

	/**
	 * @return the numeroFactura
	 */
	public String getNumeroFactura() {
		return numeroFactura;
	}

	/**
	 * @param numeroFactura the numeroFactura to set
	 */
	public void setNumeroFactura(String numeroFactura) {
		this.numeroFactura = numeroFactura;
	}

	/**
	 * @return the ubicacionPlanos
	 */
	public String getUbicacionPlanos() {
		return ubicacionPlanos;
	}

	/**
	 * @param ubicacionPlanos the ubicacionPlanos to set
	 */
	public void setUbicacionPlanos(String ubicacionPlanos) {
		this.ubicacionPlanos = ubicacionPlanos;
	}

	/**
	 * @return the usuarioEjecucion
	 */
	public InfoDatos getUsuarioEjecucion() {
		return usuarioEjecucion;
	}

	/**
	 * @param usuarioEjecucion the usuarioEjecucion to set
	 */
	public void setUsuarioEjecucion(InfoDatos usuarioEjecucion) {
		this.usuarioEjecucion = usuarioEjecucion;
	}
	
	/**
	 * @return the usuarioEjecucion
	 */
	public String getLoginUsuarioEjecucion() {
		return usuarioEjecucion.getId();
	}

	/**
	 * @param usuarioEjecucion the usuarioEjecucion to set
	 */
	public void setLoginUsuarioEjecucion(String usuarioEjecucion) {
		this.usuarioEjecucion.setId(usuarioEjecucion);
	}
	
	/**
	 * @return the usuarioEjecucion
	 */
	public String getNombreUsuarioEjecucion() {
		return usuarioEjecucion.getValue();
	}

	/**
	 * @param usuarioEjecucion the usuarioEjecucion to set
	 */
	public void setNombreUsuarioEjecucion(String usuarioEjecucion) {
		this.usuarioEjecucion.setValue(usuarioEjecucion);
	}

	/**
	 * @return the validarArchivosCarnet
	 */
	public Boolean getValidarArchivosCarnet() {
		return validarArchivosCarnet;
	}

	/**
	 * @param validarArchivosCarnet the validarArchivosCarnet to set
	 */
	public void setValidarArchivosCarnet(Boolean validarArchivosCarnet) {
		this.validarArchivosCarnet = validarArchivosCarnet;
	}
	
	/**
	 * @param validarArchivosCarnet the validarArchivosCarnet to set
	 */
	public void setValidarArchivosCarnet(boolean validarArchivosCarnet) {
		this.validarArchivosCarnet = new Boolean(validarArchivosCarnet);
	}
}
