package com.servinte.axioma.orm.delegate.facturacion.convenio;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import util.UtilidadFecha;

import com.princetonsa.dto.facturacion.DtoContrato;
import com.servinte.axioma.orm.Contratos;
import com.servinte.axioma.orm.ContratosHome;

/**
 * @author Cristhian Murillo
 *
 */
@SuppressWarnings("unchecked")
public class ContratosDelegate extends ContratosHome {
	

	
	/**
	 * Lista los contratos asociados al convenio
	 * 
	 * @param convenio
	 * @return ArrayList<Contratos>
	 */
	public ArrayList<Contratos> listarContratosPorConvenio(int convenio)
	{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Contratos.class, "contratos");
		criteria.createAlias("contratos.convenios"	, "convenios");
		criteria.add(Restrictions.eq("convenios.codigo", convenio));
		return (ArrayList<Contratos>)criteria.list();
	}

	
	/**
	 * Valida la vigencia de un contrato
	 * @return true en caso de estar vigente
	 * @author Juan David Ramírez
	 * @since 10 Septiembre 2010
	 */
	public boolean esVigenteContrato(DtoContrato contrato)
	{
		Date fechaActual=UtilidadFecha.getFechaActualTipoBD();
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Contratos.class, "contratos");
		criteria.add(Restrictions.eq("codigo", contrato.getCodigo()));
		criteria.add(Restrictions.le("fechaInicial", fechaActual));
		criteria.add(Restrictions.ge("fechaFinal", fechaActual));
		Object resultado=criteria.uniqueResult();
		return resultado!=null;
	}	
	
	
	
	@Override 
	public Contratos findById(int id) 
	{
		Contratos contrato = new Contratos();
		contrato = super.findById(id);
		contrato.getConvenios().getCodigo();
		
		return contrato;
	}
	
	
	
	/**
	 * Lista los contratos vigentes asociados al convenio
	 * 
	 * @param convenio
	 * @return ArrayList<Contratos>
	 */
	public ArrayList<Contratos> listarContratosVigentesPorConvenio(int convenio)
	{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Contratos.class, "contratos");
		criteria.createAlias("contratos.convenios"	, "convenios");
		criteria.add(Restrictions.eq("convenios.codigo", convenio));
		
		Date fechaActual = UtilidadFecha.getFechaActualTipoBD();
		criteria.add(Restrictions.ge("contratos.fechaFinal"		, fechaActual));
		criteria.add(Restrictions.le("contratos.fechaInicial"	, fechaActual));
		
		return (ArrayList<Contratos>)criteria.list();
	}
	
	/**
	 * Lista todos los contratos vigentes asociados al convenio
	 * 
	 * @param convenio
	 * @return ArrayList<Contratos>
	 * @author diecorqu
	 */
	public ArrayList<Contratos> listarTodosContratosVigentesPorConvenio(int convenio)
	{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Contratos.class, "contratos");
		criteria.createAlias("contratos.convenios"	, "convenios");
		criteria.add(Restrictions.eq("convenios.codigo", convenio));
		return (ArrayList<Contratos>)criteria.list();
	}
	
	
	/**
	 * Lista los Contratos del sistema
	 * 
	 * @author Cristhian Murillo
	 * @return ArrayList<Contratos>
	 */
	public ArrayList<Contratos> listarContratos(int codInstitucion)
	{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Contratos.class, "contratos");
		ArrayList<Contratos> listaContratos = new ArrayList<Contratos>();
		
		listaContratos = (ArrayList<Contratos>)criteria.list();
		
		for (Contratos contratos : listaContratos) 
		{
			if(contratos.getConvenios() != null){
				contratos.getConvenios().getCodigo();
			}
		}
		
		return listaContratos;
	}
	
	/**
	 * Lista los contratos asociados al convenio cuyo 
	 * campo de fecha sea menor a determinada fecha
	 * 
	 * @param fechaComparacion
	 * @param campoComparacion
	 * @param convenio
	 * @return ArrayList<Contratos>
	 */
	public ArrayList<Contratos> listarContratosPorConvenioPorFechaMenor(int convenio, 
									String campoComparacion, Date fechaComparacion)
	{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Contratos.class, "contratos");
		criteria.createAlias("contratos.convenios"	, "convenios");
		criteria.add(Restrictions.eq("convenios.codigo", convenio));
		criteria.add(Restrictions.le("contratos."+campoComparacion, fechaComparacion));
		return (ArrayList<Contratos>)criteria.list();
	}
	
	/**
	 * Lista los contratos a un convenio y que tengan una
	 * parametrización de presupuesto para un mes determinado. 
	 * 
	 * @author Ricardo Ruiz
	 * @param codigoConvenio
	 * @param mesAnio
	 * @return ArrayList<Convenios>
	*/
	public ArrayList<Contratos> listarContratosConParametrizacionPresupuestoPorConvenio(int codigoConvenio, Calendar mesAnio)
	{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Contratos.class, "contratos");
		
		criteria.createAlias("contratos.convenios", "convenio");
		criteria.createAlias("contratos.paramPresupuestosCaps", "presupuesto");
		criteria.createAlias("presupuesto.valorizacionPresCapGens", "valorizacion");
		
		criteria.add(Restrictions.eq("convenio.codigo" , codigoConvenio));
		criteria.add(Restrictions.eq("presupuesto.anioVigencia"	, String.valueOf(mesAnio.get(Calendar.YEAR))));
		criteria.add(Restrictions.eq("valorizacion.mes"	, mesAnio.get(Calendar.MONTH)));
		criteria.addOrder(Order.asc("contratos.numeroContrato"));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);	
		return (ArrayList<Contratos>)criteria.list();
	}
	
}
