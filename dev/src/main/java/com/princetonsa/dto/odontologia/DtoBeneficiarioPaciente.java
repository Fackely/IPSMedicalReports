package com.princetonsa.dto.odontologia;

import java.io.Serializable;

import com.princetonsa.dto.administracion.DtoPersonas;

public class DtoBeneficiarioPaciente extends DtoPersonas implements Serializable{

	
	/**
	 * Versión serial
	 */
	
	private static final long serialVersionUID = 1L;
	private String codigoBeneficiario;
	private String codigoPaciente;
	private String personaBeneficiario;
	private String parentezco;
	private String ocupacion;
	private String estudio;
	private String tipoOcupacion;
	private String fechaModificacion;
	private String horaModificacion;
	private String usuarioModificacion;
	private String codigoTipoIdentificacion;
	private String nombreTipoIdentificacion;
	
	private String edad;
	private String sexo;
	private boolean ingresoEdad; //para saber si se debe ingresar edad
	private String anios;
	private String seleccionEdad;
	private String empleado;
	private String independiente;
	private String nivelEstudios;
	private String paisResidencia;
	private String ciudadResidencia;
	private String telefono;
	private String celular;
	private String email;
	
	
	public DtoBeneficiarioPaciente()
	{
		this.reset();
	}
	
	
	public void reset()
	{
		this.codigoBeneficiario= new String("");
		this.codigoPaciente= new String("");
		this.personaBeneficiario= new String("");
		this.parentezco= new String("");
		this.ocupacion= new String("");
		this.estudio= new String("");
		this.tipoOcupacion= new String("");
		this.fechaModificacion= new String("");
		this.horaModificacion= new String("");
		this.usuarioModificacion= new String("");
		this.codigoTipoIdentificacion=new String("");
		this.nombreTipoIdentificacion=new String("");
		
		this.edad= new String("");
		this.sexo=new String("");
		this.ingresoEdad=false;
		this.anios=new String("");
		this.seleccionEdad=new String("");
		
		this.empleado= new String("");
		this.independiente= new String("");
		this.nivelEstudios= new String("");
		this.paisResidencia= new String("");
		this.ciudadResidencia= new String("");
		this.telefono= new String("");
		this.celular= new String("");
		this.email= new String("");
	}
	
	/**
	 * @return the codigo
	 */
	public String getCodigoBeneficiario() {
		return codigoBeneficiario;
	}
	/**
	 * @param codigo the codigo to set
	 */
	public void setCodigoBeneficiario(String codigoBeneficiario) {
		this.codigoBeneficiario = codigoBeneficiario;
	}
	/**
	 * @return the codigoPaciente
	 */
	public String getCodigoPaciente() {
		return codigoPaciente;
	}
	/**
	 * @param codigoPaciente the codigoPaciente to set
	 */
	public void setCodigoPaciente(String codigoPaciente) {
		this.codigoPaciente = codigoPaciente;
	}
	/**
	 * @return the personaBeneficiario
	 */
	public String getPersonaBeneficiario() {
		return personaBeneficiario;
	}
	/**
	 * @param personaBeneficiario the personaBeneficiario to set
	 */
	public void setPersonaBeneficiario(String personaBeneficiario) {
		this.personaBeneficiario = personaBeneficiario;
	}
	/**
	 * @return the parentezco
	 */
	public String getParentezco() {
		return parentezco;
	}
	/**
	 * @param parentezco the parentezco to set
	 */
	public void setParentezco(String parentezco) {
		this.parentezco = parentezco;
	}
	/**
	 * @return the ocupacion
	 */
	public String getOcupacion() {
		return ocupacion;
	}
	/**
	 * @param ocupacion the ocupacion to set
	 */
	public void setOcupacion(String ocupacion) {
		this.ocupacion = ocupacion;
	}
	/**
	 * @return the estudio
	 */
	public String getEstudio() {
		return estudio;
	}
	/**
	 * @param estudio the estudio to set
	 */
	public void setEstudio(String estudio) {
		this.estudio = estudio;
	}
	/**
	 * @return the tipoOcupacion
	 */
	public String getTipoOcupacion() {
		return tipoOcupacion;
	}
	/**
	 * @param tipoOcupacion the tipoOcupacion to set
	 */
	public void setTipoOcupacion(String tipoOcupacion) {
		this.tipoOcupacion = tipoOcupacion;
	}
	/**
	 * @return the fechaModificacion
	 */
	public String getFechaModificacion() {
		return fechaModificacion;
	}
	/**
	 * @param fechaModificacion the fechaModificacion to set
	 */
	public void setFechaModificacion(String fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}
	/**
	 * @return the horaModificacion
	 */
	public String getHoraModificacion() {
		return horaModificacion;
	}
	/**
	 * @param horaModificacion the horaModificacion to set
	 */
	public void setHoraModificacion(String horaModificacion) {
		this.horaModificacion = horaModificacion;
	}
	/**
	 * @return the usuarioModificacion
	 */
	public String getUsuarioModificacion() {
		return usuarioModificacion;
	}
	/**
	 * @param usuarioModificacion the usuarioModificacion to set
	 */
	public void setUsuarioModificacion(String usuarioModificacion) {
		this.usuarioModificacion = usuarioModificacion;
	}


	/**
	 * @return the edad
	 */
	public String getEdad() {
		return edad;
	}


	/**
	 * @param edad the edad to set
	 */
	public void setEdad(String edad) {
		this.edad = edad;
	}


	/**
	 * @return the empleado
	 */
	public String getEmpleado() {
		return empleado;
	}


	/**
	 * @param empleado the empleado to set
	 */
	public void setEmpleado(String empleado) {
		this.empleado = empleado;
	}


	/**
	 * @return the independiente
	 */
	public String getIndependiente() {
		return independiente;
	}


	/**
	 * @param independiente the independiente to set
	 */
	public void setIndependiente(String independiente) {
		this.independiente = independiente;
	}


	/**
	 * @return the nivelEstudios
	 */
	public String getNivelEstudios() {
		return nivelEstudios;
	}


	/**
	 * @param nivelEstudios the nivelEstudios to set
	 */
	public void setNivelEstudios(String nivelEstudios) {
		this.nivelEstudios = nivelEstudios;
	}


	/**
	 * @return the paisResidencia
	 */
	public String getPaisResidencia() {
		return paisResidencia;
	}


	/**
	 * @param paisResidencia the paisResidencia to set
	 */
	public void setPaisResidencia(String paisResidencia) {
		this.paisResidencia = paisResidencia;
	}


	/**
	 * @return the ciudadResidencia
	 */
	public String getCiudadResidencia() {
		return ciudadResidencia;
	}


	/**
	 * @param ciudadResidencia the ciudadResidencia to set
	 */
	public void setCiudadResidencia(String ciudadResidencia) {
		this.ciudadResidencia = ciudadResidencia;
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
	 * @return the celular
	 */
	public String getCelular() {
		return celular;
	}


	/**
	 * @param celular the celular to set
	 */
	public void setCelular(String celular) {
		this.celular = celular;
	}


	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}


	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}


	/**
	 * @return the codigoTipoIdentificacion
	 */
	public String getCodigoTipoIdentificacion() {
		return codigoTipoIdentificacion;
	}


	/**
	 * @param codigoTipoIdentificacion the codigoTipoIdentificacion to set
	 */
	public void setCodigoTipoIdentificacion(String codigoTipoIdentificacion) {
		this.codigoTipoIdentificacion = codigoTipoIdentificacion;
	}


	/**
	 * @return the nombreTipoIdentificacion
	 */
	public String getNombreTipoIdentificacion() {
		return nombreTipoIdentificacion;
	}


	/**
	 * @param nombreTipoIdentificacion the nombreTipoIdentificacion to set
	 */
	public void setNombreTipoIdentificacion(String nombreTipoIdentificacion) {
		this.nombreTipoIdentificacion = nombreTipoIdentificacion;
	}


	/**
	 * @return the ingresoEdad
	 */
	public boolean isIngresoEdad() {
		return ingresoEdad;
	}


	/**
	 * @param ingresoEdad the ingresoEdad to set
	 */
	public void setIngresoEdad(boolean ingresoEdad) {
		this.ingresoEdad = ingresoEdad;
	}


	/**
	 * @return the anios
	 */
	public String getAnios() {
		return anios;
	}


	/**
	 * @param anios the anios to set
	 */
	public void setAnios(String anios) {
		this.anios = anios;
	}


	/**
	 * @return the seleccionEdad
	 */
	public String getSeleccionEdad() {
		return seleccionEdad;
	}


	/**
	 * @param seleccionEdad the seleccionEdad to set
	 */
	public void setSeleccionEdad(String seleccionEdad) {
		this.seleccionEdad = seleccionEdad;
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
	
	
	
	
}
