/*
 * Mayo 15, 2007
 */
package com.princetonsa.dao.postgresql.historiaClinica;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.historiaClinica.ReferenciaDao;
import com.princetonsa.dao.sqlbase.historiaClinica.SqlBaseReferenciaDao;

/**
 * @author Sebasti�n G�mez 
 *
 * Clase que maneja los m�todos prop�os de Postgres para el acceso a la fuente
 * de datos en la funcionalidad REFERENCIA
 */
public class PostgresqlReferenciaDao implements ReferenciaDao 
{
	/**
	 * M�todo que consulta la referencia
	 * @param con
	 * @param campos
	 * @return
	 */
	public HashMap cargar(Connection con,HashMap campos)
	{
		return SqlBaseReferenciaDao.cargar(con, campos);
	}
	
	/**
	 * M�todo implementado para guardar informaci�n de referencia
	 * bien sea de inserci�n , eliminacion o modificacion
	 * @param con
	 * @param campos
	 * @return
	 */
	public String guardar(Connection con,HashMap campos)
	{
		campos.put("secuenciaReferencia","nextval('seq_referencia')");
		campos.put("secuenciaResultados","nextval('seq_resexamdiagref')");
		campos.put("secuenciaDiagnosticos", "nextval('seq_diag_referencia')");
		return SqlBaseReferenciaDao.guardar(con, campos);
	}
	
	/**
	 * M�todo implementado para realizar la b�squeda de las instituciones SIRC
	 * @param con
	 * @param campos
	 * @return
	 */
	public HashMap busquedaInstitucionesSirc(Connection con,HashMap campos)
	{
		return SqlBaseReferenciaDao.busquedaInstitucionesSirc(con, campos);
	}
	
	/**
	 * M�todo que realiza la anulacion de referencias externas caducadas
	 * @param con
	 * @param campos
	 * @return
	 */
	public boolean anularReferenciasExternasCaducadas(Connection con,HashMap campos)
	{
		return SqlBaseReferenciaDao.anularReferenciasExternasCaducadas(con, campos);
	}
}
