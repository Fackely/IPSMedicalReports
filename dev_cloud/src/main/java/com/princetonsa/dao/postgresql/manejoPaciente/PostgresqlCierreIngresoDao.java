package com.princetonsa.dao.postgresql.manejoPaciente;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import com.princetonsa.dao.manejoPaciente.CierreIngresoDao;
import com.princetonsa.dao.sqlbase.manejoPaciente.SqlBaseCierreIngresoDao;

/**
 * Mauricio Jaramillo
 * @author axioma
 * Fecha Mayo de 2008
 */

public class PostgresqlCierreIngresoDao implements CierreIngresoDao
{

	/**
	 * @param con
	 * @param vo
	 * @return
	 */
	public boolean cerrarIngreso(Connection con, HashMap vo)
    {
        return SqlBaseCierreIngresoDao.cerrarIngreso(con, vo);
    }
	
	/**
     * Método para obtener los ingresos cerrados por cierre manual de un paciente
     * pendientes por facturar
     * @param con
     * @param campos
     * @return
     */
    public ArrayList<HashMap<String,Object>> obtenerIngresosCerradosPendientesXPaciente(Connection con,HashMap campos)
    {
    	return SqlBaseCierreIngresoDao.obtenerIngresosCerradosPendientesXPaciente(con, campos);
    }
	
}
