package com.servinte.axioma.orm.delegate.tesoreria;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;

import util.ConstantesBD;
import util.UtilidadFecha;
import util.UtilidadTexto;

import com.ibm.icu.util.Calendar;
import com.princetonsa.dto.odontologia.DtoPresupuestosOdontologicosContratados;
import com.princetonsa.dto.tesoreria.DtoAnticiposRecibidosConvenio;
import com.princetonsa.dto.tesoreria.DtoDetalleDocSopor;
import com.princetonsa.dto.tesoreria.DtoFormaPago;
import com.princetonsa.dto.tesoreria.DtoFormaPagoReport;
import com.princetonsa.dto.tesoreria.DtoReciboDevolucion;
import com.princetonsa.dto.tesoreria.DtoRecibosConceptoAnticiposRecibidosConvenio;
import com.princetonsa.dto.tesoreria.DtoReporteAnticiposRecibidosConvenio;
import com.servinte.axioma.orm.EstadosRecibosCaja;
import com.servinte.axioma.orm.MovimientosCaja;
import com.servinte.axioma.orm.RecibosCaja;
import com.servinte.axioma.orm.RecibosCajaHome;
import com.servinte.axioma.orm.RecibosCajaId;
import com.servinte.axioma.orm.TurnoDeCaja;
import com.servinte.axioma.persistencia.UtilidadTransaccion;

/**
 * 
 * Esta clase se encarga de manejar las transaccciones relacionadas
 * con el objeto {@link RecibosCaja}
 * 
 * @author Jorge Armando Agudelo Quintero
 *
 */

public class RecibosCajaDelegate extends RecibosCajaHome {
	
	
	/**
	 * Recibos de caja filtrados solo por el turno de caja, cuando el movimiento de caja no ha sido registrado.
	 * Se involucra la fecha y la hora en la b&uacute;squeda, cuando el movimiento de caja est&aacute; registrado
	 * en el sistema.
	 * 
	 * @param movimientosCaja
	 * @return List<{@link DtoReciboDevolucion}>
	 */
	@SuppressWarnings("unchecked")
	public List<DtoReciboDevolucion> obtenerRecibosXMovimientoCaja(MovimientosCaja movimientosCaja) 
	{

		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(RecibosCaja.class, "rc")

		  .createAlias("estadosRecibosCaja", "esrec")
		  .createAlias("detalleConceptosRcs", "detcon")
		  .createAlias("detallePagosRcs", "detpag")
		  .createAlias("detpag.formasPago", "forpag")
		  .createAlias("recibosCajaXTurnos","rcxt")
		  .setProjection( Projections.projectionList()
				  
		   .add( Projections.property("id.numeroReciboCaja"), "numeroReciboCaja")
		   .add( Projections.property("consecutivoRecibo"), "consecutivoRecibosCaja" )
           .add( Projections.property("esrec.codigo"), "codigoEstadoReciboCaja" )
           .add( Projections.property("esrec.descripcion"), "estadoRecibo" )
           .add( Projections.property("detcon.nombreBeneficiario"), "recibidoDe" )
           .add( Projections.property("detcon.numeroIdBeneficiario"), "numIdentificacion" )
           .add( Projections.property("forpag.descripcion"), "formaPago" )
           .add( Projections.property("forpag.consecutivo"), "tipoFormaPago" )
           .add( Projections.property("detpag.valor"), "valor" )
           .add( Projections.property("detpag.consecutivo"), "idDetalleReciboPago" ))
  			
		.add(Restrictions.eq("rcxt.turnoDeCaja", movimientosCaja.getTurnoDeCaja()));
		
		if(movimientosCaja.getCodigoPk()>0){
			
			if(movimientosCaja.getFecha()!=null){
				
				List<RecibosCaja> recibosFiltradosFecha = sessionFactory.getCurrentSession()
					.createCriteria(RecibosCaja.class, "rc")
					.createAlias("recibosCajaXTurnos","rcxt")
					.add(Restrictions.eq("rcxt.turnoDeCaja", movimientosCaja.getTurnoDeCaja()))
					.add(Restrictions.le("fecha", movimientosCaja.getFecha())).list();
						
				ArrayList<String> codigosRecibosCaja =  new ArrayList<String>();
				codigosRecibosCaja.add(ConstantesBD.codigoNuncaValido+"");
				for (RecibosCaja recibosCaja : recibosFiltradosFecha) {
					
					if(UtilidadFecha.esHoraMenorIgualQueOtraReferencia(UtilidadFecha.conversionFormatoHoraABD(recibosCaja.getHora()) , movimientosCaja.getHora())){
						
						codigosRecibosCaja.add(recibosCaja.getId().getNumeroReciboCaja());
					
					}else if(UtilidadFecha.esFechaMenorQueOtraReferencia(UtilidadFecha.conversionFormatoFechaAAp(recibosCaja.getFecha()), UtilidadFecha.conversionFormatoFechaAAp( movimientosCaja.getFecha()))){
						
						codigosRecibosCaja.add(recibosCaja.getId().getNumeroReciboCaja());
					}
				}
			
				criteria.add(Restrictions.in("id.numeroReciboCaja", codigosRecibosCaja));
			}
		}
		
		List<DtoReciboDevolucion> lista =  criteria.addOrder(Order.asc("id.numeroReciboCaja"))
		.setResultTransformer(Transformers.aliasToBean(DtoReciboDevolucion.class))
		.list();
		
		return lista;
		
	}

	/**
	 * Recibos de caja filtrados solo por el turno de caja, cuando el movimiento de caja no ha sido registrado.
	 * Se involucra la fecha y la hora en la b&uacute;squeda, cuando el movimiento de caja est&aacute; registrado
	 * en el sistema.
	 * 
	 * @param movimientosCaja
	 * @return List<{@link DtoReciboDevolucion}>
	 */
	@SuppressWarnings("unchecked")
	public List<DtoReciboDevolucion> obtenerRecibosXMovimientoCajaFecha(MovimientosCaja movimientosCaja) 
	{

		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(RecibosCaja.class, "rc")

		.createAlias("estadosRecibosCaja", "esrec")
		.createAlias("detalleConceptosRcs", "detcon")
		.createAlias("detallePagosRcs", "detpag")
		.createAlias("detpag.formasPago", "forpag")
		.createAlias("recibosCajaXTurnos","rcxt")
		.setProjection( Projections.projectionList()

				.add( Projections.property("id.numeroReciboCaja"), "numeroReciboCaja")
				.add( Projections.property("consecutivoRecibo"), "consecutivoRecibosCaja" )
				.add( Projections.property("esrec.codigo"), "codigoEstadoReciboCaja" )
				.add( Projections.property("esrec.descripcion"), "estadoRecibo" )
				.add( Projections.property("detcon.nombreBeneficiario"), "recibidoDe" )
				.add( Projections.property("detcon.numeroIdBeneficiario"), "numIdentificacion" )
				.add( Projections.property("forpag.descripcion"), "formaPago" )
				.add( Projections.property("forpag.consecutivo"), "tipoFormaPago" )
				.add( Projections.property("detpag.valor"), "valor" )
				.add( Projections.property("detpag.consecutivo"), "idDetalleReciboPago" ))

				.add(Restrictions.eq("rcxt.turnoDeCaja", movimientosCaja.getTurnoDeCaja()));



		if(movimientosCaja.getFechaMovimiento()!=null){

			List<RecibosCaja> recibosFiltradosFecha = sessionFactory.getCurrentSession()
			.createCriteria(RecibosCaja.class, "rc")
			.createAlias("recibosCajaXTurnos","rcxt")
			.add(Restrictions.eq("rcxt.turnoDeCaja", movimientosCaja.getTurnoDeCaja()))
			.add(Restrictions.eq("fecha", movimientosCaja.getFechaMovimiento())).list();

			ArrayList<String> codigosRecibosCaja =  new ArrayList<String>();
			codigosRecibosCaja.add(ConstantesBD.codigoNuncaValido+"");
			for (RecibosCaja recibosCaja : recibosFiltradosFecha) {
				codigosRecibosCaja.add(recibosCaja.getId().getNumeroReciboCaja());
			}

			criteria.add(Restrictions.in("id.numeroReciboCaja", codigosRecibosCaja));
		}


		List<DtoReciboDevolucion> lista =  criteria.addOrder(Order.asc("id.numeroReciboCaja"))
		.setResultTransformer(Transformers.aliasToBean(DtoReciboDevolucion.class))
		.list();

		return lista;

	}

	
	
	/**
	 * Retorna un listado con los detalles de los recibos de caja con formas de pago diferente a tipo "NINGUNO"
	 * asociados a un Turno de Caja y que no se han relacionado a una Anulaci&oacute;n. 
	 * 
	 * El par&aacute;metro directoBanco aplica solo para los recibos con detalles de pagos que involucren Tarjetas
	 * Financieras, con el se obtienen, dependiendo del valor (true), las tarjetas que tienen pago directo con una entidad
	 * financiera
	 * 
	 * @param institucion
	 * @param turnoDeCaja
	 * @param directoBanco
	 * @return Listado con los detalles de los recibos de caja de un Turno de Caja que no se han relacionado a una
	 * una Anulaci&oacute;n
	 */
	public List<DtoDetalleDocSopor> obtenerRecibosNoAnuladosNoFormaPagoNinguna (int institucion, TurnoDeCaja turnoDeCaja, boolean directoBanco){
		
		UtilidadTransaccion.getTransaccion().begin();

		List<DtoDetalleDocSopor> listadoRecibos = obtenerRecibosCajaBonosSinAnular(institucion, turnoDeCaja);
		listadoRecibos.addAll(obtenerRecibosCajaChequeSinAnular(institucion, turnoDeCaja));
		listadoRecibos.addAll(obtenerRecibosCajaTarjetasSinAnular(institucion, turnoDeCaja, directoBanco));
		
		listadoRecibos.addAll(obtenerRecibosCajaLetraPagareSinAnular(institucion, turnoDeCaja));
		//listadoRecibos.addAll(obtenerRecibosCajaEfecSinAnular(institucion, turnoDeCaja));
		
		return listadoRecibos;
		
	}
	
	
	/**
	 * M&eacute;todo que se encarga de obtener los recibos de caja de tipo forma de pago cheque sin anular.
	 * 
	 * @param institucion
	 * @param turnoDeCaja
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private List<DtoDetalleDocSopor>  obtenerRecibosCajaChequeSinAnular(int institucion, TurnoDeCaja turnoDeCaja)
	{
		List<DtoDetalleDocSopor> lista = sessionFactory.getCurrentSession().
			createCriteria(RecibosCaja.class).
		
			add(Restrictions.eq("instituciones.codigo", institucion))
			
			.createAlias("detalleConceptosRcs","detcon")
			.createAlias("detallePagosRcs", "detpag")
			.createAlias("detpag.movimientosChequeses","movche")
			.createAlias("recibosCajaXTurnos","rcxt")
			.createAlias("movche.entidadesFinancieras","ef")
			.createAlias("ef.terceros","ter")
			.createAlias("detpag.formasPago", "fp")
			
			.setProjection( Projections.projectionList()
			  		   .add( Projections.property("detcon.nombreBeneficiario"), "nombreRecibidoDe" )
	                   .add( Projections.property("movche.numeroCheque"), "nroDocumentoEntregado" )
			  		   .add( Projections.property("ter.descripcion"), "descripcionEntFinanciera" )//review*******
			  		   .add( Projections.property("ef.consecutivo"), "consecutivoEntFinanciera" )//review*******
	                   .add( Projections.property("movche.valor"), "valorSistema" )
	                   .add( Projections.property("fp.descripcion"), "formaPago" )
	                   .add( Projections.property("fp.consecutivo"), "consecutivoFormaPago" )
	                   .add( Projections.property("id.numeroReciboCaja"), "reciboCajaId" )
	                   .add( Projections.property("detpag.consecutivo"), "detallePagosId" )
	          )
	          .add(Restrictions.eq("rcxt.turnoDeCaja", turnoDeCaja))
	          .add(Restrictions.ne("estadosRecibosCaja.codigo", ConstantesBD.codigoEstadoReciboCajaAnulado))
	          .addOrder(Order.asc("id.numeroReciboCaja"))
	         .setResultTransformer( Transformers.aliasToBean(DtoDetalleDocSopor.class))
	         .list();
		return lista;
	}
	
	
	/**
	 * M&eacute;todo que se encarga de obtener los recibos de caja de tipo forma de pago letra y pagaré sin anular.
	 * 
	 * @param institucion
	 * @param turnoDeCaja
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private List<DtoDetalleDocSopor>  obtenerRecibosCajaLetraPagareSinAnular(int institucion, TurnoDeCaja turnoDeCaja)
	{
		List<DtoDetalleDocSopor> lista = sessionFactory.getCurrentSession().
			createCriteria(RecibosCaja.class).
		
			add(Restrictions.eq("instituciones.codigo", institucion))
			
			.createAlias("detalleConceptosRcs","detcon")
			.createAlias("detallePagosRcs", "detpag")
			.createAlias("recibosCajaXTurnos","rcxt")
			.createAlias("detpag.formasPago", "fp")
			.createAlias("detpag.datosFinanciacions", "datFinanc")
			
			.setProjection( Projections.projectionList()
			  		   .add( Projections.property("detcon.nombreBeneficiario"), "nombreRecibidoDe" )
			  		   .add( Projections.property("datFinanc.consecutivo"), "nroDocumentoEntregado" )
	                   .add( Projections.property("detpag.valor"), "valorSistema" )
	                   .add( Projections.property("fp.descripcion"), "formaPago" )
	                   .add( Projections.property("fp.consecutivo"), "consecutivoFormaPago" )
	                   .add( Projections.property("id.numeroReciboCaja"), "reciboCajaId" )
	                   .add( Projections.property("detpag.consecutivo"), "detallePagosId" )
	          )
	          .add(Restrictions.eq("rcxt.turnoDeCaja", turnoDeCaja))
	          .add(Restrictions.ne("estadosRecibosCaja.codigo", ConstantesBD.codigoEstadoReciboCajaAnulado))
	          .addOrder(Order.asc("id.numeroReciboCaja"))
	         .setResultTransformer( Transformers.aliasToBean(DtoDetalleDocSopor.class))
	         .list();
		return lista;
	}
	
	@SuppressWarnings("unchecked")
	public List<DtoDetalleDocSopor> obtenerRecibosCajaTarjetasSinAnular(int institucion, TurnoDeCaja turnoDeCaja, boolean directoBanco)
	{
		Criteria criteria = sessionFactory.getCurrentSession().
		createCriteria(RecibosCaja.class)
		.createAlias("detalleConceptosRcs","detcon")
		.createAlias("detallePagosRcs", "detpag")
		.createAlias("detpag.movimientosTarjetases","movtar")
		.createAlias("movtar.entidadesFinancieras","ef")
		.createAlias("ef.terceros","ter")
		.createAlias("recibosCajaXTurnos","rcxt")
		.createAlias("detpag.formasPago", "fp");
	
		if(directoBanco){
			
			criteria.createAlias("movtar.tarjetasFinancieras","tarFin")
			.add(Restrictions.eq("tarFin.directoBanco", !directoBanco));
		}
		
	    return (List<DtoDetalleDocSopor>) criteria.add(Restrictions.eq("instituciones.codigo", institucion))

		.setProjection( Projections.projectionList()
		  		   .add( Projections.property("detcon.nombreBeneficiario"), "nombreRecibidoDe" )
                   .add( Projections.property("movtar.numeroComprobante"), "nroDocumentoEntregado" )
		  		   .add( Projections.property("ter.descripcion"), "descripcionEntFinanciera" )
		  		   .add( Projections.property("ef.consecutivo"), "consecutivoEntFinanciera" )
                   .add( Projections.property("movtar.valor"), "valorSistema" )
                   .add( Projections.property("fp.descripcion"), "formaPago" )
                   .add( Projections.property("fp.consecutivo"), "consecutivoFormaPago" )
                   .add( Projections.property("id.numeroReciboCaja"), "reciboCajaId" )
                   .add( Projections.property("detpag.consecutivo"), "detallePagosId" )
          )
          .add(Restrictions.eq("rcxt.turnoDeCaja", turnoDeCaja))
          .add(Restrictions.ne("estadosRecibosCaja.codigo", ConstantesBD.codigoEstadoReciboCajaAnulado))
          .addOrder(Order.asc("id.numeroReciboCaja"))
         .setResultTransformer( Transformers.aliasToBean(DtoDetalleDocSopor.class))
         .list();

	}
	
	@SuppressWarnings("unchecked")
	public List<DtoDetalleDocSopor>  obtenerRecibosCajaBonosSinAnular(int institucion, TurnoDeCaja turnoDeCaja)
	{
		List<DtoDetalleDocSopor> lista = sessionFactory.getCurrentSession().
			createCriteria(RecibosCaja.class).
			add(Restrictions.eq("instituciones.codigo", institucion))
			
			.createAlias("detalleConceptosRcs","detcon")
			.createAlias("detallePagosRcs", "detpag")
			.createAlias("detpag.movimientosBonoses","movbon")
			.createAlias("recibosCajaXTurnos","rcxt")	
			.createAlias("detpag.formasPago", "fp")

			.setProjection( Projections.projectionList()
			  		   .add( Projections.property("detcon.nombreBeneficiario"), "nombreRecibidoDe" )
	                   .add( Projections.property("movbon.numSerial"), "nroDocumentoEntregado" )
	                   .add( Projections.property("detpag.valor"), "valorSistema" )
	                   .add( Projections.property("fp.descripcion"), "formaPago" )
	                   .add( Projections.property("fp.consecutivo"), "consecutivoFormaPago" )
	                   .add( Projections.property("id.numeroReciboCaja"), "reciboCajaId" )
	                   .add( Projections.property("detpag.consecutivo"), "detallePagosId" )
	                   
	          )
	         .add(Restrictions.eq("rcxt.turnoDeCaja", turnoDeCaja))
	         .add(Restrictions.ne("estadosRecibosCaja.codigo", ConstantesBD.codigoEstadoReciboCajaAnulado))
	         .addOrder(Order.asc("id.numeroReciboCaja"))
	         .setResultTransformer( Transformers.aliasToBean(DtoDetalleDocSopor.class))
	         .list();
		return lista;
	}

	@SuppressWarnings("unchecked")
	public List<DtoDetalleDocSopor>  obtenerRecibosCajaFormaNingunoSinAnular9999999999999999999(int institucion, TurnoDeCaja turnoDeCaja)
	{

		List<DtoDetalleDocSopor> lista = sessionFactory.getCurrentSession().
			createCriteria(RecibosCaja.class)
			.add(Restrictions.eq("instituciones.codigo", institucion))
			
			.createAlias("detallePagosRcs", "detpag")
			.createAlias("recibosCajaXTurnos","rcxt")	
			.createAlias("detpag.formasPago", "fp")
					
			.setProjection( Projections.projectionList()
			  		   .add( Projections.property("detpag.valor"), "valorSistema" )
	                   .add( Projections.property("fp.descripcion"), "formaPago" )
	                   .add( Projections.property("fp.consecutivo"), "consecutivoFormaPago" )
	                   .add( Projections.property("id.numeroReciboCaja"), "reciboCajaId" )
	                   .add( Projections.property("detpag.consecutivo"), "detallePagosId" )
	          )
	         .add(Restrictions.eq("fp.tiposDetalleFormaPago.codigo", ConstantesBD.codigoTipoDetalleFormasPagoNinguno))
	         .add(Restrictions.eq("rcxt.turnoDeCaja", turnoDeCaja))
	         .add(Restrictions.ne("estadosRecibosCaja.codigo", ConstantesBD.codigoEstadoReciboCajaAnulado))
	         .addOrder(Order.asc("id.numeroReciboCaja"))
	         .setResultTransformer( Transformers.aliasToBean(DtoDetalleDocSopor.class))
			
	         .list();
		return lista;
	}
	


	@SuppressWarnings("unchecked")
	public List<RecibosCaja> actualizarRecibosAsociadosCierre(MovimientosCaja movimientosCaja)
	{
		List<RecibosCaja>  listaRecibosCajaTurno = sessionFactory.getCurrentSession().
			createCriteria(RecibosCaja.class)
			.createAlias("recibosCajaXTurnos","rcxt")	
	        .add(Restrictions.eq("rcxt.turnoDeCaja",  movimientosCaja.getTurnoDeCaja()))
	        .addOrder(Order.asc("id.numeroReciboCaja"))
	        .list();

		actualizarEstadoReciboNoAnulado(listaRecibosCajaTurno, ConstantesBD.codigoEstadoReciboCajaEnCierre);

		return listaRecibosCajaTurno;
	}


	/**
	 * M&eacute;todo que actualiza el estado de un listado de Recibos de caja con estado diferente a 'Anulado'
	 * al estado pasado como par&aacute;metro
	 * 
	 * @param listaRecibosCajaTurno
	 * @return
	 */
	private boolean actualizarEstadoReciboNoAnulado(List<RecibosCaja> listaRecibosCajaTurno, int codigoEstadoRecibo) {
		
		boolean update =true;
		
		if (listaRecibosCajaTurno!=null && listaRecibosCajaTurno.size()>0){
			
			EstadosRecibosCaja estadoReciboCaja = new EstadosRecibosCaja();
			
			estadoReciboCaja.setCodigo(codigoEstadoRecibo);
			
			for (RecibosCaja recibosCaja : listaRecibosCajaTurno) {						
				
				if(recibosCaja.getEstadosRecibosCaja().getCodigo() != ConstantesBD.codigoEstadoReciboCajaAnulado){
					
					recibosCaja.setEstadosRecibosCaja(estadoReciboCaja);
				}
			}
			
		}else{
			
			update = false;
		}
		
		return update;
	}
	
	/**
	 * M&eacute;todo que se encarga de consultar la fecha m&aacute;xima registrada para un recibo
	 * de caja que est&aacute; asociado a un turno de caja espec&iacute;fico.
	 * 
	 * @param turnoDeCaja
	 * @return Date, con la fecha m&aacute;xima registrada para este movimiento
	 */
	public Date obtenerFechaUltimoMovimientoRecibo(TurnoDeCaja turnoDeCaja){
		
		return (Date) sessionFactory.getCurrentSession().createCriteria(RecibosCaja.class, "rc")
		  .createAlias("recibosCajaXTurnos","rcxt")
		  .setProjection(Projections.max("fecha"))
		  .add(Restrictions.eq("rcxt.turnoDeCaja", turnoDeCaja)).uniqueResult();
	}
	
	
	/**
	 * M&eacute;todo que se encarga de totalizar los recibos de caja recaudados que no fueron
	 * anulados con detalles de formas de pago de tipo "NINGUNO"
	 * 
	 * @param institucion
	 * @param turnoDeCaja
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<DtoDetalleDocSopor>  obtenerTotalesRecibosNoAnulFormaPagoNinguno(int institucion, TurnoDeCaja turnoDeCaja)
	{
		List<DtoDetalleDocSopor> lista = sessionFactory.getCurrentSession().createCriteria(RecibosCaja.class)
			.add(Restrictions.eq("instituciones.codigo", institucion))
			
			.createAlias("detallePagosRcs", "detpag")
			.createAlias("recibosCajaXTurnos","rcxt")	
			.createAlias("detpag.formasPago", "fp")
					
			.setProjection( Projections.projectionList()
					.add(Projections.sum("detpag.valor"), "valorSistema")
					.add( Projections.property("fp.descripcion"), "formaPago" )
	                .add( Projections.property("fp.consecutivo"), "consecutivoFormaPago" )
					.add(Projections.groupProperty("fp.consecutivo"))
					.add(Projections.groupProperty("fp.descripcion"))
			)
					
	        .add(Restrictions.eq("fp.tiposDetalleFormaPago.codigo", ConstantesBD.codigoTipoDetalleFormasPagoNinguno))
	        .add(Restrictions.eq("rcxt.turnoDeCaja", turnoDeCaja))
	        .add(Restrictions.ne("estadosRecibosCaja.codigo", ConstantesBD.codigoEstadoReciboCajaAnulado))
	        .setResultTransformer( Transformers.aliasToBean(DtoDetalleDocSopor.class)).list();
		
		return lista;
	}
	
	/**
	 * M&eacute;todo que se encarga de listar
	 * los estados de los recibos de caja
	 * @return ArrayList<EstadosRecibosCaja>
	 * @author Diana Carolina G
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<EstadosRecibosCaja> obtenerEstadosRC(){
		
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(EstadosRecibosCaja.class, "estadoRC");

		criteria.addOrder(Order.asc("estadoRC.descripcion"));
		return (ArrayList<EstadosRecibosCaja>)criteria.list();

	}
	
	/**
	 * M&eacute;todo encargado de consultar
	 * los centros de atención a los cuales se encuentran asociados
	 * recibos de caja por tipo de concepto 
	 * 'Anticipos Convenios Odontol&oacute;gicos'
	 * @param dto
	 * @return ArrayList<DtoAnticiposRecibidosConvenio>
	 * @author Diana Carolina G
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<DtoAnticiposRecibidosConvenio> obtenerCentrosAtencionRecibosCajaConceptoAnticipos(DtoReporteAnticiposRecibidosConvenio dto){
		
		
		Criteria criteria =sessionFactory.getCurrentSession()
		.createCriteria(RecibosCaja.class, "rc")
		.createAlias("rc.centroAtencion", "centroAtencion")
		.createAlias("centroAtencion.ciudades", "ciudades")
		.createAlias("ciudades.paises", "paises")
		.createAlias("centroAtencion.regionesCobertura", "regionesCobertura")
		
		.createAlias("rc.estadosRecibosCaja", "estadosRecibosCaja")
		.createAlias("rc.usuarios", "usuarios")
		.createAlias("rc.detallePagosRcs", "detallePagosRcs")
		.createAlias("detallePagosRcs.formasPago", "formasPago")
		
		.createAlias("rc.detalleConceptosRcs", "detalleConceptosRcs")
		.createAlias("detalleConceptosRcs.conceptosIngTesoreria", "conceptosIngTesoreria")
		.createAlias("conceptosIngTesoreria.tipoIngTesoreria", "tipoIngTesoreria");
		
		/****EsMultiempresa ***/
		if(dto.isEsMultiempresa()){
			criteria.createAlias("centroAtencion.empresasInstitucion", "institucion", Criteria.LEFT_JOIN);
		}else{
			criteria.createAlias("centroAtencion.instituciones", "institucion");
		}
		/****EsMultiempresa ***/
		
		
		criteria.add(Restrictions.between("rc.fecha", dto.getFechaInicial(), dto.getFechaFinal()));
		criteria.add(Restrictions.eq("paises.codigoPais",dto.getCodigoPaisResidencia()));
		criteria.add(Restrictions.eq("tipoIngTesoreria.codigo", ConstantesBD.codigoTipoIngresoTesoreriaAnticipoConvenioOdon));
		
		criteria.setProjection(Projections.distinct(Projections.projectionList()
				.add(Projections.property("centroAtencion.consecutivo"), "consCentroAtencion")
				.add(Projections.property("centroAtencion.descripcion"), "descCentroAtencion")
				.add(Projections.property("paises.descripcion"),"descripcionPais")
				.add(Projections.property("ciudades.descripcion"),"descripcionCiudad")
				.add(Projections.property("regionesCobertura.descripcion"),"descripcionRegionCobertura")
				.add(Projections.property("institucion.codigo"), "codigoIns")
				.add(Projections.property("institucion.razonSocial"),"nombreInstitucion")));
				
				
		if (!UtilidadTexto.isEmpty(dto.getCiudadDeptoPais()) && !dto.getCiudadDeptoPais().trim().equals(ConstantesBD.codigoNuncaValido + "")) {
			
			String vec[]= dto.getCiudadDeptoPais().split(ConstantesBD.separadorSplit);			
			dto.setCodigoCiudad(vec[0]);
			dto.setCodigoDpto(vec[1]);
			dto.setCodigoPais(vec[2]);
			
			criteria.add(Restrictions.eq("ciudades.id.codigoCiudad", dto.getCodigoCiudad()))
			.add(Restrictions.eq("ciudades.id.codigoPais", dto.getCodigoPais()))
			.add(Restrictions.eq("ciudades.id.codigoDepartamento", dto.getCodigoDpto()));
		}  
		
		
		if ((dto.getCodigoRegion()!=ConstantesBD.codigoNuncaValido) && (dto.getCodigoRegion() > 0)) {
			criteria.add(Restrictions.eq("regionesCobertura.codigo", dto.getCodigoRegion()));
		}
		if((dto.getCodigoEmpresaInstitucion()!=ConstantesBD.codigoNuncaValido) && (dto.getCodigoEmpresaInstitucion() > 0)){
			criteria.add(Restrictions.eq("institucion.codigo",dto.getCodigoEmpresaInstitucion()));
		}
		if ((dto.getConsecutivoCentroAtencion()!=ConstantesBD.codigoNuncaValido) && (dto.getConsecutivoCentroAtencion() > 0)) {
			criteria.add(Restrictions.eq("centroAtencion.consecutivo", dto.getConsecutivoCentroAtencion())); 
		}
		
		if((dto.getCodigoConvenio()!=ConstantesBD.codigoNuncaValido) && (dto.getCodigoConvenio()>0)) {
			criteria.add(Restrictions.eq("detalleConceptosRcs.numeroIdBeneficiario", dto.getCodigoConvenio() + ""));
		}
		if(!UtilidadTexto.isEmpty(dto.getCodigoConceptoAnticipo()) && !dto.getCodigoConceptoAnticipo().equals(ConstantesBD.codigoNuncaValido + "")){
			
			String idConcepto [] = dto.getCodigoConceptoAnticipo().split(ConstantesBD.separadorSplit);
			
			String codigo = idConcepto [0];
			int institucion = Integer.parseInt(idConcepto [1]);
			
			
			criteria.add(Restrictions.eq("conceptosIngTesoreria.id.codigo", codigo));
			criteria.add(Restrictions.eq("conceptosIngTesoreria.id.institucion", institucion));
		}
		if((dto.getCodigoEstadoRC()!=ConstantesBD.codigoNuncaValido) && (dto.getCodigoEstadoRC()>0)){
			criteria.add(Restrictions.eq("estadosRecibosCaja.codigo", dto.getCodigoEstadoRC()));
		}
		//aqui
		criteria.addOrder( Order.asc("institucion.razonSocial"));
		criteria.addOrder( Order.asc("centroAtencion.descripcion"));
		criteria.setResultTransformer(Transformers.aliasToBean(DtoAnticiposRecibidosConvenio.class));
		
		
		ArrayList<DtoAnticiposRecibidosConvenio> lista= (ArrayList<DtoAnticiposRecibidosConvenio>)criteria.list() ;
		
		return lista;
	}
	
	/**
	 * M&eacute;todo encargado de consultar
	 * el detalle de los recibos de caja por concepto 
	 * 'Anticipos Convenios Odontol&oacute;gicos', asociados
	 * un cada Centro de Atenci&oacute;n
	 * @param consCentroAtencion
	 * @param dto
	 * @return ArrayList<DtoRecibosConceptoAnticiposRecibidosConvenio>
	 * @author Diana Carolina G
	 */
	
	@SuppressWarnings("unchecked")
	public ArrayList<DtoRecibosConceptoAnticiposRecibidosConvenio> obtenerRecibosCajaConceptoAnticiposConvenioOdont(int consCentroAtencion, DtoReporteAnticiposRecibidosConvenio dto){
		
		Criteria criteria =sessionFactory.getCurrentSession()
		.createCriteria(RecibosCaja.class, "rc")
		.createAlias("rc.centroAtencion", "centroAtencion")
		.createAlias("centroAtencion.ciudades", "ciudades")
		.createAlias("ciudades.paises", "paises")
		//.createAlias("centroAtencion.regionesCobertura", "regionesCobertura")
		.createAlias("centroAtencion.instituciones", "instRC") 					//-------Institución a la cual pertenece el centro de atencion asociado al RC
		
		
		.createAlias("rc.estadosRecibosCaja", "estadosRecibosCaja")
		.createAlias("rc.usuarios", "usuarios")
		.createAlias("rc.detallePagosRcs", "detallePagosRcs")
		.createAlias("detallePagosRcs.formasPago", "formasPago")
		.createAlias("rc.detalleConceptosRcs", "detalleConceptosRcs")
		.createAlias("detalleConceptosRcs.conceptosIngTesoreria", "conceptosIngTesoreria")
		.createAlias("conceptosIngTesoreria.tipoIngTesoreria", "tipoIngTesoreria");
		
		
		criteria.add(Restrictions.eq("centroAtencion.consecutivo", consCentroAtencion));
		criteria.add(Restrictions.between("rc.fecha", dto.getFechaInicial(), dto.getFechaFinal()));
		criteria.add(Restrictions.eq("paises.codigoPais",dto.getCodigoPaisResidencia()));
		criteria.add(Restrictions.eq("tipoIngTesoreria.codigo", ConstantesBD.codigoTipoIngresoTesoreriaAnticipoConvenioOdon));
		
		criteria.setProjection(Projections.distinct(Projections.projectionList()
				.add(Projections.property("id.numeroReciboCaja"),"numeroRC")					//Numero RC , hace parte de la llave primaria de RecibosCaja
				.add(Projections.property("instRC.codigo"),"codInstRC")							//Codigo Institucion, hace parte de la llave primaria de RecibosCaja 
				.add(Projections.property("centroAtencion.consecutivo"), "consCentroAtencion")
				.add(Projections.property("centroAtencion.descripcion"), "descCentroAtencion")
				//.add(Projections.property("paises.descripcion"),"descripcionPais")
				//.add(Projections.property("ciudades.descripcion"),"descripcionCiudad")
				//.add(Projections.property("regionesCobertura.descripcion"),"descripcionRegionCobertura") 
				
		
				.add(Projections.property("detalleConceptosRcs.numeroIdBeneficiario"),"numeroIdConvenio")
				.add(Projections.property("detalleConceptosRcs.nombreBeneficiario"),"nombreConvenio")
				.add(Projections.property("rc.fecha"),"fechaRC")
				.add(Projections.property("conceptosIngTesoreria.descripcion"),"descripcionConcepto")
				.add(Projections.property("estadosRecibosCaja.descripcion"),"estadoRC")
				.add(Projections.property("estadosRecibosCaja.codigo"), "codEstadoRC")
				.add(Projections.property("usuarios.login"),"loginUsuario")
				.add(Projections.property("detalleConceptosRcs.valor"),"valorTotal"))); 
				
				
		/*if ((dto.getConsecutivoCentroAtencion()!=ConstantesBD.codigoNuncaValido) && (dto.getConsecutivoCentroAtencion() > 0)) {
			criteria.add(Restrictions.eq("centroAtencion.consecutivo", dto.getConsecutivoCentroAtencion()));
		} */
		
		if((dto.getCodigoConvenio()!=ConstantesBD.codigoNuncaValido) && (dto.getCodigoConvenio()>0)) {
			criteria.add(Restrictions.eq("detalleConceptosRcs.numeroIdBeneficiario", dto.getCodigoConvenio() + ""));
		}
		if(!UtilidadTexto.isEmpty(dto.getCodigoConceptoAnticipo()) && !dto.getCodigoConceptoAnticipo().equals(ConstantesBD.codigoNuncaValido + "")){
			
			String idConcepto [] = dto.getCodigoConceptoAnticipo().split(ConstantesBD.separadorSplit);
			
			String codigo = idConcepto [0];
			int institucion = Integer.parseInt(idConcepto [1]);
			
			
			criteria.add(Restrictions.eq("conceptosIngTesoreria.id.codigo", codigo));
			criteria.add(Restrictions.eq("conceptosIngTesoreria.id.institucion", institucion));
		}
		if((dto.getCodigoEstadoRC()!=ConstantesBD.codigoNuncaValido) && (dto.getCodigoEstadoRC()>0)){
			criteria.add(Restrictions.eq("estadosRecibosCaja.codigo", dto.getCodigoEstadoRC()));
		}
		
		criteria.addOrder( Order.asc("centroAtencion.descripcion"));
		criteria.addOrder( Order.asc("detalleConceptosRcs.nombreBeneficiario"));
		criteria.addOrder( Order.asc("rc.fecha") ); 
		criteria.setResultTransformer(Transformers.aliasToBean(DtoRecibosConceptoAnticiposRecibidosConvenio.class));
		
		
		ArrayList<DtoRecibosConceptoAnticiposRecibidosConvenio> lista= (ArrayList<DtoRecibosConceptoAnticiposRecibidosConvenio>)criteria.list() ;
		
		return lista;
		
		

	} 
	
	/**
	 * M&eacute;todo encargado de consultar el 
	 * detalle de las formas de pago con sus correspondientes
	 * valores por cada recibo de caja. 
	 * 
	 * @param numeroRC
	 * @return ArrayList<DtoFormaPagoReport>
	 * @author Diana Carolina G
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<DtoFormaPagoReport> cargarFormasPago(List<RecibosCajaId> numeroRC) {
		
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(RecibosCaja.class, "rc")
		.createAlias("rc.detallePagosRcs", "detallePagosRcs")
		.createAlias("detallePagosRcs.formasPago", "formasPago");
		
		criteria.add(Restrictions.in("id", numeroRC));
		
		criteria.setProjection(Projections.projectionList()
				.add(Projections.property("formasPago.consecutivo"),"consecutivo")
				.add(Projections.property("formasPago.descripcion"),"descripcion")
				.add(Projections.property("detallePagosRcs.valor"),"valor"));
				
		criteria.setResultTransformer(Transformers.aliasToBean(DtoFormaPagoReport.class));
		
		ArrayList<DtoFormaPagoReport> lista = (ArrayList<DtoFormaPagoReport>)criteria.list();
		return lista;
		
		}



//	public static void main(String[] args) {
//		
//		UtilidadTransaccion.getTransaccion().begin();
//		
//		TurnoDeCaja turnoDeCaja= new TurnoDeCajaDelegate().findById(234);
//		
//		RecibosCajaDelegate r =  new RecibosCajaDelegate();
//		MovimientosCaja movimientosCaja = new MovimientosCajaDelegate().findById(983);
//		
//		List<DtoReciboDevolucion> l = r.obtenerRecibosXMovimientoCaja(movimientosCaja);
//		
//		for (DtoReciboDevolucion valor : l) {
//			
//			Log4JManager.info("---- " + valor.getFormaPago() + " - " + valor.getNumeroReciboCaja() );
//		}
//		
//		//UtilidadTransaccion.getTransaccion().
//	}
	
//	
//	/**
//	 * Recibos de caja filtrados solo por el turno de caja, cuando el movimiento de caja no ha sido registrado.
//	 * Se involucra la fecha y la hora en la b&uacute;squeda, cuando el movimiento de caja est&aacute; registrado
//	 * en el sistema.
//	 * 
//	 * @param movimientosCaja
//	 * @return List<{@link DtoReciboDevolucion}>
//	 */
//	@SuppressWarnings("unchecked")
//	public List<DtoReciboDevolucion> obtenerRecibosXMovimientoCaja(MovimientosCaja movimientosCaja) 
//	{
//		
//		Query q = sessionFactory.getCurrentSession().createQuery(
//				
//				"select rc.id.numeroReciboCaja as numeroReciboCaja, esrec.codigo as codigoEstadoReciboCaja, " +
//				"esrec.descripcion as estadoRecibo, detcon.nombreBeneficiario as recibidoDe, " +
//				"detcon.numeroIdBeneficiario as numIdentificacion, forpag.descripcion as formaPago, " +
//				"forpag.consecutivo as tipoFormaPago, detpag.valor as valor, detpag.consecutivo as idDetalleReciboPago " +
//				"from RecibosCaja as rc join rc.estadosRecibosCaja as esrec join rc.detalleConceptosRcs as detcon join rc.detallePagosRcs as detpag " +
//				"join rc.detallePagosRcs.formasPago as forpag join rc.recibosCajaXTurno as rcxt where rc.recibosCajaXTurnos = :turnoCaja Order By rc.id.numeroReciboCaja asc"); // 
//		
//		q.setParameter("turnoCaja", movimientosCaja.getTurnoDeCaja());
//		
//		List<DtoReciboDevolucion> lista = q.setResultTransformer(Transformers.aliasToBean(DtoReciboDevolucion.class)).list();
//		
//		
//		return lista;
//		
//	}
	
/*	public static void main(String[] args) {
	RecibosCajaDelegate x = new RecibosCajaDelegate();
	
	DtoReporteAnticiposRecibidosConvenio dto = new DtoReporteAnticiposRecibidosConvenio();
	dto.setFechaInicial(UtilidadFecha.conversionFormatoFechaStringDate("30/11/2011"));
	dto.setFechaFinal(UtilidadFecha.conversionFormatoFechaStringDate("30/11/2011"));
	dto.setCodigoPaisResidencia("57");
	
	//ArrayList<DtoAnticiposRecibidosConvenio> listaConceptos=x.obtenerRecibosCajaConceptoAnticiposConvenioOdont(dto);
//	ArrayList<DtoRecibosConceptoAnticiposRecibidosConvenio> listaRC =x.obtenerCentrosAtencionRecibosCajaConceptoAnticipos(29, dto);
	
	} */
	
	/*List<RecibosCajaId> numeroRC=new ArrayList<RecibosCajaId>();
	RecibosCajaId obj= new RecibosCajaId();
	obj.setInstitucion(2);
	obj.setNumeroReciboCaja("967");
	
	numeroRC.add(obj);
	
	ArrayList<DtoFormaPagoReport> listaFormas=x.cargarFormasPago(numeroRC);  */
	
}

