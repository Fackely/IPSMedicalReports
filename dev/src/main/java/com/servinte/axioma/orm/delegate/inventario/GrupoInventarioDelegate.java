/**
 * 
 */
package com.servinte.axioma.orm.delegate.inventario;

import java.util.ArrayList;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import com.servinte.axioma.orm.GrupoInventario;
import com.servinte.axioma.orm.GrupoInventarioHome;

/**
 * Esta clase se encarga de 
 * @author Angela Aguirre
 *
 */
public class GrupoInventarioDelegate extends GrupoInventarioHome {
	
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
		 Criteria criteria = sessionFactory.getCurrentSession()
			.createCriteria(GrupoInventario.class,"grupo");
		 criteria.createAlias("grupo.claseInventario", "clase");
		 ArrayList<GrupoInventario> lista =(ArrayList<GrupoInventario>)criteria.list();
		 
		 if(lista!=null && lista.size()>0){
			 for(GrupoInventario registro :lista){
				 registro.getClaseInventario().getCodigo();
			 }
		 }
		 
		 return lista;
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
		 Criteria criteria = sessionFactory.getCurrentSession()
			.createCriteria(GrupoInventario.class,"grupo");
		 criteria.createAlias("grupo.claseInventario", "clase");
		 
		 criteria.add(Restrictions.eq("clase.codigo", idClase));		
		 
		 ArrayList<GrupoInventario> lista =(ArrayList<GrupoInventario>)criteria.list();
			 
		 return lista;
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
		 Criteria criteria = sessionFactory.getCurrentSession()
			.createCriteria(GrupoInventario.class,"grupo");
		 criteria.createAlias("grupo.subgrupoInventarios", "subgrupo");
		 
		 criteria.add(Restrictions.eq("subgrupo.codigo", idSubgrupo));		
		 
		 GrupoInventario grupo =(GrupoInventario)criteria.uniqueResult();
			 
		 return grupo;
	}
	
}
