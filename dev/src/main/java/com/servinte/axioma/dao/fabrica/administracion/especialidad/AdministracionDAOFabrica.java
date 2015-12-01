package com.servinte.axioma.dao.fabrica.administracion.especialidad;

import com.servinte.axioma.dao.impl.administracion.CentroCostosDAO;
import com.servinte.axioma.dao.impl.manejoPaciente.HistObserGenerPacienteDAO;
import com.servinte.axioma.dao.interfaz.administracion.ICentroCostosDAO;
import com.servinte.axioma.dao.interfaz.manejoPaciente.IHistObserGenerPacienteDAO;

/**
 * TODO MODIFICAR ESTA FABRICA 
 * 
 * @author EDGAR CARVAJAL
 *
 */
public abstract class AdministracionDAOFabrica 
{
	
	
	/**	
	 * CREAR CENTROS DE COSTOS DAO
	 * @author Edgar Carvajal Ruiz
	 * @return
	 */
	public static final  ICentroCostosDAO crearCentroCostoDAO()
	{
		 return new CentroCostosDAO();
	}
	
	
	
	/**
	 * 
	 * @return
	 */
	public static final IHistObserGenerPacienteDAO crearHistoriaGeneraPacienteDAO(){
		
		return new  HistObserGenerPacienteDAO();
	}
	
}
