package com.servinte.axioma.orm.delegate.ordenes;

import java.util.ArrayList;

import org.axioma.util.log.Log4JManager;
import org.hibernate.Criteria;
import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.dto.capitacion.DtoProcesoPresupuestoCapitado;
import com.princetonsa.dto.capitacion.DtoResultadoConsultaProcesosCierre;
import com.princetonsa.dto.inventario.DtoArticulosAutorizaciones;
import com.princetonsa.dto.inventario.DtoServiciosAutorizaciones;
import com.princetonsa.dto.ordenes.DtoOrdenesAmbulatorias;
import com.princetonsa.dto.ordenes.DtoSolicitud;
import com.servinte.axioma.dto.capitacion.DtoAutorizacionCapitacionOrdenAmbulatoria;
import com.servinte.axioma.orm.OrdenesAmbulatorias;
import com.servinte.axioma.orm.OrdenesAmbulatoriasHome;

public class OrdenesAmbulatoriasDelegate extends OrdenesAmbulatoriasHome
{

	
	/**
	 * Retorna las ordenes ambulatorias de Servicios E Insumos y Medicamentos
	 *  
	 * @param parametros
	 * @return ArrayList<DtoOrdenesAmbulatorias> 
	 * @autor Camilo Gómez
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<DtoOrdenesAmbulatorias> obtenerOrdenesAmbulatorias(DtoOrdenesAmbulatorias parametros)
	{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(OrdenesAmbulatorias.class,"ordenesAmbulatorias");
			
		//criteria.createAlias("ordenesAmbulatorias.especialidades"				, "especialidad"	);
		criteria.createAlias("ordenesAmbulatorias.tiposOrdenesAmbulatorias"		, "tipoOrden"		);
		criteria.createAlias("ordenesAmbulatorias.estadosOrdAmbulatorias"		, "estadosOrden"	);
		criteria.createAlias("ordenesAmbulatorias.anulacionOrdenAmbulatorias"	, "anulacionOrden"	,Criteria.LEFT_JOIN);
		criteria.createAlias("anulacionOrden.usuarios"							, "anulacionUsuario",Criteria.LEFT_JOIN);
		criteria.createAlias("anulacionUsuario.personas"						, "anulacionPersona",Criteria.LEFT_JOIN);
				
		criteria.createAlias("ordenesAmbulatorias.usuariosByUsuarioSolicita"	, "usuarioSolicita"	);
		criteria.createAlias("usuarioSolicita.personas"							, "profesional"		);
		criteria.createAlias("profesional.medicos"								, "medico"			);
		criteria.createAlias("profesional.tiposIdentificacion"					, "tipoIdProf"		);
		criteria.createAlias("ordenesAmbulatorias.centroAtencion"				, "centroAtencion"	);
		criteria.createAlias("centroAtencion.instituciones"						, "institucion"	);		
		criteria.createAlias("ordenesAmbulatorias.ingresos"						, "ingreso"		,Criteria.LEFT_JOIN);
		criteria.createAlias("ordenesAmbulatorias.cuentas"						, "cuenta"		,Criteria.LEFT_JOIN);
		
		criteria.createAlias("ingreso.subCuentases"								, "subCuenta"		,Criteria.LEFT_JOIN);
		criteria.createAlias("subCuenta.tiposAfiliado"							, "tipoAfiliado"	,Criteria.LEFT_JOIN);		
		criteria.createAlias("subCuenta.estratosSociales"						, "estratoSocial"	,Criteria.LEFT_JOIN);
		criteria.createAlias("estratoSocial.tiposRegimen"						, "regimenT"		,Criteria.LEFT_JOIN);
		criteria.createAlias("subCuenta.convenios"								, "convenio"        ,Criteria.LEFT_JOIN);
		
		criteria.createAlias("ordenesAmbulatorias.pacientes"					, "pacientes"		);
		criteria.createAlias("pacientes.personas"								, "personas"		);
		criteria.createAlias("personas.sexo"									, "sexo"		);
		criteria.createAlias("personas.ciudadesByFkPCiudadviv"					, "mun"		);
		criteria.createAlias("mun.departamentos"								, "dep"		);
		criteria.createAlias("personas.tiposIdentificacion"						, "tiposIdentificacion"	);

		criteria.createAlias("ordenesAmbulatorias.detOrdenAmbServicio"		, "detOrdenAmbServicio"		,Criteria.LEFT_JOIN);
		criteria.createAlias("detOrdenAmbServicio.servicios"				, "servicio"				,Criteria.LEFT_JOIN);
		criteria.createAlias("detOrdenAmbServicio.finalidadesServicio"		, "finalidadServicio"		,Criteria.LEFT_JOIN);
		criteria.createAlias("servicio.referenciasServicios"				, "referenciaServicio"		,Criteria.LEFT_JOIN);
		criteria.createAlias("referenciaServicio.tarifariosOficiales"		, "tarifariosOficiales"		,Criteria.LEFT_JOIN);
				
		criteria.createAlias("ordenesAmbulatorias.detOrdenAmbArticulos"			, "detOrdenAmbArticulo"		,Criteria.LEFT_JOIN);		
		criteria.createAlias("detOrdenAmbArticulo.articulo"						, "articulo"				,Criteria.LEFT_JOIN);
		criteria.createAlias("articulo.formaFarmaceutica"						, "forma"					,Criteria.LEFT_JOIN);
		criteria.createAlias("detOrdenAmbArticulo.viasAdministracion"			, "viaAdministracion"		,Criteria.LEFT_JOIN);
		criteria.createAlias("detOrdenAmbArticulo.tiposFrecuencia"				, "tipoFrecuencia"			,Criteria.LEFT_JOIN);		
		criteria.createAlias("articulo.unidadMedida"							, "unidadMedida"			,Criteria.LEFT_JOIN);	
		
		/*	Corrección Incidencia 1758 mostrar la dosis del medicamento */
		criteria.createAlias("detOrdenAmbArticulo.unidosisXArticulo"			, "unidosisXArticulo"		,Criteria.LEFT_JOIN);
		criteria.createAlias("unidosisXArticulo.unidadMedida"				    , "unidadMedidaUni"		,Criteria.LEFT_JOIN);
		
		criteria.createAlias("ordenesAmbulatorias.diagnosticos"					, "diagnosticos"	,Criteria.LEFT_JOIN);		
		
		
		// -----------------------------------PARAMETROS DE BUSQUEDA-------------------------------------------------
		if (!UtilidadTexto.isEmpty(parametros.getNumeroOrden())){
			criteria.add(Restrictions.eq("ordenesAmbulatorias.codigo", Utilidades.convertirALong(parametros.getNumeroOrden())));
		}else if(!Utilidades.isEmpty(parametros.getParametrosOrdenesAmbulatorias()))
		{	
			criteria.add(Restrictions.in("ordenesAmbulatorias.consecutivoOrden", parametros.getParametrosOrdenesAmbulatorias()));
		}
	
		Disjunction disjunctionPrioridad = Restrictions.disjunction();  
		disjunctionPrioridad.add( Property.forName("subCuenta.nroPrioridad").eq(1));  
		Conjunction conjunction = Restrictions.conjunction();
		conjunction.add(Property.forName("subCuenta.nroPrioridad").isNull());
		conjunction.add(Property.forName("ingreso.consecutivo").isNull());
		disjunctionPrioridad.add(conjunction);
		criteria.add(disjunctionPrioridad);
		
		Integer tipoTarifario = Integer.parseInt(ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(parametros.getInstitucion()));
		Disjunction disjunction = Restrictions.disjunction();  
		disjunction.add( Property.forName("referenciaServicio.id.tipoTarifario").isNull());  
		disjunction.add( Property.forName("referenciaServicio.id.tipoTarifario").eq(tipoTarifario));
		criteria.add(disjunction);
		
		
		Log4JManager.info("--------------------->PARAMETROS DE BUSQUEDA<---------------------");
		int i=1;
		for (String ambul : parametros.getParametrosOrdenesAmbulatorias())
			Log4JManager.info((i++)+"--->ordenes para Imprimir: "+ambul);
		Log4JManager.info("--->Institucion: "+parametros.getInstitucion());	
		Log4JManager.info("--->TipoTarifarioServicios:   "+tipoTarifario);
		Log4JManager.info("--->NroPrioridadSubcuenta:   "+1);

		
		//----------------------------------------------------------------------------
		ProjectionList projectionList = Projections.projectionList();
			//Datos ordenes
			projectionList.add(Projections.property("ordenesAmbulatorias.consecutivoOrden")			,"numeroOrden");
			projectionList.add(Projections.property("tipoOrden.descripcion")						,"tipoOrden");
			projectionList.add(Projections.property("ordenesAmbulatorias.fecha")					,"fechaOrden");
			projectionList.add(Projections.property("usuarioSolicita.login")						,"loginMedico");
			projectionList.add(Projections.property("profesional.primerNombre")						,"primerNombreMedico");
			projectionList.add(Projections.property("profesional.primerApellido")					,"primerApellidoMedico");
			projectionList.add(Projections.property("profesional.segundoNombre")					,"segundoNombreMedico");
			projectionList.add(Projections.property("profesional.segundoApellido")					,"segundoApellidoMedico");
			projectionList.add(Projections.property("profesional.numeroIdentificacion")				,"numeroIdMedico");
			projectionList.add(Projections.property("tipoIdProf.acronimo")							,"tipoIdMedico");
			projectionList.add(Projections.property("medico.numeroRegistro")						,"registroMedico");
			projectionList.add(Projections.property("medico.firmaDigital")							,"firmaDigitalMedico");
			projectionList.add(Projections.property("estadosOrden.codigo")						,"estadoOrden");	
			projectionList.add(Projections.property("estadosOrden.descripcion")					,"estadoOrdenStr");	
			projectionList.add(Projections.property("anulacionOrden.motivoAnulacion")			,"motivoAnulacion");
			projectionList.add(Projections.property("anulacionOrden.fecha")						,"fechaAnulacion");	
			projectionList.add(Projections.property("anulacionOrden.hora")						,"horaAnulacion");	
			projectionList.add(Projections.property("anulacionPersona.primerNombre")			,"primerNombreAnulacion");
			projectionList.add(Projections.property("anulacionPersona.primerApellido")			,"primerApellidoAnulacion");
			projectionList.add(Projections.property("anulacionPersona.segundoNombre")			,"segundoNombreAnulacion");	
			projectionList.add(Projections.property("anulacionPersona.segundoApellido")			,"segundoApellidoAnulacion");
			projectionList.add(Projections.property("ordenesAmbulatorias.urgente")					,"urgente");
			projectionList.add(Projections.property("ordenesAmbulatorias.hora")						,"hora");
			projectionList.add(Projections.property("ordenesAmbulatorias.observaciones")			,"observaciones");
			projectionList.add(Projections.property("ordenesAmbulatorias.otros")					,"otros");
			projectionList.add(Projections.property("ordenesAmbulatorias.controlEspecial")			,"controlEspecial");
			//projectionList.add(Projections.property("especialidad.nombre")							,"especialidad");
			projectionList.add(Projections.property("institucion.razonSocial")						,"razonSocial");
			projectionList.add(Projections.property("institucion.actividadEco")						,"actividadEconomica");			
			projectionList.add(Projections.property("institucion.direccion")						,"direccion");
			projectionList.add(Projections.property("institucion.indicativo")						,"indicativo");
			projectionList.add(Projections.property("institucion.telefono")							,"telefono");
			projectionList.add(Projections.property("institucion.nit")								,"nit");
			projectionList.add(Projections.property("centroAtencion.descripcion")					,"centroAtencion");
			projectionList.add(Projections.property("convenio.nombre")								,"convenio");
			projectionList.add(Projections.property("tipoAfiliado.nombre")							,"tipoAfiliado");
			/**
			 * Inc 1656
			 * Se debe mostrar si la orden es de PYP
			 * Diana Ruiz
			 */			
			projectionList.add(Projections.property("ordenesAmbulatorias.pyp")			,"pyp");
			//Datos Paciente	
			projectionList.add(Projections.property("tiposIdentificacion.acronimo")		,"tipoId");
			projectionList.add(Projections.property("personas.numeroIdentificacion")	,"numeroId");
			projectionList.add(Projections.property("personas.primerNombre")			,"primerNombre");
			projectionList.add(Projections.property("personas.segundoNombre")			,"segundoNombre");
			projectionList.add(Projections.property("personas.primerApellido")			,"primerApellido");
			projectionList.add(Projections.property("personas.segundoApellido")			,"segundoApellido");
			projectionList.add(Projections.property("personas.segundoApellido")			,"segundoApellido");
			projectionList.add(Projections.property("sexo.nombre")						,"sexo");
			projectionList.add(Projections.property("personas.direccion")				,"dirPaciente");
			projectionList.add(Projections.property("mun.descripcion")		  			,"mun");
			projectionList.add(Projections.property("dep.descripcion")		  			,"dpto");
			projectionList.add(Projections.property("pacientes.historiaClinica")		,"historia");
			projectionList.add(Projections.property("regimenT.nombre")					,"regimen");
			projectionList.add(Projections.property("personas.fechaNacimiento")			,"fechaNacimiento");
			projectionList.add(Projections.property("personas.telefono")				,"telefonoPersona");
			projectionList.add(Projections.property("estratoSocial.descripcion")		,"categoria");			
			projectionList.add(Projections.property("ingreso.consecutivo")				,"ingreso");
			projectionList.add(Projections.property("cuenta.id")						,"cuenta");
			projectionList.add(Projections.property("diagnosticos.id.acronimo")			,"acronimoDx");
			projectionList.add(Projections.property("diagnosticos.id.tipoCie")			,"tipoCieDx");
			
			//Datos Servicios			
			projectionList.add(Projections.property("servicio.codigo")						,"codigoServicio");
			projectionList.add(Projections.property("tarifariosOficiales.nombre")			,"cups");
			projectionList.add(Projections.property("referenciaServicio.descripcion")		,"nombreservicio");
			projectionList.add(Projections.property("referenciaServicio.codigoPropietario")	,"codigoPropietario");			
			projectionList.add(Projections.property("servicio.espos")						,"esPos");
			projectionList.add(Projections.property("finalidadServicio.nombre")				,"finalidadServicio");
			projectionList.add(Projections.property("detOrdenAmbServicio.cantidad")			,"cantidad");
			//Datos Articulos
			projectionList.add(Projections.property("articulo.codigo")							,"codigoArticulo");
			/**
			 * Inc 1869
			 * Mostrar la concentración del articulo y la palabra cada en la frecuencia.
			 * Diana Ruiz
			 */			
			projectionList.add(Projections.property("articulo.concentracion")					,"concentracion");
			projectionList.add(Projections.property("articulo.codigoInterfaz")					,"codigoInterfaz");			
			projectionList.add(Projections.property("articulo.descripcion")						,"nombreArticulo");
			projectionList.add(Projections.property("forma.nombre")								,"forma");
			projectionList.add(Projections.property("unidadMedida.acronimo")					,"unidadMedida");
			
			/*	Corrección Incidencia 1758 mostrar la dosis del medicamento */
			projectionList.add(Projections.property("unidadMedidaUni.acronimo")					,"unidadMedidaDosis");
			projectionList.add(Projections.property("unidosisXArticulo.cantidad")				,"cantidadUnidadMedidaDosis");
			
			projectionList.add(Projections.property("detOrdenAmbArticulo.dosis")				,"dosis");
			projectionList.add(Projections.property("viaAdministracion.nombre")					,"via");
			projectionList.add(Projections.property("detOrdenAmbArticulo.frecuencia")			,"frecuencia");
			projectionList.add(Projections.property("tipoFrecuencia.nombre")					,"tipoFrecuencia");
			projectionList.add(Projections.property("detOrdenAmbArticulo.cantidad")				,"cantidadArticulo");
			projectionList.add(Projections.property("detOrdenAmbArticulo.medicamento")			,"medicamento");
			projectionList.add(Projections.property("detOrdenAmbArticulo.duracionTratamiento")	,"diasTratamiento");
			projectionList.add(Projections.property("detOrdenAmbArticulo.observaciones")		,"observacionesMedicamentos");
			
				
			criteria.setProjection(projectionList);
			//criteria.addOrder(Property.forName("ordenesAmbulatorias.fecha").asc());
		
		criteria.setResultTransformer(Transformers.aliasToBean(DtoOrdenesAmbulatorias.class));
		ArrayList<DtoOrdenesAmbulatorias> listaOrdenesAmbulatorias = (ArrayList<DtoOrdenesAmbulatorias>)criteria.list();
		
		return listaOrdenesAmbulatorias;
	}
	
	/**
	 * Este método consulta las ordenes ambulatorias en el sistema dependiendo de los parámetros enviados
	 * @param dtoFiltro párametros de consulta
	 * @return ArrayList<DtoResultadoConsultaProcesosCierre> lista de ordenes ambulatorias
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<DtoResultadoConsultaProcesosCierre> consultarOrdenesAmbulatorias(DtoProcesoPresupuestoCapitado dtoFiltro){
		
		Criteria criteria= sessionFactory.getCurrentSession().createCriteria(OrdenesAmbulatorias.class,"ordenAmbulatoria")
		.createAlias("ordenAmbulatoria.ingresos", "ingreso")
		.createAlias("ingreso.subCuentases", "subcuenta")
		.createAlias("subcuenta.convenios", "convenio")
		.createAlias("subcuenta.contratos", "contrato")
		//.createAlias("convenio.contratoses", "contrato")
		
		.createAlias("ordenAmbulatoria.detOrdenAmbServicio", "detalleOrdenServicio", Criteria.LEFT_JOIN)
		.createAlias("detalleOrdenServicio.servicios", "servicio", Criteria.LEFT_JOIN)
		.createAlias("servicio.referenciasServicios", "referenciasServicios" , Criteria.LEFT_JOIN)
		.createAlias("servicio.nivelAtencion", "nivelAtencionServicio", Criteria.LEFT_JOIN)
		//MT 6578
		.createAlias("servicio.gruposServicios", "grupoServicio",Criteria.LEFT_JOIN)
		
		.createAlias("ordenAmbulatoria.detOrdenAmbArticulos", "detalleOrdenArticulo", Criteria.LEFT_JOIN)
		.createAlias("detalleOrdenArticulo.articulo", "articulo", Criteria.LEFT_JOIN)
		.createAlias("articulo.nivelAtencion", "nivelAtencionArticulo", Criteria.LEFT_JOIN)
		.createAlias("articulo.tarifasInventarios", "tarifaArticulo", Criteria.LEFT_JOIN)
		.createAlias("tarifaArticulo.esquemasTarifarios", "esquemaTarifarioArticulo", Criteria.LEFT_JOIN)
		
		.createAlias("ordenAmbulatoria.anulacionOrdenAmbulatorias", "anulacionOrden", Criteria.LEFT_JOIN);
		
		ProjectionList projection = Projections.projectionList();
		
		String esquemaTarServicios = ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(dtoFiltro.getInstitucion());
		
		Log4JManager.info("++++++++Filtros Consulta Ordenes Ambulatorias++++++" +
				"\nEstado: "+ConstantesBD.codigoEstadoOrdenAmbulatoriaAnulada+
				"\nFecha Inicio: "+dtoFiltro.getFechaInicio()+
				"\nFecha Fin: "+dtoFiltro.getFechaFin()+
				"\nConvenio: "+dtoFiltro.getConvenio()+
				"\nContrato: "+dtoFiltro.getContrato()+
				"\nPrioridad: 1"+
				"\nTipo Tarifario Servicios: "+esquemaTarServicios
				);
		
		//FILTROS
		if(dtoFiltro.getFechaInicio()!=null&& dtoFiltro.getFechaFin()!=null){
			//hermorhu - MT6601
			//Se trae las Ordenes Ambulatorias y las Anulaciones para ese rango de fechas
			Disjunction disjunctionFechas = Restrictions.disjunction();  
			disjunctionFechas.add( Restrictions.between("ordenAmbulatoria.fecha", dtoFiltro.getFechaInicio(), dtoFiltro.getFechaFin()));  
			disjunctionFechas.add(Restrictions.between("anulacionOrden.fecha", dtoFiltro.getFechaInicio(), dtoFiltro.getFechaFin()));
			criteria.add(disjunctionFechas);
		}
		
		if(dtoFiltro.getConvenio()!=null){
			criteria.add(Restrictions.eq("convenio.codigo",dtoFiltro.getConvenio().intValue()));
		}
		
		criteria.add(Restrictions.eq("subcuenta.nroPrioridad",1));
		
		if(dtoFiltro.getContrato()!=null){
			criteria.add(Restrictions.eq("contrato.codigo",dtoFiltro.getContrato().intValue()));
		}
		
		Disjunction disjunction = Restrictions.disjunction();  
		disjunction.add( Property.forName("referenciasServicios.id.tipoTarifario").isNull());  
		disjunction.add( Property.forName("referenciasServicios.id.tipoTarifario").eq(Utilidades.convertirAEntero(esquemaTarServicios)));
		criteria.add(disjunction);
		
		//RETORNA VALORES PARA SERVICIOS
		projection.add(Projections.property("servicio.codigo"),"codigoServicio");
		projection.add(Projections.property("referenciasServicios.descripcion"),"nombreServicio");
		projection.add(Projections.property("nivelAtencionServicio.consecutivo"),"nivelAtencionServicio");
		projection.add(Projections.property("detalleOrdenServicio.cantidad"),"cantidadServiciosOrdenes");
		projection.add(Projections.property("grupoServicio.codigo"),"codigoGrupoServicio");
		
		//RETORNA VALORES PARA ARTICULOS
		projection.add(Projections.property("articulo.codigo"),"codigoArticulo");
		projection.add(Projections.property("nivelAtencionArticulo.consecutivo"),"nivelAtencionArticulo");
		projection.add(Projections.property("detalleOrdenArticulo.cantidad"),"cantidadArticulosOrdenes");
		projection.add(Projections.property("tarifaArticulo.valorTarifa"),"tarifa");
		projection.add(Projections.property("tarifaArticulo.fechaVigencia"),"fechaVigenciaTarifaArticulo");
		projection.add(Projections.property("articulo.subgrupo"),"subgrupoInventario");
		projection.add(Projections.property("articulo.descripcion"),"nombreArticulo");
		projection.add(Projections.property("esquemaTarifarioArticulo.codigo"),"esquemaTarifarioArticulo");
		
		projection.add(Projections.property("convenio.codigo"),"convenio");
		projection.add(Projections.property("contrato.codigo"),"contrato");
		projection.add(Projections.property("ordenAmbulatoria.fecha"),"fecha");
		projection.add(Projections.property("anulacionOrden.fecha"),"fechaAnulacion");
		projection.add(Projections.property("ordenAmbulatoria.codigo"),"codigoOrden");
		
		criteria.setProjection(Projections.distinct(projection));
		
		criteria.setResultTransformer( Transformers.aliasToBean(DtoResultadoConsultaProcesosCierre.class));
		ArrayList<DtoResultadoConsultaProcesosCierre> listadoOrdenesAmbulatorias=
			(ArrayList<DtoResultadoConsultaProcesosCierre>)criteria.list();
	
		return listadoOrdenesAmbulatorias;
		
	}
	
	
	/**
	 * Retorna las ordenes ambulatorias por cuenta o rango.
	 * Hace un filtro por los parametros recibidos de DtoSolicitud.
	 * Si no se le envia cuenta carga todas las ordenes ambulatorias por rango de las fechas
	 * 
	 * @param parametros
	 * @return ArrayList<DtoSolicitud>
	 * @autor Cristhian Murillo.
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<DtoSolicitud> obtenerOrdenesAmbulatoriasPorCuentaORango(DtoSolicitud parametros,String estadoConsulta)
	{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(OrdenesAmbulatorias.class,"ordenesAmbulatorias");
	
		criteria.createAlias("ordenesAmbulatorias.cuentas"						, "cuentas"						,Criteria.LEFT_JOIN);
		criteria.createAlias("ordenesAmbulatorias.pacientes"					, "pacientes"					,Criteria.LEFT_JOIN);
		criteria.createAlias("cuentas.viasIngreso"								, "viasIngreso"					,Criteria.LEFT_JOIN);
		criteria.createAlias("cuentas.tiposPaciente"							, "tiposPaciente"				,Criteria.LEFT_JOIN);
		
		criteria.createAlias("cuentas.centrosCosto"								, "centrosCosto"				,Criteria.LEFT_JOIN);
		criteria.createAlias("centrosCosto.centroAtencion"						, "centroAtencion"				,Criteria.LEFT_JOIN);
		
		criteria.createAlias("ordenesAmbulatorias.estadosOrdAmbulatorias"		, "estadosOrdAmbulatorias");
		
		criteria.createAlias("pacientes.personas"								, "personas"					,Criteria.LEFT_JOIN);
		criteria.createAlias("personas.tiposIdentificacion"						, "tiposIdentificacion"			,Criteria.LEFT_JOIN);
		criteria.createAlias("pacientes.centroAtencionByCentroAtencionPyp"		, "centroAtencionPyp"			,Criteria.LEFT_JOIN);
		
		//criteria.createAlias("solicitudes.centrosCostoByCentroCostoSolicitante"	, "centrosCostoByCentroCostoSolicitante");
		//criteria.createAlias("solicitudes.centrosCostoByCentroCostoSolicitado"	, "centrosCostoByCentroCostoSolicitado");
		criteria.createAlias("ordenesAmbulatorias.autoEntsubOrdenambulas"			, "autoEntsubOrdenambulas"		,Criteria.LEFT_JOIN); 
		criteria.createAlias("autoEntsubOrdenambulas.autorizacionesEntidadesSub"	, "autorizacionesEntidadesSub"	,Criteria.LEFT_JOIN); 
		
		criteria.createAlias("ordenesAmbulatorias.ordenesAmbulatoriasPosponers"	, "ordenesAmbulatoriasPosponers",Criteria.LEFT_JOIN);
		
		criteria.createAlias("ordenesAmbulatorias.ingresos"						, "ingresos"					,Criteria.LEFT_JOIN);
		criteria.createAlias("ingresos.subCuentases"							, "subCuentas"					,Criteria.LEFT_JOIN);
		criteria.createAlias("subCuentas.convenios"								, "convenios"					,Criteria.INNER_JOIN);
		criteria.createAlias("convenios.contratoses"							, "contratoses"					,Criteria.INNER_JOIN);
		criteria.createAlias("convenios.tiposContrato"							, "tiposContrato");
		
		criteria.createAlias("subCuentas.estratosSociales"						, "estratosSociales"			,Criteria.LEFT_JOIN);
		criteria.createAlias("subCuentas.tiposAfiliado"							, "tiposAfiliado"				,Criteria.LEFT_JOIN);
		criteria.createAlias("subCuentas.naturalezaPacientes"					, "naturalezaPacientes"			,Criteria.LEFT_JOIN);
		
		
		if(parametros.getCodigoCuenta() != null){
			criteria.add(Restrictions.eq("cuentas.id", parametros.getCodigoCuenta()));
		}
		
		if(parametros.getCodPaciente() != null){
			criteria.add(Restrictions.eq("pacientes.codigoPaciente", parametros.getCodPaciente()));
		}
		
		if( (parametros.getFechaSolicitud() != null) && (parametros.getFechaFinalSolicitud() != null) ){
			criteria.add(Restrictions.between("ordenesAmbulatorias.fecha", parametros.getFechaSolicitud(), parametros.getFechaFinalSolicitud()));
		}
		
		
		if(parametros.getEstadoOrden() != null){
			criteria.add(Restrictions.eq("estadosOrdAmbulatorias.codigo", parametros.getEstadoOrden().byteValue()));
		}
		
		if(!estadoConsulta.equals(ConstantesIntegridadDominio.acronimoAutorizado)){
		// estado = ANU OR estado IS NULL(cuando no tiene autorizacion)
		Disjunction disjunctionOR = Restrictions.disjunction();  
			disjunctionOR.add( Property.forName("autorizacionesEntidadesSub.estado").isNull());  
			disjunctionOR.add( Property.forName("autorizacionesEntidadesSub.estado").eq(estadoConsulta));
		criteria.add(disjunctionOR);
		}else{
			Disjunction disjunctionOR = Restrictions.disjunction();  
			disjunctionOR.add( Property.forName("autorizacionesEntidadesSub.estado").isNotNull());  
			disjunctionOR.add( Property.forName("autorizacionesEntidadesSub.estado").eq(estadoConsulta));
		criteria.add(disjunctionOR);
		}
		//----------------------------------------------------------------------------

		ProjectionList projectionList = Projections.projectionList();
			// --> Datos Ordenes Ambulatorias
				projectionList.add(Projections.property("ordenesAmbulatorias.codigo")				,"numeroSolicitudLong"); //Long to int
				projectionList.add(Projections.property("ordenesAmbulatorias.consecutivoOrden")		,"consecutivoOrdenesMedicasStr");
				projectionList.add(Projections.property("ordenesAmbulatorias.fecha")				,"fechaSolicitud");
				projectionList.add(Projections.property("ordenesAmbulatorias.hora")					,"horaSolicitud");
				projectionList.add(Projections.property("ordenesAmbulatorias.urgente")				,"urgente");
				projectionList.add(Projections.property("ordenesAmbulatoriasPosponers.codigoPk")	,"codigoPosponer");
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
			// --> Datos Centros Costo de la Orden Ambulatoria	
				//projectionList.add(Projections.property("centrosCostoByCentroCostoSolicitante.codigo")	,"codigoCentroCostoSolicitante");
				//projectionList.add(Projections.property("centrosCostoByCentroCostoSolicitante.nombre")	,"nombreCentroCostoSolicitante");
				//projectionList.add(Projections.property("centrosCostoByCentroCostoSolicitado.codigo")		,"codigoCentroCostoSolicitado");
				//projectionList.add(Projections.property("centrosCostoByCentroCostoSolicitado.nombre")		,"nombreCentroCostoSolicitado");
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
				projectionList.add(Projections.property("ingresos.id")						,"idIngreso");
				
				projectionList.add(Projections.property("tiposAfiliado.acronimo")			,"acronimoTipoAfiliado");
				
				//-------------------
				projectionList.add( Projections.groupProperty("ordenesAmbulatorias.codigo"));
				projectionList.add( Projections.groupProperty("ordenesAmbulatorias.consecutivoOrden"));
				projectionList.add( Projections.groupProperty("ordenesAmbulatorias.fecha"));
				projectionList.add( Projections.groupProperty("ordenesAmbulatorias.hora"));
				projectionList.add( Projections.groupProperty("ordenesAmbulatorias.urgente"));
				projectionList.add( Projections.groupProperty("ordenesAmbulatoriasPosponers.codigoPk"));
				//projectionList.add( Projections.groupProperty("autorizacionesEntidadesSubs.consecutivo"));
				//projectionList.add( Projections.groupProperty("autorizacionesEntidadesSubs.estado"));
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
				//projectionList.add( Projections.groupProperty("centrosCostoByCentroCostoSolicitante.codigo"));
				//projectionList.add( Projections.groupProperty("centrosCostoByCentroCostoSolicitante.nombre"));
				//projectionList.add( Projections.groupProperty("centrosCostoByCentroCostoSolicitado.codigo"));
				//projectionList.add( Projections.groupProperty("centrosCostoByCentroCostoSolicitado.nombre"));
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
				projectionList.add( Projections.groupProperty("ingresos.id"));
				projectionList.add( Projections.groupProperty("tiposAfiliado.acronimo"));
				
			criteria.setProjection(projectionList);
			criteria.addOrder(Property.forName("ordenesAmbulatorias.consecutivoOrden").asc());
		
		criteria.setResultTransformer(Transformers.aliasToBean(DtoSolicitud.class));
		
		ArrayList<DtoSolicitud> listaOrdenesAmbulatorias = (ArrayList<DtoSolicitud>)criteria.list();
		
		for (DtoSolicitud ordenAmbulatoria : listaOrdenesAmbulatorias) 
		{	
			ordenAmbulatoria.setInstitucion(parametros.getInstitucion());
			ordenAmbulatoria = obtenerDetalleArticulosOrdenAmbulatoria(ordenAmbulatoria);
			ordenAmbulatoria = obtenerDetalleServiciosOrdenAmbulatoria(ordenAmbulatoria);
			ordenAmbulatoria.setTipoOrden(ConstantesIntegridadDominio.acronimoTipoOrdenambulatoria);
		}
		 
		return listaOrdenesAmbulatorias;
	}
	
	
	/**
	 * Retorna los detalles de la Orden ambulatoria (Insumos/Medicamentos)
	 * 
	 * @param ordenAmbulatoria
	 * @return DtoSolicitud
	 * @autor Cristhian Murillo.
	*/
	@SuppressWarnings("unchecked")
	private DtoSolicitud obtenerDetalleArticulosOrdenAmbulatoria(DtoSolicitud ordenAmbulatoria)
	{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(OrdenesAmbulatorias.class,"ordenesAmbulatorias");
		
		criteria.createAlias("ordenesAmbulatorias.detOrdenAmbArticulos"		, "detOrdenAmbArticulos"	, Criteria.INNER_JOIN);
		criteria.createAlias("detOrdenAmbArticulos.ordenesAmbulatorias"		, "ordenesAmbulatoriasDet"	, Criteria.LEFT_JOIN);
		criteria.createAlias("detOrdenAmbArticulos.articulo"				, "articulo"				, Criteria.LEFT_JOIN); 
		criteria.createAlias("detOrdenAmbArticulos.unidosisXArticulo"		, "unidosisXArticulo"		, Criteria.LEFT_JOIN);
		criteria.createAlias("unidosisXArticulo.unidadMedida"				, "unidadMedida"			, Criteria.LEFT_JOIN);
		criteria.createAlias("articulo.naturalezaArticulo"					, "naturalezaArticulo"		, Criteria.LEFT_JOIN);
		criteria.createAlias("ordenesAmbulatorias.diagnosticos"				, "diagnosticos"			, Criteria.LEFT_JOIN);
		
		criteria.createAlias("detOrdenAmbArticulos.tiposFrecuencia"			, "tipoFrecuencia"			, Criteria.LEFT_JOIN);
		
		criteria.createAlias("detOrdenAmbArticulos.viasAdministracion"		, "viasAdministracion"		, Criteria.LEFT_JOIN); 
		criteria.createAlias("articulo.formaFarmaceutica"					, "formaFarmaceutica"		, Criteria.LEFT_JOIN);
		
		
		criteria.add(Restrictions.eq("ordenesAmbulatoriasDet.codigo"		, new Long(ordenAmbulatoria.getNumeroSolicitud()+"")));
		
		ProjectionList projectionList = Projections.projectionList();
			//--Medicamento
			projectionList.add(Projections.property("articulo.codigo")							, "codigoArticulo");
			projectionList.add(Projections.property("articulo.descripcion")						, "descripcionArticulo");
			projectionList.add(Projections.property("articulo.concentracion")					, "concentracionArticulo");
			projectionList.add(Projections.property("formaFarmaceutica.nombre")					, "formaFarmaceuticaArticulo");
			projectionList.add(Projections.property("unidadMedida.nombre")						, "unidadMedidaArticulo");
			projectionList.add(Projections.property("unidadMedida.acronimo")					, "acronimoUnidadMedidaArticulo");
			projectionList.add(Projections.property("unidosisXArticulo.codigo")					, "dosisXArticuloID");			
			projectionList.add(Projections.property("naturalezaArticulo.esMedicamento")			, "esMedicamento");
			projectionList.add(Projections.property("naturalezaArticulo.nombre")				, "naturalezaArticulo"); 
			
			//- Formulación 	
			projectionList.add(Projections.property("detOrdenAmbArticulos.dosis")				, "dosisFormulacion");
			projectionList.add(Projections.property("detOrdenAmbArticulos.frecuencia")			, "frecuenciaFormulacionLong"); 	// Long to Int
			projectionList.add(Projections.property("viasAdministracion.nombre")				, "viaFormulacion"); 
			projectionList.add(Projections.property("detOrdenAmbArticulos.duracionTratamiento")	, "diasTratamientoFormulacionInt"); // Int to Long
			projectionList.add(Projections.property("detOrdenAmbArticulos.cantidad")			, "totalUnidadesFormulacionLong");  // Long to Int
			projectionList.add(Projections.property("tipoFrecuencia.nombre")					, "tipoFrecuenciaFormulacion");
			//--Orden
			projectionList.add(Projections.property("ordenesAmbulatorias.consecutivoOrden")		, "consecutivoOrdenMedStr"); 
			projectionList.add(Projections.property("ordenesAmbulatorias.codigo")				, "numeroOrdenLong");
			projectionList.add(Projections.property("ordenesAmbulatorias.fecha")				, "fechaOrden");
			projectionList.add(Projections.property("ordenesAmbulatorias.hora")					, "horaOrden");
			//--Diagnostico
			projectionList.add(Projections.property("diagnosticos.id.acronimo")					, "acronimoDx");
			projectionList.add(Projections.property("diagnosticos.id.tipoCie")					, "tipoCieDx");
			projectionList.add(Projections.property("diagnosticos.nombre")						, "diagnostico");
			
		criteria.setProjection(projectionList);
		
		criteria.setResultTransformer(Transformers.aliasToBean(DtoArticulosAutorizaciones.class));
		
		ArrayList<DtoArticulosAutorizaciones> listaArticulos = (ArrayList<DtoArticulosAutorizaciones>)criteria.list();
		
		ordenAmbulatoria.setListaArticulos(listaArticulos);
		
		return ordenAmbulatoria;
	}
	
	
	/**
	 * Retorna los detalles de la orden ambulatoria (Servicios).
	 * La cantidad autorizada de servicios siempre es = 1
	 * 
	 * @param ordenAmbulatoria
	 * @return DtoSolicitud
	 * @autor Cristhian Murillo.
	 */
	@SuppressWarnings("unchecked")
	private DtoSolicitud obtenerDetalleServiciosOrdenAmbulatoria(DtoSolicitud ordenAmbulatoria)
	{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(OrdenesAmbulatorias.class,"ordenesAmbulatorias");
		
		criteria.createAlias("ordenesAmbulatorias.detOrdenAmbServicio"		, "detOrdenAmbServicio"	, Criteria.INNER_JOIN);
		criteria.createAlias("detOrdenAmbServicio.servicios"				, "servicios"			, Criteria.INNER_JOIN);
		criteria.createAlias("ordenesAmbulatorias.diagnosticos"				, "diagnosticos"		, Criteria.LEFT_JOIN);
		criteria.createAlias("servicios.gruposServicios"					, "gruposServicios"		, Criteria.LEFT_JOIN);
			
		criteria.add(Restrictions.eq("ordenesAmbulatorias.codigo"			, new Long(ordenAmbulatoria.getNumeroSolicitud()+"")));
		criteria.add(Restrictions.isNotNull("servicios.codigo"));
		
		// Referencia servicios. Se trae el nombre del servicio dependiendo del tipo de esquema tarifario
		Integer tipoTarifario = Integer.parseInt(ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(ordenAmbulatoria.getInstitucion()));
		criteria.createAlias("servicios.referenciasServicios"					, "referenciasServicios"	, Criteria.INNER_JOIN);
		criteria.add(Restrictions.eq("referenciasServicios.id.tipoTarifario"	, tipoTarifario));
		// ----------------------------------------------------------------------------------------------
		
		
		ProjectionList projectionList = Projections.projectionList();
			//--Servicio
			projectionList.add(Projections.property("servicios.codigo")						, "codigoServicio"); 
			projectionList.add(Projections.property("referenciasServicios.codigoPropietario"), "codigoPropietario");
			projectionList.add(Projections.property("referenciasServicios.descripcion")		, "descripcionServicio");
			projectionList.add(Projections.property("ordenesAmbulatorias.consecutivoOrden")	, "consecutivoOrdenMedStr"); 
			projectionList.add(Projections.property("ordenesAmbulatorias.codigo")			, "numeroOrdenLong");
			
			projectionList.add(Projections.property("ordenesAmbulatorias.fecha")			, "fechaOrden");
			projectionList.add(Projections.property("ordenesAmbulatorias.hora")				, "horaOrden");
			//--Diagnostico
			projectionList.add(Projections.property("diagnosticos.id.acronimo")				, "acronimoDx");
			projectionList.add(Projections.property("diagnosticos.id.tipoCie")				, "tipoCieDx");
			projectionList.add(Projections.property("diagnosticos.nombre")					, "diagnostico");
			//--Grupo Servicios
			projectionList.add(Projections.property("gruposServicios.numDiasUrgente")		, "numDiasUrgente");
			projectionList.add(Projections.property("gruposServicios.acroDiasUrgente")		, "acroDiasUrgente");
			projectionList.add(Projections.property("gruposServicios.numDiasNormal")		, "numDiasNormal");
			projectionList.add(Projections.property("gruposServicios.acroDiasNormal")		, "acroDiasNormal");
			
		criteria.setProjection(projectionList);
		 
		criteria.setResultTransformer(Transformers.aliasToBean(DtoServiciosAutorizaciones.class));
		
		ArrayList<DtoServiciosAutorizaciones> listaServicios = (ArrayList<DtoServiciosAutorizaciones>)criteria.list();
		
		// solo se solicita y se autoriza un servicio
		for (DtoServiciosAutorizaciones dtoServiciosAutorizaciones : listaServicios) 
		{
			dtoServiciosAutorizaciones.setCantidadAutorizadaServicio(1);
			dtoServiciosAutorizaciones.setCantidadSolicitada(1);
		}
		
		ordenAmbulatoria.setListaServicios(listaServicios);
		
		return ordenAmbulatoria;
	}
	
	
	/**
	 * Método que se encarga de consultar si la Orden Ambulatoria tiene asociada una autorización de 
	 * Capitación Subcontratada y de Autorizacion Entidad Sub mediante el consecutivoAutorizacion si es diferente de NULL.
	 * RQF 02-0025 Autorizaciones Capitación
	 * 
	 * @author Camilo Gómez
	 * @param DtoAutorizacionCapitacionOrdenAmbulatoria dto
	 * @return DtoAutorizacionCapitacionOrdenAmbulatoria dtoOrdenes
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<DtoAutorizacionCapitacionOrdenAmbulatoria> existeAutorizacionCapitaOrdenAmbulatoria(DtoAutorizacionCapitacionOrdenAmbulatoria dto)
	{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(OrdenesAmbulatorias.class,"ordenesAmbulatorias");
		
		//Autorizacion asociada a la orden ambulatoria
		criteria.createAlias("ordenesAmbulatorias.autoEntsubOrdenambulas"					, "autoEntsubOrdenambula"		);
		criteria.createAlias("autoEntsubOrdenambula.autorizacionesEntidadesSub"				, "autorizacionesEntidadesSub"	);
		criteria.createAlias("autorizacionesEntidadesSub.autorizacionesCapitacionSubs"		, "autorizacionesCapitacionSub"	);
		criteria.createAlias("ordenesAmbulatorias.ingresos"									, "ingreso"		);
		criteria.createAlias("ingreso.subCuentases"											, "subcuenta"	);
		criteria.createAlias("subcuenta.convenios"											, "convenio"	);	
		criteria.createAlias("autorizacionesEntidadesSub.autorizacionesEntSubArticus"		, "autorizacionesEntSubArticu"	,Criteria.LEFT_JOIN);
		//Centro atención de la autorizacion
		criteria.createAlias("autorizacionesEntidadesSub.autorizacionesEntSubMontoses"		, "autorizacionesEntSubMontos"	,Criteria.LEFT_JOIN);
		//criteria.createAlias("autorizacionesEntSubMontos.centrosCosto"						, "centrosCostoAutoriz"			,Criteria.LEFT_JOIN);
		criteria.createAlias("autorizacionesEntidadesSub.autoCapiXCentroCostos"				,"autoCapiXCentroCostos", Criteria.LEFT_JOIN);
		criteria.createAlias("autoCapiXCentroCostos.centrosCosto"							,"centrosCostoAutoriz", Criteria.LEFT_JOIN);
		
		
		criteria.createAlias("centrosCostoAutoriz.centroAtencion"							, "centroAtencionAutoriz"		,Criteria.LEFT_JOIN);
		//Centro Atención del paciente
		criteria.createAlias("ordenesAmbulatorias.pacientes"								, "paciente"				,Criteria.LEFT_JOIN);
		criteria.createAlias("paciente.cuentases"											, "cuenta"					,Criteria.LEFT_JOIN);
		criteria.createAlias("cuenta.centrosCosto"											, "centrosCostoPaciente"	,Criteria.LEFT_JOIN);
		criteria.createAlias("centrosCostoPaciente.centroAtencion"							, "centroAtencionPaciente"	,Criteria.LEFT_JOIN);
		
		//Obtener codigos articulos-servicios y autorizacion asociada
		criteria.createAlias("ordenesAmbulatorias.detOrdenAmbServicio"				,"detOrdenAmbServicio"	,Criteria.LEFT_JOIN);
		criteria.createAlias("detOrdenAmbServicio.servicios"						,"servicio"				,Criteria.LEFT_JOIN);
		criteria.createAlias("ordenesAmbulatorias.detOrdenAmbArticulos"				,"detOrdenAmbArticulo"	,Criteria.LEFT_JOIN);
		criteria.createAlias("detOrdenAmbArticulo.articulo"							,"articulo"				,Criteria.LEFT_JOIN);
		
		
		Log4JManager.info("--------------Filtro de Busqueda de la orden Ambulatoria asociada a una Autorizacion de Capitación--------------\n"+
				"Numero de la Orden Ambulatoria	: "+dto.getDtoOrdenesAmbulatorias().getNumeroOrden()+"\n"+
				"Estado de la autorizacion		: "+dto.getEstadoAutorizacion());
		
			
		criteria.add(Restrictions.eq("ordenesAmbulatorias.codigo"	, Utilidades.convertirALong(dto.getDtoOrdenesAmbulatorias().getNumeroOrden())));
		
		if(dto.getEstadoAutorizacion()!=null)
			criteria.add(Restrictions.eq("autorizacionesEntidadesSub.estado", dto.getEstadoAutorizacion()));
		
		ProjectionList projectionList = Projections.projectionList();
			projectionList.add(Projections.property("convenio.codigo")								,"convenio");
			projectionList.add(Projections.property("autorizacionesEntidadesSub.estado")			,"estadoAutorizacion");
			projectionList.add(Projections.property("autorizacionesEntidadesSub.fechaAutorizacion")	,"fechaAutorizacion");
			projectionList.add(Projections.property("autorizacionesEntSubArticu.estado")			,"estadoArticulo");
			projectionList.add(Projections.property("autorizacionesEntidadesSub.consecutivo")				,"consecutivoAutorEntSub");
			projectionList.add(Projections.property("autorizacionesEntidadesSub.consecutivoAutorizacion")	,"consecutivoAutorizacion");
			//Centro atención de la autorizacion			
			projectionList.add(Projections.property("centrosCostoAutoriz.codigo")					,"codigoCentrosCostoSolicitadoAutoriz");
			projectionList.add(Projections.property("centrosCostoAutoriz.nombre")					,"centrosCostoSolicitadoAutoriz");
			projectionList.add(Projections.property("centrosCostoAutoriz.tipoEntidadEjecuta")		,"tipoEntidadCentroCostoSolicitadoAutoriz");
			projectionList.add(Projections.property("centroAtencionAutoriz.consecutivo")			,"codigoCentroAtencionAutoriz");
			projectionList.add(Projections.property("centroAtencionAutoriz.descripcion")			,"centroAtencionAutoriz");
			//Centro Atención del paciente
			projectionList.add(Projections.property("centrosCostoPaciente.codigo")					,"codigoCentrosCostoSolicitadoPaciente");
			//projectionList.add(Projections.property("centrosCostoPaciente.nombre")					,"centrosCostoSolicitantePaciente");			
			projectionList.add(Projections.property("centroAtencionPaciente.consecutivo")			,"codigoCentroAtencionPaciente");
		
			projectionList.add(Projections.property("servicio.codigo")			,"codigoServicio");
			projectionList.add(Projections.property("articulo.codigo")			,"codigoArticulo");
			projectionList.add(Projections.property("articulo.descripcion")		,"nombreArticulo");
		
		criteria.setProjection(projectionList);
			
		criteria.setResultTransformer(Transformers.aliasToBean(DtoAutorizacionCapitacionOrdenAmbulatoria.class));
		ArrayList<DtoAutorizacionCapitacionOrdenAmbulatoria> dtoOrdenes= (ArrayList<DtoAutorizacionCapitacionOrdenAmbulatoria>)criteria.list();
		
		return dtoOrdenes;
	
	}
	
	
	/**
	 * Método que se encarga de consultar si la Orden Ambulatoria según los parametros enviados
	 * 
	 * @author Cristhian Murillo
	 * @param DtoOrdenesAmbulatorias
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<OrdenesAmbulatorias> buscarPorParametros(DtoOrdenesAmbulatorias dtoOrdenesAmbulatorias)
	{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(OrdenesAmbulatorias.class,"ordenesAmbulatorias");
		
		if(!UtilidadTexto.isEmpty(dtoOrdenesAmbulatorias.getNumeroOrden())){
			criteria.add(Restrictions.eq("ordenesAmbulatorias.codigo"	, Utilidades.convertirALong(dtoOrdenesAmbulatorias.getNumeroOrden())));
		}
		
		if(!UtilidadTexto.isEmpty(dtoOrdenesAmbulatorias.getConsecutivoOrden())){
			criteria.add(Restrictions.eq("ordenesAmbulatorias.consecutivoOrden"	, dtoOrdenesAmbulatorias.getConsecutivoOrden()));
		}
		
		ArrayList<OrdenesAmbulatorias> listaOrdenes = new ArrayList<OrdenesAmbulatorias>();
		listaOrdenes = (ArrayList<OrdenesAmbulatorias>)criteria.list();
		
		return listaOrdenes;
	}
	
	/**
	 * Consulta las autorizaciones 
	 * @param solicitud
	 * @param parametros
	 * @return ArrayList<DtoSolicitud> con autorizaciones autoerizadas 
	 */
	public  ArrayList<DtoSolicitud>  obtenerSolciitudesAutorizadasAmbulatorias(DtoSolicitud solicitud,Integer[] parametros){
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(OrdenesAmbulatorias.class,"ordenesAmbulatorias");
		
		
		criteria.createAlias("ordenesAmbulatorias.autoEntsubOrdenambulas"					, "autoEntsubOrdenambulas");
		criteria.createAlias("autoEntsubOrdenambulas.autorizacionesEntidadesSub"					, "autoEntSubContra");
		criteria.createAlias("autoEntSubContra.autorizacionesEntSubArticus"					, "autoEntsubArticulos");
		
		
		if(parametros != null && parametros.length>0)
		{
			criteria.add(Restrictions.in("autoEntsubArticulos.articulo.codigo",parametros));
		}
		
		criteria.add(Restrictions.eq("ordenesAmbulatorias.codigo"	, new Long(solicitud.getNumeroSolicitud())));
		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Projections.property("autoEntsubArticulos.articulo.codigo") , "numeroSolicitud");
		
		criteria.setProjection(projectionList);
		
		criteria.setResultTransformer(Transformers.aliasToBean(DtoSolicitud.class));
		
		ArrayList<DtoSolicitud> listaArticulosSolicitudes= (ArrayList<DtoSolicitud>)criteria.list();
		
		return listaArticulosSolicitudes;
	}
	
	
}