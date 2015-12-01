package com.servinte.axioma.dto.facturacion;

import java.io.Serializable;
import java.util.Date;

import util.ConstantesBD;

/**
 * Dto para mapear datos de los contratos
 * 
 * @author ricruico
 * @version 1.0
 * @created 20-jun-2012 02:24:00 p.m.
 */
public class ContratoDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2651325264051521273L;

	/**
	 * Atributo que representa el codigoPk de contrato
	 */
	private int codigo;
	
	/**
	 * Atributo que representa el número de contrato
	 */
	private String numero;
	
	
	/**
	 * Atributo que representa la fecha Inicio del Contrato
	 */
	private Date fechaInicio;
	
	/**
	 * Atributo que representa la fecha Fin del Contrato
	 */
	private Date fechaFin;
	
	/**
	 * Define si el contrato maneja tarifas por centro de atencion
	 */
	private Character manejaTarifaCentroAtencion;
	
	/**
	 * Atributo que representa el convenio al cual pertenece el contrato 
	 */
	private ConvenioDto convenio= new ConvenioDto();
	
	public ContratoDto(){

	}

	/**
	 * Cnstructor para mapear los resultados de la consulta de contrados
	 * de ConvenioContratoDelegate
	 * @param codigo
	 * @param numero
	 */
	public ContratoDto(int codigo, String numero, Date fechaInicio, Date fechaFin) {
		this.codigo = codigo;
		this.numero = numero;
		this.fechaInicio = fechaInicio;
		this.fechaFin = fechaFin;
	}
	
	/**
	 * Cnstructor para mapear los resultados de la consulta de Convenio/Contrato
	 * por Orden
	 * 
	 * @param codigo
	 * @param numero
	 * @param codigoConvenio
	 * @param nombreConvenio
	 */
	public ContratoDto(int codigo, String numero, int codigoConvenio, String nombreConvenio,
							int codigoTipoContrato, String nombreTipoContrato, Character capitacionSubcontratada,
							Character manejaPresupuestoCapitacion) {
		this.codigo = codigo;
		this.numero = numero;
		ConvenioDto convenio = new ConvenioDto();
		convenio.setCodigo(codigoConvenio);
		convenio.setNombre(nombreConvenio);
		convenio.setCodigoTipoContrato(codigoTipoContrato);
		convenio.setNombreTipoContrato(nombreTipoContrato);
		if(capitacionSubcontratada != null && capitacionSubcontratada.charValue()==ConstantesBD.acronimoSiChar){
			convenio.setConvenioManejaCapiSub(true);
		}
		else{
			convenio.setConvenioManejaCapiSub(false);
		}
		if(manejaPresupuestoCapitacion != null && manejaPresupuestoCapitacion.charValue()==ConstantesBD.acronimoSiChar){
			convenio.setConvenioManejaPresupuesto(true);
		}
		else{
			convenio.setConvenioManejaPresupuesto(false);
		}
		this.convenio = convenio;
	}
	
	/**
	 * Cnstructor para mapear los resultados de la consulta de Convenio/Contrato
	 * por Orden
	 * 
	 * @param codigo
	 * @param numero
	 * @param codigoConvenio
	 * @param nombreConvenio
	 */
	public ContratoDto(int codigo, String numero, int codigoConvenio, String nombreConvenio,
							int codigoTipoContrato, String nombreTipoContrato, Character capitacionSubcontratada,
							Character manejaPresupuestoCapitacion,Character manejaTarifaCentroAtencion) {
		this.codigo = codigo;
		this.numero = numero;
		ConvenioDto convenio = new ConvenioDto();
		convenio.setCodigo(codigoConvenio);
		convenio.setNombre(nombreConvenio);
		convenio.setCodigoTipoContrato(codigoTipoContrato);
		convenio.setNombreTipoContrato(nombreTipoContrato);
		if(capitacionSubcontratada != null && capitacionSubcontratada.charValue()==ConstantesBD.acronimoSiChar){
			convenio.setConvenioManejaCapiSub(true);
		}
		else{
			convenio.setConvenioManejaCapiSub(false);
		}
		if(manejaPresupuestoCapitacion != null && manejaPresupuestoCapitacion.charValue()==ConstantesBD.acronimoSiChar){
			convenio.setConvenioManejaPresupuesto(true);
		}
		else{
			convenio.setConvenioManejaPresupuesto(false);
		}
		this.convenio = convenio;
		
		this.manejaTarifaCentroAtencion=manejaTarifaCentroAtencion;
	}

	/**
	 * @return the codigo
	 */
	public int getCodigo() {
		return codigo;
	}

	/**
	 * @param codigo the codigo to set
	 */
	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	/**
	 * @return the numero
	 */
	public String getNumero() {
		return numero;
	}

	/**
	 * @param numero the numero to set
	 */
	public void setNumero(String numero) {
		this.numero = numero;
	}

	/**
	 * @return the fechaInicio
	 */
	public Date getFechaInicio() {
		return fechaInicio;
	}

	/**
	 * @param fechaInicio the fechaInicio to set
	 */
	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	/**
	 * @return the fechaFin
	 */
	public Date getFechaFin() {
		return fechaFin;
	}

	/**
	 * @param fechaFin the fechaFin to set
	 */
	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}

	/**
	 * @return the convenio
	 */
	public ConvenioDto getConvenio() {
		return convenio;
	}

	/**
	 * @param convenio the convenio to set
	 */
	public void setConvenio(ConvenioDto convenio) {
		this.convenio = convenio;
	}

	/**
	 * @return the manejaTarifaCentroAtencion
	 */
	public Character getManejaTarifaCentroAtencion() {
		return manejaTarifaCentroAtencion;
	}

	/**
	 * @param manejaTarifaCentroAtencion the manejaTarifaCentroAtencion to set
	 */
	public void setManejaTarifaCentroAtencion(Character manejaTarifaCentroAtencion) {
		this.manejaTarifaCentroAtencion = manejaTarifaCentroAtencion;
	}

	
}