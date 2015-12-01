package com.servinte.axioma.orm.delegate.odontologia.ventaTarjeta;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import com.servinte.axioma.orm.BeneficiarioTcPaciente;
import com.servinte.axioma.orm.BeneficiarioTcPacienteHome;
import com.servinte.axioma.orm.Pacientes;

public class BeneficiarioTcPacienteDelegate extends BeneficiarioTcPacienteHome{
	
	
	
	
	/**
	 * Metodo para lista 
	 * @param paciente
	 * @return
	 */
	public BeneficiarioTcPaciente listaBeneficiarioPaciente(Pacientes paciente)
	{
		Criteria criterio= sessionFactory.getCurrentSession().createCriteria(BeneficiarioTcPaciente.class);
		criterio.add(Restrictions.eq("pacientes", paciente));
		return (BeneficiarioTcPaciente) criterio.uniqueResult();
	}

}
