package com.princetonsa.action.odontologia;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JasperPrint;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.reportes.GeneradorReporte;

import com.princetonsa.actionform.odontologia.ReporteConsultaPacientesEstadoPresupuestoForm;
import com.princetonsa.dto.administracion.DtoIntegridadDominio;
import com.princetonsa.dto.comun.DtoCheckBox;
import com.princetonsa.dto.manejoPaciente.DtoCentrosAtencion;
import com.princetonsa.dto.odontologia.DTOCentroAtencionReportePacientesEstadoPresupuesto;
import com.princetonsa.dto.odontologia.DTOEstadosReportePacientesEstadoPresupuesto;
import com.princetonsa.dto.odontologia.DTOInstitucionReportePacientesEstadoPresupuesto;
import com.princetonsa.dto.odontologia.DTOPacientesReportePacientesEstadoPresupuesto;
import com.princetonsa.dto.odontologia.DtoReporteConsultaPacienteEstadoPresupuesto;
import com.princetonsa.enu.impresion.EnumTiposSalida;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.servinte.axioma.generadorReporte.odontologia.pacientesEstadoPresupuesto.GeneradorReporteDetallePacientesEstadoPresupuesto;
import com.servinte.axioma.generadorReporte.odontologia.pacientesEstadoPresupuesto.GeneradorReportePacientesEstadoPresupuesto;
import com.servinte.axioma.generadorReporte.odontologia.pacientesEstadoPresupuesto.GeneradorReportePlanoDetallePacientesEstadoPresupuesto;
import com.servinte.axioma.generadorReporte.odontologia.pacientesEstadoPresupuesto.GeneradorReportePlanoPacientesEstadoPresupuesto;
import com.servinte.axioma.orm.Ciudades;
import com.servinte.axioma.orm.PaquetesOdontologicos;
import com.servinte.axioma.orm.Programas;
import com.servinte.axioma.orm.RecomSerproSerpro;
import com.servinte.axioma.persistencia.UtilidadTransaccion;
import com.servinte.axioma.servicio.fabrica.facturacion.FacturacionServicioFabrica;
import com.servinte.axioma.servicio.fabrica.odontologia.administracion.AdministracionFabricaServicio;
import com.servinte.axioma.servicio.fabrica.odontologia.facturacion.PaquetesOdontologicosFabricaServicio;
import com.servinte.axioma.servicio.fabrica.odontologia.presupuesto.PresupuestoFabricaServicio;
import com.servinte.axioma.servicio.interfaz.odontologia.facturacion.IPaquetesOdontologicosServicio;
import com.servinte.axioma.servicio.interfaz.odontologia.presupuesto.IPresupuestoOdontologicoServicio;
import com.servinte.axioma.servicio.interfaz.tesoreria.ILocalizacionServicio;


/**
 * 
 * Clase usada para controlar los procesos de la
 * funcionalidad de Consulta Pacientes por Estados del Presupuesto
 * @author Angela Maria Aguirre
 * @since 28/10/2010
 */
public class ReporteConsultaPacientesEstadoPresupuestoAction extends  Action {
	
	private static String ESTADO_PRECONTRATADO_CON_SOLICITUD="Precontratado -  Con solicitud Descuento";
	private static String ESTADO_PRECONTRATADO_SIN_SOLICITUD="Precontratado -  Sin solicitud Descuento";
	private static String SEXO_AMBOS = "Femenino - Masculino";
	private static String ETIQUETA_TODAS="Todas";
	private static String ETIQUETA_TODOS="Todos";
	
	/**
	 * Método execute de la clase.
	 */
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {	
		
		ActionForward forward=null;
		if (form instanceof ReporteConsultaPacientesEstadoPresupuestoForm) {	
			
			ReporteConsultaPacientesEstadoPresupuestoForm forma = (
					ReporteConsultaPacientesEstadoPresupuestoForm) form;
			
			InstitucionBasica ins = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");			
			UsuarioBasico usuario = Utilidades.getUsuarioBasicoSesion(request
					.getSession());
			
			String estado = forma.getEstado();
			Log4JManager.info("estado " + estado);

			try {
				UtilidadTransaccion.getTransaccion().begin();
				if (estado.equals("empezar")) {
					forward= empezar(forma, usuario, mapping, ins);
				}else{
					if(estado.equals("cambiarPais")){
						listarCiudades(forma);
						forma.setTipoSalida(null);
						forma.setEnumTipoSalida(null);
						forward= mapping.findForward("principal");
					}else{
						if(estado.equals("cambiarEmpresaInstitucion")){
							listarCentrosAtencion(forma);
							forma.setTipoSalida(null);
							forma.setEnumTipoSalida(null);
							forward= mapping.findForward("principal");
						}else{
							if(estado.equals("cambiarEstadoPresupuesto")){
								forward= listarSegunEstadoPresupuesto(forma,mapping);
							}else{
								if(estado.equals("cambiarCiudad")){
									listarCentrosAtencion(forma);
									forward= cambiarCiudad(forma, mapping);
								}else{
									if(estado.equals("cambiarRegion")){
										listarCentrosAtencion(forma);
										forward= cambiarRegion(forma, mapping);
									}else{
										if(estado.equals("consultar")){
											forward= consultar(forma,mapping);
										}else{
											if(estado.equals("seleccionarPaquete")){
												forward= cambiarPaquete(forma,mapping);
											}else{
												if(estado.equals("mostrarDetalle")){													
													forward= consultarPacientesPorEstadoPresupuesto(forma, mapping);													
												}else{
													if (estado.equals("imprimirEstadoPresupuesto") || 
															estado.equals("imprimirPacientesEstadoPresupuesto")) {
														if (forma.getTipoSalida() != null && !forma.getTipoSalida().trim().equals("-1")) {
															forward= imprimirReporte(forma,mapping,usuario);
														}
													}else{
														if(estado.equals("mostrarEstadoPresupuesto")){
															forward= mapping.findForward("mostrarEstadoPresupuesto");
														}else{
															if(estado.equals("principal")){
																forward= mapping.findForward("principal");																	
															}else if(estado.equals("adicionarPrograma")){
																forma.getDtoFiltros().setCodigoPaqueteOdonto(ConstantesBD.codigoNuncaValido);
																forward= mapping.findForward("principal");																	
															}
														}
													}													
												}
											}
										}
									}
								}
							}
						}
					}
				}
				UtilidadTransaccion.getTransaccion().commit();
			}catch (Exception e) {
				UtilidadTransaccion.getTransaccion().rollback();
				Log4JManager.error("Error generando el reporte de consulta paciente por estado del presupuesto", e);
			}
		}
		return forward;
	}
	
	/**
	 * Este método se encarga de inicializar los valores de los objetos de la
	 * página para la búsqueda de los datos del reporte
	 * @param forma
	 * @param usuario
	 * @param mapping
	 * @return
	 * 
	 * @author Angela Aguirre
	 * @param ins 
	 */
	public ActionForward empezar(ReporteConsultaPacientesEstadoPresupuestoForm forma,
			UsuarioBasico usuario, ActionMapping mapping, InstitucionBasica ins)
			throws Exception {
		
		forma.reset();
				
		int codigoInstitucion = usuario.getCodigoInstitucionInt();
		
		forma.getDtoFiltros().setRutaLogo(ins.getLogoJsp());
		forma.getDtoFiltros().setUbicacionLogo(ins.getUbicacionLogo());
		forma.getDtoFiltros().setRazonSocial(ins.getRazonSocial());
		forma.setListaPaises(AdministracionFabricaServicio.crearLocalizacionServicio().listarPaises());
		String codigoPaisResidencia = ValoresPorDefecto.getPaisResidencia(codigoInstitucion);		
		if(!UtilidadTexto.isEmpty(codigoPaisResidencia)){
			String[] codigosPais=codigoPaisResidencia.split("-");
			forma.getDtoFiltros().setCodigoPais(codigosPais[0]);
		}
				
		listarCiudades(forma);
		listarRegiones(forma);
		
		if(ValoresPorDefecto.getInstitucionMultiempresa(codigoInstitucion).equals(ConstantesBD.acronimoSi)){
			forma.setMostarFiltroInstitucion(true);
			forma.setListaEmpresaInstitucion(
					FacturacionServicioFabrica.crearEmpresasInstitucionServicio().listarEmpresaInstitucion());
			if(forma.getListaEmpresaInstitucion()!=null && forma.getListaEmpresaInstitucion().size()==1){
				forma.getDtoFiltros().setCodigoEmpresaInstitucion(
						forma.getListaEmpresaInstitucion().get(0).getCodigo());
			}
			forma.getDtoFiltros().setEsMultiempresa(true);
		}else{
			forma.getDtoFiltros().setEsMultiempresa(false);
		}
		listarCentrosAtencion(forma);		
		listarSexoPaciente(forma);
		listarEstadoPresupuesto(forma, usuario);
		listarPaquetesOdontologicos(forma);
		
		int institucion = usuario.getCodigoInstitucionInt();
		String utilizaProgramasOdonto = ValoresPorDefecto.getUtilizanProgramasOdontologicosEnInstitucion(institucion);
		if(utilizaProgramasOdonto.equals(ConstantesBD.acronimoSi)){
			forma.setMostarFiltroPrograma(true);
		}
		
		return mapping.findForward("principal");
	}	
	
	/**
	 * 
	 * Este método se encarga de incializar o no la lista de 
	 * ciudades dependiendo de la selección de regiones hecha
	 *  
	 * @param forma
	 * @param mapping
	 * 
	 * @author Angela Aguirre
	 */
	public ActionForward cambiarRegion(ReporteConsultaPacientesEstadoPresupuestoForm forma,
			ActionMapping mapping) {
		
		Long codigoRegion= forma.getDtoFiltros().getCodigoRegion();		
		if(!(codigoRegion.equals(ConstantesBD.codigoNuncaValidoLong))){
			forma.getDtoFiltros().setCiudadDeptoPais("");
			listarCentrosAtencion(forma);
		}
		return mapping.findForward("principal");
	}
	
	/**
	 * 
	 * Este método se encarga de incializar o no la lista de 
	 * regiones dependiendo de la selección de ciudaes hecha
	 *  
	 * @param forma
	 * @param mapping
	 * 
	 * @author Angela Aguirre
	 */
	public ActionForward cambiarCiudad(ReporteConsultaPacientesEstadoPresupuestoForm forma,
			ActionMapping mapping) {
		
		String codigoCiudad= forma.getDtoFiltros().getCiudadDeptoPais();
		if(!UtilidadTexto.isEmpty(codigoCiudad)&& !(codigoCiudad.trim().equals("-1"))){
			forma.getDtoFiltros().setCodigoRegion(ConstantesBD.codigoNuncaValidoLong);
			listarCentrosAtencion(forma);
		}			
		return mapping.findForward("principal");
	}
	
	
	/**
	 * Este método se encarga de listar las ciudades existentes en el sistema
	 * que pertenecen a un determinado país.
	 * 
	 * @param forma 
	 * @author Angela Aguirre
	 */
	private void listarCiudades(ReporteConsultaPacientesEstadoPresupuestoForm forma) {
		if(!UtilidadTexto.isEmpty(forma.getDtoFiltros().getCodigoPais())){
			
			forma.setListaCiudades(AdministracionFabricaServicio.crearLocalizacionServicio()
					.listarCiudadesPorPais(forma.getDtoFiltros().getCodigoPais()));
			
			if(forma.getListaCiudades()!=null && forma.getListaCiudades().size()==1){
				Ciudades ciudad = forma.getListaCiudades().get(0);
				
				String codigoCiudad=ciudad.getId().getCodigoCiudad()+ ConstantesBD.separadorSplit
				+ ciudad.getDepartamentos().getId().getCodigoDepartamento()+ ConstantesBD.separadorSplit
				+ ciudad.getPaises().getCodigoPais();
				
				forma.getDtoFiltros().setCiudadDeptoPais(codigoCiudad);
			}
		}		
	}

	/**
	 * Este método se encarga de listar las ciudades existentes en el sistema
	 * que pertenecen a un determinado país.
	 * 
	 * @param forma 
	 * @author Angela Aguirre
	 */
	private void listarRegiones(ReporteConsultaPacientesEstadoPresupuestoForm forma) {
		forma.setListaRegiones(AdministracionFabricaServicio.crearLocalizacionServicio()
				.listarRegionesCoberturaActivas());		
	}
	
	/**
	 * Este método se encarga de listar los centros de atención del sistema
	 * de acuerdo a unos criterios específicos.
	 * 
	 * @param forma
	 * @author Angela Aguirre
	 */
	public void listarCentrosAtencion(ReporteConsultaPacientesEstadoPresupuestoForm forma) {
		
		ILocalizacionServicio servicio = AdministracionFabricaServicio.crearLocalizacionServicio();
		ArrayList<DtoCentrosAtencion> listaCentrosAtencion = null;
		long empresaInstitucion = forma.getDtoFiltros().getCodigoEmpresaInstitucion();
		String ciudadDtoPais= forma.getDtoFiltros().getCiudadDeptoPais();
		long codigoRegion = forma.getDtoFiltros().getCodigoRegion();		
			
		if(empresaInstitucion != ConstantesBD.codigoNuncaValidoLong && empresaInstitucion!=0){
			if(!UtilidadTexto.isEmpty(ciudadDtoPais) && !ciudadDtoPais.trim().equals("-1")){								
				String codigos[]=ciudadDtoPais.split(ConstantesBD.separadorSplit);				
				//lista todos por empresa institucion y ciudad
				listaCentrosAtencion = servicio.listarTodosPorEmpresaInstitucionYCiudad(
						empresaInstitucion, codigos[0],codigos[2], codigos[1]);
			}else{
				if(codigoRegion!=ConstantesBD.codigoNuncaValidoLong &&
						codigoRegion != 0){
					//lista todos por region y empresa institucion
					listaCentrosAtencion = servicio.listarTodosPorEmpresaInstitucionYRegion(
							empresaInstitucion, codigoRegion);
				}	
			}
		}
		if(listaCentrosAtencion==null){
			//lista todos los centros de atención del sistema
			 listaCentrosAtencion =  AdministracionFabricaServicio.crearLocalizacionServicio().listarTodosCentrosAtencion();
		}
		
		if(listaCentrosAtencion!=null && listaCentrosAtencion.size()==1){
			forma.getDtoFiltros().setConsecutivoCentroAtencion(listaCentrosAtencion.get(0).getConsecutivo());
		}
		forma.setListaCentrosAtencion(listaCentrosAtencion);
	}
	
	/**
	 * Este método se encarga de listar los tipos de sexos registrados.
	 * @param forma 
	 * @author Angela Aguirre
	 */
	private void listarSexoPaciente(ReporteConsultaPacientesEstadoPresupuestoForm forma) {
		ArrayList<DtoCheckBox> lista = new ArrayList<DtoCheckBox>();		
		Connection con=UtilidadBD.abrirConexion();		
		ArrayList<HashMap<String, Object>> listadoSexos = Utilidades.obtenerSexos(con);		
		UtilidadBD.closeConnection(con);		
		for (HashMap<String, Object> hashMap : listadoSexos) {			
			DtoCheckBox dto = new DtoCheckBox();
			dto.setCodigo(hashMap.get("codigo").toString());
			dto.setNombre(hashMap.get("nombre").toString());
			lista.add(dto);
		}		
		forma.setListaSexoPaciente(lista);
	}
	
	/**
	 * Este método se encarga de obtener el listado con los estado 
	 * del presupuesto.
	 * @param usuario 
	 * @param forma 
	 * @author Angela Aguirre
	 */
	private void listarEstadoPresupuesto(ReporteConsultaPacientesEstadoPresupuestoForm forma,
			UsuarioBasico usuario) {
		
		IPresupuestoOdontologicoServicio servicio = PresupuestoFabricaServicio.crearPresupuestoOdontologicoServicio();
		int codigoInstitucion = usuario.getCodigoInstitucionInt();
		
		String validaPresupuestoContratado = ValoresPorDefecto
			.getValidaPresupuestoOdoContratado(codigoInstitucion);
		
		ArrayList<DtoIntegridadDominio> listaEstadosPresupuesto = servicio
			.listarEstadoPresupuestoOdonto(validaPresupuestoContratado);
		
		forma.setListadoEstadosPresupuesto(listaEstadosPresupuesto);
	}
	
	/**
	 * Este método se encarga de obtener el listado con los indicativos 
	 * de contrato del presupuesto odontológico.
	 * 
	 * @param mapping 
	 * @param forma
	 * @author Angela Aguirre
	 */
	public ActionForward listarSegunEstadoPresupuesto(
			ReporteConsultaPacientesEstadoPresupuestoForm forma, ActionMapping mapping) {
		
		if(forma.getDtoFiltros().getEstadoPresupuesto().equals(ConstantesIntegridadDominio.acronimoContratado)){
			IPresupuestoOdontologicoServicio servicio = 
				PresupuestoFabricaServicio.crearPresupuestoOdontologicoServicio();		
			ArrayList<DtoIntegridadDominio> listaIndicativoContrato = servicio.listarIndicativoContrato();
			
			forma.setListadoIndicativoContrato(listaIndicativoContrato);
			forma.setMostarFiltroIndicativoContrato(true);
			forma.setMostarFiltroSolicitudAutorDescuento(false);
		}else{
			if(forma.getDtoFiltros().getEstadoPresupuesto().equals(
					ConstantesIntegridadDominio.acronimoPrecontratado)){
				forma.setMostarFiltroSolicitudAutorDescuento(true);
				forma.setMostarFiltroIndicativoContrato(false);
			}else{
				forma.setMostarFiltroIndicativoContrato(false);
				forma.setMostarFiltroSolicitudAutorDescuento(false);
			}
		}
		
		return mapping.findForward("principal"); 
	}
	
	/**
	 * Este método se encarga de listar los paquetes odontológicos
	 * creados en el sistema.
	 * @param forma
	 * @author Angela Aguirre
	 */
	public void listarPaquetesOdontologicos(ReporteConsultaPacientesEstadoPresupuestoForm forma) {
		
		IPaquetesOdontologicosServicio servicio = 
			PaquetesOdontologicosFabricaServicio.crearIPaquetesOdontologicosServicio();
		
		String codigoPaquete = null;
		String  descripcionPaquete = null; 
		int codigoEspecialidad = ConstantesBD.codigoNuncaValido;
		
		ArrayList<PaquetesOdontologicos> listadoPaquetesOdonto = servicio
	 	.listarPaquetesOdontologicos(codigoPaquete, descripcionPaquete, codigoEspecialidad);
		
		forma.setListadoPaquetesOdonto(listadoPaquetesOdonto);	
	}
	
	/**
	 * 
	 * Este Método se encarga de realizar la consulta de los datos
	 * para la generación del reporte
	 * @param ReporteConsultaPacientesEstadoPresupuestoForm forma,
			ActionMapping mapping
	 * @return ActionForward
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ActionForward consultar(ReporteConsultaPacientesEstadoPresupuestoForm forma,
			ActionMapping mapping){
		
		IPresupuestoOdontologicoServicio presupuestoServicio = 
			PresupuestoFabricaServicio.crearPresupuestoOdontologicoServicio();
		
		ArrayList<DTOInstitucionReportePacientesEstadoPresupuesto> listaInstitucion =
			presupuestoServicio.buscarPresupuestoOdontologicoPorEstadoEstructurado(forma.getDtoFiltros());
			
		if(listaInstitucion!=null && listaInstitucion.size()>0){
			forma.setListaInstituciones(listaInstitucion);
			asignarNombresFiltros(forma);
		}else{
			forma.setEstado("sinDatos");
			forma.setListaInstituciones(
					new ArrayList<DTOInstitucionReportePacientesEstadoPresupuesto>());
			return mapping.findForward("principal");
		}		
		return mapping.findForward("mostrarEstadoPresupuesto"); 
	}
	
	/**
	 * 
	 * Este Método se encarga de
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ActionForward cambiarPaquete(ReporteConsultaPacientesEstadoPresupuestoForm forma,
			ActionMapping mapping){
		if(forma.getDtoFiltros().getCodigoPaqueteOdonto()!=ConstantesBD.codigoNuncaValidoLong){
			forma.setDtoSerProSerPro(new RecomSerproSerpro());
			forma.getDtoSerProSerPro().setProgramas(new Programas());
			forma.getDtoFiltros().setCodigoPrograma(ConstantesBD.codigoNuncaValidoLong);
			forma.setListaCodigoProgramaServicios("");
		}
		
		return mapping.findForward("principal");		
	}
	
	
	/**
	 * 
	 * Este Método se encarga de consultar los pacientes por estado del 
	 * presupuesto con su respectivo valor totalizado.
	 * 
	 * @param ReporteConsultaPacientesEstadoPresupuestoForm forma,
	 *		  ActionMapping mapping
	 * @return 	ActionForward
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ActionForward consultarPacientesPorEstadoPresupuesto(ReporteConsultaPacientesEstadoPresupuestoForm forma,
			ActionMapping mapping){
		
		IPresupuestoOdontologicoServicio presupuestoServicio = 
			PresupuestoFabricaServicio.crearPresupuestoOdontologicoServicio();
		
		int indiceInstitucion = forma.getIndiceInstitucion();
		int indiceCentroAtencion = forma.getIndiceCA();
		int indiceEstado = forma.getIndiceEstado();
		DtoReporteConsultaPacienteEstadoPresupuesto dtoConsulta = new DtoReporteConsultaPacienteEstadoPresupuesto();
		
		DTOInstitucionReportePacientesEstadoPresupuesto institucion = 
			forma.getListaInstituciones().get(indiceInstitucion);
		
		DTOCentroAtencionReportePacientesEstadoPresupuesto centroAtencion =
			institucion.getListaCentroAtencion().get(indiceCentroAtencion);
		
		DTOEstadosReportePacientesEstadoPresupuesto estado = centroAtencion.getListaEstados().get(indiceEstado);
				
		if(estado.getNombreEstado().equals(ESTADO_PRECONTRATADO_CON_SOLICITUD)){
			
			dtoConsulta.setEstadoPresupuesto(ConstantesIntegridadDominio.acronimoPrecontratado);
			dtoConsulta.setConSolicitudDcto(ConstantesBD.acronimoSi);
			
		}else if(estado.getNombreEstado().equals(ESTADO_PRECONTRATADO_SIN_SOLICITUD)){
			
			dtoConsulta.setEstadoPresupuesto(ConstantesIntegridadDominio.acronimoPrecontratado);
			dtoConsulta.setConSolicitudDcto(ConstantesBD.acronimoNo);
				
		}else if(estado.getNombreEstado().equals(
				(String)ValoresPorDefecto.getIntegridadDominio(
						ConstantesIntegridadDominio.acronimoEstadoActivo))){				
			dtoConsulta.setEstadoPresupuesto(ConstantesIntegridadDominio.acronimoEstadoActivo);
			
		}else if(estado.getNombreEstado().equals(
				(String)ValoresPorDefecto.getIntegridadDominio(
						ConstantesIntegridadDominio.acronimoInactivo))){
			
			dtoConsulta.setEstadoPresupuesto(ConstantesIntegridadDominio.acronimoInactivo);
			
		}else if(estado.getNombreEstado().equals(
				(String)ValoresPorDefecto.getIntegridadDominio(
						ConstantesIntegridadDominio.acronimoContratadoContratado))){
			
			dtoConsulta.setEstadoPresupuesto(ConstantesIntegridadDominio.acronimoContratado);
			dtoConsulta.setIndicativoContrato(ConstantesIntegridadDominio.acronimoContratadoContratado);
			
		}else if(estado.getNombreEstado().equals(
				(String)ValoresPorDefecto.getIntegridadDominio(
						ConstantesIntegridadDominio.acronimoContratadoSuspendidoTemporalmente))){
			
			dtoConsulta.setEstadoPresupuesto(ConstantesIntegridadDominio.acronimoContratado);
			dtoConsulta.setIndicativoContrato(ConstantesIntegridadDominio.acronimoContratadoSuspendidoTemporalmente);		
			
		}else if(estado.getNombreEstado().equals(
				(String)ValoresPorDefecto.getIntegridadDominio(
						ConstantesIntegridadDominio.acronimoContratadoTerminado))){
			
			dtoConsulta.setEstadoPresupuesto(ConstantesIntegridadDominio.acronimoContratado);
			dtoConsulta.setIndicativoContrato(ConstantesIntegridadDominio.acronimoContratadoTerminado);
			
		}else if(estado.getNombreEstado().equals(
				(String)ValoresPorDefecto.getIntegridadDominio(
						ConstantesIntegridadDominio.acronimoContratadoCancelado))){				
			
			dtoConsulta.setEstadoPresupuesto(ConstantesIntegridadDominio.acronimoContratado);
			dtoConsulta.setIndicativoContrato(ConstantesIntegridadDominio.acronimoContratadoCancelado);
		}
		dtoConsulta.setFechaInicial(forma.getDtoFiltros().getFechaInicial());
		dtoConsulta.setFechaFinal(forma.getDtoFiltros().getFechaFinal());
		dtoConsulta.setCodigoPais(forma.getDtoFiltros().getCodigoPais());
		dtoConsulta.setConsecutivoCentroAtencion(centroAtencion.getCodigoCentroAtencion());
		dtoConsulta.setCodigoPaqueteOdonto(forma.getDtoFiltros().getCodigoPaqueteOdonto());
		dtoConsulta.setCodigoPrograma(forma.getDtoFiltros().getCodigoPrograma());
		dtoConsulta.setEdadInicial(forma.getDtoFiltros().getEdadInicial());
		dtoConsulta.setEdadFinal(forma.getDtoFiltros().getEdadFinal());
		dtoConsulta.setSexoPaciente(forma.getDtoFiltros().getSexoPaciente());
		
		forma.getDtoFiltros().setNombreEstadoPresupuesto(estado.getNombreEstado());
		
		ArrayList<DTOPacientesReportePacientesEstadoPresupuesto>  listaPacientes = 
			presupuestoServicio.buscarPacientesEstadoPresupuestoTotalizado(dtoConsulta);
		
		if(listaPacientes!=null && listaPacientes.size()>0){
			estado.setListaPacientes(listaPacientes);		
			
			(((forma.getListaInstituciones().get(indiceInstitucion)).getListaCentroAtencion().get(indiceCentroAtencion))
				.getListaEstados().get(indiceEstado)).setListaPacientes(listaPacientes);
			
			forma.setListaPacientes(listaPacientes);
			
		}		
		
		return mapping.findForward("mostrarPacientesEstadoPresupuesto");
	}
	
	
	/**
	 * 
	 * Este Método se encarga de g
	 * @author, Angela Maria Aguirre
	 *
	 */
	private ActionForward imprimirReporte(ReporteConsultaPacientesEstadoPresupuestoForm forma, 
			ActionMapping mapping,UsuarioBasico usuario) {
		
		int tipoSalida 	= Integer.parseInt(forma.getTipoSalida());
		String nombreArchivo="";		
		String nombreReporte="";
		GeneradorReporte generadorReporte = null;
		String estadoRetorno="";
		if (tipoSalida > 0) {
			
			DtoReporteConsultaPacienteEstadoPresupuesto dtoFiltros = forma.getDtoFiltros();
			dtoFiltros.setUsuarioProceso(usuario.getLoginUsuario());
			
			if(forma.getEstado().equals("imprimirEstadoPresupuesto")){
				nombreReporte="Reporte Pacientes Por Estado del Presupuesto";
				ArrayList<DTOInstitucionReportePacientesEstadoPresupuesto> listadoReporte= forma.getListaInstituciones();
				
				if(tipoSalida == EnumTiposSalida.PLANO.getCodigo() ) {
					generadorReporte = new GeneradorReportePlanoPacientesEstadoPresupuesto(listadoReporte);
				}else{
					generadorReporte = new GeneradorReportePacientesEstadoPresupuesto(listadoReporte, dtoFiltros);
					
				}
				estadoRetorno = "mostrarEstadoPresupuesto";				
				
			}else{
				nombreReporte="Reporte Detalle Pacientes Por Estado del Presupuesto";	
				
				DTOCentroAtencionReportePacientesEstadoPresupuesto dtoCentroAtencion=new DTOCentroAtencionReportePacientesEstadoPresupuesto();
				dtoCentroAtencion = forma.getListaInstituciones().get(forma.getIndiceInstitucion())
					.getListaCentroAtencion().get(forma.getIndiceCA());
				
				DTOEstadosReportePacientesEstadoPresupuesto estadoSeleccionado = new DTOEstadosReportePacientesEstadoPresupuesto();
				estadoSeleccionado = forma.getListaInstituciones().get(forma.getIndiceInstitucion())
				.getListaCentroAtencion().get(forma.getIndiceCA()).getListaEstados().get(forma.getIndiceEstado());	
				
				estadoSeleccionado.setListaPacientes(forma.getListaPacientes());
				
				ArrayList<DTOEstadosReportePacientesEstadoPresupuesto> listaEstado = 
					new ArrayList<DTOEstadosReportePacientesEstadoPresupuesto>();
				listaEstado.add(estadoSeleccionado);
				
				if(tipoSalida == EnumTiposSalida.PLANO.getCodigo() ) {
					generadorReporte = new GeneradorReportePlanoDetallePacientesEstadoPresupuesto(dtoCentroAtencion, dtoFiltros,listaEstado);
				}else{
					generadorReporte = new GeneradorReporteDetallePacientesEstadoPresupuesto(dtoCentroAtencion, dtoFiltros,listaEstado);
				}			
								
				estadoRetorno = "mostrarPacientesEstadoPresupuesto";
			}
			
			JasperPrint reporte = generadorReporte.generarReporte();
			
			if (tipoSalida == EnumTiposSalida.PDF.getCodigo() ) {
				forma.setEnumTipoSalida(EnumTiposSalida.PDF);
				
			} else if (tipoSalida == EnumTiposSalida.HOJA_CALCULO.getCodigo() ) {
				forma.setEnumTipoSalida(EnumTiposSalida.HOJA_CALCULO);
				
			} else if (tipoSalida == EnumTiposSalida.PLANO.getCodigo() ) {
				forma.setEnumTipoSalida(EnumTiposSalida.PLANO);
			} 
			
			switch (forma.getEnumTipoSalida()) {
			
			case PDF:
				nombreArchivo = generadorReporte.exportarReportePDF(reporte, nombreReporte);
				break;
				
			case PLANO:
				nombreArchivo = generadorReporte.exportarReporteTextoPlano(reporte, nombreReporte);
				break;
				
			case HOJA_CALCULO:
				nombreArchivo = generadorReporte.exportarReporteExcel(reporte, nombreReporte);
				break;
			}	
			forma.setNombreArchivoGenerado(nombreArchivo);
			//JasperViewer.viewReport(reporte, false);			
		}
		forma.setTipoSalida("");
		forma.setEnumTipoSalida(null);
		
		return mapping.findForward(estadoRetorno);
	}
	
	/**
	 * Este método se encarga de asignar los nombres de los 
	 * filtros seleccionados por el usuario
	 * 
	 * @param forma
	 */
	private void asignarNombresFiltros(ReporteConsultaPacientesEstadoPresupuestoForm forma){
		if(UtilidadTexto.isEmpty(forma.getDtoFiltros().getSexoPaciente())){
			forma.getDtoFiltros().setNombreSexo(SEXO_AMBOS);
		}else{
			for( DtoCheckBox registro: forma.getListaSexoPaciente()){
				if(registro.getCodigo().equals(forma.getDtoFiltros().getSexoPaciente())){
					forma.getDtoFiltros().setNombreSexo(registro.getNombre());
					break;
				}
			}
		}
		
		if(forma.getDtoFiltros().getEdadInicial()==null){
			forma.getDtoFiltros().setEtiquetaEdadPaciente(ETIQUETA_TODAS);
		}else{
			forma.getDtoFiltros().setEtiquetaEdadPaciente(
					forma.getDtoFiltros().getEdadInicial().toString() + " - " + 
					forma.getDtoFiltros().getEdadFinal() + " Años");
		}
		
		if(forma.getDtoFiltros().getCodigoPaqueteOdonto()==ConstantesBD.codigoNuncaValido){
			forma.getDtoFiltros().setNombrePaquete(ETIQUETA_TODOS);
		}else{
			for(PaquetesOdontologicos paquete : forma.getListadoPaquetesOdonto()){
				if(paquete.getCodigoPk()==forma.getDtoFiltros().getCodigoPaqueteOdonto()){
					forma.getDtoFiltros().setNombrePaquete(paquete.getDescripcion());
					break;
				}
			}
		}
		
		if(forma.getDtoFiltros().getCodigoPrograma()==ConstantesBD.codigoNuncaValidoLong){
			forma.getDtoFiltros().setNombrePrograma(ETIQUETA_TODOS);
		}else{
			forma.getDtoFiltros().setNombrePrograma(
					forma.getDtoSerProSerPro().getProgramas().getNombre());
			
		}
	}
	
}
