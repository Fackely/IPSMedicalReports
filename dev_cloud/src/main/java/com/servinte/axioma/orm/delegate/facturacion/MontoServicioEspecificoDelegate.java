package com.servinte.axioma.orm.delegate.facturacion;

import java.util.ArrayList;

import org.axioma.util.log.Log4JManager;
import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;

import com.princetonsa.dto.facturacion.DTOBusquedaMontoServicioEspecifico;
import com.servinte.axioma.orm.MontoServicioEspecifico;
import com.servinte.axioma.orm.MontoServicioEspecificoHome;

/**
 * Esta clase se encarga de
 * @author Angela Maria Aguirre
 * @since 26/08/2010
 */
public class MontoServicioEspecificoDelegate extends
		MontoServicioEspecificoHome {
	
	/**
	 * 
	 * Este método se encarga de consultar todos los
	 * servicios específicos relacionados al detalle del monto de cobro
	 * 
	 * @param int idDetalle
	 * @return ArrayList<DTOBusquedaMontoServicioEspecifico>
	 * @author Angela Aguirre
	 * 
	 */
	public ArrayList<DTOBusquedaMontoServicioEspecifico> obtenerServiciosPorDetalleID(int idDetalle,int codigoTarifario){
					
		Criteria criteria = sessionFactory.getCurrentSession()
		.createCriteria(MontoServicioEspecifico.class,"servicioEspecifico")
		.createAlias("servicioEspecifico.detalleMonto", "detalle")
		.createAlias("servicioEspecifico.servicios", "servicio")
		.createAlias("servicio.referenciasServicios", "referenciaServicio")
		.createAlias("referenciaServicio.tarifariosOficiales", "tarifario");
		
		criteria.add(Restrictions.eq("detalle.detalleCodigo", idDetalle));
		criteria.add(Restrictions.eq("tarifario.codigo", codigoTarifario));
		
		criteria.setProjection(Projections.projectionList()			
				.add(Projections.property("servicioEspecifico.codigo"),"codigoServicioEspecifico")
				.add(Projections.property("servicioEspecifico.cantidadServicio"),"cantidadServicio")
				.add(Projections.property("servicioEspecifico.cantidadMonto"),"cantidadMonto")
				.add(Projections.property("servicioEspecifico.valorMonto"),"valorMonto")
				.add(Projections.property("detalle.detalleCodigo"),"detalleMonto")
				.add(Projections.property("servicio.codigo"),"codigoServicio")
				.add(Projections.property("referenciaServicio.descripcion"),"descripcionServicio"));
		
		criteria.setResultTransformer(Transformers.aliasToBean(DTOBusquedaMontoServicioEspecifico.class));
		
		ArrayList<DTOBusquedaMontoServicioEspecifico> listaServicio = (
				ArrayList<DTOBusquedaMontoServicioEspecifico>)criteria.list();
		
		return listaServicio;
	}	
	
	/**
	 * 
	 * Este método se encarga de eliminar el registro
	 * de un servicio específico de montos de cobro
	 * 
	 * @param int
	 * @return boolean 
	 * @author, Angela Maria Aguirre
	 *
	 */
	public boolean eliminarServicioEspecifico(int id){
		boolean save = true;					
		try{
			MontoServicioEspecifico registro = super.findById(id);		
			super.delete(registro);
		} catch (Exception e) {
			save = false;
			Log4JManager.error("No se pudo eliminar el registro de " +
					"servicio específico del detalle de monto de cobro: ",e);
		}				
		return save;		
	} 

}
