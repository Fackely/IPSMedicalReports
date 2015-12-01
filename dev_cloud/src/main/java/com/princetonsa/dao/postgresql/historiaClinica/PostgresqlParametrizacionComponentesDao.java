package com.princetonsa.dao.postgresql.historiaClinica;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.historiaClinica.ParametrizacionComponentesDao;
import com.princetonsa.dao.sqlbase.historiaClinica.SqlBaseParametrizacionComponentesDao;

public class PostgresqlParametrizacionComponentesDao implements
		ParametrizacionComponentesDao 
		
{
	
	/**
	 * 
	 */
	public HashMap<String, Object> consultaCampos(Connection con, String componente) 
	{
		return SqlBaseParametrizacionComponentesDao.consultaCampos(con, componente);
	}

	/**
	 * 
	 */
	public HashMap<String, Object> consultaEscalas(Connection con, String componente) 
	{
		return SqlBaseParametrizacionComponentesDao.consultaEscalas(con, componente);
	}

	/**
	 * 
	 */
	public HashMap<String, Object> consultaSecciones(Connection con, String componente) 
	{
		return SqlBaseParametrizacionComponentesDao.consultaSecciones(con, componente);
	}
	
	/**
	 * 
	 */
	public boolean insertarEscala(Connection con, HashMap vo) 
	{
		return SqlBaseParametrizacionComponentesDao.insertarEscala(con, vo);
	}
	
	/**
	 * 
	 */
	public int insertarComponente(Connection con, String tipoComponente, String institucion, String loginUsuario) 
	{
		return SqlBaseParametrizacionComponentesDao.insertarComponente(con, tipoComponente, institucion, loginUsuario);
	}
	
	/**
	 * 
	 */
	public int insertarSeccion(Connection con, HashMap vo) 
	{
		return SqlBaseParametrizacionComponentesDao.insertarSeccion(con, vo);
	}
	
	/**
	 * 
	 */
	public int insertarCompSeccion(Connection con, int codigoComponente, int codigoSeccion) 
	{
		return SqlBaseParametrizacionComponentesDao.insertarCompSeccion(con, codigoComponente, codigoSeccion);
	}
	
	/**
	 * 
	 */
	public int insertarSeccionVacia(Connection con, String codigoInstitucion, String loginUsuario, String orden) 
	{
		return SqlBaseParametrizacionComponentesDao.insertarSeccionVacia(con, codigoInstitucion, loginUsuario, orden);
	}

	/**
	 * 
	 */
	public int insertarCampo(Connection con, HashMap vo) 
	{
		return SqlBaseParametrizacionComponentesDao.insertarCampo(con, vo);
	}
	
	/**
	 * 
	 */
	public boolean insertarCompCamposSec(Connection con, int componenteSeccion, int codigoCampo) 
	{
		return SqlBaseParametrizacionComponentesDao.insertarCompCamposSec(con, componenteSeccion, codigoCampo);
	}
	
	/**
	 * 
	 */
	public int insertarOpciones(Connection con, HashMap vo) 
	{
		return SqlBaseParametrizacionComponentesDao.insertarOpciones(con, vo);
	}
	
	/**
	 * 
	 */
	public int insertarSubSeccion(Connection con, HashMap vo) 
	{
		return SqlBaseParametrizacionComponentesDao.insertarSubSeccion(con, vo);
	}
	
	/**
	 * 
	 */
	public HashMap<String, Object> consultaDetalleCampo(Connection con, String campo) 
	{
		return SqlBaseParametrizacionComponentesDao.consultaDetalleCampo(con, campo);
	}
	
	/**
	 * 
	 */
	public HashMap<String, Object> consultaDetalleEscala(Connection con, String escala) 
	{
		return SqlBaseParametrizacionComponentesDao.consultaDetalleEscala(con, escala);
	}
	
	/**
	 * 
	 */
	public HashMap<String, Object> consultaDetalleSeccion(Connection con, String seccion) 
	{
		return SqlBaseParametrizacionComponentesDao.consultaDetalleSeccion(con, seccion);
	}
	
	/**
	 * 
	 */
	public boolean modificarEscala(Connection con, HashMap vo) 
	{
		return SqlBaseParametrizacionComponentesDao.modificarEscala(con, vo);
	}

	/**
	 * 
	 */
	public boolean modificarSeccion(Connection con, HashMap vo) 
	{
		return SqlBaseParametrizacionComponentesDao.modificarSeccion(con, vo);
	}
	
	/**
	 * 
	 */
	public boolean modificarCampo(Connection con, HashMap vo) 
	{
		return SqlBaseParametrizacionComponentesDao.modificarCampo(con, vo);
	}
	
	/**
	 * 
	 */
	public HashMap<String, Object> consultaCamposSeccion(Connection con, String seccion) 
	{
		return SqlBaseParametrizacionComponentesDao.consultaCamposSeccion(con, seccion);
	}
	
	/**
	 * 
	 */
	public HashMap<String, Object> consultaSubseccion(Connection con, String seccion) 
	{
		return SqlBaseParametrizacionComponentesDao.consultaSubseccion(con, seccion);
	}
	
	/**
	 * 
	 */
	public HashMap<String, Object> consultarOpciones(Connection con, String campo) 
	{
		return SqlBaseParametrizacionComponentesDao.consultarOpciones(con, campo);
	}
	
	/**
	 * 
	 */
	public boolean modificarOpciones(Connection con, HashMap vo) 
	{
		return SqlBaseParametrizacionComponentesDao.modificarOpciones(con, vo);
	}
	
	/**
	 * 
	 */
	public HashMap<String, Object> consultarSignosVitales(Connection con) 
	{
		return SqlBaseParametrizacionComponentesDao.consultarSignosVitales(con);
	}

	/**
	 * 
	 */
	public boolean modificarSignos(Connection con, HashMap vo) 
	{
		return SqlBaseParametrizacionComponentesDao.modificarSignos(con, vo);
	}
	
	/**
	 * 
	 */
	public HashMap<String, Object> consultarCodigosSeccion(Connection con) 
	{
		return SqlBaseParametrizacionComponentesDao.consultarCodigosSeccion(con);
	}
	
	/**
	 * 
	 */
	public HashMap<String, Object> consultarOrdenCampo(Connection con, String tipoComponente) 
	{
		return SqlBaseParametrizacionComponentesDao.consultarOrdenCampo(con, tipoComponente);
	}

	/**
	 * 
	 */
	public HashMap<String, Object> consultarOrdenEscala(Connection con, String tipoComponente) 
	{
		return SqlBaseParametrizacionComponentesDao.consultarOrdenEscala(con, tipoComponente);
	}

	/**
	 * 
	 */
	public HashMap<String, Object> consultarOrdenSeccion(Connection con, String tipoComponente) 
	{
		return SqlBaseParametrizacionComponentesDao.consultarOrdenSeccion(con, tipoComponente);
	}
	
	/**
	 * 
	 */
	public int consultarCodigoComponete(Connection con, String componente) 
	{
		return SqlBaseParametrizacionComponentesDao.consultarCodigoComponete(con, componente);
	}

	/**
	 * 
	 */
	public boolean modificarMostrarSeccion(Connection con, HashMap vo) 
	{
		return SqlBaseParametrizacionComponentesDao.modificarMostrarSeccion(con, vo);
	}
	
	/**
	 * 
	 */
	public boolean modificarMostrarCampos(Connection con, HashMap vo) 
	{
		return SqlBaseParametrizacionComponentesDao.modificarMostrarCampos(con, vo);
	}
	
	/**
	 * 
	 */
	public boolean modificarMostrarEscala(Connection con, HashMap vo) 
	{
		return SqlBaseParametrizacionComponentesDao.modificarMostrarEscala(con, vo);
	}
	
	/**
	 * 
	 */
	public int consultarCodigoCompSeccion(Connection con, int codigoComponente, int codigoSeccion) 
	{
		return SqlBaseParametrizacionComponentesDao.consultarCodigoCompSeccion(con, codigoComponente, codigoSeccion);
	}
	
	/**
	 * 
	 */
	public boolean consultaEscalasModificar(Connection con, int componente, String escala) 
	{
		return SqlBaseParametrizacionComponentesDao.consultaEscalasModificar(con, componente, escala);
	}
	
	/**
	 * 
	 */
	public HashMap<String, Object> consultarSeccionesExitentes(Connection con, String componente) 
	{
		return SqlBaseParametrizacionComponentesDao.consultarSeccionesExitentes(con, componente);
	}
	
	/**
	 * 
	 */
	public HashMap<String, Object> consultarCamposExitentes(Connection con, String componente) 
	{
		return SqlBaseParametrizacionComponentesDao.consultarCamposExitentes(con, componente);
	}
	
	/**
	 * 
	 */
	public String consultarFormula(Connection con, String codigoCampo) 
	{
		return SqlBaseParametrizacionComponentesDao.consultarFormula(con, codigoCampo);
	}
	
	/**
	 * 
	 */
	public HashMap consultarCamposFormula(Connection con, String codigoSeccion) 
	{
		return SqlBaseParametrizacionComponentesDao.consultarCamposFormula(con, codigoSeccion);
	}

	/**
	 * 
	 */
	public HashMap consultarSeccionesAsociadas(Connection con, String componente) 
	{
		return SqlBaseParametrizacionComponentesDao.consultarSeccionesAsociadas(con, componente);
	}

	/**
	 * 
	 */
	public boolean insertarSeccionesAsocidas(Connection con, HashMap vo) 
	{
		return SqlBaseParametrizacionComponentesDao.insertarSeccionesAsocidas(con, vo);
	}

	/**
	 * 
	 */
	public boolean insertarValoresAsociados(Connection con, HashMap vo) 
	{
		return SqlBaseParametrizacionComponentesDao.insertarValoresAsociados(con, vo);
	}

	/**
	 * 
	 */
	public HashMap consultarSeccionesGeneradas(Connection con, String codigoOpcion) 
	{
		return SqlBaseParametrizacionComponentesDao.consultarSeccionesGeneradas(con, codigoOpcion);
	}

	/**
	 * 
	 */
	public HashMap consultarValoresGeneradas(Connection con, String codigoOpcion) 
	{
		return SqlBaseParametrizacionComponentesDao.consultarValoresGeneradas(con, codigoOpcion);
	}

	
	/**
	 * 
	 */
	public boolean modificarOrdenCampos(Connection con, HashMap vo) 
	{
		return SqlBaseParametrizacionComponentesDao.modificarOrdenCampos(con, vo);
	}

	/**
	 * 
	 */
	public boolean modificarOrdenSecciones(Connection con, HashMap vo) 
	{
		return SqlBaseParametrizacionComponentesDao.modificarOrdenSecciones(con, vo);
	}
	
	/**
	 * 
	 */
	public boolean eliminarValoresAsociados(Connection con, double codigoPkOpciones) 
	{
		return SqlBaseParametrizacionComponentesDao.eliminarValoresAsociados(con, codigoPkOpciones);
	}
	
	
}
