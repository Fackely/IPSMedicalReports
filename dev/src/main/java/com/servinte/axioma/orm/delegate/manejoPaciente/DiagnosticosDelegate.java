package com.servinte.axioma.orm.delegate.manejoPaciente;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;

import util.Utilidades;

import com.princetonsa.dto.manejoPaciente.DtoDiagnostico;
import com.servinte.axioma.orm.Diagnosticos;
import com.servinte.axioma.orm.DiagnosticosHome;

public class DiagnosticosDelegate extends DiagnosticosHome
{

	/**
	 * Este Método se encarga de consultar los diagnósticos
	 * registrados en el sistema
	 * 
	 * @return ArrayList<Diagnosticos>
	 * 
	 * @author, Fabián Becerra
	 *
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<Diagnosticos> consultarDiagnosticos(){
		
		ArrayList<Diagnosticos> lista =  (ArrayList<Diagnosticos>)sessionFactory.getCurrentSession()
		.createCriteria(Diagnosticos.class, "diagnostico")
		.addOrder(Order.asc("diagnostico.nombre"))
		.list();
	
		return lista;	
		
	}
	
	/**
	 * Este Método se encarga los acronimos de los diagnosticos
	 * 
	 * @return List<String>
	 * 
	 * @author, Fabián Becerra
	 *
	 */
	@SuppressWarnings("unchecked")
	public List<String> obtenerAcronimosDiagnosticosEnSistema(){
		
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Diagnosticos.class, "diagnostico")
		
		.setProjection(Projections.distinct(Projections.projectionList()
				.add(Projections.property("diagnostico.id.acronimo"))));
		
		return criteria.list();	
		
	}
	
	
	/**
	 * Realiza la búsqueda del último diagnostico de Evolución asociado a los parametros
	 * @param parametros
	 * @return DtoDiagnostico
	 *
	 * @autor Cristhian Murillo
	 */
	@SuppressWarnings("unchecked")
	public DtoDiagnostico ultimoDiagnosticoEvolucion(DtoDiagnostico parametros)
	{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Diagnosticos.class,"diagnosticos");
		
		criteria.createAlias("diagnosticos.evolDiagnosticoses"	, "evolDiagnosticoses"	, Criteria.INNER_JOIN);
		criteria.createAlias("evolDiagnosticoses.evoluciones"	, "evoluciones"			, Criteria.INNER_JOIN);
		criteria.createAlias("evoluciones.valoraciones"			, "valoraciones"		, Criteria.INNER_JOIN);
		criteria.createAlias("valoraciones.solicitudes"			, "solicitudes"			, Criteria.INNER_JOIN);
		criteria.createAlias("solicitudes.cuentas"				, "cuentas"				, Criteria.INNER_JOIN);

		//Parametro Cuenta 
		if(parametros.getIdCuenta() != null){
			criteria.add(Restrictions.eq("cuentas.id"						, parametros.getIdCuenta() ));
		}
		criteria.add(Restrictions.eq("evolDiagnosticoses.id.principal"		, true ));
		
		ProjectionList projectionList = Projections.projectionList();
			projectionList.add(Projections.property("diagnosticos.id.acronimo")		,"acronimoDiagnostico");
			projectionList.add(Projections.property("diagnosticos.id.tipoCie")		,"tipoCieDiagnosticoInt");
			projectionList.add(Projections.property("diagnosticos.nombre")			,"descripcionDiagnostico");
		criteria.setProjection(projectionList);
		
		criteria.addOrder(Order.desc("evoluciones.codigo"));
		
		criteria.setResultTransformer( Transformers.aliasToBean(DtoDiagnostico.class));
		
		ArrayList<DtoDiagnostico> listaUltimosDiagnostico = new ArrayList<DtoDiagnostico>();
		listaUltimosDiagnostico = (ArrayList<DtoDiagnostico>) criteria.list();
		
		/* Por los joing que se tienen que hacer obligatoriamente a otras tablas 
		 * el resultado para una sola cuenta así sea el mismo lelga varias veces. */
		DtoDiagnostico ultimosDiagnosticoEvolucion = new DtoDiagnostico();
		if(!Utilidades.isEmpty(listaUltimosDiagnostico))
		{
			ultimosDiagnosticoEvolucion = listaUltimosDiagnostico.get(0);
			ultimosDiagnosticoEvolucion.organizarNombreCompletoDiagnostico();
		}
		
		return ultimosDiagnosticoEvolucion;
	}
	
	
	
	
	/**
	 * Realiza la búsqueda del último diagnostico de Valoración asociado a los parametros
	 * @param parametros
	 * @return DtoDiagnostico
	 *
	 * @autor Cristhian Murillo
	 */
	@SuppressWarnings("unchecked")
	public DtoDiagnostico ultimoDiagnosticoValoracion(DtoDiagnostico parametros)
	{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Diagnosticos.class,"diagnosticos");
		
		criteria.createAlias("diagnosticos.valDiagnosticoses"	, "valDiagnosticoses"	, Criteria.INNER_JOIN);
		criteria.createAlias("valDiagnosticoses.valoraciones"	, "valoraciones"		, Criteria.INNER_JOIN);
		criteria.createAlias("valoraciones.solicitudes"			, "solicitudes"			, Criteria.INNER_JOIN);
		criteria.createAlias("solicitudes.cuentas"				, "cuentas"				, Criteria.INNER_JOIN);

		//Parametro Cuenta 
		if(parametros.getIdCuenta() != null){
			criteria.add(Restrictions.eq("cuentas.id"						, parametros.getIdCuenta() ));
		}
		criteria.add(Restrictions.eq("valDiagnosticoses.id.principal"		, true ));
		
		ProjectionList projectionList = Projections.projectionList();
			projectionList.add(Projections.property("diagnosticos.id.acronimo")		,"acronimoDiagnostico");
			projectionList.add(Projections.property("diagnosticos.id.tipoCie")		,"tipoCieDiagnosticoInt");
			projectionList.add(Projections.property("diagnosticos.nombre")			,"descripcionDiagnostico");
		criteria.setProjection(projectionList);
		
		criteria.addOrder(Order.desc("solicitudes.numeroSolicitud"));
		
		criteria.setResultTransformer( Transformers.aliasToBean(DtoDiagnostico.class));
		
		ArrayList<DtoDiagnostico> listaUltimosDiagnostico = new ArrayList<DtoDiagnostico>();
		listaUltimosDiagnostico = (ArrayList<DtoDiagnostico>) criteria.list();
		
		/* Por los joing que se tienen que hacer obligatoriamente a otras tablas 
		 * el resultado para una sola cuenta así sea el mismo lelga varias veces. */
		DtoDiagnostico ultimosDiagnosticoValoracion = new DtoDiagnostico();
		if(!Utilidades.isEmpty(listaUltimosDiagnostico))
		{
			ultimosDiagnosticoValoracion = listaUltimosDiagnostico.get(0);
			ultimosDiagnosticoValoracion.organizarNombreCompletoDiagnostico();
		}
		
		return ultimosDiagnosticoValoracion;
	}
	
	
}
