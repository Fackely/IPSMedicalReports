package com.princetonsa.dto.facturacion;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;

import util.ConstantesBD;
import util.InfoDatosDouble;
import util.UtilidadTexto;

/**
 * 
 * @author wilson
 *
 */
public class DtoDetalleFactura implements Serializable
{
	/**
	 * Versión serial
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	private double codigoDetalleCargo;
	
	/**
	 * 
	 */
	private int codigo;
	
	/**
	 * 
	 */
	private int numeroSolicitud;
	
	/**
	 * 
	 */
	private int codigoFactura;
	
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
	private double porcentajePool;
	
	/**
	 * 
	 */
	private double porcentajeMedico;
	
	/**
	 * 
	 */
	private double valorMedico;
	
	/**
	 * 
	 */
	private String fechaCargo;
	
	/**
	 * 
	 */
	private int cantidadCargo;
	
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
	private int solicitudPago;
	
	/**
	 * 
	 */
	private int codigoTipoCargo;
	
	/**
	 * 
	 */
	private int codigoServicio;
	
	/**
	 * 
	 */
	private int codigoArticulo;
	
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
	private String diagnosticoAcronimoTipoCie;
	
	/**
	 * 
	 */
	private ArrayList<DtoAsociosDetalleFactura> asociosDetalleFactura= new ArrayList<DtoAsociosDetalleFactura>();
	
	/**
	 * 
	 */
	private ArrayList<DtoPaquetizacionDetalleFactura> paquetizacionDetalleFactura= new ArrayList<DtoPaquetizacionDetalleFactura>();
	
	/**
	 * 
	 */
	private boolean esCx;
	
	/**
	 * 
	 */
	private double valorDescuentoComercial;
	
	/**
	 * 
	 */
	private double valorConsumoPaquete;
	
	/**
	 * 
	 */
	private int codigoTipoSolicitud;
	
	/**
	 * 
	 */
	private int codigoEsquemaTarifario;
	
	/**
	 * 
	 */
	private int codigoEstadoHC;
	
	/**
	 * 
	 */
	private InfoDatosDouble programa;
	
	/**
	 * 
	 */
	private BigDecimal valorDctoOdo;
	
	/**
	 * 
	 */
	private BigDecimal valorDctoBono;
	
	/**
	 * 
	 */
	private BigDecimal valorDctoProm;
	
	/**
	 * 
	 */
	private int detallePaqueteOdonConvenio;
	
	/**
	 * 
	 *
	 */
	public DtoDetalleFactura() 
	{
		super();
		this.codigo = ConstantesBD.codigoNuncaValido;
		this.numeroSolicitud = ConstantesBD.codigoNuncaValido;
		this.codigoDetalleCargo =ConstantesBD.codigoNuncaValido;
		this.codigoFactura = ConstantesBD.codigoNuncaValido;
		this.ajusteCreditoMedico = ConstantesBD.codigoNuncaValidoDoubleNegativo;
		this.ajusteDebitoMedico = ConstantesBD.codigoNuncaValidoDoubleNegativo;
		this.setPorcentajePool(ConstantesBD.codigoNuncaValido);
		this.porcentajeMedico = ConstantesBD.codigoNuncaValidoDoubleNegativo;
		this.valorMedico = ConstantesBD.codigoNuncaValidoDoubleNegativo;
		this.fechaCargo = "";
		this.cantidadCargo = ConstantesBD.codigoNuncaValido;
		this.valorCargo = ConstantesBD.codigoNuncaValidoDoubleNegativo;
		this.valorIva = ConstantesBD.codigoNuncaValidoDoubleNegativo;
		this.valorRecargo = ConstantesBD.codigoNuncaValidoDoubleNegativo;
		this.valorTotal = ConstantesBD.codigoNuncaValidoDoubleNegativo;
		this.solicitudPago = ConstantesBD.codigoNuncaValido;
		this.codigoTipoCargo = ConstantesBD.codigoNuncaValido;
		this.codigoServicio = ConstantesBD.codigoNuncaValido;
		this.codigoArticulo = ConstantesBD.codigoNuncaValido;
		this.ajustesCredito = ConstantesBD.codigoNuncaValidoDoubleNegativo;
		this.ajustesDebito = ConstantesBD.codigoNuncaValidoDoubleNegativo;
		this.valorPool = ConstantesBD.codigoNuncaValidoDoubleNegativo;
		this.pool = ConstantesBD.codigoNuncaValido;
		this.codigoMedico = ConstantesBD.codigoNuncaValido;
		this.asociosDetalleFactura= new ArrayList<DtoAsociosDetalleFactura>();
		this.paquetizacionDetalleFactura= new ArrayList<DtoPaquetizacionDetalleFactura>();
		this.diagnosticoAcronimoTipoCie="";
		this.esCx=false;
		this.valorDescuentoComercial=ConstantesBD.codigoNuncaValidoDoubleNegativo;
		this.valorConsumoPaquete=ConstantesBD.codigoNuncaValidoDoubleNegativo;
		this.codigoTipoSolicitud=ConstantesBD.codigoNuncaValido;
		this.codigoEsquemaTarifario=ConstantesBD.codigoNuncaValido;
		this.codigoEstadoHC= ConstantesBD.codigoNuncaValido;
		this.programa= new InfoDatosDouble();
		
		this.valorDctoBono= BigDecimal.ZERO;
		this.valorDctoOdo= BigDecimal.ZERO;
		this.valorDctoProm= BigDecimal.ZERO;
		
		this.detallePaqueteOdonConvenio=0;
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
	 * @return the codigo
	 */
	public int getCodigo() {
		return codigo;
	}

	/**
	 * @param codigo the codigo to set
	 */
	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	/**
	 * @return the codigoArticulo
	 */
	public int getCodigoArticulo() {
		return codigoArticulo;
	}

	/**
	 * @param codigoArticulo the codigoArticulo to set
	 */
	public void setCodigoArticulo(int codigoArticulo) {
		this.codigoArticulo = codigoArticulo;
	}

	/**
	 * @return the codigoFactura
	 */
	public int getCodigoFactura() {
		return codigoFactura;
	}

	/**
	 * @param codigoFactura the codigoFactura to set
	 */
	public void setCodigoFactura(int codigoFactura) {
		this.codigoFactura = codigoFactura;
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
	 * @return the codigoServicio
	 */
	public int getCodigoServicio() {
		return codigoServicio;
	}

	/**
	 * @return the codigoServicio
	 */
	public int getCodigoServicioOArticulo() 
	{
		if(this.getCodigoServicio()>0)
			return codigoServicio;
		else if(this.getCodigoArticulo()>0)
			return codigoArticulo;
		return ConstantesBD.codigoNuncaValido;
	}
	
	/**
	 * @return the codigoServicio
	 */
	public boolean getEsServicio() 
	{
		if(this.getCodigoServicio()>0)
			return true;
		return false;
	}
	
	
	/**
	 * @param codigoServicio the codigoServicio to set
	 */
	public void setCodigoServicio(int codigoServicio) {
		this.codigoServicio = codigoServicio;
	}

	/**
	 * @return the codigoTipoCargo
	 */
	public int getCodigoTipoCargo() {
		return codigoTipoCargo;
	}

	/**
	 * @param codigoTipoCargo the codigoTipoCargo to set
	 */
	public void setCodigoTipoCargo(int codigoTipoCargo) {
		this.codigoTipoCargo = codigoTipoCargo;
	}

	/**
	 * @return the fechaCargo
	 */
	public String getFechaCargo() {
		return fechaCargo;
	}

	/**
	 * @param fechaCargo the fechaCargo to set
	 */
	public void setFechaCargo(String fechaCargo) {
		this.fechaCargo = fechaCargo;
	}

	/**
	 * @return the numeroSolicitud
	 */
	public int getNumeroSolicitud() {
		return numeroSolicitud;
	}

	/**
	 * @param numeroSolicitud the numeroSolicitud to set
	 */
	public void setNumeroSolicitud(int numeroSolicitud) {
		this.numeroSolicitud = numeroSolicitud;
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
	 * @return the solicitudPago
	 */
	public int getSolicitudPago() {
		return solicitudPago;
	}

	/**
	 * @param solicitudPago the solicitudPago to set
	 */
	public void setSolicitudPago(int solicitudPago) {
		this.solicitudPago = solicitudPago;
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
	 * @return the asociosDetalleFactura
	 */
	public ArrayList<DtoAsociosDetalleFactura> getAsociosDetalleFactura() {
		return asociosDetalleFactura;
	}

	/**
	 * @param asociosDetalleFactura the asociosDetalleFactura to set
	 */
	public void setAsociosDetalleFactura(
			ArrayList<DtoAsociosDetalleFactura> asociosDetalleFactura) {
		this.asociosDetalleFactura = asociosDetalleFactura;
	}

	/**
	 * @return the diagnosticoAcronimoTipoCie
	 */
	public String getDiagnosticoAcronimoTipoCie() {
		return diagnosticoAcronimoTipoCie;
	}

	/**
	 * @param diagnosticoAcronimoTipoCie the diagnosticoAcronimoTipoCie to set
	 */
	public void setDiagnosticoAcronimoTipoCie(String diagnosticoAcronimoTipoCie) {
		this.diagnosticoAcronimoTipoCie = diagnosticoAcronimoTipoCie;
	}
	
	/**
	 * @return the diagnosticoAcronimoTipoCie
	 */
	public String getDiagnosticoAcronimo() 
	{
		if(!UtilidadTexto.isEmpty(this.diagnosticoAcronimoTipoCie))
			return diagnosticoAcronimoTipoCie.split("-")[0];
		else
			return "";
	}

	/**
	 * @return the diagnosticoAcronimoTipoCie
	 */
	public int getDiagnosticoTipoCie() 
	{
		if(!UtilidadTexto.isEmpty(this.diagnosticoAcronimoTipoCie))
			return Integer.parseInt(diagnosticoAcronimoTipoCie.split("-")[1]);
		else
			return ConstantesBD.codigoNuncaValido;
	}

	/**
	 * @return the esCx
	 */
	public boolean getEsCx() {
		return esCx;
	}

	/**
	 * @param esCx the esCx to set
	 */
	public void setEsCx(boolean esCx) {
		this.esCx = esCx;
	}

	/**
	 * @return the valorDescuentoComercial
	 */
	public double getValorDescuentoComercial() {
		return valorDescuentoComercial;
	}

	/**
	 * @param valorDescuentoComercial the valorDescuentoComercial to set
	 */
	public void setValorDescuentoComercial(double valorDescuentoComercial) {
		this.valorDescuentoComercial = valorDescuentoComercial;
	}

	/**
	 * @return the valorConsumoPaquete
	 */
	public double getValorConsumoPaquete() {
		return valorConsumoPaquete;
	}

	/**
	 * 
	 * @param valorConsumoPaquete the valorConsumoPaquete to set
	 */
	public void setValorConsumoPaquete(double valorConsumoPaquete) {
		this.valorConsumoPaquete = valorConsumoPaquete;
	}

	/**
	 * @return the codigoTipoSolicitud
	 */
	public int getCodigoTipoSolicitud() {
		return codigoTipoSolicitud;
	}

	/**
	 * @param codigoTipoSolicitud the codigoTipoSolicitud to set
	 */
	public void setCodigoTipoSolicitud(int codigoTipoSolicitud) {
		this.codigoTipoSolicitud = codigoTipoSolicitud;
	}

	/**
	 * @return the paquetizacionDetalleFactura
	 */
	public ArrayList<DtoPaquetizacionDetalleFactura> getPaquetizacionDetalleFactura() {
		return paquetizacionDetalleFactura;
	}

	/**
	 * @param paquetizacionDetalleFactura the paquetizacionDetalleFactura to set
	 */
	public void setPaquetizacionDetalleFactura(
			ArrayList<DtoPaquetizacionDetalleFactura> paquetizacionDetalleFactura) {
		this.paquetizacionDetalleFactura = paquetizacionDetalleFactura;
	}

	/**
	 * @return the codigoEsquemaTarifario
	 * 
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

	/**
	 * @return the codigoEstadoHC
	 */
	public int getCodigoEstadoHC()
	{
		return codigoEstadoHC;
	}

	/**
	 * @param codigoEstadoHC the codigoEstadoHC to set
	 */
	public void setCodigoEstadoHC(int codigoEstadoHC)
	{
		this.codigoEstadoHC = codigoEstadoHC;
	}

	/**
	 * @return the programa
	 */
	public InfoDatosDouble getPrograma() {
		return programa;
	}

	/**
	 * @param programa the programa to set
	 */
	public void setPrograma(InfoDatosDouble programa) {
		this.programa = programa;
	}

	/**
	 * @return the valorDctoOdo
	 */
	public BigDecimal getValorDctoOdo() {
		return valorDctoOdo;
	}

	/**
	 * @param valorDctoOdo the valorDctoOdo to set
	 */
	public void setValorDctoOdo(BigDecimal valorDctoOdo) {
		this.valorDctoOdo = valorDctoOdo;
	}

	/**
	 * @return the valorDctoBono
	 */
	public BigDecimal getValorDctoBono() {
		return valorDctoBono;
	}

	/**
	 * @param valorDctoBono the valorDctoBono to set
	 */
	public void setValorDctoBono(BigDecimal valorDctoBono) {
		this.valorDctoBono = valorDctoBono;
	}

	/**
	 * @return the valorDctoProm
	 */
	public BigDecimal getValorDctoProm() {
		return valorDctoProm;
	}

	/**
	 * @param valorDctoProm the valorDctoProm to set
	 */
	public void setValorDctoProm(BigDecimal valorDctoProm) {
		this.valorDctoProm = valorDctoProm;
	}

	/**
	 * @return the detallePaqueteOdonConvenio
	 */
	public int getDetallePaqueteOdonConvenio() {
		return detallePaqueteOdonConvenio;
	}

	/**
	 * @param detallePaqueteOdonConvenio the detallePaqueteOdonConvenio to set
	 */
	public void setDetallePaqueteOdonConvenio(int detallePaqueteOdonConvenio) {
		this.detallePaqueteOdonConvenio = detallePaqueteOdonConvenio;
	}

	public void setPorcentajePool(double porcentajePool) {
		this.porcentajePool = porcentajePool;
	}

	public double getPorcentajePool() {
		return porcentajePool;
	}

	public double getCodigoDetalleCargo() {
		return codigoDetalleCargo;
	}

	public void setCodigoDetalleCargo(double codigoDetalleCargo) {
		this.codigoDetalleCargo = codigoDetalleCargo;
	}

}
