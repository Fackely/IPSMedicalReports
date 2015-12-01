package com.servinte.axioma.dto.tesoreria;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Esta clase almacena los registros de notas devolución abonos paciente por
 * rango.
 * 
 * @author Luis Fernando Hincapié Ospina
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
	 * Método encargado de obtener el valor del atributo numeroNotaDevolucion.
	 * 
	 * @return numeroNotaDevolucion
	 * @author Luis Fernando Hincapié Ospina
	 */
	public BigDecimal getNumeroNotaDevolucion() {
		return numeroNotaDevolucion;
	}

	/**
	 * Método encargado de establecer el valor del atributo
	 * numeroNotaDevolucion.
	 * 
	 * @param numeroNotaDevolucion
	 * @author Luis Fernando Hincapié Ospina
	 */
	public void setNumeroNotaDevolucion(BigDecimal numeroNotaDevolucion) {
		this.numeroNotaDevolucion = numeroNotaDevolucion;
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
	 * Método encargado de obtener el valor del atributo nombreCentroAtencion.
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
	 * Método encargado de obtener el valor del atributo nombreCompletoPaciente.
	 * 
	 * @return nombreCompletoPaciente
	 * @author Luis Fernando Hincapié Ospina
	 */
	public String getNombreCompletoPaciente() {
		return nombreCompletoPaciente;
	}

	/**
	 * Método encargado de establecer el valor del atributo
	 * nombreCompletoPaciente.
	 * 
	 * @param nombreCompletoPaciente
	 * @author Luis Fernando Hincapié Ospina
	 */
	public void setNombreCompletoPaciente(String nombreCompletoPaciente) {
		this.nombreCompletoPaciente = nombreCompletoPaciente;
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
	 * Método encargado de establecer el valor del atributo
	 * primerNombrePaciente.
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
	 * Método encargado de establecer el valor del atributo
	 * segundoNombrePaciente.
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
	 * Método encargado de establecer el valor del atributo
	 * primerApellidoPaciente.
	 * 
	 * @param primerApellidoPaciente
	 * @author Luis Fernando Hincapié Ospina
	 */
	public void setPrimerApellidoPaciente(String primerApellidoPaciente) {
		this.primerApellidoPaciente = primerApellidoPaciente;
	}

	/**
	 * Método encargado de obtener el valor del atributo
	 * segundoApellidoPaciente.
	 * 
	 * @return segundoApellidoPaciente
	 * @author Luis Fernando Hincapié Ospina
	 */
	public String getSegundoApellidoPaciente() {
		return segundoApellidoPaciente;
	}

	/**
	 * Método encargado de establecer el valor del atributo
	 * segundoApellidoPaciente.
	 * 
	 * @param segundoApellidoPaciente
	 * @author Luis Fernando Hincapié Ospina
	 */
	public void setSegundoApellidoPaciente(String segundoApellidoPaciente) {
		this.segundoApellidoPaciente = segundoApellidoPaciente;
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
	 * Método encargado de establecer el valor del atributo
	 * numeroIdentificacion.
	 * 
	 * @param numeroIdentificacion
	 * @author Luis Fernando Hincapié Ospina
	 */
	public void setNumeroIdentificacion(String numeroIdentificacion) {
		this.numeroIdentificacion = numeroIdentificacion;
	}

	/**
	 * Método encargado de obtener el valor del atributo ingreso.
	 * 
	 * @return ingreso
	 * @author Luis Fernando Hincapié Ospina
	 */
	public String getIngreso() {
		return ingreso;
	}

	/**
	 * Método encargado de establecer el valor del atributo ingreso.
	 * 
	 * @param ingreso
	 * @author Luis Fernando Hincapié Ospina
	 */
	public void setIngreso(String ingreso) {
		this.ingreso = ingreso;
	}

	/**
	 * Método encargado de obtener el valor del atributo primerNombreUsuario.
	 * 
	 * @return primerNombreUsuario
	 * @author Luis Fernando Hincapié Ospina
	 */
	public String getPrimerNombreUsuario() {
		return primerNombreUsuario;
	}

	/**
	 * Método encargado de establecer el valor del atributo primerNombreUsuario.
	 * 
	 * @param primerNombreUsuario
	 * @author Luis Fernando Hincapié Ospina
	 */
	public void setPrimerNombreUsuario(String primerNombreUsuario) {
		this.primerNombreUsuario = primerNombreUsuario;
	}

	/**
	 * Método encargado de obtener el valor del atributo primerApellidoUsuario.
	 * 
	 * @return primerApellidoUsuario
	 * @author Luis Fernando Hincapié Ospina
	 */
	public String getPrimerApellidoUsuario() {
		return primerApellidoUsuario;
	}

	/**
	 * Método encargado de establecer el valor del atributo
	 * primerApellidoUsuario.
	 * 
	 * @param primerApellidoUsuario
	 * @author Luis Fernando Hincapié Ospina
	 */
	public void setPrimerApellidoUsuario(String primerApellidoUsuario) {
		this.primerApellidoUsuario = primerApellidoUsuario;
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
	 * Método encargado de obtener el valor del atributo valorNotaDevolucion.
	 * 
	 * @return valorNotaDevolucion
	 * @author Luis Fernando Hincapié Ospina
	 */
	public BigDecimal getValorNotaDevolucion() {
		return valorNotaDevolucion;
	}

	/**
	 * Método encargado de establecer el valor del atributo valorNotaDevolucion.
	 * 
	 * @param valorNotaDevolucion
	 * @author Luis Fernando Hincapié Ospina
	 */
	public void setValorNotaDevolucion(BigDecimal valorNotaDevolucion) {
		this.valorNotaDevolucion = valorNotaDevolucion;
	}

}
