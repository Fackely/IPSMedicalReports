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
public class SqlBaseViasAereasDao 
{
	/**
	* Objeto para manejar los logs de esta clase
	*/
	private static Logger logger= Logger.getLogger(SqlBaseViasAereasDao.class);
	
	/**
	 * 
	 * @param con
	 * @param mapa
	 * @return
	 */
    public static int insertar(Connection con, HashMap<Object, Object> mapa)
    {
    	String cadena="INSERT INTO vias_aerea_hoja_anes 	(codigo, " +     			//1
    														"numero_solicitud, " +		//2
    														"tipo_dispositivo, " +		//3
    														"fecha, " +					//4
    														"hora, " +					//5
    														"institucion, "+			//6				
    														"fecha_modifica, " +		
    														"hora_modifica, " +			
    														"usuario_modifica " +		//7
    														") " +				
    														"values (?, ?, ?," +
    																" ?, ?, ?, " +
    																"CURRENT_DATE, " +
    																""+ValoresPorDefecto.getSentenciaHoraActualBD()+", " +
    																"?)";
    	
    	try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			int codigo= UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_vias_aerea_hoja_anes");
			ps.setInt(1, codigo);
			ps.setInt(2, Utilidades.convertirAEntero(mapa.get("numerosolicitud").toString()));
			ps.setInt(3, Utilidades.convertirAEntero(mapa.get("tipodispositivo").toString()));
			ps.setDate(4, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(mapa.get("fecha").toString())));
			ps.setString(5, mapa.get("hora")+"");
			ps.setInt(6, Utilidades.convertirAEntero(mapa.get("institucion")+""));
			ps.setString(7, mapa.get("usuariomodifica")+"");
				
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
    public static boolean eliminar(Connection con, int viaAerea)
    {
    	String cadena="DELETE FROM vias_aerea_hoja_anes WHERE codigo=? ";
		logger.info("\n ELIMINAR-->"+cadena+" via-->"+viaAerea+"\n");
    	
    	try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, viaAerea);
			
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
    	String cadena="INSERT INTO det_vias_aerea_hoja_anes (via_aerea, " +     		//1
															"articulo, " +			//2
															"cantidad, " +			//3
															"via_insercion_disp, " +//4
															"genera_consumo," +//5
															"codigo_det_mate_qx) " +	//6
															"values " +
															"(?, ?, ?," +
																" ?, ?, ? " +
																")";
    	
    	try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, Utilidades.convertirAEntero(mapa.get("viaaerea")+""));
			ps.setInt(2, Utilidades.convertirAEntero(mapa.get("articulo").toString()));
			ps.setInt(3, Utilidades.convertirAEntero(mapa.get("cantidad").toString()));
			ps.setInt(4, Utilidades.convertirAEntero(mapa.get("viainserciondisp").toString()));
			ps.setString(5, mapa.get("generaconsumo").toString());
						
			if(mapa.containsKey("codigodetmateqx") 
					&& !(mapa.get("codigodetmateqx")+"").equals("") && 
						Utilidades.convertirAEntero(mapa.get("codigodetmateqx").toString()) > 0)
				ps.setInt(6,Utilidades.convertirAEntero(mapa.get("codigodetmateqx").toString()));
			else
				ps.setNull(6,Types.INTEGER);
				
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
    	String cadena="UPDATE det_vias_aerea_hoja_anes SET 	cantidad=?, " +				//1
															"via_insercion_disp=?, " +	//2
															"genera_consumo=? " +		//3
															"WHERE " +
															"via_aerea=? " +     		//4
															"AND articulo=? "; 			//5
															
    	logger.info("\n Modificar-->"+cadena+" MAOPA-->"+mapa+"\n");
    	
    	try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, Utilidades.convertirAEntero(mapa.get("cantidad")+""));
			ps.setInt(2, Utilidades.convertirAEntero(mapa.get("viainserciondisp")+""));
			ps.setString(3, mapa.get("generaconsumo")+"");
			ps.setInt(4, Utilidades.convertirAEntero(mapa.get("viaaerea")+""));
			ps.setInt(5, Utilidades.convertirAEntero(mapa.get("articulo")+""));
				
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
     * si articulo es <=0 borra todos los articulos pertenecientes a la via aerea
     * @param con
     * @param viaAerea
     * @param articulo
     * @return
     */
    public static boolean eliminarDetalle(Connection con, int viaAerea, int articulo)
    {
    	String cadena="DELETE FROM det_vias_aerea_hoja_anes WHERE via_aerea=? ";
		
    	if(articulo>0)
    		cadena+=" AND articulo = "+articulo;
    	
		logger.info("\n ELIMINAR-->"+cadena+" via-->"+viaAerea+"\n");
    	
    	try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, viaAerea);
			
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
    public static HashMap<Object, Object> cargarTiposDispositivosCCInst( Connection con, int centroCosto, int institucion)
    {
    	HashMap<Object, Object> mapa=cargarTiposDispositivosPrivado(con, centroCosto, institucion);
    	if(Utilidades.convertirAEntero(mapa.get("numRegistros").toString())<=0)
    		mapa=cargarTiposDispositivosPrivado(con, ConstantesBD.codigoNuncaValido /*centroCosto*/, institucion);
    	return mapa;
    }
 
    /**
     * 
     * @param con
     * @param centroCosto
     * @param institucion
     * @return
     */
    private static HashMap<Object, Object> cargarTiposDispositivosPrivado( Connection con, int centroCosto, int institucion)
    {
    	HashMap<Object, Object> mapa= new HashMap<Object, Object>();
    	mapa.put("numRegistros","0");
    	String consulta=""; 
    		
    	if(centroCosto>0)
    	{	
    		consulta+="SELECT " +
    						"td.codigo, " +
    						"td.nombre " +
    					"FROM " +
    						"tipos_dispositivo td " +
    						"INNER JOIN tipos_dispositivo_inst_cc tdicc ON (tdicc.tipo_dispositivo=td.codigo) " +
    					"WHERE " +
    						"tdicc.centro_costo="+centroCosto+" and institucion=? and activo='"+ConstantesBD.acronimoSi+"' " +
    						"order by td.nombre ";
    	}
    	else
    	{
    		consulta+="SELECT " +
							"td.codigo, " +
							"td.nombre " +
						"FROM " +
							"tipos_dispositivo td " +
							"INNER JOIN tipos_dispositivo_inst_cc tdicc ON (tdicc.tipo_dispositivo=td.codigo) " +
						"WHERE " +
							"tdicc.centro_costo is null and institucion=? and activo='"+ConstantesBD.acronimoSi+"' " +
							"order by td.nombre ";
    	}
    	
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
	public static HashMap<Object, Object> obtenerListadoViasAereas(Connection con, int numeroSolicitud)
	{
		HashMap<Object, Object> mapa= new HashMap<Object, Object>();
		String consulta="SELECT " +
							"codigo AS codigo_via_aerea_ha, " +
							"to_char(fecha, 'DD/MM/YYYY') as fecha, " +
							"hora AS hora, " +
							"salascirugia.existeConsumoViaAerea(codigo) as existeconsumo, " +
							"tipo_dispositivo as tipo_dispositivo " +
						"FROM " +
							"vias_aerea_hoja_anes " +
						"WHERE " +
							"numero_solicitud=? " +
							"order by fecha, hora ";
		
		logger.info("\n obtenerListadoViasAereas->"+consulta+" \n");
		
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
    public static HashMap<Object, Object> cargarViasInsercionArticulo( Connection con, int articulo, int centroCosto, int institucion)
    {
    	HashMap<Object, Object> mapa=cargarViasInsercionArticuloPrivado(con, articulo, centroCosto, institucion);
    	if(Utilidades.convertirAEntero(mapa.get("numRegistros").toString())<=0)
    		mapa=cargarViasInsercionArticuloPrivado(con, articulo, ConstantesBD.codigoNuncaValido/*centroCosto*/, institucion);
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
    private static HashMap<Object, Object> cargarViasInsercionArticuloPrivado( Connection con, int articulo, int centroCosto, int institucion)
    {
    	HashMap<Object, Object> mapa= new HashMap<Object, Object>();
    	mapa.put("numRegistros","0");
    	String consulta=""; 
    		
    	if(centroCosto>0)
    	{	
    		consulta+="SELECT " +
    						"vid.codigo, " +
    						"vid.nombre " +
    					"FROM " +
    						"vias_insercion_disp vid " +
    						"INNER JOIN vias_art_tipo_disp vatd ON (vatd.via_insercion_disp=vid.codigo AND vatd.articulo="+articulo+") " +
    						"INNER JOIN vias_insert_inst_cc viicc ON (viicc.via_insercion_disp=vid.codigo AND viicc.centro_costo="+centroCosto+") " +
    					"WHERE " +
    						"viicc.institucion=? and viicc.activo='"+ConstantesBD.acronimoSi+"' " +
    						"order by vid.nombre ";
    	}
    	else
    	{
    		consulta+="SELECT " +
							"vid.codigo, " +
							"vid.nombre " +
						"FROM " +
							"vias_insercion_disp vid " +
							"INNER JOIN vias_art_tipo_disp vatd ON (vatd.via_insercion_disp=vid.codigo AND vatd.articulo="+articulo+") " +
							"INNER JOIN vias_insert_inst_cc viicc ON (viicc.via_insercion_disp=vid.codigo AND viicc.centro_costo is null) " +
						"WHERE " +
							"viicc.institucion=? and viicc.activo='"+ConstantesBD.acronimoSi+"' " +
							"order by vid.nombre ";
    	}
    	
    	logger.info("\n\n VIAS DE INSERCION--->"+consulta+"\n\n");
    	
    	
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
     * @param codigoViaAereaHojaAnestesia
     * @return
     */
	public static boolean existeViaAereaFechaHora(Connection con, String fecha, String hora, int codigoViaAereaHojaAnestesia)
	{
		String consulta="SELECT codigo from vias_aerea_hoja_anes where codigo<>? AND fecha=? AND hora=?";
		PreparedStatementDecorator ps=null;
		try 
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, codigoViaAereaHojaAnestesia);
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
	 * @param con
	 * @param codigoViaAereaHojaAnestesia
	 * @return
	 */	
	public static HashMap<Object, Object> cargarViaAereaHojaAnestesia(Connection con, int codigoViaAereaHojaAnestesia)
	{
		HashMap<Object, Object> mapa= new HashMap<Object, Object>();
		String consulta="SELECT " +
							"d.articulo as codigoarticulo, " +
							"getdescarticulo(d.articulo) as descripcionarticulo, " +
							"d.cantidad as cantidad, " +
							"v.nombre as nombreviainsercion, " +
							"d.via_insercion_disp as viainsercion, " +
							"d.genera_consumo as generaconsumo, " +
							"'false' as  fueeliminadoarticulo, " +
							"na.es_pos AS tipoposarticulo," +
							"coalesce(d.codigo_det_mate_qx,"+ConstantesBD.codigoNuncaValido+") as codigodetmateqx " +
						"FROM " +
							"det_vias_aerea_hoja_anes d " +
							"INNER JOIN vias_insercion_disp v ON(v.codigo=d.via_insercion_disp) " +
							"INNER JOIN articulo a ON (d.articulo=a.codigo) " +
							"INNER JOIN naturaleza_articulo na ON (a.naturaleza=na.acronimo AND a.institucion=na.institucion) " +
						"WHERE " +
							"via_aerea=? " +
							"order by d.articulo ";
		
		logger.info("\n cargarViaAereaHojaAnestesia->"+consulta+" -->"+codigoViaAereaHojaAnestesia+" \n");
		
		PreparedStatementDecorator ps=null;
		try 
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, codigoViaAereaHojaAnestesia);
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
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */	
	public static HashMap<Object, Object> cargarViaAereaCompleta(Connection con, int numeroSolicitud)
	{
		HashMap<Object, Object> mapa= new HashMap<Object, Object>();
		String consulta="SELECT " +		
							"va.codigo AS codigo_via_aerea_ha, " +
							"to_char(va.fecha, 'DD/MM/YYYY') as fecha, " +
							"va.hora AS hora, " +
							"salascirugia.existeConsumoViaAerea(va.codigo) as existeconsumo, " +
							"va.tipo_dispositivo as tipo_dispositivo, " +
							"td.nombre AS nombre_dispositivo, " +					
							"d.articulo as codigoarticulo, " +
							"getdescarticulo(d.articulo) as descripcionarticulo, " +
							"d.cantidad as cantidad, " +
							"v.nombre as nombreviainsercion, " +
							"d.via_insercion_disp as viainsercion, " +
							"d.genera_consumo as generaconsumo, " +
							"'false' as  fueeliminadoarticulo, " +
							"na.es_pos AS tipoposarticulo " +
						"FROM " +
							"salascirugia.det_vias_aerea_hoja_anes d " +
							"INNER JOIN salascirugia.vias_aerea_hoja_anes va ON (va.numero_solicitud = ? ) " +
							"INNER JOIN salascirugia.vias_insercion_disp v ON(v.codigo=d.via_insercion_disp) " +
							"INNER JOIN inventarios.articulo a ON (d.articulo=a.codigo) " +
							"INNER JOIN inventarios.naturaleza_articulo na ON (a.naturaleza=na.acronimo AND a.institucion=na.institucion) " +
							"INNER JOIN salascirugia.tipos_dispositivo td ON (td.codigo = va.tipo_dispositivo)" +
						"WHERE " +
							"d.via_aerea= va.codigo " +
							"order by va.fecha,va.hora,d.articulo ";
		
		logger.info("\n cargarViaAereaHojaAnestesia->"+consulta+" -->"+numeroSolicitud+" \n");
		
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
}