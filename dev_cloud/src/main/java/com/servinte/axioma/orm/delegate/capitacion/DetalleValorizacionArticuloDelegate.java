package com.servinte.axioma.orm.delegate.capitacion;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.axioma.util.log.Log4JManager;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;

import com.servinte.axioma.dto.capitacion.DtoProductoServicioReporte;
import com.servinte.axioma.orm.DetalleValorizacionArt;
import com.servinte.axioma.orm.DetalleValorizacionArtHome;
import com.servinte.axioma.orm.DetalleValorizacionServ;

/**
 * Clase encargada de ejecutar las transacciones del objeto {@link DetalleValorizacionArt}
 * @author diecorqu
 *
 */
@SuppressWarnings("unchecked")
public class DetalleValorizacionArticuloDelegate extends DetalleValorizacionArtHome {

	/**
	 * Este método retorna la valorizacion por el codigo del detalle
	 * 
	 * @param codigo del detalle de la Clase de inventario
	 * @return DetalleValorizacionArt
	 */
	public DetalleValorizacionArt findById(long codDetalle) {
		return super.findById(codDetalle);
	}	
	
	/**
	 * Este método verifica si existe una valorización para
	 * la clase de inventario dado el código de la parametrización
	 * y el nivel de atención
	 * 
	 * @param codigo de la parametrazación
	 * @param código nivel de atención
	 * @return boolean con el resultado de la operación
	 */
	public boolean existeValorizacionDetalleClaseInventario(long codParametrizacion, long consecutivoNivelAtencion) {
		Criteria criteria = sessionFactory.getCurrentSession()
			.createCriteria(DetalleValorizacionArt.class,"detalleValArt");
		criteria.createAlias("detalleValArt.paramPresupuestosCap", "parametrizacion").
				 createAlias("detalleValArt.nivelAtencion", "nivelAtencion").
				 createAlias("detalleValArt.claseInventario", "claseInventario").
			add(Restrictions.eq("parametrizacion.codigo", codParametrizacion)).
			add(Restrictions.eq("nivelAtencion.consecutivo",consecutivoNivelAtencion));
		ArrayList<DetalleValorizacionArt> lista = (ArrayList<DetalleValorizacionArt>)criteria.setMaxResults(1).list();
		return (lista == null || lista.size() == 0) ? false : true;
	}
		
	/**
	 * Este método retorna una lista con la valorizacion de una parametrización
	 * detallada del nivel de atención de Clase de Inventario
	 * 
	 * @param código del contrato
	 * @param código nivel de atención
	 * @return ArrayList<DetalleValorizacionServ>
	 */
	public ArrayList<DetalleValorizacionArt> obtenerValorizacionDetalleClaseInventario(
			long codParametrizacion, long consecutivoNivelAtencion) {
		Criteria criteria = sessionFactory.getCurrentSession()
			.createCriteria(DetalleValorizacionArt.class,"detalleValArt");
		criteria.createAlias("detalleValArt.paramPresupuestosCap", "parametrizacion").
				 createAlias("detalleValArt.nivelAtencion", "nivelAtencion").
				 createAlias("detalleValArt.claseInventario", "claseInventario").
			add(Restrictions.eq("parametrizacion.codigo", codParametrizacion)).
			add(Restrictions.eq("nivelAtencion.consecutivo",consecutivoNivelAtencion));
		return (ArrayList<DetalleValorizacionArt>)criteria.list();
	}
	
	/**
	 * Este método retorna la valorizacion para el detalle de la clase de inventario
	 * 
	 * @param código de la parametrizacion requerida
	 * @return ArrayList<ValorizacionPresupuesto>
	 */
	public ArrayList<DetalleValorizacionArt> detalleValorizacionArticulo(
			long codigoParametrizacion, long nivelAtencion) {
		Criteria criteria = sessionFactory.getCurrentSession()
			.createCriteria(DetalleValorizacionArt.class,"detalleValArticulo");
			criteria.createAlias("detalleValArticulo.paramPresupuestosCap", "parametrizacion").
			createAlias("detalleValArticulo.nivelAtencion", "nivelAtencion").
			add(Restrictions.eq("parametrizacion.codigo",codigoParametrizacion)).
			add(Restrictions.eq("nivelAtencion.consecutivo", nivelAtencion));
			;
		return (ArrayList<DetalleValorizacionArt>)criteria.list();
	}	

	/**
	 * Este método guarda la valorización del detalle ingresado 
	 * 
	 * @param Valorizacion del detalle
	 * @return boolean con el resultado de la operación de guardado
	 */
	public boolean guardarValorizacionDetalleArticulo(
			ArrayList<DetalleValorizacionArt> detallesValorizacionArticulo) {
		boolean save = false;
		try{
			for (DetalleValorizacionArt valorizacion : detallesValorizacionArticulo) {
				super.persist(valorizacion);
				save = true;
			}
		}catch (Exception e) {
			save = false;
			Log4JManager.error("No se pudo almacenar la valorización detallada del artículo ",e);
		}		
		return save;
	}	
	
	/**
	 * Este método modifica la valorización del detalle de la 
	 * clase de inventario ingresado
	 * 
	 * @param Parametrizacion Presupuesto
	 * @return boolean con el resultado de la operación de guardado
	 */
	public DetalleValorizacionArt modificarValorizacionDetalleClaseInventario(
			DetalleValorizacionArt detallesValorizacionClaseInventario) {
		return super.merge(detallesValorizacionClaseInventario);
	}	
	
	/**
	 * Este método elimina la valorización del detalle del artículo ingresado
	 * 
	 * @param código valorización a eliminar
	 * @return boolean con el resultado de la operación de guardado
	 */
	public boolean eliminarValorizacionDetalleArticulo(int codigo) {
		return false;
	}	
	
	/**
	 * Este método verifica si existe una parametrizacion detallada de presupuesto 
	 * capitado para el contrato y la fecha de vigencia ingresados
	 * 
	 * @author Ricardo Ruiz
	 * @param codigo del contrato
	 * @param mesAnio
	 * @return boolean con el resultado de la operación
	 */
	public boolean existeValorizacionDetalleClaseInventario(int codigoContrato, Calendar mesAnio) {
		Criteria criteria = sessionFactory.getCurrentSession()
			.createCriteria(DetalleValorizacionArt.class,"detalleValArt");
		criteria.createAlias("detalleValArt.paramPresupuestosCap", "parametrizacion").
				 createAlias("parametrizacion.contratos", "contrato").
			add(Restrictions.eq("contrato.codigo", codigoContrato)).
			add(Restrictions.eq("parametrizacion.anioVigencia",String.valueOf(mesAnio.get(Calendar.YEAR)))).
			add(Restrictions.eq("detalleValArt.mes",mesAnio.get(Calendar.MONTH)));
		ArrayList<DetalleValorizacionServ> lista = (ArrayList<DetalleValorizacionServ>)criteria.list();
		return (lista == null || lista.size() == 0) ? false : true;
	}
	
	/**
	 * Obtiene los servicios presupuestados por
	 * nivel de atención de un contrato para un mes determinado. 
	 * 
	 * @author Ricardo Ruiz
	 * @param codigoContrato
	 * @param consecutivoNivel
	 * @param mesAnio
	 * @return ArrayList<Convenios>
	*/
	public List<DtoProductoServicioReporte> obtenerArticulosPresupuestadosPorNivelAtencionPorContrato(int codigoContrato, long consecutivoNivel,
										Calendar mesAnio)
	{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(DetalleValorizacionArt.class, "detalleValorizacionArticulo");
			criteria.createAlias("detalleValorizacionArticulo.nivelAtencion", "nivelAtencion");
			criteria.createAlias("detalleValorizacionArticulo.paramPresupuestosCap", "presupuesto");
			criteria.createAlias("detalleValorizacionArticulo.claseInventario", "claseInventario");
			criteria.createAlias("presupuesto.contratos", "contrato");
			
			criteria.setProjection(Projections.projectionList()
					.add(Projections.property("claseInventario.codigo"),"codigo")
					.add(Projections.property("claseInventario.nombre"),"nombre")
					.add(Projections.property("detalleValorizacionArticulo.valorGasto"),"totalPresupuestado"));
			
			criteria.add(Restrictions.eq("detalleValorizacionArticulo.mes"	, mesAnio.get(Calendar.MONTH)))
					.add(Restrictions.eq("nivelAtencion.consecutivo"	, consecutivoNivel))
					.add(Restrictions.eq("contrato.codigo" , codigoContrato))
					.add(Restrictions.eq("presupuesto.anioVigencia"	, String.valueOf(mesAnio.get(Calendar.YEAR))))
					.addOrder(Order.asc("claseInventario.nombre"))
					.setResultTransformer(Transformers.aliasToBean(DtoProductoServicioReporte.class));
	
			return criteria.list();
	}
}
