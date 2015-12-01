package com.princetonsa.dto.odontologia;

import java.io.Serializable;
import java.math.BigDecimal;

import util.ConstantesBD;

/**
 * 
 * @author axioma
 *
 */
public class DtoPresupuestoDetalleServiciosProgramaDao implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	private BigDecimal codigoPk;
	private BigDecimal presupuestoOdoConvenio;
	private Double programa;
	private int servicio;
	private BigDecimal valorUnitarioServicio;
	private double porcentajeDctoPromocionServicio;
	private BigDecimal valorDctoPromocionServicio;
	private double porcentajeDctoBonoServicio;
	private BigDecimal valorDctoBonoServicio;
	private BigDecimal dctoComercialUnitario;
	private DtoInfoFechaUsuario FHU;
	private String errorCalculoTarifa;
	private BigDecimal valorHonorarioDctoPromocionServicio;
	private double porcentajeHonorarioDctoPromocionServicio;
	
	/**
	 * Esquema tarifario con el cual se calculan los valores
	 * al momento de realizar el presupuesto
	 */
	private int esquemaTarifario;
	
	/**
	 * Indica el descuento del porcentaje odontológico que 
	 * aplica para el Programa o servicio
	 */
	private BigDecimal porcentajeDescuentoOdontologico;
	
	/**
	 * 
	 * @param codigoPk
	 * @param presupuestoOdoConvenio
	 * @param programa
	 * @param servicio
	 * @param valorUnitarioServicio
	 * @param porcentajeDctoPromocionServicio
	 * @param valorDctoPromocionServicio
	 * @param porcentajeDctoBonoServicio
	 * @param valorDctoBonoServicio
	 * @param dctoComercialUnitario
	 * @param fhu
	 */
	public DtoPresupuestoDetalleServiciosProgramaDao()
	{
		super();
		this.codigoPk = new BigDecimal(0);
		this.presupuestoOdoConvenio = new BigDecimal(0);
		this.programa = 0.0;
		this.servicio = 0;
		this.valorUnitarioServicio = new BigDecimal(0);
		this.porcentajeDctoPromocionServicio = 0.0;
		this.valorDctoPromocionServicio = new BigDecimal(0);
		this.porcentajeDctoBonoServicio = 0.0;
		this.valorDctoBonoServicio = new BigDecimal(0);
		this.dctoComercialUnitario = new BigDecimal(0);
		this.FHU = new DtoInfoFechaUsuario();
		this.errorCalculoTarifa="";
		this.valorHonorarioDctoPromocionServicio= BigDecimal.ZERO;
		this.porcentajeHonorarioDctoPromocionServicio= 0;
		
		this.esquemaTarifario = ConstantesBD.codigoNuncaValido;
		
		this.porcentajeDescuentoOdontologico = new BigDecimal(0);
	}

	/**
	 * 
	 * @return
	 */
	public String loggerPromocion()
	{
		return "porcentajeDctoPromocionServicio-->"+porcentajeDctoPromocionServicio+" valorDctoPromocionServicio-->"+valorDctoPromocionServicio+" valorHonorarioDctoPromocionServicio-->"+valorHonorarioDctoPromocionServicio+" porcentajeHonorarioDctoPromocionServicio-->"+porcentajeHonorarioDctoPromocionServicio;
	}
	
	/**
	 * @return the codigoPk
	 */
	public BigDecimal getCodigoPk()
	{
		return codigoPk;
	}

	/**
	 * @return the presupuestoOdoConvenio
	 */
	public BigDecimal getPresupuestoOdoConvenio()
	{
		return presupuestoOdoConvenio;
	}

	/**
	 * @return the programa
	 */
	public Double getPrograma()
	{
		return programa;
	}

	/**
	 * @return the servicio
	 */
	public int getServicio()
	{
		return servicio;
	}

	/**
	 * @return the valorUnitarioServicio
	 */
	public BigDecimal getValorUnitarioServicio()
	{
		return valorUnitarioServicio;
	}

	/**
	 * @return the porcentajeDctoPromocionServicio
	 */
	public double getPorcentajeDctoPromocionServicio()
	{
		return porcentajeDctoPromocionServicio;
	}

	/**
	 * @return the valorDctoPromocionServicio
	 */
	public BigDecimal getValorDctoPromocionServicio()
	{
		return valorDctoPromocionServicio;
	}

	/**
	 * @return the porcentajeDctoBonoServicio
	 */
	public double getPorcentajeDctoBonoServicio()
	{
		return porcentajeDctoBonoServicio;
	}

	/**
	 * @return the valorDctoBonoServicio
	 */
	public BigDecimal getValorDctoBonoServicio()
	{
		return valorDctoBonoServicio;
	}

	/**
	 * @return the dctoComercialUnitario
	 */
	public BigDecimal getDctoComercialUnitario()
	{
		return dctoComercialUnitario;
	}

	/**
	 * @return the fHU
	 */
	public DtoInfoFechaUsuario getFHU()
	{
		return FHU;
	}

	/**
	 * @param codigoPk the codigoPk to set
	 */
	public void setCodigoPk(BigDecimal codigoPk)
	{
		this.codigoPk = codigoPk;
	}

	/**
	 * @param presupuestoOdoConvenio the presupuestoOdoConvenio to set
	 */
	public void setPresupuestoOdoConvenio(BigDecimal presupuestoOdoConvenio)
	{
		this.presupuestoOdoConvenio = presupuestoOdoConvenio;
	}

	/**
	 * @param programa the programa to set
	 */
	public void setPrograma(Double programa)
	{
		this.programa = programa;
	}

	/**
	 * @param servicio the servicio to set
	 */
	public void setServicio(int servicio)
	{
		this.servicio = servicio;
	}

	/**
	 * @param valorUnitarioServicio the valorUnitarioServicio to set
	 */
	public void setValorUnitarioServicio(BigDecimal valorUnitarioServicio)
	{
		this.valorUnitarioServicio = valorUnitarioServicio;
	}

	/**
	 * @param porcentajeDctoPromocionServicio the porcentajeDctoPromocionServicio to set
	 */
	public void setPorcentajeDctoPromocionServicio(
			double porcentajeDctoPromocionServicio)
	{
		this.porcentajeDctoPromocionServicio = porcentajeDctoPromocionServicio;
	}

	/**
	 * @param valorDctoPromocionServicio the valorDctoPromocionServicio to set
	 */
	public void setValorDctoPromocionServicio(BigDecimal valorDctoPromocionServicio)
	{
		this.valorDctoPromocionServicio = valorDctoPromocionServicio;
	}

	/**
	 * @param porcentajeDctoBonoServicio the porcentajeDctoBonoServicio to set
	 */
	public void setPorcentajeDctoBonoServicio(double porcentajeDctoBonoServicio)
	{
		this.porcentajeDctoBonoServicio = porcentajeDctoBonoServicio;
	}

	/**
	 * @param valorDctoBonoServicio the valorDctoBonoServicio to set
	 */
	public void setValorDctoBonoServicio(BigDecimal valorDctoBonoServicio)
	{
		this.valorDctoBonoServicio = valorDctoBonoServicio;
	}

	/**
	 * @param dctoComercialUnitario the dctoComercialUnitario to set
	 */
	public void setDctoComercialUnitario(BigDecimal dctoComercialUnitario)
	{
		this.dctoComercialUnitario = dctoComercialUnitario;
	}

	/**
	 * @param fhu the fHU to set
	 */
	public void setFHU(DtoInfoFechaUsuario fhu)
	{
		FHU = fhu;
	}

	/**
	 * @return the errorCalculoTarifa
	 */
	public String getErrorCalculoTarifa()
	{
		return errorCalculoTarifa;
	}

	/**
	 * @param errorCalculoTarifa the errorCalculoTarifa to set
	 */
	public void setErrorCalculoTarifa(String errorCalculoTarifa)
	{
		this.errorCalculoTarifa = errorCalculoTarifa;
	}

	/**
	 * @return the valorHonorarioDctoPromocionServicio
	 */
	public BigDecimal getValorHonorarioDctoPromocionServicio()
	{
		return valorHonorarioDctoPromocionServicio;
	}

	/**
	 * @param valorHonorarioDctoPromocionServicio the valorHonorarioDctoPromocionServicio to set
	 */
	public void setValorHonorarioDctoPromocionServicio(
			BigDecimal valorHonorarioDctoPromocionServicio)
	{
		this.valorHonorarioDctoPromocionServicio = valorHonorarioDctoPromocionServicio;
	}

	/**
	 * @return the porcentajeHonorarioDctoPromocionServicio
	 */
	public double getPorcentajeHonorarioDctoPromocionServicio()
	{
		return porcentajeHonorarioDctoPromocionServicio;
	}

	/**
	 * @param porcentajeHonorarioDctoPromocionServicio the porcentajeHonorarioDctoPromocionServicio to set
	 */
	public void setPorcentajeHonorarioDctoPromocionServicio(
			double porcentajeHonorarioDctoPromocionServicio)
	{
		this.porcentajeHonorarioDctoPromocionServicio = porcentajeHonorarioDctoPromocionServicio;
	}

	/**
	 * @param esquemaTarifario the esquemaTarifario to set
	 */
	public void setEsquemaTarifario(int esquemaTarifario) {
		this.esquemaTarifario = esquemaTarifario;
	}

	/**
	 * @return the esquemaTarifario
	 */
	public int getEsquemaTarifario() {
		return esquemaTarifario;
	}

	/**
	 * @param porcentajeDescuentoOdontologico the porcentajeDescuentoOdontologico to set
	 */
	public void setPorcentajeDescuentoOdontologico(
			BigDecimal porcentajeDescuentoOdontologico) {
		this.porcentajeDescuentoOdontologico = porcentajeDescuentoOdontologico;
	}

	/**
	 * @return the porcentajeDescuentoOdontologico
	 */
	public BigDecimal getPorcentajeDescuentoOdontologico() {
		return porcentajeDescuentoOdontologico;
	}
	
}