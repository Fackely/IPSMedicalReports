package com.princetonsa.action.capitacion;

import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.util.MessageResources;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.capitacion.ConstantesCapitacion;

import com.princetonsa.actionform.capitacion.ParametrizarPresupuestoCapitacionForm;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.sort.odontologia.SortGenerico;
import com.servinte.axioma.dto.capitacion.DtoLogBusquedaParametrizacion;
import com.servinte.axioma.dto.capitacion.DtoLogParamPresupCap;
import com.servinte.axioma.dto.capitacion.DtoNivelesAtencionPresupuestoParametrizacionGeneral;
import com.servinte.axioma.dto.capitacion.DtoParametrizacionGeneral;
import com.servinte.axioma.mundo.fabrica.capitacion.CapitacionFabricaMundo;
import com.servinte.axioma.mundo.fabrica.facturacion.FacturacionFabricaMundo;
import com.servinte.axioma.mundo.interfaz.capitacion.IDetalleValorizacionArticuloMundo;
import com.servinte.axioma.mundo.interfaz.capitacion.IDetalleValorizacionServicioMundo;
import com.servinte.axioma.mundo.interfaz.capitacion.ILogDetalleParametrizacionPresupuestoMundo;
import com.servinte.axioma.mundo.interfaz.capitacion.ILogParametrizacionPresupuestoCapMundo;
import com.servinte.axioma.mundo.interfaz.capitacion.IParametrizacionPresupuestoCapitacionMundo;
import com.servinte.axioma.mundo.interfaz.capitacion.IValorizacionPresupuestoCapGeneralMundo;
import com.servinte.axioma.mundo.interfaz.facturacion.IContratoMundo;
import com.servinte.axioma.mundo.interfaz.facturacion.IConveniosMundo;
import com.servinte.axioma.orm.CentroAtencion;
import com.servinte.axioma.orm.ClaseInventario;
import com.servinte.axioma.orm.Contratos;
import com.servinte.axioma.orm.Convenios;
import com.servinte.axioma.orm.DetalleValorizacionArt;
import com.servinte.axioma.orm.DetalleValorizacionServ;
import com.servinte.axioma.orm.GruposServicios;
import com.servinte.axioma.orm.Instituciones;
import com.servinte.axioma.orm.LogDetalleParamPresup;
import com.servinte.axioma.orm.LogParamPresupuestoCap;
import com.servinte.axioma.orm.NivelAtencion;
import com.servinte.axioma.orm.ParamPresupuestosCap;
import com.servinte.axioma.orm.Usuarios;
import com.servinte.axioma.orm.ValorizacionPresCapGen;
import com.servinte.axioma.persistencia.UtilidadTransaccion;


/**
 * @author diecorqu
 * Clase usada para realizar la Parametrización del Presupuesto de Capitación.
 */
public class ParametrizarPresupuestoCapitacionAction extends Action 
{
	/** * Contiene la lista de mensajes correspondiente a esta forma */
	MessageResources fuenteMensaje = MessageResources.getMessageResources("com.servinte.mensajes.capitacion.ParametrizarPresupuestoCapitacionForm");
	
	/**
	 * Método encargado de el flujo y control de la funcionalidad
	 * 
	 * @see org.apache.struts.action.Action#execute(ActionMapping, ActionForm, 
	 * HttpServletRequest, HttpServletResponse)
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, 
								HttpServletResponse response )throws Exception {
		
		if(form instanceof ParametrizarPresupuestoCapitacionForm) {

			ParametrizarPresupuestoCapitacionForm forma = (ParametrizarPresupuestoCapitacionForm)form;

			String estado = forma.getEstado(); 

			Log4JManager.info("Estado: ParametrizarPresupuestoCapitacionAction --> "+estado);

			UsuarioBasico usuario 	= (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
			
			ActionErrors errores = new ActionErrors();

			if(estado.equals("empezar")) {
				forma.reset();
				forma.setTipoOperacion("crear");
				return mapping.findForward(this.mostrarBusquedaParametrizacion(forma, request, usuario, errores));
			} else if (estado.equals("recargarBusqueda")) {
				forma.resetConvenios();
				return mapping.findForward("parametrosbusqueda");
			} else if (estado.equals("recargarBusquedaModificacion")) {
				forma.resetValorizacionGeneral();
				return mapping.findForward("parametrosbusqueda");
			} else if (estado.equals("cargarListaContratos")) {
				forma.resetContratos();
				forma.resetFechaVigencia();
				return mapping.findForward(this.cargarListaContratos(forma, request, usuario, errores, "creacion"));
			} else if (estado.equals("cargarListaContratosLog")) {
				forma.resetContratos();
				forma.resetFechaVigencia();
				return mapping.findForward(this.cargarListaContratos(forma, request, usuario, errores, "log"));
			} else if (estado.equals("cargarFechasVigencia")) {
				forma.resetFechaVigencia();
				return mapping.findForward(this.cargarFechasVigencia(forma, request, usuario, forma.getTipoOperacion(), errores));
			} else if (estado.equals("mostrarParametrizacionGeneral")) {
				forma.resetValorizacionGeneral();
				return mapping.findForward(this.parametrizacionGeneral(forma, request, usuario));
			} else if (estado.equals("volverParametrizacionGeneral")) {
				forma.resetDetalles();
				return mapping.findForward("principal");
			} else if (estado.equals("calcularValorGastoMensual")) {
				double valorGasto = (forma.getContrato().getValor() * forma.getPorcentajeGastoMensual()) / 100D;
				BigDecimal valorGastoMensual = new BigDecimal(valorGasto);
				forma.setValorGastoMensual(valorGastoMensual.doubleValue());
				return mapping.findForward("cargarValorGastoMensual");
			} else if (estado.equals("guardarParametrizacionGeneral")) {
				return mapping.findForward(this.guardarParametrizacionGeneral(forma, request, usuario));
			} else if (estado.equals("detalleNivelAtencionGrupoServicio")) {
				forma.setSoloLectura(false);
				forma.setNivelAtencionHelper(forma.getNivelActual());
				return mapping.findForward("detalleNivelAtencionGrupoServicio");
			} else if (estado.equals("detalleNivelAtencionClaseInventario")) {
				forma.setSoloLectura(false);
				forma.setNivelAtencionHelper(forma.getNivelActual());
				return mapping.findForward("detalleNivelAtencionClaseInventario");
			} else if (estado.equals("guardarParametrizacionDetalladaGrupoServicio")) {
				return mapping.findForward(this.guardarParametrizacionDetalladaGrupoServicio(forma, request, usuario));
			} else if (estado.equals("guardarParametrizacionDetalladaClaseInventario")) {
				return mapping.findForward(this.guardarParametrizacionDetalladaClaseInventario(forma, request, usuario));
			} else if (estado.equals("modificar")) {
				forma.reset();
				forma.setTipoOperacion("modificar");
				return mapping.findForward(this.mostrarBusquedaParametrizacion(forma, request, usuario, errores));
			} else if (estado.equals("consultar")) {
				forma.reset();
				forma.setSoloLectura(true);
				forma.setTipoOperacion("modificar");
				return mapping.findForward(this.mostrarBusquedaParametrizacion(forma, request, usuario, errores));
			} else if (estado.equals("listadoPresupuestoCapitacion")) {
				forma.resetValorizacionGeneral();
				boolean tipoSeleccion = (ConstantesBD.acronimoSi.equals(request.getParameter("seleccionYear")));
				if (tipoSeleccion) {
					forma.setVigencia(request.getParameter("vigencia"));
				}
				forma.setSeleccionListaxYear(tipoSeleccion);
				return mapping.findForward(this.modificarPresupuestoCapitacion(forma, request, usuario));
			} else if (estado.equals("recargarlistadoPresupuestoCapitacion")) {
				forma.resetValorizacionGeneral();
				return mapping.findForward("listadoPresupuestoCapitacion");
			} else if (estado.equals("guardarModificacionParametrizacionGeneral")) {
				return mapping.findForward(this.guardarModificacionParametrizacionGeneral(forma, request, usuario));
			} else if (estado.equals("modificarDetalleNivelAtencionGrupoServicio")) {
				forma.resetDetalles();
				return mapping.findForward(modificarDetalleNivelAtencionGrupoServicio(forma, request, usuario, "modificacion"));
			} else if (estado.equals("modificarSoloLecturaDetalleNivelAtencionGrupoServicio")) {
				forma.resetDetalles();
				forma.setSoloLectura(true);
				return mapping.findForward(modificarDetalleNivelAtencionGrupoServicio(forma, request, usuario, "modificacion"));
			} else if (estado.equals("consultarDetalleNivelAtencionGrupoServicio")) {
				forma.resetDetalles();
				forma.setSoloLectura(true);
				return mapping.findForward(modificarDetalleNivelAtencionGrupoServicio(forma, request, usuario, "consulta"));
			} else if (estado.equals("modificarDetalleNivelAtencionClaseInventario")) {
				forma.resetDetalles();
				return mapping.findForward(modificarDetalleNivelAtencionClaseInventario(forma, request, usuario, "modificacion"));
			} else if (estado.equals("modificarSoloLecturaDetalleNivelAtencionClaseInventario")) {
				forma.resetDetalles();
				forma.setSoloLectura(true);
				return mapping.findForward(modificarDetalleNivelAtencionClaseInventario(forma, request, usuario, "modificacion"));
			} else if (estado.equals("consultarDetalleNivelAtencionClaseInventario")) {
				forma.resetDetalles();
				forma.setSoloLectura(true);
				return mapping.findForward(modificarDetalleNivelAtencionClaseInventario(forma, request, usuario, "consulta"));
			} else if (estado.equals("volverModificarParametrizacionGeneral")) {
				forma.setSoloLectura(false);
				return mapping.findForward("modificacionParametrizacionGeneral");
			} else if (estado.equals("mostrarMotivosModificacion")) {
				return mapping.findForward(this.mostrarMotivosModificacion(forma, request));
			}  else if (estado.equals("guardarModificarParametrizacionDetalladaGrupoServicio")) {
				forma.setEsModificacionDetalle(true);
				forma.setPermiteGuardar(false);
				return mapping.findForward(this.guardarParametrizacionDetalladaGrupoServicio(forma, request, usuario));
			} else if (estado.equals("guardarModificarParametrizacionDetalladaClaseInventario")) {
				forma.setPermiteGuardar(false);
				forma.setEsModificacionDetalle(true);
				return mapping.findForward(this.guardarParametrizacionDetalladaClaseInventario(forma, request, usuario));
			} else if (estado.equals("log")) {
				forma.reset();
				forma.setTipoOperacion("log");
				return mapping.findForward(this.mostrarBusquedaLog(forma, request, usuario, errores));
			} else if (estado.equals("listaResultadosBusquedaLog")) {
				return mapping.findForward(this.listaResultadosBusquedaLog(forma, request));
			} else if (estado.equals("mostrarLogDetalladoPresupuestoCapitacion")) {
				String codigoLog = request.getParameter("codigoLog");
				forma.setSeleccionLogxFecha(true);
				return mapping.findForward(this.mostrarLogDetalladoPresupuestoCapitacion(forma, request, Long.parseLong(codigoLog)));
			} else if (estado.equals("volverListaResultadosLog")) {
				return mapping.findForward("listaResultadosBusquedaLog");
			} else if (estado.equals("recargarBusquedaLog")) {
				forma.resetLog();
				return mapping.findForward(this.mostrarBusquedaLog(forma, request, usuario, errores));
			} else if (estado.equals("asignarPropiedad")) {
				return null;
			} else if(estado.equals("ordenarListaResultadosLog")) {
				return mapping.findForward(accionOrdenar(forma, "ListaResultados"));
			} else if(estado.equals("ordenarDetalleLog")) {
				return mapping.findForward(accionOrdenar(forma, "DetalleLog"));
			} else if(estado.equals("ordenarListaAniosContrato")) {
				return mapping.findForward(accionOrdenar(forma, "ListaAnios"));
			} else if(estado.equals("cargarInfoPorcentajeDetalle")) {
				PrintWriter out = response.getWriter();
				String codigoNivel = request.getParameter("codigoNivel");
				String mes = request.getParameter("mesValorizacion");
				String grupoClase = request.getParameter("grupoClase");
				String porcentajeActual = request.getParameter("porcentajeActual");
				String nuevoPorcentaje = request.getParameter("nuevoPorcentaje");
				out.print(this.obtenerInfoTooltipModificacionDetalle(forma, codigoNivel, mes, porcentajeActual, nuevoPorcentaje, grupoClase));
			} else if(estado.equals("cargarInfoPorcentajeGeneral")) {
				PrintWriter out = response.getWriter();
				String subSeccion = request.getParameter("subSeccion");
				String porcentajeActual = request.getParameter("porcentajeActual");
				String nuevoPorcentaje = request.getParameter("nuevoPorcentaje");
				out.print(this.obtenerInfoTooltipModificacionGeneral(forma, porcentajeActual, nuevoPorcentaje, subSeccion));
			} else {
				errores.add("Error", new ActionMessage("errors.procesoNoExitoso", "Funcionalidad no valida"));
				return mapping.findForward("paginaErrorPresupuestoCapitacion");
			}
		}
		return null;
	}
	
	/**
	 * Método encargado obtener los convenios que son capitados y manejan presupuesto 
	 * capitación para cargarlos en la forma
	 * 
	 * @param forma
	 * @param request
	 * @param usuario
	 * @return String 
	 */
	private String mostrarBusquedaParametrizacion(ParametrizarPresupuestoCapitacionForm forma, 
			HttpServletRequest request, UsuarioBasico usuario, ActionErrors errores) {
		try {
			UtilidadTransaccion.getTransaccion().begin();
			IConveniosMundo conveniosMundo = FacturacionFabricaMundo.crearcConveniosMundo();
			IParametrizacionPresupuestoCapitacionMundo parametrizacionMundo = 
				CapitacionFabricaMundo.crearParametrizacionPresupuestoCapitacionMundo();
			ArrayList<Convenios> listaConvenios = conveniosMundo.listarConveniosCapitadosActivosPorInstitucionManejaPresupuesto(usuario.getCodigoInstitucionInt());
			forma.setListaConvenios(listaConvenios);
			forma.setListaMotivosModificacion(parametrizacionMundo.listaMotivosModificacion());
			UtilidadTransaccion.getTransaccion().commit();
		} catch (Exception e) {
			Log4JManager.info("Mostrar Busqueda Parametrizacion", e);
			errores.add("Error", new ActionMessage("errors.procesoNoExitoso", "Funcionalidad no valida"));
		}
		return "parametrosbusqueda";
	}
	
	/**
	 * Método encargado obtener los convenios que son capitados y manejan presupuesto 
	 * capitación para cargarlos en la forma para la busqueda del log
	 * 
	 * @param forma
	 * @param request
	 * @param usuario
	 * @return String
	 */
	private String mostrarBusquedaLog(ParametrizarPresupuestoCapitacionForm forma, 
			HttpServletRequest request, UsuarioBasico usuario, ActionErrors errores) {
		try {
			UtilidadTransaccion.getTransaccion().begin();
			IConveniosMundo conveniosMundo = FacturacionFabricaMundo.crearcConveniosMundo();
			ArrayList<Convenios> listaConvenios = conveniosMundo.listarConveniosCapitadosActivosPorInstitucionManejaPresupuesto(usuario.getCodigoInstitucionInt());
			UtilidadTransaccion.getTransaccion().commit();
			forma.setListaConvenios(listaConvenios);
		} catch (Exception e) {
			Log4JManager.error("Listando convenios capitados", e);
			errores.add("Error", new ActionMessage("errors.procesoNoExitoso", "Funcionalidad no valida"));
		}
		return "parametrosBusquedaLog";
	}
	
	/**
	 * Método encargado obtener los contratos por convenio seleccionado 
	 * para cargarlos en la forma
	 * 
	 * @param forma
	 * @param request
	 * @param usuario
	 * @return String
	 */
	private String cargarListaContratos(ParametrizarPresupuestoCapitacionForm forma, 
			HttpServletRequest request, UsuarioBasico usuario, ActionErrors errores, String tipo) {
		int idConvenio = forma.getConvenioHelper();
		String retorno = "cargarListaContratos";
		try {
			UtilidadTransaccion.getTransaccion().begin();
			IContratoMundo contratoMundo = FacturacionFabricaMundo.crearContratoMundo();
			if (idConvenio != ConstantesBD.codigoNuncaValido) {
				ArrayList<Contratos> listaContratos = contratoMundo.listarTodosContratosVigentesPorConvenio(idConvenio);
				forma.setListaContratos(listaContratos);
			} 
			UtilidadTransaccion.getTransaccion().commit();
		} catch (Exception e) {
			UtilidadTransaccion.getTransaccion().rollback();
			Log4JManager.error("Error cargando lista contratos", e);
			errores.add("Error", new ActionMessage("errors.procesoNoExitoso", "Funcionalidad no valida"));
		}
		
		if ("log".equals(tipo)) {
			retorno = "cargarListaContratosLog";
		}
		
		return retorno; 
	}
	
	/**
	 * Método encargado obtener los años de vigencia para un contrato seleccionado 
	 * capitación para cargarlos en la forma
	 * 
	 * @param forma
	 * @param request
	 * @param usuario
	 * @param tipoOperacion
	 * @return String
	 */
	private String cargarFechasVigencia(ParametrizarPresupuestoCapitacionForm forma, 
			HttpServletRequest request, UsuarioBasico usuario, String tipoOperacion, ActionErrors errores) {
		String retorno = ("crear".equals(tipoOperacion)) ? "cargarFechasVigencia" : "cargarFechasVigenciaModificacion";
		try {
			IContratoMundo contratoMundo = FacturacionFabricaMundo.crearContratoMundo();
			IParametrizacionPresupuestoCapitacionMundo parametrizacionMundo = 
				CapitacionFabricaMundo.crearParametrizacionPresupuestoCapitacionMundo();
			int idContrato = forma.getContratoHelper();
			if (idContrato != ConstantesBD.codigoNuncaValido) {
				UtilidadTransaccion.getTransaccion().begin();
				Contratos contrato = contratoMundo.findById(idContrato);
				UtilidadTransaccion.getTransaccion().commit();
				forma.setFechasVigencia(parametrizacionMundo.obtenerFechasVigenciasContrato(contrato));
			}
		} catch (Exception e) {
			UtilidadTransaccion.getTransaccion().rollback();
			Log4JManager.error("Error cargando fechas vigencia", e);
			errores.add("Error", new ActionMessage("errors.procesoNoExitoso", "Funcionalidad no valida"));
		}
		return retorno;
	}
	
	/**
	 * Método encargado de cargar los datos de una parametrización general
	 * dados el convenio, contrato y fecha de vigencia del contrato
	 * 
	 * @param forma
	 * @param request
	 * @param usuario
	 * @return String
	 */
	private String parametrizacionGeneral(ParametrizarPresupuestoCapitacionForm forma,
			HttpServletRequest request, UsuarioBasico usuario) {
		
		ActionErrors errores = new ActionErrors();
		String retorno = "parametrosbusqueda";
		try {
			int yearVigencia = Integer.parseInt(forma.getVigencia());
			DtoParametrizacionGeneral dtoParametrizacionGeneral = new DtoParametrizacionGeneral();
			ParamPresupuestosCap parametrizacionGeneral = new ParamPresupuestosCap();
			if (yearVigencia != ConstantesBD.codigoNuncaValido) {
				IParametrizacionPresupuestoCapitacionMundo parametrizacionMundo = 
					CapitacionFabricaMundo.crearParametrizacionPresupuestoCapitacionMundo();
				parametrizacionGeneral.setContratos(forma.getContrato());
				parametrizacionGeneral.setAnioVigencia(forma.getVigencia());
				parametrizacionGeneral.setPorcentajeGastoGeneral(new BigDecimal(forma.getPorcentajeGastoMensual()));
				parametrizacionGeneral.setValorGastoGeneral(new BigDecimal(forma.getValorGastoMensual()));
				forma.setCodigoConvenioTemp(forma.getConvenioHelper());
				forma.setCodigoContratoTemp(forma.getContratoHelper());
				forma.setFechaVigenciaTemp(forma.getVigenciaHelper());
				UtilidadTransaccion.getTransaccion().begin();
				boolean existeParametrizacion = parametrizacionMundo.existeParametrizacionPresupuesto(
						forma.getContrato().getCodigo(), forma.getVigencia());
				if(!existeParametrizacion) {
					dtoParametrizacionGeneral = parametrizacionMundo.cargarDatosParametrizacionGeneral(parametrizacionGeneral, "creacion");
					forma.setMesesMatriz(dtoParametrizacionGeneral.getMesesMatriz());
					forma.setDtoNivelesAtencion(dtoParametrizacionGeneral.getListaDtoNivelesAtencion());
					forma.setNivelesAtencion(dtoParametrizacionGeneral.getListaNivelesContrato());
					forma.setExisteParametrizacion(false);
					forma.setParametrizacionPresupuesto(parametrizacionGeneral);
					retorno =  "principal";
				} else {
					forma.setExisteParametrizacion(true);
					errores.add("valorizacionGeneralExiste", 
							new ActionMessage("error.errorEnBlanco", fuenteMensaje.
									getMessage("ParametrizarPresupuestoCapitacionForm.existeParametrizacion")));
					saveErrors(request, errores);
				}
				UtilidadTransaccion.getTransaccion().commit();
			}
		} catch (Exception e) {
			Log4JManager.error("Cargando datos parametrizacion", e);
			errores.add("Error", new ActionMessage("errors.procesoNoExitoso", "Funcionalidad no valida"));
		}
		return retorno;
	}
	
	/**
	 * Método encargado de guardar la parametrización general ingrasada 
	 * 
	 * @param forma
	 * @param request
	 * @param usuarioBasico
	 * @return String
	 */
	private String guardarParametrizacionGeneral(ParametrizarPresupuestoCapitacionForm forma, 
			HttpServletRequest request, UsuarioBasico usuarioBasico) {

		boolean operacionExitosa = false;

		ActionErrors errores = new ActionErrors();

		try {
			UtilidadTransaccion.getTransaccion().begin();
			IParametrizacionPresupuestoCapitacionMundo parametrizacionMundo = 
				CapitacionFabricaMundo.crearParametrizacionPresupuestoCapitacionMundo();
			boolean existeParametrizacion = 
				parametrizacionMundo.existeParametrizacionPresupuesto(
						forma.getContrato().getCodigo(), forma.getVigencia());
			if (!existeParametrizacion) {
				ArrayList<ValorizacionPresCapGen> valorizacionGeneral = 
					new ArrayList<ValorizacionPresCapGen>();
				CentroAtencion centroAtencion = new CentroAtencion();
				centroAtencion.setConsecutivo(usuarioBasico.getCodigoCentroAtencion());
				Instituciones institucion = new Instituciones();
				institucion.setCodigo(usuarioBasico.getCodigoInstitucionInt());
				Usuarios usuario = new Usuarios();
				usuario.setLogin(usuarioBasico.getLoginUsuario());

				ParamPresupuestosCap parametrizacionPresupuesto = new ParamPresupuestosCap();
				parametrizacionPresupuesto.setCentroAtencion(centroAtencion);
				parametrizacionPresupuesto.setContratos(forma.getParametrizacionPresupuesto().getContratos());
				parametrizacionPresupuesto.setAnioVigencia(forma.getParametrizacionPresupuesto().getAnioVigencia());
				parametrizacionPresupuesto.setValorContrato(new BigDecimal(forma.getContrato().getValor()));
				parametrizacionPresupuesto.setPorcentajeGastoGeneral(new BigDecimal(forma.getPorcentajeGastoMensual()));
				parametrizacionPresupuesto.setValorGastoGeneral(new BigDecimal(forma.getValorGastoMensual()));
				parametrizacionPresupuesto.setInstituciones(institucion);
				parametrizacionPresupuesto.setUsuarios(usuario);
				parametrizacionPresupuesto.setFechaParam(Date.valueOf(Utilidades.capturarFechaBD()));
				parametrizacionPresupuesto.setHoraParam(Utilidades.capturarHoraBD());
				parametrizacionPresupuesto.setActivo(ConstantesBD.acronimoSiChar);

				if (parametrizacionMundo.guardarParametrizacionPresupuesto(parametrizacionPresupuesto)) {
					for (DtoNivelesAtencionPresupuestoParametrizacionGeneral dtoNivelesAtencion : forma.getDtoNivelesAtencion()) {
						NivelAtencion nivel = dtoNivelesAtencion.getNivelAtencion();
						int i = 0;
						for (String[] mesMatriz : forma.getMesesMatriz()) {
							if ("Activo".equals(mesMatriz[2])) {
								double valorGastoSubSeccion = 0D;
								if (dtoNivelesAtencion.isExistenServicios()) {
									String porcentajeServicioTemp = request.getParameter(mesMatriz[0] + "_servicio_"+nivel.getConsecutivo()+"_");
									BigDecimal porcentajeServicio = 
										(porcentajeServicioTemp != null && !"".equals(porcentajeServicioTemp)) ? 
												new BigDecimal(porcentajeServicioTemp) : new BigDecimal(0);
									valorGastoSubSeccion = (forma.getValorGastoMensual() * porcentajeServicio.doubleValue()) / 100D;
									ValorizacionPresCapGen valor = new ValorizacionPresCapGen();
									valor.setParamPresupuestosCap(parametrizacionPresupuesto);
									valor.setMes(i);
									valor.setNivelAtencion(nivel);
									valor.setSubSeccion(ConstantesCapitacion.subSeccionServicio);
									valor.setPorcentajeGastoSubSeccion(porcentajeServicio);
									valor.setValorGastoSubSeccion(new BigDecimal(valorGastoSubSeccion));
									valor.setActivo(ConstantesBD.acronimoSiChar);

									valorizacionGeneral.add(valor);
								}
								if (dtoNivelesAtencion.isExistenArticulos()) {
									String porcentajeArticuloTemp = request.getParameter(mesMatriz[0] + "_med_"+nivel.getConsecutivo()+"_");
									BigDecimal porcentajeArticulo = 
										(porcentajeArticuloTemp != null && !"".equals(porcentajeArticuloTemp)) ? 
												new BigDecimal(porcentajeArticuloTemp) : new BigDecimal(0);		
									valorGastoSubSeccion = (forma.getValorGastoMensual() * porcentajeArticulo.doubleValue()) / 100D;
									ValorizacionPresCapGen valor = new ValorizacionPresCapGen();
									valor.setParamPresupuestosCap(parametrizacionPresupuesto);
									valor.setMes(i);
									valor.setNivelAtencion(nivel);
									valor.setSubSeccion(ConstantesCapitacion.subSeccionArticulo);
									valor.setPorcentajeGastoSubSeccion(porcentajeArticulo);
									valor.setValorGastoSubSeccion(new BigDecimal(valorGastoSubSeccion));
									valor.setActivo(ConstantesBD.acronimoSiChar);

									valorizacionGeneral.add(valor);
								}
							}
							i++;
						}
					}
					IValorizacionPresupuestoCapGeneralMundo valorizacionGeneralMundo = 
						CapitacionFabricaMundo.crearValorizacionPresupuestoCapGeneralMundo();
					operacionExitosa = valorizacionGeneralMundo.guardarValorizacionPresupuestoCapitado(valorizacionGeneral);
				}
				if (operacionExitosa) {
					forma.setParametrizacionPresupuesto(parametrizacionPresupuesto);
					forma.setValorizacionGeneral(valorizacionGeneral);
					forma.setGuardoDatos(true);
					UtilidadTransaccion.getTransaccion().commit();
				} else {
					UtilidadTransaccion.getTransaccion().rollback();
					forma.setGuardoDatos(false);
					errores.add("ErrorGuardarValorizacion", 
							new ActionMessage("errors.required", fuenteMensaje.
									getMessage("ParametrizarPresupuestoCapitacionForm.mensaje.errorguardado")));
					saveErrors(request, errores);
				}
			} else {
				forma.setExisteParametrizacion(true);
				errores.add("valorizacionGeneralExiste", 
						new ActionMessage("error.errorEnBlanco", fuenteMensaje.
								getMessage("ParametrizarPresupuestoCapitacionForm.existeParametrizacion")));
				saveErrors(request, errores);
				UtilidadTransaccion.getTransaccion().commit();
			}
		} catch (Exception e) {
			UtilidadTransaccion.getTransaccion().rollback();
			Log4JManager.error("Guardando parametrización general", e);
			errores.add("Error", new ActionMessage("errors.procesoNoExitoso", "Funcionalidad no valida"));
		}
		return "principal";
	}

	/**
	 * Método encargado de guardad la valorización de la parametrización ingresada para un 
	 * grupo de servicio del nivel seleccionado
	 * 
	 * @param forma
	 * @param request
	 * @param usuario
	 * @return String
	 */
	private String guardarParametrizacionDetalladaGrupoServicio(ParametrizarPresupuestoCapitacionForm forma, HttpServletRequest request, UsuarioBasico usuario) {
		ActionErrors errores = new ActionErrors();
		String retorno = "detalleNivelAtencionGrupoServicio";
		try {
			boolean operacionExitosa = false;
			boolean existeValoracionDetallada = false;
			boolean datosValidos = this.validarTotalesGrupoServicio(forma, request, "totalGrupoServicio_mes_");
			IDetalleValorizacionServicioMundo detalleValorizacionServicioMundo = 
				CapitacionFabricaMundo.crearDetalleValorizacionServicioMundo();
			if(datosValidos) {
				double valorGastoDetalle = 0D;
				ArrayList<DetalleValorizacionServ> valorizacionDetalleServicio = new ArrayList<DetalleValorizacionServ>();
				
				for (DtoNivelesAtencionPresupuestoParametrizacionGeneral dtoNivelesAtencion : forma.getDtoNivelesAtencion()) {
					NivelAtencion nivel = dtoNivelesAtencion.getNivelAtencion();
					if(nivel.getConsecutivo() == forma.getNivelActual().longValue()) {
						UtilidadTransaccion.getTransaccion().begin();
						existeValoracionDetallada = detalleValorizacionServicioMundo.
						existeValorizacionDetalleGrupoServicio(forma.getParametrizacionPresupuesto().getCodigo(),
								nivel.getConsecutivo());
						UtilidadTransaccion.getTransaccion().commit();
						if (!existeValoracionDetallada) {
							IValorizacionPresupuestoCapGeneralMundo valorizacionGeneralMundo = 
								CapitacionFabricaMundo.crearValorizacionPresupuestoCapGeneralMundo();
							for (GruposServicios grupo : dtoNivelesAtencion.getListaGruposServicios()) {
								int i = 0;
								for (String[] mesMatriz : forma.getMesesMatriz()) {
									if ("Activo".equals(mesMatriz[2])) {
										UtilidadTransaccion.getTransaccion().begin();
										ValorizacionPresCapGen valorizacionGeneralMes = valorizacionGeneralMundo.
												obtenerValorizacionGeneralxNivelAtencionSubSeccionMes(
														forma.getParametrizacionPresupuesto().getCodigo(), nivel.getConsecutivo(), 
														i, ConstantesCapitacion.subSeccionServicio);
										UtilidadTransaccion.getTransaccion().commit();
										String porcentajeValorTemp = request.getParameter(mesMatriz[0] + "_grupo_servicio_"+grupo.getCodigo()+"_");
										BigDecimal porcentajeServicio = 
											(porcentajeValorTemp != null && !"".equals(porcentajeValorTemp)) ? 
													new BigDecimal(porcentajeValorTemp) : new BigDecimal(0);
										valorGastoDetalle = (valorizacionGeneralMes != null) ? (valorizacionGeneralMes.getValorGastoSubSeccion().doubleValue() * 
															 porcentajeServicio.doubleValue()) / 100D : 0D;
										DetalleValorizacionServ detalle = new DetalleValorizacionServ();
										detalle.setParamPresupuestosCap(forma.getParametrizacionPresupuesto());
										detalle.setMes(i);
										detalle.setNivelAtencion(nivel);
										detalle.setGruposServicios(grupo);
										detalle.setPorcentajeGasto(porcentajeServicio);
										detalle.setValorGasto(new BigDecimal(valorGastoDetalle));
										detalle.setActivo(ConstantesBD.acronimoSiChar);
										valorizacionDetalleServicio.add(detalle);
										
										if (forma.isEsModificacionDetalle()) {
											LogDetalleParamPresup logDetalle = new LogDetalleParamPresup();
											logDetalle.setDescipcion(detalle.getGruposServicios().getDescripcion());
											logDetalle.setMes(obtenerNombreMes(detalle.getMes()));
											logDetalle.setNivelAtencion(detalle.getNivelAtencion().getDescripcion());
											logDetalle.setNivelModificacion(ConstantesCapitacion.grupoServicios);
											logDetalle.setPorcentajeActual(porcentajeServicio);
											logDetalle.setPorcentajeAnterior(new BigDecimal(-1));
											forma.getDtoLogParametrizacionPresupuesto().getListaLogDetalleParametrizacion().add(logDetalle);
										}
									}
									i++;
								}
							}

							if (!forma.isEsModificacionDetalle()) {
								UtilidadTransaccion.getTransaccion().begin();
								operacionExitosa = detalleValorizacionServicioMundo.guardarValorizacionDetalleServicio(valorizacionDetalleServicio);
								if (operacionExitosa) {
									forma.setValorizacionDetalleGrupoServicio(valorizacionDetalleServicio);
									dtoNivelesAtencion.setGuardoNivelAtencionGrupoServicio(true);
									forma.setGuardoDatosDetalleGrupoServicio(true);
									forma.setSoloLectura(true);
									UtilidadTransaccion.getTransaccion().commit();
								} else {
									UtilidadTransaccion.getTransaccion().rollback();
									forma.setGuardoDatosDetalleGrupoServicio(false);
									errores.add("ErrorGuardarValorizacion", 
											new ActionMessage("errors.required", fuenteMensaje.
													getMessage("ParametrizarPresupuestoCapitacionForm.mensaje.errorguardado")));
									saveErrors(request, errores);
								}
							} else {
								dtoNivelesAtencion.setDetalleValorizacionServicios(valorizacionDetalleServicio);
							}

						} else {
							retorno = "modificacionParametrizacionGeneral";
							HashMap<Long, Double> valoresActuales = new HashMap<Long, Double>();
							double porcentajeActual = 0D; 
							double nuevoPorcentaje = 0D;
							String valorTmp = "";

							UtilidadTransaccion.getTransaccion().begin();
							ArrayList<DetalleValorizacionServ> listaDetallesValServOriginal = 
								detalleValorizacionServicioMundo.detalleValorizacionServicio(
										forma.getParametrizacionPresupuesto().getCodigo(), forma.getNivelActual());
							UtilidadTransaccion.getTransaccion().commit();

							for (DetalleValorizacionServ valorizacion : listaDetallesValServOriginal) {
								valoresActuales.put(valorizacion.getCodigo(), valorizacion.getPorcentajeGasto().doubleValue());
							}
							dtoNivelesAtencion.setDetalleValorizacionServicios(new ArrayList<DetalleValorizacionServ>());

							for (DetalleValorizacionServ detallesValorizacionServicio : forma.getValorizacionDetalleGrupoServicio()) {
								valorTmp = request.getParameter("mes_"+detallesValorizacionServicio.getMes()+ "_grupo_servicio_" + 
										detallesValorizacionServicio.getGruposServicios().getCodigo() + "_valor_" +
										detallesValorizacionServicio.getCodigo() );
								nuevoPorcentaje = ( valorTmp != null && !"".equals(valorTmp)) ? Double.parseDouble(valorTmp): 0D;
								porcentajeActual = valoresActuales.get(detallesValorizacionServicio.getCodigo());
								detallesValorizacionServicio.setPorcentajeGasto(new BigDecimal(nuevoPorcentaje));
								dtoNivelesAtencion.getDetalleValorizacionServicios().add(detallesValorizacionServicio);
								if (porcentajeActual != nuevoPorcentaje) {
									LogDetalleParamPresup logDetalle = new LogDetalleParamPresup();
									logDetalle.setDescipcion(detallesValorizacionServicio.getGruposServicios().getDescripcion());
									logDetalle.setMes(obtenerNombreMes(detallesValorizacionServicio.getMes()));
									logDetalle.setNivelAtencion(detallesValorizacionServicio.getNivelAtencion().getDescripcion());
									logDetalle.setNivelModificacion(ConstantesCapitacion.grupoServicios);
									logDetalle.setPorcentajeActual(new BigDecimal(nuevoPorcentaje));
									logDetalle.setPorcentajeAnterior(new BigDecimal(porcentajeActual));

									forma.getDtoLogParametrizacionPresupuesto().getListaLogDetalleParametrizacion().add(logDetalle);
								}
							}
						}	
					}
				}

				if (!existeValoracionDetallada && forma.isEsModificacionDetalle()) {
					retorno = "modificacionParametrizacionGeneral";
				}

			} else {
				boolean sinValorizacionDetalle = false;
				if (forma.isEsModificacionDetalle()) {
					double total = 0D;
					for (Double totalMes : forma.getTotalesDetalleGrupoServicio()) {
						total += totalMes; 
					}
					sinValorizacionDetalle = (total == 0D);
					if (!sinValorizacionDetalle) {
						UtilidadTransaccion.getTransaccion().begin();
						existeValoracionDetallada = detalleValorizacionServicioMundo.
						existeValorizacionDetalleGrupoServicio(
								forma.getParametrizacionPresupuesto().getCodigo(),
								forma.getNivelActual());
						UtilidadTransaccion.getTransaccion().commit();
						if (existeValoracionDetallada) {
							this.cargarDatosGrupoServicio(forma, request, "modificar");
						} else {
							this.cargarDatosGrupoServicio(forma, request, "crear");
						}

						retorno = "modificacionDetalleGrupoServicio";
						errores.add("TotalesRequeridos", 
								new ActionMessage("errors.required",  
										fuenteMensaje.getMessage("ParametrizarPresupuestoCapitacionForm.mensaje.totalInvalido")));
						saveErrors(request, errores);
					} else {
						retorno = "modificacionParametrizacionGeneral";
					}	
				} else {
					this.cargarDatosGrupoServicio(forma, request, "crear");
					errores.add("TotalesRequeridos", 
							new ActionMessage("errors.required",  
									fuenteMensaje.getMessage("ParametrizarPresupuestoCapitacionForm.mensaje.totalInvalido")));
					saveErrors(request, errores);
				}

			}
		} catch (Exception e) {
			UtilidadTransaccion.getTransaccion().rollback();
			Log4JManager.error("Guardar Parametrizacion Detallada Grupo Servicio", e);
			errores.add("Error", new ActionMessage("errors.procesoNoExitoso", "Funcionalidad no valida"));
			saveErrors(request, errores);
		}
		return retorno;
	}
	
	/**
	 * Método encargado de guardad la valorización de la parametrización ingresada para una
	 * clase de inventario del nivel seleccionado
	 * @param forma
	 * @param request
	 * @param usuario
	 * @return String
	 */
	private String guardarParametrizacionDetalladaClaseInventario(ParametrizarPresupuestoCapitacionForm forma, 
			HttpServletRequest request, UsuarioBasico usuario) {

		ActionErrors errores = new ActionErrors();
		String retorno = "detalleNivelAtencionClaseInventario";
		boolean existeValoracionDetallada = false;
		boolean operacionExitosa = false;
		try {
			boolean datosValidos = this.validarTotalesClaseInventario(forma, request, "totalClaseInventario_mes_");
			IDetalleValorizacionArticuloMundo detalleValorizacionArticuloMundo = 
				CapitacionFabricaMundo.crearDetalleValorizacionArticuloMundo();
			if(datosValidos) {
				double valorGastoDetalle = 0D;
				ArrayList<DetalleValorizacionArt> valorizacionDetalleClaseInventario = new ArrayList<DetalleValorizacionArt>();

				for (DtoNivelesAtencionPresupuestoParametrizacionGeneral dtoNivelesAtencion : forma.getDtoNivelesAtencion()) {
					NivelAtencion nivel = dtoNivelesAtencion.getNivelAtencion();
					if(nivel.getConsecutivo() == forma.getNivelActual().longValue()) {
						UtilidadTransaccion.getTransaccion().begin();
						existeValoracionDetallada = detalleValorizacionArticuloMundo.
						existeValorizacionDetalleClaseInventario(forma.getParametrizacionPresupuesto().getCodigo(),
								nivel.getConsecutivo());
						UtilidadTransaccion.getTransaccion().commit();
						if (!existeValoracionDetallada) {
							IValorizacionPresupuestoCapGeneralMundo valorizacionGeneralMundo = 
								CapitacionFabricaMundo.crearValorizacionPresupuestoCapGeneralMundo();
							for (ClaseInventario clase : dtoNivelesAtencion.getListaClasesInventario()) {
								int i = 0;
								for (String[] mesMatriz : forma.getMesesMatriz()) {
									if ("Activo".equals(mesMatriz[2])) {
										UtilidadTransaccion.getTransaccion().begin();
										ValorizacionPresCapGen valorizacionGeneralMes = valorizacionGeneralMundo.
										obtenerValorizacionGeneralxNivelAtencionSubSeccionMes(
												forma.getParametrizacionPresupuesto().getCodigo(), nivel.getConsecutivo(), 
												i, ConstantesCapitacion.subSeccionArticulo);
										UtilidadTransaccion.getTransaccion().commit();
										String porcentajeValorTemp = request.getParameter(mesMatriz[0] + "_clase_inventario_"+clase.getCodigo()+"_");
										BigDecimal porcentajeServicio = 
											(porcentajeValorTemp != null && !"".equals(porcentajeValorTemp)) ? 
													new BigDecimal(porcentajeValorTemp) : new BigDecimal(0);
													
										valorGastoDetalle = (valorizacionGeneralMes != null) ?(valorizacionGeneralMes.getValorGastoSubSeccion().doubleValue() * 
												porcentajeServicio.doubleValue()) / 100D : 0D;
										DetalleValorizacionArt detalle = new DetalleValorizacionArt();
										detalle.setParamPresupuestosCap(forma.getParametrizacionPresupuesto());
										detalle.setMes(i);
										detalle.setNivelAtencion(nivel);
										detalle.setClaseInventario(clase);
										detalle.setPorcentajeGasto(porcentajeServicio);
										detalle.setValorGasto(new BigDecimal(valorGastoDetalle));
										detalle.setActivo(ConstantesBD.acronimoSiChar);
										valorizacionDetalleClaseInventario.add(detalle);
										
										if (forma.isEsModificacionDetalle()) {
											LogDetalleParamPresup logDetalle = new LogDetalleParamPresup();
											logDetalle.setDescipcion(detalle.getClaseInventario().getNombre());
											logDetalle.setMes(obtenerNombreMes(detalle.getMes()));
											logDetalle.setNivelAtencion(detalle.getNivelAtencion().getDescripcion());
											logDetalle.setNivelModificacion(ConstantesCapitacion.claseInventario);
											logDetalle.setPorcentajeActual(porcentajeServicio);
											logDetalle.setPorcentajeAnterior(new BigDecimal(-1));
											forma.getDtoLogParametrizacionPresupuesto().getListaLogDetalleParametrizacion().add(logDetalle);
										}
									}
									i++;
								}
							}

							if (!forma.isEsModificacionDetalle()) {
								UtilidadTransaccion.getTransaccion().begin();
								operacionExitosa = detalleValorizacionArticuloMundo.guardarValorizacionDetalleArticulo(valorizacionDetalleClaseInventario);
								if (operacionExitosa) {
									forma.setValorizacionDetalleClaseInventario(valorizacionDetalleClaseInventario);
									dtoNivelesAtencion.setGuardoNivelAtencionClaseInventario(true);
									forma.setGuardoDatosDetalleClaseInventario(true);
									forma.setSoloLectura(true);
									UtilidadTransaccion.getTransaccion().commit();
								} else {
									UtilidadTransaccion.getTransaccion().rollback();
									forma.setGuardoDatosDetalleClaseInventario(false);
									errores.add("ErrorGuardarValorizacion", 
											new ActionMessage("errors.required", fuenteMensaje.
													getMessage("ParametrizarPresupuestoCapitacionForm.mensaje.errorguardado")));
									saveErrors(request, errores);
								}
							} else {
								dtoNivelesAtencion.setDetalleValorizacionArticulos(valorizacionDetalleClaseInventario);
							}

						} else {
							retorno = "modificacionParametrizacionGeneral";
							HashMap<Long, Double> valoresActuales = new HashMap<Long, Double>();
							double porcentajeActual = 0D; 
							double nuevoPorcentaje = 0D;
							String valorTmp = "";

							UtilidadTransaccion.getTransaccion().begin();
							ArrayList<DetalleValorizacionArt> listaDetallesValArtOriginal = 
								detalleValorizacionArticuloMundo.detalleValorizacionArticulo(
										forma.getParametrizacionPresupuesto().getCodigo(), forma.getNivelActual());
							UtilidadTransaccion.getTransaccion().commit();

							for (DetalleValorizacionArt valorizacion : listaDetallesValArtOriginal) {
								valoresActuales.put(valorizacion.getCodigo(), valorizacion.getPorcentajeGasto().doubleValue());
							}
							dtoNivelesAtencion.setDetalleValorizacionArticulos(new ArrayList<DetalleValorizacionArt>());
							for (DetalleValorizacionArt detallesValorizacionArt : forma.getValorizacionDetalleClaseInventario()) {
								valorTmp = request.getParameter("mes_"+detallesValorizacionArt.getMes()+ "_clase_inventario_" + 
										detallesValorizacionArt.getClaseInventario().getCodigo() + "_valor_" +
										detallesValorizacionArt.getCodigo() );
								nuevoPorcentaje = ( valorTmp != null && !"".equals(valorTmp)) ? Double.parseDouble(valorTmp): 0D;
								porcentajeActual = valoresActuales.get(detallesValorizacionArt.getCodigo());
								detallesValorizacionArt.setPorcentajeGasto(new BigDecimal(nuevoPorcentaje));
								dtoNivelesAtencion.getDetalleValorizacionArticulos().add(detallesValorizacionArt);

								if (porcentajeActual != nuevoPorcentaje) {
									LogDetalleParamPresup logDetalle = new LogDetalleParamPresup();
									logDetalle.setDescipcion(detallesValorizacionArt.getClaseInventario().getNombre());
									logDetalle.setMes(obtenerNombreMes(detallesValorizacionArt.getMes()));
									logDetalle.setNivelAtencion(detallesValorizacionArt.getNivelAtencion().getDescripcion());
									logDetalle.setNivelModificacion(ConstantesCapitacion.claseInventario);
									logDetalle.setPorcentajeActual(new BigDecimal(nuevoPorcentaje));
									logDetalle.setPorcentajeAnterior(new BigDecimal(porcentajeActual));

									forma.getDtoLogParametrizacionPresupuesto().getListaLogDetalleParametrizacion().add(logDetalle);
								} 
							}
						}	
					}
				}

				if (!existeValoracionDetallada && forma.isEsModificacionDetalle()) {
					retorno = "modificacionParametrizacionGeneral";
				}

			} else {
				boolean sinValorizacionDetalle = false;
				if (forma.isEsModificacionDetalle()) {
					double total = 0D;
					for (Double totalMes : forma.getTotalesDetalleClaseInventario()) {
						total += totalMes; 
					}
					sinValorizacionDetalle = (total == 0D);
					if (!sinValorizacionDetalle) {
						UtilidadTransaccion.getTransaccion().begin();
						existeValoracionDetallada = detalleValorizacionArticuloMundo.
						existeValorizacionDetalleClaseInventario(
								forma.getParametrizacionPresupuesto().getCodigo(),
								forma.getNivelActual());
						UtilidadTransaccion.getTransaccion().commit();
						if (existeValoracionDetallada) {
							this.cargarDatosClaseInventario(forma, request, "modificar");
						} else {
							this.cargarDatosClaseInventario(forma, request, "crear");
						}

						retorno = "modificacionDetalleClaseInventario";
						errores.add("TotalesRequeridos", 
								new ActionMessage("errors.required",  
										fuenteMensaje.getMessage("ParametrizarPresupuestoCapitacionForm.mensaje.totalInvalido")));
						saveErrors(request, errores);
					} else {
						retorno = "modificacionParametrizacionGeneral";
					}	
				} else {
					this.cargarDatosClaseInventario(forma, request, "crear");
					errores.add("TotalesRequeridos", 
							new ActionMessage("errors.required",  
									fuenteMensaje.getMessage("ParametrizarPresupuestoCapitacionForm.mensaje.totalInvalido")));
					saveErrors(request, errores);
				}

			}
		} catch (Exception e) {
			UtilidadTransaccion.getTransaccion().rollback();
			Log4JManager.error("Guardar Parametrizacion Detallada Clase Inventario", e);
			errores.add("Error", new ActionMessage("errors.procesoNoExitoso", "Funcionalidad no valida"));
			saveErrors(request, errores);
		}
		return retorno;
	}
	
		
	/**
	 * Método encargado de obtener la información de la parametrización del presupuesto de capitación 
	 * y su respectiva valorización para cargar estos datos en la forma
	 * 
	 * @param forma
	 * @param request
	 * @param usuarioBasico
	 * @return String
	 */
	private String modificarPresupuestoCapitacion(ParametrizarPresupuestoCapitacionForm forma, 
			HttpServletRequest request, UsuarioBasico usuarioBasico) {

		ActionErrors errores = new ActionErrors();
		String retorno = "modificacionParametrizacionGeneral";
		ParamPresupuestosCap parametrizacionModificacion = new ParamPresupuestosCap();
		
		try {
			if (forma.getConvenio() != null && 
					forma.getConvenio().getCodigo() != ConstantesBD.codigoNuncaValido) {
				if (forma.getContrato() != null && 
						forma.getContrato().getCodigo() != ConstantesBD.codigoNuncaValido) {

					IParametrizacionPresupuestoCapitacionMundo parametrizacionMundo = 
						CapitacionFabricaMundo.crearParametrizacionPresupuestoCapitacionMundo();
					Usuarios usuario = new Usuarios();
					usuario.setLogin(usuarioBasico.getLoginUsuario());
					parametrizacionModificacion.setContratos(forma.getContrato());
					parametrizacionModificacion.setAnioVigencia(forma.getVigencia());
					parametrizacionModificacion.setUsuarios(usuario);

					UtilidadTransaccion.getTransaccion().begin();

					DtoParametrizacionGeneral dtoParametrizacion = 
						parametrizacionMundo.cargarValorizacionParametrizacionGeneral(parametrizacionModificacion);

					if (dtoParametrizacion.getListaParametrizaciones() != null && 
							!dtoParametrizacion.getListaParametrizaciones().isEmpty()) {
						forma.setListadoParametrizaciones(dtoParametrizacion.getListaParametrizaciones());
						retorno = "listadoPresupuestoCapitacion";
					} else {
						if (dtoParametrizacion.getValorizacionPresupuestoGeneral() != null && 
								!dtoParametrizacion.getValorizacionPresupuestoGeneral().isEmpty()) {
							forma.setParametrizacionPresupuesto(dtoParametrizacion.getParametrizacionPresupuesto());
							forma.setPorcentajeGastoMensual(dtoParametrizacion.getParametrizacionPresupuesto().getPorcentajeGastoGeneral().doubleValue());
							forma.setValorGastoMensual(dtoParametrizacion.getParametrizacionPresupuesto().getValorGastoGeneral().doubleValue());
							forma.setVigencia(dtoParametrizacion.getParametrizacionPresupuesto().getAnioVigencia());
							forma.setValorizacionGeneral(dtoParametrizacion.getValorizacionPresupuestoGeneral());
							forma.setMesesMatriz(dtoParametrizacion.getMesesMatriz());
							forma.setDtoNivelesAtencion(dtoParametrizacion.getListaDtoNivelesAtencion());
							forma.setNivelesAtencion(dtoParametrizacion.getListaNivelesContrato());
							forma.setNivelesAtencionParametrizacion(dtoParametrizacion.getListaNivelesParametrizacion());
							forma.setExisteParametrizacion(false);
							forma.setSoloLectura(dtoParametrizacion.isSoloLectura());
							
							if (dtoParametrizacion.getParametrizacionPresupuesto().getValorContrato().doubleValue() != 
										forma.getContrato().getValor()) {
								forma.setEsModificacionValorContrato(true);
							}
						} else {
							errores.add("valorizacionGeneralNoExiste", 
									new ActionMessage("error.errorEnBlanco", fuenteMensaje.
											getMessage("ParametrizarPresupuestoCapitacionForm.noExistenRegistros")));
							retorno = "parametrosbusqueda";
						}
					}

					UtilidadTransaccion.getTransaccion().commit();

				} else {
					errores.add("ContratoRequerido", new ActionMessage("errors.required", 
							fuenteMensaje.getMessage("ParametrizarPresupuestoCapitacionForm.contrato")));
					retorno = "parametrosbusqueda";
				}
			} else {
				errores.add("ConvenioRequerido", new ActionMessage("errors.required", 
						fuenteMensaje.getMessage("ParametrizarPresupuestoCapitacionForm.convenio")));
				retorno = "parametrosbusqueda";
			}	
		} catch (Exception e) {
			UtilidadTransaccion.getTransaccion().rollback();
			Log4JManager.error("Modificando presupuesto capitación", e);
			errores.add("Error", new ActionMessage("errors.procesoNoExitoso", "Funcionalidad no valida"));
		}
		saveErrors(request, errores);
		return retorno;
	}
	
	/**
	 * método encargado de recolectar toda la información ingresada en el formulario con 
	 * las respectivas valorizaciones para la parametrización del presupuesto de capitación
	 * 
	 * Este método obtiene los datos de la parametrización, la valoración general y las 
	 * valoraciones detalladas de grupos de servicio y clases de inventarios para cada uno
	 * de los niveles asociados al contrato, para luego realizar la modificación respectiva
	 * a cada uno de ellos y guardar los registros de log generados 
	 * 
	 * @param forma
	 * @param request
	 * @param usuarioBasico
	 * @return String
	 */
	private String guardarModificacionParametrizacionGeneral(ParametrizarPresupuestoCapitacionForm forma, 
			HttpServletRequest request, UsuarioBasico usuarioBasico) {

		HashMap<Long, Double> valoresActuales = new HashMap<Long, Double>();
		LogParamPresupuestoCap logParametrizacion = new LogParamPresupuestoCap();
		ArrayList<ValorizacionPresCapGen> listaNuevasValorizaciones = new ArrayList<ValorizacionPresCapGen>();
		ArrayList<Integer> listaMesesModificados = new ArrayList<Integer>();
		DtoParametrizacionGeneral dtoParametrizacion = new DtoParametrizacionGeneral();
		ParamPresupuestosCap parametrizacion = forma.getParametrizacionPresupuesto();
		ActionErrors errores = new ActionErrors();
		
		double porcentajeActual = 0D; 
		double nuevoPorcentaje = 0D;
		double porcentageMensualActual = 0D;
		double nuevoPorcentajeMensual = 0D;
		double nuevoValorMes = 0D;
		double nuevoValorGastoMensual = 0D;
		boolean modificarValorGasto = false;
		boolean guardoNuevaValorizacion = false;
		String descripcion = "";
		String mesesModificados = "";
		int index = 0;

		try {
			if (!forma.getValorizacionGeneral().isEmpty()) {

				porcentageMensualActual = forma.getParametrizacionPresupuesto().getPorcentajeGastoGeneral().doubleValue();
				nuevoPorcentajeMensual = forma.getPorcentajeGastoMensual();

				Usuarios usuario = new Usuarios();
				usuario.setLogin(usuarioBasico.getLoginUsuario());
				
				if (forma.isEsModificacionValorContrato()) {
					parametrizacion.setValorContrato(new BigDecimal(forma.getContrato().getValor()));
				}

				if (porcentageMensualActual != nuevoPorcentajeMensual || forma.isEsModificacionValorContrato()) {
					nuevoValorGastoMensual = (forma.getContrato().getValor() * nuevoPorcentajeMensual) / 100D;
					parametrizacion.setPorcentajeGastoGeneral(new BigDecimal(nuevoPorcentajeMensual));
					parametrizacion.setValorGastoGeneral(new BigDecimal(nuevoValorGastoMensual));
					dtoParametrizacion.setParametrizacionPresupuesto(parametrizacion);

					LogDetalleParamPresup logDetalle = new LogDetalleParamPresup();
					logDetalle.setDescipcion(ConstantesCapitacion.porcentajeGastoMensual);
					logDetalle.setMes("-");
					logDetalle.setNivelAtencion(ConstantesCapitacion.porcentajeGastoMensual);
					logDetalle.setNivelModificacion(ConstantesCapitacion.porcentajeGastoMensual);
					logDetalle.setPorcentajeActual(new BigDecimal(nuevoPorcentajeMensual));
					logDetalle.setPorcentajeAnterior(new BigDecimal(porcentageMensualActual));

					forma.getDtoLogParametrizacionPresupuesto().getListaLogDetalleParametrizacion().add(logDetalle);
					modificarValorGasto = true;
				} else {
					dtoParametrizacion.setParametrizacionPresupuesto(parametrizacion);
				}
				
				UtilidadTransaccion.getTransaccion().begin();
				IValorizacionPresupuestoCapGeneralMundo valorizacionGeneralMundo = 
					CapitacionFabricaMundo.crearValorizacionPresupuestoCapGeneralMundo();
				ArrayList<ValorizacionPresCapGen> valorizacionOriginal = 
					valorizacionGeneralMundo.valoracionPresupuestoCap(forma.getParametrizacionPresupuesto().getCodigo());

				UtilidadTransaccion.getTransaccion().commit();
				
				for (ValorizacionPresCapGen valorizacion : valorizacionOriginal) {
					valoresActuales.put(valorizacion.getCodigo(), valorizacion.getPorcentajeGastoSubSeccion().doubleValue());
				}
				for (ValorizacionPresCapGen valorizacion : forma.getValorizacionGeneral()) {
					nuevoPorcentaje =  valorizacion.getPorcentajeGastoSubSeccion().doubleValue();
					porcentajeActual = valoresActuales.get(valorizacion.getCodigo());
					if (porcentajeActual != nuevoPorcentaje || forma.isEsModificacionValorContrato()) {
						if (modificarValorGasto) {
							nuevoValorMes = (nuevoValorGastoMensual * nuevoPorcentaje) / 100D;
							valorizacion.setValorGastoSubSeccion(new BigDecimal(nuevoValorMes));
						} else {
							nuevoValorMes = (forma.getParametrizacionPresupuesto().getValorGastoGeneral().doubleValue() * nuevoPorcentaje) / 100D;
							valorizacion.setValorGastoSubSeccion(new BigDecimal(nuevoValorMes));
						}

						listaNuevasValorizaciones.add(valorizacion);
						
						descripcion = (ConstantesCapitacion.subSeccionServicio.equals(valorizacion.getSubSeccion())) ?
								ConstantesCapitacion.grupoServicios : ConstantesCapitacion.claseInventario;
						
						LogDetalleParamPresup logDetalle = new LogDetalleParamPresup();
						logDetalle.setDescipcion(descripcion);
						logDetalle.setMes(obtenerNombreMes(valorizacion.getMes()));
						logDetalle.setNivelAtencion(valorizacion.getNivelAtencion().getDescripcion());
						logDetalle.setNivelModificacion(ConstantesCapitacion.general);
						logDetalle.setPorcentajeActual(new BigDecimal(nuevoPorcentaje));
						logDetalle.setPorcentajeAnterior(new BigDecimal(porcentajeActual));
						logDetalle.setLogParamPresupuestoCap(logParametrizacion);

						forma.getDtoLogParametrizacionPresupuesto().getListaLogDetalleParametrizacion().add(logDetalle);

					} else {
						if (modificarValorGasto) {
							nuevoValorMes = (nuevoValorGastoMensual * porcentajeActual) / 100D;
							valorizacion.setValorGastoSubSeccion(new BigDecimal(nuevoValorMes));
							listaNuevasValorizaciones.add(valorizacion);
						}
					}
				}

				for (String[] mesesVigencia : forma.getMesesMatriz()) {
					if ("Activo".equals(mesesVigencia[2])) {
						listaMesesModificados.add(index);
					}
					index++;
				}
				mesesModificados = obtenerNombreMes(listaMesesModificados.get(0)) + " - " +
								   obtenerNombreMes(listaMesesModificados.get(listaMesesModificados.size() - 1));
				
				logParametrizacion.setAnioVigencia(forma.getVigencia());
				logParametrizacion.setFechaModificacion(Date.valueOf(Utilidades.capturarFechaBD()));
				logParametrizacion.setHoraModificacion(Utilidades.capturarHoraBD());
				logParametrizacion.setMesesModificados(mesesModificados);
				logParametrizacion.setMotivosModifiPresupuesto(forma.getMotivoModificacion());
				logParametrizacion.setObservaciones(forma.getObservacionesModificacionPresupuesto());
				logParametrizacion.setParamPresupuestosCap(forma.getParametrizacionPresupuesto());
				logParametrizacion.setPorcentajeGastoGeneral(new BigDecimal(nuevoPorcentajeMensual));
				logParametrizacion.setConvenios(forma.getConvenio());
				logParametrizacion.setContratos(forma.getContrato());
				logParametrizacion.setUsuarios(usuario);
				logParametrizacion.setValorContrato(forma.getParametrizacionPresupuesto().getValorContrato());
				logParametrizacion.setValorGastoGeneral(forma.getParametrizacionPresupuesto().getValorGastoGeneral());

				forma.getDtoLogParametrizacionPresupuesto().setLogParametrizacion(logParametrizacion);

				dtoParametrizacion.setValorizacionPresupuestoGeneral(listaNuevasValorizaciones);

				if (forma.getNuevasValorizacionesGeneral() != null && !forma.getNuevasValorizacionesGeneral().isEmpty()) {
					UtilidadTransaccion.getTransaccion().begin();
					guardoNuevaValorizacion = valorizacionGeneralMundo.guardarValorizacionPresupuestoCapitado(forma.getNuevasValorizacionesGeneral());
					UtilidadTransaccion.getTransaccion().commit();
					Log4JManager.info("GUARDO NUEVAS VALORIZACIONES >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> "+guardoNuevaValorizacion);
				}
				
				IParametrizacionPresupuestoCapitacionMundo parametrizacionMundo = 
					CapitacionFabricaMundo.crearParametrizacionPresupuestoCapitacionMundo();
				boolean resultado = parametrizacionMundo.guardarModificacionesParametrizacion(dtoParametrizacion, 
							forma.getDtoNivelesAtencion(), 
							forma.getDtoLogParametrizacionPresupuesto());
								
				forma.getDtoLogParametrizacionPresupuesto().setListaLogDetalleParametrizacion(new ArrayList<LogDetalleParamPresup>());
				if (resultado) {
					forma.setEsModificacionValorContrato(false);
					forma.setGuardoDatos(true);
					forma.setPermiteGuardar(false);
					if (guardoNuevaValorizacion) {
						UtilidadTransaccion.getTransaccion().begin();
						ArrayList<ValorizacionPresCapGen> nuevaValorizacionGeneral = 
							valorizacionGeneralMundo.valoracionPresupuestoCap(dtoParametrizacion.getParametrizacionPresupuesto().getCodigo());
						UtilidadTransaccion.getTransaccion().commit();
						if (nuevaValorizacionGeneral != null && !nuevaValorizacionGeneral.isEmpty()) {
							forma.setValorizacionGeneral(nuevaValorizacionGeneral);
						}
					}
				} else {
					errores.add("ErrorGuardarValorizacion", 
							new ActionMessage("errors.required", fuenteMensaje.
									getMessage("ParametrizarPresupuestoCapitacionForm.mensaje.errorguardado")));
					saveErrors(request, errores);
				}
				
				forma.setMotivoModificacionHelper(ConstantesBD.codigoNuncaValidoLong);
				forma.setObservacionesModificacionPresupuesto("");
				
			}
		} catch (Exception e) {
			UtilidadTransaccion.getTransaccion().rollback();
			Log4JManager.info("Error Guardardando Modificacion Parametrizacion General", e);
			errores.add("Error", new ActionMessage("errors.procesoNoExitoso", "Funcionalidad no valida"));
			saveErrors(request, errores);
		}
		return "modificacionParametrizacionGeneral";
	}

	/**
	 * Método encargado de validar que la información ingresada en la parametrización general 
	 * cumpla con las condiciones necesarias para poder mostrar el mensaje de motivos de 
	 * modificación
	 * 
	 * @param forma
	 * @param request
	 * @return String
	 */
	private String mostrarMotivosModificacion(ParametrizarPresupuestoCapitacionForm forma, HttpServletRequest request) {
		double nuevoPorcentajeMensual = forma.getPorcentajeGastoMensual();
		ActionErrors errores = new ActionErrors();
		
		try {
			if (nuevoPorcentajeMensual > 0 && nuevoPorcentajeMensual <= 100) {
				if(this.validarTotalesGeneral(forma, request, "General", "total_mes_")) {
					forma.setPermiteGuardar(true);
				} else {
					forma.setGuardoDatos(false);
					errores.add("TotalesRequeridos", new ActionMessage("errors.required",  
							fuenteMensaje.getMessage("ParametrizarPresupuestoCapitacionForm.mensaje.totalInvalido")));
				}
				forma.setPorcentajeGastoMensual(nuevoPorcentajeMensual);
			} else {
				errores.add("PorcentajeGastoMensual", new ActionMessage("errors.required", 
						fuenteMensaje.getMessage("ParametrizarPresupuestoCapitacionForm.general.porcentajeGastoMensual")));
			}
			this.cargarDatosValorizacionGeneral(forma, request);
			this.cargarNuevosDatosValorizacionGeneral(forma, request, forma.getParametrizacionPresupuesto());
		} catch (Exception e) {
			Log4JManager.error("Validando datos", e);
			errores.add("Error", new ActionMessage("errors.procesoNoExitoso", "Funcionalidad no valida"));
		}
		saveErrors(request, errores);
		return "modificacionParametrizacionGeneral";
	}
	
	/**
	 * Método encargado de obtener las valorizaciones para el grupo de servicio requerido 
	 * y cargar los datos en la forma para su posterior modificación
	 * @param forma
	 * @param request
	 * @param usuarioBasico
	 * @param tipoOperacion
	 * @return String
	 */
	private String modificarDetalleNivelAtencionGrupoServicio(ParametrizarPresupuestoCapitacionForm forma, 
			HttpServletRequest request, UsuarioBasico usuarioBasico, String tipoOperacion) {
		
		ActionErrors errores = new ActionErrors();
		ArrayList<DetalleValorizacionServ> valorizacionDetalleServicio = 
							   					new ArrayList<DetalleValorizacionServ>();
		
		String retorno = ("consulta".equals(tipoOperacion)) ? 
				"detalleNivelAtencionGrupoServicio" : "modificacionDetalleGrupoServicio";
		
		try {
			
			if(!"consulta".equals(tipoOperacion)) {
				this.cargarDatosValorizacionGeneral(forma, request);
			}
			
			UtilidadTransaccion.getTransaccion().begin();
			
			IDetalleValorizacionServicioMundo detalleValorizacionServicioMundo = 
				CapitacionFabricaMundo.crearDetalleValorizacionServicioMundo();
			
			for (DtoNivelesAtencionPresupuestoParametrizacionGeneral dtoNivelesAtencion : forma.getDtoNivelesAtencion()) {
				NivelAtencion nivel = dtoNivelesAtencion.getNivelAtencion();
				if(nivel.getConsecutivo() == forma.getNivelActual().longValue()) {
					forma.setNivelAtencion(nivel);
					if ( dtoNivelesAtencion.getDetalleValorizacionServicios() != null 
							&& ! dtoNivelesAtencion.getDetalleValorizacionServicios().isEmpty()) {
						valorizacionDetalleServicio = dtoNivelesAtencion.getDetalleValorizacionServicios();
					} else {
						valorizacionDetalleServicio = detalleValorizacionServicioMundo.obtenerValorizacionDetalleGrupoServicio(
								forma.getParametrizacionPresupuesto().getCodigo(), forma.getNivelActual());
					}
				}
			}	
			
			UtilidadTransaccion.getTransaccion().commit();
			
			if (valorizacionDetalleServicio != null) {
				forma.setValorizacionDetalleGrupoServicio(valorizacionDetalleServicio);
			} else {
				errores.add("valorizacionGeneralExiste", 
						new ActionMessage("error.errorEnBlanco", fuenteMensaje.
								getMessage("ParametrizarPresupuestoCapitacionForm.noExistenRegistros")));
				retorno = "parametrosbusqueda";
			}
		} catch (Exception e) {
			UtilidadTransaccion.getTransaccion().rollback();
			Log4JManager.error("Modificar Detalle Nivel Atencion GrupoServicio", e);
			errores.add("Error", new ActionMessage("errors.procesoNoExitoso", "Funcionalidad no valida"));
		}
		return retorno;
	}


	/**
	 * Método encargado de obtener las valorizaciones para la clase de inventario
	 * requerido y cargar los datos en la forma para su posterior modificación
	 * 
	 * @param forma
	 * @param request
	 * @param usuarioBasico
	 * @param tipoOperacion
	 * @return String
	 */
	private String modificarDetalleNivelAtencionClaseInventario(ParametrizarPresupuestoCapitacionForm forma, 
			HttpServletRequest request, UsuarioBasico usuarioBasico, String tipoOperacion) {
		
		ActionErrors errores = new ActionErrors();
		ArrayList<DetalleValorizacionArt> valorizacionDetalleClaseInventario = 
							   					new ArrayList<DetalleValorizacionArt>();
		
		String retorno = ("consulta".equals(tipoOperacion)) ? 
				"detalleNivelAtencionClaseInventario" : "modificacionDetalleClaseInventario";
		
		try {
			
			if(!"consulta".equals(tipoOperacion)) {
				this.cargarDatosValorizacionGeneral(forma, request);
			}
			
			UtilidadTransaccion.getTransaccion().begin();
			
			IDetalleValorizacionArticuloMundo detalleValorizacionArticuloMundo = 
				CapitacionFabricaMundo.crearDetalleValorizacionArticuloMundo();

			for (DtoNivelesAtencionPresupuestoParametrizacionGeneral dtoNivelesAtencion : forma.getDtoNivelesAtencion()) {
				NivelAtencion nivel = dtoNivelesAtencion.getNivelAtencion();
				if(nivel.getConsecutivo() == forma.getNivelActual().longValue()) {
					forma.setNivelAtencion(nivel);
					if ( dtoNivelesAtencion.getDetalleValorizacionArticulos() != null 
							&& ! dtoNivelesAtencion.getDetalleValorizacionArticulos().isEmpty()) {
						valorizacionDetalleClaseInventario = dtoNivelesAtencion.getDetalleValorizacionArticulos();
					} else {
						valorizacionDetalleClaseInventario = detalleValorizacionArticuloMundo.obtenerValorizacionDetalleClaseInventario(
								forma.getParametrizacionPresupuesto().getCodigo(), forma.getNivelActual());
						Log4JManager.info("Tamaño de la Lista recuperada de la bd " + valorizacionDetalleClaseInventario.size());
					}
				}
			}	

			UtilidadTransaccion.getTransaccion().commit();

			if (valorizacionDetalleClaseInventario != null) {
				forma.setValorizacionDetalleClaseInventario(valorizacionDetalleClaseInventario);
			} else {
				errores.add("valorizacionGeneralExiste", 
						new ActionMessage("error.errorEnBlanco", fuenteMensaje.
								getMessage("ParametrizarPresupuestoCapitacionForm.noExistenRegistros")));
				retorno = "parametrosbusqueda";
			}
		} catch (Exception e) {
			UtilidadTransaccion.getTransaccion().rollback();
			Log4JManager.error("Modificar Detalle Nivel Atencion Clase Inventario", e);
			errores.add("Error", new ActionMessage("errors.procesoNoExitoso", "Funcionalidad no valida"));
		}
		return retorno;
	}

	
	/**
	 * Método encargado de validar los totales obtenidos de la forma
	 * 
	 * @param forma
	 * @param request
	 * @param tipo
	 * @param prefijoTotales
	 * @return String
	 */
	private boolean validarTotalesGeneral(ParametrizarPresupuestoCapitacionForm forma, HttpServletRequest request, 
			String tipo, String prefijoTotales) throws Exception {
		int i = 0;
		boolean resultado = true;
		double total = 0;
		ArrayList<Double> totalesParametrizacionGeneral = new ArrayList<Double>();
		for (String[] meses : forma.getMesesMatriz()) {
			if ("Activo".equals(meses[2])) {
				String totalMes = request.getParameter(prefijoTotales+i);
				if (totalMes != null && !"".equals(totalMes)) {
					total = Double.parseDouble(totalMes);
					totalesParametrizacionGeneral.add(total);
					if (total != 100) {
						resultado = false;
					}
				} else {
					resultado = false;
				}
			}
			totalesParametrizacionGeneral.add(total);
			total = 0;
			i++;
		}
		forma.setTotalesParametrizacionGeneral(totalesParametrizacionGeneral);
		return resultado;
	}
	
	/**
	 *  Método encargado de validar los totales del grupo de servicio obtenidos de la forma
	 * 
	 * @param forma
	 * @param request
	 * @param prefijoTotales
	 * @return String
	 */
	private boolean validarTotalesGrupoServicio(ParametrizarPresupuestoCapitacionForm forma, HttpServletRequest request, 
			String prefijoTotales) throws Exception {
		int i = 0;
		boolean resultado = true;
		double total = 0;
		ArrayList<Double> totalesDetalleGrupoServicio = new ArrayList<Double>();
		for (String[] meses : forma.getMesesMatriz()) {
			if ("Activo".equals(meses[2])) {
				String totalMes = request.getParameter(prefijoTotales+i);
				if (totalMes != null && !"".equals(totalMes)) {
					total = Double.parseDouble(totalMes);
					if (total != 100) {
						resultado = false;
					}
				} else {
					resultado = false;
				}
			}
			totalesDetalleGrupoServicio.add(total);
			total = 0;
			i++;
		}
		forma.setTotalesDetalleGrupoServicio(totalesDetalleGrupoServicio);
		return resultado;
	}
	
	/**
	 *  Método encargado de validar los totales de la clase de inventario obtenidos de la forma
	 *  
	 * @param forma
	 * @param request
	 * @param prefijoTotales
	 * @return String
	 */
	private boolean validarTotalesClaseInventario(ParametrizarPresupuestoCapitacionForm forma, HttpServletRequest request, 
			String prefijoTotales) throws Exception {
		int i = 0;
		double total = 0;
		boolean resultado = true;
		ArrayList<Double> totalesDetalleClaseInventario = new ArrayList<Double>();
		for (String[] meses : forma.getMesesMatriz()) {
			if ("Activo".equals(meses[2])) {
				String totalMes = request.getParameter(prefijoTotales+i);
				if (totalMes != null && !"".equals(totalMes)) {
					total = Double.parseDouble(totalMes);
					totalesDetalleClaseInventario.add(total);
					if (total != 100) {
						resultado = false;
					}
				} else {
					resultado = false;
				}
			}
			totalesDetalleClaseInventario.add(total);
			total = 0;
			i++;
		}
		forma.setTotalesDetalleClaseInventario(totalesDetalleClaseInventario);
		return resultado;
	}
	
	/**
	 * Método encargado de cargar los valores ingresados en la forma para 
	 * la parametrización general
	 * 
	 * @param forma
	 * @param request
	 */
	private void cargarDatosValorizacionGeneral(ParametrizarPresupuestoCapitacionForm forma, 
			HttpServletRequest request) throws Exception {
		String valorTmp = "";
		double nuevoPorcentaje = 0D;
		int index = 0;
		for (ValorizacionPresCapGen valorizacion : forma.getValorizacionGeneral()) {
			valorTmp = request.getParameter("mes_"+valorizacion.getMes() + "_valor_" + valorizacion.getCodigo() +
					"_" +valorizacion.getSubSeccion() + "_" + valorizacion.getNivelAtencion().getConsecutivo());
			nuevoPorcentaje = ( valorTmp != null && !"".equals(valorTmp)) ? Double.parseDouble(valorTmp): 0D;
			valorizacion.setPorcentajeGastoSubSeccion(new BigDecimal(nuevoPorcentaje));
			forma.getValorizacionGeneral().set(index, valorizacion);
			index++;
		} 
	}
	
	/**
	 * Método encargado de cargar los valores ingresados en la forma para 
	 * la parametrizacion del grupo de servicio seleccionado 
	 * 
	 * @param forma
	 * @param request
	 * @param tipo
	 */
	private void cargarDatosGrupoServicio(ParametrizarPresupuestoCapitacionForm forma, 
			HttpServletRequest request, String tipo) throws Exception {
		if ("crear".equals(tipo)) {
			for (DtoNivelesAtencionPresupuestoParametrizacionGeneral dtoNivelesAtencion : forma.getDtoNivelesAtencion()) {
				NivelAtencion nivel = dtoNivelesAtencion.getNivelAtencion();
				if(nivel.getConsecutivo() == forma.getNivelActual().longValue()) {
					for (GruposServicios grupo : dtoNivelesAtencion.getListaGruposServicios()) {
						int j = 0;
						for (String[] mesMatriz : forma.getMesesMatriz()) {
							if ("Activo".equals(mesMatriz[2])) {
								String porcentajeValorTemp = request.getParameter(mesMatriz[0] + "_grupo_servicio_"+grupo.getCodigo()+"_");
								BigDecimal porcentajeServicio = 
									(porcentajeValorTemp != null && !"".equals(porcentajeValorTemp)) ? 
											new BigDecimal(porcentajeValorTemp) : new BigDecimal(0);
								DetalleValorizacionServ detalle = new DetalleValorizacionServ();
								detalle.setMes(j);
								detalle.setNivelAtencion(nivel);
								detalle.setGruposServicios(grupo);
								detalle.setPorcentajeGasto(porcentajeServicio);
								forma.getValorizacionDetalleGrupoServicio().add(detalle);
							}
							j++;
						}
					}
				}
			}
		} else if ("modificar".equals(tipo)) {
			String valorTmp = "";
			double nuevoPorcentaje = 0D;
			int index = 0;
			for (DetalleValorizacionServ detallesValorizacionServicio : forma.getValorizacionDetalleGrupoServicio()) {
				valorTmp = request.getParameter("mes_"+detallesValorizacionServicio.getMes()+ "_grupo_servicio_" + 
						detallesValorizacionServicio.getGruposServicios().getCodigo() + "_valor_" +
						detallesValorizacionServicio.getCodigo() );
				nuevoPorcentaje = ( valorTmp != null && !"".equals(valorTmp)) ? Double.parseDouble(valorTmp): 0D;
				detallesValorizacionServicio.setPorcentajeGasto(new BigDecimal(nuevoPorcentaje));
				forma.getValorizacionDetalleGrupoServicio().set(index, detallesValorizacionServicio);
				index++;
			}
		}
	}
	
	/**
	 * Método encargado de cargar los valores ingresados en la forma para 
	 * la parametrizacion de la clase de inventario seleccionado 
	 * 
	 * @param forma
	 * @param request
	 * @param tipo
	 */
	private void cargarDatosClaseInventario(ParametrizarPresupuestoCapitacionForm forma, 
			HttpServletRequest request, String tipo) throws Exception {
		if ("crear".equals(tipo)) {
			for (DtoNivelesAtencionPresupuestoParametrizacionGeneral dtoNivelesAtencion : forma.getDtoNivelesAtencion()) {
				NivelAtencion nivel = dtoNivelesAtencion.getNivelAtencion();
				if(nivel.getConsecutivo() == forma.getNivelActual().longValue()) {
					for (ClaseInventario clase : dtoNivelesAtencion.getListaClasesInventario()) {
						int j = 0;
						for (String[] mesMatriz : forma.getMesesMatriz()) {
							if ("Activo".equals(mesMatriz[2])) {
								String porcentajeValorTemp = request.getParameter(mesMatriz[0] + "_clase_inventario_"+clase.getCodigo()+"_");
								BigDecimal porcentajeServicio = 
									(porcentajeValorTemp != null && !"".equals(porcentajeValorTemp)) ? 
											new BigDecimal(porcentajeValorTemp) : new BigDecimal(0);
											DetalleValorizacionArt detalle = new DetalleValorizacionArt();
											detalle.setMes(j);
											detalle.setNivelAtencion(nivel);
											detalle.setClaseInventario(clase);
											detalle.setPorcentajeGasto(porcentajeServicio);
											forma.getValorizacionDetalleClaseInventario().add(detalle);
							}
							j++;
						}
					}
				}
			}
		} else if ("modificar".equals(tipo)) {
			String valorTmp = "";
			double nuevoPorcentaje = 0D;
			int index = 0;
			for (DetalleValorizacionArt detallesValorizacionClaseInventario : forma.getValorizacionDetalleClaseInventario()) {
				valorTmp = request.getParameter("mes_"+detallesValorizacionClaseInventario.getMes()+ "_clase_inventario_" + 
						detallesValorizacionClaseInventario.getClaseInventario().getCodigo() + "_valor_" +
						detallesValorizacionClaseInventario.getCodigo() );
				nuevoPorcentaje = ( valorTmp != null && !"".equals(valorTmp)) ? Double.parseDouble(valorTmp): 0D;
				detallesValorizacionClaseInventario.setPorcentajeGasto(new BigDecimal(nuevoPorcentaje));
				forma.getValorizacionDetalleClaseInventario().set(index, detallesValorizacionClaseInventario);
				index++;
			}
		}
	}

	/**
	 * Método que se encarga de cargar la parametrización para los servicios o medicamentos de 
	 * un nivel de servicio que se agregaron después de la creación de la parametrización y 
	 * aparecen reflejados en la modificación
	 * @param forma
	 * @param request
	 * @param parametrizacionPresupuesto
	 */
	private void cargarNuevosDatosValorizacionGeneral (ParametrizarPresupuestoCapitacionForm forma, 
			HttpServletRequest request, ParamPresupuestosCap parametrizacionPresupuesto) {
		
		ArrayList<ValorizacionPresCapGen> valorizacionGeneral =	new ArrayList<ValorizacionPresCapGen>();
		
		for (DtoNivelesAtencionPresupuestoParametrizacionGeneral dtoNivelesAtencion : forma.getDtoNivelesAtencion()) {
			NivelAtencion nivel = dtoNivelesAtencion.getNivelAtencion();
			int i = 0;
			for (String[] mesMatriz : forma.getMesesMatriz()) {
				if ("Activo".equals(mesMatriz[2])) {
					double valorGastoSubSeccion = 0D;
					if (dtoNivelesAtencion.isExistenServicios()) {
						String porcentajeServicioTemp = request.getParameter(mesMatriz[0] + "_" + ConstantesCapitacion.subSeccionServicio + "_"+nivel.getConsecutivo()+"_");
						if ((porcentajeServicioTemp != null)) {
							if ("".equals(porcentajeServicioTemp)) {
								porcentajeServicioTemp = "0";
							}
							BigDecimal porcentajeServicio = new BigDecimal(porcentajeServicioTemp);
							valorGastoSubSeccion = (forma.getValorGastoMensual() * porcentajeServicio.doubleValue()) / 100D;
							ValorizacionPresCapGen valor = new ValorizacionPresCapGen();
							valor.setParamPresupuestosCap(parametrizacionPresupuesto);
							valor.setMes(i);
							valor.setNivelAtencion(nivel);
							valor.setSubSeccion(ConstantesCapitacion.subSeccionServicio);
							valor.setPorcentajeGastoSubSeccion(porcentajeServicio);
							valor.setValorGastoSubSeccion(new BigDecimal(valorGastoSubSeccion));
							valor.setActivo(ConstantesBD.acronimoSiChar);

							valorizacionGeneral.add(valor);
							
						}			
						
					}
					if (dtoNivelesAtencion.isExistenArticulos()) {
						String porcentajeArticuloTemp = request.getParameter(mesMatriz[0] + "_"+ ConstantesCapitacion.subSeccionArticulo +"_"+nivel.getConsecutivo()+"_");
						if(porcentajeArticuloTemp != null) {
							if ("".equals(porcentajeArticuloTemp)) {
								porcentajeArticuloTemp = "0";
							}
							BigDecimal porcentajeArticulo = new BigDecimal(porcentajeArticuloTemp);		
							valorGastoSubSeccion = (forma.getValorGastoMensual() * porcentajeArticulo.doubleValue()) / 100D;
							ValorizacionPresCapGen valor = new ValorizacionPresCapGen();
							valor.setParamPresupuestosCap(parametrizacionPresupuesto);
							valor.setMes(i);
							valor.setNivelAtencion(nivel);
							valor.setSubSeccion(ConstantesCapitacion.subSeccionArticulo);
							valor.setPorcentajeGastoSubSeccion(porcentajeArticulo);
							valor.setValorGastoSubSeccion(new BigDecimal(valorGastoSubSeccion));
							valor.setActivo(ConstantesBD.acronimoSiChar);

							valorizacionGeneral.add(valor);
							
						}
					}
				}
				i++;
			}
		}
		forma.setNuevasValorizacionesGeneral(valorizacionGeneral);
	}
	
	/**
	 * Método encargado de obtener la lista de resultados obtenidos en la 
	 * busqueda de logs de la parametrización del presupuesto
	 * 
	 * @param forma
	 * @param request
	 * @return String
	 */
	public String listaResultadosBusquedaLog(ParametrizarPresupuestoCapitacionForm forma, HttpServletRequest request) {
		ActionErrors errores = new ActionErrors();
		boolean existeError = false;
		String retorno = "listaResultadosBusquedaLog";
		
		try {
			
			ILogParametrizacionPresupuestoCapMundo logParametrizacionMundo = 
				CapitacionFabricaMundo.crearLogParametrizacionPresupuestoCapMundo();
			
			if(forma.getFechaInicialLog() == null || "".equals(forma.getFechaInicialLog())) {
				errores.add("FechaInicial", new ActionMessage("errors.required", 
						fuenteMensaje.getMessage("ParametrizarPresupuestoCapitacionForm.log.fechaInicial")));
				existeError = true;
			} else {
				Log4JManager.info(forma.getFechaInicialLog() + " - " + Utilidades.capturarFechaBD());
				if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(forma.getFechaInicialLog(), 
						UtilidadFecha.conversionFormatoFechaAAp(Utilidades.capturarFechaBD()))) {
					errores.add("FechaInicial", new ActionMessage("errors.fechaPosteriorIgualAOtraDeReferencia", 
							fuenteMensaje.getMessage("ParametrizarPresupuestoCapitacionForm.log.fechaInicial"), 
							fuenteMensaje.getMessage("ParametrizarPresupuestoCapitacionForm.log.fechaActual")));
					existeError = true;
				}
			}
			if(forma.getFechaFinalLog() == null || "".equals(forma.getFechaFinalLog())) {
				errores.add("FechaFinal", new ActionMessage("errors.required", 
						fuenteMensaje.getMessage("ParametrizarPresupuestoCapitacionForm.log.fechaFinal")));
				existeError = true;
			} else if(!existeError) {
				if (!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(forma.getFechaInicialLog(), forma.getFechaFinalLog()) ||
						!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(forma.getFechaFinalLog(), 
								UtilidadFecha.conversionFormatoFechaAAp(Utilidades.capturarFechaBD()))) {
					errores.add("FechaFinal", new ActionMessage("errors.fechaPosteriorIgualAOtraDeReferencia", 
							fuenteMensaje.getMessage("ParametrizarPresupuestoCapitacionForm.log.fechaFinal"),
							fuenteMensaje.getMessage("ParametrizarPresupuestoCapitacionForm.log.fechaActual")));
					existeError = true;
				}
			}	
			if (!existeError) {
				DtoLogBusquedaParametrizacion dtoBusqueda = new DtoLogBusquedaParametrizacion();
				dtoBusqueda.setFechaInicial(Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(forma.getFechaInicialLog())));
				dtoBusqueda.setFechaFinal(Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(forma.getFechaFinalLog())));
				dtoBusqueda.setCodigoConvenio(forma.getConvenioHelper());
				dtoBusqueda.setCodigoContrato(forma.getContratoHelper());
				dtoBusqueda.setFechaVigencia(forma.getVigenciaHelper());

				UtilidadTransaccion.getTransaccion().begin();
				
				ArrayList<DtoLogParamPresupCap> listaLogsParametrizacion =
					logParametrizacionMundo.listarResultadosBusquedaLogParametrizacionPresupuesto(dtoBusqueda);
				Log4JManager.info(listaLogsParametrizacion.size());
				forma.setListaDtoLogsParametrizacion(listaLogsParametrizacion);
				
				UtilidadTransaccion.getTransaccion().commit();
				
				if (!listaLogsParametrizacion.isEmpty()) {
					if (listaLogsParametrizacion.size() == 1) {
						
						retorno = mostrarLogDetalladoPresupuestoCapitacion(forma, request, listaLogsParametrizacion.get(0).getCodigoLog());
						forma.setLogParametrizacionPresupuesto(logParametrizacionMundo.findById(listaLogsParametrizacion.get(0).getCodigoLog()));
						forma.setSeleccionLogxFecha(false);
						
					}
				} else {
					errores.add("valorizacionGeneralExiste", 
							new ActionMessage("error.errorEnBlanco", fuenteMensaje.
									getMessage("ParametrizarPresupuestoCapitacionForm.noExistenRegistros")));
					retorno = "parametrosBusquedaLog";
				}
				
				
				
			} else {
				retorno = "parametrosBusquedaLog";
			}
		} catch (Exception e) {
			Log4JManager.error("Listando resultados busqueda log", e);
			errores.add("Error", new ActionMessage("errors.procesoNoExitoso", ""));
			retorno = "paginaErrorPresupuestoCapitacion";
		}
		saveErrors(request, errores);
		return retorno;
	}

	/**
	 * Método encargado de obtener la lista de detalles del log de la
	 * parametrización del presupuesto seleccionado
	 * 
	 * @param forma
	 * @param request
	 * @param codigoLog
	 * @return String
	 */
	private String mostrarLogDetalladoPresupuestoCapitacion(ParametrizarPresupuestoCapitacionForm forma, 
			HttpServletRequest request, long codigoLog) throws Exception {
		ActionErrors errores = new ActionErrors();
		String retorno = "detalleLogModificacionParametrizacionPresupuesto";
		
		UtilidadTransaccion.getTransaccion().begin();
		
		ILogDetalleParametrizacionPresupuestoMundo logDetalleParametrizacionMundo = 
			CapitacionFabricaMundo.crearLogDetalleParametrizacionPresupuestoMundo();
		
		ArrayList<LogDetalleParamPresup> listaDetallesLog = 
			logDetalleParametrizacionMundo.obtenerLogDetalladoParametrizacionPresupuesto(codigoLog);
		
		ILogParametrizacionPresupuestoCapMundo logParametrizacionMundo = 
			CapitacionFabricaMundo.crearLogParametrizacionPresupuestoCapMundo();
		
		if (listaDetallesLog != null) {
			forma.setListaDetallesLogParametrizacion(listaDetallesLog);
			for (DtoLogParamPresupCap dtoLogParamPresup : forma.getListaDtoLogsParametrizacion()) {
				if (dtoLogParamPresup.getCodigoLog() == codigoLog) {
					forma.setLogParametrizacionPresupuesto(logParametrizacionMundo.findById(dtoLogParamPresup.getCodigoLog()));
				}
			}
		} else {
			retorno = "parametrosBusquedaLog";
			errores.add("valorizacionGeneralExiste", 
					new ActionMessage("error.errorEnBlanco", fuenteMensaje.
							getMessage("ParametrizarPresupuestoCapitacionForm.noExistenRegistros")));
		}
		UtilidadTransaccion.getTransaccion().commit();
		saveErrors(request, errores);
		return retorno;
	}
	
	/**
	 * Este m&eacute;todo se encarga de ordenar las columnas de el resultado 
	 * de la b&uacute;squeda sig&aacute;n los par&aacute;metros ingresados
	 * 
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @return ActionForward
	 */
	public String accionOrdenar(ParametrizarPresupuestoCapitacionForm forma, String tipo){
		
		String retorno = "";
		boolean ordenamiento = false;
		
		if(forma.getEsDescendente().equals(forma.getPatronOrdenar() + "descendente")) {
			ordenamiento = true;
		}
		
		SortGenerico sortG = new SortGenerico(forma.getPatronOrdenar(),ordenamiento);
		
		if ("ListaResultados".equals(tipo)) {
			Collections.sort(forma.getListaDtoLogsParametrizacion(), sortG);
			retorno = "listaResultadosBusquedaLog";
		} else if ("DetalleLog".equals(tipo)){
			Collections.sort(forma.getListaDetallesLogParametrizacion(), sortG);
			retorno = "detalleLogModificacionParametrizacionPresupuesto";
		} else if ("ListaAnios".equals(tipo)){
			Collections.sort(forma.getListadoParametrizaciones(), sortG);
			retorno = "listadoPresupuestoCapitacion";
		}
		return retorno;
	}
	
	public String obtenerInfoTooltipModificacionDetalle(ParametrizarPresupuestoCapitacionForm forma, 
			String codigoNivel, String mes, String porcentajeActual, String nuevoPorcentaje, String subSeccion) {
		long codNivel = 0L;
		int mesValorizacion = -1;
		double valorActual = 0D;
		double valorGastoSubSeccionGeneral = 0D;
		double nuevoValor = 0D;
		double nuevoValorValorizacion = 0D;
		String contenidoDiv = "";
		try {
			
			String subSeccionTemp = (subSeccion.equals(ConstantesCapitacion.subSeccionArticulo)) ?
					ConstantesCapitacion.medicamentos : ConstantesCapitacion.servicios;
			
			if (codigoNivel != null && !"".equals(codigoNivel)) {
				if (mes != null && !"".equals(mes)) {
					if (nuevoPorcentaje != null && porcentajeActual != null 
							&& !"".equals(nuevoPorcentaje) && !"".equals(porcentajeActual)) {
						IValorizacionPresupuestoCapGeneralMundo valorizacionGeneralMundo = 
							CapitacionFabricaMundo.crearValorizacionPresupuestoCapGeneralMundo();
						codNivel = Long.parseLong(codigoNivel);
						mesValorizacion = Integer.parseInt(mes);
						UtilidadTransaccion.getTransaccion().begin();
						ValorizacionPresCapGen valorizacion = valorizacionGeneralMundo.obtenerValorizacionGeneralxNivelAtencionSubSeccionMes(
								forma.getParametrizacionPresupuesto().getCodigo(), codNivel, mesValorizacion, subSeccion);
						UtilidadTransaccion.getTransaccion().commit();

						valorGastoSubSeccionGeneral = valorizacion.getValorGastoSubSeccion().doubleValue();
						valorActual = (valorizacion.getValorGastoSubSeccion().doubleValue() * Double.parseDouble(porcentajeActual)) / 100D;
						
						for (ValorizacionPresCapGen nuevaValorizacion : forma.getValorizacionGeneral()) {
							if (nuevaValorizacion.getNivelAtencion().getConsecutivo() == codNivel &&
									String.valueOf(nuevaValorizacion.getMes()).equals(mes) &&
									nuevaValorizacion.getSubSeccion().equals(subSeccion)) {
								nuevoValorValorizacion = (forma.getValorGastoMensual() * nuevaValorizacion.getPorcentajeGastoSubSeccion().doubleValue()) / 100;
								nuevoValor = (((forma.getValorGastoMensual() * nuevaValorizacion.getPorcentajeGastoSubSeccion().doubleValue()) / 100) * Double.parseDouble(nuevoPorcentaje)) / 100D;
							}
						}
					}
				}
			}
			
			contenidoDiv += "<div><table width='100%' border='0' cellpadding='4' cellspacing='1' bgcolor='#006898'>" + 
				"<tr><td class=Subtitulo>" +
				fuenteMensaje.getMessage("ParametrizarPresupuestoCapitacionForm.mensaje.informacionValorizacionPresupuesto") +
				"</td></tr>";

			contenidoDiv += "<tr bgcolor='#FFFFFF'><td>" +
						"<strong>Valor Gasto Mensual " + subSeccionTemp + ":</strong> " + "<br>" + 
						UtilidadTexto.formatearValores(valorGastoSubSeccionGeneral) + "<br>" +
						"<strong>Porcentaje Gasto Mensual Nivel " + subSeccionTemp + ":</strong> <br> " + 
						UtilidadTexto.formatearValores(porcentajeActual) + "&nbsp;%<br>" + 
						"<strong>Valor Gasto Mensual " + subSeccionTemp + ":</strong> " + "<br>" +
						UtilidadTexto.formatearValores(valorActual) +
						"</td></tr>";
			
			contenidoDiv += "<tr><td class=Subtitulo>" +
						fuenteMensaje.getMessage("ParametrizarPresupuestoCapitacionForm.mensaje.nuevosValoresPresupuesto") +
						"</td></tr>";
			
			contenidoDiv += "<tr bgcolor='#FFFFFF'><td>" +
						"<strong>Valor Gasto Mensual " + subSeccionTemp + ":</strong> " + "<br>" + 
						UtilidadTexto.formatearValores(nuevoValorValorizacion) + "<br>" +
						"<strong>Porcentaje Gasto Mensual Nivel " + subSeccionTemp + ":</strong> <br> " + 
						UtilidadTexto.formatearValores(nuevoPorcentaje) + "&nbsp;%<br>" + 
						"<strong>Valor Gasto Mensual " + subSeccionTemp + ":</strong> " + "<br>" +
						UtilidadTexto.formatearValores(nuevoValor) +
						"</td></tr>";
			
			contenidoDiv += "</table></div>";		
			
		} catch (Exception e) {
			Log4JManager.error("Mostrando tooltip", e);
		}
		return contenidoDiv;
	}
	
	public String obtenerInfoTooltipModificacionGeneral(ParametrizarPresupuestoCapitacionForm forma, 
			String porcentajeActual, String nuevoPorcentaje, String subSeccion) {
		double valorActual = 0D;
		double nuevoValor = 0D;
		String contenidoDiv = "";
		
		String subSeccionTemp = (subSeccion.equals(ConstantesCapitacion.subSeccionArticulo)) ?
				ConstantesCapitacion.medicamentos : ConstantesCapitacion.servicios;
		
		try {
			if (nuevoPorcentaje != null && !"".equals(nuevoPorcentaje) && 
					porcentajeActual != null && !"".equals(porcentajeActual)) {
				valorActual = (forma.getParametrizacionPresupuesto().getValorGastoGeneral().doubleValue() * Double.parseDouble(porcentajeActual)) / 100D;
				nuevoValor = (forma.getValorGastoMensual() * Double.parseDouble(nuevoPorcentaje)) / 100D;
			}
			contenidoDiv = 	"<div><table width='100%' border='0' cellpadding='4' cellspacing='1' bgcolor='#006898'>" + 
							"<tr><td class=Subtitulo>" +
							fuenteMensaje.getMessage("ParametrizarPresupuestoCapitacionForm.mensaje.informacionValorizacionPresupuesto") +
							"</td></tr>";
			
			contenidoDiv += "<tr bgcolor='#FFFFFF'><td>" +
							"<strong>Valor Gasto Mensual:</strong> " + "<br>" + 
							UtilidadTexto.formatearValores(forma.getParametrizacionPresupuesto().getValorGastoGeneral().doubleValue()) + "<br>" +
							"<strong>Porcentaje Gasto Mensual Nivel " + subSeccionTemp + ":</strong> <br> " + 
							UtilidadTexto.formatearValores(porcentajeActual) + "&nbsp;%<br>" + 
							"<strong>Valor Gasto Mensual " + subSeccionTemp + ":</strong> " + "<br>" +
							UtilidadTexto.formatearValores(valorActual) +
							"</td></tr>";

			contenidoDiv += "<tr><td class=Subtitulo>" +
							fuenteMensaje.getMessage("ParametrizarPresupuestoCapitacionForm.mensaje.nuevosValoresPresupuesto") +
							"</td></tr>";

			contenidoDiv += "<tr bgcolor='#FFFFFF'><td>" +
							"<strong>Valor Gasto Mensual:</strong> " + "<br>" + 
							UtilidadTexto.formatearValores(forma.getValorGastoMensual()) + "<br>" +
							"<strong>Porcentaje Gasto Mensual Nivel " + subSeccionTemp + ":</strong> <br> " + 
							UtilidadTexto.formatearValores(nuevoPorcentaje) + "&nbsp;%<br>" + 
							"<strong>Valor Gasto Mensual " + subSeccionTemp + ":</strong> " + "<br>" +
							UtilidadTexto.formatearValores(nuevoValor) + "</td></tr>";
			
			contenidoDiv += "</table></div>";		

		} catch (Exception e) {
			Log4JManager.error("Mostrando tooltip", e);
		}
		return contenidoDiv;
	}
	

	/**
	 * 
	 * @param idMes
	 * @return String
	 */
	public String obtenerNombreMes(int idMes) {
		String nombre = "";
		switch (idMes) {
		case 0: 
			nombre ="Enero";
			break;
		case 1: 
			nombre ="Febrero";
			break;
		case 2: 
			nombre ="Marzo";
			break;
		case 3: 
			nombre ="Abril";
			break;
		case 4: 
			nombre ="Mayo";
			break;
		case 5: 
			nombre ="Junio";
			break;
		case 6: 
			nombre ="Julio";
			break;
		case 7: 
			nombre ="Agosto";
			break;
		case 8: 
			nombre ="Septiembre";
			break;
		case 9: 
			nombre ="Octubre";
			break;
		case 10: 
			nombre ="Noviembre";
			break;
		case 11: 
			nombre ="Diciembre";
			break;
		default:
			nombre ="El mes no existe";
			break;
		}
		return nombre;
	}
	
}
