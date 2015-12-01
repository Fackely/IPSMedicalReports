
package com.servinte.axioma.orm.delegate.odontologia.presupuesto;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import util.ConstantesBD;

import com.servinte.axioma.orm.IncluPresuEncabezado;
import com.servinte.axioma.orm.InclusionesPresupuesto;
import com.servinte.axioma.orm.InclusionesPresupuestoHome;


/**
 * Clase que se encarga de manejar las transaccciones relacionadas
 * con la entidad {@link InclusionesPresupuesto}.
 * 
 * @author Jorge Armando Agudelo Quintero
 */
public class InclusionesPresupuestoDelegate extends InclusionesPresupuestoHome{

	
	/**
	 * Método que permite eliminar los registros
	 * de {@link InclusionesPresupuesto} asociados a un Encabezado de Inclusión
	 * 
	 * @param codigoIncluPresuEncabezado
	 * @return true en caso de eliminar los registros, false de lo contrario
	 * 
	 * @see IncluPresuEncabezado
	 */
	@SuppressWarnings("unchecked")
	public boolean eliminarInclusionesPorIncluPresuEncabezado (long codigoIncluPresuEncabezado){
		
		try {
			
			Criteria criteria = sessionFactory.getCurrentSession().createCriteria(InclusionesPresupuesto.class, "inclusiones")
			   .createAlias("incluPresuEncabezado", "encabezado");

			if(codigoIncluPresuEncabezado > ConstantesBD.codigoNuncaValidoLong)
			{
				criteria.add(Restrictions.eq("encabezado.codigoPk", codigoIncluPresuEncabezado));
			}

			List<InclusionesPresupuesto> listadoInclusiones =criteria.list();

			for (InclusionesPresupuesto inclusionPresupuesto : listadoInclusiones) {
				
				super.delete(inclusionPresupuesto);
			}

			return true;
			
		} catch (Exception e) {
		
			return false;
		}
	}
}
