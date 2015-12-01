/*
 * @(#)SqlBaseAsociosTipoServiciosDao.java
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
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.princetonsa.dao.DaoFactory;

import util.ConstantesBD;
import util.ValoresPorDefecto;

/**
 * Implementación sql genérico de todas las funciones de acceso a la fuente de datos
 * para los asocios por tipo servicio
 *
 * @version 1.0, Sep 20 / 2005
 * @author <a href="mailto:wilson@PrincetonSA.com">Wilson Ríos</a>
 */
public class SqlBaseAsociosTipoServicioDao 
{
    /**
	* Objeto para manejar los logs de esta clase
	*/
	private static Logger logger = Logger.getLogger(SqlBaseAsociosTipoServicioDao.class);

	/**
	 * Consulta la información de asocios x tipo servicio 
	 */
	private static String listadoAsociosXTipoServicioStr= 	"SELECT " +
																					"ats.codigo, " +
																					"CASE WHEN ats.esq_tar_particular IS NULL THEN ats.esq_tar_general ||'@@@true'  ELSE ats.esq_tar_particular||'@@@false' END AS codigoEsquemaTarifarioBoolEsGeneral, " +
																					"CASE WHEN et.nombre IS NULL THEN etpc.nombre ELSE et.nombre END AS nombreEsquemaTarifario, " +
																					"ats.tipo_servicio AS acronimoTipoServicio, " +
																					"ts.nombre AS nombreTipoServicio, " +
																					"ats.tipo_asocio AS codigoTipoAsocio, " +
																					"ta.acronimo_tipo_serv AS acronimoTipoServicioAsocio, " +
																					"ta.nombre_asocio || ' (' || getnombretipoasocio(ta.tipos_servicio) || ')' AS nombreTipoServicioAsocio, " +
																					"CASE WHEN ats.servicio IS NULL THEN '-1' ELSE ats.servicio END AS codigoServicio, " +
																					"rs.descripcion AS descripcionServicio, " +
																					"CASE WHEN ats.activo ="+ValoresPorDefecto.getValorTrueParaConsultas()+" THEN 'true' ELSE 'false' END AS activo, " +
																					"" + ValoresPorDefecto.getValorTrueParaConsultas() + " AS estaBD,  " +
																					"" + ValoresPorDefecto.getValorFalseParaConsultas() + " AS esEliminada " +
																					"FROM " +
																					"asocios_tipo_serv ats " +
																					"INNER JOIN tipos_servicio ts ON (ts.acronimo= ats.tipo_servicio) " +
																					"INNER JOIN tipos_asocio ta ON (ta.codigo=ats.tipo_asocio) " +
																					"LEFT OUTER JOIN referencias_servicio rs ON (rs.servicio= ats.servicio AND rs.tipo_tarifario="+ConstantesBD.codigoTarifarioCups+") " +
																					"LEFT OUTER JOIN esquemas_tarifarios et ON (ats.esq_tar_particular=et.codigo) " +
																					"LEFT OUTER JOIN esq_tar_porcen_cx etpc ON(ats.esq_tar_general=etpc.codigo) "+
																					"WHERE " +
																					"ats.institucion=?  " +
																					"ORDER BY nombreEsquemaTarifario";
	
	/**
	 * Modifica un asocios x tipo servicio
	 */
	private static String modificarAsocioXTipoServicioStr="UPDATE asocios_tipo_serv " +
																					"SET servicio=?, " +
																					"activo=? " +
																					"WHERE " +
																					"codigo=? ";
	
		/**
	 * Método para la insercion de un asocio x tipo servicio
	 * @param con
	 * @param codigoEsquemaTarifario
	 * @param codigoEsquemaTarifarioGeneral
	 * @param acronimoTipoServicio
	 * @param codigoTipoAsocio
	 * @param codigoServicio
	 * @param activo
	 * @param codigoInstitucion
	 * @param insertarStr
	 * @return
	 */
	public static boolean   insertar(		Connection con,
														int codigoEsquemaTarifario,
														boolean esEsquemaTarifarioGeneral,
														String acronimoTipoServicio,
														int codigoTipoAsocio,
														int codigoServicio,
														boolean activo,
														int codigoInstitucion,
														String insertarStr)
	{
		try
		{
			if (con == null || con.isClosed()) 
			{
				DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
				con = myFactory.getConnection();
			}
			PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(insertarStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			if(esEsquemaTarifarioGeneral)
			{
			    System.out.print("-------->null  -"+codigoEsquemaTarifario);
			    ps.setObject(1, null);
			    ps.setInt(2, codigoEsquemaTarifario);
			}
			else
			{
			    System.out.print("-------->"+codigoEsquemaTarifario+" --null ");
			    ps.setInt(1, codigoEsquemaTarifario);
			    ps.setObject(2, null);
			}
			System.out.print("---acronimoTS="+acronimoTipoServicio+" codTipoAsocio--> "+codigoTipoAsocio+" serv->"+codigoServicio+" activo-->"+activo+" ins->"+codigoInstitucion);
			ps.setString(3, acronimoTipoServicio.trim());
			ps.setInt(4, codigoTipoAsocio);
			if(codigoServicio<=0)
			    ps.setObject(5, null);
			else
			    ps.setInt(5, codigoServicio);
			ps.setBoolean(6, activo);
			ps.setInt(7, codigoInstitucion);
			if(ps.executeUpdate()>0)
			    return true;
			else
			    return false;
		}
		catch(SQLException e)
		{
			logger.warn(e+" Error en la inserción de datos: SqlBaseAsociosTipoServicioDao "+e.toString() );
			return false;
		}
	}
	
	/**
	 * Método para la modioficacion de un grupo
	 * @param con
	 * @param grupo
	 * @param codigoAsocio
	 * @param codigoEsquemaTarifario
	 * @param codigoCups
	 * @param codigoSoat
	 * @param codigoTipoLiquidacion
	 * @param unidades
	 * @param valor
	 * @param activo
	 * @param codigoPKGrupo
	 * @return
	 */
	public static boolean modificar(		Connection con,
	        											int codigo,	
														int codigoServicio,
														boolean activo
													) 
	{
		try
		{
			if (con == null || con.isClosed()) 
			{
				throw new SQLException ("Error SQL: Conexión cerrada");
			}
				
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(modificarAsocioXTipoServicioStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			if(codigoServicio<=0)
			    ps.setObject(1, null);
			else
			    ps.setInt(1, codigoServicio);
			ps.setString(2, activo+"");
			ps.setInt(3, codigo);
			if(ps.executeUpdate()>0)
			    return true;
			else
			    return false;
		}
		catch(SQLException e)
		{
			logger.warn(e+" Error en la modificación de datos: SqlBaseAsociosTipoServicioDao "+e.toString());
			return false;			
		}	
	}
	
	/**
	 * Consulta la info de la participación del pool X tarifa dado el cod del pool 
	 * @param con
	 * @param codigoPool
	 * @return
	 */
	public static ResultSetDecorator listadoAsociosTipoServicio(Connection con, int codigoInstitucion)
	{
	    try
		{
	        PreparedStatementDecorator cargarStatement= new PreparedStatementDecorator(con.prepareStatement(listadoAsociosXTipoServicioStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			cargarStatement.setInt(1, codigoInstitucion);
			return new ResultSetDecorator(cargarStatement.executeQuery());
		}
		catch(SQLException e)
		{
			logger.warn(e+" Error en la consulta de la descripción de : SqlBaseAsociosTipoServicioDao "+e.toString());
			return null;
		}
	}
	
	/**
	 * Metodo que indoca si existe o no un registro con estas caracteristicas, debido a que el unique 
	 * en la bd no evalua en valores null
	 * @param con
	 * @param codigoEsquemaTarifario
	 * @param esEsquemaTarifarioGeneral
	 * @param acronimoTipoServicio
	 * @param codigoTipoAsocio
	 * @param codigoServicio
	 * @param activo
	 * @param codigoInstitucion
	 * @return
	 */
	public static boolean existeRegistroAsocioTipoServicio(		Connection con,
																							int codigoEsquemaTarifario,
																							boolean esEsquemaTarifarioGeneral,
																							String acronimoTipoServicio,
																							int codigoTipoAsocio,
																							int codigoServicio,
																							boolean activo,
																							int codigoInstitucion,
																							boolean esModificacion,
																							int codigo)
	{
	    String existeRegistroAsocioTipoServicioStr="SELECT codigo FROM asocios_tipo_serv WHERE ";
	    if(esEsquemaTarifarioGeneral)
	        existeRegistroAsocioTipoServicioStr+=" esq_tar_particular IS NULL AND esq_tar_general="+codigoEsquemaTarifario;
	    else
	        existeRegistroAsocioTipoServicioStr+=" esq_tar_general IS NULL AND esq_tar_particular="+codigoEsquemaTarifario;
	    existeRegistroAsocioTipoServicioStr+=" AND UPPER(tipo_servicio) = UPPER('"+acronimoTipoServicio+"') AND tipo_asocio="+codigoTipoAsocio;
	    /*if(codigoServicio>=0)
	        existeRegistroAsocioTipoServicioStr+=" AND servicio="+codigoServicio;
	    else
	        existeRegistroAsocioTipoServicioStr+=" AND servicio IS NULL";*/
	    existeRegistroAsocioTipoServicioStr+=" AND activo = '"+activo+"' AND institucion="+codigoInstitucion;
	    if(esModificacion)
	        existeRegistroAsocioTipoServicioStr+=" AND codigo<>"+codigo;
	    try
		{
	        PreparedStatementDecorator cargarStatement= new PreparedStatementDecorator(con.prepareStatement(existeRegistroAsocioTipoServicioStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			if(new ResultSetDecorator(cargarStatement.executeQuery()).next())
			    return true;
			else
			    return false;
		}
		catch(SQLException e)
		{
			logger.warn(codigoServicio+" Error en la consulta existeRegistroAsocioTipoServicio : SqlBaseAsociosTipoServicioDao "+e.toString());
			return false;
		}
	}
	
}
