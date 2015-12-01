/*
 * @author artotor
 */
package com.princetonsa.dao.sqlbase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.facturacion.DtoDatosRepsonsableFactura;

import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadTexto;
import util.ValoresPorDefecto;

/**
 * 
 * @author artotor
 *
 */
public class SqlBaseImpresionFacturaDao 
{
	/**
	 * Manejador de logs de la clase
	 */
	private static Logger logger=Logger.getLogger(SqlBaseImpresionFacturaDao.class);
	
	/**
	 * Cadena para consultar todo lo conserniente a la institucion en el encabezado
	 */
	private static String consultaSeccionInstitucionFormatoEstatico="select " +
                                                                        "i.razon_social as razonsocial," +
                                                                        "coalesce(i.tipo_identificacion,'') as tipoid," +
                                                                        "getnombretipoidentificacion(i.tipo_identificacion) as descid," +
                                                                        "CASE WHEN i.digito_verificacion IS NOT NULL AND i.digito_verificacion != "+ConstantesBD.codigoNuncaValido+" THEN " +
	                                        								"i.nit || ' - ' || i.digito_verificacion||'' " +
	                                        							"ELSE " +
	                                        								"i.nit " +
	                                        							"END as numeroid, " +
                                                                        "i.direccion as direccion," +
                                                                        "i.telefono as telefono," +
                                                                        "i.departamento as coddepartamento," +
                                                                        "i.ciudad as codigociudad," +
                                                                        "getnombreciudad(i.pais,i.departamento,i.ciudad) as descciudad," +
                                                                        "coalesce(f.resolucion, i.resolucion) as resolucion," +
                                                                        "coalesce(f.rgo_inic_fact, i.rgo_inic_fact) as rangoincialfactura," +
                                                                        "coalesce(f.rgo_fin_fact, i.rgo_fin_fact) as rangofinalfacltura," +
                                                                        "coalesce(i.encabezado, '') as encabezado," +
                                                                        "coalesce(i.pie,'') as pie," +
                                                                        "i.pref_factura as prefijo_fact " +
                                                                    "from instituciones i " +
                                                                    "inner join facturas f on (f.codigo=? and f.institucion=i.codigo)" +
                                                                    "where i.codigo=?";
	
	/**
	 * 
	 */
	private static String consultaSeccionInstitucionFormatoEstaticoMultiEmpresa="select " +
																				    "i.razon_social as razonsocial," +
																				    "coalesce(i.tipo_identificacion,'') as tipoid," +
																				    "getnombretipoidentificacion(i.tipo_identificacion) as descid," +
																				    "CASE WHEN i.digito_verificacion IS NOT NULL THEN " +
				                                        								"i.nit || ' - ' || i.digito_verificacion||'' " +
				                                        							"ELSE " +
				                                        								"i.nit " +
				                                        							"END as numeroid, " +
																				    "i.direccion as direccion," +
																				    "i.telefono as telefono," +
																				    "i.departamento as coddepartamento," +
																				    "i.ciudad as codigociudad," +
																				    "getnombreciudad(i.pais,i.departamento,i.ciudad) as descciudad," +
																				    "coalesce(f.resolucion, i.resolucion) as resolucion," +
																				    "coalesce(f.rgo_inic_fact, i.rgo_inic_fact) as rangoincialfactura," +
																				    "coalesce(f.rgo_fin_fact, i.rgo_fin_fact) as rangofinalfacltura," +
																				    "coalesce(i.encabezado, '') as encabezado," +
																				    "coalesce(i.pie,'') as pie," +
																				    "i.pref_factura as prefijo_fact " +
																				"from empresas_institucion i " +
																				"inner join facturas f on (f.codigo=? and f.empresa_institucion=i.codigo)" +
																				"where i.codigo=?";
	
	
	/**
	 * Cadena para consultar las seccion paciente y los totales en formato de impresio estandar.
	 */
	private static String conSeccPacTotFormatoEstatico="SELECT " +
                                                            "f.codigo as codigofactura," +
                                                            "c.nombre as nombreconvenio," +
                                                            "'' as acronimoid," +
                                                            "'' as tipoid, " +
                                                            //Se modifico por la Tarea 48000. 
                                                            "CASE WHEN c.tipo_regimen='"+ConstantesBD.codigoTipoRegimenParticular+"' THEN " +
                                                            	"ter.numero_identificacion " +
                                                            "ELSE " +
	                                                            "(CASE WHEN t.digito_verificacion IS NOT NULL THEN " +
		                                                    		"t.numero_identificacion || ' - ' || t.digito_verificacion||'' " +
		                                                    	"ELSE " +
		                                                    		"t.numero_identificacion " +
		                                                    	"END) " +
                                                            "END AS numeroid_tercero, " +
                            								"CASE WHEN c.tipo_regimen<>'"+ConstantesBD.codigoTipoRegimenParticular+"' then c.nombre else getNomDeudorIngreso(cue.id_ingreso) end as nombretercero, " +
                                                            
                            								//Modificado por la Tarea 47906
                            								"coalesce(vd.numero_verificacion,'') as numeroautorizacion, " +
                            								//"CASE WHEN ah.numero_autorizacion IS NOT NULL THEN ah.numero_autorizacion ELSE (CASE WHEN au.numero_autorizacion IS NOT NULL THEN au.numero_autorizacion ELSE '' END) END as numeroautorizacion," +
                                                            
                            								"CASE WHEN sc.nro_poliza IS NOT NULL THEN sc.nro_poliza ELSE '' END as numeropoliza," +
                                                            "cue.indicativo_acc_transito as accidentetransito," +
                                                            "p.primer_apellido ||' '|| p.segundo_apellido ||' '|| p.primer_nombre ||' '|| p.segundo_nombre as nombrepersona," +
                                                            "p.tipo_identificacion as tipoid,getnombretipoidentificacion(p.tipo_identificacion) as desctipoid," +
                                                            "p.numero_identificacion as numeroid_paciente," +
                                                            "p.direccion as direccionpersona," +
                                                            "f.convenio as convenio," +
                                                            "p.telefono as telefonopersona," +
                                                            "getDatosIngresoEgresoCuenta(cue.id,f.via_ingreso) as infoIngreso, " +
                                                            "f.via_ingreso as codigoviaingreso, " +
                                                            "getnombreviaingresotipopac(f.cuenta) as nombreviaingreso, " + //seccion de paciente
                                                            "f.usuario as usuarioelabora, " + //usuario que elabora la factura.
                                                			"f.valor_total as valortotalfactura," +
                                                            "f.valor_convenio as valorconvenio," +
                                                            "f.valor_bruto_pac as valorbrutopaciente," +
                                                            "f.valor_abonos as valorabonos," +
                                                            "f.val_desc_pac as valordescuentopaciente," +
                                                            "f.valor_neto_paciente as valornetopaciente," +
                                                            "coalesce(f.valor_favor_convenio, 0) as valorfavorconvenio," +
                                                            "f.consecutivo_factura as consecutivo_factura," +
                                                            "f.fecha as fecha_factura," +
                                                            "f.hora as hora_factura, " + //secion totales factura
                                                			"f.estado_facturacion as estadofacturacion," +
                                                			"to_char(af.fecha_grabacion,'dd/mm/yyyy') as fechaanulacion, " +
                                                			"substr(af.hora_grabacion,0,6) as horaanulacion " + //iformacion anulacion
                                                			"from facturas f " +
                                                			"left outer join sub_cuentas sc ON (sc.sub_cuenta= f.sub_cuenta) " +
                                                			"left outer join verificaciones_derechos vd ON (vd.sub_cuenta= sc.sub_cuenta) " +
                                                            "inner join convenios c on(f.convenio=c.codigo) " +
                                                            "inner join empresas em on(c.empresa=em.codigo) " +
                                                            "inner join terceros t on (em.tercero=t.codigo) " +
                                                            "inner join cuentas cue on(f.cuenta=cue.id) " +
                                                            "inner join personas p on(cue.codigo_paciente=p.codigo) " +
                                                            "left outer join terceros ter on (f.cod_res_particular=ter.codigo) " +
                                                            "left outer join admisiones_urgencias au on (f.cuenta=au.cuenta) " +
                                                            "left outer join  admisiones_hospi ah on(f.cuenta=ah.cuenta) " +
                                                            "left outer join anulaciones_facturas af on(f.consecutivo_factura=af.consecutivo_factura and f.institucion = af.institucion) " +
                                                        "where f.codigo=?";
	
	/**
	 * Cadena para la consulta de los servicios de una factura, muestra los servicios agrupados menos las cirugias en formato de impresio estandar.
	 */
	private static String conSeccServiciosFormatoEstaticoVenezuela=" SELECT " +
																		"gs.codigo as codgruposerv, " +
																		"gs.descripcion as descgruposerv, "+
																		"dfs.servicio as servicio, " +
																		"getnombreservicio(dfs.servicio,"+ConstantesBD.codigoTarifarioCups+") as descservicio," +
																		"sum(dfs.cantidad_cargo)||'' as cantidad, " +
																		"(sum(dfs.valor_total)/sum(dfs.cantidad_cargo))||'' as valorunitario, " +
																		"sum(dfs.valor_total)||'' as valortotal " +
																	"from " +
																		"facturas f " +
																		"inner join det_factura_solicitud dfs on (f.codigo=dfs.factura) " +
																		"inner join solicitudes s on (dfs.solicitud=s.numero_solicitud) " +
																		"inner join servicios serv on (serv.codigo=dfs.servicio) " +
																		"inner join grupos_servicios gs on (gs.codigo=serv.grupo_servicio) " +
		                                                            "where " +
		                                                            	"f.codigo=? and dfs.articulo is null "+
		                                                            	"and dfs.cantidad_cargo>0 group by 1,2,3 order by descgruposerv, descservicio ";
		                                                            
	
	/**
	 * Cadena para consultar el detalle de los asocios de los servicios de una cirugia dado el numero de la solicitud y el codigo del servicio madre de la cirugia. 
	 */
	private static String conDetAsoSerCirugiaFormatoEstatico="SELECT " +
		"ta.codigo_asocio as acronimoasocio," +
        //" getnombretipoasocio(ta.codigo) nombreasocio," +
		//" getcodigopropservicio2(adf.servicio_asocio, "+ConstantesBD.codigoTarifarioCups+")||' - '|| getnombreservicio(adf.servicio_asocio, "+ConstantesBD.codigoTarifarioCups+") as nombreasocio, " +
		" coalesce(adf.codigo_propietario,'')||' - '|| coalesce(getnombreservicio(adf.servicio_asocio, "+ConstantesBD.codigoTarifarioCups+"),'') as nombreasocio, " +
		"'1' as cantidad," +
		"adf.valor_total as valorunitario," +
		"adf.valor_total as valortotal," +
		"dfs.solicitud as numerosolicitud," +
		"dfs.servicio as codigoservicio " +
		"from det_factura_solicitud dfs " +
		"inner join asocios_det_factura adf on(adf.codigo=dfs.codigo) " +
		"inner join tipos_asocio ta on (ta.codigo=adf.tipo_asocio) " +
		"where dfs.codigo=? " +
		"AND adf.valor_total>0";
	
	/**
	 * Cadena que realiza la consulta de las solicitude agrupadas por fecha
	 */
	private static String consultaAnexoMedicamentosOredenesFecha="SELECT s.fecha_solicitud as fechasolicitud,sum(valor_total) as totalsolicitudes from det_factura_solicitud dfs inner join solicitudes s on(dfs.solicitud=s.numero_solicitud) where dfs.factura=? and dfs.servicio is null group by s.fecha_solicitud";
	
	/**
	 * Cadena que realiza la consulta de los articulos agrupados dada su fecha de solicitud.
	 */
	private static String consultaMedicamentosAgrupadosFechaSolicitud="SELECT dfs.articulo as codarticulo,replace(inventarios.getDescAltArticuloSinCodigo(dfs.articulo),'   ','') as articulo,sum(dfs.cantidad_cargo) as cantidad,sum(dfs.valor_total) as valortotal,(sum(dfs.valor_total)/sum(dfs.cantidad_cargo)) as valorunitario from det_factura_solicitud dfs inner join solicitudes s on(dfs.solicitud=s.numero_solicitud) where dfs.servicio is null and s.fecha_solicitud=? and dfs.factura=? group by(dfs.articulo) order by articulo";
	
	/**
	 * Cadena que trae las solicitude de una fecha determinada y el codigo de la factura.
	 */
	private static String consultaSolMedicamentosFechaSolicitud="SELECT s.numero_solicitud as numerosolicitud,s.consecutivo_ordenes_medicas as consecutivoorden,sum(valor_total) as totalsolicitud from det_factura_solicitud dfs inner join solicitudes s on(dfs.solicitud=s.numero_solicitud) where s.fecha_solicitud=? and dfs.factura=? and dfs.servicio is null group by s.numero_solicitud,s.consecutivo_ordenes_medicas";
	
	
	/**
	 * Cadena  que consulta el detalle de una solicitud dado el numero de solicitud y el codigo de la factura.
	 */
	private static String consultaMedicamentosSolicitud="SELECT dfs.articulo as codarticulo,replace(inventarios.getDescAltArticuloSinCodigo(dfs.articulo),'   ','') as articulo,sum(dfs.cantidad_cargo) as cantidad,sum(dfs.valor_total) as valortotal,(sum(dfs.valor_total)/sum(dfs.cantidad_cargo)) as valorunitario from det_factura_solicitud dfs inner join solicitudes s on(dfs.solicitud=s.numero_solicitud) where dfs.servicio is null and s.numero_solicitud=? and dfs.factura=? group by(dfs.articulo) order by articulo";
	
	/**
	 * 
	 */
	private static String consultaCitasDadaCuenta="SELECT getnombreunidadconsulta(c.unidad_consulta) as unidadconsulta,getnombrepersona(a.codigo_medico) as profesional,to_char(a.fecha,'dd/mm/yyyy') as fecha,substr(a.hora_inicio||'',1,5) as hora,cons.descripcion as consultorio,c.codigo as cita from solicitudes s inner join servicios_cita sercit on(sercit.numero_solicitud=s.numero_solicitud ) inner join cita c on(c.codigo=sercit.codigo_cita)  inner join agenda a on(a.codigo=c.codigo_agenda) inner join usuarios u on(c.usuario=u.login) inner join consultorios cons on (a.consultorio=cons.codigo) where a.activo="+ValoresPorDefecto.getValorTrueParaConsultas()+" and s.cuenta=?";
	
	/**
	 * cadena que consulta el valor letras de un convenio especifico
	 */
	private static String consultaValorLetrasConvenio="SELECT CASE WHEN valor_letras_factura IS NOT NULL THEN " +
																	"valor_letras_factura " +
																"ELSE '' END AS valorletras " +
														"FROM convenios where codigo=? ";
	
	
	/**
	 * Cadena para consultar todo lo conserniente a la institucion en el encabezado
	 */
	private static String consultaSeccionInstitucionFormatoVersalles="select " +
                                                                        "i.razon_social as razonsocial," +
                                                                        "coalesce(i.tipo_identificacion,'') as tipoid," +
                                                                        "getnombretipoidentificacion(i.tipo_identificacion) as descid," +
                                                                        "CASE WHEN i.digito_verificacion IS NOT NULL AND i.digito_verificacion != "+ConstantesBD.codigoNuncaValido+" THEN " +
	                                        								"i.nit || ' - ' || i.digito_verificacion||'' " +
	                                        							"ELSE " +
	                                        								"i.nit " +
	                                        							"END as numeroid, " +
                                                                        "i.direccion as direccion," +
                                                                        "i.telefono as telefono," +
                                                                        "i.departamento as coddepartamento," +
                                                                        "i.ciudad as codigociudad," +
                                                                        "getnombreciudad(i.pais,i.departamento,i.ciudad) as descciudad," +
                                                                        "coalesce(f.resolucion, i.resolucion) as resolucion," +
                                                                        "coalesce(f.rgo_inic_fact, i.rgo_inic_fact) as rangoincialfactura," +
                                                                        "coalesce(f.rgo_fin_fact, i.rgo_fin_fact) as rangofinalfacltura," +
                                                                        "coalesce(i.encabezado, '') as encabezado," +
                                                                        "coalesce(i.pie,'') as pie," +
                                                                        "i.pref_factura as prefijo_fact " +
                                                                    "from instituciones i " +
                                                                    "inner join facturas f on (f.codigo=? and f.institucion=i.codigo)" +
                                                                    "where i.codigo=?";
	
	/**
	 * Cadena para consultar las seccion paciente y los totales en formato de impresio estandar.
	 */
	private static String conSeccPacTotFormatoVersalles="SELECT " +
                                                            "f.codigo as codigofactura," +
                                                            "c.nombre as nombreconvenio," +
                                                            "'' as acronimoid," +
                                                            "'' as tipoid, " +
                                                            //Se modifico por la Tarea 48000. 
                                                            "CASE WHEN c.tipo_regimen='"+ConstantesBD.codigoTipoRegimenParticular+"' THEN " +
                                                            	"ter.numero_identificacion " +
                                                            "ELSE " +
	                                                            "(CASE WHEN t.digito_verificacion IS NOT NULL THEN " +
		                                                    		"t.numero_identificacion || ' - ' || t.digito_verificacion||'' " +
		                                                    	"ELSE " +
		                                                    		"t.numero_identificacion " +
		                                                    	"END) " +
                                                            "END AS numeroid_tercero, " +
                                                            "t.direccion as dirtercero," +
                                                            "t.telefono as telefonotercero," +
                            								"CASE WHEN c.tipo_regimen<>'"+ConstantesBD.codigoTipoRegimenParticular+"' then c.nombre else getNomDeudorIngreso(cue.id_ingreso) end as nombretercero, " +
                                                            
                            								//Modificado por la Tarea 47906
                            								"coalesce(vd.numero_verificacion,'') as numeroautorizacion, " +
                            								//"CASE WHEN ah.numero_autorizacion IS NOT NULL THEN ah.numero_autorizacion ELSE (CASE WHEN au.numero_autorizacion IS NOT NULL THEN au.numero_autorizacion ELSE '' END) END as numeroautorizacion," +
                                                            
                            								"CASE WHEN sc.nro_poliza IS NOT NULL THEN sc.nro_poliza ELSE '' END as numeropoliza," +
                                                            "cue.indicativo_acc_transito as accidentetransito," +
                                                            "p.primer_apellido ||' '|| p.segundo_apellido ||' '|| p.primer_nombre ||' '|| p.segundo_nombre as nombrepersona," +
                                                            "p.tipo_identificacion as tipoid,getnombretipoidentificacion(p.tipo_identificacion) as desctipoid," +
                                                            "p.numero_identificacion as numeroid_paciente," +
                                                            "p.direccion as direccionpersona," +
                                                            "f.convenio as convenio," +
                                                            "p.telefono as telefonopersona," +
                                                            "to_char(ingre.fecha_ingreso,'dd/mm/yyyy') as fechaingreso," +
                                                            "to_char(egre.fecha_egreso,'dd/mm/yyyy') as fechaegreso, " + //fecha egreso tabla egresos
                                                            "f.via_ingreso as codigoviaingreso, " +
                                                            "getnombreviaingresotipopac(f.cuenta) as nombreviaingreso, " + //seccion de paciente
                                                            "f.usuario as usuarioelabora, " + //usuario que elabora la factura.
                                                			"f.valor_total as valortotalfactura," +
                                                            "f.valor_convenio as valorconvenio," +
                                                            "f.valor_bruto_pac as valorbrutopaciente," +
                                                            "f.valor_abonos as valorabonos," +
                                                            "f.val_desc_pac as valordescuentopaciente," +
                                                            "f.valor_neto_paciente as valornetopaciente," +
                                                            "coalesce(f.valor_favor_convenio, 0) as valorfavorconvenio," +
                                                            "f.consecutivo_factura as consecutivo_factura," +
                                                            "f.fecha as fecha_factura," +
                                                            "c.num_dias_vencimiento as diasvencimiento," +
                                                            "case when c.num_dias_vencimiento IS NOT NULL and c.num_dias_vencimiento > 0 then " +
                                                            (System.getProperty("TIPOBD").equals("ORACLE") ? " f.fecha+c.num_dias_vencimiento " : " f.fecha+cast('1 day' as interval) * c.num_dias_vencimiento ") +
                                                            "else " +
                                                            	"f.fecha " +
                                                            "end as fechavencimiento,"+
                                                            "ingre.consecutivo as consecutivoingreso,"+
                                                            "f.hora as hora_factura, " + //secion totales factura
                                                			"f.estado_facturacion as estadofacturacion," +
                                                			"to_char(af.fecha_grabacion,'dd/mm/yyyy') as fechaanulacion, " +
                                                			"substr(af.hora_grabacion,0,6) as horaanulacion,c.ENCABEZADO_FACTURA                     AS encabezadofacturaconvenio,  c.pie_factura AS  piefactura  " + //iformacion anulacion
                                                			"from facturas f " +
                                                			"inner join convenios c on(f.convenio=c.codigo) " +
                                                            "inner join empresas em on(c.empresa=em.codigo) " +
                                                            "inner join terceros t on (em.tercero=t.codigo) " +
                                                            "left outer join cuentas cue on (cue.id=f.cuenta) " +
                                                			"left outer join ingresos ingre on (cue.id_ingreso=ingre.id) " +
                                                			"left outer join egresos egre on (egre.cuenta=cue.id) " +
                                                			"left outer join sub_cuentas sc ON (sc.sub_cuenta= f.sub_cuenta) " +
                                                			"left outer join verificaciones_derechos vd ON (vd.sub_cuenta= sc.sub_cuenta) " +
                                                            "left outer join personas p on(cue.codigo_paciente=p.codigo) " +
                                                            "left outer join terceros ter on (f.cod_res_particular=ter.codigo) " +
                                                            "left outer join admisiones_urgencias au on (f.cuenta=au.cuenta) " +
                                                            "left outer join  admisiones_hospi ah on(f.cuenta=ah.cuenta) " +
                                                            "left outer join anulaciones_facturas af on(f.consecutivo_factura=af.consecutivo_factura and f.institucion = af.institucion) " +
                                                        "where f.codigo=?";
	
	/**
	 * Cadena para consultar el detalle de los asocios de los servicios de una cirugia dado el numero de la solicitud y el codigo del servicio madre de la cirugia. 
	 */
	private static String conDetAsoSerCirugiaFormatoVersalles="SELECT " +
																	"ta.codigo_asocio as acronimoasocio," +
															        //" getnombretipoasocio(ta.codigo) nombreasocio," +
																	//" getcodigopropservicio2(adf.servicio_asocio, "+ConstantesBD.codigoTarifarioCups+")||' - '|| getnombreservicio(adf.servicio_asocio, "+ConstantesBD.codigoTarifarioCups+") as nombreasocio, " +
																	" adf.codigo_propietario||' - '|| getnombreservicio(adf.servicio_asocio, "+ConstantesBD.codigoTarifarioCups+") as nombreasocio, " +
																	"'1' as cantidad," +
																	"adf.valor_total as valorunitario," +
																	"adf.valor_total as valortotal," +
																	"dfs.solicitud as numerosolicitud," +
																	"dfs.servicio as codigoservicio " +
																"FROM " +
																	"det_factura_solicitud dfs " +
																"INNER JOIN " +
																	"asocios_det_factura adf " +
																		"ON(adf.codigo=dfs.codigo) " +
																"INNER JOIN " +
																	"tipos_asocio ta " +
																		"ON (ta.codigo=adf.tipo_asocio) " +
																"WHERE " +
																		"dfs.codigo=? " +
																	"AND " +
																		"adf.valor_total>0 " +
																"ORDER BY " +
																	"nombreasocio ";
	
	
    /**
     * metodo para consultar los articulos de la factura
     * @param con Connection
     * @param codigoFactira  String 
     * @return HashMap
     */
    @SuppressWarnings("unchecked")
	public static HashMap consultarSeccionArticulosFormatoVersalles( Connection con,String codigoFactira, String filtrarXInsumos ) 
    {
    	/**
    	 * Consulta de los articulos de una factura agrupados, utilizada en el formato de impresion estatico.
    	 */
    	String conSeccArticulosFormatoVersalles="select " +
                                                    "f.codigo as codigofactura," +
                                                    "dfs.articulo as codigoarticulo," +
                                                    "replace(inventarios.getDescAltArticuloSinCodigo(dfs.articulo),'   ','') as descarticulo," +
                                                    "sum(dfs.cantidad_cargo) as cantidad," +
                                                    "sum(dfs.valor_total)/sum(dfs.cantidad_cargo) as valorunitario," +
                                                    "sum(dfs.valor_total) as valortotal, " +
                                                    "coalesce(manejopaciente.getAutoFacSerArt(f.codigo, dfs.articulo), ' ') as numeroautorizacion " +
                                                "from facturas f " +
                                                    "inner join det_factura_solicitud dfs on (f.codigo=dfs.factura) " +
                                                    "inner join articulo a on(a.codigo=dfs.articulo) "+
                                                    "inner join naturaleza_articulo na on(na.acronimo=a.naturaleza and na.institucion=a.institucion) " +
                                                "where f.codigo=? and dfs.servicio is null ";
                                                
        if(!UtilidadTexto.isEmpty(filtrarXInsumos))                                        
        {
        	if(UtilidadTexto.getBoolean(filtrarXInsumos))
        		conSeccArticulosFormatoVersalles+=" and na.es_medicamento='"+ConstantesBD.acronimoNo+"' ";
        	else
        		conSeccArticulosFormatoVersalles+=" and na.es_medicamento='"+ConstantesBD.acronimoSi+"' ";
        }
                                                
        conSeccArticulosFormatoVersalles+=      "group by f.codigo,dfs.articulo,3 " +
                                                "having sum(dfs.valor_total)>0 " + //mostrar solo los que tienen valor_total>0, los que tiene 0 son excentos.
                                                "order by descarticulo";
    	
    	
    	
        HashMap mapa=new HashMap( );
        mapa.put( "numRegistros","0" );
        PreparedStatementDecorator ps;
        try 
        {
        	logger.info("CONSULTA : "+conSeccArticulosFormatoVersalles);
        	logger.info("FACTURA : "+codigoFactira);
            ps =  new PreparedStatementDecorator(con.prepareStatement( conSeccArticulosFormatoVersalles ,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            ps.setString( 1,codigoFactira );           
            mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery( )));
            
            //Incidencia 3463 davgommo
            // Se hacen validaciones para que traiga el codigo segun el tipo de codigo dado por el convenio
            int num= Integer.parseInt(mapa.get("numRegistros").toString());
            String tipoCod="SELECT factu.convenio AS conv,  conve.tipo_codigo_articulos AS tipo, conve.institucion AS ins FROM facturacion.facturas factu,  facturacion.convenios conve WHERE factu.codigo="+codigoFactira+" AND factu.convenio=conve.codigo";
        	PreparedStatement pst1=null;
			ResultSet rs1=null;
			pst1=  con.prepareStatement( tipoCod,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			rs1=pst1.executeQuery();
			rs1.next();
			int codArt= 42;
			if (rs1.getString("tipo")!=null)
			{
		     codArt=rs1.getInt("tipo");
			} 
			int institucion=rs1.getInt("ins");
		    pst1=null;
		    rs1=null;
		    for (int i=0; i<num; i++)
            {
		    	int codigoant=Integer.parseInt(mapa.get("codigoarticulo_"+i).toString());
		    	if (codArt==0) //Si el c骴igo definido para el convenio es CUM
				{
					String obtenerCUM= "select distinct facturacion.getcodigocumarticulo(articulo.codigo) AS codigo FROM inventarios.articulo WHERE articulo.codigo="+codigoant;
					PreparedStatement pst2=null;
					ResultSet rs2=null;
					pst2=  con.prepareStatement( obtenerCUM,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
					rs2=pst2.executeQuery();
					rs2.next();
					int codNuevo=rs2.getInt("codigo");
					rs2.close();
					pst2.close();
					mapa.put("codigoarticulo_"+i, codNuevo);
				}else {
					if (codArt!=1) //El codigo axioma lo trae por defecto, pero si tipo de codigo no es axioma ni cum entra a la validacion
					{
						String codDefault=ValoresPorDefecto.getCodigoManualEstandarBusquedaArticulos(institucion);
						if (!codDefault.equals("AXM")) //si el codigo estandar no es axioma trae el codigo interfaz
						{
							String obtenerInterfaz= "SELECT DISTINCT facturacion.getcodigointerfaz(articulo.codigo) AS codigo FROM inventarios.articulo WHERE articulo.codigo="+codigoant;
							PreparedStatement pst2=null;
							ResultSet rs2=null;
							pst2=  con.prepareStatement( obtenerInterfaz,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
							rs2=pst2.executeQuery();
							rs2.next();
							int m=0;
							int codNuevo=rs2.getInt("codigo");
							rs2.close();
							pst2.close();
							mapa.put("codigoarticulo_"+i, codNuevo);
						}
					
					}
				}
		    	
            }
            
            
        } 
        catch (SQLException e) 
        {
            logger.error( "Error en la consulta <consultarSeccionArticulosFormatoVersalles> en la clase <SqlBaseImpresionFacturaDao> error: "+e );
            e.printStackTrace( );
        }        
        return ( HashMap ) mapa.clone( );
    }
	
    /**
     * metodo para consultar el detalle de los servicios
     * @param con Connection
     * @param numSolicitud String 
     * @return HashMap
     */
    public static HashMap consultarSeccionDetServiciosFormatoVersalles( Connection con,String codigoDetalleFactura ) 
    {
        HashMap mapa=new HashMap( );
        mapa.put( "numRegistros","0" );
        PreparedStatementDecorator ps;
        try 
        {
            ps =  new PreparedStatementDecorator(con, conDetAsoSerCirugiaFormatoVersalles);
            ps.setString( 1,codigoDetalleFactura );
            mapa=UtilidadBD.cargarValueObjectSinNumRegistros(new ResultSetDecorator(ps.executeQuery( )));
        } 
        catch (SQLException e) 
        {
            logger.error( "Error en la consulta <consultarSeccionDetServiciosFormatoVersalles> en la clase <SqlBaseImpresionFacturaDao> error: "+e );
            e.printStackTrace( );
        }        
        return ( HashMap ) mapa.clone( );
    }
	
    /**
     * metodo para consultar los servicios de una factura para el formato de impresion Versalles
     * @param con Connection
     * @param codigoFactira String
     * @return HashMap
     */
    @SuppressWarnings("unchecked")
	public static HashMap consultarSeccionServiciosFormatoVersalles( Connection con, String codigoFactira , String conSeccServiciosFormatoVersalles) 
    {
        HashMap mapa=new HashMap( );
        HashMap mapa2=new HashMap( );
        mapa.put( "numRegistros","0" );
        PreparedStatementDecorator ps;
        try 
        {
        	String cadena=conSeccServiciosFormatoVersalles;
        	if(System.getProperty("PAIS").toString().toUpperCase().equals("VENEZUELA"))
        		cadena=conSeccServiciosFormatoEstaticoVenezuela;
            ps =  new PreparedStatementDecorator(con.prepareStatement( cadena ,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            logger.info("conSeccServiciosFormatoVersalles-->"+cadena+" ->"+codigoFactira);
            ps.setString( 1,codigoFactira );
            mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery( )));
            int num= Integer.parseInt(mapa.get("numRegistros").toString());
            //davgommo MT 3463
            // Se arrgela el m閠odo para que utilize las validaciones para los c骴igos de servicios 
        	String tipoCod="SELECT factu.convenio AS conv,  conve.tipo_codigo AS tipo, conve.institucion AS ins FROM facturacion.facturas factu,  facturacion.convenios conve WHERE factu.codigo="+codigoFactira+" AND factu.convenio=conve.codigo";
        	PreparedStatement pst1=null;
			ResultSet rs1=null;
			pst1=  con.prepareStatement( tipoCod,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			rs1=pst1.executeQuery();
			rs1.next();
			int codSer= 42;
			if (rs1.getString("tipo")!=null)
			{
		     codSer=rs1.getInt("tipo");
			} 
			int convenio=rs1.getInt("conv");
			int institucion=rs1.getInt("ins");
		    pst1=null;
		    rs1=null;
            for (int i=0; i<num; i++)
            {
              String codigoant= mapa.get("servicio_"+i).toString();	
              String obtenerCodServicio= "select facturacion.getcodigosoat("+codigoant+", convenios.tipo_codigo) AS codigo FROM CONVENIOS where convenios.codigo="+convenio;
				String obtenerDescServicio= "select facturacion.getdescripcioncodigosoat("+codigoant+", convenios.tipo_codigo) AS codigo FROM CONVENIOS where convenios.codigo="+convenio;
				PreparedStatement pst2=null;
				ResultSet rs2=null;
				pst2=  con.prepareStatement( obtenerCodServicio,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
				rs2=pst2.executeQuery();
				rs2.next();
				int codNuevo=0;
				if (rs2.getString("codigo")!=null) //Verificamos si el servicio tiene un codigo definido para el tipo de codigo servicio del convenio 
				{codNuevo=rs2.getInt("codigo");
				mapa.put("codigopropeitario_"+i, codNuevo);
				PreparedStatement pst3=null;
				ResultSet rs3=null;
				pst3=  con.prepareStatement( obtenerDescServicio,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
				rs3=pst3.executeQuery();
				rs3.next();
				String nuevoDesc=rs3.getString("codigo");
				mapa.put("procedimiento_"+i, nuevoDesc);
				rs3.close();
				pst3.close();
				}
				else { // si el servicio no tiene definido un codigo para el convenio, se busca si tiene definido uno para el tipo de codigo en el parametro codigo manual estandar servicios
					String codSerDefault=ValoresPorDefecto.getCodigoManualEstandarBusquedaServiciosLargo(institucion);
					String[] valorD=codSerDefault.split("@@");
					if (!codSerDefault.equals("@@"))
					{obtenerCodServicio= "select distinct facturacion.getcodigosoat("+codigoant+","+valorD[0]+") AS codigo FROM CONVENIOS where convenios.codigo="+convenio;
					obtenerDescServicio= "select facturacion.getdescripcioncodigosoat("+codigoant+","+valorD[0]+") AS codigo FROM CONVENIOS where convenios.codigo="+convenio;
					PreparedStatement pst3=null;
					ResultSet rs3=null;
					pst3=  con.prepareStatement( obtenerCodServicio,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
					rs3=pst3.executeQuery();
					rs3.next();
					if (rs3.getString("codigo")!=null) //Si no tiene codigo para el tipo de codigo en el parametro se deja el axioma 
					{
					codNuevo=rs3.getInt("codigo");
					mapa.put("codigopropeitario_"+i, codNuevo);
					PreparedStatement pst4=null;
					ResultSet rs4=null;
					pst4=  con.prepareStatement( obtenerDescServicio,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
					rs4=pst4.executeQuery();
					rs4.next();
					String nuevoDesc=rs4.getString("codigo");
					mapa.put("procedimiento_"+i, nuevoDesc);
					rs4.close();
					pst4.close();
					} else 
					{
						mapa.put("codigopropeitario_"+i, codigoant);	
					}
					rs3.close();
					pst3.close();		
				}}
				rs2.close();
				pst2.close();
				
            }
        } 
        catch ( SQLException e ) 
        {
            logger.error( "Error en la consulta <consultarSeccionServiciosFormatoVersalles> en la clase <SqlBaseImpresionFacturaDao> error: "+e);
            e.printStackTrace( );
        }
        // davgommo MT 3463
        //Se requeiere que el codigo del servicio y del articulo corresponda al tipo de articulo/servicio del convenio
        int cant=0;
       
        
        return ( HashMap ) mapa.clone( );
    }
	
	/**
	 * Metodo que realiza la consulta especifica para el encabezado de una impresion de factura en formato estatico.
	 * @param con
	 * @param codigoFactira
	 * @return
	 */
	public static HashMap consultarSeccionPacienteFormatoVersalles(Connection con, String codigoFactira) 
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros","0");
		PreparedStatementDecorator ps;
		try 
		{
			ps =  new PreparedStatementDecorator(con.prepareStatement(conSeccPacTotFormatoVersalles,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1,codigoFactira);
			logger.info("===>Consulta Datos Paciente: "+conSeccPacTotFormatoVersalles+" ===>Codigo Factura: "+codigoFactira);
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()),false,false);//retorna un mapa sin indices y en minuscula.
		} 
		catch (SQLException e) 
		{
			logger.error("Error en la consulta <consultarSeccionPacienteFormatoVersalles> en la clase <SqlBaseImpresionFacturaDao> error: "+e);
			e.printStackTrace();
		}
		
		return (HashMap) mapa.clone();
	}
	
	 /**
     * metodo para consultar la informaci贸n de la instituci贸n
     * @param con Connection
     * @param codigoInstitucion int 
     * @return  HashMap
     */
    public static HashMap consultarSeccionInstitucionFormatoVersalles( Connection con, int codigoInstitucion, String codigoFactura, double empresaInstitucion ) 
    {
        HashMap mapa=new HashMap(  );
        mapa.put( "numRegistros","0" );
        PreparedStatementDecorator ps;
        
        String consulta=consultaSeccionInstitucionFormatoVersalles;
        String parametro= codigoInstitucion+"";
        
/*        if(UtilidadTexto.getBoolean(ValoresPorDefecto.getInstitucionMultiempresa(codigoInstitucion)) && empresaInstitucion>0)
        {	
			consulta=consultaSeccionInstitucionFormatoEstaticoMultiEmpresa;
			parametro= empresaInstitucion+"";
        }	
*/
        
        logger.info("\n CONSULTA SECCION INSTITUCION O EMPRESA-INSITUCION------>"+consulta+"  PARAMETRO-->"+parametro+" \n");
        
        try 
        {
            ps =  new PreparedStatementDecorator(con.prepareStatement( consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet) );
            ps.setString(1, codigoFactura);
            ps.setString( 2, parametro );
            mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator( ps.executeQuery(  )),false,false );//retorna un mapa sin indices y en minuscula.
        } 
        catch ( SQLException e ) 
        {
            logger.error( "Error en la consulta <consultarSeccionInstitucionFormatoVersalles> en la clase <SqlBaseImpresionFacturaDao> error: "+e );
            e.printStackTrace(  );
        }
        
        return ( HashMap ) mapa.clone( );
    }
    
	
	/**
	 * Metodo que verifica el valor letras factura de un convenio
	 * y pasa valor a letras
	 * @param con
	 * @param codConvenio
	 * @return
	 */
	public static HashMap consultarValorLetrasValor(Connection con, String codConvenio)
	{
		HashMap<String, Object> resultados = new HashMap<String, Object>();
		PreparedStatementDecorator ps;
		ResultSetDecorator rs;
		try
		{
			ps =  new PreparedStatementDecorator(con.prepareStatement(consultaValorLetrasConvenio,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1, codConvenio);
			
			rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
				resultados.put("parametro", rs.getString("valorletras"));
		}
		catch (SQLException e)
		{
			logger.error(e+" Error en la consulta del Valor Letras Factura por Convenio.");
		}
		logger.info("\n\nMAPA PARAMETRO>>>>>>>>>>>"+resultados);
		return resultados;
	}
	
	/**
	 * Metodo que realiza la consulta especifica para el encabezado de una impresion de factura en formato estatico.
	 * @param con
	 * @param codigoFactira
	 * @return
	 */
	public static HashMap consultarSeccionPacienteFormatoEstatico(Connection con, String codigoFactira) 
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros","0");
		PreparedStatementDecorator ps;
		try 
		{
			ps =  new PreparedStatementDecorator(con.prepareStatement(conSeccPacTotFormatoEstatico,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1,codigoFactira);
			logger.info("===>Consulta Datos Paciente: "+conSeccPacTotFormatoEstatico+" ===>Codigo Factura: "+codigoFactira);
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()),false,false);//retorna un mapa sin indices y en minuscula.
		} 
		catch (SQLException e) 
		{
			logger.error("Error en la consulta <consultarSeccionPacienteFormatoEstatico> en la clase <SqlBaseImpresionFacturaDao> error: "+e);
			e.printStackTrace();
		}
		
		return (HashMap) mapa.clone();
	}
    /**
     * metodo para consultar la informaci贸n de la instituci贸n
     * @param con Connection
     * @param codigoInstitucion int 
     * @return  HashMap
     */
    public static HashMap consultarSeccionInstitucionFormatoEstatico( Connection con, int codigoInstitucion, String codigoFactura, double empresaInstitucion ) 
    {
        HashMap mapa=new HashMap(  );
        mapa.put( "numRegistros","0" );
        PreparedStatementDecorator ps;
        
        String consulta=consultaSeccionInstitucionFormatoEstatico;
        String parametro= codigoInstitucion+"";
        
        if(UtilidadTexto.getBoolean(ValoresPorDefecto.getInstitucionMultiempresa(codigoInstitucion)) && empresaInstitucion>0)
        {	
			consulta=consultaSeccionInstitucionFormatoEstaticoMultiEmpresa;
			parametro= empresaInstitucion+"";
        }	
		
        logger.info("\n CONSULTA SECCION INSTITUCION O EMPRESA-INSITUCION------>"+consulta+"  PARAMETRO-->"+parametro+" \n");
        
        try 
        {
            ps =  new PreparedStatementDecorator(con.prepareStatement( consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet) );
            ps.setString(1, codigoFactura);
            ps.setString( 2, parametro );
            mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator( ps.executeQuery(  )),false,false );//retorna un mapa sin indices y en minuscula.
        } 
        catch ( SQLException e ) 
        {
            logger.error( "Error en la consulta <consultarSeccionInstitucionFormatoEstatico> en la clase <SqlBaseImpresionFacturaDao> error: "+e );
            e.printStackTrace(  );
        }
        
        return ( HashMap ) mapa.clone( );
    }
    /**
     * metodo para consultar los servicios de una factura
     * @param con Connection
     * @param codigoFactira String
     * @return HashMap
     */
    public static HashMap consultarSeccionServiciosFormatoEstatico( Connection con, String codigoFactira, String conSeccServiciosFormatoEstatico ) 
    {
        HashMap mapa=new HashMap( );
        mapa.put( "numRegistros","0" );
        PreparedStatementDecorator ps;
        try 
        {
        	String cadena=conSeccServiciosFormatoEstatico;
        	if(System.getProperty("PAIS").toString().toUpperCase().equals("VENEZUELA"))
        		cadena=conSeccServiciosFormatoEstaticoVenezuela;
            ps =  new PreparedStatementDecorator(con.prepareStatement( cadena ,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            logger.info("conSeccServiciosFormatoEstatico-->"+cadena+" ->"+codigoFactira);
            ps.setString( 1,codigoFactira );
            mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery( )));
            int num= Integer.parseInt(mapa.get("numRegistros").toString());
            //davgommo MT 3463
            // Se arrgela el m閠odo para que utilize las validaciones para los c骴igos de servicios 
        	String tipoCod="SELECT factu.convenio AS conv,  conve.tipo_codigo AS tipo, conve.institucion AS ins FROM facturacion.facturas factu,  facturacion.convenios conve WHERE factu.codigo="+codigoFactira+" AND factu.convenio=conve.codigo";
        	PreparedStatement pst1=null;
			ResultSet rs1=null;
			pst1=  con.prepareStatement( tipoCod,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			rs1=pst1.executeQuery();
			rs1.next();
			int codSer= 42;
			if (rs1.getString("tipo")!=null)
			{
		     codSer=rs1.getInt("tipo");
			} 
			int convenio=rs1.getInt("conv");
			int institucion=rs1.getInt("ins");
		    pst1=null;
		    rs1=null;
            for (int i=0; i<num; i++)
            {
              String codigoant= mapa.get("servicio_"+i).toString();	
              String obtenerCodServicio= "select facturacion.getcodigosoat("+codigoant+", convenios.tipo_codigo) AS codigo FROM CONVENIOS where convenios.codigo="+convenio;
				String obtenerDescServicio= "select facturacion.getdescripcioncodigosoat("+codigoant+", convenios.tipo_codigo) AS codigo FROM CONVENIOS where convenios.codigo="+convenio;
				PreparedStatement pst2=null;
				ResultSet rs2=null;
				pst2=  con.prepareStatement( obtenerCodServicio,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
				rs2=pst2.executeQuery();
				rs2.next();
				int codNuevo=0;
				if (rs2.getString("codigo")!=null) //Verificamos si el servicio tiene un codigo definido para el tipo de codigo servicio del convenio 
				{codNuevo=rs2.getInt("codigo");
				mapa.put("codigopropeitario_"+i, codNuevo);
				PreparedStatement pst3=null;
				ResultSet rs3=null;
				pst3=  con.prepareStatement( obtenerDescServicio,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
				rs3=pst3.executeQuery();
				rs3.next();
				String nuevoDesc=rs3.getString("codigo");
				mapa.put("procedimiento_"+i, nuevoDesc);
				rs3.close();
				pst3.close();
				}
				else { // si el servicio no tiene definido un codigo para el convenio, se busca si tiene definido uno para el tipo de codigo en el parametro codigo manual estandar servicios
					String codSerDefault=ValoresPorDefecto.getCodigoManualEstandarBusquedaServiciosLargo(institucion);
					String[] valorD=codSerDefault.split("@@");
					if (!codSerDefault.equals("@@"))
					{obtenerCodServicio= "select distinct facturacion.getcodigosoat("+codigoant+","+valorD[0]+") AS codigo FROM CONVENIOS where convenios.codigo="+convenio;
					obtenerDescServicio= "select facturacion.getdescripcioncodigosoat("+codigoant+","+valorD[0]+") AS codigo FROM CONVENIOS where convenios.codigo="+convenio;
					PreparedStatement pst3=null;
					ResultSet rs3=null;
					pst3=  con.prepareStatement( obtenerCodServicio,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
					rs3=pst3.executeQuery();
					rs3.next();
					if (rs3.getString("codigo")!=null) //Si no tiene codigo para el tipo de codigo en el parametro se deja el axioma 
					{
					codNuevo=rs3.getInt("codigo");
					mapa.put("codigopropeitario_"+i, codNuevo);
					PreparedStatement pst4=null;
					ResultSet rs4=null;
					pst4=  con.prepareStatement( obtenerDescServicio,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
					rs4=pst4.executeQuery();
					rs4.next();
					String nuevoDesc=rs4.getString("codigo");
					mapa.put("procedimiento_"+i, nuevoDesc);
					rs4.close();
					pst4.close();
					} else 
					{
						mapa.put("codigopropeitario_"+i, codigoant);	
					}
					rs3.close();
					pst3.close();		
				}}
				rs2.close();
				pst2.close();
				}
            
        } 
        catch ( SQLException e ) 
        {
            logger.error( "Error en la consulta <consultarSeccionServiciosFormatoEstatico> en la clase <SqlBaseImpresionFacturaDao> error: "+e);
            e.printStackTrace( );
        }        
        return ( HashMap ) mapa.clone( );
    }
    /**
     * metodo para consultar el detalle de los servicios
     * @param con Connection
     * @param numSolicitud String 
     * @return HashMap
     */
    public static HashMap consultarSeccionDetServiciosFormatoEstatico( Connection con,String codigoDetalleFactura ) 
    {
        HashMap mapa=new HashMap( );
        mapa.put( "numRegistros","0" );
        PreparedStatementDecorator ps;
        try 
        {
            ps =  new PreparedStatementDecorator(con.prepareStatement( conDetAsoSerCirugiaFormatoEstatico ,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            //logger.info("conDetAsoSerCirugiaFormatoEstatico-->"+conDetAsoSerCirugiaFormatoEstatico+" ->"+codigoDetalleFactura);
            ps.setString( 1,codigoDetalleFactura );
            mapa=UtilidadBD.cargarValueObjectSinNumRegistros(new ResultSetDecorator(ps.executeQuery( )));
        } 
        catch (SQLException e) 
        {
            logger.error( "Error en la consulta <consultarSeccionDetServiciosFormatoEstatico> en la clase <SqlBaseImpresionFacturaDao> error: "+e );
            e.printStackTrace( );
        }        
        return ( HashMap ) mapa.clone( );
    }
    /**
     * metodo para consultar los articulos de la factura
     * @param con Connection
     * @param codigoFactira  String 
     * @return HashMap
     */
    public static HashMap consultarSeccionArticulosFormatoEstatico( Connection con,String codigoFactira, String filtrarXInsumos ) 
    {
    	/**
    	 * Consulta de los articulos de una factura agrupados, utilizada en el formato de impresion estatico.
    	 */
    	String conSeccArticulosFormatoEstatitco="select " +
                                                    "f.codigo as codigofactura," +
                                                    "dfs.articulo as codigoarticulo," +
                                                    "replace(inventarios.getDescAltArticuloSinCodigo(dfs.articulo),'   ','') as descarticulo," +
                                                    "sum(dfs.cantidad_cargo) as cantidad," +
                                                    "sum(dfs.valor_total)/sum(dfs.cantidad_cargo) as valorunitario," +
                                                    "sum(dfs.valor_total) as valortotal " +
                                                "from facturas f " +
                                                    "inner join det_factura_solicitud dfs on (f.codigo=dfs.factura) " +
                                                    "inner join articulo a on(a.codigo=dfs.articulo) "+
                                                    "inner join naturaleza_articulo na on(na.acronimo=a.naturaleza and na.institucion=a.institucion) " +
                                                "where f.codigo=? and dfs.servicio is null ";
                                                
        if(!UtilidadTexto.isEmpty(filtrarXInsumos))                                        
        {
        	if(UtilidadTexto.getBoolean(filtrarXInsumos))
        	{
        		conSeccArticulosFormatoEstatitco+=" and na.es_medicamento='"+ConstantesBD.acronimoNo+"' ";
        	}
        	else
        	{
        		conSeccArticulosFormatoEstatitco+=" and na.es_medicamento='"+ConstantesBD.acronimoSi+"' ";
        	}
        }
                                                
        conSeccArticulosFormatoEstatitco+=      "group by f.codigo,dfs.articulo,3 " +
                                                "having sum(dfs.valor_total)>0 " + //mostrar solo los que tienen valor_total>0, los que tiene 0 son excentos.
                                                "order by descarticulo";
    	
    	
    	
        HashMap mapa=new HashMap( );
        mapa.put( "numRegistros","0" );
        PreparedStatementDecorator ps;
        try 
        {
        	logger.info("conSeccArticulosFormatoEstatitco-->"+conSeccArticulosFormatoEstatitco+" ->"+codigoFactira);
            ps =  new PreparedStatementDecorator(con.prepareStatement( conSeccArticulosFormatoEstatitco ,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            ps.setString( 1,codigoFactira );           
            mapa=UtilidadBD.cargarValueObjectSinNumRegistros(new ResultSetDecorator(ps.executeQuery( )));
            
            //Incidencia 3463 davgommo
            // Se hacen validaciones para que traiga el codigo segun el tipo de codigo dado por el convenio
            int num= Integer.parseInt(mapa.get("numRegistros").toString());
            String tipoCod="SELECT factu.convenio AS conv,  conve.tipo_codigo_articulos AS tipo, conve.institucion AS ins FROM facturacion.facturas factu,  facturacion.convenios conve WHERE factu.codigo="+codigoFactira+" AND factu.convenio=conve.codigo";
        	PreparedStatement pst1=null;
			ResultSet rs1=null;
			pst1=  con.prepareStatement( tipoCod,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			rs1=pst1.executeQuery();
			rs1.next();
			int codArt= 42;
			if (rs1.getString("tipo")!=null)
			{
		     codArt=rs1.getInt("tipo");
			} 
			int institucion=rs1.getInt("ins");
		    pst1=null;
		    rs1=null;
		    for (int i=0; i<num; i++)
            {
		    	int codigoant=Integer.parseInt(mapa.get("codigoarticulo_"+i).toString());
		    	if (codArt==0) //Si el c骴igo definido para el convenio es CUM
				{
					String obtenerCUM= "select distinct facturacion.getcodigocumarticulo(articulo.codigo) AS codigo FROM inventarios.articulo WHERE articulo.codigo="+codigoant;
					PreparedStatement pst2=null;
					ResultSet rs2=null;
					pst2=  con.prepareStatement( obtenerCUM,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
					rs2=pst2.executeQuery();
					rs2.next();
					int codNuevo=rs2.getInt("codigo");
					rs2.close();
					pst2.close();
					mapa.put("codigoarticulo_"+i, codNuevo);
				}else {
					if (codArt!=1) //El codigo axioma lo trae por defecto, pero si tipo de codigo no es axioma ni cum entra a la validacion
					{
						String codDefault=ValoresPorDefecto.getCodigoManualEstandarBusquedaArticulos(institucion);
						if (!codDefault.equals("AXM")) //si el codigo estandar no es axioma trae el codigo interfaz
						{
							String obtenerInterfaz= "SELECT DISTINCT facturacion.getcodigointerfaz(articulo.codigo) AS codigo FROM inventarios.articulo WHERE articulo.codigo="+codigoant;
							PreparedStatement pst2=null;
							ResultSet rs2=null;
							pst2=  con.prepareStatement( obtenerInterfaz,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
							rs2=pst2.executeQuery();
							rs2.next();
							int m=0;
							int codNuevo=rs2.getInt("codigo");
							rs2.close();
							pst2.close();
							mapa.put("codigoarticulo_"+i, codNuevo);
						}
					
					}
				}
		    	
            }
        } 
        catch (SQLException e) 
        {
            logger.error( "Error en la consulta <consultarSeccionArticulosFormatoEstatico> en la clase <SqlBaseImpresionFacturaDao> error: "+e );
            e.printStackTrace( );
        }        
        return ( HashMap ) mapa.clone( );
    }
    
    
	/**
	 * 
	 * @param con
	 * @param codigoFactira
	 * @return
	 */
	public static HashMap anexoSolicitudesMedicamentosFechaFactura(Connection con, String codigoFactura) 
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros","0");
		PreparedStatementDecorator ps,ps1;
		ResultSetDecorator rs;
		try 
		{
			ps =  new PreparedStatementDecorator(con.prepareStatement(consultaAnexoMedicamentosOredenesFecha,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1,codigoFactura);
			rs=new ResultSetDecorator(ps.executeQuery());
			int cont=0;
			while(rs.next())
			{
				mapa.put("fechasolicitud_"+cont,rs.getObject("fechasolicitud"));
				mapa.put("totalsolicitudes_"+cont,rs.getObject("totalsolicitudes"));
				ps1= new PreparedStatementDecorator(con.prepareStatement(consultaMedicamentosAgrupadosFechaSolicitud,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ps1.setDate(1,rs.getDate("fechasolicitud"));
				ps1.setString(2,codigoFactura);
				mapa.put("detalleArticulos_"+cont,UtilidadBD.cargarValueObject(new ResultSetDecorator(ps1.executeQuery())).clone());
				cont++;
			}
			mapa.put("numRegistros",cont+"");
		} 
		catch (SQLException e) 
		{
			logger.error("Error en la consulta <anexoSolicitudesMedicamentosFechaFactura> en la clase <SqlBaseImpresionFacturaDao> error: "+e);
			e.printStackTrace();
		}
		return (HashMap) mapa.clone();
	}
	
	/**
	 * 	
	 * @param con
	 * @param codigoFactira
	 * @return
	 */
	public static HashMap anexoSolicitudesMedicamentosOrdenFechaFactura(Connection con, String codigoFactura) 
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros","0");
		PreparedStatementDecorator ps,ps1,ps2;
		ResultSetDecorator rs,rs1;
		try 
		{
			ps =  new PreparedStatementDecorator(con.prepareStatement(consultaAnexoMedicamentosOredenesFecha,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1,codigoFactura);
			rs=new ResultSetDecorator(ps.executeQuery());
			int cont=0;
			while(rs.next())
			{
				mapa.put("fechasolicitud_"+cont,rs.getObject("fechasolicitud"));
				mapa.put("totalsolicitudes_"+cont,rs.getObject("totalsolicitudes"));
				ps1= new PreparedStatementDecorator(con.prepareStatement(consultaSolMedicamentosFechaSolicitud,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ps1.setDate(1,rs.getDate("fechasolicitud"));
				ps1.setString(2,codigoFactura);
				rs1=  new ResultSetDecorator(new ResultSetDecorator(ps1.executeQuery()));
				HashMap mapa1=new HashMap();
				mapa1.put("numRegistros","0");
				int cont2=0;
				while(rs1.next())
				{
					mapa1.put("numerosolicitud_"+cont2,rs1.getObject("numerosolicitud"));
					mapa1.put("consecutivoorden_"+cont2,rs1.getObject("consecutivoorden"));
					mapa1.put("totalsolicitud_"+cont2,rs1.getObject("totalsolicitud"));
					ps2= new PreparedStatementDecorator(con.prepareStatement(consultaMedicamentosSolicitud,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					ps2.setString(1,rs1.getString("numerosolicitud"));
					ps2.setString(2,codigoFactura);
					mapa1.put("detalleArticulos_"+cont2,UtilidadBD.cargarValueObject(new ResultSetDecorator(ps2.executeQuery())).clone());
					cont2++;
				}
				mapa1.put("numRegistros",cont2+"");
				mapa.put("detalleOrden_"+cont,mapa1);
				cont++;
			}
			mapa.put("numRegistros",cont+"");
		} 
		catch (SQLException e) 
		{
			logger.error("Error en la consulta <anexoSolicitudesMedicamentosOrdenFechaFactura> en la clase <SqlBaseImpresionFacturaDao> error: "+e);
			e.printStackTrace();
		}
		return (HashMap) mapa.clone();
	}
	
	/**
	 * 
	 * @param con
	 * @param codSubCuenta
	 * @return
	 */
	public static HashMap consultarInfoCitaDadaCuenta(Connection con, String codSubCuenta) 
	{

		HashMap mapa=new HashMap();
		mapa.put("numRegistros","0");
		PreparedStatementDecorator ps;
		try 
		{
			ps =  new PreparedStatementDecorator(con.prepareStatement(consultaCitasDadaCuenta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			logger.info("consultaCitasDadaCuenta-->"+consultaCitasDadaCuenta+" ->"+codSubCuenta);
			ps.setString(1,codSubCuenta);
			mapa=(HashMap)UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery())).clone();
		} 
		catch (SQLException e) 
		{
			logger.error("Error en la consulta <anexoSolicitudesMedicamentosOrdenFechaFactura> en la clase <SqlBaseImpresionFacturaDao> error: "+e);
			e.printStackTrace();
		}
		return (HashMap) mapa.clone();
	}
	
	
	public static  DtoDatosRepsonsableFactura obtenerNombreResponsable(Connection con,Integer cuenta, Integer convenio) throws SQLException{
		Integer tipoConvenio =new Integer(0); 
		DtoDatosRepsonsableFactura dtoDatosRepsonsableFactura = new DtoDatosRepsonsableFactura();

		String consultaEsparticular=" select ctc.codigo from convenios c join TIPOS_CONVENIO tc "+
		" on (c.TIPO_CONVENIO= tc.codigo and  c.INSTITUCION=tc.INSTITUCION) "+
		" join CLASIFICACION_TIPO_CONVENIO ctc "+
		" on (tc.clasificacion=ctc.codigo) "+
		" where c.codigo = ?";

		String consultaObtenerResponsable=" select rp.PRIMER_NOMBRE||' '||rp.SEGUNDO_NOMBRE||' '|| rp.PRIMER_APELLIDO||' '|| RP.SEGUNDO_APELLIDO datosResp" +
				"  ,  rp.direccion ,  rp.telefono from cuentas c "+
		" join RESPONSABLES_PACIENTES rp  "+
		" on(c.CODIGO_RESPONSABLE_PACIENTE=rp.CODIGO) "+
		" where c.id= ?";

		PreparedStatement ps = con.prepareStatement(consultaEsparticular);
		ps.setInt(1, convenio);
		ResultSet rs = ps.executeQuery();
		if(rs.next()){
			tipoConvenio=rs.getInt("codigo");
		}

		if(tipoConvenio.equals(9)){
			PreparedStatement ps2=con.prepareStatement(consultaObtenerResponsable);
			ps2.setInt(1, cuenta);
			ResultSet rs2 = ps2.executeQuery();
			if(rs2.next()){
				dtoDatosRepsonsableFactura.setDireccionResponsable(rs2.getString("direccion"));
				dtoDatosRepsonsableFactura.setNombreResponsable(rs2.getString("datosResp"));
				dtoDatosRepsonsableFactura.setTelefonoResponsable(rs2.getString("telefono"));
			}else{
				dtoDatosRepsonsableFactura=null;
			}
			rs2.close();
			ps2.close();
		}
		ps.close();
		rs.close();
		
		
		return dtoDatosRepsonsableFactura;

	}
	
	/**
	 * @param con
	 * @param convenio
	 * @param ingreso
	 * @return
	 * @throws SQLException
	 */
	public static String consultarTipoMontoFactura(Connection con,Integer convenio, Integer ConsecutivoIngreso) throws SQLException{
		String nombreTipoMonto="";
		Integer idIngreso = new Integer (0);

		String copnsultaIdIngreso="select  id from ingresos  where consecutivo = ?";
		PreparedStatement ps2 = con.prepareStatement(copnsultaIdIngreso);
		ps2.setString(1,ConsecutivoIngreso+"");
		ResultSet rs2 = ps2.executeQuery();
		if(rs2.next()){
			idIngreso= rs2.getInt("id");
		}

		rs2.close();
		ps2.close();

		if(idIngreso>0){

			String consultaMontoCobro=" select tm.nombre from sub_cuentas sc join detalle_monto dm on (sc.MONTO_COBRO=dm.DETALLE_CODIGO) "+
			" join tipos_monto tm on (dm.TIPO_MONTO_CODIGO=tm.CODIGO) "+
			" where sc.convenio = ?  "+
			" and sc.ingreso=? ";
			PreparedStatement ps = con.prepareStatement(consultaMontoCobro);
			ps.setInt(1, convenio);
			ps.setInt(2, idIngreso);
			ResultSet rs = ps.executeQuery();
			if(rs.next()){
				nombreTipoMonto=rs.getString("nombre");
			}
			rs.close();
			ps.close();
		}
		return nombreTipoMonto;

	}
	

}
