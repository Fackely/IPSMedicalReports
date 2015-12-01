/**
 * 
 */
package com.princetonsa.dto.inventario;

import java.util.Date;

import util.ConstantesBD;

import com.princetonsa.dto.facturacion.DtoContrato;


public class DTOEsqTarInventarioContrato {
	
	private Long codigo;
	private DtoContrato contrato;
	private int codigoEsquema;
	private Integer codigoClaseInventario;
	private Date fechaVigencia;
	
	/** 
	 * Método constructor de la clase usado para consultar el esquema
	 * tarifario por el id del contrato dado 
	 * @param codigo
	 * @param codigoEsquema
	 * @param codigoClaseInventario
	 * @param fechaVigencia
	 * @author javrammo
	 */
	public DTOEsqTarInventarioContrato(Long codigo, Integer codigoContrato, Integer codigoEsquema, Date fechaVigencia) {

		this.fechaVigencia = fechaVigencia;
		if(codigoContrato !=null){
			this.codigo = codigo;			
			this.contrato = new DtoContrato();
			this.contrato.setCodigo(codigoContrato);
			this.codigoEsquema = codigoEsquema;
		}else{
			this.codigo = ConstantesBD.codigoNuncaValidoLong;			
			this.contrato = new DtoContrato();
			this.contrato.setCodigo(ConstantesBD.codigoNuncaValido);
			this.codigoEsquema= ConstantesBD.codigoNuncaValido;
		}
	}

	/**
	 * 
	 * Método constructor de la clase
	 */
	public DTOEsqTarInventarioContrato(){
		this.codigo = ConstantesBD.codigoNuncaValidoLong;			
		this.contrato = new DtoContrato();
		this.contrato.setCodigo(ConstantesBD.codigoNuncaValido);
		this.codigoEsquema= ConstantesBD.codigoNuncaValido;
	}
	
	/**
	 * 
	 * Método constructor de la clase usado para consultar el esquema
	 * tarifario por el id del contrato dado
	 */
	public DTOEsqTarInventarioContrato(Long codigo, Integer codigoContrato, Integer codigoEsquema){
		if(codigoContrato !=null){
			this.codigo = codigo;			
			this.contrato = new DtoContrato();
			this.contrato.setCodigo(codigoContrato);
			this.codigoEsquema = codigoEsquema;
		}else{
			this.codigo = ConstantesBD.codigoNuncaValidoLong;			
			this.contrato = new DtoContrato();
			this.contrato.setCodigo(ConstantesBD.codigoNuncaValido);
			this.codigoEsquema= ConstantesBD.codigoNuncaValido;
		}
				
	}
	
	
	
	/**
	 * 
	 * Método constructor de la clase usado para consultar el esquema
	 * tarifario por el id del contrato dado
	 */
	public DTOEsqTarInventarioContrato(Long codigo, Integer codigoContrato, Integer codigoEsquema, Integer codigoClaseInventario){
		if(codigoContrato !=null){
			this.codigo = codigo;			
			this.contrato = new DtoContrato();
			this.contrato.setCodigo(codigoContrato);
			this.codigoEsquema = codigoEsquema;
			this.codigoClaseInventario=codigoClaseInventario;
		}else{
			this.codigo = ConstantesBD.codigoNuncaValidoLong;			
			this.contrato = new DtoContrato();
			this.contrato.setCodigo(ConstantesBD.codigoNuncaValido);
			this.codigoEsquema= ConstantesBD.codigoNuncaValido;
			this.codigoClaseInventario=ConstantesBD.codigoNuncaValido;
		}
				
	}
	

	/**
	 *Este método retorna el valor de la variable codigo
	 */
	public Long getCodigo() {
		return codigo;
	}

	/**
	 *Este método asigna el valor de la variable codigo
	 */
	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	/**
	 *Este método retorna el valor de la variable contrato
	 */
	public DtoContrato getContrato() {
		return contrato;
	}

	/**
	 *Este método asigna el valor de la variable contrato
	 */
	public void setContrato(DtoContrato contrato) {
		this.contrato = contrato;
	}

	/**
	 *Este método asigna el valor de la variable codigoEsquema
	 */
	public void setCodigoEsquema(int codigoEsquema) {
		this.codigoEsquema = codigoEsquema;
	}

	/**
	 *Este método retorna el valor de la variable codigoEsquema
	 */
	public int getCodigoEsquema() {
		return codigoEsquema;
	}

	/**
	 * @return the codigoClaseInventario
	 */
	public Integer getCodigoClaseInventario() {
		return codigoClaseInventario;
	}

	/**
	 * @param codigoClaseInventario the codigoClaseInventario to set
	 */
	public void setCodigoClaseInventario(Integer codigoClaseInventario) {
		this.codigoClaseInventario = codigoClaseInventario;
	}
	
	
	
	
	
}
