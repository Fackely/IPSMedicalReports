package util;

import java.io.Serializable;
import java.math.BigDecimal;

public class InfoPorcentajeValor implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8231977370901566895L;

	/**
	 * 
	 */
	private BigDecimal valor;
	
	/**
	 * 
	 */
	private double porcentaje;

	/**
	 * 
	 */
	private String descripcion;
	
	/**
	 * 
	 */
	public InfoPorcentajeValor()
	{
		super();
		this.valor = new BigDecimal(ConstantesBD.codigoNuncaValido);
		this.porcentaje = ConstantesBD.codigoNuncaValido;
		this.descripcion= "";
	}

	/**
	 * @return the valor
	 */
	public BigDecimal getValor()
	{
		return valor;
	}

	/**
	 * @param valor the valor to set
	 */
	public void setValor(BigDecimal valor)
	{
		this.valor = valor;
	}

	/**
	 * @return the porcentaje
	 */
	public double getPorcentaje()
	{
		return porcentaje;
	}

	/**
	 * @param porcentaje the porcentaje to set
	 */
	public void setPorcentaje(double porcentaje)
	{
		this.porcentaje = porcentaje;
	}

	/**
	 * @return the descripcion
	 */
	public String getDescripcion()
	{
		return descripcion;
	}

	/**
	 * @param descripcion the descripcion to set
	 */
	public void setDescripcion(String descripcion)
	{
		this.descripcion = descripcion;
	}
	
	
}
