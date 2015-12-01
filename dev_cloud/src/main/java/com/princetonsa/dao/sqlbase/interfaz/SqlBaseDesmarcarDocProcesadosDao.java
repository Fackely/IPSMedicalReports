package com.princetonsa.dao.sqlbase.interfaz;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMessage;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.interfaces.ConstantesIncosistenciasInterfaz;

import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.interfaz.DtoDocumentoDesmarcar;
import com.princetonsa.dto.interfaz.DtoLogInterfaz1E;
import com.princetonsa.mundo.UsuarioBasico;

public class SqlBaseDesmarcarDocProcesadosDao {
	
	private static Logger logger = Logger.getLogger(SqlBaseDesmarcarDocProcesadosDao.class);
	
	
	/**
	 * Cadena Sql que realiza la consulta de los tipos de Documentos asociados a un tipo de Movimiento 
	 */
	private static String consultarTiposDoc1eXMvto=	"SELECT " +
						 "td.codigo AS codigo, " +
						 "td.nombre AS nombre " +
						 "FROM " +
						 "tipos_documento_1e td " +
						 "INNER JOIN tipos_doc_x_tipo_mov_1e tdxmov ON (tdxmov.tipo_documento = td.codigo) " +
						 "WHERE tdxmov.tipo_movimiento = ? ";
	
	
	/**
	 * Cadena Sql que inserta los datos de la desmarcacion de los un documento
	 */
	private static String insertarLogInterfaz1EStr= "INSERT INTO  log_interfaz_1e  ( " +
			              "consecutivo, " +     //1
			              "usuario_procesa, " + //2
			              "fecha_generacion, " +//--
			              "hora_generacion, " + //--
			              "tipo_movimiento, " + //3
			              "fecha_proceso, " +   //4
			              "institucion, " +     //5
			              "tipo_proceso, " +    //6
			              "fecha_ini_desmarcacion, " +//7
			              "fecha_fin_desmarcacion, " +//8
			              "motivo_desmarcacion, " + //9
			              "path, " +        //10
			              "nombre_archivo, " +//11
			              "tipo_archivo ) " +//12
			              "VALUES (?, ?, CURRENT_DATE, "+ValoresPorDefecto.getSentenciaHoraActualBD()+", ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ) ";
	
	
	/**
	 * Cadena Sql que inserta los datos de los tipos de documentos 1E
	 */
	private static String insertarLogInterfTiposDoc1EStr= "INSERT INTO log_interfaz_tipos_doc_1e ( " +
						"log_interfaz, " +
						"tipo_documento ) " +
						"VALUES (?, ?) ";
	
	
	
//***************** INICIO Desmarcar Ventas y Honorarios Internos  ******************************************************************
	/**
	 * Cadena Sql que actualiza la tabla facturas 
	 */
	private static String demarcarFacturasPacientes= "UPDATE facturas SET contabilizado = '"+ConstantesBD.acronimoNo+"'  WHERE  contabilizado = '"+ConstantesBD.acronimoSi+"' AND " +
			             "estado_facturacion IN ("+ConstantesBD.codigoEstadoFacturacionFacturada+", "+ConstantesBD.codigoEstadoFacturacionAnulada +") ";
			              
    /**
     * Cadena Sql que actualiza la tabla facturas
     */
	private static String desmarcarAnulacionFacturasPacientes="UPDATE anulaciones_facturas  SET contabilizado = '"+ConstantesBD.acronimoNo+"'  WHERE  contabilizado = '"+ConstantesBD.acronimoSi+"' AND " +
			              "1=1 "; 
	
	/**
	 * Cadena Sql que actualiza la tabla facuras_varias
	 */
	private static String desmarcarFacturasVarias="UPDATE facturas_varias  SET contabilizado = '"+ConstantesBD.acronimoNo+"'  WHERE  contabilizado = '"+ConstantesBD.acronimoSi+"' AND " ;
                           
	/**
	 * Cadena Sql que actuliza la tabla ajus_facturas_varias 
	 */
	private static String desmarcarAjustesFacturasVarias ="UPDATE ajus_facturas_varias SET contabilizado = '"+ConstantesBD.acronimoNo+"' WHERE  contabilizado = '"+ConstantesBD.acronimoSi+"' AND " +
	        		       "estado = '"+ConstantesIntegridadDominio.acronimoEstadoAprobado+"' "; 
	
	/**
	 * Cadena Sql que actualiza la tabla cuentas_cobro_capitacion
	 */
	private static String desmarcarCuentasCobroCapitacion ="UPDATE cuentas_cobro_capitacion SET contabilizado = '"+ConstantesBD.acronimoNo+"' WHERE  contabilizado = '"+ConstantesBD.acronimoSi+"' AND "+
	                     "estado = "+ConstantesBD.codigoEstadoCarteraRadicada+" "; 
	
	/**
	 * Cadena Sql que actualiza la tabla ajustes_cxc
	 */
	private static String desmarcarAjustesCuentasCobroCapitacion= "UPDATE ajustes_cxc SET contabilizado = '"+ConstantesBD.acronimoNo+"' WHERE  contabilizado = '"+ConstantesBD.acronimoSi+"' AND " +
	                      " codigo = ? "; 
//********************** FIN Desmarcar Ventas y Honorarios Internos*********************************************************	

	
// *************** INICIO Desmarcar Recaudos ******************************************************************************
	
	/**
	 * Cadena Sql que actualiza la tabla recibos_caja
	 */
	private static String desmarcarRecibosdeCaja="UPDATE recibos_caja SET contabilizado = '"+ConstantesBD.acronimoNo+"' WHERE  contabilizado = '"+ConstantesBD.acronimoSi+"' AND " +
						  "estado IN ( "+ConstantesBD.codigoEstadoReciboCajaRecaudado+","+ConstantesBD.codigoEstadoReciboCajaEnArqueo+","+ConstantesBD.codigoEstadoReciboCajaEnCierre+","+ConstantesBD.codigoEstadoReciboCajaAnulado+" ) ";
	
	/**
	 * Cadena Sql que actualiza la tabla anulacion_recibos_caja
	 */
	private static String demarcarAnulacionRecibosdeCaja= "UPDATE anulacion_recibos_caja SET contabilizado = '"+ConstantesBD.acronimoNo+"' WHERE  contabilizado = '"+ConstantesBD.acronimoSi+"' AND ";
	
	/**
	 * Cadena Sql que actualiza la tabla devol_recibos_caja
	 */
	private static String desmarcarDevolucionRecibosCaja = "UPDATE devol_recibos_caja SET contabilizado = '"+ConstantesBD.acronimoNo+"' WHERE  contabilizado = '"+ConstantesBD.acronimoSi+"' AND " +
			              "estado = '"+ConstantesIntegridadDominio.acronimoEstadoFacturaAprobada+ "'  ";
	
// *************** FIN Desmarcar Recaudos ******************************************************************************
	
	
	
// *************** INICIO Desmarcar Ajustes y Reclasificaciones ******************************************************************************	
    
	/**
	 * Cadena Sql que actualiza la tabla ajustes_empresa
	 */
	private static String desmarcarAjustesFacuturasPacientes="UPDATE ajustes_empresa SET contabilizado = '"+ConstantesBD.acronimoNo+"' WHERE contabilizado = '"+ConstantesBD.acronimoSi+"' AND " +
			              "codigo = ? ";
	/**
	 * Cadena Sql que actualiza la tabla registro_glosas
	 */
	private static String desmarcarRegistroGlosas="UPDATE registro_glosas SET contabilizado = '"+ConstantesBD.acronimoNo+"' WHERE contabilizado = '"+ConstantesBD.acronimoSi+"' AND " +
			              "estado IN ('"+ConstantesIntegridadDominio.acronimoEstadoGlosaAprobada+ "', '"+ConstantesIntegridadDominio.acronimoEstadoGlosaRespondida+ "', '"+ConstantesIntegridadDominio.acronimoEstadoGlosaConciliada+ "') ";
	
// *************** FIN Desmarcar Ajustes y Reclasificaciones ******************************************************************************	


	
// *************** INICIO Desmarcar Servicios Entidades Externas ******************************************************************************
    /**
     * Cadena Sql que actualiza la tabla Autorizaciones entidades Subcontratadas 
     */
	private static String desmarcarRegAutorSerEntidadesSub= "UPDATE autorizaciones_entidades_sub SET contabilizado = '"+ConstantesBD.acronimoNo+"' WHERE contabilizado = '"+ConstantesBD.acronimoSi+"' AND " +
			              "tipo = '"+ConstantesIntegridadDominio.acronimoExterna+"' ";
	
	
	/**
	 * Cadena Sql que actualiza la tabla despacho
	 */
	private static String desmarcarDespachoMedicamentos=" UPDATE despacho SET contabilizado = '"+ConstantesBD.acronimoNo+"' WHERE contabilizado = '"+ConstantesBD.acronimoSi+"' AND " +
			              "orden = ? " ;
	
	
	/**
	 * Cadena Sql que actualiza la tabla devolucion medicamentos
	 */
	private static String desmarcarDevolucionMedicamentos= "UPDATE devolucion_med SET contabilizado = '"+ConstantesBD.acronimoNo+"' WHERE contabilizado = '"+ConstantesBD.acronimoSi+"' AND " +
			              "codigo = ? ";
	
	/**
	 * Cadena Sql que actualiza la tabla despacho pedidos
	 */
	private static String desmarcarDespachoPedidos= "UPDATE despacho_pedido SET contabilizado = '"+ConstantesBD.acronimoNo+"' WHERE contabilizado = '"+ConstantesBD.acronimoSi+"' AND " +
			              "pedido = ? " ;
	
	/**
	 * Cadena Sql que acutualiza la tabla devolucion pedidos
	 */
	private static String desmarcarDevolucionPedidos ="UPDATE devolucion_pedidos SET contabilizado = '"+ConstantesBD.acronimoNo+"' WHERE  contabilizado = '"+ConstantesBD.acronimoSi+"' AND " +
			              "codigo = ? ";
	
	
	/**
	 * Cadena Sql que actualiza la tabla despacho
	 */
	private static String demarcarCargosDirectosArticulos = "UPDATE despacho SET contabilizado = '"+ConstantesBD.acronimoNo+"' WHERE  contabilizado = '"+ConstantesBD.acronimoSi+"' AND " +
			              "orden = ? ";
	
	
	/**
	 * Cadena Sql que actualiza la tabla anulacion cargos farmacia
	 */
	private static String desmarcarAnulacionCargosArticulos= "UPDATE anulacion_cargos_farmacia SET contabilizado = '"+ConstantesBD.acronimoNo+"' WHERE contabilizado = '"+ConstantesBD.acronimoSi+"' AND " +
			              "codigo = ? ";
	
	
	
	
	
	
// *************** FIN Desmarcar Servicios Entidades Externas ******************************************************************************
	
	/**
	 * Metodo para consultar los Docuemetos Asociados a un tipo de Movimiento
	 * @return
	 */
	public static ArrayList<DtoDocumentoDesmarcar> consultarDocumentosXtipoMvto(String tipoMov)
	{
		ArrayList<DtoDocumentoDesmarcar> docsMovto= new ArrayList<DtoDocumentoDesmarcar>();
		String cadena= consultarTiposDoc1eXMvto;
		Connection con = null;
		con = UtilidadBD.abrirConexion();
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1,tipoMov);
			
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			
			while(rs.next())
			{
				DtoDocumentoDesmarcar dto =new DtoDocumentoDesmarcar();
				dto.setCodigo(rs.getString("codigo"));
			    dto.setNombre(rs.getString("nombre"));
				dto.setDesmarcado(ValoresPorDefecto.getValorFalseCortoParaConsultas());
				docsMovto.add(dto);
			}
			ps.close();
			rs.close();
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
		UtilidadBD.closeConnection(con);
		
		return docsMovto;
	}
	
	/**
	 * Metodo para realizar el proceso de desmarcacion (contabilizado='N') de la Tabla facturas 
	 * @param con
	 * @param parametros
	 * @return
	 */
	public static HashMap desmarcarFacturasPacientes(Connection con, HashMap parametros){
		
		ActionErrors errores = new ActionErrors();
		HashMap resultado = new HashMap();
		String cadenaModificacion = demarcarFacturasPacientes;
		cadenaModificacion+="AND to_char(fecha,'YYYY-MM-DD') BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(parametros.get("fechaInicialDesmaracion").toString())+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(parametros.get("fechaFinalDesmarcacion").toString())+"' ";
		resultado.put("estadoCont","");
		int actualizacion=0;
		resultado.put("actualizo",ConstantesBD.acronimoSi);
	 
		try {	   		 
	   		 PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadenaModificacion,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
             actualizacion = ps.executeUpdate();  

	   		 if(actualizacion >= 0 ) {
	   			logger.info("\n \n Cadena Modificacion Facturas Pacientes >>  cadena >>  "+cadenaModificacion+" ");
            	 resultado.put("estadoCont",ConstantesBD.acronimoSi);	    
            	 
            	 if(actualizacion==0)
            	 {
            		 resultado.put("actualizo",ConstantesBD.acronimoNo);
            	 }
    	    	 		        	
	   		 }
	   		ps.close();
	   		 
		 }
		catch (Exception e) {			
			 e.printStackTrace();
			 logger.info("\n \n error al  Modificar Facturas Pacientes>>  cadena >>  "+cadenaModificacion+" ");
			 errores.add("descripcion",new ActionMessage("errors.notEspecific","Se presentaron Errores en la Desmarcación de Facturas Pacientes"));   	 
	    	 resultado.put("error",errores);
		 }   		 
	   		
		 return resultado;
	}
	
	
	/**
	 * Metodo para realizar el proceso de desmarcacion (contabilizado='N') de la Tabla anulaciones_facturas
	 * @param con
	 * @param parametros
	 * @return
	 */
	public static HashMap desmarcarAnulacionFacturasPacientes(Connection con, HashMap parametros)
	{
		ActionErrors errores = new ActionErrors();
		HashMap resultado = new HashMap();
		String cadenaModificacion = desmarcarAnulacionFacturasPacientes;
		cadenaModificacion+="AND to_char(fecha_grabacion,'YYYY-MM-DD') BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(parametros.get("fechaInicialDesmaracion").toString())+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(parametros.get("fechaFinalDesmarcacion").toString())+"' ";
		resultado.put("estadoCont","");	
		int actualizacion=0;
		resultado.put("actualizo",ConstantesBD.acronimoSi);
	 
		try{
	   		 
	   		 PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadenaModificacion,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));   		 
	   		 actualizacion = ps.executeUpdate();  

	   		 if(actualizacion >= 0 ) 
	   		 {	   			
           	  resultado.put("estadoCont",ConstantesBD.acronimoSi);	    
           	 
	           	 if(actualizacion==0)
	           	 {
	           		 resultado.put("actualizo",ConstantesBD.acronimoNo);
	           	 }
	   		 }
	   		 ps.close();
	            
		 }
			catch (Exception e) {			
				 e.printStackTrace();				 
				 logger.info("\n \n error al  Modificar Anulacion Facturas Pacientes>>  cadena >>  "+cadenaModificacion+" ");
				 errores.add("descripcion",new ActionMessage("errors.notEspecific","Se presentaron Errores en la Desmarcación de Anulacion Facturas Pacientes"));
		    	 resultado.put("error",errores);
			 }   		 
	   		 
		 return resultado;
		
	}
	
	/**
	 * Metodo para realizar el proceso de desmarcacion (contabilizado='N') de la Tabla facturas_varias
	 *  Si anulacion = true realiza el proceso de desmarcacion (contabilizado_anulacion = N) de la tabla facturas_varias
	 * @param con
	 * @param parametros
	 * @return
	 */
	
	public static HashMap desmarcarFacturasVarias(Connection con, HashMap parametros, boolean anulacion)
	{
		ActionErrors errores = new ActionErrors();
		HashMap resultado = new HashMap();
		String cadenaModificacion = desmarcarFacturasVarias;
		resultado.put("estadoCont","");	
		int actualizacion=0;
		resultado.put("actualizo",ConstantesBD.acronimoSi);
		
		if(anulacion)
		{
			cadenaModificacion = cadenaModificacion.replace("contabilizado", "contabilizado_anulacion");
			cadenaModificacion+="  estado_factura='"+ConstantesIntegridadDominio.acronimoEstadoAnulado+"' AND anula_fac_apro='"+ConstantesBD.acronimoSi+"' " ;
			cadenaModificacion+=" AND to_char(fecha_anulacion,'YYYY-MM-DD') BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(parametros.get("fechaInicialDesmaracion").toString())+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(parametros.get("fechaFinalDesmarcacion").toString())+"' ";
		}else
		{
			cadenaModificacion+=" estado_factura = '"+ConstantesIntegridadDominio.acronimoEstadoAprobado+"' OR  ( estado_factura='"+ConstantesIntegridadDominio.acronimoEstadoAnulado+"' AND anula_fac_apro='"+ConstantesBD.acronimoSi+"')  " ;
			cadenaModificacion+=" AND to_char(fecha_aprobacion,'YYYY-MM-DD') BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(parametros.get("fechaInicialDesmaracion").toString())+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(parametros.get("fechaFinalDesmarcacion").toString())+"' ";
		}
		
		
	 
		try{
	   		 
	   		 PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadenaModificacion,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));	   		 	 
	   		 actualizacion = ps.executeUpdate();  

	   		 if(actualizacion >= 0 ) 
	   		 {	   			
	   			resultado.put("estadoCont",ConstantesBD.acronimoSi);
	   			
	   			if(actualizacion==0)
	           	 {
	           		 resultado.put("actualizo",ConstantesBD.acronimoNo);
	           	 }			
		      }
	   		 ps.close();
	            
		 }
			catch (Exception e) {			
				 e.printStackTrace(); 
				 logger.info("\n \n error al  Modificar  Facturas Varias>>  cadena >>  "+cadenaModificacion+" ");
				 if(anulacion)
			    	{
		   			 errores.add("descripcion",new ActionMessage("errors.notEspecific","Se presentaron Errores en la Desmarcación de Facturas Varias"));
			    	}else
			    	{
			    	 errores.add("descripcion",new ActionMessage("errors.notEspecific","Se presentaron Errores en la Desmarcación de Anulacion Facturas Varias"));
			    	}
				  resultado.put("error",errores);
			 }   		 
	   		 
		 return resultado;
		
	}
	
	/**
	 * Metodo para realizar el proceso de desmarcacion (contabilizado='N') de la tabla ajus_facturas_varias
	 * @param con
	 * @param parametros
	 * @return
	 */
	public static HashMap desmarcarAjustesFacturasVarias(Connection con, HashMap parametros)
	{
		ActionErrors errores = new ActionErrors();
		HashMap resultado = new HashMap();
		String cadenaModificacion = desmarcarAjustesFacturasVarias;
		cadenaModificacion+=" AND to_char(fecha_ajuste,'YYYY-MM-DD') BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(parametros.get("fechaInicialDesmaracion").toString())+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(parametros.get("fechaFinalDesmarcacion").toString())+"' ";
		resultado.put("estadoCont","");	
		int actualizacion=0;
		resultado.put("actualizo",ConstantesBD.acronimoSi);
	 
		try{
	   		 
	   		 PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadenaModificacion,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	   		 actualizacion = ps.executeUpdate();  

	   		 if(actualizacion >= 0 ) 
	   		   {
	   			resultado.put("estadoCont",ConstantesBD.acronimoSi);
	   			if(actualizacion==0)
	           	 {
	           		 resultado.put("actualizo",ConstantesBD.acronimoNo);
	           	 }	
		       }	                	    	
	    	    ps.close();	    	    	      
		  }
			catch (Exception e) {			
				 e.printStackTrace();
				 logger.info("\n \n error al  Modificar Ajustes Facturas Varias>>  cadena >>  "+cadenaModificacion+" ");
				 errores.add("descripcion",new ActionMessage("errors.notEspecific","Se presentaron Errores en la Desmarcación de Anulacion Ajustes Facturas Varias"));
		    	 resultado.put("error",errores);
				
			 }   		 
	   		 
		 return resultado;
		
	}
	
	/**
	 * Metodo para realizar el proceso de desmarcacion (contabilizado='N') de la tabla cuentas_cobro_capitacion
	 * @param con
	 * @param parametros
	 * @return
	 */
	public static HashMap desmarcarCuentasCobroCapitacion(Connection con, HashMap parametros)
	{
		ActionErrors errores = new ActionErrors();
		HashMap resultado = new HashMap();
		String cadenaModificacion = desmarcarCuentasCobroCapitacion;
		cadenaModificacion+=" AND to_char(fecha_radicacion,'YYYY-MM-DD') BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(parametros.get("fechaInicialDesmaracion").toString())+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(parametros.get("fechaFinalDesmarcacion").toString())+"' ";
		resultado.put("estadoCont","");	
		int actualizacion=0;
		resultado.put("actualizo",ConstantesBD.acronimoSi);
	 
		try{
	   		 
	   		 PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadenaModificacion,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	    	 actualizacion = ps.executeUpdate();  

	   		 if(actualizacion >= 0 ) 
	   		   {
	   			resultado.put("estadoCont",ConstantesBD.acronimoSi);	 				 			 
	   			if(actualizacion==0)
	           	 {
	           		 resultado.put("actualizo",ConstantesBD.acronimoNo);
	           	 }
		        }	                	    	
	    	 ps.close();	    	    	   	    	 		        	
		        
		 }
			catch (Exception e) {			
				 e.printStackTrace();				 
				 logger.info("\n \n error al  Modificar Cuentas de Cobro Capitación >>  cadena >>  "+cadenaModificacion+" ");
				 errores.add("descripcion",new ActionMessage("errors.notEspecific","Se presentaron Errores en la Desmarcación de Cuentas de Cobro Capitación"));
		    	 resultado.put("error",errores);
			 }   		 
	   		 
		 return resultado;
		
	}
	
	
	/**
	 * Metodo para realizar el proceso de desmarcacion (contabilizado='N') de la tabla ajustes_cxc
	 * @param con
	 * @param parametros
	 * @return
	 */
	public static HashMap desmarcarAjustesCuentasCobroCapitacion(Connection con, HashMap parametros)
	{
		ActionErrors errores = new ActionErrors();
		HashMap resultado = new HashMap();
		String cadenaConsultaAprobacion= "SELECT ajust.codigo AS codigoajuste " +
				                         "FROM ajustes_cxc ajust  " +
				                         "INNER JOIN aprobacion_ajustes_cxc aproajus ON (aproajus.codigo_ajuste = ajust.codigo)  " +
				                         "WHERE  ajust.contabilizado = '"+ConstantesBD.acronimoSi+"' AND  ajust.estado = "+ConstantesBD.codigoEstadoAjusteCxCAprobado+"  AND  to_char(aproajus.fecha_aprobacion,'YYYY-MM-DD') BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(parametros.get("fechaInicialDesmaracion").toString())+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(parametros.get("fechaFinalDesmarcacion").toString())+"' ";
		
		String cadenaModificacion = desmarcarAjustesCuentasCobroCapitacion;
		resultado.put("estadoCont","");	
		int actualizacion=0, contActualizaciones=0;
		resultado.put("actualizo",ConstantesBD.acronimoSi);
	 
		try{
	   		 
			 PreparedStatementDecorator ps1 =  new PreparedStatementDecorator(con.prepareStatement(cadenaConsultaAprobacion,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));			 
			 ResultSetDecorator rs = new ResultSetDecorator(ps1.executeQuery());
			 
			 while(rs.next())
			 {
				try
				{
					
					 PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadenaModificacion,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));	   		 
			   		 ps.setInt(1, rs.getInt("codigoajuste"));
			   		 actualizacion = ps.executeUpdate();  
			   		 
			   		 if(actualizacion >= 0 ) 
			   		   {
			   			 if(actualizacion > 0)
			           	  {
			   				contActualizaciones++; 
			           	  }
			   			 
				        }
			            
			   		 ps.close();
					
				}			 
				catch (Exception e) {			
					 e.printStackTrace();
					 errores.add("descripcion",new ActionMessage("errors.notEspecific","Se presentaron Errores en la Desmarcación de Ajustes Cuentas de Cobro Capitación"));
			    	 logger.info("\n \n error al  Modificar  Ajustes Cuentas de Cobro Capitacion >>  cadena >>  "+cadenaModificacion+" ");
			    	 resultado.put("error",errores);	 
					
				 } 
			 }	
			 if(contActualizaciones == 0)
			  {
			  resultado.put("actualizo",ConstantesBD.acronimoNo);
			  }
			 rs.close(); 	 
			 ps1.close();
		        if(errores.isEmpty())
		        {     
	            	 resultado.put("estadoCont",ConstantesBD.acronimoSi);	    	    	
	    	    	 	    	    	
	    	    	 return resultado;		        	
		        }
			 
	   		
		 }
			catch (Exception e) {			
				 e.printStackTrace();
				 
				 logger.info("\n \n error al  Consultar  Aprobacionn Ajustes Cuentas de Cobro Capitación >>  cadena >>  "+cadenaConsultaAprobacion+" ");
				
			 }   		 
	   		 
		 return resultado;
		
	}
	
	/**
	 * Metodo para realizar el proceso de desmarcacion (contabilizado='N') de la tabla recibos_caja
	 * @param con
	 * @param parametros
	 * @return
	 */
	public static HashMap desmarcarRecibosdeCaja(Connection con, HashMap parametros)
	{
		ActionErrors errores = new ActionErrors();
		HashMap resultado = new HashMap();
		
		String cadenaModificacion = desmarcarRecibosdeCaja;
		cadenaModificacion+=" AND to_char(fecha,'YYYY-MM-DD') BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(parametros.get("fechaInicialDesmaracion").toString())+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(parametros.get("fechaFinalDesmarcacion").toString())+"' ";
		resultado.put("estadoCont","");	
		int actualizacion=0;
		resultado.put("actualizo",ConstantesBD.acronimoSi);
	 
		try{
	   		 
	   		 PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadenaModificacion,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	   		 actualizacion=ps.executeUpdate();  		 
	   		 
	   		  if(actualizacion>=0)
		        {
	   			resultado.put("estadoCont",ConstantesBD.acronimoSi);			 			 
	   			if(actualizacion==0)
	           	 {
	           		 resultado.put("actualizo",ConstantesBD.acronimoNo);
	           	 }	   				
		        }
	   		ps.close();
		 }
			catch (Exception e) {			
				 e.printStackTrace();
				 errores.add("descripcion",new ActionMessage("errors.notEspecific","Se presentaron Errores en la Desmarcación de Recibos de Caja"));
		    	 logger.info("\n \n error al  Modificar Recibos de Caja>>  cadena >>  "+cadenaModificacion+" ");
		    	 resultado.put("error",errores);	
				 
				
			 }   		 
			
		return resultado;
		
	}
	
	
	/**
	 * Metodo para realizar el proceso de desmarcacion (contabilizado='N') de la tabla anulacion recibos_caja
	 * @param con
	 * @param parametros
	 * @return
	 */
	public static HashMap desmarcarAnulacionRecibosdeCaja(Connection con, HashMap parametros)
	{
		ActionErrors errores = new ActionErrors();
		HashMap resultado = new HashMap();
		
		String cadenaModificacion = demarcarAnulacionRecibosdeCaja;
		cadenaModificacion+=" to_char(fecha,'YYYY-MM-DD') BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(parametros.get("fechaInicialDesmaracion").toString())+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(parametros.get("fechaFinalDesmarcacion").toString())+"' ";
		resultado.put("estadoCont","");	
		int actualizacion=0;
		resultado.put("actualizo",ConstantesBD.acronimoSi);
		
		try{
	   		 
	   		 PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadenaModificacion,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	   		    		 
	   		actualizacion=ps.executeUpdate();  		 
	   		 
	   		  if(actualizacion>=0)
		        {
	   			    resultado.put("estadoCont",ConstantesBD.acronimoSi);			 			 
		   			if(actualizacion==0)
		           	 {
		           		 resultado.put("actualizo",ConstantesBD.acronimoNo);
		           	 }	   				
		        }
	   		ps.close();
		 }
		  catch (Exception e) {			
				 e.printStackTrace();
				 errores.add("descripcion",new ActionMessage("errors.notEspecific","Se presentaron Errores en la Desmarcación de Anulación de Recibos de Caja"));
		    	 logger.info("\n \n error al  Modificar Anulacion Recibos de Caja>>  cadena >>  "+cadenaModificacion+" ");
		    	 resultado.put("error",errores);	 
				
			 }  
				
		return resultado;
	}
	
	
	/**
	 * Metodo para realizar el proceso de desmarcacion (contabilizado='N') de la tabla Devoluciones Recibos Caja
	 * @param con
	 * @param parametrosBusqueda
	 * @return
	 */
	public static HashMap desmarcarDevolucionRecibosdeCaja(Connection con,HashMap parametros) {
		
		ActionErrors errores = new ActionErrors();
		HashMap resultado = new HashMap();
		String cadenaModificacion =desmarcarDevolucionRecibosCaja;
		
		cadenaModificacion += " AND to_char(fecha_aprobacion,'YYYY-MM-DD') BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(parametros.get("fechaInicialDesmaracion").toString())+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(parametros.get("fechaFinalDesmarcacion").toString())+"' ";
		
		resultado.put("estadoCont","");	
		int actualizacion=0;
		resultado.put("actualizo",ConstantesBD.acronimoSi);
	 
		try{
	   		 
	   		 PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadenaModificacion,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	   		 actualizacion=ps.executeUpdate();  		 
	   		 
	   		  if(actualizacion>=0)
		        {
	   			resultado.put("estadoCont",ConstantesBD.acronimoSi);			 			 
	   			if(actualizacion==0)
	           	 {
	           		 resultado.put("actualizo",ConstantesBD.acronimoNo);
	           	 }	   				
		        }
	   		ps.close();
		 }
			catch (Exception e) {			
				 e.printStackTrace();
				 errores.add("descripcion",new ActionMessage("errors.notEspecific","Se presentaron Errores en la Desmarcación de Devolucion Recibos de Caja"));
		    	 logger.info("\n \n error al  Modificar Devoluciones Recibos de Caja>>  cadena >>  "+cadenaModificacion+" ");
		    	 resultado.put("error",errores);	
				 
				
			 }   		
		
		return resultado;
	}
	
	
   /**
    * Metodo para realizar el proceso de desmarcacion (contabilizado='N') de la tabla Ajustes empresa
    * @param con
    * @param parametros
    * @return
    */
	public static HashMap desmarcarAjustesFacuturasPacientes(Connection con, HashMap parametros)
	
	{
		ActionErrors errores = new ActionErrors();
		HashMap resultado = new HashMap();
		String cadenaConsultaAprobacion= "SELECT ajustempr.codigo AS codigoajuste " +
				                         "FROM  ajustes_empresa ajustempr " +
				                         "INNER JOIN ajus_empresa_aprobados ajusempraprob  ON (ajusempraprob.codigo_ajuste = ajustempr.codigo)  " +
				                         "WHERE  ajustempr.estado = "+ConstantesBD.codigoEstadoCarteraAprobado+" AND  ajustempr.contabilizado = '"+ConstantesBD.acronimoSi+"' AND  to_char(ajusempraprob.fecha_aprobacion,'YYYY-MM-DD') BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(parametros.get("fechaInicialDesmaracion").toString())+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(parametros.get("fechaFinalDesmarcacion").toString())+"' ";
		
		String cadenaModificacion = desmarcarAjustesFacuturasPacientes;
		resultado.put("estadoCont","");	
		int actualizacion=0, contActualizaciones=0;
		resultado.put("actualizo",ConstantesBD.acronimoSi);
		
		try{
	   		 
			 PreparedStatementDecorator ps1 =  new PreparedStatementDecorator(con.prepareStatement(cadenaConsultaAprobacion,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));			 
			 ResultSetDecorator rs = new ResultSetDecorator(ps1.executeQuery());
			 
			 while(rs.next())
			 {
				try
				{				
					 PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadenaModificacion,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));	   		 
			   		 ps.setInt(1, rs.getInt("codigoajuste"));
                     actualizacion = ps.executeUpdate();  
			   		 
			   		 if(actualizacion >= 0 ) 
			   		   {
			   			 if(actualizacion > 0)
			           	  {
			   				contActualizaciones++; 
			           	  }
			   			 
				        }
			            
			   		 ps.close();
					
				}			 
				catch (Exception e) {			
					 e.printStackTrace();
					 errores.add("descripcion",new ActionMessage("errors.notEspecific","Se presentaron Errores en la Desmarcación de Ajustes Facturas Pacientes"));
			    	 logger.info("\n \n error al  Modificar  Ajustes Facturas Pacientes >>  cadena >>  "+cadenaModificacion+" ");
			    	 resultado.put("error",errores);	
					
				 } 
			 }	 
			
			 rs.close(); 
			 ps1.close();
			 
			 if(contActualizaciones == 0)
			  {
			  resultado.put("actualizo",ConstantesBD.acronimoNo);
			  }
		        if(errores.isEmpty())
		        {     
	            	 resultado.put("estadoCont",ConstantesBD.acronimoSi);	    	 	    	    	
	    	    	   	
		        }			 
	   		
		 }
			catch (Exception e) {			
				 e.printStackTrace();
				 
				 logger.info("\n \n error al  Consultar  Aprobaci&oacute;n Ajustes Empresa >>  cadena >>  "+cadenaConsultaAprobacion+" ");
				
			 } 
		
		 
		return resultado;
	}
	
	/**
	 * Metodo para realizar el proceso de desmarcacion (contabilizado='N') de la tabla registro glosas
	 * @param con
	 * @param parametros
	 * @return
	 */
	public static HashMap desmarcarRegistroGlosas(Connection con,HashMap parametros) {
		ActionErrors errores = new ActionErrors();
		HashMap resultado = new HashMap();
		String cadenaModificacion = desmarcarRegistroGlosas;
		
        cadenaModificacion += " AND to_char(fecha_aprobacion,'YYYY-MM-DD') BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(parametros.get("fechaInicialDesmaracion").toString())+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(parametros.get("fechaFinalDesmarcacion").toString())+"' ";
		
		resultado.put("estadoCont","");	
		int actualizacion=0;
		resultado.put("actualizo",ConstantesBD.acronimoSi);
	 
		try{
	   		 
	   		 PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadenaModificacion,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	   		 actualizacion=ps.executeUpdate();  		 
	   		 
	   		  if(actualizacion>=0)
		        {
	   			resultado.put("estadoCont",ConstantesBD.acronimoSi);			 			 
	   			if(actualizacion==0)
	           	 {
	           		 resultado.put("actualizo",ConstantesBD.acronimoNo);
	           	 }	   				
		        }
	   		ps.close();
		 }
			catch (Exception e) {			
				 e.printStackTrace();
				 errores.add("descripcion",new ActionMessage("errors.notEspecific","Se presentaron Errores en la Desmarcación de Registro de Glosas"));
		    	 logger.info("\n \n error al  Modificar Registro de Glosas>>  cadena >>  "+cadenaModificacion+" ");
		    	 resultado.put("error",errores);	
				 
				
			 } 		
		
		return resultado;
	}
	
	
	/**
	 * Metodo para realizar el proceso de desmarcacion (contabilizado='N') de la tabla autorizaciones_entidades_sub
	 * Si anlacion = true realiza el proceso de desmarcacion (contabilizado_anulacion = N) de la tabla autorizaciones_entidades_sub
	 * @param con
	 * @param parametros
	 * @return
	 */
	public static HashMap desmarcarAutorServEntidadesSub(Connection con, HashMap parametros, boolean anulacion)
	{
		ActionErrors errores = new ActionErrors();
		HashMap resultado = new HashMap();
		String cadenaModificacion = desmarcarRegAutorSerEntidadesSub;
		resultado.put("estadoCont","");
		int actualizacion=0;
		resultado.put("actualizo",ConstantesBD.acronimoSi);
		
		
		if(anulacion)
		{
			cadenaModificacion = cadenaModificacion.replace("contabilizado", "contabilizado_anulacion");
			cadenaModificacion+=" AND estado IN ('"+ConstantesIntegridadDominio.acronimoEstadoAnulado+"')  " ;
			cadenaModificacion+=" AND to_char(fecha_anulacion,'YYYY-MM-DD') BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(parametros.get("fechaInicialDesmaracion").toString())+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(parametros.get("fechaFinalDesmarcacion").toString())+"' ";
		
		}else
		{
			cadenaModificacion+=" AND estado IN ('"+ConstantesIntegridadDominio.acronimoAutorizado+"', '"+ConstantesIntegridadDominio.acronimoEstadoAnulado+"')  " ;
			cadenaModificacion+=" AND to_char(fecha_autorizacion,'YYYY-MM-DD') BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(parametros.get("fechaInicialDesmaracion").toString())+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(parametros.get("fechaFinalDesmarcacion").toString())+"' ";
		}
		
		
		try{	   		 
	   		 PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadenaModificacion,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	   		 actualizacion=ps.executeUpdate();  		 
	   		 
	   		 logger.info("la sql de andrecito >> "+cadenaModificacion);
	   		 
	   		  if(actualizacion>=0)
		        {
	   			    resultado.put("estadoCont",ConstantesBD.acronimoSi);			 			 
		   			if(actualizacion==0)
		           	 {
		           		 resultado.put("actualizo",ConstantesBD.acronimoNo);
		           	 }	   				
		        }
	   		ps.close();
		 }
		  catch (Exception e) {			
				 e.printStackTrace();
				 errores.add("descripcion",new ActionMessage("errors.notEspecific","Se presentaron Errores en la Desmarcación de Registro Autorización de Servicios Entidades Subcontratadas"));
		    	 logger.info("\n \n error al  Modificar Registro Autorizacion de Servicios Entidades Subcontratadas >>  cadena >>  "+cadenaModificacion+" ");
		    	 resultado.put("error",errores);				
				
			 }  					
		
		return resultado;
	}
	
	
	/**
	 * Metodo para realizar el proceso de desmarcacion (contabilizado='N') de la tabla despacho
	 * @param con
	 * @param parametros
	 * @param anulacion
	 * @return
	 */
	public static HashMap desmarcarDespachoMedicamentos(Connection con, HashMap parametros)
	{
		ActionErrors errores = new ActionErrors();
		HashMap resultado = new HashMap();
		
		String cadenaConsultaDespacho=  "SELECT des.orden AS numdespacho " +
								        "FROM  despacho des  " +
								        "INNER JOIN solicitudes_medicamentos solmed  ON (solmed.numero_solicitud = des.numero_solicitud )  " +
								        "WHERE des.contabilizado = '"+ConstantesBD.acronimoSi+"' AND  to_char(des.fecha,'YYYY-MM-DD') BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(parametros.get("fechaInicialDesmaracion").toString())+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(parametros.get("fechaFinalDesmarcacion").toString())+"' " +
								        "AND solmed.entidad_subcontratada IS NOT NULL ";

		String cadenaModificacion = desmarcarDespachoMedicamentos;
		resultado.put("estadoCont","");
		int actualizacion=0, contActualizaciones=0;
		resultado.put("actualizo",ConstantesBD.acronimoSi);
		
		try{
	   		 
			 PreparedStatementDecorator ps1 =  new PreparedStatementDecorator(con.prepareStatement(cadenaConsultaDespacho,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));			 
			 ResultSetDecorator rs = new ResultSetDecorator(ps1.executeQuery());
			 
			 while(rs.next())
			 {
				try
				{
					
					 PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadenaModificacion,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));	   		 
			   		 ps.setInt(1, rs.getInt("numdespacho"));
					 
			   		 actualizacion = ps.executeUpdate();  
			   		 
			   		 if(actualizacion >= 0 ) 
			   		   {
			   			 if(actualizacion > 0)
			           	  {
			   				contActualizaciones++; 
			           	  }
			   			 
				        }
			            
			   		 ps.close();
					
				}			 
				catch (Exception e) {			
					 e.printStackTrace();
					 errores.add("descripcion",new ActionMessage("errors.notEspecific","Se presentaron Errores en la Desmarcación de Despacho Medicamentos"));
			    	 logger.info("\n \n error al  Modificar Despacho Medicamentos >>  cadena >>  "+cadenaModificacion+" ");
			    	 resultado.put("error",errores);
					
				 } 
			 }	 
			
			 rs.close(); 	
			 ps1.close();
			 if(contActualizaciones == 0)
			  {
			  resultado.put("actualizo",ConstantesBD.acronimoNo);
			  }
			 
		        if(errores.isEmpty())
		        {     
	            	 resultado.put("estadoCont",ConstantesBD.acronimoSi);	    	    			        	
		        }	   		
		 }
			catch (Exception e) {			
				 e.printStackTrace();
				 
				 logger.info("\n \n error al  Consultar Solicitudes Medicamentos >>  cadena >>  "+cadenaConsultaDespacho+" ");
				
			 } 
		
		return resultado;
		
	}
	
	
	/**
	 * Metodo para realizar el proceso de desmarcacion (contabilizado='N') de la tabla devolucion_med
	 * @param con
	 * @param parametros
	 * @return
	 */
	public static HashMap desmarcarDevolucionMedicamentos(Connection con, HashMap parametros)
	{
		ActionErrors errores = new ActionErrors();
		HashMap resultado = new HashMap();
		
		String cadenaConsultaDevolucion= "SELECT devmed.codigo AS codevolucion " +
								         "FROM  devolucion_med devmed " +
								         "INNER JOIN recepciones_medicamentos recmed ON (recmed.devolucion = devmed.codigo) "+
								         "INNER JOIN  detalle_devol_med detdev ON (detdev.devolucion = devmed.codigo )  " +
								         "INNER JOIN solicitudes_medicamentos solmed  ON (solmed.numero_solicitud = detdev.numero_solicitud )  " +
								         "WHERE  devmed.contabilizado = '"+ConstantesBD.acronimoSi+"' AND devmed.estado = "+ConstantesBD.codigoEstadoDevolucionRecibida +"  AND "+ 
								         "to_char(recmed.fecha,'YYYY-MM-DD') BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(parametros.get("fechaInicialDesmaracion").toString())+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(parametros.get("fechaFinalDesmarcacion").toString())+"' " +
								         "AND solmed.entidad_subcontratada IS NOT NULL ";
		
		String cadenaModificacion = desmarcarDevolucionMedicamentos;
		resultado.put("estadoCont","");
		int actualizacion=0, contActualizaciones=0;
		resultado.put("actualizo",ConstantesBD.acronimoSi);		
		
		try{
	   		 
			 PreparedStatementDecorator ps1 =  new PreparedStatementDecorator(con.prepareStatement(cadenaConsultaDevolucion,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));			 
			 ResultSetDecorator rs = new ResultSetDecorator(ps1.executeQuery());
			 
			 while(rs.next())
			 {
				try
				{
					
					 PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadenaModificacion,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));	   		 
			   		 ps.setInt(1, rs.getInt("codevolucion"));
					 
                    actualizacion = ps.executeUpdate();  
			   		 
			   		 if(actualizacion >= 0 ) 
			   		   {
			   			 if(actualizacion > 0)
			           	  {
			   				contActualizaciones++; 
			           	  }
			   			 
				        }
			            
			   		 ps.close();
					
				}			 
				catch (Exception e) {			
					 e.printStackTrace();
					 errores.add("descripcion",new ActionMessage("errors.notEspecific","Se presentaron Errores en la Desmarcación de Devolucion Medicamentos"));
			    	 logger.info("\n \n error al  Modificar Devolucion Medicamentos >>  cadena >>  "+cadenaModificacion+" ");
			    	 resultado.put("error",errores);	
					
				 } 
			 }	 
			
			 rs.close(); 	
			 ps1.close();
			 if(contActualizaciones == 0)
			  {
			  resultado.put("actualizo",ConstantesBD.acronimoNo);
			  }
		        if(errores.isEmpty())
		        {     
	            	 resultado.put("estadoCont",ConstantesBD.acronimoSi);	    	    		        	
		        }			 	   		
		 }
			catch (Exception e) {			
				 e.printStackTrace();
				 
				 logger.info("\n \n error al  Consultar Devoluciones Medicamentos >>  cadena >>  "+cadenaConsultaDevolucion+" ");
				
			 } 
		
		return resultado;
		
	}
	
	
	/**
	 * Metodo para realizar el proceso de desmarcacion (contabilizado='N') de la tabla despacho_pedido "
	 * SI esQuirurgico = true se evalua que el campo es_qx = 'S' ( Se utiliza para despacho de pedidos Quirurgicos )  
	 * @param con
	 * @param parametros
	 * @param esQuirurgico
	 * @return
	 */
	public static HashMap desmarcarDespachoPedidos(Connection con, HashMap parametros, boolean esQuirurgico)
	{
		ActionErrors errores = new ActionErrors();
		HashMap resultado = new HashMap();
		
		String cadenaConsultaDespacho="SELECT desped.pedido AS codigodespacho " +
				                      "FROM despacho_pedido desped " +
				                      "INNER JOIN pedido ped ON (ped.codigo = desped.pedido) " +
				                      "WHERE desped.contabilizado = '"+ConstantesBD.acronimoSi+"' AND ped.entidad_subcontratada IS NOT NULL " +
				                      "AND  to_char(desped.fecha,'YYYY-MM-DD')  BETWEEN  '"+UtilidadFecha.conversionFormatoFechaABD(parametros.get("fechaInicialDesmaracion").toString())+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(parametros.get("fechaFinalDesmarcacion").toString())+"' " ;
		if(esQuirurgico)
		{
           cadenaConsultaDespacho+=" AND ped.es_qx = '"+ConstantesBD.acronimoSi+"' "; 
		}
		else
		{
			cadenaConsultaDespacho+=" AND ped.es_qx = '"+ConstantesBD.acronimoNo+"' "; 	
		}
		String cadenaModificacion = desmarcarDespachoPedidos;
		resultado.put("estadoCont","");
		int actualizacion=0, contActualizaciones=0;
		resultado.put("actualizo",ConstantesBD.acronimoSi);	
		
		try{
	   		 
			 PreparedStatementDecorator ps1 =  new PreparedStatementDecorator(con.prepareStatement(cadenaConsultaDespacho,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));			 
			 ResultSetDecorator rs = new ResultSetDecorator(ps1.executeQuery());
			 
			 while(rs.next())
			 {
				try
				{
					
					 PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadenaModificacion,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));	   		 
			   		 ps.setInt(1, rs.getInt("codigodespacho"));					 
			   		 actualizacion = ps.executeUpdate();  
			   		 
			   		 if(actualizacion >= 0 ) 
			   		   {
			   			 if(actualizacion > 0)
			           	  {
			   				contActualizaciones++; 
			           	  }
			   			 
				        }
			            
			   		 ps.close();
					
				}			 
				catch (Exception e) {			
					 e.printStackTrace();
					 errores.add("descripcion",new ActionMessage("errors.notEspecific","Se presentaron Errores en la Desmarcación de Despacho Pedidos"));
			    	 logger.info("\n \n error al  Modificar Despacho pedidos >>  cadena >>  "+cadenaModificacion+" ");
			    	 resultado.put("error",errores);
					
				 } 
			 }	 
			
			 rs.close(); 
			 ps1.close();
			 if(contActualizaciones == 0)
			  {
			  resultado.put("actualizo",ConstantesBD.acronimoNo);
			  }
		        if(errores.isEmpty())
		        {     
	            	 resultado.put("estadoCont",ConstantesBD.acronimoSi);	    	    		        	
		        }			 	   		
		 }
			catch (Exception e) {			
				 e.printStackTrace();
				 
				 logger.info("\n \n error al  Consultar Despacho Pedidos >>  cadena >>  "+cadenaConsultaDespacho+" ");
				
			 } 		
		
		return resultado;
		
	}
	
	
	/**
	 * Metodo para realizar el proceso de desmarcacion (contabilizado='N') de la tabla devolucion_pedidos "
	 * SI esQuirurgico = true se evalua que el campo es_qx = 'S' ( Se utiliza para devolucion de pedidos Quirurgicos )
	 * @param con
	 * @param parametros
	 * @param esQuirurgico
	 * @return
	 */
	public static HashMap desmarcarDevolucionPedidos (Connection con, HashMap parametros, boolean esQuirurgico)
	{
		ActionErrors errores = new ActionErrors();
		HashMap resultado = new HashMap();
		
		String cadenaConsultaDevolucion="SELECT devped.codigo AS codigodevolucion " +
				                      	"FROM devolucion_pedidos devped " +
				                      	"INNER JOIN detalle_devol_pedido detped ON (detped.devolucion = devped.codigo ) " +
				                      	"INNER JOIN pedido ped ON (ped.codigo = detped.pedido) " +
				                      	"INNER JOIN recepciones_pedidos recped ON (recped.devolucion = devped.codigo) "+
				                      	"WHERE devped.contabilizado = '"+ConstantesBD.acronimoSi+"' AND ped.entidad_subcontratada IS NOT NULL " +
				                      	"AND  to_char(recped.fecha,'YYYY-MM-DD')  BETWEEN  '"+UtilidadFecha.conversionFormatoFechaABD(parametros.get("fechaInicialDesmaracion").toString())+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(parametros.get("fechaFinalDesmarcacion").toString())+"' " ;
		if(esQuirurgico)
		{
			cadenaConsultaDevolucion+=" AND ped.es_qx = '"+ConstantesBD.acronimoSi+"' "; 
		}
		else
		{
			cadenaConsultaDevolucion+=" AND ped.es_qx = '"+ConstantesBD.acronimoNo+"' "; 	
		}
		
		
		String cadenaModificacion = desmarcarDevolucionPedidos;
		resultado.put("estadoCont","");
		int actualizacion=0, contActualizaciones=0;
		resultado.put("actualizo",ConstantesBD.acronimoSi);	
		
		try{
	   		 
			 PreparedStatementDecorator ps1 =  new PreparedStatementDecorator(con.prepareStatement(cadenaConsultaDevolucion,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));			 
			 ResultSetDecorator rs = new ResultSetDecorator(ps1.executeQuery());
			 
			 while(rs.next())
			 {
				try
				{
					
					 PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadenaModificacion,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));	   		 
			   		 ps.setInt(1, rs.getInt("codigodevolucion"));
			   		 actualizacion = ps.executeUpdate();  
			   		 
			   		 if(actualizacion >= 0 ) 
			   		   {
			   			 if(actualizacion > 0)
			           	  {
			   				contActualizaciones++; 
			           	  }
			   			 
				        }
			            
			   		 ps.close();
					
				}			 
				catch (Exception e) {			
					 e.printStackTrace();					 
					 errores.add("descripcion",new ActionMessage("errors.notEspecific","Se presentaron Errores en la Desmarcación de Devolución Pedidos"));
					 logger.info("\n \n error al  Modificar Devolucion pedidos >>  cadena >>  "+cadenaModificacion+" ");
			    	 resultado.put("error",errores);
				 } 
			 }	 
			
			 rs.close(); 	 
			 ps1.close();
			 if(contActualizaciones == 0)
			  {
			  resultado.put("actualizo",ConstantesBD.acronimoNo);
			  }
		        if(errores.isEmpty())
		        {     
	            	 resultado.put("estadoCont",ConstantesBD.acronimoSi);    	    	 		        	
		        }			 	   		
		 }
			catch (Exception e) {			
				 e.printStackTrace();
				 
				 logger.info("\n \n error al  Consultar Despacho Pedidos >>  cadena >>  "+cadenaConsultaDevolucion+" ");
				
			 }
				
		return resultado;
		
	}
	
	
	/**
	 * Metodo para realizar el proceso de desmarcacion (contabilizado='N') de la tabla despacho "
	 * @param con
	 * @param parametros
	 * @return
	 */
	public static HashMap desmarcarCargosDirectosArticulos(Connection con, HashMap parametros)
	{
		ActionErrors errores = new ActionErrors();
		HashMap resultado = new HashMap();
		
		String cadenaConsultaCargos= "SELECT desp.orden AS numdespacho " +
				                     "FROM despacho desp " +
				                     "INNER JOIN solicitudes_medicamentos solmed ON (solmed.numero_solicitud = desp.numero_solicitud) " +
				                     "INNER JOIN cargos_directos carg ON (carg.numero_solicitud = desp.numero_solicitud) " +
				                     "WHERE desp.contabilizado = '"+ConstantesBD.acronimoSi+"' AND  solmed.entidad_subcontratada IS NOT NULL  AND  " +
				                     "carg.afecta_inventarios = '"+ConstantesBD.acronimoSi+"'  AND " +
				                     "to_char(desp.fecha,'YYYY-MM-DD')  BETWEEN  '"+UtilidadFecha.conversionFormatoFechaABD(parametros.get("fechaInicialDesmaracion").toString())+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(parametros.get("fechaFinalDesmarcacion").toString())+"' " ;
		
		String cadenaModificacion = demarcarCargosDirectosArticulos;
		resultado.put("estadoCont","");
		int actualizacion=0, contActualizaciones=0;
		resultado.put("actualizo",ConstantesBD.acronimoSi);	
		
		try{
	   		 
			 PreparedStatementDecorator ps1 =  new PreparedStatementDecorator(con.prepareStatement(cadenaConsultaCargos,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));			 
			 ResultSetDecorator rs = new ResultSetDecorator(ps1.executeQuery());
			 
			 while(rs.next())
			 {
				try
				{					
					 PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadenaModificacion,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));	   		 
			   		 ps.setInt(1, rs.getInt("numdespacho"));
					 actualizacion = ps.executeUpdate();  
			   		 
			   		 if(actualizacion >= 0 ) 
			   		   {
			   			 if(actualizacion > 0)
			           	  {
			   				contActualizaciones++; 
			           	  }
			   			 
				        }
			            
			   		 ps.close();
					
				}			 
				catch (Exception e) {			
					 e.printStackTrace();
					 errores.add("descripcion",new ActionMessage("errors.notEspecific","Se presentaron Errores en la Desmarcación de Cargos Directos Articulos"));
			    	 logger.info("\n \n error al  Modificar Cargos Directos Articulos >>  cadena >>  "+cadenaModificacion+" ");
			    	 resultado.put("error",errores);	
					
				 } 
			 }	 
			
			 rs.close(); 	
			 ps1.close();
			 if(contActualizaciones == 0)
			  {
			  resultado.put("actualizo",ConstantesBD.acronimoNo);
			  }
		        if(errores.isEmpty())
		        {     
	            	 resultado.put("estadoCont",ConstantesBD.acronimoSi);	    	    		        	
		        }			 	   		
		 }
			catch (Exception e) {			
				 e.printStackTrace();
				 
				 logger.info("\n \n error al  Consultar Despacho Pedidos >>  cadena >>  "+cadenaConsultaCargos+" ");
				
			 }
		
		return resultado;
		
	}
	
	
	
	/**
	 * Metodo para realizar el proceso de desmarcacion (contabilizado='N') de la tabla anulacion_cargos_farmacia "
	 * @param con
	 * @param parametros
	 * @return
	 */
	public static HashMap desmarcarAnulacionCargosArticulos(Connection con, HashMap parametros)
	{
		ActionErrors errores = new ActionErrors();
		HashMap resultado = new HashMap();
		
		String cadenaConsultaAnulacionCargos="SELECT * FROM ( " +
				                             "SELECT anulcar.codigo AS codanul " +
				                             "FROM anulacion_cargos_farmacia anulcar " +
				                             "INNER JOIN solicitudes sol ON (sol.numero_solicitud = anulcar.numero_solicitud ) " +
				                             "INNER JOIN solicitudes_medicamentos solmed ON (solmed.numero_solicitud = anulcar.numero_solicitud ) " +
				                             "INNER JOIN cargos_directos cargdir ON ( cargdir.numero_solicitud  = anulcar.numero_solicitud )  " +
				                             "WHERE anulcar.contabilizado = '"+ConstantesBD.acronimoSi+"' AND  sol.tipo = "+ConstantesBD.codigoTipoSolicitudCargosDirectosArticulos+"  AND  " +
				                             "solmed.entidad_subcontratada IS NOT NULL AND  " +
				                             "cargdir.afecta_inventarios = '"+ConstantesBD.acronimoSi+"' AND " +
				                             "to_char(anulcar.fecha_modifica,'YYYY-MM-DD')  BETWEEN  '"+UtilidadFecha.conversionFormatoFechaABD(parametros.get("fechaInicialDesmaracion").toString())+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(parametros.get("fechaFinalDesmarcacion").toString())+"'  " +
				                             " " +
				                             "UNION " +
				                             " " +
				                             "SELECT anulcar.codigo AS codanul " +
				                             "FROM anulacion_cargos_farmacia anulcar " +
				                             "INNER JOIN solicitudes sol ON (sol.numero_solicitud = anulcar.numero_solicitud ) " +
				                             "INNER JOIN solicitudes_medicamentos solmed ON (solmed.numero_solicitud = anulcar.numero_solicitud ) " +
				                             "WHERE anulcar.contabilizado = '"+ConstantesBD.acronimoSi+"' AND sol.tipo = "+ConstantesBD.codigoTipoSolicitudMedicamentos+"  AND " +
				                             "to_char(anulcar.fecha_modifica,'YYYY-MM-DD')  BETWEEN  '"+UtilidadFecha.conversionFormatoFechaABD(parametros.get("fechaInicialDesmaracion").toString())+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(parametros.get("fechaFinalDesmarcacion").toString())+"'  AND " +
				                             "solmed.entidad_subcontratada IS NOT NULL )  tabl "; 
		
		
		String cadenaModificacion = desmarcarAnulacionCargosArticulos;
		resultado.put("estadoCont","");
		int actualizacion=0, contActualizaciones=0;
		resultado.put("actualizo",ConstantesBD.acronimoSi);
		
		try{
	   		 
			 PreparedStatementDecorator ps1 =  new PreparedStatementDecorator(con.prepareStatement(cadenaConsultaAnulacionCargos,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));			 
			 ResultSetDecorator rs = new ResultSetDecorator(ps1.executeQuery());
			 
			 while(rs.next())
			 {
				try
				{					
					 PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadenaModificacion,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));	   		 
			   		 ps.setInt(1, rs.getInt("codanul")); 
			   		 actualizacion = ps.executeUpdate();  
			   		 
			   		 if(actualizacion >= 0 ) 
			   		   {
			   			 if(actualizacion > 0)
			           	  {
			   				contActualizaciones++; 
			           	  }
			   			 
				        }
			            
			   		 ps.close();
					
				}			 
				catch (Exception e) {			
					 e.printStackTrace();					 
					 errores.add("descripcion",new ActionMessage("errors.notEspecific","Se presentaron Errores en la Desmarcación de Anulación Cargos  Articulos"));
			    	 logger.info("\n \n error al  Modificar Anulacion Cargos Articulos >>  cadena >>  "+cadenaModificacion+" ");
			    	 resultado.put("error",errores);
					
				 } 
			 }	 
			
			 rs.close(); 
			 ps1.close();
			 if(contActualizaciones == 0)
			  {
			  resultado.put("actualizo",ConstantesBD.acronimoNo);
			  }
		        if(errores.isEmpty())
		        {     
	            	 resultado.put("estadoCont",ConstantesBD.acronimoSi);	    	    		        	
		        }			 	   		
		 }
			catch (Exception e) {			
				 e.printStackTrace();
				 
				 logger.info("\n \n error al  Consultar Anulacion Cargos >>  cadena >>  "+cadenaConsultaAnulacionCargos+" ");
				
			 }		
		
		return resultado;
		
	}
	
	
	/**
	 * Metodo encargado de realizar el insert del LOG asociado a un tipo de Moviemiento
	 * @param con
	 * @param dto
	 * @return
	 */
	public static HashMap guardarLogInterfaz1E(Connection con, DtoLogInterfaz1E dto)
	{
		ActionErrors errores = new ActionErrors();
		HashMap resultado = new HashMap();
		String cadenaInsercionLogInterfaz= insertarLogInterfaz1EStr;
		 
		resultado.put("codLog","");
		
		int consecutivo = UtilidadBD.obtenerSiguienteValorSecuencia(con,"seq_log_interfaz_1e");
		
		 try{ 
		   		 PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadenaInsercionLogInterfaz,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		   		
		   		 ps.setInt(1, consecutivo);
		   		 ps.setString (2, dto.getUsuarioProcesa());
		   		 ps.setString(3, dto.getTipoMovimiento());
		   		 
		   		 if(!dto.getFechaProceso().equals(""))
		   			 ps.setString(4,dto.getFechaProceso());
		   		 else
		   			ps.setString(4, UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
		   		 
		   		 ps.setInt(5, Utilidades.convertirAEntero(dto.getInstitucion()));
		   		 ps.setString(6,dto.getTipoProceso());
		   		 
		   		 if(!dto.getFechaInicioDesmarcacion().equals(""))
		   			 ps.setString(7,dto.getFechaInicioDesmarcacion());
		   		 else
		   			ps.setNull(7, Types.DATE);
		   		 
		   		 if(!dto.getFechaFinalDesmaracion().equals(""))
		   			 ps.setString(8,dto.getFechaFinalDesmaracion());
		   		 else
		   			ps.setNull(8, Types.DATE);
		   		 
		   		 if(!dto.getMotivoDesmarcacion().equals(""))
		   			 ps.setString(9, dto.getMotivoDesmarcacion());
		   		 else
		   			ps.setNull(9, Types.VARCHAR);
		   		 
		   		 if(dto.getPath().equals(""))
		   		 {
		   			ps.setNull(10, Types.VARCHAR);
		   		 }else
		   		 {
		   			ps.setString(10, dto.getPath());
		   		 }
		   		 
		   		 if(dto.getNombreArchivo().equals(""))
		   		 {
		   			ps.setNull(11,Types.VARCHAR);
		   			
		   		 }else
		   		 {
		   			ps.setString(11, dto.getNombreArchivo());
		   		 }
		   		 
		   		if(dto.getTipoArchivo().equals(""))
		   		 {
		   			ps.setNull(12,Types.VARCHAR);
		   			
		   		 }else
		   		 {
		   			ps.setString(12, dto.getTipoArchivo());
		   		 }
		   		 
		   		 if(ps.executeUpdate()>0)
		   		 {
		   			   resultado.put("codLog",consecutivo);	
		   	          
		   		 }else
		   		 {
		   			 errores.add("descripcion",new ActionMessage("errors.notEspecific","Se presentaron Errores en la Generacion de Documentos de Auditoria"));
					 resultado.put("error",errores);				 			 
				 }
		   		 ps.close();
			 
	   		
		 }
		 catch (Exception e) {			
				 e.printStackTrace();				
				 logger.info("\n \n error al Insertar Log Interfaz 1E >>  cadena >>  "+cadenaInsercionLogInterfaz+" ");
				
			 }	
		
			return resultado;
	
	}
	
	
	/**
	 * Metodo encargado de realizar el insert del LOG asociado a un tipo de Moviemiento y un Tipo de Documento
	 * @param con
	 * @param codLogInterfaz
	 * @param tipoDocumento
	 * @return
	 */
	public static HashMap guardarLogInterfazTiposDoc1E(Connection con, int codLogInterfaz, String tipoDocumento)
	{
		ActionErrors errores = new ActionErrors();
		HashMap resultado = new HashMap();
		String cadenaInsercionLogInterfTiposDoc= insertarLogInterfTiposDoc1EStr;
		resultado.put("codLogDoc","");
		
		try{
  	   		 
  	   		 PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadenaInsercionLogInterfTiposDoc,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
  	   		 ps.setInt(1, codLogInterfaz);
  	   		 ps.setInt(2, Utilidades.convertirAEntero(tipoDocumento));
  	   		 
  	   	        if(ps.executeUpdate()>0)
  	   	         {
  	   	          resultado.put("codLogDoc",codLogInterfaz);
  	   	         }
  	   	          else
  	   	          {
  	   	          errores.add("descripcion",new ActionMessage("errors.notEspecific","Se presentaron Errores en la Generacion de Documentos de Auditoria"));
  	   	          logger.info("\n \n error al  Insertar Log Interfaz Tipos de Documentos>>  cadena >>  "+cadenaInsercionLogInterfTiposDoc+" ");
  	   	          resultado.put("error",errores);				 			 
  	   	         }
  	   	   ps.close();
  	   		 
  		     }
  		      catch (Exception e) {			
  				 e.printStackTrace();				
  				 logger.info("\n \n error al  Insertar Log Interfaz Tipos de Documentos>>  cadena >>  "+cadenaInsercionLogInterfTiposDoc+" ");
  				
  			 }		
		
		return resultado;
	}

	
	
	
	
	
}
