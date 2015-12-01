package com.servinte.axioma.orm.delegate.odontologia.programas;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import com.servinte.axioma.orm.Programas;
import com.servinte.axioma.orm.ProgramasHome;
/**
 * 
 * @author Edgar Carvajal
 *
 */
public class ProgramasDelegate extends ProgramasHome {
	
	
	/**
	 * 
	 * @author Edgar Carvajal Ruiz
	 * @param dtoProgramas
	 * @return
	 */
	public List<Programas> consultaxInstitucion(Programas dtoProgramas)
	{
		
		Criteria criterio= sessionFactory.getCurrentSession().createCriteria(Programas.class);
		
		if(dtoProgramas.getInstituciones().getCodigo()>0)
		{
			criterio.add(Restrictions.eq("instituciones.codigo",dtoProgramas.getInstituciones().getCodigo()));
		 
		}
		
		return criterio.list();
	}
	
	/**
	 * 
	 * M&eacute;todo encargado de listar los programas registrados en el sistema
	 * 
	 * @return ArrayList<Programas>
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public ArrayList<Programas> listarProgramas() {
		return (ArrayList<Programas>) sessionFactory.getCurrentSession()
				.createCriteria(Programas.class).list();
	}
	


}
