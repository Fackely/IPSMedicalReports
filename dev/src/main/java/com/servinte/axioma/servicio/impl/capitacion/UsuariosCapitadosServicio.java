package com.servinte.axioma.servicio.impl.capitacion;

import java.util.ArrayList;

import com.princetonsa.dto.manejoPaciente.DtoUsuariosCapitados;
import com.servinte.axioma.mundo.fabrica.capitacion.CapitacionFabricaMundo;
import com.servinte.axioma.mundo.interfaz.capitacion.IUsuariosCapitadosMundo;
import com.servinte.axioma.orm.UsuariosCapitados;
import com.servinte.axioma.servicio.interfaz.capitacion.IUsuariosCapitadosServicio;

/**
 * Esta clase se encarga de UsuariosCapitados
 * @author Cristhian Murillo
 */
public class UsuariosCapitadosServicio implements IUsuariosCapitadosServicio
{

	
	IUsuariosCapitadosMundo mundo;
	
	
	/**
	 * Método constructor de la clase
	 * @author Cristhian Murillo
	 */
	public UsuariosCapitadosServicio(){
		mundo = CapitacionFabricaMundo.crearUsuariosCapitadosMundo();

	}
	
	
	@Override
	public ArrayList<DtoUsuariosCapitados> buscarUsuariosCapitados(DtoUsuariosCapitados parametrosBusqueda) {
		return mundo.buscarUsuariosCapitados(parametrosBusqueda);
	}


	@Override
	public void delete(UsuariosCapitados persistentInstance) {
		mundo.delete(persistentInstance);
	}


	@Override
	public UsuariosCapitados findById(long id) {
		return mundo.findById(id);
	}

	

}
