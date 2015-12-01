package com.princetonsa.dto.administracion;

import java.io.Serializable;

import util.ConstantesBD;


/**
 * 
 * @author Edgar Carvajal
 *
 */
public class DtoUsuario implements Serializable 
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private String login;
	private String password;
	private int codigoPersona; 
	private int institucion;       
	private int  cargo; 
	private double  contratoInterfaz;
	private int centroAtencion;
	private DtoPersonas persona;
	
	

	/**
	 * 
	 */
	public  DtoUsuario()
	{
		this.login				= "";
		this.password			= "";
		this.codigoPersona		= ConstantesBD.codigoNuncaValido; 
		this.institucion		= ConstantesBD.codigoNuncaValido;  
		this.cargo				= ConstantesBD.codigoNuncaValido; 
		this.contratoInterfaz	= ConstantesBD.codigoNuncaValidoDouble;
		this.centroAtencion 	= ConstantesBD.codigoNuncaValido;
	}


	/**
	 * @return the login
	 */
	public String getLogin() {
		return login;
	}


	/**
	 * @param login the login to set
	 */
	public void setLogin(String login) {
		this.login = login;
	}


	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}


	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}


	/**
	 * @return the codigoPersona
	 */
	public int getCodigoPersona() {
		return codigoPersona;
	}


	/**
	 * @param codigoPersona the codigoPersona to set
	 */
	public void setCodigoPersona(int codigoPersona) {
		this.codigoPersona = codigoPersona;
	}


	/**
	 * @return the institucion
	 */
	public int getInstitucion() {
		return institucion;
	}


	/**
	 * @param institucion the institucion to set
	 */
	public void setInstitucion(int institucion) {
		this.institucion = institucion;
	}


	/**
	 * @return the cargo
	 */
	public int getCargo() {
		return cargo;
	}


	/**
	 * @param cargo the cargo to set
	 */
	public void setCargo(int cargo) {
		this.cargo = cargo;
	}


	/**
	 * @return the contratoInterfaz
	 */
	public double getContratoInterfaz() {
		return contratoInterfaz;
	}


	/**
	 * @param contratoInterfaz the contratoInterfaz to set
	 */
	public void setContratoInterfaz(double contratoInterfaz) {
		this.contratoInterfaz = contratoInterfaz;
	}


	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
	/**
	 * @return the centroAtencion
	 */
	public int getCentroAtencion() {
		return centroAtencion;
	}


	/**
	 * @param centroAtencion the centroAtencion to set
	 */
	public void setCentroAtencion(int centroAtencion) {
		this.centroAtencion = centroAtencion;
	}


	/**
	 * @return the persona
	 */
	public DtoPersonas getPersona() {
		return persona;
	}


	/**
	 * @param persona the persona to set
	 */
	public void setPersona(DtoPersonas persona) {
		this.persona = persona;
	}

}
