/* Princeton S.A (Parquesoft-Manizales)
*  Andrés Mauricio Ruiz Vélez
*  Creado 14-nov-2006 11:21:33
*/


package com.princetonsa.dao.historiaClinica;

import java.sql.Connection;
import java.util.HashMap;

public interface CrecimientoDesarrolloDao {

	
	/**
	 * Metodo estandar para consultar la informacion del paciente.
	 * @param con
	 * @param parametros
	 * @return
	 */	
	HashMap consultarInformacion(Connection con, HashMap parametros);

}
