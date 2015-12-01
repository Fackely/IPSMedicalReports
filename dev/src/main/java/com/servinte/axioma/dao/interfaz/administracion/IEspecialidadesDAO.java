package com.servinte.axioma.dao.interfaz.administracion;

import java.util.ArrayList;

import com.servinte.axioma.orm.Especialidades;

/**
 * Esta clase se encarga de
 * @author Angela Maria Aguirre
 * @since 9/09/2010
 */
public interface IEspecialidadesDAO {
	
	/**
	 * 	
	 * Este Método se encarga de consultar las especialidades
	 * registradas en el sistema
	 * 
	 * @return ArrayList<TiposServicio>
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ArrayList<Especialidades> buscarEspecialidades();
	
	/**
	 * 
	 * M&eacute;todo encargado de listar las especialidades registradas en el
	 * sistema en orden alfab&eacute;tico
	 * 
	 * @return ArrayList<Especialidades>
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public ArrayList<Especialidades> listarEspecialidadesEnOrden();

}
