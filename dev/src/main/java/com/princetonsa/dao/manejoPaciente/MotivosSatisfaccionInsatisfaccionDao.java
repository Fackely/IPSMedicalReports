package com.princetonsa.dao.manejoPaciente;

import com.princetonsa.mundo.manejoPaciente.MotivosSatisfaccionInsatisfaccion;
import java.sql.Connection;
import java.util.HashMap;

public interface MotivosSatisfaccionInsatisfaccionDao{
	
	/**
	 * 
	 * @param con
	 * @param mundo
	 * @return
	 */
	public HashMap consultar(Connection con, MotivosSatisfaccionInsatisfaccion mundo);
	
	/**
	 * 
	 * @param con
	 * @param mundo
	 * @return
	 */
	public boolean ingresar(Connection con, MotivosSatisfaccionInsatisfaccion mundo);
	
	/**
	 * 
	 * @param con
	 * @param mundo
	 * @return
	 */
	public boolean modificar(Connection con, MotivosSatisfaccionInsatisfaccion mundo);
	
	/**
	 * 
	 * @param con
	 * @param mundo
	 * @return
	 */
	public boolean eliminar(Connection con, MotivosSatisfaccionInsatisfaccion mundo);
}