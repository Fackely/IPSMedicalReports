package com.princetonsa.dao.oracle.manejoPaciente;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.manejoPaciente.AsignacionCamaCuidadoEspecialAPisoDao;
import com.princetonsa.dao.sqlbase.manejoPaciente.SqlBaseAsignacionCamaCuidadoEspecialAPisoDao;





/**
 * @author Jhony Alexander Duque A.
 * jduque@princetonsa.com
 */

public class OracleAsignacionCamaCuidadoEspecialAPisoDao implements AsignacionCamaCuidadoEspecialAPisoDao
{
	/**
	 * Metodo encargado de consultasr los pacientes de cuidados especiales
	 * @param connection
	 * @param criterios
	 * @return mapa
	 * -----------------------------------
	 * KEY'S DEL MAPA RESULTADO
	 * -----------------------------------
	 * -- ingreso0_
	 * -- paciente1_
	 * -- identPac2_
	 * -- fechaNacimiento3_
	 * -- sexoPac4_
	 * -- fechaHoraOrden5_
	 * -- profesional6_
	 * -- centroCosto7_
	 * -- tipoMonitoreo8_
	 * -- diagnosticoPpal9_
	 * -- convenio10_
	 * -- cama11_
	 */
	public HashMap consultaPacientes (Connection connection, HashMap criterios)
	{
		return SqlBaseAsignacionCamaCuidadoEspecialAPisoDao.consultaPacientes(connection, criterios);
	}
	
	/**
	 * Metodo encargado de consultar la informacion a mostrar en el detalle
	 * @param connection
	 * @param criterios
	 * ----------------------------
	 * KEY'S DEL MAPA CRITERIOS
	 * ----------------------------
	 * -- idCuenta12_
	 * @return mapa
	 * ---------------------------
	 * KEY'S DEL MAPA RESULTADO
	 * ---------------------------
	 * -- camaActual3_
	 * -- tipoHbitActual4_
	 * -- cCostoActual5_
	 * -- tipoMonActual6_
	 * -- codigoCamaActual16_
	 * -- codigoTipoMonitoreo17_
	 */
	public HashMap consultaDetalle (Connection connection, HashMap criterios)
	{
		return SqlBaseAsignacionCamaCuidadoEspecialAPisoDao.consultaDetalle(connection, criterios);
	}
	
	
	/**
	 * Metodo encargado de actualizar el estado de ingreso a cuidados especiales
	 * a FINALIZADA.
	 * @param connection
	 * @param criterios
	 * -----------------------------
	 * KEY'S DEL MAPA CRITERIOS
	 * ------------------------------
	 * -- fechaFinaliza0
	 * -- horaFinaliza1
	 * -- usuarioFinaliza2
	 * -- estado3
	 * -- ingreso4
	 * @return true/false
	 */
	public boolean actualizarEstadoIngresoCuiEsp (Connection connection,HashMap criterios)
	{
		return SqlBaseAsignacionCamaCuidadoEspecialAPisoDao.actualizarEstadoIngresoCuiEsp(connection, criterios);
	}
}
	