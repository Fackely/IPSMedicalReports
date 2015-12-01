package util.facturacion;

import java.io.Serializable;

/**
 * 
 * @author axioma
 *
 */
public class InfoTarifaYExcepcion implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	private double valorTarifaTotalConExcepcion;
	
	/**
	 * 
	 */
	private double valorTarifaBase;
	
	/**
	 * 
	 * @param valorExcepcion
	 * @param valorTarifaBase
	 * @param valorTarifaTotal
	 */
	public InfoTarifaYExcepcion(double valorTarifaBase,
			double valorTarifaTotalConExcepcion) {
		super();
		this.valorTarifaBase = valorTarifaBase;
		this.valorTarifaTotalConExcepcion = valorTarifaTotalConExcepcion;
	}

	/**
	 * @return the valorTarifaTotalConExcepcion
	 */
	public double getValorTarifaTotalConExcepcion() {
			if (valorTarifaTotalConExcepcion<0)
				return 0;
			return valorTarifaTotalConExcepcion;
	}

	/**
	 * @param valorTarifaTotalConExcepcion the valorTarifaTotalConExcepcion to set
	 */
	public void setValorTarifaTotalConExcepcion(double valorTarifaTotalConExcepcion) {
		this.valorTarifaTotalConExcepcion = valorTarifaTotalConExcepcion;
	}

	/**
	 * @return the valorTarifaBase
	 */
	public double getValorTarifaBase() {
		return valorTarifaBase;
	}

	/**
	 * @param valorTarifaBase the valorTarifaBase to set
	 */
	public void setValorTarifaBase(double valorTarifaBase) {
		this.valorTarifaBase = valorTarifaBase;
	}

	/**
	 * 
	 * @return
	 */
	public boolean getExisteTarifaBase()
	{
		if(this.getValorTarifaBase()>=0)
			return true;
		return false;
	}
	
}
