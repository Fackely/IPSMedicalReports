package com.princetonsa.dto.odontologia;

import java.io.Serializable;

import util.ConstantesBD;
import util.InfoDatosInt;

public class DtoLogProgServPlant implements Serializable{
	
	private Double codigoPk;
	private Double progServPlant;
	private String estadoPrograma;
	private InfoDatosInt motivo;
	private int convencion;
	private String inclusion;
	private String exclusion;
	private String garantia;
	private String estadoServicio;
	private String indPrograma;
	private String indServicio;
	private String porConfirmar;
	private DtoInfoFechaUsuario usuarioModifica;
	private InfoDatosInt especialidad;
	private Double valoracion;
	private Double evolucion;
	private int ordenServicio;
	private String activo;
	private String historicoProgServ;
	
	public DtoLogProgServPlant()
	{
		reset();
	}
	
	public void reset()
	{
		codigoPk = ConstantesBD.codigoNuncaValidoDouble;
		progServPlant = ConstantesBD.codigoNuncaValidoDouble;
		estadoPrograma = "";
		motivo = new InfoDatosInt();
		convencion = ConstantesBD.codigoNuncaValido;
		inclusion = "";
		exclusion = "";
		garantia = "";
		estadoServicio = "";
		indPrograma = "";
		indServicio = "";
		porConfirmar = "";
		usuarioModifica = new DtoInfoFechaUsuario();
		especialidad = new InfoDatosInt();
		valoracion = ConstantesBD.codigoNuncaValidoDouble;
		evolucion = ConstantesBD.codigoNuncaValidoDouble;
		ordenServicio = ConstantesBD.codigoNuncaValido;
		activo = "";
		historicoProgServ = ConstantesBD.acronimoNo;
	}

	public Double getCodigoPk() {
		return codigoPk;
	}

	public void setCodigoPk(Double codigoPk) {
		this.codigoPk = codigoPk;
	}

	public Double getProgServPlant() {
		return progServPlant;
	}

	public void setProgServPlant(Double progServPlant) {
		this.progServPlant = progServPlant;
	}

	public String getEstadoPrograma() {
		return estadoPrograma;
	}

	public void setEstadoPrograma(String estadoPrograma) {
		this.estadoPrograma = estadoPrograma;
	}

	

	public int getConvencion() {
		return convencion;
	}

	public void setConvencion(int convencion) {
		this.convencion = convencion;
	}

	public String getInclusion() {
		return inclusion;
	}

	public void setInclusion(String inclusion) {
		this.inclusion = inclusion;
	}

	public String getExclusion() {
		return exclusion;
	}

	public void setExclusion(String exclusion) {
		this.exclusion = exclusion;
	}

	public String getGarantia() {
		return garantia;
	}

	public void setGarantia(String garantia) {
		this.garantia = garantia;
	}

	public String getEstadoServicio() {
		return estadoServicio;
	}

	public void setEstadoServicio(String estadoServicio) {
		this.estadoServicio = estadoServicio;
	}

	public String getIndPrograma() {
		return indPrograma;
	}

	public void setIndPrograma(String indPrograma) {
		this.indPrograma = indPrograma;
	}

	public String getIndServicio() {
		return indServicio;
	}

	public void setIndServicio(String indServicio) {
		this.indServicio = indServicio;
	}

	public String getPorConfirmar() {
		return porConfirmar;
	}

	public void setPorConfirmar(String porConfirmar) {
		this.porConfirmar = porConfirmar;
	}

	public DtoInfoFechaUsuario getUsuarioModifica() {
		return usuarioModifica;
	}

	public void setUsuarioModifica(DtoInfoFechaUsuario usuarioModifica) {
		this.usuarioModifica = usuarioModifica;
	}

	public InfoDatosInt getEspecialidad() {
		return especialidad;
	}

	public void setEspecialidad(InfoDatosInt especialidad) {
		this.especialidad = especialidad;
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

	public int getOrdenServicio() {
		return ordenServicio;
	}

	public void setOrdenServicio(int ordenServicio) {
		this.ordenServicio = ordenServicio;
	}

	public String getActivo() {
		return activo;
	}

	public void setActivo(String activo) {
		this.activo = activo;
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
	 * @return the historicoProgServ
	 */
	public String getHistoricoProgServ() {
		return historicoProgServ;
	}

	/**
	 * @param historicoProgServ the historicoProgServ to set
	 */
	public void setHistoricoProgServ(String historicoProgServ) {
		this.historicoProgServ = historicoProgServ;
	}

	
}