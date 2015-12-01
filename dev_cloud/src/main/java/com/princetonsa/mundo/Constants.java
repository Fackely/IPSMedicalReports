/*
 * @(#)Constants.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */

package com.princetonsa.mundo;
import java.util.Hashtable;

import java.sql.Connection;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;


import util.Answer;
import util.UtilidadBD;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.TagDao;
import org.apache.log4j.Logger;
import org.axioma.util.log.Log4JManager;


/**
 * Esta clase lee de la base de datos ciertas constantes necesarias
 * para el buen funcionamiento de la aplicación, y que van a ser usadas 
 * por las clases del mundo.
 *
 * @version 1.0, Jun 12, 2003
 * @author 	<a href="mailto:sandra@PrincetonSA.com">Sandra Moya Romero</a> 
 */

public class Constants {
	 final static Hashtable constants = new Hashtable();
	private static Logger logger = Logger.getLogger(Constants.class);
	 /**
	  * Inicializa la hashtable con los valores que encuentra en la bd
	  */
	 public static void init(){
	 	
		DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		TagDao tagDao = myFactory.getTagDao();
		String consulta = "SELECT * FROM constants";
		Connection con = null;
		try{
			con = myFactory.getConnection();
			Answer ans = tagDao.resultadoConsulta(con, consulta);	
			ResultSetDecorator rs = ans.getResultSet();			
			while(rs.next()){
				constants.put(rs.getObject(1), rs.getObject(2));
			}
		}catch (SQLException ex){
			Log4JManager.error("ERROR init()", ex);
		}
		finally{
			UtilidadBD.closeConnection(con);
		}
	 }
	 
	 /**
	  * Retorna la cadena asociada a la key dada por parámetro 
	  * @param key
	  * @return
	  */
	public static String get(String key){
	 	if(constants.size() == 0) init();
	 	String value;
	 	try{
			value = (String)constants.get(key);
			 		

	 	}catch (NullPointerException ex1){
			
			logger.warn("Error lanzado en la clase com.princetonsa.mundo.Constants\nLa función Constants.getInt('null') no esta definida.\nDetalle: "+ex1);
			

			return null;
		}
		return value;
	}
	
	/**
	 * Retorna el entero asociado a la key dada por parámetro. Claramente solo se debe usar
	 * si se esta seguro que lo almacenado bajo la key es un entero, de lo contrario retorna -1. 
	 * @param key
	 * @return
	 */
	public static int getInt(String key){
	   if(constants.size() == 0) init();
	   try {
		return Integer.parseInt((String)constants.get(key));	   	
	   }catch (java.lang.NumberFormatException ex){
		
			logger.warn("Error lanzado en la clase com.princetonsa.mundo.Constants\nLa función Constants.getInt('"+key+"') va a retornar -1.\nDetalle: "+ex);
		
	   		
	   	return -1;
	   }   
	   catch (NullPointerException ex1){
		
			logger.warn("Error lanzado en la clase com.princetonsa.mundo.Constants\nLa función Constants.getInt('null') no esta definida, se va a retornar -1.\nDetalle: "+ex1);
		
		   
		   return -1;
	   }
	}	 
}
