package com.servinte.axioma.orm.delegate.manejoPaciente;

import java.util.ArrayList;

import org.axioma.util.log.Log4JManager;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;

import util.Errores;

import com.princetonsa.dto.manejoPaciente.DTOExcepcionNaturalezaPaciente;
import com.servinte.axioma.orm.ExcepcionesNaturaleza;
import com.servinte.axioma.orm.ExcepcionesNaturalezaHome;

/**
 * Esta clase se encarga de ejecutar los m&eacute;todos
 * de negocio de la entidad Excepcion para Naturaleza de Paciente
 * 
 * @author Angela Maria Aguirre
 * @since 18/08/2010
 */
public class ExcepcionesNaturalezaDelegate extends ExcepcionesNaturalezaHome {
	
	
	/**
	 * 
	 * Este M&eacute;todo se encarga de consultar todos los registros de 
	 * Excepciones de Naturaleza Pacientes
	 * 
	 * @return ArrayList<DTOExcepcionNaturalezaPaciente>
	 * @author, Angela Maria Aguirre
	 *
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<DTOExcepcionNaturalezaPaciente> consultarExcepcionNaturalezaDTO(){
		
		Criteria criteria = sessionFactory.getCurrentSession()
			.createCriteria(ExcepcionesNaturaleza.class, "excepcion")
			.createAlias("excepcion.tiposRegimen", "tipoRegimen")
			.createAlias("excepcion.naturalezaPacientes", "naturaleza");
		
		criteria.setProjection(Projections.projectionList()
			.add( Projections.property("excepcion.activo"), "activo")
			.add( Projections.property("excepcion.codigo"), "codigo")
			.add( Projections.property("tipoRegimen.acronimo"), "acronimoTipoRegimen")
			.add( Projections.property("tipoRegimen.nombre"), "nombreTipoRegimen")
			.add( Projections.property("naturaleza.codigo"), "codigoNaturalezaPaciente")
			.add( Projections.property("naturaleza.nombre"), "nombreNaturalezaPaciente"));
		
		criteria.addOrder(Order.asc("tipoRegimen.nombre"))
			.setResultTransformer( Transformers.aliasToBean(DTOExcepcionNaturalezaPaciente.class));
		
		ArrayList<DTOExcepcionNaturalezaPaciente> lista=(ArrayList<DTOExcepcionNaturalezaPaciente>)criteria.list();
		
		return lista;
	}
	
	/**
	 * 
	 * Este M&eacute;todo se encarga de consultar todos los registros de 
	 * Excepciones de Naturaleza Pacientes
	 * 
	 * @return ArrayList<NaturalezaPacientes>
	 * @author, Angela Maria Aguirre
	 *
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<ExcepcionesNaturaleza> consultarExcepcionNaturaleza(){
		
		ArrayList<ExcepcionesNaturaleza> lista =  (ArrayList<ExcepcionesNaturaleza>)sessionFactory.getCurrentSession()
			.createCriteria(ExcepcionesNaturaleza.class, "excepcion")
			.addOrder(Order.asc("excepcion.tiposRegimen"))
			.list();
		
		return lista;
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
		ArrayList<Errores> listaErores = new ArrayList<Errores>();		
		try{
			super.merge(registro);
		}catch (Exception e) {			
			Log4JManager.error("No se pudo actualizar el registro de Excepciones de" +
					" Naturaleza Paciente: ",e);
			Errores error = new Errores("Los datos no se pudieron actualizar", 
			"errores.modManejoPacienteExcepcionNaturaleza.actualizacionFallida");
			listaErores.add(error);
		}		
		return listaErores;
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
		boolean save = true;					
		try{
			registro = super.findById(registro.getCodigo());		
			super.delete(registro);
		} catch (Exception e) {
			save = false;
			Log4JManager.error("No se pudo eliminar el registro de " +
					"Excepcion Naturaleza Paciente: ",e);
		}				
		return save;		
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
		ArrayList<Errores> listaErores = new ArrayList<Errores>();
		try{
			super.persist(registro);			
		}catch (Exception e) {			
			Log4JManager.error("No se pudo guardar el registro de Excepciones de Naturalezas Paciente: ",e);
			Errores error = new Errores("Los datos no se pudieron registrar", 
			"errores.modManejoPaciente.registroFallido");
			listaErores.add(error);
		}		
		return listaErores;		
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
	@SuppressWarnings("unchecked")
	public ArrayList<ExcepcionesNaturaleza> consultarExcepcionNaturalezaPorCampos(
			ExcepcionesNaturaleza registro){
		
		Criteria criteria =  sessionFactory.getCurrentSession()
			.createCriteria(ExcepcionesNaturaleza.class, "excepcion")
			.createAlias("excepcion.naturalezaPacientes", "naturaleza")
			.createAlias("excepcion.tiposRegimen", "tipoRegimen");
		
		if(registro.getNaturalezaPacientes()!=null){
			criteria.add(Restrictions.eq("naturaleza.codigo",registro.getNaturalezaPacientes().getCodigo()));
		}
		if(registro.getTiposRegimen()!=null){
			criteria.add(Restrictions.eq("tipoRegimen.acronimo",registro.getTiposRegimen().getAcronimo()));
		}		
		ArrayList<ExcepcionesNaturaleza> lista = (ArrayList)criteria.list();
		
		for(ExcepcionesNaturaleza excepcion:lista){
			excepcion.getNaturalezaPacientes().getCodigo();
			excepcion.getNaturalezaPacientes().getNombre();
		}
		
		return lista;		
	}

}
