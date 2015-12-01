package com.princetonsa.dao.oracle.manejoPaciente;

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
public class OracleMotivosSatisfaccionInsatisfaccionDao implements MotivosSatisfaccionInsatisfaccionDao {

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
		return SqlBaseMotivosSatisfaccionInsatisfaccionDao.ingresar(con, mundo, DaoFactory.ORACLE);
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