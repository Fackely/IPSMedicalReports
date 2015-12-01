package com.servinte.axioma.orm.delegate.tesoreria;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.axioma.util.log.Log4JManager;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.type.Type;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadFecha;
import util.UtilidadTexto;

import com.servinte.axioma.dto.tesoreria.DTONotaPaciente;
import com.servinte.axioma.dto.tesoreria.DtoBusquedaNotasPacientePorRango;
import com.servinte.axioma.dto.tesoreria.DtoConsultaNotasDevolucionAbonosPacientePorRango;
import com.servinte.axioma.dto.tesoreria.DtoResumenNotasPaciente;
import com.servinte.axioma.orm.DetalleNotaPaciente;
import com.servinte.axioma.orm.NotaPaciente;
import com.servinte.axioma.orm.NotaPacienteHome;

/**
 * Clase encargada de ejecutar las transacciones del objeto 
 * {@link NotaPaciente} de la relación nota_paciente
 * @author diecorqu
 *  
 */
public class NotaPacienteDelegate extends NotaPacienteHome {

	/**
	 * Método encargado de persistir la entidad NotaPaciente
	 * @param NotaPaciente
	 * @return boolean - resultado de la operación
	 */
	public boolean guardarNotaPaciente(NotaPaciente notaPaciente) {
		boolean save = false;
		try{
			super.persist(notaPaciente);
			save = true;
		}catch (Exception e) {
			save = false;
			Log4JManager.error("No se pudo guardar la nota paciente con código: " + 
					notaPaciente.getCodigoPk(),e);
		}		
		return save;
	}

	/**
	 * Método encargado de eliminar un registro de NotaPaciente
	 * @param NotaPaciente
	 * @return boolean - resultado de la operación
	 */
	public boolean eliminarNotaPaciente(NotaPaciente notaPaciente) {
		boolean save = false;
		try{
			super.delete(notaPaciente);
			save = true;
		}catch (Exception e) {
			save = false;
			Log4JManager.error("No se pudo guardar la nota paciente con código: " + 
					notaPaciente.getCodigoPk(),e);
		}		
		return save;
	}

	/**
	 * Método encargado de modificar la entidad NotaPaciente
	 * @param NotaPaciente
	 * @return NotaPaciente
	 */
	public NotaPaciente modificarNotaPaciente(NotaPaciente notaPaciente) {
		NotaPaciente nota = null;
		try{
			nota = super.merge(notaPaciente);
		}catch (Exception e) {
			Log4JManager.error("No se pudo guardar la nota paciente con código: " + 
					notaPaciente.getCodigoPk(),e);
		}		
		return nota;
	}

	/**
	 * Método encargado de buscar una NotaPaciente por codigo
	 * @param codigo
	 * @return NotaPaciente
	 */
	public NotaPaciente findById(long codigo) {
		return super.findById(codigo);
	}
	
	/**
	 * Este Método se encarga de obtener las devoluciones de 
	 * abonos realizadas por institución.
	 * @param codigoInstitucion
	 * @return
	 * @author Yennifer Guerrero
	 */
	@SuppressWarnings("unchecked")// Conversión implícita inevitable
	public ArrayList<DTONotaPaciente> obtenerDevolucionAbonoPorInstitucion(int codigoInstitucion) {

		ProjectionList projectionList =  Projections.projectionList();
		Criteria criteria = sessionFactory.getCurrentSession()
				.createCriteria(NotaPaciente.class, "devolucionAbono")
				.createAlias("devolucionAbono.centroAtencionOrigen", "centroAtencion")
				.createAlias("centroAtencion.instituciones", "institucion");
				

		projectionList.add(Projections.property("devolucionAbono.codigoPk"), "codigoPk");
		
		if (codigoInstitucion > 0) {
			criteria.add(Restrictions.eq("institucion.codigo", codigoInstitucion ));
		}
		
		criteria.setProjection(projectionList);
		
		ArrayList<DTONotaPaciente> listadoDevoluciones = 
			(ArrayList<DTONotaPaciente>) criteria.list();
		
		return listadoDevoluciones;

	}
	
	
	/**
	 * Este Método se encarga de obtener las devoluciones de
	 * abonos filtrada por paciente y por consecutivo y el parametro general ManejoEspecialInstiOdontologicas
	 * @param codigoPaciente,consecutivo,parametroManejoEspecialInstiOdontologicas
	 * @return
	 * @author 
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })// Conversión implícita inevitable
	public ArrayList<DTONotaPaciente> obtenerDevolucionAbonoPacienteConsecutivo(int codigoPaciente, BigDecimal consecutivo, String naturaleza, String parametroManejoEspecialInstiOdontologicas) {
		ArrayList<DTONotaPaciente> lista = new ArrayList<DTONotaPaciente>();
		
		if (parametroManejoEspecialInstiOdontologicas.equals(ConstantesBD.acronimoSi)){
			Criteria criteria = sessionFactory.getCurrentSession()
			.createCriteria(NotaPaciente.class, "notaPaciente")//sobrenombre singular
			.createAlias("notaPaciente.detalleNotaPacientes", "detalleNotaPaciente")//sobrenombre paciente del anterior
			.createAlias("detalleNotaPacientes.ingresos", "ingreso")
			.createAlias("notaPaciente.pacientes", "paciente")
			.createAlias("notaPaciente.centroAtencionByCentroAtencionOrigen", "centroAtencionOrigen")
			.createAlias("notaPaciente.centroAtencionByCentroAtencionRegistro", "centroAtencionRegistro");
			
			criteria.setProjection(Projections.projectionList()
					.add( Projections.property("notaPaciente.consecutivo"),"consecutivo")
					.add( Projections.property("notaPaciente.fecha")	, "fechaIngresoNota" )
					.add( Projections.property("notaPaciente.hora")	, "hora" )
					.add( Projections.property("centroAtencionOrigen.consecutivo"), "centroAtencionOrigen" )
					.add( Projections.property("centroAtencionRegistro.consecutivo"), "centroAtencionRegistro" )
					.add( Projections.property("paciente.codigoPaciente")	, "codPaciente" )
					.add( Projections.property("notaPaciente.observaciones")	, "observaciones" )
					
					.add( Projections.property("detalleNotaPaciente.codigoPk"), "codigoPkDetalleNotaPaciente" )
					.add( Projections.property("detalleNotaPaciente.valor"), "valorNota" )
					.add( Projections.property("ingreso.id"), "ingreso" )
					.add( Projections.property("ingreso.consecutivo"), "consecutivoIngreso" )
					
					//.add( Projections.property("paciente.centroAtencionByCentroAtencionDuenio.consecutivo"), "centroAtencionByCentroAtencionDuenio" )
					//.add( Projections.sqlProjection("getnomcentroatencion(this_.centro_atencion) AS nombreCentroAtencionIngresos", new String[]{"nombreCentroAtencionIngresos"}, new Type[]{Hibernate.STRING}))
					.add( Projections.sqlProjection("getabonodisponible(ingreso2_.codigo_paciente, id) AS saldoActual", new String[]{"saldoActual"}, new Type[]{StandardBasicTypes.DOUBLE})));
					
	
			criteria.add(Restrictions.eq("paciente.codigoPaciente", codigoPaciente))
					.add(Restrictions.eq("notaPaciente.consecutivo", consecutivo))
					.add(Restrictions.eq("notaPaciente.naturaleza", naturaleza))
			.setResultTransformer(Transformers.aliasToBean(DTONotaPaciente.class));
			lista = (ArrayList)criteria.list();
		} else {
			Criteria criteria = sessionFactory.getCurrentSession()
				.createCriteria(NotaPaciente.class, "notaPaciente")//sobrenombre singular
				.createAlias("notaPaciente.detalleNotaPacientes", "detalleNotaPaciente")//sobrenombre paciente del anterior
				.createAlias("notaPaciente.pacientes", "paciente")
				.createAlias("notaPaciente.centroAtencionByCentroAtencionOrigen", "centroAtencionOrigen")
				.createAlias("notaPaciente.centroAtencionByCentroAtencionRegistro", "centroAtencionRegistro");
			
			criteria.setProjection(Projections.projectionList()
					.add( Projections.property("notaPaciente.consecutivo"),"consecutivo")
					.add( Projections.property("notaPaciente.fecha")	, "fechaIngresoNota" )
					.add( Projections.property("notaPaciente.hora")	, "hora" )
					.add( Projections.property("centroAtencionOrigen.consecutivo"), "centroAtencionOrigen" )
					.add( Projections.property("centroAtencionRegistro.consecutivo"), "centroAtencionRegistro" )
					.add( Projections.property("paciente.codigoPaciente")	, "codPaciente" )
					.add( Projections.property("notaPaciente.observaciones")	, "observaciones" )
					
					.add( Projections.property("detalleNotaPaciente.codigoPk"), "codigoPkDetalleNotaPaciente" )
					.add( Projections.property("detalleNotaPaciente.valor"), "valorNota" )
					
					.add( Projections.sqlProjection("getabonodisponible(paciente2_.codigo_paciente, null) AS saldoActual", new String[]{"saldoActual"}, new Type[]{StandardBasicTypes.DOUBLE})));
	
			criteria.add(Restrictions.eq("paciente.codigoPaciente", codigoPaciente))
					.add(Restrictions.eq("notaPaciente.consecutivo", consecutivo))
					.add(Restrictions.eq("notaPaciente.naturaleza", naturaleza))
			.setResultTransformer(Transformers.aliasToBean(DTONotaPaciente.class));
			lista = (ArrayList)criteria.list();
		}
		
		return lista;

	}
	
	

	/**
	 * Método encargado de obtener el listado de las instituciones por notas de
	 * devolución abonos paciente.
	 * 
	 * @param dtoFiltro
	 * @param listaConsecutivosCA
	 * @return listaInstitucion
	 * 
	 * @author Luis Fernando Hincapié Ospina
	 */
	@SuppressWarnings("unchecked")// Conversión implícita inevitable
	public ArrayList<DtoConsultaNotasDevolucionAbonosPacientePorRango> buscarNotaDevolucionAbonosPacienteRango(
			DtoConsultaNotasDevolucionAbonosPacientePorRango dtoFiltro, 
			ArrayList<Integer> listaConsecutivosCA) {

		ProjectionList projectionList =  Projections.projectionList();
		Criteria criteria = sessionFactory.getCurrentSession()
				.createCriteria(NotaPaciente.class, "devolucionAbono")
				.createAlias("devolucionAbono.detalleDevolucionAbonos", "detalleDevolucionAbono")
				.createAlias("devolucionAbono.usuarios", "usuario")
				.createAlias("detalleDevolucionAbono.ingresos", "ingreso")
				.createAlias("devolucionAbono.centroAtencion", "centroAtencion")
				.createAlias("centroAtencion.empresasInstitucion", "empresaInstitucion")
				.createAlias("devolucionAbono.pacientes", "paciente")
				.createAlias("paciente.personas", "persona")
				.createAlias("persona.tiposIdentificacion", "tipoIdentificacion");

		projectionList.add(Projections.property("empresaInstitucion.codigo"), "codigoEmpresaInstitucion");
		projectionList.add(Projections.property("devolucionAbono.consecutivo"), "nroNotaDevolucion");
		projectionList.add(Projections.property("devolucionAbono.fecha"), "fechaNotaDevolucion");
		projectionList.add(Projections.property("devolucionAbono.hora"), "horaNotaDevolucion");
		projectionList.add(Projections.property("empresaInstitucion.codigo"), "codigoEmpresaInstitucion");
		projectionList.add(Projections.property("empresaInstitucion.razonSocial"), "nombreEmpresaInstitucion");
		projectionList.add(Projections.property("centroAtencion.consecutivo"), "consecutivoCentroAtencion");
		projectionList.add(Projections.property("centroAtencion.descripcion"), "nombreCentroAtencion");
		projectionList.add(Projections.property("persona.primerNombre"), "primerNombrePaciente");
		projectionList.add(Projections.property("persona.segundoNombre"), "segundoNombrePaciente");
		projectionList.add(Projections.property("persona.primerApellido"), "primerApellidoPaciente");
		projectionList.add(Projections.property("persona.segundoApellido"), "segundoApellidoPaciente");
		projectionList.add(Projections.property("tipoIdentificacion.acronimo"), "tipoIdentificacion");
		projectionList.add(Projections.property("persona.numeroIdentificacion"), "numeroIdentificacion");
		projectionList.add(Projections.property("ingreso.consecutivo"), "ingresos");
		projectionList.add(Projections.property("usuario.login"), "login");
		projectionList.add(Projections.property("detalleDevolucionAbono.valorDevolucion"), "valorNota");
		projectionList.add(Projections.property("devolucionAbono.motivo"), "motivoNotaDevolucion");
		projectionList.add(Projections.property("paciente.centroAtencionByCentroAtencionDuenio.consecutivo"), "centroAtencionByCentroAtencionDuenio" );
		projectionList.add(Projections.sqlProjection("getnomcentroatencion(this_.centro_atencion) AS nombreCentroAtencionIngresos", new String[]{"nombreCentroAtencionIngresos"}, new Type[]{StandardBasicTypes.STRING}));
		
		if (!UtilidadTexto.isEmpty(UtilidadFecha.conversionFormatoFechaAAp(dtoFiltro.getFechaInicial()))
				&& !UtilidadTexto.isEmpty(UtilidadFecha.conversionFormatoFechaAAp(dtoFiltro.getFechaFinal()))) {
			criteria.add(Restrictions.between("devolucionAbono.fecha", dtoFiltro.getFechaInicial(), dtoFiltro.getFechaFinal()));
		}
		if (!dtoFiltro.getNumeroNotaDevolucionInicial().isEmpty()
				&& !dtoFiltro.getNumeroNotaDevolucionFinal().isEmpty()) {
			criteria.add(Restrictions.between("devolucionAbono.consecutivo", new BigDecimal(dtoFiltro.getNumeroNotaDevolucionInicial()), 
					new BigDecimal(dtoFiltro.getNumeroNotaDevolucionFinal())));
		}
		if (dtoFiltro.getCodigoEmpresaInstitucion() != ConstantesBD.codigoNuncaValidoLong) {
			criteria.add(Restrictions.eq("empresaInstitucion.codigo", dtoFiltro.getCodigoEmpresaInstitucion()));
		}
		if (!listaConsecutivosCA.isEmpty()) {
			criteria.add(Restrictions.in("centroAtencion.consecutivo", listaConsecutivosCA));
		}
		if (!dtoFiltro.getLogin().isEmpty() && !dtoFiltro.getLogin().equals(String.valueOf(ConstantesBD.codigoNuncaValido))) {
			criteria.add(Restrictions.eq("usuario.login", dtoFiltro.getLogin()));
		}

		criteria.setProjection(projectionList);

		criteria.addOrder(Order.asc("devolucionAbono.fecha"));
		criteria.addOrder(Order.asc("devolucionAbono.hora"));

		criteria.setResultTransformer(Transformers.aliasToBean(DtoConsultaNotasDevolucionAbonosPacientePorRango.class));

		ArrayList<DtoConsultaNotasDevolucionAbonosPacientePorRango> listaPlanesTratamientoPorEstado = 
			(ArrayList<DtoConsultaNotasDevolucionAbonosPacientePorRango>) criteria.list();

		return listaPlanesTratamientoPorEstado;

	}

	/**
	 * Método que permite consultar las Notas de Paciente por Rango
	 * @param DtoBusquedaNotasPacientePorRango
	 * @return ArrayList<DtoNotasPacientePorRango>
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<DtoResumenNotasPaciente> consultarNotasPacientePorRango(
			DtoBusquedaNotasPacientePorRango dtoFiltro, 
			boolean esInstitucionMultiempresa,
			boolean controlaAbonoPacientePorNumIngreso) {
		
		Criteria criteria = sessionFactory.getCurrentSession()
				.createCriteria(NotaPaciente.class, "notaPaciente")
				.createAlias("notaPaciente.detalleNotaPacientes", "detalleNotaPaciente")
				.createAlias("notaPaciente.centroAtencionByCentroAtencionOrigen", "centroAtencionOrigen")
				.createAlias("notaPaciente.centroAtencionByCentroAtencionRegistro", "centroAtencionRegistro")
				.createAlias("notaPaciente.conceptoNotaPaciente", "conceptoNotaPaciente")
				.createAlias("notaPaciente.usuarios", "usuario")
				.createAlias("notaPaciente.pacientes", "paciente")
				.createAlias("persona.tiposIdentificacion", "tipoIdentificacion")
				.createAlias("paciente.personas", "persona")
				.createAlias("paciente.centroAtencionByCentroAtencionDuenio", "centroAtencionDuenio", Criteria.LEFT_JOIN)
				.createAlias("centroAtencionDuenio.empresasInstitucion", "empresaInstitucionDuenio", Criteria.LEFT_JOIN);

		if (esInstitucionMultiempresa) {
			criteria.createAlias("centroAtencionOrigen.empresasInstitucion", "empresaInstitucion");
		} else {
			criteria.createAlias("centroAtencionOrigen.instituciones", "institucion");
		}
		
		ProjectionList projectionList =  Projections.projectionList();
		
		projectionList.add(Projections.property("notaPaciente.codigoPk"), "codigoPkNotaPaciente");
		projectionList.add(Projections.property("notaPaciente.consecutivo"), "nroNotaPaciente");
		projectionList.add(Projections.property("notaPaciente.naturaleza"), "naturalezaNotaPaciente");
		projectionList.add(Projections.property("notaPaciente.fecha"), "fechaNotaPaciente");
		projectionList.add(Projections.property("notaPaciente.hora"), "horaNotaPaciente");
		projectionList.add(Projections.property("notaPaciente.observaciones"), "observaciones");
		
		if (esInstitucionMultiempresa) {
			projectionList.add(Projections.property("empresaInstitucion.codigo"), "codigoEmpresaInstitucion");
			projectionList.add(Projections.property("empresaInstitucion.razonSocial"), "nombreEmpresaInstitucion");
		} else {
			projectionList.add(Projections.property("institucion.codigo"), "codigoEmpresaInstitucion");
			projectionList.add(Projections.property("institucion.razonSocial"), "nombreEmpresaInstitucion");
		}
		
		projectionList.add(Projections.property("centroAtencionOrigen.consecutivo"), "consecutivoCentroAtencionOrigen");
		projectionList.add(Projections.property("centroAtencionOrigen.descripcion"), "nombreCentroAtencionOrigen");
		projectionList.add(Projections.property("conceptoNotaPaciente.descripcion"), "descripcionConcepto");
		projectionList.add(Projections.property("persona.primerNombre"), "primerNombrePaciente");
		projectionList.add(Projections.property("persona.segundoNombre"), "segundoNombrePaciente");
		projectionList.add(Projections.property("persona.primerApellido"), "primerApellidoPaciente");
		projectionList.add(Projections.property("persona.segundoApellido"), "segundoApellidoPaciente");
		projectionList.add(Projections.property("tipoIdentificacion.acronimo"), "tipoIdentificacion");
		projectionList.add(Projections.property("persona.numeroIdentificacion"), "numeroIdentificacion");
		projectionList.add(Projections.property("usuario.login"), "login");
		projectionList.add(Projections.property("centroAtencionDuenio.consecutivo"), "consecutivoCentroAtencionByCentroAtencionDuenio" );
		projectionList.add(Projections.property("centroAtencionDuenio.descripcion"), "nombreCentroAtencionByCentroAtencionDuenio" );
		projectionList.add(Projections.property("empresaInstitucionDuenio.razonSocial"), "nombreInstitucionCentroAtencionDuenio" );
		//projectionList.add(Projections.sqlProjection("getnomcentroatencion(this_.centro_atencion) AS nombreCentroAtencionIngresos", new String[]{"nombreCentroAtencionIngresos"}, new Type[]{Hibernate.STRING}));
		

		if (dtoFiltro.getCodigoEmpresaInstitucion() != ConstantesBD.codigoNuncaValidoLong) {
			criteria.add(Restrictions.eq("empresaInstitucion.codigo", dtoFiltro.getCodigoEmpresaInstitucion()));
		}
		if (!dtoFiltro.getCodigosCentrosAtencion().isEmpty()) {
			criteria.add(Restrictions.in("centroAtencionOrigen.consecutivo", dtoFiltro.getCodigosCentrosAtencion()));
		}
		if (dtoFiltro.getFechaInicialGeneracion() != null
				&& dtoFiltro.getFechaFinalGeneracion() != null) {
			criteria.add(Restrictions.between("notaPaciente.fecha", dtoFiltro.getFechaInicialGeneracion(), dtoFiltro.getFechaFinalGeneracion()));
		}
		if (dtoFiltro.getNumeroInicialNota() != ConstantesBD.codigoNuncaValidoLong
				&& dtoFiltro.getNumeroFinalNota() != ConstantesBD.codigoNuncaValidoLong) {
			criteria.add(Restrictions.between("notaPaciente.consecutivo", new BigDecimal(dtoFiltro.getNumeroInicialNota()), 
					new BigDecimal(dtoFiltro.getNumeroFinalNota())));
		}
		if (!UtilidadTexto.isEmpty(dtoFiltro.getNaturalezaNota())) {
			if (dtoFiltro.getNaturalezaNota().equals(ConstantesIntegridadDominio.acronimoDebito) ||
					dtoFiltro.getNaturalezaNota().equals(ConstantesIntegridadDominio.acronimoCredito)) {
				criteria.add(Restrictions.eq("notaPaciente.naturaleza", dtoFiltro.getNaturalezaNota()));
			}
		}
		if (dtoFiltro.getCodigoPkConceptoNotasPaciente() != ConstantesBD.codigoNuncaValidoLong) {
			criteria.add(Restrictions.eq("conceptoNotaPaciente.codigopk", dtoFiltro.getCodigoPkConceptoNotasPaciente()));
		} 
		if (!UtilidadTexto.isEmpty(dtoFiltro.getUsuarioGeneraNotas()) && 
				!dtoFiltro.getUsuarioGeneraNotas().equals(String.valueOf(ConstantesBD.codigoNuncaValido))) {
			criteria.add(Restrictions.eq("usuario.login", dtoFiltro.getUsuarioGeneraNotas()));
		}

		criteria.setProjection(Projections.distinct(projectionList));

		criteria.addOrder(Order.asc("notaPaciente.fecha"));
		criteria.addOrder(Order.asc("notaPaciente.hora"));

		criteria.setResultTransformer(Transformers.aliasToBean(DtoResumenNotasPaciente.class));

		ArrayList<DtoResumenNotasPaciente> listaDtoResumenNotasPaciente = 
			(ArrayList<DtoResumenNotasPaciente>) criteria.list();
		
		for (DtoResumenNotasPaciente dtoResumenNotasPaciente : listaDtoResumenNotasPaciente) {
			String ingresos = "";
			double valorNota = 0;
			Criteria criteriaDetalle = sessionFactory.getCurrentSession()
				.createCriteria(DetalleNotaPaciente.class, "detalleNotaPaciente")
				.createAlias("detalleNotaPaciente.notaPaciente", "notaPaciente");
			if (controlaAbonoPacientePorNumIngreso) {
				criteriaDetalle.createAlias("detalleNotaPaciente.ingresos", "ingreso");
			}
			criteriaDetalle.add(Restrictions.eq("notaPaciente.codigoPk", dtoResumenNotasPaciente.getCodigoPkNotaPaciente()));
			ArrayList<DetalleNotaPaciente> detalle = (ArrayList<DetalleNotaPaciente>)criteriaDetalle.list();
			for (DetalleNotaPaciente detalleNotaPaciente : detalle) {
				if (controlaAbonoPacientePorNumIngreso) {
					ingresos += detalleNotaPaciente.getIngresos().getConsecutivo() + ", ";
				}
				valorNota += detalleNotaPaciente.getValor().doubleValue();
			}
			if (controlaAbonoPacientePorNumIngreso) {
				dtoResumenNotasPaciente.setIngresos(ingresos.substring(0, ingresos.lastIndexOf(",")));
			}	
			dtoResumenNotasPaciente.setValorNota(new BigDecimal(valorNota));
		}
		return listaDtoResumenNotasPaciente;
	}
	
	/**
	 * Método que permite consultar las Notas de Paciente por Paciente
	 * @param long codigoPaciente
	 * @param boolean esMultiInstitucion
	 * @return ArrayList<DtoNotasPacientePorRango>
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<DtoResumenNotasPaciente> consultarNotasPacientePorPaciente(int codigoPaciente, 
			boolean esMultiInstitucion,
			boolean controlaAbonoPacientePorNumIngreso) {
		
		Criteria criteria = sessionFactory.getCurrentSession()
				.createCriteria(NotaPaciente.class, "notaPaciente")
				.createAlias("notaPaciente.detalleNotaPacientes", "detalleNotaPaciente")
				.createAlias("notaPaciente.centroAtencionByCentroAtencionOrigen", "centroAtencionOrigen")
				.createAlias("notaPaciente.centroAtencionByCentroAtencionRegistro", "centroAtencionRegistro")
				.createAlias("notaPaciente.conceptoNotaPaciente", "conceptoNotaPaciente")
				.createAlias("notaPaciente.usuarios", "usuario")
				.createAlias("notaPaciente.pacientes", "paciente")
				.createAlias("paciente.personas", "persona")
				.createAlias("persona.tiposIdentificacion", "tipoIdentificacion");

		if (esMultiInstitucion) {
			criteria.createAlias("centroAtencionOrigen.empresasInstitucion", "empresaInstitucion");
		} else {
			criteria.createAlias("centroAtencionOrigen.instituciones", "institucion");
		}
		ProjectionList projectionList =  Projections.projectionList();
		
		projectionList.add(Projections.property("notaPaciente.codigoPk"), "codigoPkNotaPaciente");
		projectionList.add(Projections.property("notaPaciente.consecutivo"), "nroNotaPaciente");
		projectionList.add(Projections.property("notaPaciente.naturaleza"), "naturalezaNotaPaciente");
		projectionList.add(Projections.property("notaPaciente.fecha"), "fechaNotaPaciente");
		projectionList.add(Projections.property("notaPaciente.hora"), "horaNotaPaciente");
		projectionList.add(Projections.property("notaPaciente.observaciones"), "observaciones");
		
		if (esMultiInstitucion) {
			projectionList.add(Projections.property("empresaInstitucion.codigo"), "codigoEmpresaInstitucion");
			projectionList.add(Projections.property("empresaInstitucion.razonSocial"), "nombreEmpresaInstitucion");
		} else {
			projectionList.add(Projections.property("institucion.codigo"), "codigoEmpresaInstitucion");
			projectionList.add(Projections.property("institucion.razonSocial"), "nombreEmpresaInstitucion");
		}
		
		projectionList.add(Projections.property("centroAtencionOrigen.consecutivo"), "consecutivoCentroAtencionOrigen");
		projectionList.add(Projections.property("centroAtencionOrigen.descripcion"), "nombreCentroAtencionOrigen");
		projectionList.add(Projections.property("conceptoNotaPaciente.descripcion"), "descripcionConcepto");
		projectionList.add(Projections.property("persona.primerNombre"), "primerNombrePaciente");
		projectionList.add(Projections.property("persona.segundoNombre"), "segundoNombrePaciente");
		projectionList.add(Projections.property("persona.primerApellido"), "primerApellidoPaciente");
		projectionList.add(Projections.property("persona.segundoApellido"), "segundoApellidoPaciente");
		projectionList.add(Projections.property("tipoIdentificacion.acronimo"), "tipoIdentificacion");
		projectionList.add(Projections.property("persona.numeroIdentificacion"), "numeroIdentificacion");
		projectionList.add(Projections.property("usuario.login"), "login");

		criteria.add(Restrictions.eq("paciente.codigoPaciente", codigoPaciente));
		criteria.setProjection(Projections.distinct(projectionList));

		criteria.addOrder(Order.asc("notaPaciente.fecha"));
		criteria.addOrder(Order.asc("notaPaciente.hora"));

		criteria.setResultTransformer(Transformers.aliasToBean(DtoResumenNotasPaciente.class));

		ArrayList<DtoResumenNotasPaciente> listaDtoResumenNotasPaciente = 
			(ArrayList<DtoResumenNotasPaciente>) criteria.list();
		
		for (DtoResumenNotasPaciente dtoResumenNotasPaciente : listaDtoResumenNotasPaciente) {
			String ingresos = "";
			double valorIngresos = 0;
			Criteria criteriaDetalle = sessionFactory.getCurrentSession()
				.createCriteria(DetalleNotaPaciente.class, "detalleNotaPaciente")
				.createAlias("detalleNotaPaciente.notaPaciente", "notaPaciente");
			if (controlaAbonoPacientePorNumIngreso) {
				criteriaDetalle.createAlias("detalleNotaPaciente.ingresos", "ingreso");
			}	
			criteriaDetalle.add(Restrictions.eq("notaPaciente.codigoPk", dtoResumenNotasPaciente.getCodigoPkNotaPaciente()));
			ArrayList<DetalleNotaPaciente> detalle = (ArrayList<DetalleNotaPaciente>)criteriaDetalle.list();
			for (DetalleNotaPaciente detalleNotaPaciente : detalle) {
				if (controlaAbonoPacientePorNumIngreso) {
					ingresos += detalleNotaPaciente.getIngresos().getConsecutivo() + ", ";
				}
				valorIngresos += detalleNotaPaciente.getValor().doubleValue();
			}
			if (controlaAbonoPacientePorNumIngreso) {
				dtoResumenNotasPaciente.setIngresos(ingresos.substring(0, ingresos.lastIndexOf(",")));
			}
			dtoResumenNotasPaciente.setValorNota(new BigDecimal(valorIngresos));
		}
		return listaDtoResumenNotasPaciente;
	}
}
