package com.princetonsa.dao.inventarios;

import java.sql.Connection;
import java.util.HashMap;
import com.princetonsa.mundo.inventarios.ConsultaInventarioFisicoArticulos;





public interface ConsultaInventarioFisicoArticulosDao {
	
	/**
	 * 
	 * @param con
	 * @param cuc
	 * @return
	 */
	public HashMap filtrarArticulos(Connection con, ConsultaInventarioFisicoArticulos cuc);
	
	/**
	 * 
	 * @param con
	 * @param cuc
	 * @return
	 */
	public HashMap consultarConteosArticulo(Connection con, ConsultaInventarioFisicoArticulos cuc);
	
	/**
	 * 
	 * @param con
	 * @param cuc
	 * @return
	 */
	public HashMap consultarPreparacionesArticulo(Connection con, ConsultaInventarioFisicoArticulos cuc);
		
	/**
	 * 
	 * @param con
	 * @param cuc
	 * @return
	 */
	public HashMap consultarUbicacionArticulo(Connection con, ConsultaInventarioFisicoArticulos cuc);
	
	
	/**
	 * 
	 * @param con
	 * @param cuc
	 * @return
	 */
	public HashMap consultarAjusteArticulo(Connection con, ConsultaInventarioFisicoArticulos cuc);

}