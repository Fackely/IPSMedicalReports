package com.servinte.axioma.orm.delegate.capitacion;

import java.util.ArrayList;

import org.axioma.util.log.Log4JManager;
import org.hibernate.Criteria;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;

import com.servinte.axioma.orm.LogDetalleParamPresup;
import com.servinte.axioma.orm.LogDetalleParamPresupHome;

/**
 * Clase encargada de ejecutar las transacciones del objeto 
 * {@link LogDetalleParamPresup}
 * @author diecorqu
 *
 */
@SuppressWarnings("unchecked")
public class LogDetalleParametrizacionPresupuestoDelegate extends LogDetalleParamPresupHome{

	/**
	 * Este m�todo retorna el log detalle de la parametrizaci�n por codigo
	 * 
	 * @param codigo Log
	 * @return LogParamPresupuestoCap
	 */
	public LogDetalleParamPresup findById(long codigoLogDetalle) {
		return super.findById(codigoLogDetalle);
	}	

	/**
	 * Este m�todo guarda el log detalle para la parametrizaci�n del presupuesto
	 * 
	 * @param Log Detalle Parametrizacion del presupuesto
	 * @return boolean con el resultado de la operaci�n de guardado
	 */
	public boolean guardarLogDetalleParametrizacionPresupuesto(
			LogDetalleParamPresup logDetalleParametrizacion) {
		boolean save = false;
		try {
			super.persist(logDetalleParametrizacion);
			save = true;
		} catch (Exception e) {
			save = false;
			Log4JManager.error("No se pudo almacenar el log del detalle para la parametrizacion del presupuesto ",e);
		}		
		return save;
	}	
	
	/**
	 * Este m�todo verifica si existe log detalle para la parametrizaci�n del presupuesto
	 * de una parametrizaci�n dada
	 * 
	 * @param codigo de la parametrizaci�n
	 * @return boolean con el resultado de la operaci�n
	 */
	public boolean existeLogDetalleParametrizacionPresupuesto(long codLogParametrizacion) {
		Criteria criteria = sessionFactory.getCurrentSession()
			.createCriteria(LogDetalleParamPresup.class,"logDetalle");
		criteria.createAlias("logDetalle.logParamPresupuestoCap", "logGeneral").
			add(Restrictions.eq("logGeneral.codigo", codLogParametrizacion));
		return (criteria.setMaxResults(1).list() == null || criteria.setMaxResults(1).list().size() == 0) ? false : true;
	}
	
	/**
	 * Este m�todo obtiene el Log detallado para la parametrizaci�n del presupuesto
	 * 
	 * 
	 * @param codigo del log general
	 * @return boolean con el resultado de la operaci�n
	 */
	public ArrayList<LogDetalleParamPresup> obtenerLogDetalladoParametrizacionPresupuesto(
			long codigoLogGeneral) {
		Criteria criteria = sessionFactory.getCurrentSession()
		.createCriteria(LogDetalleParamPresup.class,"logDetalle");
		criteria.createAlias("logDetalle.logParamPresupuestoCap", "logGeneral").
			add(Restrictions.eq("logGeneral.codigo", codigoLogGeneral)).
			addOrder(Property.forName("nivelAtencion").asc()).
			addOrder(Property.forName("nivelModificacion").asc());

		return (ArrayList<LogDetalleParamPresup>)criteria.list();
	}
	
	/**
	 * Este m�todo modifica el Log para la parametrizaci�n del presupuesto
	 * 
	 * @param LogParamPresupuestoCap Log Parametrizaci�n Presupuesto
	 * @return LogParamPresupuestoCap modificado
	 */
	public LogDetalleParamPresup modificarLogDetalleParametrizacionPresupuesto(
			LogDetalleParamPresup logDetalle) {
		return super.merge(logDetalle);
	}	
	
	/**
	 * Este m�todo elimina el Log detalle para la parametrizaci�n del presupuesto
	 * 
	 * @param c�digo Log detalle a eliminar
	 * @return boolean con el resultado de la operaci�n de eliminado
	 */
	public void eliminarLogDetalleParametrizacion(LogDetalleParamPresup logDetalle) {
		super.delete(logDetalle);
	}	
}
