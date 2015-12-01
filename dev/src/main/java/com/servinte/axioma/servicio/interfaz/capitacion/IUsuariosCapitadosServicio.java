package com.servinte.axioma.servicio.interfaz.capitacion;

import java.util.ArrayList;

import com.princetonsa.dto.manejoPaciente.DtoUsuariosCapitados;
import com.servinte.axioma.orm.UsuariosCapitados;

/**
 * Esta clase se encarga de UsuariosCapitados
 * @author Cristhian Murillo
 */
public interface IUsuariosCapitadosServicio 
{

	/**
	 * Busca un usuario en las estructuras d eusuarios capitados
	 * @param parametrosBusqueda
	 * @return ArrayList<DtoUsuariosCapitados>
	 * 
	 * @author Cristhian Murillo
	 */
	public ArrayList<DtoUsuariosCapitados> buscarUsuariosCapitados(DtoUsuariosCapitados parametrosBusqueda);
	
	
	/**
	 * delete
	 * @param persistentInstance
	 */
	public void delete(UsuariosCapitados persistentInstance);


	/**
	 * findById
	 * @param id
	 * @return
	 */
	public UsuariosCapitados findById(long id);
}
