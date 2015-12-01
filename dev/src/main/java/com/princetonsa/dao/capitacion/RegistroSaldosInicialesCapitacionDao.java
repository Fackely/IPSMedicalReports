/*
 * Creado   08/08/2006
 *
 *Copyright Princeton S.A. &copy;&reg; 2004. Todos los Derechos Reservados.
 *
 * Lenguaje		:Java
 * Compilador	:J2SDK 1.4.2_04
 */
package com.princetonsa.dao.capitacion;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

/**
 * Interfaz Dao de las funciones de acceso a la fuente de datos
 *
 * @version 1.0, 08/08/2005
 * @author <a href="mailto:wilson@PrincetonSA.com">Wilson Rios</a>
 */
public interface RegistroSaldosInicialesCapitacionDao 
{
	/**
	 * busqueda de las cuentas de cobro 
	 * @param con
	 * @param criteriosBusquedaMap
	 * @return
	 * @throws SQLException
	 */	
	public HashMap busquedaCuentasCobro(	Connection con,
												HashMap criteriosBusquedaMap
											  )throws SQLException;
	
	/**
	 * metodo que carga en el mapa los cargues que tienen saldo pendiente = 0 ó ajustes sin aprobar
	 * @param con
	 * @param criteriosBusquedaMap
	 * @return
	 */
	public HashMap existenCarguesSaldoPendienteCeroOAjustesSinAprobar(Connection con, HashMap criteriosBusquedaMap);
}
