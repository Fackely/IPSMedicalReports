package com.princetonsa.dao.manejoPaciente;

import java.sql.Connection;
import java.util.HashMap;

/**
 * Fecha: Febrero - 2008
 * @author Mauricio Jaramillo
 */
public interface TotalOcupacionCamasDao 
{

	/**
	 * 
	 * @param con
	 * @return
	 */
	public HashMap consultarEstados(Connection con);
	
		
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
	public HashMap consultarTotalEstados(Connection connection,HashMap criterios);
	
	
}
