package com.princetonsa.dao.oracle.facturacion;

import java.sql.Connection;
import java.util.HashMap;
import com.princetonsa.dao.facturacion.ImpresionSoportesFacturasDao;
import com.princetonsa.dao.sqlbase.facturacion.SqlBaseImpresionSoportesFacturasDao;

/**
 * 
 * @author axioma
 *
 */
public class OracleImpresionSoportesFacturasDao implements ImpresionSoportesFacturasDao {
	
	/**
	 * Metodo para consultar los tipos de soporte parametrizados
	 * @param con
	 * @param mundo
	 * @return
	 */
	public HashMap listarImprimir(Connection con,HashMap<String, Object> listado, int codigoInstitucionInt, String loginUsuario) {
		return SqlBaseImpresionSoportesFacturasDao.listarImprimir(con,listado, codigoInstitucionInt, loginUsuario);
	}
	
}