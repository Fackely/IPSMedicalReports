package com.servinte.axioma.orm.delegate.manejoPaciente;

import java.util.ArrayList;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;

import com.servinte.axioma.orm.TiposPaciente;
import com.servinte.axioma.orm.TiposPacienteHome;

/**
 * Esta clase se encarga de ejecutar los métodos de 
 * negocio para la entidad Tipos de Paciente
 * 
 * @author Angela Maria Aguirre
 * @since 2/09/2010
 */
@SuppressWarnings("unchecked")
public class TiposPacienteDelegate extends TiposPacienteHome{
	
	/**
	 * 
	 * Este Método se encarga de consultar todos los 
	 * tipos de paciente registrados
	 * 
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ArrayList<TiposPaciente> buscarTiposPaciente(){
		return  (ArrayList<TiposPaciente>)sessionFactory.getCurrentSession()
		.createCriteria(TiposPaciente.class,"tipoPaciente")
		.addOrder(Order.asc("tipoPaciente.nombre"))
		.list();
	}
	
	
	/**
	 * 
	 * Este Método se encarga de consultar todos los 
	 * tipos de paciente registrados por via de ingreso
	 * 
	 * @author, Fabian Becerra
	 *
	 */
	@SuppressWarnings("rawtypes")
	public ArrayList<TiposPaciente> buscarTiposPacienteXViaIngreso(int codigoViaIngreso){
		Criteria criteria = sessionFactory.getCurrentSession()
		.createCriteria(TiposPaciente.class,"tipoPaciente")
		.createAlias("tipoPaciente.viasIngresos","viasIngresos" );
		criteria.add(Restrictions.eq("viasIngresos.codigo", codigoViaIngreso));
		
		criteria.setProjection(Projections.distinct(Projections.projectionList()
				.add(Projections.property("tipoPaciente.nombre"), "nombre")
				.add(Projections.property("tipoPaciente.acronimo"), "acronimo")
				))
		.setResultTransformer( Transformers.aliasToBean(TiposPaciente.class));
		criteria.addOrder( Order.asc("tipoPaciente.nombre") );
		
		ArrayList<TiposPaciente> listadoTiposPaciente=(ArrayList)criteria.list();
		
		return  listadoTiposPaciente;
	}
	
}
