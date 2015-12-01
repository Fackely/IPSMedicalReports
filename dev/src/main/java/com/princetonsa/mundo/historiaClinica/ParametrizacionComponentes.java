package com.princetonsa.mundo.historiaClinica;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.historiaClinica.ParametrizacionComponentesDao;

public class ParametrizacionComponentes 
{

	
	/**
	 * 
	 */
	private ParametrizacionComponentesDao objetoDao;
	
	/**
	 * 
	 *
	 */
	public ParametrizacionComponentes()
	{
		init(System.getProperty("TIPOBD"));
	}
	
	
	/**
	 * Inicializa el acceso a la base de datos de este objeto, obteniendo su respectivo DAO.
	 * param tipoBD el tipo de bases de datos que va a usar este objeto.
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores validos para tipoBD.
	 * son los nombres y constantes definidos en <code>DaoFactory</code>
	 * @return <b>true</b> si la inicializacion fue exitosa, <code>false</code> si no.
	 */
	private boolean init(String tipoBD) 
	{
		if(objetoDao==null)
		{
			DaoFactory myFactory=DaoFactory.getDaoFactory(tipoBD);
			objetoDao=myFactory.getParametrizacionComponentesDao();
			if(objetoDao!=null)
				return true;
		}
		return false;
			
	}

	
	/**
	 * 
	 * @param con
	 * @param componente
	 * @return
	 */
	public HashMap<String, Object> consultaEscalas(Connection con, String componente) 
	{
		return objetoDao.consultaEscalas(con, componente);
	}

	/**
	 * 
	 * @param con
	 * @param componente
	 * @return
	 */
	public HashMap<String, Object> consultaCampos(Connection con, String componente) 
	{
		return objetoDao.consultaCampos(con, componente);
	}

	/**
	 * 
	 * @param con
	 * @param componente
	 * @return
	 */
	public HashMap<String, Object> consultaSecciones(Connection con, String componente) 
	{
		return objetoDao.consultaSecciones(con, componente);
	}

	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public boolean insertarEscala(Connection con, HashMap vo) 
	{
		return objetoDao.insertarEscala(con, vo);
	}

	/**
	 * 
	 * @param con
	 * @param tipoComponente
	 * @param institucion
	 * @param loginUsuario
	 * @return
	 */
	public int insertarComponente(Connection con, String tipoComponente, String institucion, String loginUsuario) 
	{
		return objetoDao.insertarComponente(con, tipoComponente, institucion, loginUsuario);
	}

	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public int insertarSeccion(Connection con, HashMap vo) 
	{
		return objetoDao.insertarSeccion(con, vo);
	}

	/**
	 * 
	 * @param con
	 * @param codigoComponente
	 * @param codigoSeccion
	 * @return
	 */
	public int insertarCompSeccion(Connection con, int codigoComponente, int codigoSeccion) 
	{
		return objetoDao.insertarCompSeccion(con, codigoComponente, codigoSeccion);
	}

	/**
	 * 
	 * @param con
	 * @param codigoInstitucion
	 * @param loginUsuario
	 * @param orden 
	 * @param orden
	 * @return
	 */
	public int insertarSeccionVacia(Connection con, String codigoInstitucion, String loginUsuario, String orden) 
	{
		return objetoDao.insertarSeccionVacia(con, codigoInstitucion, loginUsuario, orden);
	}

	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public int insertarCampo(Connection con, HashMap vo) 
	{
		return objetoDao.insertarCampo(con, vo);
	}

	/**
	 * 
	 * @param con
	 * @param componenteSeccion
	 * @param codigoCampo
	 * @return
	 */
	public boolean insertarCompCamposSec(Connection con, int componenteSeccion, int codigoCampo) 
	{
		return objetoDao.insertarCompCamposSec(con, componenteSeccion, codigoCampo);
	}

	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public int insertarOpciones(Connection con, HashMap vo) 
	{
		return objetoDao.insertarOpciones(con, vo);
	}

	/**
	 * 
	 * @param con
	 * @param seccion
	 * @return
	 */
	public HashMap<String, Object> consultarCodigosSeccion(Connection con) 
	{
		return objetoDao.consultarCodigosSeccion(con);
	}

	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public int insertarSubSeccion(Connection con, HashMap vo) 
	{
		return objetoDao.insertarSubSeccion(con, vo);
	}

	/**
	 * 
	 * @param con
	 * @param seccion
	 * @return
	 */
	public HashMap<String, Object> consultaDetalleSeccion(Connection con, String seccion) 
	{
		return objetoDao.consultaDetalleSeccion(con, seccion);
	}

	/**
	 * 
	 * @param con
	 * @param escala
	 * @return
	 */
	public HashMap<String, Object> consultaDetalleEscala(Connection con, String escala) 
	{
		return objetoDao.consultaDetalleEscala(con, escala);
	}

	/**
	 * 
	 * @param con
	 * @param campo
	 * @return
	 */
	public HashMap<String, Object> consultaDetalleCampo(Connection con, String campo) 
	{
		return objetoDao.consultaDetalleCampo(con, campo);
	}

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
	public boolean modificarEscala(Connection con, HashMap vo) 
	{
		return objetoDao.modificarEscala(con, vo);
	}

	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public boolean modificarSeccion(Connection con, HashMap vo) 
	{
		return objetoDao.modificarSeccion(con, vo);
	}

	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public boolean modificarCampo(Connection con, HashMap vo) 
	{
		return objetoDao.modificarCampo(con, vo);
	}

	/**
	 * 
	 * @param con
	 * @param seccion
	 * @return
	 */
	public HashMap<String, Object> consultaSubseccion(Connection con, String seccion) 
	{
		return objetoDao.consultaSubseccion(con, seccion);
	}

	/**
	 * 
	 * @param con
	 * @param seccion
	 * @return
	 */
	public HashMap<String, Object> consultaCamposSeccion(Connection con, String seccion) 
	{
		return objetoDao.consultaCamposSeccion(con, seccion);
	}
	
	/**
	 * 
	 * @param con
	 * @param campo
	 * @return
	 */
	public HashMap<String, Object> consultarOpciones(Connection con, String campo) 
	{
		return objetoDao.consultarOpciones(con, campo);
	}

	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public boolean modificarOpciones(Connection con, HashMap vo) 
	{
		return objetoDao.modificarOpciones(con, vo);
	}

	/**
	 * 
	 * @param con
	 * @return
	 */
	public HashMap<String, Object> consultarSignosVitales(Connection con) 
	{
		return objetoDao.consultarSignosVitales(con);
	}

	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public boolean modificarSignos(Connection con, HashMap vo) 
	{
		return objetoDao.modificarSignos(con, vo);
	}

	/**
	 * 
	 * @param con
	 * @param tipoComponente
	 * @return
	 */
	public HashMap<String, Object> consultarOrdenCampo(Connection con, String tipoComponente) 
	{
		return objetoDao.consultarOrdenCampo(con, tipoComponente);
	}

	/**
	 * 
	 * @param con
	 * @param tipoComponente
	 * @return
	 */
	public HashMap<String, Object> consultarOrdenEscala(Connection con, String tipoComponente) 
	{
		return objetoDao.consultarOrdenEscala(con, tipoComponente);
	}

	/**
	 * 
	 * @param con
	 * @param tipoComponente
	 * @return
	 */
	public HashMap<String, Object> consultarOrdenSeccion(Connection con, String tipoComponente) 
	{
		return objetoDao.consultarOrdenSeccion(con, tipoComponente);
	}

	/**
	 * 
	 * @param con
	 * @param componente
	 * @return
	 */
	public int consultarCodigoComponete(Connection con, String componente) 
	{
		return objetoDao.consultarCodigoComponete(con, componente);
	}

	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public boolean modificarMostrarSeccion(Connection con, HashMap vo) 
	{
		return objetoDao.modificarMostrarSeccion(con, vo);
	}

	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public boolean modificarMostrarCampos(Connection con, HashMap vo) 
	{
		return objetoDao.modificarMostrarCampos(con, vo);
	}

	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public boolean modificarMostrarEscala(Connection con, HashMap vo) 
	{
		return objetoDao.modificarMostrarEscala(con, vo);
	}

	/**
	 * 
	 * @param con
	 * @param codigoComponente
	 * @param codigoSeccion
	 * @return
	 */
	public int consultarCodigoCompSeccion(Connection con, int codigoComponente, int codigoSeccion) 
	{
		return objetoDao.consultarCodigoCompSeccion(con, codigoComponente, codigoSeccion);
	}

	/**
	 * 
	 * @param con
	 * @param componente
	 * @param escala
	 * @return
	 */
	public boolean consultaEscalasModificar(Connection con, int componente, String escala) 
	{
		return objetoDao.consultaEscalasModificar(con, componente, escala);
	}

	/**
	 * 
	 * @param con
	 * @param componente
	 * @return
	 */
	public HashMap<String, Object> consultarSeccionesExitentes(Connection con, String componente) 
	{
		return objetoDao.consultarSeccionesExitentes(con, componente);
	}

	/**
	 * 
	 * @param con
	 * @param componente
	 * @return
	 */
	public HashMap<String, Object> consultarCamposExitentes(Connection con, String componente) 
	{
		return objetoDao.consultarCamposExitentes(con, componente);
	}

	/**
	 * 
	 * @param con
	 * @param codigoCampo
	 * @return
	 */
	public String consultarFormula(Connection con, String codigoCampo) 
	{
		return objetoDao.consultarFormula(con, codigoCampo);
	}

	/**
	 * 
	 * @param con
	 * @param codigoSeccion
	 * @return
	 */
	public HashMap consultarCamposFormula(Connection con, String codigoSeccion) 
	{
		return objetoDao.consultarCamposFormula(con, codigoSeccion);
	}

	/**
	 * 
	 * @param con
	 * @param componente
	 * @return
	 */
	public HashMap consultarSeccionesAsociadas(Connection con, String componente) 
	{
		return objetoDao.consultarSeccionesAsociadas(con, componente);
	}

	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public boolean insertarSeccionesAsocidas(Connection con, HashMap vo) 
	{
		return objetoDao.insertarSeccionesAsocidas(con, vo);
	}
	
	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public boolean eliminarValoresAsociados(Connection con, double codigoPkOpcion) 
	{
		return objetoDao.eliminarValoresAsociados(con, codigoPkOpcion);
	}

	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public boolean insertarValoresAsociados(Connection con, HashMap vo) 
	{
		return objetoDao.insertarValoresAsociados(con, vo);
	}

	/**
	 * 
	 * @param con
	 * @param codigoOpcion
	 * @return
	 */
	public HashMap consultarSeccionesGeneradas(Connection con, String codigoOpcion) 
	{
		return objetoDao.consultarSeccionesGeneradas(con, codigoOpcion);
	}

	/**
	 * 
	 * @param con
	 * @param codigoOpcion
	 * @return
	 */
	public HashMap consultarValoresGeneradas(Connection con, String codigoOpcion) 
	{
		return objetoDao.consultarValoresGeneradas(con, codigoOpcion);
	}

	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public boolean modificarOrdenCampos(Connection con, HashMap vo) 
	{
		return objetoDao.modificarOrdenCampos(con, vo);
	}

	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public boolean modificarOrdenSecciones(Connection con, HashMap vo) 
	{
		return objetoDao.modificarOrdenSecciones(con, vo);
	}
	
	
}
