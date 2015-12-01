package com.servinte.axioma.orm.delegate.capitacion;

import java.util.ArrayList;

import org.axioma.util.log.Log4JManager;
import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;

import com.princetonsa.dto.capitacion.DTOBusquedaNivelAutorServicioEspecifico;
import com.servinte.axioma.orm.NivelAutorServicio;
import com.servinte.axioma.orm.NivelAutorServicioHome;

/**
 * Esta clase se encarga de ejecutar los métodos de negocio de 
 * la entidad Nivel de Autorización de servicios específicos
 * @author Angela Maria Aguirre
 * @since 28/09/2010
 */
public class NivelAutorizacionServicioEspecificoDelegate extends
		NivelAutorServicioHome {
	
	/**
	 * 
	 * Este método se encarga de consultar todos los
	 * niveles de autorización por servicios específicos 
	 * a través del id del nivel de autorización de serivicios -
	 * artículos relacionado
	 * 
	 * @param int id
	 * @return ArrayList<DTOBusquedaNivelAutorServicioEspecifico>
	 * @author Angela Aguirre
	 * 
	 */
	public ArrayList<DTOBusquedaNivelAutorServicioEspecifico> buscarNivelAutorizacionServicios(int id,int codigoTarifario){
					
		Criteria criteria = sessionFactory.getCurrentSession()
		.createCriteria(NivelAutorServicio.class,"servicioEspecifico")
		.createAlias("servicioEspecifico.nivelAutorServMedic", "nivelAutorServArt")
		.createAlias("servicioEspecifico.servicios", "servicio")
		.createAlias("servicio.referenciasServicios", "referenciaServicio")
		.createAlias("referenciaServicio.tarifariosOficiales", "tarifario");
		
		criteria.add(Restrictions.eq("nivelAutorServArt.codigoPk", id));
		criteria.add(Restrictions.eq("tarifario.codigo", codigoTarifario));
		
		criteria.setProjection(Projections.projectionList()			
				.add(Projections.property("servicioEspecifico.codigoPk"),"codigoPk")				
				.add(Projections.property("nivelAutorServArt.codigoPk"),"nivelAutoriSerArt")
				.add(Projections.property("servicio.codigo"),"codigoServicio")
				.add(Projections.property("referenciaServicio.descripcion"),"nombreServicio"));
		
		criteria.setResultTransformer(Transformers.aliasToBean(DTOBusquedaNivelAutorServicioEspecifico.class));
		
		ArrayList<DTOBusquedaNivelAutorServicioEspecifico> listaServicio = (
				ArrayList<DTOBusquedaNivelAutorServicioEspecifico>)criteria.list();
		
		return listaServicio;
	}	
	
	/**
	 * 
	 * Este método se encarga de eliminar el registro
	 * de un nivel de autorización de servicio específico
	 * 
	 * @param int
	 * @return boolean 
	 * @author, Angela Maria Aguirre
	 *
	 */
	public boolean eliminarServicioEspecifico(int id){
		boolean save = true;					
		try{
			NivelAutorServicio registro = super.findById(id);		
			super.delete(registro);
		} catch (Exception e) {
			save = false;
			Log4JManager.error("No se pudo eliminar el registro de " +
					"nivel de autorización de servicio específico",e);
		}				
		return save;		
	} 

}
