package com.princetonsa.dao.sqlbase.historiaClinica;

import java.sql.Connection;
import java.sql.Date;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.util.HashMap;


import org.apache.log4j.Logger;


import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.UtilidadValidacion;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.historiaClinica.UtilidadesHistoriaClinica;

public class SqlBaseContrarreferenciaDao 
{
	
	private static Logger logger=Logger.getLogger(SqlBaseContrarreferenciaDao.class);
	
	
	
	/**
	 * Cadena que inserta una nueva referencia
	 */
	private static final String cadenaInsertarStr = "INSERT INTO contrarreferencia (numero_referencia_contra, institucion, institucion_sirc_origen, consecutivo_punto_atencion, anio_consecutivo, institucion_sirc_destino, fecha_remision, hora_remision, hallazgos_clinicos, examen_fisicos, tratamientos_instaurados, recomendaciones, relacion_anexos, profesional_responde, estado, usuario_finaliza, fecha_finaliza, hora_finaliza, usuario_modifica, fecha_modifica, hora_modifica) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	
	
	
	private static final String cadenaModificacionStr="UPDATE contrarreferencia SET institucion=?, institucion_sirc_origen=?, consecutivo_punto_atencion=?, anio_consecutivo=?, institucion_sirc_destino=?, fecha_remision=?, hora_remision=?, hallazgos_clinicos=?, examen_fisicos=?, tratamientos_instaurados=?, recomendaciones=?, relacion_anexos=?, profesional_responde=?, estado=?, usuario_finaliza=?, fecha_finaliza=?, hora_finaliza=?, usuario_modifica=?,  fecha_modifica=?, hora_modifica=?  WHERE numero_referencia_contra=?";
	
	
	
	private static final String cadenaConsultaStr=  "SELECT " +
													" r.numero_referencia as numeroreferencia," +
													" to_char(r.fecha_referencia, 'DD/MM/YYYY') as fechareferencia," +
													" r.hora_referencia as horareferencia," +
													" r.ingreso as ingreso,"+
													" r.tipo_atencion as tipoatencion," +
													" r.institucion_sirc_solicita as institucionsircsolicita," +
													" s.descripcion  as nombreinstitucionsolicita," +
													" 'BD' as tiporegistro, " +
													" s.tipo_red as tipred " +
													" from referencia r" +
													" inner join instituciones_sirc s ON (s.codigo=r.institucion_sirc_solicita and s.institucion=r.institucion) " +
													" inner join ingresos g ON (r.ingreso=g.id) " +
													" left outer join contrarreferencia cr on cr.numero_referencia_contra=r.numero_referencia "+
													"where r.referencia = '"+ConstantesIntegridadDominio.acronimoExterna+"' AND r.estado = '"+ConstantesIntegridadDominio.acronimoEstadoAdmitido+"' AND (cr.numero_referencia_contra is NULL or cr.estado='"+ConstantesIntegridadDominio.acronimoEstadoPendiente+"') AND (g.fecha_egreso is NULL and g.hora_egreso is NULL) ";
													
	
	
	
	
	private static final String cadenaConsultaAreaStr=  "SELECT " +
													" r.centro_costo as centrocosto, " +
													" r.numero_referencia as numeroreferencia," +
													" r.institucion_sirc_solicita || '-' || CASE WHEN r.consecutivo_punto_atencion IS NULL THEN '' ELSE r.consecutivo_punto_atencion END AS solicitud,  " +
													" getnombrepersona(r.codigo_paciente) As paciente, " +
													" to_char(r.fecha_referencia, 'DD/MM/YYYY') as fechareferencia," +
													" r.hora_referencia as horareferencia," +
													" r.ingreso as ingreso,"+
													" r.tipo_atencion as tipoatencion," +
													" r.institucion_sirc_solicita as institucionsircsolicita," +
													" s.descripcion  as nombreinstitucionsolicita," +
													" r.codigo_paciente as codigopaciente," +
													" 'BD' as tiporegistro, " +
													" s.tipo_red as tipred " +
													" from referencia r" +
													" inner join instituciones_sirc s ON (s.codigo=r.institucion_sirc_solicita and s.institucion=r.institucion) " +
													" inner join ingresos g ON (r.ingreso=g.id) " +
													" left outer join contrarreferencia cr on cr.numero_referencia_contra=r.numero_referencia "+
													"where r.referencia = '"+ConstantesIntegridadDominio.acronimoExterna+"' AND r.estado = '"+ConstantesIntegridadDominio.acronimoEstadoAdmitido+"' AND (cr.numero_referencia_contra is NULL or cr.estado='"+ConstantesIntegridadDominio.acronimoEstadoPendiente+"') AND (g.fecha_egreso is NULL and g.hora_egreso is NULL) ";
	
	
	
	private static final String cadenaConsultaContraStr=  "SELECT " +
														"cr.numero_referencia_contra as numerocontrarreferencia, " +
														"cr.institucion as institucion, " +
														"cr.institucion_sirc_origen as institucionsircorigen, " +
														"cr.consecutivo_punto_atencion as consecutivopuntoatencion, " +
														"cr.anio_consecutivo as anioconsecutivo, " +
														"cr.institucion_sirc_destino as institucionsircdestino, " +
														"cr.fecha_remision as fecharemision, " +
														"cr.hora_remision as horaremision, " +
														"cr.hallazgos_clinicos as hallazgosclinicos, " +
														"cr.examen_fisicos as examenfisicos, " +
														"cr.tratamientos_instaurados as tratamientosinstaurados, " +
														"cr.recomendaciones as recomendaciones, " +
														"cr.relacion_anexos as relacionanexos, " +
														"cr.profesional_responde as profesionalresponde, " +
														"cr.estado as estado, " +
														"cr.usuario_finaliza as usuariofinaliza, " +
														"cr.fecha_finaliza as fechafinaliza, " +
														"cr.hora_finaliza as horafinaliza, " +
														"cr.usuario_modifica as usuariomodifica, " +
														"cr.fecha_modifica as fechamodifica, " +
														"cr.hora_modifica as horamodifica, " +
														" s.descripcion  as nombreinstitucionorigen," +
														" s1.descripcion  as nombreinstituciondestino," +
														" 'BD' as tiporegistro, " +
														" s.tipo_red as tipred, " +
														" s1.tipo_red as tipred1 "+
														" from contrarreferencia cr" +
														" inner join instituciones_sirc s ON (s.codigo=cr.institucion_sirc_origen and s.institucion=cr.institucion) " +
														" left outer join instituciones_sirc s1 ON (s1.codigo=cr.institucion_sirc_destino and s1.institucion=cr.institucion) " +
														" where 1=1 ";
														
	
	
	
	private static final String cadenaConsultaProcedimientoStr=  "SELECT " +
																"r.numero_referencia_contra as numerocontrarreferencia, " +
																"r.numero_solicitud as numerosolicitud, " +
																"r.hora as hora, " +
																"r.descripcion as descripcion, " +
																"r.interpretacion as interpretacion, " +
																"to_char(r.fecha,'dd/mm/yyyy') as fecha, " +
	
																" 'BD' as tiporegistro " +
																" from resul_proc_contrarreferencia r" +
																
																" where numero_referencia_contra=? ";
	
	
	
	private static final String cadenaConsultaDiagnosticos="SELECT " +
																	" dc.acronimo_diagnostico||'"+ConstantesBD.separadorSplit+"'||dc.tipo_cie||'"+ConstantesBD.separadorSplit+"'||d.nombre as diagnostico, " +
																	" dc.principal " +
														" from diagnosticos_contrarreferencia dc " +
														" inner join diagnosticos d on(d.acronimo=dc.acronimo_diagnostico and d.tipo_cie=dc.tipo_cie) " +
														" where numero_referencia_contra=?";
	
	/**
	 * 
	 */
	private static final String insertarDiagnosticoStr = "INSERT INTO diagnosticos_contrarreferencia (numero_referencia_contra,acronimo_diagnostico,tipo_cie,principal) VALUES (?,?,?,?)";

	
	/**
	 * Cadena que elimina un diagnostico de la contrarreferencia
	 */
	private static final String eliminarDiagnosticoStr = "DELETE FROM diagnosticos_contrarreferencia WHERE numero_referencia_contra = ?";
	
	
	
	
	
	/**
	 * Cadena que inserta un resultado examen diagnostico a la contrarreferencia
	 */
	private static final String insertarProcedimientosStr = "INSERT INTO resul_proc_contrarreferencia (numero_referencia_contra,numero_solicitud,descripcion,interpretacion,hora,fecha) VALUES (?,?,?,?,?,?)";
	
	/**
	 * Cadena que elimina un resultado examen diagnostico de la contrarreferencia
	 */
	private static final String eliminarProcedimientosStr = "DELETE FROM resul_proc_contrarreferencia WHERE numero_referencia_contra=? and numero_solicitud=?";
	
	
	
	
	
	
	/**
	 * Cadena que inserta las conductas a seguir en la contrarreferencia
	 */
	private static final String insertarConductaStr = "INSERT INTO cond_seguir_contrarreferenc (numero_referencia_contra,codigo_conducta_seguir,valor) VALUES (?,?,?)";
	
	/**
	 * Cadena que elimina las conductas a seguir para la contrarreferencia
	 */
	private static final String eliminarConductaStr = "DELETE FROM cond_seguir_contrarreferenc WHERE numero_referencia_contra = ? and codigo_conducta_seguir = ?";
	
	
	
	
	
	private static final String actualizarEstadoContrarreferenciaStr = "UPDATE contrarreferencia SET " +
	"estado = ?, " +
	"fecha_modifica = ?, " +
	"hora_modifica = ?, " +
	"usuario_modifica = ? " +
	"WHERE numero_referencia_contra = ?";
	
	
	
	
	
	
	
	
	private static final String busquedaInstitucionesSircStr = "SELECT " +
																"codigo,institucion,descripcion, tipo_red " +
																"FROM instituciones_sirc " +
																"WHERE " +
																"institucion = ? AND " +
																"tipo_inst_referencia = '"+ConstantesBD.acronimoSi+"' AND " +
																"activo = '"+ConstantesBD.acronimoSi+"' ";
	
	
	/**
	 * Cadena que consulta los diagnosticos de la evolucion
	 */
	private static final String consultarDiagnosticosEvolucionStr = "SELECT "+
																		" ed.acronimo_diagnostico||'"+ConstantesBD.separadorSplit+"'||ed.tipo_cie_diagnostico||'"+ConstantesBD.separadorSplit+"'||d.nombre as diagnostico, " +
																		" case when ed.principal = '"+ValoresPorDefecto.getValorTrueCortoParaConsultas() + "' then '"+ConstantesBD.acronimoSi+"' else '"+ConstantesBD.acronimoNo+"' end as principal "+
																		"FROM evol_diagnosticos ed " +
																		"inner join diagnosticos d on(d.acronimo=ed.acronimo_diagnostico and d.tipo_cie=ed.tipo_cie_diagnostico) "+ 
																		"WHERE "+
																		"ed.evolucion = ? "+  
																		"order by ed.numero";
	
	/**
	 * Cadena que consulta los diagnosticos de la valoracion
	 */
	private static final String consultarDiagnosticosValoracionStr = "SELECT "+
																		"vd.acronimo_diagnostico||'"+ConstantesBD.separadorSplit+"'||vd.tipo_cie_diagnostico||'"+ConstantesBD.separadorSplit+"'||d.nombre as diagnostico, " +
																		"CASE WHEN vd.principal =  '"+ValoresPorDefecto.getValorTrueCortoParaConsultas()+"' THEN '"+ConstantesBD.acronimoSi+"'  ELSE '"+ConstantesBD.acronimoNo+"' END AS principal "+
																		"FROM val_diagnosticos vd " +
																		"inner join diagnosticos d on(d.acronimo=vd.acronimo_diagnostico and d.tipo_cie=vd.tipo_cie_diagnostico) "+  
																		"WHERE "+
																		"vd.valoracion = ? "+ 
																		"order by vd.numero";
	
	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public static HashMap busquedaInstitucionesSirc(Connection con,HashMap vo)
	{
		PreparedStatementDecorator pst = null;
		try
		{
			String consulta = busquedaInstitucionesSircStr;
			if(vo.get("tipoReferencia").toString().equals(ConstantesIntegridadDominio.acronimoInterna))
				consulta += "  AND codigo || '-' || institucion IN ";
			else
				consulta += "  AND codigo || '-' || institucion NOT IN ";
			
			
			consulta += " (SELECT CASE WHEN codigo_inst_sirc IS NULL THEN '' ELSE codigo_inst_sirc || '-' || cod_institucion END FROM centro_atencion) " +
				"ORDER BY descripcion ";
			
		 pst =  new PreparedStatementDecorator(con.prepareStatement(consulta));
			pst.setInt(1,Utilidades.convertirAEntero(vo.get("institucion")+""));
			
			HashMap mapaRetorno= UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()));
			pst.close();
			return mapaRetorno;
		}
		catch(SQLException e)
		{
			logger.error("Error en busquedaInstitucionesSirc: "+e);
			return null;
		}finally{
			if (pst != null){
				try{
					pst.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseContrarreferenciaDao "+sqlException.toString() );
				}
			}
		}
	}

	
	
	
	public static HashMap consultarReferenciaPaciente(Connection con, HashMap vo) 
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros","0");
		String cadena= cadenaConsultaStr;
		PreparedStatementDecorator ps  = null; 
		try
		{
			if(vo.containsKey("numeroReferencia"))
			{
				cadena+=" and r.numero_referencia='"+vo.get("numeroReferencia")+"'";
				
			}
			cadena+=" AND r.codigo_paciente="+vo.get("codigoPaciente")+" ORDER BY numero_referencia ";
			logger.info("\n\n Cadena Consulta ContraReferencia x Paciente >> "+cadena);
			ps= new PreparedStatementDecorator(con.prepareStatement(cadena));
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}finally{
			if (ps != null){
				try{
					ps.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseContrarreferenciaDao "+sqlException.toString() );
				}
			}
		}
		return mapa;
		
		
	}
	
	
	public static HashMap consultarReferenciaArea(Connection con, HashMap vo) 
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros","0");
		String cadena= cadenaConsultaAreaStr;
		
		PreparedStatementDecorator ps= null;
		try
		{
			if(vo.containsKey("centrosCosto"))
			{
				cadena+=" and r.centro_costo='"+vo.get("centrosCosto")+"' ";
			}
			
			if(vo.containsKey("numeroReferencia"))
			{
				cadena+=" and r.numero_referencia='"+vo.get("numeroReferencia")+"'";
				
			}
			cadena+=" ORDER BY numero_referencia ";
			ps= new PreparedStatementDecorator(con.prepareStatement(cadena));
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}finally{
			if (ps != null){
				try{
					ps.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseContrarreferenciaDao "+sqlException.toString() );
				}
			}
		}
		return mapa;
		
		
	}
	
	
	
	
	public static HashMap consultarContrarreferencia(Connection con, HashMap vo) 
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros","0");
		String cadena= cadenaConsultaContraStr;
		
		PreparedStatementDecorator ps = null;
		try
		{
			if(vo.containsKey("numeroContrarreferencia"))
			{
				cadena+=" and cr.numero_referencia_contra='"+vo.get("numeroContrarreferencia")+"'";
				
			}
			cadena+=" ORDER BY numero_referencia_contra ";
			ps= new PreparedStatementDecorator(con.prepareStatement(cadena));
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()),false,false);
		}
		catch (SQLException e)
		{
			logger.info("Error en consultarContrarreferencia: "+e);
		}finally{
			if (ps != null){
				try{
					ps.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseContrarreferenciaDao "+sqlException.toString() );
				}
			}
		}
		return mapa;
		
		
	}
	
	
	
	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public static boolean insertar(Connection con, HashMap vo) 
	{
		/*try
		{
			
			String consecutivo = ""; //consecutivo del registro de contrarreferencia
			String[] vector = new String[0]; //vector auxiliar
			int resp = 0;*/
		PreparedStatementDecorator ps =  null;
	
			
			try
			{
				 ps =  new PreparedStatementDecorator(con.prepareStatement(cadenaInsertarStr));
				
				/**
				 * INSERT INTO contrarreferencia (
				 * numero_referencia_contra, 
				 * institucion, 
				 * institucion_sirc_origen, 
				 * consecutivo_punto_atencion, 
				 * anio_consecutivo, 
				 * institucion_sirc_destino, 
				 * fecha_remision, 
				 * hora_remision, 
				 * hallazgos_clinicos, 
				 * examen_fisicos, 
				 * tratamientos_instaurados, 
				 * recomendaciones, 
				 * relacion_anexos, 
				 * profesional_responde, 
				 * estado, 
				 * usuario_finaliza, 
				 * fecha_finaliza, 
				 * hora_finaliza, 
				 * usuario_modifica, 
				 * fecha_modifica, 
				 * hora_modifica) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
				 */
				
				
				ps.setDouble(1, Utilidades.convertirADouble(vo.get("numero_referencia_contra")+""));
				ps.setInt(2, Utilidades.convertirAEntero(vo.get("institucion")+""));
				
				if(UtilidadTexto.isEmpty(vo.get("institucion_sirc_origen")+""))
					ps.setObject(3, null);
				else
					ps.setString(3, vo.get("institucion_sirc_origen")+"");
				
				if(UtilidadTexto.isEmpty(vo.get("consecutivo_punto_atencion")+""))
					ps.setObject(4, null);
				else
					ps.setString(4, vo.get("consecutivo_punto_atencion")+"");
				
				if(UtilidadTexto.isEmpty(vo.get("anio_consecutivo")+""))
					ps.setObject(5, null);
				else
					ps.setString(5, vo.get("anio_consecutivo")+"");
				
				if(UtilidadTexto.isEmpty(vo.get("institucion_sirc_destino")+""))
					ps.setObject(6, null);
				else
					ps.setString(6, vo.get("institucion_sirc_destino")+"");
				
				if(UtilidadTexto.isEmpty(vo.get("fecha_remision")+""))
					ps.setObject(7, null);
				else
					ps.setDate(7,Date.valueOf( UtilidadFecha.conversionFormatoFechaABD(vo.get("fecha_remision")+"")));
				
				if(UtilidadTexto.isEmpty(vo.get("hora_remision")+""))
					ps.setObject(8, null);
				else
					ps.setString(8, vo.get("hora_remision")+"");
				
				if(UtilidadTexto.isEmpty(vo.get("hallazgos_clinicos")+""))
					ps.setObject(9, null);
				else
					ps.setString(9, vo.get("hallazgos_clinicos")+"");
				
				if(UtilidadTexto.isEmpty(vo.get("examen_fisicos")+""))
					ps.setObject(10, null);
				else
					ps.setString(10, vo.get("examen_fisicos")+"");
				
				if(UtilidadTexto.isEmpty(vo.get("tratamientos_instaurados")+""))
					ps.setObject(11, null);
				else
					ps.setString(11, vo.get("tratamientos_instaurados")+"");
				
				if(UtilidadTexto.isEmpty(vo.get("recomendaciones")+""))
					ps.setObject(12, null);
				else
					ps.setString(12, vo.get("recomendaciones")+"");
				
				if(UtilidadTexto.isEmpty(vo.get("relacion_anexos")+""))
					ps.setObject(13, null);
				else
					ps.setString(13, vo.get("relacion_anexos")+"");
				
				if(UtilidadTexto.isEmpty(vo.get("profesional_responde")+"")||Utilidades.convertirAEntero(vo.get("profesional_responde")+"")<=0)
					ps.setObject(14, null);
				else
					ps.setInt(14, Utilidades.convertirAEntero(vo.get("profesional_responde")+""));
				
				if(UtilidadTexto.isEmpty(vo.get("estado")+""))
					ps.setObject(15, null);
				else
					ps.setString(15, vo.get("estado")+"");
				
				if(UtilidadTexto.isEmpty(vo.get("usuario_finaliza")+""))
					ps.setObject(16, null);
				else
					ps.setString(16, vo.get("usuario_finaliza")+"");
				
				if(UtilidadTexto.isEmpty(vo.get("fecha_finaliza")+""))
					ps.setObject(17, null);
				else
					ps.setDate(17,Date.valueOf( UtilidadFecha.conversionFormatoFechaABD(vo.get("fecha_finaliza")+"")));
				
				if(UtilidadTexto.isEmpty(vo.get("hora_finaliza")+""))
					ps.setObject(18, null);
				else
					ps.setString(18, vo.get("hora_finaliza")+"");
				
				ps.setString(19, vo.get("usuario_modifica")+"");
				ps.setDate(20,Date.valueOf( UtilidadFecha.conversionFormatoFechaABD(vo.get("fecha_modifica")+"")));
				ps.setString(21, vo.get("hora_modifica")+"");
				
				
				return ps.executeUpdate()>0;
			}
			catch (SQLException e)
			{
				e.printStackTrace();
			}finally{
				if (ps != null){
					try{
						ps.close();
					}catch(SQLException sqlException){
						logger.warn(sqlException+" Error al cerrar el recurso SqlBaseContrarreferenciaDao "+sqlException.toString() );
					}
				}
			}
			return false;
	}
	
	
			
	
	public static boolean modificar(Connection con, HashMap vo)
	{
		PreparedStatementDecorator ps= null;
		try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(cadenaModificacionStr));
			
			/**
			 * UPDATE contrarreferencia SET 
			 * institucion=?, 
			 * institucion_sirc_origen=?, 
			 * consecutivo_punto_atencion=?, 
			 * anio_consecutivo=?, 
			 * institucion_sirc_destino=?, 
			 * fecha_remision=?, 
			 * hora_remision=?, 
			 * hallazgos_clinicos=?, 
			 * examen_fisicos=?, 
			 * tratamientos_instaurados=?, 
			 * recomendaciones=?, 
			 * relacion_anexos=?, 
			 * profesional_responde=?, 
			 * estado=?, 
			 * usuario_finaliza=?, 
			 * fecha_finaliza=?, 
			 * hora_finaliza=?, 
			 * usuario_modifica=?,  
			 * fecha_modifica=?, 
			 * hora_modifica=?  WHERE numero_referencia_contra=?
			 */
			
			
			ps.setInt(1, Utilidades.convertirAEntero(vo.get("institucion")+""));
			
			if(UtilidadTexto.isEmpty(vo.get("institucion_sirc_origen")+""))
				ps.setObject(2, null);
			else
				ps.setString(2, vo.get("institucion_sirc_origen")+"");
			
			if(UtilidadTexto.isEmpty(vo.get("consecutivo_punto_atencion")+""))
				ps.setObject(3, null);
			else
				ps.setString(3, vo.get("consecutivo_punto_atencion")+"");
			
			if(UtilidadTexto.isEmpty(vo.get("anio_consecutivo")+""))
				ps.setObject(4, null);
			else
				ps.setString(4, vo.get("anio_consecutivo")+"");
			
			if(UtilidadTexto.isEmpty(vo.get("institucion_sirc_destino")+""))
				ps.setObject(5, null);
			else
				ps.setString(5, vo.get("institucion_sirc_destino")+"");
			
			if(UtilidadTexto.isEmpty(vo.get("fecha_remision")+""))
				ps.setObject(6, null);
			else
				ps.setDate(6, Date.valueOf( UtilidadFecha.conversionFormatoFechaABD(vo.get("fecha_remision")+"")));
			
			if(UtilidadTexto.isEmpty(vo.get("hora_remision")+""))
				ps.setObject(7, null);
			else
				ps.setString(7, vo.get("hora_remision")+"");
			
			if(UtilidadTexto.isEmpty(vo.get("hallazgos_clinicos")+""))
				ps.setObject(8, null);
			else
				ps.setString(8, vo.get("hallazgos_clinicos")+"");
			
			if(UtilidadTexto.isEmpty(vo.get("examen_fisicos")+""))
				ps.setObject(9, null);
			else
				ps.setString(9, vo.get("examen_fisicos")+"");
			
			if(UtilidadTexto.isEmpty(vo.get("tratamientos_instaurados")+""))
				ps.setObject(10, null);
			else
				ps.setString(10, vo.get("tratamientos_instaurados")+"");
			
			if(UtilidadTexto.isEmpty(vo.get("recomendaciones")+""))
				ps.setObject(11, null);
			else
				ps.setString(11, vo.get("recomendaciones")+"");
			
			if(UtilidadTexto.isEmpty(vo.get("relacion_anexos")+""))
				ps.setObject(12, null);
			else
				ps.setString(12, vo.get("relacion_anexos")+"");
			
			if(UtilidadTexto.isEmpty(vo.get("profesional_responde")+""))
				ps.setObject(13, null);
			else
				ps.setInt(13, Utilidades.convertirAEntero(vo.get("profesional_responde")+""));
			
			if(UtilidadTexto.isEmpty(vo.get("estado")+""))
				ps.setObject(14, null);
			else
				ps.setString(14, vo.get("estado")+"");
			
			if(UtilidadTexto.isEmpty(vo.get("usuario_finaliza")+""))
				ps.setObject(15, null);
			else
				ps.setString(15, vo.get("usuario_finaliza")+"");
			
			if(UtilidadTexto.isEmpty(vo.get("fecha_finaliza")+""))
				ps.setObject(16, null);
			else
				ps.setDate(16, Date.valueOf( UtilidadFecha.conversionFormatoFechaABD(vo.get("fecha_finaliza")+"")));
			
			if(UtilidadTexto.isEmpty(vo.get("hora_finaliza")+""))
				ps.setObject(17, null);
			else
				ps.setString(17, vo.get("hora_finaliza")+"");
			
			ps.setString(18, vo.get("usuario_modifica")+"");
			ps.setDate(19, Date.valueOf( UtilidadFecha.conversionFormatoFechaABD(vo.get("fecha_modifica")+"")));
			ps.setString(20, vo.get("hora_modifica")+"");
			ps.setDouble(21, Utilidades.convertirADouble(vo.get("numero_referencia_contra")+""));
			
			return ps.executeUpdate()>0;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}finally{
			if (ps != null){
				try{
					ps.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseContrarreferenciaDao "+sqlException.toString() );
				}
			}
		}
		return false;
	}
	
	
	
	
	

	/**
	 * 
	 * @param con
	 * @param numeroReferenciaContra
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static HashMap cargarConductasSeguirContrareferencia(Connection con, int numeroReferenciaContra) 
	{
		String cadena="";
		PreparedStatementDecorator ps= null;
		HashMap mapa=new HashMap();
		mapa.put("numRegistros", "0");
		if(numeroReferenciaContra>0)
		{
			cadena=" SELECT codigo AS codigo,descripcion AS descripcion,requiere_valor as requierevalor,coalesce(valor,'') as valor,case when csc.codigo_conducta_seguir is null then '"+ConstantesBD.acronimoNo+"' else '"+ConstantesBD.acronimoSi+"' end as activo,case when csc.codigo_conducta_seguir is null then 'MEM' else 'BD' end as tiporegistro  from conds_seguir_contrarreferenc cc left outer join cond_seguir_contrarreferenc csc on(csc.codigo_conducta_seguir=cc.codigo and numero_referencia_contra="+numeroReferenciaContra+") where codigo<>"+ConstantesBD.codigoConductaASeguirContrareferenciaOtro+"  ";
			cadena+=" UNION all SELECT codigo,descripcion,requiere_valor as requierevalor,'' as valor,'"+ConstantesBD.acronimoNo+"' as activo,'MEM' as tiporegistro from conds_seguir_contrarreferenc where codigo="+ConstantesBD.codigoConductaASeguirContrareferenciaOtro;
		}
		else
		{
			cadena="SELECT codigo,descripcion,requiere_valor as requierevalor,'' as valor,'"+ConstantesBD.acronimoNo+"' as activo,'MEM' as tiporegistro from conds_seguir_contrarreferenc ";
			
		}
		 
		 cadena+=" ORDER BY descripcion";
		
		try
		{
			logger.info("\n CADENA Conductas a seguir >> "+cadena);
			ps= new PreparedStatementDecorator(con.prepareStatement(cadena));
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}finally{
			if (ps != null){
				try{
					ps.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseContrarreferenciaDao "+sqlException.toString() );
				}
			}
		}
		return mapa;
	}
	
	
	
	
	
	public static int actualizarEstadoContrarreferencia(Connection con,HashMap vo)
	{
		PreparedStatementDecorator ps = null;
		try
		{
			 ps =  new PreparedStatementDecorator(con.prepareStatement(actualizarEstadoContrarreferenciaStr));
			
			/**
			 * "UPDATE contrarreferencia SET " +
						"estado = ?, " +
						"fecha_modifica = ?, " +
						"hora_modifica = ?, " +
						"usuario_modifica = ? " +
						"WHERE numero_referencia_contra = ?";
			 */
			
			ps.setString(1, vo.get("estado")+"");
			ps.setDate(2, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(vo.get("fecha_modifica")+"")));
			ps.setString(3, vo.get("hora_modifica")+"");
			ps.setString(4, vo.get("usuario_modifica")+"");
			ps.setDouble(5, Utilidades.convertirADouble(vo.get("numero_referencia_contra")+""));
			
			return ps.executeUpdate();
		
		}
		catch(SQLException e)
		{
			logger.error("Error en actualizarEstadoContrarreferencia :"+e);
			return 0;
		}finally{
			if (ps != null){
				try{
					ps.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseContrarreferenciaDao "+sqlException.toString() );
				}
			}
		}
	}
	
	
	
	
	
	public static HashMap consultarResultadosProcedimiento(Connection con, int numeroReferencia) 
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros","0");
		PreparedStatementDecorator ps= null;
		try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(cadenaConsultaProcedimientoStr));
			ps.setDouble(1, Utilidades.convertirADouble(numeroReferencia+""));
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}finally{
			if (ps != null){
				try{
					ps.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseContrarreferenciaDao "+sqlException.toString() );
				}
			}
		}
		return mapa;
		
		
	}
	
	
	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public static boolean insertarProcedimientos(Connection con, HashMap vo) 
	{
		PreparedStatementDecorator ps = null;
		try
		{
			 ps =  new PreparedStatementDecorator(con.prepareStatement(insertarProcedimientosStr));
			
			/**
			 * INSERT INTO resul_proc_contrarreferencia (
			 * numero_referencia_contra,
			 * numero_solicitud,
			 * descripcion,
			 * interpretacion,
			 * hora,
			 * fecha) VALUES (?,?,?,?,?,?)
			 */
			
			ps.setDouble(1, Utilidades.convertirADouble(vo.get("numero_referencia_contra")+""));
			ps.setInt(2, Utilidades.convertirAEntero(vo.get("numero_solicitud")+""));
			ps.setString(3, vo.get("descripcion")+"");
			ps.setString(4, vo.get("interpretacion")+"");
			ps.setString(5, vo.get("hora")+"");
			ps.setDate(6,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(vo.get("fecha")+"")));

			return ps.executeUpdate()>0;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}finally{
			if (ps != null){
				try{
					ps.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseContrarreferenciaDao "+sqlException.toString() );
				}
			}
		}
		return false;
	}
	
	
	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public static boolean insertarDiagnosticos(Connection con, HashMap vo) 
	{
		PreparedStatementDecorator ps =  null;
		try
		{
			ps =  new PreparedStatementDecorator(con.prepareStatement(insertarDiagnosticoStr));
			
			/**
			 * INSERT INTO diagnosticos_contrarreferencia (numero_referencia_contra,acronimo_diagnostico,tipo_cie,principal) VALUES (?,?,?,?)
			 */
			
			if(UtilidadTexto.isEmpty(vo.get("numerocontrarreferencia")+""))
				ps.setObject(1, null);
			else
				ps.setDouble(1, Utilidades.convertirADouble(vo.get("numerocontrarreferencia")+""));
			
			if(UtilidadTexto.isEmpty(vo.get("acronimodiagnostico")+""))
				ps.setObject(2, null);
			else
				ps.setString(2, vo.get("acronimodiagnostico")+"");
			
			
			
			if(UtilidadTexto.isEmpty(vo.get("tipocie")+""))
				ps.setObject(3, null);
			else
				ps.setInt(3, Utilidades.convertirAEntero(vo.get("tipocie")+""));
			
			if(UtilidadTexto.isEmpty(vo.get("principal")+""))
				ps.setObject(4, null);
			else
				ps.setString(4, vo.get("principal")+"");
			
			
			return ps.executeUpdate()>0;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}finally{
			if (ps != null){
				try{
					ps.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseContrarreferenciaDao "+sqlException.toString() );
				}
			}
		}
		return false;
	}

	


	/**
	 * 
	 * @param numeroContrarreferencia
	 * @param numeroSolicitud
	 * @return
	 */
	public static boolean eliminarProcedimientos(Connection con,String numeroContrarreferencia, String numeroSolicitud) 
	{
		PreparedStatementDecorator ps= null;
		try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(eliminarProcedimientosStr));
			
			/**
			 * DELETE FROM resul_proc_contrarreferencia WHERE numero_referencia_contra=? and numero_solicitud=?
			 */
			
			ps.setDouble(1,Utilidades.convertirADouble(numeroContrarreferencia));
			ps.setInt(2,Utilidades.convertirAEntero(numeroSolicitud));
			ps.executeUpdate();
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}finally{
			if (ps != null){
				try{
					ps.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseContrarreferenciaDao "+sqlException.toString() );
				}
			}
		}
		return false;
	}




	/**
	 * 
	 * @param con
	 * @param numeroReferenciaContra
	 * @return
	 */
	public static boolean eliminarDiagnosticos(Connection con, int numeroReferenciaContra) 
	{
		PreparedStatementDecorator ps =null;
		try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(eliminarDiagnosticoStr));
			ps.setDouble(1,Utilidades.convertirADouble(numeroReferenciaContra+""));
			ps.executeUpdate();
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}finally{
			if (ps != null){
				try{
					ps.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseContrarreferenciaDao "+sqlException.toString() );
				}
			}
		}
		return false;
	}




	/**
	 * 
	 * @param con
	 * @param numeroReferenciaContra
	 * @return
	 */
	public static HashMap consultarDiagnosticos(Connection con, int numeroReferenciaContra) 
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros","0");
		
		PreparedStatementDecorator ps= null;
		try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(cadenaConsultaDiagnosticos));
			ps.setDouble(1, Utilidades.convertirADouble(numeroReferenciaContra+""));
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			int cont=0;
			while(rs.next())
			{
				if(UtilidadTexto.getBoolean(rs.getString("principal")))
				{
					mapa.put("principal",rs.getString("diagnostico"));
				}
				else
				{
					mapa.put("relacionado_"+cont,rs.getString("diagnostico"));
					mapa.put("checkRel_"+cont,"true");
					cont++;
				}
				
			}
			mapa.put("numRegistros", cont);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}finally{
			if (ps != null){
				try{
					ps.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseContrarreferenciaDao "+sqlException.toString() );
				}
			}
		}
		return mapa;
	}




	/**
	 * 
	 * @param con
	 * @param numeroReferenciaContra
	 * @param codigoConducta
	 * @return
	 */
	public static boolean eliminarConductaSeguir(Connection con, int numeroReferenciaContra, String codigoConducta) 
	{
		PreparedStatementDecorator ps= null;
		try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(eliminarConductaStr));
			ps.setInt(1,numeroReferenciaContra);
			ps.setInt(2,Utilidades.convertirAEntero(codigoConducta));
			return ps.executeUpdate()>0;
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}finally{
			if (ps != null){
				try{
					ps.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseContrarreferenciaDao "+sqlException.toString() );
				}
			}
		}
		return false;
	}




	/**
	 * 
	 * @param con
	 * @param numeroReferenciaContra
	 * @param codigoConducta
	 * @param valor
	 * @return
	 */
	public static boolean insertarConductaSeguir(Connection con, int numeroReferenciaContra, String codigoConducta, String valor) 
	{
		PreparedStatementDecorator ps= null;
		try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(insertarConductaStr));
			
			/**
			 * INSERT INTO cond_seguir_contrarreferenc (numero_referencia_contra,codigo_conducta_seguir,valor) VALUES (?,?,?)
			 */
			
			ps.setDouble(1,Utilidades.convertirADouble(numeroReferenciaContra+""));
			ps.setInt(2,Utilidades.convertirAEntero(codigoConducta));
			ps.setString(3,valor);
			return ps.executeUpdate()>0;
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}finally{
			if (ps != null){
				try{
					ps.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseContrarreferenciaDao "+sqlException.toString() );
				}
			}
		}
		return false;
	}




	/**
	 * 
	 * @param con
	 * @param ingreso
	 * @return
	 */
	public static HashMap getUltimosDiagnosticosIngreso(Connection con, int ingreso) 
	{
		HashMap diagnosticos=new HashMap();
		diagnosticos.put("numRegistros", "0");
		PreparedStatementDecorator pst=null;
		ResultSetDecorator rs= null;
		try
		{
			int evolucion = UtilidadesHistoriaClinica.consultarUltimaEvolucionIngreso(con, ingreso);
			if(evolucion>0)
			{
				//Se consultan los diagnosticos de la evolucion
				pst =  new PreparedStatementDecorator(con.prepareStatement(consultarDiagnosticosEvolucionStr));
				pst.setInt(1,evolucion);
				rs=new ResultSetDecorator(pst.executeQuery());
				int cont=0;
				while(rs.next())
				{
					if(UtilidadTexto.getBoolean(rs.getString("principal")))
					{
						diagnosticos.put("principal",rs.getString("diagnostico"));
					}
					else
					{
						diagnosticos.put("relacionado_"+cont,rs.getString("diagnostico"));
						diagnosticos.put("checkRel_"+cont,"true");
						cont++;
					}
					
				}
				diagnosticos.put("numRegistros", cont);
			}
			else
			{
				int valoracion = UtilidadesHistoriaClinica.consultarUltimaValoracionIngreso(con, ingreso);
				if(valoracion>0)
				{
					pst =  new PreparedStatementDecorator(con.prepareStatement(consultarDiagnosticosValoracionStr));
					pst.setInt(1,valoracion);
					 rs=new ResultSetDecorator(pst.executeQuery());
					int cont=0;
					while(rs.next())
					{
						if(UtilidadTexto.getBoolean(rs.getString("principal")))
						{
							diagnosticos.put("principal",rs.getString("diagnostico"));
						}
						else
						{
							diagnosticos.put("relacionado_"+cont,rs.getString("diagnostico"));
							diagnosticos.put("checkRel_"+cont,"true");
							cont++;
						}
						
					}
					diagnosticos.put("numRegistros", cont);
				}
			}
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}finally{
			if (pst != null){
				try{
					pst.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseContrarreferenciaDao "+sqlException.toString() );
				}
			}
			if (rs != null){
				try{
					rs.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseContrarreferenciaDao "+sqlException.toString() );
				}
			}
		}
		
		return diagnosticos;
	}
}
