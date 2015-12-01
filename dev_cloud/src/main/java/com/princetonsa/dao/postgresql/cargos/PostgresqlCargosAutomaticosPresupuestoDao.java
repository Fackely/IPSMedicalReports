package com.princetonsa.dao.postgresql.cargos;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.cargos.CargosAutomaticosPresupuestoDao;
import com.princetonsa.dao.sqlbase.cargos.SqlBaseCargosAutomaticosPresupuestoDao;


public class PostgresqlCargosAutomaticosPresupuestoDao implements
		CargosAutomaticosPresupuestoDao 
		
{
	
	/**
	 * 
	 */
	public HashMap cargarServiciosAutomaticos(Connection con, int codigoPaciente) 
	{
		
		return SqlBaseCargosAutomaticosPresupuestoDao.cargarServiciosAutomaticos(con,codigoPaciente);
		
	}
	
	/**
	 * 
	 */
	public HashMap obtenerCentrosCosto(Connection con, String servicios) 
	{
		
		return SqlBaseCargosAutomaticosPresupuestoDao.obtenerCentrosCosto(con,servicios);
		
	}

	public HashMap cargarTarifas(Connection con, int numeroSolicitud, int servicio) 
	{
		return SqlBaseCargosAutomaticosPresupuestoDao.cargarTarifas(con,numeroSolicitud,servicio);
	}

	/**
	 * Metodo para validar si el paciente tiene asociado presupuesto
	 * @param con
	 * @param codigoPaciente
	 * @return
	 */
	public boolean pacienteTienePresupuesto(Connection con,int codigoPaciente)
	{
		return SqlBaseCargosAutomaticosPresupuestoDao.pacienteTienePresupuesto(con, codigoPaciente);
	}
	
}
