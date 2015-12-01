package com.princetonsa.dao.oracle.facturacion;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.facturacion.ReliquidacionTarifasDao;
import com.princetonsa.dao.sqlbase.facturacion.SqlBaseReliquidacionTarifasDao;

/**
 * 
 * @author wilson
 *
 */
public class OracleReliquidacionTarifasDao implements ReliquidacionTarifasDao 
{
	/** 
	 * @param con
	 * @param codigoCentroAtencion
	 * @param codigoPaciente
	 * @return
	 */
	public  HashMap obtenerCuentasReliquidar (Connection con, int codigoCentroAtencion, String codigoPaciente)
	{
		return SqlBaseReliquidacionTarifasDao.obtenerCuentasReliquidar(con, codigoCentroAtencion, codigoPaciente);
	}

	/**
	 * 
	 * @param con
	 * @param subCuenta
	 * @return
	 */
	public HashMap obtenerSolicitudesReliquidar(Connection con, String subCuenta, String esServicios)
	{
		return SqlBaseReliquidacionTarifasDao.obtenerSolicitudesReliquidar(con, subCuenta, esServicios);
	}
	
	/**
	 * 
	 * @param con
	 * @param subCuenta
	 * @param codigoDetalleCargoPadre
	 * @param esServicios
	 * @return
	 */
	public HashMap obtenerComponentesPaqueteReliquidar(Connection con, String subCuenta, double codigoDetalleCargoPadre, String esServicios)
	{
		return SqlBaseReliquidacionTarifasDao.obtenerComponentesPaqueteReliquidar(con, subCuenta, codigoDetalleCargoPadre, esServicios);
	}
	
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
											int esqTarArtNuevo)
	{
		return SqlBaseReliquidacionTarifasDao.insertarReliquidacion(con, loginUsuario, resultadoExitoso, esqTarServOriginal, esqTarServNuevo, esqTarArtOriginal, esqTarArtNuevo);
	}
	
}
