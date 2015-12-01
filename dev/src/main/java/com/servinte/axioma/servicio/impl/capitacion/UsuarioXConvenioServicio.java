package com.servinte.axioma.servicio.impl.capitacion;

import java.util.ArrayList;

import com.servinte.axioma.mundo.fabrica.capitacion.CapitacionFabricaMundo;
import com.servinte.axioma.mundo.interfaz.capitacion.IUsuarioXConvenioMundo;
import com.servinte.axioma.orm.UsuarioXConvenio;
import com.servinte.axioma.servicio.interfaz.capitacion.IUsuarioXConvenioServicio;

/**
 * Esta clase se encarga de UsuariosCapitados
 * @author Cristhian Murillo
 */
public class UsuarioXConvenioServicio implements IUsuarioXConvenioServicio
{

	IUsuarioXConvenioMundo mundo;
	
	/**
	 * Método constructor de la clase
	 * @author Cristhian Murillo
	 */
	public UsuarioXConvenioServicio(){
		mundo = CapitacionFabricaMundo.crearUsuarioXConvenioMundo();

	}

	/* (non-Javadoc)
	 * @see com.servinte.axioma.servicio.interfaz.capitacion.IUsuarioXConvenioServicio#attachDirty(com.servinte.axioma.orm.UsuarioXConvenio)
	 */
	@Override
	public void attachDirty(UsuarioXConvenio instance) {
		mundo.attachDirty(instance);		
	}

	/* (non-Javadoc)
	 * @see com.servinte.axioma.servicio.interfaz.capitacion.IUsuarioXConvenioServicio#listarTodos()
	 */
	@Override
	public ArrayList<UsuarioXConvenio> listarTodos() {
		return mundo.listarTodos();
	}
	
	
	
	

	

}
