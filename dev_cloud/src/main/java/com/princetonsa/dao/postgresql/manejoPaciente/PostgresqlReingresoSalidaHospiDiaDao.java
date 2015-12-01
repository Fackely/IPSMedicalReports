package com.princetonsa.dao.postgresql.manejoPaciente;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.manejoPaciente.ReingresoSalidaHospiDiaDao;
import com.princetonsa.dao.sqlbase.manejoPaciente.SqlBaseReingresoSalidaHospiDiaDao;
import com.princetonsa.dto.manejoPaciente.DtoReingresoSalidaHospiDia;

/**
 * 
 * @author wilson
 *
 */
public class PostgresqlReingresoSalidaHospiDiaDao implements ReingresoSalidaHospiDiaDao 
{
	/**
	 * 
	 * @param con
	 * @param codigo
	 */
	public boolean insertar(Connection con, DtoReingresoSalidaHospiDia dto)
	{
		return SqlBaseReingresoSalidaHospiDiaDao.insertar(con, dto);
	}
	
	/**
	 * 
	 * @param con
	 * @param dto
	 * @return
	 */
	public boolean updateSalida(Connection con, DtoReingresoSalidaHospiDia dto)
	{
		return SqlBaseReingresoSalidaHospiDiaDao.updateSalida(con, dto);
	}
	
	/**
	 * 
	 * @param con
	 * @param criteriosBusqueda key{codigoCentroAtencion, esSalida, tipoIdentificacion, numeroIdentificacion, primerNombre, primerApellido}
	 * @return
	 */
	public HashMap listadoPacientesReingresoOSalida(Connection con, HashMap criteriosBusqueda)
	{
		return SqlBaseReingresoSalidaHospiDiaDao.listadoPacientesReingresoOSalida(con, criteriosBusqueda);
	}
	
	/**
	 * M�todo que consulta el �ltimo codigo del reingreso salida hospital d�a por la cuenta
	 * @param con
	 * @param dto
	 * @return
	 */
	public String obtenerUltimoCodigoReingresoSalidaHospitalDia(Connection con,DtoReingresoSalidaHospiDia dto)
	{
		return SqlBaseReingresoSalidaHospiDiaDao.obtenerUltimoCodigoReingresoSalidaHospitalDia(con, dto);
	}
	
	/**
	 * M�todo que realiza la eliminaci�n de todos los registros de reingreso hospital d�a de una cuenta
	 * @param con
	 * @param dto
	 * @return
	 */
	public int eliminarReingresoSalidaHospitalDiaXCuenta(Connection con,DtoReingresoSalidaHospiDia dto)
	{
		return SqlBaseReingresoSalidaHospiDiaDao.eliminarReingresoSalidaHospitalDiaXCuenta(con, dto);
	}
	
	/**
	 * 
	 */
	public boolean existeSalidaXFecha(Connection con,int cuenta,String fecha)
	{
		return SqlBaseReingresoSalidaHospiDiaDao.existeSalidaXFecha(con, cuenta, fecha);
	}
	
	/**
	 * 
	 * @param con
	 * @param cuenta
	 * @param tipo
	 * @return
	 */
	public boolean existeReingresoOSalidaActivo(Connection con, int cuenta, String tipo,int codigo)
	{
		return SqlBaseReingresoSalidaHospiDiaDao.existeReingresoOSalidaActivo(con, cuenta, tipo,codigo);
	}
	
}
