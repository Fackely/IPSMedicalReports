package com.princetonsa.dao.sqlbase.tesoreria;

import java.sql.Connection;
import java.sql.Date;

import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.util.HashMap;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;

import org.apache.log4j.Logger;

public class SqlBaseAprobacionAnulacionDevolucionesDao 
{
	
	
	/**
	 * 
	 */
	public static Logger logger=Logger.getLogger(SqlBaseAprobacionAnulacionDevolucionesDao.class);
	
	
	/**
	 * Cadena para la consulta de las devoluciones
	 */
	private static final String cadenaBuscarStr="SELECT " +
				"drc.codigo as codigodevolucion, " +
				"drc.consecutivo as consecutivo, " +
				"drc.fecha_devolucion as fechadevolucion, " +
				"dc.nombre_beneficiario as nombrebeneficiario, " +
				"dc.tipo_id_beneficiario as tipoidbeneficiario, " +
				"dc.numero_id_beneficiario as numidbeneficiario, " +
				"md.descripcion as motivodevol, " +
				"drc.valor_devolucion as valordevol, " +
				"drc.estado as estadodevol," +
				"drc.numero_rc as numeroRc," +
				"r.consecutivo_recibo as consecutivorc " +
			"FROM " +
				"devol_recibos_caja drc " +
			"inner join recibos_caja r on(r.numero_recibo_caja=drc.numero_rc) " +
			"inner join detalle_conceptos_rc dc on(drc.numero_rc=dc.numero_recibo_caja) " +
			"inner join motivos_devolucion_rc md on(md.codigo=drc.motivo_devolucion) " +
			"inner join cajas c on(c.consecutivo=drc.caja_devolucion) where 1=1";
	
	
	
	/**
	 * Cadena para la consulta del detalle de las devoluciones
	 */
	public static String cadenaConsultaDetalleDevoluciones="SELECT dr.codigo as numdevol, dr.consecutivo as consecutivo, dr.fecha_devolucion as fechaelaboracion, dr.estado as estadodevol, dr.motivo_devolucion as motivodevol, md.descripcion as descripcionmotdevol, dr.valor_devolucion as valordevol, dc.nombre_beneficiario as nombrebeneficiario, dc.tipo_id_beneficiario as tipoidbeneficiario, dc.numero_id_beneficiario as numidbeneficiario, dr.numero_rc as numerorc,r.consecutivo_recibo as consecutivorc, r.fecha as fecharc, r.hora as horarc, dr.forma_pago as formapago, dr.usuario_devolucion as cajero, dr.caja_devolucion as caja, c.descripcion as descripcioncaja, dr.observaciones as observaciones, dr.doc_soporte as documentosoporte, ci.codigo_tipo_ingreso as tipoingreso, ci.codigo as concepto, ci.valor as valorconcepto, dr.motivo_anulacion as motivoanulacion "+
																	"FROM devol_recibos_caja dr inner join recibos_caja r on(r.numero_recibo_caja=dr.numero_rc) inner join detalle_conceptos_rc dc on(dc.numero_recibo_caja=dr.numero_rc) inner join conceptos_ing_tesoreria ci on(dc.concepto=ci.codigo) inner join motivos_devolucion_rc md on(md.codigo=dr.motivo_devolucion) inner join cajas c on(c.consecutivo=dr.caja_devolucion) "+
																	"WHERE dr.codigo=?";
	
	
	
	/**
	 * Cadena para la consulta del detalle de las devoluciones
	 */
	public static String cadenaConsultaDetalleDevolucionesConsecutivo="SELECT " +
																			" dr.codigo as numdevol, " +
																			" dr.consecutivo as consecutivo, " +
																			" dr.fecha_devolucion as fechaelaboracion, " +
																			" dr.estado as estadodevol, " +
																			" dr.motivo_devolucion as motivodevol, " +
																			" md.descripcion as descripcionmotdevol, " +
																			" dr.valor_devolucion as valordevol, " +
																			" dc.nombre_beneficiario as nombrebeneficiario, " +
																			" dc.tipo_id_beneficiario as tipoidbeneficiario, " +
																			" dc.numero_id_beneficiario as numidbeneficiario, " +
																			" dr.numero_rc as numerorc, " +
																			" r.consecutivo_recibo as consecutivorc," +
																			" r.fecha as fecharc, " +
																			" r.hora as horarc, " +
																			" dr.forma_pago as formapago, " +
																			" dr.usuario_devolucion as cajero, " +
																			" dr.caja_devolucion as caja, " +
																			" c.descripcion as descripcioncaja, " +
																			" dr.observaciones as observaciones, " +
																			" dr.doc_soporte as documentosoporte, " +
																			" ci.codigo_tipo_ingreso as tipoingreso, " +
																			" ci.codigo as concepto, " +
																			" ci.valor as valorconcepto, " +
																			" dr.motivo_anulacion as motivoanulacion "+
																	"FROM devol_recibos_caja dr inner join recibos_caja r on(r.numero_recibo_caja=dr.numero_rc) inner join detalle_conceptos_rc dc on(dc.numero_recibo_caja=dr.numero_rc) inner join conceptos_ing_tesoreria ci on(dc.concepto=ci.codigo) inner join motivos_devolucion_rc md on(md.codigo=dr.motivo_devolucion) inner join cajas c on(c.consecutivo=dr.caja_devolucion) "+
																	"WHERE dr.consecutivo=?";
	
	/**
	 * cadena para actualizar la devolucion del recibo de caja Anulados
	 */
	private static final String actualizarDevolReciboCajaAnuladosStr="UPDATE " +
			"devol_recibos_caja SET usuario_anulacion=?,fecha_anulacion=?,hora_anulacion=?,estado=?,motivo_anulacion=?,turno_afecta=? WHERE codigo=?";
	
	
	/**
	 * cadena para actualizar la devolucion del recibo de caja Aprobados
	 */
	private static final String actualizarDevolReciboCajaStr="UPDATE " +
			"devol_recibos_caja SET usuario_aprobacion=?,fecha_aprobacion=?,hora_aprobacion=?,estado=?,turno_afecta=? WHERE codigo=?";
	

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
     * 
     */
    private static String consultarValorConceptosRecibosCajaStr="SELECT " + 
    	 	              "crc.valor as valorconceptorc " +    	 	              
    	 	              "FROM detalle_conceptos_rc crc " +
    	 	              "inner join conceptos_ing_tesoreria cit on (crc.concepto=cit.codigo and crc.institucion=crc.institucion) " +
    	 	              "where crc.numero_recibo_caja =? and crc.institucion = ?";
    
    
	
	/**
	 * 
	 * @param con
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param devolInicial
	 * @param devolFinal
	 * @param motivoDevolucion
	 * @param tipoId
	 * @param numeroId
	 * @param centroAtencion
	 * @param caja
	 * @param reciboCaja 
	 * @return
	 */
	public static HashMap BusquedaDevoluciones(Connection con, String fechaInicial, String fechaFinal, String devolInicial, String devolFinal, String motivoDevolucion, String tipoId, String numeroId, String centroAtencion, String caja, String reciboCaja) 
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros", "0");
		
		String cadena=cadenaBuscarStr;
		if((!fechaInicial.equals("")) && (!fechaFinal.equals("")))
		{
			cadena+=" AND  drc.fecha_devolucion between '"+UtilidadFecha.conversionFormatoFechaABD(fechaInicial)+"' and '"+UtilidadFecha.conversionFormatoFechaABD(fechaFinal)+"'";
		}
		if((!devolInicial.equals("")) && (!devolFinal.equals("")))
		{
			cadena+=" AND  drc.consecutivo between "+devolInicial+" and "+devolFinal;
		}
		if(!reciboCaja.equals(""))
		{
			cadena+=" AND r.consecutivo_recibo='"+reciboCaja+"'";
		}
		if(!motivoDevolucion.equals(""))
		{
			cadena+=" AND drc.motivo_devolucion='"+motivoDevolucion+"'";
		}
		if(!tipoId.equals(""))
		{
			cadena+=" AND dc.tipo_id_beneficiario='"+tipoId+"'";
		}
		if(!numeroId.equals(""))
		{
			cadena+=" AND dc.numero_id_beneficiario='"+ numeroId+"'";
		}
		if(!centroAtencion.equals(""))
		{
			cadena+=" AND c.centro_atencion="+centroAtencion;
		}
		if(!caja.equals(""))
		{
			cadena+=" AND drc.caja_devolucion="+caja;
		}
		
		/*
		 * Según tarea 1808
		 * Se agrega el filtro de estado Generado
		 */
		cadena+=" AND drc.estado = 'GEN' ";
		cadena+="  order by drc.consecutivo";
		
		logger.info(">>> Consulta devoluciones = "+cadena);
		
		try
		{
			PreparedStatementDecorator busqueda= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(busqueda.executeQuery()));
		}
		catch(SQLException e)
		{
		e.printStackTrace();
		}
		return mapa;
	}

	/**
	 * 
	 * @param con
	 * @param devolucion
	 * @return
	 */
	public static HashMap ConsultaDetalleDevolucion(Connection con, int devolucion) 
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros", "0");
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaConsultaDetalleDevoluciones,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, devolucion);
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		}
		catch (SQLException e) 
		{
			logger.info("ERROR AL EJECUTAR LA CONSULTA DEL DETALLE DE LAS DEVOLUCIONES "+e);
			e.printStackTrace();
		}
		return mapa;
	}
	
	/**
	 * 
	 * @param con
	 * @param recibo
	 * @return
	 */
	public static String ConsultaEstadoRecibo(Connection con, int recibo) 
	{
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement("select r.estado as recibo from recibos_caja r inner join devol_recibos_caja dr on(dr.numero_rc=r.numero_recibo_caja) where dr.codigo="+recibo,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				return rs.getString("recibo");
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return "";
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoDevol
	 * @return
	 */
	public static String consultaTipoConcepto(Connection con, int codigoDevol) 
	{
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement("select c.codigo_tipo_ingreso as tipoconcepto from devol_recibos_caja dr inner join conceptos_ing_tesoreria c on(c.codigo=dr.concepto) where dr.codigo="+codigoDevol,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				return rs.getString("tipoconcepto");
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return "";
	}
	
	/**
	 * 
	 * @param con
	 * @param documento
	 * @return
	 */
	public static String consultaPagoConvenio(Connection con, int documento) 
	{
		String resultado="";
		try
		{
			logger.info(">>> Documento = "+documento);
			String consulta1 = "select " +
					"ap.codigo  as documento " +
					"from devol_recibos_caja dr " +
					"inner join pagos_general_empresa p on(p.documento=dr.numero_rc) " +
					"inner join cartera.aplicacion_pagos_empresa ap on(ap.pagos_general_empresa=p.codigo) " +
					"where dr.codigo="+documento;
			/*
			 * Según tarea: 1808
			 * Se agrega el filtro de estado Generado
			 */
			consulta1+= " AND dr.estado = 'GEN' ";
			logger.info(">>> Consulta 1: "+consulta1);
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consulta1,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			String consulta2 = "select " +
				"ap.codigo  as documento " +
				"from devol_recibos_caja dr " +
				"inner join pagos_general_empresa p on(p.documento=dr.numero_rc) " +
				"inner join capitacion.aplicacion_pagos_capitacion ap on(ap.pagos_general_empresa=p.codigo) " +
				"where dr.codigo="+documento;
			/*
			 * Según Tarea 1808
			 * Se agrega el filtro de estado Generado
			 */
			consulta2+= " AND dr.estado = 'GEN' ";
			if(rs.next())
			{
				resultado=rs.getString("documento");
			}
			if(UtilidadTexto.isEmpty(resultado))
			{
				logger.info(">>> Consulta 2: "+consulta2);
				ps= new PreparedStatementDecorator(con.prepareStatement(consulta2,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				rs=new ResultSetDecorator(ps.executeQuery());
				if(rs.next())
				{
					resultado=rs.getString("documento");
				}
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return resultado;
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoDevol
	 * @param estadoDevolucion
	 * @param loginUsuario
	 * @param motivoAnulacion
	 * @param valor
	 * @return
	 */
	public static int insertarDevolucion(Connection con, int codigoDevol, String estadoDevolucion, String loginUsuario, String motivoAnulacion, double valor, long turnoAfecta) 
	{
		int resp=0;
		
		double resultadoV=0.0;
		
		//String consulta="select dc.valor - (getValoDevolAproRC(dr.numero_rc,dr.institucion)+ "+valor+") as valor from devol_recibos_caja dr inner join detalle_conceptos_rc dc on(dc.numero_recibo_caja=dr.numero_rc) where dr.numero_rc=(select numero_rc from devol_recibos_caja where codigo="+codigoDevol+")";
		
		try
		{
			
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement("select dc.valor - (getValoDevolAproRC(dr.numero_rc,dr.institucion)+ "+valor+") as valor from devol_recibos_caja dr inner join detalle_conceptos_rc dc on(dc.numero_recibo_caja=dr.numero_rc) where dr.codigo="+codigoDevol,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			
			if(rs.next())
			{
				resultadoV=rs.getDouble("valor");
			}
			if(estadoDevolucion.equals("APR"))
			{
				if(resultadoV>=0)
				{
					
					PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(actualizarDevolReciboCajaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					
					pst.setString(1,loginUsuario);
					pst.setString(2,UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
					pst.setString(3,UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getHoraActual()));
					pst.setString(4,estadoDevolucion);
					pst.setLong(5, turnoAfecta);
					pst.setInt(6,codigoDevol);
					
					resp=pst.executeUpdate();				
				
		}
				else
				{
					return 0;
				}
			}
			if(estadoDevolucion.equals("ANU"))
			{
				PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(actualizarDevolReciboCajaAnuladosStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				
				pst.setString(1,loginUsuario);
				pst.setString(2,UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
				pst.setString(3,UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getHoraActual()));
				pst.setString(4,estadoDevolucion);
				pst.setString(5,motivoAnulacion);
				pst.setLong(6, turnoAfecta);
				pst.setInt(7,codigoDevol);
				
				resp=pst.executeUpdate();
			}
			
			if(resp>0)
				return resp;
			else
				return -1;
		}
		catch(SQLException e)
		{
			logger.error("Error en actualizarDevolucionRC de SqlUtilidadesDao: "+e);
			return -1;
		}
	}
	
	/**
	 * 
	 * @param con
	 * @param devolucion
	 * @return
	 */
	public static HashMap consultaDetalleDevolucionConsecutivo(Connection con, int devolucion) 
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros", "0");
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaConsultaDetalleDevolucionesConsecutivo,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, devolucion);
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		}
		catch (SQLException e) 
		{
			logger.info("ERROR AL EJECUTAR LA CONSULTA DEL DETALLE DE LAS DEVOLUCIONES "+e);
			e.printStackTrace();
		}
		return mapa;
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
    	boolean retorna=false;
    	String cadenaUdateValorRecConvenio= actualizarValorValorAntiRecConvenio;
    	try
    	{
        	PreparedStatementDecorator ps1= new PreparedStatementDecorator(con.prepareStatement(cadenaUdateValorRecConvenio,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
        	ps1.setDouble(1,valorAntRecConvenio);
        	ps1.setString(2, usuario);
			ps1.setDate(3, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual(con))));
			ps1.setString(4, UtilidadFecha.getHoraActual(con));
        	ps1.setInt(5, codContrato); 
        	
        	logger.info("Cadena Actualizacion ValorRec Convenio >>"+cadenaUdateValorRecConvenio+"  ValorConvenio >>"+valorAntRecConvenio+" CodContrato >>"+ codContrato);
        	retorna= (ps1.executeUpdate()>0);
        	ps1.close();
    	}
    	catch(SQLException e)
	    {
		  logger.error("Error Actualizando Valor Anticipo Recibido del Convenio [SqlBaseAnulacionReciboCajaDao]"+e);
		}
 		return retorna;
    	
    	
    }
	
	
    /**
     * Metodo para consultar los diferentes Conceptos del Recibo de Caja, y obtener el Valor de cada uno 
     * @param con
     * @param numReciboCaja
     * @param codInstitucion
     * @return
     */
    public static ResultSetDecorator consultarValorConceptosRecibosCaja(Connection con, String numReciboCaja, int codInstitucion)
    {
    	String cadena = consultarValorConceptosRecibosCajaStr;
    	
    	try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1,numReciboCaja);
			ps.setInt(2,codInstitucion);
			return new ResultSetDecorator(ps.executeQuery());
			
		}
		catch(SQLException e)
		{
		    logger.error("Error Consultando Valor de los conceptos del ReciboCaja [SqlBaseAnulacionReciboCajaDao]"+e);
		}
		return null;
    	
    }	
	
	
}
