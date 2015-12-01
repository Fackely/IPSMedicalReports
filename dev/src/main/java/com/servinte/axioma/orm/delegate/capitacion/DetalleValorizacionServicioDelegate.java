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
import com.servinte.axioma.orm.DetalleValorizacionServ;
import com.servinte.axioma.orm.DetalleValorizacionServHome;

/**
 * Clase encargada de ejecutar las transacciones del objeto {@link DetalleValorizacionServ}
 * @author diecorqu
 *
 */
@SuppressWarnings("unchecked")
public class DetalleValorizacionServicioDelegate extends DetalleValorizacionServHome {

	/**
	 * Este método retorna la valorizacion por el codigo del detalle
	 * 
	 * @param codigo de detalle de GrupoServicio
	 * @return ValorizacionPresupuestoCapitado
	 */
	public DetalleValorizacionServ findById(long codDetalle) {
		return super.findById(codDetalle);
	}	
	
	/**
	 * Este método verifica si existe una valorización para
	 * el grupo de servicio dado el código de la parametrización
	 * y el nivel de atención
	 * 
	 * @param codigo de la parametrazación
	 * @param código nivel de atención
	 * @return boolean con el resultado de la operación
	 */
	public boolean existeValorizacionDetalleGrupoServicio(long codParametrizacion, long consecutivoNivelAtencion) {
		Criteria criteria = sessionFactory.getCurrentSession()
			.createCriteria(DetalleValorizacionServ.class,"detalleValServ");
		criteria.createAlias("detalleValServ.paramPresupuestosCap", "parametrizacion").
				 createAlias("detalleValServ.nivelAtencion", "nivelAtencion").
				 createAlias("detalleValServ.gruposServicios", "grupoServicio").
			add(Restrictions.eq("parametrizacion.codigo", codParametrizacion)).
			add(Restrictions.eq("nivelAtencion.consecutivo",consecutivoNivelAtencion));
		ArrayList<DetalleValorizacionServ> lista = (ArrayList<DetalleValorizacionServ>)criteria.setMaxResults(1).list();
		return (lista == null || lista.size() == 0) ? false : true;
	}

	/**
	 * Este método retorna una lista con la valorizacion de una parametrización
	 * detallada del nivel de atención de grupo de servicios
	 * 
	 * @param codigo de la parametrazación
	 * @param código nivel de atención
	 * @return ArrayList<DetalleValorizacionServ>
	 */
	public ArrayList<DetalleValorizacionServ> obtenerValorizacionDetalleGrupoServicio(
			long codParametrizacion, long consecutivoNivelAtencion) {
		Criteria criteria = sessionFactory.getCurrentSession()
			.createCriteria(DetalleValorizacionServ.class,"detalleValServ");
		criteria.createAlias("detalleValServ.paramPresupuestosCap", "parametrizacion").
				 createAlias("detalleValServ.nivelAtencion", "nivelAtencion").
				 createAlias("detalleValServ.gruposServicios", "grupoServicio").
			add(Restrictions.eq("parametrizacion.codigo", codParametrizacion)).
			add(Restrictions.eq("nivelAtencion.consecutivo",consecutivoNivelAtencion));
		return (ArrayList<DetalleValorizacionServ>)criteria.list();
	}

	
	/**
	 * Este método retorna la valorizacion para el detalle del grupo de servicio
	 * 
	 * @param código de la valorizacion del grupo de servicio requerida
	 * @return ArrayList<DetalleValorizacionServ>
	 */
	public ArrayList<DetalleValorizacionServ> detalleValorizacionServicio(
			long codigoParametrizacion, long nivelAtencion) {
		Criteria criteria = sessionFactory.getCurrentSession()
			.createCriteria(DetalleValorizacionServ.class,"detalleValServicio");
			criteria.createAlias("detalleValServicio.paramPresupuestosCap", "parametrizacion").
			createAlias("detalleValServicio.nivelAtencion", "nivelServicio").
			add(Restrictions.eq("parametrizacion.codigo",codigoParametrizacion)).
			add(Restrictions.eq("nivelServicio.consecutivo", nivelAtencion));
		return (ArrayList<DetalleValorizacionServ>)criteria.list();
	}	

	/**
	 * Este método guarda la valorización del detalle ingresado 
	 * 
	 * @param Valorizacion del detalle
	 * @return boolean con el resultado de la operación de guardado
	 */
	public boolean guardarValorizacionDetalleServicio(
			ArrayList<DetalleValorizacionServ> detallesValorizacionServicio) {
		boolean save = false;
		try{
			for (DetalleValorizacionServ valorizacion : detallesValorizacionServicio) {
				super.persist(valorizacion);
				save = true;
			}
		}catch (Exception e) {
			save = false;
			Log4JManager.error("No se pudo almacenar la valorización detallada del servicio ",e);
		}		
		return save;
	}	
	
	/**
	 * Este método modifica la valorizacion del detalle del grupo de servicio ingresado
	 * 
	 * @param Parametrizacion Presupuesto
	 * @return boolean con el resultado de la operación de guardado
	 */
	public DetalleValorizacionServ modificarValorizacionDetalleServicio(
			DetalleValorizacionServ detallesValorizacionServicio) {
		return super.merge(detallesValorizacionServicio);
	}	
	
	/**
	 * Este método elimina la valorizacion del detalle del servicio ingresado
	 * 
	 * @param código parametrización a eliminar
	 * @return boolean con el resultado de la operación de guardado
	 */
	public boolean eliminarValorizacionDetalleServicio(int codigo) {
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
	public boolean existeValorizacionDetalleGrupoServicio(int codigoContrato, Calendar mesAnio) {
		Criteria criteria = sessionFactory.getCurrentSession()
			.createCriteria(DetalleValorizacionServ.class,"detalleValServ");
		criteria.createAlias("detalleValServ.paramPresupuestosCap", "parametrizacion").
				 createAlias("parametrizacion.contratos", "contrato").
			add(Restrictions.eq("contrato.codigo", codigoContrato)).
			add(Restrictions.eq("parametrizacion.anioVigencia",String.valueOf(mesAnio.get(Calendar.YEAR)))).
			add(Restrictions.eq("detalleValServ.mes",mesAnio.get(Calendar.MONTH)));
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
	public List<DtoProductoServicioReporte> obtenerServiciosPresupuestadosPorNivelAtencionPorContrato(int codigoContrato, long consecutivoNivel,
										Calendar mesAnio)
	{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(DetalleValorizacionServ.class, "detalleValorizacionServicio");
			criteria.createAlias("detalleValorizacionServicio.nivelAtencion", "nivelAtencion");
			criteria.createAlias("detalleValorizacionServicio.paramPresupuestosCap", "presupuesto");
			criteria.createAlias("detalleValorizacionServicio.gruposServicios", "grupoServicio");
			criteria.createAlias("presupuesto.contratos", "contrato");
			
			criteria.setProjection(Projections.projectionList()
					.add(Projections.property("grupoServicio.codigo"),"codigo")
					.add(Projections.property("grupoServicio.descripcion"),"nombre")
					.add(Projections.property("detalleValorizacionServicio.valorGasto"),"totalPresupuestado"));
			
			criteria.add(Restrictions.eq("detalleValorizacionServicio.mes"	, mesAnio.get(Calendar.MONTH)))
					.add(Restrictions.eq("nivelAtencion.consecutivo"	, consecutivoNivel))
					.add(Restrictions.eq("contrato.codigo" , codigoContrato))
					.add(Restrictions.eq("presupuesto.anioVigencia"	, String.valueOf(mesAnio.get(Calendar.YEAR))))
					.addOrder(Order.asc("grupoServicio.descripcion"))
					.setResultTransformer(Transformers.aliasToBean(DtoProductoServicioReporte.class));
	
			return criteria.list();
	}
	
	
}
