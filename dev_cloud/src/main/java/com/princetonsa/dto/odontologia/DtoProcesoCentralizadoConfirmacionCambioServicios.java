/**
 * 
 */
package com.princetonsa.dto.odontologia;

import java.io.Serializable;
import java.util.ArrayList;

import com.princetonsa.dto.manejoPaciente.DtoSubCuentas;

/**
 * @author armando
 *
 */
public class DtoProcesoCentralizadoConfirmacionCambioServicios implements Serializable
{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	private ArrayList<String> errores=new ArrayList<String>();
	
	/**
	 * 
	 */
	private double valorTotalServicios;

	/**
	 * Valor total de los servicios que se ten&iacute;an
	 * antes de la solicitud de cambio
	 */
	private double valorTotalServiciosAnteriores;

	/**
	 * 
	 */
	private String forward;
	
	/**
	 * 
	 */
	private double valorAbonoDisponiblePaciente;
	
	/**
	 * 
	 */
	private ArrayList<DtoSubCuentas> responsables;
	
	/**
	 * 
	 */
	private double valorAbonoDevolucion;
	
	/**
	 * 
	 */
	private boolean procesoExito;
	
	/**
	 * 
	 */
	private String estadoRecargar;
	
	/**
	 * 
	 */
	private DtoSolictudCambioServicioCita solicitud;
	
	/**
	 * 
	 */
	public DtoProcesoCentralizadoConfirmacionCambioServicios()
	{
		this.errores=new ArrayList<String>();
		this.valorTotalServicios=0;
		this.forward="";
		this.valorAbonoDisponiblePaciente=0;
		this.responsables=new ArrayList<DtoSubCuentas>();
		this.valorAbonoDevolucion=0;
		this.procesoExito=false;
		this.estadoRecargar="";
		this.solicitud=new DtoSolictudCambioServicioCita();
	}

	public ArrayList<String> getErrores() {
		return errores;
	}

	public void setErrores(ArrayList<String> errores) {
		this.errores = errores;
	}

	public double getValorTotalServicios() {
		return valorTotalServicios;
	}

	public void setValorTotalServicios(double valorTotalServicios) {
		this.valorTotalServicios = valorTotalServicios;
	}

	public String getForward() {
		return forward;
	}

	public void setForward(String forward) {
		this.forward = forward;
	}

	public double getValorAbonoDisponiblePaciente() {
		return valorAbonoDisponiblePaciente;
	}

	public void setValorAbonoDisponiblePaciente(double valorAbonoDisponiblePaciente) {
		this.valorAbonoDisponiblePaciente = valorAbonoDisponiblePaciente;
	}

	public ArrayList<DtoSubCuentas> getResponsables() {
		return responsables;
	}

	public void setResponsables(ArrayList<DtoSubCuentas> responsables) {
		this.responsables = responsables;
	}

	public double getValorAbonoDevolucion() {
		return valorAbonoDevolucion;
	}

	public void setValorAbonoDevolucion(double valorAbonoDevolucion) {
		this.valorAbonoDevolucion = valorAbonoDevolucion;
	}

	public boolean isProcesoExito() {
		return procesoExito;
	}

	public void setProcesoExito(boolean procesoExito) {
		this.procesoExito = procesoExito;
	}

	public String getEstadoRecargar() {
		return estadoRecargar;
	}

	public void setEstadoRecargar(String estadoRecargar) {
		this.estadoRecargar = estadoRecargar;
	}

	public DtoSolictudCambioServicioCita getSolicitud() {
		return solicitud;
	}

	public void setSolicitud(DtoSolictudCambioServicioCita solicitud) {
		this.solicitud = solicitud;
	}

	/**
	 * @return Retorna atributo valorTotalServiciosAnteriores
	 */
	public double getValorTotalServiciosAnteriores()
	{
		return valorTotalServiciosAnteriores;
	}

	/**
	 * @param valorTotalServiciosAnteriores Asigna atributo valorTotalServiciosAnteriores
	 */
	public void setValorTotalServiciosAnteriores(
			double valorTotalServiciosAnteriores)
	{
		this.valorTotalServiciosAnteriores = valorTotalServiciosAnteriores;
	}
	
	

}
