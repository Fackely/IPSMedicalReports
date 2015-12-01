package com.princetonsa.dto.odontologia;

import java.io.Serializable;

import util.ConstantesBD;
import util.InfoDatosInt;

public class DtoLogDetPlanTratamiento implements Serializable{
	private Double codigoPk;
	private Double detPlanTratamiento;
	private int piezaDental;
	private int superficie;
	private int hallazgo;
	private String clasificacion;
	private String porConfirmar;
	private int convencion;
	private DtoInfoFechaUsuario usuarioModifica;
	private int cita;
	private Double valoracion;
	private Double evolucion;
	private InfoDatosInt especialidad;
	private String activo;
	
	public DtoLogDetPlanTratamiento()
	{
		reset();
	}
	
	public void reset()
	{
		codigoPk = ConstantesBD.codigoNuncaValidoDouble;
		detPlanTratamiento = ConstantesBD.codigoNuncaValidoDouble;
		piezaDental = ConstantesBD.codigoNuncaValido;
		superficie = ConstantesBD.codigoNuncaValido;
		hallazgo = ConstantesBD.codigoNuncaValido;
		clasificacion = "";
		porConfirmar = "";
		convencion = ConstantesBD.codigoNuncaValido;
		usuarioModifica = new DtoInfoFechaUsuario();
		cita = ConstantesBD.codigoNuncaValido;
		valoracion = ConstantesBD.codigoNuncaValidoDouble;
		evolucion = ConstantesBD.codigoNuncaValidoDouble;
		especialidad = new InfoDatosInt();
		this.activo = ConstantesBD.acronimoSi;
	}

	public Double getCodigoPk() {
		return codigoPk;
	}

	public void setCodigoPk(Double codigoPk) {
		this.codigoPk = codigoPk;
	}

	public Double getDetPlanTratamiento() {
		return detPlanTratamiento;
	}

	public void setDetPlanTratamiento(Double detPlanTratamiento) {
		this.detPlanTratamiento = detPlanTratamiento;
	}

	public int getPiezaDental() {
		return piezaDental;
	}

	public void setPiezaDental(int piezaDental) {
		this.piezaDental = piezaDental;
	}

	public int getSuperficie() {
		return superficie;
	}

	public void setSuperficie(int superficie) {
		this.superficie = superficie;
	}

	public int getHallazgo() {
		return hallazgo;
	}

	public void setHallazgo(int hallazgo) {
		this.hallazgo = hallazgo;
	}

	public String getClasificacion() {
		return clasificacion;
	}

	public void setClasificacion(String clasificacion) {
		this.clasificacion = clasificacion;
	}

	public String getPorConfirmar() {
		return porConfirmar;
	}

	public void setPorConfirmar(String porConfirmar) {
		this.porConfirmar = porConfirmar;
	}

	public int getConvencion() {
		return convencion;
	}

	public void setConvencion(int convencion) {
		this.convencion = convencion;
	}

	public DtoInfoFechaUsuario getUsuarioModifica() {
		return usuarioModifica;
	}

	public void setUsuarioModifica(DtoInfoFechaUsuario usuarioModifica) {
		this.usuarioModifica = usuarioModifica;
	}

	public int getCita() {
		return cita;
	}

	public void setCita(int cita) {
		this.cita = cita;
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

	public InfoDatosInt getEspecialidad() {
		return especialidad;
	}

	public void setEspecialidad(InfoDatosInt especialidad) {
		this.especialidad = especialidad;
	}

	public String getActivo() {
		return activo;
	}

	public void setActivo(String activo) {
		this.activo = activo;
	}
}
