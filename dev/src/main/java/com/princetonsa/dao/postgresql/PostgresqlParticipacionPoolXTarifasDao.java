/*
 * @(#)PostgresqlParticipacionPoolXTarifasDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */
package com.princetonsa.dao.postgresql;

import java.sql.Connection;
import com.princetonsa.decorator.ResultSetDecorator;
import java.util.HashMap;

import com.princetonsa.dao.ParticipacionPoolXTarifasDao;
import com.princetonsa.dao.sqlbase.SqlBaseParticipacionPoolXTarifasDao;

/**
 * Implementación postgresql de las funciones de acceso a la fuente de datos
 * para la participación pool X tarifa
 *
 * @version 1.0, Abril 29 / 2004
 * @author <a href="mailto:wilson@PrincetonSA.com">Wilson Ríos</a>
 */
public class PostgresqlParticipacionPoolXTarifasDao implements ParticipacionPoolXTarifasDao 
{
	/**
	 * Consulta la info de la participación del pool X tarifa dado el cod del pool 
	 * @param con
	 * @param codigoPool
	 * @return
	 */
	public ResultSetDecorator listadoParticipacionPoolXTarifa(Connection con, int codigoPool,int institucion)
	{
	    return SqlBaseParticipacionPoolXTarifasDao.listadoParticipacionPoolXTarifa(con, codigoPool,institucion);
	}
	
	/**
	 * Búsqueda Avanzada de la participación del pool X tarifa dados los criterios
	 * (pool- esquemaTarifario- %participacion)  
	 * @param con
	 * @param codigoPool
	 * @param codigoEsquemaTarifario
	 * @param porcentajeParticipacion
	 * @return
	 */
	public  ResultSetDecorator busquedaAvanzadaPoolXTarifa(	Connection con, int codigoPool, 
	        																	int codigoEsquemaTarifario, double porcentajeParticipacion, int institucion)
	{
	    return SqlBaseParticipacionPoolXTarifasDao.busquedaAvanzadaPoolXTarifa(con,codigoPool,codigoEsquemaTarifario,porcentajeParticipacion,institucion);
	}

	/**
	 *  Inserta una participación de pool por tarifas
	 * @param con
	 * @param codigoPool
	 * @param codigoEsquemaTarifario
	 * @param porcentajeParticipacion
	 * @param cuentaContablePool
	 * @param cuentaContableInstitucion
	 * @return
	 */
	public int  insertar(		Connection con,
										int codigoPool,
										int codigoEsquemaTarifario,
										double porcentajeParticipacion,
										int cuentaContablePool,
										int cuentaContableInstitucion,
										int cuentaContableInstitucionAnterior,
										int institucion,
										double valorParticipacion,
										String usuario)
	{
	    return SqlBaseParticipacionPoolXTarifasDao.insertar(con,codigoPool,codigoEsquemaTarifario, porcentajeParticipacion, cuentaContablePool, cuentaContableInstitucion,cuentaContableInstitucionAnterior,institucion,valorParticipacion, usuario );
	}
	
	/**
	 * Modifica una participación del pool X tarifas
	 * @param con
	 * @param codigoEsquemaTarifarioNuevo
	 * @param porcentajeParticipacion
	 * @param cuentaContablePool
	 * @param cuentaContableInstitucion
	 * @param codigoPool
	 * @param codigoEsquemaTarifarioAntiguo
	 * @return
	 */
	public int modificar(		Connection con, 
										int codigoEsquemaTarifarioNuevo,
										double porcentajeParticipacion, 
										int cuentaContablePool,
										int cuentaContableInstitucion,
										int codigoPool,
										int codigoEsquemaTarifarioAntiguo,
										int cuentaContableInstitucionAnterior,
										int institucion,
										double valorParticipacion,
										String usuario) 
	{
	    return SqlBaseParticipacionPoolXTarifasDao.modificar(con, codigoEsquemaTarifarioNuevo, porcentajeParticipacion, cuentaContablePool, cuentaContableInstitucion, codigoPool, codigoEsquemaTarifarioAntiguo, cuentaContableInstitucionAnterior,institucion,valorParticipacion,usuario);
	}
	
	/**
	 * Eliminar una participación del pool X tarifas
	 * @param con
	 * @param codigoPool
	 * @param codigoEsquemaTarifario
	 * @return
	 */
	public int eliminar(	Connection con, 
	        						int codigoPool,
									int codigoEsquemaTarifario,
									int institucion)
	{
	    return SqlBaseParticipacionPoolXTarifasDao.eliminar(con, codigoPool, codigoEsquemaTarifario, institucion);
	}
	
	/**
	 * Método que busca los codigos de los esquemas tarifarios del pool que no han sido
	 * consultados en la búsqueda avanzada y son necesarios para la comparación de los
	 * mapas ya que el cod del esq hace parte del primary key.
	 * @param con
	 * @param codigoPool
	 * @param codigoEsquemaTarifario
	 * @param porcentajeParticipacion
	 * @return
	 */
	public ResultSetDecorator datosNoCargadosConBusquedaAvanzada( 	Connection con, int codigoPool, 
	        																					int codigoEsquemaTarifario, double porcentajeParticipacion )
	{
	    return SqlBaseParticipacionPoolXTarifasDao.datosNoCargadosConBusquedaAvanzada(con,codigoPool, codigoEsquemaTarifario, porcentajeParticipacion);
	}
	
	/**
	 * Método que consulta un porcentaje de participación para un
	 * pool y un esquema tarifario específico
	 * @param con
	 * @param pool
	 * @param esquemaTarifario
	 */
	public double consultarParticipacionPoolXTarifa(Connection con, int pool, int esquemaTarifario,int institucion)
	{
		return SqlBaseParticipacionPoolXTarifasDao.consultarParticipacionPoolXTarifa(con, pool, esquemaTarifario,institucion);
	}
	
	/**
	 * Este metodo se encarga de Consultar los datos de la tabla participacion_pooles_x_tarifas
	 * Los key's del HashMap parametros son:
	 * ---------------------------------
	 * 		KEY'S DE PARAMETROS
	 * ---------------------------------
	 * --institucion --> Requerido
	 * --pool --> Requerido
	 * --convenio --> Requerido
	 * 
	 * Los key del mapa que devuelve son:
	 * -------------------------------
	 *  KEY'S DEL MAPA QUE DEVUELVE
	 * ------------------------------
	 * --codigoConvenio, nombreConvenio, institucion,
	 * porcentajeParticipacion, cuentaPool, cuentaInstitucion,
	 * cuentaInstitucionAnterior, estabd.
	 * @param connection
	 * @param parametros
	 * @return
	 **/
	public HashMap consultarParticipacionPoolesXConvenio (Connection connection, HashMap parametros)
	{
		return SqlBaseParticipacionPoolXTarifasDao.consultarParticipacionPoolesXConvenio(connection, parametros);
	}

	/**
	 * Eliminar una participación del pool X Convenio
	 * @param con
	 * @param codigoPool
	 * @param codigoConvenio
	 * @return
	 */
	public int eliminarXConvenio(	Connection con, 
	        								int codigoPool,
											int convenio,
											int institucion) 
	{
		return SqlBaseParticipacionPoolXTarifasDao.eliminarXConvenio(con, codigoPool, convenio, institucion);
	}
	
	
	/**
	 * Modifica Los datos de participacion pooles por convenio
	 * el HashMap parametros contiene los siguientes key's
	 * -------------------------------------
	 * 			KEY'S PARAMETROS
	 * -------------------------------------
	 * --codigoConvenio --> Requerido
	 * --porcentajeParticipacion --> Requerido
	 * --cuentaPool --> Requerido
	 * --cuentaInstitucion --> Requerido
	 * --usuario --> Requerido
	 * --cuentaInstitucionAnterior --> Requerido
	 * --codigoPool --> Requerido
	 * --institucion --> Requerido
	 * -----------------------------------------
	 * Connection con
	 * HashMap parametros
	 * @return true si es exitoso.
	 * */
	public boolean ModificaConvenio(Connection con, HashMap parametros)
	{
		return SqlBaseParticipacionPoolXTarifasDao.ModificaConvenio(con, parametros);
	}
	
	/**
	 * inserta Los datos de participacion pooles por convenio
	 * el HashMap parametros contiene los siguientes key's
	 * -------------------------------------
	 * 			KEY'S PARAMETROS
	 * -------------------------------------
	 * --codigoConvenio --> Requerido
	 * --porcentajeParticipacion --> Requerido
	 * --cuentaPool --> Opcional
	 * --cuentaInstitucion --> Opcional
	 * --usuario --> Requerido
	 * --cuentaInstitucionAnterior --> Opcional
	 * --codigoPool --> Requerido
	 * --institucion --> Requerido
	 * -----------------------------------------
	 * Connection con
	 * HashMap parametros
	 * @return true si es exitoso.
	 * */
	
	public boolean insertarPoolXConvenio(Connection connection, HashMap parametros)
	{
		return SqlBaseParticipacionPoolXTarifasDao.insertarPoolXConvenio(connection, parametros);
	}
}
