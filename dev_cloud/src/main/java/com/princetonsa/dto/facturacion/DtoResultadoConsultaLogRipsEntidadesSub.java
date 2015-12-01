package com.princetonsa.dto.facturacion;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * Clase utilizada para capturar los datos de la consulta de log rips entidades subcontratadas
 * @author Fabián Becerra
 */
public class DtoResultadoConsultaLogRipsEntidadesSub implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private long codigoPkEntidadSub;
	private long codigoPkLog;
	private String razonSocialEntidadSub;
	private Date fechaProceso;
	private String fechaFormateada;
	private String horaProceso;
	private String loginUsuario;
	private String primerNombreUsuarioProceso;
	private String segundoNombreUsuarioProceso;
	private String primerApellidoUsuarioProceso;
	private String segundoApellidoUsuarioProceso;
	private String usuarioProceso;
	private String codificacionServicios;
	private String acronimoCodificacionMedicaInsu;
	private String nombreArchivo;
	private long cantidadRegistrosLeidos;
	private long cantidadRegistrosProcesados;
	private long numeroInconsistencias;
	private Long codigoPkInconsisCamp;
	private Long codigoPkInconsisArch;
	private Long codigoPkArchivo;
	private Long codigoPkLogRegistro;
	private String nombreCampoInconsistencia;
	private String tipoInconsistenciaCampo;
	private Integer numeroFila;
	private Long codigoPkValorCampo;
	private String campoMostrar;
	private String valorCampoMostrar;
	private String observaciones;
	
	
	/******   LISTAS UTILIZADAS PARA EL ORDENAMIENTO DE LAS CONSULTAS DE RIPS   ****************/
	DtoResultadoProcesarRipsAutorizacion dtoListaPorAutorizaciones= new DtoResultadoProcesarRipsAutorizacion();
	private ArrayList<DtoResultadoConsultaLogRipsEntidadesSub> dtoResultadoArchivos= new ArrayList<DtoResultadoConsultaLogRipsEntidadesSub>();
	private ArrayList<DtoResultadoConsultaLogRipsEntidadesSub> dtoResultadoLineasValores= new ArrayList<DtoResultadoConsultaLogRipsEntidadesSub>();
	private ArrayList<DtoResultadoConsultaLogRipsEntidadesSub> dtoResultadoNombresCampos= new ArrayList<DtoResultadoConsultaLogRipsEntidadesSub>();
	private ArrayList<DtoResultadoConsultaLogRipsEntidadesSub> dtoResultadoInconsistenciasPorRegistro= new ArrayList<DtoResultadoConsultaLogRipsEntidadesSub>();
	
	public long getCodigoPkEntidadSub() {
		return codigoPkEntidadSub;
	}
	public void setCodigoPkEntidadSub(long codigoPkEntidadSub) {
		this.codigoPkEntidadSub = codigoPkEntidadSub;
	}
	public String getRazonSocialEntidadSub() {
		return razonSocialEntidadSub;
	}
	public void setRazonSocialEntidadSub(String razonSocialEntidadSub) {
		this.razonSocialEntidadSub = razonSocialEntidadSub;
	}
	public Date getFechaProceso() {
		return fechaProceso;
	}
	public void setFechaProceso(Date fechaProceso) {
		this.fechaProceso = fechaProceso;
	}
	public String getHoraProceso() {
		return horaProceso;
	}
	public void setHoraProceso(String horaProceso) {
		this.horaProceso = horaProceso;
	}
	public void setLoginUsuario(String loginUsuario) {
		this.loginUsuario = loginUsuario;
	}
	public String getLoginUsuario() {
		return loginUsuario;
	}
	public String getPrimerNombreUsuarioProceso() {
		return primerNombreUsuarioProceso;
	}
	public void setPrimerNombreUsuarioProceso(String primerNombreUsuarioProceso) {
		this.primerNombreUsuarioProceso = primerNombreUsuarioProceso;
	}
	public String getSegundoNombreUsuarioProceso() {
		return segundoNombreUsuarioProceso;
	}
	public void setSegundoNombreUsuarioProceso(String segundoNombreUsuarioProceso) {
		this.segundoNombreUsuarioProceso = segundoNombreUsuarioProceso;
	}
	public String getPrimerApellidoUsuarioProceso() {
		return primerApellidoUsuarioProceso;
	}
	public void setPrimerApellidoUsuarioProceso(String primerApellidoUsuarioProceso) {
		this.primerApellidoUsuarioProceso = primerApellidoUsuarioProceso;
	}
	public String getSegundoApellidoUsuarioProceso() {
		return segundoApellidoUsuarioProceso;
	}
	public void setSegundoApellidoUsuarioProceso(
			String segundoApellidoUsuarioProceso) {
		this.segundoApellidoUsuarioProceso = segundoApellidoUsuarioProceso;
	}
	public void setCodigoPkLog(long codigoPkLog) {
		this.codigoPkLog = codigoPkLog;
	}
	public long getCodigoPkLog() {
		return codigoPkLog;
	}
	public void setUsuarioProceso(String usuarioProceso) {
		this.usuarioProceso = usuarioProceso;
	}
	public String getUsuarioProceso() {
		return usuarioProceso;
	}
	public void setFechaFormateada(String fechaFormateada) {
		this.fechaFormateada = fechaFormateada;
	}
	public String getFechaFormateada() {
		return fechaFormateada;
	}
	public void setCodificacionServicios(String codificacionServicios) {
		this.codificacionServicios = codificacionServicios;
	}
	public String getCodificacionServicios() {
		return codificacionServicios;
	}
	public void setAcronimoCodificacionMedicaInsu(
			String acronimoCodificacionMedicaInsu) {
		this.acronimoCodificacionMedicaInsu = acronimoCodificacionMedicaInsu;
	}
	public String getAcronimoCodificacionMedicaInsu() {
		return acronimoCodificacionMedicaInsu;
	}
	public void setNombreArchivo(String nombreArchivo) {
		this.nombreArchivo = nombreArchivo;
	}
	public String getNombreArchivo() {
		return nombreArchivo;
	}
	public void setDtoResultadoArchivos(ArrayList<DtoResultadoConsultaLogRipsEntidadesSub> dtoResultadoArchivos) {
		this.dtoResultadoArchivos = dtoResultadoArchivos;
	}
	public ArrayList<DtoResultadoConsultaLogRipsEntidadesSub> getDtoResultadoArchivos() {
		return dtoResultadoArchivos;
	}
	public void setCantidadRegistrosLeidos(long cantidadRegistrosLeidos) {
		this.cantidadRegistrosLeidos = cantidadRegistrosLeidos;
	}
	public long getCantidadRegistrosLeidos() {
		return cantidadRegistrosLeidos;
	}
	public void setCantidadRegistrosProcesados(long cantidadRegistrosProcesados) {
		this.cantidadRegistrosProcesados = cantidadRegistrosProcesados;
	}
	public long getCantidadRegistrosProcesados() {
		return cantidadRegistrosProcesados;
	}
	public void setNumeroInconsistencias(long numeroInconsistencias) {
		this.numeroInconsistencias = numeroInconsistencias;
	}
	public long getNumeroInconsistencias() {
		return numeroInconsistencias;
	}
	public void setCodigoPkInconsisCamp(Long codigoPkInconsisCamp) {
		this.codigoPkInconsisCamp = codigoPkInconsisCamp;
	}
	public Long getCodigoPkInconsisCamp() {
		return codigoPkInconsisCamp;
	}
	public void setCodigoPkInconsisArch(Long codigoPkInconsisArch) {
		this.codigoPkInconsisArch = codigoPkInconsisArch;
	}
	public Long getCodigoPkInconsisArch() {
		return codigoPkInconsisArch;
	}
	public void setCodigoPkArchivo(Long codigoPkArchivo) {
		this.codigoPkArchivo = codigoPkArchivo;
	}
	public Long getCodigoPkArchivo() {
		return codigoPkArchivo;
	}
	public void setNombreCampoInconsistencia(String nombreCampoInconsistencia) {
		this.nombreCampoInconsistencia = nombreCampoInconsistencia;
	}
	public String getNombreCampoInconsistencia() {
		return nombreCampoInconsistencia;
	}
	public void setTipoInconsistenciaCampo(String tipoInconsistenciaCampo) {
		this.tipoInconsistenciaCampo = tipoInconsistenciaCampo;
	}
	public String getTipoInconsistenciaCampo() {
		return tipoInconsistenciaCampo;
	}
	public void setCodigoPkLogRegistro(Long codigoPkLogRegistro) {
		this.codigoPkLogRegistro = codigoPkLogRegistro;
	}
	public Long getCodigoPkLogRegistro() {
		return codigoPkLogRegistro;
	}
	public void setNumeroFila(Integer numeroFila) {
		this.numeroFila = numeroFila;
	}
	public Integer getNumeroFila() {
		return numeroFila;
	}
	public void setCodigoPkValorCampo(Long codigoPkValorCampo) {
		this.codigoPkValorCampo = codigoPkValorCampo;
	}
	public Long getCodigoPkValorCampo() {
		return codigoPkValorCampo;
	}
	public void setCampoMostrar(String campoMostrar) {
		this.campoMostrar = campoMostrar;
	}
	public String getCampoMostrar() {
		return campoMostrar;
	}
	public void setValorCampoMostrar(String valorCampoMostrar) {
		this.valorCampoMostrar = valorCampoMostrar;
	}
	public String getValorCampoMostrar() {
		return valorCampoMostrar;
	}
	public ArrayList<DtoResultadoConsultaLogRipsEntidadesSub> getDtoResultadoLineasValores() {
		return dtoResultadoLineasValores;
	}
	public void setDtoResultadoLineasValores(
			ArrayList<DtoResultadoConsultaLogRipsEntidadesSub> dtoResultadoLineasValores) {
		this.dtoResultadoLineasValores = dtoResultadoLineasValores;
	}
	public ArrayList<DtoResultadoConsultaLogRipsEntidadesSub> getDtoResultadoNombresCampos() {
		return dtoResultadoNombresCampos;
	}
	public void setDtoResultadoNombresCampos(
			ArrayList<DtoResultadoConsultaLogRipsEntidadesSub> dtoResultadoNombresCampos) {
		this.dtoResultadoNombresCampos = dtoResultadoNombresCampos;
	}
	public void setDtoResultadoInconsistenciasPorRegistro(
			ArrayList<DtoResultadoConsultaLogRipsEntidadesSub> dtoResultadoInconsistenciasPorRegistro) {
		this.dtoResultadoInconsistenciasPorRegistro = dtoResultadoInconsistenciasPorRegistro;
	}
	public ArrayList<DtoResultadoConsultaLogRipsEntidadesSub> getDtoResultadoInconsistenciasPorRegistro() {
		return dtoResultadoInconsistenciasPorRegistro;
	}
	public DtoResultadoProcesarRipsAutorizacion getDtoListaPorAutorizaciones() {
		return dtoListaPorAutorizaciones;
	}
	public void setDtoListaPorAutorizaciones(
			DtoResultadoProcesarRipsAutorizacion dtoListaPorAutorizaciones) {
		this.dtoListaPorAutorizaciones = dtoListaPorAutorizaciones;
	}
	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}
	public String getObservaciones() {
		return observaciones;
	}
	
	
	
	
	

}
