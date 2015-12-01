package com.servinte.axioma.orm.delegate.capitacion;

import java.util.ArrayList;

import org.axioma.util.log.Log4JManager;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;

import com.princetonsa.dto.capitacion.DTONivelAutorizacion;
import com.princetonsa.dto.capitacion.DtoValidacionNivelesAutorizacion;
import com.servinte.axioma.orm.NivelAutorizacion;
import com.servinte.axioma.orm.NivelAutorizacionHome;

/**
 * Esta clase se encarga de ejecutar los métodos de negocio de 
 * la entidad Nivel de Autorización
 * 
 * @author Angela Maria Aguirre
 * @since 20/09/2010
 */
public class NivelAutorDelegate extends NivelAutorizacionHome {
	
	
	/**
	 * 
	 * Este Método se encarga de consultar los niveles de autorización
	 * registrados en el sistema
	 * 
	 * @return ArrayList<DTONivelAutorizacion>
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ArrayList<DTONivelAutorizacion> buscarNivelAutorizacion(){
		
		Criteria criteria = sessionFactory.getCurrentSession()
		.createCriteria(NivelAutorizacion.class,"nivel");
		
		criteria.createAlias("nivel.viasIngreso", "viaIngreso");
		
		criteria.add(Restrictions.eq("nivel.activo", true));
		
		criteria.setProjection(Projections.projectionList()				
			.add(Projections.property("nivel.codigoPk"),"codigoPk")
			.add(Projections.property("nivel.activo"),"activo")
			.add(Projections.property("nivel.descripcion"),"descripcion")
			.add(Projections.property("nivel.tipoAutorizacion"),"tipoAutorizacionAcronimo")
			.add(Projections.property("viaIngreso.codigo"),"viasIngresoPK"));
		criteria.addOrder(Order.asc("nivel.descripcion"));
		
		criteria.setResultTransformer(Transformers.aliasToBean(DTONivelAutorizacion.class));
		
		ArrayList<DTONivelAutorizacion> lista = (ArrayList<DTONivelAutorizacion>)criteria.list(); 
		
		return lista;
	}
	
	
	/**
	 * 
	 * Este Método se encarga de consultar nivel de autorización de un usuario
	 * 
	 * @return ArrayList<DTONivelAutorizacion>
	 * @author, Fabián Becerra
	 *
	 */
	public ArrayList<DtoValidacionNivelesAutorizacion> buscarNivelAutorizacionPorUsuario(String login){
		
		Criteria criteria = sessionFactory.getCurrentSession()
		.createCriteria(NivelAutorizacion.class,"nivelAuto");
		
		criteria.createAlias("nivelAuto.nivelAutorUsuarios", "nivelAutoUsuario");
		criteria.createAlias("nivelAutoUsuario.nivelAutorUsuEspecs", "nivelAutoUsuEspec");
		criteria.createAlias("nivelAuto.viasIngreso", "viaIngreso");
		criteria.createAlias("nivelAutoUsuEspec.usuariosByUsuario", "usuario");
		
		criteria.add(Restrictions.eq("usuario.login", login));
		
		criteria.setProjection(Projections.projectionList()				
			.add(Projections.property("nivelAuto.codigoPk"),"codigoNivelAutorizacion")
			.add(Projections.property("nivelAuto.descripcion"),"descripcionNivelAutorizacion")
			.add(Projections.property("viaIngreso.codigo"),"viaIngreso")
			.add(Projections.property("nivelAuto.tipoAutorizacion"),"tipoAutorizacion")
			.add(Projections.property("usuario.login"),"usuarioAutorizacion")
			.add(Projections.property("nivelAutoUsuEspec.codigoPk"),"codigoNivelAutorizacionUsuario")
			.add(Projections.property("nivelAuto.activo"),"activo"));
			
		criteria.addOrder(Order.asc("nivelAuto.descripcion"));
		
		criteria.setResultTransformer(Transformers.aliasToBean(DtoValidacionNivelesAutorizacion.class));
		
		ArrayList<DtoValidacionNivelesAutorizacion> lista = (ArrayList<DtoValidacionNivelesAutorizacion>)criteria.list(); 
		
		return lista;
	}
	
	
	
	/**
	 * 
	 * Este Método se encarga de consultar nivel de autorización por ocupación medica
	 * 
	 * @return ArrayList<DTONivelAutorizacion>
	 * @author, Fabián Becerra
	 *
	 */
	public ArrayList<DtoValidacionNivelesAutorizacion> buscarNivelAutorizacionPorOcupacionMedica(int codigoOcupacion){
		
		Criteria criteria = sessionFactory.getCurrentSession()
		.createCriteria(NivelAutorizacion.class,"nivelAuto");
		
		criteria.createAlias("nivelAuto.nivelAutorUsuarios", "nivelAutoUsuario");
		criteria.createAlias("nivelAutoUsuario.nivelAutorOcupMedicas", "nivelAutoOcupMedicas");
		criteria.createAlias("nivelAuto.viasIngreso", "viaIngreso");
		criteria.createAlias("nivelAutoOcupMedicas.ocupacionesMedicas", "ocupacionMedica");
		
		criteria.add(Restrictions.eq("ocupacionMedica.codigo", codigoOcupacion));
		
		criteria.setProjection(Projections.projectionList()				
			.add(Projections.property("nivelAuto.codigoPk"),"codigoNivelAutorizacion")
			.add(Projections.property("nivelAuto.descripcion"),"descripcionNivelAutorizacion")
			.add(Projections.property("viaIngreso.codigo"),"viaIngreso")
			.add(Projections.property("nivelAuto.tipoAutorizacion"),"tipoAutorizacion")
			.add(Projections.property("ocupacionMedica.codigo"),"ocupacionMedicaAutorizacion")
			.add(Projections.property("nivelAutoOcupMedicas.codigoPk"),"codigoNivelAutorizacionOcupacionMedica")
			.add(Projections.property("nivelAuto.activo"),"activo"));
			
			//.add(Projections.property("viaIngreso.codigo"),"viaIngreso"));
		criteria.addOrder(Order.asc("nivelAuto.descripcion"));
		
		criteria.setResultTransformer(Transformers.aliasToBean(DtoValidacionNivelesAutorizacion.class));
		
		ArrayList<DtoValidacionNivelesAutorizacion> lista = (ArrayList<DtoValidacionNivelesAutorizacion>)criteria.list(); 
		
		return lista;
	}
	
	
	/**
	 * 
	 * Este Método se encarga de guardar los niveles de autorización
	 * registrados en el sistema
	 * 
	 * @return boolean
	 * @author, Angela Maria Aguirre
	 *
	 */
	public boolean guardarNivelAutorizacion(NivelAutorizacion registro){
		boolean save = true;					
		try{
			super.persist(registro);
		} catch (Exception e) {
			save = false;
			Log4JManager.error("No se pudo guardar el registro del" +
					"nivel del autorización: ",e);
		}				
		return save;	
	}
	
	/**
	 * 
	 * Este Método se encarga de actualizar los niveles de autorización
	 * registrados en el sistema
	 * 
	 * @return boolean
	 * @author, Angela Maria Aguirre
	 *
	 */
	public boolean actualizarNivelAutorizacion(NivelAutorizacion registro){
		boolean save = true;					
		try{
			super.merge(registro);
		} catch (Exception e) {
			save = false;
			Log4JManager.error("No se pudo actualizar el registro del" +
					"nivel del autorización: ",e);
		}				
		return save;	
	}
	
	/**
	 * 
	 * Este método se encarga de eliminar el registro
	 * de un nivel de autorización
	 * 
	 * @param int
	 * @return boolean 
	 * @author, Angela Maria Aguirre
	 *
	 */
	public boolean eliminarNivelAutorizacion(int idNivelAutorizacion){
		boolean save = true;					
		try{
			NivelAutorizacion nivelAutorizacion = super.findById(idNivelAutorizacion);			
			super.delete(nivelAutorizacion);
		} catch (Exception e) {
			save = false;
			Log4JManager.error("No se pudo eliminar el registro de " +
					"nivel del autorización: ",e);
		}				
		return save;		
	}
	
	/**
	 * Este Método se encarga de consultar los niveles de autorización
	 * registrados en el sistema activos e incativos
	 * 
	 * @return ArrayList<DTONivelAutorizacion>
	 * @author, Camilo Gómez
	 */
	public ArrayList<DTONivelAutorizacion> obtenerNivelesAutorizacion(){
		
		Criteria criteria = sessionFactory.getCurrentSession()
		.createCriteria(NivelAutorizacion.class,"nivel");
		
		criteria.createAlias("nivel.viasIngreso", "viaIngreso");
		
		criteria.setProjection(Projections.projectionList()				
			.add(Projections.property("nivel.codigoPk"),"codigoPk")
			.add(Projections.property("nivel.activo"),"activo")
			.add(Projections.property("nivel.descripcion"),"descripcion")
			.add(Projections.property("nivel.tipoAutorizacion"),"tipoAutorizacionAcronimo")
			.add(Projections.property("viaIngreso.codigo"),"viasIngresoPK"));
		criteria.addOrder(Order.asc("nivel.descripcion"));
		
		criteria.setResultTransformer(Transformers.aliasToBean(DTONivelAutorizacion.class));
		
		ArrayList<DTONivelAutorizacion> lista = (ArrayList<DTONivelAutorizacion>)criteria.list(); 
		
		return lista;
	}

}
