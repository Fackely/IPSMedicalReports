package com.princetonsa.dao.sqlbase.facturacion;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.InfoDatos;
import util.InfoDatosDouble;
import util.InfoDatosInt;
import util.InfoPorcentajeValor;
import util.ResultadoInteger;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.facturacion.DtoAsociosDetalleFactura;
import com.princetonsa.dto.facturacion.DtoDetAsociosDetFactura;
import com.princetonsa.dto.facturacion.DtoDetalleFactura;
import com.princetonsa.dto.facturacion.DtoFactura;
import com.princetonsa.dto.facturacion.DtoPaquetizacionDetalleFactura;
import com.princetonsa.mundo.facturacion.CalculoHonorariosPooles;
import com.servinte.axioma.dto.facturacion.DtoAgrupacionCalucloValorCobrarPaciente;
import com.servinte.axioma.dto.facturacion.DtoInfoCargoCobroPaciente;

/**
 * 
 * @author wilson
 *
 */
public class SqlBaseFacturaDao 
{
	/**
	 * Manejador de logs
	 */
	private static Logger logger = Logger.getLogger(SqlBaseFacturaDao.class);
	
	/**
	 * Insert de la factura
	 */
	private final static String insertarFactStr =	"INSERT INTO facturas " +
																		"(" +
																		"codigo," + 					//1
																		"sub_cuenta, " +				//2
																		"estado_facturacion, " +		//3	
																		"estado_paciente, " +			//4
																		"usuario, " +					//5
																		"consecutivo_factura, " +		//6
																		"pref_factura, " +				//7  //
																		"institucion, " +				//8
																		"valor_abonos, " +				//9
																		"valor_total, " +				//10
																		"valor_neto_paciente, " +		//11
																		"valor_convenio, " +			//12
																		"valor_bruto_pac, " +			//13
																		"val_desc_pac, " +				//14
																		"numero_cuenta_cobro, " +		//15
																		"tipo_factura_sistema, " +		//16
																		"cod_paciente," +				//17 //(SELECT codigo_paciente FROM cuentas WHERE id=?)
																		"cod_res_particular, " +		//18
																		"cuenta, " +					//19
																		"convenio, " +					//20
																		"via_ingreso, " +				//21 //
																		"valor_cartera, " +				//22
																		"monto_cobro, " +				//23 
																		"centro_aten, " +				//24 //(SELECT cc.centro_atencion FROM centros_costo cc, cuentas c where c.area=cc.codigo AND c.id=?)
																		"valor_pagos, " +				//25 //0
																		"ajustes_credito, " +			//26 //0
																		"ajustes_debito, " +			//27 //0
																		"factura_cerrada, " +			//28 //false
																		//"valor_monto, " +				//29 //
																		//"porcentaje_monto, " +			//30 //
																		"tipo_afiliado, " +				//31 //
																		"estrato_social, " +			//32 //
																		"tipo_monto,   " +				//33 //
																		"valor_favor_convenio, "+		//34
																		"resolucion, " +				//35
																		"rgo_inic_fact, " +				//36
																		"rgo_fin_fact, "+				//37
																		"fecha, " +						//38
																		"hora, " +						//39
																		"entidad_subcontratada, " +		//40	
																		"empresa_institucion, " +		//41
																		"pac_entidad_subcontratada, " +	//42	
																		"valor_liquidado_paciente, " +	//43
																		"tope_facturacion, " +			//44
																		"vigencia_ini_monto_c, " +		//45
																		"vigencia_ini_tope_f, " +		//46
																		"centro_atencion_duenio, " +	//47
																		"valor_anticipo, historico_encabezado,contrato) "  + 			//48 //49
																		"VALUES" +
																		"(?, ?, ?, ?, ?, " +//5
																		" ?, ?, ?, ?, ?, " +//10
																		" ?, ?, ?, ?, ?, " +//15
																		" ?, ?, ?, ?, ?, " +//20
																		" ?, ?, ?, ?, ?, " +//25
																		" ?, ?, ?, ?, ?, " +//30
																		" ?, ?, ?, ?, ?, " +//35
																		" ?, ?, ?," +	//38
																		" ?, " +
																		" ?, ?, ?, ?, ?, ?, ? , ? ,?)";//46
																		
	
	/**
	 * Método que sirve para insertar el detalle de una factura
	 */
	private final static String insertarDetFacStr=	"INSERT INTO det_factura_solicitud " +
																		"(codigo, " +					//1
																		"solicitud, " +					//2
																		"factura, " +					//3
																		"ajuste_credito_medico, " +		//4
																		"porcentaje_pool, " +			//5
																		"porcentaje_medico, " +			//6
																		"valor_medico, " +				//7
																		"fecha_cargo, " +				//8
																		"cantidad_cargo, " +			//9
																		"valor_cargo, " +				//10
																		"valor_iva, " +					//11
																		"valor_recargo, " +				//12
																		"soli_pago, " +					//13
																		"ajustes_credito, " +			//14	
																		"servicio, " +					//15
																		"articulo, " +					//16	
																		"valor_total, " +				//17
																		"ajustes_debito, " +			//18
																		"tipo_cargo, " +				//19
																		"ajuste_debito_medico, " +		//20
																		"valor_pool, " +				//21 (SELECT pool from solicitudes where numero_solicitud =?)
																		"pool, " +						//22 (SELECT codigo_medico_responde from solicitudes where numero_solicitud =?)
																		"codigo_medico, " +				//23
																		"valor_dcto_comercial, " +		//24
																		"valor_consumo_paquete, " +		//25
																		"tipo_solicitud, " +			//26
																		"esquema_tarifario, " +			//27
																		"programa, " +					//28
																		"valor_dcto_odo, " +			//29
																		"valor_dcto_bono, " +			//30
																		"valor_dcto_prom,  " +			//31
																		"det_paq_odon_convenio "+		//32
																		") " +
																		"VALUES " +
																		"(?, ?, ?, ?, ?, " +//5
																		" ?, ?, ?, ?, round(?,2), " +//10
																		" ?, ?, ?, ?, ?, " +//15
																		" ?, round(?,2), ?, ?, ?, " +//20
																		" ?, ?, ?, ?, ?, " +		 //25	
																		" ?, ?, ?, ?, ?, " +		//30
																		" ?, ?)";			//32
																					
	
	/**
	 * Método que sirve para insertar el detalle de una factura
	 */
	private final static String insertarDetFacExcentaStr=	"INSERT INTO facturacion.det_factura_solicitud_excenta " +
																		"(codigo, " +					//1
																		"solicitud, " +					//2
																		"factura, " +					//3
																		"ajuste_credito_medico, " +		//4
																		"porcentaje_pool, " +			//5
																		"porcentaje_medico, " +			//6
																		"valor_medico, " +				//7
																		"fecha_cargo, " +				//8
																		"cantidad_cargo, " +			//9
																		"valor_cargo, " +				//10
																		"valor_iva, " +					//11
																		"valor_recargo, " +				//12
																		"soli_pago, " +					//13
																		"ajustes_credito, " +			//14	
																		"servicio, " +					//15
																		"articulo, " +					//16	
																		"valor_total, " +				//17
																		"ajustes_debito, " +			//18
																		"tipo_cargo, " +				//19
																		"ajuste_debito_medico, " +		//20
																		"valor_pool, " +				//21 (SELECT pool from solicitudes where numero_solicitud =?)
																		"pool, " +						//22 (SELECT codigo_medico_responde from solicitudes where numero_solicitud =?)
																		"codigo_medico, " +				//23
																		"valor_dcto_comercial, " +		//24
																		"valor_consumo_paquete, " +		//25
																		"tipo_solicitud, " +			//26
																		"esquema_tarifario, " +			//27
																		"programa, " +					//28
																		"valor_dcto_odo, " +			//29
																		"valor_dcto_bono, " +			//30
																		"valor_dcto_prom,  " +			//31
																		"det_paq_odon_convenio "+		//32
																		") " +
																		"VALUES " +
																		"(?, ?, ?, ?, ?, " +//5
																		" ?, ?, ?, ?, round(?,2), " +//10
																		" ?, ?, ?, ?, ?, " +//15
																		" ?, round(?,2), ?, ?, ?, " +//20
																		" ?, ?, ?, ?, ?, " +		 //25	
																		" ?, ?, ?, ?, ?, " +		//30
																		" ?, ?)";			//32
																					
	/**
	 * cadena para la insercion de los asocios que conforma el detalle de factura (sol cx)
	 */
	private final static String insertarAsociosDetFacStr=	"INSERT INTO asocios_det_factura (" +
																	"codigo, " +				//1
																	"servicio_asocio, " +		//2
																	"valor_asocio, " +			//3
																	"tipo_asocio, " +			//4
																	"ajuste_credito_medico, " +	//5
																	"ajuste_debito_medico, " +	//6
																	"porcentaje_pool, " +		//7
																	"porcentaje_medico,  " +	//8
																	"valor_medico,  " +			//9
																	"valor_cargo, " +			//10
																	"valor_iva, " +				//11
																	"valor_recargo, " +			//12
																	"valor_total, " +			//13
																	"ajustes_credito, " +		//14	
																	"ajustes_debito," +			//15
																	"esquema_tarifario," +		//16
																	"valor_pool," +				//17
																	"pool," +					//18
																	"codigo_medico, " +			//19
																	"consecutivo, " +			//20
																	"medico_asocio, "+			//21
																	"especialidad ,codigo_propietario "+	//23
																	") " +			
																	"values " +
																	"(?, ?, ?, ?, ?, " +	//5
																	" ?, ?, ?, ?, ?," +		//10
																	" ?, ?, ?, ?, ?," +		//15
																	" ?, ?, ?, ?, ?, " +	//20
																	" ?, ?,?) "; 	//23
	
	
	/**
	 * cadena para la insercion de los asocios que conforma el detalle de factura (sol cx)
	 */
	private final static String insertarAsociosDetFacExcentaStr=	"INSERT INTO facturacion.asocios_det_factura_excenta (" +
																	"codigo, " +				//1
																	"servicio_asocio, " +		//2
																	"valor_asocio, " +			//3
																	"tipo_asocio, " +			//4
																	"ajuste_credito_medico, " +	//5
																	"ajuste_debito_medico, " +	//6
																	"porcentaje_pool, " +		//7
																	"porcentaje_medico,  " +	//8
																	"valor_medico,  " +			//9
																	"valor_cargo, " +			//10
																	"valor_iva, " +				//11
																	"valor_recargo, " +			//12
																	"valor_total, " +			//13
																	"ajustes_credito, " +		//14	
																	"ajustes_debito," +			//15
																	"esquema_tarifario," +		//16
																	"valor_pool," +				//17
																	"pool," +					//18
																	"codigo_medico, " +			//19
																	"consecutivo, " +			//20
																	"medico_asocio, "+			//21
																	"especialidad ,codigo_propietario "+	//23
																	") " +			
																	"values " +
																	"(?, ?, ?, ?, ?, " +	//5
																	" ?, ?, ?, ?, ?," +		//10
																	" ?, ?, ?, ?, ?," +		//15
																	" ?, ?, ?, ?, ?, " +	//20
																	" ?, ?,?) "; 	//23
	
	/**
	 * 
	 */
	private final static String insertarDetAsociosDetFacturaStr="INSERT INTO facturacion.det_asocios_det_factura (" +
																			"codigo , " +				//1
																			"asocio_det_factura , " +	//2
																			"articulo , " +				//3
																			"cantidad , " +				//4
																			"valor_unitario , " +		//5
																			"valor_total , " +			//6
																			"fecha_modifica , " +		
																			"hora_modifica,  " +		
																			"usuario_modifica " +		//7
																			")" +
																			"VALUES " +
																			"(" +
																				"?, ?, ?, ?, ?, ?, " +//6
																				"current_date, "+ValoresPorDefecto.getSentenciaHoraActualBD()+", ? " +//7 
																			")";		
	
	/**
	 * 
	 */
	private final static String insertarDetAsociosDetFacturaExcentaStr="INSERT INTO facturacion.det_asocios_det_factura_exc (" +
																			"codigo , " +				//1
																			"asocio_det_factura , " +	//2
																			"articulo , " +				//3
																			"cantidad , " +				//4
																			"valor_unitario , " +		//5
																			"valor_total , " +			//6
																			"fecha_modifica , " +		
																			"hora_modifica,  " +		
																			"usuario_modifica " +		//7
																			")" +
																			"VALUES " +
																			"(" +
																				"?, ?, ?, ?, ?, ?, " +//6
																				"current_date, "+ValoresPorDefecto.getSentenciaHoraActualBD()+", ? " +//7 
																			")";		
	
	
	/**
	 * cadena para la insercion de paquetizacion que conforma el detalle de factura (sol cx)
	 */
	private final static String insertarPaquetizacionDetFacStr=	"INSERT INTO paquetizacion_det_factura (" +
																		"codigo, " +					//1
																		"codigo_det_fact, " +			//2
																		"servicio, " +					//3
																		"articulo, " +					//4
																		"ajuste_credito_medico, " +		//5	
																		"ajuste_debito_medico, " +		//6
																		"valor_cargo, " +				//7
																		"valor_iva, " +					//8
																		"valor_recargo, " +				//9
																		"valor_total, " +				//10
																		"valor_dif_consumo_valor, " +	//11
																		"ajustes_credito, " +			//12
																		"ajustes_debito," +				//13
																		"porcentaje_medico," +			//14
																		"porcentaje_pool," +			//15
																		"servicio_cx, "+				//16
																		"valor_asocio, "+				//17
																		"tipo_asocio, "+             	//18
																		"valor_medico, "+            	//19
																		"valor_pool, "+              	//20
																		"pool, "+                    	//21
																		"codigo_medico, "+           	//22
																		"soli_pago, "+               	//23
																		"cantidad_cargo, "+          	//24
																		"tipo_cargo, "+              	//25
																		"tipo_solicitud," +				//26
																		"solicitud, " +					//27
																		"esquema_tarifario, "+			//28
																		"medico_asocio ,"+				//29
																		"especialidad_asocio "+			//30
																		") "+				          	
																	"values " +
																	"(?, ?, ?, ?, ?, " +	//5
																	" ?, ?, ?, ?, ?," +		//10
																	" ?, ?, ?, ?, ?," +		//15
																	" ?, ?, ?, ?, ?," +		//20
																	" ?, ?, ?, ?, ?," +		//25
																	" ?, ?, ?, ?, ?)";		//30
																	
	/**
	 * Carga la información de la Factura 
	 * (idFactura)
	 */
	private final static String cargarFacturaStr = 	"SELECT " +
															"to_char(f.fecha,'DD/MM/YYYY') AS fecha, " +
															"f.hora AS hora, " +
															"f.estado_facturacion AS codigoEstadoFacturacion, " +
															"ef.nombre AS nombreEstadoFacturacion, " +
															"f.estado_paciente AS codigoEstadoPaciente, " +
															"efp.nombre AS nombreEstadoPaciente, " +
															"f.usuario AS loginUsuario, " +
															"f.consecutivo_factura AS consecutivoFactura, " +
															"coalesce(f.pref_factura,'') AS prefijoFactura, " +
															"coalesce(f.institucion, "+ConstantesBD.codigoNuncaValido+") AS codigoInstitucion, "+
															//"getSaldoFacturaAjustes(fact.codigo) as valor_cartera, " +
															"coalesce(f.valor_pagos, 0) AS valorPagos, "+
															"coalesce(f.ajustes_credito, 0) AS ajustesCredito, "+
															"coalesce(f.ajustes_debito, 0) AS ajustesDebito, "+
															"f.valor_abonos AS valorAbonos, " +
															"f.valor_total AS valorTotal, " +
															"f.valor_neto_paciente AS valorNetoPaciente, " +
															"f.valor_convenio AS valorConvenio, " +
															"f.valor_cartera AS valorCartera," +
															"f.valor_liquidado_paciente AS valorLiquidadoPaciente, " +
															"f.valor_bruto_pac AS valorBrutoPaciente, " +
															"f.val_desc_pac AS valorDescuentoPaciente, " +
															"coalesce(f.numero_cuenta_cobro, "+ConstantesBD.codigoNuncaValidoDoubleNegativo+") as numeroCuentaCobro, "+
															"f.tipo_factura_sistema as tipoFacturaSistema, " +
															"coalesce(f.cod_paciente, "+ConstantesBD.codigoNuncaValido+") AS codigoPaciente, " +
															"getidpaciente(f.cod_paciente) as idpaciente, "  +
															"getnombrepersona(f.cod_paciente) as nombrepaciente, " +
															"coalesce(f.cod_res_particular,"+ConstantesBD.codigoNuncaValido+") AS codigoResParticular, " +
															"coalesce(f.cuenta, "+ConstantesBD.codigoNuncaValido+") AS cuenta, " +
															"coalesce(f.convenio, "+ConstantesBD.codigoNuncaValido+") AS codigoConvenio, " +
															"CASE WHEN f.convenio IS NOT NULL THEN getnombreconvenio(f.convenio) ELSE '' END AS nombreConvenio, "+
															"coalesce(f.via_ingreso, "+ConstantesBD.codigoNuncaValido+") as codigoViaIngreso, " +
															"CASE WHEN f.via_ingreso IS NOT NULL THEN getnombreviaingreso(f.via_ingreso) ELSE '' END as nombreViaIngreso, " +
															"coalesce(f.monto_cobro, "+ConstantesBD.codigoNuncaValido+") AS codigoMontoCobro, "+
															"coalesce(f.sub_cuenta, "+ConstantesBD.codigoNuncaValido+") AS subCuenta, "+
															"f.factura_cerrada AS facturaCerrada, " +
															"coalesce(f.valor_monto, "+ConstantesBD.codigoNuncaValido+") AS valorMonto, " +
															"coalesce(f.porcentaje_monto, "+ConstantesBD.codigoNuncaValido+") AS porcentajeMonto, " +
															"coalesce(f.tipo_afiliado, '') as tipoAfiliado, " +
															"coalesce(f.estrato_social, "+ConstantesBD.codigoNuncaValido+") AS codigoEstrato, " +
															"CASE WHEN f.estrato_social IS NOT NULL THEN getnombreestrato(f.estrato_social) ELSE '' END AS nombreEstrato, "+
															"coalesce(f.tipo_monto, "+ConstantesBD.codigoNuncaValido+") AS codigoTipoMonto, " +
															"coalesce(f.cuenta_cobro_capitacion, "+ConstantesBD.codigoNuncaValido+") AS cuentaCobroCapitacion, "+
															"coalesce(f.centro_aten, "+ConstantesBD.codigoNuncaValido+") AS codigoCentroAtencion, "+
															"CASE WHEN f.centro_aten IS NOT NULL THEN getnomcentroatencion(f.centro_aten) ELSE '' END AS nombreCentroAtencion, "+
															"coalesce(f.tipo_comprobante, '') AS tipoComprobante, "+
															"coalesce(f.nro_comprobante, "+ConstantesBD.codigoNuncaValido+") as numeroComprobante, " +
															"coalesce(f.valor_favor_convenio, "+ConstantesBD.codigoNuncaValido+") as valorFavorConvenio, " +
															"coalesce(f.resolucion, '') as resolucionDian, " +
															"coalesce(f.rgo_inic_fact, "+ConstantesBD.codigoNuncaValido+" ) as rangoInicial, " +
															"coalesce(f.rgo_fin_fact, "+ConstantesBD.codigoNuncaValido+" ) as rangoFinal, " +
															"coalesce(f.entidad_subcontratada, "+ConstantesBD.codigoNuncaValido+") as entidadsubcontratada, " +
															"coalesce(f.empresa_institucion, "+ConstantesBD.codigoNuncaValido+") as empresainstitucion, " +
															"coalesce(f.pac_entidad_subcontratada, "+ConstantesBD.codigoNuncaValido+") as pacienteentidadsubcontratada, " +
															"coalesce(f.tope_facturacion, "+ConstantesBD.codigoNuncaValido+") as topefacturacion, "+
															"CASE WHEN f.vigencia_ini_monto_c is null then '' else to_char(f.vigencia_ini_monto_c, 'dd/mm/yyyy')||'' end  as fechaviginimontocobro, "+
															"CASE WHEN f.vigencia_ini_tope_f is null then '' else to_char(f.vigencia_ini_tope_f, 'dd/mm/yyyy')||'' end  as fechaviginitopefact, "+
															"coalesce(f.centro_atencion_duenio, "+ConstantesBD.codigoNuncaValido+") AS centroatencionduenio, "+
															"CASE WHEN f.centro_atencion_duenio IS NOT NULL THEN getnomcentroatencion(f.centro_atencion_duenio) ELSE '' END AS nombrecentroatencionduenio, " +
															"coalesce(f.valor_anticipo,0) as valoranticipo "+
														"FROM " +
															"facturas f " +
															"INNER JOIN estados_factura_f ef ON (f.estado_facturacion=ef.codigo) " +
															"INNER JOIN estados_factura_paciente efp ON (f.estado_paciente=efp.codigo) " +
														"WHERE f.codigo=? ";
	
	/**
	 * 
	 */
	private final static String cargarDetallesFacturaStr=	"SELECT " +
																"dfs.codigo as codigo, " +
																"coalesce(dfs.solicitud, "+ConstantesBD.codigoNuncaValido+") as solicitud, " +
																"dfs.factura as facturas, " +
																"dfs.ajuste_credito_medico as ajustescreditomedico, " +
																"dfs.ajuste_debito_medico as ajustedebitomedico, " +
																"coalesce(dfs.porcentaje_pool, "+ConstantesBD.codigoNuncaValido+") as porcentajepool, " +
																"coalesce(dfs.porcentaje_medico,"+ConstantesBD.codigoNuncaValido+") as porcentajemedico, " +
																"dfs.valor_medico as valormedico, " +
																"dfs.fecha_cargo as fechacargo, " +
																"dfs.cantidad_cargo as cantidadcargo, " +
																"dfs.valor_cargo as valorcargo, " +
																"dfs.valor_iva as valoriva, " +
																"dfs.valor_recargo as valorrecargo, " +
																"dfs.valor_total as valortotal, " +
																"coalesce(dfs.soli_pago, "+ConstantesBD.codigoNuncaValido+") as solicitudpago, " +
																"dfs.tipo_cargo as tipocargo, " +
																"coalesce(dfs.servicio, "+ConstantesBD.codigoNuncaValido+") as servicio, " +
																"coalesce(dfs.articulo, "+ConstantesBD.codigoNuncaValido+") as articulo, " +
																"dfs.ajustes_credito as ajustescredito, " +
																"dfs.ajustes_debito as ajustesdebito, " +
																"dfs.valor_pool as valorpool, " +
																"coalesce(dfs.pool, "+ConstantesBD.codigoNuncaValido+") as pool, " +
																"coalesce(dfs.codigo_medico, "+ConstantesBD.codigoNuncaValido+") as codigomedico, " +
																"coalesce(dfs.valor_dcto_comercial, "+ConstantesBD.codigoNuncaValido+") as valordctocomercial, " +
																"coalesce(dfs.valor_consumo_paquete, "+ConstantesBD.codigoNuncaValido+") as valorconsumopaquete, " +
																"s.tipo as tiposolicitud, " +
																//debemos actualizar historicos para quitar el colaesce
																"coalesce(dfs.esquema_tarifario, "+ConstantesBD.codigoNuncaValido+") as esquematarifario, " +
																"coalesce(dfs.programa, "+ConstantesBD.codigoNuncaValido+") as codprograma, " +
																"case when dfs.programa is not null then ( SELECT p.nombre from odontologia.programas p where p.codigo=dfs.programa ) else '' end as nomprograma, " +
																"coalesce(dfs.valor_dcto_odo, 0) as valor_dcto_odo, " +
																"coalesce(dfs.valor_dcto_bono, 0) as valor_dcto_bono, " +
																"coalesce(dfs.valor_dcto_prom, 0) as valor_dcto_prom, " +
																"coalesce(dfs.det_paq_odon_convenio,0) as det_paq_odon_convenio " +
															"FROM " +
																"det_factura_solicitud dfs " +
																"INNER JOIN solicitudes s ON (s.numero_solicitud=dfs.solicitud) " +
															"WHERE dfs.factura=? ";
	
	/**
	 * 
	 */
	private static final String cargarAsociosDetalleFacturaStr="SELECT a.consecutivo as consecutivo, " +
																	"a.codigo as codigo, " +
																	"a.servicio_asocio as servicio, " +
																	"a.valor_asocio as valorasocio, " +
																	"a.tipo_asocio as tipoasocio, " +
																	" getcodigopropservicio2(a.servicio_asocio, "+ConstantesBD.codigoTarifarioCups+")||' - '|| getnombreservicio(a.servicio_asocio, "+ConstantesBD.codigoTarifarioCups+") as nombretipoasocio, " +
																	//"getnombretipoasocio(a.tipo_asocio) as nombretipoasocio, " +
																	"a.ajuste_credito_medico as ajustecreditomedico, " +
																	"a.ajuste_debito_medico as ajustedebitomedico, " +
																	"a.porcentaje_pool as porcentajepool, " +
																	"a.porcentaje_medico as porcentajemedico, " +
																	"a.valor_medico as valormedico, " +
																	"a.valor_cargo as valorcargo, " +
																	"a.valor_iva as valoriva, " +
																	"a.valor_recargo as valorrecargo, " +
																	"a.valor_total as valortotal, " +
																	"a.ajustes_credito as ajustescredito, " +
																	"a.ajustes_debito as ajustesdebito, " +
																	"coalesce(a.esquema_tarifario, "+ConstantesBD.codigoNuncaValido+") as esquematarifario, " +
																	"a.valor_pool as valorpool, " +
																	"coalesce(a.pool, "+ConstantesBD.codigoNuncaValido+") as pool, " +
																	"coalesce(a.codigo_medico, "+ConstantesBD.codigoNuncaValido+") as codigomedico, " +
																	"coalesce(a.medico_asocio, "+ConstantesBD.codigoNuncaValido+") as codigomedicoasocio, " +
																	"coalesce(a.especialidad, "+ConstantesBD.codigoNuncaValido+") as codigoespecialidadasocio, " +
																	"ta.tipos_servicio as tiposervicio, "+
																	"a.codigo_propietario as codigopropietario "+
																"FROM " +
																	"asocios_det_factura a " +
																"INNER JOIN " +
																	"salascirugia.tipos_asocio ta ON(a.tipo_asocio=ta.codigo) " +
																"WHERE " +
																	"a.codigo=? ";
	
	/**
	 * 
	 */
	private static final String cargarDetalleAsociosStr= "SELECT " +
															"d.codigo as codigo , " +					//1
															"d.asocio_det_factura as asocio , " +		//2
															"d.articulo as articulo , " +				//3
															"d.cantidad as cantidad, " +				//4
															"d.valor_unitario as valorunitario, " +	//5
															"d.valor_total as valortotal, " +			//6
															"a.descripcion as descarticulo " +
														"from " +
															"facturacion.det_asocios_det_factura d " +
															"inner join inventarios.articulo a on (a.codigo=d.articulo) " +
														"where " +
															"asocio_det_factura=? ";//1			
															
	
	
	/**
	 * 
	 */
	private static final String cargarPaquetizacionDetfactStr="SELECT " +
																	"p.codigo as codigo, " +
																	"p.codigo_det_fact as codigodetfact, " +
																	"coalesce(p.servicio, "+ConstantesBD.codigoNuncaValido+") as servicio, " +
																	"coalesce(p.articulo, "+ConstantesBD.codigoNuncaValido+") as articulo, " +
																	"p.ajuste_credito_medico as ajustecreditomedico, " +
																	"p.ajuste_debito_medico as ajustedebitomedico, " +
																	"p.valor_cargo as valorcargo, " +
																	"p.valor_iva as valoriva, " +
																	"p.valor_recargo as valorrecargo, " +
																	"p.valor_total as valortotal, " +
																	"p.valor_dif_consumo_valor as valordifconsumovalor, " +
																	"p.ajustes_credito as ajustescredito, " +
																	"p.ajustes_debito as ajustesdebito, " +
																	"coalesce(p.porcentaje_medico, 0) as porcentajemedico, " +
																	"coalesce(p.porcentaje_pool, 0) as porcentajepool, " +
																	"p.solicitud as numerosolicitud, " +
																	"coalesce(p.esquema_tarifario, "+ConstantesBD.codigoNuncaValido+") as esquematarifario, " +
																	"coalesce(p.especialidad_asocio, "+ConstantesBD.codigoNuncaValido+") as especialidadasocio, " +
																	"coalesce(p.medico_asocio, "+ConstantesBD.codigoNuncaValido+") as medicoasocio " +
																"from " +
																	"paquetizacion_det_factura p " +
																"where " +
																	"p.codigo_det_fact=?";

	
	/**
	 * 
	 * @param con
	 * @param idIngreso
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static HashMap obtenerSubCuentasAFacturar(Connection con, String idIngreso)
	{
		HashMap mapa= new HashMap();
		PreparedStatement pst=null;
		ResultSet rs=null;
		try
		{
			String consulta=	"SELECT " +
									"sc.convenio AS codigoconvenio, " +
									"CASE WHEN c.tipo_regimen<>'"+ConstantesBD.codigoTipoRegimenParticular+"' then getNomConvContrato(sc.contrato) ||' - PRIORIDAD '||sc.nro_prioridad else getNomDeudorIngreso("+idIngreso+") ||' '|| getIdentificacionDeudorIngreso("+idIngreso+") end AS nombreresponsable, " +
									"coalesce(getFacturasSubcuenta(sc.sub_cuenta, ' - '),'') as facturagenerada, " +
									"'"+ConstantesBD.acronimoSi+"' as puedofacturar, " +
									"'"+ConstantesBD.acronimoNo+"' as seleccionado,  " +
									"CASE WHEN c.tipo_regimen<>'"+ConstantesBD.codigoTipoRegimenParticular+"' then '"+ConstantesBD.acronimoNo+"' else '"+ConstantesBD.acronimoSi+"' end as  esparticular, " +
									"coalesce(c.info_adic_ingreso_convenios, "+ValoresPorDefecto.getValorFalseParaConsultas()+") as validarinfovenezuela, " +
									"case when (c.info_adic_ingreso_convenios is not null and c.info_adic_ingreso_convenios="+ValoresPorDefecto.getValorTrueParaConsultas()+") then getValorMontoAutorizado(sc.sub_cuenta) else "+ConstantesBD.codigoNuncaValido+" end as valormontoautorizadovenezuela, " +
									"c.tipo_regimen as tiporegimen, " +
									"getnomtiporegimen(c.tipo_regimen) as nombretiporegimen, " +
									"'"+ConstantesBD.acronimoNo+"' as  pactieneexcepcionnaturaleza, " +
									" "+ConstantesBD.codigoNuncaValidoDoubleNegativo+" as valorcargototal, " +
									" "+ConstantesBD.codigoNuncaValidoDoubleNegativo+" as valorliquidadopaciente, " +
									" "+ConstantesBD.codigoNuncaValidoDoubleNegativo+" as valorbrutopaciente, " +
									" "+ConstantesBD.codigoNuncaValidoDoubleNegativo+" as valorconvenio, " +
									"sc.monto_cobro as montocobro, " +
									"coalesce(sc.clasificacion_socioeconomica, "+ConstantesBD.codigoNuncaValido+") as clasificacionsocioeconomica, " +
									
									"coalesce(dm.tipo_monto_codigo, "+ConstantesBD.codigoNuncaValido+
									
									
									") as tipomonto, " +
									"coalesce(tm.nombre,'') as nombretipomonto, " +
									"coalesce(dm.tipo_afiliado_codigo,'') as tipoafiliado, " +
									
									"getnombreestrato(sc.clasificacion_socioeconomica) as nomclassocioeconomica, " +
									"sc.sub_cuenta as subcuenta, " +
									"coalesce(c.formato_factura,0) as codigoformatoimpresion, " +
									"getnombreformatofactura (coalesce(c.formato_factura, 0)) as nombreformatoimpresion, " +
									"to_char(mc.vigencia_inicial, 'DD/MM/YYYY') as fechavigenciamontocobro ," +
									//informacion de multiempresa
									"coalesce(c.empresa_institucion, "+ConstantesBD.codigoNuncaValido+") as empresainstitucion, " +
									"coalesce(i.pac_entidades_subcontratadas, "+ConstantesBD.codigoNuncaValido+") as pacienteentidadsubcontratada, " +
									"CASE WHEN i.pac_entidades_subcontratadas IS NULL THEN "+ConstantesBD.codigoNuncaValido+" ELSE (SELECT pes.entidad_subcontratada FROM pac_entidades_subcontratadas pes WHERE pes.consecutivo=i.pac_entidades_subcontratadas) END AS entidadsubcontratada, " +
									"CASE WHEN c.tipo_contrato = "+ConstantesBD.codigoTipoContratoCapitado+" THEN '"+ConstantesBD.acronimoSi+"' ELSE '"+ConstantesBD.acronimoNo+"' END AS escapitado, " +
									"sc.contrato as contrato, " +
									"p.centro_atencion_duenio as centroatencionduenio " +
								"FROM " +
									"sub_cuentas sc " +
									"INNER JOIN convenios c ON (c.codigo=sc.convenio) " +
									"INNER JOIN ingresos i ON (i.id=sc.ingreso) " +
									"INNER JOIN manejopaciente.pacientes p on(p.codigo_paciente=i.codigo_paciente) " +
									"left outer join facturacion.detalle_monto dm on (sc.monto_cobro=dm.detalle_codigo) " +
									"left outer join facturacion.montos_cobro mc on (mc.codigo=dm.monto_codigo) "+
									"left outer join tipos_monto tm on (tm.codigo=dm.tipo_monto_codigo) " +
								"WHERE " +
									"sc.ingreso=? ";
			
			pst = con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.setInt(1, Utilidades.convertirAEntero(idIngreso));
			rs=pst.executeQuery();
			int cont=0;
			mapa.put("numRegistros","0");
			ResultSetMetaData rsm=rs.getMetaData();
			while(rs.next())
			{
				for(int i=1;i<=rsm.getColumnCount();i++)
				{
					mapa.put((rsm.getColumnLabel(i)).toLowerCase()+"_"+cont, rs.getObject(rsm.getColumnLabel(i))==null||rs.getObject(rsm.getColumnLabel(i)).toString().equals(" ")?"":rs.getObject(rsm.getColumnLabel(i)));
				}
				cont++;
			}
			mapa.put("numRegistros", cont+"");
			
			int numReg=Utilidades.convertirAEntero(mapa.get("numRegistros").toString());

			if(numReg==1)
				mapa.put("seleccionado_0", ConstantesBD.acronimoSi);
			//tenemos que colocar el indicativo de puedo facturar en No cuando validarinfovenezuela=true y valormontoautorizadovenezuela<1
			for(int w=0; w<numReg; w++)
			{
				if(UtilidadTexto.getBoolean(mapa.get("validarinfovenezuela_"+w).toString()))
				{
					double saldo= Double.parseDouble(mapa.get("valormontoautorizadovenezuela_"+w)+"");
					if(saldo<1)
					{
						mapa.put("puedofacturar_"+w, ConstantesBD.acronimoNo);
						
					}
				}
			}
		}
		catch(SQLException sqe){
			logger.error("############## SQLException ERROR obtenerSubCuentasAFacturar",sqe);
		}
		catch(Exception e){
			logger.error("############## ERROR obtenerSubCuentasAFacturar", e);
		}
		finally{
			try{
				if(rs != null){
					rs.close();
				}
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException se) {
				logger.error("###########  Error close ResultSet - PreparedStatement", se);
			}
		}
		return mapa;
	}	
	
	/**
	 * metodo para obtener el cargo total a facturar x responsable
	 * @param con
	 * @param cuentas
	 * @param subcuenta
	 * @return
	 */
	public static double obtenerValorCargoTotalAFacturarXSubCuenta(Connection con, Vector cuentas, double subCuenta)
	{
		PreparedStatement pst=null;
		ResultSet rs=null;
		try
		{
			String consulta="SELECT " +
					"SUM((coalesce(valor_unitario_cargado,0) + coalesce(valor_unitario_recargo,0) - coalesce(valor_unitario_dcto,0)) * coalesce(cantidad_cargada,0))  as total " +
				"FROM " +
					"solicitudes_subcuenta ssc " +
					"INNER JOIN det_cargos dc ON (dc.cod_sol_subcuenta=ssc.codigo) " +
				"WHERE " +
					"dc.sub_cuenta ="+(long)subCuenta+" " +
					"AND ssc.cuenta in("+UtilidadTexto.convertirVectorACodigosSeparadosXComas(cuentas, false)+") "+
					"AND dc.estado="+ConstantesBD.codigoEstadoFCargada+" " +
					//solo debe tomar el padre de los paquetizados
					"AND dc.paquetizado='"+ConstantesBD.acronimoNo+"' "+
					"AND dc.facturado='"+ConstantesBD.acronimoNo+"' " +
					"AND ssc.eliminado='"+ConstantesBD.acronimoNo+"'";
			pst= con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			rs=pst.executeQuery();
			double total=0;
			while(rs.next()){
				total+=rs.getDouble("total");
			}
			return total;
		}
		catch(SQLException sqe){
			logger.error("############## SQLException ERROR obtenerValorCargoTotalAFacturarXSubCuenta",sqe);
		}
		catch(Exception e){
			logger.error("############## ERROR obtenerValorCargoTotalAFacturarXSubCuenta", e);
		}
		finally{
			try{
				if(rs != null){
					rs.close();
				}
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException se) {
				logger.error("###########  Error close ResultSet - PreparedStatement", se);
			}
		}
		return ConstantesBD.codigoNuncaValidoDoubleNegativo;
	}
	
	/**
	 * 
	 * @param con
	 * @param dtoFactura
	 * @return
	 */
	public static ArrayList<DtoDetalleFactura> proponerDetalleFacturaDesdeCargos(Connection con, DtoFactura dtoFactura, int estadoCargo)
	{
		ArrayList<DtoDetalleFactura> detalleArray= new ArrayList<DtoDetalleFactura>();
		//dtoFactura.getConvenio().getCodigo()
		//por el momento vamos a cargar los detalles de factura con el numero de solicitud
		String consulta="SELECT " +
							"dc.codigo_detalle_cargo as codigodetallecargo, " +
							"dc.solicitud as solicitud, " +
							"coalesce(getDiagnosticoPrinVal(dc.solicitud),'') as dx, " +
							"(coalesce(dc.valor_unitario_cargado,0) + coalesce(dc.valor_unitario_recargo,0) - coalesce(dc.valor_unitario_dcto,0)) * coalesce(dc.cantidad_cargada,0) as valortotal, " +
							"coalesce(s.pool, "+ConstantesBD.codigoNuncaValido+") as pool, " +
							"coalesce(" +
										"facturacion.getPorcentMedPoolXSol(" +
																"ssc.solicitud, " +
																""+ConstantesBD.codigoNuncaValido+", " +
																""+ConstantesBD.codigoNuncaValido+", " +
																""+ConstantesBD.codigoTipoSolicitudCirugia+"," +
																""+ConstantesBD.codigoNuncaValido+", " +
																""+ConstantesBD.codigoNuncaValido+" " +
															  ")" +
									  ",0) as porcentajemedicopool, " +
							"coalesce(dc.fecha_modifica||'', CURRENT_DATE||'') as fechacargo, " +
							"dc.cantidad_cargada as cantidadcargo, " +
							"dc.valor_unitario_cargado as valorcargo, " +
							//"coalesce(dc.valor_unitario_iva, 0) as valoriva, " +
							//xplanner [id=27724]
							"0 as valoriva, " +
							"coalesce(dc.valor_unitario_recargo, 0) as valorrecargo, " +
							"case when dc.articulo is null then "+ConstantesBD.codigoTipoCargoServicios+" else "+ConstantesBD.codigoTipoCargoArticulos+" end as tipocargo, " +
							"coalesce(dc.servicio, "+ConstantesBD.codigoNuncaValido+") as codigoservicio, " +
							"coalesce(dc.articulo, "+ConstantesBD.codigoNuncaValido+") as codigoarticulo, " +
							"s.codigo_medico_responde as codigomedico, " +
							"'"+ConstantesBD.acronimoNo+"' as escx, " +
							"coalesce(dc.valor_unitario_dcto, 0) as valordescuentocomercial, " +
							"CASE WHEN s.tipo="+ConstantesBD.codigoTipoSolicitudPaquetes+" THEN  coalesce(getValorConsumoPaquete(dc.codigo_detalle_cargo, dc.sub_cuenta),0) ELSE "+ConstantesBD.codigoNuncaValido+" END AS  valorconsumopaquete, " +
							"s.tipo as tiposolicitud, " +
							"s.estado_historia_clinica as codigoestadohc, " +
							"dc.esquema_tarifario as esquematarifario, " +
							"coalesce(dc.programa,"+ConstantesBD.codigoNuncaValido+") as programa, " +
							"coalesce(dc.det_paq_odon_convenio,0) as det_paq_odon_convenio " +
						"FROM " +
							"solicitudes_subcuenta ssc " +
							"INNER JOIN sub_cuentas sc ON (sc.sub_cuenta=ssc.sub_cuenta) " +
							"INNER JOIN det_cargos dc on(dc.cod_sol_subcuenta=ssc.codigo) " +
							"INNER JOIN solicitudes s ON (ssc.solicitud=s.numero_solicitud) " +
						"WHERE " +
							"dc.sub_cuenta="+(long)dtoFactura.getSubCuenta()+" " +
							"AND ssc.cuenta in("+UtilidadTexto.convertirVectorACodigosSeparadosXComas(dtoFactura.getCuentas(), false)+") " +
							"AND ssc.eliminado='"+ConstantesBD.acronimoNo+"' "+
							"AND dc.estado="+estadoCargo+" " +
							"AND dc.facturado='"+ConstantesBD.acronimoNo+"' " +
							"AND dc.paquetizado='"+ConstantesBD.acronimoNo+"' " + ///con esto solo cargamos el encabezado de ese paquete
							"AND dc.servicio_cx is null " +
							"AND dc.cantidad_cargada>0 ";
		
		//TENEMOS QUE UNIRLE LA SUMA DE TODOS LOS ASOCIO DE LAS CX, EL ENCABEZADO DE LA CIRUGIA
		consulta+= 	"UNION "+
					"SELECT " +
						//se coloca como codigo nunca valido para que pueda funcionar el distinct,
						//ademans solo se requiere el codigo detalle de cargo para el caso de las solicitudes tipo paquete
						"DISTINCT "+ConstantesBD.codigoNuncaValido+" as codigodetallecargo, " +
						"dc.solicitud as solicitud, " +
						"'' as dx, " +
						"getValorCxCompleta(ssc.solicitud, dc.sub_cuenta, dc.servicio_cx, '"+ConstantesBD.acronimoNo+"', '"+ConstantesBD.acronimoNo+"',"+estadoCargo+") as valortotal, " +
						"coalesce(s.pool, "+ConstantesBD.codigoNuncaValido+") as pool, " +
						"0 as porcentajemedicopool, " +
						"coalesce(s.fecha_interpretacion||'', CURRENT_DATE||'') as fechacargo, " +
						"coalesce(dc.cantidad_cargada, 0) as cantidadcargo, " +
						"getValorCxCargado(ssc.solicitud, dc.sub_cuenta, dc.servicio_cx, '"+ConstantesBD.acronimoNo+"', '"+ConstantesBD.acronimoNo+"',"+estadoCargo+") as valorcargo, " +
						"0 as valoriva, " +
						"getValorCxRecargo(ssc.solicitud, dc.sub_cuenta, dc.servicio_cx, '"+ConstantesBD.acronimoNo+"', '"+ConstantesBD.acronimoNo+"',"+estadoCargo+") as valorrecargo, " +
						""+ConstantesBD.codigoTipoCargoServicios+" as tipocargo, " +
						"dc.servicio_cx as codigoservicio, " +
						""+ConstantesBD.codigoNuncaValido+" as codigoarticulo, " +
						"s.codigo_medico_responde as codigomedico, " +
						"'"+ConstantesBD.acronimoSi+"' as escx, " +
						"0 as valordescuentocomercial, " +
						""+ConstantesBD.codigoNuncaValidoDoubleNegativo+" as valorconsumopaquete,"+" "+
						"s.tipo as tiposolicitud, " +
						"s.estado_historia_clinica as codigoestadohc, " +
						"getesqtarcirugia(dc.solicitud, dc.servicio_cx) as esquematarifario, " +
						""+ConstantesBD.codigoNuncaValido+" as programa, " +
						"0 as det_paq_odon_convenio " +
					"FROM " +
						"solicitudes_subcuenta ssc " +
						"INNER JOIN sub_cuentas sc ON (sc.sub_cuenta=ssc.sub_cuenta) " +
						"INNER JOIN det_cargos dc on(dc.cod_sol_subcuenta=ssc.codigo) " +
						"INNER JOIN solicitudes s ON (ssc.solicitud=s.numero_solicitud) " +
					"WHERE " +
						"dc.sub_cuenta="+(long)dtoFactura.getSubCuenta()+" " +
						"AND ssc.cuenta in("+UtilidadTexto.convertirVectorACodigosSeparadosXComas(dtoFactura.getCuentas(), false)+") " +
						"AND ssc.eliminado='"+ConstantesBD.acronimoNo+"' "+
						"AND dc.estado="+estadoCargo+" " +
						"AND dc.facturado='"+ConstantesBD.acronimoNo+"' " +
						"AND dc.paquetizado='"+ConstantesBD.acronimoNo+"' " + ///con esto solo cargamos el encabezado de ese paquete
						"AND dc.servicio_cx is not null "+	
						"AND dc.articulo is null "+ //no tener encuenta los materiales especiales
						"AND dc.cantidad_cargada>0 ";
						 
		
		try
		{  
			PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			logger.info("llega a proponerDetalleFacturaDesdeCargos---------->"+consulta);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			{	
				double codigoDetalleCargo= rs.getDouble("codigodetallecargo");
				
				DtoDetalleFactura detalleDto= new DtoDetalleFactura();
				detalleDto.setCodigoDetalleCargo(codigoDetalleCargo);
				detalleDto.setNumeroSolicitud(rs.getInt("solicitud"));
				detalleDto.setCodigoFactura(ConstantesBD.codigoNuncaValido);
				detalleDto.setAjusteCreditoMedico(0);
				detalleDto.setAjusteDebitoMedico(0);
				detalleDto.setCodigoArticulo(rs.getInt("codigoarticulo"));
				detalleDto.setCodigoServicio(rs.getInt("codigoservicio"));
				
				detalleDto.setCodigoTipoSolicitud(rs.getInt("tiposolicitud"));
				detalleDto.setCodigoEstadoHC(rs.getInt("codigoestadohc"));
				detalleDto.setPool(rs.getInt("pool"));
				detalleDto.setPrograma(new InfoDatosDouble(rs.getDouble("programa"), ""));
				
				logger.info("%%%%%%%%%%%%INFORMACION DE POOLESSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
				logger.info("1 porcentaje->"+detalleDto.getPorcentajePool());
				logger.info("1 valor POOL->"+detalleDto.getValorPool());
				logger.info("poool->"+detalleDto.getPool());
				
				if(UtilidadTexto.getBoolean(ValoresPorDefecto.getValoresDefectoValidarPoolesFact(dtoFactura.getInstitucion())))
				{
					//no se debe calcular para las solicitudes de cx y pooles, estas solamente se hacen a nivel de asocios - componente
					if(	detalleDto.getPool()<=0
						|| detalleDto.getCodigoTipoSolicitud()==ConstantesBD.codigoTipoSolicitudPaquetes 
						|| detalleDto.getCodigoTipoSolicitud()==ConstantesBD.codigoTipoSolicitudCirugia
						|| detalleDto.getCodigoTipoSolicitud()==ConstantesBD.codigoTipoSolicitudEstancia
						|| detalleDto.getCodigoArticulo()>0)//con esta evaluamos sol medicamentos y cargos dir med 
					{
						detalleDto.setPorcentajePool(ConstantesBD.codigoNuncaValido);
						detalleDto.setValorPool(ConstantesBD.codigoNuncaValido);
					}
					else
					{	
						InfoPorcentajeValor honorario= CalculoHonorariosPooles.obtenerHonorarioPoolDetalleFacturaNoCx(codigoDetalleCargo);
						
						if(honorario!=null)
						{
							detalleDto.setValorPool(honorario.getValor().doubleValue());
							detalleDto.setPorcentajePool(honorario.getPorcentaje());
							
							logger.info("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
							logger.info("EL VALOR DEL POOL SETEADO ES-->"+detalleDto.getValorPool());
							logger.info("EL PORCENTAJE DEL POOL SETEADO ES-->"+detalleDto.getPorcentajePool());
							logger.info("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
						}
					}	
				}	

				logger.info("2 porcentaje->"+detalleDto.getPorcentajePool());
				logger.info("2 valor->"+detalleDto.getValorPool());
				
				detalleDto.setPorcentajeMedico(rs.getDouble("porcentajemedicopool"));
				
				detalleDto.setDiagnosticoAcronimoTipoCie(rs.getString("dx"));
				detalleDto.setValorTotal(rs.getDouble("valortotal"));
				
				double valorPool=ConstantesBD.codigoNuncaValido, valorMedico=ConstantesBD.codigoNuncaValido; 
				try
				{
					if(detalleDto.getPorcentajePool()>=0)
					{	
						valorPool=Math.rint((detalleDto.getValorTotal()*detalleDto.getPorcentajePool())/100);
						valorMedico=(valorPool*detalleDto.getPorcentajeMedico())/100;
					}
					else if(detalleDto.getValorPool()>=0)
					{
						valorPool= detalleDto.getValorPool();
						valorMedico=(valorPool*detalleDto.getPorcentajeMedico())/100;
					}
				}	
				catch (Exception e) 
				{
					valorMedico=ConstantesBD.codigoNuncaValido;
					valorPool=ConstantesBD.codigoNuncaValido;
				}	

				logger.info("3 porcentaje->"+detalleDto.getPorcentajePool());
				logger.info("3 valor->"+detalleDto.getValorPool());
				
				detalleDto.setValorMedico(valorMedico);
				detalleDto.setValorPool(valorPool);
				detalleDto.setFechaCargo(rs.getString("fechacargo"));
				detalleDto.setCantidadCargo(rs.getInt("cantidadcargo"));
				detalleDto.setValorCargo(rs.getDouble("valorcargo"));
				detalleDto.setValorIva(rs.getDouble("valoriva"));
				detalleDto.setValorRecargo(rs.getDouble("valorrecargo"));
				detalleDto.setSolicitudPago(ConstantesBD.codigoNuncaValido);
				detalleDto.setCodigoTipoCargo(rs.getInt("tipocargo"));
				detalleDto.setAjustesCredito(0);
				detalleDto.setAjustesDebito(0);
				detalleDto.setCodigoMedico(rs.getInt("codigomedico"));
				detalleDto.setEsCx(UtilidadTexto.getBoolean(rs.getString("escx")));
				detalleDto.setValorDescuentoComercial(rs.getDouble("valordescuentocomercial"));
				detalleDto.setValorConsumoPaquete(rs.getDouble("valorconsumopaquete"));
				detalleDto.setCodigoEsquemaTarifario(rs.getInt("esquematarifario"));
				detalleDto.setDetallePaqueteOdonConvenio(rs.getInt("det_paq_odon_convenio"));
				
				logger.info("detalleDto.getEsCx()------->"+detalleDto.getEsCx());
				if(detalleDto.getEsCx())
				{
					detalleDto.setAsociosDetalleFactura(proponerAsociosDetalleFacturaDesdeCargos(con, dtoFactura, detalleDto,estadoCargo));
				}
				
				if(detalleDto.getCodigoTipoSolicitud()==ConstantesBD.codigoTipoSolicitudPaquetes)
				{
					detalleDto.setPaquetizacionDetalleFactura(proponerPaquetizacionDetalleFacturaDesdeCargos(con, dtoFactura, detalleDto, codigoDetalleCargo));
				}
				
				detalleArray.add(detalleDto);
				
			}	
		}
		catch(SQLException e)
		{
			logger.warn(e+" Error en proponerDetalleFacturaDesdeCargos ");
			throw new RuntimeException(e);
		}
		return detalleArray;
	}
	
	/**
	 * 
	 * @param con
	 * @param dtoFactura
	 * @return
	 */
	public static ArrayList<DtoAsociosDetalleFactura> proponerAsociosDetalleFacturaDesdeCargos(Connection con, DtoFactura dtoFactura, DtoDetalleFactura dtoDetalleFactura, int estadoCargo )
	{
		ArrayList<DtoAsociosDetalleFactura> detalleAsociosArray= new ArrayList<DtoAsociosDetalleFactura>();
		String consulta= 	"SELECT " +
								"dc.servicio as servicioasocio, " +
								"( coalesce(dc.valor_unitario_cargado,0) + coalesce(dc.valor_unitario_recargo,0) - coalesce(dc.valor_unitario_dcto,0)) * coalesce(dc.cantidad_cargada,0) as valorasocio, "+
								"dc.tipo_asocio as tipoasocio, " +
								"getnombretipoasocio(dc.tipo_asocio) as nombretipoasocio, " +
								"getporcentmedpoolxsol(dc.solicitud, da.cod_sol_cx_servicio, dc.servicio, "+ConstantesBD.codigoTipoSolicitudCirugia+",dc.tipo_asocio,da.medico) as porcentajemedico, " +
								"dc.valor_unitario_cargado as valorcargo, " +
								//"coalesce(dc.valor_unitario_iva, 0) as valoriva, " +
								//xplanner [id=27724]
								"0 as valoriva, " +
								"coalesce(dc.valor_unitario_recargo, 0) as valorrecargo, " +
								"(coalesce(dc.valor_unitario_cargado,0) + coalesce(dc.valor_unitario_recargo,0) - coalesce(dc.valor_unitario_dcto,0)) * coalesce(dc.cantidad_cargada,1) as valortotal, " +
								"getesqtarcirugia(dc.solicitud, dc.servicio_cx) as esquematarifario, " +
								"coalesce(getpoolxsolicitud(dc.solicitud, da.cod_sol_cx_servicio, dc.servicio, "+ConstantesBD.codigoTipoSolicitudCirugia+",dc.tipo_asocio,da.medico), "+ConstantesBD.codigoNuncaValido+") as pool, " +
								"coalesce(s.codigo_medico_interpretacion, "+ConstantesBD.codigoNuncaValido+") as codigomedico, " +
								"dc.codigo_detalle_cargo as codigodetallecargo, " +
								"coalesce(da.medico, "+ConstantesBD.codigoNuncaValido+") as codigomedicoasocio,  " +//puede ser nulo para materiales
								"coalesce(da.especialidad_medico, "+ConstantesBD.codigoNuncaValido+") as especialidadmedicoasocio, " +//puede ser nulo para materiales
								"cc.centro_atencion as centro_atencion,  " +
								"coalesce(da.medico, "+ConstantesBD.codigoNuncaValido+") as medicoresponde "+ 
							"FROM " +
								"det_cargos dc " +
								"INNER JOIN solicitudes_subcuenta ssc on(dc.cod_sol_subcuenta=ssc.codigo) " +
								"INNER JOIN solicitudes s ON (ssc.solicitud=s.numero_solicitud) " +
								"INNER JOIN administracion.centros_costo cc ON (cc.codigo=s.centro_costo_solicitado) " +
								"LEFT OUTER JOIN det_cx_honorarios da ON(dc.det_cx_honorarios=da.codigo) " +
							"WHERE " +
								"dc.servicio_cx="+dtoDetalleFactura.getCodigoServicio() +" "+
								"AND dc.sub_cuenta="+(long)dtoFactura.getSubCuenta()+" " +
								"AND dc.solicitud = "+dtoDetalleFactura.getNumeroSolicitud()+" "+
								"AND ssc.cuenta in("+UtilidadTexto.convertirVectorACodigosSeparadosXComas(dtoFactura.getCuentas(), false)+") " +
								"AND ssc.eliminado='"+ConstantesBD.acronimoNo+"' "+
								"AND dc.estado="+estadoCargo+" " +
								"AND dc.facturado='"+ConstantesBD.acronimoNo+"' " +
								"AND dc.paquetizado='"+ConstantesBD.acronimoNo+"' " + ///con esto solo cargamos el encabezado de ese paquete
								"AND dc.servicio is not null "+
								"AND dc.cantidad_cargada>0 ";
		try
		{
			PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			logger.info("llega a proponerAsociosDetalleFacturaDesdeCargos---------->"+consulta);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			{	
				DtoAsociosDetalleFactura detalleDto= new DtoAsociosDetalleFactura();
				detalleDto.setCodigoServicioAsocio(rs.getInt("servicioasocio"));
				detalleDto.setValorAsocio(rs.getDouble("valorasocio"));
				detalleDto.setTipoAsocio(rs.getInt("tipoasocio"));
				detalleDto.setNombreTipoAsocio(rs.getString("nombretipoasocio"));
				detalleDto.setAjusteCreditoMedico(0);
				detalleDto.setAjusteDebitoMedico(0);
				detalleDto.setPool(rs.getInt("pool"));
				detalleDto.setCodigoEsquemaTarifario(rs.getInt("esquematarifario"));
				detalleDto.setCodigoEspecialidadMedicoAsocio(rs.getInt("especialidadmedicoasocio"));
				
				logger.info("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
				logger.info("%-------->"+detalleDto.getPorcentajePool());
				logger.info("valor-------->"+detalleDto.getValorPool());
				logger.info("pool-------->"+detalleDto.getPool());
				logger.info("solicitud-->"+dtoDetalleFactura.getNumeroSolicitud());
				logger.info("servicio_cx-->"+dtoDetalleFactura.getCodigoServicio());
				logger.info("valor-------->"+detalleDto.getValorPool());
				logger.info("servicio asocio-->"+detalleDto.getCodigoServicioAsocio());
				
				if(UtilidadTexto.getBoolean(ValoresPorDefecto.getValoresDefectoValidarPoolesFact(dtoFactura.getInstitucion())))
				{
					if(detalleDto.getPool()>0)
					{	
						////////////////calculo de pooles 
						//InfoPorcentajeValor honorario= CalculoHonorariosPooles.obtenerHonorarioPoolDetalleFacturaAsociosCx(detalleDto.getCodigoServicioAsocio(), detalleDto.getPool(), dtoFactura.getConvenio().getCodigo(), detalleDto.getCodigoEsquemaTarifario(), detalleDto.getCodigoEspecialidadMedicoAsocio(), rs.getInt("centro_atencion"), rs.getInt("medicoresponde"));
						//tarea 184317
						InfoPorcentajeValor honorario= CalculoHonorariosPooles.obtenerHonorarioPoolDetalleFacturaAsociosCx(dtoDetalleFactura.getCodigoServicio(), detalleDto.getPool(), dtoFactura.getConvenio().getCodigo(), detalleDto.getCodigoEsquemaTarifario(), detalleDto.getCodigoEspecialidadMedicoAsocio(), rs.getInt("centro_atencion"), rs.getInt("medicoresponde"));
						
						if(honorario!=null)
						{
							detalleDto.setValorPool(honorario.getValor().doubleValue());
							detalleDto.setPorcentajePool(new BigDecimal(honorario.getPorcentaje()).intValue());
							detalleDto.setPorcentajePoolDouble(honorario.getPorcentaje()); 
							
							logger.info("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
							logger.info("EL VALOR DEL POOL SETEADO ES-->"+detalleDto.getValorPool());
							logger.info("EL PORCENTAJE DEL POOL SETEADO ES-->"+detalleDto.getPorcentajePool());
							logger.info("EL PORCENTAJE DEL POOL (Double) SETEADO ES-->"+detalleDto.getPorcentajePoolDouble());
							logger.info("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
						}
					}
				}	
				
				logger.info("1-------->"+detalleDto.getPorcentajePool());
				logger.info("1-------->"+detalleDto.getValorPool());
				
				
				detalleDto.setPorcentajeMedico(rs.getDouble("porcentajemedico"));
				
				//CALCULO DEL VALOR MEDICO
				double valorPool=ConstantesBD.codigoNuncaValido, valorMedico=ConstantesBD.codigoNuncaValido; 
				try
				{
					if(detalleDto.getPorcentajePool()>=0)
					{
						valorPool=Math.rint((detalleDto.getValorAsocio()*detalleDto.getPorcentajePoolDouble())/100);
						valorMedico=(valorPool*detalleDto.getPorcentajeMedico())/100;
					}
					else if(detalleDto.getValorPool()>=0)
					{
						valorPool= detalleDto.getValorPool();
						valorMedico=(valorPool*detalleDto.getPorcentajeMedico())/100;
					}
				}	
				catch (Exception e) 
				{
					valorMedico=ConstantesBD.codigoNuncaValido;
					valorPool=ConstantesBD.codigoNuncaValido;
				}	
				
				logger.info("2-------->"+detalleDto.getPorcentajePool());
				logger.info("2-------->"+detalleDto.getValorPool());
				
				detalleDto.setValorMedico(valorMedico);
				detalleDto.setValorPool(valorPool);
				detalleDto.setValorCargo(rs.getDouble("valorcargo"));
				detalleDto.setValorIva(rs.getDouble("valoriva"));
				detalleDto.setValorRecargo(rs.getDouble("valorrecargo"));
				detalleDto.setValorTotal(rs.getDouble("valortotal"));
				detalleDto.setAjustesCredito(0);
				detalleDto.setAjustesDebito(0);
				detalleDto.setCodigoMedico(rs.getInt("codigomedico"));
				detalleDto.setCodigoDetalleCargo(rs.getDouble("codigodetallecargo"));
				detalleDto.setCodigoMedicoAsocio(rs.getInt("codigomedicoasocio"));
				detalleDto.setDetalleAsociosArray(proponerDetalleAsocios(con, rs.getDouble("codigodetallecargo")));
				detalleAsociosArray.add(detalleDto);
			}
		}	
		catch(SQLException e)
		{
			logger.warn(e+" Error en proponerDetalleFacturaDesdeCargos ");
			throw new RuntimeException(e);
		}
		
		return detalleAsociosArray; 
	}
	
	
	/**
	 * 
	 * @param con
	 * @param int1
	 * @return
	 */
	private static ArrayList<DtoDetAsociosDetFactura> proponerDetalleAsocios(Connection con, double codigoDetalleCargo) 
	{
		ArrayList<DtoDetAsociosDetFactura> array= new ArrayList<DtoDetAsociosDetFactura>();
		String consulta="SELECT " +
							"d.articulo as articulo, " +
							"d.cantidad as cantidad, " +
							"d.valor_unitario as valorunitario, " +
							"d.valor_total as valortotal " +
						"from " +
							"facturacion.det_cargos_art_consumo d " +
						"where " +
							"d.det_cargo=?";
	
		try
		{
			PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setLong(1, (long)codigoDetalleCargo);
			logger.info("llega a proponerDetalleAsocios---------->"+consulta);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			{	
				DtoDetAsociosDetFactura dto= new DtoDetAsociosDetFactura();
				dto.setArticulo(rs.getInt("articulo"));
				dto.setCantidad(rs.getInt("cantidad"));
				dto.setValorUnitario(rs.getDouble("valorunitario"));
				dto.setValorTotal(rs.getDouble("valortotal"));
				array.add(dto);
			}
			rs.close();
			ps.close();
		}	
		catch(SQLException e)
		{
			logger.warn(e+" Error en proponerDetalleFacturaDesdeCargos ");
			e.printStackTrace();
		}
		return array;
	}

	/**
	 * 
	 * @param con
	 * @param dtoFactura
	 * @param codigoDetalleCargo 
	 * @return
	 */
	public static ArrayList<DtoPaquetizacionDetalleFactura> proponerPaquetizacionDetalleFacturaDesdeCargos(Connection con, DtoFactura dtoFactura, DtoDetalleFactura dtoDetalleFactura, double codigoDetalleCargo )
	{
		ArrayList<DtoPaquetizacionDetalleFactura> detallePaquetizacionArray= new ArrayList<DtoPaquetizacionDetalleFactura>();
		
		//PRIMERO CARGAMOS LAS SOLICITUDES DE PAQUETES SIN CX-------------------
		String consulta= 	"SELECT " +
								"coalesce(dc.servicio, "+ConstantesBD.codigoNuncaValido+") as servicio, " +
								"coalesce(dc.articulo, "+ConstantesBD.codigoNuncaValido+") as articulo, " +
								"dc.valor_unitario_cargado as valorcargo, " +
								//"coalesce(dc.valor_unitario_iva, 0) as valoriva, " +
								//xplanner [id=27724]
								"0 as valoriva, " +
								"coalesce(dc.valor_unitario_recargo, 0) as valorrecargo, " +
								"(coalesce(dc.valor_unitario_cargado,0) + coalesce(dc.valor_unitario_recargo,0) - coalesce(dc.valor_unitario_dcto,0)) * coalesce(dc.cantidad_cargada,1) as valortotal, " +
								"coalesce(getPorcentMedPoolXSol(dc.solicitud, "+ConstantesBD.codigoNuncaValido+", "+ConstantesBD.codigoNuncaValido+", "+ConstantesBD.codigoTipoSolicitudCirugia+", "+ConstantesBD.codigoNuncaValido+", "+ConstantesBD.codigoNuncaValido+"),0)  as porcentajemedico, " +
								" "+ConstantesBD.codigoNuncaValido+" as serviciocx, " +
								" "+ConstantesBD.codigoNuncaValido+" as valorasocio, " +
								" "+ConstantesBD.codigoNuncaValido+" as tipoasocio, " +
								"s.pool as pool, " +
								"s.codigo_medico_responde as codigomedico, " +
								"dc.cantidad_cargada as cantidadcargo, " +
								"case when dc.articulo is null then "+ConstantesBD.codigoTipoCargoServicios+" else "+ConstantesBD.codigoTipoCargoArticulos+" end as tipocargo, " +
								"s.tipo as tiposolicitud, " +
								"s.estado_historia_clinica as codigoestadohc, " +
								"s.numero_solicitud as numerosolicitud, " +
								"dc.codigo_detalle_cargo as codigodetallecargo, " + 
								"dc.esquema_tarifario as esquematarifario, " +
								""+ConstantesBD.codigoNuncaValido+" as medicoasocio, " +
								""+ConstantesBD.codigoNuncaValido+" as especialidadasocio, " +
								"cc.centro_atencion as centro_atencion, " +
								"coalesce(s.codigo_medico_responde, "+ConstantesBD.codigoNuncaValido+") as medicoresponde "+
							"FROM " +
								"det_cargos dc " +
								"INNER JOIN solicitudes_subcuenta ssc on(dc.cod_sol_subcuenta=ssc.codigo) " +
								"INNER JOIN sub_cuentas sc ON (sc.sub_cuenta=ssc.sub_cuenta) " +
								"INNER JOIN solicitudes s ON (ssc.solicitud=s.numero_solicitud) " +
								"INNER JOIN administracion.centros_costo cc ON(cc.codigo=s.centro_costo_solicitado) "+
							"WHERE " +
								"dc.sub_cuenta="+(long)dtoFactura.getSubCuenta()+" " +
								"AND dc.cargo_padre = "+(long)codigoDetalleCargo+" "+
								"AND ssc.cuenta in("+UtilidadTexto.convertirVectorACodigosSeparadosXComas(dtoFactura.getCuentas(), false)+") " +
								"AND ssc.eliminado='"+ConstantesBD.acronimoNo+"' "+
								"AND dc.estado="+ConstantesBD.codigoEstadoFCargada+" " +
								"AND dc.facturado='"+ConstantesBD.acronimoNo+"' " +
								"AND dc.paquetizado='"+ConstantesBD.acronimoSi+"' "+///con esto solo cargamos el detalle del paquete
								"AND dc.servicio_cx IS NULL " +
								"AND dc.cantidad_cargada>0 ";
		
		//CARGAMOS LOS ASOCIOS DE CX DEL PAQUETE
		consulta+=			"UNION ALL " +
							"SELECT " +
								"coalesce(dc.servicio, "+ConstantesBD.codigoNuncaValido+") as servicio, " +
								" coalesce(dc.articulo, "+ConstantesBD.codigoNuncaValido+") as articulo, " +
								"dc.valor_unitario_cargado as valorcargo, " +
								//"coalesce(dc.valor_unitario_iva, 0) as valoriva, " +
								//xplanner [id=27724]
								"0 as valoriva, " +
								"coalesce(dc.valor_unitario_recargo, 0) as valorrecargo, " +
								"(coalesce(dc.valor_unitario_cargado,0) + coalesce(dc.valor_unitario_recargo,0) - coalesce(dc.valor_unitario_dcto,0)) * coalesce(dc.cantidad_cargada,1) as valortotal, " +
								"getporcentmedpoolxsol(dc.solicitud, da.cod_sol_cx_servicio, dc.servicio, "+ConstantesBD.codigoTipoSolicitudCirugia+",dc.tipo_asocio, da.medico) as porcentajemedico, " +
								" dc.servicio_cx as serviciocx, " +
								" dc.valor_total_cargado as valorasocio, " +
								" dc.tipo_asocio as tipoasocio, " +
								" getpoolxsolicitud(dc.solicitud, da.cod_sol_cx_servicio, dc.servicio, "+ConstantesBD.codigoTipoSolicitudCirugia+",dc.tipo_asocio, da.medico) as pool, "+
								" s.codigo_medico_responde as codigomedico, " +
								"coalesce(dc.cantidad_cargada,0) as cantidadcargo, " +
								"case when dc.articulo is null then "+ConstantesBD.codigoTipoCargoServicios+" else "+ConstantesBD.codigoTipoCargoArticulos+" end as tipocargo, " +
								"s.tipo as tiposolicitud, " +
								"s.estado_historia_clinica as codigoestadohc, " +
								"s.numero_solicitud as numerosolicitud, " +
								"dc.codigo_detalle_cargo as codigodetallecargo, " +
								"getesqtarcirugia(dc.solicitud, dc.servicio_cx) as esquematarifario, " +
								"coalesce(da.medico, "+ConstantesBD.codigoNuncaValido+") as medicoasocio,  " +//puede ser nulo para materiales
								"coalesce(da.especialidad_medico, "+ConstantesBD.codigoNuncaValido+") as especialidadasocio, " +//puede ser nulo para materiales
								"cc.centro_atencion as centro_atencion, " +
								"coalesce(da.medico, "+ConstantesBD.codigoNuncaValido+") as medicoresponde "+
							"FROM " +
								"det_cargos dc " +
								"INNER JOIN solicitudes_subcuenta ssc on(dc.cod_sol_subcuenta=ssc.codigo) " +
								"INNER JOIN sub_cuentas sc ON (sc.sub_cuenta=ssc.sub_cuenta) " +
								"INNER JOIN solicitudes s ON (ssc.solicitud=s.numero_solicitud) " +
								"INNER JOIN administracion.centros_costo cc ON(cc.codigo=s.centro_costo_solicitado) "+
								"LEFT OUTER JOIN det_cx_honorarios da ON(dc.det_cx_honorarios=da.codigo) " +
							"WHERE " +
								"dc.sub_cuenta="+(long)dtoFactura.getSubCuenta()+" " +
								"AND dc.cargo_padre = "+(long)codigoDetalleCargo+" "+
								"AND ssc.cuenta in("+UtilidadTexto.convertirVectorACodigosSeparadosXComas(dtoFactura.getCuentas(), false)+") " +
								"AND ssc.eliminado='"+ConstantesBD.acronimoNo+"' "+
								"AND dc.estado="+ConstantesBD.codigoEstadoFCargada+" " +
								"AND dc.facturado='"+ConstantesBD.acronimoNo+"' " +
								"AND dc.paquetizado='"+ConstantesBD.acronimoSi+"' "+///con esto solo cargamos el detalle del paquete
								"AND dc.servicio_cx IS NOT NULL " +
								"AND dc.cantidad_cargada>0 ";
		
		try
		{
			PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			logger.info("llega a proponerPaquetizacionDetalleFacturaDesdeCargos---------->"+consulta);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			{	
				DtoPaquetizacionDetalleFactura detalleDto= new DtoPaquetizacionDetalleFactura();
				detalleDto.setAjusteCreditoMedico(0);
				detalleDto.setAjusteDebitoMedico(0);
				detalleDto.setAjustesCredito(0);
				detalleDto.setAjustesDebito(0);
				detalleDto.setArticulo(new InfoDatosInt(rs.getInt("articulo")));
				detalleDto.setCodigo(ConstantesBD.codigoNuncaValidoDoubleNegativo);
				detalleDto.setCodigoDetalleFactura(ConstantesBD.codigoNuncaValido);
				detalleDto.setServicio(new InfoDatosInt(rs.getInt("servicio")));
				detalleDto.setValorCargo(rs.getDouble("valorcargo"));
				detalleDto.setValorIva(rs.getDouble("valoriva"));
				detalleDto.setValorRecargo(rs.getDouble("valorrecargo"));
				detalleDto.setValorTotal(rs.getDouble("valortotal"));
				detalleDto.setPorcentajeMedico(rs.getDouble("porcentajemedico"));
				detalleDto.setPool(rs.getInt("pool"));
				detalleDto.setCodigoMedico(rs.getInt("codigomedico"));
				detalleDto.setSolicitud(rs.getInt("numerosolicitud"));
				detalleDto.setCodigoDetalleCargo(rs.getDouble("codigodetallecargo"));
				detalleDto.setServicioCx(new InfoDatosInt(rs.getInt("serviciocx")));
				detalleDto.setCodigoEsquemaTarifario(rs.getInt("esquematarifario"));
				detalleDto.setCodigoEspecialidadAsocio(rs.getInt("especialidadasocio"));
				
				logger.info("\nPAQUETIZACION POOL-->"+detalleDto.getPool());
				logger.info("PORCENTAJE POOL PX-->"+detalleDto.getPorcentajePool());
				logger.info("VALOR POOL PX-->"+detalleDto.getValorPool());
				
				
				if(UtilidadTexto.getBoolean(ValoresPorDefecto.getValoresDefectoValidarPoolesFact(dtoFactura.getInstitucion())))
				{
					if(detalleDto.getPool()>0)
					{	
						if(detalleDto.getArticulo().getCodigo()>0) 
							//o en caso tal de q la solicitud tenga estado de historia clinica solicitada  o en proceso, no se valida porque no tiene respuesta, no tiene pool
							//|| (rs.getInt("codigoestadohc")==ConstantesBD.codigoEstadoHCSolicitada || rs.getInt("codigoestadohc")==ConstantesBD.codigoEstadoHCEnProceso || rs.getInt("codigoestadohc")==ConstantesBD.codigoEstadoHCTomaDeMuestra) 		
						{	
							detalleDto.setPorcentajePool(0);
							detalleDto.setValorPool(0);
						}	
						else
						{	
							
							InfoPorcentajeValor honorario=null; 
							if(detalleDto.getServicioCx().getCodigo()<=0)
							{	
								honorario=CalculoHonorariosPooles.obtenerHonorarioPoolDetalleFacturaNoCx(detalleDto.getCodigoDetalleCargo());
							}	
							else
							{
								//honorario=CalculoHonorariosPooles.obtenerHonorarioPoolDetalleFacturaAsociosCx(detalleDto.getServicio().getCodigo(), detalleDto.getPool(), dtoFactura.getConvenio().getCodigo(), detalleDto.getCodigoEsquemaTarifario(), detalleDto.getCodigoEspecialidadAsocio(), rs.getInt("centro_atencion"), rs.getInt("medicoresponde"));
								//tarea 184317
								honorario=CalculoHonorariosPooles.obtenerHonorarioPoolDetalleFacturaAsociosCx(detalleDto.getServicioCx().getCodigo(), detalleDto.getPool(), dtoFactura.getConvenio().getCodigo(), detalleDto.getCodigoEsquemaTarifario(), detalleDto.getCodigoEspecialidadAsocio(), rs.getInt("centro_atencion"), rs.getInt("medicoresponde"));
							}
								
							if(honorario!=null)
							{
								detalleDto.setValorPool(honorario.getValor().doubleValue());
								detalleDto.setPorcentajePool(honorario.getPorcentaje());
								
								logger.info("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
								logger.info("EL VALOR DEL POOL SETEADO ES-->"+detalleDto.getValorPool());
								logger.info("EL PORCENTAJE DEL POOL SETEADO ES-->"+detalleDto.getPorcentajePool());
								logger.info("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
							}
							
							
							////////////////calculo de pooles 
							/*if(rs.getInt("porcentaje_pool_convenio")>=0 )
							{
								detalleDto.setPorcentajePool(rs.getInt("porcentaje_pool_convenio"));
							}
							else if(rs.getDouble("valor_pool_convenio")>=0)
							{
								detalleDto.setValorPool(rs.getDouble("valor_pool_convenio"));
							}
							
							/////////en caso de que no sea asignado x convenio entonces tratamos de insertar x esquema
							if(detalleDto.getPorcentajePool()<0 && detalleDto.getValorPool()<0)
							{
								if(rs.getInt("porcentaje_pool_esquema")>=0 ) //rs.getDouble("valor_pool_convenio")>0)
								{
									detalleDto.setPorcentajePool(rs.getInt("porcentaje_pool_esquema"));
								}
								else if(rs.getDouble("valor_pool_esquema")>=0)
								{
									detalleDto.setValorPool(rs.getDouble("valor_pool_esquema"));
								}
							}*/
							
						}
					}
				}
				
				logger.info("2-PORCENTAJE POOL PX-->"+detalleDto.getPorcentajePool());
				logger.info("2-VALOR POOL PX-->"+detalleDto.getValorPool());
				
				logger.info("Codigo Medico-->"+detalleDto.getCodigoMedico());
				logger.info("Porcentaje Medico-->"+detalleDto.getPorcentajeMedico());
				
				//aplicamos el valor diferencia que existe entre lo que consume el paquete y lo que se cobro por el paquete padre
				double valorDif=0; 
				if(dtoDetalleFactura.getValorConsumoPaquete()>0)	
					valorDif=(dtoDetalleFactura.getValorTotal()/dtoDetalleFactura.getValorConsumoPaquete())*detalleDto.getValorTotal();
				detalleDto.setValorDifConsumoValor(valorDif);
				
				detalleDto.setValorAsocio(rs.getDouble("valorasocio"));
				detalleDto.setTipoAsocio(rs.getInt("tipoasocio"));
				detalleDto.setSoliPago(ConstantesBD.codigoNuncaValido);
				detalleDto.setCantidadCargo(rs.getInt("cantidadcargo"));
				detalleDto.setTipoCargo(rs.getInt("tipocargo"));
				detalleDto.setTipoSolicitud(rs.getInt("tiposolicitud"));
				
				logger.info("VALOR ASOCIO->"+detalleDto.getValorAsocio());
				//CALCULO DEL VALOR MEDICO
				double valorPool=ConstantesBD.codigoNuncaValido, valorMedico=ConstantesBD.codigoNuncaValido;
				try
				{
					if(detalleDto.getServicioCx().getCodigo()>0)
					{	
						if(detalleDto.getPorcentajePool()>=0)
						{
							valorPool=Math.rint((detalleDto.getValorAsocio()*detalleDto.getPorcentajePool())/100);
							valorMedico=(valorPool*detalleDto.getPorcentajeMedico())/100;
						}
						else if(detalleDto.getValorPool()>=0)
						{
							valorPool= detalleDto.getValorPool();
							valorMedico=(valorPool*detalleDto.getPorcentajeMedico())/100;
						}
						logger.info("VALOR POOL->(detalleDto.getValorAsocio()*detalleDto.getPorcentajePool())/100 ---->"+valorPool);
						logger.info("VALOR MEDICO->(valorPool*detalleDto.getPorcentajeMedico())/100 ---->"+valorMedico);
					}
					else
					{
						if(detalleDto.getPorcentajePool()>=0)
						{
							valorPool=Math.rint((detalleDto.getValorTotal()*detalleDto.getPorcentajePool())/100);
							valorMedico=(valorPool*detalleDto.getPorcentajeMedico())/100;
						}
						else if(detalleDto.getValorPool()>=0)
						{
							valorPool= detalleDto.getValorPool();
							valorMedico=(valorPool*detalleDto.getPorcentajeMedico())/100;
						}
					}
				}
				catch (Exception e) {
					valorMedico=ConstantesBD.codigoNuncaValido;
					valorPool=ConstantesBD.codigoNuncaValido;
				}
				
				detalleDto.setValorMedico(valorMedico);	
				detalleDto.setValorPool(valorPool);
				detalleDto.setCodigoMedicoAsocio(rs.getInt("medicoasocio"));
				
				detallePaquetizacionArray.add(detalleDto);
			}
		}	
		catch(SQLException e)
		{
			logger.warn(e+" Error en proponerPaquetizacionDetalleFacturaDesdeCargos ");
			e.printStackTrace();
		}
		return detallePaquetizacionArray; 
	}
	
	
	
	/**
	 * 
	 * @param con
	 * @param dtoFactura
	 * @return
	 */
	public static ResultadoInteger insertar(	Connection con, DtoFactura dtoFactura) 
	{	PreparedStatement pst=null;
		PreparedStatement pst1=null;
		PreparedStatement pst2=null;
		ResultSet rs=null;
		try 
		{
			
			String cadenaTempo1="select count(codigo) as cantidad from facturas where consecutivo_factura="+dtoFactura.getConsecutivoFactura();
			pst1= con.prepareStatement(cadenaTempo1);
			rs=pst1.executeQuery();
			if(rs.next())
			{
				if(rs.getInt("cantidad")>0)
				{					
					return new ResultadoInteger(ConstantesBD.codigoNuncaValido,"error.facturacion.noGuardoFactura.consecutivoRepetido");
				}
			}
			
			
			pst = con.prepareStatement(insertarFactStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			
			int codigo= UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_facturas");
			pst.setInt(1, codigo);
			pst.setLong(2, (long)dtoFactura.getSubCuenta());
			pst.setInt(3, dtoFactura.getEstadoFacturacion().getCodigo());
			pst.setInt(4, dtoFactura.getEstadoPaciente().getCodigo());
			pst.setString(5, dtoFactura.getLoginUsuario());
			
			pst.setInt(6, (int)dtoFactura.getConsecutivoFactura());
			
			if(dtoFactura.getHistoricoEncabezado()!=null&&!UtilidadTexto.isEmpty(dtoFactura.getHistoricoEncabezado().getPrefijoFactura()))
			{
				pst.setString(7, dtoFactura.getHistoricoEncabezado().getPrefijoFactura());
			}
			else
			{
				
				pst.setNull(7, Types.VARCHAR);
			}
		
			pst.setInt(8, dtoFactura.getInstitucion());
			
			if(dtoFactura.getValorAbonos()<0)
				pst.setDouble(9, 0);
			else
				pst.setDouble(9, dtoFactura.getValorAbonos());
			pst.setDouble(10, dtoFactura.getValorTotal());
			pst.setDouble(11, dtoFactura.getValorNetoPaciente());
			pst.setDouble(12, dtoFactura.getValorConvenio());
			pst.setDouble(13, dtoFactura.getValorBrutoPac());
			pst.setDouble(14, dtoFactura.getValorDescuentoPaciente());
			
			if(dtoFactura.getNumeroCuentaCobro()<0)
				pst.setNull(15, Types.DOUBLE);
			else
				pst.setLong(15, (long)dtoFactura.getNumeroCuentaCobro());
			
			pst.setBoolean(16, dtoFactura.getTipoFacturaSistema());
			
			if(dtoFactura.getCodigoPaciente()<1)
				pst.setNull(17, Types.INTEGER);
			else
				pst.setInt(17, dtoFactura.getCodigoPaciente());
			
			if(dtoFactura.getCodigoResonsableParticular()<1)
				pst.setNull(18, Types.INTEGER);
			else
				pst.setInt(18, dtoFactura.getCodigoResonsableParticular());
			
			if(Utilidades.convertirAEntero(dtoFactura.getCuentas().get(0).toString())<1)
				pst.setNull(19, Types.INTEGER );
			else
				pst.setInt(19, Utilidades.convertirAEntero(dtoFactura.getCuentas().get(0).toString()));
			
			if(dtoFactura.getConvenio().getCodigo()<1)
				pst.setNull(20, Types.INTEGER);
			else
				pst.setInt(20, dtoFactura.getConvenio().getCodigo());
			
			if(dtoFactura.getViaIngreso().getCodigo()<1)
				pst.setNull(21, Types.INTEGER);
			else
				pst.setInt(21, dtoFactura.getViaIngreso().getCodigo());
			
			pst.setDouble(22, dtoFactura.getValorCartera());
			if(dtoFactura.getMontoCobro()<1)
				pst.setNull(23, Types.INTEGER);
			else
			{
				pst.setInt(23, obtenerHistoricoDetalleMonto(con, dtoFactura.getMontoCobro()));
			}
			
			if(dtoFactura.getCentroAtencion().getCodigo()<1)
				pst.setNull(24, Types.INTEGER);
			else
				pst.setInt(24, dtoFactura.getCentroAtencion().getCodigo());
			
			pst.setDouble(25, dtoFactura.getValorPagos());
			pst.setDouble(26, dtoFactura.getAjustesCredito());
			pst.setDouble(27, dtoFactura.getAjustesDebito());
			pst.setBoolean(28, dtoFactura.getFacturaCerrada());
			
			
			if(UtilidadTexto.isEmpty(dtoFactura.getTipoAfiliado()))
				pst.setNull(29, Types.VARCHAR);
			else
				pst.setString(29, dtoFactura.getTipoAfiliado());
			
			if(dtoFactura.getEstratoSocial().getCodigo()<1)
				pst.setNull(30, Types.INTEGER);
			else
				pst.setInt(30, dtoFactura.getEstratoSocial().getCodigo());
			
			if(dtoFactura.getTipoMonto().getCodigo()<1)
				pst.setNull(31, Types.INTEGER);
			else
				pst.setInt(31, dtoFactura.getTipoMonto().getCodigo());
			
			if(dtoFactura.getValorAFavorConvenio()<1)
				pst.setNull(32, Types.DOUBLE);
			else
				pst.setDouble(32, dtoFactura.getValorAFavorConvenio());
			
			if(dtoFactura.getHistoricoEncabezado()==null||UtilidadTexto.isEmpty(dtoFactura.getHistoricoEncabezado().getResolucion()))
				pst.setNull(33, Types.VARCHAR);
			else
				pst.setString(33, dtoFactura.getHistoricoEncabezado().getResolucion());
			
			
			double rangoInicial = Utilidades.convertirADouble(dtoFactura.getHistoricoEncabezado().getRangoInicial());
			
			if(rangoInicial > 0){
	
				pst.setDouble(34, rangoInicial);
				
			}else{
				
				pst.setNull(34, Types.NUMERIC);
			}

			double rangoFinal = Utilidades.convertirADouble(dtoFactura.getHistoricoEncabezado().getRangoFinal());
			
			if(rangoFinal > 0){
				
				pst.setDouble(35, rangoFinal);
				
			}else{
				
				pst.setNull(35, Types.NUMERIC);
			}

			if(!dtoFactura.getFecha().trim().equals(""))
				pst.setDate(36, Date.valueOf(dtoFactura.getFecha()));
			else
				pst.setDate(36, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual())));
			
			pst.setString(37, UtilidadFecha.getHoraActual());
			
			if(dtoFactura.getEntidadSubcontratada()>0)
				pst.setLong(38, (long)dtoFactura.getEntidadSubcontratada());
			else
				pst.setNull(38, Types.NUMERIC);
			
			if(dtoFactura.getEmpresaInstitucion()>0)
				pst.setLong(39, (long)dtoFactura.getEmpresaInstitucion());
			else
				pst.setNull(39, Types.NUMERIC);
			
			if(dtoFactura.getPacienteEntidadSubcontratada()>0)
				pst.setLong(40, (long)dtoFactura.getPacienteEntidadSubcontratada());
			else
				pst.setNull(40, Types.NUMERIC);
			
			pst.setDouble(41, dtoFactura.getValorLiquidadoPaciente());
			
			if(dtoFactura.getTopeFacturacion()>0)
				pst.setInt(42, dtoFactura.getTopeFacturacion());
			else
				pst.setNull(42, Types.INTEGER);
			
			if(UtilidadFecha.esFechaValidaSegunAp(dtoFactura.getFechaVigenciaInicialMontoCobro()))
				pst.setDate(43, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(dtoFactura.getFechaVigenciaInicialMontoCobro())));
			else
				pst.setNull(43, Types.DATE);
			
			if(UtilidadFecha.esFechaValidaSegunAp(dtoFactura.getFechaVigenciaInicialTopeFacturacion()))
				pst.setDate(44, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(dtoFactura.getFechaVigenciaInicialTopeFacturacion())));
			else
				pst.setNull(44, Types.DATE);
			
			if(dtoFactura.getCentroAtencionDuenio().getCodigo()>0)
			{
				pst.setInt(45, dtoFactura.getCentroAtencionDuenio().getCodigo());
			}
			else
			{
				pst.setNull(45, Types.NULL);
			}
			
			if(dtoFactura.getValorAnticipos()>0)
			{
				pst.setDouble(46, dtoFactura.getValorAnticipos());
			}
			else
			{
				pst.setNull(46, Types.NULL);
			}


			if(dtoFactura.getHistoricoEncabezado().getCodigoPk()>0)
				pst.setLong(47, dtoFactura.getHistoricoEncabezado().getCodigoPk());
			else
				pst.setObject(47, null);
			
			
			if(dtoFactura.getContrato().getCodigo()>0)
				pst.setLong(48, dtoFactura.getContrato().getCodigo());
			else
				pst.setObject(48, null);
			
						
			if( pst.executeUpdate()>0)
			{
				for(int w=0; w<dtoFactura.getDetallesFactura().size(); w++)
				{	
					int codigoDetalleFactura= UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_det_fac_sol");
					if(!insertarDetalleFactura(con, dtoFactura.getDetallesFactura().get(w), codigo, dtoFactura.getLoginUsuario(),codigoDetalleFactura,false))
					{
						logger.error("NO INSERTO EL DETALLE DE LA FACTURA # ->"+w);
						return new ResultadoInteger(ConstantesBD.codigoNuncaValido,"");
					}
					//,dtoFactura.getInforCalculoValorPaciente()
					cicloExt:for(DtoAgrupacionCalucloValorCobrarPaciente dtoInfoAgrupacion:dtoFactura.getInforCalculoValorPaciente().getAgrupacionCalculo())
					{
						for(DtoInfoCargoCobroPaciente dtoCargoPaco:dtoInfoAgrupacion.getDtoInfoCargo())
						{
							if(dtoCargoPaco.getCodigoDetalleCargo()==dtoFactura.getDetallesFactura().get(w).getCodigoDetalleCargo())
							{
								insertarInformacionMontoCargo(con,codigoDetalleFactura,dtoInfoAgrupacion,dtoCargoPaco);
								break cicloExt;
							}
						}
					}
				}
				//insertar las excentas
				logger.info("\n\n\n\n\n\n\n\n\n\n\nINSERTAR EL DETALLE DE LAS FACTURAS -->"+dtoFactura.getDetallesFactura().size()+"\n\n\n\n\n\n\\n\n");
				for(int w=0; w<dtoFactura.getDetallesFacturaExcentas().size(); w++)
				{	
					int codigoDetalleFactura= UtilidadBD.obtenerSiguienteValorSecuencia(con, "facturacion.seq_det_fac_sol_excenta");
					if(!insertarDetalleFactura(con, dtoFactura.getDetallesFacturaExcentas().get(w), codigo, dtoFactura.getLoginUsuario(),codigoDetalleFactura,true))
					{
						logger.error("NO INSERTO EL DETALLE DE LA FACTURA # ->"+w);
						return new ResultadoInteger(ConstantesBD.codigoNuncaValido,"");
					}
				}
				
				String cadenaTempo="select consecutivo_factura from facturas where codigo="+codigo;
				pst2= con.prepareStatement(cadenaTempo);
				rs=pst2.executeQuery();
				if(rs.next())
				{
					if(rs.getInt(1)>0)
					{
						return new ResultadoInteger(codigo,"");
					}
				}
			}
		}
		catch(SQLException sqe){
			logger.error("############## SQLException ERROR insertar",sqe);
		}
		catch(Exception e){
			logger.error("############## ERROR insertar", e);
		}
		finally{
			try{
				if(rs != null){
					rs.close();
				}
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException se) {
				logger.error("###########  Error close ResultSet - PreparedStatement", se);
			}
		}
		return new ResultadoInteger(ConstantesBD.codigoNuncaValido,"");
	}
	
	/**
	 * 
	 * @param montoCobro
	 * @return
	 */
	private static int obtenerHistoricoDetalleMonto(Connection con, int montoCobro) 
	{
		int resultado=ConstantesBD.codigoNuncaValido;
		PreparedStatement pst=null;
		ResultSet rs=null;
		try
		{
			String consulta="SELECT codigo_pk from histo_detalle_monto where detalle_codigo="+montoCobro+" order by codigo_pk desc";
			pst=con.prepareStatement(consulta);
			rs=pst.executeQuery();
			if(rs.next()){
				resultado=rs.getInt(1);
			}
		}
		catch(SQLException sqe){
			logger.error("############## SQLException ERROR obtenerHistoricoDetalleMonto",sqe);
		}
		catch(Exception e){
			logger.error("############## ERROR obtenerHistoricoDetalleMonto", e);
		}
		finally{
			try{
				if(rs != null){
					rs.close();
				}
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException se) {
				logger.error("###########  Error close ResultSet - PreparedStatement", se);
			}
		}
		return resultado;
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoDetalleFactura
	 * @param dtoInfoAgrupacion
	 * @param dtoCargoPaco
	 */
	private static boolean insertarInformacionMontoCargo(Connection con,int codigoDetalleFactura,DtoAgrupacionCalucloValorCobrarPaciente dtoInfoAgrupacion,DtoInfoCargoCobroPaciente dtoCargoPaco) 
	{
		boolean resultado=false;
		PreparedStatement pst=null;
		
		try
		{
			String cadena="insert into facturacion.info_calculo_monto_factura values (" +
					" codigo_pk," +
					" codigo_det_factura," +
					" agrupacion_servicios," +
					" agrupacion_articulos," +
					" servicio_especifico," +
					" articulo_especifico," +
					" cantidad_monto," +
					" valor_monto," +
					" total_servart_aplican_monto," +
					" montos_a_cobrar" +
				") " +
				"values " +
				"(?,?,?,?,?,?,?,?,?,?)";
			pst =con.prepareStatement(cadena);
			pst.setInt(1, UtilidadBD.obtenerSiguienteValorSecuencia("facturacion.seq_infocalmontofactura"));
			pst.setInt(2, codigoDetalleFactura);
			if(dtoCargoPaco.getCodigoArticulo()>0)
			{
				if(!dtoInfoAgrupacion.getInfoMonto().isPorAgrupacion())
				{
					//por articculo especifico
					pst.setObject(3, null); //agrupacion_servicios
					pst.setObject(4, null);//agrupacion_articulos
					pst.setObject(5, null);//servicio especifico
					pst.setInt(6, dtoInfoAgrupacion.getInfoMonto().getCodigo());//articulo especifico
					
				}
				else
				{
					//agrupacion articulo
					pst.setObject(3, null); //agrupacion_servicios
					pst.setInt(4, dtoInfoAgrupacion.getInfoMonto().getCodigo());//agrupacion_articulos
					pst.setObject(5, null);//servicio especifico
					pst.setObject(6, null);//articulo especifico
				}
			}
			else
			{
				if(!dtoInfoAgrupacion.getInfoMonto().isPorAgrupacion())
				{
					//servicio especifico
					pst.setObject(3, null); //agrupacion_servicios
					pst.setObject(4, null);//agrupacion_articulos
					pst.setInt(5,  dtoInfoAgrupacion.getInfoMonto().getCodigo());//servicio especifico
					pst.setObject(6, null);//articulo especifico
				}
				else
				{
					//agrupacion servicios
					pst.setInt(3, dtoInfoAgrupacion.getInfoMonto().getCodigo()); //agrupacion_servicios
					pst.setObject(4, null);//agrupacion_articulos
					pst.setObject(5, null);//servicio especifico
					pst.setObject(6, null);//articulo especifico
				}
			}
			if(dtoCargoPaco.getInformacionMonto().getCantidadMonto()>0)
				pst.setInt(7, dtoCargoPaco.getInformacionMonto().getCantidadMonto());
			else
				pst.setObject(7, null);
			
			if(dtoCargoPaco.getInformacionMonto().getValorMonto()>0)
				pst.setDouble(8, dtoCargoPaco.getInformacionMonto().getValorMonto());
			else
				pst.setObject(8, null);
			
			if(dtoInfoAgrupacion.getCantidadCargadaMonto()>0)
				pst.setInt(9, dtoInfoAgrupacion.getCantidadCargadaMonto());
			else
				pst.setObject(9, null);
			
			if(dtoInfoAgrupacion.montoACobrarXRegistro()>0)
				pst.setInt(10, dtoInfoAgrupacion.montoACobrarXRegistro());
			else 
				pst.setObject(10, null);
			resultado=pst.executeUpdate()>0;
		}
		catch(SQLException sqe){
			logger.error("############## SQLException ERROR insertarInformacionMontoCargo",sqe);
		}
		catch(Exception e){
			logger.error("############## ERROR insertarInformacionMontoCargo", e);
		}
		finally{
			try{
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException se) {
				logger.error("###########  Error close ResultSet - PreparedStatement", se);
			}
		}
		return resultado;
	}

	/**
	 * 
	 * @param con
	 * @param dtoDetalleFactura
	 * @param codigofactura
	 * @param dtoInfoCobroPaciente 
	 * @return
	 */
	public static boolean insertarDetalleFactura(Connection con, DtoDetalleFactura dtoDetalleFactura, int codigofactura, String loginUsuario,int codigo, boolean esExcenta)
	{
		PreparedStatement pst=null;
		try 
		{
			
			if(!esExcenta)
				pst= con.prepareStatement(insertarDetFacStr);
			else
				pst= con.prepareStatement(insertarDetFacExcentaStr);
			
			pst.setInt(1, codigo);
			pst.setInt(2, dtoDetalleFactura.getNumeroSolicitud());
			pst.setInt(3, codigofactura);
			if(dtoDetalleFactura.getAjusteCreditoMedico()<0)
			{	
				pst.setDouble(4, 0);
			}	
			else
			{	
				pst.setDouble(4, dtoDetalleFactura.getAjusteCreditoMedico());
			}	
			
			if(dtoDetalleFactura.getPorcentajePool()<0)
				pst.setDouble(5, 0);
			else
				pst.setDouble(5, dtoDetalleFactura.getPorcentajePool());
			
			if(dtoDetalleFactura.getPorcentajeMedico()<0)
				pst.setDouble(6, 0);
			else
				pst.setDouble(6, dtoDetalleFactura.getPorcentajeMedico());
			
			if(dtoDetalleFactura.getValorMedico()<0)
				pst.setDouble(7, 0);
			else
				pst.setDouble(7, dtoDetalleFactura.getValorMedico());
			
			pst.setDate(8, Date.valueOf(dtoDetalleFactura.getFechaCargo()));
			pst.setInt(9, dtoDetalleFactura.getCantidadCargo());
			pst.setDouble(10, dtoDetalleFactura.getValorCargo());
			
			if(dtoDetalleFactura.getValorIva()<0)
				pst.setDouble(11, 0);
			else
				pst.setDouble(11, dtoDetalleFactura.getValorIva());
			
			if(dtoDetalleFactura.getValorRecargo()<0)
				pst.setDouble(12, 0);
			else
				pst.setDouble(12, dtoDetalleFactura.getValorRecargo());
			
			if(dtoDetalleFactura.getSolicitudPago()<=0)
				pst.setNull(13, Types.INTEGER);
			else
				pst.setInt(13, dtoDetalleFactura.getSolicitudPago());
			
			if(dtoDetalleFactura.getAjustesCredito()<0)
				pst.setDouble(14, 0);
			else
				pst.setDouble(14, dtoDetalleFactura.getAjustesCredito());
			
			if(dtoDetalleFactura.getCodigoServicio()<=0)
				pst.setNull(15, Types.INTEGER);
			else
				pst.setInt(15, dtoDetalleFactura.getCodigoServicio());
			
			if(dtoDetalleFactura.getCodigoArticulo()<=0)
				pst.setNull(16, Types.INTEGER);
			else
				pst.setInt(16, dtoDetalleFactura.getCodigoArticulo());
			
			pst.setDouble(17, dtoDetalleFactura.getValorTotal());
			
			if(dtoDetalleFactura.getAjustesDebito()<0)
				pst.setDouble(18, 0);
			else
				pst.setDouble(18, dtoDetalleFactura.getAjustesDebito());
			
			pst.setInt(19, dtoDetalleFactura.getCodigoTipoCargo());
			
			if(dtoDetalleFactura.getAjusteDebitoMedico()<0)
				pst.setDouble(20, 0);
			else
				pst.setDouble(20, dtoDetalleFactura.getAjusteDebitoMedico());

			if(dtoDetalleFactura.getValorPool()<0)
				pst.setDouble(21, 0);
			else
				pst.setDouble(21, dtoDetalleFactura.getValorPool());
			
			if(dtoDetalleFactura.getPool()>0)
				pst.setInt(22, dtoDetalleFactura.getPool());
			else
				pst.setNull(22, Types.INTEGER);
			
			if(dtoDetalleFactura.getCodigoMedico()<=0)
				pst.setNull(23, Types.INTEGER);
			else
				pst.setInt(23, dtoDetalleFactura.getCodigoMedico());
			
			if(dtoDetalleFactura.getValorDescuentoComercial()<0)
				pst.setNull(24, Types.DOUBLE);
			else
				pst.setDouble(24, dtoDetalleFactura.getValorDescuentoComercial());
			
			if(dtoDetalleFactura.getValorConsumoPaquete()<0)
				pst.setNull(25, Types.NUMERIC);
			else
				pst.setDouble(25, dtoDetalleFactura.getValorConsumoPaquete());
			
			pst.setInt(26, dtoDetalleFactura.getCodigoTipoSolicitud());
			
			pst.setInt(27, dtoDetalleFactura.getCodigoEsquemaTarifario());
			
			if(dtoDetalleFactura.getPrograma().getCodigo()>0)
			{
				pst.setDouble(28, dtoDetalleFactura.getPrograma().getCodigo());
			}
			else
			{
				pst.setNull(28, Types.NUMERIC);
			}
			
			if(dtoDetalleFactura.getValorDctoOdo().doubleValue()>0)
			{
				pst.setBigDecimal(29, dtoDetalleFactura.getValorDctoOdo());
			}
			else
			{
				pst.setNull(29, Types.NUMERIC);
			}
			
			if(dtoDetalleFactura.getValorDctoBono().doubleValue()>0)
			{
				pst.setBigDecimal(30, dtoDetalleFactura.getValorDctoBono());
			}
			else
			{
				pst.setNull(30, Types.NUMERIC);
			}
			
			if(dtoDetalleFactura.getValorDctoProm().doubleValue()>0)
			{
				pst.setBigDecimal(31, dtoDetalleFactura.getValorDctoProm());
			}
			else
			{
				pst.setNull(31, Types.NUMERIC);
			}
			
			if(dtoDetalleFactura.getDetallePaqueteOdonConvenio()>0)
			{
				pst.setInt(32, dtoDetalleFactura.getDetallePaqueteOdonConvenio());
			}
			else
			{
				pst.setNull(32, Types.INTEGER);
			}
			
			
			
			if( pst.executeUpdate()>0)
			{
				for(int w=0; w<dtoDetalleFactura.getAsociosDetalleFactura().size(); w++)
				{	
					if(!insertarAsociosFactura(con, dtoDetalleFactura.getAsociosDetalleFactura().get(w), codigo, loginUsuario,esExcenta))
					{
						logger.error("NO INSERTO EL DETALLE DE LA FACTURA # ->"+w);
						return false;
					}
				}
				for(int w=0; w<dtoDetalleFactura.getPaquetizacionDetalleFactura().size(); w++)
				{	
					if(!insertarPaquetizacionFactura(con, dtoDetalleFactura.getPaquetizacionDetalleFactura().get(w), codigo))
					{
						logger.error("NO INSERTO EL DETALLE DE LA FACTURA # ->"+w);
						return false;
					}
				}
				return true;
			}
			
		}
		catch(SQLException sqe){
			logger.error("############## SQLException ERROR insertarDetalleFactura",sqe);
		}
		catch(Exception e){
			logger.error("############## ERROR insertarDetalleFactura", e);
		}
		finally{
			try{
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException se) {
				logger.error("###########  Error close ResultSet - PreparedStatement", se);
			}
		}
		return false;
	}
	
	/**
	 * 
	 * @param con
	 * @param dtoDetalleFactura
	 * @param codigofactura
	 * @return
	 */
	public static boolean insertarAsociosFactura(Connection con, DtoAsociosDetalleFactura dtoAsocios, int codigoDetalleFactura, String loginUsuario,boolean esExcenta)
	{
		PreparedStatement pst=null;
		try 
		{
			if(!esExcenta)
				pst=  con.prepareStatement(insertarAsociosDetFacStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			else
				pst=  con.prepareStatement(insertarAsociosDetFacExcentaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			
			pst.setInt(1, codigoDetalleFactura);
			pst.setInt(2, dtoAsocios.getCodigoServicioAsocio());
			pst.setDouble(3, dtoAsocios.getValorAsocio());
			pst.setInt(4,dtoAsocios.getTipoAsocio());
			pst.setDouble(5, dtoAsocios.getAjusteCreditoMedico());
			pst.setDouble(6, dtoAsocios.getAjusteDebitoMedico());
			
			if(dtoAsocios.getPorcentajePool()<0)
				pst.setInt(7, 0);
			else
				pst.setInt(7, dtoAsocios.getPorcentajePool());
			
			if(dtoAsocios.getPorcentajeMedico()<0)
				pst.setDouble(8, 0);
			else
				pst.setDouble(8, dtoAsocios.getPorcentajeMedico());
			
			if(dtoAsocios.getValorMedico()<0)
				pst.setDouble(9, 0);
			else
				pst.setDouble(9, dtoAsocios.getValorMedico());
			
			if(dtoAsocios.getValorCargo()<0)
				pst.setDouble(10, 0);
			else
				pst.setDouble(10, dtoAsocios.getValorCargo());
			
			if(dtoAsocios.getValorIva()<0)
				pst.setDouble(11, 0);
			else
				pst.setDouble(11, dtoAsocios.getValorIva());
			
			if(dtoAsocios.getValorRecargo()<0)
				pst.setDouble(12, 0);
			else
				pst.setDouble(12, dtoAsocios.getValorRecargo());
			
			if(dtoAsocios.getValorTotal()<0)
				pst.setDouble(13, 0);
			else
				pst.setDouble(13, dtoAsocios.getValorTotal());
			
			if(dtoAsocios.getAjustesCredito()<0)
				pst.setDouble(14, 0);
			else
				pst.setDouble(14, dtoAsocios.getAjustesCredito());
			
			pst.setDouble(15, dtoAsocios.getAjustesDebito());
			pst.setInt(16, dtoAsocios.getCodigoEsquemaTarifario());
			
			if(dtoAsocios.getValorPool()<0)
				pst.setDouble(17, 0);
			else
				pst.setDouble(17, dtoAsocios.getValorPool());
			
			if(dtoAsocios.getPool()<=0)
				pst.setNull(18, Types.INTEGER);
			else
				pst.setInt(18, dtoAsocios.getPool());
			
			if(dtoAsocios.getCodigoMedicoAsocio()<=0)
				pst.setNull(19, Types.INTEGER);
			else
				pst.setInt(19, dtoAsocios.getCodigoMedicoAsocio());
			
			int consecutivoAsocio=ConstantesBD.codigoNuncaValido;
			if(esExcenta)
				consecutivoAsocio=UtilidadBD.obtenerSiguienteValorSecuencia(con, "facturacion.seq_asocios_det_fact_ex");
			else
				consecutivoAsocio=UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_asocios_det_fact");
			

			
			pst.setDouble(20, consecutivoAsocio);
			
			if(dtoAsocios.getCodigoMedicoAsocio()>0)
				pst.setInt(21, dtoAsocios.getCodigoMedicoAsocio());
			else
				pst.setNull(21, Types.INTEGER);
			
			if(dtoAsocios.getCodigoEspecialidadMedicoAsocio()>0)
				pst.setInt(22, dtoAsocios.getCodigoEspecialidadMedicoAsocio());
			else
				pst.setNull(22, Types.INTEGER);
				
			if(!dtoAsocios.getCodigoPropietarioAsocio().isEmpty())
				pst.setString(23, dtoAsocios.getCodigoPropietarioAsocio());
			else
				pst.setObject(23, null);
			
			
			if(pst.executeUpdate()>0)
			{
				for(DtoDetAsociosDetFactura dtoDetalleAsocios: dtoAsocios.getDetalleAsociosArray())
				{
					insertarDetalleAsocios(con, dtoDetalleAsocios, loginUsuario, consecutivoAsocio,esExcenta);
				}
				
				return true;
			}	
		}
		catch(SQLException sqe){
			logger.error("############## SQLException ERROR insertarAsociosFactura",sqe);
		}
		catch(Exception e){
			logger.error("############## ERROR insertarAsociosFactura", e);
		}
		finally{
			try{
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException se) {
				logger.error("###########  Error close ResultSet - PreparedStatement", se);
			}
		}
		return false;
	}
	
	/**
	 * 
	 * @param con
	 * @param dto
	 * @param loginUsuario
	 * @param consecutivoAsocio 
	 * @return
	 */
	public static boolean insertarDetalleAsocios(Connection con, DtoDetAsociosDetFactura dto, String loginUsuario, int consecutivoAsocio,boolean esExcento)
	{
		PreparedStatement pst=null;
		try 
		{
			if(!esExcento)
			{
				pst=  con.prepareStatement(insertarDetAsociosDetFacturaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
				pst.setInt(1, UtilidadBD.obtenerSiguienteValorSecuencia(con, "facturacion.seq_det_asocios_det_fac"));
				}
			else
			{
				pst= con.prepareStatement(insertarDetAsociosDetFacturaExcentaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
				pst.setInt(1, UtilidadBD.obtenerSiguienteValorSecuencia(con, "facturacion.seq_det_asocios_det_fac_ex"));
				
			}
			pst.setInt(2, consecutivoAsocio);
			pst.setInt(3, dto.getArticulo());
			pst.setInt(4, dto.getCantidad());
			pst.setDouble(5, dto.getValorUnitario());
			pst.setDouble(6, dto.getValorTotal());
			pst.setString(7, loginUsuario);
			
			if(pst.executeUpdate()>0)
			{
				return true;
			}
		}
		catch(SQLException sqe){
			logger.error("############## SQLException ERROR insertarDetalleAsocios",sqe);
		}
		catch(Exception e){
			logger.error("############## ERROR insertarDetalleAsocios", e);
		}
		finally{
			try{
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException se) {
				logger.error("###########  Error close ResultSet - PreparedStatement", se);
			}
		}
		return false;
	}
	
	
	/**
	 * 
	 * @param con
	 * @param dtoPaquetizacion
	 * @param codigoDetalleFactura
	 * @return
	 */
	public static boolean insertarPaquetizacionFactura(Connection con, DtoPaquetizacionDetalleFactura dtoPaquetizacion, int codigoDetalleFactura)
	{
		PreparedStatement pst=null;
		try 
		{
			pst = con.prepareStatement(insertarPaquetizacionDetFacStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			
			
			int codigo=UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_pq_det_fact");
			logger.info("codigo->"+codigo);
			pst.setInt(1, codigo);
			logger.info("codigodetfact->"+codigoDetalleFactura);
			pst.setInt(2, codigoDetalleFactura);
			logger.info("servicio->"+dtoPaquetizacion.getServicio().getCodigo());
			if(dtoPaquetizacion.getServicio().getCodigo()>0)
				pst.setInt(3, dtoPaquetizacion.getServicio().getCodigo());
			else
				pst.setNull(3, Types.INTEGER);
			logger.info("articulo->"+dtoPaquetizacion.getArticulo().getCodigo());
			if(dtoPaquetizacion.getArticulo().getCodigo()>0)
				pst.setInt(4, dtoPaquetizacion.getArticulo().getCodigo());
			else
				pst.setNull(4, Types.INTEGER);
			logger.info("ajust->"+dtoPaquetizacion.getAjusteCreditoMedico());
			pst.setDouble(5, dtoPaquetizacion.getAjusteCreditoMedico());
			logger.info("ajusd->"+dtoPaquetizacion.getAjusteDebitoMedico());
			pst.setDouble(6, dtoPaquetizacion.getAjusteDebitoMedico());
			logger.info("valcar->"+dtoPaquetizacion.getValorCargo());
			pst.setDouble(7, dtoPaquetizacion.getValorCargo());
			logger.info("iva->"+dtoPaquetizacion.getValorIva());
			pst.setDouble(8, dtoPaquetizacion.getValorIva());
			logger.info("recargo->"+dtoPaquetizacion.getValorRecargo());
			pst.setDouble(9, dtoPaquetizacion.getValorRecargo());
			logger.info("total->"+dtoPaquetizacion.getValorTotal());
			pst.setDouble(10, dtoPaquetizacion.getValorTotal());
			logger.info("val dif->"+dtoPaquetizacion.getValorDifConsumoValor());
			pst.setDouble(11, dtoPaquetizacion.getValorDifConsumoValor());
			logger.info("ajus cre->"+dtoPaquetizacion.getAjustesCredito());
			pst.setDouble(12, dtoPaquetizacion.getAjustesCredito());
			logger.info("ajus deb->"+dtoPaquetizacion.getAjustesDebito());
			pst.setDouble(13, dtoPaquetizacion.getAjustesDebito());
			logger.info("13");
			
			if(dtoPaquetizacion.getPorcentajeMedico()<0)
				pst.setNull(14, Types.DOUBLE);
			else
				pst.setDouble(14, dtoPaquetizacion.getPorcentajeMedico());
			
			if(dtoPaquetizacion.getPorcentajePool()<0)
				pst.setNull(15, Types.DOUBLE);
			else
				pst.setDouble(15, dtoPaquetizacion.getPorcentajePool());
			
			logger.info("servicio cx ->"+dtoPaquetizacion.getServicioCx().getCodigo());
			if(dtoPaquetizacion.getServicioCx().getCodigo()>0)
				pst.setInt(16, dtoPaquetizacion.getServicioCx().getCodigo());
			else
				pst.setNull(16, Types.INTEGER);

			if(dtoPaquetizacion.getValorAsocio()>=0)
				pst.setDouble(17, dtoPaquetizacion.getValorAsocio());
			else
				pst.setNull(17, Types.DOUBLE );

			if(dtoPaquetizacion.getTipoAsocio()>0)
				pst.setInt(18, dtoPaquetizacion.getTipoAsocio());
			else
				pst.setNull(18, Types.INTEGER);

			if(dtoPaquetizacion.getValorMedico()>=0)
				pst.setDouble(19, dtoPaquetizacion.getValorMedico());
			else
				pst.setNull(19, Types.DOUBLE );
			
			if(dtoPaquetizacion.getValorPool()>=0)
				pst.setDouble(20, dtoPaquetizacion.getValorPool());
			else
				pst.setNull(20, Types.DOUBLE );
			 
			if(dtoPaquetizacion.getPool()>0)
				pst.setInt(21, dtoPaquetizacion.getPool());
			else
				pst.setNull(21, Types.INTEGER); 
			
			if(dtoPaquetizacion.getCodigoMedico()>0)
				pst.setInt(22, dtoPaquetizacion.getCodigoMedico());
			else
				pst.setNull(22, Types.INTEGER);
			
			if(dtoPaquetizacion.getSoliPago()>0)
				pst.setInt(23, dtoPaquetizacion.getSoliPago());
			else
				pst.setNull(23, Types.INTEGER);
			 
			if(dtoPaquetizacion.getCantidadCargo()>=0)
				pst.setInt(24, dtoPaquetizacion.getCantidadCargo());
			else
				pst.setNull(24, Types.INTEGER);
			
			pst.setInt(25, dtoPaquetizacion.getTipoCargo());
			pst.setInt(26, dtoPaquetizacion.getTipoSolicitud());
			pst.setInt(27, dtoPaquetizacion.getSolicitud());
			
			pst.setInt(28, dtoPaquetizacion.getCodigoEsquemaTarifario());
			
			if(dtoPaquetizacion.getCodigoMedicoAsocio()>0)
				pst.setInt(29, dtoPaquetizacion.getCodigoMedicoAsocio());
			else
				pst.setNull(29, Types.INTEGER);
			
			if(dtoPaquetizacion.getCodigoEspecialidadAsocio()>0)
				pst.setInt(30, dtoPaquetizacion.getCodigoEspecialidadAsocio());
			else
				pst.setNull(30, Types.INTEGER);
			
			if(pst.executeUpdate()>0)
			{
				pst.close();
				return true;
			}	
		}
		catch(SQLException sqe){
			logger.error("############## SQLException ERROR insertarPaquetizacionFactura",sqe);
		}
		catch(Exception e){
			logger.error("############## ERROR insertarPaquetizacionFactura", e);
		}
		finally{
			try{
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException se) {
				logger.error("###########  Error close ResultSet - PreparedStatement", se);
			}
		}
		return false;
	}
	
	/**
	 * Método que inicializa el proceso de facturación Inserta en la tabla
	 * 
	 * @param con
	 * @param idCuenta
	 * @param loginUsuario
	 * @param estado
	 *            Estado de la transacción
	 * @return número mayor que 0 si se inició el proceso correctamente
	 */
	public static int empezarProcesoFacturacionTransaccional(		Connection con,	int idCuenta, 
	        														String loginUsuario, String estado, String idSesion, String hora) 
	{
		if (estado == null) 
		{
			return empezarProcesoFacturacion(con, idCuenta, loginUsuario, idSesion, hora);
		}
		try 
		{
			if (estado.equals(ConstantesBD.inicioTransaccion)) 
			{
				DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).beginTransaction(con);
			}
			int resultado = empezarProcesoFacturacion(con, idCuenta,loginUsuario, idSesion, hora);
			if (estado.equals(ConstantesBD.finTransaccion)) 
			{
				DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).endTransaction(con);
			}
			return resultado;
		} 
		catch (SQLException e) 
		{
			logger.error("Error iniciando la transacción " + e);
			return 0;
		}
	}

	/**
	 * Método que inicializa el proceso de facturación Inserta en la tabla
	 * 
	 * @param con
	 * @param idCuenta
	 * @param loginUsuario
	 * @return número mayor que 0 si se inició el proceso correctamente
	 */
	private static int empezarProcesoFacturacion(	Connection con, int idCuenta,
													String loginUsuario, String idSesion, String hora) 
	{
		PreparedStatement pst=null;
		try 
		{
			String procesoFactStr = "INSERT INTO cuentas_proceso_fact(cuenta, estado, usuario, fecha, hora, id_sesion) " +
				"VALUES(?,(SELECT estado_cuenta from cuentas where id=? AND estado_cuenta!="
								+ ConstantesBD.codigoEstadoCuentaProcesoFacturacion
								+ "), ?, CURRENT_DATE, "+hora+", ?)";
			pst =  con.prepareStatement(procesoFactStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.setInt(1, idCuenta);
			pst.setInt(2, idCuenta);
			pst.setString(3, loginUsuario);
			pst.setString(4, idSesion);
			return pst.executeUpdate();
		}
		catch(SQLException sqe){
			logger.error("############## SQLException ERROR empezarProcesoFacturacion",sqe);
		}
		catch(Exception e){
			logger.error("############## ERROR empezarProcesoFacturacion", e);
		}
		finally{
			try{
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException se) {
				logger.error("###########  Error close ResultSet - PreparedStatement", se);
			}
		}
		return 0;
	}
	
	/**
	 * Método para terminar el proceso de facturación Si el estado es null, se
	 * ejecuta no transaccional
	 * 
	 * @param con
	 * @param idCuenta
	 * @param estado
	 * @return mayor que cero (0) si se realizó correctamente el proceso de
	 *         facturación
	 */
	public static int finalizarProcesoFacturaciontransaccional(	Connection con,
																int idCuenta, String estado, String idSesion) 
	{
		if (estado == null) 
		{
			return finalizarProcesoFacturacion(con, idCuenta, idSesion);
		}
		try 
		{
			if (estado.equals(ConstantesBD.inicioTransaccion)) 
			{
				DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).beginTransaction(con);
			}
			int resultado = finalizarProcesoFacturacion(con, idCuenta, idSesion);
			if (estado.equals(ConstantesBD.finTransaccion)) 
			{
				DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).endTransaction(con);
			}
			return resultado;
		} 
		catch (SQLException e) 
		{
			logger.error("Error terminando el proceso de facturacion " + e);
			return 0;
		}
	}

	
	
	/**
	 * Método para termina el proceso de facturación
	 * 
	 * @param con
	 * @param idCuenta
	 * @return mayor que cero (0) si se realizó correctamente el proceso de
	 *         facturación
	 */
	private static int finalizarProcesoFacturacion(Connection con, int idCuenta, String idSesion) 
	{
		PreparedStatement pst=null;
		PreparedStatement pst2=null;
		ResultSet rs=null;
		try 
		{
			String verificarCuentasAsocioStr = "SELECT " +
						"ac.cuenta_inicial as cuenta, " +
						"cpf.id_sesion as idsesion " +
					"FROM " +
						"cuentas c " +
						"INNER JOIN asocios_cuenta ac ON(ac.cuenta_final=c.id) " +
						"INNER JOIN cuentas_proceso_fact cpf ON(cpf.cuenta=c.id) " +
					"WHERE " +
						"c.estado_cuenta="+ ConstantesBD.codigoEstadoCuentaProcesoFacturacion+" "+
						"AND ac.cuenta_inicial=?";
			
			String terminarProcesoFacturacionStr = "DELETE from cuentas_proceso_fact WHERE cuenta=? and id_sesion=? ";
			pst = con.prepareStatement(verificarCuentasAsocioStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.setInt(1, idCuenta);
			rs = pst.executeQuery();
			if (rs.next()) 
			{
				int cuentaHosp = rs.getInt("cuenta");
				idSesion = (rs.getString("idsesion")!=null)?rs.getString("idsesion"):"";
				cancelarProcesoFacturacionTransaccional(con, cuentaHosp,ConstantesBD.continuarTransaccion, idSesion);
				pst2 =  con.prepareStatement(terminarProcesoFacturacionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
				pst2.setInt(1, cuentaHosp);
				pst2.setString(2, idSesion);
				return pst2.executeUpdate();
			} 
			else 
			{
				//logger.info("Id Cuenta "+idCuenta);
				pst2 =  con.prepareStatement(terminarProcesoFacturacionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
				pst2.setInt(1, idCuenta);
				pst2.setString(2, idSesion);
				return pst2.executeUpdate();
			}
		}
		catch(SQLException sqe){
			logger.error("############## SQLException ERROR finalizarProcesoFacturacion",sqe);
		}
		catch(Exception e){
			logger.error("############## ERROR finalizarProcesoFacturacion", e);
		}
		finally{
			try{
				if(rs != null){
					rs.close();
				}
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException se) {
				logger.error("###########  Error close ResultSet - PreparedStatement", se);
			}
		}
		return 0;
	}
	
	/**
	 * Método para cancelar el proceso de facturación (transaccional)
	 * 
	 * @param con
	 * @param idCuenta
	 * @param estado
	 * @return numero mayor que cero (0) si se realizó correctamente la
	 *         cancelación
	 */
	public static int cancelarProcesoFacturacionTransaccional(	Connection con,
																int idCuenta, String estado, String idSesion) 
	{
		PreparedStatement pst=null;
		ResultSet rs=null;
		try{
			String cancelarProcesoFacturacionStr = "UPDATE cuentas SET " +
				"estado_cuenta=(SELECT estado FROM cuentas_proceso_fact WHERE cuenta=? and id_sesion=?) WHERE id=?";
			
			if (estado.equals(ConstantesBD.inicioTransaccion)) 
			{
				DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).beginTransaction(con);
			}
			pst =  con.prepareStatement(cancelarProcesoFacturacionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.setInt(1, idCuenta);
			pst.setString(2, idSesion);
			pst.setInt(3, idCuenta);
			
			int resultado = pst.executeUpdate();
			if (estado.equals(ConstantesBD.finTransaccion)) 
			{
				DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).endTransaction(con);
			}
			return resultado;
		}
		catch(SQLException sqe){
			logger.error("############## SQLException ERROR cancelarProcesoFacturacionTransaccional",sqe);
		}
		catch(Exception e){
			logger.error("############## ERROR cancelarProcesoFacturacionTransaccional", e);
		}
		finally{
			try{
				if(rs != null){
					rs.close();
				}
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException se) {
				logger.error("###########  Error close ResultSet - PreparedStatement", se);
			}
		}
		return 0;
	}
	
	/**
	 * metodo para cargar los detalles de la prefactura
	 * @param con
	 * @param dtoFactura
	 * @param consulta 
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static HashMap proponerPreFactura(Connection con, DtoFactura dtoFactura, String consulta)
	{
		HashMap mapaPrefactura= new HashMap();
		PreparedStatement pst=null;
		ResultSet rs=null;

		try
		{
			pst=  con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			rs=pst.executeQuery();
			int cont=0;
			mapaPrefactura.put("numRegistros","0");
			ResultSetMetaData rsm=rs.getMetaData();
			while(rs.next())
			{
				for(int i=1;i<=rsm.getColumnCount();i++)
				{
					mapaPrefactura.put((rsm.getColumnLabel(i)).toLowerCase()+"_"+cont, rs.getObject(rsm.getColumnLabel(i))==null||rs.getObject(rsm.getColumnLabel(i)).toString().equals(" ")?"":rs.getObject(rsm.getColumnLabel(i)));
				}
				cont++;
			}
			mapaPrefactura.put("numRegistros", cont+"");
			
			//MT 3463 davgommo
			//Información para obtener el tipo de codigo
			int convenio= dtoFactura.getConvenio().getCodigo();
			String obtenerTipoCodServ="Select tipo_codigo AS tipo FROM facturacion.convenios WHERE codigo="+convenio;
			String obtenerTipoCodArt="Select tipo_codigo_articulos AS tipo FROM facturacion.convenios WHERE codigo="+convenio;
			PreparedStatement pst1=null;
			ResultSet rs1=null;
			pst1=  con.prepareStatement( obtenerTipoCodServ,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			rs1=pst1.executeQuery();
			int codSer= 42;
			
			if (rs1.next())
			if (rs1.getString("tipo")!=null)
			{
		     codSer=rs1.getInt("tipo");
			} 
		    pst1=null;
		    rs1=null;
		    pst1=  con.prepareStatement(obtenerTipoCodArt,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
		    rs1=pst1.executeQuery();
		    
		    int codArt= 42;
		    if (rs1.next())
		    if (rs1.getString("tipo")!=null)
			{
			codArt= rs1.getInt("tipo");
			}
		    rs1.close();
		    pst1.close();
			// Obtenemos el tipo de codigo para servicios y articulos y cambiamos el codigo segun corresponda
			for(int w=0; w<Utilidades.convertirAEntero(mapaPrefactura.get("numRegistros").toString()); w++)
			{
			int tipodato=Integer.parseInt(mapaPrefactura.get("tipo_"+w).toString());
			if (tipodato==1) // Si es Servicio
			{
				int codigoant=Integer.parseInt(mapaPrefactura.get("codarticuloservicio_"+w).toString());
				String obtenerCodServicio= "select facturacion.getcodigosoat("+codigoant+", convenios.tipo_codigo) AS codigo FROM CONVENIOS where convenios.codigo="+convenio;
				String obtenerDescServicio= "select facturacion.getdescripcioncodigosoat("+codigoant+", convenios.tipo_codigo) AS codigo FROM CONVENIOS where convenios.codigo="+convenio;
				PreparedStatement pst2=null;
				ResultSet rs2=null;
				pst2=  con.prepareStatement( obtenerCodServicio,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
				rs2=pst2.executeQuery();
				int codNuevo=0;
				if (rs2.next())				
				if (rs2.getString("codigo")!=null) //Verificamos si el servicio tiene un codigo definido para el tipo de codigo servicio del convenio 
				{codNuevo=rs2.getInt("codigo");
				mapaPrefactura.put("codarticuloservicio_"+w, codNuevo);
				PreparedStatement pst3=null;
				ResultSet rs3=null;
				pst3=  con.prepareStatement( obtenerDescServicio,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
				rs3=pst3.executeQuery();
				rs3.next();
				String nuevoDesc=rs3.getString("codigo");
				mapaPrefactura.put("descarticuloservicio_"+w, nuevoDesc);
				rs3.close();
				pst3.close();
				}
				else { // si el servicio no tiene definido un codigo para el convenio, se busca si tiene definido uno para el tipo de codigo en el parametro codigo manual estandar servicios
					String codSerDefault=ValoresPorDefecto.getCodigoManualEstandarBusquedaServiciosLargo(dtoFactura.getInstitucion());
					String[] valorD=codSerDefault.split("@@");
					if (!codSerDefault.equals("@@"))
					{obtenerCodServicio= "select distinct facturacion.getcodigosoat("+codigoant+","+valorD[0]+") AS codigo FROM CONVENIOS where convenios.codigo="+convenio;
					obtenerDescServicio= "select facturacion.getdescripcioncodigosoat("+codigoant+","+valorD[0]+") AS codigo FROM CONVENIOS where convenios.codigo="+convenio;
					PreparedStatement pst3=null;
					ResultSet rs3=null;
					pst3=  con.prepareStatement( obtenerCodServicio,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
					rs3=pst3.executeQuery(); 
					if (rs3.next())
					if (rs3.getString("codigo")!=null) //Si no tiene codigo praa el tipo de codigo en el parametro se deja el axioma que viene por defecto
					{
					codNuevo=rs3.getInt("codigo");
					mapaPrefactura.put("codarticuloservicio_"+w, codNuevo);
					PreparedStatement pst4=null;
					ResultSet rs4=null;
					pst4=  con.prepareStatement( obtenerDescServicio,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
					rs4=pst4.executeQuery();
					rs4.next();
					String nuevoDesc=rs4.getString("codigo");
					mapaPrefactura.put("descarticuloservicio_"+w, nuevoDesc);
					rs4.close();
					pst4.close();
					}
					rs3.close();
					pst3.close();		
				}}
				rs2.close();
				pst2.close();
				
			}
			if (tipodato==2)//Si es Articulo
			{
				int codigoant=Integer.parseInt(mapaPrefactura.get("codarticuloservicio_"+w).toString());
				if (codArt==0) //Si el código definido para el convenio es CUM
				{
					String obtenerCUM= "select distinct facturacion.getcodigocumarticulo(articulo.codigo) AS codigo FROM inventarios.articulo WHERE articulo.codigo="+codigoant;
					PreparedStatement pst2=null;
					ResultSet rs2=null;
					pst2=  con.prepareStatement( obtenerCUM,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
					rs2=pst2.executeQuery();
					String codNuevo="";
					if (rs2.next())
					{codNuevo=rs2.getString("codigo");}
					rs2.close();
					pst2.close();
					mapaPrefactura.put("codarticuloservicio_"+w, codNuevo);
				}else {
					if (codArt!=1) //El codigo axioma lo trae por defecto, pero si tipo de codigo no es axioma ni cum entra a la validacion
					{
						int institucion = dtoFactura.getInstitucion();
						String codDefault=ValoresPorDefecto.getCodigoManualEstandarBusquedaArticulos(institucion);
						if (!codDefault.equals("AXM")) //si el codigo estandar no es axioma trae el codigo interfaz
						{
							
							String obtenerInterfaz= "SELECT DISTINCT facturacion.getcodigointerfaz(articulo.codigo) AS codigo FROM inventarios.articulo WHERE articulo.codigo="+codigoant;
							PreparedStatement pst2=null;
							ResultSet rs2=null;
							pst2=  con.prepareStatement( obtenerInterfaz,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
							rs2=pst2.executeQuery();
							if (rs2.next())
							if (!rs2.getString("codigo").equals("") || rs2.getString("codigo")!=null)
							{String codNuevo=rs2.getString("codigo"); 
							mapaPrefactura.put("codarticuloservicio_"+w, codNuevo);
							}
							rs2.close();
							pst2.close();
							
						}
					
					}
				}
				
			}
			}
			
			for(int w=0; w<Utilidades.convertirAEntero(mapaPrefactura.get("numRegistros").toString()); w++)
			{
				double valorTotal= Double.parseDouble(mapaPrefactura.get("valortotal_"+w).toString());
				int cantidad= Utilidades.convertirAEntero(mapaPrefactura.get("cantidadcargo_"+w).toString());
				if(cantidad>0)
					mapaPrefactura.put("valorunitario_"+w, valorTotal/cantidad);
				else
					mapaPrefactura.put("valorunitario_"+w, "0");
			}

		}
		catch(SQLException sqe){
			logger.error("############## SQLException ERROR proponerPreFactura",sqe);
		}
		catch(Exception e){
			logger.error("############## ERROR proponerPreFactura", e);
		}
		finally{
			try{
				if(rs != null){
					rs.close();
				}
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException se) {
				logger.error("###########  Error close ResultSet - PreparedStatement", se);
			}
		}
		return mapaPrefactura;
	}
	
	/**
	 *  metodo para la actualización del estado de facturacion y el estado del paciente de una factura dada
	 * @param con
	 * @param estadoFacturacion
	 * @param estadoPaciente
	 * @param codigoFactura
	 * @return
	 */
	public static boolean actualizarEstadosFactura(Connection con, int estadoFacturacion, int estadoPaciente, int codigoFactura)
	{
		String actualizarEstadosFacturaStr= "UPDATE facturas SET estado_facturacion= ?, estado_paciente=? WHERE codigo=?";
	    try
	    {
		    PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(actualizarEstadosFacturaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, estadoFacturacion);
			ps.setInt(2,estadoPaciente);
			ps.setInt(3,codigoFactura);
			if(ps.executeUpdate()>0)
			    return true;
			else
			    return false;
	    }	
	    catch(SQLException e)
		{
	        logger.error("Error en actualizarEstadosFactura de SqlBaseFacturaDao: "+e);
			return false;
		}	
	}
	
	/**
	 * obtiene el codigo - nombre del centro de atencion de una factura dada
	 * @param consecutivoFactura
	 * @param codigoInstitucion
	 * @return
	 */
	public static InfoDatosInt obtenerCentroAtencionFactura( Connection con, String consecutivoFactura, int codigoInstitucion)
	{
		String getCentroAtencionFacturaStr="select getnomcentroatencion(f.centro_aten) as nombreCentroAtencion, " +
													"f.centro_aten as codigoCentroAtencion " +
													"FROM facturas f " +
													"where f.consecutivo_factura=? and f.institucion=? ";
		
		InfoDatosInt centroAtencion= new InfoDatosInt();
		try
		{
			PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(getCentroAtencionFacturaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, Utilidades.convertirAEntero(consecutivoFactura));
			ps.setInt(2, codigoInstitucion);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				centroAtencion.setCodigo(rs.getInt("codigoCentroAtencion"));
				centroAtencion.setNombre(rs.getString("nombreCentroAtencion"));
			}
		}
		catch(SQLException e)
		{
			logger.warn("Error en el update del numero de la cuenta de cobro en la factura " +e.toString());
		}
		logger.info("CNETRO ATENCION-->"+centroAtencion.getNombre());
		return centroAtencion;
	}
	
	
	
	/**
	 * obtiene el codigo - nombre del centro de atencion de una factura dada
	 * @param consecutivoFactura
	 * @param codigoInstitucion
	 * @return
	 */
	public static InfoDatosInt obtenerCentroAtencionFactura( Connection con, int codigoFactura)
	{
		String getCentroAtencionFacturaStr="select getnomcentroatencion(f.centro_aten) as nombreCentroAtencion, " +
													"f.centro_aten as codigoCentroAtencion from facturas f where f.codigo=? ";
		InfoDatosInt centroAtencion= new InfoDatosInt();
		try
		{
			PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(getCentroAtencionFacturaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, codigoFactura);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				centroAtencion.setCodigo(rs.getInt("codigoCentroAtencion"));
				centroAtencion.setNombre(rs.getString("nombreCentroAtencion"));
			}
		}
		catch(SQLException e)
		{
			logger.warn("Error en el update del numero de la cuenta de cobro en la factura " +e.toString());
		}
		logger.info("CNETRO ATENCION-->"+centroAtencion.getNombre());
		return centroAtencion;
	}
	
	/**
	 * Adición Sebastián Método que consulta el número de cuenta de cobro
	 * teniendo como referencia el consecutivo de la factura
	 * 
	 * @param con
	 * @param consecutivoFactura
	 * @param institucion
	 * @return numero de cuenta de cobro
	 */
	public static double obtenerCuentaCobro(	Connection con,
												int consecutivoFactura,
												int institucion) 
	{
		String obtenerCuentaCobroStr = "SELECT numero_cuenta_cobro AS numero_cuenta_cobro FROM facturas WHERE consecutivo_factura=? AND institucion=?";
		try 
		{
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(obtenerCuentaCobroStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1, consecutivoFactura);
			pst.setInt(2,institucion);
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			if (rs.next()) 
			{
				//en el caso de que la cuenta de cobro de la factura sea nulo
				if (rs.getString("numero_cuenta_cobro") == null) 
				{
					return -1;
				} 
				else 
				{
					return rs.getDouble("numero_cuenta_cobro");
				}
			} 
			else 
			{
				return -1;
			}
		} 
		catch (SQLException e) 
		{
			logger.error("Error obteniendo la Cuenta de Cobro SqlBaseFacturaDao=> "	+ e);
			return -1;
		}
	}
	
	

	/**
	 * Método que consulta el número de cuenta de cobro
	 * teniendo como referencia el codigo de la factura
	 * 
	 * @param con
	 * @param codigoFactura
	 * @return numero de cuenta de cobro
	 */
	public static double obtenerCuentaCobro(	Connection con,int codigoFactura) 
	{
		String obtenerCuentaCobroStr = "SELECT numero_cuenta_cobro AS numero_cuenta_cobro FROM facturas WHERE codigo=?";
		try 
		{
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(obtenerCuentaCobroStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1, codigoFactura);
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			if (rs.next()) 
			{
				//en el caso de que la cuenta de cobro de la factura sea nulo
				if (rs.getString("numero_cuenta_cobro") == null) 
				{
					return -1;
				} 
				else 
				{
					return rs.getDouble("numero_cuenta_cobro");
				}
			} 
			else 
			{
				return -1;
			}
		} 
		catch (SQLException e) 
		{
			logger.error("Error obteniendo la Cuenta de Cobro SqlBaseFacturaDao=> "	+ e);
			return -1;
		}
	}
	
	/**
	 * Metodo que realiza la busqueda de una factura por su consecutivo y cod de institucion
	 * @param con
	 * @param consecutivoFactura
	 * @param codigoInstitucion
	 * @return
	 * @throws SQLException
	 */
	public static  ResultSetDecorator busquedaPorConsecutivoDianEInstitucion(		Connection con,
																			String consecutivoFactura,
																			int codigoInstitucion,
																			String restricciones) 
	{
		String busquedaPorConsecutivoDianEInstitucionStr= 	"SELECT codigo, " +
																"consecutivofactura, " +
																"valortotal," +
																"fechahoraelaboracion, " +
																"viaingreso, " +
																"nombreviaingreso, " +
																"codigoresponsable, " +
																"esresponsableparticular, " +
																"nombreresponsable, " +
																"codigopaciente, " +
																"nombrepaciente, " +
																"codigoestadofacturacion, " +
																"nombreestadofacturacion, " +
																"codigoestadopaciente, " +
																"nombreestadopaciente, " +
																"nomempresainstitucion as nombreempresa," +
																"institucion " +
																"FROM " +
																"view_facturas_internas " +
																"WHERE " +
																"consecutivofactura=? " +
																"AND institucion=?";
		ResultSetDecorator respuesta=null;
		try
		{
			PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(busquedaPorConsecutivoDianEInstitucionStr+" "+restricciones,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, Utilidades.convertirAEntero(consecutivoFactura));
			ps.setInt(2, codigoInstitucion);
			logger.info(busquedaPorConsecutivoDianEInstitucionStr+" "+restricciones+"--->consFact="+consecutivoFactura+"-->ins-->"+codigoInstitucion+"\n");
			respuesta=new ResultSetDecorator(ps.executeQuery());
		}
		catch(SQLException e)
		{
			logger.warn("Error en  busquedaPorConsecutivoDianEInstitucion " +e.toString());
			respuesta=null;
		}
		return respuesta;
	}
	
	/**
	 * obtiene el valor neto paciente
	 * @param con
	 * @param consecutivoFactura
	 * @param codigoInstitucion
	 * @return
	 */
	public static double obtenerValorNetoPaciente(Connection con, String consecutivoFactura, int codigoInstitucion)
	{
		String getValorNetoPacienteStr="select valor_neto_paciente AS valor from facturas where consecutivo_factura=? and institucion=?";
		try
		{
			PreparedStatementDecorator statement= new PreparedStatementDecorator(con.prepareStatement(getValorNetoPacienteStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			statement.setInt(1, Utilidades.convertirAEntero(consecutivoFactura));
			statement.setInt(2, codigoInstitucion);
			ResultSetDecorator resultado=new ResultSetDecorator(statement.executeQuery());
			if(resultado.next())
				return resultado.getDouble("valor");
			
		}
		catch (SQLException e)
		{
			logger.error("Error getValorNetoPaciente (SqlBaseFacturaDao): "+e);
		}
		return -1;
	}
	
	/**
	 * Método que cancela todos los procesos de facturación en proceso
	 * 
	 * @param con
	 *            Conexión con la BD
	 * @return numero de cancelaciones
	 */
	public static int cancelarTodosLosProcesosDeFacturacion(Connection con) 
	{
		String cuentasEnProcesoStr = "SELECT cuenta AS cuentas, id_sesion as idsesion FROM facturacion.cuentas_proceso_fact";
		try 
		{
			DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).beginTransaction(con);
			PreparedStatementDecorator cuentasEnProceso =  new PreparedStatementDecorator(con.prepareStatement(cuentasEnProcesoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator cuentas = new ResultSetDecorator(cuentasEnProceso.executeQuery());
			int cancelados = 0;
			while (cuentas.next()) 
			{
				int idCuenta = cuentas.getInt("cuentas");
				String idSesion= cuentas.getString("idsesion");
				cancelados += cancelarProcesoFacturacionTransaccional(con,idCuenta, ConstantesBD.continuarTransaccion, idSesion);
				finalizarProcesoFacturaciontransaccional(con, idCuenta,ConstantesBD.continuarTransaccion, idSesion);
			}
			DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).endTransaction(con);
			return cancelados;
		}
		catch (SQLException e) 
		{
			try 
			{
				DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).abortTransaction(con);
			} 
			catch (SQLException e1) 
			{
				logger.error("Error abortando la transacción " + e1);
				return 0;
			}
			logger.error("Error consultando cuentas en procesos de facturación "	+ e);
			return 0;
		}
	}

	/**
	 * 
	 * @param con
	 * @param codigoFactura
	 * @return
	 */
	public static int obtenerContratoFactura(Connection con, int codigoFactura)
	{
		String consulta="SELECT sc.contrato as codcontrato FROM facturas f " +
											"INNER JOIN sub_cuentas sc ON (f.sub_cuenta=sc.sub_cuenta) WHERE f.codigo=?";
		try
		{
			PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, codigoFactura);
			logger.info("obtenerContratoFactura->"+consulta+" ->codigo->"+codigoFactura);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
				return rs.getInt("codcontrato");
		}
		catch(SQLException e)
		{
			logger.warn("Error en  obtenerContratoFactura " +e.toString());
		}
		return ConstantesBD.codigoNuncaValido;
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoFactura
	 * @param cargarDetalles
	 * @return
	 */
	public static DtoFactura cargarFactura( Connection con, String codigoFactura, boolean cargarDetalles)
	{
		DtoFactura dtoFactura= new DtoFactura();
		try
		{
			PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(cargarFacturaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			logger.info("CARGAR FACTURA-->"+cargarFacturaStr+" cod->"+codigoFactura);
			ps.setInt(1, Utilidades.convertirAEntero(codigoFactura));
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				dtoFactura.setAjustesCredito(rs.getDouble("ajustesCredito"));
				dtoFactura.setAjustesDebito(rs.getDouble("ajustesDebito"));
				dtoFactura.setCentroAtencion(new InfoDatosInt(rs.getInt("codigoCentroAtencion"), rs.getString("nombreCentroAtencion")));
				dtoFactura.setCodigo(Utilidades.convertirAEntero(codigoFactura));
				dtoFactura.setCodigoPaciente(rs.getInt("codigoPaciente"));
				dtoFactura.setCodigoResonsableParticular(rs.getInt("codigoResParticular"));
				dtoFactura.setConsecutivoFactura(rs.getDouble("consecutivoFactura"));
				dtoFactura.setConvenio(new InfoDatosInt(rs.getInt("codigoConvenio"), rs.getString("nombreConvenio")));
				dtoFactura.setCuentaCobroCapitacion(rs.getDouble("numeroCuentaCobro"));
				Vector cuentas = new Vector();
				cuentas.add(rs.getString("cuenta"));
				dtoFactura.setCuentas(cuentas);
				dtoFactura.setDiagnosticoEgresoAcronimoTipoCie("");
				dtoFactura.setEstadoFacturacion(new InfoDatosInt(rs.getInt("codigoEstadoFacturacion"), rs.getString("nombreEstadoFacturacion")));
				dtoFactura.setEstadoPaciente(new InfoDatosInt(rs.getInt("codigoEstadoPaciente"), rs.getString("nombreEstadoPaciente")));
				dtoFactura.setEstratoSocial(new InfoDatosInt(rs.getInt("codigoEstrato"), rs.getString("nombreEstrato")));
				dtoFactura.setFacturaCerrada(UtilidadTexto.getBoolean(rs.getString("facturaCerrada")));
				dtoFactura.setFecha(rs.getString("fecha"));
				dtoFactura.setHora(rs.getString("hora"));
				dtoFactura.setFormatoImpresion(new InfoDatosInt());
				dtoFactura.setInstitucion(rs.getInt("codigoInstitucion"));
				dtoFactura.setLoginUsuario(rs.getString("loginUsuario"));
				dtoFactura.setMontoCobro(rs.getInt("codigoMontoCobro"));
				dtoFactura.setNroComprobante(rs.getDouble("numeroComprobante"));
				dtoFactura.setNumeroCuentaCobro(rs.getDouble("numeroCuentaCobro"));
				//dtoFactura.setPorcentajeMonto(rs.getDouble("porcentajeMonto"));
				dtoFactura.setPrefijoFactura(rs.getString("prefijoFactura"));
				dtoFactura.setSubCuenta(rs.getDouble("subCuenta"));
				dtoFactura.setTipoAfiliado(rs.getString("tipoAfiliado"));
				dtoFactura.setTipoComprobante(rs.getString("tipoComprobante"));
				dtoFactura.setTipoFacturaSistema(UtilidadTexto.getBoolean(rs.getString("tipoFacturaSistema")));
				dtoFactura.setTipoMonto(new InfoDatosInt(rs.getInt("codigoTipoMonto")));
				dtoFactura.setTipoRegimen(new InfoDatos());
				dtoFactura.setValorAbonos(rs.getDouble("valorAbonos"));
				dtoFactura.setValorAFavorConvenio(rs.getDouble("valorAbonos"));
				dtoFactura.setValorLiquidadoPaciente(rs.getDouble("valorLiquidadoPaciente"));
				dtoFactura.setValorBrutoPac(rs.getDouble("valorBrutoPaciente"));
				dtoFactura.setValorCartera(rs.getDouble("valorCartera"));
				dtoFactura.setValorConvenio(rs.getDouble("valorConvenio"));
				dtoFactura.setValorDescuentoPaciente(rs.getDouble("valorDescuentoPaciente"));
				//dtoFactura.setValorMonto(rs.getDouble("valorMonto"));
				dtoFactura.setValorNetoPaciente(rs.getDouble("valorNetoPaciente"));
				dtoFactura.setValorPagos(rs.getDouble("valorPagos"));
				dtoFactura.setValorTotal(rs.getDouble("valorTotal"));
				dtoFactura.setViaIngreso(new InfoDatosInt(rs.getInt("codigoViaIngreso"), rs.getString("nombreViaIngreso")));
				dtoFactura.setIdPaciente(rs.getString("idpaciente"));
				dtoFactura.setNombrePaciente(rs.getString("nombrepaciente"));
				dtoFactura.setResolucionDian(rs.getString("resolucionDian"));
				dtoFactura.setRangoInicialFactura(rs.getDouble("rangoInicial"));
				dtoFactura.setRangoFinalFactura(rs.getDouble("rangoFinal"));
				
				dtoFactura.setEntidadSubcontratada(rs.getDouble("entidadsubcontratada"));
				dtoFactura.setEmpresaInstitucion(rs.getDouble("empresainstitucion"));
				dtoFactura.setPacienteEntidadSubcontratada(rs.getDouble("pacienteentidadsubcontratada"));
				
				dtoFactura.setTopeFacturacion(rs.getInt("topefacturacion"));
				dtoFactura.setFechaVigenciaInicialMontoCobro(rs.getString("fechaviginimontocobro"));
				dtoFactura.setFechaVigenciaInicialTopeFacturacion(rs.getString("fechaviginitopefact"));
				
				dtoFactura.setCentroAtencionDuenio(new InfoDatosInt(rs.getInt("centroatencionduenio"), rs.getString("nombrecentroatencionduenio")));
				dtoFactura.setValorAnticipos(rs.getDouble("valoranticipo"));
				
				//ahora cargamos los detalles de la factura
				if(cargarDetalles)
					dtoFactura.setDetallesFactura(cargarDetallesFactura(con, codigoFactura));
				
			}
			rs.close();
			ps.close();
		}
		catch(SQLException e)
		{
			logger.warn("Error en el cargar factura ");
			e.printStackTrace();
		}
		
		return dtoFactura;
	}
	
	
	/**
	 * 
	 * @param con
	 * @param codigoFactura
	 * @return
	 */
	private static ArrayList<DtoDetalleFactura> cargarDetallesFactura(Connection con, String codigoFactura) 
	{
		ArrayList<DtoDetalleFactura> detalleFacturaArray= new ArrayList<DtoDetalleFactura>();
		try
		{
			PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(cargarDetallesFacturaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			logger.info("CARGAR DET FACTURA-->"+cargarDetallesFacturaStr+" cod->"+codigoFactura);
			ps.setInt(1, Utilidades.convertirAEntero(codigoFactura));
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			{
				DtoDetalleFactura dtoDetalleFactura= new DtoDetalleFactura();
				dtoDetalleFactura.setAjusteCreditoMedico(rs.getDouble("ajustescreditomedico"));
				dtoDetalleFactura.setAjusteDebitoMedico(rs.getDouble("ajusteDebitoMedico"));
				dtoDetalleFactura.setAjustesCredito(rs.getDouble("ajustescredito"));
				dtoDetalleFactura.setAjustesDebito(rs.getDouble("ajustesdebito"));
				dtoDetalleFactura.setCantidadCargo(rs.getInt("cantidadcargo"));
				dtoDetalleFactura.setCodigo(rs.getInt("codigo"));
				dtoDetalleFactura.setCodigoArticulo(rs.getInt("articulo"));
				dtoDetalleFactura.setCodigoFactura(Utilidades.convertirAEntero(codigoFactura));
				dtoDetalleFactura.setCodigoMedico(rs.getInt("codigomedico"));
				dtoDetalleFactura.setCodigoServicio(rs.getInt("servicio"));
				dtoDetalleFactura.setCodigoTipoCargo(rs.getInt("tipocargo"));
				dtoDetalleFactura.setCodigoTipoSolicitud(rs.getInt("tiposolicitud"));
				if(dtoDetalleFactura.getCodigoTipoSolicitud()==ConstantesBD.codigoTipoSolicitudCirugia)
					dtoDetalleFactura.setEsCx(true);
				else
					dtoDetalleFactura.setEsCx(false);
				dtoDetalleFactura.setNumeroSolicitud(rs.getInt("solicitud"));
				dtoDetalleFactura.setPool(rs.getInt("pool"));
				dtoDetalleFactura.setPorcentajeMedico(rs.getDouble("porcentajemedico"));
				dtoDetalleFactura.setPorcentajePool(rs.getInt("porcentajepool"));
				dtoDetalleFactura.setSolicitudPago(rs.getInt("solicitudpago"));
				dtoDetalleFactura.setValorCargo(rs.getDouble("valorcargo"));
				dtoDetalleFactura.setValorConsumoPaquete(rs.getDouble("valorconsumopaquete"));
				dtoDetalleFactura.setValorDescuentoComercial(rs.getDouble("valordctocomercial"));
				dtoDetalleFactura.setValorIva(rs.getDouble("valoriva"));
				dtoDetalleFactura.setValorMedico(rs.getDouble("valormedico"));
				dtoDetalleFactura.setValorPool(rs.getDouble("valorpool"));
				dtoDetalleFactura.setValorRecargo(rs.getDouble("valorrecargo"));
				dtoDetalleFactura.setValorTotal(rs.getDouble("valortotal"));
				dtoDetalleFactura.setCodigoEsquemaTarifario(rs.getInt("esquematarifario"));
				
				//cargamos la informacion de los asocios para el caso de cx
				if(dtoDetalleFactura.getEsCx())
				{
					dtoDetalleFactura.setAsociosDetalleFactura(cargarAsociosDetalleFactura(con, dtoDetalleFactura.getCodigo()));
				}
				//cargamos la informacion de los paquetes
				if(dtoDetalleFactura.getCodigoTipoSolicitud()==ConstantesBD.codigoTipoSolicitudPaquetes)
				{
					dtoDetalleFactura.setPaquetizacionDetalleFactura(cargarPaquetizacionDetalleFactura(con, dtoDetalleFactura.getCodigo()));
				}
				dtoDetalleFactura.setPrograma(new InfoDatosDouble(rs.getDouble("codprograma"), rs.getString("nomprograma")));
				
				dtoDetalleFactura.setValorDctoOdo(rs.getBigDecimal("valor_dcto_odo"));
				dtoDetalleFactura.setValorDctoBono(rs.getBigDecimal("valor_dcto_bono"));
				dtoDetalleFactura.setValorDctoProm(rs.getBigDecimal("valor_dcto_prom"));
				dtoDetalleFactura.setDetallePaqueteOdonConvenio(rs.getInt("det_paq_odon_convenio"));
				
				detalleFacturaArray.add(dtoDetalleFactura);
			}
			rs.close();
			ps.close();
		}
		catch(SQLException e)
		{
			logger.warn("Error en el cargar factura ");
			e.printStackTrace();
		}
		
		return detalleFacturaArray;
	}

	/**
	 * 
	 * @param con
	 * @param codigoDetalle
	 * @return
	 */
	private static ArrayList<DtoPaquetizacionDetalleFactura> cargarPaquetizacionDetalleFactura(Connection con, int codigoDetalle) 
	{
		ArrayList<DtoPaquetizacionDetalleFactura> detallePaquetizacionArray= new ArrayList<DtoPaquetizacionDetalleFactura>();
		try
		{
			PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(cargarPaquetizacionDetfactStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			logger.info("CARGAR Paq-->"+cargarPaquetizacionDetfactStr+" cod->"+codigoDetalle);
			ps.setInt(1, codigoDetalle);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			{
				DtoPaquetizacionDetalleFactura dtoPaquetizacion= new DtoPaquetizacionDetalleFactura();
				dtoPaquetizacion.setAjusteCreditoMedico(rs.getDouble("ajustecreditomedico"));
				dtoPaquetizacion.setAjusteDebitoMedico(rs.getDouble("ajustedebitomedico"));
				dtoPaquetizacion.setAjustesCredito(rs.getDouble("ajustescredito"));
				dtoPaquetizacion.setAjustesDebito(rs.getDouble("ajustesdebito"));
				dtoPaquetizacion.setArticulo(new InfoDatosInt(rs.getInt("articulo")));
				dtoPaquetizacion.setCodigo(rs.getDouble("codigo"));
				dtoPaquetizacion.setCodigoDetalleFactura(codigoDetalle);
				dtoPaquetizacion.setServicio(new InfoDatosInt(rs.getInt("servicio")));
				dtoPaquetizacion.setValorCargo(rs.getDouble("valorcargo"));
				dtoPaquetizacion.setValorDifConsumoValor(rs.getDouble("valordifconsumovalor"));
				dtoPaquetizacion.setValorIva(rs.getDouble("valoriva"));
				dtoPaquetizacion.setValorRecargo(rs.getDouble("valorrecargo"));
				dtoPaquetizacion.setValorTotal(rs.getDouble("valortotal")); 
				dtoPaquetizacion.setPorcentajeMedico(rs.getDouble("porcentajemedico"));
				dtoPaquetizacion.setPorcentajePool(rs.getDouble("porcentajepool"));
				dtoPaquetizacion.setSolicitud(rs.getInt("numerosolicitud"));
				dtoPaquetizacion.setCodigoEsquemaTarifario(rs.getInt("esquematarifario"));
				dtoPaquetizacion.setCodigoEspecialidadAsocio(rs.getInt("especialidadasocio"));
				dtoPaquetizacion.setCodigoMedicoAsocio(rs.getInt("medicoasocio"));
				
				detallePaquetizacionArray.add(dtoPaquetizacion);
			}
			ps.close();
			rs.close();
		}
		catch(SQLException e)
		{
			logger.warn("Error en el cargar factura ");
			e.printStackTrace();
		}
		return detallePaquetizacionArray;
	}

	/**
	 * 
	 * @param con
	 * @param codigo
	 * @return
	 */
	public static ArrayList<DtoAsociosDetalleFactura> cargarAsociosDetalleFactura(Connection con, int codigoDetalle) 
	{
		ArrayList<DtoAsociosDetalleFactura> detalleAsociosArray= new ArrayList<DtoAsociosDetalleFactura>();
		try
		{
			PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(cargarAsociosDetalleFacturaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			logger.info("CARGAR Asocios-->"+cargarAsociosDetalleFacturaStr+" cod->"+codigoDetalle);
			ps.setInt(1, codigoDetalle);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			{
				DtoAsociosDetalleFactura dtoAsocios= new DtoAsociosDetalleFactura();
				dtoAsocios.setAjusteCreditoMedico(rs.getDouble("ajustecreditomedico"));
				dtoAsocios.setAjusteDebitoMedico(rs.getDouble("ajustedebitomedico"));
				dtoAsocios.setAjustesCredito(rs.getDouble("ajustescredito"));
				dtoAsocios.setAjustesDebito(rs.getDouble("ajustesDebito"));
				dtoAsocios.setConsecutivo(rs.getInt("consecutivo"));
				dtoAsocios.setCodigo(codigoDetalle);
				dtoAsocios.setCodigoEsquemaTarifario(rs.getInt("esquematarifario"));
				dtoAsocios.setCodigoMedico(rs.getInt("codigomedico"));
				dtoAsocios.setCodigoServicioAsocio(rs.getInt("servicio"));
				dtoAsocios.setPool(rs.getInt("pool"));
				dtoAsocios.setPorcentajeMedico(rs.getDouble("porcentajemedico"));
				dtoAsocios.setPorcentajePool(rs.getInt("porcentajepool"));
				dtoAsocios.setTipoAsocio(rs.getInt("tipoasocio"));				
				dtoAsocios.setNombreTipoAsocio(rs.getString("nombretipoasocio"));
				dtoAsocios.setValorAsocio(rs.getDouble("valorasocio"));
				dtoAsocios.setValorCargo(rs.getDouble("valorcargo"));
				dtoAsocios.setValorIva(rs.getDouble("valoriva"));
				dtoAsocios.setValorMedico(rs.getDouble("valormedico"));
				dtoAsocios.setValorPool(rs.getDouble("valorpool"));
				dtoAsocios.setValorRecargo(rs.getDouble("valorrecargo"));
				dtoAsocios.setValorTotal(rs.getDouble("valortotal"));
				dtoAsocios.setCodigoMedicoAsocio(rs.getInt("codigomedicoasocio"));
				dtoAsocios.setCodigoEspecialidadMedicoAsocio(rs.getInt("codigoespecialidadasocio"));
				dtoAsocios.setCodigo(rs.getInt("codigo"));
				dtoAsocios.setTipoServicio(rs.getString("tiposervicio"));
				dtoAsocios.setCodigoPropietarioAsocio(rs.getString("codigopropietario"));
				
				
				logger.info("\n\ntiposervicio::: "+dtoAsocios.getTipoServicio());
				
				dtoAsocios.setDetalleAsociosArray(cargarDetalleAsocios(con, dtoAsocios.getConsecutivo()));
				
				detalleAsociosArray.add(dtoAsocios);
			}
			rs.close();
			ps.close();
		}
		catch(SQLException e)
		{
			logger.warn("Error en el cargar factura ");
			e.printStackTrace();
		}
		return detalleAsociosArray;
	}

	/**
	 * 
	 * @param con
	 * @param codigo
	 * @return
	 */
	private static ArrayList<DtoDetAsociosDetFactura> cargarDetalleAsocios(Connection con, int codigoAsocio) 
	{
		ArrayList<DtoDetAsociosDetFactura> detalleAsociosArray= new ArrayList<DtoDetAsociosDetFactura>();
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cargarDetalleAsociosStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, codigoAsocio);
			ResultSetDecorator rs= new ResultSetDecorator(ps.executeQuery());
			logger.info("CARGAR Asocios-->"+cargarDetalleAsociosStr+" cod->"+codigoAsocio);
			
			while(rs.next())
			{
				DtoDetAsociosDetFactura dto= new DtoDetAsociosDetFactura();
				dto.setCodigo(rs.getDouble("codigo"));
				dto.setArticulo(rs.getInt("articulo"));
				dto.setDescArticulo(rs.getString("descarticulo"));
				dto.setAsocioDetFactura(codigoAsocio);
				dto.setCantidad(rs.getInt("cantidad"));
				dto.setValorTotal(rs.getDouble("valortotal"));
				dto.setValorUnitario(rs.getDouble("valorunitario"));
							
				detalleAsociosArray.add(dto);
			}			
			rs.close();
			ps.close();
		}
		catch(SQLException e)
		{
			logger.warn("Error en el cargar factura ");
			e.printStackTrace();
		}
		return detalleAsociosArray;
	
		
	}

	/**
	 * 
	 * @param con
	 * @param codigoFactura
	 * @return
	 */
	public static double obtenerValorConvenioFactura(Connection con, String codigoFactura)
	{
		String consulta="SELECT f.valor_convenio as vconv FROM facturas f where f.codigo="+codigoFactura;
		try
		{
			PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			logger.info("obtenerValorConvenioFactura->"+consulta+" ->codigo->"+codigoFactura);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
				return rs.getDouble("vconv");
		}
		catch(SQLException e)
		{
			logger.warn("Error en  obtenerValorConvenioFactura " +e.toString());
		}
		return ConstantesBD.codigoNuncaValido;
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoFactura
	 * @return
	 */
	public static int obtenerCodigoPacienteFactura(Connection con, String codigoFactura)
	{
		String consulta="SELECT c.codigo_paciente as codigopac FROM facturas f INNER JOIN cuentas c ON (f.cuenta=c.id) WHERE f.codigo="+codigoFactura;
		try
		{
			PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			logger.info("obtenerCodigoPacienteFactura->"+consulta+" ->codigo->"+codigoFactura);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
				return rs.getInt("codigopac");
		}
		catch(SQLException e)
		{
			logger.warn("Error en  obtenerCodigoPacienteFactura " +e.toString());
		}
		return ConstantesBD.codigoNuncaValido;
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoFactura
	 * @return
	 */
	public static String obtenerIdIngresoFactura( Connection con, String codigoFactura)
	{
		String consulta="SELECT c.id_ingreso as idingreso FROM facturas f INNER JOIN cuentas c ON (f.cuenta=c.id) WHERE f.codigo="+codigoFactura;
		try
		{
			PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			logger.info("obtenerIdIngresoFactura->"+consulta+" ->codigo->"+codigoFactura);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
				return rs.getString("idingreso");
		}
		catch(SQLException e)
		{
			logger.warn("Error en  obtenerIdIngresoFactura " +e.toString());
		}
		return "";
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoFactura
	 * @return
	 */
	public static int obtenerFormatoFacturaXCodigoFact(Connection con, String codigoFactura)
	{
		String consulta="SELECT coalesce(c.formato_factura,"+ConstantesBD.codigoNuncaValido+") as formato FROM facturas f INNER JOIN convenios c ON (f.convenio=c.codigo) WHERE f.codigo="+codigoFactura;
		try
		{
			PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			logger.info("obtenerFormatoFacturaXCodigoFact->"+consulta+" ->codigo->"+codigoFactura);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
				return rs.getInt("formato");
		}
		catch(SQLException e)
		{
			logger.warn("Error en  obtenerIdIngresoFactura " +e.toString());
			e.printStackTrace();
		}
		return ConstantesBD.codigoFormatoImpresionEstandar;
	}
	
	/**
	 * actualiza el numero de la cuenta de cobro  en la factura
	 * @param con
	 * @param numeroCuentaCobroCapitada
	 * @param codigoFactura
	 * @return
	 */
	public static boolean updateNumeroCuentaCobroCapitadaEnFactura(Connection con, String numeroCuentaCobroCapitada, String codigoFactura)
	{
		String updateStr="update facturas set cuenta_cobro_capitacion=? where codigo="+codigoFactura;
		try
		{
			PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(updateStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setDouble(1, Utilidades.convertirADouble(numeroCuentaCobroCapitada));
			if(ps.executeUpdate()>0)
				return true;
		}
		catch(SQLException e)
		{
			logger.warn("Error en el update del numero de la cuenta de cobro en la factura " +e.toString());
		}
		return false;
	}
	
	/**
	 * Adición Sebastián Método usado para desasignar el numero de cuenta de
	 * cobro de una factura en el caso de que se haya hehco una inactivación de
	 * factura
	 * 
	 * @param con
	 * @param codigoFactura
	 * @param institucion
	 * @return
	 */
	public static int desasignarCuentaCobro(Connection con, String codigoFactura,int institucion)
	{
		String consulta=" UPDATE facturas SET numero_cuenta_cobro=? WHERE codigo=? AND institucion=?";
		try 
		{
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setNull(1, Types.DOUBLE);
			pst.setInt(2, Utilidades.convertirAEntero(codigoFactura));
			pst.setInt(3, institucion);
			
			return pst.executeUpdate();
		} 
		catch (SQLException e) 
		{
			logger.error("Error deasignando la cuenta de cobro de la factura SqlBaseFacturaDao");
			e.printStackTrace();
		}
		return ConstantesBD.codigoNuncaValido;
	}

	/**
	 * 
	 * @param con
	 * @param subCuenta
	 * @return
	 */
	public static boolean insertarHistoricoSubCuenta(Connection con, double subCuenta, double codigoFactura)
	{
		String consulta=" INSERT INTO historico_subcuentas (" +
														" codigo_factura, " +//1
														" sub_cuenta," +   //1
														" convenio," + //2
														" naturaleza_paciente," + //3
														" monto_cobro," + //4
														" nro_poliza," + //5
														" nro_carnet," + //6
														" contrato," + //7
														" ingreso," + //8
														" tipo_afiliado," + //9
														" clasificacion_socioeconomica," + //10
														" nro_autorizacion," + //11
														" fecha_afiliacion," + //12
														" semanas_cotizacion," + //13
														" codigo_paciente," + //14
														" valor_utilizado_soat," + //15
														" nro_prioridad," + //18
														" facturado," + //19
														" fecha_modifica," + //22
														" hora_modifica," + //23
														" usuario_modifica" + //24
														" ) " +
														" (" +
														" select " +
														""+(long)codigoFactura+", "+//1
														" sub_cuenta, " +//1
														" convenio, " +//2
														" naturaleza_paciente," +//3
														" monto_cobro," + //4
														" nro_poliza," + //5
														" nro_carnet," + //6
														" contrato," + //7
														" ingreso," + //8
														" tipo_afiliado," + //9
														" clasificacion_socioeconomica," + //10
														" nro_autorizacion," + //11
														" fecha_afiliacion," + //12
														" semanas_cotizacion," + //13
														" codigo_paciente," + //14
														" valor_utilizado_soat," + //15
														" nro_prioridad," + //18
														" facturado," + //19
														" CURRENT_DATE, " + //22
														" "+ValoresPorDefecto.getSentenciaHoraActualBD()+"," + //23
														" usuario_modifica " +//24
														" FROM sub_cuentas " +
														" WHERE " +
														" sub_cuenta= "+(long)subCuenta+" " + 
														" )";
		
		try 
		{
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			logger.info("\ninsertarHistoricoSubCuenta*->"+consulta);
			if(pst.executeUpdate()>0)
			{
				//como inserto entonces ahora hacemos otro insert del filtro_distribucion, con la diferencia que este puede o no existir
				insertarHistoricoFiltroDistribucion(con, subCuenta, codigoFactura);
				return true;
			}
				
		} 
		catch (SQLException e) 
		{
			logger.error("Error insertarClonSubcuenta SqlBaseFacturaDao");
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 
	 * @param con
	 * @param subCuenta
	 * @param codigoFactura
	 * @return
	 */
	private static boolean insertarHistoricoFiltroDistribucion(Connection con, double subCuenta, double codigoFactura)
	{
		String consulta= "INSERT INTO historico_filtro_distribucion (" +
																		"codigo_factura, "+
																		"sub_cuenta, " +
																		"via_ingreso, " +
																		"centro_costo_solicita, " +
																		"centro_costo_ejecuta, " +
																		"fecha_inicial_solicitud, " +
																		"fecha_final_solicitud " +
																	")" +
																	"(" +
																		"select " +
																		""+(long)codigoFactura+", "+
																		"sub_cuenta, " +
																		"via_ingreso, " +
																		"centro_costo_solicita, " +
																		"centro_costo_ejecuta, " +
																		"fecha_inicial_solicitud, " +
																		"fecha_final_solicitud " +
																		"from " +
																		"filtro_distribucion " +
																		"where " +
																		"sub_cuenta="+(long)subCuenta+" " +
																	")"; 
		try 
		{
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			logger.info("\ninsertarHistoricoFiltroDistribucion*->"+consulta);
			pst.executeUpdate();
			return true;	
		} 
		catch (SQLException e) 
		{
			logger.error("Error insertarHistoricoFiltroDistribucion SqlBaseFacturaDao");
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * Metodo que realiza la busqueda de una factura por su codigo
	 * @param con
	 * @param codigoFactura
	 * @return
	 * @throws SQLException
	 */
	public static  ResultSetDecorator busquedaPorCodigo(		Connection con,
																			int codigoFactura,
																			String restricciones) 
	{
		String busquedaPorCodigoStr= 	"SELECT codigo, " +
																"consecutivofactura, " +
																"valortotal," +
																"fechahoraelaboracion, " +
																"viaingreso, " +
																"nombreviaingreso, " +
																"codigoresponsable, " +
																"esresponsableparticular, " +
																"nombreresponsable, " +
																"codigopaciente, " +
																"nombrepaciente, " +
																"codigoestadofacturacion, " +
																"nombreestadofacturacion, " +
																"codigoestadopaciente, " +
																"nombreestadopaciente, " +
																"nomempresainstitucion as nombreempresa, " +
																"descentidadsubcontratada as descentidadsubcontratada, " +
																"institucion " +
																"FROM " +
																"view_facturas_internas " +
																"WHERE " +
																"codigo=? ";
		ResultSetDecorator respuesta=null;
		try
		{
			PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(busquedaPorCodigoStr+" "+restricciones,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			logger.info("consulta -->"+(busquedaPorCodigoStr+" "+restricciones).replace("?", codigoFactura+""));
			ps.setInt(1, codigoFactura);
			respuesta=new ResultSetDecorator(ps.executeQuery());
		}
		catch(SQLException e)
		{
			logger.warn("Error en  busquedaPorConsecutivoDianEInstitucion " +e.toString());
			respuesta=null;
		}
		return respuesta;
	}

	/**
	 * 
	 * @param con
	 * @param codigoFactura
	 * @param consecutivo
	 * @return
	 */
	public static boolean actualizarConsecutivoFactura(Connection con, int codigoFactura, String consecutivo) 
	{
		String cadena="UPDATE facturas set consecutivo_factura="+consecutivo+" where codigo="+codigoFactura;
		logger.info("actualizacion consecutivo -->"+cadena);
		try
		{
			PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(cadena));
			return (ps.executeUpdate()>0);
		}
		catch(SQLException e)
		{
			logger.warn("Error en  actualizarConsecutivoFactura " +e.toString());
		}
		return false;
	}
	
	/**
	 * 
	 * @param con
	 * @param cuenta
	 * @param idSesion
	 * @return
	 */
	public static boolean eliminarCuentaProcesoFacturacion(Connection con, BigDecimal cuenta, String idSesion)
	{
		boolean retorna= false;
		String consulta="DELETE FROM facturacion.cuentas_proceso_fact WHERE cuenta=? AND id_sesion=? ";
		try
		{
			PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(consulta));
			ps.setBigDecimal(1, cuenta);
			ps.setString(2, idSesion);
			ps.executeUpdate();
			ps.close();
			retorna=true;
		}
		catch(SQLException e)
		{
			logger.warn("Error en  eliminarCuentaProcesoFacturacion ",e);
			retorna= false;
		}
		return retorna;
	}
	
	
	
	
	/**
	 * @param con
	 * @param numeroFactura
	 * @return si tiene autorizaciones 
	 */
	public static Boolean  tieneSolicitudesSinAutorizar(Connection con, Integer numeroSolicitud){
		
		PreparedStatement pst=null;
		ResultSet rs=null;
		try
		{
			//consulta de autorizaciones por numero de solicitud
			String consulta =" select s.numero_solicitud from  solicitudes s join auto_entsub_solicitudes aes " +
					" on(s.numero_solicitud = aes.numero_solicitud) join AUTORIZACIONES_ENTIDADES_SUB aes " +
					" on(aes.autorizacion_ent_sub=aes.consecutivo) where s.numero_solicitud  = ? and aes.estado=?";
			//PreparedStatement de consulta 
			pst = con.prepareStatement(consulta);
			
			//numero de la solicitud 
			if(!UtilidadTexto.isEmpty(numeroSolicitud)){
				pst.setInt(1, numeroSolicitud);
			}
			
			//el estado debe ser autorizado 
			if(!UtilidadTexto.isEmpty(ConstantesIntegridadDominio.acronimoAutorizado)){
				pst.setString (2, ConstantesIntegridadDominio.acronimoAutorizado);
			}
			rs = pst.executeQuery();
			
			//si al menos hay un solo resultado entonces se retorna true
			if(rs.next()){
				return true;
			}

		}
		catch(SQLException sqe){
			logger.error("############## SQLException ERROR tieneSolicitudesSinAutorizar",sqe);
		}
		catch(Exception e){
			logger.error("############## ERROR tieneSolicitudesSinAutorizar", e);
		}
		finally{
			try{
				if(rs != null){
					rs.close();
				}
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException se) {
				logger.error("###########  Error close ResultSet - PreparedStatement", se);
			}
		}

		//se retorna false indicando que no se encontro una autorizacion asociada 
		return false;
		
	}
	
	
}
