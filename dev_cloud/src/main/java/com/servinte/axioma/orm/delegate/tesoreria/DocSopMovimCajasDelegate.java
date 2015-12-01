package com.servinte.axioma.orm.delegate.tesoreria;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;

import com.princetonsa.dto.tesoreria.DtoDetalleDocSopor;
import com.princetonsa.dto.tesoreria.DtoTrasladoCaja;
import com.servinte.axioma.orm.DetFaltanteSobrante;
import com.servinte.axioma.orm.DetallePagosRc;
import com.servinte.axioma.orm.DocSopMovimCajas;
import com.servinte.axioma.orm.DocSopMovimCajasHome;
import com.servinte.axioma.orm.MovimientosCaja;
import com.servinte.axioma.persistencia.UtilidadTransaccion;

/**
 * 
 * Esta clase se encarga de manejar las transaccciones relacionadas
 * con el objeto {@link DocSopMovimCajas}.
 * 
 * @author Jorge Armando Agudelo Quintero
 *
 */

public class DocSopMovimCajasDelegate extends DocSopMovimCajasHome{
	

	/**
	 *Obtiene el DocSopMovimCajas espec&iacute;fico seg&uacute;n el consecutivo enviado
	 * @param codigoPk
	 */
	@Override
	public DocSopMovimCajas findById(long codigoPk) {
		
		return super.findById(codigoPk);
	}
	
	

	/**
	 * Retorna un listado con los detalles de las entregas a Caja Mayor / Principal y/o Transportadora de valores realizadas en un turno 
	 * espec&iacute;fico. Adem&aacute;s si el movimiento enviado esta registrado en el sistema, solo se lista la infomaci&oacute;n asociada 
	 * a este movimiento, sin tener en cuenta el tipo.
	 * 
	 * No se involucran los detalles de entregas realizadas que esten asociados a Formas de Pago de Tipo "NINGUNO"
	 *  
	 * @param movimientosCaja
	 * @return listado con las Entregas a Caja Mayor / Principal y/o Transportadora de valores realizadas en un turno espec&iacute;fico. 
	 * (Sin formas de pago tipo "NINGUNO")
	 * 
	 * @see DtoDetalleDocSopor
	 */
	public List<DtoDetalleDocSopor> obtenerEntrTransYCajaMayorPpalNoFormaPagoNinguna (MovimientosCaja movimientosCaja) {
	
		List<DtoDetalleDocSopor> listadoEntregasTransCajaMayor;
		
		listadoEntregasTransCajaMayor =	obtenerTrasladosTarjetas(movimientosCaja);
		listadoEntregasTransCajaMayor.addAll(obtenerTrasladosCheque(movimientosCaja));
		listadoEntregasTransCajaMayor.addAll(obtenerTrasladosBono(movimientosCaja));
		
		return listadoEntregasTransCajaMayor;
	}

	/**
	 * Retorna un listado con las Aceptaciones realizadas en un turno espec&iacute;fico,
	 * pero sin incluir las que involucren la(s) forma de pago tipo "NINGUNO".
	 * 
	 * @param movimientosCaja
	 * @return listado con las Aceptaciones realizadas en un turno espec&iacute;fico sin incluir 
	 * las que involucren la(s) forma de pago tipo "NINGUNO"
	 * @see DtoDetalleDocSopor
	 */
	public List<DtoDetalleDocSopor> obtenerAceptacionesCajaNoFormaPagoNinguno (MovimientosCaja movimientosCaja){
		
		List<DtoDetalleDocSopor> listadoAceptacionesCaja;
		
		listadoAceptacionesCaja = obtenerAceptacionesCheque(movimientosCaja);
		listadoAceptacionesCaja.addAll(obtenerAceptacionTarjetas(movimientosCaja));
		listadoAceptacionesCaja.addAll(obtenerAceptacionesBono(movimientosCaja));
	
		return listadoAceptacionesCaja;
	}
	
	/**
	 * Valores a entregar por cada documento de soporte asociado a la
	 * aceptaci&oacute;n
	 * 
	 * @author Alejandro Echandia.
	 * @param caja
	 * @param usuario
	 * @return
	 */
	

	@SuppressWarnings("unchecked")
	public List<DtoTrasladoCaja> obtenerValoresAEntregarXSolicitudesAceptadas(List<DtoTrasladoCaja> solicitudesAceptadas,  List<DtoTrasladoCaja> aceptaciones)
	{
		for (DtoTrasladoCaja dtoTrasladoCajaAcept : aceptaciones) {
			
			DocSopMovimCajas docSopMovimCajasAcep = super.findById(dtoTrasladoCajaAcept.getCodigoDocSopMovCaja());
			Set<DetallePagosRc> detallePagosRcsAcep =  docSopMovimCajasAcep.getDetallePagosRcs();
			
			for (DtoTrasladoCaja dtoTrasladoCajaSol : solicitudesAceptadas) {
				
				if(dtoTrasladoCajaAcept.getTipoFormaPago() == dtoTrasladoCajaSol.getTipoFormaPago() 
						&&  dtoTrasladoCajaSol.getCodigoAceptacionSolicitud() == dtoTrasladoCajaAcept.getCodigoAceptacionSolicitud()){
					
					DocSopMovimCajas docSopMovimCajasSol = super.findById(dtoTrasladoCajaSol.getCodigoDocSopMovCaja());
					Set<DetallePagosRc> detallePagosRcsSol = docSopMovimCajasSol.getDetallePagosRcs();
					
					if(detallePagosRcsAcep.hashCode()==detallePagosRcsSol.hashCode()){
						
						dtoTrasladoCajaAcept.setValorTrasladado(new BigDecimal(dtoTrasladoCajaSol.getValorTrasladado()));
					}
				}
			}
		}

		return aceptaciones;
	}
	

	/**
	 * Obtiene los documentos correspondientes a movimientos de traslado de tarjetas.
	 * Si el movimiento tiene asignado un codigoPk, filtra la informaci&oacute;n que solo
	 * este relacionada a ese movimiento espec&iacute;fico
	 * 
	 * @author Alejandro Echandia.
	 * @param movimientoCaja
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<DtoDetalleDocSopor> obtenerTrasladosTarjetas(MovimientosCaja movimientosCaja) 
	{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(DocSopMovimCajas.class, "dsmc")
		   .createAlias("movimientosCaja", "mc")
		   .createAlias("mc.tiposMovimientoCaja", "tmc")
		   .createAlias("detallePagosRcs", "detpag")
		   .createAlias("detpag.recibosCaja","rc")
		   .createAlias("rc.detalleConceptosRcs","detcon")
		   .createAlias("detpag.movimientosTarjetases","movtar")
		   .createAlias("formasPago", "fp")
		   .createAlias("movtar.entidadesFinancieras","ef")
		   .createAlias("ef.terceros","ter")
	  
		   .setProjection( Projections.projectionList()
				   .add( Projections.property("detcon.nombreBeneficiario"), "nombreRecibidoDe" )
				   .add( Projections.property("nroDocumento"), "nroDocumentoEntregado" )
				   .add( Projections.property("valor"), "valorSistemaBig" )
				   .add( Projections.property("ter.descripcion"), "descripcionEntFinanciera" )
				   .add( Projections.property("ef.consecutivo"), "consecutivoEntFinanciera" )
				   .add( Projections.property("fp.descripcion"), "formaPago" )
				   .add( Projections.property("fp.consecutivo"), "consecutivoFormaPago" )
				   .add( Projections.property("rc.id.numeroReciboCaja"), "reciboCajaId" )
				   .add( Projections.property("detpag.consecutivo"), "detallePagosId" )
			);
			
		if(movimientosCaja.getCodigoPk()>0)
		{
			criteria.add(Restrictions.eq("mc.codigoPk", movimientosCaja.getCodigoPk()));
		
		}else {
			
			criteria.add(Restrictions.between("tmc.codigo", ConstantesBD.codigoTipoMovimientoEntregaTransportadora,ConstantesBD.codigoTipoMovimientoEntregaCajaPrincipalMayor))
					.add(Restrictions.eq("mc.turnoDeCaja", movimientosCaja.getTurnoDeCaja()));

		}
  
		criteria.setResultTransformer(Transformers.aliasToBean(DtoDetalleDocSopor.class))
		.list();

		List<DtoDetalleDocSopor> lista =criteria.list();

		return lista;
	}
	
	/**
	 * obtiene documentos correspondientes a movimientos de traslado de tarjetas
	 * @author Alejandro Echandia.
	 * @param movimientoCaja
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<DtoDetalleDocSopor> obtenerAceptacionTarjetas(MovimientosCaja movimientosCaja) 
	{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(MovimientosCaja.class, "mc")
	
			.createAlias("tiposMovimientoCaja", "tmc")
			.createAlias("aceptacionTrasladoCaja", "acepTrasCaja")
			
//			.createAlias("acepTrasCaja.solicitudTrasladoCaja", "solTrasCaja")
//			.createAlias("solTrasCaja.movimientosCajaByCodigoPk", "movCajaSol")
//			.createAlias("movCajaSol.docSopMovimCajases", "docSopSol")
//			.createAlias("docSopSol.detallePagosRcs", "detpag")
			
			.createAlias("solicitudTrasladoCajaByCodigoPk", "solTrasCaja")
			.createAlias("acepTrasCaja.movimientosCaja", "movCajaAcep")
			.createAlias("movCajaAcep.docSopMovimCajases", "docSopAcep")
			.createAlias("docSopAcep.detallePagosRcs", "detpag")
			
			
			.createAlias("detpag.recibosCaja","rc")
			.createAlias("rc.detalleConceptosRcs","detcon")
			.createAlias("detpag.movimientosTarjetases","movtar")
			.createAlias("docSopAcep.formasPago", "fp")
		  	.createAlias("movtar.entidadesFinancieras","ef")
		  	.createAlias("ef.terceros","ter")
		  
		  	.setProjection( Projections.projectionList()
		  			.add( Projections.property("detcon.nombreBeneficiario"), "nombreRecibidoDe" )
		  			.add( Projections.property("docSopAcep.nroDocumento"), "nroDocumentoEntregado" )
		  			.add( Projections.property("docSopAcep.valor"), "valorSistemaBig" )
		  			.add( Projections.property("ter.descripcion"), "descripcionEntFinanciera" )
		  			.add( Projections.property("ef.consecutivo"), "consecutivoEntFinanciera" )
		  			.add( Projections.property("fp.descripcion"), "formaPago" )
		  			.add( Projections.property("fp.consecutivo"), "consecutivoFormaPago" )
		  			.add( Projections.property("rc.id.numeroReciboCaja"), "reciboCajaId" )
		  			.add( Projections.property("detpag.consecutivo"), "detallePagosId" )
		  	);
	 
		//En este caso se consulta una solicitud para realizar la apertura de turno     
		if(movimientosCaja.getCodigoPk()>0)
		{
			criteria.add(Restrictions.eq("mc.codigoPk", movimientosCaja.getCodigoPk()));
	   
		}else{ //En este caso se consulta las solicitudes que ya fueron aceptadas para realizar movimientos de tipo arqueo.
			criteria.add(Restrictions.eq("tmc.codigo",ConstantesBD.codigoTipoMovimientoAperturaTurnoCaja))
			.add(Restrictions.eq("solTrasCaja.estado",  ConstantesIntegridadDominio.acronimoEstadoSolicitudTrasladoCajaAceptado))
			.add(Restrictions.eq("turnoDeCaja", movimientosCaja.getTurnoDeCaja()));
		}
	   

  	   	return (List<DtoDetalleDocSopor>) criteria.setResultTransformer( Transformers.aliasToBean(DtoDetalleDocSopor.class))
  	   	.list();
	}
	
	/**
	 * Obtiene los documentos correspondientes a movimientos de traslado de bonos.
	 * Si el movimiento tiene asignado un codigoPk, filtra la informaci&oacute;n que solo
	 * este relacionada a ese movimiento espec&iacute;fico
	 * @param movimientoCaja
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<DtoDetalleDocSopor> obtenerTrasladosBono(MovimientosCaja movimientosCaja) 
	{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(DocSopMovimCajas.class, "dsmc")
			.createAlias("movimientosCaja", "mc")
			.createAlias("mc.tiposMovimientoCaja", "tmc")
			.createAlias("detallePagosRcs", "detpag")
			.createAlias("detpag.recibosCaja","rc")
			.createAlias("rc.detalleConceptosRcs","detcon")
			.createAlias("detpag.movimientosBonoses","movbon")
			.createAlias("formasPago", "fp")
	  
			.setProjection( Projections.projectionList()
					.add( Projections.property("detcon.nombreBeneficiario"), "nombreRecibidoDe" )
					.add( Projections.property("nroDocumento"), "nroDocumentoEntregado" )
					.add( Projections.property("valor"), "valorSistemaBig")
					.add( Projections.property("fp.descripcion"), "formaPago" )
					.add( Projections.property("fp.consecutivo"), "consecutivoFormaPago" )
					.add( Projections.property("rc.id.numeroReciboCaja"), "reciboCajaId" )
					.add( Projections.property("detpag.consecutivo"), "detallePagosId" ));
		
		if(movimientosCaja.getCodigoPk()>0)
		{
			criteria.add(Restrictions.eq("mc.codigoPk", movimientosCaja.getCodigoPk()));
		
		}else {
			
			criteria.add(Restrictions.between("tmc.codigo", ConstantesBD.codigoTipoMovimientoEntregaTransportadora,ConstantesBD.codigoTipoMovimientoEntregaCajaPrincipalMayor))
					.add(Restrictions.eq("mc.turnoDeCaja", movimientosCaja.getTurnoDeCaja()));
		}
		
		criteria.setResultTransformer( Transformers.aliasToBean(DtoDetalleDocSopor.class)).list();
		
		List<DtoDetalleDocSopor> lista =criteria.list();
		
		return lista;
      
	}

	/**
	 * Obtiene los documentos correspondientes a movimientos de aceptaci&oacute;n de bonos.
	 * @param movimientoCaja
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<DtoDetalleDocSopor> obtenerAceptacionesBono(MovimientosCaja movimientosCaja) 
	{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(MovimientosCaja.class, "mc")

			.createAlias("tiposMovimientoCaja", "tmc")
			.createAlias("aceptacionTrasladoCaja", "acepTrasCaja")
			//.createAlias("acepTrasCaja.solicitudTrasladoCaja", "solTrasCaja")
			//.createAlias("solTrasCaja.movimientosCajaByCodigoPk", "movCajaSol")
			//.createAlias("movCajaSol.docSopMovimCajases", "docSopSol")
			//.createAlias("docSopSol.detallePagosRcs", "detpag")
			
			.createAlias("solicitudTrasladoCajaByCodigoPk", "solTrasCaja")
			.createAlias("acepTrasCaja.movimientosCaja", "movCajaAcep")
			.createAlias("movCajaAcep.docSopMovimCajases", "docSopAcep")
			.createAlias("docSopAcep.detallePagosRcs", "detpag")
			
			.createAlias("detpag.recibosCaja","rc")
			.createAlias("rc.detalleConceptosRcs","detcon")
			.createAlias("detpag.movimientosBonoses","movbon")
			.createAlias("docSopAcep.formasPago", "fp")
			
			.setProjection( Projections.projectionList()
					.add( Projections.property("detcon.nombreBeneficiario"), "nombreRecibidoDe" )
					.add( Projections.property("docSopAcep.nroDocumento"), "nroDocumentoEntregado" )
					.add( Projections.property("docSopAcep.valor"), "valorSistemaBig")
					.add( Projections.property("fp.descripcion"), "formaPago" )
					.add( Projections.property("fp.consecutivo"), "consecutivoFormaPago" )
					.add( Projections.property("rc.id.numeroReciboCaja"), "reciboCajaId" )
					.add( Projections.property("detpag.consecutivo"), "detallePagosId" )
			);
	          
	   //En este caso se consulta una solicitud para realizar la apertura de turno     
	   if(movimientosCaja.getCodigoPk()>0)
	   {
		 criteria.add(Restrictions.eq("mc.codigoPk", movimientosCaja.getCodigoPk()));
	   
	   }else{ //En este caso se consulta las solicitudes que ya fueron aceptadas para realizar movimientos de tipo arqueo.
	  
		   criteria.add(Restrictions.eq("tmc.codigo",ConstantesBD.codigoTipoMovimientoAperturaTurnoCaja))
		   .add(Restrictions.eq("solTrasCaja.estado",  ConstantesIntegridadDominio.acronimoEstadoSolicitudTrasladoCajaAceptado))
		   .add(Restrictions.eq("turnoDeCaja", movimientosCaja.getTurnoDeCaja()));
	   }

  	   return (List<DtoDetalleDocSopor>) criteria.setResultTransformer( Transformers.aliasToBean(DtoDetalleDocSopor.class))
  	   .list();
	}
	
	/**
	 * Obtiene los documentos correspondientes a movimientos de traslado de cheques.
	 * Si el movimiento tiene asignado un codigoPk, filtra la informaci&oacute;n que solo
	 * este relacionada a ese movimiento espec&iacute;fico
	 * 
	 * @param movimientoCaja
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<DtoDetalleDocSopor> obtenerTrasladosCheque(MovimientosCaja movimientosCaja) 
	{
	
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(DocSopMovimCajas.class, "dsmc")
			.createAlias("movimientosCaja", "mc")
			.createAlias("mc.tiposMovimientoCaja", "tmc")
			.createAlias("detallePagosRcs", "detpag")
			.createAlias("detpag.recibosCaja","rc")
			.createAlias("rc.detalleConceptosRcs","detcon")
			.createAlias("detpag.movimientosChequeses","movche")
			.createAlias("movche.entidadesFinancieras","ef")
			.createAlias("ef.terceros","ter")
			.createAlias("formasPago", "fp")
		  
			.setProjection( Projections.projectionList()
					.add( Projections.property("detcon.nombreBeneficiario"), "nombreRecibidoDe" )
					.add( Projections.property("nroDocumento"), "nroDocumentoEntregado" )
		  		   	.add( Projections.property("ter.descripcion"), "descripcionEntFinanciera" )
		  		   	.add( Projections.property("ef.consecutivo"), "consecutivoEntFinanciera" )
		  		   	.add( Projections.property("valor"), "valorSistemaBig")
		  		   	.add( Projections.property("fp.descripcion"), "formaPago" )
		  		   	.add( Projections.property("fp.consecutivo"), "consecutivoFormaPago" )
		  		   	.add( Projections.property("rc.id.numeroReciboCaja"), "reciboCajaId" )
		  		   	.add( Projections.property("detpag.consecutivo"), "detallePagosId" ));

		if(movimientosCaja.getCodigoPk()>0)
		{
			criteria.add(Restrictions.eq("mc.codigoPk", movimientosCaja.getCodigoPk()));
		
		}else {
			
			criteria.add(Restrictions.between("tmc.codigo", ConstantesBD.codigoTipoMovimientoEntregaTransportadora,ConstantesBD.codigoTipoMovimientoEntregaCajaPrincipalMayor))
					.add(Restrictions.eq("mc.turnoDeCaja", movimientosCaja.getTurnoDeCaja()));
		}
		
		criteria.setResultTransformer( Transformers.aliasToBean(DtoDetalleDocSopor.class)).list();
		
		List<DtoDetalleDocSopor> lista =criteria.list();
		
		return lista;
	}
	
	
	/**
	 * Obtiene los documentos correspondientes a movimientos de aceptaci&oacute;n de cheques.
	 * 
	 * @author Alejandro Echandia.
	 * @param movimientoCaja
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<DtoDetalleDocSopor> obtenerAceptacionesCheque(MovimientosCaja movimientosCaja) 
	{

		Criteria criteria = sessionFactory.getCurrentSession().createCriteria (MovimientosCaja.class, "mc")
			.createAlias("tiposMovimientoCaja", "tmc")
			.createAlias("aceptacionTrasladoCaja", "acepTrasCaja")
			
//			.createAlias("acepTrasCaja.solicitudTrasladoCaja", "solTrasCaja")
//			.createAlias("solTrasCaja.movimientosCajaByCodigoPk", "movCajaSol")
//			.createAlias("movCajaSol.docSopMovimCajases", "docSopSol")
//			.createAlias("docSopSol.detallePagosRcs", "detpag")
			
			.createAlias("acepTrasCaja.solicitudTrasladoCaja", "solTrasCaja")
			//.createAlias("solicitudTrasladoCajaByCodigoPk", "solTrasCaja")
			.createAlias("acepTrasCaja.movimientosCaja", "movCajaAcep")
			.createAlias("movCajaAcep.docSopMovimCajases", "docSopAcep")
			.createAlias("docSopAcep.detallePagosRcs", "detpag")
			
			
			.createAlias("detpag.recibosCaja","rc")
			.createAlias("rc.detalleConceptosRcs","detcon")
			.createAlias("detpag.movimientosChequeses","movche")
			.createAlias("movche.entidadesFinancieras","ef")
		  	.createAlias("ef.terceros","ter")
		  	.createAlias("docSopAcep.formasPago", "fp")
		  
		  	.setProjection( Projections.projectionList()
		  			.add( Projections.property("detcon.nombreBeneficiario"), "nombreRecibidoDe" )
		  			.add( Projections.property("docSopAcep.nroDocumento"), "nroDocumentoEntregado" )
		  			.add( Projections.property("ter.descripcion"), "descripcionEntFinanciera" )
		  			.add( Projections.property("ef.consecutivo"), "consecutivoEntFinanciera" )
		  			.add( Projections.property("docSopAcep.valor"), "valorSistemaBig" )
		  			.add( Projections.property("fp.descripcion"), "formaPago" )
		  			.add( Projections.property("fp.consecutivo"), "consecutivoFormaPago" )
		  			.add( Projections.property("rc.id.numeroReciboCaja"), "reciboCajaId" )
		  			.add( Projections.property("detpag.consecutivo"), "detallePagosId" )
		  	);
	      
	   //En este caso se consulta una solicitud para realizar la apertura de turno     
	   if(movimientosCaja.getCodigoPk()>0)
	   {
		 criteria.add(Restrictions.eq("mc.codigoPk", movimientosCaja.getCodigoPk()));
	   
	   }else{ //En este caso se consulta las solicitudes que ya fueron aceptadas para realizar movimientos de tipo arqueo.
		  
		   criteria.add(Restrictions.eq("tmc.codigo",ConstantesBD.codigoTipoMovimientoAperturaTurnoCaja))
		   .add(Restrictions.eq("solTrasCaja.estado",  ConstantesIntegridadDominio.acronimoEstadoSolicitudTrasladoCajaAceptado))
		   .add(Restrictions.eq("turnoDeCaja", movimientosCaja.getTurnoDeCaja()));
	   }
 
	   return (List<DtoDetalleDocSopor>) criteria.setResultTransformer( Transformers.aliasToBean(DtoDetalleDocSopor.class))
	   .list();

	}
	

	
	/**
	 * Retorna una lista de la informacion detallada para la aceptacion
	 * @param idMovimiento
	 * @return {@link List}
	 */
	@SuppressWarnings("unchecked")
	public List<DtoDetalleDocSopor> obtenerDetallesDocumentosParaAceptacion(long idMovimiento)
	{
		UtilidadTransaccion.getTransaccion().begin();
		List<DtoDetalleDocSopor> listaDetalleDocumentos = (List<DtoDetalleDocSopor>) sessionFactory.getCurrentSession()
			.createCriteria(DocSopMovimCajas.class, 			"doc_sop")
				.createAlias("detallePagosRcs", 				"det_pago")
				.createAlias("movimientosCaja",					"mov_caja")
				.createAlias("formasPago",						"forma_pago")
				.createAlias("det_pago.recibosCaja",			"recibo_caja")
				.createAlias("recibo_caja.detalleConceptosRcs", "det_concepto")
				.createAlias("forma_pago.tiposDetalleFormaPago","det_forma_pago")
				
				.add(Expression.eq("mov_caja.codigoPk", idMovimiento))
				
				// Esto se documento por el orden de las formas de pago. Pero no se ha terminado de evaluar su impacto en
				// los procesos de documentos de soporte.
				.add(Restrictions.ne("det_forma_pago.codigo", ConstantesBD.codigoTipoDetalleFormasPagoNinguno ))
				
				.setProjection(Projections.projectionList()
					.add(Projections.property("det_concepto.nombreBeneficiario")	, "nombreRecibidoDe")
					.add(Projections.property("doc_sop.nroDocumento")				, "nroDocumentoEntregado")
					.add(Projections.property("doc_sop.valor")						, "valorSistemaBig")
					.add(Projections.property("forma_pago.consecutivo")				, "consecutivoFormaPago")
					.add(Projections.property("det_forma_pago.codigo")				, "tiposDetalleFormaPago")
					.add(Projections.property("forma_pago.descripcion")				, "formaPago")
					.add(Projections.property("det_pago.consecutivo")				, "detallePagosId")
					
				)
		   	.addOrder(Order.asc("det_forma_pago.prioridad") )
			.setResultTransformer( Transformers.aliasToBean(DtoDetalleDocSopor.class)).list();
		  
			for(DtoDetalleDocSopor dtoDetDocumento : listaDetalleDocumentos)
			{
				dtoDetDocumento = obtenerDetallesDocumentosParaAceptacionEntidad(dtoDetDocumento, idMovimiento);
			}
			
		return listaDetalleDocumentos;
	}
	
	
	/**
	 * Retorna una lista de la informacion detallada para la aceptacion
	 * @param idMovimiento
	 * @return {@link List}
	 */
	@SuppressWarnings("unchecked")
	public List<DtoDetalleDocSopor> obtenerDetallesDocumentosParaAceptacionEfectivo(long idMovimiento)
	{
		UtilidadTransaccion.getTransaccion().begin();
		List<DtoDetalleDocSopor> listaDetalleDocumentos = (List<DtoDetalleDocSopor>) sessionFactory.getCurrentSession()
			.createCriteria(DocSopMovimCajas.class, 			"doc_sop")
				//.createAlias("detallePagosRcs", 				"det_pago")
				.createAlias("movimientosCaja",					"mov_caja")
				.createAlias("formasPago",						"forma_pago")
				//.createAlias("det_pago.recibosCaja",			"recibo_caja")
				//.createAlias("recibo_caja.detalleConceptosRcs", "det_concepto")
				.createAlias("forma_pago.tiposDetalleFormaPago","det_forma_pago")
				
				.add(Expression.eq("mov_caja.codigoPk", idMovimiento))
				.add(Restrictions.eq("det_forma_pago.codigo", ConstantesBD.codigoTipoDetalleFormasPagoNinguno ))
				.setProjection(Projections.projectionList()
					//.add(Projections.property("det_concepto.nombreBeneficiario")	, "nombreRecibidoDe")
					.add(Projections.property("doc_sop.nroDocumento")				, "nroDocumentoEntregado")
					.add(Projections.property("doc_sop.valor")						, "valorSistemaBig")
					.add(Projections.property("forma_pago.consecutivo")				, "consecutivoFormaPago")
					.add(Projections.property("det_forma_pago.codigo")				, "tiposDetalleFormaPago")
					.add(Projections.property("forma_pago.descripcion")				, "formaPago")
					//.add(Projections.property("det_pago.consecutivo")				, "detallePagosId")
					
				)
				
			.addOrder(Order.asc("det_forma_pago.prioridad"))
			.setResultTransformer( Transformers.aliasToBean(DtoDetalleDocSopor.class)).list();
		
		
			for(DtoDetalleDocSopor dtoDetDocumento : listaDetalleDocumentos)
			{
				dtoDetDocumento = obtenerDetallesDocumentosParaAceptacionEntidad(dtoDetDocumento, idMovimiento);
			}
			
		return listaDetalleDocumentos;
	}
	
	
	/**
	 * Metodo complemento de obtenerDetallesDocumentosParaAceptacion
	 * Termina de llenar el dto con los detalles de aceptacion
	 * @param dtoDetAceptacion
	 * @param idMovimiento
	 * @param sessionFactory
	 * @return
	 */
	public  DtoDetalleDocSopor obtenerDetallesDocumentosParaAceptacionEntidad(DtoDetalleDocSopor dtoDetAceptacion,long idMovimiento)
	{
		String movimientoentidadFinanciera;
		switch (dtoDetAceptacion.getTiposDetalleFormaPago()) 
		{
			case ConstantesBD.codigoTipoDetalleFormasPagoCheque:
				movimientoentidadFinanciera = "movimientosChequeses";
				break;
			
			case ConstantesBD.codigoTipoDetalleFormasPagoTarjeta:
				movimientoentidadFinanciera = "movimientosTarjetases";
				break;
				
			default:
				movimientoentidadFinanciera = null;
				break;
		}
	
		if(movimientoentidadFinanciera != null)
		{
			DtoDetalleDocSopor dtoDetalle = (DtoDetalleDocSopor) sessionFactory.getCurrentSession()
			.createCriteria(DocSopMovimCajas.class, 			"doc_sop")
				.createAlias("detallePagosRcs", 				"det_pago")
				.createAlias("movimientosCaja",					"mov_caja")
				.createAlias("formasPago",						"forma_pago")
				.createAlias("det_pago.recibosCaja",			"recibo_caja")
				.createAlias("recibo_caja.detalleConceptosRcs", "det_concepto")
				.createAlias("forma_pago.tiposDetalleFormaPago","det_forma_pago")
				.createAlias("det_pago."+movimientoentidadFinanciera+""	,"movimiento_entidad")
				.createAlias("movimiento_entidad.entidadesFinancieras"	,"entidad_financiera")
				.createAlias("entidad_financiera.terceros"				,"tercero_entidad_financiera")
				
				.add(Expression.eq("mov_caja.codigoPk"		, idMovimiento))
				.add(Expression.eq("doc_sop.nroDocumento"	, dtoDetAceptacion.getNroDocumentoEntregado()))
				
				.setProjection(Projections.projectionList()
					.add( Projections.property("entidad_financiera.consecutivo")			, "consecutivoEntFinanciera")
					.add( Projections.property("tercero_entidad_financiera.descripcion")	, "descripcionEntFinanciera")
				)
				.setResultTransformer( Transformers.aliasToBean(DtoDetalleDocSopor.class) )
				.setMaxResults(1).uniqueResult();
			
		
			dtoDetAceptacion.setConsecutivoEntFinanciera(dtoDetalle.getConsecutivoEntFinanciera());
			if(dtoDetalle.getDescripcionEntFinanciera() != null){
				dtoDetAceptacion.setDescripcionEntFinanciera(dtoDetalle.getDescripcionEntFinanciera());
			}
			
		}
		return dtoDetAceptacion;
	}
	
	/**
	 * M&eacute;todo que retorna los {@link DocSopMovimCajas} asociados a un movimiento espec&iacute;fico.
	 * @param codigoMovimientosCaja
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<DocSopMovimCajas> obtenerDocSoportePorMovimiento(long codigoMovimientosCaja)
	{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(DocSopMovimCajas.class, "doc_sop")

		.createAlias("doc_sop.movimientosCaja", "movCaja")
		.add(Restrictions.eq("movCaja.codigoPk", codigoMovimientosCaja));
		
		List<DocSopMovimCajas> listaDocSopMovimCajas = (List<DocSopMovimCajas>) criteria.list();
		
		for (DocSopMovimCajas docSopMovimCajas : listaDocSopMovimCajas) {
			
			Set<DetFaltanteSobrante> faltanteSobrantes = docSopMovimCajas.getDetFaltanteSobrantes();
			
			for (DetFaltanteSobrante detFaltanteSobrante : faltanteSobrantes) {
				
				detFaltanteSobrante.getValorDiferencia();
			}
		}
		
		return listaDocSopMovimCajas;
	}


	/**
	 * M&eacute;todo que se encarga de totalizar los valores entregados para las formas de pago de tipo "NINGUNO"
	 * a Transportadora de Valores y/o Caja Mayor / Principal asociadas a un turno de caja espec&iacute;fico.
	 * 
	 * @param movimientosCaja
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<DtoDetalleDocSopor>  obtenerTotalesEntregasFormaPagoNinguno (MovimientosCaja movimientosCaja){
		
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(DocSopMovimCajas.class, "dsmc")
			.createAlias("movimientosCaja", "mc")
			.createAlias("mc.tiposMovimientoCaja", "tmc")
			.createAlias("formasPago", "fp")
			.createAlias("fp.tiposDetalleFormaPago", "tipoDetFormaPago")
	
			.setProjection( Projections.projectionList()
					.add(Projections.sum("valor"), "valorSistemaBig")
					.add( Projections.property("fp.descripcion"), "formaPago" )
	                .add( Projections.property("fp.consecutivo"), "consecutivoFormaPago" )
					.add(Projections.groupProperty("fp.consecutivo"))
					.add(Projections.groupProperty("fp.descripcion")))
					
			.add(Restrictions.eq("tipoDetFormaPago.codigo", ConstantesBD.codigoTipoDetalleFormasPagoNinguno))
			.setResultTransformer( Transformers.aliasToBean(DtoDetalleDocSopor.class));
		
		
		if(movimientosCaja.getCodigoPk()>0)
		{
			criteria.add(Restrictions.eq("mc.codigoPk", movimientosCaja.getCodigoPk()));
		
		}else {
			
			criteria.add(Restrictions.between("tmc.codigo", ConstantesBD.codigoTipoMovimientoEntregaTransportadora,ConstantesBD.codigoTipoMovimientoEntregaCajaPrincipalMayor))
					.add(Restrictions.eq("mc.turnoDeCaja", movimientosCaja.getTurnoDeCaja()));
		}
		
		return (List<DtoDetalleDocSopor>) criteria.list();
	}
	
	
//	public static void main(String[] args) {
//		
//		TurnoDeCaja turnoDeCaja= new TurnoDeCajaDelegate().findById(234);
//		MovimientosCaja movimientosCaja = new MovimientosCaja();
//		movimientosCaja.setTurnoDeCaja(turnoDeCaja);
//		
//		DocSopMovimCajasDelegate r =  new DocSopMovimCajasDelegate();
//		
//		List<DtoDetalleDocSopor> l = r.obtenerTotalesEntregasFormaPagoNinguno(movimientosCaja);
//		
//		for (DtoDetalleDocSopor valor : l) {
//			
//			Log4JManager.info("---- " + valor.getFormaPago() + " - " + valor.getValorSistemaBig() );
//		}
//		
//	}

	
//	/**
//	 * M&eacute;todo que retorna un listado con los documentos de soporte para la forma de pago tipo efectivo, asociados a un Turno de Caja y
//	 * que se han entregado en un movimiento de caja de tipo (s) incluido en el arreglo codigosTipoMovimiento.
//	 * 
//	 * Tambi&eacute;n se puede realizar la b&uacute;squeda por el c&oacute;digo espec&iacute;fico de un movimiento de caja.
//	 * 
//	 * @param turnoDeCaja
//	 * @param codigosTipoMovimiento
//	 * @param codigoMovimiento
//	 * @return
//	 */
//	@SuppressWarnings("unchecked")
//	public List<DocSopMovimCajas> obtenerListaDocSoporEfectPorTipoMovimiento(TurnoDeCaja turnoDeCaja, ArrayList<Integer> codigosTipoMovimiento , long codigoMovimiento)
//	{
//		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(DocSopMovimCajas.class, "doc_sop")
//
//			.createAlias("doc_sop.movimientosCaja", "movimientosCaja")
//			.createAlias("movimientosCaja.tiposMovimientoCaja", "tipoMov")
//			.createAlias("movimientosCaja.turnoDeCaja", "turnoCaja")
//			.createAlias("formasPago", "fp")
//	
//			.add(Restrictions.in("tipoMov.codigo", codigosTipoMovimiento))
//			.add(Restrictions.eq("turnoCaja.codigoPk", turnoDeCaja.getCodigoPk()))
//			.add(Restrictions.eq("fp.tiposDetalleFormaPago.codigo", ConstantesBD.codigoTipoDetalleFormasPagoNinguno));
//		
//		if(codigoMovimiento!= ConstantesBD.codigoNuncaValidoLong){
//			
//			criteria.add(Restrictions.eq("movimientosCaja.codigoPk", codigoMovimiento));
//		}
//
//		return (List<DocSopMovimCajas>) criteria.list() ;
//	}
	
//	/**
//	 * M&eacute;todo que retorna el total de las devoluciones de los recibos de caja realizados en un turno espec&iacute;fico y
//	 * teniendo en cuenta un estado. No se incluyen las que han sido registradas en los arqueos realizados anteriormente.
//	 * 
//	 * @param movimientosCaja
//	 * @param fecha
//	 * @param estado
//	 */
//	@SuppressWarnings("unchecked")
//	public List<DtoFaltanteSobrante> obtenerTotalesFaltanteSobranteMovimiento(MovimientosCaja movimientosCaja) 
//	{
//
//		try {
//			
//			List<DtoFaltanteSobrante> listaTotalFaltSob = (List<DtoFaltanteSobrante>)  sessionFactory.getCurrentSession().createCriteria(DetFaltanteSobrante.class, "detFalSob")
//			.createAlias("docSopMovimCajas", "docSop")
//			.createAlias("docSop.movimientosCaja", "movCaja")
//			.createAlias("movCaja.entregaCajaMayorByCodigoPk", "entregaCajaMayor", Criteria.LEFT_JOIN)
//			.createAlias("entregaCajaMayor.movimientosCajaByCodigoPk", "")
//			.createAlias("movEntregaCajaMayor.docSopMovimCajases", "docSopEntrega")
//			.createAlias("movCaja.solicitudTrasladoCajaByCodigoPk", "solTrasladoCaja", Criteria.LEFT_JOIN)
//			.createAlias("solTrasladoCaja.movimientosCajaByCodigoPk", "movSolTraslado")
//			.createAlias("movSolTraslado.docSopMovimCajases", "docSopSolicitud")
//			.createAlias("docSop.formasPago", "forpag")
//			
//			.setProjection(Projections.projectionList()
//					.add( Projections.sum("valorDiferencia"), "valorDiferencia")
//					.add( Projections.groupProperty("forpag.consecutivo") , "tipoFormaPago")
//					.add( Projections.groupProperty("forpag.descripcion")	, "formaPago")
//				
//			)
//			.add(Restrictions.eq("movCaja.codigoPk", movimientosCaja.getCodigoPk()))
//			.list();
//			
//			return listaTotalFaltSob;
//			
//		} catch (Exception e) {
//			
//			return null;
//		}
//	}
	
//	/**
//	 * Obtiene los documentos correspondientes a movimientos de traslado de efectivo.
//	 * Si el movimiento tiene asignado un codigoPk, filtra la informaci&oacute;n que solo
//	 * este relacionada a ese movimiento espec&iacute;fico
//	 * 
//	 * @param movimientosCaja
//	 * @return
//	 */
//	@SuppressWarnings("unchecked")
//	public List<DtoDetalleDocSopor> obtenerTrasladosEfecdddghhhhhhhhhh(MovimientosCaja movimientosCaja) 
//	{
//	
//		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(DocSopMovimCajas.class, "dsmc")
//			.createAlias("movimientosCaja", "mc")
//			.createAlias("mc.tiposMovimientoCaja", "tmc")
//			.createAlias("formasPago", "fp")
//			.createAlias("fp.tiposDetalleFormaPago", "tipoDetFormaPago")
//			//.createAlias("detallePagosRcs", "detpag")
//			//.createAlias("detpag.recibosCaja","rc")
//	
//			.setProjection( Projections.projectionList()
//					.add( Projections.property("valor"), "valorSistemaBig" )
//					.add( Projections.property("fp.descripcion"), "formaPago" )
//					.add( Projections.property("fp.consecutivo"), "consecutivoFormaPago" )
//					.add( Projections.property("rc.id.numeroReciboCaja"), "reciboCajaId" ))
//					//.add( Projections.property("detpag.consecutivo"), "detallePagosId" ))
//					
//			.add(Restrictions.between("tmc.codigo", ConstantesBD.codigoTipoMovimientoEntregaTransportadora,ConstantesBD.codigoTipoMovimientoEntregaCajaPrincipalMayor))
//			.add(Restrictions.eq("mc.turnoDeCaja", movimientosCaja.getTurnoDeCaja())) 
//			.add(Restrictions.eq("tipoDetFormaPago.codigo", ConstantesBD.codigoTipoDetalleFormasPagoNinguno ));
//	
//	  	if(movimientosCaja.getCodigoPk()>0)
//	  	{
//			criteria.add(Restrictions.eq("mc.codigoPk", movimientosCaja.getCodigoPk()));
//		}
//		
//		criteria.setResultTransformer( Transformers.aliasToBean(DtoDetalleDocSopor.class));
//		
//		List<DtoDetalleDocSopor> lista =criteria.list();
//		
//		return lista;
//	}
	
//	/**
//	 * Obtiene los documentos correspondientes a movimientos de aceptaci&oacute;n de efectivo.
//	 * @param movimientosCaja
//	 * @return
//	 */
//	@SuppressWarnings("unchecked")
//	public List<DtoDetalleDocSopor> obtenerListadoAceptacionesEfectivojjjjjjj(MovimientosCaja movimientosCaja) 
//	{
//		Criteria criteria   = sessionFactory.getCurrentSession().createCriteria(MovimientosCaja.class, "mc")
//			.createAlias("tiposMovimientoCaja", "tmc")
//			.createAlias("aceptacionTrasladoCaja", "acepTrasCaja")
//			.createAlias("acepTrasCaja.solicitudTrasladoCaja", "solTrasCaja")
//			.createAlias("solTrasCaja.movimientosCajaByCodigoPk", "movCajaSol")
//			.createAlias("movCajaSol.docSopMovimCajases", "docSopSol")
//			.createAlias("docSopSol.detallePagosRcs", "detpag")
//			.createAlias("docSopSol.formasPago", "fp")
//			.createAlias("fp.tiposDetalleFormaPago", "tdFp")
//			.createAlias("detpag.recibosCaja","rc")
//		  
//			.setProjection( Projections.projectionList()
//					.add( Projections.property("detpag.valor"), "valorSistema" )
//					.add( Projections.property("fp.descripcion"), "formaPago" )
//					.add( Projections.property("fp.consecutivo"), "consecutivoFormaPago" )
//					.add( Projections.property("rc.id.numeroReciboCaja"), "reciboCajaId" )
//					.add( Projections.property("detpag.consecutivo"), "detallePagosId" ))
//					
//			.add(Restrictions.eq("tdFp.codigo", ConstantesBD.codigoTipoDetalleFormasPagoNinguno ));
//
//	   //En este caso se consulta una solicitud para realizar la apertura de turno     
//	   if(movimientosCaja.getCodigoPk()>0)
//	   {
//		 criteria.add(Restrictions.eq("mc.codigoPk", movimientosCaja.getCodigoPk()));
//	   
//	   }else{ //En este caso se consulta las solicitudes que ya fueron aceptadas para realizar movimientos de tipo arqueo.
//		  
//		   criteria.add(Restrictions.eq("tmc.codigo",ConstantesBD.codigoTipoMovimientoAperturaTurnoCaja))
//		   .add(Restrictions.eq("solTrasCaja.estado",  ConstantesIntegridadDominio.acronimoEstadoSolicitudTrasladoCajaAceptado))
//		   .add(Restrictions.eq("turnoDeCaja", movimientosCaja.getTurnoDeCaja()));
//	   }
//
//	   return (List<DtoDetalleDocSopor>) criteria.setResultTransformer( Transformers.aliasToBean(DtoDetalleDocSopor.class))
//	   .list();
//
//	}


}
