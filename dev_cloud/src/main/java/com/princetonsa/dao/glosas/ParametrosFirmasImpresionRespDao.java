package com.princetonsa.dao.glosas;

/**
 * @author Juan Alejandro Cardona
 * Fecha: Octubre de 2008
 */

import java.sql.Connection;
import java.util.HashMap;

public interface ParametrosFirmasImpresionRespDao {

	
	/** Enlace para listar los datos almacenados de Firmas Impresion Respuesta Glosa
	 * @param con
	 * @return */
	public abstract HashMap listadoFirmas(Connection con);

	
	public boolean insertarFirmaImpresion(Connection con, HashMap criterios);

	public boolean modificarFirmaImpresion(Connection con, HashMap criterios);

	public boolean eliminarFirmaImpresion(Connection con, int codeSecuenFirma);
	public boolean eliminarTodaFirmaImpresion(Connection con);
	
}