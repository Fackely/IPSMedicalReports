package com.princetonsa.dto.epicrisis;

import java.io.Serializable;

import util.ConstantesBD;

/**
 * 
 * @author axioma
 *
 */
public class DtoEpicrisis1 implements Serializable 
{
	/**
	 * 
	 */
	private int ingreso;
	
	/**
	 * 
	 */
	private String usuarioModifica;
	
	/**
	 * 
	 */
	private boolean finalizada;
	
	/**
	 * 
	 */
	private StringBuilder contenidoEncabezado;
	
	/**
	 * 
	 */
	private String fechaContenido;
	
	/**
	 * 
	 */
	private String horaContenido;

	/**
	 * 
	 * @param ingreso
	 * @param usuarioModifica
	 * @param finalizada
	 * @param contenidoEncabezado
	 * @param fechaContenido
	 * @param horaContenido
	 */
	public DtoEpicrisis1() 
	{
		super();
		this.ingreso = ConstantesBD.codigoNuncaValido;
		this.usuarioModifica = "";
		this.finalizada = false;
		this.contenidoEncabezado = new StringBuilder();
		this.fechaContenido = "";
		this.horaContenido = "";
	}

	/**
	 * @return the ingreso
	 */
	public int getIngreso() {
		return ingreso;
	}

	/**
	 * @param ingreso the ingreso to set
	 */
	public void setIngreso(int ingreso) {
		this.ingreso = ingreso;
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
	 * @return the finalizada
	 */
	public boolean isFinalizada() {
		return finalizada;
	}

	/**
	 * @return the finalizada
	 */
	public boolean getFinalizada() {
		return finalizada;
	}
	
	/**
	 * @param finalizada the finalizada to set
	 */
	public void setFinalizada(boolean finalizada) {
		this.finalizada = finalizada;
	}

	/**
	 * @return the contenidoEncabezado
	 */
	public StringBuilder getContenidoEncabezado() {
		return contenidoEncabezado;
	}

	/**
	 * @param contenidoEncabezado the contenidoEncabezado to set
	 */
	public void setContenidoEncabezado(StringBuilder contenidoEncabezado) {
		this.contenidoEncabezado = contenidoEncabezado;
	}

	/**
	 * @return the fechaContenido
	 */
	public String getFechaContenido() {
		return fechaContenido;
	}

	/**
	 * @param fechaContenido the fechaContenido to set
	 */
	public void setFechaContenido(String fechaContenido) {
		this.fechaContenido = fechaContenido;
	}

	/**
	 * @return the horaContenido
	 */
	public String getHoraContenido() {
		return horaContenido;
	}

	/**
	 * @param horaContenido the horaContenido to set
	 */
	public void setHoraContenido(String horaContenido) {
		this.horaContenido = horaContenido;
	}
	
	
}
