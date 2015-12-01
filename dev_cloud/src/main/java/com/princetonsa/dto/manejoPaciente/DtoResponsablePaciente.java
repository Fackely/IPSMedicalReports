/*
 * Jul 04, 2007
 */
package com.princetonsa.dto.manejoPaciente;

import java.io.Serializable;

import util.InfoDatosString;

/**
 * DATA TRANSFER OBJECT del responsable paciente
 * @author Sebastián Gómez R.
 *
 */
public class DtoResponsablePaciente implements Serializable
{
	/**
	 * Versión serial
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Consecutivo del responsable paciente
	 */
	private String codigo;
	
	/**
	 * Numerod e identificacion
	 */
	private String numeroIdentificacion;
	
	/**
	 * Tipo de identificacion
	 */
	private String tipoIdentificacion;
	
	/**
	 * Descripcion del tipo de identificacin
	 */
	private String descripcionTipoIdentificacion;
	
	/**
	 * Direccion
	 */
	private String direccion;
	
	/**
	 * Telefono
	 */
	private String telefono;
	
	/**
	 * Relacion del paciente
	 */
	private String relacionPaciente;
	
	/**
	 * Pais de expedicion
	 */
	private InfoDatosString paisExpedicion;
	
	/**
	 * Ciudad de expedicion
	 */
	private InfoDatosString ciudadExpedicion;
	
	/**
	 * primer apellido
	 */
	private String primerApellido;
	
	/**
	 * Segudo apellido
	 */
	private String segundoApellido;
	
	/**
	 * Primer Nombre
	 */
	private String primerNombre;
	
	/**
	 * Segundo Nombre 
	 */
	private String segundoNombre;
	
	/**
	 * Pais residencia
	 */
	private InfoDatosString pais;
	
	/**
	 * Ciudad residencia
	 */
	private InfoDatosString ciudad;
	
	/**
	 * Barrio
	 */
	private InfoDatosString barrio;
	
	/**
	 * Fecha de Nacimiento
	 */
	private String fechaNacimiento;
	
	/**
	 * Login Usuario
	 */
	private String loginUsuario;
	
	/**
	 * Indica si se debe eliminar el responsable paciente
	 */
	private boolean eliminar;
	
	/**
	 * Constructor
	 *
	 */
	public DtoResponsablePaciente()
	{
		this.codigo = "";
		this.numeroIdentificacion = "";
		this.tipoIdentificacion = "";
		this.descripcionTipoIdentificacion = "";
		this.direccion = "";
		this.telefono = "";
		this.relacionPaciente = "";
		this.paisExpedicion = new InfoDatosString("","");
		this.ciudadExpedicion = new InfoDatosString("","","");
		this.primerApellido = "";
		this.segundoApellido = "";
		this.primerNombre = "";
		this.segundoNombre = "";
		this.pais = new InfoDatosString("","");
		this.ciudad = new InfoDatosString("","","");
		this.barrio = new InfoDatosString("","");
		this.fechaNacimiento = "";
		this.loginUsuario = "";
		this.eliminar = false;
	}

	/**
	 * @return the barrio
	 */
	public String getCodigoBarrio() {
		return barrio.getCodigo();
	}
	
	/**
	 * @return the barrio
	 */
	public String getDescripcionBarrio() {
		return barrio.getNombre();
	}

	/**
	 * @param barrio the barrio to set
	 */
	public void setCodigoBarrio(String barrio) {
		this.barrio.setCodigo(barrio);
	}
	
	/**
	 * @param barrio the barrio to set
	 */
	public void setDescripcionBarrio(String barrio) {
		this.barrio.setNombre(barrio);
	}

	/**
	 * @return the ciudad
	 */
	public String getCodigoCiudad() {
		return ciudad.getCodigo();
	}
	
	/**
	 * @return the ciudad
	 */
	public String getCodigoDepto() {
		return ciudad.getNombre();
	}
	
	
	/**
	 * @return the ciudad
	 */
	public String getDescripcionCiudad() {
		return ciudad.getDescripcion();
	}

	/**
	 * @param ciudad the ciudad to set
	 */
	public void setCodigoCiudad(String ciudad) {
		this.ciudad.setCodigo(ciudad);
	}
	
	/**
	 * @param ciudad the ciudad to set
	 */
	public void setCodigoDepto(String ciudad) {
		this.ciudad.setNombre(ciudad);
	}
	
	/**
	 * @param ciudad the ciudad to set
	 */
	public void setDescripcionCiudad(String ciudad) {
		this.ciudad.setDescripcion (ciudad);
	}

	/**
	 * @return the ciudadExpedicion
	 */
	public String getCodigoCiudadExpedicion() {
		return ciudadExpedicion.getCodigo();
	}
	
	/**
	 * @return the ciudadExpedicion
	 */
	public String getCodigoDeptoExpedicion() {
		return ciudadExpedicion.getNombre();
	}
	
	/**
	 * @return the ciudadExpedicion
	 */
	public String getDescripcionCiudadExpedicion() {
		return ciudadExpedicion.getDescripcion();
	}

	/**
	 * @param ciudadExpedicion the ciudadExpedicion to set
	 */
	public void setCodigoCiudadExpedicion(String ciudadExpedicion) {
		this.ciudadExpedicion.setCodigo(ciudadExpedicion);
	}
	
	/**
	 * @param ciudadExpedicion the ciudadExpedicion to set
	 */
	public void setCodigoDeptoExpedicion(String ciudadExpedicion) {
		this.ciudadExpedicion.setNombre(ciudadExpedicion);
	}
	
	/**
	 * @param ciudadExpedicion the ciudadExpedicion to set
	 */
	public void setDescripcionCiudadExpedicion(String ciudadExpedicion) {
		this.ciudadExpedicion.setDescripcion(ciudadExpedicion);
	}

	/**
	 * @return the codigo
	 */
	public String getCodigo() {
		return codigo;
	}

	/**
	 * @param codigo the codigo to set
	 */
	public void setCodigo(String codigo) {
		this.codigo = codigo;
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
	 * @return the fechaNacimiento
	 */
	public String getFechaNacimiento() {
		return fechaNacimiento;
	}

	/**
	 * @param fechaNacimiento the fechaNacimiento to set
	 */
	public void setFechaNacimiento(String fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
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
	 * @return the pais
	 */
	public String getCodigoPais() {
		return pais.getCodigo();
	}
	
	/**
	 * @return the pais
	 */
	public String getDescripcionPais() {
		return pais.getNombre();
	}

	/**
	 * @param pais the pais to set
	 */
	public void setCodigoPais(String pais) {
		this.pais.setCodigo(pais);
	}
	
	/**
	 * @param pais the pais to set
	 */
	public void setDescripcionPais(String pais) {
		this.pais.setNombre(pais);
	}

	/**
	 * @return the paisExpedicion
	 */
	public String getCodigoPaisExpedicion() {
		return paisExpedicion.getCodigo();
	}
	
	/**
	 * @return the paisExpedicion
	 */
	public String getDescripcionPaisExpedicion() {
		return paisExpedicion.getNombre();
	}

	/**
	 * @param paisExpedicion the paisExpedicion to set
	 */
	public void setCodigoPaisExpedicion(String paisExpedicion) {
		this.paisExpedicion.setCodigo(paisExpedicion);
	}
	
	/**
	 * @param paisExpedicion the paisExpedicion to set
	 */
	public void setDescripcionPaisExpedicion(String paisExpedicion) {
		this.paisExpedicion.setNombre(paisExpedicion);
	}

	/**
	 * @return the primerApellido
	 */
	public String getPrimerApellido() {
		return primerApellido;
	}

	/**
	 * @param primerApellido the primerApellido to set
	 */
	public void setPrimerApellido(String primerApellido) {
		this.primerApellido = primerApellido;
	}

	/**
	 * @return the primerNombre
	 */
	public String getPrimerNombre() {
		return primerNombre;
	}

	/**
	 * @param primerNombre the primerNombre to set
	 */
	public void setPrimerNombre(String primerNombre) {
		this.primerNombre = primerNombre;
	}

	/**
	 * @return the relacionPaciente
	 */
	public String getRelacionPaciente() {
		return relacionPaciente;
	}

	/**
	 * @param relacionPaciente the relacionPaciente to set
	 */
	public void setRelacionPaciente(String relacionPaciente) {
		this.relacionPaciente = relacionPaciente;
	}

	/**
	 * @return the segundoApellido
	 */
	public String getSegundoApellido() {
		return segundoApellido;
	}

	/**
	 * @param segundoApellido the segundoApellido to set
	 */
	public void setSegundoApellido(String segundoApellido) {
		this.segundoApellido = segundoApellido;
	}

	/**
	 * @return the segundoNombre
	 */
	public String getSegundoNombre() {
		return segundoNombre;
	}

	/**
	 * @param segundoNombre the segundoNombre to set
	 */
	public void setSegundoNombre(String segundoNombre) {
		this.segundoNombre = segundoNombre;
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
	 * @return the tipoIdentificacion
	 */
	public String getTipoIdentificacion() {
		return tipoIdentificacion;
	}

	/**
	 * @param tipoIdentificacion the tipoIdentificacion to set
	 */
	public void setTipoIdentificacion(String tipoIdentificacion) {
		this.tipoIdentificacion = tipoIdentificacion;
	}

	/**
	 * @return the loginUsuario
	 */
	public String getLoginUsuario() {
		return loginUsuario;
	}

	/**
	 * @param loginUsuario the loginUsuario to set
	 */
	public void setLoginUsuario(String loginUsuario) {
		this.loginUsuario = loginUsuario;
	}

	/**
	 * @return the descripcionTipoIdentificacion
	 */
	public String getDescripcionTipoIdentificacion() {
		return descripcionTipoIdentificacion;
	}

	/**
	 * @param descripcionTipoIdentificacion the descripcionTipoIdentificacion to set
	 */
	public void setDescripcionTipoIdentificacion(
			String descripcionTipoIdentificacion) {
		this.descripcionTipoIdentificacion = descripcionTipoIdentificacion;
	}

	/**
	 * @return the eliminar
	 */
	public boolean isEliminar() {
		return eliminar;
	}

	/**
	 * @param eliminar the eliminar to set
	 */
	public void setEliminar(boolean eliminar) {
		this.eliminar = eliminar;
	}
}
