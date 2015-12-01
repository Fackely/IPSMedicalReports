package com.princetonsa.dao.glosas;

import java.sql.Connection;
import java.util.HashMap;

/**
 * @author Mauricio Jaramillo
 * Fecha: Septiembre de 2008
 */

public interface ConceptosGeneralesDao
{

	/**
	 * @param con
	 * @param codigoInstitucionInt
	 * @return
	 */
	public HashMap consultarConceptosGenerales(Connection con, int codigoInstitucionInt);

	/**
	 * @param con
	 * @param criterios
	 * @return
	 */
	public boolean insertarConceptoGeneral(Connection con, HashMap criterios);

	/**
	 * @param con
	 * @param conceptosGenerales
	 * @return
	 */
	public boolean eliminarConceptoGeneral(Connection con, int conceptosGenerales);

	/**
	 * @param con
	 * @param criterios
	 * @return
	 */
	public boolean modificarConceptoGeneral(Connection con, HashMap criterios);
	
}