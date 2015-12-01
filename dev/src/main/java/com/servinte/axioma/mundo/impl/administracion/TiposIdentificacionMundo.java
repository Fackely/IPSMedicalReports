package com.servinte.axioma.mundo.impl.administracion;

import java.util.ArrayList;

import com.servinte.axioma.dao.fabrica.AdministracionFabricaDAO;
import com.servinte.axioma.dao.interfaz.administracion.ITiposIdentificacionDAO;
import com.servinte.axioma.mundo.interfaz.administracion.ITiposIdentificacionMundo;
import com.servinte.axioma.orm.TiposIdentificacion;


/**
 * Contiene la l&oacute;gica de Negocio
 * 
 * @author Cristhian Murillo
 * @see ITiposIdentificacionMundo
 */

public class TiposIdentificacionMundo implements ITiposIdentificacionMundo{

	
	private ITiposIdentificacionDAO tiposIdentificacionDAO;

	
	public TiposIdentificacionMundo() {
		inicializar();
	}

	private void inicializar() {
		tiposIdentificacionDAO = AdministracionFabricaDAO.crearTiposIdentificacionDAO();
	}

	
	@Override
	public ArrayList<TiposIdentificacion> listarTiposIdentificacionPorTipo(
			String[] listaIntegridadDominio) 
	{
		return tiposIdentificacionDAO.listarTiposIdentificacionPorTipo(listaIntegridadDominio);
	}

	/* (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.administracion.ITiposIdentificacionMundo#obtenerTipoIdentificacionPorAcronimo(java.lang.String)
	 */
	@Override
	public TiposIdentificacion obtenerTipoIdentificacionPorAcronimo(String acronimo) {
	
		return tiposIdentificacionDAO.obtenerTipoIdentificacionPorAcronimo(acronimo);
	}
	
	
	/**
	 * Lista todos
	 */
	@Override
	public ArrayList<TiposIdentificacion> listarTodos(){
		return tiposIdentificacionDAO.listarTodos();
	}
	
}
