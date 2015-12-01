package com.princetonsa.dto.manejoPaciente;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import com.servinte.axioma.orm.Convenios;
import com.servinte.axioma.orm.Cuentas;
import com.servinte.axioma.orm.ViasIngreso;

import net.sf.jasperreports.engine.JRDataSource;

public class DtoPacientesPoliconsultadores  implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	/** Informacion General*/
	private int institucion;
	private String razonSocial;
	private String nit;
	private String direccion;	
	private String telefono;
	private String indicativo;
	private String actividadEconomica;
	private String centroAtencion;
	private String tipoReporte;
	private String usuario;
	private String fechaInicial;
	private String fechaFinal;
	private String cantidadServicios;
	
	// datos para el convenio
	private String convenio;
	private String paciente;
	private String documento;
	private String cantidadIngresos;
	
	private int codigoConvenio;
	private int codigoViaIngreso;
	private int codigoPaciente;

	
	private String rutaLogo;
	private String ubicacionLogo;
	private String logoIzquierda;
	private String logoDerecha;
	private String nombreArchivoGenerado;
	private boolean saltoPaginaReporte;
    private String tipoImpresion;	
	
    private ArrayList<DtoPacientesPoliconsultadores>listaConvenios;
    private ArrayList<DtoDetallesPacientesPoliconsultadores>listaDetalles;
    
    private JRDataSource JRDConvenio;
    private JRDataSource JRDDetalles; 
    
    private DtoDetallesPacientesPoliconsultadores dtoDetalles;
    
    public DtoPacientesPoliconsultadores(){
    	
    }  
    
    public DtoPacientesPoliconsultadores( 
	    		String nombreConvenio,
	    		String primerNombrePersona,
	    		String segundoNombre,
	    		String primerApellido,
	    		String segundoApellido,
	    		String tipoIdentificacion,
	    		String numeroIdentificacion,
	    		int codigoConvenio,
	    		int codigoViaIngreso,
	    		int codigoPaciente,
	    		String nombreViasIngreso,
	    		String tipoServicio,
	    		int codigoEspecialidad,
	    		String nombreEspecialidad,
	    		String nombreUnidadAgenda,
	    		Number conteo 
	    		){
    	this.convenio = nombreConvenio;
    	if( primerNombrePersona  != null ){
    		this.paciente = primerNombrePersona.trim() + " ";
    	}
    	if( segundoNombre != null ){
    		this.paciente += segundoNombre.trim() + " ";
    	}
    	if( primerApellido  != null ){
    		this.paciente += primerApellido.trim() + " ";
    	}
    	if( segundoApellido  != null ){
    		this.paciente += segundoApellido.trim();
    	}    	
    	this.documento = tipoIdentificacion + " " + numeroIdentificacion;
    	this.cantidadIngresos = "0";
    	this.codigoConvenio = codigoConvenio;
    	this.codigoViaIngreso = codigoViaIngreso;
    	this.codigoPaciente = codigoPaciente;
    	dtoDetalles = new DtoDetallesPacientesPoliconsultadores();
    	dtoDetalles.setCodigoEspecialidad(codigoEspecialidad);
    	dtoDetalles.setNombreEspecialidad(nombreEspecialidad);
    	dtoDetalles.setNombreViasIngreso(nombreViasIngreso);
    	dtoDetalles.setTipoServicio(tipoServicio);
    	dtoDetalles.setCantidadIngresos(conteo.intValue());
    	dtoDetalles.setNombreUnidadAgenda(nombreUnidadAgenda);
    }
    
	/** 
	 * @return the institucion
	 */
	public int getInstitucion() {
		return institucion;
	}
	/**
	 * @param institucion the institucion to set
	 */
	public void setInstitucion(int institucion) {
		this.institucion = institucion;
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
	 * @return the indicativo
	 */
	public String getIndicativo() {
		return indicativo;
	}
	/**
	 * @param indicativo the indicativo to set
	 */
	public void setIndicativo(String indicativo) {
		this.indicativo = indicativo;
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
	 * @return the rutaLogo
	 */
	public String getRutaLogo() {
		return rutaLogo;
	}
	/**
	 * @param rutaLogo the rutaLogo to set
	 */
	public void setRutaLogo(String rutaLogo) {
		this.rutaLogo = rutaLogo;
	}
	/**
	 * @return the ubicacionLogo
	 */
	public String getUbicacionLogo() {
		return ubicacionLogo;
	}
	/**
	 * @param ubicacionLogo the ubicacionLogo to set
	 */
	public void setUbicacionLogo(String ubicacionLogo) {
		this.ubicacionLogo = ubicacionLogo;
	}
	/**
	 * @return the logoIzquierda
	 */
	public String getLogoIzquierda() {
		return logoIzquierda;
	}
	/**
	 * @param logoIzquierda the logoIzquierda to set
	 */
	public void setLogoIzquierda(String logoIzquierda) {
		this.logoIzquierda = logoIzquierda;
	}
	/**
	 * @return the logoDerecha
	 */
	public String getLogoDerecha() {
		return logoDerecha;
	}
	/**
	 * @param logoDerecha the logoDerecha to set
	 */
	public void setLogoDerecha(String logoDerecha) {
		this.logoDerecha = logoDerecha;
	}
	/**
	 * @return the nombreArchivoGenerado
	 */
	public String getNombreArchivoGenerado() {
		return nombreArchivoGenerado;
	}
	/**
	 * @param nombreArchivoGenerado the nombreArchivoGenerado to set
	 */
	public void setNombreArchivoGenerado(String nombreArchivoGenerado) {
		this.nombreArchivoGenerado = nombreArchivoGenerado;
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
	 * @return the saltoPaginaReporte
	 */
	public boolean isSaltoPaginaReporte() {
		return saltoPaginaReporte;
	}
	/**
	 * @param saltoPaginaReporte the saltoPaginaReporte to set
	 */
	public void setSaltoPaginaReporte(boolean saltoPaginaReporte) {
		this.saltoPaginaReporte = saltoPaginaReporte;
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
	/**
	 * @return the fechaInicial
	 */
	public String getFechaInicial() {
		return fechaInicial;
	}
	/**
	 * @param fechaInicial the fechaInicial to set
	 */
	public void setFechaInicial(String fechaInicial) {
		this.fechaInicial = fechaInicial;
	}
	/**
	 * @return the cantidadServicios
	 */
	public String getCantidadServicios() {
		return cantidadServicios;
	}
	/**
	 * @param cantidadServicios the cantidadServicios to set
	 */
	public void setCantidadServicios(String cantidadServicios) {
		this.cantidadServicios = cantidadServicios;
	}
	/**
	 * @return the fechaFinal
	 */
	public String getFechaFinal() {
		return fechaFinal;
	}
	/**
	 * @param fechaFinal the fechaFinal to set
	 */
	public void setFechaFinal(String fechaFinal) {
		this.fechaFinal = fechaFinal;
	}
	/**
	 * @return the listaConvenios
	 */
	public ArrayList<DtoPacientesPoliconsultadores> getListaConvenios() {
		return listaConvenios;
	}
	/**
	 * @param listaConvenios the listaConvenios to set
	 */
	public void setListaConvenios(ArrayList<DtoPacientesPoliconsultadores> listaConvenios) {
		this.listaConvenios = listaConvenios;
	}
	/**
	 * @return the jRDConvenio
	 */
	public JRDataSource getJRDConvenio() {
		return JRDConvenio;
	}
	/**
	 * @param jRDConvenio the jRDConvenio to set
	 */
	public void setJRDConvenio(JRDataSource jRDConvenio) {
		JRDConvenio = jRDConvenio;
	}
	/**
	 * @return the convenio
	 */
	public String getConvenio() {
		return convenio;
	}
	/**
	 * @param convenio the convenio to set
	 */
	public void setConvenio(String convenio) {
		this.convenio = convenio;
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
	 * @return the documento
	 */
	public String getDocumento() {
		return documento;
	}
	/**
	 * @param documento the documento to set
	 */
	public void setDocumento(String documento) {
		this.documento = documento;
	}
	/**
	 * @return the cantidadIngreso
	 */
	public String getCantidadIngresos() {
		return cantidadIngresos;
	}
	/**
	 * @param cantidadIngreso the cantidadIngreso to set
	 */
	public void setCantidadIngresos(String cantidadIngresos) {
		this.cantidadIngresos = cantidadIngresos;
	}

	/**
	 * @return the listaDetalles
	 */
	public ArrayList<DtoDetallesPacientesPoliconsultadores> getListaDetalles() {
		return listaDetalles;
	}

	/**
	 * @param listaDetalles the listaDetalles to set
	 */
	public void setListaDetalles(ArrayList<DtoDetallesPacientesPoliconsultadores> listaDetalles) {
		this.listaDetalles = listaDetalles;
	}
	

	/**
	 * @return the codigoConvenio
	 */
	public int getCodigoConvenio() {
		return codigoConvenio;
	}

	/**
	 * @param codigoConvenio the codigoConvenio to set
	 */
	public void setCodigoConvenio(int codigoConvenio) {
		this.codigoConvenio = codigoConvenio;
	}

	/**
	 * @return the codigoViaIngreso
	 */
	public int getCodigoViaIngreso() {
		return codigoViaIngreso;
	}

	/**
	 * @param codigoViaIngreso the codigoViaIngreso to set
	 */
	public void setCodigoViaIngreso(int codigoViaIngreso) {
		this.codigoViaIngreso = codigoViaIngreso;
	}

	/**
	 * @return the codigoPaciente
	 */
	public int getCodigoPaciente() {
		return codigoPaciente;
	}

	/**
	 * @param codigoPaciente the codigoPaciente to set
	 */
	public void setCodigoPaciente(int codigoPaciente) {
		this.codigoPaciente = codigoPaciente;
	}

	/**
	 * @return the dtoDetalles
	 */
	public DtoDetallesPacientesPoliconsultadores getDtoDetalles() {
		return dtoDetalles;
	}

	/**
	 * @param dtoDetalles the dtoDetalles to set
	 */
	public void setDtoDetalles(DtoDetallesPacientesPoliconsultadores dtoDetalles) {
		this.dtoDetalles = dtoDetalles;
	}

	/**
	 * @return the jRDDetalles
	 */
	public JRDataSource getJRDDetalles() {
		return JRDDetalles;
	}

	/**
	 * @param jRDDetalles the jRDDetalles to set
	 */
	public void setJRDDetalles(JRDataSource jRDDetalles) {
		JRDDetalles = jRDDetalles;
	}
}
