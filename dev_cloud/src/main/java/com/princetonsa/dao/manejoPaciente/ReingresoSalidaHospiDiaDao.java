package com.princetonsa.dao.manejoPaciente;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dto.manejoPaciente.DtoReingresoSalidaHospiDia;

/**
 * 
 * @author wilson
 *
 */
public interface ReingresoSalidaHospiDiaDao 
{
	/**
	 * 
	 * @param con
	 * @param codigo
	 */
	public boolean insertar(Connection con, DtoReingresoSalidaHospiDia dto);
	
	/**
	 * 
	 * @param con
	 * @param dto
	 * @return
	 */
	public boolean updateSalida(Connection con, DtoReingresoSalidaHospiDia dto);
	
	/**
	 * 
	 * @param con
	 * @param criteriosBusqueda key{codigoCentroAtencion, esSalida, tipoIdentificacion, numeroIdentificacion, primerNombre, primerApellido}
	 * @return
	 */
	public HashMap listadoPacientesReingresoOSalida(Connection con, HashMap criteriosBusqueda);
	
	/**
	 * Método que consulta el último codigo del reingreso salida hospital día por la cuenta
	 * @param con
	 * @param dto
	 * @return
	 */
	public String obtenerUltimoCodigoReingresoSalidaHospitalDia(Connection con,DtoReingresoSalidaHospiDia dto);
	
	/**
	 * Método que realiza la eliminación de todos los registros de reingreso hospital día de una cuenta
	 * @param con
	 * @param dto
	 * @return
	 */
	public int eliminarReingresoSalidaHospitalDiaXCuenta(Connection con,DtoReingresoSalidaHospiDia dto);
	
	/**
	 * Metodo que valida si a la fecha el paciente ya tiene alguna salida
	 * @param con
	 * @param cuenta
	 * @param fecha
	 * @return
	 */
	public boolean existeSalidaXFecha(Connection con,int cuenta,String fecha);
	
	/**
	 * 
	 * @param con
	 * @param cuenta
	 * @param tipo
	 * @return
	 */
	public boolean existeReingresoOSalidaActivo(Connection con, int cuenta, String tipo,int codigo);
}
