/**
 * 
 */
package com.servinte.axioma.orm.delegate.salascirujia;

import java.util.ArrayList;
import java.util.Calendar;

import org.axioma.util.log.Log4JManager;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.dto.capitacion.DtoProcesoPresupuestoCapitado;
import com.princetonsa.dto.capitacion.DtoResultadoConsultaProcesosCierre;
import com.princetonsa.dto.inventario.DtoArticulosAutorizaciones;
import com.princetonsa.dto.inventario.DtoServiciosAutorizaciones;
import com.princetonsa.dto.ordenes.DtoSolicitud;
import com.servinte.axioma.dto.capitacion.DtoAutorizacionCapitacionPeticion;
import com.servinte.axioma.orm.PeticionQx;
import com.servinte.axioma.orm.PeticionQxHome;

/**
 * Esta clase se encarga de ejecutar los métodos de negocio
 * @author Cristhian Murillo
 */
@SuppressWarnings("unchecked")
public class PeticionQxDelegate extends PeticionQxHome 
{

	/**
	 * Lista todas las peticiones
	 * 
	 * @author Cristhian Murillo
	 */
	public ArrayList<PeticionQx> listar()
	{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(PeticionQx.class, "peticionQx");
		return ( ArrayList<PeticionQx>)criteria.list();
	}
	
	
	
	/**
	 * Este método consulta las peticionesdependiendo de los parámetros enviados
	 * @param dtoFiltro
	 * @return ArrayList<DtoResultadoConsultaProcesosCierre>
	 *
	 * @autor Cristhian Murillo
	*/
	public ArrayList<DtoResultadoConsultaProcesosCierre> consultarOrdenesAmbulatorias(DtoProcesoPresupuestoCapitado dtoFiltro)
	{
		Criteria criteria= sessionFactory.getCurrentSession().createCriteria(PeticionQx.class,"peticionQx");
		
		criteria.createAlias("ordenAmbulatoria.ingresos"					, "ingreso");
		criteria.createAlias("ingreso.subCuentases"							, "subcuenta");
		criteria.createAlias("subcuenta.convenios"							, "convenio");
		criteria.createAlias("convenio.contratoses"							, "contrato");
		
		criteria.createAlias("peticionQx.peticionesServicios"				, "peticionesServicios"		, Criteria.LEFT_JOIN);
		criteria.createAlias("peticionesServicios.servicios"				, "servicio"				, Criteria.LEFT_JOIN);
		criteria.createAlias("servicio.referenciasServicios"				, "referenciasServicios"	, Criteria.LEFT_JOIN);
		criteria.createAlias("servicio.nivelAtencion"						, "nivelAtencionServicio"	, Criteria.LEFT_JOIN);
		criteria.createAlias("servicio.gruposServicios"						, "grupoServicio");
		
		/*
		 Las Peticiones Qx no traen articulos
		.createAlias(""	, "detalleOrdenArticulo"	, Criteria.LEFT_JOIN)
		.createAlias(""	, "articulo"				, Criteria.LEFT_JOIN)
		.createAlias(""	, "nivelAtencionArticulo"	, Criteria.LEFT_JOIN)
		.createAlias(""	, "tarifaArticulo"			, Criteria.LEFT_JOIN)
		.createAlias(""	, "esquemaTarifarioArticulo", Criteria.LEFT_JOIN);
		*/
		
		Log4JManager.info("___Filtros Consulta Peticiones___" +
				"\nEstado: "+ConstantesBD.codigoEstadoOrdenAmbulatoriaAnulada+
				"\nFecha Inicio: "+dtoFiltro.getFechaInicio()+
				"\nFecha Fin: "+dtoFiltro.getFechaFin()+
				"\nConvenio: "+dtoFiltro.getConvenio()+
				"\nContrato: "+dtoFiltro.getContrato()+
				"\nPrioridad: 1"
		);
		
		//FILTROS
		if(dtoFiltro.isExcluiranuladas())		{
			criteria.add(Restrictions.ne("ordenAmbulatoria.estado",Byte.valueOf(Integer.toString(ConstantesBD.codigoEstadoOrdenAmbulatoriaAnulada))));
		}
		
		if(dtoFiltro.getFechaInicio()!=null&& dtoFiltro.getFechaFin()!=null){
			criteria.add(Restrictions.between("ordenAmbulatoria.fecha",dtoFiltro.getFechaInicio(), dtoFiltro.getFechaFin()));
		}
		
		if(dtoFiltro.getConvenio()!=null){
			criteria.add(Restrictions.eq("convenio.codigo",dtoFiltro.getConvenio().intValue()));
		}
		
		criteria.add(Restrictions.eq("subcuenta.nroPrioridad",1));
		
		if(dtoFiltro.getContrato()!=null){
			criteria.add(Restrictions.eq("contrato.codigo",dtoFiltro.getContrato().intValue()));
		}
		
		
		//RETORNA VALORES PARA SERVICIOS
		ProjectionList projection = Projections.projectionList();
			projection.add(Projections.property("servicio.codigo")							,"codigoServicio");
			projection.add(Projections.property("referenciasServicios.descripcion")			,"nombreServicio");
			projection.add(Projections.property("nivelAtencionServicio.consecutivo")		,"nivelAtencionServicio");
			projection.add(Projections.property("detalleOrdenServicio.cantidad")			,"cantidadServiciosOrdenes");
			projection.add(Projections.property("grupoServicio.codigo")						,"codigoGrupoServicio");
			
			//RETORNA VALORES PARA ARTICULOS
			projection.add(Projections.property("articulo.codigo")							,"codigoArticulo");
			projection.add(Projections.property("nivelAtencionArticulo.consecutivo")		,"nivelAtencionArticulo");
			projection.add(Projections.property("detalleOrdenArticulo.cantidad")			,"cantidadArticulosOrdenes");
			projection.add(Projections.property("tarifaArticulo.valorTarifa")				,"tarifa");
			projection.add(Projections.property("articulo.subgrupo")						,"subgrupoInventario");
			projection.add(Projections.property("articulo.descripcion")						,"nombreArticulo");
			
			projection.add(Projections.property("convenio.codigo")							,"convenio");
			projection.add(Projections.property("contrato.codigo")							,"contrato");
			projection.add(Projections.property("ordenAmbulatoria.fecha")					,"fecha");
		criteria.setProjection(Projections.distinct(projection));
		
		criteria.setResultTransformer( Transformers.aliasToBean(DtoResultadoConsultaProcesosCierre.class));
		
		ArrayList<DtoResultadoConsultaProcesosCierre> listadoOrdenesAmbulatorias = (ArrayList<DtoResultadoConsultaProcesosCierre>)criteria.list();
	
		return listadoOrdenesAmbulatorias;
	}
	
	
	/**
	 * Este método consulta las peticiones de servicios dependiendo de los parámetros enviados
	 * @param dtoFiltro
	 * @return ArrayList<DtoResultadoConsultaProcesosCierre>
	 *
	 * @autor Fabián Becerra
	*/
	public ArrayList<DtoResultadoConsultaProcesosCierre> consultarPeticionesServicios(DtoProcesoPresupuestoCapitado dtoFiltro)
	{
		String consulta = "SELECT new com.princetonsa.dto.capitacion.DtoResultadoConsultaProcesosCierre(serv.codigo as codigoServicio, " +
				//MT6578 se adiciona codigoPeticion
				" petQx.codigo as codigoPeticion, refServ.descripcion as nombreServicio, nivAtenServ.consecutivo as nivelAtencionServicio, " +
				" cont.codigo as contrato, conv.codigo as convenio, petQx.fechaPeticion as fecha, 1 as cantidadServArtPet, " +
				" anuPet.fecha as fechaAnulacion) "
				+ "FROM PeticionQx petQx "
				+ "INNER JOIN petQx.ingresos ing "
				+ "INNER JOIN ing.subCuentases subc "
				+ "INNER JOIN subc.contratos cont "
				+ "INNER JOIN cont.convenios conv "
				+ "INNER JOIN petQx.peticionesServicios petServ "
				+ "INNER JOIN petServ.servicios serv "
				+ "INNER JOIN serv.referenciasServicios refServ " 
				+ "LEFT JOIN petQx.anulacionPeticionQx anuPet "
				+ "LEFT JOIN serv.nivelAtencion nivAtenServ "
				+ "INNER JOIN refServ.tarifariosOficiales tarOfi "
				+ "WHERE tarOfi.codigo = :tarifarioSer "
				+ "AND subc.nroPrioridad = 1 ";
			if(dtoFiltro.getFechaInicio()!=null && dtoFiltro.getFechaFin()!=null)
			{
				//hermorhu - MT6601
				//Se trae las Peticiones de Articulos y las Anulaciones para ese rango de fechas
				consulta += "AND (petQx.fechaPeticion BETWEEN :fechaInicio AND :fechaFin " +
								" OR anuPet.fecha BETWEEN :fechaInicio AND :fechaFin) ";
			}
			if(dtoFiltro.getConvenio()!=null)
			{
				consulta += "AND conv.codigo = :codConvenio ";
			}
			if(dtoFiltro.getContrato()!=null)
			{
				consulta += "AND cont.codigo = :codContrato ";
			}
				;
		Query query = sessionFactory.getCurrentSession().createQuery(consulta);
		
		String esquemaTarServicios = ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(dtoFiltro.getInstitucion());
		query.setParameter("tarifarioSer", Utilidades.convertirAEntero(esquemaTarServicios), StandardBasicTypes.INTEGER);
		if(dtoFiltro.getFechaInicio()!=null && dtoFiltro.getFechaFin()!=null)
		{
			query.setParameter("fechaInicio", dtoFiltro.getFechaInicio(), StandardBasicTypes.DATE);
			query.setParameter("fechaFin", dtoFiltro.getFechaFin(), StandardBasicTypes.DATE);
		}
		if(dtoFiltro.getConvenio()!=null)
			query.setParameter("codConvenio", dtoFiltro.getConvenio(), StandardBasicTypes.INTEGER);
		if(dtoFiltro.getContrato()!=null)
			query.setParameter("codContrato", dtoFiltro.getContrato(), StandardBasicTypes.INTEGER);
		ArrayList<DtoResultadoConsultaProcesosCierre> listaPeticiones = (ArrayList<DtoResultadoConsultaProcesosCierre>) query.list();
		
		return listaPeticiones;
	}
	 
	/**
	 * Este método consulta las peticiones de Articulos dependiendo de los parámetros enviados
	 * @param dtoFiltro
	 * @return ArrayList<DtoResultadoConsultaProcesosCierre>
	 *
	 * @autor Fabián Becerra
	*/
	public ArrayList<DtoResultadoConsultaProcesosCierre> consultarPeticionesArticulos(DtoProcesoPresupuestoCapitado dtoFiltro)
	{
		String consulta = "SELECT new com.princetonsa.dto.capitacion.DtoResultadoConsultaProcesosCierre(art.codigo as codigoArticulo, " +
				" petQx.codigo as codigoPeticion, art.descripcion as nombreArticulo, nivAtenArt.consecutivo as nivelAtencionArticulo, " +
				" cont.codigo as contrato, conv.codigo as convenio, petQx.fechaPeticion as fecha, petArt.cantidad as cantidadServArtPet, " +
				" tarInv.valorTarifa as tarifa, esqTar.codigo as esquemaTarifarioArticulo, tarInv.fechaVigencia as fechaVigenciaTarifaArticulo, " +
				//MT6578 se adiciona cod de peticion
				" anuPet.fecha as fechaAnulacion) "
				+ "FROM PeticionQx petQx "
				+ "INNER JOIN petQx.ingresos ing "
				+ "INNER JOIN ing.subCuentases subc "
				+ "INNER JOIN subc.contratos cont "
				+ "INNER JOIN cont.convenios conv "
				+ "INNER JOIN petQx.articulosPeticionQxes petArt "
				+ "INNER JOIN petArt.articulo art "
				+ "LEFT JOIN petQx.anulacionPeticionQx anuPet "
				+ "LEFT JOIN art.nivelAtencion nivAtenArt "
				+ "LEFT JOIN art.tarifasInventarios tarInv "
				+ "LEFT JOIN tarInv.esquemasTarifarios AS esqTar "
				+ "WHERE subc.nroPrioridad = 1 " 
				+ "";
			
			if(dtoFiltro.getFechaInicio()!=null && dtoFiltro.getFechaFin()!=null)
			{
				//hermorhu - MT6601
				//Se trae las Peticiones de Articulos y las Anulaciones para ese rango de fechas
				consulta += "AND (petQx.fechaPeticion BETWEEN :fechaInicio AND :fechaFin " +
								" OR anuPet.fecha BETWEEN :fechaInicio AND :fechaFin) ";
			}
			if(dtoFiltro.getConvenio()!=null)
			{
				consulta += "AND conv.codigo = :codConvenio ";
			}
			if(dtoFiltro.getContrato()!=null)
			{
				consulta += "AND cont.codigo = :codContrato ";
			}
				;
		Query query = sessionFactory.getCurrentSession().createQuery(consulta);
		if(dtoFiltro.getFechaInicio()!=null && dtoFiltro.getFechaFin()!=null)
		{
			query.setParameter("fechaInicio", dtoFiltro.getFechaInicio(), StandardBasicTypes.DATE);
			query.setParameter("fechaFin", dtoFiltro.getFechaFin(), StandardBasicTypes.DATE);
		}
		if(dtoFiltro.getConvenio()!=null)
			query.setParameter("codConvenio", dtoFiltro.getConvenio(), StandardBasicTypes.INTEGER);
		if(dtoFiltro.getContrato()!=null)
			query.setParameter("codContrato", dtoFiltro.getContrato(), StandardBasicTypes.INTEGER);
		ArrayList<DtoResultadoConsultaProcesosCierre> listaPeticiones = (ArrayList<DtoResultadoConsultaProcesosCierre>) query.list();
		return listaPeticiones;
	}
	 
	
	
	//---------------------------------------
	//---------------------------------------
	//---------------------------------------
	
	/**
	 * Este método consulta las peticiones dependiendo de los parámetros enviados
	 * @param dtoFiltro
	 * @return ArrayList<DtoSolicitud>
	 *
	 * @autor Cristhian Murillo
	*/
	public ArrayList<DtoSolicitud> obtenerOrdenesPeticionesPorCuentaORango(DtoSolicitud parametros)
	{
		Criteria criteria= sessionFactory.getCurrentSession().createCriteria(PeticionQx.class,"peticionQx");
		
		criteria.createAlias("peticionQx.ingresos"								, "ingresos"					,Criteria.LEFT_JOIN);
		criteria.createAlias("ingresos.subCuentases"							, "subCuentas"					,Criteria.LEFT_JOIN);
		criteria.createAlias("subCuentas.convenios"								, "convenios"					,Criteria.INNER_JOIN);
		criteria.createAlias("convenios.contratoses"							, "contratoses"					,Criteria.INNER_JOIN);
		criteria.createAlias("convenios.tiposContrato"							, "tiposContrato");
		
		criteria.createAlias("subCuentas.estratosSociales"						, "estratosSociales"			,Criteria.LEFT_JOIN);
		criteria.createAlias("subCuentas.tiposAfiliado"							, "tiposAfiliado"				,Criteria.LEFT_JOIN);
		criteria.createAlias("subCuentas.naturalezaPacientes"					, "naturalezaPacientes"			,Criteria.LEFT_JOIN);
		
		criteria.createAlias("peticionQx.pacientes"								, "pacientes"					,Criteria.LEFT_JOIN);
		criteria.createAlias("pacientes.personas"								, "personas"					,Criteria.LEFT_JOIN);
		criteria.createAlias("personas.tiposIdentificacion"						, "tiposIdentificacion"			,Criteria.LEFT_JOIN);
		criteria.createAlias("pacientes.centroAtencionByCentroAtencionPyp"		, "centroAtencionPyp"			,Criteria.LEFT_JOIN);
		
		criteria.createAlias("ingresos.cuentases"								, "cuentas"						,Criteria.LEFT_JOIN);
		criteria.createAlias("cuentas.viasIngreso"								, "viasIngreso"					,Criteria.LEFT_JOIN);
		criteria.createAlias("cuentas.tiposPaciente"							, "tiposPaciente"				,Criteria.LEFT_JOIN);
		
		criteria.createAlias("cuentas.centrosCosto"								, "centrosCosto"				,Criteria.LEFT_JOIN);
		criteria.createAlias("centrosCosto.centroAtencion"						, "centroAtencion"				,Criteria.LEFT_JOIN);
		
		criteria.createAlias("peticionQx.autoEntsubPeticioneses"				, "autoEntsubPeticioneses"		,Criteria.LEFT_JOIN); 
		criteria.createAlias("autoEntsubPeticioneses.autorizacionesEntidadesSub", "autorizacionesEntidadesSub"	,Criteria.LEFT_JOIN); 
		
		criteria.createAlias("peticionQx.peticionesPosponers"					, "peticionesPosponers"			,Criteria.LEFT_JOIN);
		
		if(parametros.getCodigoCuenta() != null){
			criteria.add(Restrictions.eq("cuentas.id", parametros.getCodigoCuenta()));
		}
		
		if(parametros.getCodPaciente() != null){
			criteria.add(Restrictions.eq("pacientes.codigoPaciente", parametros.getCodPaciente()));
		}
		
		if( (parametros.getFechaSolicitud() != null) && (parametros.getFechaFinalSolicitud() != null) ){
			criteria.add(Restrictions.between("peticionQx.fechaPeticion", parametros.getFechaSolicitud(), parametros.getFechaFinalSolicitud()));
		}
		
		if(parametros.getEstadoOrden() != null){
			criteria.add(Restrictions.in("peticionQx.estadoPeticion", parametros.getEstadoHistoriaClinicaIgual()));
		}

		
		// estado = ANU OR estado IS NULL(cuando no tiene autorizacion)
		Disjunction disjunctionOR = Restrictions.disjunction();  
			disjunctionOR.add( Property.forName("autorizacionesEntidadesSub.estado").isNull());  
			disjunctionOR.add( Property.forName("autorizacionesEntidadesSub.estado").eq(ConstantesIntegridadDominio.acronimoEstadoAnulado));
		criteria.add(disjunctionOR);
		//----------------------------------------------------------------------------
		
		
		ProjectionList projectionList = Projections.projectionList();
			// --> Datos Petición
				projectionList.add(Projections.property("peticionQx.codigo")						,"numeroSolicitud");
				projectionList.add(Projections.property("peticionQx.codigo")						,"consecutivoOrdenesMedicas");
				projectionList.add(Projections.property("peticionQx.fechaPeticion")					,"fechaSolicitud");
				projectionList.add(Projections.property("peticionQx.horaPeticion")					,"horaSolicitud");
				projectionList.add(Projections.property("peticionQx.urgente")						,"urgente");
				projectionList.add(Projections.property("peticionesPosponers.codigoPk")				,"codigoPosponer");
				
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

				// --> Datos Cuenta Paciente/Persona	
				projectionList.add(Projections.property("cuentas.id")								,"codigoCuenta");
				projectionList.add(Projections.property("viasIngreso.codigo")						,"codViaIngreso");
				projectionList.add(Projections.property("tiposPaciente.acronimo")					,"tipoPaciente");
				
				projectionList.add(Projections.property("estratosSociales.codigo")					,"codigoEstratoSocial");
				projectionList.add(Projections.property("estratosSociales.descripcion")				,"descripcionEstratoSocial");
				projectionList.add(Projections.property("tiposAfiliado.nombre")						,"nombreTipoAfiliado");
				projectionList.add(Projections.property("subCuentas.semanasCotizacion")				,"semanasCotizacion");
				projectionList.add(Projections.property("subCuentas.tipoMontoCobro")				,"tipoMontoCobro");
				projectionList.add(Projections.property("subCuentas.porcentajeAutorizado")			,"porcentajeAutorizado");
				projectionList.add(Projections.property("subCuentas.montoAutorizado")				,"montoAutorizado");
				projectionList.add(Projections.property("subCuentas.nroPrioridad")					,"nroPrioridad");
				projectionList.add(Projections.property("naturalezaPacientes.codigo")				,"codigoNaturalezaPaciente"); 
				
				projectionList.add(Projections.property("centroAtencion.consecutivo")				,"centroAtencionCuenta");
				projectionList.add(Projections.property("ingresos.id")								,"idIngreso");
				
				projectionList.add(Projections.property("tiposAfiliado.acronimo")					,"acronimoTipoAfiliado");
				
				//-------------------
				projectionList.add( Projections.groupProperty("peticionQx.codigo"));
				projectionList.add( Projections.groupProperty("peticionQx.fechaPeticion"));
				projectionList.add( Projections.groupProperty("peticionQx.horaPeticion"));
				projectionList.add( Projections.groupProperty("peticionQx.urgente"));

				projectionList.add( Projections.groupProperty("peticionesPosponers.codigoPk"));
				
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
			criteria.addOrder(Property.forName("peticionQx.codigo").asc());
		
		criteria.setResultTransformer(Transformers.aliasToBean(DtoSolicitud.class));
		
		ArrayList<DtoSolicitud> listaPeticiones = (ArrayList<DtoSolicitud>)criteria.list();
		
		for (DtoSolicitud peticion : listaPeticiones) 
		{	
			peticion.setInstitucion(parametros.getInstitucion());
			/* Las peticiones son solo de servicios.  peticion = obtenerDetalleArticulosPeticion(peticion); */
			peticion.setListaArticulos(new ArrayList<DtoArticulosAutorizaciones>());
			peticion = obtenerDetalleServiciosPeticion(peticion);
			
			if(peticion.isUrgente() == null){
				peticion.setUrgente(false);
			}
			
			peticion.setTipoOrden(ConstantesIntegridadDominio.acronimoTipoOrdenPeticionCx);
		}
		
		return listaPeticiones;
	}
	
	/**
	 * Retorna los detalles de la peticion (Insumos/Medicamentos)
	 * 
	 * @param peticion
	 * @return DtoSolicitud
	 * @autor Cristhian Murillo.
	 */
	@SuppressWarnings("unused")
	private DtoSolicitud obtenerDetalleArticulosPeticion(DtoSolicitud peticion)
	{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(PeticionQx.class,"peticionQx");
		
		criteria.createAlias("peticionQx.articulosPeticionQxes"				, "peticionArticulo"	, Criteria.INNER_JOIN);
		criteria.createAlias("peticionArticulo.articulo"					, "articulo"			, Criteria.INNER_JOIN);
		criteria.createAlias("articulo.naturalezaArticulo"					, "naturalezaArticulo"	, Criteria.LEFT_JOIN);
		criteria.createAlias("articulo.unidadMedida"						, "unidadMedida"		, Criteria.LEFT_JOIN);
		criteria.createAlias("articulo.formaFarmaceutica"					, "formaFarmaceutica"	, Criteria.LEFT_JOIN);
		
		
		/*criteria.createAlias("detalleSolicitudeses.unidosisXArticulo"		, "unidosisXArticulo"		, Criteria.LEFT_JOIN);
		criteria.createAlias("unidosisXArticulo.unidadMedida"				, "unidadMedida"			, Criteria.LEFT_JOIN);
		
		criteria.createAlias("solicitudes.diagnosticos"						, "diagnosticos"			, Criteria.LEFT_JOIN);*/

		criteria.add(Restrictions.eq("peticionQx.codigo"	, peticion.getNumeroSolicitud()));
		
		
		ProjectionList projectionList = Projections.projectionList();
			//--Medicamento
			projectionList.add(Projections.property("articulo.codigo")				, "codigoArticulo");
			projectionList.add(Projections.property("articulo.descripcion")			, "descripcionArticulo");
			projectionList.add(Projections.property("articulo.concentracion")			, "concentracionArticulo");
			projectionList.add(Projections.property("formaFarmaceutica.nombre")		, "formaFarmaceuticaArticulo");
			
			projectionList.add(Projections.property("unidadMedida.nombre")						, "unidadMedidaArticulo");
			projectionList.add(Projections.property("unidadMedida.acronimo")					, "acronimoUnidadMedidaArticulo");
			
			/*projectionList.add(Projections.property("unidosisXArticulo.codigo")					, "dosisXArticuloID");		*/
			
			projectionList.add(Projections.property("naturalezaArticulo.esMedicamento")			, "esMedicamento");
			projectionList.add(Projections.property("naturalezaArticulo.nombre")				, "naturalezaArticulo"); 
			
			//- Formulación 	
			/*projectionList.add(Projections.property("detalleSolicitudeses.dosis")				, "dosisFormulacion");
			projectionList.add(Projections.property("detalleSolicitudeses.frecuencia")			, "frecuenciaFormulacion");
			projectionList.add(Projections.property("detalleSolicitudeses.via")					, "viaFormulacion");
			projectionList.add(Projections.property("detalleSolicitudeses.diasTratamiento")		, "diasTratamientoFormulacion");*/
			
			
			projectionList.add(Projections.property("peticionArticulo.cantidad")				, "totalUnidadesFormulacion");
			//--Orden
			projectionList.add(Projections.property("peticionQx.codigo")						, "numeroOrden");
			projectionList.add(Projections.property("peticionQx.fechaPeticion")					, "fechaOrden");
			projectionList.add(Projections.property("peticionQx.horaPeticion")					, "horaOrden");
			
			
			//--Diagnostico
			/*projectionList.add(Projections.property("diagnosticos.id.acronimo")					, "acronimoDx");
			projectionList.add(Projections.property("diagnosticos.id.tipoCie")					, "tipoCieDx");
			projectionList.add(Projections.property("diagnosticos.nombre")						, "diagnostico");*/
			
		criteria.setProjection(projectionList);
		
		criteria.setResultTransformer(Transformers.aliasToBean(DtoArticulosAutorizaciones.class));
		
		
		ArrayList<DtoArticulosAutorizaciones> listaArticulos = (ArrayList<DtoArticulosAutorizaciones>)criteria.list();
		
		peticion.setListaArticulos(listaArticulos);
		
		return peticion;
	}
	
	/**
	 * Retorna los detalles de la peticion (Servicios).
	 * La cantidad autorizada de servicios siempre es = 1
	 * 
	 * @param peticion
	 * @return DtoSolicitud
	 * @autor Cristhian Murillo.
	 */
	private DtoSolicitud obtenerDetalleServiciosPeticion(DtoSolicitud peticion)
	{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(PeticionQx.class,"peticionQx");
		
		criteria.createAlias("peticionQx.peticionesServicios"				, "peticionServicio"	    , Criteria.INNER_JOIN);
		criteria.createAlias("peticionServicio.servicios"					, "servicio"				, Criteria.INNER_JOIN);
		criteria.createAlias("servicio.gruposServicios"						, "gruposServicios"			, Criteria.LEFT_JOIN);
		criteria.createAlias("peticionQx.diagnosticos"						, "diagnosticos"			, Criteria.LEFT_JOIN);
			
		criteria.add(Restrictions.eq("peticionQx.codigo"	, peticion.getNumeroSolicitud()));
		criteria.add(Restrictions.isNotNull("servicio.codigo"));
		
		// Referencia servicios. Se trae el nombre del servicio dependiendo del tipo de esquema tarifario
		Integer tipoTarifario = Integer.parseInt(ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(peticion.getInstitucion()));
		criteria.createAlias("servicio.referenciasServicios"					, "referenciasServicios"	, Criteria.INNER_JOIN);
		criteria.add(Restrictions.eq("referenciasServicios.id.tipoTarifario"	, tipoTarifario));
		// ----------------------------------------------------------------------------------------------
		
		
		ProjectionList projectionList = Projections.projectionList();
			//--Servicio
			projectionList.add(Projections.property("servicio.codigo")							, "codigoServicio");
			projectionList.add(Projections.property("referenciasServicios.codigoPropietario")	, "codigoPropietario");
			projectionList.add(Projections.property("referenciasServicios.descripcion")			, "descripcionServicio");
			
			projectionList.add(Projections.property("peticionQx.codigo")						, "numeroOrden");
			projectionList.add(Projections.property("peticionQx.codigo")						, "consecutivoOrdenMed");
			projectionList.add(Projections.property("peticionQx.fechaPeticion")					, "fechaOrden");
			projectionList.add(Projections.property("peticionQx.horaPeticion")					, "horaOrden");
			
			//--Diagnostico
			projectionList.add(Projections.property("diagnosticos.id.acronimo")					, "acronimoDx");
			projectionList.add(Projections.property("diagnosticos.id.tipoCie")					, "tipoCieDx");
			projectionList.add(Projections.property("diagnosticos.nombre")						, "diagnostico");
			
			//--Grupo Servicios
			projectionList.add(Projections.property("gruposServicios.numDiasUrgente")			, "numDiasUrgente");
			projectionList.add(Projections.property("gruposServicios.acroDiasUrgente")			, "acroDiasUrgente");
			projectionList.add(Projections.property("gruposServicios.numDiasNormal")			, "numDiasNormal");
			projectionList.add(Projections.property("gruposServicios.acroDiasNormal")			, "acroDiasNormal");
			
			projectionList.add(Projections.property("peticionServicio.numeroServicio")			, "numeroServicio");
			
		criteria.setProjection(projectionList);
		 
		
		criteria.setResultTransformer(Transformers.aliasToBean(DtoServiciosAutorizaciones.class));
		
		ArrayList<DtoServiciosAutorizaciones> listaServicios = (ArrayList<DtoServiciosAutorizaciones>)criteria.list();
		
		
		// solo se solicita y se autoriza un servicio
		for (DtoServiciosAutorizaciones dtoServiciosAutorizaciones : listaServicios) 
		{
			dtoServiciosAutorizaciones.setCantidadAutorizadaServicio(1);
			dtoServiciosAutorizaciones.setCantidadSolicitada(1);
		}
		
		peticion.setListaServicios(listaServicios);
		
		return peticion;
	}
	
	
	
	/**
	 * Método que se encarga de consultar si la Peticion tiene asociada una autorización de 
	 * Capitación Subcontratada.
	 * RQF 02-0025 Autorizaciones Capitación
	 * 
	 * @author Camilo Gómez
	 * @param DtoAutorizacionCapitacionPeticion dto
	 * @return ArrayList<DtoAutorizacionCapitacionPeticion> dtoPeticiones
	 */
	public ArrayList<DtoAutorizacionCapitacionPeticion> existeAutorizacionCapitaPeticion(DtoAutorizacionCapitacionPeticion dto)
	{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(PeticionQx.class,"peticionQx");
		
		//Autorizacion asociada a la Peticion
		criteria.createAlias("peticionQx.autoEntsubPeticioneses"							, "autoEntsubPeticiones"		);
		criteria.createAlias("autoEntsubPeticiones.autorizacionesEntidadesSub"				, "autorizacionesEntidadesSub"	);
		criteria.createAlias("autorizacionesEntidadesSub.autorizacionesCapitacionSubs"		, "autorizacionesCapitacionSub"	);
		//criteria.createAlias("autorizacionesEntidadesSub.autorizacionesEntSubServis"		, "autorizacionesEntSubServis"	,Criteria.LEFT_JOIN);
		
		
		Log4JManager.info("*******Filtro de Busqueda de la peticion asociada a una Autorizacion de Capitación********");
		Log4JManager.info("Numero de la Peticion:	"+dto.getNumeroPeticion());	
			
		criteria.add(Restrictions.eq("peticionQx.codigo"	, dto.getNumeroPeticion()));
		
		
		ProjectionList projectionList = Projections.projectionList();
			projectionList.add(Projections.property("peticionQx.codigo")									,"numeroPeticion");
			projectionList.add(Projections.property("autorizacionesEntidadesSub.estado")			,"estadoAutorizacion");
			projectionList.add(Projections.property("autorizacionesEntidadesSub.consecutivo")				,"consecutivoAutorEntSub");
			projectionList.add(Projections.property("autorizacionesEntidadesSub.consecutivoAutorizacion")	,"consecutivoAutorizacion");
		
		criteria.setProjection(projectionList);
			
		criteria.setResultTransformer(Transformers.aliasToBean(DtoAutorizacionCapitacionPeticion.class));
		ArrayList<DtoAutorizacionCapitacionPeticion> dtoPeticiones= (ArrayList<DtoAutorizacionCapitacionPeticion>)criteria.list();
		
		return dtoPeticiones;
	}
	
	/**
	 * Metodo que devuelve las peticiones de articulos anuladas desde la orden medica (SolicitudCx)
	 * @param dtoFiltro
	 * @return ArrayList<DtoResultadoConsultaProcesosCierre>
	 * @author hermorhu
	 */
	public ArrayList<DtoResultadoConsultaProcesosCierre> consultarPeticionesArticulosAnulados(DtoProcesoPresupuestoCapitado dtoFiltro){
		
		String consulta = "SELECT new com.princetonsa.dto.capitacion.DtoResultadoConsultaProcesosCierre(art.codigo as codigoArticulo, " +
				" petQx.codigo as codigoPeticion, art.descripcion as nombreArticulo, nivAtenArt.consecutivo as nivelAtencionArticulo, " +
				" cont.codigo as contrato, conv.codigo as convenio, petArt.cantidad as cantidadServArtPet, " +
				" tarInv.valorTarifa as tarifa, esqTar.codigo as esquemaTarifarioArticulo, tarInv.fechaVigencia as fechaVigenciaTarifaArticulo, " +
				//MT6578 se adiciona cod de peticion
				" anuSolCx.fecha as fechaAnulacion) "
				+ "FROM PeticionQx petQx "
				+ "INNER JOIN petQx.solicitudesCirugias solCx "
				+ "INNER JOIN petQx.ingresos ing "
				+ "INNER JOIN ing.subCuentases subc "
				+ "INNER JOIN subc.contratos cont "
				+ "INNER JOIN cont.convenios conv "
				+ "INNER JOIN petQx.articulosPeticionQxes petArt "
				+ "INNER JOIN petArt.articulo art "
				+ "INNER JOIN solCx.anulacionSolCx anuSolCx "
				+ "LEFT JOIN art.nivelAtencion nivAtenArt "
				+ "LEFT JOIN art.tarifasInventarios tarInv "
				+ "LEFT JOIN tarInv.esquemasTarifarios AS esqTar "
				+ "WHERE subc.nroPrioridad = 1 " 
				+ "";
		
		Calendar fechaInicioAnulacion = null;
		Calendar fechaFinAnulacion = null;
		
		if(dtoFiltro.getFechaInicio() != null && dtoFiltro.getFechaFin() != null) {
			//hermorhu - MT6601
			//Se trae las Solicitudes Cx y las Anulaciones para ese rango de fechas
				
			//Agregar el rango de fechas para la anulacion de la solicitud Cx de 00:00:00 a 24:00:00
			fechaInicioAnulacion = Calendar.getInstance();
			fechaInicioAnulacion.setTime(dtoFiltro.getFechaInicio());
			fechaInicioAnulacion.add(Calendar.HOUR, 0);
			fechaInicioAnulacion.add(Calendar.MINUTE, 0);
			fechaInicioAnulacion.add(Calendar.SECOND, 0);
				
			fechaFinAnulacion = Calendar.getInstance();
			fechaFinAnulacion.setTime(dtoFiltro.getFechaFin());
			fechaFinAnulacion.add(Calendar.HOUR, 24);
			fechaFinAnulacion.add(Calendar.MINUTE, 00);
			fechaFinAnulacion.add(Calendar.SECOND, 00);
				
			//hermorhu - MT6601
			//Se trae las Peticiones de Articulos Anuladas para ese rango de fechas
			consulta += "AND (anuSolCx.fecha BETWEEN :fechaInicio AND :fechaFin ) ";
		}
			
		if(dtoFiltro.getConvenio()!=null) {
			consulta += "AND conv.codigo = :codConvenio ";
		}
			
		if(dtoFiltro.getContrato()!=null) {
				consulta += "AND cont.codigo = :codContrato ";
		}
			
		Query query = sessionFactory.getCurrentSession().createQuery(consulta);
		
		if(dtoFiltro.getFechaInicio() != null && dtoFiltro.getFechaFin() != null) {
			query.setParameter("fechaInicio", fechaInicioAnulacion.getTime(), StandardBasicTypes.DATE);
			query.setParameter("fechaFin", fechaFinAnulacion.getTime(), StandardBasicTypes.DATE);
		}
		if(dtoFiltro.getConvenio()!=null)
			query.setParameter("codConvenio", dtoFiltro.getConvenio(), StandardBasicTypes.INTEGER);
		if(dtoFiltro.getContrato()!=null)
			query.setParameter("codContrato", dtoFiltro.getContrato(), StandardBasicTypes.INTEGER);
		
		ArrayList<DtoResultadoConsultaProcesosCierre> listaPeticiones = (ArrayList<DtoResultadoConsultaProcesosCierre>) query.list();
		
		return listaPeticiones;
	}
	
	//public ArrayList<E>
}