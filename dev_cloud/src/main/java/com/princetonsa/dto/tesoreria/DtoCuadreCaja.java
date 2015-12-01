package com.princetonsa.dto.tesoreria;

import java.io.Serializable;

import util.ConstantesBD;


/**
 * Esta clase maneja la informaci&oacute;n relacionada al cuadre de caja
 * 
 * @author Jorge Armando Agudelo Quintero
 * @anexo 226
 */
public class DtoCuadreCaja implements Serializable {


	private static final long serialVersionUID = 1L;
	
	/**
	 * C&oacute;digo del cuadre de caja registrado
	 */
	private long codigo;
	
	/**
	 * Valor registrado en caja
	 */
	private Double valorCaja;
	
	/**
	 * Valor registrado en el sistema
	 */
	private Double valorSistema;
	
	/**
	 * Valor diferencia (faltante o sobrante)
	 */
	private Double valorDiferencia;
	
	/**
	 * Nombre de la forma de pago asociada al recibo
	 */
	private String formaPago;
	
	/**
	 * Indicativo de la forma de pago asociada al recibo
	 */
	private int tipoFormaPago;
	
	/**
	 * Indicativo del tipo de detalle de la forma de pago
	 */
	private int tipoDetalleFormaPago;

	/**
	 * Valor de la base en caja registrada, utilizado cuando la forma de pago es Efectivo
	 */
	private Double valorBaseEnCaja;

	/**
	 * Se utiliza para almacenar un mensaje relacionado al cuadre espec&iacute;fico de una forma de pago concreta
	 */
	private String alerta;

	/**
	 * Determina si el valor de esta diferencia es de tipo sobrante o faltante
	 * seg&uacute;n ConstantesIntegridadDominio
	 */
	private String tipodiferencia;
	
	/**
	 * Atributo que indica si la forma de pago requiere 
	 * traslado a caja de recaudo.
	 */
	private Character reqTrasladoCajaRecaudo;

	/**
	 * Constructor vac&iacute;o, requerido para hacer la proyecci&oacute;n del
	 * ORM
	 */
	public DtoCuadreCaja() {
		this.setAlerta("");
		this.valorCaja=new Double(0);
		this.valorSistema=new Double(0);
		this.valorDiferencia=new Double(0);
		this.valorBaseEnCaja=new Double(0);
		this.reqTrasladoCajaRecaudo = ConstantesBD.acronimoNoChar;
	}

	/**
	 * @return the codigo
	 */
	public long getCodigo() {
		return codigo;
	}

	/**
	 * @param codigo the codigo to set
	 */
	public void setCodigo(long codigo) {
		this.codigo = codigo;
	}

	/**
	 * @return the valorCaja
	 */
	public Double getValorCaja() {
		return valorCaja;
	}
	
	/**
	 * @param valorCaja the valorCaja to set
	 */
	public void setValorCaja(Double valorCaja) {
		
//		Log4JManager.info("intentando setear el valor de caja ");
//		
//		if(valorCaja!=null){
//			
//			Log4JManager.info("diferente de null " + valorCaja.getClass());
//			
//			if(valorCaja instanceof BigDecimal){
//				
//				BigDecimal valor = (BigDecimal) valorCaja;
//				
//				this.valorCaja = (Double) valor.DoubleValue();
//				
//			}else if (valorCaja instanceof Double){
//				
//				this.valorCaja = (Double) valorCaja;
//			}
//		}else{
//			
//			Log4JManager.info("es nulo " + valorCaja);
//		
//		}
		
		this.valorCaja =  valorCaja;
	}

	/**
	 * @return the valorSistema
	 */
	public Double getValorSistema() {
		return valorSistema;
	}

	/**
	 * @param valorSistema the valorSistema to set
	 */
	public void setValorSistema(Double valorSistema) {
		
//		if(valorSistema!=null){
//			
//			if(valorSistema instanceof BigDecimal){
//				
//				BigDecimal valor = (BigDecimal) valorSistema;
//				
//				this.valorSistema = (Double) valor.doubleValue();
//				
//			}else if (valorSistema instanceof Double){
//				
//				this.valorSistema = (Double) valorSistema;
//			}
//		}
		
		this.valorSistema =  valorSistema;
	}

	/**
	 * @return the valorDiferencia
	 */
	public Double getValorDiferencia() {
		return valorDiferencia;
	}

	/**
	 * @param valorDiferencia the valorDiferencia to set
	 */
	public void setValorDiferencia(Double valorDiferencia) {
		
//		if(valorDiferencia!=null){
//			
//			if(valorDiferencia instanceof BigDecimal){
//				
//				BigDecimal valor = (BigDecimal) valorDiferencia;
//				
//				this.valorDiferencia = (Double) valor.doubleValue();
//				
//			}else if (valorDiferencia instanceof Double){
//				
//				this.valorDiferencia = (Double) valorDiferencia;
//			}
//		}
		
		this.valorDiferencia = valorDiferencia;
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
	 * @return the tipoDetalleFormaPago
	 */
	public int getTipoDetalleFormaPago() {
		return tipoDetalleFormaPago;
	}

	/**
	 * @param tipoDetalleFormaPago the tipoDetalleFormaPago to set
	 */
	public void setTipoDetalleFormaPago(int tipoDetalleFormaPago) {
		this.tipoDetalleFormaPago = tipoDetalleFormaPago;
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
	 * @return the valorBaseEnCaja
	 */
	public Double getValorBaseEnCaja() {
		return valorBaseEnCaja;
	}

	/**
	 * @param valorBaseEnCaja the valorBaseEnCaja to set
	 */
	public void setValorBaseEnCaja(Double valorBaseEnCaja) {
		this.valorBaseEnCaja = valorBaseEnCaja;
	}

	/**
	 * @return the alerta
	 */
	public String getAlerta() {
		return alerta;
	}

	/**
	 * @param alerta the alerta to set
	 */
	public void setAlerta(String alerta) {
		this.alerta = alerta;
	}

	/**
	 * @return the tipodiferencia
	 */
	public String getTipodiferencia() {
		return tipodiferencia;
	}

	/**
	 * @param tipodiferencia the tipodiferencia to set
	 */
	public void setTipodiferencia(String tipodiferencia) {
		this.tipodiferencia = tipodiferencia;
	}

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo reqTrasladoCajaRecaudo
	 * @return retorna la variable reqTrasladoCajaRecaudo 
	 * @author Yennifer Guerrero 
	 */
	public Character getReqTrasladoCajaRecaudo() {
		return reqTrasladoCajaRecaudo;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo reqTrasladoCajaRecaudo
	 * @param valor para el atributo reqTrasladoCajaRecaudo 
	 * @author Yennifer Guerrero
	 */
	public void setReqTrasladoCajaRecaudo(Character reqTrasladoCajaRecaudo) {
		this.reqTrasladoCajaRecaudo = reqTrasladoCajaRecaudo;
	}

}
