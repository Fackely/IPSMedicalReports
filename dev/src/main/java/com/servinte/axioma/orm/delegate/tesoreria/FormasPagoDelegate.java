package com.servinte.axioma.orm.delegate.tesoreria;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;

import com.princetonsa.dto.tesoreria.DtoFormaPago;
import com.servinte.axioma.orm.FormasPago;
import com.servinte.axioma.orm.FormasPagoHome;
import com.servinte.axioma.orm.TiposDetalleFormaPago;
import com.servinte.axioma.persistencia.UtilidadTransaccion;


public class FormasPagoDelegate extends FormasPagoHome {
	
	
	@SuppressWarnings("unchecked")
	public List<FormasPago> obtenerFormasPagos() 
	{
		return (List<FormasPago>)sessionFactory.getCurrentSession().
		createCriteria(FormasPago.class).
		createAlias("tiposDetalleFormaPago", "tipos").
		addOrder(Order.asc("tipos.prioridad")).
		addOrder(Order.asc("consecutivo")).
		list();
	}
	
	/**
	 * @return lista con las formas de pago activas
	 */
	@SuppressWarnings("unchecked")
	public List<FormasPago> obtenerFormasPagosActivos() 
	{
		
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(FormasPago.class,"formasPago");
	
		
		criteria.createAlias("formasPago.tiposDetalleFormaPago", "tipos").
		addOrder(Order.asc("tipos.prioridad")).
		addOrder(Order.asc("consecutivo"));
		
		//se filtra las formas de pago activas 
		criteria.add(Restrictions.eq("formasPago.activo", true));
	
		return ((List<FormasPago> )criteria.list());
	}
	
	
	
	@SuppressWarnings("unchecked")
	public List<TiposDetalleFormaPago> obtenerTiposDetalleFormaPago()
	{
		return (List<TiposDetalleFormaPago>)sessionFactory.getCurrentSession().
		createCriteria(TiposDetalleFormaPago.class).
		addOrder(Property.forName("codigo").asc()).
		list();
	}
	
	
	@Override
	public FormasPago findById (int consecutivo){
		
		UtilidadTransaccion.getTransaccion().begin();
		FormasPago formasPago = super.findById(consecutivo);
		formasPago.getTiposDetalleFormaPago().getCodigo();

		return formasPago;
	}


	/**
	 * Retorna un listado con las formas de pago registradas en el sistema
	 * seg&uacute;n los atributos pasados en el DtoFormaPago
	 * 
	 * @return List<{@link DtoFormaPago}>
	 */
	@SuppressWarnings("unchecked")
	public List<DtoFormaPago> obtenerFormasPagos(DtoFormaPago formaPagoFiltros)
	{
		Criteria criterios=sessionFactory.getCurrentSession().
		createCriteria(FormasPago.class, "forma_pago")
		.createAlias("forma_pago.tiposDetalleFormaPago", "tipo_detalle");
		
		if(formaPagoFiltros.getConsecutivo()>0)
		{
			criterios.add(Restrictions.eq("forma_pago.consecutivo", formaPagoFiltros.getConsecutivo()));
		}
		if(formaPagoFiltros.isActivo())
		{
			criterios.add(Restrictions.eq("forma_pago.activo", true));
		}
		
		if(formaPagoFiltros.getTipoDetalle()!=null && formaPagoFiltros.getTipoDetalle().getCodigo() > 0){
			
			criterios.add(Restrictions.eq("tipo_detalle.codigo", formaPagoFiltros.getTipoDetalle().getCodigo()));
		}
		
		criterios.setProjection(Projections.projectionList()
				.add( Projections.property("forma_pago.consecutivo"), "consecutivo")
				.add( Projections.property("forma_pago.instituciones.codigo"), "institucion")
				.add( Projections.property("forma_pago.codigo"), "codigo")
				.add( Projections.property("forma_pago.descripcion"), "descripcion")
				.add( Projections.property("forma_pago.activo"), "activo")
				.add( Projections.property("forma_pago.indConsignacion"), "indicativoConsignacion")
				.add( Projections.property("forma_pago.trasladoCajaRecaudo"), "trasladoCajaRecaudo")
				.add( Projections.property("tipo_detalle.codigo"), "codigoTipoDetalle")
				.add( Projections.property("tipo_detalle.descripcion"), "descripcionTipoDetalle")
				.add( Projections.property("tipo_detalle.prioridad"), "prioridadTipoDetalle")
				.add( Projections.property("forma_pago.reqTrasladoCajaRecaudo"), "reqTrasladoCajaRecaudo")
		);
		criterios.setResultTransformer(Transformers.aliasToBean(DtoFormaPago.class));
		return criterios.list();
	}
}
