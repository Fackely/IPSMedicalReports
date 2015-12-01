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

import com.princetonsa.dto.capitacion.DTONivelAutorizacionOcupacionMedica;
import com.servinte.axioma.orm.NivelAutorOcupMedica;
import com.servinte.axioma.orm.NivelAutorOcupMedicaHome;

/**
 * Esta clase se encarga de ejecutar los m�todos de negocio
 * para la entidad NivelAutorizacionUsuarioEspecifico
 * @author Angela Aguirre
 *
 */
public class NivelAutorizacionOcupacionMedicaDelegate extends
		NivelAutorOcupMedicaHome {
	
	/**
	 * 
	 * Este m�todo se encarga de consultar los niveles de autorizaci�n de
	 * las ocupaciones m�dicas 
	 * 
	 * @param int id
	 * @return ArrayList<DTONivelAutorizacionOcupacionMedica>
	 * @author Angela Maria Aguirre
	 */
	public ArrayList<DTONivelAutorizacionOcupacionMedica> 
			buscarNivelAutorOcupacionMedicaPorNivelAutorUsuarioID(int id){
		
		Criteria criteria = sessionFactory.getCurrentSession()
		.createCriteria(NivelAutorOcupMedica.class,"nivelOcupacionMedica");
		criteria.createAlias("nivelOcupacionMedica.nivelAutorUsuario", "nivelAutorUsuario");
		criteria.createAlias("nivelOcupacionMedica.ocupacionesMedicas", "ocupacionMedica");
		
		criteria.add(Restrictions.eq("nivelAutorUsuario.codigoPk", id));
		
		criteria.setProjection(Projections.projectionList()
				.add(Projections.property("nivelOcupacionMedica.codigoPk"),"codigoPk")
				.add(Projections.property("nivelAutorUsuario.codigoPk"),"nivelAutorizacionUsuarioID")
				.add(Projections.property("ocupacionMedica.codigo"),"ocupacionMedicaID"));
				
		criteria.setResultTransformer(Transformers.aliasToBean(DTONivelAutorizacionOcupacionMedica.class));	
		
		ArrayList<DTONivelAutorizacionOcupacionMedica> listaMontos = 
			(ArrayList<DTONivelAutorizacionOcupacionMedica>)criteria.list();
	 
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
	public boolean eliminarNivelAutorizacionOcupacionMedica(int id){
		
		boolean save = true;					
		try{
			NivelAutorOcupMedica nivel = super.findById(id);			
			super.delete(nivel);
		} catch (Exception e) {
			save = false;
			Log4JManager.error("No se pudo eliminar el registro de " +
					"nivel de autorizaci�n de ocupaciones m�dicas : ",e);
		}				
		return save;
		
	}

}
