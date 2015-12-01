package com.princetonsa.dto.facturacion;

import java.io.Serializable;
import java.math.BigDecimal;

import util.ConstantesBD;

/**
 * 
 * @author axioma
 *
 */
public class DtoMontoCobroFactura implements Serializable, Cloneable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6546685111448793399L;

	/**
	 * codigo
	 */
	private int codigo;
	
	/**
	 * fecha de vigencia inicial
	 */
	private String fechaVigenciaInicial;

	/**
	 * %
	 */
	private double porcentajeMontoCobro;
	
	/**
	 * tipo monto
	 */
	private int tipoMonto;
	
	/**
	 * valor
	 */
	private BigDecimal valorMontoCobro;
	
	/**
	 * 
	 */
	private String tipoAfiliado;
	
	/**
	 * 
	 */
	public DtoMontoCobroFactura() 
	{
		super();
		this.codigo = ConstantesBD.codigoNuncaValido;
		this.fechaVigenciaInicial = "";
		this.porcentajeMontoCobro= 0;
		this.tipoMonto= ConstantesBD.codigoNuncaValido;
		this.valorMontoCobro= BigDecimal.ZERO;
		this.tipoAfiliado="";
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
	 * @return the fechaVigenciaInicial
	 */
	public String getFechaVigenciaInicial() {
		return fechaVigenciaInicial;
	}

	/**
	 * @param fechaVigenciaInicial the fechaVigenciaInicial to set
	 */
	public void setFechaVigenciaInicial(String fechaVigenciaInicial) {
		this.fechaVigenciaInicial = fechaVigenciaInicial;
	}

	/**
	 * @return the porcentajeMontoCobro
	 */
	public double getPorcentajeMontoCobro() {
		return porcentajeMontoCobro;
	}

	/**
	 * @param porcentajeMontoCobro the porcentajeMontoCobro to set
	 */
	public void setPorcentajeMontoCobro(double porcentajeMontoCobro) {
		this.porcentajeMontoCobro = porcentajeMontoCobro;
	}

	/**
	 * @return the tipoMonto
	 */
	public int getTipoMonto() {
		return tipoMonto;
	}

	/**
	 * @param tipoMonto the tipoMonto to set
	 */
	public void setTipoMonto(int tipoMonto) {
		this.tipoMonto = tipoMonto;
	}

	/**
	 * @return the valorMontoCobro
	 */
	public BigDecimal getValorMontoCobro() {
		return valorMontoCobro;
	}

	/**
	 * @param valorMontoCobro the valorMontoCobro to set
	 */
	public void setValorMontoCobro(BigDecimal valorMontoCobro) {
		this.valorMontoCobro = valorMontoCobro;
	}

	/**
	 * @return the tipoAfiliado
	 */
	public String getTipoAfiliado() {
		return tipoAfiliado;
	}

	/**
	 * @param tipoAfiliado the tipoAfiliado to set
	 */
	public void setTipoAfiliado(String tipoAfiliado) {
		this.tipoAfiliado = tipoAfiliado;
	}
	
	
}
