package com.princetonsa.dto.tesoreria;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;

import util.ConstantesBD;

/**
 * Esta clase almacena la informaci�n necesaria para mostrar los traslados
 * a caja de recaudo y caja mayor, realizados en el cierre turno de caja DCU 1123
 * 
 * @author Fabi�n Becerra
 * 
 */
public class DtoConsultaTrasladoRecaudoMayorEnCierre implements Serializable{

	/** * */
	private static final long serialVersionUID = 1L;

	/**
	 * Atributo que almacena el consecutivo del movimiento
	 */
	private Long nroConsecutivoMovimiento;
	
	/**
	 * Atributo que almacena el c�digo del tipo de movimiento de caja
	 */
	private Integer codigoTipoMovimiento;
	
	/**
	 * Atributo que almacena el c�digo de la caja de
	 * recaudo solicitada
	 */
	private Integer codigoCajaRecaudo;
	
	/**
	 * Atributo que almacena el nombre de la caja de recaudo
	 */
	private String descripcionCajaRecaudo;
	
	/**
	 * Atributo que almacena el nombre del centro de atenci�n
	 * de la caja de recaudo solicitada
	 */
	private String descripcionCentroAtenCajaRecaudo;
	
	/**
	 * Atributo que almacena el c�digo de la caja de
	 * mayor/principal
	 */
	private Integer codigoCajaMayor;
	
	/**
	 * Atributo que almacena el nombre de la caja mayor/principal
	 */
	private String descripcionCajaMayor;
	
	/**
	 * Atributo que almacena el nombre del centro de atenci�n
	 * de la caja mayor/principal
	 */
	private String descripcionCentroAtenCajaMayor;

	/**
	 * Atributo que almacena el primer nombre del testigo
	 */
	private String primerNombreTestigo;
	
	/**
	 * Atributo que almacena el segundo nombre del testigo
	 */
	private String segundoNombreTestigo;
	
	/**
	 * Atributo que almacena el primer apellido del testigo
	 */
	private String primerApellidoTestigo;
	
	/**
	 * Atributo que almacena el segundo apellido del testigo
	 */
	private String segundoApellidoTestigo;
	
	/**
	 * Atributo que almacena las observaciones del traslado a caja de recaudo
	 */
	private String observacionesTraslado;
	
	/**
	 * Atributo que almacena el consecutivo de la forma de pago
	 */
	private Integer consecutivoFormaPago;
	
	/**
	 * Atributo que almacena la descripcion de la forma de pago
	 */
	private String descripcionFormaPago;
	
	/**
	 * Atributo que almacena el valor trasladado en el recaudo o a la caja mayor 
	 */
	private BigDecimal valorTrasladado;
	
	/**
	 * Almacena las listas de totales de forma de pago y valor 
	 * trasladado para el traslado a recaudo y traslado a caja mayor
	 */
	private ArrayList<DtoTotalesParciales> totalesTraslado;
	
	
	/**
	 * M�todo que inicializa todos los valores de la clase
	 */
	public void reset()
	{
		this.nroConsecutivoMovimiento 			= ConstantesBD.codigoNuncaValidoLong;
		this.codigoCajaRecaudo					= ConstantesBD.codigoNuncaValido;
		this.codigoCajaMayor					= ConstantesBD.codigoNuncaValido;
		this.codigoTipoMovimiento				= ConstantesBD.codigoNuncaValido;
		this.consecutivoFormaPago				= ConstantesBD.codigoNuncaValido;
		this.descripcionCajaRecaudo				= "";
		this.descripcionCajaMayor				= "";
		this.descripcionCentroAtenCajaRecaudo 	= "";
		this.descripcionCentroAtenCajaMayor 	= "";
		this.primerNombreTestigo				= "";
		this.segundoNombreTestigo				= "";
		this.primerApellidoTestigo				= "";
		this.segundoApellidoTestigo				= "";
		this.observacionesTraslado				= "";
		this.descripcionFormaPago				= "";
		this.valorTrasladado					= new BigDecimal(0);
		this.totalesTraslado					= new ArrayList<DtoTotalesParciales>();
	}


	/**
	 * M�todo que retorna el valor del atributo descripcionFormaPago
	 * @return descripcionFormaPago
	 */
	public String getDescripcionFormaPago() {
		return descripcionFormaPago;
	}


	/**
	 * M�todo que retorna el valor del atributo codigoCajaMayor
	 * @return codigoCajaMayor
	 */
	public Integer getCodigoCajaMayor() {
		return codigoCajaMayor;
	}


	/**
	 * M�todo que almacena el valor del atributo the codigoCajaMayor
	 * @param codigoCajaMayor
	 */
	public void setCodigoCajaMayor(Integer codigoCajaMayor) {
		this.codigoCajaMayor = codigoCajaMayor;
	}


	/**
	 * M�todo que retorna el valor del atributo descripcionCajaMayor
	 * @return descripcionCajaMayor
	 */
	public String getDescripcionCajaMayor() {
		return descripcionCajaMayor;
	}


	/**
	 * M�todo que almacena el valor del atributo the descripcionCajaMayor
	 * @param descripcionCajaMayor
	 */
	public void setDescripcionCajaMayor(String descripcionCajaMayor) {
		this.descripcionCajaMayor = descripcionCajaMayor;
	}


	/**
	 * M�todo que retorna el valor del atributo descripcionCentroAtenCajaMayor
	 * @return descripcionCentroAtenCajaMayor
	 */
	public String getDescripcionCentroAtenCajaMayor() {
		return descripcionCentroAtenCajaMayor;
	}


	/**
	 * M�todo que almacena el valor del atributo the descripcionCentroAtenCajaMayor
	 * @param descripcionCentroAtenCajaMayor
	 */
	public void setDescripcionCentroAtenCajaMayor(
			String descripcionCentroAtenCajaMayor) {
		this.descripcionCentroAtenCajaMayor = descripcionCentroAtenCajaMayor;
	}


	/**
	 * M�todo que almacena el valor del atributo the descripcionFormaPago
	 * @param descripcionFormaPago
	 */
	public void setDescripcionFormaPago(String descripcionFormaPago) {
		this.descripcionFormaPago = descripcionFormaPago;
	}


	/**
	 * M�todo que retorna el valor del atributo valorTrasladado
	 * @return valorTrasladado
	 */
	public BigDecimal getValorTrasladado() {
		return valorTrasladado;
	}


	/**
	 * M�todo que almacena el valor del atributo the valorTrasladado
	 * @param valorTrasladado
	 */
	public void setValorTrasladado(BigDecimal valorTrasladado) {
		this.valorTrasladado = valorTrasladado;
	}


	/**
	 * M�todo que retorna el valor del atributo nroConsecutivoMovimiento
	 * @return nroConsecutivoMovimiento
	 */
	public Long getNroConsecutivoMovimiento() {
		return nroConsecutivoMovimiento;
	}


	/**
	 * M�todo que almacena el valor del atributo the nroConsecutivoMovimiento
	 * @param nroConsecutivoMovimiento
	 */
	public void setNroConsecutivoMovimiento(Long nroConsecutivoMovimiento) {
		this.nroConsecutivoMovimiento = nroConsecutivoMovimiento;
	}


	/**
	 * M�todo que retorna el valor del atributo codigoCajaRecaudo
	 * @return codigoCajaRecaudo
	 */
	public Integer getCodigoCajaRecaudo() {
		return codigoCajaRecaudo;
	}


	/**
	 * M�todo que almacena el valor del atributo the codigoCajaRecaudo
	 * @param codigoCajaRecaudo
	 */
	public void setCodigoCajaRecaudo(Integer codigoCajaRecaudo) {
		this.codigoCajaRecaudo = codigoCajaRecaudo;
	}


	/**
	 * M�todo que retorna el valor del atributo descripcionCajaRecaudo
	 * @return descripcionCajaRecaudo
	 */
	public String getDescripcionCajaRecaudo() {
		return descripcionCajaRecaudo;
	}


	/**
	 * M�todo que almacena el valor del atributo the descripcionCajaRecaudo
	 * @param descripcionCajaRecaudo
	 */
	public void setDescripcionCajaRecaudo(String descripcionCajaRecaudo) {
		this.descripcionCajaRecaudo = descripcionCajaRecaudo;
	}


	/**
	 * M�todo que retorna el valor del atributo descripcionCentroAtenCajaRecaudo
	 * @return descripcionCentroAtenCajaRecaudo
	 */
	public String getDescripcionCentroAtenCajaRecaudo() {
		return descripcionCentroAtenCajaRecaudo;
	}


	/**
	 * M�todo que almacena el valor del atributo the descripcionCentroAtenCajaRecaudo
	 * @param descripcionCentroAtenCajaRecaudo
	 */
	public void setDescripcionCentroAtenCajaRecaudo(
			String descripcionCentroAtenCajaRecaudo) {
		this.descripcionCentroAtenCajaRecaudo = descripcionCentroAtenCajaRecaudo;
	}


	/**
	 * M�todo que retorna el valor del atributo primerNombreTestigo
	 * @return primerNombreTestigo
	 */
	public String getPrimerNombreTestigo() {
		return primerNombreTestigo;
	}


	/**
	 * M�todo que almacena el valor del atributo the primerNombreTestigo
	 * @param primerNombreTestigo
	 */
	public void setPrimerNombreTestigo(String primerNombreTestigo) {
		this.primerNombreTestigo = primerNombreTestigo;
	}


	/**
	 * M�todo que retorna el valor del atributo segundoNombreTestigo
	 * @return segundoNombreTestigo
	 */
	public String getSegundoNombreTestigo() {
		return segundoNombreTestigo;
	}


	/**
	 * M�todo que almacena el valor del atributo the segundoNombreTestigo
	 * @param segundoNombreTestigo
	 */
	public void setSegundoNombreTestigo(String segundoNombreTestigo) {
		this.segundoNombreTestigo = segundoNombreTestigo;
	}


	/**
	 * M�todo que retorna el valor del atributo primerApellidoTestigo
	 * @return primerApellidoTestigo
	 */
	public String getPrimerApellidoTestigo() {
		return primerApellidoTestigo;
	}


	/**
	 * M�todo que almacena el valor del atributo the primerApellidoTestigo
	 * @param primerApellidoTestigo
	 */
	public void setPrimerApellidoTestigo(String primerApellidoTestigo) {
		this.primerApellidoTestigo = primerApellidoTestigo;
	}


	/**
	 * M�todo que retorna el valor del atributo segundoApellidoTestigo
	 * @return segundoApellidoTestigo
	 */
	public String getSegundoApellidoTestigo() {
		return segundoApellidoTestigo;
	}


	/**
	 * M�todo que almacena el valor del atributo the segundoApellidoTestigo
	 * @param segundoApellidoTestigo
	 */
	public void setSegundoApellidoTestigo(String segundoApellidoTestigo) {
		this.segundoApellidoTestigo = segundoApellidoTestigo;
	}


	/**
	 * M�todo que retorna el valor del atributo codigoTipoMovimiento
	 * @return codigoTipoMovimiento
	 */
	public Integer getCodigoTipoMovimiento() {
		return codigoTipoMovimiento;
	}


	/**
	 * M�todo que almacena el valor del atributo the codigoTipoMovimiento
	 * @param codigoTipoMovimiento
	 */
	public void setCodigoTipoMovimiento(Integer codigoTipoMovimiento) {
		this.codigoTipoMovimiento = codigoTipoMovimiento;
	}


	/**
	 * M�todo que retorna el valor del atributo totalesTraslado
	 * @return totalesTraslado
	 */
	public ArrayList<DtoTotalesParciales> getTotalesTraslado() {
		return totalesTraslado;
	}


	/**
	 * M�todo que almacena el valor del atributo the totalesTraslado
	 * @param totalesTraslado
	 */
	public void setTotalesTraslado(ArrayList<DtoTotalesParciales> totalesTraslado) {
		this.totalesTraslado = totalesTraslado;
	}


	/**
	 * M�todo que retorna el valor del atributo observacionesTraslado
	 * @return observacionesTraslado
	 */
	public String getObservacionesTraslado() {
		return observacionesTraslado;
	}


	/**
	 * M�todo que almacena el valor del atributo the observacionesTraslado
	 * @param observacionesTraslado
	 */
	public void setObservacionesTraslado(String observacionesTraslado) {
		this.observacionesTraslado = observacionesTraslado;
	}


	/**
	 * M�todo que retorna el valor del atributo consecutivoFormaPago
	 * @return consecutivoFormaPago
	 */
	public Integer getConsecutivoFormaPago() {
		return consecutivoFormaPago;
	}


	/**
	 * M�todo que almacena el valor del atributo the consecutivoFormaPago
	 * @param consecutivoFormaPago
	 */
	public void setConsecutivoFormaPago(Integer consecutivoFormaPago) {
		this.consecutivoFormaPago = consecutivoFormaPago;
	}
	

	
	
	
	
}
