package com.servinte.axioma.servicio.impl.manejoPaciente;

import java.util.ArrayList;

import util.Errores;

import com.princetonsa.dto.manejoPaciente.DTOExcepcionNaturalezaPaciente;
import com.servinte.axioma.mundo.fabrica.odontologia.manejopaciente.ManejoPacienteFabricaMundo;
import com.servinte.axioma.mundo.interfaz.manejoPaciente.IExcepcionesNaturalezaMundo;
import com.servinte.axioma.orm.ExcepcionesNaturaleza;
import com.servinte.axioma.servicio.interfaz.manejoPaciente.IExcepcionesNaturalezaServicio;

/**
 * Esta clase se encarga de ejecutar los m&eacute;todos de
 * negocio para la entidad Excepci&oacute;n para Naturaleza de Paciente
 * 
 * @author Angela Maria Aguirre
 * @since 18/08/2010
 */
public class ExcepcionesNaturalezaServicio implements
		IExcepcionesNaturalezaServicio {
	
	/**
	 * Instancia de la entidad IExcepcionesNaturalezaMundo
	 */
	IExcepcionesNaturalezaMundo mundo;
	
	public ExcepcionesNaturalezaServicio(){
		mundo = ManejoPacienteFabricaMundo.crearExcepcionNaturalezaMundo();
	}
	
	/**
	 * 
	 * Este M&eacute;todo se encarga de consultar todos los registros de 
	 * Excepciones de Naturaleza Pacientes
	 * 
	 * @return ArrayList<ExcepcionesNaturaleza>
	 * @author, Angela Maria Aguirre
	 *
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<ExcepcionesNaturaleza> consultarExcepcionNaturaleza(){
		return mundo.consultarExcepcionNaturaleza();
	}


	/**
	 * 
	 * Este m&eacute;todo se encarga de actualizar el registro
	 * de Excepciones de Naturaleza Pacientes
	 * 
	 * @param ExcepcionesNaturaleza
	 * @return ArrayList<Errores> 
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ArrayList<Errores> actualizarExcepcionNaturaleza(ExcepcionesNaturaleza registro){
		return mundo.actualizarExcepcionNaturaleza(registro);
	}

	/**
	 * 
	 * Este m&eacute;todo se encarga de eliminar el registro
	 * de Excepciones de Naturalezas Paciente
	 * 
	 * @param ExcepcionesNaturaleza
	 * @return boolean 
	 * @author, Angela Maria Aguirre
	 *
	 */
	public boolean eliminarExcepcionNaturaleza(ExcepcionesNaturaleza registro){
		return mundo.eliminarExcepcionNaturaleza(registro);
	}

	
	/**
	 * 
	 * Este m&eacute;todo se encarga de crear un registro
	 * de Excepciones de Naturalezas Paciente
	 * 
	 * @param ExcepcionesNaturaleza
	 * @return ArrayList<Errores> 
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ArrayList<Errores> crearExcepcionNaturaleza(ExcepcionesNaturaleza registro){
		return mundo.crearExcepcionNaturaleza(registro);
	}
	
	/**
	 * 
	 * Este m&eacute;todo se encarga de crear un registro
	 * de Excepciones de Naturalezas Paciente
	 * 
	 * @param ExcepcionesNaturaleza
	 * @param boolean
	 * @return ArrayList<Errores> 
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ArrayList<Errores> crearExcepcionNaturaleza(ExcepcionesNaturaleza registro, boolean nuevo){
		return mundo.crearExcepcionNaturaleza(registro, nuevo);
	}

	/**
	 * 
	 * Este M&eacute;todo se encarga de consultar todos los registros de 
	 * Excepciones de Naturalezas Pacientes del sistema por un campo especificado
	 * 
	 * @param  ExcepcionesNaturaleza
	 * @return ArrayList<ExcepcionesNaturaleza>
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ArrayList<ExcepcionesNaturaleza> consultarExcepcionNaturalezaPorCampos(
			ExcepcionesNaturaleza registro){
		return mundo.consultarExcepcionNaturalezaPorCampos(registro);
	}
	
	/**
	 * 
	 * Este M&eacute;todo se encarga de validar un registros
	 * de Excepciones para Naturalezas de Paciente
	 * 
	 * @param ExcepcionesNaturaleza
	 * @param boolean nuevo
	 * @return ArrayList<Errores> 
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ArrayList<Errores> validarExcepcionNaturalezaPaciente(
			ExcepcionesNaturaleza registro,boolean nuevo){
		return mundo.validarExcepcionNaturalezaPaciente(registro, nuevo);
	}
	
	/**
	 * 
	 * Este M&eacute;todo se encarga de consultar todos los registros de 
	 * Excepciones de Naturaleza Pacientes
	 * 
	 * @return ArrayList<DTOExcepcionNaturalezaPaciente>
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ArrayList<DTOExcepcionNaturalezaPaciente> consultarExcepcionNaturalezaDTO(){
		return mundo.consultarExcepcionNaturalezaDTO();
	}

}
