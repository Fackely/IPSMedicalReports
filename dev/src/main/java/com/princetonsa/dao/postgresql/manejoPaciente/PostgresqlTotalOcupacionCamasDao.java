package com.princetonsa.dao.postgresql.manejoPaciente;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.manejoPaciente.TotalOcupacionCamasDao;
import com.princetonsa.dao.sqlbase.manejoPaciente.SqlBaseTotalOcupacionCamasDao;

/**
 * Fecha: Febrero - 2008
 * @author Jhony Alexander Duque
 *
 */

public class PostgresqlTotalOcupacionCamasDao implements TotalOcupacionCamasDao 
{

	public HashMap consultarEstados(Connection con) 
	{
		return SqlBaseTotalOcupacionCamasDao.consultarEstados(con);
	}

	/**
	 * Metodo encargado de consultar las camas en los diferentes estados
	 * @author Jhony Alexander Duque
	 * @param criterios
	 * -------------------------------
	 * KEY'S DEL MAPA CRITERIOS
	 * -------------------------------
	 * -- centroAtencion --> Requerido
	 * -- institucion --> Requerido
	 * -- estadosCama --> Requerido
	 * -- incluirCamasUrg --> Requerido
	 * @param con
	 * @return mapa
	 * --------------------------------
	 * KEY'S DEL MAPA RESULTADO
	 * --------------------------------
	 * numeroCama0_,codigoCama1_,
	 * estadoCama2_,tipoHabitacion3_,
	 * nombrePiso4_
	 */
	public HashMap consultarTotalEstados(Connection connection,HashMap criterios)
	{
		return SqlBaseTotalOcupacionCamasDao.consultarTotalEstados(connection, criterios);
	}
	
}