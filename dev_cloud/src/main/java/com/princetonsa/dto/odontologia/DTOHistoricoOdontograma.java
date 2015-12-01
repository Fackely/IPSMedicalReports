/**
 * 
 */
package com.princetonsa.dto.odontologia;

import java.io.Serializable;
import java.math.BigDecimal;

import util.ValoresPorDefecto;

/**
 * @author Juan David Ramírez
 * @since Jan 29, 2011
 */
@SuppressWarnings("serial")
public class DTOHistoricoOdontograma implements Serializable
{
	/**
	 * Código del plan de tratamiento
	 */
	private BigDecimal codigoPlanTratamiento;
	
	/**
	 * Estado del plan de tratamiento
	 */
	private String estadoPlanTratamiento;
	
	/**
	 * Código del presupuesto
	 */
	private BigDecimal codigoPresupuesto;
	
	/**
	 * Almacena el acronimo del estado del presupuesto.
	 */
	private String estadoPresupuesto;
	
	/**
	 * Fecha de generación
	 */
	private String fechaGeneracionPresupuesto;
	
	/**
	 * Almacena el estado del presupuesto.
	 */
	private String ayudanteEstadoPresupuesto;

	/**
	 * Obtiene el valor del atributo codigoPlanTratamiento
	 *
	 * @return Retorna atributo codigoPlanTratamiento
	 */
	public BigDecimal getCodigoPlanTratamiento()
	{
		return codigoPlanTratamiento;
	}

	/**
	 * Establece el valor del atributo codigoPlanTratamiento
	 *
	 * @param valor para el atributo codigoPlanTratamiento
	 */
	public void setCodigoPlanTratamiento(BigDecimal codigoPlanTratamiento)
	{
		this.codigoPlanTratamiento = codigoPlanTratamiento;
	}

	/**
	 * Obtiene el valor del atributo estadoPlanTratamiento
	 *
	 * @return Retorna atributo estadoPlanTratamiento
	 */
	public String getEstadoPlanTratamiento()
	{
		return estadoPlanTratamiento;
	}

	/**
	 * Establece el valor del atributo estadoPlanTratamiento
	 *
	 * @param valor para el atributo estadoPlanTratamiento
	 */
	public void setEstadoPlanTratamiento(String estadoPlanTratamiento)
	{
		this.estadoPlanTratamiento = estadoPlanTratamiento;
	}

	/**
	 * Obtiene el valor del atributo codigoPresupuesto
	 *
	 * @return Retorna atributo codigoPresupuesto
	 */
	public BigDecimal getCodigoPresupuesto()
	{
		return codigoPresupuesto;
	}

	/**
	 * Establece el valor del atributo codigoPresupuesto
	 *
	 * @param valor para el atributo codigoPresupuesto
	 */
	public void setCodigoPresupuesto(BigDecimal codigoPresupuesto)
	{
		this.codigoPresupuesto = codigoPresupuesto;
	}

	/**
	 * Obtiene el valor del atributo estadoPresupuesto
	 *
	 * @return Retorna atributo estadoPresupuesto
	 */
	public String getEstadoPresupuesto()
	{
		return estadoPresupuesto;
	}

	/**
	 * Establece el valor del atributo estadoPresupuesto
	 *
	 * @param valor para el atributo estadoPresupuesto
	 */
	public void setEstadoPresupuesto(String estadoPresupuesto)
	{
		this.estadoPresupuesto = estadoPresupuesto;
	}

	/**
	 * Obtiene el valor del atributo fechaGeneracionPresupuesto
	 *
	 * @return Retorna atributo fechaGeneracionPresupuesto
	 */
	public String getFechaGeneracionPresupuesto()
	{
		return fechaGeneracionPresupuesto;
	}

	/**
	 * Establece el valor del atributo fechaGeneracionPresupuesto
	 *
	 * @param valor para el atributo fechaGeneracionPresupuesto
	 */
	public void setFechaGeneracionPresupuesto(String fechaGeneracionPresupuesto)
	{
		this.fechaGeneracionPresupuesto = fechaGeneracionPresupuesto;
	}

	public String getAyudanteEstadoPresupuesto() {
		
		this.ayudanteEstadoPresupuesto = ValoresPorDefecto.getIntegridadDominio(this.estadoPresupuesto).toString();
		
		return ayudanteEstadoPresupuesto;
	}

	public void setAyudanteEstadoPresupuesto(String ayudanteEstadoPresupuesto) {
		this.ayudanteEstadoPresupuesto = ayudanteEstadoPresupuesto;
	}
}
