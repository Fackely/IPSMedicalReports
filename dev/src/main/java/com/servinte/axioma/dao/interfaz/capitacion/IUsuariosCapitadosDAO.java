/**
 * 
 */
package com.servinte.axioma.dao.interfaz.capitacion;

import java.util.ArrayList;

import com.princetonsa.dto.manejoPaciente.DtoUsuariosCapitados;
import com.servinte.axioma.orm.UsuariosCapitados;

/**
 * Esta clase se encarga de definir los métodos de negocio
 * para la entidad UsuariosCapitados
 * @author Cristhian Murillo
 *
 */
public interface IUsuariosCapitadosDAO 
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
	
	/**
	 * Merge
	 * @param instance
	 * @return
	 * 
	 * @author Ricardo Ruiz
	 */
	public UsuariosCapitados merge(UsuariosCapitados instance);
	
	/**
	 * AttachDirty
	 * @param persistenceInstance
	 * @return
	 * 
	 * @author Ricardo Ruiz
	 */
	public void attachDirty(UsuariosCapitados persistentInstance);
	
	/**
	 * AttachClean
	 * @param persistenceInstance
	 * @return
	 * 
	 * @author Ricardo Ruiz
	 */
	public void attachClean(UsuariosCapitados persistentInstance);
	
	/**
	 * Persist
	 * @param persistenceInstance
	 * @return
	 * 
	 * @author Ricardo Ruiz
	 */
	public void persist(UsuariosCapitados persistentInstance);


	/**
	 * Método utilizado para consulta el codigo del usuario capitado por tipo y numero de identificación
	 * @param tipoId
	 * @param numeroId
	 * @return
	 * 
	 * @author Ricardo Ruiz
	 */
	public Long buscarCodigoUsuarioCapitadoPorTipoNumeroID(String tipoId, String numeroId);
	
}
