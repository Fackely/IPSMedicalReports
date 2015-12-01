/*
 * Oct 22, 2007
 * Proyect axioma_oct0707
 * Paquete com.princetonsa.mundo.historiaClinica
 * @author Jorge Armando Osorio Velasquez
 * Compilador Java 1.5.0_07-b03
 */
package com.princetonsa.mundo.historiaClinica;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.historiaClinica.RegistroTerapiasGrupalesDao;

/**
 * @author Jorge Armando Osorio Velasquez
 *
 */
public class RegistroTerapiasGrupales 
{
	
	private RegistroTerapiasGrupalesDao objetoDao;

	/**
	 * 
	 *
	 */
	public RegistroTerapiasGrupales()
	{
		this.init(System.getProperty("TIPOBD"));
	}

	/**
	 * 
	 * @param tipoBD
	 */
	private void init(String tipoBD) 
	{
		objetoDao=DaoFactory.getDaoFactory(tipoBD).getRegistroTerapiasGrupalesDao();
	}

	/**
	 * 
	 * Envio como parametro la fecha, ya que puede que se quiera consultar otra fecha diferente a la actual.
	 * si se envia en blanco, se toma por defecto la fecha actual.
	 * @param con
	 * @param centroAtencion 
	 * @param codigoSexo 
	 * @param string
	 * @return
	 */
	public HashMap<String, Object> getpacientesTerapiasGrupales(Connection con, String fecha, int centroAtencion, int codigoSexo) 
	{
		return objetoDao.getpacientesTerapiasGrupales(con,fecha,centroAtencion,codigoSexo);
	}

	
	/**
	 * 
	 * @param con
	 * @param codigoTerapia
	 * @param vo
	 * @return
	 */
	public boolean insertarCuentasTerapia(Connection con, int codigoTerapia, HashMap<String, Object> vo) 
	{
		return objetoDao.insertarCuentasTerapia(con,codigoTerapia,vo);
	}
	
	/**
	 * 
	 * @param con
	 * @param string
	 * @param codigoCentroAtencion
	 * @param sexoServicio
	 * @param centroCosto
	 * @return
	 */
	public HashMap<String, Object> getpacientesTerapiasGrupalesCentro(Connection con, String fecha, int centroAtencion, int codigoSexo, String centroCosto) 
	{
		return objetoDao.getpacientesTerapiasGrupalesCentro(con,fecha,centroAtencion,codigoSexo,centroCosto);
	}



}
