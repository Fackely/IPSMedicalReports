/*
 * @(#)BeanParejaCentroCosto.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */
 package util.Beans;

/**
 * Clase Auxiliar que permite capturar los datos 
 * en Solicitar InterConsulta
 */
public class BeanParejaCentroCosto 
{
	/**
	 * Arreglo con parejas codigo nombre del centro de costo
	 */
	private String centrosCosto[];
	
	/**
	 * Arreglo de enteros con los codigos de los centros de costo 
	 */
	private int codigoCentroCosto[];
	
	/**
	 * Arreglo de enteros con los nombres de los centros de costo
	 */
	private String nombreCentroCosto[];

	/**
	 * Constructora de la clase
	 *
	 */	
	public BeanParejaCentroCosto ()
	{
		centrosCosto = new String[0];
	
		codigoCentroCosto= new int[0];
	
		nombreCentroCosto = new String[0];
	}
	/**
	 * Este método separa lo que llega de la página anterior en sus 
	 * componentes codigo y nombre
	 *
	 */
	public void separarNombresCodigo()
	{
		int i;
		String arregloTemporal[];
		if (centrosCosto!=null)
		{
			codigoCentroCosto=new int[centrosCosto.length];
			nombreCentroCosto=new String[centrosCosto.length];
			for (i=0;i<centrosCosto.length;i++)
			{
				arregloTemporal=centrosCosto[i].split("-");
				codigoCentroCosto[i]=Integer.parseInt(arregloTemporal[0]);
				nombreCentroCosto[i]=arregloTemporal[1];
			}
		}
	}
	
	/**
	 * @return
	 */
	public String[] getCentrosCosto() {
		return centrosCosto;
	}

	/**
	 * @return
	 */
	public int[] getCodigoCentroCosto() {
		return codigoCentroCosto;
	}

	/**
	 * @return
	 */
	public String[] getNombreCentroCosto() {
		return nombreCentroCosto;
	}

	/**
	 * @param strings
	 */
	public void setCentrosCosto(String[] strings) {
		centrosCosto = strings;
	}
	
	public int getCodigoCentroCosto(int i)
	{
		return this.codigoCentroCosto[i];
	}
	public String getNombreCentroCosto(int i)
	{
		return this.nombreCentroCosto[i];
	}
}
