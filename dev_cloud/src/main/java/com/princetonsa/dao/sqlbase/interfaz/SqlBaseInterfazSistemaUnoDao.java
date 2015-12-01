package com.princetonsa.dao.sqlbase.interfaz;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.commons.beanutils.ConvertingWrapDynaBean;
import org.apache.log4j.Logger;

import com.princetonsa.decorator.ResultSetDecorator;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.Utilidades;

/**
 *
 *@author Andrés Silva Monsalve
*
**/

public class SqlBaseInterfazSistemaUnoDao 
{
	/**
	 * Objeto para manejar los logs de esta clase
	 */
	private static Logger logger=Logger.getLogger(SqlBaseInterfazSistemaUnoDao.class);
	
	
	/**
	 * Cadena Consultar el Registro de Planos
	 */
	public static String strCadenaConsultaHistorialInterfazSistemaUno = "SELECT " +
	"codigo,"+
	"tipo," +
	"reproceso," +			
	"fecha_inicial,"+
	"fecha_final,"+
	"path,"+
	"nombre,"+
	"path_inconsistencia,"+
	"exitosa,"+
	"fecha_modificacion,"+
	"hora_modificacion,"+
	"usuario_modifica"+
	"fecha_grabacion"+
	"hora_grabacion"+
	"FROM interfaz_sistema_uno " +
	"WHERE fecha_modificacion || ' ' || hora_modificacion = max(fecha_modificacion || ' ' || hora_modificacion) " +
	"AND institucion = ? ";

	
	
	/**
	 * Cadena de consulta la ruta del registro mas reciente del historial de Archivos Planos
	 */
	public static String strConsultaRutaSistemaUno = "SELECT " +			
			"path "+			
			"FROM interfaz_sistema_uno " +
			"WHERE institucion = ? " +
			"ORDER BY fecha_modifica  desc, hora_modifica desc LIMIT 1 ";
	
	

	/**
	 * Insertar en Interfaz Sistema Uno
	 */
	public static String cadenaInsercionSistemaUnoStr = "INSERT INTO  interfaz_sistema_uno (codigo, " +
																							"tipo, " +//2
																							"reproceso, " +//3
																							"fecha_inicial, " +//4
																							"fecha_final, " +//5
																							"path,  " +//6
																							"nombre, " +//7
																							"path_inconsistencia, " +//8
																							"nombre_inconsistencia, " +//9
																							"exitosa, " +//10
																							"fecha_modificacion, " +
																							"hora_modificacion, " +
																							"usuario_modifica, " +//11
																							"fecha_grabacion, " +
																							"hora_grabacion, " +
																							"institucion) " +//12
																							"VALUES (?," +
																							"?," +
																							"?," +
																							"?," +
																							"?," +
																							"?," +
																							"?," +
																							"?," +
																							"?," +
																							"?," +
																							"CURRENT_DATE, " +
																							"SUBSTR(CURRENT_TIME,1,5), " +
																							"?," +
																							"CURRENT_DATE, " +
																							"SUBSTR(CURRENT_TIME,1,5), " +
																							"?)";
	
	
	public static String cadenaContabilizarFacturaStr = "UPDATE facturas SET contabilizado='S' where codigo=?";
	
	
	public static String strCadenaCuentaConvenio = "SELECT cc.cuenta_contable FROM cuenta_convenio " +
														"cco inner join cuentas_contables cc ON(cc.codigo=cco.valor) " +
														"WHERE cco.cod_convenio = ? and cco.cod_tipo_cuenta = ? and cod_institucion = ?";

	
	public static String strCadenaCuentaRegimen = "SELECT cc.cuenta_contable FROM cuenta_regimen "+
														"cco inner join cuentas_contables cc ON(cc.codigo=cco.valor) " +
														"WHERE cco.acr_tipo_regimen = ? and cco.cod_tipo_cuenta = ? and cod_institucion = ?";
	
	
	
//	*********CONSULTAS PARA CUENTAS INTERFAZ**********************************************
	/**
	 * Cadena para consultar la cuenta del convenio
	 */
	private static final String consultaCuentaConvenioStr = "SELECT coalesce(getcuentacontable(case when valor is null or valor = '' THEN 0 ELSE to_number(valor,'9999999999') end),'') As cuenta " +
		"FROM cuenta_convenio WHERE cod_convenio = ? AND cod_tipo_cuenta = ? AND cod_institucion = ? ";
	/**
	 * Cadena para consultar cuenta convenio por tipo de regimen
	 */
	private static final String consultaCuentaTipoRegimenStr = "SELECT coalesce(getcuentacontable(case when c.valor is null or c.valor = '' THEN 0 ELSE to_number(c.valor,'9999999999') end),'') AS cuenta " +
		"FROM cuenta_regimen c " +
		"INNER JOIN convenios conv ON(conv.tipo_regimen=c.acr_tipo_regimen) " +
		"WHERE conv.codigo = ? AND cod_tipo_cuenta = ? AND c.cod_institucion = ?  ";
	/**
	 * Cadena para consultar cuenta servicio
	 */
	private static final String consultaCuentaServicioStr = "SELECT coalesce(getcuentacontable(cuenta_ingreso),'') AS cuenta " +
		"FROM servicio_cuenta_ingreso WHERE servicio = ? AND centro_costo = ?";
	/**
	 * Cadena para consultar la cuenta servicio por especialidad
	 */
	private static final String consultaCuentaEspecialidadServicioStr = "SELECT coalesce(getcuentacontable(e.cuenta_ingreso),'') AS cuenta " +
		"FROM especi_serv_cue_ing e " +
		"INNER JOIN servicios s ON(s.tipo_servicio=e.tipo_servicio AND s.grupo_servicio=e.grupo_servicio) " +
		"WHERE s.codigo = ? and e.centro_costo = ? " ;
	
	/**
	 * Cadena para consultar la cuenta servicio por tipo servicio  
	 */
	private static final String consultaCuentaTipoServicioStr = "SELECT coalesce(getcuentacontable(t.cuenta_ingreso),'') AS cuenta " +
		"FROM tipo_servicio_cuenta_ing t " +
		"INNER JOIN servicios s ON(s.grupo_servicio=t.grupo_servicio) " +
		"WHERE s.codigo = ? AND t.centro_costo = ?";
	
	/**
	 * Cadena para consultar la cuenta servicio por grupo de servicio
	 */
	private static final String consultaCuentaGrupoServicioStr = "SELECT coalesce(getcuentacontable(g.cuenta_ingreso),'') AS cuenta " +
		"FROM grupo_servicio_cue_ingr g ";
	
	/**
	 * Cadena para consultar cuenta Inventario
	 */
	private static final String consultaCuentaInventarioStr = "SELECT coalesce(getcuentacontable(cuenta_ingreso),'') AS cuenta " +
		"FROM articulo_inv_cuenta_ing WHERE articulo = ? AND centro_costo = ?";
	
	/**
	 * Cadena para consultar cuenta Inventario por subgrupo
	 */
	private static final String consultaCuentaSubgrupoInventarioStr = "SELECT coalesce(getcuentacontable(s.cuenta_ingreso),'') AS cuenta " +
		"FROM subgrupo_inv_cuenta_ing s " +
		"INNER JOIN articulo a ON(a.subgrupo=s.subgrupo_inventario) " +
		"WHERE a.codigo = ? AND s.centro_costo = ?";
	
	/**
	 * Cadena para consultar cuenta Inventario por grupo
	 */
	private static final String consultaCuentaGrupoInventarioStr = "SELECT coalesce(getcuentacontable(g.cuenta_ingreso),'') AS cuenta " +
		"FROM grupo_inv_cuenta_ing g " +
		"INNER JOIN subgrupo_inventario s ON(s.grupo=g.grupo_inventario AND s.clase=g.clase_inventario) " +
		"INNER JOIN articulo a ON(a.subgrupo=s.codigo) " +
		"WHERE a.codigo = ? and g.centro_costo = ?";
	
	/**
	 * Cadena para consultar la cuenta Inventario por clase
	 */
	private static final String consultaCuentaClaseInventarioStr = "SELECT coalesce(getcuentacontable(c.cuenta_ingreso),'') AS cuenta " +
		"FROM clase_invent_cuenta_ing c " +
		"INNER JOIN subgrupo_inventario s ON(s.clase=c.clase_inventario) " +
		"INNER JOIN articulo a ON(a.subgrupo=s.codigo) " +
		"WHERE a.codigo = ? AND c.centro_costo = ?";
	
	/**
	 * Cadena que consulta la cuenta unidad funcional de un centro de costo especifico
	 */
	private static final String consultaCuentaUnidadFuncionalCentroCostoStr = "SELECT " +
		"coalesce(getcuentacontable(u.cuenta_ingreso),'') AS cuenta_servicio," +
		"coalesce(getcuentacontable(u.cuenta_medicamento),'') AS cuenta_medicamento " +
		"FROM unidad_fun_cuenta_ing_cc u " +
		"INNER JOIN centros_costo c ON(c.codigo=u.centro_costo AND c.unidad_funcional=u.unidad_funcional) " +
		"WHERE u.centro_costo = ? AND u.institucion = ?";
	
	/**
	 * Cadena que consulta la cuenta unidad fucnional
	 */
	private static final String consultaCuentaUnidadFuncionalStr = "SELECT " +
		"coalesce(getcuentacontable(u.cuenta_ingreso),'') AS cuenta_servicio," +
		"coalesce(getcuentacontable(u.cuenta_medicamento),'') AS cuenta_medicamento " +
		"FROM unidad_fun_cuenta_ing u ";
	
	/**
	 * Cadena que consulta la cuenta pool y la cuenta ingreso pool
	 */
	private static final String consultaCuentaPoolStr = "SELECT coalesce(getcuentacontable(pet.cuenta_contable_pool),'') AS cuenta_pool, " +
							"coalesce(getcuentacontable(pet.cuenta_contable_ins),'') AS cuenta_institucion " +
							"FROM pooles_esquema_tarifario pet " +
							"WHERE pet.pool =? AND pet.esquema_tarifario =?";
	
	/**
	 * cadena que consulta la cuenta contable pool
	 */
	private static final String consultaCuentaContablePoolStr = "SELECT coalesce(getcuentacontable(cuenta_contable_pool),'') as cuenta_pool from pooles_convenio " + 
																"where convenio =? and pool =?";
	
	/**
	 * cadena que consulta la cuenta contable institucion
	 */
	private static final String consultaCuentaContableInsStr = "SELECT coalesce(getcuentacontable(cuenta_contable_ins),'') as cuenta_contable_ins from pooles_convenio " + 
																"where convenio =? and pool =?";
	
	private static final String consultaCuentaContablePositivaStr = "select coalesce(getcuentacontable(pq.cuenta_cont_may_val),'') as numeroSolicitud from paquetes " +
									"pq Inner join paquetes_convenio pc on(pq.codigo_paquete=pc.paquete and pq.institucion=pc.institucion) "+
									"inner join paquetizacion ptz on(pc.codigo=ptz.codigo_paquete_convenio) where ptz.numero_solicitud_paquete =?";

	
	private static final String consultaCuentaContableNegativaStr = "select coalesce(getcuentacontable(pq.cuenta_cont_men_val),'') as numeroSolicitud from paquetes " +
									"pq Inner join paquetes_convenio pc on(pq.codigo_paquete=pc.paquete and pq.institucion=pc.institucion) "+
									"inner join paquetizacion ptz on(pc.codigo=ptz.codigo_paquete_convenio) where ptz.numero_solicitud_paquete =?";
		
	
	private static final String consultaCentroCostoServicioStr = "select cc.identificador as identificador from solicitudes sol INNER JOIN centros_costo cc ON(cc.codigo=sol.centro_costo_solicitado) where sol.numero_solicitud=?";
	
	private static final String consultaCentroCostoArticuloStr = "select cc.identificador as identificador from solicitudes sol INNER JOIN centros_costo cc ON(cc.codigo=sol.centro_costo_solicitante) where sol.numero_solicitud=?";
	
	private static final String cadenaNitConvenioStr = "SELECT getnumeroidentificaciontercero(em.tercero) as numero_identificacion from empresas em "+
													"Inner join convenios con on(em.codigo=con.empresa) where con.codigo=?";
	
	
	private static final String consultaTipoConvenioStr = "select coalesce(tipo_convenio,'') AS tipo_convenio from convenios where codigo=?";
	
	private static final String consultaDatosEmpresaStr = "select coalesce(em.pais_principal,'') AS pais_principal, "+
																	"coalesce(em.depto_principal,'') AS depto_principal, "+
																	"coalesce(em.ciudad_principal,'') AS ciudad_principal, "+
																	"em.direccion, "+
																	"em.telefono," +
																	"coalesce(em.email,'') AS  email "+
																"from empresas em inner join convenios con on(con.empresa=em.codigo) "+
																"where con.codigo=?";
	
	private static final String consultaNaturalezaStr = "select coalesce(naturaleza_cuenta,' ') AS naturaleza_cuenta from cuentas_contables where cuenta_contable=? ";
	
	private static final String naturalezaCuentaContable = "select naturaleza_contable from cuentas_contables where cuenta_contable=?";
	
	private static final String digitoVerificadorTerceroStr = "select coalesce(ter.digito_verificacion||'','') AS digitoVerificador from convenios con INNER JOIN empresas emp " +
																"ON(emp.codigo=con.empresa) INNER JOIN terceros ter " +
																"ON(ter.codigo=emp.tercero) where con.codigo=? ";
	
	private static final String tipoTerceroStr = "select ter.tipo_tercero AS tipoTercero from convenios con INNER JOIN empresas " +
																"emp ON(emp.codigo=con.empresa) INNER JOIN terceros ter " +
																"ON(ter.codigo=emp.tercero) where con.codigo=? ";
	
	
	private static final String diasVencimientoConvenioStr = "SELECT num_dias_vencimiento as diasVencimiento from convenios where codigo=? ";
	
	
	private static final String consultaAcronimoTipoConvenioStr = "SELECT tr.acronimo AS acronimo from convenios con " +
														"INNER JOIN tipos_convenio tc ON(tc.codigo=con.tipo_convenio and tc.institucion=con.institucion) " +
														"INNER JOIN tipos_regimen tr ON(tr.acronimo=tc.tipo_regimen) where con.codigo=?";
	
	
	private static final String consultaNombresPacienteFacturaStr="SELECT per.numero_identificacion,per.primer_nombre, per.segundo_nombre, per.primer_apellido, per.segundo_apellido from personas per where per.codigo=?";
	/**************************************** CADENAS RECIBOS DE CAJA  *********************************************************
	 */
	
	private static final String consultaCentroAtencionRcStr = "SELECT c.centro_atencion as centro_atencion_rc FROM recibos_caja rc INNER JOIN cajas_cajeros ca "+
																		"ON(ca.caja=rc.caja and rc.usuario=ca.usuario) inner join cajas c "+
																		"on(c.consecutivo=ca.caja) "+
																		"WHERE rc.numero_recibo_caja=? and rc.institucion =?";
	
	
	private static final String consultaIdPacienteRcStr = "SELECT drc.numero_id_beneficiario as numero_id_beneficiario from detalle_conceptos_rc drc INNER JOIN "+
																		"conceptos_ing_tesoreria cing ON(cing.codigo=drc.concepto and cing.institucion=drc.institucion) "+
																		"where drc.numero_recibo_caja=? and drc.institucion=? ";
	
	
	private static final String consultaValorRcStr = "select valor from detalle_conceptos_rc where numero_recibo_caja=?  and institucion=? ";
	
	
	// CONSULTA EL USUARIO QUE REALIZO EL RECIBO DE CAJA
	private static final String consultaUsuarioRcStr = "SELECT usu.codigo_persona as codigo_persona from recibos_caja rc INNER JOIN cajas_cajeros cc " +
																	"ON(cc.caja=rc.caja and cc.usuario=rc.usuario) INNER JOIN usuarios usu " +
																	"ON(usu.login=cc.usuario) where rc.numero_recibo_caja=? and rc.institucion=? ";
	
	
	private static final String consultaCajaRcStr = "select caja from recibos_caja where numero_recibo_caja =? and institucion=? ";
	
	
	private static final String consultaTipoPagoRcStr = "SELECT fp.tipo_detalle as tipo_detalle from detalle_pagos_rc dpc INNER JOIN formas_pago fp "+
																	"ON(fp.consecutivo=dpc.forma_pago) where dpc.numero_recibo_caja=? and " +
																	"dpc.institucion=? ";
	
	// CONSULTA LA FORMA DE PAGO DEL RECIBO DE CAJA
	private static final String consultaFormaPagoRcStr = "select forma_pago, valor from detalle_pagos_rc where numero_recibo_caja=? and institucion=? ";
	
	
	//CONSULTA LA CUENTA CONTABLE DEL RECIBO DE CAJA
	private static final String consultaCuentaContableRcStr = "SELECT cc.cuenta_contable as cuenta_contable, cc. naturaleza_cuenta as naturaleza "+
																	"from formas_pago fp INNER JOIN cuentas_contables cc ON "+
																	"(cc.codigo=fp.cuenta_contable) where fp.consecutivo=? ";
	
	// CONSULTA EL CONCEPTO DEL RECIBO DE CAJA
	private static final String consultaConceptoRcStr = "select cing.codigo_tipo_ingreso as concepto from detalle_conceptos_rc dc INNER JOIN " +
																	"conceptos_ing_tesoreria  cing ON(cing.codigo=dc.concepto and cing.institucion=dc.institucion) " +
																	"where dc.numero_recibo_caja=? and dc.institucion=? "; 
	
	// CONSULTA LA CUENTA DEL RECIBO DE CAJA
	private static final String consultaCuentaConceptoRecaudoStr = "SELECT coalesce(getcuentacontable(cing.cuenta),'') as cuenta from detalle_conceptos_rc drc INNER JOIN conceptos_ing_tesoreria cing "+
																	"ON (cing.codigo=drc.concepto and cing.institucion=drc.institucion) " +
																	"where drc.numero_recibo_caja =? and drc.institucion=? and cing.codigo_tipo_ingreso=? ";
	
	
	// CONSULTA EL CONCEPTO DE PAGO DEL RECIBO DE CAJA
	private static final String consultaConceptoPagoRcStr = "select coalesce(concepto,'') as concepto from detalle_conceptos_rc where numero_recibo_caja=? and institucion=? ";
	
	
	// CONSULTA LA DESCRIPCION DEL CONCEPTO DE PAGO RECIBO DE CAJA
	private static final String consultaDescripConceptoPagoRcStr = "select coalesce(cit.descripcion,'') as descripcion from detalle_conceptos_rc dcr " +
																	"INNER JOIN conceptos_ing_tesoreria cit " +
																	"ON(cit.codigo=dcr.concepto and cit.institucion=dcr.institucion) " +
																	"where dcr.numero_recibo_caja=? and dcr.institucion=? ";
	
	// CONSULTA EL CODIGO DE LOS MOVIMIENTOS EN CHEQUES
	private static final String consultaBancoChequeRcStr = "select coalesce(mc.codigo_banco,'') as codigo_banco from detalle_pagos_rc dp INNER JOIN movimientos_cheques mc "+
																	"ON(mc.det_pago_rc=dp.consecutivo) where " +
																	"dp.numero_recibo_caja=? and dp.institucion=? ";
	
	// CONSULTA EL ESTADO DEL RECIBO DE CAJA
	private static final String estadoReciboRcStr = "select estado from recibos_caja where numero_recibo_caja=? and institucion=? ";
	
	
	// CONSULTA EL CAMPO INTERFAZ DE LA TABLA CONVENIOS CAMPO ARCHIVO 46
	private static final String consultaConvenioPacienteRcStr = "select coalesce(con.interfaz,'') as interfaz from pagos_general_empresa pge "+
																"INNER JOIN convenios con ON(con.codigo=pge.convenio) where " +
																"pge.documento=? " +
																"and pge.institucion=? ";
	
	
//	 CONSULTA EL CAMPO INTERFAZ DE LA TABLA CONVENIOS CAMPO ARCHIVO 124
	private static final String consultaClaseClienteRcStr = "select coalesce(con.tipo_convenio,'') as tipo_convenio from pagos_general_empresa pge "+
																"INNER JOIN convenios con ON(con.codigo=pge.convenio) where " +
																"pge.documento=? " +
																"and pge.institucion=? ";
	
	
	// CONSULTAS PARA EL TIPO DE CUENTA CONVENIO POR CUENTA CONVENIO
	private static final String consultaCuentaConvenioRcStr = "select coalesce(getcuentacontable(case when cuc.valor is null or cuc.valor = '' THEN 0 ELSE to_number(cuc.valor,'9999999999') end),'') as valor " +
																	"from pagos_general_empresa pge INNER JOIN cuenta_convenio cuc ON "+
																	"(cuc.cod_convenio=pge.convenio) " +
																	"where pge.tipo_doc=? and " +
																	"pge.documento=? and " +
																	"pge.institucion=? and " +
																	"cuc.cod_tipo_cuenta=? ";
	
	// CONSULTA PARA EL TIPO DE CUENTA CONVENIO POR CUENTA REGIMEN
	private static final String consultaCuentaConvenioRegimenRcStr = "SELECT coalesce(getcuentacontable(case when creg.valor is null or creg.valor = '' THEN 0 ELSE to_number(creg.valor,'9999999999') end),'') as valor " +
																	"from pagos_general_empresa pge INNER JOIN convenios con ON "+
																	"(con.codigo=pge.convenio) INNER JOIN cuenta_regimen creg " +
																	"ON(creg.acr_tipo_regimen=con.tipo_regimen) " +
																	"where pge.tipo_doc=? and " +
																	"pge.documento=? and " +
																	"pge.institucion=? and " +
																	"creg.cod_tipo_cuenta = ? ";
	
	
	// CONSULTA PARA EL TIPO DE CUENTA PACIENTE POR CUENTA REGIMEN
	private static final String consultaCuentaRegimenPacRcStr = "select coalesce(getcuentacontable(to_number(case when creg.valor is null or creg.valor = '' THEN '0' else creg.valor end,'9999999999')),'') as valor " +
																	"from detalle_conceptos_rc dc INNER JOIN facturas fac " +
																	"ON(fac.consecutivo_factura||''=dc.doc_soporte) INNER JOIN convenios conv " +
																	"on(fac.convenio=conv.codigo) INNER JOIN cuenta_regimen creg " +
																	"ON(creg.acr_tipo_regimen=conv.tipo_regimen and creg.cod_tipo_cuenta=? " +
																	"and creg.cod_institucion=fac.institucion) inner join conceptos_ing_tesoreria cit " +
																	"on(dc.concepto=cit.codigo and dc.institucion=cit.institucion) " +
																	"where dc.numero_recibo_caja=? " +
																	"and dc.institucion=? " +
																	"and codigo_tipo_ingreso=? ";
	
	// CAMPO DE RECIBO DE CAJA CONTABILIZADO
	public static String cadenaContabilizarReciboCajaStr = "UPDATE recibos_caja SET contabilizado_interfaz_uno ='S' where  numero_recibo_caja=? ";
	
	//CONSULTA LA NATURALEZA DEL CONCEPTO INGRESO TESORERIA PARA LOS CREDITOS
	public static String consultaNaturalezaConceptoRcStr = "select coalesce(cuec.naturaleza_cuenta,'') as naturaleza_concepto from detalle_conceptos_rc drc "+
																	"INNER JOIN conceptos_ing_tesoreria cing ON(cing.codigo=drc.concepto and cing.institucion=drc.institucion) " +
																	"INNER JOIN cuentas_contables cuec ON(cuec.codigo=cing.cuenta) " +
																	"where drc.numero_recibo_caja=? and drc.institucion=? ";
	
	
	//CONSULTA EL VALOR DE CONCEPTO DEL RECIBO DE CAJA
	
	public static String consultaValorConceptoRcStr = "select coalesce(valor,0)as valorConcepto from detalle_conceptos_rc where numero_recibo_caja=? ";
	
	
	//CONSULTA CONCEPTO DEL DETALLE RECIBO DE CAJA
	
	public static String consultaConceptoDetalleRcStr = "SELECT concepto from detalle_conceptos_rc where numero_recibo_caja =? ";
	
	
	
	public static String consultaResponsablesRcStr = "SELECT recibido_de from recibos_caja where numero_recibo_caja=?";
	
	
	public static String consultarCentroCostoConceptoRcStr ="select cc.identificador as centroCosto from detalle_conceptos_rc dc " +
										"INNER JOIN conceptos_ing_tesoreria  cing ON(cing.codigo=dc.concepto and cing.institucion=dc.institucion) " +
										"INNER JOIN centros_costo cc ON(cc.codigo=cing.codigo_centro_costo) " +
										"where dc.numero_recibo_caja=? and dc.institucion=?";
	
	//***************************************************************************************
	
	
	
	
	
	//#############################			METODOS			###############################
	
	/**
	 * 
	 */
	public static HashMap consultarFacturas(Connection con, String fechaInicial, String fechaFinal, String contabilizado) 
	{
		
		String strCadenaConsultarFacturas = "select codigo from facturas where (contabilizado=?  ";
		if(contabilizado.equals(ConstantesBD.acronimoNo))
			strCadenaConsultarFacturas +=  " or contabilizado is null ";
		strCadenaConsultarFacturas+=") and fecha between ? and ? ";
		
		HashMap mapa=new HashMap();
		mapa.put("numRegistros","0");
		PreparedStatement ps;
		try 
		{
			ps = con.prepareStatement(strCadenaConsultarFacturas, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			ps.setString(1, contabilizado);
			ps.setDate(2, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(fechaInicial)));
			ps.setDate(3, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(fechaFinal)));
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return mapa;
	}
	
	
	/**
	 * Metodo para insertar los datos del formulario de Interfaz Sistema uno en la tabla que
	 * lleva el mismo nombre.
	 * @param con
	 * @param parametros
	 * @return
	 */
	public static boolean setInterfazSistemaUno(Connection con, String tipo, String reproceso, String fechaInicial, String fechaFinal, String nombre, String path, String institucion, String usuario, int Consecutivo)
	{
		try
		{
			PreparedStatement ps = con.prepareStatement(cadenaInsercionSistemaUnoStr, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			
			logger.info("");
			logger.info("*************************  INSERTANDO REGISTRO EN TABLA INTERFAZ SISTEMA UNO  ********************** ");
			logger.info("");
			
			logger.info(" Consecutivo ---> "+Consecutivo);
			ps.setInt(1, Consecutivo); 	
			
			logger.info(" Tipo ---> "+tipo);
			ps.setString(2, tipo); 									//tipo
			
			logger.info(" Reproceso ---> "+reproceso);
			ps.setString(3, reproceso); 							//reproceso
			
			logger.info(" Fecha Inicial---> "+UtilidadFecha.conversionFormatoFechaABD(fechaInicial));
			ps.setDate(4, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(fechaInicial))); 							//fecha Inicial
			
			logger.info(" Fecha Final ---> "+UtilidadFecha.conversionFormatoFechaABD(fechaFinal));
			ps.setDate(5, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(fechaFinal))); 							// fecha Final
			
			logger.info(" Path ---> "+path);
			ps.setString(6, path);									//path
			
			logger.info(" Nombre Archivo ---> "+nombre);
			ps.setString(7, nombre+".txt"); 						//nombre
			
			logger.info(" Path Inconsistencias ---> "+path);
			ps.setString(8, path); 									//Path Inconsistencias
			
			logger.info(" Nombre Inconsistencias ---> "+"nombre");
			ps.setString(9, "Incon"+nombre+".txt"); 					//Nombre Inconsistencias
			
			logger.info(" Exitosa ---> "+ConstantesBD.acronimoSi);
			ps.setString(10, ConstantesBD.acronimoSi); 				//Exitosa
			
			logger.info(" Usuario ---> "+usuario);
			ps.setString(11, usuario); 								//Usuario
			
			logger.info(" Institucion ---> "+institucion);
			ps.setInt(12, Utilidades.convertirAEntero(institucion)); 							//Institucion
			
					
			if (ps.executeUpdate()>0)
				return true;
			
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			// TODO: handle exception
		}
		return false;
	}


	public static Object consultarCuentaContable(Connection con, String convenio, int tipoCuenta, String tipoRegimen, String institucion) {
		// TODO Auto-generated method stub
		try
		{
			PreparedStatement ps = con.prepareStatement(strCadenaCuentaConvenio, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			PreparedStatement ps1 = con.prepareStatement(strCadenaCuentaRegimen, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			//Convenio 		1
			//TipoCuenta 	2
			//Institucion	3
			ps.setInt(1, Utilidades.convertirAEntero(convenio));
			ps.setInt(2, tipoCuenta);
			ps.setString(3, institucion);
			ResultSet rs = ps.executeQuery();
			if(rs.next())
				return rs.getString("cuenta_contable");
			else
			{
				ps1.setString(1, tipoRegimen);
				ps1.setInt(2, tipoCuenta);
				ps1.setString(3, institucion);
				rs= ps1.executeQuery();
				if(rs.next())
					return rs.getString("cuenta_contable");					
			}
			
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
		return "";
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
			PreparedStatement pst = null;
			ResultSet rs = null;
			String valor = "";
			
			//Se toma el tipo de cuenta
			int tipo = Integer.parseInt(paramCuentas.get("tipoCuenta").toString());
			
		//	logger.info("MAPA ---->"+paramCuentas);
//			logger.info(">>>>>>>>>"+tipo+"<<<<<<<<<<");
			switch(tipo)
			{
				case ConstantesBD.tipoCuentaConvenio:
				case ConstantesBD.tipoCuentaPaciente:
				case ConstantesBD.tipoCuentaDescuentosPaciente:
				case ConstantesBD.tipoCuentaDevolucionPaciente:
				case ConstantesBD.tipoCuentaAbonosPaciente:
					//1) Se consulta directamente del convenio
					pst = con.prepareStatement(consultaCuentaConvenioStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
					pst.setInt(1,Utilidades.convertirAEntero(paramCuentas.get("codigoConvenio")+""));
					pst.setInt(2,Utilidades.convertirAEntero(paramCuentas.get("tipoCuenta")+""));
					pst.setInt(3,Utilidades.convertirAEntero(paramCuentas.get("institucion")+""));
					
					rs = pst.executeQuery();
					if(rs.next())
						valor = rs.getString("cuenta");
					else
						valor = "";
					
	//				logger.info("=========>>>> Valor: "+valor);
					
					if(valor.equals(""))
					{
						
						//2) Se consulta desde el tipo de regimen
						pst = con.prepareStatement(consultaCuentaTipoRegimenStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
						pst.setInt(1,Utilidades.convertirAEntero(paramCuentas.get("codigoConvenio")+""));
						pst.setInt(2,Utilidades.convertirAEntero(paramCuentas.get("tipoCuenta")+""));
						pst.setInt(3,Utilidades.convertirAEntero(paramCuentas.get("institucion")+""));
						
						rs = pst.executeQuery();
						if(rs.next())
							valor = rs.getString("cuenta");
					}
				break;
				
				case ConstantesBD.tipoCuentaServicios:
					//1) Se consulta directamente del servicio con centro costo solicitado
					pst = con.prepareStatement(consultaCuentaServicioStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
					pst.setInt(1,Utilidades.convertirAEntero(paramCuentas.get("codigoServicio")+""));
					pst.setInt(2,Utilidades.convertirAEntero(paramCuentas.get("centroCosto")+""));
					
			//		logger.info("************** Codigo Servicio     ---->"+paramCuentas.get("codigoServicio")+"<--");
			//		logger.info("************** Codigo Centro Costo ---->"+paramCuentas.get("centroCosto")+"<--");
					
					rs = pst.executeQuery();
					if(rs.next())
					{
						valor = rs.getString("cuenta");
				//		logger.info("Ejecuta consulta CC especifico ->"+consultaCuentaServicioStr);
					}	
					else
						valor = "";
					
					if(valor.equals(""))
					{
						//2) Se consulta directamente del servicio con centro costo TODOS
						pst = con.prepareStatement(consultaCuentaServicioStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
						pst.setInt(1,Utilidades.convertirAEntero(paramCuentas.get("codigoServicio")+""));
						pst.setInt(2,ConstantesBD.codigoCentroCostoTodos);
						
						rs = pst.executeQuery();
						if(rs.next())
						{
							valor = rs.getString("cuenta");
						//	logger.info("Ejecuta consulta CC TODOS ->"+consultaCuentaServicioStr);
						}	
						else
							valor = "";
						
						if(valor.equals(""))
						{
							//3) Se consulta por la especialidad del servicio y centro costo solicitado
							pst = con.prepareStatement(consultaCuentaEspecialidadServicioStr+" and e.especialidad_servicio=s.especialidad",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
							pst.setInt(1,Utilidades.convertirAEntero(paramCuentas.get("codigoServicio")+""));
							pst.setInt(2,Utilidades.convertirAEntero(paramCuentas.get("centroCosto")+""));
							
							rs = pst.executeQuery();
							if(rs.next())
							{
								valor = rs.getString("cuenta");
							//	logger.info("Ejecuta consulta Cuenta Especial Serv especifico ->"+consultaCuentaEspecialidadServicioStr);
							}
							else
								valor = "";
							
							if(valor.equals(""))
							{
								//4) Se consulta por la especialidad TODAS y centro costo solicitado
								pst = con.prepareStatement(consultaCuentaEspecialidadServicioStr+" and e.especialidad_servicio="+ConstantesBD.codigoEspecialidadMedicaNinguna,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
								pst.setInt(1,Utilidades.convertirAEntero(paramCuentas.get("codigoServicio")+""));
								pst.setInt(2,Utilidades.convertirAEntero(paramCuentas.get("centroCosto")+""));
								
								rs = pst.executeQuery();
								if(rs.next())
								{
									valor = rs.getString("cuenta");
								//	logger.info("Ejecuta consulta Cuenta Especial TODOS Serv y Centro Costo especifico ->"+consultaCuentaEspecialidadServicioStr);
								}
								else
									valor = "";
								
								if(valor.equals(""))
								{
									//5) Se consulta por la especialiad del servicio y centro de costo TODOS
									pst = con.prepareStatement(consultaCuentaEspecialidadServicioStr+" and e.especialidad_servicio=s.especialidad",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
									pst.setInt(1,Utilidades.convertirAEntero(paramCuentas.get("codigoServicio")+""));
									pst.setInt(2,ConstantesBD.codigoCentroCostoTodos);
									
									rs = pst.executeQuery();
									if(rs.next())
									{
										valor = rs.getString("cuenta");
									//	logger.info("Ejecuta consulta Cuenta Especial Serv y Centro Costo TODOS ->"+consultaCuentaEspecialidadServicioStr);
									}
									else
										valor = "";
									
									if(valor.equals(""))
									{
										//6) Se consulta por la especialidad TODAS y centro deo costo TODOS
										pst = con.prepareStatement(consultaCuentaEspecialidadServicioStr+" and e.especialidad_servicio="+ConstantesBD.codigoEspecialidadMedicaNinguna,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
										pst.setInt(1,Utilidades.convertirAEntero(paramCuentas.get("codigoServicio")+""));
										pst.setInt(2,ConstantesBD.codigoCentroCostoTodos);
										
										rs = pst.executeQuery();
										if(rs.next())
										{
											valor = rs.getString("cuenta");
										//	logger.info("Ejecuta consulta Cuenta Especial TODOS Centro Costo TODOS ->"+consultaCuentaEspecialidadServicioStr);
										}
										else
											valor = "";
										
										if(valor.equals(""))
										{
											//7) Se consulta por el tipo servicio del servicio y centro costo solicitado
											pst = con.prepareStatement(consultaCuentaTipoServicioStr+"  AND s.tipo_servicio=t.tipo_servicio",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
											pst.setInt(1,Utilidades.convertirAEntero(paramCuentas.get("codigoServicio")+""));
											pst.setInt(2,Utilidades.convertirAEntero(paramCuentas.get("centroCosto")+""));
											
											rs = pst.executeQuery();
											if(rs.next())
											{
												valor = rs.getString("cuenta");
											//	logger.info("Ejecuta consulta  por el tipo servicio del servicio y centro costo solicitado->"+consultaCuentaTipoServicioStr);
											}
											else
												valor = "";
											
											if(valor.equals(""))
											{
												//8) Se consulta por el tipo servicio TODOS y centro costo solicitado
												pst = con.prepareStatement(consultaCuentaTipoServicioStr+"  AND t.tipo_servicio='1'",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
												pst.setInt(1,Utilidades.convertirAEntero(paramCuentas.get("codigoServicio")+""));
												pst.setInt(2,Utilidades.convertirAEntero(paramCuentas.get("centroCosto")+""));
												
												rs = pst.executeQuery();
												if(rs.next())
												{
													valor = rs.getString("cuenta");
												//	logger.info("Ejecuta consulta  por el tipo servicio TODOS y centro costo solicitado->"+consultaCuentaTipoServicioStr);
												}
												else
													valor = "";
												
												if(valor.equals(""))
												{
													//9) Se consulta por el tipo servicio del servicio y centro costo TODOS
													pst = con.prepareStatement(consultaCuentaTipoServicioStr+"  AND s.tipo_servicio=t.tipo_servicio",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
													pst.setObject(1,paramCuentas.get("codigoServicio"));
													pst.setInt(2,ConstantesBD.codigoCentroCostoTodos);
													
													rs = pst.executeQuery();
													if(rs.next())
													{
														valor = rs.getString("cuenta");
													//	logger.info("Ejecuta consulta  por el tipo servicio del servicio y centro costo TODOS->"+consultaCuentaTipoServicioStr);
													}
													else
														valor = "";
													
													if(valor.equals(""))
													{
														//10) Se consulta por el tipo servicio TODOS y centro costo TODOS
														pst = con.prepareStatement(consultaCuentaTipoServicioStr+"  AND t.tipo_servicio='1'",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
														pst.setInt(1,Utilidades.convertirAEntero(paramCuentas.get("codigoServicio")+""));
														pst.setInt(2,ConstantesBD.codigoCentroCostoTodos);
														
														rs = pst.executeQuery();
														if(rs.next())
														{
															valor = rs.getString("cuenta");
														//	logger.info("Ejecuta consulta  por el tipo servicio TODOS y centro costo TODOS->"+consultaCuentaTipoServicioStr);
														}
														else
															valor = "";
														
														if(valor.equals(""))
														{
															//11) Se consulta por el grupo servicio del servicio y centro de costo solicitado
															String consulta = consultaCuentaGrupoServicioStr +
																"INNER JOIN servicios s ON(s.grupo_servicio=g.grupo_servicio) " +
																"WHERE s.codigo = ? AND g.centro_costo = ?";
															
															pst = con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
															pst.setInt(1,Utilidades.convertirAEntero(paramCuentas.get("codigoServicio")+""));
															pst.setInt(2,Utilidades.convertirAEntero(paramCuentas.get("centroCosto")+""));
															
															rs = pst.executeQuery();
															if(rs.next())
															{
																valor = rs.getString("cuenta");
																//logger.info("Ejecuta consulta  por el el grupo servicio del servicio y centro de costo solicitado ->"+consulta);
															}
															else
																valor = "";
															
															if(valor.equals(""))
															{
																//12) Se consulta por el grupo servicio TODOS y centro de costo solicitado
																consulta = consultaCuentaGrupoServicioStr +
																	" WHERE g.centro_costo = ? AND g.grupo_servicio = -1";
																pst = con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
																
																pst.setInt(1,Utilidades.convertirAEntero(paramCuentas.get("centroCosto")+""));
																
																rs = pst.executeQuery();
																if(rs.next())
																{
																	valor = rs.getString("cuenta");
																//	logger.info("Ejecuta consulta  por el grupo servicio TODOS y centro de costo solicitado ->"+consulta);
																}
																else
																	valor = "";
																
																if(valor.equals(""))
																{
																	//13) Se consulta por el grupo servicio del servicio y centro de costo TODOS
																	consulta = consultaCuentaGrupoServicioStr +
																	"INNER JOIN servicios s ON(s.grupo_servicio=g.grupo_servicio) " +
																	"WHERE s.codigo = ? AND g.centro_costo = ?";
																
																	pst = con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
																	pst.setInt(1,Utilidades.convertirAEntero(paramCuentas.get("codigoServicio")+""));
																	pst.setInt(2,ConstantesBD.codigoCentroCostoTodos);
																	
																	rs = pst.executeQuery();
																	if(rs.next())
																	{
																		valor = rs.getString("cuenta");
																	//	logger.info("Ejecuta consulta  por el grupo servicio del servicio y centro de costo TODOS ->"+consulta);
																	}
																	else
																		valor = "";
																	
																	if(valor.equals(""))
																	{
																		//14) Se consulta por el grupo de servicio TODOS y centro de costo TODOS
																		consulta = consultaCuentaGrupoServicioStr +
																		" WHERE g.centro_costo = ? AND g.grupo_servicio = -1";
																		pst = con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
																		pst.setInt(1,ConstantesBD.codigoCentroCostoTodos);
																	
																		rs = pst.executeQuery();
																		if(rs.next())
																		{
																			valor = rs.getString("cuenta");
																		//	logger.info("Ejecuta consulta  por el grupo de servicio TODOS y centro de costo TODOS ->"+consulta);
																		}
																		else
																			valor = "";
																		
																		if(valor.equals(""))
																		{
																			/** CONSULTAS CUENTA INTERFAZ UNIDAD FUNCIONAL SERVICIO **/
																			//15) Se consulta cuenta unidad funcional por centro costo solicitado
																			pst = con.prepareStatement(consultaCuentaUnidadFuncionalCentroCostoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
																			pst.setInt(1,Utilidades.convertirAEntero(paramCuentas.get("centroCosto")+""));
																			pst.setInt(2,Utilidades.convertirAEntero(paramCuentas.get("institucion")+""));
																			
																			rs = pst.executeQuery();
																			if(rs.next())
																				valor = rs.getString("cuenta_servicio");
																			
																			if(valor.equals(""))
																			{
																				//16) Se consulta cuenta por la unidad funcional del centro de costo
																				consulta = consultaCuentaUnidadFuncionalStr +
																					"INNER JOIN centros_costo c ON (c.unidad_funcional=u.unidad_funcional) " +
																					"WHERE c.codigo = ? AND u.institucion = ?";
																				pst = con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
																				pst.setInt(1,Utilidades.convertirAEntero(paramCuentas.get("centroCosto")+""));
																				pst.setInt(2,Utilidades.convertirAEntero(paramCuentas.get("institucion")+""));
																				
																				rs = pst.executeQuery();
																				if(rs.next())
																					valor = rs.getString("cuenta_servicio");
																				
																				if(valor.equals(""))
																				{
																					//17) Se consulta cuenta por unidad funcional todas
																					consulta = consultaCuentaUnidadFuncionalStr + 
																						"WHERE u.unidad_funcional IS NULL AND u.institucion = ?";
																					
																					pst = con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
																					pst.setInt(1,Utilidades.convertirAEntero(paramCuentas.get("institucion")+""));
																					
																					
																					rs = pst.executeQuery();
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
					pst = con.prepareStatement(consultaCuentaInventarioStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
					pst.setInt(1,Utilidades.convertirAEntero(paramCuentas.get("codigoArticulo")+""));
					pst.setInt(2,Utilidades.convertirAEntero(paramCuentas.get("centroCosto")+""));
					
					rs = pst.executeQuery();
					if(rs.next())
						valor = rs.getString("cuenta");
					
					if(valor.equals(""))
					{
						//2) Se consulta directamente del articulo con el centro de costo TODOS
						pst = con.prepareStatement(consultaCuentaInventarioStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
						pst.setInt(1,Utilidades.convertirAEntero(paramCuentas.get("codigoArticulo")+""));
						pst.setInt(2,ConstantesBD.codigoCentroCostoTodos);
						
						rs = pst.executeQuery();
						if(rs.next())
							valor = rs.getString("cuenta");
						
						if(valor.equals(""))
						{
							//3) Se consulta del subgrupo del articulo con el centro costo solicitante
							pst = con.prepareStatement(consultaCuentaSubgrupoInventarioStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
							pst.setInt(1,Utilidades.convertirAEntero(paramCuentas.get("codigoArticulo")+""));
							pst.setInt(2,Utilidades.convertirAEntero(paramCuentas.get("centroCosto")+""));
							
							rs = pst.executeQuery();
							if(rs.next())
								valor = rs.getString("cuenta");
							
							if(valor.equals(""))
							{
								//4) Se consulta del subgrupo del artículo con el centro costo TODOS
								pst = con.prepareStatement(consultaCuentaSubgrupoInventarioStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
								pst.setInt(1,Utilidades.convertirAEntero(paramCuentas.get("codigoArticulo")+""));
								pst.setInt(2,ConstantesBD.codigoCentroCostoTodos);
								
								rs = pst.executeQuery();
								if(rs.next())
									valor = rs.getString("cuenta");
								
								if(valor.equals(""))
								{
									//5) Se consulta del grupo del articulo con el centro de costo solicitante
									pst = con.prepareStatement(consultaCuentaGrupoInventarioStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
									pst.setInt(1,Utilidades.convertirAEntero(paramCuentas.get("codigoArticulo")+""));
									pst.setInt(2,Utilidades.convertirAEntero(paramCuentas.get("centroCosto")+""));
									
									rs = pst.executeQuery();
									if(rs.next())
										valor = rs.getString("cuenta");
									
									if(valor.equals(""))
									{
										//6) Se consulta del grupo del articulo con el centro de costo TODOS
										pst = con.prepareStatement(consultaCuentaGrupoInventarioStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
										pst.setInt(1,Utilidades.convertirAEntero(paramCuentas.get("codigoArticulo")+""));
										pst.setInt(2,ConstantesBD.codigoCentroCostoTodos);
										
										rs = pst.executeQuery();
										if(rs.next())
											valor = rs.getString("cuenta");
										
										if(valor.equals(""))
										{
											//7) Se consulta de la clase del articulo con el centro de costo solicitante
											pst = con.prepareStatement(consultaCuentaClaseInventarioStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
											pst.setInt(1,Utilidades.convertirAEntero(paramCuentas.get("codigoArticulo")+""));
											pst.setInt(2,Utilidades.convertirAEntero(paramCuentas.get("centroCosto")+""));
											
											rs = pst.executeQuery();
											if(rs.next())
												valor = rs.getString("cuenta");
											
											if(valor.equals(""))
											{
												//8) Se consulta de la clase del articulo con el centro de costo TODOS
												pst = con.prepareStatement(consultaCuentaClaseInventarioStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
												pst.setInt(1,Utilidades.convertirAEntero(paramCuentas.get("codigoArticulo")+""));
												pst.setInt(2,ConstantesBD.codigoCentroCostoTodos);
												
												rs = pst.executeQuery();
												if(rs.next())
													valor = rs.getString("cuenta");
												
												if(valor.equals(""))
												{
													/** CONSULTA CUENTA INTERFAZ UNIDAD FUNCIONAL MEDICAMENTO **/
													//9) Se consulta cuenta unidad funcional por centro costo solicitante
													pst = con.prepareStatement(consultaCuentaUnidadFuncionalCentroCostoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
													pst.setInt(1,Utilidades.convertirAEntero(paramCuentas.get("centroCosto")+""));
													pst.setInt(2,Utilidades.convertirAEntero(paramCuentas.get("institucion")+""));
													
													rs = pst.executeQuery();
													if(rs.next())
														valor = rs.getString("cuenta_medicamento");
													
													if(valor.equals(""))
													{
														//10) Se consulta cuenta por la unidad funcional del centro de costo
														String consulta = consultaCuentaUnidadFuncionalStr +
															"INNER JOIN centros_costo c ON (c.unidad_funcional=u.unidad_funcional) " +
															"WHERE c.codigo = ? AND u.institucion = ?";
														pst = con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
														pst.setInt(1,Utilidades.convertirAEntero(paramCuentas.get("centroCosto")+""));
														pst.setInt(2,Utilidades.convertirAEntero(paramCuentas.get("institucion")+""));
														
														rs = pst.executeQuery();
														if(rs.next())
															valor = rs.getString("cuenta_medicamento");
														
														if(valor.equals(""))
														{
															//11) Se consulta cuenta por unidad funcional todas
															consulta = consultaCuentaUnidadFuncionalStr + 
																"WHERE u.unidad_funcional IS NULL AND u.institucion = ?";
															
															pst = con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
															
															pst.setInt(1,Utilidades.convertirAEntero(paramCuentas.get("institucion")+""));
															
															rs = pst.executeQuery();
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
									
					boolean consulto = false;
					if(Integer.parseInt(paramCuentas.get("codigoConvenio").toString())>0)
					{
					//	logger.info("Codigo Convenio ->"+paramCuentas.get("codigoConvenio"));
					//	logger.info("Codigo Pool ->"+paramCuentas.get("codigoPool"));
						
						//consulta por convenio
						pst = con.prepareStatement(consultaCuentaContablePoolStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
						
						pst.setInt(1,Utilidades.convertirAEntero(paramCuentas.get("codigoConvenio")+""));
						pst.setInt(2,Utilidades.convertirAEntero(paramCuentas.get("codigoPool")+""));
						
						rs = pst.executeQuery();
						if(rs.next())
						{
						//	logger.info("TIPO CUENTA ->"+tipo);
							if(tipo==ConstantesBD.tipoCuentaPool)
							{
								valor = rs.getString("cuenta_pool");
							//	logger.info("VALOR POOL ->"+valor);
							}
							else
							{
								valor = rs.getString("cuenta_institucion");
							}
							consulto=true;	
						}
						
						
					}
					
					
					if(!consulto)
					{
						pst = con.prepareStatement(consultaCuentaPoolStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
						
						pst.setInt(1,Utilidades.convertirAEntero(paramCuentas.get("codigoPool")+""));
						pst.setInt(2,Utilidades.convertirAEntero(paramCuentas.get("esquemaTarifario")+""));
						
						
						rs = pst.executeQuery();
						if(rs.next())
						{
						//	logger.info("ENTRÓ +++++");
							if(tipo==ConstantesBD.tipoCuentaPool)
								valor = rs.getString("cuenta_pool");
							else
								valor = rs.getString("cuenta_institucion");
						}
					}
				break;
				
				case ConstantesBD.tipoCuentaIngresoPool:
					
					boolean consult = false;
					if(Integer.parseInt(paramCuentas.get("codigoConvenio").toString())>0)
					{
						//consulta por convenio
						pst = con.prepareStatement(consultaCuentaContableInsStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
					//	logger.info("++++++++++CodigoConvenio-->>"+paramCuentas.get("codigoConvenio")+"<<<--");
					//	logger.info("++++++++++CodigoPool-->>"+paramCuentas.get("codigoPool")+"<<<--");
						
						pst.setInt(1,Utilidades.convertirAEntero(paramCuentas.get("codigoConvenio")+""));
						pst.setInt(2,Utilidades.convertirAEntero(paramCuentas.get("codigoPool")+""));
						
						rs = pst.executeQuery();
						
						
						if(rs.next())
						{
							if(tipo==ConstantesBD.tipoCuentaPool)
							{
								valor = rs.getString("cuenta_pool");
							}
							else
							{
								valor = rs.getString("cuenta_institucion");
							}
							consult=true;	
						}
						
						
					}
					
					
					if(!consult)
					{
						pst = con.prepareStatement(consultaCuentaPoolStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
						
						pst.setInt(1,Utilidades.convertirAEntero(paramCuentas.get("codigoPool")+""));
						pst.setInt(2,Utilidades.convertirAEntero(paramCuentas.get("esquemaTarifario")+""));
						
						
						
					//	logger.info("++++++++++CodigoPool-->>"+paramCuentas.get("codigoPool")+"<<<--");
					//	logger.info("++++++++++Esquema Tarifario-->>"+paramCuentas.get("esquemaTarifario")+"<<<--");
						rs = pst.executeQuery();
						if(rs.next())
						{
							if(tipo==ConstantesBD.tipoCuentaPool)
								valor = rs.getString("cuenta_pool");
							else
								valor = rs.getString("cuenta_institucion");
						}
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
	 * Metodo para Retornar el Valor de la Utilidad o Perdida cuando esta es diferente a cero
	 * @param con
	 * @param numeroSolicitud
	 * @param institucion
	 * @return
	 */
	public static Object valorCuentaContable(Connection con, int numeroSolicitud, int institucion) 
	{
		try
		{
			PreparedStatement ps = con.prepareStatement(consultaCuentaContablePositivaStr, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			PreparedStatement ps1 = con.prepareStatement(consultaCuentaContableNegativaStr, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			
			//Numero Solicitud Paquete 		1

			if(numeroSolicitud > 0)
			{
				ps.setInt(1, numeroSolicitud);
				ResultSet rs = ps.executeQuery();
				if(rs.next())
					return rs.getString("numeroSolicitud");	
			}
			else
			{
				ps1.setInt(1, numeroSolicitud);
				ResultSet rs = ps1.executeQuery();
				if(rs.next())
					return rs.getString("numeroSolicitud");	
			}
			
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
		return "";
	}


	public static Object centroCostoSolicitaArti(Connection con, int numeroSolicitud) 
	{
		try
		{
			PreparedStatement ps = con.prepareStatement(consultaCentroCostoArticuloStr, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			
			if(numeroSolicitud > 0)
			{
				ps.setInt(1, numeroSolicitud);
				ResultSet rs = ps.executeQuery();
				if(rs.next())
					return rs.getString("identificador");	
			}
			
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
		return "";
	}


	public static Object centroCostoSolicitaServ(Connection con, int numeroSolicitud) 
	{
		try
		{
			PreparedStatement ps = con.prepareStatement(consultaCentroCostoServicioStr, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			
			if(numeroSolicitud > 0)
			{
				ps.setInt(1, numeroSolicitud);
				ResultSet rs = ps.executeQuery();
				if(rs.next())
					return rs.getString("identificador");	
			}
						
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
		return "";
	}


	public static Object nitConvenio(Connection con, int codigo) 
	{
		try
		{
			PreparedStatement ps = con.prepareStatement(cadenaNitConvenioStr, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			
			if(codigo > 0)
			{
				ps.setInt(1, codigo);
				ResultSet rs = ps.executeQuery();
				if(rs.next())
					return rs.getString("numero_identificacion");	
			}
						
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
		return "";
	}


	public static Object tipoConvenio(Connection con, int codigo) 
	{
		try
		{
			PreparedStatement ps = con.prepareStatement(consultaTipoConvenioStr, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			
			if(codigo > 0)
			{
				ps.setInt(1, codigo);
				ResultSet rs = ps.executeQuery();
				if(rs.next())
				{
					return rs.getString("tipo_convenio");
				}
				
			}
			
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
		return "";
	}


	public static HashMap datoEmpresa(Connection con, int codigo) 
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros","0");
		try 
		{
			PreparedStatement ps = con.prepareStatement(consultaDatosEmpresaStr, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			ps.setInt(1, codigo);
			
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return mapa;
	}

	/**
	 * Funcion Para marcar los registros de cada factura a la que se realice el proceso de plano Interfaz Sistema Uno
	 * @param con
	 * @param codigo
	 * @return
	 */
	public static boolean marcaFactura(Connection con, int codigo) 
	{
		try
		{
			PreparedStatement ps = con.prepareStatement(cadenaContabilizarFacturaStr, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			
			logger.info("");
			logger.info("*****************  REGISTRANDO MARCA EN LA FACTURA  ********************** ");
			logger.info("");
			
			logger.info(" Factura ---> "+codigo);
			
			
			if(codigo > 0)
			{
				ps.setInt(1, codigo);
								
				if (ps.executeUpdate()>0)
					return true;
			}
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
		return false;
	}

	/**
	 * Para buscar la Naturaleza de la cuenta
	 * Debo enviarle en año, la institucion y la cuenta contable.
	 * @param con
	 * @param ccontable
	 * @return
	 */
	public static String naturalezaTrans(Connection con, String ccontable) 
	{
		try
			{
				PreparedStatement ps = con.prepareStatement(consultaNaturalezaStr, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
				ps.setString(1, ccontable);
				ResultSet rs = ps.executeQuery();
				if(rs.next())
					return rs.getString("naturaleza_cuenta");	
							
			}
			catch (SQLException e) 
			{
				e.printStackTrace();
			}
			
			return "";
	}


	public static HashMap consultarRecibosCaja(Connection con, String fechaInicial, String fechaFinal, String reproceso) 
	{
		String strCadenaConsultarRecibosCaja = "select  numero_recibo_caja, fecha, hora, institucion from recibos_caja where (contabilizado_interfaz_uno=?  ";
		if(reproceso.equals(ConstantesBD.acronimoNo))
			strCadenaConsultarRecibosCaja +=  " or contabilizado_interfaz_uno is null ";
		strCadenaConsultarRecibosCaja+=") and fecha between ? and ? ";
		
		HashMap mapa=new HashMap();
		mapa.put("numRegistros","0");
		PreparedStatement ps;
		try 
		{
			ps = con.prepareStatement(strCadenaConsultarRecibosCaja, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			ps.setString(1, reproceso);
			ps.setDate(2, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(fechaInicial)));
			ps.setDate(3, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(fechaFinal)));
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
			logger.info("MAPA EN EL SQL"+mapa);
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return mapa;	
	}


	public static Object centroAtencionRc(Connection con, String codigoRc, String institucion) 
	{
			try
			{
				PreparedStatement ps = con.prepareStatement(consultaCentroAtencionRcStr, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
				ps.setString(1, codigoRc);
				ps.setInt(2, Utilidades.convertirAEntero(institucion));
				ResultSet rs = ps.executeQuery();
				if(rs.next())
					return rs.getString("centro_atencion_rc");	
							
			}
			catch (SQLException e) 
			{
				e.printStackTrace();
			}
			
			return "";
		
	}


	public static Object idPacienteRc(Connection con, String codigoRc, String institucion) 
	{
		try
		{
			PreparedStatement ps = con.prepareStatement(consultaIdPacienteRcStr, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			ps.setString(1, codigoRc);
			ps.setInt(2, Utilidades.convertirAEntero(institucion));
			ResultSet rs = ps.executeQuery();
			if(rs.next())
				return rs.getString("numero_id_beneficiario");	
						
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
		return "";
	}

	// BUscar el valor total del rc
	public static Object valorTotalRc(Connection con, String codigoRc, String institucion) 
	{
		try
		{
			PreparedStatement ps = con.prepareStatement(consultaValorRcStr, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			ps.setString(1, codigoRc);
			ps.setInt(2, Utilidades.convertirAEntero(institucion));
			ResultSet rs = ps.executeQuery();
			if(rs.next())
				return rs.getString("valor");	
						
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
		return "";
	}


	public static Object usuarioCajero(Connection con, String codigoRc, String institucion) 
	{
		try
		{
			PreparedStatement ps = con.prepareStatement(consultaUsuarioRcStr, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			ps.setString(1, codigoRc);
			ps.setInt(2, Utilidades.convertirAEntero(institucion));
			ResultSet rs = ps.executeQuery();
			if(rs.next())
				return rs.getString("codigo_persona");	
						
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
		return "";
	}


	public static Object codigoCajaRc(Connection con, String codigoRc, String institucion) 
	{
		try
		{
			PreparedStatement ps = con.prepareStatement(consultaCajaRcStr, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			ps.setString(1, codigoRc);
			ps.setInt(2, Utilidades.convertirAEntero(institucion));
			ResultSet rs = ps.executeQuery();
			if(rs.next())
				return rs.getString("caja");	
						
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
		return "";
	}


	public static Object tipoPagoRc(Connection con, String codigoRc, String institucion) 
	{
		try
		{
			PreparedStatement ps = con.prepareStatement(consultaTipoPagoRcStr, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			ps.setString(1, codigoRc);
			ps.setInt(2, Utilidades.convertirAEntero(institucion));
			ResultSet rs = ps.executeQuery();
			if(rs.next())
				return rs.getString("tipo_detalle");	
						
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
		return "";
	}


	public static HashMap formasPagoRc(Connection con, String codigoRc, String institucion) 
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros","0");
		
		try 
		{
			PreparedStatement ps = con.prepareStatement(consultaFormaPagoRcStr, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			ps.setString(1, codigoRc);
			ps.setInt(2, Utilidades.convertirAEntero(institucion));
			
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return mapa;
	}


	public static HashMap cContableyNaturaleza(Connection con, int formaPago) 
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros","0");
		
		try 
		{
			PreparedStatement ps = con.prepareStatement(consultaCuentaContableRcStr, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			ps.setInt(1, formaPago);
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return mapa;
	}


	public static String tipoConceptoRc(Connection con, String codigoRc, String institucion) 
	{
		try
		{
			PreparedStatement ps = con.prepareStatement(consultaConceptoRcStr, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			ps.setString(1, codigoRc);
			ps.setInt(2, Utilidades.convertirAEntero(institucion));
			ResultSet rs = ps.executeQuery();
			if(rs.next())
				return rs.getString("concepto");	
						
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
		return "";
	}


	public static String conceptoIngTesoreriaRc(Connection con, String codigoRc, String institucion, String codigoTipoIngreso) 
	{
		try
		{
			PreparedStatement ps = con.prepareStatement(consultaCuentaConceptoRecaudoStr, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			ps.setString(1, codigoRc);
			ps.setInt(2, Utilidades.convertirAEntero(institucion));
			ps.setInt(3, Utilidades.convertirAEntero(codigoTipoIngreso));
			ResultSet rs = ps.executeQuery();
			if(rs.next())
				return rs.getString("cuenta");	
						
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
		return "";
	}


	public static String cuentaConvenioRc(Connection con, String codigoRc, String institucion, String tipoIngresoTesoreria) 
	{
		try
		{
			PreparedStatement ps = con.prepareStatement(consultaCuentaConvenioRcStr, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			ps.setInt(1, ConstantesBD.codigoTipoDocumentoPagosReciboCaja);
			ps.setString(2, codigoRc);
			ps.setInt(3, Utilidades.convertirAEntero(institucion));
			ps.setInt(4, ConstantesBD.tipoCuentaConvenio);
			
			ResultSet rs = ps.executeQuery();
			if(rs.next())
				return rs.getString("valor");	
						
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
		return "";
	}


	public static String cuentaRegimenRc(Connection con, String codigoRc, String institucion, String conceptoRc) 
	{
		try
		{
			PreparedStatement ps = con.prepareStatement(consultaCuentaConvenioRegimenRcStr, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			ps.setInt(1, ConstantesBD.codigoTipoDocumentoPagosReciboCaja);
			ps.setString(2, codigoRc);
			ps.setInt(3, Utilidades.convertirAEntero(institucion));
			ps.setInt(4, ConstantesBD.tipoCuentaConvenio);
			
			ResultSet rs = ps.executeQuery();
			if(rs.next())
				return rs.getString("valor");	
						
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
		return "";
	}


	public static String cuentaPacienteConvenioRc(Connection con, String codigoRc, String institucion, String conceptoRc) 
	{
		try
		{
			String consultaCuentaConvenioPacRcStr = "select coalesce(getcuentacontable(to_number(case when cco.valor is null or cco.valor = '' THEN '0' else cco.valor end,'9999999999')),'')  as valor " +
														"from detalle_conceptos_rc dc "+
														"INNER JOIN facturas fac ON(fac.consecutivo_factura||''=dc.doc_soporte) " +
														"INNER JOIN cuenta_convenio cco " +
														"ON(cco.cod_convenio=fac.convenio and " +
														"cco.cod_tipo_cuenta=? " +
														"and cco.cod_institucion=fac.institucion) inner join conceptos_ing_tesoreria cit " +
														"on(dc.concepto=cit.codigo and dc.institucion=cit.institucion)  " +
														"where dc.numero_recibo_caja='"+codigoRc+"'"+
														"and dc.institucion=? " +
														"and codigo_tipo_ingreso=? ";
														
			PreparedStatement ps = con.prepareStatement(consultaCuentaConvenioPacRcStr, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			ps.setInt(1, ConstantesBD.tipoCuentaPaciente);
			//ps.setString(2, codigoRc);
			ps.setInt(2, Utilidades.convertirAEntero(institucion));
			ps.setInt(3, Utilidades.convertirAEntero(conceptoRc));//es el mismo ConstantesBD.codigoTipoIngresoTesoreriaPacientes.
			
			ResultSet rs = ps.executeQuery();
			if(rs.next())
				return rs.getString("valor");	
						
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
		return "";
	}


	public static String cuentaPacienteRegimenRc(Connection con, String codigoRc, String institucion, String conceptoRc) 
	{
		try
		{
			PreparedStatement ps = con.prepareStatement(consultaCuentaRegimenPacRcStr, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			ps.setInt(1, ConstantesBD.tipoCuentaPaciente);
			ps.setString(2, codigoRc);
			ps.setInt(3, Utilidades.convertirAEntero(institucion));
			ps.setInt(4, Utilidades.convertirAEntero(conceptoRc));//es el mismo ConstantesBD.codigoTipoIngresoTesoreriaPacientes.
			
			ResultSet rs = ps.executeQuery();
			if(rs.next())
				return rs.getString("valor");	
						
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
		return "";
	}


	public static String conceptoPagoRc(Connection con, String codigoRc, String institucion) 
	{
		try
		{
			PreparedStatement ps = con.prepareStatement(consultaConceptoPagoRcStr, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			ps.setString(1, codigoRc);
			ps.setInt(2, Utilidades.convertirAEntero(institucion));
			ResultSet rs = ps.executeQuery();
			if(rs.next())
				return rs.getString("concepto");	
						
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
		return "";
	}


	public static String descripConceptoPagoRc(Connection con, String codigoRc, String institucion) 
	{
		try
		{
			PreparedStatement ps = con.prepareStatement(consultaDescripConceptoPagoRcStr, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			ps.setString(1, codigoRc);
			ps.setInt(2, Utilidades.convertirAEntero(institucion));
			ResultSet rs = ps.executeQuery();
			if(rs.next())
				return rs.getString("descripcion");	
						
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
		return "";
	}


	public static String interfazConvenioPacienteRc(Connection con, String codigoRc, String institucion) 
	{
		try
		{
			PreparedStatement ps = con.prepareStatement(consultaConvenioPacienteRcStr, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			ps.setString(1, codigoRc);
			ps.setInt(2, Utilidades.convertirAEntero(institucion));
			ResultSet rs = ps.executeQuery();
			if(rs.next())
				return rs.getString("interfaz");	
						
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
		return "";
	}


	public static String bancoChequeRc(Connection con, String codigoRc, String institucion) 
	{
		try
		{
			PreparedStatement ps = con.prepareStatement(consultaBancoChequeRcStr, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			ps.setString(1, codigoRc);
			ps.setInt(2, Utilidades.convertirAEntero(institucion));
			ResultSet rs = ps.executeQuery();
			if(rs.next())
				return rs.getString("codigo_banco");	
						
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
		return "";
	}


	public static boolean marcaReciboCaja(Connection con, String codigoRc) 
	{
		try
		{
			PreparedStatement ps = con.prepareStatement(cadenaContabilizarReciboCajaStr, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			
			logger.info("");
			logger.info("*****************  REGISTRANDO MARCA EN EL RECIBO DE CAJA  ********************** ");
			logger.info("");
			
			logger.info(" Recibo Caja ---> "+codigoRc);
			
			
			if(Integer.parseInt(codigoRc) > 0)
			{
				ps.setString(1, codigoRc);
								
				if (ps.executeUpdate()>0)
					return true;
			}
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
		return false;
	}


	public static int estadoRc(Connection con, String codigoRc, String institucion) 
	{
		try
		{
			PreparedStatement ps = con.prepareStatement(estadoReciboRcStr, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			ps.setString(1, codigoRc);
			ps.setInt(2, Utilidades.convertirAEntero(institucion));
			ResultSet rs = ps.executeQuery();
			if(rs.next())
				return rs.getInt("estado");	
						
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
		return 0;
	}


	public static String naturalezaConceptoRc(Connection con, String codigoRc, String institucion) 
	{
		try
		{
			PreparedStatement ps = con.prepareStatement(consultaNaturalezaConceptoRcStr, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			ps.setString(1, codigoRc);
			ps.setInt(2, Utilidades.convertirAEntero(institucion));
			ResultSet rs = ps.executeQuery();
			if(rs.next())
				return rs.getString("naturaleza_concepto");	
						
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
		return "";
	}


	public static String valorConceptoRc(Connection con, String codigoRc, String institucion) 
	{
		try
		{
			PreparedStatement ps = con.prepareStatement(consultaValorConceptoRcStr, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			ps.setString(1, codigoRc);
			ResultSet rs = ps.executeQuery();
			if(rs.next())
				return rs.getString("valorConcepto");	
						
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
		return "";
	}


	public static String digitoVerificadorTercero(Connection con, int codigo) 
	{
		try
		{
			PreparedStatement ps = con.prepareStatement(digitoVerificadorTerceroStr, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			ps.setInt(1, codigo);
			ResultSet rs = ps.executeQuery();
			if(rs.next())
				return rs.getString("digitoVerificador");	
						
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
		return "";
	}


	public static int tipoTercero(Connection con, int codigo) 
	{
		try
		{
			PreparedStatement ps = con.prepareStatement(tipoTerceroStr, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			ps.setInt(1, codigo);
			ResultSet rs = ps.executeQuery();
			if(rs.next())
				return rs.getInt("tipoTercero");	
						
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
		return 0;
	}


	public static String claseClienteRc(Connection con, String codigoRc, String institucion) 
	{
		try
		{
			PreparedStatement ps = con.prepareStatement(consultaClaseClienteRcStr, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			ps.setString(1, codigoRc);
			ps.setInt(2, Utilidades.convertirAEntero(institucion));
			ResultSet rs = ps.executeQuery();
			if(rs.next())
				return rs.getString("tipo_convenio");	
						
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
		return "";
	}


	public static int diasVencimientoFactura(Connection con, String convenio) 
	{
		try
		{
			PreparedStatement ps = con.prepareStatement(diasVencimientoConvenioStr, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			ps.setString(1, convenio);
			ResultSet rs = ps.executeQuery();
			if(rs.next())
				return rs.getInt("diasVencimiento");	
						
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
		return 0;
	}


	public static HashMap nombresPacienteRc(Connection con, String codigoRc) 
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros","0");


		String consultaNomApellPacRcStr = "SELECT per.primer_nombre, per.segundo_nombre, per.primer_apellido, per.segundo_apellido " +
															"FROM personas per INNER JOIN facturas fac ON(fac.cod_paciente=per.codigo) " +
															"INNER JOIN detalle_conceptos_rc detrc ON(detrc.doc_soporte=fac.consecutivo_factura||'') " +
															"WHERE detrc.numero_recibo_caja= '"+codigoRc+"'";
		

		try 
		{
			PreparedStatement ps = con.prepareStatement(consultaNomApellPacRcStr, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return mapa;
	}


	public static String cuentaContablePaquete(Connection con, int numeroSolicitud, boolean utilidad) 
	{
		
		String cuentaContablePaqueteStr="";
		
		if(utilidad==true)
		{	
			cuentaContablePaqueteStr = "SELECT coalesce(getcuentacontable(cuenta_cont_may_val),'') as cuentaPaquete " +
			"from paquetes pq INNER JOIN paquetes_convenio pc ON(pc.paquete=pq.codigo_paquete and pc.institucion=pq.institucion) " +
			"INNER JOIN paquetizacion pz ON(pz.codigo_paquete_convenio=pc.codigo) " +
			"where pz.numero_solicitud_paquete=?";
		}
		else
		{
			cuentaContablePaqueteStr = "SELECT coalesce(getcuentacontable(cuenta_cont_men_val),'') as cuentaPaquete " +
			"from paquetes pq INNER JOIN paquetes_convenio pc ON(pc.paquete=pq.codigo_paquete and pc.institucion=pq.institucion) " +
			"INNER JOIN paquetizacion pz ON(pz.codigo_paquete_convenio=pc.codigo) " +
			"where pz.numero_solicitud_paquete=?";
		}
		
		try
		{
			logger.info("CONSULTA CUENTA PAQUETES--->"+cuentaContablePaqueteStr);
			PreparedStatement ps = con.prepareStatement(cuentaContablePaqueteStr, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			ps.setInt(1, numeroSolicitud);
			ResultSet rs = ps.executeQuery();
			if(rs.next())
				return rs.getString("cuentaPaquete");	
						
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
		return "";
	}


	public static String responsablesRC(Connection con, String responsable) 
	{
		try
		{
			PreparedStatement ps = con.prepareStatement(consultaResponsablesRcStr, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			ps.setString(1, responsable);
			ResultSet rs = ps.executeQuery();
			if(rs.next())
				return rs.getString("recibido_de");	
						
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
		return "";
	}


	public static String centroCostoConceptoRc(Connection con, String codigoRc, String institucion) 
	{
		try
		{
			PreparedStatement ps = con.prepareStatement(consultarCentroCostoConceptoRcStr, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			ps.setString(1, codigoRc);
			ps.setInt(2, Utilidades.convertirAEntero(institucion));
			ResultSet rs = ps.executeQuery();
			if(rs.next())
				return rs.getString("centroCosto");	
						
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
		return "";
	}


	public static String acronimoTipoConvenio(Connection con, int codigo) 
	{
		try
		{
			PreparedStatement ps = con.prepareStatement(consultaAcronimoTipoConvenioStr, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			ps.setInt(1, codigo);
			
			ResultSet rs = ps.executeQuery();
			if(rs.next())
				return rs.getString("acronimo");	
						
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
		return "";
	}


	public static HashMap nombresPacienteFac(Connection con, int codigoPaciente) 
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros","0");
		
		try 
		{
			PreparedStatement ps = con.prepareStatement(consultaNombresPacienteFacturaStr, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			ps.setInt(1, codigoPaciente);
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return mapa;
	}


		
}