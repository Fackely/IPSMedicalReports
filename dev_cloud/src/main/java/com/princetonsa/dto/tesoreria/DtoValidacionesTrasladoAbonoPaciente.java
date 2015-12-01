/**
 * 
 */
package com.princetonsa.dto.tesoreria;

import java.io.Serializable;

import org.axioma.util.log.Log4JManager;

/**
 * Encaps&uacute;la la respuesta a la validaci&oacute;n de un paciente en el momento
 * de hacer un traslado de abonos
 * 
 * @author Cristhian Murillo
 */
public class DtoValidacionesTrasladoAbonoPaciente implements Serializable{
	

	private static final long serialVersionUID = 1L;

	/* PARAMETROS  */
	private boolean parametroControlaAbonoPorIngreso;
	private boolean parametroManejoEspecialInstiOdonto;
	private boolean parametroTrasAbonoPacSaldoPresuContratado;
	
	/* PACIENTE ORIGEN Y DESTINO  */
	private boolean pacienteTieneSaldo;
	private boolean pacienteTienePresupuestoEstadoCorrecto;
	private boolean pacienteTieneRegistrosDeIngreso;
	
	/* PACIENTE DESTINO  */
	private boolean pacienteTieneTodosIngresosCerrados;
	
	
	public DtoValidacionesTrasladoAbonoPaciente()
	{
		this.parametroControlaAbonoPorIngreso			= false;
		this.parametroManejoEspecialInstiOdonto			= false;
		this.parametroTrasAbonoPacSaldoPresuContratado	= false;
		
		this.pacienteTieneSaldo							= false;
		this.pacienteTienePresupuestoEstadoCorrecto		= false;
		this.pacienteTieneRegistrosDeIngreso			= false;
		
		this.pacienteTieneTodosIngresosCerrados			= false;
	}

	

	public boolean isParametroControlaAbonoPorIngreso() {
		return parametroControlaAbonoPorIngreso;
	}


	public boolean isParametroManejoEspecialInstiOdonto() {
		return parametroManejoEspecialInstiOdonto;
	}


	public boolean isParametroTrasAbonoPacSaldoPresuContratado() {
		return parametroTrasAbonoPacSaldoPresuContratado;
	}


	public boolean isPacienteTieneSaldo() {
		return pacienteTieneSaldo;
	}


	public void setParametroControlaAbonoPorIngreso(
			boolean parametroControlaAbonoPorIngreso) {
		this.parametroControlaAbonoPorIngreso = parametroControlaAbonoPorIngreso;
	}


	public void setParametroManejoEspecialInstiOdonto(
			boolean parametroManejoEspecialInstiOdonto) {
		this.parametroManejoEspecialInstiOdonto = parametroManejoEspecialInstiOdonto;
	}


	public void setParametroTrasAbonoPacSaldoPresuContratado(
			boolean parametroTrasAbonoPacSaldoPresuContratado) {
		this.parametroTrasAbonoPacSaldoPresuContratado = parametroTrasAbonoPacSaldoPresuContratado;
	}


	public void setPacienteTieneSaldo(boolean pacienteTieneSaldo) {
		this.pacienteTieneSaldo = pacienteTieneSaldo;
	}


	public boolean isPacienteTienePresupuestoEstadoCorrecto() {
		return pacienteTienePresupuestoEstadoCorrecto;
	}


	public void setPacienteTienePresupuestoEstadoCorrecto(
			boolean pacienteTienePresupuestoEstadoCorrecto) {
		this.pacienteTienePresupuestoEstadoCorrecto = pacienteTienePresupuestoEstadoCorrecto;
	}
	
	

	public boolean isPacienteTieneRegistrosDeIngreso() {
		return pacienteTieneRegistrosDeIngreso;
	}


	public void setPacienteTieneRegistrosDeIngreso(
			boolean pacienteTieneRegistrosDeIngreso) {
		this.pacienteTieneRegistrosDeIngreso = pacienteTieneRegistrosDeIngreso;
	}




	public boolean isPacienteTieneTodosIngresosCerrados() {
		return pacienteTieneTodosIngresosCerrados;
	}



	public void setPacienteTieneTodosIngresosCerrados(
			boolean pacienteTieneTodosIngresosCerrados) {
		this.pacienteTieneTodosIngresosCerrados = pacienteTieneTodosIngresosCerrados;
	}



	public void imprimirDTO() {
		
		Log4JManager.info("" +
				"\n >>>>>>>>>>>>>>>>>>>>>>>>>>>> " 					+
				"\n DtoValidacionesTrasladoAbonoPaciente:" 			+
				"\n parametroControlaAbonoPorIngreso:" 				+parametroControlaAbonoPorIngreso+
				"\n parametroManejoEspecialInstiOdonto: " 			+parametroManejoEspecialInstiOdonto+
				"\n parametroTrasAbonoPacSaldoPresuContratado: " 	+parametroTrasAbonoPacSaldoPresuContratado+
				"\n pacienteTieneSaldo: " 							+pacienteTieneSaldo+
				"\n pacienteTienePresupuestoEstadoCorrecto: " 		+pacienteTienePresupuestoEstadoCorrecto+
				"\n pacienteTieneRegistrosDeIngreso: " 				+pacienteTieneRegistrosDeIngreso+
				"\n pacienteTienePresupuestoEstadoCorrecto: " 		+pacienteTieneTodosIngresosCerrados+
				"\n >>>>>>>>>>>>>>>>>>>>>>>>>>>>");
	}
	

}
