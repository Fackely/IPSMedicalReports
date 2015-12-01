package com.servinte.axioma.mundo.impl.odontologia.agendaOdontologica;

import java.util.ArrayList;

import com.servinte.axioma.orm.CitasAsociadasAProgramada;
import com.servinte.axioma.orm.CitasAsociadasAProgramadaHome;

@SuppressWarnings("unchecked")
public class CitasAsociadasAProgramadaDelegate extends CitasAsociadasAProgramadaHome {


	/**
	 * Retorna todos
	 * @return
	 */
	public ArrayList<CitasAsociadasAProgramada> listar (int codPaciente){
		
		return (ArrayList<CitasAsociadasAProgramada>) sessionFactory.getCurrentSession()
				.createCriteria(CitasAsociadasAProgramada.class)
				.list();
	}
	
	
	/**
	 * Guarda la entidad enviada 
	 * @param transientInstance
	 * @return true o false si el caso es exitoso o no
	 */
	public boolean guardarCitaAsociadasProgramada(CitasAsociadasAProgramada transientInstance) 
	{
		boolean save = false;
		try {
			super.persist(transientInstance);
			save = true;
		} catch (Exception e) {
			save = false;
		}
		return save;
	}


}


