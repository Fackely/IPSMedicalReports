package com.princetonsa.dto.interfaz;

import java.io.Serializable;

import util.ConstantesBD;

public class DtoInterfazInventarios implements Serializable{
	
	/**
	 * Campo proveedor de la tabla convenio_proveedor en el sistema axioma
	 */
	private String proveedor;
	
	/**
	 * campo codigo_shaio el es traido de la tabla ax_conprov de las tablas de Shaio
	 */
	private String codigo_shaio;
	
	/**
	 * campo codigo_axioma el cual es cargado de la tabla articulo de axioma
	 */
	private int codigo_axioma;
	
	/**
	 * campo valor unitario de compra
	 */
	private Object val_uni_compra;
	
	/**
	 * campo valor unitario iva
	 */
	private Object val_uni_iva;
	
	/**
	 * campo de fecha de creacion
	 */
	private String fecha;
	
	/**
	 * campo hora de creacion
	 */
	private String hora;
	
	/**
	 * campo estado de registro
	 */
	private String estado_registro;
	
	/**
	 * 
	 */
	private String tipo_convenio;
	
	/**
	 * 
	 */
	private String numero_convenio;
	
	/**
	 * 
	 */
	private Object cantidad_convenio;
	
	/**
	 * 
	 */
	private Object cantidad_recibida;
	
	/**
	 * Inicializacion de los campos;
	 */
	public DtoInterfazInventarios()
	{
		this.proveedor="";
		this.codigo_shaio="";
		this.codigo_axioma=ConstantesBD.codigoNuncaValido;
		this.val_uni_compra=ConstantesBD.codigoNuncaValido;
		this.val_uni_iva=ConstantesBD.codigoNuncaValido;
		this.fecha="";
		this.hora="";
		this.estado_registro="";
		this.tipo_convenio="";
		this.numero_convenio="";
		this.cantidad_convenio=ConstantesBD.codigoNuncaValido;
		this.cantidad_recibida=ConstantesBD.codigoNuncaValido;
		
	}
	
	/**
	 * 
	 * @param proveedor
	 * @param codigo_shaio
	 * @param codigo_axioma
	 * @param val_uni_compra
	 * @param val_uni_iva
	 * @param fecha
	 * @param hora
	 * @param estado_registro
	 */
	public DtoInterfazInventarios(String tipo_convenio,String numero_convenio,String proveedor,String codigo_shaio,int codigo_axioma,Object cantidad_convenio,Object cantidad_recibida,Object val_uni_compra,Object val_uni_iva,String fecha,String hora,String estado_registro)
	{
		this.proveedor=proveedor;
		this.codigo_shaio=codigo_shaio;
		this.codigo_axioma=codigo_axioma;
		this.val_uni_compra=val_uni_compra;
		this.val_uni_iva=val_uni_iva;
		this.fecha=fecha;
		this.hora=hora;
		this.estado_registro=estado_registro;
		this.tipo_convenio=tipo_convenio;
		this.numero_convenio=numero_convenio;
		this.cantidad_convenio=cantidad_convenio;
		this.cantidad_recibida=cantidad_recibida;
	}

	public int getCodigo_axioma() {
		return codigo_axioma;
	}

	public void setCodigo_axioma(int codigo_axioma) {
		this.codigo_axioma = codigo_axioma;
	}

	public String getCodigo_shaio() {
		return codigo_shaio;
	}

	public void setCodigo_shaio(String codigo_shaio) {
		this.codigo_shaio = codigo_shaio;
	}

	public String getEstado_registro() {
		return estado_registro;
	}

	public void setEstado_registro(String estado_registro) {
		this.estado_registro = estado_registro;
	}

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public String getHora() {
		return hora;
	}

	public void setHora(String hora) {
		this.hora = hora;
	}

	public String getProveedor() {
		return proveedor;
	}

	public void setProveedor(String proveedor) {
		this.proveedor = proveedor;
	}

	public void setCantidad_recibida(double cantidad_recibida) {
		this.cantidad_recibida = cantidad_recibida;
	}

	public String getNumero_convenio() {
		return numero_convenio;
	}

	public void setNumero_convenio(String numero_convenio) {
		this.numero_convenio = numero_convenio;
	}

	public String getTipo_convenio() {
		return tipo_convenio;
	}

	public void setTipo_convenio(String tipo_convenio) {
		this.tipo_convenio = tipo_convenio;
	}

	/**
	 * @return the cantidad_convenio
	 */
	public Object getCantidad_convenio() {
		return cantidad_convenio;
	}

	/**
	 * @param cantidad_convenio the cantidad_convenio to set
	 */
	public void setCantidad_convenio(Object cantidad_convenio) {
		this.cantidad_convenio = cantidad_convenio;
	}

	/**
	 * @return the cantidad_recibida
	 */
	public Object getCantidad_recibida() {
		return cantidad_recibida;
	}

	/**
	 * @param cantidad_recibida the cantidad_recibida to set
	 */
	public void setCantidad_recibida(Object cantidad_recibida) {
		this.cantidad_recibida = cantidad_recibida;
	}

	/**
	 * @return the val_uni_compra
	 */
	public Object getVal_uni_compra() {
		return val_uni_compra;
	}

	/**
	 * @param val_uni_compra the val_uni_compra to set
	 */
	public void setVal_uni_compra(Object val_uni_compra) {
		this.val_uni_compra = val_uni_compra;
	}

	/**
	 * @return the val_uni_iva
	 */
	public Object getVal_uni_iva() {
		return val_uni_iva;
	}

	/**
	 * @param val_uni_iva the val_uni_iva to set
	 */
	public void setVal_uni_iva(Object val_uni_iva) {
		this.val_uni_iva = val_uni_iva;
	}
}
