package com.servinte.axioma.generadorReporte.historiaClinica.curvasCrecimiento;

import java.util.List;

import net.sf.jasperreports.engine.JRDataSource;

/**
 * Dto con los datos de la Curva de Crecimiento a imprimir
 * @author hermorhu
 * @created 26-Oct-2012 
 */
public class CurvaCrecimientoDesarrolloDto{
	
	private String razonSocial;
	private String nit;
	private String actividadEconomica;
	private String direccion;
	private String telefono;
	private String centroAtencion;
	private String tipoReporte;
	
	private String logo;
	
	private String paciente;
	private String identificacion;
	private String fechaNacimiento;
	private String edad;
	private String sexo;
	private String estadoCivil;
	private String ocupacion;
	private String tipoAfiliacion;
	private String acompanantePaciente;
	private String telAcompanante;
	private String parentescoAcompanante;
	private String responsablePaciente;
	private String telResponsable;
	private String parentescoResponsable;
	private String viaIngreso;
	private String fechaIngreso;
	private String viaEgreso;
	private String fechaEgreso;
	private String cama;
	private String responsable;
	
	private List<SignosVitalesDto> listaSignosVitales;
	
	private String imagenIzquierda;
	private String imagenDerecha;
	private String tituloGrafica;
	private String descripcionGrafica;
	private String imagenCurva;
	
	private String firmaElectronica;
	
	private JRDataSource jRDSignosVitales;

	/**
	 * 
	 */
	public CurvaCrecimientoDesarrolloDto() {
		super();
	}

	/**
	 * @return the razonSocial
	 */
	public String getRazonSocial() {
		return razonSocial;
	}

	/**
	 * @param razonSocial the razonSocial to set
	 */
	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
	}

	/**
	 * @return the nit
	 */
	public String getNit() {
		return nit;
	}

	/**
	 * @param nit the nit to set
	 */
	public void setNit(String nit) {
		this.nit = nit;
	}

	/**
	 * @return the actividadEconomica
	 */
	public String getActividadEconomica() {
		return actividadEconomica;
	}

	/**
	 * @param actividadEconomica the actividadEconomica to set
	 */
	public void setActividadEconomica(String actividadEconomica) {
		this.actividadEconomica = actividadEconomica;
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
	 * @return the centroAtencion
	 */
	public String getCentroAtencion() {
		return centroAtencion;
	}

	/**
	 * @param centroAtencion the centroAtencion to set
	 */
	public void setCentroAtencion(String centroAtencion) {
		this.centroAtencion = centroAtencion;
	}

	/**
	 * @return the tipoReporte
	 */
	public String getTipoReporte() {
		return tipoReporte;
	}

	/**
	 * @param tipoReporte the tipoReporte to set
	 */
	public void setTipoReporte(String tipoReporte) {
		this.tipoReporte = tipoReporte;
	}

	/**
	 * @return the logo
	 */
	public String getLogo() {
		return logo;
	}

	/**
	 * @param logo the logo to set
	 */
	public void setLogo(String logo) {
		this.logo = logo;
	}

	/**
	 * @return the paciente
	 */
	public String getPaciente() {
		return paciente;
	}

	/**
	 * @param paciente the paciente to set
	 */
	public void setPaciente(String paciente) {
		this.paciente = paciente;
	}

	/**
	 * @return the identificacion
	 */
	public String getIdentificacion() {
		return identificacion;
	}

	/**
	 * @param identificacion the identificacion to set
	 */
	public void setIdentificacion(String identificacion) {
		this.identificacion = identificacion;
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
	 * @return the estadoCivil
	 */
	public String getEstadoCivil() {
		return estadoCivil;
	}

	/**
	 * @param estadoCivil the estadoCivil to set
	 */
	public void setEstadoCivil(String estadoCivil) {
		this.estadoCivil = estadoCivil;
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
	 * @return the tipoAfiliacion
	 */
	public String getTipoAfiliacion() {
		return tipoAfiliacion;
	}

	/**
	 * @param tipoAfiliacion the tipoAfiliacion to set
	 */
	public void setTipoAfiliacion(String tipoAfiliacion) {
		this.tipoAfiliacion = tipoAfiliacion;
	}

	/**
	 * @return the acompanantePaciente
	 */
	public String getAcompanantePaciente() {
		return acompanantePaciente;
	}

	/**
	 * @param acompanantePaciente the acompañantePaciente to set
	 */
	public void setAcompanantePaciente(String acompanantePaciente) {
		this.acompanantePaciente = acompanantePaciente;
	}

	/**
	 * @return the telAcompanante
	 */
	public String getTelAcompanante() {
		return telAcompanante;
	}

	/**
	 * @param telAcompanante the telAcompanante to set
	 */
	public void setTelAcompanante(String telAcompanante) {
		this.telAcompanante = telAcompanante;
	}

	/**
	 * @return the parentescoAcompanante
	 */
	public String getParentescoAcompanante() {
		return parentescoAcompanante;
	}

	/**
	 * @param parentescoAcompanante the parentescoAcompanante to set
	 */
	public void setParentescoAcompanante(String parentescoAcompanante) {
		this.parentescoAcompanante = parentescoAcompanante;
	}

	/**
	 * @return the responsablePaciente
	 */
	public String getResponsablePaciente() {
		return responsablePaciente;
	}

	/**
	 * @param responsablePaciente the responsablePaciente to set
	 */
	public void setResponsablePaciente(String responsablePaciente) {
		this.responsablePaciente = responsablePaciente;
	}

	/**
	 * @return the telResponsable
	 */
	public String getTelResponsable() {
		return telResponsable;
	}

	/**
	 * @param telResponsable the telResponsable to set
	 */
	public void setTelResponsable(String telResponsable) {
		this.telResponsable = telResponsable;
	}

	/**
	 * @return the parentescoResponsable
	 */
	public String getParentescoResponsable() {
		return parentescoResponsable;
	}

	/**
	 * @param parentescoResponsable the parentescoResponsable to set
	 */
	public void setParentescoResponsable(String parentescoResponsable) {
		this.parentescoResponsable = parentescoResponsable;
	}

	/**
	 * @return the viaIngreso
	 */
	public String getViaIngreso() {
		return viaIngreso;
	}

	/**
	 * @param viaIngreso the viaIngreso to set
	 */
	public void setViaIngreso(String viaIngreso) {
		this.viaIngreso = viaIngreso;
	}

	/**
	 * @return the fechaIngreso
	 */
	public String getFechaIngreso() {
		return fechaIngreso;
	}

	/**
	 * @param fechaIngreso the fechaIngreso to set
	 */
	public void setFechaIngreso(String fechaIngreso) {
		this.fechaIngreso = fechaIngreso;
	}

	/**
	 * @return the viaEgreso
	 */
	public String getViaEgreso() {
		return viaEgreso;
	}

	/**
	 * @param viaEgreso the viaEgreso to set
	 */
	public void setViaEgreso(String viaEgreso) {
		this.viaEgreso = viaEgreso;
	}

	/**
	 * @return the fechaEgreso
	 */
	public String getFechaEgreso() {
		return fechaEgreso;
	}

	/**
	 * @param fechaEgreso the fechaEgreso to set
	 */
	public void setFechaEgreso(String fechaEgreso) {
		this.fechaEgreso = fechaEgreso;
	}

	/**
	 * @return the cama
	 */
	public String getCama() {
		return cama;
	}

	/**
	 * @param cama the cama to set
	 */
	public void setCama(String cama) {
		this.cama = cama;
	}

	/**
	 * @return the responsable
	 */
	public String getResponsable() {
		return responsable;
	}

	/**
	 * @param responsable the responsable to set
	 */
	public void setResponsable(String responsable) {
		this.responsable = responsable;
	}

	/**
	 * @return the listaSignosVitales
	 */
	public List<SignosVitalesDto> getListaSignosVitales() {
		return listaSignosVitales;
	}

	/**
	 * @param listaSignosVitales the listaSignosVitales to set
	 */
	public void setListaSignosVitales(List<SignosVitalesDto> listaSignosVitales) {
		this.listaSignosVitales = listaSignosVitales;
	}

	/**
	 * @return the imagenIzquierda
	 */
	public String getImagenIzquierda() {
		return imagenIzquierda;
	}

	/**
	 * @param imagenIzquierda the imagenIzquierda to set
	 */
	public void setImagenIzquierda(String imagenIzquierda) {
		this.imagenIzquierda = imagenIzquierda;
	}

	/**
	 * @return the imagenDerecha
	 */
	public String getImagenDerecha() {
		return imagenDerecha;
	}

	/**
	 * @param imagenDerecha the imagenDerecha to set
	 */
	public void setImagenDerecha(String imagenDerecha) {
		this.imagenDerecha = imagenDerecha;
	}

	/**
	 * @return the tituloGrafica
	 */
	public String getTituloGrafica() {
		return tituloGrafica;
	}

	/**
	 * @param tituloGrafica the tituloGrafica to set
	 */
	public void setTituloGrafica(String tituloGrafica) {
		this.tituloGrafica = tituloGrafica;
	}

	/**
	 * @return the descripcionGrafica
	 */
	public String getDescripcionGrafica() {
		return descripcionGrafica;
	}

	/**
	 * @param descripcionGrafica the descripcionGrafica to set
	 */
	public void setDescripcionGrafica(String descripcionGrafica) {
		this.descripcionGrafica = descripcionGrafica;
	}

	/**
	 * @return the imagenCurva
	 */
	public String getImagenCurva() {
		return imagenCurva;
	}

	/**
	 * @param imagenCurva the imagenCurva to set
	 */
	public void setImagenCurva(String imagenCurva) {
		this.imagenCurva = imagenCurva;
	}

	/**
	 * @return the firmaElectronica
	 */
	public String getFirmaElectronica() {
		return firmaElectronica;
	}

	/**
	 * @param firmaElectronica the firmaElectronica to set
	 */
	public void setFirmaElectronica(String firmaElectronica) {
		this.firmaElectronica = firmaElectronica;
	}

	/**
	 * @return the jRDSignosVitales
	 */
	public JRDataSource getJRDSignosVitales() {
		return jRDSignosVitales;
	}

	/**
	 * @param jRDSignosVitales the jRDSignosVitales to set
	 */
	public void setJRDSignosVitales(JRDataSource jRDSignosVitales) {
		this.jRDSignosVitales = jRDSignosVitales;
	}
	
}
