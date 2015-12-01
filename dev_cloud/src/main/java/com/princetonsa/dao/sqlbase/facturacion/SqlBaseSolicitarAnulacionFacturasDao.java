package com.princetonsa.dao.sqlbase.facturacion;

import java.sql.Connection;
import java.sql.Date;
import com.princetonsa.decorator.PreparedStatementDecorator;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.princetonsa.decorator.ResultSetDecorator;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.Utilidades;

public class SqlBaseSolicitarAnulacionFacturasDao 
{
	
	
	
	/**
	 * 
	 */
	public static Logger logger=Logger.getLogger(SqlBaseSolicitarAnulacionFacturasDao.class);
	
	
	/**
	 * 
	 */
	private static final String consultaFacturaConsecutivo = "SELECT consecutivofactura, " +
															 "codigo as codigofactura " +
															 "FROM view_facturas_internas " +
															 "WHERE consecutivofactura=? AND institucion=? ";
	
	
	/**
	 * 
	 */
	private static final String consultaSolicitudAnulacionStr = "SELECT s.centro_atencion_solicita as centroatencion, " +
															   "getnomcentroatencion(s.centro_atencion_solicita) as descentroatencion, " +
															   "s.codigo_pk as numerosolicitud, " +
															   "s.fecha_solicita as fechasolicitud, " +
															   "s.hora_solicita as horasolicitud, " +
															   "s.consecutivo_factura as consecutivofactura, " +
															   "f.fecha as fechafactura, " +
															   "u.usuario as usuarioautoriza, " +
															   "m.descripcion as descmotivoanul, " +
															   "s.observaciones_sol as observaciones, " +
															   "s.observaciones_anul as observacionesanul, " +
															   "s.usuario_anula as usuarioanula, " +
															   "s.fecha_anula as fechaanula, " +
															   "s.hora_anula as horaanula " +
															   "FROM solicitud_anul_fact s " +
															   "INNER JOIN facturas f ON(s.codigo_factura=f.codigo) " +
															   "INNER JOIN motivos_anul_fact m ON(s.motivo_anulacion=m.codigo) " +
															   "INNER JOIN usu_autorizan_anul_fac u ON(u.codigo=s.usuario_autoriza) " +
															   "WHERE codigo_pk=? ";
	
	
	/**
	 * 
	 */
	private static final String consultaAnularSolicitudesStr = "SELECT s.codigo_pk as numerosolicitud, " +
															   "getnomcentroatencion(s.centro_atencion_solicita) as centroatencion, " +
															   "s.codigo_factura as codigofactura, " +
															   "s.consecutivo_factura as consecutivofactura, " +
															   "s.usuario_autoriza as usuarioautoriza, " +
															   "s.fecha_solicita as fechasolicita, " +
															   "s.hora_solicita as horasolicita, " +
															   "f.fecha as fechafactura, " +
															   "u.usuario as descusuarioautoriza  " +
															   "FROM solicitud_anul_fact s " +
															   "INNER JOIN facturas f ON(f.codigo=s.codigo_factura) " +
															   "INNER JOIN usu_autorizan_anul_fac u ON(u.codigo=s.usuario_autoriza) " +
															   "WHERE estado='"+ConstantesIntegridadDominio.acronimoEstadoPendiente+"' ";
	
	
	
	/**
	 * 
	 */
	private static  final String cadenaInsertarSolicitudesAnulacionStr= "INSERT INTO solicitud_anul_fact (codigo_pk, codigo_factura, consecutivo_factura, usuario_autoriza, motivo_anulacion, observaciones_sol, estado, centro_atencion_solicita, usuario_solicita, fecha_solicita, hora_solicita) VALUES(?,?,?,?,?,?,?,?,?,?,?) ";
	
	
	/**
	 * 
	 */
	private static final String cadenaModificacionAnularSolicitudesStr = "UPDATE solicitud_anul_fact SET observaciones_anul=? , estado=?, usuario_anula=?, fecha_anula=?, hora_anula=? WHERE codigo_pk=? ";
	
	
	
	
	/**
	 * 
	 * @param con
	 * @param consecutivoFactura
	 * @param codigoInstitucion
	 * @return
	 */
	public static HashMap consultarFactura(Connection con, String consecutivoFactura, int codigoInstitucion) 
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros", "0");
		try
		{
			
				PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consultaFacturaConsecutivo,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				
				logger.info("CADENA >>>>>>>>"+consultaFacturaConsecutivo);
				logger.info("consecutivoFactura >>>>>>>>"+consecutivoFactura);
				logger.info("codigoInstitucion >>>>>>>>"+codigoInstitucion);
				
				ps.setInt(1, Utilidades.convertirAEntero(consecutivoFactura));
				ps.setInt(2, codigoInstitucion);
				mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		}
		catch (SQLException e) 
		{
			logger.info("ERROR AL EJECUTAR LA CONSULTA DE USUARIOS AUTORIZADOS PARA AUTORIZAR "+e);
			e.printStackTrace();
		}
		return mapa;
	}


	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public static int insertarSolictud(Connection con, HashMap vo) 
	{
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaInsertarSolicitudesAnulacionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			
			/**
			 * INSERT INTO solicitud_anul_fact (
			 * codigo_pk, 
			 * codigo_factura, 
			 * consecutivo_factura, 
			 * usuario_autoriza, 
			 * motivo_anulacion, 
			 * observaciones_sol, 
			 * estado, 
			 * centro_atencion_solicita, 
			 * usuario_solicita, 
			 * fecha_solicita, 
			 * hora_solicita) VALUES(?,?,?,?,?,?,?,?,?,?,?) 
			 */
			
			int codigoSolicitud=UtilidadBD.obtenerSiguienteValorSecuencia(con,"seq_solicitud_anul_fact");
			
			ps.setDouble(1, Utilidades.convertirADouble(codigoSolicitud+""));
			
			ps.setInt(2, Utilidades.convertirAEntero(vo.get("codigofactura")+""));
			
			ps.setInt(3, Utilidades.convertirAEntero(vo.get("consecutivofactura")+""));
			
			ps.setDouble(4, Utilidades.convertirADouble(vo.get("usuarioautoriza")+""));
			
			ps.setInt(5, Utilidades.convertirAEntero(vo.get("motivoanulacion")+""));
			
			ps.setString(6, vo.get("observaciones")+"");
			
			ps.setString(7, vo.get("estado")+"");
			
			ps.setInt(8, Utilidades.convertirAEntero(vo.get("centroatencion")+""));
			
			ps.setString(9, vo.get("usuariosolicita")+"");
			
			ps.setDate(10, Date.valueOf(vo.get("fechasolicita")+""));
			
			ps.setString(11, vo.get("horasolicita")+"");
			
			ps.executeUpdate();
			
			return codigoSolicitud;
			
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return ConstantesBD.codigoNuncaValido;
	}

	
	/**
	 * 
	 * @param con
	 * @param codigoSolicitud
	 * @return
	 */
	public static HashMap consultarResumenSolictud(Connection con, int codigoSolicitud) 
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros", "0");
		try
		{
			
				PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consultaSolicitudAnulacionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				
				logger.info("CADENA >>>>>>>>"+consultaSolicitudAnulacionStr);
				logger.info("codigoSolicitud >>>>>>>>"+codigoSolicitud);
				
				ps.setDouble(1, Utilidades.convertirADouble(codigoSolicitud+""));
				mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		}
		catch (SQLException e) 
		{
			logger.info("ERROR AL EJECUTAR LA CONSULTA DE SOLICTUD DE ANULACION "+e);
			e.printStackTrace();
		}
		return mapa;
	}

	/**
	 * 
	 * @param con
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param consecutivoFactura
	 * @param codigoSolicitud
	 * @param usuarioAutoriza
	 * @param motivoAnulacion
	 * @param centroAtencion
	 * @return
	 */
	public static HashMap consultarSolictudesAnular(Connection con, String fechaInicial, String fechaFinal, String consecutivoFactura, String codigoSolicitud, String usuarioAutoriza, String motivoAnulacion, String centroAtencion) 
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros", "0");
		
		String cadena=consultaAnularSolicitudesStr;
		if((!fechaInicial.equals("")) && (!fechaFinal.equals("")))
		{
			cadena+=" AND  s.fecha_solicita between '"+UtilidadFecha.conversionFormatoFechaABD(fechaInicial)+"' and '"+UtilidadFecha.conversionFormatoFechaABD(fechaFinal)+"'";
		}
		if(!consecutivoFactura.equals(""))
		{
			cadena+=" AND s.consecutivo_factura="+consecutivoFactura;
		}
		if(!codigoSolicitud.equals(""))
		{
			cadena+=" AND s.codigo_pk="+codigoSolicitud;
		}
		if(!usuarioAutoriza.equals(""))
		{
			cadena+=" AND s.usuario_autoriza="+usuarioAutoriza;
		}
		if(!motivoAnulacion.equals(""))
		{
			cadena+=" AND s.motivo_anulacion="+motivoAnulacion;
		}
		if(!centroAtencion.equals(""))
		{
			cadena+=" AND s.centro_atencion_solicita="+centroAtencion;
		}
		
		try
		{
			PreparedStatementDecorator busqueda= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			logger.info("cadena >>>>>>> "+cadena);
			
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
	 * @param numeroSolicitud
	 * @return
	 */
	public static HashMap consultaDetalleSolicitud(Connection con, String numeroSolicitud) 
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros", "0");
		try
		{
			
				PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consultaSolicitudAnulacionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				
				logger.info("CADENA >>>>>>>>"+consultaSolicitudAnulacionStr);
				logger.info("codigoSolicitud >>>>>>>>"+numeroSolicitud);
				
				ps.setDouble(1, Utilidades.convertirADouble(numeroSolicitud));
				mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		}
		catch (SQLException e) 
		{
			logger.info("ERROR AL EJECUTAR LA CONSULTA DE SOLICTUD DE ANULACION "+e);
			e.printStackTrace();
		}
		return mapa;
	}

	
	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public static boolean insertarAnulacionSolicitud(Connection con, HashMap vo) 
	{
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaModificacionAnularSolicitudesStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			/**
			 * UPDATE solicitud_anul_fact SET 
			 * observaciones_anul=? , 
			 * estado=?, 
			 * usuario_anula=?, 
			 * fecha_anula=?, 
			 * hora_anula=? 
			 * WHERE codigo_pk=? 
			 */
			
			ps.setString(1, vo.get("observaciones")+"");
			
			ps.setString(2, vo.get("estado")+"");
			
			ps.setString(3, vo.get("usuarioanula")+"");
			
			ps.setDate(4, Date.valueOf(vo.get("fechaanula")+""));
			
			ps.setString(5, vo.get("horaanula")+"");
			
			ps.setDouble(6, Utilidades.convertirADouble(vo.get("numerosolicitud")+""));
			
			return ps.executeUpdate()>0;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return false;
	}
	
	
}
