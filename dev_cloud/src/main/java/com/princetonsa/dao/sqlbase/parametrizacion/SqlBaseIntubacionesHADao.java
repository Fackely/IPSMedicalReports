package com.princetonsa.dao.sqlbase.parametrizacion;

import java.sql.Connection;
import java.sql.Date;
import com.princetonsa.decorator.PreparedStatementDecorator;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.princetonsa.decorator.ResultSetDecorator;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.Utilidades;
import util.ValoresPorDefecto;

/**
 * 
 * @author wilson
 *
 */
public class SqlBaseIntubacionesHADao 
{
	/**
	* Objeto para manejar los logs de esta clase
	*/
	private static Logger logger= Logger.getLogger(SqlBaseIntubacionesHADao.class);
	
	/**
	 * 
	 * @param con
	 * @param mapa
	 * @return
	 */
    public static int insertar(Connection con, HashMap<Object, Object> mapa)
    {
    	String cadena="INSERT INTO intubaciones_hoja_anes 	(codigo, " +     			//1
    														"numero_solicitud, " +		//2
    														"tipo_intubacion, " +		//3
    														"clas_cormack_lehane, "+	//4
    														"fecha, " +					//5
    														"hora, " +					//6
    														"institucion, "+			//7				
    														"fecha_modifica, " +		
    														"hora_modifica, " +			
    														"usuario_modifica " +		//8
    														") " +				
    														"values (?, ?, ?," +
    																" ?, ?, ?, ?, " +
    																"CURRENT_DATE, " +
    																""+ValoresPorDefecto.getSentenciaHoraActualBD()+", " +
    																"?)";
    	
    	try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			int codigo= UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_intubacion_hoja_anes");
			ps.setInt(1, codigo);
			ps.setInt(2, Utilidades.convertirAEntero(mapa.get("numerosolicitud").toString()));
			ps.setInt(3, Utilidades.convertirAEntero(mapa.get("tipointubacion").toString()));
			ps.setInt(4, Utilidades.convertirAEntero(mapa.get("clascormacklehane").toString()));
			ps.setDate(5, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(mapa.get("fecha").toString())));
			ps.setString(6, mapa.get("hora")+"");
			ps.setInt(7, Utilidades.convertirAEntero(mapa.get("institucion")+""));
			ps.setString(8, mapa.get("usuariomodifica")+"");
				
			if(ps.executeUpdate()>0)
				return codigo;
			
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return ConstantesBD.codigoNuncaValido;
    } 
    
    /**
     * 
     * @param con
     * @param codigoIntubacionHojaAnestesia
     * @return
     */
    public static boolean eliminar(Connection con, int codigoIntubacionHojaAnestesia)
    {
    	String cadena="DELETE FROM intubaciones_hoja_anes WHERE codigo=? ";
		logger.info("\n ELIMINAR-->"+cadena+" intu-->"+codigoIntubacionHojaAnestesia+"\n");
    	
    	try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, codigoIntubacionHojaAnestesia);
			
			if(ps.executeUpdate()>0)
				return true;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return false;
    }
    
    /**
     * 
     * @param con
     * @param mapa
     * @return
     */
    public static boolean insertarDetalle(Connection con, HashMap<Object, Object> mapa)
    {
    	
        String cadena="INSERT INTO det_intubacion_hoja_anes_mul (intubacion_hoja_anes, " +     	//1
    															"tipo_intubacion_multiple, " +	//2
    															"usuario_modifica, "+			//3
    															"fecha_modifica, " +//4
    															"hora_modifica) " +	//5
    															"values (?, ?, ?," +
    																" CURRENT_DATE, "+ValoresPorDefecto.getSentenciaHoraActualBD()+" " +
    																")";
    	
    	try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, Utilidades.convertirAEntero(mapa.get("intubacionhojaanes")+""));
			ps.setInt(2, Utilidades.convertirAEntero(mapa.get("tipointubacionmultiple").toString()));
			ps.setString(3, mapa.get("usuariomodifica")+"");
			
			if(ps.executeUpdate()>0)
				return true;
			
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return false;
    }
    
    /**
     * si tipo_intubacion_multiple es <=0 borra todos las intubaciones  
     * @param con
     * @param viaAerea
     * @param articulo
     * @return
     */
    public static boolean eliminarDetalle(Connection con, int codigoIntubacionHojaAnes, int tipoIntubacionMultiple)
    {
    	String cadena="DELETE FROM det_intubacion_hoja_anes_mul WHERE intubacion_hoja_anes=? ";
		
    	if(tipoIntubacionMultiple>0)
    		cadena+=" AND tipo_intubacion_multiple = "+tipoIntubacionMultiple;
    	
		logger.info("\n ELIMINAR-->"+cadena+" intu-->"+codigoIntubacionHojaAnes+"\n");
    	
    	try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, codigoIntubacionHojaAnes);
			
			if(ps.executeUpdate()>0)
				return true;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return false;
    }
    
    /**
     * 
     * @param con
     * @param centroCosto
     * @param institucion
     * @return
     */
    public static HashMap<Object, Object> cargarTiposIntubacion( Connection con, int centroCosto, int institucion)
    {
    	HashMap<Object, Object> mapa=cargarTiposIntubacionPrivado(con, centroCosto, institucion);
    	if(Utilidades.convertirAEntero(mapa.get("numRegistros").toString())<=0)
    		mapa=cargarTiposIntubacionPrivado(con, ConstantesBD.codigoNuncaValido /*centroCosto*/, institucion);
    	return mapa;
    }
    
    /**
     * 
     * @param con
     * @param centroCosto
     * @param institucion
     * @return
     */
    private static HashMap<Object, Object> cargarTiposIntubacionPrivado( Connection con, int centroCosto, int institucion)
    {
    	HashMap<Object, Object> mapa= new HashMap<Object, Object>();
    	mapa.put("numRegistros","0");
    	String consulta=""; 
    		
    	if(centroCosto>0)
    	{	
    		consulta+="SELECT " +
    						"ti.codigo, " +
    						"ti.nombre " +
    					"FROM " +
    						"tipos_intubacion ti " +
    						"INNER JOIN tipos_intubacion_inst_cc tiicc ON (tiicc.tipo_intubacion=ti.codigo) " +
    					"WHERE " +
    						"tiicc.centro_costo="+centroCosto+" and institucion=? and activo='"+ConstantesBD.acronimoSi+"' " +
    						"order by ti.nombre ";
    	}
    	else
    	{
    		consulta+="SELECT " +
							"ti.codigo, " +
							"ti.nombre " +
						"FROM " +
							"tipos_intubacion ti " +
							"INNER JOIN tipos_intubacion_inst_cc tiicc ON (tiicc.tipo_intubacion=ti.codigo) " +
						"WHERE " +
							"tiicc.centro_costo is null and institucion=? and activo='"+ConstantesBD.acronimoSi+"' " +
							"order by ti.nombre ";
    	}
    	
    	logger.info("\n cargarTiposIntubacionPrivado->"+consulta+" \n\n");
    	
    	try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, institucion);
			mapa= UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		}	
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return mapa;
    }
    
    /**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public static HashMap<Object, Object> obtenerListadoIntubaciones(Connection con, int numeroSolicitud)
	{
		HashMap<Object, Object> mapa= new HashMap<Object, Object>();
		String consulta="SELECT " +
							"iha.codigo AS codigo_intubacion_hoja_anes, " +
							"to_char(iha.fecha, 'DD/MM/YYYY') as fecha, " +
							"iha.hora AS hora, " +
							"iha.tipo_intubacion as tipo_intubacion, " +
							"ti.nombre AS nombre_intubacion," +
							"iha.clas_cormack_lehane as clas_cormack_lehane," +
							"ccl.nombre AS nombre_clas, " +
							"CASE WHEN iha.clas_cormack_lehane IS NULL THEN '' ELSE ccl.descripcion END AS descripcion_clas " +
						"FROM " +
							"intubaciones_hoja_anes iha " +
						"INNER JOIN tipos_intubacion ti ON (ti.codigo = iha.tipo_intubacion) " +
						"LEFT OUTER JOIN clas_cormack_lehane ccl ON (ccl.codigo = iha.clas_cormack_lehane) " +
						"WHERE " +
							"iha.numero_solicitud=? " +
							"order by iha.fecha, iha.hora ";
		
		logger.info("\n obtenerListadoIntubaciones->"+consulta+" \n");
		
		PreparedStatementDecorator ps=null;
		try 
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, numeroSolicitud);
			mapa= UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		}	
		catch (SQLException e) 
		{
			e.printStackTrace();
			return new HashMap();
		}
		return mapa;
	}
	
	/**
     * 
     * @param con
     * @param centroCosto
     * @param institucion
     * @return
     */
    public static HashMap<Object, Object> cargarTiposIntubacionMultiple( Connection con, int centroCosto, int institucion)
    {
    	HashMap<Object, Object> mapa=cargarTiposIntubacionMultiplePrivado(con, centroCosto, institucion);
    	if(Utilidades.convertirAEntero(mapa.get("numRegistros").toString())<=0)
    		mapa=cargarTiposIntubacionMultiplePrivado(con, ConstantesBD.codigoNuncaValido /*centroCosto*/, institucion);
    	return mapa;
    }
    
    /**
     * 
     * @param con
     * @param centroCosto
     * @param institucion
     * @return
     */
    private static HashMap<Object, Object> cargarTiposIntubacionMultiplePrivado( Connection con, int centroCosto, int institucion)
    {
    	HashMap<Object, Object> mapa= new HashMap<Object, Object>();
    	mapa.put("numRegistros","0");
    	String consulta=""; 
    		
    	if(centroCosto>0)
    	{	
    		consulta+="SELECT " +
    						"ti.codigo, " +
    						"ti.nombre, " +
    						"'false' as checkeado " +
    					"FROM " +
    						"tipos_intubacion_multiple ti " +
    						"INNER JOIN tipos_intu_multiple_inst_cc tiicc ON (tiicc.tipo_intubacion_multiple=ti.codigo) " +
    					"WHERE " +
    						"tiicc.centro_costo="+centroCosto+" and institucion=? and activo='"+ConstantesBD.acronimoSi+"' " +
    						"order by ti.nombre ";
    	}
    	else
    	{
    		consulta+="SELECT " +
							"ti.codigo, " +
							"ti.nombre, " +
							"'false' as checkeado " +
						"FROM " +
							"tipos_intubacion_multiple ti " +
							"INNER JOIN tipos_intu_multiple_inst_cc tiicc ON (tiicc.tipo_intubacion_multiple=ti.codigo) " +
						"WHERE " +
							"tiicc.centro_costo is null and institucion=? and activo='"+ConstantesBD.acronimoSi+"' " +
							"order by ti.nombre ";
    	}
    	
    	logger.info("\n cargarTiposIntubacionMultiplePrivado->"+consulta+" \n\n");
    	
    	try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, institucion);
			mapa= UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		}	
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return mapa;
    }
    
    /**
     * 
     * @param con
     * @param fecha
     * @param hora
     * @param codigoIntubacionHojaAnestesia
     * @return
     */
	public static boolean existeIntubacionFechaHora(Connection con, String fecha, String hora, int codigoIntubacionHojaAnestesia)
	{
		String consulta="SELECT codigo from intubaciones_hoja_anes where codigo<>? AND fecha=? AND hora=?";
		PreparedStatementDecorator ps=null;
		try 
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, codigoIntubacionHojaAnestesia);
			ps.setDate(2, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(fecha)));
			ps.setString(3, hora);
			if(ps.executeQuery().next())
				return true;
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoIntubacionHojaAnestesia
	 * @param centroCosto
	 * @param institucion
	 * @return
	 */
	public static HashMap<Object, Object> cargarDetalleIntubacionHojaAnestesia(Connection con, int codigoIntubacionHojaAnestesia, int centroCosto, int institucion)
	{
		HashMap<Object, Object> mapa= new HashMap<Object, Object>();
		String consulta="";
			
		if(codigoIntubacionHojaAnestesia>0)	
		{	
			consulta= "(SELECT " +
							"d.tipo_intubacion_multiple as codigo, " +
							"tim.nombre as nombre, " +
							"'true' as checkeado " +
						"FROM " +
							"det_intubacion_hoja_anes_mul d " +
							"INNER JOIN tipos_intubacion_multiple tim ON(tim.codigo=d.tipo_intubacion_multiple) " +
						"WHERE " +
							"intubacion_hoja_anes="+codigoIntubacionHojaAnestesia+" " +
							" )";
			if(centroCosto>0)
	    	{	
	    		consulta+=" UNION " +
	    				"(SELECT " +
	    						"ti.codigo, " +
	    						"ti.nombre, " +
	    						"'false' as checkeado " +
	    					"FROM " +
	    						"tipos_intubacion_multiple ti " +
	    						"INNER JOIN tipos_intu_multiple_inst_cc tiicc ON (tiicc.tipo_intubacion_multiple=ti.codigo) " +
	    					"WHERE " +
	    						"ti.codigo not in(	SELECT " +
														"d.tipo_intubacion_multiple as codigo " +
													"FROM " +
														"det_intubacion_hoja_anes_mul d " +
														"INNER JOIN tipos_intubacion_multiple tim ON(tim.codigo=d.tipo_intubacion_multiple) " +
													"WHERE " +
														"intubacion_hoja_anes="+codigoIntubacionHojaAnestesia+" " +") " +
	    						"and tiicc.centro_costo="+centroCosto+" and institucion="+institucion+" and activo='"+ConstantesBD.acronimoSi+"' " +
	    						") ";
	    	}
	    	else
	    	{
	    		consulta+=" UNION " +
	    				"(SELECT " +
								"ti.codigo, " +
								"ti.nombre, " +
								"'false' as checkeado " +
							"FROM " +
								"tipos_intubacion_multiple ti " +
								"INNER JOIN tipos_intu_multiple_inst_cc tiicc ON (tiicc.tipo_intubacion_multiple=ti.codigo) " +
							"WHERE " +
									"ti.codigo not in(	SELECT " +
															"d.tipo_intubacion_multiple as codigo " +
														"FROM " +
															"det_intubacion_hoja_anes_mul d " +
															"INNER JOIN tipos_intubacion_multiple tim ON(tim.codigo=d.tipo_intubacion_multiple) " +
														"WHERE " +
															"intubacion_hoja_anes="+codigoIntubacionHojaAnestesia+" " +") " +
								"and tiicc.centro_costo is null and institucion="+institucion+" and activo='"+ConstantesBD.acronimoSi+"' " +
								") ";
	    	}
			
		}
		else
		{	
			if(centroCosto>0)
	    	{	
	    		consulta=" SELECT " +
	    						"ti.codigo, " +
	    						"ti.nombre, " +
	    						"'false' as checkeado " +
	    					"FROM " +
	    						"tipos_intubacion_multiple ti " +
	    						"INNER JOIN tipos_intu_multiple_inst_cc tiicc ON (tiicc.tipo_intubacion_multiple=ti.codigo) " +
	    					"WHERE " +
	    						"tiicc.centro_costo="+centroCosto+" and institucion="+institucion+" and activo='"+ConstantesBD.acronimoSi+"' " +
	    						"order by ti.nombre ";
	    	}
	    	else
	    	{
	    		consulta="SELECT " +
								"ti.codigo, " +
								"ti.nombre, " +
								"'false' as checkeado " +
							"FROM " +
								"tipos_intubacion_multiple ti " +
								"INNER JOIN tipos_intu_multiple_inst_cc tiicc ON (tiicc.tipo_intubacion_multiple=ti.codigo) " +
							"WHERE " +
								"tiicc.centro_costo is null and institucion="+institucion+" and activo='"+ConstantesBD.acronimoSi+"' " +
								"order by ti.nombre ";
	    	}
		}
		logger.info("\n cargarDetalleIntubacionHojaAnestesia->"+consulta+" -->"+codigoIntubacionHojaAnestesia+" \n");
		
		PreparedStatementDecorator ps=null;
		try 
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			mapa= UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		}	
		catch (SQLException e) 
		{
			e.printStackTrace();
			return null;
		}
		return mapa;
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoIntubacionHojaAnestesia
	 * @param centroCosto
	 * @param institucion
	 * @return
	 */
	public static HashMap<Object, Object> cargarCormack(Connection con, int centroCosto, int institucion, int tipoIntubacion, int codigoIntubacionHA)
	{
		HashMap<Object, Object> mapa= new HashMap<Object, Object>();
		String consulta="";
		
		//lo hago por si me cambian la parametrizacion de todas maneras mostrarlo
		if(codigoIntubacionHA>0)
		{
			consulta="(SELECT " +
						"c.codigo, " +
						"c.nombre, " +
						"c.descripcion " +
					"FROM " +
						"intubaciones_hoja_anes i " +
						"INNER JOIN clas_cormack_lehane c ON(c.codigo=i.clas_cormack_lehane) " +
					"WHERE " +
						"c.codigo="+codigoIntubacionHA+") " +
					"UNION "
					;
		}
		
		
		consulta+=	"(SELECT " +
						"c.codigo, " +
						"c.nombre, " +
						"c.descripcion " +
					"FROM " +
						"tipos_intu_cormack t " +
						"INNER JOIN clas_cormack_lehane c ON(c.codigo=t.clas_cormack_lehane) " +
					"WHERE " +
						"t.tipo_intubacion=(select t1.tipo_intubacion FROM tipos_intubacion_inst_cc t1 WHERE t1.tipo_intubacion="+tipoIntubacion+" and t1.centro_costo="+centroCosto+" and t1.institucion="+institucion+" and t1.activo='"+ConstantesBD.acronimoSi+"') AND t.activo='"+ConstantesBD.acronimoSi+"') ";
		consulta+=	"UNION (SELECT " +
							"c.codigo, " +
							"c.nombre, " +
							"c.descripcion " +
						"FROM " +
							"tipos_intu_cormack t " +
							"INNER JOIN clas_cormack_lehane c ON(c.codigo=t.clas_cormack_lehane) " +
						"WHERE " +
							"t.tipo_intubacion=(select t1.tipo_intubacion FROM tipos_intubacion_inst_cc t1 WHERE t1.tipo_intubacion="+tipoIntubacion+" and t1.centro_costo is null and t1.institucion="+institucion+" and t1.activo='"+ConstantesBD.acronimoSi+"') AND t.activo='"+ConstantesBD.acronimoSi+"') ";
		
		
		logger.info("\n cargarCormack->"+consulta+" \n");
		
		PreparedStatementDecorator ps=null;
		try 
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			mapa= UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		}	
		catch (SQLException e) 
		{
			e.printStackTrace();
			return null;
		}
		return mapa;
	}
}
