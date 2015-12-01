package com.princetonsa.dto.facturacion;

import java.io.Serializable;

import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.UtilidadFecha;

public class DtoLogControlAnticipoContrato implements Serializable , Cloneable{

	private double codigo;
	private double controlAnticipoContrato;
	private double valorAnticipoContratadoConvenio;
	private double valorAnticipoContratadoConvenioAnterior;
	private double valorAnticipoReservadoConvenio;
	private double valorAnticipoRecibidoConvenio;
	private double valorAnticipoUtilizado;
	private double valorAnticipoDisponible;
	private double numeroTotalPacientes;
	private double numeroMaximoPaciente;
	private double numeroPacientesAtendidos;
	private double numeroPacientesXAtender;
	private String fechaModifica;
	private String horaModifica;
	private String requiereAnticipo;
	private String eliminado;
	
	
	
	private String usuarioModifica;
	
	
	public DtoLogControlAnticipoContrato(){
		
		this.reset();
	}
	
	public void reset(){
		
		this.codigo = ConstantesBD.codigoNuncaValido;
		this.controlAnticipoContrato = ConstantesBD.codigoNuncaValido;
		this.valorAnticipoContratadoConvenio = ConstantesBD.codigoNuncaValido;
		this.valorAnticipoContratadoConvenioAnterior = ConstantesBD.codigoNuncaValido;
		this.valorAnticipoReservadoConvenio = ConstantesBD.codigoNuncaValido;
		this.valorAnticipoRecibidoConvenio = ConstantesBD.codigoNuncaValido;
		this.valorAnticipoUtilizado = ConstantesBD.codigoNuncaValido;
		this.valorAnticipoDisponible = ConstantesBD.codigoNuncaValido;
		this.numeroTotalPacientes = ConstantesBD.codigoNuncaValido;
		this.numeroMaximoPaciente = ConstantesBD.codigoNuncaValido;
		this.numeroPacientesAtendidos = ConstantesBD.codigoNuncaValido;
		this.numeroPacientesXAtender = ConstantesBD.codigoNuncaValido;
		this.fechaModifica = "";
		this.horaModifica = "";		
		this.usuarioModifica = "";
		this.requiereAnticipo = ConstantesBD.acronimoNo;
		this.eliminado = ConstantesBD.acronimoNo;
	}

	/**
	 * @return the codigo
	 */
	public double getCodigo() {
		return codigo;
	}

	/**
	 * @param codigo the codigo to set
	 */
	public void setCodigo(double codigo) {
		this.codigo = codigo;
	}

	

	/**
	 * @return the valorAnticipoContratadoConvenio
	 */
	public double getValorAnticipoContratadoConvenio() {
		return valorAnticipoContratadoConvenio;
	}

	/**
	 * @param valorAnticipoContratadoConvenio the valorAnticipoContratadoConvenio to set
	 */
	public void setValorAnticipoContratadoConvenio(
			double valorAnticipoContratadoConvenio) {
		this.valorAnticipoContratadoConvenio = valorAnticipoContratadoConvenio;
	}

	/**
	 * @return the valorAnticipoReservadoConvenio
	 */
	public double getValorAnticipoReservadoConvenio() {
		return valorAnticipoReservadoConvenio;
	}

	/**
	 * @param valorAnticipoReservadoConvenio the valorAnticipoReservadoConvenio to set
	 */
	public void setValorAnticipoReservadoConvenio(
			double valorAnticipoReservadoConvenio) {
		this.valorAnticipoReservadoConvenio = valorAnticipoReservadoConvenio;
	}

	/**
	 * @return the valorAnticipoRecibidoConvenio
	 */
	public double getValorAnticipoRecibidoConvenio() {
		return valorAnticipoRecibidoConvenio;
	}

	/**
	 * @param valorAnticipoRecibidoConvenio the valorAnticipoRecibidoConvenio to set
	 */
	public void setValorAnticipoRecibidoConvenio(
			double valorAnticipoRecibidoConvenio) {
		this.valorAnticipoRecibidoConvenio = valorAnticipoRecibidoConvenio;
	}

	/**
	 * @return the valorAnticipoUtilizado
	 */
	public double getValorAnticipoUtilizado() {
		return valorAnticipoUtilizado;
	}

	/**
	 * @param valorAnticipoUtilizado the valorAnticipoUtilizado to set
	 */
	public void setValorAnticipoUtilizado(double valorAnticipoUtilizado) {
		this.valorAnticipoUtilizado = valorAnticipoUtilizado;
	}

	/**
	 * @return the valorAnticipoDisponible
	 */
	public double getValorAnticipoDisponible() {
		return valorAnticipoDisponible;
	}

	/**
	 * @param valorAnticipoDisponible the valorAnticipoDisponible to set
	 */
	public void setValorAnticipoDisponible(double valorAnticipoDisponible) {
		this.valorAnticipoDisponible = valorAnticipoDisponible;
	}

	/**
	 * @return the numeroTotalPacientes
	 */
	public double getNumeroTotalPacientes() {
		return numeroTotalPacientes;
	}

	/**
	 * @param numeroTotalPacientes the numeroTotalPacientes to set
	 */
	public void setNumeroTotalPacientes(double numeroTotalPacientes) {
		this.numeroTotalPacientes = numeroTotalPacientes;
	}

	/**
	 * @return the numeroMaximoPaciente
	 */
	public double getNumeroMaximoPaciente() {
		return numeroMaximoPaciente;
	}

	/**
	 * @param numeroMaximoPaciente the numeroMaximoPaciente to set
	 */
	public void setNumeroMaximoPaciente(double numeroMaximoPaciente) {
		this.numeroMaximoPaciente = numeroMaximoPaciente;
	}

	/**
	 * @return the numeroPacientesAtendidos
	 */
	public double getNumeroPacientesAtendidos() {
		return numeroPacientesAtendidos;
	}

	/**
	 * @param numeroPacientesAtendidos the numeroPacientesAtendidos to set
	 */
	public void setNumeroPacientesAtendidos(double numeroPacientesAtendidos) {
		this.numeroPacientesAtendidos = numeroPacientesAtendidos;
	}

	/**
	 * @return the numeroPacientesXAtender
	 */
	public double getNumeroPacientesXAtender() {
		return numeroPacientesXAtender;
	}

	/**
	 * @param numeroPacientesXAtender the numeroPacientesXAtender to set
	 */
	public void setNumeroPacientesXAtender(double numeroPacientesXAtender) {
		this.numeroPacientesXAtender = numeroPacientesXAtender;
	}

	/**
	 * @return the fechaModifica
	 */
	public String getFechaModifica() {
		return fechaModifica;
	}

	/**
	 * @param fechaModifica the fechaModifica to set
	 */
	public void setFechaModifica(String fechaModifica) {
		this.fechaModifica = fechaModifica;
	}

	/**
	 * @return the horaModifica
	 */
	public String getHoraModifica() {
		return horaModifica;
	}

	/**
	 * @param horaModifica the horaModifica to set
	 */
	public void setHoraModifica(String horaModifica) {
		this.horaModifica = horaModifica;
	}

	/**
	 * @return the usuarioModifica
	 */
	public String getUsuarioModifica() {
		return usuarioModifica;
	}

	/**
	 * @param usuarioModifica the usuarioModifica to set
	 */
	public void setUsuarioModifica(String usuarioModifica) {
		this.usuarioModifica = usuarioModifica;
	}
	
	
	
	
	/**
	 * @return the fechaModifica
	 */
	public String getFechaModificaFromatoBD() {
		return UtilidadFecha.validarFecha(this.fechaModifica) ? UtilidadFecha
				.conversionFormatoFechaABD(this.fechaModifica) : "";
	}

	/***
	 * 
	 *  
	 */
	public Object clone(){
	        Object obj=null;
	        try{
	            obj=super.clone();
	        }catch(CloneNotSupportedException ex){
	        	Log4JManager.error(" no se puede duplicar");
	        }
	        return obj;
	    }

	/**
	 * @return the requiereAnticipo
	 */
	public String getRequiereAnticipo() {
		return requiereAnticipo;
	}

	/**
	 * @param requiereAnticipo the requiereAnticipo to set
	 */
	public void setRequiereAnticipo(String requiereAnticipo) {
		this.requiereAnticipo = requiereAnticipo;
	}

	/**
	 * @return the controlAnticipoContrato
	 */
	public double getControlAnticipoContrato() {
		return controlAnticipoContrato;
	}

	/**
	 * @param controlAnticipoContrato the controlAnticipoContrato to set
	 */
	public void setControlAnticipoContrato(double controlAnticipoContrato) {
		this.controlAnticipoContrato = controlAnticipoContrato;
	}

	/**
	 * @return the valorAnticipoContratadoConvenioAnterior
	 */
	public double getValorAnticipoContratadoConvenioAnterior() {
		return valorAnticipoContratadoConvenioAnterior;
	}

	/**
	 * @param valorAnticipoContratadoConvenioAnterior the valorAnticipoContratadoConvenioAnterior to set
	 */
	public void setValorAnticipoContratadoConvenioAnterior(
			double valorAnticipoContratadoConvenioAnterior) {
		this.valorAnticipoContratadoConvenioAnterior = valorAnticipoContratadoConvenioAnterior;
	}

	/**
	 * @return the eliminado
	 */
	public String getEliminado() {
		return eliminado;
	}

	/**
	 * @param eliminado the eliminado to set
	 */
	public void setEliminado(String eliminado) {
		this.eliminado = eliminado;
	}

	
	
}
