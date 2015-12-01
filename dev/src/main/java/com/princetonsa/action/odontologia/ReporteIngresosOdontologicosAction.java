package com.princetonsa.action.odontologia;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JasperPrint;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.odontologia.ReporteIngresosOdontologicosForm;
import com.princetonsa.dto.administracion.DtoIntegridadDominio;
import com.princetonsa.dto.administracion.DtoPersonas;
import com.princetonsa.dto.comun.DtoCheckBox;
import com.princetonsa.dto.manejoPaciente.DtoCentrosAtencion;
import com.princetonsa.dto.odontologia.DtoReporteIngresosOdontologicos;
import com.princetonsa.dto.odontologia.DtoResultadoConsultaIngresosOdonto;
import com.princetonsa.enu.impresion.EnumTiposSalida;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.servinte.axioma.generadorReporte.odontologia.ingresosOdontologicos.GeneradorReporteIngresoOdontologico;
import com.servinte.axioma.generadorReporte.odontologia.ingresosOdontologicos.GeneradorReporteIngresosOdontoArchivoPlano;
import com.servinte.axioma.hibernate.HibernateUtil;
import com.servinte.axioma.orm.Especialidades;
import com.servinte.axioma.orm.Paises;
import com.servinte.axioma.orm.PaquetesOdontologicos;
import com.servinte.axioma.persistencia.UtilidadTransaccion;
import com.servinte.axioma.servicio.fabrica.ManejoPacienteServicioFabrica;
import com.servinte.axioma.servicio.fabrica.facturacion.FacturacionServicioFabrica;
import com.servinte.axioma.servicio.fabrica.odontologia.administracion.AdministracionFabricaServicio;
import com.servinte.axioma.servicio.fabrica.odontologia.facturacion.PaquetesOdontologicosFabricaServicio;
import com.servinte.axioma.servicio.fabrica.odontologia.presupuesto.PresupuestoFabricaServicio;
import com.servinte.axioma.servicio.interfaz.administracion.ICentroAtencionServicio;
import com.servinte.axioma.servicio.interfaz.administracion.IEspecialidadServicio;
import com.servinte.axioma.servicio.interfaz.administracion.IMedicosServicio;
import com.servinte.axioma.servicio.interfaz.manejoPaciente.IIngresosServicio;
import com.servinte.axioma.servicio.interfaz.odontologia.facturacion.IPaquetesOdontologicosServicio;
import com.servinte.axioma.servicio.interfaz.odontologia.presupuesto.IPresupuestoOdontologicoServicio;
import com.servinte.axioma.servicio.interfaz.tesoreria.ILocalizacionServicio;

/**
 * Esta clase se encarga de controlar los procesos de la funcionalidad
 * Reporte Ingresos Odontol&oacute;gicos en el m&oacute;dulo de odontolog&iacute;a
 *
 * @author Yennifer Guerrero
 * @since  25/08/2010
 *
 */
public class ReporteIngresosOdontologicosAction extends Action{
	
	/**
	 * M&eacute;todo execute de la clase.
	 */
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {	
		
		if (form instanceof ReporteIngresosOdontologicosForm) {
			
			@SuppressWarnings("unused")
			ActionForward actionForward=null;
			ReporteIngresosOdontologicosForm forma = (ReporteIngresosOdontologicosForm) form;
			
			InstitucionBasica ins = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
			
			UsuarioBasico usuario = Utilidades.getUsuarioBasicoSesion(request
					.getSession());
			
			String estado = forma.getEstado();
			Log4JManager.info("estado " + estado);

			try {
				UtilidadTransaccion.getTransaccion().begin();
				ActionForward forward=null;
				if (estado.equals("empezar")) {
					forward= empezar(forma, usuario, mapping, ins);
				}	
				if(estado.equals("cambiarPais")){
					listarCiudades(forma, usuario, mapping);
					forma.setTipoSalida(null);
					forma.setEnumTipoSalida(null);
					forma.setNombreArchivoGenerado(null);
					forward= mapping.findForward("principal");
				}
				if(estado.equals("cambiarCiudad")){
					listarCiudades(forma, usuario, mapping);
					listarCentrosAtencion(forma, usuario, mapping);
					forma.setTipoSalida(null);
					forma.setEnumTipoSalida(null);
					forma.setNombreArchivoGenerado(null);
					forward= mapping.findForward("principal");
				}
				if(estado.equals("cambiarRegion")){
					listarRegiones(forma, usuario, mapping);
					listarCentrosAtencion(forma, usuario, mapping);
					forma.setTipoSalida(null);
					forma.setEnumTipoSalida(null);
					forma.setNombreArchivoGenerado(null);
					forward= mapping.findForward("principal");
				}
				if(estado.equals("cambiarEmpresaInstitucion")){
					listarCentrosAtencion(forma, usuario, mapping);
					forma.setTipoSalida(null);
					forma.setEnumTipoSalida(null);
					forma.setNombreArchivoGenerado(null);
					forward= mapping.findForward("principal");
				}
				if(estado.equals("generarReporte")){
			
						String nombreUsuario = usuario.getNombreUsuario();
						forma.getFiltroIngresos().setNombreUsuario(nombreUsuario);
						
						generarReporteIngresos(forma, ins, request);
						forma.setEnumTipoSalida(null);
						forma.setTipoSalida(null);
						forma.setNombreArchivoGenerado(null);
						
						
						forward= mapping.findForward("principal");
				}
				if(estado.equals("mostrarPrimerFiltro")){
					forma.setMostrarSegundoFiltro(ConstantesBD.acronimoNo);
					forma.setMostrarTercerFiltro(ConstantesBD.acronimoNo);
					forma.resetSegundoFiltro();
					forma.getFiltroIngresos().setConPresupuesto(null);
					forward= mapping.findForward("principal");
				}
				
				if(estado.equals("mostrarSegundoFiltro")){
					forma.resetSegundoFiltro();
					listarProfesionales(forma, usuario, mapping, ins);
					forma.setMostrarSegundoFiltro(ConstantesBD.acronimoSi);
					forma.setMostrarTercerFiltro(ConstantesBD.acronimoNo);
					forma.resetTercerFiltro();
					forward= mapping.findForward("principal");
				}
				
				if (estado.equals("mostrarTercerFiltro")) {
					
					mostrarFiltroPresupuesto(forma, usuario, mapping);
					forma.setTipoSalida(null);
					forma.setEnumTipoSalida(null);
					forma.setNombreArchivoGenerado(null);
					forward= mapping.findForward("principal");
				}
				
				if (estado.equals("seleccionarPaquete")) {
					deshabilitaPrograma(forma);
					forma.setTipoSalida(null);
					forma.setEnumTipoSalida(null);
					forma.setNombreArchivoGenerado(null);
					
					cargarDescripcionPaquete(forma);
					
					forward= mapping.findForward("principal");
				}
				
				if (estado.equals("habilitaEdadFinal")) {
					
					if (forma.getFiltroIngresos().getEdadInicial() != null) {
						forma.setDeshabilitaEdadFinal(false);
					}else{
						forma.setDeshabilitaEdadFinal(true);
					}
					
					forma.setTipoSalida(null);
					forma.setEnumTipoSalida(null);
					forma.setNombreArchivoGenerado(null);
					
					forward= mapping.findForward("principal");
				}
				
				if (estado.equals("imprimirReporte")) {
					if (forma.getTipoSalida() != null && !forma.getTipoSalida().trim().equals("-1")) {
						imprimirReporteIngresos(forma);
						forma.setTipoSalida(null);
						forma.setEnumTipoSalida(null);
					}
					
					forward= mapping.findForward("principal");
				}
				
				if(estado.equals("inicializarFiltroEdadFinal")){
					forma.getFiltroIngresos().setEdadFinal(null);
					forward= mapping.findForward("principal");
				}
				
				if(estado.equals("deshabilitaPaquete")){
					deshabilitaPaquete(forma);
					forma.setTipoSalida(null);
					forma.setEnumTipoSalida(null);
					forma.setNombreArchivoGenerado(null);
					forward= mapping.findForward("principal");
				}
				UtilidadTransaccion.getTransaccion().commit();
				return forward;
			} catch (Exception e) {
				UtilidadTransaccion.getTransaccion().rollback();
				Log4JManager.error("Error generando el reporte de ingresos odontologicos", e);
			}
		}
		return ComunAction.accionSalirCasoError(mapping, request, null, null,
				"errors.estadoInvalido", "errors.estadoInvalido", true);

	}

	/**
	 * Este mètodo se encarga de 
	 *
	 * @param forma
	 *
	 * @autor Yennifer Guerrero
	 */
	private void cargarDescripcionPaquete(ReporteIngresosOdontologicosForm forma) {
		
		IPaquetesOdontologicosServicio servicio = PaquetesOdontologicosFabricaServicio.crearIPaquetesOdontologicosServicio();
		
		String codigoPaquete = null;
		String  descripcionPaquete = null; 
		int codigoEspecialidad = forma.getFiltroIngresos().getCodigoEspecialidad();
		
		ArrayList<PaquetesOdontologicos> listadoPaquetesOdonto = servicio
	 	.listarPaquetesOdontologicos(codigoPaquete, descripcionPaquete, codigoEspecialidad);
		
		if (forma.getFiltroIngresos().getCodigoPaqueteOdonto() > 0) {
			if (!Utilidades.isEmpty(listadoPaquetesOdonto)) {
				int paquete = forma.getFiltroIngresos().getCodigoPaqueteOdonto();
				
				for (PaquetesOdontologicos registro : listadoPaquetesOdonto) {
					if (paquete == registro.getCodigoPk()) {
						forma.getFiltroIngresos().setNombrePaquete(registro.getDescripcion());
						break;
					}
				}
			}
		}else{
			forma.getFiltroIngresos().setNombrePaquete("");
		}
		
		
	}
	/**
	 * Este mètodo se encarga de habilitar o deshabilitar el campo progrmas ya que es excluyente con
	 * el campo paquetes odontológicos.
	 *
	 * @param forma
	 *
	 * @autor Yennifer Guerrero
	 */
	private void deshabilitaPrograma(ReporteIngresosOdontologicosForm forma) {
		
		int codigoPaquete = forma.getFiltroIngresos().getCodigoPaqueteOdonto();
		
		if (!UtilidadTexto.isEmpty(codigoPaquete) && codigoPaquete>0 ) {
			forma.setDeshabilitaPrograma("true");
		}else {
			forma.setDeshabilitaPrograma("false");
		}
		
	}
	
	private void deshabilitaPaquete(ReporteIngresosOdontologicosForm forma) {
		
		long codigoPrograma = forma.getFiltroIngresos().getCodigoPrograma();
		
		if (codigoPrograma > 0) {
			forma.setDeshabilitaPaquete(true);
		}else {
			forma.setDeshabilitaPaquete(false);
		}
		
	}

	/**
	 * Este m&eacute;todo se encarga de inicializar los valores de los objetos de la
	 * p&aacute;gina para la b&uacute;squeda de los criterios seleccione
	 * con el fin de realizar el reporte de los ingresos odontol&oacute;gicos.
	 * @param forma
	 * @param usuario
	 * @param mapping
	 * @return
	 * 
	 * @throws Exception
	 * @author Yennifer Guerrero
	 * @param ins 
	 */
	public ActionForward empezar(ReporteIngresosOdontologicosForm forma,
			UsuarioBasico usuario, ActionMapping mapping, InstitucionBasica ins)
			throws Exception {
		
		forma.reset();	
		forma.resetSegundoFiltro();
		forma.resetTercerFiltro();
		forma.setMostrarSegundoFiltro(ConstantesBD.acronimoNo);
		forma.setMostrarTercerFiltro(ConstantesBD.acronimoNo);
		
		Connection con=HibernateUtil.obtenerConexion();		
		forma.setPath(Utilidades.obtenerPathFuncionalidad(con, 
				ConstantesBD.codigoFuncionalidadReporteIngresosOdontologicos));
		
		int codigoInstitucion = usuario.getCodigoInstitucionInt();
		String codigoPaisResidencia = ValoresPorDefecto.getPaisResidencia(codigoInstitucion);
		String[] codigosPais=codigoPaisResidencia.split("-");
		
		String razonSocial = ins.getRazonSocial();
		
		String fechaActual = UtilidadFecha.getFechaActual(con);
		forma.setFechaActual(fechaActual);
		
		forma.getFiltroIngresos().setRazonSocial(razonSocial);
		
		
		if(!codigoPaisResidencia.trim().equals("-")){
			forma.getFiltroIngresos().setCodigoPaisResidencia(codigosPais[0]);
		}
		
		
		ArrayList<Paises> listaPaises = AdministracionFabricaServicio.crearLocalizacionServicio().listarPaises();
		
		forma.setListaPaises(listaPaises);
		
		if (listaPaises.size() == 1) {
			forma.getFiltroIngresos().setCodigoPaisResidencia(listaPaises.get(0).getCodigoPais());
		}
		
		listarCiudades(forma, usuario, mapping);
		listarRegiones(forma, usuario, mapping);
		
		forma.setUtilizaServiciosOdonto(ValoresPorDefecto.getUtilizanProgramasOdontologicosEnInstitucion(codigoInstitucion));
		
		forma.setEsMultiempresa(ValoresPorDefecto.getInstitucionMultiempresa(codigoInstitucion));
		if (forma.getEsMultiempresa().equals(ConstantesBD.acronimoSi)) {
			forma.setListaEmpresaInstitucion(FacturacionServicioFabrica.crearEmpresasInstitucionServicio().listarEmpresaInstitucion());
		}
		
		listarCentrosAtencion(forma, usuario, mapping);
	
		listarSexoPaciente(forma, usuario, mapping);
		
		forma.getFiltroIngresos().setCiudadDeptoPais(new String());

		
		return mapping.findForward("principal");
	}
	
	/**
	 * 
	 * Este m&eacute;todo se encarga de generar el listado de las regiones de 
	 * cobertura. 
	 * @param forma
	 * @param usuario
	 * @param mapping
	 * 
	 * @author Yennifer Guerrero
	 */
	private void listarRegiones(ReporteIngresosOdontologicosForm forma,
			UsuarioBasico usuario, ActionMapping mapping) {
		
		Long codigoRegion= forma.getFiltroIngresos().getCodigoRegion();
		
		if (codigoRegion == 0 || codigoRegion == -1  ) {
			forma.setListaRegiones(AdministracionFabricaServicio.crearLocalizacionServicio()
					.listarRegionesCoberturaActivas());
			forma.setDeshabilitaCiudad(false);
			forma.setDeshabilitaRegion(false);
		} else {
			forma.setListaRegiones(AdministracionFabricaServicio.crearLocalizacionServicio()
					.listarRegionesCoberturaActivas());
			forma.setDeshabilitaCiudad(true);
		}
	}
	
	/**
	 * Este m&eacute;todo se encarga de listar las ciudades existentes en el sistema
	 * que pertenecen a un determinado pa&iacute;s.
	 * @param forma
	 * @param usuario
	 * @param mapping
	 * 
	 * @author Yennifer Guerrero
	 */
	private void listarCiudades(ReporteIngresosOdontologicosForm forma,
			UsuarioBasico usuario, ActionMapping mapping) {
		
		String ciudadDeptoPais= forma.getFiltroIngresos().getCiudadDeptoPais();
		
		if(UtilidadTexto.isEmpty(ciudadDeptoPais) || ciudadDeptoPais.trim().equals("-1")){
			
			forma.setListaCiudades(AdministracionFabricaServicio.crearLocalizacionServicio()
					.listarCiudadesPorPais(forma.getFiltroIngresos().getCodigoPaisResidencia()));
			forma.setDeshabilitaCiudad(false);
			forma.setDeshabilitaRegion(false);
		}else{
			forma.setListaCiudades(AdministracionFabricaServicio.crearLocalizacionServicio()
					.listarCiudadesPorPais(forma.getFiltroIngresos().getCodigoPaisResidencia()));
			forma.setDeshabilitaRegion(true);
		}
	}

	/**
	 * Este m&eacute;todo se encarga de listar los centros de atención del sistema
	 * de acuerdo a unos criterios en especifico.
	 * @param forma
	 * @param usuario
	 * @param mapping
	 * 
	 * @author Yennifer Guerrero
	 */
	private void listarCentrosAtencion(ReporteIngresosOdontologicosForm forma,
			UsuarioBasico usuario, ActionMapping mapping) {
		
		ILocalizacionServicio servicio = AdministracionFabricaServicio.crearLocalizacionServicio();
		long empresaInstitucion = forma.getFiltroIngresos().getCodigoEmpresaInstitucion();
		String ciudadDtoPais= forma.getFiltroIngresos().getCiudadDeptoPais();
		long codigoRegion = forma.getFiltroIngresos().getCodigoRegion();
		ArrayList<DtoCentrosAtencion> listaCentrosAtencion = null;
		
		if (codigoRegion <= 0 && empresaInstitucion <=0 &&
				(UtilidadTexto.isEmpty(ciudadDtoPais) || ciudadDtoPais.trim().equals("-1"))) {
			
			//lista todos los centros de atención del sistema
			 listaCentrosAtencion =  AdministracionFabricaServicio.crearLocalizacionServicio().listarTodosCentrosAtencion();
			
		}else if (!UtilidadTexto.isEmpty(ciudadDtoPais) && !ciudadDtoPais.trim().equals("-1")) {
			
			String vec[]=forma.getFiltroIngresos().getCiudadDeptoPais().split(ConstantesBD.separadorSplit);
			forma.getFiltroIngresos().setCodigoCiudad(vec[0]);
			forma.getFiltroIngresos().setCodigoDpto(vec[1]);
			forma.getFiltroIngresos().setCodigoPais(vec[2]);
			
			if (empresaInstitucion <= 0) {	
				//lista todos por ciudad
				listaCentrosAtencion = servicio.listarTodosPorCiudad(vec[0], vec[2], vec[1]);
			} else {
				//lista todos por empresa institucion y ciudad
				listaCentrosAtencion = servicio.listarTodosPorEmpresaInstitucionYCiudad(
						empresaInstitucion, forma.getFiltroIngresos().getCodigoCiudad(),
						forma.getFiltroIngresos().getCodigoPais(), 
						forma.getFiltroIngresos().getCodigoDpto());
			}
			
		}else if (codigoRegion > 0) {
			if (empresaInstitucion > 0) {
				//lista todos por region y empresa institucion
				listaCentrosAtencion = servicio.listarTodosPorEmpresaInstitucionYRegion(
						empresaInstitucion, codigoRegion);
				
			} else {
				//listar por region
				listaCentrosAtencion = servicio.listarTodosPorRegion(codigoRegion);
			}
			
		} else {
			//lista todos por institucion
			listaCentrosAtencion = servicio.listarTodosPorEmpresaInstitucion(empresaInstitucion);
		}
		
		if (listaCentrosAtencion != null && listaCentrosAtencion.size()>0) {
			
			
			if (listaCentrosAtencion.size()==1) {
				forma.getFiltroIngresos().setConsecutivoCentroAtencion(listaCentrosAtencion.get(0).getConsecutivo());
			}
			
			forma.setListaCentrosAtencion(listaCentrosAtencion);
		}else{
			forma.setListaCentrosAtencion(null);
		}
	}
	
	/**
	 * Este m&eacute;todo se encarga de listar los sexos de los pacientes.
	 * @param forma
	 * @param usuario
	 * @param mapping
	 * 
	 * @author Yennifer Guerrero
	 */
	private void listarSexoPaciente(ReporteIngresosOdontologicosForm forma,
			UsuarioBasico usuario, ActionMapping mapping) {
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
	 * Este m&eacute;todo se encarga de 
	 * @param forma
	 * @param valIni
	 *
	 * @author Yennifer Guerrero
	 * @param response 
	 */
	private void generarReporteIngresos(ReporteIngresosOdontologicosForm forma,InstitucionBasica ins,
			 HttpServletRequest request) {
		
		IIngresosServicio servicio = ManejoPacienteServicioFabrica.crearIngresosServicio();
		ICentroAtencionServicio servicioCentroAtencion = AdministracionFabricaServicio.crearCentroAtencionServicio();
		DtoReporteIngresosOdontologicos filtroIngresos = forma.getFiltroIngresos();
		
		/*
		 *Total de Ingresos 
		 */
		List<Integer> ingresosconsulta = servicio.consultarIngresosOdontoEstadoAbierto(forma.getFiltroIngresos());
		
		
		if (ingresosconsulta != null && ingresosconsulta.size()>0) {
			
			ArrayList<DtoCentrosAtencion> centrosAtencion = servicioCentroAtencion.obtenerCentrosAtencionIngresos(ingresosconsulta);
			
			forma.setListadoCentroAtencionIngreso(centrosAtencion);
			
			
			ArrayList<DtoResultadoConsultaIngresosOdonto> listadoIngresosOdonto = new ArrayList<DtoResultadoConsultaIngresosOdonto>();
			
			forma.setUbicacionLogo(ins.getUbicacionLogo());
			String rutaLogo = ins.getLogoJsp();
			
			forma.setRutaLogo(rutaLogo);
			
			listadoIngresosOdonto = servicio.obtenerConsolidadoCitasPorPaciente(filtroIngresos, ingresosconsulta, forma.getListadoCentroAtencionIngreso());
		
			
			
			
			if (listadoIngresosOdonto != null && listadoIngresosOdonto.size()>0) {
				
				servicio.obtenerConsolidadoReporte(listadoIngresosOdonto, ingresosconsulta, filtroIngresos);
				
				forma.setMostrarEncabezadoIngresos("true");
				forma.setListaResultadoConsultaIngresos(listadoIngresosOdonto);
				request.setAttribute("esPopUp", "true");
				
				
				if (forma.getListadoCentroAtencionIngreso().size() != forma.getListaResultadoConsultaIngresos().size()) {
					
					ArrayList<DtoCentrosAtencion> centrosAtencionResultado = new ArrayList<DtoCentrosAtencion>();
					
					for (DtoCentrosAtencion centros : forma.getListadoCentroAtencionIngreso()) {
						
						int consecutivo = centros.getConsecutivo();
						
						for (DtoResultadoConsultaIngresosOdonto dto : forma.getListaResultadoConsultaIngresos()) {
							
							if (consecutivo == dto.getConsecutivoCentroAtencion()) {
								centrosAtencionResultado.add(centros);
							}
						}
					}
					
					forma.setListadoCentroAtencionIngreso(centrosAtencionResultado);
				}
				
			}else{
				forma.setMostrarEncabezadoIngresos("false");
				forma.setListaResultadoConsultaIngresos(null);
				forma.setEstado("sinDatos");
				request.setAttribute("esPopUp", "false");
			}
			
		}else {
			forma.setListaResultadoConsultaIngresos(null);
			forma.setEstado("sinDatos");
			request.setAttribute("esPopUp", "false");
		}
	}

	/**
	 * Este m&eacute;todo se encarga de listar los m&eacute;dicos o profesionales
	 * de la salud que son de tipo odont&oacute;logo o auxiliar de odontolog&iacute;a
	 * @param forma
	 * @param usuario
	 * @param mapping
	 * 
	 * @author Yennifer Guerrero
	 * @param ins 
	 */
	private void listarProfesionales(ReporteIngresosOdontologicosForm forma,
			UsuarioBasico usuario, ActionMapping mapping, InstitucionBasica ins) {
		
		IMedicosServicio servicio = AdministracionFabricaServicio.crearMedicosServicio();
		int codigoInstitucion = usuario.getCodigoInstitucionInt();
		
		ArrayList<DtoPersonas> listaProfesionales = servicio.obtenerTodosMedicosOdonto(codigoInstitucion);
		
		if (listaProfesionales != null) {
			forma.setListaProfesionales(listaProfesionales);
			
		} else {
			forma.setListaProfesionales(null);
		}
	}
	
	/**
	 * Este m&eacute;todo se encarga de mostrar el tercer filtro
	 * para la generaci&oacute;n del reporte de ingresos odontol&oacute;gicos,
	 * el cual contiene filtros a cerca del presupuesto odontol&oacute;gico.
	 * @param forma
	 * @param usuario
	 * @param mapping
	 * 
	 * @author Yennifer Guerrero
	 * @param ins 
	 */
	private void mostrarFiltroPresupuesto(
			ReporteIngresosOdontologicosForm forma, UsuarioBasico usuario, ActionMapping mapping) {
		
		forma.getFiltroIngresos().setConSolicitudDcto("");
		listarEstadoPresupuesto(forma, usuario, mapping);
		listarIndicativoContratoPresupuesto(forma, usuario, mapping);
		listarEspecialidadesOdonto(forma, usuario, mapping);
		listarPaquetesOdontologicos(forma, usuario, mapping);
		
		int institucion = usuario.getCodigoInstitucionInt();
		String utilizaProgramasOdonto = ValoresPorDefecto.getUtilizanProgramasOdontologicosEnInstitucion(institucion);
		forma.setUtilizaProgramasOdonto(utilizaProgramasOdonto);
		forma.setMostrarTercerFiltro(ConstantesBD.acronimoSi);
	}

	/**
	 * Este m&eacute;todo se encarga de obtener el listado con los estado 
	 * del presupuesto.
	 * @param mapping 
	 * @param usuario 
	 * @param forma 
	 * 
	 * @author Yennifer Guerrero
	 * @param ins 
	 */
	private void listarEstadoPresupuesto(ReporteIngresosOdontologicosForm forma, UsuarioBasico usuario, 
			ActionMapping mapping) {
		
		IPresupuestoOdontologicoServicio servicio = PresupuestoFabricaServicio.crearPresupuestoOdontologicoServicio();
		int codigoInstitucion = usuario.getCodigoInstitucionInt();
		
		String validaPresupuestoContratado = ValoresPorDefecto
			.getValidaPresupuestoOdoContratado(codigoInstitucion);
		
		ArrayList<DtoIntegridadDominio> listaEstadosPresupuesto = servicio.listarEstadoPresupuestoOdonto(validaPresupuestoContratado);
		
		if (listaEstadosPresupuesto != null && listaEstadosPresupuesto.size() > 0) {
			forma.setListadoEstadosPresupuesto(listaEstadosPresupuesto);
			
			String estadoPresupuesto = forma.getFiltroIngresos().getEstadoPresupuesto();
			
			for (DtoIntegridadDominio registro : listaEstadosPresupuesto) {
				if (estadoPresupuesto.equals(registro.getAcronimo())) {
					forma.getFiltroIngresos().setNombreEstadoPresupuesto(registro.getDescripcion());
				}
			}
		} else {
			forma.setListadoEstadosPresupuesto(null);
		}
	}
	
	/**
	 * Este m&eacute;todo se encarga de obtener el listado con los indicativos 
	 * de contrato del presupuesto odontol&oacute;gico.
	 * @param mapping 
	 * @param usuario 
	 * @param forma 
	 * 
	 * @author Yennifer Guerrero
	 */
	private void listarIndicativoContratoPresupuesto(
			ReporteIngresosOdontologicosForm forma, UsuarioBasico usuario,
			ActionMapping mapping) {
		
		IPresupuestoOdontologicoServicio servicio = PresupuestoFabricaServicio.crearPresupuestoOdontologicoServicio();
		
		ArrayList<DtoIntegridadDominio> listaIndicativoContrato = servicio.listarIndicativoContrato();
		
		if (listaIndicativoContrato != null && listaIndicativoContrato.size() > 0) {
			forma.setListadoIndicativoContrato(listaIndicativoContrato);
			
			String indicativo = forma.getFiltroIngresos().getIndicativoContrato();
			
			for (DtoIntegridadDominio registro : listaIndicativoContrato) {
				if (indicativo.equals(registro.getAcronimo())) {
					forma.getFiltroIngresos().setNombreIndicativo(registro.getDescripcion());
				}
			}
			
		} else {
			forma.setListadoIndicativoContrato(null);
		}
	}
	
	/**
	 * Este m&eacute;todo se encarga de obtener el listado de las 
	 * especialidades odontol&oacute;gicas creadas en el sistema.
	 * @param forma
	 * @param usuario
	 * @param mapping
	 *
	 * @author Yennifer Guerrero
	 */
	private void listarEspecialidadesOdonto(
			ReporteIngresosOdontologicosForm forma, UsuarioBasico usuario,
			ActionMapping mapping) {
		
		IEspecialidadServicio servicio = AdministracionFabricaServicio.crearEspecialidadServicio();
		
		Especialidades especialidades = new Especialidades();
		especialidades.setTipoEspecialidad("ODON");
		
		List<Especialidades> listadoEspecialidadesOdonto= servicio.listarEspe(especialidades);
		
		if (listadoEspecialidadesOdonto != null && listadoEspecialidadesOdonto.size() > 0) {
			forma.setListadoEspecialidadesOdonto(listadoEspecialidadesOdonto);
			
			int codigoEspecialidad = forma.getFiltroIngresos().getCodigoEspecialidad();
			
			for (Especialidades registro : listadoEspecialidadesOdonto) {
				
				if (codigoEspecialidad == registro.getCodigo()) {
					forma.getFiltroIngresos().setNombreEspecialidad(registro.getNombre());
				}
			}
			
		} else {
			forma.setListadoEspecialidadesOdonto(null);
		}
		
	}
	
	/**
	 * Este m&eacute;todo se encarga de listar los paquetes odontol&oacute;gicos
	 * creados en el sistema.
	 * @param forma
	 * @param usuario
	 * @param mapping
	 *
	 * @author Yennifer Guerrero
	 */
	private void listarPaquetesOdontologicos(
			ReporteIngresosOdontologicosForm forma, UsuarioBasico usuario,
			ActionMapping mapping) {
		
		IPaquetesOdontologicosServicio servicio = PaquetesOdontologicosFabricaServicio.crearIPaquetesOdontologicosServicio();
		
		String codigoPaquete = null;
		String  descripcionPaquete = null; 
		int codigoEspecialidad = forma.getFiltroIngresos().getCodigoEspecialidad();
		
		ArrayList<PaquetesOdontologicos> listadoPaquetesOdonto = servicio
	 	.listarPaquetesOdontologicos(codigoPaquete, descripcionPaquete, codigoEspecialidad);
		
		if (listadoPaquetesOdonto != null && listadoPaquetesOdonto.size() > 0) {
			forma.setListadoPaquetesOdonto(listadoPaquetesOdonto);
		} else {
			forma.setListadoPaquetesOdonto(null);
		}
	}
	
	
	/**
	 * Este m&eacute;todo se encarga de enviar los datos necesarios a 
	 * los métodos que se encargan de la impresión del reporte.
	 * @param forma
	 * @return
	 *
	 * @author Yennifer Guerrero
	 * @param response 
	 */
	private void imprimirReporteIngresos(ReporteIngresosOdontologicosForm forma) {
		
		int tipoSalida 	= Integer.parseInt(forma.getTipoSalida());
		String nombreArchivo="";
		
		if (tipoSalida > 0) {
			
			ArrayList<DtoResultadoConsultaIngresosOdonto> listadoResultado = forma.getListaResultadoConsultaIngresos();
			DtoReporteIngresosOdontologicos filtroIngresos = forma.getFiltroIngresos();
			
			GeneradorReporteIngresoOdontologico generadorReporte = 
				new GeneradorReporteIngresoOdontologico(listadoResultado, filtroIngresos);
			
			GeneradorReporteIngresosOdontoArchivoPlano generadorReportePlano = 
				new GeneradorReporteIngresosOdontoArchivoPlano(listadoResultado, filtroIngresos);
			
			JasperPrint reporte = generadorReporte.generarReporte();
			JasperPrint reportePlano = generadorReportePlano.generarReporte();
			
			if (tipoSalida == EnumTiposSalida.PDF.getCodigo() ) {
				forma.setEnumTipoSalida(EnumTiposSalida.PDF);
				
			} else if (tipoSalida == EnumTiposSalida.HOJA_CALCULO.getCodigo() ) {
				forma.setEnumTipoSalida(EnumTiposSalida.HOJA_CALCULO);
				
			} else if (tipoSalida == EnumTiposSalida.PLANO.getCodigo() ) {
				forma.setEnumTipoSalida(EnumTiposSalida.PLANO);
			} 
			
			switch (forma.getEnumTipoSalida()) {
			
			case PDF:
				nombreArchivo = generadorReporte.exportarReportePDF(reporte, "ReporteIngresosOdontologicos");
				break;
				
			case PLANO:
				nombreArchivo = generadorReportePlano.exportarReporteTextoPlano(reportePlano, "ReporteIngresosOdontologicos");
				break;
				
			case HOJA_CALCULO:
				nombreArchivo = generadorReporte.exportarReporteExcel(reporte, "ReporteIngresosOdontologicos");
				break;
			}
			
			forma.setNombreArchivoGenerado(nombreArchivo);
			//JasperViewer.viewReport(reporte, false);
			
		}
	}
}
