package com.princetonsa.dao.facturacion;

import java.sql.Connection;
import java.util.HashMap;

/**
 * 
 * @author wilson
 *
 */
public interface ReliquidacionTarifasDao 
{
	/**
	 * 
	 * @param con
	 * @param codigoCentroAtencion
	 * @param codigoPaciente
	 * @return
	 */
	public  HashMap obtenerCuentasReliquidar (Connection con, int codigoCentroAtencion, String codigoPaciente);
	
	/**
	 * Carga el listado de solicitudes pendientes x cuentas y responsable 
	 * @param con
	 * @param codigosCuenta
	 * @return 
	 */
	public HashMap obtenerSolicitudesReliquidar(Connection con, String subCuenta, String esServicios);
	
	/**
	 * 
	 * @param con
	 * @param subCuenta
	 * @param codigoDetalleCargoPadre
	 * @param esServicios
	 * @return
	 */
	public HashMap obtenerComponentesPaqueteReliquidar(Connection con, String subCuenta, double codigoDetalleCargoPadre, String esServicios);
	
	/**
	 * 
	 * @param con
	 * @param loginUsuario
	 * @param resultadoExitoso
	 * @param esqTarServOriginal
	 * @param esqTarServNuevo
	 * @param esqTarArtOriginal
	 * @param esqTarArtNuevo
	 * @return
	 */
	public boolean insertarReliquidacion(	Connection con, 
											String loginUsuario, 
											String resultadoExitoso, 
											int esqTarServOriginal, 
											int esqTarServNuevo,	
											int	esqTarArtOriginal,	
											int esqTarArtNuevo);
}
