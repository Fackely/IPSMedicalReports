/*
 * @(#)PostgresqlArqueosDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */
package com.princetonsa.dao.postgresql.tesoreria;

import java.sql.Connection;
import com.princetonsa.decorator.ResultSetDecorator;

import java.util.ArrayList;
import java.util.Collection;

import com.princetonsa.dao.sqlbase.tesoreria.SqlBaseArqueosDao;
import com.princetonsa.dao.tesoreria.ArqueosDao;

/**
 * Implementación Postgres de las funciones de acceso a la fuente de datos
 * para un arqueo
 *
 * @version 1.0, Abril 25 / 2006
 * @author <a href="mailto:wilson@PrincetonSA.com">Wilson Ríos</a>
 */
public class PostgresqlArqueosDao implements ArqueosDao 
{
	 /**
     * Insercion del arqueo definitivo
     * @param con
     * @param consecutivoArqueoDefinitivo
     * @param codigoInstitucion
     * @param loginUsuarioGenera
     * @param fechaArqueoDDMMYYYY
     * @param horaArqueo
     * @param consecutivoCaja
     * @param loginUsuarioCajero
     * @param fechaArqueadaDDMMYYYY
     * @return
     */
    public boolean  insertarArqueoDefinitivo(  Connection con,
                                               String consecutivoArqueoDefinitivo,
                                               int codigoInstitucion,
                                               String loginUsuarioGenera,
                                               String fechaArqueoDDMMYYYY,
                                               String horaArqueo,
                                               String consecutivoCaja,
                                               String loginUsuarioCajero,
                                               String fechaArqueadaDDMMYYYY
                                            )
    {
    	return SqlBaseArqueosDao.insertarArqueoDefinitivo(con, consecutivoArqueoDefinitivo, codigoInstitucion, loginUsuarioGenera, fechaArqueoDDMMYYYY, horaArqueo, consecutivoCaja, loginUsuarioCajero, fechaArqueadaDDMMYYYY);
    }
    
    /**
     * actualiza el campo arqueo definitivo de la tabla recibos caja
     * @param con
     * @param consecutivoArqueoDefinitivo
     * @param numeroReciboCaja
     * @param codigoInstitucion
     * @return
     */
    public boolean  actualizarRecibosCajaCampoArqueoDefinitivo(  	Connection con,
		                                               				String consecutivoArqueoDefinitivo,
		                                               				String numeroReciboCaja,
		                                               				int codigoInstitucion
		                                                   		)
    {
    	return SqlBaseArqueosDao.actualizarRecibosCajaCampoArqueoDefinitivo(con, consecutivoArqueoDefinitivo, numeroReciboCaja, codigoInstitucion);
    }
    
    /**
     * actualiza el campo cierre caja de la tabla recibos caja
     * @param con
     * @param consecutivoCierreCaja
     * @param numeroReciboCaja
     * @param codigoInstitucion
     * @return
     */
    public boolean  actualizarRecibosCajaCampoCierreCaja(  	Connection con,
                                               				String consecutivoCierreCaja,
                                               				String numeroReciboCaja,
                                               				int codigoInstitucion
                                               			)
    {
    	return SqlBaseArqueosDao.actualizarRecibosCajaCampoCierreCaja(con, consecutivoCierreCaja, numeroReciboCaja, codigoInstitucion);
    }
    
    /**
     * Insercion del cierre caja
     * @param con
     * @param consecutivoCierreCaja
     * @param codigoInstitucion
     * @param loginUsuarioGenera
     * @param fechaCierreDDMMYYYY
     * @param horaCierre
     * @param consecutivoCaja
     * @param loginUsuarioCajero
     * @param fechaCerradaDDMMYYYY
     * @return
     */
    public boolean  insertarCierreCaja( Connection con,
    									String consecutivoCierreCaja,
                                        int codigoInstitucion,
                                        String loginUsuarioGenera,
                                        String fechaCierreDDMMYYYY,
                                        String horaCierre,
                                        String consecutivoCaja,
                                        String loginUsuarioCajero,
                                        String fechaCerradaDDMMYYYY,
                                        String consecutivoCajaPpal
                                       )
    {
    	return SqlBaseArqueosDao.insertarCierreCaja(con, consecutivoCierreCaja, codigoInstitucion, loginUsuarioGenera, fechaCierreDDMMYYYY, horaCierre, consecutivoCaja, loginUsuarioCajero, fechaCerradaDDMMYYYY, consecutivoCajaPpal);
    }
    
    /**
	 * medodo para consultar los recibos caja que no cumplen con el arqueo definitivo para un
     * cajero - caja y fecha dada, retorna String con los numero Recibos caja separados por comas, 
	 * o "" si todos ya tienen el arqueo definitivo o "ERROR" en caso de exception
	 * @param con
	 * @param codigoInstitucion
	 * @param fechaArqueoDDMMYYYY
	 * @param loginUsuarioCajero
	 * @param consecutivoCaja
	 * @return String con los numero Recibos caja separados por comas, 
	 * 			o "" si todos ya tienen el arqueo definitivo
	 * 			o "ERROR" en caso de exception
	 */
	public String estanRecibosCajaConArqueoDefinitivoDadoCajeroCajaFecha(	Connection con,
																			int codigoInstitucion,
																			String fechaArqueoDDMMYYYY,
																			String loginUsuarioCajero,
																			String consecutivoCaja
																		)
	{
		return SqlBaseArqueosDao.estanRecibosCajaConArqueoDefinitivoDadoCajeroCajaFecha(con, codigoInstitucion, fechaArqueoDDMMYYYY, loginUsuarioCajero, consecutivoCaja);
	}
	
	/**
	 * metodo que evalua la existencia del cierre caja 
     * para un cajero - caja - fecha determinados
	 * @param con
	 * @param codigoInstitucion
	 * @param fechaArqueoDDMMYYYY
	 * @param loginUsuarioCajero
	 * @param consecutivoCaja
	 * @return
	 */
	public boolean existeCierreCaja(	Connection con,
										int codigoInstitucion,
										String fechaArqueoDDMMYYYY,
										String loginUsuarioCajero,
										String consecutivoCaja
									)
	{
		return SqlBaseArqueosDao.existeCierreCaja(con, codigoInstitucion, fechaArqueoDDMMYYYY, loginUsuarioCajero, consecutivoCaja);
	}
	
	/**
	 * metodo que realiza la busqueda de los arqueos y de los cierres caja
	 * @param con
	 * @param fechaInicialConsultaArqueosCierres
	 * @param fechaFinalConsultaArqueosCierres
	 * @param loginUsuarioCajero
	 * @param consecutivoCaja
	 * @param codigoTipoArqueoStr
	 * @param codigoInstitucion
	 * @return
	 */
	public Collection busquedaArqueosCierres(	Connection con,
												String fechaInicialConsultaArqueosCierres,
												String fechaFinalConsultaArqueosCierres,
												String loginUsuarioCajero,
												String consecutivoCaja,
												String codigoTipoArqueoStr,
												int codigoInstitucion,
												int codigoCentroAtencion,
												String consecutivoCajaPpal)
	{
		return SqlBaseArqueosDao.busquedaArqueosCierres(con, fechaInicialConsultaArqueosCierres, fechaFinalConsultaArqueosCierres, loginUsuarioCajero, consecutivoCaja, codigoTipoArqueoStr, codigoInstitucion, codigoCentroAtencion, consecutivoCajaPpal);
	}
	
	/**
	 * Metodo que consulta el resumen de un cierre de caja
	 * @param con
	 * @param codigoInstitucion
	 * @param consecutivo
	 * @return
	 */
	public ResultSetDecorator resumenCierreCaja(Connection con, int codigoInstitucion, String consecutivo)
	{
		return SqlBaseArqueosDao.resumenCierreCaja(con, codigoInstitucion, consecutivo);
	}
	
	/**
	 * Metodo que consulta el resumen de un arqueo definitivo
	 * @param con
	 * @param codigoInstitucion
	 * @param consecutivo
	 * @return
	 */
	public ResultSetDecorator resumenArqueoDefinitivo(Connection con, int codigoInstitucion, String consecutivo)
	{
		return SqlBaseArqueosDao.resumenArqueoDefinitivo(con, codigoInstitucion, consecutivo);
	}

	@Override
	public ArrayList<Integer> estanDevolucionesRCConArqueoDefinitivoDadoCajeroCajaFecha(
			Connection con, int codigoInstitucion, String fechaArqueoDDMMYYYY,
			String loginUsuarioCajero, String consecutivoCaja)
	{
		return SqlBaseArqueosDao.estanDevolucionesRCConArqueoDefinitivoDadoCajeroCajaFecha(con, codigoInstitucion, fechaArqueoDDMMYYYY, loginUsuarioCajero, consecutivoCaja);
	}
	
}