package com.princetonsa.enums.odontologia;

import org.axioma.util.log.Log4JManager;


/**
 *ENUMERICION PARA CARGAR LOS TIPOS DE CUOTA
 * @author Edgar Carvajal 
 *
 */
public enum ETiposCuota  
{
	
	/**
	 * ENUMERACIONES 
	 */
	VALOR("Valor"), PORC("Porcentaje");
	
	
	/**
	 * NOMBRE ENUM
	 */
	private String nombreEnum;

	
	
	
	
	
	/**
	 * CONSTRUTOR
	 * @param nombreEnum
	 */
	ETiposCuota(String nombreEnum)
	{
		this.nombreEnum=nombreEnum;
	}


	
	

	/**
	 *	GET NOMBRE ENUM	 
	 * @author Edgar Carvajal Ruiz
	 * @return
	 */
	public String getNombreEnum() {
		return nombreEnum;
	}
	
		
	
	/**
	 * CARGAR NOMBRE ENUM
	 * @author Edgar Carvajal Ruiz
	 * @param dato
	 * @return
	 */
	public static String cargarNombreEnum(String dato )
	{
		
		String retorno=""; 
		
		 for(ETiposCuota etipos:  ETiposCuota.values())
		 {
			 Log4JManager.info(etipos);
			 
			 if(dato.equals(etipos.toString()))
			 {
				 retorno=etipos.getNombreEnum();
			 }
		 }
		
		 
		return retorno;
	}
	

}
