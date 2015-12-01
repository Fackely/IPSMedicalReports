package com.princetonsa.mundo.glosas;

import java.sql.Connection;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.princetonsa.actionform.glosas.ConceptosGeneralesForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.glosas.ConceptosGeneralesDao;

/**
 * @author Mauricio Jaramillo
 * Fecha: Septiembre de 2008
 */

public class ConceptosGenerales
{

	/*---------------------------------------------
	 * 				ATRIBUTOS LOGGER
	 ---------------------------------------------*/
	public static Logger logger = Logger.getLogger(ConceptosGenerales.class);
	/*---------------------------------------------
	 * 				FIN ATRIBUTOS LOGGER
	 ---------------------------------------------*/
	
	/**
	 * Se inicializa el Dao
	 */
	public static ConceptosGeneralesDao conceptosGeneralesDao()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConceptosGeneralesDao();
	}
	
	/**
	 * Método inicial de la funcionalidad, la cual se
	 * encarga de cargar en un mapa los resultados arrojados
	 * por la consulta de Conceptos Generales de Glosas
	 * @param con
	 * @param codigoInstitucionInt
	 * @return
	 */
	public HashMap consultarConceptosGenerales(Connection con, int codigoInstitucionInt)
	{
		return conceptosGeneralesDao().consultarConceptosGenerales(con, codigoInstitucionInt);
	}

	/**
	 * Método que genera la inserción del nuevo concepto general de glosas, 
	 * especificando el código, la descripción, el tipo y si esta activo o no
	 * @param con
	 * @param forma
	 * @return
	 */

	public boolean insertarConceptoGeneral(Connection con, ConceptosGeneralesForm forma, String usuario, int institucion)
	{
		HashMap<String, Object> criterios = new HashMap<String, Object>();
		criterios.put("codigoConcepto", forma.getCodigoConcepto());
		criterios.put("descripcionConcepto", forma.getDescripcionConcepto());
		criterios.put("tipoConcepto", forma.getTipoConcepto());
		criterios.put("activoConcepto", forma.getActivoConcepto());
		criterios.put("usuarioConcepto", usuario);
		criterios.put("institucionConcepto", institucion);
		
		return conceptosGeneralesDao().insertarConceptoGeneral(con, criterios);
	}

	/**
	 * Método implementado para eliminar un Concepto General de
	 * glosas de la base de datos
	 * @param con
	 * @param conceptosGenerales
	 * @return
	 */
	public boolean eliminarConceptoGeneral(Connection con, int conceptosGenerales)
	{
		return conceptosGeneralesDao().eliminarConceptoGeneral(con, conceptosGenerales);
	}

	/**
	 * Método implementado para modificar un Concepto General de Glosas
	 * según los parámetros de modificación establecidos por el usuario
	 * @param con
	 * @param forma
	 * @param string 
	 * @param i 
	 * @return
	 */
	public boolean modificarConceptoGeneral(Connection con, ConceptosGeneralesForm forma, int codigoInstitucion, String loginUsuario)
	{
		HashMap<String, Object> criterios = new HashMap<String, Object>();
		criterios.put("codigoConcepto", forma.getConceptosGenerales("codigo_"+forma.getPosicion()));
		criterios.put("consecutivoConcepto", forma.getConceptosGenerales("consecutivo_"+forma.getPosicion()));
		criterios.put("descripcionConcepto", forma.getConceptosGenerales("descripcion_"+forma.getPosicion()));
		criterios.put("tipoConcepto", forma.getConceptosGenerales("tipoglosa_"+forma.getPosicion()));
		criterios.put("activoConcepto", forma.getConceptosGenerales("activo_"+forma.getPosicion()));
		criterios.put("institucionConcepto", codigoInstitucion);
		criterios.put("usuarioConcepto", loginUsuario);
		
		return conceptosGeneralesDao().modificarConceptoGeneral(con, criterios);
	}

}