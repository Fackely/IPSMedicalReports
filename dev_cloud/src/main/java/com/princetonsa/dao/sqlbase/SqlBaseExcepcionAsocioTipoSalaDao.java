/*
 * Sep 20/ 2005
 */
package com.princetonsa.dao.sqlbase;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadFecha;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;

/**
 * @author Sebastián Gómez
 *
 * Objeto usado para manejar los accesos comunes a la fuente de datos
 * para la parametrización de Excepciones Asocios por Tipo Sala
 * 
 * Modificada el 21/11/07 por Jhony Alexander Duque A.
 * por documento 525 --> Cambio en Funcionalidades de parametrizacion
 * 						 Cirugia por DyT.
 */
public class SqlBaseExcepcionAsocioTipoSalaDao {
	
	/**
	 * Manejador de logs de la clase
	 */
	private static Logger logger=Logger.getLogger(SqlBaseExcepcionAsocioTipoSalaDao.class);
	
	/*------------------------------------------------------------------
	 * Modificacion  Atribustos.
	 -------------------------------------------------------------------*/
	
		/*------------------------
		 * Cadena de insercion
		 -------------------------*/
	/**
	 * Cadena de insercion para la tabla ex_asocio_tipo_sala
	 * la cual es utilizada como encabezado de det_ex_asocio_tipo_sala
	 */
	private final static String strCadenaInsercion0="INSERT INTO ex_asocio_tipo_sala (esq_tar_particular,esq_tar_general,tipo_sala," +
													"institucion, usuario_modifica,fecha_modifica,hora_modifica,codigo) VALUES (?,?,?,?,?,?,?,?) ";
	
	
	
	/**
	 * Cadena de insercion de datos para la tabla det_ex_asocio_tipo_sala
	 * la cual es el detalle de ex_asocio_tipo_sala
	 */

	private final static String strCadenaInsercion1= " INSERT INTO det_ex_asocio_tipo_sala (codigo_ex_asocio_tipo_sala,tipo_servicio,tipo_cirugia," +
													 "tipo_liquidacion,cantidad, liquidar_sobre_tarifa,asocio,usuario_modifica,fecha_modifica,hora_modifica,codigo )" +
													 " VALUES (?,?,?,?,?,?,?,?,?,?,?)";	

		/*-----------------------------
		 * Fin cadenas de insercion
		 -----------------------------*/
	
		/*------------------------------
		 * Cadena Modificacion
		 -------------------------------*/
	
	/**
	 * Cadena de modificacion para la tabla det_ex_asocio_tipo_sala
	 * la cual sirve como detalle de la tabla ex_asocio_tipo_sala y 
	 * esta no se puede modificar
	 */
	private final static String strCadenaModificacion1="UPDATE  det_ex_asocio_tipo_sala SET tipo_servicio=?,tipo_cirugia=?,tipo_liquidacion=?," +
													   "cantidad=?,liquidar_sobre_tarifa=?,asocio=?, usuario_modifica=?, fecha_modifica=?, hora_modifica=?" +
													   " WHERE codigo=? AND codigo_ex_asocio_tipo_sala=?";
	
	
		/*------------------------------
		 *Fin Cadena Modificacion
		 -------------------------------*/
	
		/*--------------------------------
		 * cadena de eliminacion
		 ---------------------------------*/
	
	/**
	 * Cadena de eliminacion de datos de la tabla ex_asocio_tipo_sala
	 */
	private final static String strCadenaEliminacion0=" DELETE FROM ex_asocio_tipo_sala WHERE codigo=? AND institucion=?";
	
	/**
	 * Cadena de eliminacion de datos de la tabla det_ex_asocio_tipo_sala
	 */
	private final static String strCadenaEliminacion1="DELETE FROM det_ex_asocio_tipo_sala WHERE codigo=? AND codigo_ex_asocio_tipo_sala=?";
	
		/*--------------------------------
		 * cadena de eliminacion
		 ---------------------------------*/
	
		/*---------------------------------
		 * Cadena de busquedas
		 ---------------------------------*/
	
		
	/**
	 * Cadena que consulta los datos de la tabla det_ex_asocio_tipo_sala
	 */
	private final static String strCadenaConsulta1="SELECT deats.codigo AS codigo0," +
													   "deats.codigo_ex_asocio_tipo_sala AS cod_ex_asoc_tip_sala1," +
													   "deats.tipo_servicio AS tipo_servicio2," +
													   "deats.tipo_cirugia AS tipo_cirugia3," +
													   "deats.tipo_liquidacion AS tipo_liquidacion4," +
													   "deats.cantidad AS cantidad5," +
													   "deats.asocio AS asocio6," +
													   "deats.liquidar_sobre_tarifa AS liquidar_sobre_tarifa7," +
													   "deats.usuario_modifica AS usuario_modifica9," +
													   "eats.tipo_sala AS tipo_sala10," +
													   "COALESCE (eats.esq_tar_particular,"+ConstantesBD.codigoNuncaValido+") AS esq_tar_part11," +
													   "COALESCE (eats.esq_tar_general,"+ConstantesBD.codigoNuncaValido+") AS esq_tar_gen12," +
													   "eats.institucion AS institucion14," +
													   "'"+ConstantesBD.acronimoSi+"' AS esta_bd8 " +
												   "FROM det_ex_asocio_tipo_sala deats " +
												   "INNER JOIN ex_asocio_tipo_sala eats ON (eats.codigo=deats.codigo_ex_asocio_tipo_sala) " +
												   "WHERE eats.institucion=? ";
	
	
	private static String strCadenaEstaUtilizada="SELECT COUNT (*) FROM det_ex_asocio_tipo_sala WHERE codigo_ex_asocio_tipo_sala=?";

		/*---------------------------------
		 *FIN Cadena de busquedas
		 ---------------------------------*/
	
		/*----------------------------------------------
		 * Descripcion de los indices de las consultas
		 -----------------------------------------------*/
	
	/**
	 * indices para la consulta del detalle que se encuentra
	 * en la tabla det_ex_asocio_tipo_sala
	 */
	public final static String [] indices0={"codigo0_","codExAsocTipSala1_","tipoServicio2_","tipoCirugia3_","tipoLiquidacion4_","cantidad5_","asocio6_",
											"liquidarSobreTarifa7_","estaBd8_","usuarioModifica9_","tipoSala10_","esqTarPart11_","esqTarGen12_","tipoEsqTar13_",
											"institucion14_","esqTarFio15"};
	

	
		/*----------------------------------------------
		 * Descripcion de los indices de las consultas
		 -----------------------------------------------*/
	
	/*-------------------------------------------------------------------
	 * Fin Modificacion Atributos.
	 -------------------------------------------------------------------*/
	
	
	/*--------------------------------------------------------------------------
	 * Modificacion de Metodos
	 -------------------------------------------------------------------------*/
	
		/*---------------------------------
		 * METODOS INSERTAR
		 ---------------------------------*/
	
	/**
	 * Metodo de insercion para la tabla ex_asocio_tipo_sala
	 * Los parametros:
	 * @Connection connection
	 * @HashMap datos
	 * -------------------------------------
	 * 		KEY'S DEL HASHMAP DATOS
	 * -------------------------------------
	 * -- esqTarParticular --> Opcional
	 * -- esqTarGeneral --> Opcional
	 * -- tipoSala --> Requerido
	 * -- institucion --> Requerido
	 * -- usuarioModifica --> Requerido 
	 * ---------------------------------------
	 * @return boolean
	 *	--true --> la operacion re realizo correctamente
	 *  --false --> la operacion se aborto.										
	 **/
	public static boolean insertar0 (Connection connection, HashMap datos)
	{
		logger.info("\n\n***********************************************");
		logger.info("\n ::::::ENTRE A INSERTAR0 SQL::::::::");
		logger.info("\n hashmap al entrar ==> "+datos);
		logger.info("\n ***********************************************");
		
		String cadena = strCadenaInsercion0;
			
		try 
		{
			
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			
			int codigo = UtilidadBD.obtenerSiguienteValorSecuencia(connection, "seq_ex_asocio_tipo_sala");
			
			// se valida que si venga el esquema tarifario particular
			if (datos.containsKey(indices0[11]) && !datos.get(indices0[11]).equals("") && !(datos.get(indices0[11])+"").equals("-1"))
				ps.setObject(1, datos.get(indices0[11]));
			else
				ps.setNull(1, Types.NUMERIC);
			
			//se valida que si venga el esquema tarifario general
			if (datos.containsKey(indices0[12]) && !datos.get(indices0[12]).equals("") && !(datos.get(indices0[12])+"").equals("-1"))
				ps.setObject(2, datos.get(indices0[12]));
			else
				ps.setNull(2, Types.NUMERIC);
			
			//como el tipo de sala es requerido no se valida
			ps.setObject(3, datos.get(indices0[10]));
			
			//como la institucion es requerida entonces no se valida
			ps.setObject(4, datos.get(indices0[14]));
				
			//como el usuario modifica es querido no se valida
			ps.setObject(5, datos.get(indices0[9]));
			
			ps.setObject(6, UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
			ps.setObject(7, UtilidadFecha.getHoraActual());
			
			//codigo
			ps.setObject(8, codigo);
			
			if (ps.executeUpdate()>0)
				return true; 
			
			
		} 
		catch (SQLException e)
		{
			logger.error("\n Problema insertando datos en la tabla ex_asocio_tipo_sala "+e);
		}
		
		
		
		return false;
	}
	
	

	/**
	 * Metodo encargado de la insercion de los datos en la tabla det_ex_asocio_tipo_sala
	 * Los parametros son:
	 * @Connection connection
	 * @HashMap datos
	 * --------------------------------
	 * 	    KEY'S DEL HASHMAP DATOS
	 * --------------------------------
	 * -- codExAsocTipSala --> Requerido
	 * -- tipoServicio --> Opcional
	 * -- tipoCirugia --> Opcional
	 * -- tipoLiquidacion --> Requerido
	 * -- cantidad --> Requerido
	 * -- liquidarSobreAsocio --> Opcional
	 * -- asocio --> Requerido
	 * -- usuarioModifica --> Requerido
	 * -----------------------------------
	 * @return boolean
	 *	--true --> la operacion re realizo correctamente
	 *  --false --> la operacion se aborto.					
	 **/
	public static boolean insertar1 (Connection connection, HashMap datos)
	{
		logger.info("\n\n***********************************************");
		logger.info("\n ::::::ENTRE A INSERTAR1 SQL::::::::");
		logger.info("\n hashmap al entrar ==> "+datos);
		logger.info("\n ***********************************************");
		
		String cadena = strCadenaInsercion1;
		
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			//codigo
			int codigo = UtilidadBD.obtenerSiguienteValorSecuencia(connection, "seq_det_ex_asocio_tipo_sala");
			
			//el codigo del encabezado es requerido por eso no se valida
			ps.setObject(1, datos.get(indices0[1]));
			
			//se valida que el tipo de servicio venga, de no ser asi se le asigna null.
			if (datos.containsKey(indices0[2]) && !datos.get(indices0[2]).equals("") && !(datos.get(indices0[2])+"").equals("-1"))
				ps.setObject(2, datos.get(indices0[2]));
			else
				ps.setNull(2, Types.VARCHAR);
			
			//se valida que el tipo de sirugia venga, de no ser asi se le asigna null.
			if (datos.containsKey(indices0[3]) && !datos.get(indices0[3]).equals("") && !(datos.get(indices0[3])+"").equals("-1"))
				ps.setObject(3, datos.get(indices0[3]));
			else
				ps.setNull(3, Types.VARCHAR);
			//el tipo de liquidacion es requerido por tal motivo no se valida
			ps.setObject(4, datos.get(indices0[4]));
			
			//la cantidad es requerida por tal motivo no se valida
			ps.setObject(5, datos.get(indices0[5]));
			
			//se valida que liquidar sobre asocio venga, de no ser asi se le asigna null.
			if (datos.containsKey(indices0[7]) && !datos.get(indices0[7]).equals("") && !(datos.get(indices0[7])+"").equals("-1"))
				ps.setObject(6, datos.get(indices0[7]));
			else
				ps.setNull(6, Types.VARCHAR);
			
			//el asocio es requerido por tal motivo no se valida
			ps.setObject(7, datos.get(indices0[6]));
			
			
			//el usuario midifica es requerido por tal motivo no se valida
			ps.setObject(8, datos.get(indices0[9]));
			
			ps.setObject(9, UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
			ps.setObject(10, UtilidadFecha.getHoraActual());
			
			//codigo
			ps.setObject(11, codigo);
			
			if (ps.executeUpdate()>0)
				return true; 
			
		}
		catch (SQLException e) 
		{
			logger.error("\n Problema insertando datos en la tabla det_ex_asocio_tipo_sala "+e);
		}
		
		return false;
	}
	
	
	/**
	 * Metodo encargado de insertar los datos tango en el encabezado (ex_asocio_tipo_sala) como en el cuerpo (det_ex_asocio_tipo_sala) 
	 * Los Parametros son:
	 * ---------------------------------------
	 * LOS KEY'S DEL HASHMAP DATOS SON
	 * ---------------------------------------
	 * -- esqTarParticular --> Opcional
	 * -- esqTarGeneral --> Opcional
	 * -- tipoSala --> Requerido
	 * -- institucion --> Requerido
	 * -- usuarioModifica --> Requerido 
	 * -- codExAsocTipSala --> Requerido
	 * -- tipoServicio --> Opcional
	 * -- tipoCirugia --> Opcional
	 * -- tipoLiquidacion --> Requerido
	 * -- cantidad --> Requerido
	 * -- liquidarSobreAsocio --> Opcional
	 * -- asocio --> Requerido
	 * -- usuarioModifica --> Requerido
	 * @return boolean
	 *	--true --> la operacion re realizo correctamente
	 *  --false --> la operacion se aborto.			
	 **/
	public static boolean insertarGeneral (Connection connection, HashMap datos) 
	{
		logger.info("\n\n***********************************************");
		logger.info("\n ::::::ENTRE A INSERTARGENERAL SQL::::::::");
		logger.info("\n hashmap al entrar ==> "+datos);
		logger.info("\n ***********************************************");
		
		boolean operacionTrue = false;
		
		if (datos.containsKey(indices0[1]) &&  (datos.get(indices0[1]).equals("") || (datos.get(indices0[1])+"").equals("-1")))
		{
		
			if (insertar0(connection, datos))
			{
				try 
					{
						datos.put(indices0[1], DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).obtenerUltimoValorSecuencia(connection, "seq_ex_asocio_tipo_sala"));
					} 
				catch (Exception e)
				{
					logger.error("\n problema consiguiendo la secuencia de ex_asocio_tipo_sala "+e);
				}
			}
		}
		
		operacionTrue=insertar1(connection, datos);
		
		
		return operacionTrue;
	}
	
		/*---------------------------------
		 * FIN METODOS INSERTAR
		 ---------------------------------*/
	
	
		/*---------------------------------
		 * METODOS MODIFICAR
		 ----------------------------------*/
	
	
	/**
	 *Metodo encargado de modificar los datos de la tabla det_ex_asocio_tipo_sala
	 *Los parametros son:
	 *@Connection connection
	 *@HashMap datos
	 *------------------------------------
	 *	     KEY'S DEL HASHMAP DATOS
	 *------------------------------------
	 * -- tipoServicio --> Opcional
	 * -- tipoCirugia --> Opcional
	 * -- tipoLiquidacion --> Requerido
	 * -- cantidad --> Requerido
	 * -- liquidarSobreAsocio --> Opcional
	 * -- asocio --> Requerido
	 * -- codigo --> Requerido
	 * -- codExAsocTipSala --> Requerido
	 * -- usuarioModifica --> Requerido
	 * ------------------------------------
	 * @return boolean
	 * --true --> la operacion re realizo correctamente
	 * --false --> la operacion se aborto.		
	 **/
	public static boolean modificar1 (Connection connection,HashMap datos)
	{
		logger.info("\n\n***********************************************");
		logger.info("\n ::::::ENTRE A MODIFICAR1 SQL::::::::");
		logger.info("\n hashmap al entrar ==> "+datos);
		logger.info("\n ***********************************************");
		
		String cadena = strCadenaModificacion1;
		
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			
			//se valida que el tipo de servicio venga, de no ser asi se le asigna null.
			if (datos.containsKey(indices0[2]) && !datos.get(indices0[2]).equals("") && !(datos.get(indices0[2])+"").equals("-1"))
				ps.setObject(1, datos.get(indices0[2]));
			else
				ps.setNull(1, Types.VARCHAR);
			
			
			//se valida que el tipo de sirugia venga, de no ser asi se le asigna null.
			if (datos.containsKey(indices0[3]) && !datos.get(indices0[3]).equals("") && !(datos.get(indices0[3])+"").equals("-1"))
				ps.setObject(2, datos.get(indices0[3]));
			else
				ps.setNull(2, Types.VARCHAR);
			
			//el tipo de liquidacion es requerido por tal motivo no se valida
			ps.setObject(3, datos.get(indices0[4]));
			
			//la cantidad es requerida por tal motivo no se valida
			ps.setObject(4, datos.get(indices0[5]));
			
			//se valida que liquidar sobre asocio venga, de no ser asi se le asigna null.
			if((datos.get(indices0[4])+"").equals("3"))
				if (datos.containsKey(indices0[7]) && !datos.get(indices0[7]).equals("") && !(datos.get(indices0[7])+"").equals("-1"))
					ps.setObject(5, datos.get(indices0[7]));
				else
					ps.setNull(5, Types.VARCHAR);	
			else
				ps.setNull(5, Types.VARCHAR);	
			
			//el asocio es requerido por tal motivo no se valida
			ps.setObject(6, datos.get(indices0[6]));
			
			//el usuario midifica es requerido por tal motivo no se valida
			ps.setObject(7, datos.get(indices0[9]));
			
			ps.setObject(8, UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
			ps.setObject(9, UtilidadFecha.getHoraActual());
			
			//el codigo de la tabla det_ex_asocio_tipo_sala es requerido por eso no se valida
			ps.setObject(10, datos.get(indices0[0]));
			
			//el codigo del encabezado es requerido por eso no se valida
			ps.setObject(11, datos.get(indices0[1]));
			
			
			if(ps.executeUpdate()>0)
				return true;

			
			
		}
		catch (SQLException e) 
		{
			logger.error("\n Problema modificcando los datos en la tabla det_ex_asocio_tipo_sala "+e);
		}
		
		
		return false;
	}
	
	
		/*---------------------------------
		 * FIN METODOS MODIFICAR
	 	 ----------------------------------*/
	
		/*---------------------------------
		 * METODOS DE BUSQUEDA
		 ---------------------------------*/
	
	/**
	 * Metodo encargado de consultar la informacion  de las tablas
	 * det_ex_asocio_tipo_sala y  ex_asocio_tipo_sala
	 * Los parametros son :
	 * @Connection connection
	 * @HashMap Criterios
	 * ------------------------------
	 * KEY'S DEL HASHMAP CRITERIOS
	 * ------------------------------
	 * -- codigo --> Requerido
	 * -- institucion --> Requerido
	 * -- tipoServicio --> Opcional
	 * -- tipoCirugia --> Opcional
	 * -- tipoSala --> Opcional
	 * -- asocio --> Opcional
	 * -- tipoLiquidacion --> Opcional
	 * -- liquidarSobreAsocio --> Opcional
	 * -- esqTarPart --> Opcional
	 * -- esqTarGen --> Opcional
	 * -- tipoEsqTar --> Requerido
	 * --------------------------------
	 * @return HashMap
	 * -------------------------------
	 * KEY'S DEL HASHMAP QUE RETORNA
	 * -------------------------------
	 * --codigo_
	 * --codExAsocTipSala_
	 * --tipoServicio_
	 * --tipoCirugia_
	 * --tipoLiquidacion_
	 * --cantidad_
	 * --asocio_
	 * --liquidarSobreAsocio_
	 * --estaBd_
	 * --usuarioModifica_
	 * --tipoSala_
	 **/
	public static HashMap buscar (Connection connection,HashMap criterios)
	{
		logger.info("\n\n***********************************************");
		logger.info("\n ::::::ENTRE A BUSCAR SQL::::::::");
		logger.info("\n hashmap al entrar ==> "+criterios);
		logger.info("\n ***********************************************");
		HashMap mapa = new HashMap ();
		String cadena = strCadenaConsulta1, where ="";
		
		if (criterios.containsKey(indices0[2]) && !criterios.get(indices0[2]).equals("") && !(criterios.get(indices0[2])+"").equals("-1"))
			where+=" AND deats.tipo_servicio='"+criterios.get(indices0[2])+"'";
		
		if (criterios.containsKey(indices0[3]) && !criterios.get(indices0[3]).equals("") && !(criterios.get(indices0[3])+"").equals("-1"))
			where+=" AND deats.tipo_cirugia='"+criterios.get(indices0[3])+"'";
		
		if ((criterios.get(indices0[13])+"").equals("1"))
			where+=" AND eats.esq_tar_particular="+criterios.get(indices0[11]);
		else
			if ((criterios.get(indices0[13])+"").equals("0"))
				where+=" AND eats.esq_tar_general="+criterios.get(indices0[12]);
		
		if (criterios.containsKey(indices0[6]) && !criterios.get(indices0[6]).equals("") && !(criterios.get(indices0[6])+"").equals("-1"))
			where+=" AND deats.asocio="+criterios.get(indices0[6]);
		
		if (criterios.containsKey(indices0[4]) && !criterios.get(indices0[4]).equals("") && !(criterios.get(indices0[4])+"").equals("-1")) 
			where+=" AND deats.tipo_liquidacion="+criterios.get(indices0[4]);
		
		if (criterios.containsKey(indices0[7]) && !criterios.get(indices0[7]).equals("") && !(criterios.get(indices0[7])+"").equals("-1"))
			where+=" AND deats.liquidar_sobre_tarifa='"+criterios.get(indices0[7])+"'";
		
		if (criterios.containsKey(indices0[10]) && !criterios.get(indices0[10]).equals("") && !(criterios.get(indices0[10])+"").equals("-1"))
			where+=" AND eats.tipo_sala="+criterios.get(indices0[10]);
		
			cadena+=where+" ORDER BY tipo_servicio, tipo_cirugia, asocio";
		logger.info("\n consulta ==> "+cadena);
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			
			ps.setObject(1, criterios.get(indices0[14]));
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()),true,true);
			
						
		}
		catch (SQLException e)
		{
			logger.error("\n Problema consultando los datos en la tabla det_ex_asocio_tipo_sala "+e);
		}
		
		mapa.put("INDICES_MAPA", indices0);
	//	logger.info("\n el vlaor del hashmap al salir "+mapa);
		return mapa;
	}
	
		/*---------------------------------
		 * METODOS DE BUSQUEDA
		 ---------------------------------*/
	
	
	/*---------------------------------
	 * METODOS DE ELIMINACION
	 ---------------------------------*/
	
	/**
	 * Metodo encargado de eliminar los datos de la tabla ex_asocio_tipo_sala
	 * que son el encabezado de la tabla det_ex_asocio_tipo_sala
	 * El HashMap datos debe contener las siguientes llaves:
	 * -------------------------------------
	 * KEY'S DEL HASHMAP DATOS
	 * -------------------------------------
	 * -- codExAsocTipSala1_ --> Requerido
	 * -- institucion14_ --> Requerido
	 * -----------------------------------
	 * @return boolean
	 * --true --> la operacion re realizo correctamente
	 * --false --> la operacion se aborto.	
	 */
	public static boolean eliminar0 (Connection connection, HashMap datos)
	{
		logger.info("\n\n***********************************************");
		logger.info("\n ::::::ENTRE A ELIMINAR0 SQL::::::::");
		logger.info("\n hashmap al entrar ==> "+datos);
		logger.info("\n ***********************************************");
		
		String cadena =strCadenaEliminacion0;
		
		
		
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			// el valor del codigo del consecutivo de ex_asocio_tipo_sala
			ps.setObject(1,	datos.get(indices0[1]));
			//el codigo de la institucion
			ps.setObject(2,	datos.get(indices0[14]));
		
			if(ps.executeUpdate()>0)
				return true;
			
		}
		catch (SQLException e) 
		{
			logger.error("\n problema eliminando datos de la tabla ex_asocio_tipo_sala "+e);
		}
		
		return false;
	}
	
	
	
	/**
	 * Metodo encargado de Eliminar los datos de la 
	 * tabla det_ex_asocio_tipo_sala
	 * @param connection
	 * @param datos
	 * -------------------------------
	 * KEY'S DEL HASHMAP DATOS
	 * -------------------------------
	 *-- codigo0_ --> Requerido
	 *-- codExAsocTipSala1_ --> Requerido
	 * -----------------------------------
	 * @return boolean
	 * --true --> la operacion re realizo correctamente
	 * --false --> la operacion se aborto.	
	 */
	public static boolean eliminar1 (Connection connection, HashMap datos)
	{
		logger.info("\n\n***********************************************");
		logger.info("\n ::::::ENTRE A ELIMINAR1 SQL::::::::");
		logger.info("\n hashmap al entrar ==> "+datos);
		logger.info("\n ***********************************************");
		
		String cadena =strCadenaEliminacion1;
		
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			//codigo del consecutivo de la tabla det_ex_asocio_tipo_sala
			ps.setObject(1, datos.get(indices0[0]));
			//codigo del consecutivo de la tabla ex_asocio_tipo_sala
			ps.setObject(2, datos.get(indices0[1]));
			
			if (ps.executeUpdate()>0)
				return true;
			
		}
		catch (SQLException e)		
		{
			logger.error("\n problema eliminando datos de la tabla det_ex_asocio_tipo_sala "+e);
			
		}
		return false;
	}
	
	
	/**
	 * Metodo encargado de determinar en que porte hace el borrado
	 * de los datos, ya sea en el encabezado o en el detalle
	 * @param connection
	 * @param datos
	 * ------------------------------
	 * KEY'S DEL HASHMAP DATOS
	 * ------------------------------
	 *-- codigo0_ --> Requerido
	 *-- codExAsocTipSala1_ --> Requerido
	 *-- institucion14_ --> Requerido
	 * @return
	 */
	public static boolean eliminarGeneral (Connection connection, HashMap datos)
	{
		logger.info("\n\n***********************************************");
		logger.info("\n ::::::ENTRE A ELIMINAR GENERAL SQL::::::::");
		logger.info("\n hashmap al entrar ==> "+datos);
		logger.info("\n ***********************************************");
		
		boolean operacionTrue = false;
		
		
		if (datos.containsKey(indices0[1]) && !datos.get(indices0[1]).equals("") && !(datos.get(indices0[1])+"").equals("-1"))
			if (estaUtilizado(connection, Integer.parseInt(datos.get(indices0[1])+""))>1)
			{
				//se eliminan los datos de la tabla det_ex_asocio_tipo_sala
				operacionTrue=eliminar1(connection, datos);
			}
			else
			{
				// se eliminan los datos de la tabla det_ex_asocio_tipo_sala
				operacionTrue=eliminar1(connection, datos);
				//se eliminan los datos de ex_asocio_tipo_sala
				if (operacionTrue)
					operacionTrue= eliminar0(connection, datos);
			}
		
		
		return operacionTrue;
	}
	
	
	
	
	/**
	 * Metodo que devuelve la cantidad de detalles
	 * que tiene un registro en el encabezado.
	 * @param connection
	 * @param esqtarfio
	 * @return
	 */
	public static int estaUtilizado (Connection connection, int codcab)
	{
		
		String cadena = strCadenaEstaUtilizada;
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			ps.setObject(1, codcab);
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
				return rs.getInt(1);
		}
		catch (SQLException e) 
		{
		  logger.info("\n problema al consultar los datos en la tabla det_ex_asocio_tipo_sala "+e);
		}
		
		return 0;
	}
	
	
	/*---------------------------------
	 * FIN METODOS DE ELIMINACION
	 ---------------------------------*/
	/*--------------------------------------------------------------------------
	 * Modificacion de Metodos
	 -------------------------------------------------------------------------*/
	

}
