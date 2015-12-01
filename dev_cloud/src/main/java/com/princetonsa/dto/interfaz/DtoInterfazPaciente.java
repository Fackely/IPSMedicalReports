/**
 * 
 */
package com.princetonsa.dto.interfaz;

import java.io.Serializable;

import util.ConstantesBD;

/**
 * @author Jorge Armando Osorio Velasquez.
 * 
 * DTO para el manejo de los datos del paciente, que se comparte con la interfaz.
 *
 */
public class DtoInterfazPaciente implements Serializable
{
	/**
	 * Codigo del paciente asignado por Axioma
	 */ 
	private String codigo;
	
	/**
	 * Acronimo de los tipos de identificacion de axioma
	 */
	private String tipoIdentificiacion;
	
	/**
	 * Numero de identificacion de axioma
	 */
	private String numeroIdentificacion;
	
	/**
	 * Primer Nombre 
	 */
	private String primerNombre;
	
	/**
	 * Segundo Nombre
	 */
	private String segundoNombre;
	
	/**
	 * Primer Apellido
	 */
	private String primerApellido;
	
	/**
	 * Segundo Apellido
	 */
	private String segundoApellido;
	
	/**
	 * Numero ingreso adcual de axioma
	 */
	private String ingreso;
	
	/**
	 * Estado del ingreso A - Activo(Al crear un ingreso)    I - Inactivo (Al facturar o cerrar la cuenta de paciente).
	 */
	private String estadoIngreso;
	
	/**
	 * Estado del registro 0 - No procesado    1 - Procesado 
	 * otros valores pueden ser:
	 * 10 - 199 : codigos de inconsistencias de Axioma
	 * 200 en adelante: para el sistema interfaz
	 */
	private String estadoRegistro;
	
	/**
	 * institucion del paciente, este campo solo se usa para consultar cuales son las tablas de la interfaz para esta institucion.
	 */
	private int institucion;
	
	/**
	 * Fecha de nacimiento
	 */
	private String fechaNacimiento;

	
	/**
	 * Campos agregados el miercoles 12 de marzo del 2008, para la funcionalidad de prestamos de historias clinicas shaio
	 */
	// numero de la historia clinica
	private int numhc;
	// codigo de interfaz shaio, convenio ppal del ultmio ingreso
	private String codconv;
	// Nombre correspondiente al convenio asocioado al codigo de interfaz codconv
	private String nomconv;
	// tercero asociado a la empresa del convenio asociado al campo de codigo de interfaz codconv
	private String tercero;	
	/**
	 * 
	 */
	
	// Agregado el jueves 21 de Agosto de 2008, como adicion en la interfaz de Paciente, tambien debe existir en DTO Nutricion
	private String viaIngreso;
	
	// Agregado el viernes 26 de Septiembre de 2008, como adicion a la interfaz paciente.
	private String sexo;
	
	//Agregado el viernes 24 de Octubre de 2008, como adicion a la interfaz paciente.
	private String usuario;
	
	//-----------------------------------------------------
	//modificado por anexo 779
	/**
	 * indica si tiene error
	 */
	private boolean error=false;
	
	/**
	 * @return the error
	 */
	public boolean isError() {
		return error;
	}


	/**
	 * @param error the error to set
	 */
	public void setError(boolean error) {
		this.error = error;
	}


	/**
	 * @return the mensaje
	 */
	public String getMensaje() {
		return mensaje;
	}


	/**
	 * @param mensaje the mensaje to set
	 */
	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	/**
	 * mensaje de la operacion en el sql
	 */
	private String mensaje="";
	//-----------------------------------------------------
	/**
	 * Inicializacion de los campos.
	 *
	 */
	public DtoInterfazPaciente() 
	{
		this.codigo = "";
		this.tipoIdentificiacion = "";
		this.numeroIdentificacion = "";
		this.primerNombre = "";
		this.segundoNombre = "";
		this.primerApellido = "";
		this.segundoApellido = "";
		this.ingreso = "";
		this.estadoIngreso = "";
		this.institucion=ConstantesBD.codigoNuncaValido;
		this.fechaNacimiento = "";
		this.estadoRegistro = "";
		
		//
		this.numhc = 0;
		this.codconv = "";
		this.nomconv = "";
		this.tercero = "";
		
		this.viaIngreso="";
		this.sexo="";
		this.usuario="";
		this.error=false;
		this.mensaje="";
	}

	
	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getEstadoIngreso() {
		return estadoIngreso;
	}

	public void setEstadoIngreso(String estadoIngreso) {
		this.estadoIngreso = estadoIngreso;
	}

	public String getIngreso() {
		return ingreso;
	}

	public void setIngreso(String ingreso) {
		this.ingreso = ingreso;
	}

	public String getNumeroIdentificacion() {
		return numeroIdentificacion;
	}

	public void setNumeroIdentificacion(String numeroIdentificacion) {
		this.numeroIdentificacion = numeroIdentificacion;
	}

	public String getPrimerApellido() {
		return primerApellido;
	}

	public void setPrimerApellido(String primerApellido) {
		this.primerApellido = primerApellido;
	}

	public String getPrimerNombre() {
		return primerNombre;
	}

	public void setPrimerNombre(String primerNombre) {
		this.primerNombre = primerNombre;
	}

	public String getSegundoApellido() {
		return segundoApellido;
	}

	public void setSegundoApellido(String segundoApellido) {
		this.segundoApellido = segundoApellido;
	}

	public String getSegundoNombre() {
		return segundoNombre;
	}

	public void setSegundoNombre(String segundoNompre) {
		this.segundoNombre = segundoNompre;
	}

	public String getTipoIdentificiacion() {
		return tipoIdentificiacion;
	}

	public void setTipoIdentificiacion(String tipoIdentificiacion) {
		this.tipoIdentificiacion = tipoIdentificiacion;
	}

	public int getInstitucion() {
		return institucion;
	}

	public void setInstitucion(int institucion) {
		this.institucion = institucion;
	}

	/**
	 * @param codigo
	 * @param tipoIdentificiacion
	 * @param numeroIdentificacion
	 * @param primerNombre
	 * @param segundoNombre
	 * @param primerApellido
	 * @param segundoApellido
	 * @param ingreso
	 * @param estadoIngreso
	 * @param institucion
	 */
	public DtoInterfazPaciente(String codigo, String tipoIdentificiacion, String numeroIdentificacion, String primerNombre, String segundoNombre, String primerApellido, String segundoApellido, String ingreso, String estadoIngreso, int institucion) {
		this.codigo = codigo;
		this.tipoIdentificiacion = tipoIdentificiacion;
		this.numeroIdentificacion = numeroIdentificacion;
		this.primerNombre = primerNombre;
		this.segundoNombre = segundoNombre;
		this.primerApellido = primerApellido;
		this.segundoApellido = segundoApellido;
		this.ingreso = ingreso;
		this.estadoIngreso = estadoIngreso;
		this.institucion = institucion;
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
	 * @return the estadoRegistro
	 */
	public String getEstadoRegistro() {
		return estadoRegistro;
	}

	/**
	 * @param estadoRegistro the estadoRegistro to set
	 */
	public void setEstadoRegistro(String estadoRegistro) {
		this.estadoRegistro = estadoRegistro;
	}

	
	//-------	
	/**
	 * @return the codconv
	 */
	public String getCodconv() {
		return codconv;
	}

	/**
	 * @param codconv the codconv to set
	 */
	public void setCodconv(String codconv) {
		this.codconv = codconv;
	}

	/**
	 * @return the nomconv
	 */
	public String getNomconv() {
		return nomconv;
	}

	/**
	 * @param nomconv the nomconv to set
	 */
	public void setNomconv(String nomconv) {
		this.nomconv = nomconv;
	}

	/**
	 * @return the numhc
	 */
	public int getNumhc() {
		return numhc;
	}

	/**
	 * @param numhc the numhc to set
	 */
	public void setNumhc(int numhc) {
		this.numhc = numhc;
	}

	/**
	 * @return the tercero
	 */
	public String getTercero() {
		return tercero;
	}

	/**
	 * @param tercero the tercero to set
	 */
	public void setTercero(String tercero) {
		this.tercero = tercero;
	}
	
	/**
	 * 
	 * @return via ingreso
	 */
	public String getViaIngreso() {
		return viaIngreso;
	}
	
	/**
	 * 
	 * @param viaIngreso
	 */
	public void setViaIngreso(String viaIngreso) {
		this.viaIngreso = viaIngreso;
	}

	/**
	 * @return the sexo
	 */
	public String getSexo() {
		return sexo;
	}

	/**
	 * @param sexo the sexo to set
	 */
	public void setSexo(String sexo) {
		this.sexo = sexo;
	}
	
	/**
	 * @return the usuario
	 */
	public String getUsuario() {
		return usuario;
	}

	/**
	 * @param usuario the usuario to set
	 */
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	
}
