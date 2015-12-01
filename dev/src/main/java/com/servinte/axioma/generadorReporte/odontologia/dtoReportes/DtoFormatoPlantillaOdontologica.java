package com.servinte.axioma.generadorReporte.odontologia.dtoReportes;

import java.io.Serializable;

import net.sf.jasperreports.engine.JRDataSource;

/**
 * Esta clase se encarga de recibir los datos que se crean en el generador del formato pdf de plantillas
 * odontológicas para crear y organizar el reporte de las plantillas de la atención de la cita 
 * 
 * @author Fabian Becerra
 *
 */
public class DtoFormatoPlantillaOdontologica  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	//ENCABEZADO
	private String usuarioProcesa;
	private String razonSocial;
	private String logoIzquierda;
	private String logoDerecha;
	private String centroFechaHoraActual;
	private String nit;
	private String direccion;
	private String telefono;
	private String descripcionCentroAtencion;
	private String titulo;
	private String tituloTipoCita;
	
	//DATOS PACIENTE
	private String nombreCompletoPaciente;
	private String identificacionPaciente;
	private String fechaNacimientoPaciente;
	private String edadPaciente;
	private String sexoPaciente;
	private String direccionPaciente;
	private String ciudadResidenciaPaciente;
	private String telefonoFijoPaciente;
	private String telefonoCelularPaciente;
	private String fechaInicioTratamientoPaciente;
	private String fechaFinTratamientoPaciente;
	private String servicio;
	private String responsable;
	
	//PIE DE PAGINA
	private String piePaginaHistoriaClinica;
	private String datosProfesional;
	
	
	/** Objeto jasper para el subreporte del consolidado */
    private JRDataSource dsPlantillaOdonto;
	
	public void setUsuarioProcesa(String usuarioProcesa) {
		this.usuarioProcesa = usuarioProcesa;
	}
	public String getUsuarioProcesa() {
		return usuarioProcesa;
	}
	public String getLogoIzquierda() {
		return logoIzquierda;
	}
	public void setLogoIzquierda(String logoIzquierda) {
		this.logoIzquierda = logoIzquierda;
	}
	public String getLogoDerecha() {
		return logoDerecha;
	}
	public void setLogoDerecha(String logoDerecha) {
		this.logoDerecha = logoDerecha;
	}
	public void setDsPlantillaOdonto(JRDataSource dsPlantillaOdonto) {
		this.dsPlantillaOdonto = dsPlantillaOdonto;
	}
	public JRDataSource getDsPlantillaOdonto() {
		return dsPlantillaOdonto;
	}
	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
	}
	public String getRazonSocial() {
		return razonSocial;
	}
	public void setCentroFechaHoraActual(String centroFechaHoraActual) {
		this.centroFechaHoraActual = centroFechaHoraActual;
	}
	public String getCentroFechaHoraActual() {
		return centroFechaHoraActual;
	}
	public String getNit() {
		return nit;
	}
	public void setNit(String nit) {
		this.nit = nit;
	}
	public String getDireccion() {
		return direccion;
	}
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}
	public String getTelefono() {
		return telefono;
	}
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
	public String getDescripcionCentroAtencion() {
		return descripcionCentroAtencion;
	}
	public void setDescripcionCentroAtencion(String descripcionCentroAtencion) {
		this.descripcionCentroAtencion = descripcionCentroAtencion;
	}
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	public String getTitulo() {
		return titulo;
	}
	public String getNombreCompletoPaciente() {
		return nombreCompletoPaciente;
	}
	public void setNombreCompletoPaciente(String nombreCompletoPaciente) {
		this.nombreCompletoPaciente = nombreCompletoPaciente;
	}
	public String getIdentificacionPaciente() {
		return identificacionPaciente;
	}
	public void setIdentificacionPaciente(String identificacionPaciente) {
		this.identificacionPaciente = identificacionPaciente;
	}
	public String getFechaNacimientoPaciente() {
		return fechaNacimientoPaciente;
	}
	public void setFechaNacimientoPaciente(String fechaNacimientoPaciente) {
		this.fechaNacimientoPaciente = fechaNacimientoPaciente;
	}
	public String getEdadPaciente() {
		return edadPaciente;
	}
	public void setEdadPaciente(String edadPaciente) {
		this.edadPaciente = edadPaciente;
	}
	public String getSexoPaciente() {
		return sexoPaciente;
	}
	public void setSexoPaciente(String sexoPaciente) {
		this.sexoPaciente = sexoPaciente;
	}
	public String getDireccionPaciente() {
		return direccionPaciente;
	}
	public void setDireccionPaciente(String direccionPaciente) {
		this.direccionPaciente = direccionPaciente;
	}
	public String getCiudadResidenciaPaciente() {
		return ciudadResidenciaPaciente;
	}
	public void setCiudadResidenciaPaciente(String ciudadResidenciaPaciente) {
		this.ciudadResidenciaPaciente = ciudadResidenciaPaciente;
	}
	public String getTelefonoFijoPaciente() {
		return telefonoFijoPaciente;
	}
	public void setTelefonoFijoPaciente(String telefonoFijoPaciente) {
		this.telefonoFijoPaciente = telefonoFijoPaciente;
	}
	public String getTelefonoCelularPaciente() {
		return telefonoCelularPaciente;
	}
	public void setTelefonoCelularPaciente(String telefonoCelularPaciente) {
		this.telefonoCelularPaciente = telefonoCelularPaciente;
	}
	public String getFechaInicioTratamientoPaciente() {
		return fechaInicioTratamientoPaciente;
	}
	public void setFechaInicioTratamientoPaciente(
			String fechaInicioTratamientoPaciente) {
		this.fechaInicioTratamientoPaciente = fechaInicioTratamientoPaciente;
	}
	public String getFechaFinTratamientoPaciente() {
		return fechaFinTratamientoPaciente;
	}
	public void setFechaFinTratamientoPaciente(String fechaFinTratamientoPaciente) {
		this.fechaFinTratamientoPaciente = fechaFinTratamientoPaciente;
	}
	public String getServicio() {
		return servicio;
	}
	public void setServicio(String servicio) {
		this.servicio = servicio;
	}
	public String getResponsable() {
		return responsable;
	}
	public void setResponsable(String responsable) {
		this.responsable = responsable;
	}
	public void setPiePaginaHistoriaClinica(String piePaginaHistoriaClinica) {
		this.piePaginaHistoriaClinica = piePaginaHistoriaClinica;
	}
	public String getPiePaginaHistoriaClinica() {
		return piePaginaHistoriaClinica;
	}
	public void setTituloTipoCita(String tituloTipoCita) {
		this.tituloTipoCita = tituloTipoCita;
	}
	public String getTituloTipoCita() {
		return tituloTipoCita;
	}
	public void setDatosProfesional(String datosProfesional) {
		this.datosProfesional = datosProfesional;
	}
	public String getDatosProfesional() {
		return datosProfesional;
	}
	

	
}

