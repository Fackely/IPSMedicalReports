package com.princetonsa.dao.oracle.historiaClinica;


import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.historiaClinica.ServiciosXTipoTratamientoOdontologicoDao;
import com.princetonsa.dao.sqlbase.historiaClinica.SqlBaseServiciosXTipoTratamientoOdontologicoDao;





/**
 * @author Jhony Alexander Duque A.
 * jduque@princetonsa.com
 */

public class OracleServiciosXTipoTratamientoOdontologicoDao implements ServiciosXTipoTratamientoOdontologicoDao
{
	
	/**
	 * Metodo encargado de consultar los datos de 
	 * servicios por tipo tratamiento odontologico 
	 * en la tabla serv_x_tipo_trat_odont
	 * @author Jhony Alexander Duque A.
	 * @param connection
	 * @param criterios
	 * --------------------------
	 * KEY'S DEL MAPA CRITERIOS
	 * ----------------------------
	 * -- institucion --> Requerido
	 * -- tipoTratamientoOdontologico --> Requerido
	 * @return Mapa
	 * -------------------------------
	 * KEY'S DEL MAPA RESULTADO
	 * -------------------------------
	 * consecutivo0_,codigoTipoTratamiento1_,
	 * nombreTipoTratamiento2_,codigoCupsServicio3_,
	 * nombreServicio4_,codigoAxiomaServicio5_,
	 * uet6_,estaBd7_
	 */
	public HashMap consultarServiciosXTratamientoOdontologico (Connection connection,HashMap criterios)
	{
		return SqlBaseServiciosXTipoTratamientoOdontologicoDao.consultarServiciosXTratamientoOdontologico(connection, criterios);
	}
	
	/**
	 * Metodo encargado de ingresar los datos de servicios
	 * por tipo de tratamiento odontologico en la 
	 * tabla serv_x_tipo_trat_odont
	 * @author Jhony Alexander Duque A.
	 * @param connection
	 * @param datos
	 * ---------------------------
	 * KEY'S DEL MAPA DATOS
	 * ---------------------------
	 * codigoTipoTratamiento1_,codigoAxiomaServicio5_,
	 * uet6_,institucion8_,usuarioModifica9_
	 * @return false/true
	 */
	public boolean insertarServicosXTipoTratamiento (Connection connection, HashMap datos)
	{
		return SqlBaseServiciosXTipoTratamientoOdontologicoDao.insertarServicosXTipoTratamiento(connection, datos);
	}
	
	/**
	 *Metodo encargado de eliminar un registro de 
	 *servicios por tipo tratamiento odontologico en 
	 *la tabla  serv_x_tipo_trat_odont
	 *@author Jhony Alexander Duque A.
	 *@param connection
	 *@param consecutivo
	 *@return false/true 
	 */
	public boolean eliminarServicosXTipoTratamiento (Connection connection, String consecutivo)
	{
		return SqlBaseServiciosXTipoTratamientoOdontologicoDao.eliminarServicosXTipoTratamiento(connection, consecutivo);
	}
	
	
	/**
	 * Metodo encargado de actualizar los datos de servicio por tipo de tratamiento
	 * odontologico en la tabla serv_x_tipo_trat_odont
	 * @author Jhony Alexander Duque.
	 * @param connection
	 * @param datos
	 * ---------------------
	 * KEY'S DEL MAPA DATOS
	 * ---------------------
	 * -- codigoAxiomaServicio5_ --> Requerido
	 * -- uet6_ --> Opional
	 * -- usuarioModifica9_ --> Requerido
	 * -- consecutivo0_ --> Requerido
	 * @return false/true
	 */
	public  boolean actualizarServicioXTipoTratamientoOdontologico (Connection connection, HashMap datos)
	{
		return SqlBaseServiciosXTipoTratamientoOdontologicoDao.actualizarServicioXTipoTratamientoOdontologico(connection, datos);
	}
	
}
