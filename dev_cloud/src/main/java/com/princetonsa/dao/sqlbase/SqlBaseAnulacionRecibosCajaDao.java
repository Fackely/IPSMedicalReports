/*
 * Created on 3/10/2005
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
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.Utilidades;

/**
 * @version 1.0, 3/10/2005
 * @author <a href="mailto:armando@PrincetonSA.com">Jorge Armando Osorio Velásquez/a>
 */
public class SqlBaseAnulacionRecibosCajaDao
{

    /**
     * Manejador de log de errores
     */
    private static Logger logger=Logger.getLogger(SqlBaseAnulacionRecibosCajaDao.class);
    
    /**
     * Consulta para cargar un recibo caja especifico.
     */
    private static String cargarReciboCaja="select " +
    		"rc.fecha as fecharecibocaja," +
    		"rc.hora as horarecibocaja," +
    		"rc.caja as consecutivocaja," +
    		"c.codigo as codigocaja," +
    		"c.descripcion as descripcioncaja," +
    		"rc.usuario as loginusuario," +
    		"getnombreusuario(rc.usuario) as nombreusuario," +
    		"rc.recibido_de as recibidode, " +
    		"c.centro_atencion as codigocentroatencion, " +
    		"getnomcentroatencion(c.centro_atencion) AS nombrecentroatencion," +
    		//*******************************************************************
    		// cambio anexo 762
    		"case " +
			"  when cit.codigo_tipo_ingreso = "+ConstantesBD.codigoTipoIngresoTesoreriaPacientes+" and (fg.tipo_detalle = "+ConstantesBD.codigoTipoDetalleFormasPagoLetra+" OR fg.tipo_detalle = "+ConstantesBD.codigoTipoDetalleFormasPagoPagare+") then " + 
			"    ( " +
			"      select " + 
			"      CASE WHEN apcp.codigo_pk is not null then '"+ConstantesBD.acronimoNo+"' else dg.ingreso||'-'||dg.tipo_documento||'-'|| dg.anio_consecutivo||'-'|| dg.consecutivo end as is_elem " +
			"      from carterapaciente.datos_financiacion df " +
			"      inner join carterapaciente.documentos_garantia dg ON " + 
			"      ( " +
			"        df.codigo_pk_docgarantia=dg.codigo_pk " +
			"      ) " +
			"      left outer join carterapaciente.aplicac_pagos_cartera_pac apcp ON (apcp.datos_financiacion = df.codigo_pk) " +
			"      where df.detalle_pago_rc = dp.consecutivo " +
			"    ) " +
			"   when cit.codigo_tipo_ingreso = "+ConstantesBD.codigoTipoIngresoTesoreriaCarteraParticular+"  then " +
			"    ( " +
			"      select " + 
			"      case when count(apcp.codigo_pk) > 0 then '"+ConstantesBD.acronimoNo+"' else '"+ConstantesBD.acronimoSi+"' end as is_elem " +
			"      from carterapaciente.aplicac_pagos_cartera_pac apcp " +
			"      where apcp.numero_documento = ? " +
			"      and apcp.tipo_documento = "+ConstantesBD.codigoTipoDocumentoPagosReciboCaja+" " +
			"    ) " + 
			"  else  " +
			"    '"+ConstantesBD.acronimoSi+"'  " +
			"  end as is_eliminar_rc, " +
			// fin cambio anexo 762
			//*******************************************************************
    		"ma.ingreso AS ingreso " +
    		"FROM tesoreria.recibos_caja rc " + 
			"INNER JOIN tesoreria.cajas_cajeros cc ON (rc.usuario=cc.usuario AND rc.caja  =cc.caja AND rc.institucion =cc.institucion) " +
			"INNER JOIN tesoreria.cajas c   ON (cc.caja = c.consecutivo) " +
			"INNER JOIN tesoreria.detalle_conceptos_rc dc ON (dc.numero_recibo_caja = rc.numero_recibo_caja and rc.institucion = dc.institucion ) " +
			"INNER JOIN tesoreria.conceptos_ing_tesoreria cit ON (cit.codigo = dc.concepto) " +
			"INNER JOIN tesoreria.detalle_pagos_rc dp ON (dp.numero_recibo_caja = rc.numero_recibo_caja and rc.institucion = dp.institucion) " +
			"INNER JOIN tesoreria.formas_pago fg ON (fg.consecutivo = dp.forma_pago ) " +
			"LEFT OUTER JOIN tesoreria.movimientos_abonos ma ON(rc.numero_recibo_caja=ma.codigo_documento||'' AND ma.tipo="+ConstantesBD.tipoMovimientoAbonoIngresoReciboCaja+")" +
			"LEFT OUTER JOIN manejopaciente.ingresos ing ON (ing.id=ma.ingreso) " +
			"WHERE rc.numero_recibo_caja  = ? " +
			"AND rc.institucion           = ? ";
    
    /**
     * Consulta para cargar los conceptos de un recibo de caja
     */
    private static String consultarConceptosRecibosCaja="SELECT crc.consecutivo as consecutivoconceptorc,crc.concepto as codigoconceptorc,cit.descripcion as descripcionconceptorc,cit.codigo_tipo_ingreso as tipoconceptorc,cit.valor as valortipoconceptorc,crc.doc_soporte as docsoporteconceptorc,crc.valor as valorconceptorc,crc.tipo_id_beneficiario as tipoidbenefeciario,crc.numero_id_beneficiario as numeroidbeneficiario,crc.nombre_beneficiario as nombrebeneficiario from detalle_conceptos_rc crc inner join conceptos_ing_tesoreria cit on (crc.concepto=cit.codigo and crc.institucion=crc.institucion) where crc.numero_recibo_caja =? and crc.institucion = ?";
    
    /**
     * cadena utilizada para el ingreso de un registro de anulacion de RC.
     */
    private static String ingresarAnulacionRC="INSERT INTO anulacion_recibos_caja (numero_anulacion_rc,consecutivo_anulacion,numero_recibo_caja,institucion,usuario,fecha,hora,motivo_anulacion,observaciones,centro_atencion) values (?,?,?,?,?,?,?,?,?,?)";
    
    /**
     * cadena para consultar los pagos realizado en un recibo de caja
     */
    private static String consultarPagosFacturaPacienteReciboCaja="SELECT codigo as codigo from pagos_facturas_paciente where tipo_doc=? and documento=? and institucion=?";
    
    /**
     * Cadena para consultar los pagos realizados por una empresa en un recibo de caja
     */
    private static String consultarPagosEmpresaReciboCaja="SELECT codigo as codigo from pagos_general_empresa where tipo_doc=? and documento=? and institucion=?";
    
    /**
     * Cadena para consultar los codigos de los abono de un recibo de caja;
     */
    private static String consultarAbonosCaja="select codigo from movimientos_abonos where tipo=? and codigo_documento=? and institucion=?";
    
    /**
     * Cadena para consultar los codigos de todos los abonos de un paciente.
     */
    private static String consultarTodosAbonosCaja="SELECT codigo from movimientos_abonos where paciente=?";
    
    /**
     * Cadena para consultar la operacion y el valor de un abono
     */
    private static String consulatarAbonoValorOperacion="SELECT ma.valor as valor,tma.operacion as operacion from movimientos_abonos ma inner join tipos_mov_abonos tma on (ma.tipo=tma.codigo) where ma.codigo=?";
    
    /**
     * Cadena para la Consulta Pagos General Empresa
     */
    private static String consultaPagosGeneralEmpresa = "SELECT " +
    		"pge.codigo AS codigo_pge, " +
    		"pge.valor AS valor_aplicado, " +
    		"apfv.codigo AS codigo_apfv  " +
    		"FROM pagos_general_empresa pge " +
    		"INNER JOIN aplicacion_pagos_fvarias apfv ON (apfv.pagos_general_fvarias = pge.codigo ) " +
    		"WHERE pge.tipo_doc = ? " +
    		"AND pge.institucion = ? " +
    		"AND pge.documento = ? ";
    /**
     * actualiza el estado del pago general empres
     */
    private static String actualizarEstadoPagGenEmp = "UPDATE pagos_general_empresa SET estado = ? WHERE codigo = ? ";
    
    /**
     * actualiza el estado aplicacion pagos factura varia
     */
    private static String actualizarEstadoAplPagFacVar = "UPDATE aplicacion_pagos_fvarias SET estado = ? WHERE codigo = ? ";
    
    /**
     * cadena de consulta la factura varia con el valor de pago
     */
    private static String consutlaFacturaVaria = "SELECT " +
    		"apfv.factura AS cod_factura_varia, " +
    		"fv.valor_pagos AS valor_pagos ,  " +
    		"cfv.tipo_concepto AS tipo_concepto " +
    		"FROM aplicac_pagos_factura_fvarias apfv " +
    		"INNER JOIN facturas_varias fv ON (fv.codigo_fac_var = apfv.factura) " +
    		"INNER JOIN conceptos_facturas_varias cfv ON (cfv.consecutivo = fv.concepto)  "+
    		"WHERE apfv.aplicacion_pagos = ? "; 
    
    /**
     * cadena consulta los consecutivos de las multas de cita si las posee
     */
    private static String consultaConsecutivosMultaCita = "SELECT " +
    		"mc.consecutivo AS consecutivo, " +
    		"mc.estado AS estado " +
    		"FROM multas_facturas_varias mfv " +
    		"INNER JOIN multas_citas mc ON (mfv.multa_cita = mc.consecutivo AND mc.estado = ? ) " +
    		"WHERE mfv.factura_varia = ? ";
    
    /**
     * actualizar el estado de las multas de citas asociada a una factura varia
     */
    private static String actualizarEstadoMultaCita = "UPDATE multas_citas SET estado = ? WHERE consecutivo = ? ";
    
    /**
     * actualizar valor pagos de la facatura varia
     */
    private static String actualizarValorPagosFacVaria = "UPDATE facturas_varias SET valor_pagos = ? WHERE codigo_fac_var = ? ";
    
    /**
     * 
     */
    private static String actualizarValorValorAntiRecConvenio= "UPDATE facturacion.control_anticipos_contrato SET valor_ant_rec_convenio = ?,  usuario_modifica=?, fecha_modifica=?, hora_modifica=? WHERE contrato = ? ";
    
    /**
     * Consultar valor Anticipo Disponible del Contrato asociado a Convenio de un recibo de caja
     */
    private static String valorAnticipoDispoContratoConvenioReciboCaja = "SELECT " +
    		              "(coalesce(valor_ant_rec_convenio,0) - coalesce(valor_ant_res_pre_cont_pac,0))  AS valor, " +
    		              "valor_ant_rec_convenio AS valorantconv " +
    		              "FROM facturacion.control_anticipos_contrato " +
    		              "WHERE contrato = ? " ;
    		              
    
    /**
     * Cadena Sql para validar si Un recibo de caja es generado por concepto Ingreso de tipo Anticipos Convenios Odontologia
     */
    private static String validarConceptIngresoAnticConvenioOdon= "SELECT " +
    		"rc.numero_recibo_caja AS numrecibocaja, " +
    		"cont.codigo AS codcontrato " +
            "FROM tesoreria.recibos_caja rc " +   
            "INNER JOIN tesoreria.detalle_conceptos_rc  detconcrc ON (detconcrc.numero_recibo_caja = rc.numero_recibo_caja) " +
            "INNER JOIN cartera.pagos_general_empresa pagemp ON (pagemp.documento = detconcrc.numero_recibo_caja AND pagemp.tipo_doc = "+ ConstantesBD.codigoTipoDocumentoPagosReciboCaja +") " +
            "INNER JOIN facturacion.convenios conv ON (conv.codigo =  pagemp.convenio AND conv.tipo_atencion = '"+ConstantesIntegridadDominio.acronimoTipoAtencionConvenioOdontologico+"' ) " +
            "INNER JOIN facturacion.contratos cont ON (cont.codigo = pagemp.contrato AND cont.controla_anticipos = '"+ConstantesBD.acronimoSi+"') " +           
            "INNER JOIN tesoreria.conceptos_ing_tesoreria coningtes ON (coningtes.codigo = detconcrc.concepto  AND  coningtes.codigo_tipo_ingreso = "+ ConstantesBD.codigoTipoIngresoTesoreriaAnticipoConvenioOdon+") " +
            "WHERE rc.numero_recibo_caja = ? " ;   
             
    
    
    
   
    
    
    
    /**
     * @param con
     * @param numeroReciboCaja
     * @param institucion
     * @return
     */
    public static ResultSetDecorator cargarReciboCaja(Connection con, String numeroReciboCaja, int institucion)
    {
        try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con, cargarReciboCaja);
			ps.setString(1,numeroReciboCaja);
			ps.setString(2,numeroReciboCaja);
			ps.setInt(3,institucion);
			return new ResultSetDecorator(ps.executeQuery());
			
		}
		catch(SQLException e)
		{
			logger.error("Error cargano ReciboCaja [SqlBaseAnulacionReciboCajaDao]"+e);
		}
		return null;
    }



    
    /**
     * @param con
     * @param numeroReciboCaja
     * @param institucion
     * @return
     */
    public static ResultSetDecorator consultarDetalleConceptosRecibosCaja(Connection con, String numeroReciboCaja, int institucion)
    {
        try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consultarConceptosRecibosCaja,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1,numeroReciboCaja);
			ps.setInt(2,institucion);
			return new ResultSetDecorator(ps.executeQuery());
			
		}
		catch(SQLException e)
		{
		    logger.error("Error cargano los conceptos del ReciboCaja [SqlBaseAnulacionReciboCajaDao]"+e);
		}
		return null;
    }


    /**
     * @param con
     * @param numeroAnulacionReciboCaja
     * @param numeroReciboCaja
     * @param institucion
     * @param fechaAnulacion
     * @param horaAnulacion
     * @param usuario
     * @param motivo
     * @param observaciones
     * @param codigoCentroAtencion 
     * @return
     */
    public static int ingresarAnulacionReciboCaja(Connection con, String numeroAnulacionReciboCaja, String numeroReciboCaja, int institucion, String fechaAnulacion, String horaAnulacion, String usuario, int motivo, String observaciones, int codigoCentroAtencion)
    {
    	int codigo=ConstantesBD.codigoNuncaValido;
        try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(ingresarAnulacionRC,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			codigo=UtilidadBD.obtenerSiguienteValorSecuencia(con, "tesoreria.seq_anul_recibos_caja");
			ps.setString(1,codigo+"");
			ps.setString(2,numeroAnulacionReciboCaja);
			ps.setString(3,numeroReciboCaja);
			ps.setInt(4,institucion);
			ps.setString(5,usuario);
			ps.setDate(6,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(fechaAnulacion)));
			ps.setString(7,horaAnulacion);
			ps.setInt(8,motivo);
			ps.setString(9,observaciones);
			ps.setInt(10, codigoCentroAtencion);
			ps.executeUpdate();
			ps.close();
		}
		catch(SQLException e)
		{
			codigo=ConstantesBD.codigoNuncaValido;
		    logger.error("Error insertando la anulacion del  ReciboCaja [SqlBaseAnulacionReciboCajaDao]"+e);
		}
		return codigo;
    }




    
    /**
     * @param con
     * @param codigoTipoDocumento
     * @param numeroReciboCaja
     * @param institucion
     * @return
     */
    public static ResultSetDecorator consultarPagosFacturaPacienteReciboCaja(Connection con, int codigoTipoDocumento, String numeroReciboCaja, int institucion)
    {
        try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consultarPagosFacturaPacienteReciboCaja,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1,codigoTipoDocumento);
			ps.setString(2,numeroReciboCaja);
			ps.setInt(3,institucion);
			return new ResultSetDecorator(ps.executeQuery());
			
		}
		catch(SQLException e)
		{
		    logger.error("Error  [SqlBaseAnulacionReciboCajaDao]"+e);
		}
		return null;
    }




    
    /**
     * @param con
     * @param codigoTipoDocumento
     * @param numeroReciboCaja
     * @param institucion
     * @return
     */
    public static ResultSetDecorator consultarPagosEmpresaReciboCaja(Connection con, int codigoTipoDocumento, String numeroReciboCaja, int institucion)
    {
        try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consultarPagosEmpresaReciboCaja,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1,codigoTipoDocumento);
			ps.setString(2,numeroReciboCaja);
			ps.setInt(3,institucion);
			return new ResultSetDecorator(ps.executeQuery());
			
		}
		catch(SQLException e)
		{
		    logger.error("Error  [SqlBaseAnulacionReciboCajaDao]"+e);
		}
		return null;
    }




    
    /**
     * @param con
     * @param codigoTipoDocumento
     * @param numeroReciboCaja
     * @param institucion
     * @return
     */
    public static ResultSetDecorator consultarAbonosReciboCaja(Connection con, int codigoTipoDocumento, String numeroReciboCaja, int institucion)
    {
        try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consultarAbonosCaja,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1,codigoTipoDocumento);
			ps.setString(2,numeroReciboCaja);
			ps.setInt(3,institucion);
			return new ResultSetDecorator(ps.executeQuery());
			
		}
		catch(SQLException e)
		{
		    logger.error("Error  [SqlBaseAnulacionReciboCajaDao]"+e);
		}
		return null;
    }




    
    /**
     * @param con
     * @param numeroAnulacionReciboCaja
     * @param tipoMovimiento
     * @param fecha
     * @param hora
     * @param codigos
     * @param cadena
     * @return
     */
    public static boolean generarAbonos(Connection con, String numeroAnulacionReciboCaja, int tipoMovimiento, String fecha, String hora, String[] codigos, String cadena)
    {
        String subConsulta="";
        for(int i=0;i<codigos.length;i++)
        {
            if(i==0)
                subConsulta=subConsulta+codigos[i];
            else
                subConsulta=subConsulta+","+codigos[i];
        }
        String cadenaGenerarAbonos=cadena+" ("+subConsulta+"))";
        try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaGenerarAbonos,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1,numeroAnulacionReciboCaja);
			ps.setInt(2,tipoMovimiento);
			ps.setDate(3,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(fecha)));
			ps.setString(4,hora);
			return (ps.executeUpdate()>0);
		}
		catch(SQLException e)
		{
		    logger.error("Error generando abonos [SqlBaseAnulacionReciboCajaDao]"+e);
		}
		return false;
    }




    
    /**
     * @param con
     * @param codigoAbono
     * @return
     */
    public static ResultSetDecorator consultarValorOperacionAbono(Connection con, int codigoAbono)
    {
        try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consulatarAbonoValorOperacion,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1,codigoAbono);
			return new ResultSetDecorator(ps.executeQuery());
			
		}
		catch(SQLException e)
		{
		    logger.error("Error  [SqlBaseAnulacionReciboCajaDao]"+e);
		}
		return null;
    }




    
    /**
     * @param con
     * @param paciente
     * @param ingreso Se env&iacute;a null en caso de no querer filtrar por ingreso 
     * @return
     */
    public static ResultSetDecorator consultarTodosAbonosReciboCaja(Connection con, int paciente, Integer ingreso)
    {
        try
		{
        	String sentenciaSql=consultarTodosAbonosCaja;
        	if(ingreso!=null)
        	{
        		sentenciaSql+=" AND ingreso=?";
        	}
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consultarTodosAbonosCaja,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1,paciente);
        	if(ingreso!=null)
        	{
        		ps.setInt(2, ingreso);
        	}
			return new ResultSetDecorator(ps.executeQuery());
			
		}
		catch(SQLException e)
		{
		    logger.error("Error  [SqlBaseAnulacionReciboCajaDao]"+e);
		}
		return null;
    }
    
    /**
     * Consulta el pago general de empresa
     * @param Connection con
     * @param HashMap parametros
     * @return HashMap<String,Object>
     */
    public static HashMap<String,Object> consultarPagoGeneralEmpresa(Connection con, HashMap parametros)
    {
    	HashMap<String,Object> datos = new HashMap<String,Object>();
        try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con, consultaPagosGeneralEmpresa);
			ps.setInt(1,Utilidades.convertirAEntero(parametros.get("tipo_doc").toString()));
			ps.setInt(2,Utilidades.convertirAEntero(parametros.get("institucion").toString()));
			ps.setString(3, parametros.get("documento").toString());
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			if(rs.next()){
				datos.put("codigo_pge",rs.getObject("codigo_pge"));
				datos.put("valor_aplicacion",rs.getObject("valor_aplicado"));
				datos.put("codigo_apfv",rs.getObject("codigo_apfv"));
			}
			return datos;
		}
		catch(SQLException e)
		{
			logger.info("Error en la Consulta >>>>> "+consultaPagosGeneralEmpresa);
			logger.info("tipo documento >>>>> "+parametros.get("tipo_doc"));
			logger.info("institucion >>>>> "+parametros.get("institucion"));
			logger.info("documento >>>>> "+parametros.get("documento"));
		    logger.error("Error [SqlBaseAnulacionReciboCajaDao]"+e);
		}
		return datos;
    }

    /**
     * Actualizar estado pagos genral empresa
     * @param con
     * @param HashMap
     * @return boolean
     */
    public static boolean actualizarEstadoPagGenEmp(Connection con, HashMap parametros)
    {
        try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(actualizarEstadoPagGenEmp,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, Utilidades.convertirAEntero(parametros.get("estado").toString()));
			ps.setInt(2, Utilidades.convertirAEntero(parametros.get("codigo_pge").toString()));
			return (ps.executeUpdate()>0);
		}
		catch(SQLException e)
		{
		    logger.error("Error actualizando Estado pago general empresa [SqlBaseAnulacionReciboCajaDao]"+e);
		}
		return false;
    }
    
    /**
     * Actualizar estado pagos genral empresa
     * @param con
     * @param HashMap
     * @return boolean
     */
    public static boolean actualizarEstadoAplPagFacVar(Connection con, HashMap parametros)
    {
        try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(actualizarEstadoAplPagFacVar,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, Utilidades.convertirAEntero(parametros.get("estado").toString()));
			ps.setInt(2, Utilidades.convertirAEntero(parametros.get("codigo_apfv").toString()));
			return (ps.executeUpdate()>0);
		}
		catch(SQLException e)
		{
		    logger.error("Error actualizando Estado Aplicacin pagos facturas varias [SqlBaseAnulacionReciboCajaDao]"+e);
		}
		return false;
    }
    
    /**
     * Consulta la Factura Varia
     * @param Connection con
     * @param HashMap parametros
     * @return HashMap<String,Object>
     */
    public static HashMap<String,Object> consultarFacturaVaria(Connection con, HashMap parametros)
    {
    	
    	logger.info("*************************************************CONSULTA FACT VARIAS*********************************");
    	HashMap<String,Object> datos = new HashMap<String,Object>();
        try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consutlaFacturaVaria,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1,Utilidades.convertirAEntero(parametros.get("codigo_apfv").toString()));
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			if(rs.next()){
				datos.put("cod_factura_varia",rs.getObject("cod_factura_varia"));
				datos.put("valor_pagos",rs.getObject("valor_pagos"));
				datos.put("tipo_concepto", rs.getObject("tipo_concepto"));
			}
			Utilidades.imprimirMapa(datos);
			return datos;
		}
		catch(SQLException e)
		{
			logger.info("Error en la Consulta >>>>> "+consutlaFacturaVaria);
			logger.info("codigo Aplicacion pagos Factura Varia >>>>> "+parametros.get("codigo_apfv"));
		    logger.error("Error [SqlBaseAnulacionReciboCajaDao]"+e);
		}
		return datos;
    }
    
    
    /**
     * Consulta los consecutivos de las multas asociadas a la factura varia
     * @param Connection con
     * @param HashMap parametros
     * @return HashMap<String,Object>
     */
    public static ArrayList<String> consultarConsecutivosMultaCitas(Connection con, HashMap parametros)
    {
    	ArrayList<String> array = new ArrayList<String>(); 
        try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consultaConsecutivosMultaCita,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1, parametros.get("estado").toString());
			ps.setInt(2,Utilidades.convertirAEntero(parametros.get("codigo_fv").toString()));
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
				array.add(rs.getObject("consecutivo").toString());
			return array;
		}
		catch(SQLException e)
		{
			logger.info("Error en la Consulta >>>>> "+consultaConsecutivosMultaCita);
			logger.info("estado >>>>> "+parametros.get("estado"));
			logger.info("codigo Factura Varia >>>>> "+parametros.get("codigo_fv"));
		    logger.error("Error [SqlBaseAnulacionReciboCajaDao]"+e);
		}
		return array;
    }
    
    /**
     * actualizar el estado de las multas de citas asociada a una factura varia
     * @param con
     * @param HashMap
     * @return boolean
     */
    public static boolean actualizarEstadoMultaCita(Connection con, HashMap parametros)
    {
        try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(actualizarEstadoMultaCita,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1, parametros.get("estado").toString());
			ps.setInt(2, Utilidades.convertirAEntero(parametros.get("codigo_fv").toString()));
			return (ps.executeUpdate()>0);
		}
		catch(SQLException e)
		{
		    logger.error("Error Actualizando Estado Multa [SqlBaseAnulacionReciboCajaDao]"+e);
		}
		return false;
    }
    
    /**
     * actualizar el estado de las multas de citas asociada a una factura varia
     * @param con
     * @param HashMap
     * @return boolean
     */
    public static boolean actualizarValorPagoFacVaria(Connection con, HashMap parametros)
    {
        try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(actualizarValorPagosFacVaria,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setDouble(1, Utilidades.convertirADouble(parametros.get("valor_pago").toString()));
			ps.setInt(2, Utilidades.convertirAEntero(parametros.get("codigo_fv").toString()));
			return (ps.executeUpdate()>0);
		}
		catch(SQLException e)
		{
		    logger.error("Error Actualizando Estado Multa [SqlBaseAnulacionReciboCajaDao]"+e);
		}
		return false;
    }
    
    
    
    /**
     * Metodo para validar si Un recibo de caja es generado por concepto Ingreso de tipo Anticipos Convenios Odontologia
     * @param con
     * @param numeroReciboCaja
     * @return
     */
    public static ResultSetDecorator esReciboCajaXConceptAnticipConvenioOdonto(Connection con, String numeroReciboCaja)
    {
    	
    	 String cadena = validarConceptIngresoAnticConvenioOdon;
    	    
         try
    		{
             PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
    	     ps.setString(1,numeroReciboCaja);
    	     logger.info("CADENA CONSULTA Validacion esReciboCajaXConceptAnticipConvenioOdonto >> "+cadena +" numRecibo Caja >> "+numeroReciboCaja);
    	     
    	     return new ResultSetDecorator(ps.executeQuery());
    			     
    		}
    		catch(SQLException e)
    		{
    			logger.error("Error validando  Convenio Recibo Caja [SqlBaseAnulacionReciboCajaDao]"+e);
    			logger.info("CADENA CONSULTA >> "+cadena +" numRecibo Caja >> "+numeroReciboCaja);
    		}
        
    		return null;	
    }
    
    
    /**
     * Metodo para consultar el valor de anticipo Disponible de un contrato Asociado a un Convenio de un recibo de caja
     * @param con
     * @param numeroReciboCaja
     * @return
     */
    public static ResultSetDecorator obtenerValorAnticipoDipoContratConveRecibCaja(Connection con, int numeroContrato)
    {
    
     String cadena = valorAnticipoDispoContratoConvenioReciboCaja;
    
     try
		{
         PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	     ps.setInt(1,numeroContrato);
	     logger.info("CADENA CONSULTA  ValorAnticipoDipoContratConveRecibCaja>> "+cadena +" numContrato >> "+numeroContrato);
	     return new ResultSetDecorator(ps.executeQuery());
	     
		}
		catch(SQLException e)
		{
			logger.error("Error consultando Valor Anticipo Diponible Convenio Recibo Caja [SqlBaseAnulacionReciboCajaDao]"+e);
			logger.info("CADENA CONSULTA >> "+cadena +" numContrato >> "+numeroContrato);
		}
     
		return null;
     
    }
    
    
    /**
     * Metodo para Actualizar el valor Anticipo Contrato
     * @param con
     * @param codContrato
     * @return
     */
    public static boolean actualizarValorAnticipoContrato(Connection con , int codContrato , double valorAntRecConvenio, String usuario)
    {
    	boolean retorna= false;
    	String cadenaUdateValorRecConvenio= actualizarValorValorAntiRecConvenio;
    	try
    	{
        	PreparedStatementDecorator ps1= new PreparedStatementDecorator(con , cadenaUdateValorRecConvenio);
        	ps1.setDouble(1,valorAntRecConvenio);
        	ps1.setString(2, usuario);
			ps1.setDate(3, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual(con))));
			ps1.setString(4, UtilidadFecha.getHoraActual(con));
        	ps1.setInt(5, codContrato);
        	
        	Log4JManager.info("Consulta Actualizar Valor Anticipos SQL->"+ps1);
        	
        	retorna= ps1.executeUpdate()>0;
        	ps1.close();
    	}
    	catch(SQLException e)
	    {
		  logger.error("Error Actualizando Valor Anticipo Recibido del Convenio [SqlBaseAnulacionReciboCajaDao]"+e);
		}
 		return retorna;
    }
    
}