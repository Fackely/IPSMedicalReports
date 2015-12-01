package com.servinte.axioma.orm.delegate.tesoreria;

import java.util.ArrayList;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import util.ConstantesBD;
import com.servinte.axioma.orm.ConceptosIngTesoreria;
import com.servinte.axioma.orm.ConceptosIngTesoreriaHome;


/**
 * Esta clase se encarga de manejar las transacciones
 * relacionadas con la entidad ConceptosIngTesoreria
 * @author Diana Carolina G
 *
 */

@SuppressWarnings("unchecked")
public class ConceptosIngTesoreriaDelegate extends ConceptosIngTesoreriaHome{
	
	/**
	 * 
	 * Lista los conceptos de ingreso de tesoreria 
	 * Tipo Ingreso = Anticipos Convenios Odontol&oacute;gicos
	 * @return ArrayList<ConceptosIngTesoreria>
	 * @author Diana Carolina G
	 * 
	 */
	
	
	public ArrayList<ConceptosIngTesoreria> obtenerConceptosTipoIngAnticiposConvOdont(){
		
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ConceptosIngTesoreria.class, "conceptosIngTesoreria");
		
		criteria.createAlias("conceptosIngTesoreria.tipoIngTesoreria", "tipoIngTesoreria");

		criteria.add(Restrictions.eq("tipoIngTesoreria.codigo", ConstantesBD.codigoTipoIngresoTesoreriaAnticipoConvenioOdon));
		criteria.addOrder(Order.asc("tipoIngTesoreria.descripcion"));
		
		ArrayList<ConceptosIngTesoreria> listaConceptos=(ArrayList<ConceptosIngTesoreria>)criteria.list();
		
		return listaConceptos; 
		
	} 
	
	
	/*public static void main(String[] args) {
		ConceptosIngTesoreriaDelegate x = new ConceptosIngTesoreriaDelegate();
		ArrayList<ConceptosIngTesoreria> listaConceptos=x.obtenerConceptosTipoIngAnticiposConvOdont();
		
	}*/
	
	


}
