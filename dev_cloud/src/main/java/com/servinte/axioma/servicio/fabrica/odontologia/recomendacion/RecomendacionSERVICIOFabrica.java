package com.servinte.axioma.servicio.fabrica.odontologia.recomendacion;

import com.servinte.axioma.servicio.impl.odontologia.recomendacion.RecomendacionContOdonServicio;
import com.servinte.axioma.servicio.impl.odontologia.recomendacion.RecomendacionSerProgSerProgSERVICIO;
import com.servinte.axioma.servicio.impl.odontologia.recomendacion.RecomendacionServicioProgramaServicio;
import com.servinte.axioma.servicio.interfaz.odontologia.recomendacion.IRecomendacionSerProgSerProgSERVICIO;
import com.servinte.axioma.servicio.interfaz.odontologia.recomendacion.IRecomendacionServicioProgramaServicio;
import com.servinte.axioma.servicio.interfaz.odontologia.recomendacion.IRecomendacionesContOdontoServicio;


/**
 * FABRICA PARA  CREAR RECOMENDACIONES 
 * 
 * @author Edgar Carvajal
 *
 */
public abstract class RecomendacionSERVICIOFabrica 
{
	
	
	
	
	/**
	 * METODO QUE CREA RECOMENDACIONES CONTRATO 
	 * @author Edgar Carvajal Ruiz
	 * @return
	 */
	public static final IRecomendacionesContOdontoServicio crearRecomendacionesCont()
	{
		return new RecomendacionContOdonServicio(); 
	}
	
	
	
	
	/**
	 * METODO QUE CREA RECOMENDACIONES SERVICIO PROGRAMA
	 * @author Edgar Carvajal Ruiz
	 * @return
	 */
	public static final IRecomendacionServicioProgramaServicio crearRecomendacionServicioPrograma()
	{
		return new RecomendacionServicioProgramaServicio();
	}
	
	
	/**
	 * METODO QUE CREA LA INSTANCIA DE  RECOMENDACIONES SERVICIO PROGRAMA SER PROG
	 * @author Edgar Carvajal Ruiz
	 * @return
	 */
	public static final IRecomendacionSerProgSerProgSERVICIO crearRecomendacionSerProgSerPro()
	{
		return new RecomendacionSerProgSerProgSERVICIO(); 
	}
	
	
	
	
	
}
