package com.princetonsa.dao.sqlbase.historiaClinica;
import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.princetonsa.decorator.ResultSetDecorator;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.Utilidades;


/**
 * @author Giovanny Arias
 *
 * Objeto usado para manejar los accesos comunes a la fuente de datos
 * de la funcionalidad justificacion de servicios no pos
 */
public class SqlBaseJustificacionNoPosServDao
{
	/**
	 * Manejador de logs de la clase
	 */
	private static Logger logger=Logger.getLogger(SqlBaseInstitucionesSircDao.class);
	private static final String consultarSolicitudesJustificacionesStr= "SELECT " +
																			"s.centro_costo_solicitante as codigo_centro_costo, " +
																			"getnomcentrocosto(s.centro_costo_solicitante) as centro_costo, " +
																			"sc.convenio as codigo_convenio, " +
																			"getnombreconvenio(sc.convenio) as convenio, " +
																			"s.numero_solicitud as solicitud, " +
																			"  to_char( s.fecha_solicitud ,'YYYY/MM/DD')                              AS fecha_solicitud, " +
																			"jps.servicio as codigo_servicio, " +
																			"getNombreServicio(jps.servicio, ?) as servicio, " +
																			"getobtenercodigocupsserv(jps.servicio, ?) as cupsServicio, " +
																			"getNumeroSubcuentaJus(s.numero_solicitud, i.id) as subcuenta, " +
																			"jps.cantidad " +
																		"FROM " +
																			"cuentas c " +
																		"INNER JOIN " +
																			"solicitudes s ON (c.id=s.cuenta) " +
																		"INNER JOIN " +
																			"ingresos i ON (c.id_ingreso=i.id) " +
																		"INNER JOIN  " +
																			"sub_cuentas sc ON (sc.ingreso=i.id and sc.nro_prioridad = 1) " +
																		"INNER JOIN " +
																			"jus_pendiente_servicios jps ON (s.numero_solicitud=jps.numero_solicitud) " +
																		"WHERE " +
																			"c.id=? AND s.estado_historia_clinica!=? " +
																		"ORDER BY " +
																			"s.fecha_solicitud";
	
	private static final String consultarSolicitudesJustificacionesDiliStr=
		"SELECT * FROM " +
				"(SELECT " +
					"s.centro_costo_solicitante as codigo_centro_costo, " +
					"getnomcentrocosto(s.centro_costo_solicitante) as centro_costo, " +
					"sc.convenio as codigo_convenio, " +
					"getnombreconvenio(sc.convenio) as convenio, " +
					"s.numero_solicitud as solicitud, " +
					"to_char(s.fecha_solicitud, '"+ConstantesBD.formatoFechaBD+"') as fecha_solicitud, " +
					"jss.servicio as codigo_servicio, " +
					"getNombreServicio(jss.servicio, ?) as servicio, " +
					"getobtenercodigocupsserv(jss.servicio, ?) as cups, " +
					"getNumeroSubcuentaJus(s.numero_solicitud, i.id) as subcuenta, " +
					"jss.fecha||'' as fechajus, " +
					"jss.consecutivo as numjus, " +
					"administracion.getnombremedico(jsf.profesional_responsable) as profesionalresp " +
				"FROM " +
					"cuentas c " +
				"INNER JOIN " +
					"solicitudes s ON (c.id=s.cuenta) " +
				"INNER JOIN " +
					"ingresos i ON (c.id_ingreso=i.id) " +
				"INNER JOIN  " +
					"sub_cuentas sc ON (sc.ingreso=i.id and sc.nro_prioridad = 1) " +
				"INNER JOIN " +
					"justificacion_serv_sol jss ON (s.numero_solicitud=jss.solicitud) " +
				"INNER JOIN " +
					"justificacion_serv_fijo jsf ON (jss.codigo=jsf.justificacion_serv_sol) " +	
				"WHERE " +
					"c.id=? AND s.estado_historia_clinica!=? " +
			"UNION " +
				"SELECT " +
					"s.centro_costo_solicitante as codigo_centro_costo, " +
					"getnomcentrocosto(s.centro_costo_solicitante) as centro_costo, " +
					"sc.convenio as codigo_convenio, " +
					"getnombreconvenio(sc.convenio) as convenio, " +
					"s.numero_solicitud as solicitud, " +
					"to_char(s.fecha_solicitud, '"+ConstantesBD.formatoFechaBD+"') as fecha_solicitud, " +
					"jss.servicio as codigo_servicio, " +
					"getNombreServicio(jss.servicio, ?) as servicio, " +
					"getobtenercodigocupsserv(jss.servicio, ?) as cups, " +
					"getNumeroSubcuentaJus(s.numero_solicitud, i.id) as subcuenta, " +
					"'' as fechajus, " +
					"'' as numjus, " +
					"'' as profesionalresp " +
				"FROM " +
					"cuentas c " +
				"INNER JOIN " +
					"solicitudes s ON (c.id=s.cuenta) " +
				"INNER JOIN " +
					"ingresos i ON (c.id_ingreso=i.id) " +
				"INNER JOIN " +
					"sub_cuentas sc ON (sc.ingreso=i.id and sc.nro_prioridad = 1) " +
				"INNER JOIN " +
					"desc_atr_sol_int_proc jss ON (s.numero_solicitud=jss.numero_solicitud) " +
				"WHERE " +
					"c.id=? AND s.estado_historia_clinica!=? " +
			") tabla "+
				"ORDER BY " +
					"tabla.fecha_solicitud";
	
	
	/**
	 * Metodo que consulta las justificaciones de servicios No POS que se encuentran pendiente filtradas por los parametros ingresados
	 * @param con
	 * @param filtros
	 * @param codigosServicios
	 * @param fechaInicial
	 * @param fechaFinal
	 * @return
	 */
	public static HashMap cargarInfoIngresoRango(Connection con, HashMap filtro, String servicios, String fechaini, String fechafin,String where){
		HashMap mapa=new HashMap();
		
		String[] viapac=filtro.get("viaIngreso").toString().split(ConstantesBD.separadorSplit);
		
		
		logger.info("datos de la consulta filtros \n\n\n\n " +
				"mapa filtros >>>>"+filtro+"\n\n\n\n" +
				"cadena servicios>>>>> "+servicios+"\n\n\n\n" +
				"fechas "+fechaini+"--------"+fechafin);
		
		String consulta="SELECT  " +
							"s.centro_costo_solicitante as codigo_centro_costo," +
							"getnomcentrocosto(s.centro_costo_solicitante) as centro_costo," +
							"sc.convenio as codconvenio," +
							"getnombreconvenio(sc.convenio) as convenio," +
							"s.numero_solicitud as solicitud," +
							" TO_CHAR(s.fecha_solicitud,'YYYY/MM/DD') as fecha," +
							"getNombreServicio(jps.servicio, "+ConstantesBD.codigoTarifarioCups+") as servicio, " +
							"getobtenercodigocupsserv(jps.servicio, "+ConstantesBD.codigoTarifarioCups+") as cupsServicio, " +
							"i.id as ingreso," +
							"sc.sub_cuenta as subcuenta," +
							"c.via_ingreso as codviaingreso," +
							"c.tipo_paciente as codtipopaciente," +
							"case when getcuantosTipoPacViaIngreso(c.via_ingreso)>1  " +
							"then getnombreviaingreso(c.via_ingreso)||' - '||getnombretipopaciente(c.tipo_paciente) " +
							"else getnombreviaingreso(c.via_ingreso)||'' end  as viaingresotipopac,  " +
							"i.fecha_ingreso || '/' || i.hora_ingreso as fechahora, " +
							"i.consecutivo as noingreso," +
							"coalesce(HISTORIACLINICA.GETVALORTOTALCARG(s.numero_solicitud,jps.servicio,sc.sub_cuenta)||'','PENDIENTE') as valor," +
							"getidpaciente(i.codigo_paciente) as tipoid," +
							"getnombrepersona(i.codigo_paciente) as nombrepac," +
							"i.codigo_paciente AS codpaciente,"+
							"jps.servicio as codigoserv, " +
							"c.id as codigocuenta, " +
							"1 as cantidad " +
						"FROM " +
							"ingresos i " +
						"INNER JOIN " +
							"cuentas c on (c.id_ingreso=i.id) " +
						"INNER JOIN " +
							"solicitudes s on (c.id=s.cuenta) " +
						"INNER JOIN " +
							"sub_cuentas sc on (sc.ingreso=i.id and sc.nro_prioridad = 1) " +
						"INNER JOIN " +
							"jus_pendiente_servicios jps on (s.numero_solicitud=jps.numero_solicitud) " +
						"INNER JOIN " +
							"servicios serv on (serv.codigo=jps.servicio) " +
						"WHERE " +
						where ;
		
		if(!viapac[0].toString().equals("-1"))
			{
			consulta+=" and c.via_ingreso="+viapac[0]+" " ;
			} 
		if(!viapac[0].toString().equals("-1") && !viapac[1].toString().equals(""))
			{
			consulta+=" and c.tipo_paciente='"+viapac[1]+"' " ;
			}
		if(!filtro.get("centrocosto").toString().equals(""))
			{
			consulta+=" and c.area="+filtro.get("centrocosto")+" " ;
			}
		if(!filtro.get("convenio").toString().equals(""))
			{
			consulta+=" and sc.convenio="+filtro.get("convenio")+" " ;
			}
		if(!servicios.equals(""))
			{
			consulta+=" and jps.servicio IN ("+ConstantesBD.codigoNuncaValido+","+servicios+") " ;
			}
		
		consulta+="ORDER BY fechahora";
			
		logger.info("consulta info ingresos >>>>"+consulta);
		PreparedStatementDecorator ps= null;
		try{
			ps= new PreparedStatementDecorator(con.prepareStatement(consulta));
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		}
		catch (Exception e) {
			logger.info("error consultando servicios del ingreso "+e+"\n\n\n"+consulta);
		}finally {			
			if(ps!=null){
				try{
					ps.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseJustificacionNoPosServDao "+sqlException.toString() );
				}
			}
		
		}
		
		return mapa;
	}
	
	
	/**
	 * Método que consulta las justificaciones de servicios No POS que se encuentran pendiente por determinada cuenta
	 * @param con
	 * @param cuenta
	 * @return
	 */
	public static HashMap consultarSolicitudesJustificaciones(Connection con, int cuenta){
		HashMap mapa=new HashMap();
		PreparedStatementDecorator ps =  null;
		try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(consultarSolicitudesJustificacionesStr));
			ps.setInt(1, ConstantesBD.codigoTarifarioCups);
			ps.setInt(2, ConstantesBD.codigoTarifarioCups);
			ps.setInt(3, cuenta);
			ps.setInt(4, ConstantesBD.codigoEstadoHCAnulada);
			mapa = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
			Utilidades.imprimirMapa(mapa);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}finally {			
			if(ps!=null){
				try{
					ps.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseJustificacionNoPosServDao "+sqlException.toString() );
				}
			}
		
		}
		return mapa;
	}
	
	/**
	 * Método que consulta las justificaciones de servicios No POS que se encuentran pendiente por determinada cuenta
	 * @param con
	 * @param cuenta
	 * @return
	 */
	public static HashMap consultarSolicitudesJustificacionesDiligenciadas(Connection con, int cuenta){
		HashMap mapa=new HashMap();
		PreparedStatementDecorator ps= null;
		try
		{	
			ps= new PreparedStatementDecorator(con.prepareStatement(consultarSolicitudesJustificacionesDiliStr));
			ps.setInt(1, ConstantesBD.codigoTarifarioCups);
			ps.setInt(2, ConstantesBD.codigoTarifarioCups);
			ps.setInt(3, cuenta);
			ps.setInt(4, ConstantesBD.codigoEstadoHCAnulada);
			ps.setInt(5, ConstantesBD.codigoTarifarioCups);
			ps.setInt(6, ConstantesBD.codigoTarifarioCups);
			ps.setInt(7, cuenta);
			ps.setInt(8, ConstantesBD.codigoEstadoHCAnulada);
			
			logger.info("consultarSolicitudesJustificacionesDiligenciadas / "+consultarSolicitudesJustificacionesDiliStr);
			logger.info("Param 1 - "+ConstantesBD.codigoTarifarioCups);
			logger.info("Param 2 - "+ConstantesBD.codigoTarifarioCups);
			logger.info("Param 3 - "+cuenta);
			logger.info("Param 4 - "+ConstantesBD.codigoEstadoHCAnulada);
			logger.info("Param 5 - "+ConstantesBD.codigoTarifarioCups);
			logger.info("Param 6 - "+ConstantesBD.codigoTarifarioCups);
			logger.info("Param 7 - "+cuenta);
			logger.info("Param 8 - "+ConstantesBD.codigoEstadoHCAnulada);
			
			mapa = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}finally {			
			if(ps!=null){
				try{
					ps.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseJustificacionNoPosServDao "+sqlException.toString() );
				}
			}
		
		}
		return mapa;
	}
	
	/**
	 * 
	 * @param con
	 * @param ingresosMap
	 * @param filtro
	 * @param articulos
	 * @param fechaini
	 * @param fechafin
	 * @param tipocodigo
	 * @return
	 */
	public static HashMap cargarInfoIngresoConsultarModificarRango(Connection con, HashMap filtro, String servicios,String fechaini, String fechafin,String where)
	{
		
		
		HashMap solicitudesMap = new HashMap();
		
		String[] viapac=filtro.get("viaIngreso").toString().split(ConstantesBD.separadorSplit);
		
		logger.info("datos de la consulta filtros \n\n\n\n " +
				"mapa filtros >>>>"+filtro+"\n\n\n\n" +
				"cadena articulos>>>>> "+servicios+"\n\n\n\n" +
				"fechas "+fechaini+"--------"+fechafin);
		
		String consulta="select  " +
							"	s.centro_costo_solicitante as codigo_centro_costo," +
							"	getnomcentrocosto(s.centro_costo_solicitante) as centro_costo," +
							"	sc.convenio as codconvenio," +
							"	getnombreconvenio(getconveniosubcuenta(jsr.sub_cuenta)) as convenio," +
							"	s.numero_solicitud as solicitud," +
							"	s.fecha_solicitud as fecha," +
							"	getNombreServicio(jss.servicio, "+ConstantesBD.codigoTarifarioCups+") as servicio, " +
							"	getobtenercodigocupsserv(jss.servicio, "+ConstantesBD.codigoTarifarioCups+") as cups, " +
							"	i.id as ingreso," +
							"	jsr.sub_cuenta as subcuenta," +
							"	c.via_ingreso as codviaingreso," +
							"	c.tipo_paciente as codtipopaciente," +
							"	case when getcuantosTipoPacViaIngreso(c.via_ingreso)>1  " +
							"	then getnombreviaingreso(c.via_ingreso)||' - '||getnombretipopaciente(c.tipo_paciente) " +
							"	else getnombreviaingreso(c.via_ingreso)||'' end  As viaingresotipopac,  " +
							"	i.fecha_ingreso || '/' || i.hora_ingreso as fechahora, " +
							"	i.consecutivo as noingreso," +
							"	getidpaciente(i.codigo_paciente) as tipoid," +
							"	getnombrepersona(i.codigo_paciente) as nombrepac," +
							"	jss.servicio as codigoserv, " +
							"	c.id as codigocuenta," +
							"	i.codigo_paciente as codpaciente, " +
							"	getcamacuenta(c.id,c.via_ingreso) as cama," +
							"	jss.consecutivo as nojus," +
							"	jsr.cantidad as cantotorden," +
							"	jsr.cantidad as cantotconv," +
							" 	getintegridaddominio(jsr.estado) as estadojus, " +
							"	coalesce(getnombrepiso(getcodigopisocuenta(c.id,c.via_ingreso))||'', 'Sin cama asignada') as piso," +
							"	coalesce(dc.valor_unitario_tarifa||'','PENDIENTE') as preciotarifa ";		
				consulta+=	" FROM " +
							"	ingresos i " +
							" inner join " +
							"	cuentas c on (c.id_ingreso=i.id) " +
							" inner join " +
							"	solicitudes s on (c.id=s.cuenta) " +
							" inner join " +
							"	sub_cuentas sc on (sc.ingreso=i.id and sc.nro_prioridad = 1) " +
							" inner join " +
							"	justificacion_serv_sol jss on (s.numero_solicitud=jss.solicitud) " +
							" inner join " +
							"	justificacion_serv_resp jsr on (jss.codigo=jsr.justificacion_serv_sol) " +
							" inner join " +
							"	justificacion_serv_fijo jsf on (jsf.justificacion_serv_sol=jss.codigo) " +
							"LEFT OUTER JOIN " +
								"det_cargos dc on (dc.solicitud=jss.solicitud AND dc.servicio=jss.servicio AND dc.sub_cuenta=jsr.sub_cuenta) ";
				consulta+=where;
		
		if(!viapac[0].toString().equals("-1"))
			{
			consulta+=" and c.via_ingreso="+viapac[0]+" " ;
			} 
		if(!viapac[0].toString().equals("-1") && !viapac[1].toString().equals(""))
			{
			consulta+=" and c.tipo_paciente='"+viapac[1]+"' " ;
			}
		if(!filtro.get("centrocosto").toString().equals(""))
			{
			consulta+=" and c.area="+filtro.get("centrocosto")+" " ;
			}
		if(!filtro.get("convenio").toString().equals(""))
			{
			consulta+=" and sc.convenio="+filtro.get("convenio")+" " ;
			}
		if(!filtro.get("estadojus").toString().equals(""))
			{
			consulta+=" and jsr.estado='"+filtro.get("estadojus")+"' " ;
			}
		if(!filtro.get("piso").toString().equals(""))
			{
			consulta+=" and getcodigopisocuenta(c.id,c.via_ingreso)="+filtro.get("piso")+" " ;
			}
		if(!filtro.get("profesional").toString().equals(""))
			{
			consulta+=" and jsf.profesional_responsable ="+filtro.get("profesional")+" " ;
			}
		if(!servicios.equals(""))
			{
			consulta+=" and jss.servicio IN ("+ConstantesBD.codigoNuncaValido+","+servicios+""+ConstantesBD.codigoNuncaValido+") " ;
			}
		//falta el filtro por profesional responsable de la justificacion 
		
		consulta+="ORDER BY ";
		
		if(filtro.get("tipoRompimiento").equals("piso_"))
		{
			consulta += "piso ";
		}
		
		if(filtro.get("tipoRompimiento").equals("centro_costo_"))
		{
			consulta += "c.area ";
		}
		
		if(filtro.get("tipoRompimiento").equals("estadojus_"))
		{
			consulta += "jsr.estado ";
		}
		
		consulta+=", fechahora ";
		
		logger.info("CONSULTA ---> "+consulta);
		PreparedStatementDecorator ps= null;
		try{
			ps= new PreparedStatementDecorator(con.prepareStatement(consulta));
			solicitudesMap=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		}
		catch (Exception e) {
			logger.info("error consultando servicios del ingreso "+e+"\n\n\n"+consulta);
		}finally {			
			if(ps!=null){
				try{
					ps.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseJustificacionNoPosServDao "+sqlException.toString() );
				}
			}
		
		}
		
		return solicitudesMap;
	}
	
	/**
	 * 
	 * @param con
	 * @param ingresosMap
	 * @param filtro
	 * @param articulos
	 * @param fechaini
	 * @param fechafin
	 * @param tipocodigo
	 * @return
	 */
	public static HashMap cargarInfoIngresoConsultarModificarRangoCon(Connection con, HashMap filtro, String servicios,String fechaini, String fechafin,String where)
	{
		
		
		HashMap solicitudesMap = new HashMap();
		
		String[] viapac=filtro.get("viaIngreso").toString().split(ConstantesBD.separadorSplit);
		
		logger.info("datos de la consulta filtros \n\n\n\n " +
				"mapa filtros >>>>"+filtro+"\n\n\n\n" +
				"cadena articulos>>>>> "+servicios+"\n\n\n\n" +
				"fechas "+fechaini+"--------"+fechafin);
		
		String consulta="select  " +
		
							"	s.centro_costo_solicitante as codigo_centro_costo," +		
							"	getnomcentrocosto(s.centro_costo_solicitante) as centro_costo," +
							"	getNombreServicio(jss.servicio, "+ConstantesBD.codigoTarifarioCups+") as nomservicio, " +
							"	jss.servicio as codigoserv," +
							"	coalesce(getnombrepiso(getcodigopisocuenta(c.id,c.via_ingreso))||'', 'Sin cama asignada') as piso," +
							"	getintegridaddominio(jsr.estado) as estadojus, " +
							"	administracion.getnombremedico(jsf.profesional_responsable) as profesionalresp, " +
							"	SUM(jsr.cantidad) as cantidadord, " +
							"   SUM(dc.valor_total_cargado * jsr.cantidad) as precio_total " +
							" FROM " +
							"	ingresos i " +
							" inner join " +
							"	cuentas c on (c.id_ingreso=i.id) " +
							" inner join " +
							"	solicitudes s on (c.id=s.cuenta) " +
							" inner join " +
							"	sub_cuentas sc on (sc.ingreso=i.id and sc.nro_prioridad = 1) " +
							" inner join " +
							"	justificacion_serv_sol jss on (s.numero_solicitud=jss.solicitud) " +
							" inner join " +
							"	justificacion_serv_resp jsr on (jss.codigo=jsr.justificacion_serv_sol) " +
							" inner join " +
							"	justificacion_serv_fijo jsf on (jsf.justificacion_serv_sol=jss.codigo) " +
							"LEFT OUTER JOIN " +
								"det_cargos dc on (dc.solicitud=jss.solicitud AND dc.servicio=jss.servicio AND dc.sub_cuenta=jsr.sub_cuenta) ";
				consulta+=where;
		
		if(!viapac[0].toString().equals("-1"))
			{
			consulta+=" and c.via_ingreso="+viapac[0]+" " ;
			} 
		if(!viapac[0].toString().equals("-1") && !viapac[1].toString().equals(""))
			{
			consulta+=" and c.tipo_paciente='"+viapac[1]+"' " ;
			}
		if(!filtro.get("centrocosto").toString().equals(""))
			{
			consulta+=" and c.area="+filtro.get("centrocosto")+" " ;
			}
		if(!filtro.get("convenio").toString().equals(""))
			{
			consulta+=" and sc.convenio="+filtro.get("convenio")+" " ;
			}
		if(!filtro.get("estadojus").toString().equals(""))
			{
			consulta+=" and jsr.estado='"+filtro.get("estadojus")+"' " ;
			}
		if(!filtro.get("piso").toString().equals(""))
			{
			consulta+=" and getcodigopisocuenta(c.id,c.via_ingreso)="+filtro.get("piso")+" " ;
			}
		if(!filtro.get("profesional").toString().equals(""))
			{
			consulta+=" and jsf.profesional_responsable ="+filtro.get("profesional")+" " ;
			}
		if(!servicios.equals(""))
			{
			consulta+=" and jss.articulo IN ("+ConstantesBD.codigoNuncaValido+","+servicios+""+ConstantesBD.codigoNuncaValido+") " ;
			}
		
		consulta+=	" 	group by " +
		"	 s.centro_costo_solicitante,  c.id,  c.via_ingreso,  jss.servicio,  jsr.estado,  jsf.profesional_responsable ";
		
		consulta+="ORDER BY ";
		
		if(filtro.get("tipoRompimiento").equals("piso_"))
		{
			consulta += "piso ";
		}
		
		if(filtro.get("tipoRompimiento").equals("centro_costo_"))
		{
			consulta += "codigo_centro_costo ";
		}
		
		if(filtro.get("tipoRompimiento").equals("estadojus_"))
		{
			consulta += "jsr.estado ";
		}
		
		consulta+=", nomservicio";
		
		logger.info("CONSULTA ----> "+consulta);
		PreparedStatementDecorator ps= null;
		try{
			ps= new PreparedStatementDecorator(con.prepareStatement(consulta));
			solicitudesMap=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		}
		catch (Exception e) {
			logger.info("error consultando servicios del ingreso "+e+"\n\n\n"+consulta);
		}finally {			
			if(ps!=null){
				try{
					ps.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseJustificacionNoPosServDao "+sqlException.toString() );
				}
			}
		
		}
		return solicitudesMap;
	}	
	
}	