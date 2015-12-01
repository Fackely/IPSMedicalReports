package com.princetonsa.dto.tesoreria;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Objeto utilizado para el manejo de la informaci&oacute;n relacionada a la
 * p&aacute;gina paginaBonos.jsp
 * @author Juan David Ram&iacute;rez
 * @since 30 Jun 2010
 */
@SuppressWarnings("serial")
public class DtoDetallePagosBonos implements Serializable
{
	/**
	 * Cantidad de bonos ingresados
	 */
	private int cantidadBonos;
	
	/**
	 * Control de los seriales de los bonos ingresados en los recibos de caja
	 */
	private ArrayList<DtoBonoSerialValor> serialesBonos;
	
	/**
	 * Observaciones
	 */
	private String observaciones;

	/**
	 * Indica si el elemento si es un bono y se debe validar
	 */
	private boolean utilizado;
	
	/**
	 * Atributo que contiene el consecutivo de la forma de pago asociada al tipo detalle forma 
	 * pago Bono.
	 */
	private int consecutivoFormaPago;
	
	/**
	 * @return Retorna atributo cantidadBonos
	 */
	public int getCantidadBonos()
	{
		return cantidadBonos;
	}

	/**
	 * @param cantidadBonos Asigna atributo cantidadBonos
	 */
	public void setCantidadBonos(int cantidadBonos)
	{
		this.cantidadBonos = cantidadBonos;
	}


	/**
	 * @return Retorna atributo observaciones
	 */
	public String getObservaciones()
	{
		return observaciones;
	}

	/**
	 * @param observaciones Asigna atributo observaciones
	 */
	public void setObservaciones(String observaciones)
	{
		this.observaciones = observaciones;
	}

	/**
	 * @return Retorna atributo serialesBonos
	 */
	public ArrayList<DtoBonoSerialValor> getSerialesBonos()
	{
		return serialesBonos;
	}

	/**
	 * @param serialesBonos Asigna atributo serialesBonos
	 */
	public void setSerialesBonos(ArrayList<DtoBonoSerialValor> serialesBonos)
	{
		this.serialesBonos = serialesBonos;
	}

	/**
	 * @return Retorna atributo utilizado
	 */
	public boolean isUtilizado()
	{
		return utilizado;
	}

	/**
	 * @param utilizado Asigna atributo utilizado
	 */
	public void setUtilizado(boolean utilizado)
	{
		this.utilizado = utilizado;
	}

	/**
	 * @param consecutivoFormaPago the consecutivoFormaPago to set
	 */
	public void setConsecutivoFormaPago(int consecutivoFormaPago) {
		this.consecutivoFormaPago = consecutivoFormaPago;
	}

	/**
	 * @return the consecutivoFormaPago
	 */
	public int getConsecutivoFormaPago() {
		return consecutivoFormaPago;
	}
}
