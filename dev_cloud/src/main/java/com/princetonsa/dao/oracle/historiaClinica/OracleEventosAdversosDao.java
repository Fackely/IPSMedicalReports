package com.princetonsa.dao.oracle.historiaClinica;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.historiaClinica.EventosAdversosDao;
import com.princetonsa.dao.sqlbase.historiaClinica.SqlBaseEventosAdversosDao;
import com.princetonsa.mundo.manejoPaciente.EventosAdversos;


/**
 * 
 * @author axioma
 *
 */
public class OracleEventosAdversosDao implements EventosAdversosDao {

	/**
	 * 
	 */
	public HashMap consultar(Connection con, EventosAdversos mundo) {
		return SqlBaseEventosAdversosDao.consultar(con, mundo);
	}
	
	/**
	 * 
	 */
	public boolean ingresar(Connection con, EventosAdversos mundo) {
		return SqlBaseEventosAdversosDao.ingresar(con, mundo);
	}
	
	/**
	 * 
	 */
	public boolean modificar(Connection con, EventosAdversos mundo) {
		return SqlBaseEventosAdversosDao.modificar(con, mundo);
	}
		
	/**
	 * 
	 */
	public boolean inactivar(Connection con, EventosAdversos mundo) {
		return SqlBaseEventosAdversosDao.inactivar(con, mundo);
	}
}