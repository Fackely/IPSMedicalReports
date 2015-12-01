package com.servinte.axioma.dto.manejoPaciente;

import java.io.Serializable;
import java.util.List;

import net.sf.jasperreports.engine.JRDataSource;

/**
 * @author davgommo
 *
 */
public class UsuariosConsumidoresGeneralDto implements Serializable{


	/**
	 * 
	 */
	private static final long serialVersionUID = 8484794605205150972L;
	
	private long codigoEmpresaInstitucion=0;
	private String rutaLogo;
	private String ubicacionLogo;
	private String logoIzquierda;
	private String logoDerecha;
	private String nombreArchivoGenerado;
    private boolean saltoPaginaReporte;
    private String tipoImpresion;
	private int institucion=0;
	private String razonSocial="";
	private String nit="";
	private String direccion="";	
	private String telefono="";
	private String indicativo="";
	private String actividadEconomica="";	
	private String usuario="";	
	private String centroAtencion;
	private String criteriosBusqueda="";
	private String nombrePaciente="";
	private String nombreTipoIdentificacion="";
	private String numeroIdentificacion="";
	private int codigoPaciente=0;
	private double valorI=0;
	private double valorF=0;
	private long cantidadAutorizada=0;
	private long cantidadIngresos=0;
	private JRDataSource listaValoresGrupoServicio;
	private JRDataSource listaValoresClaseInventario;
	private double totalAutUsu;
	private double totalFacUsu;
	private double totalCantUsu;
	private String tipoSeleccion;
	private double granTotalCant;
	private double granTotalAut;
	private double granTotalFac;
	
	
	

	public UsuariosConsumidoresGeneralDto(){
	this.reset();
		}
	public void reset()
	{
				
	}
	/**
	 * @return the codigoEmpresaInstitucion
	 */
	public long getCodigoEmpresaInstitucion() {
		return codigoEmpresaInstitucion;
	}
	/**
	 * @param codigoEmpresaInstitucion the codigoEmpresaInstitucion to set
	 */
	public void setCodigoEmpresaInstitucion(long codigoEmpresaInstitucion) {
		this.codigoEmpresaInstitucion = codigoEmpresaInstitucion;
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
	 * @return the valorI
	 */
	public double getValorI() {
		return valorI;
	}
	/**
	 * @param valorI the valorI to set
	 */
	public void setValorI(double valorI) {
		this.valorI = valorI;
	}
	/**
	 * @return the valorF
	 */
	public double getValorF() {
		return valorF;
	}
	/**
	 * @param valorF the valorF to set
	 */
	public void setValorF(double valorF) {
		this.valorF = valorF;
	}
	public String getUsuario() {
		return usuario;
	}
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	/**
	 * @return the cantidadAutorizada
	 */
	public long getCantidadAutorizada() {
		return cantidadAutorizada;
	}
	/**
	 * @param cantidadAutorizada the cantidadAutorizada to set
	 */
	public void setCantidadAutorizada(long cantidadAutorizada) {
		this.cantidadAutorizada = cantidadAutorizada;
	}
	/**
	 * @return the cantidadIngresos
	 */
	public long getCantidadIngresos() {
		return cantidadIngresos;
	}
	/**
	 * @param cantidadIngresos the cantidadIngresos to set
	 */
	public void setCantidadIngresos(long cantidadIngresos) {
		this.cantidadIngresos = cantidadIngresos;
	}
	/**
	 * @return the NombrePaciente
	 */
	public String getNombrePaciente() {
		return nombrePaciente;
	}
	/**
	 * @param NombrePaciente the NombrePaciente to set
	 */
	public void setNombrePaciente(String NombrePaciente) {
		this.nombrePaciente = NombrePaciente;
	}
	/**
	 * @return the criteriosBusqueda
	 */
	public String getCriteriosBusqueda() {
		return criteriosBusqueda;
	}
	/**
	 * @param criteriosBusqueda the criteriosBusqueda to set
	 */
	public void setCriteriosBusqueda(String criteriosBusqueda) {
		this.criteriosBusqueda = criteriosBusqueda;
	}
	/**
	 * @return the listaValoresGrupoServicio
	 */
	public JRDataSource getListaValoresGrupoServicio() {
		return listaValoresGrupoServicio;
	}
	/**
	 * @param listaValoresGrupoServicio the listaValoresGrupoServicio to set
	 */
	public void setListaValoresGrupoServicio(JRDataSource listaValoresGrupoServicio) {
		this.listaValoresGrupoServicio = listaValoresGrupoServicio;
	}
	/**
	 * @return the listaValoresClaseInventario
	 */
	public JRDataSource getListaValoresClaseInventario() {
		return listaValoresClaseInventario;
	}
	/**
	 * @param listaValoresClaseInventario the listaValoresClaseInventario to set
	 */
	public void setListaValoresClaseInventario(
			JRDataSource listaValoresClaseInventario) {
		this.listaValoresClaseInventario = listaValoresClaseInventario;
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
	 * @return the totalAutUsu
	 */
	public double getTotalAutUsu() {
		return totalAutUsu;
	}
	/**
	 * @param totalAutUsu the totalAutUsu to set
	 */
	public void setTotalAutUsu(double totalAutUsu) {
		this.totalAutUsu = totalAutUsu;
	}
	/**
	 * @return the totalFacUsu
	 */
	public double getTotalFacUsu() {
		return totalFacUsu;
	}
	/**
	 * @param totalFacUsu the totalFacUsu to set
	 */
	public void setTotalFacUsu(double totalFacUsu) {
		this.totalFacUsu = totalFacUsu;
	}
	/**
	 * @return the totalCantUsu
	 */
	public double getTotalCantUsu() {
		return totalCantUsu;
	}
	/**
	 * @param totalCantUsu the totalCantUsu to set
	 */
	public void setTotalCantUsu(double totalCantUsu) {
		this.totalCantUsu = totalCantUsu;
	}
	/**
	 * @return the tipoSeleccion
	 */
	public String getTipoSeleccion() {
		return tipoSeleccion;
	}
	/**
	 * @param tipoSeleccion the tipoSeleccion to set
	 */
	public void setTipoSeleccion(String tipoSeleccion) {
		this.tipoSeleccion = tipoSeleccion;
	}
	/**
	 * @return the granTotalCant
	 */
	public double getGranTotalCant() {
		return granTotalCant;
	}
	/**
	 * @param granTotalCant the granTotalCant to set
	 */
	public void setGranTotalCant(double granTotalCant) {
		this.granTotalCant = granTotalCant;
	}
	/**
	 * @return the granTotalAut
	 */
	public double getGranTotalAut() {
		return granTotalAut;
	}
	/**
	 * @param granTotalAut the granTotalAut to set
	 */
	public void setGranTotalAut(double granTotalAut) {
		this.granTotalAut = granTotalAut;
	}
	/**
	 * @return the granTotalFac
	 */
	public double getGranTotalFac() {
		return granTotalFac;
	}
	/**
	 * @param granTotalFac the granTotalFac to set
	 */
	public void setGranTotalFac(double granTotalFac) {
		this.granTotalFac = granTotalFac;
	}


}