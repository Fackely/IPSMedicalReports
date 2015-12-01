package com.princetonsa.dao.sqlbase.parametrizacion;

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
import util.UtilidadFecha;
import util.Utilidades;
import util.ValoresPorDefecto;

/**
 * 
 * @author wilson
 *
 */
public class SqlBaseAccesosVascularesHADao 
{
	/**
	* Objeto para manejar los logs de esta clase
	*/
	private static Logger logger= Logger.getLogger(SqlBaseAccesosVascularesHADao.class);
	
	/**
	 * 
	 * @param con
	 * @param mapa
	 * @return
	 */
    public static double insertar(Connection con, HashMap<Object, Object> mapa)
    {
    	String cadena="INSERT INTO 	accesos_vasculares_hoja_anes (	codigo , " +				//1
    																"numero_solicitud , " +		//2
    																"fecha , " +				//3
    																"hora , " +					//4
    																"tipo_acceso_vascular , " +	//5
    																"articulo , " +				//6
    																"cantidad , " +				//7
    																"localizacion , " +			//8
    																"fecha_modifica , " +		
    																"hora_modifica , " +
    																"usuario_modifica," +		//9
    																"genero_consumo, " +		//10
    																"genero_orden, "+			//11
    																"servicio," +
    																"codigo_det_mate_qx) "+				//12
    														"values (?, ?, ?," +	//3
    																" ?, ?, ?, " +	//6
    																" ?, ?, " +		//8		
    																"CURRENT_DATE, " +
    																""+ValoresPorDefecto.getSentenciaHoraActualBD()+", " +
    																"?, ?, ?, (select servicio from tipos_accesos_vasculares where codigo=?),?)";	//12						
    	
    	try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			double codigo= UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_accesos_vas_hoja_anes");
			ps.setDouble(1, codigo);
			ps.setInt(2, Utilidades.convertirAEntero(mapa.get("numerosolicitud").toString()));
			ps.setDate(3, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(mapa.get("fecha").toString())));
			ps.setString(4, mapa.get("hora")+"");
			ps.setInt(5, Utilidades.convertirAEntero(mapa.get("tipoaccesovascular").toString()));
			ps.setInt(6, Utilidades.convertirAEntero(mapa.get("articulo").toString()));
			ps.setInt(7, Utilidades.convertirAEntero(mapa.get("cantidad").toString()));
			ps.setInt(8, Utilidades.convertirAEntero(mapa.get("localizacion").toString()));
			ps.setString(9, mapa.get("usuariomodifica")+"");
			ps.setString(10, mapa.get("generoconsumo")+"");
			ps.setString(11, mapa.get("generoorden").toString());
			ps.setInt(12, Utilidades.convertirAEntero(mapa.get("tipoaccesovascular")+""));
			
			if(mapa.containsKey("codigoDetMateQx") && 
					Utilidades.convertirAEntero(mapa.get("codigoDetMateQx").toString()) > 0)
				ps.setInt(13,Utilidades.convertirAEntero(mapa.get("codigoDetMateQx").toString()));
			else
				ps.setNull(13,Types.INTEGER);
			
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
     * @param viaAerea
     * @param articulo
     * @return
     */
    public static boolean eliminar(Connection con, double codigoPKAccesoVascularHA)
    {
    	String cadena="DELETE FROM accesos_vasculares_hoja_anes WHERE codigo=? and genero_consumo='"+ConstantesBD.acronimoNo+"' and genero_orden='"+ConstantesBD.acronimoNo+"' ";
		logger.info("\n ELIMINAR-->"+cadena+" cod-->"+codigoPKAccesoVascularHA+"\n");
    	
    	try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setDouble(1, codigoPKAccesoVascularHA);
			
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
    public static boolean modificar(Connection con, HashMap<Object, Object> mapa)
    {
    	String cadena="UPDATE accesos_vasculares_hoja_anes SET "+	
																"tipo_acceso_vascular=? , " +	//1
																"articulo=? , " +				//2
																"cantidad=? , " +				//3
																"localizacion=? , " +			//4
																"fecha_modifica=CURRENT_DATE , " +		
																"hora_modifica="+ValoresPorDefecto.getSentenciaHoraActualBD()+" , " +
																"usuario_modifica=?, " +		//5
																"servicio=(select servicio from tipos_accesos_vasculares where codigo=?)"+	//6
															"WHERE " +
																"codigo=? " +//7
																"and genero_orden='"+ConstantesBD.acronimoNo+"' ";   					//6
															
															
    	logger.info("\n Modificar-->"+cadena+" MAOPA-->"+mapa+"\n");
    	
    	try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, Utilidades.convertirAEntero(mapa.get("tipoaccesovascular")+""));
			ps.setInt(2, Utilidades.convertirAEntero(mapa.get("articulo")+""));
			ps.setInt(3, Utilidades.convertirAEntero(mapa.get("cantidad")+""));
			ps.setInt(4, Utilidades.convertirAEntero(mapa.get("localizacion")+""));
			ps.setString(5, mapa.get("usuariomodifica")+"");
			ps.setInt(6, Utilidades.convertirAEntero(mapa.get("tipoaccesovascular")+""));
			ps.setDouble(7, Double.parseDouble(mapa.get("codigo")+""));
				
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
    public static HashMap<Object, Object> cargarTiposAccesoVascularCCInst( Connection con, int centroCosto, int institucion)
    {
    	HashMap<Object, Object> mapa=cargarTiposAccesoVascularPrivado(con, centroCosto, institucion);
    	if(Utilidades.convertirAEntero(mapa.get("numRegistros").toString())<=0)
    		mapa=cargarTiposAccesoVascularPrivado(con, ConstantesBD.codigoNuncaValido /*centroCosto*/, institucion);
    	return mapa;
    }
 
    /**
     * 
     * @param con
     * @param centroCosto
     * @param institucion
     * @return
     */
    private static HashMap<Object, Object> cargarTiposAccesoVascularPrivado( Connection con, int centroCosto, int institucion)
    {
    	HashMap<Object, Object> mapa= new HashMap<Object, Object>();
    	mapa.put("numRegistros","0");
    	String consulta=""; 
    		
    	if(centroCosto>0)
    	{	
    		consulta+="SELECT " +
    						"ta.codigo, " +
    						"ta.nombre, " +
    						"coalesce(ta.servicio, "+ConstantesBD.codigoNuncaValido+") as servicio, " +
    						"coalesce(getnombreservicio(ta.servicio, "+ConstantesBD.codigoTarifarioCups+"), 'SIN SERVICIO') as descservicio " +
    					"FROM " +
    						"tipos_accesos_vasculares ta " +
    						"INNER JOIN tipos_acc_vas_inst_cc taicc ON (taicc.tipo_acceso_vascular=ta.codigo) " +
    					"WHERE " +
    						"taicc.centro_costo="+centroCosto+" and taicc.institucion=? and activo='"+ConstantesBD.acronimoSi+"' " +
    						"order by ta.nombre ";
    	}
    	else
    	{
    		consulta+="SELECT " +
							"ta.codigo, " +
							"ta.nombre, " +
							"coalesce(ta.servicio, "+ConstantesBD.codigoNuncaValido+") as servicio, " +
    						"coalesce(getnombreservicio(ta.servicio, "+ConstantesBD.codigoTarifarioCups+"), 'SIN SERVICIO') as descservicio " +
						"FROM " +
							"tipos_accesos_vasculares ta " +
							"INNER JOIN tipos_acc_vas_inst_cc taicc ON (taicc.tipo_acceso_vascular=ta.codigo) " +
						"WHERE " +
							"taicc.centro_costo is null and taicc.institucion=? and activo='"+ConstantesBD.acronimoSi+"' " +
							"order by ta.nombre ";
    	}
    	
    	try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			logger.info("\n\ncargarTiposAccesoVascularPrivado--->"+consulta+"");
			
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
	public static HashMap<Object, Object> obtenerListadoAccesosVasculares(Connection con, int numeroSolicitud)
	{
		HashMap<Object, Object> mapa= new HashMap<Object, Object>();
		String consulta="SELECT " +
							"a.codigo AS cod_acc_vascular_hoja_anes, " +
							"to_char(a.fecha, 'DD/MM/YYYY') as fecha, " +
							"a.hora AS hora, " +
							"a.tipo_acceso_vascular as tipoaccesovascular, " +
							"t.nombre as nombretipoaccesovascular, " +
							"a.articulo as articulo, " +
							"getdescarticulo(a.articulo) as descarticulo, " +
							"a.cantidad as cantidad, " +
							"a.localizacion as localizacion, " +
							"l.nombre as nombrelocalizacion, " +
							"a.genero_consumo as generoconsumo, " +
							"a.genero_orden as generoorden, " +
							"coalesce(a.servicio, "+ConstantesBD.codigoNuncaValido+") as servicio, " +
							"na.es_pos AS espos," +
							"coalesce(a.codigo_det_mate_qx,"+ConstantesBD.codigoNuncaValido+") as codigodetmateqx " +
						"FROM " +
							"salascirugia.accesos_vasculares_hoja_anes a " +
							"inner join salascirugia.tipos_accesos_vasculares t on(t.codigo=a.tipo_acceso_vascular) " +
							"inner join salascirugia.localizaciones_acceso l on(l.codigo=a.localizacion) " +
							"INNER JOIN inventarios.articulo ap ON (a.articulo=ap.codigo) " +
							"INNER JOIN inventarios.naturaleza_articulo na ON (ap.naturaleza=na.acronimo AND ap.institucion=na.institucion) " +
						"WHERE " +
							"numero_solicitud=? " +
							"order by fecha, hora ";
		
		logger.info("\n obtenerListadoViasAereas->"+consulta+" numero_solicitud->"+numeroSolicitud+" \n");
		
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
			return null;
		}
		return mapa;
	}
	
	/**
	 * 
	 * @param con
	 * @param articulo
	 * @param centroCosto
	 * @param institucion
	 * @return
	 */
    public static HashMap<Object, Object> cargarLocalizacionesXTipoAcceso( Connection con, int tipoAccesoVascular, int centroCosto, int institucion)
    {
    	HashMap<Object, Object> mapa=cargarLocalizacionesXTipoAccesoInstCCPrivado(con, tipoAccesoVascular, centroCosto, institucion);
    	if(Utilidades.convertirAEntero(mapa.get("numRegistros").toString())<=0)
    		mapa=cargarLocalizacionesXTipoAccesoInstCCPrivado(con, tipoAccesoVascular, ConstantesBD.codigoNuncaValido /*centroCosto*/, institucion);
    	return mapa;
    }
	
	/**
	 * 
	 * @param con
	 * @param articulo
	 * @param centroCosto
	 * @param institucion
	 * @return
	 */
    private static HashMap<Object, Object> cargarLocalizacionesXTipoAccesoInstCCPrivado(Connection con, int tipoAccesoVascular, int centroCosto, int institucion)
    {
    	HashMap<Object, Object> mapa= new HashMap<Object, Object>();
    	mapa.put("numRegistros","0");
    	String consulta=""; 
    		
    	if(centroCosto>0)
    	{	
    		consulta+="SELECT " +
    						"l.codigo, " +
    						"l.nombre " +
    					"FROM " +
    						"localizaciones_acceso l " +
    						"INNER JOIN local_tipos_acceso_vas lt ON (lt.localizacion_acceso=l.codigo) " +
    					"WHERE " +
    						"lt.tipo_acceso_vascular=? and lt.centro_costo="+centroCosto+" and lt.institucion=? " +
    						"order by l.nombre ";
    	}
    	else
    	{
    		consulta+="SELECT " +
							"l.codigo, " +
							"l.nombre " +
						"FROM " +
							"localizaciones_acceso l " +
							"INNER JOIN local_tipos_acceso_vas lt ON (lt.localizacion_acceso=l.codigo) " +
						"WHERE " +
							"lt.tipo_acceso_vascular=? and lt.centro_costo is null and lt.institucion=? " +
							"order by l.nombre ";
    	}
    	
    	logger.info("\n\n cargarLocalizacionesXTipoAccesoInstCCPrivado--->"+consulta+" tipoAccesoVascular->"+tipoAccesoVascular+" inst->"+institucion+" \n\n");
    	
    	
    	try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, tipoAccesoVascular);
			ps.setInt(2, institucion);
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
     * @param codigoPKAccesoVascularHA
     * @return
     */
	public static boolean existeAccesoVascularHAFechaHora(Connection con, String fecha, String hora, double codigoPKAccesoVascularHA)
	{
		String consulta="SELECT codigo from accesos_vasculares_hoja_anes where codigo<>? AND fecha=? AND hora=?";
		PreparedStatementDecorator ps=null;
		try 
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setDouble(1, codigoPKAccesoVascularHA);
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
	 * @param HashMap parametros
	 * @return
	 */
	public static boolean actualizarGeneroConsumo(Connection con, HashMap parametros)
	{
		double codigoPKAccesoVascularHA = Utilidades.convertirADouble(parametros.get("codigoPKAccesoVascularHA").toString());
    	String cadena="UPDATE accesos_vasculares_hoja_anes SET genero_consumo='"+ConstantesBD.acronimoSi+"', codigo_det_mate_qx = ? WHERE codigo=? ";
		logger.info("\n actualizarGeneroConsumo-->"+cadena+" cod-->"+codigoPKAccesoVascularHA+"\n");
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
						
			if(Utilidades.convertirAEntero(parametros.get("codigoDetMateQx").toString())>0)
				ps.setInt(1,Utilidades.convertirAEntero(parametros.get("codigoDetMateQx").toString()));
			else
				ps.setNull(1,Types.INTEGER);
			
			ps.setDouble(2, codigoPKAccesoVascularHA);
		
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
	 * @param codigoPKAccesoVascularHA
	 * @return
	 */
	public static boolean actualizarGeneroOrden(Connection con, double codigoPKAccesoVascularHA)
	{
    	String cadena="UPDATE accesos_vasculares_hoja_anes SET genero_orden='"+ConstantesBD.acronimoSi+"' WHERE codigo=? ";   				
		logger.info("\n actualizarGeneroOrden-->"+cadena+" cod-->"+codigoPKAccesoVascularHA+"\n");
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setDouble(1, codigoPKAccesoVascularHA);
		
			if(ps.executeUpdate()>0)
				return true;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return false;
	}
}
