package com.servinte.axioma.orm.delegate.odontologia.contrato;

import java.util.ArrayList;

import org.hibernate.Criteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;

import com.princetonsa.dto.odontologia.DtoOtroSi;
import com.servinte.axioma.orm.OtrosSi;
import com.servinte.axioma.orm.OtrosSiHome;


/**
 * @author Cristhian Murillo
 *
 * Clase que contiene logica del negocio sobre el modelo 
 */
@SuppressWarnings({"unchecked"})
public class OtrosSiDelegate extends OtrosSiHome {

	
	/**
	 * Retorna el último Otro Si (Inclusion/Esclusion) del oresupuesto enviado.
	 * EL resultado es organizado por fecha y hora de tal manera que el objeto
	 * de la posición cero (0) de la lista sería el último ingresado.
	 * 
	 * @autor Cristhian Murillo
	 * @param codPresoOdonto
	 * @return OtrosSi
	 */
	public ArrayList<OtrosSi> obtenerOtroSiOrdenadosMayorMenor(long codPresoOdonto){
		
		return (ArrayList<OtrosSi>) sessionFactory.getCurrentSession()
			
			.createCriteria(OtrosSi.class, "otrosi")
			
			.createAlias("otrosi.presupuestoOdontologico" , "presu_odonto")
			
			.add(Restrictions.eq("presu_odonto.codigoPk", codPresoOdonto))
			
			.addOrder(Order.desc("fechaGeneracion"))
		
			.addOrder(Order.desc("horaGeneracion"))
			
			.list();
	}
	
	
	
	@Override
	public void attachDirty(OtrosSi instance) {
		super.attachDirty(instance);
	}
	
	
	
	/**
	 * Consulta los números de otro si para inclusiones o exclusiones de un presupuesto
	 * @param codPresoOdonto
	 * @return ArrayList<DtoOtroSi>
	 * 
	 * @autor Cristhian Murillo
	 */
	public ArrayList<DtoOtroSi> obtenerOtrosSiporPresupuesto(long codPresoOdonto)
	{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(OtrosSi.class, "otrosSi");
		
		criteria.createAlias("otrosSi.incluPresuEncabezados"					, "incluPresuEncabezados"			, Criteria.LEFT_JOIN);
		criteria.createAlias("incluPresuEncabezados.presupuestoOdontologico"	, "inclu_presupuestoOdontologico"	, Criteria.LEFT_JOIN);
		
		criteria.createAlias("otrosSi.excluPresuEncabezados"					, "excluPresuEncabezados"			, Criteria.LEFT_JOIN);
		criteria.createAlias("excluPresuEncabezados.presupuestoOdontologico"	, "exclu_presupuestoOdontologico"	, Criteria.LEFT_JOIN);
		
		Disjunction disjunctionOR = Restrictions.disjunction();  
			disjunctionOR.add( Restrictions.eq("inclu_presupuestoOdontologico.codigoPk", codPresoOdonto));  
			disjunctionOR.add( Restrictions.eq("exclu_presupuestoOdontologico.codigoPk", codPresoOdonto));
		criteria.add(disjunctionOR);
		
		ProjectionList projectionList = Projections.projectionList();
			projectionList.add(Projections.property("otrosSi.codigoPk")		,"otroSi");
		criteria.setProjection(projectionList);	
		
		criteria.setResultTransformer(Transformers.aliasToBean(DtoOtroSi.class));
		
		ArrayList<DtoOtroSi> listaResultado = (ArrayList<DtoOtroSi>) criteria.list();
			
		return listaResultado;
	}
	
	
}
