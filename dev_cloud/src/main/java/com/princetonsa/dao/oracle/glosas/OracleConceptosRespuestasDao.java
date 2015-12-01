package com.princetonsa.dao.oracle.glosas;

/*
 * @author: Juan Alejandro Cardona
 * date: octubre 2008
 */

import java.sql.Connection;
import com.princetonsa.decorator.ResultSetDecorator;
import java.util.HashMap;
import com.princetonsa.dao.glosas.ConceptosRespuestasDao;
import com.princetonsa.dao.sqlbase.glosas.SqlBaseConceptosRespuestasDao;

public class OracleConceptosRespuestasDao implements ConceptosRespuestasDao {

	
	public ResultSetDecorator consultaConceptosRespuestasGlosas(Connection con, int codigoInstitucionInt) {
		return SqlBaseConceptosRespuestasDao.consultaConceptosRespuestasGlosas(con,codigoInstitucionInt);
	}

	
	public ResultSetDecorator busquedaAvanzadaRespuestasGlosas(Connection con, String codigo, int codigoInstitucionInt, String descripcion) {
		return SqlBaseConceptosRespuestasDao.busquedaAvanzadaRespuestasGlosas(con,codigo,codigoInstitucionInt,descripcion);
	}

	
	public ResultSetDecorator consultaRelacionConceptosRespuestasGlosas(Connection con, String codigo, int codigoInstitucionInt) {
		return SqlBaseConceptosRespuestasDao.consultaRelacionConceptosRespuestasGlosas(con,codigo,codigoInstitucionInt);
	}

	
	public int eliminarConceptoRespuestaGlosas(Connection con, String codigoConceptoAntiguo, int codigoInstitucionInt) {
		return SqlBaseConceptosRespuestasDao.eliminarConceptoRespuestaGlosas(con,codigoConceptoAntiguo,codigoInstitucionInt);
	}

	
	


	/**
	 * 
	 */
	public int insertarConceptoRespuestaGlosas(Connection con, String codigoConcepto, int codigoInstitucionInt, String descripcion, HashMap criterios)
	{
		return SqlBaseConceptosRespuestasDao.insertarConceptoRespuestaGlosas(con, codigoConcepto, codigoInstitucionInt, descripcion, criterios);
	}

	public int modificarConceptoRespuestaGlosas(Connection con, String codigoConceptoAntiguo, int codigoInstitucionInt, String codigoConcepto, String descripcion, HashMap criterios) {
		return SqlBaseConceptosRespuestasDao.modificarConceptoRespuestaGlosas(con,codigoConceptoAntiguo,codigoInstitucionInt,codigoConcepto,descripcion, criterios);
	}
	
	/**
	 * Metodo que consulta si un concepto tipo glosa ha sido utilizado o no para validar si se puede o no eliminar
	 * @param connection
	 * @param criterios
	 * @return
	 */
	public boolean consultarConceptosRespuesta (Connection con, String codConcepto)
	{
		return SqlBaseConceptosRespuestasDao.consultarConceptosRespuesta(con, codConcepto);
	}
	
	/**
	 * 
	 * @param codigo
	 * @param institucion
	 * @return
	 */
	public boolean puedoEliminar(String codigo, int institucion)
	{
		return SqlBaseConceptosRespuestasDao.puedoEliminar(codigo, institucion);
	}
}