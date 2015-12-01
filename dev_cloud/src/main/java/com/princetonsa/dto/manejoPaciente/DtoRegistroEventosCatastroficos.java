/*
 * @author Jorge Armando Osorio Velasquez
 */
package com.princetonsa.dto.manejoPaciente;

import java.io.Serializable;

import util.ConstantesBD;
import util.UtilidadFecha;

/**
 * @author Jorge Armando Osorio Velasquez
 *
 */
public class DtoRegistroEventosCatastroficos implements Serializable
{

	/**
	 * Codigo del registro de accidentes de transito
	 */
	private int codigo;
	
	/**
	 * codigo del ingreso relacionado al registro.
	 */
	private int ingreso;
	
	/**
	 * Empresa donde trabaja la persona accidentada
	 */
	private String empresaTrabaja;

	/**
	 * Ciudad Empresa
	 */
	private String ciudadEmpresa;
	
	/**
	 * Departamento Empresa.
	 */
	private String departamentoEmpresa;
	
	/**
	 * 
	 */
	private String direccionEvento;

	/**
	 * 
	 */
	private String fechaEvento;
	
	/**
	 * 
	 */
	private String ciudadEvento;
	
	/**
	 * 
	 */
	private String departamentoEvento;
	
	/**
	 * 
	 */
	private String zonaUrbanaEvento;
	
	/**
	 * 
	 */
	private int naturalezaEvento;
	/**
	 * 
	 */
	private String apellidoNombreTransporta;
	
	/**
	 * 
	 */
	private String tipoIdTransporta;
	
	/**
	 * 
	 */
	private String numeroIdTransporta;
	
	/**
	 * 
	 */
	private String ciudadExpIdTransporta;
	
	/**
	 * 
	 */
	private String deptoExpIdTransporta;
	
	/**
	 * 
	 */
	private String telefonoTransporta; 
	
	/**
	 * 
	 */
    private String direccionTransporta;
	
	/**
	 * 
	 */
    private String ciudadTranporta;
	
	/**
	 * 
	 */
    private String deptoTransporta;
	
	/**
	 * 
	 */
    private String transportaDesde;
	
	/**
	 * 
	 */
    private String transportaHasta;
	
	/**
	 * 
	 */
    private String tipoTransporte;
	
	/**
	 * 
	 */
    private String placaVehiculoTransporta;
	
	/**
	 * 
	 */
    private String estadoRegistro;
	
	/**
	 * 
	 */
    private String fechaModifica;
	
	/**
	 * 
	 */
    private String horaModifica;
	
	/**
	 * 
	 */
    private String usuarioModifica;
    
    /**
     * 
     */
    private String paisEmpresa;
    
    /**
     * 
     */
    private String paisEvento;
    
    /**
     * 
     */
    private String paisExpIdTransporta;
    
    /**
     * 
     */
    private String paisTransporta;

    /**
     * 
     */
    private String sgsss;
    
    /**
     * 
     */
    private String tipoRegimen;
    
    /**
     * 
     */
    private String horaEvento;
	
    /**
     * 
     */
	private String descOcurrencia;
    
	/**
	 * 
	 */
	private String descOtroEvento;
	
	/**
	 * 
	 */
	private String tipoReferencia;
	
	/**
	 * 
	 */
	private String fechaRemision;
	
	/**
	 * 
	 */
	private String horaRemision;
	
	/**
	 * 
	 */
	private String prestadorRemite;
	
	/**
	 * 
	 */
	private String profesionalRemite;
	
	/**
	 * 
	 */
	private String cargoProfesionalRemite;
	
	/**
	 * 
	 */
	private String prestadorRecibe;
	
	/**
	 * 
	 */
	private String profesionalRecibe;
	
	/**
	 * 
	 */
	private String cargoProfesionalRecibe;
	
	/**
	 * 
	 */
	private String fechaAceptacion;
	
	/**
	 * 
	 */
	private String horaAceptacion;
	
	/**
	 * 
	 */
	private String otroTipoTransporte;
	
	/**
	 * 
	 */
	private String zonaTransporte;
	
	/**
	 * 
	 */
	private String totalFacturadoMedicoQuirurgico;
	
	/**
	 * 
	 */
	private String totalReclamadoMedicoQuirurgico;
	
	/**
	 * 
	 */
	private String totalFacturadoTransporte;
	
	/**
	 * 
	 */
	private String totalReclamadoTransporte;
	
	/**
	 * 
	 */
	private String esReclamacion; 
	
	/**
	 * 
	 */
	private String furips;
	
	/**
	 * 
	 */
	private String furpro;
	
	/**
	 * 
	 */
	private String furtran;
	
	/**
	 * 
	 */
	private String respuestaGlosa;
	
	/**
	 * 
	 */
	private String numeroRadicadoAnterior;
	
	/**
	 * 
	 */
	private String numeroConsecutivoReclamacion;
	
	/**
	 * 
	 */
	private String protesis;
	
	/**
	 * 
	 */
	private String valorProtesis;
	
	/**
	 * 
	 */
	private String adaptacionProtesis;
	
	/**
	 * 
	 */
	private String valorAdaptacionProtesis;
	
	/**
	 * 
	 */
	private String rehabilitacion;
	
	/**
	 * 
	 */
	private String valorRehabilitacion;
	
	
	public DtoRegistroEventosCatastroficos()
	{
		this.codigo=ConstantesBD.codigoNuncaValido;
		this.ingreso=ConstantesBD.codigoNuncaValido;
		this.empresaTrabaja="";
		this.ciudadEmpresa="";
		this.departamentoEmpresa="";
		this.direccionEvento="";
		this.fechaEvento="";
		this.ciudadEvento="";
		this.departamentoEvento="";
		this.zonaUrbanaEvento="";
		this.naturalezaEvento=ConstantesBD.codigoNuncaValido;
		this.apellidoNombreTransporta="";
		this.tipoIdTransporta="";
		this.numeroIdTransporta="";
		this.ciudadExpIdTransporta="";
		this.deptoExpIdTransporta="";
		this.telefonoTransporta="";
		this.direccionTransporta="";
		this.ciudadTranporta="";
		this.deptoTransporta="";
		this.transportaDesde="";
		this.transportaHasta="";
		this.tipoTransporte="";
		this.placaVehiculoTransporta="";
		this.estadoRegistro="";
		this.fechaModifica="";
		this.horaModifica="";
		this.usuarioModifica="";
		this.paisEmpresa="";
		this.paisEvento="";
		this.paisExpIdTransporta="";
		this.paisTransporta="";
		this.tipoRegimen = "";
		this.sgsss = "";
		this.horaEvento = "";
		this.descOcurrencia = "";
		this.descOtroEvento = "";
		this.tipoReferencia = "";
		this.fechaRemision = "";
		this.horaRemision = "";
		this.prestadorRemite = "";
		this.profesionalRemite = "";
		this.cargoProfesionalRemite = "";
		this.prestadorRecibe = "";
		this.profesionalRecibe = "";
		this.cargoProfesionalRecibe = "";
		this.fechaAceptacion = "";
		this.horaAceptacion = "";
		this.otroTipoTransporte = "";
		this.zonaTransporte = "";
		this.totalFacturadoMedicoQuirurgico = "";
		this.totalReclamadoMedicoQuirurgico = "";
		this.totalFacturadoTransporte = "";
		this.totalReclamadoTransporte = "";
		this.esReclamacion = "";
		this.respuestaGlosa = "";
		this.numeroRadicadoAnterior = "";
		this.numeroConsecutivoReclamacion = "";
		this.furips = "";
		this.furpro = "";
		this.furtran = "";
		this.protesis = "";
		this.valorProtesis = "";
		this.adaptacionProtesis = "";
		this.valorAdaptacionProtesis = "";
		this.rehabilitacion = "";
		this.valorRehabilitacion = "";
	}
	
	public String getApellidoNombreTransporta()
	{
		return apellidoNombreTransporta;
	}

	public void setApellidoNombreTransporta(String apellidoNombreTransporta)
	{
		this.apellidoNombreTransporta = apellidoNombreTransporta;
	}

	public String getCiudadEmpresa()
	{
		return ciudadEmpresa;
	}

	public void setCiudadEmpresa(String ciudadEmpresa)
	{
		this.ciudadEmpresa = ciudadEmpresa;
	}

	public String getCiudadEvento()
	{
		return ciudadEvento;
	}

	public void setCiudadEvento(String ciudadEvento)
	{
		this.ciudadEvento = ciudadEvento;
	}

	public String getCiudadExpIdTransporta()
	{
		return ciudadExpIdTransporta;
	}

	public void setCiudadExpIdTransporta(String ciudadExpIdTransporta)
	{
		this.ciudadExpIdTransporta = ciudadExpIdTransporta;
	}

	public String getCiudadTranporta()
	{
		return ciudadTranporta;
	}

	public void setCiudadTranporta(String ciudadTranporta)
	{
		this.ciudadTranporta = ciudadTranporta;
	}

	public int getCodigo()
	{
		return codigo;
	}

	public void setCodigo(int codigo)
	{
		this.codigo = codigo;
	}

	public String getDepartamentoEmpresa()
	{
		return departamentoEmpresa;
	}

	public void setDepartamentoEmpresa(String departamentoEmpresa)
	{
		this.departamentoEmpresa = departamentoEmpresa;
	}

	public String getDepartamentoEvento()
	{
		return departamentoEvento;
	}

	public void setDepartamentoEvento(String departamentoEvento)
	{
		this.departamentoEvento = departamentoEvento;
	}

	public String getDeptoExpIdTransporta()
	{
		return deptoExpIdTransporta;
	}

	public void setDeptoExpIdTransporta(String deptoExpIdTransporta)
	{
		this.deptoExpIdTransporta = deptoExpIdTransporta;
	}

	public String getDeptoTransporta()
	{
		return deptoTransporta;
	}

	public void setDeptoTransporta(String deptoTransporta)
	{
		this.deptoTransporta = deptoTransporta;
	}

	public String getDireccionTransporta()
	{
		return direccionTransporta;
	}

	public void setDireccionTransporta(String direccionTransporta)
	{
		this.direccionTransporta = direccionTransporta;
	}

	public String getEmpresaTrabaja()
	{
		return empresaTrabaja;
	}

	public void setEmpresaTrabaja(String empresaTrabaja)
	{
		this.empresaTrabaja = empresaTrabaja;
	}

	public String getEstadoRegistro()
	{
		return estadoRegistro;
	}

	public void setEstadoRegistro(String estadoRegistro)
	{
		this.estadoRegistro = estadoRegistro;
	}

	public String getFechaEvento()
	{
		return fechaEvento;
	}

	public void setFechaEvento(String fechaEvento)
	{
		this.fechaEvento = fechaEvento;
	}

	public String getFechaModifica()
	{
		return fechaModifica;
	}

	public void setFechaModifica(String fechaModifica)
	{
		this.fechaModifica = fechaModifica;
	}

	public String getHoraModifica()
	{
		return horaModifica;
	}

	public void setHoraModifica(String horaModifica)
	{
		this.horaModifica = horaModifica;
	}

	public int getIngreso()
	{
		return ingreso;
	}

	public void setIngreso(int ingreso)
	{
		this.ingreso = ingreso;
	}

	public int getNaturalezaEvento()
	{
		return naturalezaEvento;
	}

	public void setNaturalezaEvento(int naturalezaEvento)
	{
		this.naturalezaEvento = naturalezaEvento;
	}

	public String getNumeroIdTransporta()
	{
		return numeroIdTransporta;
	}

	public void setNumeroIdTransporta(String numeroIdTransporta)
	{
		this.numeroIdTransporta = numeroIdTransporta;
	}

	public String getPlacaVehiculoTransporta()
	{
		return placaVehiculoTransporta;
	}

	public void setPlacaVehiculoTransporta(String placaVehiculoTransporta)
	{
		this.placaVehiculoTransporta = placaVehiculoTransporta;
	}

	public String getTelefonoTransporta()
	{
		return telefonoTransporta;
	}

	public void setTelefonoTransporta(String telefonoTransporta)
	{
		this.telefonoTransporta = telefonoTransporta;
	}

	public String getTipoIdTransporta()
	{
		return tipoIdTransporta;
	}

	public void setTipoIdTransporta(String tipoIdTransporta)
	{
		this.tipoIdTransporta = tipoIdTransporta;
	}

	public String getTipoTransporte()
	{
		return tipoTransporte;
	}

	public void setTipoTransporte(String tipoTransporte)
	{
		this.tipoTransporte = tipoTransporte;
	}

	public String getTransportaDesde()
	{
		return transportaDesde;
	}

	public void setTransportaDesde(String transportaDesde)
	{
		this.transportaDesde = transportaDesde;
	}

	public String getTransportaHasta()
	{
		return transportaHasta;
	}

	public void setTransportaHasta(String transportaHasta)
	{
		this.transportaHasta = transportaHasta;
	}

	public String getUsuarioModifica()
	{
		return usuarioModifica;
	}

	public void setUsuarioModifica(String usuarioModifica)
	{
		this.usuarioModifica = usuarioModifica;
	}

	public String getZonaUrbanaEvento()
	{
		return zonaUrbanaEvento;
	}

	public void setZonaUrbanaEvento(String zonaUrbanaEvento)
	{
		this.zonaUrbanaEvento = zonaUrbanaEvento;
	}

	public String getDireccionEvento()
	{
		return direccionEvento;
	}

	public void setDireccionEvento(String direccionEvento)
	{
		this.direccionEvento = direccionEvento;
	}

	public String getPaisEmpresa() {
		return paisEmpresa;
	}

	public void setPaisEmpresa(String paisEmpresa) {
		this.paisEmpresa = paisEmpresa;
	}

	public String getPaisEvento() {
		return paisEvento;
	}

	public void setPaisEvento(String paisEvento) {
		this.paisEvento = paisEvento;
	}

	public String getPaisExpIdTransporta() {
		return paisExpIdTransporta;
	}

	public void setPaisExpIdTransporta(String paisExpIdTransporta) {
		this.paisExpIdTransporta = paisExpIdTransporta;
	}

	public String getPaisTransporta() {
		return paisTransporta;
	}

	public void setPaisTransporta(String paisTransporta) {
		this.paisTransporta = paisTransporta;
	}

	/**
	 * @return the sgsss
	 */
	public String getSgsss() {
		return sgsss;
	}

	/**
	 * @param sgsss the sgsss to set
	 */
	public void setSgsss(String sgsss) {
		this.sgsss = sgsss;
	}

	/**
	 * @return the tipoRegimen
	 */
	public String getTipoRegimen() {
		return tipoRegimen;
	}

	/**
	 * @param tipoRegimen the tipoRegimen to set
	 */
	public void setTipoRegimen(String tipoRegimen) {
		this.tipoRegimen = tipoRegimen;
	}

	/**
	 * @return the horaEvento
	 */
	public String getHoraEvento() {
		return horaEvento;
	}

	/**
	 * @param horaEvento the horaEvento to set
	 */
	public void setHoraEvento(String horaEvento) {
		this.horaEvento = horaEvento;
	}

	/**
	 * @return the descOcurrencia
	 */
	public String getDescOcurrencia() {
		return descOcurrencia;
	}

	/**
	 * @param descOcurrencia the descOcurrencia to set
	 */
	public void setDescOcurrencia(String descOcurrencia) {
		this.descOcurrencia = descOcurrencia;
	}

	/**
	 * @return the descOtroEvento
	 */
	public String getDescOtroEvento() {
		return descOtroEvento;
	}

	/**
	 * @param descOtroEvento the descOtroEvento to set
	 */
	public void setDescOtroEvento(String descOtroEvento) {
		this.descOtroEvento = descOtroEvento;
	}

	/**
	 * @return the tipoReferencia
	 */
	public String getTipoReferencia() {
		return tipoReferencia;
	}

	/**
	 * @param tipoReferencia the tipoReferencia to set
	 */
	public void setTipoReferencia(String tipoReferencia) {
		this.tipoReferencia = tipoReferencia;
	}

	/**
	 * @return the fechaRemision
	 */
	public String getFechaRemision() {
		return fechaRemision;
	}

	/**
	 * @param fechaRemision the fechaRemision to set
	 */
	public void setFechaRemision(String fechaRemision) {
		this.fechaRemision = fechaRemision;
	}

	/**
	 * @return the horaRemision
	 */
	public String getHoraRemision() {
		return horaRemision;
	}

	/**
	 * @param horaRemision the horaRemision to set
	 */
	public void setHoraRemision(String horaRemision) {
		this.horaRemision = horaRemision;
	}

	/**
	 * @return the prestadorRemite
	 */
	public String getPrestadorRemite() {
		return prestadorRemite;
	}

	/**
	 * @param prestadorRemite the prestadorRemite to set
	 */
	public void setPrestadorRemite(String prestadorRemite) {
		this.prestadorRemite = prestadorRemite;
	}

	/**
	 * @return the profesionalRemite
	 */
	public String getProfesionalRemite() {
		return profesionalRemite;
	}

	/**
	 * @param profesionalRemite the profesionalRemite to set
	 */
	public void setProfesionalRemite(String profesionalRemite) {
		this.profesionalRemite = profesionalRemite;
	}

	/**
	 * @return the cargoProfesionalRemite
	 */
	public String getCargoProfesionalRemite() {
		return cargoProfesionalRemite;
	}

	/**
	 * @param cargoProfesionalRemite the cargoProfesionalRemite to set
	 */
	public void setCargoProfesionalRemite(String cargoProfesionalRemite) {
		this.cargoProfesionalRemite = cargoProfesionalRemite;
	}

	/**
	 * @return the prestadorRecibe
	 */
	public String getPrestadorRecibe() {
		return prestadorRecibe;
	}

	/**
	 * @param prestadorRecibe the prestadorRecibe to set
	 */
	public void setPrestadorRecibe(String prestadorRecibe) {
		this.prestadorRecibe = prestadorRecibe;
	}

	/**
	 * @return the profesionalRecibe
	 */
	public String getProfesionalRecibe() {
		return profesionalRecibe;
	}

	/**
	 * @param profesionalRecibe the profesionalRecibe to set
	 */
	public void setProfesionalRecibe(String profesionalRecibe) {
		this.profesionalRecibe = profesionalRecibe;
	}

	/**
	 * @return the cargoProfesionalRecibe
	 */
	public String getCargoProfesionalRecibe() {
		return cargoProfesionalRecibe;
	}

	/**
	 * @param cargoProfesionalRecibe the cargoProfesionalRecibe to set
	 */
	public void setCargoProfesionalRecibe(String cargoProfesionalRecibe) {
		this.cargoProfesionalRecibe = cargoProfesionalRecibe;
	}

	/**
	 * @return the fechaAceptacion
	 */
	public String getFechaAceptacion() {
		return fechaAceptacion;
	}

	/**
	 * @param fechaAceptacion the fechaAceptacion to set
	 */
	public void setFechaAceptacion(String fechaAceptacion) {
		this.fechaAceptacion = fechaAceptacion;
	}

	/**
	 * @return the horaAceptacion
	 */
	public String getHoraAceptacion() {
		return horaAceptacion;
	}

	/**
	 * @param horaAceptacion the horaAceptacion to set
	 */
	public void setHoraAceptacion(String horaAceptacion) {
		this.horaAceptacion = horaAceptacion;
	}

	/**
	 * @return the otroTipoTransporte
	 */
	public String getOtroTipoTransporte() {
		return otroTipoTransporte;
	}

	/**
	 * @param otroTipoTransporte the otroTipoTransporte to set
	 */
	public void setOtroTipoTransporte(String otroTipoTransporte) {
		this.otroTipoTransporte = otroTipoTransporte;
	}

	/**
	 * @return the zonaTransporte
	 */
	public String getZonaTransporte() {
		return zonaTransporte;
	}

	/**
	 * @param zonaTransporte the zonaTransporte to set
	 */
	public void setZonaTransporte(String zonaTransporte) {
		this.zonaTransporte = zonaTransporte;
	}

	/**
	 * @return the totalFacturadoMedicoQuirurgico
	 */
	public String getTotalFacturadoMedicoQuirurgico() {
		return totalFacturadoMedicoQuirurgico;
	}

	/**
	 * @param totalFacturadoMedicoQuirurgico the totalFacturadoMedicoQuirurgico to set
	 */
	public void setTotalFacturadoMedicoQuirurgico(
			String totalFacturadoMedicoQuirurgico) {
		this.totalFacturadoMedicoQuirurgico = totalFacturadoMedicoQuirurgico;
	}

	/**
	 * @return the totalReclamadoMedicoQuirurgico
	 */
	public String getTotalReclamadoMedicoQuirurgico() {
		return totalReclamadoMedicoQuirurgico;
	}

	/**
	 * @param totalReclamadoMedicoQuirurgico the totalReclamadoMedicoQuirurgico to set
	 */
	public void setTotalReclamadoMedicoQuirurgico(
			String totalReclamadoMedicoQuirurgico) {
		this.totalReclamadoMedicoQuirurgico = totalReclamadoMedicoQuirurgico;
	}

	/**
	 * @return the totalFacturadoTransporte
	 */
	public String getTotalFacturadoTransporte() {
		return totalFacturadoTransporte;
	}

	/**
	 * @param totalFacturadoTransporte the totalFacturadoTransporte to set
	 */
	public void setTotalFacturadoTransporte(String totalFacturadoTransporte) {
		this.totalFacturadoTransporte = totalFacturadoTransporte;
	}

	/**
	 * @return the totalReclamadoTransporte
	 */
	public String getTotalReclamadoTransporte() {
		return totalReclamadoTransporte;
	}

	/**
	 * @param totalReclamadoTransporte the totalReclamadoTransporte to set
	 */
	public void setTotalReclamadoTransporte(String totalReclamadoTransporte) {
		this.totalReclamadoTransporte = totalReclamadoTransporte;
	}

	/**
	 * @return the esReclamacion
	 */
	public String getEsReclamacion() {
		return esReclamacion;
	}

	/**
	 * @param esReclamacion the esReclamacion to set
	 */
	public void setEsReclamacion(String esReclamacion) {
		this.esReclamacion = esReclamacion;
	}

	/**
	 * @return the furips
	 */
	public String getFurips() {
		return furips;
	}

	/**
	 * @param furips the furips to set
	 */
	public void setFurips(String furips) {
		this.furips = furips;
	}

	/**
	 * @return the furpro
	 */
	public String getFurpro() {
		return furpro;
	}

	/**
	 * @param furpro the furpro to set
	 */
	public void setFurpro(String furpro) {
		this.furpro = furpro;
	}

	/**
	 * @return the furtran
	 */
	public String getFurtran() {
		return furtran;
	}

	/**
	 * @param furtran the furtran to set
	 */
	public void setFurtran(String furtran) {
		this.furtran = furtran;
	}

	/**
	 * @return the respuestaGlosa
	 */
	public String getRespuestaGlosa() {
		return respuestaGlosa;
	}

	/**
	 * @param respuestaGlosa the respuestaGlosa to set
	 */
	public void setRespuestaGlosa(String respuestaGlosa) {
		this.respuestaGlosa = respuestaGlosa;
	}

	/**
	 * @return the numeroRadicadoAnterior
	 */
	public String getNumeroRadicadoAnterior() {
		return numeroRadicadoAnterior;
	}

	/**
	 * @param numeroRadicadoAnterior the numeroRadicadoAnterior to set
	 */
	public void setNumeroRadicadoAnterior(String numeroRadicadoAnterior) {
		this.numeroRadicadoAnterior = numeroRadicadoAnterior;
	}

	/**
	 * @return the numeroConsecutivoReclamacion
	 */
	public String getNumeroConsecutivoReclamacion() {
		return numeroConsecutivoReclamacion;
	}

	/**
	 * @param numeroConsecutivoReclamacion the numeroConsecutivoReclamacion to set
	 */
	public void setNumeroConsecutivoReclamacion(String numeroConsecutivoReclamacion) {
		this.numeroConsecutivoReclamacion = numeroConsecutivoReclamacion;
	}

	/**
	 * @return the protesis
	 */
	public String getProtesis() {
		return protesis;
	}

	/**
	 * @param protesis the protesis to set
	 */
	public void setProtesis(String protesis) {
		this.protesis = protesis;
	}

	/**
	 * @return the valorProtesis
	 */
	public String getValorProtesis() {
		return valorProtesis;
	}

	/**
	 * @param valorProtesis the valorProtesis to set
	 */
	public void setValorProtesis(String valorProtesis) {
		this.valorProtesis = valorProtesis;
	}

	/**
	 * @return the adaptacionProtesis
	 */
	public String getAdaptacionProtesis() {
		return adaptacionProtesis;
	}

	/**
	 * @param adaptacionProtesis the adaptacionProtesis to set
	 */
	public void setAdaptacionProtesis(String adaptacionProtesis) {
		this.adaptacionProtesis = adaptacionProtesis;
	}

	/**
	 * @return the valorAdaptacionProtesis
	 */
	public String getValorAdaptacionProtesis() {
		return valorAdaptacionProtesis;
	}

	/**
	 * @param valorAdaptacionProtesis the valorAdaptacionProtesis to set
	 */
	public void setValorAdaptacionProtesis(String valorAdaptacionProtesis) {
		this.valorAdaptacionProtesis = valorAdaptacionProtesis;
	}

	/**
	 * @return the rehabilitacion
	 */
	public String getRehabilitacion() {
		return rehabilitacion;
	}

	/**
	 * @param rehabilitacion the rehabilitacion to set
	 */
	public void setRehabilitacion(String rehabilitacion) {
		this.rehabilitacion = rehabilitacion;
	}

	/**
	 * @return the valorRehabilitacion
	 */
	public String getValorRehabilitacion() {
		return valorRehabilitacion;
	}

	/**
	 * @param valorRehabilitacion the valorRehabilitacion to set
	 */
	public void setValorRehabilitacion(String valorRehabilitacion) {
		this.valorRehabilitacion = valorRehabilitacion;
	}

}
