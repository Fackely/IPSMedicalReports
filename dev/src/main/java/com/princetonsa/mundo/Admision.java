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
 * Esta clase abstracta define los atributos y m�todos comunes a Admisi�n por Hospitalizaci�n y Admisi�n por Urgencias.
 * Tambi�n, estblece el contrato de operaciones que deben implementar sus subclases.
 *
 * @version 1.0, Feb 28, 2003
 * @author <a href="mailto:Oscar@PrincetonSA.com">&Oacute;scar Andr&eacute;s L&oacute;pez P.</a>, <a href="mailto:Camilo@PrincetonSA.com">Camilo Camacho</a>
 */

public abstract class Admision implements AccesoBD {

	/**
	 * C�digo de la admisi�n.
	 */
	private int codigo;

	/**
	 * N�mero de identificaci�n de la cuenta asociada a esta admisi�n.
	 */
	private int idCuenta;

	/**
	 * Fecha en la que se efect�a esta admisi�n, en formato yyyy-MM-dd.
	 */
	private String fecha;

	/**
	 * Hora en la que se efect�a esta admisi�n, formato HH:mm.
	 */
	private String hora;

	/**
	 * Nombre del origen de esta admisi�n.
	 */
	private String origen;

	/**
	 * C�digo del origen de esta admisi�n.
	 */
	private int codigoOrigen;

	/**
	 * Nombre de la causa externa de esta admisi�n.
	 */
	private String causaExterna;

	/**
	 * C�digo de la causa externa de esta admisi�n.
	 */
	private int codigoCausaExterna;

	/**
	 * Login del usuario que est� efectuando esta admisi�n.
	 */
	private String loginUsuario;

	/**
	 * N�mero de autorizaci�n de esta admisi�n.
	 */
	//private String numeroAutorizacion;

	/**
	 * Datos b�sicos del m�dico que efect�a esta admisi�n.
	 */
	private PersonaBasica medico;

	/**
	 * Retorna el nombre de la causa externa de esta admisi�n.
	 * @return el nombre de la causa externa de esta admisi�n
	 */
	final public String getCausaExterna() {
		return causaExterna;
	}

	/**
	 * Retorna el c�digo de esta admisi�n.
	 * @return el c�digo de esta admisi�n
	 */
	final public int getCodigo() {
		return codigo;
	}

	/**
	 * Retorna el c�digo de la causa externa de esta admisi�n.
	 * @return el c�digo de la causa externa de esta admisi�n
	 */
	final public int getCodigoCausaExterna() {
		return codigoCausaExterna;
	}

	/**
	 * Retorna el c�digo del origen de esta admisi�n.
	 * @return el c�digo del origen de esta admisi�n
	 */
	final public int getCodigoOrigen() {
		return codigoOrigen;
	}

	/**
	 * Retorna la fecha en que se efectu� esta admisi�n.
	 * @return la fecha en que se efectu� esta admisi�n
	 */
	final public String getFecha() {
		return fecha;
	}

	/**
	 * Retorna la hora en que se efectu� esta admisi�n.
	 * @return la hora en que se efectu� esta admisi�n
	 */
	final public String getHora() {
		return hora;
	}

	/**
	 * Retorna el n�mero de identificaci�n de la cuenta asociada a esta admisi�n.
	 * @return el n�mero de identificaci�n de la cuenta asociada a esta admisi�n
	 */
	final public int getIdCuenta() {
		return idCuenta;
	}

	/**
	 * Retorna el login del usuario que efect�a esta admisi�n.
	 * @return el login del usuario que efect�a esta admisi�n
	 */
	final public String getLoginUsuario() {
		return loginUsuario;
	}

	/**
	 * Retorna el objeto <code>PersonaBasica</code> medico con los datos del
	 * m�dico.
	 * @return el objeto con los datos del m�dico
	 */
	final public PersonaBasica getMedico() {
		return this.medico;
	}

	/**
	 * Retorna el n�mero de autorizacion de esta admisi�n.
	 * @return el n�mero de autorizacion de esta admisi�n
	 */
	/*
	final public String getNumeroAutorizacion() {
		return numeroAutorizacion;
	}
	*/
	/**
	 * Retorna el nombre del origen de esta admisi�n.
	 * @return el nombre del origen de esta admisi�n
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
	 * Establece el c�digo.
	 * @param codigo el c�digo a establecer
	 */
	final public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	/**
	 * Establece el c�digo.
	 * @param codigo el c�digo a establecer
	 */
	final public void setCodigo(String codigo) {
		this.codigo = Integer.parseInt(codigo);
	}

	/**
	 * Establece el c�digo de la causa externa.
	 * @param codigoCausaExterna el c�digo de la causa externa a establecer
	 */
	final public void setCodigoCausaExterna(int codigoCausaExterna) {
		this.codigoCausaExterna = codigoCausaExterna;
	}

	/**
	 * Establece el c�digo de la causa externa.
	 * @param codigoCausaExterna el c�digo de la causa externa a establecer
	 */
	final public void setCodigoCausaExterna(String codigoCausaExterna) {
		this.codigoCausaExterna = Integer.parseInt(codigoCausaExterna);
	}

	/**
	 * Establece el c�digo de origen.
	 * @param codigoOrigen el c�digo de origen a establecer
	 */
	final public void setCodigoOrigen(int codigoOrigen) {
		this.codigoOrigen = codigoOrigen;
	}

	/**
	 * Establece el c�digo de origen.
	 * @param codigoOrigen el c�digo de origen a establecer
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
	 * Establece el n�mero de identificaci�n de la cuenta.
	 * @param idCuenta el n�mero de identificaci�n de la cuenta a establecer
	 */
	final public void setIdCuenta(int idCuenta) {
		this.idCuenta = idCuenta;
	}

	/**
	 * Establece el n�mero de identificaci�n de la cuenta.
	 * @param idCuenta el n�mero de identificaci�n de la cuenta a establecer
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
	 * Establece el m�dico.
	 * @param medico el m�dico a establecer
	 */
	final public void setMedico(PersonaBasica medico) {
		this.medico = medico;
	}

	/**
	 * Establece el n�mero de autorizaci�n.
	 * @param numeroAutorizacion el n�mero de autorizaci�n a establecer
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
	 * Este m�todo inicializa en valores vac�os (mas no nulos) los atributos de la admisi�n.
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
	 * M�todo que obtiene la informaci�n de la cama actual del paciente
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
	 * M�todo que obtiene la informaci�n de la cama actual del paciente
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