/**
 * 
 */
package com.servinte.axioma.dto.facturacion;

/**
 * @author armando
 *
 */
public class DtoInfoCargoCobroPaciente 
{
	
	/**
	 * 
	 */
	private double codigoDetalleCargo;
	
	
	/**
	 * 
	 */
	private DtoInfoMontoCobroDetallado informacionMonto;
	
	/**
	 * 
	 */
	private int codigoServicio;
	
	/**
	 * 
	 */
	private int numeroSolicitud;
	
	/**
	 * 
	 */
	private int codigoArticulo;
	

	/**
	 * 
	 */
	private int cantidadCargada;


	public double getCodigoDetalleCargo() {
		return codigoDetalleCargo;
	}


	public void setCodigoDetalleCargo(double codigoDetalleCargo) {
		this.codigoDetalleCargo = codigoDetalleCargo;
	}


	public DtoInfoMontoCobroDetallado getInformacionMonto() {
		return informacionMonto;
	}


	public void setInformacionMonto(DtoInfoMontoCobroDetallado informacionMonto) {
		this.informacionMonto = informacionMonto;
	}


	public int getCodigoServicio() {
		return codigoServicio;
	}


	public void setCodigoServicio(int codigoServicio) {
		this.codigoServicio = codigoServicio;
	}


	public int getNumeroSolicitud() {
		return numeroSolicitud;
	}


	public void setNumeroSolicitud(int numeroSolicitud) {
		this.numeroSolicitud = numeroSolicitud;
	}


	public int getCodigoArticulo() {
		return codigoArticulo;
	}


	public void setCodigoArticulo(int codigoArticulo) {
		this.codigoArticulo = codigoArticulo;
	}


	public int getCantidadCargada() {
		return cantidadCargada;
	}


	public void setCantidadCargada(int cantidadCargada) {
		this.cantidadCargada = cantidadCargada;
	}

}
