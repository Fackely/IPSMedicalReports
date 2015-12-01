/*
 * Mayo 12, 2010
 */
package com.servinte.axioma.orm.delegate.tesoreria;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadTexto;
import util.Utilidades;

import com.princetonsa.dto.tesoreria.DtoConsultaTrasladoRecaudoMayorEnCierre;
import com.princetonsa.dto.tesoreria.DtoConsultaTrasladosCajasRecaudo;
import com.princetonsa.dto.tesoreria.DtoDetalleDocSopor;
import com.princetonsa.dto.tesoreria.DtoSolicitudTrasladoPendiente;
import com.princetonsa.dto.tesoreria.DtoTrasladoCaja;
import com.servinte.axioma.orm.Cajas;
import com.servinte.axioma.orm.DocSopMovimCajas;
import com.servinte.axioma.orm.MovimientosCaja;
import com.servinte.axioma.orm.SolicitudTrasladoCaja;
import com.servinte.axioma.orm.SolicitudTrasladoCajaHome;
import com.servinte.axioma.orm.TurnoDeCaja;


/**
 * 
 * Esta clase se encarga de manejar las transaccciones relacionadas
 * con el objeto {@link SolicitudTrasladoCaja}.
 * 
 * @author Jorge Armando Agudelo Quintero
 *
 */

public class SolicitudTrasladoCajaDelegate extends SolicitudTrasladoCajaHome{
	
	
	/**
	 * Lista todos
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<SolicitudTrasladoCaja> listarTodos()
	{
		return (ArrayList<SolicitudTrasladoCaja>) sessionFactory.getCurrentSession()
			.createCriteria(SolicitudTrasladoCaja.class)
			.list();
	}

	
	
	/**
	 * Retorna una lista de las solicitudes de traslado activas para la caja
	 * @param caja
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<DtoSolicitudTrasladoPendiente> obtenerSolicitudesPendientes(Cajas caja)
	{
		
		ArrayList<DtoSolicitudTrasladoPendiente> listaDtoSolicitudesPendientes = new ArrayList<DtoSolicitudTrasladoPendiente>();
			listaDtoSolicitudesPendientes =(ArrayList<DtoSolicitudTrasladoPendiente>) sessionFactory.getCurrentSession()
				
				.createCriteria(SolicitudTrasladoCaja.class)
					.add(Expression.eq("estado", ConstantesIntegridadDominio.acronimoEstadoSolicitudTrasladoCajaSolicitado))
					.add(Expression.eq("cajas.consecutivo", caja.getConsecutivo()))
				
				.createAlias("movimientosCajaByCodigoPk", "movimientocaja")
				.createAlias("movimientosCajaByCodigoPk.turnoDeCaja", "turno")
				.createAlias("turno.cajas", "caja")
				.createAlias("turno.usuarios", "usuario")
				.createAlias("usuario.personas", "cajero")
				 
				.setProjection(Projections.projectionList()
					.add( Projections.property("movimientoCaja"), "movimientoCaja")
					.add( Projections.property("movimientocaja.fecha"), "fecha")
					.add( Projections.property("movimientocaja.hora"), "hora")
					.add( Projections.property("caja.consecutivo"), "consecutivo")
					.add( Projections.property("caja.descripcion"), "descripcion")
					.add( Projections.property("cajero.primerNombre"), "primerNombre")
					.add( Projections.property("cajero.primerApellido"), "primerApellido")
				)
				
				.setResultTransformer( Transformers.aliasToBean(DtoSolicitudTrasladoPendiente.class) )
		        .list();
			
		return listaDtoSolicitudesPendientes;
	}
	
	
	/**
	 * Retorna una lista de las solicitudes de traslado activas para la caja
	 * @param caja
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<SolicitudTrasladoCaja> obtenerSolicitudesPendientesPorCaja(Cajas caja)
	{
		ArrayList<SolicitudTrasladoCaja> listaSolicitudesPendientes = new ArrayList<SolicitudTrasladoCaja>();
			listaSolicitudesPendientes =(ArrayList<SolicitudTrasladoCaja>) sessionFactory.getCurrentSession()
				.createCriteria(SolicitudTrasladoCaja.class)
				.add(Expression.eq("estado", ConstantesIntegridadDominio.acronimoEstadoSolicitudTrasladoCajaSolicitado))
				.add(Expression.eq("cajas.consecutivo", caja.getConsecutivo()))
				.list();
		
		if(!Utilidades.isEmpty(listaSolicitudesPendientes)){
			for(SolicitudTrasladoCaja solicitud :listaSolicitudesPendientes){
				solicitud.getMovimientosCajaByCodigoPk().getFecha();
				solicitud.getMovimientosCajaByCodigoPk().getTurnoDeCaja().getCajas().getDescripcion();
				solicitud.getMovimientosCajaByCodigoPk().getTurnoDeCaja().getUsuarios().getPersonas().getPrimerNombre();
			}
		}
		
		return listaSolicitudesPendientes;
	}	
	
	

	/**
	 * Lista de las solicitudes de apertura de turno de caja con faltante sobrante y 
	 * con valor recibido por caja y cajero pasados como par&aacute;metro
	 * 
	 * @param turnoDeCaja
	 * @return  List<@link DtoTrasladoCaja}>
	 */
	public List<DtoTrasladoCaja> obtenerSolicitudesAceptadasXTurnoCajaCajero(TurnoDeCaja turnoDeCaja)
	{

		List<DtoTrasladoCaja> listaSolicitudes = obtenerSolicitudesPorCajaPorEstado(turnoDeCaja.getCajas(), ConstantesIntegridadDominio.acronimoEstadoSolicitudTrasladoCajaAceptado);
		List<DtoTrasladoCaja> listaAceptaciones = obtenerAceptacionesPorTurnoCajaCajero(turnoDeCaja);
		
		DocSopMovimCajasDelegate docSopMovimCajasDelegate = new DocSopMovimCajasDelegate();
		
		List<DtoTrasladoCaja> listadoDefinitivo =  docSopMovimCajasDelegate.obtenerValoresAEntregarXSolicitudesAceptadas(listaSolicitudes, listaAceptaciones);

		return listadoDefinitivo;
		
	}
	
	
	/**
	 * Aceptaciones por turno, por Caja y por Cajero
	 * 
	 * @param caja
	 * @param usuario
	 * @return List<{@link DtoTrasladoCaja}> de Aceptaciones por turno, por Caja y por Cajero
	 */
	
	@SuppressWarnings("unchecked")
	private List<DtoTrasladoCaja> obtenerAceptacionesPorTurnoCajaCajero (TurnoDeCaja turnoDeCaja)
	{
	
		return (List<DtoTrasladoCaja>) sessionFactory.getCurrentSession().createCriteria(SolicitudTrasladoCaja.class)

		.createAlias("aceptacionTrasladoCajas", "aceptTrasCaja")
		.createAlias("aceptTrasCaja.movimientosCaja", "movCajaAcep")
		.createAlias("movCajaAcep.docSopMovimCajases", "dsmacept")
		.createAlias("dsmacept.formasPago", "fp")
		.createAlias("fp.tiposDetalleFormaPago", "tipoDetFormaPago")
		.createAlias("dsmacept.detFaltanteSobrantes", "dfsacept", Criteria.LEFT_JOIN)
		
		.setProjection(Projections.projectionList()
			.add( Projections.property("movimientoCaja"), "idMovimientoCaja")
			.add( Projections.property("fp.consecutivo"), "tipoFormaPago")
			.add( Projections.property("tipoDetFormaPago.codigo"), "tipoDetalleFormaPago")
			.add( Projections.property("fp.descripcion"), "formaPago")
			.add( Projections.property("dsmacept.valor"), "valorRecibido")
			.add( Projections.property("dfsacept.codigoPk"), "idDetalleFaltanteSobrante")
			.add( Projections.property("dfsacept.valorDiferencia"), "valorFaltante")
			.add( Projections.property("dfsacept.tipoDiferencia"), "tipoDiferencia")
			.add( Projections.property("dsmacept.codigoPk"), "codigoDocSopMovCaja")
			.add( Projections.property("dsmacept.codigoPk"), "codigoDocSopMovCaja")
			.add( Projections.property("aceptTrasCaja.movimientoCaja"), "codigoAceptacionSolicitud")
		)
		
		.add(Restrictions.eq("estado", ConstantesIntegridadDominio.acronimoEstadoSolicitudTrasladoCajaAceptado))
		.add(Restrictions.eq("movCajaAcep.turnoDeCaja", turnoDeCaja))

		.setResultTransformer( Transformers.aliasToBean(DtoTrasladoCaja.class))
		.list();

	}
	
	
	/**
	 * Solicitudes asociadas a una caja, seg&uacute;n el estado y la caja que se pasen como par&aacute;metros. 
	 * 
	 * @param caja
	 * @param usuario
	 * @return List<{@link DtoTrasladoCaja}> de Solicitudes por Caja
	 */
	@SuppressWarnings("unchecked")
	private List<DtoTrasladoCaja> obtenerSolicitudesPorCajaPorEstado (Cajas cajas, String estado)
	{

		return (List<DtoTrasladoCaja>) sessionFactory.getCurrentSession().createCriteria(SolicitudTrasladoCaja.class)

		.createAlias("movimientosCajaByCodigoPk", "movCajaSol")
	    .createAlias("movCajaSol.docSopMovimCajases", "docSopMovimCajasSol")
		.createAlias("docSopMovimCajasSol.formasPago", "fp")
		.createAlias("fp.tiposDetalleFormaPago", "tipoDetFormaPago")
		.createAlias("docSopMovimCajasSol.detFaltanteSobrantes", "detFalSobSol",Criteria.LEFT_JOIN)
		.createAlias("aceptacionTrasladoCajas", "aceptTrasCaja")
		
		.setProjection(Projections.projectionList()
			.add( Projections.property("movimientoCaja"), "idMovimientoCaja")
			.add( Projections.property("fp.consecutivo"), "tipoFormaPago")
			.add( Projections.property("fp.descripcion"), "formaPago")
			.add( Projections.property("tipoDetFormaPago.codigo"), "tipoDetalleFormaPago")
			.add( Projections.property("docSopMovimCajasSol.valor"), "valorTrasladado")
			.add( Projections.property("detFalSobSol.codigoPk"), "idDetalleFaltanteSobrante")
			.add( Projections.property("detFalSobSol.valorDiferencia"), "valorFaltante")
			.add( Projections.property("detFalSobSol.tipoDiferencia"), "tipoDiferencia")
			.add( Projections.property("docSopMovimCajasSol.codigoPk"), "codigoDocSopMovCaja")
			.add( Projections.property("aceptTrasCaja.movimientoCaja"), "codigoAceptacionSolicitud")
			
		)
		
		.add(Restrictions.eq("estado", estado))
		.add(Restrictions.eq("cajas", cajas))
		.setResultTransformer(Transformers.aliasToBean(DtoTrasladoCaja.class))
		
		.list();
	}

	
	/**
	 * 
	 * Este M&eacute;todo se encarga de consultar los registros de las 
	 * solicitudes de traslado de caja recaudo
	 * 
	 * @param DtoConsultaTrasladosCajasRecaudo
	 * @return ArrayList<DtoConsultaTrasladosCajasRecaudo>
	 * @author, Angela Maria Aguirre
	 *
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<DtoConsultaTrasladosCajasRecaudo> consultarRegistrosSolicitudTrasladoCaja(
			DtoConsultaTrasladosCajasRecaudo dto){
		
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(MovimientosCaja.class,"movimiento")
		.createAlias("movimiento.solicitudTrasladoCajaByCodigoPk", "solicitud")
		.createAlias("movimiento.docSopMovimCajases", "documentoSoporte")
		.createAlias("movimiento.turnoDeCaja","turnoSolicitud")
		.createAlias("turnoSolicitud.cajas","cajaSolicitud")
		.createAlias("turnoSolicitud.usuarios","cajeroSolicitud")
		.createAlias("turnoSolicitud.centroAtencion","centroAtencion")
		.createAlias("solicitud.aceptacionTrasladoCajas", "aceptacion",Criteria.LEFT_JOIN)
		.createAlias("aceptacion.movimientosCaja", "movimientoAceptacion",Criteria.LEFT_JOIN)		
		.createAlias("movimientoAceptacion.turnoDeCaja", "turnoAceptacion",Criteria.LEFT_JOIN)
		.createAlias("turnoAceptacion.cajas","cajaDestino",Criteria.LEFT_JOIN)
		.createAlias("turnoAceptacion.usuarios","cajeroAceptacion",Criteria.LEFT_JOIN);
		

		if(dto.getConsecutivoCentroAtencion()!=null)
			criteria.add(Restrictions.eq("centroAtencion.consecutivo",dto.getConsecutivoCentroAtencion()));
		
		if(dto.getFechaInicial()!=null && dto.getFechaFin() !=null){
			criteria.add(Restrictions.between("movimiento.fecha",
					dto.getFechaInicial(), dto.getFechaFin()));
		}
		
		if(dto.getConsecutivoSolicitud()!=null && dto.getConsecutivoSolicitud()!=0){
			criteria.add(Restrictions.eq("movimiento.consecutivo",dto.getConsecutivoSolicitud()));
		}	
		
		if(!UtilidadTexto.isEmpty(dto.getEstadoSolicitud())){
			criteria.add(Restrictions.eq("solicitud.estado",dto.getEstadoSolicitud()));
		}
		
		if(dto.getCodigoCajaOrigen()!=ConstantesBD.codigoNuncaValido){
			criteria.add(Restrictions.eq("cajaSolicitud.consecutivo",dto.getCodigoCajaOrigen()));
		}
		
		if(dto.getCodigoCajaDestino()!=ConstantesBD.codigoNuncaValido){
			criteria.add(Restrictions.eq("cajaDestino.consecutivo",dto.getCodigoCajaDestino()));
		}
		
		if(!UtilidadTexto.isEmpty(dto.getLoginCajeroSolicitante())){
			criteria.add(Restrictions.eq("cajeroSolicitud.login",dto.getLoginCajeroSolicitante()));
		}
		
		if(!UtilidadTexto.isEmpty(dto.getLoginCajeroAcepta())){
			criteria.add(Restrictions.eq("cajeroAceptacion.login",dto.getLoginCajeroAcepta()));
		}
		
		criteria.setProjection(Projections.projectionList()
				.add(Projections.property("movimiento.fecha"),"fechaInicial")
				.add(Projections.property("movimiento.hora"),"horaSolicitud")
				.add(Projections.property("movimiento.consecutivo"),"consecutivoSolicitud")
				.add(Projections.property("cajeroSolicitud.login"),"loginCajeroSolicitante")				
				.add(Projections.property("solicitud.estado"),"estadoSolicitud")
				.add(Projections.property("movimiento.codigoPk"),"codigoPk")				
				.add(Projections.property("cajeroAceptacion.login"),"loginCajeroAcepta")
				.add(Projections.property("movimientoAceptacion.fecha"),"fechaAceptacion")
				.add(Projections.property("movimientoAceptacion.hora"),"horaAceptacion")
				.add(Projections.property("cajaSolicitud.descripcion"),"descripcionCajaOrigen")
				.add(Projections.property("cajaDestino.descripcion"),"descripcionCajaDestino")
				.add(Projections.property("movimiento.observaciones"),"observaciones")
				.add(Projections.property("movimientoAceptacion.observaciones"),"observacionesAceptacion")
				.add(Projections.sum("documentoSoporte.valor"),"valorTotalSolicitud")		
								
				.add(Projections.groupProperty("movimiento.fecha"))
				.add(Projections.groupProperty("movimiento.hora"))
				.add(Projections.groupProperty("movimiento.consecutivo"))
				.add(Projections.groupProperty("cajeroSolicitud.login"))
				.add(Projections.groupProperty("solicitud.estado"))
				.add(Projections.groupProperty("movimiento.codigoPk"))
				.add(Projections.groupProperty("cajeroAceptacion.login"))
				.add(Projections.groupProperty("movimientoAceptacion.fecha"))
				.add(Projections.groupProperty("movimientoAceptacion.hora"))
				.add(Projections.groupProperty("cajaSolicitud.descripcion"))
				.add(Projections.groupProperty("cajaDestino.descripcion"))
				.add(Projections.groupProperty("movimiento.observaciones"))
				.add(Projections.groupProperty("movimientoAceptacion.observaciones"))
				)
				
		.setResultTransformer( Transformers.aliasToBean(DtoConsultaTrasladosCajasRecaudo.class));
		ArrayList<DtoConsultaTrasladosCajasRecaudo> listaSolicitud=(ArrayList)criteria.list();
		
		return listaSolicitud;
	}
	
	
	/**
	 * Solicitud espec&iacute;fica, seg&uacute;n el consecutivo pasado como par&aacute;metro. 
	 * 
	 * @param consecutivo
	 * @return {@link DtoTrasladoCaja} con la Solicitud espec&iacute;fica
	 */
	public DtoTrasladoCaja obtenerSolicitudPorConsecutivo (long consecutivo)
	{

		return (DtoTrasladoCaja) sessionFactory.getCurrentSession().createCriteria(SolicitudTrasladoCaja.class)

		.createAlias("movimientosCajaByCodigoPk", "movCajaSol")
	    .createAlias("movCajaSol.docSopMovimCajases", "docSopMovimCajasSol")
		.createAlias("docSopMovimCajasSol.formasPago", "fp")
		.createAlias("fp.tiposDetalleFormaPago", "tipoDetFormaPago")
		.createAlias("docSopMovimCajasSol.detFaltanteSobrantes", "dfsacept",Criteria.LEFT_JOIN)
		
		.setProjection(Projections.projectionList()
			.add( Projections.property("movimientoCaja"), "idMovimientoCaja")
			.add( Projections.property("fp.consecutivo"), "tipoFormaPago")
			.add( Projections.property("fp.descripcion"), "formaPago")
			.add( Projections.property("tipoDetFormaPago.codigo"), "tipoDetalleFormaPago")
			.add( Projections.property("docSopMovimCajasSol.valor"), "valorTrasladado")
			.add( Projections.property("dfsacept.codigoPk"), "idDetalleFaltanteSobrante")
			.add( Projections.property("dfsacept.valorDiferencia"), "valorFaltante")
			.add( Projections.property("dfsacept.tipoDiferencia"), "tipoDiferencia")
			.add( Projections.property("docSopMovimCajasSol.codigoPk"), "codigoDocSopMovCaja")
		)
		
		.add(Restrictions.eq("movCajaSol.consecutivo", consecutivo))
		.setResultTransformer(Transformers.aliasToBean(DtoTrasladoCaja.class))
		.uniqueResult();
	}
	
	
	/**
	 * M&eacute;todo que se encarga de consultar la fecha m&aacute;xima registrada para una
	 * movimiento de caja de tipo Solicitud de Traslado a Caja en estado aceptada. 
	 * Este movimiento est&aacute; asociado a un turno de caja espec&iacute;fico.
	 * 
	 * @param turnoDeCaja
	 * @return Date, con la fecha m&aacute;xima registrada para este movimiento
	 */
	public Date obtenerFechaUltimoMovimientoAnulaciones(TurnoDeCaja turnoDeCaja){
		
		return (Date) sessionFactory.getCurrentSession().createCriteria(SolicitudTrasladoCaja.class)

		.createAlias("aceptacionTrasladoCajas", "atc")
		.createAlias("atc.movimientosCaja", "movCajaAcep")
		.setProjection(Projections.max("movCajaAcep.fecha"))
		
		.add(Restrictions.eq("estado", ConstantesIntegridadDominio.acronimoEstadoSolicitudTrasladoCajaAceptado))
		.add(Restrictions.eq("movCajaAcep.turnoDeCaja", turnoDeCaja)).uniqueResult();
	}
	
	
	/**
	 * M&eacute;todo que se encarga de obtener toda la informaci&oacute;n asociada
	 * a una Solicitud de Traslado a Caja espec&iacute;fica.
	 * 
	 * @param codigoSolicitud
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<DtoDetalleDocSopor> obtenerDocSopSolicitudTrasladoCaja (long codigoSolicitud)
	{

		List<DtoDetalleDocSopor> listaDetalleDocumentos = (List<DtoDetalleDocSopor>) sessionFactory.getCurrentSession()
		
		.createCriteria(SolicitudTrasladoCaja.class, "solTrasCaja")
			.createAlias("solTrasCaja.movimientosCajaByCodigoPk", "movCaja")
			.createAlias("movCaja.docSopMovimCajases", "docSopMovCaja")
			.createAlias("docSopMovCaja.detallePagosRcs", "detPago", Criteria.LEFT_JOIN)
			.createAlias("detPago.recibosCaja", "reciboCaja",  Criteria.LEFT_JOIN)
			.createAlias("reciboCaja.detalleConceptosRcs", "detConcepto" , Criteria.LEFT_JOIN)
			.createAlias("docSopMovCaja.formasPago", "formaPago")
			.createAlias("formaPago.tiposDetalleFormaPago","detFormaPago")
			
			.add(Expression.eq("solTrasCaja.movimientoCaja", codigoSolicitud))
				
			.setProjection(Projections.projectionList()
					
					.add(Projections.property("detConcepto.nombreBeneficiario")	, "nombreRecibidoDe")
					.add(Projections.property("docSopMovCaja.nroDocumento") , "nroDocumentoEntregado")
					.add(Projections.property("docSopMovCaja.valor") , "valorSistemaBig")
					.add(Projections.property("formaPago.consecutivo")	, "consecutivoFormaPago")
					.add(Projections.property("detFormaPago.codigo") , "tiposDetalleFormaPago")
					.add(Projections.property("formaPago.descripcion")	, "formaPago")
					.add(Projections.property("detPago.consecutivo") , "detallePagosId")
					
			)
		   	.addOrder(Order.asc("detFormaPago.prioridad"))
		   	.addOrder(Order.asc("formaPago.consecutivo"))
		   	
			.setResultTransformer( Transformers.aliasToBean(DtoDetalleDocSopor.class)).list();
		  
			for(DtoDetalleDocSopor dtoDetalleDocSopor : listaDetalleDocumentos)
			{
				if(dtoDetalleDocSopor.getTiposDetalleFormaPago() == ConstantesBD.codigoTipoDetalleFormasPagoCheque || 
						dtoDetalleDocSopor.getTiposDetalleFormaPago() == ConstantesBD.codigoTipoDetalleFormasPagoTarjeta){
					
					dtoDetalleDocSopor = obtenerInformacionEntidadPorDetalle(dtoDetalleDocSopor);
				}
			}
			
		return listaDetalleDocumentos;
	}
	
	
	/**
	 *  M&eacute;todo que se encarga de completar la informaci&oacute;n asociada a los documentos de soporte
	 *  con formas de pago tipo Cheque y Tarjeta.
	 *  
	 * @param dtoDetalleDocSopor
	 * @return
	 */
	private  DtoDetalleDocSopor obtenerInformacionEntidadPorDetalle(DtoDetalleDocSopor dtoDetalleDocSopor)
	{
		String movimientoEntidad;
		
		switch (dtoDetalleDocSopor.getTiposDetalleFormaPago()) 
		{
			case ConstantesBD.codigoTipoDetalleFormasPagoCheque:
				movimientoEntidad = "movimientosChequeses";
				break;
			
			case ConstantesBD.codigoTipoDetalleFormasPagoTarjeta:
				movimientoEntidad = "movimientosTarjetases";
				break;
				
			default:
				movimientoEntidad = null;
				break;
		}
	
		if(movimientoEntidad != null && !"".equals(movimientoEntidad))
		{
			DtoDetalleDocSopor dtoDetalle = (DtoDetalleDocSopor) sessionFactory.getCurrentSession()
			.createCriteria(DocSopMovimCajas.class, "docSop")
				.createAlias("detallePagosRcs", "detPago")
				.createAlias("detPago.recibosCaja",	"reciboCaja")
				.createAlias("reciboCaja.detalleConceptosRcs", "detConcepto")
				.createAlias("detPago."+movimientoEntidad+"","movimientoEntidad")
				.createAlias("movimientoEntidad.entidadesFinancieras"	,"entidadFinanciera")
				.createAlias("entidadFinanciera.terceros"				,"tercero")
				
			.add(Expression.eq("docSop.nroDocumento"	, dtoDetalleDocSopor.getNroDocumentoEntregado()))
		
			.setProjection(Projections.projectionList()
					.add( Projections.property("entidadFinanciera.consecutivo")	, "consecutivoEntFinanciera")
					.add( Projections.property("tercero.descripcion") , "descripcionEntFinanciera")
			)
			
			.setResultTransformer(Transformers.aliasToBean(DtoDetalleDocSopor.class) )
			.setMaxResults(1).uniqueResult();
			
			dtoDetalleDocSopor.setConsecutivoEntFinanciera(dtoDetalle.getConsecutivoEntFinanciera());
		
			if(dtoDetalle.getDescripcionEntFinanciera() != null){
				
				dtoDetalleDocSopor.setDescripcionEntFinanciera(dtoDetalle.getDescripcionEntFinanciera());
			}
		}
		
		return dtoDetalleDocSopor;
	}
	
	/**
	 * Este método se encarga de consultar las solicitudes de traslados a caja de 
	 * recaudo y a caja mayor realizados en el cierre
	 * @param TurnoDeCaja
	 * @return ArrayList<DtoConsultaTrasladoRecaudoMayorEnCierre>
	 */
	@SuppressWarnings("rawtypes")
	public ArrayList<DtoConsultaTrasladoRecaudoMayorEnCierre> consultarTrasladosCajaRecaudoMayorEnCierre(TurnoDeCaja turnoDeCaja){
		
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(MovimientosCaja.class,"movimientoCaja")
		.createAlias("movimientoCaja.tiposMovimientoCaja", "tipoMovimientoCaja")
		.createAlias("movimientoCaja.solicitudTrasladoCajaByCodigoPk", "solicitudTrasladoCaja", Criteria.LEFT_JOIN)
		.createAlias("solicitudTrasladoCaja.cajas", "caja", Criteria.LEFT_JOIN)
		.createAlias("caja.centroAtencion", "centroAtencionCaja", Criteria.LEFT_JOIN)
		.createAlias("solicitudTrasladoCaja.usuarios", "usuario", Criteria.LEFT_JOIN)
		.createAlias("usuario.personas", "persona", Criteria.LEFT_JOIN)
		.createAlias("movimientoCaja.docSopMovimCajases", "docSopMovimCajas", Criteria.LEFT_JOIN)
		.createAlias("docSopMovimCajas.formasPago", "formasPago", Criteria.LEFT_JOIN)
		
		.createAlias("movimientoCaja.turnoDeCaja", "turnoCaja")
		
		.createAlias("movimientoCaja.entregaCajaMayorByCodigoPk", "entregaCajaMayor", Criteria.LEFT_JOIN)
		.createAlias("entregaCajaMayor.movimientosCajaByMovimientoCajaArqueo", "movimientoCajaArqueo", Criteria.LEFT_JOIN)
		.createAlias("movimientoCajaArqueo.tiposMovimientoCaja", "tipoMovArqueo", Criteria.LEFT_JOIN)
		
		.createAlias("entregaCajaMayor.cajas", "cajaMayor", Criteria.LEFT_JOIN)
		.createAlias("cajaMayor.centroAtencion", "centroAtencionCajaMayor", Criteria.LEFT_JOIN)
		;
		
		criteria.add(Restrictions.eq("turnoCaja.codigoPk", turnoDeCaja.getCodigoPk()));
		
		Disjunction tipoMovDis = Restrictions.disjunction();
		tipoMovDis.add(Restrictions.ne("tipoMovArqueo.codigo", ConstantesBD.codigoTipoMovimientoArqueoEntregaParcial));
		tipoMovDis.add(Restrictions.isNull("tipoMovArqueo.codigo"));
		criteria.add(tipoMovDis);

		criteria.setProjection(Projections.projectionList()	
				.add(Projections.property("movimientoCaja.consecutivo"),"nroConsecutivoMovimiento")
				.add(Projections.property("movimientoCaja.observaciones"),"observacionesTraslado")
				.add(Projections.property("tipoMovimientoCaja.codigo"),"codigoTipoMovimiento")
				.add(Projections.property("caja.codigo"),"codigoCajaRecaudo")
				.add(Projections.property("caja.descripcion"),"descripcionCajaRecaudo")
				.add(Projections.property("centroAtencionCaja.descripcion"),"descripcionCentroAtenCajaRecaudo")
				.add(Projections.property("persona.primerNombre"),"primerNombreTestigo")
				.add(Projections.property("persona.segundoNombre"),"segundoNombreTestigo")
				.add(Projections.property("persona.primerApellido"),"primerApellidoTestigo")
				.add(Projections.property("persona.segundoApellido"),"segundoApellidoTestigo")
				.add(Projections.property("formasPago.consecutivo"),"consecutivoFormaPago")
				.add(Projections.property("formasPago.descripcion"),"descripcionFormaPago")
				.add(Projections.sum("docSopMovimCajas.valor"),"valorTrasladado")
				.add(Projections.property("cajaMayor.codigo"),"codigoCajaMayor")
				.add(Projections.property("cajaMayor.descripcion"),"descripcionCajaMayor")
				.add(Projections.property("centroAtencionCajaMayor.descripcion"),"descripcionCentroAtenCajaMayor")
				
				.add(Projections.groupProperty("formasPago.consecutivo"))
				.add(Projections.groupProperty("formasPago.descripcion"))
				.add(Projections.groupProperty("tipoMovimientoCaja.codigo"))
				.add(Projections.groupProperty("movimientoCaja.consecutivo"))
				.add(Projections.groupProperty("movimientoCaja.observaciones"))
				.add(Projections.groupProperty("caja.codigo"))
				.add(Projections.groupProperty("caja.descripcion"))
				.add(Projections.groupProperty("centroAtencionCaja.descripcion"))
				.add(Projections.groupProperty("persona.primerNombre"))
				.add(Projections.groupProperty("persona.segundoNombre"))
				.add(Projections.groupProperty("persona.primerApellido"))
				.add(Projections.groupProperty("persona.segundoApellido"))
				.add(Projections.groupProperty("cajaMayor.codigo"))
				.add(Projections.groupProperty("cajaMayor.descripcion"))
				.add(Projections.groupProperty("centroAtencionCajaMayor.descripcion"))
				
				
			).setResultTransformer( Transformers.aliasToBean(DtoConsultaTrasladoRecaudoMayorEnCierre.class));
		 ArrayList<DtoConsultaTrasladoRecaudoMayorEnCierre> res=(ArrayList)criteria.list();
		
		return res;
		
	}
}
