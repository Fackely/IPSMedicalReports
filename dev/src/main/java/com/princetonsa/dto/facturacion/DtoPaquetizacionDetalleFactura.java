package com.princetonsa.dto.facturacion;

import java.io.Serializable;

import util.ConstantesBD;
import util.InfoDatosInt;

/**
 * 
 * @author wilson
 *
 */
public class DtoPaquetizacionDetalleFactura implements Serializable
{
	/**
	 * 
	 */
	private double codigo;
	
	/**
	 * 
	 */
	private int codigoDetalleFactura;
	
	/**
	 * 
	 */
	private InfoDatosInt servicio;
	
	/**
	 * 
	 */
	private InfoDatosInt articulo;
	
	/**
	 * 
	 */
	private double ajusteCreditoMedico;
	
	/**
	 * 
	 */
	private double ajusteDebitoMedico;
	
	/**
	 * 
	 */
	private double valorCargo;
	
	/**
	 * 
	 */
	private double valorIva;
	
	/**
	 * 
	 */
	private double valorRecargo;
	
	/**
	 * 
	 */
	private double valorTotal;
	
	/**
	 * 
	 */
	private double valorDifConsumoValor;
	
	/**
	 * 
	 */
	private double ajustesCredito;
	
	/**
	 * 
	 */
	private double ajustesDebito;

	/**
	 * 
	 */
	private double porcentajeMedico;
	
	/**
	 * 
	 */
	private double porcentajePool;
	
	/**
	 * 
	 */
	private InfoDatosInt servicioCx;
	
	/**
	 * 
	 */
	private double valorAsocio;
	
	/**
	 * 
	 */
	private int tipoAsocio;
	
	/**
	 * 
	 */
	private double valorMedico;
	
	/**
	 * 
	 */
	private double valorPool;
	
	/**
	 * 
	 */
	private int pool;
	
	/**
	 * 
	 */
	private int codigoMedico;
	
	/**
	 * 
	 */
	private int soliPago;	
	
	/**
	 * 
	 */
	private int cantidadCargo;
	
	/**
	 * 
	 */
	private int tipoCargo;
	
	/**
	 * 
	 */
	private int tipoSolicitud;
	
	/**
	 * 
	 */
	private int solicitud;
	
	/**
	 * 
	 */
	private double codigoDetalleCargo;
	
	/**
	 * 
	 */
	private int codigoMedicoAsocio;
	
	/**
	 * 
	 */
	private int codigoEspecialidadAsocio;
	
	/**
	 * 
	 */
	private int codigoEsquemaTarifario;
	
	/**
	 * 
	 * @param codigo
	 * @param codigoDetalleFactura
	 * @param servicio
	 * @param articulo
	 * @param ajusteCreditoMedico
	 * @param ajusteDebitoMedico
	 * @param valorCargo
	 * @param valorIva
	 * @param valorRecargo
	 * @param valorTotal
	 * @param valorDifConsumoValor
	 * @param ajustesCredito
	 * @param ajustesDebito
	 */
	public DtoPaquetizacionDetalleFactura(double codigo, int codigoDetalleFactura, InfoDatosInt servicio, InfoDatosInt articulo, double ajusteCreditoMedico, double ajusteDebitoMedico, double valorCargo, double valorIva, double valorRecargo, double valorTotal, double valorDifConsumoValor, double ajustesCredito, double ajustesDebito, double porcentajeMedico, double porcentajePool, InfoDatosInt servicioCx, double valorAsocio, int tipoAsocio, double valorMedico,	double valorPool, int pool,	int codigoMedico, int soliPago,	int cantidadCargo, int tipoCargo, int tipoSolicitud, int solicitud, double codigoDetalleCargo, int codigoEsquematarifario, int codigoMedicoAsocio, int codigoEspecialidadAsocio)
	{
		super();
		this.codigo = codigo;
		this.codigoDetalleFactura= codigoDetalleFactura;
		this.servicio = servicio;
		this.articulo = articulo;
		this.ajusteCreditoMedico = ajusteCreditoMedico;
		this.ajusteDebitoMedico = ajusteDebitoMedico;
		this.valorCargo = valorCargo;
		this.valorIva = valorIva;
		this.valorRecargo = valorRecargo;
		this.valorTotal = valorTotal;
		this.valorDifConsumoValor = valorDifConsumoValor;
		this.ajustesCredito = ajustesCredito;
		this.ajustesDebito = ajustesDebito;
		this.porcentajeMedico= porcentajeMedico;
		this.porcentajePool= porcentajePool;
		
		this.servicioCx= servicioCx;
		this.valorAsocio=valorAsocio; 
		this.tipoAsocio=tipoAsocio;
		this.valorMedico=valorMedico;	
		this.valorPool=valorPool;
		this.pool= pool;
		this.codigoMedico=codigoMedico;
		this.soliPago=soliPago;	
		this.cantidadCargo=cantidadCargo;
		this.tipoCargo=tipoCargo;
		this.tipoSolicitud=tipoSolicitud;
		this.solicitud=solicitud;
		this.codigoDetalleCargo=codigoDetalleCargo;
		
		this.codigoEspecialidadAsocio=codigoEspecialidadAsocio;
		this.codigoMedicoAsocio= codigoMedicoAsocio;
		this.codigoEsquemaTarifario= codigoEsquematarifario;
	}

	/**
	 * 
	 *
	 */
	public DtoPaquetizacionDetalleFactura() 
	{
		this.codigo = ConstantesBD.codigoNuncaValidoDoubleNegativo;
		this.codigoDetalleFactura= ConstantesBD.codigoNuncaValido;
		this.servicio = new InfoDatosInt();
		this.articulo = new InfoDatosInt();
		this.ajusteCreditoMedico = ConstantesBD.codigoNuncaValidoDoubleNegativo;
		this.ajusteDebitoMedico = ConstantesBD.codigoNuncaValidoDoubleNegativo;
		this.valorCargo = ConstantesBD.codigoNuncaValidoDoubleNegativo;
		this.valorIva = ConstantesBD.codigoNuncaValidoDoubleNegativo;
		this.valorRecargo = ConstantesBD.codigoNuncaValidoDoubleNegativo;
		this.valorTotal = ConstantesBD.codigoNuncaValidoDoubleNegativo;
		this.valorDifConsumoValor = ConstantesBD.codigoNuncaValidoDoubleNegativo;
		this.ajustesCredito = ConstantesBD.codigoNuncaValidoDoubleNegativo;
		this.ajustesDebito = ConstantesBD.codigoNuncaValidoDoubleNegativo;
		this.porcentajeMedico=ConstantesBD.codigoNuncaValidoDoubleNegativo;
		this.porcentajePool=ConstantesBD.codigoNuncaValidoDoubleNegativo;

		this.servicioCx=new InfoDatosInt();
		this.valorAsocio=ConstantesBD.codigoNuncaValidoDoubleNegativo; 
		this.tipoAsocio=ConstantesBD.codigoNuncaValido;
		this.valorMedico=ConstantesBD.codigoNuncaValidoDoubleNegativo;	
		this.valorPool=ConstantesBD.codigoNuncaValidoDoubleNegativo;
		this.pool= ConstantesBD.codigoNuncaValido;
		this.codigoMedico=ConstantesBD.codigoNuncaValido;
		this.soliPago=ConstantesBD.codigoNuncaValido;	
		this.cantidadCargo=ConstantesBD.codigoNuncaValido;
		this.tipoCargo=ConstantesBD.codigoNuncaValido;
		this.tipoSolicitud=ConstantesBD.codigoNuncaValido;
		this.solicitud=ConstantesBD.codigoNuncaValido;
		this.codigoDetalleCargo=ConstantesBD.codigoNuncaValidoDoubleNegativo;
		
		this.codigoEspecialidadAsocio=ConstantesBD.codigoNuncaValido;
		this.codigoMedicoAsocio= ConstantesBD.codigoNuncaValido;
		this.codigoEsquemaTarifario= ConstantesBD.codigoNuncaValido;
	
	}

	/**
	 * @return the ajusteCreditoMedico
	 */
	public double getAjusteCreditoMedico() {
		return ajusteCreditoMedico;
	}

	/**
	 * @param ajusteCreditoMedico the ajusteCreditoMedico to set
	 */
	public void setAjusteCreditoMedico(double ajusteCreditoMedico) {
		this.ajusteCreditoMedico = ajusteCreditoMedico;
	}

	/**
	 * @return the ajusteDebitoMedico
	 */
	public double getAjusteDebitoMedico() {
		return ajusteDebitoMedico;
	}

	/**
	 * @param ajusteDebitoMedico the ajusteDebitoMedico to set
	 */
	public void setAjusteDebitoMedico(double ajusteDebitoMedico) {
		this.ajusteDebitoMedico = ajusteDebitoMedico;
	}

	/**
	 * @return the ajustesCredito
	 */
	public double getAjustesCredito() {
		return ajustesCredito;
	}

	/**
	 * @param ajustesCredito the ajustesCredito to set
	 */
	public void setAjustesCredito(double ajustesCredito) {
		this.ajustesCredito = ajustesCredito;
	}

	/**
	 * @return the ajustesDebito
	 */
	public double getAjustesDebito() {
		return ajustesDebito;
	}

	/**
	 * @param ajustesDebito the ajustesDebito to set
	 */
	public void setAjustesDebito(double ajustesDebito) {
		this.ajustesDebito = ajustesDebito;
	}

	/**
	 * @return the articulo
	 */
	public InfoDatosInt getArticulo() {
		return articulo;
	}

	/**
	 * @param articulo the articulo to set
	 */
	public void setArticulo(InfoDatosInt articulo) {
		this.articulo = articulo;
	}

	/**
	 * @return the codigo
	 */
	public double getCodigo() {
		return codigo;
	}

	/**
	 * @param codigo the codigo to set
	 */
	public void setCodigo(double codigo) {
		this.codigo = codigo;
	}

	/**
	 * @return the servicio
	 */
	public InfoDatosInt getServicio() {
		return servicio;
	}

	/**
	 * @param servicio the servicio to set
	 */
	public void setServicio(InfoDatosInt servicio) {
		this.servicio = servicio;
	}

	/**
	 * @return the valorCargo
	 */
	public double getValorCargo() {
		return valorCargo;
	}

	/**
	 * @param valorCargo the valorCargo to set
	 */
	public void setValorCargo(double valorCargo) {
		this.valorCargo = valorCargo;
	}

	/**
	 * @return the valorDifConsumoValor
	 */
	public double getValorDifConsumoValor() {
		return valorDifConsumoValor;
	}

	/**
	 * @param valorDifConsumoValor the valorDifConsumoValor to set
	 */
	public void setValorDifConsumoValor(double valorDifConsumoValor) {
		this.valorDifConsumoValor = valorDifConsumoValor;
	}

	/**
	 * @return the valorIva
	 */
	public double getValorIva() {
		return valorIva;
	}

	/**
	 * @param valorIva the valorIva to set
	 */
	public void setValorIva(double valorIva) {
		this.valorIva = valorIva;
	}

	/**
	 * @return the valorRecargo
	 */
	public double getValorRecargo() {
		return valorRecargo;
	}

	/**
	 * @param valorRecargo the valorRecargo to set
	 */
	public void setValorRecargo(double valorRecargo) {
		this.valorRecargo = valorRecargo;
	}

	/**
	 * @return the valorTotal
	 */
	public double getValorTotal() {
		return valorTotal;
	}

	/**
	 * @param valorTotal the valorTotal to set
	 */
	public void setValorTotal(double valorTotal) {
		this.valorTotal = valorTotal;
	}

	/**
	 * @return the codigoDetalleFactura
	 */
	public int getCodigoDetalleFactura() {
		return codigoDetalleFactura;
	}

	/**
	 * @param codigoDetalleFactura the codigoDetalleFactura to set
	 */
	public void setCodigoDetalleFactura(int codigoDetalleFactura) {
		this.codigoDetalleFactura = codigoDetalleFactura;
	}
	
	/**
	 * @return the getCodigoServicioArticulo
	 */
	public int getCodigoServicioOArticulo() 
	{
		if(this.servicio.getCodigo()>0)
			return this.servicio.getCodigo();
		if(this.articulo.getCodigo()>0)
			return this.articulo.getCodigo();
		return ConstantesBD.codigoNuncaValido;
	}

	/**
	 * 
	 * @return
	 */
	public boolean getEsServicio()
	{
		if(this.servicio.getCodigo()>0)
			return true;
		return false;
	}

	/**
	 * @return the porcentajeMedico
	 */
	public double getPorcentajeMedico() {
		return porcentajeMedico;
	}

	/**
	 * @param porcentajeMedico the porcentajeMedico to set
	 */
	public void setPorcentajeMedico(double porcentajeMedico) {
		this.porcentajeMedico = porcentajeMedico;
	}

	/**
	 * @return the porcentajePool
	 */
	public double getPorcentajePool() {
		return porcentajePool;
	}

	/**
	 * @param porcentajePool the porcentajePool to set
	 */
	public void setPorcentajePool(double porcentajePool) {
		this.porcentajePool = porcentajePool;
	}

	/**
	 * @return the servicioCx
	 */
	public InfoDatosInt getServicioCx() {
		return servicioCx;
	}

	/**
	 * @param servicioCx the servicioCx to set
	 */
	public void setServicioCx(InfoDatosInt servicioCx) {
		this.servicioCx = servicioCx;
	}

	/**
	 * @return the cantidadCargo
	 */
	public int getCantidadCargo() {
		return cantidadCargo;
	}

	/**
	 * @param cantidadCargo the cantidadCargo to set
	 */
	public void setCantidadCargo(int cantidadCargo) {
		this.cantidadCargo = cantidadCargo;
	}

	/**
	 * @return the codigoMedico
	 */
	public int getCodigoMedico() {
		return codigoMedico;
	}

	/**
	 * @param codigoMedico the codigoMedico to set
	 */
	public void setCodigoMedico(int codigoMedico) {
		this.codigoMedico = codigoMedico;
	}

	/**
	 * @return the pool
	 */
	public int getPool() {
		return pool;
	}

	/**
	 * @param pool the pool to set
	 */
	public void setPool(int pool) {
		this.pool = pool;
	}

	/**
	 * @return the soliPago
	 */
	public int getSoliPago() {
		return soliPago;
	}

	/**
	 * @param soliPago the soliPago to set
	 */
	public void setSoliPago(int soliPago) {
		this.soliPago = soliPago;
	}

	/**
	 * @return the tipoAsocio
	 */
	public int getTipoAsocio() {
		return tipoAsocio;
	}

	/**
	 * @param tipoAsocio the tipoAsocio to set
	 */
	public void setTipoAsocio(int tipoAsocio) {
		this.tipoAsocio = tipoAsocio;
	}

	/**
	 * @return the tipoCargo
	 */
	public int getTipoCargo() {
		return tipoCargo;
	}

	/**
	 * @param tipoCargo the tipoCargo to set
	 */
	public void setTipoCargo(int tipoCargo) {
		this.tipoCargo = tipoCargo;
	}

	/**
	 * @return the tipoSolicitud
	 */
	public int getTipoSolicitud() {
		return tipoSolicitud;
	}

	/**
	 * @param tipoSolicitud the tipoSolicitud to set
	 */
	public void setTipoSolicitud(int tipoSolicitud) {
		this.tipoSolicitud = tipoSolicitud;
	}

	/**
	 * @return the valorAsocio
	 */
	public double getValorAsocio() {
		return valorAsocio;
	}

	/**
	 * @param valorAsocio the valorAsocio to set
	 */
	public void setValorAsocio(double valorAsocio) {
		this.valorAsocio = valorAsocio;
	}

	/**
	 * @return the valorMedico
	 */
	public double getValorMedico() {
		return valorMedico;
	}

	/**
	 * @param valorMedico the valorMedico to set
	 */
	public void setValorMedico(double valorMedico) {
		this.valorMedico = valorMedico;
	}

	/**
	 * @return the valorPool
	 */
	public double getValorPool() {
		return valorPool;
	}

	/**
	 * @param valorPool the valorPool to set
	 */
	public void setValorPool(double valorPool) {
		this.valorPool = valorPool;
	}

	/**
	 * @return the solicitud
	 */
	public int getSolicitud() {
		return solicitud;
	}

	/**
	 * @param solicitud the solicitud to set
	 */
	public void setSolicitud(int solicitud) {
		this.solicitud = solicitud;
	}

	/**
	 * @return the codigoDetalleCargo
	 */
	public double getCodigoDetalleCargo() {
		return codigoDetalleCargo;
	}

	/**
	 * @param codigoDetalleCargo the codigoDetalleCargo to set
	 */
	public void setCodigoDetalleCargo(double codigoDetalleCargo) {
		this.codigoDetalleCargo = codigoDetalleCargo;
	}

	/**
	 * @return the codigoMedicoAsocio
	 */
	public int getCodigoMedicoAsocio() {
		return codigoMedicoAsocio;
	}

	/**
	 * @param codigoMedicoAsocio the codigoMedicoAsocio to set
	 */
	public void setCodigoMedicoAsocio(int codigoMedicoAsocio) {
		this.codigoMedicoAsocio = codigoMedicoAsocio;
	}

	/**
	 * @return the codigoEspecialidadAsocio
	 */
	public int getCodigoEspecialidadAsocio() {
		return codigoEspecialidadAsocio;
	}

	/**
	 * @param codigoEspecialidadAsocio the codigoEspecialidadAsocio to set
	 */
	public void setCodigoEspecialidadAsocio(int codigoEspecialidadAsocio) {
		this.codigoEspecialidadAsocio = codigoEspecialidadAsocio;
	}

	/**
	 * @return the codigoEsquemaTarifario
	 */
	public int getCodigoEsquemaTarifario() {
		return codigoEsquemaTarifario;
	}

	/**
	 * @param codigoEsquemaTarifario the codigoEsquemaTarifario to set
	 */
	public void setCodigoEsquemaTarifario(int codigoEsquemaTarifario) {
		this.codigoEsquemaTarifario = codigoEsquemaTarifario;
	}
	
	
}
