/**
 * 
 */
package com.servinte.axioma.orm.delegate.ordenes;

import java.util.ArrayList;
import java.util.Calendar;

import org.axioma.util.log.Log4JManager;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadFecha;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.dto.capitacion.DtoProcesoPresupuestoCapitado;
import com.princetonsa.dto.capitacion.DtoResultadoConsultaProcesosCierre;
import com.princetonsa.dto.inventario.DtoArticulosAutorizaciones;
import com.princetonsa.dto.inventario.DtoServiciosAutorizaciones;
import com.princetonsa.dto.manejoPaciente.DtoPacientesPoliconsultadores;
import com.princetonsa.dto.ordenes.DtoSolicitud;
import com.servinte.axioma.hibernate.HibernateUtil;
import com.servinte.axioma.orm.Solicitudes;
import com.servinte.axioma.orm.SolicitudesHome;

/**
 * @author Cristhian Murillo
 */
@SuppressWarnings("unchecked")
public class SolicitudesDelegate extends SolicitudesHome 
{
	
	/**
	 * Implementacion del findById
	 * @param id
	 * @return Solicitudes
	 */
	public Solicitudes obtenerSolicitudPorId(int id) {
		return super.findById(id);
	}
	
	
	
	/**
	 * Retorna las solicitudes por cuenta o rango.
	 * Hace un filtro por los parametros recibidos de DtoSolicitud.
	 * Si la Solicitud No tenga asociadas Autorizaciones Capitación Subcontrada o si tienen  se encuentre en estado Anulada.
	 * Si no se le envia cuenta carga todas las solicitudes por rango de las fechas
	 * 
	 * @param parametros
	 * @return ArrayList<DtoSolicitud>
	 * @autor Cristhian Murillo.
	 */
	public ArrayList<DtoSolicitud> obtenerSolicitudesPorCuentaORango(DtoSolicitud parametros,String codigoEstado)
	{
		//FIXME MODICIACION DE PARAMETRO 
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Solicitudes.class,"solicitudes");
	
		criteria.createAlias("solicitudes.cuentas"								, "cuentas"						,Criteria.LEFT_JOIN);
		criteria.createAlias("cuentas.pacientes"								, "pacientes"					,Criteria.LEFT_JOIN);
		criteria.createAlias("cuentas.viasIngreso"								, "viasIngreso"					,Criteria.LEFT_JOIN);
		criteria.createAlias("cuentas.tiposPaciente"							, "tiposPaciente"				,Criteria.LEFT_JOIN);
		
		criteria.createAlias("cuentas.centrosCosto"								, "centrosCosto"				,Criteria.LEFT_JOIN);
		criteria.createAlias("centrosCosto.centroAtencion"						, "centroAtencion"				,Criteria.LEFT_JOIN);
		
		criteria.createAlias("pacientes.personas"								, "personas"					,Criteria.LEFT_JOIN);
		criteria.createAlias("personas.tiposIdentificacion"						, "tiposIdentificacion"			,Criteria.LEFT_JOIN);
		criteria.createAlias("pacientes.centroAtencionByCentroAtencionPyp"		, "centroAtencionPyp"			,Criteria.LEFT_JOIN);
		
		criteria.createAlias("solicitudes.centrosCostoByCentroCostoSolicitante"	, "centrosCostoByCentroCostoSolicitante");
		criteria.createAlias("solicitudes.centrosCostoByCentroCostoSolicitado"	, "centrosCostoByCentroCostoSolicitado");
		
		// PermitirAutorizarDiferenteDeSolicitudes
		criteria.createAlias("solicitudes.autoEntsubSolicitudeses"					, "autoEntsubSolicitudeses"		,Criteria.LEFT_JOIN); 
		criteria.createAlias("autoEntsubSolicitudeses.autorizacionesEntidadesSub"	, "autorizacionesEntidadesSubs"	,Criteria.LEFT_JOIN);
													  
		criteria.createAlias("solicitudes.solicitudesPosponers"					, "solicitudesPosponers"		,Criteria.LEFT_JOIN);
		
		criteria.createAlias("solicitudes.solicitudesSubcuentas"				, "solicitudesSubcuentas"		,Criteria.LEFT_JOIN);
		criteria.createAlias("solicitudesSubcuentas.subCuentas"					, "subCuentas"					,Criteria.LEFT_JOIN);
		criteria.createAlias("subCuentas.convenios"								, "convenios"					,Criteria.INNER_JOIN);
		criteria.createAlias("convenios.contratoses"							, "contratoses"					,Criteria.INNER_JOIN);
		criteria.createAlias("convenios.tiposContrato"							, "tiposContrato");
		
		criteria.createAlias("subCuentas.estratosSociales"						, "estratosSociales"			,Criteria.LEFT_JOIN);
		criteria.createAlias("subCuentas.tiposAfiliado"							, "tiposAfiliado"				,Criteria.LEFT_JOIN);
		criteria.createAlias("subCuentas.naturalezaPacientes"					, "naturalezaPacientes"			,Criteria.LEFT_JOIN);
		
		criteria.createAlias("subCuentas.contratos"								, "contratosSubCuenta"			,Criteria.INNER_JOIN); // Contrato de la cuetna del paciente
		
		
		// -------------------------------------------------------------------------------------------------------------------
		if(parametros.isConveniosCapitacionSubcontratadaVigentes())
		{
			criteria.add(Restrictions.eq("convenios.capitacionSubcontratada", 	ConstantesBD.acronimoSiChar));
			criteria.add(Restrictions.eq("tiposContrato.codigo", 				ConstantesBD.codigoTipoContratoCapitado));
			criteria.add(Restrictions.gt("contratoses.fechaFinal", 				UtilidadFecha.getFechaActualTipoBD()));
		}
		
		if(parametros.isCargoDirectoEstadoCargado())
		{
			// estadosSolFact = 3 
			criteria.createAlias("solicitudes.detCargoses"			, "detCargoses"		, Criteria.INNER_JOIN);
			criteria.createAlias("detCargoses.estadosSolFact"		, "estadosSolFact"	, Criteria.INNER_JOIN);
			criteria.add(Restrictions.eq("estadosSolFact.codigo"	,  ConstantesBD.estadoSolFactCargada));
		}
		
		if(parametros.isCargoDirectoEstadoPendiente())
		{
			// estadosSolFact = 1 
			criteria.createAlias("solicitudes.detCargoses"			, "detCargoses"		, Criteria.INNER_JOIN);
			criteria.createAlias("detCargoses.estadosSolFact"		, "estadosSolFact"	, Criteria.INNER_JOIN); 
			criteria.add(Restrictions.eq("estadosSolFact.codigo"	,  ConstantesBD.estadoSolFactPendiente));
		}
		
		/*if(parametros.isNoTieneCargo())
		{
			criteria.createAlias("solicitudes.detCargoses"			, "detCargoses"		, Criteria.LEFT_JOIN);
			parametros.setNroPrioridad(null);
		}*/
		
		
		if(parametros.getEstadoHistoriaClinicaIgual() != null)
		{
			criteria.add(Restrictions.in("solicitudes.estadoHistoriaClinica", parametros.getEstadoHistoriaClinicaIgual()));//1 o 7
		}
		
		
		if(parametros.getCodigoCuenta() != null)
		{
			criteria.add(Restrictions.eq("cuentas.id", parametros.getCodigoCuenta()));
			
		}
		if( (parametros.getFechaSolicitud() != null) && (parametros.getFechaFinalSolicitud() != null) )
		{
			criteria.add(Restrictions.between("solicitudes.fechaSolicitud",
					parametros.getFechaSolicitud(), parametros.getFechaFinalSolicitud()));
		}
		
		// -------------------------------------------------------------------------------------------------------------------
		
		if(parametros.getNroPrioridad() != null)
		{
			criteria.add(Restrictions.eq("subCuentas.nroPrioridad", parametros.getNroPrioridad()));
		}
		
		if(!codigoEstado.equals(ConstantesIntegridadDominio.acronimoAutorizado)){
		// estado = ANU OR estado IS NULL(cuando no tiene autorizacion)
		Disjunction disjunctionOR = Restrictions.disjunction();  
			disjunctionOR.add( Property.forName("autorizacionesEntidadesSubs.estado").isNull());  //codigoEstado
			disjunctionOR.add( Property.forName("autorizacionesEntidadesSubs.estado").eq(codigoEstado));
		criteria.add(disjunctionOR);
		//----------------------------------------------------------------------------
		}else{
			Disjunction disjunctionOR = Restrictions.disjunction();  
			disjunctionOR.add( Property.forName("autorizacionesEntidadesSubs.estado").isNotNull());  //codigoEstado
			disjunctionOR.add( Property.forName("autorizacionesEntidadesSubs.estado").eq(codigoEstado));
		criteria.add(disjunctionOR);
		}
		
		//Date fechaPosponer=UtilidadFecha.conversionFormatoFechaStringDate(UtilidadFecha.incrementarMesesAFecha(UtilidadFecha.getFechaActual(),-1,false));
		
		// solicitudesPosponers
		/*Disjunction disjunctionORPosponer = Restrictions.disjunction();  
		disjunctionORPosponer.add( Property.forName("solicitudesPosponers.fechaPosponer").isNull());  
		disjunctionORPosponer.add( Property.forName("solicitudesPosponers.fechaPosponer").le(fechaPosponer));
		criteria.add(disjunctionORPosponer);*/
		
		/*String sql="(solicitude14_.fecha_posponer is null or ((select extract(month from to_date(solicitude14_.fecha_posponer,'dd/MM/YY')) from dual)<=(select extract(month from CURRENT_DATE) from dual) and (select extract(YEAR from to_date(solicitude14_.fecha_posponer,'dd/MM/YY')) from dual)<=(select extract(YEAR from CURRENT_DATE) from dual))) ";
		criteria.add(Restrictions.sqlRestriction(sql));*/
		
		//criteria.add(Restrictions.sqlRestriction("(solicitude14_.fecha_posponer is null or solicitude14_.fecha_posponer <=?)", UtilidadFecha.getFechaActualTipoBD(), Hibernate.DATE));
		//----------------------------------------------------------------------------
		
		if(parametros.getCodigoConvenio()!=null && parametros.getCodigoConvenio()!=ConstantesBD.codigoNuncaValido)
		{
			criteria.add(Restrictions.eq("convenios.codigo", parametros.getCodigoConvenio()));
		}
		
		//----------------------------------------------------------------------------
		
		ProjectionList projectionList = Projections.projectionList();
			// --> Datos solicitud
				projectionList.add(Projections.property("solicitudes.numeroSolicitud")				,"numeroSolicitud");
				projectionList.add(Projections.property("solicitudes.consecutivoOrdenesMedicas")	,"consecutivoOrdenesMedicas");
				projectionList.add(Projections.property("solicitudes.fechaSolicitud")				,"fechaSolicitud");
				projectionList.add(Projections.property("solicitudes.horaSolicitud")				,"horaSolicitud");
				projectionList.add(Projections.property("solicitudes.urgente")						,"urgente");
				projectionList.add(Projections.property("solicitudesPosponers.codigoPk")			,"codigoPosponer");
				projectionList.add(Projections.property("solicitudesPosponers.fechaPosponer")		,"fechaPosponer");
				projectionList.add(Projections.property("solicitudes.tipo")							,"tipoSolicitud");
				
			// --> Datos Autorización (en el caso de tener)
				projectionList.add(Projections.property("autorizacionesEntidadesSubs.consecutivo")	,"consecutivoPkAutorizacionAsociada");
				projectionList.add(Projections.property("autorizacionesEntidadesSubs.estado")		,"estadoAutorizacionAsociada");
			// --> Datos Paciente/Persona	
				projectionList.add(Projections.property("tiposIdentificacion.acronimo")				,"tipoId");
				projectionList.add(Projections.property("personas.numeroIdentificacion")			,"numeroId");
				projectionList.add(Projections.property("personas.primerNombre")					,"primerNombre");
				projectionList.add(Projections.property("personas.segundoNombre")					,"segundoNombre");
				projectionList.add(Projections.property("personas.primerApellido")					,"primerApellido");
				projectionList.add(Projections.property("personas.segundoApellido")					,"segundoApellido");
				projectionList.add(Projections.property("personas.fechaNacimiento")					,"fechaNacimiento");
				projectionList.add(Projections.property("personas.codigo")							,"codPersona");
				projectionList.add(Projections.property("centroAtencionPyp.consecutivo")			,"centroAtencionAsignado");
			// --> Datos Convenio/Contrato	
				projectionList.add(Projections.property("convenios.codigo")							,"codigoConvenio");
				projectionList.add(Projections.property("convenios.nombre")							,"nombreConvenio");
				projectionList.add(Projections.property("tiposContrato.nombre")						,"nombreTipoContrato");
			// --> Datos Centros Costo de la Solicitud	
				projectionList.add(Projections.property("centrosCostoByCentroCostoSolicitante.codigo")	,"codigoCentroCostoSolicitante");
				projectionList.add(Projections.property("centrosCostoByCentroCostoSolicitante.nombre")	,"nombreCentroCostoSolicitante");
				projectionList.add(Projections.property("centrosCostoByCentroCostoSolicitado.codigo")	,"codigoCentroCostoSolicitado");
				projectionList.add(Projections.property("centrosCostoByCentroCostoSolicitado.nombre")	,"nombreCentroCostoSolicitado");
			// --> Datos Cuenta Paciente/Persona	
				projectionList.add(Projections.property("cuentas.id")				,"codigoCuenta");
				projectionList.add(Projections.property("viasIngreso.codigo")		,"codViaIngreso");
				projectionList.add(Projections.property("tiposPaciente.acronimo")	,"tipoPaciente");
				
				projectionList.add(Projections.property("estratosSociales.codigo")			,"codigoEstratoSocial"); 
				projectionList.add(Projections.property("estratosSociales.descripcion")		,"descripcionEstratoSocial");
				projectionList.add(Projections.property("tiposAfiliado.nombre")				,"nombreTipoAfiliado");
				projectionList.add(Projections.property("subCuentas.semanasCotizacion")		,"semanasCotizacion");
				projectionList.add(Projections.property("subCuentas.tipoMontoCobro")		,"tipoMontoCobro");
				projectionList.add(Projections.property("subCuentas.porcentajeAutorizado")	,"porcentajeAutorizado");
				projectionList.add(Projections.property("subCuentas.montoAutorizado")		,"montoAutorizado");
				projectionList.add(Projections.property("subCuentas.nroPrioridad")			,"nroPrioridad");
				projectionList.add(Projections.property("naturalezaPacientes.codigo")		,"codigoNaturalezaPaciente"); 
				
				projectionList.add(Projections.property("centroAtencion.consecutivo")		,"centroAtencionCuenta");
				
				projectionList.add(Projections.property("contratosSubCuenta.codigo")		,"contratoSubcuenta");
				projectionList.add(Projections.property("tiposAfiliado.acronimo")			,"acronimoTipoAfiliado");
				
				//-------------------
				projectionList.add( Projections.groupProperty("solicitudes.numeroSolicitud"));
				projectionList.add( Projections.groupProperty("solicitudes.consecutivoOrdenesMedicas"));
				projectionList.add( Projections.groupProperty("solicitudes.fechaSolicitud"));
				projectionList.add( Projections.groupProperty("solicitudes.horaSolicitud"));
				projectionList.add( Projections.groupProperty("solicitudes.urgente"));
				projectionList.add( Projections.groupProperty("solicitudes.tipo"));
				projectionList.add( Projections.groupProperty("solicitudesPosponers.codigoPk"));
				projectionList.add( Projections.groupProperty("solicitudesPosponers.fechaPosponer"));
				projectionList.add( Projections.groupProperty("autorizacionesEntidadesSubs.consecutivo"));
				projectionList.add( Projections.groupProperty("autorizacionesEntidadesSubs.estado"));
				projectionList.add( Projections.groupProperty("tiposIdentificacion.acronimo"));
				projectionList.add( Projections.groupProperty("personas.numeroIdentificacion"));
				projectionList.add( Projections.groupProperty("personas.primerNombre"));
				projectionList.add( Projections.groupProperty("personas.segundoNombre"));
				projectionList.add( Projections.groupProperty("personas.primerApellido"));
				projectionList.add( Projections.groupProperty("personas.segundoApellido"));
				projectionList.add( Projections.groupProperty("personas.fechaNacimiento"));
				projectionList.add( Projections.groupProperty("personas.codigo"));
				projectionList.add( Projections.groupProperty("centroAtencionPyp.consecutivo"));
				projectionList.add( Projections.groupProperty("convenios.codigo"));
				projectionList.add( Projections.groupProperty("convenios.nombre"));
				projectionList.add( Projections.groupProperty("tiposContrato.nombre"));
				projectionList.add( Projections.groupProperty("centrosCostoByCentroCostoSolicitante.codigo"));
				projectionList.add( Projections.groupProperty("centrosCostoByCentroCostoSolicitante.nombre"));
				projectionList.add( Projections.groupProperty("centrosCostoByCentroCostoSolicitado.codigo"));
				projectionList.add( Projections.groupProperty("centrosCostoByCentroCostoSolicitado.nombre"));
				projectionList.add( Projections.groupProperty("cuentas.id"));
				projectionList.add( Projections.groupProperty("viasIngreso.codigo"));
				projectionList.add( Projections.groupProperty("tiposPaciente.acronimo"));
				projectionList.add( Projections.groupProperty("estratosSociales.codigo"));
				projectionList.add( Projections.groupProperty("estratosSociales.descripcion"));
				projectionList.add( Projections.groupProperty("tiposAfiliado.nombre"));
				projectionList.add( Projections.groupProperty("subCuentas.semanasCotizacion"));
				projectionList.add( Projections.groupProperty("subCuentas.tipoMontoCobro"));
				projectionList.add( Projections.groupProperty("subCuentas.porcentajeAutorizado"));
				projectionList.add( Projections.groupProperty("subCuentas.montoAutorizado"));
				projectionList.add( Projections.groupProperty("subCuentas.nroPrioridad"));
				projectionList.add( Projections.groupProperty("naturalezaPacientes.codigo"));
				projectionList.add( Projections.groupProperty("centroAtencion.consecutivo"));
				projectionList.add( Projections.groupProperty("contratosSubCuenta.codigo"));
				projectionList.add( Projections.groupProperty("tiposAfiliado.acronimo"));
				
			criteria.setProjection(projectionList);
			
			/** * criteria.addOrder(Property.forName("solicitudes.numeroSolicitud").asc());  
			 	Se modifica por Inc. 406 Mantis  */		
			criteria.addOrder(Property.forName("solicitudes.consecutivoOrdenesMedicas").asc());
						
		criteria.setResultTransformer(Transformers.aliasToBean(DtoSolicitud.class));
		
		
		ArrayList<DtoSolicitud> listaSolicitudes = (ArrayList<DtoSolicitud>)criteria.list();
		for (DtoSolicitud dtoSolicitud : listaSolicitudes) 
		{	
			dtoSolicitud.setInstitucion(parametros.getInstitucion());
			dtoSolicitud = obtenerDetalleArticulosSolicitudesPorCuenta(dtoSolicitud);
			if(parametros.isSolicitudDeCirugia()){
				dtoSolicitud = obtenerDetalleServiciosSolicitudesCirugiaPorCuenta(dtoSolicitud);
			}else{
				dtoSolicitud = obtenerDetalleServiciosSolicitudesPorCuenta(dtoSolicitud);	
			}
			dtoSolicitud.setTipoOrden(ConstantesIntegridadDominio.acronimoTipoOrdenMedica);
		}
		
		return listaSolicitudes;
	}
	
	
	

	/**
	 * Retorna los detalles de la solicitud (Insumos/Medicamentos)
	 * 
	 * @param solicitud
	 * @return DtoSolicitud
	 * @autor Cristhian Murillo.
	 */
	private DtoSolicitud obtenerDetalleArticulosSolicitudesPorCuenta(DtoSolicitud solicitud)
	{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Solicitudes.class,"solicitudes");
		
		criteria.createAlias("solicitudes.solicitudesMedicamentos"			, "solicitudesMedicamentos"	, Criteria.INNER_JOIN);
		criteria.createAlias("solicitudesMedicamentos.detalleSolicitudeses"	, "detalleSolicitudeses"	, Criteria.INNER_JOIN);
		criteria.createAlias("detalleSolicitudeses.articuloByArticulo"		, "articuloByArticulo"		, Criteria.INNER_JOIN);
		
		
		criteria.createAlias("articuloByArticulo.formaFarmaceutica"		, "formaFarmaceutica"		, Criteria.INNER_JOIN);
		
		
		
		criteria.createAlias("detalleSolicitudeses.unidosisXArticulo"		, "unidosisXArticulo"		, Criteria.LEFT_JOIN);
		criteria.createAlias("unidosisXArticulo.unidadMedida"				, "unidadMedida"			, Criteria.LEFT_JOIN);
		criteria.createAlias("articuloByArticulo.naturalezaArticulo"		, "naturalezaArticulo"		, Criteria.LEFT_JOIN);
		criteria.createAlias("solicitudes.diagnosticos"						, "diagnosticos"			, Criteria.LEFT_JOIN);

		criteria.add(Restrictions.eq("solicitudesMedicamentos.numeroSolicitud"	, solicitud.getNumeroSolicitud()));
		
		
		ProjectionList projectionList = Projections.projectionList();
			//--Medicamento
			projectionList.add(Projections.property("articuloByArticulo.codigo")				, "codigoArticulo");
			projectionList.add(Projections.property("articuloByArticulo.descripcion")			, "descripcionArticulo");
			projectionList.add(Projections.property("articuloByArticulo.concentracion")			, "concentracionArticulo");
			projectionList.add(Projections.property("formaFarmaceutica.id.acronimo")		, "formaFarmaceuticaArticulo");
			projectionList.add(Projections.property("unidadMedida.nombre")						, "unidadMedidaArticulo");
			projectionList.add(Projections.property("unidadMedida.acronimo")					, "acronimoUnidadMedidaArticulo");
			projectionList.add(Projections.property("unidosisXArticulo.codigo")					, "dosisXArticuloID");			
			projectionList.add(Projections.property("naturalezaArticulo.esMedicamento")			, "esMedicamento");
			projectionList.add(Projections.property("naturalezaArticulo.nombre")				, "naturalezaArticulo"); 
			
			//- Formulación 	
			projectionList.add(Projections.property("detalleSolicitudeses.dosis")				, "dosisFormulacion");
			projectionList.add(Projections.property("detalleSolicitudeses.frecuencia")			, "frecuenciaFormulacion");
			projectionList.add(Projections.property("detalleSolicitudeses.via")					, "viaFormulacion");
			projectionList.add(Projections.property("detalleSolicitudeses.diasTratamiento")		, "diasTratamientoFormulacion");
			projectionList.add(Projections.property("detalleSolicitudeses.cantidad")			, "totalUnidadesFormulacion");
			projectionList.add(Projections.property("detalleSolicitudeses.tipoFrecuencia")		, "tipoFrecuenciaFormulacion");
			
			//--Orden
			projectionList.add(Projections.property("solicitudes.consecutivoOrdenesMedicas")	, "consecutivoOrdenMed");
			projectionList.add(Projections.property("solicitudesMedicamentos.numeroSolicitud")	, "numeroOrden");
			projectionList.add(Projections.property("solicitudes.fechaSolicitud")				, "fechaOrden");
			projectionList.add(Projections.property("solicitudes.horaSolicitud")				, "horaOrden");
			//--Diagnostico
			projectionList.add(Projections.property("diagnosticos.id.acronimo")					, "acronimoDx");
			projectionList.add(Projections.property("diagnosticos.id.tipoCie")					, "tipoCieDx");
			projectionList.add(Projections.property("diagnosticos.nombre")						, "diagnostico");
			
		criteria.setProjection(projectionList);
		
		criteria.setResultTransformer(Transformers.aliasToBean(DtoArticulosAutorizaciones.class));
		
		
		ArrayList<DtoArticulosAutorizaciones> listaArticulos = (ArrayList<DtoArticulosAutorizaciones>)criteria.list();
		solicitud.setListaArticulos(listaArticulos);
		
		return solicitud;
	}
	
	
	
	
	/**
	 * Retorna los detalles de la solicitud (Servicios).
	 * La cantidad autorizada de servicios siempre es = 1
	 * 
	 * @param solicitud
	 * @return DtoSolicitud
	 * @autor Cristhian Murillo.
	 */
	private DtoSolicitud obtenerDetalleServiciosSolicitudesPorCuenta(DtoSolicitud solicitud)
	{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Solicitudes.class,"solicitudes");
		
		criteria.createAlias("solicitudes.solicitudesSubcuentas"			, "solicitudesSubcuentas"	, Criteria.INNER_JOIN);
		
		/*if(solicitud.getTipoSolicitud()==ConstantesBD.codigoTipoSolicitudCirugia)
			criteria.createAlias("solicitudesSubcuentas.serviciosByServicioCx"	, "serviciosByServicio"		, Criteria.INNER_JOIN);
		else*/
			criteria.createAlias("solicitudesSubcuentas.serviciosByServicio"	, "serviciosByServicio"		, Criteria.INNER_JOIN);
		
		criteria.createAlias("solicitudes.diagnosticos"						, "diagnosticos"			, Criteria.LEFT_JOIN);
		criteria.createAlias("serviciosByServicio.gruposServicios"			, "gruposServicios"			, Criteria.LEFT_JOIN);
			
		criteria.add(Restrictions.eq("solicitudes.numeroSolicitud"	, solicitud.getNumeroSolicitud()));
		criteria.add(Restrictions.isNotNull("serviciosByServicio.codigo"));
		
		// Referencia servicios. Se trae el nombre del servicio dependiendo del tipo de esquema tarifario
		Integer tipoTarifario = Integer.parseInt(ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(solicitud.getInstitucion()));
		criteria.createAlias("serviciosByServicio.referenciasServicios"			, "referenciasServicios"	, Criteria.INNER_JOIN);
		criteria.add(Restrictions.eq("referenciasServicios.id.tipoTarifario"	, tipoTarifario));
		// ----------------------------------------------------------------------------------------------
		
		
		ProjectionList projectionList = Projections.projectionList();
			//--Servicio
			projectionList.add(Projections.property("serviciosByServicio.codigo")				, "codigoServicio");
			projectionList.add(Projections.property("referenciasServicios.codigoPropietario")	, "codigoPropietario");
			projectionList.add(Projections.property("referenciasServicios.descripcion")			, "descripcionServicio");
			projectionList.add(Projections.property("solicitudes.consecutivoOrdenesMedicas")	, "consecutivoOrdenMed");
			projectionList.add(Projections.property("solicitudes.numeroSolicitud")				, "numeroOrden");
			projectionList.add(Projections.property("solicitudes.fechaSolicitud")				, "fechaOrden");
			projectionList.add(Projections.property("solicitudes.horaSolicitud")				, "horaOrden");
			//--Diagnostico
			projectionList.add(Projections.property("diagnosticos.id.acronimo")					, "acronimoDx");
			projectionList.add(Projections.property("diagnosticos.id.tipoCie")					, "tipoCieDx");
			projectionList.add(Projections.property("diagnosticos.nombre")						, "diagnostico");
			//--Grupo Servicios
			projectionList.add(Projections.property("gruposServicios.numDiasUrgente")			, "numDiasUrgente");
			projectionList.add(Projections.property("gruposServicios.acroDiasUrgente")			, "acroDiasUrgente");
			projectionList.add(Projections.property("gruposServicios.numDiasNormal")			, "numDiasNormal");
			projectionList.add(Projections.property("gruposServicios.acroDiasNormal")			, "acroDiasNormal");
		
		
		criteria.setProjection(Projections.distinct(projectionList));
		
		criteria.setResultTransformer(Transformers.aliasToBean(DtoServiciosAutorizaciones.class));
		
		ArrayList<DtoServiciosAutorizaciones> listaServicios = (ArrayList<DtoServiciosAutorizaciones>)criteria.list();
		
		
		// solo se solicita y se autoriza un servicio
		for (DtoServiciosAutorizaciones dtoServiciosAutorizaciones : listaServicios) 
		{
			dtoServiciosAutorizaciones.setCantidadAutorizadaServicio(1);
			dtoServiciosAutorizaciones.setCantidadSolicitada(1);
		}
		
		solicitud.setListaServicios(listaServicios);
		
		return solicitud;
	}
	
	
	/**
	 * Retorna los detalles de la solicitud (Servicios). (CIRUGIA)
	 * La cantidad autorizada de servicios siempre es = 1
	 * 
	 * @param solicitud
	 * @return DtoSolicitud
	 * @author camilo
	 */
	private DtoSolicitud obtenerDetalleServiciosSolicitudesCirugiaPorCuenta(DtoSolicitud solicitud)
	{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Solicitudes.class,"solicitudes");
	
		criteria.createAlias("solicitudes.solicitudesCirugia"				, "solicitudesCirugia"		, Criteria.INNER_JOIN);
		criteria.createAlias("solicitudesCirugia.solCirugiaPorServicios"	, "solCirugiaPorServicios"	, Criteria.INNER_JOIN);
		criteria.createAlias("solCirugiaPorServicios.servicios"				, "servicio"				, Criteria.INNER_JOIN);
		criteria.createAlias("solicitudes.diagnosticos"						, "diagnosticos"			, Criteria.LEFT_JOIN);
		criteria.createAlias("servicio.gruposServicios"						, "gruposServicios"			, Criteria.LEFT_JOIN);
	
		criteria.add(Restrictions.eq("solicitudes.numeroSolicitud"	, solicitud.getNumeroSolicitud()));
		criteria.add(Restrictions.isNotNull("servicio.codigo"));
		
		// Referencia servicios. Se trae el nombre del servicio dependiendo del tipo de esquema tarifario
		Integer tipoTarifario = Integer.parseInt(ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(solicitud.getInstitucion()));
		criteria.createAlias("servicio.referenciasServicios"					, "referenciasServicios"	, Criteria.INNER_JOIN);
		criteria.add(Restrictions.eq("referenciasServicios.id.tipoTarifario"	, tipoTarifario));
		// ----------------------------------------------------------------------------------------------
	
		ProjectionList projectionList = Projections.projectionList();
			//--Servicio
			projectionList.add(Projections.property("servicio.codigo")							, "codigoServicio");
			projectionList.add(Projections.property("referenciasServicios.codigoPropietario")	, "codigoPropietario");
			projectionList.add(Projections.property("referenciasServicios.descripcion")			, "descripcionServicio");
			projectionList.add(Projections.property("solicitudes.consecutivoOrdenesMedicas")	, "consecutivoOrdenMed");
			projectionList.add(Projections.property("solicitudes.numeroSolicitud")				, "numeroOrden");
			projectionList.add(Projections.property("solicitudes.fechaSolicitud")				, "fechaOrden");
			projectionList.add(Projections.property("solicitudes.horaSolicitud")				, "horaOrden");
			//--Diagnostico
			projectionList.add(Projections.property("diagnosticos.id.acronimo")					, "acronimoDx");
			projectionList.add(Projections.property("diagnosticos.id.tipoCie")					, "tipoCieDx");
			projectionList.add(Projections.property("diagnosticos.nombre")						, "diagnostico");
			//--Grupo Servicios
			projectionList.add(Projections.property("gruposServicios.numDiasUrgente")			, "numDiasUrgente");
			projectionList.add(Projections.property("gruposServicios.acroDiasUrgente")			, "acroDiasUrgente");
			projectionList.add(Projections.property("gruposServicios.numDiasNormal")			, "numDiasNormal");
			projectionList.add(Projections.property("gruposServicios.acroDiasNormal")			, "acroDiasNormal");
		
		
		criteria.setProjection(Projections.distinct(projectionList));
		
		criteria.setResultTransformer(Transformers.aliasToBean(DtoServiciosAutorizaciones.class));
		
		ArrayList<DtoServiciosAutorizaciones> listaServicios = (ArrayList<DtoServiciosAutorizaciones>)criteria.list();
		
		// solo se solicita y se autoriza un servicio
		for (DtoServiciosAutorizaciones dtoServiciosAutorizaciones : listaServicios){
			dtoServiciosAutorizaciones.setCantidadAutorizadaServicio(1);
			dtoServiciosAutorizaciones.setCantidadSolicitada(1);
		}		
		solicitud.setListaServicios(listaServicios);
		
		return solicitud;
	}
	
	/**
	 * Retorna las solicitudes por el número de esta.
	 * 
	 * @param parametros
	 * @return ArrayList<DtoSolicitud>
	 * @autor Cristhian Murillo.
	 */
	public ArrayList<DtoSolicitud> obtenerSolicitudesSubcuenta(DtoSolicitud parametros)
	{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Solicitudes.class,"solicitudes");
	
		criteria.createAlias("solicitudes.cuentas"								, "cuentas"						,Criteria.LEFT_JOIN);
		criteria.createAlias("cuentas.pacientes"								, "pacientes"					,Criteria.LEFT_JOIN);
		criteria.createAlias("cuentas.viasIngreso"								, "viasIngreso"					,Criteria.LEFT_JOIN);
		criteria.createAlias("cuentas.tiposPaciente"							, "tiposPaciente"				,Criteria.LEFT_JOIN);
		criteria.createAlias("pacientes.personas"								, "personas"					,Criteria.LEFT_JOIN);
		criteria.createAlias("personas.tiposIdentificacion"						, "tiposIdentificacion"			,Criteria.LEFT_JOIN);
		criteria.createAlias("solicitudes.centrosCostoByCentroCostoSolicitante"	, "centrosCostoByCentroCostoSolicitante");
		criteria.createAlias("solicitudes.centrosCostoByCentroCostoSolicitado"	, "centrosCostoByCentroCostoSolicitado");
		
		// PermitirAutorizarDiferenteDeSolicitudes
		criteria.createAlias("solicitudes.autoEntsubSolicitudeses"					, "autoEntsubSolicitudeses"		,Criteria.LEFT_JOIN); 
		criteria.createAlias("autoEntsubSolicitudeses.autorizacionesEntidadesSub"	, "autorizacionesEntidadesSubs"	,Criteria.LEFT_JOIN); 
		
		criteria.createAlias("solicitudes.solicitudesPosponers"					, "solicitudesPosponers"		,Criteria.LEFT_JOIN);
		
		
		/* Las cirugias en especial no tienen el convenio asociado a la subcuenta sino al cargo. 
		 * Si se intenta consultar una cirugia no se debe tener en cuenta estos parametros a menos que se necesiten. *	**/
		if(parametros.isSolicitudDeCirugia())
		{
			// En el caso de cirugía debería buscar el convenio y el contrato en el cargo
			criteria.createAlias("solicitudes.solicitudesCirugia"					, "solicitudesCirugia"			,Criteria.INNER_JOIN);
			criteria.createAlias("solicitudesCirugia.subCuentas"					, "subCuentas"					,Criteria.INNER_JOIN);
			criteria.createAlias("subCuentas.convenios"								, "convenios"					,Criteria.INNER_JOIN);
			criteria.createAlias("convenios.contratoses"							, "contratoses"					,Criteria.INNER_JOIN);
			criteria.createAlias("convenios.tiposContrato"							, "tiposContrato"				,Criteria.INNER_JOIN);
		}
		else{
			criteria.createAlias("solicitudes.solicitudesSubcuentas"				, "solicitudesSubcuentas"		,Criteria.LEFT_JOIN);
			criteria.createAlias("solicitudesSubcuentas.subCuentas"					, "subCuentas"					,Criteria.LEFT_JOIN);
			criteria.createAlias("subCuentas.convenios"								, "convenios"					,Criteria.INNER_JOIN);
			criteria.createAlias("convenios.contratoses"							, "contratoses"					,Criteria.INNER_JOIN);
			criteria.createAlias("convenios.tiposContrato"							, "tiposContrato"				,Criteria.INNER_JOIN);
		}
		//-------------------------------------------------------------------------------------------------------------------
		
		
		
		criteria.createAlias("subCuentas.estratosSociales"						, "estratosSociales"			,Criteria.LEFT_JOIN);
		criteria.createAlias("subCuentas.tiposAfiliado"							, "tiposAfiliado"				,Criteria.LEFT_JOIN);
		criteria.createAlias("subCuentas.naturalezaPacientes"					, "naturalezaPacientes"			,Criteria.LEFT_JOIN);
		
		criteria.add(Restrictions.eq("solicitudes.numeroSolicitud"	, parametros.getNumeroSolicitud()));
		
		ProjectionList projectionList = Projections.projectionList();
			// --> Datos Solicitud
				projectionList.add(Projections.property("solicitudes.numeroSolicitud")					,"numeroSolicitud");
				projectionList.add(Projections.property("solicitudes.consecutivoOrdenesMedicas")		,"consecutivoOrdenesMedicas");
				projectionList.add(Projections.property("solicitudes.fechaSolicitud")					,"fechaSolicitud");
				projectionList.add(Projections.property("solicitudes.horaSolicitud")					,"horaSolicitud");
				projectionList.add(Projections.property("solicitudes.urgente")							,"urgente");
				projectionList.add(Projections.property("solicitudesPosponers.codigoPk")				,"codigoPosponer");
			// --> Datos Autorización (en el caso de tener)
				projectionList.add(Projections.property("autorizacionesEntidadesSubs.consecutivo")		,"consecutivoPkAutorizacionAsociada");
				projectionList.add(Projections.property("autorizacionesEntidadesSubs.estado")			,"estadoAutorizacionAsociada");
			// --> Datos Paciente/Persona	
				projectionList.add(Projections.property("tiposIdentificacion.acronimo")					,"tipoId");
				projectionList.add(Projections.property("personas.numeroIdentificacion")				,"numeroId");
				projectionList.add(Projections.property("personas.primerNombre")						,"primerNombre");
				projectionList.add(Projections.property("personas.segundoNombre")						,"segundoNombre");
				projectionList.add(Projections.property("personas.primerApellido")						,"primerApellido");
				projectionList.add(Projections.property("personas.segundoApellido")						,"segundoApellido");
				projectionList.add(Projections.property("personas.fechaNacimiento")						,"fechaNacimiento");
				projectionList.add(Projections.property("personas.codigo")								,"codPersona");
			// --> Datos Convenio/Contrato	
				projectionList.add(Projections.property("convenios.codigo")								,"codigoConvenio");
				projectionList.add(Projections.property("convenios.nombre")								,"nombreConvenio");
				projectionList.add(Projections.property("tiposContrato.nombre")							,"nombreTipoContrato");
			// --> Datos Centros Costo de la Solicitud	
				projectionList.add(Projections.property("centrosCostoByCentroCostoSolicitante.codigo")	,"codigoCentroCostoSolicitante");
				projectionList.add(Projections.property("centrosCostoByCentroCostoSolicitante.nombre")	,"nombreCentroCostoSolicitante");
				projectionList.add(Projections.property("centrosCostoByCentroCostoSolicitado.codigo")	,"codigoCentroCostoSolicitado");
				projectionList.add(Projections.property("centrosCostoByCentroCostoSolicitado.nombre")	,"nombreCentroCostoSolicitado");
			// --> Datos Cuenta Paciente/Persona	
				projectionList.add(Projections.property("cuentas.id")									,"codigoCuenta");
				projectionList.add(Projections.property("viasIngreso.codigo")							,"codViaIngreso");
				projectionList.add(Projections.property("tiposPaciente.acronimo")						,"tipoPaciente");
				projectionList.add(Projections.property("estratosSociales.descripcion")					,"descripcionEstratoSocial");
				projectionList.add(Projections.property("tiposAfiliado.nombre")							,"nombreTipoAfiliado");
				projectionList.add(Projections.property("subCuentas.semanasCotizacion")					,"semanasCotizacion");
				projectionList.add(Projections.property("subCuentas.tipoMontoCobro")					,"tipoMontoCobro");
				projectionList.add(Projections.property("subCuentas.porcentajeAutorizado")				,"porcentajeAutorizado");
				projectionList.add(Projections.property("subCuentas.montoAutorizado")					,"montoAutorizado");
				projectionList.add(Projections.property("subCuentas.nroPrioridad")						,"nroPrioridad");
				projectionList.add(Projections.property("naturalezaPacientes.codigo")					,"codigoNaturalezaPaciente"); 
				
				
				//-------------------
				projectionList.add( Projections.groupProperty("solicitudes.numeroSolicitud"));
				projectionList.add( Projections.groupProperty("solicitudes.consecutivoOrdenesMedicas"));				
				projectionList.add( Projections.groupProperty("solicitudes.fechaSolicitud"));
				projectionList.add( Projections.groupProperty("solicitudes.horaSolicitud"));
				projectionList.add( Projections.groupProperty("solicitudes.urgente"));
				projectionList.add( Projections.groupProperty("solicitudesPosponers.codigoPk"));
				projectionList.add( Projections.groupProperty("autorizacionesEntidadesSubs.consecutivo"));
				projectionList.add( Projections.groupProperty("autorizacionesEntidadesSubs.estado"));
				projectionList.add( Projections.groupProperty("tiposIdentificacion.acronimo"));
				projectionList.add( Projections.groupProperty("personas.numeroIdentificacion"));
				projectionList.add( Projections.groupProperty("personas.primerNombre"));
				projectionList.add( Projections.groupProperty("personas.segundoNombre"));
				projectionList.add( Projections.groupProperty("personas.primerApellido"));
				projectionList.add( Projections.groupProperty("personas.segundoApellido"));
				projectionList.add( Projections.groupProperty("personas.fechaNacimiento"));
				projectionList.add( Projections.groupProperty("personas.codigo"));
				projectionList.add( Projections.groupProperty("convenios.codigo"));
				projectionList.add( Projections.groupProperty("convenios.nombre"));
				projectionList.add( Projections.groupProperty("tiposContrato.nombre"));
				projectionList.add( Projections.groupProperty("convenios.codigo"));
				projectionList.add( Projections.groupProperty("convenios.nombre"));
				projectionList.add( Projections.groupProperty("tiposContrato.nombre"));
				projectionList.add( Projections.groupProperty("centrosCostoByCentroCostoSolicitante.codigo"));
				projectionList.add( Projections.groupProperty("centrosCostoByCentroCostoSolicitante.nombre"));
				projectionList.add( Projections.groupProperty("centrosCostoByCentroCostoSolicitado.codigo"));
				projectionList.add( Projections.groupProperty("centrosCostoByCentroCostoSolicitado.nombre"));
				projectionList.add( Projections.groupProperty("cuentas.id"));
				projectionList.add( Projections.groupProperty("viasIngreso.codigo"));
				projectionList.add( Projections.groupProperty("tiposPaciente.acronimo"));
				projectionList.add( Projections.groupProperty("estratosSociales.descripcion"));
				projectionList.add( Projections.groupProperty("tiposAfiliado.nombre"));
				projectionList.add( Projections.groupProperty("subCuentas.semanasCotizacion"));
				projectionList.add( Projections.groupProperty("subCuentas.tipoMontoCobro"));
				projectionList.add( Projections.groupProperty("subCuentas.porcentajeAutorizado"));
				projectionList.add( Projections.groupProperty("subCuentas.montoAutorizado"));
				projectionList.add( Projections.groupProperty("subCuentas.nroPrioridad"));
				projectionList.add( Projections.groupProperty("naturalezaPacientes.codigo"));
				
			criteria.setProjection(projectionList);
		
			/**
			 * criteria.addOrder(Property.forName("solicitudes.numeroSolicitud").asc());
			 * Se modifica por Inc. 406 Mantis
			 */	
			criteria.addOrder(Property.forName("solicitudes.consecutivoOrdenesMedicas").asc());
					
		criteria.setResultTransformer(Transformers.aliasToBean(DtoSolicitud.class));
		
		ArrayList<DtoSolicitud> listaSolicitudes = (ArrayList<DtoSolicitud>)criteria.list();
		
		for (DtoSolicitud dtoSolicitud : listaSolicitudes) 
		{	
			dtoSolicitud.setInstitucion(parametros.getInstitucion());
			dtoSolicitud.setTipoSolicitud(parametros.getTipoSolicitud());
			dtoSolicitud = obtenerDetalleArticulosSolicitudesPorCuenta(dtoSolicitud);
			if(parametros.isSolicitudDeCirugia()){
				dtoSolicitud = obtenerDetalleServiciosSolicitudesCirugiaPorCuenta(dtoSolicitud);
			}else{
				dtoSolicitud = obtenerDetalleServiciosSolicitudesPorCuenta(dtoSolicitud);			
			}
		}
		
		return listaSolicitudes;
	}
	
	/**
	 * Este método consulta las solicitudes de cirugias en el sistema, dependiendo de los párametros de consulta enviados
	 * @param dtoFiltro párametros de consulta
	 * @return ArrayList<DtoResultadoConsultaProcesosCierre> lista de solicitudes
	 */
	public ArrayList<DtoResultadoConsultaProcesosCierre> consultarSolicitudesCirugias(DtoProcesoPresupuestoCapitado dtoFiltro){
		
		Criteria criteria= sessionFactory.getCurrentSession().createCriteria(Solicitudes.class,"solicitud")
			
		.createAlias("solicitud.solicitudesCirugia"			, "solicitudesCirugia"	)
		
		.createAlias("solicitudesCirugia.subCuentas"					, "subCuentas"					,Criteria.LEFT_JOIN)
		.createAlias("subCuentas.convenios"								, "convenio"					,Criteria.INNER_JOIN)
		.createAlias("subCuentas.contratos"								, "contrato"					,Criteria.INNER_JOIN)
		
		.createAlias("solicitudesCirugia.solCirugiaPorServicios"			, "solCirugiaPorServicios"	)
		.createAlias("solCirugiaPorServicios.servicios"			, "servicio"	)
		.createAlias("servicio.nivelAtencion"							, "nivelAtencionServicio"		, Criteria.LEFT_JOIN)
		.createAlias("servicio.referenciasServicios"					, "referenciasServicios"		, Criteria.LEFT_JOIN)
		.createAlias("servicio.gruposServicios"							, "grupoServicio"		, Criteria.LEFT_JOIN)
		
		.createAlias("solicitudesCirugia.anulacionSolCx"			, "anulacionSolicitudCx", Criteria.LEFT_JOIN);
		
		ProjectionList projection = Projections.projectionList();
		
		String esquemaTarServicios = ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(dtoFiltro.getInstitucion());
		
		Disjunction disjunction = Restrictions.disjunction();  
		disjunction.add( Property.forName("referenciasServicios.id.tipoTarifario").isNull());  
		disjunction.add( Property.forName("referenciasServicios.id.tipoTarifario").eq(Utilidades.convertirAEntero(esquemaTarServicios)));
		criteria.add(disjunction);
		
		if(dtoFiltro.getConvenio()!=null){
			criteria.add(Restrictions.eq("convenio.codigo",dtoFiltro.getConvenio().intValue()));
		}
		
		if(dtoFiltro.getContrato()!=null){
			criteria.add(Restrictions.eq("contrato.codigo",dtoFiltro.getContrato().intValue()));
		}
		
		
		if(dtoFiltro.getFechaInicio()!=null&& dtoFiltro.getFechaFin()!=null){
			//hermorhu - MT6601
			//Se trae las Solicitudes Cx y las Anulaciones para ese rango de fechas
			
			//Agregar el rango de fechas para la anulacion de la solicitud Cx de 00:00:00 a 23:59:59
			Calendar fechaInicioAnulacion = Calendar.getInstance();
			fechaInicioAnulacion.setTime(dtoFiltro.getFechaInicio());
			fechaInicioAnulacion.add(Calendar.HOUR, -24);
			fechaInicioAnulacion.add(Calendar.MINUTE, 0);
			fechaInicioAnulacion.add(Calendar.SECOND, 0);
			
			Calendar fechaFinAnulacion = Calendar.getInstance();
			fechaFinAnulacion.setTime(dtoFiltro.getFechaFin());
			fechaFinAnulacion.add(Calendar.HOUR, 24);
			fechaFinAnulacion.add(Calendar.MINUTE, 0);
			fechaFinAnulacion.add(Calendar.SECOND, 0);
			
			Conjunction conjunction = Restrictions.conjunction();
			conjunction.add( Restrictions.gt("anulacionSolicitudCx.fecha", fechaInicioAnulacion.getTime()) );
			conjunction.add( Restrictions.lt("anulacionSolicitudCx.fecha", fechaFinAnulacion.getTime()) ); 
			
			Disjunction disjunctionFechas = Restrictions.disjunction(); 
			
			disjunctionFechas.add(conjunction);
			 
			disjunctionFechas.add( Restrictions.between("solicitud.fechaSolicitud", dtoFiltro.getFechaInicio(), dtoFiltro.getFechaFin()));  

//			disjunctionFechas.add(Restrictions.between("anulacionSolicitudCx.fecha", fechaInicioAnulacion.getTime(), fechaFinAnulacion.getTime()));
			
			criteria.add(disjunctionFechas);
		}
		
		criteria.add(Restrictions.eq("subCuentas.nroPrioridad",1));
		
		//RETORNA VALORES PARA SERVICIOS
		projection.add(Projections.property("servicio.codigo"),"codigoServicio");
		projection.add(Projections.property("nivelAtencionServicio.consecutivo"),"nivelAtencionServicio");
		projection.add(Projections.property("referenciasServicios.descripcion"),"nombreServicio");
		//projection.add(Projections.property("solicitudesSubcuentas.cantidad"),"cantidadServiciosSolicitudes");
		projection.add(Projections.property("grupoServicio.codigo"),"codigoGrupoServicio");
		projection.add(Projections.property("grupoServicio.descripcion"),"descripcionGrupoServicio");
		
		projection.add(Projections.property("convenio.codigo"),"convenio");
		projection.add(Projections.property("contrato.codigo"),"contrato");
		projection.add(Projections.property("solicitud.fechaSolicitud"),"fecha");
		projection.add(Projections.property("solicitud.numeroSolicitud"),"numeroSolicitud");
		projection.add(Projections.property("anulacionSolicitudCx.fecha"),"fechaAnulacion");
		//MT6578 se adiciona codigoPeticion
		projection.add(Projections.property("solicitudesCirugia.codigoPeticion"),"codigoPeticion");
		
		criteria.setProjection(Projections.distinct(projection));
		
		criteria.setResultTransformer( Transformers.aliasToBean(DtoResultadoConsultaProcesosCierre.class));
		ArrayList<DtoResultadoConsultaProcesosCierre> listadoOrdenesMedicas=
			(ArrayList<DtoResultadoConsultaProcesosCierre>)criteria.list();
	
		return listadoOrdenesMedicas;
	}
	
	/**
	 * Este método consulta las solicitudes de cirugias en el sistema anuladas, dependiendo de los párametros de consulta enviados
	 * @param dtoFiltro párametros de consulta
	 * @return ArrayList<DtoResultadoConsultaProcesosCierre> 
	 * @author hermorhu
	 */
	public ArrayList<DtoResultadoConsultaProcesosCierre> consultarSolicitudesCirugiasAnuladas(DtoProcesoPresupuestoCapitado dtoFiltro){
		
		Criteria criteria= sessionFactory.getCurrentSession().createCriteria(Solicitudes.class,"solicitud")
			
		.createAlias("solicitud.solicitudesCirugia"			, "solicitudesCirugia"	)
		
		.createAlias("solicitudesCirugia.subCuentas"					, "subCuentas"					,Criteria.LEFT_JOIN)
		.createAlias("subCuentas.convenios"								, "convenio"					,Criteria.INNER_JOIN)
		.createAlias("subCuentas.contratos"								, "contrato"					,Criteria.INNER_JOIN)
		
		.createAlias("solicitudesCirugia.solCirugiaPorServicios"			, "solCirugiaPorServicios"	)
		.createAlias("solCirugiaPorServicios.servicios"			, "servicio"	)
		.createAlias("servicio.nivelAtencion"							, "nivelAtencionServicio"		, Criteria.LEFT_JOIN)
		.createAlias("servicio.referenciasServicios"					, "referenciasServicios"		, Criteria.LEFT_JOIN)
		.createAlias("servicio.gruposServicios"							, "grupoServicio"		, Criteria.LEFT_JOIN)
		
		.createAlias("solicitudesCirugia.anulacionSolCx"			, "anulacionSolicitudCx", Criteria.LEFT_JOIN);
		
		ProjectionList projection = Projections.projectionList();
		
		String esquemaTarServicios = ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(dtoFiltro.getInstitucion());
		
		Disjunction disjunction = Restrictions.disjunction();  
		disjunction.add( Property.forName("referenciasServicios.id.tipoTarifario").isNull());  
		disjunction.add( Property.forName("referenciasServicios.id.tipoTarifario").eq(Utilidades.convertirAEntero(esquemaTarServicios)));
		criteria.add(disjunction);
		
		if(dtoFiltro.getConvenio()!=null){
			criteria.add(Restrictions.eq("convenio.codigo",dtoFiltro.getConvenio().intValue()));
		}
		
		if(dtoFiltro.getContrato()!=null){
			criteria.add(Restrictions.eq("contrato.codigo",dtoFiltro.getContrato().intValue()));
		}
		
		
		if(dtoFiltro.getFechaInicio()!=null&& dtoFiltro.getFechaFin()!=null){
			//hermorhu - MT6601
			//Se trae las Solicitudes Cx y las Anulaciones para ese rango de fechas
			
			//Agregar el rango de fechas para la anulacion de la solicitud Cx de 00:00:00 a 23:59:59
			Calendar fechaInicioAnulacion = Calendar.getInstance();
			fechaInicioAnulacion.setTime(dtoFiltro.getFechaInicio());
			
			Calendar fechaFinAnulacion = Calendar.getInstance();
			fechaFinAnulacion.setTime(dtoFiltro.getFechaFin());
			
			if(System.getProperty("TIPOBD").equals("ORACLE")){
				fechaInicioAnulacion.add(Calendar.HOUR, 0);
				fechaInicioAnulacion.add(Calendar.MINUTE, 0);
				fechaInicioAnulacion.add(Calendar.SECOND, 0);
				
				fechaFinAnulacion.add(Calendar.HOUR, 24);
				fechaFinAnulacion.add(Calendar.MINUTE, 0);
				fechaFinAnulacion.add(Calendar.SECOND, 0);
				
				criteria.add(Restrictions.between("anulacionSolicitudCx.fecha", fechaInicioAnulacion.getTime(), fechaFinAnulacion.getTime()));
			}else {
				fechaInicioAnulacion.add(Calendar.HOUR, -24);
				fechaInicioAnulacion.add(Calendar.MINUTE, 0);
				fechaInicioAnulacion.add(Calendar.SECOND, 0);
				
				fechaFinAnulacion.add(Calendar.HOUR, 24);
				fechaFinAnulacion.add(Calendar.MINUTE, 0);
				fechaFinAnulacion.add(Calendar.SECOND, 0);
				
				Conjunction conjunction = Restrictions.conjunction();
				conjunction.add( Restrictions.gt("anulacionSolicitudCx.fecha", fechaInicioAnulacion.getTime()) );
				conjunction.add( Restrictions.lt("anulacionSolicitudCx.fecha", fechaFinAnulacion.getTime()) ); 
				
				criteria.add(conjunction);
				
			}
		}
		
		criteria.add(Restrictions.eq("subCuentas.nroPrioridad",1));
		
		//RETORNA VALORES PARA SERVICIOS
		projection.add(Projections.property("servicio.codigo"),"codigoServicio");
		projection.add(Projections.property("nivelAtencionServicio.consecutivo"),"nivelAtencionServicio");
		projection.add(Projections.property("referenciasServicios.descripcion"),"nombreServicio");
		projection.add(Projections.property("grupoServicio.codigo"),"codigoGrupoServicio");
		projection.add(Projections.property("grupoServicio.descripcion"),"descripcionGrupoServicio");
		
		projection.add(Projections.property("convenio.codigo"),"convenio");
		projection.add(Projections.property("contrato.codigo"),"contrato");
		//projection.add(Projections.property("solicitud.fechaSolicitud"),"fecha");
		//projection.add(Projections.property("solicitud.numeroSolicitud"),"numeroSolicitud");
		projection.add(Projections.property("anulacionSolicitudCx.fecha"),"fechaAnulacion");
		//MT6578 se adiciona codigoPeticion
		projection.add(Projections.property("solicitudesCirugia.codigoPeticion"),"codigoPeticion");
		
		criteria.setProjection(Projections.distinct(projection));
		
		criteria.setResultTransformer( Transformers.aliasToBean(DtoResultadoConsultaProcesosCierre.class));
		ArrayList<DtoResultadoConsultaProcesosCierre> listadoOrdenesMedicas=
			(ArrayList<DtoResultadoConsultaProcesosCierre>)criteria.list();
	
		return listadoOrdenesMedicas;
	}
	
	/**
	 * Este método consulta las solicitudes en el sistema, dependiendo de los párametros de consulta enviados
	 * @param dtoFiltro párametros de consulta
	 * @return ArrayList<DtoResultadoConsultaProcesosCierre> lista de solicitudes
	 */
	public ArrayList<DtoResultadoConsultaProcesosCierre> consultarSolicitudesEnSistema(DtoProcesoPresupuestoCapitado dtoFiltro){
		
		Criteria criteria= sessionFactory.getCurrentSession().createCriteria(Solicitudes.class,"solicitud")
		.createAlias("solicitud.solicitudesSubcuentas"					, "solicitudesSubcuentas"		,Criteria.LEFT_JOIN)
		.createAlias("solicitudesSubcuentas.subCuentas"					, "subCuentas"					,Criteria.LEFT_JOIN)
		.createAlias("subCuentas.convenios"								, "convenio"					,Criteria.INNER_JOIN)
		.createAlias("subCuentas.contratos"								, "contrato"					,Criteria.INNER_JOIN)
		//.createAlias("convenio.contratoses"								, "contrato"					,Criteria.INNER_JOIN)
		
		.createAlias("solicitudesSubcuentas.serviciosByServicio"		, "servicio"					, Criteria.LEFT_JOIN)
		.createAlias("servicio.referenciasServicios"					, "referenciasServicios"		, Criteria.LEFT_JOIN)
		.createAlias("servicio.nivelAtencion"							, "nivelAtencionServicio"		, Criteria.LEFT_JOIN)
		.createAlias("servicio.gruposServicios"							, "grupoServicio"		, Criteria.LEFT_JOIN)
		
		.createAlias("solicitud.solicitudesMedicamentos"			, "solicitudesMedicamentos"	, Criteria.LEFT_JOIN)
		.createAlias("solicitudesMedicamentos.detalleSolicitudeses"	, "detalleSolicitudeses"	, Criteria.LEFT_JOIN)
		.createAlias("detalleSolicitudeses.articuloByArticulo"		, "articulo"				, Criteria.LEFT_JOIN)
		.createAlias("articulo.nivelAtencion"						, "nivelAtencionArticulo"	, Criteria.LEFT_JOIN)
		.createAlias("articulo.tarifasInventarios"					, "tarifaArticulo"			, Criteria.LEFT_JOIN)
		.createAlias("tarifaArticulo.esquemasTarifarios"			, "esquemaTarifarioArticulo", Criteria.LEFT_JOIN)
		
		.createAlias("solicitud.solicitudesCirugia"			, "solicitudesCirugia"	, Criteria.LEFT_JOIN)
		.createAlias("solicitudesCirugia.solCirugiaPorServicios"			, "solCirugiaPorServicios"	, Criteria.LEFT_JOIN)
		.createAlias("solCirugiaPorServicios.servicios"			, "servicioCirugia"	, Criteria.LEFT_JOIN)
		
		
		
		.createAlias("solicitud.anulacionesSolicitud"			, "anulacionSolicitud", Criteria.LEFT_JOIN)
		.createAlias("solicitud.tiposSolicitud"	 , "tiposSolicitud" ,Criteria.INNER_JOIN)
		//MT6578 se adiciona ordenesAmbulatoria
		.createAlias("solicitud.ordenesAmbulatoriases"	 , "ordenesAmbulatoriases" ,Criteria.LEFT_JOIN)
		;
		
		ProjectionList projection = Projections.projectionList();
		
		/*String esquemaTarMedicamentos=ValoresPorDefecto.getEsquemaTariMedicamentosValorizarOrden(dtoFiltro.getInstitucion());
		if(UtilidadTexto.isEmpty(esquemaTarMedicamentos)){
			esquemaTarMedicamentos=null;
		}else
		{
			esquemaTarMedicamentos=esquemaTarMedicamentos.split("-")[0];
		}*/
		
		String esquemaTarServicios = ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(dtoFiltro.getInstitucion());
		/*String esquemaTarServicios = ValoresPorDefecto.getEsquemaTariServiciosValorizarOrden(dtoFiltro.getInstitucion());
		if(UtilidadTexto.isEmpty(esquemaTarServicios)){
			esquemaTarServicios=null;
		}else
		{
			esquemaTarServicios=esquemaTarServicios.split("-")[0];
		}*/
		
		Log4JManager.info("++++++++Filtros Consulta Solicitudes++++++" +
				"\nEstado: "+ConstantesBD.codigoEstadoHCAnulada+
				"\nFecha Inicio: "+dtoFiltro.getFechaInicio()+
				"\nFecha Fin: "+dtoFiltro.getFechaFin()+
				"\nConvenio: "+dtoFiltro.getConvenio()+
				"\nContrato: "+dtoFiltro.getContrato());
				//"\nEsquema Tarifario Medicamentos: "+esquemaTarMedicamentos+
				//"\nTipo Tarifario Servicios: "+esquemaTarServicios);
		
		//FILTROS
		//criteria.add(Restrictions.ne("solicitud.estadoHistoriaClinica", ConstantesBD.codigoEstadoHCAnulada));
		
		Disjunction disjunctionTipos = Restrictions.disjunction();  
		disjunctionTipos.add( Restrictions.eq("tiposSolicitud.codigo", ConstantesBD.codigoTipoSolicitudInterconsulta));  
		disjunctionTipos.add( Restrictions.eq("tiposSolicitud.codigo", ConstantesBD.codigoTipoSolicitudProcedimiento));
		disjunctionTipos.add( Restrictions.eq("tiposSolicitud.codigo", ConstantesBD.codigoTipoSolicitudMedicamentos));
		//MT6578 se adiciona codigoPeticion se adiciona las consultas
		disjunctionTipos.add( Restrictions.eq("tiposSolicitud.codigo", ConstantesBD.codigoTipoSolicitudCita));
		criteria.add(disjunctionTipos);
	
		
		if(dtoFiltro.getFechaInicio()!=null&& dtoFiltro.getFechaFin()!=null){
			//hermorhu - MT6601
			//Se trae las Ordenes Ambulatorias y las Anulaciones para ese rango de fechas
			Disjunction disjunctionFechas = Restrictions.disjunction();  
			disjunctionFechas.add( Restrictions.between("solicitud.fechaSolicitud", dtoFiltro.getFechaInicio(), dtoFiltro.getFechaFin()));  
			disjunctionFechas.add(Restrictions.between("anulacionSolicitud.fecha", dtoFiltro.getFechaInicio(), dtoFiltro.getFechaFin()));
			criteria.add(disjunctionFechas);
		}
		
		criteria.add(Restrictions.eq("subCuentas.nroPrioridad",1));
		
		if(dtoFiltro.getConvenio()!=null){
			criteria.add(Restrictions.eq("convenio.codigo",dtoFiltro.getConvenio().intValue()));
		}
		
		if(dtoFiltro.getContrato()!=null){
			criteria.add(Restrictions.eq("contrato.codigo",dtoFiltro.getContrato().intValue()));
		}
		
		Disjunction disjunction = Restrictions.disjunction();  
		disjunction.add( Property.forName("referenciasServicios.id.tipoTarifario").isNull());  
		disjunction.add( Property.forName("referenciasServicios.id.tipoTarifario").eq(Utilidades.convertirAEntero(esquemaTarServicios)));
		criteria.add(disjunction);
		
		/*Disjunction disjunctionOR = Restrictions.disjunction();  
			disjunctionOR.add( Property.forName("esquemaTarifarioArticulo.codigo").isNull());  
			disjunctionOR.add( Property.forName("esquemaTarifarioArticulo.codigo").eq(Utilidades.convertirAEntero(esquemaTarMedicamentos)));
		criteria.add(disjunctionOR);*/
		
		//RETORNA VALORES PARA SERVICIOS
		projection.add(Projections.property("servicio.codigo"),"codigoServicio");
		projection.add(Projections.property("nivelAtencionServicio.consecutivo"),"nivelAtencionServicio");
		projection.add(Projections.property("referenciasServicios.descripcion"),"nombreServicio");
		// MT 6729 se elimina restricci�n ya que este campo se almacena cuando se carga
		//projection.add(Projections.property("solicitudesSubcuentas.cantidad"),"cantidadServiciosSolicitudes");
		projection.add(Projections.property("grupoServicio.codigo"),"codigoGrupoServicio");
		projection.add(Projections.property("grupoServicio.descripcion"),"descripcionGrupoServicio");
		
		
		//RETORNA VALORES PARA ARTICULOS
		projection.add(Projections.property("articulo.codigo"),"codigoArticulo");
		projection.add(Projections.property("nivelAtencionArticulo.consecutivo"),"nivelAtencionArticulo");
		//MT6749 se elimina referencia ya que esta se trae de otra consulta
		//projection.add(Projections.property("tarifaArticulo.valorTarifa"),"tarifa");
		//projection.add(Projections.property("tarifaArticulo.fechaVigencia"),"fechaVigenciaTarifaArticulo");
		projection.add(Projections.property("articulo.descripcion"),"nombreArticulo");
		projection.add(Projections.property("detalleSolicitudeses.cantidad"),"cantidadArticulosSolicitudes");
		projection.add(Projections.property("articulo.subgrupo"),"subgrupoInventario");
		projection.add(Projections.property("esquemaTarifarioArticulo.codigo"),"esquemaTarifarioArticulo");
		
		projection.add(Projections.property("convenio.codigo"),"convenio");
		projection.add(Projections.property("contrato.codigo"),"contrato");
		projection.add(Projections.property("solicitud.fechaSolicitud"),"fecha");
		projection.add(Projections.property("solicitud.numeroSolicitud"),"numeroSolicitud");
		projection.add(Projections.property("anulacionSolicitud.fecha"),"fechaAnulacion");
		//MT6578 se adiciona orden ambulatoria
		projection.add(Projections.property("ordenesAmbulatoriases.codigo"),"codigoOrden");
		
		
		criteria.setProjection(Projections.distinct(projection));
		
		criteria.setResultTransformer( Transformers.aliasToBean(DtoResultadoConsultaProcesosCierre.class));
		ArrayList<DtoResultadoConsultaProcesosCierre> listadoOrdenesMedicas=
			(ArrayList<DtoResultadoConsultaProcesosCierre>)criteria.list();
	
		return listadoOrdenesMedicas;
		
	}	
	
	/**
	 * Este método consulta las solicitudes de orden ambulatoria anuladas, dependiendo de los párametros de consulta enviados
	 * @param dtoFiltro párametros de consulta
	 * @return ArrayList<DtoResultadoConsultaProcesosCierre>
	 * @author hermorhu
	 */
	public ArrayList<DtoResultadoConsultaProcesosCierre> consultarSolicitudesOrdenAmbAnuladas(DtoProcesoPresupuestoCapitado dtoFiltro){
		
		Criteria criteria= sessionFactory.getCurrentSession().createCriteria(Solicitudes.class,"solicitud")
		.createAlias("solicitud.solicitudesSubcuentas"					, "solicitudesSubcuentas"		,Criteria.LEFT_JOIN)
		.createAlias("solicitudesSubcuentas.subCuentas"					, "subCuentas"					,Criteria.LEFT_JOIN)
		.createAlias("subCuentas.convenios"								, "convenio"					,Criteria.INNER_JOIN)
		.createAlias("subCuentas.contratos"								, "contrato"					,Criteria.INNER_JOIN)
		//.createAlias("convenio.contratoses"								, "contrato"					,Criteria.INNER_JOIN)
		
		.createAlias("solicitudesSubcuentas.serviciosByServicio"		, "servicio"					, Criteria.LEFT_JOIN)
		.createAlias("servicio.referenciasServicios"					, "referenciasServicios"		, Criteria.LEFT_JOIN)
		.createAlias("servicio.nivelAtencion"							, "nivelAtencionServicio"		, Criteria.LEFT_JOIN)
		.createAlias("servicio.gruposServicios"							, "grupoServicio"		, Criteria.LEFT_JOIN)
		
		.createAlias("solicitud.solicitudesMedicamentos"			, "solicitudesMedicamentos"	, Criteria.LEFT_JOIN)
		.createAlias("solicitudesMedicamentos.detalleSolicitudeses"	, "detalleSolicitudeses"	, Criteria.LEFT_JOIN)
		.createAlias("detalleSolicitudeses.articuloByArticulo"		, "articulo"				, Criteria.LEFT_JOIN)
		.createAlias("articulo.nivelAtencion"						, "nivelAtencionArticulo"	, Criteria.LEFT_JOIN)
		.createAlias("articulo.tarifasInventarios"					, "tarifaArticulo"			, Criteria.LEFT_JOIN)
		.createAlias("tarifaArticulo.esquemasTarifarios"			, "esquemaTarifarioArticulo", Criteria.LEFT_JOIN)
		
		.createAlias("solicitud.solicitudesCirugia"			, "solicitudesCirugia"	, Criteria.LEFT_JOIN)
		.createAlias("solicitudesCirugia.solCirugiaPorServicios"			, "solCirugiaPorServicios"	, Criteria.LEFT_JOIN)
		.createAlias("solCirugiaPorServicios.servicios"			, "servicioCirugia"	, Criteria.LEFT_JOIN)
		
		.createAlias("solicitud.anulacionesSolicitud"			, "anulacionSolicitud", Criteria.INNER_JOIN)
		.createAlias("solicitud.tiposSolicitud"	 , "tiposSolicitud" ,Criteria.INNER_JOIN)
		//MT6578 se adiciona ordenesAmbulatoria
		.createAlias("solicitud.ordenesAmbulatoriases"	 , "ordenesAmbulatoriases" ,Criteria.INNER_JOIN)
		;
		
		ProjectionList projection = Projections.projectionList();
		
		String esquemaTarServicios = ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(dtoFiltro.getInstitucion());
		
		Log4JManager.info("++++++++Filtros Consulta Solicitudes++++++" +
				"\nEstado: "+ConstantesBD.codigoEstadoHCAnulada+
				"\nFecha Inicio: "+dtoFiltro.getFechaInicio()+
				"\nFecha Fin: "+dtoFiltro.getFechaFin()+
				"\nConvenio: "+dtoFiltro.getConvenio()+
				"\nContrato: "+dtoFiltro.getContrato());
				//"\nEsquema Tarifario Medicamentos: "+esquemaTarMedicamentos+
				//"\nTipo Tarifario Servicios: "+esquemaTarServicios);
		
		//FILTROS
		
		Disjunction disjunctionTipos = Restrictions.disjunction();  
		disjunctionTipos.add( Restrictions.eq("tiposSolicitud.codigo", ConstantesBD.codigoTipoSolicitudInterconsulta));  
		disjunctionTipos.add( Restrictions.eq("tiposSolicitud.codigo", ConstantesBD.codigoTipoSolicitudProcedimiento));
		disjunctionTipos.add( Restrictions.eq("tiposSolicitud.codigo", ConstantesBD.codigoTipoSolicitudMedicamentos));
		//MT6578 se adiciona codigoPeticion se adiciona las consultas
		disjunctionTipos.add( Restrictions.eq("tiposSolicitud.codigo", ConstantesBD.codigoTipoSolicitudCita));
		criteria.add(disjunctionTipos);
		
		if(dtoFiltro.getFechaInicio()!=null&& dtoFiltro.getFechaFin()!=null){
			//hermorhu - MT6601
			//Se trae las Ordenes Ambulatorias Anuladas para ese rango de fechas
			criteria.add(Restrictions.between("anulacionSolicitud.fecha", dtoFiltro.getFechaInicio(), dtoFiltro.getFechaFin()));
		}
		
		criteria.add(Restrictions.eq("subCuentas.nroPrioridad",1));
		
		if(dtoFiltro.getConvenio()!=null){
			criteria.add(Restrictions.eq("convenio.codigo",dtoFiltro.getConvenio().intValue()));
		}
		
		if(dtoFiltro.getContrato()!=null){
			criteria.add(Restrictions.eq("contrato.codigo",dtoFiltro.getContrato().intValue()));
		}
		
		Disjunction disjunction = Restrictions.disjunction();  
		disjunction.add( Property.forName("referenciasServicios.id.tipoTarifario").isNull());  
		disjunction.add( Property.forName("referenciasServicios.id.tipoTarifario").eq(Utilidades.convertirAEntero(esquemaTarServicios)));
		criteria.add(disjunction);

		//RETORNA VALORES PARA SERVICIOS
		projection.add(Projections.property("servicio.codigo"),"codigoServicio");
		projection.add(Projections.property("nivelAtencionServicio.consecutivo"),"nivelAtencionServicio");
		projection.add(Projections.property("referenciasServicios.descripcion"),"nombreServicio");
		// MT 6729 se elimina restricci�n ya que este campo se almacena cuando se carga
		//projection.add(Projections.property("solicitudesSubcuentas.cantidad"),"cantidadServiciosSolicitudes");
		projection.add(Projections.property("grupoServicio.codigo"),"codigoGrupoServicio");
		projection.add(Projections.property("grupoServicio.descripcion"),"descripcionGrupoServicio");
		
		
		//RETORNA VALORES PARA ARTICULOS
		projection.add(Projections.property("articulo.codigo"),"codigoArticulo");
		projection.add(Projections.property("nivelAtencionArticulo.consecutivo"),"nivelAtencionArticulo");

		projection.add(Projections.property("articulo.descripcion"),"nombreArticulo");
		projection.add(Projections.property("detalleSolicitudeses.cantidad"),"cantidadArticulosSolicitudes");
		projection.add(Projections.property("articulo.subgrupo"),"subgrupoInventario");
		projection.add(Projections.property("esquemaTarifarioArticulo.codigo"),"esquemaTarifarioArticulo");
		
		projection.add(Projections.property("convenio.codigo"),"convenio");
		projection.add(Projections.property("contrato.codigo"),"contrato");
		//projection.add(Projections.property("solicitud.fechaSolicitud"),"fecha");
		
		projection.add(Projections.property("anulacionSolicitud.fecha"),"fechaAnulacion");
		//MT6578 se adiciona orden ambulatoria
		projection.add(Projections.property("ordenesAmbulatoriases.codigo"),"codigoOrden");
		
		criteria.setProjection(Projections.distinct(projection));
		
		criteria.setResultTransformer( Transformers.aliasToBean(DtoResultadoConsultaProcesosCierre.class));
		ArrayList<DtoResultadoConsultaProcesosCierre> listadoOrdenesMedicas=
			(ArrayList<DtoResultadoConsultaProcesosCierre>)criteria.list();
	
		return listadoOrdenesMedicas;
		
	}	

	/**
	 * Este método consulta las Solicitudes Medicamentos por Cuenta, con tipoSolicitud = Medicamento y 
	 * estadoSolicitud != Anulada y Administrada MT 1430 
	 *  
	 * @param cuenta
	 * @return true si no contiene solicitudes en otros estados 
	 * @return false si contiene por lo menos una solicitud diferente a anulado o administrado
	 */
	public boolean consultarSolicitudesMedicamentosPorCuenta(int cuenta)
	{	
		boolean egresoValido=false;
		
		try
		{
			HibernateUtil.beginTransaction();
			
			Criteria criteria= sessionFactory.getCurrentSession().createCriteria(Solicitudes.class,"solicitud")
				.createAlias("solicitud.cuentas"	, "cuenta"	)
				.createAlias("solicitud.tiposSolicitud"	 , "tiposSolicitud" ,Criteria.INNER_JOIN)
			;
			
			ProjectionList projection = Projections.projectionList();
			
			Log4JManager.info("++++++++Filtros Consulta Solicitudes++++++" +
					"\nCuenta: "+cuenta+
					"\nTipo:  Medicamentos="+ConstantesBD.codigoTipoSolicitudMedicamentos+
					"\nEstados: Administrada="+ConstantesBD.codigoEstadoHCAdministrada+"-Anulada="+ConstantesBD.codigoEstadoHCAnulada
					);
			
			Integer []estados={ConstantesBD.codigoEstadoHCAdministrada,ConstantesBD.codigoEstadoHCAnulada};
			
			criteria.add(Restrictions.eq("cuenta.id",cuenta));
			criteria.add(Restrictions.eq("tiposSolicitud.codigo",ConstantesBD.codigoTipoSolicitudMedicamentos));
			criteria.add(Restrictions.not(Restrictions.in("solicitud.estadoHistoriaClinica",estados)));
			
			projection.add(Projections.property("tiposSolicitud"),"tiposSolicitud");
			projection.add(Projections.property("solicitud.estadoHistoriaClinica"),"estadoHistoriaClinica");
			
			criteria.setProjection(Projections.distinct(projection));
			
			criteria.setResultTransformer( Transformers.aliasToBean(Solicitudes.class));
			ArrayList<Solicitudes> listadoOrdenesMedicamentos=(ArrayList<Solicitudes>)criteria.list();
			
			
			if(Utilidades.isEmpty(listadoOrdenesMedicamentos))
				egresoValido=true;
		}
		catch (Exception e)
		{
			HibernateUtil.abortTransaction();
			Log4JManager.error(e);
			e.printStackTrace();
		}
		finally
		{
			HibernateUtil.endTransaction();
		}
		
		return egresoValido;
	}


	/**
	 * @param solicitud
	 * @return lista con articulos ya autorizados 
	 */
	public  DtoSolicitud obtenerDetalleArticulosSolicitudesPorCuentaAutorizados(DtoSolicitud solicitud){
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Solicitudes.class,"solicitudes");


		criteria.createAlias("solicitudes.solicitudesMedicamentos"			, "solicitudesMedicamentos"	, Criteria.INNER_JOIN);
		criteria.createAlias("solicitudesMedicamentos.detalleSolicitudeses"	, "detalleSolicitudeses"	, Criteria.INNER_JOIN);
		criteria.createAlias("detalleSolicitudeses.articuloByArticulo"		, "articuloByArticulo"		, Criteria.INNER_JOIN);
		criteria.createAlias("detalleSolicitudeses.unidosisXArticulo"		, "unidosisXArticulo"		, Criteria.LEFT_JOIN);
		criteria.createAlias("unidosisXArticulo.unidadMedida"				, "unidadMedida"			, Criteria.LEFT_JOIN);
		criteria.createAlias("articuloByArticulo.naturalezaArticulo"		, "naturalezaArticulo"		, Criteria.LEFT_JOIN);
		criteria.createAlias("solicitudes.diagnosticos"						, "diagnosticos"			, Criteria.LEFT_JOIN);
		criteria.createAlias("solicitudes.autoEntsubSolicitudeses"					, "autoEntsubSolicitudeses");
		criteria.createAlias("autoEntsubSolicitudeses.autorizacionesEntidadesSub"	, "autorizacionesEntidadesSub");
		criteria.createAlias("autorizacionesEntidadesSub.autorizacionesEntSubArticus"	, "autorizacionesEntSubArticus");

		criteria.add(Restrictions.eq("solicitudesMedicamentos.numeroSolicitud"	, solicitud.getNumeroSolicitud()));
		


		ProjectionList projectionList = Projections.projectionList();
		//--Medicamento
		projectionList.add(Projections.property("articuloByArticulo.codigo")				, "codigoArticulo");
		projectionList.add(Projections.property("articuloByArticulo.descripcion")			, "descripcionArticulo");
		projectionList.add(Projections.property("articuloByArticulo.concentracion")			, "concentracionArticulo");
		projectionList.add(Projections.property("articuloByArticulo.formaFarmaceutica")		, "formaFarmaceuticaArticulo");
		projectionList.add(Projections.property("unidadMedida.nombre")						, "unidadMedidaArticulo");
		projectionList.add(Projections.property("unidadMedida.acronimo")					, "acronimoUnidadMedidaArticulo");
		projectionList.add(Projections.property("unidosisXArticulo.codigo")					, "dosisXArticuloID");			
		projectionList.add(Projections.property("naturalezaArticulo.esMedicamento")			, "esMedicamento");
		projectionList.add(Projections.property("naturalezaArticulo.nombre")				, "naturalezaArticulo"); 

		//- Formulación 	
		projectionList.add(Projections.property("detalleSolicitudeses.dosis")				, "dosisFormulacion");
		projectionList.add(Projections.property("detalleSolicitudeses.frecuencia")			, "frecuenciaFormulacion");
		projectionList.add(Projections.property("detalleSolicitudeses.via")					, "viaFormulacion");
		projectionList.add(Projections.property("detalleSolicitudeses.diasTratamiento")		, "diasTratamientoFormulacion");
		projectionList.add(Projections.property("detalleSolicitudeses.cantidad")			, "totalUnidadesFormulacion");
		projectionList.add(Projections.property("detalleSolicitudeses.tipoFrecuencia")		, "tipoFrecuenciaFormulacion");

		//--Orden
		projectionList.add(Projections.property("solicitudes.consecutivoOrdenesMedicas")	, "consecutivoOrdenMed");
		projectionList.add(Projections.property("solicitudesMedicamentos.numeroSolicitud")	, "numeroOrden");
		projectionList.add(Projections.property("solicitudes.fechaSolicitud")				, "fechaOrden");
		projectionList.add(Projections.property("solicitudes.horaSolicitud")				, "horaOrden");
		//--Diagnostico
		projectionList.add(Projections.property("diagnosticos.id.acronimo")					, "acronimoDx");
		projectionList.add(Projections.property("diagnosticos.id.tipoCie")					, "tipoCieDx");
		projectionList.add(Projections.property("diagnosticos.nombre")						, "diagnostico");

		criteria.setProjection(Projections.distinct(projectionList));

		criteria.setResultTransformer(Transformers.aliasToBean(DtoArticulosAutorizaciones.class));


		ArrayList<DtoArticulosAutorizaciones> listaArticulos = (ArrayList<DtoArticulosAutorizaciones>)criteria.list();
		solicitud.setListaArticulosAutorizados(listaArticulos);
		return solicitud;
	}

	/**
	 * @param solicitud
	 * @param parametros
	 * @return  ArrayList<DtoSolicitud>
	 */
	public  ArrayList<DtoSolicitud>  obtenerSolciitudesAutorizadas(DtoSolicitud solicitud,Integer[] parametros){
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Solicitudes.class,"solicitudes");
		
		criteria.createAlias("solicitudes.autoEntsubSolicitudeses"					, "autoEntsubSolicitudeses");
		criteria.createAlias("autoEntsubSolicitudeses.autorizacionesEntidadesSub"	, "autorizacionesEntidadesSub");
		criteria.createAlias("autorizacionesEntidadesSub.autorizacionesEntSubArticus"	, "autorizacionesEntSubArticus");
		
		if(parametros != null && parametros.length>0)
		{
			criteria.add(Restrictions.in("autorizacionesEntSubArticus.articulo.codigo",parametros));
		}
		
		
		criteria.add(Restrictions.eq("solicitudesMedicamentos.numeroSolicitud"	, solicitud.getNumeroSolicitud()));
		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Projections.property("autorizacionesEntSubArticus.articulo.codigo") , "numeroSolicitud");
		
		criteria.setProjection(projectionList);
		
		criteria.setResultTransformer(Transformers.aliasToBean(DtoSolicitud.class));
		
		ArrayList<DtoSolicitud> listaArticulosSolicitudes= (ArrayList<DtoSolicitud>)criteria.list();
		
		return listaArticulosSolicitudes;
	}
	
	/**
	 * Reporte de Policonsultadores
	 * 
	 * @author Cesar Gomez
	 * @return ArrayList<DtoPacientesPoliconsultadores>
	*/
	public ArrayList<DtoPacientesPoliconsultadores> obtenerReportePoliconsultadores( 
					String fechaInicial, 
					String fechaFinal, 
					int idConvenio, 
					String tipoIdentificacion, 
					String numeroIdentificacion, 
					int idViaIngreso,
					int idEspecialidad,
					int idUnidadAgenda,
					String tipoServicio,Integer codigoPersona ) {	

		ArrayList<DtoPacientesPoliconsultadores> listaServicios = new ArrayList<DtoPacientesPoliconsultadores>();
		String consulta="SELECT new com.princetonsa.dto.manejoPaciente.DtoPacientesPoliconsultadores( " +
		"					conv.nombre, " +
		"					per.primerNombre, " +
		"					per.segundoNombre, " +
		"					per.primerApellido, " +
		"					per.segundoApellido, " +
		"					tipoIde.acronimo, " +
		"					per.numeroIdentificacion, " +
		"					conv.codigo," +		 		
		"					via.codigo," +
		"					paci.codigoPaciente, " +
		"					via.nombre, " +
		"					ts.nombre, " +
		"					esp.codigo, " +
		"					esp.nombre, " +		 		
		"					uni.descripcion, " +
		"					sum(1) ) ";		 		
		consulta += "FROM Facturas fact " +

		" INNER JOIN fact.cuentas cuen " +
		" INNER JOIN cuen.pacientes paci " +
		" INNER JOIN paci.personas per " +
		" INNER JOIN per.tiposIdentificacion tipoIde " +
		" INNER JOIN cuen.viasIngreso via " +
		" INNER JOIN fact.convenios conv " +
		" INNER JOIN fact.detFacturaSolicituds soli " +
		" INNER JOIN soli.servicios serv " +
		" INNER JOIN serv.especialidades esp " +
		" INNER JOIN serv.tiposSerNaturaleza tsn " +
		" INNER JOIN tsn.tiposServicio ts " +
		" INNER JOIN soli.solicitudes solic " +
		" LEFT JOIN solic.serviciosCitas  solSerCita " +
		" LEFT JOIN solSerCita.cita cit " +
		" LEFT JOIN  cit.unidadesConsulta uni ";
		//			 



		//					      	"INNER JOIN fact.cuentas cuen " +
		//					      	"INNER JOIN fact.convenios conv " +
		//					      	"INNER JOIN cuen.pacientes paci " +
		//					      	"INNER JOIN cuen.viasIngreso via " +
		//		 					"INNER JOIN paci.personas per " +
		//	 						"INNER JOIN per.tiposIdentificacion tipoIde "+ 
		//	 						"INNER JOIN fact.detFacturaSolicituds soli " +
		//					      	"INNER JOIN soli.servicios serv " +
		//					      	"INNER JOIN serv.especialidades esp " + 					      	
		//							"INNER JOIN serv.tiposSerNaturaleza tsn " + 					      	
		//							"INNER JOIN tsn.tiposServicio ts "+
		//							" LEFT JOIN esp.unidadesConsultas uni "+
		//							" LEFT JOIN uni.citas cit ";
		if( !tipoIdentificacion.equals("") && !numeroIdentificacion.equals("") ){
			consulta += "WHERE tipoIde.acronimo = :tipoIdentificacion AND per.numeroIdentificacion = :numeroIdentificacion ";
		}else{
			consulta += "WHERE fact.fecha BETWEEN to_date(:fechaInicial,'dd/MM/yyyy') AND to_date(:fechaFinal,'dd/MM/yyyy') ";
		}
		if( idConvenio != -1 ){
			consulta+= "AND conv.codigo = :idConvenio ";
		}
		if( idViaIngreso != -1 ){
			consulta+= "AND via.codigo = :idViaIngreso ";
		}
		if( !tipoServicio.isEmpty() ){
			consulta+= "AND ts.acronimo = :tipoServicio ";
		}
		if( idEspecialidad != -1 ){
			consulta+= "AND esp.codigo = :idEspecialidad ";
		}
		if( idUnidadAgenda != -1 ){
			consulta+= "AND uni.codigo = :idUnidadAgenda ";
		}

		consulta+= " AND conv.capitacionSubcontratada = :esCapitado ";
		consulta += " GROUP BY 	conv.nombre, " +
		"					per.primerNombre, " +
		"					per.segundoNombre, " +
		"					per.primerApellido, " +
		"					per.segundoApellido, " +
		"					tipoIde.acronimo, " +
		"					per.numeroIdentificacion, " +
		"					conv.codigo," +		 		
		"					via.codigo," +
		"					paci.codigoPaciente, " +
		"					via.nombre, " +
		"					ts.nombre, " +
		"					esp.codigo, " +
		"					esp.nombre, " +
		"					uni.descripcion ORDER BY conv.nombre, per.numeroIdentificacion, via.nombre, ts.nombre, esp.nombre";						 							

		Query query = sessionFactory.getCurrentSession().createQuery(consulta);			 

		if( !tipoIdentificacion.equals("") && !numeroIdentificacion.equals("") ){
			query.setParameter("tipoIdentificacion", tipoIdentificacion, Hibernate.STRING);
			query.setParameter("numeroIdentificacion", numeroIdentificacion, Hibernate.STRING);
		}else{
			query.setParameter("fechaInicial", fechaInicial, Hibernate.STRING);
			query.setParameter("fechaFinal", fechaFinal, Hibernate.STRING);
		}
		if( idConvenio != -1 ){
			query.setParameter("idConvenio", idConvenio, Hibernate.INTEGER);
		}
		if( idViaIngreso != -1 ){
			query.setParameter("idViaIngreso", idViaIngreso, Hibernate.INTEGER);
		}
		if( !tipoServicio.isEmpty() ){
			query.setParameter("tipoServicio", tipoServicio, Hibernate.STRING);
		}
		if( idEspecialidad != -1 ){
			query.setParameter("idEspecialidad", idEspecialidad, Hibernate.INTEGER);
		}
		if( idUnidadAgenda != -1 ){
			query.setParameter("idUnidadAgenda", idUnidadAgenda, Hibernate.INTEGER);
		}
		query.setParameter("esCapitado", ConstantesBD.acronimoSi, Hibernate.STRING);

		listaServicios = (ArrayList<DtoPacientesPoliconsultadores>)query.list();
		return listaServicios;
	}

}
