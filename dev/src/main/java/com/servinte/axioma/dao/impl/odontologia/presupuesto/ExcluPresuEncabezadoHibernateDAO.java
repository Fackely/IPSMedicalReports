
package com.servinte.axioma.dao.impl.odontologia.presupuesto;

import java.util.List;

import com.servinte.axioma.dao.interfaz.odontologia.presupuesto.IExcluPresuEncabezadoDAO;
import com.servinte.axioma.orm.ExcluPresuEncabezado;
import com.servinte.axioma.orm.delegate.odontologia.presupuesto.ExcluPresuEncabezadoDelegate;

/**
 * Implementaci&oacute;n de la interfaz 
 * {@link IExcluPresuEncabezadoDAO}.
 * 
 * @author Jorge Armando Agudelo Quintero
 * @see ExcluPresuEncabezadoDelegate
 */

public class ExcluPresuEncabezadoHibernateDAO implements IExcluPresuEncabezadoDAO {

	
	ExcluPresuEncabezadoDelegate excluPresuEncabezadoDelegate;
	
	
	/**
	 * Constructor de la Clase
	 */
	public ExcluPresuEncabezadoHibernateDAO() {
		
		excluPresuEncabezadoDelegate = new ExcluPresuEncabezadoDelegate();
	}

	
	/* (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.odontologia.presupuesto.IExcluPresuEncabezadoDAO#registrarExclusionPresupuestoEncabezado(com.servinte.axioma.dao.interfaz.odontologia.presupuesto.ExcluPresuEncabezado)
	 */
	@Override
	public long registrarExclusionPresupuestoEncabezado(ExcluPresuEncabezado excluPresuEncabezado) {
	
		return excluPresuEncabezadoDelegate.registrarExclusionPresupuestoEncabezado(excluPresuEncabezado);
	}


	/* (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.odontologia.presupuesto.IExcluPresuEncabezadoDAO#cargarRegistrosExclusion(long)
	 */
	@Override
	public List<ExcluPresuEncabezado> cargarRegistrosExclusion(long codigoPresupuesto) {
		
		return excluPresuEncabezadoDelegate.cargarRegistrosExclusion(codigoPresupuesto);
	}


}
