package com.servinte.axioma.servicio.interfaz.manejoPaciente;

import java.util.ArrayList;

import util.Errores;

import com.princetonsa.dto.manejoPaciente.DTOExcepcionNaturalezaPaciente;
import com.servinte.axioma.orm.ExcepcionesNaturaleza;

/**
 * Esta clase se encarga de definir los m&eacute;todos de
 * negocio para la entidad Excepci&oacute;n para Naturaleza de Paciente
 * 
 * @author Angela Maria Aguirre
 * @since 18/08/2010
 */
public interface IExcepcionesNaturalezaServicio {
	
	/**
	 * 
	 * Este M&eacute;todo se encarga de consultar todos los registros de 
	 * Excepciones de Naturaleza Pacientes
	 * 
	 * @return ArrayList<ExcepcionesNaturaleza>
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ArrayList<ExcepcionesNaturaleza> consultarExcepcionNaturaleza();


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
	public ArrayList<Errores> actualizarExcepcionNaturaleza(ExcepcionesNaturaleza registro);

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
	public boolean eliminarExcepcionNaturaleza(ExcepcionesNaturaleza registro);

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
	public ArrayList<Errores> crearExcepcionNaturaleza(ExcepcionesNaturaleza registro);
	
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
	public ArrayList<Errores> crearExcepcionNaturaleza(ExcepcionesNaturaleza registro, boolean nuevo);

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
			ExcepcionesNaturaleza naturalezaPaciente);
	
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
			ExcepcionesNaturaleza registro,boolean nuevo);
	
	/**
	 * 
	 * Este M&eacute;todo se encarga de consultar todos los registros de 
	 * Excepciones de Naturaleza Pacientes
	 * 
	 * @return ArrayList<DTOExcepcionNaturalezaPaciente>
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ArrayList<DTOExcepcionNaturalezaPaciente> consultarExcepcionNaturalezaDTO();


}
