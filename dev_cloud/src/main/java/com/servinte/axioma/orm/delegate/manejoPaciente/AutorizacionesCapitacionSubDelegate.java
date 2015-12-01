package com.servinte.axioma.orm.delegate.manejoPaciente;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.axioma.util.log.Log4JManager;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.AliasToBeanConstructorResultTransformer;
import org.hibernate.type.StandardBasicTypes;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.ValoresPorDefecto;
import util.manejoPaciente.ConstantesBDManejoPaciente;

import com.princetonsa.dto.capitacion.DtoConsultaProcesoAutorizacion;
import com.princetonsa.dto.capitacion.DtoProcesoPresupuestoCapitado;
import com.princetonsa.dto.manejoPaciente.DTOAdministracionAutorizacion;
import com.princetonsa.dto.manejoPaciente.DTOBusquedaAutorizacionCapitacionRango;
import com.servinte.axioma.orm.AutorizacionesCapitacionSub;
import com.servinte.axioma.orm.AutorizacionesCapitacionSubHome;

/**
 * Esta clase se encarga de ejecutar las consultas sobre 
 * la entidad  AutorizacionesCapitacionSub
 * 
 * @author Angela Maria Aguirre
 * @since 10/12/2010
 */
public class AutorizacionesCapitacionSubDelegate extends AutorizacionesCapitacionSubHome {
	
	
	/**
	 * 
	 * Este Método se encarga de insertar en la base de datos
	 * un registro de autorización de capitación subcontratada
	 * 
	 * @param AutorizacionesCapitacionSub autorizacion
	 * @return boolean
	 * @author, Angela Maria Aguirre
	 *
	 */
	public boolean guardarAutorizacionCapitacioncontratada(AutorizacionesCapitacionSub autorizacion){
		boolean save = true;					
		try{
			super.persist(autorizacion);
		} catch (Exception e) {
			save = false;
			Log4JManager.error("No se pudo guardar el registro de " +
					"autorización de capitación subcontratada: ",e);
		}				
		return save;				
	}
	
	
	
	/**
	 * 
	 * Implementación del método attachDirty de la super clase  AutorizacionesCapitacionSubHome
	 * 
	 * @param AutorizacionesCapitacionSub autorizacion
	 * @return boolean
	 * @author, Angela Maria Aguirre, Cristhian Murillo
	 *
	 */
	public boolean sincronizarAutorizacionCapitacioncontratada(AutorizacionesCapitacionSub autorizacion){
		/**
		 * Se elimina la captura de la excepción en este nivel, ya que no se manejan excepciones por cada transacción
		 * de esta manera se deja la captura de la excepción en el mundo.
		 *  
		 */
		boolean save = true;					
		super.attachDirty(autorizacion);
		return save;				
	}
	
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
	@SuppressWarnings({ "static-access", "rawtypes", "unchecked" })
	public ArrayList<DTOAdministracionAutorizacion> obtenerAutorizacionesPorPaciente(DTOAdministracionAutorizacion dto){
		
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(AutorizacionesCapitacionSub.class, "autorizacionCapitacion");		
		criteria.createAlias("autorizacionCapitacion.autorizacionesEntidadesSub"		,"autorizacionEntidadSub");
		criteria.createAlias("autorizacionEntidadSub.pacientes"							,"paciente");
		criteria.createAlias("paciente.personas"										,"persona");
		criteria.createAlias("autorizacionEntidadSub.entidadesSubcontratadas"			,"entidadSubcontratada");	
		//MT:6010 se elimina el join de la tabla usuarioXConvenios que me arrojaba el convenio, ya que el convenio se puede traer de la tabla autorizacionesEntidadesSub
		//criteria.createAlias("persona.usuarioXConvenios"								,"usuarioConvenio");
		//criteria.createAlias("usuarioConvenio.contratos"								,"contrato");
		criteria.createAlias("autorizacionesEntidadesSub.convenios"						,"convenio");
		criteria.createAlias("autorizacionEntidadSub.autorizacionesEntSubArticus"		,"autorizArticulo",criteria.LEFT_JOIN);
		criteria.createAlias("autorizacionEntidadSub.autorizacionesEntSubServis"		,"autorizServicio",criteria.LEFT_JOIN);
		criteria.createAlias("autorizacionCapitacion.autorizacionesEstanciaCapitas"		,"autorizacionIngEstanciaCapitacion", Criteria.LEFT_JOIN);
		criteria.createAlias("autorizacionEntidadSub.entregaAutorizacions"				, "entregaAutorizacion", criteria.LEFT_JOIN);
		
			
		criteria.add(Restrictions.eq("paciente.codigoPaciente", dto.getPaciente().getCodigo()));
		
		/**
		 * MT 1096 y 1097: Se remueve ya que no aplica ni para la administracion de autorizaciones 
		 * ni para las consultas 
		 * */
		/*if(!dto.getEstado().equals("consultarPorPaciente"))			
			{			
				criteria.add(Restrictions.le("usuarioConvenio.fechaInicial",UtilidadFecha.getFechaActualTipoBD()));
				criteria.add(Restrictions.ge("usuarioConvenio.fechaFinal",UtilidadFecha.getFechaActualTipoBD()));
			}				*/
		
		if(dto.isAdministracionPoblacionCapitada()){
			criteria.add(Restrictions.eq("autorizacionEntidadSub.estado", ConstantesIntegridadDominio.acronimoAutorizado));
			
			String stringMesesMaxVencimiento= ValoresPorDefecto.getMesesMaxAdminAutoCapVencidas(dto.getCodigoInstitucion());
			
			if(stringMesesMaxVencimiento!=null&&!stringMesesMaxVencimiento.trim().isEmpty()){
				int mesesMaxVencimiento=Integer.parseInt(stringMesesMaxVencimiento);
				
				Date fechaActual=UtilidadFecha.getFechaActualTipoBD();
				Calendar calendar=Calendar.getInstance();
				calendar.setTime(fechaActual);
				calendar.add(Calendar.MONTH, -mesesMaxVencimiento);
				
				Date nuevaFechaVen=calendar.getTime();
				
				if(mesesMaxVencimiento>0){
					criteria.add(Restrictions.ge("autorizacionEntidadSub.fechaVencimiento", nuevaFechaVen));
				}else{
					if(mesesMaxVencimiento==0){
						criteria.add(Restrictions.ge("autorizacionEntidadSub.fechaVencimiento", fechaActual));
					}
				}
			}
		}
		
		criteria.setProjection(Projections.distinct(Projections.projectionList()
				.add(Projections.groupProperty("autorizacionCapitacion.codigoPk"),"codigoPk")
				.add(Projections.groupProperty("autorizacionCapitacion.consecutivo"),"consecutivo")
				.add(Projections.groupProperty("autorizacionCapitacion.fechaAutoriza"),"fechaAutorizacion")
				.add(Projections.groupProperty("autorizacionEntidadSub.fechaVencimiento"),"fechaVencimiento")
				.add(Projections.groupProperty("autorizacionCapitacion.indicativoTemporal"),"indicativoTemporal")
				.add(Projections.groupProperty("entidadSubcontratada.razonSocial"),"entidadSubcontratada")
				.add(Projections.groupProperty("convenio.nombre"),"nombreConvenio")
				.add(Projections.groupProperty("autorizacionEntidadSub.estado"),"estado")
				//Dato tipoAutorizacion Servicios o articulos
				.add(Projections.countDistinct("autorizArticulo.codigoPk"),"codArticulo")
				.add(Projections.countDistinct("autorizServicio.codigoPk"),"codServicio")
				.add(Projections.groupProperty("autorizacionCapitacion.descripcionEntidad"),"descripcionEntidadSubIngEst")
				.add(Projections.groupProperty("autorizacionIngEstanciaCapitacion.codigoPk"),"idIngresoEstanciaCapitacion")
				.add(Projections.groupProperty("autorizacionEntidadSub.consecutivo"), "codigoPkEntSub")
				.add(Projections.groupProperty("autorizacionEntidadSub.consecutivoAutorizacion"), "consecutivoEntSub")
				.add(Projections.groupProperty("autorizacionEntidadSub.fechaAutorizacion"),"fechaAutorizacionEntSub")
				
				.add(Projections.groupProperty("entregaAutorizacion.id"),"idAutorizacionEntregada")
		));						
		
		Class[] parametros=new Class[16];
		parametros[0]=long.class;
		parametros[1]=long.class;
		parametros[2]=Date.class;
		parametros[3]=Date.class;
		parametros[4]=char.class;
		parametros[5]=String.class;
		parametros[6]=String.class;
		parametros[7]=String.class;
		parametros[8]=long.class;
		parametros[9]=long.class;
		parametros[10]=String.class;
		parametros[11]=Long.class;
		parametros[12]=long.class;
		parametros[13]=String.class;
		parametros[14]=Date.class;
		parametros[15]=Integer.class;
		
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
	 * Este Método se encarga de consultar las autorizaciones en estado autorizado o
	 * con indicativo temporal en un rango determinado
	 * 
	 * @param DTOAdministracionAutorizacion dto
	 * @return ArrayList<DTOAdministracionAutorizacion>
	 * @author, Angela Maria Aguirre
	 *
	 */
	@SuppressWarnings({ "static-access", "rawtypes", "unchecked" })
	public ArrayList<DTOAdministracionAutorizacion> obtenerAutorizacionesPorRango(DTOBusquedaAutorizacionCapitacionRango dto){
		
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				AutorizacionesCapitacionSub.class, "autorizacionCapitacion");		
		
		criteria.createAlias("autorizacionCapitacion.autorizacionesEntidadesSub","autorizacionEntidadSub");
		
		criteria.createAlias("autorizacionEntidadSub.pacientes","paciente");
		criteria.createAlias("paciente.personas","persona");
		criteria.createAlias("persona.tiposIdentificacion","tipoIdentificacion");
		criteria.createAlias("autorizacionEntidadSub.entidadesSubcontratadas","entidadSubcontratada");
		criteria.createAlias("persona.usuarioXConvenios","usuarioConvenio");
		criteria.createAlias("usuarioConvenio.contratos","contrato");
		criteria.createAlias("contrato.convenios","convenio");
				
		if(dto.getCodigoTipoAutorizacion() == null || dto.getCodigoTipoAutorizacion().equals("")){
			criteria.createAlias("autorizacionCapitacion.autorizacionesEstanciaCapitas","autorizacionIngEstanciaCapitacion", Criteria.LEFT_JOIN);
			criteria.createAlias("autorizacionEntidadSub.autorizacionesEntSubArticus","autorizArticulo",criteria.LEFT_JOIN);
			criteria.createAlias("autorizacionEntidadSub.autorizacionesEntSubServis","autorizServicio",criteria.LEFT_JOIN);
		}
		else if(dto.getCodigoTipoAutorizacion().equals(ConstantesBDManejoPaciente.codigoTipoAutorizacionServicio)){
			criteria.createAlias("autorizacionCapitacion.autorizacionesEstanciaCapitas","autorizacionIngEstanciaCapitacion", Criteria.LEFT_JOIN);
			criteria.createAlias("autorizacionEntidadSub.autorizacionesEntSubServis","autorizServicio");
			criteria.createAlias("autorizacionEntidadSub.autorizacionesEntSubArticus","autorizArticulo",criteria.LEFT_JOIN);
		}
		else if(dto.getCodigoTipoAutorizacion().equals(ConstantesBDManejoPaciente.codigoTipoAutorizacionMedIns)){
			criteria.createAlias("autorizacionCapitacion.autorizacionesEstanciaCapitas","autorizacionIngEstanciaCapitacion", Criteria.LEFT_JOIN);
			criteria.createAlias("autorizacionEntidadSub.autorizacionesEntSubArticus","autorizArticulo");
			criteria.createAlias("autorizacionEntidadSub.autorizacionesEntSubServis","autorizServicio",criteria.LEFT_JOIN);
		}
		else if(dto.getCodigoTipoAutorizacion().equals(ConstantesBDManejoPaciente.codigoTipoAutorizacionServicioIngre)){
			criteria.createAlias("autorizacionCapitacion.autorizacionesEstanciaCapitas","autorizacionIngEstanciaCapitacion");
			criteria.createAlias("autorizacionEntidadSub.autorizacionesEntSubServis","autorizServicio");
			criteria.createAlias("autorizacionEntidadSub.autorizacionesEntSubArticus","autorizArticulo",criteria.LEFT_JOIN);
		}
		else if(dto.getCodigoTipoAutorizacion().equals(ConstantesBDManejoPaciente.codigoTipoAutorizacionMedInsIngre)){
			criteria.createAlias("autorizacionCapitacion.autorizacionesEstanciaCapitas","autorizacionIngEstanciaCapitacion");
			criteria.createAlias("autorizacionEntidadSub.autorizacionesEntSubArticus","autorizArticulo");
			criteria.createAlias("autorizacionEntidadSub.autorizacionesEntSubServis","autorizServicio",criteria.LEFT_JOIN);
		}
		
		criteria.createAlias("autorizacionEntidadSub.entregaAutorizacions", "entregaAutorizacion", criteria.LEFT_JOIN);
		
		criteria.setProjection(Projections.distinct(Projections.projectionList()
				.add(Projections.groupProperty("autorizacionCapitacion.codigoPk"),"codigoPk")
				.add(Projections.groupProperty("autorizacionCapitacion.consecutivo"),"consecutivo")
				.add(Projections.groupProperty("autorizacionCapitacion.fechaAutoriza"),"fechaAutorizacion")
				.add(Projections.groupProperty("autorizacionEntidadSub.fechaVencimiento"),"fechaVencimiento")
				.add(Projections.groupProperty("autorizacionCapitacion.indicativoTemporal"),"indicativoTemporal")
				.add(Projections.groupProperty("entidadSubcontratada.razonSocial"),"entidadSubcontratada")				
				.add(Projections.groupProperty("convenio.nombre"),"nombreConvenio")
				.add(Projections.groupProperty("persona.primerNombre"),"primerNombrePersona")
				.add(Projections.groupProperty("persona.segundoNombre"),"segundoNombrePersona")
				.add(Projections.groupProperty("persona.primerApellido"),"primerApellidoPersona")
				.add(Projections.groupProperty("persona.segundoApellido"),"segundoApellidoPersona")
				.add(Projections.groupProperty("persona.codigo"),"codigoPersona")
				.add(Projections.groupProperty("tipoIdentificacion.acronimo"),"tipoID")
				.add(Projections.groupProperty("persona.numeroIdentificacion"),"numeroIdentificacion")
				.add(Projections.groupProperty("autorizacionEntidadSub.estado"),"estado")
				//Dato tipoAutorizacion Servicios o articulos
				.add(Projections.countDistinct("autorizArticulo.codigoPk"),"codArticulo")
				.add(Projections.countDistinct("autorizServicio.codigoPk"),"codServicio")
				.add(Projections.groupProperty("autorizacionCapitacion.descripcionEntidad"),"descripcionEntidadSubIngEst")
				.add(Projections.groupProperty("autorizacionIngEstanciaCapitacion.codigoPk"),"idIngresoEstanciaCapitacion")
				.add(Projections.groupProperty("autorizacionEntidadSub.consecutivo"), "codigoPkEntSub")
				.add(Projections.groupProperty("autorizacionEntidadSub.consecutivoAutorizacion"), "consecutivoEntSub")
				.add(Projections.groupProperty("autorizacionEntidadSub.fechaAutorizacion"),"fechaAutorizacionEntSub")
						
				.add(Projections.groupProperty("entregaAutorizacion.id"),"idAutorizacionEntregada")
			));/////
						
		/**
		 * Se comenta ya que no aplica para las autorizaciones MT 1096 y 1097
		 * */
		/*if (!dto.isEsConsulta()){
			criteria.add(Restrictions.le("usuarioConvenio.fechaInicial",UtilidadFecha.getFechaActualTipoBD()));
			criteria.add(Restrictions.ge("usuarioConvenio.fechaFinal",UtilidadFecha.getFechaActualTipoBD()));		
		}*/
		
		if(dto.getFechaIncioBusqueda()!=null && dto.getFechaFinBusqueda()!=null){
			criteria.add(Restrictions.between("autorizacionCapitacion.fechaAutoriza", 
					dto.getFechaIncioBusqueda(), dto.getFechaFinBusqueda()));
			
		}else if(dto.getFechaIncioBusqueda()!=null){
			criteria.add(Restrictions.ge("autorizacionCapitacion.fechaAutoriza", 
					dto.getFechaIncioBusqueda()));
		}else if(dto.getFechaFinBusqueda()!=null){
			criteria.add(Restrictions.le("autorizacionCapitacion.fechaAutoriza", 
					dto.getFechaFinBusqueda()));
		} 
		
		if(dto.isConsultableConsecutivoAutorizacion()){
			if(dto.getTipoConsecutivoAutorizacion().equals(""+ConstantesBDManejoPaciente.codigoTipoConsecutivoAutorizacionCapitacion)){
				if(dto.getConsecutivoIncioAutorizacion()!=null && dto.getConsecutivoIncioAutorizacion()>=0){
					criteria.add(Restrictions.ge("autorizacionCapitacion.consecutivo", 
							dto.getConsecutivoIncioAutorizacion()));
				}
				
				if(dto.getConsecutivoFinAutorizacion()!=null && dto.getConsecutivoFinAutorizacion()>=0){
					criteria.add(Restrictions.le("autorizacionCapitacion.consecutivo",
							dto.getConsecutivoFinAutorizacion()));
				}
			}else{
				if(dto.getTipoConsecutivoAutorizacion().equals(""+ConstantesBDManejoPaciente.codigoTipoConsecutivoAutorizacionEntidadSub)){
					
					criteria.add(Restrictions.isNotNull("autorizacionEntidadSub.consecutivoAutorizacion"));
						
					if(dto.getConsecutivoIncioAutorizacion()!=null && dto.getConsecutivoIncioAutorizacion()>=0){
						//MT6070 se convierte el consecutivo de la autorización que se ingreso a String 
						criteria.add(Restrictions.ge("autorizacionEntidadSub.consecutivoAutorizacion", 
								String.valueOf(dto.getConsecutivoIncioAutorizacion())));
					/*	criteria.add(Restrictions.sqlRestriction("to_number({alias}.consecutivo_autorizacion,'99999999999999') >= ? ",
								dto.getConsecutivoIncioAutorizacion(),Hibernate.LONG));*/
					}
					
					if(dto.getConsecutivoFinAutorizacion()!=null && dto.getConsecutivoFinAutorizacion()>=0){
						//MT6070 se convierte el consecutivo de la autorización que se ingreso a String 
						criteria.add(Restrictions.le("autorizacionEntidadSub.consecutivoAutorizacion",
								String.valueOf(dto.getConsecutivoFinAutorizacion())));
						/*criteria.add(Restrictions.sqlRestriction("to_number({alias}.consecutivo_autorizacion,'99999999999999') <= ? ",
								dto.getConsecutivoFinAutorizacion(),Hibernate.LONG));*/
					}
				}
			}
		}
		

		if(dto.isAdministracioCapitacion()){
			String stringMesesMaxVencimiento= ValoresPorDefecto.getMesesMaxAdminAutoCapVencidas(dto.getCodigoInstitucion());
			
			if(stringMesesMaxVencimiento!=null&&!stringMesesMaxVencimiento.trim().isEmpty()){
				int mesesMaxVencimiento=Integer.parseInt(stringMesesMaxVencimiento);
				
				Date fechaActual=UtilidadFecha.getFechaActualTipoBD();
				Calendar calendar=Calendar.getInstance();
				calendar.setTime(fechaActual);
				calendar.add(Calendar.MONTH, -mesesMaxVencimiento);
				
				Date nuevaFechaVen=calendar.getTime();
				
				if(mesesMaxVencimiento>0){
					criteria.add(Restrictions.ge("autorizacionEntidadSub.fechaVencimiento", nuevaFechaVen));
				}else{
					if(mesesMaxVencimiento==0){
						criteria.add(Restrictions.ge("autorizacionEntidadSub.fechaVencimiento", fechaActual));
					}
				}
			}
		}
		if(dto.getCodigoEntidadSub()!= null && dto.getCodigoEntidadSub()!=ConstantesBD.codigoNuncaValidoLong){
			criteria.add(Restrictions.eq("entidadSubcontratada.codigoPk",dto.getCodigoEntidadSub()));
		}	
			
		if(!UtilidadTexto.isEmpty(dto.getEstadoAutorizacion())){
			//Cuando se consulta desde la funcionalidad de Consultar Autorizaciones por Rango
			
			//se agrego este if debido al estado temporal
			if(dto.getEstadoAutorizacion().equals(ConstantesIntegridadDominio.acronimoTemporal)){
				criteria.add(Restrictions.eq("autorizacionCapitacion.indicativoTemporal", ConstantesBD.acronimoSiChar));
			}else{
				criteria.add(Restrictions.eq("autorizacionEntidadSub.estado", dto.getEstadoAutorizacion()));
			}
		}else if(!dto.isEsConsulta()){
			//Validacion cuando se consulta desde la funcionalidad de Administrar Autorizaciones por Rango
			Disjunction disjunctionOR = Restrictions.disjunction();  
			disjunctionOR.add( Property.forName("autorizacionEntidadSub.estado").eq(ConstantesIntegridadDominio.acronimoAutorizado) );  
			disjunctionOR.add( Property.forName("autorizacionCapitacion.indicativoTemporal").eq(ConstantesBD.acronimoSiChar) );
			criteria.add(disjunctionOR);
		}
		
		Class[] parametros=new Class[23];
		parametros[0]=long.class;
		parametros[1]=long.class;
		parametros[2]=Date.class;
		parametros[3]=Date.class;
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
		parametros[15]=long.class;
		parametros[16]=long.class;
		parametros[17]=String.class;
		parametros[18]=Long.class;
		parametros[19]=long.class;
		parametros[20]=String.class;
		parametros[21]=Date.class;
		parametros[22]=Integer.class;
		
		
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
	 * Este Método se encarga de consultar las autorizaciones de capitacion generadas para cada 
	 * convenio-contrato por dia en un rango de fechas determinado de Articulos y Servicios
	 * que contienen ---INCONSISTENCIAS--- de:
	 * 
	 * Articulos (ValorTarifa o NivelAtencion)
	 * Servicios (valorTarifa)
	 * Proceso Autorizaciones 1027 Cierre
	 * 
	 * @param DTOProcesoPresupuestoCapitado dto
	 * @return ArrayList<DtoConsultaProcesoAutorizacion>
	 * @author, Camilo Gómez 
	 */
	@SuppressWarnings({ "unchecked"})
	public ArrayList<DtoConsultaProcesoAutorizacion> obtenerAutorizacionesCapitaServiArtiInconsistentes(DtoProcesoPresupuestoCapitado dto){
		
		String consulta = 	"SELECT new com.princetonsa.dto.capitacion.DtoConsultaProcesoAutorizacion( "+
								"conva.codigo, " +
								"convs.codigo, " +
								"conta.codigo, " +
								"conts.codigo, " +
								"aes.fechaAutorizacion, " +
								"arti.codigo, " +
								"arti.descripcion, " +
								"aesa.valorTarifa, " +
								"naa.consecutivo, " +
								"servi.codigo, " +
								"rs.descripcion, " +
								"aess.valorTarifa " +
								") " +
							"FROM AutorizacionesCapitacionSub acs " +
								"INNER JOIN acs.autorizacionesEntidadesSub aes " +
								"LEFT JOIN aes.autorizacionesEntSubServis aess " +
								"LEFT JOIN aess.servicios servi " +
								"LEFT JOIN servi.nivelAtencion nas " +
								"LEFT JOIN servi.referenciasServicios rs " +
								"LEFT JOIN rs.tarifariosOficiales tipoTarifa " +
								"LEFT JOIN aes.autorizacionesEntSubArticus aesa " +
								"LEFT JOIN aesa.articulo arti " +
								"LEFT JOIN arti.nivelAtencion naa " +
								"LEFT JOIN aesa.contratos conta " +
								"LEFT JOIN aess.contratos conts " +
								"LEFT JOIN conta.convenios conva " +
								"LEFT JOIN conts.convenios convs " +
							"WHERE (conva.tiposContrato = :tipoContrato OR convs.tiposContrato = :tipoContrato) " +
								"AND acs.indicativoTemporal = :indicativoTemporal " +
								"AND (conva.capitacionSubcontratada = :capitacionSub OR convs.capitacionSubcontratada = :capitacionSub) "+ 
								"AND aes.fechaAutorizacion BETWEEN :fechaInicio AND :fechaFin " +
								"AND (aess.valorTarifa IS NULL OR " +
								"aesa.valorTarifa IS NULL OR " +
								"naa.consecutivo IS NULL ) "
								;
								
								if(dto.getConvenio()!=null)
								{	consulta = consulta +"AND (convs = :convenio OR conva = :convenio) ";
									if(dto.getContrato()!=null)
										consulta = consulta +"AND (conta = :contrato OR conts = :contrato) ";
								}
								
								consulta=consulta+"AND (tipoTarifa.codigo IS NULL OR tipoTarifa.codigo = :tipoTarifario) " +
								"GROUP BY conva.codigo, convs.codigo, conta.codigo, conts.codigo, aes.fechaAutorizacion, arti.codigo, arti.descripcion,aesa.valorTarifa, naa.consecutivo, servi.codigo, rs.descripcion, aess.valorTarifa " +
								"ORDER BY aes.fechaAutorizacion "
							;
								
		Query query =sessionFactory.getCurrentSession().createQuery(consulta);		
		Integer tipoTarifario = Integer.parseInt(ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(dto.getInstitucion()));
		
		query.setParameter("tipoContrato", ConstantesBD.codigoTipoContratoCapitado, StandardBasicTypes.INTEGER);
		query.setParameter("indicativoTemporal", ConstantesBD.acronimoNoChar, StandardBasicTypes.CHARACTER);
		query.setParameter("capitacionSub", ConstantesBD.acronimoSiChar, StandardBasicTypes.CHARACTER);
		query.setParameter("tipoTarifario", tipoTarifario, StandardBasicTypes.INTEGER);
		query.setParameter("fechaInicio", dto.getFechaInicio(), StandardBasicTypes.DATE);
		query.setParameter("fechaFin", dto.getFechaFin(), StandardBasicTypes.DATE);
		
		if(dto.getConvenio()!=null)
		{	query.setParameter("convenio",dto.getConvenio(), StandardBasicTypes.INTEGER);
			if(dto.getContrato()!=null)
				query.setParameter("contrato",dto.getContrato(), StandardBasicTypes.INTEGER);
		}
		
		Log4JManager.info(	"\n -------------------------------------------------------------------------\n" +
						  	"PARAMETROS DE LA CONSULTA --INCONSISTENCIAS--(Proceso Autorizaciones Cierre)" +
						  	"\n -------------------------------------------------------------------------\n" +
						  	"TipoContrato:	" +ConstantesBD.codigoTipoContratoCapitado +"\n"+
						  	"IndicativoTemporal:	"+ ConstantesBD.acronimoNoChar +"\n"+
						  	"CapitacionSubcontratada:	"+ ConstantesBD.acronimoSiChar +"\n"+
						  	"FechaInicio:	"+dto.getFechaInicio()+"\n"+
						  	"FechaFin:	"+dto.getFechaFin()+"\n"+
						  	"TipoTarifario:	"+tipoTarifario+"\n"+
						  	"Convenio:	"+dto.getConvenio()+"\n"+
						  	"Contrato:	"+dto.getContrato());
		
		ArrayList<DtoConsultaProcesoAutorizacion> listaAutorizaciones = (ArrayList<DtoConsultaProcesoAutorizacion>)query.list();
		return listaAutorizaciones;
		
	}
	
	/**
	 * Este Método se encarga de consultar las autorizaciones de capitacion generadas para cada 
	 * convenio-contrato por dia en un rango de fechas determinado para -SERVICIOS-
	 * Para Proceso Autorizaciones 1027 Cierre
	 * 
	 * @param DTOProcesoPresupuestoCapitado dto
	 * @return ArrayList<DtoConsultaProcesoAutorizacion>
	 * @author, Camilo Gómez 
	 */
	@SuppressWarnings({ "unchecked"})
	public ArrayList<DtoConsultaProcesoAutorizacion> obtenerAutorizacionesCapitacionServicios(DtoProcesoPresupuestoCapitado dto){
		
		
		String consulta = 	"SELECT new com.princetonsa.dto.capitacion.DtoConsultaProcesoAutorizacion( "+
								"convs.codigo, " +
								"conts.codigo, " +
								"aes.fechaAutorizacion, " +
								"aes.fechaAnulacion, " +
								"servi.codigo, " +
								"rs.descripcion, " +
								"nas.consecutivo, " +
								"SUM(aess.cantidad), " +
								"SUM(aess.valorTarifa), " +
								"'false' " +
								") " +
							"FROM AutorizacionesCapitacionSub acs " +
								"INNER JOIN acs.autorizacionesEntidadesSub aes " +
								"INNER JOIN aes.autorizacionesEntSubServis aess " +
								"INNER JOIN aess.servicios servi " +
								"INNER JOIN servi.nivelAtencion nas " +
								"INNER JOIN servi.referenciasServicios rs " +
								"INNER JOIN rs.tarifariosOficiales tipoTarifa " +
								"INNER JOIN aess.contratos conts " +
								"INNER JOIN conts.convenios convs " +
							"WHERE (convs.tiposContrato = :tipoContrato) " +
								"AND acs.indicativoTemporal = :indicativoTemporal " +
								"AND (convs.capitacionSubcontratada = :capitacionSub) "+ 
								"AND aes.fechaAutorizacion BETWEEN :fechaInicio AND :fechaFin " +
								"AND aes.estado = :estadoAutorizacion " +
								"AND aess.valorTarifa IS NOT NULL ";
		
								if(dto.getConvenio()!=null)
								{	consulta = consulta +"AND convs = :convenio ";
									if(dto.getContrato()!=null)
										consulta = consulta +"AND conts = :contrato ";
								}
								consulta=consulta+"AND (tipoTarifa.codigo IS NULL OR tipoTarifa.codigo = :tipoTarifario) " +
								"GROUP BY convs.codigo, conts.codigo, aes.fechaAutorizacion, aes.fechaAnulacion, servi.codigo, rs.descripcion, nas.consecutivo, 10 "+
								"ORDER BY aes.fechaAutorizacion ";
		
		Query query =sessionFactory.getCurrentSession().createQuery(consulta);		
		Integer tipoTarifario = Integer.parseInt(ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(dto.getInstitucion()));
		
		query.setParameter("tipoContrato", ConstantesBD.codigoTipoContratoCapitado, StandardBasicTypes.INTEGER);
		query.setParameter("indicativoTemporal", ConstantesBD.acronimoNoChar, StandardBasicTypes.CHARACTER);
		query.setParameter("capitacionSub", ConstantesBD.acronimoSiChar, StandardBasicTypes.CHARACTER);
		query.setParameter("tipoTarifario", tipoTarifario, StandardBasicTypes.INTEGER);
		query.setParameter("fechaInicio", dto.getFechaInicio(), StandardBasicTypes.DATE);
		query.setParameter("fechaFin", dto.getFechaFin(), StandardBasicTypes.DATE);
		query.setParameter("estadoAutorizacion",dto.getEstadoAutorizacion(), StandardBasicTypes.STRING);
		
		if(dto.getConvenio()!=null)
		{	query.setParameter("convenio",dto.getConvenio(), StandardBasicTypes.INTEGER);
			if(dto.getContrato()!=null)
				query.setParameter("contrato",dto.getContrato(), StandardBasicTypes.INTEGER);
		}		
		
		Log4JManager.info(	"\n -------------------------------------------------------------------------------------\n" +
						  	"PARAMETROS DE LA CONSULTA -SERVICIOS AUTORIZADOS AGRUPADOS-(Proceso Autorizaciones Cierre)" +
						  	"\n -------------------------------------------------------------------------------------\n" +
						  	"TipoContrato:	" +ConstantesBD.codigoTipoContratoCapitado +"\n"+
						  	"IndicativoTemporal:	"+ ConstantesBD.acronimoNoChar +"\n"+
						  	"CapitacionSubcontratada:	"+ ConstantesBD.acronimoSiChar +"\n"+
						  	"TipoTarifario:	"+tipoTarifario+"\n"+
						  	"FechaInicio:	"+dto.getFechaInicio()+"\n"+
						  	"FechaFin:	"+dto.getFechaFin()+"\n"+
						  	"Convenio:	"+dto.getConvenio()+"\n"+
						  	"Contrato:	"+dto.getContrato()+"\n"+
						  	"EstadoAutorizacion:	"+dto.getEstadoAutorizacion());
		
		ArrayList<DtoConsultaProcesoAutorizacion> listaServicios= (ArrayList<DtoConsultaProcesoAutorizacion>)query.list();
		return listaServicios;
		
	}
		
	/**
	 * Este Método se encarga de consultar las autorizaciones de capitacion generadas para cada 
	 * convenio-contrato por dia en un rango de fechas determinado para -ARTICULOS-
	 * Para Proceso Autorizaciones 1027 Cierre
	 * 
	 * @param DTOProcesoPresupuestoCapitado dto
	 * @return ArrayList<DtoConsultaProcesoAutorizacion>
	 * @author, Camilo Gómez 
	 */
	@SuppressWarnings({ "unchecked"})
	public ArrayList<DtoConsultaProcesoAutorizacion> obtenerAutorizacionesCapitacionArticulos(DtoProcesoPresupuestoCapitado dto){
		
		
		String consulta = 	"SELECT new com.princetonsa.dto.capitacion.DtoConsultaProcesoAutorizacion( "+
								"conva.codigo, " +
								"conta.codigo, " +
								"aes.fechaAutorizacion, " +
								"aes.fechaAnulacion, " +
								"arti.codigo, " +
								"arti.descripcion, " +
								"naa.consecutivo, " +
								"SUM(aesa.nroDosisTotal), " +
								//hermorhu - MT6778
								"SUM(aesa.valorTarifa * aesa.nroDosisTotal), " +
								
								"'true'" +
								") " +
							"FROM AutorizacionesCapitacionSub acs " +
								"INNER JOIN acs.autorizacionesEntidadesSub aes " +
								"INNER JOIN aes.autorizacionesEntSubArticus aesa " +
								"INNER JOIN aesa.articulo arti " +
								"LEFT JOIN arti.nivelAtencion naa " +
								"INNER JOIN aesa.contratos conta " +
								"INNER JOIN conta.convenios conva " +
							"WHERE (conva.tiposContrato = :tipoContrato) " +
								"AND acs.indicativoTemporal = :indicativoTemporal " +
								"AND (conva.capitacionSubcontratada = :capitacionSub) " + 
								"AND aes.fechaAutorizacion BETWEEN :fechaInicio AND :fechaFin "+
								"AND aes.estado = :estadoAutorizacion " +
								"AND aesa.valorTarifa IS NOT NULL " +
								"AND naa.consecutivo IS NOT NULL ";
		
		if(dto.getConvenio()!=null)
								{	consulta = consulta +"AND conva = :convenio ";
									if(dto.getContrato()!=null)
										consulta = consulta +"AND conta = :contrato ";
								}
								consulta=consulta+"GROUP BY conva.codigo, conta.codigo, aes.fechaAutorizacion, aes.fechaAnulacion, arti.codigo, arti.descripcion, naa.consecutivo,  10 "+
								"ORDER BY aes.fechaAutorizacion ";
								
		Query query =sessionFactory.getCurrentSession().createQuery(consulta);
		query.setParameter("tipoContrato", ConstantesBD.codigoTipoContratoCapitado, StandardBasicTypes.INTEGER);
		query.setParameter("indicativoTemporal", ConstantesBD.acronimoNoChar, StandardBasicTypes.CHARACTER);
		query.setParameter("capitacionSub", ConstantesBD.acronimoSiChar, StandardBasicTypes.CHARACTER);
		query.setParameter("fechaInicio", dto.getFechaInicio(), StandardBasicTypes.DATE);
		query.setParameter("fechaFin", dto.getFechaFin(), StandardBasicTypes.DATE);
		query.setParameter("estadoAutorizacion",dto.getEstadoAutorizacion(), StandardBasicTypes.STRING);
			
		if(dto.getConvenio()!=null)
		{	query.setParameter("convenio",dto.getConvenio(), StandardBasicTypes.INTEGER);
			if(dto.getContrato()!=null)
				query.setParameter("contrato",dto.getContrato(), StandardBasicTypes.INTEGER);
			}
		
		Log4JManager.info(	"\n -------------------------------------------------------------------------------------\n" +
						  	"PARAMETROS DE LA CONSULTA -ARTICULOS AUTORIZADOS AGRUPADOS-(Proceso Autorizaciones Cierre)" +
						  	"\n -------------------------------------------------------------------------------------\n" +
						  	"TipoContrato:	" +ConstantesBD.codigoTipoContratoCapitado +"\n"+
						  	"IndicativoTemporal:	"+ ConstantesBD.acronimoNoChar +"\n"+
						  	"CapitacionSubcontratada:	"+ ConstantesBD.acronimoSiChar +"\n"+
						  	"FechaInicio:	"+dto.getFechaInicio()+"\n"+
						  	"FechaFin:	"+dto.getFechaFin()+"\n"+
						  	"Convenio:	"+dto.getConvenio()+"\n"+
						  	"Contrato:	"+dto.getContrato()+"\n"+
						  	"EstadoAutorizacion:	"+dto.getEstadoAutorizacion());
		
		ArrayList<DtoConsultaProcesoAutorizacion> listaArticulos = (ArrayList<DtoConsultaProcesoAutorizacion>)query.list();
		return listaArticulos;
		
		}
		
		
	/**
	 * Este Método se encarga de consultar las autorizaciones de capitacion -ANULADAS- generadas para cada 
	 * convenio-contrato por dia en un rango de fechas determinado para el -SERVICIO- especifico
	 * Para Proceso Autorizaciones 1027 Cierre
	 * 
	 * @param DTOProcesoPresupuestoCapitado dto
	 * @return ArrayList<DtoConsultaProcesoAutorizacion>
	 * @author, Camilo Gómez 
	 */
	@SuppressWarnings({ "unchecked"})
	public ArrayList<DtoConsultaProcesoAutorizacion> obtenerAutorizacionesCapitacionServicioAnulada(DtoProcesoPresupuestoCapitado dto){
		
		String consulta = 	"SELECT new com.princetonsa.dto.capitacion.DtoConsultaProcesoAutorizacion( "+
								"aes.fechaAnulacion, " +
								"SUM(aess.cantidad), " +
								"SUM(aess.valorTarifa), " +
								"'false' " +
								") " +
							"FROM AutorizacionesCapitacionSub acs " +
								"INNER JOIN acs.autorizacionesEntidadesSub aes " +
								"INNER JOIN aes.autorizacionesEntSubServis aess " +
								"INNER JOIN aess.servicios servi " +
								"INNER JOIN servi.referenciasServicios rs " +
								"INNER JOIN rs.tarifariosOficiales tipoTarifa " +
								"INNER JOIN aess.contratos conts " +
								"INNER JOIN conts.convenios convs " +
							"WHERE (convs.tiposContrato = :tipoContrato) " +
								"AND acs.indicativoTemporal = :indicativoTemporal " +
								"AND (convs.capitacionSubcontratada = :capitacionSub) "+ 
								"AND aes.fechaAutorizacion BETWEEN :fechaInicio AND :fechaFin " +
								"AND aes.estado = :estadoAutorizacion " +
								"AND servi = :codigoServicio " +
								"AND convs = :convenio " +
								"AND conts = :contrato " +
								"AND (tipoTarifa.codigo IS NULL OR tipoTarifa.codigo = :tipoTarifario) " +
								"GROUP BY aes.fechaAnulacion, 4";
								
		Query query =sessionFactory.getCurrentSession().createQuery(consulta);		
		Integer tipoTarifario = Integer.parseInt(ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(dto.getInstitucion()));
		
		query.setParameter("tipoContrato", ConstantesBD.codigoTipoContratoCapitado, StandardBasicTypes.INTEGER);
		query.setParameter("indicativoTemporal", ConstantesBD.acronimoNoChar, StandardBasicTypes.CHARACTER);
		query.setParameter("capitacionSub", ConstantesBD.acronimoSiChar, StandardBasicTypes.CHARACTER);
		query.setParameter("tipoTarifario", tipoTarifario, StandardBasicTypes.INTEGER);
		query.setParameter("fechaInicio", dto.getFechaInicio(), StandardBasicTypes.DATE);
		query.setParameter("fechaFin", dto.getFechaFin(), StandardBasicTypes.DATE);
		query.setParameter("convenio",dto.getConvenio(), StandardBasicTypes.INTEGER);
		query.setParameter("contrato",dto.getContrato(), StandardBasicTypes.INTEGER);
		query.setParameter("codigoServicio",dto.getCodServicio(), StandardBasicTypes.INTEGER);
		query.setParameter("estadoAutorizacion",dto.getEstadoAutorizacion(), StandardBasicTypes.STRING);
		
		Log4JManager.info(	"\n ----------------------------------------------------------------------------------\n" +
						  	"PARAMETROS DE LA CONSULTA -SERVICIOS ANULADOS AGRUPADOS-(Proceso Autorizaciones Cierre)" +
						  	"\n ----------------------------------------------------------------------------------\n" +
						  	"TipoContrato:	" +ConstantesBD.codigoTipoContratoCapitado +"\n"+
						  	"IndicativoTemporal:	"+ ConstantesBD.acronimoNoChar +"\n"+
						  	"CapitacionSubcontratada:	"+ ConstantesBD.acronimoSiChar +"\n"+
						  	"TipoTarifario:	"+tipoTarifario+"\n"+
						  	"FechaInicio:	"+dto.getFechaInicio()+"\n"+
						  	"FechaFin:	"+dto.getFechaFin()+"\n"+
						  	"Convenio:	"+dto.getConvenio()+"\n"+
						  	"Contrato:	"+dto.getContrato()+"\n"+
						  	"CodigoServicio:	"+dto.getCodServicio()+"\n"+
						  	"EstadoAutorizacion:	"+dto.getEstadoAutorizacion());
		
		ArrayList<DtoConsultaProcesoAutorizacion> listaServiciosAnulados= (ArrayList<DtoConsultaProcesoAutorizacion>)query.list();
		return listaServiciosAnulados;
		
	}
		
	/**
	 * Este Método se encarga de consultar las autorizaciones de capitacion -ANULADAS- generadas para cada 
	 * convenio-contrato por dia en un rango de fechas determinado para cada -ARTICULO- especifico
	 * Para Proceso Autorizaciones 1027 Cierre
	 * 
	 * @param DTOProcesoPresupuestoCapitado dto
	 * @return ArrayList<DtoConsultaProcesoAutorizacion>
	 * @author, Camilo Gómez 
	 */
	@SuppressWarnings({ "unchecked"})
	public ArrayList<DtoConsultaProcesoAutorizacion> obtenerAutorizacionesCapitacionArticuloAnulada(DtoProcesoPresupuestoCapitado dto){

		String consulta = 	"SELECT new com.princetonsa.dto.capitacion.DtoConsultaProcesoAutorizacion( "+
								"aes.fechaAnulacion, " +
								"SUM(aesa.nroDosisTotal), " +
								"SUM(aesa.valorTarifa), " +
								"'true' " +
								") " +
							"FROM AutorizacionesCapitacionSub acs " +
								"INNER JOIN acs.autorizacionesEntidadesSub aes " +
								"INNER JOIN aes.autorizacionesEntSubArticus aesa " +
								"INNER JOIN aesa.articulo arti " +
								"INNER JOIN aesa.contratos conta " +
								"INNER JOIN conta.convenios conva " +
							"WHERE (conva.tiposContrato = :tipoContrato) " +
								"AND acs.indicativoTemporal = :indicativoTemporal " +
								"AND (conva.capitacionSubcontratada = :capitacionSub) " + 
								"AND aes.fechaAutorizacion BETWEEN :fechaInicio AND :fechaFin " +
								"AND aes.estado = :estadoAutorizacion "+								
								"AND arti  = :codigoArticulo "+
								"AND conva = :convenio " +
								"AND conta = :contrato " +
								"GROUP BY aes.fechaAnulacion, 4 ";
		
		Query query =sessionFactory.getCurrentSession().createQuery(consulta);
		query.setParameter("tipoContrato", ConstantesBD.codigoTipoContratoCapitado, StandardBasicTypes.INTEGER);
		query.setParameter("indicativoTemporal", ConstantesBD.acronimoNoChar, StandardBasicTypes.CHARACTER);
		query.setParameter("capitacionSub", ConstantesBD.acronimoSiChar, StandardBasicTypes.CHARACTER);
		query.setParameter("fechaInicio", dto.getFechaInicio(), StandardBasicTypes.DATE);
		query.setParameter("fechaFin", dto.getFechaFin(), StandardBasicTypes.DATE);
		query.setParameter("codigoArticulo", dto.getCodArticulo(), StandardBasicTypes.INTEGER);
		query.setParameter("convenio",dto.getConvenio(), StandardBasicTypes.INTEGER);
		query.setParameter("contrato",dto.getContrato(), StandardBasicTypes.INTEGER);
		query.setParameter("estadoAutorizacion",dto.getEstadoAutorizacion(), StandardBasicTypes.STRING);
		
		Log4JManager.info(	"\n ----------------------------------------------------------------------------------\n" +
						  	"PARAMETROS DE LA CONSULTA -ARTICULOS ANULADOS AGRUPADOS-(Proceso Autorizaciones Cierre)" +
						  	"\n ----------------------------------------------------------------------------------\n" +
						  	"TipoContrato:	" +ConstantesBD.codigoTipoContratoCapitado +"\n"+
						  	"IndicativoTemporal:	"+ ConstantesBD.acronimoNoChar +"\n"+
						  	"CapitacionSubcontratada:	"+ ConstantesBD.acronimoSiChar +"\n"+
						  	"FechaInicio:	"+dto.getFechaInicio()+"\n"+
						  	"FechaFin:	"+dto.getFechaFin()+"\n"+
						  	"Convenio:	"+dto.getConvenio()+"\n"+
						  	"Contrato:	"+dto.getContrato()+"\n"+
						  	"CodigoArticulo:	"+dto.getCodArticulo()+"\n"+
						  	"EstadoAutorizacion:	"+dto.getEstadoAutorizacion());
		
		ArrayList<DtoConsultaProcesoAutorizacion> listaArticulosAnuladas = (ArrayList<DtoConsultaProcesoAutorizacion>)query.list();
		return listaArticulosAnuladas;

	}
	
	
	/**
	 * 
	 * Este Método se encarga de actualizar el detalle de una autorización de capitación
	 * 
	 * @param AutorizacionesCapitacionSub
	 * @return boolean
	 * @author, Angela Maria Aguirre
	 *
	 */
	public boolean actualizarAutorizacionCapitacion(AutorizacionesCapitacionSub autorizacion){
		boolean save = false;
		try{
			super.attachDirty(autorizacion);
			save = true;
		}catch (Exception e) {
			save = false;
			Log4JManager.error("No se pudo actualizar el registro de autorizacion de capitacion: ",e);
		}		
		return save;
	}

	
	//MT6715 se crea metodo para que realice la suma de las autorizaciones
public ArrayList<DtoConsultaProcesoAutorizacion> obtenerAutorizacionesCapitacionServiciosEstado(DtoProcesoPresupuestoCapitado dto){
		
		
		String consulta = 	"SELECT new com.princetonsa.dto.capitacion.DtoConsultaProcesoAutorizacion( "+
							
								" SUM(aess.cantidad) , " +
								" SUM(aess.valorTarifa) " +
								
								") " +
							"FROM AutorizacionesCapitacionSub acs " +
								"INNER JOIN acs.autorizacionesEntidadesSub aes " +
								"INNER JOIN aes.autorizacionesEntSubServis aess " +
								"INNER JOIN aess.servicios servi " +
								"INNER JOIN servi.nivelAtencion nas " +
								"INNER JOIN servi.referenciasServicios rs " +
								"INNER JOIN rs.tarifariosOficiales tipoTarifa " +
								"INNER JOIN aess.contratos conts " +
								"INNER JOIN conts.convenios convs " +
							"WHERE (convs.tiposContrato = :tipoContrato) " +
								"AND acs.indicativoTemporal = :indicativoTemporal " +
								"AND (convs.capitacionSubcontratada = :capitacionSub) "+ 
								"AND aes.fechaAutorizacion BETWEEN :fechaInicio AND :fechaFin " +
								"AND aess.valorTarifa IS NOT NULL ";
		
								if(dto.getConvenio()!=null)
								{	consulta = consulta +"AND convs = :convenio ";
									if(dto.getContrato()!=null)
										consulta = consulta +"AND conts = :contrato ";
								}
								consulta=consulta+"AND (tipoTarifa.codigo IS NULL OR tipoTarifa.codigo = :tipoTarifario) " 
								//"GROUP BY convs.codigo, conts.codigo, aes.fechaAutorizacion, aes.fechaAnulacion, servi.codigo, rs.descripcion, nas.consecutivo, 10 "+
								/*"ORDER BY aes.fechaAutorizacion "*/;
		
		Query query =sessionFactory.getCurrentSession().createQuery(consulta);		
		Integer tipoTarifario = Integer.parseInt(ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(dto.getInstitucion()));
		
		query.setParameter("tipoContrato", ConstantesBD.codigoTipoContratoCapitado, StandardBasicTypes.INTEGER);
		query.setParameter("indicativoTemporal", ConstantesBD.acronimoNoChar, StandardBasicTypes.CHARACTER);
		query.setParameter("capitacionSub", ConstantesBD.acronimoSiChar, StandardBasicTypes.CHARACTER);
		query.setParameter("tipoTarifario", tipoTarifario, StandardBasicTypes.INTEGER);
		query.setParameter("fechaInicio", dto.getFechaInicio(), StandardBasicTypes.DATE);
		query.setParameter("fechaFin", dto.getFechaFin(), StandardBasicTypes.DATE);

		
		if(dto.getConvenio()!=null)
		{	query.setParameter("convenio",dto.getConvenio(), StandardBasicTypes.INTEGER);
			if(dto.getContrato()!=null)
				query.setParameter("contrato",dto.getContrato(), StandardBasicTypes.INTEGER);
		}		
		
		Log4JManager.info(	"\n -------------------------------------------------------------------------------------\n" +
						  	"PARAMETROS DE LA CONSULTA -SERVICIOS AUTORIZADOS AGRUPADOS-(Proceso Autorizaciones Cierre)" +
						  	"\n -------------------------------------------------------------------------------------\n" +
						  	"TipoContrato:	" +ConstantesBD.codigoTipoContratoCapitado +"\n"+
						  	"IndicativoTemporal:	"+ ConstantesBD.acronimoNoChar +"\n"+
						  	"CapitacionSubcontratada:	"+ ConstantesBD.acronimoSiChar +"\n"+
						  	"TipoTarifario:	"+tipoTarifario+"\n"+
						  	"FechaInicio:	"+dto.getFechaInicio()+"\n"+
						  	"FechaFin:	"+dto.getFechaFin()+"\n"+
						  	"Convenio:	"+dto.getConvenio()+"\n"+
						  	"Contrato:	"+dto.getContrato()+"\n"
						  	);
		
		ArrayList<DtoConsultaProcesoAutorizacion> listaServicios= (ArrayList<DtoConsultaProcesoAutorizacion>)query.list();
		return listaServicios;
		
	}
		

}
