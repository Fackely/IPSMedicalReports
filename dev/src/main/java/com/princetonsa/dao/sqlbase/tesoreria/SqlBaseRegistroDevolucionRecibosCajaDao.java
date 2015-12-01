/**
 * 
 */
package com.princetonsa.dao.sqlbase.tesoreria;

import java.sql.Connection;

import com.lowagie.text.pdf.PRStream;
import com.princetonsa.dao.sqlbase.SqlBaseUtilidadesBDDao;
import com.princetonsa.decorator.PreparedStatementDecorator;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.odontologia.DtoVentaTarjetasCliente;
import com.princetonsa.dto.tesoreria.DtoProcesoDevolucionRC;
import com.princetonsa.mundo.UsuarioBasico;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;

/**
 * @author axioma
 *
 */
public class SqlBaseRegistroDevolucionRecibosCajaDao 
{
	
	/**
	 * 
	 */
	public static Logger logger=Logger.getLogger(SqlBaseRegistroDevolucionRecibosCajaDao.class);
	
	/**
	 * 
	 * 
	 * 
	 */
	/*private static String cadenaConsultaBusquedaRecibosCaja="SELECT " +
																	" rc.numero_recibo_caja as numerorc," +
																	" to_char(rc.fecha,'dd/mm/yyyy')||' '||substr(hora::text,0,6) as fechahoraelaboracion , " +
																	" rc.fecha as fechaoriginal, " +
																	" dcrc.valor - getValoDevolAproRC(rc.numero_recibo_caja,rc.institucion) as valor, " +
																	" dcrc.nombre_beneficiario ||' - '|| coalesce(dcrc.tipo_id_beneficiario||': ','')||dcrc.numero_id_beneficiario as beneficiario, " +
																	" dcrc.tipo_id_beneficiario as idbeneficiario, " +
																	" dcrc.numero_id_beneficiario as numidbeneficiario, " +
																	" rc.estado as codigoestado, " +
																	" erc.descripcion as descestado, " +
																	" dcrc.doc_soporte as docsoporte, " +
																	" dcrc.concepto as concepto " +
														" from recibos_caja rc " +
														" inner join detalle_conceptos_rc dcrc on(rc.numero_recibo_caja=dcrc.numero_recibo_caja and rc.institucion=dcrc.institucion) " +
														" inner join estados_recibos_caja erc on(rc.estado=erc.codigo) ";
	*/

	private static String sqlConsultarDevolucionReciboCaja = "SELECT codigo FROM devol_recibos_caja WHERE institucion=? AND numero_rc=? ";
	
	/**
	 * 
	 */
	private static String cadenaInsertarDevolucionRC="INSERT INTO devol_recibos_caja(" +
																" codigo," +
																" consecutivo," +
																" institucion," +
																" numero_rc," +
																" motivo_devolucion," +
																" valor_devolucion," +
																" forma_pago," +
																" observaciones," +
																" doc_soporte," +
																" concepto," +
																" estado," +
																" usuario_devolucion," +
																" caja_devolucion," +
																" fecha_devolucion," +
																" hora_devolucion," +
																" usuario_aprobacion," +
																" fecha_aprobacion," +
																" hora_aprobacion, " +
																" turno_afecta " +
																" ) " +
																"values(" +
																	" ?,?,?,?,?," +
																	" ?,?,?,?,?," +
																	" ?,?,?,?,?," +
																	" ?,?,?,? " +
																	" )";
	
	public static String consultaStrDD;
	
	
	private static String strConsultaValorConceptoIngTes = "SELECT " +
			"cit.valor AS valor " +
			"FROM conceptos_ing_tesoreria cit " +
			"WHERE cit.codigo = ? " +
			"AND cit.institucion = ? " +
			"AND codigo_tipo_ingreso = ? ";
	
	/**
	 * 
	 * @param numeroRecibo
	 * @return
	 */
    public  static ArrayList<DtoVentaTarjetasCliente> consultaFacturas(String numeroRecibo){
    
    	ArrayList<DtoVentaTarjetasCliente> resultado= new ArrayList<DtoVentaTarjetasCliente>();
    	
    	String consulta="SELECT " +
    
	"vtar.codigo_pk as codigo_venta  "+
	"FROM  tesoreria.recibos_caja rc " +
	"INNER JOIN  cartera.pagos_general_empresa pge ON (pge.documento = rc.numero_recibo_caja and pge.tipo_doc="+ConstantesBD.codigoTipoDocumentoPagosReciboCaja+")  "+
	"INNER JOIN facturasvarias.aplicacion_pagos_fvarias apf ON(apf.pagos_general_fvarias = pge.codigo) "+
	"INNER JOIN facturasvarias.aplicac_pagos_factura_fvarias apfv ON (apfv.aplicacion_pagos = apf.codigo) " +
	"INNER JOIN facturasvarias.facturas_varias fv ON (fv.codigo_fac_var = apfv.factura) " +
	"INNER JOIN odontologia.venta_tarjeta_cliente vtar ON (vtar.codigo_fac_var = fv.codigo_fac_var)  "+
	"INNER JOIN facturasvarias.conceptos_facturas_varias cfv ON (cfv.consecutivo = fv.concepto)"+
	
	"Where rc.consecutivo_recibo='"+numeroRecibo + "'   and cfv.tipo_concepto='"+ConstantesIntegridadDominio.acronimoTipoVentaTarjetaodontologica+"'";
		
    	
    	logger.info("LA CONSULTA ES ************************************** +++++++++++ "+ consulta);
    	try 
		 {
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consulta+" order by codigo_pk ",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			 {
				DtoVentaTarjetasCliente newdto = new DtoVentaTarjetasCliente();
				newdto.setCodigoPk(rs.getDouble("codigo_venta"));
				
				resultado.add(newdto);
			 }
		 SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
		 }
		catch (SQLException e) 
		{
			logger.error("error en carga==> "+e);
			
		}
		return resultado;
    }

	/**
	 * 
	 * @param vo
	 * @return
	 */
	public static HashMap<String, Object> consultarRecibosCaja(HashMap<String, Object> vo, String cadenaSql) 
	{
		String cadenaConsultaBusquedaRecibosCaja=cadenaSql;

		HashMap<String, Object> resultado=new HashMap<String, Object>();
		resultado.put("numRegistros", "0");
		Connection con=UtilidadBD.abrirConexion();
		String cadena=cadenaConsultaBusquedaRecibosCaja;
		try 
		{
			if(vo.containsKey("numReciboCaja")&&!UtilidadTexto.isEmpty(vo.get("numReciboCaja")+""))
			{
				/*
				 * Se agrega el último filtro de where de acuerdo a la tarea: 2329
				 */
				cadena=cadenaConsultaBusquedaRecibosCaja +" WHERE rc.consecutivo_recibo='"+vo.get("numReciboCaja")+
						"' AND rc.institucion="+vo.get("institucion")+
						" AND (tesoreria.GETVALORTOTALRC(rc.numero_recibo_caja) - getValoDevolAproRC(rc.numero_recibo_caja,rc.institucion)) > 0 ";
				//cadena=cadena+" and rc.estado in("+ConstantesBD.codigoEstadoReciboCajaEnArqueo+","+ConstantesBD.codigoEstadoReciboCajaRecaudado+","+ConstantesBD.codigoEstadoReciboCajaEnCierre+") ";				
			}
			else
			{
				cadena=cadenaConsultaBusquedaRecibosCaja;
				if(vo.containsKey("centroAtencion") &&!UtilidadTexto.isEmpty(vo.get("centroAtencion")+""))
				{
					cadena=cadena+" inner join cajas c on (rc.caja=c.consecutivo) ";
				}
				//las fechas son requeridas, siempre debe estar no es necesario validar que el vo los contenga.
				cadena=cadena+" WHERE rc.institucion="+vo.get("institucion") +" and to_char(rc.fecha,'YYYY-MM-DD') between '"+UtilidadFecha.conversionFormatoFechaABD(vo.get("fechaInicial")+"")+"' and '"+UtilidadFecha.conversionFormatoFechaABD(vo.get("fechaFinal")+"")+"'";
				if(vo.containsKey("tipoIDBeneficiario") && vo.containsKey("numIDBeneficiario") &&!UtilidadTexto.isEmpty(vo.get("tipoIDBeneficiario")+"") &&!UtilidadTexto.isEmpty(vo.get("numIDBeneficiario")+""))
				{
					cadena=cadena+" and dcrc.tipo_id_beneficiario='"+vo.get("tipoIDBeneficiario")+"' and dcrc.numero_id_beneficiario='"+vo.get("numIDBeneficiario")+"' ";
				}
				if(vo.containsKey("conceptoRC") &&!UtilidadTexto.isEmpty(vo.get("conceptoRC")+""))
				{
					cadena=cadena+" and dcrc.concepto='"+vo.get("conceptoRC")+"' ";
				}
				if(vo.containsKey("estadoRC")  &&!UtilidadTexto.isEmpty(vo.get("estadoRC")+""))
				{
					cadena=cadena+" and rc.estado='"+vo.get("estadoRC")+"' ";
				}
				if(vo.containsKey("centroAtencion") &&!UtilidadTexto.isEmpty(vo.get("centroAtencion")+""))
				{
					cadena=cadena+" and c.centro_atencion="+vo.get("centroAtencion");
				}
				cadena=cadena+" and rc.estado in("+
					ConstantesBD.codigoEstadoReciboCajaEnArqueo+","+
					ConstantesBD.codigoEstadoReciboCajaRecaudado+","+
					ConstantesBD.codigoEstadoReciboCajaEnCierre+") "+
					"AND (tesoreria.GETVALORTOTALRC(rc.numero_recibo_caja) - getValoDevolAproRC(rc.numero_recibo_caja,rc.institucion) > 0) "+//Según tarea 1632
					"order by rc.consecutivo_recibo";				
			}
			//cadena=cadena+" group by numerorc,fechahoraelaboracion,fechaoriginal,beneficiario,idbeneficiario,numidbeneficiario,codigoestado,descestado,docsoporte,concepto,rc.institucion";
			logger.info(cadena);
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena));
			resultado=(HashMap<String, Object>)UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery())).clone();
		} 
		catch (SQLException e) 
		{
			logger.error("errro [consultarRecibosCaja] -->"+e);
		}
		UtilidadBD.closeConnection(con);
		return resultado;
	}

	/**
	 * 
	 * @param vo
	 * @return
	 */
	public static boolean guardarDevolucion(HashMap<String, Object> vo) 
	{
		Connection con=UtilidadBD.abrirConexion();
		boolean guardarDevolucion = true;
		
		//por solicitud de pruebas German 2009-12-09 permitir hacer n devoluciones parciales ......
		/*try {
			PreparedStatementDecorator ps = new PreparedStatementDecorator(con.prepareStatement(sqlConsultarDevolucionReciboCaja+" AND estado != 'ANU' "));
			ps.setObject(1, vo.get("institucion")+"");
			ps.setObject(2, vo.get("numeroRC")+"");
			
			if(ps.executeQuery().next()){
				guardarDevolucion = false;
			}
			
		} catch (SQLException e) {
			logger.error("ERROR [sqlConsultarDevolucionReciboCaja] --> "+e);
		}*/
		
		if(guardarDevolucion) {
			
			try 
			{
				PreparedStatementDecorator ps= new PreparedStatementDecorator(con,cadenaInsertarDevolucionRC);
				ps.setObject(1, UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_devol_rc"));
				ps.setObject(2,vo.get("consecutivo")+"");
				ps.setObject(3,vo.get("institucion")+"");
				ps.setObject(4, vo.get("numeroRC")+"");
				ps.setObject(5,vo.get("motivoDevolucion")+"");
				ps.setObject(6,vo.get("valorDevolucion")+"");
				ps.setObject(7,vo.get("formaPago")+"");
				ps.setObject(8,vo.get("observaciones")+"");
				ps.setObject(9,vo.get("docSoporte")+"");
				ps.setObject(10,vo.get("concepto")+"");
				ps.setObject(11,vo.get("estado")+"");
				ps.setObject(12,vo.get("usuarioGenera")+"");
				ps.setObject(13,vo.get("caja")+"");
				ps.setObject(14,UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual(con)+""));
				ps.setObject(15,UtilidadFecha.getHoraActual(con)+"");
				if((vo.get("estado")+"").equals(ConstantesIntegridadDominio.acronimoEstadoAprobado))
				{
					ps.setObject(16,vo.get("usuarioGenera")+"");
					ps.setObject(17,UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual(con)+""));
					ps.setObject(18,UtilidadFecha.getHoraActual(con)+"");
					
				}
				else
				{
					ps.setObject(16,null);
					ps.setObject(17,null);
					ps.setObject(18,null);
				}
				ps.setObject(19,vo.get("turnoCaja")+"");
				logger.info("\n\nquery aprobar devolucion::: "+ps);
				if(ps.executeUpdate()>0)
				{
					//DatosFinanciacion.actualizarDocGarantiaAnuRC(con, ingreso, tipo_documento, consecutivo, anio_consecutivo)
					ps.close();
					UtilidadBD.closeConnection(con);
					return true;
				}
				ps.close();
			} 
			catch (SQLException e) 
			{
				logger.error("ERROR [guardarDevolucion] --> "+e);
			}
		}

		UtilidadBD.closeConnection(con);
		return false;
	}
	
	
	/**
	 * se obtiene el valor del concepto de ingreso de tesoreria
	 * @param parametros
	 * @return String 
	 */
	public static String getValorConceptoIngTesoreria(HashMap parametros)
	{
		String resultado = "";
		Connection con=UtilidadBD.abrirConexion();
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(strConsultaValorConceptoIngTes,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            ps.setString(1, parametros.get("codigo_cit").toString());
            ps.setInt(2, Utilidades.convertirAEntero(parametros.get("cod_institucion").toString()));
            ps.setInt(3, Utilidades.convertirAEntero(parametros.get("cod_tipo_ingreso").toString()));
            ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
            if(rs.next())
            	resultado = rs.getString("valor");
            ps.close();
		}catch(Exception e)
		{
			logger.info("La consulta >>>>> "+strConsultaValorConceptoIngTes);
            logger.info("codigo concepto ing tesoreria >>>> "+parametros.get("codigo_cit").toString());
            logger.info("codigo institucion >>>> "+parametros.get("cod_institucion").toString());
            logger.info("codigo codigo_tipo_ingrso >>>> "+parametros.get("cod_tipo_ingreso").toString());
			logger.error("errro [getValorConceptoIngTesoreria] -->"+e);
		}
		UtilidadBD.closeConnection(con);
		return resultado;
	}
	
	
	/**
	 * 
	 * @param con
	 * @param arqueo
	 * @param cierre
	 */
	public static boolean actualizarEstadoArqueoCierreDevol(Connection con, String arqueo, String cierreCaja, ArrayList<Integer> codigosPkDevol)
	{
		boolean retorna=false;
		String consultaStr="";
			
		if(!UtilidadTexto.isEmpty(arqueo))
		{
			consultaStr="UPDATE devol_recibos_caja set arqueo_definitivo='"+arqueo+"' where codigo in("+UtilidadTexto.convertirArrayIntegerACodigosSeparadosXComas(codigosPkDevol)+") ";
		}
		else if(!UtilidadTexto.isEmpty(cierreCaja))
		{
			consultaStr="UPDATE devol_recibos_caja set cierre_caja='"+cierreCaja+"' where codigo in("+UtilidadTexto.convertirArrayIntegerACodigosSeparadosXComas(codigosPkDevol)+") ";
		}
		if(!UtilidadTexto.isEmpty(arqueo) || !UtilidadTexto.isEmpty(cierreCaja))
		{	
			try
			{
				logger.info("UPADTE-->"+consultaStr);
				PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	            if(ps.executeUpdate()>0)
	            {
	            	retorna=true;
	            }
	            ps.close();
			}
			catch(SQLException e)
			{
				logger.error("ERRROR ", e);
			}
		}	
		return retorna;
	}
	
	
	
	/**
	 * 
	 */
	public static DtoProcesoDevolucionRC estaEnProcesoDevolucion(String nroRC,int institucion, String idSesionOPCIONAL, boolean igualSession)
	{
		DtoProcesoDevolucionRC dto=new DtoProcesoDevolucionRC();
		String cadena	=	"SELECT " +
								"numero_rc AS rc," +
								"institucion," +
								"usuario," +
								"to_char(fecha,'dd/mm/yyyy') AS fecha," +
								"hora," +
								"id_sesion AS idsesion " +
							"FROM " +
								"tesoreria.proceso_devolucion_rc " +
							"WHERE " +
								"numero_rc=? " +
							"AND " +
								"institucion=?";
		
		if(!UtilidadTexto.isEmpty(idSesionOPCIONAL))
		{
			if(!igualSession)
			{	
				cadena+=" AND id_sesion<> '"+idSesionOPCIONAL+"' ";
			}	
			else
			{
				cadena+=" AND id_sesion = '"+idSesionOPCIONAL+"' ";
			}	
		}
		
		try
		{
			logger.info("estaEnProcesoDevolucion-->"+cadena);
			Connection con= UtilidadBD.abrirConexion();
			
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con,cadena);
			
			ps.setString(1, nroRC);
			ps.setInt(2, institucion);
			
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			
			if(rs.next())
			{
				dto.setNumeroRC(rs.getString("rc"));
				dto.setInstitucion(rs.getInt("institucion"));
				dto.setUsuario(rs.getString("usuario"));
				dto.setFecha(rs.getString("fecha"));
				dto.setHora(rs.getString("hora"));
				dto.setIdSesion(rs.getString("idsesion"));
			}
			rs.close();
			ps.close();
			UtilidadBD.closeConnection(con);
		}
		catch (SQLException e)
		{
			logger.error("\n\nError verificando si la cuenta se encuentra en proceso de devolucion "+e);
		}
		
		return dto;
	}
	
	/**
	 * 
	 */
	public static boolean empezarBloqueoDevolucion(String nroRC,int institucion,String loginUsuario,String idSesion)
	{
		boolean retorna=false;
		
		String cadena=	"INSERT INTO tesoreria.proceso_devolucion_rc (numero_rc,institucion, usuario, fecha, hora, id_sesion) " +
							" VALUES " +
						"(?,?,?,CURRENT_DATE,?,?) ";
		
		try
		{
			logger.info("estaEnProcesoDevolucion-->"+cadena);
			Connection con= UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con,cadena);
			
			ps.setString(1, nroRC);
			ps.setInt(2, institucion);
			ps.setString(3, loginUsuario);
			ps.setString(4, UtilidadFecha.getHoraActual());
			ps.setString(5, idSesion);
			
			retorna= ps.executeUpdate()>=0;
			ps.close();
			UtilidadBD.closeConnection(con);
			
			
		}
		catch (SQLException e)
		{
			logger.error("\n\nHay una excepcion comenzando el bloqueo!! "+e);
			retorna=false;
			
		}
		
		return retorna;
	}
	
	/**
	 * 
	 */
	public static boolean cancelarBloqueoDevolucion(String nroRC, int institucion, String idSesion)
	{
		boolean retorna=false;
		String cadena	=	"DELETE FROM tesoreria.proceso_devolucion_rc WHERE numero_rc=? AND institucion=?";
		try 
		{
			Connection con= UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con,cadena);
			
			ps.setString(1, nroRC);
			ps.setInt(2, institucion);
			
			retorna= ps.executeUpdate()>0;
			ps.close();
			UtilidadBD.closeConnection(con);
			
		}
		catch (SQLException e) 
		{
			logger.error("Error al intentar cancelar el bloquedo de devolucion! ", e);
			retorna=false;
		}
		return retorna;
	}
}
