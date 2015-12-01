/*
 * Sep 09/2005
 */
package com.princetonsa.dao.sqlbase;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.ibm.icu.util.ULocale.Type;

import util.ConstantesBD;
import util.RespuestaHashMap;
import util.UtilidadBD;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

/**
 * @author Sebastián Gómez
 * @author Modificado 07 Nov Jose Eduardo Arias Doncel
 *
 * Objeto usado para manejar los accesos comunes a la fuente de datos
 * para la parametrización de Porcentajes Cirugías Múltiples
 */
public class SqlBasePorcentajesCxMultiplesDao {
	/**
	 * Manejador de logs de la clase
	 */
	private static Logger logger=Logger.getLogger(SqlBasePorcentajesCxMultiplesDao.class);
	
	/**
	 * Cadena usada para consultar todos los registros del encabezado de 
	 * porcentajes cirugias multiples
	 * */
	private static final String cargarEncaPorcentahesStr ="SELECT " +
			"e.codigo AS codigo_encab," +
			"e.institucion," +			
			"e.esq_tar_particular," +
			"esq.nombre AS nombre_esq_tar_part," +
			"e.esq_tar_general," +
			"tar.nombre AS nombre_esq_tar_gen,"+
			"e.convenio," +
			"to_char(e.fecha_inicial,'DD/MM/YYYY') AS fecha_inicial," +
			"to_char(e.fecha_final,'DD/MM/YYYY') AS fecha_final," +
			"getcuantosdetencaporcxm(e.codigo) AS cuanto_detalle," +
			"'"+ConstantesBD.acronimoSi+"' AS estabd " +
			"FROM enca_porcen_cx_multi e " +
			"LEFT OUTER JOIN esq_tar_porcen_cx esq ON (esq.codigo = e.esq_tar_particular) " +
			"LEFT OUTER JOIN esquemas_tarifarios tar ON (tar.codigo = e.esq_tar_general) " +
			"WHERE e.institucion = ? ";
			
	
	
	/**
	 * Cadena usada para consultar todos los registros de
	 * los porcentajes cirugias múltiples por codigo del encabezado
	 */
	private static final String cargarPorcentajesStr="SELECT "+ 
		"p.codigo AS codigo," +
		"p.codigo_encab,"+		
		"p.tipo_cirugia," +
		"c.nombre AS nombre_tcirugia,"+
		"CASE WHEN p.tipo_asocio IS NULL THEN "+ConstantesBD.codigoNuncaValido+"  ELSE p.tipo_asocio END  AS tipo_asocio," +
		"a.codigo_asocio || '-' || a.nombre_asocio AS nombre_asocio, "+
		"CASE WHEN p.tipo_especialista IS NULL THEN "+ConstantesBD.codigoNuncaValido+" || '' ELSE p.tipo_especialista END AS tipo_especialista,"+
		"CASE WHEN p.via_acceso IS NULL THEN "+ConstantesBD.codigoNuncaValido+" || '' ELSE p.via_acceso END AS via_acceso,"+
		"p.liquidacion AS liquidacion,"+
		"p.adicional AS adicional,"+
		"p.politraumatismo AS politra,"+		 
		"p.tipo_servicio," +
		"s.nombre AS nombre_tservicio," +
		"CASE WHEN p.tipo_sala IS NULL THEN "+ConstantesBD.codigoNuncaValido+"   ELSE p.tipo_sala END AS tipo_sala," +
		"upper(ts.descripcion) AS nombre_tsala," +
		"'"+ConstantesBD.acronimoSi+"' AS estabd " +
		"FROM "+ 
		"porcentajes_cx_multi p "+ 
		"LEFT OUTER JOIN tipos_cirugia c ON(c.acronimo=p.tipo_cirugia) "+ 
		"LEFT OUTER JOIN tipos_asocio a ON(a.codigo=p.tipo_asocio) "+ 
		"LEFT OUTER JOIN tipos_servicio s ON(p.tipo_servicio=s.acronimo) "+	
		"LEFT OUTER JOIN tipos_salas ts ON(ts.codigo = p.tipo_sala) "+
		"WHERE  p.codigo_encab = ?  "; 
	
	
	/**
	 * Cadena usada para actualizar un registro del encabezado
	 * */
	private static final String actualizarEncaPorcentajeStr="UPDATE " +
			"enca_porcen_cx_multi " +
			"SET esq_tar_particular=?, " +
			"esq_tar_general = ?," +
			"convenio = ?," +
			"fecha_inicial= ?," +
			"fecha_final = ?," +
			"usuario_modifica = ?," +
			"fecha_modifica = ?," +
			"hora_modifica = ?" +
			"WHERE codigo = ?  ";
	
	/**
	 * Cadena para actualizar un registro en porcentajes_cx_multi
	 */
	private static final String actualizarPorcentajeStr="UPDATE " +
			"porcentajes_cx_multi " +
			"SET tipo_cirugia=?," +
			"tipo_asocio=?," +
			"tipo_especialista=?," +
			"via_acceso=?," +
			"liquidacion=?," +
			"adicional=?," +
			"politraumatismo=?," +			
			"tipo_servicio = ?, " +
			"tipo_sala = ?," +
			"usuario_modifica = ?," +
			"fecha_modifica = ?," +
			"hora_modifica = ? " +
			"WHERE codigo=? ";
	
		
	/**
	 * Cadena usada para eliminar un registro del encabezado de porcentajes_cx_multi
	 * */
	private static final String eliminarEncaPorcentajesStr = "DELETE FROM enca_porcen_cx_multi WHERE codigo = ? ";
	
	/**
	 * Cadena usada para eliminar un registro de porcentajes_cx_multi
	 */
	private static final String eliminarPorcentajeStr="DELETE FROM porcentajes_cx_multi WHERE codigo=? ";
	
	
	
	//*****************************************************************************
	// Cadenas para la Consulta****************************************************
	
	
	/**
	 * Cadena usada para consultar un registro de
	 * los porcentajes cirugias múltiples
	 */
	private static final String cargarPorcentajeStr="SELECT "+
		"p.codigo AS codigo,"+
		"p.esq_tar_particular AS esquema_particular,"+
		"p.esq_tar_general AS esquema_general,"+
		"p.tipo_cirugia AS tipo_cirugia,"+
		"getnomtipocirugia(p.tipo_cirugia) AS nom_tipo_cirugia,"+
		"p.tipo_asocio AS asocio,"+
		"getnombretipoasocio(ta.tipos_servicio) As nom_tipo_asocio,"+
		"p.tipo_especialista AS medico,"+
		"getnomopporcentaje(p.tipo_especialista) AS nom_medico,"+
		"p.via_acceso AS via,"+
		"getnomopporcentaje(p.via_acceso) AS nom_via,"+
		"p.liquidacion AS liquidacion,"+
		"p.adicional AS adicional,"+
		"p.politraumatismo AS politra,"+		
		"CASE WHEN p.esq_tar_particular IS NULL THEN 'true' ELSE 'false' END AS es_general,"+
		"CASE WHEN p.esq_tar_particular IS NULL THEN " +
			"getnomesqporcentaje(p.esq_tar_general,'true') " +
		"ELSE " +
			"getnomesqporcentaje(p.esq_tar_particular,'false') " +
		"END AS nom_esquema," +
		"p.convenio," +
		"to_char(p.fecha_inicial,'yyyy-mm-dd') as fecha_inicial," +
		"to_char(p.fecha_final,'yyyy-mm-dd') as fecha_final," +
		"p.tipos_servicio," +
		"p.tipos_salas "+  
		"FROM "+ 
		"porcentajes_cx_multi p, tipos_asocio ta "+  
		"WHERE p.tipo_asocio=ta.codigo AND p.codigo=?";
	
	/**
	 * Cadena usada para la bsuqueda de registros Porcentajes
	 */
	private static final String busquedaPorcentajesStr="SELECT "+
		"CASE WHEN p.esq_tar_particular IS NULL THEN getnomesqporcentaje(p.esq_tar_general,'true') ELSE getnomesqporcentaje(p.esq_tar_particular,'false') END AS esquema,"+
		"p.tipo_cirugia AS cirugia,"+
		"ta.tipos_servicio AS asocio,"+
		"getnomopporcentaje(p.tipo_especialista) As medico,"+
		"getnomopporcentaje(p.via_acceso) AS via,"+
		"p.liquidacion AS liquidacion,"+
		"p.adicional AS adicional,"+
		"p.politraumatismo AS politra,"+
		"p.convenio," +
		"to_char(p.fecha_inicial,'yyyy-mm-dd') as fecha_inicial," +
		"to_char(p.fecha_final,'yyyy-mm-dd') as fecha_final," +
		"p.tipos_servicio," +
		"p.tipos_salas "+
		"FROM "+ 
		"porcentajes_cx_multi p "+ 
		"INNER JOIN tipos_asocio ta ON(p.tipo_asocio=ta.codigo) "+
		"LEFT OUTER JOIN esquemas_tarifarios e ON(e.codigo=p.esq_tar_particular) "+ 
		"LEFT OUTER JOIN esq_tar_porcen_cx et ON(et.codigo=p.esq_tar_general) " ;
	
	
	//*****************************************************************************
	//Metodos**********************************************************************
	
		
	/**
	 * Método para cargar el listado de los procentajes de cirugías
	 * múltiples parametrizadas por institución
	 * @param con
	 * @param institucion
	 * @return
	 */
	public static HashMap cargarEncaPorcentajes(Connection con,int institucion,HashMap parametros)
	{
		//columnas del listado
		String[] columnas={
				"codigo_encab",
				"institucion",
				"esq_tar_particular",
				"nombre_esq_tar_part",
				"esq_tar_general",
				"nombre_esq_tar_gen",
				"convenio",
				"fecha_inicial",
				"fecha_final",
				"cuanto_detalle",
				"estabd"				
			};
		
		String cadena = cargarEncaPorcentahesStr;
		
		if(parametros.containsKey("convenio"))		
			cadena+= " AND e.convenio = "+parametros.get("convenio")+" ";	
		else if(parametros.containsKey("esq_tar_particular"))	
			cadena+= " AND (e.esq_tar_particular = "+parametros.get("esq_tar_particular")+" AND e.esq_tar_general = "+parametros.get("esq_tar_general")+" ) ";				
		
		
		if(parametros.containsKey("codigo_encab"))
			cadena+= " AND e.codigo =  "+parametros.get("codigo_encab")+" ";

	
	
		logger.info("--------->"+cadena);
		
		try
		{
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,institucion);
			RespuestaHashMap listado=UtilidadBD.resultSet2HashMap(columnas,new ResultSetDecorator(pst.executeQuery()),false,true);
			return listado.getMapa();
		}
		catch(SQLException e)
		{
			logger.error("Error en cargarPorcentajes de SqlBasePorcentajesCxMultiplesDao: "+e);
			return null;
		}
	}
				 
	
	/**
	 * Método para cargar el listado de los procentajes de cirugías
	 * múltiples parametrizadas por institución
	 * @param con
	 * @param institucion
	 * @return
	 */
	public static HashMap cargarPorcentajes(Connection con,int institucion,HashMap parametros)
	{
		//columnas del listado
		String[] columnas={
				"codigo",
				"codigo_encab",
				"tipo_cirugia",				
				"nombre_tcirugia",
				"tipo_asocio",
				"nombre_asocio",
				"tipo_especialista",
				"via_acceso",
				"liquidacion",
				"adicional",
				"politra",				
				"tipo_servicio",
				"nombre_tservicio",
				"tipo_sala",
				"nombre_tsala",
				"estabd"
			};
		
		String cadena = cargarPorcentajesStr;
		
		if(parametros.containsKey("codigo_detalle"))
			cadena+=" AND p.codigo = "+parametros.get("codigo_detalle");
		
		if(parametros.containsKey("tipo_servicioB"))
			cadena+=" AND p.tipo_servicio = '"+parametros.get("tipo_servicioB")+"' ";
		
		if(parametros.containsKey("tipo_cirugiaB"))
			cadena+=" AND p.tipo_cirugia = '"+parametros.get("tipo_cirugiaB")+"' ";
		
		if(parametros.containsKey("tipo_asocioB"))
			cadena+=" AND p.tipo_asocio = "+parametros.get("tipo_asocioB")+" ";
		
		if(parametros.containsKey("tipo_especialistaB"))
			cadena+=" AND p.tipo_especialista = '"+parametros.get("tipo_especialistaB")+"' ";
		
		if(parametros.containsKey("via_accesoB"))
			cadena+=" AND p.via_acceso = '"+parametros.get("via_accesoB")+"' ";				
		
		
		cadena+=" ORDER BY p.tipo_servicio,p.tipo_sala,p.tipo_cirugia ";
		
		logger.info("\n\n\n\n\n -->valor de la cadena >> "+cadena+" >> "+parametros.get("codigo_encab"));
		
		try
		{
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setObject(1,parametros.get("codigo_encab"));
			RespuestaHashMap listado=UtilidadBD.resultSet2HashMap(columnas,new ResultSetDecorator(pst.executeQuery()),false,true);
			return listado.getMapa();
		}
		catch(SQLException e)
		{
			logger.error("Error en cargarPorcentajes de SqlBasePorcentajesCxMultiplesDao: "+e);
			return null;
		}
	}
	
	
	/**
	 * Método usado para modificar un registros de encabezado de porcentajes
	 * de cirugías múltiples
	 * @param con
	 * @param HashMap parametros	  
	 * @return
	 */
	public static int actualizarEncaPorcentaje(
			Connection con,
			HashMap parametros)
	{
		try
		{			
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(actualizarEncaPorcentajeStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			//Esquema Tarifario			
			if(!parametros.get("esq_tar_particular").toString().equals(""))
				pst.setObject(1,parametros.get("esq_tar_particular"));
			else
				pst.setNull(1,Types.INTEGER);
			
			if(!parametros.get("esq_tar_general").toString().equals(""))
				pst.setObject(2,parametros.get("esq_tar_general"));
			else
				pst.setNull(2,Types.INTEGER);
			
			//Convenio
			if(!parametros.get("convenio").toString().equals(""))
				pst.setObject(3,parametros.get("convenio"));
			else
				pst.setNull(3,Types.NULL);
			
			//fecha_inicial 
			if(!parametros.get("fecha_inicial").toString().equals(""))
				pst.setObject(4,parametros.get("fecha_inicial"));
			else
				pst.setNull(4,Types.NULL);
			
			//fecha_final 
			if(!parametros.get("fecha_final").toString().equals(""))
				pst.setObject(5,parametros.get("fecha_final"));
			else
				pst.setNull(5,Types.NULL);			
						
			//Usuario Modifica
			pst.setObject(6,parametros.get("usuario_modifica"));
			
			//Fecha Modifica
			pst.setObject(7,parametros.get("fecha_modifica"));
			
			//Hora Modifica
			pst.setObject(8,parametros.get("hora_modifica"));
						
			//Codigo
			pst.setObject(9,parametros.get("codigo_encab"));
			
			return pst.executeUpdate();
		}
		catch(SQLException e)
		{
			logger.error("Error en actualizarEncaPorcentaje de SqlBasePorcentajesCxMultiplesDao: "+e);
			return -1;
		}
	}
	
	
	
	/**
	 * Método usado para modificar un registros de los porcentajes
	 * de cirugías múltiples
	 * @param con
	 * @param HashMap parametros	  
	 * @return
	 */
	public static int actualizarPorcentaje(
			Connection con,
			HashMap parametros)
	{
		try
		{			
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(actualizarPorcentajeStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));			
			
			//Campo tipo cirugia
			if(!parametros.get("tipo_cirugia").toString().equals("") 
					&& !parametros.get("tipo_cirugia").toString().equals(ConstantesBD.codigoNuncaValido+""))				
				pst.setObject(1,parametros.get("tipo_cirugia"));
			else
				pst.setNull(1,Types.VARCHAR);
			
			//Campo tipo asocio
			pst.setObject(2,parametros.get("tipo_asocio"));
			
			//Campo tipo especialista
			if(!parametros.get("tipo_especialista").toString().equals("")&&!parametros.get("tipo_especialista").toString().equals(ConstantesBD.codigoNuncaValido+""))				
				pst.setObject(3,parametros.get("tipo_especialista"));
			else
				pst.setNull(3,Types.VARCHAR);
			
			//Campos via acceso
			if(!parametros.get("via_acceso").toString().equals("")&&!parametros.get("via_acceso").toString().equals(ConstantesBD.codigoNuncaValido+""))
				pst.setObject(4,parametros.get("via_acceso"));
			else
				pst.setNull(4,Types.VARBINARY);
			
			//Liquidacion
			pst.setObject(5,parametros.get("liquidacion"));
			
			//Adicional 
			pst.setObject(6,parametros.get("adicional"));
			
			//Politraumatismo			
			pst.setObject(7,parametros.get("politra"));			
			
			//Tipo Servicio						
			if(!parametros.get("tipo_servicio").toString().equals("") 
					&& !parametros.get("tipo_servicio").toString().equals(ConstantesBD.codigoNuncaValido+""))
				pst.setObject(8,parametros.get("tipo_servicio"));
			else 
				pst.setNull(8,Types.NULL);
			
			//Tipo Sala
			if(!parametros.get("tipo_sala").toString().equals("") 
					&& !parametros.get("tipo_sala").toString().equals(ConstantesBD.codigoNuncaValido+""))
				pst.setObject(9,parametros.get("tipo_sala"));
			else
				pst.setNull(9,Types.NULL);
			
			//Usuario Modifica
			pst.setObject(10,parametros.get("usuario_modifica"));
			
			//Fecha Modifica
			pst.setObject(11,parametros.get("fecha_modifica"));
			
			//Hora Modifica
			pst.setObject(12,parametros.get("hora_modifica"));
						
			//Codigo
			pst.setObject(13,parametros.get("codigo"));
			
			return pst.executeUpdate();
		}
		catch(SQLException e)
		{
			logger.error("Error en actualizarPorcentaje de SqlBasePorcentajesCxMultiplesDao: "+e);
			return -1;
		}
	}
	

	
	/**
	 * Método para eliminar un porcentaje de cirugía múltiple
	 * @param con
	 * @param codigo
	 * @return
	 */
	public static int eliminarEncaPorcentaje(Connection con,int codigo)
	{
		try
		{
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(eliminarEncaPorcentajesStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,codigo);
			
			return pst.executeUpdate();
		}
		catch(SQLException e)
		{
			logger.error("Error en eliminarEncaPorcentaje de SqlBasePocentajesCxMultiplesDao: "+e);
			return -1;
		}
	}
	
	
	
	/**
	 * Método para eliminar un porcentaje de cirugía múltiple
	 * @param con
	 * @param codigo
	 * @return
	 */
	public static int eliminarPorcentaje(Connection con,int codigo)
	{
		try
		{
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(eliminarPorcentajeStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,codigo);
			
			return pst.executeUpdate();
		}
		catch(SQLException e)
		{
			logger.error("Error en eliminarPorcentaje de SqlBasePocentajesCxMultiplesDao: "+e);
			return -1;
		}
	}
	
	
		
	
	/**
	 * Método usado para insertar un nuevo Encabezado de Porcentaje de cirugia múltiple
	 * @param con
	 * @param HashMap parametros
	 * @param insertarPorcentajeStr
	 * @return retorna -1 o 0 si la transacción no fue exitosa
	 */
	public static int insertarEncaPorcentaje(
			Connection con,
			HashMap parametros,
			String insertarEncaPorcentajeStr)
	{
		try
		{
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(insertarEncaPorcentajeStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			//Institucion
			pst.setObject(1,parametros.get("institucion"));
			
			//Esquema Tarifario			
			if(!parametros.get("esq_tar_particular").toString().equals(""))
				pst.setObject(2,parametros.get("esq_tar_particular"));
			else
				pst.setNull(2,Types.INTEGER);			
			
			
			if(!parametros.get("esq_tar_general").toString().equals(""))
				pst.setObject(3,parametros.get("esq_tar_general"));
			else
				pst.setNull(3,Types.INTEGER);
			
			
			//Convenio
			if(!parametros.get("convenio").toString().equals(""))
				pst.setObject(4,parametros.get("convenio"));
			else
				pst.setNull(4,Types.NULL);
				
			
			//fecha_inicial 
			if(!parametros.get("fecha_inicial").toString().equals(""))
				pst.setObject(5,parametros.get("fecha_inicial"));
			else
				pst.setNull(5,Types.NULL);
				
			
			//fecha_final 
			if(!parametros.get("fecha_final").toString().equals(""))
				pst.setObject(6,parametros.get("fecha_final"));
			else
				pst.setNull(6,Types.NULL);				
						
			//Usuario Modifica
			pst.setObject(7,parametros.get("usuario_modifica"));
			
			//Fecha Modifica
			pst.setObject(8,parametros.get("fecha_modifica"));
			
			//Hora Modifica
			pst.setObject(9,parametros.get("hora_modifica"));			
			
			if(pst.executeUpdate()>0)
			{
				String cadena ="";
				
				if(!parametros.get("convenio").toString().equals(""))
					cadena= "SELECT " +
							"codigo " +
							"FROM enca_porcen_cx_multi " +
							"WHERE " +							
							"convenio = "+parametros.get("convenio")+" AND " +
							"to_char(fecha_inicial,'yyyy-mm-dd') = '"+parametros.get("fecha_inicial")+"' AND " +
							"to_char(fecha_final,'yyyy-mm-dd') = '"+parametros.get("fecha_final")+"' AND " +
							"institucion = "+parametros.get("institucion")+" "+
							ValoresPorDefecto.getValorLimit1()+" 1";
				else					
				{
					if(parametros.get("esq_tar_particular").toString().equals(""))						
							cadena = "SELECT " +
									"codigo " +
									"FROM enca_porcen_cx_multi " +
									"WHERE " +
									"esq_tar_particular  IS NULL AND " +
									"esq_tar_general = "+parametros.get("esq_tar_general")+" AND " +							
									"institucion = "+parametros.get("institucion")+" "+
									ValoresPorDefecto.getValorLimit1()+" 1";
					
					if(parametros.get("esq_tar_general").toString().equals(""))						
						cadena = "SELECT " +
								"codigo " +
								"FROM enca_porcen_cx_multi " +
								"WHERE " +
								"esq_tar_general  IS NULL AND " +
								"esq_tar_particular = "+parametros.get("esq_tar_particular")+" AND " +							
								"institucion = "+parametros.get("institucion")+" "+
								ValoresPorDefecto.getValorLimit1()+" 1";
				}
				
				logger.info("consulta -->"+cadena);
				pst =  new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
				
				
				if(rs.next())			
				{			
					return rs.getInt(1);
				}
			}			
			return -1;			
		}
		catch(SQLException e)
		{
			logger.error("Error en insertarEncaPorcentaje de SqlBasePorcentajesCxMultiplesDao: "+e);
			e.printStackTrace();
			return -1;
		}
	}
	
	
	/**
	 * Método usado para insertar un nuevo Porcentraje de cirugia múltiple
	 * @param con
	 * @param HashMap parametros
	 * @param insertarPorcentajeStr
	 * @return retorna -1 o 0 si la transacción no fue exitosa
	 */
	public static int insertarPorcentaje(
			Connection con,
			HashMap parametros,
			String insertarPorcentajeStr)
	{
		try
		{
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(insertarPorcentajeStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			logger.info("PARAMETROS INSERCION.");
			
			//Codigo Encabezado
			pst.setObject(1,parametros.get("codigo_encab"));
			
			//Campo tipo cirugia
			if(!parametros.get("tipo_cirugia").toString().equals(""))				
				pst.setObject(2,parametros.get("tipo_cirugia"));
			else
				pst.setNull(2,Types.NULL);
			
			//Campo tipo asocio
			pst.setObject(3,parametros.get("tipo_asocio"));
			
			//Campo tipo especialista
			if(!parametros.get("tipo_especialista").toString().equals("") 
					&& !parametros.get("tipo_especialista").toString().equals(ConstantesBD.codigoNuncaValido+""))				
				pst.setObject(4,parametros.get("tipo_especialista"));
			else
				pst.setNull(4,Types.NULL);
			
			//Campos via acceso
			if(!parametros.get("via_acceso").toString().equals("") 
					&& !parametros.get("via_acceso").toString().equals(ConstantesBD.codigoNuncaValido+""))
				pst.setObject(5,parametros.get("via_acceso"));
			else
				pst.setNull(5,Types.NULL);
			
			//Liquidacion
			pst.setObject(6,parametros.get("liquidacion"));
			
			//Adicional 
			pst.setObject(7,parametros.get("adicional"));
			
			//Politraumatismo			
			pst.setObject(8,parametros.get("politra"));			
			
			//Tipo Servicio						
			if(!parametros.get("tipo_servicio").toString().equals(""))
				pst.setObject(9,parametros.get("tipo_servicio"));
			else 
				pst.setNull(9,Types.NULL);
			
			//Tipo Sala
			if(!parametros.get("tipo_sala").toString().equals("") 
					&& !parametros.get("tipo_sala").toString().equals(ConstantesBD.codigoNuncaValido+""))
				pst.setObject(10,parametros.get("tipo_sala"));
			else
				pst.setNull(10,Types.NULL);
			
			//Usuario Modifica
			pst.setObject(11,parametros.get("usuario_modifica"));
			
			//Fecha Modifica
			pst.setObject(12,parametros.get("fecha_modifica"));
			
			//Hora Modifica
			pst.setObject(13,parametros.get("hora_modifica"));				
			
			return pst.executeUpdate();
			
		}
		catch(SQLException e)
		{
			logger.error("Error en insertarPorcentaje de SqlBasePorcentajesCxMultiplesDao: "+e);
			return -1;
		}
	}
	
	
	
	
	//*****************************************************************************
	// Metodos  para la Consulta****************************************************	
	
	
	
	/**
	 * Método usao para cargar un registro de los porcentajes
	 * de cirugías múltiples por su código
	 * @param con
	 * @param codigo
	 * @return
	 */
	public static HashMap cargarPorcentaje(Connection con,int codigo)
	{
		//columnas del listado
		String[] columnas={
				"codigo",
				"esquema_particular",
				"esquema_general",
				"tipo_cirugia",
				"nom_tipo_cirugia",
				"asocio",
				"nom_tipo_asocio",
				"medico",
				"nom_medico",
				"via",
				"nom_via",
				"liquidacion",
				"adicional",
				"politra",
				"convenio",
				"fecha_inicial",
				"fecha_final",
				"tipos_servicio",
				"tipos_salas",
				"es_general",
				"nom_esquema"
			};
		try
		{
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(cargarPorcentajeStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			logger.info("--->"+cargarPorcentajeStr+"---"+codigo);
			pst.setInt(1,codigo);
			RespuestaHashMap listado=UtilidadBD.resultSet2HashMap(columnas,new ResultSetDecorator(pst.executeQuery()),false,true);
			return listado.getMapa();
			
		}
		catch(SQLException e)
		{
			logger.error("Error en cargarPorcentaje de SqlBasePorcentajesCxMultiplesDao: "+e);
			return null;
		}
	}
	
	/**
	 * Método usado para la búsqueda de registros en 
	 * Consultar Porcentajes Cx Múltiples.
	 * @param con
	 * @param esGeneral
	 * @param esquemaTarifario
	 * @param tipoCirugia
	 * @param asocio
	 * @param medico 
	 * @param via
	 * @param liquidacion
	 * @param politra
	 * @param activo
	 * @param institucion
	 * @return
	 */
	public static HashMap busquedaPorcentajes(
			Connection con,
			String esGeneral,
			int esquemaTarifario,
			String tipoCirugia,
			int asocio,
			int medico,
			int via,
			float liquidacion,
			float adicional,
			float politra,
			int institucion)
	{
		//columnas del listado
		String[] columnas={
				"esquema",
				"cirugia",
				"asocio",
				"medico",
				"via",
				"liquidacion",
				"adicional",
				"politra",				
				"convenio",
				"fecha_inicial",
				"fecha_final",
				"tipos_servicio",
				"tipos_salas"
			};
		try
		{
			String consulta=busquedaPorcentajesStr;
			consulta+=" WHERE p.institucion="+institucion;
			
			//parametrización del esquema tarifario
			if(esGeneral.equals("true"))
				consulta+=" AND p.esq_tar_general="+esquemaTarifario;
			else if(esGeneral.equals("false"))
				consulta+="AND p.esq_tar_particular="+esquemaTarifario;
			
			//parametrización del tipo de cirugía
			if(!tipoCirugia.equals("0"))
				consulta+=" AND p.tipo_cirugia='"+tipoCirugia+"'";
			
			//parametrización del tipo de asocio
			if(asocio>0)
				consulta+=" AND p.tipo_asocio="+asocio;
			
			//parametrización del medico
			if(medico>0)
				consulta+=" AND p.medico="+medico;
			
			//parametrizacion de la vía de acceso
			if(via>0)
				consulta+=" AND p.via_acceso="+via;
			
			//parametrización del % liquidacion
			if(liquidacion>=0)
				consulta+=" AND p.liquidacion="+liquidacion;
			
			//parametrización del % adicional
			if(adicional>=0)
				consulta+=" AND p.adicional="+adicional;
			
			//parametrización del % politraumatismo
			if(politra>=0)
				consulta+=" AND p.politraumatismo="+politra;			
			
			consulta+=" ORDER BY et.nombre,e.nombre,p.tipo_cirugia,ta.tipos_servicio";
			
			Statement st=con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			
			RespuestaHashMap listado=UtilidadBD.resultSet2HashMap(columnas,new ResultSetDecorator(st.executeQuery(consulta)),false,true);
			return listado.getMapa();
		}
		catch(SQLException e)
		{
			logger.error("Error en busquedaPorcentajes de SqlBasePorcentrajesCxMultiplesDao: "+e);
			return null;
		}
	}
}