package com.princetonsa.dao.manejoPaciente;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Mauricio Jaramillo
 * @author axioma
 * Fecha Mayo de 2008
 */

public interface CierreIngresoDao
{

	/**
	 * @param con
	 * @param vo
	 * @return
	 */
	public abstract boolean cerrarIngreso(Connection con, HashMap vo);
	
	/**
     * Método para obtener los ingresos cerrados por cierre manual de un paciente
     * pendientes por facturar
     * @param con
     * @param campos
     * @return
     */
    public ArrayList<HashMap<String,Object>> obtenerIngresosCerradosPendientesXPaciente(Connection con,HashMap campos);

}
