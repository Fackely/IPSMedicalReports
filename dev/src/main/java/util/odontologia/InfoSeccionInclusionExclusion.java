package util.odontologia;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 
 * @author axioma
 *
 */
public class InfoSeccionInclusionExclusion implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7172878400876800942L;

	/**
	 * 
	 */
	private ArrayList<InfoInclusionExclusionPiezaSuperficie> listaPiezasDentalesSuperficies;
	
	/**
	 * 
	 */
	private ArrayList<InfoInclusionExclusionBoca> listaBoca;

	/**
	 * 
	 * @param listaPiezasDentalesSuperficies
	 * @param listaBoca
	 */
	public InfoSeccionInclusionExclusion()
	{
		super();
		this.listaPiezasDentalesSuperficies = new ArrayList<InfoInclusionExclusionPiezaSuperficie>();
		this.listaBoca = new ArrayList<InfoInclusionExclusionBoca>();
	}

	/**
	 * @return the listaPiezasDentalesSuperficies
	 */
	public ArrayList<InfoInclusionExclusionPiezaSuperficie> getListaPiezasDentalesSuperficies()
	{
		return listaPiezasDentalesSuperficies;
	}

	/**
	 * @param listaPiezasDentalesSuperficies the listaPiezasDentalesSuperficies to set
	 */
	public void setListaPiezasDentalesSuperficies(
			ArrayList<InfoInclusionExclusionPiezaSuperficie> listaPiezasDentalesSuperficies)
	{
		this.listaPiezasDentalesSuperficies = listaPiezasDentalesSuperficies;
	}

	/**
	 * @return the listaBoca
	 */
	public ArrayList<InfoInclusionExclusionBoca> getListaBoca()
	{
		return listaBoca;
	}

	/**
	 * @param listaBoca the listaBoca to set
	 */
	public void setListaBoca(ArrayList<InfoInclusionExclusionBoca> listaBoca)
	{
		this.listaBoca = listaBoca;
	} 
	
	
	
	
}
