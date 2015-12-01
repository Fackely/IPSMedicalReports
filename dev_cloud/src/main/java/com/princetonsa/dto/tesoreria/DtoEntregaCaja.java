package com.princetonsa.dto.tesoreria;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Esta clase reune toda la informaci&oacute;n necesaria para mostrar las
 * entregas realizadas a una caja espec&iacute;fica cuando se hace un arqueo,
 * estas son: Entrega a Transportadora de valores o Entrega a Caja Mayor
 * principal
 * 
 * @author Jorge Armando Agudelo Quintero
 * @anexo 226
 */
public class DtoEntregaCaja implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/**
	 * Tipo que indica el movimiento de caja realizado
	 * 
	 * tipo = 0 ---> Entrega Transportadora de Valores 
	 * tipo = 1 ---> Entrega Caja Mayor principal
	 */
	private int tipo;
	
	/**
	 * Valor recibido en el proceso de entrega
	 */
	
	private double valorEntregado;
	
	/**
	 * Nombre de la forma de pago asociada al recibo
	 */
	private String formaPago;
	
	/**
	 * Indicativo de la forma de pago asociada al recibo
	 */
	private int tipoFormaPago;
	
	/**
	 * C&oacute;dogo del movimiento de Caja
	 */
	private long idMovimientoCaja;

	/**
	 * Valor sistema asociado al proceso de entrega por cada uno de los documentos asociados
	 */
	private double valorSistema;
	
	/**
	 * Valor faltante o sobrante resultado del proceso de entrega
	 */
	private BigDecimal valorFaltante;
	
	/**
	 * Consecutivo del registro de detalle de Faltante / Sobrante
	 */
	private Long idDetalleFaltanteSobrante;
	
	/**
	 * C&oacute;digo del documento de soporte asociado
	 */
	private long codigoDocSopMovCaja;

	
	/**
	 * Atributo en donde se guarda la hora del movimiento de entrega que se consulta
	 */
	private String hora;

	/**
	 * Atributo con la fecha del movimiento de entrega que se consulta
	 */
	private Date fecha;
	
	
	/**
	 * Constructor vac&iacute;o, requerido para hacer la proyecci&oacute;n del
	 * ORM
	 */
	public DtoEntregaCaja() {
	}
	
	/**
	 * @return the tipo
	 */
	public int getTipo() {
		return tipo;
	}

	/**
	 * @param tipo the tipo to set
	 */
	public void setTipo(int tipo) {
		this.tipo = tipo;
	}

	/**
	 * @return the valorEntregado
	 */
	public double getValorEntregado() {
		return valorEntregado;
	}

	/**
	 * @param valorEntregado
	 *            the valorEntregado to set
	 */
	public void setValorEntregado(BigDecimal valorEntregado) {
		this.valorEntregado = valorEntregado.doubleValue();
	}

	/**
	 * @return the tipoFormaPago
	 */
	public int getTipoFormaPago() {
		return tipoFormaPago;
	}

	/**
	 * @param tipoFormaPago
	 *            the tipoFormaPago to set
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
	 * @param formaPago
	 *            the formaPago to set
	 */
	public void setFormaPago(String formaPago) {
		this.formaPago = formaPago;
	}

	/**
	 * @return the idMovimientoCaja
	 */
	public long getIdMovimientoCaja() {
		return idMovimientoCaja;
	}

	/**
	 * @param idMovimientoCaja
	 *            the idMovimientoCaja to set
	 */
	public void setIdMovimientoCaja(long idMovimientoCaja) {
		this.idMovimientoCaja = idMovimientoCaja;
	}

	/**
	 * @return the valorSistema
	 */
	public double getValorSistema() {
		return valorSistema;
	}

	/**
	 * @param valorSistema
	 *            the valorSistema to set
	 */
	public void setValorSistema(Object valorSistema) {
	
		if(valorSistema!=null){
			
			this.valorSistema = (Double) valorSistema;
		}
		
	}

	/**
	 * @return the valorFaltante
	 */
	public BigDecimal getValorFaltante() {
		return valorFaltante;
	}

	/**
	 * @param valorFaltante
	 *            the valorFaltante to set
	 */
	public void setValorFaltante(BigDecimal valorFaltante) {
		this.valorFaltante = valorFaltante;
	}

	/**
	 * @return the idDetalleFaltanteSobrante
	 */
	public Long getIdDetalleFaltanteSobrante() {
		return idDetalleFaltanteSobrante;
	}

	/**
	 * @param idDetalleFaltanteSobrante
	 *            the idDetalleFaltanteSobrante to set
	 */
	public void setIdDetalleFaltanteSobrante(Long idDetalleFaltanteSobrante) {
		this.idDetalleFaltanteSobrante = idDetalleFaltanteSobrante;
	}

	/**
	 * @return the codigoDocSopMovCaja
	 */
	public long getCodigoDocSopMovCaja() {
		return codigoDocSopMovCaja;
	}

	/**
	 * @param codigoDocSopMovCaja
	 *            the codigoDocSopMovCaja to set
	 */
	public void setCodigoDocSopMovCaja(long codigoDocSopMovCaja) {
		this.codigoDocSopMovCaja = codigoDocSopMovCaja;
	}

	/**
	 * @param hora the hora to set
	 */
	public void setHora(String hora) {
		this.hora = hora;
	}

	/**
	 * @return the hora
	 */
	public String getHora() {
		return hora;
	}

	/**
	 * @param fecha the fecha to set
	 */
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	/**
	 * @return the fecha
	 */
	public Date getFecha() {
		return fecha;
	}

}
