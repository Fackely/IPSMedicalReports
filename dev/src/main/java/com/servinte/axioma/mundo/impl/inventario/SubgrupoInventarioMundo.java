/**
 * 
 */
package com.servinte.axioma.mundo.impl.inventario;

import java.util.ArrayList;

import com.princetonsa.dto.facturacion.DTOBusquedaMontoAgrupacionArticulo;
import com.servinte.axioma.dao.fabrica.inventario.InventarioDAOFabrica;
import com.servinte.axioma.dao.interfaz.inventario.ISubgrupoInventarioDAO;
import com.servinte.axioma.mundo.interfaz.inventario.ISubgrupoInventarioMundo;
import com.servinte.axioma.orm.SubgrupoInventario;

/**
 * Esta clase se encarga de 
 * @author Angela Aguirre
 *
 */
public class SubgrupoInventarioMundo implements ISubgrupoInventarioMundo {
	
	ISubgrupoInventarioDAO dao;
	
	/**
	 * 
	 * M�todo constructor de la clase
	 */
	public SubgrupoInventarioMundo(){
		dao = InventarioDAOFabrica.crearSubgrupoInventarioDAO();
	}
	
	/**
	 * 	
	 * Este M�todo se encarga de consultar los subgrupos del 
	 * m�dulo de inventarios del sistema
	 * 
	 * @return ArrayList<SubgrupoInventario>
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ArrayList<SubgrupoInventario> buscarSubgrupoInventario(){
		return dao.buscarSubgrupoInventario();
	}
	
	/**
	 * 	
	 * Este M�todo se encarga de consultar los subgrupos del 
	 * m�dulo de inventarios del sistema por su llave primaria
	 *  
	 * @param DTOBusquedaMontoAgrupacionArticulo dto
	 * @return SubgrupoInventario
	 * @author, Angela Maria Aguirre
	 *
	 */
	public SubgrupoInventario buscarSubgrupoInventarioPK(DTOBusquedaMontoAgrupacionArticulo dto){
		return dao.buscarSubgrupoInventarioPK(dto);
	}
	
	/**
	 * 	
	 * Este M�todo se encarga de consultar los subgrupos del 
	 * m�dulo de inventarios por el grupo de inventario relacionado
	 *  
	 * @param ArrayList<SubgrupoInventario> 
	 * @return SubgrupoInventario
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ArrayList<SubgrupoInventario> buscarSubgrupoInventarioPorGrupoID(int grupoID){
		return dao.buscarSubgrupoInventarioPorGrupoID(grupoID);
	}

	/**
	 * 	
	 * Este M�todo se encarga de obtener el subgrupo por su codigo �nico
	 *  
	 * @param codigo c�digo �nico de la entidad subgrupoInventario
	 * @return SubgrupoInventario
	 * @author, Fabi�n Becerra
	 *
	 */
	public SubgrupoInventario buscarSubgrupoInventarioPorCodigo(int codigo){
		return dao.buscarSubgrupoInventarioPorCodigo(codigo);
	}

}
