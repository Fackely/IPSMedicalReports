
package com.servinte.axioma.orm.delegate.odontologia.presupuesto;

import java.math.BigDecimal;
import java.util.List;

import org.axioma.util.log.Log4JManager;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import com.servinte.axioma.orm.IncluServicioConvenio;
import com.servinte.axioma.orm.IncluServicioConvenioHome;


/**
 * Clase que se encarga de manejar las transaccciones relacionadas
 * con el objeto {@link IncluServicioConvenio}.
 * 
 * @author Jorge Armando Agudelo Quintero
 */
public class IncluServicioConvenioDelegate extends IncluServicioConvenioHome{

	

	/**
	 * Método que permite actualizar el porcentaje del descuento
	 * odontológico cuando ha sido autorizado, en los servicios asociados a 
	 * la contratación de la inclusión 
	 * 
	 * @param codigoIncluPresuEncabezado
	 * @param porcentajeDescuentoOdontologico 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public boolean actualizarPorcentajeDctoOdontologico(long codigoIncluPresuEncabezado, BigDecimal porcentajeDescuentoOdontologico) {
		
		try {
			
			Criteria criteria = sessionFactory.getCurrentSession().createCriteria(IncluServicioConvenio.class, "dsmc")
			   .createAlias("incluPresuConvenio", "convenio")
			   .createAlias("convenio.incluPresuEncabezado", "encabezado")
			   .add(Restrictions.eq("encabezado.codigoPk", codigoIncluPresuEncabezado));
			
		
			List<IncluServicioConvenio> listadoIncluServicioConvenio = (List<IncluServicioConvenio>) criteria.list();
			
			for (IncluServicioConvenio incluServicioConvenio : listadoIncluServicioConvenio) {
				
				incluServicioConvenio.setPorcentajeDctoOdontologico(porcentajeDescuentoOdontologico);
				super.attachDirty(incluServicioConvenio);
			}
		
			   
			return true;
			
		} catch (Exception e) {
			
			Log4JManager.error(e);
			return false;
		}
	}
}
