/**
 * 
 */
package com.servinte.axioma.servicio.interfaz.inventario;

import java.util.ArrayList;

import com.princetonsa.dto.facturacion.DTOBusquedaMontoAgrupacionArticulo;
import com.servinte.axioma.orm.SubgrupoInventario;

/**
 * Esta clase se encarga de 
 * @author Angela Aguirre
 *
 */
public interface ISubgrupoInventarioServicio {
	
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
}
