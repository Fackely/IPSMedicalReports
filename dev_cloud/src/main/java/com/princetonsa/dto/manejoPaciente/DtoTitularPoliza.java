/*
 * Jun 29/2007
 */
package com.princetonsa.dto.manejoPaciente;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import util.InfoDatosString;
import util.UtilidadTexto;

/**
 * 
 * DTO Que almacena la informacion de la poliza
 * @author Sebastián Gómez R.
 *
 */
public class DtoTitularPoliza implements Serializable
{
	
	/**
	 * Versión serial
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Sub cuenta asociada
	 */
	private String subCuenta;
	
	/**
	 * Apellidos del titular
	 */
	private String apellidos;
	/**
	 * Nombres del titular
	 */
	private String nombres;
	/**
	 * Tipo de identificacion del titular
	 */
	private InfoDatosString tipoIdentificacion;
	
	/**
	 * Número identificacion del titular
	 */
	private String numeroIdentificacion;
	
	/**
	 * Direccion del titular
	 */
	private String direccion;
	
	/**
	 * Teléfono del titular
	 */
	private String telefono;
	
	/**
	 * Para verificar si ya existe en la base de datos
	 */
	private boolean existeBd;
	

	
	/**
	 * Objeto que almacena la informacion del titular
	 * Maneja un HashMap con las siguientes llaves
	 * codigo
	 * fecha
	 * autorizacion
	 * valor
	 * usuario
	 */
	private ArrayList<HashMap<String, Object>> informacionPoliza;
	
	/**
	 * Constructor
	 *
	 */
	public DtoTitularPoliza()
	{
		this.subCuenta = "";
		this.apellidos = "";
		this.nombres = "";
		this.tipoIdentificacion = new InfoDatosString("","");
		this.numeroIdentificacion = "";
		this.direccion = "";
		this.telefono = "";
		this.informacionPoliza = new ArrayList<HashMap<String,Object>>();
		this.existeBd = false;
	}

	/**
	 * @return the apellidos
	 */
	public String getApellidos() {
		return apellidos;
	}

	/**
	 * @param apellidos the apellidos to set
	 */
	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	/**
	 * @return the direccion
	 */
	public String getDireccion() {
		return direccion;
	}

	/**
	 * @param direccion the direccion to set
	 */
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	/**
	 * @return the informacionPoliza
	 */
	public String getFechaInformacionPoliza(int pos) {
		return ((HashMap)informacionPoliza.get(pos)).get("fecha").toString();
	}
	
	/**
	 * @return the informacionPoliza
	 */
	public String getAutorizacionInformacionPoliza(int pos) {
		return ((HashMap)informacionPoliza.get(pos)).get("autorizacion").toString();
	}
	
	/**
	 * @return the informacionPoliza
	 */
	public String getValorInformacionPoliza(int pos) {
		return ((HashMap)informacionPoliza.get(pos)).get("valor").toString();
	}
	
	/**
	 * @return the informacionPoliza
	 */
	public String getCodigoInformacionPoliza(int pos) {
		return ((HashMap)informacionPoliza.get(pos)).get("codigo").toString();
	}
	
	/**
	 * @return the informacionPoliza
	 */
	public String getUsuarioInformacionPoliza(int pos) {
		return ((HashMap)informacionPoliza.get(pos)).get("usuario").toString();
	}
	
	/**
	 * @return the informacionPoliza
	 */
	public boolean getExisteBdInformacionPoliza(int pos) {
		return UtilidadTexto.getBoolean(((HashMap)informacionPoliza.get(pos)).get("existeBd").toString());
	}
	
	/**
	 * @return the informacionPoliza
	 */
	public boolean getEliminarInformacionPoliza(int pos) {
		return UtilidadTexto.getBoolean(((HashMap)informacionPoliza.get(pos)).get("eliminar").toString());
	}
	
	/**
	 * @return the informacionPoliza
	 */
	public int getSizeInformacionPoliza() {
		return informacionPoliza.size();
	}

	/**
	 * @param informacionPoliza the informacionPoliza to set
	 */
	public void setInformacionPoliza(String codigo,String fecha, String autorizacion, String valor,String usuario,boolean existeBd,boolean eliminar)
	{
		HashMap registro = new HashMap();
		registro.put("codigo", codigo);
		registro.put("fecha", fecha);
		registro.put("autorizacion", autorizacion);
		registro.put("valor",valor);
		registro.put("usuario",usuario);
		registro.put("existeBd",existeBd);
		registro.put("eliminar",eliminar);
		this.informacionPoliza.add(registro);
	}

	/**
	 * @return the nombres
	 */
	public String getNombres() {
		return nombres;
	}

	/**
	 * @param nombres the nombres to set
	 */
	public void setNombres(String nombres) {
		this.nombres = nombres;
	}

	/**
	 * @return the numeroIdentificacion
	 */
	public String getNumeroIdentificacion() {
		return numeroIdentificacion;
	}

	/**
	 * @param numeroIdentificacion the numeroIdentificacion to set
	 */
	public void setNumeroIdentificacion(String numeroIdentificacion) {
		this.numeroIdentificacion = numeroIdentificacion;
	}

	/**
	 * @return the subCuenta
	 */
	public String getSubCuenta() {
		return subCuenta;
	}

	/**
	 * @param subCuenta the subCuenta to set
	 */
	public void setSubCuenta(String subCuenta) {
		this.subCuenta = subCuenta;
	}

	/**
	 * @return the tipoIdentificacion
	 */
	public String getCodigoTipoIdentificacion() {
		return tipoIdentificacion.getCodigo();
	}
	
	/**
	 * @return the tipoIdentificacion
	 */
	public String getDescripcionTipoIdentificacion() {
		return tipoIdentificacion.getValue();
	}

	/**
	 * @param tipoIdentificacion the tipoIdentificacion to set
	 */
	public void setCodigoTipoIdentificacion(String tipoIdentificacion) {
		this.tipoIdentificacion.setCodigo(tipoIdentificacion);
	}
	
	/**
	 * @param tipoIdentificacion the tipoIdentificacion to set
	 */
	public void setDescripcionTipoIdentificacion(String tipoIdentificacion) {
		this.tipoIdentificacion.setValue(tipoIdentificacion);
	}

	/**
	 * @return the telefono
	 */
	public String getTelefono() {
		return telefono;
	}

	/**
	 * @param telefono the telefono to set
	 */
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	/**
	 * @return the existeBd
	 */
	public boolean isExisteBd() {
		return existeBd;
	}

	/**
	 * @param existeBd the existeBd to set
	 */
	public void setExisteBd(boolean existeBd) {
		this.existeBd = existeBd;
	}

	
	


}
