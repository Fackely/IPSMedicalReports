/**
 * 
 */
package com.servinte.axioma.dto.facturacion;

import java.math.BigDecimal;
import java.util.ArrayList;


/**
 * @author armando
 *
 */
public class DtoInfoCobroPaciente 
{

	
	public DtoInfoCobroPaciente() 
	{
		this.valorCargoPaciente=new BigDecimal(0);
		this.generado=false;
		this.observaciones="";
		this.manejaMonto=false;
		this.detallado=false;
		this.infoCargoCobroPaciente=new ArrayList<DtoInfoCargoCobroPaciente>();
		this.agrupacionCalculo=new ArrayList<DtoAgrupacionCalucloValorCobrarPaciente>();
	}
	
	private BigDecimal valorCargoPaciente;
	
	private boolean generado;
	
	private String observaciones;
	
	/**
	 * 
	 */
	private boolean manejaMonto;
	
	/**
	 * 
	 */
	private boolean detallado;
	
	
	/**
	 * 
	 */
	private ArrayList<DtoAgrupacionCalucloValorCobrarPaciente> agrupacionCalculo=new ArrayList<DtoAgrupacionCalucloValorCobrarPaciente>();
	
	/**
	 * 
	 */
	private ArrayList<DtoInfoCargoCobroPaciente> infoCargoCobroPaciente;
	

	public BigDecimal getValorCargoPaciente() {
		return valorCargoPaciente;
	}

	public void setValorCargoPaciente(BigDecimal valorCargoPaciente) {
		this.valorCargoPaciente = valorCargoPaciente;
	}

	public boolean isGenerado() {
		return generado;
	}

	public void setGenerado(boolean generado) {
		this.generado = generado;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public boolean isManejaMonto() {
		return manejaMonto;
	}

	public void setManejaMonto(boolean manejaMonto) {
		this.manejaMonto = manejaMonto;
	}

	public boolean isDetallado() {
		return detallado;
	}

	public void setDetallado(boolean detallado) {
		this.detallado = detallado;
	}

	public ArrayList<DtoInfoCargoCobroPaciente> getInfoCargoCobroPaciente() {
		return infoCargoCobroPaciente;
	}

	public void setInfoCargoCobroPaciente(
			ArrayList<DtoInfoCargoCobroPaciente> infoCargoCobroPaciente) {
		this.infoCargoCobroPaciente = infoCargoCobroPaciente;
	}

	public ArrayList<DtoAgrupacionCalucloValorCobrarPaciente> getAgrupacionCalculo() {
		return agrupacionCalculo;
	}

	public void setAgrupacionCalculo(
			ArrayList<DtoAgrupacionCalucloValorCobrarPaciente> agrupacionCalculo) {
		this.agrupacionCalculo = agrupacionCalculo;
	}
	
	
	
	
}
