package com.servinte.axioma.orm.delegate.facturacion.convenio;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;

import com.princetonsa.dto.facturacion.DtoConvenio;
import com.princetonsa.dto.facturacion.DtoSeccionConvenioPaciente;
import com.servinte.axioma.orm.ConveniosIngresoPaciente;
import com.servinte.axioma.orm.ConveniosIngresoPacienteHome;


/**
 * Esta clase se encarga de de manejar las transaccciones relacionadas con ConveniosIngresoPacienteHome
 * 
 * @author Cristhian Murillo
 *
 */
@SuppressWarnings("unchecked")
public class ConveniosIngresoPacienteDelegate extends ConveniosIngresoPacienteHome{
	
	
	/**
	 * Retorna los ConveniosIngresoPaciente de un paciente en el estado indicado
	 * 
	 * @param codPaciente
	 * @param listaEstadosIngreso
	 * @return  List<ConveniosIngresoPaciente>
	 */
	public List<ConveniosIngresoPaciente> obtenerConveniosIngresoPacientePorEstado (int codPaciente, char acronimoEstadoActivo)
	{
		
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ConveniosIngresoPaciente.class, "conveniosIngresoPaciente");
		
		criteria.createAlias("conveniosIngresoPaciente.pacientes"		, "pacientes");
		
		criteria.add(Restrictions.eq("pacientes.codigoPaciente"			, codPaciente));
		criteria.add(Restrictions.eq("conveniosIngresoPaciente.activo"	, acronimoEstadoActivo));
		
		return (List<ConveniosIngresoPaciente>) criteria.list();
		
	}
	

	
	
	
	/**
	 * Retorna los ConveniosIngresoPaciente de un paciente en el estado indicado
	 * 
	 * @param codPaciente
	 * @param listaEstadosIngreso
	 * @return  List<DtoSeccionConvenioPaciente>
	 */
	public List<DtoSeccionConvenioPaciente> obtenerDtoConveniosIngresoPacientePorEstado (int codPaciente, char acronimoEstadoActivo)
	{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ConveniosIngresoPaciente.class, "conveniosIngresoPaciente");
		
		criteria.createAlias("conveniosIngresoPaciente.pacientes"		, "pacientes");
		criteria.createAlias("conveniosIngresoPaciente.contratos"		, "contratos");
		criteria.createAlias("contratos.convenios"						, "convenios");
		
		criteria.add(Restrictions.eq("pacientes.codigoPaciente"			, codPaciente));
		criteria.add(Restrictions.eq("conveniosIngresoPaciente.activo"	, acronimoEstadoActivo));
		
		criteria.setProjection( Projections.projectionList()
	  			.add( Projections.property("convenios.codigo")		, "codigoConvenioInt" )
	  			.add( Projections.property("convenios.nombre")		, "descripcionConvenio" )
	  	);
		
		criteria.addOrder(Property.forName("convenios.nombre").asc());
		
		criteria.setResultTransformer( Transformers.aliasToBean(DtoSeccionConvenioPaciente.class));
		
		return (List<DtoSeccionConvenioPaciente>) criteria.list();
		
	}
	
	
	
	/**
	 * Lista los convenios activos del paciente
	 * @author Juan David Ramírez
	 * @param codPaciente
	 * @param acronimoEstadoActivo
	 * @return Lista de {@link DtoConvenio} con los convenios asociados al paciente en estado activo
	 */
	public ArrayList<DtoConvenio> obtenerConveniosIngresoPaciente(int codPaciente, char acronimoEstadoActivo)
	{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ConveniosIngresoPaciente.class, "conveniosIngresoPaciente");
		
		criteria.createAlias("conveniosIngresoPaciente.pacientes", "pacientes");
		criteria.createAlias("conveniosIngresoPaciente.contratos", "contratos");
		criteria.createAlias("contratos.convenios", "convenios");
		
		criteria.setProjection(
			Projections.projectionList()
				.add(Projections.property("convenios.nombre"), "descripcion")
				.add(Projections.property("convenios.codigo"), "codigo")
				.add(Projections.property("convenios.esConvTarCli"), "esConvenioTarjetaClienteChar")
				.add(Projections.property("convenios.activo"), "activo")
			);
		
		criteria.add(Restrictions.eq("pacientes.codigoPaciente"			, codPaciente));
		
		criteria.add(Restrictions.eq("conveniosIngresoPaciente.activo"	, acronimoEstadoActivo));

		criteria.setResultTransformer(Transformers.aliasToBean(DtoConvenio.class));
		
		return (ArrayList<DtoConvenio>) criteria.list();
	}
	
	
	
	/**
	 * Retorna los convenios asociados al ingreso de un paciente que cumplan con las validaciones de asignación de cita:
	 *  -Tipo tarjeta cliente = NO
	 *  -Tipo tarjeta cliente = SI los cuales:
	 *  	-Tengan la tarjeta en estado Activa
	 * 
	 * @author Cristhian Murillo
	 * 
	 * @param codPaciente
	 * @return  List<DtoSeccionConvenioPaciente>
	 */
	public List<DtoSeccionConvenioPaciente> obtenerDtoConveniosIngresoPacienteValidacionesAsignacioncita (int codPaciente)
	{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ConveniosIngresoPaciente.class, "conveniosIngresoPaciente");
		
		criteria.createAlias("conveniosIngresoPaciente.pacientes"				, "pacientes");
		criteria.createAlias("conveniosIngresoPaciente.contratos"				, "contratos");
		criteria.createAlias("contratos.convenios"								, "convenios");
		criteria.createAlias("contratos.ventaTarjetaClientes"					, "ventaTarjetaClientes"		, Criteria.LEFT_JOIN);
		criteria.createAlias("ventaTarjetaClientes.beneficiarioTarjetaClientes"	, "beneficiarioTarjetaClientes"	, Criteria.LEFT_JOIN);	
		
		criteria.add(Restrictions.eq("pacientes.codigoPaciente"			, codPaciente));
		criteria.add(Restrictions.eq("conveniosIngresoPaciente.activo"	, ConstantesBD.acronimoSiChar));
		
		
		criteria.setProjection( Projections.projectionList()
	  			.add( Projections.property("convenios.codigo")					, "codigoConvenioInt" )
	  			.add( Projections.property("convenios.nombre")					, "descripcionConvenio" )
	  			.add( Projections.property("conveniosIngresoPaciente.codigoPk")	, "codigoConveniosIngresoPaciente" )
	  		
	  			.add(Projections.groupProperty("convenios.codigo"))
	  			.add(Projections.groupProperty("convenios.nombre"))
	  			.add(Projections.groupProperty("conveniosIngresoPaciente.codigoPk"))
	  	);
		
		// Tipo Tarjeta cliente NO ---------------------------------------------------
		DetachedCriteria dtCriteriaTarjetaTno = DetachedCriteria.forClass(ConveniosIngresoPaciente.class, "conveniosIngresoPacienteTno"); 
			dtCriteriaTarjetaTno.add(Restrictions.eq("convenios.esConvTarCli"			, ConstantesBD.acronimoNoChar));
			dtCriteriaTarjetaTno.setProjection(Property.forName("conveniosIngresoPacienteTno.codigoPk"));
		// ---------------------------------------------------------------------------
			
			
		// Tipo Tarjeta cliente SI con tarjeta Activa --------------------------------
		DetachedCriteria dtCriteriaTarjetaTsi = DetachedCriteria.forClass(ConveniosIngresoPaciente.class, "conveniosIngresoPacienteTsi"); 
			dtCriteriaTarjetaTsi.add(Restrictions.eq("convenios.esConvTarCli"						, ConstantesBD.acronimoSiChar));
			dtCriteriaTarjetaTsi.add(Restrictions.eq("beneficiarioTarjetaClientes.estadoTarjeta"	, ConstantesIntegridadDominio.acronimoEstadoActivo));
			dtCriteriaTarjetaTsi.setProjection(Property.forName("conveniosIngresoPacienteTsi.codigoPk"));
		// ---------------------------------------------------------------------------
			
		// Equivalente al OR. El codigoPk este en dtCriteriaTarjetaTno OR en dtCriteriaTarjetaTsi
		Disjunction disjunctionOR = Restrictions.disjunction();  
			disjunctionOR.add( Property.forName("conveniosIngresoPaciente.codigoPk").in(dtCriteriaTarjetaTno) );  
			disjunctionOR.add( Property.forName("conveniosIngresoPaciente.codigoPk").in(dtCriteriaTarjetaTsi) );  
		//----------------------------------------------------------------------------
		
			
		criteria.add(disjunctionOR);
		criteria.addOrder(Property.forName("convenios.nombre").asc());
		criteria.setResultTransformer( Transformers.aliasToBean(DtoSeccionConvenioPaciente.class));
		List<DtoSeccionConvenioPaciente> listaRetorno = criteria.list();
		
		return listaRetorno;
	}
	
	
	
	
	/**
	 * Retorna los ConveniosIngresoPaciente de un paciente según el contrato
	 * @param codPaciente
	 * @param contrato
	 * @return ConveniosIngresoPaciente
	 * 
	 * @author Cristhian Murillo
	 */
	public ConveniosIngresoPaciente obtenerConvenioIngresoPacientePorContrato (int codPaciente, int contrato)
	{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ConveniosIngresoPaciente.class, "conveniosIngresoPaciente");
		
		criteria.createAlias("conveniosIngresoPaciente.pacientes"	, "pacientes");
		criteria.createAlias("conveniosIngresoPaciente.contratos"	, "contratos");
		
		criteria.add(Restrictions.eq("pacientes.codigoPaciente"	, codPaciente));
		criteria.add(Restrictions.eq("contratos.codigo"			, contrato));
		
		return (ConveniosIngresoPaciente) criteria.uniqueResult();
		
	}
	

}
