package util.facturacion;

import java.io.Serializable;

import util.ConstantesBD;

/**
 * 
 * @author wilson
 *
 */
public class InfoCobertura implements Serializable
{
	/**
	 * 
	 */
	private boolean existe;
	
	/**
	 * 
	 */
	private boolean incluido;
	
	/**
	 * 
	 */
	private boolean requiereAutorizacion;
	
	/**
	 * 
	 */
	private int semanasMinimasCotizacion;

	/**
	 * 
	 */
	private int cantidad;
	
	/**
	 * constructor 
	 * @param existe
	 * @param incluido
	 * @param requiereAutorizacion
	 * @param semanasMinimasCotizacion
	 */
	public InfoCobertura(boolean existe, boolean incluido, boolean requiereAutorizacion, int semanasMinimasCotizacion, int cantidad) 
	{
		super();
		this.existe = existe;
		this.incluido = incluido;
		this.requiereAutorizacion = requiereAutorizacion;
		this.semanasMinimasCotizacion = semanasMinimasCotizacion;
		this.cantidad= cantidad;
	}

	/**
	 * Constructor
	 *
	 */
	public InfoCobertura() 
	{
		this.existe= false;
		this.incluido = false;
		this.requiereAutorizacion = false;
		this.semanasMinimasCotizacion = ConstantesBD.codigoNuncaValido;
		this.cantidad= ConstantesBD.codigoNuncaValido;
	}

	/**
	 * @return the incluido
	 */
	public boolean incluido() {
		return incluido;
	}

	/**
	 * @return the incluido
	 */
	public boolean getIncluido() {
		return incluido;
	}
	
	/**
	 * @return the incluido
	 */
	public String getIncluidoStr() {
		if( incluido )
			return ConstantesBD.acronimoSi;
		else
			return ConstantesBD.acronimoNo;
	}
	
	/**
	 * @param incluido the incluido to set
	 */
	public void setIncluido(boolean incluido) {
		this.incluido = incluido;
	}

	/**
	 * @return the requiereAutorizacion
	 */
	public boolean getRequiereAutorizacion() {
		return requiereAutorizacion;
	}

	/**
	 * @return the requiereAutorizacion
	 */
	public String getRequiereAutorizacionStr() 
	{
		if(requiereAutorizacion)
			return ConstantesBD.acronimoSi;
		else
			return ConstantesBD.acronimoNo;
	}
	
	
	/**
	 * @param requiereAutorizacion the requiereAutorizacion to set
	 */
	public void setRequiereAutorizacion(boolean requiereAutorizacion) {
		this.requiereAutorizacion = requiereAutorizacion;
	}

	/**
	 * @return the semanasMinimasCotizacion
	 */
	public int getSemanasMinimasCotizacion() {
		return semanasMinimasCotizacion;
	}

	/**
	 * @param semanasMinimasCotizacion the semanasMinimasCotizacion to set
	 */
	public void setSemanasMinimasCotizacion(int semanasMinimasCotizacion) {
		this.semanasMinimasCotizacion = semanasMinimasCotizacion;
	}
	
	/**
	 * @param incluido the incluido to set
	 */
	public void setIncluido(String incluido) 
	{
		if(incluido.equals(ConstantesBD.acronimoNo))
			this.incluido = false;
		else if(incluido.equals(ConstantesBD.acronimoSi))
			this.incluido = true;
	}
	
	/**
	 * @param requiereAutorizacion the requiereAutorizacion to set
	 */
	public void setRequiereAutorizacion(String requiereAutorizacion) 
	{
		if(requiereAutorizacion.equals(ConstantesBD.acronimoNo))
			this.requiereAutorizacion = false;
		else if(requiereAutorizacion.equals(ConstantesBD.acronimoSi))
			this.requiereAutorizacion = true;
	}

	/**
	 * @return the existe
	 */
	public boolean existe() {
		return existe;
	}

	/**
	 * @return the existe
	 */
	public boolean getExiste() {
		return existe;
	}
	
	/**
	 * @param existe the existe to set
	 */
	public void setExiste(boolean existe) {
		this.existe = existe;
	}
	
	/**
	 * 
	 * @return
	 */
	public String loggerCobertura()
	{
		return "existe->"+this.existe()+" incluye?-> "+this.getIncluido()+" req aut-> "+this.requiereAutorizacion+ " semanas-> "+this.semanasMinimasCotizacion+" CANTIDAD --> "+this.cantidad;
	}

	/**
	 * @return the cantidad
	 */
	public int getCantidad() {
		return cantidad;
	}

	/**
	 * @param cantidad the cantidad to set
	 */
	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}
	
}