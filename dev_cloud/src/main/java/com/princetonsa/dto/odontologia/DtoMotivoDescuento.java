package com.princetonsa.dto.odontologia;

import java.io.Serializable;

import util.ConstantesBD;
import util.UtilidadFecha;

/**
 * 
 * @author axioma
 *
 */
public class DtoMotivoDescuento implements Serializable 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * atributos
	 */
	private double codigoPk;
	private String codigoMotivo;
	private int institucion;
	private String descripcion;
	private String tipo;
	private String activo;
	private String fechaModifica;
	private String horaModifica;
	private String usuarioModifica;
	private double codigoSolicitudDcto;
	
	/**
	 * Estado actual que tiene la solicitud de Descuento odontólogico que se esta modificando.
	 */
	private String estadoActualSolicitud;
	
	/**
	 * MODIFICACION ANEXO 816 
	 * VERSION 1.4
	 * nuevos atributos-indicativo
	 */
	private String indicativo;
	
	
	/**
	 * Constructor
	 */
	public DtoMotivoDescuento() {
		super();
		clean();
	}
	/**
	 * 
	 */
	public void clean()
	{
		this.codigoPk = ConstantesBD.codigoNuncaValido;
		this.codigoMotivo = "";
		this.institucion = ConstantesBD.codigoNuncaValido;
		this.descripcion = "";
		this.tipo = "";
		this.activo = "";
		this.fechaModifica = "";
		this.horaModifica = "";
		this.usuarioModifica = "";
		this.indicativo="";
		this.codigoSolicitudDcto=ConstantesBD.codigoNuncaValidoDouble;
		this.estadoActualSolicitud = "";
	}
	/**
	 * @return the codigoMotivo
	 */
	public String getCodigoMotivo() {
		return codigoMotivo;
	}
	/**
	 * @param codigoMotivo the codigoMotivo to set
	 */
	public void setCodigoMotivo(String codigoMotivo) {
		this.codigoMotivo = codigoMotivo;
	}
	/**
	 * @return the institucion
	 */
	public int getInstitucion() {
		return institucion;
	}
	/**
	 * @param institucion the institucion to set
	 */
	public void setInstitucion(int institucion) {
		this.institucion = institucion;
	}
	/**
	 * @return the descripcion
	 */
	public String getDescripcion() {
		return descripcion;
	}
	/**
	 * @param descripcion the descripcion to set
	 */
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	/**
	 * @return the tipo
	 */
	public String getTipo() {
		return tipo;
	}
	/**
	 * @param tipo the tipo to set
	 */
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	/**
	 * @return the activo
	 */
	public String getActivo() {
		return activo;
	}
	/**
	 * @param activo the activo to set
	 */
	public void setActivo(String activo) {
		this.activo = activo;
	}
	/**
	 * @return the fechaModifica
	 */
	public String getFechaModifica() {
		return fechaModifica;
	}
	/**
	 * @return the fechaModifica
	 */
	public String getFechaModificaFormatoBD() {
		return UtilidadFecha.validarFecha(this.fechaModifica)?UtilidadFecha.conversionFormatoFechaABD(this.fechaModifica):"";
	}
	
	/**
	 * @param fechaModifica the fechaModifica to set
	 */
	public void setFechaModifica(String fechaModifica) {
		this.fechaModifica = fechaModifica;
	}
	/**
	 * @return the horaModifica
	 */
	public String getHoraModifica() {
		return horaModifica;
	}
	/**
	 * @param horaModifica the horaModifica to set
	 */
	public void setHoraModifica(String horaModifica) {
		this.horaModifica = horaModifica;
	}
	/**
	 * @return the usuarioModifica
	 */
	public String getUsuarioModifica() {
		return usuarioModifica;
	}
	/**
	 * @param usuarioModifica the usuarioModifica to set
	 */
	public void setUsuarioModifica(String usuarioModifica) {
		this.usuarioModifica = usuarioModifica;
	}
	/**
	 * @return the codigoPk
	 */
	public double getCodigoPk() {
		return codigoPk;
	}
	/**
	 * @param codigoPk the codigoPk to set
	 */
	public void setCodigoPk(double codigoPk) {
		this.codigoPk = codigoPk;
	}
	public void setIndicativo(String indicativo) {
		this.indicativo = indicativo;
	}
	public String getIndicativo() {
		return indicativo;
	}
	public double getCodigoSolicitudDcto() {
		return codigoSolicitudDcto;
	}
	public void setCodigoSolicitudDcto(double codigoSolicitudDcto) {
		this.codigoSolicitudDcto = codigoSolicitudDcto;
	}
	/**
	 * @param estadoActualSolicitud the estadoActualSolicitud to set
	 */
	public void setEstadoActualSolicitud(String estadoActualSolicitud) {
		this.estadoActualSolicitud = estadoActualSolicitud;
	}
	/**
	 * @return the estadoActualSolicitud
	 */
	public String getEstadoActualSolicitud() {
		return estadoActualSolicitud;
	}
	
	
}
