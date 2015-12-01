package com.princetonsa.mundo.historiaClinica;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.historiaClinica.ImpresionCLAPDao;

public class ImpresionCLAP
{
	
	ImpresionCLAPDao objetoDao;

	public ImpresionCLAP()
	{
		this.reset();
		init(System.getProperty("TIPOBD"));
	}
	
	/**
	 * 
	 * @param property
	 */
	public boolean init(String tipoBD)
	{
	    if ( objetoDao== null ) 
		{
			// Obtengo el DAO que encapsula las operaciones de BD de este objeto
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			objetoDao= myFactory.getImpresionCLAPDao();
			if( objetoDao!= null )
				return true;
		}
		return false;
	}

	/**
	 * 
	 *
	 */
	private void reset()
	{
		
	}

	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public HashMap consultarSolicitudes(Connection con, HashMap vo)
	{
		return objetoDao.consultarSolicitudes(con,vo);
	}

	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param paciente
	 * @param usuario
	 */
	public void generarRegistroLogImpresion(Connection con, HashMap vo)
	{
		objetoDao.generarRegistroLogImpresion(con,vo);
	}

	
	//-------------------------------- MANIZALES -----------------------------------------------------------//
	
	/**
	 * Metodo que consulta la información obstetrica del paciente
	 * @param con -> Conexion
	 * @param parametros -> Mapa que contiene los parametros de la consulta
	 */
	public HashMap consultarInformacionObstetrica(Connection con, HashMap parametros) 
	{
		return objetoDao.consultarInformacionObstetrica(con,parametros);
	}

	/**
	 * 
	 * @param con
	 * @param codigoCirugia
	 * @param codigoPaciente 
	 * @return
	 */
	public HashMap consultarInformacionRecienNacido(Connection con, String codigoCirugia, String codigoPaciente)
	{
		return objetoDao.consultarInformacionRecienNacido(con,codigoCirugia,codigoPaciente);
	}

}
