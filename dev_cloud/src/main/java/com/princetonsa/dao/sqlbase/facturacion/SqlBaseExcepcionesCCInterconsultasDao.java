package com.princetonsa.dao.sqlbase.facturacion;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.util.HashMap;
import org.apache.log4j.Logger;
import util.ConstantesBD;
import util.UtilidadBD;
import util.Utilidades;
import util.ValoresPorDefecto;


public class SqlBaseExcepcionesCCInterconsultasDao{
	/**
	 * Objeto para manejar log de la clase  
	 * */
	private static Logger logger = Logger.getLogger(SqlBaseExcepcionesCCInterconsultasDao.class);
	
	private static String strIngresarExcepcionCCInterconsulta = "INSERT INTO " +
																	"exc_cc_interconsulta " +
																"(codigo,centro_atencion,centro_costo_solicita,servicio,medico_ejecuta,centro_costo_ejecuta,institucion,fecha_modifica,hora_modifica,usuario_modifica) "+
																	"VALUES " +
																"(?,?,?,?,?,?,?,CURRENT_DATE, "+ValoresPorDefecto.getSentenciaHoraActualBD()+"  ,?)";
	private static String strEliminarExcepcion= "DELETE FROM " +
													"exc_cc_interconsulta " +
												"WHERE codigo=?";
	
	private static String strModificarExcepcion = 	"UPDATE " +
														"exc_cc_interconsulta  " +
													"SET " +
														"centro_costo_solicita=?, " +
														"servicio=?, " +
														"medico_ejecuta=?, " +
														"centro_costo_ejecuta=?, " +
														"fecha_modifica=CURRENT_DATE, " +
														"hora_modifica="+ValoresPorDefecto.getSentenciaHoraActualBD()+", " +
														"usuario_modifica=? " +
													"WHERE " +
														"codigo=?";
	
	/**
	 * Metodo para consultar el detalle de las facturas de una glosa
	 * @param con
	 * @param glosa
	 * @return
	 */
	public static HashMap consultarXCentroAtencion(Connection con, String centroAtencion)
	{
		HashMap consultarExcepcionesMap = new HashMap();
		String sql = "SELECT " +
						"ex.codigo, " +
						"ex.centro_atencion AS centroatencion, " +
						"ex.centro_costo_solicita AS centrocostosolicita, " +
						"ex.servicio, " +
						"ex.medico_ejecuta AS medico, " +
						"ex.centro_costo_ejecuta AS centrocostoejecuta, " +
						"ex.institucion, " +
						"ex.fecha_modifica AS fechamodifica, " +
						"ex.hora_modifica AS horamodifica, " +
						"ex.usuario_modifica AS usuariomodifica, " +
						"sv.especialidad AS especialidadservicio, " +
						"getnombrecentroscosto(ex.centro_costo_solicita) AS nombrecentrocostosolicita, " +
						"getnombrecentroscosto(ex.centro_costo_ejecuta)  AS nombrecentrocostoejecuta, " +
						"getnombrepersona(ex.medico_ejecuta) AS nombremedico, " +
						"getnombreespecialidad(sv.especialidad) AS nombreespecialidadservicio, " +
						"getdescrefservicio(ex.servicio) AS descrefservicio, " +
						"getidentificadorcentrocosto(ex.centro_costo_solicita) AS idcentrocostosolicita, " +
						"getidentificadorcentrocosto(ex.centro_costo_ejecuta) AS idcentrocostoejecuta, " +
						"getcodigocups(ex.servicio,"+ConstantesBD.codigoTarifarioCups+") AS codigocups "+
					"FROM " +
						"exc_cc_interconsulta ex " +
					"INNER JOIN " +
						"servicios sv ON (sv.codigo=ex.servicio) " +
					"WHERE " +
						"centro_atencion="+centroAtencion;
					
		
		logger.info("consultarExcepcionesQuery / "+sql);
		
		try {
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(sql, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			consultarExcepcionesMap=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()), true, true);
			consultarExcepcionesMap.put("consultasql", sql);
		} 
		catch (SQLException e) {	
			logger.info("ERROR / consultarExcepcionesQuery / "+e);
		}
		return consultarExcepcionesMap;	
	}
	
	public static boolean guardarExcepcion(Connection con, HashMap datosIngresoExcepcion, String login, String institucion, String centroAtencion)
	{
		boolean transaccionExitosa = false;
		try {
		PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(strIngresarExcepcionCCInterconsulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
		
		logger.info("strIngresarExcepcionCCInterconsulta->"+strIngresarExcepcionCCInterconsulta+" ");
		logger.info(""+UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_exc_cc_interconsulta")+" "+centroAtencion+" "+datosIngresoExcepcion.get("centroCostoSolicita")+" "+datosIngresoExcepcion.get("codServicio")+" "+datosIngresoExcepcion.get("medico")+" "+datosIngresoExcepcion.get("centroCostoEjecuta")+" "+institucion+" "+login);
		
		ps.setLong(1, UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_exc_cc_interconsulta"));
		ps.setInt(2,Utilidades.convertirAEntero(centroAtencion));
		ps.setInt(3,Utilidades.convertirAEntero(datosIngresoExcepcion.get("centroCostoSolicita").toString()));
		ps.setInt(4,Utilidades.convertirAEntero(datosIngresoExcepcion.get("codServicio").toString()));
		ps.setInt(5,Utilidades.convertirAEntero(datosIngresoExcepcion.get("medico").toString()));
		ps.setInt(6,Utilidades.convertirAEntero(datosIngresoExcepcion.get("centroCostoEjecuta").toString()));
		ps.setInt(7,Utilidades.convertirAEntero(institucion));
		ps.setString(8, login);
		
		
		
		if(ps.executeUpdate()>0)
			transaccionExitosa = true;
		} 
		catch (SQLException e) 
		{	
			logger.info("ERROR / ingresarExcepcionQuery / "+e);
			transaccionExitosa=false;
		}
		return transaccionExitosa;
	}
	
	public static boolean eliminarExcepcion(Connection con, int indice)
	{
		boolean transaccionExitosa = false;
		try {
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(strEliminarExcepcion, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			ps.setInt(1,indice);
		if(ps.executeUpdate()>0)
			transaccionExitosa = true;
		} 
		catch (SQLException e) {	
		logger.info("ERROR / ingresarExcepcionQuery / "+e);
		transaccionExitosa=false;
		}
		
		return transaccionExitosa;
	}
	
	public static HashMap obtenerMedicos(Connection con, int institucion)
	{
		HashMap obtenerMedicos = new HashMap();
		String sql = "SELECT DISTINCT " +
							"m.codigo_medico AS codigo, " +
							"getnombrepersona(m.codigo_medico) AS nombremedico, " +
							"m.ocupacion_medica AS ocupacion " +
					"FROM " +
						"medicos m " +
					"INNER JOIN " +
						"interconsu_perm ip ON (ip.ocupacion_medica=m.ocupacion_medica) " +
					"ORDER BY " +
						"nombremedico";
		
		logger.info("consultarMedicosQuery / "+sql);
		
		try {
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(sql, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			obtenerMedicos=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()), true, true);
			obtenerMedicos.put("consultasql", sql);
		} 
		catch (SQLException e) {	
			logger.info("ERROR / consultarExcepcionesQuery / "+e);
		}
		return obtenerMedicos;	
		
	}
	
	public static boolean modificarExcepcion(Connection con, HashMap datosIngresoExcepcion, String login, String institucion, String centroAtencion)
	{
		boolean transaccionExitosa = false;
		try {
		PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(strModificarExcepcion, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
		
		ps.setInt(1,Utilidades.convertirAEntero(datosIngresoExcepcion.get("centroCostoSolicita").toString()));
		ps.setInt(2,Utilidades.convertirAEntero(datosIngresoExcepcion.get("servicio").toString()));
		ps.setInt(3,Utilidades.convertirAEntero(datosIngresoExcepcion.get("medico").toString()));
		ps.setInt(4,Utilidades.convertirAEntero(datosIngresoExcepcion.get("centroCostoEjecuta").toString()));
		ps.setString(5, login);
		ps.setInt(6,Utilidades.convertirAEntero(datosIngresoExcepcion.get("codigo").toString()));
		
		if(ps.executeUpdate()>0)
			transaccionExitosa = true;
		} 
		catch (SQLException e) {	
		logger.info("ERROR / modificarExcepcionQuery / "+e);
		transaccionExitosa=false;
		}
		return transaccionExitosa;
	}
	
	public static HashMap obtenerCentrosCosto(Connection con, String centroAtencion, int tipoAreaDirecto, int institucion)
	{
		HashMap obtenerCC = new HashMap();
		String sql = "SELECT DISTINCT " +
							"cc.codigo, " +
							"cc.identificador, " +
							"cc.nombre AS nomcentrocosto " +
					"FROM " +
						"centros_costo cc " +
					"INNER JOIN " +
						"centro_costo_via_ingreso ccv ON (ccv.centro_costo=cc.codigo) " +
					"WHERE " +
						"cc.tipo_area=? " +
					"AND " +
						"cc.centro_atencion=? " +
					"AND " +
						"cc.institucion=? " +
					"AND " +
						"cc.es_activo="+ValoresPorDefecto.getValorTrueParaConsultas()+" "+
					"AND " +
						"cc.codigo>0 " +
					"ORDER BY " +
						"cc.nombre";
		
		logger.info("consultarCCQuery / "+sql);
		
		try {
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(sql, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			ps.setString(1,centroAtencion);
			ps.setInt(2, tipoAreaDirecto);
			ps.setInt(3,institucion);
			obtenerCC=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()), true, true);
			obtenerCC.put("consultasql", sql);
		} 
		catch (SQLException e) {	
			logger.info("ERROR / consultarCCQuery / "+e);
		}
		return obtenerCC;	
		
	}
	
	public static HashMap obtenerCentrosCostoEjecutan(Connection con, String centroAtencion, int tipoAreaDirecto, int institucion)
	{
		HashMap obtenerCC = new HashMap();
		String sql = "SELECT DISTINCT " +
							"cc.codigo, " +
							"cc.identificador, " +
							"cc.nombre AS nomcentrocosto " +
					"FROM " +
						"centros_costo cc " +
					"WHERE " +
						"cc.tipo_area=? " +
					"AND " +
						"cc.centro_atencion=? "+
					"AND " +
						"cc.institucion=? " +
					"AND " +
						"cc.es_activo="+ValoresPorDefecto.getValorTrueParaConsultas()+" "+
					"AND " +
						"cc.codigo>0 " +
					"ORDER BY " +
						"cc.nombre";
		
		logger.info("consultarCCQuery / "+sql);
		
		try {
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(sql, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			ps.setString(1,centroAtencion);
			ps.setInt(2, tipoAreaDirecto);
			ps.setInt(3,institucion);
			obtenerCC=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()), true, true);
			obtenerCC.put("consultasql", sql);
		} 
		catch (SQLException e) {	
			logger.info("ERROR / consultarCCQuery / "+e);
		}
		return obtenerCC;	
		
	}
}