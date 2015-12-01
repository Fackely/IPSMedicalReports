package com.princetonsa.dto.manejoPaciente;

import java.io.Serializable;
import net.sf.jasperreports.engine.JRDataSource;




/**
 * Dto encargado de almacenar los datos del 
 * reporte de autorizaci&oacute;n de ingreso estancia
 * @author Diana Carolina G
 * @since 20/11/2010
 *
 */


public class DtoGeneralReporteIngresoEstancia implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	// Datos Generales del reporte ---------
	private String logoDerecha;
	private String logoIzquierda;
	private String datosEncabezado;
	private String datosPie;
	private String ubicacionLogo;
	private String rutaLogo;
	private String tipoReporteMediaCarta;
	// ------------------------------------
	
	
	// Datos Paciente -----------------------
	private String nombrePaciente;
	private String tipoDocPaciente;
	private String numeroDocPaciente;
	private String edadPaciente;
	private String convenioPaciente;
	private String tipoContratoPaciente;
	private String categoriaSocioEconomica;
	private String tipoAfiliado;
	private String semanasCotizacion;
	private String recobro;
	private String entidadRecobro;
	// ---------------------------------------
	
	// Datos Encabezado --------------------
	private String razonSocial;
	private String nit;
	private String direccion;	
	private String telefono;
	private String actividadEconomica;
	private String usuario;
	
	
	// Datos Admisi&oacute;n -------------------
	DTOReporteAutorizacionSeccionAdmision dtoAdmisionIngresoEstancia;
	
	// Datos Autorizaci&oacute;n----------------
	DTOReporteAutorizacionSeccionAutorizacion dtoAutorizacion;
	
    /** Objeto jasper para el subreporte de datos de admisión de ingreso estancia */
    transient private JRDataSource dsAdmisionIngresoEstancia;
	
	/** Objeto jasper para el subreporte de autorizaci&oacute;n */
    transient private JRDataSource dsAutorizacion;
    
    
    //-----------------------------------
	
    public DtoGeneralReporteIngresoEstancia(){
    	this.reset();
    }

    private void reset()
    {
    	this.razonSocial				= "";
    	this.nit						= "";
    	this.direccion					= "";
    	this.telefono					= "";
    	this.actividadEconomica			= "";
    	this.usuario					= "";
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


	public String getNombrePaciente() {
		return nombrePaciente;
	}


	public void setNombrePaciente(String nombrePaciente) {
		this.nombrePaciente = nombrePaciente;
	}


	public String getTipoDocPaciente() {
		return tipoDocPaciente;
	}


	public void setTipoDocPaciente(String tipoDocPaciente) {
		this.tipoDocPaciente = tipoDocPaciente;
	}


	public String getNumeroDocPaciente() {
		return numeroDocPaciente;
	}


	public void setNumeroDocPaciente(String numeroDocPaciente) {
		this.numeroDocPaciente = numeroDocPaciente;
	}


	public String getEdadPaciente() {
		return edadPaciente;
	}


	public void setEdadPaciente(String edadPaciente) {
		this.edadPaciente = edadPaciente;
	}


	public String getConvenioPaciente() {
		return convenioPaciente;
	}


	public void setConvenioPaciente(String convenioPaciente) {
		this.convenioPaciente = convenioPaciente;
	}


	public String getTipoContratoPaciente() {
		return tipoContratoPaciente;
	}


	public void setTipoContratoPaciente(String tipoContratoPaciente) {
		this.tipoContratoPaciente = tipoContratoPaciente;
	}


	public String getCategoriaSocioEconomica() {
		return categoriaSocioEconomica;
	}


	public void setCategoriaSocioEconomica(String categoriaSocioEconomica) {
		this.categoriaSocioEconomica = categoriaSocioEconomica;
	}


	public String getTipoAfiliado() {
		return tipoAfiliado;
	}


	public void setTipoAfiliado(String tipoAfiliado) {
		this.tipoAfiliado = tipoAfiliado;
	}


	public String getSemanasCotizacion() {
		return semanasCotizacion;
	}


	public void setSemanasCotizacion(String semanasCotizacion) {
		this.semanasCotizacion = semanasCotizacion;
	}


	public String getRecobro() {
		return recobro;
	}


	public void setRecobro(String recobro) {
		this.recobro = recobro;
	}


	public String getEntidadRecobro() {
		return entidadRecobro;
	}


	public void setEntidadRecobro(String entidadRecobro) {
		this.entidadRecobro = entidadRecobro;
	}


	public DTOReporteAutorizacionSeccionAdmision getDtoAdmisionIngresoEstancia() {
		return dtoAdmisionIngresoEstancia;
	}


	public void setDtoAdmisionIngresoEstancia(
			DTOReporteAutorizacionSeccionAdmision dtoAdmisionIngresoEstancia) {
		this.dtoAdmisionIngresoEstancia = dtoAdmisionIngresoEstancia;
	}


	public DTOReporteAutorizacionSeccionAutorizacion getDtoAutorizacion() {
		return dtoAutorizacion;
	}


	public void setDtoAutorizacion(
			DTOReporteAutorizacionSeccionAutorizacion dtoAutorizacion) {
		this.dtoAutorizacion = dtoAutorizacion;
	}


	public JRDataSource getDsAdmisionIngresoEstancia() {
		return dsAdmisionIngresoEstancia;
	}


	public void setDsAdmisionIngresoEstancia(JRDataSource dsAdmisionIngresoEstancia) {
		this.dsAdmisionIngresoEstancia = dsAdmisionIngresoEstancia;
	}


	public JRDataSource getDsAutorizacion() {
		return dsAutorizacion;
	}


	public void setDsAutorizacion(JRDataSource dsAutorizacion) {
		this.dsAutorizacion = dsAutorizacion;
	}


	public void setUbicacionLogo(String ubicacionLogo) {
		this.ubicacionLogo = ubicacionLogo;
	}


	public String getUbicacionLogo() {
		return ubicacionLogo;
	}


	public void setRutaLogo(String rutaLogo) {
		this.rutaLogo = rutaLogo;
	}


	public String getRutaLogo() {
		return rutaLogo;
	}


	public void setTipoReporteMediaCarta(String tipoReporteMediaCarta) {
		this.tipoReporteMediaCarta = tipoReporteMediaCarta;
	}


	public String getTipoReporteMediaCarta() {
		return tipoReporteMediaCarta;
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

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
    

	

	

	
		
}
