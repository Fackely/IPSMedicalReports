/*
 * Mayo 12, 2010
 */
package com.servinte.axioma.orm.delegate.tesoreria;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.axioma.util.log.Log4JManager;
import org.hibernate.Criteria;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadFecha;
import util.UtilidadTexto;

import com.princetonsa.dto.tesoreria.DtoBusquedaCierreArqueo;
import com.princetonsa.dto.tesoreria.DtoConsolidadoCierreReporte;
import com.princetonsa.dto.tesoreria.DtoConsolidadoCierreXCentroAtencion;
import com.princetonsa.dto.tesoreria.DtoConsultaEntregaTransportadora;
import com.servinte.axioma.orm.Cajas;
import com.servinte.axioma.orm.DevolRecibosCaja;
import com.servinte.axioma.orm.EntregaCajaMayor;
import com.servinte.axioma.orm.FormasPago;
import com.servinte.axioma.orm.MovimientosCaja;
import com.servinte.axioma.orm.MovimientosCajaHome;
import com.servinte.axioma.orm.SolicitudTrasladoCaja;
import com.servinte.axioma.persistencia.UtilidadTransaccion;


/**
 * @author Cristhian Murillo
 *
 * Clase que contiene logica del negocio sobre el modelo 
 */
@SuppressWarnings({"unchecked","rawtypes"})
public class MovimientosCajaDelegate extends MovimientosCajaHome{
	
	
	/**
	 * Retorna el ultimo turno de tipo cierre
	 * Si el movimiento de caja enviado es nulo, solo se tendra en cuenta al caja para buscar su ultimo cierre
	 * De lo contrario el turno de cierre debe corresponder al movimiento enviado.
	 * 
	 * @param caja
	 * @param movCaja
	 * @return
	 */
	public MovimientosCaja obtenerUltimoTurnoCierre(Cajas caja, MovimientosCaja movimientoCaja)
	{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(MovimientosCaja.class, "mov_caja");
		
		criteria.createAlias("turnoDeCaja"					, "turno");
		criteria.createAlias("turno.cajas"					, "caja_turno");
		criteria.createAlias("mov_caja.tiposMovimientoCaja"	, "tipo_mov_caja");
		
		criteria.add(Restrictions.eq("tipo_mov_caja.codigo"	, ConstantesBD.codigoTipoMovimientoArqueoCierreTurno));
		criteria.add(Expression.eq("turno.estado"			, ConstantesIntegridadDominio.acronimoEstadoAperturaDeCajaCerrado));
		
		if(movimientoCaja == null)
		{
			criteria.add(Expression.eq("caja_turno.consecutivo"	, caja.getConsecutivo()));
			criteria.addOrder(Order.desc("mov_caja.fecha"));
			criteria.addOrder(Order.desc("mov_caja.hora"));
		}
		else if(movimientoCaja != null){ 
			movimientoCaja = super.findById(movimientoCaja.getCodigoPk());
			criteria.add(Restrictions.eq("mov_caja.turnoDeCaja"	, movimientoCaja.getTurnoDeCaja()));
		}
		
		MovimientosCaja movimiento = (MovimientosCaja)criteria.setMaxResults(1).uniqueResult();
		if(movimiento != null)
		{
			movimiento.getTurnoDeCaja().getCodigoPk();
			movimiento.getTiposMovimientoCaja().getCodigo();
			movimiento.getTiposMovimientoCaja().getDescripcion();
			movimiento.getTurnoDeCaja().getCajas().getDescripcion();
			movimiento.getTurnoDeCaja().getCentroAtencion().getDescripcion();
			movimiento.getUsuarios().getLoginUsuariosActivoses();
			movimiento.getUsuarios().getPersonas().getPrimerNombre();

			if(movimiento.getDetalleMovCajaCierre().getCajas() != null){	
				movimiento.getDetalleMovCajaCierre().getCajas().getConsecutivo();
				movimiento.getDetalleMovCajaCierre().getCajas().getDescripcion();
			}
			
			if(movimiento.getEntregaCajaMayorByCodigoPk() != null){	
				movimiento.getEntregaCajaMayorByCodigoPk(); 
			}
			
			if(movimiento.getSolicitudTrasladoCajaByCodigoPk() != null){	
				movimiento.getSolicitudTrasladoCajaByCodigoPk(); 
			}
		}
		
		return movimiento;
	}
	
	

	/**
	 * Guarda el movimiento enviado conteniendo los errores que puedan ocurrir
	 * 
	 * @param movimientosCaja
	 * @return boolean indicando si el guardado fue exitoso
	 */
	public boolean guardarMovimientoCaja (MovimientosCaja movimientosCaja){
		
		boolean save = false;
		try{
			super.persist(movimientosCaja);
			save = true;
		}
		catch (Exception e) {
			save = false;
			Log4JManager.error("No se pudo guardar el Movimientos de Caja: ",e);
		}
		
		return save;
	}
	
	
	@Override
	public MovimientosCaja findById(long id) {
		UtilidadTransaccion.getTransaccion().begin();
		MovimientosCaja movimientosCaja = super.findById(id);
		movimientosCaja.getTurnoDeCaja().getUsuarios().getLogin();
		movimientosCaja.getTurnoDeCaja().getUsuarios().getPersonas().getPrimerNombre();
		movimientosCaja.getTurnoDeCaja().getCajas();
		UtilidadTransaccion.getTransaccion().commit();
		return movimientosCaja;
	}

	
	/**
	 * Retorna una solicitud de traslado relacionada al movimiento de caja enviado
	 * @param idMovimiento
	 * @return solicitudTrasladoCaja
	 */
	public SolicitudTrasladoCaja obtenerSolicitudTraslado(long idMovimiento)
	{
		SolicitudTrasladoCaja solicitudTrasladoCaja = (SolicitudTrasladoCaja) sessionFactory.getCurrentSession()
			.createCriteria(SolicitudTrasladoCaja.class)
			.add(Expression.eq("movimientoCaja", idMovimiento))
			.uniqueResult();
		
		return solicitudTrasladoCaja;
		
	}
	
	/**
	 * Retorna un list de movimientos
	 * @param dtoConsultaEntregaTransportadora
	 * @return
	 */
	public List<MovimientosCaja>  ConsultaEntregaTransportadora (DtoConsultaEntregaTransportadora dtoConsultaEntregaTransportadora)
	{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(MovimientosCaja.class, "mvc")
		  .createAlias("mvc.turnoDeCaja", "tmc")
		  .createAlias("tmc.cajas", "cs")
		  .createAlias("tmc.centroAtencion", "cenAt")
		  .createAlias("tmc.usuarios","usu")
		  .createAlias("mvc.entregaTransportadoraByCodigoPk","et")
		  .createAlias("et.cuentasBancarias","cb", Criteria.LEFT_JOIN)
		  .createAlias("cb.entidadesFinancieras","ef", Criteria.LEFT_JOIN)
		  .createAlias("et.transportadoraValores","tp")
		  .createAlias("tp.terceros","tr");
		
		
				
		criteria.add(Restrictions.eq("cenAt.consecutivo", dtoConsultaEntregaTransportadora.getCodigoCentroAtencion()));		
		
		criteria.add(Restrictions.between("mvc.fecha",UtilidadFecha.conversionFormatoFechaStringDate(dtoConsultaEntregaTransportadora.getFechaGeneracionInicial()),UtilidadFecha.conversionFormatoFechaStringDate( dtoConsultaEntregaTransportadora.getFechaGeneracionFinal())));
	  		
		if(dtoConsultaEntregaTransportadora.getNumeroEntrega()>0){
			
			criteria.add(Restrictions.eq("mvc.consecutivo", dtoConsultaEntregaTransportadora.getNumeroEntrega()));
		}
		
		if(dtoConsultaEntregaTransportadora.getConsecutivoCaja()>0){
			
			criteria.add(Restrictions.eq("cs.consecutivo", dtoConsultaEntregaTransportadora.getConsecutivoCaja()));
		}
		
		if(!UtilidadTexto.isEmpty(dtoConsultaEntregaTransportadora.getLoginCajero())){
			
			criteria.add(Restrictions.eq("usu.login", dtoConsultaEntregaTransportadora.getLoginCajero()));
		}
		
		if(dtoConsultaEntregaTransportadora.getCodigoTransportadora()>0){
			
			criteria.add(Restrictions.eq("tp.codigoPk", dtoConsultaEntregaTransportadora.getCodigoTransportadora()));
		}
		
		if(dtoConsultaEntregaTransportadora.getCodigoEntidadFinanciera()>0){
			
			criteria.add(Restrictions.eq("ef.consecutivo", dtoConsultaEntregaTransportadora.getCodigoEntidadFinanciera()));
		}
		
		if(!UtilidadTexto.isEmpty(dtoConsultaEntregaTransportadora.getNumeroCuentaBancaria())){
			
			criteria.add(Restrictions.eq("cb.numCuentaBan", dtoConsultaEntregaTransportadora.getNumeroCuentaBancaria()));
		}
		
		List<MovimientosCaja> lista= criteria.list();
		
		return lista;
		
	}
	
	/**
	 * Método que se encarga de realizar la consulta de los movimientos de caja de tipo Arqueo, Arqueo Entrega Parcial
	 * y Cierre Turno de caja que cumplan con los parámetros de búsqueda.
	 * 
	 * @param dtoBusquedaCierreArqueo
	 * @return
	 */
	public List<MovimientosCaja> consultarCierreArqueo (DtoBusquedaCierreArqueo dtoBusquedaCierreArqueo){
		
		/*
		 * Solo se tienen en cuenta los movimientos de caja de tipo Arqueo.
		 * Arqueo Caja, Arqueo Entrega Parcial, Cierre Turno de Caja.
		 */
		ArrayList<Integer> codigosMovimientosArqueos = new ArrayList<Integer>();
		
		codigosMovimientosArqueos.add(ConstantesBD.codigoTipoMovimientoArqueoCaja);
		codigosMovimientosArqueos.add(ConstantesBD.codigoTipoMovimientoArqueoEntregaParcial);
		codigosMovimientosArqueos.add(ConstantesBD.codigoTipoMovimientoArqueoCierreTurno);
		
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(MovimientosCaja.class, "movCaja");
		
		criteria.createAlias("movCaja.turnoDeCaja" , "turnoCaja");
		criteria.createAlias("turnoCaja.cajas" , "cajaTurno");
		criteria.createAlias("turnoCaja.usuarios" , "usuario");
		criteria.createAlias("usuario.personas" , "persona");
		criteria.createAlias("tiposMovimientoCaja"	, "tipoMovCaja");
		
		Log4JManager.info(" cajero " + dtoBusquedaCierreArqueo.getCajero());
		Log4JManager.info(" consecutivo caja " + dtoBusquedaCierreArqueo.getConsecutivoCaja());
		Log4JManager.info(" tipo arqueo " + dtoBusquedaCierreArqueo.getCodigoTipoMovimiento());
		Log4JManager.info(" fecha inicial " + dtoBusquedaCierreArqueo.getFechaInicial());
		Log4JManager.info(" fecha final " + dtoBusquedaCierreArqueo.getFechaFinal());
		
		if(dtoBusquedaCierreArqueo.getFechaInicial()!=null && dtoBusquedaCierreArqueo.getFechaFinal()!=null){
			
			criteria.add(Restrictions.between("movCaja.fechaMovimiento", dtoBusquedaCierreArqueo.getFechaInicial(), 
					dtoBusquedaCierreArqueo.getFechaFinal()));
			criteria.addOrder(Order.desc("movCaja.fechaMovimiento"));
		}
		
		if(dtoBusquedaCierreArqueo.getCajero()!=null && !"".equals(dtoBusquedaCierreArqueo.getCajero())){
			
			criteria.add(Restrictions.eq("usuario.login" , dtoBusquedaCierreArqueo.getCajero()));
		}

		if(dtoBusquedaCierreArqueo.getCodigoTipoMovimiento() > ConstantesBD.codigoNuncaValido){
			
			criteria.add(Restrictions.eq("tipoMovCaja.codigo" , dtoBusquedaCierreArqueo.getCodigoTipoMovimiento()));
		}
		
		if(dtoBusquedaCierreArqueo.getConsecutivoCaja() > ConstantesBD.codigoNuncaValido){
			
			criteria.add(Restrictions.eq("cajaTurno.consecutivo" , dtoBusquedaCierreArqueo.getConsecutivoCaja()));
			criteria.addOrder(Order.desc("cajaTurno.consecutivo"));
		}
		
		criteria.add(Restrictions.in("tipoMovCaja.codigo", codigosMovimientosArqueos));
		criteria.addOrder(Order.asc("movCaja.consecutivo"));
		
		List<MovimientosCaja> registrosMovimientosCaja = criteria.list();
		
		for (MovimientosCaja movimientoCaja : registrosMovimientosCaja) {
			
			Criteria criteriaEntrega = sessionFactory.getCurrentSession().createCriteria(EntregaCajaMayor.class, "entrega");
			criteriaEntrega.createAlias("movimientosCajaByMovimientoCajaArqueo"	, "movArqueo");
			criteriaEntrega.add(Restrictions.eq("movArqueo.codigoPk", movimientoCaja.getCodigoPk()));
			
			EntregaCajaMayor entregaCajaMayorByCodigoPk = (EntregaCajaMayor)criteriaEntrega.uniqueResult();
			movimientoCaja.setEntregaCajaMayorByCodigoPk(entregaCajaMayorByCodigoPk);
			
			Criteria criteriaSolicitud = sessionFactory.getCurrentSession().createCriteria(SolicitudTrasladoCaja.class, "solicitudTraslado");
			criteriaSolicitud.createAlias("movimientosCajaByCodigoPk"	, "movimientoSolicitudTraslado");
			criteriaSolicitud.createAlias("movimientoSolicitudTraslado.turnoDeCaja"	, "turnoCaja");
			criteriaSolicitud.add(Restrictions.eq("turnoCaja.codigoPk", movimientoCaja.getTurnoDeCaja().getCodigoPk()));
			
			SolicitudTrasladoCaja solicitudTrasladoCajaByCodigoPk = (SolicitudTrasladoCaja)criteriaSolicitud.uniqueResult();
			movimientoCaja.setSolicitudTrasladoCajaByCodigoPk(solicitudTrasladoCajaByCodigoPk);
			
		}
		
		
		for (MovimientosCaja movimientoCaja : registrosMovimientosCaja) {
		
			inicializarMovimientoCaja(movimientoCaja);
		}
		
		return registrosMovimientosCaja;
	}

	/**
	 * Método que se encarga de realizar la consulta de los movimientos de caja de tipo 
	 * Entrega Caja Mayor o Entrega Transportadora de Valores asociado a un Arqueo Entrega Parcial
	 * 
	 * @param dtoBusquedaCierreArqueo
	 * @return
	 */
	public MovimientosCaja consultarEntregaAsociadaArqueoParcial (long codigoMovimientoArqueo){
		
		MovimientosCaja movimientoCaja;
		
		movimientoCaja = consultarEntregaCajaMayorAsociadaArqueoParcial(codigoMovimientoArqueo);
		
		if(movimientoCaja == null)
		{
			movimientoCaja = consultarEntregaTransportadoraAsociadaArqueoParcial(codigoMovimientoArqueo);
		}
		
		inicializarMovimientoCaja(movimientoCaja);
		
		return movimientoCaja;
	}
	
	/**
	 * Método que se encarga de consultar la Entrega a Caja Mayor / Principal que esta asociada a un
	 * Arqueo Entrega Parcial.
	 * 
	 * @param codigoMovimientoArqueo
	 * @return
	 */
	private MovimientosCaja consultarEntregaCajaMayorAsociadaArqueoParcial (long codigoMovimientoArqueo){
		
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(MovimientosCaja.class, "movCaja");
		
		criteria.createAlias("tiposMovimientoCaja"	, "tipoMovCaja");
		criteria.createAlias("entregaCajaMayorByCodigoPk"	, "entregaCajaMayor");
		criteria.createAlias("entregaCajaMayor.movimientosCajaByMovimientoCajaArqueo"	, "movArqueoEntregaCaja");
		
        criteria.add(Restrictions.eq("movArqueoEntregaCaja.codigoPk", codigoMovimientoArqueo));
		criteria.add(Restrictions.eq("tipoMovCaja.codigo", ConstantesBD.codigoTipoMovimientoEntregaCajaPrincipalMayor));
		
		return (MovimientosCaja) criteria.uniqueResult();
		
	}
	
	/**
	 * Método que se encarga de consultar la Entrega a Transportadora de Valores que esta asociada a un
	 * Arqueo Entrega Parcial.
	 * 
	 * @param codigoMovimientoArqueo
	 * @return
	 */
	private MovimientosCaja consultarEntregaTransportadoraAsociadaArqueoParcial (long codigoMovimientoArqueo){
		
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(MovimientosCaja.class, "movCaja");
		
		criteria.createAlias("tiposMovimientoCaja"	, "tipoMovCaja");
		criteria.createAlias("entregaTransportadoraByCodigoPk"	, "entregaTransportadora");
		criteria.createAlias("entregaTransportadora.movimientosCajaByMovimientoCajaArqueo"	, "movArqueoEntregaTransportadora");
	
        criteria.add(Restrictions.eq("movArqueoEntregaTransportadora.codigoPk", codigoMovimientoArqueo));
		criteria.add(Restrictions.eq("tipoMovCaja.codigo", ConstantesBD.codigoTipoMovimientoEntregaTransportadora));
		
		return (MovimientosCaja) criteria.uniqueResult();
	}
	
	
	/**
	 * Método que se encarga de inicializar toda la información del movimiento de caja
	 * consultado.
	 * 
	 * @param movimientoCaja
	 */
	private void inicializarMovimientoCaja(MovimientosCaja movimientoCaja) {
		
		if(movimientoCaja != null)
		{
			movimientoCaja.getTiposMovimientoCaja().getDescripcion();
			movimientoCaja.getTurnoDeCaja().getCajas().getDescripcion();
			movimientoCaja.getTurnoDeCaja().getCentroAtencion().getDescripcion();
			movimientoCaja.getTurnoDeCaja().getUsuarios().getPersonas().getPrimerNombre();
			movimientoCaja.getUsuarios().getPersonas().getPrimerNombre();
			
			if(movimientoCaja.getEntregaCajaMayorByCodigoPk()!=null){
				
				movimientoCaja.getEntregaCajaMayorByCodigoPk().getMovimientoCaja();
				movimientoCaja.getEntregaCajaMayorByCodigoPk().getEstado();
			}
			
			if(movimientoCaja.getEntregaTransportadoraByCodigoPk()!=null){
				
				movimientoCaja.getEntregaTransportadoraByCodigoPk().getMovimientoCaja();
				movimientoCaja.getEntregaTransportadoraByCodigoPk().getEstado();
			}
			
			if(movimientoCaja.getTurnoDeCaja()!=null){
				
				movimientoCaja.getTurnoDeCaja().getCajas().getCodigo();
				movimientoCaja.getTurnoDeCaja().getCajas().getDescripcion();
				movimientoCaja.getTurnoDeCaja().getCajas().getCentroAtencion().getDescripcion();
				movimientoCaja.getTurnoDeCaja().getCajas().getCentroAtencion().getConsecutivo();
			}
			
			if(movimientoCaja.getDetalleMovCajaCierre()!=null){
				
				movimientoCaja.getDetalleMovCajaCierre().getCajas().getCodigo();
				movimientoCaja.getDetalleMovCajaCierre().getCajas().getCentroAtencion().getDescripcion();
			}
		}
	}
	
	
	
	
	

	
	
	/**
	 * Metodo que consulta los cierres translados centro atencion
	 * @param fechaSeleccionada
	 * @param CentroAtencion
	 * @param empresaInstitucion
	 * @return cierres de translados centro atencion
	 */
	public ArrayList<DtoConsolidadoCierreXCentroAtencion> consultarMovimientosCierreCajaTranslados(Date fechaSeleccionada, String CentroAtencion,String empresaInstitucion){
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(MovimientosCaja.class,"movimientoCaja")
		.createAlias("movimientoCaja.cierreCajaAcepTrasCajas", "cierreCajaAcepTrasCajas")
		.createAlias("movimientoCaja.tiposMovimientoCaja", "tiposMovimientoCaja")
		.createAlias("movimientoCaja.turnoDeCaja", "turnoDeCaja")
		.createAlias("turnoDeCaja.centroAtencion", "centroAtencion")
		.createAlias("centroAtencion.instituciones", "instituciones")
		.createAlias("cierreCajaAcepTrasCajas.aceptacionTrasladoCaja", "aceptacionTrasladoCaja")
		.createAlias("aceptacionTrasladoCaja.movimientosCaja", "movimientosCajaAceptacion")
		.createAlias("movimientosCajaAceptacion.docSopMovimCajases", "docSopMovimCajases")
		.createAlias("docSopMovimCajases.formasPago", "formasPago")
		;
	
		
//		filtro por fecha
		if (fechaSeleccionada != null ) {
			criteria.add(Restrictions.eq("movimientoCaja.fecha", fechaSeleccionada));
		}
//		filtro por centro atencion
		if (CentroAtencion!=null && !CentroAtencion.equals("") && !CentroAtencion.equals(String.valueOf(ConstantesBD.codigoNuncaValido))) {
			criteria.add(Restrictions.eq("centroAtencion.codigo", CentroAtencion));
		}
//		filtro por institucion
		if (empresaInstitucion!=null && !empresaInstitucion.equals("")&& !empresaInstitucion.equals(String.valueOf(ConstantesBD.codigoNuncaValido))) {
			criteria.add(Restrictions.eq("instituciones.codigo",Integer.valueOf(empresaInstitucion.trim())));
		}
		
		// order by centro de atencion
		criteria.addOrder(Order.asc("centroAtencion.consecutivo"));
		
		//proyeccion a dto
		criteria.setProjection(Projections.projectionList()	
				.add(Projections.property("docSopMovimCajases.valor"),"valor")
				.add(Projections.property("formasPago.descripcion"),"formaPagoDescripcion")
				.add(Projections.property("centroAtencion.descripcion"),"centroAtencion")
				.add(Projections.property("instituciones.razonSocial"),"Insitucion")
			).setResultTransformer( Transformers.aliasToBean(DtoConsolidadoCierreXCentroAtencion.class));
		 ArrayList<DtoConsolidadoCierreXCentroAtencion> res=(ArrayList)criteria.list();
		
		return res;
		
	}
	
	
	/**
	 * Metodo que consulta los cierres de tranlados por caja cajero 
	 * @param fechaSeleccionada
	 * @param CentroAtencion
	 * @param empresaInstitucion
	 * @return cierres translados de caja cajero
	 */
	public ArrayList<DtoConsolidadoCierreXCentroAtencion> consultarMovimientosCierreCajaTransladosCajaCajero(Date fechaSeleccionada, String CentroAtencion,String empresaInstitucion){
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(MovimientosCaja.class,"movimientoCaja")
		.createAlias("movimientoCaja.cierreCajaAcepTrasCajas", "cierreCajaAcepTrasCajas")
		.createAlias("movimientoCaja.tiposMovimientoCaja", "tiposMovimientoCaja")
		.createAlias("movimientoCaja.turnoDeCaja", "turnoDeCaja")
		.createAlias("turnoDeCaja.centroAtencion", "centroAtencion")
		.createAlias("centroAtencion.instituciones", "instituciones")
		.createAlias("cierreCajaAcepTrasCajas.aceptacionTrasladoCaja", "aceptacionTrasladoCaja")
		.createAlias("aceptacionTrasladoCaja.movimientosCaja", "movimientosCajaAceptacion")
		.createAlias("movimientosCajaAceptacion.docSopMovimCajases", "docSopMovimCajases")
		.createAlias("docSopMovimCajases.formasPago", "formasPago")
		.createAlias("turnoDeCaja.cajas", "cajas")
		.createAlias("turnoDeCaja.usuarios", "usuarios")
		.createAlias("usuarios.personas", "personas")
		;
		
//		filtro de fecha 
		if (fechaSeleccionada != null ) {
			criteria.add(Restrictions.eq("movimientoCaja.fecha", fechaSeleccionada));
		}
//		filtro de centro atencion
		if (CentroAtencion!=null && !CentroAtencion.equals("") && !CentroAtencion.equals(String.valueOf(ConstantesBD.codigoNuncaValido))) {
			criteria.add(Restrictions.eq("centroAtencion.codigo", CentroAtencion));
		}

//      filtro de institucion		
		if (empresaInstitucion!=null && !empresaInstitucion.equals("")&& !empresaInstitucion.equals(String.valueOf(ConstantesBD.codigoNuncaValido))) {
			criteria.add(Restrictions.eq("instituciones.codigo", Integer.valueOf(empresaInstitucion.trim())));
		}
		// oreder by turno de caja 
		criteria.addOrder(Order.desc("turnoDeCaja.horaApertura"));

		// proyeccion a dto 
		criteria.setProjection(Projections.projectionList()	
				.add(Projections.property("docSopMovimCajases.valor"),"valor")
				.add(Projections.property("formasPago.descripcion"),"formaPagoDescripcion")
				.add(Projections.property("centroAtencion.descripcion"),"centroAtencion")
				.add(Projections.property("instituciones.razonSocial"),"Insitucion")
				.add(Projections.property("turnoDeCaja.horaApertura"),"horaApertura")
				.add(Projections.property("movimientoCaja.hora"),"hora")
				.add(Projections.property("personas.primerNombre"),"primerNombre")
				.add(Projections.property("personas.segundoNombre"),"segundonombre")
				.add(Projections.property("personas.primerApellido"),"primerApellido")
				.add(Projections.property("personas.segundoApellido"),"segundoApellido")
				.add(Projections.property("usuarios.login"),"login")
				.add(Projections.property("cajas.codigo"),"cajaCodigo")
				.add(Projections.property("cajas.descripcion"),"cajaDescripcion")
			).setResultTransformer( Transformers.aliasToBean(DtoConsolidadoCierreXCentroAtencion.class));
		 ArrayList<DtoConsolidadoCierreXCentroAtencion> res=(ArrayList)criteria.list();
		
		return res;
		
	}
	

	
	/**
	 * Metodo que se encarga de consultar los cierres solciitudes por centro de atencion
	 * @param fechaSeleccionada
	 * @param CentroAtencion
	 * @param empresaInstitucion
	 * @return cierres de solicitudes por centro de atencion
	 */
	public ArrayList<DtoConsolidadoCierreXCentroAtencion> consultarMovimientosCierreCajaSolicitudes(Date fechaSeleccionada, String CentroAtencion,String empresaInstitucion){
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(SolicitudTrasladoCaja.class,"solicitudTrasladoCaja")
		.createAlias("solicitudTrasladoCaja.movimientosCajaByMovimientoCajaCierre", "movimientosCajaByMovimientoCajaCierre")
		.createAlias("solicitudTrasladoCaja.movimientosCajaByCodigoPk", "movimientosCajaByCodigoPk")
		.createAlias("movimientosCajaByCodigoPk.docSopMovimCajases", "docSopMovimCajases")
		.createAlias("movimientosCajaByCodigoPk.tiposMovimientoCaja", "tiposMovimientoCaja")
		.createAlias("movimientosCajaByCodigoPk.turnoDeCaja", "turnoDeCaja")
		.createAlias("turnoDeCaja.centroAtencion", "centroAtencion")
		.createAlias("centroAtencion.instituciones", "instituciones")
		.createAlias("docSopMovimCajases.formasPago", "formasPago")
		;
	
		
//		filtro de fecha
		if (fechaSeleccionada != null ) {
			criteria.add(Restrictions.eq("movimientosCajaByMovimientoCajaCierre.fecha", fechaSeleccionada));
		}
//		filtro de centro de atencion
		if (CentroAtencion!=null && !CentroAtencion.equals("") && !CentroAtencion.equals(String.valueOf(ConstantesBD.codigoNuncaValido))) {
			criteria.add(Restrictions.eq("centroAtencion.codigo", CentroAtencion));
		}
//		filtro de institucion
		if (empresaInstitucion!=null && !UtilidadTexto.isEmpty(empresaInstitucion)&& !empresaInstitucion.equals(String.valueOf(ConstantesBD.codigoNuncaValido))) {
			criteria.add(Restrictions.eq("instituciones.codigo", Integer.valueOf(empresaInstitucion.trim())));
		}
		
		// oreder by centro de atencion
		criteria.addOrder(Order.asc("centroAtencion.consecutivo"));
		
		//proyeccion al dto
		criteria.setProjection(Projections.projectionList()	
				.add(Projections.property("docSopMovimCajases.valor"),"valor")
				.add(Projections.property("formasPago.descripcion"),"formaPagoDescripcion")
				.add(Projections.property("centroAtencion.descripcion"),"centroAtencion")
				.add(Projections.property("instituciones.razonSocial"),"Insitucion")
			).setResultTransformer( Transformers.aliasToBean(DtoConsolidadoCierreXCentroAtencion.class));
		 ArrayList<DtoConsolidadoCierreXCentroAtencion> res=(ArrayList)criteria.list();
		
		return res;
		
	}
	

	/**
	 * Metodo que se encarga de consultar los cierres de solcitudes por caja cajero 
	 * @param fechaSeleccionada
	 * @param CentroAtencion
	 * @param empresaInstitucion
	 * @return cierres de solciitudes de caja cajero 
	 */
	public ArrayList<DtoConsolidadoCierreXCentroAtencion> consultarMovimientosCierreCajaSolicitudesCajaCajero(Date fechaSeleccionada, String CentroAtencion,String empresaInstitucion){
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(SolicitudTrasladoCaja.class,"solicitudTrasladoCaja")
		.createAlias("solicitudTrasladoCaja.movimientosCajaByMovimientoCajaCierre", "movimientosCajaByMovimientoCajaCierre")
		.createAlias("solicitudTrasladoCaja.movimientosCajaByCodigoPk", "movimientosCajaByCodigoPk")
		.createAlias("movimientosCajaByCodigoPk.docSopMovimCajases", "docSopMovimCajases")
		.createAlias("movimientosCajaByCodigoPk.tiposMovimientoCaja", "tiposMovimientoCaja")
		.createAlias("movimientosCajaByCodigoPk.turnoDeCaja", "turnoDeCaja")
		.createAlias("turnoDeCaja.centroAtencion", "centroAtencion")
		.createAlias("centroAtencion.instituciones", "instituciones")
		.createAlias("docSopMovimCajases.formasPago", "formasPago")
		.createAlias("turnoDeCaja.cajas", "cajas")
		.createAlias("turnoDeCaja.usuarios", "usuarios")
		.createAlias("usuarios.personas", "personas")
		;
	
		
//		filtro de fecha
		if (fechaSeleccionada != null ) {
			criteria.add(Restrictions.eq("movimientosCajaByMovimientoCajaCierre.fecha", fechaSeleccionada));
		}
//		filtro de centro de atencion
		if (CentroAtencion!=null && !CentroAtencion.equals("") && !CentroAtencion.equals(String.valueOf(ConstantesBD.codigoNuncaValido))) {
			criteria.add(Restrictions.eq("centroAtencion.codigo", CentroAtencion));
		}
//		filtro de institucion
		if (empresaInstitucion!=null && !UtilidadTexto.isEmpty(empresaInstitucion)&& !empresaInstitucion.equals(String.valueOf(ConstantesBD.codigoNuncaValido))) {
			criteria.add(Restrictions.eq("instituciones.codigo", Integer.valueOf(empresaInstitucion.trim())));
		}
		
		//order by turno de caja 
		criteria.addOrder(Order.desc("turnoDeCaja.horaApertura"));
		
		//proyeccion al dto
		criteria.setProjection(Projections.projectionList()	
				.add(Projections.property("docSopMovimCajases.valor"),"valor")
				.add(Projections.property("formasPago.descripcion"),"formaPagoDescripcion")
				.add(Projections.property("centroAtencion.descripcion"),"centroAtencion")
				.add(Projections.property("instituciones.razonSocial"),"Insitucion")
				.add(Projections.property("turnoDeCaja.horaApertura"),"horaApertura")
				.add(Projections.property("movimientosCajaByMovimientoCajaCierre.hora"),"hora")
				.add(Projections.property("personas.primerNombre"),"primerNombre")
				.add(Projections.property("personas.segundoNombre"),"segundonombre")
				.add(Projections.property("personas.primerApellido"),"primerApellido")
				.add(Projections.property("personas.segundoApellido"),"segundoApellido")
				.add(Projections.property("usuarios.login"),"login")
				.add(Projections.property("cajas.codigo"),"cajaCodigo")
				.add(Projections.property("cajas.descripcion"),"cajaDescripcion")
			).setResultTransformer( Transformers.aliasToBean(DtoConsolidadoCierreXCentroAtencion.class));
		 ArrayList<DtoConsolidadoCierreXCentroAtencion> res=(ArrayList)criteria.list();
		
		return res;
		
	}
	

	

	
	

	/**
	 * Metodo que consulta los faltantes y sobrantes por centro de atencion
	 * @param fechaSeleccionada
	 * @param CentroAtencion
	 * @param empresaInstitucion
	 * @return consultar Movimientos de cierre de faltantes sobramtes por centro de atencion
	 */
	public ArrayList<DtoConsolidadoCierreXCentroAtencion> consultarMovimientosCierreCajaFaltantesSobrantes(Date fechaSeleccionada, String CentroAtencion,String empresaInstitucion){
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(MovimientosCaja.class,"movimientoCaja")
		.createAlias("movimientoCaja.tiposMovimientoCaja", "tiposMovimientoCaja")
		.createAlias("movimientoCaja.turnoDeCaja", "turnoDeCaja")
		.createAlias("turnoDeCaja.centroAtencion", "centroAtencion")
		.createAlias("centroAtencion.instituciones", "instituciones")
		.createAlias("movimientoCaja.docSopMovimCajases", "docSopMovimCajases")
		.createAlias("docSopMovimCajases.formasPago", "formasPago")
		.createAlias("docSopMovimCajases.detFaltanteSobrantes", "detFaltanteSobrantes")
		;
	
		
//		filtro de fecha
		if (fechaSeleccionada != null ) {
			criteria.add(Restrictions.eq("movimientoCaja.fecha", fechaSeleccionada));
		}
//		filtro de codigo
		if (CentroAtencion!=null && !CentroAtencion.equals("") && !CentroAtencion.equals(String.valueOf(ConstantesBD.codigoNuncaValido))) {
			criteria.add(Restrictions.eq("centroAtencion.codigo", CentroAtencion));
		}
//		filtro de institucion
		if (empresaInstitucion!=null && !empresaInstitucion.equals("")&& !empresaInstitucion.equals(String.valueOf(ConstantesBD.codigoNuncaValido))) {
			criteria.add(Restrictions.eq("instituciones.codigo", Integer.valueOf(empresaInstitucion.trim())));
		}
		
		//order by centro de atencion
		criteria.addOrder(Order.asc("centroAtencion.consecutivo"));
		
		//proyeccion a dto
		criteria.setProjection(Projections.projectionList()	
				.add(Projections.property("detFaltanteSobrantes.valorDiferencia"),"valor")
				.add(Projections.property("formasPago.descripcion"),"formaPagoDescripcion")
				.add(Projections.property("centroAtencion.descripcion"),"centroAtencion")
				.add(Projections.property("instituciones.razonSocial"),"Insitucion")
				.add(Projections.property("detFaltanteSobrantes.tipoDiferencia"),"tipoDiferenciaFaltante")
				.add(Projections.property("detFaltanteSobrantes.valorDiferencia"),"valorDiferencia")
			).setResultTransformer( Transformers.aliasToBean(DtoConsolidadoCierreXCentroAtencion.class));
		 ArrayList<DtoConsolidadoCierreXCentroAtencion> res=(ArrayList)criteria.list();
		
		return res;
		
	}
	
	
	
	 
	/**
	 * Metodo que se encarga de consultar faltantes y sobrantes por caja cajero 
	 * @param fechaSeleccionada
	 * @param CentroAtencion
	 * @param empresaInstitucion
	 * @return lista con faltantes y sobrantes por caja cajero 
	 */
	public ArrayList<DtoConsolidadoCierreXCentroAtencion> consultarMovimientosCierreCajaFaltantesSobrantesCajaCajero(Date fechaSeleccionada, String CentroAtencion,String empresaInstitucion){
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(MovimientosCaja.class,"movimientoCaja")
		.createAlias("movimientoCaja.tiposMovimientoCaja", "tiposMovimientoCaja")
		.createAlias("movimientoCaja.turnoDeCaja", "turnoDeCaja")
		.createAlias("turnoDeCaja.centroAtencion", "centroAtencion")
		.createAlias("centroAtencion.instituciones", "instituciones")
		.createAlias("movimientoCaja.docSopMovimCajases", "docSopMovimCajases")
		.createAlias("docSopMovimCajases.formasPago", "formasPago")
		.createAlias("docSopMovimCajases.detFaltanteSobrantes", "detFaltanteSobrantes")
		.createAlias("turnoDeCaja.cajas", "cajas")
		.createAlias("turnoDeCaja.usuarios", "usuarios")
		.createAlias("usuarios.personas", "personas")
		;
	
//		filtro de fecha
		if (fechaSeleccionada != null ) {
			criteria.add(Restrictions.eq("movimientoCaja.fecha", fechaSeleccionada));
		}
//		filtro de centro de atencion
		if (CentroAtencion!=null && !CentroAtencion.equals("") && !CentroAtencion.equals(String.valueOf(ConstantesBD.codigoNuncaValido))) {
			criteria.add(Restrictions.eq("centroAtencion.codigo", CentroAtencion));
		}
//		filtro de institucion
		if (empresaInstitucion!=null && !empresaInstitucion.equals("")&& !empresaInstitucion.equals(String.valueOf(ConstantesBD.codigoNuncaValido))) {
			criteria.add(Restrictions.eq("instituciones.codigo", Integer.valueOf(empresaInstitucion.trim())));
		}
		
		//order by turno de caja 
		criteria.addOrder(Order.desc("turnoDeCaja.horaApertura"));
		
		//proyeccion a dto
		criteria.setProjection(Projections.projectionList()	
				.add(Projections.property("detFaltanteSobrantes.valorDiferencia"),"valor")
				.add(Projections.property("formasPago.descripcion"),"formaPagoDescripcion")
				.add(Projections.property("centroAtencion.descripcion"),"centroAtencion")
				.add(Projections.property("instituciones.razonSocial"),"Insitucion")
				.add(Projections.property("turnoDeCaja.horaApertura"),"horaApertura")
				.add(Projections.property("movimientoCaja.hora"),"hora")
				.add(Projections.property("personas.primerNombre"),"primerNombre")
				.add(Projections.property("personas.segundoNombre"),"segundonombre")
				.add(Projections.property("personas.primerApellido"),"primerApellido")
				.add(Projections.property("personas.segundoApellido"),"segundoApellido")
				.add(Projections.property("usuarios.login"),"login")
				.add(Projections.property("cajas.codigo"),"cajaCodigo")
				.add(Projections.property("cajas.descripcion"),"cajaDescripcion")
			).setResultTransformer( Transformers.aliasToBean(DtoConsolidadoCierreXCentroAtencion.class));
		 ArrayList<DtoConsolidadoCierreXCentroAtencion> res=(ArrayList)criteria.list();
		
		return res;
		
	}
	
	
	
	

	/**
	 * Metodo que lista los cierres de recibos de caja por centro de atencion
	 * @param fechaSeleccionada
	 * @param CentroAtencion
	 * @param empresaInstitucion
	 * @return lista con cierres de recibos de caja por centro de atencion
	 */
	public ArrayList<DtoConsolidadoCierreXCentroAtencion> consultarMovimientosCierreRecibosCaja(Date fechaSeleccionada, String CentroAtencion,String empresaInstitucion){
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(MovimientosCaja.class,"movimientoCaja")
		.createAlias("movimientoCaja.cierreCajaXReciboCajas", "cierreCajaXReciboCajas")
		.createAlias("cierreCajaXReciboCajas.recibosCaja", "recibosCaja")
		.createAlias("recibosCaja.detallePagosRcs", "detallePagosRcs")
		.createAlias("movimientoCaja.turnoDeCaja", "turnoDeCaja")
		.createAlias("turnoDeCaja.centroAtencion", "centroAtencion")
		.createAlias("detallePagosRcs.formasPago", "formasPago")
		.createAlias("centroAtencion.instituciones", "instituciones")
		;
	
		
		criteria.add(Restrictions.ne("recibosCaja.estadosRecibosCaja.codigo", ConstantesBD.codigoEstadoReciboCajaAnulado));
		
//		filtro de fecha 
		if (fechaSeleccionada != null ) {
			criteria.add(Restrictions.eq("movimientoCaja.fecha", fechaSeleccionada));
		}
//		filtro de centro de atencion
		if (CentroAtencion!=null && !CentroAtencion.equals("") && !CentroAtencion.equals(String.valueOf(ConstantesBD.codigoNuncaValido))) {
			criteria.add(Restrictions.eq("centroAtencion.codigo", CentroAtencion));
		}
//		filtro de institucion
		if (empresaInstitucion!=null && !empresaInstitucion.equals("")&& !empresaInstitucion.equals(String.valueOf(ConstantesBD.codigoNuncaValido))) {
			criteria.add(Restrictions.eq("instituciones.codigo", Integer.valueOf(empresaInstitucion.trim())));
		}
		//order by centro de atencion
		criteria.addOrder(Order.asc("centroAtencion.consecutivo"));
		
		//proyeccion a dto
		criteria.setProjection(Projections.projectionList()	
				.add(Projections.property("detallePagosRcs.valor"),"valor")
				.add(Projections.property("formasPago.descripcion"),"formaPagoDescripcion")
				.add(Projections.property("centroAtencion.descripcion"),"centroAtencion")
				.add(Projections.property("instituciones.razonSocial"),"Insitucion")
			).setResultTransformer( Transformers.aliasToBean(DtoConsolidadoCierreXCentroAtencion.class));
		 ArrayList<DtoConsolidadoCierreXCentroAtencion> res=(ArrayList)criteria.list();
		
		return res;
	}
	

	/**
	 * Metodo que se encarga de listar los cierres de recibo de caja por caja cajero
	 * @param fechaSeleccionada
	 * @param CentroAtencion
	 * @param empresaInstitucion
	 * @return lista con cierres de recibo de caja por caja cajero
	 */
	public ArrayList<DtoConsolidadoCierreXCentroAtencion> consultarMovimientosCierreRecibosCajaCajaCajero(Date fechaSeleccionada, String CentroAtencion,String empresaInstitucion){
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(MovimientosCaja.class,"movimientoCaja")
		.createAlias("movimientoCaja.cierreCajaXReciboCajas", "cierreCajaXReciboCajas")
		.createAlias("cierreCajaXReciboCajas.recibosCaja", "recibosCaja")
		.createAlias("recibosCaja.detallePagosRcs", "detallePagosRcs")
		.createAlias("movimientoCaja.turnoDeCaja", "turnoDeCaja")
		.createAlias("turnoDeCaja.centroAtencion", "centroAtencion")
		.createAlias("detallePagosRcs.formasPago", "formasPago")
		.createAlias("centroAtencion.instituciones", "instituciones")
		.createAlias("turnoDeCaja.cajas", "cajas")
		.createAlias("turnoDeCaja.usuarios", "usuarios")
		.createAlias("usuarios.personas", "personas")
		;
	
		criteria.add(Restrictions.ne("recibosCaja.estadosRecibosCaja.codigo", ConstantesBD.codigoEstadoReciboCajaAnulado));
		
		
//		filtro de fecha
		if (fechaSeleccionada != null ) {
			criteria.add(Restrictions.eq("movimientoCaja.fecha", fechaSeleccionada));
		}
//		filtro de centro atencion
		if (CentroAtencion!=null && !CentroAtencion.equals("") && !CentroAtencion.equals(String.valueOf(ConstantesBD.codigoNuncaValido))) {
			criteria.add(Restrictions.eq("centroAtencion.codigo", CentroAtencion));
		}
//		filtro de institucion
		if (empresaInstitucion!=null && !empresaInstitucion.equals("")&& !empresaInstitucion.equals(String.valueOf(ConstantesBD.codigoNuncaValido))) {
			criteria.add(Restrictions.eq("instituciones.codigo",Integer.valueOf(empresaInstitucion.trim())));
		}
		//order by turno de caja 
		criteria.addOrder(Order.desc("turnoDeCaja.horaApertura"));
		//proyeccion a dto
		criteria.setProjection(Projections.projectionList()	
				.add(Projections.property("detallePagosRcs.valor"),"valor")
				.add(Projections.property("formasPago.descripcion"),"formaPagoDescripcion")
				.add(Projections.property("centroAtencion.descripcion"),"centroAtencion")
				.add(Projections.property("instituciones.razonSocial"),"Insitucion")
				.add(Projections.property("turnoDeCaja.horaApertura"),"horaApertura")
				.add(Projections.property("movimientoCaja.hora"),"hora")
				.add(Projections.property("personas.primerNombre"),"primerNombre")
				.add(Projections.property("personas.segundoNombre"),"segundonombre")
				.add(Projections.property("personas.primerApellido"),"primerApellido")
				.add(Projections.property("personas.segundoApellido"),"segundoApellido")
				.add(Projections.property("usuarios.login"),"login")
				.add(Projections.property("cajas.codigo"),"cajaCodigo")
				.add(Projections.property("cajas.descripcion"),"cajaDescripcion")
			).setResultTransformer( Transformers.aliasToBean(DtoConsolidadoCierreXCentroAtencion.class));
		 ArrayList<DtoConsolidadoCierreXCentroAtencion> res=(ArrayList)criteria.list();
		
		return res;
	}
	
	
	
	/**
	 * Metodo que se encarga de listar los cierres de recibo de caja por caja cajero
	 * @param fechaSeleccionada
	 * @param CentroAtencion
	 * @param empresaInstitucion
	 * @author Cristhian Murillo
	 * 
	 * @return ArrayList<DtoConsolidadoCierreXCentroAtencion>
	 */
	public ArrayList<DtoConsolidadoCierreXCentroAtencion> consultarMovimientosCierreRecibosCajaCajaCajeroEntregaCajaPrincipal(Date fechaSeleccionada, String centroAtencion,String empresaInstitucion, Integer cajaEntregaCajaMayor, Integer tipoMovimiento)
	{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(MovimientosCaja.class,"movimientoCaja")
		.createAlias("movimientoCaja.cierreCajaXReciboCajas"					, "cierreCajaXReciboCajas")
		.createAlias("cierreCajaXReciboCajas.recibosCaja"						, "recibosCaja")
		.createAlias("recibosCaja.detallePagosRcs"								, "detallePagosRcs")
		.createAlias("movimientoCaja.turnoDeCaja"								, "turnoDeCaja")
		.createAlias("turnoDeCaja.centroAtencion"								, "centroAtencion")
		.createAlias("detallePagosRcs.formasPago"								, "formasPago")
		.createAlias("centroAtencion.instituciones"								, "instituciones")
		.createAlias("turnoDeCaja.cajas"										, "cajas")
		.createAlias("turnoDeCaja.usuarios"										, "usuarios")
		.createAlias("usuarios.personas"										, "personas")
		.createAlias("movimientoCaja.entregaCajaMayorsForMovimientoCajaArqueo"	, "entregaCajaMayor"					, Criteria.LEFT_JOIN)
		.createAlias("entregaCajaMayor.movimientosCajaByCodigoPk"				, "entregaCajaMayorMovimientoCierrePk"	, Criteria.LEFT_JOIN) 
		.createAlias("entregaCajaMayor.cajas"									, "cajaEntregaCajaMayor"				, Criteria.LEFT_JOIN)
		.createAlias("entregaCajaMayor.trasladoCajaPrincipalMayor"				, "trasladoCajaPrincipalMayor"			, Criteria.LEFT_JOIN)
	;

	criteria.add(Restrictions.ne("recibosCaja.estadosRecibosCaja.codigo", ConstantesBD.codigoEstadoReciboCajaAnulado));
	//filtro de fecha
	if (fechaSeleccionada != null ) {
		criteria.add(Restrictions.eq("movimientoCaja.fecha", fechaSeleccionada));
	}
	
	//filtro de centro atencion
	if (!UtilidadTexto.isEmpty(centroAtencion) && !centroAtencion.equals(String.valueOf(ConstantesBD.codigoNuncaValido))) {
		criteria.add(Restrictions.eq("centroAtencion.consecutivo", Integer.parseInt(centroAtencion)));
	}
	
	//filtro de institucion
	if (!UtilidadTexto.isEmpty(empresaInstitucion)&& !empresaInstitucion.equals(String.valueOf(ConstantesBD.codigoNuncaValido))) {
		criteria.add(Restrictions.eq("instituciones.codigo",Integer.parseInt(empresaInstitucion.trim())));
	}
	
	//filtro caja principal del traslado 
	if(cajaEntregaCajaMayor != null){
		criteria.add(Restrictions.eq("cajaEntregaCajaMayor.consecutivo", cajaEntregaCajaMayor));
	}
	
	//order by turno de caja apertura
	criteria.addOrder(Order.desc("turnoDeCaja.horaApertura"));
	
	//proyeccion a dto
	criteria.setProjection(Projections.projectionList()	
			.add(Projections.property("entregaCajaMayorMovimientoCierrePk.codigoPk")			,"movimientoCaja") 
			.add(Projections.property("movimientoCaja.consecutivo")								,"consecutivo") 
			.add(Projections.property("detallePagosRcs.valor")									,"valor")
			.add(Projections.property("formasPago.descripcion")									,"formaPagoDescripcion")
			.add(Projections.property("centroAtencion.descripcion")								,"centroAtencion")
			.add(Projections.property("instituciones.razonSocial")								,"Insitucion")
			.add(Projections.property("turnoDeCaja.horaApertura")								,"horaApertura")
			.add(Projections.property("movimientoCaja.hora")									,"hora")
			.add(Projections.property("personas.primerNombre")									,"primerNombre")
			.add(Projections.property("personas.segundoNombre")									,"segundonombre")
			.add(Projections.property("personas.primerApellido")								,"primerApellido")
			.add(Projections.property("personas.segundoApellido")								,"segundoApellido")
			.add(Projections.property("usuarios.login")											,"login")
			.add(Projections.property("cajas.codigo")											,"cajaCodigo")
			.add(Projections.property("cajas.descripcion")										,"cajaDescripcion")
			.add(Projections.property("trasladoCajaPrincipalMayor.movimientoEntregaCajaMayor")	,"trasladoCajaPrincipalMayor")
			.add(Projections.property("trasladoCajaPrincipalMayor.consecutivo")					,"consecutivoTraslado")
			
		).setResultTransformer( Transformers.aliasToBean(DtoConsolidadoCierreXCentroAtencion.class));
	
		 ArrayList<DtoConsolidadoCierreXCentroAtencion> res=(ArrayList)criteria.list();
		
		return res;
	}
	
	
	
	/**
	 * Metodo que consulta ls cierres de devoluciones pro centro atencion
	 * @param fechaSeleccionada
	 * @param CentroAtencion
	 * @param empresaInstitucion
	 * @return lista con cierres de devolucion
	 */
	public ArrayList<DtoConsolidadoCierreXCentroAtencion> consultarMovimientosCierreDevolucionRecibosCaja(Date fechaSeleccionada, String CentroAtencion,String empresaInstitucion){
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(DevolRecibosCaja.class,"devolRecibosCaja")
		.createAlias("devolRecibosCaja.turnoDeCaja", "turnoDeCaja")
		.createAlias("devolRecibosCaja.formasPago", "formasPago")
		.createAlias("devolRecibosCaja.cierreCajaXDevolRecibos", "cierreCajaXDevolRecibos")
		.createAlias("cierreCajaXDevolRecibos.movimientosCaja", "movimientosCaja")
		.createAlias("turnoDeCaja.centroAtencion", "centroAtencion")
		.createAlias("devolRecibosCaja.recibosCaja", "recibosCaja")
		.createAlias("devolRecibosCaja.instituciones", "instituciones")
		.add(Restrictions.eq("devolRecibosCaja.estado", ConstantesIntegridadDominio.acronimoEstadoAprobado));
		
//		filtro de fecha
		if (fechaSeleccionada != null ) {
			criteria.add(Restrictions.eq("movimientosCaja.fecha", fechaSeleccionada));
		}
//		filtro de centro de atencion
		if (CentroAtencion!=null && !CentroAtencion.equals("") && !CentroAtencion.equals(String.valueOf(ConstantesBD.codigoNuncaValido))) {
			criteria.add(Restrictions.eq("centroAtencion.codigo", CentroAtencion));
		}
//		filtro de institucion
		if (empresaInstitucion!=null && !empresaInstitucion.equals("")&& !empresaInstitucion.equals(String.valueOf(ConstantesBD.codigoNuncaValido))) {
			criteria.add(Restrictions.eq("instituciones.codigo", Integer.valueOf(empresaInstitucion.trim())));
		}
		//order by centro de atencion 
		criteria.addOrder(Order.asc("centroAtencion.consecutivo"));
		
		//proyeccion a dto
		criteria.setProjection(Projections.projectionList()	
				.add(Projections.property("devolRecibosCaja.valorDevolucion"),"valor")
				.add(Projections.property("formasPago.descripcion"),"formaPagoDescripcion")
				.add(Projections.property("centroAtencion.descripcion"),"centroAtencion")
				.add(Projections.property("instituciones.razonSocial"),"Insitucion")
			).setResultTransformer( Transformers.aliasToBean(DtoConsolidadoCierreXCentroAtencion.class));
		 ArrayList<DtoConsolidadoCierreXCentroAtencion> res=(ArrayList)criteria.list();
		
		return res;
		
	}
	
	
	/**
	 * metodo que consulta cierres de devoluciones por caja cajero
	 * @param fechaSeleccionada
	 * @param CentroAtencion
	 * @param empresaInstitucion
	 * @return cierres de devoluciones por caja cajero
	 */
	public ArrayList<DtoConsolidadoCierreXCentroAtencion> consultarMovimientosCierreDevolucionRecibosCajaCajero(Date fechaSeleccionada, String CentroAtencion,String empresaInstitucion){
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(DevolRecibosCaja.class,"devolRecibosCaja")
		.createAlias("devolRecibosCaja.turnoDeCaja", "turnoDeCaja")
		.createAlias("devolRecibosCaja.formasPago", "formasPago")
		.createAlias("devolRecibosCaja.cierreCajaXDevolRecibos", "cierreCajaXDevolRecibos")
		.createAlias("cierreCajaXDevolRecibos.movimientosCaja", "movimientosCaja")
		.createAlias("turnoDeCaja.centroAtencion", "centroAtencion")
		.createAlias("devolRecibosCaja.recibosCaja", "recibosCaja")
		.createAlias("devolRecibosCaja.instituciones", "instituciones")
		.createAlias("turnoDeCaja.cajas", "cajas")
		.createAlias("turnoDeCaja.usuarios", "usuarios")
		.createAlias("usuarios.personas", "personas")
		.add(Restrictions.eq("devolRecibosCaja.estado", ConstantesIntegridadDominio.acronimoEstadoAprobado));
		;
		
//		filtro de fecha
		if (fechaSeleccionada != null ) {
			criteria.add(Restrictions.eq("movimientosCaja.fecha", fechaSeleccionada));
		}
//		filtro de centro de atencion
		if (CentroAtencion!=null && !CentroAtencion.equals("") && !CentroAtencion.equals(String.valueOf(ConstantesBD.codigoNuncaValido))) {
			criteria.add(Restrictions.eq("centroAtencion.codigo", CentroAtencion));
		}
//		filtro institucion
		if (empresaInstitucion!=null && !empresaInstitucion.equals("")&& !empresaInstitucion.equals(String.valueOf(ConstantesBD.codigoNuncaValido))) {
			criteria.add(Restrictions.eq("instituciones.codigo", Integer.valueOf(empresaInstitucion.trim())));
		}
		//order by truno de caja  
		criteria.addOrder(Order.desc("turnoDeCaja.horaApertura"));
		
		//proyeccion a dto 
		criteria.setProjection(Projections.projectionList()	
				.add(Projections.property("devolRecibosCaja.valorDevolucion"),"valor")
				.add(Projections.property("formasPago.descripcion"),"formaPagoDescripcion")
				.add(Projections.property("centroAtencion.descripcion"),"centroAtencion")
				.add(Projections.property("instituciones.razonSocial"),"Insitucion")
				.add(Projections.property("turnoDeCaja.horaApertura"),"horaApertura")
			    .add(Projections.property("movimientosCaja.hora"),"hora")
				.add(Projections.property("personas.primerNombre"),"primerNombre")
				.add(Projections.property("personas.segundoNombre"),"segundonombre")
				.add(Projections.property("personas.primerApellido"),"primerApellido")
				.add(Projections.property("personas.segundoApellido"),"segundoApellido")
				.add(Projections.property("usuarios.login"),"login")
				.add(Projections.property("cajas.codigo"),"cajaCodigo")
				.add(Projections.property("cajas.descripcion"),"cajaDescripcion")
			).setResultTransformer( Transformers.aliasToBean(DtoConsolidadoCierreXCentroAtencion.class));
		 ArrayList<DtoConsolidadoCierreXCentroAtencion> res=(ArrayList)criteria.list();
		
		return res;
		
	}
	
	
	/**
	 * Metodo que se encarga de consultar cierres de entregas parciales 
	 * @param fechaSeleccionada
	 * @param CentroAtencion
	 * @param empresaInstitucion
	 * @return lista de cierres de entregas parciles
	 */
	public ArrayList<DtoConsolidadoCierreXCentroAtencion> consultarMovimientosCierreEntregasParciales(Date fechaSeleccionada, String CentroAtencion,String empresaInstitucion){
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(EntregaCajaMayor.class,"entregaCajaMayor")
		.createAlias("entregaCajaMayor.movimientosCajaByMovimientoCajaArqueo", "movimientosCajaByMovimientoCajaArqueo")
		.createAlias("entregaCajaMayor.movimientosCajaByCodigoPk", "movimientosCajaByCodigoPk")
		.createAlias("movimientosCajaByCodigoPk.docSopMovimCajases", "docSopMovimCajases")
		.createAlias("docSopMovimCajases.formasPago", "formasPago")
		.createAlias("movimientosCajaByCodigoPk.tiposMovimientoCaja", "tiposMovimientoCaja")
		.createAlias("movimientosCajaByCodigoPk.turnoDeCaja", "turnoDeCaja")
		.createAlias("turnoDeCaja.centroAtencion", "centroAtencion")
		.createAlias("centroAtencion.instituciones", "instituciones")
		;
	//filtro de entrega parcial
		criteria.add(Restrictions.eq("movimientosCajaByMovimientoCajaArqueo.tiposMovimientoCaja.codigo", ConstantesBD.entregaParcialCaja));

//		filtro de fecha 		
		if (fechaSeleccionada != null ) {
			criteria.add(Restrictions.eq("movimientosCajaByMovimientoCajaArqueo.fecha", fechaSeleccionada));
		}
//		filtro de centro de atencion
		if (CentroAtencion!=null && !CentroAtencion.equals("") && !CentroAtencion.equals(String.valueOf(ConstantesBD.codigoNuncaValido))) {
			criteria.add(Restrictions.eq("centroAtencion.codigo", CentroAtencion));
		}
//		filtro de institucion
		if (empresaInstitucion!=null && !empresaInstitucion.equals("")&& !empresaInstitucion.equals(String.valueOf(ConstantesBD.codigoNuncaValido))) {
			criteria.add(Restrictions.eq("instituciones.codigo", Integer.valueOf(empresaInstitucion.trim())));
		}
		
		//order by centro de atencion
		criteria.addOrder(Order.asc("centroAtencion.consecutivo"));
		
		//proyeccion a dto
		criteria.setProjection(Projections.projectionList()	
				.add(Projections.property("docSopMovimCajases.valor"),"valor")
				.add(Projections.property("formasPago.descripcion"),"formaPagoDescripcion")
				.add(Projections.property("centroAtencion.descripcion"),"centroAtencion")
				.add(Projections.property("instituciones.razonSocial"),"Insitucion")
			).setResultTransformer( Transformers.aliasToBean(DtoConsolidadoCierreXCentroAtencion.class));
		 ArrayList<DtoConsolidadoCierreXCentroAtencion> res=(ArrayList)criteria.list();
		
		return res;
		
	}
	
	
	/**
	 * Metodo que consulta cierres de  las entregas parciales caja cajero
	 * @param fechaSeleccionada
	 * @param CentroAtencion
	 * @param empresaInstitucion
	 * @return  cierres de  las entregas parciales caja cajero
	 */
	public ArrayList<DtoConsolidadoCierreXCentroAtencion> consultarMovimientosCierreEntregasParcialesCajaCajero(Date fechaSeleccionada, String CentroAtencion,String empresaInstitucion)
	{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(EntregaCajaMayor.class,"entregaCajaMayor")
			.createAlias("entregaCajaMayor.movimientosCajaByMovimientoCajaArqueo"	, "movimientosCajaByMovimientoCajaArqueo")
			.createAlias("entregaCajaMayor.movimientosCajaByCodigoPk"				, "movimientosCajaByCodigoPk")
			.createAlias("movimientosCajaByCodigoPk.docSopMovimCajases"				, "docSopMovimCajases")
			.createAlias("docSopMovimCajases.formasPago"							, "formasPago")
			.createAlias("movimientosCajaByCodigoPk.tiposMovimientoCaja"			, "tiposMovimientoCaja")
			.createAlias("movimientosCajaByCodigoPk.turnoDeCaja"					, "turnoDeCaja")
			.createAlias("turnoDeCaja.centroAtencion"								, "centroAtencion")
			.createAlias("centroAtencion.instituciones"								, "instituciones")
			 .createAlias("turnoDeCaja.cajas"										, "cajas")
			.createAlias("turnoDeCaja.usuarios"										, "usuarios")
			.createAlias("usuarios.personas"										, "personas")
			.createAlias("entregaCajaMayor.cajas"									, "cajaEntregaCajaMayor"				,Criteria.LEFT_JOIN )
		;
	
		//filtro de traslados
		criteria.add(Restrictions.eq("movimientosCajaByMovimientoCajaArqueo.tiposMovimientoCaja.codigo", ConstantesBD.codigoTipoMovimientoArqueoEntregaParcial));
		
		//filtro de fecha
		if (fechaSeleccionada != null ) {
			criteria.add(Restrictions.eq("movimientosCajaByMovimientoCajaArqueo.fecha", fechaSeleccionada));
		}
		
		//filtro centro atencion
		if (CentroAtencion!=null && !CentroAtencion.equals("") && !CentroAtencion.equals(String.valueOf(ConstantesBD.codigoNuncaValido))) {
			criteria.add(Restrictions.eq("centroAtencion.codigo", CentroAtencion));
		}
		
		//institucion
		if (empresaInstitucion!=null && !empresaInstitucion.equals("")&& !empresaInstitucion.equals(String.valueOf(ConstantesBD.codigoNuncaValido))) {
			criteria.add(Restrictions.eq("instituciones.codigo", Integer.valueOf(empresaInstitucion.trim())));
		}
		
		//filtro turno de caja apertura
		criteria.addOrder(Order.desc("turnoDeCaja.horaApertura"));
		
		//proyeccion 
		criteria.setProjection(Projections.projectionList()	
				.add(Projections.property("docSopMovimCajases.valor")					,"valor")
				.add(Projections.property("formasPago.descripcion")						,"formaPagoDescripcion")
				.add(Projections.property("centroAtencion.descripcion")					,"centroAtencion")
				.add(Projections.property("instituciones.razonSocial")					,"Insitucion")
				.add(Projections.property("turnoDeCaja.horaApertura")					,"horaApertura")
				.add(Projections.property("movimientosCajaByMovimientoCajaArqueo.hora")	,"hora")
				.add(Projections.property("personas.primerNombre")						,"primerNombre")
				.add(Projections.property("personas.segundoNombre")						,"segundonombre")
				.add(Projections.property("personas.primerApellido")					,"primerApellido")
				.add(Projections.property("personas.segundoApellido")					,"segundoApellido")
				.add(Projections.property("usuarios.login")								,"login")
				.add(Projections.property("cajas.codigo")								,"cajaCodigo")
				.add(Projections.property("cajas.descripcion")							,"cajaDescripcion")
			).setResultTransformer( Transformers.aliasToBean(DtoConsolidadoCierreXCentroAtencion.class));
		
		 ArrayList<DtoConsolidadoCierreXCentroAtencion> res=(ArrayList)criteria.list();
		
		return res;
	}
	
	
	
	/**
	 * Metodo que consulta cierres de las entregas a caja mayor.
	 * 
	 * @param fechaSeleccionada
	 * @param CentroAtencion
	 * @param empresaInstitucion
	 * @param cajaEntregaCajaMayor
	 * @param tipoMovimiento
	 * 
	 * @author Cristhian Murillo
	 * 
	 * @return  cierres de  las entregas parciales caja cajero
	*/
	public ArrayList<DtoConsolidadoCierreXCentroAtencion> consultarMovimientosEntregaCajaMayorCajaCajero(Date fechaSeleccionada, String centroAtencion,String empresaInstitucion, Integer cajaEntregaCajaMayor, Integer tipoMovimiento)
	{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(EntregaCajaMayor.class,"entregaCajaMayor")
			.createAlias("entregaCajaMayor.movimientosCajaByMovimientoCajaArqueo"	, "movimientosCajaByMovimientoCajaArqueo")
			.createAlias("entregaCajaMayor.movimientosCajaByCodigoPk"				, "movimientosCajaByCodigoPk")
			.createAlias("movimientosCajaByCodigoPk.docSopMovimCajases"				, "docSopMovimCajases")
			.createAlias("docSopMovimCajases.formasPago"							, "formasPago")
			.createAlias("movimientosCajaByCodigoPk.tiposMovimientoCaja"			, "tiposMovimientoCaja")
			.createAlias("movimientosCajaByCodigoPk.turnoDeCaja"					, "turnoDeCaja")
			.createAlias("turnoDeCaja.centroAtencion"								, "centroAtencion")
			.createAlias("centroAtencion.instituciones"								, "instituciones")
			.createAlias("turnoDeCaja.cajas"										, "cajas")
			.createAlias("turnoDeCaja.usuarios"										, "usuarios")
			.createAlias("usuarios.personas"										, "personas")
			.createAlias("entregaCajaMayor.cajas"									, "cajaEntregaCajaMayor")
		;
	
		//filtro tipo movimiento
		if(tipoMovimiento != null){
			criteria.add(Restrictions.eq("movimientosCajaByCodigoPk.tiposMovimientoCaja.codigo", tipoMovimiento));
		}
		
		//filtro de fecha
		if (fechaSeleccionada != null ) {
			criteria.add(Restrictions.eq("movimientosCajaByMovimientoCajaArqueo.fecha", fechaSeleccionada));
		}
		
		//filtro centro atencion
		if ( !UtilidadTexto.isEmpty(centroAtencion) && !centroAtencion.equals(String.valueOf(ConstantesBD.codigoNuncaValido))) {
			criteria.add(Restrictions.eq("centroAtencion.consecutivo", Integer.parseInt(centroAtencion))); 
		}
		
		//filtro institucion
		if ( !UtilidadTexto.isEmpty(empresaInstitucion) && !empresaInstitucion.equals(String.valueOf(ConstantesBD.codigoNuncaValido))) {
			criteria.add(Restrictions.eq("instituciones.codigo", Integer.parseInt(empresaInstitucion.trim())));
		}
		
		//filtro caja principal del traslado
		if(cajaEntregaCajaMayor != null){
			criteria.add(Restrictions.eq("cajaEntregaCajaMayor.consecutivo", cajaEntregaCajaMayor));
		}
		
		//filtro turno de caja apertura
		criteria.addOrder(Order.desc("turnoDeCaja.horaApertura"));
		

		criteria.setProjection(Projections.projectionList()	
				.add(Projections.property("movimientosCajaByCodigoPk.codigoPk")					,"movimientoCaja")
				.add(Projections.property("docSopMovimCajases.valor")							,"valor")
				.add(Projections.property("formasPago.descripcion")								,"formaPagoDescripcion")
				.add(Projections.property("centroAtencion.descripcion")							,"centroAtencion")
				.add(Projections.property("instituciones.razonSocial")							,"Insitucion")
				.add(Projections.property("turnoDeCaja.horaApertura")							,"horaApertura")
				.add(Projections.property("movimientosCajaByMovimientoCajaArqueo.hora")			,"hora")
				.add(Projections.property("personas.primerNombre")								,"primerNombre")
				.add(Projections.property("personas.segundoNombre")								,"segundonombre")
				.add(Projections.property("personas.primerApellido")							,"primerApellido")
				.add(Projections.property("personas.segundoApellido")							,"segundoApellido")
				.add(Projections.property("usuarios.login")										,"login")
				.add(Projections.property("cajas.codigo")										,"cajaCodigo")
				.add(Projections.property("cajas.descripcion")									,"cajaDescripcion")
				.add(Projections.property("movimientosCajaByMovimientoCajaArqueo.consecutivo")	,"consecutivo") 
				
			).setResultTransformer( Transformers.aliasToBean(DtoConsolidadoCierreXCentroAtencion.class));
		
		 ArrayList<DtoConsolidadoCierreXCentroAtencion> listaDtoConsolidadoCierreXCentroAtencion =(ArrayList)criteria.list();
		
		return listaDtoConsolidadoCierreXCentroAtencion;
		
	}
	
	
	
	/**
	 * Metodo que calcula los cierres caja cajero 
	 * @param fechaSeleccionada
	 * @param CentroAtencion
	 * @param empresaInstitucion
	 * @param formasPago
	 * @return lista con los cierres caja cajero 
	 */
	public ArrayList<DtoConsolidadoCierreReporte> consultarconsolidadoCierreCajaCajero(Date fechaSeleccionada, String CentroAtencion,String empresaInstitucion,List<FormasPago> formasPago)
	{
		
		//se consulta por cada tipo de cierre los movimientos caja cajeros asociados a los filtros seleccionados
		
		ArrayList<DtoConsolidadoCierreXCentroAtencion> translados=consultarMovimientosCierreCajaTransladosCajaCajero(fechaSeleccionada, CentroAtencion, empresaInstitucion);
		ArrayList<DtoConsolidadoCierreXCentroAtencion> solicitudes=consultarMovimientosCierreCajaSolicitudesCajaCajero(fechaSeleccionada, CentroAtencion, empresaInstitucion);
		ArrayList<DtoConsolidadoCierreXCentroAtencion> faltantesSobrantes=consultarMovimientosCierreCajaFaltantesSobrantesCajaCajero(fechaSeleccionada, CentroAtencion, empresaInstitucion);
		ArrayList<DtoConsolidadoCierreXCentroAtencion> recibosCaja=consultarMovimientosCierreRecibosCajaCajaCajero(fechaSeleccionada, CentroAtencion, empresaInstitucion);
		ArrayList<DtoConsolidadoCierreXCentroAtencion> devoluciones = consultarMovimientosCierreDevolucionRecibosCajaCajero(fechaSeleccionada, CentroAtencion, empresaInstitucion);
		ArrayList<DtoConsolidadoCierreXCentroAtencion> entregasParciales=consultarMovimientosCierreEntregasParcialesCajaCajero(fechaSeleccionada, CentroAtencion, empresaInstitucion);
		
		// por cada tipo de movimiento se totaliza por forma de pago 
		ArrayList<DtoConsolidadoCierreReporte> transladoReporte = totalizarCajaCajero(translados,ConstantesBD.translados,formasPago);
		ArrayList<DtoConsolidadoCierreReporte> solicitudesReporte = totalizarCajaCajero(solicitudes,ConstantesBD.solicitudes,formasPago);
		ArrayList<DtoConsolidadoCierreReporte> faltantesSobrantesReporte = totalizarCajaCajero(faltantesSobrantes,ConstantesBD.faltantesSobrantes,formasPago);
		ArrayList<DtoConsolidadoCierreReporte> recibosCajaReporte = totalizarCajaCajero(recibosCaja,ConstantesBD.recibosCaja,formasPago);
		ArrayList<DtoConsolidadoCierreReporte> devolucionesReporte = totalizarCajaCajero(devoluciones,ConstantesBD.devolucionreciboscaja,formasPago);
		ArrayList<DtoConsolidadoCierreReporte> entregasParcialesReporte = totalizarCajaCajero(entregasParciales,ConstantesBD.entregasparciales,formasPago);
		
		// se adicionan todos los totalizados en una sola lista para generar el consolidado
		ArrayList<DtoConsolidadoCierreReporte> dtoCalculados=new ArrayList<DtoConsolidadoCierreReporte>();
		dtoCalculados.addAll(transladoReporte);
		dtoCalculados.addAll(solicitudesReporte);
		dtoCalculados.addAll(faltantesSobrantesReporte);
		dtoCalculados.addAll(recibosCajaReporte);
		dtoCalculados.addAll(devolucionesReporte);
		dtoCalculados.addAll(entregasParcialesReporte);
		
		
		//se totaliza por tipo de cierre el gran consolidado
		ArrayList<DtoConsolidadoCierreReporte> totales =calculoPortipomedioPagoCajaCajero(dtoCalculados,formasPago);
		
		totales = agruparTotalSumado(totales, formasPago);
		
		
		return totales;
		
		
	}
	
	
	/**
	 * Agrupa para mostrar un solo consolidado por cierre y no los detalles de cada cierre.
	 * MT 1728
	 * 
	 * @param lista
	 * @param formasPago
	 * @return ArrayList<DtoConsolidadoCierreReporte>
	 *
	 * @autor Cristhian Murillo
	*/
	private ArrayList<DtoConsolidadoCierreReporte> agruparTotalSumado (ArrayList<DtoConsolidadoCierreReporte> lista, List<FormasPago> formasPago)
	{
		HashMap<String, DtoConsolidadoCierreReporte> mapaAgrupado 	= new HashMap<String, DtoConsolidadoCierreReporte>();
		ArrayList<String> listaKeys 								= new ArrayList<String>();
		
		for (DtoConsolidadoCierreReporte dtoConsolidadoCierreReporte : lista) 
		{
			DtoConsolidadoCierreReporte elemento;
			elemento = new DtoConsolidadoCierreReporte();
			
			String key = "";
			key += dtoConsolidadoCierreReporte.getCentroAtencion()+"_";
			key += dtoConsolidadoCierreReporte.getEmpresaInstitucion()+"_";
			key += dtoConsolidadoCierreReporte.getHoraApertura()+"_";
			key += dtoConsolidadoCierreReporte.getCajero()+"_"; 
			key += dtoConsolidadoCierreReporte.getCaja()+"_";
			
			if(!mapaAgrupado.containsKey(key))
			{
				elemento = dtoConsolidadoCierreReporte;
				listaKeys.add(key);
				mapaAgrupado.put(key, elemento);
			}
			else{
				elemento = mapaAgrupado.get(key);
				
				for (int i = 0; i < formasPago.size(); i++)	{
					elemento.getTotalesPorFormaPago().set(i, dtoConsolidadoCierreReporte.getTotalesPorFormaPago().get(i).add(elemento.getTotalesPorFormaPago().get(i)));
				}
				
				elemento.setTotalCentroAtencion(elemento.getTotalCentroAtencion().add(dtoConsolidadoCierreReporte.getTotalCentroAtencion()));
			}
		}
		
		ArrayList<DtoConsolidadoCierreReporte> listaAgrupado = new ArrayList<DtoConsolidadoCierreReporte>();
		
		for (String stringKey : listaKeys){
			listaAgrupado.add(mapaAgrupado.get(stringKey));
		}
		
		return listaAgrupado;
	}
	
	
	
	/**
	 * Agrupa para mostrar un solo consolidado por consecutivo de traslado.
	 * 
	 * @param lista
	 * @param formasPago
	 * @return ArrayList<DtoConsolidadoCierreReporte>
	 *
	 * @autor Cristhian Murillo
	*/
	private ArrayList<DtoConsolidadoCierreReporte> agruparTotalSumadoConsecutivoTraslado (ArrayList<DtoConsolidadoCierreReporte> lista, List<FormasPago> formasPago)
	{
		HashMap<String, DtoConsolidadoCierreReporte> mapaAgrupado 	= new HashMap<String, DtoConsolidadoCierreReporte>();
		ArrayList<String> listaKeys 								= new ArrayList<String>();
		
		for (DtoConsolidadoCierreReporte dtoConsolidadoCierreReporte : lista) 
		{
			DtoConsolidadoCierreReporte elemento;
			elemento = new DtoConsolidadoCierreReporte();
			
			String key = "";
			/*
			key += dtoConsolidadoCierreReporte.getCentroAtencion()+"_";
			key += dtoConsolidadoCierreReporte.getEmpresaInstitucion()+"_";
			key += dtoConsolidadoCierreReporte.getHoraApertura()+"_";
			key += dtoConsolidadoCierreReporte.getCajero()+"_"; 
			key += dtoConsolidadoCierreReporte.getCaja()+"_";
			*/
			key += dtoConsolidadoCierreReporte.getConsecutivoTraslado()+"_";
			
			if(!mapaAgrupado.containsKey(key))
			{
				elemento = dtoConsolidadoCierreReporte;
				listaKeys.add(key);
				mapaAgrupado.put(key, elemento);
			}
			else{
				elemento = mapaAgrupado.get(key);
				
				for (int i = 0; i < formasPago.size(); i++)	{
					elemento.getTotalesPorFormaPago().set(i, dtoConsolidadoCierreReporte.getTotalesPorFormaPago().get(i).add(elemento.getTotalesPorFormaPago().get(i)));
				}
				
				elemento.setTotalCentroAtencion(elemento.getTotalCentroAtencion().add(dtoConsolidadoCierreReporte.getTotalCentroAtencion()));
			}
		}
		
		ArrayList<DtoConsolidadoCierreReporte> listaAgrupado = new ArrayList<DtoConsolidadoCierreReporte>();
		
		for (String stringKey : listaKeys){
			listaAgrupado.add(mapaAgrupado.get(stringKey));
		}
		
		return listaAgrupado;
	}
	
	/**
	 * Metodo que calcula los cierres centro de atencion
	 * @param fechaSeleccionada
	 * @param CentroAtencion
	 * @param empresaInstitucion
	 * @param formasPago
	 * @return lista con los cierres centro de atencion
	 */
	public ArrayList<DtoConsolidadoCierreReporte> consultarconsolidadoCierre(Date fechaSeleccionada, String CentroAtencion,String empresaInstitucion,List<FormasPago> formasPago){
		//se consulta por cada tipo de cierre los movimientos caja cajeros asociados a los filtros seleccionados
		
		ArrayList<DtoConsolidadoCierreXCentroAtencion> translados= consultarMovimientosCierreCajaTranslados(fechaSeleccionada, CentroAtencion, empresaInstitucion);
		ArrayList<DtoConsolidadoCierreXCentroAtencion> solicitudes=  consultarMovimientosCierreCajaSolicitudes(fechaSeleccionada, CentroAtencion, empresaInstitucion);
		ArrayList<DtoConsolidadoCierreXCentroAtencion> faltantesSobrantes= consultarMovimientosCierreCajaFaltantesSobrantes(fechaSeleccionada, CentroAtencion, empresaInstitucion);
		ArrayList<DtoConsolidadoCierreXCentroAtencion> recibosCaja = consultarMovimientosCierreRecibosCaja(fechaSeleccionada, CentroAtencion, empresaInstitucion);
		ArrayList<DtoConsolidadoCierreXCentroAtencion> devoluciones = consultarMovimientosCierreDevolucionRecibosCaja(fechaSeleccionada, CentroAtencion, empresaInstitucion);
		ArrayList<DtoConsolidadoCierreXCentroAtencion> entregasParciales =consultarMovimientosCierreEntregasParciales(fechaSeleccionada, CentroAtencion, empresaInstitucion);
		
		// por cada tipo de movimiento se totaliza por forma de pago 
		ArrayList<DtoConsolidadoCierreReporte> transladoReporte=totalizar(translados,ConstantesBD.translados,formasPago);
		ArrayList<DtoConsolidadoCierreReporte> solicitudesReporte= totalizar(solicitudes,ConstantesBD.solicitudes,formasPago);
		ArrayList<DtoConsolidadoCierreReporte> faltantesSobrantesReporte= totalizar(faltantesSobrantes,ConstantesBD.faltantesSobrantes,formasPago);
		ArrayList<DtoConsolidadoCierreReporte> recibosCajaReporte= totalizar(recibosCaja,ConstantesBD.recibosCaja,formasPago);
		ArrayList<DtoConsolidadoCierreReporte> devolucionesReporte= totalizar(devoluciones,ConstantesBD.devolucionreciboscaja,formasPago);
		ArrayList<DtoConsolidadoCierreReporte> entregasParcialesReporte= totalizar(entregasParciales,ConstantesBD.entregasparciales,formasPago);
		
		// se adicionan todos los totalizados en una sola lista para generar el consolidado
		ArrayList<DtoConsolidadoCierreReporte> dtoCalculados=new ArrayList<DtoConsolidadoCierreReporte>();
		dtoCalculados.addAll(transladoReporte);
		dtoCalculados.addAll(solicitudesReporte);
		dtoCalculados.addAll(faltantesSobrantesReporte);
		dtoCalculados.addAll(recibosCajaReporte);
		dtoCalculados.addAll(devolucionesReporte);
		dtoCalculados.addAll(entregasParcialesReporte);
		
		//se totaliza por tipo de cierre el gran consolidado
		ArrayList<DtoConsolidadoCierreReporte> totales =calculoPortipomedioPago(dtoCalculados,formasPago);
		
		
		return totales;
		
	}
	
	
	/**
	 * Metodo que calcula los cierres centro de atencion
	 * @param parametros
	 * @author Cristhian Murillo
	 * 
	 * @return lista con los cierres centro de atencion
	 */
	public ArrayList<DtoConsolidadoCierreReporte> consultaTrasladoCajaMayorPrincipal(DtoConsolidadoCierreReporte parametros)
	{
		Integer cajaEntregaCajaMayor = null;
		if(!UtilidadTexto.isEmpty(parametros.getCaja())){
			cajaEntregaCajaMayor = Integer.parseInt(parametros.getCaja());
		}
		
		/* se consulta por cada tipo de cierre el movimiento caja entrega caja mayor asociados a los filtros seleccionados */
		ArrayList<DtoConsolidadoCierreXCentroAtencion> recibosCaja	= consultarMovimientosCierreRecibosCajaCajaCajeroEntregaCajaPrincipal(parametros.getFechaSeleccionada(), parametros.getCentroAtencion(), parametros.getEmpresaInstitucion(), cajaEntregaCajaMayor, null);
		
		/* Solo retorna las que NO tengan asociadas traslado a caja mayor */
		ArrayList<DtoConsolidadoCierreXCentroAtencion> listaRecibosCajaSinTraslado = new ArrayList<DtoConsolidadoCierreXCentroAtencion>();
		for (DtoConsolidadoCierreXCentroAtencion dtoConsolidadoCierreXCentroAtencion : recibosCaja) 
		{
			if(dtoConsolidadoCierreXCentroAtencion.getTrasladoCajaPrincipalMayor() == null){
				listaRecibosCajaSinTraslado.add(dtoConsolidadoCierreXCentroAtencion);
			}
		}
		
		// por cada tipo de movimiento se totaliza por forma de pago 
		ArrayList<DtoConsolidadoCierreReporte> recibosCajaReporte 	= totalizarCajaCajero(listaRecibosCajaSinTraslado, ConstantesBD.recibosCaja, parametros.getFormasPago());
		
		// se adicionan todos los totalizados en una sola lista para generar el consolidado
		ArrayList<DtoConsolidadoCierreReporte> dtoCalculados=new ArrayList<DtoConsolidadoCierreReporte>();
		dtoCalculados.addAll(recibosCajaReporte);
		
		/* se totaliza por tipo de cierre el gran consolidado */
		ArrayList<DtoConsolidadoCierreReporte> totales = calculoPortipomedioPagoCajaCajero(dtoCalculados,parametros.getFormasPago());

		totales = agruparTotalSumado(totales, parametros.getFormasPago());
		
		return totales;
	}
	

	
	
	/**
	 * metodo que totalzia segun los tipos de cierre
	 * @param dtoCalculados
	 * @param formasPago
	 * @return lista con valores consoldiados 
	 */
	public ArrayList<DtoConsolidadoCierreReporte> calculoPortipomedioPago(
			ArrayList<DtoConsolidadoCierreReporte> dtoCalculados,List<FormasPago> formasPago){
		ArrayList<DtoConsolidadoCierreReporte> tmpDtoCalculadosInstitucion = new ArrayList<DtoConsolidadoCierreReporte>();
		ArrayList<DtoConsolidadoCierreReporte> tmpDtoCalculadosCentroAtencion = new ArrayList<DtoConsolidadoCierreReporte>();
		ArrayList<DtoConsolidadoCierreReporte> totalesReporte= new ArrayList<DtoConsolidadoCierreReporte>();
		tmpDtoCalculadosCentroAtencion.addAll(dtoCalculados);
		tmpDtoCalculadosInstitucion.addAll(dtoCalculados);
		String institucionPivot="";
		String centroAtencionPivot="";
		BigDecimal totalizador =new BigDecimal(0);
		ArrayList<BigDecimal> totales= new ArrayList<BigDecimal>();
		BigDecimal totalCentroAtencion= new BigDecimal(0);

//se recorren las instituciones
		for (int i = 0; i < dtoCalculados.size(); i++) {
			institucionPivot=dtoCalculados.get(i).getIntitucion();
			if (analizarInstitucionDto(tmpDtoCalculadosInstitucion, institucionPivot)) {
				for (int j = 0; j < dtoCalculados.size(); j++) {
					
					//se recorren los centros de atencion 
					centroAtencionPivot= dtoCalculados.get(j).getCentroAtencion();
					if (analizarCentroAtencionDto(tmpDtoCalculadosCentroAtencion, centroAtencionPivot)) {
						totales= new ArrayList<BigDecimal>();
						
						// se recorren las formas pago para calcular
						for (int j4 = 0; j4 < formasPago.size(); j4++) {
							totalizador=new BigDecimal(0);
							for (int j2 = 0; j2 < dtoCalculados.size(); j2++) {
								//si el tipo de cierre es tralados, recibos caja faltantes sobrantes se suma sino se restan
								if (dtoCalculados.get(j2).getIntitucion().equals(institucionPivot)&&
										dtoCalculados.get(j2).getCentroAtencion().equals(centroAtencionPivot)) {
									if ((dtoCalculados.get(j2).getTipoDeMovimiento().equals(ConstantesBD.translados))||
											(dtoCalculados.get(j2).getTipoDeMovimiento().equals(ConstantesBD.recibosCaja)) ||
											(dtoCalculados.get(j2).getTipoDeMovimiento().equals(ConstantesBD.faltantesSobrantes))){
										totalizador=totalizador.add(dtoCalculados.get(j2).getTotalesPorFormaPago().get(j4));
									}else{
										totalizador=totalizador.subtract(dtoCalculados.get(j2).getTotalesPorFormaPago().get(j4));
									}
								}
							}
							totales.add(totalizador);
						}
						
						//se crea el dto con los consolidados
						DtoConsolidadoCierreReporte registroReporteTotalizado= new DtoConsolidadoCierreReporte();
						registroReporteTotalizado.setIntitucion(institucionPivot);
						registroReporteTotalizado.setCentroAtencion(centroAtencionPivot);
						registroReporteTotalizado.setTotalesPorFormaPago(totales);
						totalCentroAtencion=calculoTotalCentroAtencion(totales);
						registroReporteTotalizado.setTotalCentroAtencion(totalCentroAtencion);
						totalesReporte.add(registroReporteTotalizado);
						totalCentroAtencion= new BigDecimal(0);
						//se elimina el centro de atencion para no volver a sumar estos valores 
						tmpDtoCalculadosCentroAtencion=eliminarCentroAtencionTotalTmp(tmpDtoCalculadosCentroAtencion, centroAtencionPivot);
					}
				}
				//se elimina la institucion para no volverla a tener en cuenta al momneto de totalizar 
				tmpDtoCalculadosInstitucion=eliminarInstitucionTotalTmp(tmpDtoCalculadosInstitucion, institucionPivot);
				tmpDtoCalculadosCentroAtencion.clear();
				tmpDtoCalculadosCentroAtencion.addAll(dtoCalculados);
			}
		}
		return totalesReporte;
	}

	
	
	/**
	 * calcular el total del centro de atencion 
	 * @param totales
	 * @return total de centro atencion
	 */
	public BigDecimal calculoTotalCentroAtencion(ArrayList<BigDecimal> totales){
		BigDecimal totalCentroAtencion= new BigDecimal(0);
		for (BigDecimal bigDecimal : totales) {
			totalCentroAtencion=totalCentroAtencion.add(bigDecimal);
		}
		return  totalCentroAtencion;
	}
	
	
	
	/**
	 * Se encarga de calcular los valores por forma de pago  
	 * @param lista
	 * @param tipoMovimiento
	 * @param formasPago
	 * @return lista con totoales por cierre hecho 
	 */
	public ArrayList<DtoConsolidadoCierreReporte> totalizar(ArrayList<DtoConsolidadoCierreXCentroAtencion> lista ,String tipoMovimiento,List<FormasPago> formasPago){

		ArrayList<DtoConsolidadoCierreXCentroAtencion> listaTmp= new ArrayList<DtoConsolidadoCierreXCentroAtencion>();
		listaTmp.addAll(lista);
		ArrayList<DtoConsolidadoCierreXCentroAtencion> listaTmpCentroAtencion = new ArrayList<DtoConsolidadoCierreXCentroAtencion>();
		listaTmpCentroAtencion.addAll(lista);
		ArrayList<DtoConsolidadoCierreReporte> DtoReporte = new ArrayList<DtoConsolidadoCierreReporte>();
		String institucionPivot="";
		String centroAtencionPivot="";
		String formaPagoPivot="";
		BigDecimal totalizador= new BigDecimal(0);
		ArrayList<BigDecimal> totalesPorFormaPago= new ArrayList<BigDecimal>();
		BigDecimal valor= new BigDecimal(0);

		// se recorre la lista para obtener instituciones
		for (int i = 0; i < lista.size(); i++) {
			institucionPivot=lista.get(i).getInsitucion();
			if (analizarInstitucion(listaTmp, institucionPivot)) {
				//por cada inastitucion se recorre los centro de atencion 
				for (int j = 0; j < lista.size(); j++) {
					centroAtencionPivot= lista.get(j).getCentroAtencion();
					if (analizarCentroAtencion(listaTmpCentroAtencion, centroAtencionPivot)) {
						totalesPorFormaPago= new ArrayList<BigDecimal>();
						//por cada forma de pago se recorre y se totaliza 
						for (int j4 = 0; j4 < formasPago.size(); j4++) {
							formaPagoPivot=formasPago.get(j4).getDescripcion();
							totalizador= new BigDecimal(0);
							for (int j2 = 0; j2 < lista.size(); j2++) {
								if (lista.get(j2).getInsitucion().equals(institucionPivot)&&lista.get(j2).getCentroAtencion().equals(centroAtencionPivot)) {
									valor = lista.get(j2).getValor();
									if (lista.get(j2).getInsitucion().equals(institucionPivot)&& lista.get(j2).getCentroAtencion().equals(centroAtencionPivot)) {
										if (formaPagoPivot.equals(lista.get(j2).getFormaPagoDescripcion())) {
											totalizador=totalizador.add(valor);
										}
									}
								}
							}
							totalesPorFormaPago.add(totalizador);
						}
						//se crea instacia de dto con totalziados 
						DtoConsolidadoCierreReporte filaReporte = new DtoConsolidadoCierreReporte();
						filaReporte.setCentroAtencion(centroAtencionPivot);
						filaReporte.setIntitucion(institucionPivot);
						filaReporte.setTotalesPorFormaPago(totalesPorFormaPago);
						filaReporte.setTipoDeMovimiento(tipoMovimiento);
						DtoReporte.add(filaReporte);
						valor= new BigDecimal(0);
						//se elimina el centro de atencion para no volver a sumar estos valores 
						listaTmpCentroAtencion=eliminarCentroAtencion(listaTmpCentroAtencion, centroAtencionPivot);
					}
				}
				//se elimina la institucion para no volver a sumar estos valores 
				listaTmp=eliminarInstucionCalculada(listaTmp, institucionPivot);
				listaTmpCentroAtencion.clear();
				listaTmpCentroAtencion.addAll(lista);
			}
		}
		
		return DtoReporte;
	}

	/**
	 * Metodo que totaliza por formas de pago en consulta de caja cajero 
	 * @param lista
	 * @param tipoMovimiento
	 * @param formasPago
	 * @return lista con totalizados por forma de pago 
	 */
	public ArrayList<DtoConsolidadoCierreReporte> totalizarCajaCajero(ArrayList<DtoConsolidadoCierreXCentroAtencion> lista ,String tipoMovimiento,List<FormasPago> formasPago){

		ArrayList<DtoConsolidadoCierreXCentroAtencion> listaTmp	= new ArrayList<DtoConsolidadoCierreXCentroAtencion>();
		listaTmp.addAll(lista);
		ArrayList<DtoConsolidadoCierreXCentroAtencion> listaTmpCentroAtencion = new ArrayList<DtoConsolidadoCierreXCentroAtencion>();
		listaTmpCentroAtencion.addAll(lista);
		ArrayList<DtoConsolidadoCierreXCentroAtencion> listaTmpLogin = new ArrayList<DtoConsolidadoCierreXCentroAtencion>();
		listaTmpLogin.addAll(lista);
		ArrayList<DtoConsolidadoCierreReporte> DtoReporte = new ArrayList<DtoConsolidadoCierreReporte>();
		ArrayList<String> listaHorasAperturaLogin= new ArrayList<String>();
		
		String formaPagoPivot			="";
		BigDecimal totalizador			= new BigDecimal(0);
		String institucionPivot			="";
		String centroAtencionPivot		="";
		String loginPivot				="";
		String nombreCajero				="";
		String horaCajero				="";
		String horaApertura				="";
		String caja						="";
		String horaAperturaPivot		="";
		Long consecutivoMovimiento		= null;
		Long movimientoCaja				= null;
		Long trasladoCajaPrincipalMayor = null;
		Long consecutivoTraslado		= null;
		String cajaInicialDescripcion	= null;
		Integer cajaInicialCodigo		= null;
		String cajaFinalDescripcion		= null;
		Integer cajaFinalCodigo			= null;
		Date fechaTraslado				= null;
		String horaTraslado				= null;
		String primerNombreTraslado		= null;
		String segundonombreTraslado	= null;
		String primerApellidoTraslado	= null;
		String segundoApellidoTraslado	= null;
		String loginTraslado			= null;
		Date fecha						= null;
		
		
		ArrayList<BigDecimal> totalesPorFormaPago= new ArrayList<BigDecimal>();
		BigDecimal valor= new BigDecimal(0);

		// se recorre la lista para obtener instituciones
		for (int i = 0; i < lista.size(); i++) {
			institucionPivot=lista.get(i).getInsitucion();
			if (analizarInstitucion(listaTmp, institucionPivot)) {
				//por cada inastitucion se recorre los centro de atencion 
				for (int j = 0; j < lista.size(); j++) {
					centroAtencionPivot= lista.get(j).getCentroAtencion();
					if (analizarCentroAtencion(listaTmpCentroAtencion, centroAtencionPivot)) {
						for (int j3 = 0; j3 < lista.size(); j3++) {
							loginPivot=lista.get(j3).getLogin();
							if (analizarLoginCajeroDto(listaTmpLogin, loginPivot)) {
								listaHorasAperturaLogin=new ArrayList<String>();
								//por cada forma de pago se recorre y se totaliza 
								for (int k = 0; k < lista.size(); k++) {
									horaAperturaPivot=lista.get(k).getHoraApertura();
									if (!listaHorasAperturaLogin.contains(loginPivot+horaAperturaPivot)) {
										totalesPorFormaPago= new ArrayList<BigDecimal>();
										for (int j4 = 0; j4 < formasPago.size(); j4++) {
											formaPagoPivot=formasPago.get(j4).getDescripcion();
											totalizador= new BigDecimal(0);
											//tambien se obtiene la caja el cajero y el rango de funcionamiento de la caja 
											for (int j2 = 0; j2 < lista.size(); j2++) {
												if (lista.get(j2).getInsitucion().equals(institucionPivot)&&lista.get(j2).getCentroAtencion().equals(centroAtencionPivot)
														&& lista.get(j2).getLogin().equals(loginPivot)&&lista.get(j2).getHoraApertura().equals(horaAperturaPivot)) {
	
													nombreCajero=lista.get(j2).getPrimerNombre()+" "+validacionNull(lista.get(j2).getSegundonombre())+" "+
													lista.get(j2).getPrimerApellido()+" "+validacionNull(lista.get(j2).getSegundoApellido())+" -  "+loginPivot;
	
													horaCajero=lista.get(j2).getHoraApertura()+" - "+lista.get(j2).getHora();
													horaApertura=lista.get(j2).getHoraApertura();
													caja="("+lista.get(j2).getCajaCodigo()+") "+lista.get(j2).getCajaDescripcion();
													valor = lista.get(j2).getValor();
													
													consecutivoMovimiento 	= lista.get(j2).getConsecutivo();
													movimientoCaja 			= lista.get(j2).getMovimientoCaja();
													trasladoCajaPrincipalMayor = lista.get(j2).getTrasladoCajaPrincipalMayor();
													consecutivoTraslado 	= lista.get(j2).getConsecutivoTraslado();
													cajaInicialDescripcion	= lista.get(j2).getCajaInicialDescripcion();
													cajaInicialCodigo		= lista.get(j2).getCajaInicialCodigo();
													cajaFinalDescripcion	= lista.get(j2).getCajaFinalDescripcion();
													cajaFinalCodigo			= lista.get(j2).getCajaFinalCodigo();
													
													fechaTraslado			= lista.get(j2).getFechaTraslado();
													horaTraslado			= lista.get(j2).getHoraTraslado();
													primerNombreTraslado	= lista.get(j2).getPrimerNombreTraslado();
													segundonombreTraslado	= lista.get(j2).getSegundonombreTraslado();
													primerApellidoTraslado	= lista.get(j2).getPrimerApellidoTraslado();
													segundoApellidoTraslado	= lista.get(j2).getSegundoApellidoTraslado();
													loginTraslado			= lista.get(j2).getLoginTraslado();
													fecha					= lista.get(j2).getFecha();
													
													if (formaPagoPivot.equals(lista.get(j2).getFormaPagoDescripcion())) {
														totalizador=totalizador.add(valor);
													}
												}
											}
											totalesPorFormaPago.add(totalizador);
										}
										//se crea instacia de dto con totalziados 
										listaHorasAperturaLogin.add(loginPivot+horaAperturaPivot);
										DtoConsolidadoCierreReporte filaReporte = new DtoConsolidadoCierreReporte();
										filaReporte.setCentroAtencion(centroAtencionPivot);
										filaReporte.setIntitucion(institucionPivot);
										filaReporte.setTotalesPorFormaPago(totalesPorFormaPago);
										filaReporte.setTipoDeMovimiento(tipoMovimiento);
										filaReporte.setLogin(loginPivot);
										filaReporte.setCajero(nombreCajero);
										filaReporte.setHoraApertura(horaApertura);
										filaReporte.setHoraTurnoCajero(horaCajero);
										filaReporte.setCaja(caja);
										filaReporte.setConsecutivo(consecutivoMovimiento);
										filaReporte.setMovimientoCaja(movimientoCaja);
										filaReporte.setTrasladoCajaPrincipalMayor(trasladoCajaPrincipalMayor);
										filaReporte.setConsecutivoTraslado(consecutivoTraslado);
										
										filaReporte.setCajaInicialDescripcion(cajaInicialDescripcion);
										filaReporte.setCajaInicialCodigo(cajaInicialCodigo);
										filaReporte.setCajaFinalDescripcion(cajaFinalDescripcion);
										filaReporte.setCajaFinalCodigo(cajaFinalCodigo);
										filaReporte.setFechaTraslado(fechaTraslado);
										filaReporte.setHoraTraslado(horaTraslado);
										filaReporte.setPrimerNombreTraslado(primerNombreTraslado);
										filaReporte.setSegundonombreTraslado(segundonombreTraslado);
										filaReporte.setPrimerApellidoTraslado(primerApellidoTraslado);
										filaReporte.setSegundoApellidoTraslado(segundoApellidoTraslado);
										filaReporte.setLoginTraslado(loginTraslado);
										filaReporte.setFecha(fecha);
										
										
										if (!nombreCajero.equals("")) {
											DtoReporte.add(filaReporte);
										}
										
										valor 						= new BigDecimal(0);
										nombreCajero				= "";
										consecutivoMovimiento 		= null;
										movimientoCaja 				= null;
										trasladoCajaPrincipalMayor 	= null;
										consecutivoTraslado 		= null;
										cajaInicialDescripcion		= null;
										cajaInicialCodigo			= null;
										cajaFinalDescripcion		= null;
										cajaFinalCodigo				= null;
										fechaTraslado				= null;
										horaTraslado				= null;
										primerNombreTraslado		= null;
										segundonombreTraslado		= null;
										primerApellidoTraslado		= null;
										segundoApellidoTraslado		= null;
										loginTraslado				= null;
										fecha 						= null;
									}
								}	
								//se elimina el centro de atencion para no volver a sumar estos valores
								listaTmpLogin=eliminarLogin(listaTmpLogin, loginPivot);
							}

						}
						//se elimina la institucion para no volver a sumar estos valores 
						listaTmpCentroAtencion=eliminarCentroAtencion(listaTmpCentroAtencion, centroAtencionPivot);
					}

					listaTmpLogin.clear();
					listaTmpLogin.addAll(lista);
				}
				listaTmp=eliminarInstucionCalculada(listaTmp, institucionPivot);
				listaTmpCentroAtencion.clear();
				listaTmpCentroAtencion.addAll(lista);
				listaTmpLogin.clear();
				listaTmpLogin.addAll(lista);
			}
		}

		return DtoReporte;
	}


	
	
	/**
	 * Metodo qeu calcula totales por tipo medio de pago 
	 * @param lista
	 * @param formasPago
	 * @return lista con el calculo por tipo medio de pago 
	 */
	public ArrayList<DtoConsolidadoCierreReporte> calculoPortipomedioPagoCajaCajero(ArrayList<DtoConsolidadoCierreReporte> lista,List<FormasPago> formasPago ){

		ArrayList<DtoConsolidadoCierreReporte> listaTmp= new ArrayList<DtoConsolidadoCierreReporte>();
		listaTmp.addAll(lista);
		ArrayList<DtoConsolidadoCierreReporte> listaTmpCentroAtencion = new ArrayList<DtoConsolidadoCierreReporte>();
		listaTmpCentroAtencion.addAll(lista);
		ArrayList<DtoConsolidadoCierreReporte> listaTmpLogin = new ArrayList<DtoConsolidadoCierreReporte>();
		listaTmpLogin.addAll(lista);

		@SuppressWarnings("unused")
		ArrayList<DtoConsolidadoCierreReporte> DtoReporte = new ArrayList<DtoConsolidadoCierreReporte>();
		ArrayList<DtoConsolidadoCierreReporte> totalesReporte= new ArrayList<DtoConsolidadoCierreReporte>();
		ArrayList<String> listaHorasAperturaLogin= new ArrayList<String>();
		@SuppressWarnings("unused")
		String formaPagoPivot="";
		BigDecimal totalizador= new BigDecimal(0);


		String institucionPivot			= "";
		String centroAtencionPivot		= "";
		String loginPivot				= "";
		String caja						= "";
		String cajero					= "";
		String turnoCajero				= "";
		String horaApertura				= "";
		String horaAperturaPivot		= "";
		Long conseutivoMovimiento 		= null;
		Long movimientoCaja 			= null;
		Long trasladoCajaPrincipalMayor = null;
		Long consecutivoTraslado		= null;
		String cajaInicialDescripcion	= null;
		Integer cajaInicialCodigo		= null;
		String cajaFinalDescripcion		= null;
		Integer cajaFinalCodigo			= null;
		Date fechaTraslado				= null;
		String horaTraslado				= null;
		String primerNombreTraslado		= null;
		String segundonombreTraslado	= null;
		String primerApellidoTraslado	= null;
		String segundoApellidoTraslado	= null;
		String loginTraslado			= null;
		Date fecha						= null;
		
		BigDecimal totalCentroAtencion= new BigDecimal(0);
		ArrayList<BigDecimal> totalesPorFormaPago= new ArrayList<BigDecimal>();


		// se recorre la lista para obtener instituciones
		for (int i = 0; i < lista.size(); i++) {
			institucionPivot=lista.get(i).getIntitucion();
			if (analizarInstitucionDto(listaTmp, institucionPivot)) {
				//por cada inastitucion se recorre los centro de atencion 
				for (int j = 0; j < lista.size(); j++) {
					centroAtencionPivot= lista.get(j).getCentroAtencion();
					if (analizarCentroAtencionDto(listaTmpCentroAtencion, centroAtencionPivot)) {
						for (int j3 = 0; j3 < lista.size(); j3++) {
							loginPivot=lista.get(j3).getLogin();
							if (analizarLoginCajero(listaTmpLogin, loginPivot)) {
								listaHorasAperturaLogin=new ArrayList<String>();
								for (int k = 0; k < lista.size(); k++) {
									horaAperturaPivot=lista.get(k).getHoraTurnoCajero();
									//por cada forma de pago se recorre y se totaliza 
									if (!listaHorasAperturaLogin.contains(loginPivot+horaAperturaPivot)) {
										totalesPorFormaPago= new ArrayList<BigDecimal>();
										for (int j4 = 0; j4 < formasPago.size(); j4++) {
											formaPagoPivot=formasPago.get(j4).getDescripcion();
											totalizador= new BigDecimal(0);
											//tambien se obtiene la caja el cajero y el rango de funcionamiento de la caja
											for (int j2 = 0; j2 < lista.size(); j2++) {
												if (lista.get(j2).getIntitucion()  .equals(institucionPivot)&&lista.get(j2).getCentroAtencion().equals(centroAtencionPivot)
														&& lista.get(j2).getLogin().equals(loginPivot) && lista.get(j2).getHoraTurnoCajero().equals(horaAperturaPivot) ) {

													caja=lista.get(j2).getCaja();
													cajero=lista.get(j2).getCajero();
													turnoCajero=lista.get(j2).getHoraTurnoCajero();
													horaApertura=lista.get(j2).getHoraApertura();
													
													conseutivoMovimiento 	= lista.get(j2).getConsecutivo();
													movimientoCaja 			= lista.get(j2).getMovimientoCaja();
													trasladoCajaPrincipalMayor = lista.get(j2).getTrasladoCajaPrincipalMayor();
													consecutivoTraslado 	= lista.get(j2).getConsecutivoTraslado();
													cajaInicialDescripcion	= lista.get(j2).getCajaInicialDescripcion();
													cajaInicialCodigo		= lista.get(j2).getCajaInicialCodigo();
													cajaFinalDescripcion	= lista.get(j2).getCajaFinalDescripcion();
													cajaFinalCodigo			= lista.get(j2).getCajaFinalCodigo();
													fechaTraslado			= lista.get(j2).getFechaTraslado();
													horaTraslado			= lista.get(j2).getHoraTraslado();
													primerNombreTraslado	= lista.get(j2).getPrimerNombreTraslado();
													segundonombreTraslado	= lista.get(j2).getSegundonombreTraslado();
													primerApellidoTraslado	= lista.get(j2).getPrimerApellidoTraslado();
													segundoApellidoTraslado	= lista.get(j2).getSegundoApellidoTraslado();
													loginTraslado			= lista.get(j2).getLoginTraslado();
													fecha					= lista.get(j2).getFecha();
													
													if ((lista.get(j2).getTipoDeMovimiento().equals(ConstantesBD.translados))||
															(lista.get(j2).getTipoDeMovimiento().equals(ConstantesBD.recibosCaja)) ||
															(lista.get(j2).getTipoDeMovimiento().equals(ConstantesBD.faltantesSobrantes))){
														totalizador=totalizador.add(lista.get(j2).getTotalesPorFormaPago().get(j4));

													}else{
														totalizador=totalizador.subtract(lista.get(j2).getTotalesPorFormaPago().get(j4));											
													}
												}
											}
											totalesPorFormaPago.add(totalizador);
										}
										//se crea instacia de dto con totalziados
										listaHorasAperturaLogin.add(loginPivot+horaAperturaPivot);
										DtoConsolidadoCierreReporte registroReporteTotalizado= new DtoConsolidadoCierreReporte();
										
										registroReporteTotalizado.setIntitucion(institucionPivot);
										registroReporteTotalizado.setCentroAtencion(centroAtencionPivot);
										totalCentroAtencion=calculoTotalCentroAtencion(totalesPorFormaPago);
										
										registroReporteTotalizado.setTotalCentroAtencion(totalCentroAtencion);
										registroReporteTotalizado.setCaja(caja);
										registroReporteTotalizado.setCajero(cajero);
										registroReporteTotalizado.setHoraApertura(horaApertura);
										registroReporteTotalizado.setHoraTurnoCajero(turnoCajero);
										registroReporteTotalizado.setTotalesPorFormaPago(totalesPorFormaPago);
										registroReporteTotalizado.setConsecutivo(conseutivoMovimiento);
										registroReporteTotalizado.setMovimientoCaja(movimientoCaja);
										registroReporteTotalizado.setTrasladoCajaPrincipalMayor(trasladoCajaPrincipalMayor);
										registroReporteTotalizado.setConsecutivoTraslado(consecutivoTraslado);
										registroReporteTotalizado.setCajaInicialDescripcion(cajaInicialDescripcion);
										registroReporteTotalizado.setCajaInicialCodigo(cajaInicialCodigo);
										registroReporteTotalizado.setCajaFinalDescripcion(cajaFinalDescripcion);
										registroReporteTotalizado.setCajaFinalCodigo(cajaFinalCodigo);
										registroReporteTotalizado.setFechaTraslado(fechaTraslado);
										registroReporteTotalizado.setHoraTraslado(horaTraslado);
										registroReporteTotalizado.setPrimerNombreTraslado(primerNombreTraslado);
										registroReporteTotalizado.setSegundonombreTraslado(segundonombreTraslado);
										registroReporteTotalizado.setPrimerApellidoTraslado(primerApellidoTraslado);
										registroReporteTotalizado.setSegundoApellidoTraslado(segundoApellidoTraslado);
										registroReporteTotalizado.setLoginTraslado(loginTraslado);
										registroReporteTotalizado.setFecha(fecha);
										
										if (!cajero.equals("")) {
											totalesReporte.add(registroReporteTotalizado);
										}
										
										totalCentroAtencion			= new BigDecimal(0);
										cajero						= "";
										conseutivoMovimiento 		= null;
										movimientoCaja 				= null;
										trasladoCajaPrincipalMayor 	= null;
										consecutivoTraslado 		= null;
										cajaInicialDescripcion		= null;
										cajaInicialCodigo			= null;
										cajaFinalDescripcion		= null;
										cajaFinalCodigo				= null;
										fechaTraslado				= null;
										horaTraslado				= null;
										primerNombreTraslado		= null;
										segundonombreTraslado		= null;
										primerApellidoTraslado		= null;
										segundoApellidoTraslado		= null;
										loginTraslado				= null;
										fecha						= null;
									}
								}

								//se elimina el centro de atencion para no volver a sumar estos valores
								listaTmpLogin=eliminarInstitucionLoginDto(listaTmpLogin, loginPivot);
							}
						}
						//se elimina la institucion para no volver a sumar estos valores 
						listaTmpCentroAtencion=eliminarCentroAtencionTotalTmp(listaTmpCentroAtencion, centroAtencionPivot);
					}
					listaTmpLogin.clear();
					listaTmpLogin.addAll(lista);
				}
				listaTmp=eliminarInstitucionTotalTmp(listaTmp, institucionPivot);
				listaTmpCentroAtencion.clear();
				listaTmpCentroAtencion.addAll(lista);
				listaTmpLogin.clear();
				listaTmpLogin.addAll(lista);
			}
		}

		return totalesReporte;
	}

	
	
	/**
	 * Metodo qeu evalua nulls
	 * @param cadena
	 * @return si es es null retorna vacio
	 */
	public String validacionNull(String cadena){
		if (cadena==null) {
			return "";
		}else{
			return  cadena;
		}
	}
	
	
	/**
	 * metodo qeu calcula si  la lista contiene o no el login 
	 * @param lista
	 * @param login
	 * @return si la lista contiene o no el login 
	 */
	public Boolean analizarLoginCajeroDto(ArrayList<DtoConsolidadoCierreXCentroAtencion> lista , String login){
		Boolean esta = false;
		for (int i = 0; i < lista.size(); i++) {
			if (lista.get(i).getLogin().equals(login)) {
				esta = true;
			}
		}
		return esta;
	}
	
	
	
	
	
	
	/**
	 * @param lista
	 * @param login
	 * @return si la lista contiene el login 
	 */
	public Boolean analizarLoginCajero(ArrayList<DtoConsolidadoCierreReporte> lista , String login){
		Boolean esta = false;
		for (int i = 0; i < lista.size(); i++) {
			if (lista.get(i).getLogin().equals(login)) {
				esta = true;
			}
		}
		return esta;
	}
	
	
	
	/**
	 * @param lista
	 * @param centroAtencion
	 * @return si lal ista contiene el centro de atencion 
	 */ 
	public Boolean analizarCentroAtencionDto(ArrayList<DtoConsolidadoCierreReporte> lista , String centroAtencion){
		Boolean esta = false;
		for (int i = 0; i < lista.size(); i++) {
			if (lista.get(i).getCentroAtencion().equals(centroAtencion)) {
				esta = true;
			}
		}
		return esta;
	}
	
	/**
	 * @param lista
	 * @param institucion
	 * @return si la lista contiene la institucion
	 */
	public Boolean analizarInstitucionDto(ArrayList<DtoConsolidadoCierreReporte> lista , String institucion){
		Boolean esta = false;
		for (int i = 0; i < lista.size(); i++) {
			if (lista.get(i).getIntitucion() .equals(institucion)) {
				esta = true;
			}
		}
		return esta;
	}
	
	/**
	 * Elimina el centro de atencion 
	 * @param lista
	 * @param centroAtencion
	 * @return la lista sin el centro de atencion 
	 */
	public ArrayList<DtoConsolidadoCierreReporte> eliminarCentroAtencionTotalTmp(ArrayList<DtoConsolidadoCierreReporte>lista, String centroAtencion){
		ArrayList<DtoConsolidadoCierreReporte> listaTmp = new ArrayList<DtoConsolidadoCierreReporte>();
		listaTmp.addAll(lista);
		for (int i = 0; i < lista.size(); i++) {
			if (lista.get(i).getCentroAtencion().equals(centroAtencion)) {
				listaTmp.remove(lista.get(i));
			}
		}
		return listaTmp;
	}
	
	/**
	 * Elimina el login 
	 * @param lista
	 * @param login
	 * @return lista sin el login 
	 */
	public ArrayList<DtoConsolidadoCierreReporte> eliminarInstitucionLoginDto(ArrayList<DtoConsolidadoCierreReporte>lista, String login){
		ArrayList<DtoConsolidadoCierreReporte> listaTmp = new ArrayList<DtoConsolidadoCierreReporte>();
		listaTmp.addAll(lista);
		for (int i = 0; i < lista.size(); i++) {
			if (lista.get(i).getLogin().equals(login)) {
				listaTmp.remove(lista.get(i));
			}
		}
		return listaTmp;
	}
	
	/**
	 * Elimina la institucion
	 * @param lista
	 * @param institucion
	 * @return elimina la institucion de la lista 
	 */
	public ArrayList<DtoConsolidadoCierreReporte> eliminarInstitucionTotalTmp(ArrayList<DtoConsolidadoCierreReporte>lista, String institucion){
		ArrayList<DtoConsolidadoCierreReporte> listaTmp = new ArrayList<DtoConsolidadoCierreReporte>();
		listaTmp.addAll(lista);
		for (int i = 0; i < lista.size(); i++) {
			if (lista.get(i).getIntitucion().equals(institucion)) {
				listaTmp.remove(lista.get(i));
			}
		}
		return listaTmp;
	}

	
	/**
	 * @param lista
	 * @param centroAtencion
	 * @return si la lista contiene o no el centro de tencion 
	 */
	public Boolean analizarCentroAtencion(ArrayList<DtoConsolidadoCierreXCentroAtencion> lista , String centroAtencion){
		Boolean esta = false;
		for (int i = 0; i < lista.size(); i++) {
			if (lista.get(i).getCentroAtencion().equals(centroAtencion)) {
				esta = true;
			}
		}
		return esta;
	}
	
	/**
	 * @param lista
	 * @param institucion
	 * @return Si contiene la institucion
	 */
	public Boolean analizarInstitucion(ArrayList<DtoConsolidadoCierreXCentroAtencion> lista , String institucion){
		Boolean esta = false;
		for (int i = 0; i < lista.size(); i++) {
			if (lista.get(i).getInsitucion().equals(institucion)) {
				esta = true;
			}
		}
		return esta;
	}
	
	
	/**
	 * Elimina el login de la lista de dtos
	 * @param lista
	 * @param login
	 * @return lista sin el login 
	 */
	public ArrayList<DtoConsolidadoCierreXCentroAtencion> eliminarLogin(ArrayList<DtoConsolidadoCierreXCentroAtencion> lista, String login){
		ArrayList<DtoConsolidadoCierreXCentroAtencion> listaTmp = new ArrayList<DtoConsolidadoCierreXCentroAtencion>();
		listaTmp.addAll(lista);
		for (int i = 0; i < lista.size(); i++) {
			if (lista.get(i).getLogin().equals(login)) {
				listaTmp.remove(lista.get(i));
			}
		}
		return listaTmp;
	}
	
	
	
	/**
	 * @param lista
	 * @param centroAtencion
	 * @return lista sin el centro de atencion 
	 */
	public ArrayList<DtoConsolidadoCierreXCentroAtencion> eliminarCentroAtencion(ArrayList<DtoConsolidadoCierreXCentroAtencion> lista, String centroAtencion){
		ArrayList<DtoConsolidadoCierreXCentroAtencion> listaTmp = new ArrayList<DtoConsolidadoCierreXCentroAtencion>();
		listaTmp.addAll(lista);
		for (int i = 0; i < lista.size(); i++) {
			if (lista.get(i).getCentroAtencion().equals(centroAtencion)) {
				listaTmp.remove(lista.get(i));
			}
		}
		return listaTmp;
	}
	
	
	
	/**
	 * Elimina la institucion 
	 * 
	 * @param lista
	 * @param institucion
	 * @return lista sin la institucion 
	 */
	public ArrayList<DtoConsolidadoCierreXCentroAtencion> eliminarInstucionCalculada(ArrayList<DtoConsolidadoCierreXCentroAtencion> lista, String institucion){
		ArrayList<DtoConsolidadoCierreXCentroAtencion> listaTmp = new ArrayList<DtoConsolidadoCierreXCentroAtencion>();
		listaTmp.addAll(lista);
		for (int i = 0; i < lista.size(); i++) {
			if (lista.get(i).getInsitucion().equals(institucion)) {
				listaTmp.remove(lista.get(i));
			}
		}
		return listaTmp;
	}
	
	
	/**
	 * Metodo que consulta los traslados realizados a caja mayor
	 * @param parametros
	 * @author Cristhian Murillo
	 * 
	 * @return ArrayList<DtoConsolidadoCierreReporte>
	 */
	public ArrayList<DtoConsolidadoCierreReporte> consultaTrasladoHaciaCajaMayor(DtoConsolidadoCierreReporte parametros)
	{
		/* se consulta por cada tipo de cierre el movimiento caja entrega caja mayor asociados a los filtros seleccionados */
		ArrayList<DtoConsolidadoCierreXCentroAtencion> recibosCaja	= consultarMovimientosCierreRecibosCajaCajaCajeroTrasladoCajaMayor(parametros);
		
		// por cada tipo de movimiento se totaliza por forma de pago 
		ArrayList<DtoConsolidadoCierreReporte> recibosCajaReporte 	= totalizarCajaCajero(recibosCaja, ConstantesBD.recibosCaja, parametros.getFormasPago());
		
		// se adicionan todos los totalizados en una sola lista para generar el consolidado
		ArrayList<DtoConsolidadoCierreReporte> dtoCalculados=new ArrayList<DtoConsolidadoCierreReporte>();
		dtoCalculados.addAll(recibosCajaReporte);
		
		/* se totaliza por tipo de cierre el gran consolidado */
		ArrayList<DtoConsolidadoCierreReporte> totales = calculoPortipomedioPagoCajaCajero(dtoCalculados,parametros.getFormasPago());

		
		if(parametros.isAgruparSumadoPorConsecutivoTraslado()){
			totales = agruparTotalSumadoConsecutivoTraslado(totales, parametros.getFormasPago());
		}
		else{
			totales = agruparTotalSumado(totales, parametros.getFormasPago());
		}
		
		return totales;
	}
	
	
	/**
	 * Metodo que se encarga de listar los cierres con traslado a caja mayor
	 * @param parametros
	 * @author Cristhian Murillo
	 * 
	 * @return ArrayList<DtoConsolidadoCierreXCentroAtencion>
	 */
	public ArrayList<DtoConsolidadoCierreXCentroAtencion> consultarMovimientosCierreRecibosCajaCajaCajeroTrasladoCajaMayor(DtoConsolidadoCierreReporte parametros)
	{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(MovimientosCaja.class,"movimientoCaja")
		.createAlias("movimientoCaja.cierreCajaXReciboCajas"					, "cierreCajaXReciboCajas")
		.createAlias("cierreCajaXReciboCajas.recibosCaja"						, "recibosCaja")
		.createAlias("recibosCaja.detallePagosRcs"								, "detallePagosRcs")
		.createAlias("movimientoCaja.turnoDeCaja"								, "turnoDeCaja")
		.createAlias("turnoDeCaja.centroAtencion"								, "centroAtencion")
		.createAlias("detallePagosRcs.formasPago"								, "formasPago")
		.createAlias("centroAtencion.instituciones"								, "instituciones")
		.createAlias("turnoDeCaja.cajas"										, "cajas")
		.createAlias("turnoDeCaja.usuarios"										, "usuarios")
		.createAlias("usuarios.personas"										, "personas")
		.createAlias("movimientoCaja.entregaCajaMayorsForMovimientoCajaArqueo"	, "entregaCajaMayor"					, Criteria.LEFT_JOIN)
		.createAlias("entregaCajaMayor.movimientosCajaByCodigoPk"				, "entregaCajaMayorMovimientoCierrePk"	, Criteria.LEFT_JOIN) 
		.createAlias("entregaCajaMayor.cajas"									, "cajaEntregaCajaMayor"				, Criteria.LEFT_JOIN)
		.createAlias("entregaCajaMayor.trasladoCajaPrincipalMayor"				, "trasladoCajaPrincipalMayor"			, Criteria.INNER_JOIN) // (INNER_JOIN) Se traen solo los que tengan movimiento de traslado a caja mayor
		.createAlias("trasladoCajaPrincipalMayor.cajas"							, "cajaTrasladoPrincipalMayor"			, Criteria.LEFT_JOIN)
		.createAlias("trasladoCajaPrincipalMayor.usuarios"						, "usuarioTrasladoPrincipalMayor"		, Criteria.LEFT_JOIN)
		.createAlias("usuarioTrasladoPrincipalMayor.personas"					, "personaTrasladoPrincipalMayor"		, Criteria.LEFT_JOIN)
	;

	criteria.add(Restrictions.ne("recibosCaja.estadosRecibosCaja.codigo", ConstantesBD.codigoEstadoReciboCajaAnulado));
	
	//filtro de fecha inicio
	if (parametros.getFechaInicio() != null ) {
		criteria.add(Restrictions.ge("trasladoCajaPrincipalMayor.fecha", parametros.getFechaInicio()));
	}
	//filtro de fecha inicio
	if (parametros.getFechaFin() != null ) {
		criteria.add(Restrictions.le("trasladoCajaPrincipalMayor.fecha", parametros.getFechaFin()));
	}
	
	//filtro de centro atencion
	if (!UtilidadTexto.isEmpty(parametros.getCentroAtencion()) && !parametros.getCentroAtencion().equals(String.valueOf(ConstantesBD.codigoNuncaValido))) {
		criteria.add(Restrictions.eq("centroAtencion.consecutivo", Integer.parseInt(parametros.getCentroAtencion())));
	}
	
	//filtro de institucion
	if (!UtilidadTexto.isEmpty(parametros.getEmpresaInstitucion())&& !parametros.getEmpresaInstitucion().equals(String.valueOf(ConstantesBD.codigoNuncaValido))) {
		criteria.add(Restrictions.eq("instituciones.codigo",Integer.parseInt(parametros.getEmpresaInstitucion().trim())));
	}
	
	//filtro caja principal de EntregaCajaMayor
	if(!UtilidadTexto.isEmpty(parametros.getCaja()) && !parametros.getCaja().equals(ConstantesBD.codigoNuncaValido+"")){
		criteria.add(Restrictions.eq("cajaEntregaCajaMayor.consecutivo",Integer.parseInt( parametros.getCaja())));
	}
	//filtro caja mayor de TrasladoCajaPrincipalMayor
	if(!UtilidadTexto.isEmpty(parametros.getCaja2()) && !parametros.getCaja2().equals(ConstantesBD.codigoNuncaValido+"")){
		criteria.add(Restrictions.eq("cajaTrasladoPrincipalMayor.consecutivo", Integer.parseInt(parametros.getCaja2())));
	}
	
	//filtro consecutivo inicio traslado TrasladoCajaPrincipalMayor
	if(!UtilidadTexto.isEmpty(parametros.getNumeroInicioTraslado())){
		criteria.add(Restrictions.ge("trasladoCajaPrincipalMayor.consecutivo", Long.parseLong(parametros.getNumeroInicioTraslado())));
	}
	//filtro consecutivo fin traslado TrasladoCajaPrincipalMayor
	if(!UtilidadTexto.isEmpty(parametros.getNumeroFinTraslado())){
		criteria.add(Restrictions.le("trasladoCajaPrincipalMayor.consecutivo", Long.parseLong(parametros.getNumeroFinTraslado())));
	}
	
	//filtro consecutivo traslado TrasladoCajaPrincipalMayor
	if(parametros.getConsecutivoTraslado() != null){
		criteria.add(Restrictions.eq("trasladoCajaPrincipalMayor.consecutivo", parametros.getConsecutivoTraslado()));
	}
	
	//order by turno de caja apertura
	criteria.addOrder(Order.desc("turnoDeCaja.horaApertura"));
	
	//proyeccion a dto
	criteria.setProjection(Projections.projectionList()	
			.add(Projections.property("entregaCajaMayorMovimientoCierrePk.codigoPk")			,"movimientoCaja") 
			.add(Projections.property("movimientoCaja.consecutivo")								,"consecutivo") 
			.add(Projections.property("detallePagosRcs.valor")									,"valor")
			.add(Projections.property("formasPago.descripcion")									,"formaPagoDescripcion")
			.add(Projections.property("centroAtencion.descripcion")								,"centroAtencion")
			.add(Projections.property("instituciones.razonSocial")								,"Insitucion")
			.add(Projections.property("turnoDeCaja.horaApertura")								,"horaApertura")
			.add(Projections.property("movimientoCaja.hora")									,"hora")
			.add(Projections.property("movimientoCaja.fecha")									,"fecha")
			.add(Projections.property("personas.primerNombre")									,"primerNombre")
			.add(Projections.property("personas.segundoNombre")									,"segundonombre")
			.add(Projections.property("personas.primerApellido")								,"primerApellido")
			.add(Projections.property("personas.segundoApellido")								,"segundoApellido")
			.add(Projections.property("usuarios.login")											,"login")
			.add(Projections.property("cajas.codigo")											,"cajaCodigo")
			.add(Projections.property("cajas.descripcion")										,"cajaDescripcion")
			/* Información de entrega y traslados a caja*/
			.add(Projections.property("trasladoCajaPrincipalMayor.movimientoEntregaCajaMayor")	,"trasladoCajaPrincipalMayor")
			.add(Projections.property("trasladoCajaPrincipalMayor.consecutivo")					,"consecutivoTraslado")
			.add(Projections.property("cajaEntregaCajaMayor.descripcion")						,"cajaInicialDescripcion")
			.add(Projections.property("cajaEntregaCajaMayor.codigo")							,"cajaInicialCodigo")
			.add(Projections.property("cajaTrasladoPrincipalMayor.descripcion")					,"cajaFinalDescripcion")
			.add(Projections.property("cajaTrasladoPrincipalMayor.codigo")						,"cajaFinalCodigo")
			.add(Projections.property("trasladoCajaPrincipalMayor.fecha")						,"fechaTraslado")
			.add(Projections.property("trasladoCajaPrincipalMayor.hora")						,"horaTraslado")
			.add(Projections.property("personaTrasladoPrincipalMayor.primerNombre")				,"primerNombreTraslado")
			.add(Projections.property("personaTrasladoPrincipalMayor.segundoNombre")			,"segundonombreTraslado")
			.add(Projections.property("personaTrasladoPrincipalMayor.primerApellido")			,"primerApellidoTraslado")
			.add(Projections.property("personaTrasladoPrincipalMayor.segundoApellido")			,"segundoApellidoTraslado")
			.add(Projections.property("usuarioTrasladoPrincipalMayor.login")					,"loginTraslado")
			
		).setResultTransformer( Transformers.aliasToBean(DtoConsolidadoCierreXCentroAtencion.class));
	
		 ArrayList<DtoConsolidadoCierreXCentroAtencion> res=(ArrayList)criteria.list();
		
		return res;
	}
}
