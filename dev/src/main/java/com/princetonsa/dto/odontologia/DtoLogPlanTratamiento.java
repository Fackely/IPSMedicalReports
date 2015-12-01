package com.princetonsa.dto.odontologia;

import java.io.Serializable;

import util.ConstantesBD;
import util.InfoDatosInt;

public class DtoLogPlanTratamiento implements Serializable{
	
	private Double codigoPk;
	private Double planTratamiento;
	private String estado;
	private InfoDatosInt motivo;
	private InfoDatosInt especialidad;
	private InfoDatosInt codigoMedico;
	private String porConfirmar;
	private int cita;
	private DtoInfoFechaUsuario modificacion;
	private Double valoracion;
	private Double evolucion;
	private String imagen;
	private String historicoPlanT;
	
	public DtoLogPlanTratamiento()
	{
		reset();
	}
	
	public void reset()
	{
		codigoPk = ConstantesBD.codigoNuncaValidoDouble;
		planTratamiento = ConstantesBD.codigoNuncaValidoDouble;
		estado = "";
		motivo = new InfoDatosInt();
		especialidad = new InfoDatosInt();
		codigoMedico = new InfoDatosInt();
		porConfirmar = "";
		cita = ConstantesBD.codigoNuncaValido;
		modificacion = new DtoInfoFechaUsuario();
		valoracion = ConstantesBD.codigoNuncaValidoDouble;
		evolucion = ConstantesBD.codigoNuncaValidoDouble;
		imagen = "";
		historicoPlanT=ConstantesBD.acronimoNo;
	}

	public Double getCodigoPk() {
		return codigoPk;
	}

	public void setCodigoPk(Double codigoPk) {
		this.codigoPk = codigoPk;
	}

	public Double getPlanTratamiento() {
		return planTratamiento;
	}

	public void setPlanTratamiento(Double planTratamiento) {
		this.planTratamiento = planTratamiento;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	
	
	public String getPorConfirmar() {
		return porConfirmar;
	}

	public void setPorConfirmar(String porConfirmar) {
		this.porConfirmar = porConfirmar;
	}

	public int getCita() {
		return cita;
	}

	public void setCita(int cita) {
		this.cita = cita;
	}

	public DtoInfoFechaUsuario getModificacion() {
		return modificacion;
	}

	public void setModificacion(DtoInfoFechaUsuario modificacion) {
		this.modificacion = modificacion;
	}

	public Double getValoracion() {
		return valoracion;
	}

	public void setValoracion(Double valoracion) {
		this.valoracion = valoracion;
	}

	public Double getEvolucion() {
		return evolucion;
	}

	public void setEvolucion(Double evolucion) {
		this.evolucion = evolucion;
	}

	public String getImagen() {
		return imagen;
	}

	public void setImagen(String imagen) {
		this.imagen = imagen;
	}

	public void setEspecialidad(InfoDatosInt especialidad) {
		this.especialidad = especialidad;
	}

	public InfoDatosInt getEspecialidad() {
		return especialidad;
	}

	/**
	 * @return the motivo
	 */
	public InfoDatosInt getMotivo() {
		return motivo;
	}

	/**
	 * @param motivo the motivo to set
	 */
	public void setMotivo(InfoDatosInt motivo) {
		this.motivo = motivo;
	}

	/**
	 * @return the codigoMedico
	 */
	public InfoDatosInt getCodigoMedico() {
		return codigoMedico;
	}

	/**
	 * @param codigoMedico the codigoMedico to set
	 */
	public void setCodigoMedico(InfoDatosInt codigoMedico) {
		this.codigoMedico = codigoMedico;
	}

	/**
	 * @return the historicoPlanT
	 */
	public String getHistoricoPlanT() {
		return historicoPlanT;
	}

	/**
	 * @param historicoPlanT the historicoPlanT to set
	 */
	public void setHistoricoPlanT(String historicoPlanT) {
		this.historicoPlanT = historicoPlanT;
	}

	
}