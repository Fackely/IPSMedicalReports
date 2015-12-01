package com.servinte.axioma.dto.tesoreria;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Esta clase almacena los registros de notas devoluci�n abonos paciente por
 * rango.
 * 
 * @author Luis Fernando Hincapi� Ospina
 * @since 07/03/2011
 */
public class DTONotaDevolucionAbonosPacienteRango implements Serializable {

	private static final long serialVersionUID = 1L;
	private BigDecimal numeroNotaDevolucion;
	private Date fechaNotaDevolucion;
	private String horaNotaDevolucion;
	private String nombreCentroAtencion;
	private String nombreCompletoPaciente;
	private String primerNombrePaciente;
	private String segundoNombrePaciente;
	private String primerApellidoPaciente;
	private String segundoApellidoPaciente;
	private String tipoIdentificacion;
	private String numeroIdentificacion;
	private String ingreso;
	private String primerNombreUsuario;
	private String primerApellidoUsuario;
	private String nombreUsuario;
	private String login;
	private BigDecimal valorNotaDevolucion;

	/**
	 * M�todo encargado de obtener el valor del atributo numeroNotaDevolucion.
	 * 
	 * @return numeroNotaDevolucion
	 * @author Luis Fernando Hincapi� Ospina
	 */
	public BigDecimal getNumeroNotaDevolucion() {
		return numeroNotaDevolucion;
	}

	/**
	 * M�todo encargado de establecer el valor del atributo
	 * numeroNotaDevolucion.
	 * 
	 * @param numeroNotaDevolucion
	 * @author Luis Fernando Hincapi� Ospina
	 */
	public void setNumeroNotaDevolucion(BigDecimal numeroNotaDevolucion) {
		this.numeroNotaDevolucion = numeroNotaDevolucion;
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
	 * M�todo encargado de obtener el valor del atributo nombreCentroAtencion.
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
	 * M�todo encargado de obtener el valor del atributo nombreCompletoPaciente.
	 * 
	 * @return nombreCompletoPaciente
	 * @author Luis Fernando Hincapi� Ospina
	 */
	public String getNombreCompletoPaciente() {
		return nombreCompletoPaciente;
	}

	/**
	 * M�todo encargado de establecer el valor del atributo
	 * nombreCompletoPaciente.
	 * 
	 * @param nombreCompletoPaciente
	 * @author Luis Fernando Hincapi� Ospina
	 */
	public void setNombreCompletoPaciente(String nombreCompletoPaciente) {
		this.nombreCompletoPaciente = nombreCompletoPaciente;
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
	 * M�todo encargado de establecer el valor del atributo
	 * primerNombrePaciente.
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
	 * M�todo encargado de establecer el valor del atributo
	 * segundoNombrePaciente.
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
	 * M�todo encargado de establecer el valor del atributo
	 * primerApellidoPaciente.
	 * 
	 * @param primerApellidoPaciente
	 * @author Luis Fernando Hincapi� Ospina
	 */
	public void setPrimerApellidoPaciente(String primerApellidoPaciente) {
		this.primerApellidoPaciente = primerApellidoPaciente;
	}

	/**
	 * M�todo encargado de obtener el valor del atributo
	 * segundoApellidoPaciente.
	 * 
	 * @return segundoApellidoPaciente
	 * @author Luis Fernando Hincapi� Ospina
	 */
	public String getSegundoApellidoPaciente() {
		return segundoApellidoPaciente;
	}

	/**
	 * M�todo encargado de establecer el valor del atributo
	 * segundoApellidoPaciente.
	 * 
	 * @param segundoApellidoPaciente
	 * @author Luis Fernando Hincapi� Ospina
	 */
	public void setSegundoApellidoPaciente(String segundoApellidoPaciente) {
		this.segundoApellidoPaciente = segundoApellidoPaciente;
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
	 * M�todo encargado de establecer el valor del atributo
	 * numeroIdentificacion.
	 * 
	 * @param numeroIdentificacion
	 * @author Luis Fernando Hincapi� Ospina
	 */
	public void setNumeroIdentificacion(String numeroIdentificacion) {
		this.numeroIdentificacion = numeroIdentificacion;
	}

	/**
	 * M�todo encargado de obtener el valor del atributo ingreso.
	 * 
	 * @return ingreso
	 * @author Luis Fernando Hincapi� Ospina
	 */
	public String getIngreso() {
		return ingreso;
	}

	/**
	 * M�todo encargado de establecer el valor del atributo ingreso.
	 * 
	 * @param ingreso
	 * @author Luis Fernando Hincapi� Ospina
	 */
	public void setIngreso(String ingreso) {
		this.ingreso = ingreso;
	}

	/**
	 * M�todo encargado de obtener el valor del atributo primerNombreUsuario.
	 * 
	 * @return primerNombreUsuario
	 * @author Luis Fernando Hincapi� Ospina
	 */
	public String getPrimerNombreUsuario() {
		return primerNombreUsuario;
	}

	/**
	 * M�todo encargado de establecer el valor del atributo primerNombreUsuario.
	 * 
	 * @param primerNombreUsuario
	 * @author Luis Fernando Hincapi� Ospina
	 */
	public void setPrimerNombreUsuario(String primerNombreUsuario) {
		this.primerNombreUsuario = primerNombreUsuario;
	}

	/**
	 * M�todo encargado de obtener el valor del atributo primerApellidoUsuario.
	 * 
	 * @return primerApellidoUsuario
	 * @author Luis Fernando Hincapi� Ospina
	 */
	public String getPrimerApellidoUsuario() {
		return primerApellidoUsuario;
	}

	/**
	 * M�todo encargado de establecer el valor del atributo
	 * primerApellidoUsuario.
	 * 
	 * @param primerApellidoUsuario
	 * @author Luis Fernando Hincapi� Ospina
	 */
	public void setPrimerApellidoUsuario(String primerApellidoUsuario) {
		this.primerApellidoUsuario = primerApellidoUsuario;
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
	 * M�todo encargado de obtener el valor del atributo valorNotaDevolucion.
	 * 
	 * @return valorNotaDevolucion
	 * @author Luis Fernando Hincapi� Ospina
	 */
	public BigDecimal getValorNotaDevolucion() {
		return valorNotaDevolucion;
	}

	/**
	 * M�todo encargado de establecer el valor del atributo valorNotaDevolucion.
	 * 
	 * @param valorNotaDevolucion
	 * @author Luis Fernando Hincapi� Ospina
	 */
	public void setValorNotaDevolucion(BigDecimal valorNotaDevolucion) {
		this.valorNotaDevolucion = valorNotaDevolucion;
	}

}
