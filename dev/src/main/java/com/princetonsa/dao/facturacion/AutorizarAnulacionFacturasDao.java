package com.princetonsa.dao.facturacion;

import java.sql.Connection;
import java.util.HashMap;

public interface AutorizarAnulacionFacturasDao 
{
	
	
	/**
	 * 
	 * @param con
	 * @param loginUsuario
	 * @param centroAtencion
	 * @return
	 */
	HashMap consultarListadoSolicitudes(Connection con, String loginUsuario, int centroAtencion, int codigoInstitucion);
	
	/**
	 * 
	 * @param con
	 * @param codigoSolicitud
	 * @return
	 */
	HashMap consultarDetalleSolicitud(Connection con, String codigoSolicitud);
	
	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	boolean insertarAutorizacion(Connection con, HashMap vo);
	
	/**
	 * 
	 * @param con
	 * @param codigoSolicitud
	 * @return
	 */
	HashMap consultarResumenAutorizar(Connection con, String codigoSolicitud);

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
	HashMap consultarListadoAprobadas(Connection con, String fechaInicial, String fechaFinal, String consecutivoFactura, String numeroAutorizacion, String usuarioAutoriza);
	
	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	boolean insertarAnulacionAutorizacion(Connection con, HashMap vo);
	
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
	HashMap consultaListadoAutorizaciones(Connection con, String fechaInicial, String fechaFinal, String consecutivoFactura, String estadoAutorizacion, String usuarioAutoriza, String motivoAnulacion, String centroAtencion);
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	HashMap consultaDetalleAutorizacion(Connection con, String numeroSolicitud);

	
	
}
