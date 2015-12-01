/*
 * Julio 22, 2009
 */
package com.princetonsa.dto.interfaz;

import java.io.Serializable;

import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;

/**
 * DTO para la representación de una cuenta contable
 * @author Sebastián Gómez R.
 *
 */
public class DtoCuentaContable  implements Cloneable , Serializable
{
	private String codigo;
	private String cuentaContable;
	private String descripcion;
	private boolean activo;
	private boolean manejaTerceros;
	private boolean manejoCentrosCosto;
	private boolean manejoBaseGravable;
	private String naturalezaCuenta;
	private int codigoInstitucion;
	private String anioVigencia;
	private String mensaje;
	
	public void reset()
	{
		this.codigo = "";
		this.cuentaContable = "";
		this.descripcion = "";
		this.activo = false;
		this.manejaTerceros = false;
		this.manejoCentrosCosto = false;
		this.manejoBaseGravable = false;
		this.naturalezaCuenta = "";
		this.codigoInstitucion = ConstantesBD.codigoNuncaValido;
		this.anioVigencia = "";
		this.mensaje = "";
	}
	
	/**
	 * Constructor
	 */
	public DtoCuentaContable()
	{
		reset();
	}

	/**
	 * @return the codigo
	 */
	public String getCodigo() {
		return codigo;
	}

	/**
	 * @param codigo the codigo to set
	 */
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	/**
	 * @return the cuentaContable
	 */
	public String getCuentaContable() {
		return cuentaContable;
	}

	/**
	 * @param cuentaContable the cuentaContable to set
	 */
	public void setCuentaContable(String cuentaContable) {
		this.cuentaContable = cuentaContable;
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
	 * @return the activo
	 */
	public boolean isActivo() {
		return activo;
	}

	/**
	 * @param activo the activo to set
	 */
	public void setActivo(boolean activo) {
		this.activo = activo;
	}

	/**
	 * @return the manejaTerceros
	 */
	public boolean isManejaTerceros() {
		return manejaTerceros;
	}

	/**
	 * @param manejaTerceros the manejaTerceros to set
	 */
	public void setManejaTerceros(boolean manejaTerceros) {
		this.manejaTerceros = manejaTerceros;
	}

	/**
	 * @return the manejoCentrosCosto
	 */
	public boolean isManejoCentrosCosto() {
		return manejoCentrosCosto;
	}

	/**
	 * @param manejoCentrosCosto the manejoCentrosCosto to set
	 */
	public void setManejoCentrosCosto(boolean manejoCentrosCosto) {
		this.manejoCentrosCosto = manejoCentrosCosto;
	}

	/**
	 * @return the manejoBaseGravable
	 */
	public boolean isManejoBaseGravable() {
		return manejoBaseGravable;
	}

	/**
	 * @param manejoBaseGravable the manejoBaseGravable to set
	 */
	public void setManejoBaseGravable(boolean manejoBaseGravable) {
		this.manejoBaseGravable = manejoBaseGravable;
	}

	/**
	 * @return the naturalezaCuenta
	 */
	public String getNaturalezaCuenta() {
		return naturalezaCuenta;
	}

	/**
	 * @param naturalezaCuenta the naturalezaCuenta to set
	 */
	public void setNaturalezaCuenta(String naturalezaCuenta) {
		this.naturalezaCuenta = naturalezaCuenta;
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
	 * @return the anioVigencia
	 */
	public String getAnioVigencia() {
		return anioVigencia;
	}

	/**
	 * @param anioVigencia the anioVigencia to set
	 */
	public void setAnioVigencia(String anioVigencia) {
		this.anioVigencia = anioVigencia;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}
	
	/***
	 * 
	 *  
	 */
	public Object clone(){
	        Object obj=null;
	        try{
	            obj=super.clone();
	        }catch(CloneNotSupportedException ex){
	        	Log4JManager.error(" no se puede duplicar");
	        }
	        return obj;
	    }
	
}
