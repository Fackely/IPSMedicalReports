package com.servinte.axioma.mundo.helper.odontologia;

import com.servinte.axioma.orm.RecomendacionesContOdonto;


/**
 * 
 * @author Edgar Carvajal 
 *
 */
public class RecomendacionesHelper 
{

	private RecomendacionesHelper(){
		
	}	
	 	
	
	
	
	
	/**
	 * METODO PARA VALIDAR SI  EXISTE RELACION CON RECOMENDACIONES SERVICIO PROGRMA
	 * RECIBE UN LA ENTIDAD  {@link RecomendacionesContOdonto} y retorna true si existe relacion entro caso false
	 * @author Edgar Carvajal Ruiz
	 * @param recomendacion
	 */
	public static final boolean existeRecomendacionxRecomendacionSerProg( RecomendacionesContOdonto  recomendacion )
	{
		boolean  retorno=Boolean.FALSE;
		
		
		if(recomendacion!=null)
		{
			if(recomendacion.getRecomendacionesServProgs()!=null && recomendacion.getRecomendacionesServProgs().size()>0)
			{
				retorno=Boolean.TRUE;
			}
		}
		
		return retorno;
	}
	
	
	
	
	
	
	
}

