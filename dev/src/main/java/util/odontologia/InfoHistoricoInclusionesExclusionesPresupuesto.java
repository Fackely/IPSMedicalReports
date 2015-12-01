package util.odontologia;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * 
 * @author axioma
 *
 */
public class InfoHistoricoInclusionesExclusionesPresupuesto implements Serializable
{
	/**
	 * 
	 */
	private BigDecimal codigoPresupuesto;
	private String consecutivoPresupuesto;
	private String estadoActual;
	private ArrayList<InfoDetalleHistoricoIncExcPresupuesto> listaInclusiones;
	private ArrayList<InfoDetalleHistoricoIncExcPresupuesto> listaExclusiones;
	
	
	/**
	 * 
	 * @param consecutivoPresupuesto
	 * @param estadoActual
	 * @param listaInclusiones
	 * @param listaExclusiones
	 */
	public InfoHistoricoInclusionesExclusionesPresupuesto()
	{
		super();
		this.codigoPresupuesto= new BigDecimal(0);
		this.consecutivoPresupuesto = "";
		this.estadoActual = "";
		this.listaInclusiones = new ArrayList<InfoDetalleHistoricoIncExcPresupuesto>();
		this.listaExclusiones = new ArrayList<InfoDetalleHistoricoIncExcPresupuesto>();
	}
	/**
	 * @return the consecutivoPresupuesto
	 */
	public String getConsecutivoPresupuesto()
	{
		return consecutivoPresupuesto;
	}
	/**
	 * @param consecutivoPresupuesto the consecutivoPresupuesto to set
	 */
	public void setConsecutivoPresupuesto(String consecutivoPresupuesto)
	{
		this.consecutivoPresupuesto = consecutivoPresupuesto;
	}
	/**
	 * @return the estadoActual
	 */
	public String getEstadoActual()
	{
		return estadoActual;
	}
	/**
	 * @param estadoActual the estadoActual to set
	 */
	public void setEstadoActual(String estadoActual)
	{
		this.estadoActual = estadoActual;
	}
	/**
	 * @return the listaInclusiones
	 */
	public ArrayList<InfoDetalleHistoricoIncExcPresupuesto> getListaInclusiones()
	{
		return listaInclusiones;
	}
	/**
	 * @param listaInclusiones the listaInclusiones to set
	 */
	public void setListaInclusiones(
			ArrayList<InfoDetalleHistoricoIncExcPresupuesto> listaInclusiones)
	{
		this.listaInclusiones = listaInclusiones;
	}
	/**
	 * @return the listaExclusiones
	 */
	public ArrayList<InfoDetalleHistoricoIncExcPresupuesto> getListaExclusiones()
	{
		return listaExclusiones;
	}
	/**
	 * @param listaExclusiones the listaExclusiones to set
	 */
	public void setListaExclusiones(
			ArrayList<InfoDetalleHistoricoIncExcPresupuesto> listaExclusiones)
	{
		this.listaExclusiones = listaExclusiones;
	}
	/**
	 * @return the codigoPresupuesto
	 */
	public BigDecimal getCodigoPresupuesto()
	{
		return codigoPresupuesto;
	}
	/**
	 * @param codigoPresupuesto the codigoPresupuesto to set
	 */
	public void setCodigoPresupuesto(BigDecimal codigoPresupuesto)
	{
		this.codigoPresupuesto = codigoPresupuesto;
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean existeInclusiones()
	{
		return this.listaInclusiones.size()>0;
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean existeExclusiones()
	{
		return this.listaExclusiones.size()>0;
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean existeHistorico()
	{
		return (this.listaExclusiones.size()>0 || this.listaInclusiones.size()>0);
	}
}
