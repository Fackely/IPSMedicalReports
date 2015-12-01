package com.princetonsa.dao.oracle.glosas;

/**
 * @author Juan Alejandro Cardona
 * @date: Octubre de 2008
 */

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.glosas.ParametrosFirmasImpresionRespDao;
import com.princetonsa.dao.sqlbase.glosas.SqlBaseParametrosFirmasImpresionRespDao;

public class OracleParametrosFirmasImpresionRespDao implements ParametrosFirmasImpresionRespDao {
	
	// Enlace al metodo para realizar el listado de datos 
	public HashMap listadoFirmas(Connection con) {
        return SqlBaseParametrosFirmasImpresionRespDao.listadoFirmas(con, "SELECT " +
    		 	"frg.codigo AS codsecuencia, " +
    		 	"frg.usuario AS usuario, " +
    		 	"frg.cargo AS cargo " +
    		"FROM " +
    			"firmas_resp_glosas frg " +
    		"ORDER BY " +
    			"codsecuencia ");
    }


	// Enlace para Insertar una nueva firma en impresion respuesta de glosa
	public boolean insertarFirmaImpresion(Connection con, HashMap criterios) {
		return SqlBaseParametrosFirmasImpresionRespDao.insertarFirmaImpresion(con, criterios);
	}

	
	// Enlace para Modificar una firma en impresion respuesta de glosa
	public boolean modificarFirmaImpresion(Connection con, HashMap criterios) {
		return SqlBaseParametrosFirmasImpresionRespDao.modificarFirmaImpresion(con, criterios);
	}

	
	// Enlace para Eliminar una firma en impresion respuesta de glosa
	public boolean eliminarFirmaImpresion(Connection con, int codeSecuenFirma) {
		return SqlBaseParametrosFirmasImpresionRespDao.eliminarFirmaImpresion(con, codeSecuenFirma);
	}

	
	// Enlace para Eliminar TODAS las firmas en impresion respuesta de glosa
	public boolean eliminarTodaFirmaImpresion(Connection con) {
		return SqlBaseParametrosFirmasImpresionRespDao.eliminarTodaFirmaImpresion(con);
	}
	
	
	
}