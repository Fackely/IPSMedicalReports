/*
 * @(#)SqlBaseExcepcionesQXDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */
package com.princetonsa.dao.sqlbase;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import org.apache.log4j.Logger;
import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.mundo.salasCirugia.ExcepcionesQX;

/**
 * Implementación sql genérico de todas las funciones de acceso a la fuente de datos
 * para las excepciones QX
 *
 * @version 1.0, Oct 10 / 2005
 * @author <a href="mailto:wilson@PrincetonSA.com">Wilson Ríos</a>
 * 
 * Clase reestruturada totalmente por el documento 525 Cambio
 * en funcionalidades de parametrizacion cirugia DyT.
 * 
 * Modificado el 4/12/2007
 * Modificado por Jhony Alexander Duque A.
 */
public class SqlBaseExcepcionesQXDao 
{
    /**
	* Objeto para manejar los logs de esta clase
	*/
	private static Logger logger = Logger.getLogger(SqlBaseExcepcionesQXDao.class);
	
	/*-----------------------------------------
	 *Modificacion de Atributos
	 ------------------------------------------*/
	
		/*-----------------------------------
		 * Cadenas de insercion
		 -----------------------------------*/
	/**
	 * Cadena de insercion para la tabla (excepciones_qx)
	 * Esta tabla es utilizado como encabezado de
	 * la tabla (excepciones_qx_cc)
	 */
	private final static String strCadenaInsercion0 ="INSERT INTO excepciones_qx (convenio, fecha_inicial, fecha_final, institucion, " +
													 "usuario_modifica, fecha_modifica, hora_modifica, contrato,codigo) VALUES (?,?,?,?,?,?,?,?,?)";
	
	/**
	 * Cadena de insercion para la tabla (excepciones_qx_cc)
	 * Esta es tulizada como encabezado de la tabla
	 * (det_excepciones_qx_cc)
	 */
	private final static String strCadenaInsercion1 =" INSERT INTO excepciones_qx_cc (codigo_excepcion_qx, centro_costo, usuario_modifica," +
													 " fecha_modifica, hora_modifica, codigo, via_ingreso, tipo_paciente) VALUES (?,?,?,?,?,?,?,?)";
	
	/**
	 * cadena de insercion para la tabla (det_excepciones_qx_cc) 
	 */
	private final static String strCadenaInsercion2 ="INSERT INTO det_excepciones_qx_cc (codigo_excepcion_qx_cc, tipo_servicio, tipo_cirugia, " +
													 "tipo_liquidacion, tipo_asocio, servicio, liquidar_sobre_tarifa, especialidad, grupo_servicio," +
													 "continua_medico, continua_via_acceso, valor, usuario_modifica, fecha_modifica, hora_modifica," +
													 "tipo_sala,codigo,liquidar_x_porcentaje) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	
	
	/*-----------------------------------
	 * Fin Cadenas de insercion
	 -----------------------------------*/
	    
	
	/*-----------------------------------
	 * Cadenas de Eliminacion
	 -----------------------------------*/
	
	/**
	 *Cadena de eliminacion para la tabla (excepciones_qx) 
	 */
	private final static String strCadenaEliminacion0="DELETE FROM excepciones_qx WHERE codigo=? AND institucion=?";
	
	/**
	 * Cadena de eliminacion para la tabla (excepciones_qx_cc)
	 */
	private final static String strCadenaEliminacion1="DELETE FROM excepciones_qx_cc WHERE codigo=?";
	
	/**
	 * Cadena de eliminacion para la tabla (det_excepciones_qx_cc)
	 */
	private final static String strCadenaEliminacion2="DELETE FROM det_excepciones_qx_cc WHERE codigo=?";
	/*-----------------------------------
	 * Fin Cadenas de Eliminacion
	 -----------------------------------*/
	
	/*-----------------------------------
	 * Cadenas de Modificacion
	 -----------------------------------*/
	
	/**
	 * Cadena de modificacion de los datos de
	 * la tabla (excepciones_qx)
	 */
	private final static String strCadenaModificacion0="UPDATE excepciones_qx SET fecha_inicial=?, fecha_final=?, usuario_modifica=?," +
													   " fecha_modifica=?, hora_modifica=? WHERE codigo=? AND institucion=?";
	
	/**
	 * Cadena de modificacion de los datos de
	 * la tabla (det_excepciones_qx_cc)
	 */
	private final static String strCadenaModificacion1="UPDATE det_excepciones_qx_cc SET tipo_servicio=?, tipo_cirugia=?, " +
													   "tipo_liquidacion=?, tipo_asocio=?, servicio=?, liquidar_sobre_tarifa=?, especialidad=?," +
													   "grupo_servicio=?, continua_medico=?, continua_via_acceso=?, valor=?, usuario_modifica=?," +
													   " fecha_modifica=?, hora_modifica=?, tipo_sala=?, liquidar_x_porcentaje=? WHERE codigo=?";
	
	
	/*-----------------------------------
	 * Fin Cadenas de Modificacion
	 -----------------------------------*/
	
	/*-----------------------------------
	 * Cadenas de Consulta
	 -----------------------------------*/
	
	/**
	 * Cadena de consulta de datos de vigencias
	 * en la tabla ( excepciones_qx )
	 */
	private final static String strCadenaConsulta0="SELECT " +
														" exqx.codigo AS codigo_excepcion_qx6," +
														" CASE WHEN  exqx.fecha_inicial IS NULL THEN '' ELSE to_char(exqx.fecha_inicial, 'DD/MM/YYYY') END AS fecha_inicial2," +
														" CASE WHEN  exqx.fecha_final IS NULL THEN '' ELSE to_char(exqx.fecha_final, 'DD/MM/YYYY') END AS fecha_final3, " +
														" exqx.contrato AS contrato23," + 
														"'"+ConstantesBD.acronimoSi+"' AS esta_bd20 " +
													"FROM excepciones_qx exqx " +
													"INNER JOIN contratos c ON (c.codigo=exqx.contrato)" +
													"WHERE exqx.convenio=? AND institucion=?";
	
	/**
	 * Cadena encargada de consultar los diferentes
	 * centros de costo para las vigencias
	 */
	private final static String strCadenaConsulta1="SELECT " +
														" exqxcc.codigo AS codigo_excepcion_qx_cc8," +
														" exqxcc.centro_costo AS centro_costo7  " +
													"FROM excepciones_qx_cc exqxcc " +
													"WHERE exqxcc.codigo_excepcion_qx=?";
	
	/**
	 * Cadena encargada de consultar los datos
	 * para ese convenio, vigencias y centro de costo.
	 */
	private final static String strCadenaConsulta2="SELECT " +
													   " detexqxcc.codigo AS det_codigo_excepcion_qx_cc0," +
													   " COALESCE(detexqxcc.codigo_excepcion_qx_cc||'','"+ConstantesBD.codigoNuncaValido+"')  AS codigo_excepcion_qx_cc8," +
													   " COALESCE(detexqxcc.tipo_servicio||'','"+ConstantesBD.codigoNuncaValido+"')  AS tipo_servicio9," +
													   " detexqxcc.tipo_cirugia AS tipo_cirugia10," +
													   " detexqxcc.tipo_liquidacion AS tipo_liquidacion11," +
													   " detexqxcc.tipo_asocio AS tipo_asocio12," +
													   " COALESCE(detexqxcc.tipo_sala||'','"+ConstantesBD.codigoNuncaValido+"')  AS tipo_sala21," +
													   " COALESCE(detexqxcc.servicio||'','"+ConstantesBD.codigoNuncaValido+"')  AS servicio13," +
													   " getnombreservicio(detexqxcc.servicio,'"+ConstantesBD.codigoTarifarioCups+"') AS nombre_servicio25," +
													   " COALESCE(detexqxcc.liquidar_sobre_tarifa||'','"+ConstantesBD.codigoNuncaValido+"')  AS liquidar_sobre_asocio14," +
													   " COALESCE(detexqxcc.especialidad||'','"+ConstantesBD.codigoNuncaValido+"')  AS especialidad15," +
													   " COALESCE(detexqxcc.grupo_servicio||'','"+ConstantesBD.codigoNuncaValido+"')  AS grupo_servicio16," +
													   " COALESCE(detexqxcc.continua_medico||'','"+ConstantesBD.codigoNuncaValido+"')  AS continua_medico17," +
													   " COALESCE(detexqxcc.continua_via_acceso||'','"+ConstantesBD.codigoNuncaValido+"')  AS continua_via_acceso18," +
													   " detexqxcc.valor AS valor19," +
													   "'"+ConstantesBD.acronimoSi+"' AS esta_bd20, " +
													   " COALESCE(detexqxcc.liquidar_x_porcentaje||'','') AS liquidar_x_porcentaje28," +
													   "  COALESCE(getcodigocups(detexqxcc.servicio,0)||'','') As codigo_cups_cervicio29 " +
												   "FROM det_excepciones_qx_cc detexqxcc " +
												   "INNER JOIN excepciones_qx_cc exqxcc ON (exqxcc.codigo=detexqxcc.codigo_excepcion_qx_cc) ";
												  
												  

	
		/*-----------------------------------
		 * cadena de consulta de utilizacion
		 ------------------------------------*/
	

		/*-----------------------------------
		 * cadena de consulta de utilizacion
	 	------------------------------------*/
	
	/*-----------------------------------
	 * Fin Cadenas de Consulta
	 -----------------------------------*/
	
	
	public final static String [] indices = ExcepcionesQX.indices;
	
	/*-----------------------------------------
	 *Fin de Modificacion de Atributos
	 ------------------------------------------*/
	
	
	/*-------------------------------------------
	 * Metodos modificados
	 -------------------------------------------*/
	
		/*-----------------------------------------
		 *  Metodos para las Busquedas de datos
		 ------------------------------------------*/
	
	/**
	 * Metodo encargado de Buscar las
	 * Vigencias para el convenio seleccionado.
	 * Parametros:
	 * @connection
	 * @criterios
	 * ------------------------------
	 * KEY'S DEL HASHMAP CRITERIOS
	 * ------------------------------
	 * -- convenio1_ --> Requerido
	 * -- institucion4_ --> Requerido
	 * ---------------------------------
	 * Retorna
	 * @return mapa
	 * --------------------------------
	 * KEY'S DEL HASHMAP MAPA
	 * --------------------------------
	 * -- codigoExcepcionQx6_
	 * -- fechaInicial2_
	 * -- fechaFinal3_ 
	 * -- contrato23_
	 */
	public static HashMap buscar0 (Connection connection,HashMap criterios)
	{
		logger.info("\n\n***********************************************");
		logger.info("\n ::::::ENTRE A BUSCAR0 SQL::::::::");
		logger.info("\n hashmap al entrar ==> "+criterios);
		logger.info("\n ***********************************************");
			
		HashMap mapa = new HashMap ();
		String cadena = strCadenaConsulta0;
		
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			//1)se agrega el convenio
			ps.setObject(1, criterios.get(indices[1]));
			//2) se agrega la institucion 
			ps.setObject(2, criterios.get(indices[4]));
			//se carga el mapa con los datos de la consulta
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()),true,true);
			
			for (int i=0;i<Integer.parseInt(mapa.get("numRegistros")+"");i++)
				mapa.put(indices[22]+i, UtilidadBD.estaUtilizadoEnTabla(connection, "excepciones_qx_cc", "codigo_excepcion_qx", Integer.parseInt(mapa.get(indices[6]+i)+"")));
		}
		catch (SQLException e) 
		{
			logger.info("\n Problema Buscando los datos en la tabla excepciones_qx "+e);
		}
		
		//se cargan los indices al mapa.
		mapa.put("INDICES_MAPA", indices);
		
		return mapa;
	}
	
	
	
	/**
	 * Metodo encargado de buscar los 
	 * centros de costo para una vigencia.
	 * @param connection
	 * @param criterios
	 * -----------------------------------------
	 * KEY'S PARA EL HASHMAP CRITERIOS
	 * -----------------------------------------
	 * -- codigoExcepcionQx6_
	 * ---------------------------------------
	 * @return
	 * ---------------------------------
	 * KEY'S DEL MAPA QUE RETORNA
	 * ---------------------------------
	 * -- codigoExcepcionQxCc8_
	 * -- centroCosto7_
	 */
	public static HashMap buscar1 (Connection connection, HashMap criterios )
	{
		logger.info("\n\n***********************************************");
		logger.info("\n ::::::ENTRE A BUSCAR1 SQL::::::::");
		logger.info("\n hashmap al entrar ==> "+criterios);
		logger.info("\n ***********************************************");
			
		HashMap mapa = new HashMap ();
		String cadena = strCadenaConsulta1;
		
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			//1)se agrega el codigo de la tabla excepcion_qx
			ps.setObject(1, criterios.get(indices[6]));
		
			//se carga el mapa con los datos de la consulta
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()),true,true);

		}
		catch (SQLException e) 
		{
			logger.info("\n Problema Buscando los datos en la tabla excepciones_qx_cc "+e);
		}
		
		//se cargan los indices al mapa.
		mapa.put("INDICES_MAPA", indices);
		
	
		return mapa;
	}
	
	/**
	 * Metodo encargado  de buscar los datos 
	 * de  excepciones quirurjicas que pertenecen
	 * a un convenio, una vigencia y un centro de costo.
	 * @param connection
	 * @param criterios
	 * ----------------------------------
	 * KEY'S DEL HASHMAP CRITERIOS
	 * ----------------------------------
	 * -- detCodigoExcepcionQxCc0_ --> Opcional
	 * -- centroCosto7_ --> Opcional
	 * -- especialidad15_ --> Opcional
	 * -- grupoServicio16_ --> Opcional
	 * -- tipoServicio9_ --> Opcional
	 * -- tipoSala21_ --> Opcional
	 * -- servicio13_ --> Opcional
	 * -- tipoAsocio12_ --> Opcional
	 * -- tipoCirugia10_ --> Opcional
	 * -- continuaMedico17_ --> Opcional
	 * -- continuaViaAcceso18_ --> Opcional
	 * -- tipoLiquidacion11_ --> Opcional
	 * -- valor19_ --> Opcional
	 * @return
	 */
	public static HashMap buscar2 (Connection connection, HashMap criterios )
	{
		logger.info("\n\n***********************************************");
		logger.info("\n ::::::ENTRE A BUSCAR2 SQL::::::::");
		logger.info("\n hashmap al entrar ==> "+criterios);
		logger.info("\n ***********************************************");
			
		HashMap mapa = new HashMap ();
		String cadena = strCadenaConsulta2,where =" WHERE 1=1 ";
		
		//1)se agrega el codigo de l aexcepcion por vigenica y centro de costo
		if (criterios.containsKey(indices[0]) && !UtilidadTexto.isEmpty(criterios.get(indices[0])+"") && !(criterios.get(indices[0])+"").equals(ConstantesBD.codigoNuncaValido+""))
			where +=" AND detexqxcc.codigo_excepcion_qx_cc="+criterios.get(indices[0]);
		else
			//2) se verifica si viene el centro de costo para filtrar por el
			if (criterios.containsKey(indices[7]) && !UtilidadTexto.isEmpty(criterios.get(indices[7])+""))
			{
				if (!(criterios.get(indices[7])+"").equals(ConstantesBD.codigoNuncaValido+""))
					where +=" AND exqxcc.centro_costo="+criterios.get(indices[7]);
				else
					where +=" AND exqxcc.centro_costo IS NULL ";
			}
		
		//3)se verifica si viene la especialidad para filtar por ella.
		if (criterios.containsKey(indices[15]) && !UtilidadTexto.isEmpty(criterios.get(indices[15])+"") && !(criterios.get(indices[15])+"").equals(ConstantesBD.codigoNuncaValido+""))
			where +=" AND detexqxcc.especialidad="+criterios.get(indices[15]);
		
		//4)se verifica si viene el grupo de servicio para filtar por el.
		if (criterios.containsKey(indices[16]) && !UtilidadTexto.isEmpty(criterios.get(indices[16])+"") && !(criterios.get(indices[16])+"").equals(ConstantesBD.codigoNuncaValido+""))
			where +=" AND detexqxcc.grupo_servicio="+criterios.get(indices[16]);
		
		//5)se verifica si viene el tipo de servicio para filtar por el.
		if (criterios.containsKey(indices[9]) && !UtilidadTexto.isEmpty(criterios.get(indices[9])+"") && !(criterios.get(indices[9])+"").equals(ConstantesBD.codigoNuncaValido+""))
			where +=" AND detexqxcc.tipo_servicio='"+criterios.get(indices[9])+"'";
		
		//6)se verifica si viene el tipo de sala para filtar por el.
		if (criterios.containsKey(indices[21]) && !UtilidadTexto.isEmpty(criterios.get(indices[21])+"") && !(criterios.get(indices[21])+"").equals(ConstantesBD.codigoNuncaValido+""))
			where +=" AND detexqxcc.tipo_sala="+criterios.get(indices[21]);
		
		//7)se verifica si viene el servicio para filtar por el.
		if (criterios.containsKey(indices[13]) && !UtilidadTexto.isEmpty(criterios.get(indices[13])+"") && !(criterios.get(indices[13])+"").equals(ConstantesBD.codigoNuncaValido+""))
			where +=" AND detexqxcc.servicio="+criterios.get(indices[13]);
		
		//8)se verifica si viene el tipo de asocio para filtar por el.
		if (criterios.containsKey(indices[12]) && !UtilidadTexto.isEmpty(criterios.get(indices[12])+"") && !(criterios.get(indices[12])+"").equals(ConstantesBD.codigoNuncaValido+""))
			where +=" AND detexqxcc.tipo_asocio="+criterios.get(indices[12]);
		
		//9)se verifica si viene el tipo de cirugia para filtar por el.
		if (criterios.containsKey(indices[10]) && !UtilidadTexto.isEmpty(criterios.get(indices[10])+"") && !(criterios.get(indices[10])+"").equals(ConstantesBD.codigoNuncaValido+""))
			where +=" AND detexqxcc.tipo_cirugia='"+criterios.get(indices[10])+"'";
		
		//10)se verifica si viene continua medico para filtar por el.
		if (criterios.containsKey(indices[17]) && !UtilidadTexto.isEmpty(criterios.get(indices[17])+"") && !(criterios.get(indices[17])+"").equals(ConstantesBD.codigoNuncaValido+""))
			where +=" AND detexqxcc.continua_medico='"+criterios.get(indices[17])+"'";
		
		//11)se verifica si viene continua via de acceso para filtar por el.
		if (criterios.containsKey(indices[18]) && !UtilidadTexto.isEmpty(criterios.get(indices[18])+"") && !(criterios.get(indices[18])+"").equals(ConstantesBD.codigoNuncaValido+""))
			where +=" AND detexqxcc.continua_via_acceso='"+criterios.get(indices[18])+"'";
		
		//12)se verifica si viene el tipo de liquidacion para filtar por el.
		if (criterios.containsKey(indices[11]) && !UtilidadTexto.isEmpty(criterios.get(indices[11])+"") && !(criterios.get(indices[11])+"").equals(ConstantesBD.codigoNuncaValido+""))
			where +=" AND detexqxcc.tipo_liquidacion="+criterios.get(indices[11]);
		
		//13)se verifica si viene el valor para filtar por el.
		if (criterios.containsKey(indices[19]) && !UtilidadTexto.isEmpty(criterios.get(indices[19])+"") && !(criterios.get(indices[19])+"").equals(ConstantesBD.codigoNuncaValido+""))
			where +=" AND detexqxcc.valor="+criterios.get(indices[19]);
		
		//14)se verifica si viene el codigo de excepcion qx
		if (criterios.containsKey(indices[6]) && !UtilidadTexto.isEmpty(criterios.get(indices[6])+"") && !(criterios.get(indices[6])+"").equals(ConstantesBD.codigoNuncaValido+""))
			where +=" AND exqxcc.codigo_excepcion_qx="+criterios.get(indices[6]);
		
		//15)se verifica si viene el codigo de la via de ingreso
		if (criterios.containsKey(indices[26]) && !UtilidadTexto.isEmpty(criterios.get(indices[26])+"") && !(criterios.get(indices[26])+"").equals(ConstantesBD.codigoNuncaValido+""))
			where +=" AND exqxcc.via_ingreso="+criterios.get(indices[26]);
		
		//16)se verifica si viene el tipo paciente
		if (criterios.containsKey(indices[27]) && !UtilidadTexto.isEmpty(criterios.get(indices[27])+"") && !(criterios.get(indices[27])+"").equals(ConstantesBD.codigoNuncaValido+""))
			where +=" AND exqxcc.tipo_paciente='"+criterios.get(indices[27])+"'";
		
		cadena+=where;
		
		logger.info("\n cadena de busqueda "+cadena);
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					
			//se carga el mapa con los datos de la consulta
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()),true,true);

		}
		catch (SQLException e) 
		{
			logger.info("\n Problema Buscando los datos en la tabla det_excepciones_qx_cc "+e);
		}
		
		//se cargan los indices al mapa.
		mapa.put("INDICES_MAPA", indices);
		
	
		return mapa;
	}
	
	/*-----------------------------------------
	 *  Fin Metodos para las Busquedas de datos
	 ------------------------------------------*/
	
	
		/*-----------------------------
		 * Metodos para insertar datos
		 -----------------------------*/
	
	
	
	/**
	 *Metodo encargado de elegir donde seran insertados los datos
	 *ya sea en la tabla excepciones_qx_cc o en la tabla
	 *det_excepciones_qx_cc.
	 *@connection
	 *@datos
	 *-----------------------
	 *KEY'S DEL HASHMAP DATOS
	 *-----------------------
	 * -- codigoExcepcionQx6_ -->Requerido
	 * -- centroCosto7_ --> Requerido
	 * -- codigoExcepcionQxCc8_ --> Requerido
	 * -- tipoServicio9_ --> Opcional
	 * -- tipoCirugia10_ --> Requerido
	 * -- tipoLiquidacion11_ --> Requerido
	 * -- tipoAsocio12_ --> Requerido
	 * -- servicio13_ --> Opcional
	 * -- liquidarSobreAsocio14_ --> Opcional
	 * -- especialidad15_ --> Opcional
	 * -- grupoServicio16_ --> Opcional
	 * -- continuaMedico17_ --> Opcional
	 * -- continuaViaAcceso18_ --> Opcional
	 * -- valor19_ --> Requerido
	 * -- usuarioModifica5_ --> Requerido
	 * -- tipoSala21_
	 * 
	 */
	public static int insertarGeneral (Connection connection,HashMap datos)
	{
		logger.info("\n\n:::::::::ENTRO A INSERTAR GENERAL ::::::" +datos);
		
		try
		{	//1) sevalida si trae el codigo excepcionQxCc
			if (datos.containsKey(indices[8]) && !UtilidadTexto.isEmpty(datos.get(indices[8])+"") && !(datos.get(indices[8])+"").equals(ConstantesBD.codigoNuncaValido+""))
			{
				if(insetar2(connection, datos))
					return Utilidades.convertirAEntero(datos.get(indices[8])+"");
			}
			else
				if (insetar1(connection, datos))
					{
						int secuencia=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).obtenerUltimoValorSecuencia(connection, "seq_excepciones_qx_cc");
						datos.put(indices[8], secuencia);
						if(insetar2(connection, datos))
							return Utilidades.convertirAEntero(datos.get(indices[8])+"");
					}
			
		}
		catch (SQLException e)
		{
		 logger.info("\n Problema en insertar General "+e);
		}
		
		return ConstantesBD.codigoNuncaValido;
	}
	
	
	
	/**
	 * Metodo encargado de insertar los datos
	 * en la tabla excepciones_qx.
	 * Parametros: 
	 * @connection
	 * @datos
	 * --------------------------
	 * KEY'S DEL HASHMAP DATOS
	 * --------------------------
	 * -- convenio1_ --> Requerido
	 * -- fechaInicial2_ --> Requerido
	 * -- fechaFinal3_ --> Requerido
	 * -- institucion4_ --> Requerido
	 * -- usuarioModifica5_ --> Requerido
	 * -- contrato23_ --> Requerido
	 * @return boolean
	 * --true --> la operacion re realizo correctamente
	 * --false --> la operacion se aborto.				
	 */
	public static boolean insetar0 (Connection connection, HashMap datos)
	{
		logger.info("\n\n***********************************************");
		logger.info("\n ::::::ENTRE A INSERTAR0 SQL::::::::");
		logger.info("\n hashmap al entrar ==> "+datos);
		logger.info("\n ***********************************************");
		
	
		String cadena = strCadenaInsercion0;
				
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena , ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			//codigo
			int codigo = UtilidadBD.obtenerSiguienteValorSecuencia(connection, "seq_exc_qx");
			
			//1)se agrega el convenio
			ps.setObject(1, datos.get(indices[1]));
			//2)se agrega la fecha inicial.
			ps.setObject(2, UtilidadFecha.conversionFormatoFechaABD(datos.get(indices[2])+""));
			//3)se agrega la fecha final.
			ps.setObject(3, UtilidadFecha.conversionFormatoFechaABD(datos.get(indices[3])+""));
			//4)se agrega la institucion
			ps.setObject(4, datos.get(indices[4]));
			//5)se agrega el usuario que modifica
			ps.setObject(5, datos.get(indices[5]));
			//6)se agrega la feha modifica
			ps.setObject(6, UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
			//7)se agrega la hora modifica
			ps.setObject(7, UtilidadFecha.getHoraActual());
			//8)se agrega el contrato
			ps.setObject(8, datos.get(indices[23]));
			//codigo
			ps.setObject(9, codigo);
			
			
			if (ps.executeUpdate()>0)
				return true;

			
		} 
		catch (SQLException e)
		{
			logger.info("\n Problema insertando los datos en la tabla excepciones_qx "+e);
		}
		
		
		return false;
	}
	
	
	/**
	 * Metodo encargado de insertar los datos en 
	 * la tabla excepciones_qx_cc
	 * @param connection
	 * @param datos
	 * ----------------------------------
	 * KEY'S DEL HASHMAP DATOS
	 * ----------------------------------
	 * -- codigoExcepcionQx6_
	 * -- centroCosto7_
	 * -- usuarioModifica5_
	 * @return
	 * --true --> la operacion re realizo correctamente
	 * --false --> la operacion se aborto.				
	 */
	public static boolean insetar1 (Connection connection, HashMap datos)
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
			int codigo = UtilidadBD.obtenerSiguienteValorSecuencia(connection, "seq_excepciones_qx_cc");
			
			//1)se agrega el codigo de excepciones_qx
			ps.setObject(1, datos.get(indices[6]));
			
			//2)se agrega el Centro de costo
			if (datos.containsKey(indices[7]) && !UtilidadTexto.isEmpty(datos.get(indices[7])+"") && !(datos.get(indices[7])+"").equals(ConstantesBD.codigoNuncaValido+""))
				ps.setObject(2, datos.get(indices[7]));
			else
				ps.setNull(2, Types.NUMERIC);
			
			//3)se agrega el usuario que modifica
			ps.setObject(3, datos.get(indices[5]));
			
			//4)se agrega la feha modifica
			ps.setObject(4, UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
			
			//5)se agrega la hora modifica
			ps.setObject(5, UtilidadFecha.getHoraActual());
			
			//6) codigo
			ps.setObject(6, codigo);
			
			//7)Via Ingreso
			if (datos.containsKey(indices[26]) && !UtilidadTexto.isEmpty(datos.get(indices[26])+"") && !(datos.get(indices[26])+"").equals(ConstantesBD.codigoNuncaValido+""))
				ps.setObject(7, datos.get(indices[26]));
			else
				ps.setNull(7, Types.INTEGER);
			
			//8)Tipo Paciente
			if (datos.containsKey(indices[27]) && !UtilidadTexto.isEmpty(datos.get(indices[27])+"") && !(datos.get(indices[27])+"").equals(ConstantesBD.codigoNuncaValido+""))
				ps.setObject(8, datos.get(indices[27]));
			else
				ps.setNull(8, Types.CHAR);
			
			if (ps.executeUpdate()>0)
				return true;
			
		}
		catch (SQLException e)
		{
			logger.info("\n Problema insertando los datos en la tabla excepciones_qx_cc "+e);
		}
		
		return false;
	}
	
	
	/**
	 * Metodo encargado de insertar los datos de
	 * la tabla (det_excepciones_qx_cc) 
	 * @param connection
	 * @param datos
	 * --------------------------------
	 * KEY'S DEL HASHMAP DATOS
	 * --------------------------------
	 * -- codigoExcepcionQxCc8_ --> Requerido
	 * -- tipoServicio9_ --> Opcional
	 * -- tipoCirugia10_ --> Requerido
	 * -- tipoLiquidacion11_ --> Requerido
	 * -- tipoAsocio12_ --> Requerido
	 * -- servicio13_ --> Opcional
	 * -- liquidarSobreAsocio14_ --> Opcional
	 * -- especialidad15_ --> Opcional
	 * -- grupoServicio16_ --> Opcional
	 * -- continuaMedico17_ --> Opcional
	 * -- continuaViaAcceso18_ --> Opcional
	 * -- valor19_ --> Requerido
	 * -- usuarioModifica5_ --> Requerido
	 * -- tipoSala21_
	 * @return
	 * --true --> la operacion re realizo correctamente
	 * --false --> la operacion se aborto.	
	 */
	public static boolean insetar2 (Connection connection, HashMap datos)
	{
		logger.info("\n\n***********************************************");
		logger.info("\n ::::::ENTRE A INSERTAR2 SQL::::::::");
		logger.info("\n hashmap al entrar ==> "+datos);
		logger.info("\n ***********************************************");

		String cadena = strCadenaInsercion2;
		
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			//codigo
			int codigo = UtilidadBD.obtenerSiguienteValorSecuencia(connection, "seq_det_excepciones_qx_cc");
			
			//1)se agrega el valor del codigo de la tabla ex_cepcion_qx_cc
			ps.setObject(1, datos.get(indices[8]));
			
			//2)se agrega el tipo de servicio
			if (datos.containsKey(indices[9]) && !UtilidadTexto.isEmpty(datos.get(indices[9])+"") && !(datos.get(indices[9])+"").equals(ConstantesBD.codigoNuncaValido+""))
				ps.setObject(2, datos.get(indices[9]));
			else
				ps.setNull(2, Types.VARCHAR);
			
			//3) se agrega el valor de tipo de cirugia
			if (datos.containsKey(indices[10]) && !UtilidadTexto.isEmpty(datos.get(indices[10])+"") && !(datos.get(indices[10])+"").equals(ConstantesBD.codigoNuncaValido+""))
				ps.setObject(3, datos.get(indices[10]));
			else
				ps.setNull(3, Types.VARCHAR);
			
			//4) se agrega el valor de tipo de liquidacion
			ps.setObject(4, datos.get(indices[11]));
						
			//5) se agrega el valor de tipo de asocio
			ps.setObject(5, datos.get(indices[12]));
			
			//6)se agrega valor de servicio
			if (datos.containsKey(indices[13]) && !UtilidadTexto.isEmpty(datos.get(indices[13])+"") && !(datos.get(indices[13])+"").equals(ConstantesBD.codigoNuncaValido+""))
				ps.setObject(6, datos.get(indices[13]));
			else
				ps.setNull(6, Types.NUMERIC);
			
			//7)se agrega valor de liquidar sobre asocio
			if (datos.containsKey(indices[14]) && !UtilidadTexto.isEmpty(datos.get(indices[14])+"") && !(datos.get(indices[14])+"").equals(ConstantesBD.codigoNuncaValido+""))
				ps.setObject(7, datos.get(indices[14]));
			else
				ps.setNull(7, Types.VARCHAR);
			
			//8)se agrega valor de la especialidad
			if (datos.containsKey(indices[15]) && !UtilidadTexto.isEmpty(datos.get(indices[15])+"") && !(datos.get(indices[15])+"").equals(ConstantesBD.codigoNuncaValido+""))
				ps.setObject(8, datos.get(indices[15]));
			else
				ps.setNull(8, Types.NUMERIC);
			
			//9)se agrega valor del grupo de servicio
			if (datos.containsKey(indices[16]) && !UtilidadTexto.isEmpty(datos.get(indices[16])+"") && !(datos.get(indices[16])+"").equals(ConstantesBD.codigoNuncaValido+""))
				ps.setObject(9, datos.get(indices[16]));
			else
				ps.setNull(9, Types.NUMERIC);
			
			//10)se agrega valor de continua medico
			if (datos.containsKey(indices[17]) && !UtilidadTexto.isEmpty(datos.get(indices[17])+"") && !(datos.get(indices[17])+"").equals(ConstantesBD.codigoNuncaValido+""))
				ps.setObject(10, datos.get(indices[17]));
			else
				ps.setNull(10, Types.VARCHAR);
						
			//11)se agrega valor de continua via acceso
			if (datos.containsKey(indices[18]) && !UtilidadTexto.isEmpty(datos.get(indices[18])+"") && !(datos.get(indices[18])+"").equals(ConstantesBD.codigoNuncaValido+""))
				ps.setObject(11, datos.get(indices[18]));
			else
				ps.setNull(11, Types.VARCHAR);
			
			//12) se agrega el valor de valor
			ps.setObject(12, datos.get(indices[19]));
			
			//13) se agrega el valor del usuario que modifica
			ps.setObject(13, datos.get(indices[5]));
			
			//14)se agrega la feha modifica
			ps.setObject(14, UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
			
			//15)se agrega la hora modifica
			ps.setObject(15, UtilidadFecha.getHoraActual());
			
			//16)se agrega valor tipo de sala
			if (datos.containsKey(indices[21]) && !UtilidadTexto.isEmpty(datos.get(indices[21])+"") && !(datos.get(indices[21])+"").equals(ConstantesBD.codigoNuncaValido+""))
				ps.setObject(16, datos.get(indices[21]));
			else
				ps.setNull(16, Types.NUMERIC);
			
			//17)codigo
			ps.setObject(17, codigo);
			
			logger.info("===>Aplicar Liq. % Cx. Múlt: "+datos.get(indices[28]));
			//18)Se agrega el campo Liq. % Cx. Múlt.
			if (datos.containsKey(indices[28]) && !UtilidadTexto.isEmpty(datos.get(indices[28])+"") && !(datos.get(indices[28])+"").equals(ConstantesBD.codigoNuncaValido+""))
				ps.setObject(18, datos.get(indices[28]));
			else
				ps.setNull(18, Types.CHAR);
			
			if (ps.executeUpdate()>0)
				return true;
			
		}
		catch (SQLException e) 
		{
			logger.info("\n Problema insertando los datos en la tabla det_excepciones_qx_cc "+e);
		}
		
		
		return false;
	
	}
	
		/*--------------------------------
		 *Fin Metodos para insertar datos
		 --------------------------------*/
	
		/*-------------------------------
		 * Metodos para eliminar datos
		 -------------------------------*/
	
	
	
	/**
	 * Metodo encargado de eliminar los datos
	 * de la tabla  (excepciones_qx)
	 * @connection
	 * @datos
	 * ------------------------------------
	 * KEY'S DEL HASHMAP DATOS
	 * ------------------------------------
	 * -- codigoExcepcionQx6_ --> Requerido
	 * -- institucion4_ --> Requerido
	 * @return
	 * --true --> la operacion re realizo correctamente
	 * --false --> la operacion se aborto.	
	 */
	public static boolean eliminar0 (Connection connection,HashMap datos)
	{

		logger.info("\n\n***********************************************");
		logger.info("\n ::::::ENTRE A ELIMINAR0 SQL::::::::");
		logger.info("\n hashmap al entrar ==> "+datos);
		logger.info("\n ***********************************************");
		
		String cadena = strCadenaEliminacion0;
		
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			//1) se agrega el valor del codigo de la tabla excepciones_qx para filtar por el.
			ps.setObject(1, datos.get(indices[6]));
			
			//2) se agrega el valor del codigo de la  insitucionpara filtar por el.
			ps.setObject(2, datos.get(indices[4]));
			
			if (ps.executeUpdate()>0)
				return true;
		}
		catch (SQLException e)
		{
			logger.info("\n Problema eliminando los datos en la tabla excepciones_qx "+e);
		}
		
		
		return false;
	}
	
	/**
	 * Metodo encargado de identificar en que
	 * parte de la BD eliminar los datos
	 * @param connection
	 * @param datos
	 * 
	 * -- codigoExcepcionQxCc8_ --> Requerido
	 * -- detCodigoExcepcionQxCc0_ --> Requerido
	 * @return
	 */
	public static boolean eliminarGeneral (Connection connection, HashMap datos)
	{
		
		int cant =0;
		cant=UtilidadBD.estaUtilizadoEnTabla(connection, "det_excepciones_qx_cc", "codigo_excepcion_qx_cc", Integer.parseInt(datos.get(indices[8])+""));
		
		if (cant>1)
			return eliminar2(connection, datos);
		else
			if (eliminar2(connection, datos))
				return eliminar1(connection, datos);
		
		return false;
		
	}
	
	
	/**
	 * Metodo encargado de eliminar los datos
	 * de la tabla  (excepciones_qx_cc)
	 * @connection
	 * @datos
	 * ------------------------------------
	 * KEY'S DEL HASHMAP DATOS
	 * ------------------------------------
	 * -- codigoExcepcionQxCc8_ --> Requerido
	 * @return
	 * --true --> la operacion re realizo correctamente
	 * --false --> la operacion se aborto.	
	 */
	public static boolean eliminar1 (Connection connection,HashMap datos)
	{

		logger.info("\n\n***********************************************");
		logger.info("\n ::::::ENTRE A ELIMINAR1 SQL::::::::");
		logger.info("\n hashmap al entrar ==> "+datos);
		logger.info("\n ***********************************************");
		
		String cadena = strCadenaEliminacion1;
		
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			//1) se agrega el valor del codigo de la tabla excepciones_qx_cc para filtar por el.
			ps.setObject(1, datos.get(indices[8]));
			
			if (ps.executeUpdate()>0)
				return true;
		}
		catch (SQLException e)
		{
			logger.info("\n Problema eliminando los datos en la tabla excepciones_qx_cc "+e);
		}
		
		
		return false;
	}
	
	
	/**
	 * Metodo encargado de eliminar los datos
	 * de la tabla  (det_excepciones_qx_cc)
	 * @connection
	 * @datos
	 * ------------------------------------
	 * KEY'S DEL HASHMAP DATOS
	 * ------------------------------------
	 * -- detCodigoExcepcionQxCc0_ --> Requerido
	 * @return
	 * --true --> la operacion re realizo correctamente
	 * --false --> la operacion se aborto.	
	 */
	public static boolean eliminar2 (Connection connection,HashMap datos)
	{

		logger.info("\n\n***********************************************");
		logger.info("\n ::::::ENTRE A ELIMINAR2 SQL::::::::");
		logger.info("\n hashmap al entrar ==> "+datos);
		logger.info("\n ***********************************************");
		
		String cadena = strCadenaEliminacion2;
		
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			//1) se agrega el valor del codigo de la tabla det_excepciones_qx_cc para filtar por el.
			ps.setObject(1, datos.get(indices[0]));
			
			if (ps.executeUpdate()>0)
				return true;
		}
		catch (SQLException e)
		{
			logger.info("\n Problema eliminando los datos en la tabla det_excepciones_qx_cc "+e);
		}
		
		
		return false;
	}
	
	
		/*---------------------------------
		 * Fin Metodos para eliminar datos
	 	 ---------------------------------*/
	
	
		/*-------------------------	
		 * Metodos para Modificar
		 -------------------------*/
	
	/**
	 * Metodo encargado de modificar los datos
	 * de la tabla excepciones_qx
	 * @connection
	 * @datos
	 * -------------------------------
	 * KEY'S DEL HASHMAP DATOS
	 * -------------------------------
	 * -- fechaInicial2_ --> Requiere
	 * -- fechaFinal3_ --> Requiere
	 * -- usuarioModifica5_ --> Requiere
	 * -- ExcepcionQx6_ --> Requiere
	 * -- institucion4_ --> Requiere
	 *  * @return
	 * --true --> la operacion re realizo correctamente
	 * --false --> la operacion se aborto.	
	 */
	public static boolean modificar0 (Connection connection, HashMap datos)
	{
		logger.info("\n\n***********************************************");
		logger.info("\n ::::::ENTRE A MODIFICAR0 SQL::::::::");
		logger.info("\n hashmap al entrar ==> "+datos);
		logger.info("\n ***********************************************");
		
	
		String cadena = strCadenaModificacion0;
				
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena , ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			
			//1)se agrega la fecha inicial.
			ps.setObject(1, UtilidadFecha.conversionFormatoFechaABD(datos.get(indices[2])+""));
			//2)se agrega la fecha final.
			ps.setObject(2, UtilidadFecha.conversionFormatoFechaABD(datos.get(indices[3])+""));
			//3)se agrega el usuario que modifica
			ps.setObject(3, datos.get(indices[5]));
			//4)se agrega la feha modifica
			ps.setObject(4, UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
			//5)se agrega la hora modifica
			ps.setObject(5, UtilidadFecha.getHoraActual());
			
			//6)se agrega el codigo de la tabla excepciones_qx para filtrar por el.
			ps.setObject(6, datos.get(indices[6]));
			//7)se agrega la institucion
			ps.setObject(7, datos.get(indices[4]));
			
			if (ps.executeUpdate()>0)
				return true;

			
		} 
		catch (SQLException e)
		{
			logger.info("\n Problema insertando los datos en la tabla excepciones_qx "+e);
		}
		
		
		
		return false;
	}
	
	
	
	/**
	 * Metodo encargado de modificar los datos de
	 * la tabla (det_excepciones_qx_cc) 
	 * @param connection
	 * @param datos
	 * --------------------------------
	 * KEY'S DEL HASHMAP DATOS
	 * --------------------------------
	 * -- tipoServicio9_ --> Opcional
	 * -- tipoCirugia10_ --> Requerido
	 * -- tipoLiquidacion11_ --> Requerido
	 * -- tipoAsocio12_ --> Requerido
	 * -- servicio13_ --> Opcional
	 * -- liquidarSobreAsocio14_ --> Opcional
	 * -- especialidad15_ --> Opcional
	 * -- grupoServicio16_ --> Opcional
	 * -- continuaMedico17_ --> Opcional
	 * -- continuaViaAcceso18_ --> Opcional
	 * -- valor19_ --> Requerido
	 * -- usuarioModifica5_ --> Requerido
	 * -- detCodigoExcepcionQxCc0_ --> Requerido
	 * -- tipoSala21_ --> Opcional
	 * @return
	 * --true --> la operacion re realizo correctamente
	 * --false --> la operacion se aborto.	
	 */
	public static boolean modificar1 (Connection connection, HashMap datos)
	{
		logger.info("\n\n***********************************************");
		logger.info("\n ::::::ENTRE A MODIFICAR1 SQL::::::::");
		logger.info("\n hashmap al entrar ==> "+datos);
		logger.info("\n ***********************************************");

		String cadena = strCadenaModificacion1;
		
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			//1)se agrega el tipo de servicio
			if (datos.containsKey(indices[9]) && !UtilidadTexto.isEmpty(datos.get(indices[9])+"") && !(datos.get(indices[9])+"").equals(ConstantesBD.codigoNuncaValido+""))
				ps.setObject(1, datos.get(indices[9]));
			else
				ps.setNull(1, Types.VARCHAR);
			
			//2) se agrega el valor de tipo de cirugia
			if (datos.containsKey(indices[10]) && !UtilidadTexto.isEmpty(datos.get(indices[10])+"") && !(datos.get(indices[10])+"").equals(ConstantesBD.codigoNuncaValido+""))
				ps.setObject(2, datos.get(indices[10]));
			else
				ps.setNull(2, Types.VARCHAR);
			
			//3) se agrega el valor de tipo de liquidacion
			ps.setObject(3, datos.get(indices[11]));
						
			//4) se agrega el valor de tipo de asocio
			ps.setObject(4, datos.get(indices[12]));
			
			//5)se agrega valor de servicio
			if (datos.containsKey(indices[13]) && !UtilidadTexto.isEmpty(datos.get(indices[13])+"") && !(datos.get(indices[13])+"").equals(ConstantesBD.codigoNuncaValido+""))
				ps.setObject(5, datos.get(indices[13]));
			else
				ps.setNull(5, Types.NUMERIC);
			
			//6)se agrega valor de liquidar sobre asocio
			if (datos.containsKey(indices[14]) && !UtilidadTexto.isEmpty(datos.get(indices[14])+"") && !(datos.get(indices[14])+"").equals(ConstantesBD.codigoNuncaValido+""))
				ps.setObject(6, datos.get(indices[14]));
			else
				ps.setNull(6, Types.VARCHAR);
			
			//7)se agrega valor de la especialidad
			if (datos.containsKey(indices[15]) && !UtilidadTexto.isEmpty(datos.get(indices[15])+"") && !(datos.get(indices[15])+"").equals(ConstantesBD.codigoNuncaValido+""))
				ps.setObject(7, datos.get(indices[15]));
			else
				ps.setNull(7, Types.NUMERIC);
			
			//8)se agrega valor del grupo de servicio
			if (datos.containsKey(indices[16]) && !UtilidadTexto.isEmpty(datos.get(indices[16])+"") && !(datos.get(indices[16])+"").equals(ConstantesBD.codigoNuncaValido+""))
				ps.setObject(8, datos.get(indices[16]));
			else
				ps.setNull(8, Types.NUMERIC);
			
			//9)se agrega valor de continua medico
			if (datos.containsKey(indices[17]) && !UtilidadTexto.isEmpty(datos.get(indices[17])+"") && !(datos.get(indices[17])+"").equals(ConstantesBD.codigoNuncaValido+""))
				ps.setObject(9, datos.get(indices[17]));
			else
				ps.setNull(9, Types.VARCHAR);
						
			//10)se agrega valor de continua via acceso
			if (datos.containsKey(indices[18]) && !UtilidadTexto.isEmpty(datos.get(indices[18])+"") && !(datos.get(indices[18])+"").equals(ConstantesBD.codigoNuncaValido+""))
				ps.setObject(10, datos.get(indices[18]));
			else
				ps.setNull(10, Types.VARCHAR);
			
			//11) se agrega el valor de valor
			ps.setObject(11, datos.get(indices[19]));
			
			//12) se agrega el valor del usuario que modifica
			ps.setObject(12, datos.get(indices[5]));
			
			//13)se agrega la feha modifica
			ps.setObject(13, UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
			//14)se agrega la hora modifica
			ps.setObject(14, UtilidadFecha.getHoraActual());
			
			//15)se agrega valor tipo de sala
			if (datos.containsKey(indices[21]) && !UtilidadTexto.isEmpty(datos.get(indices[21])+"") && !(datos.get(indices[21])+"").equals(ConstantesBD.codigoNuncaValido+""))
				ps.setObject(15, datos.get(indices[21]));
			else
				ps.setNull(15, Types.NUMERIC);
			
			logger.info("===>Aplicar Liq. % Cx. Múlt: "+datos.get(indices[28]));
			//16)Se agrega el campo Liq. % Cx. Múlt.
			if (datos.containsKey(indices[28]) && !UtilidadTexto.isEmpty(datos.get(indices[28])+"") && !(datos.get(indices[28])+"").equals(ConstantesBD.codigoNuncaValido+""))
				ps.setObject(16, datos.get(indices[28]));
			else
				ps.setNull(16, Types.CHAR);
			
			//17)se agrega el valor del codigo de la tabla det_excepciones_qx_cc para filtrar por el.
			ps.setObject(17, datos.get(indices[0]));
			
			if (ps.executeUpdate()>0)
				return true;
			
		}
		catch (SQLException e) 
		{
			logger.info("\n Problema insertando los datos en la tabla det_excepciones_qx_cc "+e);
		}
		
		
		return false;
	
	}
	
		/*-------------------------	
		 * Fin Metodos para Modificar
		 -------------------------*/
	
	
	/*-------------------------------------------
	 * Fin Metodos modificados
	 -------------------------------------------*/
	
	
}
