package com.princetonsa.dao.oracle.historiaClinica;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.historiaClinica.RegistroEventosAdversosDao;
import com.princetonsa.dao.manejoPaciente.EncuestaCalidadAtencionDao;
import com.princetonsa.dao.sqlbase.historiaClinica.SqlBaseRegistroEventosAdversosDao;
import com.princetonsa.dao.sqlbase.manejoPaciente.SqlBaseEncuestaCalidadAtencionDao;
import com.princetonsa.mundo.historiaClinica.RegistroEventosAdversos;
import com.princetonsa.mundo.manejoPaciente.EncuestaCalidadAtencion;

/**
 * 
 * @author axioma
 *
 */
public class OracleRegistroEventosAdversosDao implements RegistroEventosAdversosDao 
{
	
	/** 
	 * @param con
	 * @param codigo
	 * @return
	 */
	public HashMap consultar(Connection con, RegistroEventosAdversos encuesta) 
	{
		return SqlBaseRegistroEventosAdversosDao.consultar(con, encuesta);	
	}
	
	/** 
	 * @param con
	 * @param codigo
	 * @return
	 */
	public HashMap consultarDetalleXCuenta(Connection con, RegistroEventosAdversos encuesta) 
	{
		return SqlBaseRegistroEventosAdversosDao.consultarDetalleXCuenta(con, encuesta);	
	}
	
	/** 
	 * @param con
	 * @param codigo
	 * @return
	 */
	public HashMap consultarDetalleXCuenta2(Connection con, RegistroEventosAdversos encuesta) 
	{
		return SqlBaseRegistroEventosAdversosDao.consultarDetalleXCuenta2(con, encuesta);	
	}

	public int guardarEvento(Connection con, HashMap filtro) 
	{
		return SqlBaseRegistroEventosAdversosDao.guardarEvento(con, filtro);
	}


	public int modificar(Connection con, HashMap filtro) 
	{
		return SqlBaseRegistroEventosAdversosDao.modificar(con, filtro);
	}	
	
	
}
