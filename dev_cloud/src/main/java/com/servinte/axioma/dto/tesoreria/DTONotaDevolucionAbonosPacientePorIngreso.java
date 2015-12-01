package com.servinte.axioma.dto.tesoreria;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

/**
 * Esta clase almacena los registros de notas de devoluci�n de abonos de un
 * paciente cuando controla abono de pacientes por n�mero de ingreso.
 * 
 * @author Luis Fernando Hincapi� Ospina
 * @since 09/03/2011
 */
public class DTONotaDevolucionAbonosPacientePorIngreso implements Serializable {

	private static final long serialVersionUID = 1L;
	private BigDecimal numeroNotaDevolucion;
	private Date fechaNotaDevolucion;
	private String horaNotaDevolucion;
	private String nombreCompletoPaciente;
	private String tipoIdentificacion;
	private String numeroIdentificacion;
	private String login;
	private String nombreUsuario;
	private ArrayList<DTODevolucionPacienteIngreso> listaDevolucionPacienteIngreso;
	private String motivoNotaDevolucion;
	private BigDecimal valorTotalNotaDevolucion;

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
	 * M�todo encargado de obtener el valor del atributo
	 * listaDevolucionPacienteIngreso.
	 * 
	 * @return listaDevolucionPacienteIngreso
	 * @author Luis Fernando Hincapi� Ospina
	 */
	public ArrayList<DTODevolucionPacienteIngreso> getListaDevolucionPacienteIngreso() {
		return listaDevolucionPacienteIngreso;
	}

	/**
	 * M�todo encargado de establecer el valor del atributo
	 * listaDevolucionPacienteIngreso.
	 * 
	 * @param listaDevolucionPacienteIngreso
	 * @author Luis Fernando Hincapi� Ospina
	 */
	public void setListaDevolucionPacienteIngreso(
			ArrayList<DTODevolucionPacienteIngreso> listaDevolucionPacienteIngreso) {
		this.listaDevolucionPacienteIngreso = listaDevolucionPacienteIngreso;
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
	 * M�todo encargado de obtener el valor del atributo
	 * valorTotalNotaDevolucion.
	 * 
	 * @return valorTotalNotaDevolucion
	 * @author Luis Fernando Hincapi� Ospina
	 */
	public BigDecimal getValorTotalNotaDevolucion() {
		return valorTotalNotaDevolucion;
	}

	/**
	 * M�todo encargado de establecer el valor del atributo
	 * valorTotalNotaDevolucion.
	 * 
	 * @param valorTotalNotaDevolucion
	 * @author Luis Fernando Hincapi� Ospina
	 */
	public void setValorTotalNotaDevolucion(BigDecimal valorTotalNotaDevolucion) {
		this.valorTotalNotaDevolucion = valorTotalNotaDevolucion;
	}

}
