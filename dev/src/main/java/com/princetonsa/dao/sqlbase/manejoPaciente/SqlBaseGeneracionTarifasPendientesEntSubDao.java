package com.princetonsa.dao.sqlbase.manejoPaciente;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.Utilidades;

import com.princetonsa.decorator.ResultSetDecorator;


@SuppressWarnings("unchecked")
public class SqlBaseGeneracionTarifasPendientesEntSubDao
{
	private static Logger logger = Logger.getLogger(SqlBaseGeneracionTarifasPendientesEntSubDao.class);
	
	
	public static HashMap buscarAutorizaciones(Connection con, HashMap filtros)
	{
	
		@SuppressWarnings("unused")
		boolean tieneentidad=false;
		
		HashMap resultadoBusqueda = new HashMap();
		resultadoBusqueda.put("numRegistros", 0);
		
		String consultaAutorizaciones = "";
		
		try {
			//PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaAutorizaciones, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
		
			if (filtros.get("tipotarifa").equals(ConstantesIntegridadDominio.acronimoServicio))
			{
				consultaAutorizaciones = 	"SELECT DISTINCT " +
												"a.consecutivo_autorizacion AS consecutivoaut, " +
												"a.consecutivo AS consecutivo, " +
												"to_char(a.fecha_autorizacion,'yyyy-mm-dd') AS fechaauto, " +
												"a.hora_autorizacion AS horaauto, " +
												"a.entidad_autorizada_sub AS entidad, " +
												"sol.centro_costo_solicitado AS ccsolicitado," +
												"to_char(sol.fecha_solicitud,'yyyy-mm-dd') AS fechasol," +
												"a.codigo_paciente AS paciente," +
												"aess.numero_solicitud AS solicitud, " +
												"ase.servicio AS codservicio, " +
												"getdescentitadsubcontratada(a.entidad_autorizada_sub) AS razonsocialentsub," +
												"getnombrecentroscosto(sol.centro_costo_solicitado) AS nombrecc," +
												"getnombrepersona(a.codigo_paciente) AS nombrepersona," +
												"getidentificacionpaciente(a.codigo_paciente) AS idpaciente," +
												"gettipoid(a.codigo_paciente) AS tipoid," +
												"'' AS autorizacionActivo," +
												"'"+ConstantesBD.acronimoNo+"' AS tarifado "+
											
												"FROM " + 	"autorizaciones_entidades_sub a " +
												
												// PermitirAutorizarDiferenteDeSolicitudes
												"INNER JOIN " + "ordenes.auto_entsub_solicitudes aess ON (aess.autorizacion_ent_sub = a.consecutivo) " + 
												"INNER JOIN " + "solicitudes sol ON (sol.numero_solicitud = aess.numero_solicitud) " + 
												
												"INNER JOIN " + "tarifas_entidad_sub te ON (te.autorizacion=a.consecutivo) " +
												"INNER JOIN " + "autorizaciones_ent_sub_servi ase ON (a.consecutivo = ase.autorizacion_ent_sub) " +	
													
												"WHERE ";
				
				consultaAutorizaciones+=	"(a.estado='"+ConstantesIntegridadDominio.acronimoAutorizado+"' OR "+"a.estado='"+ConstantesIntegridadDominio.acronimoEstadoAnulado+"') AND ";
				//Se sebe eliminar esta restricción por que según el DCU no aplica pero falta definir
				//por parte de análisis el proceso para las INTERNAS
				consultaAutorizaciones+=	"a.tipo='"+ConstantesIntegridadDominio.acronimoExterna+"' AND ";
				consultaAutorizaciones+=	"to_char(a.fecha_autorizacion,'yyyy-mm-dd') BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(filtros.get("fechaini").toString())+"'"+" AND '"+UtilidadFecha.conversionFormatoFechaABD(filtros.get("fechafin").toString())+"' AND ";
				consultaAutorizaciones+=	"te.estado="+ConstantesBD.estadoSolFactPendiente+" AND ";
				
				if (!filtros.get("codigoentidad").toString().equals(ConstantesBD.codigoNuncaValido+""))
				{
						consultaAutorizaciones+=	" a.entidad_autorizada_sub="+filtros.get("codigoentidad").toString()+" AND ";
						tieneentidad=true;
				}
				consultaAutorizaciones+=	"1=1";
			}		
			logger.info("LA CADENA ES-------->"+consultaAutorizaciones);
			
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			resultadoBusqueda = UtilidadBD.cargarValueObject(new ResultSetDecorator(st.executeQuery(consultaAutorizaciones)));
		}
		
		catch (SQLException e) 
		{	
			logger.info("ERROR / consultaAutorizaciones / "+e);

		}
		logger.info("CONSULTÉ LAS AUTORIZACIONES");
		return resultadoBusqueda;
	}
	
	
	public static HashMap generarTarifaServicio(Connection con, String autorizacion)
	{
		HashMap resultadoBusquedaServ = new HashMap();
		resultadoBusquedaServ.put("numRegistros", 0);
		
		String consultaServicios=	"SELECT DISTINCT " +
										"ase.servicio AS servicio," +
										"getcodigocups(ase.servicio,"+ConstantesBD.codigoTarifarioCups+") AS codigocups," +
										"getnombreserviciotarifa(ase.servicio,"+ConstantesBD.codigoTarifarioCups+") AS nombreservicio, " +
										"getesposservicioppal(ase.servicio) AS espos, " +
										"coalesce(s.espossubsidiado,'') AS espossub, " +
										"t.nombre AS codigotarifario " +
									"FROM " + "autorizaciones_entidades_sub a " +
									"INNER JOIN " + "convenios c ON (c.codigo=a.convenio) " +
									"LEFT OUTER JOIN " + "tarifarios_oficiales t ON (t.codigo=c.tipo_codigo) " +
									"INNER JOIN " + "autorizaciones_ent_sub_servi ase ON (a.consecutivo = ase.autorizacion_ent_sub) " +
									"INNER JOIN " + "servicios s ON (s.codigo=ase.servicio) "+									
									
									"WHERE " +
										"a.consecutivo_autorizacion='"+autorizacion+"'";
	
		logger.info("LA CADENA ES-------->"+consultaServicios);
		
		try {
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			resultadoBusquedaServ = UtilidadBD.cargarValueObject(new ResultSetDecorator(st.executeQuery(consultaServicios)));
		}
		catch (SQLException e) 
		{	
			logger.info("ERROR / consultaServicios / "+e);

		}
		logger.info("CONSULTÉ LOS SERVICIOS");
		return resultadoBusquedaServ;
		
	}
	
	public static HashMap obtenerErroresServicioAut(Connection con, String autorizacion)
	{
		HashMap resultadoBusquedaErrores = new HashMap();
		resultadoBusquedaErrores.put("numRegistros", 0);
		
		String consultaErrores=	"SELECT DISTINCT " +
										"ase.servicio AS servicio," +
										"e.error AS descerror "+
								"FROM " +
									"autorizaciones_entidades_sub a " +
								"INNER JOIN " +
									"tarifas_entidad_sub ta ON (ta.autorizacion=a.consecutivo) " +
								"INNER JOIN " +
									"errores_tarifas_ent_sub e ON (e.tarifa_entidad_sub=ta.codigo_detalle_cargo) "+
								"INNER JOIN " + "autorizaciones_ent_sub_servi ase ON (a.consecutivo = ase.autorizacion_ent_sub) " +
								
								"WHERE " +
									"a.consecutivo_autorizacion='"+autorizacion+"'";
	
		logger.info("LA CADENA ES-------->"+consultaErrores);
		
		try {
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			resultadoBusquedaErrores = UtilidadBD.cargarValueObject(new ResultSetDecorator(st.executeQuery(consultaErrores)));
		}
		catch (SQLException e) 
		{	
			logger.info("ERROR / consultaErrores / "+e);

		}
		Utilidades.imprimirMapa(resultadoBusquedaErrores);
		logger.info("CONSULTÉ LOS ERRORES");
		return resultadoBusquedaErrores;
		
	}
	
	@SuppressWarnings({ "unused"})
	public static HashMap buscarArticulosPedidos(Connection con, HashMap filtros)
	{
	
		boolean tieneentidad=false;
		HashMap resultadoPedidos = new HashMap();
		resultadoPedidos.put("numRegistros", 0);
		
		String consultaPedidos = "";
		//CONSULTA DE LOS PEDIDOS
		try {
			
		
			if (filtros.get("tipotarifa").equals(ConstantesIntegridadDominio.acronimoArticulo))
			{
				
				consultaPedidos 	= 	"SELECT DISTINCT " +
												"p.codigo AS codigopedido," +
												"to_char(p.fecha,'yyyy-mm-dd')  AS fechapedido," +
												"p.es_qx AS esqx, " +
												"p.centro_costo_solicitado AS ccsolicitado," +
												"p.centro_costo_solicitante AS ccsolicitante,  " +												
												"te.entidad_subcontratada AS entidad, " +
												"te.codigo_detalle_cargo AS codigodetalle," +
												"p.auto_por_subcontratacion AS autosub, "+//------
												"te.articulo, " +
												"te.art_principal AS artppal, " +
												"te.viene_despacho AS vienedespacho, " +
												"to_char(te.fecha,'yyyy-mm-dd') AS fecha, " +
												"te.hora, " +
												"te.codigo_detalle_cargo AS codigotarifa, " +
												"getnombrecentroscosto(p.centro_costo_solicitado) AS nomccsolicitado, " +
												"getnombrecentroscosto(p.centro_costo_solicitante) AS nomccsolicitante, " +
												"getdescentitadsubcontratada(te.entidad_subcontratada) AS nomentidad, " +
												"getnombrepersona(pqx.paciente) AS nombrepersona," +
												"getidentificacionpaciente(pqx.paciente) AS idpaciente," +
												"gettipoid(pqx.paciente) AS tipoid," +
												"getdescarticulo(te.articulo) AS descarticulo," +
												"getdescarticulo(te.art_principal) AS descarticuloppal," +
												"getunidadmedidaarticulo(te.articulo) AS medidaarticulo, " +
												"getunidadmedidaarticulo(te.art_principal) AS medidaartppal, " +
												"'"+ConstantesBD.acronimoNo+"' AS tarifado "+
											"FROM " +
												"pedido p " +
											"INNER JOIN " +
												"tarifas_entidad_sub te ON(te.pedido=p.codigo) " +
											"LEFT OUTER JOIN " +
												"pedidos_peticiones_qx pp ON (pp.peticion=p.codigo) " +
											"LEFT OUTER JOIN " +
												"peticion_qx pqx ON (pqx.codigo=pp.peticion) " +
											"WHERE ";
				
				consultaPedidos+=	"te.estado="+ConstantesBD.estadoSolFactPendiente+" AND ";
				consultaPedidos+= 	" to_char(p.fecha,'yyyy-mm-dd') BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(filtros.get("fechaini").toString())+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(filtros.get("fechafin").toString())+"' AND ";
				if(Utilidades.convertirAEntero(filtros.get("codigoentidad")+"")>0)
				{
						consultaPedidos+= "te.entidad_subcontratada="+filtros.get("codigoentidad")+" AND " ;
				}
				consultaPedidos+=	"1=1";
			}		
			logger.info("LA CADENA ES-------->"+consultaPedidos);
			
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			resultadoPedidos = UtilidadBD.cargarValueObject(new ResultSetDecorator(st.executeQuery(consultaPedidos)));
			
		}
		
		catch (SQLException e) 
		{	
			logger.info("ERROR / consultaPedidos / "+e);
		}
				
		logger.info("CONSULTÉ LOS PEDIDOS");
		return resultadoPedidos;
	}
	
	@SuppressWarnings({"unused"})
	public static HashMap buscarArticulosSolicitudes(Connection con, HashMap filtros)
	{
	
		boolean tieneentidad=false;
		HashMap resultadoSolicitudes = new HashMap();
		resultadoSolicitudes.put("numRegistros", 0);
		
		String consultaSolicitudes = "";
		
		//CONSULTA DE LAS SOLICITUDES
		try {
			if (filtros.get("tipotarifa").equals(ConstantesIntegridadDominio.acronimoArticulo))
			{
				
				consultaSolicitudes 	= 	"SELECT DISTINCT " +
												"s.numero_solicitud  AS numsolicitud," +
												"to_char(s.fecha_solicitud,'yyyy-mm-dd') AS fechasol, " +
												"s.centro_costo_solicitado AS ccsol, " +
												"s.interpretacion, " +
												"ts.nombre, "+
												"te.entidad_subcontratada AS entidad," +
												"te.codigo_detalle_cargo AS codigodetalle, " +
												"te.articulo, " +
												"te.art_principal AS artppal, " +
												"te.viene_despacho AS vienedespacho, " +
												"to_char(te.fecha,'yyyy-mm-dd') AS fecha, " +
												"te.hora, " +
												"te.codigo_detalle_cargo AS codigotarifa, " +
												"c.codigo_paciente AS codpaciente, " +
												"getnombrepersona(c.codigo_paciente) AS nombrepersona," +
												"getidentificacionpaciente(c.codigo_paciente) AS idpaciente," +
												"gettipoid(c.codigo_paciente) AS tipoid, " +
												"getnombrecentroscosto(s.centro_costo_solicitado) AS nomccsol, " +
												"getdescentitadsubcontratada(te.entidad_subcontratada) AS nomentidad, " +
												"getidpaciente(c.codigo_paciente) AS nroidpaciente," +
												"getdescarticulo(te.articulo) AS descarticulo," +
												"getdescarticulo(te.art_principal) AS descarticuloppal, " +
												"getunidadmedidaarticulo(te.articulo) AS medidaarticulo, " +
												"getunidadmedidaarticulo(te.art_principal) AS medidaartppal, " +
												"'"+ConstantesBD.acronimoNo+"' AS tarifado "+
											"FROM " +
												"solicitudes s " +
											"INNER JOIN " +
												"tarifas_entidad_sub te ON(te.solicitud=s.numero_solicitud) " +
											"INNER JOIN " +
												"tipos_solicitud ts ON (ts.codigo=s.tipo) " +
											"LEFT OUTER JOIN " +
												"cuentas c ON (c.id=s.cuenta) " +
											"WHERE ";
				
				consultaSolicitudes+=	"te.estado="+ConstantesBD.estadoSolFactPendiente+" AND ";
				
				if (!filtros.get("codigoentidad").toString().equals(ConstantesBD.codigoNuncaValido))
				{
					consultaSolicitudes+= " to_char(s.fecha_solicitud,'"+ConstantesBD.formatoFechaBD+"') BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(filtros.get("fechaini").toString())+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(filtros.get("fechafin").toString())+"' AND "
										+ " (s.tipo="+ConstantesBD.codigoTipoSolicitudCargosDirectosArticulos+" OR s.tipo="+ConstantesBD.codigoTipoSolicitudMedicamentos+") AND ";
					if(Utilidades.convertirAEntero(filtros.get("codigoentidad")+"")>0)
						consultaSolicitudes+=" te.entidad_subcontratada="+filtros.get("codigoentidad")+" AND " ;
				}
				consultaSolicitudes+=	"1=1";
			}		
			logger.info("LA CADENA ES-------->"+consultaSolicitudes);
			
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			resultadoSolicitudes = UtilidadBD.cargarValueObject(new ResultSetDecorator(st.executeQuery(consultaSolicitudes)));
		}
		
		catch (SQLException e) 
		{	
			logger.info("ERROR / consultaSolicitudes / "+e);
		}
	
		
		logger.info("CONSULTÉ LAS SOLICITUDES");
		return resultadoSolicitudes;
	}
	
	public static HashMap obtenerErroresPedidos(Connection con, String codigo)
	{
		HashMap resultadoBusquedaErrores = new HashMap();
		resultadoBusquedaErrores.put("numRegistros", 0);
		
		String consultaErrores=	"SELECT DISTINCT " +
									"e.error AS descerror "+
								"FROM " +
										"errores_tarifas_ent_sub e " +
								"INNER JOIN " +
									"tarifas_entidad_sub te ON (te.codigo_detalle_cargo=e.tarifa_entidad_sub) "+
								"WHERE " +
									"te.codigo_detalle_cargo="+codigo+"";
	
		logger.info("LA CADENA ES-------->"+consultaErrores);
		
		try {
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			resultadoBusquedaErrores = UtilidadBD.cargarValueObject(new ResultSetDecorator(st.executeQuery(consultaErrores)));
		}
		catch (SQLException e) 
		{	
			logger.info("ERROR / consultaErrores / "+e);

		}
		Utilidades.imprimirMapa(resultadoBusquedaErrores);
		logger.info("CONSULTÉ LOS ERRORES DE PEDIDOS");
		return resultadoBusquedaErrores;
		
	}
	
	public static HashMap obtenerErroresSolicitudes(Connection con, String codigo)
	{
		HashMap resultadoBusquedaErrores = new HashMap();
		resultadoBusquedaErrores.put("numRegistros", 0);
		
		String consultaErrores=	"SELECT DISTINCT " +
									"e.error AS descerror "+
								"FROM " +
										"errores_tarifas_ent_sub e " +
								"INNER JOIN " +
									"tarifas_entidad_sub te ON (te.codigo_detalle_cargo=e.tarifa_entidad_sub) "+
								"WHERE " +
									"te.codigo_detalle_cargo="+codigo+"";
	
		logger.info("LA CADENA ES-------->"+consultaErrores);
		
		try {
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			resultadoBusquedaErrores = UtilidadBD.cargarValueObject(new ResultSetDecorator(st.executeQuery(consultaErrores)));
		}
		catch (SQLException e) 
		{	
			logger.info("ERROR / consultaErrores / "+e);

		}
		Utilidades.imprimirMapa(resultadoBusquedaErrores);
		logger.info("CONSULTÉ LOS ERRORES DE SOLICITUDES");
		return resultadoBusquedaErrores;
		
	}
	
	public static HashMap obtenerServiciosTarifados (Connection con, HashMap servicios)
	{
		
		boolean esPrimero=true;
		HashMap serviciosTarifados = new HashMap();
		serviciosTarifados.put("numRegistros", 0);
		
		String serviciosTarifadosStr = "SELECT " +
											"tes.codigo_detalle_cargo AS codtarifa, " +
											"es.razon_social AS razonsocial, " +
											"tes.contrato," +
											"to_char(tes.fecha,'dd/mm/yyyy') AS fecha," +
											"aes.consecutivo_autorizacion AS autorizacion," +
											"tes.valor_unitario AS valor," +
											"tes.observaciones, " +
											"getnombreserviciotarifa(aesser.servicio,"+ConstantesBD.codigoTarifarioCups+") AS nombreservicio " +
											
										"FROM " + "facturacion.tarifas_entidad_sub tes " +
										"INNER JOIN " + "facturacion.entidades_subcontratadas es ON(es.codigo_pk=tes.entidad_subcontratada) " +
										"INNER JOIN " + "manejopaciente.autorizaciones_entidades_sub aes ON(aes.consecutivo=tes.autorizacion) " +
										"INNER JOIN " + "manejopaciente.autorizaciones_ent_sub_servi aesser ON(aesser.autorizacion_ent_sub = aes.consecutivo) " +
										"WHERE " + "tes.autorizacion IN(";
		
		for (int i=0;i<Utilidades.convertirAEntero(servicios.get("numRegistros")+"");i++)
		{
			if (servicios.get("tarifado_"+i).toString().equals(ConstantesBD.acronimoSi))
			{
				if (esPrimero)
				{
					serviciosTarifadosStr+=""+servicios.get("consecutivo_"+i);
					esPrimero=false;
				}
				else
					serviciosTarifadosStr+=","+servicios.get("consecutivo_"+i);
			}
		}
		serviciosTarifadosStr +=")";
		
		logger.info("LA CONSULTA DE LOS SERVICIOS TARIFADOS ES--->"+serviciosTarifadosStr);
		
		try {
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			serviciosTarifados = UtilidadBD.cargarValueObject(new ResultSetDecorator(st.executeQuery(serviciosTarifadosStr)));
			Utilidades.imprimirMapa(serviciosTarifados);
		}
		catch (SQLException e) 
		{	
			logger.info("ERROR / serviciosTarifadosStr  / "+e);

		}
		
		return serviciosTarifados;
	}
	
	public static HashMap obtenerPedidosTarifados(Connection con,HashMap pedidos)
	{
		boolean esPrimero=true;
		HashMap pedidosTarifados = new HashMap();
		pedidosTarifados.put("numRegistros", 0);
		
		String pedidosTarifadosStr	=	"SELECT " +
											"tes.codigo_detalle_cargo AS codtarifa, " +
											"tes.contrato," +
											"es.razon_social AS razonsocial, " +
											"to_char(tes.fecha,'dd/mm/yyyy') AS fecha," +
											"p.codigo AS codpedido," +
											"tes.valor_unitario AS valor," +
											"tes.observaciones," +
											"tes.articulo, " +
											"getdescarticulo(tes.articulo) AS descarticulo," +
											"getdescarticulo(tes.art_principal) AS descarticuloppal " +
										"FROM " +
											"facturacion.tarifas_entidad_sub tes " +
										"INNER JOIN " +
											"inventarios.pedido p ON(p.codigo=tes.pedido) " +
										"INNER JOIN " +
											"facturacion.entidades_subcontratadas es ON(es.codigo_pk=tes.entidad_subcontratada) " +
										"WHERE " +
											"tes.pedido IN(";
		
		for (int i=0;i<Utilidades.convertirAEntero(pedidos.get("numRegistros")+"");i++)
		{
			if (pedidos.get("tarifado_"+i).toString().equals(ConstantesBD.acronimoSi))
			{
				if (esPrimero)
				{
					logger.info("EL PEDIDO ES----->"+pedidos.get("codigopedido_"+i));
					pedidosTarifadosStr+=""+pedidos.get("codigopedido_"+i);
					esPrimero=false;
				}
				else
				{
					logger.info("EL PEDIDO ES----->"+pedidos.get("codigopedido_"+i));
					pedidosTarifadosStr+=","+pedidos.get("codigopedido_"+i);
				}
			}
		}
		pedidosTarifadosStr +=")";
		
		logger.info("LA CONSUTLA ES----->"+pedidosTarifadosStr);
		
		try {
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			pedidosTarifados = UtilidadBD.cargarValueObject(new ResultSetDecorator(st.executeQuery(pedidosTarifadosStr)));
			Utilidades.imprimirMapa(pedidosTarifados);
		}
		catch (SQLException e) 
		{	
			logger.info("ERROR / pedidosTarifadosStr  / "+e);

		}
		return pedidosTarifados;	
	}
	
	
	
	
	/**
	 * obtenerSolicitudesTarifados
	 * @param con
	 * @param solicitudes
	 * @return HashMap
	*/
	public static HashMap obtenerSolicitudesTarifados(Connection con,HashMap solicitudes)
	{
		boolean esPrimero=true; 
		HashMap solicitudesTarifados = new HashMap();
		solicitudesTarifados.put("numRegistros", 0);
		
		String solicitudesTarifadosStr	=	"SELECT " +
											"tes.codigo_detalle_cargo AS codtarifa, " +
											"tes.contrato," +
											"es.razon_social AS razonsocial, " +
											"to_char(tes.fecha,'dd/mm/yyyy') AS fecha," +
											"s.numero_solicitud AS numsol," +
											"tes.valor_unitario AS valor," +
											"tes.observaciones," +
											"tes.articulo, " +
											"getdescarticulo(tes.articulo) AS descarticulo," +
											"getdescarticulo(tes.art_principal) AS descarticuloppal " +
										"FROM " +
											"facturacion.tarifas_entidad_sub tes " +
										"INNER JOIN " +
											"ordenes.solicitudes s ON(s.numero_solicitud=tes.solicitud) " +
										"INNER JOIN " +
											"facturacion.entidades_subcontratadas es ON(es.codigo_pk=tes.entidad_subcontratada) " +
										"WHERE " +
											"tes.solicitud IN(";
		
		for (int i=0;i<Utilidades.convertirAEntero(solicitudes.get("numRegistros")+"");i++)
		{
			if (solicitudes.get("tarifado_"+i).toString().equals(ConstantesBD.acronimoSi))
			{
				if (esPrimero)
				{
					logger.info("LA SOLICITUD ES----->"+ solicitudes.get("numsolicitud_"+i));
					solicitudesTarifadosStr+=""+solicitudes.get("numsolicitud_"+i);
					esPrimero=false;
				}
				else
				{
					logger.info("LA SOLICITUD ES----->"+ solicitudes.get("numsolicitud_"+i));
					solicitudesTarifadosStr+=","+solicitudes.get("numsolicitud_"+i);
				}
			}
		}
		solicitudesTarifadosStr +=")";
		
		logger.info("LA CONSUTLA ES----->"+solicitudesTarifadosStr);
		
		try {
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			solicitudesTarifados = UtilidadBD.cargarValueObject(new ResultSetDecorator(st.executeQuery(solicitudesTarifadosStr)));
			Utilidades.imprimirMapa(solicitudesTarifados);
		}
		catch (SQLException e) 
		{	
			logger.info("ERROR / solicitudesTarifadosStr  / "+e);

		}
		return solicitudesTarifados;
	}
	
}