package util;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * 
 * @author alejo
 * Clase que identifica una pareja de mapeo en un HashMap que implementa la interfaz Comparable con el fin de ser usada 
 * para el ordenamiento y facilitar el ordenamiento de todos los elementos de un HashMap de registros
 */
public class ParejaMapa implements Comparable 
{
	/**
	 * Almacena el valor del elemento para ser usado en el ordenamiento
	 */
	private Object 	valor;
	
	/**
	 * Almacena el valor del elemento en el HashMap
	 */
	private Object 	valorOriginal;
	
	/**
	 * Almacena el nombre de la llave que está mapeando el elemento en el HashMap
	 */
	private String 	llave;
	
	/**
	 * Almacena la posicion inicial del elemento en el HashMap para que luego del ordenamiento se sepa como ordenar los demas valores
	 */
	private int 		posicionInicial;
	
	/**
	 * Indica el tipo de datos del valor, para ser utilizado en la comparación para el ordenamiento
	 */
	private int		tipoValor;
	
	public final static int CADENA=1;
	public final static int NUMERO=2;
	public final static int FECHA=3;
	
	/**
	 * Constructor
	 * @param valor
	 * @param posicionInicial
	 * @param llave
	 * @param tipoValor
	 */
	public ParejaMapa(Object valor, int posicionInicial, String llave, int tipoValor) throws ParseException
	{
		this.valorOriginal=valor;
		this.llave=llave;
		this.tipoValor=tipoValor;
		this.posicionInicial=posicionInicial;
		if(tipoValor==ParejaMapa.CADENA)
		{
			this.valor=""+valor;
		}
		else if(tipoValor==ParejaMapa.NUMERO)
		{
			this.valor = Double.parseDouble(""+valor);
		}
		else if(tipoValor==ParejaMapa.FECHA)
		{
			SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy");
			this.valor = formateador.parse(UtilidadFecha.conversionFormatoFechaAAp(""+valor));
		}
		else
		{
			this.valor=""+valor;
		}
	}
	
	public int compareTo(Object o) 
	{
		return ((Comparable)this.valor).compareTo( ((ParejaMapa)o).getValor() );
	}

	/**
	 * @return Returns the llave.
	 */
	public String getLlave() 
	{
		return llave;
	}

	/**
	 * @param llave The llave to set.
	 */
	public void setLlave(String llave) 
	{
		this.llave = llave;
	}

	/**
	 * @return Returns the posicionInicial.
	 */
	public int getPosicionInicial() 
	{
		return posicionInicial;
	}

	/**
	 * @param posicionInicial The posicionInicial to set.
	 */
	public void setPosicionInicial(int posicionInicial) 
	{
		this.posicionInicial = posicionInicial;
	}

	/**
	 * @return Returns the tipoValor.
	 */
	public int getTipoValor() 
	{
		return tipoValor;
	}

	/**
	 * @param tipoValor The tipoValor to set.
	 */
	public void setTipoValor(int tipoValor) 
	{
		this.tipoValor = tipoValor;
	}

	/**
	 * @return Returns the valor.
	 */
	public Object getValor() 
	{
		return valor;
	}

	/**
	 * @param valor The valor to set.
	 */
	public void setValor(Object valor) 
	{
		this.valor = valor;
	}

	/**
	 * @return Returns the valorOriginal.
	 */
	public Object getValorOriginal() 
	{
		return valorOriginal;
	}

	/**
	 * @param valorOriginal The valorOriginal to set.
	 */
	public void setValorOriginal(Object valorOriginal) 
	{
		this.valorOriginal = valorOriginal;
	}
}
