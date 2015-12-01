package com.princetonsa.dto.tesoreria;

import java.io.Serializable;

import util.UtilidadTexto;
import util.Utilidades;

/**
 * @author Cristhian Murillo
 */
public class DtoUsuarioPersona implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String login;
	private String nombre;
	private String apellido;
	private String segundoNombre;
	private String segundoApellido;
	private String acronimoTipoID;
	private String numeroID;
	private String nombreOrganizado;
	private String nombreOrganizadoLogin;
	private Integer codigo;
	
	
	/**
	 * Constructor de la clase
	 */
	public DtoUsuarioPersona(){
		this.login 				= "";
		this.nombre 			= "";
		this.apellido 			= "";
		this.segundoNombre 		= "";
		this.segundoApellido 	= "";
		this.acronimoTipoID		= "";
		this.numeroID			= "";
		this.nombreOrganizado  	= "";
		this.nombreOrganizadoLogin	= "";
		this.codigo 			= null;
	}

	
	/**
	 * Retorna el nombre organizado validando los campos que sean nulos.
	 * El formato entregado es: primerApellido segundoApellido primerNombre segundoNombre 
	 * @return nombreOrganizado
	 * @autor Cristhian Murillo
	 */
	public String obtenerNombreOrganizado()
	{
		this.nombreOrganizado = "";
		
		this.nombreOrganizado += this.apellido +" ";
		
		if(!UtilidadTexto.isEmpty(this.segundoApellido)){
			this.nombreOrganizado += this.segundoApellido +" ";
		}
		
		this.nombreOrganizado += this.nombre +" ";
		
		if(!UtilidadTexto.isEmpty(this.segundoNombre)){
			this.nombreOrganizado += this.segundoNombre +" ";
		}
		
		return this.nombreOrganizado;
	}
	
	
	/**
	 * Retorna el nombre organizado y el login validando los campos que sean nulos.
	 * El formato entregado es: primerApellido segundoApellido primerNombre segundoNombre (login)
	 * @return nombreOrganizadoLogin
	 * @autor Cristhian Murillo
	 */
	public String obtenerNombreOrganizadoLogin()
	{
		this.nombreOrganizadoLogin = Utilidades.obtenerNombreLoginOrganizado(this.nombre, this.segundoNombre, this.apellido, this.segundoApellido, this.login);
		
		return this.nombreOrganizadoLogin;
	}
	
	/**
	 * Retorna el nombre organizado y el login validando los campos que sean nulos.
	 * El formato entregado es: primerNombre primerApellido (login)
	 * @return nombreOrganizadoLogin
	 * @autor diecorqu
	 */
	public String obtenerNombreApellidoLogin()
	{
		this.nombreOrganizadoLogin = (!UtilidadTexto.isEmpty(this.nombre)) ? this.nombre + " " : ""; 
		this.nombreOrganizadoLogin += (!UtilidadTexto.isEmpty(this.nombre)) ? this.apellido + " " : "";
		this.nombreOrganizadoLogin += (!UtilidadTexto.isEmpty(this.login)) ? "(" + this.login + ")" : "";
		return this.nombreOrganizadoLogin;
	}	
	
	

	/**
	 * @return valor de login
	 */
	public String getLogin() {
		return login;
	}


	/**
	 * @param login el login para asignar
	 */
	public void setLogin(String login) {
		this.login = login;
	}


	/**
	 * @return valor de nombre
	 */
	public String getNombre() {
		return nombre;
	}


	/**
	 * @param nombre el nombre para asignar
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}


	/**
	 * @return valor de apellido
	 */
	public String getApellido() {
		return apellido;
	}


	/**
	 * @param apellido el apellido para asignar
	 */
	public void setApellido(String apellido) {
		this.apellido = apellido;
	}


	/**
	 * @return valor de segundoNombre
	 */
	public String getSegundoNombre() {
		return segundoNombre;
	}


	/**
	 * @param segundoNombre el segundoNombre para asignar
	 */
	public void setSegundoNombre(String segundoNombre) {
		this.segundoNombre = segundoNombre;
	}


	/**
	 * @return valor de segundoApellido
	 */
	public String getSegundoApellido() {
		return segundoApellido;
	}


	/**
	 * @param segundoApellido el segundoApellido para asignar
	 */
	public void setSegundoApellido(String segundoApellido) {
		this.segundoApellido = segundoApellido;
	}


	/**
	 * @return valor de acronimoTipoID
	 */
	public String getAcronimoTipoID() {
		return acronimoTipoID;
	}


	/**
	 * @param acronimoTipoID el acronimoTipoID para asignar
	 */
	public void setAcronimoTipoID(String acronimoTipoID) {
		this.acronimoTipoID = acronimoTipoID;
	}


	/**
	 * @return valor de numeroID
	 */
	public String getNumeroID() {
		return numeroID;
	}


	/**
	 * @param numeroID el numeroID para asignar
	 */
	public void setNumeroID(String numeroID) {
		this.numeroID = numeroID;
	}


	/**
	 * @return valor de nombreOrganizado
	 */
	public String getNombreOrganizado() {
		return nombreOrganizado;
	}


	/**
	 * @param nombreOrganizado el nombreOrganizado para asignar
	 */
	public void setNombreOrganizado(String nombreOrganizado) {
		this.nombreOrganizado = nombreOrganizado;
	}

	/**
	 * @return valor de nombreOrganizadoLogin
	 */
	public String getNombreOrganizadoLogin() {
		return nombreOrganizadoLogin;
	}


	/**
	 * @return valor de codigo
	 */
	public Integer getCodigo() {
		return codigo;
	}


	/**
	 * @param codigo el codigo para asignar
	 */
	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}


	/**
	 * @param nombreOrganizadoLogin el nombreOrganizadoLogin para asignar
	 */
	public void setNombreOrganizadoLogin(String nombreOrganizadoLogin) {
		this.nombreOrganizadoLogin = nombreOrganizadoLogin;
	}
		
		


}
