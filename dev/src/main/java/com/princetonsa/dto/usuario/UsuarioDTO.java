/**
 * 
 */
package com.princetonsa.dto.usuario;

import java.io.Serializable;

/**
 * DTO para lo datos de usuario al autenticarse.
 * 
 * @author Fernando Ocampo
 * 
 */
public class UsuarioDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7990558633137110437L;

	/**
	 * login del usuario.
	 */
	private String usuario;

	/**
	 * contrase&ntilde;a del usuario.
	 */
	private String password;

	/**
	 * N&uacute;mero de intentos para autenticarse.
	 */
	private int intentos;
	
	public UsuarioDTO() {
	}

	public UsuarioDTO(String usuario, String password) {
		super();
		this.usuario = usuario;
		this.password = password;
	}

	
	public UsuarioDTO(String usuario, String password, int intentos) {
		super();
		this.usuario = usuario;
		this.password = password;
		this.intentos = intentos;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getIntentos() {
		return intentos;
	}

	public void setIntentos(int intentos) {
		this.intentos = intentos;
	}
}
