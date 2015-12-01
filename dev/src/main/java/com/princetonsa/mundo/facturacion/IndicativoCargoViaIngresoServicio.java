package com.princetonsa.mundo.facturacion;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.IndicativoCargoViaIngresoServicioDao;

public class IndicativoCargoViaIngresoServicio
{
	
	/**
	 * 
	 */
	IndicativoCargoViaIngresoServicioDao objetoDao;
	
	
	public IndicativoCargoViaIngresoServicio()
	{
		this.reset();
		this.init(System.getProperty("TIPOBD"));
	}
	
	/**
	 * 
	 * @param property
	 */
	private void init(String tipoBD)
	{
		if (objetoDao == null) 
		{
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			objetoDao = myFactory.getIndicativoCargoViaIngresoServicioDao();
		}
	}
	

	private void reset()
	{
		
	}

	/**
	 * Metodo que retorna todos los grupos de servicios que estan relacionados a algun procedimiento.
	 * @param con
	 * @param codigoInstitucion
	 * @return
	 */
	public HashMap consultarGruposServiciosProcedimientosInstitucion(Connection con, String codigoInstitucion)
	{
		return objetoDao.consultarGruposServiciosProcedimientosInstitucion(con,codigoInstitucion);
	}

	/**
	 * 
	 * @param con
	 * @param grupoServicio
	 * @param viaIngreso
	 * @return
	 */
	public HashMap consultarServiciosGrupoServicioViaIngreso(Connection con, String grupoServicio, String viaIngreso, String tipoPaciente)
	{
		return objetoDao.consultarServiciosGrupoServicioViaIngreso(con,grupoServicio,viaIngreso,tipoPaciente);
	}

	/**
	 * 
	 * @param con
	 * @param viaIngreso
	 * @param servicio
	 * @param institucion
	 * @param cargoSolicitud
	 * @param cargoProceso
	 * @return
	 */
	public boolean actualizarServicioProcedimientoViaIngreso(Connection con, String viaIngreso, String servicio, String institucion, String tipoPaciente, String cargoSolicitud, String cargoProceso)
	{
		return objetoDao.actualizarServicioProcedimientoViaIngreso(con,viaIngreso,servicio,institucion,tipoPaciente, cargoSolicitud,cargoProceso);
	}



}
