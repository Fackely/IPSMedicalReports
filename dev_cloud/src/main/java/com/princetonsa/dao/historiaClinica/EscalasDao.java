package com.princetonsa.dao.historiaClinica;

import java.sql.Connection;
import java.util.HashMap;

public interface EscalasDao
{

	/**
	 * 
	 * @param con
	 * @return
	 */
	HashMap<String, Object> consultaEscalas(Connection con);

	/**
	 * 
	 * @param con
	 * @param codigoEscala
	 * @param nombreEscala
	 * @param requiereObservaciones
	 * @param codigoInstitucion
	 * @param loginUsuario
	 * @return
	 */
	boolean insertarEscala(Connection con, String nombreEscala, String requiereObservaciones, int codigoInstitucion, String loginUsuario);

	/**
	 * 
	 * @param con
	 * @param codigoEscala
	 * @param nombreEscala
	 * @param requiereObservaciones
	 * @param codigoInstitucion
	 * @param loginUsuario
	 * @return
	 */
	boolean insertarEscala(Connection con, String codigo, String nombreEscala, String requiereObservaciones, int codigoInstitucion, String loginUsuario);

	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	int insertarSecciones(Connection con, HashMap vo);

	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	boolean insertarFactores(Connection con, HashMap vo);

	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	int insertarCampos(Connection con, HashMap vo);

	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	boolean insertarCamposSeccion(Connection con, HashMap vo);

	/**
	 * 
	 * @param con
	 * @param escala
	 * @param institucion
	 * @return
	 */
	HashMap<String, Object> consultaDetalleEscala(Connection con, String escala, String institucion);
	
	/**
	 * 
	 * @param con
	 * @param escala
	 * @return
	 */
	HashMap<String, Object> detalleSeccion(Connection con, String escala);
	
	/**
	 * 
	 * @param con
	 * @param escala
	 * @return
	 */
	HashMap<String, Object> detalleCampos(Connection con, String escala);
	
	/**
	 * 
	 * @param con
	 * @param escala
	 * @param institucion
	 * @return
	 */
	HashMap<String, Object> detalleFactores(Connection con, String escala, String institucion);
	
	/**
	 * 
	 * @param con
	 * @param vo
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
	boolean modificarCamposSeccion(Connection con, HashMap vo);
	
	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	boolean modificarCampos(Connection con, HashMap vo);
	
	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	boolean modificarFactores(Connection con, HashMap vo);
	
	/**
	 * 
	 * @param con
	 * @param escala
	 * @return
	 */
	boolean eliminarEscala(Connection con, String escala);

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
	boolean modificarMostrarSeccion(Connection con, HashMap vo);

	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	boolean modificarMostrarFactor(Connection con, HashMap vo);
	/**
	 * 
	 * @param con
	 * @param codigoFactor
	 * @return
	 */
	boolean eliminarFactor(Connection con, String codigoFactor);
	
	/**
	 * 
	 * @param con
	 * @param codigoCampo
	 * @return
	 */
	boolean eliminarCampos(Connection con, String codigoCampo, String loginUsuario);
	
	/**
	 * 
	 * @param con
	 * @param codigoSeccion
	 * @return
	 */
	boolean eliminarSecciones(Connection con, String codigoSeccion);

	
}
