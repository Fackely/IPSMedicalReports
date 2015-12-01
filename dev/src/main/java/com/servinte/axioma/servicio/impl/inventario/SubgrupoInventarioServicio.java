/**
 * 
 */
package com.servinte.axioma.servicio.impl.inventario;

import java.util.ArrayList;

import com.princetonsa.dto.facturacion.DTOBusquedaMontoAgrupacionArticulo;
import com.servinte.axioma.mundo.fabrica.inventario.InventarioMundoFabrica;
import com.servinte.axioma.mundo.interfaz.inventario.ISubgrupoInventarioMundo;
import com.servinte.axioma.orm.SubgrupoInventario;
import com.servinte.axioma.servicio.interfaz.inventario.ISubgrupoInventarioServicio;

/**
 * Esta clase se encarga de 
 * @author Angela Aguirre
 *
 */
public class SubgrupoInventarioServicio implements ISubgrupoInventarioServicio {
	
	ISubgrupoInventarioMundo mundo;
	
	public SubgrupoInventarioServicio(){
		mundo = InventarioMundoFabrica.crearSubgrupoInventarioMundo();
	}
	
	/**
	 * 	
	 * Este Método se encarga de consultar los subgrupos del 
	 * módulo de inventarios del sistema
	 * 
	 * @return ArrayList<SubgrupoInventario>
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ArrayList<SubgrupoInventario> buscarSubgrupoInventario(){
		return mundo.buscarSubgrupoInventario();
	}
	
	/**
	 * 	
	 * Este Método se encarga de consultar los subgrupos del 
	 * módulo de inventarios del sistema por su llave primaria
	 *  
	 * @param DTOBusquedaMontoAgrupacionArticulo dto
	 * @return SubgrupoInventario
	 * @author, Angela Maria Aguirre
	 *
	 */
	public SubgrupoInventario buscarSubgrupoInventarioPK(DTOBusquedaMontoAgrupacionArticulo dto){
		return mundo.buscarSubgrupoInventarioPK(dto);
	}
	
	/**
	 * 	
	 * Este Método se encarga de consultar los subgrupos del 
	 * módulo de inventarios por el grupo de inventario relacionado
	 *  
	 * @param ArrayList<SubgrupoInventario> 
	 * @return SubgrupoInventario
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ArrayList<SubgrupoInventario> buscarSubgrupoInventarioPorGrupoID(int grupoID){
		return mundo.buscarSubgrupoInventarioPorGrupoID(grupoID);
	}

}
