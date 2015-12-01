/**
 * 
 */
package com.servinte.axioma.actionForm.consultaExterna;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.struts.validator.ValidatorForm;

import com.princetonsa.dto.comun.DtoCheckBox;
import com.servinte.axioma.dto.administracion.CentroAtencionDto;
import com.servinte.axioma.dto.administracion.FuncionalidadDto;
import com.servinte.axioma.dto.administracion.ProfesionalSaludDto;
import com.servinte.axioma.dto.consultaExterna.DtoUnidadesConsulta;
import com.servinte.axioma.dto.facturacion.ConvenioDto;
import com.servinte.axioma.dto.facturacion.GrupoServicioDto;
import com.servinte.axioma.dto.inventario.ClaseInventarioDto;

/**
 * @author jeilones
 * @created 29/10/2012
 *
 */
public class IdentificadoresOrdenadoresConsultaForm extends ValidatorForm{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6007904317253742571L;
	
	private List<FuncionalidadDto> listaFuncionalidades=new ArrayList<FuncionalidadDto>(0);
	
	private String estado="";
	
	/**
	 * Filtros de busqueda
	 * */
	private Date fechaInicialAtencionCita;
	private Date fechaFinalAtencionCita;
	private CentroAtencionDto centroAtencion;
	private String idCentroAtencion;
	private DtoUnidadesConsulta unidadesConsulta;
	private String idUnidadConsulta;
	private ConvenioDto convenio;
	private String idConvenio;
	private ProfesionalSaludDto profesionalSalud;
	private String loginProfesionalSalud;
	private GrupoServicioDto grupoServicio;
	private String idGrupoServicio;
	private ClaseInventarioDto inventarioDto;
	private String idClaseInventario;
	private String idTipoReporte;
	private String tipoSalida;
	private String nombreArchivoGenerado;
	
	private List<CentroAtencionDto>listaCentrosAtencion=new ArrayList<CentroAtencionDto>(0);
	private List<DtoUnidadesConsulta>listaUnidadesConsultas=new ArrayList<DtoUnidadesConsulta>(0);
	private List<ConvenioDto>listaConvenios=new ArrayList<ConvenioDto>(0);
	private List<ProfesionalSaludDto>listaProfesionalesSalud=new ArrayList<ProfesionalSaludDto>(0);
	private List<GrupoServicioDto>listaGruposServicio=new ArrayList<GrupoServicioDto>(0);
	private List<ClaseInventarioDto>listaClasesInventario=new ArrayList<ClaseInventarioDto>(0);
	
	private List<DtoCheckBox> listaTipoReporte=new ArrayList<DtoCheckBox>(0);
	
	private boolean permitirPopUpImpresion=false;
	private boolean hayMensajeExito=false;
	
	/**
	 * Fin filtros de busqueda
	 * */
	
	/**
	 * @return the listaFuncionalidades
	 */
	public List<FuncionalidadDto> getListaFuncionalidades() {
		return listaFuncionalidades;
	}
	/**
	 * @param listaFuncionalidades the listaFuncionalidades to set
	 */
	public void setListaFuncionalidades(List<FuncionalidadDto> listaFuncionalidades) {
		this.listaFuncionalidades = listaFuncionalidades;
	}
	/**
	 * @return the estado
	 */
	public String getEstado() {
		return estado;
	}
	/**
	 * @param estado the estado to set
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	}
	/**
	 * @return the fechaIncialAtencionCita
	 */
	public Date getFechaInicialAtencionCita() {
		return fechaInicialAtencionCita;
	}
	/**
	 * @param fechaIncialAtencionCita the fechaIncialAtencionCita to set
	 */
	public void setFechaInicialAtencionCita(Date fechaIncialAtencionCita) {
		this.fechaInicialAtencionCita = fechaIncialAtencionCita;
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
	 * @return the centroAtencion
	 */
	public CentroAtencionDto getCentroAtencion() {
		return centroAtencion;
	}
	/**
	 * @param centroAtencion the centroAtencion to set
	 */
	public void setCentroAtencion(CentroAtencionDto centroAtencion) {
		this.centroAtencion = centroAtencion;
	}
	/**
	 * @return the unidadesConsulta
	 */
	public DtoUnidadesConsulta getUnidadesConsulta() {
		return unidadesConsulta;
	}
	/**
	 * @param unidadesConsulta the unidadesConsulta to set
	 */
	public void setUnidadesConsulta(DtoUnidadesConsulta unidadesConsulta) {
		this.unidadesConsulta = unidadesConsulta;
	}
	/**
	 * @return the convenio
	 */
	public ConvenioDto getConvenio() {
		return convenio;
	}
	/**
	 * @param convenio the convenio to set
	 */
	public void setConvenio(ConvenioDto convenio) {
		this.convenio = convenio;
	}
	/**
	 * @return the profesionalSalud
	 */
	public ProfesionalSaludDto getProfesionalSalud() {
		return profesionalSalud;
	}
	/**
	 * @param profesionalSalud the profesionalSalud to set
	 */
	public void setProfesionalSalud(ProfesionalSaludDto profesionalSalud) {
		this.profesionalSalud = profesionalSalud;
	}
	/**
	 * @return the grupoServicio
	 */
	public GrupoServicioDto getGrupoServicio() {
		return grupoServicio;
	}
	/**
	 * @param grupoServicio the grupoServicio to set
	 */
	public void setGrupoServicio(GrupoServicioDto grupoServicio) {
		this.grupoServicio = grupoServicio;
	}
	/**
	 * @return the inventarioDto
	 */
	public ClaseInventarioDto getInventarioDto() {
		return inventarioDto;
	}
	/**
	 * @param inventarioDto the inventarioDto to set
	 */
	public void setInventarioDto(ClaseInventarioDto inventarioDto) {
		this.inventarioDto = inventarioDto;
	}
	/**
	 * @return the listaCentrosAtencion
	 */
	public List<CentroAtencionDto> getListaCentrosAtencion() {
		return listaCentrosAtencion;
	}
	/**
	 * @param listaCentrosAtencion the listaCentrosAtencion to set
	 */
	public void setListaCentrosAtencion(List<CentroAtencionDto> listaCentrosAtencion) {
		this.listaCentrosAtencion = listaCentrosAtencion;
	}
	/**
	 * @return the listaConvenios
	 */
	public List<ConvenioDto> getListaConvenios() {
		return listaConvenios;
	}
	/**
	 * @param listaConvenios the listaConvenios to set
	 */
	public void setListaConvenios(List<ConvenioDto> listaConvenios) {
		this.listaConvenios = listaConvenios;
	}
	/**
	 * @return the listaProfesionalesSalud
	 */
	public List<ProfesionalSaludDto> getListaProfesionalesSalud() {
		return listaProfesionalesSalud;
	}
	/**
	 * @param listaProfesionalesSalud the listaProfesionalesSalud to set
	 */
	public void setListaProfesionalesSalud(
			List<ProfesionalSaludDto> listaProfesionalesSalud) {
		this.listaProfesionalesSalud = listaProfesionalesSalud;
	}
	/**
	 * @return the listaGruposServicio
	 */
	public List<GrupoServicioDto> getListaGruposServicio() {
		return listaGruposServicio;
	}
	/**
	 * @param listaGruposServicio the listaGruposServicio to set
	 */
	public void setListaGruposServicio(List<GrupoServicioDto> listaGruposServicio) {
		this.listaGruposServicio = listaGruposServicio;
	}
	/**
	 * @return the listaClasesInventario
	 */
	public List<ClaseInventarioDto> getListaClasesInventario() {
		return listaClasesInventario;
	}
	/**
	 * @param listaClasesInventario the listaClasesInventario to set
	 */
	public void setListaClasesInventario(
			List<ClaseInventarioDto> listaClasesInventario) {
		this.listaClasesInventario = listaClasesInventario;
	}
	/**
	 * @return the listaUnidadesConsultas
	 */
	public List<DtoUnidadesConsulta> getListaUnidadesConsultas() {
		return listaUnidadesConsultas;
	}
	/**
	 * @param listaUnidadesConsultas the listaUnidadesConsultas to set
	 */
	public void setListaUnidadesConsultas(
			List<DtoUnidadesConsulta> listaUnidadesConsultas) {
		this.listaUnidadesConsultas = listaUnidadesConsultas;
	}
	/**
	 * @return the idCentroAtencion
	 */
	public String getIdCentroAtencion() {
		return idCentroAtencion;
	}
	/**
	 * @param idCentroAtencion the idCentroAtencion to set
	 */
	public void setIdCentroAtencion(String idCentroAtencion) {
		this.idCentroAtencion = idCentroAtencion;
	}
	/**
	 * @return the idUnidadConsulta
	 */
	public String getIdUnidadConsulta() {
		return idUnidadConsulta;
	}
	/**
	 * @param idUnidadConsulta the idUnidadConsulta to set
	 */
	public void setIdUnidadConsulta(String idUnidadConsulta) {
		this.idUnidadConsulta = idUnidadConsulta;
	}
	/**
	 * @return the idConvenio
	 */
	public String getIdConvenio() {
		return idConvenio;
	}
	/**
	 * @param idConvenio the idConvenio to set
	 */
	public void setIdConvenio(String idConvenio) {
		this.idConvenio = idConvenio;
	}
	/**
	 * @return the idProfesionalSalud
	 */
	public String getLoginProfesionalSalud() {
		return loginProfesionalSalud;
	}
	/**
	 * @param idProfesionalSalud the idProfesionalSalud to set
	 */
	public void setLoginProfesionalSalud(String idProfesionalSalud) {
		this.loginProfesionalSalud = idProfesionalSalud;
	}
	/**
	 * @return the idGrupoServicio
	 */
	public String getIdGrupoServicio() {
		return idGrupoServicio;
	}
	/**
	 * @param idGrupoServicio the idGrupoServicio to set
	 */
	public void setIdGrupoServicio(String idGrupoServicio) {
		this.idGrupoServicio = idGrupoServicio;
	}
	/**
	 * @return the idInventario
	 */
	public String getIdClaseInventario() {
		return idClaseInventario;
	}
	/**
	 * @param idInventario the idInventario to set
	 */
	public void setIdClaseInventario(String idInventario) {
		this.idClaseInventario = idInventario;
	}
	/**
	 * @return the tipoReporte
	 */
	public List<DtoCheckBox> getListaTipoReporte() {
		return listaTipoReporte;
	}
	/**
	 * @param tipoReporte the tipoReporte to set
	 */
	public void setListaTipoReporte(List<DtoCheckBox> tipoReporte) {
		this.listaTipoReporte = tipoReporte;
	}
	/**
	 * @return the idTipoReporte
	 */
	public String getIdTipoReporte() {
		return idTipoReporte;
	}
	/**
	 * @param idTipoReporte the idTipoReporte to set
	 */
	public void setIdTipoReporte(String idTipoReporte) {
		this.idTipoReporte = idTipoReporte;
	}
	/**
	 * @return the idTipoImpresion
	 */
	public String getTipoSalida() {
		return tipoSalida;
	}
	/**
	 * @param idTipoImpresion the idTipoImpresion to set
	 */
	public void setTipoSalida(String idTipoImpresion) {
		this.tipoSalida = idTipoImpresion;
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
	 * @return the permitirPopUpImpresion
	 */
	public boolean isPermitirPopUpImpresion() {
		return permitirPopUpImpresion;
	}

	/**
	 * @param permitirPopUpImpresion the permitirPopUpImpresion to set
	 */
	public void setPermitirPopUpImpresion(boolean permitirPopUpImpresion) {
		this.permitirPopUpImpresion = permitirPopUpImpresion;
	}
	/**
	 * @return the hayMensajeExito
	 */
	public boolean isHayMensajeExito() {
		return hayMensajeExito;
	}
	/**
	 * @param hayMensajeExito the hayMensajeExito to set
	 */
	public void setHayMensajeExito(boolean hayMensajeExito) {
		this.hayMensajeExito = hayMensajeExito;
	}

}
