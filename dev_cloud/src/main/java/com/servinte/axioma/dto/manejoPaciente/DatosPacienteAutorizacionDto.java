package com.servinte.axioma.dto.manejoPaciente;

import java.io.Serializable;

import com.servinte.axioma.dto.facturacion.ContratoDto;

/**
 * @author DiaRuiPe
 * @version 1.0
 * @created 03-jul-2012 03:34:35 p.m.
 */
public class DatosPacienteAutorizacionDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5418446833904971961L;

	/**Atributo para almacenar el centro de atención asignado al paciente */
	private Integer centroAtencionAsignadoPaciente;
	
	/**Atributo para almacenar el número de ingreso del paciente */
	private int codigoIngresoPaciente;
	
	/**Atributo para almacenar el tipo de paciente */
	private String tipoPaciente;
	
	/**Atributo para almacenar el tipo de afiliado para el paciente */
	private String tipoAfiliado;
	
	/**Atributo para almacenar el nombre del tipo de afiliado para el paciente */
	private String nombreTipoAfiliado;
	
	/**Atributo para almacenar la clasificación socieconomica del paciente */
	private int clasificacionSocieconomica;
	
	/**Atributo para almacenar el nombre de la clasificación socieconomica del paciente */
	private String nombreClasificacionSocieconomica;
	
	/**Atributo para almacenar el codido del convenio asociado a la cuenta del paciente */
	private int codConvenioCuenta;
	
	/**Atributo para almacenar la naturaleza del paciente */
	private Integer naturalezaPaciente;
	
	/**Atributo para almacenar la naturaleza del paciente */
	private String nombreNaturalezaPaciente;
	
	/**Atributo para almacenar el número de cuenta del paciente */
	private int cuenta;
	
	/**Atributo para almacenar el codigo del paciente */
	private int codigoPaciente;
	
	/**Atributo que almacena el estado de la cuenta del paciente*/
	private boolean cuentaAbierta;
	
	/**Atributo que permite identificar si la cuenta del paciente maneja montos */
	private boolean cuentaManejaMontos;
	
	/**Atributo que almacena el porcentaje definido cuando la cuenta del paciente no maneja
	 * montos */
	private Double porcentajeMontoCuenta;
	
	
	/**
	 * Atributo que representa los nombres y apellidos del paciente
	 */
	private String nombresPaciente;
	
	/**
	 * Atributo que representa el tipo de identificación del paciente
	 */
	private String tipoIdPaciente;
	
	/**
	 * Atributo que representa el número de identificación del paciente
	 */
	private String numIdPaciente;
	
	/**
	 * Atributo que representa la edad del paciente
	 */
	private String edadPaciente;
	
	/**
	 * Atributo que representa el contrato/convenio responsable
	 */
	private ContratoDto contrato = new ContratoDto();
	
	/**
	 * Atributo que representa la via de ingreso
	 */
	private ViaIngresoDto viaIngreso = new ViaIngresoDto();
	
		
	public DatosPacienteAutorizacionDto(){
		
	}
	
	/**
	 * @return centroAtencionAsignadoPaciente
	 */
	public Integer getCentroAtencionAsignadoPaciente() {
		return centroAtencionAsignadoPaciente;
	}
	
	/**
	 * @param centroAtencionAsignadoPaciente
	 */
	public void setCentroAtencionAsignadoPaciente(
			Integer centroAtencionAsignadoPaciente) {
		this.centroAtencionAsignadoPaciente = centroAtencionAsignadoPaciente;
	}
	
	/**
	 * @return codigoIngresoPaciente
	 */
	public int getCodigoIngresoPaciente() {
		return codigoIngresoPaciente;
	}
	
	/**
	 * @param codigoIngresoPaciente
	 */
	public void setCodigoIngresoPaciente(int codigoIngresoPaciente) {
		this.codigoIngresoPaciente = codigoIngresoPaciente;
	}
	
	/**
	 * @return tipoPaciente
	 */
	public String getTipoPaciente() {
		return tipoPaciente;
	}
	
	/**
	 * @param tipoPaciente
	 */
	public void setTipoPaciente(String tipoPaciente) {
		this.tipoPaciente = tipoPaciente;
	}
	
	/**
	 * @return tipoAfiliado
	 */
	public String getTipoAfiliado() {
		return tipoAfiliado;
	}
	
	/**
	 * @param tipoAfiliado
	 */
	public void setTipoAfiliado(String tipoAfiliado) {
		this.tipoAfiliado = tipoAfiliado;
	}
	
	/**
	 * @return clasificacionSocieconomica
	 */
	public int getClasificacionSocieconomica() {
		return clasificacionSocieconomica;
	}
	
	/**
	 * @param clasificacionSocieconomica
	 */
	public void setClasificacionSocieconomica(int clasificacionSocieconomica) {
		this.clasificacionSocieconomica = clasificacionSocieconomica;
	}
	
	/**
	 * @return codConvenioCuenta
	 */
	public int getCodConvenioCuenta() {
		return codConvenioCuenta;
	}
	
	/**
	 * @param codConvenioCuenta
	 */
	public void setCodConvenioCuenta(int codConvenioCuenta) {
		this.codConvenioCuenta = codConvenioCuenta;
	}
	
	/**
	 * @return naturalezaPaciente
	 */
	public Integer getNaturalezaPaciente() {
		return naturalezaPaciente;
	}
	
	/**
	 * @param naturalezaPaciente
	 */
	public void setNaturalezaPaciente(Integer naturalezaPaciente) {
		this.naturalezaPaciente = naturalezaPaciente;
	}
	
	/**
	 * @return cuenta
	 */
	public int getCuenta() {
		return cuenta;
	}
	
	/**
	 * @param cuenta
	 */
	public void setCuenta(int cuenta) {
		this.cuenta = cuenta;
	}
	
	/**
	 * @return codigoPaciente
	 */
	public int getCodigoPaciente() {
		return codigoPaciente;
	}
	
	/**
	 * @param codigoPaciente
	 */
	public void setCodigoPaciente(int codigoPaciente) {
		this.codigoPaciente = codigoPaciente;
	}
	
	/**
	 * @return cuentaAbierta
	 */
	public boolean isCuentaAbierta() {
		return cuentaAbierta;
	}
	
	/**
	 * @param cuentaAbierta
	 */
	public void setCuentaAbierta(boolean cuentaAbierta) {
		this.cuentaAbierta = cuentaAbierta;
	}
	
	/**
	 * @return cuentaManejaMontos
	 */
	public boolean isCuentaManejaMontos() {
		return cuentaManejaMontos;
	}
	
	/**
	 * @param cuentaManejaMontos
	 */
	public void setCuentaManejaMontos(boolean cuentaManejaMontos) {
		this.cuentaManejaMontos = cuentaManejaMontos;
	}
	
	/**
	 * @return porcentajeMontoCuenta
	 */
	public Double getPorcentajeMontoCuenta() {
		return porcentajeMontoCuenta;
	}
	
	/**
	 * @param porcentajeMontoCuenta
	 */
	public void setPorcentajeMontoCuenta(Double porcentajeMontoCuenta) {
		this.porcentajeMontoCuenta = porcentajeMontoCuenta;
	}

	/**
	 * @return the nombresPaciente
	 */
	public String getNombresPaciente() {
		return nombresPaciente;
	}

	/**
	 * @param nombresPaciente the nombresPaciente to set
	 */
	public void setNombresPaciente(String nombresPaciente) {
		this.nombresPaciente = nombresPaciente;
	}

	/**
	 * @return the tipoIdPaciente
	 */
	public String getTipoIdPaciente() {
		return tipoIdPaciente;
	}

	/**
	 * @param tipoIdPaciente the tipoIdPaciente to set
	 */
	public void setTipoIdPaciente(String tipoIdPaciente) {
		this.tipoIdPaciente = tipoIdPaciente;
	}

	/**
	 * @return the numIdPaciente
	 */
	public String getNumIdPaciente() {
		return numIdPaciente;
	}

	/**
	 * @param numIdPaciente the numIdPaciente to set
	 */
	public void setNumIdPaciente(String numIdPaciente) {
		this.numIdPaciente = numIdPaciente;
	}

	/**
	 * @return the edadPaciente
	 */
	public String getEdadPaciente() {
		return edadPaciente;
	}

	/**
	 * @param edadPaciente the edadPaciente to set
	 */
	public void setEdadPaciente(String edadPaciente) {
		this.edadPaciente = edadPaciente;
	}

	/**
	 * @return the contrato
	 */
	public ContratoDto getContrato() {
		return contrato;
	}

	/**
	 * @param contrato the contrato to set
	 */
	public void setContrato(ContratoDto contrato) {
		this.contrato = contrato;
	}

	/**
	 * @return the viaIngreso
	 */
	public ViaIngresoDto getViaIngreso() {
		return viaIngreso;
	}

	/**
	 * @param viaIngreso the viaIngreso to set
	 */
	public void setViaIngreso(ViaIngresoDto viaIngreso) {
		this.viaIngreso = viaIngreso;
	}

	/**
	 * @return the nombreTipoAfiliado
	 */
	public String getNombreTipoAfiliado() {
		return nombreTipoAfiliado;
	}

	/**
	 * @param nombreTipoAfiliado the nombreTipoAfiliado to set
	 */
	public void setNombreTipoAfiliado(String nombreTipoAfiliado) {
		this.nombreTipoAfiliado = nombreTipoAfiliado;
	}

	/**
	 * @return the nombreClasificacionSocieconomica
	 */
	public String getNombreClasificacionSocieconomica() {
		return nombreClasificacionSocieconomica;
	}

	/**
	 * @param nombreClasificacionSocieconomica the nombreClasificacionSocieconomica to set
	 */
	public void setNombreClasificacionSocieconomica(
			String nombreClasificacionSocieconomica) {
		this.nombreClasificacionSocieconomica = nombreClasificacionSocieconomica;
	}

	/**
	 * @return the nombreNaturalezaPaciente
	 */
	public String getNombreNaturalezaPaciente() {
		return nombreNaturalezaPaciente;
	}

	/**
	 * @param nombreNaturalezaPaciente the nombreNaturalezaPaciente to set
	 */
	public void setNombreNaturalezaPaciente(String nombreNaturalezaPaciente) {
		this.nombreNaturalezaPaciente = nombreNaturalezaPaciente;
	}

}