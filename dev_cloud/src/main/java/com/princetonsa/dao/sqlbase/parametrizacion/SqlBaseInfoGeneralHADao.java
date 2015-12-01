package com.princetonsa.dao.sqlbase.parametrizacion;

import java.sql.Connection;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.Vector;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

/**
 * 
 * @author wilson
 *
 */
public class SqlBaseInfoGeneralHADao 
{
	/**
	* Objeto para manejar los logs de esta clase
	*/
	private static Logger logger= Logger.getLogger(SqlBasePosicionesAnestesiaDao.class);
	
	
	//***********************************************MONITOREOS**************************************************************//
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @param centroCosto
	 * @param institucion
	 * @return
	 */
	public static HashMap<Object, Object> cargarMonitoreos (Connection con, int numeroSolicitud, int centroCosto, int institucion)
	{
		HashMap<Object, Object> mapa= cargarMonitoreosPrivado(con, numeroSolicitud, centroCosto, institucion);
		if(Utilidades.convertirAEntero(mapa.get("numRegistros")+"")<=0)
			mapa= cargarMonitoreosPrivado(con, numeroSolicitud, ConstantesBD.codigoNuncaValido/*centroCosto*/, institucion);
		return mapa;
	}
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @param centroCosto
	 * @param institucion
	 * @return
	 */
	private static HashMap<Object, Object> cargarMonitoreosPrivado (Connection con, int numeroSolicitud, int centroCosto, int institucion)
	{
		HashMap<Object, Object> mapa= new HashMap<Object, Object>();
		logger.info("llega sql cargarmonitoreos");
		mapa.put("numRegistros", "0");
		String consultaStr="";
		
		if(centroCosto>0)
		{
			consultaStr=	"SELECT " +
									"maic.codigo AS codigo_monitoreo_inst_cc,  " +
									"ma.codigo AS codigo_monitoreo, " +
									"coalesce(ma.nombre, '') AS nombre, " +
									"existeMonitoreoSol("+numeroSolicitud+", ma.codigo) as checkeado, " +
									"(select m.codigo from monitoreos_hoja_anestesia m where m.numero_solicitud="+numeroSolicitud+" and m.monitoreo=ma.codigo) as codigomonitoreohojaanestesia " +
								"FROM " +
									"monitoreos_anest_inst_cc maic " +
									"INNER JOIN monitoreos_anestesia ma ON (maic.monitoreo=ma.codigo) " +
								"WHERE " +
									"maic.centro_costo="+centroCosto+" " +
									"and maic.institucion="+institucion+" " +
									"and maic.activo='"+ConstantesBD.acronimoSi+"' "+
								"ORDER BY ma.nombre ";
		}
		else
		{
			consultaStr=	"SELECT " +
								"maic.codigo AS codigo_monitoreo_inst_cc,  " +
								"ma.codigo AS codigo_monitoreo, " +
								"coalesce(ma.nombre, '') AS nombre, " +
								"existeMonitoreoSol("+numeroSolicitud+", ma.codigo) as checkeado, " +
								"(select m.codigo from monitoreos_hoja_anestesia m where m.numero_solicitud="+numeroSolicitud+" and m.monitoreo=ma.codigo) as codigomonitoreohojaanestesia " +
							"FROM " +
								"monitoreos_anest_inst_cc maic " +
								"INNER JOIN monitoreos_anestesia ma ON (maic.monitoreo=ma.codigo) " +
							"WHERE " +
								"maic.centro_costo is null " +
								"and maic.institucion="+institucion+" " +
								"and maic.activo='"+ConstantesBD.acronimoSi+"' "+
							"ORDER BY ma.nombre ";
		}
		
		logger.info("\n cargarMonitoreo-->"+consultaStr);
		
		
		PreparedStatementDecorator ps=null;
		try 
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			mapa= UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
			
			for(int w=0; w<Utilidades.convertirAEntero(mapa.get("numRegistros")+""); w++)
			{
				mapa.put("estabd_"+w, mapa.get("checkeado_"+w));
			}
			
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
	 * @param mapaSignoVitalAnestesia
	 * @return
	 */
    public static boolean insertarMonitoreos(Connection con, HashMap<Object, Object> mapa)
    {
    	String cadena="INSERT INTO monitoreos_hoja_anestesia 	(codigo, " +     			//1
	    														"numero_solicitud, " +		//2
	    														"monitoreo_inst_cc, " +		//3
	    														"monitoreo, " +				//4
	    														"fecha_modifica, " +		
	    														"hora_modifica, " +			
	    														"usuario_modifica) " +		//5
	    														"values (?, ?, " +
	    																" ?, ?, " +
	    																"CURRENT_DATE, " +
	    																""+ValoresPorDefecto.getSentenciaHoraActualBD()+", " +
	    																"?)";
    	
    	logger.info("\n insertar mobnitoreo->"+mapa.get("codigomonitoreo").toString()+" moninstcc->"+mapa.get("codigomonitoreoinstcc").toString()+"\n");
    	
    	try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			int codigo= UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_monitoreos_hoja_anestesia");
			ps.setInt(1, codigo);
			ps.setInt(2, Utilidades.convertirAEntero(mapa.get("numerosolicitud").toString()));
			ps.setInt(3, Utilidades.convertirAEntero(mapa.get("codigomonitoreoinstcc").toString()));
			ps.setInt(4, Utilidades.convertirAEntero(mapa.get("codigomonitoreo").toString()));
			ps.setString(5, mapa.get("loginusuario")+"");
			
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
    public static boolean modificarMonitoreos(Connection con, HashMap<Object, Object> mapa)
    {
    	String cadena="UPDATE monitoreos_hoja_anestesia SET " +
    						"monitoreo_inst_cc=?, " +		//1
							"monitoreo=?, " +				//2
							"fecha_modifica= CURRENT_DATE, " +		
							"hora_modifica= "+ValoresPorDefecto.getSentenciaHoraActualBD()+", " +							
							"usuario_modifica=? " +			//3
						"WHERE "+
							"codigo=? "; 					//4
							
							
    	logger.info("\n Modificar-->"+cadena+" MAOPA-->"+mapa+"\n");
    	
    	try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, Utilidades.convertirAEntero(mapa.get("codigomonitoreoinstcc").toString()));
			ps.setInt(2, Utilidades.convertirAEntero(mapa.get("codigomonitoreo").toString()));
			ps.setString(3, mapa.get("loginusuario")+"");
			ps.setInt(4, Utilidades.convertirAEntero(mapa.get("codigomonitoreohojaanestesia")+""));
			
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
    public static boolean eliminarMonitoreo(Connection con, int codigoMonitoreoHojaAnestesia)
    {
    	String cadena="DELETE FROM monitoreos_hoja_anestesia WHERE codigo=? ";
							
		logger.info("\n ELIMINAR-->"+cadena+" codigo-->"+codigoMonitoreoHojaAnestesia+"\n");
    	
    	try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, codigoMonitoreoHojaAnestesia);
			
			if(ps.executeUpdate()>0)
				return true;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return false;
    }
    
    //***********************************************FIN MONITOREOS**************************************************************//
    
    //***********************************************PROTECCIONES**************************************************************//
    /**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @param centroCosto
	 * @param institucion
	 * @return
	 */
	public static HashMap<Object, Object> cargarProtecciones (Connection con, int numeroSolicitud, int centroCosto, int institucion)
	{
		HashMap<Object, Object> mapa= cargarProteccionesPrivado(con, numeroSolicitud, centroCosto, institucion);
		if(Utilidades.convertirAEntero(mapa.get("numRegistros")+"")<=0)
			mapa= cargarProteccionesPrivado(con, numeroSolicitud, ConstantesBD.codigoNuncaValido/*centroCosto*/, institucion);
		return mapa;
	}
    
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @param centroCosto
	 * @param institucion
	 * @return
	 */
	private static HashMap<Object, Object> cargarProteccionesPrivado (Connection con, int numeroSolicitud, int centroCosto, int institucion)
	{
		HashMap<Object, Object> mapa= new HashMap<Object, Object>();
		logger.info("llega sql cargarprotecciones");
		mapa.put("numRegistros", "0");
		String consultaStr="";
		
		if(centroCosto>0)
		{
			consultaStr=	"SELECT " +
									"paic.codigo AS codigo_proteccion_inst_cc,  " +
									"pa.codigo AS codigo_proteccion, " +
									"coalesce(pa.nombre, '') AS nombre, " +
									"existeProteccionSol("+numeroSolicitud+", pa.codigo) as checkeado, " +
									"(select p.codigo from protecciones_hoja_anestesia p where p.numero_solicitud="+numeroSolicitud+" and p.proteccion=pa.codigo) as codigoproteccionhojaanestesia " +
								"FROM " +
									"protecciones_anest_inst_cc paic " +
									"INNER JOIN protecciones_anestesia pa ON (paic.proteccion=pa.codigo) " +
								"WHERE " +
									"paic.centro_costo="+centroCosto+" " +
									"and paic.institucion="+institucion+" " +
									"and paic.activo='"+ConstantesBD.acronimoSi+"' "+
								"ORDER BY pa.nombre ";
		}
		else
		{
			consultaStr=	"SELECT " +
								"paic.codigo AS codigo_proteccion_inst_cc,  " +
								"pa.codigo AS codigo_proteccion, " +
								"coalesce(pa.nombre, '') AS nombre, " +
								"existeProteccionSol("+numeroSolicitud+", pa.codigo) as checkeado, " +
								"(select p.codigo from protecciones_hoja_anestesia p where p.numero_solicitud="+numeroSolicitud+" and p.proteccion=pa.codigo) as codigoproteccionhojaanestesia " +
							"FROM " +
								"protecciones_anest_inst_cc paic " +
								"INNER JOIN protecciones_anestesia pa ON (paic.proteccion=pa.codigo) " +
							"WHERE " +
								"paic.centro_costo is null " +
								"and paic.institucion="+institucion+" " +
								"and paic.activo='"+ConstantesBD.acronimoSi+"' "+
							"ORDER BY pa.nombre ";
		}
		
		logger.info("\n cargarProteccion-->"+consultaStr);
		
		PreparedStatementDecorator ps=null;
		try 
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			mapa= UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
			
			for(int w=0; w<Utilidades.convertirAEntero(mapa.get("numRegistros")+""); w++)
			{
				mapa.put("estabd_"+w, mapa.get("checkeado_"+w));
			}
			
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
	 * @param mapaSignoVitalAnestesia
	 * @return
	 */
    public static boolean insertarProtecciones(Connection con, HashMap<Object, Object> mapa)
    {
    	String cadena="INSERT INTO protecciones_hoja_anestesia 	(codigo, " +     			//1
	    														"numero_solicitud, " +		//2
	    														"proteccion_inst_cc, " +	//3
	    														"proteccion, " +			//4
	    														"fecha_modifica, " +		
	    														"hora_modifica, " +			
	    														"usuario_modifica) " +		//5
	    														"values (?, ?, " +
	    																" ?, ?, " +
	    																"CURRENT_DATE, " +
	    																""+ValoresPorDefecto.getSentenciaHoraActualBD()+", " +
	    																"?)";
    	
    	logger.info("\n insertar proteccion->"+mapa.get("codigoproteccion").toString()+" proinstcc->"+mapa.get("codigoproteccioninstcc").toString()+"\n");
    	
    	try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			int codigo= UtilidadBD.obtenerSiguienteValorSecuencia(con, "salascirugia.seq_protecc_hoja_anestesia");
			ps.setInt(1, codigo);
			ps.setInt(2, Utilidades.convertirAEntero(mapa.get("numerosolicitud").toString()));
			ps.setInt(3, Utilidades.convertirAEntero(mapa.get("codigoproteccioninstcc").toString()));
			ps.setInt(4, Utilidades.convertirAEntero(mapa.get("codigoproteccion").toString()));
			ps.setString(5, mapa.get("loginusuario")+"");
			
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
    public static boolean eliminarProteccion(Connection con, int codigoProteccionHojaAnestesia)
    {
    	String cadena="DELETE FROM protecciones_hoja_anestesia WHERE codigo=? ";
							
		logger.info("\n ELIMINAR-->"+cadena+" codigo-->"+codigoProteccionHojaAnestesia+"\n");
    	
    	try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, codigoProteccionHojaAnestesia);
			
			if(ps.executeUpdate()>0)
				return true;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return false;
    }
    
    //***********************************************FIN PROTECCIONES**************************************************************//
    
    
    //***********************************************PESO Y TALLA**************************************************************//
    
    /**
     * 
     */
    public static Vector<String> cargarPesoTalla(Connection con, int numeroSolicitud, int institucion)
    {
    	Vector<String> vector= new Vector<String>();
    	String cadena="select coalesce(peso||'', ''), coalesce(talla||'', '') from hoja_anestesia where numero_solicitud =?";
    	try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, numeroSolicitud);
			
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{	
				vector.add(0,rs.getString(1));
				vector.add(1,rs.getString(2));
			}
			
			if(vector.size()<=0)
			{
				vector.add(0,cargarPesoTallaValoracionPreanestesia(con, numeroSolicitud, institucion, ConstantesBD.codigoTipoExamenFisicoPrePeso));
				vector.add(1,cargarPesoTallaValoracionPreanestesia(con, numeroSolicitud, institucion, ConstantesBD.codigoTipoExamenFisicoPreTalla));
			}
			else
			{
				if(UtilidadTexto.isEmpty(vector.get(0)))
				{
					vector.add(0,cargarPesoTallaValoracionPreanestesia(con, numeroSolicitud, institucion, ConstantesBD.codigoTipoExamenFisicoPrePeso));
					vector.add(1,cargarPesoTallaValoracionPreanestesia(con, numeroSolicitud, institucion, ConstantesBD.codigoTipoExamenFisicoPreTalla));
				}
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return vector;
    }
    
    /**
     * 
     * @param con
     * @param numeroSolicitud
     * @param institucion
     * @return
     */
    private static String cargarPesoTallaValoracionPreanestesia(Connection con, int numeroSolicitud, int institucion, int tipo) 
    {
    	String resultado="";
    	String consulta="SELECT " +
							"vp.valor " +
						"FROM " +
							"val_pre_exam_fisico_text vp " +
							"INNER JOIN examen_fisico_pre_inst ef ON(ef.codigo=vp.examen_fisico_pre_inst) " +
						"WHERE " +
							"ef.examen_fisico_pre="+tipo+" " +
							"and ef.institucion="+institucion+" " +
							"and activo="+ValoresPorDefecto.getValorTrueParaConsultas()+" " +
							"and vp.val_preanestesia= (select sc.codigo_peticion from solicitudes_cirugia sc where sc.numero_solicitud=? ) ";
		
		try
		{
			logger.info("\n cargarPesoTallaValoracionPreanestesia-->"+consulta+" sol->"+numeroSolicitud);
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, numeroSolicitud);
			
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			
			if(rs.next())
			{	
				resultado=rs.getString(1);
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return resultado;
	}

	/**
     * 
     * @param con
     * @param mapa
     * @return
     */
    public static boolean modificarPesoTalla(Connection con, float peso, float talla, int numeroSolicitud)
    {
    	String cadena="UPDATE hoja_anestesia SET " +
    						"peso=?, " +		//1
							"talla=? " +		//2
						"WHERE "+
							"numero_solicitud=? "; 		//3
							
							
    	logger.info("\n Modificar-->"+cadena+"\n");
    	
    	try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			if(peso>0)
				ps.setDouble(1, peso);
			else
				ps.setNull(1, Types.DOUBLE);
			if(talla>0)
				ps.setDouble(2, talla);
			else
				ps.setNull(2, Types.DOUBLE);
			ps.setInt(3, numeroSolicitud);
			
			if(ps.executeUpdate()>0)
				return true;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return false;
    }
    
    //***********************************************FIN PESO Y TALLA**************************************************************//
 
    /**
     * Metodo que obtiene la ultima fecha hora de las secciones graficables del record de anestesia
     * @param tipoBD 
     */
    public static String[] obtenerUltimaFechaHoraGraficarRecord(Connection con, int numeroSolicitud, int tipoBD)
    {
    	String[] fechaHora= {"",""};
    	String consulta=	"SELECT " +
    							"to_char(t.fecha,'DD/MM/YYYY') AS fecha, " +
    							"CASE WHEN length(t.hora)=4 then '0'||t.hora ELSE t.hora end AS hora " +
    						"FROM " +
    						"("+ 	
	    						//primero los signos vitales
	    						"(" +
	    							"SELECT " +
	    								"fecha as fecha, " +
	    								"hora as hora " +
	    							"from " +
	    								"tiempo_signo_vital_anest " +
	    							"where " +
	    								"solicitud_hoja_anest=? " +
	    								"and graficar='"+ConstantesBD.acronimoSi+"' " +
	    						")" +
	    						//segundo los eventos
	    						"UNION " +
	    						"(" +
	    							"SELECT " +
	    								"fecha_inicial as fecha, " +
	    								"hora_inicial as hora " +
	    							"from " +
	    								"eventos_hoja_anestesia " +
	    							"where " +
	    								"numero_solicitud=? " +
	    								"and graficar='"+ConstantesBD.acronimoSi+"' " +
	    						")" +
	    						"UNION " +
	    						"(" +
	    							"SELECT " +
	    								"fecha_final as fecha, " +
	    								"hora_final as hora " +
	    							"from " +
	    								"eventos_hoja_anestesia " +
	    							"where " +
	    								"numero_solicitud=? " +
	    								"and graficar='"+ConstantesBD.acronimoSi+"' " +
	    								"and fecha_final is not null " +
	    								"and hora_final is not null " +
	    						")" +
	    						"UNION " +
	    						"(" +
	    							"SELECT " +
	    								"sc.fecha_inicia_anestesia as fecha, " +
	    								"sc.hora_inicia_anestesia as hora " +
	    							"FROM " +
	    								"hoja_anestesia sc  " +
	    							"WHERE " +
	    								"sc.numero_solicitud =? " +
	    								"and sc.fecha_inicia_anestesia is not null " +
	    								"and sc.hora_inicia_anestesia is not null " +
	    						")" +
	    						"UNION " +
	    						"(" +
	    							"SELECT " +
	    								"sc.fecha_finaliza_anestesia as fecha, " +
	    								"sc.hora_finaliza_anestesia as horafinal " +
	    							"FROM " +
	    								"hoja_anestesia sc  " +
	    							"WHERE " +
	    								"sc.numero_solicitud =? " +
	    								"and sc.fecha_finaliza_anestesia is not null " +
	    								"and sc.hora_finaliza_anestesia is not null " +
	    						")" +
	    						"UNION " +
	    						"(" +
	    							"SELECT " +
	    								"sc.fecha_inicial_cx as fecha, " +
	    								"sc.hora_inicial_cx as horainicial " +
	    							"FROM " +
	    								"solicitudes_cirugia sc " +
	    							"WHERE " +
	    								"sc.numero_solicitud =? " +
	    								"and sc.fecha_inicial_cx is not null " +
	    								"and sc.hora_inicial_cx is not null " +
	    						")" +
	    						"UNION " +
	    						"(" +
	    							"SELECT " +
	    								"sc.fecha_final_cx as fecha, " +
	    								"sc.hora_final_cx as hora " +
	    							"FROM " +
	    								"solicitudes_cirugia sc " +
	    							"WHERE " +
	    								"sc.numero_solicitud =? " +
	    								"and sc.fecha_final_cx is not null " +
	    								"and sc.hora_final_cx is not null " +
	    						")" +
	    						//tercero los gases
	    						"UNION " +
	    						"(" +
	    							"SELECT " +
	    								"fecha_inicial as fecha, " +
	    								"hora_inicial as hora " +
	    							"from " +
	    								"gases_hoja_anestesia " +
	    							"where " +
	    								"numero_solicitud=? " +
	    								"and graficar='"+ConstantesBD.acronimoSi+"' " +
	    						") " +
	    						//cuarto medicamentos 
	    						"UNION " +
	    						"(" +
	    							"SELECT " +
	    								"da.fecha as fecha, " +
	    								"da.hora as hora " +
	    							"from " +
	    								"admin_hoja_anestesia aha " +
	    								"inner join det_admin_hoja_anes da ON (da.admin_hoja_anes=aha.codigo) " +
	    							"where " +
	    								"aha.numero_solicitud=? " +
	    								"and da.graficar='"+ConstantesBD.acronimoSi+"' " +
	    								"and da.fecha is not null " +
	    								"and da.hora is not null " +
	    						") " +
	    						"UNION " +
	    						//quinto infusiones
	    						"(" +
	    							"SELECT " +
	    								"adm.fecha AS fecha, " +
	    								"adm.hora as hora " +
	    							"FROM " +
	    								"infusiones_hoja_anes iha " +
	    								"INNER JOIN mezclas_inst_cc mcc ON (mcc.consecutivo = iha.mezcla_inst_cc) " +
	    								"INNER JOIN adm_infusiones_hoja_anes adm ON (adm.cod_info_hoja_anes = iha.codigo) " +
	    							"WHERE " +
	    								"iha.numero_solicitud = ? " +
	    								"and iha.graficar='"+ConstantesBD.acronimoSi+"' " +
	    								"and adm.fecha is not null " +
	    								"and adm.hora is not null " +
	    						")" +
	    					")t ";
    	
    	if(tipoBD==DaoFactory.ORACLE)
    	{
    		consulta += " WHERE ROWNUM <= 1 ";
    	}
    	consulta += "ORDER BY fecha desc, hora desc  ";
    	if(tipoBD==DaoFactory.POSTGRESQL)
    	{
    		consulta += ValoresPorDefecto.getValorLimit1()+" 1 ";
    	}
    	
    	try
		{
			logger.info("\n obtenerUltimaFechaHoraGraficarRecord-->"+consulta+" sol->"+numeroSolicitud);
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, numeroSolicitud);
			ps.setInt(2, numeroSolicitud);
			ps.setInt(3, numeroSolicitud);
			ps.setInt(4, numeroSolicitud);
			ps.setInt(5, numeroSolicitud);
			ps.setInt(6, numeroSolicitud);
			ps.setInt(7, numeroSolicitud);
			ps.setInt(8, numeroSolicitud);
			ps.setInt(9, numeroSolicitud);
			ps.setInt(10, numeroSolicitud);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			
			if(rs.next())
			{	
				fechaHora[0]=rs.getString("fecha");
				fechaHora[1]=rs.getString("hora");
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
    	
		logger.info("Resultado--->"+fechaHora[0]+" hora->"+fechaHora[1]);
		
		return fechaHora;
    }
    
}
