/*
 * Julio 14, 2010
 */
package com.servinte.axioma.orm.delegate.manejoPaciente;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.axioma.util.log.Log4JManager;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.hibernate.type.Type;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;

import com.princetonsa.dto.administracion.DtoPersonas;
import com.princetonsa.dto.capitacion.DTOPacienteCapitado;
import com.princetonsa.dto.manejoPaciente.DtoUsuariosCapitados;
import com.servinte.axioma.dto.tesoreria.DtoInfoIngresoPacienteControlarAbonoPacientes;
import com.servinte.axioma.orm.Convenios;
import com.servinte.axioma.orm.IngresosEstancia;
import com.servinte.axioma.orm.Pacientes;
import com.servinte.axioma.orm.PacientesHome;


/**
 * @author Cristhian Murillo
 *
 * Clase que contiene logica del negocio sobre el modelo 
 */
@SuppressWarnings("unchecked")
public class PacientesDelegate extends PacientesHome
{
	
	
	/**
	 * Lista todos
	 * 
	 * @author Cristhian Murillo
	 */
	public ArrayList<Pacientes> listarTodos()
	{
		return (ArrayList<Pacientes>) sessionFactory.getCurrentSession()
			.createCriteria(Pacientes.class)
			.list();
	}
	
	
	/**
	 * Retorna una persona por su numero de identificacion y tipo
	 * @param identificacionBuscar
	 * @param acronimoTipoIdentificacion
	 * @return DtoPersonas
	 * 
	 * @author Cristhian Murillo
	 */
	public DtoPersonas obtenerPaciente(String identificacionBuscar, String acronimoTipoIdentificacion)
	{
		return (DtoPersonas) sessionFactory.getCurrentSession()
			.createCriteria(Pacientes.class	, "pac")
			.createAlias("personas"			, "per")
			.createAlias("institucioneses"	, "insti")
			.createAlias("per.tiposIdentificacion", "tipoId")
			
			.add(Restrictions.eq("per.numeroIdentificacion"			, identificacionBuscar))
			.add(Restrictions.eq("per.tiposIdentificacion.acronimo"	, acronimoTipoIdentificacion))
			
			 .setProjection( Projections.projectionList()
					 .add( Projections.property("pac.codigoPaciente")		, "codigo" )
					 .add( Projections.property("tipoId.acronimo")			, "tipoId" )
					 .add( Projections.property("per.numeroIdentificacion")	, "numeroId" )
					 .add( Projections.property("per.primerNombre")			, "primerNombre" )
					 .add( Projections.property("per.segundoNombre")		, "segundoNombre" )
					 .add( Projections.property("per.primerApellido")		, "primerApellido" )
					 .add( Projections.property("per.segundoApellido")		, "segundoApellido" )
					 .add( Projections.property("insti.codigo")				, "institucion" )
	          )
			.setResultTransformer( Transformers.aliasToBean(DtoPersonas.class))
			.uniqueResult();
	}
	
	
	/**
	 * Retorna un boolean que indica si el paciente enviado coincide el estado del 
	 * presupuesto odontologico dado
	 *  
	 * @param idPaciente
	 * @param listaEstadosPresupuesto
	 * @return boolean
	 * @author Cristhian Murillo
	 */
	public boolean tienePacientePresupuestoEnEstados(int idPaciente, String[] listaEstadosPresupuesto)
	{
		boolean tiene = false;
		
		Pacientes paciente = (Pacientes) sessionFactory.getCurrentSession()
			.createCriteria(Pacientes.class				, "pac")
			.createAlias("presupuestoOdontologicos"		, "presupuesto")
			
			.add(Restrictions.eq("pac.codigoPaciente"	,  idPaciente))
			.add(Restrictions.in("presupuesto.estado"	,  listaEstadosPresupuesto))
			.uniqueResult();
		
		if(paciente != null){
			tiene = true;
		}
		
		return tiene;
	}
	
	
	/**
	 * Carga la informacion del apciente con su codigo
	 * @param identificacionBuscar
	 * @param acronimoTipoIdentificacion
	 * @return DtoPersonas
	 * 
	 * @author Cristhian Murillo
	 */
	public DtoPersonas obtenerDatosPaciente(int codPaciente)
	{
		return (DtoPersonas) sessionFactory.getCurrentSession()
			.createCriteria(Pacientes.class	, "pac")
			.createAlias("personas"			, "per")
			.createAlias("institucioneses"	, "insti")
			.createAlias("per.tiposIdentificacion", "tipoId")
			.createAlias("pac.centroAtencionByCentroAtencionDuenio"	, "ca_duenio", Criteria.LEFT_JOIN)
			
			.add(Restrictions.eq("pac.codigoPaciente"	, codPaciente))
			
			 .setProjection( Projections.projectionList()
					 .add( Projections.property("pac.codigoPaciente")		, "codigo" )
					 .add( Projections.property("tipoId.acronimo")			, "tipoId" )
					 .add( Projections.property("per.numeroIdentificacion")	, "numeroId" )
					 .add( Projections.property("per.primerNombre")			, "primerNombre" )
					 .add( Projections.property("per.segundoNombre")		, "segundoNombre" )
					 .add( Projections.property("per.primerApellido")		, "primerApellido" )
					 .add( Projections.property("per.segundoApellido")		, "segundoApellido" )
					.add( Projections.property("ca_duenio.consecutivo")		, "cAtencionDuenioInteger" )
					 .add( Projections.property("ca_duenio.descripcion")	, "descripcionCentroAtencionDuenio" )
					 .add( Projections.property("insti.codigo")				, "institucion" )
	          )
			.setResultTransformer( Transformers.aliasToBean(DtoPersonas.class))
			.uniqueResult();
	}
	
	
	/**
	 * Este m&eacute;todo se encarga de obtener un listado con la informaci&oacute;n 
	 * de los pacientes de un determinado centro de atenci&oacute;n.
	 * 
	 * @author Yennifer Guerrero
	 */
	public ArrayList<DtoPersonas> obtenerInfoPacienteValoracionInicial(int codigoPaciente){
		
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Pacientes.class,"pacientes")
		.createAlias("pacientes.personas", "personas")
		.createAlias("personas.tiposIdentificacion", "tipoId");
		
		criteria.add(Restrictions.eq("personas.codigo", codigoPaciente));
		
		
		criteria.setProjection(Projections.projectionList()
				.add(Projections.property("tipoId.acronimo"),"tipoId")
				.add(Projections.property("personas.numeroIdentificacion"),"numeroId")
				.add(Projections.property("personas.primerNombre"),"primerNombre")
				.add( Projections.property("personas.segundoNombre")	, "segundoNombre" )
				.add( Projections.property("personas.primerApellido"), "primerApellido" )
				.add( Projections.property("personas.segundoApellido"), "segundoApellido" )
				
				//proyectar el estado de la valoracion inicial.
				.add( Projections.property("personas.telefonoFijo"), "telefonoFijo" )
				.add( Projections.property("personas.telefonoCelular"), "telefonoCelular" )
					
		)
				
		.setResultTransformer( Transformers.aliasToBean(DtoPersonas.class));
		ArrayList<DtoPersonas> listaPacientes=(ArrayList)criteria.list();
		
		return listaPacientes;
	}
	
	
	@Override
	public Pacientes findById(int id) {
		return super.findById(id);
	}

	
	/**
	 * Busca un paciente por los parametros enviados.
	 * @param parametrosBusqueda
	 * @return ArrayList<DtoUsuariosCapitados>
	 * 
	 * @author Cristhian Murillo
	 */
	public ArrayList<DtoUsuariosCapitados> buscarPacienteConvenio(DtoUsuariosCapitados parametrosBusqueda)
	{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Pacientes.class,"pacientes");
		
		// Join Persona
		criteria.createAlias("pacientes.personas"				, "personas");
		criteria.createAlias("personas.tiposIdentificacion"		, "tiposIdentificacion");
		// Join Convenio
		criteria.createAlias("personas.usuarioXConvenios"		, "usuarioXConvenios"		, Criteria.LEFT_JOIN);		
		/*
		 * Se busca en usuariosxconvenio el tipo de afiliado y clasificiación socioeconomica 
		 * DCU 1094 versión 1.1
		 * Diana Ruiz
		 */
		criteria.createAlias("usuarioXConvenios.tiposAfiliado"		, "tiposAfiliado"		, Criteria.LEFT_JOIN);
		criteria.createAlias("usuarioXConvenios.estratosSociales"	, "estratosSociales"	, Criteria.LEFT_JOIN);
		criteria.createAlias("usuarioXConvenios.naturalezaPacientes"	, "naturalezaPacientes"	, Criteria.LEFT_JOIN);
				
		criteria.createAlias("usuarioXConvenios.contratos"		, "contratos"				, Criteria.LEFT_JOIN);		
		criteria.createAlias("contratos.convenios"				, "convenios"				, Criteria.LEFT_JOIN);
		criteria.createAlias("convenios.tiposContrato"			, "tiposContrato");
		// Join TipoUsuario/TipoAfiliado 
		criteria.createAlias("pacientes.subCuentases"			, "subCuentases"			, Criteria.LEFT_JOIN);
		//criteria.createAlias("subCuentases.tiposAfiliado"		, "tiposAfiliado"			, Criteria.LEFT_JOIN);
		//criteria.createAlias("subCuentases.estratosSociales"	, "estratosSociales"		, Criteria.LEFT_JOIN);
		// Join Ingreso
		criteria.createAlias("pacientes.ingresoses"				, "ingresoses"				, Criteria.LEFT_JOIN);
		criteria.createAlias("ingresoses.cuentases"				, "cuentases"				, Criteria.LEFT_JOIN);
		
		criteria.createAlias("subCuentases.detalleMonto"		, "detalleMonto"			, Criteria.LEFT_JOIN);
		
		// Parametro convenio
		if(!UtilidadTexto.isEmpty(parametrosBusqueda.getConvenio()))
		{
			Date fechaActual = UtilidadFecha.getFechaActualTipoBD();
			criteria.add(Restrictions.eq("convenios.codigo"				, Integer.parseInt(parametrosBusqueda.getConvenio())));
			//MT 5788 se aumenta la restricción  de la fecha inicial y final del convenio
			criteria.add(Restrictions.ge("usuarioXConvenios.fechaFinal"			, fechaActual));
			criteria.add(Restrictions.le("usuarioXConvenios.fechaInicial"		, fechaActual));
			//FIN MT
			criteria.add(Restrictions.ge("contratos.fechaFinal"			, fechaActual));
			criteria.add(Restrictions.le("contratos.fechaInicial"		, fechaActual));
		}
		//Parametro TipoUsuario/TipoAfiliado 
		if(!UtilidadTexto.isEmpty(parametrosBusqueda.getTipoAfiliado()+""))
		{
			criteria.add(Restrictions.eq("tiposAfiliado.acronimo"		, parametrosBusqueda.getTipoAfiliado()));
		}
		//Parametro Tipo Identificación
		if(!UtilidadTexto.isEmpty(parametrosBusqueda.getTipoIdentificacion()))
		{
			criteria.add(Restrictions.eq("tiposIdentificacion.acronimo"	, parametrosBusqueda.getTipoIdentificacion()));
		}
		//Parametro Número Identificación
		if(!UtilidadTexto.isEmpty(parametrosBusqueda.getNumeroIdentificacion()))
		{
			criteria.add(Restrictions.eq("personas.numeroIdentificacion", parametrosBusqueda.getNumeroIdentificacion()));
		}
		//Parametro Primer Nombre
		if(!UtilidadTexto.isEmpty(parametrosBusqueda.getPrimerNombre()))
		{
			criteria.add(Restrictions.like("personas.primerNombre", "%"+parametrosBusqueda.getPrimerNombre()+"%"));
		}
		//Parametro Segundo Nombre
		if(!UtilidadTexto.isEmpty(parametrosBusqueda.getSegundoNombre()))
		{
			criteria.add(Restrictions.like("personas.segundoNombre", "%"+parametrosBusqueda.getSegundoNombre()+"%"));
		}
		//Parametro Primer Apellido
		if(!UtilidadTexto.isEmpty(parametrosBusqueda.getPrimerApellido()))
		{
			criteria.add(Restrictions.like("personas.primerApellido", "%"+parametrosBusqueda.getPrimerApellido()+"%"));
		}
		//Parametro Segundo Apellido
		if(!UtilidadTexto.isEmpty(parametrosBusqueda.getSegundoApellido()))
		{
			criteria.add(Restrictions.like("personas.segundoApellido", "%"+parametrosBusqueda.getSegundoApellido()+"%"));
		}
		//Parametro Estado Ingreso
		if(!UtilidadTexto.isEmpty(parametrosBusqueda.getEstadoIngreso()))
		{
			if(parametrosBusqueda.getEstadoIngreso().equals(ConstantesIntegridadDominio.acronimoEstadoCerrado))
			{
				// Ingreso cerrado o que no tenga
				Disjunction disjunctionOR = Restrictions.disjunction();  
					disjunctionOR.add( Property.forName("ingresoses.estado").eq(parametrosBusqueda.getEstadoIngreso()) );  
					disjunctionOR.add( Property.forName("ingresoses.estado").isNull());  
				criteria.add(disjunctionOR);
			}
			else{
				criteria.add(Restrictions.eq("ingresoses.estado", parametrosBusqueda.getEstadoIngreso()));
			}
		}
		
		// Parametro contrato
		if(parametrosBusqueda.getCodigoContrato() != null) 
		{
			criteria.add(Restrictions.eq("contratos.codigo"	, parametrosBusqueda.getCodigoContrato()));
		}
		// Parametro Cuenta
		if(parametrosBusqueda.getIdCuenta() != null) 
		{
			criteria.add(Restrictions.eq("cuentases.id"		, parametrosBusqueda.getIdCuenta()));
		}
		
		//Parametro código Paciente
		if(parametrosBusqueda.getCodigoPaciente() != null){
			criteria.add(Restrictions.eq("pacientes.codigoPaciente", parametrosBusqueda.getCodigoPaciente()));
		}
		
		//Parametro Fecha vigencia carga
		if(parametrosBusqueda.getFechaVigenciaCargue() != null){
			 criteria.add(Restrictions.le("usuarioXConvenios.fechaInicial", parametrosBusqueda.getFechaVigenciaCargue()));
			  criteria.add(Restrictions.ge("usuarioXConvenios.fechaFinal", parametrosBusqueda.getFechaVigenciaCargue()));
		}

		ProjectionList projectionList = Projections.projectionList();
			projectionList.add(Projections.property("convenios.codigo")					,"convenioInt");
			projectionList.add(Projections.property("convenios.nombre")					,"nombreConvenio");
			projectionList.add(Projections.property("tiposAfiliado.acronimo")			,"tipoAfiliado");
			projectionList.add(Projections.property("tiposIdentificacion.acronimo")		,"tipoIdentificacion");
			projectionList.add(Projections.property("personas.numeroIdentificacion")	,"numeroIdentificacion");
			projectionList.add(Projections.property("personas.primerNombre")			,"primerNombre");
			projectionList.add(Projections.property("personas.segundoNombre")			,"segundoNombre");
			projectionList.add(Projections.property("personas.primerApellido")			,"primerApellido");
			projectionList.add(Projections.property("personas.segundoApellido")			,"segundoApellido");
			projectionList.add(Projections.property("ingresoses.estado")				,"estadoIngreso");
			projectionList.add(Projections.property("personas.fechaNacimiento")			,"fechaNacimiento");
			projectionList.add(Projections.property("personas.direccion")				,"direccion");
			projectionList.add(Projections.property("pacientes.codigoPaciente")			,"codigoPaciente");
			projectionList.add(Projections.property("tiposContrato.nombre")				,"nombreTipoContrato");
			projectionList.add(Projections.property("tiposAfiliado.nombre")				,"nombreTipoAfiliado");
			projectionList.add(Projections.property("estratosSociales.descripcion")		,"descripcionEstratoSocial");
			projectionList.add(Projections.property("estratosSociales.codigo")			,"codigoEstratoSocial");
			projectionList.add(Projections.property("contratos.codigo")					,"codigoContrato");
			projectionList.add(Projections.property("naturalezaPacientes.codigo")		,"naturaleza");
			projectionList.add(Projections.property("subCuentases.tipoCobroPaciente")	,"tipoCobroPaciente");
			projectionList.add(Projections.property("subCuentases.tipoMontoCobro")		,"tipoMontoCobro"); 
			projectionList.add(Projections.property("subCuentases.porcentajeMontoCobro"),"porcentajeMontoCobro");
			projectionList.add(Projections.property("detalleMonto.detalleCodigo")		,"detalleCodigo"); 
			projectionList.add(Projections.property("convenios.manejaPresupCapitacion")	,"manejaPresupuestoCapit");
			
			
		criteria.setProjection(projectionList);
		
		criteria.setResultTransformer( Transformers.aliasToBean(DtoUsuariosCapitados.class));
		
		ArrayList<DtoUsuariosCapitados> listaResultado = (ArrayList<DtoUsuariosCapitados>) criteria.list();
		
		return listaResultado;
	}
	
	
	/**
	 * M&eacute;todo encargado de obtener los datos del paciente
	 * asociados a un ingreso estancia
	 * @param codigoIngresoEstancia
	 * @return DTOPacienteCapitado
	 * @author Diana Carolina G
	 */
	public DTOPacienteCapitado buscarPacienteAutorizacionIngresoEstancia(long codigoIngresoEstancia){
		
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Pacientes.class,"pacientes");
		
		criteria.createAlias("pacientes.ingresosEstancias"						, "ingresosEstancias"							);
		criteria.createAlias("ingresosEstancias.autorizacionesIngreEstancias"	, "autorizacionesIngreEstancias"				);
		criteria.createAlias("autorizacionesIngreEstancias.centrosCosto"		, "centrosCosto"								);
		criteria.createAlias("pacientes.personas"								, "personas"									);
		criteria.createAlias("personas.tiposIdentificacion"						, "tiposIdentificacion"							);
		//criteria.createAlias("personas.usuarioXConvenios"						, "usuarioXConvenios"							);
		//criteria.createAlias("usuarioXConvenios.estratosSociales"				, "estratosSociales"	    , Criteria.LEFT_JOIN);
		//criteria.createAlias("pacientes.subCuentases"          	 			, "subCuentases"  			, Criteria.LEFT_JOIN);
		//criteria.createAlias("pacientes.cuentases"              				, "cuentases"  			    , Criteria.LEFT_JOIN);
		//criteria.createAlias("cuentases.tiposPaciente"          				, "tiposPaciente"  			, Criteria.LEFT_JOIN);
		criteria.createAlias("ingresosEstancias.entidadesSubcontratadas"		, "entidadesSubcontratadas"						);
		
		//criteria.createAlias("subCuentases.estratosSociales"    				, "estratosSociales" 		, Criteria.LEFT_JOIN);
		//criteria.createAlias("subCuentases.naturalezaPacientes" 				, "naturalezaPacientes" 	, Criteria.LEFT_JOIN);
		//criteria.createAlias("subCuentases.ingresos" 		          		 	, "ingresos" 	                                );
		criteria.createAlias("ingresosEstancias.tiposAfiliado"  				, "tiposAfiliado"			, Criteria.LEFT_JOIN);
		criteria.createAlias("ingresosEstancias.viasIngreso"    				, "viasIngreso"		   							);
		criteria.createAlias("ingresosEstancias.estratosSociales"    			, "estratosSociales"	  	, Criteria.LEFT_JOIN);
		
		criteria.createAlias("ingresosEstancias.diagnosticosByFkIeDxPpal"		,"diagnosticoIngresoEstancia", Criteria.LEFT_JOIN);
		
		
		if(codigoIngresoEstancia != ConstantesBD.codigoNuncaValidoLong){
			criteria.add(Restrictions.eq("ingresosEstancias.codigoPk", codigoIngresoEstancia));
		}
		
		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Projections.property("personas.primerNombre")					,"primerNombre");
		projectionList.add(Projections.property("personas.primerApellido")					,"primerApellido");
		projectionList.add(Projections.property("personas.segundoNombre")					,"segundoNombre");
		projectionList.add(Projections.property("personas.segundoApellido")					,"segundoApellido");
		projectionList.add(Projections.property("tiposIdentificacion.acronimo")				,"tipoId");
		projectionList.add(Projections.property("personas.numeroIdentificacion")			,"numeroId");
		projectionList.add(Projections.property("personas.fechaNacimiento")					,"fechaNacimientoTipoDate");
		projectionList.add(Projections.property("pacientes.codigoPaciente")					,"codigo");
		
		projectionList.add(Projections.property("tiposAfiliado.acronimo")					,"tipoAfiliadoChar");
		projectionList.add(Projections.property("tiposAfiliado.nombre")						,"tipoAfiliado");
		projectionList.add(Projections.property("estratosSociales.descripcion")				,"clasificacionSocioEconomica");
		projectionList.add(Projections.property("estratosSociales.codigo")					,"codigoClasificacionSE");
		projectionList.add(Projections.property("viasIngreso.codigo")                       ,"codigoViaIngreso");
		//projectionList.add(Projections.property("naturalezaPacientes.codigo")             ,"codigoNaturalezaPaciente");
		//projectionList.add(Projections.property("tiposPaciente.acronimo")                 ,"acronimotipoPaciente");
		projectionList.add(Projections.property("entidadesSubcontratadas.codigoPk")         ,"codigoPkEntidadSubcontratada");
		projectionList.add(Projections.property("ingresosEstancias.descripcionEntidadSub")  ,"descripcionEntidadSubOtra");
		projectionList.add(Projections.property("ingresosEstancias.direccionEntidadSub")    ,"direccionEntidadSubOtra");
		projectionList.add(Projections.property("ingresosEstancias.telefonoEntidadSub")     ,"telefonoEntidadSubOtra");
		//projectionList.add(Projections.property("cuentases.id")        					,"idCuenta");
		//projectionList.add(Projections.property("ingresos.id")							,"idIngreso");
		projectionList.add(Projections.property("diagnosticoIngresoEstancia.id.acronimo")	,"diagnosticoIngEst");
		projectionList.add(Projections.property("diagnosticoIngresoEstancia.id.tipoCie")	,"tipoCieDxIngEst");
		projectionList.add(Projections.property("diagnosticoIngresoEstancia.nombre")		,"descripcionDiagnosticoIngEst");
		projectionList.add(Projections.property("centrosCosto.codigo")						,"centroCostoRespondeIngreEstancia");
		projectionList.add(Projections.property("autorizacionesIngreEstancias.codigoPk")	,"codIngresoEstancia");
		projectionList.add(Projections.property("centrosCosto.tipoEntidadEjecuta")			,"tipoEntidadEjecuta");
		
		
		criteria.setProjection(projectionList);
		criteria.setResultTransformer(Transformers.aliasToBean(DTOPacienteCapitado.class));
		List<DTOPacienteCapitado>  lista  = new ArrayList<DTOPacienteCapitado>();
		try {
			 lista =criteria.list();
			
		} catch (Exception e) {
			Log4JManager.info(e.getMessage());
		}
		
		
		DTOPacienteCapitado dtoPaciente= new DTOPacienteCapitado();
		
		if(lista!=null && lista.size()>0)
		{
			dtoPaciente=lista.get(0);
			
			String strConsulta="select p.convenios from Pacientes  p  inner join p.convenios c where p.codigoPaciente= :codigo";
			
			List<Convenios> listaConvenios = sessionFactory.getCurrentSession().createQuery(strConsulta).setParameter("codigo", dtoPaciente.getCodigo()).list();
			
			if(listaConvenios.size()>0){
				for (Convenios convenio: listaConvenios ){
					Log4JManager.info(convenio.getNombre());
					Log4JManager.info(convenio.getCodigo());
					dtoPaciente.setConvenio(convenio);
					
				}
			}else{
				strConsulta="select s.convenios from SubCuentas s inner join s.pacientes p where p.codigoPaciente= :codigo";
				List<Convenios>  listConveniosSubcuenta= sessionFactory.getCurrentSession().createQuery(strConsulta).setParameter("codigo", dtoPaciente.getCodigo()).list();
				for (Convenios convenio: listConveniosSubcuenta ){
						Log4JManager.info(convenio.getNombre());
						Log4JManager.info(convenio.getCodigo());
						dtoPaciente.setConvenio(convenio);
					}
				}
		}
		
		return dtoPaciente ;
	}
	
	/**
	 * Retorna el abono disponible del paciente de un paciente.
	 * 
	 * @param codPaciente
	 * @return
	 */
	public ArrayList<DtoInfoIngresoPacienteControlarAbonoPacientes> cargarAbonoDisponiblePorPaciente(int codPaciente) {

		Criteria criteria = sessionFactory.getCurrentSession()
		.createCriteria(Pacientes.class, "paciente");
		
		criteria.setProjection(Projections.distinct(Projections.projectionList()
				.add( Projections.sqlProjection("getabonodisponible(this_.codigo_paciente, null) AS saldoActual", new String[]{"saldoActual"}, new Type[]{Hibernate.DOUBLE}))));
		criteria.add(Restrictions.eq("paciente.codigoPaciente", codPaciente));
		criteria.setResultTransformer(Transformers.aliasToBean(DtoInfoIngresoPacienteControlarAbonoPacientes.class));
		
		return (ArrayList<DtoInfoIngresoPacienteControlarAbonoPacientes>)criteria.list();
		
	}
	
	
}
