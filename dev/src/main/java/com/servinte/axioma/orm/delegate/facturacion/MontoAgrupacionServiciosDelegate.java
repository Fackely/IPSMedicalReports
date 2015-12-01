package com.servinte.axioma.orm.delegate.facturacion;

import java.util.ArrayList;

import org.axioma.util.log.Log4JManager;
import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;

import com.princetonsa.dto.facturacion.DTOBusquedaMontoAgrupacionServicio;
import com.servinte.axioma.orm.MontoAgrupacionServicios;
import com.servinte.axioma.orm.MontoAgrupacionServiciosHome;

/**
 * Esta clase se encarga de ejecutar los métodos de negocio
 * de la entidad MontoAgrupacionServicios
 * @author Angela Maria Aguirre
 * @since 26/08/2010
 */
public class MontoAgrupacionServiciosDelegate extends
		MontoAgrupacionServiciosHome {
	
	/**
	 * 
	 * Este método se encarga de consultar todos las agrupaciones
	 * de servicios relacionadas a un detalle específico
	 * 
	 * @param int idDetalle
	 * @return ArrayList<DTOBusquedaMontoAgrupacionServicio>
	 * @author Angela Aguirre
	 * 
	 */
	public ArrayList<DTOBusquedaMontoAgrupacionServicio> obtenerServiciosPorDetalleID(int idDetalle){
		
		Criteria criteria = sessionFactory.getCurrentSession()
		.createCriteria(MontoAgrupacionServicios.class,"agrupacionServicio")
		.createAlias("agrupacionServicio.detalleMonto", "detalle")
		.createAlias("agrupacionServicio.tiposServicio", "tipoServicio", Criteria.LEFT_JOIN)
		.createAlias("agrupacionServicio.gruposServicios", "grupoServicios", Criteria.LEFT_JOIN)
		.createAlias("agrupacionServicio.especialidades", "especialidad", Criteria.LEFT_JOIN);
		
		criteria.add(Restrictions.eq("detalle.detalleCodigo", idDetalle));
		
		criteria.setProjection(Projections.projectionList()			
				.add(Projections.property("agrupacionServicio.codigo"),"codigoAgrupacionServicio")
				.add(Projections.property("tipoServicio.acronimo"),"acronimoTipoServicio")
				.add(Projections.property("tipoServicio.nombre"),"nombreTipoServicio")				
				.add(Projections.property("grupoServicios.codigo"),"codigoGrupoServicio")
				.add(Projections.property("grupoServicios.descripcion"),"descripcionGrupoServicio")
				.add(Projections.property("especialidad.codigo"),"codigoEspecialidad")
				.add(Projections.property("especialidad.nombre"),"nombreEspecialidad")				
				.add(Projections.property("agrupacionServicio.cantidadServicio"),"cantidadServicio") 
				.add(Projections.property("agrupacionServicio.cantidadMonto"),"cantidadMonto") 
				.add(Projections.property("agrupacionServicio.valorMonto"),"valorMonto")
				.add(Projections.property("detalle.detalleCodigo"),"detalleCodigo")
		);
		
		criteria.setResultTransformer(Transformers.aliasToBean(DTOBusquedaMontoAgrupacionServicio.class));		
		ArrayList<DTOBusquedaMontoAgrupacionServicio> lista = (
				ArrayList<DTOBusquedaMontoAgrupacionServicio>)criteria.list();
		
		return lista;
		
	}
	
	/**
	 * 
	 * Este método se encarga de eliminar el registro
	 * de una agrupación de servicios de montos de cobro
	 * 
	 * @param int
	 * @return boolean 
	 * @author, Angela Maria Aguirre
	 *
	 */
	public boolean eliminarAgrupacionServicio(int id){
		boolean save = true;					
		try{
			MontoAgrupacionServicios registro = super.findById(id);		
			super.delete(registro);
		} catch (Exception e) {
			save = false;
			Log4JManager.error("No se pudo eliminar el registro de " +
					"agrupación de servicios del detalle de monto de cobro: ",e);
		}				
		return save;		
	} 

}
