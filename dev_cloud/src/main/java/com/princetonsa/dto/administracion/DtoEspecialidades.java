package com.princetonsa.dto.administracion;

import java.io.Serializable;

import util.ConstantesBD;

/**
 * @author Víctor Hugo Gómez L.
 */

public class DtoEspecialidades implements Serializable{
	
	private int codigo;
	private String consecutivo;
	private int codigoInstitucion;
	private String descripcion;
	private int codigoCentroCostoHonorario;
	private String usuarioGrabacion;
	private String nombreCentroCosto;
	
	/**
	 * nombre del profesional 
	 */
	private String nombreProfesional;

	/**
	 *Numero del registro del profesional de la salud  
	 */
	private String numeroRegistro;
	
//	Anexo 844
	private String tipoEspecialidad;

	// Atributos Validacion 
	private String modificar;
	private String ingresar;
	private String eliminar;
	
	// Atributo Boton Eliminar
	private String mostrar;
	
	/**
	 * Constructor
	 */
	public DtoEspecialidades()
	{
		this.reset();
	}
	
	/**
	 * Método que inicia los datos de la espcecialidad
	 *
	 */
	private void reset() 
	{
		this.codigo = ConstantesBD.codigoNuncaValido;
		this.consecutivo = "";
		this.codigoInstitucion = ConstantesBD.codigoNuncaValido;
		this.descripcion = "";
		this.codigoCentroCostoHonorario = ConstantesBD.codigoNuncaValido;
		this.usuarioGrabacion = "";
		this.nombreCentroCosto="";
		
		// Atributos Validacion
		this.modificar = ConstantesBD.acronimoNo;
		this.ingresar = ConstantesBD.acronimoNo;
		this.eliminar = ConstantesBD.acronimoNo;
		
		// Atributo Boton Eliminar
		this.mostrar = ConstantesBD.acronimoSi;
		
		this.tipoEspecialidad = new String("");
	}

	/**
	 * @return the codigo
	 */
	public int getCodigo() {
		return codigo;
	}

	/**
	 * @param codigo the codigo to set
	 */
	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	/**
	 * @return the consecutivo
	 */
	public String getConsecutivo() {
		return consecutivo;
	}

	/**
	 * @param consecutivo the consecutivo to set
	 */
	public void setConsecutivo(String consecutivo) {
		this.consecutivo = consecutivo;
	}

	/**
	 * @return the codigoInstitucion
	 */
	public int getCodigoInstitucion() {
		return codigoInstitucion;
	}

	/**
	 * @param codigoInstitucion the codigoInstitucion to set
	 */
	public void setCodigoInstitucion(int codigoInstitucion) {
		this.codigoInstitucion = codigoInstitucion;
	}

	/**
	 * @return the descripcion
	 */
	public String getDescripcion() {
		return descripcion;
	}

	/**
	 * @param descripcion the descripcion to set
	 */
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	/**
	 * @return the codigoCentroCostoHonorario
	 */
	public int getCodigoCentroCostoHonorario() {
		return codigoCentroCostoHonorario;
	}

	/**
	 * @param codigoCentroCostoHonorario the codigoCentroCostoHonorario to set
	 */
	public void setCodigoCentroCostoHonorario(int codigoCentroCostoHonorario) {
		this.codigoCentroCostoHonorario = codigoCentroCostoHonorario;
	}

	/**
	 * @return the usuarioGrabacion
	 */
	public String getUsuarioGrabacion() {
		return usuarioGrabacion;
	}

	/**
	 * @param usuarioGrabacion the usuarioGrabacion to set
	 */
	public void setUsuarioGrabacion(String usuarioGrabacion) {
		this.usuarioGrabacion = usuarioGrabacion;
	}

	/**
	 * @return the nombreCentroCosto
	 */
	public String getNombreCentroCosto() {
		return nombreCentroCosto;
	}

	/**
	 * @param nombreCentroCosto the nombreCentroCosto to set
	 */
	public void setNombreCentroCosto(String nombreCentroCosto) {
		this.nombreCentroCosto = nombreCentroCosto;
	}

	/**
	 * @return the modificar
	 */
	public String getModificar() {
		return modificar;
	}

	/**
	 * @param modificar the modificar to set
	 */
	public void setModificar(String modificar) {
		this.modificar = modificar;
	}

	/**
	 * @return the ingresar
	 */
	public String getIngresar() {
		return ingresar;
	}

	/**
	 * @param ingresar the ingresar to set
	 */
	public void setIngresar(String ingresar) {
		this.ingresar = ingresar;
	}

	/**
	 * @return the eliminar
	 */
	public String getEliminar() {
		return eliminar;
	}

	/**
	 * @param eliminar the eliminar to set
	 */
	public void setEliminar(String eliminar) {
		this.eliminar = eliminar;
	}

	/**
	 * @return the mostrar
	 */
	public String getMostrar() {
		return mostrar;
	}

	/**
	 * @param mostrar the mostrar to set
	 */
	public void setMostrar(String mostrar) {
		this.mostrar = mostrar;
	}

	/**
	 * @return the tipoEspecialidad
	 */
	public String getTipoEspecialidad() {
		return tipoEspecialidad;
	}

	/**
	 * @param tipoEspecialidad the tipoEspecialidad to set
	 */
	public void setTipoEspecialidad(String tipoEspecialidad) {
		this.tipoEspecialidad = tipoEspecialidad;
	}

	/**
	 * @return the nombreProfesional
	 */
	public String getNombreProfesional() {
		return nombreProfesional;
	}

	/**
	 * @param nombreProfesional the nombreProfesional to set
	 */
	public void setNombreProfesional(String nombreProfesional) {
		this.nombreProfesional = nombreProfesional;
	}

	/**
	 * @return the numeroRegistro
	 */
	public String getNumeroRegistro() {
		return numeroRegistro;
	}

	/**
	 * @param numeroRegistro the numeroRegistro to set
	 */
	public void setNumeroRegistro(String numeroRegistro) {
		this.numeroRegistro = numeroRegistro;
	}
	
	
	
	
	
}
