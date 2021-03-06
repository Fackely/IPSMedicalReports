/*
 * @(#)ParticipacionPoolXTarifasDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */
package com.princetonsa.dao;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.decorator.ResultSetDecorator;

/**
 *  Interfaz para el acceder a la fuente de datos de la participaci?n por pool X tarifa
 *
 * @version 1.0, Nov 29 / 2004
 * @author <a href="mailto:wilson@PrincetonSA.com">Wilson R?os</a>
 */
public interface ParticipacionPoolXTarifasDao 
{
	/**
	 * Consulta la info de la participaci?n del pool X tarifa dado el cod del pool 
	 * @param con
	 * @param codigoPool
	 * @return
	 */
	public ResultSetDecorator listadoParticipacionPoolXTarifa(Connection con, int codigoPool,int institucion);

	/**
	 * B?squeda Avanzada de la participaci?n del pool X tarifa dados los criterios
	 * (pool- esquemaTarifario- %participacion)  
	 * @param con
	 * @param codigoPool
	 * @param codigoEsquemaTarifario
	 * @param porcentajeParticipacion
	 * @return
	 */
	public  ResultSetDecorator busquedaAvanzadaPoolXTarifa(	Connection con, int codigoPool, 
	        																	int codigoEsquemaTarifario, double porcentajeParticipacion,int institucion );
	
	/**
	 *  Inserta una participaci?n de pool por tarifas
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
										int cuentaConstableInstitucionAnterior,
										int institucion,
										double valorParticipacion,
										String usuario);
	
	/**
	 * Modifica una participaci?n del pool X tarifas
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
										String usuario);
	
	/**
	 * Eliminar una participaci?n del pool X tarifas
	 * @param con
	 * @param codigoPool
	 * @param codigoEsquemaTarifario
	 * @return
	 */
	public int eliminar(	Connection con, 
	        						int codigoPool,
									int codigoEsquemaTarifario,
									int institucion); 
	
	/**
	 * M?todo que busca los codigos de los esquemas tarifarios del pool que no han sido
	 * consultados en la b?squeda avanzada y son necesarios para la comparaci?n de los
	 * mapas ya que el cod del esq hace parte del primary key.
	 * @param con
	 * @param codigoPool
	 * @param codigoEsquemaTarifario
	 * @param porcentajeParticipacion
	 * @return
	 */
	public ResultSetDecorator datosNoCargadosConBusquedaAvanzada( 	Connection con, int codigoPool, 
	        																					int codigoEsquemaTarifario, double porcentajeParticipacion );
	/**
	 * M?todo que consulta un porcentaje de participaci?n para un
	 * pool y un esquema tarifario espec?fico
	 * @param con
	 * @param pool
	 * @param esquemaTarifario
	 */
	public double consultarParticipacionPoolXTarifa(Connection con, int pool, int esquemaTarifario,int institucion);
	
	
	
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
	public HashMap consultarParticipacionPoolesXConvenio (Connection connection, HashMap parametros);
	
	
	/**
	 * Eliminar una participaci?n del pool X Convenio
	 * @param con
	 * @param codigoPool
	 * @param codigoConvenio
	 * @return
	 */
	public int eliminarXConvenio(	Connection con, int codigoPool, int convenio,int institucion); 
	
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
	public  boolean ModificaConvenio(Connection con, HashMap parametros);
	

	
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
	public boolean insertarPoolXConvenio(Connection connection, HashMap parametros);
	
	
	
}
