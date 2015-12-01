package com.servinte.axioma.orm.delegate.tesoreria;

import java.util.ArrayList;

import org.axioma.util.log.Log4JManager;
import org.hibernate.Criteria;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;

import com.servinte.axioma.dto.tesoreria.DtoConceptoNotasPacientes;
import com.servinte.axioma.orm.ConceptoNotaPaciente;
import com.servinte.axioma.orm.ConceptoNotaPacienteHome;

/**
 * Clase encargada de ejecutar las transacciones del objeto 
 * {@link ConceptoNotaPaciente} de la relación concepto_nota_paciente
 * @author diecorqu
 *  
 */
public class ConceptoNotasPacientesDelegate extends ConceptoNotaPacienteHome {

	/**
	 * Método encargado de persistir la entidad ConceptoNotaPaciente
	 * @param conceptoNotaPaciente
	 * @return boolean - resultado de la operación
	 */
	public boolean guardarConceptoNotaPaciente(ConceptoNotaPaciente conceptoNotaPaciente) {
		boolean save = false;
		try{
			super.persist(conceptoNotaPaciente);
			save = true;
		}catch (Exception e) {
			save = false;
			Log4JManager.error("No se pudo guardar el concepto con código: " + 
					conceptoNotaPaciente.getCodigo(),e);
		}		
		return save;
	}

	/**
	 * Método encargado de eliminar un registro de ConceptoNotaPaciente
	 * @param conceptoNotaPaciente
	 */
	public void eliminarConceptoNotaPaciente(ConceptoNotaPaciente conceptoNotaPaciente) {
		try{
			super.delete(conceptoNotaPaciente);
		}catch (Exception e) {
			Log4JManager.error("No se pudo eliminar el concepto con código: " + 
					conceptoNotaPaciente.getCodigo(),e);
		}	
	}

	/**
	 * Método encargado de modificar la entidad ConceptoNotaPaciente
	 * @param conceptoNotaPaciente
	 * @return ConceptoNotaPaciente
	 */
	public ConceptoNotaPaciente modificarConceptoNotaPaciente(
			ConceptoNotaPaciente conceptoNotaPaciente) {
		ConceptoNotaPaciente concepto = null;
		try {
			concepto = super.merge(conceptoNotaPaciente);
		} catch (Exception e) {
			throw new RuntimeException(">>>>>>>>>>>>>>>>>>>>>>HibernateException<<<<<<<<<<<<<<<<<<<<<<<<");
		}
		return concepto;
	}

	/**
	 * Método encargado de buscar un ConceptoNotaPaciente por codigo
	 * @param codigo
	 * @return ConceptoNotaPaciente
	 */
	public ConceptoNotaPaciente findById(long codigo) {
		return super.findById(codigo);
	}

	/**
	 * Método encargado de retornar una lista de DtoConceptosNotasPacientes con los 
	 * Conceptos de Notas de Pacientes existentes en el sistema
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<DtoConceptoNotasPacientes> listarConceptoNotasPacientes() {
		Criteria criteria = sessionFactory.getCurrentSession()
			.createCriteria(ConceptoNotaPaciente.class, "conceptoNotasPaciente")
			.createAlias("conceptoNotasPaciente.notaPacientes", "notasPacientes", Criteria.LEFT_JOIN)
			.addOrder(Property.forName("codigo").asc());
		
		ProjectionList projectionList = Projections.projectionList(); 
		projectionList.add(Projections.property("conceptoNotasPaciente.codigopk") ,"codigoPk");
		projectionList.add(Projections.property("conceptoNotasPaciente.codigo") ,"codigo");
		projectionList.add(Projections.property("conceptoNotasPaciente.descripcion") ,"descripcion");
		projectionList.add(Projections.property("conceptoNotasPaciente.naturaleza") ,"naturaleza");
		projectionList.add(Projections.property("conceptoNotasPaciente.activo") ,"activo");
		projectionList.add(Projections.property("notasPacientes.naturaleza") ,"relacionNaturalezaNotaPaciente");
		
		criteria.setProjection(Projections.distinct(projectionList));
		
		criteria.setResultTransformer(Transformers.aliasToBean(DtoConceptoNotasPacientes.class));
		
		return (ArrayList<DtoConceptoNotasPacientes>)criteria.list(); 
	}
	
	/**
	 * Método encargado de retornar una lista de DtoConceptosNotasPacientes con los 
	 * Conceptos de Notas de Pacientes según el estado
	 * @param boolean estado
	 * @return ArrayList<DtoConceptoNotasPacientes>
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<DtoConceptoNotasPacientes> listarConceptoNotasPacientesEstado(boolean estado) {
		Criteria criteria = sessionFactory.getCurrentSession()
			.createCriteria(ConceptoNotaPaciente.class, "conceptoNotasPaciente")
			.createAlias("conceptoNotasPaciente.notaPacientes", "notasPacientes", Criteria.LEFT_JOIN)
			.add(Restrictions.eq("conceptoNotasPaciente.activo", estado))
			.addOrder(Property.forName("codigo").asc());
		
		ProjectionList projectionList = Projections.projectionList(); 
		projectionList.add(Projections.property("conceptoNotasPaciente.codigopk") ,"codigoPk");
		projectionList.add(Projections.property("conceptoNotasPaciente.codigo") ,"codigo");
		projectionList.add(Projections.property("conceptoNotasPaciente.descripcion") ,"descripcion");
		projectionList.add(Projections.property("conceptoNotasPaciente.naturaleza") ,"naturaleza");
		projectionList.add(Projections.property("conceptoNotasPaciente.activo") ,"activo");
		projectionList.add(Projections.property("notasPacientes.naturaleza") ,"relacionNaturalezaNotaPaciente");
		
		criteria.setProjection(Projections.distinct(projectionList));
		
		criteria.setResultTransformer(Transformers.aliasToBean(DtoConceptoNotasPacientes.class));
		
		return (ArrayList<DtoConceptoNotasPacientes>)criteria.list(); 
	}
	
	/**
	 * Método encargado de retornar una lista de DtoConceptosNotasPacientes con los 
	 * Conceptos de Notas de Pacientes existentes en el sistema para la busqueda 
	 * avanzada
	 * @return ArrayList<DtoConceptoNotasPacientes>
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<DtoConceptoNotasPacientes> listarConceptoNotasPacientesBusquedaAvanzada(
			DtoConceptoNotasPacientes dtoConceptosNotasPacientes) {
		Criteria criteria = sessionFactory.getCurrentSession()
			.createCriteria(ConceptoNotaPaciente.class, "conceptoNotasPaciente")
			.createAlias("conceptoNotasPaciente.notaPacientes", "notasPacientes", Criteria.LEFT_JOIN);
		
		if (dtoConceptosNotasPacientes.getCodigo() != null) {
			criteria.add(Restrictions.eq("conceptoNotasPaciente.codigo", dtoConceptosNotasPacientes.getCodigo()));
		}	
		if (dtoConceptosNotasPacientes.getDescripcion() != null) {
			criteria.add(Restrictions.ilike("conceptoNotasPaciente.descripcion", "%"+dtoConceptosNotasPacientes.getDescripcion()+"%"));
		}
		if (dtoConceptosNotasPacientes.getNaturaleza() != null) {
			criteria.add(Restrictions.eq("conceptoNotasPaciente.naturaleza", dtoConceptosNotasPacientes.getNaturaleza()));
		}
		if (dtoConceptosNotasPacientes.getActivo() != null) {
			criteria.add(Restrictions.eq("conceptoNotasPaciente.activo", dtoConceptosNotasPacientes.getActivo()));
		}
		criteria.addOrder(Property.forName("codigo").asc());
		
		ProjectionList projectionList = Projections.projectionList(); 
		projectionList.add(Projections.property("conceptoNotasPaciente.codigopk") ,"codigoPk");
		projectionList.add(Projections.property("conceptoNotasPaciente.codigo") ,"codigo");
		projectionList.add(Projections.property("conceptoNotasPaciente.descripcion") ,"descripcion");
		projectionList.add(Projections.property("conceptoNotasPaciente.naturaleza") ,"naturaleza");
		projectionList.add(Projections.property("conceptoNotasPaciente.activo") ,"activo");
		projectionList.add(Projections.property("notasPacientes.naturaleza") ,"relacionNaturalezaNotaPaciente");
		
		criteria.setProjection(Projections.distinct(projectionList));
		
		criteria.setResultTransformer(Transformers.aliasToBean(DtoConceptoNotasPacientes.class));
		
		return (ArrayList<DtoConceptoNotasPacientes>)criteria.list(); 
	}
	
}
