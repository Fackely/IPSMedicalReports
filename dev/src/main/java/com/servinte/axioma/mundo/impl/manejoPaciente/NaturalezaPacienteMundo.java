package com.servinte.axioma.mundo.impl.manejoPaciente;

import java.util.ArrayList;

import util.ConstantesBD;
import util.Errores;
import util.Utilidades;

import com.princetonsa.dto.manejoPaciente.DTONaturalezaPaciente;
import com.servinte.axioma.dao.fabrica.ManejoPacienteDAOFabrica;
import com.servinte.axioma.dao.interfaz.manejoPaciente.INaturalezaPacienteDAO;
import com.servinte.axioma.mundo.interfaz.manejoPaciente.INaturalezaPacienteMundo;
import com.servinte.axioma.orm.NaturalezaPacientes;

/**
 * Esta clase se encarga de ejecutar los m&eacute;todos
 * de negocio de la entidad Naturaleza Paciente
 *  
 * @author Angela Maria Aguirre
 * @since 11/08/2010
 */
public class NaturalezaPacienteMundo implements INaturalezaPacienteMundo {
	INaturalezaPacienteDAO dao;
	
	public NaturalezaPacienteMundo(){
		dao = ManejoPacienteDAOFabrica.crearNaturalezaPacienteDAO();
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
		return dao.consultarNaturalezaPacientes();
	}
	
	
	/**
	 * 
	 * Este m&eacute;todo se encarga de actualizar el registro
	 * de Naturalezas Paciente
	 * 
	 * @param NaturalezaPacientes
	 * @return boolean 
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ArrayList<Errores> actualizarNaturalezaPaciente(NaturalezaPacientes registro){
		return dao.actualizarNaturalezaPaciente(registro);
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
		return dao.eliminarNaturalezaPaciente(registro);
	}
	
	/**
	 * 
	 * Este m&eacute;todo se encarga de crear un registro
	 * de Naturalezas Paciente
	 * 
	 * @param NaturalezaPacientes
	 * @param boolean nuevo
	 * @return ArrayList<Errores> 
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ArrayList<Errores> crearNaturalezaPaciente(NaturalezaPacientes registro,boolean nuevo){
		ArrayList<Errores> listaErrores=new ArrayList<Errores>();		
		if(nuevo){	
			NaturalezaPacientes naturaleza = new NaturalezaPacientes();
			//MT6471 se agrega codigo para la actualización
			naturaleza.setCodigo(registro.getCodigo());
			naturaleza.setActivo(registro.isActivo());
			naturaleza.setConsecutivo(registro.getConsecutivo());
			naturaleza.setNombre(registro.getNombre());
			naturaleza.setFecha(registro.getFecha());
			naturaleza.setHora(registro.getHora());
			naturaleza.setUsuarios(registro.getUsuarios());
			listaErrores = dao.crearNaturalezaPaciente(naturaleza);
		}else{
			NaturalezaPacientes registroExistente = dao.fidByID(registro.getCodigo());
			registroExistente.setActivo(registro.isActivo());
			registroExistente.setConsecutivo(registro.getConsecutivo());
			registroExistente.setNombre(registro.getNombre());
			registroExistente.setFecha(registro.getFecha());
			registroExistente.setHora(registro.getHora());
			registroExistente.setUsuarios(registro.getUsuarios());
			dao.actualizarNaturalezaPaciente(registroExistente);
		}		
		return listaErrores;
	}
	
	/**
	 * 
	 * Este M&eacute;todo se encarga de validar un registros
	 * de Naturalezas Paciente
	 * 
	 * @param NaturalezaPacientes
	 * @param boolean nuevo
	 * @return ArrayList<Errores> 
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ArrayList<Errores> validarNaturalezaPaciente(
			NaturalezaPacientes registro,boolean nuevo){
		
		ArrayList<Errores> listaErrores=new ArrayList<Errores>();
		NaturalezaPacientes registroValidar = new NaturalezaPacientes();
		ArrayList<NaturalezaPacientes> lista = null;		
		
		registroValidar.setCodigo(ConstantesBD.codigoNuncaValido);
		registroValidar.setConsecutivo(registro.getConsecutivo());
		lista = consultarNaturalezaPacientesPorCampos(registroValidar);
		
		if(nuevo){
			if(lista!=null && lista.size()>0){
				Errores error = new Errores("El consecutivo ya se encuentra en el sistema", 
						"errores.modManejoPaciente.consecutivoRepetido", registro.getConsecutivo());		
				listaErrores.add(error);
			}
		}else{
			if(lista!=null && lista.size()>0){
				for(NaturalezaPacientes registroExistente : lista){
					if(registroExistente.getCodigo()!=registro.getCodigo()){
						Errores error = new Errores("El consecutivo ya se encuentra en el sistema", 
								"errores.modManejoPaciente.consecutivoRepetido", registro.getConsecutivo());		
						listaErrores.add(error);
					}
				}
			}
		}
		
				
		registroValidar = new NaturalezaPacientes();
		registroValidar.setCodigo(ConstantesBD.codigoNuncaValido);
		registroValidar.setNombre(registro.getNombre());
		lista = consultarNaturalezaPacientesPorCampos(registroValidar);	
		
		if(nuevo){
			if(lista!=null && lista.size()>0){
				Errores error = new Errores("El nombre ya se encuentra en el sistema", 
						"errores.modManejoPaciente.nombreRepetido", registro.getNombre());
				listaErrores.add(error);
			}
		}else{
			if(lista!=null && lista.size()>0){
				for(NaturalezaPacientes registroExistente : lista){
					if(registroExistente.getCodigo()!=registro.getCodigo()){
						Errores error = new Errores("El nombre ya se encuentra en el sistema", 
								"errores.modManejoPaciente.nombreRepetido", registro.getNombre());
						listaErrores.add(error);
					}
				}
			}
		}
		return listaErrores;
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
		 return dao.consultarNaturalezaPacientesPorCampos(naturalezaPaciente);				
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
		return dao.obtenerRelacionesForaneas(registro);
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
		ArrayList<DTONaturalezaPaciente> listaDTO = new ArrayList<DTONaturalezaPaciente>();
		DTONaturalezaPaciente dto = null;
		
		ArrayList<NaturalezaPacientes> lista = consultarNaturalezaPacientes();
		
		if(!Utilidades.isEmpty(lista)){
			for(NaturalezaPacientes registro : lista){
				
				dto = new DTONaturalezaPaciente();
				
				dto.setCodigo(registro.getCodigo());
				dto.setConsecutivo(registro.getConsecutivo());
				dto.setNombre(registro.getNombre());
				dto.setActivo(registro.isActivo());				
				
				if(obtenerRelacionesForaneas(registro)){				
					dto.setPermiteEliminar(false);
				}else{				
					dto.setPermiteEliminar(true);
				}
				listaDTO.add(dto);
			}
		}				
		return listaDTO;		
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
		return dao.fidByID(id);
	}

	/* (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.manejoPaciente.INaturalezaPacienteMundo#listarNaturalezaPacientesPorConvenio(int)
	 */
	@Override
	public ArrayList<DTONaturalezaPaciente> listarNaturalezaPacientesPorConvenio(
			int codigoConvenio) {
		return dao.listarNaturalezaPacientesPorConvenio(codigoConvenio);
	}

}
