/**
 * 
 */
package com.princetonsa.dto.tesoreria;

import java.io.Serializable;
import java.util.ArrayList;

import util.ConstantesBD;

import com.princetonsa.dto.administracion.DtoPersonas;

/**
 * Contiene todos los DTO's necesarios para realizar un traslado de abonos
 * 
 * @author Cristhian Murillo
 */
public class DtoGuardarTrasladoAbonoPaciente implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private DtoPersonas origen;
	private double totalMovimiento;
	boolean parametroControlAbonoPorIngreso;
	ArrayList<DtoInfoIngresoTrasladoAbonoPaciente> listaIngresosPacOrigen;
	ArrayList<DtoInfoIngresoTrasladoAbonoPaciente> listaIngresosPacDestino;
	
	private String loginUsuarioActual;
	private int centroAtencionActual;
	private int institucion;
	
	
	public DtoGuardarTrasladoAbonoPaciente ()
	{
		this.origen								= new DtoPersonas();
		this.totalMovimiento					= ConstantesBD.codigoNuncaValidoDouble;
		this.parametroControlAbonoPorIngreso	= false;;
		this.listaIngresosPacOrigen				= new ArrayList<DtoInfoIngresoTrasladoAbonoPaciente>();
		this.listaIngresosPacDestino			= new ArrayList<DtoInfoIngresoTrasladoAbonoPaciente>();
		this.loginUsuarioActual					= "";
		this.centroAtencionActual				= ConstantesBD.codigoNuncaValido;
		this.institucion						= ConstantesBD.codigoNuncaValido;
	}


	public DtoPersonas getOrigen() {
		return origen;
	}


	public double getTotalMovimiento() {
		return totalMovimiento;
	}


	public boolean isParametroControlAbonoPorIngreso() {
		return parametroControlAbonoPorIngreso;
	}


	public ArrayList<DtoInfoIngresoTrasladoAbonoPaciente> getListaIngresosPacOrigen() {
		return listaIngresosPacOrigen;
	}


	public ArrayList<DtoInfoIngresoTrasladoAbonoPaciente> getListaIngresosPacDestino() {
		return listaIngresosPacDestino;
	}


	public void setOrigen(DtoPersonas origen) {
		this.origen = origen;
	}


	public void setTotalMovimiento(double totalMovimiento) {
		this.totalMovimiento = totalMovimiento;
	}


	public void setParametroControlAbonoPorIngreso(
			boolean parametroControlAbonoPorIngreso) {
		this.parametroControlAbonoPorIngreso = parametroControlAbonoPorIngreso;
	}


	public void setListaIngresosPacOrigen(
			ArrayList<DtoInfoIngresoTrasladoAbonoPaciente> listaIngresosPacOrigen) {
		this.listaIngresosPacOrigen = listaIngresosPacOrigen;
	}


	public void setListaIngresosPacDestino(
			ArrayList<DtoInfoIngresoTrasladoAbonoPaciente> listaIngresosPacDestino) {
		this.listaIngresosPacDestino = listaIngresosPacDestino;
	}


	public String getLoginUsuarioActual() {
		return loginUsuarioActual;
	}


	public int getCentroAtencionActual() {
		return centroAtencionActual;
	}


	public void setLoginUsuarioActual(String loginUsuarioActual) {
		this.loginUsuarioActual = loginUsuarioActual;
	}


	public void setCentroAtencionActual(int centroAtencionActual) {
		this.centroAtencionActual = centroAtencionActual;
	}


	public int getInstitucion() {
		return institucion;
	}


	public void setInstitucion(int institucion) {
		this.institucion = institucion;
	}

	
}
