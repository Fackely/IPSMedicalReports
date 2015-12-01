package com.servinte.axioma.servicio.impl.administracion;

import java.util.ArrayList;

import com.servinte.axioma.mundo.fabrica.AdministracionFabricaMundo;
import com.servinte.axioma.mundo.interfaz.administracion.IEspecialidadesMundo;
import com.servinte.axioma.orm.Especialidades;
import com.servinte.axioma.servicio.interfaz.administracion.IEspecialidadesServicio;

/**
 * Esta clase se encarga de
 * @author Angela Maria Aguirre
 * @since 9/09/2010
 */
public class EspecialidadesServicio implements IEspecialidadesServicio {
	
	IEspecialidadesMundo mundo;
	
	public EspecialidadesServicio(){
		mundo = AdministracionFabricaMundo.crearEspecialidadesMundo();
	}
	
	/**
	 * 	
	 * Este Método se encarga de consultar las especialidades
	 * registradas en el sistema
	 * 
	 * @return ArrayList<TiposServicio>
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ArrayList<Especialidades> buscarEspecialidades(){
		return mundo.buscarEspecialidades();
	}

	/**
	 * 
	 * M&eacute;todo encargado de listar las especialidades registradas en el
	 * sistema en orden alfab&eacute;tico
	 * 
	 * @return ArrayList<Especialidades>
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public ArrayList<Especialidades> listarEspecialidadesEnOrden() {
		return mundo.listarEspecialidadesEnOrden();
	}

}
