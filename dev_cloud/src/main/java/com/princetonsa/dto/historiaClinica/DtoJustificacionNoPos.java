package com.princetonsa.dto.historiaClinica;

import java.io.Serializable;
import java.util.ArrayList;

import com.princetonsa.mundo.atencion.Diagnostico;

public class DtoJustificacionNoPos implements Serializable
{
	private String codigo;
	private String articulo;
	private String solicitud;
	private String subcuenta;
	private String consecutivo;
	private String ordenAmbulatoria;
	private String cantidad;
	private String justificacion;
	private String articuloSustituto;
	private String cantidadSustituto;	
	private String profesionalResponsable;
	private String usuarioModifica;
	private String institucion;
	private String bibliografia;
	private String estado;
	private String fechaJustificacion;
	private String solicitadoEn;
	private String ingreso;
	private String idPaciente;
	private String nombrePaciente;
	private String fechaNacimientoPaciente;
	private String nombreArticulo;
	private String especialidadMedico;
	private String idMedico;
	private String registroMedico;
	private String nombreMedico;
	private String firmaDigital;
	private ArrayList<Diagnostico> diagnosticos;
	private ArrayList<DtoResponsableJustificacionNoPos> responsables;
	private ArrayList<DtoParamCamposJusNoPos> camposParam;
	
	/**
	 * Constructor
	 */
	public DtoJustificacionNoPos()
	{
		this.clean();
	}
	
	/**
	 * Resetea todas las variables del DTO
	 */
	public void clean()
	{
		this.codigo="";
		this.articulo = "";
		this.solicitud = "";
		this.subcuenta = "";
		this.ordenAmbulatoria = "";
		this.cantidad = "";
		this.justificacion = "";
		this.articuloSustituto = "";
		this.cantidadSustituto = "";	
		this.profesionalResponsable = "";
		this.usuarioModifica = "";
		this.institucion = "";
		this.bibliografia = "";
		this.estado = "";
		this.consecutivo = "";
		this.fechaJustificacion = "";
		this.solicitadoEn="";
		this.ingreso="";
		this.idPaciente="";
		this.nombrePaciente="";
		this.fechaNacimientoPaciente="";
		this.nombreArticulo="";
		this.especialidadMedico="";
		this.idMedico="";
		this.registroMedico="";
		this.nombreMedico="";
		this.firmaDigital="";
		this.diagnosticos = new ArrayList<Diagnostico>();
		this.responsables = new ArrayList<DtoResponsableJustificacionNoPos>();
		this.camposParam = new ArrayList<DtoParamCamposJusNoPos>();
	}

	/**
	 * @return the articulo
	 */
	public String getArticulo() {
		return articulo;
	}

	/**
	 * @param articulo the articulo to set
	 */
	public void setArticulo(String articulo) {
		this.articulo = articulo;
	}

	/**
	 * @return the justificacion
	 */
	public String getJustificacion() {
		return justificacion;
	}

	/**
	 * @param justificacion the justificacion to set
	 */
	public void setJustificacion(String justificacion) {
		this.justificacion = justificacion;
	}

	/**
	 * @return the cantidadSustituto
	 */
	public String getCantidadSustituto() {
		return cantidadSustituto;
	}

	/**
	 * @param cantidadSustituto the cantidadSustituto to set
	 */
	public void setCantidadSustituto(String cantidadSustituto) {
		this.cantidadSustituto = cantidadSustituto;
	}

	/**
	 * @return the profesionalResponsable
	 */
	public String getProfesionalResponsable() {
		return profesionalResponsable;
	}

	/**
	 * @param profesionalResponsable the profesionalResponsable to set
	 */
	public void setProfesionalResponsable(String profesionalResponsable) {
		this.profesionalResponsable = profesionalResponsable;
	}

	/**
	 * @return the diagnosticos
	 */
	public ArrayList<Diagnostico> getDiagnosticos() {
		return diagnosticos;
	}

	/**
	 * @param diagnosticos the diagnosticos to set
	 */
	public void setDiagnosticos(ArrayList<Diagnostico> diagnosticos) {
		this.diagnosticos = diagnosticos;
	}

	/**
	 * @return the responsables
	 */
	public ArrayList<DtoResponsableJustificacionNoPos> getResponsables() {
		return responsables;
	}

	/**
	 * @param responsables the responsables to set
	 */
	public void setResponsables(
			ArrayList<DtoResponsableJustificacionNoPos> responsables) {
		this.responsables = responsables;
	}

	/**
	 * @return the camposParam
	 */
	public ArrayList<DtoParamCamposJusNoPos> getCamposParam() {
		return camposParam;
	}

	/**
	 * @param camposParam the camposParam to set
	 */
	public void setCamposParam(ArrayList<DtoParamCamposJusNoPos> camposParam) {
		this.camposParam = camposParam;
	}

	/**
	 * @return the solicitud
	 */
	public String getSolicitud() {
		return solicitud;
	}

	/**
	 * @param solicitud the solicitud to set
	 */
	public void setSolicitud(String solicitud) {
		this.solicitud = solicitud;
	}

	/**
	 * @return the usuarioModifica
	 */
	public String getUsuarioModifica() {
		return usuarioModifica;
	}

	/**
	 * @param usuarioModifica the usuarioModifica to set
	 */
	public void setUsuarioModifica(String usuarioModifica) {
		this.usuarioModifica = usuarioModifica;
	}

	/**
	 * @return the institucion
	 */
	public String getInstitucion() {
		return institucion;
	}

	/**
	 * @param institucion the institucion to set
	 */
	public void setInstitucion(String institucion) {
		this.institucion = institucion;
	}

	/**
	 * @return the ordenAmbulatoria
	 */
	public String getOrdenAmbulatoria() {
		return ordenAmbulatoria;
	}

	/**
	 * @param ordenAmbulatoria the ordenAmbulatoria to set
	 */
	public void setOrdenAmbulatoria(String ordenAmbulatoria) {
		this.ordenAmbulatoria = ordenAmbulatoria;
	}

	public boolean isOrdenAmbulatoria(){
		return this.ordenAmbulatoria!=null&&!this.ordenAmbulatoria.trim().isEmpty();
	}
	/**
	 * @return the bibliografia
	 */
	public String getBibliografia() {
		return bibliografia;
	}

	/**
	 * @param bibliografia the bibliografia to set
	 */
	public void setBibliografia(String bibliografia) {
		this.bibliografia = bibliografia;
	}
	/**
	 * @return the articuloSustituto
	 */
	public String getArticuloSustituto() {
		return articuloSustituto;
	}
	/**
	 * @param articuloSustituto the articuloSustituto to set
	 */
	public void setArticuloSustituto(String articuloSustituto) {
		this.articuloSustituto = articuloSustituto;
	}
	/**
	 * @return the subcuenta
	 */
	public String getSubcuenta() {
		return subcuenta;
	}
	/**
	 * @param subcuenta the subcuenta to set
	 */
	public void setSubcuenta(String subcuenta) {
		this.subcuenta = subcuenta;
	}
	/**
	 * @return the cantidad
	 */
	public String getCantidad() {
		return cantidad;
	}
	/**
	 * @param cantidad the cantidad to set
	 */
	public void setCantidad(String cantidad) {
		this.cantidad = cantidad;
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
	 * @return the consecutivo
	 */
	public String getConsecutivo() {
		return consecutivo;
	}

	/**
	 * @param consecutivo the consecutivo to set
	 */
	public void setConsecutivo(String consecutivo) {
		this.consecutivo = consecutivo;
	}

	/**
	 * @return the fechaJustificacion
	 */
	public String getFechaJustificacion() {
		return fechaJustificacion;
	}

	/**
	 * @param fechaJustificacion the fechaJustificacion to set
	 */
	public void setFechaJustificacion(String fechaJustificacion) {
		this.fechaJustificacion = fechaJustificacion;
	}

	/**
	 * @return the codigo
	 */
	public String getCodigo() {
		return codigo;
	}

	/**
	 * @param codigo the codigo to set
	 */
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	/**
	 * @return the solicitadoEn
	 */
	public String getSolicitadoEn() {
		return solicitadoEn;
	}

	/**
	 * @param solicitadoEn the solicitadoEn to set
	 */
	public void setSolicitadoEn(String solicitadoEn) {
		this.solicitadoEn = solicitadoEn;
	}

	/**
	 * @return the ingreso
	 */
	public String getIngreso() {
		return ingreso;
	}

	/**
	 * @param ingreso the ingreso to set
	 */
	public void setIngreso(String ingreso) {
		this.ingreso = ingreso;
	}

	/**
	 * @return the idPaciente
	 */
	public String getIdPaciente() {
		return idPaciente;
	}

	/**
	 * @param idPaciente the idPaciente to set
	 */
	public void setIdPaciente(String idPaciente) {
		this.idPaciente = idPaciente;
	}

	/**
	 * @return the nombrePaciente
	 */
	public String getNombrePaciente() {
		return nombrePaciente;
	}

	/**
	 * @param nombrePaciente the nombrePaciente to set
	 */
	public void setNombrePaciente(String nombrePaciente) {
		this.nombrePaciente = nombrePaciente;
	}

	/**
	 * @return the fechaNacimientoPaciente
	 */
	public String getFechaNacimientoPaciente() {
		return fechaNacimientoPaciente;
	}

	/**
	 * @param fechaNacimientoPaciente the fechaNacimientoPaciente to set
	 */
	public void setFechaNacimientoPaciente(String fechaNacimientoPaciente) {
		this.fechaNacimientoPaciente = fechaNacimientoPaciente;
	}

	/**
	 * @return the nombreArticulo
	 */
	public String getNombreArticulo() {
		return nombreArticulo;
	}

	/**
	 * @param nombreArticulo the nombreArticulo to set
	 */
	public void setNombreArticulo(String nombreArticulo) {
		this.nombreArticulo = nombreArticulo;
	}

	/**
	 * @return the especialidadMedico
	 */
	public String getEspecialidadMedico() {
		return especialidadMedico;
	}

	/**
	 * @param especialidadMedico the especialidadMedico to set
	 */
	public void setEspecialidadMedico(String especialidadMedico) {
		this.especialidadMedico = especialidadMedico;
	}

	/**
	 * @return the idMedico
	 */
	public String getIdMedico() {
		return idMedico;
	}

	/**
	 * @param idMedico the idMedico to set
	 */
	public void setIdMedico(String idMedico) {
		this.idMedico = idMedico;
	}

	/**
	 * @return the registroMedico
	 */
	public String getRegistroMedico() {
		return registroMedico;
	}

	/**
	 * @param registroMedico the registroMedico to set
	 */
	public void setRegistroMedico(String registroMedico) {
		this.registroMedico = registroMedico;
	}

	/**
	 * @return the nombreMedico
	 */
	public String getNombreMedico() {
		return nombreMedico;
	}

	/**
	 * @param nombreMedico the nombreMedico to set
	 */
	public void setNombreMedico(String nombreMedico) {
		this.nombreMedico = nombreMedico;
	}

	/**
	 * @return the firmaDigital
	 */
	public String getFirmaDigital() {
		return firmaDigital;
	}

	/**
	 * @param firmaDigital the firmaDigital to set
	 */
	public void setFirmaDigital(String firmaDigital) {
		this.firmaDigital = firmaDigital;
	}
	
	
}
