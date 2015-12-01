package com.servinte.axioma.orm.delegate.tesoreria;

import java.util.ArrayList;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;

import com.servinte.axioma.orm.HistConcEgrXusuXcatencion;
import com.servinte.axioma.orm.HistConcEgrXusuXcatencionHome;


/**
 * @author Cristhian Murillo
 *
 * Clase que contiene logica del negocio sobre el modelo 
 */
public class HistConcEgrXusuXcatencionDelegate extends HistConcEgrXusuXcatencionHome{
	
	
	
	/**
	 * 	Lista todos por Institucion
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<HistConcEgrXusuXcatencion> listarTodosPorInstitucion(int institucion){
		
		return (ArrayList<HistConcEgrXusuXcatencion>) sessionFactory.getCurrentSession()
				.createCriteria(HistConcEgrXusuXcatencion.class)
				.addOrder(Order.desc("fechaModifica"))
				.createCriteria("centroAtencion")
					.add(Expression.eq("instituciones.codigo", institucion))
				
				.list();
	}
	
}
