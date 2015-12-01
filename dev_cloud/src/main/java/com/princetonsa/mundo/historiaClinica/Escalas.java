package com.princetonsa.mundo.historiaClinica;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.historiaClinica.EscalasDao;

public class Escalas 
{

	
	/**
	 * 
	 */
	private EscalasDao objetoDao;
	
	
	/**
	 * 
	 *
	 */
	public Escalas()
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
			objetoDao=myFactory.getEscalasDao();
			if(objetoDao!=null)
				return true;
		}
		return false;
			
	}


	/**
	 * 
	 * @param con
	 * @return
	 */
	public HashMap<String, Object> consultaEscalas(Connection con) 
	{
		return objetoDao.consultaEscalas(con);
	}

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
	public boolean insertarEscala(Connection con, String nombreEscala, String requiereObservaciones, int codigoInstitucion, String loginUsuario) 
	{
		return objetoDao.insertarEscala(con,  nombreEscala, requiereObservaciones, codigoInstitucion, loginUsuario);
	}
	
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
	public boolean insertarEscala(Connection con, String codigoEscala, String nombreEscala, String requiereObservaciones, int codigoInstitucion, String loginUsuario) 
	{
		return objetoDao.insertarEscala(con,  codigoEscala,nombreEscala, requiereObservaciones, codigoInstitucion, loginUsuario);
	}
	
	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public int insertarSecciones(Connection con, HashMap vo) 
	{
		return objetoDao.insertarSecciones(con, vo);
	}

	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public boolean insertarFactores(Connection con, HashMap vo) 
	{
		return objetoDao.insertarFactores(con, vo);
	}

	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public int insertarCampos(Connection con, HashMap vo) 
	{
		return objetoDao.insertarCampos(con, vo);
	}

	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public boolean insertarCamposSeccion(Connection con, HashMap vo) 
	{
		return objetoDao.insertarCamposSeccion(con, vo);
	}

	/**
	 * 
	 * @param con
	 * @param escala
	 * @param institucion
	 * @return
	 */
	public HashMap<String, Object> consultaDetalleEscala(Connection con, String escala, String institucion) 
	{
		return objetoDao.consultaDetalleEscala(con, escala, institucion);
	}

	/**
	 * 
	 * @param con
	 * @param escala
	 * @return
	 */
	public HashMap<String, Object> detalleSeccion(Connection con, String escala) 
	{
		return objetoDao.detalleSeccion(con, escala);
	}

	/**
	 * 
	 * @param con
	 * @param escala
	 * @return
	 */
	public HashMap<String, Object> detalleCampos(Connection con, String escala) 
	{
		return objetoDao.detalleCampos(con, escala);
	}

	/**
	 * 
	 * @param con
	 * @param escala
	 * @param institucion
	 * @return
	 */
	public HashMap<String, Object> detalleFactores(Connection con, String escala, String institucion) 
	{
		return objetoDao.detalleFactores(con, escala, institucion);
	}

	/**
	 * 
	 * @param con
	 * @param vo
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
	public boolean modificarCamposSeccion(Connection con, HashMap vo) 
	{
		return objetoDao.modificarCamposSeccion(con, vo);
	}

	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public boolean modificarCampos(Connection con, HashMap vo) 
	{
		return objetoDao.modificarCampos(con, vo);
	}

	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public boolean modificarFactores(Connection con, HashMap vo) 
	{
		return objetoDao.modificarFactores(con, vo);
	}

	/**
	 * 
	 * @param con
	 * @param escala
	 * @return
	 */
	public boolean eliminarEscala(Connection con, String escala) 
	{
		return objetoDao.eliminarEscala(con,escala);
		
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
	public boolean modificarMostrarFactor(Connection con, HashMap vo) 
	{
		return objetoDao.modificarMostrarFactor(con, vo);
	}

	/**
	 * 
	 * @param con
	 * @param codigoFactor
	 * @return
	 */
	public boolean eliminarFactor(Connection con, String codigoFactor) 
	{
		return objetoDao.eliminarFactor(con, codigoFactor);
	}

	/**
	 * 
	 * @param con
	 * @param codigoCampo
	 * @return
	 */
	public boolean eliminarCampos(Connection con, String codigoCampo, String loginUsuario) 
	{
		return objetoDao.eliminarCampos(con, codigoCampo, loginUsuario);
	}

	/**
	 * 
	 * @param con
	 * @param codigoSeccion
	 */
	public boolean eliminarSecciones(Connection con, String codigoSeccion) 
	{
		return objetoDao.eliminarSecciones(con, codigoSeccion);
	}
	
	
}
