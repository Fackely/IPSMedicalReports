/*
 * @(#)SqlBaseAjustesEmpresaDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */
package com.princetonsa.dao.sqlbase;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.princetonsa.decorator.PreparedStatementDecorator;
import java.sql.SQLException;
import com.princetonsa.decorator.ResultSetDecorator;
import java.util.HashMap;
import org.apache.log4j.Logger;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

/**
 * Implementación sql genérico de todas las funciones de acceso a la fuente de datos
 * para los ajustes de empresa
 *
 * @version 1.0, Julio 22 / 2005	
 * @author <a href="mailto:armando@PrincetonSA.com">Jorge Armando Osorio Velasquez</a>
 */
public class SqlBaseAjustesEmpresaDao 
{
	/**
	* Objeto para manejar los logs de esta clase
	*/
	private static Logger logger = Logger.getLogger(SqlBaseAjustesEmpresaDao.class);
	
	/**
	 * Cadena para insertar el ajuste de una factura.
	 */
	private static String insertarAjusteFactura="INSERT INTO ajus_fact_empresa(codigo,factura,metodo_ajuste,valor_ajuste,concepto_ajuste,institucion) values (?,?,?,?,?,?)";
	
	/**
	 * Cadena para insertar el ajuste de un servicio de una factura especifica
	 */
	private static String insertarAjusteDetalleFactura="INSERT INTO ajus_det_fact_empresa(codigo_pk,codigo,factura,det_fact_solicitud,pool,codigo_medico_responde,metodo_ajuste,valor_ajuste,valor_ajuste_pool,valor_ajuste_institucion,concepto_ajuste,institucion) values(?,?,?,?,?,?,?,?,?,?,?,?)";
	
	/**
	 * Cadena general para la consulta de las facturas, las condiciones se adcionan
	 * en cada metido puesto que puden cambia de acuerdo a lo que se requiere.
	 */
	private static String consultaFacturas="SELECT f.consecutivo_factura as consecutivofactura,f.numero_cuenta_cobro as cuentacobro,f.centro_aten as codigocentroatencion,getnomcentroatencion(f.centro_aten) as nombrecentroatencion,f.convenio as codigoconvenio,getnombreconvenio(f.convenio) as nombreconvenio,f.valor_cartera as valorcartera,f.ajustes_credito as ajustescredito,f.ajustes_debito as ajustesdebito,f.valor_pagos as pagos,(((f.valor_cartera+f.ajustes_debito)-f.ajustes_credito)-f.valor_pagos) as saldofactura,(((f.valor_total+f.ajustes_debito)-f.ajustes_debito)) as totalfactura,f.tipo_factura_sistema as facturasistema from facturas f left outer join cuentas_cobro cc on(cc.numero_cuenta_cobro=f.numero_cuenta_cobro) ";
	
	/**
	 * Cadena para subir los servicios relacionados a una factura específica.
	 */
	private static String consultarDetalleFacturaServicios="SELECT dfs.codigo as codigodetalle,dfs.solicitud as solicitud,case when s.tipo="+ConstantesBD.codigoTipoSolicitudCirugia+" then 'true' else 'false' end as escirugia,dfs.servicio as codigoservart,ser.especialidad||' - '||ser.codigo as codigoaxioma,getnombreservicio(dfs.servicio,0) as nombreservart,1 as esservicio,s.codigo_medico_responde as codigomedico,administracion.getnombremedico(s.codigo_medico_responde) as nombremedico,'' as loginmedico,s.pool as codigopool,p.descripcion as nombrepool,(valor_total - ajustes_credito + ajustes_debito) as saldo from det_factura_solicitud dfs inner join solicitudes s on(dfs.solicitud=s.numero_solicitud) inner join servicios ser on (dfs.servicio=ser.codigo) left outer join pooles p on(s.pool=p.codigo) where dfs.articulo is null and factura=? order by getnombreservicio(dfs.servicio,0)";
	/**
	 * Cadena para subir los servicios relacionados a un ajuste.
	 */
	private static String consultarDetalleFacturaServiciosAjustes="SELECT dfs.codigo as codigodetalle,dfs.solicitud as solicitud,case when s.tipo="+ConstantesBD.codigoTipoSolicitudCirugia+" then 'true' else 'false' end as escirugia,dfs.servicio as codigoservart,ser.especialidad||' - '||ser.codigo as codigoaxioma,getnombreservicio(dfs.servicio,0) as nombreservart,1 as esservicio,s.codigo_medico_responde as codigomedico,administracion.getnombremedico(s.codigo_medico_responde) as nombremedico,'' as loginmedico,s.pool as codigopool,p.descripcion as nombrepool,(valor_total - ajustes_credito + ajustes_debito) as saldo,adfe.codigo_pk as codigopk,adfe.metodo_ajuste as metodoajuste,adfe.valor_ajuste as valorajuste,adfe.valor_ajuste as valorajusteoriginal,adfe.valor_ajuste_pool as valorajustepool,adfe.valor_ajuste_institucion as valorajusteinstitucion,adfe.concepto_ajuste as conceptoajuste from det_factura_solicitud dfs inner join solicitudes s on(dfs.solicitud=s.numero_solicitud) inner join servicios ser on (dfs.servicio=ser.codigo) left outer join pooles p on(s.pool=p.codigo) inner join ajus_det_fact_empresa adfe on(dfs.codigo=adfe.det_fact_solicitud)where dfs.articulo is null and dfs.factura=? and  adfe.codigo=? order by getnombreservicio(dfs.servicio,0)";
	
	/**
	 * Cadena para subir los servicios relacionados a una factura específica.
	 */
	private static String consultarDetalleFacturaArticulos="select -1 as codigodetalle,-1 as solicitud,'false' as escirugia,dfs.articulo as codigoservart,'' as codigoaxioma, getdescarticulo(dfs.articulo) as nombreservart,0 as esservicio,-1 as codigomedico,'' as nombremedico,'' as loginmedico,-1 as codigopool,'' as nombrepool,sum((valor_total - ajustes_credito + ajustes_debito)) as saldo from det_factura_solicitud dfs inner join articulo a on (dfs.articulo=a.codigo) where dfs.servicio is null and factura=? group by dfs.articulo,a.descripcion order by a.descripcion";
	
	/**
	 * Cadena para subir los servicios relacionados a un ajuste.
	 */
	private static String consultarDetalleFacturaArticulosAjustes="select -1 as codigodetalle,-1 as solicitud,'false' as escirugia,dfs.articulo as codigoservart,'' as codigoaxioma, getdescarticulo(dfs.articulo) as nombreservart,0 as esservicio,-1 as codigomedico,'' as nombremedico,'' as loginmedico,-1 as codigopool,'' as nombrepool,sum((valor_total - ajustes_credito + ajustes_debito)) as saldo,-1 as codigopk,adfe.metodo_ajuste as metodoajuste,sum(adfe.valor_ajuste) as valorajuste,sum(adfe.valor_ajuste) as valorajusteoriginal,sum(adfe.valor_ajuste_pool) as valorajustepool,sum(adfe.valor_ajuste_institucion) as valorajusteinstitucion,adfe.concepto_ajuste as conceptoajuste from det_factura_solicitud dfs inner join articulo a on (dfs.articulo=a.codigo) inner join ajus_det_fact_empresa adfe on(dfs.codigo=adfe.det_fact_solicitud) where dfs.servicio is null and dfs.factura=? and  adfe.codigo=? group by dfs.articulo,a.descripcion,adfe.metodo_ajuste,adfe.concepto_ajuste order by a.descripcion";
	
	/**
	 * Cadena para actualizar el ajuste a niver de servicios articulo.
	 */
	private static String updateAjusteDetalle="UPDATE ajus_det_fact_empresa SET metodo_ajuste = ?,valor_ajuste=?,valor_ajuste_pool=?,valor_ajuste_institucion=?,concepto_ajuste=? where codigo_pk=?";
	
	/**
	 * Cadena genera para consultar una cuenta de cobro.
	 **/
	private static String consultaCuentaCobro="SELECT convenio as codigoconvenio,getnombreconvenio(convenio) as nombreconvenio,saldo_cuenta as saldo from cuentas_cobro where numero_cuenta_cobro=? and institucion=?";
	
	/**
	 * cadena para consultar las facturas de una cuenta de cobro, solo trae el codigo de la factura y el saldo(VC-AC+AD-PAGOS);
	 */
	private static String consultaFacutrasCuentaCobro="SELECT codigo as codigofactura,consecutivo_factura as consecutivofactura,centro_aten as codigocentroatencion,getnomcentroatencion(centro_aten) as nombrecentroatencion, (valor_cartera-ajustes_credito+ajustes_debito-valor_pagos) as saldofactura,(valor_total-ajustes_credito+ajustes_debito) as totalfactura,tipo_factura_sistema as facturasistema from facturas where numero_cuenta_cobro=? and institucion=? order by consecutivo_factura asc";
		
	/**
	 * Cadena para consultar la distribucion de un ajustes a nivel de facturas de una cuenta de cobro.
	 */
	private static String consultaFacutrasCuentaCobroAjuste="SELECT a.factura as codigofactura,f.consecutivo_factura as consecutivofactura,f.centro_aten as codigocentroatencion,getnomcentroatencion(f.centro_aten) as nombrecentroatencion, (f.valor_cartera-f.ajustes_credito+f.ajustes_debito-f.valor_pagos) as saldofactura,(valor_total-ajustes_credito+ajustes_debito) as totalfactura,a.metodo_ajuste as metodoajuste,a.valor_ajuste as valorajuste,a.concepto_ajuste as conceptoajuste,cac.descripcion as descconceptoajuste,f.tipo_factura_sistema as facturasistema from ajus_fact_empresa a inner join facturas f on a.factura=f.codigo inner join concepto_ajustes_cartera cac on a.concepto_ajuste=cac.codigo where a.codigo=? order by f.consecutivo_factura asc";
	
	
	/**
	 * Cadena para actualizar el ajuste a niver de servicios articulo.
	 */
	private static String updateAjusteFactura="UPDATE ajus_fact_empresa SET metodo_ajuste = ?,valor_ajuste=?,concepto_ajuste=? where codigo=? and factura=? and institucion=?";
	
	/**
	 * Cadena para consultar el Ecabezado Ajuste
	 */
	private static String cadenaConsultaEncabezadoAjsute="SELECT codigo as codigo,castigo_cartera as castigocartera,concepto_castigo_cartera as conceptocastigocartera,to_char(fecha_ajuste,'"+ConstantesBD.formatoFechaBD+"') as fechaajuste,cuenta_cobro as cuentacobro,concepto_ajuste as conceptoajuste,metodo_ajuste as metodoajuste,valor_ajuste as valorajuste,observaciones as observaciones from ajustes_empresa where consecutivo_ajuste = ? and tipo_ajuste in (?,?) and institucion = ? and estado=?";
	
	/**
	 * Cadena para actualizar la informacion general de un ajuste.
	 */
	//private static String actualizarAjusteGeneral="UPDATE ajustes_empresa SET castigo_cartera=?,concepto_castigo_cartera=?,fecha_ajuste=?,fecha_elaboracion=?,hora_elaboracion=?,concepto_ajuste=?,metodo_ajuste=?,valor_ajuste=?,observaciones=? where codigo=?";
	private static String actualizarAjusteGeneral="UPDATE ajustes_empresa SET castigo_cartera=?,concepto_castigo_cartera=?,fecha_ajuste=?,concepto_ajuste=?,metodo_ajuste=?,valor_ajuste=?,observaciones=? where codigo=?";
	
	/**
	 * Cadena para cambiar el estado de un ajuste
	 */
	private static String cambiarEstadoAjusteStr="UPDATE ajustes_empresa SET estado=? where codigo=?";
	
	/**
	 * Cadena para insertar una fila en la tabla anulacion_ajustes_empresa
	 */
	private static String insertarAnulacionAjuste="INSERT INTO anulacion_ajustes_empresa (codigo_ajuste,usuario,fecha_anulacion,hora_anulacion,motivo) values(?,?,?,?,?)";
	
	/**
	 * Cadena para insertar la reversion de un ajuste a nivel de ajutes_empresa
	 */
	private static String cadenaInsercionAjusteReversion="INSERT INTO ajustes_empresa (codigo,consecutivo_ajuste,tipo_ajuste,institucion,castigo_cartera,concepto_castigo_cartera,fecha_ajuste,fecha_elaboracion,hora_elaboracion,usuario,cuenta_cobro,concepto_ajuste,metodo_ajuste,valor_ajuste,observaciones,estado,ajuste_reversado,cod_ajuste_reversado) (select ?,?,?,institucion,castigo_cartera,concepto_castigo_cartera,?,?,?,?,cuenta_cobro,concepto_ajuste,metodo_ajuste,valor_ajuste,?,?,?,? from ajustes_empresa where codigo=?)";
	
	/**
	 * Cadena para insertar la reversion de un ajuste a nivel de ajus_fact_empresa
	 */
	private static String cadenaIsertarAjusteFacturaReversion="INSERT INTO ajus_fact_empresa(codigo,factura,metodo_ajuste,valor_ajuste,concepto_ajuste,institucion) (select ?,factura,metodo_ajuste,valor_ajuste,concepto_ajuste,institucion from ajus_fact_empresa where codigo=?)";

	/**
	 * Cadena para insertar la reversion de un ajuste a nivel de ajus_det_fact_empresa
	 */
	private static String cadenaIsertarAjusteServiciosFacturaReversion="INSERT INTO ajus_det_fact_empresa (" +
																										" codigo_pk," +
																										" codigo," +
																										" factura," +
																										" det_fact_solicitud," +
																										" pool," +//5
																										" codigo_medico_responde," +
																										" metodo_ajuste," +
																										" valor_ajuste," +
																										" valor_ajuste_pool," +
																										" valor_ajuste_institucion," +//10
																										" concepto_ajuste," +
																										" institucion" +//12
																										" ) values (" +
																										" ?,?,?,?,?," +
																										" ?,?,?,?,?," +
																										" ?,?)";
	
   
    
    /**
	 * Cadena para acturalizar el atributo ajuste_reversado.
	 */
	private static String strCambiarAtributoReversion="UPDATE ajustes_empresa SET ajuste_reversado = ? WHERE codigo= ?";
	
	/**
	 * Cadena para realizar la busqueda por fecha de los ajustes aprobados
	 */
	private static String cadenaConsulaAjusteReversionPorFecha="SELECT " +
																		" DISTINCT " +
																		" a.codigo as codigo, " +
																		" a.tipo_ajuste as tipoajuste, " +
																		" a.consecutivo_ajuste as consecutivoajuste, " +
																		" to_char(a.fecha_ajuste,'yyyy-mm-dd') as fechaajuste, " +
																		" a.valor_ajuste as valorajuste, " +
																		" a.cuenta_cobro as cuentacobro, " +
																		" case when a.cuenta_cobro is null then f.consecutivo_factura else null end as factura, " +
																		" case when a.cuenta_cobro is null then getnombreconvenio(f.convenio) else getnombreconvenio(cxc.convenio) end as convenio, " +
																		" case when a.cuenta_cobro is null then administracion.getnombremedico(f.cod_paciente) else null end as nombrepaciente, " +
																		" case when a.cuenta_cobro is null then 'false' else 'true' end as ajustecuentacobro" +
																		" from ajustes_empresa a " +
																		" inner join ajus_fact_empresa afe on (afe.codigo=a.codigo) " +
																		" left outer join cuentas_cobro cxc on(cxc.numero_cuenta_cobro=a.cuenta_cobro) " +
																		" left outer join facturas f on (f.codigo=afe.factura) " +
																		" where a.fecha_ajuste=? and a.estado=?";
	
	
    /**
     * Cadena para la consulta de asocios det factura de los servicios de cirugia.
     */
    private static String cadenaBusquedaAsociosDetFacutr="select distinct " +
                                                            " adf.consecutivo as consecitivoasodetfac," +
                                                            " adf.codigo as codigodetfactura," +
                                                            " adf.servicio_asocio as codigoservicio," +
                                                            " ser.especialidad||' - '||ser.codigo as codigoaxioma," +
                                                            " ta.codigo_asocio as acronimoasocio," +
                                                            " getnombretipoasocio(ta.codigo) as nombreasocio," +
                                                            " getnombreservicio(adf.servicio_asocio,0) as nombreservicio," +
                                                            " coalesce(adf.codigo_medico,"+ConstantesBD.codigoNuncaValido+") as codigomedico," +
                                                            " coalesce(administracion.getnombremedico(adf.codigo_medico),'') as nombremedico," +
                                                            " '' as loginmedico," +
                                                            " coalesce(adf.pool,"+ConstantesBD.codigoNuncaValido+") as codigopool," +
                                                            " coalesce(getdescripcionpool(adf.pool),'') as nombrepool," +
                                                            " (adf.valor_total - adf.ajustes_credito + adf.ajustes_debito) as saldo," +
                                                            " case when ta.tipos_servicio IN('"+ConstantesBD.codigoServicioHonorariosCirugia+"','"+ConstantesBD.codigoServicioProcedimiento+"') then 'true'   else 'false' end  as eshonorarios," +
                                                            " adf.porcentaje_pool as porcentajepool," +
                                                            " adf.porcentaje_medico as porcentajemedico " +
                                                       " from asocios_det_factura adf " +
                                                       " inner join servicios ser on (adf.servicio_asocio=ser.codigo) " +
                                                       " inner join det_factura_solicitud dfs on(adf.codigo=dfs.codigo) " +
                                                       " inner join solicitudes s on(dfs.solicitud=s.numero_solicitud) " +
                                                       " inner join sol_cirugia_por_servicio scps on(scps.numero_solicitud=dfs.solicitud and scps.servicio=dfs.servicio) " +
                                                       " inner join tipos_asocio ta on (adf.tipo_asocio=ta.codigo) " +
                                                       " where adf.codigo=?";
                                                  //pendiente por saber si es requerido o no
    												/*" UNION " +
                                                      " select distinct " +
														" -1 as consecitivoasodetfac," +
														" dfs.codigo as codigodetfactura," +
													    " dfs.articulo as codigoservicio," +
													    " '' as codigoaxioma," +
													    " '' as acronimoasocio," +
													    " '' as nombreasocio," +
													    " getdescarticulo(dfs.articulo) as nombreservicio," +
													    " -1 as codigomedico," +
													    " '' as nombremedico," +
													    " '' as loginmedico," +
													    " -1 as codigopool," +
													    " '' as nombrepool," +
													    " sum((valor_total - ajustes_credito + ajustes_debito)) as saldo," +
													    " 'false' as eshonorarios," +
													    " -1 as porcentajepool," +
													    " -1 as porcentajemedico " +
													" from det_factura_solicitud dfs " +
													" inner join sol_cirugia_por_servicio scps on(scps.numero_solicitud=dfs.solicitud and dfs.articulo not is null) " +
													" where dfs.codigo=?";
                                                 
                                                      
                                                     
                                                       
                                                        
                                                     
                                                       
                                                      
                                                     
                                                     
                                                       
                                                       
                                                     
                                         
    /**
     * Cadena para la consulta de asocios det factura de los servicios de cirugia.
     */
    private static String cadenaBusquedaDetallePaquete="SELECT " +
    															" pdf.codigo as codpaqdetfac," +
    															" pdf.codigo_det_fact as codigodetfactura," +
    															" pdf.servicio," +
    															" pdf.articulo," +
    															" coalesce(pdf.porcentaje_pool,0) as porcentajepool," +
    															" (pdf.valor_total - pdf.ajustes_credito + pdf.ajustes_debito) as saldo " +
    															//" coalesce(dfs.valor_consumo_paquete,0) as valorconsumopaquete " +
    													" from facturacion.paquetizacion_det_factura pdf " +
    													" where pdf.codigo_det_fact=?";
    
    /**
     * Cadena para consultar la distribucion de los ajustes a nivel de asocios.
     */
    private static String cadenaConsultarServiciosAsociosAjustes="SELECT " +
                                                                    " consecutivo_aso_det_fac as consecitivoasodetfac," +
                                                                    " a1.det_aso_fac_solicitud as codigodetfactura," +
                                                                    " a1.servicio_asocio as codigoservicio," +
                                                                    " a1.institucion as institucion, " +
                                                                    " s.especialidad||' - '||adf.servicio_asocio as codigoaxioma," +
                                                                    " ta.codigo_asocio as acronimoasocio," +
                                                                    " getnombretipoasocio(ta.codigo) as nombreasocio," +
                                                                    " dcx.medico as codigomedico," +
                                                                    " administracion.getnombremedico(dcx.medico) as nombremedico," +
                                                                    " dcx.pool as codigopool," +
                                                                    " getdescripcionpool(dcx.pool) as descpool," +
                                                                    " (adf.valor_total-adf.ajustes_credito+adf.ajustes_debito) as saldo," +
                                                                    " a1.valor_ajuste as valorajuste," +
                                                                    " a1.concepto_ajuste as concepto," +
                                                                    " cac.descripcion as descconcepto " +
                                                              " from ajus_asocios_fact_empresa a1 " +
                                                              " inner join det_factura_solicitud dfs on (dfs.codigo=a1.det_aso_fac_solicitud) " +
                                                              " inner join sol_cirugia_por_servicio scx on (dfs.solicitud=scx.numero_solicitud and dfs.servicio=scx.servicio) " +
                                                              " inner join asocios_det_factura adf on (a1.consecutivo_aso_det_fac=adf.consecutivo) " +
                                                              " left outer join det_cx_honorarios dcx on(scx.codigo=dcx.cod_sol_cx_servicio and adf.servicio_asocio=dcx.servicio and adf.tipo_asocio=dcx.tipo_asocio ) " +
                                                              " left outer join det_asocio_cx_salas_mat dacx on (scx.codigo=dacx.cod_sol_cx_servicio and adf.servicio_asocio=dacx.servicio and adf.tipo_asocio=dacx.tipo_asocio) " +
                                                              " inner join servicios s on(adf.servicio_asocio=s.codigo) " +
                                                              " inner join tipos_asocio ta on (dacx.tipo_asocio=ta.codigo or dcx.tipo_asocio=ta.codigo) " +
                                                              " inner join concepto_ajustes_cartera cac on (cac.codigo=a1.concepto_ajuste) " +
                                                              //" where a1.codigo_ajuste=? and a1.factura=? and a1.det_aso_fac_solicitud=?" +
                                                              " where a1.codigo_pk_ser_art=?" +
                                                              " order by nombreasocio";
    
    
    
    
	/**
	 * Metodo para incertar el encabezado de un ajuste, esta informacion y la general del ajuste.
	 * @param con
	 * @param cadena, sentencia SQL se recibe ya que varia de acuerdo al motor da Base de datos.
	 * @param codigo, codigo del ajuste, se obtiene a traves de la secuencia seq_ajustes_empresa.
	 * @param consecutivo, consecutivo parametrizable por cada institucion.
	 * @param tipo_ajuste, tipo de ajuste debito factura, debito cuenta cobro, credito factura, credito cuenta cobro.
	 * @param institucion.
	 * @param castigo_cartera, indica si el ajuste es un castigo o no.
	 * @param conceptoCastigoCartera, en caso de que sea castigo se requiere el cocepto de castigo.
	 * @param fechaAjuste
	 * @param fechaElaboracion
	 * @param horaElaboracion
	 * @param usuario
	 * @param cuentaCobro
	 * @param conceptoAjuste, concepto de porque se hizo el ajuste.
	 * @param metodoAjuste, metodo de ajuste A-Automatico, P-Porcentual, M-Manual
	 * @param valorAjuste
	 * @param observaciones
	 * @param estado, Estado del ajuste, Generado-Aprobado-Anulado
	 * @param codAjusteReversado
	 * @param reversado
	 * @param insertaAjusteGeneral 
	 * @return >0 en caso de que no se genere error en la insercion.
	 */
	public static int ingresarAjusteGeneral(Connection con,String consecutivo, int tipo_ajuste, int institucion, boolean castigo_cartera, 
								String conceptoCastigoCartera, String fechaAjuste,String fechaElaboracion,String horaElaboracion,String usuario, 
								double cuentaCobro,String conceptoAjuste,String metodoAjuste,double valorAjuste,String observaciones, int estado, boolean reversado, double codAjusteReversado, String insertaAjusteGeneral )
	{
		int result=ConstantesBD.codigoNuncaValido;
		 PreparedStatement pst=null;
	        try 
	        {   
	        	pst =  con.prepareStatement(insertaAjusteGeneral,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
	            pst.setString(1,consecutivo);
	            pst.setInt(2,tipo_ajuste);
	            pst.setInt(3,institucion);
	            pst.setBoolean(4,castigo_cartera);
	            if(conceptoCastigoCartera==null||conceptoCastigoCartera.equals("null")||conceptoCastigoCartera.equals(""))
	            {
	            	pst.setObject(5,null);
	            }
	            else
	            {
	            	pst.setString(5,conceptoCastigoCartera);
	            }
	            pst.setDate(6,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(fechaAjuste)));
	            pst.setDate(7,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(fechaElaboracion)));
	            pst.setString(8,horaElaboracion);
	            pst.setString(9,usuario);
	            if(cuentaCobro<=0)
	            {
	            	pst.setObject(10,null);	
	            }
	            else
	            {
	            	pst.setDouble(10,cuentaCobro);
	            }
	            pst.setString(11,conceptoAjuste);
	            pst.setString(12,metodoAjuste);
	            pst.setDouble(13,valorAjuste);
	            pst.setString(14,observaciones);
	            pst.setInt(15,estado);
	            pst.setBoolean(16,reversado);
	            if(codAjusteReversado>0)
	            {
	            	pst.setDouble(17,codAjusteReversado);
	            }
	            else
	            {
	            	pst.setObject(17,null);
	            }
	            result=pst.executeUpdate();
	            
	        }catch(Exception e){
				Log4JManager.error("ERROR ingresarAjusteGeneral", e);
			}
			finally{
				try{
					if(pst != null){
						pst.close();
					}
				}
				catch (SQLException sql) {
					Log4JManager.error("ERROR cerrando objetos persistentes", sql);
				}
			}
	        return result;
	}

	/**
	 * Metodo para ingresar el ajuste referente a una factura.
	 * @param con
	 * @param codigo, Codigo del ajuste, el mismo obtenido para la insercion del ajuste general.
	 * @param factura, FActura a la que se le aplica el ajuste.
	 * @param metodoAjuste, Metodo de Ajuste utilizado.
	 * @param valorAjuste
	 * @param conceptoAjuste
	 * @param institucion
	 * @return >0 en caso de que no se genere error en la insercion.
	 */
	public static int ingresarAjustesFactura(Connection con,double codigo,int factura, String metodoAjuste, double valorAjuste,String conceptoAjuste,int institucion)
	{
		PreparedStatement pst=null;
		int result=ConstantesBD.codigoNuncaValido;
        try 
        {   
            pst =  con.prepareStatement(insertarAjusteFactura,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
            pst.setDouble(1,codigo);
            pst.setInt(2,factura);
            pst.setString(3,metodoAjuste);
            pst.setDouble(4,valorAjuste);
            pst.setString(5,conceptoAjuste);
            pst.setInt(6,institucion);
            result= pst.executeUpdate();
            
        }catch(Exception e){
			Log4JManager.error("ERROR ingresarAjustesFactura", e);
		}
		finally{
			try{
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException sql) {
				Log4JManager.error("ERROR cerrando objetos persistentes", sql);
			}
		}
        return result;
	}
	
	/**
	 * Metodo para ingresa el detalle de los ajustes de una factura.
	 * @param con, Conexion
	 * @param codigo, Codigo del ajuste, el mismo obtenido para la insercion del ajuste general. 
	 * @param factura, factura,
	 * @param detFacturaSolicitud, detalle de la factura, de esta forma se hace referencia directa a los servicios y los articulos.
	 * @param pool, pool del medico que respondio el servicio.
	 * @param codigoMedicoResponsable
	 * @param metodoAjuste
	 * @param valorAjuste
	 * @param valorAjustePool
	 * @param valorAjusteInstitucion
	 * @param conceptoAjuste
	 * @param institucion
	 * @return
	 */
	public static int ingresarAjustedDetalleFactura(Connection con, double codigo, int factura, int detFacturaSolicitud, int pool,int codigoMedicoResponsable,
								String metodoAjuste,double valorAjuste,double valorAjustePool, double valorAjusteInstitucion,String conceptoAjuste,int institucion)
	{
		PreparedStatement pst=null;
		int result=ConstantesBD.codigoNuncaValido;
        try 
        {   
            pst =  con.prepareStatement(insertarAjusteDetalleFactura,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
            int codigoPKTempo=UtilidadBD.obtenerSiguienteValorSecuencia(con, "cartera.seq_ajusdetfacemp_pk");
            pst.setInt(1,codigoPKTempo);
            pst.setDouble(2,codigo);
            pst.setInt(3,factura);
            pst.setInt(4,detFacturaSolicitud);
            if(pool==ConstantesBD.codigoNuncaValido||pool==0)
            {
            	pst.setObject(5,null);
            }
            else
            {
            	pst.setInt(5,pool);
            }
            if(codigoMedicoResponsable==ConstantesBD.codigoNuncaValido||codigoMedicoResponsable==0)
            {
            	pst.setObject(6,null);
            }
            else
            {
            	pst.setInt(6,codigoMedicoResponsable);
            }
            pst.setString(7,metodoAjuste);
            pst.setDouble(8,valorAjuste);
            pst.setDouble(9,valorAjustePool);
            pst.setDouble(10,valorAjusteInstitucion);
            pst.setString(11,conceptoAjuste);
            pst.setInt(12,institucion);
            if(pst.executeUpdate()>0)
            	result=codigoPKTempo;
        	else
        		result=ConstantesBD.codigoNuncaValido;
            
        } catch(Exception e){
			Log4JManager.error("ERROR ingresarAjustedDetalleFactura", e);
		}
		finally{
			try{
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException sql) {
				Log4JManager.error("ERROR cerrando objetos persistentes", sql);
			}
		}
		return result;
	}

	/**
	 * @param con
	 * @param codigoFactura
	 * @param castigoCartera
	 * @param ajustesCxCRadicada
	 * @param modificacion
	 * @param codigoAjuste
	 * @return
	 */
	public static ResultSetDecorator cargarUnaFactura(Connection con, int codigoFactura, boolean castigoCartera, boolean ajustesCxCRadicada, boolean modificacion, double codigoAjuste) 
	{
		String consulta=consultaFacturas;
		PreparedStatementDecorator pst=null;
		try
		{
			if(!modificacion)
			{
				if(castigoCartera)
				{
					consulta+="where cc.estado="+ConstantesBD.codigoEstadoCarteraRadicada;
				}
				else
				{
					if(ajustesCxCRadicada)
					{
						consulta+="where (f.numero_cuenta_cobro is null or cc.estado in ("+ConstantesBD.codigoEstadoCarteraGenerado+","+ConstantesBD.codigoEstadoCarteraRadicada+"))";
					}
					else
					{
						consulta+="where (f.numero_cuenta_cobro is null or cc.estado = "+ConstantesBD.codigoEstadoCarteraGenerado+")";
					}
				}
				consulta+=" and f.factura_cerrada="+ValoresPorDefecto.getValorFalseParaConsultas()+" and f.consecutivo_factura=?";
				pst= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				//pst.setBoolean(1,false);
				pst.setInt(1,codigoFactura);
				
				logger.info("\n\n\ncargarUnaFactura:\nCodigo Factura:"+codigoFactura+"\n"+consulta);

			}
			else
			{
				consulta+="inner join ajus_fact_empresa afe on (afe.factura=f.codigo) inner join ajustes_empresa ae on (ae.codigo=afe.codigo) where ae.codigo=?";
				pst= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				pst.setDouble(1,codigoAjuste);
				logger.info("\n\n\ncargarUnaFactura:\ncodigoAjuste:"+codigoAjuste+"\n"+consulta);

			}
			return new ResultSetDecorator(pst.executeQuery());
			
		}
		catch(SQLException e)
		{
			logger.error("Error Consultando Facturas por fecha "+e);
			return null;
		}
	}

	/**
	 * @param con
	 * @param codigoFactura
	 * @param servicio
	 * @return
	 */
	public static ResultSetDecorator cargarDetalleFactura(Connection con, int codigoFactura, boolean servicio) 
	{
		logger.info("\n cargarDetalleFactura query:"+consultarDetalleFacturaServicios+" codigoFactura -->"+codigoFactura+" servicio-->"+servicio);
		try
		{
			String consulta="";
			if(servicio)
				consulta=consultarDetalleFacturaServicios;
			else
				consulta=consultarDetalleFacturaArticulos;
            
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			logger.info("\n\n\ncargarDetalleFactura:\ncodigoFactura:"+codigoFactura+"\n"+consulta);
			pst.setInt(1,codigoFactura);
			return new ResultSetDecorator(pst.executeQuery());
			
		}
		catch(SQLException e)
		{
			logger.error("Error Consultando El Detalle De La Factura"+e);
		}
		return null;
	}

	/**
	 * @param con
	 * @param codigo
	 * @param factura
	 * @param detFacturaSolicitud
	 * @param metodoAjuste
	 * @param valorAjuste
	 * @param valorAjustePool
	 * @param valorAjusteInstitucion
	 * @param conceptoAjuste
	 * @return
	 */
	public static int updateAjustedDetalleFactura(Connection con,int codigo_pk, double codigo, int factura, int detFacturaSolicitud, String metodoAjuste, double valorAjuste, double valorAjustePool, double valorAjusteInstitucion, String conceptoAjuste,int institucion) 
	{
		PreparedStatement pst=null;
		int result=ConstantesBD.codigoNuncaValido;
        try 
        {   
            pst =  con.prepareStatement(updateAjusteDetalle,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
            pst.setString(1,metodoAjuste);
            pst.setDouble(2,valorAjuste);
            pst.setDouble(3,valorAjustePool);
            pst.setDouble(4,valorAjusteInstitucion);
            pst.setString(5,conceptoAjuste);
            pst.setInt(6,codigo_pk);
            result=pst.executeUpdate();
            
        } catch(Exception e){
			Log4JManager.error("ERROR updateAjustedDetalleFactura", e);
		}
		finally{
			try{
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException sql) {
				Log4JManager.error("ERROR cerrando objetos persistentes", sql);
			}
		}
		return result;
	}

	/**
	 * @param con
	 * @param codigoAjuste
	 * @param codigoFactura
	 * @param servicio
	 * @return
	 */
	public static ResultSetDecorator cargarDetalleAjusteFactura(Connection con, double codigoAjuste, int codigoFactura, boolean servicio) 
	{
		logger.info("\n entro a cargarDetalleAjusteFactura codigo ajuste--> "+codigoAjuste+"  codigo factura --> "+codigoFactura+" esservicio -->"+servicio);
		try
		{
			String consulta="";
			if(servicio)
				consulta=consultarDetalleFacturaServiciosAjustes;
			else
				consulta=consultarDetalleFacturaArticulosAjustes;
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			//logger.info("\n\n\ncargarDetalleAjusteFactura:\ncodigoFactura:"+codigoFactura+" codigoAjuste:"+codigoAjuste+"\n"+consulta);
			pst.setInt(1,codigoFactura);
			pst.setDouble(2,codigoAjuste);
			return new ResultSetDecorator(pst.executeQuery());
			
		}
		catch(SQLException e)
		{
			logger.error("Error Consultando El Detalle De La Factura"+e);
		}
		return null;
	}

	/**
	 * @param con
	 * @param codigoFactura
	 * @param codigoArticuloAgrupacion
	 * @return
	 */
	public static ResultSetDecorator cargarDetalleFacturasArticuloAgrupado(Connection con, int codigoFactura, int codigoArticuloAgrupacion)
	{
		try
		{
			String consulta="SELECT codigo as codigodetalle,(valor_total - ajustes_credito + ajustes_debito) as saldo from det_factura_solicitud where factura=? and articulo=?";
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			//logger.info("\n\n\ncargarDetalleFacturasArticuloAgrupado:\ncodigoFactura:"+codigoFactura+" codigoArticuloAgrupacion:"+codigoArticuloAgrupacion+"\n"+consulta);
			pst.setInt(1,codigoFactura);
			pst.setInt(2,codigoArticuloAgrupacion);
			return new ResultSetDecorator(pst.executeQuery());
		}
		catch(SQLException e)
		{
			logger.error("Error Consultando El Detalle De La Factura"+e);
		}
		return null;
	}

	/**
	 * @param con
	 * @param cuentaCobro
	 * @param institucion
	 * @param castigoCartera
	 * @param ajustesCxCRadicada
	 * @param modificacion
	 * @return
	 */
	public static ResultSetDecorator cargarCuentaCobro(Connection con, double cuentaCobro, int institucion, boolean castigoCartera, boolean ajustesCxCRadicada, boolean modificacion) 
	{
		String consulta=consultaCuentaCobro;
		if(!modificacion)
		{
			if(castigoCartera)
			{
				consulta+=" and estado="+ConstantesBD.codigoEstadoCarteraRadicada;
			}
			else
			{
				if(ajustesCxCRadicada)
				{
					consulta+=" and estado in ("+ConstantesBD.codigoEstadoCarteraGenerado+","+ConstantesBD.codigoEstadoCarteraRadicada+")";
				}
				else
				{
					consulta+=" and estado = "+ConstantesBD.codigoEstadoCarteraGenerado;
				}
			}
		}
		try
		{
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			//logger.info("\n\n\ncargarCuentaCobro:\ncuentaCobro:"+cuentaCobro+" institucion:"+institucion+"\n"+consulta);
			
			pst.setDouble(1,cuentaCobro);
			pst.setInt(2,institucion);
			return new ResultSetDecorator(pst.executeQuery());
			
		}
		catch(SQLException e)
		{
			logger.error("Error Consultando Cuenta Cobro "+e);
			return null;
		}
	}

	/**
	 * @param con
	 * @param numeroCuentaCobro
	 * @return
	 */
	public static ResultSetDecorator cargarFacturasCuentaCobro(Connection con, double numeroCuentaCobro, int institucion)
	{
		try
		{
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(consultaFacutrasCuentaCobro,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			//logger.info("\n\n\ncargarFacturasCuentaCobro:\nnumeroCuentaCobro:"+numeroCuentaCobro+" institucion:"+institucion+"\n"+consultaFacutrasCuentaCobro);
			pst.setDouble(1,numeroCuentaCobro);
			pst.setInt(2,institucion);
			return new ResultSetDecorator(pst.executeQuery());
			
		}
		catch(SQLException e)
		{
			logger.error("Error Consultando El Detalle De La Factura"+e);
		}
		return null;
	}

	/**
	 * @param con
	 * @param codigoAjuste
	 * @return
	 */
	public static ResultSetDecorator cargarFacturasCuentaCobroAjuste(Connection con, double codigoAjuste) 
	{
		try
		{
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(consultaFacutrasCuentaCobroAjuste,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			//logger.info("\n\n\ncargarFacturasCuentaCobroAjuste:\ncodigoAjuste:"+codigoAjuste+"\n"+consultaFacutrasCuentaCobroAjuste);
			pst.setDouble(1,codigoAjuste);
			return new ResultSetDecorator(pst.executeQuery());
			
		}
		catch(SQLException e)
		{
			logger.error("Error Consultando La factura Ajuste"+e);
		}
		return null;
	}

	/**
	 * @param con
	 * @param codigo
	 * @param factura
	 * @param metodoAjuste
	 * @param valorAjuste
	 * @param conceptoAjuste
	 * @param institucion
	 * @return
	 */
	public static int updateAjusteFacturaEmpresa(Connection con, double codigo, int factura, String metodoAjuste, double valorAjuste, String conceptoAjuste, int institucion) 
	{
		PreparedStatement pst=null;
		int result=ConstantesBD.codigoNuncaValido;
        try 
        {   
            pst =  con.prepareStatement(updateAjusteFactura,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
            pst.setString(1,metodoAjuste);
            pst.setDouble(2,valorAjuste);
            pst.setString(3,conceptoAjuste);
            pst.setDouble(4,codigo);
            pst.setInt(5,factura);
            pst.setInt(6,institucion);
            result= pst.executeUpdate();
            
        } 
        catch(Exception e){
			Log4JManager.error("ERROR updateAjusteFacturaEmpresa", e);
		}
		finally{
			try{
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException sql) {
				Log4JManager.error("ERROR cerrando objetos persistentes", sql);
			}
		}
		return result;
	}

	/**
	 * @param con
	 * @param codigoAjuste
	 * @return
	 */
	public static void eliminarAjuste(Connection con, double codigoAjuste,int nivel) 
	{
		PreparedStatement pst=null;
		PreparedStatement pst2=null;
		PreparedStatement pst3=null;
		PreparedStatement pst4=null;
		PreparedStatement pst5=null;
		
		try 
        {  
            if(nivel==1||nivel==2||nivel==3||nivel==4)
            {
                pst = con.prepareStatement("DELETE from ajus_asocios_fact_empresa where codigo_ajuste=?",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
                pst.setDouble(1,codigoAjuste);
                pst.executeUpdate();
                
                pst2 =  con.prepareStatement("DELETE from ajus_paquetizacion_det_factura where codigo_ajuste=?",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
                pst2.setDouble(1,codigoAjuste);
                pst2.executeUpdate();
            }
        	if(nivel==1||nivel==2||nivel==3)
        	{
		        pst3 = con.prepareStatement("DELETE from ajus_det_fact_empresa where codigo=?",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
		        pst3.setDouble(1,codigoAjuste);
		        pst3.executeUpdate();
        	}
        	if(nivel==1||nivel==2)
        	{
        		pst4 = con.prepareStatement("DELETE from ajus_fact_empresa where codigo=?",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
	            pst4.setDouble(1,codigoAjuste);
	            pst4.executeUpdate();
        	}
        	if(nivel==1)
        	{
		        pst5 = con.prepareStatement("DELETE from ajustes_empresa where codigo=?",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
		        pst5.setDouble(1,codigoAjuste);
		        pst5.executeUpdate();
        	}

        } 
		catch(Exception e){
			Log4JManager.error("ERROR eliminarAjuste", e);
		}
		finally{
			try{
				if(pst != null){
					pst.close();
				}
				if(pst2 != null){
					pst2.close();
				}
				if(pst3 != null){
					pst3.close();
				}
				if(pst4 != null){
					pst4.close();
				}
				if(pst5 != null){
					pst5.close();
				}
			}
			catch (SQLException sql) {
				Log4JManager.error("ERROR cerrando objetos persistentes", sql);
			}
		}
	}

	/**
	 * @param con
	 * @param codigoAjuste
	 * @param codFacturas
	 */
	public static int eliminarAjusteFacturaNoEstan(Connection con, double codigoAjuste, String codFacturas) 
	{
		PreparedStatement pst=null;
		PreparedStatement pst2=null;
		PreparedStatement pst3=null;
		PreparedStatement pst4=null;
		
		int result=ConstantesBD.codigoNuncaValido;
        try 
        {
            String cadena="DELETE FROM ajus_asocios_fact_empresa where codigo_ajuste=? and factura not in ("+codFacturas+")";
            pst = con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
            pst.setDouble(1,codigoAjuste);
            pst.executeUpdate();
            
            
            pst2 =  con.prepareStatement("DELETE from ajus_paquetizacion_det_factura where codigo_ajuste=? and factura not in("+codFacturas+")",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
            pst2.setDouble(1,codigoAjuste);
            pst2.executeUpdate();
            
            cadena="DELETE FROM ajus_det_fact_empresa where codigo=? and factura not in ("+codFacturas+")";
            pst3 = con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
            pst3.setDouble(1,codigoAjuste);
            pst3.executeUpdate();
            cadena="DELETE FROM ajus_fact_empresa where codigo=? and factura not in ("+codFacturas+")";
            pst4 = con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
            pst4.setDouble(1,codigoAjuste);
            result=pst4.executeUpdate();
        } 
        catch(Exception e){
			Log4JManager.error("ERROR eliminarAjusteFacturaNoEstan", e);
		}
		finally{
			try{
				if(pst != null){
					pst.close();
				}
				if(pst2 != null){
					pst2.close();
				}
				if(pst3 != null){
					pst3.close();
				}
				if(pst4 != null){
					pst4.close();
				}
			}
			catch (SQLException sql) {
				Log4JManager.error("ERROR cerrando objetos persistentes", sql);
			}
		}
		return result;
		
	}

	/**
	 * @param con
	 * @param codigo
	 * @param factura
	 * @return
	 */
	public static int eliminarAjusteServicio(Connection con, double codigo, int factura) 
	{
		PreparedStatement pst=null;
		PreparedStatement pst2=null;
		PreparedStatement pst3=null;
		int result=ConstantesBD.codigoNuncaValido;
        try 
        {
            String cadena="DELETE FROM ajus_asocios_fact_empresa where codigo_ajuste=? and factura =?";
            pst =  con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
            pst.setDouble(1,codigo);
            pst.setInt(2,factura);
            pst.executeUpdate();
            
            
            pst2 = con.prepareStatement("DELETE from ajus_paquetizacion_det_factura where codigo_ajuste=? and factura=?",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
            pst2.setDouble(1,codigo);
            pst2.setInt(2, factura);
            pst2.executeUpdate();
            
        	cadena="DELETE FROM ajus_det_fact_empresa where codigo=? and factura =?";
            pst3 =  con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
            pst3.setDouble(1,codigo);
            pst3.setInt(2,factura);
            result= pst3.executeUpdate();
        } 
        catch(Exception e){
			Log4JManager.error("ERROR eliminarAjusteServicio", e);
		}
		finally{
			try{
				if(pst != null){
					pst.close();
				}
				if(pst2 != null){
					pst2.close();
				}
				if(pst3 != null){
					pst3.close();
				}
			}
			catch (SQLException sql) {
				Log4JManager.error("ERROR cerrando objetos persistentes", sql);
			}
		}
		return result;
	}

	/**
	 * @param con
	 * @param consecutivoAjuste
	 * @param institucion
	 * @param estado
	 * @param codigoAjustes
	 * @return
	 */
	public static ResultSetDecorator cargarEncabezadoAjuste(Connection con, String consecutivoAjuste, String tipoAjuste, int institucion, int estado) 
	{
		try
		{
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(cadenaConsultaEncabezadoAjsute,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setString(1,consecutivoAjuste);
			if(tipoAjuste.equals(ConstantesBD.ajustesCreditoFuncionalidadAjustes.getAcronimo()))
			{
				pst.setInt(2,ConstantesBD.codigoAjusteCreditoCuentaCobro);
				pst.setInt(3,ConstantesBD.codigoAjusteCreditoFactura);
			}
			else if(tipoAjuste.equals(ConstantesBD.ajustesDebitoFuncionalidadAjustes.getAcronimo()))
			{
				pst.setInt(2,ConstantesBD.codigoAjusteDebitoCuentaCobro);
				pst.setInt(3,ConstantesBD.codigoAjusteDebitoFactura);
			}
			pst.setInt(4,institucion);
			pst.setInt(5,estado);
			return new ResultSetDecorator(pst.executeQuery());
		}
		catch(SQLException e)
		{
			logger.error("Error Consultando La factura Ajuste"+e);
		}
		return null;
	}

	/**
	 * @param con
	 * @param codigo
	 * @param castigoCartera
	 * @param conceptoCastigoCartera
	 * @param fechaAjuste
	 * @param fechaElaboracion
	 * @param horaElaboracion
	 * @param conceptoAjuste
	 * @param metodoAjuste
	 * @param valorAjuste
	 * @param observaciones
	 * @return
	 */
	public static int actualizarAjuste(Connection con, double codigo, boolean castigoCartera, String conceptoCastigoCartera, String fechaAjuste, String fechaElaboracion, String horaElaboracion, String conceptoAjuste, String metodoAjuste, double valorAjuste, String observaciones) 
	{
		PreparedStatement pst=null;
		int result=ConstantesBD.codigoNuncaValido;
        try 
        {   
        	pst = con.prepareStatement(actualizarAjusteGeneral,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
        	pst.setBoolean(1,castigoCartera);
            if(conceptoCastigoCartera==null||conceptoCastigoCartera.equals("null")||conceptoCastigoCartera.equals(""))
            {
            	pst.setObject(2,null);
            }
            else
            {
            	pst.setString(2,conceptoCastigoCartera);
            }
            pst.setDate(3,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(fechaAjuste)));
            pst.setString(4,conceptoAjuste);
            pst.setString(5,metodoAjuste);
            pst.setDouble(6,valorAjuste);
            pst.setString(7,observaciones);
            pst.setDouble(8,codigo);
            result=pst.executeUpdate();
            
        }
        catch(Exception e){
			Log4JManager.error("ERROR actualizarAjuste", e);
		}
		finally{
			try{
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException sql) {
				Log4JManager.error("ERROR cerrando objetos persistentes", sql);
			}
		}
        return result;	
    }

	/**
	 * @param con
	 * @param codigoAjuste
	 * @param codigoEstadoCarteraAnulado
	 * @return
	 */
	public static int cambiarEstadoAjuste(Connection con, double codigoAjuste, int codigoEstadoCarteraAnulado) 
	{
		PreparedStatement pst=null;
		int result=ConstantesBD.codigoNuncaValido;
        try 
        {   
        	pst = con.prepareStatement(cambiarEstadoAjusteStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
        	pst.setInt(1,codigoEstadoCarteraAnulado);
            pst.setDouble(2,codigoAjuste);
            result=pst.executeUpdate();
            
        } catch(Exception e){
			Log4JManager.error("ERROR cambiarEstadoAjuste", e);
		}
		finally{
			try{
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException sql) {
				Log4JManager.error("ERROR cerrando objetos persistentes", sql);
			}
		}
        return result;
	}

	/**
	 * @param con
	 * @param codigoFactura
	 * @param campoBusqueda
	 * @param valorCampoBusqueda
	 * @param servicios
	 * @return
	 */
	public static ResultSetDecorator cargarDetalleFacturaAvanzada(Connection con, int codigoFactura, String campoBusqueda, String valorCampoBusqueda, boolean servicios) 
	{
		try
		{
			String consulta="";
			String busAvanzada="";
			//"solicitud""nomServicioArticulo""pool"
			if(campoBusqueda.equals("solicitud")&&servicios)
			{
				busAvanzada="and dfs.solicitud="+valorCampoBusqueda;
			}
			else if(campoBusqueda.equals("nomServicioArticulo"))
			{
				busAvanzada="and upper(getnombreservicio(dfs.servicio,0)) like upper('%"+valorCampoBusqueda+"%')";
			}
			else if(campoBusqueda.equals("pool")&&servicios)
			{
				busAvanzada="and upper(p.descripcion) like upper('%"+valorCampoBusqueda+"%')";
			}
			if(servicios)
            {
                consulta="SELECT dfs.codigo as codigodetalle,dfs.solicitud as solicitud,case when s.tipo="+ConstantesBD.codigoTipoSolicitudCirugia+" then 'true' else 'false' end as escirugia,dfs.servicio as codigoservart,ser.especialidad||' - '||ser.codigo as codigoaxioma,getnombreservicio(dfs.servicio,0) as nombreservart,1 as esservicio,s.codigo_medico_responde as codigomedico,administracion.getnombremedico(s.codigo_medico_responde) as nombremedico,'' as loginmedico,s.pool as codigopool,p.descripcion as nombrepool,(valor_total - ajustes_credito + ajustes_debito) as saldo from det_factura_solicitud dfs inner join solicitudes s on(dfs.solicitud=s.numero_solicitud) inner join servicios ser on (dfs.servicio=ser.codigo) left outer join pooles p on(s.pool=p.codigo) where dfs.articulo is null and factura=? "+busAvanzada+" order by getnombreservicio(dfs.servicio,0)";
            }
			else
            {
				consulta="select -1 as codigodetalle,-1 as solicitud,'false' as escirugia,dfs.articulo as codigoservart,'' as codigoaxioma,a.descripcion as nombreservart,0 as esservicio,-1 as codigomedico,'' as nombremedico,'' as loginmedico,-1 as codigopool,'' as nombrepool,sum((valor_total - ajustes_credito + ajustes_debito)) as saldo from det_factura_solicitud dfs inner join articulo a on (dfs.articulo=a.codigo) where dfs.servicio is null and factura=? "+busAvanzada+" group by dfs.articulo,a.descripcion order by a.descripcion";
            }
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			//logger.info("\n\n\ncargarDetalleFacturaAvanzada:\ncodigoFactura:"+codigoFactura+"\n"+consulta);
			
			pst.setInt(1,codigoFactura);
			return new ResultSetDecorator(pst.executeQuery());
			
		}
		catch(SQLException e)
		{
			logger.error("Error Consultando El Detalle De La Factura"+e);
		}
		return null;	}

	/**
	 * @param con
	 * @param numeroCuentaCobro
	 * @param institucion
	 * @param valorCampoBusqueda
	 * @return
	 */
	public static ResultSetDecorator cargarFacturasCuentaCobroAvanzada(Connection con, double numeroCuentaCobro, int institucion, String valorCampoBusqueda) 
	{
		try
		{
			String cadena="SELECT codigo as codigofactura,consecutivo_factura as consecutivofactura,centro_aten as codigocentroatencion,getnomcentroatencion(centro_aten) as nombrecentroatencion, (valor_cartera-ajustes_credito+ajustes_debito-valor_pagos) as saldofactura,(valor_total-ajustes_credito+ajustes_debito) as totalfactura,tipo_factura_sistema as facturasistema from facturas where numero_cuenta_cobro=? and institucion=? and consecutivo_factura="+valorCampoBusqueda+" order by consecutivo_factura asc";
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			//logger.info("\n\n\ncargarFacturasCuentaCobroAvanzada:\nnumeroCuentaCobro:"+numeroCuentaCobro+" institucion:"+institucion+"\n"+cadena);
			pst.setDouble(1,numeroCuentaCobro);
			pst.setInt(2,institucion);
			return new ResultSetDecorator(pst.executeQuery());
			
		}
		catch(SQLException e)
		{
			logger.error("Error Consultando El Detalle De La Factura"+e);
		}
		return null;
	}

	/**
	 * @param con
	 * @param codigoAjuste
	 * @param motivoAnulacion
	 * @param loginUsuario
	 * @param fechaActual
	 * @param horaActual
	 * @return
	 */
	public static boolean anularAjuste(Connection con, double codigoAjuste, String motivoAnulacion, String loginUsuario, String fechaActual, String horaActual) 
	{
		PreparedStatement pst=null;
		boolean result=false;
		try 
        {   
        	pst =  con.prepareStatement(insertarAnulacionAjuste,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
            pst.setDouble(1,codigoAjuste);
            pst.setString(2,loginUsuario);
            pst.setDate(3,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(fechaActual)));
            pst.setString(4,horaActual);
            pst.setString(5,motivoAnulacion);
            result=pst.executeUpdate()>0;
            
        } catch(Exception e){
			Log4JManager.error("ERROR anularAjuste", e);
		}
		finally{
			try{
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException sql) {
				Log4JManager.error("ERROR cerrando objetos persistentes", sql);
			}
		}
        return result;
	}

	/**
	 * @param con
	 * @param codigo 
	 * @param consecutivoAjuste
	 * @param tipoAjuste
	 * @param fechaAjuste
	 * @param fechaElaboracion
	 * @param horaElaboracion
	 * @param usuario
	 * @param observaciones
	 * @param estado
	 * @param ajusteReversado
	 * @param codAjusteReversado
	 * @return
	 */
	public static boolean ingresarAjusteReversion(Connection con, int codigo, String consecutivoAjuste, int tipoAjuste, String fechaAjuste, String fechaElaboracion, String horaElaboracion, String usuario, String observaciones, int estado, boolean ajusteReversado, double codAjusteReversado,String cadenaIsertarAjusteServiciosCirugiaReversion,String cadenaIsertarAjusteDetallePaqueteReversion) 
	{
		PreparedStatement pst=null;
		PreparedStatement pst2=null;
		PreparedStatement pst3=null;
		PreparedStatement pst4=null;
		PreparedStatement pst5=null;
		PreparedStatement pst6=null;
		ResultSet rs=null;
		boolean result=false;
        try 
        {   
        	//insercion del encabezado
        	pst =  con.prepareStatement(cadenaInsercionAjusteReversion,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
            pst.setDouble(1,codigo);
            pst.setString(2,consecutivoAjuste);
            pst.setInt(3,tipoAjuste);
            pst.setDate(4,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(fechaAjuste)));
            pst.setDate(5,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(fechaElaboracion)));
            pst.setString(6,horaElaboracion);
            pst.setString(7,usuario);
            pst.setString(8,observaciones);
            pst.setInt(9,estado);
            pst.setBoolean(10,ajusteReversado);
            if(codAjusteReversado>0)
            {
            	pst.setDouble(11,codAjusteReversado);
            	pst.setDouble(12,codAjusteReversado);
            }
            else
            {
            	pst.setObject(11,null);
            	pst.setObject(12,null);
            }
            pst.executeUpdate();
            //insercion en la tabla ajus_fact_empresa
            pst2 = con.prepareStatement(cadenaIsertarAjusteFacturaReversion,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
            pst2.setDouble(1,codigo);
            pst2.setDouble(2,codAjusteReversado);
            pst2.executeUpdate();
            //insercion en la tabla ajus_det_fact_empresa
            pst3=con.prepareStatement("select codigo_pk as codigopk,factura,det_fact_solicitud,pool,codigo_medico_responde,metodo_ajuste,valor_ajuste,valor_ajuste_pool,valor_ajuste_institucion,concepto_ajuste,institucion from ajus_det_fact_empresa where codigo=?" );
            pst3.setDouble(1,codAjusteReversado);
            rs=pst3.executeQuery();
            while(rs.next())
            {
                pst4 = con.prepareStatement(cadenaIsertarAjusteServiciosFacturaReversion,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
                int codigoPKTempo=UtilidadBD.obtenerSiguienteValorSecuencia(con, "cartera.seq_ajusdetfacemp_pk");
                pst4.setInt(1,codigoPKTempo);
                pst4.setDouble(2,codigo);
                pst4.setInt(3,rs.getInt("factura"));
                if(Utilidades.convertirAEntero(rs.getInt("det_fact_solicitud")+"")>0)
                	pst4.setInt(4,rs.getInt("det_fact_solicitud"));
                else
                	pst4.setObject(4, null);
                if(Utilidades.convertirAEntero(rs.getInt("pool")+"")>0)
                	pst4.setInt(5,rs.getInt("pool"));
                else
                	pst4.setObject(5, null);
                logger.info("--->"+rs.getInt("codigo_medico_responde")+"<---");
                if(Utilidades.convertirAEntero(rs.getInt("codigo_medico_responde")+"")>0)
                	pst4.setInt(6,rs.getInt("codigo_medico_responde"));
                else
                	pst4.setObject(6, null);
                pst4.setString(7, rs.getString("metodo_ajuste"));
                pst4.setDouble(8, rs.getDouble("valor_ajuste"));
                pst4.setDouble(9, rs.getDouble("valor_ajuste_pool"));
                pst4.setDouble(10, rs.getDouble("valor_ajuste_institucion"));
                pst4.setString(11, rs.getString("concepto_ajuste"));
                pst4.setInt(12,rs.getInt("institucion"));
                pst4.executeUpdate();
                pst4.close();
                
                //insercion en la tabal ajus_asocios_fact_empresa
                pst5 =  con.prepareStatement(cadenaIsertarAjusteServiciosCirugiaReversion,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
                pst5.setInt(1,codigoPKTempo);
                pst5.setDouble(2,codigo);
                pst5.setInt(3,rs.getInt("codigopk"));
                pst5.executeUpdate();
                pst5.close();
                
                //insercion en la tabal ajus_paquetizacion_det_factura
                pst6 =  con.prepareStatement(cadenaIsertarAjusteDetallePaqueteReversion,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
                pst6.setInt(1,codigoPKTempo);
                pst6.setDouble(2,codigo);
                pst6.setInt(3,rs.getInt("codigopk"));
                pst6.executeUpdate();
                pst6.close();
            }
            result= true;
        } catch(Exception e){
			Log4JManager.error("ERROR ingresarAjusteReversion", e);
		}
		finally{
			try{
				if(rs != null){
					rs.close();
				}
				if(pst3 != null){
					pst3.close();
				}
				if(pst != null){
					pst.close();
				}
				if(pst2 != null){
					pst2.close();
				}
			}
			catch (SQLException sql) {
				Log4JManager.error("ERROR cerrando objetos persistentes", sql);
			}
		}
        return result;
	}

	/**
	 * @param con
	 * @param codigoAjuste
	 * @param valorCampo
	 * @return
	 */
	public static boolean cambiarAtributoReversion(Connection con, double codigoAjuste, boolean valorCampo) 
	{
		PreparedStatement pst=null;
		boolean result=false;
		try 
        {   
        	pst =  con.prepareStatement(strCambiarAtributoReversion,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
        	pst.setBoolean(1,valorCampo);
        	pst.setDouble(2,codigoAjuste);
        	result=pst.executeUpdate()>0;
            
        } catch(Exception e){
			Log4JManager.error("ERROR cambiarAtributoReversion", e);
		}
		finally{
			try{
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException sql) {
				Log4JManager.error("ERROR cerrando objetos persistentes", sql);
			}
		}
        return result;
	}

	/**
	 * @param con
	 * @param fechaAjuste
	 * @param institucion
	 * @return
	 */
	public static ResultSetDecorator buscarAjustesAprobadosPorFechaParaReversion(Connection con, String fechaAjuste, int institucion) 
	{
		try
		{
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(cadenaConsulaAjusteReversionPorFecha,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setDate(1,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(fechaAjuste)));
			pst.setInt(2,institucion);
			return new ResultSetDecorator(pst.executeQuery());
			
		}
		catch(SQLException e)
		{
			logger.error("Error Consultando El Detalle De La Factura"+e);
		}
		return null;
	}

    /**
     * 
     * @param con
     * @param detFactSolicitud
     * @return
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public static HashMap cargarAsociosServiciosCirugia(Connection con, int detFactSolicitud)
    {
        logger.info("\n entro a cargarAsociosServiciosCirugia -->"+detFactSolicitud);
    	HashMap mapa=new HashMap();
    	PreparedStatementDecorator pst=null;
    	ResultSetDecorator rs=null;
        mapa.put("numRegistros", "0");
        try
        {
            logger.info((cadenaBusquedaAsociosDetFacutr+""));
            pst= new PreparedStatementDecorator(con.prepareStatement(cadenaBusquedaAsociosDetFacutr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            pst.setInt(1,detFactSolicitud);
            rs=new ResultSetDecorator(pst.executeQuery());
			mapa= UtilidadBD.cargarValueObject(rs);
            
        }
        catch(Exception e){
			Log4JManager.error("ERROR cargarAsociosServiciosCirugia", e);
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
			catch (SQLException sql) {
				Log4JManager.error("ERROR cerrando objetos persistentes", sql);
			}
		}
        return mapa;
    }

    
    

    /**
     * 
     * @param con
     * @param detFactSolicitud
     * @return
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public static HashMap cargarDetallePaquete(Connection con, int detFactSolicitud)
    {
        HashMap mapa=new HashMap();
        PreparedStatementDecorator pst=null;
        ResultSetDecorator rs=null;
        mapa.put("numRegistros", "0");
        try
        {
            pst= new PreparedStatementDecorator(con.prepareStatement(cadenaBusquedaDetallePaquete,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            pst.setInt(1,detFactSolicitud);
            rs=new ResultSetDecorator(pst.executeQuery());
           mapa=UtilidadBD.cargarValueObject(rs);
            
        }
        catch(Exception e){
			Log4JManager.error("ERROR cargarDetallePaquete", e);
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
			catch (SQLException sql) {
				Log4JManager.error("ERROR cerrando objetos persistentes", sql);
			}
		}
        return mapa;
    }
    /**
     * 
     * @param con
     * @param mapa
     * @return
     */
    @SuppressWarnings("rawtypes")
	public static boolean insertarAsociosServicio(Connection con, HashMap mapa)
    {
        String cadenaInsercionAjusAsocios="INSERT INTO ajus_asocios_fact_empresa(" +
        																		" codigo_pk," +
        																		" codigo_pk_ser_art," +
        																		" codigo_ajuste," +
        																		" factura," +
        																		" consecutivo_aso_det_fac," +
        																		" det_aso_fac_solicitud," +
        																		" servicio_asocio," +
        																		" codigo_medico," +
        																		" pool," +
        																		" valor_ajuste," +
        																		" valor_ajuste_pool," +
        																		" valor_ajuste_institucion," +
        																		" concepto_ajuste," +
        																		" institucion" +
        																	") values (" +
        																	" ?,?,?,?,?," +
        																	" ?,?,?,?,?," +
        																	" ?,?,?,?)";
        PreparedStatement pst=null;
        boolean result=false;
        try 
        {   
            //insercion del encabezado
            pst =  con.prepareStatement(cadenaInsercionAjusAsocios,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
            for(int k = 0 ; k < Integer.parseInt(mapa.get("numRegistros")+"") ; k ++)
            {
            	HashMap mapa2= new HashMap();
        	    HashMap mapa3= new HashMap();
            	if (mapa.get("concepto_"+k)==null || mapa.get("valorajuste_"+k)==null)
            	{
            		String cadenaman="Select concepto_ajuste as concepto, valor_ajuste as valorajuste, valor_ajuste_pool as valorajustepool, valor_ajuste_institucion as valorajusteint from ajus_det_fact_empresa where codigo="+mapa.get("codigoajuste")+" and det_fact_solicitud="+mapa.get("codigodetfactura_"+k)+"";
            		String anteriore= "SELECT afc.valor_ajuste as valorajuste, afc.valor_ajuste_pool as valorajustepool, afc.valor_ajuste_institucion as valorajusteint  from ajus_asocios_fact_empresa afc where afc.det_aso_fac_solicitud="+mapa.get("codigodetfactura_"+k)+"";
            	    
            		PreparedStatement pst1=null;
            		pst1 =  con.prepareStatement(cadenaman,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
            	    ResultSetDecorator rs1=new ResultSetDecorator(pst1.executeQuery());
            	    mapa2= UtilidadBD.cargarValueObject(rs1);
            
            	    PreparedStatement pst2=null;
            	    pst2 =  con.prepareStatement(anteriore,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
        	    	ResultSetDecorator rs2=new ResultSetDecorator(pst2.executeQuery());
        	    	mapa3= UtilidadBD.cargarValueObject(rs2);
        	    	
            	    pst1.close();
            	    rs1.close();
            	    pst2.close();
              	    rs2.close();
            	    
            	    mapa.put("concepto_"+k, mapa2.get("concepto_0"));
            	    
            
        	  
            	    if (mapa.get("valorajuste_"+k)==null)
            	    { 	    	
            	    	
            	    	
                	    if (mapa3.containsKey("valorajuste_"+k))
                	    {	
                	    	 mapa.put("valorajuste_"+k, Float.parseFloat(mapa3.get("valorajuste_"+k+"").toString()));
                	    	 mapa.put("valajustepool_"+k, Float.parseFloat(mapa3.get("valorajustepool_"+k+"").toString()));
                	    	 mapa.put("valajusteinstitucion_"+k, Float.parseFloat(mapa3.get("valorajusteint_"+k+"").toString()));
                           	    	
                	    } 
                	    else{
                   	    	mapa.put("valorajuste_"+k, Float.parseFloat(mapa2.get("valorajuste_0").toString()));
                  	    	 mapa.put("valajustepool_"+k, Float.parseFloat(mapa2.get("valorajustepool_0").toString()));
                  	    	 mapa.put("valajusteinstitucion_"+k, Float.parseFloat(mapa2.get("valorajusteint_0").toString()));
                	    }
            	    }
            	    
            	}
            	pst.setString(1,UtilidadBD.obtenerSiguienteValorSecuencia(con, "cartera.seq_ajusasodetfacemp_pk")+"");
            	pst.setString(2,mapa.get("codigopkserart")+"");
            	pst.setString(3,mapa.get("codigoajuste")+"");
                pst.setString(4,mapa.get("factura")+"");
                pst.setString(5,mapa.get("consecitivoasodetfac_"+k)+"");
                pst.setString(6,mapa.get("codigodetfactura_"+k)+"");
                pst.setString(7,mapa.get("codigoservicio_"+k)+"");
                pst.setObject(8,(mapa.get("codigomedico_"+k)+"").equals("-1")||UtilidadTexto.isEmpty(mapa.get("codigomedico_"+k)+"")?null:mapa.get("codigomedico_"+k));
                pst.setObject(9,(mapa.get("codigopool_"+k)+"").equals("-1")||UtilidadTexto.isEmpty(mapa.get("codigopool_"+k)+"")?null:mapa.get("codigopool_"+k));
                pst.setObject(10,(mapa.get("valorajuste_"+k))==null||UtilidadTexto.isEmpty(mapa.get("valorajuste_"+k)+"")?0:mapa.get("valorajuste_"+k));
                pst.setObject(11,(mapa.get("valajustepool_"+k))==null||UtilidadTexto.isEmpty(mapa.get("valajustepool_"+k)+"")?0:mapa.get("valajustepool_"+k));
                pst.setObject(12,(mapa.get("valajusteinstitucion_"+k))==null||UtilidadTexto.isEmpty(mapa.get("valajusteinstitucion_"+k)+"")?0:mapa.get("valajusteinstitucion_"+k));
                pst.setString(13,mapa.get("concepto_"+k)+"");
                pst.setString(14,mapa.get("institucion")==null?mapa.get("institucion_"+k)+"":mapa.get("institucion")+"");
                logger.info("\n\nconsultaaa::: "+pst);
                pst.executeUpdate();
            }
            result= true;
        } catch(Exception e){
			Log4JManager.error("ERROR insertarAsociosServicio", e);
		}
		finally{
			try{
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException sql) {
				Log4JManager.error("ERROR cerrando objetos persistentes", sql);
			}
		}
        return result;
    }

    /**
     * 
     * @param con
     * @param codAjuste
     * @param factura
     * @param detFacSol
     * @param servicioGeneral
     * @return
     */
    public static int eliminarAjusteServicioAsocios(Connection con,int codigopk, double codAjuste, int factura, int detFacSol)
    {
        PreparedStatement pst=null;
        int result=ConstantesBD.codigoNuncaValido;
        try 
        {
            String cadena="DELETE FROM ajus_asocios_fact_empresa where codigo_pk_ser_art=?";
            pst = con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
            pst.setInt(1,codigopk);
            result= pst.executeUpdate();
        } 
        catch(Exception e){
			Log4JManager.error("ERROR eliminarAjusteServicioAsocios", e);
		}
		finally{
			try{
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException sql) {
				Log4JManager.error("ERROR cerrando objetos persistentes", sql);
			}
		}
        return result;
    }
    

    /**
     * 
     * @param con
     * @param codAjuste
     * @param factura
     * @param detFacSol
     * @param servicioGeneral
     * @return
     */
    public static int eliminarAjusteDetallePaquetes(Connection con,int codigopk, double codAjuste, int factura, int detFacSol)
    {
        PreparedStatement pst=null;
        int result=ConstantesBD.codigoNuncaValido;
        try 
        {
            String cadena="DELETE FROM ajus_paquetizacion_det_factura where codigo_pk_ser_art=?";// codigo_ajuste=? and factura =? and det_fac_solicitud=?";
            pst = con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
            pst.setInt(1,codigopk);
            result=pst.executeUpdate();
        } 
        catch(Exception e){
			Log4JManager.error("ERROR eliminarAjusteDetallePaquetes", e);
		}
		finally{
			try{
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException sql) {
				Log4JManager.error("ERROR cerrando objetos persistentes", sql);
			}
		}
        return result;
    }

    /**
     * 
     * @param con
     * @param codAjuste
     * @param factura
     * @param detFacSol
     * @return
     */
    public static int updateValAjusteInstPoolSerCx(Connection con,int codigopk, double codAjuste, int factura, int detFacSol)
    {
        PreparedStatement pst=null;
        int result=ConstantesBD.codigoNuncaValido;
        try
        {
            String cadena="UPDATE ajus_det_fact_empresa set valor_ajuste_pool = (select sum(aafe.valor_ajuste_pool)  from ajus_asocios_fact_empresa aafe where aafe.codigo_pk_ser_art=?),valor_ajuste_institucion = (select sum(aafe.valor_ajuste_institucion) from ajus_asocios_fact_empresa aafe where aafe.codigo_pk_ser_art=?) where codigo_pk=?";
            pst = con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
            pst.setInt(1,codigopk);
            pst.setInt(2,codigopk);
            pst.setInt(3,codigopk);
            result=pst.executeUpdate();
        } 
        catch(Exception e){
			Log4JManager.error("ERROR updateValAjusteInstPoolSerCx", e);
		}
		finally{
			try{
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException sql) {
				Log4JManager.error("ERROR cerrando objetos persistentes", sql);
			}
		}
        return result;
    }

    
    /**
     * 
     * @param con
     * @param codAjuste
     * @param factura
     * @param detFacSol
     * @return
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public static HashMap consultarServiciosAsociosAjustes(Connection con,int codigopk, double codAjuste, int factura, int detFacSol)
    {
        HashMap mapa=new HashMap();
        mapa.put("numRegistros", "0");
        PreparedStatementDecorator pst=null;
        ResultSetDecorator rs=null;
        try
        {
            pst= new PreparedStatementDecorator(con.prepareStatement(cadenaConsultarServiciosAsociosAjustes,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            pst.setInt(1, codigopk);
            rs=new ResultSetDecorator(pst.executeQuery());
            mapa= UtilidadBD.cargarValueObject(rs);
        }
        catch(Exception e){
			Log4JManager.error("ERROR consultarServiciosAsociosAjustes", e);
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
			catch (SQLException sql) {
				Log4JManager.error("ERROR cerrando objetos persistentes", sql);
			}
		}
        return mapa;
    }

    /**
     * 
     * @param con
     * @param detallePaquete
     * @return
     */
	@SuppressWarnings("rawtypes")
	public static boolean insertarDetallePaquetes(Connection con, HashMap mapa) 
	{
		 String cadena="INSERT INTO ajus_paquetizacion_det_factura(codigo_pk,codigo_pk_ser_art,codigo_ajuste,factura,det_fac_solicitud,paq_det_factura,valor_ajuste,valor_ajuste_pool,valor_ajuste_institucion,concepto_ajuste,institucion) values(?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement pst=null;
        boolean result=false;
        try 
        {   
            pst =  con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
            for(int k = 0 ; k < Integer.parseInt(mapa.get("numRegistros")+"") ; k ++)
            {
            	pst.setInt(1,UtilidadBD.obtenerSiguienteValorSecuencia(con, "cartera.seq_ajuspaqdetfacemp_pk"));
            	pst.setString(2,mapa.get("codigopkserart")+"");
                pst.setString(3,mapa.get("codigoajuste")+"");
                pst.setString(4,mapa.get("factura")+"");
                pst.setString(5,mapa.get("codigodetfactura_"+k)+"");
                pst.setString(6,mapa.get("codpaqdetfac_"+k)+"");
                pst.setString(7,mapa.get("valorajuste_"+k)+"");
                pst.setString(8,mapa.get("valajustepool_"+k)+"");
                pst.setString(9,mapa.get("valajusteinstitucion_"+k)+"");
                pst.setString(10,mapa.get("concepto_"+k)+"");
                pst.setString(11,mapa.get("institucion")==null?mapa.get("institucion_"+k)+"":mapa.get("institucion")+"");
                pst.executeUpdate();
            }
            result=true;
        }catch(Exception e){
			Log4JManager.error("ERROR insertarDetallePaquetes", e);
		}
		finally{
			try{
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException sql) {
				Log4JManager.error("ERROR cerrando objetos persistentes", sql);
			}
		}
        return result;
	}

	/**
	 * 
	 * @param con
	 * @param codigoAjuste
	 * @return
	 */
	public static int obtenerCodigoFacturaAjuste(Connection con, double codigoAjuste) 
	{
		String cadena="SELECT factura from ajus_fact_empresa where codigo = "+codigoAjuste;
        PreparedStatement pst=null;
        ResultSet rs=null;
        int result=ConstantesBD.codigoNuncaValido;
        try 
        {   
            pst =  con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
            rs=pst.executeQuery();
            if(rs.next())
            	result=rs.getInt(1);
        } catch(Exception e){
			Log4JManager.error("ERROR obtenerCodigoFacturaAjuste", e);
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
			catch (SQLException sql) {
				Log4JManager.error("ERROR cerrando objetos persistentes", sql);
			}
		}
		return result;
	}
	
}
