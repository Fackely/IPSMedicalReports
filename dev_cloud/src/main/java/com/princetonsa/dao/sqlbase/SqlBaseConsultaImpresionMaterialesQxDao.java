/*
 * Oct 4, 2005
 *
 */
package com.princetonsa.dao.sqlbase;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMessage;

import com.princetonsa.dao.DaoFactory;
import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.RespuestaHashMap;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.inventarios.UtilidadInventarios;

/**
 * @author Luis Gabriel Chavez Salazar
 *
 * Objeto usado para manejar los accesos comunes a la fuente de datos
 * para la funcionalidad de Consulta Materiales Qx
 */
public class SqlBaseConsultaImpresionMaterialesQxDao 
{
	/**
	 * Manejador de logs de la clase
	 */
	private static Logger logger=Logger.getLogger(SqlBaseMaterialesQxDao.class);
	
	private static String consultaIngresosPac="SELECT distinct " +
												" i.codigo_paciente," +
												" getnomcentroatencion(i.centro_atencion) as centro_atencion," +
												" i.consecutivo," +
												" i.fecha_ingreso || ' - ' || i.hora_ingreso as fecha_ingreso," +
												" i.estado," +
												" getnombreviaingreso(c.via_ingreso) as via_ingreso," +
												" getnombreestadocuenta(c.estado_cuenta) as estado_cuenta," +
												" i.fecha_egreso || ' - ' || i.hora_egreso as fecha_egreso," +
												" c.id as cuenta, " +
												" i.id as ingreso" +
											" FROM " +
												" ingresos i " +
												"INNER JOIN " +
												" cuentas c ON (i.id=c.id_ingreso)	" +
												"INNER JOIN " +
												" solicitudes s ON (c.id=s.cuenta)" +
												"INNER JOIN " +
												" solicitudes_cirugia sc ON (s.numero_solicitud=sc.numero_solicitud)	" +
											" WHERE " +
												"	1=1 " +
												"	AND getcuentafinal(c.id) IS NULL " ;
	
	
	private static String consultaSolicitudesQx="SELECT " +
														" s.numero_solicitud," +
														" s.fecha_solicitud || ' - ' || s.hora_solicitud as fecha_solicitud, " +
														" getnomcentrocosto(s.centro_costo_solicitado) as centro_costo," +
														" getestadosolhis(s.estado_historia_clinica) as estadohistoria," +
														" getnomcentroatencion(getcentroatencionsol(s.numero_solicitud)) as centroatencion," +
														" getidpaciente(c.codigo_paciente) as tipoid, " +
														" getnombrepersona(c.codigo_paciente) as nombrepac, " +
														" c.id_ingreso as ingreso, " +
														" s.fecha_solicitud as fechasol," +
														" getEstadoIngreso(c.id_ingreso) as estadoing," +
														" c.codigo_paciente as codigopac," +
														" s.consecutivo_ordenes_medicas as consecutivo " +
													" FROM " +
														" solicitudes s " +
														"INNER JOIN " +
														" cuentas c ON (c.id=s.cuenta) " +
														"INNER JOIN " +
														" solicitudes_cirugia sc ON (s.numero_solicitud=sc.numero_solicitud)	" +
													" WHERE " +
														"	1=1 " ;

	
	private static String consultaConsumoMaterialesQx="SELECT "+
														"	dmq.articulo as cod," +
														"	getnombreservicio(dmq.servicio,"+ConstantesBD.codigoTarifarioCups+") as nomserv, " +
														"   gettienejustificacionnopos(dmq.articulo,dmq.numero_solicitud) as nopos," +
														"	getespos(dmq.articulo) as espos," +
														"	getdescarticulosincodigo(dmq.articulo) as descripcion,	" +
														"	getunidadmedidaarticulo(dmq.articulo) as unimedida," +
														"	getCantidadTotalDespachada(dmq.articulo,dmq.numero_solicitud ) as cantdesp," +
														"	SUM(dmq.cantidad) as cantcons," +
														"	getCantidadTotalDevuelta(dmq.articulo,dmq.numero_solicitud ) as cantdevu," +
														"	getcostounitConsumoMaterialCx(dmq.numero_solicitud,dmq.articulo) as costouni," +
														"	getvalorunitConsuMaterialesCx(dmq.numero_solicitud,dmq.articulo) as tarifauni," +
														"	getcostounitConsumoMaterialCx(dmq.numero_solicitud,dmq.articulo)as valortotal," +
														"	coalesce(dfmq.incluido, 'S') as incluido," +
														"	coalesce(getIndicTarifaConsuMateriales(dmq.numero_solicitud), 'N') as indica, " +
														"	dmq.servicio as codserv," +
														"	'N' as band " +
														" FROM " +
														"	det_materiales_qx dmq" +
														" LEFT OUTER JOIN " +
														"	det_fin_materiales_qx dfmq ON (dmq.articulo=dfmq.articulo and dmq.numero_solicitud=dfmq.numero_solicitud)" +
														" WHERE " +
														"	1=1 " ;
	

	private static String consultaConsumoMaterialesQxActo="SELECT "+
															"	dmq.articulo as cod," +
															"   gettienejustificacionnopos(dmq.articulo,dmq.numero_solicitud) as nopos," +
															"	getespos(dmq.articulo) as espos," +
															"	getdescarticulosincodigo(dmq.articulo) as descripcion,	" +
															"	getunidadmedidaarticulo(dmq.articulo) as unimedida," +
															"	getCantidadTotalDespachada(dmq.articulo,dmq.numero_solicitud ) as cantdesp," +
															"	SUM(dmq.cantidad) as cantcons," +
															"	getCantidadTotalDevuelta(dmq.articulo,dmq.numero_solicitud ) as cantdevu," +
															"	getcostounitConsumoMaterialCx(dmq.numero_solicitud,dmq.articulo) as costouni," +
															"	getvalorunitConsuMaterialesCx(dmq.numero_solicitud,dmq.articulo) as tarifauni," +
															"	SUM(dmq.cantidad)*getcostounitConsumoMaterialCx(dmq.numero_solicitud,dmq.articulo)as valortotal," +
															"	coalesce(dfmq.incluido, 'S') as incluido," +
															"	coalesce(getIndicTarifaConsuMateriales(dmq.numero_solicitud), 'N') as indica," +
															"	dmq.servicio as codserv " +
															" FROM " +
															"	det_materiales_qx dmq" +
															" LEFT OUTER JOIN " +
															"	det_fin_materiales_qx dfmq ON (dmq.articulo=dfmq.articulo and dmq.numero_solicitud=dfmq.numero_solicitud)" +
															" WHERE " +
															"	1=1 " ;

	
	
	private static String consultaServiciosConsumoMaterialesQxActo="SELECT " +
																	" getnombreservicio(scps.servicio,"+ConstantesBD.codigoTarifarioCups+") as servicio," +
																	" scps.consecutivo," +
																	" scps.servicio as codserv	 " +
																" FROM " +
																	" sol_cirugia_por_servicio scps " +
																" WHERE " +
																	"	1=1 " ;

	private static String consultaServiciosConsumoMaterialesQx="SELECT distinct " +
																	" getnombreservicio(scps.servicio,"+ConstantesBD.codigoTarifarioCups+") as servicio," +
																	" scps.servicio as codserv	 " +
																" FROM " +
																	" det_materiales_qx scps " +
																" WHERE " +
																	"	1=1 " ;
	
	
	
	
	

	/**
	 * 
	 * @param con
	 * @param criterios
	 * @param espaciente
	 * @return
	 */
	public static HashMap consultaIngresosPaciente(Connection con, HashMap criterios) {
		
		String consulta="";

			consulta=consultaIngresosPac;
			consulta+=" AND i.codigo_paciente="+Utilidades.convertirAEntero(criterios.get("paciente").toString())+" "+
					  " AND i.centro_atencion="+Utilidades.convertirAEntero(criterios.get("centroatencion").toString())+" " +
					  "	AND i.estado IN ('"+ConstantesIntegridadDominio.acronimoEstadoCerrado+"','"+ConstantesIntegridadDominio.acronimoEstadoAbierto+"') " +
					  " AND s.tipo="+ConstantesBD.codigoTipoSolicitudCirugia+" "+
					  " AND sc.ind_qx IN ('"+ConstantesIntegridadDominio.acronimoIndicativoCargoCirugiaCargoDirecto+"','"+ConstantesIntegridadDominio.acronimoIndicativoCargoNoCruentoCargoDirecto+"','"+ConstantesIntegridadDominio.acronimoIndicativoCargoNoCruento+"','"+ConstantesIntegridadDominio.acronimoIndicativoCargoCirugia+"') " +
					  " ORDER BY " +
						"	fecha_ingreso" +
						"";
		
		HashMap mapa=new HashMap();
		mapa.put("numRegistros","0");
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
			logger.info("\n\n\n\n Consulta INGRESOS  >>>"+consulta);
		}
		catch (SQLException e)
		{
			logger.info("\n\n\n Error en la consulta de ingresos >>>"+e+"\n\n\n Sentencia"+consulta);
		}
		return mapa;
		
	}
		

	
	
	
	
	/**
	 * 
	 * @param con
	 * @param criterios
	 * @param espaciente
	 * @return
	 */
	public static HashMap cargarSolicitudesCx(Connection con, HashMap criterios, boolean espaciente) {
		
		String consulta=consultaSolicitudesQx;
			
		if(espaciente)
		{
			consulta+=" AND s.tipo="+ConstantesBD.codigoTipoSolicitudCirugia+" "+
					  " AND sc.ind_qx IN ('"+ConstantesIntegridadDominio.acronimoIndicativoCargoCirugiaCargoDirecto+"','"+ConstantesIntegridadDominio.acronimoIndicativoCargoNoCruentoCargoDirecto+"','"+ConstantesIntegridadDominio.acronimoIndicativoCargoNoCruento+"','"+ConstantesIntegridadDominio.acronimoIndicativoCargoCirugia+"') " +
					  " AND c.id_ingreso="+criterios.get("ingreso").toString()+" "+
					  " AND c.id="+criterios.get("cuenta").toString()+" " +
					  " AND s.estado_historia_clinica <>"+ConstantesBD.codigoEstadoHCAnulada+"";
		}
		else{
			
			consulta+=" AND s.tipo="+ConstantesBD.codigoTipoSolicitudCirugia+" "+
					  " AND sc.ind_qx IN ('"+ConstantesIntegridadDominio.acronimoIndicativoCargoCirugiaCargoDirecto+"','"+ConstantesIntegridadDominio.acronimoIndicativoCargoNoCruentoCargoDirecto+"','"+ConstantesIntegridadDominio.acronimoIndicativoCargoNoCruento+"','"+ConstantesIntegridadDominio.acronimoIndicativoCargoCirugia+"') " +
					  " AND s.estado_historia_clinica <>"+ConstantesBD.codigoEstadoHCAnulada+""+
					  " AND s.fecha_solicitud BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(criterios.get("fechaInicial")+"")+"' and '"+UtilidadFecha.conversionFormatoFechaABD(criterios.get("fechaFinal")+"")+"' "+
					  " AND getcentroatencionsol(s.numero_solicitud) ="+criterios.get("centroAtencion")+"";
			
			int centroCosto=Utilidades.convertirAEntero(criterios.get("centrocosto")+"");
			if (centroCosto>0)
			{
				consulta+=" AND s.centro_costo_solicitado="+criterios.get("centrocosto")+" ";	
			}
					
			
		}
		
		
		consulta+=  " ORDER BY " +
							"	s.fecha_solicitud,fecha_solicitud desc " +
							"	";
		
		HashMap mapa=new HashMap();
		mapa.put("numRegistros","0");
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
			logger.info("\n\n\n\n Consulta solicitudes cirugia  >>>"+consulta);
		}
		catch (SQLException e)
		{
			logger.info("\n\n\n Error en la consulta de solicitudes cirugia >>>"+e+"\n\n\n Sentencia"+consulta);
		}
		return mapa;
		
	}
	
	
	/**
	 * 
	 * @param con
	 * @param criterios
	 * @param esacto
	 * @return
	 */
	public static HashMap cargarConsumoMaterialesCirugias(Connection con, HashMap criterios, boolean esacto)
	{
	
		String consulta="";
		
		HashMap mapa=new HashMap();
		mapa.put("numRegistros","0");
	
		/*********************************************************************************
		 * 	CONSULTA LOS SERVICIOS DE LA SOLICITUD DE CIRUGIA  SI ES POR ACTO
		 *********************************************************************************/
			
		if(esacto) {
			consulta=consultaServiciosConsumoMaterialesQxActo;
			consulta+=" AND scps.numero_solicitud="+criterios.get("solicitud").toString()+" "+
					  " ORDER BY " +
						"	codserv, scps.consecutivo " +
						"	";
		}
		else
		{
			consulta=consultaServiciosConsumoMaterialesQx;
			consulta+=" AND scps.numero_solicitud="+criterios.get("solicitud").toString()+" "+
					  " ORDER BY " +
						"	codserv " +
						"	";
		}
		
		
			try
			{
				PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
				logger.info("\n\n\n\n Consulta Servicios cirugia  >>>"+consulta);
			}
			catch (SQLException e)
			{
				logger.info("\n\n\n Error en la consulta de servicios cirugia >>>"+e+"\n\n\n Sentencia"+consulta);
			}
		
		/*********************************************************************************
		 * FIN CONSULTA LOS SERVICIOS DE LA SOLICITUD DE CIRUGIA
		 *********************************************************************************/
		
		/*********************************************************************************
		 * 	CONSULTA EL CONSUMO DE MATERIALES QX DE LA SOLICITUD
		 *********************************************************************************/
		
		
		if(esacto){
			consulta=consultaConsumoMaterialesQxActo;
			consulta+=" AND	dmq.numero_solicitud="+criterios.get("solicitud").toString()+" ";
			consulta+=	" GROUP BY " +
							"	dmq.articulo,dmq.numero_solicitud,dfmq.incluido,dmq.servicio " +
						" ORDER BY " +
						"	descripcion" ;
		}else
			{
				consulta=consultaConsumoMaterialesQx;
				consulta+=" AND	dmq.numero_solicitud="+criterios.get("solicitud").toString()+"" ;
		
				consulta+=	" GROUP BY " +
							"	dmq.articulo,dmq.servicio,dmq.numero_solicitud,dfmq.incluido " +
							" ORDER BY " +
							"	codserv,descripcion " ;
			}
			
		
		HashMap mapa1=new HashMap();
		mapa1.put("numRegistros","0");
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			mapa1=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
			logger.info("\n\n\n\n Consulta Servicios cirugia  >>>"+consulta);
		}
		catch (SQLException e)
		{
			logger.info("\n\n\n Error en la consulta de servicios cirugia >>>"+e+"\n\n\n Sentencia"+consulta);
		}

		/*********************************************************************************
		 * 	FIN CONSULTA EL CONSUMO DE MATERIALES QX DE LA SOLICITUD
		 *********************************************************************************/
	
		//	Mapa de servicios en mapa de materiales qx
			mapa1.put("mapaCirugias", mapa);
		
		
		return mapa1;
	}
	
	
	
	
	
	
	
	
	
}
