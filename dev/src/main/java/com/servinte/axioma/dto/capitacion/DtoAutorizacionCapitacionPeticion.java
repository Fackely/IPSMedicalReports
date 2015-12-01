package com.servinte.axioma.dto.capitacion;

import java.io.Serializable;

import util.ConstantesBD;
/**
 * Esta clase se encarga de contener los datos para las validaciones de 
 * autorizacion de capitación de peticiones 
 * 
 * @author Camilo Gómez
 *
 */
public class DtoAutorizacionCapitacionPeticion implements Serializable{

	private static final long serialVersionUID = 1L;

	/**Variable que almacena el número de la solictud */
	private int numeroSolicitudAutorizar;
	/**Variable que almacena el numero de la peticion*/
	private Integer numeroPeticion;
	/**Variable que almacena el estado de la autorización si se generó para la peticion*/
	private String estadoAutorizacion;
	/**Variable que indica si existe una Autorización de Capitación asociada a una Peticion*/
	private boolean existeAutorizPeticion;
	/**Variable que almacena el consecutivo de la autorizacion asociada a la Peticion*/
	private Long consecutivoAutorEntSub;
	/**Variable que almacena el consecutivo de la autorizacion de Entidad Subcontratada*/
	private String consecutivoAutorizacion;
	/**Variable que indica el tipo de solicitud para actualizar tabla por asocio con autorizacion*/
	private int tipoSolicitud;
	/**Variable que indica el codigo del servicio asociado a la autorizacion*/
	private Integer codigoServicio;
	/**Variable que indica el nombre del servicio asociado a la autorizacion*/
	private String nombreServicio;
	
	
	/**
	 * Método constructor de la clase
	 */
	public DtoAutorizacionCapitacionPeticion() {
		this.reset();
	}
	
	private void reset()
	{
		this.numeroSolicitudAutorizar		=ConstantesBD.codigoNuncaValido;
		this.numeroPeticion					=ConstantesBD.codigoNuncaValido;
		this.estadoAutorizacion				="";
		this.existeAutorizPeticion			=false;
		this.consecutivoAutorizacion		="";
		this.consecutivoAutorEntSub			=ConstantesBD.codigoNuncaValidoLong;
		this.tipoSolicitud					=ConstantesBD.codigoNuncaValido;
		this.codigoServicio					=ConstantesBD.codigoNuncaValido;
		this.nombreServicio					="";
	}
	
	
	public Integer getNumeroPeticion() {
		return numeroPeticion;
	}
	public void setNumeroPeticion(Integer numeroPeticion) {
		this.numeroPeticion = numeroPeticion;
	}
	public String getEstadoAutorizacion() {
		return estadoAutorizacion;
	}
	public void setEstadoAutorizacion(String estadoAutorizacion) {
		this.estadoAutorizacion = estadoAutorizacion;
	}
	public boolean isExisteAutorizPeticion() {
		return existeAutorizPeticion;
	}
	public void setExisteAutorizPeticion(boolean existeAutorizPeticion) {
		this.existeAutorizPeticion = existeAutorizPeticion;
	}
	public String getConsecutivoAutorizacion() {
		return consecutivoAutorizacion;
	}
	public void setConsecutivoAutorizacion(String consecutivoAutorizacion) {
		this.consecutivoAutorizacion = consecutivoAutorizacion;
	}

	public void setNumeroSolicitudAutorizar(int numeroSolicitudAutorizar) {
		this.numeroSolicitudAutorizar = numeroSolicitudAutorizar;
	}

	public int getNumeroSolicitudAutorizar() {
		return numeroSolicitudAutorizar;
	}
	
	public Long getConsecutivoAutorEntSub() {
		return consecutivoAutorEntSub;
	}

	public void setConsecutivoAutorEntSub(Long consecutivoAutorEntSub) {
		this.consecutivoAutorEntSub = consecutivoAutorEntSub;
	}

	public int getTipoSolicitud() {
		return tipoSolicitud;
	}

	public void setTipoSolicitud(int tipoSolicitud) {
		this.tipoSolicitud = tipoSolicitud;
	}

	public Integer getCodigoServicio() {
		return codigoServicio;
	}

	public void setCodigoServicio(Integer codigoServicio) {
		this.codigoServicio = codigoServicio;
	}

	public String getNombreServicio() {
		return nombreServicio;
	}

	public void setNombreServicio(String nombreServicio) {
		this.nombreServicio = nombreServicio;
	}
	
	
	
}
