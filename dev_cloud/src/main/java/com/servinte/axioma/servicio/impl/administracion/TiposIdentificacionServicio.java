package com.servinte.axioma.servicio.impl.administracion;

import java.util.ArrayList;

import com.servinte.axioma.mundo.fabrica.AdministracionFabricaMundo;
import com.servinte.axioma.mundo.interfaz.administracion.ITiposIdentificacionMundo;
import com.servinte.axioma.orm.TiposIdentificacion;
import com.servinte.axioma.servicio.interfaz.tesoreria.ITiposIdentificacionServicio;

/**
 * Implementaci&oacute;n de la interfaz {@link ITiposIdentificacionServicio}.
 * 
 * @author Cristhian Murillo
 * @see TiposIdentificacionMundo.
 */

public class TiposIdentificacionServicio implements ITiposIdentificacionServicio{

	private ITiposIdentificacionMundo tiposIdentificacionMundo;
	
	public TiposIdentificacionServicio() {
		
		tiposIdentificacionMundo =  AdministracionFabricaMundo.crearTiposIdentificacionMundo();
	}


	/* (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.administracion.ITiposIdentificacionDAO#obtenerTipoIdentificacionPorAcronimo(java.lang.String)
	 */
	@Override
	public TiposIdentificacion obtenerTipoIdentificacionPorAcronimo(String acronimo) {
	
		return tiposIdentificacionMundo.obtenerTipoIdentificacionPorAcronimo(acronimo);
	}


	/* (non-Javadoc)
	 * @see com.servinte.axioma.servicio.interfaz.tesoreria.ITiposIdentificacionServicio#listarTiposIdentificacionPorTipo(java.lang.String[])
	 */
	@Override
	public ArrayList<TiposIdentificacion> listarTiposIdentificacionPorTipo(String[] listaIntegridadDominio) {
		
		return tiposIdentificacionMundo.listarTiposIdentificacionPorTipo(listaIntegridadDominio);
	}
	
	
	/**
	 * Lista todos
	 */
	@Override
	public ArrayList<TiposIdentificacion> listarTodos()
	{
		return tiposIdentificacionMundo.listarTodos();
	}

}
