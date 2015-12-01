package com.servinte.axioma.orm.delegate.tesoreria;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;

import com.princetonsa.dto.tesoreria.DtoDetalleDocSopor;
import com.servinte.axioma.orm.AceptacionTrasladoCaja;
import com.servinte.axioma.orm.AceptacionTrasladoCajaHome;
import com.servinte.axioma.orm.TurnoDeCaja;

/**
 * 
 * Esta clase se encarga de manejar las transaccciones relacionadas
 * con el objeto {@link AceptacionTrasladoCaja}.
 * 
 * @author Jorge Armando Agudelo Quintero
 *
 */


public class AceptacionTrasladoCajaDelegate extends AceptacionTrasladoCajaHome{

	
	/**
	 * Aceptaciones por Turno de Caja
	 * 
	 * @param turnoDeCaja
	 * @return List<{@link AceptacionTrasladoCaja}> de Aceptaciones por turno de Caja
	 */
	
	@SuppressWarnings("unchecked")
	public List<AceptacionTrasladoCaja> obtenerAceptacionesSolicitudTrasladoPorTurnoCaja (TurnoDeCaja turnoDeCaja)
	{
	
		return (List<AceptacionTrasladoCaja>) sessionFactory.getCurrentSession().createCriteria(AceptacionTrasladoCaja.class)
		.createAlias("movimientosCaja", "movCajaAcep")
		.add(Restrictions.eq("movCajaAcep.turnoDeCaja", turnoDeCaja))
		.list();

	}
	
	
	/**
	 * M&eacute;todo que se encarga de totalizar por forma de pago asociada a tipo "NINGUNO", las Aceptaciones de Solicitud de 
	 * Traslado a Caja de Recaudo realizadas.
	 * 
	 * @param turnoDeCaja
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<DtoDetalleDocSopor>  obtenerTotalesAceptacionSolicitudFormaPagoNinguno (TurnoDeCaja turnoDeCaja){
		
		List<DtoDetalleDocSopor> lista =  sessionFactory.getCurrentSession().createCriteria(AceptacionTrasladoCaja.class)
			.createAlias("solicitudTrasladoCaja", "solTrasCaja")
			.createAlias("movimientosCaja", "mc")
			.createAlias("mc.tiposMovimientoCaja", "tmc")
			.createAlias("mc.docSopMovimCajases", "docSopAcep")
			.createAlias("docSopAcep.formasPago", "fp")
			.createAlias("fp.tiposDetalleFormaPago", "tdFp")
	  
			.setProjection( Projections.projectionList()
					.add(Projections.sum("docSopAcep.valor"), "valorSistemaBig")
					.add( Projections.property("fp.descripcion"), "formaPago" )
					.add( Projections.property("fp.consecutivo"), "consecutivoFormaPago" )
					.add(Projections.groupProperty("fp.consecutivo"))
					.add(Projections.groupProperty("fp.descripcion"))
			)
				
			.add(Restrictions.eq("tdFp.codigo", ConstantesBD.codigoTipoDetalleFormasPagoNinguno ))
			.add(Restrictions.eq("tmc.codigo",ConstantesBD.codigoTipoMovimientoAperturaTurnoCaja))
			.add(Restrictions.eq("mc.turnoDeCaja", turnoDeCaja)) 
			.add(Restrictions.eq("solTrasCaja.estado",  ConstantesIntegridadDominio.acronimoEstadoSolicitudTrasladoCajaAceptado))
			.setResultTransformer( Transformers.aliasToBean(DtoDetalleDocSopor.class)).list();
		
		return lista;
		
	}


	/**
	 * Método que se encarga de verificar que no exista un registro de Aceptacion para Solicitudes
	 * de traslado a Cajas de Recaudo realizadas.
	 * 
	 * @param codigosSolicitudes
	 * @return boolean indicando si existe un registro para la(s) Solicitudes de Traslado a Caja de Recaudo
	 */
	@SuppressWarnings("unchecked")
	public boolean existeAceptacionTrasladoPorSolicitudes(ArrayList<Long> codigosSolicitudes) {
		
		boolean resultado = false;
		
		if(codigosSolicitudes!=null && codigosSolicitudes.size()>0){
			
			List<DtoDetalleDocSopor> lista =  sessionFactory.getCurrentSession().createCriteria(AceptacionTrasladoCaja.class)
			.createAlias("solicitudTrasladoCaja", "solTrasCaja")
			.createAlias("solTrasCaja.movimientosCajaByCodigoPk", "movCajaSol")
			
			.setProjection(Projections.property("movCajaSol.codigoPk"))
			
			.add(Restrictions.in("movCajaSol.codigoPk", codigosSolicitudes))
			.add(Restrictions.eq("solTrasCaja.estado",  ConstantesIntegridadDominio.acronimoEstadoSolicitudTrasladoCajaAceptado)).list();
		
			if(lista!=null && lista.size()>0){
				
				resultado =  true;
				
			}
		}
		
		return resultado;
	}
}
