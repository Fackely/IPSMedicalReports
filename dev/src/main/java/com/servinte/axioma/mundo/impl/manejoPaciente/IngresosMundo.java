package com.servinte.axioma.mundo.impl.manejoPaciente;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.UtilidadTexto;
import util.Utilidades;

import com.princetonsa.actionform.odontologia.AgendaOdontologicaForm;
import com.princetonsa.dto.manejoPaciente.DtoCentrosAtencion;
import com.princetonsa.dto.manejoPaciente.DtoInformacionBasicaIngresoPaciente;
import com.princetonsa.dto.manejoPaciente.DtoIngresos;
import com.princetonsa.dto.odontologia.DtoConsolidadoReporteIngresosOdonto;
import com.princetonsa.dto.odontologia.DtoIngresosOdontologicos;
import com.princetonsa.dto.odontologia.DtoReporteIngresosOdontologicos;
import com.princetonsa.dto.odontologia.DtoResultadoConsultaIngresosOdonto;
import com.princetonsa.dto.odontologia.DtoServicioOdontologico;
import com.princetonsa.dto.tesoreria.DtoInfoIngresoTrasladoAbonoPaciente;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.sort.odontologia.SortGenerico;
import com.servinte.axioma.dao.fabrica.ManejoPacienteDAOFabrica;
import com.servinte.axioma.dao.interfaz.manejoPaciente.IIngresosDAO;
import com.servinte.axioma.dto.tesoreria.DtoInfoIngresoPacienteControlarAbonoPacientes;
import com.servinte.axioma.mundo.fabrica.AdministracionFabricaMundo;
import com.servinte.axioma.mundo.fabrica.facturacion.FacturacionFabricaMundo;
import com.servinte.axioma.mundo.fabrica.odontologia.manejopaciente.ManejoPacienteFabricaMundo;
import com.servinte.axioma.mundo.fabrica.odontologia.presupuesto.PresupuestoFabricaMundo;
import com.servinte.axioma.mundo.interfaz.administracion.ICentroAtencionMundo;
import com.servinte.axioma.mundo.interfaz.administracion.ICentroCostoMundo;
import com.servinte.axioma.mundo.interfaz.administracion.IInstitucionesMundo;
import com.servinte.axioma.mundo.interfaz.administracion.IUsuariosMundo;
import com.servinte.axioma.mundo.interfaz.facturacion.IContratoMundo;
import com.servinte.axioma.mundo.interfaz.facturacion.IConveniosMundo;
import com.servinte.axioma.mundo.interfaz.manejoPaciente.IIngresosMundo;
import com.servinte.axioma.mundo.interfaz.manejoPaciente.IPacientesMundo;
import com.servinte.axioma.mundo.interfaz.odontologia.agendaOdontologica.ICitaOdontologicaMundo;
import com.servinte.axioma.mundo.interfaz.presupuesto.IPresupuestoContratadoMundo;
import com.servinte.axioma.mundo.interfaz.presupuesto.IPresupuestoOdontologicoMundo;
import com.servinte.axioma.orm.Ingresos;


public class IngresosMundo implements IIngresosMundo {
	
	private IIngresosDAO 	ingresosDAO;
	private ICentroAtencionMundo centroAtencionMundo;
	private IUsuariosMundo usuarioMundo;
	private IInstitucionesMundo institucionesMundo;
	private IPacientesMundo pacientesMundo;
	private ICentroCostoMundo centroCostoMundo;
	private IConveniosMundo conveniosMundo;
	private IContratoMundo contratoMundo;
	public static int PRIORIDAD_PRESUPUESTO_CON_SOLICITUD_DCTO = 3;
	public static int PRIORIDAD_PRESUPUESTO_SIN_SOLICITUD_DCTO = 3;
	
	/**
	 * Atributos que almacenan los c&oacute;digos de los ingresos que tienen o no 
	 * presupuesto odontol&oacute;gico.
	 */
	private List<Integer> ingresosSinPresupuesto = new ArrayList<Integer>();
	private List<Integer> ingresosConPresupuesto = new ArrayList<Integer>();
	@SuppressWarnings("unused")
	private int totalPresupuestoConDcto = 0;
	@SuppressWarnings("unused")
	private int totalPresupuestoSinDcto = 0;
	
	
	public IngresosMundo() {
		inicializar();
	}
	
	private void inicializar() {
		ingresosDAO			= ManejoPacienteDAOFabrica.crearinIngresosDAO();
		centroAtencionMundo = AdministracionFabricaMundo.crearCentroAtencionMundo();
		usuarioMundo 		= AdministracionFabricaMundo.crearUsuariosMundo();
		institucionesMundo 	= AdministracionFabricaMundo.crearInstitucionesMundo();
		pacientesMundo 		= ManejoPacienteFabricaMundo.crearPacientesMundo();
		centroCostoMundo	= AdministracionFabricaMundo.crearCentroCostoMundo();
		conveniosMundo		= FacturacionFabricaMundo.crearcConveniosMundo();
		contratoMundo		= FacturacionFabricaMundo.crearContratoMundo();
		ingresosSinPresupuesto = new ArrayList<Integer>();
		ingresosConPresupuesto = new ArrayList<Integer>();
		totalPresupuestoConDcto = 0;
		totalPresupuestoSinDcto = 0;
	}
	
	@Override
	public ArrayList<DtoInfoIngresoPacienteControlarAbonoPacientes> cargarIngresosPorPaciente(int codPaciente, String parametroGeneralControlarAbonoPacientesXIngreso) {
		return ingresosDAO.cargarIngresosPorPaciente(codPaciente, parametroGeneralControlarAbonoPacientesXIngreso);
	}

	@Override
	public List<DtoInfoIngresoTrasladoAbonoPaciente> obtenerIngresosParaTrasladoPorPaciente(int codPaciente, boolean listarPorIngreso) {
		return ingresosDAO.obtenerIngresosParaTrasladoPorPaciente(codPaciente, listarPorIngreso);
	}


	@Override
	public List<Ingresos> obtenerIngresosPacientePorEstado(int codPaciente,	String[] listaEstadosIngreso) {
		return ingresosDAO.obtenerIngresosPacientePorEstado(codPaciente, listaEstadosIngreso);
	}


	@Override
	public DtoInfoIngresoTrasladoAbonoPaciente obtenerUltimoIngresoAbiertoPaciente(int codPaciente, boolean parametroManejoEspecialInstiOdontologicas) {
		return ingresosDAO.obtenerUltimoIngresoAbiertoPaciente(codPaciente, parametroManejoEspecialInstiOdontologicas);
	}
	
	/**
	 * Este m&eacute;todo se encarga de obtener el listado de los ingresos que se encuentran en estado
	 * abierto, que cumplen con unos par&acute;metros de b&uacute;squeda y que adem&aacute;s
	 * no tienen valoraci&oacute;n inicial y si la tienen &eacute;sta se encuentra en estado no atendida.
	 * @param dto
	 * @return
	 * @author Yennifer Guerrero
	 */
	public ArrayList<DtoResultadoConsultaIngresosOdonto> consolidarReporteIngresosSinValIni(
			DtoReporteIngresosOdontologicos dto, List<Integer> ingresosconsulta){
		
		ICitaOdontologicaMundo citaOdontoMundo = ManejoPacienteFabricaMundo.crearCitaOdontologicaMundo();	
		
		if (ingresosconsulta != null && ingresosconsulta.size()>0) {
			ArrayList<DtoResultadoConsultaIngresosOdonto> listaIngresosOdonto = new ArrayList<DtoResultadoConsultaIngresosOdonto>();
			
			ArrayList<DtoIngresosOdontologicos> listaIngresosValiniNoAtendidos = citaOdontoMundo.obtenercitaOdontoValIniNoAtendida(ingresosconsulta);
			List<Integer> codigoPaciente = new ArrayList<Integer>();
			
			for (DtoIngresosOdontologicos lista : listaIngresosValiniNoAtendidos) {
				codigoPaciente.add(lista.getCodigoPaciente());
			}
			
			ArrayList<DtoIngresosOdontologicos> listaIngresosSinValIni = citaOdontoMundo.obtenerCitaOdontoSinValIni(ingresosconsulta, codigoPaciente);
			
			
			ArrayList<DtoIngresosOdontologicos> listaIngresos= ingresosDAO.obtenerIngresosSinCitasOdonto(ingresosconsulta);
			
			
			if (listaIngresos != null && listaIngresos.size() > 0) {
				//A los ingresos sin citas odontol&oacute;gicas les asigna el estado a sin cita.
				for (DtoIngresosOdontologicos dtoIngresosOdontologicos : listaIngresos) {
					dtoIngresosOdontologicos.setEstadoCitaOdonto("Sin cita");
				}
			}
			
			if (listaIngresosSinValIni != null && listaIngresosSinValIni.size() > 0) {
				//A los ingresos sin valoraciones odontol&oacute;gicas les asigna el estado a sin cita.		
				for (DtoIngresosOdontologicos dtoIngresosOdontologicos : listaIngresosSinValIni) {
					dtoIngresosOdontologicos.setEstadoCitaOdonto("Sin cita");
				}
				
				listaIngresos.addAll(listaIngresosSinValIni);
				
			}
			
			if (listaIngresosValiniNoAtendidos != null && listaIngresosValiniNoAtendidos.size() > 0) {
				listaIngresos.addAll(listaIngresosValiniNoAtendidos);
			}
			
			if (listaIngresos != null && listaIngresos.size() > 0) {
				accionOrdenar(listaIngresos);
				Collections.sort(listaIngresos);
				boolean esValIni = false;
				
				listaIngresosOdonto = ordenarDatosConRompimiento(listaIngresos, esValIni);
				
			}
			
			
			return listaIngresosOdonto;
		}
		
		return null;
	}
	
	/**
	 * Este m&eacute;todo se encarga de obtener el listado de ingresos odontol&oacute;gicos
	 * que tienen valoraciones iniciales en estado atendida.
	 * @param dto
	 * @return
	 *
	 * @author Yennifer Guerrero
	 * @param listaUltimaCitaPorPaciente 
	 */
	public ArrayList<DtoResultadoConsultaIngresosOdonto> consolidarReporteIngresosConValIni(
			DtoReporteIngresosOdontologicos filtroIngresos, List<Integer> ingresosconsulta, List<Long> listaUltimaCitaPorPaciente){
		
		ICitaOdontologicaMundo citaOdontoMundo = ManejoPacienteFabricaMundo.crearCitaOdontologicaMundo();
		String conPresupuesto = filtroIngresos.getConPresupuesto();
	
		//Se obtienen los ingresos con valoración inicial en estado atendida.
		List<Integer> listaIngresosConValIni = citaOdontoMundo.obtenercitaOdontoValIniAtendida(ingresosconsulta);
		
		if (listaIngresosConValIni != null && listaIngresosConValIni.size() > 0) {
			
			ArrayList<DtoIngresosOdontologicos> listaIngresosSinPresupuesto = null;
			ArrayList<DtoIngresosOdontologicos> listaIngresosConPresupuesto = null;
			ArrayList<DtoIngresosOdontologicos> listadoConsolidadoIngresosValIni = new ArrayList<DtoIngresosOdontologicos>();
			
			if (conPresupuesto.equals(ConstantesBD.acronimoNo)) {
				listaIngresosSinPresupuesto = obtenerIngresosSinPresupuesto(listaIngresosConValIni, listaUltimaCitaPorPaciente);				
				accionOrdenar(listaIngresosSinPresupuesto);	
				
				if (listaIngresosSinPresupuesto != null && listaIngresosSinPresupuesto.size()>0) {
					listadoConsolidadoIngresosValIni.addAll(listaIngresosSinPresupuesto);
				}
			} else if (conPresupuesto.equals(ConstantesBD.acronimoSi)) {				
				listaIngresosConPresupuesto = obtenerIngresosConPresupuesto(listaIngresosConValIni, filtroIngresos, listaUltimaCitaPorPaciente);
				
				if (listaIngresosConPresupuesto != null && listaIngresosConPresupuesto.size() > 0) {
					listadoConsolidadoIngresosValIni.addAll(listaIngresosConPresupuesto);
				}else{
					listadoConsolidadoIngresosValIni = null;
				}
								
			}else{
				listaIngresosSinPresupuesto = obtenerIngresosSinPresupuesto(listaIngresosConValIni, listaUltimaCitaPorPaciente);
				listaIngresosConPresupuesto = obtenerIngresosConPresupuesto(listaIngresosConValIni, filtroIngresos, listaUltimaCitaPorPaciente);
				
				if (listaIngresosSinPresupuesto != null && listaIngresosSinPresupuesto.size() > 0) {
					listadoConsolidadoIngresosValIni.addAll(listaIngresosSinPresupuesto);
				}
				
				if (listaIngresosConPresupuesto != null && listaIngresosConPresupuesto.size() > 0) {
					listadoConsolidadoIngresosValIni.addAll(listaIngresosConPresupuesto);
				}
				
				if(listadoConsolidadoIngresosValIni != null && listadoConsolidadoIngresosValIni.size() > 0)  {
					accionOrdenar(listadoConsolidadoIngresosValIni);
				}
			}
			
			if (listadoConsolidadoIngresosValIni != null && listadoConsolidadoIngresosValIni.size() > 0) {
				Collections.sort(listadoConsolidadoIngresosValIni);
				boolean esValIni = true;
				
				ArrayList<DtoResultadoConsultaIngresosOdonto> listado= ordenarDatosConRompimiento(listadoConsolidadoIngresosValIni, esValIni);
				
				return listado;
			} else {
				return null;
			}
			
		}
		return null;
	}
	
	/**
	 * Este m&eacute;todo se encarga de iterar los dto que contienen la informaci&oacute;n 
	 * sobre los ingresos odontol&oacute;gicos y organizar estos en un solo dto que contiene
	 * ArrayList con los datos especificos a mostrar e iterar en el reporte. 
	 * @param listaIngresos
	 * @param esValIni
	 * @return
	 *
	 * @author Yennifer Guerrero
	 */
	public ArrayList<DtoResultadoConsultaIngresosOdonto> ordenarDatosConRompimiento(ArrayList<DtoIngresosOdontologicos> listaIngresos, boolean esValIni) {
		
		ArrayList<DtoIngresosOdontologicos> listaDetalles = new ArrayList<DtoIngresosOdontologicos>();
		ArrayList<DtoResultadoConsultaIngresosOdonto> listaIngresosOdonto = new ArrayList<DtoResultadoConsultaIngresosOdonto>();
		DtoResultadoConsultaIngresosOdonto dtoConsolidadoIngresos = null;
		int penultimoRegistro=0;
		int indice=0;
		boolean registroDiferente=false;
		
		if (listaIngresos != null && listaIngresos.size()>0) {
			int idCentroAtencion = listaIngresos.get(0).getConsecutivoCentroAtencion();
			
			for(DtoIngresosOdontologicos registro :listaIngresos){	
				
				if(idCentroAtencion==registro.getConsecutivoCentroAtencion()){
					listaDetalles.add(registro);
					registroDiferente=false;
				}else{
					
					idCentroAtencion= registro.getConsecutivoCentroAtencion();
					
					dtoConsolidadoIngresos =cargarDtoConsolidadoIngresos(dtoConsolidadoIngresos, listaDetalles);
					
					if (esValIni) {
						dtoConsolidadoIngresos.setListaIngresosConValIni(listaDetalles);
					}else {
						dtoConsolidadoIngresos.setListaIngresosSinValIni(listaDetalles);
					}
					listaIngresosOdonto.add(dtoConsolidadoIngresos);
					listaDetalles = new ArrayList<DtoIngresosOdontologicos>();
					listaDetalles.add(registro);
					registroDiferente=true;
				}
				indice++;
			}
			
			if(!registroDiferente){
				dtoConsolidadoIngresos = cargarDtoConsolidadoIngresos(dtoConsolidadoIngresos, listaIngresos);
				
				if (esValIni) {
					dtoConsolidadoIngresos.setListaIngresosConValIni(listaDetalles);
				} else {
					dtoConsolidadoIngresos.setListaIngresosSinValIni(listaDetalles);
				}
				
				listaIngresosOdonto.add(dtoConsolidadoIngresos);
				listaDetalles = new ArrayList<DtoIngresosOdontologicos>();
				
			}else{
				if(listaIngresos.size()>=2){
					penultimoRegistro=2;
				}else{
					penultimoRegistro=1;
				}
				
				if(listaIngresos.get(listaIngresos.size()-penultimoRegistro).getConsecutivoCentroAtencion()==idCentroAtencion){
					if(listaIngresosOdonto.size()>0){
						
						if (esValIni) {
							listaIngresosOdonto.get(listaIngresosOdonto.size()-1).getListaIngresosConValIni().add(
									listaIngresos.get(listaIngresos.size()-1));
						} else {
							listaIngresosOdonto.get(listaIngresosOdonto.size()-1).getListaIngresosSinValIni().add(
									listaIngresos.get(listaIngresos.size()-1));
						}
						
					}else{
						dtoConsolidadoIngresos=new DtoResultadoConsultaIngresosOdonto();
						
						if (esValIni) {
							dtoConsolidadoIngresos.setListaIngresosConValIni(listaDetalles);
						} else {
							dtoConsolidadoIngresos.setListaIngresosSinValIni(listaDetalles);
						}
										
						listaIngresosOdonto.add(dtoConsolidadoIngresos);
					}				
				}else{
					listaDetalles = new  ArrayList<DtoIngresosOdontologicos>();
					dtoConsolidadoIngresos=new DtoResultadoConsultaIngresosOdonto();
					listaDetalles.add(listaIngresos.get(indice-1));	
					
					if (esValIni) {
						dtoConsolidadoIngresos.setListaIngresosConValIni(listaDetalles);
					} else {
						dtoConsolidadoIngresos.setListaIngresosSinValIni(listaDetalles);
					}
					
					dtoConsolidadoIngresos.setDescripcionCentroAtencion(
							listaDetalles.get(listaDetalles.size()-1).getDescripcionCentroAtencion());
					dtoConsolidadoIngresos.setCodigoInstitucion(
							listaDetalles.get(listaDetalles.size()-1).getCodigoInstitucion());
					dtoConsolidadoIngresos.setEsMultiempresa(
							listaDetalles.get(listaDetalles.size()-1).getEsMultiempresa());	
					dtoConsolidadoIngresos.setDescripcionCiudad(
							listaDetalles.get(listaDetalles.size()-1).getDescripcionCiudad());
					dtoConsolidadoIngresos.setDescripcionEmpresaInstitucion(
							listaDetalles.get(listaDetalles.size()-1).getDescripcionEmpresaInstitucion());
					dtoConsolidadoIngresos.setDescripcionPais(
							listaDetalles.get(listaDetalles.size()-1).getDescripcionPais());
					dtoConsolidadoIngresos.setDescripcionRegionCobertura(
							listaDetalles.get(listaDetalles.size()-1).getDescripcionRegionCobertura());
					dtoConsolidadoIngresos.setConsecutivoCentroAtencion(
							listaDetalles.get(listaDetalles.size()-1).getConsecutivoCentroAtencion());
									
					listaIngresosOdonto.add(dtoConsolidadoIngresos);
				}
			}		
			
		}
		return listaIngresosOdonto;
	}


	/**
	 * Este m&eacute;todo se encarga de asignar la informaci&oacute;n a cerca del rompimiento
	 * en el dtoConsolidadoIngresos para que sea visualizada de forma correcta en el reporte.
	 * @param dtoConsolidadoIngresos
	 * @param lista
	 *
	 * @author Yennifer Guerrero
	 */
	private DtoResultadoConsultaIngresosOdonto  cargarDtoConsolidadoIngresos(DtoResultadoConsultaIngresosOdonto dtoConsolidadoIngresos, 
			ArrayList<DtoIngresosOdontologicos> lista){
		
		dtoConsolidadoIngresos = new DtoResultadoConsultaIngresosOdonto();
		
		dtoConsolidadoIngresos.setDescripcionCentroAtencion(
				lista.get(lista.size()-1).getDescripcionCentroAtencion());
		dtoConsolidadoIngresos.setCodigoInstitucion(
				lista.get(lista.size()-1).getCodigoInstitucion());
		dtoConsolidadoIngresos.setEsMultiempresa(
				lista.get(lista.size()-1).getEsMultiempresa());	
		dtoConsolidadoIngresos.setDescripcionCiudad(
				lista.get(lista.size()-1).getDescripcionCiudad());
		dtoConsolidadoIngresos.setDescripcionEmpresaInstitucion(
				lista.get(lista.size()-1).getDescripcionEmpresaInstitucion());
		dtoConsolidadoIngresos.setDescripcionPais(
				lista.get(lista.size()-1).getDescripcionPais());
		dtoConsolidadoIngresos.setDescripcionRegionCobertura(
				lista.get(lista.size()-1).getDescripcionRegionCobertura());
		dtoConsolidadoIngresos.setConsecutivoCentroAtencion(
				lista.get(lista.size()-1).getConsecutivoCentroAtencion());
		
		return dtoConsolidadoIngresos;
	}
	
	/**
	 * Este m&eacute;todo se encarga de obtener el listado de ingresos
	 * odontol&oacute;gicos con valoraciones iniciales y sin presupuesto.
	 * @param listaIngresosConValIni
	 *
	 * @author Yennifer Guerrero
	 * @param listaUltimaCitaPorPaciente 
	 */
	private ArrayList<DtoIngresosOdontologicos> obtenerIngresosSinPresupuesto(List<Integer> listaIngresosConValIni, List<Long> listaUltimaCitaPorPaciente) {
		
		ArrayList<DtoIngresosOdontologicos> listaIngresosSinPresupuesto = ingresosDAO
		.obtenerIngresosSinPresupuesto(listaIngresosConValIni, listaUltimaCitaPorPaciente);
		
		if (listaIngresosSinPresupuesto != null && listaIngresosSinPresupuesto.size() > 0) {
			for (DtoIngresosOdontologicos registro : listaIngresosSinPresupuesto) {
				registro.setEstadoPresupuesto(ConstantesBD.codigoNuncaValido + "");
				registro.setNumeroContrato("-");
				registro.setValorPresupuesto("-");
				registro.setNumeroPresupuesto("-");
				registro.setFechaGeneracionPresup("-");
				
				ingresosSinPresupuesto.add(registro.getIdIngreso());
			}	
			
		}
		
		return listaIngresosSinPresupuesto;
	}

	/**
	 * Este m&eacute;todo se encarga de obtener el listado de ingresos
	 * odontol&oacute;gicos con valoraciones iniciales y con presupuesto.
	 * @param listaIngresosConValIni
	 *
	 * @author Yennifer Guerrero
	 * @param listaUltimaCitaPorPaciente 
	 * @param conSolicitudDcto2 
	 */
	private ArrayList<DtoIngresosOdontologicos> obtenerIngresosConPresupuesto(
			List<Integer> listaIngresosConValIni, DtoReporteIngresosOdontologicos filtroIngresos, List<Long> listaUltimaCitaPorPaciente) {
		
		IPresupuestoContratadoMundo mundoContratado = PresupuestoFabricaMundo.crearPresupuestoContratadoMundo();
		IPresupuestoOdontologicoMundo mundo = PresupuestoFabricaMundo.crearPresupuestoOdontologicoMundo();
		List<Long> listaPresupuestos = new ArrayList<Long>();
		List<Long> presupuestos = new ArrayList<Long>();
		ArrayList<DtoIngresosOdontologicos> listadoConDcto = new ArrayList<DtoIngresosOdontologicos>();
		ArrayList<DtoIngresosOdontologicos> listadoSinDcto = new ArrayList<DtoIngresosOdontologicos>();
		
		
		ArrayList<DtoIngresosOdontologicos> listaIngresosConPresupuesto = ingresosDAO
			.obtenerIngresosValIniConPresupuesto(listaIngresosConValIni, filtroIngresos, listaUltimaCitaPorPaciente);
		
		if (listaIngresosConPresupuesto != null && listaIngresosConPresupuesto.size() > 0) {
			ArrayList<DtoIngresosOdontologicos> consolidadoIngresosConPresupuesto = mundoContratado
			.consolidarInfoIngresosValIniConPresupuesto(listaIngresosConPresupuesto);
		
			if (consolidadoIngresosConPresupuesto != null && consolidadoIngresosConPresupuesto.size() > 0) {
				for (DtoIngresosOdontologicos dto : consolidadoIngresosConPresupuesto) {
					listaPresupuestos.add(dto.getCodigoPkPresupuesto());
				}
				
				if (!UtilidadTexto.isEmpty(filtroIngresos.getConSolicitudDcto())){
					
					String conSolicitudDcto = filtroIngresos.getConSolicitudDcto();
					ArrayList<DtoIngresosOdontologicos> temp = mundo.obtenerPresupuestosPrecontratados(listaPresupuestos, conSolicitudDcto);
					
					if(temp != null && temp.size()> 0){
						temp = mundoContratado.consolidarInfoIngresosValIniConPresupuesto(temp);
						consolidadoIngresosConPresupuesto = temp;
						
					}else{
						consolidadoIngresosConPresupuesto = null;
						totalPresupuestoSinDcto = listaPresupuestos.size();
					}
					
				}
					
				for (DtoIngresosOdontologicos registro : consolidadoIngresosConPresupuesto) {
					presupuestos.add(registro.getCodigoPkPresupuesto());
				}
					
				listadoConDcto = mundo.obtenerPresupuestosPrecontratados(presupuestos, ConstantesBD.acronimoSi);
				listadoSinDcto = mundo.obtenerPresupuestosPrecontratados(presupuestos, ConstantesBD.acronimoNo);
				
				
				if ((listadoConDcto != null && listadoConDcto.size() > 0) ||
						(listadoSinDcto != null && listadoSinDcto.size()> 0)) {
					
					for (DtoIngresosOdontologicos dto : consolidadoIngresosConPresupuesto) {
						boolean asignaEstado= false;
						for (DtoIngresosOdontologicos conDcto : listadoConDcto) {
							if (dto.getCodigoPkPresupuesto().equals(conDcto.getCodigoPkPresupuesto())) {
								dto.setConSolicitudDcto(ConstantesBD.acronimoSi);
								asignaEstado= true;
								break;
							}
						}
					
						if (!asignaEstado) {
							for (DtoIngresosOdontologicos sinDcto : listadoSinDcto) {
								if (dto.getCodigoPkPresupuesto().equals(sinDcto.getCodigoPkPresupuesto())) {
									dto.setConSolicitudDcto(ConstantesBD.acronimoNo);
									break;
								}
							}
						}
					}
				}
					
				
				if (consolidadoIngresosConPresupuesto != null && consolidadoIngresosConPresupuesto.size() > 0 ) {
					//obteniendo el codigo de los ingresos con presupuesto.
					for (DtoIngresosOdontologicos dto : consolidadoIngresosConPresupuesto) {
						ingresosConPresupuesto.add(dto.getIdIngreso());
					}
				}
				
			}
			
			return consolidadoIngresosConPresupuesto;
		}
		
		return null;
	}


	@Override
	public List<Integer> consultarIngresosOdontoEstadoAbierto(
			DtoReporteIngresosOdontologicos dto){
		return ingresosDAO.consultarIngresosOdontoEstadoAbierto(dto);
	}
	
	@Override
	public ArrayList<DtoResultadoConsultaIngresosOdonto> obtenerConsolidadoReporte (ArrayList<DtoResultadoConsultaIngresosOdonto> listadoIngresosOdonto, 
			List<Integer> ingresosconsulta,  DtoReporteIngresosOdontologicos filtroIngresos){
		
		IIngresosDAO dao = ManejoPacienteDAOFabrica.crearinIngresosDAO();
		IPresupuestoOdontologicoMundo mundo = PresupuestoFabricaMundo.crearPresupuestoOdontologicoMundo();
		
		for (DtoResultadoConsultaIngresosOdonto registro : listadoIngresosOdonto) {
			
			//listas temporales
			ArrayList<DtoIngresosOdontologicos> listadoIngresosConValIni = new ArrayList<DtoIngresosOdontologicos>();
			List<Integer> ingresosConValIni = new ArrayList<Integer>();
			List<Long> presupuestosValIni = new ArrayList<Long>();
			ArrayList<DtoIngresosOdontologicos> listadoIngresosSinValIni = new ArrayList<DtoIngresosOdontologicos>();
			List<Integer> ingresosSinValIni = new ArrayList<Integer>();
			ArrayList<DtoIngresosOdontologicos> listadoConDcto = new ArrayList<DtoIngresosOdontologicos>();
			ArrayList<DtoIngresosOdontologicos> listadoSinDcto = new ArrayList<DtoIngresosOdontologicos>();
			
			DtoConsolidadoReporteIngresosOdonto consolidadoIngresosOdonto = new DtoConsolidadoReporteIngresosOdonto();
			int consecutivoCentroAtencion = 0;
			int totalPacientesIngresados =	0;
			int totalPacientesConValIni = 	0;
			int totalPacientesSinValIni = 	0;
			int totalPacientesSinPresupuesto = 0;
			int totalPacientesConPresupuesto = 0;
			String porcentajePacientesSinValIni = "";
			String porcentajePacientesConValIni = "";
			ArrayList<DtoConsolidadoReporteIngresosOdonto> listadoPacientesConValIni = new ArrayList<DtoConsolidadoReporteIngresosOdonto>();
			ArrayList<DtoConsolidadoReporteIngresosOdonto> listadoPacientesSinValIni = new ArrayList<DtoConsolidadoReporteIngresosOdonto>();
			ArrayList<DtoConsolidadoReporteIngresosOdonto> listadoPresupuestoPorEstado = new ArrayList<DtoConsolidadoReporteIngresosOdonto>();
			
			consecutivoCentroAtencion = registro.getConsecutivoCentroAtencion();
			
			listadoIngresosConValIni = registro.getListaIngresosConValIni();
			
			
			if (listadoIngresosConValIni != null) {
				for (DtoIngresosOdontologicos dto : listadoIngresosConValIni) {
					
					ingresosConValIni.add(dto.getIdIngreso());
					presupuestosValIni.add(dto.getCodigoPkPresupuesto());
				}
				
				if (ingresosConValIni != null && ingresosConValIni.size()>0) {
					listadoPresupuestoPorEstado = dao.obtenerConsolidadoIngresosConPresupuesto(ingresosConValIni, consecutivoCentroAtencion, filtroIngresos);
				
					if (!UtilidadTexto.isEmpty(filtroIngresos.getConSolicitudDcto())) {
						for (DtoConsolidadoReporteIngresosOdonto dto : listadoPresupuestoPorEstado) {
							if (dto.getEstadoPresupuesto().equals(ConstantesIntegridadDominio.acronimoPrecontratado)) {
								listadoPresupuestoPorEstado = new ArrayList<DtoConsolidadoReporteIngresosOdonto>();
								listadoPresupuestoPorEstado.add(dto);
							}
						}
					}
					
				}
				
				
				if (listadoPresupuestoPorEstado != null && listadoPresupuestoPorEstado.size() > 0) {
					
					int index = 0;
					boolean encontrado = false;
					
					
					
					for (DtoConsolidadoReporteIngresosOdonto dto : listadoPresupuestoPorEstado) {
						
						if (dto.getEstadoPresupuesto().equals(ConstantesIntegridadDominio.acronimoPrecontratado)) {
							encontrado = true;
							break;
						}
						
						index ++;
					}
					
					if (encontrado) {
						listadoPresupuestoPorEstado.remove(index);
					}
					
					listadoConDcto = mundo.obtenerPresupuestosPrecontratados(presupuestosValIni, ConstantesBD.acronimoSi);
					listadoSinDcto = mundo.obtenerPresupuestosPrecontratados(presupuestosValIni, ConstantesBD.acronimoNo);
					
					
					
					
					if (listadoConDcto != null && listadoConDcto.size() > 0) {
						DtoConsolidadoReporteIngresosOdonto precontratados = new DtoConsolidadoReporteIngresosOdonto();
						precontratados.setAyudanteEstadoPresupuesto("Precontratado - Con Solicitud Descuento");
						precontratados.setTotalPresupuestoPorEstado(listadoConDcto.size());
						precontratados.setPrioridadEstadoPresupuesto(PRIORIDAD_PRESUPUESTO_CON_SOLICITUD_DCTO); //aqui
						listadoPresupuestoPorEstado.add(precontratados);
						
					}
					
					if (listadoSinDcto != null && listadoSinDcto.size() > 0) {
						DtoConsolidadoReporteIngresosOdonto precontratados = new DtoConsolidadoReporteIngresosOdonto();
						precontratados.setAyudanteEstadoPresupuesto("Precontratado - Sin Solicitud Descuento");
						precontratados.setTotalPresupuestoPorEstado(listadoSinDcto.size());
						precontratados.setPrioridadEstadoPresupuesto(PRIORIDAD_PRESUPUESTO_SIN_SOLICITUD_DCTO);
						
						listadoPresupuestoPorEstado.add(precontratados);
					} 
					
					/**
					 * Se adiciona la prioridad en la que deben ser mostrados los estados
					 * en el consolidado.
					 */
					
					ordenar(listadoPresupuestoPorEstado);
					
					consolidadoIngresosOdonto.setListaTotalPresupuesto(listadoPresupuestoPorEstado);
					
				}
				
				
				//obtiene el total de ingresos sin presupuesto.				
				totalPacientesSinPresupuesto = dao.obtenerTotalPacientesPresupuesto(ingresosSinPresupuesto, null, consecutivoCentroAtencion);
				
				totalPacientesConPresupuesto = dao.obtenerTotalPacientesPresupuesto(null, ingresosConPresupuesto, consecutivoCentroAtencion);
				
				//se obtiene el total de pacientes con valoracion inicial
				//totalPacientesConValIni = dao.obtenerTotalPacientes(ingresosConValIni, null, consecutivoCentroAtencion);
				listadoPacientesConValIni.addAll(dao.obtenerTotalPacientes(ingresosConValIni, null, consecutivoCentroAtencion));
			}
			
			
			listadoIngresosSinValIni = registro.getListaIngresosSinValIni();
			
			if (listadoIngresosSinValIni != null && listadoIngresosSinValIni.size() > 0) {
				totalPacientesSinValIni= listadoIngresosSinValIni.size();
			}
			
			if (listadoPacientesConValIni != null && listadoPacientesConValIni.size() > 0) {
				totalPacientesConValIni = listadoPacientesConValIni.size();
			}
			
			
			
			ArrayList<DtoIngresosOdontologicos> listadoTotalIngresos = new ArrayList<DtoIngresosOdontologicos>();
			List<Integer> listaTotalIngresos = new ArrayList<Integer>();
			
			if (!Utilidades.isEmpty(listadoIngresosConValIni) && !Utilidades.isEmpty(listadoIngresosSinValIni)) {
				listadoTotalIngresos.addAll(listadoIngresosConValIni);
				listadoTotalIngresos.addAll(listadoIngresosSinValIni);
			}else if (!Utilidades.isEmpty(listadoIngresosConValIni)) {
				listadoTotalIngresos.addAll(listadoIngresosConValIni);
			}else if (!Utilidades.isEmpty(listadoIngresosSinValIni)) {
				listadoTotalIngresos.addAll(listadoIngresosSinValIni);
			}
			
			for (DtoIngresosOdontologicos dto : listadoTotalIngresos) {
				listaTotalIngresos.add(dto.getIdIngreso());
			}
			
			totalPacientesIngresados= dao.obtenerTotalPacientesIngresados(ingresosconsulta, consecutivoCentroAtencion);
			consolidadoIngresosOdonto.setTotalPacientesIngresados(totalPacientesIngresados);
			
			if (totalPacientesSinValIni >0) {
				consolidadoIngresosOdonto.setTotalPacientesSinValIni(totalPacientesSinValIni);
			}
			if (totalPacientesConValIni >0) {
				consolidadoIngresosOdonto.setTotalPacientesConValIni(totalPacientesConValIni);
			}
			
			//se obtiene el porcentaje de pacientes sin valoracion inicial
			porcentajePacientesSinValIni= consolidadoIngresosOdonto.getPorcentajePacientesSinValIni();
			consolidadoIngresosOdonto.setPorcentajePacientesSinValIni(porcentajePacientesSinValIni);
			
			//se obtiene el porcentaje de pacientes con valoraciones iniciales.
			porcentajePacientesConValIni = consolidadoIngresosOdonto.getPorcentajePacientesConValIni();
			consolidadoIngresosOdonto.setPorcentajePacientesConValIni(porcentajePacientesConValIni);
			
			consolidadoIngresosOdonto.setTotalPacientesConPresupuesto(totalPacientesConPresupuesto);
			consolidadoIngresosOdonto.setTotalPacientesSinPresupuesto(totalPacientesSinPresupuesto);
			
			
			
			
			registro.setConsolidadoIngresosOdonto(consolidadoIngresosOdonto);
		}
		
		return listadoIngresosOdonto;
	}
	
	
	/**
	 * Este m&eacute;todo se encarga de ordenar las columnas de el resultado 
	 * de la b&uacute;squeda sig&aacute;n los par&aacute;metros ingresados
	 * @param listaIngresos 
	 * @param forma 
	 * 
	 * @param mapping
	 * @param forma
	 * @param listadoIngresosOdontoSinValIni 
	 * @param usuario
	 * @return 
	 */
	public void accionOrdenar(ArrayList<DtoIngresosOdontologicos> listaIngresos){
		
		boolean ordenamiento = false;
		
		SortGenerico sortG=new SortGenerico("NumeroId",ordenamiento);
		Collections.sort(listaIngresos,sortG);
	}
	
	
	@Override
	public DtoInformacionBasicaIngresoPaciente construirDtoInformacionBasicaIngresoPaciente(AgendaOdontologicaForm forma, UsuarioBasico usuario)
	{
		DtoInformacionBasicaIngresoPaciente dtoInfoBasica = new DtoInformacionBasicaIngresoPaciente();
		
		dtoInfoBasica.setInstitucion(institucionesMundo.buscarPorCodigo(usuario.getCodigoInstitucionInt()));
		dtoInfoBasica.setCentroAtencion(centroAtencionMundo.buscarPorCodigo(usuario.getCodigoCentroAtencion()));
		dtoInfoBasica.setUsuario(usuarioMundo.buscarPorLogin(usuario.getLoginUsuario()));
		dtoInfoBasica.setPaciente(pacientesMundo.findById(forma.getPaciente().getCodigo()));
		dtoInfoBasica.setAreaCentroCosto(centroCostoMundo.findById(Integer.parseInt(forma.getCodigoArea())));
		dtoInfoBasica.setCcostoCentroCosto(centroCostoMundo.findById(forma.getCodCentroCostoXuniAgen()));
		if(!UtilidadTexto.isEmpty(forma.getDtoSeccionConvenioPaciente().getCodigoConvenio())){
			dtoInfoBasica.setConvenio(conveniosMundo.findById(Integer.parseInt(forma.getDtoSeccionConvenioPaciente().getCodigoConvenio())));
		}
		if(!UtilidadTexto.isEmpty(forma.getDtoSeccionConvenioPaciente().getCodigoContrato())){
			dtoInfoBasica.setContrato(contratoMundo.findById(Integer.parseInt(forma.getDtoSeccionConvenioPaciente().getCodigoContrato())));
		}
		
		return dtoInfoBasica;
	}
	
	
	
	@Override
	public Ingresos crearIngresos(DtoInformacionBasicaIngresoPaciente dtoInfoBasica)
	{
		Ingresos ingreso;
		ingreso = new Ingresos();
		
		String valorConsecutivo = UtilidadBD.obtenerValorConsecutivoDisponible(ConstantesBD.nombreConsecutivoIngresos, dtoInfoBasica.getInstitucion().getCodigo());
		String anioConsecutivo="";
		
		ingreso.setAnioConsecutivo(anioConsecutivo);
		ingreso.setCierreManual(dtoInfoBasica.getAcronimoNo().charAt(0));
		ingreso.setConsecutivo(valorConsecutivo);
		ingreso.setUsuariosByUsuarioModifica(dtoInfoBasica.getUsuario());
		ingreso.setCentroAtencion(dtoInfoBasica.getCentroAtencion());
		ingreso.setEstado(dtoInfoBasica.getEstadoIngresoAbierto());
		ingreso.setInstituciones(dtoInfoBasica.getInstitucion());
		ingreso.setFechaIngreso(dtoInfoBasica.getFechaActual());
		ingreso.setHoraIngreso(dtoInfoBasica.getHoraActual());
		ingreso.setTipoIngreso(dtoInfoBasica.getTipoIngresoOdontologia());
		ingreso.setFechaModifica(dtoInfoBasica.getFechaActual());
		ingreso.setHoraModifica(dtoInfoBasica.getHoraActual());
		ingreso.setPacientes(dtoInfoBasica.getPaciente());
		
		return ingreso;
	}

	
	
	@Override
	public DtoInformacionBasicaIngresoPaciente construirDtoInformacionBasicaIngresoPaciente(
			DtoServicioOdontologico servicio, UsuarioBasico usuario) {
		
		DtoInformacionBasicaIngresoPaciente dtoInfoBasica = new DtoInformacionBasicaIngresoPaciente();
		
		//FIXMe implementar
		/*
		dtoInfoBasica.setInstitucion(institucionesMundo.buscarPorCodigo(usuario.getCodigoInstitucionInt()));
		dtoInfoBasica.setCentroAtencion(centroAtencionMundo.buscarPorCodigo(usuario.getCodigoCentroAtencion()));
		dtoInfoBasica.setUsuario(usuarioMundo.buscarPorLogin(usuario.getLoginUsuario()));
		dtoInfoBasica.setPaciente(pacientesMundo.findById(forma.getPaciente().getCodigo()));
		dtoInfoBasica.setAreaCentroCosto(centroCostoMundo.findById(Integer.parseInt(forma.getCodigoArea())));
		dtoInfoBasica.setCcostoCentroCosto(centroCostoMundo.findById(forma.getCodCentroCostoXuniAgen()));
		dtoInfoBasica.setConvenio(conveniosMundo.findById(Integer.parseInt(forma.getDtoSeccionConvenioPaciente().getCodigoConvenio())));
		dtoInfoBasica.setContrato(contratoMundo.findById(Integer.parseInt(forma.getDtoSeccionConvenioPaciente().getCodigoContrato())));
		*/
		
		return dtoInfoBasica;
	}
	
	
	@Override
	public ArrayList<DtoResultadoConsultaIngresosOdonto> ordenarInfoReporteAmbos(
			ArrayList<DtoCentrosAtencion> listadoCentroAtencionIngreso, 
			ArrayList<DtoResultadoConsultaIngresosOdonto> listadoIngresosOdonto){
		
		ArrayList<DtoResultadoConsultaIngresosOdonto> listadoResultadoOrdenado = new ArrayList<DtoResultadoConsultaIngresosOdonto>();
		DtoResultadoConsultaIngresosOdonto dtoResultadoOrdenado= null;
		
		for (DtoCentrosAtencion dtoCentros : listadoCentroAtencionIngreso) {
			
			dtoResultadoOrdenado = new DtoResultadoConsultaIngresosOdonto();
			
			for (DtoResultadoConsultaIngresosOdonto dtoResultado : listadoIngresosOdonto) {
				
				if (dtoCentros.getConsecutivo() == dtoResultado.getConsecutivoCentroAtencion()) {
					
					dtoResultadoOrdenado.setConsecutivoCentroAtencion(dtoResultado.getConsecutivoCentroAtencion());
					dtoResultadoOrdenado.setDescripcionCentroAtencion(dtoResultado.getDescripcionCentroAtencion());
					dtoResultadoOrdenado.setDescripcionCiudad(dtoResultado.getDescripcionCiudad());
					dtoResultadoOrdenado.setDescripcionEmpresaInstitucion(dtoResultado.getDescripcionEmpresaInstitucion());
					dtoResultadoOrdenado.setDescripcionPais(dtoResultado.getDescripcionPais());
					dtoResultadoOrdenado.setDescripcionRegionCobertura(dtoResultado.getDescripcionRegionCobertura());
					
					if (dtoResultado.getListaIngresosConValIni() != null && dtoResultado.getListaIngresosConValIni().size() > 0) {
						dtoResultadoOrdenado.setListaIngresosConValIni(dtoResultado.getListaIngresosConValIni());
					}
					
					if (dtoResultado.getListaIngresosSinValIni() != null && dtoResultado.getListaIngresosSinValIni().size() > 0) {
						dtoResultadoOrdenado.setListaIngresosSinValIni(dtoResultado.getListaIngresosSinValIni());
					}
					
					dtoResultadoOrdenado.setConsolidadoIngresosOdonto(dtoResultado.getConsolidadoIngresosOdonto());
				}
			}
			listadoResultadoOrdenado.add(dtoResultadoOrdenado);
		}
		
		return listadoResultadoOrdenado;
	}

	
	
	@Override
	public DtoInfoIngresoTrasladoAbonoPaciente obtenerUltimoIngresoPaciente(int codPaciente) {
		return ingresosDAO.obtenerUltimoIngresoPaciente(codPaciente);
	}
	
	@Override
	public int consultarIdIngresosPacienteEstadoAbierto(int codigoPaciente) {
		return ingresosDAO.consultarIdIngresosPacienteEstadoAbierto(codigoPaciente);
	}
	
	public void ordenar(ArrayList<DtoConsolidadoReporteIngresosOdonto> listadoPresupuestoPorEstado){
		
		boolean ordenamiento = true;
		
		SortGenerico sortG=new SortGenerico("PrioridadEstadoPresupuesto",ordenamiento);
		Collections.sort(listadoPresupuestoPorEstado,sortG);

		
	}
	
	/**
	 * Este mètodo se encarga de obtener un listado con los pacientes que tienen
	 * citas odontológicas según los ingresos que llegan por parámetro.
	 * Obtiene el último registro de cada estado de la cita de valoración inicial
	 * y su correspondiente fecha de modificación.
	 *
	 * @param filtro
	 * @param ingresos
	 *
	 * @autor Yennifer Guerrero
	 */
	public ArrayList<DtoResultadoConsultaIngresosOdonto> obtenerConsolidadoCitasPorPaciente(DtoReporteIngresosOdontologicos filtro, 
			List<Integer> ingresos, ArrayList<DtoCentrosAtencion> listadoCentrosAtencionIngreso){
		
		ICitaOdontologicaMundo mundo = ManejoPacienteFabricaMundo.crearCitaOdontologicaMundo();	
		ArrayList<DtoResultadoConsultaIngresosOdonto> listadoIngresosOdonto = new ArrayList<DtoResultadoConsultaIngresosOdonto>();
		ArrayList<DtoIngresosOdontologicos> listadoEstadoCitasPorPaciente = new ArrayList<DtoIngresosOdontologicos>();
		ArrayList<DtoIngresosOdontologicos> listadoResultado = new ArrayList<DtoIngresosOdontologicos>();
		DtoIngresosOdontologicos infoPaciente = new DtoIngresosOdontologicos();
		List<Integer> pacientes = new ArrayList<Integer>();
		
		listadoEstadoCitasPorPaciente = mundo.obtenerEstadoCitasPorPaciente(ingresos, filtro);
		
		if (listadoEstadoCitasPorPaciente != null && listadoEstadoCitasPorPaciente.size() > 0) {
			for (DtoIngresosOdontologicos dto : listadoEstadoCitasPorPaciente) {
				pacientes.add(dto.getCodigoPaciente());
			}
		}
		
		HashSet hashset = new HashSet();
		hashset.addAll(pacientes);
		List<Integer> pacientesOrdenados = new ArrayList<Integer>();
		
		for (Object registro : hashset) {
			
			int codigoPaciente = Integer.parseInt(registro.toString());
			pacientesOrdenados.add(codigoPaciente);
			infoPaciente = new DtoIngresosOdontologicos();
			infoPaciente.setCodigoPaciente(codigoPaciente);
			
			for (DtoIngresosOdontologicos dto : listadoEstadoCitasPorPaciente) {
				
				if (codigoPaciente == dto.getCodigoPaciente()) {
					
					if (dto.getEstadoCitaOdonto().trim().equals(ConstantesIntegridadDominio.acronimoAtendida)) {
						infoPaciente.setCitasAtendidas(dto.getNumeroCitas());
						infoPaciente.setFechaAtendida(dto.getFechaHoraModifica());
						
					}else if (dto.getEstadoCitaOdonto().trim().equals(ConstantesIntegridadDominio.acronimoReservado)) {
						infoPaciente.setCitasReservadas(dto.getNumeroCitas());
						infoPaciente.setFechaReservada(dto.getFechaHoraModifica());
						
					}else if (dto.getEstadoCitaOdonto().trim().equals(ConstantesIntegridadDominio.acronimoAsignado)) {
						infoPaciente.setCitasAsignadas(dto.getNumeroCitas());
						infoPaciente.setFechaAsignada(dto.getFechaHoraModifica());
						
					}else if (dto.getEstadoCitaOdonto().trim().equals(ConstantesIntegridadDominio.acronimoAreprogramar)) {
						infoPaciente.setCitasAreprog(dto.getNumeroCitas());
						infoPaciente.setFechaAreprog(dto.getFechaHoraModifica());
						
					}else if (dto.getEstadoCitaOdonto().trim().equals(ConstantesIntegridadDominio.acronimoNoAsistio)) {
						infoPaciente.setCitasNoasis(dto.getNumeroCitas());
						infoPaciente.setFechaNoasis(dto.getFechaHoraModifica());
						
					}else if (dto.getEstadoCitaOdonto().trim().equals(ConstantesIntegridadDominio.acronimoNoAtencion)) {
						infoPaciente.setCitasNoaten(dto.getNumeroCitas());
						infoPaciente.setFechaNoaten(dto.getFechaHoraModifica());
						
					}else if (dto.getEstadoCitaOdonto().trim().equals(ConstantesIntegridadDominio.acronimoEstadoCancelado)) {
						infoPaciente.setCanceladas(dto.getNumeroCitas());
						infoPaciente.setFechaCancelada(dto.getFechaHoraModifica());
					}
					
				}
			}
			
			listadoResultado.add(infoPaciente);
		}
		
		listadoIngresosOdonto = ordenarInfoPacientes(listadoResultado, pacientesOrdenados, ingresos, filtro, listadoCentrosAtencionIngreso);
		
		return listadoIngresosOdonto;
	}
	
	/**
	 * Este mètodo se encarga de ordenar los pacientes
	 * en dos listados: pacientes con valoración inicial atendida , 
	 * pacientes sin valoración inicial atendida. Según las validaciones
	 * expresadas en el anexo 978.
	 *
	 * @param listado
	 * @param pacientes
	 * @param filtro 
	 *
	 * @autor Yennifer Guerrero
	 */
	public ArrayList<DtoResultadoConsultaIngresosOdonto> ordenarInfoPacientes(ArrayList<DtoIngresosOdontologicos> listado, 
			List<Integer> pacientes, List<Integer> ingresos, DtoReporteIngresosOdontologicos filtro, 
			ArrayList<DtoCentrosAtencion> listadoCentrosAtencionIngreso){
		
		ICitaOdontologicaMundo citaOdontoMundo = ManejoPacienteFabricaMundo.crearCitaOdontologicaMundo();
		ArrayList<DtoIngresosOdontologicos> listadoPacientesSinValIni = new ArrayList<DtoIngresosOdontologicos>();
		ArrayList<DtoIngresosOdontologicos> listadoPacientesConValIni = new ArrayList<DtoIngresosOdontologicos>();
		ArrayList<DtoResultadoConsultaIngresosOdonto> listadoIngresosOdontoSinValIni = new ArrayList<DtoResultadoConsultaIngresosOdonto>();
		ArrayList<DtoResultadoConsultaIngresosOdonto> listadoIngresosOdontoConValIni = new ArrayList<DtoResultadoConsultaIngresosOdonto>();
		ArrayList<DtoResultadoConsultaIngresosOdonto> listadoIngresosOdonto = new ArrayList<DtoResultadoConsultaIngresosOdonto>();
		String conValIni = filtro.getConValoracion();
		List<Integer> pacientesConValIni = new ArrayList<Integer>();
		List<Long> listaUltimaCitaPorPaciente = new ArrayList<Long>();
		
		for (DtoIngresosOdontologicos registro : listado) {
			
			if (registro.getCitasAtendidas() > 0 && registro.getCitasReservadas() == 0 && registro.getCitasAsignadas() == 0) {
				listadoPacientesConValIni.add(registro);
			}else if (registro.getCitasAtendidas() > 0 && (registro.getCitasAsignadas() > 0 || registro.getCitasReservadas() > 0)) {
				listadoPacientesSinValIni.add(registro);
			}else if (registro.getCitasAtendidas() == 0) {
				listadoPacientesSinValIni.add(registro);
			}
		}
		
		if (listadoPacientesConValIni != null && listadoPacientesConValIni.size() > 0) {
			for (DtoIngresosOdontologicos dto : listadoPacientesConValIni) {
				pacientesConValIni.add(dto.getCodigoPaciente());
			}
			
			listaUltimaCitaPorPaciente = citaOdontoMundo.obtenerCodigoCitasOdontoAtendidas(pacientesConValIni);
		}
		
		
		
		if (conValIni.equals(ConstantesBD.acronimoNo)){
			listadoIngresosOdontoSinValIni = consolidarInfoPacientesSinValIni(listadoPacientesSinValIni, listadoPacientesConValIni, pacientes, 
					ingresos, pacientesConValIni);
			listadoIngresosOdonto.addAll(listadoIngresosOdontoSinValIni);
			
		} else if (conValIni.equals(ConstantesBD.acronimoSi)) {
			
			listadoIngresosOdontoConValIni = consolidarReporteIngresosConValIni(filtro, ingresos, listaUltimaCitaPorPaciente);
			
			if (listadoIngresosOdontoConValIni != null && listadoIngresosOdontoConValIni.size() > 0) {
				listadoIngresosOdonto.addAll(listadoIngresosOdontoConValIni);
			}
			
			
		} else {
			listadoIngresosOdontoConValIni = consolidarReporteIngresosConValIni(filtro, ingresos, listaUltimaCitaPorPaciente);
			
			if (listadoPacientesSinValIni != null && listadoPacientesSinValIni.size() > 0) {
				listadoIngresosOdontoSinValIni = consolidarInfoPacientesSinValIni(listadoPacientesSinValIni, listadoPacientesConValIni, pacientes, 
						ingresos, pacientesConValIni);
			}
			
			
			if (listadoIngresosOdontoConValIni != null && listadoIngresosOdontoConValIni.size() > 0) {
				listadoIngresosOdonto.addAll(listadoIngresosOdontoConValIni);
			} 
			if (listadoIngresosOdontoSinValIni != null && listadoIngresosOdontoSinValIni.size() > 0) {
				listadoIngresosOdonto.addAll(listadoIngresosOdontoSinValIni);
			}
			
			if (listadoIngresosOdonto != null && listadoIngresosOdonto.size() > 0) {
				listadoIngresosOdonto = ordenarInfoReporteAmbos(listadoCentrosAtencionIngreso, listadoIngresosOdonto);
			}
		}
		
		return listadoIngresosOdonto;
		
	}
	
	/**
	 * Este mètodo se encarga de obtener el consolidado de la información 
	 * de las citas de valoración inicial no atendidas. 
	 *
	 * @param listadoPacientesSinValIni
	 * @param listadoPacientesConValIni
	 * @param pacientes
	 * @param ingresos
	 * @param pacientesConValIni 
	 * @return
	 *
	 * @autor Yennifer Guerrero
	 */
	private ArrayList<DtoResultadoConsultaIngresosOdonto> consolidarInfoPacientesSinValIni(
			ArrayList<DtoIngresosOdontologicos> listadoPacientesSinValIni, 
			ArrayList<DtoIngresosOdontologicos> listadoPacientesConValIni,
			List<Integer> pacientes, List<Integer> ingresos, List<Integer> pacientesConValIni) {
		
		ICitaOdontologicaMundo citaOdontoMundo = ManejoPacienteFabricaMundo.crearCitaOdontologicaMundo();	
		ArrayList<DtoIngresosOdontologicos> consolidadoPacientesSinValIni = new ArrayList<DtoIngresosOdontologicos>();
		ArrayList<DtoResultadoConsultaIngresosOdonto> consolidadoIngresosSinValIni = new ArrayList<DtoResultadoConsultaIngresosOdonto>();
		List<Integer> pacientesSinValIni = new ArrayList<Integer>();
		
		for (DtoIngresosOdontologicos dto : listadoPacientesSinValIni) {
			pacientesSinValIni.add(dto.getCodigoPaciente());
		}
		
		ArrayList<DtoIngresosOdontologicos> listaIngresosSinCitas= ingresosDAO.obtenerIngresosSinCitasOdonto(ingresos);
		ArrayList<DtoIngresosOdontologicos> listaIngresosConCitasDifValIni = citaOdontoMundo.obtenerCitaOdontoDiferenteDeValIni(ingresos, pacientes);
		
		ArrayList<DtoIngresosOdontologicos> listaIngresosValIniNoAtendidas = citaOdontoMundo.obtenerInfoPacientesSinValIni(pacientesSinValIni);
		
		//recorrer cada arraylist y asignar el estado de la cita.
		
		if (listaIngresosSinCitas != null && listaIngresosSinCitas.size() > 0) {
			
			for (DtoIngresosOdontologicos dto : listaIngresosSinCitas) {
				dto.setEstadoCitaOdonto("Sin cita");
			}
			
			consolidadoPacientesSinValIni.addAll(listaIngresosSinCitas);
		}
		
		if (listaIngresosConCitasDifValIni != null && listaIngresosConCitasDifValIni.size() > 0) {
			
			for (DtoIngresosOdontologicos dto : listaIngresosConCitasDifValIni) {
				dto.setEstadoCitaOdonto("Sin cita");
			}
			
			consolidadoPacientesSinValIni.addAll(listaIngresosConCitasDifValIni);
		}
		
		if (listaIngresosValIniNoAtendidas != null && listaIngresosValIniNoAtendidas.size() > 0) {
			
			for (DtoIngresosOdontologicos dto : listadoPacientesSinValIni) {
				
				List<String> fechaHora = new ArrayList<String>();
				
				if (dto.getCitasAtendidas() > 0 && (dto.getCitasAsignadas() > 0 || dto.getCitasReservadas() > 0)) {
					fechaHora.add(dto.getFechaAsignada());
					fechaHora.add(dto.getFechaReservada());
				}else{
					fechaHora.add(dto.getFechaAsignada());
					fechaHora.add(dto.getFechaReservada());
					fechaHora.add(dto.getFechaAreprog());
					fechaHora.add(dto.getFechaCancelada());
					fechaHora.add(dto.getFechaNoasis());
					fechaHora.add(dto.getFechaNoaten());
				}
				
				String prueba = Collections.max(fechaHora);
				
				if (dto.getFechaAsignada().trim().equals(prueba)) {
					dto.setEstadoCitaOdonto(ConstantesIntegridadDominio.acronimoAsignado);
					
				}else if (dto.getFechaCancelada().trim().equals(prueba)) {
					dto.setEstadoCitaOdonto(ConstantesIntegridadDominio.acronimoEstadoCancelado);
					
				}else if (dto.getFechaAreprog().trim().equals(prueba)) {
					dto.setEstadoCitaOdonto(ConstantesIntegridadDominio.acronimoAreprogramar);
					
				}else if (dto.getFechaNoasis().trim().equals(prueba)) {
					dto.setEstadoCitaOdonto(ConstantesIntegridadDominio.acronimoNoAsistio);
					
				}else if (dto.getFechaNoaten().trim().equals(prueba)) {
					dto.setEstadoCitaOdonto(ConstantesIntegridadDominio.acronimoNoAtencion);
					
				}else if (dto.getFechaReservada().trim().equals(prueba)) {
					dto.setEstadoCitaOdonto(ConstantesIntegridadDominio.acronimoReservado);
				}
				
				Log4JManager.info("fecha hora:" + dto.getCodigoPaciente());
				Log4JManager.info("fecha hora:" + dto.getNombreEstadoCita());
				
				for (DtoIngresosOdontologicos infoPaciente : listaIngresosValIniNoAtendidas) {
					
					if (infoPaciente.getCodigoPaciente() == dto.getCodigoPaciente()) {
						infoPaciente.setEstadoCitaOdonto(dto.getEstadoCitaOdonto());
					}
					
				}
				
			}
			
			consolidadoPacientesSinValIni.addAll(listaIngresosValIniNoAtendidas);
		}
		
		
		if (consolidadoPacientesSinValIni != null && consolidadoPacientesSinValIni.size() > 0) {
			accionOrdenar(consolidadoPacientesSinValIni);
			Collections.sort(consolidadoPacientesSinValIni);
			boolean esValIni = false;
			
			consolidadoIngresosSinValIni = ordenarDatosConRompimiento(consolidadoPacientesSinValIni, esValIni);
			
		}else{
			return null;
		}
		
		return consolidadoIngresosSinValIni;
	}

	/* (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.manejoPaciente.IIngresosMundo#consultarIngresosConsultaExternaPorFechaHoraEstadoCita(int, java.util.Date, java.lang.String, int)
	 */
	@Override
	public List<DtoIngresos> consultarIngresosConsultaExternaPorFechaHoraEstadoCita(
			int codigoPaciente, Date fecha, String hora, int estadoCita) {
		return ingresosDAO.consultarIngresosConsultaExternaPorFechaHoraEstadoCita(codigoPaciente, fecha, hora, estadoCita);
	}
	
	
}
