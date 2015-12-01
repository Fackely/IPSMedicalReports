package com.princetonsa.dao.postgresql.manejoPaciente;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.manejoPaciente.MotivosSatisfaccionInsatisfaccionDao;
import com.princetonsa.dao.sqlbase.manejoPaciente.SqlBaseMotivosSatisfaccionInsatisfaccionDao;
import com.princetonsa.mundo.manejoPaciente.MotivosSatisfaccionInsatisfaccion;

/**
 * 
 * @author axioma
 *
 */
public class PostgresqlMotivosSatisfaccionInsatisfaccionDao implements MotivosSatisfaccionInsatisfaccionDao {

	/**
	 * 
	 */
	public HashMap consultar(Connection con, MotivosSatisfaccionInsatisfaccion mundo) {
		return SqlBaseMotivosSatisfaccionInsatisfaccionDao.consultar(con, mundo);
	}
	
	/**
	 * 
	 */
	public boolean ingresar(Connection con, MotivosSatisfaccionInsatisfaccion mundo) {
		return SqlBaseMotivosSatisfaccionInsatisfaccionDao.ingresar(con, mundo, DaoFactory.POSTGRESQL);
	}
	
	/**
	 * 
	 */
	public boolean modificar(Connection con, MotivosSatisfaccionInsatisfaccion mundo) {
		return SqlBaseMotivosSatisfaccionInsatisfaccionDao.modificar(con, mundo);
	}
		
	/**
	 * 
	 */
	public boolean eliminar(Connection con, MotivosSatisfaccionInsatisfaccion mundo) {
		return SqlBaseMotivosSatisfaccionInsatisfaccionDao.eliminar(con, mundo);
	}
}