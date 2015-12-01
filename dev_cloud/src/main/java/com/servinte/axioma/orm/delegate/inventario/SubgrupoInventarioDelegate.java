/**
 * 
 */
package com.servinte.axioma.orm.delegate.inventario;

import java.util.ArrayList;

import org.axioma.util.log.Log4JManager;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import com.princetonsa.dto.facturacion.DTOBusquedaMontoAgrupacionArticulo;
import com.servinte.axioma.orm.SubgrupoInventario;
import com.servinte.axioma.orm.SubgrupoInventarioHome;

/**
 * Esta clase se encarga de 
 * @author Angela Aguirre
 *
 */
@SuppressWarnings("unchecked")
public class SubgrupoInventarioDelegate extends SubgrupoInventarioHome {
	
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
		 Criteria criteria = sessionFactory.getCurrentSession()
			.createCriteria(SubgrupoInventario.class);
		 
		 return (ArrayList<SubgrupoInventario>)criteria.list();
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
		 Criteria criteria = sessionFactory.getCurrentSession()
			.createCriteria(SubgrupoInventario.class,"subgrupo");
		 
		 criteria.add(Restrictions.eq("subgrupo.id.clase", dto.getClaseInventarioCodigo()));
		 criteria.add(Restrictions.eq("subgrupo.id.grupo", dto.getGrupoCodigo()));
		 criteria.add(Restrictions.eq("subgrupo.id.subgrupo", dto.getCodigoSubgrupoInventario()));		 
		 
		 return (SubgrupoInventario)criteria.uniqueResult();
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
		Criteria criteria = sessionFactory.getCurrentSession()
		.createCriteria(SubgrupoInventario.class,"subgrupo");
	 
	 criteria.add(Restrictions.eq("subgrupo.id.grupo", grupoID));		 
	 
	 return (ArrayList<SubgrupoInventario>)criteria.list();
	}

	
	/**
	 * 	
	 * Este Método se encarga de obtener el subgrupo por su codigo único
	 *  
	 * @param codigo código único de la entidad subgrupoInventario
	 * @return SubgrupoInventario
	 * @author, Fabián Becerra
	 *
	 */
	public SubgrupoInventario buscarSubgrupoInventarioPorCodigo(int codigo)
	{
		 Criteria criteria = sessionFactory.getCurrentSession().createCriteria(SubgrupoInventario.class,"subgrupo");
		 
		 criteria.add(Restrictions.eq("subgrupo.codigo", codigo));
		 
		 SubgrupoInventario subgrupoInventario = new SubgrupoInventario();
		 subgrupoInventario = (SubgrupoInventario)criteria.uniqueResult();
		 
		 if(subgrupoInventario != null)
		 {
			 subgrupoInventario.getGrupoInventario().getClaseInventario().getCodigo();
			 Log4JManager.info("subgrupoInventario: "+subgrupoInventario.getNombre());
		 }
		 
		 return subgrupoInventario;
	} 
	
	
	
}
