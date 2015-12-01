
/*
 * Creado en Jul 6, 2006
 * Andrés Mauricio Ruiz Vélez
 * Princeton S.A (Parquesoft - Manizales)
 */
package com.princetonsa.dao.sqlbase.capitacion;

import java.sql.Connection;
import java.sql.Date;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.sql.Time;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;

/**
 * SqlBase de Pagos de Capitación
 * @author Andrés Mauricio Ruiz Vélez
 * Princeton S.A. (Parquesoft-Manizales) 
 * @version Jul 6, 2006
 */
public class SqlBasePagosCapitacionDao
{
	/**
	 * Manejador de logs de la clase
	 */
	private static Logger logger=Logger.getLogger(SqlBasePagosCapitacionDao.class);
	
	 /**
     * Cadena que contiene la definicion de los campos del select para la consulta de los pagos generales empres
     */
	private static String selectPagosGralEmpresa="SELECT pge.codigo as codigo,pge.convenio as codigo_convenio,getnombreconvenio(pge.convenio) as nombre_convenio," + 
			    														"pge.tipo_doc as codigo_tipo_doc,tdp.descripcion as descripcion_tipo_doc,tdp.acronimo as acronimo_tipo_doc,pge.documento as documento," +
			    														"pge.estado as codigo_estado,ep.descripcion as descripcion_estado,convertiranumero(pge.valor||'') as valor_documento,pge.fecha_documento as fecha_documento," +
			    														"getCodAplicacionPagoCapiActual(pge.codigo) as aplicacion_actual, getUltimaApliPagoCapi(pge.codigo) as numero_pago, " +
							    										"getValorPagosAplicadosCxCCapi(pge.codigo," + ConstantesBD.codigoEstadoAplicacionPagosPendiente+") as valor_pagos_cxc, " +
							    										//"getTotalConceptosApliPagos(getCodAplicacionPagoCapiActual(pge.codigo)) as val_concepto_pago_actual, " +
							    										"getObservacionApliPagoCapi(getCodAplicacionPagoCapiActual(pge.codigo)) as observaciones, " +
							    										"	convertiranumero((pge.valor " +
							    										"		+ getvalconceptosaplipagoscapi(pge.codigo,"+ConstantesBD.codigoTipoConceptoMayorValor+","+ConstantesBD.codigoEstadoAplicacionPagosAprobado+") " +
							    										"		- getvalconceptosaplipagoscapi(pge.codigo,"+ConstantesBD.codigoTipoConceptoMenorValor+","+ConstantesBD.codigoEstadoAplicacionPagosAprobado+") " +
							    										"		- getValorPagosAplicadosCxCCapi(pge.codigo," + ConstantesBD.codigoEstadoAplicacionPagosAprobado+") " +
							    										"		)||'') as valor_x_aplicar" +
							    										"		FROM pagos_general_empresa pge " +
							    										"			INNER JOIN tipos_doc_pagos tdp ON (tdp.codigo=pge.tipo_doc) " +
							    										"			INNER JOIN estados_pagos ep ON (pge.estado=ep.codigo)" +
							    										"			INNER JOIN convenios con ON (pge.convenio=con.codigo)" +
							    										"			INNER JOIN detalle_conceptos_rc dcr ON (dcr.numero_recibo_caja=pge.documento AND dcr.institucion=pge.institucion)" +
							    										"			INNER JOIN conceptos_ing_tesoreria cit ON (dcr.concepto=cit.codigo AND dcr.institucion=cit.institucion)";
	
	/**
     * Variable que contiene la condicion que filtra los pagos general empresa de una determinada institucion y
     * pendientes de realizar / aprobar aplicacion de pago. los pagos deben estar en estado recaudado y/o aplicado
     * y que el convenio sea capitado  y que tienen saldo pendiente de aplicar.
     */
	private static String wherePagosGralEmpresa="WHERE (pge.valor " +
						    	    								"							+ getvalconceptosaplipagoscapi(pge.codigo,"+ConstantesBD.codigoTipoConceptoMayorValor+","+ConstantesBD.codigoEstadoAplicacionPagosAprobado+") " +
						    	    								"							- getvalconceptosaplipagoscapi(pge.codigo,"+ConstantesBD.codigoTipoConceptoMenorValor+","+ConstantesBD.codigoEstadoAplicacionPagosAprobado+") " +
						    	    								"							- getValorPagosAplicadosCxCCapi(pge.codigo," + ConstantesBD.codigoEstadoAplicacionPagosAprobado+") " +
						    	    								"								)>0 " +
						    	    								"					AND (pge.estado ="+ConstantesBD.codigoEstadoPagosRecaudado+" or pge.estado ="+ConstantesBD.codigoEstadoPagosAplicado +") " +
						    	    								"					AND pge.tipo_doc="+ConstantesBD.codigoTipoDocumentoPagosReciboCaja+
						    	    								"					AND con.tipo_contrato="+ConstantesBD.codigoTipoContratoCapitado+			
						    	    								"					AND cit.codigo_tipo_ingreso="+ConstantesBD.codigoTipoIngresoTesoreriaConvenios+
						    	    								"					AND pge.institucion=?";
	
	  /**
     * Cadena para ordenar el listado de pagos general de empresa
     */
	private static String ordenarPagosGralEmpresa=" ORDER BY pge.tipo_doc,convertiranumero(pge.documento||'')";
	
	/**
     * Cadena para realizar la insercion en la BD de una aplicacion de pagos de capitacion de cuenta de cobro.
     */
	 private static String cadenaInsertAplicacionPagosCXC="INSERT INTO apli_pagos_cxc_capitacion (aplica_pagos_capitacion,cuenta_cobro_capitacion,institucion,valor_pago, tipo_pago_capitacion) " +
	 																									"		VALUES (?,?,?,?, ?)";
	 
	 /**
	     * Cadena para actualizar la aplicacion de un pago a nivel de Cuenta de Cobro de capitación
	     */
	    private static String cadenaUpdateAplicacionPagosCXC="UPDATE apli_pagos_cxc_capitacion SET valor_pago= ?, tipo_pago_capitacion=? " +
	    																									"			WHERE aplica_pagos_capitacion=? and cuenta_cobro_capitacion=?";
	
	//----------------------------------------------------------------------------METODOS DE LA CLASE ---------------------------------------------------------------------//
	/**
	 * Método que consulta los documentos pendientes de aplicar aprobar pagos 
     * de convenios capitados
     * @param con
     * @param codigoInstitucion
     * @return HashMap
	 */
	public static HashMap consultarDocumentosPagos(Connection con, int codigoInstitucion)
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros","0");
		
		try
		{
		PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(selectPagosGralEmpresa+wherePagosGralEmpresa+ordenarPagosGralEmpresa));
		ps.setInt(1, codigoInstitucion);
		mapa=cargarValueObjectDocumentosPagos(new ResultSetDecorator(ps.executeQuery()));
		
		 return (HashMap)mapa.clone();
		}
		catch (SQLException e)
		{
		logger.error("Error Consultando consultarDocumentosPagos:  en SqlBasePagosCapitacionDao"+e.toString());
		return null;
		}
	}
	
	/**
	 * Método que realiza la búsqueda avanzada de los documentos de pago
	 * de acuerdo a los parámetros de búsqueda
	 * @param con
	 * @param tipoDocBusqueda
	 * @param documentoBusqueda
	 * @param fechaDocBusqueda
	 * @param convenioBusqueda
	 * @param codigoInstitucion
	 * @return
	 */
	public static HashMap busquedaAvanzadaDocumentos(Connection con, int tipoDocBusqueda, String documentoBusqueda, String fechaDocBusqueda, int convenioBusqueda, int codigoInstitucion)
	{
		StringBuffer condicion=new StringBuffer();
		HashMap mapa=new HashMap();
		mapa.put("numRegistros","0");
		
		condicion.append(wherePagosGralEmpresa);
		PreparedStatementDecorator ps =  null;
		if(tipoDocBusqueda != ConstantesBD.codigoNuncaValido)
		{
			condicion.append(" AND pge.tipo_doc="+tipoDocBusqueda);
		}
		
		if(UtilidadCadena.noEsVacio(documentoBusqueda))
		{
			condicion.append(" AND pge.documento="+documentoBusqueda);
		}
		
		if(UtilidadCadena.noEsVacio(fechaDocBusqueda))
		{
			condicion.append(" AND pge.fecha_documento='"+UtilidadFecha.conversionFormatoFechaABD(fechaDocBusqueda)+"'");
		}
		
		if(convenioBusqueda!=ConstantesBD.codigoNuncaValido)
		{
			condicion.append(" AND pge.convenio="+convenioBusqueda);
		}
		
		try
		{
		 ps =  new PreparedStatementDecorator(con.prepareStatement(selectPagosGralEmpresa+condicion.toString()+ordenarPagosGralEmpresa));
		ps.setInt(1, codigoInstitucion);
		
		mapa=cargarValueObjectDocumentosPagos(new ResultSetDecorator(ps.executeQuery()));
		return (HashMap)mapa.clone();
		}
		catch (SQLException e)
		{
		logger.error("Error en la Búsqueda avanzada de documentos de pago:  en SqlBasePagosCapitacionDao"+e.toString());
		return null;
		}finally{
			try{
				if(ps!=null){
					ps.close();					
				}
			}catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SQLBaseNivelAutorizacionArticuloEspecificoDAO "+sqlException.toString() );
			}
			
			
		}
	}
	
	/**
	 * Mètodo que crea el mapa con la información de los documentos pagos
     * @param rs
     * @return
     * @throws SQLException
     */
    private static HashMap cargarValueObjectDocumentosPagos(ResultSetDecorator rs) throws SQLException
    {
        HashMap mapa=new HashMap();
        int cont=0;
        while (rs.next())
        {
            mapa.put("codigo_"+cont,rs.getString("codigo"));
            mapa.put("codigo_convenio_"+cont,rs.getString("codigo_convenio"));
            mapa.put("nombre_convenio_"+cont,rs.getString("nombre_convenio"));
            mapa.put("codigo_tipo_doc_"+cont,rs.getString("codigo_tipo_doc"));
            mapa.put("descripcion_tipo_doc_"+cont,rs.getString("descripcion_tipo_doc"));
            mapa.put("acronimo_tipo_doc_"+cont,rs.getString("acronimo_tipo_doc"));
            mapa.put("documento_"+cont,rs.getString("documento"));
            mapa.put("codigo_estado_"+cont,rs.getString("codigo_estado"));
            mapa.put("descripcion_estado_"+cont,rs.getString("descripcion_estado"));
            mapa.put("valor_documento_"+cont,rs.getString("valor_documento"));
            mapa.put("fecha_documento_"+cont,rs.getString("fecha_documento"));
            mapa.put("aplicacion_actual_"+cont,rs.getString("aplicacion_actual"));
           
            if(rs.getInt("aplicacion_actual")==ConstantesBD.codigoNuncaValido)
            {
                mapa.put("numero_aplicacion_"+cont,(rs.getInt("numero_pago")+1)+"");
                mapa.put("modificacion_"+cont,"false");
            }
            else
            {
                mapa.put("numero_aplicacion_"+cont,rs.getString("numero_pago"));
               mapa.put("modificacion_"+cont,"true");
            }
            /*if(rs.getInt("codigo_estado")!=ConstantesBD.codigoEstadoPagosRecaudado)
            {
               mapa.put("modificacion_"+cont,"false");
            }
            else
            {
               mapa.put("modificacion_"+cont,"true");
            }*/
            //mapa.put("val_aplicado_"+cont,rs.getString("val_aplicado"));
            //mapa.put("val_concepto_pago_actual"+cont,rs.getString("val_concepto_pago_actual"));
            mapa.put("valor_pagos_cxc_"+cont,rs.getString("valor_pagos_cxc"));
            mapa.put("valor_x_aplicar_"+cont,rs.getString("valor_x_aplicar"));
            mapa.put("observaciones_"+cont,rs.getString("observaciones"));
            cont++;
        }
        mapa.put("numRegistros", cont+"");
        return mapa;
    }
    
	/**
	 * Método que consulta los conceptos de pago para la aplicación del pago del documento 
	 * @param con
	 * @param codigoAplicacionPago
	 * @return
	 */
	public static HashMap consultarConceptosPago(Connection con, int codigoAplicacionPago)
	{
		String consultaStr="SELECT cap.concepto_pagos as codigo_concepto," +
			    							"				cp.descripcion as descripcion_concepto," +
			    							"				cp.tipo as tipo_concepto," +
			    							"				convertiranumero(cap.valor_base||'') as valor_base," +
			    							"				convertiranumero(cap.valor_concepto||'') as valor_concepto," +
			    							"				cap.porcentaje as porcentaje," +
			    							"				'true' as bd " +
			    							"		FROM capitacion.conceptos_apli_pagos_cap cap " +
			    							"			INNER JOIN cartera.concepto_pagos cp on(cp.codigo=cap.concepto_pagos)" +
			    							"				WHERE cap.aplica_pagos_capitacion=?";
		
		
		PreparedStatementDecorator ps= null;
		try
        {
            ps= new PreparedStatementDecorator(con.prepareStatement(consultaStr));
            ps.setInt(1,codigoAplicacionPago);
            
    		HashMap mapaRetorno=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
    		ps.close();
    		return mapaRetorno;

        }
        catch (SQLException e)
        {
        	logger.error("Error en consultando los Conceptos de Pago:  en SqlBasePagosCapitacionDao"+e.toString());
            e.printStackTrace();
            return null;
        }finally{
			try{
				if(ps!=null){
					ps.close();					
				}
			}catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SQLBaseNivelAutorizacionArticuloEspecificoDAO "+sqlException.toString() );
			}
			
			
		}
	}
	
	/**
	 * Método que guarda/elimina los concetos de pago y actualiza/inserta la aplicación de pagos
	 * @param con
	 * @param mapa
	 * @param seq 
	 * @return
	 */
	public static int guardarConceptosAplicacionPagos(Connection con, HashMap mapa, String seq)
	{
		PreparedStatementDecorator ps=null;
        boolean finTransaccion=true;
        try
        {
            UtilidadBD.iniciarTransaccion(con);
            //---------Si es modificar ---------------//
	        if(UtilidadTexto.getBoolean(mapa.get("modificacion")+""))
	        {
	            String actualizarAplicacionPagosEmpresa="UPDATE capitacion.aplicacion_pagos_capitacion set fecha_aplicacion=?,observaciones=? " +
	            																					"WHERE codigo=?";
	            
	            
                ps= new PreparedStatementDecorator(con.prepareStatement(actualizarAplicacionPagosEmpresa));
                ps.setDate(1,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD( mapa.get("fecha_aplicacion")+"")));
                ps.setString(2,mapa.get("observaciones")+"");
                ps.setInt(3, Utilidades.convertirAEntero(mapa.get("codigo_aplicacion")+""));
                finTransaccion=(ps.executeUpdate()>0);
                
                if(finTransaccion)
                {
                	 eliminarConceptosAplicacion(con,Utilidades.convertirAEntero(mapa.get("codigo_aplicacion")+""));
                }
            }
	        else if(!UtilidadTexto.getBoolean(mapa.get("modificacion")+""))
	        {
	            String insertaAplicacionPagosEmpresa="INSERT INTO capitacion.aplicacion_pagos_capitacion " +
	            													"(codigo,pagos_general_empresa,estado,numero_aplicacion,fecha_aplicacion,observaciones,usuario,fecha_grabacion,hora_grabacion) " +
	            													"VALUES ("+seq+",?,?,?,?,?,?,?,?)";
	            
               
	            ps= new PreparedStatementDecorator(con.prepareStatement(insertaAplicacionPagosEmpresa));
                ps.setInt(1,Utilidades.convertirAEntero(mapa.get("pago_general_empresa")+""));
                ps.setInt(2,Utilidades.convertirAEntero(mapa.get("estado_aplicacion")+""));
                ps.setInt(3,Utilidades.convertirAEntero(mapa.get("numero_aplicacion")+""));
                ps.setDate(4,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD( mapa.get("fecha_aplicacion")+"")));
                ps.setString(5,mapa.get("observaciones")+"");
                ps.setString(6,mapa.get("usuario")+"");
                ps.setDate(7,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD( mapa.get("fecha_grabacion")+"")));
                ps.setString(8,mapa.get("hora_grabacion")+"");
                finTransaccion=false;
               
                if(ps.executeUpdate()>0)
                {
                    finTransaccion=(actualizarEstadoPagoEmpresa(con, Utilidades.convertirAEntero(mapa.get("pago_general_empresa")+""), Utilidades.convertirAEntero(mapa.get("estado_pago")+""))>0);
                }
	        }
	        
	        //----------Inserción de los conceptos de pago-------------------------------//
	        if(finTransaccion)
	        {
	            for(int i=0;i<Utilidades.convertirAEntero(mapa.get("numRegistros")+"");i++)
	            {
	                if(finTransaccion&&!UtilidadTexto.getBoolean(mapa.get("eliminado_"+i)+""))
	                {
	                    String cadena="INSERT INTO capitacion.conceptos_apli_pagos_cap " +
	                    							"				(aplica_pagos_capitacion,concepto_pagos,institucion,valor_base,porcentaje,valor_concepto) " +
	                    							"						VALUES (getCodAplicacionPagoCapiActual(?),?,?,?,?,?)";
	                    
	                    
	                    ps= new PreparedStatementDecorator(con.prepareStatement(cadena));
	                    ps.setInt(1,Utilidades.convertirAEntero(mapa.get("pago_general_empresa")+""));
	                    ps.setString(2,mapa.get("codigo_concepto_"+i)+"");
	                    ps.setInt(3,Utilidades.convertirAEntero(mapa.get("institucion")+""));
	                    ps.setDouble(4,Utilidades.convertirADouble(mapa.get("valor_base_"+i)+""));
	                    ps.setDouble(5,Utilidades.convertirADouble(mapa.get("porcentaje_"+i)+""));
	                    ps.setDouble(6,Utilidades.convertirADouble(mapa.get("valor_concepto_"+i)+""));
	                    
	                    finTransaccion=ps.executeUpdate()>0;
	                }
	            }
	            UtilidadBD.finalizarTransaccion(con);
	            return 1;
	        }
	        else
	        {
	            UtilidadBD.abortarTransaccion(con);
	        }
        }
        catch (SQLException e)
        {
            logger.error("Error en la inserción de la aplicación y conceptos de pago de capitación (SqlBasePagosCapitacionDao)"+e.toString());
            e.printStackTrace();
        }finally{
			try{
				if(ps!=null){
					ps.close();					
				}
			}catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SQLBaseNivelAutorizacionArticuloEspecificoDAO "+sqlException.toString() );
			}
			
			
		}
        return ConstantesBD.codigoNuncaValido;
	}
	
    /**
     * Metodo para eliminar los conceptos de una aplicacion
     * @param con
     * @param codigoPago
     * @return
     */
    public static int eliminarConceptosAplicacion(Connection con,int codigoPago)
    {
        PreparedStatementDecorator ps=null;
      
        String cadena="DELETE FROM capitacion.conceptos_apli_pagos_cap " +
        							"			WHERE aplica_pagos_capitacion = ?";
        try
        {
            ps= new PreparedStatementDecorator(con.prepareStatement(cadena));
            ps.setInt(1,codigoPago);
            return ps.executeUpdate();
        }
        catch (SQLException e)
        {
        	logger.error("Error eliminando los conceptos de pago de la aplicacion (SqlBasePagosCapitacionDao)"+e.toString());
            e.printStackTrace();
        }finally{
			try{
				if(ps!=null){
					ps.close();					
				}
			}catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SQLBaseNivelAutorizacionArticuloEspecificoDAO "+sqlException.toString() );
			}
			
			
		}
        return ConstantesBD.codigoNuncaValido;
    }
    
    /**
     * Método que actualiza el estado del pago general de empresa
     * @param con
     * @param codigoPago
     * @param codigoNuevoEstado
     * @return
     */
    public static int actualizarEstadoPagoEmpresa(Connection con,int codigoPago,int codigoNuevoEstado)
    {
        PreparedStatementDecorator ps=null;
        try
        {
            String cadena="UPDATE cartera.pagos_general_empresa SET estado = ? WHERE codigo = ?";
            
            ps= new PreparedStatementDecorator(con.prepareStatement(cadena));
            ps.setInt(1, codigoNuevoEstado);
            ps.setInt(2, codigoPago);
            return ps.executeUpdate();
        }
        catch(SQLException e)
        {
            logger.error("Se produjo un error actualizando el estado del pago del documento (SqlBasePagosCapitacionDao)"+e.toString());
            e.printStackTrace();
        }finally{
			try{
				if(ps!=null){
					ps.close();					
				}
			}catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SQLBaseNivelAutorizacionArticuloEspecificoDAO "+sqlException.toString() );
			}
			
			
		}
        return 0;
    }
    
    /**
	 *  Método que realiza la anulación de la aplicación del pago
	 * @param con
	 * @param mapa
	 * @return
	 */
	public static int anularAplicacionPago(Connection con, HashMap mapa)
	{
		boolean finTransaccion=true;
        util.UtilidadBD.iniciarTransaccion(con);
        finTransaccion=(actualizarEstadoAplicacionPago(con, Utilidades.convertirAEntero(mapa.get("codigo_aplicacion")+""), ConstantesBD.codigoEstadoAplicacionPagosAnulado)>0);
        if(finTransaccion)
        {
            finTransaccion=(eliminarConceptosAplicacion(con, Utilidades.convertirAEntero(mapa.get("codigo_aplicacion")+""))>0);
            if(finTransaccion)
            {
                finTransaccion=(eliminarAplicacionCxCCapitacion(con,Utilidades.convertirAEntero(mapa.get("codigo_aplicacion")+""))>0);
            }
        }
        if(finTransaccion)
        {
        	PreparedStatementDecorator ps = null;
        	ResultSetDecorator rs = null; 
            try
            {
            	//---------------Se inserta la anulación de pago de capitación ----------//
                String cadena="INSERT INTO capitacion.anulacion_apli_pagos_cap (" +
                		"aplica_pagos_capitacion," +
                		"usuario," +
                		"motivo," +
                		"fecha," +
                		"hora) " +
                		"VALUES (?,?,?,?,?)";
                
                ps= new PreparedStatementDecorator(con.prepareStatement(cadena));
                ps.setInt(1,Utilidades.convertirAEntero(mapa.get("codigo_aplicacion")+""));
                ps.setString(2,mapa.get("login_usuario")+"");
                ps.setString(3,mapa.get("motivo")+"" );
                ps.setDate(4,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD( mapa.get("fecha")+"")));
                ps.setString(5,mapa.get("hora")+"");
                finTransaccion=(ps.executeUpdate()>0);
                
                //----------Se consulta si hay aplicaciones en estado aprobada para el documento de pago, si no hay se coloca en estado recaudado
                cadena="SELECT count(1) as aplicaciones " +
                				"		from capitacion.aplicacion_pagos_capitacion " +
                				"			where estado=? and pagos_general_empresa=?";
                
                ps= new PreparedStatementDecorator(con.prepareStatement(cadena));
                ps.setInt(1, ConstantesBD.codigoEstadoAplicacionPagosAprobado);
                ps.setInt(2,Utilidades.convertirAEntero(mapa.get("numero_pago")+""));
                new ResultSetDecorator(ps.executeQuery());
                rs=new ResultSetDecorator(ps.executeQuery());
                if(rs.next())
                {
                	//--------Si no hay aplicaciones en estado aprobado se coloca el estado del documento de pago en estado recaudado
                    if(rs.getInt("aplicaciones")==0)
                    {
                        actualizarEstadoPagoEmpresa(con, Utilidades.convertirAEntero(mapa.get("numero_pago")+""), ConstantesBD.codigoEstadoPagosRecaudado);
                    }
                }
                
            }
            catch (SQLException e)
            {
                finTransaccion=false;
                logger.error("Error ingresando la anulacion de pago de capitación (SqlBasePagosCapitacionDao)"+e.toString());
                e.printStackTrace();
            }finally{
    			try{
    				if(ps!=null){
    					ps.close();					
    				}
    			}catch(SQLException sqlException){
    				logger.warn(sqlException+" Error al cerrar el recurso SQLBaseNivelAutorizacionArticuloEspecificoDAO "+sqlException.toString() );
    			}
    			try{
    				if(rs!=null){
    					rs.close();					
    				}
    			}catch(SQLException sqlException){
    				logger.warn(sqlException+" Error al cerrar el recurso SQLBaseNivelAutorizacionArticuloEspecificoDAO "+sqlException.toString() );
    			}
    			
    			
    		}
        }
        if(finTransaccion)
        {
	        util.UtilidadBD.finalizarTransaccion(con);
	        return 1;
	    }
	    else
	    {
	        util.UtilidadBD.abortarTransaccion(con);
	    }
        return 0;
	}
	
	/**
	 * Método que actualiza el estado de la aplicación del pago empresa
	 * @param con
	 * @param codigoAplicacionPago
	 * @param codigoNuevoEstado
	 * @return
	 */
    public static int actualizarEstadoAplicacionPago(Connection con,int codigoAplicacionPago,int codigoNuevoEstado)
    {
        PreparedStatementDecorator ps=null;
        try
        {
            String cadena="UPDATE capitacion.aplicacion_pagos_capitacion SET estado = ? " +
            							"		WHERE codigo = ?";
            
            
            ps= new PreparedStatementDecorator(con.prepareStatement(cadena));
            ps.setInt(1, codigoNuevoEstado);
            ps.setInt(2, codigoAplicacionPago);
            return ps.executeUpdate();
        }
        catch(SQLException e)
        {
            logger.error("Error actualizando el estado de la aplicación del  pago (SqlBasePagosCapitacionDao)"+e.toString());
            e.printStackTrace();
        }finally{
			try{
				if(ps!=null){
					ps.close();					
				}
			}catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SQLBaseNivelAutorizacionArticuloEspecificoDAO "+sqlException.toString() );
			}
			
			
		}
        return 0;
    }
    
    /**
     * Mètodo que elimina las aplicaciones de pago de cuentas de cobro de capitacion
     * @param con
     * @param codigoAplicacion
     * @return
     */
    private static int eliminarAplicacionCxCCapitacion(Connection con, int codigoAplicacion)
    {
        PreparedStatementDecorator ps=null;
        String cadena="DELETE FROM capitacion.apli_pagos_cxc_capitacion " +
        							"		WHERE aplica_pagos_capitacion = ?";
        try
        {
            ps= new PreparedStatementDecorator(con.prepareStatement(cadena));
            ps.setInt(1,codigoAplicacion);
            return 1;
        }
        catch (SQLException e)
        {
        	logger.error("Error eliminando las aplicaciones de pago de capitación para la cuenta de cobro (SqlBasePagosCapitacionDao)"+e.toString());
            e.printStackTrace();
        }finally{
			try{
				if(ps!=null){
					ps.close();					
				}
			}catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SQLBaseNivelAutorizacionArticuloEspecificoDAO "+sqlException.toString() );
			}
			
			
		}
        return ConstantesBD.codigoNuncaValido;
    }
    
	/**
	 * Método que consulta para la aplicación actual los posibles pagos
	 * por cuentas de cobro
	 * @param con
	 * @param codAplicacionPago
	 * @return
	 */
	public static HashMap consultarPagosCuentaCobro(Connection con, int codAplicacionPago)
	{
		String consultaStr="SELECT " +
    										"		apcxc.aplica_pagos_capitacion as cod_aplicacion," +
    										"		apcxc.cuenta_cobro_capitacion as cxc," +
    										"		cxc.fecha_radicacion as fecha_radicacion," +
    										"		(cxc.valor_inicial_cuenta+cxc.ajustes_debito-cxc.ajustes_credito-cxc.valor_pagos) as saldo_cxc," +
    										"		apcxc.institucion as institucion," +
    										"		apcxc.tipo_pago_capitacion as tipo_pago," +
    										"		convertiranumero(apcxc.valor_pago||'') as valor_pago_cxc," +
    										//"		apcxc.metodo_pago as codmetodopago," +
    										//"		totalValPagoCXC(apcxc.aplica_pagos_capitacion,apcxc.cuenta_cobro_capitacion) as valor_pago_cxc," +
    										/*"		(" +
    										"		SELECT case when sum(apf.valor_pago) is null then '0' else sum(apf.valor_pago) end " +
    										"				from aplicacion_pagos_factura apf " +
    										"					inner join aplicacion_pagos_cxc apc on" +
    										"				(" +
    										"				apf.aplicacion_pagos=apc.aplica_pagos_empresa and " +
    										"				apf.numero_cuenta_cobro=apc.numero_cuenta_cobro" +
    										"				) " +
    										"				where apc.aplica_pagos_empresa=apcxc.aplica_pagos_empresa and apc.numero_cuenta_cobro<>apcxc.numero_cuenta_cobro" +
    										"		) as totalvalotrascxc, " + //el total distribuido de la aplicacion en otras cuentas de cobro.*/
    										"		'true' as bd " +
    										"			from apli_pagos_cxc_capitacion apcxc " +
    										"				inner join cuentas_cobro_capitacion cxc on (apcxc.cuenta_cobro_capitacion=cxc.numero_cuenta_cobro)" +
    										"				inner join aplicacion_pagos_capitacion apc on (apcxc.aplica_pagos_capitacion=apc.codigo)" +
    										"					where apcxc.aplica_pagos_capitacion=?";

		PreparedStatementDecorator ps = null;	
		try
		{
		ps =  new PreparedStatementDecorator(con.prepareStatement(consultaStr));
		
		ps.setInt(1, codAplicacionPago);
		
		HashMap mapaRetorno=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		ps.close();
		return mapaRetorno;
	
		}
		catch (SQLException e)
		{
		logger.error("Error Consultando los pagos realizados a la aplicación por cuentas de cobro:  en SqlBasePagosCapitacionDao"+e.toString());
		return null;
		}finally{
			try{
				if(ps!=null){
					ps.close();					
				}
			}catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SQLBaseNivelAutorizacionArticuloEspecificoDAO "+sqlException.toString() );
			}
			
			
		}
	}
	
	/**
	 * Método que consulta la(s) cuentas de cobro del convenio capitado, que están radicadas y
	 * que la cuenta de cobro tenga saldo mayor a cero
	 * @param con
	 * @param mapa
	 * @return
	 */
	public static HashMap buscarCuentasCobroConvenio(Connection con, HashMap mapa)
	{
		String filtroCuentaCobro="";
		
		//------Se verifica si colocaron numero de cuenta de cobro a buscar-------//
		if(UtilidadCadena.noEsVacio(mapa.get("cuentaCobroBuscar")+""))
			filtroCuentaCobro="AND numero_cuenta_cobro='"+mapa.get("cuentaCobroBuscar")+""+"'";
		
		String consultaStr="SELECT cxc.numero_cuenta_cobro as cxc," +
    										"				cxc.fecha_radicacion as fecha_radicacion," +
    										//"				convertiranumero((cxc.valor_inicial_cuenta+cxc.ajustes_debito-cxc.ajustes_credito-cxc.valor_pagos)||'') as saldo_cxc," +
    										" 				 (cxc.valor_inicial_cuenta+cxc.ajustes_debito-cxc.ajustes_credito-cxc.valor_pagos) as saldo_cxc," +
    										"				'false' as seleccionado" +
    										"	 	from cuentas_cobro_capitacion cxc" +
    										"			where cxc.convenio=? AND cxc.institucion=? " +
    										"					AND (cxc.valor_inicial_cuenta+cxc.ajustes_debito-cxc.ajustes_credito-cxc.valor_pagos) > 0 " + filtroCuentaCobro+ 
    										"					AND getNumCxCAjustesCapi(cxc.numero_cuenta_cobro,cxc.institucion,"+ConstantesBD.codigoEstadoAjusteCxCPendiente+") = 0" +
    										"					AND getNumCxCApliPagosCapi(cxc.numero_cuenta_cobro,cxc.institucion,"+ConstantesBD.codigoEstadoAplicacionPagosPendiente+") = 0" +
    										"					AND cxc.estado=? AND cxc.numero_cuenta_cobro not in ("+mapa.get("cuentas")+")" +
    												"					ORDER BY cxc.numero_cuenta_cobro";

		PreparedStatementDecorator ps = null;	
		try
		{
		ps =  new PreparedStatementDecorator(con.prepareStatement(consultaStr));
		
		 ps.setInt(1,Utilidades.convertirAEntero(mapa.get("convenio")+""));
         ps.setInt(2,Utilidades.convertirAEntero(mapa.get("institucion")+""));
         ps.setInt(3, ConstantesBD.codigoEstadoCarteraRadicada);
		
 		HashMap mapaRetorno=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));

		return mapaRetorno;
	
		}
		catch (SQLException e)
		{
		logger.error("Error Consultando la(s) cuentas de cobro para los pagos:  en SqlBasePagosCapitacionDao"+e.toString());
		return null;
		}finally{
			try{
				if(ps!=null){
					ps.close();					
				}
			}catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SQLBaseNivelAutorizacionArticuloEspecificoDAO "+sqlException.toString() );
			}
			
			
		}
	}
	
	/**
	 * Método que guarda las cuentas de cobro agregadas al pago de la aplicación
     * de capitación
	 * @param con
	 * @param mapa
	 * @return
	 */
	public static int guardarAplicacionPagosCXC(Connection con, HashMap mapa)
	{
		PreparedStatementDecorator psInsert=null;
        PreparedStatementDecorator psUpdate=null;
        boolean finTransaccion=true;
    	PreparedStatementDecorator psDelete= null;
        try
        {
            util.UtilidadBD.iniciarTransaccion(con);           
            
            for(int i=0;i<Integer.parseInt(mapa.get("numRegistros")+"");i++)
            {
                if(UtilidadTexto.getBoolean(mapa.get("bd_"+i)+""))
                {
                    if(UtilidadTexto.getBoolean(mapa.get("eliminado_"+i)+""))
                    {
                        String cadena="DELETE FROM apli_pagos_cxc_capitacion " +
                        							  "			WHERE aplica_pagos_capitacion=? and cuenta_cobro_capitacion=?";
                        
                        
                         psDelete= new PreparedStatementDecorator(con.prepareStatement(cadena));
                        psDelete.setInt(1,Utilidades.convertirAEntero(mapa.get("cod_aplicacion_"+i)+""));
                        psDelete.setDouble(2,Utilidades.convertirADouble(mapa.get("cxc_"+i)+""));
                        psDelete.executeUpdate();
                    }
                    else 
                    {
                    	psUpdate= new PreparedStatementDecorator(con.prepareStatement(cadenaUpdateAplicacionPagosCXC));
                    	
                    	psUpdate.setDouble(1,Utilidades.convertirADouble(mapa.get("valor_pago_cxc_"+i)+""));
	                    psUpdate.setString(2,mapa.get("tipo_pago_"+i)+"");
	                    psUpdate.setInt(3,Utilidades.convertirAEntero(mapa.get("cod_aplicacion_"+i)+""));
	                    psUpdate.setDouble(4,Utilidades.convertirADouble(mapa.get("cxc_"+i)+""));
	                    
	                    psUpdate.execute();
                    }//else	                
                }//if bd
             else
                {
            	 	psInsert= new PreparedStatementDecorator(con.prepareStatement(cadenaInsertAplicacionPagosCXC));
            	 	
                	psInsert.setInt(1,Utilidades.convertirAEntero(mapa.get("cod_aplicacion_"+i)+""));
            	 	psInsert.setDouble(2,Utilidades.convertirADouble(mapa.get("cxc_"+i)+""));
            	 	psInsert.setInt(3,Utilidades.convertirAEntero(mapa.get("institucion_"+i)+""));
            	 	psInsert.setDouble(4,Utilidades.convertirADouble(mapa.get("valor_pago_cxc_"+i)+""));
            	 	psInsert.setString(5,mapa.get("tipo_pago_"+i)+"");
            	 	
            	 	psInsert.execute();
                }//else
            }//for
        }
        catch (SQLException e)
        {
            logger.error("Se produjo un error guardando las aplicaciones de pagos de capitación "+e);
            e.printStackTrace();
            finTransaccion=false;
        }finally{
			try{
				if(psDelete!=null){
					psDelete.close();					
				}
			}catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SQLBaseNivelAutorizacionArticuloEspecificoDAO "+sqlException.toString() );
			}
			try{
				if(psInsert!=null){
					psInsert.close();					
				}
			}catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SQLBaseNivelAutorizacionArticuloEspecificoDAO "+sqlException.toString() );
			}
			try{
				if(psUpdate!=null){
					psUpdate.close();					
				}
			}catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SQLBaseNivelAutorizacionArticuloEspecificoDAO "+sqlException.toString() );
			}
			
			
		}
        
        if(finTransaccion)
        {
            util.UtilidadBD.finalizarTransaccion(con);
            return 1;
        }
        util.UtilidadBD.abortarTransaccion(con);
        return ConstantesBD.codigoNuncaValido;
	}
	
//	----------------------------------------------------------------- APROBACIÒN DE PAGOS ------------------------------------------------------------------------------//
	
	/**
	 * Método que realiza la búsqueda de las aplicaciones de pago de capitación pendientes
	 * para realizarles la aprobación
	 * @param con
	 * @param tipoDocBusqueda
	 * @param documentoBusqueda
	 * @param consecutivoPagoBusqueda
	 * @param convenioBusqueda
	 * @param institucion
	 * @return
	 */
	public static HashMap busquedaPagosParaAprobar(Connection con, int tipoDocBusqueda, String documentoBusqueda, String consecutivoPagoBusqueda, int convenioBusqueda, int institucion)
	{
		StringBuffer consultaStr=new StringBuffer();
		consultaStr.append("SELECT "+
								    		"				apc.numero_aplicacion as numero_aplicacion," +
								    		"				apc.codigo as codigo_aplicacion_pago," +
								    		"				pge.tipo_doc as tipo_documento," +
								    		"				tdp.acronimo as acronimo_tipo_documento," +
								    		"				pge.documento as documento," +
								    		"				pge.codigo AS codigo_pago," +
								    		"				pge.fecha_documento as fecha," +
								    		"				pge.convenio as codigo_convenio," +
								    		"				pge.valor as valor_inicial_documento," +
								    		"				getnombreconvenio(pge.convenio) as nombre_convenio," +
								    		"				convertiranumero(''||(getTotalConceptosApliPagosCapi(apc.codigo, "+ConstantesBD.codigoTipoConceptoMayorValor+","+ConstantesBD.codigoEstadoAplicacionPagosPendiente+")" +
								    		"				 -  getTotalConceptosApliPagosCapi(apc.codigo, "+ConstantesBD.codigoTipoConceptoMenorValor+","+ConstantesBD.codigoEstadoAplicacionPagosPendiente+"))) as valor_conceptos," +
								    		"				getTotalVlrCxCApliPagosCapi(apc.codigo, "+ConstantesBD.codigoEstadoAplicacionPagosPendiente+") as valor_pagos," +
								    		"				apc.observaciones as observaciones," +
								    		"				apc.fecha_aplicacion as fecha_aplicacion," +
								    		"				apc.estado as estado," +
								    		"				getDescEstadoApliPagos(apc.estado) as descripcion_estado," +
								    		"				 apc.fecha_aplicacion AS fecha_elaboracion" +
								    		"		FROM aplicacion_pagos_capitacion apc " +
								    		"				inner join pagos_general_empresa pge on (pge.codigo=apc.pagos_general_empresa) " +
								    		"				inner join tipos_doc_pagos tdp on(tdp.codigo=pge.tipo_doc)" +
								    		"		WHERE apc.estado="+ConstantesBD.codigoEstadoAplicacionPagosPendiente+
								    		"						AND pge.institucion="+institucion);
		
		if (tipoDocBusqueda!=ConstantesBD.codigoNuncaValido)
		{
			consultaStr.append("	AND pge.tipo_doc="+tipoDocBusqueda);
		}
		
		if (UtilidadCadena.noEsVacio(documentoBusqueda))
		{
			consultaStr.append("	AND pge.documento="+documentoBusqueda);
		}
		
		if(UtilidadCadena.noEsVacio(consecutivoPagoBusqueda))
		{
			consultaStr.append("	AND apc.numero_aplicacion="+consecutivoPagoBusqueda);
		}
		
		if(convenioBusqueda!=ConstantesBD.codigoNuncaValido)
		{
			consultaStr.append(" AND pge.convenio="+convenioBusqueda);
		}
		
		consultaStr.append("	ORDER BY pge.documento");
		PreparedStatementDecorator ps= null;
		try
		{
		ps =  new PreparedStatementDecorator(con.prepareStatement(consultaStr.toString()));
				
		HashMap mapaRetorno=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
	
		return mapaRetorno;
	
		}
		catch (SQLException e)
		{
		logger.error("Error en busquedaPagosParaAprobar:  en SqlBasePagosCapitacionDao"+e.toString());
		return null;
		}finally{
			try{
				if(ps!=null){
					ps.close();					
				}
			}catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SQLBaseNivelAutorizacionArticuloEspecificoDAO "+sqlException.toString() );
			}
			
			
		}
	}
	
	/**
	 *  Método que calcula el nuevo saldo del documento 
	 * NuevoSaldo= Saldo anterior + conceptos tipo mayor valor ? conceptos tipo menor valor ? valor pago 
	 * @param con
	 * @param valorInicialDocumento
	 * @param codigoPago
	 * @param codigoAplicacion
	 * @return
	 */
	public static double calcularNuevoSaldoDocumento(Connection con, float valorInicialDocumento, int codigoPago, int codigoAplicacion)
	{
		ResultSetDecorator rs=null;
		PreparedStatementDecorator pst= null;
    	try
		{
    		String consultaStr="SELECT (" +
    															"	("+valorInicialDocumento+
    															"	+ getvalconceptosaplipagoscapi("+codigoPago+","+ConstantesBD.codigoTipoConceptoMayorValor+","+ConstantesBD.codigoEstadoAplicacionPagosAprobado+")"+
    															"	- getvalconceptosaplipagoscapi("+codigoPago+","+ConstantesBD.codigoTipoConceptoMenorValor+","+ConstantesBD.codigoEstadoAplicacionPagosAprobado+")"+
    															"	- getValorPagosAplicadosCxCCapi("+codigoPago+"," + ConstantesBD.codigoEstadoAplicacionPagosAprobado+") " +
    															"	)" +
    															"	+ getTotalConceptosApliPagosCapi("+codigoAplicacion+","+ConstantesBD.codigoTipoConceptoMayorValor+","+ConstantesBD.codigoEstadoAplicacionPagosPendiente+")" +
    															"	- getTotalConceptosApliPagosCapi("+codigoAplicacion+","+ConstantesBD.codigoTipoConceptoMenorValor+","+ConstantesBD.codigoEstadoAplicacionPagosPendiente+")" +
    															"	- getTotalVlrCxCApliPagosCapi("+codigoAplicacion+","+ConstantesBD.codigoEstadoAplicacionPagosPendiente+")"+
    															"  ) AS nuevo_saldo";
    		
    		pst= new PreparedStatementDecorator(con.prepareStatement(consultaStr));
    		
    		
    		rs=new ResultSetDecorator(pst.executeQuery());
    		if(rs.next())
    		{	
    			if(rs.getString("nuevo_saldo")!=null&&!rs.getString("nuevo_saldo").equals(""))
				{
    				return rs.getDouble("nuevo_saldo");
				}
    			else
    			{
    				return ConstantesBD.codigoNuncaValidoDouble;
    			}
    		}
    		else
    		{
    			return ConstantesBD.codigoNuncaValidoDouble;
    		}
			
		}
    	catch(SQLException e)
		{
    		logger.error("Error en calcularNuevoSaldoDocumento en SqlBasePagosCapitacionDao : "+e);
    		return ConstantesBD.codigoNuncaValidoDouble;
		}finally{
			try{
				if(pst!=null){
					pst.close();					
				}
			}catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SQLBaseNivelAutorizacionArticuloEspecificoDAO "+sqlException.toString() );
			}
			try{
				if(rs!=null){
					rs.close();					
				}
			}catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SQLBaseNivelAutorizacionArticuloEspecificoDAO "+sqlException.toString() );
			}
			
			
		}
	}
	
    /**
     * Mètodo que inserta la aprobación de la aplicación de pago de capitación
     * @param con Connection
     * @param codAplicacion int
     * @param usuario String
     * @param fechaA string
     */
    public static boolean insertarAprobacionPago(Connection con,int codAplicacion,String usuario,String fechaAprobacion)
    {
    	PreparedStatementDecorator ps = null;
        try
        {
            
            String insertarStr="INSERT INTO aproba_apli_pagos_capi" +
																		 "(aplica_pagos_capitacion," +
																		 "usuario," +
																		 "fecha_aprobacion," +
																		 "fecha_grabacion," +
																		 "hora_grabacion) " +
																		 "VALUES(?,?,?,?,?)";
            
                                    
            ps= new PreparedStatementDecorator(con.prepareStatement(insertarStr));            
            ps.setInt(1,codAplicacion);
            ps.setString(2, usuario);
            ps.setDate(3, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(fechaAprobacion)));
            ps.setDate(4, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual())));
            ps.setString(5, UtilidadFecha.getHoraActual());
                        
            int r=ps.executeUpdate();
            
            if(r>0)
                return true;
            else
                return false;
        }
        catch(SQLException e)
	    {
	        logger.warn(e+"Error en insertarAprobacionPago SqlBasePagosCapitacionDao"+e.toString() );
		   return false;
	    }    finally{
			try{
				if(ps!=null){
					ps.close();					
				}
			}catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SQLBaseNivelAutorizacionArticuloEspecificoDAO "+sqlException.toString() );
			}
			
			
		}
    }
	
	
	/**
	 * Método que realiza el proceso de aprobación del pago
	 * @param con
	 * @param mapa
	 * @return
	 */
	public static int guardarAprobacionAplicacionPago(Connection con, HashMap mapa)
	{
		 boolean finTransaccion=true;
		
		  try
	        {
	            util.UtilidadBD.iniciarTransaccion(con);
	            
	            finTransaccion=insertarAprobacionPago(con, Utilidades.convertirAEntero(mapa.get("codigo_aplicacion")+""), mapa.get("usuario")+"", mapa.get("fecha_aprobacion")+"");
	            
	            if(finTransaccion)
	            {
	            	finTransaccion=(actualizarEstadoAplicacionPago(con, Utilidades.convertirAEntero(mapa.get("codigo_aplicacion")+""), ConstantesBD.codigoEstadoAplicacionPagosAprobado)>0);
	            	
	            	if(finTransaccion)
	            	{
	            		//--------------Se actualiza el valor del a las cuentas de cobro de capitación que se agregaron al pago-----//
	            		 for(int i=0;i<Utilidades.convertirAEntero(mapa.get("numRegistros")+"");i++)
		     	            {
		            			 finTransaccion=(actualizarVlrPagoYSaldoCuentaCobro(con, Utilidades.convertirAEntero(mapa.get("cuenta_cobro_capitacion_"+i)+""), Utilidades.convertirAEntero(mapa.get("institucion_"+i)+""), mapa.get("valor_pago_"+i)+"" )>0);
		            			 
		            			 if(!finTransaccion)
		            				 break;
		     	            }//for
	            	}
	            }
	            
	        }
		  catch (Exception e)
	        {
	            logger.error("Se produjo un error en guardarAprobacionAplicacionPago (SqlBasePagosCapitacionDao) "+e);
	            e.printStackTrace();
	            finTransaccion=false;
	        }
		
		  if(finTransaccion)
	        {
	            util.UtilidadBD.finalizarTransaccion(con);
	            return 1;
	        }
	        util.UtilidadBD.abortarTransaccion(con);
	        return ConstantesBD.codigoNuncaValido;
	}
	
	
	/**
	 * Método que actualiza el valor del pago en la cuenta de cobro de capitación
	 * @param con
	 * @param cuentaCobro
	 * @param institucion
	 * @param valorPago
	 * @return
	 */
	private static int actualizarVlrPagoYSaldoCuentaCobro(Connection con, int cuentaCobro, int institucion, String valorPago)
	{
		 PreparedStatementDecorator ps=null;
	        try
	        {
	        	double valorPagoDouble=0;
	        	if(UtilidadCadena.noEsVacio(valorPago))
	        	{
	        		valorPagoDouble=Utilidades.convertirADouble(valorPago);
	        	}
	            String cadena="UPDATE capitacion.cuentas_cobro_capitacion " +
	            							"		SET valor_pagos = ?, saldo_cuenta=(saldo_cuenta-?) " +
	            							"			WHERE numero_cuenta_cobro = ? AND institucion = ?";
	            
	            
	            ps= new PreparedStatementDecorator(con.prepareStatement(cadena));
	            ps.setDouble(1, valorPagoDouble);
	            ps.setDouble(2, valorPagoDouble);
	            ps.setDouble(3, Utilidades.convertirADouble(cuentaCobro+""));
	            ps.setInt(4, institucion);
	            
	            return ps.executeUpdate();
	        }
	        catch(SQLException e)
	        {
	            logger.error("Se produjo un error en actualizarVlrPagoCuentaCobro (SqlBasePagosCapitacionDao)"+e.toString());
	            e.printStackTrace();
	        }finally{
				try{
					if(ps!=null){
						ps.close();					
					}
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SQLBaseNivelAutorizacionArticuloEspecificoDAO "+sqlException.toString() );
				}
				
				
			}
	        return 0;
	}

	/**
	 * Método que consulta el detalle de pagos CxC  de la aplicación de pagos, 
	 * las cuentas de cobro agregadas al pago
	 * @param con
	 * @param codigoAplicacionPago
	 * @return
	 */
	public static HashMap consultarPagosCxCAplicacion(Connection con, int codigoAplicacionPago)
	{
		String consultaStr="SELECT aplica_pagos_capitacion AS aplica_pagos_capitacion,  " +
											"				cuenta_cobro_capitacion AS cuenta_cobro_capitacion, " +
											"				institucion AS institucion, " +
											"				convertiranumero(valor_pago||'') AS valor_pago" +
											"		FROM capitacion.apli_pagos_cxc_capitacion" +
											"			WHERE aplica_pagos_capitacion=?" +
											"				ORDER BY cuenta_cobro_capitacion";
		PreparedStatementDecorator ps= null;
		try
		{
		ps= new PreparedStatementDecorator(con.prepareStatement(consultaStr));
		ps.setInt(1,codigoAplicacionPago);
		
		HashMap mapaRetorno=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
	
		return mapaRetorno;

		}
		catch (SQLException e)
		{
		logger.error("Error en consularPagosCxCAplicacion:  en SqlBasePagosCapitacionDao"+e.toString());
		e.printStackTrace();
		return null;
		}finally{
			try{
				if(ps!=null){
					ps.close();					
				}
			}catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SQLBaseNivelAutorizacionArticuloEspecificoDAO "+sqlException.toString() );
			}
			
			
		}
	}
	
//	----------------------------------------------------------------- CONSULTA DE  PAGOS ------------------------------------------------------------------------------//
	
	/**
	 * Método que realiza la búsqueda de los pagos de acuerdo a los parámetros
	 * de búsqueda de la consulta
	 * @param con
	 * @param tipoDocBusqueda
	 * @param documentoBusqueda
	 * @param consecutivoPagoBusqueda
	 * @param estadoPagoBusqueda
	 * @param convenioBusqueda
	 * @param institucion
	 * @return
	 */
	public static HashMap busquedaPagosConsulta(Connection con, int tipoDocBusqueda, String documentoBusqueda, String consecutivoPagoBusqueda, int estadoPagoBusqueda, int convenioBusqueda, int institucion)
	{
		StringBuffer consultaStr=new StringBuffer();
		consultaStr.append("SELECT "+
								    		"				apc.numero_aplicacion as numero_aplicacion," +
								    		"				apc.codigo as codigo_aplicacion_pago," +
								    		"				pge.tipo_doc as tipo_documento," +
								    		"				tdp.acronimo as acronimo_tipo_documento," +
								    		"				pge.documento as documento," +
								    		"				pge.codigo AS codigo_pago," +
								    		"				pge.fecha_documento as fecha," +
								    		"				pge.convenio as codigo_convenio," +
								    		"				pge.valor as valor_inicial_documento," +
								    		"				getnombreconvenio(pge.convenio) as nombre_convenio," +
								    		"				convertiranumero(''||(getTotalConceptosApliPagosCapi(apc.codigo, "+ConstantesBD.codigoTipoConceptoMayorValor+","+ConstantesBD.codigoNuncaValido+")" +
								    		"				 -  getTotalConceptosApliPagosCapi(apc.codigo, "+ConstantesBD.codigoTipoConceptoMenorValor+","+ConstantesBD.codigoNuncaValido+"))) as valor_conceptos," +
								    		"				getTotalVlrCxCApliPagosCapi(apc.codigo, "+ConstantesBD.codigoNuncaValido+") as valor_pagos," +
								    		"				apc.observaciones as observaciones," +
								    		"				apc.fecha_aplicacion as fecha_aplicacion," +
								    		"				apc.estado as estado," +
								    		"				getDescEstadoApliPagos(apc.estado) as descripcion_estado," +
								    		"				 apc.fecha_aplicacion AS fecha_elaboracion" +
								    		"		FROM aplicacion_pagos_capitacion apc " +
								    		"				inner join pagos_general_empresa pge on (pge.codigo=apc.pagos_general_empresa) " +
								    		"				inner join tipos_doc_pagos tdp on(tdp.codigo=pge.tipo_doc)" +
								    		"		WHERE pge.institucion="+institucion);
		
		if (tipoDocBusqueda!=ConstantesBD.codigoNuncaValido)
		{
			consultaStr.append("	AND pge.tipo_doc="+tipoDocBusqueda);
		}
		
		if (UtilidadCadena.noEsVacio(documentoBusqueda))
		{
			consultaStr.append("	AND pge.documento="+documentoBusqueda);
		}
		
		if(UtilidadCadena.noEsVacio(consecutivoPagoBusqueda))
		{
			consultaStr.append("	AND apc.numero_aplicacion="+consecutivoPagoBusqueda);
		}
		
		if(estadoPagoBusqueda!=ConstantesBD.codigoNuncaValido)
		{
			consultaStr.append(" AND apc.estado="+estadoPagoBusqueda);
		}
		
		if(convenioBusqueda!=ConstantesBD.codigoNuncaValido)
		{
			consultaStr.append(" AND pge.convenio="+convenioBusqueda);
		}
		
		consultaStr.append("	ORDER BY pge.documento");
		PreparedStatementDecorator ps= null;
		try
		{
		 ps =  new PreparedStatementDecorator(con.prepareStatement(consultaStr.toString()));
				
		HashMap mapaRetorno=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
	
		return mapaRetorno;
	
		}
		catch (SQLException e)
		{
		logger.error("Error en busquedaPagosConsulta:  en SqlBasePagosCapitacionDao"+e.toString());
		return null;
		}finally{
			try{
				if(ps!=null){
					ps.close();					
				}
			}catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SQLBaseNivelAutorizacionArticuloEspecificoDAO "+sqlException.toString() );
			}
		}
	}
	
	/**
	 * Método que consulta los conceptos de pago del documento de pago
	 * que están en estado aprobado, para mostrar en el listado de conceptos pero
	 * sólo serán de consulta
	 * @param con
	 * @param codigoDocumentoPago
	 * @return
	 */
	public static HashMap consultarConceptosPagoAprobadosDoc(Connection con, int codigoDocumentoPago)
	{
		String consultaStr="SELECT cap.concepto_pagos as codigo_concepto," +
											"				cp.descripcion as descripcion_concepto," +
											"				cp.tipo as tipo_concepto," +
											"				convertiranumero(cap.valor_base||'') as valor_base," +
											"				convertiranumero(cap.valor_concepto||'') as valor_concepto," +
											"				cap.porcentaje as porcentaje" +
											//"				'true' as bd " +
											"		FROM capitacion.conceptos_apli_pagos_cap cap " +
											"			INNER JOIN cartera.concepto_pagos cp on(cp.codigo=cap.concepto_pagos)" +
											"			INNER JOIN capitacion.aplicacion_pagos_capitacion apc ON (cap.aplica_pagos_capitacion=apc.codigo)" +
											"				WHERE apc.pagos_general_empresa=? AND apc.estado="+ConstantesBD.codigoEstadoAplicacionPagosAprobado;

		
		PreparedStatementDecorator ps= null;
		try
		{
		 ps= new PreparedStatementDecorator(con.prepareStatement(consultaStr));
		ps.setInt(1,codigoDocumentoPago);
		
		HashMap mapaRetorno=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
	
		return mapaRetorno;

		}
		catch (SQLException e)
		{
		logger.error("Error en consultarConceptosPagoAprobadosDoc:  en SqlBasePagosCapitacionDao"+e.toString());
		e.printStackTrace();
		return null;
		}finally{
			try{
				if(ps!=null){
					ps.close();					
				}
			}catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SQLBaseNivelAutorizacionArticuloEspecificoDAO "+sqlException.toString() );
			}
			
			
		}
	}
	

}
