package com.servinte.axioma.orm.delegate.manejoPaciente;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.axioma.util.log.Log4JManager;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.AliasToBeanConstructorResultTransformer;
import org.hibernate.transform.Transformers;
import org.hibernate.type.Type;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.ValoresPorDefecto;

import com.princetonsa.dto.manejoPaciente.DTOAdministracionAutorizacion;
import com.princetonsa.dto.manejoPaciente.DTOAutorizacionIngresoEstancia;
import com.princetonsa.dto.manejoPaciente.DTOBusquedaAutorizacionCapitacionRango;
import com.servinte.axioma.orm.AutorizacionesIngreEstancia;
import com.servinte.axioma.orm.AutorizacionesIngreEstanciaHome;

/**
 * Esta clase se encarga de ejecutar los métodos de 
 * negocio de la entidad Autorizaciones Ingreso Estancia
 * 
 * @author Angela Maria Aguirre
 * @since 29/12/2010
 */
@SuppressWarnings("unchecked")
public class AutorizacionesIngresoEstanciaDelegate extends AutorizacionesIngreEstanciaHome{
	
	
	/**
	 * 
	 * Este Método se encarga de consultar las autorizaciones en estado autorizado o
	 * con indicativo temporal de un paciente determinado
	 * 
	 * @param DTOAdministracionAutorizacion dto
	 * @return ArrayList<DTOAdministracionAutorizacion>
	 * @author, Angela Maria Aguirre
	 *
	 */
	@SuppressWarnings("rawtypes")
	public ArrayList<DTOAdministracionAutorizacion> obtenerAutorizacionesPorPaciente(DTOAdministracionAutorizacion dto){
		
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(AutorizacionesIngreEstancia.class	,"autorizacionIngresoEstancia");		
		criteria.createAlias("autorizacionIngresoEstancia.ingresosEstancia"				,"ingresoEstancia");
		criteria.createAlias("ingresoEstancia.entidadesSubcontratadas"					,"entidadSubcontratada");
		criteria.createAlias("ingresoEstancia.pacientes"								,"paciente");	
		criteria.createAlias("paciente.personas"										,"persona");
		criteria.createAlias("persona.usuarioXConvenios"								,"usuarioConvenio");
		criteria.createAlias("usuarioConvenio.contratos"								,"contrato");
		criteria.createAlias("contrato.convenios"										,"convenio");
		//criteria.createAlias("autorizacionIngresoEstancia.histoAutorizacionIngEstans"	,"histoAutorizacionIngEstans", Criteria.LEFT_JOIN);
		//criteria.createAlias("autorizacionIngresoEstancia.autorizacionesEstanciaCapitas","autorizacionIngEstanciaCapitacion", Criteria.LEFT_JOIN);
		
		
		criteria.setProjection(Projections.distinct(Projections.projectionList()
				.add(Projections.property("autorizacionIngresoEstancia.codigoPk")						,"codigoPk")
				.add(Projections.property("autorizacionIngresoEstancia.consecutivoAdmision")			,"consecutivo")								
				.add(Projections.property("autorizacionIngresoEstancia.fechaInicioAutorizacion")		,"fechaAutorizacion")
				.add(Projections.property("autorizacionIngresoEstancia.diasEstanciaAutorizados")		,"diasEstancia")
				.add(Projections.property("autorizacionIngresoEstancia.indicativoTemporal")				,"indicativoTemporal")
				.add(Projections.property("entidadSubcontratada.razonSocial")							,"entidadSubcontratada")
				.add(Projections.property("convenio.nombre")											,"nombreConvenio")
				.add(Projections.property("ingresoEstancia.codigoPk")									,"codIngresoEstancia")
				.add(Projections.property("autorizacionIngresoEstancia.estado")							,"estado")
				//.add(Projections.property("histoAutorizacionIngEstans.fechaModifica")					,"fechaModifica")
				.add(Projections.property("ingresoEstancia.descripcionEntidadSub")						,"descripcionEntidadSubIngEst")
				.add(Projections.property("ingresoEstancia.fechaAdmision")                              ,"fechaAdmision")
				//.add(Projections.property("autorizacionIngEstanciaCapitacion.codigoPk")                 ,"idIngresoEstanciaCapitacion")
				));  
		
		criteria.add(Restrictions.eq("paciente.codigoPaciente", dto.getPaciente().getCodigo()));
		
		/**
		 * MT 1096 y 1097: Se remueve ya que no aplica ni para la administracion de autorizaciones 
		 * ni para las consultas 
		 * */
		/*criteria.add(Restrictions.le("usuarioConvenio.fechaInicial"	,UtilidadFecha.getFechaActualTipoBD()));
		criteria.add(Restrictions.ge("usuarioConvenio.fechaFinal"	,UtilidadFecha.getFechaActualTipoBD()));*/		
		
		if(dto.isAdministracionPoblacionCapitada())
		{
			criteria.add(Restrictions.eq("autorizacionIngresoEstancia.estado", ConstantesIntegridadDominio.acronimoAutorizado));
			
			String stringMesesMaxVencimiento= ValoresPorDefecto.getMesesMaxAdminAutoCapVencidas(dto.getCodigoInstitucion());
			
			if(stringMesesMaxVencimiento!=null&&!stringMesesMaxVencimiento.trim().isEmpty()){
				int mesesMaxVencimiento=Integer.parseInt(stringMesesMaxVencimiento);
				
				String sqlFechaVencimiento="({alias}.fecha_inicio_autorizacion  + {alias}.DIAS_ESTANCIA_AUTORIZADOS) >= ? ";
				
				Date fechaActual=UtilidadFecha.getFechaActualTipoBD();
				Calendar calendar=Calendar.getInstance();
				calendar.setTime(fechaActual);
				calendar.add(Calendar.MONTH, -mesesMaxVencimiento);
				
				Date nuevaFechaVen=calendar.getTime();
				
				if(mesesMaxVencimiento>0){
					criteria.add(Restrictions.sqlRestriction(sqlFechaVencimiento, new Object[]{nuevaFechaVen}, new Type[]{Hibernate.DATE}));
				}else{
					if(mesesMaxVencimiento==0){
						criteria.add(Restrictions.sqlRestriction(sqlFechaVencimiento, new Object[]{fechaActual}, new Type[]{Hibernate.DATE}));
					}
				}
			}
			
			//Se elimina debido a la version 1.6 del DCU 1096
			//criteria.add(Restrictions.eq("autorizacionIngresoEstancia.indicativoTemporal", ConstantesBD.acronimoSiChar));
		}
		
		if(dto.isExcluirRegistrosTemporales()){
			criteria.add(Restrictions.eq("autorizacionIngresoEstancia.indicativoTemporal", ConstantesBD.acronimoNoChar));
		}
		
		
		Class[] parametros=new Class[11];
		parametros[0]=long.class;
		parametros[1]=long.class;
		parametros[2]=Date.class;
		parametros[3]=int.class;
		parametros[4]=char.class;
		parametros[5]=String.class;
		parametros[6]=String.class;
		parametros[7]=long.class;
		parametros[8]=String.class;
		//parametros[9]=Date.class;
		parametros[9]=String.class;
		parametros[10]=Date.class;
		//parametros[12]=Long.class;
		
		Constructor constructor;
		try {
			constructor = DTOAdministracionAutorizacion.class.getConstructor(parametros);
			criteria.setResultTransformer(new AliasToBeanConstructorResultTransformer(constructor));
			
		} catch (SecurityException e) {
			Log4JManager.error("Error convirtiendo el resultado", e);
		} catch (NoSuchMethodException e) {
			Log4JManager.error("Error convirtiendo el resultado", e);
			e.printStackTrace();
		}	
		
		
		if(dto.isOrdenarDescendente()){
			criteria.addOrder(Order.desc("autorizacionIngresoEstancia.codigoPk"));
		}
		
		ArrayList<DTOAdministracionAutorizacion> listaAutorizaciones = (ArrayList<DTOAdministracionAutorizacion>)criteria.list();
		
		// Se Asigna la fecha de vencimiento obtenida por un calculo: Fecha inicio + dias autorizados
		for (DTOAdministracionAutorizacion dtoAdministracionAutorizacion : listaAutorizaciones) 
		{
			Date fechaVencimiento = UtilidadFecha.conversionFormatoFechaStringDate(
					UtilidadFecha.incrementarDiasAFecha(UtilidadFecha.conversionFormatoFechaAAp(
							dtoAdministracionAutorizacion.getFechaInicioAutorizacion()), dtoAdministracionAutorizacion.getDiasEstanciaAutorizados(), false));
			
			dtoAdministracionAutorizacion.setFechaVencimientoAutorizacion(fechaVencimiento);
		}
		
		return listaAutorizaciones;
		
	}	
	
	
	
	/**
	 * 
	 * Este Método se encarga de consultar las autorizaciones en estado autorizado o
	 * con indicativo temporal en un rango determinado
	 * 
	 * @param DTOBusquedaAutorizacionCapitacionRango dto
	 * @return ArrayList<DTOAdministracionAutorizacion>
	 * @author, Angela Maria Aguirre
	 *
	 */
	@SuppressWarnings("rawtypes")
	public ArrayList<DTOAdministracionAutorizacion> obtenerAutorizacionesPorRango(DTOBusquedaAutorizacionCapitacionRango dto){
		
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				AutorizacionesIngreEstancia.class, "autorizacionIngresoEstancia");		
		criteria.createAlias("autorizacionIngresoEstancia.ingresosEstancia","ingresoEstancia");
		criteria.createAlias("ingresoEstancia.entidadesSubcontratadas","entidadSubcontratada");
		criteria.createAlias("ingresoEstancia.pacientes","paciente");
		criteria.createAlias("paciente.personas","persona");
		criteria.createAlias("persona.tiposIdentificacion","tipoIdentificacion");		
		criteria.createAlias("persona.usuarioXConvenios","usuarioConvenio");
		criteria.createAlias("usuarioConvenio.contratos","contrato");
		criteria.createAlias("contrato.convenios","convenio");
		//criteria.createAlias("autorizacionIngresoEstancia.autorizacionesEstanciaCapitas","autorizacionIngEstanciaCapitacion", Criteria.LEFT_JOIN);
				
		criteria.setProjection(Projections.distinct(Projections.projectionList()
				.add(Projections.property("autorizacionIngresoEstancia.codigoPk"),"codigoPk")
				.add(Projections.property("autorizacionIngresoEstancia.consecutivoAdmision"),"consecutivo")								
				.add(Projections.property("autorizacionIngresoEstancia.fechaInicioAutorizacion"),"fechaAutorizacion")
				.add(Projections.property("autorizacionIngresoEstancia.diasEstanciaAutorizados"),"diasEstancia")
				.add(Projections.property("autorizacionIngresoEstancia.indicativoTemporal"),"indicativoTemporal")
				.add(Projections.property("entidadSubcontratada.razonSocial"),"entidadSubcontratada")
				.add(Projections.property("convenio.nombre"),"nombreConvenio")
				.add(Projections.property("persona.primerNombre"),"primerNombrePersona")
				.add(Projections.property("persona.segundoNombre"),"segundoNombrePersona")
				.add(Projections.property("persona.primerApellido"),"primerApellidoPersona")
				.add(Projections.property("persona.segundoApellido"),"segundoApellidoPersona")
				.add(Projections.property("persona.codigo"),"codigoPersona")
				.add(Projections.property("tipoIdentificacion.acronimo"),"tipoID")
				.add(Projections.property("persona.numeroIdentificacion"),"numeroIdentificacion")
				.add(Projections.property("autorizacionIngresoEstancia.estado"),"estado")
				.add(Projections.property("ingresoEstancia.descripcionEntidadSub"),"descripcionEntidadSubIngEst")
				//.add(Projections.property("autorizacionIngEstanciaCapitacion.codigoPk"),"idIngresoEstanciaCapitacion")
				));
				
		/**
		 * Se comenta ya que no aplica para las autorizaciones MT 1096 y 1097
		 * */
		/*if(!dto.isEsConsulta()){
		criteria.add(Restrictions.le("usuarioConvenio.fechaInicial",UtilidadFecha.getFechaActualTipoBD()));
		criteria.add(Restrictions.ge("usuarioConvenio.fechaFinal",UtilidadFecha.getFechaActualTipoBD()));
		}*/
						
		if(dto.getFechaIncioBusqueda()!=null && dto.getFechaFinBusqueda()!=null){
			criteria.add(Restrictions.between("autorizacionIngresoEstancia.fechaInicioAutorizacion", 
					dto.getFechaIncioBusqueda(), dto.getFechaFinBusqueda()));
			
		}else if(dto.getFechaIncioBusqueda()!=null){
			criteria.add(Restrictions.ge("autorizacionIngresoEstancia.fechaInicioAutorizacion", 
					dto.getFechaIncioBusqueda()));
		}else if(dto.getFechaFinBusqueda()!=null){
			criteria.add(Restrictions.le("autorizacionIngresoEstancia.fechaInicioAutorizacion", 
					dto.getFechaFinBusqueda()));
		} 
		
		if(dto.isAdministracioCapitacion()){

			String stringMesesMaxVencimiento= ValoresPorDefecto.getMesesMaxAdminAutoCapVencidas(dto.getCodigoInstitucion());
			
			if(stringMesesMaxVencimiento!=null&&!stringMesesMaxVencimiento.trim().isEmpty()){
				int mesesMaxVencimiento=Integer.parseInt(stringMesesMaxVencimiento);
				
				String sqlFechaVencimiento="({alias}.fecha_inicio_autorizacion  + {alias}.DIAS_ESTANCIA_AUTORIZADOS) >= ? ";
				
				Date fechaActual=UtilidadFecha.getFechaActualTipoBD();
				Calendar calendar=Calendar.getInstance();
				calendar.setTime(fechaActual);
				calendar.add(Calendar.MONTH, -mesesMaxVencimiento);
				
				Date nuevaFechaVen=calendar.getTime();
				
				if(mesesMaxVencimiento>0){
					criteria.add(Restrictions.sqlRestriction(sqlFechaVencimiento, new Object[]{nuevaFechaVen}, new Type[]{Hibernate.DATE}));
				}else{
					if(mesesMaxVencimiento==0){
						criteria.add(Restrictions.sqlRestriction(sqlFechaVencimiento, new Object[]{fechaActual}, new Type[]{Hibernate.DATE}));
					}
				}
			}
			
		}
		
		if(dto.isConsultableConsecutivoAutorizacion()){
			if(dto.getConsecutivoIncioAutorizacion()!=null && dto.getConsecutivoIncioAutorizacion()>=0){
				criteria.add(Restrictions.ge("autorizacionIngresoEstancia.consecutivoAdmision", 
						dto.getConsecutivoIncioAutorizacion()));
			}
			
			if(dto.getConsecutivoFinAutorizacion()!=null && dto.getConsecutivoFinAutorizacion()>=0){
				criteria.add(Restrictions.le("autorizacionIngresoEstancia.consecutivoAdmision", 
						dto.getConsecutivoFinAutorizacion()));
			}
		}
		
		if(dto.getCodigoEntidadSub()!= null && dto.getCodigoEntidadSub()!=ConstantesBD.codigoNuncaValidoLong){
			criteria.add(Restrictions.eq("entidadSubcontratada.codigoPk",dto.getCodigoEntidadSub()));
		}
		
		if(!UtilidadTexto.isEmpty(dto.getEstadoAutorizacion())){
			//Cuando se consulta desde la funcionalidad de Consultar Autorizaciones por Rango
			
			//Se elimina debido a la version 1.4 del DCU 1097
			if(dto.getEstadoAutorizacion().equals(ConstantesIntegridadDominio.acronimoTemporal)){
				
				criteria.add(Restrictions.eq("autorizacionIngresoEstancia.indicativoTemporal", ConstantesBD.acronimoSiChar));
			}
			else{
				criteria.add(Restrictions.eq("autorizacionIngresoEstancia.estado", dto.getEstadoAutorizacion()));
			}
			
		}else if(!dto.isEsConsulta()){
			//Validacion cuando se consulta desde la funcionalidad de Administrar Autorizaciones por Rango
			Disjunction disjunctionOR = Restrictions.disjunction();  
			disjunctionOR.add( Property.forName("autorizacionIngresoEstancia.estado").eq(ConstantesIntegridadDominio.acronimoAutorizado) );  
			disjunctionOR.add( Property.forName("autorizacionIngresoEstancia.indicativoTemporal").eq(ConstantesBD.acronimoSiChar) );
			criteria.add(disjunctionOR);
		}
			
							
		Class[] parametros=new Class[16];
		parametros[0]=long.class;
		parametros[1]=long.class;
		parametros[2]=Date.class;
		parametros[3]=int.class;
		parametros[4]=char.class;
		parametros[5]=String.class;
		parametros[6]=String.class;		
		parametros[7]=String.class;
		parametros[8]=String.class;
		parametros[9]=String.class;
		parametros[10]=String.class;
		parametros[11]=int.class;
		parametros[12]=String.class;
		parametros[13]=String.class;
		parametros[14]=String.class;
		parametros[15]=String.class;
		//parametros[16]=Long.class;
		
		Constructor constructor;
		try {
			constructor = DTOAdministracionAutorizacion.class.getConstructor(parametros);
			criteria.setResultTransformer(new AliasToBeanConstructorResultTransformer(constructor));
			
		} catch (SecurityException e) {
			Log4JManager.error("Error convirtiendo el resultado", e);
		} catch (NoSuchMethodException e) {
			Log4JManager.error("Error convirtiendo el resultado", e);
			e.printStackTrace();
		}			
		
		
		ArrayList<DTOAdministracionAutorizacion> listaAutorizaciones = (ArrayList<DTOAdministracionAutorizacion>)criteria.list();
		
		return listaAutorizaciones;
		
	}	
	
	/**
	 * 
	 * Este Método se encarga de consultar una autorización de ingreso estancia
	 * por su id
	 * 
	 * @param DTOAutorizacionIngresoEstancia dto
	 * @return DTOAutorizacionIngresoEstancia
	 * @author Angela Maria Aguirre
	 *
	 */
	@SuppressWarnings("rawtypes")
	public DTOAutorizacionIngresoEstancia consultarAutorizacionPorID(DTOAutorizacionIngresoEstancia dto){
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				AutorizacionesIngreEstancia.class, "autorizacionIngresoEstancia");		
		criteria.createAlias("autorizacionIngresoEstancia.ingresosEstancia","ingresoEstancia");
		criteria.createAlias("autorizacionIngresoEstancia.histoAutorizacionIngEstans","historial");
		criteria.createAlias("historial.usuarios","usuarioModifica");
		criteria.createAlias("usuarioModifica.personas","personaModifica");		
		criteria.createAlias("ingresoEstancia.tiposAfiliado","tipoAfiliado", Criteria.LEFT_JOIN);
		criteria.createAlias("ingresoEstancia.estratosSociales","estratoSocial", Criteria.LEFT_JOIN);
		criteria.createAlias("ingresoEstancia.viasIngreso","viaIngreso");
		criteria.createAlias("ingresoEstancia.entidadesSubcontratadas","entidadSubcontratada");
		criteria.createAlias("ingresoEstancia.pacientes","paciente");				
		criteria.createAlias("paciente.personas","persona");
		criteria.createAlias("persona.tiposIdentificacion","tipoIdentificacion");		
		criteria.createAlias("persona.usuarioXConvenios","usuarioConvenio");
		criteria.createAlias("usuarioConvenio.contratos","contrato");
		criteria.createAlias("contrato.convenios","convenio");
		criteria.createAlias("convenio.tiposContrato","tipoContrato");
		criteria.createAlias("ingresoEstancia.diagnosticosByFkIeDxCompli","dxComplicacionID", Criteria.LEFT_JOIN);		
		criteria.createAlias("ingresoEstancia.diagnosticosByFkIeDxPpal","dxPrincipalID");
		criteria.createAlias("ingresoEstancia.instituciones","entidadAutoriza");
		criteria.createAlias("autorizacionIngresoEstancia.convenios","convenioRecobro",Criteria.LEFT_JOIN);		
		
		
		
		criteria.add(Restrictions.eq("autorizacionIngresoEstancia.codigoPk", dto.getCodigoPk()));
		criteria.add(Restrictions.eq("historial.accionRealizada", ConstantesIntegridadDominio.acronimoAccionHistoricaInsertar));
		
		//criteria.add(Restrictions.eqProperty("convenio.codigo", "convenioRecobro.codigo"));asdfadf
		/*Date fechaInicial = UtilidadFecha.conversionFormatoFechaStringDate("01/01/2011");
		Date fechaFinal = UtilidadFecha.conversionFormatoFechaStringDate("31/01/2011");*/
		
		/**
		 * Se comenta ya que no aplica para las autorizaciones MT 1096 y 1097
		 * */
		/*criteria.add(Restrictions.le("usuarioConvenio.fechaInicial",UtilidadFecha.getFechaActualTipoBD()));
		criteria.add(Restrictions.ge("usuarioConvenio.fechaFinal",UtilidadFecha.getFechaActualTipoBD()));*/
		
		criteria.setProjection(Projections.distinct(Projections.projectionList()
				.add(Projections.property("autorizacionIngresoEstancia.consecutivoAdmision"),"consecutivo")								
				.add(Projections.property("autorizacionIngresoEstancia.fechaInicioAutorizacion"),"fechaAutorizacion")
				.add(Projections.property("autorizacionIngresoEstancia.diasEstanciaAutorizados"),"diasEstancia")
				.add(Projections.property("autorizacionIngresoEstancia.indicativoTemporal"),"indicativoTemporal")
				.add(Projections.property("autorizacionIngresoEstancia.estado"),"estado")
				.add(Projections.property("personaModifica.primerNombre"),"primerNombreUsuarioAutoriza")
			    .add(Projections.property("personaModifica.primerApellido"),"primerApellidoUsuarioAutoriza")
				.add(Projections.property("autorizacionIngresoEstancia.observaciones"),"observaciones")
				
				.add(Projections.property("entidadAutoriza.razonSocial"),"institucion")
				.add(Projections.property("ingresoEstancia.fechaAdmision"),"fechaAdmision")
				.add(Projections.property("ingresoEstancia.horaAdmision"),"horaAdmision")
				.add(Projections.property("dxPrincipalID.id.acronimo"),"acronimoDxPrincipal")
				.add(Projections.property("dxComplicacionID.id.acronimo"),"acronimoDxComplicacion")
				.add(Projections.property("ingresoEstancia.medicoSolicitante"),"medicoSolicitante")
				.add(Projections.property("ingresoEstancia.observaciones"),"observacionAdmision")
				.add(Projections.property("viaIngreso.nombre"),"viaIngreso")
				
				.add(Projections.property("entidadSubcontratada.razonSocial"),"entidadSubcontratada")
				
				.add(Projections.property("ingresoEstancia.descripcionEntidadSub"),"descripcionEntSub")
				
				.add(Projections.property("entidadSubcontratada.direccion"),"direccion")
				.add(Projections.property("entidadSubcontratada.telefono"),"telefono")
								
				.add(Projections.property("persona.primerNombre"),"primerNombrePersona")
				.add(Projections.property("persona.segundoNombre"),"segundoNombrePersona")
				.add(Projections.property("persona.primerApellido"),"primerApellidoPersona")
				.add(Projections.property("persona.segundoApellido"),"segundoApellidoPersona")
				.add(Projections.property("persona.codigo"),"codigoPersona")
				.add(Projections.property("tipoIdentificacion.acronimo"),"tipoID")
				.add(Projections.property("persona.numeroIdentificacion"),"numeroIdentificacion")
				
				.add(Projections.property("convenio.nombre"),"nombreConvenio")
				.add(Projections.property("tipoContrato.nombre"),"tipoContrato")
				.add(Projections.property("tipoAfiliado.nombre"),"tipoAfiliado")
				.add(Projections.property("estratoSocial.descripcion"),"estratoSocial")				
				.add(Projections.property("autorizacionIngresoEstancia.otroConvenioRecobro"),"entidadRecobrar")
				.add(Projections.property("autorizacionIngresoEstancia.usuarioContacta"),"usuarioContacta")
				.add(Projections.property("autorizacionIngresoEstancia.cargoUsuContacta"),"cargoUsuarioContacta")		
				.add(Projections.property("convenioRecobro.nombre"),"convenioRecobro")
				.add(Projections.property("persona.fechaNacimiento"),"fechaNacimiento")
				.add(Projections.property("dxComplicacionID.id.tipoCie"),"tipoCieComplicacion")
				.add(Projections.property("dxPrincipalID.id.tipoCie"),"tipoCiePrincipal")
				.add(Projections.property("dxPrincipalID.nombre"),"nombreDxPrincipal")
				.add(Projections.property("dxComplicacionID.nombre"),"nombreDxComplicacion")
				.add(Projections.property("viaIngreso.codigo"),"codigoViaIngreso")
				.add(Projections.property("ingresoEstancia.codigoPk"),"codigoIngresoEstancia")
				.add(Projections.property("ingresoEstancia.direccionEntidadSub"),"direccionEntidadOtra")
				.add(Projections.property("ingresoEstancia.telefonoEntidadSub"),"telefonoEntidadOtra")));
		
		Class[] parametros=new Class[44];
		parametros[0]=long.class;
		parametros[1]=Date.class;
		parametros[2]=int.class;
		parametros[3]=char.class;
		parametros[4]=String.class;		
		parametros[5]=String.class;		
		parametros[6]=String.class;		
		parametros[7]=String.class;
		parametros[8]=String.class;	
		parametros[9]=Date.class;
		parametros[10]=String.class;		
		parametros[11]=String.class;
		parametros[12]=String.class;
		parametros[13]=String.class;
		parametros[14]=String.class;
		parametros[15]=String.class;
		parametros[16]=String.class;
		parametros[17]=String.class;
		parametros[18]=String.class;
		parametros[19]=String.class;
		parametros[20]=String.class;
		parametros[21]=String.class;
		parametros[22]=String.class;		
		parametros[23]=String.class;
		
		parametros[24]=int.class;
		parametros[25]=String.class;
		parametros[26]=String.class;
		parametros[27]=String.class;
		parametros[28]=String.class;
		parametros[29]=String.class;
		parametros[30]=String.class;
		parametros[31]=String.class;
		parametros[32]=String.class;
		parametros[33]=String.class;
		parametros[34]=String.class;
		parametros[35]=Date.class;
		parametros[36]=Integer.class;
		parametros[37]=Integer.class;
		parametros[38]=String.class;
		parametros[39]=String.class;
		parametros[40]=Integer.class;
		parametros[41]=long.class;
		parametros[42]=String.class;
		parametros[43]=String.class;
		
		Constructor constructor;
		try {
			constructor = DTOAutorizacionIngresoEstancia.class.getConstructor(parametros);
			criteria.setResultTransformer(new AliasToBeanConstructorResultTransformer(constructor));
			
		} catch (SecurityException e) {
			Log4JManager.error("Error convirtiendo el resultado", e);
		} catch (NoSuchMethodException e) {
			Log4JManager.error("Error convirtiendo el resultado", e);
			e.printStackTrace();
		}
		
		DTOAutorizacionIngresoEstancia dtoAutorizacion = (DTOAutorizacionIngresoEstancia)criteria.uniqueResult();
		return dtoAutorizacion;
	}
	
	
	/**
	 * 
	 * Este Método se encarga de consultar una autorización de ingreso estancia
	 * cuando esta tiene una autorización de capitación asociada
	 * 
	 * @author, Angela Maria Aguirre
	 *
	 */
	public AutorizacionesIngreEstancia obtenerAutorizacionIngEstanciaCapitacion(DTOAutorizacionIngresoEstancia dto){
		
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				AutorizacionesIngreEstancia.class, "autorizacionIngresoEstancia");		
		criteria.createAlias("autorizacionIngresoEstancia.autorizacionesEstanciaCapitas","autorEstanciaCapita");
		criteria.createAlias("autorEstanciaCapita.autorizacionesCapitacionSub","autorizacionCapitacion");
				
		criteria.add(Restrictions.eq("autorizacionIngresoEstancia.codigoPk",dto.getCodigoPk()));
		AutorizacionesIngreEstancia autorizacion = (AutorizacionesIngreEstancia)criteria.uniqueResult();
		
		if(autorizacion!=null){
			//autorizacion.getAutorizacionesEstanciaCapita().getAutorizacionesCapitacionSub().getCodigoPk();
			try {
				autorizacion.getAutorizacionesEstanciaCapitas();
			} catch (Exception e) {
				Log4JManager.error("No tiene Autoriaciones de estancia capitadas");
			}
		
		}
		
		return autorizacion;
	}
	
	
	/*
	public static void main(String[] args){
		AutorizacionesIngresoEstanciaDelegate delegate = new  AutorizacionesIngresoEstanciaDelegate();
		DTOAutorizacionIngresoEstancia dto = new DTOAutorizacionIngresoEstancia();
		
		dto.setCodigoPk(1);
		
		delegate.consultarAutorizacionPorID(dto);
	}
	*/
	
	
	/**
	 * 
	 * Este Método se encarga de actualizar una autorización de ingreso estancia
	 * 
	 * @param AutorizacionesIngreEstancia autorizacion
	 * @return boolean
	 * @author, Angela Maria Aguirre
	 *
	 */
	public boolean actualizarAutorizacionIngresoEstancia(AutorizacionesIngreEstancia autorizacion){
		boolean save = false;
		try{
			super.merge(autorizacion);
			save = true;
		}catch (Exception e) {
			save = false;
			Log4JManager.error("No se pudo actualizar la autorización de ingreso estancia: ",e);
		}		
		return save;
	}
	
	
	
	/**
	 * attachDirty
	 * @param instance
	 */
	@Override
	public void attachDirty(AutorizacionesIngreEstancia instance) {
		super.attachDirty(instance);
	}

	
	/**
	 * findById
	 * @param id
	 */
	@Override
	public AutorizacionesIngreEstancia findById(long id) {
		return super.findById(id);
	}
	
	
	/**
	 * 
	 * Este Método se encarga de consultar las autorizaciones por entidad subcontratada
	 * 
	 * @param DcodigoPkEntidadSub
	 * @return ArrayList<DTOAdministracionAutorizacion>
	 * @author, Fabián Becerra
	 *
	 */
	public ArrayList<DTOAdministracionAutorizacion> obtenerAutorizacionesPorEntidadSub(long codigoPkEntidadSub){
		
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(AutorizacionesIngreEstancia.class	,"autorizacionIngresoEstancia");		
		criteria.createAlias("autorizacionIngresoEstancia.ingresosEstancia"				,"ingresoEstancia");
		criteria.createAlias("ingresoEstancia.viasIngreso"				    			,"viaIngreso");
		criteria.createAlias("ingresoEstancia.diagnosticosByFkIeDxPpal"				    ,"diagnosticos");
		criteria.createAlias("ingresoEstancia.entidadesSubcontratadas"					,"entidadSubcontratada");
		
		
		criteria.setProjection(Projections.distinct(Projections.projectionList()
				.add(Projections.property("autorizacionIngresoEstancia.codigoPk")						,"codigoPk")
				.add(Projections.property("autorizacionIngresoEstancia.consecutivoAdmision")			,"consecutivo")								
				.add(Projections.property("autorizacionIngresoEstancia.fechaInicioAutorizacion")		,"fechaAutorizacion")
				.add(Projections.property("autorizacionIngresoEstancia.diasEstanciaAutorizados")		,"diasEstancia")
				.add(Projections.property("autorizacionIngresoEstancia.indicativoTemporal")				,"indicativoTemporal")
				.add(Projections.property("entidadSubcontratada.razonSocial")							,"entidadSubcontratada")
				.add(Projections.property("ingresoEstancia.fechaAdmision")								,"fechaAdmision")
				.add(Projections.property("viaIngreso.codigo")											,"codigoViaIngreso")
				.add(Projections.property("diagnosticos.id.acronimo")									,"acronimoDiagnosticoPrinc")
				));  
		
		criteria.add(Restrictions.eq("entidadSubcontratada.codigoPk", codigoPkEntidadSub));
		
		criteria.setResultTransformer(Transformers.aliasToBean(DTOAdministracionAutorizacion.class));
		
		ArrayList<DTOAdministracionAutorizacion> listaAutorizaciones = (ArrayList<DTOAdministracionAutorizacion>)criteria.list();
		
		
		return listaAutorizaciones;
		
	}
	
	/** 
	 * Este Método se encarga de consultar las autorizaciones de Ingreso estancia 
	 * por paciente
	 * 
	 * @param DTOAdministracionAutorizacion dto
	 * @return ArrayList<DTOAdministracionAutorizacion>
	 * @author, Camilo Gomez
	 */
	@SuppressWarnings("rawtypes")
	public ArrayList<DTOAdministracionAutorizacion> obtenerAutorizacionesIngresoEstanciaPaciente(DTOAdministracionAutorizacion dto){
		
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(AutorizacionesIngreEstancia.class	,"autorizacionIngresoEstancia");		
		criteria.createAlias("autorizacionIngresoEstancia.ingresosEstancia"				,"ingresoEstancia");
		criteria.createAlias("ingresoEstancia.entidadesSubcontratadas"					,"entidadSubcontratada");
		criteria.createAlias("ingresoEstancia.pacientes"								,"paciente");	
		criteria.createAlias("paciente.personas"										,"persona");
		criteria.createAlias("persona.usuarioXConvenios"								,"usuarioConvenio");
		criteria.createAlias("usuarioConvenio.contratos"								,"contrato");
		criteria.createAlias("contrato.convenios"										,"convenio");
		criteria.createAlias("convenio.tiposContrato"									,"tipoContrato");
		criteria.createAlias("autorizacionIngresoEstancia.histoAutorizacionIngEstans"	,"histoAutorizacionIngEstans", Criteria.LEFT_JOIN);
		criteria.createAlias("autorizacionIngresoEstancia.centrosCosto"					,"centrosCosto"	, Criteria.LEFT_JOIN);
		criteria.createAlias("ingresoEstancia.tiposAfiliado"							,"tipoAfiliado"	, Criteria.LEFT_JOIN);
		criteria.createAlias("ingresoEstancia.estratosSociales"							,"estratoSocial", Criteria.LEFT_JOIN);
		
		criteria.setProjection(Projections.distinct(Projections.projectionList()
				.add(Projections.property("autorizacionIngresoEstancia.codigoPk")						,"codigoPk")
				.add(Projections.property("autorizacionIngresoEstancia.consecutivoAdmision")			,"consecutivo")								
				.add(Projections.property("autorizacionIngresoEstancia.fechaInicioAutorizacion")		,"fechaAutorizacion")
				.add(Projections.property("autorizacionIngresoEstancia.diasEstanciaAutorizados")		,"diasEstancia")
				.add(Projections.property("autorizacionIngresoEstancia.indicativoTemporal")				,"indicativoTemporal")
				.add(Projections.property("tipoAfiliado.nombre")										,"tipoAfiliado")
				.add(Projections.property("estratoSocial.descripcion")									,"clasificacionSE")
				.add(Projections.property("entidadSubcontratada.razonSocial")							,"entidadSubcontratada")
				.add(Projections.property("convenio.nombre")											,"nombreConvenio")
				.add(Projections.property("tipoContrato.nombre")										,"tipoContrato")
				.add(Projections.property("ingresoEstancia.codigoPk")									,"codIngresoEstancia")
				.add(Projections.property("autorizacionIngresoEstancia.estado")							,"estado")
				.add(Projections.property("histoAutorizacionIngEstans.fechaModifica")					,"fechaModifica")
				.add(Projections.property("ingresoEstancia.descripcionEntidadSub")						,"descripcionEntidadSubIngEst")
				.add(Projections.property("ingresoEstancia.fechaAdmision")                              ,"fechaAdmision")
				.add(Projections.property("centrosCosto.codigo")                						,"codigoCentroCosto")
				.add(Projections.property("centrosCosto.nombre")                						,"nombreCentroCosto")
				.add(Projections.property("centrosCosto.tipoEntidadEjecuta")       						,"tipoEntidadEjecuta")
				));  
		
		criteria.add(Restrictions.eq("paciente.codigoPaciente", dto.getPaciente().getCodigo()));
		
		/**
		 * Se comenta ya que no aplica para las autorizaciones MT 1096 y 1097
		 * */
		/*criteria.add(Restrictions.le("usuarioConvenio.fechaInicial"	,UtilidadFecha.getFechaActualTipoBD()));
		criteria.add(Restrictions.ge("usuarioConvenio.fechaFinal"	,UtilidadFecha.getFechaActualTipoBD()));*/		
				
		if(dto.isExcluirRegistrosTemporales()){
			criteria.add(Restrictions.eq("autorizacionIngresoEstancia.indicativoTemporal", ConstantesBD.acronimoNoChar));
		}
		
		
		Class[] parametros=new Class[18];
		parametros[0]=long.class;
		parametros[1]=long.class;
		parametros[2]=Date.class;
		parametros[3]=int.class;
		parametros[4]=char.class;
		parametros[5]=String.class;//
		parametros[6]=String.class;//		
		parametros[7]=String.class;
		parametros[8]=String.class;
		parametros[9]=String.class;//
		parametros[10]=long.class;
		parametros[11]=String.class;
		parametros[12]=Date.class;
		parametros[13]=String.class;
		parametros[14]=Date.class;
		parametros[15]=int.class;//
		parametros[16]=String.class;//
		parametros[17]=String.class;//
		
		Constructor constructor;
		try {
			constructor = DTOAdministracionAutorizacion.class.getConstructor(parametros);
			criteria.setResultTransformer(new AliasToBeanConstructorResultTransformer(constructor));
			
		} catch (SecurityException e) {
			Log4JManager.error("Error convirtiendo el resultado", e);
		} catch (NoSuchMethodException e) {
			Log4JManager.error("Error convirtiendo el resultado", e);
			e.printStackTrace();
		}	
		
		if(dto.isOrdenarDescendente()){
			criteria.addOrder(Order.desc("autorizacionIngresoEstancia.codigoPk"));
		}
		
		ArrayList<DTOAdministracionAutorizacion> listaAutorizaciones = (ArrayList<DTOAdministracionAutorizacion>)criteria.list();
		
		// Se Asigna la fecha de vencimiento obtenida por un calculo: Fecha inicio + dias autorizados
		for (DTOAdministracionAutorizacion dtoAdministracionAutorizacion : listaAutorizaciones) 
		{
			Date fechaVencimiento = UtilidadFecha.conversionFormatoFechaStringDate(
					UtilidadFecha.incrementarDiasAFecha(UtilidadFecha.conversionFormatoFechaAAp(
							dtoAdministracionAutorizacion.getFechaInicioAutorizacion()), dtoAdministracionAutorizacion.getDiasEstanciaAutorizados(), false));
			
			dtoAdministracionAutorizacion.setFechaVencimientoAutorizacion(fechaVencimiento);
		}
		
		return listaAutorizaciones;
	}
		
}
