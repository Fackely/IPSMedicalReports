/**
 * 
 */
package com.servinte.axioma.servicio.impl.inventario;

import java.util.ArrayList;

import com.servinte.axioma.mundo.fabrica.inventario.InventarioMundoFabrica;
import com.servinte.axioma.mundo.interfaz.inventario.IGrupoInventarioMundo;
import com.servinte.axioma.orm.GrupoInventario;
import com.servinte.axioma.servicio.interfaz.inventario.IGrupoInventarioServicio;

/**
 * Esta clase se encarga de 
 * @author Angela Aguirre
 *
 */
public class GrupoInventarioServicio implements IGrupoInventarioServicio {
	
	IGrupoInventarioMundo mundo;
	
	public GrupoInventarioServicio(){
		mundo = InventarioMundoFabrica.crearGrupoInventarioMundo();
	}	
	
	/**
	 * 	
	 * Este Método se encarga de consultar los grupos del módulo
	 * de inventario del sistema
	 * 
	 * @return ArrayList<GrupoInventario>
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ArrayList<GrupoInventario> buscarGrupoInventario(){
		return mundo.buscarGrupoInventario();
	}
	
	/**
	 * 	
	 * Este Método se encarga de consultar los grupos del módulo
	 * de inventario por la clase de inventario seleccionada
	 * 
	 * @return ArrayList<GrupoInventario>
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ArrayList<GrupoInventario> buscarGrupoInventarioPorClase(int idClase){
		return mundo.buscarGrupoInventarioPorClase(idClase);
	}

	/**
	 * 	
	 * Este Método se encarga de consultar el grupo de un subgrupo de inventario
	 * 
	 * @return GrupoInventario
	 * @author, Fabian Becerra
	 *
	 */
	public GrupoInventario buscarGrupoInventarioPorSubgrupo(int idSubgrupo){
		return mundo.buscarGrupoInventarioPorSubgrupo(idSubgrupo);
	}
}
