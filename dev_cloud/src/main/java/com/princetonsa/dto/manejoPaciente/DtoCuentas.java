/*
 * Jun 27, 2007
 */
package com.princetonsa.dto.manejoPaciente;


import java.io.Serializable;
import java.math.BigDecimal;

import util.ConstantesBD;
import util.InfoDatosInt;
import util.InfoDatosString;

/**
 * 
 * Dto Para el manejo de la información de cuentas
 * @author Sebastián Gómez R.
 *
 */
public class DtoCuentas implements Serializable
{
	/**
	 * Serial version UID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Id de la cuenta
	 */
	private String idCuenta;
	
	/**
	 * Id del ingreso
	 */
	private String idIngreso;
	
	/**
	 * Fecha Apertura
	 */
	private String fechaApertura;
	
	/**
	 * Hora apertura
	 */
	private String horaApertura;
	
	/**
	 * Estado
	 */
	private InfoDatosInt estado;
	
	/**
	 * Paciente
	 */
	private InfoDatosString paciente;
	
	/**
	 * Vía de ingreso
	 */
	private InfoDatosInt viaIngreso;
	
	/**
	 * Tipo de complejidad
	 */
	private InfoDatosInt tipoComplejidad;
	
	/**
	 * Tipo Paciente
	 */
	private InfoDatosString tipoPaciente;
	
	/**
	 * Tipo Evento
	 */
	private InfoDatosString tipoEvento;
	
	/**
	 * Arp Afiliado
	 */
	private InfoDatosInt convenioArpAfiliado;
	
	/**
	 * Origen Admision
	 */
	private InfoDatosInt origenAdmision;
	
	/**
	 * Área
	 */
	private InfoDatosInt area;
	
	/**
	 * desplazado
	 */
	private boolean desplazado;
	
	/**
	 * Login del usuario que inserta/modifica
	 */
	private String loginUsuario;
	
	/**
	 * Convenios de la cuenta
	 */
	private DtoSubCuentas[] convenios;
	
	/**
	 * Indicador para saber si tiene responsable paciente
	 */
	private boolean tieneResponsablePaciente;
	
	/**
	 * Responsable paciente
	 */
	private DtoResponsablePaciente responsablePaciente;
	
	/**
	 * Indicativo hospital día
	 */
	private boolean hospitalDia;
	
	/**
	 * Consecutivo del Ingreso en Pacientes Entidades Subcontratadas
	 * */
	private String pacEntidadesSubcontratdas;
	
	/**
	 * Descripcion de la entidad subcontratada del ingreso del paciente
	 * */
	private String descEntidadesSubcontratadas;
	
	
	private String transplante ="";
	
	private int codigoTipoMonitoreo;
	
	/**
	 * Observaciones de la  cuenta
	 */
	private String observaciones;
	
	/**
	 * 
	 */
	private String consecutivoIngreso;
	
	/**
	 * Constructor DtoCuentas
	 *
	 */
	public DtoCuentas()
	{
		this.idCuenta = "";
		this.idIngreso = "";
		this.fechaApertura = "";
		this.horaApertura = "";
		this.estado = new InfoDatosInt();
		this.paciente = new InfoDatosString();
		this.viaIngreso = new InfoDatosInt();
		this.tipoComplejidad = new InfoDatosInt();
		this.tipoPaciente = new InfoDatosString();
		this.tipoEvento = new InfoDatosString();
		this.convenioArpAfiliado = new InfoDatosInt();
		this.origenAdmision = new InfoDatosInt();
		this.area = new InfoDatosInt();
		this.convenios = new DtoSubCuentas[0];
		this.desplazado = false;
		this.loginUsuario = "";
		this.tieneResponsablePaciente = false;
		this.responsablePaciente = new DtoResponsablePaciente();
		this.hospitalDia = false;
		this.pacEntidadesSubcontratdas ="";
		this.descEntidadesSubcontratadas = "";
		this.transplante = "";
		this.codigoTipoMonitoreo = ConstantesBD.codigoNuncaValido;
		this.observaciones = "";
		this.consecutivoIngreso="";
	}

	
	public String getTransplante() {
		return transplante;
	}

	public void setTransplante(String transplante) {
		this.transplante = transplante;
	}

	/**
	 * @return the area
	 */
	public String getDescripcionArea() {
		return area.getDescripcion();
	}
	
	/**
	 * @return the area
	 */
	public int getCodigoArea() {
		return area.getCodigo();
	}

	/**
	 * @param area the area to set
	 */
	public void setDescripcionArea(String area) {
		this.area.setDescripcion(area);
	}
	
	/**
	 * @param area the area to set
	 */
	public void setCodigoArea(int area) {
		this.area.setCodigo(area);
	}

	/**
	 * @return the convenioArpAfiliado
	 */
	public String getDescripcionConvenioArpAfiliado() {
		return convenioArpAfiliado.getDescripcion();
	}
	
	/**
	 * @return the convenioArpAfiliado
	 */
	public int getCodigoConvenioArpAfiliado() {
		return convenioArpAfiliado.getCodigo();
	}

	/**
	 * @param convenioArpAfiliado the convenioArpAfiliado to set
	 */
	public void setDescripcionConvenioArpAfiliado(String convenioArpAfiliado) {
		this.convenioArpAfiliado.setDescripcion(convenioArpAfiliado);
	}
	
	/**
	 * @param convenioArpAfiliado the convenioArpAfiliado to set
	 */
	public void setCodigoConvenioArpAfiliado(int convenioArpAfiliado) {
		this.convenioArpAfiliado.setCodigo(convenioArpAfiliado);
	}

	/**
	 * @return the convenios
	 */
	public DtoSubCuentas[] getConvenios() {
		return convenios;
	}

	/**
	 * @param convenios the convenios to set
	 */
	public void setConvenios(DtoSubCuentas[] convenios) {
		this.convenios = convenios;
	}

	/**
	 * @return the idCuenta
	 */
	public String getIdCuenta() {
		return idCuenta;
	}

	/**
	 * @param idCuenta the idCuenta to set
	 */
	public void setIdCuenta(String idCuenta) {
		this.idCuenta = idCuenta;
	}

	/**
	 * @return the origenAdmision
	 */
	public String getDescripcionOrigenAdmision() {
		return origenAdmision.getDescripcion();
	}
	
	/**
	 * @return the origenAdmision
	 */
	public int getCodigoOrigenAdmision() {
		return origenAdmision.getCodigo();
	}

	/**
	 * @param origenAdmision the origenAdmision to set
	 */
	public void setDescripcionOrigenAdmision(String origenAdmision) {
		this.origenAdmision.setDescripcion(origenAdmision);
	}
	
	/**
	 * @param origenAdmision the origenAdmision to set
	 */
	public void setCodigoOrigenAdmision(int origenAdmision) {
		this.origenAdmision.setCodigo(origenAdmision);
	}

	/**
	 * @return the tipoComplejidad
	 */
	public String getDescripcionTipoComplejidad() {
		return tipoComplejidad.getDescripcion();
	}
	
	/**
	 * @return the tipoComplejidad
	 */
	public int getCodigoTipoComplejidad() {
		return tipoComplejidad.getCodigo();
	}

	/**
	 * @param tipoComplejidad the tipoComplejidad to set
	 */
	public void setDescripcionTipoComplejidad(String tipoComplejidad) {
		this.tipoComplejidad.setDescripcion(tipoComplejidad);
	}
	
	/**
	 * @param tipoComplejidad the tipoComplejidad to set
	 */
	public void setCodigoTipoComplejidad(int tipoComplejidad) {
		this.tipoComplejidad.setCodigo (tipoComplejidad);
	}

	/**
	 * @return the tipoEvento
	 */
	public String getDescripcionTipoEvento() {
		return tipoEvento.getDescripcion();
	}
	
	/**
	 * @return the tipoEvento
	 */
	public String getCodigoTipoEvento() {
		return tipoEvento.getCodigo();
	} 
	
	/**
	 * @param tipoEvento the tipoEvento to set
	 */
	public void setDescripcionTipoEvento(String tipoEvento) {
		this.tipoEvento.setDescripcion(tipoEvento);
	}
	
	/**
	 * @param tipoEvento the tipoEvento to set
	 */
	public void setCodigoTipoEvento(String tipoEvento) {
		this.tipoEvento.setCodigo(tipoEvento);
	}

	/**
	 * @return the tipoPaciente
	 */
	public String getDescripcionTipoPaciente() {
		return tipoPaciente.getDescripcion();
	}
	
	/**
	 * @return the tipoPaciente
	 */
	public String getCodigoTipoPaciente() {
		return tipoPaciente.getCodigo();
	}

	/**
	 * @param tipoPaciente the tipoPaciente to set
	 */
	public void setDescripcionTipoPaciente(String tipoPaciente) {
		this.tipoPaciente.setDescripcion(tipoPaciente);
	}
	
	/**
	 * @param tipoPaciente the tipoPaciente to set
	 */
	public void setCodigoTipoPaciente(String tipoPaciente) {
		this.tipoPaciente.setCodigo(tipoPaciente);
	}

	/**
	 * @return the viaIngreso
	 */
	public String getDescripcionViaIngreso() {
		return viaIngreso.getDescripcion();
	}
	
	/**
	 * @return the viaIngreso
	 */
	public int getCodigoViaIngreso() {
		return viaIngreso.getCodigo();
	}

	/**
	 * @param viaIngreso the viaIngreso to set
	 */
	public void setDescripcionViaIngreso(String viaIngreso) {
		this.viaIngreso.setDescripcion(viaIngreso);
	}
	
	/**
	 * @param viaIngreso the viaIngreso to set
	 */
	public void setCodigoViaIngreso(int viaIngreso) {
		this.viaIngreso.setCodigo(viaIngreso);
	}

	/**
	 * @return the idIngreso
	 */
	public String getIdIngreso() {
		return idIngreso;
	}

	/**
	 * @param idIngreso the idIngreso to set
	 */
	public void setIdIngreso(String idIngreso) {
		this.idIngreso = idIngreso;
	}

	/**
	 * @return the paciente
	 */
	public String getDescripcionPaciente() {
		return paciente.getDescripcion();
	}
	
	/**
	 * @return the paciente
	 */
	public String getCodigoPaciente() {
		return paciente.getCodigo();
	}

	/**
	 * @param paciente the paciente to set
	 */
	public void setDescripcionPaciente(String paciente) {
		this.paciente.setDescripcion(paciente);
	}
	
	/**
	 * @param paciente the paciente to set
	 */
	public void setCodigoPaciente(String paciente) {
		this.paciente.setCodigo(paciente);
	}

	/**
	 * @return the desplazado
	 */
	public boolean isDesplazado() {
		return desplazado;
	}

	/**
	 * @param desplazado the desplazado to set
	 */
	public void setDesplazado(boolean desplazado) {
		this.desplazado = desplazado;
	}

	/**
	 * @return the estado
	 */
	public String getDescripcionEstado() {
		return estado.getDescripcion();
	}
	
	/**
	 * @return the estado
	 */
	public int getCodigoEstado() {
		return estado.getCodigo();
	}

	/**
	 * @param estado the estado to set
	 */
	public void setDescripcionEstado(String estado) {
		this.estado.setDescripcion(estado);
	}
	
	/**
	 * @param estado the estado to set
	 */
	public void setCodigoEstado(int estado) {
		this.estado.setCodigo(estado);
	}

	/**
	 * @return the fechaApertura
	 */
	public String getFechaApertura() {
		return fechaApertura;
	}

	/**
	 * @param fechaApertura the fechaApertura to set
	 */
	public void setFechaApertura(String fechaApertura) {
		this.fechaApertura = fechaApertura;
	}

	/**
	 * @return the horaApertura
	 */
	public String getHoraApertura() {
		return horaApertura;
	}

	/**
	 * @param horaApertura the horaApertura to set
	 */
	public void setHoraApertura(String horaApertura) {
		this.horaApertura = horaApertura;
	}

	/**
	 * @return the loginUsuario
	 */
	public String getLoginUsuario() {
		return loginUsuario;
	}

	/**
	 * @param loginUsuario the loginUsuario to set
	 */
	public void setLoginUsuario(String loginUsuario) {
		this.loginUsuario = loginUsuario;
	}

	/**
	 * @return the responsablePaciente
	 */
	public DtoResponsablePaciente getResponsablePaciente() {
		return responsablePaciente;
	}

	/**
	 * @param responsablePaciente the responsablePaciente to set
	 */
	public void setResponsablePaciente(DtoResponsablePaciente responsablePaciente) {
		this.responsablePaciente = responsablePaciente;
	}

	/**
	 * @return the tieneResponsablePaciente
	 */
	public boolean isTieneResponsablePaciente() {
		return tieneResponsablePaciente;
	}

	/**
	 * @param tieneResponsablePaciente the tieneResponsablePaciente to set
	 */
	public void setTieneResponsablePaciente(boolean tieneResponsablePaciente) {
		this.tieneResponsablePaciente = tieneResponsablePaciente;
	}

	/**
	 * @return the hospitalDia
	 */
	public boolean isHospitalDia() {
		return hospitalDia;
	}

	/**
	 * @param hospitalDia the hospitalDia to set
	 */
	public void setHospitalDia(boolean hospitalDia) {
		this.hospitalDia = hospitalDia;
	}

	/**
	 * @return the pacEntidadesSubcontratdas
	 */
	public String getPacEntidadesSubcontratdas() {
		return pacEntidadesSubcontratdas;
	}

	/**
	 * @param pacEntidadesSubcontratdas the pacEntidadesSubcontratdas to set
	 */
	public void setPacEntidadesSubcontratdas(String pacEntidadesSubcontratdas) {
		this.pacEntidadesSubcontratdas = pacEntidadesSubcontratdas;
	}

	/**
	 * @return the descEntidadesSubcontratadas
	 */
	public String getDescEntidadesSubcontratadas() {
		return descEntidadesSubcontratadas;
	}

	/**
	 * @param descEntidadesSubcontratadas the descEntidadesSubcontratadas to set
	 */
	public void setDescEntidadesSubcontratadas(String descEntidadesSubcontratadas) {
		this.descEntidadesSubcontratadas = descEntidadesSubcontratadas;
	}


	/**
	 * @return the codigoTipoMonitoreo
	 */
	public int getCodigoTipoMonitoreo() {
		return codigoTipoMonitoreo;
	}


	/**
	 * @param codigoTipoMonitoreo the codigoTipoMonitoreo to set
	 */
	public void setCodigoTipoMonitoreo(int codigoTipoMonitoreo) {
		this.codigoTipoMonitoreo = codigoTipoMonitoreo;
	}


	/**
	 * @return the observaciones
	 */
	public String getObservaciones() {
		return observaciones;
	}


	/**
	 * @param observaciones the observaciones to set
	 */
	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}


	public String getConsecutivoIngreso() {
		return consecutivoIngreso;
	}


	public void setConsecutivoIngreso(String consecutivoIngreso) {
		this.consecutivoIngreso = consecutivoIngreso;
	}

	
	
}
