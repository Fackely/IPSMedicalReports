package com.princetonsa.mundo.administracion;

import java.sql.Connection;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.administracion.FactorConversionMonedasDao;
import com.princetonsa.dto.administracion.DtoFactorConversionMonedas;

/**
 * 
 * @author wilson
 *
 */
public class FactorConversionMonedas 
{
	
	/*-----------------------
	 * 		ATRIBUTOS
	 ------------------------*/
	static Logger logger = Logger.getLogger(FactorConversionMonedas.class);
	
	/*-----------------------
	 * 		FIN ATRIBUTOS
	 ------------------------*/
	
	
	/*----------------------------------
	 *  		METODOS
	 ---------------------------------*/
		
		/**
		 * Instancia DAO
		 * */
		public static FactorConversionMonedasDao factorConversionMonedasDao()
		{
			return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getFactorConversionMonedasDao();
		}
		
		/**
		 * Consulta  
		 * @param con
		 * @param centroAtencion
		 * @return
		 */
		public static HashMap cargar(Connection con, DtoFactorConversionMonedas dtoFactor)
		{
			return factorConversionMonedasDao().cargar(con, dtoFactor);
		}
		
		
		/**
		 * 
		 * @param con
		 * @param string
		 * @return
		 */
		public static boolean eliminarRegistro(Connection con, int codigo)
		{
			return factorConversionMonedasDao().eliminarRegistro(con, codigo);
		}
		
		/**
		 * 
		 * @param con
		 * @param codigo
		 */
		public static boolean insertar(Connection con, DtoFactorConversionMonedas dtoFactor)
		{
			return factorConversionMonedasDao().insertar(con, dtoFactor);
		}
		
		
		/**
		 * 
		 * @param con
		 * @param codigo
		 */
		public static boolean modificar(Connection con, DtoFactorConversionMonedas dtoFactor)
		{
			return factorConversionMonedasDao().modificar(con, dtoFactor);
		}
}
