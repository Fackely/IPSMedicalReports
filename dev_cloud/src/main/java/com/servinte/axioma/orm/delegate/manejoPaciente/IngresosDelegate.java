package com.servinte.axioma.orm.delegate.manejoPaciente;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.axioma.util.log.Log4JManager;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.type.Type;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.InfoDatos;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.ValoresPorDefecto;

import com.princetonsa.dto.manejoPaciente.DtoIngresos;
import com.princetonsa.dto.odontologia.DtoConsolidadoReporteIngresosOdonto;
import com.princetonsa.dto.odontologia.DtoIngresosOdontologicos;
import com.princetonsa.dto.odontologia.DtoReporteIngresosOdontologicos;
import com.princetonsa.dto.tesoreria.DtoInfoIngresoTrasladoAbonoPaciente;
import com.servinte.axioma.dto.tesoreria.DtoInfoIngresoPacienteControlarAbonoPacientes;
import com.servinte.axioma.orm.CitasOdontologicas;
import com.servinte.axioma.orm.Ingresos;
import com.servinte.axioma.orm.IngresosHome;
import com.servinte.axioma.orm.Pacientes;
import com.servinte.axioma.orm.PresupuestoOdontologico;


@SuppressWarnings({"unchecked"})
public class IngresosDelegate extends IngresosHome {
	
	/**
	 * Retorna los ingresos de un paciente
	 * 
	 * @param codPaciente
	 * @return
	 */
	public ArrayList<Ingresos> listarCuentasPorIngreso (int codPaciente){
		
		return (ArrayList<Ingresos>) sessionFactory.getCurrentSession()
				.createCriteria(Ingresos.class)
				.add(Expression.eq("pacientes.codigoPaciente",codPaciente))
				.list();
	}
	
	/**
	 * Retorna los ingresos de un paciente.
	 * El parametro General Controlar Abono Pacientes X Ingreso define si se debe mostrar detallado cada uno de los 
	 * ingresos con su valor o si se debe listar el totalizado de estos para el paciente dado
	 * 
	 * @param codPaciente
	 * @param parametroGeneralControlarAbonoPacientesXIngreso
	 * @return
	 */
	public ArrayList<DtoInfoIngresoPacienteControlarAbonoPacientes> cargarIngresosPorPaciente(int codPaciente, String parametroGeneralControlarAbonoPacientesXIngreso) {
	
		/*DetachedCriteria citasOdonto = DetachedCriteria.forClass(CitasOdontologicas.class) 
		 .setProjection( Property.forName("pacientes.codigoPaciente")); */
	
		ArrayList<DtoInfoIngresoPacienteControlarAbonoPacientes> listaSinSaldosEnCero = 
			new ArrayList<DtoInfoIngresoPacienteControlarAbonoPacientes>();
		ArrayList<DtoInfoIngresoPacienteControlarAbonoPacientes> listaQuery = 
			new ArrayList<DtoInfoIngresoPacienteControlarAbonoPacientes>();
		Criteria criteria = sessionFactory.getCurrentSession()
		.createCriteria(Ingresos.class, "ingresos")
		.createAlias("pacientes", "pacientes")
		.createAlias("pacientes.personas", "personas");

		if (parametroGeneralControlarAbonoPacientesXIngreso.equals(ConstantesBD.acronimoSi)){
			criteria.setProjection(Projections.projectionList()
					.add( Projections.property("ingresos.id"),"idIngresos")
					.add( Projections.property("ingresos.consecutivo")	, "consecutivoIngresos" )
					.add( Projections.property("ingresos.centroAtencion.consecutivo"), "centroAtencionIngresos" )
					.add( Projections.sqlProjection("getnomcentroatencion(this_.centro_atencion) AS nombreCentroAtencionIngresos", new String[]{"nombreCentroAtencionIngresos"}, new Type[]{StandardBasicTypes.STRING}))
					.add( Projections.sqlProjection("getabonodisponible(this_.codigo_paciente, id) AS saldoActual", new String[]{"saldoActual"}, new Type[]{StandardBasicTypes.DOUBLE})));
			
			criteria.add(Restrictions.eq("ingresos.pacientes.codigoPaciente", codPaciente))
			.setResultTransformer(Transformers.aliasToBean(DtoInfoIngresoPacienteControlarAbonoPacientes.class));
			
			listaQuery = (ArrayList<DtoInfoIngresoPacienteControlarAbonoPacientes>)criteria.list();
		
		/*
		 * Lista que filtra y quita los saldos en Cero que se van a mostrar
		 */
			for (int m =0; m <listaQuery.size(); m++ ) {
			if(listaQuery.get(m).getSaldoActual() > 0){ 
				listaSinSaldosEnCero.add(listaQuery.get(m));
			}
		}
			return listaSinSaldosEnCero;
		} else {
			criteria.setProjection(Projections.distinct(Projections.projectionList()
					.add( Projections.sqlProjection("getnomcentroatencion(this_.centro_atencion) AS nombreCentroAtencionIngresos", new String[]{"nombreCentroAtencionIngresos"}, new Type[]{StandardBasicTypes.STRING}))
					.add( Projections.sqlProjection("getabonodisponible(this_.codigo_paciente, null) AS saldoActual", new String[]{"saldoActual"}, new Type[]{StandardBasicTypes.DOUBLE}))));
		
			criteria.add(Restrictions.eq("ingresos.pacientes.codigoPaciente", codPaciente));
			criteria.setResultTransformer(Transformers.aliasToBean(DtoInfoIngresoPacienteControlarAbonoPacientes.class));

			return (ArrayList<DtoInfoIngresoPacienteControlarAbonoPacientes>)criteria.list();
		}
	}
	
	/**
	 * Retorna los ingresos de un paciente.
	 * El parametro listarPorIngreso define si se debe mostrar detallado cada uno de los 
	 * ingresos con su valor o si se debe listar el totalizado de estos para el paciente dado
	 * 
	 * @param codPaciente
	 * @return
	 */
	public List<DtoInfoIngresoTrasladoAbonoPaciente> obtenerIngresosParaTrasladoPorPaciente (int codPaciente, boolean listarPorIngreso)
	{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Ingresos.class, "ingreso");
		ProjectionList projectionList = Projections.projectionList();
		
		if(listarPorIngreso)
		{
			// QUERY USADO: SELECT id, centro_atencion, getabonodisponible(codigo_paciente, id), estado from ingresos where codigo_paciente=?;
			criteria.createAlias("centroAtencion"	, "centroAtencion");
			criteria.createAlias("pacientes"		, "pac");
			
			projectionList.add( Projections.property("centroAtencion.consecutivo")	,"centroAtencion");
			projectionList.add( Projections.property("centroAtencion.descripcion")	,"nombreCentroAtencion" );
			projectionList.add( Projections.property("ingreso.id")					,"idIngreso" );
			projectionList.add( Projections.property("ingreso.estado")				,"estadoIngreso" );
			projectionList.add( Projections.property("pac.codigoPaciente")			,"codigoPaciente" );
			projectionList.add( Projections.sqlProjection("getabonodisponible(this_.codigo_paciente, id) AS abonoDisponible", new String[]{"abonoDisponible"}, new Type[]{StandardBasicTypes.DOUBLE}));
			
			criteria.addOrder( Order.desc("fechaIngreso") );
			criteria.addOrder( Order.desc("horaIngreso") );
		}
		else
		{
			/*
			 * Puede ocurrir el caso de que no se tenga ingreso, en este punto el resultado retornara valores nulos
			 * para los tipos de datos primitivos ingreso.id y  centroAtencion.consecutivo, Por esta razon DtoInfoIngresoTrasladoAbonoPaciente
			 * recibe datos de tipo Object para poder realizar el cast adecuado segun el tipo de dato que recibe, ya que en el
			 * caso de tener ingresos retornara tipos de datos Integer.
			 */
			// QUERY USADO: SELECT id, getabonodisponible(codigo_paciente, id), from pacientes where codigo_paciente=?;
			criteria = sessionFactory.getCurrentSession().createCriteria(Pacientes.class, "pac");
			criteria.createAlias("ingresoses"				, "ingreso",Criteria.LEFT_JOIN);
			criteria.createAlias("ingreso.centroAtencion"	, "centroAtencion",Criteria.LEFT_JOIN);
			
			projectionList.add( Projections.property("centroAtencion.consecutivo")	,"centroAtencionObj" );
			projectionList.add( Projections.property("centroAtencion.descripcion")	,"nombreCentroAtencion");
			projectionList.add( Projections.property("ingreso.id")					,"idIngresoObj" );
			projectionList.add( Projections.property("ingreso.estado")				,"estadoIngreso" );
			projectionList.add( Projections.property("pac.codigoPaciente")			,"codigoPaciente" );
			projectionList.add( Projections.sqlProjection("getabonodisponible(this_.codigo_paciente, null) AS abonoDisponible", new String[]{"abonoDisponible"}, new Type[]{StandardBasicTypes.DOUBLE}));
		}
		
		List<DtoInfoIngresoTrasladoAbonoPaciente> listaResultado = (ArrayList<DtoInfoIngresoTrasladoAbonoPaciente>) criteria
				.add(Restrictions.eq("pac.codigoPaciente", codPaciente))
				.setProjection(projectionList)
				.setResultTransformer( Transformers.aliasToBean(DtoInfoIngresoTrasladoAbonoPaciente.class))
				.list();
		
		List<DtoInfoIngresoTrasladoAbonoPaciente> listaResultadoTotal = new ArrayList<DtoInfoIngresoTrasladoAbonoPaciente>();
		for (DtoInfoIngresoTrasladoAbonoPaciente resultado : listaResultado) 
		{
			if(resultado.getAbonoDisponible() > 0)
			{
				listaResultadoTotal.add(resultado);
			}
		}
		return listaResultadoTotal;
	}
	
	
	
	/**
	 * Retorna los ingresos de un paciente en el estado indicado
	 * 
	 * @param codPaciente
	 * @param listaEstadosIngreso
	 * @return
	 */
	public List<Ingresos> obtenerIngresosPacientePorEstado (int codPaciente, String[] listaEstadosIngreso)
	{
		return 
			(ArrayList<Ingresos>) sessionFactory.getCurrentSession()
				.createCriteria(Ingresos.class)
				.createAlias("pacientes", "pacientes")
				.add(Restrictions.eq("pacientes.codigoPaciente",codPaciente))
				.add(Restrictions.in("estado",listaEstadosIngreso))
			.list();
	}
	
	
	
	/**
	 * Retorna el ultimo ingreso en estado abierto del paciente
	 * Este metodo debe llamarse luego de validar luego que el paciente tenga ingresos 
	 * en estado abierto @see (List<Ingresos> obtenerIngresosPacientePorEstado (int codPaciente, String[] listaEstadosIngreso)).
	 * El valor de abonoDisponible retornado es el valor totalizado de todos los ingresos.
	 * Si el paciente no tiene un centro de atencion duenio asociado esta consulta retornara vacia.
	 * El boolean parametroManejoEspecialInstiOdontologicas indica cual es el Centro de Atencion que debe ser cargado.
	 * 
	 * @param codPaciente
	 * @param parametroManejoEspecialInstiOdontologicas
	 * @return
	 */
	public DtoInfoIngresoTrasladoAbonoPaciente obtenerUltimoIngresoAbiertoPaciente (int codPaciente, boolean parametroManejoEspecialInstiOdontologicas)
	{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Ingresos.class, "ingreso");
		ProjectionList projectionList = Projections.projectionList();
		
		String centroAtencion = "";
		if(parametroManejoEspecialInstiOdontologicas){
			centroAtencion = "pac.centroAtencionByCentroAtencionDuenio";// centro atencion = dueño del paciente
		}else{
			centroAtencion = "centroAtencion";							// centro atencion = ultimo ingreso en estado abierto
		}
			
		projectionList.add( Projections.property("centroAtencion.consecutivo")	,"centroAtencion")
			.add( Projections.property("centroAtencion.descripcion")			,"nombreCentroAtencion")
			.add( Projections.property("ingreso.id")							,"idIngreso")
			.add( Projections.property("ingreso.estado")						,"estadoIngreso")
			.add( Projections.property("pac.codigoPaciente")					,"codigoPaciente")
			.add( Projections.sqlProjection("getabonodisponible(this_.codigo_paciente, null) AS abonoDisponible", new String[]{"abonoDisponible"}, new Type[]{StandardBasicTypes.DOUBLE}));
		
		return (DtoInfoIngresoTrasladoAbonoPaciente) criteria
				.createAlias(centroAtencion	, "centroAtencion", criteria.LEFT_JOIN)
				.createAlias("pacientes"		, "pac")
				.add(Restrictions.eq("pac.codigoPaciente", codPaciente))
				.add(Restrictions.eq("estado", ConstantesIntegridadDominio.acronimoEstadoAbierto))
				.setProjection(projectionList)
				.setResultTransformer( Transformers.aliasToBean(DtoInfoIngresoTrasladoAbonoPaciente.class))
				.addOrder( Order.desc("fechaIngreso") )
				.addOrder( Order.desc("horaIngreso") )
				.setMaxResults(1)
				.uniqueResult();
	}
	
	
	/**
	 * Este m&eacute;todo se encarga de consultar los ingresos odontol&oacute;gicos
	 * en estado abierto seg&uacute;n los criterios especificados.
	 * @param dto
	 * @return
	 * 
	 * @author Yennifer Guerrero
	 */
	public List<Integer> consultarIngresosOdontoEstadoAbierto(
			DtoReporteIngresosOdontologicos dto){
		
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Ingresos.class,"ingresos")
		.createAlias("ingresos.centroAtencion", "centroAtencion")
		.createAlias("centroAtencion.ciudades", "ciudades")
		.createAlias("ciudades.paises", "paises")
		.createAlias("centroAtencion.regionesCobertura", "regionesCobertura")
		.createAlias("centroAtencion.empresasInstitucion", "empresaInstitucion")
		.createAlias("ingresos.pacientes", "pacientes")
		.createAlias("pacientes.personas", "personas");
		
		if (!UtilidadTexto.isEmpty(dto.getSexoPaciente()) && !dto.getSexoPaciente().equals(ConstantesBD.codigoNuncaValido + "")) {
			criteria.createAlias("personas.sexo", "sexo");
		}
		
		if (!UtilidadTexto.isEmpty(dto.getLogin()) && !dto.getLogin().trim().equals(ConstantesBD.codigoNuncaValido+"") && 
				!dto.getLogin().trim().equals("Todos")) {
			criteria.createAlias("pacientes.citasOdontologicases", "citaOdonto");
			criteria.createAlias("citaOdonto.valoracionesOdontos", "valoraciones");
			criteria.createAlias("valoraciones.usuarios", "usuarioModifica");
		}
		
		criteria.add(Restrictions.eq("ingresos.estado", ConstantesIntegridadDominio.acronimoEstadoAbierto))
		.add(Restrictions.eq("ingresos.tipoIngreso", ConstantesIntegridadDominio.acronimoTipoIngresoOdontologico));
		
		if(dto.getFechaInicial()!=null && dto.getFechaFinal() !=null){
			criteria.add(Restrictions.between("ingresos.fechaIngreso",
					dto.getFechaInicial(), dto.getFechaFinal()));
		}
		if (!UtilidadTexto.isEmpty(dto.getCodigoPaisResidencia())) {
			criteria.add(Restrictions.eq("paises.codigoPais", dto.getCodigoPaisResidencia()));
		}
		if (!UtilidadTexto.isEmpty(dto.getCiudadDeptoPais()) && !dto.getCiudadDeptoPais().trim().equals(ConstantesBD.codigoNuncaValido+"")) {
			
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
		if (dto.getCodigoEmpresaInstitucion() > 0) {
			criteria.add(Restrictions.eq("empresaInstitucion.codigo",dto.getCodigoEmpresaInstitucion()));
		}
		if (dto.getConsecutivoCentroAtencion()!= null && dto.getConsecutivoCentroAtencion()!= ConstantesBD.codigoNuncaValido ) {
			criteria.add(Restrictions.eq("centroAtencion.consecutivo",dto.getConsecutivoCentroAtencion()));
		}
		if (!UtilidadTexto.isEmpty(dto.getRangoEdadInicial()) &&
				!UtilidadTexto.isEmpty(dto.getRangoEdadFinal())) {
			
			Date rangoFechaInicial = UtilidadFecha.conversionFormatoFechaStringDate(dto.getRangoEdadInicial());
			Date rangoFechaFinal = UtilidadFecha.conversionFormatoFechaStringDate(dto.getRangoEdadFinal());
			
			criteria.add(Restrictions.between("personas.fechaNacimiento",
					rangoFechaFinal, rangoFechaInicial));
		}
		
		if (!UtilidadTexto.isEmpty(dto.getSexoPaciente()) && !dto.getSexoPaciente().equals(ConstantesBD.codigoNuncaValido + "")) {
		
			int sexoPaciente = Integer.parseInt(dto.getSexoPaciente());
			
			criteria.add(Restrictions.eq("sexo.codigo", sexoPaciente));
		}
		
		if (!UtilidadTexto.isEmpty(dto.getLogin()) && !dto.getLogin().trim().equals(ConstantesBD.codigoNuncaValido+"") && 
				!dto.getLogin().trim().equals("Todos")) {
			criteria.add(Restrictions.eq("usuarioModifica.login", dto.getLogin()));
		}
		
		criteria.setProjection(Projections.projectionList()
				.add(Projections.property("ingresos.id"))
				)
				
		.addOrder( Order.asc("centroAtencion.descripcion") )
		.addOrder( Order.asc("empresaInstitucion.codigo"))
		.addOrder( Order.asc("paises.codigoPais"));
		
		return criteria.list();
		
	}
	
	/**
	 * Este m&eacute;todo se encarga de obtener los ingresos
	 * sin citas odontol&oacute;gicas.
	 * @param ingresos
	 * @return
	 * 
	 * @author Yennifer Guerrero
	 */
	public ArrayList<DtoIngresosOdontologicos> obtenerIngresosSinCitasOdonto(List<Integer> ingresos){
		
		DetachedCriteria citasOdonto = DetachedCriteria.forClass(CitasOdontologicas.class) 
		 .setProjection( Property.forName("pacientes.codigoPaciente")); 
		
		Criteria criteria = sessionFactory.getCurrentSession()
		.createCriteria(Ingresos.class, "ingresos")
		.createAlias("pacientes", "pacientes")
		.createAlias("ingresos.centroAtencion", "centroAtencion")
		.createAlias("centroAtencion.ciudades", "ciudades")
		.createAlias("ciudades.paises", "paises")
		.createAlias("centroAtencion.regionesCobertura", "regionesCobertura")
		.createAlias("centroAtencion.empresasInstitucion", "empresaInstitucion")
		.createAlias("pacientes.personas", "personas")
		.createAlias("personas.tiposIdentificacion", "tipoId")
		.createAlias("personas.sexo", "sexo", Criteria.LEFT_JOIN)
		.createAlias("centroAtencion.instituciones", "institucion");
	
		criteria.setProjection(Projections.distinct(Projections.projectionList()
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
				.add( Projections.property("personas.segundoApellido"), "segundoApellido")
				.add( Projections.property("personas.telefonoFijo"), "telefonoFijo")
				.add( Projections.property("personas.telefonoCelular"), "telefonoCelular")
				.add( Projections.property("personas.telefono"), "telefono")
				.add( Projections.property("personas.fechaNacimiento"), "fechaNacimiento")
				.add( Projections.property("sexo.nombre"), "sexoPaciente")
				));
		
		criteria.add( Property.forName("pacientes.codigoPaciente").notIn(citasOdonto) )
		
		.add(Restrictions.in("ingresos.id", ingresos))
		.setResultTransformer(Transformers.aliasToBean(DtoIngresosOdontologicos.class));
		
		criteria.addOrder( Order.asc("tipoId.acronimo") );
		criteria.addOrder( Order.asc("personas.numeroIdentificacion") );
		
		ArrayList<DtoIngresosOdontologicos> lista=(ArrayList)criteria.list();
		
		
		return lista;
		
	}
	
	/**
	 * Este m&eacute;todo se encarga de obtener los ingresos odontológicos sin presupuesto.
	 * @return
	 *
	 * @author Yennifer Guerrero
	 * @param listaUltimaCitaPorPaciente 
	 */
	public ArrayList<DtoIngresosOdontologicos> obtenerIngresosSinPresupuesto(
			List<Integer> ingresosConValIni, List<Long> listaUltimaCitaPorPaciente){
		
		DetachedCriteria presupuestoOdonto = DetachedCriteria.forClass(PresupuestoOdontologico.class) 
		.setProjection(Property.forName("ingresos.id"));

		Criteria criteria = sessionFactory.getCurrentSession()
		.createCriteria(Ingresos.class, "ingreso")
		.createAlias("ingreso.pacientes", "pacientes")
		.createAlias("ingreso.centroAtencion", "centroAtencion")
		.createAlias("centroAtencion.ciudades", "ciudades")
		.createAlias("ciudades.paises", "paises")
		.createAlias("centroAtencion.regionesCobertura", "regionesCobertura")
		.createAlias("centroAtencion.empresasInstitucion", "empresaInstitucion")
		.createAlias("pacientes.personas", "personas")
		.createAlias("personas.tiposIdentificacion", "tipoId")
		.createAlias("personas.sexo", "sexo", Criteria.LEFT_JOIN)
		.createAlias("centroAtencion.instituciones", "institucion")
		.createAlias("pacientes.citasOdontologicases", "citaOdonto", Criteria.LEFT_JOIN)
		.createAlias("citaOdonto.valoracionesOdontos", "valoraciones", Criteria.LEFT_JOIN)
		.createAlias("valoraciones.usuarios", "usuarioModifica", Criteria.LEFT_JOIN)
		
		.setProjection(Projections.distinct(Projections.projectionList()
				.add( Projections.property("ingreso.id"), "idIngreso")
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
				.add( Projections.property("personas.segundoApellido"), "segundoApellido")
				.add( Projections.property("personas.fechaNacimiento"), "fechaNacimiento")
				.add( Projections.property("sexo.nombre"), "sexoPaciente")
				.add( Projections.property("usuarioModifica.login"), "loginProfesional")
				
		))
		
			.add(Restrictions.in("ingreso.id", ingresosConValIni))
			.add(Restrictions.in("citaOdonto.codigoPk", listaUltimaCitaPorPaciente))
			.add(Property.forName("ingreso.id").notIn(presupuestoOdonto))
		
			.setResultTransformer(Transformers.aliasToBean(DtoIngresosOdontologicos.class));
		
			criteria.addOrder( Order.asc("tipoId.acronimo") );
			criteria.addOrder( Order.asc("personas.numeroIdentificacion") );
			
		ArrayList<DtoIngresosOdontologicos> lista=(ArrayList)criteria.list();
		
		return lista;
	}
	
	
	/**
	 * Este m&eacute;todo se encarga de obtener las ingresos de tipo
	 * valoraci&oacute;n inicial en estado atendida que tienen presupuestos
	 * odontol&oacute;gicos.
	 * @return
	 *
	 * @author Yennifer Guerrero
	 * @param listaUltimaCitaPorPaciente 
	 */
	public ArrayList<DtoIngresosOdontologicos> obtenerIngresosValIniConPresupuesto(List<Integer> ingresos,
			DtoReporteIngresosOdontologicos filtroIngresos, List<Long> listaUltimaCitaPorPaciente){
		
		Criteria criteria = sessionFactory.getCurrentSession()
		
		.createCriteria(Ingresos.class, "ingresos")
		.createAlias("pacientes", "pacientes")
		.createAlias("ingresos.centroAtencion", "centroAtencion")
		.createAlias("centroAtencion.ciudades", "ciudades")
		.createAlias("ciudades.paises", "paises")
		.createAlias("centroAtencion.regionesCobertura", "regionesCobertura")
		.createAlias("centroAtencion.empresasInstitucion", "empresaInstitucion")
		.createAlias("pacientes.personas", "personas")
		.createAlias("personas.tiposIdentificacion", "tipoId")
		.createAlias("centroAtencion.instituciones", "institucion")
		.createAlias("personas.sexo", "sexo", Criteria.LEFT_JOIN)
		.createAlias("ingresos.presupuestoOdontologicos", "presupuesto")
		.createAlias("pacientes.citasOdontologicases", "citaOdonto", Criteria.INNER_JOIN)
		.createAlias("citaOdonto.valoracionesOdontos", "valoraciones", Criteria.INNER_JOIN)
		.createAlias("valoraciones.usuarios", "usuarioModifica", Criteria.INNER_JOIN);
		
		if (!UtilidadTexto.isEmpty(filtroIngresos.getCodigoEspecialidad()) && 
				filtroIngresos.getCodigoEspecialidad() > 0  ||
				(!UtilidadTexto.isEmpty(filtroIngresos.getCodigoPaqueteOdonto()) &&
				filtroIngresos.getCodigoPaqueteOdonto() > 0)) {
			criteria.createAlias("presupuesto.especialidades", "especialidad");
		}
		
		if (!UtilidadTexto.isEmpty(filtroIngresos.getCodigoPaqueteOdonto()) &&
				filtroIngresos.getCodigoPaqueteOdonto() > 0) {
			criteria.createAlias("especialidad.paquetesOdontologicoses", "paquetes");
		}
		
		if(filtroIngresos.getCodigoPrograma() > 0 ||
				! UtilidadTexto.isEmpty(filtroIngresos.getServicio().getCodigo())){
			criteria.createAlias("presupuesto.presupuestoOdoProgServs", "presupserprog");
		}
		
		
		if(filtroIngresos.getCodigoPrograma() > 0){
			criteria.createAlias("presupserprog.programas", "programas");
		}
	
		if(! UtilidadTexto.isEmpty(filtroIngresos.getServicio().getCodigo())
				&& !filtroIngresos.getServicio().getCodigo().trim().equals(ConstantesBD.codigoNuncaValido+"")){
			criteria.createAlias("presupserprog.servicios", "servicios");
		}
		
		criteria.setProjection(Projections.distinct(Projections.projectionList()
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
				.add( Projections.property("personas.segundoApellido"), "segundoApellido")
				
				.add( Projections.property("presupuesto.codigoPk"), "codigoPkPresupuesto")
				.add( Projections.property("presupuesto.consecutivo"), "consecutivoPresupuesto")
				.add( Projections.property("presupuesto.fechaGeneracion"), "fechaGeneracionPresupuesto")
				.add( Projections.property("presupuesto.estado"), "estadoPresupuesto")
				.add( Projections.property("personas.fechaNacimiento"), "fechaNacimiento")
				.add( Projections.property("sexo.nombre"), "sexoPaciente")
				.add( Projections.property("usuarioModifica.login"), "loginProfesional")
				
			));
		
			if (!UtilidadTexto.isEmpty(filtroIngresos.getEstadoPresupuesto()) && !filtroIngresos.getEstadoPresupuesto().trim().equals(ConstantesBD.codigoNuncaValido+"") &&
					!filtroIngresos.getEstadoPresupuesto().equals(ConstantesIntegridadDominio.acronimoContratado)) {
				
				criteria.add(Restrictions.eq("presupuesto.estado", filtroIngresos.getEstadoPresupuesto()));
				
			}else if (filtroIngresos.getEstadoPresupuesto().equals(ConstantesIntegridadDominio.acronimoContratado) && 
					(UtilidadTexto.isEmpty(filtroIngresos.getIndicativoContrato()) || 
							filtroIngresos.getIndicativoContrato().trim().equals(ConstantesBD.codigoNuncaValido+""))) {
				
				String[] listadoIndicativo = new String[]{
						ConstantesIntegridadDominio.acronimoContratadoContratado,
						ConstantesIntegridadDominio.acronimoContratadoSuspendidoTemporalmente,
						ConstantesIntegridadDominio.acronimoContratadoTerminado,
						ConstantesIntegridadDominio.acronimoContratadoCancelado};
				
				criteria.add(Restrictions.in("presupuesto.estado", listadoIndicativo));
				
			}else if (!UtilidadTexto.isEmpty(filtroIngresos.getIndicativoContrato()) && 
							!filtroIngresos.getIndicativoContrato().trim().equals(ConstantesBD.codigoNuncaValido+"")) {
				
				criteria.add(Restrictions.eq("presupuesto.estado", filtroIngresos.getIndicativoContrato()));
			} 
			
			
			if (filtroIngresos.getCodigoEspecialidad() > 0) {
				criteria.add(Restrictions.eq("especialidad.codigo", filtroIngresos.getCodigoEspecialidad()));
			}
			
			if (filtroIngresos.getCodigoPaqueteOdonto()>0) {
				criteria.add(Restrictions.eq("paquetes.codigoPk", filtroIngresos.getCodigoPaqueteOdonto()));
			}
			
			if (filtroIngresos.getCodigoPrograma() > 0) {
				criteria.add(Restrictions.eq("programas.codigo", filtroIngresos.getCodigoPrograma()));
			}
			
			if (!UtilidadTexto.isEmpty(filtroIngresos.getServicio().getCodigo()) && 
					!filtroIngresos.getServicio().getCodigo().trim().equals(ConstantesBD.codigoNuncaValido+"")) {
				
				
				int codigoServicio = Integer.parseInt(filtroIngresos.getServicio().getCodigo());
				
				
				criteria.add(Restrictions.eq("servicios.codigo", codigoServicio));
			}
			
		criteria.add(Restrictions.in("ingresos.id", ingresos));
		criteria.add(Restrictions.in("citaOdonto.codigoPk", listaUltimaCitaPorPaciente));
		
		criteria.setResultTransformer(Transformers.aliasToBean(DtoIngresosOdontologicos.class));
		
		criteria.addOrder( Order.asc("tipoId.acronimo") );
		criteria.addOrder( Order.asc("personas.numeroIdentificacion") );
		ArrayList<DtoIngresosOdontologicos> lista=(ArrayList)criteria.list();
		
		return lista;
	}
	
	
	/**
	 * Este m&eacute;todo se encarga de obtener el total de pacientes ingresados por
	 * un determinado centro de atencion.
	 * @param ingresosconsulta
	 * @return
	 *
	 * @author Yennifer Guerrero
	 */
	public int obtenerTotalPacientesIngresados (List<Integer> ingresosconsulta, int consecutivoCA){
		
		Criteria criteria = sessionFactory.getCurrentSession()
		.createCriteria(Ingresos.class, "ingresos")
		.createAlias("ingresos.pacientes", "pacientes")
		.createAlias("ingresos.centroAtencion", "centroAtencion")
		
		.setProjection(Projections.projectionList()
				.add(Projections.countDistinct("pacientes.codigoPaciente"))
		);
		
		criteria.add(Restrictions.eq("centroAtencion.consecutivo",consecutivoCA));
		criteria.add(Restrictions.in("ingresos.id", ingresosconsulta));
		
		List<Integer> lista = (ArrayList)criteria.list();
		
		int totalIngresos = lista.get(0);
		
		return totalIngresos;
	}
	
	/**
	 * Este m&eacute;todo se encarga de obtener el total de pacientes ingresados por
	 * un determinado centro de atencion.
	 * @param ingresosconsulta
	 * @return
	 *
	 * @author Yennifer Guerrero
	 */
	public int obtenerTotalPacientesSinValIni (List<Integer> ingresosconsulta, int consecutivoCA){
		
		Criteria criteria = sessionFactory.getCurrentSession()
		.createCriteria(Ingresos.class, "ingresos")
		.createAlias("ingresos.pacientes", "pacientes")
		.createAlias("ingresos.centroAtencion", "centroAtencion")
		
		.setProjection(Projections.projectionList()
				.add(Projections.countDistinct("pacientes.codigoPaciente"))
		);
		
		criteria.add(Restrictions.eq("centroAtencion.consecutivo",consecutivoCA));
		criteria.add(Restrictions.in("ingresos.id", ingresosconsulta));
		
		List<Integer> lista = (ArrayList)criteria.list();
		
		int totalIngresos = lista.get(0);
		
		return totalIngresos;
	}
	
	/**
	 * Este m&eacute;todo se encarga de obtener el total de pacientes ingresados por
	 * un determinado centro de atencion.
	 * @param ingresosconsulta
	 * @return
	 *
	 * @author Yennifer Guerrero
	 */
	public ArrayList<DtoConsolidadoReporteIngresosOdonto> obtenerTotalPacientes (List<Integer> ingresosConValIni, List<Integer> ingresosSinValIni, int consecutivoCA){
		
		Criteria criteria = sessionFactory.getCurrentSession()
		.createCriteria(Ingresos.class, "ingresos")
		.createAlias("ingresos.pacientes", "pacientes")
		.createAlias("ingresos.centroAtencion", "centroAtencion")
		
		.setProjection(Projections.projectionList()
				.add(Projections.countDistinct("pacientes.codigoPaciente"), "ayudanteTotalPacientesValIni")
				.add(Projections.property("pacientes.codigoPaciente"), "codigoPaciente")
				.add(Projections.groupProperty("pacientes.codigoPaciente"))
		);
		
		criteria.add(Restrictions.eq("centroAtencion.consecutivo",consecutivoCA));
		
		if (ingresosConValIni != null && ingresosConValIni.size() > 0) {
			criteria.add(Restrictions.in("ingresos.id", ingresosConValIni));
		}else if (ingresosSinValIni != null && ingresosSinValIni.size() > 0) {
			criteria.add(Restrictions.in("ingresos.id", ingresosSinValIni));
		}
		
		criteria.setResultTransformer(Transformers.aliasToBean(DtoConsolidadoReporteIngresosOdonto.class));
		
		ArrayList<DtoConsolidadoReporteIngresosOdonto> lista=(ArrayList)criteria.list();
		
		return lista;
	}
	
	
	/**
	 * Este m&eacute;todo se encarga de obtener el total de pacientes ingresados por
	 * un determinado centro de atencion.
	 * @param ingresosconsulta
	 * @return
	 *
	 * @author Yennifer Guerrero
	 */
	public int obtenerTotalPacientesPresupuesto (List<Integer> ingresosSinPresupuesto, List<Integer> ingresosConPresupuesto, int consecutivoCA){
		
		Criteria criteria = sessionFactory.getCurrentSession()
		.createCriteria(Ingresos.class, "ingresos")
		.createAlias("ingresos.centroAtencion", "centroAtencion")
		
		.setProjection(Projections.projectionList()
				.add(Projections.count("ingresos.id"))
		);
		
		criteria.add(Restrictions.eq("centroAtencion.consecutivo",consecutivoCA));
		
		if (ingresosConPresupuesto != null && ingresosConPresupuesto.size() > 0) {
			criteria.add(Restrictions.in("ingresos.id", ingresosConPresupuesto));
		}else if (ingresosSinPresupuesto != null && ingresosSinPresupuesto.size() > 0) {
			criteria.add(Restrictions.in("ingresos.id", ingresosSinPresupuesto));
		}else{
			return 0;
		}
		
		
		List<Integer> lista = (ArrayList)criteria.list();
		
		int totalIngresos = lista.get(0);
		
		return totalIngresos;
	}
	
	/**
	 * Este m&eacute;todo se encarga de obtener el totalizado por estado de 
	 * los ingresos con presupuesto.
	 * @param ingresos
	 * @return
	 *
	 * @author Yennifer Guerrero
	 */
	public ArrayList<DtoConsolidadoReporteIngresosOdonto> obtenerConsolidadoIngresosConPresupuesto(List<Integer> ingresosConPresupuesto,
			int consecutivoCA, DtoReporteIngresosOdontologicos filtroIngresos){
		
		Criteria criteria = sessionFactory.getCurrentSession()
		.createCriteria(Ingresos.class, "ingresos")
		.createAlias("ingresos.presupuestoOdontologicos", "presupuesto")
		.createAlias("ingresos.centroAtencion", "centroAtencion")
		
		.setProjection(Projections.projectionList()
				.add(Projections.property("presupuesto.estado"), "estadoPresupuesto")
				.add(Projections.count("presupuesto.estado"), "totalPresupuestoPorEstado")
				.add(Projections.groupProperty("presupuesto.estado")));
			
		
		criteria.add(Restrictions.eq("centroAtencion.consecutivo",consecutivoCA));
		criteria.add(Restrictions.in("ingresos.id", ingresosConPresupuesto));
		
		if (!UtilidadTexto.isEmpty(filtroIngresos.getEstadoPresupuesto()) && 
				!filtroIngresos.getEstadoPresupuesto().trim().equals(ConstantesBD.codigoNuncaValido + "")) {
			
			if (filtroIngresos.getEstadoPresupuesto().trim().equals(ConstantesIntegridadDominio.acronimoContratado)) {
				
				String[] listadoIndicativo = new String[]{
						ConstantesIntegridadDominio.acronimoContratadoContratado,
						ConstantesIntegridadDominio.acronimoContratadoSuspendidoTemporalmente,
						ConstantesIntegridadDominio.acronimoContratadoTerminado,
						ConstantesIntegridadDominio.acronimoContratadoCancelado};
				
				criteria.add(Restrictions.in("presupuesto.estado", listadoIndicativo));
				
			}else {
				criteria.add(Restrictions.eq("presupuesto.estado",filtroIngresos.getEstadoPresupuesto()));
			}
		}

		
		criteria.setResultTransformer(Transformers.aliasToBean(DtoConsolidadoReporteIngresosOdonto.class))
		.addOrder( Order.asc("presupuesto.estado") );
		ArrayList<DtoConsolidadoReporteIngresosOdonto> lista=(ArrayList)criteria.list();
		
		return lista;
		
	}
	
	
	
	
	
	
	
	/**
	 * Retorna el ultimo ingresodel paciente
	 * 
	 * @param codPaciente
	 * @return DtoInfoIngresoTrasladoAbonoPaciente
	 */
	public DtoInfoIngresoTrasladoAbonoPaciente obtenerUltimoIngresoPaciente (int codPaciente)
	{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Ingresos.class, "ingreso");
		ProjectionList projectionList = Projections.projectionList();
		
		String centroAtencion = "centroAtencion";							// centro atencion del ultimo ingreso
		
		projectionList.add( Projections.property("centroAtencion.consecutivo")	,"centroAtencion")
			.add( Projections.property("centroAtencion.descripcion")			,"nombreCentroAtencion")
			.add( Projections.property("ingreso.id")							,"idIngreso")
			.add( Projections.property("ingreso.estado")						,"estadoIngreso")
			.add( Projections.property("ingreso.fechaEgreso")					,"fechaEgreso")
			.add( Projections.property("ingreso.horaEgreso")					,"horaEgreso")
			.add( Projections.property("pac.codigoPaciente")					,"codigoPaciente")
			.add( Projections.sqlProjection("getabonodisponible(this_.codigo_paciente, null) AS abonoDisponible", new String[]{"abonoDisponible"}, new Type[]{StandardBasicTypes.DOUBLE}));
		
		return (DtoInfoIngresoTrasladoAbonoPaciente) criteria
				.createAlias(centroAtencion	, "centroAtencion")
				.createAlias("pacientes"		, "pac")
				.add(Restrictions.eq("pac.codigoPaciente", codPaciente))
				//.add(Restrictions.eq("estado", ConstantesIntegridadDominio.acronimoEstadoAbierto))
				.setProjection(projectionList)
				.setResultTransformer( Transformers.aliasToBean(DtoInfoIngresoTrasladoAbonoPaciente.class))
				.addOrder( Order.desc("fechaIngreso") )
				.addOrder( Order.desc("horaIngreso") )
				.setMaxResults(1)
				.uniqueResult();
	}
	
	/**
	 * Método encargado de obtener el id del ingreso abierto de un paciente
	 * determinado.
	 * 
	 * @param codigoPaciente
	 * @return
	 * 
	 * @author Luis Fernando Hincapié Ospina
	 */
	public int consultarIdIngresosPacienteEstadoAbierto(int codigoPaciente){
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Ingresos.class, "ingreso").setProjection(
				Projections.projectionList().add(Projections.property("ingreso.id")))
				.createAlias("ingreso.pacientes", "paciente")
				.add(Expression.eq("paciente.codigoPaciente", codigoPaciente))
				.add(Restrictions.eq("ingreso.estado", ConstantesIntegridadDominio.acronimoEstadoAbierto));
		Integer codigoIngreso = (Integer)criteria.uniqueResult();
		if (codigoIngreso != null) {
			return codigoIngreso;
		} else {
			return ConstantesBD.codigoNuncaValido;
		}
	}
	
	/**
	 * Método encargado de obtener los ingresos con Vía de Ingreso = CONSULTA EXTERNA, cuya fecha de atención 
	 * de las citas se encuentre dentro de las últimas 24 horas.
	 * 
	 * @param codigoPaciente
	 * @return List<DtoIngreso> ingresos
	 * 
	 * @author Ricardo Ruiz
	 */
	public List<DtoIngresos> consultarIngresosConsultaExternaPorFechaHoraEstadoCita(int codigoPaciente, Date fecha, String hora, int estadoCita){
		List<DtoIngresos> listaIngresos = new ArrayList<DtoIngresos>();
		try{
			String consulta="SELECT DISTINCT ing.id idIngreso, ing.consecutivo consecutivo, ing.fecha_ingreso fechaIngreso, ing.hora_ingreso horaIngreso, "+
									"vi.codigo codViaIngreso, vi.nombre nomViaIngreso, ca.codigo codCentroAtencion, ca.descripcion nomCentroAte, " +
									"cue.id codCuenta "+
							"FROM manejopaciente.ingresos ing "+
								"INNER JOIN manejopaciente.cuentas cue ON (cue.id_ingreso=ing.id) "+
								"INNER JOIN ordenes.solicitudes sol ON (sol.cuenta=cue.id) "+
								"INNER JOIN consultaexterna.servicios_cita sc ON (sc.numero_solicitud=sol.numero_solicitud) "+
								"INNER JOIN consultaexterna.cita cit ON (cit.codigo=sc.codigo_cita AND ing.codigo_paciente = cit.codigo_paciente) "+
								"INNER JOIN manejopaciente.vias_ingreso vi ON (vi.codigo=cue.via_ingreso) "+
								"INNER JOIN administracion.centro_atencion ca ON (ca.codigo=ing.centro_atencion) "+
							"WHERE ing.codigo_paciente= :codigoPaciente "+
								"AND cue.via_ingreso= :viaIngreso "+
								"AND cit.estado_cita = :estadoCita "+
								"AND (cit.fecha_modifica > :fecha "+
								"OR (cit.fecha_modifica = :fecha AND cit.hora_modifica >= :hora)) ";
			
			SQLQuery query = sessionFactory.getCurrentSession().createSQLQuery(consulta);
			query.setParameter("codigoPaciente", codigoPaciente, StandardBasicTypes.INTEGER);
			query.setParameter("fecha", fecha, StandardBasicTypes.DATE);
			query.setParameter("hora", hora, StandardBasicTypes.STRING);
			query.setParameter("estadoCita", estadoCita, StandardBasicTypes.INTEGER);
			query.setParameter("viaIngreso", ConstantesBD.codigoViaIngresoConsultaExterna, StandardBasicTypes.INTEGER);
			query.addScalar("idIngreso", StandardBasicTypes.INTEGER);
			query.addScalar("consecutivo", StandardBasicTypes.STRING);
			query.addScalar("fechaIngreso", StandardBasicTypes.DATE);
			query.addScalar("horaIngreso", StandardBasicTypes.STRING);
			query.addScalar("codViaIngreso", StandardBasicTypes.INTEGER);
			query.addScalar("nomViaIngreso", StandardBasicTypes.STRING);
			query.addScalar("codCentroAtencion", StandardBasicTypes.INTEGER);
			query.addScalar("nomCentroAte", StandardBasicTypes.STRING);
			query.addScalar("codCuenta", StandardBasicTypes.INTEGER);
			List<Object[]> resultado=query.list();
			for(Object[] result:resultado){
				DtoIngresos dtoIngreso=new DtoIngresos((Integer)result[0], (String) result[1], (Date) result[2],
												(String) result[3], (Integer) result[4], (String) result[5], 
												(Integer) result[6], (String) result[7], (Integer) result[8]);
				listaIngresos.add(dtoIngreso);
			}
		}
		catch (Exception e) {
			Log4JManager.error("ERROR ", e);
		}
		return listaIngresos;
		
	}
	
	/**
	 * Método encargado de obtener el último diagnostico principal correspondiente a la
	 * respuesta de procedimientos
	 * 
	 * @param codigoPaciente
	 * @return InfoDatos
	 * 
	 * @author Ricardo Ruiz
	 */
	public InfoDatos consultarUltimoDiagnosticoRespuestaSolicitudes(int codigoIngreso, int tipoSolicitud){
		InfoDatos respuesta= null;
		try{
			String consulta="SELECT dp.acronimo acronimoDiag, dp.tipo_cie tipoCieDiag, manejopaciente.getdiagnostico(dp.acronimo,dp.tipo_cie) nombreDiag " +
							"FROM cuentas cue "+ 
								"INNER JOIN solicitudes sol on (sol.cuenta=cue.id) "+
								"INNER JOIN res_sol_proc rsp on (rsp.numero_solicitud=sol.numero_solicitud) "+
								"INNER JOIN diag_procedimientos dp on (dp.codigo_respuesta=rsp.codigo) "+
							"WHERE cue.id_ingreso=:codigoIngreso "+
								"AND sol.tipo=:tipoSolicitud "+
								"AND dp.principal="+ValoresPorDefecto.getValorTrueParaConsultas()+" "+
							"ORDER BY rsp.fecha_ejecucion desc, rsp.hora_ejecucion desc ";
			
			SQLQuery query = sessionFactory.getCurrentSession().createSQLQuery(consulta);
			query.setParameter("codigoIngreso", codigoIngreso, StandardBasicTypes.INTEGER);
			query.setParameter("tipoSolicitud", tipoSolicitud, StandardBasicTypes.INTEGER);
			query.addScalar("acronimoDiag", StandardBasicTypes.STRING);
			query.addScalar("tipoCieDiag", StandardBasicTypes.INTEGER);
			query.addScalar("nombreDiag", StandardBasicTypes.STRING);
			List<Object[]> resultado=query.list();
			for(Object[] result:resultado){
				respuesta=new InfoDatos();
				respuesta.setDescripcion((String)result[0]);
				respuesta.setCodigo((Integer)result[1]);
				respuesta.setDescripcionInd((String) result[2]);
				break;
			}
		}
		catch (Exception e) {
			Log4JManager.error("ERROR ", e);
		}
		return respuesta;
		
	}
	
}
