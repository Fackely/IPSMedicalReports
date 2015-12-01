package com.servinte.axioma.orm.delegate.odontologia.contrato;


import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadFecha;
import util.UtilidadTexto;

import com.princetonsa.dto.odontologia.DTOPacientesReportePacientesEstadoPresupuesto;
import com.princetonsa.dto.odontologia.DtoFormatoImpresionContratoOdontologico;
import com.princetonsa.dto.odontologia.DtoIngresosOdontologicos;
import com.princetonsa.dto.odontologia.DtoPaqueteReport;
import com.princetonsa.dto.odontologia.DtoPresupuestosOdontologicosContratados;
import com.princetonsa.dto.odontologia.DtoProgramaReport;
import com.princetonsa.dto.odontologia.DtoReporteConsultaPacienteEstadoPresupuesto;
import com.princetonsa.dto.odontologia.DtoReportePresupuestosOdontologicosContratados;
import com.princetonsa.enu.impresion.EnumTiposSalida;
import com.servinte.axioma.orm.PresupuestoDctoOdon;
import com.servinte.axioma.orm.PresupuestoOdontologico;
import com.servinte.axioma.orm.PresupuestoOdontologicoHome;


/**
 * @author Cristhian Murillo
 *
 * Clase que contiene logica del negocio sobre el modelo 
 */
@SuppressWarnings("unchecked")
public class PresupuestoOdontologicoDelegate extends PresupuestoOdontologicoHome {


	/**
	 * Retorna un dto con la informacion utilizada para la impresion del
	 * formato de contrato odontologico
	 * 
	 * @author Cristhian Murillo
	 * @param infoPresupuesto
	 * @return
	 */
	public DtoFormatoImpresionContratoOdontologico obtenerContratoOdontologico(DtoFormatoImpresionContratoOdontologico infoPresupuesto){
		
		return (DtoFormatoImpresionContratoOdontologico) sessionFactory.getCurrentSession()
			.createCriteria(PresupuestoOdontologico.class, "presu_odonto")
			
			.createAlias("presu_odonto.presupuestoContratado"		, "presu_contra"		,Criteria.LEFT_JOIN)
			.createAlias("presu_odonto.pacientes"					, "pac"					,Criteria.LEFT_JOIN)
			.createAlias("pac.personas"								, "pac_persona"			,Criteria.LEFT_JOIN)
			.createAlias("pac_persona.tiposIdentificacion"			, "persona_tipoId"		,Criteria.LEFT_JOIN)
			.createAlias("presu_odonto.centroAtencion"				, "centro_aten"			,Criteria.LEFT_JOIN)
			.createAlias("presu_odonto.instituciones"				, "insti"				,Criteria.LEFT_JOIN)
			.createAlias("presu_contra.presuContratoOdoImps"		, "presu_cont_odo_imp"	,Criteria.LEFT_JOIN)
			
			.add(Restrictions.eq("presu_odonto.codigoPk", infoPresupuesto.getCodigoPkPresupuesto()))

			
			.setProjection(Projections.projectionList()
					
					.add(Projections.property("insti.path")					, "pathLogoInstitucion")
					.add(Projections.property("insti.ubicacionLogoReportes"), "ubicacionLogoInstitucion")
					.add(Projections.property("insti.codigo")				, "codigoInstitucion")
					.add(Projections.property("insti.razonSocial")			, "razonSocialInstitucion")
					.add(Projections.property("insti.nit")					, "nitInstitucion")
					.add(Projections.property("insti.actividadEco")			, "actividadEcoInstitucion")
					.add(Projections.property("insti.direccion")			, "direccionInstitucion")
					.add(Projections.property("insti.telefono")				, "telefonoInstitucion")
					
					.add(Projections.property("presu_odonto.codigoPk") 		, "codigoPkPresupuesto")
					.add(Projections.property("presu_odonto.consecutivo")	, "numeroConsecutivoPresupuesto")
					.add(Projections.property("presu_odonto.fechaModifica")	, "fechaContrato")
					.add(Projections.property("presu_odonto.estado")		, "estadoPresupuesto")
					.add(Projections.property("presu_contra.consecutivo")	, "numeroConsecutivoContrato")
					.add(Projections.property("centro_aten.consecutivo")	, "codCentroAtencionPresupuestoContratado")
					.add(Projections.property("centro_aten.descripcion")	, "nomCentroAtencionPresupuestoContratado")
					
					.add(Projections.property("pac_persona.primerNombre")	, "primerNombre")
					.add(Projections.property("pac_persona.segundoNombre")	, "segundoNombre")
					.add(Projections.property("pac_persona.primerApellido")	, "primerApellido")
					.add(Projections.property("pac_persona.segundoApellido"), "segundoApellido")
					.add(Projections.property("persona_tipoId.nombre")		, "tipoId")
					.add(Projections.property("pac_persona.numeroIdentificacion"), "numeroId")
					.add(Projections.property("pac_persona.direccion")		, "pacDireccion")
					.add(Projections.property("pac_persona.telefono")		, "pacTelefono")
					.add(Projections.property("presu_cont_odo_imp.clausulas"), "clausulasContrato")
					
					.add(Projections.property("presu_cont_odo_imp.piePagina"), "piePagina")
			)

			.setResultTransformer(Transformers.aliasToBean(DtoFormatoImpresionContratoOdontologico.class))
			.uniqueResult();
	}
	
	
	
	/**
	 * Este m&eacute;todo se encarga de obtener los presupuestos en estado 
	 * precontratado que no tienen una solicitud de descuento odontol&oacute;gico.
	 *
	 * @author Yennifer Guerrero
	 */
	@SuppressWarnings("rawtypes")
	public ArrayList<DtoIngresosOdontologicos> obtenerPresupuestosPrecontratados(
			List<Long> presupuestos, String conSolicituDcto){
		
		DetachedCriteria solicitudesDcto = DetachedCriteria.forClass(PresupuestoDctoOdon.class, "solicitud") 
		 .setProjection( Property.forName("presupuestoOdontologico"));
		
		
		Criteria criteria = sessionFactory.getCurrentSession()
		.createCriteria(PresupuestoOdontologico.class, "presupuesto")
		.createAlias("presupuesto.ingresos", "ingresos")
		.createAlias("presupuesto.pacientes", "pacientes")
		.createAlias("ingresos.centroAtencion", "centroAtencion")
		.createAlias("centroAtencion.ciudades", "ciudades")
		.createAlias("ciudades.paises", "paises")
		.createAlias("centroAtencion.regionesCobertura", "regionesCobertura")
		.createAlias("centroAtencion.empresasInstitucion", "empresaInstitucion")
		.createAlias("pacientes.personas", "personas")
		.createAlias("personas.tiposIdentificacion", "tipoId")
		.createAlias("centroAtencion.instituciones", "institucion");
		
		criteria.setProjection(Projections.projectionList()
				.add(Projections.property("presupuesto.codigoPk"), "codigoPkPresupuesto")
				.add( Projections.property("presupuesto.consecutivo"), "consecutivoPresupuesto")
				.add( Projections.property("presupuesto.fechaGeneracion"), "fechaGeneracionPresupuesto")
				.add( Projections.property("presupuesto.estado"), "estadoPresupuesto")
				
				.add( Projections.property("ingresos.id"), "idIngreso")
				.add( Projections.property("centroAtencion.consecutivo"), "consecutivoCentroAtencion" )
				.add( Projections.property("centroAtencion.descripcion"), "descripcionCentroAtencion" )
				.add( Projections.property("institucion.codigo"), "codigoInstitucion" )
				.add( Projections.property("empresaInstitucion.razonSocial"), "descripcionEmpresaInstitucion" )
				.add( Projections.property("paises.descripcion"),"descripcionPais")
				.add( Projections.property("ciudades.descripcion")	, "descripcionCiudad" )
				.add( Projections.property("regionesCobertura.descripcion"), "descripcionRegionCobertura" )
				
				.add( Projections.property("personas.codigo"), "codigoPaciente")
				.add( Projections.property("tipoId.acronimo"), "tipoId")
				.add( Projections.property("personas.numeroIdentificacion"), "numeroId")
				.add( Projections.property("personas.primerNombre"), "primerNombre")
				.add( Projections.property("personas.segundoNombre"), "segundoNombre")
				.add( Projections.property("personas.primerApellido"), "primerApellido")
				.add( Projections.property("personas.segundoApellido"), "segundoApellido"));
		
			if (conSolicituDcto.equals(ConstantesBD.acronimoNo)) {
				criteria.add( Property.forName("presupuesto.codigoPk").notIn(solicitudesDcto) );
			}
			
			if (conSolicituDcto.equals(ConstantesBD.acronimoSi)) {
				criteria.add( Property.forName("presupuesto.codigoPk").in(solicitudesDcto) );
			}
			criteria.add(Restrictions.in("presupuesto.codigoPk", presupuestos));
			criteria.add(Restrictions.eq("presupuesto.estado", ConstantesIntegridadDominio.acronimoPrecontratado));
		
		criteria.setResultTransformer(Transformers.aliasToBean(DtoIngresosOdontologicos.class));
		ArrayList<DtoIngresosOdontologicos> lista=(ArrayList)criteria.list();
		
		return lista;
	}
	
	/**
	 * M&eacute;todo que se encarga de obtener los presupuestos odontologicos
	 * contratados
	 * @param dto
	 * @author Diana Carolina G&oacute;mez
	 */
	
	public ArrayList<DtoPresupuestosOdontologicosContratados> obtenerPresupuestoOdoContratado(DtoReportePresupuestosOdontologicosContratados dto, String tSalidaReporte){
		
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(PresupuestoOdontologico.class,"presupuestoOdonto")
		.createAlias("presupuestoOdonto.centroAtencion", "centroAtencionContrato")
		.createAlias("centroAtencionContrato.ciudades", "ciudades")
		.createAlias("ciudades.paises", "paises")
		.createAlias("centroAtencionContrato.regionesCobertura", "regionesCobertura")
		.createAlias("presupuestoOdonto.presupuestoContratado", "presupuestoContratado") 
		.createAlias("presupuestoOdonto.pacientes", "pacientes")
		.createAlias("pacientes.centroAtencionByCentroAtencionDuenio", "centroAtencionDuenio", Criteria.LEFT_JOIN )
		.createAlias("pacientes.personas", "personas")
		.createAlias("personas.tiposIdentificacion", "tipoId")
		
		.createAlias("presupuestoOdonto.usuariosByUsuarioModifica", "profesionalContrato")
		.createAlias("profesionalContrato.personas", "datosProfesionalContrato")
		
		.createAlias("presupuestoOdonto.planTratamiento", "planTratamiento")
		.createAlias("planTratamiento.valoracionesOdonto", "valoracionesOdonto", Criteria.LEFT_JOIN)
		.createAlias("valoracionesOdonto.usuarios", "profesionalValoro", Criteria.LEFT_JOIN)
		.createAlias("profesionalValoro.personas", "datosProfesionalValoro", Criteria.LEFT_JOIN)
		
		.createAlias("presupuestoOdonto.presupuestoOdoProgServs", "presupuestoOdoProgServs")		
		.createAlias("presupuestoOdoProgServs.presupuestoOdoConvenios","presupuestoOdoConvenios")
		.createAlias("presupuestoOdoProgServs.programas","programas")
		
		.createAlias("presupuestoOdoConvenios.presupuestoPaquetes", "presupuestoPaquetes", Criteria.LEFT_JOIN)
		.createAlias("presupuestoPaquetes.detPaqOdontConvenio", "detPaqOdontConvenio", Criteria.LEFT_JOIN)
		.createAlias("detPaqOdontConvenio.paquetesOdontologicos", "paquetesOdontologicos", Criteria.LEFT_JOIN)
		
		.createAlias("planTratamiento.detPlanTratamientos", "detPlanTratamientos", Criteria.LEFT_JOIN)
		.createAlias("detPlanTratamientos.programasServiciosPlanTs", "programasServiciosPlanTs", Criteria.LEFT_JOIN)
		.createAlias("programasServiciosPlanTs.servicios", "servicios", Criteria.LEFT_JOIN)
		.createAlias("programasServiciosPlanTs.programas", "programasPlanTs", Criteria.LEFT_JOIN);
		
		if(! UtilidadTexto.isEmpty(dto.getServicio().getCodigo())
				&& !dto.getServicio().getCodigo().trim().equals(ConstantesBD.codigoNuncaValido+"")){
			
			int codigoServicio = Integer.parseInt(dto.getServicio().getCodigo());
			criteria.add(Restrictions.eq("servicios.codigo", codigoServicio));
			criteria.add(Restrictions.eqProperty("programas.codigo", "programasPlanTs.codigo"));
			criteria.add(Restrictions.eq("presupuestoOdoConvenios.contratado", ConstantesBD.acronimoSi));

		}
		
		if((dto.getCodigoPrograma()!=ConstantesBD.codigoNuncaValidoLong) && (dto.getCodigoPrograma()>0)){
			criteria.add(Restrictions.eq("programas.codigo", dto.getCodigoPrograma()));
			criteria.add(Restrictions.eq("presupuestoOdoConvenios.contratado", ConstantesBD.acronimoSi));
		}
		
		/****EsMultiempresa ***/
		if(dto.isEsMultiempresa()){
			criteria.createAlias("centroAtencionContrato.empresasInstitucion", "institucion", Criteria.LEFT_JOIN);
		}else{
			criteria.createAlias("centroAtencionContrato.instituciones", "institucion");
		}
		/****EsMultiempresa ***/
		
		
		criteria.setProjection(Projections.distinct(Projections.projectionList()
				.add(Projections.property("presupuestoOdonto.codigoPk"),"codigoPkPresupuesto")
				.add(Projections.property("institucion.razonSocial"),"nombreInstitucion")
				.add(Projections.property("institucion.codigo"),"codigoInstitucion")
				.add(Projections.property("centroAtencionContrato.consecutivo"), "consCentroAtencionContrato")
				.add(Projections.property("centroAtencionContrato.descripcion"), "descCentroAtencionContrato")
				.add(Projections.property("paises.descripcion"),"descripcionPais")
				.add(Projections.property("ciudades.descripcion"),"descripcionCiudad")
				.add(Projections.property("regionesCobertura.descripcion"),"descripcionRegionCobertura")
				
				.add(Projections.property("presupuestoOdonto.estado"),"estadoPresupuesto")
				.add(Projections.property("centroAtencionDuenio.consecutivo"),"consCentroAtencionDuenio")
				.add(Projections.property("centroAtencionDuenio.descripcion"),"descCentroAtencionDuenio")
				.add(Projections.property("presupuestoOdonto.fechaModifica"),"fechaContrato")
				.add(Projections.property("presupuestoContratado.consecutivo"),"numeroContrato")
				.add(Projections.property("tipoId.acronimo"),"tipoId")
				.add(Projections.property("personas.numeroIdentificacion"),"numeroId")
				.add(Projections.property("personas.primerNombre"),"primerNombre")
				.add(Projections.property("personas.segundoNombre"),"segundoNombre")
				.add(Projections.property("personas.primerApellido"),"primerApellido")
				.add(Projections.property("personas.segundoApellido"),"segundoApellido")
				
				.add(Projections.property("profesionalContrato.login"),"loginProfesionalContrato")
				.add(Projections.property("datosProfesionalContrato.primerNombre"),"primerNombreProfesionalContrato") 
				.add(Projections.property("datosProfesionalContrato.primerApellido"),"primerApellidoProfesionalContrato")

				.add(Projections.property("profesionalValoro.login"),"loginProfesionalValoro")
				.add(Projections.property("datosProfesionalValoro.primerNombre"),"primerNombreProfesionalValoro")
				.add(Projections.property("datosProfesionalValoro.primerApellido"),"primerApellidoProfesionalValoro")));
		
				
		criteria.add(Restrictions.between("presupuestoOdonto.fechaModifica", dto.getFechaInicial(), dto.getFechaFinal()));
		criteria.add(Restrictions.eq("paises.codigoPais",dto.getCodigoPaisResidencia()));
		
		String[] listaEstados = {   
			ConstantesIntegridadDominio.acronimoContratadoContratado,
			ConstantesIntegridadDominio.acronimoContratadoCancelado,
			ConstantesIntegridadDominio.acronimoContratadoSuspendidoTemporalmente,
			ConstantesIntegridadDominio.acronimoContratadoTerminado };
		
		criteria.add(Restrictions.in("presupuestoOdonto.estado",listaEstados));
		
		if (!UtilidadTexto.isEmpty(dto.getCiudadDeptoPais()) && !dto.getCiudadDeptoPais().trim().equals(ConstantesBD.codigoNuncaValido + "")) {
			
			String vec[]= dto.getCiudadDeptoPais().split(ConstantesBD.separadorSplit);			
			dto.setCodigoCiudad(vec[0]);
			dto.setCodigoDpto(vec[1]);
			dto.setCodigoPais(vec[2]);
			
			criteria.add(Restrictions.eq("ciudades.id.codigoCiudad", dto.getCodigoCiudad()))
			.add(Restrictions.eq("ciudades.id.codigoPais", dto.getCodigoPais()))
			.add(Restrictions.eq("ciudades.id.codigoDepartamento", dto.getCodigoDpto()));
		} 
		
		
		if (dto.getCodigoRegion() > 0) {
			criteria.add(Restrictions.eq("regionesCobertura.codigo", dto.getCodigoRegion()));
		}
		
		if(dto.getCodigoEmpresaInstitucion() > 0){
			criteria.add(Restrictions.eq("institucion.codigo",dto.getCodigoEmpresaInstitucion()));
		}
		
		if (dto.getConsecutivoCentroAtencion() > 0) {
			criteria.add(Restrictions.eq("centroAtencionContrato.consecutivo", dto.getConsecutivoCentroAtencion()));
		}

		if(!UtilidadTexto.isEmpty(dto.getIndicativoContrato()) && !dto.getIndicativoContrato().equals(ConstantesBD.codigoNuncaValido + "")){
			criteria.add(Restrictions.eq("presupuestoOdonto.estado", dto.getIndicativoContrato()));
		}
		
		if(!UtilidadTexto.isEmpty(dto.getLoginProfesionalContrato()) && !dto.getLoginProfesionalContrato().equals(ConstantesBD.codigoNuncaValido + "")){
			criteria.add(Restrictions.eq("profesionalContrato.login",dto.getLoginProfesionalContrato()));
		}
		
		if(!UtilidadTexto.isEmpty(dto.getLogin()) && !dto.getLogin().equals(ConstantesBD.codigoNuncaValido + "")){
			criteria.add(Restrictions.eq("profesionalValoro.login", dto.getLogin()));
			
		}
		
		if(dto.getCodigoPaqueteOdonto()>0){
			criteria.add(Restrictions.eq("paquetesOdontologicos.codigoPk", dto.getCodigoPaqueteOdonto()));
			
		}
		
		
		
		criteria.addOrder( Order.asc("institucion.razonSocial"));
		criteria.addOrder( Order.asc("centroAtencionContrato.descripcion") );
		criteria.addOrder( Order.asc("presupuestoOdonto.estado") );
		criteria.addOrder( Order.asc("presupuestoOdonto.fechaModifica") );
		criteria.setResultTransformer(Transformers.aliasToBean(DtoPresupuestosOdontologicosContratados.class));
		
		
		ArrayList<DtoPresupuestosOdontologicosContratados> lista= (ArrayList<DtoPresupuestosOdontologicosContratados>)criteria.list() ;
	
		
		/*  Consulta de Codigos de Programas y Paquetes */
		String tmp="";
		
		
		
		for( DtoPresupuestosOdontologicosContratados dtoPresupuesto: lista)
		{
			 tmp =cargarProgramas(dtoPresupuesto.getCodigoPkPresupuesto(), tSalidaReporte);
			 dtoPresupuesto.setCodigoPrograma(tmp);
			 
		}
		return lista;
		
					
	}
	
	/**
	 * M&eacute;todo encargado de cargar los paquetes odontologicos
	 * a los cuales pertenecen los programas asociados a los  
	 * presupuestos odontologicos contratados
	 * @param codigoPresupuesto
	 * @param codigoPrograma
	 * @author Diana Carolina G&oacute;mez
	 * @return
	 */
	
	private String cargarPaquetes(long codigoPresupuesto, long codigoPrograma) {
		
		
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(PresupuestoOdontologico.class,"presupuestoOdonto")
		.createAlias("presupuestoOdonto.presupuestoOdoProgServs", "presupuestoOdoProgServs")
		.createAlias("presupuestoOdoProgServs.presupuestoOdoConvenios","presupuestoOdoConvenios")
		.createAlias("presupuestoOdoProgServs.programas","programas")
		.createAlias("presupuestoOdoConvenios.presupuestoPaquetes", "presupuestoPaquetes")
		.createAlias("presupuestoPaquetes.detPaqOdontConvenio", "detPaqOdontConvenio")
		.createAlias("detPaqOdontConvenio.paquetesOdontologicos", "paquetesOdontologicos");
		criteria.add(Restrictions.eq("presupuestoOdonto.codigoPk", codigoPresupuesto));
		criteria.add(Restrictions.eq("programas.codigo", codigoPrograma));
		criteria.add(Restrictions.eq("presupuestoOdoConvenios.contratado", ConstantesBD.acronimoSi));
		
		
		criteria.setProjection(Projections.projectionList()
				.add(Projections.property("paquetesOdontologicos.codigoPk"),"codigoPkPaquete")
				.add(Projections.property("paquetesOdontologicos.codigo"),"codigoPaquete"));
		criteria.setResultTransformer(Transformers.aliasToBean(DtoPaqueteReport.class));
			
		
		
		
		String tmpCodigoPaquete="";
		try{
			DtoPaqueteReport dtoPaqueteTMP=(DtoPaqueteReport)criteria.uniqueResult();
			if(dtoPaqueteTMP!=null)
			{
				tmpCodigoPaquete="p("+dtoPaqueteTMP.getCodigoPaquete()+")";
			}
		}
		catch (Exception e) {
			
		}
		
		return tmpCodigoPaquete;
	}



	/**
	 * M&eacute;todo encargado de cargar los programas odontologicos
	 * asociados a los presupuestos odontologicos contratados
	 * @author Diana Carolina G&oacute;mez
	 * @param codigoPresupuesto
	 * @return
	 */
	public String cargarProgramas(long codigoPresupuesto, String tSalidaReporte){
		
		StringBuilder tmpProgramas= new StringBuilder();
		
	
		
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(PresupuestoOdontologico.class,"presupuestoOdonto")
		.createAlias("presupuestoOdonto.presupuestoOdoProgServs", "presupuestoOdoProgServs")
		.createAlias("presupuestoOdoProgServs.programas","programas")
		.createAlias("presupuestoOdoProgServs.presupuestoOdoConvenios","presupuestoOdoConvenio");
		criteria.add(Restrictions.eq("presupuestoOdonto.codigoPk", codigoPresupuesto));
		criteria.add(Restrictions.eq("presupuestoOdoConvenio.contratado", ConstantesBD.acronimoSi));
	
		criteria.setProjection(Projections.distinct(Projections.projectionList()
				.add(Projections.property("programas.codigo"),"codigoPkPrograma")
				.add(Projections.property("programas.codigoPrograma"),"codigoPrograma")));
		criteria.setResultTransformer(Transformers.aliasToBean(DtoProgramaReport.class));
			
		
		List<DtoProgramaReport> listTmp=  criteria.list();
		
		int tipoSalida 	= Integer.parseInt(tSalidaReporte);
		/*
		 * recorrido
		 */
		for(DtoProgramaReport dtoProg :listTmp)
		{
			
			tmpProgramas.append(cargarPaquetes(codigoPresupuesto, dtoProg.getCodigoPkPrograma())+" "+dtoProg.getCodigoPrograma());
			
			if (tipoSalida == EnumTiposSalida.PDF.getCodigo() ){
				
				tmpProgramas.append("\n"); 
			}
			else
			{
				tmpProgramas.append("-");
			} 
			
		}
		
		
		String tmp ="";
		if(tmpProgramas!=null)
		{
			tmp=tmpProgramas.toString();
		}
		
		
		return tmp;
		
	}
	
	
	
	
	
	/**
	 * 
	 * Este Método se encarga de consultar los pacientes por
	 * estados del presupuesto odontológico, según los filtros
	 * enviados por parámetro
	 * 
	 * @param DtoReporteConsultaPacienteEstadoPresupuesto
	 * @return ArrayList<DtoReporteConsultaPacienteEstadoPresupuesto>
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ArrayList<DtoReporteConsultaPacienteEstadoPresupuesto> buscarPresupuestoOdontologicoPorEstado(
			DtoReporteConsultaPacienteEstadoPresupuesto dto){
		ArrayList<DtoReporteConsultaPacienteEstadoPresupuesto> listaDTO = null;
		
		Criteria criteria = sessionFactory.getCurrentSession()
			.createCriteria(PresupuestoOdontologico.class, "presupuesto");
		ProjectionList projectionList =  Projections.projectionList();
		
		criteria.createAlias("presupuesto.centroAtencion", "centroAtencion");
		criteria.createAlias("centroAtencion.ciudades", "ciudad");
		criteria.createAlias("ciudad.paises", "pais");
		criteria.createAlias("centroAtencion.regionesCobertura", "region");
		criteria.createAlias("presupuesto.pacientes", "paciente");
		criteria.createAlias("paciente.personas", "persona");
		criteria.createAlias("persona.sexo", "sexo");
				
		if(dto.isEsMultiempresa()){
			criteria.createAlias("centroAtencion.empresasInstitucion", "institucion");
		}else{
			criteria.createAlias("centroAtencion.instituciones", "institucion");
		}
		
		if(!UtilidadTexto.isEmpty(dto.getEstadoPresupuesto())){			
			if(dto.getEstadoPresupuesto().equals(ConstantesIntegridadDominio.acronimoEstadoActivo)||
					dto.getEstadoPresupuesto().equals(ConstantesIntegridadDominio.acronimoInactivo)	){
				criteria.add(Restrictions.eq("presupuesto.estado",dto.getEstadoPresupuesto()));
			}else{
				if(dto.getEstadoPresupuesto().equals(ConstantesIntegridadDominio.acronimoContratado)){
					criteria.createAlias("presupuesto.presupuestoContratado", "presupuestoContratado");
					if(!UtilidadTexto.isEmpty(dto.getIndicativoContrato()))
					{
						criteria.add(Restrictions.eq("presupuesto.estado",dto.getIndicativoContrato()));
					}
					else
					{
						criteria.add(Restrictions.like("presupuesto.estado","%"+ConstantesIntegridadDominio.acronimoPresupuestoContratado+"%"));
					}
				}else{
					if(dto.getEstadoPresupuesto().equals(ConstantesIntegridadDominio.acronimoPrecontratado)){
						criteria.add(Restrictions.eq("presupuesto.estado",ConstantesIntegridadDominio.acronimoPrecontratado));
						
						if(dto.getConSolicitudDcto().equals(ConstantesBD.acronimoSi)){
							
							criteria.createAlias("presupuesto.presupuestoDctoOdons", "descuentoPresupuesto");
							
						}else{
							if(dto.getConSolicitudDcto().equals(ConstantesBD.acronimoNo)){
								DetachedCriteria solicitudesDcto = DetachedCriteria.forClass(PresupuestoDctoOdon.class, "solicitud") 
								 .setProjection( Property.forName("presupuestoOdontologico"));		
								
								criteria.add( Property.forName("presupuesto.codigoPk").notIn(solicitudesDcto) );
							}
						}										
					}
				}
			}
		}		
		
		if(dto.getCodigoPaqueteOdonto()!=ConstantesBD.codigoNuncaValido){
			criteria.createAlias("presupuesto.presupuestoPaqueteses", "presupuestoPaquete");
			criteria.createAlias("presupuestoPaquete.detPaqOdontConvenio", "detallePaquete");
			criteria.createAlias("detallePaquete.paquetesOdontologicos", "paqueteOdontologico");
			
			criteria.add(Restrictions.eq("paqueteOdontologico.codigoPk", dto.getCodigoPaqueteOdonto()));
			
			projectionList.add(Projections.property("paqueteOdontologico.descripcion"),"nombrePaquete");
			projectionList.add(Projections.groupProperty("paqueteOdontologico.descripcion"));
			
		}else{
			if(dto.getCodigoPrograma()!=ConstantesBD.codigoNuncaValidoLong){
				criteria.createAlias("presupuesto.presupuestoOdoProgServs", "presupuestoProgramaServicio");
				criteria.createAlias("presupuestoProgramaServicio.programas", "programa");
				
				criteria.add(Restrictions.eq("programa.codigo", dto.getCodigoPrograma()));
				
				projectionList.add(Projections.property("programa.nombre"),"nombrePrograma");
				projectionList.add(Projections.groupProperty("programa.nombre"));
			}
		}
		
		criteria.add(Restrictions.between("presupuesto.fechaGeneracion", dto.getFechaInicial(), dto.getFechaFinal()));
		criteria.add(Restrictions.eq("pais.codigoPais",dto.getCodigoPais()));
		
		if(!UtilidadTexto.isEmpty(dto.getCiudadDeptoPais())){
			
			String[] codigos = (dto.getCiudadDeptoPais()).split(ConstantesBD.separadorSplit);
			
			criteria.add(Restrictions.eq("ciudad.id.codigoCiudad", codigos[0]))
			.add(Restrictions.eq("ciudad.id.codigoDepartamento", codigos[1]))
			.add(Restrictions.eq("ciudad.id.codigoPais", codigos[2]));
		}		
		if(dto.getCodigoRegion()!=ConstantesBD.codigoNuncaValidoLong){
			criteria.add(Restrictions.eq("region.codigo", dto.getCodigoRegion()));
		}
		if(dto.getCodigoEmpresaInstitucion()!=ConstantesBD.codigoNuncaValidoLong){
			criteria.add(Restrictions.eq("institucion.codigo", dto.getCodigoEmpresaInstitucion()));			
		}
		if(dto.getConsecutivoCentroAtencion()!=null && 
				dto.getConsecutivoCentroAtencion()!=ConstantesBD.codigoNuncaValidoLong){
			criteria.add(Restrictions.eq("centroAtencion.consecutivo", dto.getConsecutivoCentroAtencion()));
		}
		if(dto.getEdadInicial()!=null && dto.getEdadInicial()>0){
			String rangoEdadInicial = UtilidadFecha.calcularFechaNacimiento(1, dto.getEdadInicial());
			String rangoEdadFinal = UtilidadFecha.calcularFechaNacimiento(1, dto.getEdadFinal());			
			criteria.add(Restrictions.between("persona.fechaNacimiento",UtilidadFecha.conversionFormatoFechaStringDate(rangoEdadFinal),
					UtilidadFecha.conversionFormatoFechaStringDate(rangoEdadInicial)));			
		}
		if(!UtilidadTexto.isEmpty(dto.getSexoPaciente())){
			criteria.add(Restrictions.eq("sexo.codigo", Integer.valueOf(dto.getSexoPaciente()).intValue()));
			projectionList.add(Projections.property("sexo.nombre"),"nombreSexo");
			projectionList.add(Projections.groupProperty("sexo.nombre"));
		}	
		if(dto.getCodigoInstitucion()!=ConstantesBD.codigoNuncaValido){
			criteria.add(Restrictions.eq("institucion.codigo", Long.valueOf((dto.getCodigoInstitucion())).intValue()));
		}
				
		projectionList.add(Projections.property("centroAtencion.descripcion"),"nombreCentroAtencion");
		projectionList.add(Projections.property("centroAtencion.consecutivo"),"codigoCentroAtencion");
		projectionList.add(Projections.property("region.descripcion"),"nombreRegion");
		projectionList.add(Projections.property("pais.descripcion"),"nombrePais");
		projectionList.add(Projections.property("ciudad.descripcion"),"nombreCiudad");
		projectionList.add(Projections.property("presupuesto.estado"),"nombreEstadoPresupuesto");
		projectionList.add(Projections.property("institucion.razonSocial"),"nombreInstitucion");
		projectionList.add(Projections.property("institucion.codigo"),"codigoInstitucion");
		
		projectionList.add(Projections.groupProperty("centroAtencion.descripcion"));
		projectionList.add(Projections.groupProperty("region.descripcion"));
		projectionList.add(Projections.groupProperty("pais.descripcion"));
		projectionList.add(Projections.groupProperty("ciudad.descripcion"));
		projectionList.add(Projections.groupProperty("presupuesto.estado"));
		projectionList.add(Projections.groupProperty("institucion.razonSocial"));
		projectionList.add(Projections.groupProperty("centroAtencion.consecutivo"));
		projectionList.add(Projections.groupProperty("institucion.codigo"));
		projectionList.add(Projections.count("presupuesto.estado"),"totalPresupuestoPorEstado");
				
		criteria.setProjection(projectionList);
		
		criteria.addOrder(Order.asc("institucion.razonSocial"));
		criteria.addOrder(Order.asc("centroAtencion.descripcion"));
		criteria.addOrder(Order.asc("presupuesto.estado"));			
						
		criteria.setResultTransformer(Transformers.aliasToBean(DtoReporteConsultaPacienteEstadoPresupuesto.class));
		listaDTO = (ArrayList<DtoReporteConsultaPacienteEstadoPresupuesto>)criteria.list();		
		
		return listaDTO;		
	}
	
	/**
	 * Este m&eacute;todo se encarga de obtener el totalizado por estado de 
	 * los presupuestos contratados.
	 * 
	 * @author Diana Carolina G&oacute;mez
	 */
	
	@SuppressWarnings("rawtypes")
	public ArrayList<DtoPresupuestosOdontologicosContratados> obtenerConsolidadoEstadosPresupuestosContratados(List<Long> codigoPkPresupuesto, int consecutivoCA){
		
		Criteria criteria = sessionFactory.getCurrentSession()
		.createCriteria(PresupuestoOdontologico.class, "presupuesto")
		.createAlias("presupuesto.centroAtencion", "centroAtencion")
		
		.setProjection(Projections.projectionList()
				.add(Projections.property("presupuesto.estado"), "estadoPresupuesto")
				.add(Projections.sum("presupuesto.estado"), "totalPresupuestoPorEstado")
				.add(Projections.groupProperty("presupuesto.estado")));
			
		
		criteria.add(Restrictions.eq("centroAtencion.consecutivo",consecutivoCA));
		criteria.add(Restrictions.in("presupuesto.codigoPk", codigoPkPresupuesto));

		
		criteria.setResultTransformer(Transformers.aliasToBean(DtoPresupuestosOdontologicosContratados.class))
		.addOrder( Order.asc("presupuesto.estado") );
		ArrayList<DtoPresupuestosOdontologicosContratados> lista=(ArrayList)criteria.list();
		
		return lista;
		
	}
	
	/**
	 * 
	 * Este Método se encarga de consultar el presupuesto odontológico, 
	 * que no esté asociado a una solicitud de descuento 
	 * 
	 * @param DtoReporteConsultaPacienteEstadoPresupuesto
	 * @return ArrayList<DtoReporteConsultaPacienteEstadoPresupuesto>
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ArrayList<PresupuestoOdontologico> buscarPresupuestoOdontologicoSinSolicitudDescuento(){
		
		ArrayList<PresupuestoOdontologico> lista;
						
		Criteria criteria = sessionFactory.getCurrentSession()
		.createCriteria(PresupuestoOdontologico.class, "presupuesto");
		
		DetachedCriteria solicitudesDcto = DetachedCriteria.forClass(PresupuestoDctoOdon.class, "solicitud") 
		.setProjection( Property.forName("presupuestoOdontologico"));
		
		criteria.add( Property.forName("presupuesto.codigoPk").notIn(solicitudesDcto) );
						
		lista = (ArrayList<PresupuestoOdontologico>)criteria.list();
		
		return lista;
		
	}	
	
	/**
	 * 
	 * Este Método se encarga de consultar los pacientes por estado del 
	 * presupuesto.
	 * 
	 * 
	 * @param DtoReporteConsultaPacienteEstadoPresupuesto dto
	 * @return ArrayList<DTOPacientesReportePacientesEstadoPresupuesto>
	 * @author, Angela Maria Aguirre
	 * 
	 *
	 */
	public ArrayList<DTOPacientesReportePacientesEstadoPresupuesto> 
		buscarPacientesEstadoPresupuestoTotalizado(DtoReporteConsultaPacienteEstadoPresupuesto dto){
			
		Criteria criteria = sessionFactory.getCurrentSession()
		.createCriteria(PresupuestoOdontologico.class, "presupuesto");
		ProjectionList projectionList =  Projections.projectionList();
		
		criteria.createAlias("presupuesto.centroAtencion", "centroAtencion");
		criteria.createAlias("centroAtencion.ciudades", "ciudad");
		criteria.createAlias("ciudad.paises", "pais");
		criteria.createAlias("presupuesto.pacientes", "paciente");		
		criteria.createAlias("paciente.personas", "persona");
		criteria.createAlias("persona.tiposIdentificacion", "tipoID");
		criteria.createAlias("persona.sexo", "sexo");
		
		criteria.createAlias("paciente.centroAtencionByCentroAtencionDuenio", "CentroAtencinDuenio",Criteria.LEFT_JOIN);
		
		if(!UtilidadTexto.isEmpty(dto.getEstadoPresupuesto())){			
			if(dto.getEstadoPresupuesto().equals(ConstantesIntegridadDominio.acronimoEstadoActivo)||
					dto.getEstadoPresupuesto().equals(ConstantesIntegridadDominio.acronimoInactivo)	){
				criteria.add(Restrictions.eq("presupuesto.estado",dto.getEstadoPresupuesto()));
			}else{
				if(dto.getEstadoPresupuesto().equals(ConstantesIntegridadDominio.acronimoContratado)){
					criteria.createAlias("presupuesto.presupuestoContratado", "presupuestoContratado");
					if(!UtilidadTexto.isEmpty(dto.getIndicativoContrato()))
					{
						criteria.add(Restrictions.eq("presupuesto.estado",dto.getIndicativoContrato()));
					}
					else
					{
						criteria.add(Restrictions.like("presupuesto.estado","%"+ConstantesIntegridadDominio.acronimoPresupuestoContratado+"%"));
					}
				}else{
					if(dto.getEstadoPresupuesto().equals(ConstantesIntegridadDominio.acronimoPrecontratado)){
						criteria.add(Restrictions.eq("presupuesto.estado",ConstantesIntegridadDominio.acronimoPrecontratado));
						
						if(dto.getConSolicitudDcto().equals(ConstantesBD.acronimoSi)){
							
							criteria.createAlias("presupuesto.presupuestoDctoOdons", "descuentoPresupuesto");
							
						}else{
							if(dto.getConSolicitudDcto().equals(ConstantesBD.acronimoNo)){
								DetachedCriteria solicitudesDcto = DetachedCriteria.forClass(PresupuestoDctoOdon.class, "solicitud") 
								 .setProjection( Property.forName("presupuestoOdontologico"));		
								
								criteria.add( Property.forName("presupuesto.codigoPk").notIn(solicitudesDcto) );
							}
						}										
					}
				}
			}
		}		
		
		if(dto.getCodigoPaqueteOdonto()!=ConstantesBD.codigoNuncaValido){
			criteria.createAlias("presupuesto.presupuestoPaqueteses", "presupuestoPaquete");
			criteria.createAlias("presupuestoPaquete.detPaqOdontConvenio", "detallePaquete");
			criteria.createAlias("detallePaquete.paquetesOdontologicos", "paqueteOdontologico");
			
			criteria.add(Restrictions.eq("paqueteOdontologico.codigoPk", dto.getCodigoPaqueteOdonto()));			
			
		}else{
			if(dto.getCodigoPrograma()!=ConstantesBD.codigoNuncaValidoLong){
				criteria.createAlias("presupuesto.presupuestoOdoProgServs", "presupuestoProgramaServicio");
				criteria.createAlias("presupuestoProgramaServicio.programas", "programa");
				
				criteria.add(Restrictions.eq("programa.codigo", dto.getCodigoPrograma()));								
			}
		}
		
		criteria.add(Restrictions.between("presupuesto.fechaGeneracion", dto.getFechaInicial(), dto.getFechaFinal()));
		criteria.add(Restrictions.eq("pais.codigoPais",dto.getCodigoPais()));
		criteria.add(Restrictions.eq("centroAtencion.consecutivo", dto.getConsecutivoCentroAtencion()));
		
		if(dto.getEdadInicial()!=null && dto.getEdadInicial()>0){
			String rangoEdadInicial = UtilidadFecha.calcularFechaNacimiento(1, dto.getEdadInicial());
			String rangoEdadFinal = UtilidadFecha.calcularFechaNacimiento(1, dto.getEdadFinal());			
			criteria.add(Restrictions.between("persona.fechaNacimiento",rangoEdadFinal, rangoEdadInicial));			
		}
		if(!UtilidadTexto.isEmpty(dto.getSexoPaciente())){
			criteria.add(Restrictions.eq("sexo.codigo", Integer.valueOf(dto.getSexoPaciente()).intValue()));			
		}	
						
		projectionList.add(Projections.property("persona.numeroIdentificacion"),"numeroIdentificacion");
		projectionList.add(Projections.property("persona.primerNombre"),"nombrePaciente");
		projectionList.add(Projections.property("persona.segundoNombre"),"segundoNombrePaciente");
		projectionList.add(Projections.property("persona.primerApellido"),"apellidoPaciente");
		projectionList.add(Projections.property("persona.segundoApellido"),"segundoApellidoPaciente");
		projectionList.add(Projections.property("CentroAtencinDuenio.descripcion"),"centroAtencionDueno");
		projectionList.add(Projections.property("presupuesto.consecutivo"),"numeroPresupuesto");
		projectionList.add(Projections.property("presupuesto.codigoPk"),"codigoPresupuesto");
		projectionList.add(Projections.property("presupuesto.fechaModifica"),"fechaModificacionPresupuesto");
		projectionList.add(Projections.property("tipoID.acronimo"),"tipoIdentificacion");
		projectionList.add(Projections.property("sexo.nombre"),"sexoPaciente");
		projectionList.add(Projections.property("persona.fechaNacimiento"),"fechaNacimiento");
		
		criteria.setProjection(projectionList);
		
		criteria.setResultTransformer(Transformers.aliasToBean(DTOPacientesReportePacientesEstadoPresupuesto.class));
		
		ArrayList<DTOPacientesReportePacientesEstadoPresupuesto> lista = 
			(ArrayList<DTOPacientesReportePacientesEstadoPresupuesto>)criteria.list();
		
		return lista;
				
	}
	
	
}