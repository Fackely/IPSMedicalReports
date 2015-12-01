package com.princetonsa.dao.postgresql.manejoPaciente;


import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.manejoPaciente.OcupacionDiariaCamasDao;
import com.princetonsa.dao.sqlbase.manejoPaciente.SqlBaseOcupacionDiariaCamasDao;

/**
 * Fecha: Febrero - 2008
 * @author Jhony Alexander Duque
 *
 */

public class PostgresqlOcupacionDiariaCamasDao implements OcupacionDiariaCamasDao 
{
	/**
	 * Metodo encargado de generar las clausulas 
	 * where para la consulta en el birt.
	 * @param criterios
	 * --------------------------------
	 * KEY'S DEL MAPA CRITERIOS
	 * --------------------------------
	 * -- institucion6 --> Requerido
	 * -- centroAtencion0 --> Requerido
	 * -- fechaInicial1 --> Requerido
	 * -- fechaFinal2 --> Requerido
	 * -- incluirCamasUrgencias4 --> Opcional
	 * -- rompimientoPorPiso3 --> Opcional
	 * @param estadosCama debe ir ejm. 1,2,3
	 * @return cadena Where
	 */
	public String obtenerWhere (HashMap criterios, String estadosCama)
	{
		return SqlBaseOcupacionDiariaCamasDao.obtenerWhere(criterios, estadosCama);
	}

	/**
	 * Metodo encargado de consultar
	 * la informacion para el archivo plano
	 * @param criterios
	 * --------------------------------
	 * KEY'S DEL MAPA CRITERIOS
	 * --------------------------------
	 * -- institucion6 --> Requerido
	 * -- centroAtencion0 --> Requerido
	 * -- fechaInicial1 --> Requerido
	 * -- fechaFinal2 --> Requerido
	 * -- incluirCamasUrgencias4 --> Opcional
	 * -- rompimientoPorPiso3 --> Opcional
	 * @param estadosCama debe ir ejm. 1,2,3
	 * @return mapa 
	 * -----------------------------
	 * KEY'S DEL MAPA RESULTADO
	 * -----------------------------
	 * cantidad0, codigoEstado1,
	 * nomEstadoCama2,piso3,
	 * fecha4
	 */
	public HashMap consulta (Connection connection, HashMap criterios,String estadosCama)
	{
		return SqlBaseOcupacionDiariaCamasDao.consulta(connection, criterios, estadosCama);
	}
	
}
