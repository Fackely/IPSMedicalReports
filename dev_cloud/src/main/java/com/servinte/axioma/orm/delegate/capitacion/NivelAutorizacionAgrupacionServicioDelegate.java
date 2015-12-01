package com.servinte.axioma.orm.delegate.capitacion;

import java.util.ArrayList;

import org.axioma.util.log.Log4JManager;
import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;

import com.princetonsa.dto.capitacion.DTOBusquedaNivelAutorAgrupacionServicio;
import com.servinte.axioma.orm.NivelAutorAgrServ;
import com.servinte.axioma.orm.NivelAutorAgrServHome;

/**
 * Esta clase se encarga de ejecutar los métodos de negocio de 
 * la entidad Nivel de Autorización de agrupación de servicios
 * @author Angela Maria Aguirre
 * @since 28/09/2010
 */
public class NivelAutorizacionAgrupacionServicioDelegate extends
		NivelAutorAgrServHome {
	
	/**
	 * 
	 * Este Método se encarga de consultar los niveles de autorización
	 * de agrupación de servicios registrados en el sistema
	 * 
	 * @return DTOBusquedaNivelAutorizacionServicioArticulo
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ArrayList<DTOBusquedaNivelAutorAgrupacionServicio> buscarNivelAutorizacionServicioArticulo(int nivelAutorizacionID){
		
		Criteria criteria = sessionFactory.getCurrentSession()
		.createCriteria(NivelAutorAgrServ.class,"autorizacionAgrServicio");
		
		criteria.createAlias("autorizacionAgrServicio.nivelAutorServMedic", "nivelAutorizacionSerArt")
			.createAlias("autorizacionAgrServicio.tiposServicio", "tipoServicio",Criteria.LEFT_JOIN)
			.createAlias("autorizacionAgrServicio.gruposServicios", "grupoServicios",Criteria.LEFT_JOIN)
			.createAlias("autorizacionAgrServicio.especialidades", "especialidad",Criteria.LEFT_JOIN);
		
		criteria.add(Restrictions.eq("nivelAutorizacionSerArt.codigoPk", nivelAutorizacionID));	
		
		criteria.setProjection(Projections.projectionList()				
			.add(Projections.property("autorizacionAgrServicio.codigoPk"),"codigoPk")
			.add(Projections.property("nivelAutorizacionSerArt.codigoPk"),"nivelAutoriSerArt")
			.add(Projections.property("tipoServicio.acronimo"),"tipoServicio")		
			.add(Projections.property("grupoServicios.codigo"),"grupoServicio")
			.add(Projections.property("especialidad.codigo"),"especialidad")		
		);
		
				
		criteria.setResultTransformer(Transformers.aliasToBean(DTOBusquedaNivelAutorAgrupacionServicio.class));
		
		ArrayList<DTOBusquedaNivelAutorAgrupacionServicio> lista = 
			(ArrayList<DTOBusquedaNivelAutorAgrupacionServicio>)criteria.list();
				
		return lista;
	}
	
	/**
	 * 
	 * Este método se encarga de eliminar el registro
	 * de un nivel de autorización de agrupación de servicios
	 * 
	 * @param int
	 * @return boolean 
	 * @author, Angela Maria Aguirre
	 *
	 */
	public boolean eliminarNivelAutorizacionServicioArticulo(int idNivelAutorizacionUsuario){
		boolean save = true;					
		try{
			NivelAutorAgrServ nivelAutorizacion = super.findById(idNivelAutorizacionUsuario);			
			super.delete(nivelAutorizacion);
		} catch (Exception e) {
			save = false;
			Log4JManager.error("No se pudo eliminar el registro de " +
					"nivel de agrupacioón de servicios: ",e);
		}				
		return save;		
	} 

}
