package com.princetonsa.dao.glosas;

/*
 * @author: Juan Alejandro Cardona
 * date: octubre 2008
 */


import java.sql.Connection;
import com.princetonsa.decorator.ResultSetDecorator;
import java.util.HashMap;


public interface ConceptosRespuestasDao {

	
	/**
	 * @param con, codigoInstitucionInt
	 * @return	 */
	public ResultSetDecorator consultaConceptosRespuestasGlosas(Connection con, int codigoInstitucionInt);



	/**
	 * @param con, codigoConcepto, codigoInstitucionInt, descripcion, criterios
	 * @return	 */
	public int insertarConceptoRespuestaGlosas(Connection con, String codigoConcepto, int codigoInstitucionInt, String descripcion, HashMap criterios);

	
	
	

	/**
	 * @param con, codigo, codigoInstitucionInt, descripcion
	 * @return	 */
	public ResultSetDecorator busquedaAvanzadaRespuestasGlosas(Connection con, String codigo, int codigoInstitucionInt, String descripcion);

	

	/**
	 * @param con, codigo, codigoInstitucionInt
	 * @return	 */
	public ResultSetDecorator consultaRelacionConceptosRespuestasGlosas(Connection con, String codigo, int codigoInstitucionInt);

	

	/**
	 * @param con, codigoConceptoAntiguo, codigoInstitucionInt
	 * @return	 */
	public int eliminarConceptoRespuestaGlosas(Connection con, String codigoConceptoAntiguo, int codigoInstitucionInt);

	

	/**
	 * @param con, codigoConceptoAntiguo, codigoInstitucionInt, codigoConcepto, descripcion
	 * @return	 */
	public int modificarConceptoRespuestaGlosas(Connection con, String codigoConceptoAntiguo, int codigoInstitucionInt, String codigoConcepto, String descripcion, HashMap criterios);

	/**
	 * Metodo que consulta si un concepto tipo glosa ha sido utilizado o no para validar si se puede o no eliminar
	 * @param connection
	 * @param criterios
	 * @return
	 */
	public boolean consultarConceptosRespuesta (Connection con, String codConcepto);	
	
	/**
	 * 
	 * @param codigo
	 * @param institucion
	 * @return
	 */
	public boolean puedoEliminar(String codigo, int institucion);
}