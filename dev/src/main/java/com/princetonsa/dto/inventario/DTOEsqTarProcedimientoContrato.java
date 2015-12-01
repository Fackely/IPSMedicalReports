/**
 * 
 */
package com.princetonsa.dto.inventario;

import java.util.Date;

import util.ConstantesBD;

import com.princetonsa.dto.facturacion.DtoContrato;


public class DTOEsqTarProcedimientoContrato {
	
	private Long codigo;
	private DtoContrato contrato;
	private int codigoEsquema;
	private Integer codigoGrupoServicio;
	private Date fechaVigencia;
	
	/**
	 * 
	 * Método constructor de la clase
	 */
	public DTOEsqTarProcedimientoContrato(){
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
	public DTOEsqTarProcedimientoContrato(Long codigo, Integer codigoContrato, Integer codigoEsquema){
		if(codigoContrato !=null){
			this.codigo = codigo;			
			this.contrato = new DtoContrato();
			this.contrato.setCodigo(codigoContrato);
			this.codigoEsquema = codigoEsquema;
		}else{
			this.codigo = ConstantesBD.codigoNuncaValidoLong;			
			this.contrato = new DtoContrato();
			this.contrato.setCodigo(ConstantesBD.codigoNuncaValido);
			this.codigoEsquema = ConstantesBD.codigoNuncaValido;
		}
				
	}
	
	public DTOEsqTarProcedimientoContrato(Long codigo, Integer codigoContrato,Integer codigoEsquema, Date fechaVigencia) {
		if(codigoContrato !=null){
			this.codigo = codigo;			
			this.contrato = new DtoContrato();
			this.contrato.setCodigo(codigoContrato);
			this.codigoEsquema = codigoEsquema;
			this.fechaVigencia=fechaVigencia;
		}else{
			this.codigo = ConstantesBD.codigoNuncaValidoLong;			
			this.contrato = new DtoContrato();
			this.contrato.setCodigo(ConstantesBD.codigoNuncaValido);
			this.codigoEsquema = ConstantesBD.codigoNuncaValido;
			this.fechaVigencia=fechaVigencia;
		}
	}	
	
	/**
	 * 
	 * Método constructor de la clase usado para consultar el esquema
	 * tarifario por el id del contrato dado
	 */
	public DTOEsqTarProcedimientoContrato(Long codigo, Integer codigoContrato, Integer codigoEsquema, Integer codigoGrupoServicio){
		if(codigoContrato !=null){
			this.codigo = codigo;			
			this.contrato = new DtoContrato();
			this.contrato.setCodigo(codigoContrato);
			this.codigoEsquema = codigoEsquema;
			this.codigoGrupoServicio=codigoGrupoServicio;
		}else{
			this.codigo = ConstantesBD.codigoNuncaValidoLong;			
			this.contrato = new DtoContrato();
			this.contrato.setCodigo(ConstantesBD.codigoNuncaValido);
			this.codigoEsquema = ConstantesBD.codigoNuncaValido;
			this.codigoGrupoServicio=ConstantesBD.codigoNuncaValido;
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
	 * @return the codigoGrupoServicio
	 */
	public Integer getCodigoGrupoServicio() {
		return codigoGrupoServicio;
	}

	/**
	 * @param codigoGrupoServicio the codigoGrupoServicio to set
	 */
	public void setCodigoGrupoServicio(Integer codigoGrupoServicio) {
		this.codigoGrupoServicio = codigoGrupoServicio;
	}		
	
	public Date getFechaVigencia() {
		return fechaVigencia;
	}

	public void setFechaVigencia(Date fechaVigencia) {
		this.fechaVigencia = fechaVigencia;
	}
	
	
	
	
	
}
