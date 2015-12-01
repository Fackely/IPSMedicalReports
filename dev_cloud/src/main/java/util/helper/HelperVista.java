/**
 * 
 */
package util.helper;

import java.io.Serializable;

 

/**
 * @author axioma
 *
 */
public class HelperVista implements Serializable
{
	
	
	/**
	 * 
	 */
	private static final String ON="on";
	
	/**
	 * 
	 */
	private static final String SI="Si";

	/**
	 * 
	 */
	private  static final String off="off";
	
	
	
	/**
	 * 
	 * @param dato
	 * @return
	 */
	public  static String esActivo(String dato){
			
		String retorno="";
		
		if(dato==null || dato.trim().equals("")){
			retorno="";
		}
		else if( dato.equals(ON))
		{
			retorno="Si";
		}
		
		return retorno;
	}
	
	
	
	
	
	
	

}
