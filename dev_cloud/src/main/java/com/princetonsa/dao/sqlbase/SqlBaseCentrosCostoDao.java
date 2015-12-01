
package com.princetonsa.dao.sqlbase;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import org.apache.log4j.Logger;

import com.princetonsa.dao.DaoFactory;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.Utilidades;
import util.ValoresPorDefecto;

/**
 * @author <a href="mailto:cperalta@PrincetonSA.com">Carlos Peralta</a>
 * Clase para las transacciones de Centros de Costo
 * @version 1.0  10 /May/ 2006
 */
public class SqlBaseCentrosCostoDao 
{
	/**
	 * Manejador de logs de la clase
	 */
	private static Logger logger=Logger.getLogger(SqlBaseCentrosCostoDao.class);
	
	/**
	 * Cadena con el statement necesario para consultar los centros de costo asociados a un centro de atencion y a una institucion
	 */
	private final static String consultarCentrosCostoStr=" SELECT cc.codigo as codigo, " +
														 " cc.identificador as identificador, " +
														 " cc.nombre as descripcion, " +
														 " cc.tipo_area as codigotipoarea, " +
														 " ta.nombre as nombretipoarea, " +
														 " cc.manejo_camas as manejocamas, " +
														 " cc.reg_res_porc_ter as reg_respx_tercer, "+
														 " cc.unidad_funcional as unidadfuncional, " +
														 " cc.codigo_interfaz as codigo_interfaz, " +
														 " uf.descripcion as nombreunidadfuncional, " +
														 " cc.tipo_entidad_ejecuta as tipoEntidad, " +
														 " case when cc.es_activo ="+ValoresPorDefecto.getValorTrueParaConsultas()+" then 'true' else 'false' end as activo, " +
														 " 'si' as existebd "+
														 " FROM centros_costo cc " +
														 " INNER JOIN tipos_area ta ON(cc.tipo_area=ta.codigo) " +
														 " INNER JOIN unidades_funcionales uf ON(cc.unidad_funcional=uf.acronimo and cc.institucion=uf.institucion)  " +
														 " WHERE cc.centro_atencion = ? " +
														 " AND cc.institucion = ? and cc.codigo<>"+ConstantesBD.codigoNuncaValido +
														 " AND cc.codigo<>"+ConstantesBD.codigoCentroCostoTodos  +
														 " AND cc.codigo<>"+ConstantesBD.codigoCentroCostoExternos +
														 " ORDER BY cc.nombre asc ";
	
	/**
	 * Cadena con el statement necesario para consultar los centros de costo asociados a  una institucion
	 */
	private final static String consultarCentrosCostoGeneralesStr=" SELECT cc.codigo as codigo, " +
														 " cc.identificador as identificador, " +
														 " cc.nombre as descripcion, " +
														 " cc.tipo_area as codigotipoarea, " +
														 " ta.nombre as nombretipoarea, " +
														 " cc.manejo_camas as manejocamas, " +
														 " cc.reg_res_porc_ter as reg_respx_tercer, "+
														 " cc.unidad_funcional as unidadfuncional, " +
														 " cc.codigo_interfaz as codigo_interfaz, " +
														 " uf.descripcion as nombreunidadfuncional, " +
														 " case when cc.es_activo ="+ValoresPorDefecto.getValorTrueParaConsultas()+" then 'true' else 'false' end as activo, " +
														 " cc.tipo_entidad_ejecuta as tipoEntidad, " +
														 " 'si' as existebd "+
														 " FROM centros_costo cc " +
														 " INNER JOIN tipos_area ta ON(cc.tipo_area=ta.codigo) " +
														 " INNER JOIN unidades_funcionales uf ON(cc.unidad_funcional=uf.acronimo and cc.institucion=uf.institucion)  " +
														 " WHERE cc.institucion = ? and cc.codigo<>"+ConstantesBD.codigoNuncaValido +
														 " AND cc.codigo<>"+ConstantesBD.codigoCentroCostoTodos  +
														 " AND cc.codigo<>"+ConstantesBD.codigoCentroCostoExternos +
														 " ORDER BY cc.nombre asc ";
	
	
	/**
	 * Hace la modificación de los datos de los centros de costo
	 */
	private final static String modificarCentroCostoStr=" UPDATE centros_costo SET " +
														" identificador= ? , " +
														" nombre = ? ," +
														" tipo_area = ? ," +
														" manejo_camas = ?," +
														" unidad_funcional = ? ," +
														" codigo_interfaz =? ," +
														" es_activo = ?, " +
														" reg_res_porc_ter= ?, "+
														" tipo_entidad_ejecuta= ? "+
														" WHERE codigo = ?";
	
	/**
	 * Cadena con el statement necesario para saber si existe un centro de costo determinado
	 */
	private final static String existeCentroCostoStr=" SELECT count(1) as cantidad " +
													 " FROM centros_costo " +
													 " WHERE identificador = ? " +
													 " AND centro_atencion = ? " +
													 " AND institucion = ?" +
													 " AND codigo = ? ";
	/**
	 * Cadena con el statement necesario para saber si existe un centro de costo determinado
	 */
	private final static String existeCentroCostoStr2=" SELECT count(1) as cantidad " +
													 " FROM centros_costo " +
													 " WHERE centro_atencion = ? " +
													 " AND institucion = ?" +
													 " AND codigo = ? ";
	
	/**
	 * Cadena con el statement necesario para eliminar un centro de costo dado su codigo
	 */
	private final static String eliminarCentroCostoStr=" DELETE FROM centros_costo WHERE codigo = ? ";
	
	/**
	 * Cadena con el statement necesario para eliminar un centro de costo dado su codigo en la tabla almcen_parametros.
	 */
	private final static String eliminarAlmacenStr=" DELETE FROM almacen_parametros WHERE codigo = ? ";
	
	/**
	 * Método para consultar los centros de costo asociados a un centro de atencion y una
	 * institucion en especifico
	 * @param con
	 * @param centroatencion
	 * @param institucion
	 * @return
	 * @throws SQLException
	 */
	public static HashMap consultarCentrosCosto(Connection con , int centroatencion, int institucion) throws SQLException
	{
		try
		{
			if(centroatencion==ConstantesBD.codigoNuncaValido)
			{
				PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consultarCentrosCostoGeneralesStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				pst.setInt(1, institucion);
				logger.info("Instituccion>>"+ institucion);
			    logger.info("String sql Consulta>>"+ consultarCentrosCostoGeneralesStr);
				
				HashMap mapaRetorno= UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()),true,false);
				pst.close();
				return mapaRetorno;
			}
			else
			{
				PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultarCentrosCostoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ps.setInt(1, centroatencion);
				ps.setInt(2, institucion);
				logger.info("centro atencion>>"+ centroatencion);
				logger.info("instituccion>>"+ institucion);
				logger.info("String sql Consulta>>"+ consultarCentrosCostoStr);
				
				HashMap mapaRetorno= UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()),true,false);
				ps.close();
				return mapaRetorno;
			}
		
		}
		catch(SQLException e)
		{
			logger.warn(e+"Error en consultarCentrosCosto : [SqlBaseCentrosCostoDao] "+e.toString() );
			
		}
		return null;
	}
	
	
	/**
	 * Método para eliminar un centro de costo dado su codigo
	 * @param con
	 * @param centroCosto
	 * @return
	 */
	public static int eliminarCentroCosto(Connection con, int centroCosto) throws SQLException
	{
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(eliminarCentroCostoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, centroCosto);
			return ps.executeUpdate();
		}
		catch(SQLException e)
		{
			logger.warn(e+"Error en eliminarCentroCosto : [SqlBaseCentrosCostoDao] "+e.toString() );
			return Integer.parseInt(e.getSQLState());
		}
		
	}
	
	
	/**
	 * Método para modificar un centro de costo dado su codigo
	 * @param con
	 * @param identificador
	 * @param descripcion
	 * @param codigoTipoArea
	 * @param manejoCamas
	 * @param unidadFuncional
	 * @param activo
	 * @param codigo
	 * @param tipoEntidad 
	 * @return
	 */
	public static int modificarCentroCosto(Connection con, String identificador, String descripcion, int codigoTipoArea, String reg_resp, boolean manejoCamas, int unidadFuncional, String codigo_interfaz, boolean activo, int codigo, String tipoEntidad) throws SQLException 
	{
		int resp=0;	
		try
		{
			if (con == null || con.isClosed()) 
			{
				throw new SQLException ("Error SQL: Conexión cerrada");
			}
			
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(modificarCentroCostoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			ps.setString(1, identificador);
			ps.setString(2, descripcion);
			ps.setInt(3, codigoTipoArea);
			ps.setBoolean(4, manejoCamas);
			ps.setInt(5, unidadFuncional);
			ps.setString(6, codigo_interfaz);
			ps.setBoolean(7, activo);
			ps.setString(8, reg_resp);
			ps.setString(9,tipoEntidad);
			ps.setInt(10, codigo);
			
			resp=ps.executeUpdate();
		}
		catch(SQLException e)
		{
			logger.warn(e+" Error en modificarCentroCosto : [SqlBaseCentrosCostoDao] "+e.toString());
			resp=0;			
		}	
		return resp;	
	}
	
	/**
	 * Método para la insercion de un nuevo Centro de Costo con todos sus atributos
	 * @param con
	 * @param codigocentrocosto
	 * @param nombre
	 * @param codigoTipoArea
	 * @param institucion
	 * @param activo
	 * @param identificador
	 * @param manejoCamas
	 * @param unidadFuncional
	 * @param centroAtencion
	 * @param insertarCentroCostoStr -> Postgres - Oracle
	 * @param tipoEntidad 
	 * @return
	 */
	public static int insertarCentrosCosto(Connection con, int codigoCentroCosto, String nombre, int codigoTipoArea,String reg_resp, int institucion, boolean activo, String identificador, boolean manejoCamas, String unidadFuncional, String codigo_interfaz, int centroAtencion, String insertarCentroCostoStr, String tipoEntidad) throws SQLException
	{
	   ResultSetDecorator rs ;
	   int resp = 0;
	   int temp = 0;
	  
	    if(reg_resp.equals("")){
	    	reg_resp=ConstantesBD.acronimoNo;
	    }
	    
		try
		{
			if (con == null || con.isClosed()) 
			{
				DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
				con = myFactory.getConnection();
			}
			
			if(codigoCentroCosto>0)
			{
				PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(existeCentroCostoStr2,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				// Comentado por tarea 35344
				//pst.setString(1, identificador);
				pst.setInt(1, centroAtencion);
				pst.setInt(2, institucion);
				pst.setInt(3, codigoCentroCosto);
				rs=new ResultSetDecorator(pst.executeQuery());
				//logger.info("PARAM 1 ->  "+centroAtencion);
				//logger.info("PARAM 2 ->  "+institucion);
				//logger.info("PARAM 3 ->  "+codigoCentroCosto);
				//logger.info("CONSULTA SI EXISTE EL REGISTRO --- > "+existeCentroCostoStr);
				if(rs.next())
				{
					temp=rs.getInt("cantidad");
				}
			}
			logger.info("¿Existe centro de costo "+nombre+" con codigo "+codigoCentroCosto+"? "+temp);
			//Si existe un centro de costo lo que hacemos es modificarlo
			if(temp>0)
			{
				PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(modificarCentroCostoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				
				
				ps.setString(1, identificador);
				
				ps.setString(2, nombre);
				
				ps.setInt(3, codigoTipoArea);
				
				ps.setBoolean(4, manejoCamas);
				
				ps.setString(5, unidadFuncional);
				
				ps.setString(6, codigo_interfaz.trim());
				
				ps.setBoolean(7, activo);
				
				if(!reg_resp.equals(""))
				{
					ps.setString(8, reg_resp);
				
				}
				else
				{
					ps.setString(8, ConstantesBD.acronimoNo);
				
				}
				if(tipoEntidad.equals("") || tipoEntidad.equals(null) || tipoEntidad.equals(ConstantesBD.codigoNuncaValido+""))
				{
					
				tipoEntidad=ConstantesIntegridadDominio.acronimoInterna;
				ps.setString(9,tipoEntidad);
				
				}else
				{
					ps.setString(9,tipoEntidad);
				
				}
				
				ps.setInt(10, codigoCentroCosto);
				
			
				
				resp = ps.executeUpdate();
			}
			else
			{
				//Insertamos un nuevo Centro de Costo nuevo
				PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(insertarCentroCostoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				
				ps.setString(1, nombre);
				ps.setInt(2, codigoTipoArea);
				ps.setInt(3, institucion);
				ps.setBoolean(4, activo);
				ps.setString(5, identificador);
				ps.setBoolean(6, manejoCamas);
				ps.setString(7, unidadFuncional);
				ps.setString(8, codigo_interfaz.trim());
				if(!reg_resp.equals(""))
					ps.setString(9, reg_resp);
				else
					ps.setString(9,ConstantesBD.acronimoNo);
				ps.setInt(10, centroAtencion);
				/*
				 * Solución de la tarea 1522,
				 * Cuando el centro de costo se intenta ingresar con tipo área = indirecto,
				 * el tipo entidad viene vacío, entonces se realiza la inserción de un null String.
				 * Ayuda validad con Sebastián Gómez. 
				 */
				if(tipoEntidad.equals("") || tipoEntidad.equals(null) || tipoEntidad.equals(ConstantesBD.codigoNuncaValido+""))
				{
					ps.setNull(11, Types.VARCHAR);
				}
				else
				{
					ps.setString(11,tipoEntidad);					
				}
				logger.info("insert de centros de costo=> "+insertarCentroCostoStr+"\nnombre: "+nombre+"\ncodigoTipoArea: "+codigoTipoArea+ "\nRegistroProced: "+ reg_resp +"\ninstitucion: "+institucion+"\nactivo: "+activo+"\nidentificador:  "+identificador+"\nmanejoCamas: "+manejoCamas+"\n unidadFuncional: "+unidadFuncional+"\n centroAtecnio: "+centroAtencion+"\n Tipo Entidad :"+tipoEntidad);
				resp = ps.executeUpdate();
			}
		}
		catch(SQLException e)
		{
				logger.warn(e+" Error en insertarCentrosCosto : [SqlBaseCentrosCostoDao] "+e.toString() );
				resp=0;
		}
		return resp;
	}
	
	/**
	 * 
	 * @param con
	 * @param almacen
	 * @return
	 */
	public static int eliminarAlmacen(Connection con, int almacen) 
	{
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(eliminarAlmacenStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, almacen);
			return ps.executeUpdate();
		}
		catch(SQLException e)
		{
			logger.warn(e+"Error en eliminarCentroCosto : [SqlBaseCentrosCostoDao] "+e.toString() );
			return Integer.parseInt(e.getSQLState());
		}
	}


	/**
	 * 
	 * @param con
	 * @param institucion
	 * @param centroAtencion
	 * @return
	 */
	public static HashMap consultarCentrosGrupoServicios(Connection con, int centroAtencion) 
	{
		String consulta =" SELECT " +
		 		   " DISTINCT ccgs.centro_costo as codigocentrocosto, " +
		 		   " cc.nombre as nombrecentrocosto " +
		 		   " FROM centro_costo_grupo_ser ccgs " +
		 		   " INNER JOIN centros_costo cc ON(ccgs.centro_costo=cc.codigo) " +
		 		   " WHERE cc.centro_atencion = "+centroAtencion+" "+
				   " ORDER BY nombrecentrocosto ASC ";
		
		logger.info("\n\nconsultarCentrosCostoViaIngreso-->"+consulta+"\n\n");
		
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			HashMap mapaRetorno= UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()),true,false);
			ps.close();
			return mapaRetorno;
		}
		catch(SQLException e)
		{
			logger.warn(e+"Error en consultarCentrosCostO : [consultarCentrosGrupoServicios] "+e.toString() );
		}
		return null;
	}
	
}

