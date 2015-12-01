/*
 * Created on 6/10/2005
 * 
 * @author <a href="mailto:artotor@hotmail.com">Jorge Armando Osorio Velásquez</a>
 * 
 * Copyright Princeton S.A. &copy;&reg; 2005. Todos los Derechos Reservados.
 *
 * Lenguaje		:Java
 *
 */
package com.princetonsa.dao.sqlbase;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.InfoDatosInt;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.ValoresPorDefecto;

import com.ibm.icu.math.BigDecimal;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.tesoreria.DtoDevolRecibosCaja;

import java.sql.SQLException;

/**
 * @version 1.0, 6/10/2005
 * @author <a href="mailto:armando@PrincetonSA.com">Jorge Armando Osorio Velásquez/a>
 */
public class SqlBaseConsultaRecibosCajaDao
{
    /**
	* Objeto para manejar los logs de esta clase
	*/
	private static Logger logger = Logger.getLogger(SqlBaseConsultaRecibosCajaDao.class);

	/**
	 * Cadena para consultar el total de recibos de caja
	 */
	private static String cadenaConsultaTotalRecibosCaja="SELECT sum(drc.valor) as total from recibos_caja rc inner join  detalle_conceptos_rc drc on (drc.numero_recibo_caja=rc.numero_recibo_caja and drc.institucion=rc.institucion) left join facturas_varias fv ON (to_number(drc.doc_soporte, '99999999999999999999999999999999') = fv.codigo_fac_var)";
	
	/**
	 * Cadena para insertar el cabezote del ajuste.
	 */
	private static String cadenaConsultaRecibosCaja="SELECT " +
															"rc.numero_recibo_caja as numerorecibo," +
															"rc.consecutivo_recibo as consecutivorc," +
                                                            "rc.estado as codigoestado, " +
															"rc.fecha as fecha," +
                                                            "rc.hora as hora, " +
                                                            "erc.descripcion as descripcionestado, " +
															"rc.usuario as usuario, " +
                                                            "administracion.getnombremedico(u.codigo_persona) as nombreusuario," +
                                                            "rc.recibido_de as recibidode, " +
															"rc.caja as consecutivocaja, " +
                                                            "c.codigo as codigocaja," +
                                                            "c.descripcion as descripcioncaja, " +
															"rc.observaciones as observaciones," +
															"sum(drc.valor) AS valortotal," +
                                                            "getnomcentroatencion(c.centro_atencion) AS nombrecentroatencion, " +
                                                            " drc.numero_id_beneficiario AS idBeneficiario, drc.tipo_id_beneficiario AS tipoIdBeneficiario "+
													"from recibos_caja rc " +
													"inner join detalle_conceptos_rc drc ON (drc.numero_recibo_caja= rc.numero_recibo_caja and drc.institucion=rc.institucion) " +
													"left join facturas_varias fv ON (to_number(drc.doc_soporte, '99999999999999999999999999999999') = fv.codigo_fac_var)"+
													"inner join estados_recibos_caja erc on(rc.estado=erc.codigo) " +
													"inner join cajas c on (rc.caja=c.consecutivo) " +
													"inner join usuarios u on(u.login=rc.usuario) " +
													"where rc.institucion=? ";
	
	/**
     * Consulta para cargar los conceptos de un recibo de caja
     */
    private static String consultarConceptosRecibosCaja="SELECT " +
    															"crc.consecutivo as consecutivoconceptorc, " +
    															"crc.concepto as codigoconceptorc, " +
    															"cit.descripcion as descripcionconceptorc, " +
    															"cit.codigo_tipo_ingreso as tipoconceptorc," +
    															"cit.valor as valortipoconceptorc, " +
    															"crc.doc_soporte as docsoporteconceptorc," +
    															"crc.valor as valorconceptorc, " +
    															"crc.tipo_id_beneficiario as tipoidbenefeciario, " +
    															"crc.numero_id_beneficiario as numeroidbeneficiario, " +
    															"crc.nombre_beneficiario as nombrebeneficiario " +
    													"from detalle_conceptos_rc crc " +
    													"inner join recibos_caja rc on (crc.numero_recibo_caja=rc.numero_recibo_caja) " +
    													"inner join conceptos_ing_tesoreria cit on (crc.concepto=cit.codigo and crc.institucion=crc.institucion) " +
    													"where rc.consecutivo_recibo=? and crc.institucion = ?";
    
    /**
     * Consulta para las formas de pago
     */
    private static String consultaFormasPago = "SELECT dpr.consecutivo as consecutivo, " +
    												"dpr.forma_pago as formapago, " +
    												"fp.codigo as codigoformapago, " +
    												"fp.descripcion as descripcionformapago, " +
    												"dpr.valor as valor, " +
    												"fp.tipo_detalle as tipodetalle " +
    										"from detalle_pagos_rc dpr " +
    										"inner join recibos_caja rc on (dpr.numero_recibo_caja=rc.numero_recibo_caja) " +
    										"inner join formas_pago fp on(dpr.forma_pago=fp.consecutivo) " +
    										"where rc.consecutivo_recibo=? and dpr.institucion=?";
    
    /**
     * Consulta para los cheques
     */
    private static String consultaChequesFormaPago = "SELECT " +
    													"mv.codigo as codigo, " +
    													"mv.numero_cheque as numerocheque, " +
    													"t.codigo as codigobanco, " +
    													"t.descripcion as nombrebanco, " +
    													"mv.numero_cuenta as numerocuenta, " +
    													"mv.ciudad_plaza as codciudadplaza, " +
    													"getnombreciudad(mv.pais_plaza,mv.departamento_plaza,mv.ciudad_plaza) as nombreciudadplaza, " +
    													"mv.departamento_plaza as coddeptoplaza, " +
    													"getnombredepto(mv.pais_plaza,mv.departamento_plaza) as nombredeptoplaza, " +
    													"mv.fecha_giro as fechagiro, " +
    													"mv.valor as valor, " +
    													"mv.girador as girador, " +
    													"mv.direccion as direccion, " +
    													"mv.ciudad_girador as codciudadgirador, " +
    													"getnombreciudad(mv.pais_girador,mv.departamento_girador,mv.ciudad_girador) as nombreciudadgirador, " +
    													"mv.departamento_girador as coddeptogirador, " +
    													"getnombredepto(mv.pais_girador,mv.departamento_girador) as nombredeptogirador, " +
    													"mv.telefono as telefono,mv.observaciones as observaciones, " +
    													"mv.pais_plaza as codpaisplaza,getdescripcionpais(mv.pais_plaza) as nompaisplaza, " +
    													"mv.pais_girador as codpaisgirador,getdescripcionpais(mv.pais_girador) as nombrepaisgirador " +
    												"FROM " +
    													"movimientos_cheques mv " +
    													"inner join entidades_financieras ef on (ef.consecutivo=mv.entidad_financiera) "+
    													"inner join terceros t on (t.codigo=ef.tercero)" +
    												"WHERE " +
    													"mv.det_pago_rc=?";
    
    /**
     * Consulta para las tarjeras
     */
    private static String consultaTarjetasFormaPago="SELECT " +
    														"mv.codigo as codigo, " +
    														"mv.codigo_tarjeta, " +
    														"tf.codigo as codigotarjeta, " +
    														"t.descripcion as entidad, " +
    														"mv.numero_tarjeta as numerotarjeta, " +
    														"mv.numero_comprobante as numerocomprobante, " +
    														"mv.numero_autorizacion as numeroautorizacion, " +
    														"mv.fecha as fecha, " +
    														"mv.valor as valor, " +
    														"mv.girador as girador, " +
    														"mv.direccion as direccion, " +
    														"mv.ciudad as codciudad, " +
    														"getnombreciudad(mv.pais,mv.departamento,mv.ciudad) as nomciudad, " +
    														"mv.departamento as coddepto, " +
    														"getnombredepto(mv.pais,mv.departamento) as nomdepto, " +
    														"mv.telefono as telefono, " +
    														"mv.observaciones as observaciones, " +
    														"mv.pais as codpais," +
    														"getdescripcionpais(mv.pais) as nombrepaisgirador " +
    												"FROM " +
    													"movimientos_tarjetas mv " +
    													"inner join tarjetas_financieras tf on(mv.codigo_tarjeta = tf.consecutivo) " +
    													"inner join entidades_financieras ef on (ef.consecutivo = mv.entidad_financiera) " +
    													"inner join terceros t on (t.codigo=ef.tercero) " +
    												"WHERE " +
    													"mv.det_pago_rc=?";

    
    
    /****************************************************************************************************************
     * Consulta anulación recibos de caja
     * @author jarloc
     ****************************************************************************************************************/
    
    private static String queryFiltroConsultaAnulacionStr=" where arc.institucion=? and rc.consecutivo_recibo=?";
    
    /**
     * Consulta los recibos de caja para utilizarlos en las funcionalidades de cierre caja y arqueos
     */
    private static String consultaRecibosCajaStr=	"SELECT " +
    												"rc.numero_recibo_caja as numerorecibo,  " +
    												"rc.estado as codigoestado,   " +
    												"erc.descripcion as descripcionestado,   " +
    												"rc.recibido_de as recibidode,  " +
    												"dprc.forma_pago AS codigoformapago, " +
    												"fp.descripcion AS descripcionformapago, " +
    												"dprc.valor as valorReciboCaja, " +
    												"CASE WHEN rc.estado="+ConstantesBD.codigoEstadoReciboCajaAnulado+" THEN 0 ELSE dprc.valor END AS valorReciboCajaCeroAnulado, "+
    												"CASE WHEN rc.estado="+ConstantesBD.codigoEstadoReciboCajaAnulado+" OR rc.estado="+ConstantesBD.codigoEstadoReciboCajaEnCierre+" OR rc.estado="+ConstantesBD.codigoEstadoReciboCajaRecaudado+" THEN 1 ELSE 0 END AS contadorEstados  "+
    												"from " +
    												"recibos_caja rc   " +
    												"inner join estados_recibos_caja erc on(rc.estado=erc.codigo)   " +
    												"inner join cajas c on (rc.caja=c.consecutivo)   " +
    												"inner join usuarios u on(u.login=rc.usuario) " +
    												"inner join detalle_pagos_rc dprc ON (dprc.numero_recibo_caja=rc.numero_recibo_caja AND dprc.institucion=rc.institucion) " +
    												"inner join formas_pago fp ON (fp.consecutivo=dprc.forma_pago)   ";
    
    /**
     * Consulta los recibos de caja para utilizarlos en las funcionalidades de cierre caja y arqueos
     */
    private static String consultaRecibosCajaParaBloqueosStr=	"SELECT " +
			    												"rc.numero_recibo_caja as numerorecibo  " +
			    												"from " +
			    												"recibos_caja rc   ";
	
    /**
     * Consulta los recibos de caja para utilizarlos en las funcionalidades de cierre caja y arqueos
     */
    private static String consultaDevolucionesRCParaBloqueosStr=	" SELECT d.codigo FROM recibos_caja rc " +
																	"INNER JOIN devol_recibos_caja d ON (d.numero_rc=rc.numero_recibo_caja AND d.institucion=rc.institucion)  " ;
	
    												
    /**
     * 
     */
    private static String consultaDevolucionesRC="SELECT " +
    												"d.codigo as codigo, " +
    												"d.consecutivo as consecutivo, " +
    												"d.estado as acronimoestado, " +
    												"getintegridaddominio(d.estado) as nombreestado, " +
    												"rc.recibido_de as recibidode, " +
    												"d.forma_pago as acronimoformapago, " +
    												"fp.descripcion AS descripcionformapago, " +
    												"d.valor_devolucion as valor " +
    											"FROM " +
    												"devol_recibos_caja d " +
    											"inner join " +
    												"recibos_caja rc " +
    													"ON(rc.numero_recibo_caja=d.numero_rc and rc.institucion=d.institucion) " +
    											"INNER JOIN " +
    												"formas_pago fp " +
    												"ON (fp.consecutivo=d.forma_pago)" +
    												"";
    
    
    
    
    /**
	 * Cadena para la consulta devoluciones Recibos de Caja Aprobados
	 */
	public static String cadenaconsultarDevolucionesAprobadas="SELECT dr.codigo as numdevol, dr.consecutivo as consecutivo, to_char(dr.fecha_devolucion,'dd/mm/yyyy') as fechaelaboracion, dr.hora_devolucion as horadevolucion, dr.estado as estadodevol, dr.motivo_devolucion as motivodevol, md.descripcion as descripcionmotdevol, dr.valor_devolucion as valordevol, dr.numero_rc as numerorc, dr.usuario_devolucion as cajero "+
																	"FROM devol_recibos_caja dr inner join motivos_devolucion_rc md on(md.codigo=dr.motivo_devolucion) "+
																	"WHERE dr.numero_rc=? and dr.institucion=? and dr.estado='"+ConstantesIntegridadDominio.acronimoEstadoAprobado+"'";
    
    
	
    /**
     * metodo para generar la busqueda avanzada
     * de consulta anulaciones recibos de caja
     * @param con Connection
     * @param vo HashMap
     * @param cadenaConsultaAnulacionReciboCaja 
     * @return HashMap 
     * @author jarloc
     */
    public static HashMap ejecutarBusquedaAvanzadaAnulacionesRC(Connection con, HashMap vo, String cadenaConsultaAnulacionReciboCaja)
    {
        HashMap mapa=new HashMap();
        String cadena=" where arc.institucion="+vo.get("institucion")+" ";
        if(!vo.get("numero_anulacion_inicial").equals("") && !vo.get("numero_anulacion_final").equals(""))
               cadena=cadena+" and to_number(arc.consecutivo_anulacion,'99999999999999999999') between "+vo.get("numero_anulacion_inicial")+" and "+vo.get("numero_anulacion_final")+"";
        if(!vo.get("numero_anulacion_inicial").equals("") && vo.get("numero_anulacion_final").equals(""))
               cadena=cadena+" and arc.consecutivo_anulacion='"+vo.get("numero_anulacion_inicial")+"'";;
        if(!vo.get("numero_anulacion_final").equals("") && vo.get("numero_anulacion_inicial").equals(""))
               cadena=cadena+" and arc.consecutivo_anulacion='"+vo.get("numero_anulacion_final")+"'";;        
        if(!vo.get("fecha_anulacion_inicial").equals("") && !vo.get("fecha_anulacion_final").equals(""))
               cadena=cadena+" and to_char(arc.fecha,'yyyy-mm-dd') between '"+UtilidadFecha.conversionFormatoFechaABD(vo.get("fecha_anulacion_inicial")+"")+"' and '"+UtilidadFecha.conversionFormatoFechaABD(vo.get("fecha_anulacion_final")+"")+"'";        
        if(!vo.get("fecha_anulacion_inicial").equals("") && vo.get("fecha_anulacion_final").equals(""))
                cadena=cadena+" and arc.fecha='"+UtilidadFecha.conversionFormatoFechaABD(vo.get("fecha_anulacion_inicial")+"")+"'";           
        if(!vo.get("fecha_anulacion_final").equals("") && vo.get("fecha_anulacion_inicial").equals(""))
                cadena=cadena+" and arc.fecha='"+UtilidadFecha.conversionFormatoFechaABD(vo.get("fecha_elaboracion_final")+"")+"'";
        if(!vo.get("motivo_anulacion").equals(ConstantesBD.codigoNuncaValido+""))
               cadena=cadena+" and arc.motivo_anulacion='"+vo.get("motivo_anulacion")+"'";;
        if(!vo.get("usuario_anulacion").equals(ConstantesBD.codigoNuncaValido+""))
            cadena=cadena+" and arc.usuario='"+vo.get("usuario_anulacion")+"'";
        if(!vo.get("numero_recibo_caja").equals(""))
            cadena=cadena+" and rc.consecutivo_recibo='"+vo.get("numero_recibo_caja")+"'";
        if(!vo.get("codigo_centro_atencion").toString().equals("-1"))
        	cadena=cadena+" AND  c.centro_atencion = "+vo.get("codigo_centro_atencion")+" ";
        cadena+=" order by consecutivoanulacion ";
       
        String sql=cadenaConsultaAnulacionReciboCaja+cadena;
        logger.info("ejecutarBusquedaAvanzadaAnulacionesRC-->"+sql);
        try
        {
            PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(sql,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));            
            mapa=UtilidadBD.cargarValueObjectSinNumRegistros(new ResultSetDecorator(ps.executeQuery()));
            mapa.put("consultaWhere", cadena);//para la impresion
        }
        catch (SQLException e)
        {
            logger.error("Error generando la consulta de anulación recibos de caja [SqlBaseSolicitudTrasladoAlmacenDao] "+e);
            e.printStackTrace();
        }
        return (HashMap)mapa.clone();       
    }
    
    /**
     * @param con
     * @param fechaInicial
     * @param fechaFinal
     * @param institucion
     * @param numeroRecibosCajaInicial
     * @param numeroRecibosCajaFinal
     * @param buscarRecibosCaja
     * @param conceptoReciboCaja
     * @param busquedaConceptos
     * @param estadoReciboCaja
     * @param busquedaEstado
     * @param usuario
     * @param busquedaUsuario
     * @param caja
     * @param busquedaCaja
     * @param buscarFormaPago 
     * @param formaPago 
     * @return mapa con los datos
     */
    public static HashMap busquedaAvanzada(Connection con, String fechaInicial, String fechaFinal, int institucion, String numeroRecibosCajaInicial, 
            							String numeroRecibosCajaFinal, boolean buscarRecibosCaja, String conceptoReciboCaja, boolean busquedaConceptos, 
            							int estadoReciboCaja, boolean busquedaEstado, String usuario, boolean busquedaUsuario, int caja, boolean busquedaCaja, int formaPago, boolean buscarFormaPago, int codigoCentroAtencion,
            							String docSoporte, String tipoIdBeneficiario, String numeroIdBeneficiario)
    {
    	logger.info("docSopoter--->"+docSoporte+" tipoId->"+tipoIdBeneficiario+" numeroId->"+numeroIdBeneficiario);
        HashMap mapa=new HashMap();
        String cadenaConsulta=cadenaConsultaRecibosCaja;
        String cadenaCondiciones="";
        int index=0;
        try
        {
            if(buscarRecibosCaja)
            {
                String cadena=" AND (to_number(rc.consecutivo_recibo, '99999999999999999999999999999999.9999999999') between "+numeroRecibosCajaInicial+" and "+numeroRecibosCajaFinal+")";
                cadenaCondiciones=cadenaCondiciones+cadena;
            }
            if(busquedaEstado)
            {
                String cadena=" and (rc.estado = "+estadoReciboCaja+")";
                cadenaCondiciones=cadenaCondiciones+cadena;
            }
            if(busquedaUsuario)
            {
                String cadena=" and (rc.usuario = '"+usuario+"')";
                cadenaCondiciones=cadenaCondiciones+cadena;
            }
            if(busquedaCaja)
            {
                String cadena=" and (rc.caja = "+caja+")";
                cadenaCondiciones=cadenaCondiciones+cadena;
            }
            if(busquedaConceptos)
            {
                String cadena="and ('"+conceptoReciboCaja +"' in (select dcr.concepto from detalle_conceptos_rc dcr where dcr.numero_recibo_caja=rc.numero_recibo_caja and dcr.institucion=rc.institucion))";
                cadenaCondiciones=cadenaCondiciones+cadena;
            }
            if(buscarFormaPago)
            {
            	String cadena=" and ("+formaPago +" in (select dpr.forma_pago from detalle_pagos_rc dpr where dpr.numero_recibo_caja=rc.numero_recibo_caja and dpr.institucion=rc.institucion))";
            	cadenaCondiciones=cadenaCondiciones+cadena;
            }
            if(!fechaFinal.trim().equals("")&&!fechaInicial.trim().equals(""))
            {
            	String cadena=" and rc.fecha between '"+UtilidadFecha.conversionFormatoFechaABD(fechaInicial)+"' and '"+UtilidadFecha.conversionFormatoFechaABD(fechaFinal)+"' ";
                cadenaCondiciones=cadenaCondiciones+cadena;
            }
            if(codigoCentroAtencion>0)
            {
            	String cadena=" and c.centro_atencion ="+codigoCentroAtencion+" ";
            	cadenaCondiciones=cadenaCondiciones+cadena;
            }
            if(!docSoporte.trim().equals(""))
            {
            	String cadena=" and fv.consecutivo ="+docSoporte+" ";
            	cadenaCondiciones=cadenaCondiciones+cadena;
            }
            if(!tipoIdBeneficiario.trim().equals(""))
            {
            	String cadena=" and drc.tipo_id_beneficiario ='"+tipoIdBeneficiario+"' ";
            	cadenaCondiciones=cadenaCondiciones+cadena;
            }
            if(!numeroIdBeneficiario.trim().equals(""))
            {
            	String cadena=" and drc.numero_id_beneficiario ='"+numeroIdBeneficiario+"' ";
            	cadenaCondiciones=cadenaCondiciones+cadena;
            }
            
            cadenaConsulta=cadenaConsulta+cadenaCondiciones+"  GROUP BY rc.numero_recibo_caja, rc.consecutivo_recibo, rc.estado, rc.fecha, rc.hora, erc.descripcion, rc.usuario, u.codigo_persona, rc.recibido_de, rc.caja, c.codigo, c.descripcion, rc.observaciones, c.centro_atencion,drc.numero_id_beneficiario , drc.tipo_id_beneficiario ORDER BY rc.numero_recibo_caja ";
            
            PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaConsulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            ps.setInt(1,institucion);
            
            logger.info("\n\n BUSQUEDA AVANZADA-->"+cadenaConsulta+" inst->"+institucion);
            
            ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
            while(rs.next())
            {
                mapa.put("numerorecibo_"+index,rs.getString("numerorecibo"));
                mapa.put("consecutivorc_"+index,rs.getString("consecutivorc"));
                mapa.put("fecha_"+index,UtilidadFecha.conversionFormatoFechaAAp(rs.getDate("fecha")));
                try{
                    mapa.put("hora_"+index,UtilidadFecha.conversionFormatoHoraABD(rs.getTime("hora")));
                }
                catch (Exception e) {
                    mapa.put("hora_"+index,rs.getString("hora"));
				}
                mapa.put("codigoestado_"+index,rs.getString("codigoestado"));
                mapa.put("descripcionestado_"+index,rs.getString("descripcionestado"));
                mapa.put("usuario_"+index,rs.getString("usuario"));
                mapa.put("nombreusuario_"+index,rs.getString("nombreusuario"));
                mapa.put("observaciones_"+index,rs.getString("observaciones"));
                mapa.put("consecutivocaja_"+index,rs.getString("consecutivocaja"));
                mapa.put("codigocaja_"+index,rs.getString("codigocaja"));
                mapa.put("descripcioncaja_"+index,rs.getString("descripcioncaja"));
                mapa.put("recibidode_"+index,rs.getString("recibidode"));
                mapa.put("valortotal_"+index,rs.getString("valortotal"));
                mapa.put("nombrecentroatencion_"+index,rs.getString("nombrecentroatencion"));
                
                mapa.put("tipoIdBeneficiario_"+index,rs.getString("tipoIdBeneficiario"));
                mapa.put("idBeneficiario_"+index,rs.getString("idBeneficiario"));
              

                index++;
            }
            String cadenaTotalTemp=cadenaConsultaTotalRecibosCaja+" inner join estados_recibos_caja erc on(rc.estado=erc.codigo) inner join cajas c on (rc.caja=c.consecutivo) inner join usuarios u on(u.login=rc.usuario)  where rc.institucion=?"; 
            String cadenaTemp=cadenaTotalTemp+cadenaCondiciones;
            ps= new PreparedStatementDecorator(con.prepareStatement(cadenaTemp,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            ps.setInt(1,institucion);
            
            logger.info("\n\n BUSQUEDA AVANZADA 2->"+cadenaTemp+" inst->"+institucion);
            
            rs=new ResultSetDecorator(ps.executeQuery());
            if(rs.next())
            	mapa.put("totalRC",rs.getObject("total")==null?"0":rs.getString("total"));
            else
            	mapa.put("totalRC","0");
            
            cadenaTemp=cadenaTotalTemp+" and estado="+ConstantesBD.codigoEstadoReciboCajaAnulado+cadenaCondiciones;
            ps= new PreparedStatementDecorator(con.prepareStatement(cadenaTemp,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            ps.setInt(1,institucion);
            
            logger.info("\n\n BUSQUEDA AVANZADA 3->"+cadenaTemp+" inst->"+institucion);
            rs=new ResultSetDecorator(ps.executeQuery());
            if(rs.next())
            	mapa.put("totalRCAnulados",rs.getObject("total")==null?"0":rs.getString("total"));
            else
            	mapa.put("totalRCAnulados","0");
            
            cadenaTemp=cadenaTotalTemp+" and estado<>"+ConstantesBD.codigoEstadoReciboCajaAnulado+cadenaCondiciones;
            ps= new PreparedStatementDecorator(con.prepareStatement(cadenaTemp,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            ps.setInt(1,institucion);
            
            logger.info("\n\n BUSQUEDA AVANZADA 4->"+cadenaTemp+" inst->"+institucion);
            rs=new ResultSetDecorator(ps.executeQuery());
            if(rs.next())
            	mapa.put("totalRCMenosAnulados",rs.getObject("total")==null?"0":rs.getString("total"));
            else
            	mapa.put("totalRCMenosAnulados","0");
            
        }
        catch (SQLException e)
        {
            logger.error("Errore ejecutando la consulta avanzada: "+e);
        }
        mapa.put("numeroregistros",index+"");
        return mapa;
    }
    
    /**
     * @param con
     * @param institucion
     * @param numeroReciboCaja
     * @return
     */
    public static HashMap consultarConceptosReciboCaja(Connection con, int institucion, String numeroReciboCaja)
    {
        ResultSetDecorator rs1=null;
        int i=0;
        HashMap mapa=new HashMap();
        try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consultarConceptosRecibosCaja,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1,numeroReciboCaja);
			ps.setInt(2,institucion);
			rs1=new ResultSetDecorator(ps.executeQuery());
			while(rs1.next())
            {
                mapa.put("consecutivoconceptorc_"+i,rs1.getString("consecutivoconceptorc"));
                mapa.put("codigoconceptorc_"+i,rs1.getString("codigoconceptorc"));
                mapa.put("descripcionconceptorc_"+i,rs1.getString("descripcionconceptorc"));
                mapa.put("tipoconceptorc_"+i,rs1.getString("tipoconceptorc"));
                mapa.put("valortipoconceptorc_"+i,rs1.getString("valortipoconceptorc"));
                mapa.put("docsoporteconceptorc_"+i,rs1.getString("docsoporteconceptorc"));
                mapa.put("valorconceptorc_"+i,rs1.getString("valorconceptorc"));
                mapa.put("tipoidbenefeciario_"+i,rs1.getString("tipoidbenefeciario"));
                mapa.put("numeroidbeneficiario_"+i,rs1.getString("numeroidbeneficiario"));
                mapa.put("nombrebeneficiario_"+i,rs1.getString("nombrebeneficiario"));
                i++;
            }
			
		}
		catch(SQLException e)
		{
		    logger.error("Error cargando los conceptos del ReciboCaja [SqlBaseAnulacionReciboCajaDao]"+e);
		}
		mapa.put("numeroregistros",i+"");
		return mapa;
    }
    
    /**
     * @param con
     * @param institucion
     * @param numeroReciboCaja
     * @return
     */
    public static HashMap consultarFormasPagoReciboCaja(Connection con, int institucion, String numeroReciboCaja) {
        ResultSetDecorator rs=null,rs1=null;
        int i=0;
        HashMap mapa=new HashMap();
        
        try {
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consultaFormasPago,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1, numeroReciboCaja);
			ps.setInt(2, institucion);
			rs =new ResultSetDecorator(ps.executeQuery());
			
			while(rs.next()) {
                mapa.put("consecutivofp_"+i,rs.getString("consecutivo"));
                mapa.put("codigoformapago_"+i,rs.getString("formapago"));
                mapa.put("codigoformapago_"+i,rs.getString("codigoformapago"));
                mapa.put("descripcionformapago_"+i,rs.getString("descripcionformapago"));
                mapa.put("valorfp_"+i,rs.getString("valor"));
                mapa.put("tipodetallefp_"+i,rs.getString("tipodetalle"));
                mapa.put("entidad_"+i,"");
                
                logger.info("999999999999999999999999");
                logger.info("Tipo Detalle: " + rs.getInt("tipodetalle"));
                
                if(rs.getInt("tipodetalle")==ConstantesBD.codigoTipoDetalleFormasPagoCheque) {
                    logger.info("Tipo Detalle: Formas Pago Cheque");

                	PreparedStatementDecorator ps1= new PreparedStatementDecorator(con.prepareStatement(consultaChequesFormaPago,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
                    ps1.setInt(1,rs.getInt("consecutivo"));
                    rs1=  new ResultSetDecorator(ps1.executeQuery());
                    
                    if(rs1.next()) {
                        mapa.put("codigocheque_"+i,rs1.getString("codigo"));
                        mapa.put("numerocheque_"+i,rs1.getString("numerocheque"));
                        mapa.put("codigobanco_"+i,rs1.getString("codigobanco"));
                        mapa.put("entidad_"+i,rs1.getString("nombrebanco"));
                        mapa.put("numerocuenta_"+i,rs1.getString("numerocuenta"));
                        mapa.put("codciudadplaza_"+i,rs1.getString("codciudadplaza"));
                        mapa.put("nombreciudadplaza_"+i,rs1.getString("nombreciudadplaza"));
                        mapa.put("coddeptoplaza_"+i,rs1.getString("coddeptoplaza"));
                        mapa.put("nombredeptoplaza_"+i,rs1.getString("nombredeptoplaza"));
                        mapa.put("fechagiro_"+i,UtilidadFecha.conversionFormatoFechaAAp(rs1.getString("fechagiro")));
                        mapa.put("valor_"+i,rs1.getString("valor"));
                        mapa.put("girador_"+i,rs1.getString("girador"));
                        mapa.put("direccion_"+i,rs1.getString("direccion"));
                        mapa.put("codciudadgirador_"+i,rs1.getString("codciudadgirador"));
                        mapa.put("nombreciudadgirador_"+i,rs1.getString("nombreciudadgirador"));
                        mapa.put("coddeptogirador_"+i,rs1.getString("coddeptogirador"));
                        mapa.put("nombredeptogirador_"+i,rs1.getString("nombredeptogirador"));
                        mapa.put("telefono_"+i,rs1.getString("telefono"));
                        mapa.put("observaciones_"+i,rs1.getString("observaciones"));
                        mapa.put("codpaisplaza_"+i,rs1.getString("codpaisplaza"));
                        
                        // 70757
                        //mapa.put("nombrepaisplaza_"+i,rs1.getString("nombrepaisplaza"));
                        mapa.put("nombrepaisplaza_"+i,rs1.getString("nompaisplaza"));
                        
                        mapa.put("codpaisgirador_"+i,rs1.getString("codpaisgirador"));
                        mapa.put("nombrepaisgirador_"+i,rs1.getString("nombrepaisgirador"));
                    }
                }

                else if(rs.getInt("tipodetalle")==ConstantesBD.codigoTipoDetalleFormasPagoTarjeta) {
                    logger.info("Tipo Detalle: Formas Pago Tarjeta");
                	
                    PreparedStatementDecorator ps1= new PreparedStatementDecorator(con.prepareStatement(consultaTarjetasFormaPago,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
                    ps1.setInt(1, rs.getInt("consecutivo"));
                    rs1 = new ResultSetDecorator(ps1.executeQuery());
                    
                    if(rs1.next()) {
                        mapa.put("codigomovtarjeta_"+i,rs1.getString("codigo"));
                        mapa.put("codigotarjeta_"+i,rs1.getString("codigotarjeta"));
                        mapa.put("entidad_"+i,rs1.getString("entidad"));
                        mapa.put("numerotarjeta_"+i,rs1.getString("numerotarjeta"));
                        mapa.put("numerocomprobante_"+i,rs1.getString("numerocomprobante"));
                        mapa.put("numeroautorizacion_"+i,rs1.getString("numeroautorizacion"));
                        mapa.put("fecha_"+i,UtilidadFecha.conversionFormatoFechaAAp(rs1.getString("fecha")));
                        mapa.put("valor_"+i,rs1.getString("valor"));
                        mapa.put("girador_"+i,rs1.getString("girador"));
                        mapa.put("direccion_"+i,rs1.getString("direccion"));
                        mapa.put("codciudad_"+i,rs1.getString("codciudad"));
                        mapa.put("nomciudad_"+i,rs1.getString("nomciudad"));
                        mapa.put("coddepto_"+i,rs1.getString("coddepto"));
                        mapa.put("nomdepto_"+i,rs1.getString("nomdepto"));
                        mapa.put("telefono_"+i,rs1.getString("telefono"));
                        mapa.put("observaciones_"+i,rs1.getString("observaciones"));
                        mapa.put("codpais_"+i,rs1.getString("codpais"));
                        mapa.put("nompais_"+i,rs1.getString("nombrepaisgirador"));
                    }
                }
                i++;
            }
		}

        catch(SQLException e) {
		    //logger.error("Error cargando las Formas de pago del ReciboCaja [SqlBaseAnulacionReciboCajaDao]"+e);
		    logger.error("Error cargando las Formas de pago del ReciboCaja. " + e);
		    e.printStackTrace();
		}
		mapa.put("numeroregistros",i+"");
		return mapa;
    }

    
    /**
     * @param con
     * @param institucion
     * @param numeroReciboCaja
     * @param cadenaConsultaAnulacionreciboCaja 
     * @return
     */
    public static HashMap consultarAnulacionReciboCaja(Connection con, int institucion, String numeroReciboCaja, String cadenaConsultaAnulacionReciboCaja)
    {
    	ResultSetDecorator rs=null;
    	HashMap mapa=new HashMap();
    	try
    	{
    		PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaConsultaAnulacionReciboCaja + queryFiltroConsultaAnulacionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
    		ps.setInt(1,institucion);
    		ps.setString(2,numeroReciboCaja);
    		rs=new ResultSetDecorator(ps.executeQuery());
    		while(rs.next())
    		{
    			mapa.put("numeroanulacion",rs.getString("numeroanulacion"));
    			mapa.put("consecutivoanulacion",rs.getString("consecutivoanulacion"));
    			mapa.put("consecutivorc",rs.getString("consecutivorc"));    			
    			mapa.put("loginusuario",rs.getString("loginusuario"));
    			mapa.put("nomusuario",rs.getString("nomusuario"));
    			mapa.put("fecha",UtilidadFecha.conversionFormatoFechaAAp(rs.getDate("fecha")));
    			mapa.put("hora",rs.getString("hora"));
    			mapa.put("codmotivo",rs.getString("codmotivo"));
    			mapa.put("descmotivoanulacion",rs.getString("descmotivoanulacion"));
    			mapa.put("observaciones",rs.getString("observaciones"));
    		}
    	}
		catch(SQLException e)
		{
		    logger.error("Error cargano los datos de la anulacion der Recibo de Caja [SqlBaseAnulacionReciboCajaDao]"+e);
		}
		return mapa;
		
    }

    
    /**
     * @param con
     * @param institucion
     * @param numeroReciboCaja
     * @return
     */
    public static HashMap consultarTotalesPagos(Connection con, int institucion, String numeroReciboCaja)
    {
    	HashMap mapa= new HashMap();
    	
    	
    	String totalPagosEfectivos="SELECT fp.descripcion as descripcion, dprc.valor as valor from detalle_pagos_rc dprc inner join formas_pago fp on(dprc.forma_pago=fp.consecutivo) ";
        String totalPagosCheques="SELECT  fp.descripcion ||' '|| mc.numero_cheque as descripcion,mc.valor as valor from movimientos_cheques mc inner join detalle_pagos_rc dprc on(mc.det_pago_rc=dprc.consecutivo) inner join formas_pago fp on(dprc.forma_pago=fp.consecutivo)";
        String totalTarjetasCredito="SELECT fp.descripcion ||' '|| mt.numero_tarjeta as descripcion,mt.valor as valor from movimientos_tarjetas mt inner join tarjetas_financieras tf on (mt.codigo_tarjeta=tf.consecutivo) inner join detalle_pagos_rc dprc on(mt.det_pago_rc=dprc.consecutivo) inner join formas_pago fp on(dprc.forma_pago=fp.consecutivo) ";
        String totalTarjetasDebito="SELECT fp.descripcion ||' '||mt.numero_tarjeta as descripcion,mt.valor as valor from movimientos_tarjetas mt inner join tarjetas_financieras tf on (mt.codigo_tarjeta=tf.consecutivo) inner join detalle_pagos_rc dprc on(mt.det_pago_rc=dprc.consecutivo) inner join formas_pago fp on(dprc.forma_pago=fp.consecutivo) ";
        
        String totalBonos = "SELECT fp.descripcion ||' '|| mb.num_serial as descripcion, dprc.valor as valor from movimientos_bonos mb inner join detalle_pagos_rc dprc on(mb.det_pago_rc=dprc.consecutivo) inner join formas_pago fp on(dprc.forma_pago=fp.consecutivo) ";
        
        String consulta= "";
        String union=" UNION ";
//        
//        String numeroReciboCaja=parametros.get("nrorc")+"";
//		String institucion=parametros.get("institucion")+"";
//        
        
//    	
//        String totalPagosEfectivos="SELECT fp.descripcion as descripcion, dprc.valor as valor from detalle_pagos_rc dprc inner join formas_pago fp on(dprc.forma_pago=fp.consecutivo) ";
//        String totalPagosCheques="SELECT  fp.descripcion ||' '|| mc.numero_cheque as descripcion,mc.valor as valor from movimientos_cheques mc inner join detalle_pagos_rc dprc on(mc.det_pago_rc=dprc.consecutivo) inner join formas_pago fp on(dprc.forma_pago=fp.consecutivo)";
//        String totalTarjetasCredito="SELECT fp.descripcion ||' '|| mt.numero_tarjeta as descripcion,mt.valor as valor from movimientos_tarjetas mt inner join tarjetas_financieras tf on (mt.codigo_tarjeta=tf.consecutivo) inner join detalle_pagos_rc dprc on(mt.det_pago_rc=dprc.consecutivo) inner join formas_pago fp on(dprc.forma_pago=fp.consecutivo) ";
//        String totalTarjetasDebito="SELECT fp.descripcion ||' '||mt.numero_tarjeta as descripcion,mt.valor as valor from movimientos_tarjetas mt inner join tarjetas_financieras tf on (mt.codigo_tarjeta=tf.consecutivo) inner join detalle_pagos_rc dprc on(mt.det_pago_rc=dprc.consecutivo) inner join formas_pago fp on(dprc.forma_pago=fp.consecutivo) ";
//        String consulta= "";
//        String union=" UNION ";
//        
        try
        {
//            // condicion efectivo
//        	totalPagosEfectivos+="where fp.tipo_detalle="+ConstantesBD.codigoTipoDetalleFormasPagoNinguno+" and dprc.numero_recibo_caja='"+numeroReciboCaja+"' and dprc.institucion="+institucion;
//        	//condicion cheque
//        	totalPagosCheques+="where dprc.numero_recibo_caja='"+numeroReciboCaja+"' and dprc.institucion="+institucion;
//            //condicion tarjetas Credito
//        	totalTarjetasCredito+="where tf.tipo_tarjeta_financiera='"+ConstantesBD.tiposTarjetaCredito.getAcronimo()+"' and dprc.numero_recibo_caja='"+numeroReciboCaja+"' and dprc.institucion="+institucion;
//            //condicion tarjetas Debito
//            totalTarjetasDebito+="where tf.tipo_tarjeta_financiera='"+ConstantesBD.tiposTarjetaDebito.getAcronimo()+"' and dprc.numero_recibo_caja='"+numeroReciboCaja+"' and dprc.institucion="+institucion;
//            
//            consulta= totalPagosEfectivos+union+totalPagosCheques+union+totalTarjetasCredito+union+totalTarjetasDebito;
//            
        	
        	// condicion efectivo
        	totalPagosEfectivos+="where fp.tipo_detalle="+ConstantesBD.codigoTipoDetalleFormasPagoNinguno+" and dprc.numero_recibo_caja='"+numeroReciboCaja+"' and dprc.institucion="+institucion;
        	//condicion cheque
        	totalPagosCheques+="where dprc.numero_recibo_caja='"+numeroReciboCaja+"' and dprc.institucion="+institucion;
            //condicion tarjetas Credito
        	totalTarjetasCredito+="where tf.tipo_tarjeta_financiera='"+ConstantesBD.tiposTarjetaCredito.getAcronimo()+"' and dprc.numero_recibo_caja='"+numeroReciboCaja+"' and dprc.institucion="+institucion;
            //condicion tarjetas Debito
            totalTarjetasDebito+="where tf.tipo_tarjeta_financiera='"+ConstantesBD.tiposTarjetaDebito.getAcronimo()+"' and dprc.numero_recibo_caja='"+numeroReciboCaja+"' and dprc.institucion="+institucion;
            
            //condicion Bonos
            totalBonos+="where fp.tipo_detalle="+ConstantesBD.codigoTipoDetalleFormasPagoBono+" and dprc.numero_recibo_caja='"+numeroReciboCaja+"' and dprc.institucion="+institucion;
           
            consulta= totalPagosEfectivos+union+totalPagosCheques+union+totalTarjetasCredito+union+totalTarjetasDebito+union+totalBonos;
            
            
            PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
        }
        catch(SQLException e)
		{
		    logger.error("Error cargano Recibo de Caja [SqlBaseAnulacionReciboCajaDao]"+e);
		    e.printStackTrace();
		}
        return mapa;
    }

    
    /**
     * Consulta los recibos de caja para utilizarlos en las funcionalidades de cierre caja y arqueos
     * @param con
     * @param codigoInstitucion, codigoInstitucion
     * @param fechaDDMMYYYY, aca va la fecha generacion del arqueo o gecha generacion cierre
     * @param loginUsuarioCajero, login del usuario/cajero al que se va ha realizar  el arqueo o cierre 
     * @param consecutivoCaja, caja a la cual se le esta realizando el cierre o arqueo 
     * @param restriccionEstadosSeparadosXComas, restricciones por los estados eje 1,4 recaudado-anulado
     * @param loginUsuarioGenera
     * @param fechaGeneracionConsulta
     * @param horaGeneracionConsulta
     * @param bloquearRegistro, LE COLOCA UN SELECT FOR UPDATE EN CASO DE QUE VENGA EN TRUE, TENGA 
     * EN CUENTA QUE DEBE VENIR CONNECTION EN UNA TRANSACCION CUANDO bloquearRegistro=TRUE
     * @param consecutivoArqueoOCierre. en caso de que no sea vacio "" entonces busca por el consecutivo arqueo
     * @param codigoTipoArqueo
     * @return
     */
	public static HashMap consultaRecibosCaja(	Connection con, 
												int codigoInstitucion, 
												String fechaDDMMYYYY, 
												String loginUsuarioCajero, 
												String consecutivoCaja, 
												String restriccionEstadosSeparadosXComas,
												String loginUsuarioGenera,
												String fechaGeneracionConsulta,
												String horaGeneracionConsulta,
												boolean bloquearRegistro,
												String consecutivoArqueoOCierre,
												int codigoTipoArqueo,
												String consecutivoCajaPpal
												)
	{
		String consultaAEjecutar="";
		String consultaWhere="";
		String consultaConBloqueo="";
		
		try
		{
			//PRIMERO SE CONSULTA EL LISTADO DE RECIBOS CAJA VERIFICANDO SI DEBE REALIZAR BLOQUEO O NO
			consultaWhere=armarWhereConsultaRecibosCajaYFormasPago(codigoInstitucion, fechaDDMMYYYY, loginUsuarioCajero, consecutivoCaja, restriccionEstadosSeparadosXComas, true, fechaGeneracionConsulta, horaGeneracionConsulta, consecutivoArqueoOCierre, codigoTipoArqueo, /*estados devolucion*/null, true, true );
			consultaAEjecutar= consultaRecibosCajaStr+consultaWhere;
			
			PreparedStatementDecorator statement=null;
			if(bloquearRegistro)
			{	
				consultaConBloqueo=consultaRecibosCajaParaBloqueosStr+consultaWhere+" FOR UPDATE NOWAIT ";
				logger.info("\n\n\n CONSULTA CON BLOQUEO--->"+consultaConBloqueo);
				statement= new PreparedStatementDecorator(con.prepareStatement(consultaConBloqueo,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				statement.executeQuery();
				
				
				////tambien hacemos los bloqueos a nivel de devoluciones
				Vector vectorEstadosDevol= new Vector<String>();
				vectorEstadosDevol.add(ConstantesIntegridadDominio.acronimoEstadoAprobado);
				
				String consultaWhereDevolucionesRC= armarWhereConsultaDevolucionesRecibosCajaYFormasPago(codigoInstitucion, fechaDDMMYYYY, loginUsuarioCajero, consecutivoCaja, restriccionEstadosSeparadosXComas, true, fechaGeneracionConsulta, horaGeneracionConsulta, consecutivoArqueoOCierre, codigoTipoArqueo, vectorEstadosDevol, true, true );
				String consultaBloqueoDevolucionesRC= consultaDevolucionesRCParaBloqueosStr+consultaWhereDevolucionesRC+" FOR UPDATE NOWAIT ";
				logger.info("\n\n\n consultaBloqueoDevolucionesRC--->"+consultaBloqueoDevolucionesRC);
				PreparedStatementDecorator statementDevol=new PreparedStatementDecorator(con.prepareStatement(consultaBloqueoDevolucionesRC,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				statementDevol.executeQuery();
			}
			statement= new PreparedStatementDecorator(con.prepareStatement(consultaAEjecutar,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			HashMap mapToReturn=UtilidadBD.cargarValueObject(new ResultSetDecorator(statement.executeQuery()), true, true);
			
			//SEGUNDO SE HACE UN PUT DE LA CONSULTA PARA LA IMPRESION CON BIRT, TENIENDO EN CUENTA LA FECHA HORA DE LA GENERACION DE LA CONSULTA [CONCURRENCIA]
			mapToReturn.put("consultaPpalRecibosCaja", consultaAEjecutar);
			
			logger.info("\n\n****************************************************************************************");
			logger.info("CONSULTA RC-->"+consultaAEjecutar);
			logger.info("****************************************************************************************\n\n");
			
			//CONSULTAMOS LAS DEVOLUCIONES DE LOS RECIBOS CAJA
			Vector<String> vectorEstadosDevol= new Vector<String>();
			vectorEstadosDevol.add(ConstantesIntegridadDominio.acronimoEstadoAprobado);
			vectorEstadosDevol.add(ConstantesIntegridadDominio.acronimoEstadoAnulado);
			consultaWhere=armarWhereConsultaDevolucionesRecibosCajaYFormasPago(codigoInstitucion, fechaDDMMYYYY, loginUsuarioCajero, consecutivoCaja, restriccionEstadosSeparadosXComas, true, fechaGeneracionConsulta, horaGeneracionConsulta, consecutivoArqueoOCierre, codigoTipoArqueo, vectorEstadosDevol, true, true);
			consultaAEjecutar=consultaDevolucionesRC+consultaWhere;
			
			logger.info("\n\n****************************************************************************************");
			logger.info("CONSULTA DET DEVOLUCIONES-->"+consultaAEjecutar);
			logger.info("****************************************************************************************\n\n");
			
			statement= new PreparedStatementDecorator(con.prepareStatement(consultaAEjecutar,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			HashMap mapDevoluciones=UtilidadBD.cargarValueObject(new ResultSetDecorator(statement.executeQuery()), true, true);
			
			//SE HACE UN PUT DEL WHERE PARA ADICIONARLO A LA CONSULTA DE LA IMPRESION CON BIRT
			mapToReturn.put("devoluciones", consultaAEjecutar);
			mapToReturn.put("detalleDevoluciones", mapDevoluciones);
			
			//TERCERO ADICIONAMOS LA CONSULTA DEL DETALLE DE LAS FORMA DE PAGO Y LO ADICIONAMOS COMO UN MAPA AL MAPA PRINCIPAL
			
			vectorEstadosDevol= new Vector<String>();
			vectorEstadosDevol.add(ConstantesIntegridadDominio.acronimoEstadoAprobado);
			
			String consultaDetalleFormaPagoStr=	"SELECT tabla.\"detcodigoformapago\" as detcodigoformapago, tabla.\"detdescripcionformapago\" as detdescripcionformapago, coalesce(sum(tabla.\"detvalortotalformapago\"),0) as detvalortotalformapago FROM ("+
													"SELECT " +
														"dprc.forma_pago AS \"detcodigoformapago\", " +
														"fp.descripcion AS \"detdescripcionformapago\", " +
														"coalesce(dprc.valor,0) AS \"detvalortotalformapago\" " +
													"FROM recibos_caja rc " +
														"INNER JOIN detalle_pagos_rc dprc ON (dprc.numero_recibo_caja=rc.numero_recibo_caja AND dprc.institucion=rc.institucion) " +
														"INNER JOIN formas_pago fp ON(dprc.forma_pago=fp.consecutivo) "
														+armarWhereConsultaRecibosCajaYFormasPago(codigoInstitucion, fechaDDMMYYYY, loginUsuarioCajero, consecutivoCaja, restriccionEstadosSeparadosXComas, false, fechaGeneracionConsulta, horaGeneracionConsulta, consecutivoArqueoOCierre, codigoTipoArqueo, /*estados devolucion*/null, false,false)+" "+
													"UNION ALL " +
													"select "+ValoresPorDefecto.getFormaPagoEfectivo(codigoInstitucion)+" as \"detcodigoformapago\", " +
														"(SELECT fp.descripcion FROM formas_pago FP WHERE fp.consecutivo="+ValoresPorDefecto.getFormaPagoEfectivo(codigoInstitucion)+") AS \"detdescripcionformapago\",  " +
														"coalesce(d.valor_devolucion,0) * -1 AS \"detvalortotalformapago\"  " +
													"FROM recibos_caja rc " +
														"INNER JOIN devol_recibos_caja d ON (d.numero_rc=rc.numero_recibo_caja AND d.institucion=rc.institucion)  " 
														+armarWhereConsultaDevolucionesRecibosCajaYFormasPago(codigoInstitucion, fechaDDMMYYYY, loginUsuarioCajero, consecutivoCaja, restriccionEstadosSeparadosXComas, false, fechaGeneracionConsulta, horaGeneracionConsulta, consecutivoArqueoOCierre, codigoTipoArqueo, vectorEstadosDevol, false, false)+" "+
												") tabla group by tabla.\"detcodigoformapago\", tabla.\"detdescripcionformapago\" ";	

			statement= new PreparedStatementDecorator(con, consultaDetalleFormaPagoStr);
			Log4JManager.info(statement);
			HashMap mapDetalleFormaPago=UtilidadBD.cargarValueObject(new ResultSetDecorator(statement.executeQuery()), true, true);
			statement.close();
			
			//CUARTO SE HACE UN PUT DEL WHERE PARA ADICIONARLO A LA CONSULTA DE LA IMPRESION CON BIRT
			mapToReturn.put("consultaFormaPago", consultaDetalleFormaPagoStr);
			mapToReturn.put("detalleFormaPago", mapDetalleFormaPago);
			
			//QUINTO SE CONSULTA LA INFORMACION DEL ENCABEZADO DEL ARQUEO
			mapToReturn.put("encabezadoArqueo", encabezadoArqueos(con,loginUsuarioGenera, loginUsuarioCajero, consecutivoCaja, fechaDDMMYYYY, fechaGeneracionConsulta, horaGeneracionConsulta, consecutivoArqueoOCierre, codigoInstitucion, codigoTipoArqueo, consecutivoCajaPpal));
			
			//SEXTO SE CARGA EL NUMERO DE RECIBOS DE CAJA QUE ESTEN EN UN ESTADO DADO
			HashMap mapContadores=new HashMap();
			String combinadoNumeroRecibosEnEstadoDadoYConsulta[]=getNumeroRecibosCajasEnEstadoDado(con, codigoInstitucion, fechaDDMMYYYY, fechaGeneracionConsulta, horaGeneracionConsulta, loginUsuarioCajero, consecutivoCaja, ConstantesBD.codigoEstadoReciboCajaAnulado, consecutivoArqueoOCierre, codigoTipoArqueo);
			mapContadores.put("numeroRecibosCajaAnulados", combinadoNumeroRecibosEnEstadoDadoYConsulta[0]);
			mapContadores.put("consultaNumeroRecibosCajaAnulados", combinadoNumeroRecibosEnEstadoDadoYConsulta[1]);
			
			combinadoNumeroRecibosEnEstadoDadoYConsulta=getNumeroRecibosCajasEnEstadoDado(con, codigoInstitucion, fechaDDMMYYYY, fechaGeneracionConsulta, horaGeneracionConsulta, loginUsuarioCajero, consecutivoCaja, ConstantesBD.codigoEstadoReciboCajaEnArqueo, consecutivoArqueoOCierre, codigoTipoArqueo);
			mapContadores.put("numeroRecibosCajaEnArqueo", combinadoNumeroRecibosEnEstadoDadoYConsulta[0]);
			mapContadores.put("consultaNumeroRecibosCajaEnArqueo", combinadoNumeroRecibosEnEstadoDadoYConsulta[1]);
			mapContadores.put("consultaSumaAnuladosYEnArqueo", combinadoNumeroRecibosEnEstadoDadoYConsulta[2]);
			
			combinadoNumeroRecibosEnEstadoDadoYConsulta= getNumeroRecibosCajasEnEstadoDado(con, codigoInstitucion, fechaDDMMYYYY, fechaGeneracionConsulta, horaGeneracionConsulta, loginUsuarioCajero, consecutivoCaja, ConstantesBD.codigoEstadoReciboCajaEnCierre, consecutivoArqueoOCierre, codigoTipoArqueo);
			mapContadores.put("numeroRecibosCajaEnCierre", combinadoNumeroRecibosEnEstadoDadoYConsulta[0]);
			mapContadores.put("consultaNumeroRecibosCajaEnCierre", combinadoNumeroRecibosEnEstadoDadoYConsulta[1]);
			mapContadores.put("consultaSumaAnuladosYEnCierre", combinadoNumeroRecibosEnEstadoDadoYConsulta[2]);
			
			combinadoNumeroRecibosEnEstadoDadoYConsulta= getNumeroRecibosCajasEnEstadoDado(con, codigoInstitucion, fechaDDMMYYYY, fechaGeneracionConsulta, horaGeneracionConsulta, loginUsuarioCajero, consecutivoCaja, ConstantesBD.codigoEstadoReciboCajaRecaudado, consecutivoArqueoOCierre, codigoTipoArqueo);
			mapContadores.put("numeroRecibosCajaRecaudado", combinadoNumeroRecibosEnEstadoDadoYConsulta[0]);
			mapContadores.put("consultaNumeroRecibosCajaRecaudado", combinadoNumeroRecibosEnEstadoDadoYConsulta[1]);
			mapContadores.put("consultaSumaAnuladosYRecaudados", combinadoNumeroRecibosEnEstadoDadoYConsulta[2]);
			
			String combinadoDevolucionesRCEnEstadoDadoYConsulta[]= getNumeroDevolucionesRCEnEstadoDado(con, codigoInstitucion, fechaDDMMYYYY, fechaGeneracionConsulta, horaGeneracionConsulta, loginUsuarioCajero, consecutivoCaja, consecutivoArqueoOCierre, codigoTipoArqueo, ConstantesIntegridadDominio.acronimoEstadoAprobado, restriccionEstadosSeparadosXComas);
			mapContadores.put("numeroDevolucionesAprobadas", combinadoDevolucionesRCEnEstadoDadoYConsulta[0]);
			mapContadores.put("consultaDevolucionesAprobadas", combinadoDevolucionesRCEnEstadoDadoYConsulta[1]);
			mapContadores.put("consultaDevolSumaAnuladosYAprobados", combinadoDevolucionesRCEnEstadoDadoYConsulta[2]);
			
			combinadoDevolucionesRCEnEstadoDadoYConsulta= getNumeroDevolucionesRCEnEstadoDado(con, codigoInstitucion, fechaDDMMYYYY, fechaGeneracionConsulta, horaGeneracionConsulta, loginUsuarioCajero, consecutivoCaja, consecutivoArqueoOCierre, codigoTipoArqueo, ConstantesIntegridadDominio.acronimoEstadoAnulado, restriccionEstadosSeparadosXComas);
			mapContadores.put("numeroDevolucionesAnuladas", combinadoDevolucionesRCEnEstadoDadoYConsulta[0]);
			mapContadores.put("consultaDevolucionesAnuladas", combinadoDevolucionesRCEnEstadoDadoYConsulta[1]);
			
			mapToReturn.put("mapContadores", mapContadores);
			return mapToReturn;
		}
		catch (SQLException e)
		{
			logger.error("Error cargando consulta Recibos Caja : ",e);
			//e.printStackTrace();
			HashMap mapaError= new HashMap();
			mapaError.put("numRegistros", "0");
			mapaError.put("ERROR", e.getSQLState()+"");
			return mapaError;
		}
	}
    
	/**
	 * metodo privado que arma el segmento de consulta where para consultar los
	 * recibos de caja o el detalle de las formas de pago
	 * @param codigoInstitucion
	 * @param fechaDDMMYYYY
	 * @param loginUsuarioCajero
	 * @param consecutivoCaja
	 * @param restriccionEstadosSeparadosXComas
	 * @param esConsultaRecibosCaja
	 * @param consecutivoArqueoOCierre
	 * @param codigoTipoArqueo
	 * @return
	 */
	private static String armarWhereConsultaRecibosCajaYFormasPago(	int codigoInstitucion, 
																	String fechaDDMMYYYY, 
																	String loginUsuarioCajero, 
																	String consecutivoCaja, 
																	String restriccionEstadosSeparadosXComas,
																	boolean esConsultaRecibosCaja,
																	String fechaGeneracionConsulta, 
																	String horaGeneracionConsulta,
																	String consecutivoArqueoOCierre,
																	int codigoTipoArqueo,
																	Vector<String> estadosDevolucion,
																	boolean hacerOrder,
																	boolean hacerGroupBy)
	{
		String consultaWhereAEjecutar="";
		fechaGeneracionConsulta=UtilidadFecha.conversionFormatoFechaABD(fechaGeneracionConsulta);
		String restricciones=	" where rc.institucion= "+codigoInstitucion+" ";
		
		if(estadosDevolucion!=null)
		{
			restricciones+="AND d.estado IN("+UtilidadTexto.convertirVectorACodigosSeparadosXComas(estadosDevolucion, true)+")";
		}
		
		if(!consecutivoArqueoOCierre.equals(""))
		{
			if(codigoTipoArqueo==ConstantesBD.codigoTipoArqueoDefinitivo)
				restricciones+=" AND rc.arqueo_definitivo='"+consecutivoArqueoOCierre+"'";
			else if(codigoTipoArqueo==ConstantesBD.codigoTipoArqueoCierreCaja)
				restricciones+=" AND rc.cierre_caja='"+consecutivoArqueoOCierre+"' ";
		}
		else
		{	
			if(codigoTipoArqueo==ConstantesBD.codigoTipoArqueoDefinitivo)
				restricciones+=" AND rc.arqueo_definitivo is null ";
			else if(codigoTipoArqueo==ConstantesBD.codigoTipoArqueoCierreCaja)
				restricciones+=" AND rc.cierre_caja is null ";
			else if(codigoTipoArqueo==ConstantesBD.codigoTipoArqueoProvisional)
				restricciones+=" AND rc.arqueo_definitivo is null ";
		}			
		
		restricciones+=" and (to_char(rc.fecha, 'YYYY-MM-DD') < '"+fechaGeneracionConsulta+"' or ( to_char(rc.fecha, 'YYYY-MM-DD') ='"+fechaGeneracionConsulta+"' and rc.hora <= '"+horaGeneracionConsulta+"')) ";
		String restriccionesEstados="";
		String groupBy="";
		String orderBy="";
		
		if(hacerOrder)
		{	
			orderBy= "ORDER BY rc.numero_recibo_caja ";
			//para este caso se ordena es por los consecutivos de devolucion
			if(estadosDevolucion!=null)
			{
				orderBy=" ORDER BY consecutivo ";
			}
		}
		if(!fechaDDMMYYYY.trim().equals(""))
			restricciones+=" and to_char(rc.fecha, 'YYYY-MM-DD') = '"+UtilidadFecha.conversionFormatoFechaABD(fechaDDMMYYYY)+"' ";	
		if(!loginUsuarioCajero.trim().equals(""))
			restricciones+=" and rc.usuario='"+loginUsuarioCajero+"' ";
		if(!consecutivoCaja.trim().equals(""))
			restricciones+=" and rc.caja = '"+consecutivoCaja+"' ";
		if(!restriccionEstadosSeparadosXComas.trim().equals(""))
			restriccionesEstados+=" and rc.estado in ("+restriccionEstadosSeparadosXComas+") ";
		
		if(esConsultaRecibosCaja)
			consultaWhereAEjecutar=restricciones+restriccionesEstados+orderBy;
		else 
		{
			restriccionesEstados=" and rc.estado in ("+restriccionEstadosSeparadosXComas+") and rc.estado<> "+ConstantesBD.codigoEstadoReciboCajaAnulado+" ";
			if(hacerGroupBy)
				groupBy=" GROUP BY dprc.forma_pago, fp.descripcion ";
			if(hacerOrder)
				orderBy=" ORDER BY dprc.forma_pago ";
			consultaWhereAEjecutar=restricciones+restriccionesEstados+groupBy+orderBy;
		}
		
		
		return consultaWhereAEjecutar;
		
	}
	
	
	/**
	 * metodo privado que arma el segmento de consulta where para consultar los
	 * recibos de caja o el detalle de las formas de pago
	 * @param codigoInstitucion
	 * @param fechaDDMMYYYY
	 * @param loginUsuarioCajero
	 * @param consecutivoCaja
	 * @param restriccionEstadosSeparadosXComas
	 * @param esConsultaRecibosCaja
	 * @param consecutivoArqueoOCierre
	 * @param codigoTipoArqueo
	 * @return
	 */
	private static String armarWhereConsultaDevolucionesRecibosCajaYFormasPago(	int codigoInstitucion, 
																				String fechaDDMMYYYY, 
																				String loginUsuarioCajero, 
																				String consecutivoCaja, 
																				String restriccionEstadosSeparadosXComas,
																				boolean esConsultaRecibosCaja,
																				String fechaGeneracionConsulta, 
																				String horaGeneracionConsulta,
																				String consecutivoArqueoOCierre,
																				int codigoTipoArqueo,
																				Vector<String> estadosDevolucion,
																				boolean hacerOrder,
																				boolean hacerGroupBy)
	{
		String consultaWhereAEjecutar="";
		fechaGeneracionConsulta=UtilidadFecha.conversionFormatoFechaABD(fechaGeneracionConsulta);
		String restricciones=	" where rc.institucion= "+codigoInstitucion+" ";
		
		if(estadosDevolucion!=null)
		{
			restricciones+="AND d.estado IN("+UtilidadTexto.convertirVectorACodigosSeparadosXComas(estadosDevolucion, true)+")";
		}
		
		if(!consecutivoArqueoOCierre.equals(""))
		{
			if(codigoTipoArqueo==ConstantesBD.codigoTipoArqueoDefinitivo)
				restricciones+=" AND d.arqueo_definitivo='"+consecutivoArqueoOCierre+"'";
			else if(codigoTipoArqueo==ConstantesBD.codigoTipoArqueoCierreCaja)
				restricciones+=" AND d.cierre_caja='"+consecutivoArqueoOCierre+"' ";
		}
		else
		{	
			if(codigoTipoArqueo==ConstantesBD.codigoTipoArqueoDefinitivo)
				restricciones+=" AND d.arqueo_definitivo is null ";
			else if(codigoTipoArqueo==ConstantesBD.codigoTipoArqueoCierreCaja)
				restricciones+=" AND d.cierre_caja is null ";
			else if(codigoTipoArqueo==ConstantesBD.codigoTipoArqueoProvisional)
				restricciones+=" AND d.arqueo_definitivo is null ";
		}			
		
		restricciones+=" and (to_char(d.fecha_devolucion, 'YYYY-MM-DD') < '"+fechaGeneracionConsulta+"' or ( to_char(d.fecha_devolucion, 'YYYY-MM-DD') ='"+fechaGeneracionConsulta+"' and d.hora_devolucion <= '"+horaGeneracionConsulta+"')) ";
		String restriccionesEstados="";
		String groupBy="";
		String orderBy="";
		
		if(hacerOrder)
		{	
			orderBy= "ORDER BY rc.numero_recibo_caja ";
			//para este caso se ordena es por los consecutivos de devolucion
			if(estadosDevolucion!=null)
			{
				orderBy=" ORDER BY consecutivo ";
			}
		}
		if(!fechaDDMMYYYY.trim().equals(""))
			restricciones+=" and to_char(d.fecha_devolucion, 'YYYY-MM-DD') = '"+UtilidadFecha.conversionFormatoFechaABD(fechaDDMMYYYY)+"' ";	
		if(!loginUsuarioCajero.trim().equals(""))
			restricciones+=" and d.usuario_devolucion='"+loginUsuarioCajero+"' ";
		if(!consecutivoCaja.trim().equals(""))
			restricciones+=" and d.caja_devolucion = '"+consecutivoCaja+"' ";
		/*if(!restriccionEstadosSeparadosXComas.trim().equals(""))
			restriccionesEstados+=" and rc.estado in ("+restriccionEstadosSeparadosXComas+") ";*/
		
		if(esConsultaRecibosCaja)
			consultaWhereAEjecutar=restricciones+restriccionesEstados+orderBy;
		else 
		{
			//restriccionesEstados=" and rc.estado in ("+restriccionEstadosSeparadosXComas+") and rc.estado<> "+ConstantesBD.codigoEstadoReciboCajaAnulado+" ";
			if(hacerGroupBy)
				groupBy=" GROUP BY dprc.forma_pago, fp.descripcion ";
			if(hacerOrder)
				orderBy=" ORDER BY dprc.forma_pago ";
			consultaWhereAEjecutar=restricciones+restriccionesEstados+groupBy+orderBy;
		}
		
		
		return consultaWhereAEjecutar;
		
	}
	
	
	
	
	
	
	
	
	/**
	 * Carga la informacion del encabezado de arqueos
	 * @param con
	 * @param loginUsuario
	 * @param loginCajero
	 * @param consecutivoCaja
	 * @return
	 */
	private static HashMap encabezadoArqueos(Connection con, String loginUsuario, String loginCajero, String consecutivoCaja, 
												String fechaDDMMYYYY, String fechaGeneracionConsulta, String horaGeneracionConsulta, 
												String consecutivoArqueoOCierre, int institucion, int codigoTipoArqueo, String consecutivoCajaPpal)
	{
		HashMap mapToReturn=new HashMap();
		String consulta="";
		
		if(consecutivoArqueoOCierre.equals(""))
		{	
			if(codigoTipoArqueo==ConstantesBD.codigoTipoArqueoCierreCaja)
			{
				consulta=	"SELECT '"+fechaGeneracionConsulta+"' AS fechageneracion, '" +
							horaGeneracionConsulta+"' AS horageneracion, " +
							"getnombrepersona(u.codigo_persona) ||'['||u.login||']' AS usuariogenera, " +
							"getnombrepersona(u1.codigo_persona) ||' ['||u1.login||']' AS cajero, " +
							"c.descripcion ||' - '|| getnomcentroatencion(c.centro_atencion) AS descripcionCaja,  '" +
							fechaDDMMYYYY +"' AS fechaarqueo, " +
							"c1.descripcion ||' - '|| getnomcentroatencion(c1.centro_atencion) AS descripcionCajaPpal " +
							"FROM " +
							"usuarios u, " +
							"usuarios u1, " +
							"cajas c, " +
							"cajas c1 " +
							"WHERE " +
							"u.login= '"+loginUsuario+"' "+
							"and u1.login= '"+loginCajero+"' "+
							"and c.consecutivo= '"+consecutivoCaja+"' ";
							
							if(!UtilidadTexto.isEmpty(consecutivoCajaPpal))
							{	
								consulta+="and c1.consecutivo= '"+consecutivoCajaPpal+"' ";
							}
			}
			else
			{	
				consulta=	"SELECT '"+fechaGeneracionConsulta+"' AS fechageneracion, '" +
							horaGeneracionConsulta+"' AS horageneracion, " +
							"getnombrepersona(u.codigo_persona) ||'['||u.login||']' AS usuariogenera, " +
							"getnombrepersona(u1.codigo_persona) ||' ['||u1.login||']' AS cajero, " +
							"c.descripcion ||' - '|| getnomcentroatencion(c.centro_atencion) AS descripcionCaja,  '" +
							fechaDDMMYYYY +"' AS fechaarqueo " +
							"FROM " +
							"usuarios u, " +
							"usuarios u1, " +
							"cajas c " +
							"WHERE " +
							"u.login= '"+loginUsuario+"' "+
							"and u1.login= '"+loginCajero+"' "+
							"and c.consecutivo= '"+consecutivoCaja+"' ";
			}	
		}
		else
		{
			consulta=	" SELECT " ;
			if(codigoTipoArqueo==ConstantesBD.codigoTipoArqueoDefinitivo)
			{	
				consulta+=" (select to_char(fecha_arqueo, 'DD/MM/YYYY') FROM arqueos_definitivos where consecutivo='"+consecutivoArqueoOCierre+"' and institucion="+institucion+") ";
			}
			else if(codigoTipoArqueo==ConstantesBD.codigoTipoArqueoCierreCaja)
			{
				consulta+= " (select to_char(fecha_cierre, 'DD/MM/YYYY') FROM cierres_cajas where consecutivo='"+consecutivoArqueoOCierre+"' and institucion="+institucion+") ";
			}
			consulta+=	" AS fechageneracion, ";
			if(codigoTipoArqueo==ConstantesBD.codigoTipoArqueoDefinitivo)
			{
				consulta+=" (select substr(hora_arqueo, 1,5) FROM arqueos_definitivos where consecutivo='"+consecutivoArqueoOCierre+"' and institucion="+institucion+") ";
			}
			else if(codigoTipoArqueo==ConstantesBD.codigoTipoArqueoCierreCaja)
			{
				consulta+= " (select substr(hora_cierre, 1,5) FROM cierres_cajas where consecutivo='"+consecutivoArqueoOCierre+"' and institucion="+institucion+") ";
			}
			
			
			if(codigoTipoArqueo==ConstantesBD.codigoTipoArqueoCierreCaja)
			{
				consulta+=		"AS horageneracion, " +
								"getnombrepersona(u.codigo_persona) ||'['||u.login||']' AS usuariogenera, " +
								"getnombrepersona(u1.codigo_persona) ||' ['||u1.login||']' AS cajero, " +
								"c.descripcion ||' - '|| getnomcentroatencion(c.centro_atencion) AS descripcionCaja,  '" +
								fechaDDMMYYYY +"' AS fechaarqueo, " +
								"c1.descripcion ||' - '|| getnomcentroatencion(c1.centro_atencion) AS descripcionCajaPpal " +
								"FROM " +
								"usuarios u, " +
								"usuarios u1, " +
								"cajas c, " +
								"cajas c1 " +
								"WHERE " +
								"u.login= '"+loginUsuario+"' "+
								"and u1.login= '"+loginCajero+"' "+
								"and c.consecutivo= '"+consecutivoCaja+"' ";
								
								if(!UtilidadTexto.isEmpty(consecutivoCajaPpal))
								{	
									consulta+="and c1.consecutivo= '"+consecutivoCajaPpal+"' ";
								}
			}
			else
			{
				consulta+=		"AS horageneracion, " +
								"getnombrepersona(u.codigo_persona) ||'['||u.login||']' AS usuariogenera, " +
								"getnombrepersona(u1.codigo_persona) ||' ['||u1.login||']' AS cajero, " +
								"c.descripcion ||' - '|| getnomcentroatencion(c.centro_atencion) AS descripcionCaja,  '" +
								fechaDDMMYYYY +"' AS fechaarqueo " +
								"FROM " +
								"usuarios u, " +
								"usuarios u1, " +
								"cajas c " +
								"WHERE " +
								"u.login= '"+loginUsuario+"' "+
								"and u1.login= '"+loginCajero+"' "+
								"and c.consecutivo= '"+consecutivoCaja+"' ";
			}
		}
		
		consulta+=" "+ValoresPorDefecto.getValorLimit1()+" 1 ";
		logger.info("\n\n encabezado Arqueos->"+consulta);
		
		try
		{
			PreparedStatementDecorator statement= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			mapToReturn=UtilidadBD.cargarValueObject(new ResultSetDecorator(statement.executeQuery()), true, true);
		}	
		catch (SQLException e)
		{
			logger.error("Error cargando encabezadoArqueos : "+e);
			return null;
		}
		mapToReturn.put("consultaEncabezadoArqueos", consulta);
		return mapToReturn;
	}
	
	
	/**
	 * metodo que obtiene el numero de recibos de caja que esten en un estado dado
	 * y la consulta realizada para asignarla a la consulta de birt
	 * @param con
	 * @param codigoInstitucion
	 * @param fechaDDMMYYYY
	 * @param fechaGeneracionConsulta
	 * @param horaGeneracionConsulta
	 * @param loginUsuarioCajero
	 * @param consecutivoCaja
	 * @param codigoEstado
	 * @return
	 */
	private static String[] getNumeroRecibosCajasEnEstadoDado (	Connection con, 
																int codigoInstitucion, 
																String fechaDDMMYYYY, 
																String fechaGeneracionConsulta, 
																String horaGeneracionConsulta, 
																String loginUsuarioCajero, 
																String consecutivoCaja, 
																int codigoEstado,
																String consecutivoArqueoOCierre,
																int codigoTipoArqueo)
	{
		String[] respuesta={"","", ""};
		fechaGeneracionConsulta=UtilidadFecha.conversionFormatoFechaABD(fechaGeneracionConsulta);
		fechaDDMMYYYY=UtilidadFecha.conversionFormatoFechaABD(fechaDDMMYYYY);
		String alias="";
		if(codigoEstado==ConstantesBD.codigoEstadoReciboCajaAnulado)
			alias="contadorAnulados";
		else if(codigoEstado==ConstantesBD.codigoEstadoReciboCajaEnArqueo)
			alias="contadorArqueados";
		else if(codigoEstado==ConstantesBD.codigoEstadoReciboCajaEnCierre)
			alias="contadorCerrados";
		else if(codigoEstado==ConstantesBD.codigoEstadoReciboCajaRecaudado)
			alias="contadorRecaudados";
		
		String consulta="SELECT count(1) AS "+alias+" from recibos_caja rc where " +
						"rc.institucion= "+codigoInstitucion+"  " +
						"and (to_char(rc.fecha, 'YYYY-MM-DD') < '"+fechaGeneracionConsulta+"' or ( to_char(rc.fecha, 'YYYY-MM-DD') ='"+fechaGeneracionConsulta+"' and rc.hora <= '"+horaGeneracionConsulta+"'))  " +
						"and to_char(rc.fecha, 'YYYY-MM-DD') = '"+fechaDDMMYYYY+"'  " +
						"and rc.usuario='"+loginUsuarioCajero+"'  " +
						"and rc.caja = '"+consecutivoCaja+"' "; 
		
		if(!consecutivoArqueoOCierre.equals(""))
		{
			if(codigoTipoArqueo==ConstantesBD.codigoTipoArqueoDefinitivo)
				consulta+=" AND rc.arqueo_definitivo='"+consecutivoArqueoOCierre+"'";
			if(codigoTipoArqueo==ConstantesBD.codigoTipoArqueoCierreCaja)
				consulta+=" AND rc.cierre_caja='"+consecutivoArqueoOCierre+"' ";
		}
		else
		{
			if(codigoTipoArqueo==ConstantesBD.codigoTipoArqueoProvisional || codigoTipoArqueo==ConstantesBD.codigoTipoArqueoDefinitivo)
				consulta+=" AND rc.arqueo_definitivo is null AND rc.cierre_caja is null ";
			else
				consulta+=" AND rc.cierre_caja is null ";
		}
		
		String consultaConlaSumaAnulados=""; 
			
		consultaConlaSumaAnulados= consulta+" and (rc.estado ="+codigoEstado+" or rc.estado = "+ConstantesBD.codigoEstadoReciboCajaAnulado+" ";
		
		if(codigoTipoArqueo==ConstantesBD.codigoTipoArqueoDefinitivo && codigoEstado==ConstantesBD.codigoEstadoReciboCajaEnArqueo)
			consultaConlaSumaAnulados+=" or rc.estado= "+ConstantesBD.codigoEstadoReciboCajaEnCierre;
		
		consultaConlaSumaAnulados+=") ";
		
		consultaConlaSumaAnulados= consultaConlaSumaAnulados.replaceAll(alias, "contadorTotal");
		consulta+=" and (rc.estado ="+codigoEstado+" ";
		
		if(codigoTipoArqueo==ConstantesBD.codigoTipoArqueoDefinitivo && codigoEstado==ConstantesBD.codigoEstadoReciboCajaEnArqueo)
			consulta+=" or rc.estado= "+ConstantesBD.codigoEstadoReciboCajaEnCierre;
		consulta+=") ";
		
		try
		{
			PreparedStatementDecorator statement= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs= new ResultSetDecorator(statement.executeQuery());
			if(rs.next())
				respuesta[0]=rs.getString(1);
			else
				respuesta[0]="-1";
			respuesta[1]=consulta;
			respuesta[2]=consultaConlaSumaAnulados;
			
			return respuesta;
		}	
		catch (SQLException e)
		{
			logger.error("Error cargando getNumeroRecibosCajasEnEstadoDado : "+e);
			return respuesta;
		}
	}

	/**
	 * metodo que obtiene el numero de recibos de caja que esten en un estado dado
	 * y la consulta realizada para asignarla a la consulta de birt
	 * @param con
	 * @param codigoInstitucion
	 * @param fechaDDMMYYYY
	 * @param fechaGeneracionConsulta
	 * @param horaGeneracionConsulta
	 * @param loginUsuarioCajero
	 * @param consecutivoCaja
	 * @param codigoEstado
	 * @return
	 */
	private static String[] getNumeroDevolucionesRCEnEstadoDado (	Connection con, 
																	int codigoInstitucion, 
																	String fechaDDMMYYYY, 
																	String fechaGeneracionConsulta, 
																	String horaGeneracionConsulta, 
																	String loginUsuarioCajero, 
																	String consecutivoCaja, 
																	String consecutivoArqueoOCierre,
																	int codigoTipoArqueo,
																	String estadoDevolucion,
																	String restriccionEstadosSeparadosXComas)
	{
		String[] respuesta={"","", ""};
		fechaGeneracionConsulta=UtilidadFecha.conversionFormatoFechaABD(fechaGeneracionConsulta);
		fechaDDMMYYYY=UtilidadFecha.conversionFormatoFechaABD(fechaDDMMYYYY);
		String alias="";
		if(estadoDevolucion.equals(ConstantesIntegridadDominio.acronimoEstadoAprobado))
			alias="contadoraprobados";
		if(estadoDevolucion.equals(ConstantesIntegridadDominio.acronimoEstadoAnulado))
			alias="contadoranulados";
		
		String consulta="SELECT count(1) AS "+alias+" from devol_recibos_caja d INNER JOIN recibos_caja rc ON(rc.numero_recibo_caja=d.numero_rc and rc.institucion=d.institucion) ";
		Vector<String> vectorEstadosDevol= new Vector<String>();
		vectorEstadosDevol.add(estadoDevolucion);
		String where= armarWhereConsultaDevolucionesRecibosCajaYFormasPago(codigoInstitucion, fechaDDMMYYYY, loginUsuarioCajero, consecutivoCaja, restriccionEstadosSeparadosXComas, true, fechaGeneracionConsulta, horaGeneracionConsulta, consecutivoArqueoOCierre, codigoTipoArqueo, vectorEstadosDevol, false, true);
		if(!estadoDevolucion.equals(ConstantesIntegridadDominio.acronimoEstadoAnulado))
			vectorEstadosDevol.add(ConstantesIntegridadDominio.acronimoEstadoAnulado);
		String consultaConlaSumaAnulados= consulta+armarWhereConsultaDevolucionesRecibosCajaYFormasPago(codigoInstitucion, fechaDDMMYYYY, loginUsuarioCajero, consecutivoCaja, restriccionEstadosSeparadosXComas, true, fechaGeneracionConsulta, horaGeneracionConsulta, consecutivoArqueoOCierre, codigoTipoArqueo, vectorEstadosDevol,false,true); 
		consultaConlaSumaAnulados= consultaConlaSumaAnulados.replaceAll(alias, "contadordevoltotal");
		consulta+=where;
		
		try
		{
			logger.info("\n************************************************************************************************");
			logger.info("CONSULTA NUM DEVOL->"+consulta);
			logger.info("************************************************************************************************\n");
			
			logger.info("\n************************************************************************************************");
			logger.info("CONSULTA NUM DEVOL CON ANULADOS->"+consultaConlaSumaAnulados);
			logger.info("************************************************************************************************\n");
			
			PreparedStatementDecorator statement= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs= new ResultSetDecorator(statement.executeQuery());
			if(rs.next())
				respuesta[0]=rs.getString(1);
			else
				respuesta[0]="-1";
			respuesta[1]=consulta;
			respuesta[2]=consultaConlaSumaAnulados;
			
			return respuesta;
		}	
		catch (SQLException e)
		{
			logger.error("Error cargando getNumeroDevolucionesRCEnEstadoDado : "+e);
			return respuesta;
		}
	}
	
	
	
	/**
	 * obtiene el codigo - nombre del centro de atencion de un recibo de caja
	 * @param consecutivoFactura
	 * @param codigoInstitucion
	 * @return
	 */
	public static InfoDatosInt getCentroAtencionReciboCaja( Connection con, String numeroReciboCaja, int codigoInstitucion)
	{
		String getCentroAtencionFacturaStr="select getnomcentroatencion(c.centro_atencion) as nombreCentroAtencion, c.centro_atencion as codigoCentroAtencion from recibos_caja r INNER JOIN cajas c ON (c.consecutivo=r.caja) where r.numero_recibo_caja=? and r.institucion=? ";
		InfoDatosInt centroAtencion= new InfoDatosInt();
		try
		{
			PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(getCentroAtencionFacturaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1, numeroReciboCaja);
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
			logger.warn("Error en el  getCentroAtencionReciboCaja " +e.toString());
		}
		return centroAtencion;
		
	}
	
	
	/**
	 * 
	 * @param con
	 * @param institucion
	 * @param numeroReciboCaja
	 * @return
	 */
	public static ArrayList<DtoDevolRecibosCaja> consultarDevolucionesAprobadas( int institucion, String numeroReciboCaja) 
	{
		ArrayList<DtoDevolRecibosCaja> listadoDevoluciones=new ArrayList<DtoDevolRecibosCaja>();
		try
		{
			Connection con=UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con,cadenaconsultarDevolucionesAprobadas);
			ps.setString(1, numeroReciboCaja);
			ps.setInt(2, institucion);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			
			logger.info("LA CONSULTA ES------>"+ps);
			while (rs.next())
			{
				DtoDevolRecibosCaja dto=new DtoDevolRecibosCaja();
				dto.setCodigoPk(rs.getInt("numdevol"));
				dto.setConsecutivo(rs.getDouble("consecutivo"));
				dto.setFechaDevolucion(rs.getString("fechaelaboracion"));
				dto.setHoraDevolucion(rs.getString("horadevolucion"));
				dto.setUsuarioDevolucion(rs.getString("cajero"));
				dto.setMotivo(rs.getInt("motivodevol"));
				dto.setNombreMotivo(rs.getString("descripcionmotdevol"));
				dto.setNumeroRC(rs.getString("numerorc"));
				dto.setValorDevolucion(new BigDecimal(rs.getString("valordevol")));
				listadoDevoluciones.add(dto);
			}
			
			UtilidadBD.cerrarObjetosPersistencia(ps, rs, con);
			
		}
		catch (SQLException e) 
		{
			logger.info("ERROR AL EJECUTAR LA CONSULTA DEL DETALLE DE LAS DEVOLUCIONES "+e);
			e.printStackTrace();
		}
		return listadoDevoluciones;
	}
	
}
