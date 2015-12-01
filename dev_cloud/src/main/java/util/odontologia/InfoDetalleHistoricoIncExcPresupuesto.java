package util.odontologia;

import java.io.Serializable;

/**
 * 
 * @author axioma
 *
 */
public class InfoDetalleHistoricoIncExcPresupuesto implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -825368110432315203L;
	private String fecha;
	private String piezaDentalOboca;
	private String superficie;
	private String programaOservicio;
	private String valorFormateado;
	private String responsable;
	
	
	/**
	 * 
	 */
	public InfoDetalleHistoricoIncExcPresupuesto()
	{
		super();
		this.fecha = "";
		this.piezaDentalOboca = "";
		this.superficie = "";
		this.programaOservicio = "";
		this.valorFormateado = "";
		this.responsable = "";
	}
	/**
	 * @return the fecha
	 */
	public String getFecha()
	{
		return fecha;
	}
	/**
	 * @param fecha the fecha to set
	 */
	public void setFecha(String fecha)
	{
		this.fecha = fecha;
	}
	/**
	 * @return the piezaDentalOboca
	 */
	public String getPiezaDentalOboca()
	{
		return piezaDentalOboca;
	}
	/**
	 * @param piezaDentalOboca the piezaDentalOboca to set
	 */
	public void setPiezaDentalOboca(String piezaDentalOboca)
	{
		this.piezaDentalOboca = piezaDentalOboca;
	}
	/**
	 * @return the superficie
	 */
	public String getSuperficie()
	{
		return superficie;
	}
	/**
	 * @param superficie the superficie to set
	 */
	public void setSuperficie(String superficie)
	{
		this.superficie = superficie;
	}
	/**
	 * @return the programaOservicio
	 */
	public String getProgramaOservicio()
	{
		return programaOservicio;
	}
	/**
	 * @param programaOservicio the programaOservicio to set
	 */
	public void setProgramaOservicio(String programaOservicio)
	{
		this.programaOservicio = programaOservicio;
	}
	/**
	 * @return the valorFormateado
	 */
	public String getValorFormateado()
	{
		return valorFormateado;
	}
	/**
	 * @param valorFormateado the valorFormateado to set
	 */
	public void setValorFormateado(String valorFormateado)
	{
		this.valorFormateado = valorFormateado;
	}
	/**
	 * @return the responsable
	 */
	public String getResponsable()
	{
		return responsable;
	}
	/**
	 * @param responsable the responsable to set
	 */
	public void setResponsable(String responsable)
	{
		this.responsable = responsable;
	}
	
	
}
