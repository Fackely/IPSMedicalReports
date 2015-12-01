package com.princetonsa.dao.facturacion;

import java.sql.Connection;
import java.util.HashMap;

public interface SolicitarAnulacionFacturasDao 
{
	
	
	
	/**
	 * 
	 * @param con
	 * @param consecutivoFactura
	 * @param codigoInstitucion
	 * @return
	 */
	HashMap consultarFactura(Connection con, String consecutivoFactura, int codigoInstitucion);
	
	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	int insertarSolictud(Connection con, HashMap vo);
	
	/**
	 * 
	 * @param con
	 * @param codigoSolicitud
	 * @return
	 */
	HashMap consultarResumenSolictud(Connection con, int codigoSolicitud);
	
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
	HashMap consultarSolictudesAnular(Connection con, String fechaInicial, String fechaFinal, String consecutivoFactura, String codigoSolicitud, String usuarioAutoriza, String motivoAnulacion, String centroAtencion);
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	HashMap consultaDetalleSolicitud(Connection con, String numeroSolicitud);
	
	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	boolean insertarAnulacionSolicitud(Connection con, HashMap vo);
	
	
}
