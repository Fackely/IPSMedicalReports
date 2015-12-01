package com.servinte.axioma.orm.delegate.capitacion;

import java.util.ArrayList;

import org.axioma.util.log.Log4JManager;
import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;

import com.princetonsa.dto.capitacion.DTOBusquedaNivelAutorizacionServicioArticulo;
import com.servinte.axioma.orm.NivelAutorServMedic;
import com.servinte.axioma.orm.NivelAutorServMedicHome;

/**
 * Esta clase se encarga de ejecutar los m�todos de negocio de 
 * la entidad Nivel de Autorizaci�n de Servicios y Art�culos
 * @author Angela Maria Aguirre
 * @since 28/09/2010
 */
public class NivelAutorizacionServicioArticuloDelegate extends
		NivelAutorServMedicHome {
	
	/**
	 * 
	 * Este M�todo se encarga de consultar los niveles de autorizaci�n
	 * de servicios y art�culos registrados en el sistema
	 * 
	 * @return DTOBusquedaNivelAutorizacionServicioArticulo
	 * @author, Angela Maria Aguirre
	 *
	 */
	public DTOBusquedaNivelAutorizacionServicioArticulo buscarNivelAutorizacionServicioArticulo(int nivelAutorizacionID){
		
		Criteria criteria = sessionFactory.getCurrentSession()
		.createCriteria(NivelAutorServMedic.class,"autorizacionSerArt");
		
		criteria.createAlias("autorizacionSerArt.nivelAutorizacion", "nivelAutorizacion");
				
		criteria.setProjection(Projections.projectionList()				
			.add(Projections.property("autorizacionSerArt.codigoPk"),"codigoPk")
			.add(Projections.property("nivelAutorizacion.codigoPk"),"nivelAutorizacionID")			
			.add(Projections.property("nivelAutorizacion.descripcion"),"descripcionNivelAutorizacion"));			
		
		criteria.add(Restrictions.eq("nivelAutorizacion.codigoPk", nivelAutorizacionID));	
				
		criteria.setResultTransformer(Transformers.aliasToBean(DTOBusquedaNivelAutorizacionServicioArticulo.class));
		
		ArrayList<DTOBusquedaNivelAutorizacionServicioArticulo> lista = 
			(ArrayList<DTOBusquedaNivelAutorizacionServicioArticulo>)criteria.list();
		
		if(lista!=null && lista.size()>0){
			
			return (DTOBusquedaNivelAutorizacionServicioArticulo)criteria.list().get(0);
		}
		return null;
	}
	
	/**
	 * 
	 * Este M�todo se encarga de guardar los niveles de autorizaci�n 
	 * de servicios y art�culos registrados en el sistema
	 * 
	 * @return boolean
	 * @author, Angela Maria Aguirre
	 *
	 */
	public boolean guardarNivelAutorizacionServicioArticulo(NivelAutorServMedic registro){
		boolean save = true;					
		try{
			super.persist(registro);
		} catch (Exception e) {
			save = false;
			Log4JManager.error("No se pudo guardar el registro del" +
					"nivel del autorizaci�n de servicios / art�culos: ",e);
		}				
		return save;	
	}
	
	/**
	 * 
	 * Este M�todo se encarga de actualizar los niveles de autorizaci�n
	 * de servicios y art�culos registrados en el sistema
	 * 
	 * @return boolean
	 * @author, Angela Maria Aguirre
	 *
	 */
	public boolean actualizarNivelAutorizacionServicioArticulo(NivelAutorServMedic registro){
		boolean save = true;					
		try{
			super.merge(registro);
		} catch (Exception e) {
			save = false;
			Log4JManager.error("No se pudo actualizar el registro del" +
					"nivel de autorizaci�n de servicios / art�culos: ",e);
		}				
		return save;	
	}
	
	/**
	 * 
	 * Este m�todo se encarga de eliminar el registro
	 * de un nivel de autorizaci�n de servicios y art�culos
	 * 
	 * @param int
	 * @return boolean 
	 * @author, Angela Maria Aguirre
	 *
	 */
	public boolean eliminarNivelAutorizacionServicioArticulo(int idNivelAutorizacionUsuario){
		boolean save = true;					
		try{
			NivelAutorServMedic nivelAutorizacion = super.findById(idNivelAutorizacionUsuario);			
			super.delete(nivelAutorizacion);
		} catch (Exception e) {
			save = false;
			Log4JManager.error("No se pudo eliminar el registro de " +
					"nivel de servicios / art�culos: ",e);
		}				
		return save;		
	} 

}
