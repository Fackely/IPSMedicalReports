package com.princetonsa.dao.sqlbase.historiaClinica;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import java.sql.SQLException;
import java.util.HashMap;
import org.apache.log4j.Logger;
import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.Utilidades;
import com.princetonsa.dao.UtilidadesBDDao;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.mundo.historiaClinica.ValoracionPacientesCuidadosEspeciales;



public class SqlBaseValoracionPacientesCuidadosEspecialesDao{
	
	/**
	 * Objeto para manejar log de la clase  
	 * */
	private static Logger logger = Logger.getLogger(SqlBaseValoracionPacientesCuidadosEspecialesDao.class);
	
	/**
	 * Consultar los eventos adversos
	 */
	private static String consultarStr = "SELECT " +
											"i.consecutivo AS ingreso, " +
											"i.codigo_paciente AS codigo_paciente, " +
											"getnombrepersona(p.codigo) AS nombre, " +
											"tipo_identificacion||' '||numero_identificacion AS identificacion, " +
											"getedad(p.fecha_nacimiento) AS edad, " +
											"getdescripcionsexo(p.sexo) AS sexo, " +
											"c.area AS codarea, " +
											"getnomcentrocosto(c.area) AS area, " +
											"getDescripcionUltimaCama(c.id) AS cama, " +
											"coalesce(getDxPrincTrasladoCuidadEspec(p.codigo),'') AS dxFechaHora, " +
											"getnombreusuario2(ice.usuario_resp) AS profesional, " +
											"ice.fecha_resp AS fechaingreso, " +
											"ice.hora_resp AS horaingreso, " +
											"COALESCE(ice.valoracion_orden, "+ConstantesBD.codigoNuncaValido+") AS valoracionOrdena, " +
											"COALESCE(ice.evolucion_orden, "+ConstantesBD.codigoNuncaValido+") AS evolucionOrdena, " +
											"COALESCE(ice.valoracion, "+ConstantesBD.codigoNuncaValido+") AS valoracion, " +
											"COALESCE(sol.tipo, "+ConstantesBD.codigoNuncaValido+") AS valoracionOrdenaTipo " +
										"FROM " +
											"ingresos_cuidados_especiales ice " +
										"INNER JOIN " +
											"ingresos i ON (i.id=ice.ingreso) " +
										"INNER JOIN " +
											"cuentas c ON (c.id_ingreso=i.id) " +
										"INNER JOIN " +
											"personas p ON (p.codigo = i.codigo_paciente) " +
										"LEFT OUTER JOIN " +
											"solicitudes sol on (sol.numero_solicitud = ice.valoracion_orden) " +
										"WHERE " +
											"ice.estado='"+ConstantesIntegridadDominio.acronimoEstadoActivo+"' " +
											"AND ice.valoracion IS null " +
											"AND tipo_monitoreo=? " +
											"AND institucion=? " +
											"AND c.estado_cuenta <> "+ConstantesBD.codigoEstadoCuentaCerrada+" " +
											"AND getcuentafinalasocio(ice.ingreso,c.id) IS NULL " +
										"ORDER BY " +
											"ice.fecha_resp DESC, " +
											"ice.hora_resp DESC";
	
	private static String  consultarTiposMonitoreoStr = "SELECT DISTINCT " +
															"codigo, " +
															"nombre " +
														"FROM " +
															"tipo_monitoreo " +
														"WHERE " +
															"institucion=? " +
														"ORDER BY " +
															"nombre ";
	
	/**
	 * 
	 * @param con
	 * @param mundo
	 * @return
	 */
	public static HashMap consultarTiposMonitoreo(Connection con, ValoracionPacientesCuidadosEspeciales mundo){
		HashMap mapa = new HashMap();
		mapa.put("numRegistros", "0");
		PreparedStatementDecorator ps=  null;
		try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(consultarTiposMonitoreoStr));
			ps.setInt(1, Utilidades.convertirAEntero(mundo.getInstitucion()));
			mapa = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}finally{
			try {
				if(ps!=null){
					ps.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseTextosRespuestaProcedimientosDao "+sqlException.toString() );
			}
		}
		return mapa;
	}

	/**
	 * 
	 * @param con
	 * @param mundo
	 * @return
	 */
	public static HashMap consultar(Connection con, ValoracionPacientesCuidadosEspeciales mundo){
		HashMap mapa = new HashMap();
		mapa.put("numRegistros", "0");
		PreparedStatementDecorator ps=  null;
		try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(consultarStr));
			ps.setInt(1, mundo.getTipoMonitoreo());
			ps.setInt(2, Utilidades.convertirAEntero(mundo.getInstitucion()+""));
			logger.info("Consulta (Pacientes pendientes de valoración de cuidados especiales) ---- > "+consultarStr);
			logger.info("Tipo de monitoreo: "+mundo.getTipoMonitoreo());
			logger.info("Institucion: "+mundo.getInstitucion());
			mapa=organizarMapa(UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery())));
			
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}finally{
			try {
				if(ps!=null){
					ps.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseTextosRespuestaProcedimientosDao "+sqlException.toString() );
			}
		}
		return mapa;
	}
	
	private static HashMap organizarMapa(HashMap mapaAnterior) {
		HashMap mapaNuevo = new HashMap();
		mapaNuevo.put("numRegistros", mapaAnterior.get("numRegistros"));
		for (int i=0; i<Integer.parseInt(mapaAnterior.get("numRegistros").toString()); i++)
		{
			String[] vectorDxFechaHora = mapaAnterior.get("dxfechahora_"+i).toString().split(ConstantesBD.separadorSplit);
			
			mapaNuevo.put("ingreso_"+i, mapaAnterior.get("ingreso_"+i));
			mapaNuevo.put("codigo_paciente_"+i, mapaAnterior.get("codigo_paciente_"+i));
			mapaNuevo.put("nombre_"+i, mapaAnterior.get("nombre_"+i));
			mapaNuevo.put("identificacion_"+i, mapaAnterior.get("identificacion_"+i));
			mapaNuevo.put("edad_"+i, mapaAnterior.get("edad_"+i));
			mapaNuevo.put("sexo_"+i, mapaAnterior.get("sexo_"+i));
			mapaNuevo.put("codarea_"+i, mapaAnterior.get("codarea_"+i));
			mapaNuevo.put("area_"+i, mapaAnterior.get("area_"+i));
			mapaNuevo.put("cama_"+i, mapaAnterior.get("cama_"+i));
			mapaNuevo.put("dxprincipal_"+i, vectorDxFechaHora[0]);
			if(vectorDxFechaHora.length>=3)
				mapaNuevo.put("fechahoraorden_"+i, UtilidadFecha.conversionFormatoFechaAAp(mapaAnterior.get("dxfechahora_"+i).toString().split(ConstantesBD.separadorSplit)[1])+" "+mapaAnterior.get("dxfechahora_"+i).toString().split(ConstantesBD.separadorSplit)[2]);
			else
				mapaNuevo.put("fechahoraorden_"+i, "");
			mapaNuevo.put("profesional_"+i, mapaAnterior.get("profesional_"+i));
			mapaNuevo.put("fechahoraingreso_"+i, UtilidadFecha.conversionFormatoFechaAAp(mapaAnterior.get("fechaingreso_"+i).toString())+" "+mapaAnterior.get("horaingreso_"+i));
			mapaNuevo.put("valoracion_"+i, mapaAnterior.get("valoracion_"+i));
			mapaNuevo.put("evolucionordena_"+i, mapaAnterior.get("evolucionordena_"+i));
			mapaNuevo.put("valoracionordena_"+i, mapaAnterior.get("valoracionordena_"+i));
			mapaNuevo.put("valoracionordenatipo_"+i, mapaAnterior.get("valoracionordenatipo_"+i));
		}
		
		Utilidades.imprimirMapa(mapaNuevo);
		return mapaNuevo;
	}
	
}