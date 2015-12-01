/**
 * 
 */
package com.princetonsa.dto.cartera;

import java.util.ArrayList;

import util.ConstantesBD;
  
/**
 * @author armando
 *
 */
public class DtoResultadoReporteCuentaCobro 
{

	/**
	 * 
	 */
	private int codigoConvenio;

	/**
	 * 
	 */
	private String nombreConvenio;
	
	
	/**
	 * 
	 */
	private ArrayList<DtoDetalleReporteCuentaCobro> dtoDetalle;
	

	public DtoResultadoReporteCuentaCobro()
	{
		this.codigoConvenio=ConstantesBD.codigoNuncaValido;
		this.nombreConvenio="";
		this.dtoDetalle=new ArrayList<DtoDetalleReporteCuentaCobro>();
	}


	public int getCodigoConvenio() {
		return codigoConvenio;
	}


	public void setCodigoConvenio(int codigoConvenio) {
		this.codigoConvenio = codigoConvenio;
	}


	public String getNombreConvenio() {
		return nombreConvenio;
	}


	public void setNombreConvenio(String nombreConvenio) {
		this.nombreConvenio = nombreConvenio;
	}


	public ArrayList<DtoDetalleReporteCuentaCobro> getDtoDetalle() {
		return dtoDetalle;
	}


	public void setDtoDetalle(ArrayList<DtoDetalleReporteCuentaCobro> dtoDetalle) {
		this.dtoDetalle = dtoDetalle;
	}


}
