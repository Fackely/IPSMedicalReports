/*
 * Mayo 15, 2007
 */
package com.princetonsa.dao.oracle.historiaClinica;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.historiaClinica.ReferenciaDao;
import com.princetonsa.dao.sqlbase.historiaClinica.SqlBaseReferenciaDao;

/**
 * @author Sebastián Gómez
 *
 * Clase que maneja los métodos propìos de Oracle para el acceso a la fuente
 * de datos en la funcionalidad REFERENCIA
 */
public class OracleReferenciaDao implements ReferenciaDao 
{
	/**
	 * Método que consulta la referencia
	 * @param con
	 * @param campos
	 * @return
	 */
	public HashMap cargar(Connection con,HashMap campos)
	{
		return SqlBaseReferenciaDao.cargar(con, campos);
	}
	
	/**
	 * Método implementado para guardar información de referencia
	 * bien sea de inserción , eliminacion o modificacion
	 * @param con
	 * @param campos
	 * @return
	 */
	public String guardar(Connection con,HashMap campos)
	{
		campos.put("secuenciaReferencia","seq_referencia.nextval");
		campos.put("secuenciaResultados","seq_resexamdiagref.nextval");
		campos.put("secuenciaDiagnosticos", "seq_diag_referencia.nextval");
		return SqlBaseReferenciaDao.guardar(con, campos);
	}
	
	/**
	 * Método implementado para realizar la búsqueda de las instituciones SIRC
	 * @param con
	 * @param campos
	 * @return
	 */
	public HashMap busquedaInstitucionesSirc(Connection con,HashMap campos)
	{
		return SqlBaseReferenciaDao.busquedaInstitucionesSirc(con, campos);
	}
	
	/**
	 * Método que realiza la anulacion de referencias externas caducadas
	 * @param con
	 * @param campos
	 * @return
	 */
	public boolean anularReferenciasExternasCaducadas(Connection con,HashMap campos)
	{
		return SqlBaseReferenciaDao.anularReferenciasExternasCaducadas(con, campos);
	}
		
}
