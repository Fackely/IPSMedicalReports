package com.princetonsa.dto.salas;

import java.io.Serializable;
import java.util.List;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import com.princetonsa.dto.salasCirugia.DtoProfesionalesCirugia;

public class DtoPeticion implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8017065014390916245L;
	
	/**/
	private String logoIzquierda;
	
	/*INFO INSTITUCION*/
	private String nombreInstitucion;
	private String nit;
	private String actividadEconomica;
	private String centroAtencion;
	private String direccion;
	private String telefono;	
	
	/*INFO PACIENTE*/
	private String paciente;
	private String ingreso;
	private String cuenta;
	private String tipoAfiliado;
	private String categoria;
	private String convenio;
	private String diagnostico;
	
	/*INFO CIRUGIA*/
	private String nroPeticion;
	private String fechaHoraPeticion;
	private int codigoEstadoPeticion;
	private String estadoPeticion;
	private String fechaEstimadaCirugia;
	private String duracion;
	private String urgente;
	private String tipoPaciente;
	private String tipoAnestecia;
	private String requiereUCI;
	private String centroAtencionSolicitante;
	private String profesionalSolicitante;
	private String observaciones;
	
	private String usuario;
	
	/*INFO SERVICIOS*/
	private List<DtoServicioAsociado>serviciosAsociados;
	private JRDataSource dsServiciosAsociados;
	
	/*INFO PROFESIONALES*/
	private List<DtoProfesionalesCirugia>profesionales;
	 private JRDataSource dsProfesionales;

	/*INFO MATERIALES*/
	private List<DtoMaterial>materiales;
	 private JRDataSource dsMateriales;


	public DtoPeticion() {
		// TODO Auto-generated constructor stub
	}

	
	/**
	 * Constructor para saber el codigo y estado de una peticion
	 * @param codigoEstadoPeticion
	 * @param estadoPeticion
	 * @author jeilones
	 * @created 17/08/2012
	 */
	public DtoPeticion(int codigoEstadoPeticion, String estadoPeticion) {
		super();
		this.codigoEstadoPeticion = codigoEstadoPeticion;
		this.estadoPeticion = estadoPeticion;
	}

	public String getPaciente() {
		return paciente;
	}


	public void setPaciente(String paciente) {
		this.paciente = paciente;
	}


	public String getIngreso() {
		return ingreso;
	}


	public void setIngreso(String ingreso) {
		this.ingreso = ingreso;
	}


	public String getCuenta() {
		return cuenta;
	}


	public void setCuenta(String cuenta) {
		this.cuenta = cuenta;
	}


	public String getTipoAfiliado() {
		return tipoAfiliado;
	}


	public void setTipoAfiliado(String tipoAfiliado) {
		this.tipoAfiliado = tipoAfiliado;
	}


	public String getCategoria() {
		return categoria;
	}


	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}


	public String getConvenio() {
		return convenio;
	}


	public void setConvenio(String convenio) {
		this.convenio = convenio;
	}


	public String getDiagnostico() {
		return diagnostico;
	}


	public void setDiagnostico(String diagnostico) {
		this.diagnostico = diagnostico;
	}


	public String getNroPeticion() {
		return nroPeticion;
	}


	public void setNroPeticion(String nroPeticion) {
		this.nroPeticion = nroPeticion;
	}


	public String getFechaHoraPeticion() {
		return fechaHoraPeticion;
	}


	public void setFechaHoraPeticion(String fechaHoraPeticion) {
		this.fechaHoraPeticion = fechaHoraPeticion;
	}


	public String getEstadoPeticion() {
		return estadoPeticion;
	}


	public void setEstadoPeticion(String estadoPeticion) {
		this.estadoPeticion = estadoPeticion;
	}


	public String getFechaEstimadaCirugia() {
		return fechaEstimadaCirugia;
	}


	public void setFechaEstimadaCirugia(String fechaEstimadaCirugia) {
		this.fechaEstimadaCirugia = fechaEstimadaCirugia;
	}


	public String getDuracion() {
		return duracion;
	}


	public void setDuracion(String duracion) {
		this.duracion = duracion;
	}


	public String getUrgente() {
		return urgente;
	}


	public void setUrgente(String urgente) {
		this.urgente = urgente;
	}


	public String getTipoPaciente() {
		return tipoPaciente;
	}


	public void setTipoPaciente(String tipoPaciente) {
		this.tipoPaciente = tipoPaciente;
	}


	public String getTipoAnestecia() {
		return tipoAnestecia;
	}


	public void setTipoAnestecia(String tipoAnestecia) {
		this.tipoAnestecia = tipoAnestecia;
	}


	public String getRequiereUCI() {
		return requiereUCI;
	}


	public void setRequiereUCI(String requiereUCI) {
		this.requiereUCI = requiereUCI;
	}


	public String getCentroAtencionSolicitante() {
		return centroAtencionSolicitante;
	}


	public void setCentroAtencionSolicitante(String centroAtencionSolicitante) {
		this.centroAtencionSolicitante = centroAtencionSolicitante;
	}


	public String getProfesionalSolicitante() {
		return profesionalSolicitante;
	}


	public void setProfesionalSolicitante(String profesionalSolitante) {
		this.profesionalSolicitante = profesionalSolitante;
	}


	public List<DtoServicioAsociado> getServiciosAsociados() {
		return serviciosAsociados;
	}


	public void setServiciosAsociados(List<DtoServicioAsociado> serviciosAsociados) {
		this.serviciosAsociados = serviciosAsociados;
		this.dsServiciosAsociados=new JRBeanCollectionDataSource(this.serviciosAsociados);
	}


	public List<DtoProfesionalesCirugia> getProfesionales() {
		return profesionales;
	}


	public void setProfesionales(List<DtoProfesionalesCirugia> profesionales) {
		this.profesionales = profesionales;
		this.dsProfesionales=new JRBeanCollectionDataSource(this.profesionales);
	}


	public List<DtoMaterial> getMateriales() {
		return materiales;
	}


	public void setMateriales(List<DtoMaterial> materiales) {
		this.materiales = materiales;
		this.dsMateriales=new JRBeanCollectionDataSource(this.materiales);
	}


	public String getObservaciones() {
		return observaciones;
	}


	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}


	public JRDataSource getDsServiciosAsociados() {
		return dsServiciosAsociados;
	}


	public void setDsServiciosAsociados(JRDataSource dsServiciosAsociados) {
		this.dsServiciosAsociados = dsServiciosAsociados;
	}


	public JRDataSource getDsProfesionales() {
		return dsProfesionales;
	}


	public void setDsProfesionales(JRDataSource dsProfesionales) {
		this.dsProfesionales = dsProfesionales;
	}


	public JRDataSource getDsMateriales() {
		return dsMateriales;
	}


	public void setDsMateriales(JRDataSource dsMateriales) {
		this.dsMateriales = dsMateriales;
	}


	public String getNombreInstitucion() {
		return nombreInstitucion;
	}


	public void setNombreInstitucion(String nombre) {
		this.nombreInstitucion = nombre;
	}


	public String getNit() {
		return nit;
	}


	public void setNit(String nit) {
		this.nit = nit;
	}


	public String getActividadEconomica() {
		return actividadEconomica;
	}


	public void setActividadEconomica(String actividadEconomica) {
		this.actividadEconomica = actividadEconomica;
	}


	public String getCentroAtencion() {
		return centroAtencion;
	}


	public void setCentroAtencion(String centroAtencion) {
		this.centroAtencion = centroAtencion;
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


	public String getLogoIzquierda() {
		return logoIzquierda;
	}


	public void setLogoIzquierda(String logoIzquierda) {
		this.logoIzquierda = logoIzquierda;
	}


	public String getUsuario() {
		return usuario;
	}


	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}


	/**
	 * @return the codigoEstadoPeticion
	 */
	public int getCodigoEstadoPeticion() {
		return codigoEstadoPeticion;
	}


	/**
	 * @param codigoEstadoPeticion the codigoEstadoPeticion to set
	 */
	public void setCodigoEstadoPeticion(int codigoEstadoPeticion) {
		this.codigoEstadoPeticion = codigoEstadoPeticion;
	}


	

}
