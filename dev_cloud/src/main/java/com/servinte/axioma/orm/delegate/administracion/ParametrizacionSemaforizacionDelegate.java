package com.servinte.axioma.orm.delegate.administracion;

import java.util.ArrayList;

import org.axioma.util.log.Log4JManager;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import com.princetonsa.mundo.UsuarioBasico;
import com.servinte.axioma.orm.Instituciones;
import com.servinte.axioma.orm.ParametrizacionSemaforizacion;
import com.servinte.axioma.orm.ParametrizacionSemaforizacionHome;

public class ParametrizacionSemaforizacionDelegate extends
		ParametrizacionSemaforizacionHome {
	
	

	/**
	 * @param reporte
	 * @return lista con registros de la base de datos 
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<com.servinte.axioma.orm.ParametrizacionSemaforizacion> consultarParametrizacionSemaforizaciones(
			String reporte) {


		Criteria criteria = getSessionFactory().getCurrentSession()
				.createCriteria(ParametrizacionSemaforizacion.class,
						"parametrizacionSemaforizacion");
		
		//SE CONSULTA POR RANGO INICIAL ASCENDENTEMENTE
		criteria.addOrder(Order.asc("parametrizacionSemaforizacion.rangoInicial"));

		ArrayList<com.servinte.axioma.orm.ParametrizacionSemaforizacion> res = (ArrayList<ParametrizacionSemaforizacion>) criteria
				.add(Restrictions.eq(
						"parametrizacionSemaforizacion.tipoReporte", reporte))
				.list();

		return res;
	}

	

	/**
	 * Se eliminan parametrizaciondes de la base de datos
	 * @param elementoAEliminar
	 */
	public void eliminarParametrizacion(ParametrizacionSemaforizacion elementoAEliminar){
			super.delete(elementoAEliminar);
	}
	
	
	
	/**
	 * @param id
	 * @return Parametrizacion buscada por id 
	 */
	public ParametrizacionSemaforizacion buscarPorID(Number id){
		return super.findById((Long) id);
		
	}
	
	
	
	/**
	 * Guarda y actualiza los registros en la BD
	 * @param usuario
	 * @param lista
	 * @param tipoReporte
	 */
	public void guardarActualizar(ParametrizacionSemaforizacion elementoAGuardar){
			super.attachDirty(elementoAGuardar);
	}
	
	

	
	
}
