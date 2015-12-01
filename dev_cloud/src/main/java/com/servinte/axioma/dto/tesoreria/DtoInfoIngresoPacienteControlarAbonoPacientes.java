/**
 * 
 */
package com.servinte.axioma.dto.tesoreria;
import java.io.Serializable;

import util.ConstantesBD;
import util.UtilidadTexto;

/**
 * Los ingresos de un paciente.
 * El parametro General Controlar Abono Pacientes X Ingreso define si se debe mostrar detallado cada uno de los 
 * ingresos con su valor o si se debe listar el totalizado de estos para el paciente dado
 * 
 */


public class DtoInfoIngresoPacienteControlarAbonoPacientes implements Serializable{
	

	private static final long serialVersionUID = 1L;
	
	private String consecutivoIngresos;
	private int centroAtencionIngresos;
	private String nombreCentroAtencionIngresos;
	private double saldoActual;
	private double valorDevolucion;
	private int idIngresos;
	private double nuevoSaldo;


	public DtoInfoIngresoPacienteControlarAbonoPacientes(){
		
		this.consecutivoIngresos = "";
		this.centroAtencionIngresos = ConstantesBD.codigoNuncaValido;
		this.nombreCentroAtencionIngresos = "";
		this.saldoActual = ConstantesBD.codigoNuncaValidoDouble;
		this.valorDevolucion = ConstantesBD.codigoNuncaValidoDouble;
		this.idIngresos = ConstantesBD.codigoNuncaValido;
	}

	/**
	 * @return the consecutivoIngresos
	 */
	public String getConsecutivoIngresos() {
		return consecutivoIngresos;
	}


	/**
	 * @param consecutivoIngresos the consecutivoIngresos to set
	 */
	public void setConsecutivoIngresos(String consecutivoIngresos) {
		this.consecutivoIngresos = consecutivoIngresos;
	}


	/**
	 * @return the centroAtencionIngresos
	 */
	public int getCentroAtencionIngresos() {
		return centroAtencionIngresos;
	}


	/**
	 * @param centroAtencionIngresos the centroAtencionIngresos to set
	 */
	public void setCentroAtencionIngresos(int centroAtencionIngresos) {
		this.centroAtencionIngresos = centroAtencionIngresos;
	}


	/**
	 * @return the nombreCentroAtencionIngresos
	 */
	public String getNombreCentroAtencionIngresos() {
		return nombreCentroAtencionIngresos;
	}


	/**
	 * @param nombreCentroAtencionIngresos the nombreCentroAtencionIngresos to set
	 */
	public void setNombreCentroAtencionIngresos(String nombreCentroAtencionIngresos) {
		this.nombreCentroAtencionIngresos = nombreCentroAtencionIngresos;
	}


	/**
	 * @return the saldoActual
	 */
	/*public double getSaldoActual() {
		return saldoActual;
	}


	/**
	 * @param saldoActual the saldoActual to set
	 */
	/*public void setSaldoActual(double saldoActual) {
		this.saldoActual = saldoActual;*/
	//}

	
	/**
	 * @return the valorDevolucion
	 */
	/*public double getValorDevolucion() {
		return valorDevolucion;
	}

	/**
	 * @param valorDevolucion the valorDevolucion to set
	 */
	/*public void setValorDevolucion(double valorDevolucion) {
		this.valorDevolucion = valorDevolucion;
	}*/
	
	/**
	 * @return the idIngresos
	 */
	public int getIdIngresos() {
		return idIngresos;
	}

	/**
	 * @param idIngresos the idIngresos to set
	 */
	public void setIdIngresos(int idIngresos) {
		this.idIngresos = idIngresos;
	}
	
	
	/**
	 * @return the nuevoSaldo
	 */
	public double getNuevoSaldo() {
		//return nuevoSaldo;
		nuevoSaldo = this.saldoActual-this.valorDevolucion;
		return nuevoSaldo;
	}
	
	/**
	 * @return the nuevoSaldo
	 */
	public String getNuevoSaldoFormateado() {
		//return nuevoSaldo;
		nuevoSaldo = this.saldoActual-this.valorDevolucion;
		String nuevoSaldoFormateado = UtilidadTexto.formatearValores(nuevoSaldo);
		return nuevoSaldoFormateado;
	}

	/**
	 * @param nuevoSaldo the nuevoSaldo to set
	 */
	public void setNuevoSaldo(double nuevoSaldo) {
		this.nuevoSaldo = nuevoSaldo;
	}
	
	
	/**
	 * @return the saldoActual
	 */
	public double getSaldoActual() {
		return saldoActual;
	}

	/**
	 * @return the saldoActual
	 */
	public String getSaldoActualFormateado() {
		
		String saldoActualFormateado = UtilidadTexto.formatearValores(this.saldoActual);
		return saldoActualFormateado;
	}
	
	/**
	 * @param saldoActual the saldoActual to set
	 */
	public void setSaldoActual(double saldoActual) {
		this.saldoActual = saldoActual;
	}


	/**
	 * @return the valorDevolucion
	 */
	public double getValorDevolucion() {
		return valorDevolucion;
	}

	/**
	 * @return the valorDevolucion
	 */
	public String getValorDevolucionFormateado() {
	
		String valorDevolucionFormateado = UtilidadTexto.formatearValores(this.valorDevolucion);
		return valorDevolucionFormateado;
	}

	/**
	 * @param valorDevolucion the valorDevolucion to set
	 */
	public void setValorDevolucion(double valorDevolucion) {
		this.valorDevolucion = valorDevolucion;
	}
	
	
	public String getSaldoActualFormateado(double saldoActual){
		
		String saldoActualFormateado="";
		saldoActualFormateado = UtilidadTexto.formatearValores(this.saldoActual);
		return saldoActualFormateado;
		
	}
	
	public String getNuevoSaldoFormateado(double nuevoSaldo){
		
		String nuevoSaldoFormateado="";
		nuevoSaldoFormateado = UtilidadTexto.formatearValores(this.nuevoSaldo);
		return nuevoSaldoFormateado;
		
	}
	
	/**
	 * Obtiene saldo anterior
	 * @return
	 */
	public String getTotalSaldoAnteriorFormateado()
	{
		double total=0;
		String totalFormateado="";

		total+=this.getSaldoActual()+this.getValorDevolucion();
		totalFormateado = UtilidadTexto.formatearValores(total);

		return totalFormateado;
	}

	/**
	 * @return the valorNotaFormateado
	 */
	public String getValorNotaFormateado() {
		return UtilidadTexto.formatearValores(valorDevolucion);
	}
	
	
	
}
