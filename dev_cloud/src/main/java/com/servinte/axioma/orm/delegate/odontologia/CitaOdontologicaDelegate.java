package com.servinte.axioma.orm.delegate.odontologia;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.axioma.util.log.Log4JManager;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;

import com.princetonsa.dto.odontologia.DtoFiltroReporteCambioServCitaOdonto;
import com.princetonsa.dto.odontologia.DtoFiltroReporteTiempoEsperaAtencionCitasOdontologicas;
import com.princetonsa.dto.odontologia.DtoIngresosOdontologicos;
import com.princetonsa.dto.odontologia.DtoReporteCambioServicioOrdenarServiciosFinales;
import com.princetonsa.dto.odontologia.DtoReporteCambioServiciosListaEspecialidadesMedico;
import com.princetonsa.dto.odontologia.DtoReporteCambioServiciosOrdenarServicios;
import com.princetonsa.dto.odontologia.DtoReporteIngresosOdontologicos;
import com.princetonsa.dto.odontologia.DtoResultadoConsultaCambioServiciosOdontologicos;
import com.princetonsa.dto.odontologia.DtoResultadoConsultaReporteTiempoEsperaAtencionCitasOdonto;
import com.servinte.axioma.orm.CitasOdontologicas;
import com.servinte.axioma.orm.CitasOdontologicasHome;
import com.servinte.axioma.orm.EspecialidadesMedicos;
import com.servinte.axioma.orm.ServiciosCitaOdontologica;
import com.servinte.axioma.orm.SolicitudCambioServicio;
import com.servinte.axioma.orm.ValoracionesOdonto;


@SuppressWarnings("unchecked")
public class CitaOdontologicaDelegate extends CitasOdontologicasHome {
	
	/**
	 * Este m&eacute;todo se encarga de consultar las citas odontol&oacute;gicas
	 * que no tienen una valoraci&oacute;n inicial.
	 * @param ingresos
	 * @return
	 * 
	 * @author Yennifer Guerrero
	 * @param codigoPaciente 
	 */
	public ArrayList<DtoIngresosOdontologicos> obtenerCitaOdontoSinValIni(List<Integer> ingresos, List<Integer> codigoPaciente){
		
		List <Integer> valoraciones = obtenerPacientesConValoraciones();
		
		Criteria criteria = sessionFactory.getCurrentSession()
		.createCriteria(CitasOdontologicas.class, "citasOdonto")
		.createAlias("citasOdonto.pacientes", "pacientes")
		.createAlias("pacientes.ingresoses", "ingreso")
		.createAlias("ingreso.centroAtencion", "centroAtencion")
		.createAlias("centroAtencion.ciudades", "ciudades")
		.createAlias("ciudades.paises", "paises")
		.createAlias("centroAtencion.regionesCobertura", "regionesCobertura")
		.createAlias("centroAtencion.empresasInstitucion", "empresaInstitucion")
		.createAlias("pacientes.personas", "personas")
		.createAlias("personas.tiposIdentificacion", "tipoId")
		.createAlias("personas.sexo", "sexo", Criteria.LEFT_JOIN)
		.createAlias("centroAtencion.instituciones", "institucion");
	
		criteria.setProjection(Projections.distinct(Projections.projectionList()
				.add(Projections.property("ingreso.id"), "idIngreso")
				.add( Projections.property("centroAtencion.consecutivo"), "consecutivoCentroAtencion" )
				.add( Projections.property("centroAtencion.descripcion"), "descripcionCentroAtencion" )
				.add( Projections.property("institucion.codigo"), "codigoInstitucion" )
				.add( Projections.property("empresaInstitucion.razonSocial"), "descripcionEmpresaInstitucion" )
				.add(Projections.property("paises.descripcion"),"descripcionPais")
				.add( Projections.property("ciudades.descripcion")	, "descripcionCiudad" )
				.add( Projections.property("regionesCobertura.descripcion"), "descripcionRegionCobertura" )
				
				.add( Projections.property("personas.codigo"), "codigoPaciente")
				.add( Projections.property("tipoId.acronimo"), "tipoId")
				.add( Projections.property("personas.numeroIdentificacion"), "numeroId")
				.add( Projections.property("personas.primerNombre"), "primerNombre")
				.add( Projections.property("personas.segundoNombre"), "segundoNombre")
				.add( Projections.property("personas.primerApellido"), "primerApellido")
				.add( Projections.property("personas.segundoApellido"), "segundoApellido")
				.add( Projections.property("personas.telefonoFijo"), "telefonoFijo")
				.add( Projections.property("personas.telefonoCelular"), "telefonoCelular")
				.add( Projections.property("personas.telefono"), "telefono")
				.add( Projections.property("personas.fechaNacimiento"), "fechaNacimiento")
				.add( Projections.property("sexo.nombre"), "sexoPaciente")
				
				
				));
		
		if (codigoPaciente != null && codigoPaciente.size()>0) {
			valoraciones.addAll(codigoPaciente);
		}
		
		criteria.add(Restrictions.not(Restrictions.in("personas.codigo", valoraciones)));
		
		
		criteria.add(Restrictions.in("ingreso.id", ingresos));
		//criteria.add(Restrictions.eq("citasOdonto.tipo", ConstantesIntegridadDominio.acronimoTipoCitaOdonValoracionInicial))
		
		criteria.setResultTransformer( Transformers.aliasToBean(DtoIngresosOdontologicos.class));
		
		criteria.addOrder( Order.asc("tipoId.acronimo") );
		criteria.addOrder( Order.asc("personas.numeroIdentificacion") );
		ArrayList<DtoIngresosOdontologicos> listadoIngresos=(ArrayList)criteria.list();
		
		return listadoIngresos;
		
	}
	
	/**
	 * Este m&eacute;todo se encarga de 
	 * @return
	 *
	 * @author Yennifer Guerrero
	 */
	private List<Integer> obtenerPacientesConValoraciones() {
		
		Criteria criteria = sessionFactory.getCurrentSession()
		.createCriteria(ValoracionesOdonto.class, "valoracion")
		.createAlias("valoracion.citasOdontologicas", "citasOdo")
		.createAlias("citasOdo.pacientes", "pacientes")
		 .setProjection( Property.forName("pacientes.codigoPaciente")); 
		return criteria.list();
	}

	/**
	 * Este m&eacute;todo se encarga de consultar las citas odontol&oacute;gicas
	 * que tienen una valoraci&oacute;n inicial pero que su estado es diferente 
	 * a atendida.
	 * @return
	 * 
	 * @author Yennifer Guerrero
	 * @param codigoPaciente 
	 */
	public ArrayList<DtoIngresosOdontologicos> obtenercitaOdontoValIniNoAtendida(List<Integer> ingresos){
		
	 Criteria criteria = sessionFactory.getCurrentSession().createCriteria(CitasOdontologicas.class,"citaOdonto")
		.createAlias("citaOdonto.valoracionesOdontos", "valoracion",Criteria.LEFT_JOIN)
		.createAlias("citaOdonto.pacientes", "pacientes")
		.createAlias("pacientes.ingresoses", "ingreso")
		.createAlias("ingreso.centroAtencion", "centroAtencion")
		.createAlias("centroAtencion.ciudades", "ciudades")
		.createAlias("ciudades.paises", "paises")
		.createAlias("centroAtencion.regionesCobertura", "regionesCobertura")
		.createAlias("centroAtencion.empresasInstitucion", "empresaInstitucion")
		.createAlias("pacientes.personas", "personas")
		.createAlias("personas.tiposIdentificacion", "tipoId")
		.createAlias("personas.sexo", "sexo", Criteria.LEFT_JOIN)
		.createAlias("centroAtencion.instituciones", "institucion")
		
		.add(Restrictions.ne("citaOdonto.estado", ConstantesIntegridadDominio.acronimoAtendida))
		.add(Restrictions.eq("citaOdonto.tipo", ConstantesIntegridadDominio.acronimoTipoCitaOdonValoracionInicial))
		.add(Restrictions.in("ingreso.id", ingresos));
		
	 	criteria.setProjection(Projections.distinct(Projections.projectionList()
				.add(Projections.property("ingreso.id"), "idIngreso")
				.add( Projections.property("centroAtencion.consecutivo"), "consecutivoCentroAtencion" )
				.add( Projections.property("centroAtencion.descripcion"), "descripcionCentroAtencion" )
				.add( Projections.property("institucion.codigo"), "codigoInstitucion" )
				.add( Projections.property("empresaInstitucion.razonSocial"), "descripcionEmpresaInstitucion" )
				.add(Projections.property("paises.descripcion"),"descripcionPais")
				.add( Projections.property("ciudades.descripcion")	, "descripcionCiudad" )
				.add( Projections.property("regionesCobertura.descripcion"), "descripcionRegionCobertura" )
				
				.add( Projections.property("personas.codigo"), "codigoPaciente")
				.add( Projections.property("tipoId.acronimo"), "tipoId")
				.add( Projections.property("personas.numeroIdentificacion"), "numeroId")
				.add( Projections.property("personas.primerNombre"), "primerNombre")
				.add( Projections.property("personas.segundoNombre"), "segundoNombre")
				.add( Projections.property("personas.primerApellido"), "primerApellido")
				.add( Projections.property("personas.segundoApellido"), "segundoApellido")
				.add( Projections.property("personas.telefonoFijo"), "telefonoFijo")
				.add( Projections.property("personas.telefonoCelular"), "telefonoCelular")
				.add( Projections.property("personas.telefono"), "telefono")
				.add( Projections.property("citaOdonto.estado"), "estadoCitaOdonto")
				.add( Projections.property("personas.fechaNacimiento"), "fechaNacimiento")
				.add( Projections.property("sexo.nombre"), "sexoPaciente")
			
				))
		
		.setResultTransformer( Transformers.aliasToBean(DtoIngresosOdontologicos.class));
	 	
	 	criteria.addOrder( Order.asc("tipoId.acronimo") );
		criteria.addOrder( Order.asc("personas.numeroIdentificacion") );
		
		ArrayList<DtoIngresosOdontologicos> listadoIngresos=(ArrayList)criteria.list();
		
		return listadoIngresos;

	}
	
	/**
	 * Este m&eacute;todo se encarga de consultar las citas odontol&oacute;gicas
	 * que tienen una valoraci&oacute;n inicial en estado atendida.
	 * @return
	 * 
	 * @author Yennifer Guerrero
	 */
	public List<Integer> obtenercitaOdontoValIniAtendida(List<Integer> ingresos){
		
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(CitasOdontologicas.class, "citaOdonto")
		
		.createAlias("citaOdonto.valoracionesOdontos", "valoracion",Criteria.LEFT_JOIN)
		.createAlias("citaOdonto.pacientes", "pacientes")
		.createAlias("pacientes.ingresoses", "ingreso")
		
		.add(Restrictions.eq("citaOdonto.estado", ConstantesIntegridadDominio.acronimoAtendida))
		.add(Restrictions.eq("citaOdonto.tipo", ConstantesIntegridadDominio.acronimoTipoCitaOdonValoracionInicial))
		.add(Restrictions.in("ingreso.id", ingresos))
		
		.setProjection(Projections.distinct(Projections.projectionList()
				.add(Projections.property("ingreso.id"))));
		
		return criteria.list();
		
	}
	
	/**
	 * M&eacute;todo que devuelve un listado con los c&oacute;digos de las citas de un paciente
	 * en estado programada y de un tipo de cita espec&iacute;fico. 
	 * 
	 * @param codigoPaciente
	 * @param tipoCita
	 * @return
	 */
	public List<Long> obtenerCodigoCitasProgramadasPacienteXTipoCita (int codigoPaciente, String tipoCita){
		
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(CitasOdontologicas.class)
		
		.setProjection(Projections.projectionList()
				.add(Projections.property("codigoPk")))
		
		.add(Restrictions.eq("pacientes.codigoPaciente", codigoPaciente))
		.add(Restrictions.eq("estado", ConstantesIntegridadDominio.acronimoProgramado))
		.add(Restrictions.eq("tipo", tipoCita));
		
		return criteria.list();

	}
	
	/**
	 * Este m&eacute;todo se encarga de consultar las citas odontol&oacute;gicas
	 * que no tienen una valoraci&oacute;n inicial.
	 * @param ingresos
	 * @return
	 * 
	 * @author Yennifer Guerrero
	 * @param pacientesConValIni 
	 */
	public ArrayList<DtoIngresosOdontologicos> obtenerCitaOdontoDiferenteDeValIni(List<Integer> ingresos, List<Integer> pacientesConValIni){
		
		Criteria criteria = sessionFactory.getCurrentSession()
		.createCriteria(CitasOdontologicas.class, "citasOdonto")
		.createAlias("citasOdonto.pacientes", "pacientes")
		.createAlias("pacientes.ingresoses", "ingreso")
		.createAlias("ingreso.centroAtencion", "centroAtencion")
		.createAlias("centroAtencion.ciudades", "ciudades")
		.createAlias("ciudades.paises", "paises")
		.createAlias("centroAtencion.regionesCobertura", "regionesCobertura")
		.createAlias("centroAtencion.empresasInstitucion", "empresaInstitucion")
		.createAlias("pacientes.personas", "personas")
		.createAlias("personas.tiposIdentificacion", "tipoId")
		.createAlias("personas.sexo", "sexo", Criteria.LEFT_JOIN)
		.createAlias("centroAtencion.instituciones", "institucion");
	
		criteria.setProjection(Projections.distinct(Projections.projectionList()
				.add(Projections.property("ingreso.id"), "idIngreso")
				.add( Projections.property("centroAtencion.consecutivo"), "consecutivoCentroAtencion" )
				.add( Projections.property("centroAtencion.descripcion"), "descripcionCentroAtencion" )
				.add( Projections.property("institucion.codigo"), "codigoInstitucion" )
				.add( Projections.property("empresaInstitucion.razonSocial"), "descripcionEmpresaInstitucion" )
				.add(Projections.property("paises.descripcion"),"descripcionPais")
				.add( Projections.property("ciudades.descripcion")	, "descripcionCiudad" )
				.add( Projections.property("regionesCobertura.descripcion"), "descripcionRegionCobertura" )
				
				.add( Projections.property("personas.codigo"), "codigoPaciente")
				.add( Projections.property("tipoId.acronimo"), "tipoId")
				.add( Projections.property("personas.numeroIdentificacion"), "numeroId")
				.add( Projections.property("personas.primerNombre"), "primerNombre")
				.add( Projections.property("personas.segundoNombre"), "segundoNombre")
				.add( Projections.property("personas.primerApellido"), "primerApellido")
				.add( Projections.property("personas.segundoApellido"), "segundoApellido")
				.add( Projections.property("personas.telefonoFijo"), "telefonoFijo")
				.add( Projections.property("personas.telefonoCelular"), "telefonoCelular")
				.add( Projections.property("personas.telefono"), "telefono")
				.add( Projections.property("personas.fechaNacimiento"), "fechaNacimiento")
				.add( Projections.property("sexo.nombre"), "sexoPaciente")
				
				
				));
		
		criteria.add(Restrictions.in("ingreso.id", ingresos));
		criteria.add(Restrictions.not(Restrictions.in("pacientes.codigoPaciente", pacientesConValIni)));
		criteria.add(Restrictions.ne("citasOdonto.tipo", ConstantesIntegridadDominio.acronimoTipoCitaOdonValoracionInicial))
		
		.setResultTransformer( Transformers.aliasToBean(DtoIngresosOdontologicos.class));
		
		criteria.addOrder( Order.asc("tipoId.acronimo") );
		criteria.addOrder( Order.asc("personas.numeroIdentificacion") );
		ArrayList<DtoIngresosOdontologicos> listadoIngresos=(ArrayList)criteria.list();
		
		return listadoIngresos;
		
	}
	
	
	
	/**
	 * Consulta los cambios de servicios de citas odontologicas 
	 * @param dto filtro del reporte cambio servicios
	 * @return ArrayList<DtoResultadoConsultaCambioServiciosOdontologicos>
	 */
	public  ArrayList<DtoResultadoConsultaCambioServiciosOdontologicos> consultarCambioServCitasOdonto(DtoFiltroReporteCambioServCitaOdonto dto){
		
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(SolicitudCambioServicio.class, "solicitud")
		.createAlias("solicitud.citasOdontologicas", "citasOdonto")
		.createAlias("citasOdonto.centrosCosto", "centroCosto")
		.createAlias("centroCosto.centroAtencion", "centroAtencion")
		
		.createAlias("centroAtencion.ciudades", "ciudad")
		.createAlias("centroAtencion.regionesCobertura", "regionCobertura")
		
		
		.createAlias("citasOdonto.pacientes", "paciente")
		.createAlias("paciente.personas", "persona")
		
		.createAlias("persona.tiposIdentificacion","tipoIdentificacion")
		.createAlias("persona.sexo","sexo")
		
		.createAlias("solicitud.usuariosByUsuarioSolicita", "usuSolicita")
		.createAlias("solicitud.usuariosByUsuarioConfirma", "usuConfirma")
		.createAlias("usuSolicita.personas", "personaProfesional")
		
		.createAlias("solicitud.progServAnterioresCitas", "progServAnterior")
		.createAlias("progServAnterior.programas", "programa")
		.createAlias("programa.especialidades", "especialidad")
		;
		
		ProjectionList projection = Projections.projectionList();
		
		//FECHA DE SOLICITUD
		if(dto.getFechaInicial()!=null && dto.getFechaFinal() !=null){
			criteria.add(Restrictions.between("solicitud.fechaSolicita",dto.getFechaInicial(), dto.getFechaFinal()));
			
		}
		projection.add(Projections.property("solicitud.fechaSolicita"),"fechaSol");
		projection.add(Projections.property("solicitud.horaSolicita"),"horaSol");
		//PAIS
		if (!UtilidadTexto.isEmpty(dto.getCodigoPaisSeleccionado())&&!dto.getCodigoPaisSeleccionado().trim().equals(ConstantesBD.codigoNuncaValido+"")) {
			criteria.createAlias("ciudad.departamentos", "departamento");
			criteria.createAlias("departamento.paises", "pais");
			criteria.add(Restrictions.eq("pais.codigoPais", dto.getCodigoPaisSeleccionado()));
		}
		//ESTADO DE LA SOLICITUD
		if(dto.getTiposEstadoSolicitud()!=null && dto.getTiposEstadoSolicitud().length>0){
			criteria.add(Restrictions.in("solicitud.estado",dto.getTiposEstadoSolicitud()));
			
		}
		projection.add(Projections.property("solicitud.estado"),"estadoSol");
		
		//INSTITUCION
		if(dto.getEsInstitucionMultiempresa().equals(ConstantesBD.acronimoSi)){
			criteria.createAlias("centroAtencion.empresasInstitucion", "empresaInstitucion");
			if (dto.getCodigoEmpresaInstitucionSeleccionada() > 0) {
				criteria.add(Restrictions.eq("empresaInstitucion.codigo",dto.getCodigoEmpresaInstitucionSeleccionada()));
				
			}
		}else{
			criteria.createAlias("centroAtencion.instituciones", "empresaInstitucion");
			criteria.add(Restrictions.eq("empresaInstitucion.codigo",dto.getCodigoInstitucion()));
		}
		projection.add(Projections.property("empresaInstitucion.razonSocial"),"descripcionEmpresaInstitucion");
		
		//SEXO
		if (!UtilidadTexto.isEmpty(dto.getSexoPaciente()) && !dto.getSexoPaciente().equals("-2")) {
			int sexoPaciente = Integer.parseInt(dto.getSexoPaciente());
			
			criteria.add(Restrictions.eq("sexo.codigo", sexoPaciente));
			
		}
		//RANGOS EDAD
		if (!UtilidadTexto.isEmpty(dto.getRangoEdadInicial()) &&!UtilidadTexto.isEmpty(dto.getRangoEdadFinal())) {
			
			Date rangoFechaInicial = UtilidadFecha.conversionFormatoFechaStringDate(dto.getRangoEdadInicialFecha());
			Date rangoFechaFinal = UtilidadFecha.conversionFormatoFechaStringDate(dto.getRangoEdadFinalFecha());
			criteria.add(Restrictions.between("persona.fechaNacimiento",rangoFechaFinal, rangoFechaInicial));
		}
		//REGION
		if (dto.getCodigoRegionSeleccionada() > 0) {
			criteria.add(Restrictions.eq("regionCobertura.codigo", dto.getCodigoRegionSeleccionada()));
			
		}
		projection.add(Projections.property("regionCobertura.descripcion"),"descripcionRegionCobertura");
		//CIUDAD
		if (!UtilidadTexto.isEmpty(dto.getCiudadDeptoPais()) && !dto.getCiudadDeptoPais().trim().equals(ConstantesBD.codigoNuncaValido+"")) {
			
			String vec[]= dto.getCiudadDeptoPais().split(ConstantesBD.separadorSplit);			
			dto.setCodigoCiudad(vec[0]);
			dto.setCodigoDpto(vec[1]);
			dto.setCodigoPais(vec[2]);
			criteria.add(Restrictions.eq("ciudad.id.codigoCiudad", dto.getCodigoCiudad()))
			.add(Restrictions.eq("ciudad.id.codigoPais", dto.getCodigoPais()))
			.add(Restrictions.eq("ciudad.id.codigoDepartamento", dto.getCodigoDpto()));
		}
		projection.add(Projections.property("ciudad.descripcion"),"descripcionCiudad");
		projection.add(Projections.property("pais.descripcion"),"descripcionPais");
		//CENTRO ATENCION
		if (dto.getConsecutivoCentroAtencionSeleccionado() > 0) {
			criteria.add(Restrictions.eq("centroAtencion.consecutivo",dto.getConsecutivoCentroAtencionSeleccionado()));
			
		}
		projection.add(Projections.property("centroAtencion.descripcion"),"descripcionCentroAtencion");
		//CODIGO PROFESIONAL
		if (!UtilidadTexto.isEmpty(dto.getLoginProfesional())&& !dto.getLoginProfesional().trim().equals(ConstantesBD.codigoNuncaValido+"")) {
			criteria.add(Restrictions.eq("usuSolicita.login",dto.getLoginProfesional()));
			
		}
		//USUARIO
		if (dto.getLoginUsuario() != null && !UtilidadTexto.isEmpty(dto.getLoginUsuario()) && !dto.getLoginUsuario().trim().equals(ConstantesBD.codigoNuncaValido+"")) {
			criteria.add(Restrictions.eq("usuConfirma.login",dto.getLoginUsuario()));
		}
		
		//SERVICIOS
		if(dto.getServicios()!=null&&dto.getServicios().size()>0){
			criteria.createAlias("progServAnterior.servicios", "servicio");
			criteria.add(Restrictions.in("servicio.codigo",dto.getCodigosServicios()));
		}
		//PROGRAMAS
		if(dto.getcodigoPrograma() > 0){
			criteria.add(Restrictions.eq("programa.codigo",dto.getcodigoPrograma()));
		}
		//ESPECIALIDADES
		if (dto.getcodigoEspecialidad() > 0) {
			criteria.add(Restrictions.eq("especialidad.codigo",dto.getcodigoEspecialidad()));
		}
		
		projection.add(Projections.property("solicitud.codigoPk"),"codigoPkSolicitud");
		projection.add(Projections.property("especialidad.nombre"),"nombreEspecialidad");
		projection.add(Projections.property("programa.codigoPrograma"),"nombrePrograma");
		projection.add(Projections.property("personaProfesional.primerNombre"),"primerNombre");
		projection.add(Projections.property("personaProfesional.segundoNombre"),"segundoNombre");
		projection.add(Projections.property("personaProfesional.primerApellido"),"primerApellido");
		projection.add(Projections.property("personaProfesional.segundoApellido"),"segundoApellido");
		projection.add(Projections.property("personaProfesional.codigo"),"codigoProfesional");
		projection.add(Projections.property("usuSolicita.login"),"loginProfesional");
		projection.add(Projections.property("solicitud.estadoCita"),"estadoCita");
		projection.add(Projections.property("persona.numeroIdentificacion"),"identificacionPaciente");
		projection.add(Projections.property("persona.primerNombre"),"primerNombrePaciente");
		projection.add(Projections.property("persona.segundoNombre"),"segundoNombrePaciente");
		projection.add(Projections.property("persona.primerApellido"),"primerApellidoPaciente");
		projection.add(Projections.property("persona.segundoApellido"),"segundoApellidoPaciente");
		projection.add(Projections.property("persona.fechaNacimiento"),"fechaNacimientoPaciente");
		projection.add(Projections.property("sexo.codigo"),"ayudanteCodigoSexoPaciente");
		projection.add(Projections.property("tipoIdentificacion.acronimo"),"acronimoIdentificacion");
		projection.add(Projections.property("usuConfirma.login"),"usuario");
		projection.add(Projections.property("centroAtencion.consecutivo"),"consecutivoCentroAtencion");
		projection.add(Projections.property("empresaInstitucion.codigo"),"codigoEmpresaInstitucion");
		projection.add(Projections.property("citasOdonto.codigoPk"),"codigoPkCita");
		
		
	
	
		criteria.setProjection(Projections.distinct(projection));
		criteria.addOrder( Order.asc("empresaInstitucion.codigo") );
		criteria.addOrder( Order.asc("centroAtencion.consecutivo") );
		criteria.addOrder( Order.asc("personaProfesional.primerApellido") );
		criteria.addOrder( Order.asc("personaProfesional.segundoApellido") );
		criteria.addOrder( Order.asc("personaProfesional.primerNombre") );
		criteria.addOrder( Order.asc("personaProfesional.segundoNombre") );
		criteria.addOrder( Order.asc("solicitud.fechaSolicita") );
		criteria.addOrder( Order.asc("solicitud.horaSolicita") );
		
				
		criteria.setResultTransformer( Transformers.aliasToBean(DtoResultadoConsultaCambioServiciosOdontologicos.class));
		ArrayList<DtoResultadoConsultaCambioServiciosOdontologicos> listadoCambioServicios=
			(ArrayList<DtoResultadoConsultaCambioServiciosOdontologicos>)criteria.list();
	
		
		for (DtoResultadoConsultaCambioServiciosOdontologicos registro : listadoCambioServicios) {
			
			String especialidadesMedico=cargarEspecialidadesMedico(registro.getCodigoProfesional());
			registro.setEspecialidad(especialidadesMedico);
			registro.setEspecialidadesProfesional(especialidadesProf);
			this.cargarServiciosyValoresIniciales(registro.getCodigoPkSolicitud(),dto.getCodigoManualEstandarBusquedaServicios());
			this.cargarServiciosyValoresFinales(registro.getCodigoPkSolicitud(),dto.getCodigoManualEstandarBusquedaServicios());
			registro.setValorInicial(valorInicial);
			registro.setValorFinal(valorFinal);

			if(!UtilidadTexto.isEmpty(tmpServiciosAnteriores))
				registro.setServicioInicial(tmpServiciosAnteriores);
			else
				registro.setServicioInicial("-");
			
			if(!UtilidadTexto.isEmpty(tmpServiciosNuevos))
				registro.setServicioFinal(tmpServiciosNuevos);
			else
				registro.setServicioFinal("-");
			
		}
		
	
		
		return listadoCambioServicios;
	}
	
	
	BigDecimal valorInicial;
	BigDecimal valorFinal;
	String tmpServiciosAnteriores;
	String tmpServiciosNuevos;
	ArrayList<String> especialidadesProf;
	
	
	
	
	/**
	 * Metodo que devuelve todos los servicios y valores iniciales de la
	 * solicitud de cambio de servicios
	 * @param codigopk de la solicitud
	 * @param codigoEstandar 
	 * 
	 */
	public void cargarServiciosyValoresIniciales(int codigoPk, int codigoEstandar){
		
		
		
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(SolicitudCambioServicio.class, "solicitud")
		.createAlias("solicitud.citasOdontologicas", "citasOdonto")
		
		.createAlias("solicitud.progServAnterioresCitas", "progServAnteriores")
		.createAlias("progServAnteriores.servicios", "serviciosAnteriores")
		.createAlias("serviciosAnteriores.referenciasServicios", "referenciaServAnterior")
		.createAlias("referenciaServAnterior.tarifariosOficiales", "tipoTarifarioAnterior")
		;
		criteria.add(Restrictions.eq("solicitud.codigoPk", codigoPk));
		criteria.add(Restrictions.eq("tipoTarifarioAnterior.codigo", codigoEstandar));
		
		criteria.setProjection(Projections.projectionList()
				.add(Projections.property("solicitud.codigoPk"),"codigoPk")
				.add(Projections.property("serviciosAnteriores.codigo"),"codigoServiciosAnteriores")
				.add(Projections.property("progServAnteriores.valorUnitario"),"valorInicial")
				.add(Projections.property("referenciaServAnterior.codigoPropietario"),"codigoPropietarioAnteriores"));
		
		criteria.setResultTransformer(Transformers.aliasToBean(DtoReporteCambioServiciosOrdenarServicios.class));
		
		List<DtoReporteCambioServiciosOrdenarServicios> listTmp=  criteria.list();
		
		valorInicial = new BigDecimal("0.0").setScale(2);
		StringBuilder tmpSA= new StringBuilder();
	
		if(listTmp.size()>0){
			for(int i=0;i<listTmp.size();i++)
			{
			
					if(listTmp.get(i).getValorInicial()!=null)
						valorInicial=valorInicial.add(listTmp.get(i).getValorInicial());
					if(listTmp.get(i).getCodigoPropietarioAnteriores()!=null){
						tmpSA.append(listTmp.get(i).getCodigoPropietarioAnteriores());
						
						if(i!=listTmp.size()-1)			 
							tmpSA.append("\n"); // para hacer un enter
					}
			
			}	
				
			
		}
		tmpServiciosAnteriores ="";
		if(tmpSA!=null)
		{
			tmpServiciosAnteriores=tmpSA.toString();
		}
		
		
	}
	
	/**
	 * Metodo que devuelve todos los servicios y valores finales de la
	 * solicitud de cambio de servicios
	 * @param codigopk de la solicitud
	 * @param codigoEstandar 
	 * 
	 */
	public void cargarServiciosyValoresFinales(int codigoPk, int codigoEstandar){
		
		
		
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(SolicitudCambioServicio.class, "solicitud")
		.createAlias("solicitud.citasOdontologicas", "citasOdonto")
		
		.createAlias("solicitud.progServNuevosCitas", "progServNuevos")
		.createAlias("progServNuevos.servicios", "serviciosNuevos")
		.createAlias("serviciosNuevos.referenciasServicios", "referenciaServNuevos")
		.createAlias("referenciaServNuevos.tarifariosOficiales", "tipoTarifarioNuevos")
		;
		criteria.add(Restrictions.eq("solicitud.codigoPk", codigoPk));
		criteria.add(Restrictions.eq("tipoTarifarioNuevos.codigo", codigoEstandar));
		
		criteria.setProjection(Projections.projectionList()
				.add(Projections.property("solicitud.codigoPk"),"codigoPk")
				.add(Projections.property("serviciosNuevos.codigo"),"codigoServiciosNuevos")
				.add(Projections.property("progServNuevos.valorUnitario"),"valorFinal")
				.add(Projections.property("referenciaServNuevos.codigoPropietario"),"codigoPropietarioNuevos"));
		
		criteria.setResultTransformer(Transformers.aliasToBean(DtoReporteCambioServicioOrdenarServiciosFinales.class));
		
		List<DtoReporteCambioServicioOrdenarServiciosFinales> listTmp=  criteria.list();
		
		valorFinal = new BigDecimal("0.0").setScale(2);
		StringBuilder tmpSN= new StringBuilder();

		if(listTmp.size()>0){
			for(int i=0;i<listTmp.size();i++)
			{	
				
					if(listTmp.get(i).getValorFinal()!=null)
						valorFinal=valorFinal.add(listTmp.get(i).getValorFinal());
						
					if(listTmp.get(i).getCodigoPropietarioNuevos()!=null){
						if(!UtilidadTexto.isEmpty(listTmp.get(i).getCodigoPropietarioNuevos())){
							tmpSN.append(listTmp.get(i).getCodigoPropietarioNuevos());
							
							if(i!=listTmp.size()-1)			 
								tmpSN.append("\n"); // para hacer un enter
						}
						
					}
				
			}
		}
		tmpServiciosNuevos ="";
		if(tmpSN!=null)
		{
			tmpServiciosNuevos=tmpSN.toString();
		}
		
		
	}
	
	/**
	 * Metodo que devuelve una cadena con las especialidades
	 * de un profesional de la salud 
	 * @param codigoMed codigo del profesional
	 * @return cadena con las especialidades del profesional
	 */
	public String cargarEspecialidadesMedico(int codigoMed){
		
		
		
		StringBuilder tmpServicios= new StringBuilder();
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(EspecialidadesMedicos.class, "especialidadesMedicos")
		.createAlias("especialidadesMedicos.medicos", "medico")
		.createAlias("especialidadesMedicos.especialidades", "especialidad")
		;
				
		criteria.add(Restrictions.eq("medico.codigoMedico", codigoMed));
		
		criteria.setProjection(Projections.projectionList()
				.add(Projections.property("especialidad.nombre"),"nombreEspecialidad"));
				
		criteria.setResultTransformer(Transformers.aliasToBean(DtoReporteCambioServiciosListaEspecialidadesMedico.class));
		
		List<DtoReporteCambioServiciosListaEspecialidadesMedico> listTmp=  criteria.list();
		especialidadesProf=new ArrayList<String>();
		for(int i=0;i<listTmp.size();i++)
		{
			especialidadesProf.add(listTmp.get(i).getNombreEspecialidad());
			tmpServicios.append(listTmp.get(i).getNombreEspecialidad());
			if(i!=listTmp.size()-1)	
			tmpServicios.append("\n"); // para hacer un enter
		}
		
		
		String tmp ="";
		if(tmpServicios!=null)
		{
			tmp=tmpServicios.toString();
		}
		
	return tmp;
		
	}
	
	
	/**
	 * Consulta los tiempos de espera de atención de citas odontológicas 
	 * @param dto filtro del reporte tiempo espera atención citas odontológicas
	 * @return ArrayList<DtoResultadoConsultaCambioServiciosOdontologicos>
	 */
	public  ArrayList<DtoResultadoConsultaReporteTiempoEsperaAtencionCitasOdonto> consultarTiempoEsperaAtencionCitasOdonto(DtoFiltroReporteTiempoEsperaAtencionCitasOdontologicas dto){
		
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(CitasOdontologicas.class, "citaOdonto")
		
		.createAlias("citaOdonto.inicioAtencionCita", "inicioatencioncita",Criteria.LEFT_JOIN)
		.createAlias("citaOdonto.agendaOdontologica", "agenda")
		.createAlias("agenda.unidadesConsulta", "unidadConsulta")
		.createAlias("unidadConsulta.especialidades", "especialidades")
		
		.createAlias("citaOdonto.centrosCosto", "centroCosto")
		.createAlias("centroCosto.centroAtencion", "centroAtencion")
			
		.createAlias("centroAtencion.ciudades", "ciudad")
		.createAlias("centroAtencion.regionesCobertura", "regionCobertura")
		
		.createAlias("citaOdonto.pacientes", "paciente")
		.createAlias("paciente.personas", "personapaciente")
		
		.createAlias("personapaciente.tiposIdentificacion","tipoIdentificacion")
		
		.createAlias("citaOdonto.usuariosByUsuarioModifica", "usuarioAsigno")
		
		.createAlias("citaOdonto.usuariosByUsuarioConfirma", "usuarios",Criteria.LEFT_JOIN)
		.createAlias("usuarios.personas", "personaProfesional",Criteria.LEFT_JOIN)
		
		.createAlias("citaOdonto.logCitasOdontologicases", "logcitas")
		;
		
		ProjectionList projection = Projections.projectionList();
		
		//EN ESTADO ATENDIDA
		criteria.add(Restrictions.eq("citaOdonto.estado",ConstantesIntegridadDominio.acronimoAtendida));
		
		//FECHA 
		if(!UtilidadTexto.isEmpty(dto.getFechaInicial().toString())&& !UtilidadTexto.isEmpty(dto.getFechaFinal().toString())){
			criteria.add(Restrictions.between("agenda.fecha",dto.getFechaInicial(), dto.getFechaFinal()));
		}
			
		//TIPO DE CITAS
		if(dto.getTiposCita()!=null && dto.getTiposCita().length>0){
			criteria.add(Restrictions.in("citaOdonto.tipo",dto.getTiposCita()));
		}
		
		//INSTITUCION
		if(dto.getInstitucionMultiempresa().equals(ConstantesBD.acronimoSi)){
			criteria.createAlias("centroAtencion.empresasInstitucion", "empresaInstitucion");
			if (dto.getCodigoEmpresaInstitucion() > 0) {
				criteria.add(Restrictions.eq("empresaInstitucion.codigo",dto.getCodigoEmpresaInstitucion()));
			}
		}else{
			criteria.createAlias("centroAtencion.instituciones", "empresaInstitucion");
			criteria.add(Restrictions.eq("empresaInstitucion.codigo",dto.getCodigoInstitucion()));
		}
		projection.add(Projections.property("empresaInstitucion.razonSocial"),"descripcionEmpresaInstitucion");
		
		//CENTRO ATENCION
		if (dto.getConsecutivoCentroAtencionSeleccionado() > 0) {
			criteria.add(Restrictions.eq("centroAtencion.consecutivo",dto.getConsecutivoCentroAtencionSeleccionado()));
		}
		projection.add(Projections.property("centroAtencion.descripcion"),"descripcionCentroAtencion");
		
		//REGION
		if (dto.getCodigoRegionSeleccionada() > 0) {
			criteria.add(Restrictions.eq("regionCobertura.codigo", dto.getCodigoRegionSeleccionada()));
			
		}
		projection.add(Projections.property("regionCobertura.descripcion"),"descripcionRegionCobertura");
		
		//ESPECIALIDADES
		if (dto.getCodigoEspecialidad() > 0) {
			criteria.add(Restrictions.eq("especialidades.codigo",dto.getCodigoEspecialidad()));
		}
		projection.add(Projections.property("especialidades.nombre"),"nombreEspecialidad");
		
		//UNIDAD DE AGENDA
		if (dto.getCodigoUnidadAgenda() > 0) {
			criteria.add(Restrictions.eq("unidadConsulta.codigo",dto.getCodigoUnidadAgenda()));
		}
		projection.add(Projections.property("unidadConsulta.descripcion"),"nombreUnidadAgenda");
		projection.add(Projections.property("unidadConsulta.codigo"),"codigoUnidadAgenda");
		
		//PAIS
		if (!UtilidadTexto.isEmpty(dto.getCodigoPaisSeleccionado())&&!dto.getCodigoPaisSeleccionado().trim().equals(ConstantesBD.codigoNuncaValido+"")) {
			criteria.createAlias("ciudad.departamentos", "departamento");
			criteria.createAlias("departamento.paises", "pais");
			criteria.add(Restrictions.eq("pais.codigoPais", dto.getCodigoPaisSeleccionado()));
		}
		
		//CIUDAD
		if (!UtilidadTexto.isEmpty(dto.getCiudadDeptoPais()) && !dto.getCiudadDeptoPais().trim().equals(ConstantesBD.codigoNuncaValido+"")) {
			
			String vec[]= dto.getCiudadDeptoPais().split(ConstantesBD.separadorSplit);			
			dto.setCodigoCiudad(vec[0]);
			dto.setCodigoDpto(vec[1]);
			dto.setCodigoPais(vec[2]);
			criteria.add(Restrictions.eq("ciudad.id.codigoCiudad", dto.getCodigoCiudad()))
			.add(Restrictions.eq("ciudad.id.codigoPais", dto.getCodigoPais()))
			.add(Restrictions.eq("ciudad.id.codigoDepartamento", dto.getCodigoDpto()));
		}
		projection.add(Projections.property("ciudad.descripcion"),"descripcionCiudad");
		projection.add(Projections.property("pais.descripcion"),"descripcionPais");
		
		//SERVICIOS
		if(!Utilidades.isEmpty(dto.getCodigosServicios())){
			criteria.createAlias("citaOdonto.serviciosCitaOdontologicas", "serviciosCitaOdonto");
			criteria.createAlias("serviciosCitaOdonto.servicios", "servicio");
			criteria.add(Restrictions.in("servicio.codigo",dto.getCodigosServicios()));
		}
		
		//CODIGO PROFESIONAL
		if (!UtilidadTexto.isEmpty(dto.getLoginProfesional())&& !dto.getLoginProfesional().trim().equals(ConstantesBD.codigoNuncaValido+"")) {
			if(!dto.getLoginProfesional().trim().equals(ConstantesBD.codigoProfesionalSaludNoAsignado+"")){
				criteria.add(Restrictions.eq("usuarios.login",dto.getLoginProfesional()));
			}else{
				criteria.add(Restrictions.isNull("agenda.medicos"));
			}
		}
		
		//USUARIO
		if (dto.getLoginUsuario() != null && !UtilidadTexto.isEmpty(dto.getLoginUsuario()) && !dto.getLoginUsuario().trim().equals(ConstantesBD.codigoNuncaValido+"")) {
			criteria.add(Restrictions.eq("usuarioAsigno.login",dto.getLoginUsuario()));
		}else{
			criteria.add(Restrictions.in("usuarioAsigno.login",dto.getLoginUsuarios()));
		}
		
		//LOGCITASODONTO PARA FECHA DE ASIGNACION
		criteria.add(Restrictions.eq("logcitas.estado",ConstantesIntegridadDominio.acronimoAsignado));
		criteria.add(Restrictions.isNull("logcitas.porConfirmar"));
		
		projection.add(Projections.property("citaOdonto.codigoPk"),"codigoPkCita");
		projection.add(Projections.property("personapaciente.numeroIdentificacion"),"identificacionPaciente");
		projection.add(Projections.property("personapaciente.primerNombre"),"primerNombrePaciente");
		projection.add(Projections.property("personapaciente.segundoNombre"),"segundoNombrePaciente");
		projection.add(Projections.property("personapaciente.primerApellido"),"primerApellidoPaciente");
		projection.add(Projections.property("personapaciente.segundoApellido"),"segundoApellidoPaciente");
		projection.add(Projections.property("tipoIdentificacion.acronimo"),"acronimoIdentificacion");
		projection.add(Projections.property("centroAtencion.consecutivo"),"consecutivoCentroAtencion");
		projection.add(Projections.property("empresaInstitucion.codigo"),"codigoEmpresaInstitucion");
		projection.add(Projections.property("usuarioAsigno.login"),"loginUsuarioAsigno");
		projection.add(Projections.property("personaProfesional.primerNombre"),"primerNombreProfesional");
		projection.add(Projections.property("personaProfesional.primerApellido"),"primerApellidoProfesional");
		
		projection.add(Projections.property("inicioatencioncita.fechaInicio"),"fechaInicioAtencion");
		projection.add(Projections.property("inicioatencioncita.horaInicio"),"horaInicioAtencion");
		
		projection.add(Projections.property("agenda.fecha"),"fechaCita");
		projection.add(Projections.property("citaOdonto.horaInicio"),"horaCita");
		projection.add(Projections.property("citaOdonto.tipo"),"tipoCita");
		projection.add(Projections.property("logcitas.fechaModifica"),"fechaAsignacion");
		projection.add(Projections.property("logcitas.horaModifica"),"horaAsignacion");
		projection.add(Projections.property("citaOdonto.fechaConfirma"),"fechaConfirmacion");
		projection.add(Projections.property("citaOdonto.horaConfirma"),"horaConfirmacion");
		
		criteria.setProjection(Projections.distinct(projection));
		criteria.addOrder( Order.asc("empresaInstitucion.codigo") );
		criteria.addOrder( Order.asc("centroAtencion.consecutivo") );
		criteria.addOrder( Order.asc("unidadConsulta.descripcion") );
		criteria.addOrder( Order.asc("agenda.fecha") );
		criteria.addOrder( Order.asc("citaOdonto.horaInicio") );
		
		criteria.setResultTransformer( Transformers.aliasToBean(DtoResultadoConsultaReporteTiempoEsperaAtencionCitasOdonto.class));
		ArrayList<DtoResultadoConsultaReporteTiempoEsperaAtencionCitasOdonto> listadoTiempoEspera=
			(ArrayList<DtoResultadoConsultaReporteTiempoEsperaAtencionCitasOdonto>)criteria.list();
	
		return listadoTiempoEspera;
	}
	
	

	/**
	 * Método que se encarga de eliminar el registro de la cita programada
	 * y sus servicios asociados al código de cita
	 * 
	 * @param codigoCita
	 * @return
	 */
	public boolean eliminarCitaProgramada (long codigoCita){
		
		boolean resultado= false;
		
		CitasOdontologicas citaProgramada = super.findById(codigoCita);
		
		if(citaProgramada!=null){
			
			Set<ServiciosCitaOdontologica> serviciosCitaOdontologica = citaProgramada.getServiciosCitaOdontologicas();
			
			for (ServiciosCitaOdontologica servicio : serviciosCitaOdontologica) {
			
				servicio.getDuracion();
			}
			
			super.delete(citaProgramada);
		}
		
		return resultado;
	}
	
	/**
	 * Este mètodo se encarga de obtener un listado con el número de citas por estado y la 
	 * fecha más reciente de la cita, para los pacientes cuyo ingreso se encuentra dentro de la lista
	 * que llega por parámetro.
	 *
	 * @param ingresos
	 * @param filtro
	 * @return
	 *
	 * @autor Yennifer Guerrero
	 */
	public ArrayList<DtoIngresosOdontologicos> obtenerEstadoCitasPorPaciente(List<Integer> ingresos, DtoReporteIngresosOdontologicos filtro){
		
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(CitasOdontologicas.class,"citaOdonto")
		.createAlias("citaOdonto.pacientes", "pacientes")
		.createAlias("pacientes.ingresoses", "ingreso");
		
		criteria.add(Restrictions.in("ingreso.id", ingresos));
		criteria.add(Restrictions.between("citaOdonto.fechaModifica", filtro.getFechaInicial(), filtro.getFechaFinal()));
		criteria.add(Restrictions.eq("citaOdonto.tipo", ConstantesIntegridadDominio.acronimoTipoCitaOdonValoracionInicial));
		
	 	criteria.setProjection(Projections.projectionList()
				.add(Projections.property("pacientes.codigoPaciente"), "codigoPaciente")
				.add( Projections.property("citaOdonto.estado"), "estadoCitaOdonto")
				.add(Projections.count("citaOdonto.estado"), "numeroCitas")
				.add(Projections.sqlProjection("max(this_.fecha_modifica ||' '|| this_.hora_modifica) as fechaHoraModifica", 
						new String[] {"fechaHoraModifica"}, new org.hibernate.type.Type[] {StandardBasicTypes.STRING}),"fechaHoraModifica" )
				
				.add(Projections.groupProperty("pacientes.codigoPaciente"))
				.add(Projections.groupProperty("citaOdonto.estado"))
				)
		
		.setResultTransformer( Transformers.aliasToBean(DtoIngresosOdontologicos.class));
	 	
		ArrayList<DtoIngresosOdontologicos> listadoIngresos=(ArrayList)criteria.list();
		
		return listadoIngresos;
	}
	
	/**
	 * Este mètodo se encarga de obtener la información de los pacientes 
	 * sin valoraciones iniciales según la lista de pacientes que llega por parámetro. 
	 *
	 * @param pacientes
	 * @return
	 *
	 * @autor Yennifer Guerrero
	 */
	public ArrayList<DtoIngresosOdontologicos> obtenerInfoPacientesSinValIni(List<Integer> pacientes){
		
		 Criteria criteria = sessionFactory.getCurrentSession().createCriteria(CitasOdontologicas.class,"citaOdonto")
			.createAlias("citaOdonto.pacientes", "pacientes")
			.createAlias("pacientes.ingresoses", "ingreso")
			.createAlias("ingreso.centroAtencion", "centroAtencion")
			.createAlias("centroAtencion.ciudades", "ciudades")
			.createAlias("ciudades.paises", "paises")
			.createAlias("centroAtencion.regionesCobertura", "regionesCobertura")
			.createAlias("centroAtencion.empresasInstitucion", "empresaInstitucion")
			.createAlias("pacientes.personas", "personas")
			.createAlias("personas.tiposIdentificacion", "tipoId")
			.createAlias("personas.sexo", "sexo", Criteria.LEFT_JOIN)
			.createAlias("centroAtencion.instituciones", "institucion")
			
			.add(Restrictions.in("personas.codigo", pacientes));
			
		 	criteria.setProjection(Projections.distinct(Projections.projectionList()
					.add( Projections.property("centroAtencion.consecutivo"), "consecutivoCentroAtencion" )
					.add( Projections.property("centroAtencion.descripcion"), "descripcionCentroAtencion" )
					.add( Projections.property("institucion.codigo"), "codigoInstitucion" )
					.add( Projections.property("empresaInstitucion.razonSocial"), "descripcionEmpresaInstitucion" )
					.add(Projections.property("paises.descripcion"),"descripcionPais")
					.add( Projections.property("ciudades.descripcion")	, "descripcionCiudad" )
					.add( Projections.property("regionesCobertura.descripcion"), "descripcionRegionCobertura" )
					
					.add( Projections.property("personas.codigo"), "codigoPaciente")
					.add( Projections.property("tipoId.acronimo"), "tipoId")
					.add( Projections.property("personas.numeroIdentificacion"), "numeroId")
					.add( Projections.property("personas.primerNombre"), "primerNombre")
					.add( Projections.property("personas.segundoNombre"), "segundoNombre")
					.add( Projections.property("personas.primerApellido"), "primerApellido")
					.add( Projections.property("personas.segundoApellido"), "segundoApellido")
					.add( Projections.property("personas.telefonoFijo"), "telefonoFijo")
					.add( Projections.property("personas.telefonoCelular"), "telefonoCelular")
					.add( Projections.property("personas.telefono"), "telefono")
					.add( Projections.property("personas.fechaNacimiento"), "fechaNacimiento")
					.add( Projections.property("sexo.nombre"), "sexoPaciente")
				
					))
			
			.setResultTransformer( Transformers.aliasToBean(DtoIngresosOdontologicos.class));
		 	
		 	criteria.addOrder( Order.asc("tipoId.acronimo") );
			criteria.addOrder( Order.asc("personas.numeroIdentificacion") );
			
			ArrayList<DtoIngresosOdontologicos> listadoIngresos=(ArrayList)criteria.list();
			
			return listadoIngresos;

		}
	
	/**
	 * 
	 * Este mètodo se encarga de obtener el código de las citas de valoración 
	 * inicial en estado atendida cuyo registro es el
	 * más actual por paciente.
	 * @param pacientesConValIni 
	 *
	 * @return
	 *
	 * @autor Yennifer Guerrero
	 */
	public List<Long> obtenerCodigoCitasOdontoAtendidas(List<Integer> pacientesConValIni){
		
		String hql="	select coe.codigoPk FROM CitasOdontologicas coe"+ 
						"	INNER JOIN coe.agendaOdontologica aoe " +	
						"	INNER JOIN coe.pacientes pe "+
						
						"	where (pe.codigoPaciente,concat(concat(aoe.fecha, ' '), coe.horaInicio )) in " +
							"( "+
							"	select p.codigoPaciente, MAX(concat(concat(ao.fecha, ' '), co.horaInicio )) AS fecha_hora "+
							"	from CitasOdontologicas co "+
							"	INNER JOIN co.agendaOdontologica ao "+
							"	INNER JOIN co.pacientes p "+
							"	WHERE "+
							"	co.tipo = :tipo_cita "+
							"	and co.estado =:estado_cita "+
							"	and p.codigoPaciente in (:pacientes_con_valini) "+
							"	GROUP BY p.codigoPaciente"+
							"	) and "+
							"	coe.tipo = :tipo_cita "+
							"	and coe.estado =:estado_cita "+
							"	and pe.codigoPaciente in (:pacientes_con_valini) ";
		
		
		Query query=sessionFactory.getCurrentSession().createQuery(hql);
		query.setParameter("tipo_cita", ConstantesIntegridadDominio.acronimoTipoCitaOdonValoracionInicial);
		query.setParameter("estado_cita", ConstantesIntegridadDominio.acronimoAtendida);
		query.setParameterList("pacientes_con_valini", pacientesConValIni);
		
		return query.list();
	}
	
	/**
	 * Este mètodo se encarga de encontrar una cita odontológica por medio
	 * de su codigo y actualizar su indicativo de cambio de estado.
	 *
	 * @param codigoCita
	 * @return
	 *
	 * @autor Yennifer Guerrero
	 */
	public boolean actualizarIndicativoCambioEstadoCita(long codigoCita){
		
		CitasOdontologicas co=null;
		try{
			co=super.findById(codigoCita);
		} catch (Exception e) {
			Log4JManager.error("No se pudo encontrar la cita odontológica consultada ",e);
		}	
		
		co.setIndicativoCambioEstado(ConstantesIntegridadDominio.acronimoManual);
		
		boolean actualizado = actualizarCitaOdontologica(co);
		
		return actualizado;				
	}
	
	
	/**
	 * Este mètodo se encarga de actualizar una determinada cita odontológica
	 * con los valores que han sido modificados por parte del usuario.
	 *
	 * @param citaOdontologica
	 * @return
	 *
	 * @autor Yennifer Guerrero
	 */
	public boolean actualizarCitaOdontologica(CitasOdontologicas citaOdontologica){
		boolean save = false;
		try{
			super.merge(citaOdontologica);
			save = true;
		}catch (Exception e) {
			save = false;
			Log4JManager.error("No se pudo actualizar la cita odontológica: ",e);
		}		
		return save;
	}
}