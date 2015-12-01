package com.princetonsa.dto.cargos;

import java.io.Serializable;

import util.ConstantesBD;
import util.InfoDatos;
import util.InfoDatosInt;
import util.UtilidadFecha;

/**
 * 
 * Data Transfer Object que almacena la información del cargo directo
 * @author Sebastián Gómez R
 *
 */
public class DtoCargoDirecto implements Serializable
{
	/**
	 * Número de solicitud
	 */
	private String numeroSolicitud;
	/**
	 * Usuario
	 */
	private InfoDatos usuario;
	/**
	 * Tipo de recargo
	 */
	private InfoDatosInt tipoRecargo;
	/**
	 * Servicio solicitado
	 */
	private InfoDatosInt servicioSolicitado;
	/**
	 * Codigo del registro que hace enlace a los datos de historia clínica
	 */
	private String codigoDatosHC;
	
	/**
	 * Indica si el cargo directo de artículo maneja inventarios
	 */
	private boolean afectaInventarios;
	
	/**
	 * Indica si el cargo directo ya está registrado en la base de datos
	 */
	private boolean existeBaseDatos;
	

	/**
	 * 
	 */
	private String fechaEjecucion;
	
	
	/**
	 * Constructor
	 *
	 */
	public DtoCargoDirecto()
	{
		this.clean();
	}


	/**
	 * Método que inicia los datos del cargo directo
	 *
	 */
	private void clean() 
	{
		this.numeroSolicitud = "";
		this.usuario = new InfoDatos("","");
		this.tipoRecargo = new InfoDatosInt(ConstantesBD.codigoNuncaValido,"");
		this.servicioSolicitado = new InfoDatosInt(ConstantesBD.codigoNuncaValido,"");
		this.codigoDatosHC = "";
		this.afectaInventarios = true;
		this.existeBaseDatos = false;
		this.fechaEjecucion="";
	}


	/**
	 * @return the codigoDatosHC
	 */
	public String getCodigoDatosHC() {
		return codigoDatosHC;
	}


	/**
	 * @param codigoDatosHC the codigoDatosHC to set
	 */
	public void setCodigoDatosHC(String codigoDatosHC) {
		this.codigoDatosHC = codigoDatosHC;
	}


	/**
	 * @return the numeroSolicitud
	 */
	public String getNumeroSolicitud() {
		return numeroSolicitud;
	}


	/**
	 * @param numeroSolicitud the numeroSolicitud to set
	 */
	public void setNumeroSolicitud(String numeroSolicitud) {
		this.numeroSolicitud = numeroSolicitud;
	}


	/**
	 * @return the serviciosSolicitado
	 */
	public InfoDatosInt getServicioSolicitado() {
		return servicioSolicitado;
	}


	/**
	 * @param serviciosSolicitado the serviciosSolicitado to set
	 */
	public void setServicioSolicitado(InfoDatosInt servicioSolicitado) {
		this.servicioSolicitado = servicioSolicitado;
	}
	
	/**
	 * @return the serviciosSolicitado
	 */
	public int getCodigoServicioSolicitado() {
		return servicioSolicitado.getCodigo();
	}


	/**
	 * @param serviciosSolicitado the serviciosSolicitado to set
	 */
	public void setCodigoServicioSolicitado(int servicioSolicitado) {
		this.servicioSolicitado.setCodigo(servicioSolicitado);
	}
	
	/**
	 * @return the serviciosSolicitado
	 */
	public String getNombreServicioSolicitado() {
		return servicioSolicitado.getNombre();
	}


	/**
	 * @param serviciosSolicitado the serviciosSolicitado to set
	 */
	public void setNombreServicioSolicitado(String servicioSolicitado) {
		this.servicioSolicitado.setNombre(servicioSolicitado);
	}


	/**
	 * @return the tipoRecargo
	 */
	public InfoDatosInt getTipoRecargo() {
		return tipoRecargo;
	}


	/**
	 * @param tipoRecargo the tipoRecargo to set
	 */
	public void setTipoRecargo(InfoDatosInt tipoRecargo) {
		this.tipoRecargo = tipoRecargo;
	}
	
	/**
	 * @return the tipoRecargo
	 */
	public int getCodigoTipoRecargo() {
		return tipoRecargo.getCodigo();
	}


	/**
	 * @param tipoRecargo the tipoRecargo to set
	 */
	public void setCodigoTipoRecargo(int tipoRecargo) {
		this.tipoRecargo.setCodigo(tipoRecargo);
	}
	
	/**
	 * @return the tipoRecargo
	 */
	public String getNombreTipoRecargo() {
		return tipoRecargo.getNombre();
	}


	/**
	 * @param tipoRecargo the tipoRecargo to set
	 */
	public void setNombreTipoRecargo(String tipoRecargo) {
		this.tipoRecargo.setNombre(tipoRecargo);
	}


	/**
	 * @return the usuario
	 */
	public InfoDatos getUsuario() {
		return usuario;
	}


	/**
	 * @param usuario the usuario to set
	 */
	public void setUsuario(InfoDatos usuario) {
		this.usuario = usuario;
	}
	
	/**
	 * @return the usuario
	 */
	public String getLoginUsuario() {
		return usuario.getId();
	}


	/**
	 * @param usuario the usuario to set
	 */
	public void setLoginUsuario(String usuario) {
		this.usuario.setId(usuario);
	}
	
	/**
	 * @return the usuario
	 */
	public String getNombreUsuario() {
		return usuario.getValue();
	}


	/**
	 * @param usuario the usuario to set
	 */
	public void setNombreUsuario(String usuario) {
		this.usuario.setValue(usuario);
	}


	/**
	 * @return the afectaInventarios
	 */
	public boolean isAfectaInventarios() {
		return afectaInventarios;
	}


	/**
	 * @param afectaInventarios the afectaInventarios to set
	 */
	public void setAfectaInventarios(boolean afectaInventarios) {
		this.afectaInventarios = afectaInventarios;
	}


	/**
	 * @return the existeBaseDatos
	 */
	public boolean isExisteBaseDatos() {
		return existeBaseDatos;
	}


	/**
	 * @param existeBaseDatos the existeBaseDatos to set
	 */
	public void setExisteBaseDatos(boolean existeBaseDatos) {
		this.existeBaseDatos = existeBaseDatos;
	}


	public String getFechaEjecucion() {
		return fechaEjecucion;
	}


	public void setFechaEjecucion(String fechaEjecucion) {
		this.fechaEjecucion = fechaEjecucion;
	}



}
