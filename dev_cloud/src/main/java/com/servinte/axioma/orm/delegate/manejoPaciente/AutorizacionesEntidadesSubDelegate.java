package com.servinte.axioma.orm.delegate.manejoPaciente;

import java.lang.reflect.Constructor;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.axioma.util.log.Log4JManager;
import org.hibernate.Criteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.AliasToBeanConstructorResultTransformer;
import org.hibernate.transform.Transformers;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.ValoresPorDefecto;

import com.princetonsa.dto.capitacion.DtoConsultaProcesoAutorizacion;
import com.princetonsa.dto.capitacion.DtoProcesoPresupuestoCapitado;
import com.princetonsa.dto.facturacion.DtoContrato;
import com.princetonsa.dto.inventario.DtoAutorizacionEntSubcontratadasCapitacion;
import com.princetonsa.dto.manejoPaciente.DTOAutorEntidadSubcontratadaCapitacion;
import com.princetonsa.dto.manejoPaciente.DtoBusquedaTotalOrdenesAutorizadasEntSub;
import com.princetonsa.dto.manejoPaciente.DtoCentroCosto;
import com.princetonsa.dto.manejoPaciente.DtoConsultaTotalOrdenesAutorizadasEntSub;
import com.princetonsa.mundo.Camas1;
import com.princetonsa.mundo.solicitudes.Solicitudes;
import com.servinte.axioma.orm.AutorizacionesEntidadesSub;
import com.servinte.axioma.orm.AutorizacionesEntidadesSubHome;


/**
 * @author Cristhian Murillo
 *
 * Clase que contiene lógica del negocio sobre el modelo 
 */
@SuppressWarnings({"unchecked","rawtypes"})
public class AutorizacionesEntidadesSubDelegate extends AutorizacionesEntidadesSubHome
{
	
	/**
	 * Lista todos
	 */
	public ArrayList<AutorizacionesEntidadesSub> listarTodos()
	{
		return (ArrayList<AutorizacionesEntidadesSub>) sessionFactory.getCurrentSession()
			.createCriteria(AutorizacionesEntidadesSub.class)
			.list();
	}
	
	
	/**
	 * Retorna las Autorizaciones de la EntidadesSubcontratada enviada.
	 * Filtrando por su estado si se envia y qué sean vigentes
	 * @param DtoAutorizacionEntSubcontratadasCapitacion
	 * @return ArrayList<DtoEntregaMedicamentosInsumosEntSubcontratadas>
	 * 
	 * @author Cristhian Murillo
	 */
	public ArrayList<DtoAutorizacionEntSubcontratadasCapitacion> obtenerAutorizacionesPorEntSub(
			DtoAutorizacionEntSubcontratadasCapitacion dtoEntregaMedicamentosInsumosEntSubcontratadas)
	{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(AutorizacionesEntidadesSub.class, "autorizacionesEntidadesSub");
		
		criteria.createAlias("autorizacionesEntidadesSub.entidadesSubcontratadas"		, "entidadesSubcontratadas"	,Criteria.LEFT_JOIN);
		criteria.createAlias("autorizacionesEntidadesSub.pacientes"						, "pacientes"				,Criteria.LEFT_JOIN);
		
		// PermitirAutorizarDiferenteDeSolicitudes
		criteria.createAlias("autorizacionesEntidadesSub.autoEntsubSolicitudeses"		, "autoEntsubSolicitudeses"	,Criteria.LEFT_JOIN);	
		criteria.createAlias("autoEntsubSolicitudeses.solicitudes"						, "solicitudes"				,Criteria.LEFT_JOIN); 
		
		criteria.createAlias("solicitudes.cuentas"										, "cuentas"					,Criteria.LEFT_JOIN);
		criteria.createAlias("solicitudes.centrosCostoByCentroCostoSolicitado"			, "centrosCostoByCentroCostoSolicitado"	,Criteria.LEFT_JOIN);
		criteria.createAlias("autorizacionesEntidadesSub.autorizacionesCapitacionSubs"		, "autorizacionesCapitacionSubs"	,Criteria.LEFT_JOIN);
		criteria.createAlias("autorizacionesCapitacionSubs.autorizacionesEstanciaCapitas"	, "autorizacionesEstanciaCapitas"	,Criteria.LEFT_JOIN);
		criteria.createAlias("autorizacionesEstanciaCapitas.autorizacionesIngreEstancia"	, "autorizacionesIngreEstancia"		,Criteria.LEFT_JOIN);
		
		
		
		// Filtros de Búsqueda -----------------------------------------------------------------------------------------------------------
		
		criteria.add(Restrictions.eq("entidadesSubcontratadas.codigoPk"	, dtoEntregaMedicamentosInsumosEntSubcontratadas.getCodigoEntidadSubcontratada()));
		
		Date fechaActual=UtilidadFecha.getFechaActualTipoBD();
		criteria.add(Restrictions.ge("autorizacionesEntidadesSub.fechaVencimiento", fechaActual));
		
		if(dtoEntregaMedicamentosInsumosEntSubcontratadas.getValorGenerico().equals(ConstantesIntegridadDominio.acronimoArticulo)){
			// Unicamente consulta Autorizaciones con registro de entrega de Medicamentos e Insumos (articulos - autorizacionesEntSubArticu)
			criteria.createAlias("autorizacionesEntidadesSub.autorizacionesEntSubArticus","autorizacionesEntSubArticu",Criteria.INNER_JOIN);
			if(!UtilidadTexto.isEmpty(dtoEntregaMedicamentosInsumosEntSubcontratadas.getEstado2())){
				criteria.add(Restrictions.eq("autorizacionesEntSubArticu.estado"	, dtoEntregaMedicamentosInsumosEntSubcontratadas.getEstado2()));
			}
		}
		
		if(!UtilidadTexto.isEmpty(dtoEntregaMedicamentosInsumosEntSubcontratadas.getEstado())){
			criteria.add(Restrictions.eq("autorizacionesEntidadesSub.estado"	, dtoEntregaMedicamentosInsumosEntSubcontratadas.getEstado()));
		}
		
		if(dtoEntregaMedicamentosInsumosEntSubcontratadas.getCodigoPaciente() > 0){
			criteria.add(Restrictions.eq("pacientes.codigoPaciente"	, dtoEntregaMedicamentosInsumosEntSubcontratadas.getCodigoPaciente()));
		}
		// --------------------------------------------------------------------------------------------------------------------------------
		
		
		ProjectionList projectionList = Projections.projectionList();
			projectionList.add(Projections.property("entidadesSubcontratadas.codigoPk")				,"codigoEntidadSubcontratada");
			
			projectionList.add(Projections.property("autorizacionesIngreEstancia.codigoPk")			,"autorizacionIngresoEstancia");
		
			projectionList.add(Projections.property("autorizacionesEntidadesSub.consecutivo")		,"autorizacion");
			projectionList.add(Projections.property("autorizacionesEntidadesSub.tipo")				,"tipoAutorizacion");
			projectionList.add(Projections.property("autorizacionesEntidadesSub.fechaAutorizacion")	,"fechaAutorizacion");
			projectionList.add(Projections.property("autorizacionesEntidadesSub.horaAutorizacion")	,"horaAutorizacion"); 
			projectionList.add(Projections.property("autorizacionesEntidadesSub.fechaVencimiento")	,"fechaVencimiento");
			projectionList.add(Projections.property("autorizacionesEntidadesSub.estado")			,"estado");
			projectionList.add(Projections.property("autorizacionesEntidadesSub.observaciones")		,"observacionesGenerales");
			projectionList.add(Projections.property("autorizacionesEntidadesSub.consecutivoAutorizacion")		,"consecutivoAutorizacion");
			
			projectionList.add(Projections.property("solicitudes.numeroSolicitud")					,"numeroOrden");
			projectionList.add(Projections.property("solicitudes.consecutivoOrdenesMedicas")		,"consecutivoOrden");
			projectionList.add(Projections.property("solicitudes.fechaSolicitud")					,"fechaOrden");
			projectionList.add(Projections.property("solicitudes.urgente")							,"urgente");
			
			projectionList.add(Projections.property("centrosCostoByCentroCostoSolicitado.codigo")	,"codigoCentroCostoSolicitado");
			projectionList.add(Projections.property("centrosCostoByCentroCostoSolicitado.nombre")	,"nombreCentroCostoSolicitado");
			
			projectionList.add(Projections.property("cuentas.id")									,"numCuenta");
			
			projectionList.add( Projections.groupProperty("entidadesSubcontratadas.codigoPk"));
			projectionList.add( Projections.groupProperty("autorizacionesIngreEstancia.codigoPk"));
			projectionList.add( Projections.groupProperty("autorizacionesEntidadesSub.consecutivo"));
			projectionList.add( Projections.groupProperty("autorizacionesEntidadesSub.consecutivoAutorizacion"));
			projectionList.add( Projections.groupProperty("autorizacionesEntidadesSub.tipo"));
			projectionList.add( Projections.groupProperty("autorizacionesEntidadesSub.fechaAutorizacion"));
			projectionList.add( Projections.groupProperty("autorizacionesEntidadesSub.horaAutorizacion"));
			projectionList.add( Projections.groupProperty("autorizacionesEntidadesSub.fechaVencimiento"));
			projectionList.add( Projections.groupProperty("autorizacionesEntidadesSub.estado"));
			projectionList.add( Projections.groupProperty("autorizacionesEntidadesSub.observaciones"));
			projectionList.add( Projections.groupProperty("solicitudes.numeroSolicitud"));
			projectionList.add( Projections.groupProperty("solicitudes.consecutivoOrdenesMedicas"));
			projectionList.add( Projections.groupProperty("solicitudes.fechaSolicitud"));
			projectionList.add( Projections.groupProperty("solicitudes.urgente"));
			projectionList.add( Projections.groupProperty("centrosCostoByCentroCostoSolicitado.codigo"));
			projectionList.add( Projections.groupProperty("centrosCostoByCentroCostoSolicitado.nombre"));
			projectionList.add( Projections.groupProperty("cuentas.id"));
			
			
		criteria.setProjection(projectionList);
		
		criteria.addOrder(Property.forName("autorizacionesEntidadesSub.fechaAutorizacion").asc());
		
		criteria.setResultTransformer(Transformers.aliasToBean(DtoAutorizacionEntSubcontratadasCapitacion.class));
		
		ArrayList<DtoAutorizacionEntSubcontratadasCapitacion> listaResultado = new ArrayList<DtoAutorizacionEntSubcontratadasCapitacion>();
		listaResultado = (ArrayList<DtoAutorizacionEntSubcontratadasCapitacion>) criteria.list();

		
		// -------------------------------------------------------------------
		// Se adiciona el código de cama al resultado
		Connection con = null;
    	con = UtilidadBD.abrirConexion();
		for (DtoAutorizacionEntSubcontratadasCapitacion resultado : listaResultado) 
		{
			resultado.setCamaCama(Camas1.obtenerCamaDadaCuenta(con,resultado.getNumCuenta()+""));
			Camas1 detalleCama;
			detalleCama = new Camas1();
			try { detalleCama.detalleCama1(con);
			} catch (SQLException e) { Log4JManager.error("no se pudo cargar el detalle de la cama. ",e); }
			resultado.setCamaPiso(detalleCama.getPiso()+"-"+detalleCama.getNombrePiso());
			resultado.setCamaHabitacion(detalleCama.getHabitacion()+"-"+detalleCama.getNombreHabitacion());
			
			if(resultado.getUrgente() == null){
				resultado.setUrgente(new Boolean(false));
			}
		}
		UtilidadBD.closeConnection(con);
		// -------------------------------------------------------------------
		
		return listaResultado;
	}
	
	
	/**
	 * Implementacion del método findById
	 * @param id
	 * @return AutorizacionesEntidadesSub
	 */
	public AutorizacionesEntidadesSub obtenerAutorizacionesEntidadesSubPorId(long id) {
		return super.findById(id);
	}
	
	
	/**
	 * Obtener autorización por el consecutivo de autorizacion 
	 * 
	 * @param consecutivoAutorizacion
	 * @return
	 */
	@SuppressWarnings("unused")
	public AutorizacionesEntidadesSub obtenerAutorizacionEntSubPorConsecutivoAutorizacion (String consecutivoAutorizacion){
		
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(AutorizacionesEntidadesSub.class,"autorEntSub")
		.add(Restrictions.eq("autorEntSub.consecutivoAutorizacion", consecutivoAutorizacion));
		
		AutorizacionesEntidadesSub autorizacionesEntidadesSub = new AutorizacionesEntidadesSub();
		autorizacionesEntidadesSub = (AutorizacionesEntidadesSub)criteria.uniqueResult();
		
		return autorizacionesEntidadesSub;

	}
	
	/**
	 * 
	 * Este Método se encarga de insertar en la base de datos
	 * un registro de autorización de entidad subcontratada
	 * 
	 * @param AutorizacionesEntidadesSub autorizacion
	 * @return boolean
	 * @author, Angela Maria Aguirre
	 *
	 */
	public boolean guardarAutorizacionEntidadSubcontratada(AutorizacionesEntidadesSub autorizacion){
		boolean save = true;					
		try{
			super.persist(autorizacion);
		} catch (Exception e) {
			save = false;
			Log4JManager.error("No se pudo guardar el registro de " +
					"autorización entidad subcontratada: ",e);
		}				
		return save;				
	}
	
	
	/**
	 * 
	 * Implementación del método attachDirty de la super clase AutorizacionesEntidadesSubHome
	 * 
	 * @param AutorizacionesEntidadesSub autorizacion
	 * @return boolean
	 * 
	 * @author, Angela Maria Aguirre, Cristhian Murillo
	 *
	 */
	public boolean sincronizarAutorizacionEntidadSubcontratada(AutorizacionesEntidadesSub autorizacion){
		/**
		 * Se elimina la captura de la excepción en este nivel, ya que no se manejan excepciones por cada transacción
		 * de esta manera se deja la captura de la excepción en el mundo.
		 */
		boolean save = true;					
		super.attachDirty(autorizacion);
		return save;				
	}
	
	
	/**
	 * 
	 * Este Método se encarga de consultar por el ID las autorizaciones de
	 * entidades subcontratadas y su respectiva autorización de capitación
	 * 
	 * @param DTOAdministracionAutorizacion dto
	 * @return ArrayList<DTOAdministracionAutorizacion>
	 * @author, Angela Maria Aguirre
	 *
	 */
	public DTOAutorEntidadSubcontratadaCapitacion obtenerAutorizacionEntidadSubCapitacionPorID(
			DTOAutorEntidadSubcontratadaCapitacion dto){		
		
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				AutorizacionesEntidadesSub.class, "autorizacionEntidadSub");		
		criteria.createAlias("autorizacionEntidadSub.autorizacionesCapitacionSubs","autorizacionCapitacion");
		
		/**
		 * MT 3377, MT 1316
		 * Diana Ruiz          
		 */
		criteria.createAlias("autorizacionCapitacion.tiposAfiliado","tiposAfiliado", Criteria.LEFT_JOIN);
		criteria.createAlias("autorizacionCapitacion.estratosSociales","estratosSociales", Criteria.LEFT_JOIN);
			
		criteria.createAlias("autorizacionCapitacion.histoAutorizacionCapitaSubs","historicoAutorizacion");
		criteria.createAlias("autorizacionCapitacion.convenios","convenioRecobro",Criteria.LEFT_JOIN);		
		criteria.createAlias("autorizacionEntidadSub.entidadesSubcontratadas","entidadSubcontratada");
		criteria.createAlias("autorizacionEntidadSub.instituciones","institucionAutoriza");
		criteria.createAlias("autorizacionEntidadSub.pacientes","paciente");
		
		//En caso de ser solicitud
		criteria.createAlias("autorizacionEntidadSub.autoEntsubSolicitudeses","autoentsubsolicitudes", Criteria.LEFT_JOIN);
		criteria.createAlias("autoentsubsolicitudes.solicitudes","solicitud", Criteria.LEFT_JOIN);
		//En caso de ser Orden Ambulatoria
		criteria.createAlias("autorizacionEntidadSub.autoEntsubOrdenambulas","autoentsubordenesamb", Criteria.LEFT_JOIN);
		criteria.createAlias("autoentsubordenesamb.ordenesAmbulatorias","ordenAmbulatoria", Criteria.LEFT_JOIN);
		criteria.createAlias("ordenAmbulatoria.estadosOrdAmbulatorias","estadoOrdenAmb", Criteria.LEFT_JOIN);
		//En caso de ser Peticion
		criteria.createAlias("autorizacionEntidadSub.autoEntsubPeticioneses","autoentsubpeticiones", Criteria.LEFT_JOIN);
		criteria.createAlias("autoentsubpeticiones.peticionQx","peticion", Criteria.LEFT_JOIN);
		criteria.createAlias("peticion.estadosPeticion","estadoPeticion", Criteria.LEFT_JOIN);
		
				
		criteria.createAlias("autorizacionEntidadSub.autorizacionesEntSubMontoses","autoEntSubMontos");
		criteria.createAlias("autoEntSubMontos.viasIngreso","viaIngreso");
		criteria.createAlias("autoEntSubMontos.detalleMonto","detalleMonto",Criteria.LEFT_JOIN);
		criteria.createAlias("paciente.personas","persona");
		criteria.createAlias("persona.tiposIdentificacion","tipoIdentificacion");
		criteria.createAlias("persona.usuarioXConvenios","usuarioConvenio");
		
		criteria.createAlias("autorizacionEntidadSub.convenios","convenio", Criteria.LEFT_JOIN);
				
		criteria.createAlias("convenio.tiposContrato","tipoContrato");
		criteria.createAlias("historicoAutorizacion.usuarios","usuarioAutoriza");
		criteria.createAlias("usuarioAutoriza.personas","personaAutoriza");
		criteria.add(Restrictions.eq("autorizacionCapitacion.codigoPk", dto.getAutorCapitacion().getCodigoPK()));
		criteria.add(Restrictions.eq("historicoAutorizacion.accionRealizada", ConstantesIntegridadDominio.acronimoAccionHistoricaInsertar));
		criteria.setProjection(Projections.distinct(Projections.projectionList()
				
				//Datos Sección paciente				
				.add(Projections.property("persona.primerNombre"),"primerNombrePersona")
				.add(Projections.property("persona.segundoNombre"),"segundoNombrePersona")
				.add(Projections.property("persona.primerApellido"),"primerApellidoPersona")
				.add(Projections.property("persona.segundoApellido"),"segundoApellidoPersona")
				.add(Projections.property("persona.codigo"),"codigoPersona")
				.add(Projections.property("tipoIdentificacion.acronimo"),"tipoID")
				.add(Projections.property("persona.numeroIdentificacion"),"numeroIdentificacion")				
				.add(Projections.property("convenio.nombre"),"nombreConvenio")
				.add(Projections.property("convenio.codigo"),"codigoConvenio")
								
				.add(Projections.property("tipoContrato.nombre"),"tipoContrato")
				.add(Projections.property("estratosSociales.descripcion"),"estratoSocial")
				.add(Projections.property("tiposAfiliado.nombre"),"tipoAfiliado")
				.add(Projections.property("autoEntSubMontos.valormonto"),"valorMonto")
				.add(Projections.property("autoEntSubMontos.porcentajemonto"),"porcentajeMonto")
				.add(Projections.property("viaIngreso.codigo"),"codigoViaIngreso")
				.add(Projections.property("viaIngreso.nombre"),"nombreViaIngreso")
				.add(Projections.property("convenioRecobro.nombre"),"convenioRecobro")
				.add(Projections.property("autorizacionCapitacion.otroConvenioRecobro"),"descripcionConvenioRecobro")
				
				//Datos Sección autorización
				.add(Projections.property("entidadSubcontratada.razonSocial"),"entidadSubcontratada")
				.add(Projections.property("entidadSubcontratada.direccion"),"direccion")
				.add(Projections.property("entidadSubcontratada.telefono"),"telefono")	
				.add(Projections.property("autorizacionCapitacion.consecutivo"),"consecutivo")
				.add(Projections.property("autorizacionEntidadSub.fechaAutorizacion"),"fechaAutorizacion")
				.add(Projections.property("autorizacionEntidadSub.fechaVencimiento"),"fechaVencimiento")				
				.add(Projections.property("autorizacionCapitacion.indicadorPrioridad"),"indicadorPrioridad")
				.add(Projections.property("autorizacionEntidadSub.estado"),"estado")
				.add(Projections.property("institucionAutoriza.razonSocial"),"entidadAutoriza")				
				.add(Projections.property("personaAutoriza.primerNombre"),"nombreUsuarioAutoriza")
				.add(Projections.property("personaAutoriza.primerApellido"),"apellidoUsuarioAutoriza")
				.add(Projections.property("autorizacionEntidadSub.observaciones"),"observaciones")
				.add(Projections.property("persona.fechaNacimiento"),"fechaNacimiento")
				.add(Projections.property("autorizacionEntidadSub.consecutivo"),"codigoAutorEntSub")
				.add(Projections.property("autorizacionCapitacion.indicativoTemporal"),"temporal")
				.add(Projections.property("autorizacionCapitacion.direccionEntidad"),"direccionotra")
				.add(Projections.property("autorizacionCapitacion.telefonoEntidad"),"telefonootra")
				.add(Projections.property("autorizacionCapitacion.descripcionEntidad"),"descripcionEntidad")
				.add(Projections.property("solicitud.numeroSolicitud"),"codigoSolicitud")
				.add(Projections.property("ordenAmbulatoria.codigo"),"codigoOrdenAmb")
				.add(Projections.property("ordenAmbulatoria.consecutivoOrden"),"consecutivoOrdenAmb")
				.add(Projections.property("ordenAmbulatoria.fecha"),"fechaOrdenAmb")
				.add(Projections.property("peticion.codigo"),"codigoPeticion")
				.add(Projections.property("peticion.fechaPeticion"),"fechaPeticion")
				.add(Projections.property("solicitud.estadoHistoriaClinica"),"estadoSolicitud")
				.add(Projections.property("estadoOrdenAmb.codigo"),"estadoOrdenAmb")
				.add(Projections.property("estadoPeticion.codigo"),"estadoPeticion")
				.add(Projections.property("estadoOrdenAmb.descripcion"),"nombreEstadoOrdenAmb")
				.add(Projections.property("autorizacionEntidadSub.consecutivo"),"codigoAutorEntSub")
				.add(Projections.property("autorizacionEntidadSub.consecutivoAutorizacion"),"consecutivoAutorEntSub")
				.add(Projections.property("autorizacionCapitacion.tipoAutorizacion"),"tipoAutorizacionCapitacion")
				.add(Projections.property("estratosSociales.codigo"),"codigoEstratoSocial")
				.add(Projections.property("tiposAfiliado.acronimo"),"acronimoTipoAfiliado")
				.add(Projections.property("autoEntSubMontos.tipomonto"),"tipoMonto")
				.add(Projections.property("autoEntSubMontos.cantidadMonto"),"cantidadMonto")
				.add(Projections.property("detalleMonto.detalleCodigo"),"codigoDetalleMonto")
		));		
		
		Class[] parametros=new Class[54];
		parametros[0]=String.class;
		parametros[1]=String.class;
		parametros[2]=String.class;
		parametros[3]=String.class;
		parametros[4]=int.class;
		parametros[5]=String.class;
		parametros[6]=String.class;	
		parametros[7]=String.class;
		parametros[8]=int.class;
		parametros[9]=String.class;
		parametros[10]=String.class;
		parametros[11]=String.class;
		parametros[12]=Double.class;
		parametros[13]=Double.class;
		parametros[14]=Integer.class;
		parametros[15]=String.class;
		parametros[16]=String.class;
		parametros[17]=String.class;
		parametros[18]=String.class;
		parametros[19]=String.class;
		parametros[20]=String.class;
		parametros[21]=long.class;
		parametros[22]=Date.class;
		parametros[23]=Date.class;
		parametros[24]=Integer.class;
		parametros[25]=String.class;
		parametros[26]=String.class;
		parametros[27]=String.class;
		parametros[28]=String.class;
		parametros[29]=String.class;
		parametros[30]=Date.class;
		parametros[31]=long.class;
		parametros[32]=char.class;
		parametros[33]=String.class;
		parametros[34]=String.class;
		parametros[35]=String.class;
		parametros[36]=Integer.class;
		parametros[37]=Long.class;
		parametros[38]=String.class;
		parametros[39]=Date.class;
		parametros[40]=Integer.class;
		parametros[41]=Date.class;
		parametros[42]=Integer.class;
		parametros[43]=Byte.class;
		parametros[44]=Integer.class;
		parametros[45]=String.class;
		parametros[46]=long.class;
		parametros[47]=String.class;
		parametros[48]=String.class;
		parametros[49]=Integer.class;
		parametros[50]=Character.class;
		parametros[51]=Integer.class;
		parametros[52]=Integer.class;
		parametros[53]=Integer.class;
		
		Constructor constructor;
		try {
			constructor = DTOAutorEntidadSubcontratadaCapitacion.class.getConstructor(parametros);
			criteria.setResultTransformer(new AliasToBeanConstructorResultTransformer(constructor));
			
		} catch (SecurityException e) {
			Log4JManager.error("Error convirtiendo el resultado", e);
		} catch (NoSuchMethodException e) {
			Log4JManager.error("Error convirtiendo el resultado", e);
			e.printStackTrace();
		}	
		
		
		
		//------------------------------------------------------------------------------------------------
		DTOAutorEntidadSubcontratadaCapitacion autorizacion = new DTOAutorEntidadSubcontratadaCapitacion();
		
		ArrayList<DTOAutorEntidadSubcontratadaCapitacion>listaResultado = new ArrayList<DTOAutorEntidadSubcontratadaCapitacion>(); 
		listaResultado = (ArrayList<DTOAutorEntidadSubcontratadaCapitacion>)criteria.list();
		
		// Si retorna más de una resultado es posible que tenga varias cuentas en asocio. Se filtra y se selecciona la cuenta activa.
		if(listaResultado.size() >= 1){
			autorizacion = listaResultado.get(0);
			//Se deben consultar los centros de costo/ farmacias dado que pueden
			//existir varias asociadas a la autorización
			Criteria criteriaDet = sessionFactory.getCurrentSession().createCriteria(AutorizacionesEntidadesSub.class, "autorizacionEntidadSub");
			criteriaDet.createAlias("autorizacionEntidadSub.autorizacionesCapitacionSubs","autorizacionCapitacion")
				.createAlias("autorizacionEntidadSub.autoCapiXCentroCostos","autoCapiXCentroCostos")
				.createAlias("autoCapiXCentroCostos.centrosCosto","centrosCosto");
			criteriaDet.add(Restrictions.eq("autorizacionCapitacion.codigoPk", dto.getAutorCapitacion().getCodigoPK()));
			criteriaDet.setProjection(Projections.distinct(Projections.projectionList()
				.add(Projections.property("centrosCosto.codigo"),"codigoCentroCosto")
				.add(Projections.property("centrosCosto.nombre"),"nombre")));
			criteriaDet.setResultTransformer(Transformers.aliasToBean(DtoCentroCosto.class));
			List<DtoCentroCosto> centrosCostoAuto=(List<DtoCentroCosto>)criteriaDet.list();
			if(centrosCostoAuto != null && !centrosCostoAuto.isEmpty()){
				String nombresCC=null;
				int codigoCentroCosto=ConstantesBD.codigoNuncaValido;
				int i=0;
				int k=centrosCostoAuto.size();
				StringBuffer buf = new StringBuffer("");
				for(DtoCentroCosto dtoCC:centrosCostoAuto){
					if(i==0){
						codigoCentroCosto=dtoCC.getCodigoCentroCosto();
					}
					i++;
					buf.append(dtoCC.getNombre());
					if(i<k){
						buf.append(", ");
					}
				}
				nombresCC=buf.toString();
				autorizacion.getDtoPaciente().setNombreCentroCosto(nombresCC);
				autorizacion.getDtoPaciente().setCodigoCentroCosto(codigoCentroCosto);
			}
		}
		else{
			autorizacion = null;
		}
		//(antes) DTOAutorEntidadSubcontratadaCapitacion autorizacion = (DTOAutorEntidadSubcontratadaCapitacion)criteria.uniqueResult(); 	
		//-------------------------------------------------------------------------------------------------
		
		return autorizacion;
	}
	
	
	/**
	 * 
	 * Este Método se encarga de consultar por el ID las autorizaciones de
	 * entidades subcontratadas y su respectiva autorización de capitación, 
	 * estas autorizaciones son generadas en un ingreso estancia
	 * 
	 * @param DTOAdministracionAutorizacion dto
	 * @return ArrayList<DTOAdministracionAutorizacion>
	 * @author, Angela Maria Aguirre
	 *
	 */
	public DTOAutorEntidadSubcontratadaCapitacion obtenerAutorizacionEntidadSubCapitacionIngEstanciaPorID(
			DTOAutorEntidadSubcontratadaCapitacion dto){
				
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				AutorizacionesEntidadesSub.class, "autorizacionEntidadSub");		
		criteria.createAlias("autorizacionEntidadSub.autorizacionesCapitacionSubs","autorizacionCapitacion");
		criteria.createAlias("autorizacionCapitacion.histoAutorizacionCapitaSubs","historicoAutorizacion");
		
		
		criteria.createAlias("autorizacionCapitacion.tiposAfiliado","tiposAfiliado", Criteria.LEFT_JOIN);
		criteria.createAlias("autorizacionCapitacion.estratosSociales","estratosSociales", Criteria.LEFT_JOIN);
		
		
		criteria.createAlias("autorizacionCapitacion.convenios","convenioRecobro",Criteria.LEFT_JOIN);
		
		criteria.createAlias("autorizacionCapitacion.autorizacionesEstanciaCapitas","autorizacionesEstanciaCapita");
		criteria.createAlias("autorizacionesEstanciaCapita.autorizacionesIngreEstancia","autorizacionIngEstancia");
		criteria.createAlias("autorizacionIngEstancia.ingresosEstancia","ingresoEstancia");
		criteria.createAlias("autorizacionEntidadSub.autorizacionesEntSubMontoses","autoEntSubMontos",Criteria.LEFT_JOIN);
		criteria.createAlias("autoEntSubMontos.viasIngreso","viaIngreso",Criteria.LEFT_JOIN);
		criteria.createAlias("autoEntSubMontos.detalleMonto","detalleMonto",Criteria.LEFT_JOIN);
			
		
		//criteria.createAlias("autoEntSubMontos.autoCapiXCentroCostos","autoCapiXCentroCostos");
		//criteria.createAlias("autoCapiXCentroCostos.centrosCosto","centrosCosto", Criteria.LEFT_JOIN);
		
		criteria.createAlias("autorizacionEntidadSub.entidadesSubcontratadas","entidadSubcontratada");
		criteria.createAlias("autorizacionEntidadSub.instituciones","institucionAutoriza");
		criteria.createAlias("autorizacionEntidadSub.pacientes","paciente");
		criteria.createAlias("paciente.personas","persona");
		criteria.createAlias("persona.tiposIdentificacion","tipoIdentificacion");
		
		criteria.createAlias("autorizacionEntidadSub.convenios","convenio", Criteria.LEFT_JOIN);
		
		criteria.createAlias("convenio.tiposContrato","tipoContrato");	
		criteria.createAlias("historicoAutorizacion.usuarios","usuarioAutoriza");
		criteria.createAlias("usuarioAutoriza.personas","personaAutoriza");
		
		
		Log4JManager.info("_____ LOG_DEBUG _____ autorizacionCapitacion.codigoPk: "+dto.getAutorCapitacion().getCodigoPK());
		criteria.add(Restrictions.eq("autorizacionCapitacion.codigoPk", dto.getAutorCapitacion().getCodigoPK()));
		criteria.add(Restrictions.eq("historicoAutorizacion.accionRealizada", ConstantesIntegridadDominio.acronimoAccionHistoricaInsertar));
		
		Log4JManager.info("_____ LOG_DEBUG _____ fechaActual: "+UtilidadFecha.getFechaActualTipoBD());
		
		
		ProjectionList projectionList =  Projections.projectionList();		
				
		//Datos Sección paciente	
		projectionList.add(Projections.property("persona.primerNombre"),"primerNombrePersona");
		projectionList.add(Projections.property("persona.segundoNombre"),"segundoNombrePersona");
		projectionList.add(Projections.property("persona.primerApellido"),"primerApellidoPersona");
		projectionList.add(Projections.property("persona.segundoApellido"),"segundoApellidoPersona");
		projectionList.add(Projections.property("persona.codigo"),"codigoPersona");
		projectionList.add(Projections.property("tipoIdentificacion.acronimo"),"tipoID");
		projectionList.add(Projections.property("persona.numeroIdentificacion"),"numeroIdentificacion");				
		projectionList.add(Projections.property("convenio.nombre"),"nombreConvenio");		
		projectionList.add(Projections.property("tipoContrato.nombre"),"tipoContrato");
		
		projectionList.add(Projections.property("estratosSociales.descripcion"),"estratoSocial");
		projectionList.add(Projections.property("tiposAfiliado.nombre"),"tipoAfiliado");
		
		
		projectionList.add(Projections.property("convenioRecobro.nombre"),"convenioRecobro");
		projectionList.add(Projections.property("autorizacionCapitacion.otroConvenioRecobro"),"descripcionConvenioRecobro");
		
		//Datos Sección autorización
		projectionList.add(Projections.property("entidadSubcontratada.razonSocial"),"entidadSubcontratada");
		projectionList.add(Projections.property("entidadSubcontratada.direccion"),"direccion");
		projectionList.add(Projections.property("entidadSubcontratada.telefono"),"telefono");				
		projectionList.add(Projections.property("autorizacionCapitacion.consecutivo"),"consecutivo");
		projectionList.add(Projections.property("autorizacionEntidadSub.fechaAutorizacion"),"fechaAutorizacion");
		projectionList.add(Projections.property("autorizacionEntidadSub.fechaVencimiento"),"fechaVencimiento");		
		projectionList.add(Projections.property("autorizacionCapitacion.indicadorPrioridad"),"indicadorPrioridad");
		projectionList.add(Projections.property("autorizacionEntidadSub.estado"),"estado");
		projectionList.add(Projections.property("institucionAutoriza.razonSocial"),"entidadAutoriza");			
		projectionList.add(Projections.property("personaAutoriza.primerNombre"),"nombreUsuarioAutoriza");
		projectionList.add(Projections.property("personaAutoriza.primerApellido"),"apellidoUsuarioAutoriza");
		projectionList.add(Projections.property("autorizacionEntidadSub.observaciones"),"observaciones");
		projectionList.add(Projections.property("persona.fechaNacimiento"),"fechaNacimiento");
		projectionList.add(Projections.property("viaIngreso.codigo"),"codigoViaIngreso");
		projectionList.add(Projections.property("viaIngreso.nombre"),"nombreViaIngreso");
		//projectionList.add(Projections.property("centrosCosto.codigo"),"codigoCentroCosto");
		//projectionList.add(Projections.property("centrosCosto.nombre"),"nombreCentroCosto");
		projectionList.add(Projections.property("autoEntSubMontos.valormonto"),"codigoCentroCosto");
		projectionList.add(Projections.property("autoEntSubMontos.porcentajemonto"),"nombreCentroCosto");
		projectionList.add(Projections.property("autorizacionEntidadSub.consecutivo"),"codigoAutorEntSub");
		projectionList.add(Projections.property("autorizacionCapitacion.indicativoTemporal"),"temporal");
		projectionList.add(Projections.property("autoEntSubMontos.tipomonto"),"tipoMonto");
		
		projectionList.add(Projections.property("autoEntSubMontos.cantidadMonto"),"cantidadMonto");
		projectionList.add(Projections.property("detalleMonto.detalleCodigo"),"codigoDetalleMonto");
		
		//hermorhu - MT6537
		//Traer el consecutivo de la autorizacion de Entidad Subcontratada
		projectionList.add(Projections.property("autorizacionEntidadSub.consecutivoAutorizacion"), "consecutivoAutorEntSub");
		
		criteria.setProjection(projectionList);
		
		Class[] parametros=new Class[36];
		parametros[0]=String.class;
		parametros[1]=String.class;
		parametros[2]=String.class;
		parametros[3]=String.class;
		parametros[4]=int.class;
		parametros[5]=String.class;
		parametros[6]=String.class;	
		parametros[7]=String.class;
		parametros[8]=String.class;
		parametros[9]=String.class;
		parametros[10]=String.class;		
		parametros[11]=String.class;
		parametros[12]=String.class;
		parametros[13]=String.class;
		parametros[14]=String.class;
		parametros[15]=String.class;
		parametros[16]=long.class;
		parametros[17]=Date.class;
		parametros[18]=Date.class;
		parametros[19]=Integer.class;
		parametros[20]=String.class;
		parametros[21]=String.class;
		parametros[22]=String.class;
		parametros[23]=String.class;
		parametros[24]=String.class;
		parametros[25]=Date.class;
		parametros[26]=Integer.class;
		parametros[27]=String.class;
		parametros[28]=Double.class;
		parametros[29]=Double.class;
		parametros[30]=long.class;
		parametros[31]=char.class;
		parametros[32]=Integer.class;
		parametros[33]=Integer.class;
		parametros[34]=Integer.class;
		parametros[35]=String.class;
		
		Constructor constructor;
		try {
			constructor = DTOAutorEntidadSubcontratadaCapitacion.class.getConstructor(parametros);
			criteria.setResultTransformer(new AliasToBeanConstructorResultTransformer(constructor));
			
		} catch (SecurityException e) {
			Log4JManager.error("Error convirtiendo el resultado", e);
		} catch (NoSuchMethodException e) {
			Log4JManager.error("Error convirtiendo el resultado", e);
			e.printStackTrace();
		}	
		
		DTOAutorEntidadSubcontratadaCapitacion autorizacion = (DTOAutorEntidadSubcontratadaCapitacion)criteria.uniqueResult();
		
		// Si retorna más de una resultado es posible que tenga varias cuentas en asocio. Se filtra y se selecciona la cuenta activa.
		if(autorizacion !=null){
			//Se deben consultar los centros de costo/ farmacias dado que pueden
			//existir varias asociadas a la autorización
			Criteria criteriaDet = sessionFactory.getCurrentSession().createCriteria(AutorizacionesEntidadesSub.class, "autorizacionEntidadSub");
			criteriaDet.createAlias("autorizacionEntidadSub.autorizacionesCapitacionSubs","autorizacionCapitacion")
				.createAlias("autorizacionEntidadSub.autoCapiXCentroCostos","autoCapiXCentroCostos")
				.createAlias("autoCapiXCentroCostos.centrosCosto","centrosCosto");
			criteriaDet.add(Restrictions.eq("autorizacionCapitacion.codigoPk", dto.getAutorCapitacion().getCodigoPK()));
			criteriaDet.setProjection(Projections.distinct(Projections.projectionList()
				.add(Projections.property("centrosCosto.codigo"),"codigoCentroCosto")
				.add(Projections.property("centrosCosto.nombre"),"nombre")));
			criteriaDet.setResultTransformer(Transformers.aliasToBean(DtoCentroCosto.class));
			List<DtoCentroCosto> centrosCostoAuto=(List<DtoCentroCosto>)criteriaDet.list();
			if(centrosCostoAuto != null && !centrosCostoAuto.isEmpty()){
				String nombresCC=null;
				int codigoCentroCosto=ConstantesBD.codigoNuncaValido;
				int i=0;
				int k=centrosCostoAuto.size();
				StringBuffer buf = new StringBuffer("");
				for(DtoCentroCosto dtoCC:centrosCostoAuto){
					if(i==0){
						codigoCentroCosto=dtoCC.getCodigoCentroCosto();
					}
					i++;
					buf.append(dtoCC.getNombre());
					if(i<k){
						buf.append(", ");
					}
				}
				nombresCC=buf.toString();
				autorizacion.getDtoPaciente().setNombreCentroCosto(nombresCC);
				autorizacion.getDtoPaciente().setCodigoCentroCosto(codigoCentroCosto);
			}
		}
		else{
			autorizacion = null;
		}
		
		return autorizacion;		
	}
	
	
	/**
	 * 
	 * Este Método se encarga de actualizar el detalle de una autorización de entidad subcontratada
	 * 
	 * @param AutorizacionesEntidadesSub
	 * @return boolean
	 * @author, Angela Maria Aguirre
	 *
	 */
	public boolean actualizarAutorizacionEntidadSub(AutorizacionesEntidadesSub autorizacion){
		boolean save = false;
		try{
			super.merge(autorizacion);
			save = true;
		}catch (Exception e) {
			save = false;
			Log4JManager.error("No se pudo actualizar el registro de autorizacion de capitacion: ",e);
		}		
		return save;
	}
	
	
	
	
	
	/**
	 * Retorna las Autorizaciones de la EntidadesSubcontratada según en número de solicitud enviada.
	 * @param DtoAutorizacionEntSubcontratadasCapitacion
	 * @return ArrayList<DtoEntregaMedicamentosInsumosEntSubcontratadas>
	 * 
	 * @author Cristhian Murillo
	 */
	public ArrayList<DtoAutorizacionEntSubcontratadasCapitacion> obtenerAutorizacionesPorEntSubPorNumeroSolicitud(
			DtoAutorizacionEntSubcontratadasCapitacion dtoEntregaMedicamentosInsumosEntSubcontratadas)
	{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(AutorizacionesEntidadesSub.class, "autorizacionesEntidadesSub");
		
		criteria.createAlias("autorizacionesEntidadesSub.entidadesSubcontratadas"			, "entidadesSubcontratadas"				,Criteria.LEFT_JOIN);
		criteria.createAlias("autorizacionesEntidadesSub.pacientes"							, "pacientes"							,Criteria.LEFT_JOIN);
		
		// PermitirAutorizarDiferenteDeSolicitudes
		criteria.createAlias("autorizacionesEntidadesSub.autoEntsubSolicitudeses"			, "autoEntsubSolicitudeses"				,Criteria.LEFT_JOIN);	
		criteria.createAlias("autoEntsubSolicitudeses.solicitudes"							, "solicitudes"							,Criteria.LEFT_JOIN); 
		
		criteria.createAlias("solicitudes.cuentas"											, "cuentas"								,Criteria.LEFT_JOIN);
		criteria.createAlias("solicitudes.centrosCostoByCentroCostoSolicitado"				, "centrosCostoByCentroCostoSolicitado"	,Criteria.LEFT_JOIN);
		criteria.createAlias("autorizacionesEntidadesSub.autorizacionesCapitacionSubs"		, "autorizacionesCapitacionSubs"		,Criteria.LEFT_JOIN);
		criteria.createAlias("autorizacionesCapitacionSubs.autorizacionesEstanciaCapitas"	, "autorizacionesEstanciaCapitas"		,Criteria.LEFT_JOIN);
		criteria.createAlias("autorizacionesEstanciaCapitas.autorizacionesIngreEstancia"	, "autorizacionesIngreEstancia"			,Criteria.LEFT_JOIN);
		
		
		criteria.add(Restrictions.eq("solicitudes.numeroSolicitud"	, dtoEntregaMedicamentosInsumosEntSubcontratadas.getNumeroOrden()));
		
		
		ProjectionList projectionList = Projections.projectionList();
			projectionList.add(Projections.property("entidadesSubcontratadas.codigoPk")				,"codigoEntidadSubcontratada");
			
			projectionList.add(Projections.property("autorizacionesIngreEstancia.codigoPk")			,"autorizacionIngresoEstancia");
		
			projectionList.add(Projections.property("autorizacionesEntidadesSub.consecutivo")		,"autorizacion");
			projectionList.add(Projections.property("autorizacionesEntidadesSub.tipo")				,"tipoAutorizacion");
			projectionList.add(Projections.property("autorizacionesEntidadesSub.fechaAutorizacion")	,"fechaAutorizacion");
			projectionList.add(Projections.property("autorizacionesEntidadesSub.horaAutorizacion")	,"horaAutorizacion"); 
			projectionList.add(Projections.property("autorizacionesEntidadesSub.fechaVencimiento")	,"fechaVencimiento");
			projectionList.add(Projections.property("autorizacionesEntidadesSub.estado")			,"estado");
			projectionList.add(Projections.property("autorizacionesEntidadesSub.observaciones")		,"observacionesGenerales");
			
			projectionList.add(Projections.property("solicitudes.numeroSolicitud")					,"numeroOrden");
			projectionList.add(Projections.property("solicitudes.consecutivoOrdenesMedicas")		,"consecutivoOrden");
			projectionList.add(Projections.property("solicitudes.fechaSolicitud")					,"fechaOrden");
			projectionList.add(Projections.property("solicitudes.urgente")							,"urgente");
			
			projectionList.add(Projections.property("centrosCostoByCentroCostoSolicitado.codigo")	,"codigoCentroCostoSolicitado");
			projectionList.add(Projections.property("centrosCostoByCentroCostoSolicitado.nombre")	,"nombreCentroCostoSolicitado");
			
			projectionList.add(Projections.property("cuentas.id")									,"numCuenta");
			
			projectionList.add( Projections.groupProperty("entidadesSubcontratadas.codigoPk"));
			projectionList.add( Projections.groupProperty("autorizacionesIngreEstancia.codigoPk"));
			projectionList.add( Projections.groupProperty("autorizacionesEntidadesSub.consecutivo"));
			projectionList.add( Projections.groupProperty("autorizacionesEntidadesSub.tipo"));
			projectionList.add( Projections.groupProperty("autorizacionesEntidadesSub.fechaAutorizacion"));
			projectionList.add( Projections.groupProperty("autorizacionesEntidadesSub.horaAutorizacion"));
			projectionList.add( Projections.groupProperty("autorizacionesEntidadesSub.fechaVencimiento"));
			projectionList.add( Projections.groupProperty("autorizacionesEntidadesSub.estado"));
			projectionList.add( Projections.groupProperty("autorizacionesEntidadesSub.observaciones"));
			projectionList.add( Projections.groupProperty("solicitudes.numeroSolicitud"));
			projectionList.add( Projections.groupProperty("solicitudes.consecutivoOrdenesMedicas"));
			projectionList.add( Projections.groupProperty("solicitudes.fechaSolicitud"));
			projectionList.add( Projections.groupProperty("solicitudes.urgente"));
			projectionList.add( Projections.groupProperty("centrosCostoByCentroCostoSolicitado.codigo"));
			projectionList.add( Projections.groupProperty("centrosCostoByCentroCostoSolicitado.nombre"));
			projectionList.add( Projections.groupProperty("cuentas.id"));
			
		criteria.setProjection(projectionList);
		
		criteria.addOrder(Property.forName("autorizacionesEntidadesSub.fechaAutorizacion").asc());
		
		criteria.setResultTransformer(Transformers.aliasToBean(DtoAutorizacionEntSubcontratadasCapitacion.class));
		
		ArrayList<DtoAutorizacionEntSubcontratadasCapitacion> listaResultado = new ArrayList<DtoAutorizacionEntSubcontratadasCapitacion>();
		listaResultado = (ArrayList<DtoAutorizacionEntSubcontratadasCapitacion>) criteria.list();

		
		return listaResultado;
	}
	
	/**
	 * 
	 * Este método se encarga de buscar las autorizaciones en estado autorizado 
	 * según un contrato dado
	 *  
	 * @Author Angela Aguirre
	 */
	public ArrayList<AutorizacionesEntidadesSub> obtenerAutorizacionesContratoID(DtoContrato contrato){
		
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				AutorizacionesEntidadesSub.class, "autorizacion");
		
		criteria.createAlias("autorizacion.convenios", "convenio");
		criteria.createAlias("convenio.contratoses", "contrato");		
		
		criteria.add(Restrictions.eq("contrato.codigo", contrato.getCodigo()));
		criteria.add(Restrictions.eq("autorizacion.estado", ConstantesIntegridadDominio.acronimoAutorizado));
		
		ArrayList<AutorizacionesEntidadesSub> lista = (ArrayList<AutorizacionesEntidadesSub>)criteria.list();
		
		return  lista;
		
	}
	
	
	/**
	 * Retorna las Autorizaciones de la EntidadesSubcontratada sin importar su vigencia.
	 * @param DtoAutorizacionEntSubcontratadasCapitacion
	 * @return ArrayList<DtoEntregaMedicamentosInsumosEntSubcontratadas>
	 * 
	 * @author Fabian Becerra
	 */
	public ArrayList<DtoAutorizacionEntSubcontratadasCapitacion> obtenerAutorizacionesPorEntSubSinVigencia(
			DtoAutorizacionEntSubcontratadasCapitacion dtoEntregaMedicamentosInsumosEntSubcontratadas)
	{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(AutorizacionesEntidadesSub.class, "autorizacionesEntidadesSub");
		
		criteria.createAlias("autorizacionesEntidadesSub.entidadesSubcontratadas"		, "entidadesSubcontratadas"	,Criteria.LEFT_JOIN);
		criteria.createAlias("autorizacionesEntidadesSub.pacientes"						, "pacientes"				,Criteria.LEFT_JOIN);
		
		// PermitirAutorizarDiferenteDeSolicitudes
		criteria.createAlias("autorizacionesEntidadesSub.autoEntsubSolicitudeses"		, "autoEntsubSolicitudeses"	,Criteria.LEFT_JOIN);	
		criteria.createAlias("autoEntsubSolicitudeses.solicitudes"						, "solicitudes"				,Criteria.LEFT_JOIN); 
		
		criteria.createAlias("solicitudes.cuentas"										, "cuentas"					,Criteria.LEFT_JOIN);
		criteria.createAlias("solicitudes.centrosCostoByCentroCostoSolicitado"			, "centrosCostoByCentroCostoSolicitado"	,Criteria.LEFT_JOIN);
		criteria.createAlias("autorizacionesEntidadesSub.autorizacionesCapitacionSubs"		, "autorizacionesCapitacionSubs"	,Criteria.LEFT_JOIN);
		criteria.createAlias("autorizacionesCapitacionSubs.autorizacionesEstanciaCapitas"	, "autorizacionesEstanciaCapitas"	,Criteria.LEFT_JOIN);
		criteria.createAlias("autorizacionesEstanciaCapitas.autorizacionesIngreEstancia"	, "autorizacionesIngreEstancia"		,Criteria.LEFT_JOIN);
		
		
		
		// Filtros de Búsqueda -----------------------------------------------------------------------------------------------------------
		
		criteria.add(Restrictions.eq("entidadesSubcontratadas.codigoPk"	, dtoEntregaMedicamentosInsumosEntSubcontratadas.getCodigoEntidadSubcontratada()));
		
		ProjectionList projectionList = Projections.projectionList();
			projectionList.add(Projections.property("entidadesSubcontratadas.codigoPk")				,"codigoEntidadSubcontratada");
			
			projectionList.add(Projections.property("autorizacionesIngreEstancia.codigoPk")			,"autorizacionIngresoEstancia");
		
			projectionList.add(Projections.property("autorizacionesEntidadesSub.consecutivo")		,"autorizacion");
			projectionList.add(Projections.property("autorizacionesEntidadesSub.tipo")				,"tipoAutorizacion");
			projectionList.add(Projections.property("autorizacionesEntidadesSub.fechaAutorizacion")	,"fechaAutorizacion");
			projectionList.add(Projections.property("autorizacionesEntidadesSub.horaAutorizacion")	,"horaAutorizacion"); 
			projectionList.add(Projections.property("autorizacionesEntidadesSub.fechaVencimiento")	,"fechaVencimiento");
			projectionList.add(Projections.property("autorizacionesEntidadesSub.estado")			,"estado");
			projectionList.add(Projections.property("autorizacionesEntidadesSub.observaciones")		,"observacionesGenerales");
			projectionList.add(Projections.property("autorizacionesEntidadesSub.consecutivoAutorizacion")		,"consecutivoAutorizacion");
			
			projectionList.add(Projections.property("solicitudes.numeroSolicitud")					,"numeroOrden");
			projectionList.add(Projections.property("solicitudes.consecutivoOrdenesMedicas")		,"consecutivoOrden");
			projectionList.add(Projections.property("solicitudes.fechaSolicitud")					,"fechaOrden");
			projectionList.add(Projections.property("solicitudes.urgente")							,"urgente");
			
			projectionList.add(Projections.property("centrosCostoByCentroCostoSolicitado.codigo")	,"codigoCentroCostoSolicitado");
			projectionList.add(Projections.property("centrosCostoByCentroCostoSolicitado.nombre")	,"nombreCentroCostoSolicitado");
			
			projectionList.add(Projections.property("cuentas.id")									,"numCuenta");
			
			projectionList.add( Projections.groupProperty("entidadesSubcontratadas.codigoPk"));
			projectionList.add( Projections.groupProperty("autorizacionesIngreEstancia.codigoPk"));
			projectionList.add( Projections.groupProperty("autorizacionesEntidadesSub.consecutivo"));
			projectionList.add( Projections.groupProperty("autorizacionesEntidadesSub.consecutivoAutorizacion"));
			projectionList.add( Projections.groupProperty("autorizacionesEntidadesSub.tipo"));
			projectionList.add( Projections.groupProperty("autorizacionesEntidadesSub.fechaAutorizacion"));
			projectionList.add( Projections.groupProperty("autorizacionesEntidadesSub.horaAutorizacion"));
			projectionList.add( Projections.groupProperty("autorizacionesEntidadesSub.fechaVencimiento"));
			projectionList.add( Projections.groupProperty("autorizacionesEntidadesSub.estado"));
			projectionList.add( Projections.groupProperty("autorizacionesEntidadesSub.observaciones"));
			projectionList.add( Projections.groupProperty("solicitudes.numeroSolicitud"));
			projectionList.add( Projections.groupProperty("solicitudes.consecutivoOrdenesMedicas"));
			projectionList.add( Projections.groupProperty("solicitudes.fechaSolicitud"));
			projectionList.add( Projections.groupProperty("solicitudes.urgente"));
			projectionList.add( Projections.groupProperty("centrosCostoByCentroCostoSolicitado.codigo"));
			projectionList.add( Projections.groupProperty("centrosCostoByCentroCostoSolicitado.nombre"));
			projectionList.add( Projections.groupProperty("cuentas.id"));
			
			
		criteria.setProjection(projectionList);
		
		criteria.addOrder(Property.forName("autorizacionesEntidadesSub.fechaAutorizacion").asc());
		
		criteria.setResultTransformer(Transformers.aliasToBean(DtoAutorizacionEntSubcontratadasCapitacion.class));
		
		ArrayList<DtoAutorizacionEntSubcontratadasCapitacion> listaResultado = new ArrayList<DtoAutorizacionEntSubcontratadasCapitacion>();
		listaResultado = (ArrayList<DtoAutorizacionEntSubcontratadasCapitacion>) criteria.list();

		
		return listaResultado;
	}
	
	
	
	/**
	 * 
	 * Este Método se encarga de consultar las autorizaciones entidades subcontratadas y de capitacion generadas para cada 
	 * convenio-contrato por dia en un rango de fechas determinado.
	 * Proceso Autorizaciones anexo 1027  
	 * 
	 * @param DTOProcesoPresupuestoCapitado dto
	 * @return ArrayList<DtoConsultaProcesoAutorizacion>
	 * @author Camilo Gómez 
	 *
	 */
	@SuppressWarnings({ "static-access" })
	public ArrayList<DtoConsultaProcesoAutorizacion> obtenerAutorizacionesEntSubServiArti(DtoProcesoPresupuestoCapitado dto){
		
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				AutorizacionesEntidadesSub.class, "autorizacionEntidadSub");	
		
		criteria.createAlias("autorizacionEntidadSub.autorizacionesCapitacionSubs"	,"autorizacionCapitacion",criteria.INNER_JOIN);
		//criteria.createAlias("autorizacionEntidadSub.convenios"						,"convenio");
		//criteria.createAlias("convenio.contratoses"									,"contrato");
		criteria.createAlias("autorizacionEntidadSub.pacientes"						,"paciente");//-
		criteria.createAlias("paciente.personas"									,"persona");//-
		criteria.createAlias("persona.usuarioXConvenios"							,"usuarioConvenio");//-
		criteria.createAlias("usuarioConvenio.contratos"							,"contrato");//-
		criteria.createAlias("contrato.convenios"									,"convenio");//-
		criteria.createAlias("convenio.tiposContrato"								,"tipoContrato");//-
		
		criteria.createAlias("autorizacionEntidadSub.autorizacionesEntSubArticus"	,"autorizArticulo",criteria.LEFT_JOIN);
		criteria.createAlias("autorizacionEntidadSub.autorizacionesEntSubServis"	,"autorizServicio",criteria.LEFT_JOIN);
		criteria.createAlias("autorizServicio.servicios"							,"servicio",criteria.LEFT_JOIN);
		criteria.createAlias("servicio.referenciasServicios"						,"referenciaServicio",criteria.LEFT_JOIN);
		criteria.createAlias("autorizArticulo.articulo"								,"articulo",criteria.LEFT_JOIN);
		criteria.createAlias("articulo.nivelAtencion"								,"nivelAtencionArticulo",criteria.LEFT_JOIN);
		criteria.createAlias("servicio.nivelAtencion"								,"nivelAtencionServicio",criteria.LEFT_JOIN);
		criteria.createAlias("servicio.gruposServicios"								,"grupoServicio",criteria.LEFT_JOIN);
		
		if(dto.getFechaInicio()!=null && dto.getFechaFin()!=null)		
		{
			criteria.add(Restrictions.between("autorizacionEntidadSub.fechaAutorizacion",dto.getFechaInicio(),dto.getFechaFin()));
		}		
		
		/*****AGREGA PARA ANULADAS***/
		Disjunction disjunctionAnulacion = Restrictions.disjunction();  
		disjunctionAnulacion.add( Restrictions.neProperty("autorizacionEntidadSub.fechaAutorizacion", "autorizacionEntidadSub.fechaAnulacion"));  
		disjunctionAnulacion.add( Property.forName("autorizacionEntidadSub.fechaAnulacion").isNull());
		criteria.add(disjunctionAnulacion);
		/***************************/
		
		criteria.add(Restrictions.eq("tipoContrato.codigo",ConstantesBD.codigoTipoContratoCapitado));
		//criteria.add(Restrictions.isNotNull("convenio.capitacionSubcontratada"));
		criteria.add(Restrictions.eq("convenio.capitacionSubcontratada",ConstantesBD.acronimoSiChar));
		//*****criteria.add(Restrictions.eq("autorizacionEntidadSub.estado",ConstantesIntegridadDominio.acronimoAutorizado));
		
		/*Disjunction disjunction = Restrictions.disjunction();  
			disjunction.add( Property.forName("autorizacionCapitacion.indicativoTemporal").isNull());  
			disjunction.add( Property.forName("autorizacionCapitacion.indicativoTemporal").eq(ConstantesBD.acronimoNoChar));
		criteria.add(disjunction);*/
		criteria.add(Restrictions.eq("autorizacionCapitacion.indicativoTemporal",ConstantesBD.acronimoNoChar));
		
		
		if(dto.getConvenio()!=null)
		{
			criteria.add(Restrictions.eq("convenio.codigo",dto.getConvenio()));
			
			if(dto.getContrato()!=null)
			{
				criteria.add(Restrictions.eq("contrato.codigo",dto.getContrato()));	 			
			}
		}
		
		Integer tipoTarifario = Integer.parseInt(ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(dto.getInstitucion()));
		Disjunction disjunction2 = Restrictions.disjunction();
		disjunction2.add( Property.forName("referenciaServicio.id.tipoTarifario").isNull());  
		disjunction2.add( Property.forName("referenciaServicio.id.tipoTarifario").eq(tipoTarifario));
		criteria.add(disjunction2);
		
		Log4JManager.info("#################################   Paramteros para la consulta Autorizaciones  #################################"
											+"\n-->autorizacionEntidadSub.fechaAutorizacion(Inicio)	: "+dto.getFechaInicio()
											+"\n-->autorizacionEntidadSub.fechaAutorizacion(Fin) 	: "+dto.getFechaFin()
											+"\n-->tipoContrato.codigo					: "+ConstantesBD.codigoTipoContratoCapitado
											+"\n-->convenio.capitacionSubcontratada			: "+ConstantesBD.acronimoSiChar
											//+"\n-->autorizacionEntidadSub.estado			: "+ConstantesIntegridadDominio.acronimoAutorizado
											+"\n-->autorizacionCapitacion.indicativoTemporal		: "+ConstantesBD.acronimoNoChar
											+"\n-->convenio.codigo					: "+dto.getConvenio()
											+"\n-->contrato.codigo					: "+dto.getContrato()
											+"\n-->Para el tipoTarifario (Intitucion: "+dto.getInstitucion()+")"
											+"\n-->tipotarifario					: "+tipoTarifario
		);
		
		
		ProjectionList projectionList = Projections.projectionList();		
			projectionList.add(Projections.property("convenio.codigo")							,"convenio");
			projectionList.add(Projections.property("contrato.codigo")							,"contrato");
			projectionList.add(Projections.property("autorizacionEntidadSub.fechaAutorizacion")	,"fecha");
			projectionList.add(Projections.property("autorizacionEntidadSub.fechaAnulacion")	,"fechaAnulacion");
			
			
			//ARTICULOS
			projectionList.add(Projections.property("articulo.codigo")							,"codigoArticulo");
			projectionList.add(Projections.property("articulo.descripcion")						,"nombreArticulo");
			projectionList.add(Projections.property("autorizArticulo.nroDosisTotal")			,"cantidadArticulo");
			projectionList.add(Projections.property("autorizArticulo.valorTarifa")				,"tarifaArticulo");
			projectionList.add(Projections.property("nivelAtencionArticulo.descripcion")		,"nivelAtencionArticulo");
			projectionList.add(Projections.property("nivelAtencionArticulo.consecutivo")		,"codNivelAtencionArticulo");
			projectionList.add(Projections.property("articulo.subgrupo")						,"subGrupoInventario");
			//SERVICIOS
			projectionList.add(Projections.property("servicio.codigo")							,"codigoServicio");
			projectionList.add(Projections.property("referenciaServicio.descripcion")			,"nombreServicio");	
			projectionList.add(Projections.property("autorizServicio.cantidad")					,"cantidadServicio");
			projectionList.add(Projections.property("autorizServicio.valorTarifa")				,"tarifaServicio");
			projectionList.add(Projections.property("nivelAtencionServicio.descripcion")		,"nivelAtencionServicio");
			projectionList.add(Projections.property("nivelAtencionServicio.consecutivo")		,"codNivelAtencionServicio");
			projectionList.add(Projections.property("grupoServicio.descripcion")				,"grupoServicio");
			projectionList.add(Projections.property("grupoServicio.codigo")						,"codGrupoServicio");
		
		criteria.addOrder(Order.asc("autorizacionEntidadSub.fechaAutorizacion"));
		criteria.addOrder(Order.asc("convenio.codigo"));

		
		criteria.setProjection(projectionList);
		
		criteria.setResultTransformer( Transformers.aliasToBean(DtoConsultaProcesoAutorizacion.class));
		
		ArrayList<DtoConsultaProcesoAutorizacion> listaResultado = (ArrayList<DtoConsultaProcesoAutorizacion>) criteria.list();

		return listaResultado;
	}
	
	
	/** 
	 * Este Método se encarga de consultar las ordenes autorizadas a entidades subcontratadas  
	 * Anexo 925
	 * 
	 * @param DtoBusquedaTotalOrdenesAutorizadasEntSub dto
	 * @return ArrayList<DtoConsultaTotalOrdenesAutorizadasEntSub>
	 * @author Camilo Gómez 
	 *
	 */
	@SuppressWarnings({ "static-access" })
	public ArrayList<DtoConsultaTotalOrdenesAutorizadasEntSub> obtenerOrdenesAutorizadasEntSub(DtoBusquedaTotalOrdenesAutorizadasEntSub dto){
		
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				AutorizacionesEntidadesSub.class, "autorizacionEntidadSub");	
		
		/*criteria.createAlias("autorizacionEntidadSub.convenios"						,"convenio");
		criteria.createAlias("autorizacionEntidadSub.entidadesSubcontratadas"		,"entidadSub");
		criteria.createAlias("convenio.tiposContrato"								,"tipoContrato");
		criteria.createAlias("convenio.contratoses"									,"contrato");
		criteria.createAlias("autorizacionEntidadSub.autorizacionesEntSubArticus"	,"autorizArticulo",criteria.LEFT_JOIN);
		criteria.createAlias("autorizacionEntidadSub.autorizacionesEntSubServis"	,"autorizServicio",criteria.LEFT_JOIN);
		criteria.createAlias("autorizServicio.servicios"							,"servicio",criteria.LEFT_JOIN);
		criteria.createAlias("servicio.referenciasServicios"						,"referenciaServicio",criteria.LEFT_JOIN);
		criteria.createAlias("autorizArticulo.articulo"								,"articulo",criteria.LEFT_JOIN);
		criteria.createAlias("articulo.nivelAtencion"								,"nivelAtencionArticulo",criteria.LEFT_JOIN);
		criteria.createAlias("servicio.nivelAtencion"								,"nivelAtencionServicio",criteria.LEFT_JOIN);
		criteria.createAlias("servicio.gruposServicios"								,"grupoServicio",criteria.LEFT_JOIN);*/
		
		criteria.createAlias("autorizacionEntidadSub.convenios"						,"convenio");
		criteria.createAlias("autorizacionEntidadSub.entidadesSubcontratadas"		,"entidadSub");
		criteria.createAlias("convenio.tiposContrato"								,"tipoContrato");
		criteria.createAlias("convenio.contratoses"									,"contrato");
		criteria.createAlias("autorizacionEntidadSub.autorizacionesEntSubArticus"	,"autorizArticulo",criteria.LEFT_JOIN);
		criteria.createAlias("autorizacionEntidadSub.autorizacionesEntSubServis"	,"autorizServicio",criteria.LEFT_JOIN);
		criteria.createAlias("autorizServicio.servicios"							,"servicio",criteria.LEFT_JOIN);
		criteria.createAlias("servicio.referenciasServicios"						,"referenciaServicio",criteria.LEFT_JOIN);
		criteria.createAlias("autorizArticulo.articulo"								,"articulo",criteria.LEFT_JOIN);
		criteria.createAlias("articulo.nivelAtencion"								,"nivelAtencionArticulo",criteria.LEFT_JOIN);
		criteria.createAlias("servicio.nivelAtencion"								,"nivelAtencionServicio",criteria.LEFT_JOIN);
		criteria.createAlias("servicio.gruposServicios"								,"grupoServicio",criteria.LEFT_JOIN);
		
		criteria.createAlias("autorizacionEntidadSub.tarifasEntidadSubs"			,"tarifasEntidadSub");
		
		
		//FILTROS DE BUSQUEDA
		if(dto.getFechaInicioBusqueda()!=null && dto.getFechaFinBusqueda()!=null)		
		{
			Date fechaFin=new Date();
			try{					
				Calendar fechaCalendar=Calendar.getInstance();
				fechaCalendar.setTime(dto.getFechaFinBusqueda());
				fechaCalendar.add(Calendar.DAY_OF_MONTH, 1);
				fechaFin=fechaCalendar.getTime();
				//dto.setFechaFinBusqueda(fechaCalendar.getTime());
				
			} catch (Exception e) {
				Log4JManager.error("Error cambiando el formato de la fecha", e);
				e.printStackTrace();
			}
			
			criteria.add(Restrictions.between("autorizacionEntidadSub.fechaAutorizacion",dto.getFechaInicioBusqueda(),fechaFin));
		}		
		
		Log4JManager.info("longitud del arreglo de autorizaciones-->"+dto.getEstadosAutorizacion().length);
		if(dto.getEstadosAutorizacion().length > 0 )
		{			
			criteria.add(Restrictions.in("autorizacionEntidadSub.estado", dto.getEstadosAutorizacion()));	
		}
		
		if(dto.getCodigoEntidadSub()!=null && dto.getCodigoEntidadSub()!=ConstantesBD.codigoNuncaValidoLong)
		{
			criteria.add(Restrictions.eq("entidadSub.codigoPk", dto.getCodigoEntidadSub()));
		}
		
		if(dto.getConvenio()!=null && dto.getConvenio()!=ConstantesBD.codigoNuncaValidoLong)
		{
			criteria.add(Restrictions.eq("convenio.codigo", dto.getConvenio()));
		}
		
		
		
		Integer tipoTarifario = Integer.parseInt(ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(dto.getInstitucion()));
		Disjunction disjunction2 = Restrictions.disjunction();
		disjunction2.add( Property.forName("referenciaServicio.id.tipoTarifario").isNull());  
		disjunction2.add( Property.forName("referenciaServicio.id.tipoTarifario").eq(tipoTarifario));
		criteria.add(disjunction2);
		
		Disjunction disjunctionTarifas = Restrictions.disjunction();
		//6411 SE CAMBIA EL NOMBRE DEL ATRIBUTO articulo POR articuloByArticulo
		disjunctionTarifas.add( Property.forName("tarifasEntidadSub.articuloByArticulo").isNull());  
		disjunctionTarifas.add( Restrictions.eqProperty("tarifasEntidadSub.articuloByArticulo","articulo.codigo"));
		criteria.add(disjunctionTarifas);
		
		Log4JManager.info("#################################   Paramteros para la consulta Ordenes Autorizadas a Entidades Sub  #################################"
											+"\n-->autorizacionEntidadSub.fechaAutorizacion(Inicio)	: "+dto.getFechaInicioBusqueda()
											+"\n-->autorizacionEntidadSub.fechaAutorizacion(Fin) 	: "+dto.getFechaFinBusqueda()
											+"\n-->autorizacionEntidadSub.entidadesSubcontratadas	: "+dto.getCodigoEntidadSub()
											+"\n-->convenio.codigo					: "+dto.getConvenio()
											+"\n-->Para el tipoTarifario (Intitucion: "+dto.getInstitucion()+")"
											+"\n-->tipotarifario					: "+tipoTarifario
		);
		if(dto.getEstadosAutorizacion().length > 0)
		{
			for (String esta : dto.getEstadosAutorizacion()) {
				Log4JManager.info("-->autorizacionEntidadSub.estado	:"+esta);
			}
		}
		
		
		criteria.setProjection(Projections.distinct(Projections.projectionList()		
			//AUTORIZACIONentidades
			.add(Projections.property("autorizacionEntidadSub.consecutivo")			,"consecutivo")			
			.add(Projections.property("autorizacionEntidadSub.fechaAutorizacion")	,"fechaAutorizacion")
			.add(Projections.property("autorizacionEntidadSub.horaAutorizacion")	,"horaAutorizacion")
			.add(Projections.property("autorizacionEntidadSub.estado")				,"estadoAutorizacion")
			.add(Projections.property("entidadSub.codigoPk")						,"codigoEntidadSub")
			.add(Projections.property("entidadSub.razonSocial")						,"nombreEntidadSub")
			.add(Projections.property("convenio.codigo")							,"codigoConvenio")
			.add(Projections.property("convenio.nombre")							,"nombreConvenio")
			
			//ARTICULOS
			.add(Projections.property("articulo.codigo")						,"codigoArticulo")
			.add(Projections.property("articulo.descripcion")					,"nombreArticulo")
			.add(Projections.property("autorizArticulo.nroDosisTotal")			,"cantidadArticulo")
			//.add(Projections.property("autorizArticulo.valorTarifa")			,"valorArticulo")
			.add(Projections.property("nivelAtencionArticulo.descripcion")		,"nivelAtencionArticulo")
			//.add(Projections.property("nivelAtencionArticulo.consecutivo")		,"codNivelAtencionArticulo")
			.add(Projections.property("articulo.subgrupo")						,"subGrupoInventario")
			//SERVICIOS
			.add(Projections.property("servicio.codigo")						,"codigoServicio")
			.add(Projections.property("referenciaServicio.descripcion")			,"nombreServicio")
			.add(Projections.property("autorizServicio.cantidad")				,"cantidadServicio")
			//.add(Projections.property("autorizServicio.valorTarifa")			,"valorServicio")
			.add(Projections.property("nivelAtencionServicio.descripcion")		,"nivelAtencionServicio")
			//.add(Projections.property("nivelAtencionServicio.consecutivo")		,"codNivelAtencionServicio")
			.add(Projections.property("grupoServicio.descripcion")				,"grupoServicio")
			//.add(Projections.property("grupoServicio.codigo")					,"codGrupoServicio")
			
			.add(Projections.property("tarifasEntidadSub.valorUnitario")		,"valorUnitario")
			));
		
		
		
		criteria.addOrder(Order.asc("entidadSub.razonSocial"));
		criteria.addOrder(Order.asc("convenio.nombre"));
		criteria.addOrder(Order.asc("nivelAtencionServicio.descripcion"));
		criteria.addOrder(Order.asc("nivelAtencionArticulo.descripcion"));
	
		
		criteria.setResultTransformer( Transformers.aliasToBean(DtoConsultaTotalOrdenesAutorizadasEntSub.class));
		
	
		ArrayList<DtoConsultaTotalOrdenesAutorizadasEntSub> listaResultado = (ArrayList<DtoConsultaTotalOrdenesAutorizadasEntSub>) criteria.list();

		return listaResultado;
	}
	
	/**
	 * Consulta
	 * @param args
	 */
	public static void main(String[] args) {
		AutorizacionesEntidadesSubDelegate delegate=new AutorizacionesEntidadesSubDelegate();
		//consulta para anexo 1027
		/*DtoProcesoPresupuestoCapitado dtoProceso=new DtoProcesoPresupuestoCapitado();
		dtoProceso.setFechaInicio(UtilidadFecha.conversionFormatoFechaStringDate("15/04/2011"));
		dtoProceso.setFechaFin(UtilidadFecha.conversionFormatoFechaStringDate("15/04/2011"));
		dtoProceso.setInstitucion(2);
			delegate.obtenerAutorizacionesEntSubServiArti(dtoProceso);*/
		//consulta para anexo 925
		DtoBusquedaTotalOrdenesAutorizadasEntSub dto=new DtoBusquedaTotalOrdenesAutorizadasEntSub();		
		dto.setFechaInicioBusqueda(UtilidadFecha.conversionFormatoFechaStringDate("20/04/2011"));
		dto.setFechaFinBusqueda(UtilidadFecha.conversionFormatoFechaStringDate("20/04/2011"));
		dto.setInstitucion(2);
			delegate.obtenerOrdenesAutorizadasEntSub(dto);
			
			
	}

}
