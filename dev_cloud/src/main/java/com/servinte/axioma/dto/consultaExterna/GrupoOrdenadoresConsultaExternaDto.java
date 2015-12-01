/**
 * 
 */
package com.servinte.axioma.dto.consultaExterna;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import net.sf.jasperreports.engine.JRDataSource;

import com.servinte.axioma.dto.administracion.ProfesionalSaludDto;
import com.servinte.axioma.dto.facturacion.GrupoServicioDto;
import com.servinte.axioma.dto.inventario.ClaseInventarioDto;

/**
 * @author jeilones
 * @created 13/11/2012
 *
 */
public class GrupoOrdenadoresConsultaExternaDto implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2547252233419074902L;
	
	/** Informacion General **/
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
	
	private String rutaLogo;
	private String ubicacionLogo;
	private String logoIzquierda;
	private String logoDerecha;
	private String nombreArchivoGenerado;
	private boolean saltoPaginaReporte;
	
	/** Fin Informacion General **/
    
    /**FILTROS DE BUSQUEDA**/
	private int codigoTipoReporte;
    private int codigoTipoImpresion;
    
    
    private Date fechaInicialAtencionCita;
	private Date fechaFinalAtencionCita;
	
	private String filtroFechaInicialAtencionCita;
	private String filtroFechaFinalAtencionCita;
	private String filtroIdCentroAtencion;
	private String filtroIdUnidadConsulta;
	private String filtroIdConvenio;
	private ProfesionalSaludDto filtroProfesionalSalud;
	private String filtroIdProfesionalSalud;
	private String filtroIdGrupoServicio;
	private String filtroIdClaseInventario;
	
	private String filtroCentroAtencion;
	private String unidadesConsulta;
	private String convenio;
	private String profesionalSalud;
	private String grupoServicio;
	private String claseInventario;
    
    /**FIN DE FILTROS DE BUSQUEDA**/
	
	private List<ProfesionalSaludDto>profesionales;
	private JRDataSource grupoProfesionales;
	
	private List<OrdenadoresConsultaExternaPlanoDto> ordenadoresConsultaExternaPlano;
	private JRDataSource grupoOrdenadoresConsultaExternaPlano;
	
	
	private List<GrupoServicioDto>gruposServicio;
	private JRDataSource grupoGruposServicio;
	
	private List<ClaseInventarioDto>clasesInventario;
	private JRDataSource grupoClasesInventario;
	
	/**
	 * 
	 * @author jeilones
	 * @created 13/11/2012
	 */
	public GrupoOrdenadoresConsultaExternaDto() {
		super();
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
	 * @return the profesionales
	 */
	public List<ProfesionalSaludDto> getProfesionales() {
		return profesionales;
	}
	/**
	 * @param profesionales the profesionales to set
	 */
	public void setProfesionales(List<ProfesionalSaludDto> profesionales) {
		this.profesionales = profesionales;
	}
	/**
	 * @return the grupoProfesionales
	 */
	public JRDataSource getGrupoProfesionales() {
		return grupoProfesionales;
	}
	/**
	 * @param grupoProfesionales the grupoProfesionales to set
	 */
	public void setGrupoProfesionales(JRDataSource grupoProfesionales) {
		this.grupoProfesionales = grupoProfesionales;
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
	 * @return the fechaInicialAtencionCita
	 */
	public Date getFechaInicialAtencionCita() {
		return fechaInicialAtencionCita;
	}
	/**
	 * @param fechaInicialAtencionCita the fechaInicialAtencionCita to set
	 */
	public void setFechaInicialAtencionCita(Date fechaInicialAtencionCita) {
		this.fechaInicialAtencionCita = fechaInicialAtencionCita;
	}
	/**
	 * @return the fechaFinalAtencionCita
	 */
	public Date getFechaFinalAtencionCita() {
		return fechaFinalAtencionCita;
	}
	/**
	 * @param fechaFinalAtencionCita the fechaFinalAtencionCita to set
	 */
	public void setFechaFinalAtencionCita(Date fechaFinalAtencionCita) {
		this.fechaFinalAtencionCita = fechaFinalAtencionCita;
	}
	/**
	 * @return the filtroCentroAtencion
	 */
	public String getFiltroCentroAtencion() {
		return filtroCentroAtencion;
	}
	/**
	 * @param filtroCentroAtencion the filtroCentroAtencion to set
	 */
	public void setFiltroCentroAtencion(String filtroCentroAtencion) {
		this.filtroCentroAtencion = filtroCentroAtencion;
	}
	/**
	 * @return the unidadesConsulta
	 */
	public String getUnidadesConsulta() {
		return unidadesConsulta;
	}
	/**
	 * @param unidadesConsulta the unidadesConsulta to set
	 */
	public void setUnidadesConsulta(String unidadesConsulta) {
		this.unidadesConsulta = unidadesConsulta;
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
	 * @return the profesionalSalud
	 */
	public String getProfesionalSalud() {
		return profesionalSalud;
	}
	/**
	 * @param profesionalSalud the profesionalSalud to set
	 */
	public void setProfesionalSalud(String profesionalSalud) {
		this.profesionalSalud = profesionalSalud;
	}
	/**
	 * @return the grupoServicio
	 */
	public String getGrupoServicio() {
		return grupoServicio;
	}
	/**
	 * @param grupoServicio the grupoServicio to set
	 */
	public void setGrupoServicio(String grupoServicio) {
		this.grupoServicio = grupoServicio;
	}
	/**
	 * @return the claseInventario
	 */
	public String getClaseInventario() {
		return claseInventario;
	}
	/**
	 * @param claseInventario the claseInventario to set
	 */
	public void setClaseInventario(String claseInventario) {
		this.claseInventario = claseInventario;
	}
	/**
	 * @return the filtroIdCentroAtencion
	 */
	public String getFiltroIdCentroAtencion() {
		return filtroIdCentroAtencion;
	}
	/**
	 * @param filtroIdCentroAtencion the filtroIdCentroAtencion to set
	 */
	public void setFiltroIdCentroAtencion(String filtroIdCentroAtencion) {
		this.filtroIdCentroAtencion = filtroIdCentroAtencion;
	}
	/**
	 * @return the filtroIdUnidadConsulta
	 */
	public String getFiltroIdUnidadConsulta() {
		return filtroIdUnidadConsulta;
	}
	/**
	 * @param filtroIdUnidadConsulta the filtroIdUnidadConsulta to set
	 */
	public void setFiltroIdUnidadConsulta(String filtroIdUnidadConsulta) {
		this.filtroIdUnidadConsulta = filtroIdUnidadConsulta;
	}
	/**
	 * @return the filtroIdConvenio
	 */
	public String getFiltroIdConvenio() {
		return filtroIdConvenio;
	}
	/**
	 * @param filtroIdConvenio the filtroIdConvenio to set
	 */
	public void setFiltroIdConvenio(String filtroIdConvenio) {
		this.filtroIdConvenio = filtroIdConvenio;
	}
	/**
	 * @return the filtroProfesionalSalud
	 */
	public ProfesionalSaludDto getFiltroProfesionalSalud() {
		return filtroProfesionalSalud;
	}
	/**
	 * @param filtroProfesionalSalud the filtroProfesionalSalud to set
	 */
	public void setFiltroProfesionalSalud(ProfesionalSaludDto filtroProfesionalSalud) {
		this.filtroProfesionalSalud = filtroProfesionalSalud;
	}
	/**
	 * @return the filtroIdProfesionalSalud
	 */
	public String getFiltroIdProfesionalSalud() {
		return filtroIdProfesionalSalud;
	}
	/**
	 * @param filtroIdProfesionalSalud the filtroIdProfesionalSalud to set
	 */
	public void setFiltroIdProfesionalSalud(String filtroIdProfesionalSalud) {
		this.filtroIdProfesionalSalud = filtroIdProfesionalSalud;
	}
	/**
	 * @return the filtroIdGrupoServicio
	 */
	public String getFiltroIdGrupoServicio() {
		return filtroIdGrupoServicio;
	}
	/**
	 * @param filtroIdGrupoServicio the filtroIdGrupoServicio to set
	 */
	public void setFiltroIdGrupoServicio(String filtroIdGrupoServicio) {
		this.filtroIdGrupoServicio = filtroIdGrupoServicio;
	}
	/**
	 * @return the filtroIdClaseInventario
	 */
	public String getFiltroIdClaseInventario() {
		return filtroIdClaseInventario;
	}
	/**
	 * @param filtroIdClaseInventario the filtroIdClaseInventario to set
	 */
	public void setFiltroIdClaseInventario(String filtroIdClaseInventario) {
		this.filtroIdClaseInventario = filtroIdClaseInventario;
	}
	/**
	 * @return the ordenadoresConsultaExternaPlano
	 */
	public List<OrdenadoresConsultaExternaPlanoDto> getOrdenadoresConsultaExternaPlano() {
		return ordenadoresConsultaExternaPlano;
	}
	/**
	 * @param ordenadoresConsultaExternaPlano the ordenadoresConsultaExternaPlano to set
	 */
	public void setOrdenadoresConsultaExternaPlano(
			List<OrdenadoresConsultaExternaPlanoDto> ordenadoresConsultaExternaPlano) {
		this.ordenadoresConsultaExternaPlano = ordenadoresConsultaExternaPlano;
	}
	/**
	 * @return the grupoOrdenadoresConsultaExternaPlano
	 */
	public JRDataSource getGrupoOrdenadoresConsultaExternaPlano() {
		return grupoOrdenadoresConsultaExternaPlano;
	}
	/**
	 * @param grupoOrdenadoresConsultaExternaPlano the grupoOrdenadoresConsultaExternaPlano to set
	 */
	public void setGrupoOrdenadoresConsultaExternaPlano(
			JRDataSource grupoOrdenadoresConsultaExternaPlano) {
		this.grupoOrdenadoresConsultaExternaPlano = grupoOrdenadoresConsultaExternaPlano;
	}
	/**
	 * @return the codigoTipoImpresion
	 */
	public int getCodigoTipoImpresion() {
		return codigoTipoImpresion;
	}
	/**
	 * @param codigoTipoImpresion the codigoTipoImpresion to set
	 */
	public void setCodigoTipoImpresion(int codigoTipoImpresion) {
		this.codigoTipoImpresion = codigoTipoImpresion;
	}
	/**
	 * @return the codigoTipoReporte
	 */
	public int getCodigoTipoReporte() {
		return codigoTipoReporte;
	}
	/**
	 * @param codigoTipoReporte the codigoTipoReporte to set
	 */
	public void setCodigoTipoReporte(int codigoTipoReporte) {
		this.codigoTipoReporte = codigoTipoReporte;
	}
	/**
	 * @return the gruposServicio
	 */
	public List<GrupoServicioDto> getGruposServicio() {
		return gruposServicio;
	}
	/**
	 * @param gruposServicio the gruposServicio to set
	 */
	public void setGruposServicio(List<GrupoServicioDto> gruposServicio) {
		this.gruposServicio = gruposServicio;
	}
	/**
	 * @return the grupoGruposServicio
	 */
	public JRDataSource getGrupoGruposServicio() {
		return grupoGruposServicio;
	}
	/**
	 * @param grupoGruposServicio the grupoGruposServicio to set
	 */
	public void setGrupoGruposServicio(JRDataSource grupoGruposServicio) {
		this.grupoGruposServicio = grupoGruposServicio;
	}
	/**
	 * @return the clasesInventario
	 */
	public List<ClaseInventarioDto> getClasesInventario() {
		return clasesInventario;
	}
	/**
	 * @param clasesInventario the clasesInventario to set
	 */
	public void setClasesInventario(List<ClaseInventarioDto> clasesInventario) {
		this.clasesInventario = clasesInventario;
	}
	/**
	 * @return the grupoClaseInventario
	 */
	public JRDataSource getGrupoClasesInventario() {
		return grupoClasesInventario;
	}
	/**
	 * @param grupoClaseInventario the grupoClaseInventario to set
	 */
	public void setGrupoClasesInventario(JRDataSource grupoClaseInventario) {
		this.grupoClasesInventario = grupoClaseInventario;
	}
	/**
	 * @return the filtroFechaInicialAtencionCita
	 */
	public String getFiltroFechaInicialAtencionCita() {
		return filtroFechaInicialAtencionCita;
	}
	/**
	 * @param filtroFechaInicialAtencionCita the filtroFechaInicialAtencionCita to set
	 */
	public void setFiltroFechaInicialAtencionCita(
			String filtroFechaInicialAtencionCita) {
		this.filtroFechaInicialAtencionCita = filtroFechaInicialAtencionCita;
	}
	/**
	 * @return the filtroFechaFinalAtencionCita
	 */
	public String getFiltroFechaFinalAtencionCita() {
		return filtroFechaFinalAtencionCita;
	}
	/**
	 * @param filtroFechaFinalAtencionCita the filtroFechaFinalAtencionCita to set
	 */
	public void setFiltroFechaFinalAtencionCita(String filtroFechaFinalAtencionCita) {
		this.filtroFechaFinalAtencionCita = filtroFechaFinalAtencionCita;
	}
	
	
	
}
