package com.servinte.axioma.orm.delegate.tesoreria;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;

import util.ConstantesBD;
import util.UtilidadFecha;
import util.UtilidadTexto;

import com.princetonsa.dto.tesoreria.DtoEntregaCaja;
import com.servinte.axioma.orm.EntregaCajaMayor;
import com.servinte.axioma.orm.EntregaCajaMayorHome;
import com.servinte.axioma.orm.MovimientosCaja;
import com.servinte.axioma.orm.TurnoDeCaja;

/**
 * 
 * Esta clase se encarga de manejar las transaccciones relacionadas
 * con el objeto {@link EntregaCajaMayor}
 * 
 * @author Jorge Armando Agudelo Quintero
 *
 */

public class EntregaCajaMayorDelegate extends EntregaCajaMayorHome{

	
	/**
	 * M&eacute;todo utilizado para listar toda la informaci&oacute;n relacionada
	 * con las Entregas a Caja Mayor / Principal asociadas a un Turno de caja
	 * o a un movimiento de caja espec&iacute;fico, si el movimiento pasado como par&aacute;metro 
	 * se encuentra registrado en el sistema
	 * 
	 * @param movimientosCaja
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<DtoEntregaCaja> obtenerEntregasCajaMayor(MovimientosCaja movimientosCaja)
	{

		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(EntregaCajaMayor.class)
		  .createAlias("movimientosCajaByCodigoPk", "mc")
		  .createAlias("mc.tiposMovimientoCaja", "tmc")
		  .createAlias("mc.docSopMovimCajases", "docSopMovCajaEntrega")
		  .createAlias("movimientosCajaByMovimientoCajaArqueo", "movEntregaArqueo")
		  .createAlias("movEntregaArqueo.tiposMovimientoCaja", "tipoMovArqueo")
		  .createAlias("docSopMovCajaEntrega.formasPago", "fp")
		  
		  .setProjection( Projections.projectionList()
		  		   .add( Projections.property("mc.codigoPk"), "idMovimientoCaja" )
	               .add( Projections.property("docSopMovCajaEntrega.valor"), "valorEntregado" )
	               .add( Projections.property("fp.consecutivo"), "tipoFormaPago" )
	               .add( Projections.property("fp.descripcion"), "formaPago" )
	      )
	     .add(Restrictions.eq("tmc.codigo", ConstantesBD.codigoTipoMovimientoEntregaCajaPrincipalMayor))
	     .add(Restrictions.eq("tipoMovArqueo.codigo", ConstantesBD.codigoTipoMovimientoArqueoEntregaParcial))
	     .add(Restrictions.eq("mc.turnoDeCaja", movimientosCaja.getTurnoDeCaja()));
	     
		if(movimientosCaja.getCodigoPk()>0){

			if(movimientosCaja.getFecha()!=null){
				
				List<DtoEntregaCaja> entregasFiltradasFecha = sessionFactory.getCurrentSession().createCriteria(EntregaCajaMayor.class)
				  .createAlias("movimientosCajaByCodigoPk", "mc")
				  .createAlias("mc.tiposMovimientoCaja", "tmc")
				  .createAlias("movimientosCajaByMovimientoCajaArqueo", "movEntregaArqueo")
				  .createAlias("movEntregaArqueo.tiposMovimientoCaja", "tipoMovArqueo")
				  
				  .setProjection( Projections.projectionList()
						  .add( Projections.property("mc.codigoPk"), "idMovimientoCaja")
			              .add( Projections.property("mc.fecha"), "fecha")
			              .add( Projections.property("mc.hora"), "hora"))
			              
				  .add(Restrictions.eq("tmc.codigo", ConstantesBD.codigoTipoMovimientoEntregaCajaPrincipalMayor))
				  .add(Restrictions.eq("tipoMovArqueo.codigo", ConstantesBD.codigoTipoMovimientoArqueoEntregaParcial))
				  .add(Restrictions.eq("mc.turnoDeCaja", movimientosCaja.getTurnoDeCaja()))
				  .add(Restrictions.le("mc.fecha", movimientosCaja.getFecha()))
				  
				  .setResultTransformer( Transformers.aliasToBean(DtoEntregaCaja.class)).list();
				  
				ArrayList<Long> codigosEntrega =  new ArrayList<Long>();
				codigosEntrega.add(ConstantesBD.codigoNuncaValidoLong);
				
				for (DtoEntregaCaja entregaCaja: entregasFiltradasFecha) {
					
					if(UtilidadFecha.esHoraMenorIgualQueOtraReferencia(entregaCaja.getHora() , movimientosCaja.getHora())){
						
						codigosEntrega.add(entregaCaja.getIdMovimientoCaja());
					
					}else if(UtilidadFecha.esFechaMenorQueOtraReferencia(UtilidadFecha.conversionFormatoFechaAAp(entregaCaja.getFecha()), UtilidadFecha.conversionFormatoFechaAAp(movimientosCaja.getFecha()))){
						
						codigosEntrega.add(entregaCaja.getIdMovimientoCaja());
					}
				}
			
				criteria.add(Restrictions.in("mc.codigoPk", codigosEntrega));
			}
		}
		
		List<DtoEntregaCaja> lista = criteria.setResultTransformer( Transformers.aliasToBean(DtoEntregaCaja.class))
	     .list();
		
		return lista;
	}
	
	/**
	 * M&eacute;todo utilizado para listar toda la informaci&oacute;n relacionada
	 * con las Entregas a Caja Mayor / Principal asociadas a un Turno de caja
	 * o a un movimiento de caja espec&iacute;fico, si el movimiento pasado como par&aacute;metro 
	 * se encuentra registrado en el sistema
	 * 
	 * @param movimientosCaja
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<DtoEntregaCaja> obtenerEntregasCajaMayorPorFecha(MovimientosCaja movimientosCaja)
	{

		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(EntregaCajaMayor.class)
		.createAlias("movimientosCajaByCodigoPk", "mc")
		.createAlias("mc.tiposMovimientoCaja", "tmc")
		.createAlias("mc.docSopMovimCajases", "docSopMovCajaEntrega")
		.createAlias("movimientosCajaByMovimientoCajaArqueo", "movEntregaArqueo")
		.createAlias("movEntregaArqueo.tiposMovimientoCaja", "tipoMovArqueo")
		.createAlias("docSopMovCajaEntrega.formasPago", "fp")

		.setProjection( Projections.projectionList()
				.add( Projections.property("mc.codigoPk"), "idMovimientoCaja" )
				.add( Projections.property("docSopMovCajaEntrega.valor"), "valorEntregado" )
				.add( Projections.property("fp.consecutivo"), "tipoFormaPago" )
				.add( Projections.property("fp.descripcion"), "formaPago" )
		)
		.add(Restrictions.eq("tmc.codigo", ConstantesBD.codigoTipoMovimientoEntregaCajaPrincipalMayor))
		.add(Restrictions.eq("tipoMovArqueo.codigo", ConstantesBD.codigoTipoMovimientoArqueoEntregaParcial))
		.add(Restrictions.eq("mc.turnoDeCaja", movimientosCaja.getTurnoDeCaja()));



		if(movimientosCaja.getFechaMovimiento()!=null){

			List<DtoEntregaCaja> entregasFiltradasFecha = sessionFactory.getCurrentSession().createCriteria(EntregaCajaMayor.class)
			.createAlias("movimientosCajaByCodigoPk", "mc")
			.createAlias("mc.tiposMovimientoCaja", "tmc")
			.createAlias("movimientosCajaByMovimientoCajaArqueo", "movEntregaArqueo")
			.createAlias("movEntregaArqueo.tiposMovimientoCaja", "tipoMovArqueo")

			.setProjection( Projections.projectionList()
					.add( Projections.property("mc.codigoPk"), "idMovimientoCaja")
					.add( Projections.property("mc.fecha"), "fecha")
					.add( Projections.property("mc.hora"), "hora"))

					.add(Restrictions.eq("tmc.codigo", ConstantesBD.codigoTipoMovimientoEntregaCajaPrincipalMayor))
					.add(Restrictions.eq("tipoMovArqueo.codigo", ConstantesBD.codigoTipoMovimientoArqueoEntregaParcial))
					.add(Restrictions.eq("mc.turnoDeCaja", movimientosCaja.getTurnoDeCaja()))
					.add(Restrictions.eq("mc.fecha", movimientosCaja.getFechaMovimiento()))

					.setResultTransformer( Transformers.aliasToBean(DtoEntregaCaja.class)).list();

			ArrayList<Long> codigosEntrega =  new ArrayList<Long>();
			codigosEntrega.add(ConstantesBD.codigoNuncaValidoLong);

			for (DtoEntregaCaja entregaCaja: entregasFiltradasFecha) {
				codigosEntrega.add(entregaCaja.getIdMovimientoCaja());
			}

			criteria.add(Restrictions.in("mc.codigoPk", codigosEntrega));
		}


		List<DtoEntregaCaja> lista = criteria.setResultTransformer( Transformers.aliasToBean(DtoEntregaCaja.class))
		.list();

		return lista;
	}
	
	
	/**
	 * M&eacute;todo que retorna una entrega a Caja Mayor / Principal asociada a un c&oacute;digo espec&iacute;fico.
	 * 
	 * @param codigoEntrega
	 * @return
	 */
	public EntregaCajaMayor obtenerEntregaCajaMayorPorCodigo (long codigoEntrega){
		
		EntregaCajaMayor entregaCajaMayor = super.findById(codigoEntrega);
		
		if(entregaCajaMayor!=null){
			
			entregaCajaMayor.getMovimientosCajaByMovimientoCajaArqueo().getObservaciones();
			entregaCajaMayor.getCajas().getCentroAtencion().getDescripcion();
			entregaCajaMayor.getMovimientosCajaByCodigoPk().getCodigoPk();
			entregaCajaMayor.getMovimientosCajaByMovimientoCajaArqueo().getTurnoDeCaja().getUsuarios().getPersonas().getPrimerNombre();
			entregaCajaMayor.getMovimientosCajaByMovimientoCajaArqueo().getTurnoDeCaja().getCajas().getDescripcion();
		}
		
		return entregaCajaMayor;
	}
	

	/**
	 * M&eacute;todo que se encarga de consultar la fecha m&aacute;xima registrada para una
	 * movimiento de caja de tipo Entrega a Caja Mayor / Principal. 
	 * Este movimiento est&aacute; asociado a un turno de caja espec&iacute;fico.
	 * 
	 * @param turnoDeCaja
	 * @return Date, con la fecha m&aacute;xima registrada para este movimiento
	 */
	public Date obtenerFechaUltimoMovimientoEntregaCaja(TurnoDeCaja turnoDeCaja){
		
		return (Date) sessionFactory.getCurrentSession().createCriteria(EntregaCajaMayor.class)

		.createAlias("movimientosCajaByCodigoPk", "movCajaEnt")
		.setProjection(Projections.max("movCajaEnt.fecha"))
		
		.add(Restrictions.eq("movCajaEnt.turnoDeCaja", turnoDeCaja)).uniqueResult();
	}
	

	
	/**
	 * Retorna un listado con las Entregas a Caja Mayor / Principal realizadas en un Turno de Caja
	 * espec&iacute;fico
	 * 
	 * @param turnoDeCaja
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<EntregaCajaMayor> obtenerEntregasCajaMayorPrincipalXTurnoCaja(TurnoDeCaja turnoDeCaja) {
		
		 List<EntregaCajaMayor> listaEntregasCajaMayorPrincipal = sessionFactory.getCurrentSession().createCriteria(EntregaCajaMayor.class)
		  .createAlias("movimientosCajaByCodigoPk", "mc")

	     .add(Restrictions.eq("mc.turnoDeCaja", turnoDeCaja))
	     .list();

		return listaEntregasCajaMayorPrincipal;
	}
	
	
	/**
	 * @param codigoEntrega
	 * @return  usuario que genero arqueo
	 */
	public String obtenerUsuarioArqueo (long codigoEntrega){

		EntregaCajaMayor entregaCajaMayor = super.findById(codigoEntrega);
		String nombreUsuario ="";
		if(entregaCajaMayor!=null){
			if(!UtilidadTexto.isEmpty(entregaCajaMayor.getMovimientosCajaByMovimientoCajaArqueo().getTurnoDeCaja().getUsuarios().getPersonas().getPrimerApellido())){
				nombreUsuario=	entregaCajaMayor.getMovimientosCajaByMovimientoCajaArqueo().getTurnoDeCaja().getUsuarios().getPersonas().getPrimerApellido();
			}

			if(!UtilidadTexto.isEmpty(entregaCajaMayor.getMovimientosCajaByMovimientoCajaArqueo().getTurnoDeCaja().getUsuarios().getPersonas().getSegundoApellido())){
				nombreUsuario+=" "+entregaCajaMayor.getMovimientosCajaByMovimientoCajaArqueo().getTurnoDeCaja().getUsuarios().getPersonas().getSegundoApellido();
			}

			if(!UtilidadTexto.isEmpty(entregaCajaMayor.getMovimientosCajaByMovimientoCajaArqueo().getTurnoDeCaja().getUsuarios().getPersonas().getPrimerNombre())){
				nombreUsuario+=" "+	entregaCajaMayor.getMovimientosCajaByMovimientoCajaArqueo().getTurnoDeCaja().getUsuarios().getPersonas().getPrimerNombre();
			}

			if(!UtilidadTexto.isEmpty(entregaCajaMayor.getMovimientosCajaByMovimientoCajaArqueo().getTurnoDeCaja().getUsuarios().getPersonas().getSegundoNombre())){
				nombreUsuario+=" "+	entregaCajaMayor.getMovimientosCajaByMovimientoCajaArqueo().getTurnoDeCaja().getUsuarios().getPersonas().getSegundoNombre();
			}

			if(!UtilidadTexto.isEmpty(entregaCajaMayor.getMovimientosCajaByMovimientoCajaArqueo().getTurnoDeCaja().getUsuarios().getLogin())){
				nombreUsuario+=" ("+entregaCajaMayor.getMovimientosCajaByMovimientoCajaArqueo().getTurnoDeCaja().getUsuarios().getLogin()+" )";
			}
		}

		return nombreUsuario;
	}
	
	
	
//	/**
//	 * obtiene documentos correspondientes al movimiento 
//	 * @author Alejandro Echandia.
//	 * @param movimientoCaja
//	 * @return
//	 */
//	@SuppressWarnings("unchecked")
//	public List<DtoDetalleDocSopor> obtenerDetalleEntregaCajaPorCodigo (MovimientosCaja movimientosCaja) 
//	{
//		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(EntregaCajaMayor.class, "dsmc")
//			.createAlias("movimientosCajaByCodigoPk", "mc")
//			.createAlias("mc.docSopMovimCajases", "docSopMovCajaEntrega")
//			.createAlias("docSopMovCajaEntrega.detallePagosRcs", "detpag", Criteria.LEFT_JOIN)
//
//			.createAlias("formasPago", "fp")
//			.createAlias("movtar.entidadesFinancieras","ef")
//			.createAlias("ef.terceros","ter")
//		  
//			.setProjection( Projections.projectionList()
//					
//					.add( Projections.property("detcon.nombreBeneficiario"), "nombreRecibidoDe" )
//					.add( Projections.property("movtar.numeroComprobante"), "nroDocumentoEntregado" )
//					.add( Projections.property("movtar.valor"), "valorSistema" )//este o el de doc sop movim??
//					.add( Projections.property("ter.descripcion"), "descripcionEntFinanciera" )//review*******
//					.add( Projections.property("ef.consecutivo"), "consecutivoEntFinanciera" )
//					.add( Projections.property("fp.descripcion"), "formaPago" )
//					.add( Projections.property("fp.consecutivo"), "consecutivoFormaPago" )
//					.add( Projections.property("rc.id.numeroReciboCaja"), "reciboCajaId" )
//					.add( Projections.property("detpag.consecutivo"), "detallePagosId" )
//                   
//			)
//			.add(Restrictions.eq("mc.turnoDeCaja", movimientosCaja.getTurnoDeCaja()));
//         
//		if(movimientosCaja.getCodigoPk()>0)
//		{
//			criteria.add(Restrictions.eq("mc.codigoPk", movimientosCaja.getCodigoPk()));
//		}
//         
//		criteria.setResultTransformer( Transformers.aliasToBean(DtoDetalleDocSopor.class)).list();
//
//        List<DtoDetalleDocSopor> lista =criteria.list();
//        return lista;
//	}
	
	
}
