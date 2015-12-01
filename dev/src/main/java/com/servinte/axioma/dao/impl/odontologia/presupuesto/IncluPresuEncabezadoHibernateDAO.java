
package com.servinte.axioma.dao.impl.odontologia.presupuesto;

import java.util.List;

import com.servinte.axioma.dao.interfaz.odontologia.presupuesto.IIncluPresuEncabezadoDAO;
import com.servinte.axioma.orm.IncluPresuEncabezado;
import com.servinte.axioma.orm.delegate.odontologia.presupuesto.IncluPresuEncabezadoDelegate;

/**
 * Implementaci&oacute;n de la interfaz 
 * {@link IIncluPresuEncabezadoDAO}.
 * 
 * @author Jorge Armando Agudelo Quintero
 * @see IncluPresuEncabezadoDelegate
 */

public class IncluPresuEncabezadoHibernateDAO implements IIncluPresuEncabezadoDAO {

	
	IncluPresuEncabezadoDelegate incluPresuEncabezadoDelegate;
	
	
	/**
	 * Constructor de la Clase
	 */
	public IncluPresuEncabezadoHibernateDAO() {
		
		incluPresuEncabezadoDelegate = new IncluPresuEncabezadoDelegate();
	}


	/* (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.odontologia.presupuesto.IIncluPresuEncabezadoDAO#cargarRegistrosInclusion(long)
	 */
	@Override
	public List<IncluPresuEncabezado> cargarRegistrosInclusion(long codigoPresupuesto) {
		
		return incluPresuEncabezadoDelegate.cargarRegistrosInclusion(codigoPresupuesto);
	}


	/* (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.odontologia.presupuesto.IIncluPresuEncabezadoDAO#registrarInclusionPresupuestoEncabezado(com.servinte.axioma.orm.IncluPresuEncabezado)
	 */
	@Override
	public long registrarInclusionPresupuestoEncabezado(IncluPresuEncabezado incluPresuEncabezado){
		
		return incluPresuEncabezadoDelegate.registrarInclusionPresupuestoEncabezado(incluPresuEncabezado);
	}


	/* (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.odontologia.presupuesto.IIncluPresuEncabezadoDAO#cargarDetalleRegistroInclusion(long)
	 */
	@Override
	public IncluPresuEncabezado cargarDetalleRegistroInclusion(long codigoIncluPresuEncabezado) {
		
		return incluPresuEncabezadoDelegate.cargarDetalleRegistroInclusion(codigoIncluPresuEncabezado);
	}

	/* (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.odontologia.presupuesto.IIncluPresuEncabezadoDAO#actualizarIncluPresuEncabezado(com.servinte.axioma.orm.IncluPresuEncabezado)
	 */
	@Override
	public boolean actualizarIncluPresuEncabezado(
			IncluPresuEncabezado incluPresuEncabezado) {
	
		return incluPresuEncabezadoDelegate.actualizarIncluPresuEncabezado(incluPresuEncabezado);
	}


	/* (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.odontologia.presupuesto.IIncluPresuEncabezadoDAO#cargarEncabezadoInclusion(long)
	 */
	@Override
	public IncluPresuEncabezado cargarEncabezadoInclusion(long codigoIncluPresuEncabezado) {
	
		return incluPresuEncabezadoDelegate.cargarEncabezadoInclusion(codigoIncluPresuEncabezado);
	}


	/* (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.odontologia.presupuesto.IIncluPresuEncabezadoDAO#eliminarDetalleInclusiones(long)
	 */
	@Override
	public boolean eliminarDetalleInclusiones(long encabezadoInclusionPresupuesto)
	{
		return incluPresuEncabezadoDelegate.eliminarDetalleInclusiones(encabezadoInclusionPresupuesto);
	}

}
