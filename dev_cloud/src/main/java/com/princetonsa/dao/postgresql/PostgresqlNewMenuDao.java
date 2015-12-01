package com.princetonsa.dao.postgresql;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import com.princetonsa.dao.NewMenuDao;
import com.princetonsa.dao.historiaClinica.EventosAdversosDao;
import com.princetonsa.dao.sqlbase.SqlBaseNewMenuDao;
import com.princetonsa.dao.sqlbase.historiaClinica.SqlBaseEventosAdversosDao;
import com.princetonsa.dto.administracion.DtoModulo;
import com.princetonsa.mundo.manejoPaciente.EventosAdversos;


/**
 * 
 * @author GIO
 *
 */
public class PostgresqlNewMenuDao implements NewMenuDao {

	/**
	 * 
	 */
	public ArrayList<DtoModulo> cargar(Connection con, String loginUsuario) {
		return SqlBaseNewMenuDao.cargar(con, loginUsuario);
	}
	
}