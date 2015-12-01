package com.mercury.mundo.odontologia;

import com.princetonsa.mundo.UsuarioBasico;

public class EvolucionOdontologia
{
	private int						codigo;

	private String					descripcion;

	private transient UsuarioBasico	medico;

	private String					fecha;

	private String					observaciones;

	private String					obsNuevas;

	private boolean					enBD;

	public EvolucionOdontologia()
	{
		this.codigo = -1;
		this.descripcion = "";
		this.medico = new UsuarioBasico();
		this.fecha = "";
		this.observaciones = "";
		this.obsNuevas = "";
		this.enBD = false;
	}

	public int getCodigo()
	{
		return this.codigo;
	}

	public void setCodigo(int codigo)
	{
		this.codigo = codigo;
	}

	public String getDescripcion()
	{
		return this.descripcion;
	}

	public void setDescripcion(String descripcion)
	{
		this.descripcion = descripcion;
	}

	public UsuarioBasico getMedico()
	{
		return this.medico;
	}

	public void setMedico(UsuarioBasico medico)
	{
		this.medico = medico;
	}

	public String getFecha()
	{
		return this.fecha;
	}

	public void setFecha(String fecha)
	{
		this.fecha = fecha;
	}

	public String getObservaciones()
	{
		return this.observaciones;
	}

	public void setObservaciones(String observaciones)
	{
		this.observaciones = observaciones;
	}

	public String getObsAnteriores()
	{
		return this.observaciones;
	}

	public void setObsAnteriores(String obsAnteriores)
	{
		this.observaciones = obsAnteriores;
	}

	public String getObsNuevas()
	{
		return this.obsNuevas;
	}

	public void setObsNuevas(String obsNuevas)
	{
		this.obsNuevas = obsNuevas;
	}

	public boolean getEnBD()
	{
		return this.enBD;
	}

	public void setEnBD(boolean enBD)
	{
		this.enBD = enBD;
	}
}
