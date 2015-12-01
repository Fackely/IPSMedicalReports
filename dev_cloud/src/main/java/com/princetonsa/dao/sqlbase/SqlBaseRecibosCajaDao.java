
/*
 * Creado   28/09/2005
 *
 *Copyright Princeton S.A. &copy;&reg; 2004. Todos los Derechos Reservados.
 *
 * Lenguaje		:Java
 * Compilador	:J2SDK 1.5.0_03
 */
package com.princetonsa.dao.sqlbase;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.FacturasVarias.UtilidadesFacturasVarias;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.facturasVarias.DtoRecibosCaja;
import com.princetonsa.enu.administracion.EmunTiposIdentificacion;


/**
 * Implementaci�n sql gen�rico de todas las funciones de acceso a la fuente de datos
 * para un recibo de caja
 *
 * @version 1.0, 28/09/2005
 * @author <a href="mailto:joan@PrincetonSA.com">Joan L&oacute;pez</a>
 */
public class SqlBaseRecibosCajaDao 
{

    /**
	* Objeto para manejar los logs de esta clase
	*/
	public static Logger logger = Logger.getLogger(SqlBaseRecibosCajaDao.class);
    /**
     * query para la consulta de facturas
     */
    private static final String consultaFacturas="SELECT " +
						    					 "getnombreviaingreso(f.via_ingreso) as nombre_via_ingreso,"+
							    				 "f.consecutivo_factura as consecutivo_factura," +
							    				 "to_char(f.fecha,'dd/mm/yyyy') as fecha," +
							    				 "f.codigo as codigo_factura,"+
							    				 "administracion.getnombremedico(f.cod_paciente) as nombre_paciente," +
							    				 "f.valor_neto_paciente as valor_neto_paciente," +
							    				 "p.numero_identificacion as numero_identificacion, " +
							    				 "p.tipo_identificacion as tipo_identificacion, " +
							    				 "getnomcentroatencion(f.centro_aten) AS nombre_centro_atencion, ";
  /**
   *query para la consulta de pacientes
   */
    private static final String consultaPaciente="SELECT " +
							 "p.codigo as codigo_paciente," +
							 "p.primer_nombre as primer_nombre," +
							 "p.segundo_nombre as segundo_nombre," +
							 "p.primer_apellido as primer_apellido," +
							 "p.segundo_apellido as segundo_apellido," +
							 "p.tipo_identificacion as tipo_identificacion," +
							 "p.numero_identificacion as numero_identificacion ";
			    		
    /**
     * query para consultar el tipo detalle de formas de pago
     */
    private static final String consultaFormasPagosStr="SELECT tipo_detalle as tipo_detalle FROM formas_pago WHERE consecutivo=?";
    /**
     * query para insertar en la tabla de recibos_caja
     */
    private static final String insertarReciboCajaStr="INSERT " +
										    		  "INTO recibos_caja " +
										    		  "(numero_recibo_caja,consecutivo_recibo,institucion,usuario,caja,fecha,hora,estado,recibido_de,observaciones,centro_atencion) " +
										    		  "VALUES (?,?,?,?,?,?,?,?,?,?,?)";    
    /**
     * query para consultar el �ltimo recibo de caja insertado
     */
    private static final String ultimoCodigoReciboCajaStr="SELECT MAX(convertiranumero(numero_recibo_caja||'')) AS secuencia from recibos_caja where institucion=?";
    /**
     * query para consultar el �ltimo c�digo insertado de detalle pagos
     */
    private static final String ultimoCodigoDetallePagosStr="SELECT max(consecutivo) as consecutivo from detalle_pagos_rc where institucion=?";
    /**
     * query para actualizar el documento de conceptos
     */
    private static final String actualizarDocumentoDetalleConceptosStr="UPDATE detalle_conceptos_rc SET doc_soporte=? WHERE consecutivo=? AND institucion=?";
    /**
     * query para actualizar el estado paciente de la factura a cancelado
     */
    private static final String actualizarEstadoPacienteFacturaStr="UPDATE facturas SET estado_paciente="+ConstantesBD.codigoEstadoFacturacionPacienteCancelada+
    															   " WHERE institucion=? AND codigo=?";
    /**
     * query para consultar el ultimo movimiento de abonos insertando
     */
    private static final String ultimoCodigoMovimientoAbonosStr="SELECT max(codigo) as consecutivo FROM movimientos_abonos WHERE institucion=?";
    /**
     * query para consultar el ultimo codigo de detalle de conceptos
     */
    private static final String ultimoCodigoDetalleConceptosStr="SELECT max(consecutivo) as consecutivo FROM detalle_conceptos_rc WHERE institucion=?";
    
    /**
     * Cadena para acturalizar el estado de un recibo de caja
     */
    private static String cadenaActualizacionEstadoRC="UPDATE recibos_caja SET estado = ? where numero_recibo_caja=? and institucion=?";
    
    /**
     * cadena que se encarga de verificar si un numero de recibo de caja
     * esta o no registrado en aplicacion de pagos  de facturas varias.
     */
    private static String strVerificaReciboEnAplicaPagFacVarias =" SELECT count (*) FROM pagos_general_empresa pge " +
    																	"				INNER JOIN aplicacion_pagos_fvarias apf ON (apf.pagos_general_fvarias=pge.codigo AND apf.estado != "+ConstantesBD.codigoEstadoPagosAplicacionesAnulado+" )" +
    																	"               WHERE pge.documento=? AND pge.institucion=? AND pge.tipo_doc = "+ConstantesBD.codigoTipoDocumentoPagosReciboCaja+" ";
    
    
    /**
     * cadena que verifica si el concepto de la factura varia posee asociadas multas
     */
    private static String strVerificarTipoConcepto ="SELECT " +
    	"CASE WHEN cfv.tipo_concepto IS NULL THEN 'ANIN' ELSE cfv.tipo_concepto END AS tipo_concepto " +
    	"FROM conceptos_facturas_varias cfv " +
    	"WHERE cfv.consecutivo = ? ";
    	
    /**
     * actualizar deudor de pagos gneral empresa
     */
    private static String strActualizarDeudorPagGenEmp = "UPDATE pagos_general_empresa " +
    	"SET deudor = ? " +
    	"WHERE codigo = ? ";
    
    /**
     * cadena que consulta el valor del concepto de ingreso de tesoreria
     */
    private static String strValorConceptoIngresoTesoreria = "SELECT " +
    	"valor AS valor " +
    	"FROM conceptos_ing_tesoreria " +
    	"WHERE codigo = ? ";
    
    
    
    private static String strActualizarEstadoTarjeta = "UPDATE odontologia.beneficiario_tarjeta_cliente SET " +
    		"estado_tarjeta= '"+ConstantesIntegridadDominio.acronimoEstadoActivo+"' " +
    		"WHERE venta_tarjeta_cliente = ?";
    
    
    
    
    private static String strConsultarCodigoBeneficiario = "SELECT codigo_pk AS codPk FROM odontologia.venta_tarjeta_cliente WHERE codigo_fac_var = ? ";
    
    
    
    private static String strActualizarValorAnticipoREcibidoConvenio = "UPDATE " +
    																		"facturacion.control_anticipos_contrato " +
    																	"SET " +
    																		"valor_ant_rec_convenio = (coalesce(valor_ant_rec_convenio,0)+?), " +
    																		"usuario_modifica=?, " +
    																		"fecha_modifica=?, " +
    																		"hora_modifica=? " +
		"WHERE contrato = ? ";
    
    /**
     * Metodo encargado de verificar si un recibo de caja
     * se encuentra registrado en aplicacion de pagos de facturas varias.
     * @param connection
     * @param codigoResivoCaja
     * @param institucion
     * @return
     */
    public static boolean estaRegistradoEnAplicacionPagosFacturasVarias (Connection connection, String codigoResivoCaja,String institucion)
    {
    	logger.info("\n entra a estaRegistradoEnAplicacionPagosFacturasVarias codigo recibo caja --> "+codigoResivoCaja+" institucion --> "+institucion);
    	
    	String cadena = strVerificaReciboEnAplicaPagFacVarias;
    	
    	try 
    	{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			ps.setObject(1, codigoResivoCaja);
			ps.setObject(2, institucion);
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			if (rs.next())
				if (rs.getInt(1)>0)
					return true;
				else
					return false;
    		
		}
    	catch (Exception e) 
    	{
		logger.info("\n\n problema verificando existencia de recibo de caja en aplicacion de pagos de facturas varias "+e); 
		}
    	
    	
    	return false;
    }
    
    
    
    
    /**
     * metodo para realizar la consulta de facturas
     * @param con
     * @param consecutivoFact
     * @param fechaFact
     * @param numeroIdentif
     * @param tipoIdentif
     * @param institucion
     * @param valorConcepto
     * @return ResultSet
     * @see com.princetonsa.dao.RecibosCajaDao#generarConsultaFacturas(java.sql.Connection,int,String,String,String,int,String)
     * @author jarloc
     */
    public static ResultSetDecorator generarConsultaFacturas(Connection con,int consecutivoFact,
														            String fechaFact,String numeroIdentif,
														            String tipoIdentif,int institucion,
														            String valorConcepto)
    {
        boolean esPrimero = true;
        String query=consultaFacturas+"getPagosFacturaPaciente("+institucion+","+ConstantesBD.codigoEstadoPagosRecaudado+",f.codigo) AS pagosPaciente " +        			
        			"FROM facturas f "+
        			"INNER JOIN personas p ON(p.codigo=f.cod_paciente) " +
        			"WHERE ";
        try
	    {
	        PreparedStatementDecorator ps = null;	        
	        //no debe aplicar enviar el valor de concepto y restringir la via de ingreso
	        //es il�gico, tarea 98646
	        /*if(!valorConcepto.equals("null"))
	        {
	        	if(!valorConcepto.trim().equals(""))
	        	{	
	        		query+="f.via_ingreso="+valorConcepto;
		            esPrimero = false;
	        	}    
	        }*/
	        if(consecutivoFact!=ConstantesBD.codigoNuncaValido && consecutivoFact!=0)
	        {
	            if(!esPrimero)
	                query+=" AND ";
	            query+="f.consecutivo_factura='"+consecutivoFact+"'";
	            esPrimero = false;
	        }
	        if(!fechaFact.equals(""))
	        {
	            if(!esPrimero)
	                query+=" AND ";
	            query+="f.fecha='"+UtilidadFecha.conversionFormatoFechaABD(fechaFact)+"'";
	            esPrimero = false;
	        }
	        if(!numeroIdentif.equals("")||!tipoIdentif.equals(""))
	        {
	            boolean esPrimero2=true;
	            String subConsulta="SELECT codigo from personas where ";
	            if(!numeroIdentif.equals(""))
	            {
	                subConsulta+="numero_identificacion='"+numeroIdentif+"'";
	                esPrimero2=false;
	            }
	            if(!tipoIdentif.equals(""))
	            {
	                if(!esPrimero2)
	                    subConsulta+=" AND ";
	                subConsulta+="tipo_identificacion='"+tipoIdentif+"'";
	                esPrimero2 = false;  
	            }
	            if(!esPrimero)
	                query+=" AND ";            
	            query+="f.cod_paciente in("+subConsulta+")";
	            esPrimero = false;
	        }
	        if(institucion!=ConstantesBD.codigoNuncaValido)
	        {
	            if(!esPrimero)
	                query+=" AND ";
	            query+="f.institucion="+institucion;
	            esPrimero = false;
	        }
	        
	        if(!esPrimero)
                query+=" AND ";
            query+="f.estado_facturacion="+ConstantesBD.codigoEstadoFacturacionFacturada;
            esPrimero = false;
            
            if(!esPrimero)
                query+=" AND ";
            query+="f.estado_paciente="+ConstantesBD.codigoEstadoFacturacionPacientePorCobrar;
            esPrimero = false;
            
            query+=" order by f.consecutivo_factura asc";
            
            logger.info("\n\n\n CONSULTA FACTURAS-->"+query+"\n\n\n");
            
	        ps= new PreparedStatementDecorator(con.prepareStatement(query,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	        return new ResultSetDecorator(ps.executeQuery());
	    }catch(SQLException e)
	    {
	        logger.warn(e+"Error en generarConsultaFacturas"+e.toString() );
		   return null;
	    }
    }
    /**
     * metodo para generar la consulta de pacientes
     * @param con
     * @param primerNombre
     * @param segundoNombre
     * @param primerApellido
     * @param segundoApellido
     * @param tipoIdentificacion
     * @param numeroIdentificacion
     * @param institucion
     * @param controlarAbonoPaciente
     * @see com.princetonsa.dao.RecibosCajaDao#generarConsultaPacientes(java.sql.Connection,String,String,String,String,String,int)
     * @author jarloc
     * @return
     */
    public static ResultSetDecorator generarConsultaPacientes(Connection con,String primerNombre,
														            String segundoNombre,String primerApellido,
														            String segundoApellido,String tipoIdentificacion,
														            String numeroIdentificacion,int institucion, boolean controlarAbonoPaciente)
    {
        boolean esPrimero = true;
        String query=consultaPaciente;
        try
	    {
	        /*if(controlarAbonoPaciente)
	        {*/
	        	query+=", ing.id AS ingreso ";
	        /*}
	        else
	        {
	        	query+=", null AS ingreso ";
	        }*/
	        	
	        query+=
	        		"FROM personas p " +
	        		"INNER JOIN " +
	        			"pacientes_instituciones pi " +
	        				"ON (pi.codigo_paciente=p.codigo) ";
	        
	        if(controlarAbonoPaciente)
	        {
	        	query+="INNER JOIN " +
	        				"manejopaciente.ingresos ing " +
	        					"ON(ing.codigo_paciente=p.codigo) ";
	        }else
	        {
	        	query+="LEFT JOIN " +
							"manejopaciente.ingresos ing " +
								"ON(ing.codigo_paciente=p.codigo) ";
	        }
	        query+="WHERE ";
        	
	        PreparedStatementDecorator ps = null;	        
	        if(controlarAbonoPaciente)
	        {
	        	query+="ing.estado='"+ConstantesIntegridadDominio.acronimoEstadoAbierto+"' ";
	        	esPrimero=false;
	        }	        
	        if(primerNombre !=null && !"".equals(primerNombre))
	        {
	            if(!esPrimero)
	                query+=" AND ";
	            query+=" UPPER(p.primer_nombre) LIKE UPPER('%"+primerNombre+"%') ";
	            esPrimero = false;
	        }
	        if(segundoNombre !=null &&  !"".equals(segundoNombre))
	        {
	            if(!esPrimero)
	                query+=" AND ";
	            query+=" UPPER(p.segundo_nombre) LIKE UPPER('%"+segundoNombre+"%') ";
	            esPrimero = false;
	        }
	        if(primerApellido !=null &&  !"".equals(primerApellido))
	        {
	            if(!esPrimero)
	                query+=" AND ";
	            query+=" UPPER(p.primer_apellido) LIKE UPPER('%"+primerApellido+"%') ";
	            esPrimero = false;
	        }
	        if(segundoApellido !=null &&  !"".equals(segundoApellido))
	        {
	            if(!esPrimero)
	                query+=" AND ";
	            query+=" UPPER(p.segundo_apellido) LIKE UPPER('%"+segundoApellido+"%') ";
	            esPrimero = false;
	        }
	        if(tipoIdentificacion !=null &&  !"".equals(tipoIdentificacion))
	        {
	            if(!esPrimero)
	                query+=" AND ";
	            query+=" p.tipo_identificacion= '"+tipoIdentificacion+"'";
	            esPrimero = false;
	        }
	        if(numeroIdentificacion !=null &&  !"".equals(numeroIdentificacion))
	        {
	            if(!esPrimero)
	                query+=" AND ";
	            query+=" p.numero_identificacion= '"+numeroIdentificacion+"'";
	            esPrimero = false;
	        }
	        if(institucion!=ConstantesBD.codigoNuncaValido)
	        {
	            if(!esPrimero)
	                query+=" AND ";
	            query+="pi.codigo_institucion="+institucion;
	            esPrimero = false;
	        }
	        
	        query+=" ORDER BY primer_apellido,segundo_apellido,primer_nombre,segundo_nombre asc";
	        ps= new PreparedStatementDecorator(con,query);
	        logger.info(ps);
	        return new ResultSetDecorator(ps.executeQuery());
	    }
        catch(SQLException e)
	    {
	        logger.warn(e+"Error en generarConsultaFacturas"+e.toString() );
		   return null;
	    }
    }
    /**
     * Metodo para consultar el tipo detalle
     * de formas de pago 
     * @param con
     * @return
     * @see com.princetonsa.dao.RecibosCajaDao#generarConsultaFormaPago(java.sql.Connection,int)
     */    
    public static ResultSetDecorator generarConsultaFormaPago(Connection con,int consecutivo)
    {
        try
	    {
	        PreparedStatementDecorator ps = null;	        
	        if (con == null || con.isClosed()) 
			{
				DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
				con = myFactory.getConnection();
			}
	        ps= new PreparedStatementDecorator(con.prepareStatement(consultaFormasPagosStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	        ps.setInt(1,consecutivo);
	        return new ResultSetDecorator(ps.executeQuery());
	    }
        catch(SQLException e)
	    {
	        logger.warn(e+"Error en generarConsultaFormaPago"+e.toString() );
		   return null;
	    }
    }
    
    
    
    /**
     * metodo para realizar el insert de recibos de caja
     * @param con Connection
     * @param numeroReciboCaja String, consecutivo del recibo de caja
     * @param institucion int 
     * @param usuario String
     * @param caja String 
     * @param fecha String 
     * @param hora String 
     * @param recibidoDe String 
     * @param observaciones String
     * @param estadoRC 
     * @return true si es efectivo
     * @see com.princetonsa.dao.RecibosCajaDao#insertarReciboCaja(java.sql.Connection,String,int,String,String,String,String,String,String)
     */
    public static int  insertarReciboCaja(Connection con,
											            String numeroReciboCaja,int institucion,
											            String usuario,String caja,
											            String fecha,String hora,
											            String recibidoDe,String observaciones, int codigoCentroAtencion, int estadoRC)
    {
        int resp=0;
        int codigoRC=ConstantesBD.codigoNuncaValido;
        boolean inserto=false;
        try
	    {
	        PreparedStatementDecorator ps = null;	        
	        ps= new PreparedStatementDecorator(con, insertarReciboCajaStr);
	        codigoRC=UtilidadBD.obtenerSiguienteValorSecuencia(con, "tesoreria.seq_recibos_caja");
	        ps.setString(1,codigoRC+"");
	        ps.setString(2,numeroReciboCaja);
	        ps.setInt(3,institucion);
	        ps.setString(4,usuario);
	        ps.setInt(5,Integer.parseInt(caja));
	        ps.setString(6,fecha);
	        ps.setTime(7,Time.valueOf(hora+":00"));
	        if(estadoRC==ConstantesBD.codigoNuncaValido)
	        	ps.setInt(8,ConstantesBD.codigoEstadoReciboCajaRecaudado);
	        else
	        	ps.setInt(8,estadoRC);
	        ps.setString(9,recibidoDe);
	        ps.setString(10,observaciones);
	        ps.setInt(11, codigoCentroAtencion);
	        Log4JManager.info(ps);
	        resp=ps.executeUpdate();
	        if(resp>0){
	            inserto=true; 
	        }
	    }
        catch(SQLException e)
	    {
        	codigoRC=ConstantesBD.codigoNuncaValido;
	        logger.error("Error en insertarReciboCaja",e );		  
	    }
        return codigoRC;
    }
    /**
     * M�todo para insertar el detalle de pagos
     * 
     * @param con Connection     
     * @param numeroRecibo String 
     * @param institucion int
     * @param formaPago String 
     * @param valor String
     * @return true si es efectivo
     * @see com.princetonsa.dao.RecibosCajaDao#insertarDetallePagoRC(java.sql.Connection,String,int,String,String)
     */
    public static boolean insertarDetallePagoRC (Connection con,String numeroRecibo,int institucion,String formaPago,String valor,String query)
    {
        byte resp=0;
        boolean inserto=false;
        
        if(!UtilidadTexto.isEmpty(valor)){
        	
        	try
		    {
		        PreparedStatementDecorator ps = null;	        
		        ps= new PreparedStatementDecorator(con, query);	        
		        ps.setString(1,numeroRecibo);
		        ps.setInt(2,institucion);
		        ps.setInt(3,Integer.parseInt(formaPago));
		        ps.setDouble(4,Double.parseDouble(valor));
		        Log4JManager.info(ps);
		        resp=Byte.parseByte(ps.executeUpdate()+"");
		        if(resp>0){
		            inserto=true; 
		        }
		    }
	        catch(SQLException e)
		    {
		        logger.warn(e+"Error en insertarDetallePagoRC"+e.toString() );		  
		    }
        }

        return inserto;
    }    

    /**
     * metodo para insertar el detalle de pagos
     * @param con Connection     
     * @param codigoDetallePagoRC 
     * @param numeroRecibo String 
     * @param institucion int
     * @param formaPago String 
     * @param valor String
     * @return true si es efectivo
     * @see com.princetonsa.dao.RecibosCajaDao#insertarDetallePagoRC(java.sql.Connection,String,int,String,String)
     */
    public static boolean insertarDetallePagoRC (Connection con, int codigoDetallePagoRC, String numeroRecibo,int institucion,String formaPago,String valor,String query)
    {
    	byte resp=0;
        boolean inserto=false;
        try
	    {
	        PreparedStatementDecorator ps = null;	        
	         ps= new PreparedStatementDecorator(con, query);	        
	        ps.setInt(1,codigoDetallePagoRC);
	        ps.setString(2,numeroRecibo);
	        ps.setInt(3,institucion);
	        ps.setInt(4,Integer.parseInt(formaPago));
	        ps.setDouble(5,Double.parseDouble(valor));
	        resp=Byte.parseByte(ps.executeUpdate()+"");
	        if(resp>0){
	            inserto=true; 
	        }
	    }
        catch(SQLException e)
	    {
	        Log4JManager.error("Error en insertarDetallePagoRC", e);		  
	    }
        return inserto;
    }    

    /**
     * metodo para insertar los movimientos de cheques
     * @param con Connection    
     * @param detPago  String,
     * @param numeroCheque String,
     * @param codigoBanco String,
     * @param numeroCuenta String,
     * @param codCiudadPlaza String,
     * @param codDeptoPlaza String,
     * @param fechaGiro String,
     * @param valor String,
     * @param girador String,
     * @param direccion String,
     * @param codCiudadGirador String,
     * @param codDeptoGirador String,
     * @param telefono String,
     * @param observaciones String,
     * @return boolean, true si es efectivo
     * @see com.princetonsa.dao.RecibosCajaDao#insertarMovimientoCheque(java.sql.Connection,String,String,String,String,String,String,String,String,String,String,String,String,String,String)
     */
    public static boolean insertarMovimientoCheque(Connection con,
														            String detPago,
														            String numeroCheque,String codigoBanco,
														            String numeroCuenta,String codCiudadPlaza,
														            String codDeptoPlaza,String fechaGiro,
														            String valor,String girador,String direccion,
														            String codCiudadGirador,String codDeptoGirador,
														            String telefono,String observaciones,String codPaisPlaza,String codPaisGirador, String autorizacion, String query)
    {
        byte resp=0;
        boolean inserto=false;
        try
	    {
	        PreparedStatementDecorator ps = null;	        
	        ps= new PreparedStatementDecorator(con.prepareStatement(query,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));	        
	        ps.setInt(1,Integer.parseInt(detPago));
	        ps.setString(2,numeroCheque);
	        ps.setString(3,codigoBanco);
	        ps.setString(4,numeroCuenta);
	        if(UtilidadTexto.isEmpty(codCiudadPlaza)||codCiudadPlaza.trim().equals("-1"))
	        {
	        	ps.setObject(5,null);
	        }
	        else
	        {
	        	ps.setString(5,codCiudadPlaza);
	        }
	        if(UtilidadTexto.isEmpty(codDeptoPlaza)||codDeptoPlaza.trim().equals("-1"))
	        {
	        	ps.setObject(6,null);
	        }
	        else
	        {
	        	ps.setString(6,codDeptoPlaza);
	        }
	        ps.setString(7,fechaGiro);
	        ps.setDouble(8,Double.parseDouble(valor));
	        ps.setString(9,girador);
	        ps.setString(10,direccion);
	        if(UtilidadTexto.isEmpty(codCiudadGirador)||codCiudadGirador.trim().equals("-1"))
	        {
	        	ps.setObject(11,null);
	        }
	        else
	        {
	        	ps.setString(11,codCiudadGirador);
	        }
	        if(UtilidadTexto.isEmpty(codDeptoGirador)||codDeptoGirador.trim().equals("-1"))
	        {
	        	ps.setObject(12,null);
	        }
	        else
	        {
	        	ps.setString(12,codDeptoGirador);
	        }
	        ps.setString(13,telefono);
	        ps.setString(14,observaciones);
	        if(UtilidadTexto.isEmpty(codPaisPlaza)||codPaisPlaza.trim().equals("-1"))
	        {
	        	ps.setObject(15,null);
	        }
	        else
	        {
	        	ps.setString(15,codPaisPlaza);
	        }
	        if(UtilidadTexto.isEmpty(codPaisGirador)||codPaisGirador.trim().equals("-1"))
	        {
	        	ps.setObject(16,null);
	        }
	        else
	        {
	        	ps.setString(16,codPaisGirador);
	        }
	        
	        if(!UtilidadTexto.isEmpty(autorizacion))
	        	ps.setString(17, autorizacion);
	        else
	        	ps.setNull(17, Types.VARCHAR);
	        
	        
	        resp=Byte.parseByte(ps.executeUpdate()+"");
	        if(resp>0)
	        {
	            inserto=true; 
	        }
	    }
        catch(SQLException e)
	    {
	        logger.error("Error en insertarMovimientoCheque",e );		  
	    }
        return inserto; 
    }
    /**
     * metodo para insertar los movimientos de tarjetas de
     * credito
     * @param con Connection     
     * @param detPago String,
     * @param codTarjeta String,
     * @param numTarjeta String,
     * @param numComprobante String,
     * @param numAutorizacion String,
     * @param fecha String,
     * @param valor String,
     * @param girador String,
     * @param direccion String,
     * @param codCiudad String,
     * @param codDepto String,
     * @param telefono String,
     * @param observaciones String,
     * @return boolean, true si es efectivo
     * @see com.princetonsa.dao.RecibosCajaDao#insertarMovimientosTarjetas(java.sql.Connection,String,String,String,String,String,String,String,String,String,String,String,String,String,String)
     */
    public static boolean insertarMovimientosTarjetas (Connection con,
															            String detPago,
															            String codTarjeta,String numTarjeta,
															            String numComprobante,String numAutorizacion,
															            String fecha, String valor,String girador,
															            String direccion,String codCiudad,String codDepto,
															            String telefono,String observaciones,String codPais,String fechaVencimiento, String codigoSeguridad, String query, int entidadFinanciera)
    {
    	
    	logger.info("\n insertarMovimientosTarjetas codPais-->"+codPais+" codCiudad -->"+codCiudad+" codDepto-->"+codDepto);
    	
        byte resp=0;
        boolean inserto=false;
        try
	    {
	        PreparedStatementDecorator ps = null;	        
	        ps= new PreparedStatementDecorator(con.prepareStatement(query,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));	        
	        ps.setInt(1,Integer.parseInt(detPago));
	        ps.setInt(2,Integer.parseInt(codTarjeta));
	        ps.setString(3,numTarjeta);
	        ps.setString(4,numComprobante);
	        ps.setString(5,numAutorizacion);
	        ps.setString(6,fecha);
	        ps.setDouble(7,Double.parseDouble(valor));
	        ps.setString(8,girador);	       
	        ps.setString(9,direccion);
	        
	        if(UtilidadTexto.isEmpty(codCiudad)||codCiudad.trim().equals("-1"))
	        {
	        	ps.setObject(10,null);
	        }
	        else
	        {
	        	ps.setString(10,codCiudad);
	        }
	        if(UtilidadTexto.isEmpty(codDepto)||codDepto.trim().equals("-1"))
	        {
	        	ps.setObject(11,null);
	        }
	        else
	        {
	        	ps.setString(11,codDepto);
	        }
	        ps.setString(12,telefono);
	        ps.setString(13,observaciones);
	        if(UtilidadTexto.isEmpty(codPais)||codPais.trim().equals("-1"))
	        {
	        	ps.setObject(14,null);
	        }
	        else
	        {
	        	ps.setString(14,codPais);
	        }
	        
	        if(!UtilidadTexto.isEmpty(fechaVencimiento))
	        	ps.setString(15, fechaVencimiento);
	        else
	        	ps.setNull(15, Types.VARCHAR);
	        
	        if(!UtilidadTexto.isEmpty(codigoSeguridad))
	        	ps.setString(16, codigoSeguridad);
	        else
	        	ps.setNull(16, Types.VARCHAR);
	        ps.setInt(17, entidadFinanciera);
	        
	        resp=Byte.parseByte(ps.executeUpdate()+"");	
	        if(resp>0){
	            inserto=true; 
	        }
	    }
        catch(SQLException e)
	    {
	        logger.warn(e+"Error en insertarMovimientosTarjetas"+e.toString() );		  
	    }
        return inserto; 
    }
    /**
     * metodo para consultar el ultimo codigo
     * del recibo de caja insertado
     * @param con Connection
     * @param institucion int
     * @return int, codigo del recibo de caja
     * @see com.princetonsa.dao.RecibosCajaDao#ultimoReiboCajaInsertado(java.sql.Connection,int)
     */
    public static int ultimoReiboCajaInsertado(Connection con,int institucion)
    {
        try
	    {
	        PreparedStatementDecorator ps = null;	        
	        ps= new PreparedStatementDecorator(con.prepareStatement(ultimoCodigoReciboCajaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	        ps.setInt(1,institucion);
	        ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
	        if(rs.next())
	            return rs.getInt("secuencia");
	    }
        catch(SQLException e)
        {
            logger.warn(e+"Error en ultimoReiboCajaInsertado"+e.toString() );            
        }
        return ConstantesBD.codigoNuncaValido;
    }
    /**
     * metodo para consultar el ultimo codigo insertado 
     * de detalle de pagos
     * @param con Connection
     * @param institucion
     * @return int, c�digo del detalle
     * @see com.princetonsa.dao.RecibosCajaDao#ultimoCodigoDetallePagos(java.sql.Connection,int)
     */
    public static int ultimoCodigoDetallePagos(Connection con,int institucion)
    {
        try
	    {
	        PreparedStatementDecorator ps = null;	        
	        ps= new PreparedStatementDecorator(con.prepareStatement(ultimoCodigoDetallePagosStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	        ps.setInt(1,institucion);
	        ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
	        if(rs.next())
	            return rs.getInt("consecutivo");
	    }
        catch(SQLException e)
        {
            logger.warn(e+"Error en ultimoCodigoDetallePagos"+e.toString() );            
        }
        return ConstantesBD.codigoNuncaValido;  
    }    
    /**
     * metodo para insertar el detalle del concepto
     * del recibo de caja
     * @param con Connection
     * @param query String
     * @param numReciboCaja String
     * @param institucion String
     * @param concepto String
     * @param docSoporte String
     * @param valor String
     * @param tipoIdBeneficiario String
     * @param numIdBeneficiario String
     * @param nombreBeneficiario String
     * @return boolean, true si es efectivo
     * @see com.princetonsa.dao.RecibosCajaDao#insertarDetalleConceptosRecibosCaja(java.sql.Connection,String,String,String,String,String,String,String,String,String)
     */
    public static boolean insertarDetalleConceptosRecibosCaja(Connection con,
																            String numReciboCaja,
																            String institucion,String concepto,
																            String docSoporte,String valor,
																            String tipoIdBeneficiario,String numIdBeneficiario,
																            String nombreBeneficiario,String deudor,
																            String clase_deudorco,String num_id_deudorco,
																            int inst_deudor, int ingreso, String codPaciente, String codigoTipoConcepto)
    {
        byte resp=0,resp1=0;
        boolean inserto=false;
        
        int codigoPkDetalle=UtilidadBD.obtenerSiguienteValorSecuencia(con, "tesoreria.seq_detalle_conceptos_rc");
    	
        /*
         * query para insertar el detalle del concepto de el recibo de caja
         */
        final String insertarDetalleConceptosStr="INSERT " +
							"INTO detalle_conceptos_rc " +
							"(consecutivo,numero_recibo_caja,institucion,concepto,doc_soporte,valor,tipo_id_beneficiario,numero_id_beneficiario,nombre_beneficiario,deudor,clase_deudorco,num_id_deudorco,inst_deudor,ingreso) " +
							"VALUES ("+codigoPkDetalle+",?,?,?,?,?,?,?,?,?,?,?,?,?)";

    	logger.info("Sql --> " + insertarDetalleConceptosStr);
        logger.info("EL CONCEPTO-------->"+concepto);
    	logger.info("LA INSTITUCION----->"+inst_deudor);
        try
	    {
	        PreparedStatementDecorator ps = null;	        
	        ps= new PreparedStatementDecorator(con.prepareStatement(insertarDetalleConceptosStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));	 
	        ps.setString(1,numReciboCaja);
	        ps.setInt(2,Integer.parseInt(institucion));
	        ps.setString(3,concepto+"");
	        ps.setString(4,docSoporte);
	        ps.setDouble(5,Double.parseDouble(valor));
	        
	        if(tipoIdBeneficiario==null || tipoIdBeneficiario.trim().equals("")||tipoIdBeneficiario.equals(ConstantesBD.codigoNuncaValido+""))
	        	ps.setObject(6, null);
	        else
	        	ps.setString(6,tipoIdBeneficiario);
	        
	        ps.setString(7,numIdBeneficiario);
	        ps.setString(8,nombreBeneficiario);
	        
	        if (deudor!=null && !deudor.equals("") && !deudor.equals("null") && !deudor.equals("-1"))
	        	ps.setObject(9, deudor);
	        else
	        	ps.setNull(9, Types.INTEGER);
	        
	        //**********************************************
	        // Anexo 762
	        if(clase_deudorco!=null && !clase_deudorco.equals(""))
	        	ps.setString(10, clase_deudorco);
	        else
	        	ps.setNull(10, Types.VARCHAR);
	        
	        if(num_id_deudorco!=null && !num_id_deudorco.equals(""))
	        	ps.setString(11, num_id_deudorco);
	        else
	        	ps.setNull(11, Types.VARCHAR);
	        
	        if(inst_deudor>0)
	        	ps.setInt(12, inst_deudor);
	        else
	        	ps.setNull(12, Types.INTEGER);
	        
	        if(ingreso>0)
	        	ps.setInt(13, ingreso);       
	        else
	        	ps.setNull(13, Types.INTEGER);
	        // Fin Anexo 762
	        //**********************************************
	        
	        Log4JManager.info(ps);
	        resp=Byte.parseByte(ps.executeUpdate()+"");	
	        if(resp>0){
	        	if(Utilidades.convertirAEntero(codigoTipoConcepto)==ConstantesBD.codigoTipoIngresoTesoreriaAbonos)
	        	{
	        		try{
	        			PreparedStatementDecorator ps2 = null;
	        			String insertDetalle = "INSERT INTO tesoreria.detalle_conc_rc_x_paciente (detalle_concepto_rc, paciente) VALUES (?,?)";
	        			ps2= new PreparedStatementDecorator(con.prepareStatement(insertDetalle,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));	 
	        			ps2.setInt(1,codigoPkDetalle);
	        			ps2.setInt(2,Integer.parseInt(codPaciente));
	        			resp1=	Byte.parseByte(ps2.executeUpdate()+"");
	        	        Log4JManager.info(ps2);
	        			if(resp1>0)
	        			{
	        				inserto=true; 
	        			}
	        			ps2.close();
	        		}catch(SQLException e)
	        		{
	        			Log4JManager.error("Error en insertar Detalle_conc_rc_x_paciente ",e );
	        		}
	        	}else
	        	{
	        		inserto=true; 
	        	}
	            
	        }
	        ps.close();
	    }
        catch(SQLException e)
	    {
	        Log4JManager.error("Error en insertarDetalleConceptosRecibosCaja ", e);		  
	    }
        return inserto; 
    }
    /**
     * /**
     * metodo para actualizar el documento
     * de detalle conceptos     
     * @param con Connection 
     * @param documento String
     * @param consecutivo int 
     * @param institucion int 
     * @return boolean, true cuando es efectivo
     * @see com.princetonsa.dao.RecibosCajaDao#insertarDetalleConceptosRecibosCaja(java.sql.Connection,String,int,int)
     */  
    public static boolean actualizarDocumentoDetalleConceptos(Connection con,String documento,int consecutivo,int institucion)
    {
        byte resp=0;
        boolean inserto=false;
        try
	    {
	        PreparedStatementDecorator ps = null;	        
	        ps= new PreparedStatementDecorator(con.prepareStatement(actualizarDocumentoDetalleConceptosStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	        ps.setString(1,documento);
	        ps.setInt(2,consecutivo);
	        ps.setInt(3,institucion);
	        resp=Byte.parseByte(ps.executeUpdate()+"");	
	        if(resp>0){
	            inserto=true; 
	        }
	    }
	    catch(SQLException e)
	    {
	        logger.warn(e+"Error en actualizarDocumentoDetalleConceptos"+e.toString() );		  
	    }
	    return inserto;
    }    
    /**
     * metodo para actualizar el estado paciente de 
     * la factura a cancelado.
     * @param con Connection
     * @param institucion int
     * @param codigoFact int 
     * @return boolean, true si es efectivo
     * @see com.princetonsa.dao.RecibosCajaDao#actualizarEstadoPacienteFactura(java.sql.Connection,int,int)
     */
    public static boolean actualizarEstadoPacienteFactura(Connection con,int institucion, int codigoFact)
    {
        byte resp=0;
        boolean actualizo=false;
        try
	    {
	        PreparedStatementDecorator ps = null;	        
	        ps= new PreparedStatementDecorator(con.prepareStatement(actualizarEstadoPacienteFacturaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	        ps.setInt(1,institucion);
	        ps.setInt(2,codigoFact);
	        resp=Byte.parseByte(ps.executeUpdate()+"");	
	        if(resp>0){
	            actualizo=true; 
	        }
	    }
	    catch(SQLException e)
	    {
	        logger.warn(e+"Error en actualizarEstadoPacienteFactura"+e.toString() );		  
	    }
	    return actualizo;
    }
    /**
     * metodo para insertar los pagos de realizados
     * de facturas paciente
     * @param con Connection
     * @param tipoDoc String
     * @param documento String
     * @param factura String
     * @param valor String
     * @param estado String
     * @param institucion String
     * @param query String
     * @return boolean, true si es efectivo
     * @see com.princetonsa.dao.RecibosCajaDao#actualizarEstadoPacienteFactura(java.sql.Connection,String,String,String,String,int,int,String)
     */
    public static boolean insertarPagosFacturaPaciente(Connection con,String tipoDoc,
														              String documento,String factura,
														              String valor,int estado,int institucion,String query)
    {
        byte resp=0;
        boolean inserto=false;
        try
	    {
	        PreparedStatementDecorator ps = null;	        
	        ps= new PreparedStatementDecorator(con.prepareStatement(query,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	        ps.setInt(1,Integer.parseInt(tipoDoc));
	        ps.setString(2,documento);
	        ps.setInt(3,Integer.parseInt(factura));
	        ps.setDouble(4,Double.parseDouble(valor));
	        ps.setInt(5,estado);
	        ps.setInt(6,institucion);
	        resp=Byte.parseByte(ps.executeUpdate()+"");	
	        if(resp>0){
	            inserto=true; 
	        }
	    }
	    catch(SQLException e)
	    {
	        logger.warn(e+"Error en insertarPagosFacturaPaciente"+e.toString() );		  
	    }
	    return inserto;
    }
    /**
     * metodo para insertar los pagos generales
     * empresa
     * @param con Connection
     * @param query String
     * @param convenio int
     * @param tipoDoc int
     * @param documento String
     * @param estado int 
     * @param valor String
     * @param institucion int
     * @param codigoPk TODO
     * @return boolean, true si es efectivo
     * @see com.princetonsa.dao.RecibosCajaDao#insertarPagosGeneralEmpresa(java.sql.Connection,String,int,int,String,int,String,int)
     */
    public static boolean insertarPagosGeneralEmpresa(Connection con,String query,
														             int convenio,int tipoDoc,
														             String documento,int estado,
														             String valor,int institucion,String fechaDocumento,
														             String deudor, int codigoContrato  /*, int codigoPk*/)
    {
        byte resp=0;
        boolean inserto=false;
        try
	    {
	        PreparedStatementDecorator ps = null;	        
	        ps= new PreparedStatementDecorator(con.prepareStatement(query,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	        //ps.setInt(1, codigoPk);
	        if (!(convenio+"").equals("") && !(convenio+"").equals("null") && !(convenio+"").equals("-1"))
	        	ps.setInt(1,convenio);
	        else
	        	ps.setNull(1, Types.INTEGER);
	        ps.setInt(2,tipoDoc);
	        ps.setString(3,documento);
	        ps.setInt(4,estado);
	        ps.setDouble(5,Double.parseDouble(valor));
	        ps.setInt(6,institucion);
	        ps.setString(7,fechaDocumento);
	        if (!deudor.equals("") && !deudor.equals("null") && !deudor.equals("-1"))
	        	ps.setObject(8, deudor);
	        else
	        	ps.setNull(8, Types.INTEGER);
	        
	        if(codigoContrato>0)
	        	ps.setInt(9, codigoContrato);
	        else
	        	ps.setNull(9, Types.INTEGER);
	        
	        resp=Byte.parseByte(ps.executeUpdate()+"");	
	        if(resp>0){
	            inserto=true; 
	        }
	    }
	    catch(SQLException e)
	    {
	        logger.warn(e+"Error en insertarPagosGeneralEmpresa"+e.toString() );		  
	    }
	    return inserto;	        
    }
    /**
     * metodo para insertar movimientos
     * de abonos
     * @param con Connection
     * @param query String
     * @param paciente String
     * @param documento String 
     * @param tipo String
     * @param valor String
     * @param fecha String
     * @param hora String 
     * @param institucion int
     * @return boolean, true si es efectivo
     * @see com.princetonsa.dao.RecibosCajaDao#insertarMovimientosAbonos(java.sql.Connection,String,String,String,String,String,String,String,int)
     */
    public static boolean insertarMovimientosAbonos(Connection con,String query,
														           String paciente,String documento,
														           String tipo,String valor,
														           String fecha,String hora,int institucion, int centroAtencionDuenio, Integer ingreso, int codigoCentroAtencion)
    {
        int resp=0;
        boolean inserto=false;
        try
	    {
	        PreparedStatementDecorator ps = null;	        
	        if (con == null || con.isClosed()) 
			{
				DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
				con = myFactory.getConnection();
			}
	        ps= new PreparedStatementDecorator(con.prepareStatement(query,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	        ps.setInt(1,Integer.parseInt(paciente));
	        ps.setInt(2,Integer.parseInt(documento));
	        ps.setInt(3,Integer.parseInt(tipo));
	        ps.setDouble(4,Double.parseDouble(valor));
	        ps.setString(5,fecha);
	        ps.setString(6,hora);
	        ps.setInt(7,institucion);
	        
	        //Anexo 958 para agregar el centro de atencion duenio del paciente
	        if (centroAtencionDuenio!=ConstantesBD.codigoNuncaValido)
	        	ps.setInt(8, centroAtencionDuenio);
	        else
	        	ps.setNull(8, Types.INTEGER);

	        if(ingreso==null)
	        {
	        	ps.setNull(9, Types.INTEGER);
	        }
	        else
	        {
	        	ps.setInt(9,ingreso);
	        }
	        
	        if (codigoCentroAtencion > 0)
	        	ps.setInt(10, codigoCentroAtencion);
	        else
	        	ps.setNull(10, Types.INTEGER);
	        
	        resp=ps.executeUpdate();	
	        if(resp>0){
	            inserto=true; 
	        }
	    }
	    catch(SQLException e)
	    {
	        logger.warn(e+"Error en insertarMovimientosAbonos"+e.toString() );		  
	    }
	    return inserto;
    }
    /**
     * metodo para consultar el ultimo codigo
     * de movimientos abonos insertado
     * @param con Connection
     * @param institucion
     * @return int, c�digo del detalle
     * @see com.princetonsa.dao.RecibosCajaDao#ultimoCodigoMovimientoAbonos(java.sql.Connection,int)
     */
    public static int ultimoCodigoMovimientoAbonos(Connection con,int institucion)
    {
        try
	    {
	        PreparedStatementDecorator ps = null;	        
	        ps= new PreparedStatementDecorator(con.prepareStatement(ultimoCodigoMovimientoAbonosStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	        ps.setInt(1,institucion);
	        ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
	        if(rs.next())
	            return rs.getInt("consecutivo");
	    }
        catch(SQLException e)
        {
            logger.warn(e+"Error en ultimoCodigoMovimientoAbonos"+e.toString() );            
        }
        return ConstantesBD.codigoNuncaValido;  
    }    
    /**
     * metodo para consultar el ultimo codigo 
     * detalle de conceptos.
     * @param con Connection
     * @param institucion
     * @return int, c�digo del detalle
     * @see com.princetonsa.dao.RecibosCajaDao#ultimoCodigoDetalleConceptos(java.sql.Connection,int)
     */
    public static int ultimoCodigoDetalleConceptos(Connection con,int institucion)
    {
        try
	    {
	        PreparedStatementDecorator ps = null;	        
	        ps= new PreparedStatementDecorator(con.prepareStatement(ultimoCodigoDetalleConceptosStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	        ps.setInt(1,institucion);
	        ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
	        if(rs.next())
	            return rs.getInt("consecutivo");
	    }
        catch(SQLException e)
        {
            logger.warn(e+"Error en ultimoCodigoDetalleConceptos"+e.toString() );            
        }
        return ConstantesBD.codigoNuncaValido;  
    }
    
    /**
     * @param con
     * @param numeroReciboCaja
     * @param institucion
     * @param nuevoEstado
     * @return
     */
    public static boolean actualizarEstadoReciboCaja(Connection con, String numeroReciboCaja, int institucion, int nuevoEstado)
    {
        try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaActualizacionEstadoRC,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1,nuevoEstado);
			ps.setString(2,numeroReciboCaja);
			ps.setInt(3,institucion);
			return (ps.executeUpdate()>0);
		}
		catch(SQLException e)
		{
		    logger.error("Error actualizando el estado del ReciboCaja [SqlBaseAnulacionReciboCajaDao]"+e);
		}
		return false;
    }
    
    /**
     * 
     * @param con
     * @param reciboCaja
     * @param codigoInstitucionInt
     * @return
     */
	public static String obtenerCajaCajeroRC(Connection con, String reciboCaja, int codigoInstitucionInt) 
	{
		String cadena=" SELECT usuario,caja from recibos_caja  where numero_recibo_caja='"+reciboCaja+"' and institucion="+codigoInstitucionInt;
		String resultado="";
		PreparedStatementDecorator ps=null;
		ResultSetDecorator rs;
		try {
			if(Utilidades.convertirAEntero(reciboCaja)>0)
			{
				ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				rs =new ResultSetDecorator(ps.executeQuery());
				if(rs.next())
				{
					resultado=rs.getString(1)+ConstantesBD.separadorSplit+rs.getString(2);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return resultado;
	}
	
	
	
	
	/**
	 * Busqueda
	 * @param con
     * @param HashMap parametros
     * @return ArrayList<DtoRecibosCaja>
	 */
	public static ArrayList<DtoRecibosCaja> facturasVarias(Connection con, HashMap parametros)
	{
		logger.info("*******************************************************************************************************************");
		logger.info("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
		logger.info("*******************************************************************************************************************");
		logger.info("Multas------>"+parametros.containsKey("consectivo_mulcitas"));
		logger.info("Tipo Deudor-> "+parametros.containsKey("tipo_deudor"));
		logger.info("Deudor------> "+parametros.containsKey("deudor")); // se filtra por el deudor parametros.containsKey("consectivo_mulcitas")){
		logger.info("Factura----->"+parametros.containsKey("factura"));// se filtra por el consecutivo de la factura
		logger.info("Concepto---->"+parametros.containsKey("concepto"));// se filtra por el concepto de la factu
		logger.info("Deudor------>"+parametros.containsKey("deudor"));// se filtra por el deudor 
		logger.info("*");
		logger.info("*******************************************************************************************************************");
		logger.info("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
		logger.info("");
		
		
		String consulta = "";
		ArrayList<DtoRecibosCaja> recibosCajaAPostular = new ArrayList<DtoRecibosCaja>();
		HashMap<String,Object> datos = new HashMap<String,Object>(); 
		try{
			
			//validadcion de que las facturas varias esten aprobadas y sin pagos relacionados 
			//EL ESTADO DE LA APLICACION PAGOS FACTURAS VARIAS DEBE ESTAR EN EN ESTADO ANULADO (3)
			PreparedStatementDecorator ps=null;
			ResultSetDecorator rs;
			consulta ="SELECT DISTINCT " +
				"fv.consecutivo AS codigo_factura, " +
				"fv.codigo_fac_var AS nro_factura, " +
				"fv.fecha AS fecha,  " +
				"((coalesce(valor_factura,0)+coalesce(ajustes_debito,0))-coalesce(ajustes_credito,0)) AS saldo_factura,  " +
				"d.tipo AS tipoDeudor,  " +
				"CASE " +
				
				"WHEN d.tipo ='"+ConstantesIntegridadDominio.acronimoPaciente+"' THEN getnombrepersona(d.codigo_paciente) " +
				"WHEN d.tipo='"+ConstantesIntegridadDominio.acronimoTipoDeudorEmpresa+"' THEN getnombreempresa(d.codigo_empresa) " +
				"WHEN d.tipo ='"+ConstantesIntegridadDominio.acronimoTipoDeudorOtros+"' THEN getdescripciontercero(d.codigo_tercero) " +
				"WHEN d.tipo= '"+ConstantesIntegridadDominio.acronimoOtro+"' THEN  d.primer_nombre ||'  '|| d.primer_apellido " +
			
				"END AS desdeudor," +
			
				"CASE " +
				
				"WHEN d.tipo='"+ConstantesIntegridadDominio.acronimoPaciente+"' THEN getidentificacionpaciente(d.codigo_paciente)  " +
				"WHEN d.tipo='"+ConstantesIntegridadDominio.acronimoTipoDeudorEmpresa+"' THEN getnitempresa(d.codigo_empresa)  " +
				"WHEN d.tipo='"+ConstantesIntegridadDominio.acronimoTipoDeudorOtros+"' THEN getnittercero(d.codigo_tercero)  " +
				"WHEN d.tipo= '"+ConstantesIntegridadDominio.acronimoOtro+"' THEN  d.numero_identificacion " +
				"END AS identificacion_deudor ,"+
				
				"coalesce(fv.deudor,"+ConstantesBD.codigoNuncaValido+") AS cod_deudor, " +
				"fv.concepto AS concepto, " +
				"cfv.tipo_concepto  AS tipo_concepto, " +
				" case when d.codigo_paciente  is not null  then getTipoId(d.codigo_paciente) " +
				" when d.codigo_tercero  is not null then '"+EmunTiposIdentificacion.NI+"'"+
				" when d.codigo_empresa  is not null then '"+EmunTiposIdentificacion.NI+"'" +
				" else  d.tipo_identificacion end as tipoIdentificacion " +
				"" +
				
				"FROM facturas_varias fv " +
				"INNER JOIN deudores  d ON(d.codigo=fv.deudor) " + 
				"INNER JOIN conceptos_facturas_varias cfv ON (cfv.consecutivo = fv.concepto) ";
			
			String where = "";
			String order = "";
			
			if(parametros.containsKey("tipo_deudor"))// se filtra por el tipo de deudor
				where += "INNER JOIN deudores d2 ON (d2.codigo = fv.deudor AND d2.tipo = '"+parametros.get("tipo_deudor").toString()+"' ) ";
			
			if(parametros.containsKey("consectivo_mulcitas")){
				if(parametros.containsKey("fecha_generacion")){
					where += "INNER JOIN multas_facturas_varias mfv ON (mfv.factura_varia =  fv.consecutivo AND mfv.multa_cita = "+parametros.get("consectivo_mulcitas")+" ) " ;
					where += "INNER JOIN multas_citas mc ON (mc.consecutivo = "+parametros.get("consectivo_mulcitas")+" " +
						"AND mc.estado = '"+ConstantesIntegridadDominio.acronimoEstadoFacturado+"' " +
						"AND mc.fecha_generacion = '"+UtilidadFecha.conversionFormatoFechaABD(parametros.get("fecha_generacion").toString())+"' )";
				}else{
					where += "INNER JOIN multas_facturas_varias mfv ON (mfv.factura_varia =  fv.consecutivo AND mfv.multa_cita = "+parametros.get("consectivo_mulcitas")+" ) " ;
					where += "INNER JOIN multas_citas mc ON (mc.consecutivo = "+parametros.get("consectivo_mulcitas")+" " +
						"AND mc.estado = '"+ConstantesIntegridadDominio.acronimoEstadoFacturado+"' ) " ;
				}
			}else{
				if(parametros.containsKey("fecha_generacion")){
					where += "INNER JOIN multas_facturas_varias mfv ON (mfv.factura_varia =  fv.consecutivo ) " ;
					where += "INNER JOIN multas_citas mc ON (mfv.multa_cita = mc.consecutivo " +
						"AND mc.estado = '"+ConstantesIntegridadDominio.acronimoEstadoFacturado+"' " +
						"AND mc.fecha_generacion = '"+UtilidadFecha.conversionFormatoFechaABD(parametros.get("fecha_generacion").toString())+"' )";
				}
			}
			
			where += "LEFT OUTER JOIN aplicac_pagos_factura_fvarias apfv ON (apfv.factura = fv.consecutivo ) " +
				"LEFT OUTER JOIN aplicacion_pagos_fvarias afv ON (apfv.aplicacion_pagos = afv.codigo " +
				"AND ((apfv.factura IS NULL) OR (afv.estado = 3 AND apfv.factura IS NOT NULL))) ";
			
			where += "WHERE fv.estado_factura = '"+ConstantesIntegridadDominio.acronimoEstadoAprobado+"' ";
				
			if(parametros.containsKey("factura"))
			{
				// se filtra por el consecutivo de la factura
				where += " AND fv.consecutivo = "+parametros.get("factura")+" ";
			
				/*
				where +=
					" " +
					"	AND " +	

					"( " +
					"	( " +
					"	select count(0) 	from 		tesoreria.recibos_caja rc " + 	
					"	INNER JOIN 		tesoreria.devol_recibos_caja  drc ON (rc.numero_recibo_caja=drc.numero_rc) " + 	
					"	INNER JOIN 		tesoreria.detalle_conceptos_rc dcr ON  (rc.numero_recibo_caja=dcr.numero_recibo_caja) " + 	
					"	INNER JOIN 		conceptos_ing_tesoreria cit ON(cit.codigo=dcr.concepto and cit.institucion=dcr.institucion)" + 
					"	where		 rc.estado not in (4) and drc.estado in ('"+ConstantesIntegridadDominio.acronimoEstadoAnulado+"','"+ConstantesIntegridadDominio.acronimoEstadoAprobado+"')" +
					"	AND dcr.doc_soporte ='"+parametros.get("codigo_pk_facturas_varias")+"' and cit.codigo_tipo_ingreso=3" +
					"	)>0  " +
					
					"	OR " +  
					
					"	( " +
					"	select count(0) " + 
					"		from tesoreria.recibos_caja rc " + 
					"	INNER JOIN " +
					"		tesoreria.detalle_conceptos_rc dcr ON  (rc.numero_recibo_caja=dcr.numero_recibo_caja) " + 	
					"	INNER JOIN " +
					"		conceptos_ing_tesoreria cit ON(cit.codigo=dcr.concepto and cit.institucion=dcr.institucion) " +  
					"	LEFT OUTER JOIN tesoreria.devol_recibos_caja  drc ON (rc.numero_recibo_caja=drc.numero_rc) " + 		
					"	where rc.estado not in (4) and dcr.doc_soporte ='"+parametros.get("codigo_pk_facturas_varias")+"' and  cit.codigo_tipo_ingreso=3 AND drc.numero_rc IS NULL " + 
					"	)<=0 " +
					")";
				*/
			
			}
			
			if(parametros.containsKey("concepto"))// se filtra por el concepto de la factura
				where += " AND fv.concepto = "+parametros.get("concepto")+" ";
			
			if(parametros.containsKey("deudor"))// se filtra por el deudor 
				where += " AND fv.deudor = "+parametros.get("deudor")+" ";
			
			
			order= " ORDER BY fv.codigo_fac_var ";
			consulta += where+order;
			
			logger.info("La Consulta >>>>>>>>> "+consulta);
			
			ps= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			rs =new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			{
				DtoRecibosCaja elem = new DtoRecibosCaja();
				elem.setConsecutivo(rs.getObject("codigo_factura").toString());
				elem.setNroFactura(rs.getObject("nro_factura").toString());
				elem.setFecha(UtilidadFecha.conversionFormatoFechaAAp(rs.getObject("fecha").toString()));
				elem.setSaldoFactura(rs.getObject("saldo_factura").toString());
				elem.setCodDeudor(rs.getString("cod_deudor"));
				elem.setDeudor(rs.getString("desdeudor"));
				elem.setIdentificacionDeudor(rs.getString("identificacion_deudor"));
				elem.setConceptoFactura(rs.getString("tipo_concepto"));
				elem.setTipoIdentificacion(rs.getString("tipoIdentificacion"));
				elem.setTipoDeudor(rs.getString("tipoDeudor"));
				// se obtienen los consecutivos de las multas de citas asociadas a la factura varia
				datos.put("consectivo_fvarias", elem.getConsecutivo());
				elem.setMultasCitas(getMultasCitasFacturasVarias(con, datos)); 
				recibosCajaAPostular.add(elem);
			}
		}catch (Exception e) {
			logger.info("Error en la Busqueda >>>>>>>> "+consulta);
			e.printStackTrace();
		}
		return  aplicaReciboCaja(con, recibosCajaAPostular);
	}
	
	
	
	
	
	/**
	 *	METODO  QUE SETTEA 'S' SI Y SOLO SI, SE PUEDE CREAR UN NUEVO RECIBO DE CAJA; EN OTRO CASO SETTEA 'N' 
	 */
	public static  ArrayList<DtoRecibosCaja>  aplicaReciboCaja(Connection con, ArrayList<DtoRecibosCaja> listaRecibos)
	{
		for(DtoRecibosCaja objRecibo : listaRecibos)
		{
			
			 if( UtilidadesFacturasVarias.aplicaReciboCaja(con, objRecibo).equals(ConstantesBD.acronimoSi) )
			 {
				 objRecibo.setAplicaReciboCaja(ConstantesBD.acronimoSi);
				 logger.info("**********************************************************");
				 logger.info("\n   CONSECUTIVO--->"+objRecibo.getConsecutivo());
				 logger.info("\n  	NOMBRE AP-->"+objRecibo.getNombreDeudor());
				 logger.info("\n\n\n***************************************************");
			 }
			
		}
		
	
		return listaRecibos;
	}
	
	
	
	
	
	

	/**
	 * 
	 * @param con
     * @param parametros
     * @return
	 */
	public static boolean tipoConceptoIsMulta(Connection con, HashMap parametros)
	{
		PreparedStatementDecorator ps=null;
		ResultSetDecorator rs;
		boolean resultado = false;
		try {
			
			ps= new PreparedStatementDecorator(con.prepareStatement(strVerificarTipoConcepto,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1,Utilidades.convertirAEntero(parametros.get("consecutivo").toString()));
			rs =new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				if(rs.getString("tipo_concepto").equals(ConstantesIntegridadDominio.acronimoAcompanianteNinguno)||rs.getString("tipo_concepto").equals(ConstantesIntegridadDominio.acronimoTipoVentaTarjetaodontologica))
					resultado = false;
				else
					resultado = true;
			}
		} catch (SQLException e) {
			logger.info("Error en la consulta del Tipo de Concepto >>>>>> "+strVerificarTipoConcepto);
			logger.info("consecutivo >>>> "+parametros.get("consecutivo").toString());
			e.printStackTrace();
		}
		return resultado;
	}
	
	/**
	 * me retorna la lista de consecutivos de las  las multas asociadas a una factura varia
	 * @param con
     * @param parametros
     * @return
	 */
	public static String getMultasCitasFacturasVarias(Connection con, HashMap parametros)
	{
		PreparedStatementDecorator ps=null;
		ResultSetDecorator rs = null;
		String multasCitas = "";
		String consulta = "" ;
		try {

			consulta = "SELECT mfv.multa_cita||'' AS multa_citas FROM multas_facturas_varias mfv WHERE mfv.factura_varia = ? " ; 

			logger.info("la Consulta >>>>>> "+consulta);
			logger.info("codigo factura >>>>> "+parametros.get("consectivo_fvarias").toString());
			ps= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, Utilidades.convertirAEntero(parametros.get("consectivo_fvarias").toString()));
			rs = new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
				multasCitas += rs.getString("multa_citas")+",";

			if(!multasCitas.equals(""))
				multasCitas.substring(0, multasCitas.length()-1);

		} catch (SQLException e) {
			Log4JManager.error("Error en la consulta del Tipo de Concepto >>>>>> "+consulta);
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
				if (rs != null) {
					rs.close();
				}
			} catch (Exception e2) {
				Log4JManager.error("Error cerrando PreparedStatementDecorator - ResulsetDecorator: " + e2);
			}
		}
		return multasCitas;
	}
	
	
	/**
	 * Actualizar deudor
     * @param con
     * @param HashMap parametros
     * @return boolean
     */
    public static boolean actualizarDeudorPagosGenEmp(Connection con, HashMap parametros)
    {
        try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(strActualizarDeudorPagGenEmp,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1,Utilidades.convertirAEntero(parametros.get("deudor").toString()));
			ps.setInt(2,Utilidades.convertirAEntero(parametros.get("codigo").toString()));
			return (ps.executeUpdate()>0);
		}
		catch(SQLException e)
		{
			logger.info("consulta >>>>>> "+strActualizarDeudorPagGenEmp);
			logger.info("deudor >>>>>"+parametros.get("deudor"));
			logger.info("codigo >>>>>"+parametros.get("codigo"));
		    logger.error("Error actualizando del deudor de pagos generales empresa "+e);
		}
		return false;
    }
    
    /**
	 * valor concepto ingreso tesoreria
	 * @param con
     * @param parametros
     * @return
	 */
	public static String getValorConceptoIngresoTesoreria(Connection con, HashMap parametros)
	{
		PreparedStatementDecorator ps=null;
		ResultSetDecorator rs;
		try 
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(strValorConceptoIngresoTesoreria,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1, parametros.get("codigo").toString());
			rs =new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
				return rs.getString("valor");
		} catch (SQLException e) {
			logger.info("Error en la consulta del Tipo de Concepto >>>>>> "+strValorConceptoIngresoTesoreria);
			logger.info("codigo >>>>"+parametros.get("codigo"));
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * metodo que recalcula el valor de anticipo disponible
	 * @param con
	 * @param usuario
	 * @param codigoContrato
	 * @return
	 */
	public static boolean recalcularValorAnticipoDisponible(Connection con, String usuario, int codigoContrato, String valorAnticipo)
	{
		boolean result = false;
		
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con,strActualizarValorAnticipoREcibidoConvenio);			
			ps.setString(1, valorAnticipo);
			ps.setString(2, usuario);
			ps.setDate(3, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual(con))));
			ps.setString(4, UtilidadFecha.getHoraActual(con));
			ps.setInt(5, codigoContrato);
			logger.info("\n\nquery guardar recibos caja:::::: "+ps);
			if(ps.executeUpdate()>0)
				result = true;
			ps.close();
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return result; 
	}

	/***
	 * 
	 * @param con
	 * @param tipoconcepto
	 * @param codFacturaVaria
	 * @return
	 */
	public static boolean actualizarEstadoTarjeta(Connection con,String tipoconcepto, String codFacturaVaria) 
	{		
		boolean result = false;
		int codigoPk=0;
		PreparedStatementDecorator ps=null;
		ResultSetDecorator rs;
		
		try
		{
			ps= new PreparedStatementDecorator(con,strConsultarCodigoBeneficiario);
			ps.setString(1, codFacturaVaria);
			rs =new ResultSetDecorator(ps.executeQuery());
			logger.info("\n\nla consulta de codigo pk:: "+ps);
			
			if(rs.next())
				codigoPk= rs.getInt("codPk");
			ps.close();
		}catch (Exception e) {
			e.printStackTrace();
		}	
		
		try
		{
			ps= new PreparedStatementDecorator(con,strActualizarEstadoTarjeta);			
			ps.setInt(1, codigoPk);
			logger.info("\n\nquery actualizar estado tarjeta:::::: "+ps);
			if(ps.executeUpdate()>0)
				result = true;
			ps.close();
		}catch (Exception e) {
			e.printStackTrace();
		}		
		
		return result; 
	}


	/**
	 * Inserta los detalle de los abonos generados en el recibo de caja
	 * @param con Conexi&oacute;n con la BD
	 * @param serial Selial del bono
	 * @param consecutivoDetallePagosRC Consecutivo de detalle pago
	 * @param observaciones Observaciones del detalle pago
	 * @return truen en caso de insertar correctamente
	 */
	public static boolean insertarMovimientoBono(Connection con, String serial,String consecutivoDetallePagosRC, String observaciones)
	{
		String insertMovimientosBonos="INSERT INTO tesoreria.movimientos_bonos (codigo_pk, det_pago_rc, num_serial, observaciones) VALUES(?, ?, ?, ?)";
		int consecutivoMovimientosBonos=UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_movimientos_bonos");
		try{
			PreparedStatementDecorator psd=new PreparedStatementDecorator(con, insertMovimientosBonos);
			psd.setInt(1, consecutivoMovimientosBonos);
			psd.setInt(2, Integer.parseInt(consecutivoDetallePagosRC));
			psd.setString(3, serial);
			psd.setString(4, observaciones);
			int resultado=psd.executeUpdate();
			
			psd.close();
			return resultado>0;
		}
		catch (SQLException e) {
			Log4JManager.error("error insertando el movimiento de los bonos", e);
		}
		return false;
	}




	public static boolean recibosGeneradoCorrectamente(Connection con,
			String numReciboCaja) {
		boolean resultado=false;
		String consulta1=" select count(1) from detalle_conceptos_rc where numero_recibo_caja='"+numReciboCaja+"'";
		String consulta2=" select count(1) from detalle_pagos_rc where numero_recibo_caja='"+numReciboCaja+"'";
		PreparedStatementDecorator ps=null;
		ResultSetDecorator rs=null;
		try
		{
			ps=new PreparedStatementDecorator(con,consulta1);
			rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
				resultado=rs.getInt(1)>0;
			if(resultado)
			{
				ps=new PreparedStatementDecorator(con,consulta2);
				rs=new ResultSetDecorator(ps.executeQuery());
				if(rs.next())
					resultado=rs.getInt(1)>0;
			}
			ps.close();
			rs.close();
		}
		catch (Exception e) 
		{
			Log4JManager.error("error",e);
		}
		return resultado;
	}
	
	public static Integer obtenerIngresoPacientePorConsecutivoReciboCaja(String consecutivoReciboCaja){
		PreparedStatement pst=null;
		ResultSet rs = null;
		Integer ingreso=null;
		Connection con=null;
		try {

			String consulta = "SELECT ing.id AS id_ingreso "+
								"FROM recibos_caja rc "+
								"INNER JOIN movimientos_abonos ma ON (ma.institucion=rc.institucion AND ma.codigo_documento || '' =rc.numero_recibo_caja AND ma.tipo = 1) "+
								"INNER JOIN tesoreria.tipos_mov_abonos tma ON(tma.codigo=ma.tipo) "+
								"LEFT OUTER JOIN ingresos ing ON(ing.id=ma.ingreso) "+
								"WHERE rc.consecutivo_recibo = ?";
			con=UtilidadBD.abrirConexion();

			pst= con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.setString(1, consecutivoReciboCaja);
			rs = pst.executeQuery();
			if(rs.next()){
				ingreso = rs.getInt("id_ingreso");
			}

		} catch (SQLException e) {
			Log4JManager.error("Error en obtenerIngresoPacientePorConsecutivoReciboCaja ", e);
		} finally {
			try {
				if (pst != null) {
					pst.close();
				}
				if (rs != null) {
					rs.close();
				}
				if(con != null){
					UtilidadBD.closeConnection(con);
				}
			} catch (Exception e2) {
				Log4JManager.error("Error cerrando PreparedStatementDecorator - ResulsetDecorator: " + e2);
			}
		}
		return ingreso;
	}
}




