package com.servinte.axioma.orm.delegate.capitacion;

import java.util.ArrayList;

import org.axioma.util.log.Log4JManager;
import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;

import com.princetonsa.dto.capitacion.DTOBusquedaNivelAutorizacionUsuario;
import com.servinte.axioma.orm.NivelAutorUsuario;
import com.servinte.axioma.orm.NivelAutorUsuarioHome;

/**
 * Esta clase se encarga de ejecutar los métodos de negocio de 
 * la entidad Nivel de Autorización
 * 
 * @author Angela Maria Aguirre
 * @since 20/09/2010
 */
public class NivelAutorizacionUsuarioDelegate extends NivelAutorUsuarioHome {
	
	/**
	 * 
	 * Este Método se encarga de consultar los niveles de autorización
	 * registrados en el sistema
	 * 
	 * @return DTONivelAutorizacionUsuario
	 * @author, Angela Maria Aguirre
	 *
	 */
	public DTOBusquedaNivelAutorizacionUsuario buscarNivelAutorizacionUsuario(int nivelAutorizacionID){
		
		Criteria criteria = sessionFactory.getCurrentSession()
		.createCriteria(NivelAutorUsuario.class,"autorizacionUsuario");
		
		criteria.createAlias("autorizacionUsuario.nivelAutorizacion", "nivelAutorizacion");
				
		criteria.setProjection(Projections.projectionList()				
			.add(Projections.property("autorizacionUsuario.codigoPk"),"codigoPk")
			.add(Projections.property("nivelAutorizacion.codigoPk"),"nivelAutorizacionID")			
			.add(Projections.property("nivelAutorizacion.descripcion"),"descripcionNivelAutorizacion"));			
		
		criteria.add(Restrictions.eq("nivelAutorizacion.codigoPk", nivelAutorizacionID));	
				
		criteria.setResultTransformer(Transformers.aliasToBean(DTOBusquedaNivelAutorizacionUsuario.class));
		
		ArrayList<DTOBusquedaNivelAutorizacionUsuario> lista = 
			(ArrayList<DTOBusquedaNivelAutorizacionUsuario>)criteria.list();
		
		if(lista!=null && lista.size()>0){
			
			return (DTOBusquedaNivelAutorizacionUsuario)criteria.list().get(0);
		}
		return null;
	}
	
	/**
	 * 
	 * Este Método se encarga de guardar los niveles de autorización 
	 * de usuario registrados en el sistema
	 * 
	 * @return boolean
	 * @author, Angela Maria Aguirre
	 *
	 */
	public boolean guardarNivelAutorizacionUsuario(NivelAutorUsuario registro){
		boolean save = true;					
		try{
			super.persist(registro);
		} catch (Exception e) {
			save = false;
			Log4JManager.error("No se pudo guardar el registro del" +
					"nivel del autorización de usuario: ",e);
		}				
		return save;	
	}
	
	/**
	 * 
	 * Este Método se encarga de actualizar los niveles de autorización
	 * de usuario registrados en el sistema
	 * 
	 * @return boolean
	 * @author, Angela Maria Aguirre
	 *
	 */
	public boolean actualizarNivelAutorizacionUsuario(NivelAutorUsuario registro){
		boolean save = true;					
		try{
			super.merge(registro);
		} catch (Exception e) {
			save = false;
			Log4JManager.error("No se pudo actualizar el registro del" +
					"nivel de autorización de usuario: ",e);
		}				
		return save;	
	}
	
	/**
	 * 
	 * Este método se encarga de eliminar el registro
	 * de un nivel de autorización de usuario
	 * 
	 * @param int
	 * @return boolean 
	 * @author, Angela Maria Aguirre
	 *
	 */
	public boolean eliminarNivelAutorizacionUsuario(int idNivelAutorizacionUsuario){
		boolean save = true;					
		try{
			NivelAutorUsuario nivelAutorizacionUsuario = super.findById(idNivelAutorizacionUsuario);			
			super.delete(nivelAutorizacionUsuario);
		} catch (Exception e) {
			save = false;
			Log4JManager.error("No se pudo eliminar el registro de " +
					"nivel de autorización de usuario: ",e);
		}				
		return save;		
	} 

	

}
