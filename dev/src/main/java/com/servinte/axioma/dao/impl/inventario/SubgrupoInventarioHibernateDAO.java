/**
 * 
 */
package com.servinte.axioma.dao.impl.inventario;

import java.util.ArrayList;

import com.princetonsa.dto.facturacion.DTOBusquedaMontoAgrupacionArticulo;
import com.servinte.axioma.dao.interfaz.inventario.ISubgrupoInventarioDAO;
import com.servinte.axioma.orm.SubgrupoInventario;
import com.servinte.axioma.orm.delegate.inventario.SubgrupoInventarioDelegate;

/**
 * Esta clase se encarga de 
 * @author Angela Aguirre
 *
 */
public class SubgrupoInventarioHibernateDAO implements ISubgrupoInventarioDAO{
	
	SubgrupoInventarioDelegate delegate;
	
	public SubgrupoInventarioHibernateDAO(){
		delegate = new SubgrupoInventarioDelegate();
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
		return delegate.buscarSubgrupoInventario();
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
		return delegate.buscarSubgrupoInventarioPK(dto);
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
		return delegate.buscarSubgrupoInventarioPorGrupoID(grupoID);
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
		return delegate.buscarSubgrupoInventarioPorCodigo(codigo);
	}
}
