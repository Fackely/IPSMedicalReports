/*
 * @(#)TipoNumeroId.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */

package util;

import java.io.Serializable;

/**
 * Clase utilitaria, encapsula el tipo (código) y el número de un identificador que
 * corresponda a la PK necesaria para traer desde una BD el conjunto de información
 * de un objeto del mundo. Si la PK sólo necesita de un valor (e.g., sólo un código,
 * o sólo un login), por convención este solamente se almacena en el atributo 'tipoId'.
 *
 * @version 1.0, Mar 1, 2003
 * @author 	<a href="mailto:Oscar@PrincetonSA.com">&Oacute;scar Andr&eacute;s L&oacute;pez P.</a>, <a href="mailto:Camilo@PrincetonSA.com">Camilo Andr&eacute;s Camacho P.</a>
 */
/*
* Tipo Modificacion: Segun incidencia 7055
* usuario: jesrioro
* Fecha: 30/05/2013
* Descripcion: Se  implementa  serializable  para  poder generar  el  reporte  de  HC  en  el  contexto de  reportes            
*/
public class TipoNumeroId implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Login, Tipo o Código del Id, según corresponda.
	 */
	private String tipoId;

	/**
	 * Número del Id, si aplica.
	 */
	private String numeroId;

	/**
	 * Indica si vamos a cambiar el tipo de Id de este objeto.
	 */
	private boolean changeId;

	/**
	 * Nuevo tipo de Id, si se trata de un cambio de Id.
	 */
	private String nuevoTipoId;

	/**
	 * Nuevo número de Id, si se trata de un cambio de Id.
	 */
	private String nuevoNumeroId;

	public TipoNumeroId()
	{
		this.tipoId = "";
		this.numeroId = "";
	}
	
	/**
	 * Crea un nuevo <code>TipoNumeroId</code> a partir de un tipo
	 * de Id, y asigna "" al número de Id.
	 * @param tipoId tipo del dentificador
	 */
	public TipoNumeroId(String tipoId) {
		this(tipoId, null);
	}

	/**
	 * Crea un nuevo <code>TipoNumeroId</code> a partir de un tipo
	 * de Id y un número de Id. No permite que los atributos queden
	 * en valores nulos.
	 * @param tipoId tipo del identificador
	 * @param numeroId número del identificador
	 */
	public TipoNumeroId(String tipoId, String numeroId) {
		this.tipoId = (tipoId != null) ? tipoId.trim() : "";
		this.numeroId = (numeroId != null) ? numeroId.trim() : "";
		this.changeId = false;
		this.nuevoTipoId = "";
		this.nuevoNumeroId = "";
	}

	/**
	 * Crea un nuevo <code>TipoNumeroId</code> a partir de un tipo
	 * de Id y un número de Id. No permite que los atributos queden
	 * en valores nulos. También, establece los valores de changeId,
	 * nuevoTipoId y nuevoNumeroId en sus valores respectivos, o ""
	 * si son nulos.
	 * @param tipoId tipo del identificador
	 * @param numeroId número del identificador
	 * @param changeId 
	 * @param nuevoTipoId 
	 * @param nuevoNumeroId 
	 */
	public TipoNumeroId(String tipoId, String numeroId, boolean changeId, String nuevoTipoId, String nuevoNumeroId) {
		this.tipoId = (tipoId != null) ? tipoId.trim() : "";
		this.numeroId = (numeroId != null) ? numeroId.trim() : "";
		this.changeId = changeId;
		this.nuevoTipoId = (nuevoTipoId != null) ? nuevoTipoId : "";
		this.nuevoNumeroId = (nuevoNumeroId != null) ? nuevoNumeroId : "";
	}

	/**
	 * Retorna el número del identificador.
	 * @return el número del identificador
	 */
	public String getNumeroId() {
		return numeroId;
	}

	/**
	 * Retorna el tipo del identificador.
	 * @return el tipo del identificador
	 */
	public String getTipoId() {
		return tipoId;
	}

	/**
	 * Indica si vamos o no cambiar el id de este objeto.
	 * @return <b>true</b> si es un cambio de id, <b>false</b> si no
	 */
	public boolean isChangeId() {
		return changeId;
	}

	/**
	 * Retorna el nuevo número de identificación de este objeto.
	 * @return el nuevo número de identificación de este objeto
	 */
	public String getNuevoNumeroId() {
		return nuevoNumeroId;
	}

	/**
	 * Retorna el nuevo tipo de identificación de este objeto.
	 * @return el nuevo tipo de identificación de este objeto
	 */
	public String getNuevoTipoId() {
		return nuevoTipoId;
	}

	/**
	* Establece el número del identificador.
	* @param as_numeroId el número del identificador a establecer
	*/
	public void setNumeroId(String as_numeroId)
	{
		if(as_numeroId != null)
			numeroId = as_numeroId.trim();
	}

	/**
	* Establece el tipo del identificador.
	* @param as_tipoId el tipo del identificador a establecer
	*/
	public void setTipoId(String as_tipoId)
	{
		if(as_tipoId != null)
			tipoId = as_tipoId.trim();
	}

	/**
	 * Establece si se trata o no de un cambio de Id.
	 * @param changeId <b>true</b> si es un cambio de id, <b>false</b> si no
	 */
	public void setChangeId(boolean changeId) {
		this.changeId = changeId;
	}

	/**
	 * Establece el nuevo número de Id.
	 * @param nuevoNumeroId el número de Id a establecer
	 */
	public void setNuevoNumeroId(String nuevoNumeroId) {
		this.nuevoNumeroId = nuevoNumeroId;
	}

	/**
	 * Establece el nuevo tipo de Id.
	 * @param nuevoTipoId el tipo de Id a establecer
	 */
	public void setNuevoTipoId(String nuevoTipoId) {
		this.nuevoTipoId = nuevoTipoId;
	}
}