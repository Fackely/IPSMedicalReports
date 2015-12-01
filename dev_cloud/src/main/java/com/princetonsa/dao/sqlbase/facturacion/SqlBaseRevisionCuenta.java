package com.princetonsa.dao.sqlbase.facturacion;

import java.util.HashMap;

import java.sql.Date;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import java.sql.Types;

import org.apache.log4j.Logger;

import com.princetonsa.action.facturacion.RevisionCuentaPacienteAction;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

/**
 * @author Jose Eduardo Arias Doncel
 * jearias@princetonsa.com
 * */
 
public class SqlBaseRevisionCuenta
{
	
	//*******************************************************************************
	//*****************************Atributos*****************************************
	
	
	static Logger logger = Logger.getLogger(RevisionCuentaPacienteAction.class);
	
	
	/**
	 * Cadena Consulta de la Revision del Cuenta
	 * */
	private static final String strCadenaConsultaResponsables = "SELECT " +
			"sc.sub_cuenta AS subcuenta, " +
			"sc.convenio AS convenio, " +
			"sc.ingreso AS ingreso," +
			"c.nombre AS descripcionconvenio, " +
			"c.tipo_regimen AS codigotiporegimen, " +
			"sc.contrato AS contrato, " +					
			"sc.codigo_paciente AS codigopaciente," +
			"sc.facturado AS facturado," +
			//"(SELECT list(fac.consecutivo_factura||' ') FROM facturas fac WHERE fac.sub_cuenta=sc.sub_cuenta AND fac.estado_facturacion='"+ConstantesBD.codigoEstadoFacturacionFacturada+"' ) AS numerofactura, " +
			"(SELECT sin_contrato from contratos where codigo=sc.contrato) AS sinContrato, " +
			"(SELECT controla_anticipos from contratos where codigo=sc.contrato) AS controlAnticipos " +			
			"FROM sub_cuentas sc " +
			"INNER JOIN convenios c ON (c.codigo=sc.convenio) " +			
			"WHERE ingreso=? ";
	
	
	/**
	 * Cadena de Consulta de Requisitos para el Ingreso y el Egreso del Convenio
	 * */
	private static final String strCadenaConsultaRequisitosConvenio = "SELECT " +
			"rps.requisito_paciente  AS requisitopaciente," +
			"rp.descripcion AS descripcion," +
			"rps.subcuenta AS subcuenta," +						
			"CASE WHEN rps.cumplido  =  "+ValoresPorDefecto.getValorTrueParaConsultas()+" THEN " +
			"'"+ConstantesBD.acronimoSi+"' ELSE '"+ConstantesBD.acronimoNo+"' END AS cumplido, " +
			"CASE WHEN rps.cumplido  =  "+ValoresPorDefecto.getValorTrueParaConsultas()+" THEN " +
			"'"+ConstantesBD.acronimoSi+"' ELSE '"+ConstantesBD.acronimoNo+"' END AS cumplidoold, " +
			"rp.tipo_requisito AS tiporequisito " +
			"FROM requisitos_pac_subcuenta rps " +			
			"INNER JOIN requisitos_paciente rp ON (rp.codigo=rps.requisito_paciente AND rp.tipo_requisito=?) "+
			"WHERE rps.subcuenta=? " ;
	
	
	/**
	 * Cadena de Actualizacion para los requisitos
	 * */
	private static final String strCadenaActualizacionRequisitosConvenio = "UPDATE " +
			"requisitos_pac_subcuenta " +
			"SET cumplido=? " +
			"WHERE " +
			"requisito_paciente=? AND subcuenta=? ";
	
	
	
	
	
			
	
	/**
	 * Cadena de consulta de estados solicitudes historia clinica 
	 * */
	private static final String strCadenaListadoEstadosHistoriaClinica = "SELECT " +
			"codigo AS codigo," +
			"nombre AS nombre " +
			"FROM estados_sol_his_cli ";
	
	/**
	 * Cadena de consulta de tipos de solicitudes 
	 * */
	private static final String strCadenaListadoTiposSolicitudes = "SELECT " +
			"codigo AS codigo," +
			"nombre AS nombre " +
			"FROM tipos_solicitud ";
	
	/**
	 * Cadena de consulta de estados solicitudes historia clinica 
	 * */
	private static final String strCadenaListadoEstadosSolFact = "SELECT " +
			"codigo AS codigo," +
			"nombre AS nombre " +
			"FROM estados_sol_fact ";
	

	
	/**
	 * Cadena de Consulta del Detalle   
	 * */
	private static final String strCadenadetallesolicitud =			
			" codigo_detalle_cargo AS codigodetallecargo," +
			"sub_cuenta AS subcuenta, " +
			"convenio AS convenio," +
			//"nro_autorizacion AS nroautorizacion, " +
			//"nro_autorizacion AS nroautorizacionold, " +
			//Modificado por la Tarea 49579
			"servicio AS servicio, " +
			"getcodigopropservicio2(servicio, "+ConstantesBD.codigoTarifarioCups+") AS codservicio, " +
			"articulo AS articulo, " +						
			"getnombreservicio(servicio,"+ConstantesBD.codigoTarifarioCups+") AS nombreservicio, " +
			"getdescarticulo(articulo) AS nombrearticulo, " +
			"to_char(fecha_modifica, 'DD/MM/YYYY') AS fechamodifica," +
			"estado AS estado," +
			"estado AS estadoold," +
			"facturado AS facturado," +
			
			"CASE WHEN articulo IS NULL THEN " +
			"	getnumerofaccargo(solicitud,servicio,'ser') " +
			"ELSE" +
			"   getnumerofaccargo(solicitud,articulo,'art') " +
			"END AS numerofactura," +
			
			"getestadosolfac(estado) AS nombreestado," +
			"cantidad_cargada AS cantidadcargada," +
			"valor_unitario_cargado AS valorunitariocargado," +
			"valor_unitario_cargado AS valorunitariocargadoold," +
			"coalesce(valor_total_cargado,0) AS valortotalcargado," +
			
			"CASE WHEN articulo IS NULL THEN " +
			"	getnumrespsolservicio(solicitud,servicio) " +
			"ELSE " +
			"	getnumrespsolarticulo(solicitud,articulo) " +
			"END AS cantidaddistribucion, " +
			
			"servicio_cx AS serviciocx, " +			
			"CASE WHEN servicio_cx IS NULL THEN " +
			"	' ' " +
			"ELSE " +
			"	getnombreservicio(servicio_cx,"+ConstantesBD.codigoTarifarioCups+") " +
			"END AS nombreserviciocx, " +
			
			"tipo_distribucion AS tipodistribucion," +
			"paquetizado AS paquetizado," +
			"cargo_padre AS cargopadre " +
			"FROM det_cargos " +
			"WHERE sub_cuenta = ? AND solicitud = ? ";
	
	
	/**
	 * Cadena de Consulta del detalle de la Cirugia
	 * */
	private static final String strCadenaDetalleCirugia=
			" dc.codigo_detalle_cargo AS codigodetallecargo," +
			"dc.sub_cuenta AS subcuenta, " +
			"dc.convenio AS convenio," +
			//"dc.nro_autorizacion AS nroautorizacion, " +
			//"dc.nro_autorizacion AS nroautorizacionold, " +
			
			"coalesce(dc.servicio,dc.articulo) AS servicio," +
			//Modificado por la Tarea 49579
			"CASE WHEN dc.servicio IS NULL THEN dc.articulo||' ' ELSE getcodigopropservicio2(dc.servicio,"+ConstantesBD.codigoTarifarioCups+") END AS codservicio," +
			
			"coalesce(getnombreservicio(dc.servicio,"+ConstantesBD.codigoTarifarioCups+"),getdescarticulo(dc.articulo)) AS nombreservicio, " +			
			"to_char(dc.fecha_modifica, 'DD/MM/YYYY') AS fechamodifica," +
			
			"dc.estado AS estado," +
			"dc.estado AS estadoold," +
			
			"getestadosolfac(dc.estado) AS nombreestado," +
			"facturado AS facturado, " +
			//Si el artículo es nulo quiere decir que es un asocio normal de la cirugia,
			//de lo contrario es un material especial
			"CASE WHEN dc.articulo is null then getnumerofaccargo(dc.solicitud,dc.servicio,'cir') else getnumerofaccargo(dc.solicitud,dc.articulo,'art') END AS numerofactura," +						
			
			"dc.cantidad_cargada AS cantidadcargada," +
			"dc.valor_unitario_cargado AS valorunitariocargado," +
			"dc.valor_unitario_cargado AS valorunitariocargadoold," +
			"coalesce(dc.valor_total_cargado,0) AS valortotalcargado," +
			
			"CASE WHEN dc.articulo IS NULL THEN " +
			"	getnumrespsolservicio(dc.solicitud,dc.servicio) " +
			"ELSE " +
			"	getnumrespsolarticulo(dc.solicitud,dc.articulo) " +
			"END AS cantidaddistribucion, " +
			
			"coalesce(dc.servicio_cx,"+ConstantesBD.codigoNuncaValido+") AS serviciocx," +
			"coalesce(dc.tipo_asocio,"+ConstantesBD.codigoNuncaValido+") AS tipoasocio, " +
			"coalesce(tas.codigo_asocio,' ') AS acronimotiposerv," +
			"coalesce(getnombretipoasocio(tas.codigo),' ') AS nombretipoasocio, " +
			
			"coalesce(getnombreservicio(dc.servicio_cx,"+ConstantesBD.codigoTarifarioCups+"),' ') AS nombreserviciocx, " +
			
			"dc.tipo_distribucion AS tipodistribucion," +
			"dc.paquetizado AS paquetizado," +
			"dc.cargo_padre AS cargopadre " +
			"FROM det_cargos dc " +
			"LEFT OUTER JOIN tipos_asocio tas ON (tas.codigo = dc.tipo_asocio) " +
			"LEFT OUTER JOIN sol_cirugia_por_servicio solci ON (solci.numero_solicitud=dc.solicitud AND solci.servicio=dc.servicio_cx) " +
			"WHERE dc.sub_cuenta = ? AND  dc.solicitud = ? " ;
			
	
	
	/**
	 * Cadena de actualizacion del detalle de la solicitud
	 * */
	private static final String strCadenaActualizacionDetalleSolicitud ="UPDATE det_cargos " +
			//"SET nro_autorizacion   = ?, " +			
			"SET valor_unitario_cargado = ?, " +
			"valor_total_cargado    = ?, " +
			"estado = ? "+
			"WHERE codigo_detalle_cargo = ? ";
	
	
	/**
	 * Indice del Mapa de Consulta de Responsables 
	 * */
	private static final String [] indices_responsables = {"subcuenta_","convenio_","ingreso_","descripcionconvenio_","contrato_","codigopaciente_","facturado_","numerofactura_","codigotiporegimen_","sincontrato_","controlanticipos_"};
	
	/**
	 * Indice del Mapa de Requisitos
	 * */
	private static final String [] indices_requisitos = {"requisitopaciente_","descripcion_","subcuenta_","cumplido_","tiporequisito_"};
	
	/**
	 * Indice del Mapa de Solicitudes por Responsables
	 * */
	private static final String [] indices_solicitudesXResponsable = {"codigo_","indicativo_","consecutivoordenesmedicas_","numerosolicitud_","tiposolicitud_","nombresolicitud_","servicio_","codigoportatil_","articulo_","indicadorservart_","descripcionservarticulo_","viaingreso_","nombreviaingreso_","pyp_","fechasolicitud_","estadohc_","nombreestadohc_","serviciocx_","nombreserviciocx_","horasolicitud_","fecharespuesta_","horarespuesta_","nombreestadocargo_","valortotal_"};
	
	/**
	 * Indice del Mapa de Detalle de solicitudes
	 * */
	private static final String [] indices_solicitudesDetalle = {"codigodetallecargo_","subcuenta_","convenio_",/*"nroautorizacion_","nroautorizacionold_",*/"servicio_","nombreservicio_","codigoportatil_","nombreservicioportatil_","nombrearticulo_","fechamodifica_","estado_","estadoold_","facturado_","numerofactura_","nombreestado_","cantidadcargada_","valorunitariocargado_","valorunitariocargadoold_","valortotalcargado_","cantidaddistribucion_","serviciocx_","nombreserviciocx_","tipodistribucion_","paquetizado_","cargopadre_"};
	
	/**
	 * Indice del Mapa de Detalla de Cirugias
	 * */
	private static final String [] indices_solicitudesCirugia = {"codigodetallecargo_","subcuenta_","convenio_",/*"nroautorizacion_","nroautorizacionold_",*/"servicio_","nombreservicio_","codigoportatil_","nombreservicioportatil_","fechamodifica_","estado_","estadoold_","nombreestado_","cantidadcargada_","valorunitariocargado_","valorunitariocargadoold_","valortotalcargado_","cantidaddistribucion_","serviciocx_","tipoasocio_","acronimotiposerv_","nombretipoasocio_","nombreserviciocx_","tipodistribucion_","paquetizado_",	"cargopadre_"};
	
	
	
	//************************************************************************************
	//*****************************Atributos Pool*****************************************
	
	
	/**
	 * Cadena de Consulta de Pooles
	 * */
	private static String strCadenaListadoPooles ="SELECT " +
			"s.consecutivo_ordenes_medicas AS consecutivoordenesmedicas," +
			"s.numero_solicitud AS numerosolicitud," +
			"facturacion.getCodigoSolicitud(s.numero_solicitud) AS codigosolicitud, " +
			"s.tipo AS tiposolicitud," +
			"s.cuenta AS cuenta," +
			"getnombreviaingreso(getviaingresocuenta(s.cuenta)) AS viaingreso," +
			"to_char(s.fecha_solicitud,'DD/MM/YYYY') AS fechasolicitud, " +
			"to_char(getfecharespuestasol(s.numero_solicitud,s.tipo),'DD/MM/YYYY"+ConstantesBD.separadorSplit+"HH24:MI') AS fecharespuesta, " +			
			"s.codigo_medico_responde AS codigomedicoresponde," +
			"s.datos_medico_responde AS datosmedicoresponde," +
			"getCuantosPoolesMedico(s.codigo_medico_responde) AS cuantospoolesmedico," +
			"getserviciosolicitud(s.numero_solicitud,s.tipo) || ' ' || getnombreservicio(getserviciosolicitud(s.numero_solicitud,s.tipo),"+ConstantesBD.codigoTarifarioCups+") AS descripcionservicio," +
			"s.pool AS pool," +
			"getdescripcionpool(s.pool) AS nombrepool," +
			"CASE WHEN s.tipo = 15 OR s.tipo = 14 THEN " +
			"	'"+ConstantesBD.acronimoSi+"' " +
			"ELSE " +
			"	'"+ConstantesBD.acronimoNo+"' " +
			"END AS tipodetalle, " +				
			"getnumrespsolicitud(s.numero_solicitud) AS numeropsolicitud," +
			"s.estado_historia_clinica AS estadohistoriaclinica " +			
			"FROM ordenes.solicitudes s ";			
	
	
	/**
	 * Cadena de consulta de Pooles por medico 	 
	 * */
	private static String strCadenaListadoPoolesXMedico = "SELECT " +
			"pp.pool AS pool, " +
			"getdescripcionpool(pp.pool) AS nombrepool," +
			"to_char(pp.fecha_ingreso,'DD/MM/YYYY') AS fechaingreso," +
			"pp.hora_ingreso AS horaingreso," +
			"to_char(pp.fecha_retiro,'DD/MM/YYYY') AS fecharetiro,  " +
			"pp.hora_retiro AS horaretiro  " +
			"FROM participaciones_pooles pp " +
			"INNER JOIN pooles pol ON (pol.codigo = pp.pool)   " +
			"WHERE pp.medico = ? AND pp.fecha_ingreso<= ? AND  pol.activo = 1";
	
	/**
	 * Cadena de modificacion de Pooles por medico diferentes de tipo de solicitud Cirugia
	 * */
	private static String strCadenaModificacionPoolesMedico ="UPDATE solicitudes " +
			"set pool = ? " +
			"WHERE " +
			"numero_solicitud=? ";
	
	
	
	/**
	 * Cadena de Consulta de Pooles Pendientes de Ciruguias 
	 * */
	private static String strCadenaCirugiaPool = "SELECT " +
			"sol.numero_solicitud AS numerosolicitud, " +
			"consecutivo_ordenes_medicas AS consecutivoordenesmedicas," +
			"det.codigo AS codigocxhonorarios,  "+
			"to_char(sol.fecha_solicitud,'DD/MM/YYYY') AS fechasolicitud, "+ 
			"to_char(getfecharespuestasol(sol.numero_solicitud,sol.tipo),'DD/MM/YYYY@@@@@HH24:MI') AS fecharespuesta, "+ 
			"det.medico AS codigomedicoresponde, "+ 
			"getnombremedico(det.medico) AS datosmedicoresponde, "+
			"det.tipo_asocio AS tipoasocio, "+  
			"tas.codigo_asocio AS acronimotiposerv, "+
			"getnombretipoasocio(tas.codigo) AS nombretipoasocio, "+
			"solci.servicio AS serviciocx, "+
			"getnombreservicio(solci.servicio,"+ConstantesBD.codigoTarifarioCups+") AS nombreserviciocx "+
			"FROM solicitudes sol "+ 
			"INNER JOIN sol_cirugia_por_servicio solci ON (solci.numero_solicitud=sol.numero_solicitud) "+ 
			"INNER JOIN det_cx_honorarios det ON (det.cod_sol_cx_servicio = solci.codigo AND det.pool IS NULL) "+ 
			"INNER JOIN tipos_asocio tas ON (tas.codigo = det.tipo_asocio) "+
			"WHERE sol.numero_solicitud=? AND getespaquetizadoasocio(sol.numero_solicitud,solci.servicio,det.servicio) = ? and det.cobrable = '"+ConstantesBD.acronimoSi+"' ";		

	
	/**
	 * Cadena de Actualizacion de Pooles Para la Cirugia
	 * */
	private static String strCadenaModificacionPoolCirugia = "UPDATE det_cx_honorarios " +
			"set pool = ? " +
			"WHERE " +
			"codigo = ? ";
		
	
	/**
	 * Indice del Mapa de Listado de POOL
	 * */
	private static String [] indices_listadoPooles = {"consecutivoordenesmedicas_","numerosolicitud_","codigosolicitud_","tiposolicitud_","cuenta_","viaingreso_","fechasolicitud_","fecharespuesta_","codigomedicoresponde_","datosmedicoresponde_","cuantospoolesmedico_","descripcionservicio_","pool_","nombrepool_","tipodetalle_","numeropsolicitud_"};
	
	
	
	private static String strCadenaModificarEstadoCuenta = " UPDATE det_cargos SET estado=? " +
															"where solicitud =?   AND " +
															"paquetizado = 'N' " +
															"AND (estado=3 OR estado=5) " +
															"AND facturado='N' and eliminado='N' ";
	
	
	private static String strCadenaConsultaDetalleSolicitud = "SELECT codigo_detalle_cargo, estado " +
																"from det_cargos where solicitud=? " +
																"and paquetizado = 'N' and facturado='N' and eliminado='N' ";
	
	//*******************************************************************************
	//******************************Metodos******************************************
	
	/**
	 * Consulta los responsables (convenios) de la subcuenta
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public static HashMap consultarResponsables(Connection con,
												HashMap parametros)	
	{
		HashMap mapa = new HashMap();
		
		try		
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(strCadenaConsultaResponsables,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1,Utilidades.convertirAEntero(parametros.get("ingreso").toString()));			
			mapa = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
			logger.info("===>Consulta Responsables: "+strCadenaConsultaResponsables+" ===>Ingreso: "+parametros.get("ingreso"));
			
			for(int w=0; w<Utilidades.convertirAEntero(mapa.get("numRegistros")+""); w++)
			{
				mapa.put("numerofactura_"+w, obtenerConsecutivosFactXSubCuenta(con, mapa.get("subcuenta_"+w)+""));
			}
			
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		
		mapa.put("INDICES_MAP",indices_responsables);
		return mapa;
	}
	
	/**
	 * 
	 * @param con
	 * @param subCuenta
	 * @return
	 */
	private static String obtenerConsecutivosFactXSubCuenta(Connection con, String subCuenta)
	{
		String consulta= "SELECT fac.consecutivo_factura||' ' FROM facturas fac WHERE fac.sub_cuenta="+Utilidades.convertirALong(subCuenta)+" AND fac.estado_facturacion='"+ConstantesBD.codigoEstadoFacturacionFacturada+"' ";
		logger.info("consulta-->"+consulta);
		String retorna="";
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs= new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			{
				retorna+= rs.getString(1);
				if(!rs.isLast())
					retorna+=" ,";
			}
			 
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		logger.info("retorna->"+retorna);
		return retorna;
	}
	
	
	/**
	 * Consulta los requisitos de ingreso y egreso por convenio
	 * @param Connection con
	 * @param HashMap parametros 
	 * */
	public static HashMap consultarRequisitosConvenio(Connection con,
													HashMap parametros)
	{
		HashMap mapa = new HashMap();
		
		try		
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(strCadenaConsultaRequisitosConvenio,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1,parametros.get("tiporequisito").toString());
			ps.setInt(2,Utilidades.convertirAEntero(parametros.get("subcuenta").toString()));
			
			mapa = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		
		mapa.put("INDICES_MAP",indices_requisitos);
		return mapa;
	}

	
	/**
	 * Actualiza los requisitos de ingreso y egreso por convenio
	 * @param Connection con
	 * @param String requisitopaciente
	 * @param String subcuenta
	 * @param String cumplido
	 * */
	
	public static boolean actualizarRequisitosConvenio(Connection con,
													   String requisitopaciente,
													   String subcuenta,
											  		   String cumplido)
	{
		HashMap mapa = new HashMap();
		
		try		
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(strCadenaActualizacionRequisitosConvenio,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			ps.setBoolean(1,UtilidadTexto.getBoolean(cumplido));
			ps.setInt(2,Utilidades.convertirAEntero(requisitopaciente));
			ps.setInt(3,Utilidades.convertirAEntero(subcuenta));
			
			if(ps.executeUpdate()>0)
				return true;			
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		
			
		return false;
	}	
	
	
	/**
	 * Consulta de las solicitudes por el Responsable
	 * @param Connection con
	 * @param HashMap parametros 
	 * */
	public static HashMap consultarListadoSolicitudes(Connection con,
														HashMap parametros,String strCadenaListadoConsultaSolicitudesXResponsable,String strCadenaSolicitudesXResponsableSinCargo)
	{
		HashMap mapa = new HashMap();
		String cadenaW = ""; 
		String cadenaA = "";		
		String cadenaW1 = ""; 
		String cadenaA1 = "";
		boolean indicador = true;
		
		
		if(!parametros.get("where").toString().equals(""))
		{
			
			//Filtros para solicitud tipo cirugia sin cargo
			if(!parametros.containsKey("tiposolicitud") ||
					(parametros.containsKey("tiposolicitud") && parametros.get("tiposolicitud").toString().equals(ConstantesBD.codigoTipoSolicitudCirugia+"")))
			{
				if(parametros.containsKey("numerosolicitud"))
					cadenaA1 += " AND sol.numero_solicitud="+parametros.get("numerosolicitud");					
				
				
				if(parametros.containsKey("fechasolicitudinicial") && parametros.containsKey("fechasolicitudfinal"))					
					cadenaA1 += " AND sol.fecha_solicitud BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(parametros.get("fechasolicitudinicial").toString())+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(parametros.get("fechasolicitudfinal").toString())+"'";					
							
				if(parametros.containsKey("estadohc"))
					cadenaA1 += " AND sol.estado_historia_clinica="+parametros.get("estadohc");								
			}
			else			
				indicador = false;			
			
			cadenaW1 = strCadenaSolicitudesXResponsableSinCargo + cadenaW1 + cadenaA1;
			
			//Filtros para la cosulta de solicitudes con cargo
			if(parametros.containsKey("numerosolicitud"))
			{//FIXME se quita validaciones ya que el where ya se encuentra en la consulta y por eso no funcionaba la busqueda, Y si trae
				//parametros de busqueda debe evaluarlos todos, por tal razon se comentararon los if's
				
				if(parametros.get("where").toString().equals("numerosolicitud"))
					/*cadenaW += " WHERE numero_solicitud="+parametros.get("numerosolicitud");
				else*/
					cadenaA += " AND numero_solicitud="+parametros.get("numerosolicitud");					
			}
			
			
			if(parametros.containsKey("tiposolicitud"))
			{
				if(parametros.get("where").toString().equals("tiposolicitud"))
					cadenaW += " WHERE tipo_solicitud="+parametros.get("tiposolicitud").toString();
				else
					cadenaA += " AND tipo_solicitud="+parametros.get("tiposolicitud").toString();					
			}
			
			
			if(parametros.containsKey("fechasolicitudinicial") && parametros.containsKey("fechasolicitudfinal"))
			{
				if(parametros.get("where").toString().equals("fechasolicitudinicial"))
					cadenaW += " WHERE fecha_solicitud='"+UtilidadFecha.conversionFormatoFechaABD(parametros.get("fechasolicitud")+"")+"'";
				else
					cadenaA += " AND fecha_solicitud BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(parametros.get("fechasolicitudinicial").toString())+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(parametros.get("fechasolicitudfinal").toString())+"'";					
			}				
						
			if(parametros.containsKey("estadohc"))
			{
				if(parametros.get("where").toString().equals("estadohc"))
					cadenaW += " WHERE estado_hc="+parametros.get("estadohc");
				else
					cadenaA += " AND estado_historia_clinica="+parametros.get("estadohc");					
			}
			
			if(parametros.containsKey("solsubcuentapadre"))
			{
				if(parametros.get("where").toString().equals("solsubcuentapadre"))
					cadenaW += " WHERE convertiranumero(sol_subcuenta_padre)="+parametros.get("solsubcuentapadre").toString();
				else
					cadenaA += " AND convertiranumero(sol_subcuenta_padre)="+parametros.get("solsubcuentapadre").toString();
			}
			
			cadenaW = strCadenaListadoConsultaSolicitudesXResponsable + cadenaW + cadenaA;
		}	
		else
		{
			cadenaW = strCadenaListadoConsultaSolicitudesXResponsable;
			cadenaW1 = strCadenaSolicitudesXResponsableSinCargo;
		}
		
		
		
		
		try		
		{		
			if(indicador)		
				cadenaW = "SELECT * FROM (("+cadenaW1+"  ) UNION ("+cadenaW+" group by consecutivo_ordenes_medicas,ssb.sub_cuenta,so.numero_solicitud,pyp,tipo,via_ingreso,fecha_solicitud,estado_historia_clinica,paquetizada )) s " ;			
		
			logger.info("88888888888888888888888888888888888888888888888");
			logger.info("Sql: " + cadenaW);
			
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadenaW,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			mapa = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
			for(int i=0;i<Utilidades.convertirAEntero(mapa.get("numRegistros")+"");i++)
			{
				String subCon="select case when servicio is null then articulo || ' - ' || getdescripcionarticulo(articulo) else getcodigopropservicio2(servicio, 0)|| ' - ' || getnombreservicio(servicio,0) end AS descripcionservarticulo from solicitudes_subcuenta where solicitud="+mapa.get("numerosolicitud_"+i);
				PreparedStatementDecorator psTempo=new PreparedStatementDecorator(con,subCon);
				ResultSetDecorator rsTempo=new ResultSetDecorator(psTempo.executeQuery());
				rsTempo.last();
				if(rsTempo.getRow()==1)
				{
					rsTempo.first();
					mapa.put("descripcionservarticulo_"+i, rsTempo.getString("descripcionservarticulo"));
				}				
				rsTempo.close();
				psTempo.close();
			}
			ps.close();
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		logger.info("\n===>Consulta: "+cadenaW);
		logger.info("\n===>Ingreso: "+parametros.get("ingreso")+" ===>Subcuenta: "+parametros.get("subcuenta")+" ===>Mostrar Indicativo: "+parametros.get("mostrarIndicativo")+" ===>Es Paquetizada: "+parametros.get("espaquetizada"));
		mapa.put("INDICES_MAP",indices_solicitudesXResponsable);
		return mapa;
	}
			
	
	
	
	/**
	 * Consulta los estados de la solicitud en la historia clinica 
	 * @param Connection con  
	 * */
	public static HashMap consultarEstadosSolicitudHistoriaC(Connection con)
	{
		HashMap mapa = new HashMap();
		
		try		
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(strCadenaListadoEstadosHistoriaClinica,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));			
			mapa = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		
		return mapa;
	}
	
	
	/**
	 * Consultar los tipos de solicitud 
	 * @param Connection con  
	 * */
	public static HashMap consultarTiposSolicitud(Connection con)
	{
		HashMap mapa = new HashMap();
		
		try		
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(strCadenaListadoTiposSolicitudes,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));			
			mapa = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		
		return mapa;
	}
	
	
	/**
	 * Consultar Detalle de la solicitud   
	 * @param Connection con  
	 * @param HashMap parametros
	 * */
	public static HashMap consultarDetalleSolicitud(Connection con, 
													HashMap parametros)
	{
		HashMap mapa = new HashMap();
		String cadena = strCadenadetallesolicitud;
		
		
		logger.info("valor mapa Consultar detalle solicitud >> "+parametros);
		
		//se evalua si se muestra o no el campo indicativo
		if(parametros.get("hayIndicativo").toString().equals(ConstantesBD.acronimoSi))
		{
			//Se quita un parametro de la funcion por error en la consulta Tarea Xplanner 19586
			cadena = "SELECT facturacion.getIndicativoSolicitudes("+parametros.get("subcuenta").toString()+","+parametros.get("numerosolicitud").toString()+",'"+ConstantesBD.acronimoNo+"') AS indicativo, "+cadena;
			//cadena = "SELECT facturacion.getIndicativoSolicitudes("+parametros.get("subcuenta").toString()+","+parametros.get("numerosolicitud").toString()+",coalesce(servicio,articulo)) AS indicativo, "+cadena;
		}
		else
		{
			cadena = "SELECT "+strCadenadetallesolicitud;
		}
		
		
		if(parametros.containsKey("paquetizado"))		
			cadena += " AND paquetizado = '"+parametros.get("paquetizado").toString()+"'";		
		
		try		
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setLong(1,Utilidades.convertirALong(parametros.get("subcuenta").toString()));
			ps.setInt(2,Utilidades.convertirAEntero(parametros.get("numerosolicitud").toString()));	
			
			logger.info("\n\n\nCadena Detalle Solicitud: "+cadena);
			logger.info("\n\n\nSubcuenta: "+parametros.get("subcuenta"));
			logger.info("\n\n\nNumero Solicitud: "+parametros.get("numerosolicitud"));
			
			mapa = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		
		mapa.put("INDICES_MAP",indices_solicitudesDetalle);
		return mapa;
	}
	
	
	
	/**
	 * Consultar Detalle de la Cirugia  
	 * @param Connection con  
	 * @param HashMap parametros
	 * */
	public static HashMap consultarDetalleCirugia(Connection con, 
												  HashMap parametros)
	{
		HashMap mapa = new HashMap();
		String  cadena = strCadenaDetalleCirugia;		
		
		//se evalua si se muestra o no el campo indicativo
		if(parametros.get("hayIndicativo").toString().equals(ConstantesBD.acronimoSi))		
			cadena = "SELECT getindicativosolicitudes("+parametros.get("subcuenta").toString()+","+parametros.get("numerosolicitud").toString()+",'"+ConstantesBD.acronimoNo+"') AS indicativo, "+
							"getIndicatSolicitudesServArt("+parametros.get("subcuenta").toString()+","+parametros.get("numerosolicitud").toString()+",'"+ConstantesBD.acronimoNo+"',coalesce(dc.servicio,0),coalesce(dc.articulo,0)) AS indicativoservart, "+
						    ""+cadena;		
		else
			cadena = "SELECT "+strCadenadetallesolicitud;		
		
		if(parametros.containsKey("paquetizado"))		
			cadena += " AND dc.paquetizado = '"+parametros.get("paquetizado").toString()+"'";
		
		cadena+=" ORDER BY solci.consecutivo DESC, dc.tipo_asocio DESC ";

		logger.info("===>Consulta: "+cadena);
		logger.info("info parametros cirugia >> "+parametros);
		
		try		
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setLong(1,Utilidades.convertirALong(parametros.get("subcuenta").toString()));
			ps.setInt(2,Utilidades.convertirAEntero(parametros.get("numerosolicitud").toString()));	
			
			
			mapa = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		
		mapa.put("INDICES_MAP",indices_solicitudesCirugia);
		return mapa;
	}
	
		
	
	
	/**
	 * Actualiza los registros del detalle de la solicitud
	 * @param Connection con
	 * @param HashMap parametros 
	 * */
	public static boolean actualizarDetalleSolicitud(Connection con,
													 HashMap parametros)	
	{		
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(strCadenaActualizacionDetalleSolicitud,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));		
			
			/**
			 * UPDATE det_cargos " +
			"SET nro_autorizacion   = ?, " +			
			"valor_unitario_cargado = ?, " +
			"valor_total_cargado    = ?, " +
			"estado = ? "+
			"WHERE codigo_detalle_cargo = ? 
			 */
			
			//ps.setString(1,parametros.get("nroautorizacion").toString());
			
			if(!parametros.get("valorunitariocargado").toString().equals(""))
				ps.setDouble(1,Utilidades.convertirADouble(parametros.get("valorunitariocargado").toString()));
			else
				ps.setNull(1,Types.NULL);
			
			if(!parametros.get("valortotalcargado").toString().equals("") && !parametros.get("valortotalcargado").toString().equals("0"))
				ps.setDouble(2,Utilidades.convertirADouble(parametros.get("valortotalcargado").toString()));
			else
				ps.setNull(2,Types.NULL);			
			
			ps.setInt(3,Utilidades.convertirAEntero(parametros.get("estado").toString()));
			ps.setDouble(4,Utilidades.convertirADouble(parametros.get("codigodetallecargo").toString()));			
			
			if(ps.executeUpdate()>0)
				return true;
		}
		catch(SQLException e)
		{
			e.printStackTrace();			
		}		
		
		return false;
	}
	
	
	//**************************************************************************************
	//******************************Metodos POOLES******************************************
	
	/**
	 * Consultar el listado de Pooles pendientes  
	 * @param Connection con  
	 * @param HashMap parametros
	 * */
	public static HashMap consultarListadoPooles(Connection con, 
												 HashMap parametros)
	{
		HashMap mapa = new HashMap();	
		String cadena = strCadenaListadoPooles;
		String inner=" LEFT OUTER JOIN consultaexterna.servicios_cita sc ON (sc.numero_solicitud=s.numero_solicitud) ";
		try		
		{
			//Si la consulta se hace para revisión de cuenta, entonces 
			if (parametros.containsKey("filtrarRevisionCuenta"))
				cadena+=inner;
			
			cadena += "WHERE s.cuenta IN("+parametros.get("cuenta").toString()+") ";
			
			//Se omite esta validacion para la consulta porque se requiere que muestre todo
			if (!parametros.containsKey("filtrarRevisionCuenta"))
			{
			
				cadena+=	"AND (s.estado_historia_clinica = 2 " +
							"OR s.estado_historia_clinica = 3 "+
							"OR s.estado_historia_clinica = 9 " +
							"OR s.estado_historia_clinica = 7) ";
			}
				
			cadena+=	"AND s.tipo!=6 " +
						"AND s.tipo!=9 " +
						"AND s.tipo!=13 "+		
						"AND getcuantospoolpendsolicitud(s.numero_solicitud,s.tipo,'"+parametros.get("espaquetizada").toString()+"',"+parametros.get("solsubcuentapadre").toString()+") > 0 ";
			
			//Agregado por tarea  143743
			cadena+=	"AND s.codigo_medico_responde IS NOT NULL ";
			
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					
			logger.info("valor de la cadena >> "+cadena);
			mapa = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		
		mapa.put("INDICES_MAP",indices_listadoPooles);
		return mapa;
	}	
	
	
	
	/**
	 * Verifica la existencia de pooles pendientes  
	 * @param Connection con  
	 * @param HashMap parametros
	 * */
	public static int verificarPoolesPendientes(Connection con, HashMap parametros)
	{			
		//Cambio tarea 143743 - Se agrega la excepcion cuando no existe el medico responde para no mostrar el link
		String consulta1="SELECT COUNT(numero_solicitud) FROM solicitudes s WHERE cuenta IN("+parametros.get("cuenta").toString()+")AND tipo <> 6 AND tipo <> 9 AND tipo <> 13 AND tipo <> 14 AND tipo <> 15 AND (estado_historia_clinica = 1 OR estado_historia_clinica = 2 OR estado_historia_clinica = 3 OR estado_historia_clinica = 9 OR estado_historia_clinica = 7) AND pool IS NULL AND codigo_medico_responde IS NOT NULL";
		String consulta2="SELECT COUNT(1) FROM det_cx_honorarios dh INNER JOIN solicitudes sol ON (sol.cuenta IN ("+parametros.get("cuenta").toString()+") AND sol.tipo = 14 AND (sol.estado_historia_clinica = 2 OR sol.estado_historia_clinica = 3 OR sol.estado_historia_clinica = 9  OR estado_historia_clinica = 7) ) INNER JOIN sol_cirugia_por_servicio solc ON(solc.numero_solicitud = sol.numero_solicitud) WHERE dh.cod_sol_cx_servicio=solc.codigo AND dh.pool IS NULL and dh.cobrable = 'S' AND sol.codigo_medico_responde IS NOT NULL";
		
		logger.info("CONSULTA 1----->"+consulta1);
		logger.info("CONSULTA 2----->"+consulta2);
		
		int cantidad=ConstantesBD.codigoNuncaValido;
		try		
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consulta1,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));							
			ResultSetDecorator rs =new ResultSetDecorator(ps.executeQuery());
			if(rs.next())			
				cantidad=rs.getInt(1);
			rs.close();
			ResultSetDecorator rs1 =new ResultSetDecorator(ps.executeQuery());
			ps =  new PreparedStatementDecorator(con.prepareStatement(consulta2,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));							
			rs1 =new ResultSetDecorator(ps.executeQuery());
			if(rs1.next())			
				cantidad=cantidad+rs1.getInt(1);
			rs1.close();
			ps.close();
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}	
		
		return cantidad;
	}
	
	
	/**
	 * Consultar el listado de Pooles para el medico   
	 * @param Connection con  
	 * @param HashMap parametros
	 * */
	public static HashMap consultarListadoPoolesMedico(Connection con, 
												 	   HashMap parametros)
	{
		HashMap mapa = new HashMap();		
		
		try		
		{
			logger.info("LA CONSULTA DE POOLES X MEDICO------>"+strCadenaListadoPoolesXMedico);
			logger.info("El medico-->"+parametros.get("medico")+" La fecha generacion sol"+UtilidadFecha.conversionFormatoFechaABD(parametros.get("fechageneracionsolicitud").toString()));
			
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(strCadenaListadoPoolesXMedico,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
						
			if(!parametros.get("medico").toString().equals(""))
				ps.setInt(1,Utilidades.convertirAEntero(parametros.get("medico").toString()));
			else
				ps.setInt(1,0);
			
			if(!parametros.get("fechageneracionsolicitud").toString().equals(""))
				ps.setString(2,UtilidadFecha.conversionFormatoFechaABD(parametros.get("fechageneracionsolicitud").toString()));
			else
				ps.setString(2,UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
			
			mapa = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}	
		
		return mapa;
	}	
	
	
	
	/**
	 * Actualiza los Pooles por Medico diferentes de tipo Solicitud Cirugia 
	 * @param Connection con
	 * @param HashMap parametros
	 * */	
	public static boolean actualizarPoolesMedico(Connection con,
												 HashMap parametros)
	{			
		try		
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(strCadenaModificacionPoolesMedico,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));			
			ps.setInt(1,Utilidades.convertirAEntero(parametros.get("pool").toString()));
			ps.setInt(2,Utilidades.convertirAEntero(parametros.get("numerosolicitud").toString()));
			
			if(ps.executeUpdate()>0)
				return true;			
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}		
			
		return false;
	}
	
	
	
	
	/**
	 * Consultar Detalle Pooles Pendientes de la Cirugia  
	 * @param Connection con  
	 * @param HashMap parametros
	 * */
	public static HashMap consultarDetalleCirugiaPool(Connection con, 
												  	  HashMap parametros)
	{
		HashMap mapa = new HashMap();
		String cadena = strCadenaCirugiaPool;		
			
		cadena+=" ORDER BY solci.codigo DESC, det.tipo_asocio DESC ";		
		
		try		
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1,Utilidades.convertirAEntero(parametros.get("numerosolicitud").toString()));			
			ps.setString(2,parametros.get("paquetizado").toString());			
			
			mapa = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}		
		
		return mapa;
	}
	
	
	
	/**
	 * Actualiza los Pooles por Medico, tipo Solicitud Cirugia 
	 * @param Connection con
	 * @param HashMap parametros
	 * */	
	public static boolean actualizarPoolesMedicoCirugia(Connection con,
														HashMap parametros)
	{			
		try		
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(strCadenaModificacionPoolCirugia,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));			
			ps.setInt(1,Utilidades.convertirAEntero(parametros.get("pool").toString()));
			ps.setInt(2,Utilidades.convertirAEntero(parametros.get("codigo").toString()));
			
			if(ps.executeUpdate()>0)
				return true;			
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}		
			
		return false;
	}



	/**
	 * Actualiza los Estados de una Solicitud de Excenta a Cargada y Viseversa
	 * @param con
	 * @param nuevoEstadoCargo
	 * @param numeroSolicitud
	 */
	public static boolean actualizarEstadoCargo(Connection con, int nuevoEstadoCargo, String numeroSolicitud) 
	{
		try		
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(strCadenaModificarEstadoCuenta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));			
			ps.setInt(1, nuevoEstadoCargo);
			ps.setString(2, numeroSolicitud);
			
			if(ps.executeUpdate()>0)
				return true;			
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}		
		return false;
	}



	/**
	 * Metodo para consultar todos los detalles de la cuenta y los estados de una solicitud
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public static HashMap detalleEstadoSolicitud(Connection con, String numeroSolicitud) 
	{
		HashMap mapa = new HashMap();
		
		try		
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(strCadenaConsultaDetalleSolicitud,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, Utilidades.convertirAEntero(numeroSolicitud));			
			
			mapa = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}		
		
		return mapa;
	}



	/**
	 * 
	 * @param con
	 * @return
	 */
	public static HashMap consultarEstadosSolFactura(Connection con) 
	{
		HashMap mapa = new HashMap();
		
		try		
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(strCadenaListadoEstadosSolFact,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));			
			mapa = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		
		return mapa;
	}		


	public static void insertarLogEstadoCargo(Connection con, String codigoDetalleCargo, String estadoAnterior, String estadoActual, String loginUsuario, String consulta) 
	{
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setDate(1, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual())));
			ps.setString(2, UtilidadFecha.getHoraActual());
			ps.setString(3, loginUsuario);
			ps.setDouble(4, Utilidades.convertirADouble(codigoDetalleCargo));
			ps.setInt(5, Utilidades.convertirAEntero(estadoAnterior));
			ps.setInt(6, Utilidades.convertirAEntero(estadoActual));
			
			int rs = ps.executeUpdate();
						
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
				
	}			
}