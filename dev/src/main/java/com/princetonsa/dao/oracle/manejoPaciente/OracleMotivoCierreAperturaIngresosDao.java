package com.princetonsa.dao.oracle.manejoPaciente;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.manejoPaciente.MotivoCierreAperturaIngresosDao;
import com.princetonsa.dao.sqlbase.manejoPaciente.SqlBaseMotivoCierreAperturaIngresosDao;
import com.princetonsa.mundo.manejoPaciente.MotivoCierreAperturaIngresos;
/**
 * 
 * @author diego
 *
 */


public class OracleMotivoCierreAperturaIngresosDao implements MotivoCierreAperturaIngresosDao {

	public HashMap consultarMotivoCierreAperturaIngresos(Connection con, MotivoCierreAperturaIngresos motivoCierreAperturaIngresos) {
		
		return SqlBaseMotivoCierreAperturaIngresosDao.consultar(con, motivoCierreAperturaIngresos);
	}

	public boolean eliminarMotivoCierreAperturaIngresos(Connection con, String motivoCierreAperturaIngresos) {
		
		return SqlBaseMotivoCierreAperturaIngresosDao.eliminar(con, motivoCierreAperturaIngresos);
	}

	public boolean insertarMotivoCierreAperturaIngresos(Connection con, MotivoCierreAperturaIngresos motivoCierreAperturaIngresos) {
		
		return SqlBaseMotivoCierreAperturaIngresosDao.insertar(con, motivoCierreAperturaIngresos);
	}

	public boolean modificarMotivoCierreAperturaIngresos(Connection con, MotivoCierreAperturaIngresos motivoCierreAperturaIngresos) {
		return SqlBaseMotivoCierreAperturaIngresosDao.modificar(con, motivoCierreAperturaIngresos);
	}
		
}