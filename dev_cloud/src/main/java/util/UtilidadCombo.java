package util;

public class UtilidadCombo 
{

	/**
	 * @param args
	 * Sandra Milena Barreto
	 * Utilidad para llenar combo al utilizar el metodo Split
	 */
  public String[]  llenarCombo(String cadena)
  {
	
	String[] dato=cadena.split("-");																		
	 if(dato.length > 2)
	 {																			
		int posicion= cadena.indexOf("-");
		dato[1]=cadena.substring(posicion+1);																		   																			
     }	
	 return dato;
  }
  
}
