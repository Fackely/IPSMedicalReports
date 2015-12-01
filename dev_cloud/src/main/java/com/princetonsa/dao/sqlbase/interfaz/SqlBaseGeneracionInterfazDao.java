/*
 * Junio 27, 2006 
 */
package com.princetonsa.dao.sqlbase.interfaz;

import java.sql.Connection;
import java.sql.Date;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

/**
 * 
 * @author sgomez
 * Objeto usado para el acceso común a la fuente de datos
 * de la funcionalidad Generació9n Interfaz Facturación
 */
public class SqlBaseGeneracionInterfazDao 
{
	/**
	 * Manejador de logs de la clase
	 */
	private static Logger logger=Logger.getLogger(SqlBaseGeneracionInterfazDao.class);
	
	/**
	 * Sección SELECT para la consulta de los datos de las facturas/anulaciones
	 */
	private static final String consultarFacturasAnulacionesSELECT = "SELECT "+ 
		"f.codigo AS codigo, "+
		"f.fecha AS fecha_factura, "+
		"to_char(f.fecha,'YYYY') AS anio_factura, "+
		"to_char(f.fecha,'MM') AS mes_factura, "+
		"to_char(f.fecha,'DD') AS dia_factura, "+
		"f.hora AS hora_factura, "+
		"f.consecutivo_factura AS consecutivo_factura, "+
		"f.pref_factura AS prefijo_factura, "+
		"f.valor_abonos As valor_abonos, "+
		"f.valor_total AS valor_total, "+
		"f.valor_neto_paciente AS valor_paciente, "+
		"f.valor_convenio AS valor_convenio, "+
		"f.valor_cartera AS valor_cartera, "+
		"f.valor_bruto_pac AS valor_bruto_pac, "+
		"f.val_desc_pac AS val_desc_pac, "+
		"f.cod_paciente AS codigo_paciente, "+
		"getnombrepersona(f.cod_paciente) As nombre_paciente, "+
		"f.convenio AS codigo_convenio, "+
		"getnombreconvenio(f.convenio) AS nombre_convenio, "+
		"CASE WHEN conv.tipo_regimen = '"+ConstantesBD.codigoTipoRegimenParticular+"' THEN getnumeroidentificaciontercero(f.cod_res_particular) ELSE getnumeroidentificaciontercero(e.tercero) END AS num_id_tercero, " +
		"CASE WHEN p.numero_identificacion IS NULL THEN '' ELSE p.numero_identificacion END  AS num_id_paciente, " +
		"CASE WHEN e.razon_social IS NULL THEN '' ELSE e.razon_social END AS nombre_empresa, " +
		"CASE WHEN getdescripciontercero(e.tercero) IS NULL THEN '' ELSE getdescripciontercero(e.tercero) END AS nombre_tercero, " +
		"getnombrepersona(usu.codigo_persona) AS usuario_factura, " +
		"f.usuario AS login_factura, ";
	
	/**
	 * Sección INNER para la consulta de datos de las anulaciones
	 */
	private static final String consultarFacturasAnulacionesINNER = " " +
		"INNER JOIN anulaciones_facturas af ON(af.codigo=f.codigo) " +
		"INNER JOIN usuarios usa  ON(usa.login=af.usuario) ";
	
	/**
	 * Sección INNER para la consulta de datos de facturas
	 */
	private static final String consultarFacturasAnulacionesINNER_01 = " " +
		"LEFT OUTER JOIN personas p ON(p.codigo=f.cod_paciente) "+
		"INNER JOIN convenios conv ON(conv.codigo=f.convenio) "+ 
		"INNER JOIN empresas e ON(e.codigo=conv.empresa) " +
		"INNER JOIN usuarios usu ON(usu.login=f.usuario) ";
	/**
	 * Cadena para consultar las generaciones de interfaz previas
	 */
	private static final String consultarGeneracionesInterfazPreviasStr = "SELECT "+ 
		"fecha_grabacion, "+
		"usuario, "+
		"fecha_inicial, "+
		"fecha_final "+ 
		"FROM generaciones_interfaz "+ 
		"WHERE "+ 
		"institucion = ? AND "+
		"exitoso = "+ValoresPorDefecto.getValorTrueParaConsultas()+" AND "+ 
		"tipo_interfaz = ? AND "+ 
		"((fecha_inicial BETWEEN ? AND ?) OR (fecha_final BETWEEN ? AND ?)) ";
	
	/**
	 * Cadena para consultar los valores parametrizados de los campos de un registro
	 * de la interfaz
	 */
	private static final String consultarCamposRegistroInterfazStr = " SELECT "+ 
		"dri.nombre AS nombre_campo, "+
		"cri.orden_campo AS orden_campo, "+
		"cri.indicativo_requerido AS es_requerido, "+
		"cri.tipo_campo_interfaz AS tipo, "+ 
		"vtc1.tipo_selector_interfaz AS codigo_valor1, "+ 
		"tsi1.nombre AS nombre_valor1, "+ 
		"vtc2.tipo_selector_interfaz AS codigo_valor2, "+ 
		"tsi2.nombre AS nombre_valor2, "+ 
		"dri.indicativo_existe AS indicativo_existe, "+
		"dri.tamanio_campo AS tamanio_campo, "+
		"getDetalleValorInterfaz(dri.codigo,1) AS descripcion, "+
		"getDetalleValorInterfaz(dri.codigo,2) AS valor_default, "+
		"getDetalleValorInterfaz(dri.codigo,3) AS valor_tamanio "+ 
		"FROM detalle_regis_interfaz dri "+ 
		"INNER JOIN campos_regis_interfaz cri ON(cri.codigo=dri.campo_regis_interfaz) "+
		"INNER JOIN tipos_campo_interfaz tci ON(tci.codigo=cri.tipo_campo_interfaz) "+
		"LEFT OUTER JOIN valor_tipo_campo_interfaz vtc1 ON(vtc1.codigo=dri.valor_tipo_campo_int) "+ 
		"LEFT OUTER JOIN tipo_selector_interfaz tsi1 ON(tsi1.codigo=vtc1.tipo_selector_interfaz) "+ 
		"LEFT OUTER JOIN valor_tipo_campo_interfaz vtc2 ON(vtc2.codigo=dri.secun_tipo_campo_int) "+ 
		"LEFT OUTER JOIN tipo_selector_interfaz tsi2 ON(tsi2.codigo=vtc2.tipo_selector_interfaz) "+ 
		"WHERE dri.param_regis_interfaz = ? "+ 
		"ORDER BY cri.orden_campo";
	
	//*********CONSULTAS PARA CUENTAS INTERFAZ**********************************************
	/**
	 * Cadena para consultar la cuenta del convenio
	 */
	private static final String consultaCuentaConvenioStr = "SELECT valor As cuenta " +
		"FROM cuenta_convenio WHERE cod_convenio = ? AND cod_tipo_cuenta = ? AND cod_institucion = ? ";
	/**
	 * Cadena para consultar cuenta convenio por tipo de regimen
	 */
	private static final String consultaCuentaTipoRegimenStr = "SELECT c.valor AS cuenta " +
		"FROM cuenta_regimen c " +
		"INNER JOIN convenios conv ON(conv.tipo_regimen=c.acr_tipo_regimen) " +
		"WHERE conv.codigo = ? AND cod_tipo_cuenta = ? AND c.cod_institucion = ?  ";
	/**
	 * Cadena para consultar cuenta servicio
	 */
	private static final String consultaCuentaServicioStr = "SELECT cuenta_ingreso AS cuenta " +
		"FROM servicio_cuenta_ingreso WHERE servicio = ? AND centro_costo = ?";
	/**
	 * Cadena para consultar la cuenta servicio por especialidad
	 */
	private static final String consultaCuentaEspecialidadServicioStr = "SELECT e.cuenta_ingreso AS cuenta " +
		"FROM especi_serv_cue_ing e " +
		"INNER JOIN servicios s ON(s.tipo_servicio=e.tipo_servicio AND s.grupo_servicio=e.grupo_servicio) " +
		"WHERE s.codigo = ? and e.centro_costo = ? " ;
	
	/**
	 * Cadena para consultar la cuenta servicio por tipo servicio
	 */
	private static final String consultaCuentaTipoServicioStr = "SELECT t.cuenta_ingreso AS cuenta " +
		"FROM tipo_servicio_cuenta_ing t " +
		"INNER JOIN servicios s ON(s.grupo_servicio=t.grupo_servicio) " +
		"WHERE s.codigo = ? AND t.centro_costo = ?";
	
	/**
	 * Cadena para consultar la cuenta servicio por grupo de servicio
	 */
	private static final String consultaCuentaGrupoServicioStr = "SELECT g.cuenta_ingreso AS cuenta " +
		"FROM grupo_servicio_cue_ingr g ";
	
	/**
	 * Cadena para consultar cuenta Inventario
	 */
	private static final String consultaCuentaInventarioStr = "SELECT cuenta_ingreso AS cuenta " +
		"FROM articulo_inv_cuenta_ing WHERE articulo = ? AND centro_costo = ?";
	
	/**
	 * Cadena para consultar cuenta Inventario por subgrupo
	 */
	private static final String consultaCuentaSubgrupoInventarioStr = "SELECT s.cuenta_ingreso AS cuenta " +
		"FROM subgrupo_inv_cuenta_ing s " +
		"INNER JOIN articulo a ON(a.subgrupo=s.subgrupo_inventario) " +
		"WHERE a.codigo = ? AND s.centro_costo = ?";
	
	/**
	 * Cadena para consultar cuenta Inventario por grupo
	 */
	private static final String consultaCuentaGrupoInventarioStr = "SELECT g.cuenta_ingreso AS cuenta " +
		"FROM grupo_inv_cuenta_ing g " +
		"INNER JOIN subgrupo_inventario s ON(s.grupo=g.grupo_inventario AND s.clase=g.clase_inventario) " +
		"INNER JOIN articulo a ON(a.subgrupo=s.codigo) " +
		"WHERE a.codigo = ? and g.centro_costo = ?";
	
	/**
	 * Cadena para consultar la cuenta Inventario por clase
	 */
	private static final String consultaCuentaClaseInventarioStr = "SELECT c.cuenta_ingreso AS cuenta " +
		"FROM clase_invent_cuenta_ing c " +
		"INNER JOIN subgrupo_inventario s ON(s.clase=c.clase_inventario) " +
		"INNER JOIN articulo a ON(a.subgrupo=s.codigo) " +
		"WHERE a.codigo = ? AND c.centro_costo = ?";
	
	/**
	 * Cadena que consulta la cuenta unidad funcional de un centro de costo especifico
	 */
	private static final String consultaCuentaUnidadFuncionalCentroCostoStr = "SELECT " +
		"CASE WHEN u.cuenta_ingreso IS NULL THEN '' ELSE u.cuenta_ingreso END AS cuenta_servicio," +
		"CASE WHEN u.cuenta_medicamento IS NULL THEN '' ELSE u.cuenta_medicamento END AS cuenta_medicamento " +
		"FROM unidad_fun_cuenta_ing_cc u " +
		"INNER JOIN centros_costo c ON(c.codigo=u.centro_costo AND c.unidad_funcional=u.unidad_funcional) " +
		"WHERE u.centro_costo = ? AND u.institucion = ?";
	
	/**
	 * Cadena que consulta la cuenta unidad fucnional
	 */
	private static final String consultaCuentaUnidadFuncionalStr = "SELECT " +
		"CASE WHEN u.cuenta_ingreso IS NULL THEN '' ELSE u.cuenta_ingreso END AS cuenta_servicio," +
		"CASE WHEN u.cuenta_medicamento IS NULL THEN '' ELSE u.cuenta_medicamento END AS cuenta_medicamento " +
		"FROM unidad_fun_cuenta_ing u ";
	
	/**
	 * Cadena que consulta la cuenta pool y la cuenta ingreso pool
	 */
	private static final String consultaCuentaPoolStr = "SELECT " +
		"cc1.cuenta_contable AS cuenta_pool," +
		"cc2.cuenta_contable AS cuenta_institucion " +
		"FROM pooles_esquema_tarifario pet " +
		"LEFT OUTER JOIN cuentas_contables cc1 ON(pet.cuenta_contable_pool=cc1.codigo) " +
		"LEFT OUTER JOIN cuentas_contables cc2 ON(pet.cuenta_contable_ins=cc2.codigo) " +
		"WHERE pet.pool = ? AND pet.esquema_tarifario = ?";
	
	//***************************************************************************************
	
	/**
	 * Cadena que consulta el detalle de servicios de la factura
	 */
	private static final String consultaDetalleServiciosFacturaStr = "" +
		"( " +
			//CONSULTA DE LOS SERVICIOS DE LA FACTURA
			"SELECT "+
			"s.consecutivo_ordenes_medicas AS orden, "+
			"dfs.valor_total AS valor_total, "+
			"dfs.valor_pool AS valor_pool, "+
			"dfs.valor_total - dfs.valor_pool AS valor_ingreso_pool, "+
			"s.centro_costo_solicitado As centro_costo_solicitado, "+
			"dfs.servicio AS codigo_servicio, "+
			"getcodigoespecialidad(dfs.servicio) As codigo_especialidad, "+
			"CASE WHEN dfs.pool IS NULL THEN 0 ELSE dfs.pool END AS codigo_pool, "+
			"getEsquemaTarifarioSerArt(f.sub_cuenta,getcontratosubcuenta(f.sub_cuenta),dfs.servicio,'S') AS esquema_tar, "+
			"CASE WHEN getnumeroidentificaciontercero(p.tercero_responsable) IS NULL THEN '' ELSE getnumeroidentificaciontercero(p.tercero_responsable) END AS num_tercero_pool, "+
			"CASE WHEN per.numero_identificacion IS NULL THEN '' ELSE per.numero_identificacion END AS num_id_medico, "+
			"CASE WHEN getnombreservicio(dfs.servicio,"+ConstantesBD.codigoTarifarioCups+") IS NULL THEN '' ELSE getnombreservicio(dfs.servicio,"+ConstantesBD.codigoTarifarioCups+") END AS descripcion_servicio, "+
			"getnomcentrocosto(s.centro_costo_solicitado) AS nom_centro_costo_solicitado, "+
			"CASE WHEN getdescripcionpool(dfs.pool) IS NULL THEN '' ELSE getdescripcionpool(dfs.pool) END AS nombre_pool, "+
			"CASE WHEN getnombrepersona(dfs.codigo_medico) IS NULL THEN '' ELSE getnombrepersona(dfs.codigo_medico) END AS nombre_medico, "+
			"0 AS grupo_asocio "+  
			"FROM facturas f "+
			"INNER JOIN det_factura_solicitud dfs ON(dfs.factura=f.codigo) "+ 
			"INNER JOIN solicitudes s ON(dfs.solicitud=s.numero_solicitud) "+ 
			"LEFT OUTER JOIN pooles p ON(p.codigo=dfs.pool) "+ 
			"LEFT OUTER JOIN personas per ON(per.codigo=dfs.codigo_medico) "+ 
			"WHERE "+ 
			"f.codigo = ? AND s.tipo NOT IN ("+ConstantesBD.codigoTipoSolicitudMedicamentos+","+ConstantesBD.codigoTipoSolicitudCargosDirectosArticulos+","+ConstantesBD.codigoTipoSolicitudCirugia+") "+
		") "+
		"UNION "+
		"( "+
			//CONSULTA DE LOS ASOCIOS DE LA FACTURA
			"SELECT "+ 
			"s.consecutivo_ordenes_medicas AS orden, "+
			"adf.valor_total AS valor_total, "+
			"adf.valor_pool AS valor_pool, "+
			"adf.valor_total - adf.valor_pool AS valor_ingreso_pool, "+
			"s.centro_costo_solicitado As centro_costo_solicitado, "+
			"adf.servicio_asocio AS codigo_servicio, "+
			"getcodigoespecialidad(adf.servicio_asocio) As codigo_especialidad, "+
			"CASE WHEN adf.pool IS NULL THEN 0 ELSE adf.pool END AS codigo_pool, "+
			"CASE WHEN adf.esquema_tarifario IS NULL THEN 0 ELSE adf.esquema_tarifario END AS esquema_tar, "+
			"CASE WHEN getnumeroidentificaciontercero(p.tercero_responsable) IS NULL THEN '' ELSE getnumeroidentificaciontercero(p.tercero_responsable) END AS num_tercero_pool, "+
			"CASE WHEN per.numero_identificacion IS NULL THEN '' ELSE per.numero_identificacion END AS num_id_medico, "+
			"CASE WHEN getnombreservicio(adf.servicio_asocio,"+ConstantesBD.codigoTarifarioCups+") IS NULL THEN '' ELSE getnombreservicio(adf.servicio_asocio,"+ConstantesBD.codigoTarifarioCups+") END AS descripcion_servicio, "+
			"getnomcentrocosto(s.centro_costo_solicitado) AS nom_centro_costo_solicitado, "+
			"CASE WHEN getdescripcionpool(adf.pool) IS NULL THEN '' ELSE getdescripcionpool(adf.pool) END AS nombre_pool, "+
			"CASE WHEN getnombrepersona(adf.codigo_medico) IS NULL THEN '' ELSE getnombrepersona(adf.codigo_medico) END AS nombre_medico, "+ 
			"adf.grupo_asocio AS grupo_asocio "+ 
			"FROM det_factura_solicitud dfs "+ 
			"INNER JOIN asocios_det_factura adf ON(adf.codigo=dfs.codigo) "+ 
			"INNER JOIN solicitudes s ON(dfs.solicitud=s.numero_solicitud) "+ 
			"LEFT OUTER JOIN pooles p ON(p.codigo=adf.pool) "+ 
			"LEFT OUTER JOIN personas per ON(per.codigo=adf.codigo_medico) "+ 
			"WHERE "+ 
			"dfs.factura = ?" +
		") " +
		"ORDER BY orden";
	
	/**
	 * Cadena que consulta el detalle de articulos de una factura
	 */
	private static final String consultaDetalleArticulosFacturaStr = "SELECT "+
		"s.consecutivo_ordenes_medicas AS orden, "+
		"dfs.valor_total AS valor_total, "+
		"s.centro_costo_solicitante As centro_costo_solicitante, "+
		"CASE WHEN dfs.articulo IS NULL THEN 0 ELSE dfs.articulo END AS codigo_articulo, "+
		"CASE WHEN getdescripcionarticulo(dfs.articulo) IS NULL THEN '' ELSE getdescripcionarticulo(dfs.articulo) END AS descripcion_articulo, "+
		"getnomcentrocosto(s.centro_costo_solicitante) AS nom_centro_costo_solicita "+  
		"FROM det_factura_solicitud dfs "+ 
		"INNER JOIN solicitudes s ON(dfs.solicitud=s.numero_solicitud) "+ 
		"WHERE "+ 
		"dfs.factura = ? AND s.tipo IN ("+ConstantesBD.codigoTipoSolicitudMedicamentos+","+ConstantesBD.codigoTipoSolicitudCargosDirectosArticulos+") " +
		"ORDER BY orden";
	
	/**
	 * Cadena que ingresa información de auditoria de la generación de interfaz
	 */
	private static final String insertarAuditoriaGeneracionStr = " INSERT INTO " +
		"generaciones_interfaz " +
		"(codigo,usuario,fecha_grabacion,hora_grabacion," +
		"fecha_inicial,fecha_final,tipo_interfaz," +
		"documento_inicial,documento_final," +
		"path_salida,path_inconsistencias," +
		"exitoso,institucion) ";
	
	/**
	 * Método implementado para consultar las facturas/anulaciones
	 * según los campos parametrizados
	 * @param con
	 * @param campos
	 * @return
	 */
	public static HashMap consultarFacturasAnulaciones(Connection con,HashMap campos)
	{
		try
		{
			int tipoInterfaz = Utilidades.convertirAEntero(campos.get("tipoInterfaz").toString());
			String consulta = "";
			
			//TIPO INTERFAZ FACTURACION**************************
			if(tipoInterfaz==ConstantesBD.tipoInterfazFacturacion)
				consulta = armarConsultaFacturas(campos);
			else if(tipoInterfaz==ConstantesBD.tipoInterfazAnulacion)
				consulta = armarConsultaAnulaciones(campos);
			else if(tipoInterfaz==ConstantesBD.tipoInterfazAmbos)
			{
				String consultaFactura = armarConsultaFacturas(campos);
				String consultaAnulaciones = armarConsultaAnulaciones(campos);
				consulta = "("+consultaFactura+") UNION ("+consultaAnulaciones+")";
				
			}
			
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			HashMap mapaRetorno= UtilidadBD.cargarValueObject(new ResultSetDecorator(st.executeQuery(consulta)),true,false);
	        st.close();
			return mapaRetorno;

			
		}
		catch(SQLException e)
		{
			logger.error("Error en consultarFacturasAnulaciones en SqlBaseGeneracionInterfazDao: "+e);
			return null;
		}
	}

	/**
	 * Método implementado para armar la consulta de anulaciones
	 * @param campos
	 * @return
	 */
	private static String armarConsultaAnulaciones(HashMap campos) 
	{
		String consulta = consultarFacturasAnulacionesSELECT + 
		"getnombrepersona(usa.codigo_persona) AS usuario_anula, " +
		"af.usuario AS login_anula, "+
		"to_char(af.fecha_grabacion,'YYYY-MM-DD') AS fecha_anulacion, "+ 
		"af.hora_grabacion || '' AS hora_anulacion, "+
		"to_char(af.fecha_grabacion,'YYYY') AS anio_anulacion, "+
		"to_char(af.fecha_grabacion,'MM') AS mes_anulacion, "+
		"to_char(af.fecha_grabacion,'DD') AS dia_anulacion, "+
		"af.consecutivo_anulacion AS consecutivo_anulacion "+
		"FROM facturas f "+
		consultarFacturasAnulacionesINNER + 
		consultarFacturasAnulacionesINNER_01 +
		" WHERE "+
		"(af.fecha_grabacion BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(campos.get("fechaInicial").toString())+"' " +
			"AND '"+UtilidadFecha.conversionFormatoFechaABD(campos.get("fechaFinal").toString())+"') AND "+
		" af.institucion = "+campos.get("institucion")+" ";
	
		if(!campos.get("documentoInicial").toString().equals("")&&
			!campos.get("documentoFinal").toString().equals(""))
			consulta += " AND (af.consecutivo_anulacion BETWEEN "+campos.get("documentoInicial")+" AND "+campos.get("documentoFinal")+") ";
		
		if(!campos.get("convenio").toString().equals(""))
			consulta += " AND f.convenio = "+campos.get("convenio")+" ";
		
		
		
		consulta+= " ORDER BY af.consecutivo_anulacion ";
		return consulta;
	}

	/**
	 * Método implementado para armar la consulta de facturas
	 * @param campos
	 * @return
	 */
	private static String armarConsultaFacturas(HashMap campos) 
	{
		String consulta = consultarFacturasAnulacionesSELECT + 
		"'' AS usuario_anula, " +
		"'' AS login_anula, "+
		"'' As fecha_anulacion, " +
		"'' AS hora_anulacion, "+
		"'' AS anio_anulacion, "+
		"'' AS mes_anulacion, "+
		"'' AS dia_anulacion, "+
		"'' AS consecutivo_anulacion "+
		"FROM facturas f "+
		consultarFacturasAnulacionesINNER_01 +
		" WHERE "+
		"(f.fecha BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(campos.get("fechaInicial").toString())+"' " +
			"AND '"+UtilidadFecha.conversionFormatoFechaABD(campos.get("fechaFinal").toString())+"') AND "+
		" f.institucion = "+campos.get("institucion")+" ";
	
		if(!campos.get("documentoInicial").toString().equals("")&&
			!campos.get("documentoFinal").toString().equals(""))
			consulta += " AND (f.consecutivo_factura BETWEEN "+campos.get("documentoInicial")+" AND "+campos.get("documentoFinal")+") ";
		
		if(!campos.get("convenio").toString().equals(""))
			consulta += " AND f.convenio = "+campos.get("convenio")+" ";
		
		
		
		consulta+= " ORDER BY f.consecutivo_factura ";
		return consulta;
	}
	
	/**
	 * Método implementado para consultar las generaciones de interfaz previas
	 * @param con
	 * @param campos
	 * @return
	 */
	public static HashMap consultarGeneracionesInterfazPrevias(Connection con,HashMap campos)
	{
		try
		{
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consultarGeneracionesInterfazPreviasStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,Utilidades.convertirAEntero(campos.get("institucion")+""));
			pst.setInt(2,Utilidades.convertirAEntero(campos.get("tipoInterfaz")+""));
			pst.setDate(3,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(campos.get("fechaInicial").toString())));
			pst.setDate(4,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(campos.get("fechaFinal").toString())));
			pst.setDate(5,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(campos.get("fechaInicial").toString())));
			pst.setDate(6,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(campos.get("fechaFinal").toString())));
			
			HashMap mapaRetorno=UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()),true,false);
			pst.close();
			return mapaRetorno;
		}
		catch(SQLException e)
		{
			logger.error("Error en consultarGeneracionesInterfazPrevias en SqlBaseGeneracionInterfazDao: "+e);
			return null;
		}
	}
	
	/**
	 * Método implementado para consultar los datos de parametrizacion de los 
	 * campos de un registro interfaz
	 * @param con
	 * @param codigoRegistro
	 * @return
	 */
	public static HashMap consultarCamposRegistroInterfaz(Connection con,String codigoRegistro)
	{
		try
		{
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consultarCamposRegistroInterfazStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,Utilidades.convertirAEntero(codigoRegistro));
			
			HashMap mapaRetorno=UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()),true,false);
			pst.close();
			return mapaRetorno;
		}
		catch(SQLException e)
		{
			logger.error("Error en consultarCamposRegistroInterfaz de SqlBaseGeneracionInterfazDao: "+e);
			return null;
		}
	}
	
	/**
	 * Método implementado para consultar la cuenta parametrizable 
	 * según el tipo y los parámetros enviados por el mapa paramCuentas
	 * @param con
	 * @param paramCuentas
	 * @return
	 */
	public static String consultarCuentaParametrizable(Connection con,HashMap paramCuentas)
	{
		try
		{
			PreparedStatementDecorator pst = null;
			ResultSetDecorator rs = null;
			String valor = "";
			
			//Se toma el tipo de cuenta
			int tipo = Utilidades.convertirAEntero(paramCuentas.get("tipoCuenta").toString());
			
			switch(tipo)
			{
				case ConstantesBD.tipoCuentaConvenio:
				case ConstantesBD.tipoCuentaPaciente:
				case ConstantesBD.tipoCuentaDescuentosPaciente:
				case ConstantesBD.tipoCuentaDevolucionPaciente:
				case ConstantesBD.tipoCuentaAbonosPaciente:
					//1) Se consulta directamente del convenio
					pst =  new PreparedStatementDecorator(con.prepareStatement(consultaCuentaConvenioStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					pst.setInt(1,Utilidades.convertirAEntero(paramCuentas.get("codigoConvenio")+""));
					pst.setInt(2,Utilidades.convertirAEntero(paramCuentas.get("tipoCuenta")+""));
					pst.setInt(3,Utilidades.convertirAEntero(paramCuentas.get("institucion")+""));
					
					rs = new ResultSetDecorator(pst.executeQuery());
					if(rs.next())
						valor = rs.getString("cuenta");
					else
						valor = "";
					
					if(valor.equals(""))
					{
						
						//2) Se consulta desde el tipo de regimen
						pst =  new PreparedStatementDecorator(con.prepareStatement(consultaCuentaTipoRegimenStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
						pst.setInt(1,Utilidades.convertirAEntero(paramCuentas.get("codigoConvenio")+""));
						pst.setInt(2,Utilidades.convertirAEntero(paramCuentas.get("tipoCuenta")+""));
						pst.setInt(3,Utilidades.convertirAEntero(paramCuentas.get("institucion")+""));
						
						rs = new ResultSetDecorator(pst.executeQuery());
						if(rs.next())
							valor = rs.getString("cuenta");
					}
				break;
				
				case ConstantesBD.tipoCuentaServicios:
					//1) Se consulta directamente del servicio con centro costo solicitado
					pst =  new PreparedStatementDecorator(con.prepareStatement(consultaCuentaServicioStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					pst.setInt(1,Utilidades.convertirAEntero(paramCuentas.get("codigoServicio")+""));
					pst.setInt(2,Utilidades.convertirAEntero(paramCuentas.get("centroCosto")+""));
					
					rs = new ResultSetDecorator(pst.executeQuery());
					if(rs.next())
						valor = rs.getString("cuenta");
					else
						valor = "";
					
					if(valor.equals(""))
					{
						//2) Se consulta directamente del servicio con centro costo TODOS
						pst =  new PreparedStatementDecorator(con.prepareStatement(consultaCuentaServicioStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
						pst.setInt(1,Utilidades.convertirAEntero(paramCuentas.get("codigoServicio")+""));
						pst.setInt(2,ConstantesBD.codigoCentroCostoTodos);
						
						rs = new ResultSetDecorator(pst.executeQuery());
						if(rs.next())
							valor = rs.getString("cuenta");
						else
							valor = "";
						
						if(valor.equals(""))
						{
							//3) Se consulta por la especialidad del servicio y centro costo solicitado
							pst =  new PreparedStatementDecorator(con.prepareStatement(consultaCuentaEspecialidadServicioStr+" and e.especialidad_servicio=s.especialidad",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
							pst.setInt(1,Utilidades.convertirAEntero(paramCuentas.get("codigoServicio")+""));
							pst.setInt(2,Utilidades.convertirAEntero(paramCuentas.get("centroCosto")+""));
							
							rs = new ResultSetDecorator(pst.executeQuery());
							if(rs.next())
								valor = rs.getString("cuenta");
							else
								valor = "";
							
							if(valor.equals(""))
							{
								//4) Se consulta por la especialidad TODAS y centro costo solicitado
								pst =  new PreparedStatementDecorator(con.prepareStatement(consultaCuentaEspecialidadServicioStr+" and e.especialidad_servicio="+ConstantesBD.codigoEspecialidadMedicaNinguna,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
								pst.setInt(1,Utilidades.convertirAEntero(paramCuentas.get("codigoServicio")+""));
								pst.setInt(2,Utilidades.convertirAEntero(paramCuentas.get("centroCosto")+""));
								
								rs = new ResultSetDecorator(pst.executeQuery());
								if(rs.next())
									valor = rs.getString("cuenta");
								else
									valor = "";
								
								if(valor.equals(""))
								{
									//5) Se consulta por la especialiad del servicio y centro de costo TODOS
									pst =  new PreparedStatementDecorator(con.prepareStatement(consultaCuentaEspecialidadServicioStr+" and e.especialidad_servicio=s.especialidad",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
									pst.setInt(1,Utilidades.convertirAEntero(paramCuentas.get("codigoServicio")+""));
									pst.setInt(2,ConstantesBD.codigoCentroCostoTodos);
									
									rs = new ResultSetDecorator(pst.executeQuery());
									if(rs.next())
										valor = rs.getString("cuenta");
									else
										valor = "";
									
									if(valor.equals(""))
									{
										//6) Se consulta por la especialidad TODAS y centro deo costo TODOS
										pst =  new PreparedStatementDecorator(con.prepareStatement(consultaCuentaEspecialidadServicioStr+" and e.especialidad_servicio="+ConstantesBD.codigoEspecialidadMedicaNinguna,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
										pst.setInt(1,Utilidades.convertirAEntero(paramCuentas.get("codigoServicio")+""));
										pst.setInt(2,ConstantesBD.codigoCentroCostoTodos);
										
										rs = new ResultSetDecorator(pst.executeQuery());
										if(rs.next())
											valor = rs.getString("cuenta");
										else
											valor = "";
										
										if(valor.equals(""))
										{
											//7) Se consulta por el tipo servicio del servicio y centro costo solicitado
											pst =  new PreparedStatementDecorator(con.prepareStatement(consultaCuentaTipoServicioStr+"  AND s.tipo_servicio=t.tipo_servicio",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
											pst.setInt(1,Utilidades.convertirAEntero(paramCuentas.get("codigoServicio")+""));
											pst.setInt(2,Utilidades.convertirAEntero(paramCuentas.get("centroCosto")+""));
											
											rs = new ResultSetDecorator(pst.executeQuery());
											if(rs.next())
												valor = rs.getString("cuenta");
											else
												valor = "";
											
											if(valor.equals(""))
											{
												//8) Se consulta por el tipo servicio TODOS y centro costo solicitado
												pst =  new PreparedStatementDecorator(con.prepareStatement(consultaCuentaTipoServicioStr+"  AND t.tipo_servicio='1'",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
												pst.setInt(1,Utilidades.convertirAEntero(paramCuentas.get("codigoServicio")+""));
												pst.setInt(2,Utilidades.convertirAEntero(paramCuentas.get("centroCosto")+""));
												
												rs = new ResultSetDecorator(pst.executeQuery());
												if(rs.next())
													valor = rs.getString("cuenta");
												else
													valor = "";
												
												if(valor.equals(""))
												{
													//9) Se consulta por el tipo servicio del servicio y centro costo TODOS
													pst =  new PreparedStatementDecorator(con.prepareStatement(consultaCuentaTipoServicioStr+"  AND s.tipo_servicio=t.tipo_servicio",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
													pst.setInt(1,Utilidades.convertirAEntero(paramCuentas.get("codigoServicio")+""));
													pst.setInt(2,ConstantesBD.codigoCentroCostoTodos);
													
													rs = new ResultSetDecorator(pst.executeQuery());
													if(rs.next())
														valor = rs.getString("cuenta");
													else
														valor = "";
													
													if(valor.equals(""))
													{
														//10) Se consulta por el tipo servicio TODOS y centro costo TODOS
														pst =  new PreparedStatementDecorator(con.prepareStatement(consultaCuentaTipoServicioStr+"  AND t.tipo_servicio='1'",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
														pst.setInt(1,Utilidades.convertirAEntero(paramCuentas.get("codigoServicio")+""));
														pst.setInt(2,ConstantesBD.codigoCentroCostoTodos);
														
														rs = new ResultSetDecorator(pst.executeQuery());
														if(rs.next())
															valor = rs.getString("cuenta");
														else
															valor = "";
														
														if(valor.equals(""))
														{
															//11) Se consulta por el grupo servicio del servicio y centro de costo solicitado
															String consulta = consultaCuentaGrupoServicioStr +
																"INNER JOIN servicios s ON(s.grupo_servicio=g.grupo_servicio) " +
																"WHERE s.codigo = ? AND g.centro_costo = ?";
															
															pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
															pst.setInt(1,Utilidades.convertirAEntero(paramCuentas.get("codigoServicio")+""));
															pst.setInt(2,Utilidades.convertirAEntero(paramCuentas.get("centroCosto")+""));
															
															rs = new ResultSetDecorator(pst.executeQuery());
															if(rs.next())
																valor = rs.getString("cuenta");
															else
																valor = "";
															
															if(valor.equals(""))
															{
																//12) Se consulta por el grupo servicio TODOS y centro de costo solicitado
																consulta = consultaCuentaGrupoServicioStr +
																	" WHERE g.centro_costo = ? AND g.grupo_servicio = -1";
																pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
																pst.setInt(1,Utilidades.convertirAEntero(paramCuentas.get("centroCosto")+""));
																
																rs = new ResultSetDecorator(pst.executeQuery());
																if(rs.next())
																	valor = rs.getString("cuenta");
																else
																	valor = "";
																
																if(valor.equals(""))
																{
																	//13) Se consulta por el grupo servicio del servicio y centro de costo TODOS
																	consulta = consultaCuentaGrupoServicioStr +
																	"INNER JOIN servicios s ON(s.grupo_servicio=g.grupo_servicio) " +
																	"WHERE s.codigo = ? AND g.centro_costo = ?";
																
																	pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
																	pst.setInt(1,Utilidades.convertirAEntero(paramCuentas.get("codigoServicio")+""));
																	pst.setInt(2,ConstantesBD.codigoCentroCostoTodos);
																	
																	rs = new ResultSetDecorator(pst.executeQuery());
																	if(rs.next())
																		valor = rs.getString("cuenta");
																	else
																		valor = "";
																	
																	if(valor.equals(""))
																	{
																		//14) Se consulta por el grupo de servicio TODOS y centro de costo TODOS
																		consulta = consultaCuentaGrupoServicioStr +
																		" WHERE g.centro_costo = ? AND g.grupo_servicio = -1";
																		pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
																		pst.setInt(1,ConstantesBD.codigoCentroCostoTodos);
																	
																		rs = new ResultSetDecorator(pst.executeQuery());
																		if(rs.next())
																			valor = rs.getString("cuenta");
																		else
																			valor = "";
																		
																		if(valor.equals(""))
																		{
																			/** CONSULTAS CUENTA INTERFAZ UNIDAD FUNCIONAL SERVICIO **/
																			//15) Se consulta cuenta unidad funcional por centro costo solicitado
																			pst =  new PreparedStatementDecorator(con.prepareStatement(consultaCuentaUnidadFuncionalCentroCostoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
																			pst.setInt(1,Utilidades.convertirAEntero(paramCuentas.get("centroCosto")+""));
																			pst.setInt(2,Utilidades.convertirAEntero(paramCuentas.get("institucion")+""));
																			
																			rs = new ResultSetDecorator(pst.executeQuery());
																			if(rs.next())
																				valor = rs.getString("cuenta_servicio");
																			
																			if(valor.equals(""))
																			{
																				//16) Se consulta cuenta por la unidad funcional del centro de costo
																				consulta = consultaCuentaUnidadFuncionalStr +
																					"INNER JOIN centros_costo c ON (c.unidad_funcional=u.unidad_funcional) " +
																					"WHERE c.codigo = ? AND u.institucion = ?";
																				pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
																				pst.setInt(1,Utilidades.convertirAEntero(paramCuentas.get("centroCosto")+""));
																				pst.setInt(2,Utilidades.convertirAEntero(paramCuentas.get("institucion")+""));
																				
																				rs = new ResultSetDecorator(pst.executeQuery());
																				if(rs.next())
																					valor = rs.getString("cuenta_servicio");
																				
																				if(valor.equals(""))
																				{
																					//17) Se consulta cuenta por unidad funcional todas
																					consulta = consultaCuentaUnidadFuncionalStr + 
																						"WHERE u.unidad_funcional IS NULL AND u.institucion = ?";
																					
																					pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
																					pst.setInt(1,Utilidades.convertirAEntero(paramCuentas.get("institucion")+""));
																					
																					rs = new ResultSetDecorator(pst.executeQuery());
																					if(rs.next())
																						valor = rs.getString("cuenta_servicio");
																				}
																			}
																		}
																		
																	}
																	
																}
																
															}
															
														}
													}
												}
											}
											
											
										}
									}
								}
									
							}
							
						}
						
					}
				break;
				
				case ConstantesBD.tipoCuentaInventarios:
					//1) Se consulta directamente del articulo con el centro de costo solicitante
					pst =  new PreparedStatementDecorator(con.prepareStatement(consultaCuentaInventarioStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					pst.setInt(1,Utilidades.convertirAEntero(paramCuentas.get("codigoArticulo")+""));
					pst.setInt(2,Utilidades.convertirAEntero(paramCuentas.get("centroCosto")+""));
					
					rs = new ResultSetDecorator(pst.executeQuery());
					if(rs.next())
						valor = rs.getString("cuenta");
					
					if(valor.equals(""))
					{
						//2) Se consulta directamente del articulo con el centro de costo TODOS
						pst =  new PreparedStatementDecorator(con.prepareStatement(consultaCuentaInventarioStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
						pst.setInt(1,Utilidades.convertirAEntero(paramCuentas.get("codigoArticulo")+""));
						pst.setInt(2,ConstantesBD.codigoCentroCostoTodos);
						
						rs = new ResultSetDecorator(pst.executeQuery());
						if(rs.next())
							valor = rs.getString("cuenta");
						
						if(valor.equals(""))
						{
							//3) Se consulta del subgrupo del articulo con el centro costo solicitante
							pst =  new PreparedStatementDecorator(con.prepareStatement(consultaCuentaSubgrupoInventarioStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
							pst.setInt(1,Utilidades.convertirAEntero(paramCuentas.get("codigoArticulo")+""));
							pst.setInt(2,Utilidades.convertirAEntero(paramCuentas.get("centroCosto")+""));
							
							rs = new ResultSetDecorator(pst.executeQuery());
							if(rs.next())
								valor = rs.getString("cuenta");
							
							if(valor.equals(""))
							{
								//4) Se consulta del subgrupo del artículo con el centro costo TODOS
								pst =  new PreparedStatementDecorator(con.prepareStatement(consultaCuentaSubgrupoInventarioStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
								pst.setInt(1,Utilidades.convertirAEntero(paramCuentas.get("codigoArticulo")+""));
								pst.setInt(2,ConstantesBD.codigoCentroCostoTodos);
								
								rs = new ResultSetDecorator(pst.executeQuery());
								if(rs.next())
									valor = rs.getString("cuenta");
								
								if(valor.equals(""))
								{
									//5) Se consulta del grupo del articulo con el centro de costo solicitante
									pst =  new PreparedStatementDecorator(con.prepareStatement(consultaCuentaGrupoInventarioStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
									pst.setInt(1,Utilidades.convertirAEntero(paramCuentas.get("codigoArticulo")+""));
									pst.setInt(2,Utilidades.convertirAEntero(paramCuentas.get("centroCosto")+""));
									
									rs = new ResultSetDecorator(pst.executeQuery());
									if(rs.next())
										valor = rs.getString("cuenta");
									
									if(valor.equals(""))
									{
										//6) Se consulta del grupo del articulo con el centro de costo TODOS
										pst =  new PreparedStatementDecorator(con.prepareStatement(consultaCuentaGrupoInventarioStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
										pst.setInt(1,Utilidades.convertirAEntero(paramCuentas.get("codigoArticulo")+""));
										pst.setInt(2,ConstantesBD.codigoCentroCostoTodos);
										
										rs = new ResultSetDecorator(pst.executeQuery());
										if(rs.next())
											valor = rs.getString("cuenta");
										
										if(valor.equals(""))
										{
											//7) Se consulta de la clase del articulo con el centro de costo solicitante
											pst =  new PreparedStatementDecorator(con.prepareStatement(consultaCuentaClaseInventarioStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
											pst.setInt(1,Utilidades.convertirAEntero(paramCuentas.get("codigoArticulo")+""));
											pst.setInt(2,Utilidades.convertirAEntero(paramCuentas.get("centroCosto")+""));
											
											rs = new ResultSetDecorator(pst.executeQuery());
											if(rs.next())
												valor = rs.getString("cuenta");
											
											if(valor.equals(""))
											{
												//8) Se consulta de la clase del articulo con el centro de costo TODOS
												pst =  new PreparedStatementDecorator(con.prepareStatement(consultaCuentaClaseInventarioStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
												pst.setInt(1,Utilidades.convertirAEntero(paramCuentas.get("codigoArticulo")+""));
												pst.setInt(2,ConstantesBD.codigoCentroCostoTodos);
												
												rs = new ResultSetDecorator(pst.executeQuery());
												if(rs.next())
													valor = rs.getString("cuenta");
												
												if(valor.equals(""))
												{
													/** CONSULTA CUENTA INTERFAZ UNIDAD FUNCIONAL MEDICAMENTO **/
													//9) Se consulta cuenta unidad funcional por centro costo solicitante
													pst =  new PreparedStatementDecorator(con.prepareStatement(consultaCuentaUnidadFuncionalCentroCostoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
													pst.setInt(1,Utilidades.convertirAEntero(paramCuentas.get("centroCosto")+""));
													pst.setInt(2,Utilidades.convertirAEntero(paramCuentas.get("institucion")+""));
													
													rs = new ResultSetDecorator(pst.executeQuery());
													if(rs.next())
														valor = rs.getString("cuenta_medicamento");
													
													if(valor.equals(""))
													{
														//10) Se consulta cuenta por la unidad funcional del centro de costo
														String consulta = consultaCuentaUnidadFuncionalStr +
															"INNER JOIN centros_costo c ON (c.unidad_funcional=u.unidad_funcional) " +
															"WHERE c.codigo = ? AND u.institucion = ?";
														pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
														pst.setInt(1,Utilidades.convertirAEntero(paramCuentas.get("centroCosto")+""));
														pst.setInt(2,Utilidades.convertirAEntero(paramCuentas.get("institucion")+""));
														
														rs = new ResultSetDecorator(pst.executeQuery());
														if(rs.next())
															valor = rs.getString("cuenta_medicamento");
														
														if(valor.equals(""))
														{
															//11) Se consulta cuenta por unidad funcional todas
															consulta = consultaCuentaUnidadFuncionalStr + 
																"WHERE u.unidad_funcional IS NULL AND u.institucion = ?";
															
															pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
															pst.setInt(1,Utilidades.convertirAEntero(paramCuentas.get("institucion")+""));
															
															rs = new ResultSetDecorator(pst.executeQuery());
															if(rs.next())
																valor = rs.getString("cuenta_medicamento");
														}
													}
												}
											}
											
										}
									}
								}
							}
						}
					}
				break;
				
				case ConstantesBD.tipoCuentaPool:
				case ConstantesBD.tipoCuentaIngresoPool:
					pst =  new PreparedStatementDecorator(con.prepareStatement(consultaCuentaPoolStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					pst.setInt(1,Utilidades.convertirAEntero(paramCuentas.get("codigoPool")+""));
					pst.setInt(2,Utilidades.convertirAEntero(paramCuentas.get("esquemaTarifario")+""));
					
					rs = new ResultSetDecorator(pst.executeQuery());
					if(rs.next())
					{
						if(tipo==ConstantesBD.tipoCuentaPool)
							valor = rs.getString("cuenta_pool");
						else
							valor = rs.getString("cuenta_institucion");
					}
				break;
			}
			
			return valor;
		}
		catch(SQLException e)
		{
			logger.error("Error en consultarCuentaParametrizable en SqlBaseGeneracionInterfazDao: "+e);
			return "";
		}
	}
	
	/**
	 * Método implementado para consultar el detalle de sevicios/asocios
	 * de una factura
	 * @param con
	 * @param codigoFactura
	 * @return
	 */
	public static HashMap consultaDetalleServiciosFactura(Connection con,String codigoFactura)
	{
		try
		{
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consultaDetalleServiciosFacturaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,Utilidades.convertirAEntero(codigoFactura));
			pst.setInt(2,Utilidades.convertirAEntero(codigoFactura));
			
			HashMap mapaRetorno= UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()),true,false);
			pst.close();
			return mapaRetorno;
		}
		catch(SQLException e)
		{
			logger.error("Error en consultaDetalleServiciosFactura de SqlBaseGeneracionInterfazDao: "+e);
			return null;
		}
	}
	
	/**
	 * Método implementado para consultar el detalle de artículos
	 * de una factura
	 * @param con
	 * @param codigoFactura
	 * @return
	 */
	public static HashMap consultaDetalleArticulosFactura(Connection con,String codigoFactura)
	{
		try
		{
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consultaDetalleArticulosFacturaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,Utilidades.convertirAEntero(codigoFactura));
			
			HashMap mapaRetorno= UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()),true,false);
			pst.close();
			return mapaRetorno;
		}
		catch(SQLException e)
		{
			logger.error("Error en consultaDetalleArticulosFactura de SqlBaseGeneracionInterfazDao: "+e);
			return null;
		}
	}
	
	/**
	 * Método implementado para ingresar información de auditoria
	 * sobre la generación de interfaz
	 * @param con
	 * @param campos
	 * @return
	 */
	public static int insertarAuditoriaGeneracion(Connection con,HashMap campos)
	{
		try
		{
			String consulta = insertarAuditoriaGeneracionStr;
			consulta += "values("+campos.get("secuencia")+",?,CURRENT_DATE,"+ValoresPorDefecto.getSentenciaHoraActualBDTipoTime()+",?,?,?,?,?,?,?,?,?)";
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			/**
			 * INSERT INTO " +
					"generaciones_interfaz " +
					"(codigo,usuario,fecha_grabacion,hora_grabacion," +
					"fecha_inicial,fecha_final,tipo_interfaz," +
					"documento_inicial,documento_final," +
					"path_salida,path_inconsistencias," +
					"exitoso,institucion) 
			 */
			
			pst.setString(1,campos.get("usuario")+"");
			pst.setDate(2,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(campos.get("fechaInicial").toString())));
			pst.setDate(3,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(campos.get("fechaFinal").toString())));
			pst.setInt(4,Utilidades.convertirAEntero(campos.get("tipoInterfaz")+""));
			pst.setString(5,campos.get("documentoInicial")+"");
			pst.setString(6,campos.get("documentoFinal")+"");
			pst.setString(7,campos.get("pathSalida")+"");
			pst.setString(8,campos.get("pathInconsistencias")+"");
			pst.setBoolean(9,UtilidadTexto.getBoolean(campos.get("exitoso").toString()));
			pst.setInt(10,Utilidades.convertirAEntero(campos.get("institucion")+""));
			
			return pst.executeUpdate();
		}
		catch(SQLException e)
		{
			logger.error("Error en insertarAuditoriaGeneracion de SqlBaseGeneracionInterfazDao: "+e);
			return 0;
		}
	}
}
