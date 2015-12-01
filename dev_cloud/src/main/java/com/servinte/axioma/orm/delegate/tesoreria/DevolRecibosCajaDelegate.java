package com.servinte.axioma.orm.delegate.tesoreria;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadFecha;

import com.princetonsa.dto.tesoreria.DtoReciboDevolucion;
import com.servinte.axioma.orm.DevolRecibosCaja;
import com.servinte.axioma.orm.DevolRecibosCajaHome;
import com.servinte.axioma.orm.MovimientosCaja;
import com.servinte.axioma.orm.TurnoDeCaja;

/**
 * 
 * Esta clase se encarga de manejar las transaccciones relacionadas
 * con el objeto {@link DevolRecibosCaja}.
 * 
 * @author Jorge Armando Agudelo Quintero
 *
 */

public class DevolRecibosCajaDelegate extends DevolRecibosCajaHome {
	

	/**
	 * Listado con las devoluciones asociadas a los recibos de caja, ordenadas
	 * ascendentemente por consecutivo.
	 * Se obtienen todas la devoluciones del último turno que afecta la devolución segun Tarea 180061  Cambio realizado por Ricardo Ruiz.
	 * 
	 * Se tiene en cuenta las devoluciones que estan en estado Aprobado y Anulado. Si el movimiento de caja se encuentra registrado
	 * en el sistema, se involucra en la b&uacute;squeda los filtros de fecha y hora de generaci&oacute;n de la devoluci&oacute;n.
	 * 
	 * 
	 * @param movimientosCaja
	 * @return List<{@link DtoReciboDevolucion}>
	 */
	public List<DtoReciboDevolucion> obtenerDevolRecibosCajaXMovimientoCaja(MovimientosCaja movimientosCaja)
	{
		List<DtoReciboDevolucion> devolucionesRecibosCajas = obtenerDevolucioneRecibosXTurnoXMovimientoXEstado(movimientosCaja , ConstantesIntegridadDominio.acronimoEstadoAprobado, "fechaAprobacion");
		devolucionesRecibosCajas.addAll(obtenerDevolucioneRecibosXTurnoXMovimientoXEstado(movimientosCaja, ConstantesIntegridadDominio.acronimoEstadoAnulado, "fechaAnulacion"));
		return devolucionesRecibosCajas;
	}
	
	/**
	 * Listado con las devoluciones asociadas a los recibos de caja, ordenadas
	 * ascendentemente por consecutivo.
	 * Se obtienen todas la devoluciones del último turno que afecta la devolución segun Tarea 180061  Cambio realizado por Ricardo Ruiz.
	 * 
	 * Se tiene en cuenta las devoluciones que estan en estado Aprobado y Anulado. Si el movimiento de caja se encuentra registrado
	 * en el sistema, se involucra en la b&uacute;squeda los filtros de fecha y hora de generaci&oacute;n de la devoluci&oacute;n.
	 * 
	 * 
	 * @param movimientosCaja
	 * @return List<{@link DtoReciboDevolucion}>
	 */
	public List<DtoReciboDevolucion> obtenerDevolRecibosCajaXMovimientoCajaFecha(MovimientosCaja movimientosCaja)
	{
		List<DtoReciboDevolucion> devolucionesRecibosCajas = obtenerDevolucioneRecibosXTurnoXMovimientoXEstadoFecha(movimientosCaja , ConstantesIntegridadDominio.acronimoEstadoAprobado, "fechaAprobacion");
		devolucionesRecibosCajas.addAll(obtenerDevolucioneRecibosXTurnoXMovimientoXEstadoFecha(movimientosCaja, ConstantesIntegridadDominio.acronimoEstadoAnulado, "fechaAnulacion"));
		return devolucionesRecibosCajas;
	}
	
	/**
	 * M&eacute;todo que retorna un listado con las devoluciones asociadas a una caja, a un cajero y a un turno
	 * Este turno es el último que afecta la devolución segun Tarea 180061  Cambio realizado por Ricardo Ruiz.
	 * Se involucra la fecha y la hora en la b&uacute;squeda y se compara con la fecha de devoluci&oacute;n, cuando el 
	 * movimiento de caja se encuentra registrado en el sistema.
	 * 
	 * @param movimientosCaja
	 * @param fecha
	 * @param estado
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<DtoReciboDevolucion> obtenerDevolucioneRecibosXTurnoXMovimientoXEstado(MovimientosCaja movimientosCaja, String estado, String fechaSegunEstado) 
	{
	
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(DevolRecibosCaja.class, "drc")
		  .createAlias("recibosCaja", "rc")
		  .createAlias("rc.detalleConceptosRcs", "detcon")
		  .createAlias("formasPago", "forpag")
		  .createAlias("drc.turnoDeCaja", "turnoDeCaja")
		  
		  .setProjection( Projections.projectionList()
				  .add( Projections.property("codigo"), "codigoDevolucion" )
				  .add( Projections.property("consecutivo"), "consecutivoDevolucion")
				  .add( Projections.property("estado"), "acronimoEstadoDevolucion" )
				  .add( Projections.property("detcon.nombreBeneficiario"), "recibidoDe" )
				  .add( Projections.property("detcon.numeroIdBeneficiario"), "numIdentificacion" )
				  .add( Projections.property("forpag.descripcion"), "formaPago" )
	              .add( Projections.property("forpag.consecutivo"), "tipoFormaPago" )
				  .add( Projections.property("valorDevolucion"), "valor" )
				  .add( Projections.property("rc.id.numeroReciboCaja"), "numeroReciboCaja" )
	  			)
			.add(Restrictions.eq("turnoDeCaja.codigoPk", movimientosCaja.getTurnoDeCaja().getCodigoPk()))
			.add(Restrictions.eq("drc.estado",estado))
			.addOrder(Order.asc("consecutivo"));

		if(movimientosCaja.getCodigoPk()>0){
		
			if(movimientosCaja.getFecha()!=null){

				List<DevolRecibosCaja> devolucionesFiltradasFecha = sessionFactory.getCurrentSession()
				.createCriteria(DevolRecibosCaja.class, "drc")
				  .createAlias("recibosCaja", "rc")
				  .createAlias("drc.turnoDeCaja", "turnoDeCaja")
				  .add(Restrictions.eq("turnoDeCaja.codigoPk", movimientosCaja.getTurnoDeCaja().getCodigoPk()))
				  .add(Restrictions.eq("drc.estado",estado))
				  .add(Restrictions.le(fechaSegunEstado, movimientosCaja.getFecha())).list();
					
				ArrayList<Integer> codigosDevolRecibos =  new ArrayList<Integer>();
				codigosDevolRecibos.add(ConstantesBD.codigoNuncaValido);
				
				String hora = "";
				Date fecha = new Date();
				
				for (DevolRecibosCaja devolucion : devolucionesFiltradasFecha) {
					
					if(estado.equals(ConstantesIntegridadDominio.acronimoEstadoAprobado)){
						
						fecha = devolucion.getFechaAprobacion();
						hora = devolucion.getHoraAprobacion();
						
					}else if(estado.equals(ConstantesIntegridadDominio.acronimoEstadoAnulado)){
						
						fecha = devolucion.getFechaAnulacion();
						hora = devolucion.getHoraAnulacion();
					}
					
					if(UtilidadFecha.esHoraMenorIgualQueOtraReferencia(hora , movimientosCaja.getHora())){
						
						codigosDevolRecibos.add(devolucion.getCodigo());
					
					}else if(UtilidadFecha.esFechaMenorQueOtraReferencia(UtilidadFecha.conversionFormatoFechaAAp(fecha), UtilidadFecha.conversionFormatoFechaAAp( movimientosCaja.getFecha()))){
						
						codigosDevolRecibos.add(devolucion.getCodigo());
					}
				}
			
				criteria.add(Restrictions.in("codigo", codigosDevolRecibos));	
			}
		}
		
		List<DtoReciboDevolucion> lista = criteria.setResultTransformer( Transformers.aliasToBean(DtoReciboDevolucion.class))
		.list();
		
		return lista;
	}
	
	/**
	 * M&eacute;todo que retorna un listado con las devoluciones asociadas a una caja, a un cajero y a un turno
	 * Este turno es el último que afecta la devolución segun Tarea 180061  Cambio realizado por Ricardo Ruiz.
	 * Se involucra la fecha y la hora en la b&uacute;squeda y se compara con la fecha de devoluci&oacute;n, cuando el 
	 * movimiento de caja se encuentra registrado en el sistema.
	 * 
	 * @param movimientosCaja
	 * @param fecha
	 * @param estado
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<DtoReciboDevolucion> obtenerDevolucioneRecibosXTurnoXMovimientoXEstadoFecha(MovimientosCaja movimientosCaja, String estado, String fechaSegunEstado) 
	{

		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(DevolRecibosCaja.class, "drc")
		.createAlias("recibosCaja", "rc")
		.createAlias("rc.detalleConceptosRcs", "detcon")
		.createAlias("formasPago", "forpag")
		.createAlias("drc.turnoDeCaja", "turnoDeCaja")

		.setProjection( Projections.projectionList()
				.add( Projections.property("codigo"), "codigoDevolucion" )
				.add( Projections.property("consecutivo"), "consecutivoDevolucion")
				.add( Projections.property("estado"), "acronimoEstadoDevolucion" )
				.add( Projections.property("detcon.nombreBeneficiario"), "recibidoDe" )
				.add( Projections.property("detcon.numeroIdBeneficiario"), "numIdentificacion" )
				.add( Projections.property("forpag.descripcion"), "formaPago" )
				.add( Projections.property("forpag.consecutivo"), "tipoFormaPago" )
				.add( Projections.property("valorDevolucion"), "valor" )
				.add( Projections.property("rc.id.numeroReciboCaja"), "numeroReciboCaja" )
		)
		.add(Restrictions.eq("turnoDeCaja.codigoPk", movimientosCaja.getTurnoDeCaja().getCodigoPk()))
		.add(Restrictions.eq("drc.estado",estado))
		.addOrder(Order.asc("consecutivo"));



		if(movimientosCaja.getFechaMovimiento() != null){

			List<DevolRecibosCaja> devolucionesFiltradasFecha = sessionFactory.getCurrentSession()
			.createCriteria(DevolRecibosCaja.class, "drc")
			.createAlias("recibosCaja", "rc")
			.createAlias("drc.turnoDeCaja", "turnoDeCaja")
			.add(Restrictions.eq("turnoDeCaja.codigoPk", movimientosCaja.getTurnoDeCaja().getCodigoPk()))
			.add(Restrictions.eq("drc.estado",estado))
			.add(Restrictions.eq(fechaSegunEstado, movimientosCaja.getFechaMovimiento())).list();

			ArrayList<Integer> codigosDevolRecibos =  new ArrayList<Integer>();
			codigosDevolRecibos.add(ConstantesBD.codigoNuncaValido);

			for (DevolRecibosCaja devolucion : devolucionesFiltradasFecha) {
				codigosDevolRecibos.add(devolucion.getCodigo());
			}

			criteria.add(Restrictions.in("codigo", codigosDevolRecibos));	
		}


		List<DtoReciboDevolucion> lista = criteria.setResultTransformer( Transformers.aliasToBean(DtoReciboDevolucion.class))
		.list();

		return lista;
	}
	
	/**
	 * M&eacute;todo que retorna el total de las devoluciones de los recibos de caja realizados en un turno espec&iacute;fico
	 * teniendo en cuenta un estado. Se totalizan las devoluciones realizadas por cada una de las formas de pago.
	 * 
	 * Este turno es el último que afecta la devolución segun Tarea 180061  Cambio realizado por Ricardo Ruiz.
	 * 
	 * @param turnoDeCaja
	 * @param estado
	 * @return total de las devoluciones de los recibos de caja realizados en un turno espec&iacute;fico por forma de pago
	 */
	@SuppressWarnings("unchecked")
	public List<DtoReciboDevolucion> obtenerTotalDevolucionesDisponiblesXEstadoXTurnoCaja(TurnoDeCaja turnoDeCaja, String estado) 
	{
		return (List<DtoReciboDevolucion>) sessionFactory.getCurrentSession().createCriteria(DevolRecibosCaja.class, "drc")
			.createAlias("formasPago", "fp")
			.createAlias("recibosCaja", "rc")
			.createAlias("drc.turnoDeCaja", "turnoDeCaja")

			.setProjection( Projections.projectionList()
					.add( Projections.sum("valorDevolucion"), "valor")
					.add( Projections.property("fp.descripcion"), "formaPago" )
	                .add( Projections.property("fp.consecutivo"), "tipoFormaPago" )
					.add(Projections.groupProperty("fp.consecutivo"))
					.add(Projections.groupProperty("fp.descripcion"))
	  		)
	  		
	  		.add(Restrictions.eq("turnoDeCaja.codigoPk", turnoDeCaja.getCodigoPk()))
			.add(Restrictions.eq("drc.estado",estado))
			.setResultTransformer( Transformers.aliasToBean(DtoReciboDevolucion.class))
			.list();

	}
	

	
	/**
	 * M&eacute;todo que se encarga de consultar la fecha m&aacute;xima registrada para una
	 * devoluci&oacute;n de un recibo de caja que est&aacute; asociado a un turno de caja espec&iacute;fico
	 * Este turno es el último que afecta la devolución segun Tarea 180061  Cambio realizado por Ricardo Ruiz.
	 * Se toman en cuenta todas las devoluciones sin tener en cuenta el estado.
	 * 
	 * @param turnoDeCaja
	 * @return Date, con la fecha m&aacute;xima registrada para este movimiento
	 */
	public Date obtenerFechaUltimoMovimientoDevoluciones(TurnoDeCaja turnoDeCaja){
		
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(DevolRecibosCaja.class, "drc");
		criteria.createAlias("recibosCaja", "reciboCaja")
				.createAlias("drc.turnoDeCaja", "turnoDeCaja")
				.add(Restrictions.eq("turnoDeCaja.codigoPk", turnoDeCaja.getCodigoPk()));
		
		criteria.setProjection(Projections.max("drc.fechaDevolucion"));
		
		Date fechaMayor = criteria.uniqueResult() != null ? (Date) criteria.uniqueResult() : new Date(ConstantesBD.codigoNuncaValidoLong);
		
		criteria.setProjection(Projections.max("drc.fechaAprobacion"));
		 
		Date fechaAprobacion = criteria.uniqueResult() != null ? (Date) criteria.uniqueResult() : new Date(ConstantesBD.codigoNuncaValidoLong);
		
		criteria.setProjection(Projections.max("drc.fechaAnulacion"));
		
		Date fechaAnulacion = criteria.uniqueResult() != null ? (Date) criteria.uniqueResult() : new Date(ConstantesBD.codigoNuncaValidoLong);
		
		if(fechaAprobacion.getTime() > fechaMayor.getTime() && fechaAprobacion.getTime() > fechaAnulacion.getTime()){
			
			return fechaAprobacion;

		}else if(fechaAnulacion.getTime() > fechaMayor.getTime() && fechaAnulacion.getTime() > fechaAprobacion.getTime()){
			
			return fechaAnulacion;
			
		}else{
			return fechaMayor;
		}
	}
	
	
//	public static void main (String args[]){
//		
//		UtilidadTransaccion.getTransaccion().begin();
//		
//		TurnoDeCaja turnoDeCaja = new TurnoDeCajaDelegate().findById(234);
//
//		//MovimientosCaja movimientosCaja = new MovimientosCaja();
//	   // movimientosCaja.setTurnoDeCaja(turnoDeCaja);
//		
//		//movimientosCaja = new MovimientosCajaHome().findById(703);
//		
//		DevolRecibosCajaDelegate x = new DevolRecibosCajaDelegate();
//		
//		
//		List<DtoReciboDevolucion> l = x.obtenerTotalDevolucionesDisponiblesXEstadoXTurnoCaja(turnoDeCaja, ConstantesIntegridadDominio.acronimoEstadoAprobado);
//		
//		
//		for (DtoReciboDevolucion dtoReciboDevolucion : l) {
//			
//			Log4JManager.info(dtoReciboDevolucion.getFormaPago() + " - " + dtoReciboDevolucion.getValor());
//		}
//		
//		UtilidadTransaccion.getTransaccion().commit();
//	}
	
//	/**
//	 * Actualiza el estado de las devoluciones que han sido relacionadas (afectando el efectivo entregado) en un movimiento de caja
//	 * de tipo Arqueo (Arego Entrega Parcial - Cierre Turno de Caja)
//	
//	 * @param listaDtoDevolucionesRecibos
//	 * @return boolean indicando si la actualizaci&oacute;n fue realizada exitosamente
//	 */
//	public boolean cambiarEstadoArqueoDevoluciones (List<DtoReciboDevolucion> listaDtoDevolucionesRecibos){
//		
//		boolean update =true;
//		
//		if (listaDtoDevolucionesRecibos!=null && listaDtoDevolucionesRecibos.size()>0){
//			
//			for (DtoReciboDevolucion dtoReciboDevolucion : listaDtoDevolucionesRecibos) {
//				
//				if(!UtilidadTexto.isEmpty(dtoReciboDevolucion.getAcronimoEstadoDevolucion()) 
//				&& ConstantesIntegridadDominio.acronimoEstadoAprobado.equals(dtoReciboDevolucion.getAcronimoEstadoDevolucion())){
//					
//					DevolRecibosCaja devolRecibosCaja = super.findById(dtoReciboDevolucion.getCodigoDevolucion());
//					
//					if(ConstantesBD.acronimoNo.equals(devolRecibosCaja.getEstadoArqueo())){
//						Log4JManager.info("estoy actualizando las devoluciones " + devolRecibosCaja.getCodigo());
//						devolRecibosCaja.setEstadoArqueo(ConstantesBD.acronimoSi);
//					}
//				}
//			}
//		}else{
//			update = false;
//		}
//			
//		return update;
//	}
	
}