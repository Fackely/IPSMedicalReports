/*
 * @(#)SqlBaseRadicacionCuentasCobroCapitacionDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */
package com.princetonsa.dao.sqlbase.capitacion;

import java.sql.Connection;
import java.sql.Date;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.Utilidades;

import com.princetonsa.dao.DaoFactory;

/**
 * Implementación sql genérico de todas las funciones de acceso a la fuente de datos
 * para la radicacion cuentas cobro capitacion
 *
 * @version 1.0, Junio 30 / 2006
 * @author <a href="mailto:wilson@PrincetonSA.com">Wilson Ríos</a>
 */
public class SqlBaseRadicacionCuentasCobroCapitacionDao 
{
	/**
	* Objeto para manejar los logs de esta clase
	*/
	private static Logger logger = Logger.getLogger(SqlBaseRadicacionCuentasCobroCapitacionDao.class);
	
	/**
	 * inserta la radicacion
	 */
	private static String insertarRadicacionCxCStr="UPDATE cuentas_cobro_capitacion SET fecha_radicacion=?, numero_radicacion=?, usuario_radica=?, obs_radicacion=?, estado= ? where numero_cuenta_cobro=? and institucion=?";
	
	/**
	 * Busqueda de las cuentas cobro a radicar
	 * @param con
	 * @param criteriosBusquedaMap ( keys= cuentaCobro, codigoConvenio, fechaInicial, fechaFinal), 
	 * 								las fechas deben estar en formato aplicacion
	 * @return
	 * @throws SQLException
	 */
	public static Collection busquedaCuentasCobroARadicar(	Connection con,
															HashMap criteriosBusquedaMap
														 )throws SQLException
	{
		String consultaArmada="";
		if(con==null || con.isClosed())
		{
			try
			{
				DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
				con = myFactory.getConnection();
			}
			catch(SQLException e)
			{
				logger.warn("No se pudo realizar la conexión "+e.toString());
				return null;
			}
		}
		PreparedStatementDecorator ps= null;
		try
		{
			consultaArmada=armarConsulta(	 criteriosBusquedaMap	 );
																	 
			ps=  new PreparedStatementDecorator(con.prepareStatement(consultaArmada));
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			return UtilidadBD.resultSet2Collection(rs);
		}
		catch(SQLException e)
		{
			logger.warn("Error en la búsqueda avanzada de radicacion cuentas cobro capitacion " +e.toString());
			return null;
		}finally{
			try{
				if(ps!=null){
					ps.close();					
				}
			}catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUnidadPagoDao "+sqlException.toString() );
			}
			
			
		}
	}

	/**
	 * Método que arma la consulta según los datos dados por el usuarios en 
	 * la búsqueda avanzada. 
	 * @param criteriosBusquedaMap
	 * @return
	 */
	private static String armarConsulta  (	HashMap criteriosBusquedaMap )
	{
		String consulta=	"SELECT " +
							"ccc.numero_cuenta_cobro AS numeroCuentaCobro, " +
							"ccc.convenio AS codigoConvenio, " +
							"c.nombre AS nombreConvenio, " +
							"to_char(ccc.fecha_inicial, 'DD/MM/YYYY') ||' '|| to_char(ccc.fecha_final, 'DD/MM/YYYY') AS fechaInicialFinal, " +
							"ccc.estado AS codigoEstado, " +
							"ec.descripcion AS descripcionEstado, " +
							"CASE WHEN ccc.saldo_cuenta IS NULL THEN 0 ELSE ccc.saldo_cuenta END AS saldoCuenta, " +
							"to_char(ccc.fecha_elaboracion, 'DD/MM/YYYY') AS fechaElaboracionCuentaCobro, " +
							"getCarguesAjustesPendientes(ccc.numero_cuenta_cobro, ccc.institucion, "+ConstantesBD.codigoEstadoAjusteCxCPendiente+", '-') AS carguesAjustesPendientes, " +
							"getConsecutivosAjustesCxC(ccc.numero_cuenta_cobro, ccc.institucion, "+ConstantesBD.codigoEstadoAjusteCxCPendiente+", '-') AS consecutivosAjustesPendientes " +
							"FROM " +
							"cuentas_cobro_capitacion ccc " +
							"INNER JOIN convenios c ON (c.codigo=ccc.convenio) " +
							"INNER JOIN estados_cartera ec ON(ec.codigo=ccc.estado) WHERE 1=1 ";
		
		
		if(criteriosBusquedaMap.containsKey("cuentaCobro"))
		{
			if(!criteriosBusquedaMap.get("cuentaCobro").toString().equals("") && !criteriosBusquedaMap.get("cuentaCobro").toString().equals("null"))
			{
				consulta+=" AND ccc.numero_cuenta_cobro = "+criteriosBusquedaMap.get("cuentaCobro").toString();
			}
		}
		
		if(criteriosBusquedaMap.containsKey("codigoConvenio"))
		{
			if(!criteriosBusquedaMap.get("codigoConvenio").toString().equals("") && !criteriosBusquedaMap.get("codigoConvenio").toString().equals("null"))
			{
				consulta+=" AND ccc.convenio = "+criteriosBusquedaMap.get("codigoConvenio").toString();
			}
		}
		
		if(criteriosBusquedaMap.containsKey("fechaInicial") && criteriosBusquedaMap.containsKey("fechaFinal"))
		{
			if(!criteriosBusquedaMap.get("fechaInicial").toString().equals("") && !criteriosBusquedaMap.get("fechaInicial").toString().equals("null")
				&& !criteriosBusquedaMap.get("fechaFinal").toString().equals("") && !criteriosBusquedaMap.get("fechaFinal").toString().equals("null"))
			{
				consulta+=	" AND (" +
									"('"+UtilidadFecha.conversionFormatoFechaABD(criteriosBusquedaMap.get("fechaInicial").toString())+"' <= ccc.fecha_inicial "+
									"and '"+UtilidadFecha.conversionFormatoFechaABD(criteriosBusquedaMap.get("fechaFinal").toString())+"' >= "+" ccc.fecha_final " +
									") " +
									"or "+
									"('"+UtilidadFecha.conversionFormatoFechaABD(criteriosBusquedaMap.get("fechaInicial").toString())+"' >= ccc.fecha_inicial "+
									"and '"+UtilidadFecha.conversionFormatoFechaABD(criteriosBusquedaMap.get("fechaFinal").toString())+"' <= "+" ccc.fecha_final " +
									") " +
									"or " +
									"('"+UtilidadFecha.conversionFormatoFechaABD(criteriosBusquedaMap.get("fechaInicial").toString())+"' >= ccc.fecha_inicial " +
									" and '"+UtilidadFecha.conversionFormatoFechaABD(criteriosBusquedaMap.get("fechaInicial").toString())+"' <= ccc.fecha_final " +
									"and '"+UtilidadFecha.conversionFormatoFechaABD(criteriosBusquedaMap.get("fechaFinal").toString())+"' >= "+" ccc.fecha_final " +
									")or " +
									"('"+UtilidadFecha.conversionFormatoFechaABD(criteriosBusquedaMap.get("fechaInicial").toString())+"' <= ccc.fecha_inicial " +
									" and '"+UtilidadFecha.conversionFormatoFechaABD(criteriosBusquedaMap.get("fechaFinal").toString())+"' >= ccc.fecha_inicial " +
									") " +
								") ";
				
							
			}
		}
		
		consulta+=" ORDER BY numeroCuentaCobro ASC";						
								
		
		return consulta;	
	}	
	
	/**
	 * metodo que inserta la radicacion de cuentas cobro capitacion
	 * @param fechaRadicacionFormatApp
	 * @param numeroRadicacion
	 * @param loginUsuario
	 * @param observaciones
	 * @param institucion
	 * @return
	 */
	public static boolean insertarRadicacionCxC (	Connection con, String fechaRadicacionFormatApp, 
													String numeroRadicacion, String loginUsuario, 
													String observaciones, String numeroCuentaCobro, 
													int institucion)
	{
		PreparedStatementDecorator ps= null;
		try
		{
			if (con == null || con.isClosed()) 
			{
				throw new SQLException ("Error SQL: Conexión cerrada");
			}
			ps= new PreparedStatementDecorator(con.prepareStatement(insertarRadicacionCxCStr));
			
			ps.setDate(1,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(fechaRadicacionFormatApp)));
			ps.setString(2, numeroRadicacion);
			ps.setString(3, loginUsuario);
			ps.setString(4,observaciones);
			ps.setInt(5,ConstantesBD.codigoEstadoCarteraRadicada);
			ps.setDouble(6,Utilidades.convertirADouble(numeroCuentaCobro));
			ps.setInt(7,institucion);
			if(ps.executeUpdate()>0)
			{
				return true;
			}	
		}
		catch(SQLException e)
		{
			logger.warn(e+" Error en la inserción de datos: SqlBaseRadicacionCuentasCobroCapitacionDao "+e.toString());
		}	finally{
			try{
				if(ps!=null){
					ps.close();					
				}
			}catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseUnidadPagoDao "+sqlException.toString() );
			}
			
			
		}
		return false;
	}
}
