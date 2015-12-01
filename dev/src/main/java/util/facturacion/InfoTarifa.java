package util.facturacion;

import java.io.Serializable;
import java.util.Vector;

import util.UtilidadTexto;

/**
 * 
 * @author wilson
 *
 */
public class InfoTarifa implements Serializable  
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	private String valor;
	
	/**
	 * 
	 */
	private String nuevaTarifa;

	/**
	 * 
	 */
	private boolean existe;
	
	/**
	 * 
	 */
	private Vector<String> porcentajes= new Vector<String>();
	
	/**
	 * 
	 * @param porcentaje
	 * @param valor
	 * @param nuevaTarifa
	 */
	public InfoTarifa(String valor, String nuevaTarifa, boolean existe, Vector<String> porcentajes) 
	{
		super();
		this.valor = valor;
		this.nuevaTarifa = nuevaTarifa;
		this.existe= existe;
		this.porcentajes= porcentajes;
	}
	
	/**
	 * 
	 * @param porcentaje
	 * @param valor
	 * @param nuevaTarifa
	 */
	public InfoTarifa() 
	{
		this.valor = "";	
		this.nuevaTarifa = "";
		this.existe=false;
		this.porcentajes= new Vector<String>();
	}

	/**
	 * @return the nuevaTarifa
	 */
	public String getNuevaTarifa() {
		return nuevaTarifa;
	}

	/**
	 * @param nuevaTarifa the nuevaTarifa to set
	 */
	public void setNuevaTarifa(String nuevaTarifa) {
		this.nuevaTarifa = nuevaTarifa;
	}

	/**
	 * @return the porcentajes
	 */
	public Vector<String> getPorcentajes() {
		return porcentajes;
	}

	/**
	 * @param porcentajes the porcentajes to set
	 */
	public void setPorcentajes(Vector<String> porcentajes) {
		this.porcentajes = porcentajes;
	}

	/**
	 * @return the valor
	 */
	public String getValor() {
		return valor;
	}

	/**
	 * @param valor the valor to set
	 */
	public void setValor(String valor) {
		this.valor = valor;
	}

	/**
	 * @return the existe
	 */
	public boolean getExiste() 
	{
		if(UtilidadTexto.isEmpty(this.getNuevaTarifa())
			&& this.getPorcentajes().size()<=0
			&& UtilidadTexto.isEmpty(this.getValor()))
		{
			return false;
		}
		else
		{
			return true;
		}
	}

	/**
	 * 
	 * @return
	 */
	public boolean existe()
	{
		return this.getExiste();
	}
	
	/**
	 * @param existe the existe to set
	 */
	public void setExiste(boolean existe) {
		this.existe = existe;
	}
	
	
	
}
