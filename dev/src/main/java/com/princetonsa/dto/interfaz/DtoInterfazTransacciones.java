package com.princetonsa.dto.interfaz;

import java.io.Serializable;

import util.ConstantesBD;

/**
 * 
 * @author Andr&eacute;s Mauricio Guerrero Toro
 *
 */
public class DtoInterfazTransacciones implements Serializable{
	
	/**
	 * campo codigo de la tabla transacciones por almacen
	 */
	private String consecutivo;
	
	private int transaccion;
	
	private String fecha_elaboracion;
	
	private String usuario;
	
	private String entidad;
	
	private String observaciones;
	
	private int estado;
	
	private int almacen;
	
	private String tipo_comprobante;
	
	private double nro_comprobante;
	
	private String contabilizado;
	
	private String usuario_cierre;
	
	private String fecha_cierre;
	
	private String hora_cierre;
	
	private String usuario_anula;
	
	private String fecha_anula;
	
	private String hora_anula;
	
	private String motivo_anula;
	
	private int secuencia;
	
	/**
	 * Inicializacion de los campos
	 *
	 */
	public DtoInterfazTransacciones()
	{
		this.consecutivo="";
		this.transaccion=ConstantesBD.codigoNuncaValido;
		this.fecha_elaboracion="";
		this.usuario="";
		this.entidad="";
		this.observaciones="";
		this.estado=ConstantesBD.codigoNuncaValido;
		this.almacen=ConstantesBD.codigoNuncaValido;
		this.tipo_comprobante="";
		this.nro_comprobante=ConstantesBD.codigoNuncaValido;
		this.contabilizado="";
		this.usuario_cierre="";
		this.fecha_cierre="";
		this.hora_cierre="";
		this.usuario_anula="";
		this.fecha_anula="";
		this.hora_anula="";
		this.secuencia=ConstantesBD.codigoNuncaValido;
	}
	
	public DtoInterfazTransacciones(String consecutivo, int transaccion,String fecha_elaboracion,String usuario,int almacen,String tipo_comprobante,double nro_comprobante)
	{
		this.consecutivo=consecutivo;
		this.transaccion=transaccion;
		this.fecha_elaboracion=fecha_elaboracion;
		this.usuario=usuario;
		this.entidad=null;
		this.observaciones=null;
		this.estado=2;
		this.almacen=almacen;
		this.tipo_comprobante=tipo_comprobante;
		this.nro_comprobante=nro_comprobante;
		this.contabilizado=null;
		this.usuario_cierre=null;
		this.fecha_cierre=null;
		this.hora_cierre=null;
		this.usuario_anula=null;
		this.fecha_anula=null;
		this.hora_anula=null;
	}
	
	
	
	/**
	 * @return the secuencia
	 */
	public int getSecuencia() {
		return secuencia;
	}

	/**
	 * @param secuencia the secuencia to set
	 */
	public void setSecuencia(int secuencia) {
		this.secuencia = secuencia;
	}

	public int getAlmacen() {
		return almacen;
	}

	public void setAlmacen(int almacen) {
		this.almacen = almacen;
	}

	public int getEstado() {
		return estado;
	}

	public void setEstado(int estado) {
		this.estado = estado;
	}

	public String getFecha_anula() {
		return fecha_anula;
	}

	public void setFecha_anula(String fecha_anula) {
		this.fecha_anula = fecha_anula;
	}

	public String getFecha_cierre() {
		return fecha_cierre;
	}

	public void setFecha_cierre(String fecha_cierre) {
		this.fecha_cierre = fecha_cierre;
	}

	public String getFecha_elaboracion() {
		return fecha_elaboracion;
	}

	public void setFecha_elaboracion(String fecha_elaboracion) {
		this.fecha_elaboracion = fecha_elaboracion;
	}

	public String getHora_anula() {
		return hora_anula;
	}

	public void setHora_anula(String hora_anula) {
		this.hora_anula = hora_anula;
	}

	public String getHora_cierre() {
		return hora_cierre;
	}

	public void setHora_cierre(String hora_cierre) {
		this.hora_cierre = hora_cierre;
	}

	public String getMotivo_anula() {
		return motivo_anula;
	}

	public void setMotivo_anula(String motivo_anula) {
		this.motivo_anula = motivo_anula;
	}

	public double getNro_comprobante() {
		return nro_comprobante;
	}

	public void setNro_comprobante(double nro_comprobante) {
		this.nro_comprobante = nro_comprobante;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public String getTipo_comprobante() {
		return tipo_comprobante;
	}

	public void setTipo_comprobante(String tipo_comprobante) {
		this.tipo_comprobante = tipo_comprobante;
	}

	public int getTransaccion() {
		return transaccion;
	}

	public void setTransaccion(int transaccion) {
		this.transaccion = transaccion;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getUsuario_anula() {
		return usuario_anula;
	}

	public void setUsuario_anula(String usuario_anula) {
		this.usuario_anula = usuario_anula;
	}

	public String getUsuario_cierre() {
		return usuario_cierre;
	}

	public void setUsuario_cierre(String usuario_cierre) {
		this.usuario_cierre = usuario_cierre;
	}

	public String getContabilizado() {
		return contabilizado;
	}

	public void setContabilizado(String contabilizado) {
		this.contabilizado = contabilizado;
	}

	public String getEntidad() {
		return entidad;
	}

	public void setEntidad(String entidad) {
		this.entidad = entidad;
	}

	public String getConsecutivo() {
		return consecutivo;
	}

	public void setConsecutivo(String consecutivo) {
		this.consecutivo = consecutivo;
	}
}
