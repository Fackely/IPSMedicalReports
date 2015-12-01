package com.servinte.axioma.orm.delegate.facturasVarias;

import java.util.ArrayList;

import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadTexto;

import com.princetonsa.dto.consultaExterna.DtoConceptoFacturaVaria;
import com.servinte.axioma.orm.ConceptosFacturasVarias;
import com.servinte.axioma.orm.FacturasVarias;
import com.servinte.axioma.orm.FacturasVariasHome;

/**
 * 
 * @author Edgar Carvajal Ruiz, Juan David Ramírez
 * 
 */
public class FacturasVariasDelegate extends FacturasVariasHome {

	@SuppressWarnings("unchecked")
	public ArrayList<DtoConceptoFacturaVaria> listarConceptosFacturasVarias() {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				ConceptosFacturasVarias.class);
		criteria.add(Restrictions.eq("activo", ConstantesBD.acronimoSi));
		criteria
				.add(Restrictions
						.eq(
								"tipoConcepto",
								ConstantesIntegridadDominio.acronimoTipoVentaTarjetaodontologica));

		criteria.setProjection(Projections.projectionList().add(
				Projections.property("consecutivo"), "consecutivo").add(
				Projections.property("codigo"), "codigo").add(
				Projections.property("descripcion"), "descripcion"));

		criteria.setResultTransformer(Transformers
				.aliasToBean(DtoConceptoFacturaVaria.class));

		return (ArrayList<DtoConceptoFacturaVaria>) criteria.list();
	}

	/**
	 * Método que se encarga de obtener el prefijo y el consecutivo
	 * de una factura varia específica teniendo el código de la factura.
	 * 
	 * @param codigoFacVar
	 * @return
	 */
	//TODO este metodo es temporal mientras se inicia el desarrollo completo para esta funcionalidad
	public String obtenerPrefijoConsecutivo(long codigoFacVar) {

		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				FacturasVarias.class);
		
		criteria.createAlias("instituciones", "institucion");
		
		criteria.add(Restrictions.eq("codigoFacVar", codigoFacVar));
		
//		criteria.setProjection(Projections.projectionList()
//				.add(Projections.property("institucion.prefFactura"),"prefFactura"));

		FacturasVarias facturaVarias = (FacturasVarias) criteria.setMaxResults(1).uniqueResult();
		
		String prefijoConsecutivo = "";
		
		if(facturaVarias!=null){
			
			prefijoConsecutivo = !UtilidadTexto.isEmpty(facturaVarias.getInstituciones().getPrefFactura()) ? 
					(facturaVarias.getInstituciones().getPrefFactura()+"-"+facturaVarias.getConsecutivo()+"") : facturaVarias.getConsecutivo()+"";
		}

		return prefijoConsecutivo;
	}
	
	/**
	 * Retorna ConceptosFacturasVarias
	 * 
	 * @return DtoConceptoFacturaVaria
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<DtoConceptoFacturaVaria> listarConceptosFacturasV ( ){
		

		Criteria criteria= sessionFactory.getCurrentSession().createCriteria(ConceptosFacturasVarias.class);
		criteria.addOrder(Property.forName("descripcion").asc());
		
		criteria.setProjection(Projections.projectionList().add(
				Projections.property("consecutivo"), "consecutivo").add(
				Projections.property("codigo"), "codigo").add(
				Projections.property("descripcion"), "descripcion"));

		criteria.setResultTransformer(Transformers
				.aliasToBean(DtoConceptoFacturaVaria.class));

		return (ArrayList<DtoConceptoFacturaVaria>) criteria.list();
	}
	
	/**
	 * Método que se encarga de obtener el código de una factura varia
	 * específica teniendo el consecutivo de la factura.
	 * 
	 * @param codigoFacVar
	 * @return
	 */
	public long obtenerCodigoFacturaVaria(long consecutivo) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				FacturasVarias.class);
		criteria.add(Restrictions.eq("consecutivo", consecutivo));
		return ((FacturasVarias) criteria.uniqueResult()).getCodigoFacVar();
	}
	
	/**
	 * Método que se encarga de obtener el consecutivo de una factura varia
	 * específica teniendo el código de la misma.
	 * 
	 * @param codigoFacVar
	 * @return long - consecutivo Factura Varia
	 */
	public long obtenerConsecutivoFacturaVaria(long codigoFacVar) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				FacturasVarias.class);
		criteria.add(Restrictions.eq("codigoFacVar", codigoFacVar));
		return ((FacturasVarias) criteria.uniqueResult()).getConsecutivo();
	}
}
