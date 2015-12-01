package com.princetonsa.action.manejoPaciente;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JasperPrint;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.util.MessageResources;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.facturacion.InfoCobertura;

import com.ibm.icu.util.Calendar;
import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.manejoPaciente.AutorizacionesIngresoEstanciaForm;
import com.princetonsa.actionform.manejoPaciente.AutorizacionesServiciosMedicamentosIngresoEstanciaForm;
import com.princetonsa.dto.administracion.DtoPaciente;
import com.princetonsa.dto.capitacion.DTOPacienteCapitado;
import com.princetonsa.dto.facturacion.DtoEntidadSubcontratada;
import com.princetonsa.dto.facturacion.DtoViewFinalidadesServ;
import com.princetonsa.dto.inventario.DtoArticulosAutorizaciones;
import com.princetonsa.dto.inventario.DtoServiciosAutorizaciones;
import com.princetonsa.dto.manejoPaciente.DTOAdministracionAutorizacion;
import com.princetonsa.dto.manejoPaciente.DTOReporteAutorizacionSeccionAutorizacion;
import com.princetonsa.dto.manejoPaciente.DTOReporteAutorizacionSeccionPaciente;
import com.princetonsa.dto.manejoPaciente.DtoGeneralReporteArticulosAutorizados;
import com.princetonsa.dto.manejoPaciente.DtoGeneralReporteServiciosAutorizados;
import com.princetonsa.dto.manejoPaciente.DtoServicioAutoHelper;
import com.princetonsa.dto.manejoPaciente.DtoUsuariosCapitados;
import com.princetonsa.enu.manejoPaciente.EnumTipoAutorizacion;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.facturacion.Cobertura;
import com.princetonsa.sort.odontologia.SortGenerico;
import com.servinte.axioma.bl.facturacion.facade.FacturacionFacade;
import com.servinte.axioma.bl.manejoPaciente.facade.ManejoPacienteFacade;
import com.servinte.axioma.common.ErrorMessage;
import com.servinte.axioma.dto.facturacion.EntidadSubContratadaDto;
import com.servinte.axioma.dto.facturacion.GrupoServicioDto;
import com.servinte.axioma.dto.manejoPaciente.ArticuloAutorizadoCapitacionDto;
import com.servinte.axioma.dto.manejoPaciente.AutorizacionCapitacionDto;
import com.servinte.axioma.dto.manejoPaciente.DatosPacienteAutorizacionDto;
import com.servinte.axioma.dto.manejoPaciente.ServicioAutorizadoCapitacionDto;
import com.servinte.axioma.dto.ordenes.MedicamentoInsumoAutorizacionOrdenDto;
import com.servinte.axioma.dto.ordenes.OrdenAutorizacionDto;
import com.servinte.axioma.dto.ordenes.ServicioAutorizacionOrdenDto;
import com.servinte.axioma.fwk.exception.IPSException;
import com.servinte.axioma.fwk.exception.util.CODIGO_ERROR_NEGOCIO;
import com.servinte.axioma.generadorReporte.manejoPaciente.formatosCapitacion.formatosAutorizacionMedicamentosInsumos.GeneradorReporteFormatoCapitacionAutorArticulos;
import com.servinte.axioma.generadorReporte.manejoPaciente.formatosCapitacion.formatosAutorizacionServicios.GeneradorReporteFormatoCapitacionAutorservicio;
import com.servinte.axioma.mundo.fabrica.capitacion.CapitacionFabricaMundo;
import com.servinte.axioma.mundo.interfaz.capitacion.IUsuarioXConvenioMundo;
import com.servinte.axioma.orm.Articulo;
import com.servinte.axioma.orm.Contratos;
import com.servinte.axioma.orm.Convenios;
import com.servinte.axioma.orm.ViewFinalidadesServ;
import com.servinte.axioma.persistencia.UtilidadPersistencia;
import com.servinte.axioma.persistencia.UtilidadTransaccion;
import com.servinte.axioma.servicio.fabrica.ManejoPacienteServicioFabrica;
import com.servinte.axioma.servicio.fabrica.capitacion.CapitacionFabricaServicio;
import com.servinte.axioma.servicio.fabrica.facturacion.FacturacionServicioFabrica;
import com.servinte.axioma.servicio.fabrica.inventario.InventarioServicioFabrica;
import com.servinte.axioma.servicio.fabrica.odontologia.administracion.AdministracionFabricaServicio;
import com.servinte.axioma.servicio.interfaz.administracion.ICentroCostosServicio;
import com.servinte.axioma.servicio.interfaz.administracion.IPersonasServicio;
import com.servinte.axioma.servicio.interfaz.capitacion.IUsuarioXConvenioServicio;
import com.servinte.axioma.servicio.interfaz.capitacion.IUsuariosCapitadosServicio;
import com.servinte.axioma.servicio.interfaz.facturacion.IEntidadesSubcontratadasServicio;
import com.servinte.axioma.servicio.interfaz.facturacion.IEntregaMedicamentosInsumosEntSubcontratadasServicio;
import com.servinte.axioma.servicio.interfaz.facturacion.IViewFinalidadesServServicio;
import com.servinte.axioma.servicio.interfaz.facturacion.convenio.IConvenioServicio;
import com.servinte.axioma.servicio.interfaz.inventario.IArticulosServicio;
import com.servinte.axioma.servicio.interfaz.manejoPaciente.IAutorizacionIngresoEstanciaServicio;
import com.servinte.axioma.servicio.interfaz.manejoPaciente.IIngresosEstanciaServicio;
import com.servinte.axioma.servicio.interfaz.manejoPaciente.IIngresosServicio;
import com.servinte.axioma.servicio.interfaz.manejoPaciente.IPacienteServicio;
import com.servinte.axioma.servicio.interfaz.manejoPaciente.ITiposAfiliadoServicio;
import com.servinte.axioma.servicio.interfaz.manejoPaciente.IViasIngresoServicio;
import com.servinte.axioma.servicio.interfaz.tesoreria.ITiposIdentificacionServicio;



/**
 * @author Cristhian Murillo
 * Clase usada para controlar los procesos de la funcionalidad.
 */
@SuppressWarnings({"unchecked"})
public class AutorizacionesServiciosMedicamentosIngresoEstanciaAction extends Action 
{
	/** * Log */
	Logger logger = Logger.getLogger(AutorizacionesServiciosMedicamentosIngresoEstanciaAction.class);
	
	/** * Contiene la lista de mensajes correspondiente a esta forma */
	MessageResources fuenteMensaje = MessageResources.getMessageResources("com.servinte.mensajes.manejoPaciente.AutorizacionesServiciosMedicamentosIngresoEstanciaForm");
	
	/** * Servicios */
	IConvenioServicio convenioServicio 										= FacturacionServicioFabrica.crearConvenioServicio();
	ITiposAfiliadoServicio tiposAfiliadoServicio 							= ManejoPacienteServicioFabrica.crearTiposAfiliadoServicio();
	ITiposIdentificacionServicio tiposIdentificacionServicio				= AdministracionFabricaServicio.crearTiposIdentificacionServicio();
	IUsuariosCapitadosServicio usuariosCapitadosServicio					= CapitacionFabricaServicio.crearUsuariosCapitadosServicio();
	IAutorizacionIngresoEstanciaServicio autorizacionIngresoEstanciaServicio= ManejoPacienteServicioFabrica.crearAutorizacionIngresoEstanciaServicio();
	IEntregaMedicamentosInsumosEntSubcontratadasServicio entregaMedicamentosInsumosEntSubcontratadasServicio = FacturacionServicioFabrica.crearEntregaMedicamentosInsumosEntSubcontratadasServicio();
	IEntidadesSubcontratadasServicio entidadesSubcontratadasServicio 		= FacturacionServicioFabrica.crearEntidadesSubcontratadasServicio();
	IViasIngresoServicio viasIngresoServicio								= ManejoPacienteServicioFabrica.crearViasIngresoServicio();
	IPacienteServicio pacienteServicio 										= com.servinte.axioma.servicio.fabrica.administracion.AdministracionFabricaServicio.crearPacienteServicio();
	IPersonasServicio personasServicio										= com.servinte.axioma.servicio.fabrica.administracion.AdministracionFabricaServicio.crearPersonaServicio();
	IUsuarioXConvenioServicio usuarioXConvenioServicio 						= CapitacionFabricaServicio.crearUsuarioXConvenioServicio();
	IIngresosEstanciaServicio ingresosEstanciaServicio						= ManejoPacienteServicioFabrica.crearIngresosEstancia();
	ICentroCostosServicio centroCostosServicio								= AdministracionFabricaServicio.crearCentroCostosServicio();
	IIngresosServicio ingresosServicio										= ManejoPacienteServicioFabrica.crearIngresosServicio();
	IViewFinalidadesServServicio viewFinalidadesServicio					= FacturacionServicioFabrica.crearViewFinalidadesServServicio();
	IIngresosEstanciaServicio servicioIngresoEstancia 						= ManejoPacienteServicioFabrica.crearIngresosEstancia();
	IArticulosServicio servicioArticulo										= InventarioServicioFabrica.crearArticulosServicio();
	private static final String tipoAutorizacionServicio 					= String.valueOf(EnumTipoAutorizacion.SERVICIOS.getCodigo());
	private static final String tipoAutorizacionArticulo 					= String.valueOf(EnumTipoAutorizacion.MEDICAMENTOSINSUMOS.getCodigo());
	
	
	/**
	 * Método encargado de el flujo y control de la funcionalidad
	 * 
	 * @see org.apache.struts.action.Action#execute(ActionMapping, ActionForm, 
	 * HttpServletRequest, HttpServletResponse)
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )throws Exception
	{
		Connection con=null;
		try{
			if(form instanceof AutorizacionesServiciosMedicamentosIngresoEstanciaForm)
			{
				AutorizacionesServiciosMedicamentosIngresoEstanciaForm forma = (AutorizacionesServiciosMedicamentosIngresoEstanciaForm)form;
				String estado = forma.getEstado(); 

				Log4JManager.info("Estado: AutorizacionesServiciosMedicamentosIngresoEstanciaAction --> "+estado);

				UsuarioBasico usuario 	= (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
				PersonaBasica paciente	= (PersonaBasica)request.getSession().getAttribute("pacienteActivo");
				forma.setMostrarPopUpTipoOrden(false);

				try {

					if(estado.equals("empezar"))
					{
						forma.reset();
						ActionForward actionForward = new ActionForward();
						actionForward = validarEmpezar(mapping, request, usuario);

						if(actionForward != null){
							return actionForward;
						}
						else
						{
							con = UtilidadBD.abrirConexion();
							if(con == null)
							{
								request.setAttribute("CodigoDescripcionError","erros.problemasBd");
								return mapping.findForward("paginaError");
							}
							else
							{
								actionForward = accionValidarPaciente(con, paciente, request, mapping);
								forma.setPath(Utilidades.obtenerPathFuncionalidad(con,ConstantesBD.codigoFuncionalidadAutorizacionesCapitacionSubcontratada));
								forma.setPathAutorizar(Utilidades.obtenerPathFuncionalidad(con,ConstantesBD.codigoFuncionalidadAutorizacionesServiciosMedicamentosIngresoEstancia));
								UtilidadBD.closeConnection(con); 
								if(actionForward != null){
									return actionForward;
								}
							}
						}

						accionEmpezar(forma, usuario, paciente, request);
						return mapping.findForward("principal");
					}

					else if(estado.equals("ordenar"))
					{
						accionOrdenar(mapping, forma); 
						return mapping.findForward("principal");

					}

					else if(estado.equals("seleccionarTipoAutorizacion")){
						return accionSeleccionarTipoAutorizacion(mapping, forma);

					}

					else if(estado.equals("mostrarPopUpTipoOrden"))
					{
						forma.setMostrarPopUpTipoOrden(true);
						return mapping.findForward("principal");
					}


					else if(estado.equals("mostrarServiciosArticulos")){

						if(forma.getTipoAutorizacion().equals(tipoAutorizacionServicio)){
							/** C&aacute;lculo fecha de vencimiento segun par&aacute;metro   **/
							Date fechaActual 				= UtilidadFecha.getFechaActualTipoBD();
							String diasCalculoFechaVencimientoServ = "";
							diasCalculoFechaVencimientoServ = ValoresPorDefecto.getDiasCalcularFechaVencAutorizacionServicio(usuario.getCodigoInstitucionInt());

							if((!UtilidadTexto.isEmpty(diasCalculoFechaVencimientoServ)) && (!diasCalculoFechaVencimientoServ.equals("0"))){

								Date fechaVencimientoAutorizacion = UtilidadFecha.conversionFormatoFechaStringDate(
										UtilidadFecha.incrementarDiasAFecha(UtilidadFecha.conversionFormatoFechaAAp(
												fechaActual), Integer.parseInt(diasCalculoFechaVencimientoServ), false));

								forma.setFechaVencimiento(fechaVencimientoAutorizacion);
							}
							return mapping.findForward("detalleAutorizacionServicios");

						}else if(forma.getTipoAutorizacion().equals(tipoAutorizacionArticulo)) {
							/** C&aacute;lculo fecha de vencimiento segun par&aacute;metro   **/
							Date fechaActual 				= UtilidadFecha.getFechaActualTipoBD();
							String diasCalculoFechaVencimientoArt = ValoresPorDefecto.getDiasCalcularFechaVencAutorizacionArticulo(usuario.getCodigoInstitucionInt());

							if((!UtilidadTexto.isEmpty(diasCalculoFechaVencimientoArt)) && (!diasCalculoFechaVencimientoArt.equals("0"))){
								Date fechaVencimientoAutorizacion = UtilidadFecha.conversionFormatoFechaStringDate(
										UtilidadFecha.incrementarDiasAFecha(UtilidadFecha.conversionFormatoFechaAAp(
												fechaActual), Integer.parseInt(diasCalculoFechaVencimientoArt), false));

								forma.setFechaVencimiento(fechaVencimientoAutorizacion);
							}
							return mapping.findForward("detalleAutorizacionArticulos");

						}

					}
					else if(estado.equals("adicionarServiciosAuto")){
						/**Metodo para adicionar un objeto servicio a la lista. **/
						return adicionarServicioAutorizacion(mapping, request,
								forma, usuario);

					}
					else if(estado.equals("elimiarServicio")){

						/** Metodo para remover un objeto de la lista **/
						forma.getListaServiciosAutoHelper().remove(forma.getPosListaServicios());
						return mapping.findForward("detalleAutorizacionServicios");
					}
					else if(estado.equals("ingresarNuevoArticulo"))
					{
						ActionErrors errores 	= new ActionErrors();
						return this.accionIngresarArticulo(forma, mapping, usuario, request, errores);
					}
					else if(estado.equals("autorizarServMedIngresoEstancia")){

						ActionErrors errores	= new ActionErrors();
						errores = validarCamposRequeridos(forma, request, usuario);

						if(!errores.isEmpty()){
							saveErrors(request, errores);

						}else{

							validacionNivelAutorizacion(forma, usuario, paciente, request, con);
							
							if(forma.isRetornarDetalleArticulos()){
								return mapping.findForward("detalleAutorizacionArticulos");

							}else if(forma.isRetornarDetalleServicios()){
								return mapping.findForward("detalleAutorizacionServicios");

							}else if(!forma.isRetornarDetalleServicios() && !forma.isRetornarDetalleArticulos() && forma.getTipoAutorizacion().equals(tipoAutorizacionServicio)){
								return mapping.findForward("detalleAutorizacionServicios");

							}else if(!forma.isRetornarDetalleServicios() && !forma.isRetornarDetalleArticulos() && forma.getTipoAutorizacion().equals(tipoAutorizacionArticulo)){
								return mapping.findForward("detalleAutorizacionArticulos");	
							}
						}
						if(forma.getTipoAutorizacion().equals(tipoAutorizacionServicio)){
							return mapping.findForward("detalleAutorizacionServicios");

						}else if(forma.getTipoAutorizacion().equals(tipoAutorizacionArticulo)){
							return mapping.findForward("detalleAutorizacionArticulos");	
						}
					}
					else if(estado.equals("imprimirAutorizacion")){
						
						validarFormatoImpresion(mapping, forma,request, usuario);
						if(forma.isRetornarDetalleArticulos()){
							return mapping.findForward("detalleAutorizacionArticulos");

						}else if(forma.isRetornarDetalleServicios()){
							return mapping.findForward("detalleAutorizacionServicios");

						}else if(!forma.isRetornarDetalleServicios() && !forma.isRetornarDetalleArticulos() && forma.getTipoAutorizacion().equals(tipoAutorizacionServicio)){
							return mapping.findForward("detalleAutorizacionServicios");

						}else if(!forma.isRetornarDetalleServicios() && !forma.isRetornarDetalleArticulos() && forma.getTipoAutorizacion().equals(tipoAutorizacionArticulo)){
							return mapping.findForward("detalleAutorizacionArticulos");	
						}
					}
						
				} catch (Exception e) {
					UtilidadTransaccion.getTransaccion().rollback();
					Log4JManager.error("Error en las autorizaciones de servicios medicamentos de ingreso estancia", e);
				}
			}
		}catch (Exception e) {
			Log4JManager.error(e);
		}
		finally{
			UtilidadBD.closeConnection(con);
		}
		return null;
	}


	


	/**
	 * M&eacute;todo encargado de hacer llamado a la validaci&oacute;n de nivel
	 * de autorizaci&oacute;n para las autorizaciones
	 * @param forma
	 * @param usuario
	 * @param paciente
	 * @param request
	 * @param con
	 * @author Diana Carolina G
	 */
	private void validacionNivelAutorizacion(AutorizacionesServiciosMedicamentosIngresoEstanciaForm forma,
			UsuarioBasico usuario, PersonaBasica paciente, HttpServletRequest request, Connection con)throws IPSException
	{
		boolean esServicio=false;
		
		ActionMessages errores = new ActionMessages();
		ManejoPacienteFacade manejoPacienteFacade = new ManejoPacienteFacade();
		ServicioAutorizacionOrdenDto servicioAutorizar = null;
		OrdenAutorizacionDto ordenAutorizar = null;
		DatosPacienteAutorizacionDto datosPacienteAutorizar = null;
		AutorizacionCapitacionDto generacionAutorizacion = null;
		MedicamentoInsumoAutorizacionOrdenDto medicamentosAutorizar = null;
		EntidadSubContratadaDto entidadSubcontratada = null;
		
		try {
			con = UtilidadBD.abrirConexion();
			UtilidadTransaccion.getTransaccion().begin();
						
			ArrayList<DtoServiciosAutorizaciones> listaDtoServiciosAutorizacion = new ArrayList<DtoServiciosAutorizaciones>();
			ArrayList<DtoArticulosAutorizaciones> listaDtoArticulosAutorizacion = new ArrayList<DtoArticulosAutorizaciones>();
			
			if((forma.getListaServiciosAutoHelper()!=null) 
					&& (!forma.getListaServiciosAutoHelper().equals("")) 
					&& (forma.getListaServiciosAutoHelper().size()>0)){
				
				/** Conviertos datos de servicios seleccionados a  Arraylist de Dto**/
				listaDtoServiciosAutorizacion = convertirListServicios(forma);
				esServicio = true;
				
			}else if((forma.getNumeroFilasMapa()!= ConstantesBD.codigoNuncaValido) && (forma.getNumeroFilasMapa()>0)){
				
				/** Conviertos datos de articulos seleccionados a  Arraylist de Dto**/
				listaDtoArticulosAutorizacion = convertirMapaListArticulosAutorizados(forma);
				
			}
			//TODO
			//Se envia la información correspondiente al dto de AutorizacionCapitacionDto
			//Envio la información de la orden
			generacionAutorizacion = new AutorizacionCapitacionDto();
			ordenAutorizar = new OrdenAutorizacionDto();
	 		ordenAutorizar.setCodigoViaIngreso(forma.getDtoPacienteSeleccionado().getCodigoViaIngreso());
	 		ordenAutorizar.setEsPyp(false);
	 		ordenAutorizar.getContrato().getConvenio().setCodigo(forma.getDtoPacienteSeleccionado().getConvenio().getCodigo());
	 		ordenAutorizar.getContrato().getConvenio().setNombre(forma.getDtoPacienteSeleccionado().getConvenio().getNombre());
	 		ordenAutorizar.getContrato().setCodigo(forma.getDtoPacienteSeleccionado().getCodigoContrato());
			ordenAutorizar.getContrato().getConvenio().setConvenioManejaCapiSub(true);
			ordenAutorizar.setTipoEntidadEjecuta(forma.getDtoPacienteSeleccionado().getTipoEntidadEjecuta());
			if (forma.getDtoPacienteSeleccionado().getConvenio().getManejaPresupCapitacion() == ConstantesBD.acronimoSiChar){
				ordenAutorizar.getContrato().getConvenio().setConvenioManejaPresupuesto(true);
			}else{
				ordenAutorizar.getContrato().getConvenio().setConvenioManejaPresupuesto(false);
			}
			
	 		ordenAutorizar.setMigrado(ConstantesBD.acronimoNoChar);
	 		ordenAutorizar.setCodigoCentroCostoEjecuta(forma.getDtoPacienteSeleccionado().getCentroCostoRespondeIngreEstancia());
	 		
	 		if (esServicio){
	 			for (DtoServiciosAutorizaciones serviciosAutorizar : listaDtoServiciosAutorizacion) {
	 				//Envio el servicio a autorizar
	 	 			servicioAutorizar = new ServicioAutorizacionOrdenDto();
	 				servicioAutorizar.setCodigo(serviciosAutorizar.getCodigoServicio());
	 				servicioAutorizar.setAutorizar(true);
	 				servicioAutorizar.setAutorizado(true);
	 				servicioAutorizar.setCantidad((long) serviciosAutorizar.getCantidadSolicitada());
	 				servicioAutorizar.setUrgente(serviciosAutorizar.getUrgente());
	 				servicioAutorizar.setAcronimoTipoServicio(serviciosAutorizar.getTipoServicio());
	 				servicioAutorizar.setCodigoGrupoServicio(serviciosAutorizar.getCodigoGrupoServicio());
	 				servicioAutorizar.setCodigoEspecialidad(serviciosAutorizar.getEspecialidad());
	 				servicioAutorizar.setAcronimoTipoServicio(servicioAutorizar.getAcronimoTipoServicio());
	 				ordenAutorizar.getServiciosPorAutorizar().add(servicioAutorizar);
	 			}
	         	generacionAutorizacion.getOrdenesAutorizar().add(ordenAutorizar);
	         	forma.setListaServicios(listaDtoServiciosAutorizacion);
	         	
	 		}else{
	 			for (DtoArticulosAutorizaciones articulosAutorizar : listaDtoArticulosAutorizacion) {
					//Envio el medicamento a autorizar
					medicamentosAutorizar = new MedicamentoInsumoAutorizacionOrdenDto();
					medicamentosAutorizar.setCodigo(articulosAutorizar.getCodigoArticulo());
					medicamentosAutorizar.setCantidad((long)articulosAutorizar.getCantidadSolicitada());
					medicamentosAutorizar.setAutorizar(true);
					medicamentosAutorizar.setAutorizado(true);
					medicamentosAutorizar.setDiasTratamiento(articulosAutorizar.getDiasTratamientoFormulacion());
					medicamentosAutorizar.setDosis(articulosAutorizar.getDosisFormulacion());
					medicamentosAutorizar.setDescripcion(articulosAutorizar.getDescripcionArticulo());
					medicamentosAutorizar.setSubGrupoInventario(articulosAutorizar.getCodigoSubGrupoArticulo());
					medicamentosAutorizar.setAcronimoNaturaleza(articulosAutorizar.getAcronimoNaturalezaArticulo());
					ordenAutorizar.getMedicamentosInsumosPorAutorizar().add(medicamentosAutorizar);
				}
	 			generacionAutorizacion.getOrdenesAutorizar().add(ordenAutorizar);
	 			forma.setListaArticulos(listaDtoArticulosAutorizacion);
	 		}
	 		
	 		//Envio los datos del paciente para la autorización de capita
			datosPacienteAutorizar = new DatosPacienteAutorizacionDto();
			//Se obtiene la información de el tipo Afiliado, la clasificación Socioeconómica y Naturaleza del paciente
			datosPacienteAutorizar.setTipoAfiliado(forma.getDtoPacienteSeleccionado().getTipoAfiliado());
			if(forma.getDtoPacienteSeleccionado().getCodigoClasificacionSE()!= null){
				datosPacienteAutorizar.setClasificacionSocieconomica(forma.getDtoPacienteSeleccionado().getCodigoClasificacionSE());
			}
			datosPacienteAutorizar.setCuentaManejaMontos(false);
			datosPacienteAutorizar.setCodigoPaciente(forma.getDtoPacienteSeleccionado().getCodigo());
			
			//Envio la información correspondiente al dto del proceso de autorización
			generacionAutorizacion.setTipoAutorizacion(ConstantesIntegridadDominio.acronimoManual);
			generacionAutorizacion.setCodigoInstitucion(usuario.getCodigoInstitucionInt());
			generacionAutorizacion.setLoginUsuario(usuario.getLoginUsuario());
			generacionAutorizacion.setCentroAtencion(usuario.getCodigoCentroAtencion());
			generacionAutorizacion.setDatosPacienteAutorizar(datosPacienteAutorizar);
			generacionAutorizacion.setAutoServArtIngresoEstancia(true);
			generacionAutorizacion.setCodAutorIngresoEstancia(forma.getDtoPacienteSeleccionado().getCodIngresoEstancia());
			generacionAutorizacion.setFechaVencimientoAutorizacion(forma.getFechaVencimiento());
			
			//hermorhu - MT6146 
			//Enviar en AutorizacionCapitacionDto las Observaciones Generales de la Autorizacion
	 		generacionAutorizacion.setObservacionesGenerales(forma.getObservacionesGenerales());
	 		
			entidadSubcontratada = manejoPacienteFacade.verificarEntidadSubContratadaParametrizada(String.valueOf(forma.getDtoEntidadSubcontratada().getCodigoPk()));
			
			if (entidadSubcontratada!=null){
				generacionAutorizacion.setProcesoExitoso(true);
				generacionAutorizacion.setEntidadSubAutorizarCapitacion(entidadSubcontratada);
			}else{
				ErrorMessage error = new ErrorMessage("errors.autorizacion.contratoEntidadIngresoEstanci");
				generacionAutorizacion.setMensajeErrorGeneral(error);
				generacionAutorizacion.setProcesoExitoso(false);
				generacionAutorizacion.setVerificarDetalleError(false);
			}
			
			//Realizar las validaciones de los niveles de autorización DCU 1105
			if (generacionAutorizacion.isProcesoExitoso()){
				//Realizar la validación de los niveles 1105
				manejoPacienteFacade.validarNivelesAutorizacionAutorizacionesPoblacionCapitadaAutorServMedIngresoInstancia(generacionAutorizacion, false);
			}
			
			//Realizar el proceso de generación de autorización DCU 1106
			if (generacionAutorizacion.isProcesoExitoso()){
				manejoPacienteFacade.generarAutorizacion(generacionAutorizacion);
			}

			List<AutorizacionCapitacionDto> listaAutorizacionCapitacion = new ArrayList<AutorizacionCapitacionDto>();
			listaAutorizacionCapitacion.add(generacionAutorizacion);
			manejoPacienteFacade.obtenerMensajesError(listaAutorizacionCapitacion, errores);

			if(generacionAutorizacion.isProcesoExitoso()){
				forma.setMostrarImprimirAutorizacion(true);
				this.generarReporteAutorizacionFormatoCapitacion(forma, con, paciente, usuario, request, generacionAutorizacion);
			}
			
			
			
			UtilidadTransaccion.getTransaccion().commit();
			
		}catch (IPSException ipse) {
			UtilidadTransaccion.getTransaccion().rollback();
			Log4JManager.warning(ipse.getMessage(),ipse);
			errores.add("", new ActionMessage(ipse.getErrorCode().toString()));
		}
		catch (Exception e){
			UtilidadTransaccion.getTransaccion().rollback();
			Log4JManager.error(e.getMessage(),e);
			errores.add("", new ActionMessage(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO+""));
		}
		finally{
			if(!errores.isEmpty()){
				saveErrors(request, errores);
			}
		}
	}

	
	/**
	 * Este m&eacute;todo se encarga de generar el formato Versalles
	 * para las autorizaciones generadas de servicios o art&iacute;culos
	 * 
	 * @param AutorizacionesServiciosMedicamentosIngresoEstanciaForm forma, Connection con,
			PersonaBasica paciente, UsuarioBasico usuarioSesion,
			HttpServletRequest request, 
			DTOValidacionNivelAutoAutomatica dtoValidacionNivelAutorizacion,
			DTOProcesoAutorizacion dtoProcesoAutorizacion
	 *
	 *@author Diana Carolina G
	 * @throws Exception 
	 */
	private void generarReporteAutorizacionFormatoCapitacion(AutorizacionesServiciosMedicamentosIngresoEstanciaForm forma, Connection con,
			PersonaBasica paciente, UsuarioBasico usuarioSesion, HttpServletRequest request,
			AutorizacionCapitacionDto generacionAutorizacion)throws Exception
	{
		try{
			String nombreReporte="AUTORIZACION SERVICIOS MEDICAMENTOS INGRESO ESTANCIA";
			String tipoContrato ="Capitado";
			String nombreArchivo ="";
			
			DtoGeneralReporteServiciosAutorizados dtoReporteServicios = new DtoGeneralReporteServiciosAutorizados();
			DtoGeneralReporteArticulosAutorizados dtoReporteArticulos = new DtoGeneralReporteArticulosAutorizados();
			forma.setFormatoImpresionDefinido(true);		
			ArrayList<String> listaNombresReportes = new ArrayList<String>();
			DTOReporteAutorizacionSeccionPaciente dtoPaciente = new DTOReporteAutorizacionSeccionPaciente();
			DTOReporteAutorizacionSeccionAutorizacion dtoAutorizacion = new DTOReporteAutorizacionSeccionAutorizacion();
			ManejoPacienteFacade manejoPacienteFacade = new ManejoPacienteFacade();
			Long idAutorizacionEntSub = null;
			
			//**********Datos Comunes para todas las autorizaciones que llegan
		    InstitucionBasica institucion = (
		    		InstitucionBasica)request.getSession().getAttribute("institucionBasica");
		    
			String nombrePaciente = paciente.getPrimerNombre() + " " + 
				(UtilidadTexto.isEmpty(paciente.getSegundoNombre())?"":paciente.getSegundoNombre())
				+ " " + paciente.getPrimerApellido()+" " + 
				(UtilidadTexto.isEmpty(paciente.getSegundoApellido())?"":paciente.getSegundoApellido());
				 			     			
			String formatoReporte = ValoresPorDefecto.getImpresionMediaCarta(
					usuarioSesion.getCodigoInstitucionInt());
			
			String infoParametroGeneral = ValoresPorDefecto.getEncFormatoImpresionAutorEntidadSub(
					usuarioSesion.getCodigoInstitucionInt());
			
			String infoPiePagina=ValoresPorDefecto.getPiePagFormatoImpresionAutorEntidadSub(
					usuarioSesion.getCodigoInstitucionInt());
			
			if(UtilidadTexto.isEmpty(formatoReporte)){
				formatoReporte=ConstantesBD.acronimoNo;
			}
			
			dtoPaciente.setNombrePaciente(nombrePaciente);
			dtoPaciente.setTipoDocPaciente(paciente.getCodigoTipoIdentificacionPersona());
			dtoPaciente.setNumeroDocPaciente(paciente.getNumeroIdentificacionPersona());
			dtoPaciente.setTipoContratoPaciente(tipoContrato);
			dtoPaciente.setTipoAfiliado(forma.getDtoPacienteSeleccionado().getTipoAfiliado());
			String fechaNaciemiento=UtilidadFecha.conversionFormatoFechaAAp(forma.getDtoPacienteSeleccionado().getFechaNacimiento());
			dtoPaciente.setEdadPaciente(String.valueOf(UtilidadFecha.calcularEdad(fechaNaciemiento)));
			dtoPaciente.setCategoriaSocioEconomica(forma.getDtoPacienteSeleccionado().getClasificacionSocioEconomica());
			dtoPaciente.setRecobro(ConstantesBD.acronimoNo);
			
			dtoAutorizacion.setEntidadSub(generacionAutorizacion.getEntidadSubAutorizarCapitacion().getRazonSocial());
			dtoAutorizacion.setDireccionEntidadSub(generacionAutorizacion.getEntidadSubAutorizarCapitacion().getDireccionEntidad());
			dtoAutorizacion.setTelefonoEntidadSub(generacionAutorizacion.getEntidadSubAutorizarCapitacion().getTelefonoEntidad());
			dtoAutorizacion.setEntidadAutoriza(usuarioSesion.getInstitucion());
			dtoAutorizacion.setUsuarioAutoriza(usuarioSesion.getLoginUsuario());
			dtoAutorizacion.setObservaciones(forma.getObservacionesGenerales());
			
			dtoAutorizacion.setFechaAutorizacion(UtilidadFecha.conversionFormatoFechaAAp(generacionAutorizacion.getFechaAutorizacion()));
			dtoAutorizacion.setFechaVencimiento(UtilidadFecha.conversionFormatoFechaAAp(generacionAutorizacion.getFechaVencimientoAutorizacion()));
			dtoAutorizacion.setNumeroAutorizacion(generacionAutorizacion.getConsecutivoAutorizacion());
			if(!UtilidadTexto.isEmpty(generacionAutorizacion.getEstadoAutorizacion())){
				String estado = (String)ValoresPorDefecto.getIntegridadDominio(
						generacionAutorizacion.getEstadoAutorizacion());	 			     					
				dtoAutorizacion.setEstadoAutorizacion(estado);	 
			}
			
			for(OrdenAutorizacionDto orden: generacionAutorizacion.getOrdenesAutorizar()){
				dtoPaciente.setConvenioPaciente(orden.getContrato().getConvenio().getNombre());
			}
			
			idAutorizacionEntSub = manejoPacienteFacade.consultarIdAutorizacionEntidadSubXConsecutivoAutorizacion(generacionAutorizacion.getConsecutivoAutorizacion());
			
			//----------------------------------------------------------------------------------
			if(forma.getListaServicios() != null && !forma.getListaServicios().isEmpty()){	
				DtoServiciosAutorizaciones dtoServi=new DtoServiciosAutorizaciones(); 
				dtoServi=forma.getListaServicios().get(0);
				dtoServi.setConsecutivoOrdenMed(dtoServi.getNumeroOrden());
				dtoServi.setCodigoPropietario(dtoServi.getCodigoServicio()+"");
				dtoServi.setFechaOrden(UtilidadFecha.conversionFormatoFechaStringDate(dtoAutorizacion.getFechaAutorizacion()));
				dtoServi.setConsecutivoOrdenMed(null);
				if(!UtilidadTexto.isEmpty(dtoServi.getDiagnosticoIngEst())&&!UtilidadTexto.isEmpty(dtoServi.getTipoCieDxIngEst()))
					dtoServi.setDiagnostico(dtoServi.getDiagnosticoIngEst()+"-"+dtoServi.getTipoCieDxIngEst());
				
				dtoReporteServicios.setDtoPaciente(dtoPaciente);
				dtoReporteServicios.setDatosEncabezado(infoParametroGeneral);
				dtoReporteServicios.setRazonSocial(institucion.getRazonSocial());
				dtoReporteServicios.setNit("NIT - "+institucion.getNit());
				dtoReporteServicios.setDireccion("Dir:  "+institucion.getDireccion() +"  -  Tel: "+institucion.getTelefono());
				dtoReporteServicios.setActividadEconomica(institucion.getActividadEconomica());
				dtoReporteServicios.setUsuario(usuarioSesion.getNombreUsuario()+" ("+usuarioSesion.getLoginUsuario()+")");
				dtoReporteServicios.setObservaciones(forma.getObservacionesGenerales());
				dtoReporteServicios.setDatosPie(infoPiePagina);
				dtoReporteServicios.setTipoReporteMediaCarta(formatoReporte);
				dtoReporteServicios.setRutaLogo(institucion.getLogoJsp());
				dtoReporteServicios.setUbicacionLogo(institucion.getUbicacionLogo());
				dtoReporteServicios.setDtoAutorizacion(dtoAutorizacion);
				
				//hermorhu - MT6207 
				//Se cargan para la impresion solo los servicios que fueron autorizados
				String tipoTarifario = ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(usuarioSesion.getCodigoInstitucionInt());
				if(UtilidadTexto.isEmpty(tipoTarifario)) {
					tipoTarifario=ConstantesBD.codigoTarifarioCups+"";
				}
				
				List<ServicioAutorizadoCapitacionDto> listaServiciosAutorizados = manejoPacienteFacade.consultarServiciosAutorizadosCapitacion(idAutorizacionEntSub, Long.parseLong(tipoTarifario), true);
				
				List<DtoServiciosAutorizaciones> listaServiciosImprimir = new ArrayList<DtoServiciosAutorizaciones>();
				
				for(ServicioAutorizadoCapitacionDto servicioAutorizado : listaServiciosAutorizados) {
					for(DtoServiciosAutorizaciones servicioOrdenado : forma.getListaServicios()) {
						if(servicioAutorizado.getIdServicio() == servicioOrdenado.getCodigoServicio().longValue()) {
							listaServiciosImprimir.add(servicioOrdenado);
						}
					}	
				}
				
				dtoReporteServicios.setListaServicios((ArrayList<DtoServiciosAutorizaciones>)listaServiciosImprimir);

				
				GeneradorReporteFormatoCapitacionAutorservicio generadorReporteServicios = 
					new GeneradorReporteFormatoCapitacionAutorservicio(dtoReporteServicios);
				JasperPrint reporte = generadorReporteServicios.generarReporte();
				nombreArchivo = generadorReporteServicios.exportarReportePDF(reporte, nombreReporte);
			}
			else if(forma.getListaArticulos() != null && !forma.getListaArticulos().isEmpty()){
				for (DtoArticulosAutorizaciones articulo : forma.getListaArticulos()) {
					articulo.setNumeroOrden(null);
					articulo.setFechaOrden(UtilidadFecha.conversionFormatoFechaStringDate(dtoAutorizacion.getFechaAutorizacion()));
					if(!UtilidadTexto.isEmpty(articulo.getDiagnosticoIngEst())&&!UtilidadTexto.isEmpty(articulo.getTipoCieDxIngEst()))
						articulo.setDiagnostico(articulo.getDiagnosticoIngEst()+"-"+articulo.getTipoCieDxIngEst());
				}
				dtoReporteArticulos.setDtoPaciente(dtoPaciente);
				dtoReporteArticulos.setDatosEncabezado(infoParametroGeneral);
				dtoReporteArticulos.setRazonSocial(institucion.getRazonSocial());
				dtoReporteArticulos.setNit("NIT - "+institucion.getNit());
				dtoReporteArticulos.setDireccion("Dir:  "+institucion.getDireccion() +"  -  Tel: "+institucion.getTelefono());
				dtoReporteArticulos.setActividadEconomica(institucion.getActividadEconomica());
				dtoReporteArticulos.setUsuario(usuarioSesion.getNombreUsuario()+" ("+usuarioSesion.getLoginUsuario()+")");
				dtoReporteArticulos.setObservaciones(forma.getObservacionesGenerales());
				dtoReporteArticulos.setDatosPie(infoPiePagina);
				dtoReporteArticulos.setTipoReporteMediaCarta(formatoReporte);
				dtoReporteArticulos.setRutaLogo(institucion.getLogoJsp());
				dtoReporteArticulos.setUbicacionLogo(institucion.getUbicacionLogo());
				dtoReporteArticulos.setDtoAutorizacion(dtoAutorizacion);
				
				//hermorhu - MT6207 
				//Se cargan para la impresion solo los articulos que fueron autorizados
				List<ArticuloAutorizadoCapitacionDto> listaArticulosAutorizados = manejoPacienteFacade.consultarArticulosAutorizadosCapitacion(idAutorizacionEntSub, usuarioSesion.getCodigoInstitucionInt(),true);
				
				List<DtoArticulosAutorizaciones> listaArticulosImprimir = new ArrayList<DtoArticulosAutorizaciones>();
				
				for(ArticuloAutorizadoCapitacionDto articuloAutorizado : listaArticulosAutorizados) {
					for(DtoArticulosAutorizaciones articuloOrdenado : forma.getListaArticulos()) {
						if(articuloAutorizado.getCodArt() == articuloOrdenado.getCodigoArticulo().longValue()) {
							listaArticulosImprimir.add(articuloOrdenado);
						}
					}
				}
				
				dtoReporteArticulos.setListaArticulos((ArrayList<DtoArticulosAutorizaciones>)listaArticulosImprimir);
				
				
				GeneradorReporteFormatoCapitacionAutorArticulos generadorReporteArticulos = 
					new GeneradorReporteFormatoCapitacionAutorArticulos(dtoReporteArticulos);
				JasperPrint reporte = generadorReporteArticulos.generarReporte();
				nombreArchivo = generadorReporteArticulos.exportarReportePDF(reporte, nombreReporte);
			}
			listaNombresReportes.add(nombreArchivo);
			forma.setNombreArchivoGenerado(nombreArchivo);
			
			dtoAutorizacion.setObservaciones("");
			
			if(listaNombresReportes!=null && listaNombresReportes.size()>0){
				
				forma.setListaNombresReportes(listaNombresReportes);
				forma.setMostrarImprimirAutorizacion(true);
			}
		}
		catch (Exception e) {
			Log4JManager.warning(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO,e);
		}
	}
	
	/**
	 * M&eacute;todo encargado de obtener datos de presentaci&oacute;n
	 * al momento de seleccionar un tipo de autorizaci&oacute;n 
	 * @param mapping
	 * @param forma
	 * @return ActionForward
	 * @author Diana Carolina G
	 */
	private ActionForward accionSeleccionarTipoAutorizacion(
			ActionMapping mapping,
			AutorizacionesServiciosMedicamentosIngresoEstanciaForm forma) {
		
		UtilidadTransaccion.getTransaccion().begin();
		
		Log4JManager.info("");
		
		try{
			DTOAdministracionAutorizacion dtoAutorizacionIngresoEstancia= forma.getListaAutorizacionesIngresoEstancia().get(forma.getPostListAutorizaciones());
			long codigoIngresoEstancia = ConstantesBD.codigoNuncaValidoLong;
			DTOPacienteCapitado dtoPacienteCapitado = new DTOPacienteCapitado();
			codigoIngresoEstancia 	= dtoAutorizacionIngresoEstancia.getCodIngresoEstancia();
			Convenios convenio  	= new Convenios();
			DtoEntidadSubcontratada dtoEntidad = new DtoEntidadSubcontratada();
			dtoPacienteCapitado=pacienteServicio.buscarPacienteAutorizacionIngresoEstancia(codigoIngresoEstancia);
			
			Integer codigoContratoConvenioValido=0;
			if(forma.getDtoPacienteSeleccionado().getCodigoContrato()!=null && forma.getDtoPacienteSeleccionado().getCodigoContrato()>0)
				codigoContratoConvenioValido=forma.getDtoPacienteSeleccionado().getCodigoContrato();
			
			/** Datos Persona**/
			dtoPacienteCapitado.setPrimerNombre(dtoPacienteCapitado.getPrimerNombre());
			dtoPacienteCapitado.setPrimerApellido(dtoPacienteCapitado.getPrimerApellido());
			dtoPacienteCapitado.setSegundoNombre(dtoPacienteCapitado.getSegundoNombre());
			dtoPacienteCapitado.setSegundoApellido(dtoPacienteCapitado.getSegundoApellido());
			dtoPacienteCapitado.setTipoIdentificacion(dtoPacienteCapitado.getTipoId());
			dtoPacienteCapitado.setNumeroIdentificacion(dtoPacienteCapitado.getNumeroIdentificacion());
			dtoPacienteCapitado.setFechaNacimiento(dtoPacienteCapitado.getFechaNacimientoTipoDate()+"");
			dtoPacienteCapitado.setCodigoContrato(codigoContratoConvenioValido);
			
			/** Datos Paciente**/
			convenio.setNombre(forma.getDtoPacienteSeleccionado().getConvenio().getNombre());
			convenio.setCodigo(forma.getDtoPacienteSeleccionado().getConvenio().getCodigo());
			convenio.setManejaPresupCapitacion(forma.getConvenioManejaPresupuesto());
			dtoPacienteCapitado.setConvenio(convenio);
			//Se toma de la Admisión asociada a la Autorización de Ingreso / Estancia. (Tipo Afiliado y Clasificacion SE)
			dtoPacienteCapitado.setTipoAfiliado(dtoAutorizacionIngresoEstancia.getTipoAfiliado());
			dtoPacienteCapitado.setClasificacionSocioEconomica(dtoAutorizacionIngresoEstancia.getClasificacionSE());
			forma.setDtoPacienteSeleccionado(dtoPacienteCapitado);
			
			/** Datos entidad Subcontratada*/
			dtoEntidad.setCodigoPk(dtoPacienteCapitado.getCodigoPkEntidadSubcontratada());
			dtoEntidad.setRazonSocial(dtoAutorizacionIngresoEstancia.getEntidadSubcontratada().getRazonSocial());
			/*dtoEntidad.setDescripcionEntidad((dtoPacienteCapitado.getDescripcionEntidadSubOtra()));
			dtoEntidad.setDireccionotra(dtoPacienteCapitado.getDireccionEntidadSubOtra());
			dtoEntidad.setTelefonootra(dtoPacienteCapitado.getTelefonoEntidadSubOtra());*/
						
			
			forma.setDtoEntidadSubcontratada(dtoEntidad);
			
			forma.resetMapaArticulos();
			
			UtilidadTransaccion.getTransaccion().commit();

		}catch (Exception e){
			UtilidadTransaccion.getTransaccion().rollback();
			Log4JManager.warning("Error obteniendo datos del paciente Ingreso estancia: ", e);
		}
		return mapping.findForward("popUpTipoAutorizaciones");
	}

	
	/**
	 * M&eacute;todo encargado de realizar la 
	 * validaci&oacute;n de cobertura para los servicios 
	 * al momento de cargarlo en la jsp
	 * @param con
	 * @param forma
	 * @param mapping
	 * @return ActionForward
	 * @author Diana Carolina G
	 */
	private ActionForward adicionarServicioAutorizacion(ActionMapping mapping,
			HttpServletRequest request,
			AutorizacionesServiciosMedicamentosIngresoEstanciaForm forma,
			UsuarioBasico usuario) throws IPSException {
		
		ActionErrors errores 							= new ActionErrors();
		InfoCobertura cobertura 						= null;
		Connection con 									= UtilidadBD.abrirConexion();
		
		long codigoPkEntidad 			= forma.getDtoPacienteSeleccionado().getCodigoPkEntidadSubcontratada();
		int codViaIngreso 				= forma.getDtoPacienteSeleccionado().getCodigoViaIngreso();
		int codigoNaturalezaPaciente 	= ConstantesBD.codigoNuncaValido;
		String tipoPaciente				= null;
		String fecha 					= UtilidadFecha.getFechaActual();
		int codigoServicio 				= new Integer(forma.getDtoServicioHelper().getCodigoAxiomaJS());
		int codigoEspecialidad 			= new Integer(forma.getDtoServicioHelper().getCodigoEspecialidadJS());
		String acronimoTipoServicio     = forma.getDtoServicioHelper().getTipoServicioJS();
		String descripcionServicio 		= forma.getDtoServicioHelper().getDescripcionCupsJS();
		String esPos					= forma.getDtoServicioHelper().getEsPosJS();
		
		/**Valido si el articulo esta cubierto por la Entidad Subcontratada
		 * se hace llamado a Validacion Cobertura Entidades Subcontratadas 800 Servicios**/ 
		cobertura=Cobertura.validacionCoberturaServicioEntidadSubDadaEntidad(con, 
																			String.valueOf(codigoPkEntidad), 
																			codViaIngreso, 
																			tipoPaciente, 
																			codigoServicio, 
																			codigoNaturalezaPaciente, 
																			usuario.getCodigoInstitucionInt(), 
																			fecha);
		

		UtilidadBD.closeConnection(con);
		
		if(cobertura.existe()){
			DtoServicioAutoHelper dtoServicioHelper= new DtoServicioAutoHelper(); 
			dtoServicioHelper.setCodigoAxiomaJS(codigoServicio+"");
			dtoServicioHelper.setDescripcionCupsJS(descripcionServicio);
			dtoServicioHelper.setCodigoEspecialidadJS(codigoEspecialidad+"");
			dtoServicioHelper.setTipoServicioJS(acronimoTipoServicio);
			FacturacionFacade facturacionFacade = new FacturacionFacade();
			try {
				GrupoServicioDto grupoServicio= facturacionFacade.consultaGrupoServicioxServicio(codigoServicio);
				dtoServicioHelper.setCodigoGrupoServicio(grupoServicio.getCodigo()+"");
				dtoServicioHelper.setNombreGrupoServicio(grupoServicio.getDescripcion());
			} catch (IPSException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			dtoServicioHelper.setEsPosJS(esPos);
			forma.setDtoServicioHelper(dtoServicioHelper);
			
			/**
			 * Adiciono datos servicio a jsp detalleAutorizacionServicios
			 */
			List<DtoViewFinalidadesServ> listDtoHelper = accionCargarListaFinalidadesxServicio(forma);
			forma.getDtoServicioHelper().setListaFinalidades(listDtoHelper);
			forma.getListaServiciosAutoHelper().add(forma.getDtoServicioHelper());
			forma.setDtoServicioHelper(new DtoServicioAutoHelper());
			return mapping.findForward("detalleAutorizacionServicios");
			
			
		}else{
			  /**No muestro servicio, muestro mensaje de error **/
			  String mensajeConcreto = fuenteMensaje.getMessage("AutorizacionesServiciosMedicamentosIngresoEstanciaForm.servicioNoCubierto", codigoServicio);
			  errores = retornarErrorEnviado(forma, request, mensajeConcreto, errores);
			  return mapping.findForward("detalleAutorizacionServicios");
		}
	}



	/**
	 * M&eacute;todo encargado de realizar la 
	 * validaci&oacute;n de cobertura para los art&iacute;culos 
	 * al momento de cargarlo en la jsp
	 * @param con
	 * @param forma
	 * @param mapping
	 * @return ActionForward
	 * @author Diana Carolina G
	 */
	private ActionForward accionIngresarArticulo(AutorizacionesServiciosMedicamentosIngresoEstanciaForm forma, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request, ActionErrors errores) throws IPSException 
	{
		
		
		
		Articulo articulo;
		articulo = new Articulo();
		InfoCobertura cobertura =null;
		int codArticuloSeleccionado=ConstantesBD.codigoNuncaValido;
		ArrayList<DtoArticulosAutorizaciones> listaArticulosAutorizacion = new ArrayList<DtoArticulosAutorizaciones>();
		DtoArticulosAutorizaciones dtoArticulosAutorizaciones= new DtoArticulosAutorizaciones();
		Connection con = UtilidadBD.abrirConexion();
		
		long codigoPkEntidad 		 = forma.getDtoPacienteSeleccionado().getCodigoPkEntidadSubcontratada();
		int codViaIngreso 			 = forma.getDtoPacienteSeleccionado().getCodigoViaIngreso();
		int codigoNaturalezaPaciente = ConstantesBD.codigoNuncaValido;
		String tipoPaciente			 = null;
		String fecha				 = UtilidadFecha.getFechaActual();
		
		/**Obtengo el codigo seleccionado desde el pop up de la busqueda generica de articulos **/
		codArticuloSeleccionado= forma.getCodigoArticuloSeleccionado();
		
		if(codArticuloSeleccionado != ConstantesBD.codigoNuncaValido ){
			/** Obtengo toda la informacion del articulo existente en BD **/ 
			UtilidadTransaccion.getTransaccion().begin();
			articulo = servicioArticulo.obtenerArticuloPorId(codArticuloSeleccionado);
			dtoArticulosAutorizaciones.setCodigoArticulo(articulo.getCodigo());
			dtoArticulosAutorizaciones.setCodigoSubGrupoArticulo(articulo.getSubgrupo());
			if(articulo.getNaturalezaArticulo()!=null && articulo.getNaturalezaArticulo().getId()!=null){
				dtoArticulosAutorizaciones.setAcronimoNaturalezaArticulo(articulo.getNaturalezaArticulo().getId().getAcronimo());
			}
			forma.setNaturalezaArticuloConsultada(articulo.getNaturalezaArticulo().getNombre());
			forma.setAcronimoUnidadMedidaArticulo(articulo.getUnidadMedida().getAcronimo());
			
			
			/**Valido si el articulo esta cubierto por la Entidad Subcontratada
			 * se hace llamado a Validacion Cobertura Entidades Subcontratadas 800 Articulos**/ 
			cobertura=Cobertura.validacionCoberturaArticuloEntidadSubDadaEntidad(con, // connection 
																				String.valueOf(codigoPkEntidad), 
																				codViaIngreso,
																				tipoPaciente, 
																				articulo.getCodigo(), 
																				codigoNaturalezaPaciente, 
																				usuario.getCodigoInstitucionInt(), 
																				fecha);
			UtilidadTransaccion.getTransaccion().commit();
			UtilidadBD.closeConnection(con);
		}
		
		
		if(cobertura.existe()){
			/**Adiciono el articulo a la lista de articulos para autorizar **/
			listaArticulosAutorizacion.add(dtoArticulosAutorizaciones);
			forma.setDtoArticulosHelper(dtoArticulosAutorizaciones);
			forma.setArticulos("numRegistros", forma.getNumeroFilasMapa()+"");
			forma.setCriterioBusqueda("");
			forma.setCriterioBusquedaInsumo("");
			return mapping.findForward("detalleAutorizacionArticulos");
			
		}else{
			  /**No muestro articulo, muestro mensaje de error **/
			  eliminarMapaArticulos(forma, forma.getNumeroFilasMapa()-1);
			  String mensajeConcreto = fuenteMensaje.getMessage("AutorizacionesServiciosMedicamentosIngresoEstanciaForm.articuloNoCubierto", articulo.getDescripcion());
			  errores = retornarErrorEnviado(forma, request, mensajeConcreto, errores);
			  return mapping.findForward("detalleAutorizacionArticulos");
		}
		
	}
	
	/**
	 * M&eacute;todo encargado de Eliminar Mapa de Art&iacute;culos
	 * @param forma
	 * @param posicion
	 * @author Diana Carolina G
	 */
	private void eliminarMapaArticulos(AutorizacionesServiciosMedicamentosIngresoEstanciaForm forma, int posicion){
		forma.getArticulos().put("fueEliminadoArticulo_"+posicion, "true");
	}
	
	
	/**
	 * Convertir Mapa lista Articulos Autorizados
	 * @param forma
	 * @return ArrayList<DtoArticulosAutorizaciones>
	 * @author Diana Carolina G
	 */
	private ArrayList<DtoArticulosAutorizaciones> convertirMapaListArticulosAutorizados(AutorizacionesServiciosMedicamentosIngresoEstanciaForm forma){
	
	
		ArrayList<DtoArticulosAutorizaciones> listaArticulos = new ArrayList<DtoArticulosAutorizaciones>();
		String tipoFrecuenciaValidar 						 = "";
		String frecuenciaValidar 							 = "";
		String viaValidar      								 = "";
		String tratamientoValidar							 = "";
		
		for(int i=0; i<forma.getNumeroFilasMapa(); i++){
			
			DtoArticulosAutorizaciones dtoArticulosAutorizaciones = new DtoArticulosAutorizaciones();
			
			forma.setFueEliminadoArticulo(forma.getArticulos().get("fueEliminadoArticulo_"+i).toString());
			
			if(!forma.getFueEliminadoArticulo().equals("true")){
				dtoArticulosAutorizaciones.setCodigoArticulo(new Integer(forma.getArticulos().get("articulo_"+i).toString())); //sacar descripcion, naturaleza, cantidad
				dtoArticulosAutorizaciones.setDescripcionArticulo(forma.getArticulos().get("descripcionArticulo_"+i).toString());
				dtoArticulosAutorizaciones.setAcronimoNaturalezaArticulo(forma.getDtoArticulosHelper().getAcronimoNaturalezaArticulo());
				dtoArticulosAutorizaciones.setCodigoSubGrupoArticulo(forma.getDtoArticulosHelper().getCodigoSubGrupoArticulo());
				dtoArticulosAutorizaciones.setAcronimoUnidadMedidaArticulo(forma.getAcronimoUnidadMedidaArticulo());
				dtoArticulosAutorizaciones.setUnidadMedidaArticulo(forma.getArticulos().get("unidadMedidaArticulo_"+i).toString());
				dtoArticulosAutorizaciones.setDosisFormulacion(forma.getArticulos().get("dosis_"+i).toString());
				dtoArticulosAutorizaciones.setCantidadSolicitada(new Integer(forma.getArticulos().get("cantidad_"+i).toString()));
				dtoArticulosAutorizaciones.setDiagnosticoIngEst(forma.getDtoPacienteSeleccionado().getDiagnosticoIngEst());
				dtoArticulosAutorizaciones.setTipoCieDxIngEst(forma.getDtoPacienteSeleccionado().getTipoCieDxIngEst());
				dtoArticulosAutorizaciones.setDescripcionDiagnosticoIngEst(forma.getDtoPacienteSeleccionado().getDescripcionDiagnosticoIngEst());
				forma.setEsMedicamentoString(forma.getArticulos().get("medicamento_"+i).toString());
				
				if(forma.getEsMedicamentoString().equals("true")){
					
					tipoFrecuenciaValidar = forma.getArticulos().get("tipofrecuencia_"+i).toString();
					frecuenciaValidar	  = forma.getArticulos().get("frecuencia_"+i).toString();
					viaValidar 			  = forma.getArticulos().get("via_"+i).toString();
					tratamientoValidar    = forma.getArticulos().get("duraciontratamiento_"+i).toString();
					
					if(!UtilidadTexto.isEmpty(tipoFrecuenciaValidar)){
						if(tipoFrecuenciaValidar.equals(ConstantesBD.codigoTipoFrecuenciaDias))
							dtoArticulosAutorizaciones.setTipoFrecuenciaFormulacion(ConstantesBD.nombreTipoFrecuenciaDias);
						else if(tipoFrecuenciaValidar.equals(ConstantesBD.codigoTipoFrecuenciaHoras))
							dtoArticulosAutorizaciones.setTipoFrecuenciaFormulacion(ConstantesBD.nombreTipoFrecuenciaHoras);
						else
							dtoArticulosAutorizaciones.setTipoFrecuenciaFormulacion(ConstantesBD.nombreTipoFrecuenciaMinutos);						
					}
					
					if(!UtilidadTexto.isEmpty(frecuenciaValidar)){
						dtoArticulosAutorizaciones.setFrecuenciaFormulacion(new Integer(forma.getArticulos().get("frecuencia_"+i).toString()));
					}
					
					if(!UtilidadTexto.isEmpty(viaValidar)){
						dtoArticulosAutorizaciones.setViaFormulacion(forma.getArticulos().get("via_"+i).toString());
					}
					
					if(!UtilidadTexto.isEmpty(tratamientoValidar)){
						dtoArticulosAutorizaciones.setDiasTratamientoFormulacion(new Long(forma.getArticulos().get("duraciontratamiento_"+i).toString()));
					}
					
					dtoArticulosAutorizaciones.setEsMedicamento(ConstantesBD.acronimoSiChar);
					
				}else{
					dtoArticulosAutorizaciones.setEsMedicamento(ConstantesBD.acronimoNoChar);
				}
				
				dtoArticulosAutorizaciones.setNaturalezaArticulo(forma.getNaturalezaArticuloConsultada());
				
				listaArticulos.add(dtoArticulosAutorizaciones);
				
			}
			
		}
		
		return listaArticulos;
		
	}
	
	
	/**
	 * M&eacute;todo encargado de convertir la lista
	 * de servicios seleccionados en Dto para realizar el
	 * proceso respectivo de validaciones
	 * @param forma
	 * @return ArrayList<DtoServiciosAutorizaciones>
	 * @author Diana Carolina G
	 * 
	 */
	private ArrayList<DtoServiciosAutorizaciones> convertirListServicios(AutorizacionesServiciosMedicamentosIngresoEstanciaForm forma){
	
		ArrayList<DtoServiciosAutorizaciones> listaServicios = new ArrayList<DtoServiciosAutorizaciones>();
		
		for( DtoServicioAutoHelper dtoAutoHelper: forma.getListaServiciosAutoHelper() ){
			DtoServiciosAutorizaciones dtoServiciosAutorizaciones = new DtoServiciosAutorizaciones();
			dtoServiciosAutorizaciones.setCodigoServicio(new Integer(dtoAutoHelper.getCodigoAxiomaJS()));
			dtoServiciosAutorizaciones.setDescripcionServicio(dtoAutoHelper.getDescripcionCupsJS());
			dtoServiciosAutorizaciones.setCantidadSolicitada(Integer.parseInt(dtoAutoHelper.getCantidad()));
			dtoServiciosAutorizaciones.setDiagnosticoIngEst(forma.getDtoPacienteSeleccionado().getDiagnosticoIngEst());
			dtoServiciosAutorizaciones.setTipoCieDxIngEst(forma.getDtoPacienteSeleccionado().getTipoCieDxIngEst());
			dtoServiciosAutorizaciones.setDescripcionDiagnosticoIngEst(forma.getDtoPacienteSeleccionado().getDescripcionDiagnosticoIngEst());
			dtoServiciosAutorizaciones.setEspecialidad(new Integer(dtoAutoHelper.getCodigoEspecialidadJS()));
			dtoServiciosAutorizaciones.setCodigoGrupoServicio(new Integer(dtoAutoHelper.getCodigoGrupoServicio()));
			dtoServiciosAutorizaciones.setNombreGrupoServicio(dtoAutoHelper.getNombreGrupoServicio());
			dtoServiciosAutorizaciones.setTipoServicio(dtoAutoHelper.getTipoServicioJS());
			if (dtoAutoHelper.getUrgente()=="true"){
				dtoServiciosAutorizaciones.setUrgente(ConstantesBD.acronimoSiChar);
			}else {
				dtoServiciosAutorizaciones.setUrgente(ConstantesBD.acronimoNoChar);
			}
			
			listaServicios.add(dtoServiciosAutorizaciones);
		}
		
		return listaServicios; 
		
		
	}

	/**
	 * M&eacute;todo encargado de cargar la lista
	 * de finalidades por servicio seleccionado
	 * en la b&uacute;squeda g&eacute;nerica de servicios
	 * @param forma
	 * @return List<DtoViewFinalidadesServ>
	 * @author Diana Carolina G
	 */ 
	private List<DtoViewFinalidadesServ> accionCargarListaFinalidadesxServicio(
			AutorizacionesServiciosMedicamentosIngresoEstanciaForm forma) {
		
		
		List<DtoViewFinalidadesServ> listDtoHelper= new ArrayList<DtoViewFinalidadesServ>();
		UtilidadTransaccion.getTransaccion().begin();
		if(!UtilidadTexto.isEmpty(forma.getDtoServicioHelper().getCodigoAxiomaJS()) )
		{	
			//UtilidadTransaccion.getTransaccion().begin();
			List<ViewFinalidadesServ> listViewFinalidad=viewFinalidadesServicio.obtenerViewFinalidadesServ(Integer.parseInt(forma.getDtoServicioHelper().getCodigoAxiomaJS()));
			
			//UtilidadTransaccion.getTransaccion().commit();
			
			listDtoHelper= new ArrayList<DtoViewFinalidadesServ>();
			for(ViewFinalidadesServ object: listViewFinalidad){
				DtoViewFinalidadesServ dtoHelper = new DtoViewFinalidadesServ();
				dtoHelper.setCodigoFinalidad(object.getId().getCodigoFinalidad());
				dtoHelper.setNombreFinalidad(object.getId().getNombreFinalidad());
				listDtoHelper.add(dtoHelper);
			}
		}
		UtilidadTransaccion.getTransaccion().commit();
		return listDtoHelper;
	}
	
	
	
	
	/**
	 * Valida los convenios cargados del paciente
	 * @param listaconveniosIngresoPaciente
	 * @param forma
	 * @param request
	 * @param errores
	 * @return encontroConvenioValido
	 */
	private boolean validarConveniosPaciente(int codigoPersona, ArrayList<DtoUsuariosCapitados> listaconveniosIngresoPaciente,
			AutorizacionesServiciosMedicamentosIngresoEstanciaForm forma, HttpServletRequest request, ActionErrors errores)
	{
	
		Convenios convenios				= null;
		boolean encontroConvenioValido 	= false;
		Date fechaActual 				= UtilidadFecha.getFechaActualTipoBD();
		DtoUsuariosCapitados pacienteCapitado=new DtoUsuariosCapitados();
		
		if(Utilidades.isEmpty(listaconveniosIngresoPaciente))
		{
			String mensajeConcreto = fuenteMensaje.getMessage("AutorizacionesServiciosMedicamentosIngresoEstanciaForm.noPoblacionCapitadaSub");
			retornarErrorEnviado(forma, request, mensajeConcreto, errores);
		}
		else
		{
			/**En caso de tener resultado la lista solo tiene un registro ya que se esta buscando por código de paciente */			
			pacienteCapitado	= listaconveniosIngresoPaciente.get(0);
			pacienteCapitado.getConvenioInt();			
			
			/**
			 *Validar si el convenio del paciente es capitado, maneja capitaci&oacute;n subcontratada 
			 *y se encuentra dentro de un cargue vigente 
			 */
			convenios= convenioServicio.findById(pacienteCapitado.getConvenioInt());
			
			Log4JManager.info("Esta activo el convenio/responsable del paciente: "+convenios.isActivo());
			IUsuarioXConvenioMundo usuarioXConvenioMundo = CapitacionFabricaMundo.crearUsuarioXConvenioMundo();
			

			if(convenios.getCapitacionSubcontratada() != null)
			{
				if(convenios.getCapitacionSubcontratada().equals(ConstantesBD.acronimoSiChar))
				{
					HashSet<Contratos> setContratosConvenios = new HashSet<Contratos>();
					setContratosConvenios.addAll(convenios.getContratoses());
					ArrayList<Contratos> listaContratosConvenio = new ArrayList(setContratosConvenios); 
	
					for (Contratos contratos : listaContratosConvenio) 
					{
						if(contratos.getFechaFinal().after(fechaActual))
						{
							if(usuarioXConvenioMundo.existeCargueVigentePorPersonaPorContrato(codigoPersona, fechaActual, contratos.getCodigo())){
								forma.getDtoPacienteSeleccionado().setCodigoContrato(contratos.getCodigo());
								if (convenios.getManejaPresupCapitacion() != null){
									forma.setConvenioManejaPresupuesto(convenios.getManejaPresupCapitacion());
								}else {
									forma.setConvenioManejaPresupuesto(ConstantesBD.acronimoNoChar);
								}
								encontroConvenioValido = true; 
								break;
							}
							else{
								String mensajeConcreto = fuenteMensaje.getMessage("AutorizacionesServiciosMedicamentosIngresoEstanciaForm.noContratoVigente", convenios.getNombre());
								errores = retornarErrorEnviado(forma, request, mensajeConcreto, errores);
							}
						}
						else
						{
							String mensajeConcreto = fuenteMensaje.getMessage("AutorizacionesServiciosMedicamentosIngresoEstanciaForm.noContratoVigente", convenios.getNombre());
							errores = retornarErrorEnviado(forma, request, mensajeConcreto, errores);
						}
					}
				}
				else
				{
					String mensajeConcreto = fuenteMensaje.getMessage("AutorizacionesServiciosMedicamentosIngresoEstanciaForm.noManejaCapitacion", convenios.getNombre());
					errores = retornarErrorEnviado(forma, request, mensajeConcreto, errores);
				}
			}
			else
			{
				String mensajeConcreto = fuenteMensaje.getMessage("AutorizacionesServiciosMedicamentosIngresoEstanciaForm.noManejaCapitacion", convenios.getNombre());
				errores = retornarErrorEnviado(forma, request, mensajeConcreto, errores);
			}
		}
		
		return encontroConvenioValido;
	}
	
	

	
	/**
	 * M&eacute;todo encargado de obtener el convenio del
	 * paciente 
	 * @param forma
	 * @param usuario
	 * @param paciente
	 * @param request
	 * @author Cristhian Murillo 
	 */
	private void accionEmpezar(AutorizacionesServiciosMedicamentosIngresoEstanciaForm forma, UsuarioBasico usuario, PersonaBasica paciente, HttpServletRequest request) 
	{
		UtilidadTransaccion.getTransaccion().begin();
		
		DtoUsuariosCapitados parametrosBusqueda			=new DtoUsuariosCapitados();
		Convenios convenio								=new Convenios();
		ArrayList<DtoUsuariosCapitados> listaConvenios 	=new ArrayList<DtoUsuariosCapitados>();
		parametrosBusqueda.setFechaVigenciaCargue(UtilidadFecha.getFechaActualTipoBD());
		parametrosBusqueda.setCodigoPaciente(paciente.getCodigoPersona());
		
		/**En caso de tener resultado la lista solo tiene un registro ya que se esta buscando por código de paciente */
		listaConvenios = pacienteServicio.buscarPacienteConvenio(parametrosBusqueda);
		
				
		ActionErrors errores 	= new ActionErrors();
		forma.setConvenioValido(validarConveniosPaciente(paciente.getCodigoPersona(), listaConvenios, forma, request, errores));
		
		if(!forma.isConvenioValido())
		{
			saveErrors(request, errores);
		}
		else
		{
			convenio.setCodigo(listaConvenios.get(0).getConvenioInt());
			convenio.setNombre(listaConvenios.get(0).getNombreConvenio());
			forma.getDtoPacienteSeleccionado().setConvenio(convenio);
			
			//-------------------------------
			DTOAdministracionAutorizacion parametros = new DTOAdministracionAutorizacion();
			parametros.setAdministracionPoblacionCapitada(false);
			DtoPaciente dtoPaciente = new DtoPaciente();
			dtoPaciente.setCodigo(paciente.getCodigoPersona());
			parametros.setPaciente(dtoPaciente);
			parametros.setOrdenarDescendente(false);
			parametros.setExcluirRegistrosTemporales(true);
			
			ArrayList<DTOAdministracionAutorizacion> listaAutorizacionesIngresoEstancia = new ArrayList<DTOAdministracionAutorizacion>();
			//listaAutorizacionesIngresoEstancia.addAll(autorizacionIngresoEstanciaServicio.obtenerAutorizacionesPorPaciente(parametros));
			listaAutorizacionesIngresoEstancia.addAll(autorizacionIngresoEstanciaServicio.obtenerAutorizacionesIngresoEstanciaPaciente(parametros));
			//-------------------------------
			
			if(Utilidades.isEmpty(listaAutorizacionesIngresoEstancia))
			{
				String mensajeConcreto = fuenteMensaje.getMessage("AutorizacionesServiciosMedicamentosIngresoEstanciaForm.noSolicitudes");
				mostrarErrorEnviado(forma, request, mensajeConcreto);
			}
			else
			{
				forma.setListaAutorizacionesIngresoEstancia(listaAutorizacionesIngresoEstancia);
				validarListaAutorizacionesIngresoEstancia(forma, usuario, request);
			}
		}
		
		UtilidadTransaccion.getTransaccion().commit();
	}





	/**M&eacute;todo encargado de validar la lista de 
	 * Autorizaciones de ingreso estancia del paciente 
	 * cargado en sesi&oacute;n
	 * @param forma
	 * @param usuario
	 * @param request
	 * @author Diana Carolina G
	 */
	private void validarListaAutorizacionesIngresoEstancia(AutorizacionesServiciosMedicamentosIngresoEstanciaForm forma, UsuarioBasico usuario, HttpServletRequest request) 
	{
		String diasVigentesNuevaAutoEstancia = ValoresPorDefecto.getDiasVigentesNuevaAutorizacionEstanciaSerArt(usuario.getCodigoInstitucionInt());
		ArrayList<DTOAdministracionAutorizacion> listaAutorizacionesIngresoEstanciaVigentes = new ArrayList<DTOAdministracionAutorizacion>();
		Date fechaActual = UtilidadFecha.getFechaActualTipoBD();
		
		if(UtilidadTexto.isEmpty(diasVigentesNuevaAutoEstancia))
		{
			/* * Si NO está definido el parámetro se debe verificar de las autorizaciones encontradas si están vigentes  */
			
			for (DTOAdministracionAutorizacion dtoAdministracionAutorizacion : forma.getListaAutorizacionesIngresoEstancia()) 
			{
				if( (dtoAdministracionAutorizacion.getFechaVencimientoAutorizacion().after(fechaActual))  ||
						(dtoAdministracionAutorizacion.getFechaVencimientoAutorizacion().equals(fechaActual)) )
				{
					listaAutorizacionesIngresoEstanciaVigentes.add(dtoAdministracionAutorizacion);
				}
			}
			
			forma.setListaAutorizacionesIngresoEstancia(new ArrayList<DTOAdministracionAutorizacion>());
			
			if(Utilidades.isEmpty(listaAutorizacionesIngresoEstanciaVigentes))
			{
				String mensajeConcreto = fuenteMensaje.getMessage("AutorizacionesServiciosMedicamentosIngresoEstanciaForm.noDefinidoDiasVigencia");
				mostrarErrorEnviado(forma, request, mensajeConcreto);
			}
		}
		else
		{
			/*  * Si SI está definido el parametro: Fecha Vigencia Autorización = 
			 * Fecha Final Autorización +  Días vigentes para solicitar nueva autorización Estancia y Servicios/Artículos  */
			
			for (DTOAdministracionAutorizacion dtoAdministracionAutorizacion : forma.getListaAutorizacionesIngresoEstancia()) 
			{
				Date fechaVigenciaAutorizacion = UtilidadFecha.conversionFormatoFechaStringDate(
						UtilidadFecha.incrementarDiasAFecha(UtilidadFecha.conversionFormatoFechaAAp(
								dtoAdministracionAutorizacion.getFechaVencimientoAutorizacion()), Integer.parseInt(diasVigentesNuevaAutoEstancia), false));
				
				if( (fechaVigenciaAutorizacion.after(fechaActual))  || (fechaVigenciaAutorizacion.equals(fechaActual)) )
				{
					listaAutorizacionesIngresoEstanciaVigentes.add(dtoAdministracionAutorizacion);
				}
			}
			
			if(Utilidades.isEmpty(listaAutorizacionesIngresoEstanciaVigentes))
			{
				String mensajeConcreto = fuenteMensaje.getMessage("AutorizacionesServiciosMedicamentosIngresoEstanciaForm.autorizacionNoVigente");
				mostrarErrorEnviado(forma, request, mensajeConcreto);
			}
		}
		
		forma.setListaAutorizacionesIngresoEstancia(listaAutorizacionesIngresoEstanciaVigentes);
		
	}




	/**
	 * Valida las precondiciones para empezas la funcionalidad
	 * @param mapping
	 * @param request
	 * @param usuario
	 * @return ActionForward
	 * 
	 * @author Cristhian Murillo
	 */
	private ActionForward validarEmpezar(ActionMapping mapping, HttpServletRequest request, UsuarioBasico usuario) 
	{
		// Validación consecutivo disponible
		Connection con = null;
		int codigoInstitucion = usuario.getCodigoInstitucionInt();
		Connection conH=UtilidadPersistencia.getPersistencia().obtenerConexion();
		String consecutivoAutoCapitaSub = UtilidadBD.obtenerValorActualTablaConsecutivos(conH, ConstantesBD.nombreConsecutivoAutorizacionPoblacionCapitada, codigoInstitucion);
		UtilidadBD.closeConnection(conH);
		if(UtilidadTexto.isEmpty(consecutivoAutoCapitaSub))
		{
			String mensajeConcreto = fuenteMensaje.getMessage("AutorizacionesServiciosMedicamentosIngresoEstanciaForm.noExisteConsecutivoDisponible");
			
	    	con = UtilidadBD.abrirConexion();
	    	ActionForward forward=ComunAction.accionSalirCasoError(mapping, request, con, logger, "Paciente no cargado", mensajeConcreto, false);
	    	UtilidadBD.closeConnection(con);
	    	
	    	return forward;
		}
		
		return null;
	}
	


	
	
	/**
	 * Validacion de Paciente Cargado en Sesion
	 * @param con
	 * @param paciente
	 * @param request
	 * @param mapping
	 * @return ActionForward
	 */
	 private ActionForward accionValidarPaciente(Connection con, PersonaBasica paciente, HttpServletRequest request, ActionMapping mapping) 
	{
			if(paciente==null || paciente.getCodigoPersona()<=0)
			{
				return ComunAction.accionSalirCasoError(mapping, request, con, logger, "errors.paciente.noCargado", "errors.paciente.noCargado", true);
			}
	        
			return null;
    }
	
	 
	 
	 /**
	 * Muestra el mensaje de error enviado
	 * @param forma
	 * @param request
	 * @param errores
	 * @autor Cristhian Murillo
	 */
	private ActionErrors retornarErrorEnviado(AutorizacionesServiciosMedicamentosIngresoEstanciaForm forma, HttpServletRequest request, String mensajeConcreto, ActionErrors errores) 
	{
		errores.add("error_concreto_enviado", new ActionMessage("errors.notEspecific", mensajeConcreto));
		saveErrors(request, errores);
		return errores;
	}
	 
		
	/**
	 * Muestra el mensaje de error enviado
	 * @param forma
	 * @param request
	 * @autor Cristhian Murillo
	 */
	private void mostrarErrorEnviado(AutorizacionesServiciosMedicamentosIngresoEstanciaForm forma, HttpServletRequest request, String mensajeConcreto) 
	{
		ActionErrors errores = new ActionErrors();
		errores.add("error_concreto_enviado", new ActionMessage("errors.notEspecific", mensajeConcreto));
		saveErrors(request, errores);
	}
	
	/**
	 * Método de ordenamiento generico
	 * @param mapping
	 * @param forma
	 * @return ActionForward
	 * 
	 * @author Cristhian Murillo
	 */
	private ActionForward accionOrdenar(ActionMapping mapping,	AutorizacionesIngresoEstanciaForm forma) 
	{
		boolean ordenamiento = false;
		
		if(forma.getEsDescendente().equals(forma.getPatronOrdenar()+"descendente"))
		{
			ordenamiento = true;
		}
		
		SortGenerico sortG=new SortGenerico(forma.getPatronOrdenar(),ordenamiento);
		Collections.sort(forma.getListaUsuariosCapitados(),sortG);

		return null;
	}
	
	/**
	 * Método de ordenamiento generico
	 * @param mapping
	 * @param forma
	 * @return ActionForward
	 * 
	 * @author Diana Carolina G
	 */
	private ActionForward accionOrdenar(ActionMapping mapping,
			AutorizacionesServiciosMedicamentosIngresoEstanciaForm forma) {
		
		boolean ordenamiento = false;
		
		if(forma.getEsDescendente().equals(forma.getPatronOrdenar()+"descendente"))
		{
			ordenamiento = true;
		}
		
		SortGenerico sortG=new SortGenerico(forma.getPatronOrdenar(),ordenamiento);
		Collections.sort(forma.getListaAutorizacionesIngresoEstancia(),sortG);

		return null;
		
	}
	
	/**
	 * Valida los campos requeridos para la funcionalidad en el momento de iniciar
	 * el proceso de autorizacion
	 * @param forma
	 * @param request
	 * @return ActionErrors
	 * 
	 * @author Diana Carolina G
	 */
	private ActionErrors validarCamposRequeridos(AutorizacionesServiciosMedicamentosIngresoEstanciaForm forma, HttpServletRequest request, UsuarioBasico usuario)
	{
		ActionErrors errores				= new ActionErrors();
		//String mensajeConcreto 			= "";
		String cantidadRequeridaArticulo	= "";
		/*String diasCalculoFechaVencimientoServ = "";
		String diasCalculoFechaVencimientoArt = "";
		diasCalculoFechaVencimientoServ = ValoresPorDefecto.getDiasCalcularFechaVencAutorizacionServicio(usuario.getCodigoInstitucionInt());
		diasCalculoFechaVencimientoArt = ValoresPorDefecto.getDiasCalcularFechaVencAutorizacionArticulo(usuario.getCodigoInstitucionInt());*/
		
		if((forma.getFechaVencimiento()==null) || (forma.getFechaVencimiento().equals("")) ){
			errores.add("La Fecha de Vencimiento es requerida",
					new ActionMessage("errors.required", "El campo Fecha Vencimiento"));
			
			}else{
				
					if(forma.getTipoAutorizacion().equals(tipoAutorizacionServicio)){
						validacionFechaVencimientoRequerida(forma, errores, request);
						
						if(forma.getListaServiciosAutoHelper().equals("") || forma.getListaServiciosAutoHelper().equals(null)){
							errores.add("Seleccionar un Servicio es requerido",
									new ActionMessage("errors.required", "Servicio "));
						}else{
							
							if(UtilidadTexto.isEmpty(forma.getListaServiciosAutoHelper().
									get(forma.getPostListAutorizaciones()).getCantidad())){
								
								errores.add("Cantidad del servicio requerida",
										new ActionMessage("errors.required", "Cantidad del servicio"));
							}
							
							if(UtilidadTexto.isEmpty(forma.getListaServiciosAutoHelper().
									get(forma.getPostListAutorizaciones()).getFinalidad())){
								
								errores.add("Finalidad del servicio requerida",
										new ActionMessage("errors.required", "Finalidad del servicio"));
							}
						} 
						
					}
					
					if(forma.getTipoAutorizacion().equals(tipoAutorizacionArticulo)){
						validacionFechaVencimientoRequerida(forma, errores, request);
						
						if(forma.getNumeroFilasMapa() == 0){
							errores.add("Seleccionar un Medicamentos o Insumo es requerido",
									new ActionMessage("errors.required", "Medicamento o Insumo "));
						}
						
						if(forma.getNumeroFilasMapa()>0){
							
							for(int i=0; i<forma.getNumeroFilasMapa(); i++){
								cantidadRequeridaArticulo= forma.getArticulos().get("cantidad_"+i).toString();
								
								if ((cantidadRequeridaArticulo == null) || (cantidadRequeridaArticulo.equals(""))){
									errores.add("La Cantidad del Art&iacute;culo es requerida",
											new ActionMessage("errors.required", "El campo Cantidad del Artículo"));
								}
							}
							
						} 
					}
			}
		
		
		return errores;
	}


	/**
	 * Validaci&oacute;n de fecha de vencimiento requerida para
	 * generar la autorizaci&oacute;n
	 * @param forma
	 * @param errores
	 * @author Diana Carolina G 
	 */
	private void validacionFechaVencimientoRequerida(
			AutorizacionesServiciosMedicamentosIngresoEstanciaForm forma,
			ActionErrors errores,
			HttpServletRequest request) {
		String fechaVencimiento = UtilidadFecha.conversionFormatoFechaAAp(
				forma.getFechaVencimiento());
		
		String fechaActual = UtilidadFecha.conversionFormatoFechaAAp(
				Calendar.getInstance().getTime());
		
		if(UtilidadFecha.esFechaMenorIgualQueOtraReferencia(
				fechaVencimiento,fechaActual)){
			 errores.add("Fecha menor que la actual", new ActionMessage("errors.notEspecific", 
     				fuenteMensaje.getMessage("AutorizacionesServiciosMedicamentosIngresoEstanciaForm.fechaPosteriorFechaActual",
     						" Vencimiento "+fechaVencimiento," Actual "+fechaActual)));
		}
	}
	
	/**
	 * Metodo que evalua si se encuentra definido el parametro del formato
	 * de Impresion Autorizacion Capitacion Subcontratada
	 * MT 3488
	 * 
	 * @param mapping
	 * @param forma
	 * @param request
	 * @param usuarioSesion
	 * @author Camilo Gómez
	 */
	private void validarFormatoImpresion(ActionMapping mapping, AutorizacionesServiciosMedicamentosIngresoEstanciaForm forma,
			HttpServletRequest request, UsuarioBasico usuarioSesion)
	{
		ActionErrors errores	=new ActionErrors();
		
		String tipoFormatoImpresion = ValoresPorDefecto.getFormatoImpresionAutorEntidadSub(
					usuarioSesion.getCodigoInstitucionInt()); 
			
		if(UtilidadTexto.isEmpty(tipoFormatoImpresion))
		{
			errores.add("Falta definir Formato Impresion", new ActionMessage("errors.notEspecific", 
					fuenteMensaje.getMessage("AutorizacionesServiciosMedicamentosIngresoEstanciaForm.mensajeFaltaDefinirFormato")));
			saveErrors(request, errores);
		}else{
			//Nuevo Formato Se modifica el formato Versalles y se renombra a Capitacion (estandar).
			if(tipoFormatoImpresion.equals(ConstantesIntegridadDominio.acronimoFormatoImpresionEstandar))
			{
				forma.setFormatoImpresionDefinido(true);
			}else{
				errores.add("Falta definir Formato Impresion", new ActionMessage("errors.notEspecific", 
						fuenteMensaje.getMessage("AutorizacionesIngresoEstanciaForm.mensajeFaltaDefinirFormato")));
				saveErrors(request, errores);
			}
		}
	}
	 
}
