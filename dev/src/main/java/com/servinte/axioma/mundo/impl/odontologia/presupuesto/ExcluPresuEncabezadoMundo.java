
package com.servinte.axioma.mundo.impl.odontologia.presupuesto;

import java.util.List;

import com.servinte.axioma.dao.fabrica.odontologia.presupuesto.PresupuestoFabricaDAO;
import com.servinte.axioma.dao.interfaz.odontologia.presupuesto.IExcluPresuEncabezadoDAO;
import com.servinte.axioma.mundo.interfaz.odontologia.presupuesto.IExcluPresuEncabezadoMundo;
import com.servinte.axioma.orm.ExcluPresuEncabezado;


/**
 * Esta clase se encarga de implementar la l&oacute;gica de negocio 
 * para la entidad {@link ExcluPresuEncabezado}
 *
 * @autor Jorge Armando Agudelo Quintero
 * @since  14/09/2010
 *
 */

public class ExcluPresuEncabezadoMundo  implements IExcluPresuEncabezadoMundo{

	IExcluPresuEncabezadoDAO excluPresuEncabezadoDAO;
	
	/**
	 * Constructor de la clase
	 */
	public ExcluPresuEncabezadoMundo() {
		inicializar();
	}
	
	/**
	 * M&eacute;todo que se encarga de inicializar el objeto DAO encargado de manejar 
	 * la capa de integraci&oacute;n de los objetos {@link ExcluPresuEncabezado}
	 */
	private void inicializar() {
		excluPresuEncabezadoDAO = PresupuestoFabricaDAO.crearExcluPresuEncabezadoDAO();
	}

	/* (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.odontologia.presupuesto.IExcluPresuEncabezadoMundo#registrarExclusionPresupuestoEncabezado(com.servinte.axioma.orm.ExcluPresuEncabezado)
	 */
	@Override
	public long registrarExclusionPresupuestoEncabezado(ExcluPresuEncabezado excluPresuEncabezado) {
		
		return excluPresuEncabezadoDAO.registrarExclusionPresupuestoEncabezado(excluPresuEncabezado);
	}

	/* (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.odontologia.presupuesto.IExcluPresuEncabezadoMundo#cargarRegistrosExclusion(long)
	 */
	@Override
	public List<ExcluPresuEncabezado> cargarRegistrosExclusion(
			long codigoPresupuesto) {

		return excluPresuEncabezadoDAO.cargarRegistrosExclusion(codigoPresupuesto);
	}
}
