/*
 * Marzo 16, 2006 
 */
package com.princetonsa.dao.sqlbase.salasCirugia;

import java.sql.Connection;
import java.sql.Date;
import com.princetonsa.decorator.PreparedStatementDecorator;
import java.sql.SQLException;

import java.sql.Types;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.princetonsa.decorator.ResultSetDecorator;

import util.ConstantesBD;
import util.UtilidadBD;
import util.Utilidades;

/**
 * @author Sebastián Gómez
 * @author Modificado Nov 2007 Jose Eduardo Arias Doncel 
 * 
 * Objeto usado para manejar los accesos comunes a la fuente de datos
 * para la parametrización de Excepciones Tarifas Asocios
 */

public class SqlBaseExTarifasAsociosDao 
{
	/**
	 * Manejador de logs de la clase
	 */
	private static Logger logger=Logger.getLogger(SqlBaseExTarifasAsociosDao.class);
	
	
	//**********************Cadenas del Encabezado
	/**
	 * Consulta la tabla encabezado
	 * */
	private static final String strConsultarEncabezado = "SELECT " +
			"e.codigo," +
			"e.institucion," +
			"e.convenio," +
			"getnombreconvenio(e.convenio) AS nombre_convenio," +
			"to_char(e.fecha_inicial,'DD/MM/YYYY') AS fecha_inicial," +
			"to_char(e.fecha_final,'DD/MM/YYYY') AS fecha_final," +
			"(SELECT COUNT(m.codigo) FROM ex_tarifas_asocios_xvitpcc m WHERE m.codigo_encab = e.codigo ) AS cuanto_detalle, " +
			"'"+ConstantesBD.acronimoSi+"' AS estabd " +
			"FROM ex_tarifas_asocios e " +
			"WHERE e.institucion = ? ";
	
	
	/**
	 * Modifica la tabla encabezado
	 * */
	private static final String strModificarEncabezado = "UPDATE " +
			"ex_tarifas_asocios " +
			"SET " +
			"fecha_inicial = ?," +
			"fecha_final = ?," +
			"usuario_modifica = ?," +
			"fecha_modifica = ?," +
			"hora_modifica = ? " +
			"WHERE " +
			"codigo = ? AND " +
			"institucion = ? ";	
	
	
	/**
	 * Eliminar Tabla encabezado
	 * */
	private static final String strEliminarEncabezado = "DELETE FROM ex_tarifas_asocios WHERE codigo = ? AND institucion = ? ";
	
	//**************************************************
	
	
	//*********************Cadenas de la Tabla Media 
	
	/**
	 * Consulta la informacion de la tabla media
	 * */
	private static final String strConsultarMedia = "SELECT " +
			"m.codigo," +
			"m.codigo_encab," +
			"CASE WHEN m.via_ingreso IS NULL THEN "+ConstantesBD.codigoNuncaValido+" ELSE m.via_ingreso END AS via_ingreso, " +
			"getnombreviaingreso(m.via_ingreso) AS nombre_via_ingreso," +
			"CASE WHEN m.tipo_paciente IS NULL THEN "+ConstantesBD.codigoNuncaValido+" || '' ELSE m.tipo_paciente || '' END AS tipo_paciente, " +			
			"getnombretipopaciente(m.tipo_paciente) AS nombre_tipo_paciente," +
			"CASE WHEN m.centro_costo IS NULL THEN "+ConstantesBD.codigoNuncaValido+" ELSE m.centro_costo END AS centro_costo, " +			
			"m.centro_costo," +
			"cc.nombre AS nombre_ccosto, " +
			"(SELECT COUNT(d.codigo) FROM det_ex_tarifas_asocios d WHERE d.codigo_encab_xvit = m.codigo) AS cuanto_detalle," +
			"'"+ConstantesBD.acronimoSi+"' AS estabd " +
			"FROM ex_tarifas_asocios_xvitpcc m " +
			"LEFT OUTER JOIN centros_costo cc ON(m.centro_costo = cc.codigo) " +
			"WHERE m.codigo_encab = ? ";
	
	
	/**
	 * Modificacion de la Tabla Media
	 * */
	private static final String strModificarMedia = "UPDATE " +
			"ex_tarifas_asocios_xvitpcc " +
			"SET " +
			"via_ingreso = ? ," +
			"tipo_paciente = ?," +
			"centro_costo = ?," +
			"usuario_modifica = ?," +
			"fecha_modifica = ?," +
			"hora_modifica = ? " +
			"WHERE " +
			"codigo = ? AND codigo_encab = ? ";
	
	
	/**
	 * eliminar Tabla Media
	 * */
	private static final String strEliminarMedia =" DELETE FROM ex_tarifas_asocios_xvitpcc WHERE codigo = ? AND codigo_encab = ? ";
	
	//*************************************************
	
	
	//***********************Cadenas de la Tabla Detalle
	
	/**
	 * Consulta de la informacion del detalle 
	 * */
	private static final String strConsultaDetalle = "SELECT " +
			"d.codigo," +
			"d.codigo_encab_xvit," +
			"CASE WHEN d.tipo_servicio IS NULL THEN "+ConstantesBD.codigoNuncaValido+" || '' ELSE d.tipo_servicio || '' END AS tipo_servicio," +
			"getnombretiposervicio(d.tipo_servicio) AS nombre_tipo_servicio," +
			"CASE WHEN d.especialidad IS NULL THEN "+ConstantesBD.codigoNuncaValido+" ELSE d.especialidad END AS especialidad, " +
			"getnombreespecialidad(d.especialidad) AS nombre_especialidad," +
			"CASE WHEN d.grupo_servicio IS NULL THEN "+ConstantesBD.codigoNuncaValido+" ELSE d.grupo_servicio END AS grupo_servicio," +
			"getnombregruposervicio(d.grupo_servicio) AS nombre_grupo_servicio," +
			"CASE WHEN d.servicio IS NULL THEN "+ConstantesBD.codigoNuncaValido+" ELSE d.servicio END AS servicio," +
			"getcodigoespecialidad(d.servicio) || ' - ' || d.servicio || ' ' || getnombreservicio(d.servicio,"+ConstantesBD.codigoTarifarioCups+") AS nombreServicio," +
			"CASE WHEN d.tipo_cirugia IS NULL THEN "+ConstantesBD.codigoNuncaValido+" || '' ELSE d.tipo_cirugia || '' END AS tipo_cirugia," +
			"tc.nombre AS nombre_tipo_cirugia," +
			"CASE WHEN d.asocio IS NULL THEN "+ConstantesBD.codigoNuncaValido+" ELSE d.asocio END AS asocio," +
			"getnombretipoasocio(d.asocio) AS nombre_asocio," +
			"d.rango_inicial," +
			"d.rango_final," +
			"d.porcentaje," +
			"d.valor," +			
			"d.tipo_excepcion," +
			"'"+ConstantesBD.acronimoSi+"' AS estabd " +
			"FROM " +
			"det_ex_tarifas_asocios d " +
			"LEFT OUTER JOIN tipos_cirugia tc ON (tc.acronimo = d.tipo_cirugia) " +
			"WHERE d.codigo_encab_xvit = ? ";	
	

	/**
	 * Modificacion de la informacion del detalle
	 * */
	private static final String strModificacionDetalle=" UPDATE " +
			"det_ex_tarifas_asocios " +
			"SET " +
			"tipo_servicio = ?," +
			"especialidad = ?," +
			"grupo_servicio = ?," +
			"servicio = ?," +
			"tipo_cirugia =?," +
			"asocio = ?," +
			"rango_inicial = ?," +
			"rango_final = ?," +
			"porcentaje= ?," +
			"valor = ?," +
			"tipo_excepcion = ?," +
			"usuario_modifica = ?," +
			"fecha_modifica = ?," +
			"hora_modifica = ? " +
			"WHERE " +
			"codigo = ? AND " +
			"codigo_encab_xvit = ? ";
	
	
	/**
	 * Eliminar la informacion del detalle
	 * */
	private static final String strEliminarDetalle= "DELETE FROM det_ex_tarifas_asocios WHERE codigo = ? AND codigo_encab_xvit = ? ";
	
	
	//***************************************************************
	
	//****************Metodos*****************************************
	/**
	 * Insertar informacion tabla encabezado
	 * @param Connection con
	 * @param HashMap parametros
	 * @param String strInsertarEncabezado
	 * @return 
	 * */	
	public static boolean insertarEncabezado(Connection con, HashMap parametros,String strInsertarEncabezado)
	{
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(strInsertarEncabezado,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));			
			
			/**
			 * INSERT INTO ex_tarifas_asocios(
			 * codigo,
			 * convenio,
			 * institucion,
			 * fecha_inicial,
			 * fecha_final,
			 * usuario_modifica,
			 * fecha_modifica,
			 * hora_modifica) VALUES('seq_ex_tarifas_asocios'),?,?,?,?,?,?,?)
			 */
			
			ps.setInt(1,Utilidades.convertirAEntero(parametros.get("convenio")+""));
			ps.setInt(2,Utilidades.convertirAEntero(parametros.get("institucion")+""));
			ps.setDate(3,Date.valueOf(parametros.get("fecha_inicial")+""));
			ps.setDate(4,Date.valueOf(parametros.get("fecha_final")+""));			
			ps.setString(5,parametros.get("usuario_modifica")+"");
			ps.setDate(6,Date.valueOf(parametros.get("fecha_modifica")+""));
			ps.setString(7,parametros.get("hora_modifica")+"");
			
			logger.info("valor del mapa al ingresar un encabezado >> "+parametros);
			
			if(ps.executeUpdate()>0)
				return true;			
			
		}catch(SQLException e){
			e.printStackTrace();
		}
		
		return false;
	}
	
	
	/**
	 * Modificar informacion tabla encabezado
	 * @param Connection con
	 * @param HashMap parametros
	 * @return 
	 * */	
	public static boolean modificarEncabezado(Connection con, HashMap parametros)
	{
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(strModificarEncabezado,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));			
			
			/**
			 * UPDATE " +
			"ex_tarifas_asocios " +
			"SET " +
			"fecha_inicial = ?," +
			"fecha_final = ?," +
			"usuario_modifica = ?," +
			"fecha_modifica = ?," +
			"hora_modifica = ? " +
			"WHERE " +
			"codigo = ? AND " +
			"institucion = ? 
			 */
			
			ps.setDate(1,Date.valueOf(parametros.get("fecha_inicial")+""));
			ps.setDate(2,Date.valueOf(parametros.get("fecha_final")+""));			
			ps.setString(3,parametros.get("usuario_modifica")+"");
			ps.setDate(4,Date.valueOf(parametros.get("fecha_modifica")+""));
			ps.setString(5,parametros.get("hora_modifica")+"");
			
			ps.setInt(6,Utilidades.convertirAEntero(parametros.get("codigo")+""));
			ps.setInt(7,Utilidades.convertirAEntero(parametros.get("institucion")+""));
			
			if(ps.executeUpdate()>0)
				return true;			
			
		}catch(SQLException e){
			e.printStackTrace();
		}
		
		return false;
	}
	
	
	/**
	 * Eliminar informacion tabla encabezado
	 * @param Connection con
	 * @param HashMap parametros
	 * @return 
	 * */	
	public static boolean EliminarEncabezado(Connection con, HashMap parametros)
	{
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(strEliminarEncabezado,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1,Utilidades.convertirAEntero(parametros.get("codigo")+""));
			ps.setInt(2,Utilidades.convertirAEntero(parametros.get("institucion")+""));	
			
			logger.info("valor mapa al eliminar >> "+parametros);
			
			if(ps.executeUpdate()>0)
				return true;			
			
		}catch(SQLException e){
			e.printStackTrace();
		}
		
		return false;
	}
	
	
	/**
	 * Consultar informacion tabla Encabezado
	 * @param Connection con
	 * @param HashMap parametros
	 * @return 
	 * */	
	public static HashMap consultarEncabezado(Connection con, HashMap parametros)
	{
		HashMap respuesta = new HashMap();
		try
		{
			String cadena = strConsultarEncabezado;
			
			if(parametros.containsKey("convenio"))
				cadena+=" AND e.convenio = "+parametros.get("convenio");
			
			if(parametros.containsKey("codigo"))
				cadena+=" AND e.codigo = "+parametros.get("codigo");
			
			
			
			cadena+=" ORDER BY e.fecha_inicial,e.fecha_final DESC ";
			
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1,Utilidades.convertirAEntero(parametros.get("institucion")+""));
			
			respuesta = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()),true,false);
			
		}catch(SQLException e){
			e.printStackTrace();
		}
		
		return respuesta;
	}

	
	/**
	 * Insertar informacion tabla media
	 * @param Connection con
	 * @param HashMap parametros
	 * @param String strInsertarMedia
	 * @return 
	 * */	
	public static boolean insertarMedia(Connection con, HashMap parametros,String strInsertarMedia)
	{
		try
		{			
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(strInsertarMedia,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			/**
			 * INSERT INTO ex_tarifas_asocios_xvitpcc(
			 * codigo,
			 * codigo_encab,
			 * via_ingreso,
			 * tipo_paciente,
			 * centro_costo,
			 * usuario_modifica,
			 * fecha_modifica,
			 * hora_modifica) VALUES('seq_extar_asoc_xvitpcc'),?,?,?,?,?,?,?)
			 */
			
			ps.setInt(1,Utilidades.convertirAEntero(parametros.get("codigo_encab")+""));
			
			if(!parametros.get("via_ingreso").toString().equals("") &&
					!parametros.get("via_ingreso").toString().equals(ConstantesBD.codigoNuncaValido+""))
				ps.setInt(2,Utilidades.convertirAEntero(parametros.get("via_ingreso")+""));
			else
				ps.setNull(2,Types.INTEGER);
			
			if(!parametros.get("tipo_paciente").toString().equals("") &&
					!parametros.get("tipo_paciente").toString().equals(ConstantesBD.codigoNuncaValido+""))
				ps.setString(3,parametros.get("tipo_paciente")+"");
			else
				ps.setNull(3,Types.VARCHAR);
				
			if(!parametros.get("centro_costo").toString().equals("") &&
					!parametros.get("centro_costo").toString().equals(ConstantesBD.codigoNuncaValido+""))
				ps.setInt(4,Utilidades.convertirAEntero(parametros.get("centro_costo")+""));
			else
				ps.setNull(4,Types.INTEGER);
			
			ps.setString(5,parametros.get("usuario_modifica")+"");
			ps.setDate(6,Date.valueOf(parametros.get("fecha_modifica")+""));
			ps.setString(7,parametros.get("hora_modifica")+"");
			
			
			
			if(ps.executeUpdate()>0)
				return true;			
			
		}catch(SQLException e){
			logger.info("valor del mapa media >> "+parametros);			
			e.printStackTrace();
		}
		
		return false;
	}
	
	
	
	/**
	 * Modificar informacion tabla media
	 * @param Connection con
	 * @param HashMap parametros
	 * @return 
	 * */	
	public static boolean modificarMedia(Connection con, HashMap parametros)
	{
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(strModificarMedia,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			/**
			 * UPDATE " +
			"ex_tarifas_asocios_xvitpcc " +
				"SET " +
				"via_ingreso = ? ," +
				"tipo_paciente = ?," +
				"centro_costo = ?," +
				"usuario_modifica = ?," +
				"fecha_modifica = ?," +
				"hora_modifica = ? " +
				"WHERE " +
				"codigo = ? AND codigo_encab = ? 
			 */
			
			if(!parametros.get("via_ingreso").toString().equals("") &&
					!parametros.get("via_ingreso").toString().equals(ConstantesBD.codigoNuncaValido+""))
				ps.setInt(1,Utilidades.convertirAEntero(parametros.get("via_ingreso")+""));
			else
				ps.setNull(1,Types.INTEGER);
			
			if(!parametros.get("tipo_paciente").toString().equals("") &&
					!parametros.get("tipo_paciente").toString().equals(ConstantesBD.codigoNuncaValido+""))
				ps.setString(2,parametros.get("tipo_paciente")+"");
			else
				ps.setNull(2,Types.CHAR);
				
			if(!parametros.get("centro_costo").toString().equals("") &&
					!parametros.get("centro_costo").toString().equals(ConstantesBD.codigoNuncaValido+""))
				ps.setInt(3,Utilidades.convertirAEntero(parametros.get("centro_costo")+""));
			else
				ps.setNull(3,Types.INTEGER);
			
			ps.setString(4,parametros.get("usuario_modifica")+"");
			ps.setDate(5,Date.valueOf(parametros.get("fecha_modifica")+""));
			ps.setString(6,parametros.get("hora_modifica")+"");
			
			ps.setInt(7,Utilidades.convertirAEntero(parametros.get("codigo")+""));
			ps.setInt(8,Utilidades.convertirAEntero(parametros.get("codigo_encab")+""));
			
			
			if(ps.executeUpdate()>0)
				return true;			
			
		}catch(SQLException e){
			e.printStackTrace();
		}
		
		return false;
	}
	
	
	
	/**
	 * Consultar informacion tabla Media
	 * @param Connection con
	 * @param HashMap parametros
	 * @return 
	 * */	
	public static HashMap consultarMedia(Connection con, HashMap parametros)
	{
		
		HashMap respuesta = new HashMap();
		
		try
		{
			String cadena = strConsultarMedia;
			
			if(parametros.containsKey("codigo"))
				cadena+=" AND m.codigo = "+parametros.get("codigo");
			
			
			if(parametros.containsKey("via_ingresoB"))
				cadena+=" AND m.via_ingreso = "+parametros.get("via_ingresoB").toString();
			
			if(parametros.containsKey("tipo_pacienteB"))
				cadena+=" AND m.tipo_paciente = '"+parametros.get("tipo_pacienteB").toString()+"' ";
			
			if(parametros.containsKey("centro_costoB"))
				cadena+=" AND m.centro_costo = "+parametros.get("centro_costoB").toString();			
			
			cadena+=" ORDER BY m.via_ingreso,m.tipo_paciente,m.centro_costo DESC ";
			
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1,Utilidades.convertirAEntero(parametros.get("codigo_encab")+""));
			
			respuesta = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()),true,false);
			
			
			logger.info("\n\n hashmap respuesta media SQL  CONSULTA >> "+respuesta+" parametros >> "+parametros+" \n\n ");
						
		}catch(SQLException e){
			e.printStackTrace();
		}
		
		return respuesta;
	}

	
	

	/**
	 * Eliminar informacion tabla Media
	 * @param Connection con
	 * @param HashMap parametros
	 * @return 
	 * */	
	public static boolean EliminarMedia(Connection con, HashMap parametros)
	{
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(strEliminarMedia,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1,Utilidades.convertirAEntero(parametros.get("codigo")+""));
			ps.setInt(2,Utilidades.convertirAEntero(parametros.get("codigo_encab")+""));		
			
			if(ps.executeUpdate()>0)
				return true;			
			
		}catch(SQLException e){
			e.printStackTrace();
		}
		
		return false;
	}
	
	
	/**
	 * Insertar informacion tabla Detalle
	 * @param Connection con
	 * @param HashMap parametros
	 * @return 
	 * */	
	public static boolean insertarDetalle(Connection con, HashMap parametros,String strInsertarDetalle)
	{
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(strInsertarDetalle,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			/**
			 * INSERT INTO det_ex_tarifas_asocios(
			 * codigo,
			 * codigo_encab_xvit,
			 * tipo_servicio,
			 * especialidad,
			 * grupo_servicio,
			 * servicio,
			 * tipo_cirugia,
			 * asocio,
			 * rango_inicial,
			 * rango_final,
			 * porcentaje,
			 * valor,
			 * tipo_excepcion,
			 * usuario_modifica,
			 * fecha_modifica,
			 * hora_modifica) VALUES('seq_det_ex_tarifa'),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)
			 */
			
			ps.setInt(1,Utilidades.convertirAEntero(parametros.get("codigo_encab_xvit")+""));
			
			if(!parametros.get("tipo_servicio").toString().equals("") &&
					!parametros.get("tipo_servicio").toString().equals(ConstantesBD.codigoNuncaValido+""))
				ps.setString(2,parametros.get("tipo_servicio")+"");
			else
				ps.setNull(2,Types.VARCHAR);
				
			if(!parametros.get("especialidad").toString().equals("") &&
					!parametros.get("especialidad").toString().equals(ConstantesBD.codigoNuncaValido+""))
				ps.setInt(3,Utilidades.convertirAEntero(parametros.get("especialidad")+""));
			else
				ps.setNull(3,Types.INTEGER);
			
			if(!parametros.get("grupo_servicio").toString().equals("") &&
					!parametros.get("grupo_servicio").toString().equals(ConstantesBD.codigoNuncaValido+""))
				ps.setInt(4,Utilidades.convertirAEntero(parametros.get("grupo_servicio")+""));			
			else
				ps.setNull(4,Types.INTEGER);
			
			if(!parametros.get("servicio").toString().equals("") &&
					!parametros.get("servicio").toString().equals(ConstantesBD.codigoNuncaValido+""))
				ps.setInt(5,Utilidades.convertirAEntero(parametros.get("servicio")+""));
			else				
				ps.setNull(5,Types.INTEGER);
			
			if(!parametros.get("tipo_cirugia").toString().equals("") && 
					!parametros.get("tipo_cirugia").toString().equals(ConstantesBD.codigoNuncaValido+""))
				ps.setString(6,parametros.get("tipo_cirugia")+"");
			else
				ps.setNull(6,Types.CHAR);
			
			if(!parametros.get("asocio").toString().equals("") &&
					!parametros.get("asocio").toString().equals(ConstantesBD.codigoNuncaValido+""))
				ps.setInt(7,Utilidades.convertirAEntero(parametros.get("asocio")+""));
			else
				ps.setNull(7,Types.INTEGER);
			
			ps.setInt(8,Utilidades.convertirAEntero(parametros.get("rango_inicial")+""));
			
			ps.setInt(9,Utilidades.convertirAEntero(parametros.get("rango_final")+""));
			
			if(!parametros.get("porcentaje").toString().equals(""))
				ps.setDouble(10,Utilidades.convertirADouble(parametros.get("porcentaje")+""));
			else
				ps.setNull(10,Types.NUMERIC);
			
			if(!parametros.get("valor").toString().equals(""))
				ps.setDouble(11,Utilidades.convertirADouble(parametros.get("valor")+""));
			else
				ps.setNull(11,Types.NUMERIC);
						
			ps.setString(12,parametros.get("tipo_excepcion")+"");			
			
			ps.setString(13,parametros.get("usuario_modifica")+"");
			ps.setDate(14,Date.valueOf(parametros.get("fecha_modifica")+""));
			ps.setString(15,parametros.get("hora_modifica")+"");
			
			if(ps.executeUpdate()>0)
				return true;			
			
		}catch(SQLException e){
			e.printStackTrace();
		}
		
		return false;
	}	
	
	
	
	/**
	 * Modificar informacion tabla Detalle
	 * @param Connection con
	 * @param HashMap parametros
	 * @return 
	 * */	
	public static boolean modificarDetalle(Connection con, HashMap parametros)
	{
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(strModificacionDetalle,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			/**
			 * UPDATE " +
			"det_ex_tarifas_asocios " +
			"SET " +
			"tipo_servicio = ?," +
			"especialidad = ?," +
			"grupo_servicio = ?," +
			"servicio = ?," +
			"tipo_cirugia =?," +
			"asocio = ?," +
			"rango_inicial = ?," +
			"rango_final = ?," +
			"porcentaje= ?," +
			"valor = ?," +
			"tipo_excepcion = ?," +
			"usuario_modifica = ?," +
			"fecha_modifica = ?," +
			"hora_modifica = ? " +
			"WHERE " +
			"codigo = ? AND " +
			"codigo_encab_xvit = ? 
			 */
			
			if(!parametros.get("tipo_servicio").toString().equals("") &&
					!parametros.get("tipo_servicio").toString().equals(ConstantesBD.codigoNuncaValido+""))
				ps.setString(1,parametros.get("tipo_servicio")+"");
			else
				ps.setNull(1,Types.CHAR);
				
			if(!parametros.get("especialidad").toString().equals("")&&
					!parametros.get("especialidad").toString().equals(ConstantesBD.codigoNuncaValido+""))
				ps.setInt(2,Utilidades.convertirAEntero(parametros.get("especialidad")+""));
			else
				ps.setNull(2,Types.INTEGER);
			
			if(!parametros.get("grupo_servicio").toString().equals("")&&
					!parametros.get("grupo_servicio").toString().equals(ConstantesBD.codigoNuncaValido+""))
				ps.setInt(3,Utilidades.convertirAEntero(parametros.get("grupo_servicio")+""));			
			else
				ps.setNull(3,Types.INTEGER);
			
			if(!parametros.get("servicio").toString().equals("")&&
					!parametros.get("servicio").toString().equals(ConstantesBD.codigoNuncaValido+""))
				ps.setInt(4,Utilidades.convertirAEntero(parametros.get("servicio")+""));
			else				
				ps.setNull(4,Types.INTEGER);
			
			if(!parametros.get("tipo_cirugia").toString().equals("") &&
					!parametros.get("tipo_cirugia").toString().equals(ConstantesBD.codigoNuncaValido+""))
				ps.setString(5,parametros.get("tipo_cirugia")+"");
			else
				ps.setNull(5,Types.CHAR);
			
			if(!parametros.get("asocio").toString().equals("")&&
					!parametros.get("asocio").toString().equals(ConstantesBD.codigoNuncaValido+""))
				ps.setInt(6,Utilidades.convertirAEntero(parametros.get("asocio")+""));
			else
				ps.setNull(6,Types.INTEGER);
			
			ps.setInt(7,Utilidades.convertirAEntero(parametros.get("rango_inicial")+""));
			
			ps.setInt(8,Utilidades.convertirAEntero(parametros.get("rango_final")+""));
			
			if(!parametros.get("porcentaje").toString().equals(""))
				ps.setDouble(9,Utilidades.convertirADouble(parametros.get("porcentaje")+""));
			else
				ps.setNull(9,Types.NUMERIC);
			
			if(!parametros.get("valor").toString().equals(""))
				ps.setDouble(10,Utilidades.convertirADouble(parametros.get("valor")+""));
			else
				ps.setNull(10,Types.NUMERIC);
						
			ps.setString(11,parametros.get("tipo_excepcion")+"");			
			
			ps.setString(12,parametros.get("usuario_modifica")+"");
			ps.setDate(13,Date.valueOf(parametros.get("fecha_modifica")+""));
			ps.setString(14,parametros.get("hora_modifica")+"");
			
			ps.setInt(15,Utilidades.convertirAEntero(parametros.get("codigo")+""));
			ps.setInt(16,Utilidades.convertirAEntero(parametros.get("codigo_encab_xvit")+""));
			
			if(ps.executeUpdate()>0)
				return true;			
			
		}catch(SQLException e){
			e.printStackTrace();
		}
		
		return false;
	}
	
	
	
	/**
	 * Eliminar informacion tabla Detalle
	 * @param Connection con
	 * @param HashMap parametros
	 * @return 
	 * */	
	public static boolean EliminarDetalle(Connection con, HashMap parametros)
	{
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(strEliminarDetalle,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1,Utilidades.convertirAEntero(parametros.get("codigo")+""));
			ps.setInt(2,Utilidades.convertirAEntero(parametros.get("codigo_encab_xvit")+""));		
			
			if(ps.executeUpdate()>0)
				return true;			
			
		}catch(SQLException e){
			e.printStackTrace();
		}
		
		return false;
	}
	
	
	/**
	 * Consultar informacion tabla Detalle
	 * @param Connection con
	 * @param HashMap parametros
	 * @return 
	 * */	
	public static HashMap consultarDetalle(Connection con, HashMap parametros)
	{
		HashMap respuesta = new HashMap();
		
		try
		{
			String cadena = strConsultaDetalle;
			
			if(parametros.containsKey("codigo"))
				cadena+=" AND d.codigo = "+parametros.get("codigo")+" ";			
			
			if(parametros.containsKey("tipo_servicioB"))
				cadena+=" AND d.tipo_servicio = '"+parametros.get("tipo_servicioB")+"' ";
			
			if(parametros.containsKey("grupo_servicioB"))
				cadena+=" AND d.grupo_servicio = "+parametros.get("grupo_servicioB")+" ";
			
			if(parametros.containsKey("especialidadB"))
				cadena+=" AND d.especialidad = "+parametros.get("especialidadB")+" ";
			
			if(parametros.containsKey("servicioB"))
				cadena+=" AND d.servicio = "+parametros.get("servicioB")+" ";
			
			if(parametros.containsKey("tipo_cirugiaB"))
				cadena+=" AND d.tipo_cirugia = '"+parametros.get("tipo_cirugiaB")+"' ";
			
			if(parametros.containsKey("asocioB"))
				cadena+=" AND d.asocio = "+parametros.get("asocioB")+" ";
			
			cadena+=" ORDER BY  nombre_tipo_servicio,nombre_especialidad,nombre_grupo_servicio,nombreServicio,nombre_tipo_cirugia ";
			
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1,Utilidades.convertirAEntero(parametros.get("codigo_encab_xvit")+""));
			
			respuesta = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()),true,false);
			
		}catch(SQLException e){
			e.printStackTrace();
		}
		
		return respuesta;
	}	
}