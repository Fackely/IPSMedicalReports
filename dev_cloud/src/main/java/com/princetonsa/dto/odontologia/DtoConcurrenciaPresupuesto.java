package com.princetonsa.dto.odontologia;

import java.io.Serializable;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMessage;

/**
 * 
 * @author axioma
 *
 */
public class DtoConcurrenciaPresupuesto implements Serializable
{
	/**
	 * 
	 */
	private Double cuenta;
	
	/**
	 * 
	 */
	private DtoInfoFechaUsuario dtoFHU;
	
	/**
	 * 
	 */
	private String idSesion;
	
	/**
	 * 
	 */
	private String nombresUsuario;

	/**
	 * 
	 */
	private boolean esFlujoInclusiones;
	
	/**
	 * 
	 */
	public DtoConcurrenciaPresupuesto()
	{
		super();
		this.cuenta = new Double(0);
		this.dtoFHU = new DtoInfoFechaUsuario();
		this.idSesion = "";
		this.nombresUsuario="";
		this.esFlujoInclusiones=false;
	}

	/**
	 * @return the cuenta
	 */
	public Double getCuenta()
	{
		return cuenta;
	}

	/**
	 * @param cuenta the cuenta to set
	 */
	public void setCuenta(Double cuenta)
	{
		this.cuenta = cuenta;
	}

	/**
	 * @return the dtoFHU
	 */
	public DtoInfoFechaUsuario getDtoFHU()
	{
		return dtoFHU;
	}

	/**
	 * @param dtoFHU the dtoFHU to set
	 */
	public void setDtoFHU(DtoInfoFechaUsuario dtoFHU)
	{
		this.dtoFHU = dtoFHU;
	}

	/**
	 * @return the idSesion
	 */
	public String getIdSesion()
	{
		return idSesion;
	}

	/**
	 * @param idSesion the idSesion to set
	 */
	public void setIdSesion(String idSesion)
	{
		this.idSesion = idSesion;
	}

	/**
	 * @return the nombresUsuario
	 */
	public String getNombresUsuario()
	{
		return nombresUsuario;
	}

	/**
	 * @param nombresUsuario the nombresUsuario to set
	 */
	public void setNombresUsuario(String nombresUsuario)
	{
		this.nombresUsuario = nombresUsuario;
	}

	/**
	 * @return the errorConcurrencia
	 */
	public ActionErrors getErrorConcurrencia()
	{
		ActionErrors errores= new ActionErrors();
		if(existeConcurrencia())
		{	
			if(!this.isEsFlujoInclusiones())
			{	
				errores.add("", new ActionMessage("error.odontologia.presupuesto.concurrencia", this.getNombresUsuario()));
			}	
			else
			{
				errores.add("", new ActionMessage("error.odontologia.presupuesto.concurrenciaInclusiones", this.getNombresUsuario()));
			}
			return errores;
		}	
		return null;
	}

	/**
	 * 
	 * @return
	 */
	public boolean existeConcurrencia()
	{
		return (cuenta>0);
	}

	/**
	 * @return the esFlujoInclusiones
	 */
	public boolean isEsFlujoInclusiones()
	{
		return esFlujoInclusiones;
	}

	/**
	 * @param esFlujoInclusiones the esFlujoInclusiones to set
	 */
	public void setEsFlujoInclusiones(boolean esFlujoInclusiones)
	{
		this.esFlujoInclusiones = esFlujoInclusiones;
	}
}
