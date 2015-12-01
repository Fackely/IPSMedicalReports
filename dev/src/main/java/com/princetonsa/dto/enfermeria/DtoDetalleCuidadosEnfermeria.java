package com.princetonsa.dto.enfermeria;

import java.io.Serializable;

import util.ConstantesBD;

public class DtoDetalleCuidadosEnfermeria implements Serializable
{
	private int codigoPkDetalleProgramacion;
	
	private int codigoPkProgramacion;
	
	private String fecha;
	
	private String hora;
	
	private String activo;
	
	
	/**
	 * 
	 */
	public void clean()
	{
		this.codigoPkDetalleProgramacion = ConstantesBD.codigoNuncaValido;
		this.codigoPkProgramacion = ConstantesBD.codigoNuncaValido;
		this.fecha="";
		this.hora="";
		this.activo="";
	}
	
	
	/**
	 * 
	 */
	public DtoDetalleCuidadosEnfermeria()
	{
		this.clean();
	}

	

	/**
	 * 
	 * @return
	 */
	public String getFecha() {
		return fecha;
	}

	/**
	 * 
	 * @param fecha
	 */
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	/**
	 * 
	 * @return
	 */
	public String getHora() {
		return hora;
	}

	/**
	 * 
	 * @param hora
	 */
	public void setHora(String hora) {
		this.hora = hora;
	}

	/**
	 * 
	 * @return
	 */
	public String getActivo() {
		return activo;
	}

	/**
	 * 
	 * @param activo
	 */
	public void setActivo(String activo) {
		this.activo = activo;
	}


	/**
	 * @return the codigoPkDetalleProgramacion
	 */
	public int getCodigoPkDetalleProgramacion() {
		return codigoPkDetalleProgramacion;
	}


	/**
	 * @param codigoPkDetalleProgramacion the codigoPkDetalleProgramacion to set
	 */
	public void setCodigoPkDetalleProgramacion(int codigoPkDetalleProgramacion) {
		this.codigoPkDetalleProgramacion = codigoPkDetalleProgramacion;
	}


	/**
	 * @return the codigoPkProgramacion
	 */
	public int getCodigoPkProgramacion() {
		return codigoPkProgramacion;
	}


	/**
	 * @param codigoPkProgramacion the codigoPkProgramacion to set
	 */
	public void setCodigoPkProgramacion(int codigoPkProgramacion) {
		this.codigoPkProgramacion = codigoPkProgramacion;
	}	
}