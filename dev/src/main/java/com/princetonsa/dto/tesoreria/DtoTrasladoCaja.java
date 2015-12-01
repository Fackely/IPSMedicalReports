package com.princetonsa.dto.tesoreria;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Esta clase reune toda la informaci&oacute;n necesaria para mostrar los
 * traslados realizados a una caja espec&iacute;fica cuando se hace un arqueo.
 * 
 * @author Jorge Armando Agudelo Quintero
 * @anexo 226
 */
public class DtoTrasladoCaja implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/**
	 * C&oacute;dogo del movimiento de Caja
	 */
	private long idMovimientoCaja;
	
	/**
	 * Valor trasladado en la solicitud de traslado a caja
	 */
	private double valorTrasladado;
	
	/**
	 * Valor recibido en la aceptaci&oacute;n de traslado a caja
	 */
	private double valorRecibido;
	
	/**
	 * Valor faltante o sobrante resultado del proceso de aceptaci&oacute;n
	 */
	private BigDecimal valorFaltante;

	/**
	 * Nombre de la forma de pago asociada al recibo
	 */
	private String formaPago;
	
	/**
	 * Indicativo de la forma de pago asociada al recibo
	 */
	private int tipoFormaPago;
	
	/**
	 * Consecutivo del registro de detalle de Faltante / Sobrante
	 */
	private Long idDetalleFaltanteSobrante;
	
	/**
	 * Indicativo del tipo de diferencia presentada
	 */
	private String tipoDiferencia;
	
	/**
	 * C&oacute;digo del documento de soporte asociado
	 */
	private long codigoDocSopMovCaja;

	
	/**
	 * Indicativo del tipo de detalle de la forma de pago
	 */
	private int tipoDetalleFormaPago;
	
	
	/**
	 * C&oacute;dogo del movimiento de Caja
	 */
	private long codigoAceptacionSolicitud;
	
	/**
	 * Constructor vac&iacute;o, requerido para hacer la proyecci&oacute;n del
	 * ORM
	 */
	public DtoTrasladoCaja() {

	}

	/**
	 * @return the idMovimientoCaja
	 */
	public long getIdMovimientoCaja() {
		return idMovimientoCaja;
	}

	/**
	 * @param idMovimientoCaja the idMovimientoCaja to set
	 */
	public void setIdMovimientoCaja(long idMovimientoCaja) {
		this.idMovimientoCaja = idMovimientoCaja;
	}

	/**
	 * @return the valorTrasladado
	 */
	public double getValorTrasladado() {
		return valorTrasladado;
	}

	/**
	 * @param valorTrasladado the valorTrasladado to set
	 */
	public void setValorTrasladado(BigDecimal valorTrasladado) {
		
		if(valorTrasladado!=null){
			
			this.valorTrasladado = valorTrasladado.doubleValue();
			
		}else
		{
			this.valorTrasladado = 0;
		}
	}

	/**
	 * @return the valorRecibido
	 */
	public double getValorRecibido() {
		return valorRecibido;
	}

	/**
	 * @param valorRecibido the valorRecibido to set
	 */
	public void setValorRecibido(BigDecimal valorRecibido) {
		
		if(valorRecibido!=null){
			
			this.valorRecibido = valorRecibido.doubleValue();
			
		}else
		{
			this.valorRecibido = 0;
		}
		
	}
	
	/**
	 * @return the valorFaltante
	 */
	public BigDecimal getValorFaltante() {
		return valorFaltante;
	}

	/**
	 * @param valorFaltante the valorFaltante to set
	 */
	public void setValorFaltante(BigDecimal valorFaltante) {
		
		this.valorFaltante = valorFaltante;
	}

	/**
	 * @return the tipoFormaPago
	 */
	public int getTipoFormaPago() {
		return tipoFormaPago;
	}

	/**
	 * @param tipoFormaPago the tipoFormaPago to set
	 */
	public void setTipoFormaPago(int tipoFormaPago) {
		this.tipoFormaPago = tipoFormaPago;
	}

	/**
	 * @return the formaPago
	 */
	public String getFormaPago() {
		return formaPago;
	}

	/**
	 * @param formaPago the formaPago to set
	 */
	public void setFormaPago(String formaPago) {
		this.formaPago = formaPago;
	}

	/**
	 * @return the idDetalleFaltanteSobrante
	 */
	public Long getIdDetalleFaltanteSobrante() {
		return idDetalleFaltanteSobrante;
	}

	/**
	 * @param idDetalleFaltanteSobrante the idDetalleFaltanteSobrante to set
	 */
	public void setIdDetalleFaltanteSobrante(Long idDetalleFaltanteSobrante) {
		this.idDetalleFaltanteSobrante = idDetalleFaltanteSobrante;
	}

	/**
	 * @return the tipoDiferencia
	 */
	public String getTipoDiferencia() {
		return tipoDiferencia;
	}

	/**
	 * @param tipoDiferencia the tipoDiferencia to set
	 */
	public void setTipoDiferencia(String tipoDiferencia) {
		this.tipoDiferencia = tipoDiferencia;
	}

	/**
	 * @return the codigoDocSopMovCaja
	 */
	public long getCodigoDocSopMovCaja() {
		return codigoDocSopMovCaja;
	}

	/**
	 * @param codigoDocSopMovCaja the codigoDocSopMovCaja to set
	 */
	public void setCodigoDocSopMovCaja(long codigoDocSopMovCaja) {
		this.codigoDocSopMovCaja = codigoDocSopMovCaja;
	}

	/**
	 * @param tipoDetalleFormaPago the tipoDetalleFormaPago to set
	 */
	public void setTipoDetalleFormaPago(int tipoDetalleFormaPago) {
		this.tipoDetalleFormaPago = tipoDetalleFormaPago;
	}

	/**
	 * @return the tipoDetalleFormaPago
	 */
	public int getTipoDetalleFormaPago() {
		return tipoDetalleFormaPago;
	}

	/**
	 * @param codigoAceptacionSolicitud the codigoAceptacionSolicitud to set
	 */
	public void setCodigoAceptacionSolicitud(long codigoAceptacionSolicitud) {
		this.codigoAceptacionSolicitud = codigoAceptacionSolicitud;
	}

	/**
	 * @return the codigoAceptacionSolicitud
	 */
	public long getCodigoAceptacionSolicitud() {
		return codigoAceptacionSolicitud;
	}

}
