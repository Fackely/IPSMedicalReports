package com.servinte.axioma.orm.delegate.capitacion;

import java.util.List;

import org.axioma.util.log.Log4JManager;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import com.servinte.axioma.orm.InconsistenSubirPaciente;
import com.servinte.axioma.orm.InconsistenSubirPacienteHome;

public class InconsistenSubirPacienteDelegate extends InconsistenSubirPacienteHome {

	
	/**
	 * 
	 * Este Método se encarga de insertar en la base de datos
	 * un registro de inconsistenSubirPaciente
	 * 
	 * @param inconsistenSubirPaciente 
	 * @return boolean
	 * @author Camilo Gómez
	 *
	 */
	public boolean guardarInconsistenciaSubirPaciente(InconsistenSubirPaciente inconsistenSubirPaciente){
		boolean save = true;					
		try{
			super.attachDirty(inconsistenSubirPaciente);
		} catch (Exception e) {
			save = false;
			Log4JManager.error("No se pudo guardar el registro de " +
					"inconsistencia subir paciente: ",e);
		}				
		return save;				
	}
	
	/**
	 * Lista las inconsistencias asociadas al log de subir pacientes
	 * 
	 * @author Ricardo Ruiz
	 * @param codigoLogSubirPaciente
	 * @return List<InconsistenSubirPaciente>
	*/
	@SuppressWarnings("unchecked")
	public List<InconsistenSubirPaciente> buscarInconsistenciasPorLog(long codigoLogSubirPaciente)
	{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(InconsistenSubirPaciente.class, "inconsistenciaSubirPaciente");
			criteria.createAlias("inconsistenciaSubirPaciente.logSubirPacientes", "logSubirPaciente");
			criteria.add(Restrictions.eq("logSubirPaciente.codigoPk" , codigoLogSubirPaciente));
			return criteria.list();
	}

}
