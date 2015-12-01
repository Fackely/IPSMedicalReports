package com.princetonsa.dto.odontologia;
import java.io.Serializable;
import java.math.BigDecimal;

import util.ConstantesBD;
import util.UtilidadTexto;

@SuppressWarnings("serial")
public class DtoPresupuestoOdontologicoDescuento implements Serializable , Cloneable {
	/**
	 * 
	 */
	private BigDecimal codigo;
	private DtoInfoFechaUsuario usuarioFechaSolicitud;
	private BigDecimal presupuesto;
	private double detalleDescuentoOdontologico;
	private double detalleDescuentoOdontologicoAtencion;
	private DtoInfoFechaUsuario usuarioFechaModifica;
	private String estado;
	private BigDecimal valorDescuento;
	private BigDecimal consecutivo;
	private double porcentajeDcto;
	private double motivo; 
	private String nombreMotivo;
	private String observaciones; 

	private BigDecimal codigoPkLog;
	
	private boolean inclusion;
	
	/**
	 * Código del registro de autorización del descuento odontológico
	 */
	private long codigoAutorizacionPresuDctoOdon;
	
	/**
	 * 
	 */
	public DtoPresupuestoOdontologicoDescuento() {
		
		this.codigo = new BigDecimal(0);
		this.usuarioFechaSolicitud = new DtoInfoFechaUsuario();
		this.presupuesto = new BigDecimal(0);;
		this.detalleDescuentoOdontologico = ConstantesBD.codigoNuncaValido;
		this.usuarioFechaModifica =  new DtoInfoFechaUsuario();
		this.estado= "";
		this.valorDescuento = new BigDecimal(ConstantesBD.codigoNuncaValidoDouble);
		this.consecutivo = new BigDecimal(ConstantesBD.codigoNuncaValidoDouble);
		this.porcentajeDcto=0;
		this.motivo=ConstantesBD.codigoNuncaValido; 
		this.observaciones="";
		this.detalleDescuentoOdontologicoAtencion = ConstantesBD.codigoNuncaValido;
		this.codigoPkLog = new BigDecimal(0);
		this.setNombreMotivo("");
		this.setInclusion(false);
		
		this.codigoAutorizacionPresuDctoOdon = ConstantesBD.codigoNuncaValidoLong;
	}


	/**
	 * @return the consecutivo
	 */
	public BigDecimal getConsecutivo() {
		return consecutivo;
	}


	/**
	 * @param consecutivo the consecutivo to set
	 */
	public void setConsecutivo(BigDecimal consecutivo) {
		this.consecutivo = consecutivo;
	}


	/**
	 * @return the codigo
	 */
	public BigDecimal getCodigo() {
		return codigo;
	}


	/**
	 * @param codigo the codigo to set
	 */
	public void setCodigo(BigDecimal codigo) {
		this.codigo = codigo;
	}


	/**
	 * @return the usuarioFechaSolicitud
	 */
	public DtoInfoFechaUsuario getUsuarioFechaSolicitud() {
		return usuarioFechaSolicitud;
	}


	/**
	 * @param usuarioFechaSolicitud the usuarioFechaSolicitud to set
	 */
	public void setUsuarioFechaSolicitud(DtoInfoFechaUsuario usuarioFechaSolicitud) {
		this.usuarioFechaSolicitud = usuarioFechaSolicitud;
	}


	/**
	 * @return the presupuesto
	 */
	public BigDecimal getPresupuesto() {
		return presupuesto;
	}


	/**
	 * @param presupuesto the presupuesto to set
	 */
	public void setPresupuesto(BigDecimal presupuesto) {
		this.presupuesto = presupuesto;
	}


	

	/**
	 * @return the detalleDescuentoOdontologico
	 */
	public double getDetalleDescuentoOdontologico() {
		return detalleDescuentoOdontologico;
	}


	/**
	 * @param detalleDescuentoOdontologico the detalleDescuentoOdontologico to set
	 */
	public void setDetalleDescuentoOdontologico(double detalleDescuentoOdontologico) {
		this.detalleDescuentoOdontologico = detalleDescuentoOdontologico;
	}

	/**
	 * @return the usuarioFechaModifica
	 */
	public DtoInfoFechaUsuario getUsuarioFechaModifica() {
		return usuarioFechaModifica;
	}


	/**
	 * @param usuarioFechaModifica the usuarioFechaModifica to set
	 */
	public void setUsuarioFechaModifica(DtoInfoFechaUsuario usuarioFechaModifica) {
		this.usuarioFechaModifica = usuarioFechaModifica;
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
	 * @return the valorDescuento
	 */
	public BigDecimal getValorDescuento() {
		return valorDescuento;
	}

	/**
	 * @return the valorDescuento
	 */
	public String getValorDescuentoFormateado() {
		return UtilidadTexto.formatearValores(valorDescuento+"");
	}

	/**
	 * @param valorDescuento the valorDescuento to set
	 */
	public void setValorDescuento(BigDecimal valorDescuento) {
		this.valorDescuento = valorDescuento;
	}

	/**
	 * @return the porcentajeDcto
	 */
	public double getPorcentajeDcto()
	{
		return porcentajeDcto;
	}


	/**
	 * @return the motivo
	 */
	public double getMotivo()
	{
		return motivo;
	}


	/**
	 * @return the observaciones
	 */
	public String getObservaciones()
	{
		return observaciones;
	}


	/**
	 * @param porcentajeDcto the porcentajeDcto to set
	 */
	public void setPorcentajeDcto(double porcentajeDcto)
	{
		this.porcentajeDcto = porcentajeDcto;
	}


	/**
	 * @param motivo the motivo to set
	 */
	public void setMotivo(double motivo)
	{
		this.motivo = motivo;
	}


	/**
	 * @param observaciones the observaciones to set
	 */
	public void setObservaciones(String observaciones)
	{
		this.observaciones = observaciones;
	}


	/**
	 * @return the detalleDescuentoOdontologicoAtencion
	 */
	public double getDetalleDescuentoOdontologicoAtencion()
	{
		return detalleDescuentoOdontologicoAtencion;
	}


	/**
	 * @param detalleDescuentoOdontologicoAtencion the detalleDescuentoOdontologicoAtencion to set
	 */
	public void setDetalleDescuentoOdontologicoAtencion(
			double detalleDescuentoOdontologicoAtencion)
	{
		this.detalleDescuentoOdontologicoAtencion = detalleDescuentoOdontologicoAtencion;
	}





	public BigDecimal getCodigoPkLog() {
		return codigoPkLog;
	}


	public void setCodigoPkLog(BigDecimal codigoPkLog) {
		this.codigoPkLog = codigoPkLog;
	}


	public void setNombreMotivo(String nombreMotivo) {
		this.nombreMotivo = nombreMotivo;
	}


	public String getNombreMotivo() {
		return nombreMotivo;
	}


	/**
	 * Obtiene el valor del atributo inclusion
	 *
	 * @return Retorna atributo inclusion
	 */
	public boolean isInclusion()
	{
		return inclusion;
	}


	/**
	 * Establece el valor del atributo inclusion
	 *
	 * @param valor para el atributo inclusion
	 */
	public void setInclusion(boolean inclusion)
	{
		this.inclusion = inclusion;
	}


	/**
	 * @param codigoAutorizacionPresuDctoOdon the codigoAutorizacionPresuDctoOdon to set
	 */
	public void setCodigoAutorizacionPresuDctoOdon(
			long codigoAutorizacionPresuDctoOdon) {
		this.codigoAutorizacionPresuDctoOdon = codigoAutorizacionPresuDctoOdon;
	}


	/**
	 * @return the codigoAutorizacionPresuDctoOdon
	 */
	public long getCodigoAutorizacionPresuDctoOdon() {
		return codigoAutorizacionPresuDctoOdon;
	}
	
	
	
}
