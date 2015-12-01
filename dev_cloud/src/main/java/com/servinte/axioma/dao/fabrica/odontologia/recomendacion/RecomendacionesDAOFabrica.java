package com.servinte.axioma.dao.fabrica.odontologia.recomendacion;

import com.servinte.axioma.dao.impl.odontologia.recomendacion.RecomendacionSerProSerProgDAO;
import com.servinte.axioma.dao.impl.odontologia.recomendacion.RecomendacionServicioProgramaDAO;
import com.servinte.axioma.dao.impl.odontologia.recomendacion.RecomendacionesContratoDAO;
import com.servinte.axioma.dao.interfaz.odontologia.recomendacion.IRecomendacionSerProServProDAO;
import com.servinte.axioma.dao.interfaz.odontologia.recomendacion.IRecomendacionServicioProgramaDAO;
import com.servinte.axioma.dao.interfaz.odontologia.recomendacion.IRecomendacionesContratoDAO;



/**
 * FABRICA PARA RECOMENDACIONES 
 * @author Edgar Carvajal
 *
 */
public abstract class RecomendacionesDAOFabrica 
{

	
	/**
	 * CREAR INSTANCIA DE RECOMENDACIONES DAO 
	 * @author Edgar Carvajal Ruiz
	 * @return
	 */
	public static final IRecomendacionesContratoDAO  crearRecomendacionesDao()
	{
		return new RecomendacionesContratoDAO();
	} 
	
	
	
	/**
	 * CREAR INSTANCIA RECOMENADACION SERVICIO PROGRAMA DAO
	 * @author Edgar Carvajal Ruiz
	 * @return
	 */
	public static final IRecomendacionServicioProgramaDAO crearRecomendacionServicioProgramaDao(){
		return new RecomendacionServicioProgramaDAO();
	}
	
	
	
	/**
	 * CREAR INSTANCIA RECOMENDACION SERVICIO PROGRAMA. SERVICIO PROGRAMA DAO 
	 * @author Edgar Carvajal Ruiz
	 * @return
	 */
	public static final IRecomendacionSerProServProDAO crearRecomendacionServProServProDao(){
		
		return new  RecomendacionSerProSerProgDAO();
	}

}
