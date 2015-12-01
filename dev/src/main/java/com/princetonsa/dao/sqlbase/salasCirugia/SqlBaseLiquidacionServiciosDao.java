/*
 * Feb 6, 2008
 */
package com.princetonsa.dao.sqlbase.salasCirugia;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.InfoDatosInt;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.salas.ConstantesBDSalas;

import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;


/**
 * 
 * @author Sebastián Gómez
 * Objeto usado para manejar los accesos comunes a la fuente de datos
 * para la liquidacion de servicios
 */
public class SqlBaseLiquidacionServiciosDao 
{
	/**
	 * Manejador de logs de la clase
	 */
	private static Logger logger=Logger.getLogger(SqlBaseLiquidacionServiciosDao.class);
	
	private static final String[] indices = {"numeroSolicitud_","fechaOrden_","horaOrden_","orden_","medicoSolicita_","fechaCirugia_","medicoResponde_","consumoPendiente_"};
	
	//**************************************************************************************************************************************
	//***********************************QUERYS PARA EL MANEJO DE LA INFORMACION DE LA ORDEN************************************************
	//***************************************************************************************************************************************
	
	/**
	 * Cadena que consulta el listado de órdenes
	 */
	private static final String consultarListadoOrdenes_Str = "SELECT "+ 
		"s.numero_solicitud AS numero_solicitud, "+
		"to_char(s.fecha_solicitud,'"+ConstantesBD.formatoFechaAp+"') AS fecha_orden, " +
		"substr(s.hora_solicitud,0,6) AS hora_orden, "+
		"s.consecutivo_ordenes_medicas AS orden, "+
		"getnombrepersona(s.codigo_medico) AS medico_solicita, "+
		"coalesce(to_char(sc.fecha_inicial_cx,'"+ConstantesBD.formatoFechaAp+"'),'') AS fecha_cirugia, "+
		"coalesce(getnombrepersona(hq.medico_finaliza),'') AS medico_responde "+ 
		"FROM cuentas c "+ 
		"INNER JOIN solicitudes s ON(s.cuenta=c.id AND " +
			"s.estado_historia_clinica IN ("+ConstantesBD.codigoEstadoHCInterpretada+","+ConstantesBD.codigoEstadoHCCargoDirecto+","+ConstantesBD.codigoEstadoHCRespondida+") AND " +
			"s.tipo = "+ConstantesBD.codigoTipoSolicitudCirugia+" AND " +
			"esSolicitudTotalPendiente(s.numero_solicitud) = '"+ConstantesBD.acronimoSi+"') "+ 
		"INNER JOIN solicitudes_cirugia sc on(sc.numero_solicitud=s.numero_solicitud) "+
		"INNER JOIN hoja_quirurgica hq on(hq.numero_solicitud=s.numero_solicitud) "+
		"WHERE c.id_ingreso = ? and " +
		//Valida si se puede liquidar
		"(" +
			"(sc.salida_sala_paciente is not null and sc.ind_qx in ('"+ConstantesIntegridadDominio.acronimoIndicativoCargoCirugia+"','"+ConstantesIntegridadDominio.acronimoIndicativoCargoNoCruento+"')) " +
			"or " +
			"(sc.ind_qx in ('"+ConstantesIntegridadDominio.acronimoIndicativoCargoCirugiaCargoDirecto+"','"+ConstantesIntegridadDominio.acronimoIndicativoCargoNoCruentoCargoDirecto+"')) " +
		") "+  
		"ORDER BY s.fecha_solicitud DESC,s.consecutivo_ordenes_medicas DESC";
	
	/**
	 * Cadena para verificar si una solicitud tiene consumo pendiente de finalizar
	 */
	private static final String tieneConsumoPendiente_Str = "SELECT "+ 
		"CASE WHEN count(1) > 0 THEN '"+ConstantesBD.acronimoSi+"' ELSE '"+ConstantesBD.acronimoNo+"' END as resultado "+ 
		"FROM materiales_qx "+ 
		"WHERE numero_solicitud = ? AND (finalizado IS NULL OR finalizado = '"+ConstantesBD.acronimoNo+"')";
	
	/**
	 * Cadena que realiza la consulta de los datos de la hoja quirurgica
	 */
	private static final String cargarDatosHojaQuirurgica_Str = "SELECT "+ 
		"coalesce(hq.tipo_sala,"+ConstantesBD.codigoNuncaValido+")  AS codigo_tipo_sala, "+
		"coalesce(getnombretiposala(hq.tipo_sala),'') As nombre_tipo_sala, "+
		"coalesce(hq.sala,"+ConstantesBD.codigoNuncaValido+") AS codigo_sala, "+
		"coalesce(sa.descripcion,'') AS nombre_sala, "+
		"CASE WHEN hq.politrauma = "+ValoresPorDefecto.getValorTrueParaConsultas()+" THEN '"+ConstantesBD.acronimoSi+"' ELSE '"+ConstantesBD.acronimoNo+"' END AS politraumatismo, "+
		"coalesce(hq.tipo_anestesia,"+ConstantesBD.codigoNuncaValido+") AS codigo_tipo_anestesia, "+
		"coalesce(getnombretipoanestesia(hq.tipo_anestesia),'') AS nombre_tipo_anestesia, "+
		"CASE WHEN hq.participo_anestesiologo = "+ValoresPorDefecto.getValorTrueParaConsultas()+" THEN '"+ConstantesBD.acronimoSi+"' ELSE '"+ConstantesBD.acronimoNo+"' END AS participo_anestesiologo," +
		"coalesce(getnombrepersona(hq.medico_finaliza),'') AS medico_responde," +
		"coalesce(to_char(hq.fecha_finaliza,'"+ConstantesBD.formatoFechaAp+"'),'') AS fecha_finaliza, " +
		"hq.finalizada as hoja_qx_finalizada, "+ 
		"anestesiologo AS codigoanestesiologohq, "+
		"getnombrepersona(anestesiologo) AS nombreanestesiologohq, " +
		"(to_char(fecha_ini_anestesia,'DD/MM/YYYY')) as fechainianestesiahq, " +
		"hora_ini_anestesia as horainianestesiahq, " +
		"(to_char(fecha_fin_anestesia,'DD/MM/YYYY')) as fechafinanestesiahq, " +
		"hora_fin_anestesia as horafinanestesiahq "+ 
		"FROM hoja_quirurgica hq "+ 
		"LEFT OUTER JOIN salas sa ON(sa.consecutivo=hq.sala) "+ 
		"WHERE hq.numero_solicitud = ?"; 
	
	/**
	 * Cadena que realiza la consulta de los datos del anestesiologo
	 */
	private static final String consultarDatosAnestesiologo_Str = "SELECT "+ 
		"coalesce(ha.anestesiologo_cobrable,'"+ConstantesBD.acronimoNo+"') AS cobrar_anestesia, "+
		"aha.profesional AS codigo_anestesiologo, "+
		"getnombrepersona(aha.profesional) AS nombre_anestesiologo, " +
		"coalesce(ha.finalizada,"+ValoresPorDefecto.getValorFalseParaConsultas()+") as hoja_anes_finalizada "+ 
		"FROM hoja_anestesia ha "+  
		"INNER JOIN anestesiologos_hoja_anes aha on(aha.numero_solicitud=ha.numero_solicitud) "+ 
		"WHERE ha.numero_solicitud = ? AND aha.definitivo = '"+ConstantesBD.acronimoSi+"'";
	
	
	
	
	/**
	 * Cadena que realiza la consulta del tipo de anestesia
	 */
	private static final String consultarTipoAnestesia_Str = "SELECT "+ 
		"ta.codigo As codigo_tipo_anestesia, "+
		"ta.descripcion AS nombre_tipo_anestesia "+ 
		"FROM hoja_tipo_anestesia taha "+ 
		"INNER JOIN tipos_anestesia_inst_cc taic ON(taic.codigo=taha.tipo_anestesia_inst_cc) "+ 
		"INNER JOIN tipos_anestesia ta ON(ta.codigo=taic.tipo_anestesia) "+ 
		"WHERE "+ 
		"taha.numero_solicitud = ? ORDER BY ta.prioridad_cobro ASC";
	
	/**
	 * Cadena que realiza la consulta de otros profesionales
	 */
	private static final String consultarOtrosProfesionales_Str = "SELECT "+ 
		"piq.consecutivo AS consecutivo, "+
		"piq.numero_solicitud As numero_solicitud, "+
		"piq.tipo_asocio AS codigo_asocio, "+
		"ta.nombre_asocio As nombre_asocio, "+
		"ta.tipos_servicio AS tipo_asocio, "+
		"piq.codigo_profesional As codigo_profesional, "+
		"getnombrepersona(piq.codigo_profesional) AS nombre_profesional, "+
		"piq.cobrable AS cobrable," +
		"piq.historia_clinica AS historia_clinica, "+
		"'"+ConstantesBD.acronimoSi+"' AS existe_bd, " +
		"'"+ConstantesBD.acronimoNo+"' AS eliminar "+
		"FROM profesionales_info_qx piq "+ 
		"INNER JOIN tipos_asocio ta ON(ta.codigo=piq.tipo_asocio) "+ 
		"WHERE piq.numero_solicitud = ? ORDER BY piq.historia_clinica desc";
	
	/**
	 * Método implementado para cargar los profesionales de la cirugía
	 */
	private static final String consultarProfesionalesCirugia_Str = "SELECT "+ 
		"pc.consecutivo As consecutivo, "+
		"ta.codigo AS consecutivo_asocio, "+
		"ta.nombre_asocio AS nombre_asocio, "+
		"ta.tipos_servicio AS tipo_asocio, "+
		"getnombretiposervicio(ta.tipos_servicio) AS nombre_tipo_asocio, "+
		"pc.especialidad AS codigo_especialidad, "+
		"getnombreespecialidad(pc.especialidad) AS nombre_especialidad, "+
		"pc.codigo_profesional AS codigo_profesional, "+
		"getnombrepersona(pc.codigo_profesional) AS nombre_profesional, "+
		"pc.cobrable AS cobrable, "+
		"coalesce(pc.pool||'','') AS pool, "+
		"coalesce(getdescripcionpool(pc.pool),'') AS nombre_pool, "+
		"coalesce(pc.tipo_especialista,'') AS tipo_especialista, " +
		"pc.historia_clinica AS historia_clinica, " +
		"'"+ConstantesBD.acronimoSi+"' AS existe_bd, " +
		"'"+ConstantesBD.acronimoNo+"' AS eliminar "+ 
		"FROM profesionales_cirugia pc "+ 
		"INNER JOIN tipos_asocio ta ON(ta.codigo=pc.tipo_asocio) "+
		"WHERE pc.cod_sol_cx_serv = ? order by historia_clinica desc";
	
	//**************************************************************************************************************************************
	//***********************************QUERYS PARA EL MANEJO DE LA LIQUIDACION************************************************************
	//***************************************************************************************************************************************
	
	/**
	 * Cadena que consulta el listado de articulos del consumo de materiales
	 */
	private static final String obtenerListaArticulosConsumoMateriales_Str = "SELECT DISTINCT articulo as codigo_articulo, getdescripcionarticulo(articulo) As nombre_articulo FROM det_fin_materiales_qx WHERE numero_solicitud = ? ORDER BY articulo";
	
	/**
	 * Cadena que actualiza la inclusion y la tarifa del artículo
	 */
	private static final String actualizarInclusionYTarifaArticulo_Str = " UPDATE det_fin_materiales_qx SET incluido = ?, valor_unitario = ?, fecha_modifica = current_date, hora_modifica = "+ValoresPorDefecto.getSentenciaHoraActualBD()+" WHERE numero_solicitud = ? AND articulo = ?";
	
	/**
	 * Cadena para consultar el codigo propietario de un asocio parametrizado por liquidacion Soat
	 */
	private static final String consultaCodigoPropietarioAsocioXSoat_Str = "SELECT codigo AS codigo_propietario FROM det_codigos_grupos WHERE cod_detalle_grupo = ? AND codigo_tarifario = ?";
	
	/**
	 * Cadena para consultar el codigo propietario de un asocio parametrizado por liquidacion Iss
	 */
	private static final String consultaCodigoPropietarioAsocioXIss_Str = "SELECT coalesce(codigo,'') AS codigo_propietario FROM det_cod_asocios_x_uvr WHERE cod_detalle_asocio_x_uvr = ? and codigo_tarifario = ?";
	
	/**
	 * Cadena para consultar el codigo propietario de un asocio parametrizado por rango de tiempos
	 */
	private static final String consultaCodigoPropietarioAsocioXRangoTiempo_Str = "SELECT codigo AS codigo_propietario FROM det_asoc_x_ran_tiem WHERE codigo_asocxrangtiem = ? AND codigo_tarfio_oficial = ?";
	
	
	/**
	 * Cadena para insertar un asocio de honorarios
	 */
	private static final String insertarAsocioHonorarios_Str = "INSERT INTO det_cx_honorarios (codigo,cod_sol_cx_servicio,especialidad_medico,institucion,medico,servicio,pool,tipo_asocio,codigo_propietario,valor,cobrable,centro_costo_ejecuta) values (?,?,?,?,?,?,?,?,?,?,?,?)";
	
	/**
	 * Cadena para insertar un asocio de salas / materiales
	 */
	private static final String insertarAsocioSalasMateriales_Str = "INSERT INTO det_asocio_cx_salas_mat (codigo,cod_sol_cx_servicio,servicio,tipo_asocio,codigo_propietario,valor,medico) values (?,?,?,?,?,?,?)";
	
	/**
	 * Cadena que consulta los materiales especiales
	 */
	private static final String consultarMaterialesEspeciales_Str = "SELECT " +
		"articulo AS codigo_articulo, " +
		"getdescripcionarticulo(articulo) as nombre_articulo, " +
		"coalesce(costo_unitario,"+ConstantesBD.codigoNuncaValidoDoubleNegativo+") as costo_unitario," +
		"sum(cantidad_consumo_total) as cantidad," +
		"coalesce(valor_unitario,"+ConstantesBD.codigoNuncaValidoDoubleNegativo+") as valor_unitario " +
		"FROM det_fin_materiales_qx " +
		"WHERE numero_solicitud = ? AND incluido = '"+ConstantesBD.acronimoNo+"' " +
		"GROUP BY articulo,costo_unitario,valor_unitario";
	
	/**
	 * Cadena que inserta el log bd de liquidacion de servicios
	 */
	private static final String insertarLiquidacionServicios_Str = "INSERT INTO liquidacion_servicios (codigo,numero_solicitud,fecha,hora,usuario) VALUES(?,?,CURRENT_DATE,"+ValoresPorDefecto.getSentenciaHoraActualBD()+",?)";
	
	/**
	 * Cadena para obtener los asocios liquidados
	 */
	private static final String consultarAsociosLiquidados_Str = "SELECT * "+ 
		"FROM "+ 
		"(" +
			"SELECT " +
			"dch.codigo AS consecutivo, "+ 
			"dch.cod_sol_cx_servicio AS cod_sol_cx_servicio, "+ 
			"coalesce(dch.especialidad_medico,"+ConstantesBD.codigoNuncaValido+")  AS codigo_especialidad, "+ 
			"coalesce(getnombreespecialidad(dch.especialidad_medico),'') AS nombre_especialidad, "+
			"coalesce(dch.medico,"+ConstantesBD.codigoNuncaValido+") AS codigo_profesional, "+
			"coalesce(getnombrepersona(dch.medico),'') AS nombre_profesional, "+
			"dch.servicio AS codigo_servicio, "+
			"getnombreservicio(dch.servicio,"+ConstantesBD.codigoTarifarioCups+") AS nombre_servicio, "+
			"coalesce(dch.pool,"+ConstantesBD.codigoNuncaValido+") AS codigo_pool, "+
			"coalesce(getdescripcionpool(dch.pool),'') AS nombre_pool, "+
			"dch.tipo_asocio AS codigo_asocio, "+
			"ta.nombre_asocio AS nombre_asocio, " +
			"ta.codigo_asocio AS acronimo_asocio, " +
			"ta.tipos_servicio AS codigo_tipo_servicio, " +
			"getnombretiposervicio(ta.tipos_servicio) AS nombre_tipo_servicio, "+
			"coalesce(dch.codigo_propietario,'') AS codigo_propietario, "+
			"dch.valor AS valor "+ 
			"FROM det_cx_honorarios dch " +
			"INNER JOIN tipos_asocio ta ON(ta.codigo=dch.tipo_asocio) "+ 
			"WHERE dch.cod_sol_cx_servicio = ? "+  
			"UNION "+ 
			"SELECT "+ 
			"dcs.codigo AS consecutivo, "+ 
			"dcs.cod_sol_cx_servicio AS cod_sol_cx_servicio, "+ 
			ConstantesBD.codigoNuncaValido+" AS codigo_especialidad, "+ 
			"'' AS nombre_especialidad, "+
			ConstantesBD.codigoNuncaValido+" AS codigo_profesional, "+
			"'' AS nombre_profesional, "+
			"dcs.servicio AS codigo_servicio, "+
			"getnombreservicio(dcs.servicio,"+ConstantesBD.codigoTarifarioCups+") AS nombre_servicio, "+
			ConstantesBD.codigoNuncaValido+" AS codigo_pool, "+
			"'' AS nombre_pool, "+
			"dcs.tipo_asocio AS codigo_asocio, "+
			"ta.nombre_asocio AS nombre_asocio, " +
			"ta.codigo_asocio AS acronimo_asocio, " +
			"ta.tipos_servicio AS codigo_tipo_servicio, " +
			"getnombretiposervicio(ta.tipos_servicio) AS nombre_tipo_servicio, "+
			"coalesce(dcs.codigo_propietario,'') AS codigo_propietario, "+
			"dcs.valor AS valor "+ 
			"FROM det_asocio_cx_salas_mat dcs "+ 
			"INNER JOIN tipos_asocio ta ON(ta.codigo=dcs.tipo_asocio) "+
			"WHERE dcs.cod_sol_cx_servicio = ?" +
		") t "+ 
		"ORDER BY t.nombre_asocio";
	
	
	
	//**************************************************************************************************************************************
	//***********************************MÉTODOS PARA EL MANEJO DE LA INFORMACION DE LA ORDEN************************************************
	//***************************************************************************************************************************************
	/**
	 * Método que realiza la consulta las ordenes de cirugia para liquidar
	 * @param con
	 * @param campos
	 * @return
	 */
	public static HashMap<String, Object> consultarListadoOrdenes(Connection con,HashMap campos)
	{
		HashMap<String, Object> resultados = new HashMap<String, Object>();
		try
		{
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consultarListadoOrdenes_Str, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			pst.setInt(1,Utilidades.convertirAEntero(campos.get("codigoIngreso")+""));
			
			resultados = UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()), true, true);
			
			for(int i=0;i<Utilidades.convertirAEntero(resultados.get("numRegistros")+"", true);i++)
			{
				pst =  new PreparedStatementDecorator(con.prepareStatement(tieneConsumoPendiente_Str, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
				pst.setInt(1, Utilidades.convertirAEntero(resultados.get("numeroSolicitud_"+i)+""));
				ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
				
				if(rs.next())
					resultados.put("consumoPendiente_"+i, rs.getString("resultado"));
				
				pst.close();
				rs.close();
				
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en consultarListadoOrdenes: "+e);
		}
		
		resultados.put("INDICES",indices);
		return resultados;
	}
	
	/**
	 * Método implementado para cargar los datos del acto quirurgico
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public static HashMap<String, Object> cargarDatosActoQuirurgico(Connection con,String numeroSolicitud,int codigoInstitucion)
	{
		HashMap<String, Object> resultados = new HashMap<String, Object>();
		try
		{
			//1) **************************	CONSULTA DATOS DE LA HOJA QUIRURGICA*********************************************************
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(cargarDatosHojaQuirurgica_Str, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			pst.setInt(1,Utilidades.convertirAEntero(numeroSolicitud));
			
			resultados = UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()), false, true);
			pst.close();
			
			if(Utilidades.convertirAEntero(resultados.get("numRegistros")+"", true)>0)
			{
				//Se verifica si participó anestesiólogo para saber de donde sacar los datos de la anestesia
				if(UtilidadTexto.getBoolean(resultados.get("participoAnestesiologo").toString()))
				{
					//2) *************** CONSULTA DATOS DEL ANESTESIÓLOGO **********************************
					if(UtilidadTexto.getBoolean(ValoresPorDefecto.getManejaHojaAnestesia(codigoInstitucion)))
					{
						pst =  new PreparedStatementDecorator(con.prepareStatement(consultarDatosAnestesiologo_Str, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
						pst.setInt(1,Utilidades.convertirAEntero(numeroSolicitud));
						
						ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
						if(rs.next())
						{
							resultados.put("cobrarAnestesia",rs.getString("cobrar_anestesia"));
							resultados.put("codigoAnestesiologo",rs.getString("codigo_anestesiologo"));
							resultados.put("nombreAnestesiologo",rs.getString("nombre_anestesiologo"));
							if (UtilidadTexto.getBoolean(rs.getString("hoja_anes_finalizada")))
								resultados.put("hojaAnesFinalizada",ConstantesBD.acronimoSi);
							else
								resultados.put("hojaAnesFinalizada",ConstantesBD.acronimoNo);
						}
						else
						{
							resultados.put("cobrarAnestesia",ConstantesBD.acronimoSi);
							resultados.put("codigoAnestesiologo","");
							resultados.put("nombreAnestesiologo","");
							resultados.put("hojaAnesFinalizada",ConstantesBD.acronimoNo);
							logger.error("No se pudo encontrar datos de la hoja anestesia");
						}
						pst.close();
						rs.close();
					}
					else
					{
						resultados.put("cobrarAnestesia",ConstantesBD.acronimoSi);
						resultados.put("codigoAnestesiologo",resultados.get("codigoanestesiologohq")+"");
						resultados.put("nombreAnestesiologo",resultados.get("nombreanestesiologohq")+"");
						resultados.put("hojaAnesFinalizada",ConstantesBD.acronimoSi);
						logger.error("No se pudo encontrar datos de la hoja anestesia");
					}
					
					
					//**********************************************************************************
					//3) ************* CONSULTA DE LOS DATOS DE LA ANESTESIA*****************************
					pst =  new PreparedStatementDecorator(con.prepareStatement(consultarTipoAnestesia_Str,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					pst.setInt(1,Utilidades.convertirAEntero(numeroSolicitud));
					
					ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
					if(rs.next())
					{
						resultados.put("codigoTipoAnestesia",rs.getString("codigo_tipo_anestesia"));
						resultados.put("nombreTipoAnestesia",rs.getString("nombre_tipo_anestesia"));
					}
					pst.close();
					rs.close();
				
					//*************************************************************************************
				}
				else
				{
					resultados.put("cobrarAnestesia",ConstantesBD.acronimoSi);
					resultados.put("codigoAnestesiologo",""); //pendiente para asignarle el cirujano
					resultados.put("nombreAnestesiologo",""); //pendiente para asignarle el cirujano
					resultados.put("hojaAnesFinalizada",ConstantesBD.acronimoSi); //como no participó anestesiólogo se considera que se finalizó la hoja de anestesia
				}
			}
			else
			{
				logger.error("No se pudo encontrar datos de la hoja quirurgica");
				resultados.put("cobrarAnestesia",ConstantesBD.acronimoSi);
				resultados.put("codigoAnestesiologo","");
				resultados.put("nombreAnestesiologo","");
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en cargarDatosHojaQuirurgica: "+e);
		}
		return resultados;
	}
	
	/**
	 * Método que realiza la consulta de otros profesionales del acto quirúrgico
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public static HashMap<String, Object> consultarOtrosProfesionales(Connection con,String numeroSolicitud)
	{
		HashMap<String,Object> resultados = new HashMap<String, Object>();
		try
		{
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consultarOtrosProfesionales_Str, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			pst.setInt(1,Utilidades.convertirAEntero(numeroSolicitud));
			
			resultados = UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()), true, true);
			pst.close();
		}
		catch(SQLException e)
		{
			logger.error("Error en consultarOtrosProfesionales: "+e);
		}
		return resultados;
	}
	
	/**
	 * Método que consulta los profesionales de la cirugía
	 * @param con
	 * @param codigoCirugia
	 * @return
	 */
	public static HashMap<String, Object> consultarProfesionalesCirugia(Connection con,String codigoCirugia)
	{
		HashMap<String, Object> resultados = new HashMap<String, Object>();
		try
		{
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consultarProfesionalesCirugia_Str, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			pst.setInt(1,Utilidades.convertirAEntero(codigoCirugia,true));
			
			resultados = UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()), true, true);
			pst.close();
		}
		catch(SQLException e)
		{
			logger.error("Error en consultarProfesionalesCirugia: "+e);
		}
		
		return resultados;
	}
	
	//**************************************************************************************************************************************
	//***********************************MÉTODOS PARA EL MANEJO DE LA LIQUIDACIÓN***********************************************************
	//***************************************************************************************************************************************
	/**
	 * Método para obtener la parametrización de los valores de los asocios x grupos
	 */
	public static HashMap<String, Object> obtenerValorAsociosXGrupo(Connection con,HashMap campos)
	{
		HashMap<String, Object> resultados = new HashMap<String, Object>();
		try
		{
			Utilidades.imprimirMapa(campos);
			//*********Se toman los parámetros********************************************************
			String consulta = "";
			int codigoConvenio = Utilidades.convertirAEntero(campos.get("codigoConvenio").toString());
			String fechaReferencia = campos.get("fechaReferencia").toString();
			int codigoInstitucion = Utilidades.convertirAEntero(campos.get("codigoInstitucion").toString());
			int codigoEsquemaTarifario = Utilidades.convertirAEntero(campos.get("codigoEsquemaTarifario").toString());
			int grupo = Utilidades.convertirAEntero(campos.get("grupo").toString());
			String tipoServicio = campos.get("tipoServicio").toString();
			int codigoAsocio = Utilidades.convertirAEntero(campos.get("codigoAsocio").toString());
			//******************************************************************************************
			
			int nroConsulta = 1;
			
			while(Utilidades.convertirAEntero(resultados.get("numRegistros")+"", true)==0&&nroConsulta<=3)
			{
				switch(nroConsulta)
				{
					//1) SE FILTRA POR CONVENIO
					case 1:
						consulta = "SELECT codigo FROM grupos " +
						"WHERE " +
						"convenio = "+codigoConvenio+" AND " +
						"'"+UtilidadFecha.conversionFormatoFechaABD(fechaReferencia)+"' between fecha_inicial AND fecha_final AND " +
						"institucion = "+codigoInstitucion;
					break;
					//2) SE FILTRA POR ESQUEMA TARIFARIO ESPECÍFICO
					case 2:
						consulta = "SELECT codigo FROM grupos WHERE esquema_tarifario = "+codigoEsquemaTarifario+" AND institucion = "+codigoInstitucion;
					break;
					//3) SE FILTRA POR ESQUEMA TARIFARIO TODOS
					case 3:
						consulta = "SELECT codigo FROM grupos WHERE esq_tar_general = "+ConstantesBD.codigoEsqTarifarioGeneralTodosSoat+" AND institucion = "+codigoInstitucion;
					break;
				}
				
				//logger.info("consulta grupos=> "+consulta);
				Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
				ResultSetDecorator rs = new ResultSetDecorator(st.executeQuery(consulta));
				
				if(rs.next())
				{
					//Se consulta el detalle de la parametrizacion de grupos 
					consulta = "SELECT dg.codigo as codigo_parametrizacion,dg.asocio,ta.tipos_servicio as codigo_tipo_servicio,ta.nombre_asocio,dg.tipo_liquidacion,dg.unidades,dg.valor,dg.liquidar_por " +
					"FROM detalle_grupos dg " +
					"INNER JOIN tipos_asocio ta ON(ta.codigo=dg.asocio) " +
					"WHERE dg.codigo_grupo = "+rs.getInt("codigo")+" and dg.tipo_servicio = '"+tipoServicio+"' and dg.grupo = "+grupo;
					
					if(codigoAsocio>0)
						consulta += " and dg.asocio ="+codigoAsocio;
					
					st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
					//logger.info("detalle=> "+consulta);
					resultados = UtilidadBD.cargarValueObject(new ResultSetDecorator(st.executeQuery(consulta)), true, true);
				}
				else
					resultados.put("numRegistros", "0");
				
				st.close();
				rs.close();
				nroConsulta++;
			
			}
			
			
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerValorAsociosXGrupo: "+e);
		}
		Utilidades.imprimirMapa(resultados);
		return resultados;
	}
	
	/**
	 * Método que realiza la consulta de la excepcion Qx de un asocio
	 * @param con
	 * @param campos
	 * @return
	 */
	public static HashMap<String, Object> obtenerExcepcionQxAsocio(Connection con,HashMap campos)
	{
		HashMap<String, Object> resultados = new HashMap<String, Object>();
		try
		{
			//Se toman los parámetros**********************************************
			int codigoConvenio = Utilidades.convertirAEntero(campos.get("codigoConvenio").toString());
			int codigoInstitucion = Utilidades.convertirAEntero(campos.get("codigoInstitucion").toString());
			String fechaReferencia = UtilidadFecha.conversionFormatoFechaABD(campos.get("fechaReferencia").toString());
			int codigoContrato = Utilidades.convertirAEntero(campos.get("codigoContrato").toString());
			int codigoCentroCosto = Utilidades.convertirAEntero(campos.get("codigoCentroCosto").toString());
			int codigoViaIngreso = Utilidades.convertirAEntero(campos.get("codigoViaIngreso").toString());
			String codigoTipoPaciente = campos.get("codigoTipoPaciente").toString();
			//************************************************************************************************
			
			String codigoExcepcion = "",consulta = "";
			int nroConsulta = 1;
			final String consultaInicial = "SELECT " +
				"eqc.codigo " +
				"FROM excepciones_qx eq " +
				"INNER JOIN excepciones_qx_cc eqc ON(eqc.codigo_excepcion_qx=eq.codigo) " +
				"WHERE " +
				"eq.convenio = "+codigoConvenio+" AND " +
				"eq.institucion = "+codigoInstitucion+" and " +
				"'"+fechaReferencia+"' between eq.fecha_inicial and eq.fecha_final and " +
				"eq.contrato = "+codigoContrato+" ";
			
			while(Utilidades.convertirAEntero(resultados.get("numRegistros")+"", true)==0 && nroConsulta<=4)
			{
				switch(nroConsulta)
				{
					//Filtro por Via ingreso, tipo paciente y centro de csto
					case 1:
					consulta = consultaInicial + " AND eqc.via_ingreso = "+codigoViaIngreso+" and eqc.tipo_paciente = '"+codigoTipoPaciente+"' and eqc.centro_costo = "+codigoCentroCosto;
					break;
					
					//Filtro por vía de ingreso, tipo paciente y centro costo todos
					case 2:
					consulta = consultaInicial + " AND eqc.via_ingreso = "+codigoViaIngreso+" and eqc.tipo_paciente = '"+codigoTipoPaciente+"' and eqc.centro_costo IS NULL ";
					break;
					
					//Filtro por solo el centro de costo
					case 3:
					consulta = consultaInicial + " AND eqc.via_ingreso IS NULL and eqc.tipo_paciente IS NULL and eqc.centro_costo = "+codigoCentroCosto;
					break;
					
					//Filtro por todas las vias de ingreso, tipos de paciente y centro de costo
					case 4:
					consulta = consultaInicial + " AND eqc.via_ingreso IS NULL and eqc.tipo_paciente IS NULL and eqc.centro_costo IS NULL ";
					break;
					
				}
				
				Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
				ResultSetDecorator rs = new ResultSetDecorator(st.executeQuery(consulta));
				//logger.info("consulta encabezado excepciones qx=> "+consulta+", nroConsulta: "+nroConsulta);
				if(rs.next())
				{
					codigoExcepcion = rs.getString("codigo");
					//logger.info("consecutivo excepcion_qx encontrada!! "+codigoExcepcion);
					resultados = obtenerDetalleExcepcionesQxAsocio01(con,codigoExcepcion,campos);
				}
				else
					resultados.put("numRegistros", "0");
				
				st.close();
				rs.close();
				nroConsulta++;
			}
			
			
			
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerExcepcionQxAsocio: "+e);
		}
		return resultados;
	}

	/**
	 * Método para obtener el detalle de las excepciones qx de asocios
	 * @param con
	 * @param codigoExcepcion
	 * @param campos
	 * @return
	 */
	private static HashMap<String, Object> obtenerDetalleExcepcionesQxAsocio01(Connection con, String codigoExcepcion, HashMap campos) 
	{
		HashMap<String, Object> resultados = new HashMap<String, Object>();
		
		//Se toman los parámetros********************************************************
		
		int codigoTipoAsocio = Utilidades.convertirAEntero(campos.get("codigoTipoAsocio").toString());
		int codigoServicio = Utilidades.convertirAEntero(campos.get("codigoServicio").toString());
		String tipoServicio = campos.get("tipoServicio").toString();
		int codigoEspecialidad = Utilidades.convertirAEntero(campos.get("codigoEspecialidad").toString());
		int codigoGrupoServicio = Utilidades.convertirAEntero(campos.get("codigoGrupoServicio").toString());
		//*******************************************************************************************
		
		final String consultaInicial = "SELECT " +
			"tipo_liquidacion," +
			"coalesce(liquidar_sobre_tarifa,'') AS liquidar_sobre_tarifa," +
			"valor, " +
			"coalesce(liquidar_x_porcentaje,'') as liquidar_x_porcentaje " +
			"FROM det_excepciones_qx_cc " +
			"WHERE " +
			"codigo_excepcion_qx_cc = "+codigoExcepcion+" and " +
			"tipo_asocio = "+codigoTipoAsocio+" ";
		String consulta = "";
		
		int nroConsulta = 1;
		
		while(Utilidades.convertirAEntero(resultados.get("numRegistros")+"", true)==0&&nroConsulta <= 5)
		{
			switch(nroConsulta)
			{
				//1) CONSULTA DEL MERO SERVICIO
				case 1:
					consulta = consultaInicial + " AND servicio = "+codigoServicio+" ";
				break;
				//2) CONSULTA POR TIPO SERVICIO, ESPECIALIDAD Y GRUPO DE SERVICIO
				case 2:
					consulta = consultaInicial + " AND tipo_servicio = '"+tipoServicio+"' AND especialidad = "+codigoEspecialidad+" AND grupo_servicio = "+codigoGrupoServicio+" and servicio is null ";
				break;
				//3) CONSULTA POR TIPO SERVICIO, ESPECIALIDAD
				case 3:
					consulta = consultaInicial + " AND tipo_servicio = '"+tipoServicio+"' AND especialidad = "+codigoEspecialidad+" AND (grupo_servicio = "+codigoGrupoServicio+" OR grupo_servicio IS NULL) and servicio is null ";
				break;
				//4) CONSULTA POR TIPO SERVICIO
				case 4:
					consulta = consultaInicial + " AND tipo_servicio = '"+tipoServicio+"' AND (especialidad = "+codigoEspecialidad+" OR especialidad IS NULL) AND (grupo_servicio = "+codigoGrupoServicio+" OR grupo_servicio IS NULL) and servicio is null ";
				break;
				//5) CONSULTA POR TODOS LOS DATOS DEL SERVICIO
				case 5:
					consulta = consultaInicial + " AND (tipo_servicio = '"+tipoServicio+"' or tipo_servicio IS NULL) AND (especialidad = "+codigoEspecialidad+" OR especialidad IS NULL) AND (grupo_servicio = "+codigoGrupoServicio+" OR grupo_servicio IS NULL) and servicio is null ";
				break;
			}
			
			resultados = obtenerDetalleExcepcionesQxAsocio02(con, consulta, campos);
			nroConsulta ++;
		}
		//logger.info("encontró resultados? "+resultados.get("numRegistros"));
		
		return resultados;
	}

	/**
	 * Método que obtiene el detalle de las excepciones Qx del asocio
	 * @param con
	 * @param consulta
	 * @param campos
	 * @return
	 */
	private static HashMap<String, Object> obtenerDetalleExcepcionesQxAsocio02(Connection con, String consulta, HashMap campos) 
	{
		HashMap<String, Object> resultados = new HashMap<String, Object>();
		try
		{
			//Se toman los parámetros **********************************************************
			int codigoTipoSala = Utilidades.convertirAEntero(campos.get("codigoTipoSala").toString());
			String tipoEspecialista = campos.get("tipoEspecialista").toString();
			String acronimoTipoCirugia = campos.get("acronimoTipoCirugia").toString();
			String via = campos.get("via").toString();
			//***********************************************************************************
			
			final String consultaInicial = consulta;
			int nroConsulta = 1;
			String consultaDefinitiva = "";
			
			while(Utilidades.convertirAEntero(resultados.get("numRegistros")+"", true)==0&&nroConsulta <= 5)
			{
				switch(nroConsulta)
				{
					//1) SE CONSULTA POR TIPO SALA, TIPO CIRUGIA,  TIPO ESPECIALISTA Y VIA
					case 1:
						consultaDefinitiva = consultaInicial + " and tipo_sala = "+codigoTipoSala+" and tipo_cirugia = '"+acronimoTipoCirugia+"' and continua_medico = '"+tipoEspecialista+"' and continua_via_acceso = '"+via+"' ";
					break;
					//2) SE CONSULTA POR TIPO SALA, TIPO CIRUGIA Y TIPO ESPECIALISTA
					case 2:
						consultaDefinitiva = consultaInicial + " and tipo_sala = "+codigoTipoSala+" and tipo_cirugia = '"+acronimoTipoCirugia+"' and continua_medico = '"+tipoEspecialista+"' and (continua_via_acceso = '"+via+"' or continua_via_acceso IS NULL) ";
					break;
					//3) SE CONSULTA POR TIPO SALA, TIPO CIRUGIA 
					case 3:
						consultaDefinitiva = consultaInicial + " and tipo_sala = "+codigoTipoSala+" and tipo_cirugia = '"+acronimoTipoCirugia+"' and (continua_medico = '"+tipoEspecialista+"' or continua_medico IS NULL) and (continua_via_acceso = '"+via+"' or continua_via_acceso IS NULL) ";
					break;
					//4) SE CONSULTA POR TIPO SALA 
					case 4:
						consultaDefinitiva = consultaInicial + " and tipo_sala = "+codigoTipoSala+" and (tipo_cirugia = '"+acronimoTipoCirugia+"' OR tipo_cirugia IS NULL) and (continua_medico = '"+tipoEspecialista+"' or continua_medico IS NULL) and (continua_via_acceso = '"+via+"' or continua_via_acceso IS NULL) ";
					break;
					//5) SE CONSULTA POR TODOs
					case 5:
						consultaDefinitiva = consultaInicial + " and (tipo_sala = "+codigoTipoSala+" OR tipo_sala IS NULL) and (continua_medico = '"+tipoEspecialista+"' or continua_medico IS NULL) and (continua_via_acceso = '"+via+"' or continua_via_acceso IS NULL) ";
					break;
				}
				
				//logger.info("nroConsulta Excepciones Qx["+nroConsulta+"]=> "+consultaDefinitiva+"\n\n");
				Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
				resultados = UtilidadBD.cargarValueObject(new ResultSetDecorator(st.executeQuery(consultaDefinitiva)), true, true);
				st.close();
				nroConsulta ++;
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerDetalleExcepcionesQxAsocio02: "+e);
		}
		return resultados;
	}
	
	
	/**
	 * Método que retorna el servicio de un asocio
	 * @param con
	 * @param campos
	 * @return
	 */
	public static int obtenerServicioAsocio(Connection con,HashMap campos)
	{
		int codigoServicio = ConstantesBD.codigoNuncaValido;
		try
		{
			String consulta = "SELECT " +
				"servicio AS codigo_servicio " +
				"FROM servicios_asocios " +
				"WHERE " +
				"tipo_servicio = '"+campos.get("tipoServicio")+"' AND " +
				"asocio = "+campos.get("codigoAsocio")+" AND " +
				"institucion = "+campos.get("codigoInstitucion");
			
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			ResultSetDecorator rs = new ResultSetDecorator(st.executeQuery(consulta));
			
			if(rs.next())
				codigoServicio = rs.getInt("codigo_servicio");
			
			st.close();
			rs.close();
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerServicioAsocio: "+e);
		}
		return codigoServicio;
	}
	
	/**
	 * Método implementado para consultar la excepcion tarifa quirurgica de un asocio
	 * @param con
	 * @param campos
	 * @return
	 */
	public static HashMap<String, Object> obtenerExcepcionTarifaQxAsocio(Connection con,HashMap campos)
	{
		HashMap<String, Object> resultados = new HashMap<String, Object>();
		try
		{
			//Se toman los parámetros**********************************************************
			int codigoConvenio = Utilidades.convertirAEntero(campos.get("codigoConvenio").toString());
			String fechaReferencia = UtilidadFecha.conversionFormatoFechaABD(campos.get("fechaReferencia").toString());
			int codigoInstitucion = Utilidades.convertirAEntero(campos.get("codigoInstitucion").toString());
			int codigoViaIngreso = Utilidades.convertirAEntero(campos.get("codigoViaIngreso").toString());
			String codigoTipoPaciente = campos.get("codigoTipoPaciente").toString();
			int codigoCentroCosto = Utilidades.convertirAEntero(campos.get("codigoCentroCosto").toString());
			
			//Se consulta por convenio********************************************************
			String consulta = "SELECT codigo " +
				"FROM ex_tarifas_asocios " +
				"WHERE " +
				"convenio = "+codigoConvenio+" AND " +
				"'"+fechaReferencia+"' between fecha_inicial and fecha_final and " +
				"institucion = "+codigoInstitucion;
			int nroConsulta = 1;
			String codigoExTarifaAsocio = "";
			String codExTarifaAsocioViTpCC = "";
			
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			ResultSetDecorator rs = new ResultSetDecorator(st.executeQuery(consulta));
			
			if(rs.next())
			{
				codigoExTarifaAsocio = rs.getString("codigo");
				
				while(Utilidades.convertirAEntero(resultados.get("numRegistros")+"", true)==0&&nroConsulta<=4)
				{
					switch(nroConsulta)
					{
						//FILTRO POR VIA DE INGRESO, TIPO PACIENTE Y CENTRO DE COSTO
						case 1:
							consulta = "SELECT codigo " +
								"FROM ex_tarifas_asocios_xvitpcc WHERE " +
								"codigo_encab = "+codigoExTarifaAsocio+" AND " +
								"via_ingreso = "+codigoViaIngreso+" AND " +
								"tipo_paciente = '"+codigoTipoPaciente+"' and " +
								"centro_costo = "+codigoCentroCosto;
						break;
						//FILTRO POR VIA DE INGRESO, TIPO PACIENTE
						case 2:
							consulta = "SELECT codigo " +
								"FROM ex_tarifas_asocios_xvitpcc WHERE " +
								"codigo_encab = "+codigoExTarifaAsocio+" AND " +
								"via_ingreso = "+codigoViaIngreso+" AND " +
								"tipo_paciente = '"+codigoTipoPaciente+"' and " +
								"(centro_costo = "+codigoCentroCosto+" OR centro_costo IS NULL)";
						break;
						//FILTRO POR VIA DE INGRESO
						case 3:
							consulta = "SELECT codigo " +
								"FROM ex_tarifas_asocios_xvitpcc WHERE " +
								"codigo_encab = "+codigoExTarifaAsocio+" AND " +
								"via_ingreso = "+codigoViaIngreso+" AND " +
								"(tipo_paciente = '"+codigoTipoPaciente+"' OR tipo_paciente IS NULL) and " +
								"(centro_costo = "+codigoCentroCosto+" OR centro_costo IS NULL)";
						break;
						//FILTRO POR TODOS
						case 4:
							consulta = "SELECT codigo " +
								"FROM ex_tarifas_asocios_xvitpcc WHERE " +
								"codigo_encab = "+codigoExTarifaAsocio+" AND " +
								"(via_ingreso = "+codigoViaIngreso+" OR via_ingreso IS NULL) AND " +
								"(tipo_paciente = '"+codigoTipoPaciente+"' OR tipo_paciente IS NULL) and " +
								"(centro_costo = "+codigoCentroCosto+" OR centro_costo IS NULL)";
						break;
					}
					st.close();
					rs.close();
					
					nroConsulta++;
					st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
					rs =  new ResultSetDecorator(st.executeQuery(consulta));
					
					if(rs.next())
						codExTarifaAsocioViTpCC = rs.getString("codigo");
					else
						codExTarifaAsocioViTpCC = "";
					
					///Si se encontró parametrización por via ingreso o tipo paciente o centro costo entonces se prosigue al detalle
					if(!codExTarifaAsocioViTpCC.equals(""))
					{
						resultados = obtenerDetalleExcepcionTarifaQxAsocio01(con,codExTarifaAsocioViTpCC,campos);
					}
					else
						resultados.put("numRegistros","0");
					
					st.close();
					rs.close();
				}
				
				
			}
			else
				resultados.put("numRegistros", "0");
			
			
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerExcepcionTarifaQxAsocio: "+e);
		}
		return resultados;
	}
	
	
	/**
	 * Método implementado para ampliar el filtro del detalle de una excepcion tarifa quirurgica de un asocio
	 * @param con
	 * @param codExTarifaAsocioViTpCC
	 * @param campos
	 * @return
	 */
	private static HashMap<String, Object> obtenerDetalleExcepcionTarifaQxAsocio01(Connection con, String codExTarifaAsocioViTpCC, HashMap campos) 
	{
		HashMap<String, Object> resultados = new HashMap<String, Object>();
		
		//Se toman los parámetros*******************************************
		double grupoUvr = Double.parseDouble(campos.get("grupoUvr").toString());
		int codigoServicio = Utilidades.convertirAEntero(campos.get("codigoServicio").toString());
		int codigoGrupoServicio = Utilidades.convertirAEntero(campos.get("codigoGrupoServicio").toString());
		String tipoServicio = campos.get("tipoServicio").toString();
		int codigoEspecialidad = Utilidades.convertirAEntero(campos.get("codigoEspecialidad").toString());
		//*******************************************************************
		
		final String consultaInicial = "SELECT " +
			"coalesce(porcentaje||'','') as porcentaje," +
			"coalesce(valor||'','') as valor," +
			"tipo_excepcion  " +
			"from det_ex_tarifas_asocios " +
			"where " +
			"codigo_encab_xvit = "+codExTarifaAsocioViTpCC+" and " +
			grupoUvr+" between rango_inicial and rango_final ";
		String consulta = "";
		
		int nroConsulta = 1;
		
		while(Utilidades.convertirAEntero(resultados.get("numRegistros")+"", true)==0&&nroConsulta<=5)
		{
			switch(nroConsulta)
			{
				//1) FILTRO POR SERVICIO
				case 1:
					consulta = consultaInicial + " AND servicio = "+codigoServicio+" ";
				break;
				//2) FILTRO POR TIPO SERVICIO, ESPECIALIDAD Y GRUPO SERVICIO
				case 2:
					consulta = consultaInicial + " AND tipo_servicio = '"+tipoServicio+"' AND especialidad = "+codigoEspecialidad+" AND grupo_servicio = "+codigoGrupoServicio+" ";
				break;
				//3) FILTRO POR TIPO SERVICIO Y ESPECIALIDAD
				case 3:
					consulta = consultaInicial + " AND tipo_servicio = '"+tipoServicio+"' AND especialidad = "+codigoEspecialidad+" AND (grupo_servicio = "+codigoGrupoServicio+" OR grupo_servicio IS NULL) ";
				break;
				//4) FILTRO POR TIPO SERVICIO
				case 4:
					consulta = consultaInicial + " AND tipo_servicio = '"+tipoServicio+"' AND (especialidad = "+codigoEspecialidad+" or especialidad is null) AND (grupo_servicio = "+codigoGrupoServicio+" OR grupo_servicio IS NULL) ";
				break;
				//5) FILTRO POR TODOs
				case 5:
					consulta = consultaInicial + " AND (tipo_servicio = '"+tipoServicio+"' or tipo_servicio IS NULL) AND (especialidad = "+codigoEspecialidad+" or especialidad is null) AND (grupo_servicio = "+codigoGrupoServicio+" OR grupo_servicio IS NULL) ";
				break;
			}
			
			nroConsulta ++;
			resultados = obtenerDetalleExcepcionTarifaQxAsocio02(con,consulta,campos);
		}
		
		return resultados;
	}
	
	/**
	 * Método para ampliar el filtro del detalle de la excepcion tarifa quirúrgica de un asocio
	 * @param con
	 * @param consulta
	 * @param campos
	 * @return
	 */
	private static HashMap<String, Object> obtenerDetalleExcepcionTarifaQxAsocio02(Connection con, String consulta, HashMap campos) 
	{
		HashMap<String, Object> resultados = new HashMap<String, Object>();
		try
		{
			//********Se toman los parámetros********************************************
			String acronimoTipoCirugia = campos.get("acronimoTipoCirugia").toString();
			int codigoAsocio = Utilidades.convertirAEntero(campos.get("codigoAsocio").toString());
			//**************************************************************************
			
			String consultaFinal = "";
			int nroConsulta = 1;
			
			while(Utilidades.convertirAEntero(resultados.get("numRegistros")+"", true)==0&&nroConsulta<=3)
			{
				switch(nroConsulta)
				{
					//1) FILTRO POR TIPO DE CIRUGIA Y ASOCIO
					case 1:
						consultaFinal = consulta + " AND tipo_cirugia = '"+acronimoTipoCirugia+"' AND asocio = "+codigoAsocio+" "; 
					break;
					//2) FILTRO POPR TIPO DE CIRUGIA
					case 2:
						consultaFinal = consulta + " AND tipo_cirugia = '"+acronimoTipoCirugia+"' AND (asocio = "+codigoAsocio+" or asocio is null) ";
					break;
					//3) FILTRO POR TODOS
					case 3:
						consultaFinal = consulta + " AND (tipo_cirugia = '"+acronimoTipoCirugia+"' or tipo_cirugia is null) AND (asocio = "+codigoAsocio+" or asocio is null) ";
					break;
				}
				
				nroConsulta++;
				Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
				resultados = UtilidadBD.cargarValueObject(new ResultSetDecorator(st.executeQuery(consultaFinal)), true, true);
				st.close();
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerDetalleExcepcionTarifaQxAsocio02: "+e);
		}
		return resultados;
	}
	
	/**
	 * Método para realizar la consulta una excepcion tipo sala del asocio
	 * @param con
	 * @param campos
	 * @return
	 */
	public static HashMap<String, Object> obtenerExcepcionXTipoSala(Connection con,HashMap campos)
	{
		HashMap<String,Object> resultados = new HashMap<String, Object>();
		try
		{
			//*******Se toman los parámetros*****************************************
			int codigoTipoSala = Utilidades.convertirAEntero(campos.get("codigoTipoSala").toString());
			int codigoInstitucion = Utilidades.convertirAEntero(campos.get("codigoInstitucion").toString());
			int codigoEsquemaTarifario = Utilidades.convertirAEntero(campos.get("codigoEsquemaTarifario").toString());
			int tarifarioOficial = Utilidades.convertirAEntero(campos.get("tarifarioOficial").toString());
			//***********************************************************************
			
			final String consultaInicial = "SELECT codigo FROM ex_asocio_tipo_sala WHERE tipo_sala = "+codigoTipoSala+" AND institucion = "+codigoInstitucion+" ";
			String consulta = "", codExcepcionTipoSala = "";
			int nroConsulta = 1;
			
			while(Utilidades.convertirAEntero(resultados.get("numRegistros")+"", true)==0&&nroConsulta<=3)
			{
				switch(nroConsulta)
				{
					//FILTRO POR ESQUEMA TARIFARIO ESPECÍFICO
					case 1:
						consulta = consultaInicial + " AND esq_tar_particular = "+codigoEsquemaTarifario+" ";
					break;
					//FILTRO POR OPCION GENERAL
					case 2:
						if(tarifarioOficial==ConstantesBD.codigoTarifarioISS)
							consulta = consultaInicial + " AND esq_tar_general = "+ConstantesBD.codigoEsqTarifarioGeneralTodosIss+" ";
						else if(tarifarioOficial==ConstantesBD.codigoTarifarioSoat)
							consulta = consultaInicial + " AND esq_tar_general = "+ConstantesBD.codigoEsqTarifarioGeneralTodosSoat+" ";
					break;
					//FILTRO POR OPCION TODOS
					case 3:
						consulta = consultaInicial + " AND esq_tar_general = "+ConstantesBD.codigoEsqTarifarioGeneralTodos+" ";
					break;
				}
				
				nroConsulta ++;
				Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
				ResultSetDecorator rs = new ResultSetDecorator(st.executeQuery(consulta));
				
				if(rs.next())
					codExcepcionTipoSala = rs.getString("codigo");
				else
					codExcepcionTipoSala = "";
				
				st.close();
				rs.close();
				
				//Si se encontró parametrizacion por esquema tarifario se continúa la busqueda
				if(!codExcepcionTipoSala.equals(""))
					resultados = obtenerDetalleExcepcionXTipoSala(con,codExcepcionTipoSala,campos);
				else
					resultados.put("numRegistros", "0");
			}
			
			
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerExcepcionXTipoSala: "+e);
		}
		return resultados;
	}
	
	
	/**
	 * Método para realizar el filtro del detalle de la excepcion tipo sala de un asocio
	 * @param con
	 * @param codExcepcionTipoSala
	 * @param campos
	 * @return
	 */
	private static HashMap<String, Object> obtenerDetalleExcepcionXTipoSala(Connection con, String codExcepcionTipoSala, HashMap campos) 
	{
		HashMap<String, Object> resultados = new HashMap<String, Object>();
		try
		{
			//********Se toman los parámetros************************************
			int codigoAsocio = Utilidades.convertirAEntero(campos.get("codigoAsocio").toString());
			String tipoServicio = campos.get("tipoServicio").toString();
			String acronimoTipoCirugia = campos.get("acronimoTipoCirugia").toString();
			//********************************************************************
			
			final String consultaInicial = "SELECT " +
				"tipo_liquidacion,cantidad,coalesce(liquidar_sobre_tarifa,'') as liquidar_sobre_tarifa " +
				"from det_ex_asocio_tipo_sala " +
				"WHERE codigo_ex_asocio_tipo_sala = "+codExcepcionTipoSala+" AND asocio = "+codigoAsocio+" ";
			String consulta = "";
			int nroConsulta = 1;
			
			while(Utilidades.convertirAEntero(resultados.get("numRegistros")+"", true)==0&&nroConsulta<=4)
			{
				switch(nroConsulta)
				{
					//1) FILTRO POR TIPO SERVICIO Y TIPO CIRUGIA
					case 1:
						consulta = consultaInicial + " AND tipo_servicio = '"+tipoServicio+"' AND tipo_cirugia = '"+acronimoTipoCirugia+"'";
					break;
					//2) FILTRO SOLO POR TIPO DE SERVICIO
					case 2:
						consulta = consultaInicial + " AND tipo_servicio = '"+tipoServicio+"' AND tipo_cirugia IS NULL ";
					break;
					//3) FILTRO SOLO POR TIPO CIRUGIA
					case 3:
						consulta = consultaInicial + " AND tipo_servicio IS NULL AND tipo_cirugia = '"+acronimoTipoCirugia+"'";
					break;
					//4) FILTRO POR TODOs
					case 4:
						consulta = consultaInicial + " AND tipo_servicio IS NULL AND tipo_cirugia IS NULL ";
					break;
				}
				nroConsulta++;
				Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
				resultados = UtilidadBD.cargarValueObject(new ResultSetDecorator(st.executeQuery(consulta)),true,true);
				st.close();
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerDetalleExcepcionXTipoSala: "+e);
		}
		
		return resultados;
	}
	
	/**
	 * Método para consultar los porcentajes de cx multi de un asocio
	 * @param con
	 * @param campos
	 * @return
	 */
	public static HashMap<String, Object> obtenerPorcentajeCirugiaMultiple(Connection con,HashMap campos)
	{
		HashMap<String, Object> resultados = new  HashMap<String, Object>();
		try
		{
			//*****Se toman los parámetros**************************************
			int codigoConvenio = Utilidades.convertirAEntero(campos.get("codigoConvenio").toString());
			String fechaReferencia = UtilidadFecha.conversionFormatoFechaABD(campos.get("fechaReferencia").toString());
			int codigoEsquemaTarifario = Utilidades.convertirAEntero(campos.get("codigoEsquemaTarifario").toString());
			int tarifarioOficial = Utilidades.convertirAEntero(campos.get("tarifarioOficial").toString());
			int codigoInstitucion = Utilidades.convertirAEntero(campos.get("codigoInstitucion").toString());
			//******************************************************************
			
			final String consultaInicial = "SELECT codigo FROM enca_porcen_cx_multi WHERE institucion = "+codigoInstitucion+"  ";
			String consulta = "",codPorcenCxMulti = "";
			int nroConsulta = 1;
			
			while(Utilidades.convertirAEntero(resultados.get("numRegistros")+"", true)==0&nroConsulta<=4)
			{
				switch(nroConsulta)
				{
					//1) FILTRO POR CONVENIO
					case 1:
						consulta = consultaInicial + " and convenio = "+codigoConvenio+" and '"+fechaReferencia+"' between fecha_inicial and fecha_final ";
					break;
					//2) FILTRO POR UN ESQUEMA TARIFARIO 
					case 2:
						consulta = consultaInicial + " and esq_tar_particular = "+codigoEsquemaTarifario;
					break;
					//3) FILTRO POR TIPO DE ESQUEMA
					case 3:
						if(tarifarioOficial==ConstantesBD.codigoTarifarioISS)
							consulta = consultaInicial + " and esq_tar_general = "+ConstantesBD.codigoEsqTarifarioGeneralTodosIss;
						else if(tarifarioOficial==ConstantesBD.codigoTarifarioSoat)
							consulta = consultaInicial + " and esq_tar_general = "+ConstantesBD.codigoEsqTarifarioGeneralTodosSoat;
					break;
					//4) FILTRO POR TODOS LOS ESQUEMAS
					case 4:
						consulta = consultaInicial + " and esq_tar_general = "+ConstantesBD.codigoEsqTarifarioGeneralTodos;
					break;
				}
				/*if(nroConsulta==2)
					logger.info("consulta encabezado porcen cx multi=> "+consulta);*/
				nroConsulta++;
				Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
				ResultSetDecorator rs = new ResultSetDecorator(st.executeQuery(consulta));
				
				if(rs.next())
					codPorcenCxMulti = rs.getString("codigo");
				else
					codPorcenCxMulti = "";
				
				st.close();
				rs.close();
				
				//Si se encontró parametrización se prosigue a consultar el detalle
				if(!codPorcenCxMulti.equals(""))
					resultados = obtenerDetallePorcentajeCirugiaMultiple(con,codPorcenCxMulti,campos);
				else
					resultados.put("numRegistros","0");
			
			}
				
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerPorcentajeCirugiaMultiple: "+e);
		}
		return resultados;
	}
	
	/**
	 * Método para realizar el filtro de la consulta del procentaje cx multi de un asocio
	 * @param con
	 * @param codPorcenCxMulti
	 * @param campos
	 * @return
	 */
	private static HashMap<String, Object> obtenerDetallePorcentajeCirugiaMultiple(Connection con, String codPorcenCxMulti, HashMap campos) 
	{
		HashMap<String, Object> resultados = new HashMap<String, Object>();
		try
		{
			//*********Se toman los campos***************************************
			int codigoAsocio = Utilidades.convertirAEntero(campos.get("codigoAsocio").toString());
			String tipoServicio = campos.get("tipoServicio").toString();
			int codigoTipoSala = Utilidades.convertirAEntero(campos.get("codigoTipoSala").toString());
			String acronimoTipoCirugia = campos.get("acronimoTipoCirugia").toString();
			String tipoEspecialista = campos.get("tipoEspecialista").toString().toLowerCase();
			String via = campos.get("via").toString().toLowerCase();
			//*******************************************************************
			
			final String consultaInicial = "SELECT " +
				"liquidacion,adicional,politraumatismo " +
				"FROM porcentajes_cx_multi " +
				"WHERE codigo_encab = "+codPorcenCxMulti+" and tipo_asocio = "+codigoAsocio;
			String consulta = "";
			int nroConsulta = 1;
			
			while(Utilidades.convertirAEntero(resultados.get("numRegistros")+"", true)==0&&nroConsulta<=6)
			{
				switch(nroConsulta)
				{
					//1) FILTRO POR TIPO SERVICIO, TIPO SALA, TIPO CIRUGIA, TIPO ESPECIALISTA , VIA
					case 1:
						consulta = consultaInicial + " and tipo_servicio = '"+tipoServicio+"' and tipo_sala = "+codigoTipoSala+" and tipo_cirugia = '"+acronimoTipoCirugia+"' and tipo_especialista = '"+tipoEspecialista+"' and via_acceso = '"+via+"' ";
					break;
					//2) FILTRO POR TIPO SERVICIO, TIPO SALA, TIPO CIRUGIA, TIPO ESPECIALISTA
					case 2:
						consulta = consultaInicial + " and tipo_servicio = '"+tipoServicio+"' and tipo_sala = "+codigoTipoSala+" and tipo_cirugia = '"+acronimoTipoCirugia+"' and tipo_especialista = '"+tipoEspecialista+"' and (via_acceso = '"+via+"' OR via_acceso IS NULL) ";
					break;
					//3) FILTRO POR TIPO SERVICIO, TIPO SALA, TIPO CIRUGIA
					case 3:
						consulta = consultaInicial + " and tipo_servicio = '"+tipoServicio+"' and tipo_sala = "+codigoTipoSala+" and tipo_cirugia = '"+acronimoTipoCirugia+"' and (tipo_especialista = '"+tipoEspecialista+"' OR tipo_especialista is null) and (via_acceso = '"+via+"' OR via_acceso IS NULL) ";
					break;
					//4) FILTRO POR TIPO SERVICIO, TIPO SALA
					case 4:
						consulta = consultaInicial + " and tipo_servicio = '"+tipoServicio+"' and tipo_sala = "+codigoTipoSala+" and (tipo_cirugia = '"+acronimoTipoCirugia+"' OR tipo_cirugia is null) and (tipo_especialista = '"+tipoEspecialista+"' OR tipo_especialista is null) and (via_acceso = '"+via+"' OR via_acceso IS NULL) ";
					break;
					//5) FILTRO POR TIPO SERVICIO
					case 5:
						consulta = consultaInicial + " and tipo_servicio = '"+tipoServicio+"' and (tipo_sala = "+codigoTipoSala+" OR tipo_sala is null) and (tipo_cirugia = '"+acronimoTipoCirugia+"' OR tipo_cirugia is null) and (tipo_especialista = '"+tipoEspecialista+"' OR tipo_especialista is null) and (via_acceso = '"+via+"' OR via_acceso IS NULL) ";
					break;
					//6) FILTRO POR TODOS
					case 6:
						consulta = consultaInicial + " and (tipo_servicio = '"+tipoServicio+"' OR tipo_servicio IS NULL) and (tipo_sala = "+codigoTipoSala+" OR tipo_sala is null) and (tipo_cirugia = '"+acronimoTipoCirugia+"' OR tipo_cirugia is null) and (tipo_especialista = '"+tipoEspecialista+"' OR tipo_especialista is null) and (via_acceso = '"+via+"' OR via_acceso IS NULL) ";
					break;
				}
				//logger.info("consulta["+nroConsulta+"]: "+consulta);
				nroConsulta ++;
				Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
				resultados = UtilidadBD.cargarValueObject(new ResultSetDecorator(st.executeQuery(consulta)), true, true);
				st.close();
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerDetallePorcentajeCirugiaMultiple: "+e);
		}
		return resultados;
	}
	
	/**
	 * Método que consulta los valores de los asocios por uvr
	 * @param con
	 * @param campos
	 * @return
	 */
	public static HashMap<String, Object> obtenerValorAsociosXUvr(Connection con,HashMap campos)
	{
		HashMap<String, Object> resultados = new HashMap<String, Object>();
		try
		{
			//***Se toman los parámetros**********************************
			int codigoInstitucion = Utilidades.convertirAEntero(campos.get("codigoInstitucion").toString());
			int codigoConvenio = Utilidades.convertirAEntero(campos.get("codigoConvenio").toString());
			String fechaReferencia = UtilidadFecha.conversionFormatoFechaABD(campos.get("fechaReferencia").toString());
			int codigoEsquemaTarifario = Utilidades.convertirAEntero(campos.get("codigoEsquemaTarifario").toString());
			//**************************************************************
			
			final String consultaInicial = "SELECT codigo FROM asocios_x_uvr WHERE institucion = "+codigoInstitucion;
			String consulta = "", codValorAsocioXUvr = "";
			int nroConsulta = 1;
			
			while(Utilidades.convertirAEntero(resultados.get("numRegistros")+"", true)==0&&nroConsulta<=3)
			{
				switch(nroConsulta)
				{
					//1) FILTRO POR CONVENIO
					case 1:
						consulta = consultaInicial + " and convenio = "+codigoConvenio+" AND '"+fechaReferencia+"' between fecha_inicial and fecha_final ";
					break;
					//2) FILTRO POR ESQUEMA TARIFARIO ESPECÍFICO
					case 2:
						consulta = consultaInicial + " and esq_tar_particular = "+codigoEsquemaTarifario;
					break;
					//3) FILTRO POR TODOS ISS
					case 3:
						consulta = consultaInicial + " and esq_tar_general = "+ConstantesBD.codigoEsqTarifarioGeneralTodosIss;
					break;
					
				}
				
				nroConsulta++;
				Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
				logger.info("CONSULTA INICIAL ASOCIOS UVR N°"+nroConsulta+": "+consulta);
				ResultSetDecorator rs = new ResultSetDecorator(st.executeQuery(consulta));
				
				if(rs.next())
					codValorAsocioXUvr = rs.getString("codigo");
				else
					codValorAsocioXUvr = "";
				
				st.close();
				rs.close();
				
				if(!codValorAsocioXUvr.equals(""))
					resultados = obtenerDetalleValorAsociosXUvr01(con,codValorAsocioXUvr,campos);
				else
					resultados.put("numRegistros","0");
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerValorAsociosXUvr: "+e);
		}
		return resultados;
	}
	
	/**
	 * Método para hacer el filtro del detalle en la consulta de los valores de asocios x uvr
	 * @param con
	 * @param codValorAsocioXUvr
	 * @param campos
	 * @return
	 */
	private static HashMap<String, Object> obtenerDetalleValorAsociosXUvr01(Connection con, String codValorAsocioXUvr, HashMap campos) 
	{
		HashMap<String, Object> resultados = new HashMap<String, Object>();
		
		//******Se toman los parámetros***************************************
		int codigoTipoSala = Utilidades.convertirAEntero(campos.get("codigoTipoSala").toString());
		String tipoServicio = campos.get("tipoServicio").toString();
		double uvr = Double.parseDouble(campos.get("uvr").toString());
		int codigoAsocio = Utilidades.convertirAEntero(campos.get("codigoAsocio").toString());
		//*********************************************************************
		
		String consultaInicial = "SELECT "+
			"auts.tipo_asocio as asocio, " +
			"ta.nombre_asocio as nombre_asocio, " +
			"dau.codigo as codigo_parametrizacion," +
			"coalesce(dau.tipo_liquidacion,"+ConstantesBD.codigoNuncaValido+") as tipo_liquidacion, " +
			"coalesce(dau.unidades,"+ConstantesBD.codigoNuncaValido+") AS unidades," +
			"coalesce(dau.valor,"+ConstantesBD.codigoNuncaValidoDouble+") AS valor," +
			"dau.rango1 AS rango_inicial," +
			"dau.rango2 as rango_final, " +
			"coalesce(dau.ocupacion,"+ConstantesBD.codigoNuncaValido+") as codigo_ocupacion, "+
			"coalesce(dau.especialidad,"+ConstantesBD.codigoNuncaValido+") as codigo_especialidad,  "+
			"coalesce(dau.tipo_especialista,'') as tipo_especialista," +
			"auts.liquidar_por AS liquidar_por, " +
			"ta.tipos_servicio as codigo_tipo_servicio, " +
			"coalesce(getnombretiposala(auts.tipo_sala),'') as nombre_tipo_sala "+ 
			"FROM asocios_x_uvr_tipo_sala auts "+ 
			"INNER JOIN tipos_asocio ta ON(ta.codigo=auts.tipo_asocio) "+
			"INNER JOIN detalle_asocios_x_uvr dau ON(dau.cod_asocio_x_uvr_tipo_sala=auts.codigo) "+ 
			"WHERE "+ 
			"auts.cod_asocio_x_uvr = "+codValorAsocioXUvr;
		if(codigoAsocio>0)
			consultaInicial += " and auts.tipo_asocio = "+codigoAsocio+" ";
		String consulta = "";
		int nroConsulta = 1;
		
		String consultaRetorno = null;
		
		while(Utilidades.convertirAEntero(resultados.get("numRegistros")+"", true)==0&&nroConsulta<=2)
		{
			switch(nroConsulta)
			{
				//1) FILTRO POR TIPO DE SALA
				case 1:
					consulta = consultaInicial + " and auts.tipo_sala = "+codigoTipoSala;
				break;
				//2) SIN FILTRO DE TIPO SALA
				case 2:
					consulta = consultaInicial + " and auts.tipo_sala IS NULL ";
				break;
			}
			
			//Se adicionan otros filtros fijos
			consulta += " AND dau.tipo_servicio = '"+tipoServicio+"' and "+uvr+" between dau.rango1 and dau.rango2 ";
			nroConsulta++;
			
			
			//-------------------------------- Tarea Versalles [173066]. Cristhian Murillo
			try	{
				PreparedStatementDecorator psd = new PreparedStatementDecorator(con, consulta);
				ResultSetDecorator rs = new ResultSetDecorator(psd.executeQuery());
				String consultaBase = consulta;
				
				// Indica cual es el asocio que se va a buscar
				int asocioBuscar = 0;	
				// En esta lista se van agregando los asocios ya consultados en el caso de que sean varios
				ArrayList<Integer> listaAsociosConsultados = new ArrayList<Integer>();
				// Lista de consultas que arrojaron resultados
				ArrayList<String> listaConsultas = new ArrayList<String>();
				
				while(rs.next())
				{
					campos.put("codigoOcupacion"	,rs.getInt("codigo_ocupacion"));
					campos.put("codigoEspecialidad"	,rs.getInt("codigo_especialidad"));
					campos.put("tipoEspecialista"	,rs.getString("tipo_especialista"));
					asocioBuscar = rs.getInt("asocio"); 

					// Solo se consulta una vez por asocio
					if(!listaAsociosConsultados.contains(asocioBuscar))
					{
						listaAsociosConsultados.add(asocioBuscar); // Lista de verificacion de asocios consultados
						// Se hace el filtro por asocio
						consulta = consultaBase + " AND auts.tipo_asocio = '"+rs.getInt("asocio")+"'";
						// Se obtiene la consula valida segun los parametros de busqueda de asocios
						consultaRetorno = obtenerDetalleValorAsociosXUvr02Consulta(con,consulta,campos);
						
						if(!UtilidadTexto.isEmpty(consultaRetorno))
						{
							// Se carga la consulta en la lista siempre y cuando no sea nula
							listaConsultas.add(consultaRetorno);
						}
					}
				}
				
				// Variable que contiene la union de todas las consultas validas
				String consultaUnion = "";
				for (int c = 0; c < listaConsultas.size(); c++)
				{
					if(c != 0){ // Cada una de las consultas es unida con la siguiente
						consultaUnion += " UNION "+listaConsultas.get(c);
					}
					else{	 // al primer registro no se le antepone la sentencia UNION
						consultaUnion += listaConsultas.get(c);
					}
				}
				
				Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
				resultados = UtilidadBD.cargarValueObject(new ResultSetDecorator(st.executeQuery(consultaUnion)), true, true);
				
				st.close();
				psd.close();
				
				
			} 
			catch (Exception e)
			{
				Log4JManager.error("Error asignando la ocupacion y especialidad de la consulat de detalle_asocios_x_uvr",e);
			}
			//-------------------------------------------------------------------------------------------------------------
			
			// Asi estaba antes de la Tarea Versalles [173066].
			//resultados = obtenerDetalleValorAsociosXUvr02(con,consulta,campos);
			
		}
	return resultados;
	}
	
	

	/**
	 * Método implementado para realizar el filtro del detalle en la consulta de los asocios x uvr 
	 * retornando la consulta valida
	 * @param con
	 * @param consulta
	 * @param campos
	 * @return
	 */
	private static String obtenerDetalleValorAsociosXUvr02Consulta(Connection con, String consulta, HashMap campos) 
	{
		HashMap<String, Object> resultados = new HashMap<String, Object>();
		String consultaFinal = "";
		try
		{
			//*Se toman los parámetros ******************************************
			int codigoTipoAnestesia = Utilidades.convertirAEntero(campos.get("codigoTipoAnestesia").toString());
			int codigoOcupacion = Utilidades.convertirAEntero(campos.get("codigoOcupacion").toString());
			int codigoEspecialidad = Utilidades.convertirAEntero(campos.get("codigoEspecialidad").toString());
			String tipoEspecialista = campos.get("tipoEspecialista").toString();
			//********************************************************************
			
			final String consultaInicial = consulta;
			int nroConsulta = 1;
			
			while(Utilidades.convertirAEntero(resultados.get("numRegistros")+"", true)==0&&nroConsulta<=5)
			{
				switch(nroConsulta)
				{
					//1) FILTRO POR TIPO ANESTESIA, OCUPACION, ESPECIALIDAD, TIPO ESPECIALISTA
					case 1:
						consultaFinal = consultaInicial + " AND dau.tipo_anestesia = "+codigoTipoAnestesia+" AND dau.ocupacion = "+codigoOcupacion+" AND dau.especialidad = "+codigoEspecialidad+" AND dau.tipo_especialista IN ('"+tipoEspecialista+"')";
					break;
					//2) FILTRO POR TIPO ANESTESIA, OCUPACION, ESPECIALIDAD
					case 2:
						consultaFinal = consultaInicial + " AND (dau.tipo_anestesia = "+codigoTipoAnestesia+" or dau.tipo_anestesia is null) AND dau.ocupacion = "+codigoOcupacion+" AND dau.especialidad = "+codigoEspecialidad+" AND (dau.tipo_especialista IN ('"+tipoEspecialista+"') OR dau.tipo_especialista IS NULL)";
					break;
					//3) FILTRO POR TIPO ANESTESIA, OCUPACION
					case 3:
						consultaFinal = consultaInicial + " AND (dau.tipo_anestesia = "+codigoTipoAnestesia+" or dau.tipo_anestesia is null) AND dau.ocupacion = "+codigoOcupacion+" AND (dau.especialidad = "+codigoEspecialidad+" OR (dau.especialidad is null OR dau.especialidad = 0)) AND (dau.tipo_especialista IN ('"+tipoEspecialista+"') OR dau.tipo_especialista IS NULL)";
					break;
					//4) FILTRO POR TIPO ANESTESIA
					case 4:
						consultaFinal = consultaInicial + " AND (dau.tipo_anestesia = "+codigoTipoAnestesia+" or dau.tipo_anestesia is null) AND (dau.ocupacion = "+codigoOcupacion+" OR (dau.ocupacion is null OR dau.ocupacion = 0)) AND (dau.especialidad = "+codigoEspecialidad+" OR (dau.especialidad is null OR dau.especialidad = 0)) AND (dau.tipo_especialista IN ('"+tipoEspecialista+"') OR dau.tipo_especialista IS NULL)";
					break;
					//5) SIN FILTRO
					case 5:
						consultaFinal = consultaInicial + " AND (dau.tipo_anestesia = "+codigoTipoAnestesia+" or dau.tipo_anestesia is null) AND (dau.ocupacion = "+codigoOcupacion+" OR (dau.ocupacion is null OR dau.ocupacion = 0)) AND (dau.especialidad = "+codigoEspecialidad+" OR (dau.especialidad is null OR dau.especialidad = 0 )) AND (dau.tipo_especialista IN ('"+tipoEspecialista+"') OR dau.tipo_especialista IS NULL)";
						//consultaFinal = consultaInicial + " AND 1=1";
					break;
				}
				logger.info("CONSULTA VALOR UVR ["+nroConsulta+"] "+consultaFinal);
				nroConsulta++;
				Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
				resultados = UtilidadBD.cargarValueObject(new ResultSetDecorator(st.executeQuery(consultaFinal)), true, true);
				st.close();
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerDetalleValorAsociosXUvr02: "+e);
		}
		
		return consultaFinal;
	}
	
	
	
	/**
	 * Método implementado para realizar el filtro del detalle en la consulta de los asocios x uvr
	 * @param con
	 * @param consulta
	 * @param campos
	 * @return
	 */
	private static HashMap<String, Object> obtenerDetalleValorAsociosXUvr02(Connection con, String consulta, HashMap campos) 
	{
		HashMap<String, Object> resultados = new HashMap<String, Object>();
		try
		{
			//*Se toman los parámetros ******************************************
			int codigoTipoAnestesia = Utilidades.convertirAEntero(campos.get("codigoTipoAnestesia").toString());
			int codigoOcupacion = Utilidades.convertirAEntero(campos.get("codigoOcupacion").toString());
			int codigoEspecialidad = Utilidades.convertirAEntero(campos.get("codigoEspecialidad").toString());
			String tipoEspecialista = campos.get("tipoEspecialista").toString();
			//********************************************************************
			
			final String consultaInicial = consulta;
			String consultaFinal = "";
			int nroConsulta = 1;
			
			while(Utilidades.convertirAEntero(resultados.get("numRegistros")+"", true)==0&&nroConsulta<=5)
			{
				switch(nroConsulta)
				{
				//1) FILTRO POR TIPO ANESTESIA, OCUPACION, ESPECIALIDAD, TIPO ESPECIALISTA
				case 1:
					consultaFinal = consultaInicial + " AND dau.tipo_anestesia = "+codigoTipoAnestesia+" AND dau.ocupacion = "+codigoOcupacion+" AND dau.especialidad = "+codigoEspecialidad+" AND dau.tipo_especialista IN ('"+tipoEspecialista+"')";
				break;
				//2) FILTRO POR TIPO ANESTESIA, OCUPACION, ESPECIALIDAD
				case 2:
					consultaFinal = consultaInicial + " AND (dau.tipo_anestesia = "+codigoTipoAnestesia+" or dau.tipo_anestesia is null) AND dau.ocupacion = "+codigoOcupacion+" AND dau.especialidad = "+codigoEspecialidad+" AND (dau.tipo_especialista IN ('"+tipoEspecialista+"') OR dau.tipo_especialista IS NULL)";
				break;
				//3) FILTRO POR TIPO ANESTESIA, OCUPACION
				case 3:
					consultaFinal = consultaInicial + " AND (dau.tipo_anestesia = "+codigoTipoAnestesia+" or dau.tipo_anestesia is null) AND dau.ocupacion = "+codigoOcupacion+" AND (dau.especialidad = "+codigoEspecialidad+" OR (dau.especialidad is null OR dau.especialidad = 0)) AND (dau.tipo_especialista IN ('"+tipoEspecialista+"') OR dau.tipo_especialista IS NULL)";
				break;
				//4) FILTRO POR TIPO ANESTESIA
				case 4:
					consultaFinal = consultaInicial + " AND (dau.tipo_anestesia = "+codigoTipoAnestesia+" or dau.tipo_anestesia is null) AND (dau.ocupacion = "+codigoOcupacion+" OR (dau.ocupacion is null OR dau.ocupacion = 0)) AND (dau.especialidad = "+codigoEspecialidad+" OR (dau.especialidad is null OR dau.especialidad = 0)) AND (dau.tipo_especialista IN ('"+tipoEspecialista+"') OR dau.tipo_especialista IS NULL)";
				break;
				//5) SIN FILTRO
				case 5:
					consultaFinal = consultaInicial + " AND (dau.tipo_anestesia = "+codigoTipoAnestesia+" or dau.tipo_anestesia is null) AND (dau.ocupacion = "+codigoOcupacion+" OR (dau.ocupacion is null OR dau.ocupacion = 0)) AND (dau.especialidad = "+codigoEspecialidad+" OR (dau.especialidad is null OR dau.especialidad = 0 )) AND (dau.tipo_especialista IN ('"+tipoEspecialista+"') OR dau.tipo_especialista IS NULL)";
				break;
			}
				logger.info("CONSULTA VALOR UVR ["+nroConsulta+"] "+consultaFinal);
				nroConsulta++;
				Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
				resultados = UtilidadBD.cargarValueObject(new ResultSetDecorator(st.executeQuery(consultaFinal)), true, true);
				st.close();
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerDetalleValorAsociosXUvr02: ",e);
		}
		return resultados;
	}
	
	
	/**
	 * Método que realiza la consulta de los asocios por rango de tiempo
	 * @param con
	 * @param campos
	 * @return
	 */
	public static HashMap<String, Object> obtenerValorAsociosXRangoTiempo(Connection con,HashMap campos)
	{
		HashMap<String, Object> resultados = new HashMap<String, Object>();
		try
		{
			//****Se toman los parámetros********************************
			int codigoConvenio = Utilidades.convertirAEntero(campos.get("codigoConvenio").toString());
			String fechaReferencia = UtilidadFecha.conversionFormatoFechaABD(campos.get("fechaReferencia").toString());
			int codigoEsquemaTarifario = Utilidades.convertirAEntero(campos.get("codigoEsquemaTarifario").toString());
			int tarifarioOficial = Utilidades.convertirAEntero(campos.get("tarifarioOficial").toString());
			int codigoInstitucion = Utilidades.convertirAEntero(campos.get("codigoInstitucion").toString());
			//************************************************************
			
			final String consultaInicial = "SELECT consecutivo FROM cabeza_asoc_x_ran_tiem WHERE institucion = "+codigoInstitucion;
			String consulta = "",codAsocioXRangoTiempo = "";
			int nroConsulta = 1;
			
			while(Utilidades.convertirAEntero(resultados.get("numRegistros")+"", true)==0&&nroConsulta<=4)
			{
				switch(nroConsulta)
				{
					//1) FILTRO POR CONVENIO
					case 1:
						consulta = consultaInicial + " AND convenio = "+codigoConvenio+" AND '"+fechaReferencia+"' between fecha_inicial and fecha_final";
					break;
					//2) FILTRO POR ESQUEMA TARIFARIO PARTICULAR
					case 2:
						consulta = consultaInicial + " AND esquema_tarifario = "+codigoEsquemaTarifario;
					break;
					//3) FILTRO POR ESQUEMA TARIFARIO GENERAL SEGUN TARIFARIO
					case 3:
						if(tarifarioOficial==ConstantesBD.codigoTarifarioISS)
							consulta = consultaInicial + " AND esquema_tarifario_general = "+ConstantesBD.codigoEsqTarifarioGeneralTodosIss;
						else if(tarifarioOficial==ConstantesBD.codigoTarifarioSoat)
							consulta = consultaInicial + " AND esquema_tarifario_general = "+ConstantesBD.codigoEsqTarifarioGeneralTodosSoat;
					break;
					//4) FILTRO POR TODOS LOS ESQUEMAS TARIFARIOS
					case 4:
						consulta = consultaInicial + " AND esquema_tarifario_general = "+ConstantesBD.codigoEsqTarifarioGeneralTodos;
					break;
				}
				
				nroConsulta++;
				Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
				ResultSetDecorator rs = new ResultSetDecorator(st.executeQuery(consulta));
				
				if(rs.next())
					codAsocioXRangoTiempo = rs.getString("consecutivo");
				else
					codAsocioXRangoTiempo = "";
				
				st.close();
				rs.close();
				
				//Si se encontró parametrización se prosigue a consultar el detalle
				if(!codAsocioXRangoTiempo.equals(""))
					resultados = obtenerDetalleValorAsociosXRangoTiempo01(con,codAsocioXRangoTiempo,campos);
				else
					resultados.put("numRegistros","0");
				
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerValorAsociosXRangoTiempo: "+e);
		}
		return resultados;
	}
	
	/**
	 * Método que realiza los filtrso de la consulta del detalle del valor de los asocios por rango de tiempo
	 * @param con
	 * @param codAsocioXRangoTiempo
	 * @param campos
	 * @return
	 */
	private static HashMap<String, Object> obtenerDetalleValorAsociosXRangoTiempo01(Connection con, String codAsocioXRangoTiempo, HashMap campos) 
	{
		HashMap<String, Object> resultados = new HashMap<String, Object>();
		//****Se toman los parámetros******************************************
		String tipoTiempoBase = campos.get("tipoTiempoBase").toString();
		int duracion = Utilidades.convertirAEntero(campos.get("duracion").toString());
		int codigoAsocio = Utilidades.convertirAEntero(campos.get("codigoAsocio").toString());
		int codigoServicio = Utilidades.convertirAEntero(campos.get("codigoServicio").toString());
		String tipoServicio = campos.get("tipoServicio").toString();
		//*********************************************************************
		
		
		String consultaInicial = "SELECT " +
			"axrt.codigo as codigo_parametrizacion," +
			"axrt.asocio, " +
			"ta.nombre_asocio AS nombre_asocio, " +
			"axrt.minutos_rango_inicial," +
			"axrt.minutos_rango_final," +
			"coalesce(axrt.min_frac_adicional,0) AS min_frac_adicional," +
			"coalesce(axrt.valor_frac_adicional,0) as valor_frac_adicional, " +
			"axrt.valor_asocio As valor, " +
			"axrt.liquidarpor AS liquidar_por, " +
			"ta.tipos_servicio as codigo_tipo_servicio " +
			"FROM asocios_x_rango_tiempo axrt " +
			"INNER JOIN tipos_asocio ta ON(ta.codigo=axrt.asocio) " +
			"WHERE axrt.codigo_encab = "+codAsocioXRangoTiempo+" AND axrt.tipo_tiempo_base = '"+tipoTiempoBase+"' AND ("+duracion+" between axrt.minutos_rango_inicial and (axrt.minutos_rango_final + coalesce(axrt.min_frac_adicional,0))) ";
		
		//Si la busqueda es por un asocio específico se postula
		if(codigoAsocio>0)
			consultaInicial += " AND axrt.asocio = "+codigoAsocio;
		
		String consulta = "";
		int nroConsulta = 1;
		
		while(Utilidades.convertirAEntero(resultados.get("numRegistros")+"", true)==0&&nroConsulta<=3)
		{
			switch(nroConsulta)
			{
				//1) FILTRO POR SERVICIO
				case 1:
					consulta = consultaInicial + " and axrt.servicio = "+codigoServicio;
				break;
				//2) FILTRO POR TIPO SERVICIO
				case 2:
					consulta = consultaInicial + " and axrt.tipo_servicio = '"+tipoServicio+"' ";
				break;
				//3) SIN FILTRO DE SERVICIO
				case 3:
					consulta = consultaInicial + " and axrt.servicio IS NULL and axrt.tipo_servicio IS NULL ";
				break;
			}
			
			nroConsulta ++;
			resultados = obtenerDetalleValorAsociosXRangoTiempo02(con,consulta,campos);
		}
		
		//*******************CONSULTA DEL RANGO FINAL EN CASO DE NO ENCONTRAR PARAMETRIZACION*************************************
		if(Utilidades.convertirAEntero(resultados.get("numRegistros").toString())<=0)
		{
			int rangoFinal = 0;
			/**
			 * Si no se encontraron resultados debido a que no había ninguna parametrización para la duración de la cirugia/sala
			 * entonces se toman los asocios del ultimo rango, siempre y cuando tenga fracción adicional
			 */
			consultaInicial =  "SELECT " +
				"coalesce(max(axrt.minutos_rango_final),0) as rango_final " +
				"FROM asocios_x_rango_tiempo axrt " +
				"WHERE axrt.codigo_encab = "+codAsocioXRangoTiempo+" AND axrt.tipo_tiempo_base = '"+tipoTiempoBase+"' AND axrt.min_frac_adicional is not null and axrt.min_frac_adicional > 0 ";
			
			//Si la busqueda es por un asocio específico se postula
			if(codigoAsocio>0)
				consultaInicial += " AND axrt.asocio = "+codigoAsocio;
			
			consulta = "";
			nroConsulta = 1;
			while(Utilidades.convertirAEntero(resultados.get("rangoFinal_0")+"")<=0&&nroConsulta<=3)
			{
				switch(nroConsulta)
				{
					//1) FILTRO POR SERVICIO
					case 1:
						consulta = consultaInicial + " and axrt.servicio = "+codigoServicio;
					break;
					//2) FILTRO POR TIPO SERVICIO
					case 2:
						consulta = consultaInicial + " and axrt.tipo_servicio = '"+tipoServicio+"' ";
					break;
					//3) SIN FILTRO DE SERVICIO
					case 3:
						consulta = consultaInicial;
					break;
				}
				
				nroConsulta ++;
				resultados = obtenerDetalleValorAsociosXRangoTiempo02(con,consulta,campos);
			}
			
			logger.info("VALOR DEL RANGO FINAL ENCONTRADO=> "+resultados.get("rangoFinal_0"));
			//Si se encontraron resultados entonces se consulta los registros encontrasos en ese rango final
			if(Utilidades.convertirAEntero(resultados.get("rangoFinal_0").toString())>0)
			{
				/**
				 * Como se encontró rango final con minutos de fraccion adicional parametrizados entonces se toman los registros
				 */
				rangoFinal = Utilidades.convertirAEntero(resultados.get("rangoFinal_0").toString());
				resultados = new HashMap<String, Object>(); //se reinicia el mapa de resultados
				consultaInicial =  "SELECT " +
					"axrt.codigo as codigo_parametrizacion," +
					"axrt.asocio, " +
					"ta.nombre_asocio AS nombre_asocio, " +
					"axrt.minutos_rango_inicial," +
					"axrt.minutos_rango_final," +
					"coalesce(axrt.min_frac_adicional,0) AS min_frac_adicional," +
					"coalesce(axrt.valor_frac_adicional,0) as valor_frac_adicional, " +
					"axrt.valor_asocio As valor, " +
					"axrt.liquidarpor AS liquidar_por, " +
					"ta.tipos_servicio as codigo_tipo_servicio " +
					"FROM asocios_x_rango_tiempo axrt " +
					"INNER JOIN tipos_asocio ta ON(ta.codigo=axrt.asocio) " +
					"WHERE axrt.codigo_encab = "+codAsocioXRangoTiempo+" AND axrt.tipo_tiempo_base = '"+tipoTiempoBase+"' AND axrt.minutos_rango_final = "+rangoFinal+" AND axrt.min_frac_adicional is not null and axrt.min_frac_adicional > 0 ";
				
				//Si la busqueda es por un asocio específico se postula
				if(codigoAsocio>0)
					consultaInicial += " AND axrt.asocio = "+codigoAsocio;
				
				consulta = "";
				nroConsulta = 1;
				while(Utilidades.convertirAEntero(resultados.get("numRegistros")+"", true)==0&&nroConsulta<=3)
				{
					switch(nroConsulta)
					{
						//1) FILTRO POR SERVICIO
						case 1:
							consulta = consultaInicial + " and axrt.servicio = "+codigoServicio;
						break;
						//2) FILTRO POR TIPO SERVICIO
						case 2:
							consulta = consultaInicial + " and axrt.tipo_servicio = '"+tipoServicio+"' ";
						break;
						//3) SIN FILTRO DE SERVICIO
						case 3:
							consulta = consultaInicial;
						break;
					}
					
					nroConsulta ++;
					resultados = obtenerDetalleValorAsociosXRangoTiempo02(con,consulta,campos);
				}
			}
			else
				//Si no hubo rango final no hay registros
				resultados.put("numRegistros", "0");
			
		}
		//***************************************************************************************************************
		
		return resultados;
	}
	
	/**
	 * Método implementado para realizar los filtros del detalle del valor de asociso x rango de tiempos
	 * @param con
	 * @param consultaInicial
	 * @param campos
	 * @return
	 */
	private static HashMap<String, Object> obtenerDetalleValorAsociosXRangoTiempo02(Connection con, String consultaInicial, HashMap campos) 
	{
		HashMap<String, Object> resultados = new HashMap<String, Object>();
		try
		{
			//******Se toman los parámetros****************
			String acronimoTipoCirugia = campos.get("acronimoTipoCirugia").toString();
			int codigoTipoAnestesia = Utilidades.convertirAEntero(campos.get("codigoTipoAnestesia").toString());
			//**************************************************************************
			
			String consulta = "";
			int nroConsulta = 1;
			
			while(Utilidades.convertirAEntero(resultados.get("numRegistros")+"", true)==0&&nroConsulta<=4)
			{
				switch(nroConsulta)
				{
					//1) FILTRO POR TIPO CIRUGIA Y TIPO ANESTESIA
					case 1:
						consulta = consultaInicial + " and axrt.tipo_cirugia = '"+acronimoTipoCirugia+"' AND axrt.tipo_anestesia = "+codigoTipoAnestesia+" ";
					break;
					//2) FILTRO POR SOLO TIPO CIRUGIA
					case 2:
						consulta = consultaInicial + " and axrt.tipo_cirugia = '"+acronimoTipoCirugia+"' AND axrt.tipo_anestesia IS NULL ";
					break;
					//3) FILTRO POT SOLO TIPO ANESTESIA
					case 3:
						consulta = consultaInicial + " and axrt.tipo_cirugia IS NULL AND axrt.tipo_anestesia = "+codigoTipoAnestesia+" ";
					break;
					//4) SIN FILTRO
					case 4:
						consulta = consultaInicial + " and axrt.tipo_cirugia IS NULL AND axrt.tipo_anestesia IS NULL ";
					break;
				}
				
				//logger.info("consulta["+nroConsulta+"]: "+consulta+"\n\n");
				nroConsulta++;
				
				Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
				resultados = UtilidadBD.cargarValueObject(new ResultSetDecorator(st.executeQuery(consulta)), true, true);
				
				st.close();
				
				
				/**
				 * Esta validación solo aplica para la consulta del máximo rango final, por eso se verifica
				 * si el mapa tiene la llave rangoFinal
				 */
				if(resultados.containsKey("rangoFinal_0")&&Utilidades.convertirAEntero(resultados.get("rangoFinal_0").toString())<=0)
					resultados.put("numRegistros", "0");
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerDetalleValorAsociosXRangoTiempo02: "+e);
		}
		return resultados;
	}
	
	
	/**
	 * Método que realiza la consulta de lso asocios del servicio x tarifa
	 * @param con
	 * @param campos
	 * @return
	 */
	public static HashMap<String, Object> obtenerAsociosServicioXTarifa(Connection con,HashMap campos)
	{
		HashMap<String, Object> resultados = new HashMap<String, Object>();
		try
		{
			//************Se toman los parámetros *******************************************
			int codigoInstitucion = Utilidades.convertirAEntero(campos.get("codigoInstitucion").toString());
			int codigoConvenio = Utilidades.convertirAEntero(campos.get("codigoConvenio").toString());
			String fechaReferencia = UtilidadFecha.conversionFormatoFechaABD(campos.get("fechaReferencia").toString());
			int codigoEsquemaTarifario = Utilidades.convertirAEntero(campos.get("codigoEsquemaTarifario").toString());
			int tarifarioOficial = Utilidades.convertirAEntero(campos.get("tarifarioOficial").toString());
			//********************************************************************************
			
			final String consultaInicial = "SELECT codigo FROM asocios_servicios_tarifa WHERE institucion = "+codigoInstitucion+" ";
			String consulta = "",codAsociosServicioXTarifa = "";
			int nroConsulta = 1;
			
			while(Utilidades.convertirAEntero(resultados.get("numRegistros")+"", true)==0&&nroConsulta<=4)
			{
				switch(nroConsulta)
				{
					//1) FILTRO POR CONVENIO Y VIGENCIA
					case 1:
						consulta = consultaInicial + " AND convenio = "+codigoConvenio+" and '"+fechaReferencia+"' between fecha_inicial and fecha_final ";
					break;
					//2) FILTRO POR ESQUEMA TARIFARIO ESPECÍFICO
					case 2:
						consulta = consultaInicial + " AND esq_tar_particular = "+codigoEsquemaTarifario+" ";
					break;
					//3) FILTRO POR ESQUEMA TARIFARIO GENERAL
					case 3:
						if(tarifarioOficial==ConstantesBD.codigoTarifarioISS)
							consulta = consultaInicial + " AND esq_tar_general = "+ConstantesBD.codigoEsqTarifarioGeneralTodosIss+" ";
						else if(tarifarioOficial==ConstantesBD.codigoTarifarioSoat)
							consulta = consultaInicial + " AND esq_tar_general = "+ConstantesBD.codigoEsqTarifarioGeneralTodosSoat+" ";
					break;
					//4) FILTRO POR TODOS LOS ESQUEMAS
					case 4:
						consulta = consultaInicial + " AND esq_tar_general = "+ConstantesBD.codigoEsqTarifarioGeneralTodos+" ";
					break;
				}
				
				nroConsulta++;
				Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
				ResultSetDecorator rs = new ResultSetDecorator(st.executeQuery(consulta));
				
				if(rs.next())
					codAsociosServicioXTarifa = rs.getString("codigo");
				else
					codAsociosServicioXTarifa = "";
				
				st.close();
				rs.close();
				
				if(!codAsociosServicioXTarifa.equals(""))
					resultados = obtenerDetalleAsociosServicioXTarifa(con,codAsociosServicioXTarifa,campos);
				
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerAsociosServicioXTarifa: "+e);
		}
		return resultados;
	}
	
	/**
	 * Método que realiza el filtro de la consulta del detalle de los asocios del servicio x tarifa
	 * @param con
	 * @param codAsociosServicioXTarifa
	 * @param campos
	 * @return
	 */
	private static HashMap<String, Object> obtenerDetalleAsociosServicioXTarifa(Connection con, String codAsociosServicioXTarifa, HashMap campos) 
	{
		HashMap<String, Object> resultados = new HashMap<String, Object>();
		try
		{
			//*****Se toman los parámetros*********************************
			int codigoServicio = Utilidades.convertirAEntero(campos.get("codigoServicio").toString());
			String tipoServicio = campos.get("tipoServicio").toString();
			int codigoEspecialidad = Utilidades.convertirAEntero(campos.get("codigoEspecialidad").toString());
			int codigoGrupoServicio = Utilidades.convertirAEntero(campos.get("codigoGrupoServicio").toString());
			//**************************************************************
			
			final String consultaInicial = "SELECT " +
				"da.asocio AS codigo_asocio," +
				"ta.nombre_asocio AS nombre_asocio, " +
				"da.liquidar_por AS liquidar_por, " +
				"ta.tipos_servicio as codigo_tipo_servicio " +
				"FROM det_aso_servicios_tarifa da " +
				"INNER JOIN tipos_asocio ta ON(ta.codigo=da.asocio) " +
				"WHERE da.codigo_aso_serv_tarifa = "+codAsociosServicioXTarifa;
			String consulta = "";
			int nroConsulta = 1;
			
			while(Utilidades.convertirAEntero(resultados.get("numRegistros")+"", true)==0&&nroConsulta<=5)
			{
				switch(nroConsulta)
				{
					//1) FILTRO POR SERVICIO
					case 1:
						consulta = consultaInicial + " AND da.servicio = "+codigoServicio+" ";
					break;
					//2) FILTRO POR TIPO SERVICIO, ESPECIALIDAD Y GRUPO SERVICIO
					case 2:
						consulta = consultaInicial + " AND da.tipo_servicio = '"+tipoServicio+"' AND da.especialidad = "+codigoEspecialidad+" and da.grupo_servicio = "+codigoGrupoServicio+" ";
					break;
					//3) FILTRO POR TIPO SERVICIO, ESPECIALIDAD
					case 3:
						consulta = consultaInicial + " AND da.tipo_servicio = '"+tipoServicio+"' AND da.especialidad = "+codigoEspecialidad+" and (da.grupo_servicio = "+codigoGrupoServicio+"  or da.grupo_servicio IS NULL)";
					break;
					//4) FILTRO POR TIPO SERVICIO
					case 4:
						consulta = consultaInicial + " AND da.tipo_servicio = '"+tipoServicio+"' AND (da.especialidad = "+codigoEspecialidad+" OR da.especialidad IS NULL) and (da.grupo_servicio = "+codigoGrupoServicio+"  or da.grupo_servicio IS NULL)";
					break;
					//5) FILTRO POR TODOs
					case 5:
						consulta = consultaInicial + " AND (da.tipo_servicio = '"+tipoServicio+"' OR da.tipo_servicio IS NULL) AND (da.especialidad = "+codigoEspecialidad+" OR da.especialidad IS NULL) and (da.grupo_servicio = "+codigoGrupoServicio+"  or da.grupo_servicio IS NULL)";
					break;
				}
				nroConsulta++;
				Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
				resultados = UtilidadBD.cargarValueObject(new ResultSetDecorator(st.executeQuery(consulta)), true, true);
				
				st.close();
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerDetalleAsociosServicioXTarifa: "+e);
		}
		return resultados;
	}
	
	
	/**
	 * Método implementado para obtener el listado de articulos del consumo de materiales
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public static HashMap<String, Object> obtenerListaArticulosConsumoMateriales(Connection con,String numeroSolicitud)
	{
		HashMap<String, Object> resultados = new HashMap<String, Object>();
		try
		{
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(obtenerListaArticulosConsumoMateriales_Str, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			pst.setInt(1,Utilidades.convertirAEntero(numeroSolicitud));
			
			resultados = UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()), true, true);
			
			pst.close();
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerListaArticulosConsumoMateriales: "+e);
		}
		return resultados;
	}
	
	/**
	 * Método que realiza la actualización de incluido y valor unitario de un articulo del consumo de materiales
	 * @param con
	 * @param numeroSolicitud
	 * @param codigoArticulo
	 * @param incluido
	 * @param valorUnitario
	 * @return
	 */
	public static int actualizarInclusionYTarifaArticulo(Connection con,String numeroSolicitud,int codigoArticulo,String incluido,double valorUnitario)
	{
		int respuesta = 0;
		try
		{
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(actualizarInclusionYTarifaArticulo_Str, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			
			/**
			 *  UPDATE det_fin_materiales_qx SET incluido = ?, valor_unitario = ? WHERE numero_solicitud = ? AND articulo = ?
			 */
			if(!incluido.equals(""))
				pst.setString(1,incluido);
			else
				pst.setNull(1,Types.VARCHAR);
			logger.info("Se actualiza el valor unitario y la inclusion del articulo ["+codigoArticulo+"] y solicitud ["+numeroSolicitud+"] ");
			logger.info("El valor a actualizar es "+valorUnitario+", incluido/excluido "+incluido);
			pst.setDouble(2, valorUnitario);
			pst.setInt(3,Utilidades.convertirAEntero(numeroSolicitud));
			pst.setInt(4,codigoArticulo);
			
			respuesta = pst.executeUpdate();
			
			pst.close();
		}
		catch(SQLException e)
		{
			logger.error("Error en actualizarInclusionYTarifaArticulo: "+e);
		}
		return respuesta;
	}
	
	/**
	 * Método para obtener el total del consumo de materiales
	 * @param con
	 * @param numeroSolicitud
	 * @param codigoServicio
	 * @param esTarifa
	 * @return
	 */
	public static double obtenerTotalConsumoMateriales(Connection con,String numeroSolicitud,int codigoServicio,boolean esTarifa)
	{
		double total = 0;
		try
		{
			String parametro = "costo_unitario";
			if(esTarifa)
				parametro = "valor_unitario";
			
			String consulta = "SELECT " +
				"coalesce(sum(cantidad_consumo_total*"+parametro+"),0) as total " +
				"FROM det_fin_materiales_qx WHERE numero_solicitud = "+numeroSolicitud;
			
			if(codigoServicio>0)
				consulta += " and servicio = "+codigoServicio;
			
			consulta += " and (incluido = '"+ConstantesBD.acronimoNo+"' or incluido IS NULL)";
			
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			ResultSetDecorator rs = new ResultSetDecorator(st.executeQuery(consulta));
			
			if(rs.next())
				total = rs.getDouble("total");
			
			st.close();
			rs.close();
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerTotalConsumoMateriales: "+e);
		}
		return total;
	}
	
	/**
	 * Método implementado para verificar que existe un consumo de materiales
	 * @param con
	 * @param numeroSolicitud
	 * @param codigoServicio
	 * @return
	 */
	public static boolean existeConsumoMateriales(Connection con,String numeroSolicitud,int codigoServicio)
	{
		boolean existe = false;
		try
		{
			String consulta  = "SELECT count(1) As cuenta FROM det_fin_materiales_qx WHERE numero_solicitud = "+numeroSolicitud;
			
			if(codigoServicio>0)
				consulta+=" AND servicio = "+codigoServicio;
			
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			ResultSetDecorator rs = new ResultSetDecorator(st.executeQuery(consulta));
			
			if(rs.next())
				if(rs.getInt("cuenta")>0)
					existe = true;
			
			st.close();
			rs.close();
		}
		catch(SQLException e)
		{
			logger.error("Error en existeConsumoMateriales: "+e);
		}
		return existe;
	}
	
	/**
	 * Método implementado para obtener el codigo propietario de un asocio
	 * @param con
	 * @param codigoParametrizacion
	 * @param tarifarioOficial
	 * @param modoLiquidacion
	 * @return
	 */
	public static String obtenerCodigoPropietarioAsocio(Connection con,String codigoParametrizacion,int tarifarioOficial,int modoLiquidacion)
	{
		String codigoPropietario = "";
		try
		{
			String consulta = "";
			
			//Segun modo de liquidacion se toma la consulta específica
			switch(modoLiquidacion)
			{
				case ConstantesBDSalas.codigoLiquidacionSoat:
					consulta = consultaCodigoPropietarioAsocioXSoat_Str;
				break;
				case ConstantesBDSalas.codigoLiquidacionIss:
					consulta = consultaCodigoPropietarioAsocioXIss_Str;
				break;
				case ConstantesBDSalas.codigoLiquidacionRangoTiempos:
					consulta = consultaCodigoPropietarioAsocioXRangoTiempo_Str;
				break;
				
			}
			
			//Si se alcanzó a editar una consulta se ejecuta
			if(!consulta.equals(""))
			{
				PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
				pst.setInt(1,Utilidades.convertirAEntero(codigoParametrizacion));
				pst.setInt(2,tarifarioOficial);
				
				ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
				
				if(rs.next())
					codigoPropietario = rs.getString("codigo_propietario");
				
				pst.close();
				rs.close();
			}
			
			//si se busco por el tarifario parametrizado y no encontro información, debe buscar por codigoCUPS que es el aprobado, solictado por nury el 31 de enero de 2010.
			if(tarifarioOficial!=ConstantesBD.codigoTarifarioCups&&UtilidadTexto.isEmpty(codigoPropietario))
			{
				return obtenerCodigoPropietarioAsocio(con, codigoParametrizacion, ConstantesBD.codigoTarifarioCups, modoLiquidacion);
			}
			
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerCodigoPropietarioAsocio: "+e);
		}
		return codigoPropietario;
	}
	
	/**
	 * Método implementado para insertar un asocio
	 * @param con
	 * @param campos
	 * @return
	 */
	public static int insertarAsocio(Connection con,HashMap campos)
	{
		int resultado = ConstantesBD.codigoNuncaValido;
		try
		{
			//***************SE TOMAN LOS PARÁMETROS*******************************
			int consecutivo = ConstantesBD.codigoNuncaValido;
			String consecutivoServicio = campos.get("consecutivoServicio").toString();
			int codigoEspecialidad = Utilidades.convertirAEntero(campos.get("codigoEspecialidad").toString());
			int codigoInstitucion = Utilidades.convertirAEntero(campos.get("codigoInstitucion").toString());
			int codigoProfesional = Utilidades.convertirAEntero(campos.get("codigoProfesional").toString());
			int codigoServicio = Utilidades.convertirAEntero(campos.get("codigoServicio").toString());
			int codigoPool = Utilidades.convertirAEntero(campos.get("codigoPool").toString());
			int codigoAsocio = Utilidades.convertirAEntero(campos.get("codigoAsocio").toString());
			String codigoPropietario = campos.get("codigoPropietario").toString();
			double valorAsocio = Double.parseDouble(campos.get("valorAsocio").toString());
			String tipoServicio = campos.get("tipoServicio").toString();
			String cobrable = campos.get("cobrable").toString();
			
			/*ADICION ANEXO 777 - CAMBIOS EN FUNCIONALIDADES X EXCEPCIONES EN CENTROS DE COSTO RESPUESTAS SHAIO*/
			int centroCostoEjecuta= Utilidades.convertirAEntero(campos.get("centroCostoEjecuta")+"");
			int medico= Utilidades.convertirAEntero(campos.get("medico")+"");
			/*--------------------------------------------------------------------------------------------------*/
			
			//**********************************************************************
			
			if(codigoPool<=0 && codigoProfesional>0) 
				codigoPool=obtenerCodigoPoolProfesionalPostular(con,codigoProfesional);
			
			PreparedStatementDecorator pst = null;
			
			if(tipoServicio.equals(ConstantesBD.codigoServicioHonorariosCirugia+"")||tipoServicio.equals(ConstantesBD.codigoServicioProcedimiento+""))
			{
				consecutivo = UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_det_cx_honorario");
				pst =  new PreparedStatementDecorator(con.prepareStatement(insertarAsocioHonorarios_Str, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
				
				/**
				 * INSERT INTO det_cx_honorarios (
				 * codigo,
				 * cod_sol_cx_servicio,
				 * especialidad_medico,
				 * institucion,
				 * medico,
				 * servicio,
				 * pool,
				 * tipo_asocio,
				 * codigo_propietario,
				 * valor,
				 * cobrable,
				 * centro_costo_ejecuta) values (?,?,?,?,?,?,?,?,?,?,?,?)
				 */
				
				pst.setInt(1,consecutivo);
				pst.setInt(2,Utilidades.convertirAEntero(consecutivoServicio));
				pst.setInt(3,codigoEspecialidad);
				if(codigoProfesional>0) //si se ingresa profesional se inserta la institucion
					pst.setInt(4,codigoInstitucion);
				else
					pst.setNull(4,Types.INTEGER);
				if(codigoProfesional>0)
					pst.setInt(5,codigoProfesional);
				else
					pst.setNull(5,Types.INTEGER);
				pst.setInt(6,codigoServicio);
				if(codigoPool>0)
					pst.setInt(7,codigoPool);
				else
					pst.setNull(7,Types.INTEGER);
				pst.setInt(8,codigoAsocio);
				pst.setString(9,codigoPropietario);
				pst.setDouble(10, valorAsocio);
				pst.setString(11,cobrable);
				if(centroCostoEjecuta>0)
					pst.setInt(12, centroCostoEjecuta);
				else	
					pst.setNull(12, Types.INTEGER);
			}
			else
			{
				consecutivo = UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_det_as_cx_sal_mat");
				pst =  new PreparedStatementDecorator(con.prepareStatement(insertarAsocioSalasMateriales_Str, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
				pst.setInt(1,consecutivo);
				pst.setInt(2,Utilidades.convertirAEntero(consecutivoServicio));
				pst.setInt(3,codigoServicio);
				pst.setInt(4,codigoAsocio);
				pst.setString(5,codigoPropietario);
				pst.setDouble(6, valorAsocio);
				if(medico>0)
					pst.setInt(7, medico);
				else
					pst.setNull(7, medico);
			}
			
			resultado = pst.executeUpdate();
			
			if(resultado>0)
				resultado = consecutivo;
			
			pst.close();
			
		}
		catch(SQLException e)
		{
			logger.error("Error en insertarAsocio: "+e);
		}
		return resultado;
	}
	
	
	/**
     * @throws SQLException 
     * 
     */
    private static int obtenerCodigoPoolProfesionalPostular(Connection con,int codigoProfesional) throws SQLException  
    {
    	int resultado=ConstantesBD.codigoNuncaValido;
    	String consulta="SELECT count(1) from participaciones_pooles where medico="+codigoProfesional+" and (fecha_retiro > current_date or fecha_retiro is null)";
    	PreparedStatementDecorator ps=new PreparedStatementDecorator(con,consulta);
    	ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
    	if(rs.next())
    	{
    		if(rs.getInt(1)==1)
    		{
    			String consultaInterna="SELECT pool from participaciones_pooles where medico="+codigoProfesional+" and (fecha_retiro > current_date or fecha_retiro is null)";
    			PreparedStatementDecorator psInterna=new PreparedStatementDecorator(con,consultaInterna);
    	    	ResultSetDecorator rsInterna=new ResultSetDecorator(psInterna.executeQuery());
    	    	if(rsInterna.next())
    	    		resultado=rsInterna.getInt(1);
    	    	rsInterna.close();
    	    	psInterna.close();
    		}
    	}
    	rs.close();
    	ps.close();
		return resultado;
	}
	
	/**
	 * Método implementado para consultar los materiales especiales
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public static HashMap<String, Object> consultarMaterialesEspeciales(Connection con,String numeroSolicitud)
	{
		HashMap<String, Object> resultados = new HashMap<String, Object>();
		try
		{
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consultarMaterialesEspeciales_Str, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			pst.setInt(1,Utilidades.convertirAEntero(numeroSolicitud));
			
			resultados = UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()), true, true);
			
			pst.close();
		}
		catch(SQLException e)
		{
			logger.error("Error en consultarMaterialesEspeciales: "+e);
		}
		return resultados;
	}
	
	/**
	 * Método implementado
	 * @param con
	 * @return
	 */
	public static InfoDatosInt obtenerAsocioProcedimientos(Connection con,int institucion)
	{
		InfoDatosInt asocio = new InfoDatosInt(ConstantesBD.codigoNuncaValido,"");
		try
		{
			String consulta = "SELECT codigo AS consecutivo,codigo_asocio,nombre_asocio FROM tipos_asocio WHERE institucion = "+institucion+" and tipos_servicio = '"+ConstantesBD.codigoServicioProcedimiento+"'";
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			ResultSetDecorator rs = new ResultSetDecorator(st.executeQuery(consulta));
			
			if(rs.next())
			{
				asocio.setCodigo(rs.getInt("consecutivo"));
				asocio.setNombre(rs.getString("nombre_asocio"));
			}
			
			st.close();
			rs.close();
			
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerAsocioProcedimientos: "+e);
		}
		return asocio;
	}
	
	/**
	 * Método implementado para insertar el log bd de la liquidación de servicios
	 * @param con
	 * @param numeroSolicitud
	 * @param loginUsuario
	 * @return
	 */
	public static int insertarLiquidacionServicios(Connection con,String numeroSolicitud,String loginUsuario)
	{
		int respuesta = 0;
		try
		{
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(insertarLiquidacionServicios_Str, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			
			/**
			 * INSERT INTO liquidacion_servicios (codigo,numero_solicitud,fecha,hora,usuario) VALUES(?,?,CURRENT_DATE,"+ValoresPorDefecto.getSentenciaHoraActualBD()+",?)
			 */
			
			int secuencia = UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_liquidacion_servicios");
			pst.setDouble(1,Utilidades.convertirADouble(secuencia+""));
			pst.setInt(2,Utilidades.convertirAEntero(numeroSolicitud));
			pst.setString(3,loginUsuario);
			
			respuesta = pst.executeUpdate();
			
			pst.close();
			
		}
		catch(SQLException e)
		{
			logger.error("Error en insertarLiquidacionServicios: "+e);
		}
		return respuesta;
	}
	
	/**
	 * Método para consultar los asocios liquidados de una orden
	 * @param con
	 * @param consecutivoServicio
	 * @return
	 */
	public static HashMap<String, Object> consultarAsociosLiquidados(Connection con,String consecutivoServicio)
	{
		HashMap<String, Object> resultados = new HashMap<String, Object>();
		try
		{
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consultarAsociosLiquidados_Str, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			pst.setInt(1,Utilidades.convertirAEntero(consecutivoServicio));
			pst.setInt(2,Utilidades.convertirAEntero(consecutivoServicio));
			
			resultados = UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()), true, true);
			
			pst.close();
		}
		catch(SQLException e)
		{
			logger.error("Error en consultarAsociosLiquidados: "+e);
		}
		return resultados;
	}
	
	/**
	 * Método para actualizar el indicativo de consumo de materiales en la solicitud
	 * @param con
	 * @param numeroSolicitud
	 * @param indicador
	 * @return
	 */
	public static int actualizarIndicadorConsumoMaterialesSolicitud(Connection con,String numeroSolicitud,boolean indicador)
	{
		int resp = ConstantesBD.codigoNuncaValido;
		try
		{
			String consulta = " UPDATE solicitudes_cirugia SET " +
				"indi_tarifa_consumo_materiales = '"+(indicador?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo)+"' " +
				"WHERE numero_solicitud = "+numeroSolicitud;
			
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			resp = st.executeUpdate(consulta);
			
			st.close();
			
		}
		catch(SQLException e)
		{
			logger.error("Error en actualizarIndicadorConsumoMaterialesSolicitud: "+e);
		}
		return resp;
	}
	//*****************************************************************************************************************************
	//**************************FIN MÉTODOS PARA EL MANEJO DE LA LIQUIDACION*******************************************************
	//******************************************************************************************************************************

}
