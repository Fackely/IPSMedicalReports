package com.princetonsa.dto.tesoreria;

import java.io.Serializable;

import util.ConstantesBD;

public class DtoProcesoDevolucionRC implements Serializable 
{
	private String numeroRC;
	
	private int institucion;
	
	private String usuario;
	
	private String fecha;
	
	private String hora;
	
	private String idSesion;
	
	private boolean existeConcurrencia;
	
	
	/**
	 * Constructor
	 */
	public void reset()
	{
		this.numeroRC="";
		this.institucion=ConstantesBD.codigoNuncaValido;
		this.usuario="";
		this.fecha="";
		this.hora="";
		this.idSesion="";
	}
	
	public DtoProcesoDevolucionRC()
	{
		this.reset();
	}

	public String getNumeroRC() {
		return numeroRC;
	}

	public void setNumeroRC(String numeroRC) {
		this.numeroRC = numeroRC;
	}

	public int getInstitucion() {
		return institucion;
	}

	public void setInstitucion(int institucion) {
		this.institucion = institucion;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
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

	public String getIdSesion() {
		return idSesion;
	}

	public void setIdSesion(String idSesion) {
		this.idSesion = idSesion;
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean existeConcurrencia()
	{
		if(!numeroRC.isEmpty()&&institucion>0)
			return true;
		else
			return false;
	}
	
}