package com.servinte.axioma.mundo.impl.administracion;

import java.util.ArrayList;

import com.servinte.axioma.dao.fabrica.AdministracionFabricaDAO;
import com.servinte.axioma.dao.interfaz.administracion.IEspecialidadesDAO;
import com.servinte.axioma.mundo.interfaz.administracion.IEspecialidadesMundo;
import com.servinte.axioma.orm.Especialidades;

/**
 * Esta clase se encarga de
 * @author Angela Maria Aguirre
 * @since 9/09/2010
 */
public class EspecialidadesMundo implements IEspecialidadesMundo {
	
	IEspecialidadesDAO dao;
	
	/**
	 * 
	 * Método constructor de la clase
	 * @author, Angela Maria Aguirre
	 */
	public EspecialidadesMundo(){
		dao = AdministracionFabricaDAO.crearEspecialidadesDAO();
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
		return dao.buscarEspecialidades();
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
		return dao.listarEspecialidadesEnOrden();
	}

}
