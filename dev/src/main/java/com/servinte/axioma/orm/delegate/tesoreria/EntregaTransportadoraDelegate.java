package com.servinte.axioma.orm.delegate.tesoreria;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadFecha;

import com.princetonsa.dto.tesoreria.DtoEntregaCaja;
import com.servinte.axioma.orm.EntregaTransportadora;
import com.servinte.axioma.orm.EntregaTransportadoraHome;
import com.servinte.axioma.orm.MovimientosCaja;
import com.servinte.axioma.orm.TurnoDeCaja;


/**
 * 
 * Esta clase se encarga de manejar las transaccciones relacionadas
 * con el objeto {@link EntregaTransportadora}
 * 
 * @author Jorge Armando Agudelo Quintero
 *
 */

public class EntregaTransportadoraDelegate extends EntregaTransportadoraHome {
	

	/**
	 * M&eacute;todo que retorna una entrega a Transportadora de valores asociada a un c&oacute;digo espec&iacute;fico.
	 * 
	 * @param codigoPk
	 * @return
	 */
	public EntregaTransportadora obtenerEntregaTransportadoraPorCodigo (long codigoPk){
		
		EntregaTransportadora entregaTransportadora = super.findById(codigoPk);
		
		if(entregaTransportadora!=null){
			
			if(entregaTransportadora.getCuentasBancarias()!=null){
				
				entregaTransportadora.getCuentasBancarias().getCodigo();
				entregaTransportadora.getCuentasBancarias().getEntidadesFinancieras();
			}
			
			if(entregaTransportadora.getTransportadoraValores()!=null){
				
				entregaTransportadora.getTransportadoraValores().getCodigoPk();
				entregaTransportadora.getTransportadoraValores().getTerceros().getDescripcion();
				entregaTransportadora.getMovimientosCajaByCodigoPk().getConsecutivo();
				entregaTransportadora.getMovimientosCajaByMovimientoCajaArqueo().getTurnoDeCaja().getCajas().getCentroAtencion().getDescripcion();
				entregaTransportadora.getMovimientosCajaByMovimientoCajaArqueo().getTurnoDeCaja().getUsuarios().getPersonas().getPrimerNombre();
				if(entregaTransportadora.getCuentasBancarias()!=null){
					
					entregaTransportadora.getCuentasBancarias().getTipoCuentaBancaria().getDescripcion();
					entregaTransportadora.getCuentasBancarias().getEntidadesFinancieras().getTerceros().getDescripcion();
				}
			}
		}
		
		return entregaTransportadora;
	}
	
	
	
	@SuppressWarnings("unchecked")
	public List<EntregaTransportadora> obtenerEntregasTransportadoraXTurnoCaja(TurnoDeCaja turnoDeCaja){
		
		 List<EntregaTransportadora> listaEntregasTransportadora = sessionFactory.getCurrentSession().createCriteria(EntregaTransportadora.class)
		  .createAlias("movimientosCajaByCodigoPk", "mc")

	     .add(Restrictions.eq("mc.turnoDeCaja", turnoDeCaja))
	     .list();

		return listaEntregasTransportadora;
	}
	
	
	/**
	 * M&eacute;todo que se encarga de consultar la fecha m&aacute;xima registrada para una
	 * movimiento de caja de tipo Entrega a Transportadora de valores. 
	 * Este movimiento est&aacute; asociado a un turno de caja espec&iacute;fico.
	 * 
	 * @param turnoDeCaja
	 * @return Date, con la fecha m&aacute;xima registrada para este movimiento
	 */
	public Date obtenerFechaUltimoMovimientoEntTransValores(TurnoDeCaja turnoDeCaja){
		
		return (Date) sessionFactory.getCurrentSession().createCriteria(EntregaTransportadora.class)

		.createAlias("movimientosCajaByCodigoPk", "movCajaEnt")
		.setProjection(Projections.max("movCajaEnt.fecha"))
		
		.add(Restrictions.eq("movCajaEnt.turnoDeCaja", turnoDeCaja)).uniqueResult();
	}
	
	
	/**
	 * M&eacute;todo utilizado para listar toda la informaci&oacute;n relacionada
	 * con las Entregas a Transportadora de valores asociadas a un Turno de caja
	 * o a un movimiento de caja espec&iacute;fico, si el movimiento pasado como 
	 * par&aacute;metro se encuentra registrado en el sistema
	 * 
	 * @param movimientosCaja
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<DtoEntregaCaja> obtenerEntregasTransportadoraValores(MovimientosCaja movimientosCaja)
	{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(EntregaTransportadora.class, "dsmc")
			.createAlias("movimientosCajaByCodigoPk", "mc")
			.createAlias("mc.turnoDeCaja", "turCaja")
			.createAlias("mc.tiposMovimientoCaja", "tmc")
			.createAlias("movimientosCajaByMovimientoCajaArqueo", "movEntregaArqueo")
			.createAlias("movEntregaArqueo.tiposMovimientoCaja", "tipoMovArqueo")
			.createAlias("mc.docSopMovimCajases", "docSopMovCajas")
			.createAlias("docSopMovCajas.formasPago", "fp")
			.createAlias("fp.tiposDetalleFormaPago", "tipoDetFormaPago")
			.createAlias("docSopMovCajas.detallePagosRcs", "detpagRcs",  Criteria.LEFT_JOIN)
			.createAlias("docSopMovCajas.detFaltanteSobrantes", "dfsEntrega", Criteria.LEFT_JOIN)
	
			.setProjection( Projections.projectionList()
					.add( Projections.property("mc.codigoPk"), "idMovimientoCaja")
					.add( Projections.property("docSopMovCajas.valor"), "valorEntregado")
					.add( Projections.property("docSopMovCajas.codigoPk"), "codigoDocSopMovCaja")    
					.add( Projections.property("fp.consecutivo"), "tipoFormaPago")
					.add( Projections.property("fp.descripcion"), "formaPago")
					.add( Projections.property("detpagRcs.valor"), "valorSistema")
					.add( Projections.property("dfsEntrega.codigoPk"), "idDetalleFaltanteSobrante")
					.add( Projections.property("dfsEntrega.valorDiferencia"), "valorFaltante"));
			
		if(movimientosCaja.getCodigoPk()>0){
	
			if(movimientosCaja.getFecha()!=null){
				
				List<DtoEntregaCaja> entregasFiltradasFecha = sessionFactory.getCurrentSession().createCriteria(EntregaTransportadora.class)
				  .createAlias("movimientosCajaByCodigoPk", "mc")
				  .createAlias("mc.tiposMovimientoCaja", "tmc")
				  .createAlias("movimientosCajaByMovimientoCajaArqueo", "movEntregaArqueo")
				  .createAlias("movEntregaArqueo.tiposMovimientoCaja", "tipoMovArqueo")
				  
				  .setProjection( Projections.projectionList()
						  .add( Projections.property("mc.codigoPk"), "idMovimientoCaja")
			              .add( Projections.property("mc.fecha"), "fecha")
			              .add( Projections.property("mc.hora"), "hora"))
			              
				  .add(Restrictions.eq("mc.turnoDeCaja", movimientosCaja.getTurnoDeCaja()))
				  .add(Restrictions.eq("estado", ConstantesIntegridadDominio.acronimoEstadoEntregaTransportadoraValores))
				  .add(Restrictions.eq("tmc.codigo",  ConstantesBD.codigoTipoMovimientoEntregaTransportadora))
				  .add(Restrictions.eq("tipoMovArqueo.codigo", ConstantesBD.codigoTipoMovimientoArqueoEntregaParcial))
				  .add(Restrictions.le("mc.fecha", movimientosCaja.getFecha()))
				 
				  .setResultTransformer( Transformers.aliasToBean(DtoEntregaCaja.class)).list();
				  
				ArrayList<Long> codigosEntrega =  new ArrayList<Long>();
				codigosEntrega.add(ConstantesBD.codigoNuncaValidoLong);
				
				for (DtoEntregaCaja entregaTransportadora: entregasFiltradasFecha) {
					
					if(UtilidadFecha.esHoraMenorIgualQueOtraReferencia(entregaTransportadora.getHora() , movimientosCaja.getHora())){
						
						codigosEntrega.add(entregaTransportadora.getIdMovimientoCaja());
					
					}else if(UtilidadFecha.esFechaMenorQueOtraReferencia(UtilidadFecha.conversionFormatoFechaAAp(entregaTransportadora.getFecha()), UtilidadFecha.conversionFormatoFechaAAp(movimientosCaja.getFecha()))){
						
						codigosEntrega.add(entregaTransportadora.getIdMovimientoCaja());
					}
				}
			
				criteria.add(Restrictions.in("mc.codigoPk", codigosEntrega));
			}
		}
		
		criteria.add(Restrictions.eq("estado", ConstantesIntegridadDominio.acronimoEstadoEntregaTransportadoraValores))
			.add(Restrictions.eq("tmc.codigo",  ConstantesBD.codigoTipoMovimientoEntregaTransportadora))
		    .add(Restrictions.eq("mc.turnoDeCaja", movimientosCaja.getTurnoDeCaja()))
		    .add(Restrictions.eq("tipoMovArqueo.codigo", ConstantesBD.codigoTipoMovimientoArqueoEntregaParcial))
		    .setResultTransformer(Transformers.aliasToBean(DtoEntregaCaja.class));
		  
		return (List<DtoEntregaCaja>) criteria.list();
		
		
//		//Primero se obtiene el listado de las Entregas a Transportadoras que involucran una entrega de forma de pago de tipo efectivo
//
//		DocSopMovimCajasDelegate docSopMovimCajasDelegate = new DocSopMovimCajasDelegate();
//		
//		List<DtoEntregaCaja> listaEntregaTransportadora = obtenerEntregasTransportadoraValoresRestriccionEfectivo(movimientosCaja, true);
//		double valorSistema;
//		//BigDecimal valorDiferencia;
//		
//		for (DtoEntregaCaja dtoEntregaCajaTransportadora : listaEntregaTransportadora) {
//			
//			valorSistema = 0;
//		//	valorDiferencia = new BigDecimal(0);
//			
//			DocSopMovimCajas docSopMovimCajas = docSopMovimCajasDelegate.findById(dtoEntregaCajaTransportadora.getCodigoDocSopMovCaja());
//			
//			Set<DetallePagosRc> setDetPagosRc = docSopMovimCajas.getDetallePagosRcs();
//			
//			for (Iterator iterator = setDetPagosRc.iterator(); iterator.hasNext();) {
//				
//				DetallePagosRc detallePagosRc = (DetallePagosRc) iterator.next();
//				valorSistema+=detallePagosRc.getValor();
//				
//			}
//			
//			dtoEntregaCajaTransportadora.setValorSistema(valorSistema);
//
//			Set<DetFaltanteSobrante> setDetFaltanteSobrante = docSopMovimCajas.getDetFaltanteSobrantes();
//			
//			for (Iterator iterator = setDetFaltanteSobrante.iterator(); iterator.hasNext();) {
//				
//				DetFaltanteSobrante detFaltanteSobrante = (DetFaltanteSobrante) iterator.next();
//				valorDiferencia = detFaltanteSobrante.getValorDiferencia();
//				
//			}
//			
//			dtoEntregaCajaTransportadora.setValorFaltante(valorDiferencia);
//			
//		}
//		
//		listaEntregaTransportadora.addAll(obtenerEntregasTransportadoraValoresRestriccionEfectivo(movimientosCaja, false));
//		
//		return listaEntregaTransportadora;
	}
	
	/**
	 * M&eacute;todo utilizado para listar toda la informaci&oacute;n relacionada
	 * con las Entregas a Transportadora de valores asociadas a un Turno de caja
	 * o a un movimiento de caja espec&iacute;fico, si el movimiento pasado como 
	 * par&aacute;metro se encuentra registrado en el sistema
	 * 
	 * @param movimientosCaja
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<DtoEntregaCaja> obtenerEntregasTransportadoraValoresPorFecha(MovimientosCaja movimientosCaja)
	{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(EntregaTransportadora.class, "dsmc")
		.createAlias("movimientosCajaByCodigoPk", "mc")
		.createAlias("mc.turnoDeCaja", "turCaja")
		.createAlias("mc.tiposMovimientoCaja", "tmc")
		.createAlias("movimientosCajaByMovimientoCajaArqueo", "movEntregaArqueo")
		.createAlias("movEntregaArqueo.tiposMovimientoCaja", "tipoMovArqueo")
		.createAlias("mc.docSopMovimCajases", "docSopMovCajas")
		.createAlias("docSopMovCajas.formasPago", "fp")
		.createAlias("fp.tiposDetalleFormaPago", "tipoDetFormaPago")
		.createAlias("docSopMovCajas.detallePagosRcs", "detpagRcs",  Criteria.LEFT_JOIN)
		.createAlias("docSopMovCajas.detFaltanteSobrantes", "dfsEntrega", Criteria.LEFT_JOIN)

		.setProjection( Projections.projectionList()
				.add( Projections.property("mc.codigoPk"), "idMovimientoCaja")
				.add( Projections.property("docSopMovCajas.valor"), "valorEntregado")
				.add( Projections.property("docSopMovCajas.codigoPk"), "codigoDocSopMovCaja")    
				.add( Projections.property("fp.consecutivo"), "tipoFormaPago")
				.add( Projections.property("fp.descripcion"), "formaPago")
				.add( Projections.property("detpagRcs.valor"), "valorSistema")
				.add( Projections.property("dfsEntrega.codigoPk"), "idDetalleFaltanteSobrante")
				.add( Projections.property("dfsEntrega.valorDiferencia"), "valorFaltante"));



		if(movimientosCaja.getFechaMovimiento()!=null){

			List<DtoEntregaCaja> entregasFiltradasFecha = sessionFactory.getCurrentSession().createCriteria(EntregaTransportadora.class)
			.createAlias("movimientosCajaByCodigoPk", "mc")
			.createAlias("mc.tiposMovimientoCaja", "tmc")
			.createAlias("movimientosCajaByMovimientoCajaArqueo", "movEntregaArqueo")
			.createAlias("movEntregaArqueo.tiposMovimientoCaja", "tipoMovArqueo")

			.setProjection( Projections.projectionList()
					.add( Projections.property("mc.codigoPk"), "idMovimientoCaja")
					.add( Projections.property("mc.fecha"), "fecha")
					.add( Projections.property("mc.hora"), "hora"))

					.add(Restrictions.eq("mc.turnoDeCaja", movimientosCaja.getTurnoDeCaja()))
					.add(Restrictions.eq("estado", ConstantesIntegridadDominio.acronimoEstadoEntregaTransportadoraValores))
					.add(Restrictions.eq("tmc.codigo",  ConstantesBD.codigoTipoMovimientoEntregaTransportadora))
					.add(Restrictions.eq("tipoMovArqueo.codigo", ConstantesBD.codigoTipoMovimientoArqueoEntregaParcial))
					.add(Restrictions.eq("mc.fecha", movimientosCaja.getFechaMovimiento()))

					.setResultTransformer( Transformers.aliasToBean(DtoEntregaCaja.class)).list();

			ArrayList<Long> codigosEntrega =  new ArrayList<Long>();
			codigosEntrega.add(ConstantesBD.codigoNuncaValidoLong);

			for (DtoEntregaCaja entregaTransportadora: entregasFiltradasFecha) {
				codigosEntrega.add(entregaTransportadora.getIdMovimientoCaja());
			}

			criteria.add(Restrictions.in("mc.codigoPk", codigosEntrega));
		}


		criteria.add(Restrictions.eq("estado", ConstantesIntegridadDominio.acronimoEstadoEntregaTransportadoraValores))
		.add(Restrictions.eq("tmc.codigo",  ConstantesBD.codigoTipoMovimientoEntregaTransportadora))
		.add(Restrictions.eq("mc.turnoDeCaja", movimientosCaja.getTurnoDeCaja()))
		.add(Restrictions.eq("tipoMovArqueo.codigo", ConstantesBD.codigoTipoMovimientoArqueoEntregaParcial))
		.setResultTransformer(Transformers.aliasToBean(DtoEntregaCaja.class));

		return (List<DtoEntregaCaja>) criteria.list();
	}	
	
//	public static void main(String[] args) {
//		
//		//MovimientosCaja movimientoCaja=new MovimientosCaja();
//		TurnoDeCaja turnoDeCaja= new TurnoDeCajaDelegate().findById(1);
//		
//		//movimientoCaja.setTurnoDeCaja(turnoDeCaja);
//		
//		EntregaTransportadoraDelegate x = new EntregaTransportadoraDelegate();
//		
//	    Date l = x.obtenerFechaUltimoMovimientoEntTransValores(turnoDeCaja);
//	    
//	    Log4JManager.info(l);
//		
//	}
	

//	/**
//	 * Obtiene los detalles de las Entregas a Transportadora de Valores.
//	 * 
//	 * Se restringe la consulta cuando se requiere obtener la informaci&oacute;n 
//	 * de los detalles de las entregas a Transportadora de valores para la forma de pago tipo efectivo.
//	 * 
//	 * @param fecha
//	 * @param usuario
//	 * @param caja
//	 * @return
//	 */
//	@SuppressWarnings("unchecked")
//	private List<DtoEntregaCaja> obtenerEntregasTransportadoraValoresRestriccionEfectivo (MovimientosCaja movimientosCaja, boolean restriccionEfectivo)
//	{
//
//		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(DocSopMovimCajas.class, "dsmc")
//		  .createAlias("movimientosCaja", "mc")
//		  .createAlias("mc.turnoDeCaja", "turCaja")
//		  .createAlias("mc.tiposMovimientoCaja", "tmc")
//		  .createAlias("mc.entregaTransportadoraByCodigoPk", "et")
//		  .createAlias("et.movimientosCajaByMovimientoCajaArqueo", "movEntregaArqueo")
//		  .createAlias("movEntregaArqueo.tiposMovimientoCaja", "tipoMovArqueo")
//		  .createAlias("formasPago", "fp")
//		  .createAlias("fp.tiposDetalleFormaPago", "tipoDetFormaPago") ;
//		
//		 ProjectionList projections = Projections.projectionList();
//		 
//		 projections.add( Projections.property("mc.codigoPk"), "idMovimientoCaja")
//	                .add( Projections.property("valor"), "valorEntregado")
//		            .add( Projections.property("codigoPk"), "codigoDocSopMovCaja")    
//	                .add( Projections.property("fp.consecutivo"), "tipoFormaPago")
//	                .add( Projections.property("fp.descripcion"), "formaPago");
//		 
//		 if(!restriccionEfectivo){
//			 
//			criteria.createAlias("detallePagosRcs", "detpagRcs")
//			  .createAlias("detFaltanteSobrantes", "dfsEntrega", Criteria.LEFT_JOIN);
//			 
//			projections.add( Projections.property("detpagRcs.valor"), "valorSistema" )
//		               .add( Projections.property("dfsEntrega.codigoPk"), "idDetalleFaltanteSobrante")
//		               .add( Projections.property("dfsEntrega.valorDiferencia"), "valorFaltante");
//		 
//			criteria.add(Restrictions.ne("tipoDetFormaPago.codigo", ConstantesBD.codigoTipoDetalleFormasPagoNinguno)); 
//			
//		 }else{
//			
//			 criteria.add(Restrictions.eq("tipoDetFormaPago.codigo", ConstantesBD.codigoTipoDetalleFormasPagoNinguno)); 
//		 }
//    
//	     criteria.setProjection(projections);
//	       
//	     
//	 	if(movimientosCaja.getCodigoPk()>0){
//
//			if(movimientosCaja.getFecha()!=null){
//				criteria.add(Restrictions.le("mc.fecha", movimientosCaja.getFecha()))
//			    .add(Restrictions.le("mc.hora", movimientosCaja.getHora()));
//			}
//		}
//	     
//	    criteria.add(Restrictions.eq("et.estado", ConstantesIntegridadDominio.acronimoEstadoEntregaTransportadoraValores))
//	     .add(Restrictions.eq("tmc.codigo",  ConstantesBD.codigoTipoMovimientoEntregaTransportadora))
//	     .add(Restrictions.eq("turCaja.codigoPk", movimientosCaja.getTurnoDeCaja().getCodigoPk()))
//	     .add(Restrictions.eq("tipoMovArqueo.codigo", ConstantesBD.codigoTipoMovimientoArqueoEntregaParcial))
//	     .setResultTransformer( Transformers.aliasToBean(DtoEntregaCaja.class));
//
//		return (List<DtoEntregaCaja>) criteria.list();
//	}
//	
}
