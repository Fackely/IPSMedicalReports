/**
 * 
 */
package com.princetonsa.actionform.historiaClinica;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import com.princetonsa.dto.historiaClinica.historicoAtenciones.DtoHistoricosHC;

/**
 * 
 * @author axioma
 *
 */
public class HistoricoAtencionesForm extends ValidatorForm 
{

	/**
	 * 
	 */
	private int codigoPaciente;

	/**
	 * 
	 */
	private ArrayList<DtoHistoricosHC> historicoAtenciones;

	/**
	 * 
	 */
	private String estado;
	
	/**
	 * @return the codigoPaciente
	 */
	public int getCodigoPaciente() 
	{
		return codigoPaciente;
	}

	/**
	 * @param codigoPaciente the codigoPaciente to set
	 */
	public void setCodigoPaciente(int codigoPaciente) 
	{
		this.codigoPaciente = codigoPaciente;
	}

	/**
	 * @return the historicoAtenciones
	 */
	public ArrayList<DtoHistoricosHC> getHistoricoAtenciones() 
	{
		return historicoAtenciones;
	}

	/**
	 * @param historicoAtenciones the historicoAtenciones to set
	 */
	public void setHistoricoAtenciones(ArrayList<DtoHistoricosHC> historicoAtenciones) 
	{
		this.historicoAtenciones = historicoAtenciones;
	}

	/**
	 * @return the estado
	 */
	public String getEstado() {
		return estado;
	}

	/**
	 * @param estado the estado to set
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	}
}
