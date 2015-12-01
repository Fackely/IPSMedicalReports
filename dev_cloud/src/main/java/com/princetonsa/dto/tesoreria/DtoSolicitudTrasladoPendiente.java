/**
 * 
 */
package com.princetonsa.dto.tesoreria;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * Encaps&uacute;la informaci&oacute;n para mostrar las solicitudes pendientes
 * @author Cristhian Murillo
 */
@SuppressWarnings("serial")
public class DtoSolicitudTrasladoPendiente implements Serializable{
	
	/**
	 * Codigo del movimiento de caja que realizo la solicitud
	 */
	private long movimientoCaja;
	
	/**
	 * Fecha de la solicitud
	 */
	private Date fecha;
	
	/**
	 * Hora de la solicitud
	 */
	private String hora;
	
	/**
	 * Numero de la caja que entrega
	 */
	private int consecutivo;
	
	/**
	 * Descripcion de la caja que entrega
	 */
	private String descripcion;
	
	/**
	 * Nombres del cajero que entrega
	 */
	private String primerNombre;
	
	/**
	 * Nombres del cajero que entrega
	 */
	private String primerApellido;
	
	/**
	 * Detalles de las formas de pago
	 */
	private ArrayList<DtoDetalleFormaPago> listaDetalleFormasPago;
	
	/**
	 * Detalles de las formas de pago
	 */
	private ArrayList<DtoCuadreCaja> listaCuadreCaja;
	
	/**
	 * Variable usada para indicar que la solicitud fue 
	 * aceptada pero no ha sido registrado  el cambio en el sistema 
	 */
	private boolean aceptadoTemporalmente;
	
	
	/**
	 * Constructor sin parametros REQUERIDO para reflexion
	 */
	public DtoSolicitudTrasladoPendiente() 
	{	
		this.aceptadoTemporalmente	= false;
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * @return the listaCuadreCaja
	 */
	public ArrayList<DtoCuadreCaja> getListaCuadreCaja() {
		return listaCuadreCaja;
	}


	/**
	 * @param listaCuadreCaja the listaCuadreCaja to set
	 */
	public void setListaCuadreCaja(ArrayList<DtoCuadreCaja> listaCuadreCaja) {
		this.listaCuadreCaja = listaCuadreCaja;
	}


	/**
	 * @return the listaDetalleFormasPago
	 */
	public ArrayList<DtoDetalleFormaPago> getListaDetalleFormasPago() {
		return listaDetalleFormasPago;
	}


	/**
	 * @param listaDetalleFormasPago the listaDetalleFormasPago to set
	 */
	public void setListaDetalleFormasPago(
			ArrayList<DtoDetalleFormaPago> listaDetalleFormasPago) {
		this.listaDetalleFormasPago = listaDetalleFormasPago;
	}


	
	/**
	 * @return the movimientoCaja
	 */
	public long getMovimientoCaja() {
		return movimientoCaja;
	}
	/**
	 * @param movimientoCaja the movimientoCaja to set
	 */
	public void setMovimientoCaja(long movimientoCaja) {
		this.movimientoCaja = movimientoCaja;
	}
	/**
	 * @return the fecha
	 */
	public Date getFecha() {
		return fecha;
	}
	/**
	 * @param fecha the fecha to set
	 */
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	/**
	 * @return the hora
	 */
	public String getHora() {
		return hora;
	}
	/**
	 * @param hora the hora to set
	 */
	public void setHora(String hora) {
		this.hora = hora;
	}
	/**
	 * @return the consecutivo
	 */
	public int getConsecutivo() {
		return consecutivo;
	}
	/**
	 * @param consecutivo the consecutivo to set
	 */
	public void setConsecutivo(int consecutivo) {
		this.consecutivo = consecutivo;
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
	 * @return the primerNombre
	 */
	public String getPrimerNombre() {
		return primerNombre;
	}
	/**
	 * @param primerNombre the primerNombre to set
	 */
	public void setPrimerNombre(String primerNombre) {
		this.primerNombre = primerNombre;
	}
	/**
	 * @return the primerApellido
	 */
	public String getPrimerApellido() {
		return primerApellido;
	}
	/**
	 * @param primerApellido the primerApellido to set
	 */
	public void setPrimerApellido(String primerApellido) {
		this.primerApellido = primerApellido;
	}


	/**
	 * @return the aceptadoTemporalmente
	 */
	public boolean isAceptadoTemporalmente() {
		return aceptadoTemporalmente;
	}


	/**
	 * @param aceptadoTemporalmente the aceptadoTemporalmente to set
	 */
	public void setAceptadoTemporalmente(boolean aceptadoTemporalmente) {
		this.aceptadoTemporalmente = aceptadoTemporalmente;
	}
	
}
