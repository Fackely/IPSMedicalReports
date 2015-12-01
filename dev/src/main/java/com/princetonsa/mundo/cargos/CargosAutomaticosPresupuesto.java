package com.princetonsa.mundo.cargos;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.cargos.CargosAutomaticosPresupuestoDao;


public class CargosAutomaticosPresupuesto 
{
	
	/**
	 * 
	 */
	private CargosAutomaticosPresupuestoDao objetoDao;
	
	/**
	 * 
	 */
	private HashMap serviciosAutomaticos;
	
	
	/**
	 * 
	 *
	 */
	public CargosAutomaticosPresupuesto() 
	{
		init(System.getProperty("TIPOBD"));
		this.reset();
	}
	
	/**
	 * 
	 * @param tipoBD
	 * @return
	 */
	private boolean init(String tipoBD) 
	{
		if(objetoDao==null)
		{
			DaoFactory myFactory=DaoFactory.getDaoFactory(tipoBD);
			objetoDao=myFactory.getCargosAutomaticosPresupuestoDao();
			if(objetoDao!=null)
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
		serviciosAutomaticos=new HashMap();
    	serviciosAutomaticos.put("numRegistros","0");
   	}
	
	/**
	 * 
	 * @param con
	 * @param codigoPaciente
	 * @return
	 */
	public HashMap cargarServiciosAutomaticos(Connection con, int codigoPaciente) 
	{
		return objetoDao.cargarServiciosAutomaticos(con,codigoPaciente);
	}
	
	/**
	 * 
	 * @param con
	 * @param servicios
	 * @return
	 */
	public HashMap obtenerCentrosCosto(Connection con, String servicios) 
	{
		return objetoDao.obtenerCentrosCosto(con,servicios);
	}
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @param servicio
	 * @return
	 */
	public HashMap cargarTarifas(Connection con, int numeroSolicitud, int servicio)
	{
		return objetoDao.cargarTarifas(con,numeroSolicitud,servicio);
	}
	
	/**
	 * Metodo que valida si el paciente tiene asiciado un presupuesto
	 * @param con
	 * @param codigoPaciente
	 * @return
	 */
	public boolean pacienteTienePresupuesto(Connection con,int codigoPaciente)
	{
		return objetoDao.pacienteTienePresupuesto(con, codigoPaciente);
	}
	
}
