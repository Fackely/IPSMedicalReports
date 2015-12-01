package com.servinte.axioma.dao.impl.manejoPaciente;

import java.util.ArrayList;

import util.Errores;

import com.princetonsa.dto.manejoPaciente.DTOExcepcionNaturalezaPaciente;
import com.servinte.axioma.dao.interfaz.manejoPaciente.IExcepcionesNaturalezaDAO;
import com.servinte.axioma.orm.ExcepcionesNaturaleza;
import com.servinte.axioma.orm.delegate.manejoPaciente.ExcepcionesNaturalezaDelegate;

/**
 * Esta clase se encarga de ejecutar los m&eacute;todos de
 * negocio para la entidad Excepci&oacute;n para Naturaleza de Paciente
 * 
 * @author Angela Maria Aguirre
 * @since 18/08/2010
 */
public class ExcepcionesNaturalezaHibernateDAO implements
		IExcepcionesNaturalezaDAO {
	
	ExcepcionesNaturalezaDelegate delegate = new ExcepcionesNaturalezaDelegate(); 
	
	/**
	 * 
	 * Este M&eacute;todo se encarga de consultar todos los registros de 
	 * Excepciones de Naturaleza Pacientes
	 * 
	 * @return ArrayList<ExcepcionesNaturaleza>
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ArrayList<ExcepcionesNaturaleza> consultarExcepcionNaturaleza(){
		return delegate.consultarExcepcionNaturaleza();
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
		return delegate.actualizarExcepcionNaturaleza(registro);
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
		return  delegate.eliminarExcepcionNaturaleza(registro);
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
		return delegate.crearExcepcionNaturaleza(registro);
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
		return delegate.consultarExcepcionNaturalezaPorCampos(registro);
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
		return delegate.consultarExcepcionNaturalezaDTO();
	}
	
}
