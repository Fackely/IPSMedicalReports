/**
 * 
 */
package com.servinte.axioma.mundo.interfaz.inventario;

import java.util.ArrayList;

import com.servinte.axioma.orm.GrupoInventario;

/**
 * Esta clase se encarga de 
 * @author Angela Aguirre
 *
 */
public interface IGrupoInventarioMundo {
	
	/**
	 * 	
	 * Este Método se encarga de consultar los grupos del módulo
	 * de inventario del sistema
	 * 
	 * @return ArrayList<GrupoInventario>
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ArrayList<GrupoInventario> buscarGrupoInventario();
	
	/**
	 * 	
	 * Este Método se encarga de consultar los grupos del módulo
	 * de inventario por la clase de inventario seleccionada
	 * 
	 * @return ArrayList<GrupoInventario>
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ArrayList<GrupoInventario> buscarGrupoInventarioPorClase(int idClase);
	
	/**
	 * 	
	 * Este Método se encarga de consultar el grupo de un subgrupo de inventario
	 * 
	 * @return ArrayList<GrupoInventario>
	 * @author, Fabian Becerra
	 *
	 */
	public GrupoInventario buscarGrupoInventarioPorSubgrupo(int idSubgrupo);

}
