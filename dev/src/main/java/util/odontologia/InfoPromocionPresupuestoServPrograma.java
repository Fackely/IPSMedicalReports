package util.odontologia;

import java.io.Serializable;
import java.math.BigDecimal;

import util.ConstantesBD;

/**
 * 
 * @author axioma
 *
 */
public class InfoPromocionPresupuestoServPrograma implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6458914884134486519L;

	/**
	 * 
	 */
	private int detPromocion;
	
	/**
	 * 
	 */
	private String advertencia;
	
	/**
	 * 
	 */
	private double porcentajeHonorario;
	
	/**
	 * 
	 */
	private BigDecimal valorHonorario;
	
	/**
	 * 
	 */
	private BigDecimal valorPromocion;
	
	/**
	 * 
	 */
	private double porcentajePromocion;
	
	/**
	 * 
	 */
	private String nombre;
	
	/**
	 * 
	 */
	private boolean seleccionadoPorcentaje;
	
	/**
	 * 
	 */
	public InfoPromocionPresupuestoServPrograma()
	{
		super();
		this.detPromocion = ConstantesBD.codigoNuncaValido;
		this.advertencia = "";
		this.porcentajeHonorario = 0;
		this.valorHonorario = new BigDecimal(0);
		this.valorPromocion= new BigDecimal(0);
		this.porcentajePromocion= 0;
		this.nombre="";
		this.seleccionadoPorcentaje= false;
	}

	/**
	 * @return the detPromocion
	 */
	public int getDetPromocion()
	{
		return detPromocion;
	}

	/**
	 * @param detPromocion the detPromocion to set
	 */
	public void setDetPromocion(int detPromocion)
	{
		this.detPromocion = detPromocion;
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
	 * @return the porcentajeHonorario
	 */
	public double getPorcentajeHonorario()
	{
		return porcentajeHonorario;
	}

	/**
	 * @param porcentajeHonorario the porcentajeHonorario to set
	 */
	public void setPorcentajeHonorario(double porcentajeHonorario)
	{
		this.porcentajeHonorario = porcentajeHonorario;
	}

	/**
	 * @return the valorHonorario
	 */
	public BigDecimal getValorHonorario()
	{
		return valorHonorario;
	}

	/**
	 * @param valorHonorario the valorHonorario to set
	 */
	public void setValorHonorario(BigDecimal valorHonorario)
	{
		this.valorHonorario = valorHonorario;
	}

	/**
	 * @return the valorPromocion
	 */
	public BigDecimal getValorPromocion()
	{
		return valorPromocion;
	}

	/**
	 * @param valorPromocion the valorPromocion to set
	 */
	public void setValorPromocion(BigDecimal valorPromocion)
	{
		this.valorPromocion = valorPromocion;
	}

	/**
	 * @return the porcentajePromocion
	 */
	public double getPorcentajePromocion()
	{
		return porcentajePromocion;
	}

	/**
	 * @param porcentajePromocion the porcentajePromocion to set
	 */
	public void setPorcentajePromocion(double porcentajePromocion)
	{
		this.porcentajePromocion = porcentajePromocion;
	}

	/**
	 * @return the nombre
	 */
	public String getNombre()
	{
		return nombre;
	}

	/**
	 * @param nombre the nombre to set
	 */
	public void setNombre(String nombre)
	{
		this.nombre = nombre;
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

}