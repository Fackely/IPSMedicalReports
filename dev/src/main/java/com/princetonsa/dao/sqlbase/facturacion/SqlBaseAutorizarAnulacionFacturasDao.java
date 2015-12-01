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

public class SqlBaseAutorizarAnulacionFacturasDao 
{
	
	
	/**
	 * 
	 */
	public static Logger logger=Logger.getLogger(SqlBaseAutorizarAnulacionFacturasDao.class);
	
	
	/**
	 * 
	 */
	private static final String cadenaConsultaSolicitudesStr = "SELECT s.codigo_pk as numerosolicitud, " +
															   "s.fecha_solicita as fechasolicitud, " +
															   "s.hora_solicita as horasolicitud, " +
															   "s.consecutivo_factura as numerofactura, " +
															   "f.fecha as fechafactura, " +
															   "s.usuario_solicita as usuariosolicita  " +
															   "FROM solicitud_anul_fact s " +
															   "INNER JOIN usu_autorizan_anul_fac u ON(u.codigo=s.usuario_autoriza) " +
															   "INNER JOIN facturas f ON(f.codigo=s.codigo_factura) " +
															   "WHERE f.estado_facturacion<>"+ConstantesBD.codigoEstadoFacturacionAnulada+" AND s.centro_atencion_solicita=? AND u.usuario=? AND f.institucion=? AND s.estado='"+ConstantesIntegridadDominio.acronimoEstadoPendiente+"' AND s.anulacion_autorizada is null  order by fechasolicitud desc ";
	
	
	/**
	 * 
	 */
	private static final String cadenaConsultaDetalleSolicitudStr = "SELECT s.codigo_pk as numerosolicitud, " +
																	"s.fecha_solicita as fechasolicitud, " +
																	"s.hora_solicita as horasolicitud, " +
																	"s.consecutivo_factura as numerofactura, " +
																	"s.estado as estado, " +
																	"f.fecha as fechafactura, " +
																	"s.usuario_solicita as usuariosolicita, " +
																	"m.descripcion as motivoanulacion, " +
																	"s.observaciones_sol as observacionessol, " +
																	"getnombreconvenio(f.convenio) as nombreconvenio, " +
																	"getnombrepersona(f.cod_paciente) as nombrepersona, " +
																	"f.valor_total as valofactura, " +
																	"s.anulacion_autorizada as autorizada, " +
																	"s.observaciones_autoriza as observacionesautoriza, " +
																	"getnomcentroatencion(s.centro_atencion_solicita) as centroatencion, " +
																	"u.usuario as usuarioautoriza, " +
																	"s.fecha_autoriza as fechaautoriza, " +
																	"s.hora_autoriza as horaautoriza, " +
																	"fecha_anul_autoriza as fechaanulautoriza, " +
																	"hora_anul_autoriza as horaanulautoriza, " +
																	"s.observaciones_anul_autoriza as observanulautoriza " +
																	"FROM solicitud_anul_fact s " +
																	"INNER JOIN facturas f ON(f.codigo=s.codigo_factura) " +
																	"INNER JOIN motivos_anul_fact m ON(m.codigo=s.motivo_anulacion) " +
																	"INNER JOIN usu_autorizan_anul_fac u ON(u.codigo=s.usuario_autoriza) " +
																	"WHERE s.codigo_pk=? ";
	
	
	/**
	 * 
	 */
	private static final String cadenaConsultaAnularAutorizacionesStr = "SELECT s.codigo_pk as numeroautoriza, " +
																	    "getnomcentroatencion(s.centro_atencion_solicita) as centroatencion, " +
																	    "s.codigo_factura as codigofactura, " +
																	    "s.consecutivo_factura as consecutivofactura, " +
																	    "s.usuario_autoriza as usuarioautoriza, " +
																	    "s.fecha_autoriza as fechaautoriza, " +
																	    "s.hora_autoriza as horaautoriza, " +
																	    "f.fecha as fechafactura, " +
																	    "u.usuario as descusuarioautoriza  " +
																	    "FROM solicitud_anul_fact s " +
																	    "INNER JOIN facturas f ON(f.codigo=s.codigo_factura) " +
																	    "INNER JOIN usu_autorizan_anul_fac u ON(u.codigo=s.usuario_autoriza) " +
																	    "WHERE estado='"+ConstantesIntegridadDominio.acronimoEstadoAprobado+"' AND anulacion_autorizada='"+ConstantesBD.acronimoSi+"' AND f.estado_facturacion<>"+ConstantesBD.codigoEstadoFacturacionAnulada+" ";
	
	
	
	/**
	 * 
	 */
	private static final String cadenaConsultaAutorizacionesStr = "SELECT s.codigo_pk as numerosolicitud, " +
																  "getnomcentroatencion(s.centro_atencion_solicita) as centroatencion, " +
																  "s.estado as estadoautoriza, " +
																  "s.anulacion_autorizada as anulacionautorizada, " +
																  "case when s.estado='PEN' then s.fecha_solicita when s.estado='ANA' then s.fecha_anul_autoriza else s.fecha_autoriza end as fecha, " +
																  "case when s.estado='PEN' then s.hora_solicita when s.estado='ANA' then s.hora_anul_autoriza else s.hora_autoriza end as hora, " +
																  "s.consecutivo_factura as factura, " +
																  "f.fecha as fechafactura, " +
																  "case when s.estado='PEN' then s.usuario_solicita when estado='ANA' then s.usuario_anul_autoriza else uf.usuario end as usuario " +
																  "FROM solicitud_anul_fact s " +
																  "INNER JOIN facturas f on(f.codigo=s.codigo_factura) " +
																  "INNER JOIN usu_autorizan_anul_fac uf on(uf.codigo=s.usuario_autoriza) " +
																  "WHERE s.estado<>'"+ConstantesIntegridadDominio.acronimoEstadoAnulado+"' ";
	
	
	
	
	/**
	 * 
	 */
	private static final String cadenaModificacionAutorizarAnulacionStr = "UPDATE solicitud_anul_fact SET observaciones_autoriza=?, anulacion_autorizada=?, estado=?, fecha_autoriza=?, hora_autoriza=? WHERE codigo_pk=? ";
	
	
	/**
	 * 
	 */
	private static final String cadenaModificacionAnularAutorizacionStr = "UPDATE solicitud_anul_fact SET observaciones_anul_autoriza=?, estado=?, usuario_anul_autoriza=?, fecha_anul_autoriza=?, hora_anul_autoriza=? WHERE codigo_pk=? ";
	
	
	
	
	/**
	 * 
	 * @param con
	 * @param loginUsuario
	 * @param centroAtencion
	 * @return
	 */
	public static HashMap consultarListadoSolicitudes(Connection con, String loginUsuario, int centroAtencion, int codigoInstitucion) 
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros", "0");
		try
		{
			
				PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaConsultaSolicitudesStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				
				logger.info("CADENA >>>>>>>>"+cadenaConsultaSolicitudesStr);
				logger.info("loginUsuario >>>>>>>>"+loginUsuario);
				logger.info("centroAtencion >>>>>>>>"+centroAtencion);
				
				ps.setInt(1, centroAtencion);
				ps.setString(2, loginUsuario);
				ps.setInt(3, codigoInstitucion);
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
	 * @param codigoSolicitud
	 * @return
	 */
	public static HashMap consultarDetalleSolicitud(Connection con, String codigoSolicitud) 
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros", "0");
		try
		{
			
				PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaConsultaDetalleSolicitudStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				
				logger.info("CADENA >>>>>>>>"+cadenaConsultaDetalleSolicitudStr);
				logger.info("codigoSolicitud >>>>>>>>"+codigoSolicitud);
				
				ps.setDouble(1, Utilidades.convertirADouble(codigoSolicitud));
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
	 * @param codigoSolicitud
	 * @return
	 */
	public static HashMap consultarResumenAutorizar(Connection con, String codigoSolicitud) 
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros", "0");
		try
		{
			
				PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaConsultaDetalleSolicitudStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				
				logger.info("CADENA >>>>>>>>"+cadenaConsultaDetalleSolicitudStr);
				logger.info("codigoSolicitud >>>>>>>>"+codigoSolicitud);
				
				ps.setDouble(1, Utilidades.convertirADouble(codigoSolicitud));
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
	public static boolean insertarAutorizacion(Connection con, HashMap vo) 
	{
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaModificacionAutorizarAnulacionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			ps.setString(1, vo.get("observaciones").toString());
			
			ps.setString(2, vo.get("autorizada").toString());
			
			ps.setString(3, vo.get("estado").toString());
			
			ps.setDate(4, Date.valueOf(vo.get("fechaautoriza").toString()));
			
			ps.setString(5, vo.get("horaautoriza").toString());
			
			ps.setDouble(6, Utilidades.convertirADouble(vo.get("codigosolicitud").toString()));
			
			return ps.executeUpdate()>0;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return false;
	}

	
	/**
	 * 
	 * @param con
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param consecutivoFactura
	 * @param numeroAutorizacion
	 * @param usuarioAutoriza
	 * @return
	 */
	public static HashMap consultarListadoAprobadas(Connection con, String fechaInicial, String fechaFinal, String consecutivoFactura, String numeroAutorizacion, String usuarioAutoriza) 
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros", "0");
		
		String cadena=cadenaConsultaAnularAutorizacionesStr;
		if((!fechaInicial.equals("")) && (!fechaFinal.equals("")))
		{
			cadena+=" AND  s.fecha_autoriza between '"+UtilidadFecha.conversionFormatoFechaABD(fechaInicial)+"' and '"+UtilidadFecha.conversionFormatoFechaABD(fechaFinal)+"'";
		}
		if(!consecutivoFactura.equals(""))
		{
			cadena+=" AND s.consecutivo_factura="+consecutivoFactura;
		}
		if(!numeroAutorizacion.equals(""))
		{
			cadena+=" AND s.codigo_pk="+numeroAutorizacion;
		}
		if(!usuarioAutoriza.equals(""))
		{
			cadena+=" AND s.usuario_autoriza="+usuarioAutoriza;
		}
		cadena+=" order by fechaautoriza desc";
		
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
	 * @param vo
	 * @return
	 */
	public static boolean insertarAnulacionAutorizacion(Connection con, HashMap vo) 
	{
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaModificacionAnularAutorizacionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			ps.setString(1, vo.get("observaciones").toString());
			
			ps.setString(2, vo.get("estado").toString());
			
			ps.setString(3, vo.get("usuarioanula").toString());
			
			ps.setDate(4, Date.valueOf(vo.get("fechaanula").toString()));
			
			ps.setString(5, vo.get("horaanula").toString());
			
			ps.setDouble(6, Utilidades.convertirADouble(vo.get("numerosolicitud").toString()));
			
			return ps.executeUpdate()>0;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return false;
	}

	
	/**
	 * 
	 * @param con
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param consecutivoFactura
	 * @param estadoAutorizacion
	 * @param usuarioAutoriza
	 * @param motivoAnulacion
	 * @param centroAtencion
	 * @return
	 */
	public static HashMap consultaListadoAutorizaciones(Connection con, String fechaInicial, String fechaFinal, String consecutivoFactura, String estadoAutorizacion, String usuarioAutoriza, String motivoAnulacion, String centroAtencion) 
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros", "0");
		
		String cadena=cadenaConsultaAutorizacionesStr;
		if((!fechaInicial.equals("")) && (!fechaFinal.equals("")))
		{
			cadena+=" AND  s.fecha_solicita between '"+UtilidadFecha.conversionFormatoFechaABD(fechaInicial)+"' and '"+UtilidadFecha.conversionFormatoFechaABD(fechaFinal)+"'";
		}
		if(!consecutivoFactura.equals(""))
		{
			cadena+=" AND s.consecutivo_factura="+consecutivoFactura;
		}
		if(!estadoAutorizacion.equals(""))
		{
			if(estadoAutorizacion.equals(ConstantesIntegridadDominio.acronimoEstadoPendiente))
				cadena+=" AND s.estado='"+ConstantesIntegridadDominio.acronimoEstadoPendiente+"' AND s.anulacion_autorizada is null ";
			else if(estadoAutorizacion.equals(ConstantesIntegridadDominio.acronimoEstadoAutorizacionDenegada))
				cadena+=" AND s.estado='"+ConstantesIntegridadDominio.acronimoEstadoPendiente+"' AND s.anulacion_autorizada='"+ConstantesBD.acronimoNo+"' "; 
			else			 
				cadena+=" AND s.estado='"+estadoAutorizacion+"'";
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
		cadena+=" order by fecha desc";
		
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
	public static HashMap consultaDetalleAutorizacion(Connection con, String numeroSolicitud) 
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros", "0");
		try
		{
			
				PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaConsultaDetalleSolicitudStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				
				logger.info("CADENA >>>>>>>>"+cadenaConsultaDetalleSolicitudStr);
				logger.info("codigoSolicitud >>>>>>>>"+numeroSolicitud);
				
				ps.setDouble(1, Utilidades.convertirADouble(numeroSolicitud));
				mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		}
		catch (SQLException e) 
		{
			logger.info("ERROR AL EJECUTAR LA CONSULTA DE USUARIOS AUTORIZADOS PARA AUTORIZAR "+e);
			e.printStackTrace();
		}
		return mapa;
	}

	
	
}
