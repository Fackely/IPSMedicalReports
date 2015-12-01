/**
 * 
 */
package com.servinte.axioma.dto.facturacion;

import java.util.ArrayList;

import util.UtilidadTexto;

/**
 * @author armando
 *
 */
public class DtoAgrupacionCalucloValorCobrarPaciente 
{
	/**
	 * 
	 */
	private DtoInfoMontoCobroDetallado infoMonto;
	
	/**
	 * 
	 */
	private int cantidadCargadaMonto;
	
	/**
	 * 
	 */
	private boolean servicio;
	
	/**
	 * 
	 */
	private boolean agrupacion;
	
	/**
	 * 
	 */
	private ArrayList<DtoInfoCargoCobroPaciente> dtoInfoCargo;
	

	public DtoAgrupacionCalucloValorCobrarPaciente() {
		this.infoMonto = new DtoInfoMontoCobroDetallado();
		this.cantidadCargadaMonto = 0;
		this.agrupacion=false;
		this.servicio=false;
		this.dtoInfoCargo=new ArrayList<DtoInfoCargoCobroPaciente>();
	}

	public DtoInfoMontoCobroDetallado getInfoMonto() {
		return infoMonto;
	}

	public void setInfoMonto(DtoInfoMontoCobroDetallado infoMonto) {
		this.infoMonto = infoMonto;
	}

	public int getCantidadCargadaMonto() {
		return cantidadCargadaMonto;
	}

	public void setCantidadCargadaMonto(int cantidadCargadaMonto) {
		this.cantidadCargadaMonto = cantidadCargadaMonto;
	}

	public boolean isAgrupacion() {
		return agrupacion;
	}

	public void setAgrupacion(boolean agrupacion) {
		this.agrupacion = agrupacion;
	}

	public boolean isServicio() {
		return servicio;
	}

	public void setServicio(boolean servicio) {
		this.servicio = servicio;
	}

	/**
	 * 
	 * @return
	 */
	public int montoACobrarXRegistro()
	{//Se adecua de acuerdo a la fórmula Anexo 1001 MT 1159-Camilo Gómez
		double dividendo	=(infoMonto.getCantidadArticuloServicio()>0)?infoMonto.getCantidadArticuloServicio():this.cantidadCargadaMonto;
		double montosCobrar	=this.cantidadCargadaMonto/dividendo;
		int aprox 			=UtilidadTexto.aproximarSiguienteUnidad(montosCobrar+"");
		return aprox*infoMonto.getCantidadMonto();
	}
	
	public double calculoValorMonto()
	{
		return montoACobrarXRegistro()*infoMonto.getValorMonto();
	}

	public ArrayList<DtoInfoCargoCobroPaciente> getDtoInfoCargo() {
		return dtoInfoCargo;
	}

	public void setDtoInfoCargo(ArrayList<DtoInfoCargoCobroPaciente> dtoInfoCargo) {
		this.dtoInfoCargo = dtoInfoCargo;
	}
	
}
