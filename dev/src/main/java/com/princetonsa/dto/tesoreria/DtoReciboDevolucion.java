package com.princetonsa.dto.tesoreria;

import java.io.Serializable;
import java.util.Date;

import com.servinte.axioma.orm.RecibosCajaId;

/**
 * Esta clase reune toda la informaci&oacute;n necesaria para mostrar los Recibos de Caja o
 * las Devoluciones de recibos de caja realizadas a una caja
 * espec&iacute;fica cuando se hace un arqueo.
 * 
 * @author Jorge Armando Agudelo Quintero
 * @anexo 226
 */
public class DtoReciboDevolucion  implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private static int RECIBO_CAJA = 0;
	private static int DEVOLUCION_RECIBO_CAJA = 1;

	/*
	 * tipo = 0 ---> Recibo de Caja 
	 * tipo = 1 ---> Devolución de Recibo de Caja
	 */
	private int tipo;
	
	/**
	 * 
	 */
	private String consecutivoRecibosCaja;
	
	/**
	 * N&uacute;mero del recibo de caja asociado
	 */
	private String numeroReciboCaja;
	
	/**
	 * C&oacute;digo de la devoluci&oacute;n asociada
	 */
	private int codigoDevolucion;
	
	/**
	 * Consecutivo de la devoluci&oacute;n asociada
	 */
	private long consecutivoDevolucion;
	
	/**
	 * Acronimo del estado de la devoluci&oacute;n
	 */
	private String acronimoEstadoDevolucion;
	
	/**
	 * C&oacute;digo del estado del recibo de caja
	 */
	private int codigoEstadoReciboCaja;
	
	/**
	 * Estado del recibo de caja
	 */
	private String estadoRecibo;
	
	/**
	 * Nombre de la persona asociada al recibo
	 */
	private String recibidoDe;
	
	/**
	 * N&uacute;mero de identificaci&oacute;n de la persona asociada al recibo
	 */
	private String numIdentificacion;
	
	/**
	 * Nombre de la forma de pago asociada al recibo
	 */
	private String formaPago;
	
	/**
	 * Indicativo de la forma de pago asociada al recibo
	 */
	private int tipoFormaPago;
	
	/**
	 * Valor asociado al recibo o a la devoluci&oacute;n
	 */
	private double valor;
	
	/**
	 * C&oacute;digo del detalle del recibo de pago
	 */
	private int idDetalleReciboPago;
	
	/**
	 * Nombre de la Entidad Financiera
	 */
	private String entidad_financiera;
	
	/**
	 * C&oacute;digo del recibo de caja
	 */
	private RecibosCajaId recibosCajaId;

	/**
	 * Atributo en donde se guarda la hora de la devoluci&oacute;n que se consulta,
	 * dependiendo del estado consultado.
	 */
	private String horaDevolucion;

	/**
	 * Atributo con la fecha del movimiento. En el caso de la devoluci&oacute;n
	 * se tiene en cuenta la fecha asociada al estado consultado.
	 */
	private Date fecha;
	
	/**
	 * Constructor vac&iacute;o, requerido para hacer la proyecci&oacute;n del ORM
	 */
	public DtoReciboDevolucion() {
	}

	public DtoReciboDevolucion(String numeroReciboCaja, int codigoEstadoReciboCaja, String estadoRecibo ,
			String recibidoDe, String  numIdentificacion, String formaPago, int tipoFormaPago, double valor, 
			int idDetalleReciboPago) {
		
		this.numeroReciboCaja = numeroReciboCaja;
		this.codigoEstadoReciboCaja = codigoEstadoReciboCaja;
		this.estadoRecibo = estadoRecibo;
		this.recibidoDe = recibidoDe;
		this.numIdentificacion = numIdentificacion;
		this.formaPago = formaPago;
		this.tipoFormaPago = tipoFormaPago ;
		this.valor = valor;
		this.idDetalleReciboPago = idDetalleReciboPago;
	}
	
	
	public String getConsecutivo() {
		if (tipo == RECIBO_CAJA) {
			return numeroReciboCaja;
		} else if (tipo == DEVOLUCION_RECIBO_CAJA) {
			return getCodigoDevolucion() + "";
		}
		return null;
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
	 * @return the numeroReciboCaja
	 */
	public String getNumeroReciboCaja() {
		return numeroReciboCaja;
	}


	/**
	 * @param numeroReciboCaja the numeroReciboCaja to set
	 */
	public void setNumeroReciboCaja(String numeroReciboCaja) {
		this.numeroReciboCaja = numeroReciboCaja;
	}


	/**
	 * @return the codigoDevolucion
	 */
	public int getCodigoDevolucion() {
		return codigoDevolucion;
	}


	/**
	 * @param codigoDevolucion the codigoDevolucion to set
	 */
	public void setCodigoDevolucion(int codigoDevolucion) {
		this.codigoDevolucion = codigoDevolucion;
	}


	/**
	 * @return the acronimoEstadoDevolucion
	 */
	public String getAcronimoEstadoDevolucion() {
		return acronimoEstadoDevolucion;
	}


	/**
	 * @param acronimoEstadoDevolucion the acronimoEstadoDevolucion to set
	 */
	public void setAcronimoEstadoDevolucion(String acronimoEstadoDevolucion) {
		this.acronimoEstadoDevolucion = acronimoEstadoDevolucion;
	}


	/**
	 * @return the codigoEstadoReciboCaja
	 */
	public int getCodigoEstadoReciboCaja() {
		return codigoEstadoReciboCaja;
	}


	/**
	 * @param codigoEstadoReciboCaja the codigoEstadoReciboCaja to set
	 */
	public void setCodigoEstadoReciboCaja(int codigoEstadoReciboCaja) {
		this.codigoEstadoReciboCaja = codigoEstadoReciboCaja;
	}


	/**
	 * @return the estadoRecibo
	 */
	public String getEstadoRecibo() {
		return estadoRecibo;
	}


	/**
	 * @param estadoRecibo the estadoRecibo to set
	 */
	public void setEstadoRecibo(String estadoRecibo) {
		this.estadoRecibo = estadoRecibo;
	}


	/**
	 * @return the recibidoDe
	 */
	public String getRecibidoDe() {
		return recibidoDe;
	}


	/**
	 * @param recibidoDe the recibidoDe to set
	 */
	public void setRecibidoDe(String recibidoDe) {
		this.recibidoDe = recibidoDe;
	}


	/**
	 * @return the numIdentificacion
	 */
	public String getNumIdentificacion() {
		return numIdentificacion;
	}


	/**
	 * @param numIdentificacion the numIdentificacion to set
	 */
	public void setNumIdentificacion(String numIdentificacion) {
		this.numIdentificacion = numIdentificacion;
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
	 * @return the valor
	 */
	public double getValor() {
		return valor;
	}


	/**
	 * @param valor the valor to set
	 */
	public void setValor(double valor) {
		this.valor = valor;
	}


	/**
	 * @return the idDetalleReciboPago
	 */
	public int getIdDetalleReciboPago() {
		return idDetalleReciboPago;
	}


	/**
	 * @param idDetalleReciboPago the idDetalleReciboPago to set
	 */
	public void setIdDetalleReciboPago(int idDetalleReciboPago) {
		this.idDetalleReciboPago = idDetalleReciboPago;
	}


	/**
	 * @return the entidad_financiera
	 */
	public String getEntidad_financiera() {
		return entidad_financiera;
	}


	/**
	 * @param entidadFinanciera the entidad_financiera to set
	 */
	public void setEntidad_financiera(String entidadFinanciera) {
		entidad_financiera = entidadFinanciera;
	}


	/**
	 * @return the recibosCajaId
	 */
	public RecibosCajaId getRecibosCajaId() {
		return recibosCajaId;
	}


	/**
	 * @param recibosCajaId the recibosCajaId to set
	 */
	public void setRecibosCajaId(RecibosCajaId recibosCajaId) {
		this.recibosCajaId = recibosCajaId;
	}


	/**
	 * @param consecutivoDevolucion the consecutivoDevolucion to set
	 */
	public void setConsecutivoDevolucion(long consecutivoDevolucion) {
		this.consecutivoDevolucion = consecutivoDevolucion;
	}


	/**
	 * @return the consecutivoDevolucion
	 */
	public long getConsecutivoDevolucion() {
		return consecutivoDevolucion;
	}

	
	/**
	 * @return the horaDevolucion
	 */
	public String getHoraDevolucion() {
		return horaDevolucion;
	}

	/**
	 * @param horaDevolucion the horaDevolucion to set
	 */
	public void setHoraDevolucion(String horaDevolucion) {
		this.horaDevolucion = horaDevolucion;
	}

	/**
	 * @return the fecha
	 */
	public Date getFecha() {
		return fecha;
	}

	/**
	 * @param fecha the fecha to set
	 */
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public String getConsecutivoRecibosCaja() {
		return consecutivoRecibosCaja;
	}

	public void setConsecutivoRecibosCaja(String consecutivoRecibosCaja) {
		this.consecutivoRecibosCaja = consecutivoRecibosCaja;
	}
}
