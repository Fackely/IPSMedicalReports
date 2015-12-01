package util.odontologia;

import java.io.Serializable;
import java.math.BigDecimal;

import util.UtilidadTexto;

/**
 * 
 * @author axioma
 *
 */
public class InfoDctoOdontologicoPresupuesto implements Serializable
{
	/**
	 * 
	 */
	private BigDecimal detalleDctoOdon;
	
	/**
	 * 
	 */
	private BigDecimal valorDctoCALCULADO;

	/**
	 * boolean que me indica que no puedo aplicar el descuento porque ya existe un incumplimiento
	 */
	private boolean existeIncumplimiento;
	
	/**
	 * 
	 */
	private String mensaje;
	
	
	private double porcentajeDcto;
	
	/**
	 * 
	 * @param detalleDctoOdon
	 * @param valorDctoCALCULADO
	 */
	public InfoDctoOdontologicoPresupuesto()
	{
		super();
		this.detalleDctoOdon = new BigDecimal(0);
		this.valorDctoCALCULADO = new BigDecimal(0);
		this.existeIncumplimiento=false;
		this.mensaje="";
		this.porcentajeDcto=0;
	}

	/**
	 * @return the detalleDctoOdon
	 */
	public BigDecimal getDetalleDctoOdon()
	{
		return detalleDctoOdon;
	}

	/**
	 * @param detalleDctoOdon the detalleDctoOdon to set
	 */
	public void setDetalleDctoOdon(BigDecimal detalleDctoOdon)
	{
		this.detalleDctoOdon = detalleDctoOdon;
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
	 * @return the valorDctoCALCULADO
	 */
	public String getValorDctoCALCULADOFormateado()
	{
		return UtilidadTexto.formatearValores(valorDctoCALCULADO+"");
	}

	
	/**
	 * @return the valorDctoCALCULADO
	 */
	public String getValorPresupuestoMenosDctoCALCULADOFormateado(BigDecimal valorPresupuesto)
	{
		return UtilidadTexto.formatearValores(valorPresupuesto.subtract(valorDctoCALCULADO)+"");
	}
	
	
	/**
	 * @return the existeIncumplimiento
	 */
	public boolean isExisteIncumplimiento()
	{
		return existeIncumplimiento;
	}

	/**
	 * @param existeIncumplimiento the existeIncumplimiento to set
	 */
	public void setExisteIncumplimiento(boolean existeIncumplimiento)
	{
		this.existeIncumplimiento = existeIncumplimiento;
	}
	
	/**
	 * @return the existeIncumplimiento
	 */
	public boolean getExisteIncumplimiento()
	{
		return existeIncumplimiento;
	}

	/**
	 * @return the mensaje
	 */
	public String getMensaje()
	{
		return mensaje;
	}

	/**
	 * @param mensaje the mensaje to set
	 */
	public void setMensaje(String mensaje)
	{
		this.mensaje = mensaje;
	}

	/**
	 * @return the porcentajeDcto
	 */
	public double getPorcentajeDcto()
	{
		return porcentajeDcto;
	}

	/**
	 * @param porcentajeDcto the porcentajeDcto to set
	 */
	public void setPorcentajeDcto(double porcentajeDcto)
	{
		this.porcentajeDcto = porcentajeDcto;
	}
}
