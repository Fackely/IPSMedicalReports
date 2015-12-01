package com.servinte.axioma.dto.tesoreria;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

/**
 * Esta clase almacena los registros de notas de devolución de abonos de un
 * paciente cuando controla abono de pacientes por número de ingreso.
 * 
 * @author Luis Fernando Hincapié Ospina
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
	 * Método encargado de obtener el valor del atributo
	 * listaDevolucionPacienteIngreso.
	 * 
	 * @return listaDevolucionPacienteIngreso
	 * @author Luis Fernando Hincapié Ospina
	 */
	public ArrayList<DTODevolucionPacienteIngreso> getListaDevolucionPacienteIngreso() {
		return listaDevolucionPacienteIngreso;
	}

	/**
	 * Método encargado de establecer el valor del atributo
	 * listaDevolucionPacienteIngreso.
	 * 
	 * @param listaDevolucionPacienteIngreso
	 * @author Luis Fernando Hincapié Ospina
	 */
	public void setListaDevolucionPacienteIngreso(
			ArrayList<DTODevolucionPacienteIngreso> listaDevolucionPacienteIngreso) {
		this.listaDevolucionPacienteIngreso = listaDevolucionPacienteIngreso;
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
	 * Método encargado de obtener el valor del atributo
	 * valorTotalNotaDevolucion.
	 * 
	 * @return valorTotalNotaDevolucion
	 * @author Luis Fernando Hincapié Ospina
	 */
	public BigDecimal getValorTotalNotaDevolucion() {
		return valorTotalNotaDevolucion;
	}

	/**
	 * Método encargado de establecer el valor del atributo
	 * valorTotalNotaDevolucion.
	 * 
	 * @param valorTotalNotaDevolucion
	 * @author Luis Fernando Hincapié Ospina
	 */
	public void setValorTotalNotaDevolucion(BigDecimal valorTotalNotaDevolucion) {
		this.valorTotalNotaDevolucion = valorTotalNotaDevolucion;
	}

}
