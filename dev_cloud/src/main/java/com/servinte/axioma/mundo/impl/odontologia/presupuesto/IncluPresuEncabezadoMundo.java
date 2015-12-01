
package com.servinte.axioma.mundo.impl.odontologia.presupuesto;

import java.util.List;

import com.servinte.axioma.dao.fabrica.odontologia.presupuesto.PresupuestoFabricaDAO;
import com.servinte.axioma.dao.interfaz.odontologia.presupuesto.IIncluPresuEncabezadoDAO;
import com.servinte.axioma.mundo.interfaz.odontologia.presupuesto.IIncluPresuEncabezadoMundo;
import com.servinte.axioma.orm.IncluPresuEncabezado;


/**
 * Esta clase se encarga de implementar la l&oacute;gica de negocio 
 * para la entidad {@link IncluPresuEncabezado}
 *
 * @autor Jorge Armando Agudelo Quintero
 *
 */

public class IncluPresuEncabezadoMundo  implements IIncluPresuEncabezadoMundo{

	IIncluPresuEncabezadoDAO incluPresuEncabezadoDAO;
	
	/**
	 * Constructor de la clase
	 */
	public IncluPresuEncabezadoMundo() {
		inicializar();
	}
	
	/**
	 * M&eacute;todo que se encarga de inicializar el objeto DAO encargado de manejar 
	 * la capa de integraci&oacute;n de los objetos {@link IncluPresuEncabezado}
	 */
	private void inicializar() {
		incluPresuEncabezadoDAO = PresupuestoFabricaDAO.crearIncluPresuEncabezadoDAO();
	}

	

	/* (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.odontologia.presupuesto.IIncluPresuEncabezadoMundo#cargarRegistrosInclusion(long)
	 */
	@Override
	public List<IncluPresuEncabezado> cargarRegistrosInclusion(long codigoPresupuesto) {
		
		return incluPresuEncabezadoDAO.cargarRegistrosInclusion(codigoPresupuesto);
	}

	/* (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.odontologia.presupuesto.IIncluPresuEncabezadoMundo#registrarInclusionPresupuestoEncabezado(com.servinte.axioma.orm.IncluPresuEncabezado)
	 */
	@Override
	public long registrarInclusionPresupuestoEncabezado(
			IncluPresuEncabezado incluPresuEncabezado) {
		
		return incluPresuEncabezadoDAO.registrarInclusionPresupuestoEncabezado(incluPresuEncabezado);
	}

	/* (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.odontologia.presupuesto.IIncluPresuEncabezadoMundo#cargarDetalleRegistroInclusion(long)
	 */
	@Override
	public IncluPresuEncabezado cargarDetalleRegistroInclusion(	long codigoIncluPresuEncabezado) {
		
		return incluPresuEncabezadoDAO.cargarDetalleRegistroInclusion(codigoIncluPresuEncabezado);
	}


	/* (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.odontologia.presupuesto.IIncluPresuEncabezadoMundo#actualizarIncluPresuEncabezado(com.servinte.axioma.orm.IncluPresuEncabezado)
	 */
	@Override
	public boolean actualizarIncluPresuEncabezado(
			IncluPresuEncabezado incluPresuEncabezado) {
		
		return incluPresuEncabezadoDAO.actualizarIncluPresuEncabezado(incluPresuEncabezado);
	}

	/* (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.odontologia.presupuesto.IIncluPresuEncabezadoMundo#cargarEncabezadoInclusion(long)
	 */
	@Override
	public IncluPresuEncabezado cargarEncabezadoInclusion(long codigoIncluPresuEncabezado) {
	
		return incluPresuEncabezadoDAO.cargarEncabezadoInclusion(codigoIncluPresuEncabezado);
	}

	/* (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.odontologia.presupuesto.IIncluPresuEncabezadoMundo#eliminarDetalleInclusiones(long)
	 */
	@Override
	public boolean eliminarDetalleInclusiones(
			long encabezadoInclusionPresupuesto)
	{
		return incluPresuEncabezadoDAO.eliminarDetalleInclusiones(encabezadoInclusionPresupuesto);
	}
}
