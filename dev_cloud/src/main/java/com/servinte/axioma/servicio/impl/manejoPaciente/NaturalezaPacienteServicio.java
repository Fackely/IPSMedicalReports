package com.servinte.axioma.servicio.impl.manejoPaciente;

import java.util.ArrayList;

import util.Errores;

import com.princetonsa.dto.manejoPaciente.DTONaturalezaPaciente;
import com.servinte.axioma.mundo.fabrica.odontologia.manejopaciente.ManejoPacienteFabricaMundo;
import com.servinte.axioma.mundo.interfaz.manejoPaciente.INaturalezaPacienteMundo;
import com.servinte.axioma.orm.NaturalezaPacientes;
import com.servinte.axioma.servicio.interfaz.manejoPaciente.INaturalezaPacienteServicio;

/**
 * Esta clase se encarga de ejecutar los m&eacute;todos
 * de negocio de la entidad Naturaleza Paciente
 * 
 * @author Angela Maria Aguirre
 * @since 11/08/2010
 */
public class NaturalezaPacienteServicio implements INaturalezaPacienteServicio {
	
	INaturalezaPacienteMundo mundo;
	
	public NaturalezaPacienteServicio(){
		mundo = ManejoPacienteFabricaMundo.crearNaturalezaPacienteMundo();
	}
	
	/**
	 * 
	 * Este M&eacute;todo se encarga de consultar todos los registros de 
	 * Naturalezas Pacientes del sistema
	 * 
	 * @return ArrayList<NaturalezaPacientes>
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ArrayList<NaturalezaPacientes> consultarNaturalezaPacientes(){
		return mundo.consultarNaturalezaPacientes();
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
		return mundo.actualizarNaturalezaPaciente(registro);
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
		return mundo.eliminarNaturalezaPaciente(registro);
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
		return mundo.consultarNaturalezaPacientesPorCampos(naturalezaPaciente);
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
	public ArrayList<Errores> crearNaturalezaPaciente(NaturalezaPacientes registro, boolean nuevo) {
		return mundo.crearNaturalezaPaciente(registro, nuevo);
	}	
	
	/**
	 * 
	 * Este M&eacute;todo se encarga de validar un registro
	 * de Naturalezas Paciente
	 * 
	 * @param NaturalezaPacientes
	 * @param boolean nuevo
	 * @return ArrayList<Errores> 
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ArrayList<Errores> validarNaturalezaPaciente(NaturalezaPacientes registro,boolean nuevo){
		return mundo.validarNaturalezaPaciente(registro, nuevo);
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
		return mundo.obtenerRelacionesForaneas(registro);
	}
	
	/**
	 * 
	 * Este M&eacute;todo se encarga de consultar que para cada
	 * naturaleza paciente exitan o no relaciones con otras tablas
	 * en la base de datos.
	 * 
	 * 
	 * @return ArrayList<DTONaturalezaPaciente>
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ArrayList<DTONaturalezaPaciente> consultarNaturalezaPacienteEliminable(){
		return mundo.consultarNaturalezaPacienteEliminable();
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
		return mundo.fidByID(id);
	}
	

}
