package com.servinte.axioma.dao.interfaz.manejoPaciente;

import java.util.ArrayList;

import util.Errores;

import com.princetonsa.dto.manejoPaciente.DTONaturalezaPaciente;
import com.servinte.axioma.orm.NaturalezaPacientes;

/**
 * Esta clase se encarga de definir los m&eacute;todos de
 * negocio para la entidad Naturaleza Paciente
 * 
 * @author Angela Maria Aguirre
 * @since 11/08/2010
 */
public interface INaturalezaPacienteDAO {
	
	/**
	 * 
	 * Este M&eacute;todo se encarga de consultar todos los registros de 
	 * Naturalezas Pacientes del sistema
	 * 
	 * @return ArrayList<NaturalezaPacientes>
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ArrayList<NaturalezaPacientes> consultarNaturalezaPacientes();

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
	public ArrayList<Errores> actualizarNaturalezaPaciente(NaturalezaPacientes registro);


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
	public boolean eliminarNaturalezaPaciente(NaturalezaPacientes registro);
	
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
	public ArrayList<Errores> crearNaturalezaPaciente(NaturalezaPacientes registro);
	
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
	public ArrayList<NaturalezaPacientes> consultarNaturalezaPacientesPorCampos(NaturalezaPacientes naturalezaPaciente);
	
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
	public boolean obtenerRelacionesForaneas(NaturalezaPacientes registro);
	
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
	public NaturalezaPacientes fidByID(int id);
	
	/**
	 * 
	 * M?todo se encarga de consultar las Naturaleza Pacientes asociadas al regimen
	 * del convenio
	 * 
	 * @param codigoConvenio
	 * 
	 * @return ArrayList<DTONaturalezaPaciente>
	 * @author Ricardo Ruiz
	 *
	 */
	public ArrayList<DTONaturalezaPaciente> listarNaturalezaPacientesPorConvenio(int codigoConvenio);
	
			
}
