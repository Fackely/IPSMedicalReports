package com.princetonsa.dao.oracle.inventarios;

import java.sql.Connection;
import java.util.HashMap;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.dao.inventarios.ArticulosConsumidosPacientesDao;
import com.princetonsa.dao.sqlbase.inventarios.SqlBaseArticulosConsumidosPacientesDao;


public class OracleArticulosConsumidosPacientesDao implements ArticulosConsumidosPacientesDao 
{

	/**
	 * Parte principal del UNION Articulos Consumidos Pacientes
	 */
	private static String strConArticulosConsumidosPacientesPartePrin = "SELECT " +
																			"ap.\"fecha\" AS fecha, " +
																			"ap.\"codigo\" AS codigo, " +
																			"ap.\"des\" AS des, " +
																			"ap.\"concen\" AS concen, " +
																			"ap.\"forma\" AS forma, " +
																			"ap.\"unidad\" AS unidad, " +
																			"ap.\"ingreso\" AS ingreso, " +
																			"ap.\"tipo_id\" AS tipo_id, " +
																			"ap.\"paciente\" AS paciente, " +
																			"ap.\"id_ent\" AS id_ent, " +
																			"ap.\"entidad\" AS entidad, " +
																			"ap.\"cantidad\" AS cantidad " +
																		"FROM ";
	
	/**
	 * Cadena SELECT que Consulta los Totales de Consumo Diario por Articulo
	 */
	private static String strConTotalesConsumoDiarioArticuloPartePrin = "SELECT " +
																			"ap.\"codigo\" AS codigo, " +
																			"ap.\"des\" AS des, " +
																			"ap.\"concen\" AS concen, " +
																			"ap.\"unidad\" AS unidad, " +
																			"ap.\"cantidad\" AS cantidad, " +
																			"ap.\"fechadia\" AS fechadia " +
																		"FROM ";
	
	/**
	 * Primera parte del UNION Articulos Consumidos Pacientes
	 */
	private static String strConArticulosConsumidosPacientesParteUno = "SELECT " +
																			"to_char(dt.fecha_modifica, 'DD/MM/YYYY') || ' - ' ||dt.hora_modifica AS \"fecha\", " +
																			"getCodArticuloAxiomaInterfaz(va.codigo, ?) AS \"codigo\", " +
																			"getdescarticulosincodigo(va.codigo) AS \"des\", " +
																			"va.concentracion AS \"concen\", " +
																			"va.forma_farmaceutica AS \"forma\", " +
																			"getnomunidadmedida(va.unidad_medida) AS \"unidad\", " +
																			"i.consecutivo AS \"ingreso\", " +
																			"getidpaciente(c.codigo_paciente) AS \"tipo_id\", " +
																			"getnombrepersona(c.codigo_paciente) AS \"paciente\", " +
																			"getnumeroidentificaciontercero(e.tercero) AS \"id_ent\", " +
																			"co.nombre AS \"entidad\", " +
																			"dt.cantidad_cargada AS \"cantidad\" " +
																		"FROM " +
																			"solicitudes s " +
																			"INNER JOIN det_cargos dt ON (s.numero_solicitud=dt.solicitud) " +
																			"INNER JOIN view_articulos va ON (dt.articulo=va.codigo) " +
																			"INNER JOIN articulos_almacen aa ON (va.codigo=aa.articulo) " +
																			"INNER JOIN cuentas c ON (s.cuenta=c.id) " +
																			"INNER JOIN ingresos i ON (c.id_ingreso=i.id) " +
																			"INNER JOIN sub_cuentas sub ON (i.id=sub.ingreso) " +
																			"INNER JOIN convenios co ON (sub.convenio=co.codigo) " +
																			"INNER JOIN empresas e ON (co.empresa=e.codigo) " +
																		"WHERE ";
	
	/**
	 * Segunda parte del UNION Articulos Consumidos Pacientes
	 */
	private static String strConArticulosConsumidosPacientesParteDos = "SELECT " +
																			"to_char(dp.fecha,'DD/MM/YYYY') || ' - ' || dp.hora AS \"fecha\", " +
																			"getCodArticuloAxiomaInterfaz(va.codigo, ?) AS \"codigo\", " +
																			"getdescarticulosincodigo(va.codigo) AS \"des\", " +
																			"va.concentracion AS \"concen\", " +
																			"va.forma_farmaceutica AS \"forma\", " +
																			"getnomunidadmedida(va.unidad_medida) AS \"unidad\", " +
																			"i.consecutivo AS \"ingreso\", " +
																			"getidpaciente(pq.paciente) AS \"tipo_id\", " +
																			"getnombrepersona(pq.paciente) AS \"paciente\", " +
																			"getnumeroidentificaciontercero(e.tercero) AS \"id_ent\", " +
																			"co.nombre AS \"entidad\", " +
																			"coalesce((ddp.cantidad- coalesce((SELECT sum(ddpe.cantidad) FROM detalle_devol_pedido ddpe   INNER JOIN devolucion_pedidos dp ON(ddpe.devolucion=dp.codigo) WHERE dp.estado=2 AND ddpe.pedido=ddp.pedido and ddpe.articulo=ddp.articulo),0)),0) AS cantidad " +
																		"FROM " +
																			"pedido p " +
																			"INNER JOIN despacho_pedido dp ON (p.codigo=dp.pedido) " +
																			"INNER JOIN detalle_despacho_pedido ddp ON (dp.pedido=ddp.pedido) " +
																			"INNER JOIN view_articulos va ON (ddp.articulo=va.codigo) " +
																			"INNER JOIN pedidos_peticiones_qx ppq ON (p.codigo=ppq.pedido) " +
																			"INNER JOIN peticion_qx pq ON (ppq.peticion=pq.codigo) " +
																			"INNER JOIN solicitudes_cirugia sc ON (pq.codigo=sc.codigo_peticion) " +
																			"INNER JOIN solicitudes s ON (sc.numero_solicitud=s.numero_solicitud) " +
																			"INNER JOIN cuentas c ON (s.cuenta=c.id) " +
																			"INNER JOIN ingresos i ON (c.id_ingreso=i.id) " +
																			"INNER JOIN sub_cuentas sub ON (i.id=sub.ingreso) " +
																			"INNER JOIN convenios co ON (sub.convenio=co.codigo) " +
																			"INNER JOIN det_cargos dc ON (dc.sub_cuenta=sub.sub_cuenta AND dc.solicitud=s.numero_solicitud AND dc.eliminado='"+ConstantesBD.acronimoNo+"') " +
																			"INNER JOIN empresas e ON (co.empresa=e.codigo) " +
																			"INNER JOIN articulos_almacen aa ON (aa.articulo=va.codigo) " +
																		"WHERE ";
	
	/**
	 * Tercera parte del UNION Articulos Consumidos Pacientes
	 */
	private static String strConArticulosConsumidosPacientesParteTres = "SELECT " +
																			"to_char(dp.fecha,'DD/MM/YYYY') || ' - ' || dp.hora AS \"fecha\", " +
																			"getCodArticuloAxiomaInterfaz(va.codigo, ?) AS \"codigo\", " +
																			"getdescarticulosincodigo(va.codigo) AS \"des\", " +
																			"va.concentracion AS \"concen\", " +
																			"va.forma_farmaceutica AS \"forma\", " +
																			"getnomunidadmedida(va.unidad_medida) AS \"unidad\", " +
																			"i.consecutivo AS \"ingreso\", " +
																			"getidpaciente(pq.paciente) AS \"tipo_id\", " +
																			"getnombrepersona(pq.paciente) AS \"paciente\", " +
																			"getnumeroidentificaciontercero(e.tercero) AS \"id_ent\", " +
																			"co.nombre AS \"entidad\", " +
																			"coalesce((ddp.cantidad - coalesce((SELECT sum(ddpe.cantidad) FROM detalle_devol_pedido ddpe   INNER JOIN devolucion_pedidos dp ON(ddpe.devolucion=dp.codigo) WHERE dp.estado=2 AND ddpe.pedido=ddp.pedido and ddpe.articulo=ddp.articulo),0)),0) AS \"cantidad\" " +
																		"FROM " +
																			"pedido p " +
																			"INNER JOIN despacho_pedido dp ON (p.codigo=dp.pedido) " +
																			"INNER JOIN detalle_despacho_pedido ddp ON (dp.pedido=ddp.pedido) " +
																			"INNER JOIN view_articulos va ON (ddp.articulo=va.codigo) " +
																			"INNER JOIN pedidos_peticiones_qx ppq ON (p.codigo=ppq.pedido) " +
																			"INNER JOIN peticion_qx pq ON (ppq.peticion=pq.codigo) " +
																			"INNER JOIN solicitudes_cirugia sc ON (pq.codigo=sc.codigo_peticion) " +
																			"INNER JOIN solicitudes s ON (sc.numero_solicitud=s.numero_solicitud) " +
																			"INNER JOIN cuentas c ON (s.cuenta=c.id) " +
																			"INNER JOIN ingresos i ON (c.id_ingreso=i.id) " +
																			"INNER JOIN sub_cuentas sub ON (i.id=sub.ingreso) " +
																			"INNER JOIN convenios co ON (sub.convenio=co.codigo) " +
																			"LEFT OUTER JOIN det_cargos dc ON (dc.sub_cuenta=sub.sub_cuenta AND dc.solicitud=s.numero_solicitud AND dc.eliminado='"+ConstantesBD.acronimoNo+"') " +
																			"INNER JOIN empresas e ON (co.empresa=e.codigo) " +
																			"INNER JOIN articulos_almacen aa ON (aa.articulo=va.codigo) " +
																		"WHERE ";
	
	
	
	/**
	 * Primera parte del UNION Consulta los Totales de Consumo Diario por Articulo
	 */
	private static String strConTotalesConsumoDiarioArticuloParteUno = "SELECT " +
																			"getCodArticuloAxiomaInterfaz(va.codigo, ?) AS \"codigo\", " +
																			"va.descripcion AS \"des\", " +
																			"va.concentracion AS \"concen\", " +
																			"getnomunidadmedida(va.unidad_medida) AS \"unidad\", " +
																			"coalesce(dt.cantidad_cargada,0) AS \"cantidad\", " +
																			"to_char(dt.fecha_modifica, 'DD') AS \"fechadia\" " +
																		"FROM " +
																			"solicitudes s " +
																			"INNER JOIN det_cargos dt ON(s.numero_solicitud=dt.solicitud) " +
																			"INNER JOIN view_articulos va on(dt.articulo=va.codigo) " +
																			"INNER JOIN cuentas c on(s.cuenta=c.id) " +
																			"INNER JOIN ingresos i on(c.id_ingreso=i.id) " +
																			"INNER JOIN sub_cuentas sub on(i.id=sub.ingreso) " +
																			"INNER JOIN convenios co on(sub.convenio=co.codigo) " +
																			"INNER JOIN empresas e on(co.empresa=e.codigo) " +
																			"INNER JOIN articulos_almacen aa on(aa.articulo=va.codigo) " +
																		"WHERE ";
	
	/**
	 * Segunda parte del UNION Consulta los Totales de Consumo Diario por Articulo
	 */
	private static String strConTotalesConsumoDiarioArticuloParteDos = "SELECT " +
																			"getCodArticuloAxiomaInterfaz(va.codigo, ?) AS \"codigo\", " +
																			"va.descripcion AS \"des\", " +
																			"va.concentracion AS \"concen\", " +
																			"getnomunidadmedida(va.unidad_medida) AS \"unidad\", " +
																			"coalesce((ddp.cantidad - coalesce((SELECT sum(ddpe.cantidad) FROM detalle_devol_pedido ddpe   INNER JOIN devolucion_pedidos dp ON(ddpe.devolucion=dp.codigo) WHERE dp.estado=2 AND ddpe.pedido=ddp.pedido and ddpe.articulo=ddp.articulo),0)),0) AS \"cantidad\", " +
																			"to_char(dp.fecha, 'DD') AS \"fechadia\" " +
																		"FROM " +
																			"pedido p " +
																			"INNER JOIN despacho_pedido dp on(p.codigo=dp.pedido) " +
																			"INNER JOIN detalle_despacho_pedido ddp on(dp.pedido=ddp.pedido) " +
																			"INNER JOIN view_articulos va on(ddp.articulo=va.codigo) " +
																			"INNER JOIN pedidos_peticiones_qx ppq on(p.codigo=ppq.pedido) " +
																			"INNER JOIN peticion_qx pq on(ppq.peticion=pq.codigo) " +
																			"INNER JOIN solicitudes_cirugia sc on(pq.codigo=sc.codigo_peticion) " +
																			"INNER JOIN solicitudes s on(sc.numero_solicitud=s.numero_solicitud) " +
																			"INNER JOIN cuentas c on(s.cuenta=c.id) " +
																			"INNER JOIN ingresos i on(c.id_ingreso=i.id) " +
																			"INNER JOIN sub_cuentas sub on(i.id=sub.ingreso) " +
																			"INNER JOIN convenios co on(sub.convenio=co.codigo) " +
																			"INNER JOIN det_cargos dc ON (dc.sub_cuenta=sub.sub_cuenta AND dc.solicitud=s.numero_solicitud AND dc.eliminado='"+ConstantesBD.acronimoNo+"') " +
																			"INNER JOIN empresas e on(co.empresa=e.codigo) " +
																			"INNER JOIN articulos_almacen aa on(aa.articulo=va.codigo) " +
																		"WHERE ";
	
	/**
	 * Tercera parte del UNION Consulta los Totales de Consumo Diario por Articulo
	 */
	private static String strConTotalesConsumoDiarioArticuloParteTres = "SELECT " +
																			"getCodArticuloAxiomaInterfaz(va.codigo, ?) AS \"codigo\", " +
																			"va.descripcion AS \"des\", " +
																			"va.concentracion AS \"concen\", " +
																			"getnomunidadmedida(va.unidad_medida) AS \"unidad\", " +
																			"coalesce((ddp.cantidad - coalesce((SELECT sum(ddpe.cantidad) FROM detalle_devol_pedido ddpe   INNER JOIN devolucion_pedidos dp ON(ddpe.devolucion=dp.codigo) WHERE dp.estado=2 AND ddpe.pedido=ddp.pedido and ddpe.articulo=ddp.articulo),0)),0) AS \"cantidad\", " +
																			"to_char(dp.fecha, 'DD') AS \"fechadia\" " +
																		"FROM " +
																			"pedido p " +
																			"INNER JOIN despacho_pedido dp on(p.codigo=dp.pedido) " +
																			"INNER JOIN detalle_despacho_pedido ddp on(dp.pedido=ddp.pedido) " +
																			"INNER JOIN view_articulos va on(ddp.articulo=va.codigo) " +
																			"INNER JOIN pedidos_peticiones_qx ppq on(p.codigo=ppq.pedido) " +
																			"INNER JOIN peticion_qx pq on(ppq.peticion=pq.codigo) " +
																			"INNER JOIN solicitudes_cirugia sc on(pq.codigo=sc.codigo_peticion) " +
																			"INNER JOIN solicitudes s on(sc.numero_solicitud=s.numero_solicitud) " +
																			"INNER JOIN cuentas c on(s.cuenta=c.id) " +
																			"INNER JOIN ingresos i on(c.id_ingreso=i.id) " +
																			"INNER JOIN sub_cuentas sub on(i.id=sub.ingreso) " +
																			"INNER JOIN convenios co on(sub.convenio=co.codigo) " +
																			"LEFT OUTER JOIN det_cargos dc ON (dc.sub_cuenta=sub.sub_cuenta AND dc.solicitud=s.numero_solicitud AND dc.eliminado='"+ConstantesBD.acronimoNo+"') " +
																			"INNER JOIN empresas e on(co.empresa=e.codigo) " +
																			"INNER JOIN articulos_almacen aa on(aa.articulo=va.codigo) " +
																		"WHERE ";
	
	/**
	 * 
	 */
   	public HashMap<String, Object> consultarCondicionesArticulosConsumidos(Connection con, HashMap<String, Object> criterios)
   	{
   		HashMap mapa = new HashMap<String, Object>();
		String parametros = "", condicionesGenerales = "", condicionesUno = "", condicionesDos = "", consulta = "", condicionesTres="";
			
		//Condiciones Iniciales de la Funcionalidad
		condicionesUno = "s.tipo IN ("+ConstantesBD.codigoTipoSolicitudMedicamentos+", "+ConstantesBD.codigoTipoSolicitudCargosDirectosArticulos+") AND s.estado_historia_clinica IN ("+ConstantesBD.codigoEstadoHCAdministrada+", "+ConstantesBD.codigoEstadoHCDespachada+", "+ConstantesBD.codigoEstadoHCCargoDirecto+") AND dt.estado <> "+ConstantesBD.codigoEstadoFAnulada+" AND sub.nro_prioridad = 1 ";
		condicionesDos = "p.estado = "+ConstantesBD.codigoEstadoPedidoDespachado+" AND p.es_qx = '"+ConstantesBD.acronimoSi+"' ";
		
		//*******************INICIO VALIDACIONES GENERALES************************
		parametros = "Centro Atención ["+Utilidades.obtenerNombreCentroAtencion(con, Utilidades.convertirAEntero(criterios.get("centroAtencion")+""))+"], ";
		condicionesGenerales += "AND i.centro_atencion = "+criterios.get("centroAtencion")+" ";
		
		parametros += "Periodo ["+criterios.get("fechaInicial")+" - "+criterios.get("fechaFinal")+"], ";
		condicionesUno += "AND to_char(dt.fecha_modifica, 'YYYY-MM-DD') BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(criterios.get("fechaInicial")+"")+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(criterios.get("fechaFinal")+"")+"' ";
		condicionesDos += "AND to_char(dp.fecha, 'YYYY-MM-DD') BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(criterios.get("fechaInicial")+"")+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(criterios.get("fechaFinal")+"")+"' ";
		
		//Validamos el Almacen
		if(UtilidadCadena.noEsVacio(criterios.get("almacen")+""))
		{
			condicionesGenerales += "AND aa.almacen = "+criterios.get("almacen")+" ";
			parametros += "Almacén ["+Utilidades.obtenerNombreCentroCosto(con, Utilidades.convertirAEntero(criterios.get("almacen")+""), Utilidades.convertirAEntero(criterios.get("institucion")+""))+"], ";
		}
		
		//Validamos el Articulo
		if(UtilidadCadena.noEsVacio(criterios.get("articulo")+""))
		{
			condicionesGenerales += "AND va.codigo = "+criterios.get("articulo")+" ";
			parametros += "Artículo ["+criterios.get("desArticulo")+"], ";
		}
		
		//Validamos la Clase
		if(UtilidadCadena.noEsVacio(criterios.get("clase")+""))
		{
			condicionesGenerales += "AND va.clase = "+criterios.get("clase")+" ";
			parametros += "Clase ["+criterios.get("desClase")+"], ";
		}
		
		//Validamos el Grupo
		if(UtilidadCadena.noEsVacio(criterios.get("grupo")+""))
		{
			condicionesGenerales += "AND va.grupo = "+criterios.get("grupo")+" ";
			parametros += "Grupo ["+criterios.get("desGrupo")+"], ";
		}
		
		//Validamos el SubGrupo
		if(UtilidadCadena.noEsVacio(criterios.get("desSubGrupo")+""))
		{
			condicionesGenerales += "AND va.subgrupo = "+criterios.get("subGrupo")+" ";
			parametros += "SubGrupo ["+criterios.get("subGrupo")+"], ";
		}
		
		condicionesTres = condicionesDos+" AND dc.codigo_detalle_cargo IS NULL AND sub.nro_prioridad=1 ";
		
		parametros += "Tipo Código Artículo ["+ValoresPorDefecto.getIntegridadDominio(criterios.get("tipoCodigoArticulo")+"")+"], ";
		parametros += "Tipo Informe ["+ValoresPorDefecto.getIntegridadDominio(criterios.get("tipoInforme")+"")+"]";
		//*********************FIN VALIDACIONES GENERALES**************************
		
		//Validamos que tipo de informe se selecciono con la intención de armar la consulta
		if((criterios.get("tipoInforme")+"").equals(ConstantesIntegridadDominio.acronimoConsumoPacienteArticulo))
			consulta = strConArticulosConsumidosPacientesPartePrin+"(("+strConArticulosConsumidosPacientesParteUno+condicionesUno+condicionesGenerales+") UNION ("+strConArticulosConsumidosPacientesParteDos+condicionesDos+condicionesGenerales+") UNION ("+strConArticulosConsumidosPacientesParteTres+condicionesTres+condicionesGenerales+")) ap ORDER BY ap.\"fecha\" ";
		else if((criterios.get("tipoInforme")+"").equals(ConstantesIntegridadDominio.acronimoTotalesConsumoArticulo))
			consulta = strConTotalesConsumoDiarioArticuloPartePrin+"(("+strConTotalesConsumoDiarioArticuloParteUno+condicionesUno+condicionesGenerales+") UNION ("+strConTotalesConsumoDiarioArticuloParteDos+condicionesDos+condicionesGenerales+") UNION ("+strConTotalesConsumoDiarioArticuloParteTres+condicionesTres+condicionesGenerales+")) ap ORDER BY ap.\"des\" ";
		
		mapa.put("parametros", parametros);
		mapa.put("consulta", consulta);
		
		return mapa;
   	}
   	
   	/**
	 * 
	 */
   	public HashMap<String, Object> consultarArticulosConsumidos(Connection con, HashMap<String, Object> criterios)
   	{
   		return SqlBaseArticulosConsumidosPacientesDao.consultarArticulosConsumidos(con, criterios);
   	}
   	
}