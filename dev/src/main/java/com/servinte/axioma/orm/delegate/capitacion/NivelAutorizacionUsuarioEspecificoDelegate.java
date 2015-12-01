/**
 * 
 */
package com.servinte.axioma.orm.delegate.capitacion;

import java.util.ArrayList;

import org.axioma.util.log.Log4JManager;
import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;

import com.princetonsa.dto.capitacion.DTONivelAutorizacionUsuarioEspecifico;
import com.servinte.axioma.orm.NivelAutorUsuEspec;
import com.servinte.axioma.orm.NivelAutorUsuEspecHome;

/**
 * Esta clase se encarga de ejecutar los m�todos de negocio
 * para la entidad NivelAutorizacionUsuarioEspecifico
 * @author Angela Aguirre
 *
 */
public class NivelAutorizacionUsuarioEspecificoDelegate extends
		NivelAutorUsuEspecHome {
	
	/**
	 * 
	 * Este m�todo se encarga de consultar los niveles de autorizaci�n de
	 * usuario espec�fico 
	 * 
	 * @param int id
	 * @return ArrayList<DTONivelAutorizacionUsuarioEspecifico>
	 * @author Angela Maria Aguirre
	 */
	public ArrayList<DTONivelAutorizacionUsuarioEspecifico> 
			buscarNivelAutorUsuarioEspPorNivelAutorUsuarioID(int id){
		
		Criteria criteria = sessionFactory.getCurrentSession()
		.createCriteria(NivelAutorUsuEspec.class,"nivelUsuarioEsp");
		criteria.createAlias("nivelUsuarioEsp.nivelAutorUsuario", "nivelAutorUsuario");	
		criteria.createAlias("nivelUsuarioEsp.usuariosByUsuario", "usuarioEsp");
		
		criteria.add(Restrictions.eq("nivelAutorUsuario.codigoPk", id));
		
		criteria.setProjection(Projections.projectionList()
				.add(Projections.property("nivelUsuarioEsp.codigoPk"),"codigoPk")
				.add(Projections.property("nivelAutorUsuario.codigoPk"),"nivelAutorizacionUsuID")
				.add(Projections.property("usuarioEsp.login"),"usuarioEspLogin"));
				
		criteria.setResultTransformer(Transformers.aliasToBean(DTONivelAutorizacionUsuarioEspecifico.class));	
		
		ArrayList<DTONivelAutorizacionUsuarioEspecifico> listaMontos = 
			(ArrayList<DTONivelAutorizacionUsuarioEspecifico>)criteria.list();
	 
		return listaMontos;
		
	}
	
	/**
	 * 
	 * Este m�todo se encarga de eliminar los niveles de autorizaci�n de
	 * usuario espec�fico 
	 * 
	 * @param int id
	 * @return boolean
	 * @author Angela Maria Aguirre
	 */
	public boolean eliminarNivelAutorizacionUsuarioEspecifico(int id){
		
		boolean save = true;					
		try{
			NivelAutorUsuEspec nivel = super.findById(id);			
			super.delete(nivel);
		} catch (Exception e) {
			save = false;
			Log4JManager.error("No se pudo eliminar el registro de " +
					"nivel de autorizaci�n de usuario espec�fico : ",e);
		}				
		return save;
		
	}

}
