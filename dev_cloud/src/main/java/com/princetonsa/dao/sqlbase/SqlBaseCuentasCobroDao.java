package com.princetonsa.dao.sqlbase;


import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.cartera.DtoDetalleReporteCuentaCobro;
import com.princetonsa.dto.cartera.DtoFiltroReporteCuentasCobro;
import com.princetonsa.dto.cartera.DtoResultadoReporteCuentaCobro;

/**
 * @author armando
 *Dao Gen�rico que maneja las transacciones en Bases de Datos de las Cuentas
 *de Cobro del m�dulo Cartera
 *
 */
public class SqlBaseCuentasCobroDao {
	
	/**
	 * Variable para manejar los log de la clase
	 */
	private static Logger logger = Logger.getLogger(SqlBaseCuentasCobroDao.class);
	
	
	/**
	 * Cadena para consultar los codigos de las facturas relacionadas a una cuenta de cobro.
	 */
	private static final String facturasCuentasCobro="SELECT " +
															" f.codigo as codigofactura," +
															" f.consecutivo_factura as consecutivo," +
															" f.centro_aten as codigocentroatencion," +
															" f.estado_paciente AS codigo_estado_paciente, " +
															" getnomcentroatencion(f.centro_aten) as centroatencion," +
															" to_char(f.fecha,'dd/mm/yyyy') as fecha," +
															" CASE WHEN p.codigo IS NULL THEN '' ELSE p.tipo_identificacion || ' ' || p.numero_identificacion END AS identificacion_paciente, " +
															" getnombrepersona(p.codigo) as nombrepersona," +
															" f.via_ingreso as viaingreso," +
															" getnombreviaingreso(f.via_ingreso) as nombreviaingreso," +
															" f.valor_cartera as valorcartera," +
															" f.valor_total as valortotal," +
															" f.tipo_factura_sistema as facturasistema, " +
															" coalesce(f.tipo_monto,"+ConstantesBD.codigoNuncaValido+") AS codigo_tipo_monto, " +
															" coalesce(f.valor_bruto_pac,0) AS valor_bruto_pac " +
														"FROM " +
															"facturas f " +
														"LEFT OUTER " +
															"join personas p on(f.cod_paciente=p.codigo) " +
														"WHERE " +
															"numero_cuenta_cobro=? and institucion=? " +
														"ORDER BY " +
															"10,f.consecutivo_factura ";
	
	/**
	 * Cadena para consultar los movientos que tiene una cuenta de cobro.
	 */
	private static final String movimientosCuentasCobro="SELECT " +
															"numero_cuenta_cobro AS numero_cuenta_cobro," +
															"usuario AS usuario," +
															"fecha_movimiento AS fecha_movimiento," +
															"hora_movimiento AS hora_movimiento," +
															"observacion AS observacion," +
															"tipo_movimiento AS tipo_movimiento " +
														"FROM " +
															"movimientos_cxc " +
														"WHERE " +
															"numero_cuenta_cobro =? AND institucion = ?";
	
	
	/**
	 * Cadena para ordenar las consultas de cuentas_cobro por su numero_cuenta_cobro
	*/
	private static final String cargarViasIngreso=	"SELECT via_ingreso as codigoviaingreso, getnombreviaingreso(via_ingreso) as nombreviaingreso from vias_ingreso_cxc where numero_cuenta_cobro =? and institucion=?";

	/**
	 * Cadena para insertar el encabezado de una Cuenta de Cobro
	*/
	private static final String insertarCabezaCuentaCobro=	"insert into cuentas_cobro (numero_cuenta_cobro,convenio,estado,fecha_elaboracion,hora_elaboracion,usuario_genera,valor_inicial_cuenta,saldo_cuenta,fecha_inicial,fecha_final,obs_generacion,institucion,centro_atencion) values (?,?,?,?,?,?,?,?,?,?,?,?,?)";


	/**
	 * Cadena para insertar el encabezado de una Cuenta de Cobro
	*/
	private static final String insertarViasIngresoCuentaCobro=	"insert into vias_ingreso_cxc (numero_cuenta_cobro, via_ingreso,institucion) values (?,?,?)";

	/**
	 * Cadena para actualizar la cuenta de cobro para las facturas.
	 */
	private static final String actualizarFacturasCuentaCobro="UPDATE facturas SET numero_cuenta_cobro = ? where codigo = ? AND institucion = ?";

	/**
	 * Cadena para organizar al cargar el detallde de la impresion resumida por numero de factura
	 */
	private static final String orderByCargarCuentaCobroStr= "order by f.consecutivo_factura ASC";
															
	
	
	
	private static final String cargarCuentaCobro="SELECT " +
																"cxc.numero_cuenta_cobro as numero_cuenta_cobro, " +
																"cxc.convenio as codigoconvenio, " +
																"c.nombre as nombreconvenio, " +
																"'' as tipo_id_responsable, " +
																"'' as nom_id_responsable, " +
																"t.numero_identificacion as numero_id_responsable, " +
																"cxc.estado as estado, " +
																"to_char(cxc.fecha_elaboracion, '"+ConstantesBD.formatoFechaBD+"') as fecha_elaboracion, " +
																"cxc.hora_elaboracion as hora_elaboracion, " +
																"cxc.usuario_genera as usuario_genera, " +
																//"to_char(cxc.fecha_radicacion, '"+ConstantesBD.formatoFechaBD+"') as fecha_radicacion, " +
																"cxc.fecha_radicacion as fecha_radicacion, " +
																"cxc.numero_radicacion as numero_radicacion, " +
																"cxc.usuario_radica as usuario_radica, " +
																"cxc.valor_inicial_cuenta as valor_inicial_cuenta, " +
																"cxc.saldo_cuenta as saldo_cuenta, " +
																"to_char(cxc.fecha_inicial, '"+ConstantesBD.formatoFechaBD+"') as fecha_inicial, " +
																"to_char(cxc.fecha_final, '"+ConstantesBD.formatoFechaBD+"') as fecha_final, " +
																"to_char(cxc.fecha_aprobacion, '"+ConstantesBD.formatoFechaBD+"') as fecha_aprobacion, " +
																"CASE WHEN cxc.obs_generacion IS NULL THEN '' ELSE cxc.obs_generacion END as obs_generacion, " +
																"cxc.obs_radicacion as obs_radicacion," +
																"case when cxc.centro_atencion is null then -1 else cxc.centro_atencion end as codigocentroatencion," +
																"case when cxc.centro_atencion is null then 'Todos' else administracion.getnomcentroatencion(cxc.centro_atencion) end as nombrecentroatencion," +
																"e.direccion as direccion, " +
																"e.telefono as telefono " +
																"FROM  cartera.cuentas_cobro cxc " +
																"inner join facturacion.convenios c on (c.codigo=cxc.convenio) " +
																"inner join facturacion.empresas e on (c.empresa=e.codigo) " +
																"inner join facturacion.terceros t on(e.tercero=t.codigo) " +
																"where cxc.numero_cuenta_cobro=? and cxc.institucion=?";
	
	
	/**
	 * Canea para cargar el resumen de la impresion=> los valores facturados agrupados
	 * por via de ingreso con su total tambien por via de ingreso segun un numero de cuenta de cobro
	 */
	private static final String cargarResumenImpresion=" SELECT c.numero_cuenta_cobro as numeroCxC, " +
													   " v.via_ingreso as codigo_viaIngreso, " +
													   " manejopaciente.getnombreviaingreso(v.via_ingreso) as viaingreso," +
													   " ( SELECT sum(f.valor_convenio) FROM facturacion.facturas f INNER JOIN manejopaciente.sub_cuentas sc on (f.sub_cuenta=sc.sub_cuenta) " +
													   //" inner join montos_cobro m  on (m.codigo=sc.monto_cobro) where f.numero_cuenta_cobro=c.numero_cuenta_cobro and m.via_ingreso=v.via_ingreso group by v.via_ingreso) as valor_via_ingreso " +
													   " INNER JOIN facturacion.detalle_monto dm ON (dm.detalle_codigo=sc.monto_cobro) where f.numero_cuenta_cobro=c.numero_cuenta_cobro AND dm.via_ingreso_codigo=v.via_ingreso group by v.via_ingreso) AS valor_via_ingreso"+
													   " FROM cartera.cuentas_cobro c  " +
													   " INNER JOIN cartera.vias_ingreso_cxc v on (c.numero_cuenta_cobro=v.numero_cuenta_cobro) " +
													   " WHERE c.numero_cuenta_cobro=?";
	
	
	private static final String convenioxCuentaCobroStr="select t.numero_identificacion as nit from terceros t INNER JOIN empresas e on(t.codigo=e.tercero) INNER JOIN convenios c ON (e.codigo=c.empresa) INNER JOIN cuentas_cobro cxc ON(c.codigo=cxc.convenio) where cxc.numero_cuenta_cobro=?";
	
	private static final String  consultaFacturasPorFechaStr="SELECT f.codigo as codigofactura FROM facturas f WHERE  f.numero_cuenta_cobro=? AND to_char(f.fecha, '"+ConstantesBD.formatoFechaBD+"')=? ";
	
	private static final String consultarFacturasPorViaIngresoStr="SELECT f.codigo as codigofactura FROM  facturas f INNER JOIN vias_ingreso_cxc vcxc ON(f.numero_cuenta_cobro=vcxc.numero_cuenta_cobro) WHERE f.numero_cuenta_cobro=? AND UPPER(getnombreviaingreso(vcxc.via_ingreso)) like UPPER('%?%')";
	
	private static final String consultarFacturaPorConsecutivoStr="SELECT f.codigo as codigofactura FROM  facturas f WHERE f.numero_cuenta_cobro=? AND f.consecutivo_factura=?";
	
	//*****************************************MOVIMIENTOS CUENTAS COBRO**************************************************************************//
	
	/**
	 * Cadena para realizar la actualizaci�n del c�digo de la CXC (null)
	 * en la tabla de facturas
	 */
	private static final String liberarFacturas = " UPDATE facturas SET numero_cuenta_cobro = ? WHERE numero_cuenta_cobro = ? AND institucion = ?";
	
	/**
	 * Cadena para eliminar el registro correspondiente a la
	 * CXC y la v�a de ingreso correspondiente
	 */
	private static final String liberarCxCdeViasIngresoCxC = "DELETE FROM vias_ingreso_cxc WHERE numero_cuenta_cobro= ? AND via_ingreso = ? AND institucion = ?";
	
	/**
	 * cadena para adicionar una cuenta de cobro, con su respectiva
	 * v�a de ingreso.
	 */
	private static final String adicionarCxCaViasIngresoCxC="INSERT INTO vias_ingreso_cxc VALUES (?,?,?)" ;
	
	/**
	 * almacena la cadena de anulaci�n de CXC
	 */
	private static final String anularCuentaCobro="UPDATE cuentas_cobro SET estado = "+ConstantesBD.codigoEstadoCarteraAnulado+" WHERE numero_cuenta_cobro = ? AND institucion = ?";
	
	/**
	 * almacena el insert de la anulaci�n de la cxc
	 */
	private static final String insertarAnulacionCXC = "INSERT INTO movimientos_cxc " +
																						"(numero_cuenta_cobro," +
																						"usuario," +
																						"fecha_movimiento," +
																						"hora_movimiento," +
																						"observacion," +
																						"tipo_movimiento," +
																						"institucion) " +
																						"VALUES (?,?,?,?,?,?,?)";
	
	/**
	 * cadena para consular si una factura tiene glosas registradas
	 */
	private static final String glosaRegistrada = "SELECT codigo,codigo_factura FROM glosas_factura WHERE codigo_factura = ? AND institucion = ?";
	
	/**
	 * cadena para consultar si una factura tiene movimientos de pagos
	 */
	private static String pagoRegistrado = "SELECT aplicacion_pagos FROM aplicacion_pagos_factura WHERE factura = ?";
	
	/**
	 * almacena la consulta de ajustes aprobados, 
	 * generados despues de radicar la CXC, 
	 * retormnando la sumatoria de ajustes debiito y credito
	 * 
	 */
	//ya se arreglo para el nuevo efoque de ajustes
	private static String sumAjustesAprobados = "select " +
												"(select case when sum(af.valor_ajuste) is NULL then 0 else sum(af.valor_ajuste) end as ajustes " +
												"from ajustes_empresa a inner join ajus_fact_empresa af on(a.codigo=af.codigo) " +
												"where af.factura=? and a.estado= "+ConstantesBD.codigoEstadoCarteraAprobado+" and a.tipo_ajuste = "+ConstantesBD.codigoAjusteDebitoCuentaCobro+") " +
												" - " +
												"(select case when sum(af.valor_ajuste) is NULL then 0 else sum(af.valor_ajuste) end as ajustes " +
												"from ajustes_empresa a inner join ajus_fact_empresa af on(a.codigo=af.codigo) " +
												"where af.factura=? and a.estado= "+ConstantesBD.codigoEstadoCarteraAprobado+" and a.tipo_ajuste = "+ConstantesBD.codigoAjusteCreditoCuentaCobro+") " +
												"as total  " +
												"from ajustes_empresa";
													
	
	//*****************************************FIN MOVIMIENTOS CUENTAS COBRO**********************************************************************//
	//"SELECT numero_cuenta_cobro, convenio as codigoconvenio, getnombreconvenio(convenio) as nombreconvenio, estado, fecha_elaboracion, hora_elaboracion, usuario_genera, fecha_radicacion, numero_radicacion, usuario_radica, valor_inicial_cuenta, saldo_cuenta, fecha_inicial, fecha_final, obs_generacion, obs_radicacion  FROM  cuentas_cobro where numero_cuenta_cobro=?";
	//
	//***********************CADENAS DE INACTIVACION FACTURAS***********************************************+
	/**
	 * Cadena para revisar si la factura tiene glosas
	 */
	private static final String revisionGlosasStr="SELECT g.codigo FROM glosas_factura g,facturas f  WHERE g.codigo_factura=f.codigo AND f.codigo=?";
	
	
	/**
	 * Cadena para realizar la consulta de los datos de una factura por su consecutivo.
	 * los datos consultados son minimos solo los exigidos en la generacion de cuentas
	 * de cobro por factura.
	 */
	//private static final String consultasFacturasCxC="SELECT f.codigo as codigo,f.fecha as fecha,getsaldofacturaajustes(f.codigo) as valor,f.valor_convenio as valor_convenio,sc.convenio as codigoconvenio,getnombreconvenio(sc.convenio) as nombreConvenio,m.via_ingreso as codigoViaIngres,getnombreviaingreso(m.via_ingreso) as nombreViaIngreso from facturas f inner  join sub_cuentas sc on (f.codigo=sc.sub_cuenta) inner join montos_cobro m on (sc.monto_cobro=m.codigo) where f.consecutivo_factura=? and f.institucion=?";
	private static final String consultasFacturasCxC="SELECT f.codigo as codigo,f.consecutivo_factura as consecutivo,to_char(f.fecha, 'YYYY-MM-DD') as fecha,getsaldofacturaajustes(f.codigo) as valor,f.valor_convenio as valor_convenio,f.convenio as codigoconvenio,getnombreconvenio(f.convenio) as nombreConvenio,f.via_ingreso as codigoViaIngres,getnombreviaingreso(f.via_ingreso) as nombreViaIngreso,f.centro_aten as codigocentroatencion,getnomcentroatencion(f.centro_aten) as nombrecentroatencion from facturas f where f.codigo=? ";
	
	/**
	 * Cadena para revisar si la factura tiene ajustes pendientes por aprobar
	 */
	private static final String revisionAjustesPendientesStr="SELECT ae.codigo "+
		"FROM ajustes_empresa ae "+ 
		"INNER JOIN ajus_fact_empresa afe ON(afe.codigo=ae.codigo) "+ 
		"INNER JOIN facturas f ON(afe.factura=f.codigo) "+ 
		"WHERE ae.estado="+ConstantesBD.codigoEstadoCarteraGenerado+" AND f.codigo=?";
	
	/**
	 * Cadena para validar que los ajustes debito y credito sean = 0.
	 * - discriminando solicitudes de farmacia y de servicios
	 * - los ajustes deben estar aprobados
	 * - la cuenta de cobro debe estar radicada
	 * PARA FARMACIA
	 */
	private static final String revisionSumatoriasAjustesArticuloStr="SELECT " +
		"getdescripcionarticulo(tabla.articulo) AS articulo," +
		"tabla.solicitud AS solicitud " +
		"FROM (SELECT " +
			"getAjusRadicadosDetFactura(f.codigo,dfs.codigo,"+ConstantesBD.codigoAjusteDebitoCuentaCobro+") - getAjusRadicadosDetFactura(f.codigo,dfs.codigo,"+ConstantesBD.codigoAjusteCreditoCuentaCobro+") as valorTotalAjuste, " +
			"dfs.articulo as articulo," +
			"dfs.solicitud as solicitud " +
			"FROM facturas f INNER JOIN det_factura_solicitud dfs ON (f.codigo=dfs.factura) " +
			"WHERE f.codigo=? AND dfs.servicio is NULL) tabla where tabla.valorTotalAjuste !=0";
	/**
	 * Cadena para validar que los ajustes debito y credito sean = 0.
	 * - discriminando solicitudes de farmacia y de servicios
	 * - los ajustes deben estar aprobados
	 * - la cuenta de cobro debe estar radicada
	 * PARA SERVICIOS
	 */
	private static final String revisionSumatoriasAjustesServiciosStr="SELECT " +
		"getnombreservicio(tabla.servicio,"+ConstantesBD.codigoTarifarioCups+") AS servicio," +
		"tabla.solicitud AS solicitud " +
		"FROM (SELECT " +
			"getAjusRadicadosDetFactura(f.codigo,dfs.codigo,"+ConstantesBD.codigoAjusteDebitoCuentaCobro+") - getAjusRadicadosDetFactura(f.codigo,dfs.codigo,"+ConstantesBD.codigoAjusteCreditoCuentaCobro+") as valorTotalAjuste, " +
			"dfs.servicio as servicio," +
			"dfs.solicitud as solicitud " +
			"FROM facturas f INNER JOIN det_factura_solicitud dfs ON (f.codigo=dfs.factura) " +
			"WHERE f.codigo=? AND dfs.articulo is NULL) tabla where tabla.valorTotalAjuste !=0";

	/**
	 * Cadena para validar si la factura tiene movimientos de pago
	 */
	private static final String revisionPagosStr="SELECT apf.aplicacion_pagos "+
		"FROM aplicacion_pagos_factura apf "+
		"INNER JOIN facturas f ON(f.codigo=apf.factura) "+
		"WHERE f.codigo=?";
	
	/**
	 * Cadena para validar si la factura est� relacionada a una 
	 * cuenta de cobro
	 */
	private static final String revisionCuentaCobroStr="SELECT  numero_cuenta_cobro "+
		"FROM facturas WHERE numero_cuenta_cobro IS NOT NULL AND codigo=?";
	
	private static final String revisionCuentaCobroRadicadaStr="SELECT cc.numero_cuenta_cobro "+
		"FROM cuentas_cobro cc "+
		"INNER JOIN facturas f ON(f.numero_cuenta_cobro=cc.numero_cuenta_cobro) "+
		"WHERE cc.estado="+ConstantesBD.codigoEstadoCarteraRadicada+" AND f.codigo=?";
	//************************FIN SENTENCIAS DE VALIDACI�N INACTIVACI�N FACTURAS*********************************
	//*********************SENTENCIAS DE VALIDACI�N RADICACI�N DE CUENTAS****************************************************************
	/**
	 * Cadena para revisar si la cuenta de cobro tiene estado 'Generado'
	 */
	private static final String revisionEstadoCuentaCobroStr="SELECT " +
			"ec.descripcion AS descripcion FROM cuentas_cobro cc " +
			"INNER JOIN estados_cartera ec ON(ec.codigo=cc.estado) " +
			"WHERE cc.estado<>"+ConstantesBD.codigoEstadoCarteraAprobado+" AND cc.numero_cuenta_cobro=? AND institucion=?";
	
	/**
	 * Cadena que verifica si una cuenta de cobro existe
	 */
	private static final String revisionExistenciaCuentaCobroStr = "SELECT " +
			"count(1) As cuenta " +
			"FROM cuentas_cobro WHERE numero_cuenta_cobro=? and institucion = ?";
	/**
	 * Cadena que retorna el n�mero de facturas que posee una cuenta de cobro 
	 */
	private static final String revisionFacturasAsociadasACuentaStr="SELECT count(1) AS numero_facturas FROM facturas WHERE numero_cuenta_cobro=? AND institucion=?";
	
	private static final String revisionAjustesPendientesCuentaCobroStr="SELECT " +
			"f.consecutivo_factura AS factura FROM facturas f " +
			"INNER JOIN ajus_fact_empresa afe ON(afe.factura=f.codigo) "+ 
			"INNER JOIN ajustes_empresa ae ON(ae.codigo=afe.codigo) " +
			"WHERE ae.estado="+ConstantesBD.codigoEstadoCarteraGenerado+" AND f.numero_cuenta_cobro=? " +
			"AND f.institucion=? GROUP BY f.consecutivo_factura";
	//**********************FIN DE SENTENCIAS DE VALIDACI�N RADIACI�N DE CUENTAS***************************************
	/**
	 * Cadena para cargar los datos de la factura antes de ser inactivada
	 */
	private static final String cargarDatosFacturaStr="SELECT "+ 
		"getnombreconvenio(sc.convenio) AS convenio, "+
		"to_char(f.fecha, '"+ConstantesBD.formatoFechaBD+"') AS fecha_elaboracion, "+
		"getsaldofacturaajustes(f.codigo) AS valor, "+
		"f.numero_cuenta_cobro AS cuenta_cobro, "+
		"cc.fecha_radicacion AS fecha_radicacion, "+
		"f.codigo AS codigo," +
		"f.consecutivo_factura as consecutivo "+
		"FROM facturas f "+ 
		"INNER JOIN sub_cuentas sc ON(sc.sub_cuenta=f.sub_cuenta) "+ 
		"INNER JOIN cuentas_cobro cc ON(f.numero_cuenta_cobro=cc.numero_cuenta_cobro) "+ 
		"WHERE f.codigo=?";
	/**
	 * Cadena para insertar una inactivaci�n de factura
	 */
	private static final String insertarInactivacionFacturaStr="INSERT "+
		"INTO inactivacion_factura "+ 
		"(numero_cuenta_cobro,codigo_factura,fecha_inactivacion,hora_inactivacion,usuario_inactiva,obs_inactivacion,institucion) "+ 
		"VALUES(?,?,CURRENT_DATE,"+ValoresPorDefecto.getSentenciaHoraActualBDTipoTime()+",?,?,?)";
	/**
	 * Cadena para actualizar el saldo y valor incial de la cuenta en caso de que una factura
	 * haya sido inactivada
	 */
	private static final String actualizarInactivacionEnCuentaCobroStr="UPDATE "+
		"cuentas_cobro SET "+
		"valor_inicial_cuenta=(SELECT valor_inicial_cuenta-? FROM cuentas_cobro where numero_cuenta_cobro=?)," +
		"saldo_cuenta=(SELECT saldo_cuenta-? FROM cuentas_cobro where numero_cuenta_cobro=?)" +
		" WHERE numero_cuenta_cobro=? AND institucion=?";
	
	/**
	 * Cadena que ingresa una radicaci�n a una cuenta de cobro
	 */
	private static final String ingresarRadicacionStr="UPDATE cuentas_cobro " +
	"SET fecha_radicacion=?,numero_radicacion=?,usuario_radica=?,fecha_radica=?,hora_radica=?,obs_radicacion=?," +
	"estado="+ConstantesBD.codigoEstadoCarteraRadicada+" "+
	"WHERE numero_cuenta_cobro=? AND institucion=?";
	
	/**
	 *Cadena que asigna el estado 'Radicada' a la cuenta de cobro 
	 */
	private static final String asignarEstadoRadicacionStr="UPDATE cuentas_cobro " +
			"SET estado="+ConstantesBD.codigoEstadoCarteraRadicada+" where numero_cuenta_cobro=?";
	
	
	
	/**
	 * Cadena que almacena el tipo de  movimienot de una cuenta de cobro, las observaciones,
	 * la fecha y hora del movimiento y el usuario. Solo para movimientos de tipo Anulacion
	 */
	private static final String motivoAnulacionCxCStr=" SELECT mcxc.fecha_movimiento as Fecha, "+
													  " mcxc.hora_movimiento as Hora, "+
													  " mcxc.usuario as Usuario, "+
													  " mcxc.observacion as Motivo, "+
													  " tmcxc.descripcion as TipoMovimiento "+
													  " FROM movimientos_cxc mcxc "+
													  " INNER JOIN tipos_movimientos_cxc tmcxc ON(mcxc.tipo_movimiento=tmcxc.codigo)  "+
													  " WHERE mcxc.numero_cuenta_cobro=?";
	
//	***************** Registro Saldo Inicial Cartera *********************//
	/**
	 * Cadena para consultar si una cuenta de cobro existe en BD. 
	 */
	private static final String existeCuentaCobroStr="SELECT COUNT (*) AS codigos FROM cuentas_cobro WHERE numero_cuenta_cobro = ? AND institucion = ?";
	
	/**
	 * Cadena para consultar si una via de ingreso tiene entrada en la tabla vias_ingreso_cxc
	 */
	private static final String existeViaIngresoCxCStr = "SELECT COUNT(*) AS vias FROM vias_ingreso_cxc WHERE via_ingreso = ? AND institucion = ? AND numero_cuenta_cobro = ?";
	
	//***************** Registro Saldo Inicial Cartera *********************//
	//******************Cierre Saldo Inicial cartera **********************//
	
	private static final String cuentaCobroPoseeRadicacionStr = "SELECT COUNT(1) as numFilas FROM cuentas_cobro WHERE numero_cuenta_cobro = ? AND institucion = ? AND estado = "+ConstantesBD.codigoEstadoCarteraRadicada;
	
	//******************Cierre Saldo Inicial cartera **********************// 
	
	
	//******************Consulta del estado de la glosa correspondiente a la Factura********************//
	
	private static final String estadoGlosaFacturaStr="SELECT " +
															"g.estado,	" +
															"f.consecutivo_factura AS consecutivo " +
														"FROM " +
															"registro_glosas g " +
														"INNER JOIN " +
															"auditorias_glosas ag ON (ag.glosa=g.codigo) " +
														"INNER JOIN " +
															"facturas f ON (f.codigo=ag.codigo_factura) " +
														"WHERE ";
	
	//******************Fin Consulta del Estado de la Glosa*********************************************//
	
	
	private static String aprobarCuentasCobro="UPDATE cartera.cuentas_cobro SET " +
												"estado = "+ConstantesBD.codigoEstadoCarteraAprobado+", " +
												"fecha_aprobacion = CURRENT_DATE, " +
												"hora_aprobacion = ?, " +
												"usuario_aprobacion = ? " +
												"WHERE numero_cuenta_cobro = ? AND institucion=?"; 
	
	private static String consultaFacturasCxc="SELECT codigo, consecutivo_factura FROM facturacion.facturas WHERE numero_cuenta_cobro=? AND institucion=?";
	
	private static String guardarDetMovimientosCxc="INSERT INTO cartera.det_movimientos_cxc " +
													"(numero_cuenta_cobro,institucion,factura) " +
													"VALUES (?,?,?) ";
	
	private static String consultarFacturaEnProcesoAudi= "SELECT count(ag.codigo) AS cant " +
															"FROM auditorias_glosas ag " +
															"INNER JOIN registro_glosas rg ON(ag.glosa= rg.codigo) " +
															"WHERE ag.codigo_factura=? " +
															"AND rg.estado <> 'RESP' " +
															"AND rg.estado <> 'ANUL'";
	
	

	public static int consultarFacturaEnProcesoAudi(int factura) 
	{
		Connection con;		
		con= UtilidadBD.abrirConexion();
		int cont=0;
		PreparedStatementDecorator ps=null;
		ResultSetDecorator rs=null;
		try
		{		
			ps= new PreparedStatementDecorator(con.prepareStatement(consultarFacturaEnProcesoAudi,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			ps.setInt(1, factura);	
			
			logger.info("\n\nconsulta::::::: "+consultarFacturaEnProcesoAudi+" factura:::: "+factura);
			
			rs= new ResultSetDecorator(ps.executeQuery());
			
						
			if(rs.next()){
				cont = rs.getInt("cant");
			}
		}
		catch (SQLException e)
		{
			logger.info("\n\nERROR CONSULTANDO FACTURA EN PROCESO AUDI.------>>>>>>"+e);
			e.printStackTrace();
		}
		finally
		{
			try {
				if(ps!=null)
					ps.close();
				if(rs!=null)
					rs.close();
				UtilidadBD.cerrarConexion(con);
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return cont;	
	}
	
	public static boolean guardarDetMovimientosCxc(Connection con, int factura, double numCuentaCobro,int institucion)
	{
		
		PreparedStatementDecorator ps=null;
				
		try
		{	
			ps =  new PreparedStatementDecorator(con.prepareStatement(guardarDetMovimientosCxc, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet ));			
			int valorseq=UtilidadBD.obtenerSiguienteValorSecuencia(con, "facturacion.seq_ex_coberturas_entidad_sub");			
			ps.setDouble(1, numCuentaCobro);			
			ps.setInt(2, institucion);			
			ps.setInt(3, factura);			
						
			if(ps.executeUpdate()>0)
			{
				ps.close();
				return true;
			}
		}
		catch (SQLException e)
		{
			logger.info("\n\nERROR. INSERTANDO DETALLE MOV CXC------>>>>>>"+e);
			e.printStackTrace();
		}
		finally{
			try {
				if(ps!=null)
					ps.close();
			} catch (SQLException e) {
				logger.error("\n Error al cerrar objetos de persistencia "+e);
				e.printStackTrace();
			}
		}
		return false;
	}
	
	public static HashMap consultaFacturasCxC(Connection con, double numCuentaCobro, int institucion)
	{
		HashMap resultados= new HashMap();
		PreparedStatementDecorator ps=null;
		
		try
		{			
			ps =  new PreparedStatementDecorator(con.prepareStatement(consultaFacturasCxc, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet ));
			ps.setDouble(1, numCuentaCobro);
			ps.setInt(2, institucion);
					
			resultados=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()), true, true);
						
			logger.info("\n\nresultado::: "+resultados);
	
		}
		catch (SQLException e)
		{
			logger.info("\n\nERROR. CONSULTANDO FACTURAS CUENTA DE COBRO------>>>>>>"+e);
			e.printStackTrace();
		}
		finally{
			
			try {
				if(ps!=null)
					ps.close();
			} catch (SQLException e) {
				logger.error("\n Error al cerrar objetos de persistencia "+e);
				e.printStackTrace();
			}
		}
		return resultados;
	}	
	
	
	public static boolean  aprobarCuentasCobro(Connection con,int cuenta, String usuario, int institucion)
	{
						
		logger.info("\n\nACTUALIZANDO REGISTRO CUENTA COBRO----->>>>>>>>>>>"+aprobarCuentasCobro);
		PreparedStatementDecorator ps=null;
		try
		{
			ps =  new PreparedStatementDecorator(con.prepareStatement(aprobarCuentasCobro, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet ));
			
			ps.setString(1, UtilidadFecha.getHoraActual());
			ps.setString(2, usuario);
			ps.setInt(3, cuenta);
			ps.setInt(4, institucion);
			
			if(ps.executeUpdate()>0)
			{
				ps.close();
				return true;
			}
		}
		catch (SQLException e)
		{
			logger.info("\n\nERROR. ACTUALIZANDO REGISTRO CUENTA COBRO------>>>>>>"+e);
			e.printStackTrace();
		}
		finally
		{
			try {
				if(ps!=null)
					ps.close();
			} catch (SQLException e) {
				logger.error("\n Error al cerrar objetos de persistencia "+e);
				e.printStackTrace();
			}
		}
		return false;
		
	}
	
	
	
	/**
	 * Metodo para consultar por el codigo de la factura, si
	 * tiene  glosas �/y castigos
	 * @param con Connection, conexi�n con la fuente de datos
	 * @param codFactura int, codigo de la factura
	 * @param vGlosa, boolean true si es consulta de glosas
	 * @param vCastigo, boolean true si es consulta de castigo
	 * @param vPago, boolean true si es consulta de pagos
	 * @param institucion int, c�digo de la instituci�n
	 * @param sumAjustes, boolean true si es consulta de sumatoria de ajustes
	 * @see com.princetonsa.dao.CuentasCobroDao#validacionesDevolucionCXC(java.sql.Connection,int,boolean,boolean,int)
	 * @return ResultSet
	 */
	public static ResultSetDecorator validacionesDevolucionCXC (Connection con, 
																        int codFactura,
																        boolean vGlosa,
																        boolean vPago,
																        boolean sumAjustes,
																        String sumAjustesAprobados,
																        int institucion)
	{
	    try
		{
	        if (con == null || con.isClosed()) 
			{
				throw new SQLException ("Error SQL en validacionesDevolucionCXC SqlBaseCuentasCobroDao : Conexi�n cerrada");
			}
	        if(vGlosa)
	        {
	            PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(glosaRegistrada,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));		
	            ps.setInt(1,codFactura);
	            ps.setInt(2,institucion);	            
	            return new ResultSetDecorator(ps.executeQuery());
	        }
	        if(vPago)
	        {
	            PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(pagoRegistrado,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));		
	            ps.setInt(1,codFactura);
	            return new ResultSetDecorator(ps.executeQuery());
	        }
	        if(sumAjustes)
	        {
	            PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(sumAjustesAprobados,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));		
	            ps.setInt(1,codFactura);
	            ps.setInt(2,codFactura);
	            return new ResultSetDecorator(ps.executeQuery());
	        }
		}
	    catch(SQLException e)
		{
			logger.warn(e+"Error en validacionesDevolucionCXC de datos: SqlBaseCuentasCobroDao "+e.toString());
			return null;
		}
        return null;	   
	}
	
	
	/**
	 * Metodo implementado para realizar la modificaci�n
	 * de la cuenta de cobro.
	 * @param con Connection, conexion con la fuente de datos
	 * @param numCxC, double n�mero de la cxc
	 * @param fechaFinal String fecha final de la cxc
	 * @param fechaInicial String fecha inicial de la cxc
	 * @param observaciones String observaciones de la generaci�n de la cxc
	 * @param fechaElaboracion String
	 * @see com.princetonsa.dao.CuentasCobroDao#modificarCuentaCobro(java.sql.Connection,double,String,String,String,String,double,String)
	 * @return boolean true si es efectivo, false de lo contrario. 
	 */
	public static boolean modificarCuentaCobro(Connection con, 
														        double numCxC,
														        String fechaFinal,
														        String fechaInicial, 
														        String observaciones,
														        String usuarioGen,
														        double valorInicial, 
														        String fechaElaboracion) 
	{
	    int resp = 0;
	    boolean esModificado=false;
	    String cadenaModificar="UPDATE cuentas_cobro SET " +
												    		"usuario_genera= ?, " +
												    		"fecha_inicial = ?, " +
												    		"fecha_final = ?, " +
												    		"obs_generacion = ?, " +
												    		"valor_inicial_cuenta = ?, "+
												    		"saldo_cuenta = ?";
	    if(!fechaElaboracion.equals(""))
	    {
	        cadenaModificar=cadenaModificar.concat(",fecha_elaboracion = '"+fechaElaboracion+"'");	        
	        esModificado=true;  
	    }
	    
	    String where = " WHERE numero_cuenta_cobro= ? ";
	   
	    PreparedStatementDecorator ps=null;
        try 
        {
            ps =  new PreparedStatementDecorator(con.prepareStatement(cadenaModificar+where,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
        } catch (SQLException e1) 
        {
          e1.printStackTrace();
        }
        try
		{
	        if (con == null || con.isClosed()) 
			{
				throw new SQLException ("Error SQL en modificarCuentaCobro SqlBaseCuentasCobroDao : Conexi�n cerrada");
			}
	        
	        if(!usuarioGen.equals(""))
	        {
	           esModificado=true;
	        }
	        if(!fechaInicial.equals(""))
	        {
	            esModificado=true;
	        }
	        if(!fechaFinal.equals(""))
	        {
	           esModificado=true;
	        }
	        if(!observaciones.equals(""))
	        {
	           esModificado=true;
	        }
	        if(valorInicial != 0.0)
	        {
	            esModificado=true;
	        }
	        if(esModificado)
	        {     
	          ps.setString(1,usuarioGen);
	          ps.setDate(2,Date.valueOf(fechaInicial));  
	          ps.setDate(3,Date.valueOf(fechaFinal));
	          ps.setString(4,observaciones);	          
	          ps.setDouble(5,valorInicial);
	          ps.setDouble(6,valorInicial);
	          ps.setDouble(7,numCxC);
			  resp=ps.executeUpdate();			  
	        }
	        if(resp>0)
				return true;
			else
				return false;
		}
	    catch(SQLException e)
		{
			logger.warn(e+"Error en la modificarCuentaCobro de datos: SqlBaseCuentasCobroDao "+e.toString());
			return false;			
		}	     
	    
	}
	
	/**
	 * Metodo implementado para anular una cuenta de cobro.
	 * @param con Connection con la fuente de datos
	 * @param numCXC double numero de la cuenta de cobro
	 * @param institucion int, c�digo de la instituci�n
	 * @see com.princetonsa.dao.CuentasCobroDao#anularCXC(java.sql.Connection,double)
	 * @return boolean true si es efectivo, de lo contrario false.
	 */
	public static boolean anularCXC (Connection con, double numCXC,int institucion)
	{
	    int resp = 0;
	    
	    try
		{
	        if (con == null || con.isClosed()) 
			{
				throw new SQLException ("Error SQL en anularCXC SqlBaseCuentasCobroDao : Conexi�n cerrada");
			}
	        
	        PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(anularCuentaCobro,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));		
	        ps.setDouble(1,numCXC);
	        ps.setInt(2,institucion);
	        resp=ps.executeUpdate();
			
			if(resp>0)
				return true;
			else
				return false;
		}
	    catch(SQLException e)
		{
			logger.warn(e+"Error en anularCXC de datos: SqlBaseCuentasCobroDao "+e.toString());
			return false;			
		}	    
	}
	
	/**
	 * implementado para insertar una CXC que ha sido anulada.
	 * @param con connection con la fuente de datos
	 * @param numCXC double numero de la cuenta de cobro
	 * @param usuario Strig login del usuario 
	 * @param fecha, String fecha de la anulaci�n
	 * @param hora String hora de la anulaci�n
	 * @param observacion String observaci�n
	 * @param tipoMovimiento int codigo del tipo de anualci�n
	 * @param institucion int, c�digo de la instituci�n
	 * @see com.princetonsa.dao.CuentasCobroDao#insertarAnulacionCXC(java.sql.Connection,double,String,String,String,String,int,int)
	 * @return boolean true si es efectivo.
	 */
	public static boolean insertarAnulacionCXC (Connection con, 
														        double numCXC,
														        String usuario,
														        String fecha, 
														        String hora,
														        String observacion, 
														        int tipoMovimiento,
														        int institucion)
	{
	    int resp = 0;
	    
	    try
		{
	        if (con == null || con.isClosed()) 
			{
				throw new SQLException ("Error SQL en anularCXC SqlBaseCuentasCobroDao : Conexi�n cerrada");
			}
	        
	        PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(insertarAnulacionCXC,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));		
	        ps.setDouble(1,numCXC);
	        ps.setString(2,usuario);        
	        ps.setDate(3,Date.valueOf(fecha));
	        ps.setString(4,hora);
	        ps.setString(5,observacion);
	        ps.setInt(6,tipoMovimiento);
	        ps.setInt(7,institucion);
	        resp=ps.executeUpdate();
			
			if(resp>0)
				return true;
			else
				return false;
		}
	    catch(SQLException e)
		{
			logger.warn(e+"Error en insertarAnulacionCXC de datos: SqlBaseCuentasCobroDao "+e.toString());
			return false;			
		}	    
	}
	/**
	 * Metodo implementado para liberar todas las facturas
	 * que pertenecen a una cuenta de cobro especifica.
	 * se liberan todas las facturas colocando el
	 * campo en latabla que almacena el numero de la CXC
	 * en null, pero solo las facturas que pertenecen a
	 * una CXC especifica.
	 * 
	 * @param con Connection con la fuente de datos
	 * @param numeroCxc, double numero de la cuenta de cobro
	 * @param codInstitucion int, c�digo de la instituci�n
	 * @see com.princetonsa.dao.CuentasCobroDao#liberarFacturasCXC(java.sql.Connection,double,int)
	 * @return boolean, true existoso, false de lo contrario
	 */
	public static boolean liberarFacturasCXC(Connection con, double numeroCxc,int codInstitucion)
	{	    
	    int resp = 0;
	    
	    try
		{
	        if (con == null || con.isClosed()) 
			{
				throw new SQLException ("Error SQL en modificar SqlBaseCuentasCobroDao : Conexi�n cerrada");
			}
	        
	        PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(liberarFacturas,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));		    
	        ps.setObject(1,null);
	        ps.setDouble(2,numeroCxc);
	        ps.setInt(3,codInstitucion);
	        resp=ps.executeUpdate();
			
			if(resp>0)
				return true;
			else
				return false;
		}
	    catch(SQLException e)
		{
			logger.warn(e+"Error en la modificaci�n de datos: SqlBaseCuentasCobroDao "+e.toString());
			return false;			
		}	    
	}
	
	/**
	 * Metodo implementado para eliminar un registro de la
	 * tabla <code>vias_ingreso_cxc</code>, segun el 
	 * n�mero de la CXC y la V�a de Ingreso.
	 * @param con Connection, conexion con la fuente de datos
	 * @param numCxC double, numero de la cuenta de cobro
	 * @param viaIngreso int, codigo de la via de ingreso.
	 * @param institucion int, c�digo de la instituci�n
	 * @see com.princetonsa.dao.CuentasCobroDao#borrarRegistroCXCdeViasIngresoCXC(java.sql.Connection,double,int)
	 * @return boolean, true existoso, false de lo contrario
	 */
	public static boolean borrarRegistroCXCdeViasIngresoCXC(Connection con, double numCxC, int viaIngreso,int institucion)
	{
	    int resp = 0;
	    
	    try
		{
	        if (con == null || con.isClosed()) 
			{
				throw new SQLException ("Error SQL en eliminar SqlBaseCuentasCobroDao : Conexi�n cerrada");
			}
	        PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(liberarCxCdeViasIngresoCxC,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	        ps.setDouble(1,numCxC);
	        ps.setInt(2,viaIngreso);
	        ps.setInt(3,institucion);
	        resp=ps.executeUpdate();
	        if(resp>0)
				return true;
			else
				return false;	        
		}
	    
	    catch(SQLException e)
		{
			logger.warn(e+"Error en la eliminaci�n de registro: SqlBaseCuentasCobroDao "+e.toString());
			return false;			
		}	    
	    
	}
	
	/**
	 * Metodo implementado para adicionar un registro en la
	 * tabla vias_ingreso_cxc, segun su numero de cuenta de cobro
	 * y la via de ingreso.
	 * @param con Connection, conexion con la fuente de datos
	 * @param numCxC, double numero de la cuenta de cobro
	 * @param viaIngreso, int c�digo de la v�a de ingreso.
	 * @param institucion int, c�digo de la instituci�n
	 * @see com.princetonsa.dao.CuentasCobroDao#adicionarRegistroCXCaViasIngresoCXC(java.sql.Connection,double,int)
	 * @return boolean, true existoso, false de lo contrario
	 */
	public static boolean adicionarRegistroCXCaViasIngresoCXC(Connection con, double numCxC, int viaIngreso,int institucion)
	{
	    int resp = 0;
	    
	    try
		{
	        if (con == null || con.isClosed()) 
			{
				throw new SQLException ("Error SQL en eliminar SqlBaseCuentasCobroDao : Conexi�n cerrada");
			}
	        PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(adicionarCxCaViasIngresoCxC,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	        ps.setDouble(1,numCxC);
	        ps.setInt(2,viaIngreso);
	        ps.setInt(3,institucion);
	        resp=ps.executeUpdate();
	        if(resp>0)
				return true;
			else
				return false;	
		}
	    catch(SQLException e)
		{
			logger.warn(e+"Error en la adici�n de registro: SqlBaseCuentasCobroDao "+e.toString());
			return false;			
		}	 
	}
	
	/**
	 * M�todo usado para validar si la factura puede ser inactivada.
	 * Retorna una cadena de etiquetas de mensajes de error separados por '-'
	 * @param con
	 * @param consecutivoFactura
	 * @return
	 */
	public static String validacionInactivacionFacturas(Connection con,int codigoFactura){
		//Cadena usada para almacenar los errores de la validaci�n
		String cadena_validacion="";
		PreparedStatementDecorator pst;
		ResultSetDecorator rs;
		try{
			//Validaci�n para saber si tiene glosas registradas
			pst= new PreparedStatementDecorator(con.prepareStatement(revisionGlosasStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,codigoFactura);
			rs=new ResultSetDecorator(pst.executeQuery());
			if(rs.next()){
				cadena_validacion+="error.inactivacion.glosas";
			}
			
			
			//Validaci�n para saber si tiene ajustes pendientes por aprobar
			pst= new PreparedStatementDecorator(con.prepareStatement(revisionAjustesPendientesStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,codigoFactura);
			rs=new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
			{
				if(!cadena_validacion.equals(""))
					cadena_validacion+=ConstantesBD.separadorTags;
				cadena_validacion+="error.inactivacion.ajuste_pendiente";
			}
			
			
			//Validaci�n de sumatoria de ajustes d�bito y cr�dito para servicios
			pst= new PreparedStatementDecorator(con.prepareStatement(revisionSumatoriasAjustesServiciosStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,codigoFactura);
			rs=new ResultSetDecorator(pst.executeQuery());
			while(rs.next()){
				if(!cadena_validacion.equals(""))
					cadena_validacion+=ConstantesBD.separadorTags;
				cadena_validacion+="error.inactivacion.sumatoria_ajustes_servicios@"+
					"solicitud: "+rs.getString("solicitud")+" servicio: "+rs.getString("servicio");
				
				//se recorren otros servicios
				while(rs.next()){
					cadena_validacion+=",solicitud: "+rs.getString("solicitud")+" servicio: "+rs.getString("servicio");
				}
			}
			
			//Validaci�n de sumatoria de ajustes d�bito y cr�dito para farmacia
			pst= new PreparedStatementDecorator(con.prepareStatement(revisionSumatoriasAjustesArticuloStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,codigoFactura);
			rs=new ResultSetDecorator(pst.executeQuery());
			while(rs.next()){
				if(!cadena_validacion.equals(""))
					cadena_validacion+=ConstantesBD.separadorTags;
				cadena_validacion+="error.inactivacion.sumatoria_ajustes_farmacia@"+
					"solicitud: "+rs.getString("solicitud")+" art�culo: "+rs.getString("articulo");
				
				//se recorren otros art�culos
				while(rs.next()){
					cadena_validacion+=",solicitud: "+rs.getString("solicitud")+" art�culo: "+rs.getString("articulo");
				}
			}
			
			//Validaci�n para saber si tiene movimiento de pagos
			pst= new PreparedStatementDecorator(con.prepareStatement(revisionPagosStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,codigoFactura);
			rs=new ResultSetDecorator(pst.executeQuery());
			if(rs.next()){
				if(!cadena_validacion.equals(""))
					cadena_validacion+=ConstantesBD.separadorTags;
				cadena_validacion+="error.inactivacion.pagos";
			}
			
			//Validar si la factura est� relacionada con una cuenta de cobro
			pst= new PreparedStatementDecorator(con.prepareStatement(revisionCuentaCobroStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,codigoFactura);
			rs=new ResultSetDecorator(pst.executeQuery());
			if(!rs.next()){
				if(!cadena_validacion.equals(""))
					cadena_validacion+=ConstantesBD.separadorTags;
				cadena_validacion+="error.inactivacion.cuentacobro";
			}
			
			//Validar si la cuenta de cobro de la factura es radicada
			pst= new PreparedStatementDecorator(con.prepareStatement(revisionCuentaCobroRadicadaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,codigoFactura);
			rs=new ResultSetDecorator(pst.executeQuery());
			if(!rs.next()){
				if(!cadena_validacion.equals(""))
					cadena_validacion+=ConstantesBD.separadorTags;
				cadena_validacion+="error.inactivacion.cuentacobro_radicada";
			}

			return cadena_validacion;
			
		}
		catch(SQLException e){
			logger.error("Error validando la Inactivaci�n de Facturas en SqlBaseCuentasCobroDao: "+e);
			return null;
		}
	}
	
	/**
	 * M�todo para cargar los datos de la factura antes de ser inactivada
	 * @param con
	 * @param consecutivoFactura
	 * @return
	 */
	public static Collection cargarDatosFactura(Connection con,int codigoFactura){
		try{
			//logger.info(cargarDatosFacturaStr+" "+codigoFactura);
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(cargarDatosFacturaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,codigoFactura);
			ResultSetDecorator rs=new ResultSetDecorator(pst.executeQuery());
			Collection mapa=UtilidadBD.resultSet2Collection(rs);
			rs.close();
			pst.close();
			return mapa;
		}
		catch(SQLException e){
			logger.error("Error cargando los datos de la factura en SqlBaseCuentasCobroDao: "+e);
			return null;
		}
		
	}
	
	/**
	 * M�todo para insertar una inactivaci�n de facturas
	 * @param con
	 * @param numeroCuentaCobro
	 * @param codigoFactura
	 * @param loginUsuario
	 * @param observaciones
	 * @param estado transaccional
	 * @return
	 */
	public static int insertarInactivacionFactura(Connection con,double numeroCuentaCobro,int codigoFactura,String loginUsuario, String observaciones,int institucion,String estado){
		DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		int resp=0;
		try{
			//inicio de transacci�n
			if(estado.equals(ConstantesBD.inicioTransaccion))
			{
				myFactory.beginTransaction(con);
			}
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(insertarInactivacionFacturaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setDouble(1,numeroCuentaCobro);
			pst.setInt(2,codigoFactura);
			pst.setString(3,loginUsuario);
			pst.setString(4,observaciones);
			pst.setInt(5,institucion);
			
			resp=pst.executeUpdate();
			//error en transaccion
			if(resp<=0){
				myFactory.abortTransaction(con);
			}
			//fin de transacci�n
			if(estado.equals(ConstantesBD.finTransaccion))
			{
				myFactory.endTransaction(con);
			}
			return resp;
		}
		catch(SQLException e){
			try {
				myFactory.abortTransaction(con);
			} catch (SQLException e1) {
				e1.printStackTrace();}
			logger.error("Error insertando inactivaci�n de Factura en SqlBaseCuentasCobroDao: "+e);
			return -1;
		}
	}
	
	/**
	 * M�todo usado para registrar la inactivaci�n de la factura en la cuenta de cobro,
	 * restando el valor de la factura en el valor inicial y el saldo de la cuenta
	 * @param con
	 * @param numCuentaCobro
	 * @param valorFactura
	 * @param institucion
	 * @param estado
	 * @return
	 */
	public static int actualizarInactivacionEnCuentaCobro(Connection con,double numCuentaCobro,double valorFactura,int institucion,String estado){
		DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		int resp=0;
		try{
			//inicio de transacci�n
			if(estado.equals(ConstantesBD.inicioTransaccion))
			{
				myFactory.beginTransaction(con);
			}
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(actualizarInactivacionEnCuentaCobroStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setDouble(1,valorFactura);
			pst.setDouble(2,numCuentaCobro);
			pst.setDouble(3,valorFactura);
			pst.setDouble(4,numCuentaCobro);
			pst.setDouble(5,numCuentaCobro);
			pst.setInt(6,institucion);
			
			resp=pst.executeUpdate();
			//error en transaccion
			if(resp<=0){
				myFactory.abortTransaction(con);
			}
			//fin de transacci�n
			if(estado.equals(ConstantesBD.finTransaccion))
			{
				myFactory.endTransaction(con);
			}
			return resp;
		}
		catch(SQLException e){
			try {
				myFactory.abortTransaction(con);
			} catch (SQLException e1) {
				e1.printStackTrace();}
			logger.error("Error actualizando la inactivaci�n de factura en la cuenta de cobro SqlBaseCuentasCobroDao=> "+e);
			return -1;
		}
	}
	
	/**
	 * M�todo que hace las validaciones antes de radicar una cuenta de cobro
	 * @param con
	 * @param numCuentaCobro
	 * @return
	 */
	public static String validacionRadicacion(Connection con,double numCuentaCobro,int institucion){
		//Cadena usada para almacenar los errores de la validaci�n
		String cadena_validacion="";
		PreparedStatementDecorator pst;
		ResultSetDecorator rs;
		try{
			//Validaci�n para saber si la cuenta de cobro tiene un estado diferente 
			//a 'generado'
			pst= new PreparedStatementDecorator(con.prepareStatement(revisionEstadoCuentaCobroStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setDouble(1,numCuentaCobro);
			pst.setInt(2,institucion);
			rs=new ResultSetDecorator(pst.executeQuery());
			if(rs.next()){
				cadena_validacion+="error.radicacion.estado_cuenta@"+rs.getString("descripcion");
			}
			
			boolean existeCuenta = false;
			//Se verifica si la cuenta de cobro existe
			pst =  new PreparedStatementDecorator(con.prepareStatement(revisionExistenciaCuentaCobroStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setDouble(1,numCuentaCobro);
			pst.setInt(2,institucion);
			rs=new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
			{
				if(rs.getInt("cuenta")>0)
					existeCuenta = true;
				else
				{
					String cxc=UtilidadTexto.formatearExponenciales(numCuentaCobro);
					if(cxc.indexOf(".")>0)
					{
						cxc=cxc.substring(0,cxc.indexOf("."));
					}
					cadena_validacion+="errors.noExiste@"+"La Cuenta de Cobro "+cxc;
				}
			}
			
			if(existeCuenta)
			{
				//Validaci�n para saber si tiene castigo de cartera registrado
				pst= new PreparedStatementDecorator(con.prepareStatement(revisionFacturasAsociadasACuentaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				pst.setDouble(1,numCuentaCobro);
				pst.setInt(2,institucion);
				rs=new ResultSetDecorator(pst.executeQuery());
				if(rs.next()){
					//Se revisa si la cuenta de cobro no tiene facturas asociadas
					if(rs.getInt("numero_facturas")==0){
						if(!cadena_validacion.equals(""))
							cadena_validacion+=ConstantesBD.separadorTags;
						cadena_validacion+="error.radicacion.facturas_asociadas";
					}
				}
			}
			
			//Validaci�n para saber si hay facturas en la cuenta de cobro
			//con ajustes pendientes por aprobar
			pst= new PreparedStatementDecorator(con.prepareStatement(revisionAjustesPendientesCuentaCobroStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setDouble(1,numCuentaCobro);
			pst.setInt(2,institucion);
			rs=new ResultSetDecorator(pst.executeQuery());
			if(rs.next()){
				if(!cadena_validacion.equals(""))
					cadena_validacion+=ConstantesBD.separadorTags;
				cadena_validacion+="error.radicacion.ajustes_pendientes@"+rs.getString("factura");
				
				//se recorren otras facturas
				while(rs.next()){
					cadena_validacion+=","+rs.getString("factura");
				}
				
			}
			return cadena_validacion;
		}
		catch(SQLException e){
			logger.error("Error validando la Radicaci�n de la Cuenta de Cobro SqlBaseCuentasCobroDao=>"+e);
			return null;
		}
			
	}

	
	/**
	 * Metodo que carga una cuenta de Cobro dado su codigo.
	 * No carga sus vias de ingreso.
	 * Para cargar las vias de ingreso con la cuenta de cobro debo encapsularlo en
	 * un objeto, debido a que es un registro de nx1 filas y en algunos metodos se
	 * necesita el resultset. 
	 * @param con
	 * @param cuentaCobro
	 * @param institucion
	 * @return ResultSet.
	 */
	public static ResultSetDecorator cargarCuentaCobro(Connection con, double cuentaCobro, int institucion) 
	{
		try{
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(cargarCuentaCobro,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setDouble(1,cuentaCobro);
			pst.setInt(2,institucion);
			logger.info("\n\nquery::::: "+cargarCuentaCobro+"\n\ncuenta "+cuentaCobro+"   institucion     "+institucion);
			return new ResultSetDecorator(pst.executeQuery());
		}
		catch(SQLException e){
			logger.error("Error cargando los datos de la factura en SqlBaseCuentasCobroDao: "+e);
			return null;
		}
	}

	
	
	/**
	 * Metodo para cargar las vias de ingreso relacionada a una cuenta de cobro
	 * @param con
	 * @param cuentaCobro
	 * @param institucion
	 * @return
	 */
	public static ResultSetDecorator cargarViasIngreso(Connection con, double cuentaCobro, int institucion) 
	{
		try{
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(cargarViasIngreso,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setDouble(1,cuentaCobro);
			pst.setInt(2,institucion);
			return new ResultSetDecorator(pst.executeQuery());
		}
		catch(SQLException e)
		{
			logger.error("Error cargando las vias de ingreso relacionadas a una CXC: "+e);
			return null;
		}
	}

	/**
	 * Metodo para cargar los movimiento que tiene una cuenta de cobro.
	 * @param con
	 * @param cuentaCobro
	 * @param institucion
	 * @return ResultSet
	 */
	public static ResultSetDecorator cargarMovimientoCxC(Connection con, double cuentaCobro, int institucion) 
	{
		try{
			logger.info("SQL / cargarMovimientoCxC / "+movimientosCuentasCobro);
			logger.info("Cuenta de Cobro = "+cuentaCobro);
			logger.info("Institucion = "+institucion);
			
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(movimientosCuentasCobro,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setDouble(1,cuentaCobro);
			pst.setInt(2,institucion);
			return new ResultSetDecorator(pst.executeQuery());
		}
		catch(SQLException e){
			logger.error("Error cargando los movimientos de la cuenta de cobro.: "+e);
			return null;
		}
	}

	
	/**
	 * Metodo que retorna un ResultSetDecorator Con los codigos de las facturas
	 * realiconadas a una cuenta de cobro.
	 * @param con
	 * @param cuentaCobro
	 * @param institucion
	 * @return
	 */
	public static ResultSetDecorator facturasCuentaCobro(Connection con, double cuentaCobro, int institucion)
	{
		try{
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(facturasCuentasCobro,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setDouble(1,cuentaCobro);
			pst.setInt(2,institucion);
			return new ResultSetDecorator(pst.executeQuery());
		}
		catch(SQLException e){
			logger.error("Error cargando los de las facturas de la cuenta de cobro.: "+e);
			return null;
		}

	}
	
	
	
	public static Collection movimientosCxC (Connection con, double numeroCuentaCobro)
	{
		try
		{
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(motivoAnulacionCxCStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setDouble(1,numeroCuentaCobro);
			return UtilidadBD.resultSet2Collection(new ResultSetDecorator(pst.executeQuery()));
		}
		catch(SQLException e)
		{
			logger.error("Error cargando el motivo de Anulaci�n de una cuenta de Cobro: "+e);
			return null;
		}
	}
	
	
	
	
	
	
	public static Collection cargarDatosImpresionResumida(Connection con, double numCuentaCobro) throws SQLException
	{
		try
		{
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(cargarResumenImpresion,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setDouble(1,numCuentaCobro);
			return UtilidadBD.resultSet2Collection(new ResultSetDecorator(pst.executeQuery()));
		}
		catch(SQLException e)
		{
			logger.error("Error cargando el datos de la impresion resumida de una cuenta de cobro: "+e);
			return null;
		}
	}
	
	
	/**
	 * M�todo para cargar el nit de una convenio segun una cuenta de cobro
	 * @param con
	 * @param numCuentaCobro
	 * @return
	 */
	public  static ResultSetDecorator convenioxCuentaCobro(Connection con, double numCuentaCobro)
	{
		try
		{
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(convenioxCuentaCobroStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setDouble(1,numCuentaCobro);
			return new ResultSetDecorator(pst.executeQuery());
		}
		catch(SQLException e)
		{
			logger.error("Error cargando el convenio de una cuenta de Cobro: "+e);
			return null;
		}
	}
	
	
	
	
	/**
	 * M�todo usado para registrar la radicaci�n de una cuenta de cobro
	 * @param con
	 * @param numCuentaCobro
	 * @param fechaRadicacion
	 * @param numRadicacion
	 * @param usuarioRadica
	 * @param observacionRadicacion
	 * @param institucion
	 * @return
	 */
	public static int insertarRadicacion(Connection con,
			double numCuentaCobro,String fechaRadicacion,String numRadicacion,
			String usuarioRadica,String observacionRadicacion,int institucion){
		
		try{
			
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(ingresarRadicacionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setString(1,UtilidadFecha.conversionFormatoFechaABD(fechaRadicacion));
			pst.setString(2,numRadicacion);
			pst.setString(3,usuarioRadica);
			pst.setString(4, UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
			pst.setString(5, UtilidadFecha.getHoraActual());
			pst.setString(6,observacionRadicacion);
			pst.setDouble(7,numCuentaCobro);
			pst.setInt(8,institucion);

			return pst.executeUpdate();
			
			
		}
		catch(SQLException e){
			
			logger.error("Error insertando la radicaci�n en SqlBaseCuentasCobroDao: "+e);
			return -1;
		}
	}

	/**
	 * Metodo que retorna las facturas para ser asignadas a una cuenta de cobro
	 * la consulta se basa en los parametro principales de las cuentas de cobro.
	 * @param con
	 * @param codigo
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param codigoViaIngreso
	 * @param cxc
	 * @param institucion
	 * @param codigoCentroAtencion 
	 * @return
	 */
	public static ResultSetDecorator consultarFacturasCxC(Connection con, int convenio, String fechaInicial, String fechaFinal, String codigoViaIngreso, double cxc, int institucion, int codigoCentroAtencion)
	{
		String facturasParaCxC="SELECT " +
									"f.codigo as codigo," +
									"f.consecutivo_factura as consecutivo_factura, " +
									"to_char(f.fecha, '"+ConstantesBD.formatoFechaBD+"') as fecha," +
									"f.via_ingreso as via_ingreso, " +
									"getnombreviaingreso(f.via_ingreso) as nombre_viaingreso," +
									"f.valor_convenio as valor_convenio," +
									"valor_cartera as valor_cartera, " +
									"getSaldoFacturaAjustes(f.codigo) as valor, " +
									"((case when f.ajustes_credito IS NULL then 0 else f.ajustes_credito end)-(case when f.ajustes_debito IS NULL then 0 else f.ajustes_debito end)) as valor_ajustes," +
									"f.centro_aten as codigocentroatencion," +
									"getnomcentroatencion(f.centro_aten) as nombrecentroatencion " +
								"FROM " +
									"facturas f " +
								"WHERE " +
									" f.estado_facturacion="+ConstantesBD.codigoEstadoFacturacionFacturada+" " +
									" and f.via_ingreso IN ("+codigoViaIngreso +") "+
									" and f.convenio="+convenio+
									" and f.institucion=" +institucion +
									" and to_char(f.fecha, '"+ConstantesBD.formatoFechaBD+"') between '"+UtilidadFecha.conversionFormatoFechaABD(fechaInicial)+"' and '"+UtilidadFecha.conversionFormatoFechaABD(fechaFinal)+"' " +
									" and (f.numero_cuenta_cobro is NULL";
		try{
			
			if(cxc==ConstantesBD.codigoNuncaValido)
				facturasParaCxC=facturasParaCxC+")";
			else
				facturasParaCxC=facturasParaCxC+" or f.numero_cuenta_cobro="+cxc+")";
			
			if(codigoCentroAtencion>0)
				facturasParaCxC=facturasParaCxC+" and f.centro_aten="+codigoCentroAtencion;
			
			facturasParaCxC += " ORDER BY 5,f.consecutivo_factura";
			logger.info("SQL / "+facturasParaCxC);
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(facturasParaCxC,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			return new ResultSetDecorator(pst.executeQuery());
		}
		catch(SQLException e){
			logger.error("Error cargando los de las facturas para la cuenta de cobro.: "+e);
			return null;
		}
	}



	/**
	 * @param con
	 * @param consulta
	 * @return
	 */
	public static ResultSetDecorator siguienteCuentaCobro(Connection con, String consulta) 
	{
		try{
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			return new ResultSetDecorator(pst.executeQuery());
		}
		catch(SQLException e){
			logger.error("Error incrementando el consecutivo de cuenta de cobro.: "+e);
			return null;
		}
	}



	/**
	 * Metodo para realizar la insercion de la cabecera de una
	 * cuenta de cobro
	 * @param con
	 * @param numeroCuentaCobro
	 * @param codigoConvenio
	 * @param estado
	 * @param fechaElaboracion
	 * @param horaElaboracion
	 * @param usuario
	 * @param valorInicial
	 * @param saldo
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param observacionesGen
	 * @param institucion
	 * @param centroAtencion 
	 * @return
	 */

	public static boolean insertarEncabezadoCuentaCobro(Connection con, double numeroCuentaCobro, int codigoConvenio, int estado, String fechaElaboracion, String horaElaboracion, String usuario, double valorInicial, double saldo, String fechaInicial, String fechaFinal, String observacionesGen, int institucion, int centroAtencion) 
	{
		try{
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(insertarCabezaCuentaCobro,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setDouble(1,numeroCuentaCobro);
			pst.setInt(2,codigoConvenio);
			pst.setInt(3,estado);
			pst.setDate(4,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(fechaElaboracion)));
			pst.setString(5,horaElaboracion.substring(0,5));			
			pst.setString(6,usuario);
			pst.setDouble(7,valorInicial);
			pst.setDouble(8,saldo);
			pst.setDate(9,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(fechaInicial)));
			pst.setDate(10,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(fechaFinal)));
			pst.setString(11,observacionesGen);
			pst.setInt(12,institucion);
			if(centroAtencion>0)
			{
				pst.setInt(13,centroAtencion);
			}
			else
			{
				pst.setObject(13,null);
			}
			pst.executeUpdate();
			return true;
		}
		catch(SQLException e){
			logger.error("Error Insertando el encabezado de la Cueta \n"+e.getStackTrace());
			e.printStackTrace();
			return false;
		}
	}



	/**
	 * @param con
	 * @param numeroCuentaCobro
	 * @param viaIngreso
	 * @param institucion
	 * @return
	 */
	public static boolean insertarViasIngresCxC(Connection con, double numeroCuentaCobro, int viaIngreso, int institucion) 
	{
		try{
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(insertarViasIngresoCuentaCobro,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setDouble(1,numeroCuentaCobro);
			pst.setInt(2,viaIngreso);
			pst.setInt(3,institucion);
			pst.executeUpdate();
			return true;
		}
		catch(SQLException e){
			logger.error("Error Insertando las vias de ingreso Cueta"+e);
			return false;
		}
	}



	/**
	 * @param con Conecction, conexi�n con la fuente de datos
	 * @param numeroCuentaCobro double, n�mero de la cuenta de cobro
	 * @param factura int, c�digo de la factura
	 * @param codInstitucion int, c�digo de la instituci�n	 
	 * @return boolean
	 */
	public static boolean actulizarNumeroCxCFactura(Connection con, double numeroCuentaCobro, int factura,int codInstitucion) 
	{
		try{
			logger.info("\n\nquery:: "+actualizarFacturasCuentaCobro+"  cuenta:: "+numeroCuentaCobro+" factura:: "+factura+" institucion::"+codInstitucion);
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(actualizarFacturasCuentaCobro,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setDouble(1,numeroCuentaCobro);
			pst.setInt(2,factura);
			pst.setInt(3,codInstitucion);
			pst.executeUpdate();
			pst.close();
			return true;
		}
		catch(SQLException e){
			logger.error("Error Actualizando Facturas ",e);
			return false;
		}
		
	}

	/**
	 * @param con
	 * @param consecutivoFactura
	 * @param institucion
	 * @return
	 */
	public static ResultSetDecorator cargarFacutraParaCxC(Connection con, int codigoFactura) 
	{
		try{
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(consultasFacturasCxC,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,codigoFactura);
			return new ResultSetDecorator(pst.executeQuery());
		}
		catch(SQLException e){
			logger.error("Error Actualizando Facturas"+e);
		}
		return null;
	}
	
	
	/**
	 * M�todo que consulta todas las facturas de una cuenta de cobro
	 * segun la fecha de elaboracion
	 * @param con
	 * @param numeroCuentaCobro
	 * @param fecha
	 * @return
	 */
	public static ResultSetDecorator cargarFacturasPorFecha(Connection con, double numeroCuentaCobro, String fecha)
	{
		try
		{
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(consultaFacturasPorFechaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setDouble(1,numeroCuentaCobro);
			pst.setString(2,fecha);
			return new ResultSetDecorator(pst.executeQuery());
			
		}
		catch(SQLException e)
		{
			logger.error("Error Consultando Facturas por fecha "+e);
			return null;
		}
	}
	
	
	/**
	 * M�todo que me devualve las facturas segun una via de ingreso y su numero de cuenta de cobro
	 * @param con
	 * @param numeroCuentaCobro
	 * @param viaIngreso
	 * @return
	 */
	public static ResultSetDecorator cargarFacturasPorViaIngreso(Connection con, double numeroCuentaCobro, String viaIngreso)
	{
		try
		{
			String cadena=" SELECT f.codigo as codigofactura FROM  facturas f "+
						  " INNER JOIN vias_ingreso_cxc vcxc ON(f.numero_cuenta_cobro=vcxc.numero_cuenta_cobro) "+
						  " WHERE f.numero_cuenta_cobro="+numeroCuentaCobro +
						  " AND UPPER(getnombreviaingreso(vcxc.via_ingreso)) like UPPER('%"+viaIngreso+"%')";
			
			Statement st=con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			return  new ResultSetDecorator(st.executeQuery(cadena));
		}
		catch(SQLException e)
		{
			logger.error("Error Consultando Facturas por Via de Ingreso "+e);
			return null;
		}
		
	}
	
	/**
	 * @param con
	 * @param numeroCuentaCobro
	 * @param viaIngreso
	 * @return
	 */
	public static int cargarFacturaPorConsecutivo(Connection con, double numeroCuentaCobro, int consecutivo_factura)
	{
		
		try
		{
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(consultarFacturaPorConsecutivoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setDouble(1,numeroCuentaCobro);
			pst.setInt(2,consecutivo_factura);
			ResultSetDecorator rs=new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
			{
				return rs.getInt("codigofactura");
			}
			else
			{
				return -1;
			}
			
		}
		catch(SQLException e)
		{
			logger.error("Error Consultando el codigo de la factura "+e);
			return -1;
		}
		
	}


	/**
	 * Metodo para verificar si una cuenta de cobro existe
	 * @param con Connection, conexi�n con la fuente de datos
	 * @param institucion int, codigo de la instituci�n
	 * @param numeroCuentaCobro double, n�mero de la cuenta de cobro
	 * @return ResultSet
	 * @author jarloc
	 */
	public static ResultSetDecorator existeCodigoCuentaCobroEnBD (Connection con,int institucion,double numeroCuentaCobro)
	{
	    
	    try
	    {
	        PreparedStatementDecorator ps = null;
	        
	        if (con == null || con.isClosed()) 
			{
				DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
				con = myFactory.getConnection();
			} 
	        ps=  new PreparedStatementDecorator(con.prepareStatement(existeCuentaCobroStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	        ps.setDouble(1,numeroCuentaCobro);
	        ps.setInt(2,institucion);	        
	        return new ResultSetDecorator(ps.executeQuery());
	    }
	    catch(SQLException e)
	    {
	        logger.warn(e+"Error existeCodigoCuentaCobroEnBD: SqlBaseCuentasCobroDao "+e.toString() );
		   return null;
	    }   
	}
	
	/**
	 * Metodo para verificar si una via de ingreso
	 * posee entrada en la tabla vias_ingreso_cxc
	 * @param con Connection, conexi�n con la fuente de datos
	 * @param institucion int, codigo de la instituci�n
	 * @param viaIngreso int, c�digo de la via de ingreso
	 * @param numCXC double, n�mero de la cuenta de cobro
	 * @see com.princetonsa.dao.CuentasCobroDao#existeViaIngresoCxCEnBD(Connection,int,int,double)
	 * @return ResultSet
	 * @author jarloc
	 */
	public static int existeViaIngresoCxCEnBD (Connection con,int institucion,int viaIngreso,double numCXC)
	{
	    
	    try
	    {   
	        if (con == null || con.isClosed()) 
			{
				DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
				con = myFactory.getConnection();
			} 
	        PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(existeViaIngresoCxCStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	        ps.setInt(1,viaIngreso);
	        ps.setInt(2,institucion);
	        ps.setDouble(3,numCXC);	        
	        ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
	        if(rs.next())
			{
			    return rs.getInt("vias");
			}        
	    }
	    catch(SQLException e)
	    {
	       logger.warn(e+"Error existeViaIngresoCxCEnBD: SqlBaseCuentasCobroDao "+e.toString() );
		   return 0;
	    }
	    
	    return 0;
	}


	/**
	 * @param con
	 * @param cuentaCobro
	 * @param institucion
	 * @return
	 */
	public static boolean eliminarViasIngresoCxC(Connection con, double cuentaCobro, int institucion) 
	{
		 try
			{
		        if (con == null || con.isClosed()) 
				{
					throw new SQLException ("Error SQL en eliminar SqlBaseCuentasCobroDao : Conexi�n cerrada");
				}
		        String cadena="DELETE from vias_ingreso_cxc where numero_cuenta_cobro=? and institucion=?";
		        PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		        ps.setDouble(1,cuentaCobro);
		        ps.setInt(2,institucion);
		        if(ps.executeUpdate()>0)
					return true;
				else
					return false;	        
			}
		    catch(SQLException e)
			{
				logger.warn(e+"Error en la eliminaci�n de registro: SqlBaseCuentasCobroDao "+e.toString());
				return false;			
			}	
	}


	/**
	 * @param con
	 * @param valorCartera
	 * @param codigoFactura
	 * @param institucion
	 * @return
	 */
	public static boolean actualizaValorCartera(Connection con, double valorCartera, int codigoFactura, int institucion) 
	{
		try{
			String cadena="UPDATE facturas SET valor_cartera = ? where codigo=? and institucion=?";
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setDouble(1,valorCartera);
			pst.setInt(2,codigoFactura);
			pst.setInt(3,institucion);
			pst.executeUpdate();
			return true;
		}
		catch(SQLException e){
			logger.error("Error Actualizando Facturas"+e);
			return false;
		}
	}
	
	/**
	 * Metodo implementado para verificar si una cuenta
	 * de cobro esta radicada
	 * @param con Connection, conexi�n con la fuente de datos
	 * @param institucion int, codigo de la instituci�n	
	 * @param numCXC double, n�mero de la cuenta de cobro
	 * @see com.princetonsa.dao.CuentasCobroDao#cuentaCobroPoseeRadicacion(Connection,int,double)
	 * @return boolean, true si es radicada
	 * @author jarloc
	 */
	public static boolean cuentaCobroPoseeRadicacion (Connection con,int institucion,double numCXC)
	{
	    
	    try
	    {   
	        if (con == null || con.isClosed()) 
			{
				DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
				con = myFactory.getConnection();
			} 
	        PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(cuentaCobroPoseeRadicacionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));	        
	        ps.setDouble(1,numCXC);
	        ps.setDouble(2,institucion);	
	        ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
	        while (rs.next())
	        {
		        if(rs.getInt("numFilas")!=0)
		          return true;
		        else
		          return false;
	        }
	    }
	    catch(SQLException e)
	    {
	       logger.warn(e+"Error cuentaCobroPoseeRadicacion: SqlBaseCuentasCobroDao "+e.toString() );
	       return false;
	    }
        return false;     
	}
	
	/**
	 * M�todo para consultar los estados de la Glosa correspondiente a la Factura a Inactivar
	 * @param con
	 * @param consecutivoFactura
	 * @return
	 */
	public static HashMap estadoGlosasFacturas(Connection con,int consecutivoFactura) {
		
		HashMap estadoGlosasFacturas = new HashMap();
		estadoGlosasFacturas.put("numRegistros", 0);
		String cadena = estadoGlosaFacturaStr;
		
		cadena+="f.consecutivo_factura="+consecutivoFactura;
						
		try {
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			estadoGlosasFacturas = UtilidadBD.cargarValueObject(new ResultSetDecorator(st.executeQuery(cadena)));
			estadoGlosasFacturas.put("consultasql", cadena);
		}
		catch (SQLException e) {
			logger.warn("ERROR / guardarEncabezado / "+e);
		}
			
		return estadoGlosasFacturas;
	}
	
	

	/**
	 * M�todo para determinar si la factura posee una glosa en estado respondida y que no tenga ajustes asociados con el fin de no permitir inactivarla
	 * Validacion Anexo 712
	 * @param con
	 * @param consecutivoFactura
	 * @return
	 */
	public static int requiereGlosaInactivar(Connection con,int consecutivoFactura)
	{
		boolean requiere=false;
		boolean tieneGlosaRespondida=false;
		boolean tieneAjustesDieferenteAnulado=false;
		HashMap requiereGlosaInactivar = new HashMap();
		requiereGlosaInactivar.put("numRegistros", 0);
		HashMap requiereGlosaInactivar2 = new HashMap();
		requiereGlosaInactivar2.put("numRegistros", 0);
		HashMap requiereGlosaInactivar3 = new HashMap();
		requiereGlosaInactivar3.put("numRegistros", 0);
		String cadena=		"SELECT g.estado, " +
								"f.consecutivo_factura AS consecutivo, " +
								"cag.concepto_glosa " +
							"FROM " +
								"registro_glosas g " +
							"INNER JOIN " +
								"auditorias_glosas ag ON (ag.glosa=g.codigo) " +
							"INNER JOIN	" +
								"facturas f ON (f.codigo=ag.codigo_factura) " +
							"INNER JOIN " +
								"conceptos_audi_glosas cag  ON (cag.auditoria_glosa=ag.codigo) " +
							"INNER JOIN " +
								"fact_respuesta_glosa frg ON (frg.factura=f.codigo) " +
							"INNER JOIN " +
								"det_fact_resp_glosa dfrg ON (dfrg.fact_respuesta_glosa=frg.codigo) " +
							"INNER JOIN " +
								"concepto_glosas conglo on (cag.concepto_glosa=conglo.codigo and cag.institucion=conglo.institucion) " +
							"WHERE " +
								"g.estado='RESP' "+
							"AND " +
								"conglo.tipo_concepto='DEV' " +
							"AND " +
								"f.consecutivo_factura="+consecutivoFactura;
		
		
		String cadena2= 	"SELECT " +
								"g.glosa_sistema AS glosa, " +
								"g.estado,f.consecutivo_factura AS consecutivofact, " +
								"cag.concepto_glosa AS conceptoglosa " +
							"FROM " +
								"registro_glosas g " +
							"INNER JOIN " +
								"auditorias_glosas ag ON (ag.glosa=g.codigo) " +
							"INNER JOIN " +
								"facturas f ON (f.codigo=ag.codigo_factura) " +
							"INNER JOIN " +
								"conceptos_audi_glosas cag  ON (cag.auditoria_glosa=ag.codigo) " +
							"INNER JOIN " +
								"fact_respuesta_glosa frg ON (frg.factura=f.codigo) " +
							"INNER JOIN " +
								"det_fact_resp_glosa dfrg ON (dfrg.fact_respuesta_glosa=frg.codigo) " +
							"INNER JOIN " +
								"ajustes_empresa ae ON (ae.codigo=dfrg.ajuste) " +
							"INNER JOIN " +
								"estados_cartera ec ON (ec.codigo=ae.estado) " +
							"WHERE " +
								"dfrg.ajuste>0 " +
							"OR " +
								"ec.codigo<>2 " +
							"AND " +
								"f.consecutivo_factura="+consecutivoFactura;
		
		String cadena3 = 	"SELECT " +
								"*" +
							"FROM " +
								"valores_por_defecto " +
							"WHERE " +
								"parametro='requiere_glosa_para_inactivar' " +
							"AND " +
								"valor='true'";
						
		try {
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			requiereGlosaInactivar = UtilidadBD.cargarValueObject(new ResultSetDecorator(st.executeQuery(cadena)));
			if (Utilidades.convertirAEntero(requiereGlosaInactivar.get("numRegistros").toString())>0)
				tieneGlosaRespondida=true;
						
			requiereGlosaInactivar2 = UtilidadBD.cargarValueObject(new ResultSetDecorator(st.executeQuery(cadena2)));
			if (Utilidades.convertirAEntero(requiereGlosaInactivar2.get("numRegistros").toString())>0)
				tieneAjustesDieferenteAnulado=true;
							
			requiereGlosaInactivar3 = UtilidadBD.cargarValueObject(new ResultSetDecorator(st.executeQuery(cadena3)));
			if (Utilidades.convertirAEntero(requiereGlosaInactivar3.get("numRegistros").toString())>0)
				requiere=true;
						
			if (requiere)
			{
				if(!tieneGlosaRespondida)
					return 1;
				if(tieneAjustesDieferenteAnulado)
					return 2;
			}
			
			
		}
		catch (SQLException e) {
			logger.warn("ERROR / guardarEncabezado / "+e);
		}
		return ConstantesBD.codigoNuncaValido;	
	}


	
	/**
	 * 
	 * @param dtoFiltro
	 * @return
	 */
	public static ArrayList<DtoResultadoReporteCuentaCobro> generarReporteCuentaCobro(
			DtoFiltroReporteCuentasCobro dtoFiltro) {
		String cosultaReporte="select " +
										" convenio," +
										" getnombreconvenio(convenio) as nomconvenio," +
										" numero_cuenta_cobro as numerocuentacobro," +
										" estado as codigoestado," +
										" ec.descripcion as desceestado," +
										" cc.centro_atencion as centroatencion," +
										" coalesce(getnomcentroatencion(cc.centro_atencion),'Todos') as nomcentroatencion," +
										" to_char(cc.fecha_elaboracion,'dd/mm/yyyy') as fechaelaboracion," +
										" usuario_genera as usuariogenera," +
										" getnombreusuario(usuario_genera) as nomusuariogenera," +
										" to_char(fecha_radicacion,'dd/mm/yyyy') as fecharadicacion," +
										" usuario_radica as usuarioradica," +
										" getnombreusuario(usuario_radica) as nomusuarioradica," +
										" cc.valor_inicial_cuenta as valor " +
								" from cuentas_cobro cc " +
								" inner join estados_cartera ec on (ec.codigo=cc.estado) " +
								" where 1=1 ";
		ArrayList<DtoResultadoReporteCuentaCobro> resultado=new ArrayList<DtoResultadoReporteCuentaCobro>();
		Connection con=UtilidadBD.abrirConexion();
		if(!dtoFiltro.getFechaElaboracionInicial().isEmpty()&&!dtoFiltro.getFechaElaboracionFinal().isEmpty())
		{
			cosultaReporte=cosultaReporte+" and cc.fecha_elaboracion between '"+UtilidadFecha.conversionFormatoFechaABD(dtoFiltro.getFechaElaboracionInicial())+"' and '"+UtilidadFecha.conversionFormatoFechaABD(dtoFiltro.getFechaElaboracionFinal())+"'";
		}
		
		if(!dtoFiltro.getFechaRadicacionInicial().isEmpty()&&!dtoFiltro.getFechaRadicacionFinal().isEmpty())
		{
			cosultaReporte=cosultaReporte+" and fecha_radicacion between '"+UtilidadFecha.conversionFormatoFechaABD(dtoFiltro.getFechaRadicacionInicial())+"' and '"+UtilidadFecha.conversionFormatoFechaABD(dtoFiltro.getFechaRadicacionFinal())+"'";
		}
		if(Utilidades.convertirAEntero(dtoFiltro.getCuentaCobroInicial())>0&&Utilidades.convertirAEntero(dtoFiltro.getCuentaCobroFinal())>0)
		{
			cosultaReporte=cosultaReporte+" and numero_cuenta_cobro between "+dtoFiltro.getCuentaCobroInicial()+" and "+dtoFiltro.getCuentaCobroFinal();
		}
		if(dtoFiltro.getCentroAtencion()>0)
		{
			cosultaReporte=cosultaReporte+" and cc.centro_atencion="+dtoFiltro.getCentroAtencion();
		}
		
		if(dtoFiltro.getConvenio()>0)
		{
			cosultaReporte=cosultaReporte+" and convenio="+dtoFiltro.getConvenio();
		}
		if(dtoFiltro.getEstadoCuentaCobro()>0)
		{
			cosultaReporte=cosultaReporte+" and estado="+dtoFiltro.getEstadoCuentaCobro();
		}
		
		if(!dtoFiltro.getUsuarioElaboracion().isEmpty())
		{
			cosultaReporte=cosultaReporte+" and usuario_genera='"+dtoFiltro.getUsuarioElaboracion()+"'";
		}
		
		if(!dtoFiltro.getUsuarioRadicacion().isEmpty())
		{
			cosultaReporte=cosultaReporte+" and usuario_radica='"+dtoFiltro.getUsuarioRadicacion()+"'";
		}
		
		PreparedStatementDecorator ps=new PreparedStatementDecorator(con,cosultaReporte+" order by cc.convenio,cc.fecha_elaboracion,cc.numero_cuenta_cobro asc");
		ResultSetDecorator rs;
		try 
		{
			rs = new ResultSetDecorator(ps.executeQuery());
			int codigoConvenioAnterior=ConstantesBD.codigoNuncaValido;
			while(rs.next())
			{
				DtoResultadoReporteCuentaCobro dto;
				if(rs.getInt("convenio")!=codigoConvenioAnterior)
				{
					dto=new DtoResultadoReporteCuentaCobro();
					dto.setCodigoConvenio(rs.getInt("convenio"));
					dto.setNombreConvenio(rs.getString("nomconvenio"));
					resultado.add(dto);
				}
				dto=resultado.get(resultado.size()-1);
				codigoConvenioAnterior=dto.getCodigoConvenio();
				DtoDetalleReporteCuentaCobro dtoDetalle=new DtoDetalleReporteCuentaCobro();
				dtoDetalle.setCuentaCobro(rs.getInt("numerocuentacobro"));
				dtoDetalle.setCodigoEstado(rs.getInt("codigoestado"));
				dtoDetalle.setDescripcionEstado(rs.getString("desceestado"));
				dtoDetalle.setCentroAtencion(rs.getInt("centroatencion"));
				dtoDetalle.setDescripcionCentroAtencion(rs.getString("nomcentroatencion"));
				dtoDetalle.setFechaElaboracion(rs.getString("fechaelaboracion"));
				dtoDetalle.setUsuarioElaboracion(rs.getString("usuariogenera"));
				dtoDetalle.setNombreUsuarioElaboraccion(rs.getString("nomusuariogenera"));
				dtoDetalle.setFechaRadicacion(rs.getString("fecharadicacion"));
				dtoDetalle.setUsuarioRadicacion(rs.getString("usuarioradica"));
				dtoDetalle.setNombreUsuarioRadicacion(rs.getString("nomusuarioradica"));
				dtoDetalle.setValor(rs.getDouble("valor"));
				dto.getDtoDetalle().add(dtoDetalle);
			}
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		finally
		{
			UtilidadBD.closeConnection(con);
		}
		return resultado;
	}	
}

