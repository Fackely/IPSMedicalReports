/*
 * @(#)Admision.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */

package com.princetonsa.mundo;

import java.sql.Connection;

import util.ConstantesBD;

/**
 * Esta clase abstracta define los atributos y métodos comunes a Admisión por Hospitalización y Admisión por Urgencias.
 * También, estblece el contrato de operaciones que deben implementar sus subclases.
 *
 * @version 1.0, Feb 28, 2003
 * @author <a href="mailto:Oscar@PrincetonSA.com">&Oacute;scar Andr&eacute;s L&oacute;pez P.</a>, <a href="mailto:Camilo@PrincetonSA.com">Camilo Camacho</a>
 */

public abstract class Admision implements AccesoBD {

	/**
	 * Código de la admisión.
	 */
	private int codigo;

	/**
	 * Número de identificación de la cuenta asociada a esta admisión.
	 */
	private int idCuenta;

	/**
	 * Fecha en la que se efectúa esta admisión, en formato yyyy-MM-dd.
	 */
	private String fecha;

	/**
	 * Hora en la que se efectúa esta admisión, formato HH:mm.
	 */
	private String hora;

	/**
	 * Nombre del origen de esta admisión.
	 */
	private String origen;

	/**
	 * Código del origen de esta admisión.
	 */
	private int codigoOrigen;

	/**
	 * Nombre de la causa externa de esta admisión.
	 */
	private String causaExterna;

	/**
	 * Código de la causa externa de esta admisión.
	 */
	private int codigoCausaExterna;

	/**
	 * Login del usuario que está efectuando esta admisión.
	 */
	private String loginUsuario;

	/**
	 * Número de autorización de esta admisión.
	 */
	//private String numeroAutorizacion;

	/**
	 * Datos básicos del médico que efectúa esta admisión.
	 */
	private PersonaBasica medico;

	/**
	 * Retorna el nombre de la causa externa de esta admisión.
	 * @return el nombre de la causa externa de esta admisión
	 */
	final public String getCausaExterna() {
		return causaExterna;
	}

	/**
	 * Retorna el código de esta admisión.
	 * @return el código de esta admisión
	 */
	final public int getCodigo() {
		return codigo;
	}

	/**
	 * Retorna el código de la causa externa de esta admisión.
	 * @return el código de la causa externa de esta admisión
	 */
	final public int getCodigoCausaExterna() {
		return codigoCausaExterna;
	}

	/**
	 * Retorna el código del origen de esta admisión.
	 * @return el código del origen de esta admisión
	 */
	final public int getCodigoOrigen() {
		return codigoOrigen;
	}

	/**
	 * Retorna la fecha en que se efectuó esta admisión.
	 * @return la fecha en que se efectuó esta admisión
	 */
	final public String getFecha() {
		return fecha;
	}

	/**
	 * Retorna la hora en que se efectuó esta admisión.
	 * @return la hora en que se efectuó esta admisión
	 */
	final public String getHora() {
		return hora;
	}

	/**
	 * Retorna el número de identificación de la cuenta asociada a esta admisión.
	 * @return el número de identificación de la cuenta asociada a esta admisión
	 */
	final public int getIdCuenta() {
		return idCuenta;
	}

	/**
	 * Retorna el login del usuario que efectúa esta admisión.
	 * @return el login del usuario que efectúa esta admisión
	 */
	final public String getLoginUsuario() {
		return loginUsuario;
	}

	/**
	 * Retorna el objeto <code>PersonaBasica</code> medico con los datos del
	 * médico.
	 * @return el objeto con los datos del médico
	 */
	final public PersonaBasica getMedico() {
		return this.medico;
	}

	/**
	 * Retorna el número de autorizacion de esta admisión.
	 * @return el número de autorizacion de esta admisión
	 */
	/*
	final public String getNumeroAutorizacion() {
		return numeroAutorizacion;
	}
	*/
	/**
	 * Retorna el nombre del origen de esta admisión.
	 * @return el nombre del origen de esta admisión
	 */
	final public String getOrigen() {
		return origen;
	}

	/**
	 * Establece la causa externa.
	 * @param causaExterna la causa externa a establecer
	 */
	final public void setCausaExterna(String causaExterna) {
		this.causaExterna = (causaExterna != null) ? causaExterna.trim() : null;
	}

	/**
	 * Establece el código.
	 * @param codigo el código a establecer
	 */
	final public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	/**
	 * Establece el código.
	 * @param codigo el código a establecer
	 */
	final public void setCodigo(String codigo) {
		this.codigo = Integer.parseInt(codigo);
	}

	/**
	 * Establece el código de la causa externa.
	 * @param codigoCausaExterna el código de la causa externa a establecer
	 */
	final public void setCodigoCausaExterna(int codigoCausaExterna) {
		this.codigoCausaExterna = codigoCausaExterna;
	}

	/**
	 * Establece el código de la causa externa.
	 * @param codigoCausaExterna el código de la causa externa a establecer
	 */
	final public void setCodigoCausaExterna(String codigoCausaExterna) {
		this.codigoCausaExterna = Integer.parseInt(codigoCausaExterna);
	}

	/**
	 * Establece el código de origen.
	 * @param codigoOrigen el código de origen a establecer
	 */
	final public void setCodigoOrigen(int codigoOrigen) {
		this.codigoOrigen = codigoOrigen;
	}

	/**
	 * Establece el código de origen.
	 * @param codigoOrigen el código de origen a establecer
	 */
	final public void setCodigoOrigen(String codigoOrigen) {
		this.codigoOrigen = Integer.parseInt(codigoOrigen);
	}

	/**
	 * Establece la fecha.
	 * @param fecha la fecha a establecer
	 */
	final public void setFecha(String fecha) {
		this.fecha = (fecha != null) ? fecha.trim() : null;
	}

	/**
	 * Establece la hora.
	 * @param hora la hora a establecer
	 */
	final public void setHora(String hora) {
		this.hora = (hora != null) ? hora.trim() : null;
	}

	/**
	 * Establece el número de identificación de la cuenta.
	 * @param idCuenta el número de identificación de la cuenta a establecer
	 */
	final public void setIdCuenta(int idCuenta) {
		this.idCuenta = idCuenta;
	}

	/**
	 * Establece el número de identificación de la cuenta.
	 * @param idCuenta el número de identificación de la cuenta a establecer
	 */
	final public void setIdCuenta(String idCuenta) {
		this.idCuenta = Integer.parseInt(idCuenta);
	}

	/**
	 * Establece el login del usuario.
	 * @param loginUsuario el login del usuario a establecer
	 */
	final public void setLoginUsuario(UsuarioBasico usuario) {
		this.loginUsuario = (usuario.getLoginUsuario() != null) ? usuario.getLoginUsuario().trim() : null;
	}

	/**
	 * Establece el médico.
	 * @param medico el médico a establecer
	 */
	final public void setMedico(PersonaBasica medico) {
		this.medico = medico;
	}

	/**
	 * Establece el número de autorización.
	 * @param numeroAutorizacion el número de autorización a establecer
	 */
	/*
	final public void setNumeroAutorizacion(String numeroAutorizacion) {
		this.numeroAutorizacion = (numeroAutorizacion != null) ? numeroAutorizacion.trim() : null;
	}
	*/
	/**
	 * Establece el origen.
	 * @param origen el origen a establecer
	 */
	final public void setOrigen(String origen) {
		this.origen = (origen != null) ? origen.trim() : null;
	}

	/**
	 * Este método inicializa en valores vacíos (mas no nulos) los atributos de la admisión.
	 */
	public void clean() {

		codigo = 0;
		idCuenta = 0;
		fecha = "";
		hora = "";
		origen = "";
		codigoOrigen = 0;
		causaExterna = "";
		codigoCausaExterna = 0;
		loginUsuario = "";
		//numeroAutorizacion = "";
		medico = new PersonaBasica();

	}
	
	/**
	 * Mètodo que obtiene la información de la cama actual del paciente
	 * @param con
	 * @param codigoAdmision
	 * @param tipoAdmision
	 * @param codigoPaciente
	 * @return
	 */
	public static String getCama(Connection con, int codigoAdmision, int tipoAdmision, int codigoPaciente)
	{
		String camas[]=getCamaCompleta (con, codigoAdmision, tipoAdmision, codigoPaciente);
		if(camas!=null && camas.length!=0)
		{
			return camas[0];
		}
		else
		{
			return "";
		}
		
	}
	
	/**
	 * Método que obtiene la información de la cama actual del paciente
	 * @param con
	 * @param codigoAdmision
	 * @param tipoAdmision
	 * @param codigoPaciente
	 * @return
	 */
	public static String[] getCamaCompleta (Connection con, int codigoAdmision, int tipoAdmision, int codigoPaciente)
	{
		if(tipoAdmision == ConstantesBD.codigoViaIngresoUrgencias)
		{
			return AdmisionUrgencias.getCama(con, codigoAdmision);
		}
		else if(tipoAdmision == ConstantesBD.codigoViaIngresoHospitalizacion)
		{
			return AdmisionHospitalaria.getCama(con, codigoAdmision, codigoPaciente);
		}
		String[] a={"-1","-1","-1"};
		return a;
	}
	
}