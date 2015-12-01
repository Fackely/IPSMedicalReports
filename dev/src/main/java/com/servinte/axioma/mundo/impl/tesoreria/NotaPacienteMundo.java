package com.servinte.axioma.mundo.impl.tesoreria;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Set;

import org.apache.struts.util.MessageResources;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.ValoresPorDefecto;

import com.princetonsa.actionform.tesoreria.NotasPacientesForm;
import com.princetonsa.dto.administracion.DtoPersonas;
import com.princetonsa.mundo.facturacion.AbonosYDescuentos;
import com.servinte.axioma.dao.fabrica.TesoreriaFabricaDAO;
import com.servinte.axioma.dao.interfaz.tesoreria.INotaPacienteDAO;
import com.servinte.axioma.dto.tesoreria.DTONotaPaciente;
import com.servinte.axioma.dto.tesoreria.DtoBusquedaNotasPacientePorRango;
import com.servinte.axioma.dto.tesoreria.DtoConceptoNotasPacientes;
import com.servinte.axioma.dto.tesoreria.DtoConsultaNotasDevolucionAbonosPacientePorRango;
import com.servinte.axioma.dto.tesoreria.DtoInfoIngresoPacienteControlarAbonoPacientes;
import com.servinte.axioma.dto.tesoreria.DtoNotasPorNaturaleza;
import com.servinte.axioma.dto.tesoreria.DtoResumenNotasPaciente;
import com.servinte.axioma.mundo.fabrica.AdministracionFabricaMundo;
import com.servinte.axioma.mundo.fabrica.TesoreriaFabricaMundo;
import com.servinte.axioma.mundo.fabrica.odontologia.manejopaciente.ManejoPacienteFabricaMundo;
import com.servinte.axioma.mundo.interfaz.administracion.ICentroAtencionMundo;
import com.servinte.axioma.mundo.interfaz.administracion.IInstitucionesMundo;
import com.servinte.axioma.mundo.interfaz.manejoPaciente.IIngresosMundo;
import com.servinte.axioma.mundo.interfaz.manejoPaciente.IPacientesMundo;
import com.servinte.axioma.mundo.interfaz.tesoreria.IConceptoNotasPacientesMundo;
import com.servinte.axioma.mundo.interfaz.tesoreria.IMovimientosAbonosMundo;
import com.servinte.axioma.mundo.interfaz.tesoreria.INotaPacienteMundo;
import com.servinte.axioma.orm.CentroAtencion;
import com.servinte.axioma.orm.ConceptoNotaPaciente;
import com.servinte.axioma.orm.DetalleNotaPaciente;
import com.servinte.axioma.orm.Ingresos;
import com.servinte.axioma.orm.NotaPaciente;
import com.servinte.axioma.orm.Pacientes;
import com.servinte.axioma.orm.Personas;
import com.servinte.axioma.orm.Usuarios;

/**
 * Define la logica de negocio relaciona con 
 * NotaPaciente
 * @author diecorqu
 * @see INotaPacienteMundo
 */
public class NotaPacienteMundo implements INotaPacienteMundo {

	INotaPacienteDAO dao;
	ICentroAtencionMundo centroAtencionMundo;
	IIngresosMundo ingresosMundo;
	IConceptoNotasPacientesMundo conceptoNotasPacientesMundo;
	IPacientesMundo pacientesMundo;
	IInstitucionesMundo institucionMundo;
	
	/** * Contiene la lista de mensajes correspondiente a esta forma */
	MessageResources fuenteMensaje = MessageResources.getMessageResources("com.servinte.mensajes.tesoreria.NotasPacientesForm");
	
	public NotaPacienteMundo() {
		dao = TesoreriaFabricaDAO.crearNotaPacienteDAO();
		centroAtencionMundo = AdministracionFabricaMundo.crearCentroAtencionMundo();
		ingresosMundo = ManejoPacienteFabricaMundo.crearIngresosMundo();
		conceptoNotasPacientesMundo = TesoreriaFabricaMundo.crearConceptoNotasPacientes();
		pacientesMundo = ManejoPacienteFabricaMundo.crearPacientesMundo();
		institucionMundo = AdministracionFabricaMundo.crearInstitucionesMundo();
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.tesoreria.INotaPacienteMundo#guardarNotaPaciente(com.servinte.axioma.orm.NotaPaciente)
	 */
	@Override
	public boolean guardarNotaPaciente(NotaPaciente notaPaciente) {
		return dao.guardarNotaPaciente(notaPaciente);
	}

	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.tesoreria.INotaPacienteMundo#eliminarNotaPaciente(com.servinte.axioma.orm.NotaPaciente)
	 */
	@Override
	public boolean eliminarNotaPaciente(NotaPaciente notaPaciente) {
		return dao.eliminarNotaPaciente(notaPaciente);
	}

	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.tesoreria.INotaPacienteMundo#modificarNotaPaciente(com.servinte.axioma.orm.NotaPaciente)
	 */
	@Override
	public NotaPaciente modificarNotaPaciente(NotaPaciente notaPaciente) {
		return dao.modificarNotaPaciente(notaPaciente);
	}

	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.tesoreria.INotaPacienteMundo#findById(long)
	 */
	@Override
	public NotaPaciente findById(long codigo) {
		return dao.findById(codigo);
	}

	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.tesoreria.INotaPacienteMundo#obtenerCentrosAtencionInstitucion()
	 */
	public ArrayList<CentroAtencion> obtenerCentrosAtencionInstitucion() {
		return centroAtencionMundo.listarActivos();
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.tesoreria.INotaPacienteMundo#obtenerCentrosAtencionInstitucion()
	 */
	public CentroAtencion obtenerCentroAtencion(int codigo) {
		return centroAtencionMundo.buscarPorCodigo(codigo);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.tesoreria.INotaPacienteMundo#obtenerCentrosAtencionActivosUsuario(java.lang.String)
	 */
	@Override
	public ArrayList<CentroAtencion> obtenerCentrosAtencionActivosUsuario(
			String loginUsuario) {
		return centroAtencionMundo.listarCentrosAtencionActivosUsuario(loginUsuario);
	}
	
	/* (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.tesoreria.IRecibosCajaMundo#obtenerRecibosXMovimientoCaja(com.servinte.axioma.orm.MovimientosCaja)
	 */
	@Override
	public String validarSiTieneMovimientosAbonos(int codigoPaciente){

		IMovimientosAbonosMundo movimientosAbonosMundo = TesoreriaFabricaMundo.crearMovimientosAbonosMundo();
		return movimientosAbonosMundo.validarSiTieneMovimientosAbonos(codigoPaciente);
		//return devolucionAbonoDAO.validarSiTieneMovimientosAbonos(codigoPaciente);
	}
	
	@Override
	public ArrayList<DTONotaPaciente> obtenerDevolucionAbonoPorInstitucion(int codigoInstitucion){
		return dao.obtenerDevolucionAbonoPorInstitucion(codigoInstitucion);
	}
	
	@Override
	public ArrayList<DtoConsultaNotasDevolucionAbonosPacientePorRango> buscarNotaDevolucionAbonosPacienteRango(
			DtoConsultaNotasDevolucionAbonosPacientePorRango dtoFiltro, 
			ArrayList<Integer> listaConsecutivosCA) {
		return dao.buscarNotaDevolucionAbonosPacienteRango(dtoFiltro,listaConsecutivosCA);
	}
		
	@Override
	public  ArrayList<DtoInfoIngresoPacienteControlarAbonoPacientes> cargarIngresosPorPaciente(int codigoPaciente, String parametroGeneralControlarAbonoPacientesXIngreso){
			return ingresosMundo.cargarIngresosPorPaciente(codigoPaciente,parametroGeneralControlarAbonoPacientesXIngreso);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.tesoreria.INotaPacienteMundo#cargarAbonoDisponiblePorPaciente(int)
	 */
	@Override
	public ArrayList<DtoInfoIngresoPacienteControlarAbonoPacientes> cargarAbonoDisponiblePorPaciente(
			int codigoPaciente) {
		return pacientesMundo.cargarAbonoDisponiblePorPaciente(codigoPaciente);
	}
	
	@Override
	public ArrayList<DTONotaPaciente> obtenerDevolucionAbonoPacienteConsecutivo(int codigoPaciente, BigDecimal consecutivo, String naturaleza, String parametroManejoEspecialInstiOdontologicas) {
		return dao.obtenerDevolucionAbonoPacienteConsecutivo(codigoPaciente,consecutivo,naturaleza,parametroManejoEspecialInstiOdontologicas);
	}
	
	@Override

	public int insertarMovimientoAbonos(Connection con,int codigoPaciente,int codigoDocumento,
				int tipoDocumento,double valor,int institucion, Integer ingreso, int codigoCentroAtencion)
	{	    
			AbonosYDescuentos abonosYDescuentos = new AbonosYDescuentos();
			return abonosYDescuentos.insertarMovimientoAbonos(con,codigoPaciente,codigoDocumento,tipoDocumento,valor,institucion,ingreso, codigoCentroAtencion);
	}

	/**
	 * Guarda en cascada en las tablas Devolucion Abono y Detalle Devolucion Abono
	 * 
	 * @param 
	 * @return S/N
	 */
	
	public boolean guardarDevolucionAbono (BigDecimal consecutivo,  
										   String observaciones, Pacientes paciente, Usuarios usuario, 
										   CentroAtencion centroAtencionOrigen, CentroAtencion centroAtencionRegistro,
										   String naturalezaNota, DtoConceptoNotasPacientes dtoConceptoNota,
										   ArrayList<DtoInfoIngresoPacienteControlarAbonoPacientes> dtoInfoIngresoPacienteControlarAbonoPacientes){
	
		
		/*
		 * Tabla Papa que genera el ORM de Hibernate
		 */
		
		NotaPaciente notaPaciente  = new NotaPaciente();
		
		notaPaciente.setConsecutivo(consecutivo);
		notaPaciente.setFecha(UtilidadFecha.getFechaActualTipoBD());
		notaPaciente.setHora(UtilidadFecha.getHoraActual());
		notaPaciente.setObservaciones(observaciones);
		notaPaciente.setPacientes(paciente);
		notaPaciente.setUsuarios(usuario);
		notaPaciente.setCentroAtencionByCentroAtencionOrigen(centroAtencionOrigen);
		notaPaciente.setCentroAtencionByCentroAtencionRegistro(centroAtencionRegistro);
		notaPaciente.setNaturaleza(naturalezaNota);
		
		ConceptoNotaPaciente concepto = new ConceptoNotaPaciente();
		concepto.setCodigopk(dtoConceptoNota.getCodigoPk());
		concepto.setCodigo(dtoConceptoNota.getCodigo());
		concepto.setDescripcion(dtoConceptoNota.getNaturaleza());
		concepto.setNaturaleza(dtoConceptoNota.getNaturaleza());
		concepto.setActivo(dtoConceptoNota.getActivo());
		
		notaPaciente.setConceptoNotaPaciente(concepto);
   
		/*
		 * Recorriendo el DTO (que tiene la informacion) para llenar el tabla hija DetalleDevolucionAbono (generada desde hibernate)
		 * y crear el Hashset 
		 * Para unirla con la tabla papa DevolucionAbono
		 */
		
		HashSet<DetalleNotaPaciente> setDetalleDevolucionAbono = new HashSet<DetalleNotaPaciente>();

		if(dtoInfoIngresoPacienteControlarAbonoPacientes.get(0).getIdIngresos() != ConstantesBD.codigoNuncaValido){
			
			for(DtoInfoIngresoPacienteControlarAbonoPacientes detalleNota : dtoInfoIngresoPacienteControlarAbonoPacientes)
			{
				Ingresos ingresos = new Ingresos();
				//ingresos.setConsecutivo(ingreso.getConsecutivoIngresos());
				ingresos.setId(detalleNota.getIdIngresos());
				
				DetalleNotaPaciente detalleNotaPaciente = new DetalleNotaPaciente();
				detalleNotaPaciente.setNotaPaciente(notaPaciente);
				detalleNotaPaciente.setIngresos(ingresos);
				detalleNotaPaciente.setValor(new BigDecimal(detalleNota.getValorDevolucion()));
				setDetalleDevolucionAbono.add(detalleNotaPaciente);
			}
			
			/*
			 * Uniendo tabla devolucionAbono con DetalleDevolucionAbono
			 */
			
			notaPaciente.setDetalleNotaPacientes(setDetalleDevolucionAbono);
		}else{
			
			for(DtoInfoIngresoPacienteControlarAbonoPacientes ingreso:dtoInfoIngresoPacienteControlarAbonoPacientes)
			{
				DetalleNotaPaciente detalleDevolucionAbono = new DetalleNotaPaciente();
				detalleDevolucionAbono.setNotaPaciente(notaPaciente);
				detalleDevolucionAbono.setValor(new BigDecimal(ingreso.getValorDevolucion()));
				setDetalleDevolucionAbono.add(detalleDevolucionAbono);
			}
			
			/*
			 * Uniendo tabla devolucionAbono con DetalleDevolucionAbono
			 */
			
			notaPaciente.setDetalleNotaPacientes(setDetalleDevolucionAbono);
		}

		
		return dao.guardarNotaPaciente(notaPaciente);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.tesoreria.INotaPacienteMundo#listarConceptoNotaPacientexNaturaleza(java.lang.String)
	 */
	@Override
	public ArrayList<DtoConceptoNotasPacientes> listarConceptoNotaPacientexNaturaleza(String naturalezaNota) {
		DtoConceptoNotasPacientes dtoConceptoNotasPacientes = new DtoConceptoNotasPacientes();
		dtoConceptoNotasPacientes.setNaturaleza(naturalezaNota);
		return conceptoNotasPacientesMundo.listarConceptoNotasPacientesBusquedaAvanzada(dtoConceptoNotasPacientes);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.tesoreria.INotaPacienteMundo#listarConceptoNotaPacientexNaturaleza(java.lang.String)
	 */
	@Override
	public ArrayList<DtoConceptoNotasPacientes> listarConceptoNotaPacientexNaturalezaEstadoActivo(String naturalezaNota) {
		DtoConceptoNotasPacientes dtoConceptoNotasPacientes = new DtoConceptoNotasPacientes();
		dtoConceptoNotasPacientes.setNaturaleza(naturalezaNota);
		dtoConceptoNotasPacientes.setActivo(ConstantesBD.acronimoSi);
		return conceptoNotasPacientesMundo.listarConceptoNotasPacientesBusquedaAvanzada(dtoConceptoNotasPacientes);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.tesoreria.INotaPacienteMundo#listarConceptosNotaPaciente(java.lang.String)
	 */
	@Override
	public ArrayList<DtoConceptoNotasPacientes> listarConceptosNotaPaciente() {
		return conceptoNotasPacientesMundo.listarConceptoNotasPacientes();
	}

	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.tesoreria.INotaPacienteMundo#consultarNotasPacientePorRango(com.servinte.axioma.mundo.impl.tesoreria.DtoBusquedaNotasPacientePorRango)
	 */
	@Override
	public LinkedHashMap<String, DtoNotasPorNaturaleza> consultarNotasPacientePorRango(
			DtoBusquedaNotasPacientePorRango dtoFiltro, boolean esInstitucionMultiempresa, 
			String nombreInstitucion, boolean controlaAbonoPacientePorNumIngreso,
			boolean manejoEspecialInstOdonto, NotasPacientesForm forma) {
		ArrayList<DtoResumenNotasPaciente> listaDtoResumenNotasPaciente = 
			dao.consultarNotasPacientePorRango(dtoFiltro, esInstitucionMultiempresa, controlaAbonoPacientePorNumIngreso);
		forma.setListaDtoResumenNotasPaciente(listaDtoResumenNotasPaciente);
		return cargarMapaResultadosNotasPaciente(listaDtoResumenNotasPaciente, esInstitucionMultiempresa, nombreInstitucion);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.tesoreria.INotaPacienteMundo#consultarNotasPacientePorPaciente(long, boolean)
	 */
	@Override
	public LinkedHashMap<String, DtoNotasPorNaturaleza> consultarNotasPacientePorPaciente(
			int codigoPaciente, boolean esInstitucionMultiempresa, String nombreInstitucion, 
			boolean controlaAbonoPacientePorNumIngreso, NotasPacientesForm forma) {
		ArrayList<DtoResumenNotasPaciente> listaDtoResumenNotasPaciente = 
			dao.consultarNotasPacientePorPaciente(codigoPaciente, esInstitucionMultiempresa, controlaAbonoPacientePorNumIngreso);
		forma.setListaDtoResumenNotasPaciente(listaDtoResumenNotasPaciente);
		return cargarMapaResultadosNotasPaciente(listaDtoResumenNotasPaciente, esInstitucionMultiempresa, nombreInstitucion);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.tesoreria.INotaPacienteMundo#obtenerNotaPacienteConsecutivo(long)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public DTONotaPaciente obtenerNotaPacienteConsecutivo(long consecutivoNota, 
			boolean controlaAbonoPacientePorNumIngreso) {
		DTONotaPaciente dtoNotaPaciente = new DTONotaPaciente();
		ArrayList<DtoInfoIngresoPacienteControlarAbonoPacientes> listaDtoInfoDetalle = 
			new ArrayList<DtoInfoIngresoPacienteControlarAbonoPacientes>();
		DtoInfoIngresoPacienteControlarAbonoPacientes dtoInfoDetalle = null;
		NotaPaciente notaPaciente = this.findById(consecutivoNota);
		double totalValorNota = 0;
		
		if (notaPaciente != null) {
			Set<DetalleNotaPaciente> detalle = notaPaciente.getDetalleNotaPacientes();
			for (DetalleNotaPaciente detalleNotaPaciente : detalle) {
				dtoInfoDetalle = new DtoInfoIngresoPacienteControlarAbonoPacientes();
				if (controlaAbonoPacientePorNumIngreso) {
					dtoInfoDetalle.setCentroAtencionIngresos(detalleNotaPaciente.getIngresos().getCentroAtencion().getConsecutivo());
					dtoInfoDetalle.setConsecutivoIngresos(detalleNotaPaciente.getIngresos().getConsecutivo());
					dtoInfoDetalle.setNombreCentroAtencionIngresos(detalleNotaPaciente.getIngresos().getCentroAtencion().getDescripcion());
				}
				dtoInfoDetalle.setValorDevolucion(detalleNotaPaciente.getValor().doubleValue());
				totalValorNota += detalleNotaPaciente.getValor().doubleValue();
				listaDtoInfoDetalle.add(dtoInfoDetalle);
			}
			dtoNotaPaciente.setListaDtoInfoIngresoPacienteControlarAbonoPacientes(listaDtoInfoDetalle);
			dtoNotaPaciente.setConsecutivo(notaPaciente.getConsecutivo());
			dtoNotaPaciente.setFecha(UtilidadFecha.conversionFormatoFechaAAp(notaPaciente.getFecha().toString()));
			dtoNotaPaciente.setHora(notaPaciente.getHora());
			dtoNotaPaciente.setCentroAtencionOrigen(notaPaciente.getCentroAtencionByCentroAtencionOrigen().getConsecutivo());
			dtoNotaPaciente.setNombreCentroAtencionOrigen(notaPaciente.getCentroAtencionByCentroAtencionOrigen().getDescripcion());
			dtoNotaPaciente.setNaturalezaNota((String)ValoresPorDefecto.getIntegridadDominio(notaPaciente.getNaturaleza()));
			dtoNotaPaciente.setDescripcionConcepto(notaPaciente.getConceptoNotaPaciente().getDescripcion());
			dtoNotaPaciente.setObservaciones(notaPaciente.getObservaciones());
			dtoNotaPaciente.setUsuarioGeneraNota(this.obtenerNombreUsuarioLogin(notaPaciente.getUsuarios()));
			dtoNotaPaciente.setTotalValorNotaPaciente(UtilidadTexto.formatearValores(totalValorNota));
			dtoNotaPaciente.setNombreCompletoPaciente(this.obtenerNombrePaciente(notaPaciente.getPacientes()));
			dtoNotaPaciente.setIdentificacionPaciente(this.obtenerIdentificacionPaciente(notaPaciente.getPacientes()));
		} 
		return dtoNotaPaciente;
	}
	
	/**
	 * 
	 * @param listaDtoResumenNotasPaciente
	 * @param esMultiInstitucion
	 * @param nombreInstitucion
	 * @return
	 */
	private LinkedHashMap<String, DtoNotasPorNaturaleza> cargarMapaResultadosNotasPaciente(
			ArrayList<DtoResumenNotasPaciente> listaDtoResumenNotasPaciente, boolean esInstitucionMultiempresa, 
			String nombreInstitucion) {
		
		LinkedHashMap<String, DtoNotasPorNaturaleza> mapaResultados = 
			new LinkedHashMap<String, DtoNotasPorNaturaleza>();
		
		if (!listaDtoResumenNotasPaciente.isEmpty()) {
			if (esInstitucionMultiempresa) {
				for (DtoResumenNotasPaciente dtoResumenNotasPaciente : listaDtoResumenNotasPaciente) {
					if (dtoResumenNotasPaciente.getNaturalezaNotaPaciente().equals(ConstantesIntegridadDominio.acronimoDebito)) {
						if (!mapaResultados.containsKey(dtoResumenNotasPaciente.getNombreEmpresaInstitucion())) {
							DtoNotasPorNaturaleza notasPacientePorNaturaleza = new DtoNotasPorNaturaleza();
							ArrayList<DtoResumenNotasPaciente> resumenNotasDebito = new ArrayList<DtoResumenNotasPaciente>();
							ArrayList<DtoResumenNotasPaciente> resumenNotasCredito = new ArrayList<DtoResumenNotasPaciente>();
							resumenNotasDebito.add(dtoResumenNotasPaciente);
							notasPacientePorNaturaleza.setDtoResumenNotasDebito(resumenNotasDebito);
							notasPacientePorNaturaleza.setDtoResumenNotasCredito(resumenNotasCredito);
							mapaResultados.put(dtoResumenNotasPaciente.getNombreEmpresaInstitucion(), notasPacientePorNaturaleza);
						} else {
							mapaResultados.get(dtoResumenNotasPaciente.getNombreEmpresaInstitucion()).getDtoResumenNotasDebito().add(dtoResumenNotasPaciente);
						}
					} else if (dtoResumenNotasPaciente.getNaturalezaNotaPaciente().equals(ConstantesIntegridadDominio.acronimoCredito)) {
						if (!mapaResultados.containsKey(dtoResumenNotasPaciente.getNombreEmpresaInstitucion())) {
							DtoNotasPorNaturaleza notasPacientePorNaturaleza = new DtoNotasPorNaturaleza();
							ArrayList<DtoResumenNotasPaciente> resumenNotasDebito = new ArrayList<DtoResumenNotasPaciente>();
							ArrayList<DtoResumenNotasPaciente> resumenNotasCredito = new ArrayList<DtoResumenNotasPaciente>();
							resumenNotasCredito.add(dtoResumenNotasPaciente);
							notasPacientePorNaturaleza.setDtoResumenNotasDebito(resumenNotasDebito);
							notasPacientePorNaturaleza.setDtoResumenNotasCredito(resumenNotasCredito);
							mapaResultados.put(dtoResumenNotasPaciente.getNombreEmpresaInstitucion(), notasPacientePorNaturaleza);
						} else {
							mapaResultados.get(dtoResumenNotasPaciente.getNombreEmpresaInstitucion()).getDtoResumenNotasCredito().add(dtoResumenNotasPaciente);
						}
					}
				}
			} else {
				DtoNotasPorNaturaleza notasPacientePorNaturaleza = new DtoNotasPorNaturaleza();
				ArrayList<DtoResumenNotasPaciente> resumenNotasDebito = new ArrayList<DtoResumenNotasPaciente>();
				ArrayList<DtoResumenNotasPaciente> resumenNotasCredito = new ArrayList<DtoResumenNotasPaciente>();
				for (DtoResumenNotasPaciente dtoResumenNotasPaciente : listaDtoResumenNotasPaciente) {
					if (dtoResumenNotasPaciente.getNaturalezaNotaPaciente().equals(ConstantesIntegridadDominio.acronimoDebito)) {
						resumenNotasDebito.add(dtoResumenNotasPaciente);
					} else if (dtoResumenNotasPaciente.getNaturalezaNotaPaciente().equals(ConstantesIntegridadDominio.acronimoCredito)) {
						resumenNotasCredito.add(dtoResumenNotasPaciente);
					}
				}
				notasPacientePorNaturaleza.setDtoResumenNotasDebito(resumenNotasDebito);
				notasPacientePorNaturaleza.setDtoResumenNotasCredito(resumenNotasCredito);
				mapaResultados.put(nombreInstitucion, notasPacientePorNaturaleza);
			}
		}
		return mapaResultados;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.tesoreria.INotaPacienteMundo#obtenerIngresoPaciente(int)
	 */
	@Override
	public int obtenerIngresoPaciente(int codigoPaciente) {
		return ingresosMundo.consultarIdIngresosPacienteEstadoAbierto(codigoPaciente);
	}
	
	
	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.tesoreria.INotaPacienteMundo#obtenerNombrePaciente(com.servinte.axioma.orm.Pacientes)
	 */
	@Override
	public String obtenerNombrePaciente(Pacientes paciente) {
		String nombreCompletoPaciente = "";
		if (paciente != null) {
			Personas persona = paciente.getPersonas();
			nombreCompletoPaciente = (!UtilidadTexto.isEmpty(persona.getPrimerNombre())) ? 
					UtilidadTexto.convertirPrimeraLetraCapital(persona.getPrimerNombre()) : "";
			nombreCompletoPaciente += (!UtilidadTexto.isEmpty(persona.getSegundoNombre())) ? 
					" " + UtilidadTexto.convertirPrimeraLetraCapital(persona.getSegundoNombre()) : "";		
			nombreCompletoPaciente += (!UtilidadTexto.isEmpty(persona.getPrimerApellido())) ? 
					" " + UtilidadTexto.convertirPrimeraLetraCapital(persona.getPrimerApellido()) : "";		
			nombreCompletoPaciente += (!UtilidadTexto.isEmpty(persona.getSegundoApellido())) ? 
					" " + UtilidadTexto.convertirPrimeraLetraCapital(persona.getSegundoApellido()) : "";		
		}
		return nombreCompletoPaciente;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.tesoreria.INotaPacienteMundo#obtenerCentroAtencionDuenioPaciente(int)
	 */
	@Override
	public DtoPersonas obtenerDatosPersona(int codigoPersona) {
		return pacientesMundo.obtenerDatosPaciente(codigoPersona);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.tesoreria.INotaPacienteMundo#obtenerCentroAtencionDuenioPaciente(int)
	 */
	@Override
	public String obtenerDatosCentroAtencionDuenioPaciente(int codigoCentroAtencion) {
		CentroAtencion centroAtencion = centroAtencionMundo.buscarPorCodigo(codigoCentroAtencion);
		if (centroAtencion != null) {
			return fuenteMensaje.getMessage("notasPaciente.labelDireccion") + 
				": " + ((!UtilidadTexto.isEmpty(centroAtencion.getDireccion())) ? centroAtencion.getDireccion() : "") + ", " + 
				fuenteMensaje.getMessage("notasPaciente.labelTelefono") +
				": " + ((!UtilidadTexto.isEmpty(centroAtencion.getTelefono())) ? centroAtencion.getTelefono() : ""); 
		} else {
			return "";
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.tesoreria.INotaPacienteMundo#obtenerIdentificacionPaciente(com.servinte.axioma.orm.Pacientes)
	 */
	@Override
	public String obtenerIdentificacionPaciente(Pacientes paciente) {
		String idPaciente = "";
		if (paciente != null) {
			idPaciente = paciente.getPersonas().getTiposIdentificacion().getAcronimo() + " " +
						 paciente.getPersonas().getNumeroIdentificacion();
		}
		return idPaciente;
	}
	
	/**
	 * 
	 * @param usuario
	 * @return
	 */
	private String obtenerNombreUsuarioLogin(Usuarios usuario) {
		String nombreCompletoUsuarioLogin = "";
		if (usuario != null) {
			nombreCompletoUsuarioLogin = (!UtilidadTexto.isEmpty(usuario.getPersonas().getPrimerNombre())) ? 
					usuario.getPersonas().getPrimerNombre() : "";
			nombreCompletoUsuarioLogin += (!UtilidadTexto.isEmpty(usuario.getPersonas().getSegundoNombre())) ? 
					" " + usuario.getPersonas().getSegundoNombre() : "";		
			nombreCompletoUsuarioLogin += (!UtilidadTexto.isEmpty(usuario.getPersonas().getPrimerApellido())) ? 
					" " + usuario.getPersonas().getPrimerApellido() : "";		
			nombreCompletoUsuarioLogin += (!UtilidadTexto.isEmpty(usuario.getPersonas().getSegundoApellido())) ? 
					" " + usuario.getPersonas().getSegundoApellido() : "";		
			nombreCompletoUsuarioLogin += " (" + usuario.getLogin() + ")";		
		}
		return nombreCompletoUsuarioLogin;
	}

	
		
}
