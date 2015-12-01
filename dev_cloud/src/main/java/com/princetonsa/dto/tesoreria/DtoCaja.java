package com.princetonsa.dto.tesoreria;

import java.io.Serializable;
import java.math.BigDecimal;

@SuppressWarnings("serial")
public class DtoCaja implements Serializable
{
	private Integer consecutivo;
	private Integer codigo;
	private String descripcion;
    private BigDecimal valorBase;
    
    public DtoCaja()
	{
    	consecutivo=null;
    	codigo=null;
    	descripcion="";
    	valorBase=null;

	}

	/**
	 * @return Retorna atributo consecutivo
	 */
	public Integer getConsecutivo()
	{
		return consecutivo;
	}

	/**
	 * @param consecutivo Asigna atributo consecutivo
	 */
	public void setConsecutivo(Integer consecutivo)
	{
		this.consecutivo = consecutivo;
	}

	/**
	 * @return Retorna atributo codigo
	 */
	public Integer getCodigo()
	{
		return codigo;
	}

	/**
	 * @param codigo Asigna atributo codigo
	 */
	public void setCodigo(Integer codigo)
	{
		this.codigo = codigo;
	}

	/**
	 * @return Retorna atributo descripcion
	 */
	public String getDescripcion()
	{
		return descripcion;
	}

	/**
	 * @param descripcion Asigna atributo descripcion
	 */
	public void setDescripcion(String descripcion)
	{
		this.descripcion = descripcion;
	}

	/**
	 * @return Retorna atributo valor_base
	 */
	public BigDecimal getValorBase()
	{
		return valorBase;
	}

	/**
	 * @param valorBase Asigna atributo valor_base
	 */
	public void setValorBase(BigDecimal valorBase)
	{
		valorBase = valorBase;
	}
}
