package util.odontologia;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;

import util.ConstantesBD;
import util.UtilidadTexto;

/**
 * 
 * @author axioma
 *
 */
public class InfoPresupuestoExclusionesInclusiones implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8929033319273760576L;

	/**
	 * 
	 */
	private BigDecimal codigoPresupuesto;
	
	/**
	 * 
	 */
	private BigDecimal consecutivoPresupuesto;
	
	/**
	 * 
	 */
	private String fecha;
	
	/**
	 * 
	 */
	private BigDecimal valorPresupuesto;
	
	/**
	 * 
	 */
	private InfoSeccionInclusionExclusion seccionInclusiones;
	
	/**
	 * 
	 */
	private InfoSeccionInclusionExclusion seccionExclusiones;

	/**
	 * 
	 */
	private ArrayList<InfoConvenioContratoPresupuesto> listaConveniosContratos;
	
	/**
	 * 
	 */
	private BigDecimal codigoPlanTratamiento;
	
	/**
	 * 
	 */
	public InfoPresupuestoExclusionesInclusiones()
	{
		super();
		this.codigoPresupuesto = new BigDecimal(0);
		this.consecutivoPresupuesto = new BigDecimal(0);
		this.valorPresupuesto = new BigDecimal(0);
		this.seccionInclusiones = new InfoSeccionInclusionExclusion();
		this.seccionExclusiones = new InfoSeccionInclusionExclusion();
		this.fecha="";
		this.listaConveniosContratos= new ArrayList<InfoConvenioContratoPresupuesto>();
		this.codigoPlanTratamiento= BigDecimal.ZERO;
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
	 * @return the consecutivoPresupuesto
	 */
	public BigDecimal getConsecutivoPresupuesto()
	{
		return consecutivoPresupuesto;
	}

	/**
	 * @param consecutivoPresupuesto the consecutivoPresupuesto to set
	 */
	public void setConsecutivoPresupuesto(BigDecimal consecutivoPresupuesto)
	{
		this.consecutivoPresupuesto = consecutivoPresupuesto;
	}

	/**
	 * @return the valorPresupuesto
	 */
	public BigDecimal getValorPresupuesto()
	{
		return valorPresupuesto;
	}

	/**
	 * @return the valorPresupuesto
	 */
	public String getValorPresupuestoFormateado()
	{
		return UtilidadTexto.formatearValores(valorPresupuesto+"");
	}

	
	/**
	 * @param valorPresupuesto the valorPresupuesto to set
	 */
	public void setValorPresupuesto(BigDecimal valorPresupuesto)
	{
		this.valorPresupuesto = valorPresupuesto;
	}

	/**
	 * @return the seccionInclusiones
	 */
	public InfoSeccionInclusionExclusion getSeccionInclusiones()
	{
		return seccionInclusiones;
	}

	/**
	 * @param seccionInclusiones the seccionInclusiones to set
	 */
	public void setSeccionInclusiones(
			InfoSeccionInclusionExclusion seccionInclusiones)
	{
		this.seccionInclusiones = seccionInclusiones;
	}

	/**
	 * @return the seccionExclusiones
	 */
	public InfoSeccionInclusionExclusion getSeccionExclusiones()
	{
		return seccionExclusiones;
	}

	/**
	 * @param seccionExclusiones the seccionExclusiones to set
	 */
	public void setSeccionExclusiones(
			InfoSeccionInclusionExclusion seccionExclusiones)
	{
		this.seccionExclusiones = seccionExclusiones;
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
	 * 
	 * @return
	 */
	public boolean getExisteInclusionPiezas()
	{
		return (this.getSeccionInclusiones().getListaPiezasDentalesSuperficies().size()>0); 
	}
	
	/**
	 * Método que permite determinar si existen inclusiones sin precontratar
	 * @return
	 */
	public boolean getExistenInclusionesSinPrecontratar (){
		
		return (this.getExisteInclusionPiezasSinPrecontratar() || this.getExisteInclusionBocaSinPrecontratar());
	}
	
	/**
	 * Método que permite determinar si existen inclusiones de pieza dental
	 * sin precontratar
	 * 
	 * @return
	 */
	public boolean getExisteInclusionPiezasSinPrecontratar()
	{
		for (InfoInclusionExclusionPiezaSuperficie inclusion : this.getSeccionInclusiones().getListaPiezasDentalesSuperficies()) {
			
			if(inclusion.getPrecontratada().equals(ConstantesBD.acronimoNo)){
				
				return true;
			}
		}
		
		return false;
	}
	
	
	/**
	 * Método que permite determinar si existen inclusiones en boca
	 * sin precontratar
	 * @return
	 */
	public boolean getExisteInclusionBocaSinPrecontratar()
	{
		for (InfoInclusionExclusionBoca inclusion : this.getSeccionInclusiones().getListaBoca()) {
			
			if(inclusion.getPrecontratada().equals(ConstantesBD.acronimoNo)){
				
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean getExisteInclusionBoca()
	{
		return (this.getSeccionInclusiones().getListaBoca().size()>0); 
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean getExisteInclusiones()
	{
		return (this.getExisteInclusionPiezas() || this.getExisteInclusionBoca());
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean getExisteExclusionPiezas()
	{
		return (this.getSeccionExclusiones().getListaPiezasDentalesSuperficies().size()>0); 
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean getExisteExclusionBoca()
	{
		return (this.getSeccionExclusiones().getListaBoca().size()>0); 
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean getExisteExclusiones()
	{
		return (this.getExisteExclusionPiezas() || this.getExisteExclusionBoca());
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean getExisteInclusionesExclusiones()
	{
		return (this.getExisteExclusiones() || this.getExisteInclusiones());
	}

	/**
	 * @return the listaConveniosContratos
	 */
	public ArrayList<InfoConvenioContratoPresupuesto> getListaConveniosContratos()
	{
		return listaConveniosContratos;
	}

	/**
	 * @param listaConveniosContratos the listaConveniosContratos to set
	 */
	public void setListaConveniosContratos(
			ArrayList<InfoConvenioContratoPresupuesto> listaConveniosContratos)
	{
		this.listaConveniosContratos = listaConveniosContratos;
	}

	/**
	 * @return the codigoPlanTratamiento
	 */
	public BigDecimal getCodigoPlanTratamiento() {
		return codigoPlanTratamiento;
	}

	/**
	 * @param codigoPlanTratamiento the codigoPlanTratamiento to set
	 */
	public void setCodigoPlanTratamiento(BigDecimal codigoPlanTratamiento) {
		this.codigoPlanTratamiento = codigoPlanTratamiento;
	}
}