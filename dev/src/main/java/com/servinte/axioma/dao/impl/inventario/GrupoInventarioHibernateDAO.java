/**
 * 
 */
package com.servinte.axioma.dao.impl.inventario;

import java.util.ArrayList;

import com.servinte.axioma.dao.interfaz.inventario.IGrupoInventarioDAO;
import com.servinte.axioma.orm.GrupoInventario;
import com.servinte.axioma.orm.delegate.inventario.GrupoInventarioDelegate;

/**
 * Esta clase se encarga de 
 * @author Angela Aguirre
 *
 */
public class GrupoInventarioHibernateDAO implements IGrupoInventarioDAO {
	
	GrupoInventarioDelegate delegate;
	
	public GrupoInventarioHibernateDAO(){
		delegate = new GrupoInventarioDelegate();
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
		return delegate.buscarGrupoInventario();
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
		return delegate.buscarGrupoInventarioPorClase(idClase);
	}
	
	
	/**
	 * 	
	 * Este Método se encarga de consultar el grupo de un subgrupo de inventario
	 * 
	 * @return ArrayList<GrupoInventario>
	 * @author, Fabian Becerra
	 *
	 */
	public GrupoInventario buscarGrupoInventarioPorSubgrupo(int idSubgrupo){
		return delegate.buscarGrupoInventarioPorSubgrupo(idSubgrupo);
	}

}
