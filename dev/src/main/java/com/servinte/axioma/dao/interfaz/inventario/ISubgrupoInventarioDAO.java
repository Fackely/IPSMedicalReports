/**
 * 
 */
package com.servinte.axioma.dao.interfaz.inventario;

import java.util.ArrayList;

import com.princetonsa.dto.facturacion.DTOBusquedaMontoAgrupacionArticulo;
import com.servinte.axioma.orm.SubgrupoInventario;

/**
 * Esta clase se encarga de 
 * @author Angela Aguirre
 *
 */
public interface ISubgrupoInventarioDAO {
	
	/**
	 * 	
	 * Este M�todo se encarga de consultar los subgrupos del 
	 * m�dulo de inventarios del sistema
	 * 
	 * @return ArrayList<SubgrupoInventario>
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ArrayList<SubgrupoInventario> buscarSubgrupoInventario();
	
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
	public SubgrupoInventario buscarSubgrupoInventarioPK(DTOBusquedaMontoAgrupacionArticulo dto);
	
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
	public ArrayList<SubgrupoInventario> buscarSubgrupoInventarioPorGrupoID(int grupoID);
	
	/**
	 * 	
	 * Este M�todo se encarga de obtener el subgrupo por su codigo �nico
	 *  
	 * @param codigo c�digo �nico de la entidad subgrupoInventario
	 * @return SubgrupoInventario
	 * @author, Fabi�n Becerra
	 *
	 */
	public SubgrupoInventario buscarSubgrupoInventarioPorCodigo(int codigo);

}
