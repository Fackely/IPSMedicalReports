package com.servinte.axioma.orm.delegate.manejoPaciente;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import com.servinte.axioma.orm.HistObserGenerPaciente;
import com.servinte.axioma.orm.HistObserGenerPacienteHome;
import com.servinte.axioma.orm.Pacientes;

/**
 * 
 * @author axioma
 *
 */
@SuppressWarnings("unchecked")
public class HistObserGenerPacienteDelegate extends HistObserGenerPacienteHome {
	
	
	
	/**
	 * listaHistoricoObservaciones
	 * @param paciente
	 * @return List<HistObserGenerPaciente>
	 */
	public List<HistObserGenerPaciente> listaHistoricoObservaciones(Pacientes paciente){
		
		Criteria criterio= sessionFactory.getCurrentSession().createCriteria(HistObserGenerPaciente.class, "histoObser")
		.createAlias("histoObser.usuarios", "usuario")
		.createAlias("usuario.personas", "persona");
		criterio.add(Restrictions.eq("pacientes.codigoPaciente", paciente.getCodigoPaciente()));
		return criterio.list();
	}
	
	

}
