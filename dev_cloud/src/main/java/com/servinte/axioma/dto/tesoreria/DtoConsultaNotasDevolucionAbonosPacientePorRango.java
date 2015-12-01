package com.servinte.axioma.dto.tesoreria;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import util.ConstantesBD;

public class DtoConsultaNotasDevolucionAbonosPacientePorRango implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * Atributo que almacena la fecha de las notas de devoluci�n de abonos paciente.
	 */
	private Date fechaNotaDevolucion;

	/**
	 * Atributo que almacena la hora de las notas de devoluci�n de abonos paciente.
	 */
	private String horaNotaDevolucion;

	/**
	 * Atributo que almacena la fecha inicial a partir de la cual se debe
	 * realizar la consulta de las notas de devoluci�n de abonos paciente.
	 */
	private Date fechaInicial;

	/**
	 * Atributo que almacena la fecha final hasta la cual se debe realizar la
	 * consulta de las notas de devoluci�n de abonos paciente.
	 */
	private Date fechaFinal;

	/**
	 * Atributo que almacena el consecutivo de devoluci�n de abonos paciente.
	 */
	private BigDecimal nroNotaDevolucion;

	/**
	 * Atributos que almacenan el consecutivo de devoluci�n desde el que se
	 * quiere hacer la consulta de las notas de devoluci�n de abonos paciente.
	 */
	private String numeroNotaDevolucionInicial;

	/**
	 * Atributos que almacenan el consecutivo de devoluci�n hasta el que se
	 * quiere hacer la consulta de las notas de devoluci�n de abonos paciente.
	 */
	private String numeroNotaDevolucionFinal;

	/**
	 * Atributos que almacenan el c�digo y el nombre de la empresa-instituci�n.
	 */
	private long codigoEmpresaInstitucion;
	private String nombreEmpresaInstitucion;

	/**
	 * Atributos que almacenan el consecutivo y el c�digo del
	 * centro de atenci�n.
	 */
	private Integer consecutivoCentroAtencion;
	private String nombreCentroAtencion;

	/**
	 * Atributo que almacena el login y el nombre del usuario.
	 */
	private String login;
	private String nombreUsuario;

	/**
	 * Atributo que almacena los ingresos para los cuales se realiz� un
	 * devoluci�n en la nota de devoluci�n.
	 */
	private String ingresos;

	/**
	 * Atributo que almacena el valor por el cual se realiz� la nota de
	 * devoluci�n.
	 */
	private BigDecimal valorNota;

	/**
	 * Atributo que almacena el motivo por el cual fue realizada la nota de
	 * devoluci�n seleccionada.
	 */
	private String motivoNotaDevolucion;

	/**
	 * Datos del paciente.
	 */
	private String tipoIdentificacion;
	private String numeroIdentificacion;
	private String primerNombrePaciente;
	private String segundoNombrePaciente;
	private String primerApellidoPaciente;
	private String segundoApellidoPaciente;

	private boolean multiempresa;

	/**
	 * M�todo constructor de la clase
	 */
	public DtoConsultaNotasDevolucionAbonosPacientePorRango() {
		this.fechaNotaDevolucion = null;
		this.horaNotaDevolucion = null;
		this.fechaInicial = null;
		this.fechaFinal = null;
		this.numeroNotaDevolucionInicial = "";
		this.numeroNotaDevolucionFinal = "";
		this.codigoEmpresaInstitucion = ConstantesBD.codigoNuncaValidoLong;
		this.nombreEmpresaInstitucion = "";
		this.consecutivoCentroAtencion = ConstantesBD.codigoNuncaValido;
		this.nombreCentroAtencion = "";
		this.login = "";
		this.nombreUsuario = "";
	}

	/**
	 * M�todo encargado de obtener el valor del atributo fechaNotaDevolucion.
	 * 
	 * @return fechaNotaDevolucion
	 * @author Luis Fernando Hincapi� Ospina
	 */
	public Date getFechaNotaDevolucion() {
		return fechaNotaDevolucion;
	}

	/**
	 * M�todo encargado de establecer el valor del atributo fechaNotaDevolucion.
	 * 
	 * @param fechaNotaDevolucion
	 * @author Luis Fernando Hincapi� Ospina
	 */
	public void setFechaNotaDevolucion(Date fechaNotaDevolucion) {
		this.fechaNotaDevolucion = fechaNotaDevolucion;
	}

	/**
	 * M�todo encargado de obtener el valor del atributo horaNotaDevolucion.
	 * 
	 * @return horaNotaDevolucion
	 * @author Luis Fernando Hincapi� Ospina
	 */
	public String getHoraNotaDevolucion() {
		return horaNotaDevolucion;
	}

	/**
	 * M�todo encargado de establecer el valor del atributo horaNotaDevolucion.
	 * 
	 * @param horaNotaDevolucion
	 * @author Luis Fernando Hincapi� Ospina
	 */
	public void setHoraNotaDevolucion(String horaNotaDevolucion) {
		this.horaNotaDevolucion = horaNotaDevolucion;
	}

	/**
	 * M�todo encargado de obtener el valor del atributo fechaInicial.
	 * 
	 * @return fechaInicial
	 * @author Luis Fernando Hincapi� Ospina
	 */
	public Date getFechaInicial() {
		return fechaInicial;
	}

	/**
	 * M�todo encargado de establecer el valor del atributo fechaInicial.
	 * 
	 * @param fechaInicial
	 * @author Luis Fernando Hincapi� Ospina
	 */
	public void setFechaInicial(Date fechaInicial) {
		this.fechaInicial = fechaInicial;
	}

	/**
	 * M�todo encargado de obtener el valor del atributo fechaFinal.
	 * 
	 * @return fechaFinal
	 * @author Luis Fernando Hincapi� Ospina
	 */
	public Date getFechaFinal() {
		return fechaFinal;
	}

	/**
	 * M�todo encargado de establecer el valor del atributo fechaFinal.
	 * 
	 * @param fechaFinal
	 * @author Luis Fernando Hincapi� Ospina
	 */
	public void setFechaFinal(Date fechaFinal) {
		this.fechaFinal = fechaFinal;
	}

	/**
	 * M�todo encargado de obtener el valor del atributo nroNotaDevolucion.
	 * 
	 * @return nroNotaDevolucion
	 * @author Luis Fernando Hincapi� Ospina
	 */
	public BigDecimal getNroNotaDevolucion() {
		return nroNotaDevolucion;
	}

	/**
	 * M�todo encargado de establecer el valor del atributo nroNotaDevolucion.
	 * 
	 * @param nroNotaDevolucion
	 * @author Luis Fernando Hincapi� Ospina
	 */
	public void setNroNotaDevolucion(BigDecimal nroNotaDevolucion) {
		this.nroNotaDevolucion = nroNotaDevolucion;
	}

	/**
	 * M�todo encargado de obtener el valor del atributo
	 * numeroNotaDevolucionInicial.
	 * 
	 * @return numeroNotaDevolucionInicial
	 * @author Luis Fernando Hincapi� Ospina
	 */
	public String getNumeroNotaDevolucionInicial() {
		return numeroNotaDevolucionInicial;
	}

	/**
	 * M�todo encargado de establecer el valor del atributo
	 * numeroNotaDevolucionInicial.
	 * 
	 * @param numeroNotaDevolucionInicial
	 * @author Luis Fernando Hincapi� Ospina
	 */
	public void setNumeroNotaDevolucionInicial(
			String numeroNotaDevolucionInicial) {
		this.numeroNotaDevolucionInicial = numeroNotaDevolucionInicial;
	}

	/**
	 * M�todo encargado de obtener el valor del atributo
	 * numeroNotaDevolucionFinal.
	 * 
	 * @return numeroNotaDevolucionFinal
	 * @author Luis Fernando Hincapi� Ospina
	 */
	public String getNumeroNotaDevolucionFinal() {
		return numeroNotaDevolucionFinal;
	}

	/**
	 * M�todo encargado de establecer el valor del atributo
	 * numeroNotaDevolucionFinal.
	 * 
	 * @param numeroNotaDevolucionFinal
	 * @author Luis Fernando Hincapi� Ospina
	 */
	public void setNumeroNotaDevolucionFinal(String numeroNotaDevolucionFinal) {
		this.numeroNotaDevolucionFinal = numeroNotaDevolucionFinal;
	}

	/**
	 * M�todo encargado de obtener el valor del atributo
	 * codigoEmpresaInstitucion.
	 * 
	 * @return codigoEmpresaInstitucion
	 * @author Luis Fernando Hincapi� Ospina
	 */
	public long getCodigoEmpresaInstitucion() {
		return codigoEmpresaInstitucion;
	}

	/**
	 * M�todo encargado de establecer el valor del atributo
	 * codigoEmpresaInstitucion.
	 * 
	 * @param codigoEmpresaInstitucion
	 * @author Luis Fernando Hincapi� Ospina
	 */
	public void setCodigoEmpresaInstitucion(long codigoEmpresaInstitucion) {
		this.codigoEmpresaInstitucion = codigoEmpresaInstitucion;
	}

	/**
	 * M�todo encargado de obtener el valor del atributo
	 * nombreEmpresaInstitucion.
	 * 
	 * @return nombreEmpresaInstitucion
	 * @author Luis Fernando Hincapi� Ospina
	 */
	public String getNombreEmpresaInstitucion() {
		return nombreEmpresaInstitucion;
	}

	/**
	 * M�todo encargado de establecer el valor del atributo
	 * nombreEmpresaInstitucion.
	 * 
	 * @param nombreEmpresaInstitucion
	 * @author Luis Fernando Hincapi� Ospina
	 */
	public void setNombreEmpresaInstitucion(String nombreEmpresaInstitucion) {
		this.nombreEmpresaInstitucion = nombreEmpresaInstitucion;
	}

	/**
	 * M�todo encargado de obtener el valor del atributo
	 * consecutivoCentroAtencion.
	 * 
	 * @return consecutivoCentroAtencion
	 * @author Luis Fernando Hincapi� Ospina
	 */
	public Integer getConsecutivoCentroAtencion() {
		return consecutivoCentroAtencion;
	}

	/**
	 * M�todo encargado de establecer el valor del atributo
	 * consecutivoCentroAtencion.
	 * 
	 * @param consecutivoCentroAtencion
	 * @author Luis Fernando Hincapi� Ospina
	 */
	public void setConsecutivoCentroAtencion(Integer consecutivoCentroAtencion) {
		this.consecutivoCentroAtencion = consecutivoCentroAtencion;
	}

	/**
	 * M�todo encargado de obtener el valor del atributo
	 * nombreCentroAtencion.
	 * 
	 * @return nombreCentroAtencion
	 * @author Luis Fernando Hincapi� Ospina
	 */
	public String getNombreCentroAtencion() {
		return nombreCentroAtencion;
	}

	/**
	 * M�todo encargado de establecer el valor del atributo
	 * nombreCentroAtencion.
	 * 
	 * @param nombreCentroAtencion
	 * @author Luis Fernando Hincapi� Ospina
	 */
	public void setNombreCentroAtencion(String nombreCentroAtencion) {
		this.nombreCentroAtencion = nombreCentroAtencion;
	}

	/**
	 * M�todo encargado de obtener el valor del atributo login.
	 * 
	 * @return login
	 * @author Luis Fernando Hincapi� Ospina
	 */
	public String getLogin() {
		return login;
	}

	/**
	 * M�todo encargado de establecer el valor del atributo login.
	 * 
	 * @param login
	 * @author Luis Fernando Hincapi� Ospina
	 */
	public void setLogin(String login) {
		this.login = login;
	}

	/**
	 * M�todo encargado de obtener el valor del atributo nombreUsuario.
	 * 
	 * @return nombreUsuario
	 * @author Luis Fernando Hincapi� Ospina
	 */
	public String getNombreUsuario() {
		return nombreUsuario;
	}

	/**
	 * M�todo encargado de establecer el valor del atributo nombreUsuario.
	 * 
	 * @param nombreUsuario
	 * @author Luis Fernando Hincapi� Ospina
	 */
	public void setNombreUsuario(String nombreUsuario) {
		this.nombreUsuario = nombreUsuario;
	}

	/**
	 * M�todo encargado de obtener el valor del atributo ingresos.
	 * 
	 * @return ingresos
	 * @author Luis Fernando Hincapi� Ospina
	 */
	public String getIngresos() {
		return ingresos;
	}

	/**
	 * M�todo encargado de establecer el valor del atributo ingresos.
	 * 
	 * @param ingresos
	 * @author Luis Fernando Hincapi� Ospina
	 */
	public void setIngresos(String ingresos) {
		this.ingresos = ingresos;
	}

	/**
	 * M�todo encargado de obtener el valor del atributo valorNota.
	 * 
	 * @return valorNota
	 * @author Luis Fernando Hincapi� Ospina
	 */
	public BigDecimal getValorNota() {
		return valorNota;
	}

	/**
	 * M�todo encargado de establecer el valor del atributo valorNota.
	 * 
	 * @param valorNota
	 * @author Luis Fernando Hincapi� Ospina
	 */
	public void setValorNota(BigDecimal valorNota) {
		this.valorNota = valorNota;
	}

	/**
	 * M�todo encargado de obtener el valor del atributo motivoNotaDevolucion.
	 * 
	 * @return motivoNotaDevolucion
	 * @author Luis Fernando Hincapi� Ospina
	 */
	public String getMotivoNotaDevolucion() {
		return motivoNotaDevolucion;
	}

	/**
	 * M�todo encargado de establecer el valor del atributo
	 * motivoNotaDevolucion.
	 * 
	 * @param motivoNotaDevolucion
	 * @author Luis Fernando Hincapi� Ospina
	 */
	public void setMotivoNotaDevolucion(String motivoNotaDevolucion) {
		this.motivoNotaDevolucion = motivoNotaDevolucion;
	}

	/**
	 * M�todo encargado de obtener el valor del atributo tipoIdentificacion.
	 * 
	 * @return tipoIdentificacion
	 * @author Luis Fernando Hincapi� Ospina
	 */
	public String getTipoIdentificacion() {
		return tipoIdentificacion;
	}

	/**
	 * M�todo encargado de establecer el valor del atributo tipoIdentificacion.
	 * 
	 * @param tipoIdentificacion
	 * @author Luis Fernando Hincapi� Ospina
	 */
	public void setTipoIdentificacion(String tipoIdentificacion) {
		this.tipoIdentificacion = tipoIdentificacion;
	}

	/**
	 * M�todo encargado de obtener el valor del atributo numeroIdentificacion.
	 * 
	 * @return numeroIdentificacion
	 * @author Luis Fernando Hincapi� Ospina
	 */
	public String getNumeroIdentificacion() {
		return numeroIdentificacion;
	}

	/**
	 * M�todo encargado de establecer el valor del atributo numeroIdentificacion.
	 * 
	 * @param numeroIdentificacion
	 * @author Luis Fernando Hincapi� Ospina
	 */
	public void setNumeroIdentificacion(String numeroIdentificacion) {
		this.numeroIdentificacion = numeroIdentificacion;
	}

	/**
	 * M�todo encargado de obtener el valor del atributo primerNombrePaciente.
	 * 
	 * @return primerNombrePaciente
	 * @author Luis Fernando Hincapi� Ospina
	 */
	public String getPrimerNombrePaciente() {
		return primerNombrePaciente;
	}

	/**
	 * M�todo encargado de establecer el valor del atributo primerNombrePaciente.
	 * 
	 * @param primerNombrePaciente
	 * @author Luis Fernando Hincapi� Ospina
	 */
	public void setPrimerNombrePaciente(String primerNombrePaciente) {
		this.primerNombrePaciente = primerNombrePaciente;
	}

	/**
	 * M�todo encargado de obtener el valor del atributo segundoNombrePaciente.
	 * 
	 * @return segundoNombrePaciente
	 * @author Luis Fernando Hincapi� Ospina
	 */
	public String getSegundoNombrePaciente() {
		return segundoNombrePaciente;
	}

	/**
	 * M�todo encargado de establecer el valor del atributo segundoNombrePaciente.
	 * 
	 * @param segundoNombrePaciente
	 * @author Luis Fernando Hincapi� Ospina
	 */
	public void setSegundoNombrePaciente(String segundoNombrePaciente) {
		this.segundoNombrePaciente = segundoNombrePaciente;
	}

	/**
	 * M�todo encargado de obtener el valor del atributo primerApellidoPaciente.
	 * 
	 * @return primerApellidoPaciente
	 * @author Luis Fernando Hincapi� Ospina
	 */
	public String getPrimerApellidoPaciente() {
		return primerApellidoPaciente;
	}

	/**
	 * M�todo encargado de establecer el valor del atributo primerApellidoPaciente.
	 * 
	 * @param primerApellidoPaciente
	 * @author Luis Fernando Hincapi� Ospina
	 */
	public void setPrimerApellidoPaciente(String primerApellidoPaciente) {
		this.primerApellidoPaciente = primerApellidoPaciente;
	}

	/**
	 * M�todo encargado de obtener el valor del atributo segundoApellidoPaciente.
	 * 
	 * @return segundoApellidoPaciente
	 * @author Luis Fernando Hincapi� Ospina
	 */
	public String getSegundoApellidoPaciente() {
		return segundoApellidoPaciente;
	}

	/**
	 * M�todo encargado de establecer el valor del atributo segundoApellidoPaciente.
	 * 
	 * @param segundoApellidoPaciente
	 * @author Luis Fernando Hincapi� Ospina
	 */
	public void setSegundoApellidoPaciente(String segundoApellidoPaciente) {
		this.segundoApellidoPaciente = segundoApellidoPaciente;
	}

	/**
	 * M�todo encargado de obtener el valor del atributo multiempresa.
	 * 
	 * @return multiempresa
	 * @author Luis Fernando Hincapi� Ospina
	 */
	public boolean isMultiempresa() {
		return multiempresa;
	}

	/**
	 * M�todo encargado de establecer el valor del atributo multiempresa.
	 * 
	 * @param multiempresa
	 * @author Luis Fernando Hincapi� Ospina
	 */
	public void setMultiempresa(boolean multiempresa) {
		this.multiempresa = multiempresa;
	}

}
