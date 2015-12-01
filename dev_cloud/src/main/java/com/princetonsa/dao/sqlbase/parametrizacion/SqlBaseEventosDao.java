package com.princetonsa.dao.sqlbase.parametrizacion;

import java.sql.Connection;
import java.sql.Date;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.princetonsa.dto.salas.DtoColor;
import com.princetonsa.dto.salas.DtoEventos;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;


/**
 * 
 * @author wilson
 *
 */
public class SqlBaseEventosDao 
{
	/**
	* Objeto para manejar los logs de esta clase
	*/
	private static Logger logger= Logger.getLogger(SqlBaseEventosDao.class);
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @param centroCosto
	 * @param institucion
	 * @return
	 */
	public static HashMap<Object, Object> cargarSubseccionesEventos (Connection con, int numeroSolicitud, int centroCosto, int institucion)
	{
		HashMap<Object, Object> mapaSecciones= new HashMap<Object, Object>();
		mapaSecciones.put("numRegistros", "0");
		int tipo=1;
		
		//1. CARGAMOS LOS INSERTADOS EN BD
		while((Utilidades.convertirAEntero(mapaSecciones.get("numRegistros").toString())==0) || tipo==2)
		{	
			mapaSecciones=  obtenerSubseccionesEventos(con, numeroSolicitud, centroCosto, institucion, tipo);
			tipo++;
		}
		return mapaSecciones;
	}
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @param centroCosto
	 * @param institucion
	 * @param tipo, 1= xcentro costo especifico, 2= centro costo null
	 * @return
	 */
	private static HashMap<Object, Object> obtenerSubseccionesEventos(Connection con, int numeroSolicitud, int centroCosto, int institucion, int tipo)
	{
		HashMap<Object, Object> mapa= new HashMap<Object, Object>();
		//primero se carga los historicos, 
		String consultaStr=	"(SELECT " +
								"DISTINCT " +
								"eaic.codigo AS codigo_evento_inst_cc, " +
								"eaic.codigo_evento as evento, " +
								"coalesce(ea.nombre, '') AS nombre, " +
								"ea.orden "+
							"FROM " +
								"eventos_hoja_anestesia eha " +
								"INNER JOIN eventos_anest_inst_cc eaic ON(eha.codigo_evento_inst_cc=eaic.codigo) " +
								"INNER JOIN eventos_anestesia ea ON (eaic.codigo_evento=ea.codigo) " +
								"INNER JOIN tipos_color tc ON (ea.color=tc.codigo) " +
							"WHERE " +
								"eha.numero_solicitud="+numeroSolicitud+" " +
							//NO FUNCIONA EN ORACLE//"ORDER BY ea.orden " +
							" ) " +
							" UNION ";
		
		if(tipo==1)
		{
			consultaStr+=	"(SELECT " +
								"DISTINCT " +
								"eaic.codigo AS codigo_evento_inst_cc, " +
								"eaic.codigo_evento as evento, " +
								"coalesce(ea.nombre, '') AS nombre, " +
								"ea.orden "+
							"FROM " +
								"eventos_anest_inst_cc eaic " +
								"INNER JOIN eventos_anestesia ea ON (eaic.codigo_evento=ea.codigo) " +
								"INNER JOIN tipos_color tc ON (ea.color=tc.codigo) " +
							"WHERE " +
								"eaic.centro_costo="+centroCosto+" " +
								"and eaic.institucion="+institucion+" " +
								"and eaic.activo='"+ConstantesBD.acronimoSi+"' "+
							//no funiona en oracle//" ORDER BY ea.orden" +
							" ) ";
		}
		else if(tipo==2)
		{
			consultaStr+=	"(SELECT " +
								"DISTINCT " +
								"eaic.codigo AS codigo_evento_inst_cc, " +
								"eaic.codigo_evento as evento, " +
								"coalesce(ea.nombre, '') AS nombre, " +
								"ea.orden "+
							"FROM " +
								"eventos_anest_inst_cc eaic " +
								"INNER JOIN eventos_anestesia ea ON (eaic.codigo_evento=ea.codigo) " +
								"INNER JOIN tipos_color tc ON (ea.color=tc.codigo) " +
							"WHERE " +
								"eaic.centro_costo is null "+
								"and eaic.institucion="+institucion+" " +
								"and eaic.activo='"+ConstantesBD.acronimoSi+"' "+
							//no funiona en oracle //"ORDER BY ea.orden" +
							" ) ";
		}
		
		logger.info("\n obtenerSubseccionesEventos->"+consultaStr+"\n");
		
		PreparedStatementDecorator ps=null;
		try 
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
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
	 * @param codigoEvento
	 * @return
	 */
	public static DtoEventos cargarEvento(Connection con, int codigoEvento)
	{
		DtoEventos dtoEvento= new DtoEventos();
		String cadena= "SELECT ea.nombre, ea.orden, ea.obligatorio, ea.convencion_inicio, ea.convencion_fin, ea.color, tc.descripcion as nombrecolor, tc.valor_hexa, ea.n_registros, ea.lleva_fecha_fin from eventos_anestesia ea inner join tipos_color tc on(tc.codigo=ea.color) WHERE ea.codigo=?";
		//logger.info("cargar evento-->"+cadena+" ->"+codigoEvento);
		
		PreparedStatementDecorator ps=null;
		try 
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, codigoEvento);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			
			while(rs.next())
			{
				DtoColor dtoColor= new DtoColor(rs.getInt("color"), rs.getString("nombrecolor"), rs.getString("valor_hexa"));
				dtoEvento.setConvencionFin(rs.getString("convencion_fin"));
				dtoEvento.setConvencionInicio(rs.getString("convencion_inicio"));
				dtoEvento.setDtoColor(dtoColor);
				dtoEvento.setNombre(rs.getString("nombre"));
				dtoEvento.setObligatorio(UtilidadTexto.getBoolean(rs.getString("obligatorio")));
				dtoEvento.setOrden(rs.getInt("orden"));
				dtoEvento.setCodigo(codigoEvento);
				dtoEvento.setNRegistros(UtilidadTexto.getBoolean(rs.getString("n_registros")));
				dtoEvento.setLlevaFechaFin(UtilidadTexto.getBoolean(rs.getString("lleva_fecha_fin")));
			}
			
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
		return dtoEvento;
	}
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @param codigoEventoInstCC
	 * @param codigoEvento
	 * @return
	 */
	public static HashMap<Object, Object> cargarEventoHojaAnestesia (Connection con, int numeroSolicitud, int codigoEventoInstCC, int codigoEvento)
	{
		//primero se carga el dto de eventos
		DtoEventos dtoEventos= cargarEvento(con, codigoEvento);
		HashMap<Object, Object> mapa= new HashMap<Object, Object>();
		
		// como puede manejar n registros entonces no se carga un Dto sino un mapa
		String cadena= "SELECT " +
							"codigo as codigoeventohojaanestesia, " +
							"to_char(fecha_inicial, 'DD/MM/YYYY') as fechainicial, " +
							"hora_inicial||'' as horainicial, "+
							"case when fecha_final is null then '' else to_char(fecha_final, 'DD/MM/YYYY') end as fechafinal, " +
							"coalesce(hora_final,'') as horafinal, " +
							"coalesce(consecutivo_otros_eventos||'', '') as consecutivootroseventos, " +
							"desc_otros_eventos as descotroseventos, " +
							"graficar as graficar, " +
							" '"+ConstantesBD.acronimoSi+"' as estabd " +
						"FROM " +
							"eventos_hoja_anestesia " +
						"WHERE " +
							"numero_solicitud =? " +
							"AND codigo_evento_inst_cc="+codigoEventoInstCC+" " +
							"order by fechainicial, horainicial  ";
		
		if(codigoEvento==ConstantesBD.codigoEventoHojaAnestesiaInicioFinCx)
		{
			cadena="SELECT " +
						ConstantesBD.codigoNuncaValido+" as codigoeventohojaanestesia, " +
						"case when sc.fecha_inicial_cx is null then '' else to_char(sc.fecha_inicial_cx, 'DD/MM/YYYY') end as fechainicial, " +
						"coalesce(sc.hora_inicial_cx ||'', '') as horainicial, "+
						"case when sc.fecha_final_cx is null then '' else to_char(sc.fecha_final_cx, 'DD/MM/YYYY') end as fechafinal, " +
						"coalesce(sc.hora_final_cx ||'','') as horafinal, " +
						" '' as consecutivootroseventos, " +
						" '' as descotroseventos, " +
						"'"+ConstantesBD.acronimoSi+"' as graficar, " +
						" '"+ConstantesBD.acronimoSi+"' as estabd " +
					"FROM " +
						"solicitudes_cirugia sc " +
					"WHERE " +
						"sc.numero_solicitud =? " +
						"order by fechainicial, horainicial  ";
		}
		if(codigoEvento==ConstantesBD.codigoEventoHojaAnestesiaInicioFinAnestesia)
		{
			cadena="SELECT " +
						ConstantesBD.codigoNuncaValido+" as codigoeventohojaanestesia, " +
						"case when sc.fecha_inicia_anestesia is null then '' else to_char(sc.fecha_inicia_anestesia, 'DD/MM/YYYY') end as fechainicial, " +
						"coalesce(sc.hora_inicia_anestesia ||'', '') as horainicial, "+
						"case when sc.fecha_finaliza_anestesia is null then '' else to_char(sc.fecha_finaliza_anestesia, 'DD/MM/YYYY') end as fechafinal, " +
						"coalesce(sc.hora_finaliza_anestesia ||'','') as horafinal, " +
						" '' as consecutivootroseventos, " +
						" '' as descotroseventos, " +
						"'"+ConstantesBD.acronimoSi+"' as graficar, " +
						" '"+ConstantesBD.acronimoSi+"' as estabd " +
					"FROM " +
						"hoja_anestesia sc  " +
					"WHERE " +
						"sc.numero_solicitud =? " +
						"order by fechainicial, horainicial  ";
		}
		
		
		logger.info("\ncargarEventoHojaAnestesia->"+cadena+" -->numsol="+numeroSolicitud+"  eventoCC="+codigoEventoInstCC+"\n");
		
		PreparedStatementDecorator ps=null;
		try 
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, numeroSolicitud);
			mapa= UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		}	
		catch (SQLException e) 
		{
			e.printStackTrace();
			return null;
		}
		
		mapa.put("DTOEVENTO", dtoEventos);
		
		return mapa;
	}
	
	/**
	 * 
	 * @param con
	 * @param mapaSignoVitalAnestesia
	 * @return
	 */
    public static boolean insertar(Connection con, HashMap<Object, Object> mapa)
    {
    	String cadena="INSERT INTO eventos_hoja_anestesia 	(codigo, " +     			//1
    														"numero_solicitud, " +		//2
    														"codigo_evento_inst_cc, " +	//3
    														"fecha_inicial, " +			//4
    														"fecha_final, " +			//5
    														"hora_inicial, " +			//6
    														"hora_final, " +			//7
    														"consecutivo_otros_eventos, " +//8
    														"desc_otros_eventos, " +	//9	
    														"fecha_modifica, " +		
    														"hora_modifica, " +			
    														"usuario_modifica, " +		//10
    														"graficar) " +				//11
    														"values (?, ?, ?," +
    																" ?, ?, ?," +
    																" ?, ?, ?, " +
    																"CURRENT_DATE, " +
    																""+ValoresPorDefecto.getSentenciaHoraActualBD()+", " +
    																"?, ?)";
    	
    	try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			int codigo= UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_eventos_hoja_anestesia");
			ps.setInt(1, codigo);
			ps.setInt(2, Utilidades.convertirAEntero(mapa.get("numerosolicitud").toString()));
			ps.setInt(3, Utilidades.convertirAEntero(mapa.get("codigoeventoinstcc").toString()));
			ps.setDate(4, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(mapa.get("fechainicial").toString())));
			
			if(UtilidadFecha.esFechaValidaSegunAp(mapa.get("fechafinal")+""))
				ps.setDate(5, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(mapa.get("fechafinal").toString())));
			else
				ps.setNull(5, Types.DATE);
			
			ps.setString(6, mapa.get("horainicial")+"");
			
			if(UtilidadFecha.validacionHora(mapa.get("horafinal")+"").puedoSeguir)			
				ps.setString(7, mapa.get("horafinal")+"");
			else
				ps.setNull(7, Types.VARCHAR);
			
			if(UtilidadTexto.isNumber(mapa.get("consecutivootroseventos")+""))
				ps.setInt(8, Utilidades.convertirAEntero(mapa.get("consecutivootroseventos")+""));
			else
				ps.setNull(8, Types.INTEGER);
			
			if(!UtilidadTexto.isEmpty(mapa.get("descotroseventos")+""))
				ps.setString(9, mapa.get("descotroseventos")+"");
			else
				ps.setNull(9, Types.VARCHAR);
			
			ps.setString(10, mapa.get("loginusuario")+"");
			ps.setString(11, mapa.get("graficar")+"");
				
			
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
    	String cadena="UPDATE eventos_hoja_anestesia SET " +
    						"fecha_inicial=?, " +			//1
							"fecha_final=?, " +				//2
							"hora_inicial=?, " +			//3
							"hora_final=?, " +				//4
							"consecutivo_otros_eventos=?, " +//5
							"desc_otros_eventos=?, " +		//6	
							"fecha_modifica= CURRENT_DATE, " +		
							"hora_modifica= "+ValoresPorDefecto.getSentenciaHoraActualBD()+", " +							
							"usuario_modifica=?, " +		//7
							"graficar=? " +					//8
						"WHERE "+
							"codigo=? "; 					//9
							
							
    	//logger.info("\n Modificar-->"+cadena+" MAOPA-->"+mapa+"\n");
    	
    	try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setDate(1, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(mapa.get("fechainicial").toString())));
			
			if(UtilidadFecha.esFechaValidaSegunAp(mapa.get("fechafinal")+""))
				ps.setDate(2, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(mapa.get("fechafinal").toString())));
			else
				ps.setNull(2, Types.DATE);
			
			ps.setString(3, mapa.get("horainicial")+"");
			
			if(UtilidadFecha.validacionHora(mapa.get("horafinal")+"").puedoSeguir)			
				ps.setString(4, mapa.get("horafinal")+"");
			else
				ps.setNull(4, Types.VARCHAR);
			
			if(UtilidadTexto.isNumber(mapa.get("consecutivootroseventos")+""))
				ps.setInt(5, Utilidades.convertirAEntero(mapa.get("consecutivootroseventos")+""));
			else
				ps.setNull(5, Types.INTEGER);
			
			if(!UtilidadTexto.isEmpty(mapa.get("descotroseventos")+""))
				ps.setString(6, mapa.get("descotroseventos")+"");
			else
				ps.setNull(6, Types.VARCHAR);
			
			ps.setString(7, mapa.get("loginusuario")+"");
			ps.setString(8, mapa.get("graficar")+"");
		
			ps.setInt(9, Utilidades.convertirAEntero(mapa.get("codigoeventohojaanestesia")+""));
			
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
    public static boolean eliminar(Connection con, int codigoHojaAnestesia)
    {
    	String cadena="DELETE FROM eventos_hoja_anestesia WHERE codigo=? ";
							
		//logger.info("\n ELIMINAR-->"+cadena+" codigo-->"+codigoHojaAnestesia+"\n");
    	
    	try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, codigoHojaAnestesia);
			
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
     * @param codigoHojaAnestesia
     * @return
     */
    public static boolean actualizarConsecutivoOtrosEventos(Connection con, int codigoHojaAnestesia)
    {
    	String cadena="UPDATE eventos_hoja_anestesia " +
    							"SET " +
    								"consecutivo_otros_eventos=(consecutivo_otros_eventos-1) " +
    							"where " +
    								"consecutivo_otros_eventos>(select eha.consecutivo_otros_eventos FROM eventos_hoja_anestesia eha WHERE eha.codigo=?) " +
    								"and numero_solicitud=(select eha.numero_solicitud FROM eventos_hoja_anestesia eha WHERE eha.codigo=?) " +
    								"and codigo_evento_inst_cc in(select eha.codigo_evento_inst_cc FROM eventos_hoja_anestesia eha INNER JOIN eventos_anest_inst_cc eaic on(eaic.codigo=eha.codigo_evento_inst_cc) WHERE eha.codigo=? AND eaic.codigo_evento="+ConstantesBD.codigoEventoHojaAnestesiaOtros+" ) ";
    	
    	//logger.info("\n ACTUALIZAR CONSECUTIVOS OTROS EVENTOS-->"+cadena+" codigo-->"+codigoHojaAnestesia+"\n");
    	
    	try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, codigoHojaAnestesia);
			ps.setInt(2, codigoHojaAnestesia);
			ps.setInt(3, codigoHojaAnestesia);
			
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
     * @param fechaInicial
     * @param horaInicial
     * @param fechaFinal
     * @param horaFinal
     * @param codigoEventoInstCC
     * @param codigoEventoHojaAnestesia
     * @param numeroSolicitud
     * @return
     */
    public static boolean existeCruceFechasEventos(Connection con, String fechaInicial, String horaInicial, String fechaFinal, String horaFinal, int codigoEventoInstCC, int codigoEventoHojaAnestesia, int numeroSolicitud)
    {
    	String cadena=	"SELECT " +
    						"codigo " +
    					"from " +
    						"eventos_hoja_anestesia " +
    					"where " +
    						"numero_solicitud="+numeroSolicitud+" " +
    						"and codigo_evento_inst_cc="+codigoEventoInstCC+" " +
    						"and codigo<>"+codigoEventoHojaAnestesia+" "+
    						//primero verifica si la fecha inicial esta entre fecha inicial y final
    						"and (('"+UtilidadFecha.conversionFormatoFechaABD(fechaInicial)+" "+horaInicial+"'::timestamp) between ((fecha_inicial ||' '||hora_inicial) ::timestamp) and ((fecha_final||' '||hora_final) ::timestamp) " +
    						//segundo verifica si la fecha final esta entre fecha inicial y final
    						"or ('"+UtilidadFecha.conversionFormatoFechaABD(fechaFinal)+" "+horaFinal+"'::timestamp) between ((fecha_inicial ||' '||hora_inicial) ::timestamp) and ((fecha_final||' '||hora_final) ::timestamp) " +
    						//tercero q la fecha inicial sea menor a la fecha de bd inicial y la fecha final sea mayor a la fecha bd final 
    						"or (('"+UtilidadFecha.conversionFormatoFechaABD(fechaInicial)+" "+horaInicial+"'::timestamp) < ((fecha_inicial ||' '||hora_inicial) ::timestamp) and  ('"+UtilidadFecha.conversionFormatoFechaABD(fechaFinal)+" "+horaFinal+"'::timestamp) > ((fecha_final||' '||hora_final) ::timestamp))) ";
    	
    	//logger.info("\n existeCruceFechasEventos-->"+cadena+"\n");
    	
    	try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
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
     * @param numeroSolicitud
     * @return
     */
    public static HashMap<Object, Object> consultaEventosMinutos(Connection con, int numeroSolicitud)
    {
    	HashMap<Object, Object> mapa= new HashMap<Object, Object>();
    	mapa.put("numRegistros", "0");
    	int cont=0;
    	
    	String consulta="select ea.nombre, " +
    							"ea.convencion_inicio as convencion_inicio, " +
    							"ea.convencion_fin as convencion_fin, " +
    							"to_char(eha.fecha_inicial, 'DD/MM/YYYY') as fecha_inicial, " +
    							"eha.hora_inicial as hora_inicial, " +
    							"to_char(eha.fecha_final, 'DD/MM/YYYY') as fecha_final, " +
    							"eha.hora_final as hora_final " +
    						"from " +
    							"eventos_hoja_anestesia eha " +
    							"inner join eventos_anest_inst_cc eaic on(eaic.codigo=eha.codigo_evento_inst_cc) " +
    							"inner join eventos_anestesia ea on(ea.codigo=eaic.codigo_evento) " +
    						"where eha.numero_solicitud=? " +
    							"and eha.fecha_final is not null " +
    							"and eha.hora_final is not null " +
    						"order by eha.fecha_inicial, eha.hora_inicial, eha.fecha_final, eha.hora_final ";
    	
    	//logger.info("\n consultaEventosMinutos->"+consulta+" numsol->"+numeroSolicitud);
    	
    	PreparedStatementDecorator ps=null;
		try 
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, numeroSolicitud);
			
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			{
				int numeroMinutosDif=UtilidadFecha.numeroMinutosEntreFechas(rs.getString("fecha_inicial"), rs.getString("hora_inicial"), rs.getString("fecha_final"), rs.getString("hora_final"));
				mapa.put("nombre_"+cont, rs.getString("nombre"));
				mapa.put("convencion_inicio_"+cont, rs.getString("convencion_inicio"));
				mapa.put("convencion_fin_"+cont, rs.getString("convencion_fin"));
				mapa.put("minutos_"+cont, numeroMinutosDif);
				cont++;
			}
			mapa.put("numRegistros", cont);
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
     * @param fechaInicio
     * @param horaInicio
     * @param fechaFin
     * @param horaFin
     * @param numeroSolicitud
     * @return
     */
    public static boolean actualizarInicioFinHojaAnestesia(Connection con, String fechaInicio, String horaInicio, String fechaFin, String horaFin, int numeroSolicitud)
    {
    	String cadena="UPDATE hoja_anestesia SET fecha_inicia_anestesia=?, hora_inicia_anestesia=?, fecha_finaliza_anestesia=?, hora_finaliza_anestesia=? WHERE numero_solicitud=? ";
    	
    	try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setDate(1, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(fechaInicio)));
			//logger.info("fechaInicio->"+UtilidadFecha.conversionFormatoFechaABD(fechaInicio));
			ps.setString(2, horaInicio);
			//logger.info("horaInicio->"+horaInicio);
			ps.setDate(3, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(fechaFin)));
			//logger.info("fechaFin->"+UtilidadFecha.conversionFormatoFechaABD(fechaFin));
			ps.setString(4, horaFin);
			//logger.info("horaFin->"+horaFin);
			ps.setInt(5, numeroSolicitud);
			//logger.info("numsol->"+numeroSolicitud);
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
     * @param fechaInicio
     * @param horaInicio
     * @param fechaFin
     * @param horaFin
     * @param numeroSolicitud
     * @return
     */
    public static boolean actualizarInicioFinCx(Connection con, String fechaInicio, String horaInicio, String fechaFin, String horaFin, int numeroSolicitud)
    {
    	String cadena="UPDATE solicitudes_cirugia SET fecha_inicial_cx=?, hora_inicial_cx=?, fecha_final_cx=?, hora_final_cx=? where numero_solicitud=?";  
    	
    	try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setDate(1, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(fechaInicio)));
			ps.setString(2, horaInicio);
			ps.setDate(3, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(fechaFin)));
			ps.setString(4, horaFin);
			ps.setInt(5, numeroSolicitud);
			
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
     * Consulta las fechas de los eventos los cuales sean obligatorios 
     * y posea el indicador lleva_fecha_fin como lo indica el parametro
     * @param Connection con
     * @param HashMap parametros
     * */
    public static HashMap consultarEventosHojaAnesLlevaFin(Connection con, HashMap parametros)
    {
    	PreparedStatementDecorator ps=null;
    	HashMap mapa = new HashMap();
    	
    	String consulta = "SELECT " +
    			"eha.fecha_inicial," +
    			"eha.hora_inicial," +
    			"eha.fecha_final," +
    			"eha.hora_final, " +
    			"ea.lleva_fecha_fin," +
    			"ea.nombre " +
    			"FROM " +
    			"eventos_hoja_anestesia eha " +
    			"INNER JOIN eventos_anest_inst_cc eacc ON (eacc.codigo = eha.codigo_evento_inst_cc AND eacc.activo = '"+ConstantesBD.acronimoSi+"') " +
    			"INNER JOIN eventos_anestesia ea ON (ea.codigo = eacc.codigo_evento AND ea.lleva_fecha_fin = '"+parametros.get("llevaFechaFin").toString()+"' AND ea.obligatorio = '"+ConstantesBD.acronimoSi+"') " +
    			"WHERE eha.numero_solicitud = ? ";
    	
    	try 
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1,Utilidades.convertirAEntero(parametros.get("numeroSolicitud")+""));
			
			mapa = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()),true,true);
			
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
     * @param numeroSolicitud
     * @param graficar
     * @return
     */
	public static HashMap<Object, Object> cargarEventosHojaAnestesia (Connection con, int numeroSolicitud, String graficar)
	{
		//primero se carga el dto de eventos
		HashMap<Object, Object> mapa= new HashMap<Object, Object>();
		
		// como puede manejar n registros entonces no se carga un Dto sino un mapa
		String cadena= "(SELECT " +
							"eha.codigo as codigoeventohojaanestesia, " +
							"to_char(eha.fecha_inicial, 'DD/MM/YYYY') as fecha, " +
							"eha.hora_inicial as hora, "+
							"coalesce(eha.consecutivo_otros_eventos||'', '') as consecutivootroseventos, " +
							"eha.desc_otros_eventos as descotroseventos, " +
							"eha.graficar as graficar, " +
							"ea.convencion_inicio as convencion, " +
							"t.valor_hexa as valor_hexa, " +
							"ea.n_registros as n_registros, " +
							"ea.nombre as nomevento " +
						"FROM " +
							"eventos_hoja_anestesia eha " +
							"inner join eventos_anest_inst_cc eai on(eai.codigo=eha.codigo_evento_inst_cc) " +
							"inner join eventos_anestesia ea on(ea.codigo=eai.codigo_evento) " +
							"inner join tipos_color t on(t.codigo=ea.color) " +
						"WHERE " +
							"eha.numero_solicitud =? ";
		
		if(graficar.equals(ConstantesBD.acronimoSi))
			cadena+="and eha.graficar='"+ConstantesBD.acronimoSi+"' ";
		else if(graficar.equals(ConstantesBD.acronimoNo)) 
			cadena+="and eha.graficar='"+ConstantesBD.acronimoNo+"' ";
							
		cadena+=	" )  ";
		
		cadena+="UNION ";
		cadena+= "(SELECT " +
					"eha.codigo as codigoeventohojaanestesia, " +
					"case when eha.fecha_final is null then '' else to_char(eha.fecha_final, 'DD/MM/YYYY') end as fecha, " +
					"coalesce(eha.hora_final,'') as hora, " +
					"coalesce(eha.consecutivo_otros_eventos||'', '') as consecutivootroseventos, " +
					"eha.desc_otros_eventos as descotroseventos, " +
					"eha.graficar as graficar, " +
					"coalesce(ea.convencion_fin, '') as convencion, " +
					"t.valor_hexa as valor_hexa, " +
					"ea.n_registros as n_registros, " +
					"ea.nombre as nomevento " +
				"FROM " +
					"eventos_hoja_anestesia eha " +
					"inner join eventos_anest_inst_cc eai on(eai.codigo=eha.codigo_evento_inst_cc) " +
					"inner join eventos_anestesia ea on(ea.codigo=eai.codigo_evento) " +
					"inner join tipos_color t on(t.codigo=ea.color) " +
				"WHERE " +
					"eha.numero_solicitud =? " +
					"AND fecha_final IS NOT NULL ";
		
		if(graficar.equals(ConstantesBD.acronimoSi))
			cadena+="and eha.graficar='"+ConstantesBD.acronimoSi+"' ";
		else if(graficar.equals(ConstantesBD.acronimoNo)) 
			cadena+="and eha.graficar='"+ConstantesBD.acronimoNo+"' ";
							
		cadena+=	" )  ";
		
		
		//se le une la informacion de la cx
		cadena+=	"UNION " +
				"(SELECT " +
					ConstantesBD.codigoNuncaValido+" as codigoeventohojaanestesia, " +
					"to_char(sc.fecha_inicial_cx, 'DD/MM/YYYY') as fecha, " +
					"coalesce(sc.hora_inicial_cx ||'', '') as hora, "+
					"'' as consecutivootroseventos, " +
					"'' as descotroseventos, "+
					"'"+ConstantesBD.acronimoSi+"' as graficar, " +
					"(SELECT ea.convencion_inicio from eventos_anestesia ea WHERE ea.codigo="+ConstantesBD.codigoEventoHojaAnestesiaInicioFinCx+") as convencion, " +
					"(SELECT t.valor_hexa as valor_hexa from eventos_anestesia ea inner join tipos_color t on(t.codigo=ea.color) WHERE ea.codigo="+ConstantesBD.codigoEventoHojaAnestesiaInicioFinCx+") as color, " +
					"(SELECT ea.n_registros from eventos_anestesia ea WHERE ea.codigo="+ConstantesBD.codigoEventoHojaAnestesiaInicioFinCx+") as n_registros, " +
					"(SELECT ea.nombre from eventos_anestesia ea WHERE ea.codigo="+ConstantesBD.codigoEventoHojaAnestesiaInicioFinCx+") as nomevento " +
				"FROM " +
					"solicitudes_cirugia sc " +
				"WHERE " +
					"sc.numero_solicitud =? " +
					//no funciona en oracle//"order by fecha, hora" +
					" )  ";
		
		cadena+=	"UNION " +
					"(SELECT " +
						ConstantesBD.codigoNuncaValido+" as codigoeventohojaanestesia, " +
						"to_char(sc.fecha_final_cx, 'DD/MM/YYYY') as fecha, " +
						"coalesce(sc.hora_final_cx ||'', '') as hora, "+
						"'' as consecutivootroseventos, " +
						"'' as descotroseventos, "+
						"'"+ConstantesBD.acronimoSi+"' as graficar, " +
						"(SELECT ea.convencion_fin from eventos_anestesia ea WHERE ea.codigo="+ConstantesBD.codigoEventoHojaAnestesiaInicioFinCx+") as convencion, " +
						"(SELECT t.valor_hexa as valor_hexa from eventos_anestesia ea inner join tipos_color t on(t.codigo=ea.color) WHERE ea.codigo="+ConstantesBD.codigoEventoHojaAnestesiaInicioFinCx+") as color, " +
						"(SELECT ea.n_registros from eventos_anestesia ea WHERE ea.codigo="+ConstantesBD.codigoEventoHojaAnestesiaInicioFinCx+") as n_registros, " +
						"(SELECT ea.nombre from eventos_anestesia ea WHERE ea.codigo="+ConstantesBD.codigoEventoHojaAnestesiaInicioFinCx+") as nomevento " +
					"FROM " +
						"solicitudes_cirugia sc " +
					"WHERE " +
						"sc.numero_solicitud =? " +
						//no funciona en oracle//"order by fecha, hora" +
						" )  ";
		
		//se le une la informacion de la anestesia
		cadena+=	"UNION " +
					"(SELECT " +
					ConstantesBD.codigoNuncaValido+" as codigoeventohojaanestesia, " +
					"case when sc.fecha_inicia_anestesia is null then '' else to_char(sc.fecha_inicia_anestesia, 'DD/MM/YYYY') end as fecha, " +
					"coalesce(sc.hora_inicia_anestesia ||'', '') as hora, "+
					"'' as consecutivootroseventos, " +
					"'' as descotroseventos, "+
					"'"+ConstantesBD.acronimoSi+"' as graficar, " +
					"(SELECT ea.convencion_inicio from eventos_anestesia ea WHERE ea.codigo="+ConstantesBD.codigoEventoHojaAnestesiaInicioFinAnestesia+") as convencion, " +
					"(SELECT t.valor_hexa from eventos_anestesia ea inner join tipos_color t on(t.codigo=ea.color) WHERE ea.codigo="+ConstantesBD.codigoEventoHojaAnestesiaInicioFinAnestesia+") as color, " +
					"(SELECT ea.n_registros from eventos_anestesia ea WHERE ea.codigo="+ConstantesBD.codigoEventoHojaAnestesiaInicioFinAnestesia+") as n_registros, " +
					"(SELECT ea.nombre from eventos_anestesia ea WHERE ea.codigo="+ConstantesBD.codigoEventoHojaAnestesiaInicioFinAnestesia+") as nomevento " +
				"FROM " +
					"hoja_anestesia sc  " +
				"WHERE " +
					"sc.numero_solicitud =? " +
					//no funciona en oracle// "order by fecha, hora" +
					" )  ";
		
		cadena+=	"UNION " +
					"(SELECT " +
					ConstantesBD.codigoNuncaValido+" as codigoeventohojaanestesia, " +
					"case when sc.fecha_finaliza_anestesia is null then '' else to_char(sc.fecha_finaliza_anestesia, 'DD/MM/YYYY') end as fecha, " +
					"coalesce(sc.hora_finaliza_anestesia ||'', '') as hora, "+
					"'' as consecutivootroseventos, " +
					"'' as descotroseventos, "+
					"'"+ConstantesBD.acronimoSi+"' as graficar, " +
					"(SELECT ea.convencion_fin from eventos_anestesia ea WHERE ea.codigo="+ConstantesBD.codigoEventoHojaAnestesiaInicioFinAnestesia+") as convencion, " +
					"(SELECT t.valor_hexa from eventos_anestesia ea inner join tipos_color t on(t.codigo=ea.color) WHERE ea.codigo="+ConstantesBD.codigoEventoHojaAnestesiaInicioFinAnestesia+") as color, " +
					"(SELECT ea.n_registros from eventos_anestesia ea WHERE ea.codigo="+ConstantesBD.codigoEventoHojaAnestesiaInicioFinAnestesia+") as n_registros, " +
					"(SELECT ea.nombre from eventos_anestesia ea WHERE ea.codigo="+ConstantesBD.codigoEventoHojaAnestesiaInicioFinAnestesia+") as nomevento " +
				"FROM " +
					"hoja_anestesia sc  " +
				"WHERE " +
					"sc.numero_solicitud =? " +
					//no funciona en oracle//"order by fecha, hora" +
					" )  ";
		
		logger.info("\ncargarEventosHojaAnestesia->"+cadena+" -->numsol="+numeroSolicitud+"\n");
		
		PreparedStatementDecorator ps=null;
		try 
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, numeroSolicitud);
			ps.setInt(2, numeroSolicitud);
			ps.setInt(3, numeroSolicitud);
			ps.setInt(4, numeroSolicitud);
			ps.setInt(5, numeroSolicitud);
			ps.setInt(6, numeroSolicitud);
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