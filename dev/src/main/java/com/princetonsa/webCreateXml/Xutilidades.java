/*
 * @(#)Xutilidades.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.5.0_06
 *
 */
package com.princetonsa.webCreateXml;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.StringTokenizer;

/**
 * Clase que contiene las utilidades para la correcta generacion del web.xml,
 * no se reutilizo el package util de axioma debido a que esta funcionalidad
 * debe ejecutarse independientemente
 *  
 * @author <a href="mailto:wilson@PrincetonSA.com">Wilson R�os</a>
 *
 */
public class Xutilidades 
{
	/**
	 * "Aplana" una cadena de texto, quit�ndole espacios, acentos y �'s, marcando
	 * el inicio de cada palabra posterior a la primera mediante el uso de may�sculas
	 * (como la convenci�n de nombres de variables en Java).
	 * @param str la cadena que se desea aplanar
	 * @return la cadena aplanada, o una cadena vac�a si "str" es nula
	 */
	public static String flattenString (String str) 
	{
		if (str != null && !str.equals("")) 
		{
			str = str.trim();
			if (str.length() > 0) 
			{
				str = removeAccents(str.toLowerCase());
				StringTokenizer strtok = new StringTokenizer (str, " -\f\n\r\t");
				String tmp, resp = strtok.nextToken();
				char [] tmpArray;
				while (strtok.hasMoreTokens()) 
				{
					tmp = strtok.nextToken();
					tmpArray = tmp.toCharArray();
					tmpArray[0] = Character.toUpperCase(tmpArray[0]);
					resp += new String(tmpArray);
				}
				str = resp;
			}
		}
		return (str != null) ? str : "";
	}
	
	/**
	 * Reemplaza los acentos y las �'s de una cadena de texto por el caracter no acentuado.
	 * @param la cadena que se desea procesar
	 * @return la cadena de texto sin acentos y sin �'s
	 */
	public static String removeAccents (String str) 
	{
		if (str != null && !str.equals("")) 
		{
			str = str.replaceAll("[����]", "a");
			str = str.replaceAll("[����]", "e");
			str = str.replaceAll("[����]", "i");
			str = str.replaceAll("[����]", "o");
			str = str.replaceAll("[����]", "u");
			str = str.replaceAll("�", "n");
			str = str.replaceAll("[����]", "A");
			str = str.replaceAll("[����]", "E");
			str = str.replaceAll("[����]", "I");
			str = str.replaceAll("[����]", "O");
			str = str.replaceAll("[����]", "U");
			str = str.replaceAll("�", "N");
		}
		return (str != null) ? str : "";
	}

	/**
	 * retorna el mapa dado el resultset
	 * @param mapa
	 * @param columnNames
	 * @param rs
	 * @param toLowerCase
	 * @param use_
	 * @return
	 */
	public static HashMap resultSet2HashMap(String[] columnNames, ResultSet rs, boolean toLowerCase, boolean use_ )
	{
		HashMap mapa= new HashMap();
	    String keyValue = "";
	 	String regValue = "";
	    int i, k=0;

	    if( rs == null)
	    {
	    	 mapa.put("numRegistros", (new Integer(0)));
	    	 return mapa;
	    }
	    try 
	    {
	    	if(rs.first() == false)
	        {
	    	    mapa.put("numRegistros", (new Integer(0)));
	            return mapa;
	        }
	    	do
	    	{
	    		for( i=0; i<columnNames.length; i++)
	    		{
	    		   if(!use_) 
	    			   keyValue = columnNames[i]  +"[" +k + "]";
	    		   else
	    		       keyValue = columnNames[i]  +"_" +k ;
	    		   if(rs.getString(columnNames[i])==null)
	    			   regValue = "";
	    		   else
	    			   regValue = rs.getString(columnNames[i]); 
	    		   if( toLowerCase == true )
	    			   regValue = regValue.toLowerCase();
	    		   if(regValue == null)
	    			   regValue = "";
	    		   mapa.put(keyValue, regValue); 
	    		}
			    k  +=  1;
	    	}
	    	while(rs.next());
	    	mapa.put("numRegistros", (new Integer(k)));
	    }
	    catch (Exception e) 
	    {
		   e.printStackTrace();
		}
	    return mapa;
	}
		
}