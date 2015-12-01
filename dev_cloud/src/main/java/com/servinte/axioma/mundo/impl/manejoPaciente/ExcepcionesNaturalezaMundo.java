package com.servinte.axioma.mundo.impl.manejoPaciente;

import java.util.ArrayList;

import util.Errores;

import com.princetonsa.dto.manejoPaciente.DTOExcepcionNaturalezaPaciente;
import com.servinte.axioma.dao.fabrica.ManejoPacienteDAOFabrica;
import com.servinte.axioma.dao.interfaz.manejoPaciente.IExcepcionesNaturalezaDAO;
import com.servinte.axioma.dao.interfaz.manejoPaciente.INaturalezaPacienteDAO;
import com.servinte.axioma.dao.interfaz.manejoPaciente.ITiposRegimenDAO;
import com.servinte.axioma.mundo.interfaz.manejoPaciente.IExcepcionesNaturalezaMundo;
import com.servinte.axioma.orm.ExcepcionesNaturaleza;
import com.servinte.axioma.orm.NaturalezaPacientes;
import com.servinte.axioma.orm.TiposRegimen;

/**
 * Esta clase se encarga de ejecutar los m&eacute;todos de
 * negocio para la entidad Excepci&oacute;n para Naturaleza de Paciente
 * 
 * @author Angela Maria Aguirre
 * @since 18/08/2010
 */
public class ExcepcionesNaturalezaMundo implements IExcepcionesNaturalezaMundo {
	
	/**
	 * Instancia de la entidad IExcepcionesNaturalezaDAO
	 */
	IExcepcionesNaturalezaDAO dao;
	
	public ExcepcionesNaturalezaMundo(){
		dao = ManejoPacienteDAOFabrica.crearExcepcionNaturalezaDAO();		
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
	public ArrayList<ExcepcionesNaturaleza> consultarExcepcionNaturaleza(){
		return dao.consultarExcepcionNaturaleza();
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
		return dao.actualizarExcepcionNaturaleza(registro);
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
		return dao.eliminarExcepcionNaturaleza(registro);
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
		return dao.crearExcepcionNaturaleza(registro);
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
		ArrayList<Errores> listaErrores=new ArrayList<Errores>();		
		if(nuevo){
			listaErrores = dao.crearExcepcionNaturaleza(registro);
		}else{
			listaErrores = dao.actualizarExcepcionNaturaleza(registro);
		}		
		return listaErrores;
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
		return dao.consultarExcepcionNaturalezaPorCampos(registro);
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
		
		ArrayList<Errores> listaErrores=new ArrayList<Errores>();
		boolean esError=false;
			
		
		ArrayList<ExcepcionesNaturaleza> listaValidada = 
			consultarExcepcionNaturalezaPorCampos(registro);
		
		if(listaValidada!=null && listaValidada.size()>0){
			if(nuevo){			
				esError=true;
			}else{
				for(ExcepcionesNaturaleza registroExistente : listaValidada){
					if(registroExistente.getCodigo()!=registro.getCodigo()){
						esError=true;	
					}
				}
			}
		}
		if(esError){

			INaturalezaPacienteDAO naturalezaDAO = 
				ManejoPacienteDAOFabrica.crearNaturalezaPacienteDAO();
			ITiposRegimenDAO tipoRegimenDAO = ManejoPacienteDAOFabrica.crearTipoRegimenDAO();
			
			NaturalezaPacientes naturaleza = naturalezaDAO.fidByID(
					registro.getNaturalezaPacientes().getCodigo());
			TiposRegimen tipoRegimen = tipoRegimenDAO.findByID(registro.
					getTiposRegimen().getAcronimo());	
			
			Errores error = new Errores("Esta Excepcion ya se encuentra registrada en el sistema", 
					"errores.modManejoPacienteExcepcionNaturaleza.excepcionExistente", 
					tipoRegimen.getNombre(),naturaleza.getNombre());		
			listaErrores.add(error);
		}
		return listaErrores;
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
		return dao.consultarExcepcionNaturalezaDTO();
	}
	
}
