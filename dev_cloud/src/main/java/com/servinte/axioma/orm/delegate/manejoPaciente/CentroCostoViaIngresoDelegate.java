package com.servinte.axioma.orm.delegate.manejoPaciente;

import java.util.ArrayList;

import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;

import com.princetonsa.dto.manejoPaciente.DtoCentroCostoViaIngreso;
import com.servinte.axioma.orm.CentroCostoViaIngreso;
import com.servinte.axioma.orm.CentroCostoViaIngresoHome;
/**
 * Esta clase se encarga de manejar las transaccciones relacionadas
 * con el objeto  {@link CentroCostoViaIngreso}
 * @author Diana Carolina G
 */
public class CentroCostoViaIngresoDelegate extends CentroCostoViaIngresoHome {
	
	/**
	 * M&eacute;todo encargado de obtener los centros de costo por 
	 * v&iacute;a de ingreso existentes en el sistema
	 * @author Diana Carolina G
	 * @param institucion
	 * @return ArrayList<DtoCentroCostoViaIngreso>
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<DtoCentroCostoViaIngreso> obtenerCentrosCostoViaIngreso(int institucion){
		
		Criteria criteria = sessionFactory.getCurrentSession()
		.createCriteria(CentroCostoViaIngreso.class,"centroCostoViaIngreso")
		.createAlias("centroCostoViaIngreso.centrosCosto", "centrosCosto")
		.createAlias("centroCostoViaIngreso.viasIngreso", "viasIngreso")
		.createAlias("centroCostoViaIngreso.tiposPaciente", "tiposPaciente")
		.createAlias("centroCostoViaIngreso.instituciones", "instituciones");
		
		criteria.add(Restrictions.eq("instituciones.codigo", institucion));
		
		criteria.setProjection(Projections.distinct(Projections.projectionList()
				.add(Projections.property("centroCostoViaIngreso.codigo"),"codigoCentroCostoViaIngreso")
				.add(Projections.property("centrosCosto.codigo"), "codigoCentroCosto")
				.add(Projections.property("centrosCosto.nombre"),"nombreCentroCosto")
				.add(Projections.property("viasIngreso.codigo"),"codigoViaIngreso")
				.add(Projections.property("viasIngreso.nombre"),"nombreViaIngreso")
				.add(Projections.property("tiposPaciente.acronimo"),"acronimoTipoPaciente")));
		
		criteria.setResultTransformer(Transformers.aliasToBean(DtoCentroCostoViaIngreso.class));
		ArrayList<DtoCentroCostoViaIngreso> lista = (ArrayList<DtoCentroCostoViaIngreso>)criteria.list();
		return lista;
	}
	
	
	/**
	 * M&eacute;todo encargado de obtener los centros de costo por 
	 * v&iacute;a de ingreso
	 * @author Camilo Gomez
	 * @param viaIngreso
	 * @return ArrayList<DtoCentroCostoViaIngreso>
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<DtoCentroCostoViaIngreso> listarCentrosCostoXViaIngreso(int viaIngreso){
		
		Criteria criteria = sessionFactory.getCurrentSession()
		.createCriteria(CentroCostoViaIngreso.class,"centroCostoViaIngreso")
		.createAlias("centroCostoViaIngreso.centrosCosto", "centrosCosto")
		.createAlias("centroCostoViaIngreso.viasIngreso", "viasIngreso")
		.createAlias("centroCostoViaIngreso.tiposPaciente", "tiposPaciente")
		.createAlias("centroCostoViaIngreso.instituciones", "instituciones");
		
		criteria.add(Restrictions.eq("viasIngreso.codigo", viaIngreso));
		
		criteria.setProjection(Projections.distinct(Projections.projectionList()
				.add(Projections.property("centroCostoViaIngreso.codigo"),"codigoCentroCostoViaIngreso")
				.add(Projections.property("centrosCosto.codigo"), "codigoCentroCosto")
				.add(Projections.property("centrosCosto.nombre"),"nombreCentroCosto")
				.add(Projections.property("viasIngreso.codigo"),"codigoViaIngreso")
				.add(Projections.property("viasIngreso.nombre"),"nombreViaIngreso")
				.add(Projections.property("tiposPaciente.acronimo"),"acronimoTipoPaciente")));
		
		criteria.setResultTransformer(Transformers.aliasToBean(DtoCentroCostoViaIngreso.class));
		ArrayList<DtoCentroCostoViaIngreso> lista = (ArrayList<DtoCentroCostoViaIngreso>)criteria.list();
		return lista;
	}
	
	
	public static void main(String[] args) {
	CentroCostoViaIngresoDelegate x = new CentroCostoViaIngresoDelegate();
	
	int institucion=2;
	ArrayList<DtoCentroCostoViaIngreso> listaRC =x.obtenerCentrosCostoViaIngreso(institucion);
		
	} 
	
	
	
	

}
