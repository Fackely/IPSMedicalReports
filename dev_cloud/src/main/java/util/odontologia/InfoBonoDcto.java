package util.odontologia;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 
 * @author axioma
 *
 */
@SuppressWarnings("serial")
public class InfoBonoDcto implements Serializable
{
	/**
	 * 
	 */
	private BigDecimal serial;
	
	/**
	 * 
	 */
	private BigDecimal valorDctoCALCULADO;
	
	/**
	 * 
	 */
	private String advertencia;

	/**
	 * valor en la tabla sin calculos
	 */
	private BigDecimal valorDcto;
	
	/**
	 * 
	 */
	private double porcentajeDescuento;
	
	/**
	 * 
	 */
	private boolean seleccionadoPorcentaje;
	
	/**
	 * Bono del paciente, estricta relación al campo codigo_pk de la tabla bonos_conv_ing_pac
	 */
	private int bonoPaciente;

	
	/**
	 * 
	 * @param serial
	 * @param valorDcto
	 * @param advertencia
	 */
	public InfoBonoDcto()
	{
		super();
		this.serial = new BigDecimal(0);
		this.valorDctoCALCULADO = new BigDecimal(0);
		this.advertencia = "";
		this.valorDcto= new BigDecimal(0);
		this.porcentajeDescuento= 0;
		this.seleccionadoPorcentaje= false;
	}

	/**
	 * @return the serial
	 */
	public BigDecimal getSerial()
	{
		return serial;
	}

	/**
	 * @param serial the serial to set
	 */
	public void setSerial(BigDecimal serial)
	{
		this.serial = serial;
	}

	/**
	 * @return the valorDctoCALCULADO
	 */
	public BigDecimal getValorDctoCALCULADO()
	{
		return valorDctoCALCULADO;
	}

	/**
	 * @param valorDctoCALCULADO the valorDctoCALCULADO to set
	 */
	public void setValorDctoCALCULADO(BigDecimal valorDctoCALCULADO)
	{
		this.valorDctoCALCULADO = valorDctoCALCULADO;
	}

	/**
	 * @return the advertencia
	 */
	public String getAdvertencia()
	{
		return advertencia;
	}

	/**
	 * @param advertencia the advertencia to set
	 */
	public void setAdvertencia(String advertencia)
	{
		this.advertencia = advertencia;
	}

	/**
	 * @return the valorDcto
	 */
	public BigDecimal getValorDcto()
	{
		return valorDcto;
	}

	/**
	 * @return the porcentajeDescuento
	 */
	public double getPorcentajeDescuento()
	{
		return porcentajeDescuento;
	}

	/**
	 * @param valorDcto the valorDcto to set
	 */
	public void setValorDcto(BigDecimal valorDcto)
	{
		this.valorDcto = valorDcto;
	}

	/**
	 * @param porcentajeDescuento the porcentajeDescuento to set
	 */
	public void setPorcentajeDescuento(double porcentajeDescuento)
	{
		this.porcentajeDescuento = porcentajeDescuento;
	}

	/**
	 * @return the seleccionadoPorcentaje
	 */
	public boolean isSeleccionadoPorcentaje() {
		return seleccionadoPorcentaje;
	}

	/**
	 * @param seleccionadoPorcentaje the seleccionadoPorcentaje to set
	 */
	public void setSeleccionadoPorcentaje(boolean seleccionadoPorcentaje) {
		this.seleccionadoPorcentaje = seleccionadoPorcentaje;
	}

	/**
	 * Obtiene el valor del atributo bonoPaciente
	 *
	 * @return Retorna atributo bonoPaciente
	 */
	public int getBonoPaciente()
	{
		return bonoPaciente;
	}

	/**
	 * Establece el valor del atributo bonoPaciente
	 *
	 * @param valor para el atributo bonoPaciente
	 */
	public void setBonoPaciente(int bonoPaciente)
	{
		this.bonoPaciente = bonoPaciente;
	}
	
}
