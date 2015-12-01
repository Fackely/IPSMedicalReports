package com.princetonsa.dao.postgresql.facturacion;

import java.sql.Connection;
import java.util.HashMap;
import util.ConstantesBD;
import util.ValoresPorDefecto;

import com.princetonsa.dao.facturacion.RevisionCuentaDao;
import com.princetonsa.dao.sqlbase.facturacion.SqlBaseRevisionCuenta;

public class PostgresqlRevisionCuentaDao implements RevisionCuentaDao
{
	private static final String consulta="INSERT INTO log_estad_cargo_revis_cuenta " +
										"(codigo, " +
										"fecha, " +
										"hora, " +
										"usuario, " +
										"codigo_detalle_cargo, " +
										"estado_anterior, " +
										"estado_actual)" +
										"VALUES (nextval('facturacion.seq_log_estad_cargo_revi_cuen'),?,?,?,?,?,? ) ";
	
	/**
	 * Consulta los responsables (convenios) de la subcuenta
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public HashMap consultarResponsables(Connection con,
										HashMap parametros)
	{
		return SqlBaseRevisionCuenta.consultarResponsables(con, parametros);
	}
	
	
	/**
	 * Consulta los requisitos de ingreso y egreso por convenio
	 * @param Connection con
	 * @param HashMap parametros 
	 * */
	public HashMap consultarRequisitosConvenio(Connection con,
													HashMap parametros)
	{
		return SqlBaseRevisionCuenta.consultarRequisitosConvenio(con, parametros);
	}
	
	
	/**
	 * Actualiza los requisitos de ingreso y egreso por convenio
	 * @param Connection con
	 * @param String requisitopaciente
	 * @param String subcuenta
	 * @param String cumplido
	 * */
	
	public boolean actualizarRequisitosConvenio(Connection con,
													   String requisitopaciente,
													   String subcuenta,
											  		   String cumplido)
	{
		if(cumplido.equals(ConstantesBD.acronimoSi))
			cumplido = "t";
		else
			cumplido = "f";
		
		return SqlBaseRevisionCuenta.actualizarRequisitosConvenio(con, requisitopaciente, subcuenta, cumplido);
	}
	
	
	/**
	 * Consulta de las solicitudes por el Responsable
	 * @param Connection con
	 * @param HashMap parametros 
	 * */
	public HashMap consultarListadoSolicitudes(Connection con,
														HashMap parametros)
	{
		String strCadenaListadoConsultaSolicitudesXResponsable = 
			"SELECT "+
					"consecutivo_ordenes_medicas AS consecutivoordenesmedicas, "+
					//"codigo AS codigo, "+
					"case when '"+parametros.get("mostrarIndicativo").toString()+"'='"+ConstantesBD.acronimoSi+"' then getIndicativoSolicitudes(ssb.sub_cuenta,so.numero_solicitud,(CASE WHEN pyp ="+ValoresPorDefecto.getValorTrueParaConsultas()+" THEN '"+ConstantesBD.acronimoSi+"' ELSE '"+ConstantesBD.acronimoNo+"' END)) else ' ' end AS indicativo, "+
					"numero_solicitud AS numerosolicitud, "+
					"so.tipo AS tiposolicitud, "+
					"getnomtiposolicitud(so.tipo) as nombresolicitud, "+
					/*"CASE " +
					    "WHEN so.tipo = "+ConstantesBD.codigoTipoSolicitudPaquetes+" THEN "+ 
							"getintegridaddominio(getIndicadorQxSolicitud(numero_solicitud)) "+ 
					    "ELSE "+ 
							"getnomtiposolicitud (ssb.tipo_solicitud) "+ 
					"END AS nombresolicitud, "+ 
					"servicio AS servicio, "+
					"gettieneportatilsolicitud(numero_solicitud) AS codigoportatil, "+
					"articulo AS articulo, "+
					"case when servicio is null then 0 else 1 end AS indicadorservart, "+
					"case " +
					    "when servicio is null then "+ 
							"articulo || ' - ' || getdescripcionarticulo(articulo) "+ 
						"else "+ 
						"( "+
						  	"case when so.tipo <> "+ConstantesBD.codigoTipoSolicitudCirugia+" and so.tipo <>"+ConstantesBD.codigoTipoSolicitudPaquetes+" then "+ 
						  		"getcodigopropservicio2(servicio, 0)|| ' - ' || getnombreservicio(servicio,"+ConstantesBD.codigoTarifarioCups+") "+ 
						    "else "+
								"getcodigopropservicio2(servicio, 0)|| ' - ' ||getnombreservicio(getserviciosolicitud(numero_solicitud,so.tipo),"+ConstantesBD.codigoTarifarioCups+") "+
							"end "+ 
						") "+
					"end AS descripcionservarticulo, "+*/
					"via_ingreso AS viaingreso, "+
					"getnombreviaingreso(via_ingreso) AS nombreviaingreso, "+
					"CASE WHEN so.pyp ="+ValoresPorDefecto.getValorTrueParaConsultas()+" THEN 'S' ELSE 'N' END  AS pyp, "+
					"fecha_solicitud||'' AS fechasolicitud, "+
					"estado_historia_clinica AS estadohc, "+
					"getestadosolhis(so.estado_historia_clinica) AS nombreestadohc, "+
					"paquetizada AS paquetizada, "+
					//"sol_subcuenta_padre AS solsubcuentapadre, "+
					//"servicio_cx AS serviciocx, "+
					//"' ' AS nombreserviciocx, "+ 
					"CASE WHEN so.tipo = "+ConstantesBD.codigoTipoSolicitudMedicamentos+" THEN "+ 
					      "to_char(getfecharespuestasolmedi(numero_solicitud,so.tipo),'DD/MM/YYYY HH24:MI') "+ 
					"ELSE "+
					      "to_char(getfecharespuestasol(numero_solicitud,so.tipo),'DD/MM/YYYY HH24:MI') "+ 
					"END  AS fecharespuesta, "+ 
					"getestadosolfac(getcodestadodetcargos(ssb.sub_cuenta,so.numero_solicitud)) AS nombreestadocargo, "+ 
					"getvalortotalestadodetcargos(ssb.sub_cuenta, so.numero_solicitud) AS valortotal "+ 
				"FROM solicitudes_subcuenta ssb "  +
				"INNER JOIN cuentas cu ON (cu.id = ssb.cuenta) "  +
				//"INNER JOIN  solicitudes so ON (so.numero_solicitud = so.numero_solicitud) "+ OJO CON ESTO 
				"INNER JOIN  solicitudes so ON (so.numero_solicitud = ssb.solicitud) "+
				"WHERE " +
				"ssb.sub_cuenta = "+parametros.get("subcuenta")+" AND "+ 
				"ssb.paquetizada = '"+parametros.get("espaquetizada")+"' and ssb.eliminado='"+ConstantesBD.acronimoNo+ "' ";
		

		String strCadenaSolicitudesXResponsableSinCargo = "SELECT " +
				"sol.consecutivo_ordenes_medicas AS consecutivoordenesmedicas," +
				//"0 AS codigo," +
				"getIndicativoSolicitudes(-1,sol.numero_solicitud,CASE WHEN sol.pyp ="+ValoresPorDefecto.getValorTrueParaConsultas()+" THEN '"+ConstantesBD.acronimoSi+"' ELSE '"+ConstantesBD.acronimoNo+"' END) AS indicativo," +
				"sol.numero_solicitud AS numerosolicitud," +
				"sol.tipo AS tiposolicitud," +
				"getnomtiposolicitud (sol.tipo) AS nombresolicitud," +
				/*"solc.servicio AS servicio," +
				"gettieneportatilsolicitud(sol.numero_solicitud) AS codigoportatil,"+
				"0 AS articulo, " +
				"0 AS indicadorservart," +
				"'"+ConstantesBD.codigoNuncaValido+"' AS descripcionservarticulo," +*/
				"cu.via_ingreso AS viaingreso," +
				"getnombreviaingreso(cu.via_ingreso) AS nombreviaingreso," +
				"CASE WHEN sol.pyp ="+ValoresPorDefecto.getValorTrueParaConsultas()+" THEN 'S' ELSE 'N' END  AS pyp," +			
				"to_char(sol.fecha_solicitud,'DD/MM/YYYY') || ' ' || sol.hora_solicitud AS fechasolicitud," +
				"sol.estado_historia_clinica AS estadohc," +
				"getestadosolhis(sol.estado_historia_clinica) AS nombreestadohc," +
				"'N' AS paquetizada," +
				/*"0 AS solsubcuentapadre," +
				"solc.servicio AS serviciocx," +
				"UPPER(getnombreservicio(solc.servicio,"+ConstantesBD.codigoTarifarioCups+")) AS nombreserviciocx, " +*/
				
				"CASE WHEN sol.tipo = "+ConstantesBD.codigoTipoSolicitudMedicamentos+" THEN " +
				"	to_char(getfecharespuestasolmedi(sol.numero_solicitud,sol.tipo),'DD/MM/YYYY HH24:MI') " +
				"ELSE" +
				"   to_char(getfecharespuestasol(sol.numero_solicitud,sol.tipo),'DD/MM/YYYY HH24:MI') " +
				"END  AS fecharespuesta, " +
				
				"getestadosolfac(getcodestadodetcargos(sc.sub_cuenta, sol.numero_solicitud)) AS nombreestadocargo, " +
				"getvalortotalestadodetcargos(sc.sub_cuenta, sol.numero_solicitud) AS valortotal " +
				
				"FROM cuentas cu " +
				"INNER JOIN solicitudes sol ON (sol.cuenta = cu.id ) " +
				"INNER JOIN solicitudes_cirugia sc ON (sc.numero_solicitud = sol.numero_solicitud) " +		
				"INNER JOIN sol_cirugia_por_servicio solc ON (solc.numero_solicitud = sol.numero_solicitud AND solc.consecutivo = 1 ) " +
				"WHERE cu.id_ingreso = "+parametros.get("ingreso")+" AND sc.sub_cuenta = "+parametros.get("subcuenta")+" AND tieneCargosSolicitud(sol.numero_solicitud) = '"+ConstantesBD.acronimoNo+"' ";
		
		
		
		return SqlBaseRevisionCuenta.consultarListadoSolicitudes(con, parametros,strCadenaListadoConsultaSolicitudesXResponsable,strCadenaSolicitudesXResponsableSinCargo);
	}
	

	/**
	 * Consulta los estados de la solicitud en la historia clinica 
	 * @param Connection con  
	 * */
	public HashMap consultarEstadosSolicitudHistoriaC(Connection con)
	{
		return SqlBaseRevisionCuenta.consultarEstadosSolicitudHistoriaC(con);
	}
	
	
	/**
	 * Consultar los tipos de solicitud 
	 * @param Connection con  
	 * */
	public HashMap consultarTiposSolicitud(Connection con)
	{
		return SqlBaseRevisionCuenta.consultarTiposSolicitud(con);	
	}
	
	
	/**
	 * Consultar Detalle de la solicitud   
	 * @param Connection con  
	 * @param HashMap parametros
	 * */
	public HashMap consultarDetalleSolicitud(Connection con, HashMap parametros)
	{
		return SqlBaseRevisionCuenta.consultarDetalleSolicitud(con, parametros);
	}
	
	
	/**
	 * Actualiza los registros del detalle de la solicitud
	 * @param Connection con
	 * @param HashMap parametros 
	 * */
	public boolean actualizarDetalleSolicitud(Connection con,
													 HashMap parametros)	
	{
		return SqlBaseRevisionCuenta.actualizarDetalleSolicitud(con, parametros);
	}
	
	
	/**
	 * Consultar Detalle de la Cirugia  
	 * @param Connection con  
	 * @param HashMap parametros
	 * */
	public HashMap consultarDetalleCirugia(Connection con, 
										   HashMap parametros)
	{
		return SqlBaseRevisionCuenta.consultarDetalleCirugia(con, parametros);
	}
	
	
	/**
	 * Consultar el listado de Pooles pendientes  
	 * @param Connection con  
	 * @param HashMap parametros
	 * */
	public HashMap consultarListadoPooles(Connection con, 
												 HashMap parametros)
	{
		return SqlBaseRevisionCuenta.consultarListadoPooles(con, parametros);
	}
	
	
	/**
	 * Verifica la existencia de pooles pendientes  
	 * @param Connection con  
	 * @param HashMap parametros
	 * */
	public int verificarPoolesPendientes(Connection con, 
											 	HashMap parametros)
	{	
		
		return SqlBaseRevisionCuenta.verificarPoolesPendientes(con, parametros);
	}
	
	
	/**
	 * Consultar el listado de Pooles para el medico   
	 * @param Connection con  
	 * @param HashMap parametros
	 * */
	public HashMap consultarListadoPoolesMedico(Connection con, 
												HashMap parametros)
	{
		return SqlBaseRevisionCuenta.consultarListadoPoolesMedico(con, parametros);
	}
	
	
	/**
	 * Actualiza los Pooles por Medico diferentes de tipo Solicitud Cirugia 
	 * @param Connection con
	 * @param HashMap parametros
	 * */	
	public boolean actualizarPoolesMedico(Connection con,
										  HashMap parametros)
	{
		return SqlBaseRevisionCuenta.actualizarPoolesMedico(con, parametros);
	}
	
	
	/**
	 * Consultar Detalle Pooles Pendientes de la Cirugia  
	 * @param Connection con  
	 * @param HashMap parametros
	 * */
	public HashMap consultarDetalleCirugiaPool(Connection con, 
			   							  	   HashMap parametros)
	{
		return SqlBaseRevisionCuenta.consultarDetalleCirugiaPool(con, parametros);
	}	
	
	/**
	 * Actualiza los Pooles por Medico, tipo Solicitud Cirugia 
	 * @param Connection con
	 * @param HashMap parametros
	 * */	
	public boolean actualizarPoolesMedicoCirugia(Connection con,
														HashMap parametros)
	{
		return SqlBaseRevisionCuenta.actualizarPoolesMedicoCirugia(con, parametros);
	}


	/**
	 * Actualiza los Estados de una Solicitud de Excenta a Cargada y Viseversa
	 */
	public boolean actualizarEstadoCargo(Connection con, int nuevoEstadoCargo, String numeroSolicitud) 
	{
		return SqlBaseRevisionCuenta.actualizarEstadoCargo(con, nuevoEstadoCargo, numeroSolicitud);
	}

	/**
	 * Metodo para consultar todos los detalles de la cuenta y los estados de una solicitud
	 */
	public HashMap detalleEstadoSolicitud(Connection con, String numeroSolicitud) 
	{
		return SqlBaseRevisionCuenta.detalleEstadoSolicitud(con, numeroSolicitud);
	}

	/**
	 * Metodo para insertar el registro del cambio en la tabla log_estad_cargo_revis_cuenta
	 */
	public void insertarLogEstadoCargo(Connection con, String codigoDetalleCargo, String estadoAnterior, String estadoActual, String loginUsuario) 
	{
		SqlBaseRevisionCuenta.insertarLogEstadoCargo(con, codigoDetalleCargo, estadoAnterior, estadoActual, loginUsuario, consulta);
	}
	
	/**
	 * 
	 */
	public HashMap consultarEstadosSolFactura(Connection con)
	{
		return SqlBaseRevisionCuenta.consultarEstadosSolFactura(con);
	}
}