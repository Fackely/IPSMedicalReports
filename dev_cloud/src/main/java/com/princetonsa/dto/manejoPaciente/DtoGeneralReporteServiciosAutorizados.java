package com.princetonsa.dto.manejoPaciente;

import java.io.Serializable;
import java.util.ArrayList;

import net.sf.jasperreports.engine.JRDataSource;

import com.princetonsa.dto.inventario.DtoServiciosAutorizaciones;

/**
 * Dto encargado de almacenar los datos del 
 * reporte de autorizaci&oacute;n de servicios
 * @author Diana Carolina G
 * @since 20/11/2010
 *
 */


public class DtoGeneralReporteServiciosAutorizados implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	// Datos Generales del reporte ---------
	private String logoDerecha;
	private String logoIzquierda;
	private String rutaLogo;
	private String datosEncabezado;
	private String datosPie;
	private String tipoReporteMediaCarta;
	private String ubicacionLogo;
	private String razonSocial;
	private String nit;
	private String direccion;
	private String telefono;
	private String actividadEconomica;
	private String usuario;
	private String observaciones;
	private ArrayList<DtoServiciosAutorizaciones> listaServicios;
	
	private String tipoImpresion;
	private String fechaHoraEntrega;
	// ------------------------------------
	
	
	// Datos Paciente -----------------------
	DTOReporteAutorizacionSeccionPaciente dtoPaciente;

	// Datos Autorizaci&oacute;n----------------
	DTOReporteAutorizacionSeccionAutorizacion dtoAutorizacion;
	
	/** Objeto jasper para el subreporte de la sección de paciente */
    transient private JRDataSource dsPaciente;
		
	/** Objeto jasper para el subreporte de autorizaci&oacute;n */
    transient private JRDataSource dsAutorizacion;
	// ---------------------------------------
    
    
    //Datos Detalle Servicios Autorizados
    /** Objeto jasper para el subreporte de servicios autorizados */
    transient private JRDataSource dsAutorizacionServicios;
    
	
	public DtoGeneralReporteServiciosAutorizados(){
		this.reset();
	}

	private void reset()
	{
		this.razonSocial		= "";
		this.nit				= "";
		this.direccion			= "";	
		this.telefono			= "";
		this.actividadEconomica	= "";
		this.usuario			= "";
		this.observaciones		= "";
		
		this.tipoImpresion = "";
		this.fechaHoraEntrega = "";
	}
	
	public String getLogoDerecha() {
		return logoDerecha;
	}

	public void setLogoDerecha(String logoDerecha) {
		this.logoDerecha = logoDerecha;
	}

	public String getLogoIzquierda() {
		return logoIzquierda;
	}

	public void setLogoIzquierda(String logoIzquierda) {
		this.logoIzquierda = logoIzquierda;
	}

	public String getDatosEncabezado() {
		return datosEncabezado;
	}

	public void setDatosEncabezado(String datosEncabezado) {
		this.datosEncabezado = datosEncabezado;
	}

	public String getDatosPie() {
		return datosPie;
	}

	public void setDatosPie(String datosPie) {
		this.datosPie = datosPie;
	}	

	public JRDataSource getDsAutorizacion() {
		return dsAutorizacion;
	}

	public void setDsAutorizacion(JRDataSource dsAutorizacion) {
		this.dsAutorizacion = dsAutorizacion;
	}

	public void setDsAutorizacionServicios(JRDataSource dsAutorizacionServicios) {
		this.dsAutorizacionServicios = dsAutorizacionServicios;
	}

	public JRDataSource getDsAutorizacionServicios() {
		return dsAutorizacionServicios;
	}

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo tipoReporteMediaCarta
	
	 * @return retorna la variable tipoReporteMediaCarta 
	 * @author Angela Maria Aguirre 
	 */
	public String getTipoReporteMediaCarta() {
		return tipoReporteMediaCarta;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo tipoReporteMediaCarta
	
	 * @param valor para el atributo tipoReporteMediaCarta 
	 * @author Angela Maria Aguirre 
	 */
	public void setTipoReporteMediaCarta(String tipoReporteMediaCarta) {
		this.tipoReporteMediaCarta = tipoReporteMediaCarta;
	}

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo listaServicios
	
	 * @return retorna la variable listaServicios 
	 * @author Angela Maria Aguirre 
	 */
	public ArrayList<DtoServiciosAutorizaciones> getListaServicios() {
		return listaServicios;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo listaServicios
	
	 * @param valor para el atributo listaServicios 
	 * @author Angela Maria Aguirre 
	 */
	public void setListaServicios(
			ArrayList<DtoServiciosAutorizaciones> listaServicios) {
		this.listaServicios = listaServicios;
	}

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo dtoPaciente
	
	 * @return retorna la variable dtoPaciente 
	 * @author Angela Maria Aguirre 
	 */
	public DTOReporteAutorizacionSeccionPaciente getDtoPaciente() {
		return dtoPaciente;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo dtoPaciente
	
	 * @param valor para el atributo dtoPaciente 
	 * @author Angela Maria Aguirre 
	 */
	public void setDtoPaciente(DTOReporteAutorizacionSeccionPaciente dtoPaciente) {
		this.dtoPaciente = dtoPaciente;
	}

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo dtoAutorizacion
	
	 * @return retorna la variable dtoAutorizacion 
	 * @author Angela Maria Aguirre 
	 */
	public DTOReporteAutorizacionSeccionAutorizacion getDtoAutorizacion() {
		return dtoAutorizacion;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo dtoAutorizacion
	
	 * @param valor para el atributo dtoAutorizacion 
	 * @author Angela Maria Aguirre 
	 */
	public void setDtoAutorizacion(
			DTOReporteAutorizacionSeccionAutorizacion dtoAutorizacion) {
		this.dtoAutorizacion = dtoAutorizacion;
	}

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo dsPaciente
	
	 * @return retorna la variable dsPaciente 
	 * @author Angela Maria Aguirre 
	 */
	public JRDataSource getDsPaciente() {
		return dsPaciente;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo dsPaciente
	
	 * @param valor para el atributo dsPaciente 
	 * @author Angela Maria Aguirre 
	 */
	public void setDsPaciente(JRDataSource dsPaciente) {
		this.dsPaciente = dsPaciente;
	}

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo rutaLogo
	
	 * @return retorna la variable rutaLogo 
	 * @author Angela Maria Aguirre 
	 */
	public String getRutaLogo() {
		return rutaLogo;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo rutaLogo
	
	 * @param valor para el atributo rutaLogo 
	 * @author Angela Maria Aguirre 
	 */
	public void setRutaLogo(String rutaLogo) {
		this.rutaLogo = rutaLogo;
	}

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo ubicacionLogo
	
	 * @return retorna la variable ubicacionLogo 
	 * @author Angela Maria Aguirre 
	 */
	public String getUbicacionLogo() {
		return ubicacionLogo;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo ubicacionLogo
	
	 * @param valor para el atributo ubicacionLogo 
	 * @author Angela Maria Aguirre 
	 */
	public void setUbicacionLogo(String ubicacionLogo) {
		this.ubicacionLogo = ubicacionLogo;
	}

	public String getRazonSocial() {
		return razonSocial;
	}

	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
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

	public String getActividadEconomica() {
		return actividadEconomica;
	}

	public void setActividadEconomica(String actividadEconomica) {
		this.actividadEconomica = actividadEconomica;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public String getObservaciones() {
		return observaciones;
	}
		
	/**
	 * @return the tipoImpresion
	 */
	public String getTipoImpresion() {
		return tipoImpresion;
	}

	/**
	 * @param tipoImpresion the tipoImpresion to set
	 */
	public void setTipoImpresion(String tipoImpresion) {
		this.tipoImpresion = tipoImpresion;
	}

	/**
	 * @return the fechaHoraEntrega
	 */
	public String getFechaHoraEntrega() {
		return fechaHoraEntrega;
	}

	/**
	 * @param fechaHoraEntrega the fechaHoraEntrega to set
	 */
	public void setFechaHoraEntrega(String fechaHoraEntrega) {
		this.fechaHoraEntrega = fechaHoraEntrega;
	}
		
}
