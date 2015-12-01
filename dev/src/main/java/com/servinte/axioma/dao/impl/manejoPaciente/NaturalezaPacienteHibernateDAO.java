package com.servinte.axioma.dao.impl.manejoPaciente;

import java.util.ArrayList;

import util.Errores;

import com.princetonsa.dto.manejoPaciente.DTONaturalezaPaciente;
import com.servinte.axioma.dao.interfaz.manejoPaciente.INaturalezaPacienteDAO;
import com.servinte.axioma.orm.NaturalezaPacientes;
import com.servinte.axioma.orm.delegate.manejoPaciente.NaturalezaPacienteDelegate;

/**
 * Esta clase se encarga de ejecutar los m&eacute;todos
 * de negocio de la entidad Naturaleza Paciente 
 * 
 * @author Angela Maria Aguirre
 * @since 11/08/2010
 */
public class NaturalezaPacienteHibernateDAO implements INaturalezaPacienteDAO {
	
	NaturalezaPacienteDelegate delegate = new NaturalezaPacienteDelegate(); 
	
	/**
	 * Este M&eacute;todo se encarga de
	 * @author, Angela Maria Aguirre
	 * 
	 */
	@Override
	public ArrayList<NaturalezaPacientes> consultarNaturalezaPacientes() {
		return delegate.consultarNaturalezaPacientes();
	}	
	
	/**
	 * 
	 * Este m&eacute;todo se encarga de actualizar el registro
	 * de Naturalezas Paciente
	 * 
	 * @param NaturalezaPacientes
	 * @return ArrayList<Errores> 
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ArrayList<Errores> actualizarNaturalezaPaciente(NaturalezaPacientes registro){
		return delegate.actualizarNaturalezaPaciente(registro);
	}	


	/**
	 * 
	 * Este m&eacute;todo se encarga de eliminar el registro
	 * de Naturalezas Paciente
	 * 
	 * @param NaturalezaPacientes
	 * @return boolean 
	 * @author, Angela Maria Aguirre
	 *
	 */
	public boolean eliminarNaturalezaPaciente(NaturalezaPacientes registro){
		return delegate.eliminarNaturalezaPaciente(registro);
	}	
	
	/**
	 * 
	 * Este m&eacute;todo se encarga de crear un registro
	 * de Naturalezas Paciente
	 * 
	 * @param NaturalezaPacientes
	 * @return ArrayList<Errores> 
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ArrayList<Errores> crearNaturalezaPaciente(NaturalezaPacientes registro){
		return delegate.crearNaturalezaPaciente(registro);
	}
	
	/**
	 * 
	 * Este M&eacute;todo se encarga de consultar todos los registros de 
	 * Naturalezas Pacientes del sistema por un campo especificado
	 * 
	 * @param  NaturalezaPacientes
	 * @return ArrayList<NaturalezaPacientes>
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ArrayList<NaturalezaPacientes> consultarNaturalezaPacientesPorCampos(NaturalezaPacientes naturalezaPaciente){
		return delegate.consultarNaturalezaPacientesPorCampos(naturalezaPaciente);
	}
	
	/**
	 * 
	 * Este m&eacute;todo se encarga de consultar si el registro
	 * de Naturalezas Paciente tiene o no relaciones con otras
	 * tablas de la base de datos
	 * 
	 * @param NaturalezaPacientes
	 * @return boolean 
	 * @author, Angela Maria Aguirre
	 *
	 */
	public boolean obtenerRelacionesForaneas(NaturalezaPacientes registro){
		return delegate.obtenerRelacionesForaneas(registro);
	}	
	
	/**
	 * 
	 * Este M&eacute;todo se encarga de consultar un registro de 
	 * una naturalea paciente por su c&oacute;digo PK
	 * 
	 * @param int
	 * @return NaturalezaPacientes
	 * @author, Angela Maria Aguirre
	 *
	 */
	public NaturalezaPacientes fidByID(int id){
		return delegate.findById(id);
	}

	@Override
	public ArrayList<DTONaturalezaPaciente> listarNaturalezaPacientesPorConvenio(
			int codigoConvenio) {
		return delegate.listarNaturalezaPacientesPorConvenio(codigoConvenio);
	}
	

}
