package com.princetonsa.dao.oracle.historiaClinica;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.historiaClinica.EscalasDao;
import com.princetonsa.dao.sqlbase.historiaClinica.SqlBaseEscalasDao;

public class OracleEscalasDao implements EscalasDao 
{
	
	/**
	 * 
	 */
	public HashMap<String, Object> consultaEscalas(Connection con) 
	{
		return SqlBaseEscalasDao.consultaEscalas(con);
	}

	/**
	 * 
	 */
	public boolean insertarEscala(Connection con, String nombreEscala, String requiereObservaciones, int codigoInstitucion, String loginUsuario) 
	{
		return SqlBaseEscalasDao.insertarEscala(con, nombreEscala, requiereObservaciones, codigoInstitucion, loginUsuario);
	}
	

	/**
	 * 
	 */
	public boolean insertarEscala(Connection con,  String codigo,  String nombreEscala, String requiereObservaciones, int codigoInstitucion, String loginUsuario) 
	{
		return SqlBaseEscalasDao.insertarEscala(con,codigo, nombreEscala, requiereObservaciones, codigoInstitucion, loginUsuario);
	}

	/**
	 * 
	 */
	public int insertarSecciones(Connection con, HashMap vo) 
	{
		return SqlBaseEscalasDao.insertarSecciones(con, vo);
	}

	/**
	 * 
	 */
	public boolean insertarFactores(Connection con, HashMap vo) 
	{
		return SqlBaseEscalasDao.insertarFactores(con, vo);
	}

	/**
	 * 
	 */
	public int insertarCampos(Connection con, HashMap vo) 
	{
		return SqlBaseEscalasDao.insertarCampos(con, vo);
	}

	/**
	 * 
	 */
	public boolean insertarCamposSeccion(Connection con, HashMap vo) 
	{
		return SqlBaseEscalasDao.insertarCamposSeccion(con, vo);
	}

	/**
	 * 
	 */
	public HashMap<String, Object> consultaDetalleEscala(Connection con, String escala, String institucion) 
	{
		return SqlBaseEscalasDao.consultaDetalleEscala(con, escala, institucion);
	}
	
	/**
	 * 
	 */
	public HashMap<String, Object> detalleCampos(Connection con, String escala) 
	{
		return SqlBaseEscalasDao.detalleCampos(con, escala);
	}
	
	/**
	 * 
	 */
	public HashMap<String, Object> detalleFactores(Connection con, String escala, String institucion) 
	{
		return SqlBaseEscalasDao.detalleFactores(con, escala, institucion);
	}
	
	/**
	 * 
	 */
	public HashMap<String, Object> detalleSeccion(Connection con, String escala) 
	{
		return SqlBaseEscalasDao.detalleSeccion(con, escala);
	}
	
	/**
	 * 
	 */
	public boolean modificarCampos(Connection con, HashMap vo) 
	{
		return SqlBaseEscalasDao.modificarCampos(con, vo);
	}

	/**
	 * 
	 */
	public boolean modificarCamposSeccion(Connection con, HashMap vo) 
	{
		return SqlBaseEscalasDao.modificarCamposSeccion(con, vo);
	}

	/**
	 * 
	 */
	public boolean modificarEscala(Connection con, HashMap vo) 
	{
		return SqlBaseEscalasDao.modificarEscala(con, vo);
	}

	/**
	 * 
	 */
	public boolean modificarFactores(Connection con, HashMap vo) 
	{
		return SqlBaseEscalasDao.modificarFactores(con, vo);
	}
	
	/**
	 * 
	 */
	public boolean modificarSeccion(Connection con, HashMap vo) 
	{
		return SqlBaseEscalasDao.modificarSeccion(con, vo);
	}
	
	/**
	 * 
	 */
	public boolean eliminarEscala(Connection con, String escala) 
	{
		return SqlBaseEscalasDao.eliminarEscala(con,escala);
	}

	/**
	 * 
	 */
	public boolean modificarMostrarCampos(Connection con, HashMap vo) 
	{
		return SqlBaseEscalasDao.modificarMostrarCampos(con, vo);
	}

	/**
	 * 
	 */
	public boolean modificarMostrarEscala(Connection con, HashMap vo) 
	{
		return SqlBaseEscalasDao.modificarMostrarEscala(con, vo);
	}

	/**
	 * 
	 */
	public boolean modificarMostrarFactor(Connection con, HashMap vo) 
	{
		return SqlBaseEscalasDao.modificarMostrarFactor(con, vo);
	}

	/**
	 * 
	 */
	public boolean modificarMostrarSeccion(Connection con, HashMap vo) 
	{
		return SqlBaseEscalasDao.modificarMostrarSeccion(con, vo);
	}
	
	/**
	 * 
	 */
	public boolean eliminarFactor(Connection con, String codigoFactor) 
	{
		return SqlBaseEscalasDao.eliminarFactor(con, codigoFactor);
	}
	
	/**
	 * 
	 */
	public boolean eliminarCampos(Connection con, String codigoCampo, String loginUsuario) 
	{
		return SqlBaseEscalasDao.eliminarCampos(con, codigoCampo, loginUsuario);
	}
	
	/**
	 * 
	 */
	public boolean eliminarSecciones(Connection con, String codigoSeccion) 
	{
		return SqlBaseEscalasDao.eliminarSecciones(con, codigoSeccion);
	}
	
	
}
