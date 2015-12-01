package com.princetonsa.dao.historiaClinica;

import java.sql.Connection;
import java.util.HashMap;

public interface ParametrizacionComponentesDao 
{

	
	/**
	 * 
	 * @param con
	 * @param componente
	 * @return
	 */
	HashMap<String, Object> consultaSecciones(Connection con, String componente);

	/**
	 * 
	 * @param con
	 * @param componente
	 * @return
	 */
	HashMap<String, Object> consultaEscalas(Connection con, String componente);

	/**
	 * 
	 * @param con
	 * @param componente
	 * @return
	 */
	HashMap<String, Object> consultaCampos(Connection con, String componente);
	
	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	boolean insertarEscala(Connection con, HashMap vo);
	
	/**
	 * 
	 * @param con
	 * @param tipoComponente
	 * @param institucion
	 * @param loginUsuario
	 * @return
	 */
	int insertarComponente(Connection con, String tipoComponente, String institucion, String loginUsuario);

	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	int insertarSeccion(Connection con, HashMap vo);

	/**
	 * 
	 * @param con
	 * @param codigoComponente
	 * @param codigoSeccion
	 * @return
	 */
	int insertarCompSeccion(Connection con, int codigoComponente, int codigoSeccion);

	/**
	 * 
	 * @param con
	 * @param codigoInstitucion
	 * @param loginUsuario
	 * @param orden
	 * @return
	 */
	int insertarSeccionVacia(Connection con, String codigoInstitucion, String loginUsuario, String orden);

	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	int insertarCampo(Connection con, HashMap vo);

	/**
	 * 
	 * @param con
	 * @param componenteSeccion
	 * @param codigoCampo
	 * @return
	 */
	boolean insertarCompCamposSec(Connection con, int componenteSeccion, int codigoCampo);
	
	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	int insertarOpciones(Connection con, HashMap vo);
	
	/**
	 * 
	 * @param con
	 * @return
	 */
	HashMap<String, Object> consultarCodigosSeccion(Connection con);
	
	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	int insertarSubSeccion(Connection con, HashMap vo);
	
	/**
	 * 
	 * @param con
	 * @param seccion
	 * @return
	 */
	HashMap<String, Object> consultaDetalleSeccion(Connection con, String seccion);
	
	/**
	 * 
	 * @param con
	 * @param escala
	 * @return
	 */
	HashMap<String, Object> consultaDetalleEscala(Connection con, String escala);
	
	/**
	 * 
	 * @param con
	 * @param campo
	 * @return
	 */
	HashMap<String, Object> consultaDetalleCampo(Connection con, String campo);
	
	/**
	 * 
	 * @param con
	 * @param componente
	 * @param codigoEscala
	 * @param orden
	 * @param mostrar
	 * @param escalaModificada 
	 * @return
	 */
	boolean modificarEscala(Connection con, HashMap vo);
	
	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	boolean modificarSeccion(Connection con, HashMap vo);
	
	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	boolean modificarCampo(Connection con, HashMap vo);
	
	/**
	 * 
	 * @param con
	 * @param seccion
	 * @return
	 */
	HashMap<String, Object> consultaSubseccion(Connection con, String seccion);
	
	/**
	 * 
	 * @param con
	 * @param seccion
	 * @return
	 */
	HashMap<String, Object> consultaCamposSeccion(Connection con, String seccion);
	
	/**
	 * 
	 * @param con
	 * @param campo
	 * @return
	 */
	HashMap<String, Object> consultarOpciones(Connection con, String campo);
	
	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	boolean modificarOpciones(Connection con, HashMap vo);
	
	/**
	 * 
	 * @param con
	 * @return
	 */
	HashMap<String, Object> consultarSignosVitales(Connection con);

	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	boolean modificarSignos(Connection con, HashMap vo);
	
	/**
	 * 
	 * @param con
	 * @param tipoComponente
	 * @return
	 */
	HashMap<String, Object> consultarOrdenCampo(Connection con, String tipoComponente);
	
	/**
	 * 
	 * @param con
	 * @param tipoComponente
	 * @return
	 */
	HashMap<String, Object> consultarOrdenEscala(Connection con, String tipoComponente);
	
	/**
	 * 
	 * @param con
	 * @param tipoComponente
	 * @return
	 */
	HashMap<String, Object> consultarOrdenSeccion(Connection con, String tipoComponente);
	
	/**
	 * 
	 * @param con
	 * @param componente
	 * @return
	 */
	int consultarCodigoComponete(Connection con, String componente);

	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	boolean modificarMostrarSeccion(Connection con, HashMap vo);

	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	boolean modificarMostrarCampos(Connection con, HashMap vo);

	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	boolean modificarMostrarEscala(Connection con, HashMap vo);
	
	/**
	 * 
	 * @param con
	 * @param codigoComponente
	 * @param codigoSeccion
	 * @return
	 */
	int consultarCodigoCompSeccion(Connection con, int codigoComponente, int codigoSeccion);
	
	/**
	 * 
	 * @param con
	 * @param componente
	 * @param escala
	 * @return
	 */
	boolean consultaEscalasModificar(Connection con, int componente, String escala);
	
	/**
	 * 
	 * @param con
	 * @param componente
	 * @return
	 */
	HashMap<String, Object> consultarSeccionesExitentes(Connection con, String componente);
	
	/**
	 * 
	 * @param con
	 * @param componente
	 * @return
	 */
	HashMap<String, Object> consultarCamposExitentes(Connection con, String componente);
	
	/**
	 * 
	 * @param con
	 * @param codigoCampo
	 * @return
	 */
	String consultarFormula(Connection con, String codigoCampo);
	
	/**
	 * 
	 * @param con
	 * @param codigoSeccion
	 * @return
	 */
	HashMap consultarCamposFormula(Connection con, String codigoSeccion);
	
	/**
	 * 
	 * @param con
	 * @param componente
	 * @return
	 */
	HashMap consultarSeccionesAsociadas(Connection con, String componente);
	
	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	boolean insertarSeccionesAsocidas(Connection con, HashMap vo);
	
	/**
	 * 
	 * 
	 * 
	 * 
	 */
	boolean eliminarValoresAsociados(Connection con, double codigoPkOpcion);
	
	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	boolean insertarValoresAsociados(Connection con, HashMap vo);
	
	/**
	 * 
	 * @param con
	 * @param codigoOpcion
	 * @return
	 */
	HashMap consultarSeccionesGeneradas(Connection con, String codigoOpcion);
	
	/**
	 * 
	 * @param con
	 * @param codigoOpcion
	 * @return
	 */
	HashMap consultarValoresGeneradas(Connection con, String codigoOpcion);
	
	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	boolean modificarOrdenCampos(Connection con, HashMap vo);
	
	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	boolean modificarOrdenSecciones(Connection con, HashMap vo);

	
}
