package com.servinte.axioma.dto.tesoreria;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import util.ConstantesBD;

public class DtoConsultaNotasDevolucionAbonosPacientePorRango implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * Atributo que almacena la fecha de las notas de devolución de abonos paciente.
	 */
	private Date fechaNotaDevolucion;

	/**
	 * Atributo que almacena la hora de las notas de devolución de abonos paciente.
	 */
	private String horaNotaDevolucion;

	/**
	 * Atributo que almacena la fecha inicial a partir de la cual se debe
	 * realizar la consulta de las notas de devolución de abonos paciente.
	 */
	private Date fechaInicial;

	/**
	 * Atributo que almacena la fecha final hasta la cual se debe realizar la
	 * consulta de las notas de devolución de abonos paciente.
	 */
	private Date fechaFinal;

	/**
	 * Atributo que almacena el consecutivo de devolución de abonos paciente.
	 */
	private BigDecimal nroNotaDevolucion;

	/**
	 * Atributos que almacenan el consecutivo de devolución desde el que se
	 * quiere hacer la consulta de las notas de devolución de abonos paciente.
	 */
	private String numeroNotaDevolucionInicial;

	/**
	 * Atributos que almacenan el consecutivo de devolución hasta el que se
	 * quiere hacer la consulta de las notas de devolución de abonos paciente.
	 */
	private String numeroNotaDevolucionFinal;

	/**
	 * Atributos que almacenan el código y el nombre de la empresa-institución.
	 */
	private long codigoEmpresaInstitucion;
	private String nombreEmpresaInstitucion;

	/**
	 * Atributos que almacenan el consecutivo y el código del
	 * centro de atención.
	 */
	private Integer consecutivoCentroAtencion;
	private String nombreCentroAtencion;

	/**
	 * Atributo que almacena el login y el nombre del usuario.
	 */
	private String login;
	private String nombreUsuario;

	/**
	 * Atributo que almacena los ingresos para los cuales se realizó un
	 * devolución en la nota de devolución.
	 */
	private String ingresos;

	/**
	 * Atributo que almacena el valor por el cual se realizó la nota de
	 * devolución.
	 */
	private BigDecimal valorNota;

	/**
	 * Atributo que almacena el motivo por el cual fue realizada la nota de
	 * devolución seleccionada.
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
	 * Método constructor de la clase
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
	 * Método encargado de obtener el valor del atributo fechaNotaDevolucion.
	 * 
	 * @return fechaNotaDevolucion
	 * @author Luis Fernando Hincapié Ospina
	 */
	public Date getFechaNotaDevolucion() {
		return fechaNotaDevolucion;
	}

	/**
	 * Método encargado de establecer el valor del atributo fechaNotaDevolucion.
	 * 
	 * @param fechaNotaDevolucion
	 * @author Luis Fernando Hincapié Ospina
	 */
	public void setFechaNotaDevolucion(Date fechaNotaDevolucion) {
		this.fechaNotaDevolucion = fechaNotaDevolucion;
	}

	/**
	 * Método encargado de obtener el valor del atributo horaNotaDevolucion.
	 * 
	 * @return horaNotaDevolucion
	 * @author Luis Fernando Hincapié Ospina
	 */
	public String getHoraNotaDevolucion() {
		return horaNotaDevolucion;
	}

	/**
	 * Método encargado de establecer el valor del atributo horaNotaDevolucion.
	 * 
	 * @param horaNotaDevolucion
	 * @author Luis Fernando Hincapié Ospina
	 */
	public void setHoraNotaDevolucion(String horaNotaDevolucion) {
		this.horaNotaDevolucion = horaNotaDevolucion;
	}

	/**
	 * Método encargado de obtener el valor del atributo fechaInicial.
	 * 
	 * @return fechaInicial
	 * @author Luis Fernando Hincapié Ospina
	 */
	public Date getFechaInicial() {
		return fechaInicial;
	}

	/**
	 * Método encargado de establecer el valor del atributo fechaInicial.
	 * 
	 * @param fechaInicial
	 * @author Luis Fernando Hincapié Ospina
	 */
	public void setFechaInicial(Date fechaInicial) {
		this.fechaInicial = fechaInicial;
	}

	/**
	 * Método encargado de obtener el valor del atributo fechaFinal.
	 * 
	 * @return fechaFinal
	 * @author Luis Fernando Hincapié Ospina
	 */
	public Date getFechaFinal() {
		return fechaFinal;
	}

	/**
	 * Método encargado de establecer el valor del atributo fechaFinal.
	 * 
	 * @param fechaFinal
	 * @author Luis Fernando Hincapié Ospina
	 */
	public void setFechaFinal(Date fechaFinal) {
		this.fechaFinal = fechaFinal;
	}

	/**
	 * Método encargado de obtener el valor del atributo nroNotaDevolucion.
	 * 
	 * @return nroNotaDevolucion
	 * @author Luis Fernando Hincapié Ospina
	 */
	public BigDecimal getNroNotaDevolucion() {
		return nroNotaDevolucion;
	}

	/**
	 * Método encargado de establecer el valor del atributo nroNotaDevolucion.
	 * 
	 * @param nroNotaDevolucion
	 * @author Luis Fernando Hincapié Ospina
	 */
	public void setNroNotaDevolucion(BigDecimal nroNotaDevolucion) {
		this.nroNotaDevolucion = nroNotaDevolucion;
	}

	/**
	 * Método encargado de obtener el valor del atributo
	 * numeroNotaDevolucionInicial.
	 * 
	 * @return numeroNotaDevolucionInicial
	 * @author Luis Fernando Hincapié Ospina
	 */
	public String getNumeroNotaDevolucionInicial() {
		return numeroNotaDevolucionInicial;
	}

	/**
	 * Método encargado de establecer el valor del atributo
	 * numeroNotaDevolucionInicial.
	 * 
	 * @param numeroNotaDevolucionInicial
	 * @author Luis Fernando Hincapié Ospina
	 */
	public void setNumeroNotaDevolucionInicial(
			String numeroNotaDevolucionInicial) {
		this.numeroNotaDevolucionInicial = numeroNotaDevolucionInicial;
	}

	/**
	 * Método encargado de obtener el valor del atributo
	 * numeroNotaDevolucionFinal.
	 * 
	 * @return numeroNotaDevolucionFinal
	 * @author Luis Fernando Hincapié Ospina
	 */
	public String getNumeroNotaDevolucionFinal() {
		return numeroNotaDevolucionFinal;
	}

	/**
	 * Método encargado de establecer el valor del atributo
	 * numeroNotaDevolucionFinal.
	 * 
	 * @param numeroNotaDevolucionFinal
	 * @author Luis Fernando Hincapié Ospina
	 */
	public void setNumeroNotaDevolucionFinal(String numeroNotaDevolucionFinal) {
		this.numeroNotaDevolucionFinal = numeroNotaDevolucionFinal;
	}

	/**
	 * Método encargado de obtener el valor del atributo
	 * codigoEmpresaInstitucion.
	 * 
	 * @return codigoEmpresaInstitucion
	 * @author Luis Fernando Hincapié Ospina
	 */
	public long getCodigoEmpresaInstitucion() {
		return codigoEmpresaInstitucion;
	}

	/**
	 * Método encargado de establecer el valor del atributo
	 * codigoEmpresaInstitucion.
	 * 
	 * @param codigoEmpresaInstitucion
	 * @author Luis Fernando Hincapié Ospina
	 */
	public void setCodigoEmpresaInstitucion(long codigoEmpresaInstitucion) {
		this.codigoEmpresaInstitucion = codigoEmpresaInstitucion;
	}

	/**
	 * Método encargado de obtener el valor del atributo
	 * nombreEmpresaInstitucion.
	 * 
	 * @return nombreEmpresaInstitucion
	 * @author Luis Fernando Hincapié Ospina
	 */
	public String getNombreEmpresaInstitucion() {
		return nombreEmpresaInstitucion;
	}

	/**
	 * Método encargado de establecer el valor del atributo
	 * nombreEmpresaInstitucion.
	 * 
	 * @param nombreEmpresaInstitucion
	 * @author Luis Fernando Hincapié Ospina
	 */
	public void setNombreEmpresaInstitucion(String nombreEmpresaInstitucion) {
		this.nombreEmpresaInstitucion = nombreEmpresaInstitucion;
	}

	/**
	 * Método encargado de obtener el valor del atributo
	 * consecutivoCentroAtencion.
	 * 
	 * @return consecutivoCentroAtencion
	 * @author Luis Fernando Hincapié Ospina
	 */
	public Integer getConsecutivoCentroAtencion() {
		return consecutivoCentroAtencion;
	}

	/**
	 * Método encargado de establecer el valor del atributo
	 * consecutivoCentroAtencion.
	 * 
	 * @param consecutivoCentroAtencion
	 * @author Luis Fernando Hincapié Ospina
	 */
	public void setConsecutivoCentroAtencion(Integer consecutivoCentroAtencion) {
		this.consecutivoCentroAtencion = consecutivoCentroAtencion;
	}

	/**
	 * Método encargado de obtener el valor del atributo
	 * nombreCentroAtencion.
	 * 
	 * @return nombreCentroAtencion
	 * @author Luis Fernando Hincapié Ospina
	 */
	public String getNombreCentroAtencion() {
		return nombreCentroAtencion;
	}

	/**
	 * Método encargado de establecer el valor del atributo
	 * nombreCentroAtencion.
	 * 
	 * @param nombreCentroAtencion
	 * @author Luis Fernando Hincapié Ospina
	 */
	public void setNombreCentroAtencion(String nombreCentroAtencion) {
		this.nombreCentroAtencion = nombreCentroAtencion;
	}

	/**
	 * Método encargado de obtener el valor del atributo login.
	 * 
	 * @return login
	 * @author Luis Fernando Hincapié Ospina
	 */
	public String getLogin() {
		return login;
	}

	/**
	 * Método encargado de establecer el valor del atributo login.
	 * 
	 * @param login
	 * @author Luis Fernando Hincapié Ospina
	 */
	public void setLogin(String login) {
		this.login = login;
	}

	/**
	 * Método encargado de obtener el valor del atributo nombreUsuario.
	 * 
	 * @return nombreUsuario
	 * @author Luis Fernando Hincapié Ospina
	 */
	public String getNombreUsuario() {
		return nombreUsuario;
	}

	/**
	 * Método encargado de establecer el valor del atributo nombreUsuario.
	 * 
	 * @param nombreUsuario
	 * @author Luis Fernando Hincapié Ospina
	 */
	public void setNombreUsuario(String nombreUsuario) {
		this.nombreUsuario = nombreUsuario;
	}

	/**
	 * Método encargado de obtener el valor del atributo ingresos.
	 * 
	 * @return ingresos
	 * @author Luis Fernando Hincapié Ospina
	 */
	public String getIngresos() {
		return ingresos;
	}

	/**
	 * Método encargado de establecer el valor del atributo ingresos.
	 * 
	 * @param ingresos
	 * @author Luis Fernando Hincapié Ospina
	 */
	public void setIngresos(String ingresos) {
		this.ingresos = ingresos;
	}

	/**
	 * Método encargado de obtener el valor del atributo valorNota.
	 * 
	 * @return valorNota
	 * @author Luis Fernando Hincapié Ospina
	 */
	public BigDecimal getValorNota() {
		return valorNota;
	}

	/**
	 * Método encargado de establecer el valor del atributo valorNota.
	 * 
	 * @param valorNota
	 * @author Luis Fernando Hincapié Ospina
	 */
	public void setValorNota(BigDecimal valorNota) {
		this.valorNota = valorNota;
	}

	/**
	 * Método encargado de obtener el valor del atributo motivoNotaDevolucion.
	 * 
	 * @return motivoNotaDevolucion
	 * @author Luis Fernando Hincapié Ospina
	 */
	public String getMotivoNotaDevolucion() {
		return motivoNotaDevolucion;
	}

	/**
	 * Método encargado de establecer el valor del atributo
	 * motivoNotaDevolucion.
	 * 
	 * @param motivoNotaDevolucion
	 * @author Luis Fernando Hincapié Ospina
	 */
	public void setMotivoNotaDevolucion(String motivoNotaDevolucion) {
		this.motivoNotaDevolucion = motivoNotaDevolucion;
	}

	/**
	 * Método encargado de obtener el valor del atributo tipoIdentificacion.
	 * 
	 * @return tipoIdentificacion
	 * @author Luis Fernando Hincapié Ospina
	 */
	public String getTipoIdentificacion() {
		return tipoIdentificacion;
	}

	/**
	 * Método encargado de establecer el valor del atributo tipoIdentificacion.
	 * 
	 * @param tipoIdentificacion
	 * @author Luis Fernando Hincapié Ospina
	 */
	public void setTipoIdentificacion(String tipoIdentificacion) {
		this.tipoIdentificacion = tipoIdentificacion;
	}

	/**
	 * Método encargado de obtener el valor del atributo numeroIdentificacion.
	 * 
	 * @return numeroIdentificacion
	 * @author Luis Fernando Hincapié Ospina
	 */
	public String getNumeroIdentificacion() {
		return numeroIdentificacion;
	}

	/**
	 * Método encargado de establecer el valor del atributo numeroIdentificacion.
	 * 
	 * @param numeroIdentificacion
	 * @author Luis Fernando Hincapié Ospina
	 */
	public void setNumeroIdentificacion(String numeroIdentificacion) {
		this.numeroIdentificacion = numeroIdentificacion;
	}

	/**
	 * Método encargado de obtener el valor del atributo primerNombrePaciente.
	 * 
	 * @return primerNombrePaciente
	 * @author Luis Fernando Hincapié Ospina
	 */
	public String getPrimerNombrePaciente() {
		return primerNombrePaciente;
	}

	/**
	 * Método encargado de establecer el valor del atributo primerNombrePaciente.
	 * 
	 * @param primerNombrePaciente
	 * @author Luis Fernando Hincapié Ospina
	 */
	public void setPrimerNombrePaciente(String primerNombrePaciente) {
		this.primerNombrePaciente = primerNombrePaciente;
	}

	/**
	 * Método encargado de obtener el valor del atributo segundoNombrePaciente.
	 * 
	 * @return segundoNombrePaciente
	 * @author Luis Fernando Hincapié Ospina
	 */
	public String getSegundoNombrePaciente() {
		return segundoNombrePaciente;
	}

	/**
	 * Método encargado de establecer el valor del atributo segundoNombrePaciente.
	 * 
	 * @param segundoNombrePaciente
	 * @author Luis Fernando Hincapié Ospina
	 */
	public void setSegundoNombrePaciente(String segundoNombrePaciente) {
		this.segundoNombrePaciente = segundoNombrePaciente;
	}

	/**
	 * Método encargado de obtener el valor del atributo primerApellidoPaciente.
	 * 
	 * @return primerApellidoPaciente
	 * @author Luis Fernando Hincapié Ospina
	 */
	public String getPrimerApellidoPaciente() {
		return primerApellidoPaciente;
	}

	/**
	 * Método encargado de establecer el valor del atributo primerApellidoPaciente.
	 * 
	 * @param primerApellidoPaciente
	 * @author Luis Fernando Hincapié Ospina
	 */
	public void setPrimerApellidoPaciente(String primerApellidoPaciente) {
		this.primerApellidoPaciente = primerApellidoPaciente;
	}

	/**
	 * Método encargado de obtener el valor del atributo segundoApellidoPaciente.
	 * 
	 * @return segundoApellidoPaciente
	 * @author Luis Fernando Hincapié Ospina
	 */
	public String getSegundoApellidoPaciente() {
		return segundoApellidoPaciente;
	}

	/**
	 * Método encargado de establecer el valor del atributo segundoApellidoPaciente.
	 * 
	 * @param segundoApellidoPaciente
	 * @author Luis Fernando Hincapié Ospina
	 */
	public void setSegundoApellidoPaciente(String segundoApellidoPaciente) {
		this.segundoApellidoPaciente = segundoApellidoPaciente;
	}

	/**
	 * Método encargado de obtener el valor del atributo multiempresa.
	 * 
	 * @return multiempresa
	 * @author Luis Fernando Hincapié Ospina
	 */
	public boolean isMultiempresa() {
		return multiempresa;
	}

	/**
	 * Método encargado de establecer el valor del atributo multiempresa.
	 * 
	 * @param multiempresa
	 * @author Luis Fernando Hincapié Ospina
	 */
	public void setMultiempresa(boolean multiempresa) {
		this.multiempresa = multiempresa;
	}

}
