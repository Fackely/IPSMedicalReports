package com.princetonsa.dao.sqlbase.inventarios;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.mundo.inventarios.AlmacenParametros;

/**
 * Parametrica Almacenes
 * @author Jose Eduardo Arias Doncel
 * */
public class SqlBaseAlmacenParametrosDao
{
	
	static Logger logger = Logger.getLogger(SqlBaseAlmacenParametrosDao.class);
	
	/**
	 * Cadena de Consulta de Parametros de Almacen 
	 * */
	private static String consultarAlmacenParametrosStr = "SELECT ap.codigo AS codigo, " +
														  "cc.nombre AS descripcion, " +
														  "cc.identificador as identificador, " +
														  "ap.institucion AS institucion, " +
														  "ap.centro_atencion AS centroatencion, " +
														  "ca.descripcion AS descripcioncentroatencion, " +														  
														  "ap.exist_negativa AS existencianegativa, " +
														  "ap.tipo_consignac AS tipoconsignacion, " +
														  "ap.plan_especial AS planEspecial, " +
														  "ap.codigo_interfaz AS codigointerfaz, " +
														  "ap.centro_costo_principal AS centrocostoprincipal, " +
														  "'"+ConstantesBD.acronimoSi+"' AS esusado, " +
														  "ap.afecta_costo_prom AS afectacostoprom " +													  
														  "FROM inventarios.almacen_parametros ap, administracion.centros_costo cc, centro_atencion ca " +
														  "WHERE ap.institucion=? AND ap.codigo=cc.codigo AND ca.consecutivo = ap.centro_atencion and cc.tipo_area="+ConstantesBD.codigoTipoAreaSubalmacen ;
	
	/**
	 * Cadena de Consulta de Centros de Costos
	 * */
	private static String consultaCentroCostosStr ="SELECT codigo AS codigo," +
													"nombre AS nombre  " +
													"FROM administracion.centros_costo" +
													"WHERE tipo_area="+ConstantesBD.codigoTipoAreaSubalmacen+" AND institucion=? " +
													"	   AND es_activo='"+ValoresPorDefecto.getValorTrueCortoParaConsultas()+"' " +
													"	   AND centro_atencion=? ";
	
	
	/**
	 * Cadema de Consulta Count de almacen Parametros
	 * */
	private static String consultaCountAlmacenParametrosStr = "SELECT COUNT (codigo) AS cuenta FROM inventarios.almacen_parametros WHERE institucion=? "; 
	
	/**
	 * Cadena de inicializacion de la tabla almacen parametros si esta se encuentra vacia
	 **/
	private static String iniciarTablaAlmacenParametrosStr = "INSERT INTO inventarios.almacen_parametros (codigo, centro_atencion, institucion, exist_negativa, tipo_consignac, plan_especial, codigo_interfaz, centro_costo_principal, afecta_costo_prom) " +
															 " 		(SELECT cc.codigo, cc.centro_atencion, ?, 'N', 'N', 'N', '',cc.codigo, 'S' FROM administracion.centros_costo cc WHERE cc.tipo_area="+ConstantesBD.codigoTipoAreaSubalmacen+
															 "         AND cc.es_activo='"+ValoresPorDefecto.getValorTrueCortoParaConsultas()+"' AND cc.institucion = ?) ";
	
	/**
	 * Cadena de insercion en almacen parametros de centro de costos no ingresados
	 **/
	private static String completarTablaAlmacenParametrosStr = "INSERT INTO inventarios.almacen_parametros (codigo, centro_atencion, institucion, exist_negativa, tipo_consignac, plan_especial, codigo_interfaz, centro_costo_principal, afecta_costo_prom) " +
															 " 		(SELECT cc.codigo, cc.centro_atencion, ?, 'N', 'N', 'N', '',cc.codigo, 'S' FROM administracion.centros_costo cc WHERE cc.tipo_area="+ConstantesBD.codigoTipoAreaSubalmacen+
															 "         AND cc.es_activo='"+ValoresPorDefecto.getValorTrueCortoParaConsultas()+"' AND cc.institucion = ? AND cc.codigo " +
															 		"  NOT IN (SELECT ap.codigo FROM inventarios.almacen_parametros ap WHERE ap.institucion = ? )) ";
	
	/**
	 * Cadena de insercion de Parametros de Almacen
	 * */
	private static String insertarAlmacenParametrosStr = "INSERT INTO inventarios.almacen_parametros (codigo, exist_negativa, tipo_consignac, plan_especial, codigo_interfaz) " +
														 " VALUES (?,?,?,?,?) ";
	
	/**
	 * Cadena de eliminacion de Parametros de Almacen
	 * */
	private static String eliminarAlmacenParametrosStr = "DELETE FROM inventarios.almacen_parametros WHERE codigo = ? AND institucion=? ";
	
	/**
	 * Cadena de modificacion de Parametros de Almacen	  
	 * */
	private static String modificarAlmacenParametros = "UPDATE inventarios.almacen_parametros SET exist_negativa=?, tipo_consignac=?, plan_especial=?, codigo_interfaz=?, centro_costo_principal=?, afecta_costo_prom = ? " +
													   " WHERE codigo=? AND institucion=? ";
	
	
	/**
	 * Cadena consulta parametro Afecta Costo Promedio
	 */
	private static String consultaAfectaCostoPromedioStr = "SELECT " +
																"afecta_costo_prom AS afectacostoprom " +
															"FROM almacen_parametros " +
															"WHERE codigo = ? AND institucion = ? ";
	/**sql
	 * Cadena de indices del HashMap  
	 * */
	private static String[] indicesAlmacenParametros = {"codigo_","descripcion_","institucion_","centroatencion_","descripcioncentroatencion_", "existencianegativa_", "tipoconsignacion_", "planespecial_", "codigointerfaz_", "centrocostoprincipal_", "estabd_",  "afectacostoprom_", "identificador_"};	
	
	
	//--- Fin Metodos
	
	
	//--- Metodos
	
	/**
	 * Consulta registros en base al codigo del centro de costos (subalmacen)
	 * @param Connection con
	 * @param int codigo del centro de costos (subalmacen)
	 * @param int centro de atencion
	 * @param int institucion (*)
	 * @return HashMap
	 * */
	@SuppressWarnings("unchecked")
	public static HashMap consultarAlmacenParametros(Connection con, int codigo, int centroAtencion,int institucion)
	{
		HashMap mapa = new HashMap();
		
		PreparedStatementDecorator ps = null;
		
		String condicion=consultarAlmacenParametrosStr;
		mapa.put("numRegistros","0");		
		
		if(codigo!=ConstantesBD.codigoNuncaValido)
			condicion+=" AND ap.codigo="+codigo;
		
		if(centroAtencion!=ConstantesBD.codigoNuncaValido)
			condicion+=" AND ap.centro_atencion="+centroAtencion;
		
		condicion+=" ORDER BY cc.nombre ASC ";		
		
		if(cuantosAlmacenParametros(con,institucion)==0)
			iniciarTableAlmacenParametros(con,institucion,1);							
		
		try
		{
			ps =  new PreparedStatementDecorator(con.prepareStatement(condicion,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1,institucion);
			mapa = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		
		}catch(SQLException e){
			logger.error("ERROR -- SQLException consultarAlmacenParametros: "+e);
		
		}catch(Exception ex){
			logger.error("ERROR -- Exception consultarAlmacenParametros: "+ex);
			
		}finally{
			try {
				if(ps != null){
					ps.close();
		}
			} catch (SQLException e) {
				logger.error("Error close PreparedStatement ");
			}
		}		
		
		mapa.put("INDICES_MAPA",indicesAlmacenParametros);
		return mapa;		
	}
	
	
	/**
	 * Inicializa la tabla almacen parametros si esta se encuentra vacia
	 * @param Connection con
	 * @param int institucion
	 * @param int opc 1 iniciar tabla, 2 completar tabla
	 * */
	public static void iniciarTableAlmacenParametros(Connection con, int institucion, int opc)
	{
		try
		{
			if(opc == 1)
			{
				PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(iniciarTablaAlmacenParametrosStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ps.setInt(1,institucion);
				ps.setInt(2,institucion);
				ps.executeUpdate();				
			}
			else if (opc == 2)
			{				
				PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(completarTablaAlmacenParametrosStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ps.setInt(1,institucion);
				ps.setInt(2,institucion);
				ps.setInt(3,institucion);
				ps.executeUpdate();				
			}							
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}	
	}
	
	/**
	 * Retorna el numero de filas de la tabla almacen parametros
	 * @param Connection con
	 * @param int institucion
	 * */
	public static int cuantosAlmacenParametros(Connection con, int institucion)
	{
		try
		{
			ResultSetDecorator rs;
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaCountAlmacenParametrosStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1,institucion);
			
			rs =new ResultSetDecorator(ps.executeQuery());
			rs.next();
			return rs.getInt("cuenta");
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}		
		return 0;		
	}
	
	
	/**
	 * Consulta centro de Costos
	 * @param Connection con
	 * @param int institucion
	 * @return HashMap
	 * */
	@SuppressWarnings("unchecked")
	public static HashMap consultarCentroCostos(Connection con, int institucion, int centroAtencion)
	{
		HashMap mapa = new HashMap();
		mapa.put("numRegistros","0");
		
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaCentroCostosStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1,institucion);
			ps.setInt(2,centroAtencion);
			
			mapa = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));			
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		
		return mapa;
	}
		
	
	/**
	 * Inserta un regitros de parametros de almacen
	 * @param Connection con
	 * @param AlmacenProcedimiento almacenProcedimiento
	 * */	
	public static boolean insertarAlmacenParametros(Connection con, AlmacenParametros almacenParametros)
	{		
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(insertarAlmacenParametrosStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			/**
			 * INSERT INTO inventarios.almacen_parametros (codigo, exist_negativa, tipo_consignac, plan_especial, codigo_interfaz) 
			 */
			
			ps.setInt(1,almacenParametros.getCodigo());
			ps.setString(2,almacenParametros.getExist_negativa());
			ps.setString(3,almacenParametros.getTipo_consignac());
			ps.setString(4, almacenParametros.getPlan_especial());
			ps.setString(5,almacenParametros.getCodigo_interfaz());
			
			if(ps.executeUpdate()>0)
				return true;
		}
		catch(SQLException e)
		{
			e.printStackTrace();			
		}
		
		return false;
	}
	
	
	/**
	 * Elimina un regitros de parametros de almacen
	 * @param Connection con
	 * @param AlmacenProcedimiento almacenProcedimiento
	 * */	
	public static boolean eliminarAlmacenParametros(Connection con, int codigo, int institucion )
	{		
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(eliminarAlmacenParametrosStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1,codigo);
			ps.setInt(2,institucion);
			
			if(ps.executeUpdate()>0)		
				return true;
		}
		catch(SQLException e)
		{
			e.printStackTrace();			
		}
		
		return false;
	}

	
	/**
	 * Modifica un registro de la base de datos
	 * @param Connection con
	 * @param AlmacenParametros almacenParametros 
	 * */
	public static boolean modificarAlmacenParametros(Connection con, AlmacenParametros almacenParametros)
	{		
		logger.info("====>Centro de Costo Principal: "+almacenParametros.getCentro_costo_principal());

		PreparedStatementDecorator ps = null;
		
		try
		{
			ps =  new PreparedStatementDecorator(con.prepareStatement(modificarAlmacenParametros,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1,almacenParametros.getExist_negativa());
			ps.setString(2,almacenParametros.getTipo_consignac());
			ps.setString(3, almacenParametros.getPlan_especial());
			ps.setString(4,almacenParametros.getCodigo_interfaz());
			if(Utilidades.convertirAEntero(almacenParametros.getCentro_costo_principal())>0)
				ps.setInt(5, Utilidades.convertirAEntero(almacenParametros.getCentro_costo_principal()));
			else
				ps.setObject(5,null);
			
			ps.setString(6,almacenParametros.getAfectaCostoPromedio());
			
			ps.setInt(7,almacenParametros.getCodigo());
			ps.setInt(8,almacenParametros.getInstitucion());
			
			if(ps.executeUpdate()>0)
				return true;
			
		}catch(SQLException e){
			logger.error("ERROR -- SQLException modificarAlmacenParametros: "+e);			
		
		}catch(Exception ex){
			logger.error("ERROR -- Exception modificarAlmacenParametros: "+ex);
			
		}finally{
			try{
				if(ps!=null){
					ps.close();
		}
			}catch(SQLException e){
				logger.error("Error close PreparedStatement ");
			}
		}
		
		return false;
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoAlmacen
	 * @param codigoInstitucion
	 * @return
	 */
	public static boolean manejaExistenciasNegativas(Connection con, int codigoAlmacen, int codigoInstitucion)
	{
		String consulta= "SELECT exist_negativa as existnegativa from almacen_parametros a where codigo=? and institucion=?";
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1,codigoAlmacen);
			ps.setInt(2,codigoInstitucion);
			
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
				return UtilidadTexto.getBoolean(rs.getString("existnegativa"));
		}
		catch(SQLException e)
		{
			logger.info("Error en manejaExistenciasNegativas");
			e.printStackTrace();
		}
		//si no existe parametrizacion significa que N
		return false;
	}
		
	/**
	 * 
	 * @param con
	 * @param codigoAlmacen
	 * @param codigoInstitucion
	 * @return
	 */
	public static boolean manejaExistenciasNegativasCentroAten(Connection con, int centroCosto, int codigoInstitucion)
	{
		String consulta= "SELECT exist_negativa as existnegativa from almacen_parametros where codigo=? and institucion=?";
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1,centroCosto);
			ps.setInt(2,codigoInstitucion);
			
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
				return UtilidadTexto.getBoolean(rs.getString("existnegativa"));
		}
		catch(SQLException e)
		{
			logger.info("Error en manejaExistenciasNegativas");
			e.printStackTrace();
		}
		//si no existe parametrizacion significa que N
		return false;
	}
		
	/**
	 * Metodo que consulta el parametro "Afecta Costo Promedio"
	 * @param con
	 * @param codigoAlmacen
	 * @param codigoInstitucion
	 * @return
	 */
	public static boolean afectaCostoPromedio(Connection con, int codigoAlmacen, int codigoInstitucion){
		
		PreparedStatementDecorator ps = null;
		ResultSetDecorator rs = null;
		
		try{
			
			ps = new PreparedStatementDecorator(con.prepareStatement(consultaAfectaCostoPromedioStr, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			ps.setInt(1,codigoAlmacen);
			ps.setInt(2,codigoInstitucion);
			
			rs=new ResultSetDecorator(ps.executeQuery());
			
			if(rs.next())
				return UtilidadTexto.getBoolean(rs.getString("afectacostoprom"));
			
		}catch(SQLException e){
			logger.error("ERROR -- SQLException afectaCostoPromedio: ", e);
			
		}catch(Exception ex){
			logger.error("ERROR -- Exception afectaCostoPromedio: ", ex);
			
		}finally{
			try{
				if(ps != null){
					ps.close();
				}
				if(rs != null){
					rs.close();
				}
			}catch(SQLException e){
				logger.error("Error close PreparedStatement - ResultSet", e);
			}
		}
		
		return false;
	}
	
	//-- Fin Metodos
}