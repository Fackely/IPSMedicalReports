/*
 * Jul 5, 2007
 * Proyect axioma
 * Paquete com.princetonsa.action.facturacion
 * @author Jorge Armando Osorio Velasquez
 * Compilador Java 1.5.0_07-b03
 */
package com.princetonsa.action.facturacion;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.ElementoApResource;
import util.InfoDatosInt;
import util.InfoDatosString;
import util.Listado;
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.UtilidadSesion;
import util.UtilidadTexto;
import util.UtilidadValidacion;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.facturacion.InfoCobertura;
import util.facturacion.InfoResponsableCobertura;
import util.facturacion.InfoTarifa;
import util.facturacion.UtilidadesFacturacion;
import util.historiaClinica.UtilidadesHistoriaClinica;
import util.manejoPaciente.UtilidadesManejoPaciente;
import util.salas.UtilidadesSalas;

import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.facturacion.DistribucionCuentaForm;
import com.princetonsa.dto.cargos.DtoDetalleCargo;
import com.princetonsa.dto.cargos.DtoDetalleCargoArticuloConsumo;
import com.princetonsa.dto.facturacion.DTOResultadoBusquedaDetalleMontos;
import com.princetonsa.dto.facturacion.DtoValidacionTipoCobroPaciente;
import com.princetonsa.dto.manejoPaciente.DtoAutorizacion;
import com.princetonsa.dto.manejoPaciente.DtoRequsitosPaciente;
import com.princetonsa.dto.manejoPaciente.DtoSubCuentas;
import com.princetonsa.mundo.IngresoGeneral;
import com.princetonsa.mundo.ObservableBD;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.cargos.Cargos;
import com.princetonsa.mundo.cargos.Contrato;
import com.princetonsa.mundo.cargos.Convenio;
import com.princetonsa.mundo.cargos.EsquemaTarifario;
import com.princetonsa.mundo.cargos.UtilidadJustificacionPendienteArtServ;
import com.princetonsa.mundo.facturacion.Cobertura;
import com.princetonsa.mundo.facturacion.DistribucionCuenta;
import com.princetonsa.mundo.facturacion.EntidadesSubContratadas;
import com.princetonsa.mundo.inventarios.FormatoJustArtNopos;
import com.princetonsa.mundo.manejoPaciente.Autorizaciones;
import com.princetonsa.mundo.manejoPaciente.ViasIngreso;
import com.princetonsa.mundo.ordenesmedicas.cirugias.SolicitudesCx;
import com.princetonsa.mundo.solicitudes.Solicitudes;
import com.servinte.axioma.bl.facturacion.facade.FacturacionFacade;
import com.servinte.axioma.dto.facturacion.DtoInfoCobroPaciente;
import com.servinte.axioma.dto.facturacion.InfoCreacionHistoricoCargosDto;
import com.servinte.axioma.fwk.exception.IPSException;
import com.servinte.axioma.fwk.exception.util.CODIGO_ERROR_NEGOCIO;
import com.servinte.axioma.orm.Cuentas;
import com.servinte.axioma.orm.delegate.administracion.CuentasDelegate;
import com.servinte.axioma.servicio.fabrica.facturacion.FacturacionServicioFabrica;
import com.servinte.axioma.servicio.interfaz.facturacion.ICalculoValorCobrarPaciente;
import com.servinte.axioma.servicio.interfaz.facturacion.IMontosCobroServicio;
import com.servinte.axioma.servicio.interfaz.facturacion.IValidacionTipoCobroPacienteServicio;
/**
 * @author Jorge Armando Osorio Velasquez
 *
 */
public class DistribucionCuentaAction extends Action 
{
	/**
	 * Para hacer logs de debug / warn / error de esta funcionalidad.
	 */
	private Logger logger = Logger.getLogger(DistribucionCuentaAction.class);
	
	/**
	 * 
	 */
	private static final String[] indicesEsquemasInventario={"codigo_","subcuenta_","claseinventario_","nombreclaseinventario_","esquematarifario_","nombreesquematarifario_"};
		
	/**
	 * 
	 */
	private static final String[] indicesEsquemasProcedimientos={"codigo_","subcuenta_","gruposervicio_","nombregruposervicio_","esquematarifario_","nombreesquematarifario_"};

	
	/**
	 * Indices para el mapa de detalle solicitudes.
	 */
	private String[] indicesDetSolicitudes={
											"fechasolicitud_",
											"solicitud_",
											"viaingreso_",
											"servicio_",
											"articulo_",
											"nomserart_",
											"esservicio_",
											"cuenta_",
											"tiposolicitud_",
											"nomtiposolicitud_",
											"serviciocx_",
											"tipoasocio_",
											"nomtipoasocio_",
											"tipodistribucion_",
											"tipodistribucionoriginal_",
											"consecutivosolicitud_",
											"codccsolicita_",
											"nomccsolicita_",
											"codccejecuta_",
											"nomccejecuta_",
											"cantidad_",
											"codestadohc_",
											"nomestadohc_",
											"numresservicio_",
											"numresfactservicio_",
											"numresarticulo_",
											"numresfactarticulo_",
											"paquetizada_",
											"solsubcuentapadre_",
											"codigoportatil_",
											"nomservicioportatil_",
											"detcxhonorarios_",
											"detascxsalmat_"
											};
	
	private String[] indicesDetResponsables={
											"fechasolicitud_",
											"nomserart_",
											"cantidad_",
											"nomtiposolicitud_",
											"nomtipoasocio_",
											"consecutivosolicitud_",
											"nomccsolicita_",
											"nomccejecuta_",
											"tipodistribucion_",
											"nomestadohc_",
											"valtotalcargado_",
											"porcentajecargado_",
											"estadofac_",
											"nomestadofac_",
											};

	
	/**
	 * Indices para el mapa de detalle solicitudes.
	 */
	private String[] indicesDistribucionSol={
												"codigodetcargo_",
												"codsolsubcuenta_",
												"solicitud_",
												"subcuenta_",
												"autorizacion_",
												"cantidad_",
												"porcentaje_",
												"esquematarifario_",
												"nomesquematarifario_",
												"valorunitariocargado_",
												"valortotalcargado_",
												"porcentajedcto_",
												"valorunitariodcto_",
												"valortotaldcto_",
												"codestado_",
												"nomestado_",
												"facturado_"
											};
	
	
	public  static String indicesListado [] = {"idIngreso0_","consecutivoIngreso1_","centroAtencion2_","nombreCentroAtencion3_","estadoIngreso4_",
		"fechaAperturaIngreso5_","viaIngreso6_","nombreViaIngreso7_","fechaCierreIngreso8_","numeroCuenta9_",
		"estadoCuenta10_","nombreEstadoCuenta11_"};
	

	public static String indicesEncabezadoDetalle [] ={"ingreso0","consecutivoIngreso1","codigoCentroAtencion2","nombreCentroAtencion3",
													   "fechaHoraIngreso4","numeroAutorizacionAdmision5","idCuenta6","viaIngreso7"};
		
	public static String indicesServiciosArticulos [] = {"solicitud0_","consecutivoOrdenes1_","tipoSolicitud2_","nombreTipoSolicitud3_","estadoHistoriaClinica4_",
														 "nombreEstadoHistoriaClinica5_","centroCostoSolicita6_","nombreCentroCostoSolicita7_",
														 "centroCostoEjecuta8_","nombreCentroCostoEjecuta9_","porcentajeAutorizado10_",
														 "montoAutorizado11_","valorUnitario12_","valorTotal13_","estadoCargo14_","nombreEstadoCargo15_",
														 "fechaSolicitud16_","servArticulo17_","cantidad18_","requiereAutorizacion19_","numeroAutorizacion20_",
														 "codigoDetalleCargo21_","check22_","checkTot23","nombreTipoAsocio24_","servicioCx25_","esMaterialEspecial26_",
														 "codAutorizacion27_","codDetAutorizacion28_","estadoAutorizacion29_","tipoTramite30_","urgente31_","esVigente32_",
														 "fechaVigencia33_","idSubCuenta34_","codigoConvenio35_","fechaAutorizacion36_","horaAutorizacion37_","medicoSol38_",
														 "nombreMedicoSol39_","indMostrarCheck40_"};
	
	private static final String DESCRIPCION_BACKUP = "Backup Proceso Distribución de Cuentas";
	
	private static final String KEY_ERROR_NO_ESPECIFIC="errors.notEspecific";
	/**
	 * 
	 */
	public ActionForward execute(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
	{
		Connection con=null;
		try{
			if (form instanceof DistribucionCuentaForm)
			{
				UsuarioBasico usuario=(UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
				PersonaBasica paciente=(PersonaBasica)request.getSession().getAttribute("pacienteActivo");

				DistribucionCuentaForm forma=(DistribucionCuentaForm) form;
				DistribucionCuenta mundo=new DistribucionCuenta();

				con=UtilidadBD.abrirConexion();

				String estado=forma.getEstado();

				logger.warn("Estado [DistribucionCuentaAction] -->"+estado);

				//validar que el paciente este cargado en sesion.
				if(paciente.getCodigoPersona()<1)
				{
					return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Paciente no Cargado", "errors.paciente.noCargado", true);
				}

				/**
				 * Validar concurrencia
				 * Si ya estï¿½ en proceso de facturaciï¿½n, no debe dejar entrar
				 **/
				if(UtilidadValidacion.estaEnProcesofacturacion(con, paciente.getCodigoPersona(), "") )
				{
					return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error.facturacion.cuentaEnProcesoFact", "error.facturacion.cuentaEnProcesoFact", true);
				}

				forma.setMostrarMensaje(new ResultadoBoolean(false));


				if(estado.equals("empezar"))
				{
					forma.reset();
					forma.resetJust();

					forma.setConvenios(Utilidades.obtenerConvenios(con, "", ConstantesBD.codigoTipoContratoEvento+"", true, "", true));
					//Se consultan los convenios del paciente
					HashMap conveniosPaciente = UtilidadesManejoPaciente.consultarConveniosPaciente(con, paciente.getCodigoPersona()+"");
					for(int i=0;i<Integer.parseInt(conveniosPaciente.get("numRegistros").toString());i++)
					{
						HashMap elemento = new HashMap();
						elemento.put("codigoConvenio", conveniosPaciente.get("codigoConvenio_"+i));
						elemento.put("nombreConvenio", conveniosPaciente.get("nombreConvenio_"+i));
						elemento.put("codigoTipoContrato", ConstantesBD.codigoTipoContratoCapitado+"");
						elemento.put("pyp", conveniosPaciente.get("esPyp_"+i));
						elemento.put("empresasInstitucion", conveniosPaciente.get("empresasInstitucion_"+i));
						forma.getConvenios().add(elemento);
					}
					forma.setRegistrandoDistribucion(true);
					forma.setTiposIdentificacion(Utilidades.obtenerTiposIdentificacion(con, "", usuario.getCodigoInstitucionInt()));
					forma.setIngresos(mundo.consultarIngresosValidosDistribucion(con,paciente.getCodigoPersona(),usuario.getCodigoCentroAtencion()));
					if(Utilidades.convertirAEntero(forma.getIngresos().get("numRegistros")+"",false)==1)
					{
						forma.setIndiceIngresoSeleccionado(0);
						return this.accionCargarInformacionGeneral(con,forma,mundo,paciente,usuario,mapping,request);
					}
					else if (Utilidades.convertirAEntero(forma.getIngresos().get("numRegistros")+"",false)>1)
					{
						UtilidadBD.closeConnection(con);
						return mapping.findForward("listadoIngresos");
					}
					else
					{
						return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Paciente no tiene Ingresos/Cuentas validos para la distribucion.", "error.distribucion.sinCuentasValidasDistribuir", true);
					}
				}
				else if (estado.equals("distribucionCancelada"))
				{
					return this.accionDistribucionCancelada(con, paciente, forma, mapping, response);
				}
				else if(estado.equals("cargarIngreso") || estado.equals("cargarIngresoButton"))
				{
					return this.accionCargarInformacionGeneral(con,forma,mundo,paciente,usuario,mapping,request);
				}
				else if(estado.equals("guardarPaginaPrincipal"))
				{
					return this.accionModificarInformacionGeneral(con, forma, mundo, paciente, usuario, mapping, request);
				}
				else if(estado.equals("guardarModificarInformacionGeneral"))
				{
					return this.accionGuardarEncabezadoDistribucion(con,forma,mundo,paciente,usuario,mapping,request, response);
				}
				else if(estado.equals("mostrarInfoResponsable"))
				{
					forma.setControlMensaje(false);
					forma.setMensajesAlerta(new ArrayList<ElementoApResource>());

					return this.accionCargarInfoResponsable(con,forma,mundo,paciente,usuario,mapping,request);
				}
				else if(estado.equals("ingresarConvenioAdicionalNuevo"))
				{
					forma.setControlMensaje(false);
					forma.setMensajesAlerta(new ArrayList<ElementoApResource>());

					return this.accionCargarInfoResponsable(con,forma,mundo,paciente,usuario,mapping,request);
				}
				else if(estado.equals("guardarInfoResponsable"))
				{
					if(forma.isModificacionResponsable())
					{
						return this.accionModificarInfoResponsable(con,forma,mundo,paciente,usuario,mapping);
					}
					else 
					{
						return this.accionInsertarInfoResponsable(con,forma,mundo,paciente,usuario,mapping, request);
					}
				}
				else if (estado.equals("filtroFechaAfiliacion"))
				{
					return accionFiltroFechaAfiliacion(con,forma,response);
				}
				else if(estado.equals("filtroTipoAfiliado"))
				{
					return filtroTipoAfiliado(con, forma, usuario, response);
				}
				else if(estado.equals("filtroNaturalezaPaciente"))
				{
					return filtroNaturalezaPaciente(con,forma,response);
				}
				else if(estado.equals("eliminarResponsable"))
				{
					return accionEliminarResponsables(con,forma,mapping);
				}
				else if(estado.equals("eliminarResponsableSilSolicitudes"))
				{
					if(forma.getResponsables().size()==1){
						forma.setMostrarMensaje(new ResultadoBoolean(true,"No se pudede eliminar el responsable"));
						return mapping.findForward("principal");
					}
					boolean transaccion=UtilidadBD.iniciarTransaccion(con);
					//envio el indicativo de solicitudes en true en caso de que tenga solicitudes anuladas.
					int ultimoResp=1;
					if(Integer.valueOf(forma.getIndiceReponsableEliminar())==(forma.getResponsables().size()-1)){
						ultimoResp=2;
					}
					transaccion=mundo.eliminarSubCuenta(con,forma.getResponsables().get(Integer.parseInt(forma.getIndiceReponsableEliminar())),forma.getResponsables().get(forma.getResponsables().size()-ultimoResp).getSubCuentaDouble(),forma.getResponsables().get(forma.getResponsables().size()-ultimoResp).getConvenio().getCodigo(),true);
					if(transaccion)
					{
						//transaccion=mundo.reacomodarPrioridades(con,forma.getCodigoIngreso());
						//se agregan los campos de PersonaBasica paciente, UsuarioBasico usuario debido a que se debe realizar la insercion en la tabla ax_pacien en la funcion  reacomodarPrioridades en el sqlBase 
						ResultadoBoolean resultadoBoolean=mundo.reacomodarPrioridades(con,forma.getCodigoIngreso(), paciente, usuario );
						transaccion=resultadoBoolean.isTrue();
						if (!transaccion)
							forma.setMostrarMensaje(new ResultadoBoolean(true,resultadoBoolean.getDescripcion()));
					}

					if(transaccion)
					{
						forma.setMostrarMensaje(new ResultadoBoolean(true,"Responsable eliminado exitosamente"));
						UtilidadBD.finalizarTransaccion(con);
					}
					else
					{
						forma.setMostrarMensaje(new ResultadoBoolean(true,"No se pudo eliminar el responsable"));
						UtilidadBD.abortarTransaccion(con);
					}
					return this.accionCargarInformacionGeneral(con,forma,mundo,paciente,usuario,mapping,request);
				}
				else if(estado.equals("liquidarDistribucion"))
				{
					logger.info("\n\n\n\n\n\n\n\n\nINICIA EL PROCESO DE DISTRIBUCION --->"+UtilidadFecha.getHoraSegundosActual(con)+"\n\n\n\n\n\n\n\n\n");
					return accionLiquidarDistribucion(con,forma,mundo,mapping,paciente,usuario,request);
				}
				else if(estado.equals("guardarFiltroDistribucion"))
				{
					return accionGuardarFiltroDistribucion(con,forma,mundo,paciente,usuario,mapping,response);
				}
				else if(estado.equals("mostrarParamDistribucion"))
				{
					if(UtilidadValidacion.esAccidenteTransito(con, forma.getCodigoIngreso()))
						forma.setModificarMontoAutorizado(false);
					else
						forma.setModificarMontoAutorizado(true);
					return this.accionCargarParamDistribucion(con,forma,mundo,paciente,usuario,mapping,request);
				}
				else if(estado.equals("guardarParamDistribucion"))
				{
					return this.accionModificarParamDistribucion(con,forma,mundo,paciente,usuario,mapping);
				}
				else if(estado.equals("cargarIngresoParamDistribucion"))
				{
					int indice=Utilidades.convertirAEntero(forma.getParamDistribucion("indice")+"");
					forma.getResponsables().get(indice).setPorcentajeAutorizado(forma.getParamDistribucion().get("porcentajeAutorizado")+"");
					forma.getResponsables().get(indice).setMontoAutorizado(forma.getParamDistribucion().get("montoAutorizado")+"");
					forma.getResponsables().get(indice).setObsParametrosDistribucion(forma.getParamDistribucion().get("observaciones")+"");
					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");
				}
				else if(estado.equals("aplicarTopesSOAT"))
				{
					return this.accionAplicarTopesSoat(con,forma,mundo,paciente,usuario,mapping,request,response);
				}
				else if(estado.equals("detalleSolcitudesResponsable"))
				{
					return this.accionDetalleSolicitudesResponsable(con,forma,mundo,paciente,usuario,mapping,request);
				}
				else if(estado.equals("listadoSolicitudesPaciente"))
				{
					return this.accionDetalleSolicitudesPaciente(con,forma,mundo,paciente,usuario,mapping,request);
				}
				else if(estado.equals("vovlerListadoSolicitudesPaciente"))
				{
					//Inicio Cambio por Tarea 52463. Se resetea el mapa que almacena los datos de la Bï¿½squeda Avanzada
					HashMap<String, Object> tempo = new HashMap<String, Object>();
					forma.setParametrosBusquedaAvanzada(tempo);
					//Fin
					forma.resetJust();
					UtilidadBD.closeConnection(con);
					return mapping.findForward("listadoSolicitudesPaciente");
				}
				else if(estado.equals("vovlerListadoSolicitudesPacienteButton"))
				{
					//Inicio Cambio por Tarea 52463. Se resetea el mapa que almacena los datos de la Bï¿½squeda Avanzada
					HashMap<String, Object> tempo = new HashMap<String, Object>();
					forma.setParametrosBusquedaAvanzada(tempo);
					//Fin
					forma.resetJust();
					forma.setDetSolicitudesPaciente("tipodistribucion_"+forma.getIndice(),forma.getDetSolicitudesPaciente("tipodistribucionoriginal_"+forma.getIndice()+""));
					UtilidadBD.closeConnection(con);
					return mapping.findForward("listadoSolicitudesPaciente");
				}
				else if(estado.equals("prepararBusqueda"))
				{
					UtilidadBD.closeConnection(con);
					return mapping.findForward("listadoSolicitudesPaciente");
				}
				else if(estado.equals("ordernarDetalleSolPaciente"))
				{
					this.accionOrdenar(forma);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("listadoSolicitudesPaciente");
				}
				else if(estado.equals("ejecutarBusquedaAvanzada"))
				{
					return this.accionEjecutarBusqueda(con,forma,mundo,paciente,usuario,mapping,request);
				}
				else if(estado.equals("detalleSolicitud"))
				{
					return this.accionDetalleSolicitud(con,forma,mundo,paciente,usuario,mapping,request);
				}
				else if(estado.equals("cambioEsquemaTarifario"))
				{
					return this.accionCambioEsquemaTarifario(con,forma,mapping,usuario);
				}
				else if(estado.equals("guardarDistribucionDetalle"))
				{
					return this.accionGuardarDistribucionDetalle(con,forma,mundo,paciente,usuario,mapping,request);
				}
				else if(estado.equals("consultar"))
				{
					forma.reset();
					forma.setTiposIdentificacion(Utilidades.obtenerTiposIdentificacion(con, "", usuario.getCodigoInstitucionInt()));

					forma.setRegistrandoDistribucion(false);
					forma.setIngresos(mundo.consultarIngresos(con, paciente.getCodigoPersona()));
					UtilidadBD.closeConnection(con);
					return mapping.findForward("detalleDistribucion");
				}
				else if(estado.equals("cargarIngresoConsulta"))
				{
					return this.accionCargarInfoIngresoConsulta(con,forma,mundo,mapping,request,paciente,usuario);			
				}
				else if(estado.equals("detalleSolcitudesResponsableConsulta"))
				{
					return this.accionDetalleSolicitudesResponsableConsulta(con,forma,mundo,paciente,usuario,mapping,request);
				}
				else if(estado.equals("ordernarSolicitudesResponsable"))
				{
					this.accionOrdenarResponsable(forma);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("listadoSolicitudesResponsable");
				}
				else if(estado.equals("prepararBusquedaResponsables"))
				{
					UtilidadBD.closeConnection(con);
					return mapping.findForward("listadoSolicitudesResponsable");
				}
				else if(estado.equals("ejecutarBusquedaAvanzadaResponsables"))
				{
					return this.accionEjecutarBusquedaResponsable(con,forma,mundo,paciente,usuario,mapping,request);
				}
				//Se agrego este estado para la bï¿½squeda avanzada de responsables desde el registro; ya que utiliza una manera diferente
				//de consultar que la funcionalidad propia de la consulta de distribucion de la cuenta
				else if(estado.equals("ejecutarBusquedaAvanzadaResponsablesDesdeRegistro"))
				{
					return this.accionEjecutarBusquedaResponsableDesdeRegistro(con,forma,mundo,paciente,usuario,mapping,request);
				}
				else if(estado.equals("redireccionarFuncionalidad"))
				{
					this.accionDistribucionCancelada(con, paciente, forma, mapping, response);
					try {
						response.sendRedirect("../"+forma.getPathRedireccion());
					} catch (IOException e) {
						e.printStackTrace();
					}
					UtilidadBD.closeConnection(con);
					return null;
				}
				else if(estado.equals("nuevoEsquemaInventarios"))
				{
					Utilidades.nuevoRegistroMapaGenerico(forma.getEsquemasInventario(), indicesEsquemasInventario, "numRegistros","tiporegistro_","MEM");
					UtilidadBD.closeConnection(con);
					return mapping.findForward("parametrosDistribucion");	
				}
				else if(estado.equals("nuevoEsquemaProcedimientos"))
				{
					Utilidades.nuevoRegistroMapaGenerico(forma.getEsquemasProcedimientos(), indicesEsquemasProcedimientos, "numRegistros","tiporegistro_","MEM");
					UtilidadBD.closeConnection(con);
					return mapping.findForward("parametrosDistribucion");	
				}
				else if(estado.equals("eliminarEsquemaInventarios"))
				{
					Utilidades.eliminarRegistroMapaGenerico(forma.getEsquemasInventario(),forma.getEsquemasInventarioEliminados(),forma.getPosEliminar(),indicesEsquemasInventario,"numRegistros","tiporegistro_","BD",false);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("parametrosDistribucion");	
				}
				else if(estado.equals("eliminarEsquemaProcedimientos"))
				{
					Utilidades.eliminarRegistroMapaGenerico(forma.getEsquemasProcedimientos(),forma.getEsquemasProcedimientosEliminados(),forma.getPosEliminar(),indicesEsquemasProcedimientos,"numRegistros","tiporegistro_","BD",false);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("parametrosDistribucion");	
				}
				//***********************CAMBIO EN FUNCIONALIDADE POR DECRETO 4747 *******************
				//Solicitud de Autorizaciones
				else if(estado.equals("solicitudAutorizacion"))
				{
					return accionCargarSolicitudAutorizacion(con, forma, mundo, paciente, usuario, mapping, request);
				}
				//***********************INICIO CAMBIO POR TAREA 52846 *******************************
				//Permite distribuir las solicitudes por porcentaje desde la bï¿½squeda avanzada
				else if(estado.equals("distribucionSolicitudesPorPorcentaje"))
				{
					return this.accionDistribucionSolicitudesPorPorcentaje(con,forma,mundo,paciente,usuario,mapping,request);
				}
				else if(estado.equals("guardarDistribucionSolicitudesPorPorcentaje"))
				{
					return this.accionGuardarDistribucionSolicitudesPorPorcentaje(con,forma,mundo,paciente,usuario,mapping,request);
				}
				//***********************FIN CAMBIO POR TAREA 52846 *******************************
				else
				{
					this.cancelarIngresoProcesoDistribucion(con,  forma, response);
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.closeConnection(con);
					return mapping.findForward("paginaError");
				}
			}
			else
			{
				logger.error("El form no es compatible con el form de DistribucionCuentaForm");
				request.setAttribute("codigoDescripcionError", "errors.formaTipoInvalido");
				return mapping.findForward("paginaError");
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
	 * Carga la Solicitud de Autorizacion
	 */
	
	private ActionForward accionCargarSolicitudAutorizacion(Connection con, DistribucionCuentaForm forma, DistribucionCuenta mundo, PersonaBasica paciente, UsuarioBasico usuario, ActionMapping mapping, HttpServletRequest request)
	{
		logger.info("codigo det cargo >>>>> "+forma.getDistribucionSolicitud().get("codigodetcargo_"+forma.getResponsables().get(0).getSubCuenta()+"_"+ConstantesBD.acronimoNo));
		Utilidades.imprimirMapa(forma.getDistribucionSolicitud());
		forma.resetDetalle();
		forma.setListadoIngresos(mundo.cargarListadoIngresos(con, paciente.getCodigoPersona(), usuario.getCodigoInstitucionInt()));
		forma.setCuerpoDetalle(mundo.cargarServiciosArticulos(con, Utilidades.convertirAEntero(forma.getDistribucionSolicitud().get("codigodetcargo_"+forma.getResponsables().get(0).getSubCuenta()+"_"+ConstantesBD.acronimoNo).toString())));
		forma.setCuerpoDetalleOld((HashMap)forma.getCuerpoDetalle().clone());
		// se carga el encabezado
		forma.setEncabezadoDetalle(cargarEncabezado(forma.getListadoIngresos(),"0"));
		// se valida la autorizacion
		forma.setCuerpoDetalle(validarAutorizaciones(forma.getCuerpoDetalle()));
		Utilidades.imprimirMapa(forma.getCuerpoDetalle());
		return mapping.findForward("solicitudAutorizacion");
	}
	
	/**
	 * Validaciones de las Autorizaciones
	 * @param HashMap mapaOriginal
	 * */
	public static HashMap validarAutorizaciones(HashMap mapaOriginal)
	{
		int numRegistros = Utilidades.convertirAEntero(mapaOriginal.get("numRegistros").toString());
				
		for(int i=0; i<numRegistros; i++)
		{	
			mapaOriginal.put(indicesServiciosArticulos[40]+i,ConstantesBD.acronimoNo);

			if(mapaOriginal.get(indicesServiciosArticulos[29]+i).toString().equals("") ||
					mapaOriginal.get(indicesServiciosArticulos[29]+i).toString().equals(ConstantesIntegridadDominio.acronimoEstadoNegado) || 
							mapaOriginal.get(indicesServiciosArticulos[29]+i).toString().equals(ConstantesIntegridadDominio.acronimoEstadoAnulado))
				mapaOriginal.put(indicesServiciosArticulos[40]+i,ConstantesBD.acronimoSi);
			else if(mapaOriginal.get(indicesServiciosArticulos[29]+i).toString().equals(ConstantesIntegridadDominio.acronimoAutorizado))
			{
				mapaOriginal.put(indicesServiciosArticulos[40]+i,
					Autorizaciones.tieneActivaVigencia(
							mapaOriginal.get("vigencia_"+i).toString(),
							mapaOriginal.get("tipoVigencia_"+i).toString(),
							mapaOriginal.get("fechaFinAutoriza_"+i).toString(),
							mapaOriginal.get(indicesServiciosArticulos[36]+i).toString(),
							mapaOriginal.get(indicesServiciosArticulos[37]+i).toString())?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);												
			}
			
			if(mapaOriginal.get(indicesServiciosArticulos[29]+i).toString().equals(ConstantesIntegridadDominio.acronimoEstadoNegado) || 
				mapaOriginal.get(indicesServiciosArticulos[29]+i).toString().equals(ConstantesIntegridadDominio.acronimoAutorizado))
			{
				InfoDatosString info = Autorizaciones.calcularIncrementoFechaVigencia(
						mapaOriginal.get("vigencia_"+i).toString(),
						mapaOriginal.get("tipoVigencia_"+i).toString(), 
						mapaOriginal.get(indicesServiciosArticulos[36]+i).toString(),
						mapaOriginal.get(indicesServiciosArticulos[37]+i).toString());
				
				mapaOriginal.put(indicesServiciosArticulos[36]+i,info.getCodigo());
				mapaOriginal.put(indicesServiciosArticulos[37]+i,info.getValue());			
			}
			else
			{
				mapaOriginal.put(indicesServiciosArticulos[36]+i,"");
				mapaOriginal.put(indicesServiciosArticulos[37]+i,"");
			}
		}

		return mapaOriginal;		
	}
	
	/**
	 * Metodo encargado de copiar los datos del ingreso
	 * del mapa listado a el mapa encabezado detalle.
	 * @param Datos
	 * @param index
	 * @return
	 */
	public static HashMap cargarEncabezado (HashMap datos,String index)
	{
		HashMap encabezado = new HashMap();
		//id ingreso
		encabezado.put(indicesEncabezadoDetalle[0], datos.get(indicesListado[0]+index));
		//consecutivo ingreso
		encabezado.put(indicesEncabezadoDetalle[1], datos.get(indicesListado[1]+index));
		//codigo centro atencion
		encabezado.put(indicesEncabezadoDetalle[2], datos.get(indicesListado[2]+index));
		//nombre centro atencion
		encabezado.put(indicesEncabezadoDetalle[3], datos.get(indicesListado[3]+index));
		//fecha hora ingreso
		encabezado.put(indicesEncabezadoDetalle[4], datos.get(indicesListado[5]+index));
		//id cuenta
		encabezado.put(indicesEncabezadoDetalle[6], datos.get(indicesListado[9]+index));
		//via ingreso
		encabezado.put(indicesEncabezadoDetalle[7], datos.get(indicesListado[6]+index));
		
		return encabezado;
	}
	
	/**
	 * Mï¿½todo que permite distribuir las solicitudes arrojadas por
	 * la busqueda avanzada, por tipo de distribuciï¿½n por porcentaje
	 * Este cambio fue incluido por la Tarea 52846 
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param paciente
	 * @param usuario
	 * @param mapping
	 * @param request
	 * @return
	 */
	private ActionForward accionGuardarDistribucionSolicitudesPorPorcentaje(Connection con, DistribucionCuentaForm forma, DistribucionCuenta mundo, PersonaBasica paciente, UsuarioBasico usuario, ActionMapping mapping, HttpServletRequest request) throws IPSException
	{
		List<Integer> numeroSolicitudes = new ArrayList<Integer>();
		ActionErrors errores = new ActionErrors(); 
		boolean backupExitoso = false;
		boolean transaccion = false;
		
		try { 
				
			for(int i=0 ; i<Integer.parseInt(forma.getDetSolicitudesPaciente("numRegistros").toString()) ; i++){
				numeroSolicitudes.add(Integer.parseInt(forma.getDetSolicitudesPaciente("solicitud_"+i).toString()));
			}
			
			//Manejo historicos de cargos eliminados en el proceso de liquidacion automatica
			backupExitoso = crearHistoricoCargosXLiquidacionDistribucion(forma, usuario, paciente, ConstantesIntegridadDominio.acronimoManual, numeroSolicitudes,  ConstantesIntegridadDominio.acronimoTipoDistribucionPorcentual);
			
			if(backupExitoso){
				
				//MT5958 manejo de transaccionalidad
				transaccion=UtilidadBD.iniciarTransaccion(con);
						
				if(transaccion) {
								
					boolean exitoso=true;
					String[] excluirResponsables = new String[0];
			
					//se consultan los responsables que no han sido facturados.
					forma.setResponsables(UtilidadesHistoriaClinica.obtenerResponsablesIngreso(con,forma.getCodigoIngreso(),true,excluirResponsables,false, "" /*subCuenta*/,forma.getUltimaViaIngreso()));
					
					HashMap esquemasCirugias=new HashMap();
					int numRegistros=0;
					HashMap distribucionInsertar=new HashMap();
					//recorro cada solicitud.
					for(int a=0;a<Utilidades.convertirAEntero(forma.getDetSolicitudesPaciente("numRegistros")+"");a++)
					{
						if(Integer.parseInt(forma.getDetSolicitudesPaciente("tiposolicitud_"+a)+"")!=ConstantesBD.codigoTipoSolicitudPaquetes)
						{
							//si tiene responsables facturados no se toma en cuenta para la liquidacion.
							if((Utilidades.convertirAEntero(forma.getDetSolicitudesPaciente("numresfactservicio_"+a)+"")+Utilidades.convertirAEntero(forma.getDetSolicitudesPaciente("numresfactarticulo_"+a)+""))==0)
							{
								//si tiene responsables facturados no se toma en cuenta para la liquidacion.
								if((Utilidades.convertirAEntero(forma.getDetSolicitudesPaciente("numresfactservicio_"+a)+"")+Utilidades.convertirAEntero(forma.getDetSolicitudesPaciente("numresfactarticulo_"+a)+""))==0)
								{
									//recorro cada responsable.
									int indiceRespValido=ConstantesBD.codigoNuncaValido;
									
									int numSolicitud=Utilidades.convertirAEntero(forma.getDetSolicitudesPaciente("solicitud_"+a)+"");
									String fechaCalculosCargos=Cargos.obtenerFechaCalculoCargo(con, numSolicitud);
									
									
									for(int j=0;j<forma.getResponsables().size();j++)
									{
										///analizar a que responsable le aplica el filtro.
										if(validoServArtParaResponsable(con,forma,a,j,false,true,false,numSolicitud,usuario,forma.getResponsables().get(j)))
										{
											indiceRespValido=j;
											j=forma.getResponsables().size();
										}
									}
					
									//no es contenido en ninguno. se asigna al ultimo responsable
									if(indiceRespValido==ConstantesBD.codigoNuncaValido)
										indiceRespValido=(forma.getResponsables().size()-1);
									
									double valorTarifaBase=0;
									
									int esquemaTarifario=ConstantesBD.codigoNuncaValido;
									
									if(!UtilidadTexto.isEmpty(forma.getDetSolicitudesPaciente("servicio_"+a)+""))
									{
										esquemaTarifario=forma.getResponsables().get(indiceRespValido).getEsquemaTarifarioServiciosPpalOoriginal(con,forma.getResponsables().get(indiceRespValido).getSubCuenta(),forma.getResponsables().get(indiceRespValido).getContrato(),Utilidades.convertirAEntero(forma.getDetSolicitudesPaciente("servicio_"+a)+""),fechaCalculosCargos, Cargos.obtenerCentroAtencionCargoSolicitud(con,numSolicitud));
										valorTarifaBase=Cargos.obtenerValorTarifaYExcepcion(con, forma.getResponsables().get(indiceRespValido), esquemaTarifario, usuario, Utilidades.convertirAEntero(forma.getDetSolicitudesPaciente("servicio_"+a)+""), true, Utilidades.convertirAEntero(forma.getDetSolicitudesPaciente("solicitud_"+a)+""),fechaCalculosCargos);
									}
									else if(!UtilidadTexto.isEmpty(forma.getDetSolicitudesPaciente("articulo_"+a)+""))
									{
										esquemaTarifario=forma.getResponsables().get(indiceRespValido).getEsquemaTarifarioArticuloPpalOoriginal(con,forma.getResponsables().get(indiceRespValido).getSubCuenta(),forma.getResponsables().get(indiceRespValido).getContrato(),Utilidades.convertirAEntero(forma.getDetSolicitudesPaciente("articulo_"+a)+""),fechaCalculosCargos, Cargos.obtenerCentroAtencionCargoSolicitud(con,numSolicitud));
										valorTarifaBase=Cargos.obtenerValorTarifaYExcepcion(con, forma.getResponsables().get(indiceRespValido), esquemaTarifario, usuario, Utilidades.convertirAEntero(forma.getDetSolicitudesPaciente("articulo_"+a)+""), false, Utilidades.convertirAEntero(forma.getDetSolicitudesPaciente("solicitud_"+a)+""),fechaCalculosCargos);
									}
									
									if(Integer.parseInt(forma.getDetSolicitudesPaciente("tiposolicitud_"+a)+"")==ConstantesBD.codigoTipoSolicitudCirugia&&!UtilidadTexto.isEmpty(forma.getDetSolicitudesPaciente("servicio_"+a)+""))
									{
										if(esquemasCirugias.containsKey("esquema_"+forma.getDetSolicitudesPaciente("solicitud_"+a)+"_"+forma.getDetSolicitudesPaciente("servicio_"+a)+"_"+forma.getDetSolicitudesPaciente("serviciocx_"+a)+"_"+forma.getDetSolicitudesPaciente("detcxhonorarios_"+forma.getIndice())+"_"+forma.getDetSolicitudesPaciente("detascxsalmat_"+forma.getIndice())+""))
										{
											esquemaTarifario=Utilidades.convertirAEntero((esquemasCirugias.get("esquema_"+forma.getDetSolicitudesPaciente("solicitud_"+a)+"_"+forma.getDetSolicitudesPaciente("servicio_"+a)+"_"+forma.getDetSolicitudesPaciente("serviciocx_"+a)+"_"+forma.getDetSolicitudesPaciente("detcxhonorarios_"+forma.getIndice())+"_"+forma.getDetSolicitudesPaciente("detascxsalmat_"+forma.getIndice())+""))+"");
										}
										else
										{
											esquemaTarifario=UtilidadesFacturacion.obtenerEsquemaTarifarioBase(con,forma.getDetSolicitudesPaciente("solicitud_"+a)+"",forma.getDetSolicitudesPaciente("servicio_"+a)+"",forma.getDetSolicitudesPaciente("serviciocx_"+a)+"",forma.getDetSolicitudesPaciente("detcxhonorarios_"+a)+"",forma.getDetSolicitudesPaciente("detascxsalmat_"+a)+"",true,ConstantesBD.acronimoNo);
											esquemasCirugias.put("esquema_"+forma.getDetSolicitudesPaciente("solicitud_"+a)+"_"+forma.getDetSolicitudesPaciente("servicio_"+a)+"_"+forma.getDetSolicitudesPaciente("serviciocx_"+a)+"_"+forma.getDetSolicitudesPaciente("detcxhonorarios_"+forma.getIndice())+"_"+forma.getDetSolicitudesPaciente("detascxsalmat_"+forma.getIndice()),esquemaTarifario+"");
										}
										if(esquemasCirugias.containsKey("valor_"+forma.getDetSolicitudesPaciente("solicitud_"+a)+"_"+forma.getDetSolicitudesPaciente("servicio_"+a)+"_"+forma.getDetSolicitudesPaciente("serviciocx_"+a)+"_"+forma.getDetSolicitudesPaciente("detcxhonorarios_"+a)+"_"+forma.getDetSolicitudesPaciente("detascxsalmat_"+a)))
										{
											valorTarifaBase=UtilidadesFacturacion.obtenerValorUnitarioCargadoConvenioBase(con,forma.getDetSolicitudesPaciente("solicitud_"+a)+"",forma.getDetSolicitudesPaciente("servicio_"+a)+"",forma.getDetSolicitudesPaciente("serviciocx_"+a)+"",forma.getDetSolicitudesPaciente("detcxhonorarios_"+a)+"",forma.getDetSolicitudesPaciente("detascxsalmat_"+a)+"",true,ConstantesBD.acronimoNo);
										}
										else
										{
											valorTarifaBase=UtilidadesFacturacion.obtenerValorUnitarioCargadoConvenioBase(con,forma.getDetSolicitudesPaciente("solicitud_"+a)+"",forma.getDetSolicitudesPaciente("servicio_"+a)+"",forma.getDetSolicitudesPaciente("serviciocx_"+a)+"",forma.getDetSolicitudesPaciente("detcxhonorarios_"+a)+"",forma.getDetSolicitudesPaciente("detascxsalmat_"+a)+"",true,ConstantesBD.acronimoNo);
											esquemasCirugias.put("valor_"+forma.getDetSolicitudesPaciente("solicitud_"+a)+"_"+forma.getDetSolicitudesPaciente("servicio_"+a)+"_"+forma.getDetSolicitudesPaciente("serviciocx_"+a)+"_"+forma.getDetSolicitudesPaciente("detcxhonorarios_"+a)+"_"+forma.getDetSolicitudesPaciente("detascxsalmat_"+a),valorTarifaBase+"");
										}
									}
									
									if(!UtilidadTexto.isEmpty(forma.getDetSolicitudesPaciente("servicio_"+a)+""))
										exitoso=mundo.eliminarSolicitudSubCuentaDetCargoXSolServArt(con,numSolicitud,forma.getDetSolicitudesPaciente("servicio_"+a)+"",forma.getDetSolicitudesPaciente("serviciocx_"+a)+"",forma.getDetSolicitudesPaciente("detcxhonorarios_"+a)+"",forma.getDetSolicitudesPaciente("detascxsalmat_"+a)+"",true,Utilidades.convertirAEntero(forma.getDetSolicitudesPaciente("estadocargo_"+a)+""));
									else if(!UtilidadTexto.isEmpty(forma.getDetSolicitudesPaciente("articulo_"+a)+""))
										exitoso=mundo.eliminarSolicitudSubCuentaDetCargoXSolServArt(con,numSolicitud,forma.getDetSolicitudesPaciente("articulo_"+a)+"","","","",false,Utilidades.convertirAEntero(forma.getDetSolicitudesPaciente("estadocargo_"+a)+""));
					
					
									if(exitoso)
									{
										int cantidadCargada=Utilidades.convertirAEntero(forma.getDetSolicitudesPaciente("cantidad_"+a)+"");
										
										double porcentajeFaltaAsignar=100; //al iniciar falta el 100%
										double valorTotalFaltaAsignar=cantidadCargada*valorTarifaBase;//falta todo el valor por asignar
						
										for(int j=indiceRespValido;j<forma.getResponsables().size();j++)
										{
											boolean cargoAnulInactivo=(forma.getDetSolicitudesPaciente("estadocargo_"+a)+"").trim().equals(ConstantesBD.codigoEstadoFAnulada+"")||(forma.getDetSolicitudesPaciente("estadocargo_"+a)+"").trim().equals(ConstantesBD.codigoEstadoFInactiva+"");
											if(!cargoAnulInactivo||(cargoAnulInactivo&&j==(forma.getResponsables().size()-1)))//incluir las anuladas e inactivas, solo si es el ultimo responsalbe.
											{
												if(valorTotalFaltaAsignar>0)
												{
													boolean puedoDistribuir=true;
													
													//validacion solicitudes pyp
													boolean esSolPyp = Utilidades.esSolicitudPYP(con, numSolicitud); 
													if(esSolPyp)
													{
														if((UtilidadValidacion.esConvenioPYP(con, forma.getResponsables().get(j).getConvenio().getCodigo()+"")&&Utilidades.esActividadSolicitudPYPCubiertaConvenio(con,numSolicitud+"",paciente.getCodigoPersona()+"",forma.getResponsables().get(j).getConvenio().getCodigo()+""))||j==(forma.getResponsables().size()-1))
															puedoDistribuir=true;
														else
															puedoDistribuir=false;
													}
					
													//validar los filtros del responsable o sino asignarlos al ultimo.
													//solo filtrar los filtros de la distribucion
													boolean validoRespo=false;
													if(forma.getTipoDistribucion().equals(ConstantesIntegridadDominio.acronimoTipodistribucionMonto))
														validoRespo=(validoServArtParaResponsable(con,forma,a,j,false,false,true,numSolicitud, usuario,forma.getResponsables().get(j))||j==(forma.getResponsables().size()-1));
													else
														validoRespo=(validoServArtParaResponsable(con,forma,a,j,false,false,false,numSolicitud, usuario,forma.getResponsables().get(j))||j==(forma.getResponsables().size()-1));
													
													if(puedoDistribuir&&validoRespo)
													{
														DtoSubCuentas responsable=forma.getResponsables().get(j);
				
														///si es el ultimo responsable se le asigna el restante.
														double porAsignar=Utilidades.convertirADouble(forma.getDistribucionSolicitudesPorPorcentaje("porcentajeDistribuido_"+responsable.getSubCuenta())+"",true);
														if(j==(forma.getResponsables().size()-1))
															porAsignar=porcentajeFaltaAsignar;
														
														double valTotalCarg=cantidadCargada*valorTarifaBase*porAsignar/100;
														if(j==(forma.getResponsables().size()-1))
															valTotalCarg=valorTotalFaltaAsignar;
														
														double valorTotalPaquetesResponsable = Utilidades.obtenerValorTotalPaquetesResponsable(con, responsable.getSubCuenta());
														boolean validoMonto=(j==(forma.getResponsables().size()-1));
														
														if(forma.getTipoDistribucion().equals(ConstantesIntegridadDominio.acronimoTipodistribucionMonto))
														{
															if(!validoMonto)
															{
																if(Utilidades.convertirADouble(responsable.getMontoAutorizado(),true) > 0)
																	validoMonto = ((Utilidades.convertirADouble(responsable.getMontoAutorizado(),true)-valorTotalPaquetesResponsable-DistribucionCuenta.obtenerValorFacturadoSoatEstatico(con,forma.getCodigoIngreso(),responsable.getConvenio().getCodigo())-Utilidades.convertirADouble(responsable.getValorUtilizadoSoat(),true)-valTotalCarg)>0);
																else
																	validoMonto = true;
															}
														}
														else
															validoMonto=true;
			
														if(validoMonto)
														{
				
															distribucionInsertar.put("porcentaje_"+numRegistros, porAsignar);
				
															porcentajeFaltaAsignar=porcentajeFaltaAsignar-porAsignar;
				
															InfoTarifa descuento=new InfoTarifa();
															
															if(!UtilidadTexto.isEmpty(forma.getDetSolicitudesPaciente("servicio_"+a)+""))
															{
																//en caso de que la cobertura y el descuento sea por cirugia debe hacerse esta validacion  para el serviciocx_
																if(Integer.parseInt(forma.getDetSolicitudesPaciente("tiposolicitud_"+a)+"")==ConstantesBD.codigoTipoSolicitudCirugia&&!UtilidadTexto.isEmpty(forma.getDetSolicitudesPaciente("servicio_"+a)+""))
																{
																	descuento=Cargos.obtenerDescuentoComercialXConvenioServicio(con, Utilidades.convertirAEntero(forma.getDetSolicitudesPaciente("viaingreso_"+a)+""), forma.getDetSolicitudesPaciente("tipopaciente_"+a)+"",responsable.getContrato(), Utilidades.convertirAEntero(forma.getDetSolicitudesPaciente("serviciocx_"+a)+""), usuario.getCodigoInstitucionInt(),fechaCalculosCargos);
																}
																else
																{
																	descuento=Cargos.obtenerDescuentoComercialXConvenioServicio(con, Utilidades.convertirAEntero(forma.getDetSolicitudesPaciente("viaingreso_"+a)+""), forma.getDetSolicitudesPaciente("tipopaciente_"+a)+"",responsable.getContrato(), Utilidades.convertirAEntero(forma.getDetSolicitudesPaciente("servicio_"+a)+""), usuario.getCodigoInstitucionInt(),fechaCalculosCargos);
																}
															}
						
															distribucionInsertar.put("solicitud_"+numRegistros,numSolicitud+"");
															distribucionInsertar.put("cuenta_"+numRegistros,Utilidades.getCuentaSolicitud(con, numSolicitud));
															distribucionInsertar.put("subcuenta_"+numRegistros,responsable.getSubCuenta());
															distribucionInsertar.put("servicio_"+numRegistros, forma.getDetSolicitudesPaciente("servicio_"+a)+"");
															distribucionInsertar.put("articulo_"+numRegistros, forma.getDetSolicitudesPaciente("articulo_"+a)+"");
															distribucionInsertar.put("cantidad_"+numRegistros, cantidadCargada+"");
															distribucionInsertar.put("cubierto_"+numRegistros, ConstantesBD.acronimoSi+"");
															distribucionInsertar.put("tiposolicitud_"+numRegistros, forma.getDetSolicitudesPaciente("tiposolicitud_"+a)+"");
															distribucionInsertar.put("serviciocx_"+numRegistros, forma.getDetSolicitudesPaciente("serviciocx_"+a)+"");
															distribucionInsertar.put("detcxhonorarios_"+numRegistros, forma.getDetSolicitudesPaciente("detcxhonorarios_"+a)+"");
															distribucionInsertar.put("detascxsalmat_"+numRegistros, forma.getDetSolicitudesPaciente("detascxsalmat_"+a)+"");
															distribucionInsertar.put("tipoasocio_"+numRegistros, UtilidadTexto.isEmpty(forma.getDetSolicitudesPaciente("tipoasocio_"+a)+"")?ConstantesBD.codigoNuncaValido:Utilidades.convertirAEntero(forma.getDetSolicitudesPaciente("tipoasocio_"+a)+""));
															distribucionInsertar.put("tipodistribucion_"+numRegistros,ConstantesIntegridadDominio.acronimoTipoDistribucionPorcentual);
															distribucionInsertar.put("paquetizada_"+numRegistros, ConstantesBD.acronimoNo);
															distribucionInsertar.put("solsubcuentapadre_"+numRegistros, "");
															
					
															if(forma.getTipoDistribucion().equals(ConstantesIntegridadDominio.acronimoTipodistribucionMonto))
																responsable.setMontoAutorizado((Utilidades.convertirADouble(responsable.getMontoAutorizado(),true)-valTotalCarg)+"");
															
															distribucionInsertar.put("valortotalcargado_"+numRegistros, valTotalCarg+"");
															valorTotalFaltaAsignar=valorTotalFaltaAsignar-valTotalCarg;
															distribucionInsertar.put("autorizacion_"+numRegistros, responsable.getNroAutorizacion());
															distribucionInsertar.put("valorbasedistribucion_"+numRegistros, valorTarifaBase);
															distribucionInsertar.put("valorunitariocargado_"+numRegistros, valTotalCarg/cantidadCargada);
															distribucionInsertar.put("esquematarifario_"+numRegistros, esquemaTarifario);
															
															if(descuento.getPorcentajes().size()>0)
																distribucionInsertar.put("porcentajedcto_"+numRegistros, descuento.getPorcentajes().get(0));
															else
																distribucionInsertar.put("porcentajedcto_"+numRegistros, "");
															
															distribucionInsertar.put("valorunitariodcto_"+numRegistros, descuento.getValor());
//															
															if(cargoAnulInactivo) 
																distribucionInsertar.put("estado_"+numRegistros, forma.getDetSolicitudesPaciente("estadocargo_"+a)+"");
															else
																distribucionInsertar.put("estado_"+numRegistros, ConstantesBD.codigoEstadoFCargada);
															distribucionInsertar.put("requiereautorizacion_"+numRegistros,ConstantesBD.acronimoNo+"");
															distribucionInsertar.put("convenio_"+numRegistros, responsable.getConvenio().getCodigo());
															distribucionInsertar.put("contrato_"+numRegistros, responsable.getContrato());
															numRegistros++;
														}
													}
												}	
											}
										}
									}
									else
									{
										exitoso=false;
										a=Utilidades.convertirAEntero(forma.getDetSolicitudesPaciente("numRegistros")+"");
									}
								}
							}
						}
					}
					distribucionInsertar.put("numRegistros",numRegistros);
					exitoso=guardarDistribucionMapa(con,distribucionInsertar,mundo,usuario);
					
								if(exitoso){
									UtilidadBD.finalizarTransaccion(con);
								}else {
									UtilidadBD.abortarTransaccion(con);
								}
				}
			}else {
				errores.add("", new ActionMessage("error.distribucion.backupCancelado"));
			}
				
		}catch(IPSException ipse){
			UtilidadBD.abortarTransaccion(con);
			Log4JManager.error(ipse.getMessage(), ipse);
			errores.add("", new ActionMessage(ipse.getErrorCode().toString()));
		}catch(Exception e){
			UtilidadBD.abortarTransaccion(con);
			Log4JManager.error(e.getMessage(), e);
			errores.add("", new ActionMessage(KEY_ERROR_NO_ESPECIFIC, e.getMessage()));
		}
		
		if(!errores.isEmpty()){
			saveErrors(request, errores);
		}
		
		return this.accionEjecutarBusqueda(con,forma,mundo,paciente,usuario,mapping,request);
	}

	/**
	 * Mï¿½todo que permite distribuir las solicitudes arrojadas por
	 * la busqueda avanzada, por tipo de distribuciï¿½n por porcentaje
	 * Este cambio fue incluido por la Tarea 52846 
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param paciente
	 * @param usuario
	 * @param mapping
	 * @param request
	 * @return
	 */
	private ActionForward accionDistribucionSolicitudesPorPorcentaje(Connection con, DistribucionCuentaForm forma, DistribucionCuenta mundo, PersonaBasica paciente, UsuarioBasico usuario, ActionMapping mapping, HttpServletRequest request)
	{
		forma.setEstado("guardarDistribucionSolicitudesPorPorcentaje");
		UtilidadBD.closeConnection(con);
		return mapping.findForward("distribucionSolicitudesPorPorcentaje");
	}
	
	/**
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param paciente
	 * @param usuario
	 * @param mapping
	 * @param request
	 * @return
	 * @author hermorhu
	 */
	private ActionForward accionModificarInformacionGeneral(Connection con, DistribucionCuentaForm forma, DistribucionCuenta mundo, PersonaBasica paciente, UsuarioBasico usuario, ActionMapping mapping, HttpServletRequest request)
	{
		forma.setEstado("guardarModificarInformacionGeneral");
		UtilidadBD.closeConnection(con);
		return mapping.findForward("modificarInformacionGeneral");
	}
	
	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param mapping
	 * @param request
	 * @param paciente
	 * @param usuario
	 * @return
	 */
	private ActionForward accionCargarInfoIngresoConsulta(Connection con, DistribucionCuentaForm forma, DistribucionCuenta mundo, ActionMapping mapping, HttpServletRequest request, PersonaBasica paciente, UsuarioBasico usuario) throws IPSException 
	{
		forma.setResponsablesEliminados(new ArrayList<DtoSubCuentas>());
		int codigoPaciente=paciente.getCodigoPersona();
		forma.setCodigoIngreso(Utilidades.convertirAEntero(forma.getIngresos().get("idingreso_"+forma.getIndiceIngresoSeleccionado())+"",false));
		forma.setConsecutivoIngreso(forma.getIngresos().get("consecutivo_"+forma.getIndiceIngresoSeleccionado())+"");
		forma.setCuenta(Utilidades.convertirAEntero(forma.getIngresos().get("cuenta_"+forma.getIndiceIngresoSeleccionado())+"",false));
		InfoDatosInt tipoCom=Utilidades.obtenerTipoComplejidadCuenta(con,forma.getCuenta());
		forma.setTipoComplejidad(tipoCom.getCodigo());
		forma.setDescTipoComplejidad(tipoCom.getNombre());
		forma.setUltimaViaIngreso(Utilidades.convertirAEntero(Utilidades.obtenerViaIngresoCuenta(con, forma.getCuenta()+"")));
		forma.setConvenioDefectoVia(ViasIngreso.obtenerConvenioDefecto(con,forma.getUltimaViaIngreso()));
			
			//********************SE CARGA EL PACIENTE*******************************************
			ObservableBD observable = (ObservableBD)servlet.getServletContext().getAttribute("observable");
			try 
			{
				paciente.clean();
				paciente.cargar(con, codigoPaciente);
				
				//solo si el ingreso se encuentra en estado abierto, se puede cargar la informacion del ingreso en la sesion.
				//tarea id=25875
				//solo si el ingreso se encuentra en estado abierto o cerrado, se puede cargar la informacion del ingreso en la sesion.
				if((forma.getIngresos().get("estadoingreso_"+forma.getIndiceIngresoSeleccionado())+"").trim().equals(ConstantesIntegridadDominio.acronimoEstadoAbierto)||(forma.getIngresos().get("estadoingreso_"+forma.getIndiceIngresoSeleccionado())+"").trim().equals(ConstantesIntegridadDominio.acronimoEstadoCerrado))
					paciente.cargarPacienteXingreso(con, forma.getCodigoIngreso()+"", usuario.getCodigoInstitucion(), usuario.getCodigoCentroAtencion()+"");
			
				if(forma.isRegistrandoDistribucion())
					DistribucionCuenta.empezarProcesoDistribucionIngreso(con, forma.getCodigoIngreso(), usuario.getLoginUsuario());
				/*
				DistribucionCuenta.empezarProcesoDistribucion(con, forma.getCuenta(), usuario.getLoginUsuario());
				DistribucionCuenta.cuentaProcesoDistribucion(con,forma.getCuenta());
				*/
				
			}
			catch (Exception e) 
			{
				logger.info("Error cargando el paciente.: "+e);
			}
			observable.addObserver(paciente);
			request.getSession().removeAttribute("pacienteActivo");
			request.getSession().setAttribute("pacienteActivo",paciente);
			UtilidadSesion.notificarCambiosObserver(paciente.getCodigoPersona(),servlet.getServletContext());
			//***********************************************************************************
		
		///cargar los responsables del ingreso.
		forma.setResponsables(UtilidadesHistoriaClinica.obtenerResponsablesIngreso(con,forma.getCodigoIngreso(),true,new String[0],false, "" /*subCuenta*/,forma.getUltimaViaIngreso()));
		
		/*****************************************************************************************************************/
		ArrayList<String> aux = new ArrayList<String>();
        //se valida que el paciente es de entidad subcontratada
		//logger.info("\n VOY A ENTRAR A ENTIDAD  CONSULTAR!!!");
	    if (IngresoGeneral.esIngresoComoEntidadSubContratada(con, forma.getCodigoIngreso()).equals(ConstantesBD.acronimoSi))
	    {
	    //	logger.info("\n YA ENTRE A ENTIDAD CONSULTAR!!!");
	    	aux.add("Ingreso de paciente en entidad subcontratada "+EntidadesSubContratadas.getDescripcionEntidadSubXIngreso(con, paciente.getCodigoIngreso()+""));
	    	forma.setMensajes(aux);
	    	//logger.info("\n VOY A SALIR DE ENTIDAD CONSULTAR!!!");
	    }
	    /*****************************************************************************************************************/		
		
		
		for(int a=0;a<forma.getResponsables().size();a++)
		{	///cargar los filtros de la distribucion
			this.accionCargarFiltroDistribucionResponsable(con,forma,mundo,a);
			//obtiene los valores a pagar del convenio y del paciente
			this.accionObtenerValoresConvenioPaciente(con, forma, mundo, a);
		}
		
		
		if(forma.getResponsables().size()>0)
		{
			validarVigenciasTopesContratos(con,forma);
		}

		
		//cargar infomacion Ultima Distribucion.
		this.cargarInfoUltimaDistribucion(forma,mundo.consultarEncabezadoUltimaDistribucion(con,forma.getCodigoIngreso()),usuario);
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
		
	}
	
	
	/**
	 * 
	 * @param con 
	 * @param forma
	 */
	private void validarVigenciasTopesContratos(Connection con, DistribucionCuentaForm forma) 
	{
		Vector contratos= new Vector();
		for(int a=0;a<forma.getResponsables().size();a++)
		{
			contratos.add(forma.getResponsables().get(a).getContrato());
		}

		HashMap contratosVencidosMap= Contrato.obtenerContratosVencidos(con, contratos);
		int cont=0;
		for(int a=0; a<Utilidades.convertirAEntero(contratosVencidosMap.get("numRegistros")+""); a++)
		{
			forma.getVigenciaTopesContratos().put("warning_"+cont, "EL CONTRATO DEL CONVENIO  "+contratosVencidosMap.get("nombreconvenio_"+a)+" VENCIDO. Fecha Inicial "+contratosVencidosMap.get("fechainicial_"+a)+" Fecha Final "+contratosVencidosMap.get("fechafinal_"+a));
			cont++;
		}		
		
		HashMap contratosTopeCompleto= Contrato.obtenerContratosTopesCompletos(con, contratos,"");
		
		
		for(int a=0; a<Utilidades.convertirAEntero(contratosTopeCompleto.get("numRegistros")+""); a++)
		{
			forma.getVigenciaTopesContratos().put("warning_"+cont, "VALOR DEL CONTRATO PARA EL CONVENIO  "+contratosTopeCompleto.get("nombreconvenio_"+a)+" YA SE COMPLETO, Total Contrato "+UtilidadTexto.formatearValores(contratosTopeCompleto.get("valor_"+a)+"")+" Total Acumulado Anterior "+UtilidadTexto.formatearValores(contratosTopeCompleto.get("acumulado_"+a)+""));
			cont++;

		}
		forma.getVigenciaTopesContratos().put("numRegistros", cont+"");
	}

	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mapping
	 * @param usuario
	 * @return
	 */
	private ActionForward accionGuardarDistribucionDetalle(Connection con, DistribucionCuentaForm forma, DistribucionCuenta mundo, PersonaBasica paciente, UsuarioBasico usuario, ActionMapping mapping, HttpServletRequest request) throws IPSException
	{
		ActionErrors errores = new ActionErrors();
		if(forma.getResponsables() != null && !forma.getResponsables().isEmpty()){
			for(DtoSubCuentas responsable:forma.getResponsables()){
				String subCuenta=responsable.getSubCuenta();
				if(subCuenta != null && !subCuenta.trim().isEmpty()){
					String keyPorceDescS="porcentajedcto_"+subCuenta+"_"+ConstantesBD.acronimoSi;
					String keyPorceDescN="porcentajedcto_"+subCuenta+"_"+ConstantesBD.acronimoNo;
					Object porcenDescS=forma.getDistribucionSolicitud(keyPorceDescS);
					Object porcenDescN=forma.getDistribucionSolicitud(keyPorceDescN);
					Double max=100D;
					try{
						if(porcenDescS != null && !porcenDescS.toString().trim().isEmpty()){
							Double valorS=Double.valueOf(porcenDescS.toString().trim());
							if(valorS.doubleValue() > max.doubleValue()){
								errores.add("Porcentaje Descuento Invalido", new ActionMessage("errors.MenorIgualQue", new Object[]{"% Desto", "100"}));
							}
						}
						if(porcenDescN != null && !porcenDescN.toString().trim().isEmpty()){
							Double valorN=Double.valueOf(porcenDescN.toString().trim());
							if(valorN.doubleValue() > max.doubleValue()){
								errores.add("Porcentaje Descuento Invalido", new ActionMessage("errors.MenorIgualQue", new Object[]{"% Desto", "100"}));
							}
						}
					}
					catch (Exception e) {
						errores.add("Porcentaje Descuento Invalido", new ActionMessage("errors.MenorIgualQue", new Object[]{"% Desto", "100"}));
						Log4JManager.error(e);
					}
					if(!errores.isEmpty()){
						break;
					}
				}
			}
		}
		
		if(!errores.isEmpty()){
			saveErrors(request, errores);
			return mapping.findForward("detalleSolicitud");
		}
		
		boolean backupExitoso = false;
		boolean transaccion = false;
		List<Integer> numeroSolicitudes = new ArrayList<Integer>();
		numeroSolicitudes.add(Integer.parseInt(forma.getDetSolicitudesPaciente("solicitud_"+forma.getIndice()).toString()));
	
		try{
			//Manejo historicos de cargos eliminados en el proceso de ditribucion
			backupExitoso = crearHistoricoCargosXLiquidacionDistribucion(forma, usuario, paciente, ConstantesIntegridadDominio.acronimoManual, numeroSolicitudes, forma.getDetSolicitudesPaciente("tipodistribucion_"+forma.getIndice()).toString());
			
			if(backupExitoso){
			
			transaccion = UtilidadBD.iniciarTransaccion(con);
			
				///si se cambio el tipo de distribucion anterior (De cantidad a diferente o de monto-porcentaje a cantidad), eliminar todo lo que habï¿½a.
				if(Utilidades.convertirAEntero(forma.getDetSolicitudesPaciente("tiposolicitud_"+forma.getIndice())+"")!=ConstantesBD.codigoTipoSolicitudPaquetes)
					transaccion = accionGuardarDistribucionDetalleCasoNoPaquete(con,forma,mundo,paciente,usuario);
				else {
					//solo se puede para porcentual - monto
					if((forma.getDetSolicitudesPaciente("tipodistribucion_"+forma.getIndice())+"").trim().equals(ConstantesIntegridadDominio.acronimoTipoDistribucionPorcentual)||(forma.getDetSolicitudesPaciente("tipodistribucion_"+forma.getIndice())+"").trim().equals(ConstantesIntegridadDominio.acronimoTipodistribucionMonto))
						transaccion = accionGuardarDistribucionDetalleCasoPaquete(con,forma,mundo,paciente,usuario);
				}
			
			}else {
				errores.add("", new ActionMessage("error.distribucion.backupCancelado"));
			}
			
		}catch(IPSException ipse){
			transaccion = false;
			UtilidadBD.abortarTransaccion(con);
			Log4JManager.error(ipse.getMessage(), ipse);
			errores.add("", new ActionMessage(ipse.getErrorCode().toString()));
		}catch(Exception e){
			transaccion = false;
			UtilidadBD.abortarTransaccion(con);
			Log4JManager.error(e.getMessage(), e);
			errores.add("", new ActionMessage(KEY_ERROR_NO_ESPECIFIC, e.getMessage()));
		}
		
		if(!errores.isEmpty()){
			saveErrors(request, errores);
		}
		
		if(transaccion)
		{
			UtilidadBD.finalizarTransaccion(con);
			forma.setDetSolicitudesPaciente("tipodistribucionoriginal_"+forma.getIndice(),forma.getDetSolicitudesPaciente("tipodistribucion_"+forma.getIndice()));
			forma.setMostrarMensaje(new ResultadoBoolean(true, "Distribuciï¿½n Realizada Exitosamente."));
			logger.info("===>Mapa Mensajes Justificaciï¿½n: "+forma.getJustificacionNoPosMap());
			return this.accionDetalleSolicitud(con,forma,mundo,paciente,usuario,mapping,request);
			///////mensaje
		}
		else
			UtilidadBD.abortarTransaccion(con);
		
		forma.setMostrarMensaje(new ResultadoBoolean(true,"Distribuciï¿½n No Realizada."));
		logger.info("===>Mapa Mensajes Justificaciï¿½n: "+forma.getJustificacionNoPosMap());
		UtilidadBD.closeConnection(con);
		return mapping.findForward("detalleSolicitud");
	}

	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param paciente
	 * @param usuario
	 * @return
	 */
	private boolean accionGuardarDistribucionDetalleCasoPaquete(Connection con, DistribucionCuentaForm forma, DistribucionCuenta mundo, PersonaBasica paciente, UsuarioBasico usuario) throws IPSException 
	{
		boolean transaccion=true;
		
		try {
		
			int numeroSolicitud=Utilidades.convertirAEntero(forma.getDetSolicitudesPaciente("solicitud_"+forma.getIndice())+"");
			int viaIngreso=Utilidades.convertirAEntero(forma.getDetSolicitudesPaciente("viaingreso_"+forma.getIndice())+"");
			int codigoEstadoHC=Utilidades.convertirAEntero(forma.getDetSolicitudesPaciente("codestadohc_"+forma.getIndice())+"");
			boolean dejarPendiente=false;
			if(codigoEstadoHC==ConstantesBD.codigoEstadoHCSolicitada)
				dejarPendiente=true;
			String cuenta=""+Utilidades.getCuentaSolicitud(con, numeroSolicitud);
			
			HashMap detCargoOrginial=new HashMap();
			HashMap solSubcuentaOriginal=new HashMap();
	
			////si cambia el tipo de distribucion eliminar la distribucion antigua.
			//siempre eliminar
			for(int a=0;a<forma.getResponsables().size();a++)
			{
				if((forma.getDistribucionSolicitud("tiporegistro_"+forma.getResponsables().get(a).getSubCuenta()+"_"+ConstantesBD.acronimoNo)+"").equals("BD"))
				{
					detCargoOrginial=mundo.consultarDetlleCargoDetPaqueteOrginal(con,forma.getDistribucionSolicitud("codigodetcargo_"+forma.getResponsables().get(a).getSubCuenta()+"_"+ConstantesBD.acronimoNo)+"");
					solSubcuentaOriginal=mundo.consultarSolSubCuentaDetPaqueteOrginal(con,forma.getDistribucionSolicitud("codsolsubcuenta_"+forma.getResponsables().get(a).getSubCuenta()+"_"+ConstantesBD.acronimoNo)+"");
	
					transaccion=mundo.eliminarDetalleCargoDetallePaquete(con,Utilidades.convertirAEntero(forma.getDistribucionSolicitud("codigodetcargo_"+forma.getResponsables().get(a).getSubCuenta()+"_"+ConstantesBD.acronimoNo)+""));
					if(transaccion)
					{
						transaccion=mundo.eliminarSolicitudSubCuentaDetallePaquete(con,forma.getDistribucionSolicitud("codsolsubcuenta_"+forma.getResponsables().get(a).getSubCuenta()+"_"+ConstantesBD.acronimoNo)+"");
					}
					
					if(transaccion)
					{
						transaccion=Cargos.eliminarDetalleCargoXCodigoDetalle(con,Utilidades.convertirAEntero(forma.getDistribucionSolicitud("codigodetcargo_"+forma.getResponsables().get(a).getSubCuenta()+"_"+ConstantesBD.acronimoNo)+""));
					}
					if(transaccion)
					{
						transaccion=mundo.eliminarSolicitudSubCuenta(con,forma.getDistribucionSolicitud("codsolsubcuenta_"+forma.getResponsables().get(a).getSubCuenta()+"_"+ConstantesBD.acronimoNo)+"");
					}
				}
				forma.setDistribucionSolicitud("tiporegistro_"+forma.getResponsables().get(a).getSubCuenta()+"_"+ConstantesBD.acronimoNo, "MEM");
			}
			
			int servicio=ConstantesBD.codigoNuncaValido;
			int articulo=ConstantesBD.codigoNuncaValido;
			
			////////valida la cobertura del servicio del paquete.
			if(!UtilidadTexto.isEmpty(forma.getDetSolicitudesPaciente("servicio_"+forma.getIndice())+""))
			{
				//Es un paquete
				servicio=Utilidades.convertirAEntero(forma.getDetSolicitudesPaciente("servicio_"+forma.getIndice())+"");
			}
			
			//aunque esta validacion aplica solo para los servicios de procedimiento, lo pongo tambien acï¿½ por si algun dia aplian el concepto para todos los servicios incluyendo paquetes.
			if(!UtilidadTexto.isEmpty(forma.getDetSolicitudesPaciente("servicio_"+forma.getIndice())+""))
			{
				if(UtilidadValidacion.esServicioViaIngresoCargoProceso(con, viaIngreso+"", forma.getDetSolicitudesPaciente("servicio_"+forma.getIndice())+"", usuario.getCodigoInstitucion())
			    	|| UtilidadValidacion.esServicioViaIngresoCargoSolicitud(con, viaIngreso+"",forma.getDetSolicitudesPaciente("servicio_"+forma.getIndice())+"", usuario.getCodigoInstitucion()))
			    {
			    	dejarPendiente=false;
			    }
			}
			
			String cubierto=ConstantesBD.acronimoSi;
			String requiereAutorizacion=ConstantesBD.acronimoNo;
			if(!(forma.getDetSolicitudesPaciente("tipodistribucion_"+forma.getIndice())+"").trim().equals(ConstantesIntegridadDominio.acronimoTipoDistribucionPorcentual))
			{
				InfoResponsableCobertura infoCobertura=new InfoResponsableCobertura();
				infoCobertura=Cobertura.validacionCoberturaServicio(con,forma.getCodigoIngreso()+"", viaIngreso, forma.getDetSolicitudesPaciente("tipopaciente_"+forma.getIndice())+"",servicio, usuario.getCodigoInstitucionInt(),false, "" /*subCuentaCoberturaOPCIONAL*/);
				cubierto=infoCobertura.getInfoCobertura().getIncluidoStr();
				requiereAutorizacion=infoCobertura.getInfoCobertura().getRequiereAutorizacionStr();
			}
			
			//***************INICIO CONSULTAMOS Y ELIMINAMOS LOS RESPONSABLES DE LA JUSTIFICACION****************************
			int codigoJustificacionSolicitud = ConstantesBD.codigoNuncaValido;
			double codigoJustificacionPendiente = ConstantesBD.codigoNuncaValido;
			
			if(!UtilidadTexto.isEmpty(forma.getDetSolicitudesPaciente("servicio_"+forma.getIndice())+""))
				codigoJustificacionSolicitud = mundo.eliminarResponsablesJustificacionSolicitud(con, numeroSolicitud, forma.getDetSolicitudesPaciente("servicio_"+forma.getIndice())+"", false);
			else
				codigoJustificacionSolicitud = mundo.eliminarResponsablesJustificacionSolicitud(con, numeroSolicitud, forma.getDetSolicitudesPaciente("articulo_"+forma.getIndice())+"", true);
			logger.info("===>Cï¿½digo Justificaciï¿½n Solicitud Eliminados Responsables: "+codigoJustificacionSolicitud);
			
			// Si no se encuentra una justificaciï¿½n diligenciada se busca el codigo de la justificacion pendiente
			if(codigoJustificacionSolicitud==ConstantesBD.codigoNuncaValido){
				if(!UtilidadTexto.isEmpty(forma.getDetSolicitudesPaciente("servicio_"+forma.getIndice())+""))
					codigoJustificacionPendiente = UtilidadJustificacionPendienteArtServ.eliminarResponsablesJustificacionPendiente(con, numeroSolicitud, Utilidades.convertirAEntero(forma.getDetSolicitudesPaciente("servicio_"+forma.getIndice())+""), false);
				else
					codigoJustificacionPendiente = UtilidadJustificacionPendienteArtServ.eliminarResponsablesJustificacionPendiente(con, numeroSolicitud, Utilidades.convertirAEntero(forma.getDetSolicitudesPaciente("articulo_"+forma.getIndice())+""), true);
			}
			//***************FIN CONSULTAMOS Y ELIMINAMOS LOS RESPONSABLES DE LA JUSTIFICACION****************************
			
			/////se recorre responsable por responsable.
			for(int a=0;a<forma.getResponsables().size();a++)
			{
				//if(!UtilidadTexto.getBoolean(forma.getResponsables().get(a).getFacturado()))
				//si el responsable esta facturado quiere decir que tiene facturacion parcial.
				{
					if((Utilidades.convertirADouble(forma.getDistribucionSolicitud("valortotalcargado_"+forma.getResponsables().get(a).getSubCuenta()+"_"+ConstantesBD.acronimoNo)+"")>0))
					{
						double solicitudSubCuenta = ConstantesBD.codigoNuncaValido;
						solicitudSubCuenta = mundo.insertarSolicitudSubCuenta(	con,numeroSolicitud,Integer.parseInt(cuenta),forma.getResponsables().get(a).getSubCuenta(),servicio+"","" /*codigoArticulo*/,(Utilidades.convertirAEntero(forma.getDistribucionSolicitud("cantidad_"+forma.getResponsables().get(a).getSubCuenta())+"")),cubierto,(Utilidades.convertirAEntero(forma.getDetSolicitudesPaciente("tiposolicitud_"+forma.getIndice())+"")),forma.getDetSolicitudesPaciente("serviciocx_"+forma.getIndice())+"",forma.getDetSolicitudesPaciente("detcxhonorarios_"+forma.getIndice())+"",forma.getDetSolicitudesPaciente("detascxsalmat_"+forma.getIndice())+"",UtilidadTexto.isEmpty(forma.getDetSolicitudesPaciente("tipoasocio_"+forma.getIndice())+"")?ConstantesBD.codigoNuncaValido:Utilidades.convertirAEntero(forma.getDetSolicitudesPaciente("tipoasocio_"+forma.getIndice())+""),forma.getDetSolicitudesPaciente("tipodistribucion_"+forma.getIndice())+"",usuario.getLoginUsuario(),forma.getDistribucionSolicitud("porcentaje_"+forma.getResponsables().get(a).getSubCuenta())+"",forma.getDistribucionSolicitud("valortotalcargado_"+forma.getResponsables().get(a).getSubCuenta())+"",forma.getDetSolicitudesPaciente("paquetizada_"+forma.getIndice())+"",forma.getDetSolicitudesPaciente("solsubcuentapadre_"+forma.getIndice())+"");
						
						///insertar el cargo con la tarifa calculada del primer responsable, 
						DtoDetalleCargo detCargo=new DtoDetalleCargo();
						detCargo.setTipoDistribucion(forma.getDetSolicitudesPaciente("tipodistribucion_"+forma.getIndice())+"");
						//detCargo.setNumeroAutorizacion(forma.getDistribucionSolicitud("autorizacion_"+forma.getResponsables().get(a).getSubCuenta()+"_"+ConstantesBD.acronimoNo)+"");
						detCargo.setCantidadCargada(Utilidades.convertirAEntero(forma.getDistribucionSolicitud("cantidad_"+forma.getResponsables().get(a).getSubCuenta()+"_"+ConstantesBD.acronimoNo)+""));
						detCargo.setPorcentajeCargado(Utilidades.convertirADouble(forma.getDistribucionSolicitud("porcentaje_"+forma.getResponsables().get(a).getSubCuenta()+"_"+ConstantesBD.acronimoNo)+""));
						detCargo.setValorUnitarioTarifa(Utilidades.convertirADouble(forma.getDetSolicitudesPaciente("valorbasedistribucion_"+forma.getIndice())+""));
						detCargo.setValorTotalCargado(Utilidades.convertirADouble(forma.getDistribucionSolicitud("valortotalcargado_"+forma.getResponsables().get(a).getSubCuenta()+"_"+ConstantesBD.acronimoNo)+""));
						detCargo.setValorUnitarioCargado(detCargo.getValorTotalCargado()/detCargo.getCantidadCargada());
						detCargo.setCodigoEsquemaTarifario(Utilidades.convertirAEntero(forma.getDistribucionSolicitud("esquematarifario_"+forma.getResponsables().get(a).getSubCuenta()+"_"+ConstantesBD.acronimoNo)+""));
						detCargo.setPorcentajeDescuento(Utilidades.convertirADouble(forma.getDistribucionSolicitud("porcentajedcto_"+forma.getResponsables().get(a).getSubCuenta()+"_"+ConstantesBD.acronimoNo)+""));
						detCargo.setValorUnitarioDescuento(Utilidades.convertirADouble(forma.getDistribucionSolicitud("valorunitariodcto_"+forma.getResponsables().get(a).getSubCuenta()+"_"+ConstantesBD.acronimoNo)+""));
						if(dejarPendiente)
							detCargo.setEstado(ConstantesBD.codigoEstadoFPendiente);
						else
							detCargo.setEstado(ConstantesBD.codigoEstadoFCargada);
						detCargo.setCubierto(cubierto);
						detCargo.setRequiereAutorizacion(requiereAutorizacion);
						
						///datos requeridos.
						detCargo.setCodigoSubcuenta(forma.getResponsables().get(a).getSubCuentaDouble());
						detCargo.setCodigoConvenio(forma.getResponsables().get(a).getConvenio().getCodigo());
						detCargo.setCodigoEsquemaTarifario(Utilidades.convertirAEntero(forma.getDistribucionSolicitud("esquematarifario_"+forma.getResponsables().get(a).getSubCuenta()+"_"+ConstantesBD.acronimoNo)+""));
						detCargo.setNumeroSolicitud(numeroSolicitud);
						detCargo.setCodigoServicio(servicio);
						detCargo.setCodigoArticulo(articulo);
						detCargo.setCodigoServicioCx(Utilidades.convertirAEntero(forma.getDetSolicitudesPaciente("serviciocx_"+forma.getIndice())+""));
						detCargo.setDetCxHonorarios(Utilidades.convertirAEntero(forma.getDetSolicitudesPaciente("detcxhonorarios_"+forma.getIndice())+""));
						detCargo.setDetAsocioCxSalasMat(Utilidades.convertirAEntero(forma.getDetSolicitudesPaciente("detascxsalmat_"+forma.getIndice())+""));
						detCargo.setCodigoTipoAsocio(Utilidades.convertirAEntero(forma.getDetSolicitudesPaciente("tipoasocio_"+forma.getIndice())+""));
						detCargo.setFacturado(ConstantesBD.acronimoNo);
						detCargo.setCodigoTipoSolicitud(Utilidades.convertirAEntero(forma.getDetSolicitudesPaciente("tiposolicitud_"+forma.getIndice())+""));
						detCargo.setPaquetizado(ConstantesBD.acronimoNo);
						detCargo.setCodigoSolicitudSubCuenta(solicitudSubCuenta);
						detCargo.setCodigoContrato(forma.getResponsables().get(a).getContrato());
						
						double codigoDetCargo=Cargos.insertarDetalleCargos(con, detCargo, usuario.getLoginUsuario());
						//solicitudSubCuenta;
						transaccion=codigoDetCargo>0;
						if(transaccion)
						{
							double valor=Utilidades.convertirAEntero(forma.getDetSolicitudesPaciente("cantidad_"+forma.getIndice())+"")*Utilidades.convertirADouble(forma.getDetSolicitudesPaciente("valorbasedistribucion_"+forma.getIndice())+"");
							double porc=Utilidades.convertirADouble(forma.getDistribucionSolicitud("valortotalcargado_"+forma.getResponsables().get(a).getSubCuenta()+"_"+ConstantesBD.acronimoNo)+"")*100/valor;
							transaccion=mundo.insertarDetalleCargoSolSubDetPaquete(con, detCargoOrginial,solSubcuentaOriginal,codigoDetCargo,solicitudSubCuenta, porc,forma.getResponsables().get(a).getSubCuenta(),forma.getResponsables().get(a).getConvenio().getCodigo(),usuario.getLoginUsuario(),forma.getDetSolicitudesPaciente("tipodistribucion_"+forma.getIndice())+"");
						}
						//************Justificacion No Pos Servicio******************//
						this.JustificacionNoPosServicios(con, forma, usuario, servicio, numeroSolicitud, a, forma.getResponsables().get(a).getConvenio().getCodigo(), codigoJustificacionSolicitud, codigoJustificacionPendiente);
						//***********************************************************//
					}
					if(!transaccion)
						a=forma.getResponsables().size();
				}
			}
		}catch (IPSException ipsme) {
			throw ipsme;
		}
		catch (Exception e) {
			Log4JManager.error(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
		return transaccion;
		
	}

	/**
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param paciente
	 * @param usuario
	 * @return
	 */
	private boolean accionGuardarDistribucionDetalleCasoNoPaquete(Connection con, DistribucionCuentaForm forma, DistribucionCuenta mundo, PersonaBasica paciente, UsuarioBasico usuario) throws IPSException
	{
		boolean transaccion=true;
		
		try{
		
			int numeroSolicitud = Utilidades.convertirAEntero(forma.getDetSolicitudesPaciente("solicitud_"+forma.getIndice())+"");
			int viaIngreso = Utilidades.convertirAEntero(forma.getDetSolicitudesPaciente("viaingreso_"+forma.getIndice())+"");
			int codigoEstadoHC = Utilidades.convertirAEntero(forma.getDetSolicitudesPaciente("codestadohc_"+forma.getIndice())+"");
			
			boolean dejarPendiente=false;
			if(codigoEstadoHC==ConstantesBD.codigoEstadoHCSolicitada)
				dejarPendiente=true;
			String cuenta=""+Utilidades.getCuentaSolicitud(con, numeroSolicitud);
			
			String fechaCalculosCargos=Cargos.obtenerFechaCalculoCargo(con, numeroSolicitud);
			
			if(!(forma.getDetSolicitudesPaciente("tipodistribucion_"+forma.getIndice())+"").trim().equals((forma.getDetSolicitudesPaciente("tipodistribucionoriginal_"+forma.getIndice())+"").trim()))
			{
				for(int a=0;a<forma.getResponsables().size();a++)
				{
					if((forma.getDistribucionSolicitud("tiporegistro_"+forma.getResponsables().get(a).getSubCuenta()+"_"+ConstantesBD.acronimoNo)+"").equals("BD"))
					{
						transaccion=Cargos.eliminarDetalleCargoXCodigoDetalle(con,Utilidades.convertirAEntero(forma.getDistribucionSolicitud("codigodetcargo_"+forma.getResponsables().get(a).getSubCuenta()+"_"+ConstantesBD.acronimoNo)+""));
						if(transaccion)
						{
							transaccion=mundo.eliminarSolicitudSubCuenta(con,forma.getDistribucionSolicitud("codsolsubcuenta_"+forma.getResponsables().get(a).getSubCuenta()+"_"+ConstantesBD.acronimoNo)+"");
						}
					}
					forma.setDistribucionSolicitud("tiporegistro_"+forma.getResponsables().get(a).getSubCuenta()+"_"+ConstantesBD.acronimoNo, "MEM");
				}
			}
			
			String cubierto=ConstantesBD.acronimoSi;
			String requiereAutorizacion=ConstantesBD.acronimoNo;
			
			if(!(forma.getDetSolicitudesPaciente("tipodistribucion_"+forma.getIndice())+"").trim().equals(ConstantesIntegridadDominio.acronimoTipoDistribucionPorcentual))
			{
				InfoResponsableCobertura infoCobertura=new InfoResponsableCobertura();
				if(!UtilidadTexto.isEmpty(forma.getDetSolicitudesPaciente("servicio_"+forma.getIndice())+""))
				{
					//en caso de que la cobertura y el descuento sea por cirugia debe hacerse esta validacion  para el serviciocx_
					int servicio=Utilidades.convertirAEntero(forma.getDetSolicitudesPaciente("servicio_"+forma.getIndice())+"");
					if(Integer.parseInt(forma.getDetSolicitudesPaciente("tiposolicitud_"+forma.getIndice())+"")==ConstantesBD.codigoTipoSolicitudCirugia)
					{
						infoCobertura=Cobertura.validacionCoberturaServicio(con,forma.getCodigoIngreso()+"", viaIngreso, forma.getDetSolicitudesPaciente("tipopaciente_"+forma.getIndice())+"",Utilidades.convertirAEntero(forma.getDetSolicitudesPaciente("serviciocx_"+forma.getIndice())+""), usuario.getCodigoInstitucionInt(),Utilidades.esSolicitudPYP(con, numeroSolicitud), "" /*subCuentaCoberturaOPCIONAL*/);	
					}
					else
					{
						infoCobertura=Cobertura.validacionCoberturaServicio(con,forma.getCodigoIngreso()+"", viaIngreso, forma.getDetSolicitudesPaciente("tipopaciente_"+forma.getIndice())+"",servicio, usuario.getCodigoInstitucionInt(),Utilidades.esSolicitudPYP(con, numeroSolicitud), "" /*subCuentaCoberturaOPCIONAL*/);
					}
				}
				else
				{
					int articulo=Utilidades.convertirAEntero(forma.getDetSolicitudesPaciente("articulo_"+forma.getIndice())+"");
					infoCobertura=Cobertura.validacionCoberturaArticulo(con,forma.getCodigoIngreso()+"", viaIngreso, forma.getDetSolicitudesPaciente("tipopaciente_"+forma.getIndice())+"",articulo, usuario.getCodigoInstitucionInt(), Utilidades.esSolicitudPYP(con, numeroSolicitud));
				}
				cubierto=infoCobertura.getInfoCobertura().getIncluidoStr();
				requiereAutorizacion=infoCobertura.getInfoCobertura().getRequiereAutorizacionStr();
			}
			
			//aunque esta validacion aplica solo para los servicios de procedimiento, lo pongo tambien acï¿½ por si algun dia aplian el concepto para todos los servicios incluyendo paquetes.
			if(!UtilidadTexto.isEmpty(forma.getDetSolicitudesPaciente("servicio_"+forma.getIndice())+""))
			{
				if(UtilidadValidacion.esServicioViaIngresoCargoProceso(con, viaIngreso+"", forma.getDetSolicitudesPaciente("servicio_"+forma.getIndice())+"", usuario.getCodigoInstitucion())
			    	|| UtilidadValidacion.esServicioViaIngresoCargoSolicitud(con, viaIngreso+"",forma.getDetSolicitudesPaciente("servicio_"+forma.getIndice())+"", usuario.getCodigoInstitucion()))
			    {
			    	dejarPendiente=false;
			    }
			}
			
			if(transaccion)
			{
				//***************INICIO CONSULTAMOS Y ELIMINAMOS LOS RESPONSABLES DE LA JUSTIFICACION****************************
				int codigoJustificacionSolicitud = ConstantesBD.codigoNuncaValido;
				double codigoJustificacionPendiente = ConstantesBD.codigoNuncaValido;
				
				if(!UtilidadTexto.isEmpty(forma.getDetSolicitudesPaciente("servicio_"+forma.getIndice())+""))
					codigoJustificacionSolicitud = mundo.eliminarResponsablesJustificacionSolicitud(con, numeroSolicitud, forma.getDetSolicitudesPaciente("servicio_"+forma.getIndice())+"", false);
				else
					codigoJustificacionSolicitud = mundo.eliminarResponsablesJustificacionSolicitud(con, numeroSolicitud, forma.getDetSolicitudesPaciente("articulo_"+forma.getIndice())+"", true);
				
				// Si no se encuentra una justificaciï¿½n diligenciada se busca el codigo de la justificacion pendiente
				if(codigoJustificacionSolicitud==ConstantesBD.codigoNuncaValido){
					if(!UtilidadTexto.isEmpty(forma.getDetSolicitudesPaciente("servicio_"+forma.getIndice())+""))
						codigoJustificacionPendiente = UtilidadJustificacionPendienteArtServ.eliminarResponsablesJustificacionPendiente(con, numeroSolicitud, Utilidades.convertirAEntero(forma.getDetSolicitudesPaciente("servicio_"+forma.getIndice())+""), false);
					else
						codigoJustificacionPendiente = UtilidadJustificacionPendienteArtServ.eliminarResponsablesJustificacionPendiente(con, numeroSolicitud, Utilidades.convertirAEntero(forma.getDetSolicitudesPaciente("articulo_"+forma.getIndice())+""), true);
				}
				//***************FIN CONSULTAMOS Y ELIMINAMOS LOS RESPONSABLES DE LA JUSTIFICACION****************************
				//tipo cantidad.
				if((forma.getDetSolicitudesPaciente("tipodistribucion_"+forma.getIndice())+"").trim().equals(ConstantesIntegridadDominio.acronimoTipoDistribucionCantidad))
				{
					for(int a=0;a<forma.getResponsables().size();a++)
					{
						
						//if(!UtilidadTexto.getBoolean(forma.getResponsables().get(a).getFacturado()))
						//si el responsable esta facturado quiere decir que tiene facturacion parcial.
						{
	
							if((forma.getDistribucionSolicitud("tiporegistro_"+forma.getResponsables().get(a).getSubCuenta()+"_"+ConstantesBD.acronimoNo)+"").equals("BD"))
							{
								
								/////////ELIMINAR LOS DET_CARGOS
								if(Utilidades.convertirAEntero(forma.getDistribucionSolicitud("cantidad_"+forma.getResponsables().get(a).getSubCuenta()+"_"+ConstantesBD.acronimoNo)+"")<=0)
								{
									
									transaccion=Cargos.eliminarDetalleCargoXCodigoDetalle(con,Utilidades.convertirAEntero(forma.getDistribucionSolicitud("codigodetcargo_"+forma.getResponsables().get(a).getSubCuenta()+"_"+ConstantesBD.acronimoNo)+""));
									if(transaccion)
									{
										transaccion=mundo.eliminarSolicitudSubCuenta(con,forma.getDistribucionSolicitud("codsolsubcuenta_"+forma.getResponsables().get(a).getSubCuenta()+"_"+ConstantesBD.acronimoNo)+"");
									}
								}
								/////////MODIFICAR LOS DET_CARGOS
								else
								{
									
									Cargos cargo=new Cargos();
									
									HashMap vo=new HashMap();
									vo.put("porcentaje", forma.getDistribucionSolicitud("porcentaje_"+forma.getResponsables().get(a).getSubCuenta()+"_"+ConstantesBD.acronimoNo)+"");
									vo.put("cantidad", forma.getDistribucionSolicitud("cantidad_"+forma.getResponsables().get(a).getSubCuenta()+"_"+ConstantesBD.acronimoNo)+"");
									vo.put("monto", forma.getDistribucionSolicitud("valortotalcargado_"+forma.getResponsables().get(a).getSubCuenta()+"_"+ConstantesBD.acronimoNo)+"");
									vo.put("tipodistribucion", forma.getDetSolicitudesPaciente("tipodistribucion_"+forma.getIndice())+"");
									vo.put("usuariomodifica", usuario.getLoginUsuario());
									vo.put("codsolsubcuenta", forma.getDistribucionSolicitud("codsolsubcuenta_"+forma.getResponsables().get(a).getSubCuenta()+"_"+ConstantesBD.acronimoNo)+"");
									transaccion=mundo.modificarSolicitudSubcuenta(con,vo);
									
									if(transaccion)
									{
										//servicio
										if(!UtilidadTexto.isEmpty(forma.getDetSolicitudesPaciente("servicio_"+forma.getIndice())+""))
										{
											//en caso de que la cobertura y el descuento sea por cirugia debe hacerse esta validacion  para el serviciocx_
											int servicio=Utilidades.convertirAEntero(forma.getDetSolicitudesPaciente("servicio_"+forma.getIndice())+"");
											if(Integer.parseInt(forma.getDetSolicitudesPaciente("tiposolicitud_"+forma.getIndice())+"")==ConstantesBD.codigoTipoSolicitudCirugia)
											{
												transaccion=Cargos.eliminarDetalleCargoXCodigoDetalle(con,Utilidades.convertirAEntero(forma.getDistribucionSolicitud("codigodetcargo_"+forma.getResponsables().get(a).getSubCuenta()+"_"+ConstantesBD.acronimoNo)+""));
												if(transaccion)
												{
													transaccion=mundo.eliminarSolicitudSubCuenta(con,forma.getDistribucionSolicitud("codsolsubcuenta_"+forma.getResponsables().get(a).getSubCuenta()+"_"+ConstantesBD.acronimoNo)+"");
												}
												//tipo asocio Utilidades.convertirAEntero(forma.getDetSolicitudesPaciente("tipoasocio_"+forma.getIndice())+"")
												//MT 6147 Tipo Distribucion Cantidad Servicion Cx 
												cargo.generarCargoSolicitudesCxYSolSubCuenta(con, numeroSolicitud, cuenta,forma.getResponsables().get(a).getSubCuenta(),servicio+"", cubierto+"", forma.getDetSolicitudesPaciente("serviciocx_"+forma.getIndice())+"",  Utilidades.convertirAEntero(forma.getDetSolicitudesPaciente("tipoasocio_"+forma.getIndice())+""), Utilidades.convertirAEntero(forma.getDistribucionSolicitud("esquematarifario_"+forma.getResponsables().get(a).getSubCuenta()+"_"+ConstantesBD.acronimoNo)+""), usuario, Utilidades.convertirADouble(forma.getDistribucionSolicitud("valortotalcargado_"+forma.getResponsables().get(a).getSubCuenta()+"_"+ConstantesBD.acronimoNo)+""), requiereAutorizacion, usuario.getLoginUsuario(), Utilidades.convertirAEntero(forma.getDetSolicitudesPaciente("detcxhonorarios_"+forma.getIndice())+""),Utilidades.convertirAEntero(forma.getDetSolicitudesPaciente("detascxsalmat_"+forma.getIndice())+""),usuario.getCodigoInstitucionInt(),true /*aplicar mï¿½todo ajuste*/, UtilidadesSalas.esCxPorConsumoMateriales(con,numeroSolicitud), UtilidadesSalas.obtenerCodigoTipoServicioAsocio(con, Utilidades.convertirAEntero(forma.getDetSolicitudesPaciente("tipoasocio_"+forma.getIndice())+"")),Utilidades.convertirADouble(forma.getDistribucionSolicitud("porcentajedcto_"+forma.getResponsables().get(a).getSubCuenta()+"_"+ConstantesBD.acronimoNo)+""),Utilidades.convertirADouble(forma.getDistribucionSolicitud("valorunitariodcto_"+forma.getResponsables().get(a).getSubCuenta()+"_"+ConstantesBD.acronimoNo)+""));
												//************Justificacion No Pos Servicio******************//
												this.JustificacionNoPosServicios(con, forma, usuario, servicio, numeroSolicitud, a, cargo.getDtoDetalleCargo().getCodigoConvenio(), codigoJustificacionSolicitud, codigoJustificacionPendiente);
												//***********************************************************//
											}
											else
											{
												logger.info("\n\n\n\n\nGENERANDO EL CARGO PARA EL SERVICIO .\n\n\n\n\n");
												transaccion=!cargo.generarCargoServicio(con,dejarPendiente,false,numeroSolicitud,viaIngreso,forma.getResponsables().get(a).getContrato(),forma.getTipoComplejidad(),usuario.getCodigoInstitucionInt(),"",usuario.getLoginUsuario(),Utilidades.convertirADouble(forma.getResponsables().get(a).getSubCuenta()),(Utilidades.convertirADouble(forma.getDistribucionSolicitud("codsolsubcuenta_"+forma.getResponsables().get(a).getSubCuenta()+"_"+ConstantesBD.acronimoNo)+"")),cubierto,ConstantesBD.codigoNuncaValidoDouble,ConstantesBD.acronimoNo,(Utilidades.convertirAEntero(forma.getDistribucionSolicitud("cantidad_"+forma.getResponsables().get(a).getSubCuenta()+"_"+ConstantesBD.acronimoNo)+"")),requiereAutorizacion,forma.getDetSolicitudesPaciente("tipodistribucion_"+forma.getIndice())+"",ConstantesBD.codigoNuncaValido,servicio,ConstantesBD.codigoNuncaValido,forma.getResponsables().get(a).getConvenio().getCodigo(),(Utilidades.convertirAEntero(forma.getDistribucionSolicitud("esquematarifario_"+forma.getResponsables().get(a).getSubCuenta()+"_"+ConstantesBD.acronimoNo)+"")),ConstantesBD.codigoNuncaValido,ConstantesBD.codigoNuncaValido,"",/*forma.getDistribucionSolicitud("autorizacion_"+forma.getResponsables().get(a).getSubCuenta()+"_"+ConstantesBD.acronimoNo)+"",*/Utilidades.convertirADouble(forma.getDistribucionSolicitud("porcentajedcto_"+forma.getResponsables().get(a).getSubCuenta()+"_"+ConstantesBD.acronimoNo)+""),Utilidades.convertirADouble(forma.getDistribucionSolicitud("valorunitariodcto_"+forma.getResponsables().get(a).getSubCuenta()+"_"+ConstantesBD.acronimoNo)+""),false,true, ""/*esPortatil*/,false,fechaCalculosCargos, 0 /*porcentajeDctoPromocionServicio*/, 
														BigDecimal.ZERO /*valorDescuentoPromocionServicio*/, 
														0 /*porcentajeHonorarioPromocionServicio*/, 
														BigDecimal.ZERO /*valorHonorarioPromocionServicio*/,
														0/*programa*/,
														0/*porcentajeDctoBono*/,
														BigDecimal.ZERO/*valorDescuentoBono*/, 
														0/*porcentajeDctoOdontologico*/, 
														BigDecimal.ZERO/*valorDescuentoOdontologico*/,
														0/*detallePaqueteOdonConvenio*/).getTieneErroresCodigo();
												//************Justificacion No Pos Servicio******************//
												this.JustificacionNoPosServicios(con, forma, usuario, servicio, numeroSolicitud, a, cargo.getDtoDetalleCargo().getCodigoConvenio(), codigoJustificacionSolicitud, codigoJustificacionPendiente);
												//***********************************************************//
											}
										}
										else
										{
											int articulo=Utilidades.convertirAEntero(forma.getDetSolicitudesPaciente("articulo_"+forma.getIndice())+"");
											logger.info("\n\n\n\n\nGENERANDO EL CARGO PARA EL ARTICULO .\n\n\n\n\n");
											transaccion=!cargo.generarCargoArticulo(con,
													dejarPendiente,
													numeroSolicitud,
													forma.getResponsables().get(a).getContrato(),
													articulo,
													viaIngreso,
													forma.getTipoComplejidad(),
													usuario.getCodigoInstitucionInt(), 
													Utilidades.convertirADouble(forma.getResponsables().get(a).getSubCuenta()),
													(Utilidades.convertirADouble(forma.getDistribucionSolicitud("codsolsubcuenta_"+forma.getResponsables().get(a).getSubCuenta()+"_"+ConstantesBD.acronimoNo)+"")),
													cubierto+"", 
													ConstantesBD.acronimoNo,
													ConstantesBD.codigoNuncaValido,
													requiereAutorizacion,
													forma.getDetSolicitudesPaciente("tipodistribucion_"+forma.getIndice())+"",
													(Utilidades.convertirAEntero(forma.getDistribucionSolicitud("esquematarifario_"+forma.getResponsables().get(a).getSubCuenta()+"_"+ConstantesBD.acronimoNo)+"")),
													forma.getResponsables().get(a).getConvenio().getCodigo(),
													ConstantesBD.codigoNuncaValido ,
													ConstantesBD.codigoNuncaValido,
													ConstantesBD.codigoNuncaValido,
													(Utilidades.convertirAEntero(forma.getDistribucionSolicitud("cantidad_"+forma.getResponsables().get(a).getSubCuenta()+"_"+ConstantesBD.acronimoNo)+"")),
													/*forma.getDistribucionSolicitud("autorizacion_"+forma.getResponsables().get(a).getSubCuenta()+"_"+ConstantesBD.acronimoNo)+"",*/
													Utilidades.convertirADouble(forma.getDistribucionSolicitud("porcentajedcto_"+forma.getResponsables().get(a).getSubCuenta()+"_"+ConstantesBD.acronimoNo)+""),
													Utilidades.convertirADouble(forma.getDistribucionSolicitud("valorunitariodcto_"+forma.getResponsables().get(a).getSubCuenta()+"_"+ConstantesBD.acronimoNo)+""),
													usuario.getLoginUsuario(), 
													"",
													false,
													true,
													false /*cancelarInsercionSiExisteError*/,
													fechaCalculosCargos, false /*tarifaNoModificada*/ ).getTieneErroresCodigo();
											//************Justificacion No Pos Articulo******************//
											this.JustificacionNoPosArticulos(con, forma, usuario, articulo, numeroSolicitud, a, cargo.getDtoDetalleCargo().getCodigoConvenio(), codigoJustificacionSolicitud, codigoJustificacionPendiente);
											//***********************************************************//
										}
									}
								}
							}
							///////////INSERTAR LOS DET_CARGOS
							else if((forma.getDistribucionSolicitud("tiporegistro_"+forma.getResponsables().get(a).getSubCuenta()+"_"+ConstantesBD.acronimoNo)+"").equals("MEM") && Utilidades.convertirAEntero(forma.getDistribucionSolicitud("cantidad_"+forma.getResponsables().get(a).getSubCuenta()+"_"+ConstantesBD.acronimoNo)+"")>0)
							{
								Cargos cargo=new Cargos();
								
								//servicio
								if(!UtilidadTexto.isEmpty(forma.getDetSolicitudesPaciente("servicio_"+forma.getIndice())+""))
								{
									//en caso de que la cobertura y el descuento sea por cirugia debe hacerse esta validacion  para el serviciocx_
									int servicio=Utilidades.convertirAEntero(forma.getDetSolicitudesPaciente("servicio_"+forma.getIndice())+"");
									
									if(Integer.parseInt(forma.getDetSolicitudesPaciente("tiposolicitud_"+forma.getIndice())+"")==ConstantesBD.codigoTipoSolicitudCirugia)
									{
	 									cargo.generarCargoSolicitudesCxYSolSubCuenta(con, numeroSolicitud, cuenta,forma.getResponsables().get(a).getSubCuenta(),servicio+"", cubierto, forma.getDetSolicitudesPaciente("serviciocx_"+forma.getIndice())+"",  Utilidades.convertirAEntero(forma.getDetSolicitudesPaciente("tipoasocio_"+forma.getIndice())+""), Utilidades.convertirAEntero(forma.getDistribucionSolicitud("esquematarifario_"+forma.getResponsables().get(a).getSubCuenta()+"_"+ConstantesBD.acronimoNo)+""), usuario, Utilidades.convertirADouble(forma.getDistribucionSolicitud("valortotalcargado_"+forma.getResponsables().get(a).getSubCuenta()+"_"+ConstantesBD.acronimoNo)+""), requiereAutorizacion, usuario.getLoginUsuario(), Utilidades.convertirAEntero(forma.getDetSolicitudesPaciente("detcxhonorarios_"+forma.getIndice())+""),Utilidades.convertirAEntero(forma.getDetSolicitudesPaciente("detascxsalmat_"+forma.getIndice())+""),usuario.getCodigoInstitucionInt(),true /*aplicar metodo ajuste*/, UtilidadesSalas.esCxPorConsumoMateriales(con,numeroSolicitud), UtilidadesSalas.obtenerCodigoTipoServicioAsocio(con, Utilidades.convertirAEntero(forma.getDetSolicitudesPaciente("tipoasocio_"+forma.getIndice())+"")),0,0);
										//************Justificacion No Pos Servicio******************//
										this.JustificacionNoPosServicios(con, forma, usuario, servicio, numeroSolicitud, a, cargo.getDtoDetalleCargo().getCodigoConvenio(), codigoJustificacionSolicitud, codigoJustificacionPendiente);
										//***********************************************************//
									}
									else
									{
										double solicitudSubCuenta = mundo.insertarSolicitudSubCuenta(	con,numeroSolicitud,Integer.parseInt(cuenta),forma.getResponsables().get(a).getSubCuenta(),servicio+"","" /*codigoArticulo*/,(Utilidades.convertirAEntero(forma.getDistribucionSolicitud("cantidad_"+forma.getResponsables().get(a).getSubCuenta()+"_"+ConstantesBD.acronimoNo)+"")),cubierto,(Utilidades.convertirAEntero(forma.getDetSolicitudesPaciente("tiposolicitud_"+forma.getIndice())+"")),forma.getDetSolicitudesPaciente("serviciocx_"+forma.getIndice())+"",forma.getDetSolicitudesPaciente("detcxhonorarios_"+forma.getIndice())+"",forma.getDetSolicitudesPaciente("detascxsalmat_"+forma.getIndice())+"",UtilidadTexto.isEmpty(forma.getDetSolicitudesPaciente("tipoasocio_"+forma.getIndice())+"")?ConstantesBD.codigoNuncaValido:Utilidades.convertirAEntero(forma.getDetSolicitudesPaciente("tipoasocio_"+forma.getIndice())+""),forma.getDetSolicitudesPaciente("tipodistribucion_"+forma.getIndice())+"",usuario.getLoginUsuario(),"","",forma.getDetSolicitudesPaciente("paquetizada_"+forma.getIndice())+"",forma.getDetSolicitudesPaciente("solsubcuentapadre_"+forma.getIndice())+"");
										if(solicitudSubCuenta>0)
										{
											logger.info("\n\n\n\n\nGENERANDO EL CARGO PARA EL SERVICIO .\n\n\n\n\n");
											transaccion=!cargo.generarCargoServicio(con,dejarPendiente,false,numeroSolicitud,viaIngreso,forma.getResponsables().get(a).getContrato(),forma.getTipoComplejidad(),usuario.getCodigoInstitucionInt(),"",usuario.getLoginUsuario(),Utilidades.convertirADouble(forma.getResponsables().get(a).getSubCuenta()),solicitudSubCuenta,cubierto,ConstantesBD.codigoNuncaValidoDouble,ConstantesBD.acronimoNo,(Utilidades.convertirAEntero(forma.getDistribucionSolicitud("cantidad_"+forma.getResponsables().get(a).getSubCuenta()+"_"+ConstantesBD.acronimoNo)+"")),requiereAutorizacion,forma.getDetSolicitudesPaciente("tipodistribucion_"+forma.getIndice())+"",ConstantesBD.codigoNuncaValido,servicio,ConstantesBD.codigoNuncaValido,forma.getResponsables().get(a).getConvenio().getCodigo(),(Utilidades.convertirAEntero(forma.getDistribucionSolicitud("esquematarifario_"+forma.getResponsables().get(a).getSubCuenta()+"_"+ConstantesBD.acronimoNo)+"")),ConstantesBD.codigoNuncaValido,ConstantesBD.codigoNuncaValido,"",/*forma.getDistribucionSolicitud("autorizacion_"+forma.getResponsables().get(a).getSubCuenta()+"_"+ConstantesBD.acronimoNo)+"",*/Utilidades.convertirADouble(forma.getDistribucionSolicitud("porcentajedcto_"+forma.getResponsables().get(a).getSubCuenta()+"_"+ConstantesBD.acronimoNo)+""),Utilidades.convertirADouble(forma.getDistribucionSolicitud("valorunitariodcto_"+forma.getResponsables().get(a).getSubCuenta()+"_"+ConstantesBD.acronimoNo)+""),true,true, ""/*esPortatil*/,false,fechaCalculosCargos, 0 /*porcentajeDctoPromocionServicio*/, 
													BigDecimal.ZERO /*valorDescuentoPromocionServicio*/, 
													0 /*porcentajeHonorarioPromocionServicio*/, 
													BigDecimal.ZERO /*valorHonorarioPromocionServicio*/,
													0/*programa*/,
													0/*porcentajeDctoBono*/,
													BigDecimal.ZERO/*valorDescuentoBono*/, 
													0/*porcentajeDctoOdontologico*/, 
													BigDecimal.ZERO/*valorDescuentoOdontologico*/,
													0 /*detallePaqueteOdonConvenio*/).getTieneErroresCodigo();
										}
										else
										{
											transaccion=false;
										}
										//************Justificacion No Pos Servicio******************//
										this.JustificacionNoPosServicios(con, forma, usuario, servicio, numeroSolicitud, a, cargo.getDtoDetalleCargo().getCodigoConvenio(), codigoJustificacionSolicitud, codigoJustificacionPendiente);
										//***********************************************************//
									}
								}
								else
								{
									int articulo=Utilidades.convertirAEntero(forma.getDetSolicitudesPaciente("articulo_"+forma.getIndice())+"");
									double solicitudSubCuenta = mundo.insertarSolicitudSubCuenta(con,numeroSolicitud,Integer.parseInt(cuenta),forma.getResponsables().get(a).getSubCuenta(),"",articulo +"",(Utilidades.convertirAEntero(forma.getDistribucionSolicitud("cantidad_"+forma.getResponsables().get(a).getSubCuenta()+"_"+ConstantesBD.acronimoNo)+"")),cubierto,(Utilidades.convertirAEntero(forma.getDetSolicitudesPaciente("tiposolicitud_"+forma.getIndice())+"")),forma.getDetSolicitudesPaciente("serviciocx_"+forma.getIndice())+"",forma.getDetSolicitudesPaciente("detcxhonorarios_"+forma.getIndice())+"",forma.getDetSolicitudesPaciente("detascxsalmat_"+forma.getIndice())+"",UtilidadTexto.isEmpty(forma.getDetSolicitudesPaciente("tipoasocio_"+forma.getIndice())+"")?ConstantesBD.codigoNuncaValido:Utilidades.convertirAEntero(forma.getDetSolicitudesPaciente("tipoasocio_"+forma.getIndice())+""),forma.getDetSolicitudesPaciente("tipodistribucion_"+forma.getIndice())+"",usuario.getLoginUsuario(),"","",forma.getDetSolicitudesPaciente("paquetizada_"+forma.getIndice())+"",forma.getDetSolicitudesPaciente("solsubcuentapadre_"+forma.getIndice())+"");
									if(solicitudSubCuenta>0)
									{
										logger.info("\n\n\n\n\nGENERANDO EL CARGO PARA EL ARTICULO .\n\n\n\n\n");
										transaccion=!cargo.generarCargoArticulo(con,
												dejarPendiente,
												numeroSolicitud,
												forma.getResponsables().get(a).getContrato(),
												articulo,viaIngreso,
												forma.getTipoComplejidad(),
												usuario.getCodigoInstitucionInt(), 
												Utilidades.convertirADouble(forma.getResponsables().get(a).getSubCuenta()),
												solicitudSubCuenta,
												cubierto,
												ConstantesBD.acronimoNo,
												ConstantesBD.codigoNuncaValido,
												requiereAutorizacion,
												forma.getDetSolicitudesPaciente("tipodistribucion_"+forma.getIndice())+"",
												(Utilidades.convertirAEntero(forma.getDistribucionSolicitud("esquematarifario_"+forma.getResponsables().get(a).getSubCuenta()+"_"+ConstantesBD.acronimoNo)+"")),
												forma.getResponsables().get(a).getConvenio().getCodigo(),
												ConstantesBD.codigoNuncaValido ,  
												ConstantesBD.codigoNuncaValido,
												ConstantesBD.codigoNuncaValido,
												(Utilidades.convertirAEntero(forma.getDistribucionSolicitud("cantidad_"+forma.getResponsables().get(a).getSubCuenta()+"_"+ConstantesBD.acronimoNo)+"")),
												/*forma.getDistribucionSolicitud("autorizacion_"+forma.getResponsables().get(a).getSubCuenta()+"_"+ConstantesBD.acronimoNo)+"",*/
												Utilidades.convertirADouble(forma.getDistribucionSolicitud("porcentajedcto_"+forma.getResponsables().get(a).getSubCuenta()+"_"+ConstantesBD.acronimoNo)+""),
												Utilidades.convertirADouble(forma.getDistribucionSolicitud("valorunitariodcto_"+forma.getResponsables().get(a).getSubCuenta()+"_"+ConstantesBD.acronimoNo)+""),
												usuario.getLoginUsuario(),
												"",
												true,
												true,
												false /*cancelarInsercionSiExisteError*/,
												fechaCalculosCargos,false /*tarifaNoModificada*/).getTieneErroresCodigo();
									}
									else
									{
										transaccion=false;
									}
									//************Justificacion No Pos Articulo******************//
									this.JustificacionNoPosArticulos(con, forma, usuario, articulo, numeroSolicitud, a, cargo.getDtoDetalleCargo().getCodigoConvenio(), codigoJustificacionSolicitud, codigoJustificacionPendiente);
									//**********************************************************//
								}
							}
							if(!transaccion)
								a=forma.getResponsables().size();
						}
						
					}
				}
				
				///////////porcentual o por monto.
				else 
				{
					
					int servicio=ConstantesBD.codigoNuncaValido;
					int articulo=ConstantesBD.codigoNuncaValido;
					if(!UtilidadTexto.isEmpty(forma.getDetSolicitudesPaciente("servicio_"+forma.getIndice())+""))
					{
						//en caso de que la cobertura y el descuento sea por cirugia debe hacerse esta validacion  para el serviciocx_
						servicio=Utilidades.convertirAEntero(forma.getDetSolicitudesPaciente("servicio_"+forma.getIndice())+"");
					}
					else
					{
						articulo=Utilidades.convertirAEntero(forma.getDetSolicitudesPaciente("articulo_"+forma.getIndice())+"");
					}
					
					for(int a=0;a<forma.getResponsables().size();a++)
					{
						//AQUIIIIIIIIIIII
						if((forma.getDistribucionSolicitud("tiporegistro_"+forma.getResponsables().get(a).getSubCuenta()+"_"+ConstantesBD.acronimoNo)+"").equals("BD"))
						{
							if(Utilidades.convertirADouble(forma.getDistribucionSolicitud("valortotalcargado_"+forma.getResponsables().get(a).getSubCuenta()+"_"+ConstantesBD.acronimoNo)+"")<=0)
							{
								transaccion=Cargos.eliminarDetalleCargoXCodigoDetalle(con,Utilidades.convertirAEntero(forma.getDistribucionSolicitud("codigodetcargo_"+forma.getResponsables().get(a).getSubCuenta()+"_"+ConstantesBD.acronimoNo)+""));
								if(transaccion)
								{
									transaccion=mundo.eliminarSolicitudSubCuenta(con,forma.getDistribucionSolicitud("codsolsubcuenta_"+forma.getResponsables().get(a).getSubCuenta()+"_"+ConstantesBD.acronimoNo)+"");
								}
							}
							else
							{
								//modificar el detCargo
								//
								DtoDetalleCargo detCargo=new DtoDetalleCargo();
								detCargo.setCodigoDetalleCargo(Utilidades.convertirADouble(forma.getDistribucionSolicitud("codigodetcargo_"+forma.getResponsables().get(a).getSubCuenta()+"_"+ConstantesBD.acronimoNo)+""));
								ArrayList<DtoDetalleCargo> tempo=Cargos.cargarDetalleCargos(con, detCargo);
								if(tempo.size()>0)
								{   
									detCargo=tempo.get(0);
									detCargo.setTipoDistribucion(forma.getDetSolicitudesPaciente("tipodistribucion_"+forma.getIndice())+"");
									//detCargo.setNumeroAutorizacion(forma.getDistribucionSolicitud("autorizacion_"+forma.getResponsables().get(a).getSubCuenta()+"_"+ConstantesBD.acronimoNo)+"");
									detCargo.setCantidadCargada(Utilidades.convertirAEntero(forma.getDistribucionSolicitud("cantidad_"+forma.getResponsables().get(a).getSubCuenta()+"_"+ConstantesBD.acronimoNo)+""));
									detCargo.setPorcentajeCargado(Utilidades.convertirADouble(forma.getDistribucionSolicitud("porcentaje_"+forma.getResponsables().get(a).getSubCuenta()+"_"+ConstantesBD.acronimoNo)+""));
									detCargo.setValorUnitarioTarifa(Utilidades.convertirADouble(forma.getDetSolicitudesPaciente("valorbasedistribucion_"+forma.getIndice())+""));
									detCargo.setValorTotalCargado(Utilidades.convertirADouble(forma.getDistribucionSolicitud("valortotalcargado_"+forma.getResponsables().get(a).getSubCuenta()+"_"+ConstantesBD.acronimoNo)+""));
									detCargo.setValorUnitarioCargado(detCargo.getValorTotalCargado()/detCargo.getCantidadCargada());
									detCargo.setCodigoEsquemaTarifario(Utilidades.convertirAEntero(forma.getDistribucionSolicitud("esquematarifario_"+forma.getResponsables().get(a).getSubCuenta()+"_"+ConstantesBD.acronimoNo)+""));
									detCargo.setPorcentajeDescuento(Utilidades.convertirADouble(forma.getDistribucionSolicitud("porcentajedcto_"+forma.getResponsables().get(a).getSubCuenta()+"_"+ConstantesBD.acronimoNo)+""));
									detCargo.setValorUnitarioDescuento(Utilidades.convertirADouble(forma.getDistribucionSolicitud("valorunitariodcto_"+forma.getResponsables().get(a).getSubCuenta()+"_"+ConstantesBD.acronimoNo)+""));
									detCargo.setDetCxHonorarios(Utilidades.convertirAEntero(forma.getDetSolicitudesPaciente("detcxhonorarios_"+forma.getIndice())+""));
									detCargo.setDetAsocioCxSalasMat(Utilidades.convertirAEntero(forma.getDetSolicitudesPaciente("detascxsalmat_"+forma.getIndice())+""));
									
									if(dejarPendiente)
										detCargo.setEstado(ConstantesBD.codigoEstadoFPendiente);
									else
										detCargo.setEstado(ConstantesBD.codigoEstadoFCargada);
									detCargo.setCubierto(cubierto);
									detCargo.setRequiereAutorizacion(requiereAutorizacion);
									transaccion=Cargos.updateDetalleCargo(con, detCargo, usuario.getLoginUsuario());
	
									///insertar el consumo para el caso de materiales.
									if((Utilidades.convertirAEntero(forma.getDistribucionSolicitud("tiposolicitud_"+forma.getResponsables().get(a).getSubCuenta()+"_"+ConstantesBD.acronimoNo)+""))==ConstantesBD.codigoTipoSolicitudCirugia)
									{
										Cargos.eliminarDetalleCargoArticuloConsumoXCodigoDetalle(con, detCargo.getCodigoDetalleCargo());
										boolean esCirugiaPorConsumo=UtilidadesSalas.esCxPorConsumoMateriales(con,numeroSolicitud); 
										String tipoServicioAsocio=UtilidadesSalas.obtenerCodigoTipoServicioAsocio(con, Utilidades.convertirAEntero(forma.getDistribucionSolicitud("tipoasocio_"+forma.getResponsables().get(a).getSubCuenta()+"_"+ConstantesBD.acronimoNo)+""));
										double valorTarifa=detCargo.getValorTotalCargado();
										if(tipoServicioAsocio.equals(ConstantesBD.codigoServicioMaterialesCirugia+"") && esCirugiaPorConsumo && valorTarifa>0)
										{	
											double porcentaje=Utilidades.convertirADouble(forma.getDistribucionSolicitud("porcentaje_"+forma.getResponsables().get(a).getSubCuenta()+"_"+ConstantesBD.acronimoNo)+"");
											int servicioCx=Utilidades.convertirAEntero(forma.getDistribucionSolicitud("serviciocx_"+forma.getResponsables().get(a).getSubCuenta()+"_"+ConstantesBD.acronimoNo)+"");
											double codigoDetalleCargo=detCargo.getCodigoDetalleCargo();
											guardarDistribucionConsumoMateriales(con,porcentaje,servicioCx,codigoDetalleCargo,numeroSolicitud,usuario.getLoginUsuario(),(forma.getDetSolicitudesPaciente("tipodistribucion_"+forma.getIndice())+"")+"");
										}
									}
									//fin metodo de consumo materiales.
									
								}
								else
								{
									transaccion=false;
								}
								if(!UtilidadTexto.isEmpty(forma.getDetSolicitudesPaciente("servicio_"+forma.getIndice())+""))
								{
									//************Justificacion No Pos Servicio******************//
									this.JustificacionNoPosServicios(con, forma, usuario, servicio, numeroSolicitud, a, tempo.get(0).getCodigoConvenio(), codigoJustificacionSolicitud, codigoJustificacionPendiente);
									//***********************************************************//
								}
								else
								{
									//************Justificacion No Pos Articulo******************//
									this.JustificacionNoPosArticulos(con, forma, usuario, articulo, numeroSolicitud, a, tempo.get(0).getCodigoConvenio(), codigoJustificacionSolicitud,codigoJustificacionPendiente);
									//***********************************************************//
								}
							}
						}
						///AQUIIIIIIIIIIIIIIIII
						else if((forma.getDistribucionSolicitud("tiporegistro_"+forma.getResponsables().get(a).getSubCuenta()+"_"+ConstantesBD.acronimoNo)+"").equals("MEM") && (Utilidades.convertirADouble(forma.getDistribucionSolicitud("valortotalcargado_"+forma.getResponsables().get(a).getSubCuenta()+"_"+ConstantesBD.acronimoNo)+"")>0))
						{
							double solicitudSubCuenta = ConstantesBD.codigoNuncaValido;
							if(!UtilidadTexto.isEmpty(forma.getDetSolicitudesPaciente("servicio_"+forma.getIndice())+""))
							{
								solicitudSubCuenta = mundo.insertarSolicitudSubCuenta(	con,numeroSolicitud,Integer.parseInt(cuenta),forma.getResponsables().get(a).getSubCuenta(),servicio+"","" /*codigoArticulo*/,(Utilidades.convertirAEntero(forma.getDistribucionSolicitud("cantidad_"+forma.getResponsables().get(a).getSubCuenta()+"_"+ConstantesBD.acronimoNo)+"")),cubierto,(Utilidades.convertirAEntero(forma.getDetSolicitudesPaciente("tiposolicitud_"+forma.getIndice())+"")),forma.getDetSolicitudesPaciente("serviciocx_"+forma.getIndice())+"",forma.getDetSolicitudesPaciente("detcxhonorarios_"+forma.getIndice())+"",forma.getDetSolicitudesPaciente("detascxsalmat_"+forma.getIndice())+"",UtilidadTexto.isEmpty(forma.getDetSolicitudesPaciente("tipoasocio_"+forma.getIndice())+"")?ConstantesBD.codigoNuncaValido:Utilidades.convertirAEntero(forma.getDetSolicitudesPaciente("tipoasocio_"+forma.getIndice())+""),forma.getDetSolicitudesPaciente("tipodistribucion_"+forma.getIndice())+"",usuario.getLoginUsuario(),forma.getDistribucionSolicitud("porcentaje_"+forma.getResponsables().get(a).getSubCuenta()+"_"+ConstantesBD.acronimoNo)+"",forma.getDistribucionSolicitud("valortotalcargado_"+forma.getResponsables().get(a).getSubCuenta()+"_"+ConstantesBD.acronimoNo)+"",forma.getDetSolicitudesPaciente("paquetizada_"+forma.getIndice())+"",forma.getDetSolicitudesPaciente("solsubcuentapadre_"+forma.getIndice())+"");
							}
							else
							{
								solicitudSubCuenta = mundo.insertarSolicitudSubCuenta(	con,numeroSolicitud,Integer.parseInt(cuenta),forma.getResponsables().get(a).getSubCuenta(),"",articulo+"",(Utilidades.convertirAEntero(forma.getDistribucionSolicitud("cantidad_"+forma.getResponsables().get(a).getSubCuenta()+"_"+ConstantesBD.acronimoNo)+"")),cubierto,(Utilidades.convertirAEntero(forma.getDetSolicitudesPaciente("tiposolicitud_"+forma.getIndice())+"")),forma.getDetSolicitudesPaciente("serviciocx_"+forma.getIndice())+"",forma.getDetSolicitudesPaciente("detcxhonorarios_"+forma.getIndice())+"",forma.getDetSolicitudesPaciente("detascxsalmat_"+forma.getIndice())+"",UtilidadTexto.isEmpty(forma.getDetSolicitudesPaciente("tipoasocio_"+forma.getIndice())+"")?ConstantesBD.codigoNuncaValido:Utilidades.convertirAEntero(forma.getDetSolicitudesPaciente("tipoasocio_"+forma.getIndice())+""),forma.getDetSolicitudesPaciente("tipodistribucion_"+forma.getIndice())+"",usuario.getLoginUsuario(),forma.getDistribucionSolicitud("porcentaje_"+forma.getResponsables().get(a).getSubCuenta()+"_"+ConstantesBD.acronimoNo)+"",forma.getDistribucionSolicitud("valortotalcargado_"+forma.getResponsables().get(a).getSubCuenta()+"_"+ConstantesBD.acronimoNo)+"",forma.getDetSolicitudesPaciente("paquetizada_"+forma.getIndice())+"",forma.getDetSolicitudesPaciente("solsubcuentapadre_"+forma.getIndice())+"");
							}
							
							///insertar el cargo con la tarifa calculada del primer responsable, 
							DtoDetalleCargo detCargo=new DtoDetalleCargo();
							detCargo.setTipoDistribucion(forma.getDetSolicitudesPaciente("tipodistribucion_"+forma.getIndice())+"");
							//detCargo.setNumeroAutorizacion(forma.getDistribucionSolicitud("autorizacion_"+forma.getResponsables().get(a).getSubCuenta()+"_"+ConstantesBD.acronimoNo)+"");
							detCargo.setCantidadCargada(Utilidades.convertirAEntero(forma.getDistribucionSolicitud("cantidad_"+forma.getResponsables().get(a).getSubCuenta()+"_"+ConstantesBD.acronimoNo)+""));
							detCargo.setPorcentajeCargado(Utilidades.convertirADouble(forma.getDistribucionSolicitud("porcentaje_"+forma.getResponsables().get(a).getSubCuenta()+"_"+ConstantesBD.acronimoNo)+""));
							detCargo.setValorUnitarioTarifa(Utilidades.convertirADouble(forma.getDetSolicitudesPaciente("valorbasedistribucion_"+forma.getIndice())+""));
							detCargo.setValorTotalCargado(Utilidades.convertirADouble(forma.getDistribucionSolicitud("valortotalcargado_"+forma.getResponsables().get(a).getSubCuenta()+"_"+ConstantesBD.acronimoNo)+""));
							detCargo.setValorUnitarioCargado(detCargo.getValorTotalCargado()/detCargo.getCantidadCargada());
							detCargo.setCodigoEsquemaTarifario(Utilidades.convertirAEntero(forma.getDistribucionSolicitud("esquematarifario_"+forma.getResponsables().get(a).getSubCuenta()+"_"+ConstantesBD.acronimoNo)+""));
							detCargo.setPorcentajeDescuento(Utilidades.convertirADouble(forma.getDistribucionSolicitud("porcentajedcto_"+forma.getResponsables().get(a).getSubCuenta()+"_"+ConstantesBD.acronimoNo)+""));
							detCargo.setValorUnitarioDescuento(Utilidades.convertirADouble(forma.getDistribucionSolicitud("valorunitariodcto_"+forma.getResponsables().get(a).getSubCuenta()+"_"+ConstantesBD.acronimoNo)+""));
							if(dejarPendiente)
								detCargo.setEstado(ConstantesBD.codigoEstadoFPendiente);
							else
								detCargo.setEstado(ConstantesBD.codigoEstadoFCargada);
							detCargo.setCubierto(cubierto);
							detCargo.setRequiereAutorizacion(requiereAutorizacion);
							
							///datos requeridos.
							detCargo.setCodigoSubcuenta(forma.getResponsables().get(a).getSubCuentaDouble());
							detCargo.setCodigoConvenio(forma.getResponsables().get(a).getConvenio().getCodigo());
							detCargo.setCodigoEsquemaTarifario(Utilidades.convertirAEntero(forma.getDistribucionSolicitud("esquematarifario_"+forma.getResponsables().get(a).getSubCuenta()+"_"+ConstantesBD.acronimoNo)+""));
							detCargo.setNumeroSolicitud(numeroSolicitud);
							detCargo.setCodigoServicio(servicio);
							detCargo.setCodigoArticulo(articulo);
							detCargo.setCodigoServicioCx(Utilidades.convertirAEntero(forma.getDetSolicitudesPaciente("serviciocx_"+forma.getIndice())+""));
							detCargo.setDetCxHonorarios(Utilidades.convertirAEntero(forma.getDetSolicitudesPaciente("detcxhonorarios_"+forma.getIndice())+""));
							detCargo.setDetAsocioCxSalasMat(Utilidades.convertirAEntero(forma.getDetSolicitudesPaciente("detascxsalmat_"+forma.getIndice())+""));
							
							detCargo.setCodigoTipoAsocio(Utilidades.convertirAEntero(forma.getDetSolicitudesPaciente("tipoasocio_"+forma.getIndice())+""));
							detCargo.setFacturado(ConstantesBD.acronimoNo);
							detCargo.setCodigoTipoSolicitud(Utilidades.convertirAEntero(forma.getDetSolicitudesPaciente("tiposolicitud_"+forma.getIndice())+""));
							detCargo.setPaquetizado(ConstantesBD.acronimoNo);
							detCargo.setCodigoSolicitudSubCuenta(solicitudSubCuenta);
							detCargo.setCodigoContrato(forma.getResponsables().get(a).getContrato());
							detCargo.setCodigoDetalleCargo(Cargos.insertarDetalleCargos(con, detCargo, usuario.getLoginUsuario()));
							transaccion=detCargo.getCodigoDetalleCargo()>0; 
							///insertar el consumo para el caso de materiales.
							if(detCargo.getCodigoTipoSolicitud()==ConstantesBD.codigoTipoSolicitudCirugia)
							{
								boolean esCirugiaPorConsumo=UtilidadesSalas.esCxPorConsumoMateriales(con,numeroSolicitud); 
								String tipoServicioAsocio=UtilidadesSalas.obtenerCodigoTipoServicioAsocio(con, detCargo.getCodigoTipoAsocio());
								double valorTarifa=detCargo.getValorTotalCargado();
								if(tipoServicioAsocio.equals(ConstantesBD.codigoServicioMaterialesCirugia+"") && esCirugiaPorConsumo && valorTarifa>0)
								{	
									double porcentaje=detCargo.getPorcentajeCargado();
									int servicioCx=detCargo.getCodigoServicioCx();
									double codigoDetalleCargo=detCargo.getCodigoDetalleCargo();
									guardarDistribucionConsumoMateriales(con,porcentaje,servicioCx,codigoDetalleCargo,numeroSolicitud,usuario.getLoginUsuario(),detCargo.getTipoDistribucion());
								}
								
								//MT 6584 Actualiza tabla solicitud_cirugias
								int consecutivoServicioCx= SolicitudesCx.obtenerConsecutivoServicioCx(con, Utilidades.convertirAEntero(forma.getDetSolicitudesPaciente("serviciocx_"+forma.getIndice())+""), forma.getDetSolicitudesPaciente("solicitud_"+forma.getIndice())+"");
								if(consecutivoServicioCx==1)
								{
									if(!SolicitudesCx.actualizarSubCuentaCx(con, Utilidades.convertirADouble(forma.getResponsables().get(a).getSubCuenta()), forma.getDetSolicitudesPaciente("solicitud_"+forma.getIndice())+""))
									{
										transaccion=false;
									}
								}
								
							}
							
							if(!UtilidadTexto.isEmpty(forma.getDetSolicitudesPaciente("servicio_"+forma.getIndice())+""))
							{
								//************Justificacion No Pos Servicio******************//
								this.JustificacionNoPosServicios(con, forma, usuario, servicio, numeroSolicitud, a, detCargo.getCodigoConvenio(), codigoJustificacionSolicitud, codigoJustificacionPendiente);
								//***********************************************************//
							}
							else
							{
								//************Justificacion No Pos Articulo******************//
								this.JustificacionNoPosArticulos(con, forma, usuario, articulo, numeroSolicitud, a, detCargo.getCodigoConvenio(), codigoJustificacionSolicitud, codigoJustificacionPendiente);
								//***********************************************************//
							}
						}
						if(!transaccion)
							a=forma.getResponsables().size();
					}
				}
			}
			
		}catch (IPSException ipsme) {
			throw ipsme;
		}
		catch (Exception e) {
			Log4JManager.error(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
			
		return transaccion;
	}
	
	/**
	 * 
	 * @param con
	 * @param porcentaje
	 * @param servicioCx
	 * @param codigoDetalleCargo
	 * @param tipoDistribucion 
	 */
	private boolean guardarDistribucionConsumoMateriales(Connection con,double porcentaje, int servicioCx, double codigoDetalleCargo,int numeroSolicitud,String logginUsuario, String tipoDistribucion) throws IPSException 
	{
		try {
			int codigoServicioCxParametroConsumo=(UtilidadesSalas.esSolicitudCirugiaPorActo(con, numeroSolicitud))?ConstantesBD.codigoNuncaValido:servicioCx;
			HashMap<Object, Object> mapaConsumos= UtilidadesSalas.obtenerArticulosConsumoCx(con,numeroSolicitud+"", codigoServicioCxParametroConsumo);
			for(int w=0; w<Utilidades.convertirAEntero(mapaConsumos.get("numRegistros")+""); w++)
			{
				DtoDetalleCargoArticuloConsumo dto= new DtoDetalleCargoArticuloConsumo();
				dto.setCantidad(Utilidades.convertirAEntero(mapaConsumos.get("cantidad_consumo_total_"+w)+""));
				dto.setCodigoArticulo(Utilidades.convertirAEntero(mapaConsumos.get("articulo_"+w)+""));
				dto.setDetalleCargo(codigoDetalleCargo);
				//en los valores se guarda el porcentaje que cubre.
				//dto.setValorTotal(Utilidades.convertirADouble(mapaConsumos.get("valor_total_"+w)+""));
				//dto.setValorUnitario(Utilidades.convertirADouble(mapaConsumos.get("valor_unitario_"+w)+""));
				if(tipoDistribucion.equals(ConstantesIntegridadDominio.acronimoTipoDistribucionPorcentual))
				{
					dto.setPorcentaje(porcentaje);
					dto.setValorTotal(Utilidades.convertirADouble(mapaConsumos.get("valor_total_"+w)+"")*porcentaje/100);
					dto.setValorUnitario(Utilidades.convertirADouble(mapaConsumos.get("valor_unitario_"+w)+"")*porcentaje/100);
				}
				else if(tipoDistribucion.equals(ConstantesIntegridadDominio.acronimoTipodistribucionMonto))
				{
					dto.setValorTotal(Utilidades.convertirADouble(mapaConsumos.get("valor_total_"+w)+"")*porcentaje/100);
					dto.setValorUnitario(Utilidades.convertirADouble(mapaConsumos.get("valor_unitario_"+w)+"")*porcentaje/100);
				}
				
				if(Cargos.insertarDetalleCargosArtConsumos(con, dto, logginUsuario)<=0)
				{
					Log4JManager.error("NO INSERTO LOS CONSUMOS DE LA CX  ->"+numeroSolicitud);
		    		return false;
				}
			}
			return true;
		}
		catch (IPSException ipsme) {
			throw ipsme;
		}
		catch (Exception e) {
			Log4JManager.error(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
	}

	/**
	 * Metodo para verificar existencias y dejar pendiente justificaciones de articulos
	 * @param con
	 * @param forma
	 * @param usuario
	 * @param articulo
	 * @param numeroSolicitud
	 * @param a
	 * @param codigoConvenio 
	 * @param codigoJustificacionSolicitud 
	 */
	private void JustificacionNoPosArticulos(Connection con, DistribucionCuentaForm forma, UsuarioBasico usuario, int articulo, int numeroSolicitud, int a, int codigoConvenio, int codigoJustificacionSolicitud, double codigoJustificacionPendiente) throws IPSException
	{
		try {
			//Cambio Incluido por la Tarea 59745. Si el articulo es POS no se debe distribuir la Justificaciï¿½n
			//********JUSTIFICACION NO POS ARITCULOS******************//
			if(UtilidadJustificacionPendienteArtServ.validarNOPOS(con, numeroSolicitud, articulo, true, false, codigoConvenio))
			{
				if(codigoJustificacionSolicitud != ConstantesBD.codigoNuncaValido)
				{
					//Insertamos el registro del responsable con su correspondiente cantidad
					FormatoJustArtNopos.insertarResponsableJustificacion(con, numeroSolicitud, articulo, forma.getResponsables().get(a).getSubCuenta(), forma.getDistribucionSolicitud("cantidad_"+forma.getResponsables().get(a).getSubCuenta()+"_"+ConstantesBD.acronimoNo)+"", codigoJustificacionSolicitud);
				} else {
					if(codigoJustificacionPendiente != ConstantesBD.codigoNuncaValido)
					{	
						//Insertamos el registro del responsable con su correspondiente cantidad
						UtilidadJustificacionPendienteArtServ.insertarResponsableJustificacionPendiente(con, codigoJustificacionPendiente, forma.getResponsables().get(a).getSubCuenta(), forma.getDistribucionSolicitud("cantidad_"+forma.getResponsables().get(a).getSubCuenta()+"_"+ConstantesBD.acronimoNo)+"", true);
						forma.setJustificacionNoPosMap("mensaje_0", "ARTï¿½CULO ["+articulo+"] DE NATURALEZA NO POS REQUIERE DE JUSTIFICACIï¿½N NO POS.");
					}
				}
			}
		}
		catch (IPSException ipsme) {
			throw ipsme;
		}
		catch (Exception e) {
			Log4JManager.error(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
	}
	
	/**
	 * Metodo para verificar existencias y dejar pendiente justificaciones de servicio
	 * @param con
	 * @param forma
	 * @param usuario
	 * @param servicio
	 * @param numeroSolicitud
	 * @param a
	 */
	private void JustificacionNoPosServicios(Connection con, DistribucionCuentaForm forma, UsuarioBasico usuario, int servicio, int numeroSolicitud, int a, int codigoConvenio, int codigoJustificacionSolicitud, double codigoJustificacionPendiente) throws IPSException
	{
		try {
		//Cambio Incluido por la Tarea 59745. Si el articulo es POS no se debe distribuir la Justificaciï¿½n
		//********JUSTIFICACION NO POS ARITCULOS******************//
		if(UtilidadJustificacionPendienteArtServ.validarNOPOS(con, numeroSolicitud, servicio, false, false, codigoConvenio))
		{
			if(codigoJustificacionSolicitud != ConstantesBD.codigoNuncaValido)
			{
				//Insertamos el registro del responsable con su correspondiente cantidad
				FormatoJustArtNopos.insertarResponsableJustificacionServicio(con, numeroSolicitud, servicio, forma.getResponsables().get(a).getSubCuenta(), forma.getDistribucionSolicitud("cantidad_"+forma.getResponsables().get(a).getSubCuenta()+"_"+ConstantesBD.acronimoNo)+"", codigoJustificacionSolicitud);
			} else {
				if(codigoJustificacionPendiente != ConstantesBD.codigoNuncaValido)
				{	
					//Insertamos el registro del responsable con su correspondiente cantidad
					UtilidadJustificacionPendienteArtServ.insertarResponsableJustificacionPendiente(con, codigoJustificacionPendiente, forma.getResponsables().get(a).getSubCuenta(), forma.getDistribucionSolicitud("cantidad_"+forma.getResponsables().get(a).getSubCuenta()+"_"+ConstantesBD.acronimoNo)+"", false);
					forma.setJustificacionNoPosMap("mensaje_0", "SERVICIO ["+servicio+"] DE NATURALEZA NO POS REQUIERE DE JUSTIFICACIï¿½N NO POS.");
				}
			}	
		}
	}
		catch (IPSException ipsme) {
			throw ipsme;
		}
		catch (Exception e) {
			Log4JManager.error(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
		
	}

	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mapping
	 * @return
	 */
	private ActionForward accionCambioEsquemaTarifario(Connection con, DistribucionCuentaForm forma, ActionMapping mapping,UsuarioBasico usuario) throws IPSException
	{
		///para cirugias no aplica
		double valorUnitarioCargado=0;
		if(UtilidadTexto.isEmpty(forma.getDetSolicitudesPaciente("servicio_"+forma.getIndice())+""))
			valorUnitarioCargado=Cargos.obtenerValorTarifaYExcepcion(con, forma.getResponsables().get(forma.getIndiceReponsable()),Utilidades.convertirAEntero(forma.getDistribucionSolicitud("esquematarifario_"+forma.getIndiceDetSol())+""), usuario, Utilidades.convertirAEntero(forma.getDetSolicitudesPaciente("articulo_"+forma.getIndice())+""), false,Utilidades.convertirAEntero(forma.getDetSolicitudesPaciente("solicitud_"+forma.getIndice())+""),Cargos.obtenerFechaCalculoCargo(con, Utilidades.convertirAEntero(forma.getDetSolicitudesPaciente("solicitud_"+forma.getIndice())+"")));
		else
			valorUnitarioCargado=Cargos.obtenerValorTarifaYExcepcion(con, forma.getResponsables().get(forma.getIndiceReponsable()),Utilidades.convertirAEntero(forma.getDistribucionSolicitud("esquematarifario_"+forma.getIndiceDetSol())+""), usuario, Utilidades.convertirAEntero(forma.getDetSolicitudesPaciente("servicio_"+forma.getIndice())+""), true,Utilidades.convertirAEntero(forma.getDetSolicitudesPaciente("solicitud_"+forma.getIndice())+""),Cargos.obtenerFechaCalculoCargo(con, Utilidades.convertirAEntero(forma.getDetSolicitudesPaciente("solicitud_"+forma.getIndice())+"")));
		
		forma.setDistribucionSolicitud("valorunitariocargado_"+forma.getIndiceDetSol(), valorUnitarioCargado+"");
		double valTotalCargado=valorUnitarioCargado*(Utilidades.convertirAEntero(forma.getDistribucionSolicitud("cantidad_"+forma.getIndiceDetSol())+"",true));
		forma.setDistribucionSolicitud("valortotalcargado_"+forma.getIndiceDetSol(),valTotalCargado+"");
		UtilidadBD.closeConnection(con);
		return mapping.findForward("detalleSolicitud");
	}

	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param paciente
	 * @param usuario
	 * @param mapping
	 * @param request
	 * @return
	 */
	private ActionForward accionEjecutarBusqueda(Connection con, DistribucionCuentaForm forma, DistribucionCuenta mundo, PersonaBasica paciente, UsuarioBasico usuario, ActionMapping mapping, HttpServletRequest request) 
	{
		String[] subCuentasResponsables = new String[forma.getResponsables().size()];
		for(int a=0;a<forma.getResponsables().size();a++)
		{
			subCuentasResponsables[a]=forma.getResponsables().get(a).getSubCuenta();
		}
		forma.setDetSolicitudesPaciente(mundo.consultarDetSolicitudesPaciente(con,forma.getParametrosBusquedaAvanzada(),true,subCuentasResponsables));
		forma.setEstado("ejecutarBusquedaAvanzada");
		UtilidadBD.closeConnection(con);
		return mapping.findForward("listadoSolicitudesPaciente");
	}
	
	/**
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param paciente
	 * @param usuario
	 * @param mapping
	 * @param request
	 * @return
	 */
	private ActionForward accionEjecutarBusquedaResponsable(Connection con, DistribucionCuentaForm forma, DistribucionCuenta mundo, PersonaBasica paciente, UsuarioBasico usuario, ActionMapping mapping, HttpServletRequest request) 
	{
		forma.setDetSolicitudesResponsable(mundo.consultarDetSolicitudesResponsableAvanzada(con,forma.getResponsables().get(forma.getIndice()).getSubCuenta(),forma.getParametrosBusquedaAvanzadaResponsable()));
		UtilidadBD.closeConnection(con);
		return mapping.findForward("listadoSolicitudesResponsable");
	}
	
	/**
	 * Mï¿½todo que consulta los registros de distribucion por convenio 
	 * segï¿½n los parametros de busqueda avanzada seleccionada. Este mï¿½todo
	 * se creo ya que en la funcionalidad de registrar utilizan una consulta
	 * diferente a la de la funcionalidad de consultar la distribucion
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param paciente
	 * @param usuario
	 * @param mapping
	 * @param request
	 * @return
	 */
	private ActionForward accionEjecutarBusquedaResponsableDesdeRegistro(Connection con, DistribucionCuentaForm forma, DistribucionCuenta mundo, PersonaBasica paciente, UsuarioBasico usuario, ActionMapping mapping, HttpServletRequest request)
	{
		forma.getResponsables().get(forma.getIndice()).setSolicitudesSubcuenta(mundo.obtenerSolicitudesSubCuentaBusquedaAvanzada(con, forma.getResponsables().get(forma.getIndice()).getSubCuenta(), false, true, forma.getParametrosBusquedaAvanzadaResponsable()));
		UtilidadBD.closeConnection(con);
		return mapping.findForward("listadoSolicitudesResponsable");
	}
	
	/**
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param paciente
	 * @param usuario
	 * @param mapping
	 * @param request
	 * @return
	 */
	private ActionForward accionDetalleSolicitudesResponsableConsulta(Connection con, DistribucionCuentaForm forma, DistribucionCuenta mundo, PersonaBasica paciente, UsuarioBasico usuario, ActionMapping mapping, HttpServletRequest request) 
	{
		//Cambio por Tarea 52462. Se resetea el mapa que almacena los datos de la Bï¿½squeda Avanzada
		HashMap<String, Object> tempo = new HashMap<String, Object>();
		forma.setParametrosBusquedaAvanzadaResponsable(tempo);
		
		int[] estados={ConstantesBD.codigoEstadoFCargada,ConstantesBD.codigoEstadoFExento,ConstantesBD.codigoEstadoFPendiente};
		forma.setDetSolicitudesResponsable(mundo.consultarDetSolicitudesResponsable(con,forma.getResponsables().get(forma.getIndice()).getSubCuenta(),estados));
		UtilidadBD.closeConnection(con);
		return mapping.findForward("listadoSolicitudesResponsable");
	}
	
	/**
	 * @param forma
	 */
	private void accionOrdenar(DistribucionCuentaForm forma) 
	{
		int numReg=Utilidades.convertirAEntero(forma.getDetSolicitudesPaciente("numRegistros")+"");
		forma.setDetSolicitudesPaciente(Listado.ordenarMapa(indicesDetSolicitudes,forma.getPatronOrdenar(),forma.getUltimoPatron(),forma.getDetSolicitudesPaciente(),numReg));
		forma.setUltimoPatron(forma.getPatronOrdenar());
		forma.setDetSolicitudesPaciente("numRegistros",numReg+"");
	}
	
	/**
	 * @param forma
	 */
	private void accionOrdenarResponsable(DistribucionCuentaForm forma) 
	{
		int numReg=Utilidades.convertirAEntero(forma.getDetSolicitudesResponsable("numRegistros")+"");
		forma.setDetSolicitudesResponsable(Listado.ordenarMapa(indicesDetResponsables,forma.getPatronOrdenar(),forma.getUltimoPatron(),forma.getDetSolicitudesResponsable(),numReg));
		forma.setUltimoPatron(forma.getPatronOrdenar());
		forma.setDetSolicitudesResponsable("numRegistros",numReg+"");
	}

	/**
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param paciente
	 * @param usuario
	 * @param mapping
	 * @param request
	 * @return
	 */
	private ActionForward accionDetalleSolicitud(Connection con, DistribucionCuentaForm forma, DistribucionCuenta mundo, PersonaBasica paciente, UsuarioBasico usuario, ActionMapping mapping, HttpServletRequest request) throws IPSException 
	{
		double valorBaseDistribucion=0;
		
		if(UtilidadTexto.isEmpty(forma.getDetSolicitudesPaciente("servicio_"+forma.getIndice())+""))
			valorBaseDistribucion=UtilidadesFacturacion.obtenerValorUnitarioCargadoConvenioBase(con,forma.getDetSolicitudesPaciente("solicitud_"+forma.getIndice())+"",forma.getDetSolicitudesPaciente("articulo_"+forma.getIndice())+"","","","",false,ConstantesBD.acronimoNo);
		else
			valorBaseDistribucion=UtilidadesFacturacion.obtenerValorUnitarioCargadoConvenioBase(con,forma.getDetSolicitudesPaciente("solicitud_"+forma.getIndice())+"",forma.getDetSolicitudesPaciente("servicio_"+forma.getIndice())+"",forma.getDetSolicitudesPaciente("serviciocx_"+forma.getIndice())+"",forma.getDetSolicitudesPaciente("detcxhonorarios_"+forma.getIndice())+"",forma.getDetSolicitudesPaciente("detascxsalmat_"+forma.getIndice())+"",true,ConstantesBD.acronimoNo);

		forma.setDetSolicitudesPaciente("valorbasedistribucion_"+forma.getIndice(),valorBaseDistribucion+"");

		forma.setDistribucionSolicitud(mundo.consultarDistribucionSolicitud(con,forma.getDetSolicitudesPaciente("solicitud_"+forma.getIndice())+"",forma.getDetSolicitudesPaciente("servicio_"+forma.getIndice())+"",forma.getDetSolicitudesPaciente("articulo_"+forma.getIndice())+"",forma.getDetSolicitudesPaciente("serviciocx_"+forma.getIndice())+"",forma.getDetSolicitudesPaciente("detcxhonorarios_"+forma.getIndice())+"",forma.getDetSolicitudesPaciente("detascxsalmat_"+forma.getIndice())+"",forma.getDetSolicitudesPaciente("tipodistribucion_"+forma.getIndice())+""));
		
		for(int a=0;a<forma.getResponsables().size();a++)
		{
			for(int j=0;j<indicesDistribucionSol.length;j++)
			{
				if(!forma.getDistribucionSolicitud().containsKey(indicesDistribucionSol[j]+""+forma.getResponsables().get(a).getSubCuenta()+"_"+ConstantesBD.acronimoSi))
				{
					forma.setDistribucionSolicitud(indicesDistribucionSol[j]+""+forma.getResponsables().get(a).getSubCuenta()+"_"+ConstantesBD.acronimoSi, "");
				}
				if(!forma.getDistribucionSolicitud().containsKey(indicesDistribucionSol[j]+""+forma.getResponsables().get(a).getSubCuenta()+"_"+ConstantesBD.acronimoNo))
				{
					forma.setDistribucionSolicitud(indicesDistribucionSol[j]+""+forma.getResponsables().get(a).getSubCuenta()+"_"+ConstantesBD.acronimoNo, "");
				}
			}
			if(!forma.getDistribucionSolicitud().containsKey("tiporegistro_"+forma.getResponsables().get(a).getSubCuenta()+"_"+ConstantesBD.acronimoSi))
			{
				forma.setDistribucionSolicitud("tiporegistro_"+forma.getResponsables().get(a).getSubCuenta()+"_"+ConstantesBD.acronimoSi, "MEM");
			}
			if(!forma.getDistribucionSolicitud().containsKey("tiporegistro_"+forma.getResponsables().get(a).getSubCuenta()+"_"+ConstantesBD.acronimoNo))
			{
				forma.setDistribucionSolicitud("tiporegistro_"+forma.getResponsables().get(a).getSubCuenta()+"_"+ConstantesBD.acronimoNo, "MEM");
			}
		}
		
		int codigoEsquemaTarifarioBase=0;
		if(UtilidadTexto.isEmpty(forma.getDetSolicitudesPaciente("servicio_"+forma.getIndice())+""))
			codigoEsquemaTarifarioBase=UtilidadesFacturacion.obtenerEsquemaTarifarioBase(con,forma.getDetSolicitudesPaciente("solicitud_"+forma.getIndice())+"",forma.getDetSolicitudesPaciente("articulo_"+forma.getIndice())+"","","","",false,ConstantesBD.acronimoNo);
		else
			codigoEsquemaTarifarioBase=UtilidadesFacturacion.obtenerEsquemaTarifarioBase(con,forma.getDetSolicitudesPaciente("solicitud_"+forma.getIndice())+"",forma.getDetSolicitudesPaciente("servicio_"+forma.getIndice())+"",forma.getDetSolicitudesPaciente("serviciocx_"+forma.getIndice())+"",forma.getDetSolicitudesPaciente("detcxhonorarios_"+forma.getIndice())+"",forma.getDetSolicitudesPaciente("detascxsalmat_"+forma.getIndice())+"",true,ConstantesBD.acronimoNo);
		
		logger.info("codigoEsquemaTarifarioBase-->"+codigoEsquemaTarifarioBase);

		////INCIALIZACION DE LOS DATOS DE ACUERDO AL TIPO DE DISTRIBUCION
		if(!(forma.getDetSolicitudesPaciente("tipodistribucion_"+forma.getIndice())+"").trim().equals((forma.getDetSolicitudesPaciente("tipodistribucionoriginal_"+forma.getIndice())+"").trim()))
		{
			forma.setResponsablePaquete(UtilidadesFacturacion.obtenerResposablePaquetizadoDadaSolicitud(con,forma.getDetSolicitudesPaciente("solicitud_"+forma.getIndice())+""));
			for(int a=0;a<forma.getResponsables().size();a++)
			{
				this.inicializarValoresDistribucionDetalleTipoDistribucionDiferente(con,a,forma,ConstantesBD.acronimoSi,codigoEsquemaTarifarioBase,valorBaseDistribucion,usuario);
				this.inicializarValoresDistribucionDetalleTipoDistribucionDiferente(con,a,forma,ConstantesBD.acronimoNo,codigoEsquemaTarifarioBase,valorBaseDistribucion,usuario);
			}
		}
		else
		{
			forma.setResponsablePaquete(UtilidadesFacturacion.obtenerResposablePaquetizadoDadaSolicitud(con,forma.getDetSolicitudesPaciente("solicitud_"+forma.getIndice())+""));
			///inciliazar en el caso de que no se ha cambiado el tipo de distribucion.
			for(int a=0;a<forma.getResponsables().size();a++)
			{
				this.inicializarValoresDistribucionDetalleTipoDistribucionIgual(con, a, forma, ConstantesBD.acronimoSi, codigoEsquemaTarifarioBase, valorBaseDistribucion, usuario);
				this.inicializarValoresDistribucionDetalleTipoDistribucionIgual(con, a, forma, ConstantesBD.acronimoNo, codigoEsquemaTarifarioBase, valorBaseDistribucion, usuario);
			}
		}

		//multiplico por 2 el numero de registros, contemplando los cargos facturados y los que no.
		forma.setDistribucionSolicitud("numRegistros", (forma.getResponsables().size()*2)+"");
		UtilidadBD.closeConnection(con);
		return mapping.findForward("detalleSolicitud");
	}

	/**
	 * @param con
	 * @param a
	 * @param forma
	 * @param acronimoSi
	 * @param codigoEsquemaTarifarioBase
	 * @param valorBaseDistribucion
	 * @param usuario
	 */
	/* Se adiciona una nota ya que se quitaron todas las validaciones del campo valortotalcargado_
	 * las cuales hacian que este campo quedara reseteado. Este cambio se aplico por la Tarea 52468 */
	private void inicializarValoresDistribucionDetalleTipoDistribucionIgual(Connection con, int indice, DistribucionCuentaForm forma, String acronimo, int codigoEsquemaTarifarioBase, double valorBaseDistribucion, UsuarioBasico usuario) throws IPSException
	{
		if(!forma.getDistribucionSolicitud().containsKey("esquematarifario_"+forma.getResponsables().get(indice).getSubCuenta()+"_"+acronimo)||UtilidadTexto.isEmpty(forma.getDistribucionSolicitud("esquematarifario_"+forma.getResponsables().get(indice).getSubCuenta()+"_"+acronimo)+""))
			forma.setDistribucionSolicitud("esquematarifario_"+forma.getResponsables().get(indice).getSubCuenta()+"_"+acronimo, codigoEsquemaTarifarioBase+"");

		double valorUnitarioCargado=0;
		if(UtilidadTexto.isEmpty(forma.getDetSolicitudesPaciente("servicio_"+forma.getIndice())+""))
			valorUnitarioCargado=Cargos.obtenerValorTarifaYExcepcion(con, forma.getResponsables().get(indice),codigoEsquemaTarifarioBase, usuario, Utilidades.convertirAEntero(forma.getDetSolicitudesPaciente("articulo_"+forma.getIndice())+""), false,Utilidades.convertirAEntero(forma.getDetSolicitudesPaciente("solicitud_"+forma.getIndice())+""),Cargos.obtenerFechaCalculoCargo(con,Utilidades.convertirAEntero(forma.getDetSolicitudesPaciente("solicitud_"+forma.getIndice())+"")));
		else
			valorUnitarioCargado=Cargos.obtenerValorTarifaYExcepcion(con, forma.getResponsables().get(indice),codigoEsquemaTarifarioBase, usuario, Utilidades.convertirAEntero(forma.getDetSolicitudesPaciente("servicio_"+forma.getIndice())+""), true,Utilidades.convertirAEntero(forma.getDetSolicitudesPaciente("solicitud_"+forma.getIndice())+""),Cargos.obtenerFechaCalculoCargo(con, Utilidades.convertirAEntero(forma.getDetSolicitudesPaciente("solicitud_"+forma.getIndice())+"")));
		
		if(Integer.parseInt(forma.getDetSolicitudesPaciente("tiposolicitud_"+forma.getIndice())+"")==ConstantesBD.codigoTipoSolicitudCirugia&&!UtilidadTexto.isEmpty(forma.getDetSolicitudesPaciente("servicio_"+forma.getIndice())+""))
		{
			valorUnitarioCargado=UtilidadesFacturacion.obtenerValorUnitarioCargadoConvenioBase(con,forma.getDetSolicitudesPaciente("solicitud_"+forma.getIndice())+"",forma.getDetSolicitudesPaciente("servicio_"+forma.getIndice())+"",forma.getDetSolicitudesPaciente("serviciocx_"+forma.getIndice())+"",forma.getDetSolicitudesPaciente("detcxhonorarios_"+forma.getIndice())+"",forma.getDetSolicitudesPaciente("detascxsalmat_"+forma.getIndice())+"",true,ConstantesBD.acronimoNo);
		}

		if(!forma.getDistribucionSolicitud().containsKey("valorunitariocargado_"+forma.getResponsables().get(indice).getSubCuenta()+"_"+acronimo)||UtilidadTexto.isEmpty(forma.getDistribucionSolicitud("valorunitariocargado_"+forma.getResponsables().get(indice).getSubCuenta()+"_"+acronimo)+""))
			forma.setDistribucionSolicitud("valorunitariocargado_"+forma.getResponsables().get(indice).getSubCuenta()+"_"+acronimo, valorUnitarioCargado+"");

		if((forma.getDetSolicitudesPaciente("tipodistribucion_"+forma.getIndice())+"").trim().equals(ConstantesIntegridadDominio.acronimoTipoDistribucionCantidad))
		{
			if(!forma.getDistribucionSolicitud().containsKey("cantidad_"+forma.getResponsables().get(indice).getSubCuenta()+"_"+acronimo)||UtilidadTexto.isEmpty(forma.getDistribucionSolicitud("cantidad_"+forma.getResponsables().get(indice).getSubCuenta()+"_"+acronimo)+""))
				forma.setDistribucionSolicitud("cantidad_"+forma.getResponsables().get(indice).getSubCuenta()+"_"+acronimo, "0");
			forma.setDistribucionSolicitud("porcentaje_"+forma.getResponsables().get(indice).getSubCuenta()+"_"+acronimo, "");	
			double valTotalCargado=valorUnitarioCargado*(Utilidades.convertirAEntero(forma.getDistribucionSolicitud("cantidad_"+forma.getResponsables().get(indice).getSubCuenta()+"_"+acronimo)+"",true));
			/*if(!forma.getDistribucionSolicitud().containsKey("valortotalcargado_"+forma.getResponsables().get(indice).getSubCuenta()+"_"+acronimo)||UtilidadTexto.isEmpty(forma.getDistribucionSolicitud("valortotalcargado_"+forma.getResponsables().get(indice).getSubCuenta()+"_"+acronimo)+""))
				forma.setDistribucionSolicitud("valortotalcargado_"+forma.getResponsables().get(indice).getSubCuenta()+"_"+acronimo,valTotalCargado+"");*/

		}
		int cantidad=Utilidades.convertirAEntero(forma.getDetSolicitudesPaciente("cantidad_"+forma.getIndice())+"");
		if((forma.getDetSolicitudesPaciente("tipodistribucion_"+forma.getIndice())+"").trim().equals(ConstantesIntegridadDominio.acronimoTipoDistribucionPorcentual))
		{
			forma.setDistribucionSolicitud("cantidad_"+forma.getResponsables().get(indice).getSubCuenta()+"_"+acronimo,cantidad+"");
			forma.setDistribucionSolicitud("valorunitariocargado_"+forma.getResponsables().get(indice).getSubCuenta()+"_"+acronimo, valorBaseDistribucion+"");
			if(!forma.getDistribucionSolicitud().containsKey("porcentaje_"+forma.getResponsables().get(indice).getSubCuenta()+"_"+acronimo)||UtilidadTexto.isEmpty(forma.getDistribucionSolicitud("porcentaje_"+forma.getResponsables().get(indice).getSubCuenta()+"_"+acronimo)+""))
			{
				forma.setDistribucionSolicitud("porcentaje_"+forma.getResponsables().get(indice).getSubCuenta()+"_"+acronimo, "0");	
				//forma.setDistribucionSolicitud("valortotalcargado_"+forma.getResponsables().get(indice).getSubCuenta()+"_"+acronimo,"0");
			}
		}
		if((forma.getDetSolicitudesPaciente("tipodistribucion_"+forma.getIndice())+"").trim().equals(ConstantesIntegridadDominio.acronimoTipodistribucionMonto))
		{
			forma.setDistribucionSolicitud("cantidad_"+forma.getResponsables().get(indice).getSubCuenta()+"_"+acronimo,cantidad+"");
			forma.setDistribucionSolicitud("porcentaje_"+forma.getResponsables().get(indice).getSubCuenta()+"_"+acronimo, "");	
			forma.setDistribucionSolicitud("valorunitariocargado_"+forma.getResponsables().get(indice).getSubCuenta()+"_"+acronimo, valorBaseDistribucion+"");
			/*if(!forma.getDistribucionSolicitud().containsKey("valortotalcargado_"+forma.getResponsables().get(indice).getSubCuenta()+"_"+acronimo)||UtilidadTexto.isEmpty(forma.getDistribucionSolicitud("valortotalcargado_"+forma.getResponsables().get(indice).getSubCuenta()+"_"+acronimo)+""))
				forma.setDistribucionSolicitud("valortotalcargado_"+forma.getResponsables().get(indice).getSubCuenta()+"_"+acronimo,"0");*/
		}
		
		if(!forma.getDistribucionSolicitud().containsKey("porcentajedcto_"+forma.getResponsables().get(indice).getSubCuenta()+"_"+acronimo)||UtilidadTexto.isEmpty(forma.getDistribucionSolicitud("porcentajedcto_"+forma.getResponsables().get(indice).getSubCuenta()+"_"+acronimo)+""))
			forma.setDistribucionSolicitud("porcentajedcto_"+forma.getResponsables().get(indice).getSubCuenta()+"_"+acronimo, "");
		if(!forma.getDistribucionSolicitud().containsKey("valorunitariodcto_"+forma.getResponsables().get(indice).getSubCuenta()+"_"+acronimo)||UtilidadTexto.isEmpty(forma.getDistribucionSolicitud("valorunitariodcto_"+forma.getResponsables().get(indice).getSubCuenta()+"_"+acronimo)+""))
			forma.setDistribucionSolicitud("valorunitariodcto_"+forma.getResponsables().get(indice).getSubCuenta()+"_"+acronimo, "");
		if(!forma.getDistribucionSolicitud().containsKey("valortotaldcto_"+forma.getResponsables().get(indice).getSubCuenta()+"_"+acronimo)||UtilidadTexto.isEmpty(forma.getDistribucionSolicitud("valortotaldcto_"+forma.getResponsables().get(indice).getSubCuenta()+"_"+acronimo)+""))
			forma.setDistribucionSolicitud("valortotaldcto_"+forma.getResponsables().get(indice).getSubCuenta()+"_"+acronimo, "");
	}


	/**
	 * 
	 * @param con 
	 * @param indice 
	 * @param forma
	 * @param acronimoSi
	 * @param valorBaseDistribucion 
	 * @param codigoEsquemaTarifarioBase 
	 * @param usuario 
	 */
	/* Se adiciona una nota ya que se quitaron todas las validaciones del campo valortotalcargado_
	 * las cuales hacian que este campo quedara reseteado. Este cambio se aplico por la Tarea 52468 */
	private void inicializarValoresDistribucionDetalleTipoDistribucionDiferente(Connection con, int indice, DistribucionCuentaForm forma, String acronimo, int codigoEsquemaTarifarioBase, double valorBaseDistribucion, UsuarioBasico usuario) throws IPSException 
	{
		forma.setDistribucionSolicitud("esquematarifario_"+forma.getResponsables().get(indice).getSubCuenta()+"_"+acronimo, codigoEsquemaTarifarioBase+"");
		double valorUnitarioCargado=0;
		if(UtilidadTexto.isEmpty(forma.getDetSolicitudesPaciente("servicio_"+forma.getIndice())+""))
			valorUnitarioCargado=Cargos.obtenerValorTarifaYExcepcion(con, forma.getResponsables().get(indice),codigoEsquemaTarifarioBase, usuario, Utilidades.convertirAEntero(forma.getDetSolicitudesPaciente("articulo_"+forma.getIndice())+""), false,Utilidades.convertirAEntero(forma.getDetSolicitudesPaciente("solicitud_"+forma.getIndice())+""),Cargos.obtenerFechaCalculoCargo(con, Utilidades.convertirAEntero(forma.getDetSolicitudesPaciente("solicitud_"+forma.getIndice())+"")));
		else
			valorUnitarioCargado=Cargos.obtenerValorTarifaYExcepcion(con, forma.getResponsables().get(indice),codigoEsquemaTarifarioBase, usuario, Utilidades.convertirAEntero(forma.getDetSolicitudesPaciente("servicio_"+forma.getIndice())+""), true,Utilidades.convertirAEntero(forma.getDetSolicitudesPaciente("solicitud_"+forma.getIndice())+""),Cargos.obtenerFechaCalculoCargo(con, Utilidades.convertirAEntero(forma.getDetSolicitudesPaciente("solicitud_"+forma.getIndice())+"")));
		
		if(Integer.parseInt(forma.getDetSolicitudesPaciente("tiposolicitud_"+forma.getIndice())+"")==ConstantesBD.codigoTipoSolicitudCirugia&&!UtilidadTexto.isEmpty(forma.getDetSolicitudesPaciente("servicio_"+forma.getIndice())+""))
		{
			valorUnitarioCargado=UtilidadesFacturacion.obtenerValorUnitarioCargadoConvenioBase(con,forma.getDetSolicitudesPaciente("solicitud_"+forma.getIndice())+"",forma.getDetSolicitudesPaciente("servicio_"+forma.getIndice())+"",forma.getDetSolicitudesPaciente("serviciocx_"+forma.getIndice())+"",forma.getDetSolicitudesPaciente("detcxhonorarios_"+forma.getIndice())+"",forma.getDetSolicitudesPaciente("detascxsalmat_"+forma.getIndice())+"",true,ConstantesBD.acronimoNo);
		}

		forma.setDistribucionSolicitud("valorunitariocargado_"+forma.getResponsables().get(indice).getSubCuenta()+"_"+acronimo, valorUnitarioCargado+"");

		if((forma.getDetSolicitudesPaciente("tipodistribucion_"+forma.getIndice())+"").trim().equals(ConstantesIntegridadDominio.acronimoTipoDistribucionCantidad))
		{
			forma.setDistribucionSolicitud("cantidad_"+forma.getResponsables().get(indice).getSubCuenta()+"_"+acronimo, "0");
			forma.setDistribucionSolicitud("porcentaje_"+forma.getResponsables().get(indice).getSubCuenta()+"_"+acronimo, "");	
			double valTotalCargado=valorUnitarioCargado*(Utilidades.convertirAEntero(forma.getDistribucionSolicitud("cantidad_"+forma.getResponsables().get(indice).getSubCuenta()+"_"+acronimo)+"",true));
			//forma.setDistribucionSolicitud("valortotalcargado_"+forma.getResponsables().get(indice).getSubCuenta()+"_"+acronimo,valTotalCargado+"");

		}
		int cantidad=Utilidades.convertirAEntero(forma.getDetSolicitudesPaciente("cantidad_"+forma.getIndice())+"");
		if((forma.getDetSolicitudesPaciente("tipodistribucion_"+forma.getIndice())+"").trim().equals(ConstantesIntegridadDominio.acronimoTipoDistribucionPorcentual))
		{
			forma.setDistribucionSolicitud("cantidad_"+forma.getResponsables().get(indice).getSubCuenta()+"_"+acronimo,cantidad+"");
			if(forma.getResponsablePaquete().trim().equals(forma.getResponsables().get(indice).getSubCuenta()+"_"+acronimo))
			{
				forma.setDistribucionSolicitud("porcentaje_"+forma.getResponsables().get(indice).getSubCuenta()+"_"+acronimo, "100");
				//forma.setDistribucionSolicitud("valortotalcargado_"+forma.getResponsables().get(indice).getSubCuenta()+"_"+acronimo,(cantidad*valorBaseDistribucion));
			}
			else
			{
				forma.setDistribucionSolicitud("porcentaje_"+forma.getResponsables().get(indice).getSubCuenta()+"_"+acronimo, "0");
				//forma.setDistribucionSolicitud("valortotalcargado_"+forma.getResponsables().get(indice).getSubCuenta()+"_"+acronimo,"0");
			}
			
			forma.setDistribucionSolicitud("valorunitariocargado_"+forma.getResponsables().get(indice).getSubCuenta()+"_"+acronimo, valorBaseDistribucion+"");

		}
		if((forma.getDetSolicitudesPaciente("tipodistribucion_"+forma.getIndice())+"").trim().equals(ConstantesIntegridadDominio.acronimoTipodistribucionMonto))
		{
			forma.setDistribucionSolicitud("cantidad_"+forma.getResponsables().get(indice).getSubCuenta()+"_"+acronimo,cantidad+"");
			forma.setDistribucionSolicitud("porcentaje_"+forma.getResponsables().get(indice).getSubCuenta()+"_"+acronimo, "");	
			/*if(forma.getResponsablePaquete().trim().equals(forma.getResponsables().get(indice).getSubCuenta()+"_"+acronimo))
				//forma.setDistribucionSolicitud("valortotalcargado_"+forma.getResponsables().get(indice).getSubCuenta()+"_"+acronimo,(cantidad*valorBaseDistribucion));
			else
				//forma.setDistribucionSolicitud("valortotalcargado_"+forma.getResponsables().get(indice).getSubCuenta()+"_"+acronimo,"0");*/
			
			forma.setDistribucionSolicitud("valorunitariocargado_"+forma.getResponsables().get(indice).getSubCuenta()+"_"+acronimo, valorBaseDistribucion+"");
		}
		

		forma.setDistribucionSolicitud("porcentajedcto_"+forma.getResponsables().get(indice).getSubCuenta()+"_"+acronimo, "");
		forma.setDistribucionSolicitud("valorunitariodcto_"+forma.getResponsables().get(indice).getSubCuenta()+"_"+acronimo, "");
		forma.setDistribucionSolicitud("valortotaldcto_"+forma.getResponsables().get(indice).getSubCuenta()+"_"+acronimo, "");

		//siempre que se cambie el tipo de distribucion, inicializar los estados
		forma.setDistribucionSolicitud("codestado_"+forma.getResponsables().get(indice).getSubCuenta()+"_"+acronimo, ConstantesBD.codigoNuncaValido+"");
		forma.setDistribucionSolicitud("nomestado_"+forma.getResponsables().get(indice).getSubCuenta()+"_"+acronimo, "");
	}


	/**
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param paciente
	 * @param usuario
	 * @param mapping
	 * @param request
	 * @return
	 */
	private ActionForward accionDetalleSolicitudesPaciente(Connection con, DistribucionCuentaForm forma, DistribucionCuenta mundo, PersonaBasica paciente, UsuarioBasico usuario, ActionMapping mapping, HttpServletRequest request) throws IPSException
	{
		//Cambio por Tarea 52463. Se resetea el mapa que almacena los datos de la Bï¿½squeda Avanzada
		HashMap<String, Object> tempo = new HashMap<String, Object>();
		forma.setParametrosBusquedaAvanzada(tempo);
		
		String[] subCuentasResponsables = new String[forma.getResponsables().size()];
		for(int a=0;a<forma.getResponsables().size();a++)
		{
			subCuentasResponsables[a]=forma.getResponsables().get(a).getSubCuenta();
		}
		forma.setDetSolicitudesPaciente(mundo.consultarDetSolicitudesPaciente(con,new int[0],true,new String[0],subCuentasResponsables,false));
		UtilidadBD.closeConnection(con);
		return mapping.findForward("listadoSolicitudesPaciente");
	}

	/**
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param paciente
	 * @param usuario
	 * @param mapping
	 * @param request
	 * @return
	 */
	private ActionForward accionDetalleSolicitudesResponsable(Connection con, DistribucionCuentaForm forma, DistribucionCuenta mundo, PersonaBasica paciente, UsuarioBasico usuario, ActionMapping mapping, HttpServletRequest request) 
	{
		//Cambio por Tarea 52462. Se resetea el mapa que almacena los datos de la Bï¿½squeda Avanzada
		HashMap<String, Object> tempo = new HashMap<String, Object>();
		forma.setParametrosBusquedaAvanzadaResponsable(tempo);
		
		int[] estados={ConstantesBD.codigoEstadoFCargada,ConstantesBD.codigoEstadoFExento,ConstantesBD.codigoEstadoFPendiente};
		forma.getResponsables().get(forma.getIndice()).setSolicitudesSubcuenta(UtilidadesHistoriaClinica.obtenerSolicitudesSubCuenta(con,forma.getResponsables().get(forma.getIndice()).getSubCuenta(),estados,false,true));
		UtilidadBD.closeConnection(con);
		return mapping.findForward("listadoSolicitudesResponsable");
	}

	
	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param paciente
	 * @param usuario
	 * @param mapping
	 * @param request 
	 * @return
	 */
	private ActionForward accionAplicarTopesSoat(Connection con, DistribucionCuentaForm forma, DistribucionCuenta mundo, PersonaBasica paciente, UsuarioBasico usuario, ActionMapping mapping, HttpServletRequest request, HttpServletResponse response) throws IPSException 
	{
		HashMap coberturaAT=Utilidades.obtenerCoberturaAccidenteTransitos(con,usuario.getCodigoInstitucionInt());
		String fechaAccidente=Utilidades.obtenerFechaAccidenteTransito(con,forma.getCodigoIngreso(),false);
		double salarioMinimo=0;
		ActionErrors errores = new ActionErrors();
		
		if(UtilidadTexto.isEmpty(fechaAccidente))
		{
			errores.add("Paciente sin fecha del accidente en el registro de transito", new ActionMessage("error.pacienteSinFechaAccidenteTransito"));
		}
		else
		{
			salarioMinimo=Utilidades.obtenerSalarioMinimoVigente(con,fechaAccidente);
			if(Utilidades.convertirAEntero(coberturaAT.get("numRegistros")+"")<=0)
			{
				errores.add("FALTA PARAMETRIZAR COBERTURA", new ActionMessage("error.distribucion.coberturaAccTransitoNoParametrizada"));
			}
			if(salarioMinimo<=0)
			{
				errores.add("FALTA PARAMETRIZAR SALARIO MINIMO", new ActionMessage("error.distribucion.salarioMinimoNoParametrizado"));
			}
		}
		if(!errores.isEmpty())
		{
			saveErrors(request, errores);
			UtilidadBD.closeConnection(con);
			return mapping.findForward("principal");
		}

		
		HashMap vo=new HashMap();
		int contador=0;
		for(int a=0;a<forma.getResponsables().size();a++)
		{
			Convenio mundoConvenio = new Convenio();
			mundoConvenio.cargarResumen(con, forma.getResponsables().get(a).getConvenio().getCodigo());
			if(esConvenioTipoConventioEventoCatTrans(mundoConvenio.getCodigo()))
			{
				vo.put("subCuenta_"+contador,forma.getResponsables().get(a).getSubCuenta());
				vo.put("fisalud_"+contador, ValoresPorDefecto.getConvenioFisalud(usuario.getCodigoInstitucionInt()).trim().equals(mundoConvenio.getCodigo()+""));
				vo.put("monto_"+contador,coberturaAT.get("cobertura_"+contador)+"");
				contador++;
			}
			else
			{
				vo.put("subCuenta_"+contador,forma.getResponsables().get(a).getSubCuenta());
				vo.put("fisalud_"+contador, ValoresPorDefecto.getConvenioFisalud(usuario.getCodigoInstitucionInt()).trim().equals(mundoConvenio.getCodigo()+""));
				vo.put("monto_"+contador,"");
				contador++;
			}
		}
		vo.put("numRegistros", contador+"");

		//si solo existe un convenio soat y es fisalud, asignar tod a fisalud.
		if(contador==1&&UtilidadTexto.getBoolean(vo.get("fisalud_0")+""))
		{
			logger.info("solo existe un convenio soat y es fisalud, asignar tod a fisalud");
			double temporal=0;
			for(int a=0;a<Utilidades.convertirAEntero(coberturaAT.get("numRegistros")+"");a++)
			{
				temporal+=Utilidades.convertirADouble(coberturaAT.get("cobertura_"+a)+"", true);
			}
			vo.put("monto_0", temporal+"");
		}
		
		//Cambio el Tipo de Formato de Temporal y de Salario Minimo por la Tarea 52465
		///calcular el valor del monto autorizado
		for(int a=0;a<Utilidades.convertirAEntero(vo.get("numRegistros")+"");a++)
		{
			if(UtilidadCadena.noEsVacio(vo.get("monto_"+a)+""))
			{
				double temporal=Utilidades.convertirADouble(vo.get("monto_"+a)+"",true)*Double.parseDouble(UtilidadTexto.formatearValores((salarioMinimo/30)+"","#.######"));
				vo.put("montoAutorizado_"+a, Utilidades.convertirAEntero(temporal+"") +"");
			}
			else
				vo.put("montoAutorizado_"+a, "");
		}
		boolean transaccion=UtilidadBD.iniciarTransaccion(con);

		try
		{
			transaccion=mundo.actualizarMontoAutorizado(con,vo);
		}
		catch(Exception e)
		{
			transaccion=false;
			e.printStackTrace();

		}
		
		
		if(transaccion)
		{
			UtilidadBD.finalizarTransaccion(con);
		}
		else
		{
			this.cancelarIngresoProcesoDistribucion(con, forma, response);
			UtilidadBD.abortarTransaccion(con);
			errores.add("", new ActionMessage("errors.ingresoDatos","los Monto Autorizados"));
			saveErrors(request, errores);
			UtilidadBD.closeConnection(con);
			return mapping.findForward("principal");
			
		}
		return this.accionCargarInformacionGeneral(con,forma,mundo,paciente,usuario,mapping,request);
		
	}


	/**
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param paciente
	 * @param usuario
	 * @param mapping
	 * @return
	 */
	private ActionForward accionModificarParamDistribucion(Connection con, DistribucionCuentaForm forma, DistribucionCuenta mundo, PersonaBasica paciente, UsuarioBasico usuario, ActionMapping mapping) 
	{  
		boolean transaccion=UtilidadBD.iniciarTransaccion(con);
		try
		{
			transaccion=mundo.modificarParametrosDistribucion(con,forma.getParamDistribucion(),usuario.getLoginUsuario());
			if(transaccion)
			{
				transaccion=guardarEsquemasTarifarios(con, forma, mundo, usuario);
				forma.setEsquemasInventario(mundo.consultarEsquemasTarifariosInventario(con,forma.getResponsables().get(forma.getIndice()).getSubCuenta()));
				forma.setEsquemasProcedimientos(mundo.consultarEsquemasTarifariosProcedimientos(con,forma.getResponsables().get(forma.getIndice()).getSubCuenta()));
			}
		}
		catch(Exception e)
		{
			transaccion=false;
			e.printStackTrace();
		}
		
		if(transaccion)
			UtilidadBD.finalizarTransaccion(con);
		else
			UtilidadBD.abortarTransaccion(con);
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("resumenParamDistribucion");
	}
	
	/**
	 * 
	 * @param con
	 * @param 
	 * @param mundo
	 * @param usuarios
	 */
	private boolean guardarEsquemasTarifarios(Connection con, DistribucionCuentaForm forma, DistribucionCuenta mundo, UsuarioBasico usuario) 
	{
		String subCuenta=forma.getResponsables().get(forma.getIndice()).getSubCuenta();
		///ESQUEMAS TARIFARIOS DE INVENTARIO
		//proceso para esquemas tarifarios de inventario.
		//eliminar
		for(int i=0;i<Utilidades.convertirAEntero(forma.getEsquemasInventarioEliminados().get("numRegistros")+"");i++)
		{
			if(mundo.eliminarEsquema(con,forma.getEsquemasInventarioEliminados().get("codigo_"+i)+"",true))
			{
				Utilidades.generarLogGenerico(forma.getEsquemasInventarioEliminados(), new HashMap(), usuario.getLoginUsuario(), true, i,ConstantesBD.logDistribucionCuentaCodigo,indicesEsquemasInventario);
			}
		}
		for(int i=0;i<Utilidades.convertirAEntero(forma.getEsquemasInventario("numRegistros")+"");i++)
		{
			//modificar
			if((forma.getEsquemasInventario("tiporegistro_"+i)+"").trim().equalsIgnoreCase("BD")&&this.existeModificacion(con,forma.getEsquemasInventario(),mundo.consultarEsquemaInventarioLLave(con, forma.getEsquemasInventario("codigo_"+i)+""),i,usuario,indicesEsquemasInventario,true))
			{
				HashMap vo=new HashMap();
				vo.put("codigo", forma.getEsquemasInventario("codigo_"+i)+"");
				vo.put("subcuenta", subCuenta);
				vo.put("clase", forma.getEsquemasInventario("claseinventario_"+i));
				vo.put("esquematarifario", forma.getEsquemasInventario("esquematarifario_"+i));
				vo.put("fechavigencia", forma.getEsquemasInventario("fechavigencia_"+i));
				vo.put("usuario", usuario.getLoginUsuario());
				mundo.modificarEsquemasInventario(con, vo);
				
			}
			//insertar
			else if((forma.getEsquemasInventario("tiporegistro_"+i)+"").trim().equalsIgnoreCase("MEM"))
			{
				HashMap vo=new HashMap();
				vo.put("subcuenta", subCuenta);
				vo.put("clase", forma.getEsquemasInventario("claseinventario_"+i));
				vo.put("esquematarifario", forma.getEsquemasInventario("esquematarifario_"+i));
				vo.put("fechavigencia", forma.getEsquemasInventario("fechavigencia_"+i));
				vo.put("usuario", usuario.getLoginUsuario());
				mundo.insertarEsquemasInventario(con, vo);
			}
			
		}
		
		
//		proceso para esquemas tarifarios de procedimientos.
		//eliminar
		Utilidades.imprimirMapa(forma.getEsquemasProcedimientosEliminados());
		for(int i=0;i<Utilidades.convertirAEntero(forma.getEsquemasProcedimientosEliminados().get("numRegistros")+"");i++)
		{
			if(mundo.eliminarEsquema(con,forma.getEsquemasProcedimientosEliminados().get("codigo_"+i)+"",false))
			{
				Utilidades.generarLogGenerico(forma.getEsquemasProcedimientosEliminados(), new HashMap(), usuario.getLoginUsuario(), true, i,ConstantesBD.logDistribucionCuentaCodigo,indicesEsquemasProcedimientos);
			}
		}
		for(int i=0;i<Utilidades.convertirAEntero(forma.getEsquemasProcedimientos("numRegistros")+"");i++)
		{
			//modificar
			if((forma.getEsquemasProcedimientos("tiporegistro_"+i)+"").trim().equalsIgnoreCase("BD")&&this.existeModificacion(con,forma.getEsquemasProcedimientos(),mundo.consultarEsquemaProcedimientoLLave(con, forma.getEsquemasProcedimientos("codigo_"+i)+""),i,usuario,indicesEsquemasProcedimientos,true))
			{
				HashMap vo=new HashMap();
				vo.put("codigo", forma.getEsquemasProcedimientos("codigo_"+i)+"");
				vo.put("subcuenta", subCuenta);
				vo.put("grupo", forma.getEsquemasProcedimientos("gruposervicio_"+i));
				vo.put("esquematarifario", forma.getEsquemasProcedimientos("esquematarifario_"+i));
				vo.put("fechavigencia", forma.getEsquemasProcedimientos("fechavigencia_"+i));
				vo.put("usuario", usuario.getLoginUsuario());
				mundo.modificarEsquemasProcedimientos(con, vo);
				
			}
			//insertar
			else if((forma.getEsquemasProcedimientos("tiporegistro_"+i)+"").trim().equalsIgnoreCase("MEM"))
			{
				HashMap vo=new HashMap();
				vo.put("subcuenta", subCuenta);
				vo.put("grupo", forma.getEsquemasProcedimientos("gruposervicio_"+i));
				vo.put("esquematarifario", forma.getEsquemasProcedimientos("esquematarifario_"+i));
				vo.put("fechavigencia", forma.getEsquemasProcedimientos("fechavigencia_"+i));
				vo.put("usuario", usuario.getLoginUsuario());
				mundo.insertarEsquemasProcedimientos(con, vo);
			}
			
		}
		return true;
	}

	
	/**
	 * 
	 * @param con
	 * @param esquemasInventario
	 * @param map
	 * @param i
	 * @param usuario
	 * @param indices
	 * @return
	 */
	private boolean existeModificacion(Connection con, HashMap mapa, HashMap mapaTemp, int pos, UsuarioBasico usuario, String[] indices,boolean generarLog) 
	{
		for(int i=0;i<indices.length;i++)
		{
			if(mapaTemp.containsKey(indices[i]+"0")&&mapa.containsKey(indices[i]+""+pos))
			{
				if(!((mapaTemp.get(indices[i]+"0")+"").trim().equals((mapa.get(indices[i]+""+pos)+"").trim())))
				{
					if(generarLog)
						Utilidades.generarLogGenerico(mapa, mapaTemp, usuario.getLoginUsuario(), false, pos,ConstantesBD.logContratoCodigo,indices);
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param paciente
	 * @param usuario
	 * @param mapping
	 * @param request
	 * @return
	 */
	private ActionForward accionCargarParamDistribucion(Connection con, DistribucionCuentaForm forma, DistribucionCuenta mundo, PersonaBasica paciente, UsuarioBasico usuario, ActionMapping mapping, HttpServletRequest request) throws IPSException 
	{
		//*************INICIO CAMBIO PARA PONER EL CONVENIO Y EL CONTRATO EN EL POPUP****************************
		forma.setParamDistribucion("nombreConvenio", forma.getResponsables().get(forma.getIndice()).getConvenio().getNombre()+"");
		forma.setParamDistribucion("numeroContrato", Contrato.obtenerNumeroContrato(con,forma.getResponsables().get(forma.getIndice()).getContrato()));
		logger.info("===>Nombre Convenio: "+forma.getParamDistribucion("nombreConvenio"));
		logger.info("===>Numero Contrato: "+forma.getParamDistribucion("numeroContrato"));
		//****************FIN CAMBIO PARA PONER EL CONVENIO Y EL CONTRATO EN EL POPUP****************************
		
		forma.setParamDistribucion("indice",forma.getIndice());
		forma.setParamDistribucion("subCuenta", forma.getResponsables().get(forma.getIndice()).getSubCuenta()+"");
		
		forma.setEsquemasInventario(mundo.consultarEsquemasTarifariosInventario(con,forma.getResponsables().get(forma.getIndice()).getSubCuenta()));
		forma.setEsquemasProcedimientos(mundo.consultarEsquemasTarifariosProcedimientos(con,forma.getResponsables().get(forma.getIndice()).getSubCuenta()));
		
		if(forma.getTipoDistribucion().equals(ConstantesIntegridadDominio.acronimoTipoDistribucionPorcentual))
		{
			if((forma.getResponsables().get(forma.getIndice()).getPorcentajeAutorizado()+"").trim().equals(""))
			{
				forma.setParamDistribucion("porcentajeAutorizado", Utilidades.obtenerPorcentajeAutorizadoVerficacionDerechos(con,forma.getResponsables().get(forma.getIndice()).getSubCuenta())+"");
				//logger.info("===>Porcentaje Autorizado 1: "+forma.getParamDistribucion("porcentajeAutorizado"));
			}
			else
			{
				forma.setParamDistribucion("porcentajeAutorizado", forma.getResponsables().get(forma.getIndice()).getPorcentajeAutorizado()+"");
				//logger.info("===>Porcentaje Autorizado 2: "+forma.getParamDistribucion("porcentajeAutorizado"));
			}
		}
		else
		{
			forma.setParamDistribucion("porcentajeAutorizado", "");
		}
		
		if(forma.getTipoDistribucion().equals(ConstantesIntegridadDominio.acronimoTipodistribucionMonto))
			forma.setParamDistribucion("montoAutorizado", forma.getResponsables().get(forma.getIndice()).getMontoAutorizado()+"");
		else
			forma.setParamDistribucion("montoAutorizado", "");
		
		if(UtilidadValidacion.esAccidenteTransito(con, forma.getCodigoIngreso()))
			forma.setParamDistribucion("montoFacturadosOtrosSoat", mundo.obtenerValorFacturadoSoat(con,forma.getCodigoIngreso(),forma.getResponsables().get(forma.getIndice()).getConvenio().getCodigo())+"");
		else
			forma.setParamDistribucion("montoFacturadosOtrosSoat", "0");
		
		forma.setParamDistribucion("valorUtilizadoSoat", forma.getResponsables().get(forma.getIndice()).getValorUtilizadoSoat()+"");

		double montoNetoAutorizado=Utilidades.convertirADouble(forma.getParamDistribucion("montoAutorizado")+"",true)-Utilidades.convertirADouble(forma.getParamDistribucion("montoFacturadosOtrosSoat")+"",true)-Utilidades.convertirADouble(forma.getParamDistribucion("valorUtilizadoSoat")+"",true);
		forma.setParamDistribucion("montoNetoAutorizado",UtilidadTexto.formatearValores(montoNetoAutorizado+"","#.######")+"");

		forma.setParamDistribucion("observaciones", forma.getResponsables().get(forma.getIndice()).getObsParametrosDistribucion()+"");
		UtilidadBD.closeConnection(con);
		return mapping.findForward("parametrosDistribucion");
	}

	/**
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param paciente
	 * @param usuario
	 * @param mapping
	 * @param response 
	 * @return
	 */
	private ActionForward accionGuardarFiltroDistribucion(Connection con, DistribucionCuentaForm forma, DistribucionCuenta mundo, PersonaBasica paciente, UsuarioBasico usuario, ActionMapping mapping, HttpServletResponse response) 
	{
		HashMap vo=new HashMap();
		vo.put("subCuenta", forma.getFiltroDistribucion("subcuenta_"+forma.getIndiceFiltroDistribucion()));
		vo.put("viaIngreso", forma.getFiltroDistribucion("viaingreso_"+forma.getIndiceFiltroDistribucion()));
		vo.put("ccSol", forma.getFiltroDistribucion("ccsol_"+forma.getIndiceFiltroDistribucion()));
		vo.put("ccEje", forma.getFiltroDistribucion("cceje_"+forma.getIndiceFiltroDistribucion()));
		vo.put("fechaInicial", forma.getFiltroDistribucion("fechainicial_"+forma.getIndiceFiltroDistribucion()));
		vo.put("fechaFinal", forma.getFiltroDistribucion("fechafinal_"+forma.getIndiceFiltroDistribucion()));
		vo.put("tipoPaciente", forma.getFiltroDistribucion("tipopaciente_"+forma.getIndiceFiltroDistribucion()));
		vo.put("tipoRegistro", forma.getFiltroDistribucion("tiporegistro_"+forma.getIndiceFiltroDistribucion()));
		if(mundo.guardarFiltroDistribucion(con,vo));
			forma.setFiltroDistribucion("tiporegistro_"+forma.getIndiceFiltroDistribucion(),"BD");
		UtilidadBD.closeConnection(con);
		try {
			response.sendRedirect("filtroDistribucion.jsp?indice="+forma.getIndiceFiltroDistribucion()+"&nombreConvenio="+forma.getFiltroDistribucion("nombreConvenio_"+forma.getIndiceFiltroDistribucion())+"&numContrato="+forma.getFiltroDistribucion("numeroContrato_"+forma.getIndiceFiltroDistribucion()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return mapping.findForward("filtroDistribucion");
	}

	/**
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param mapping
	 * @param request 
	 * @param usuario 
	 * @param paciente 
	 * @return
	 */
	private ActionForward accionLiquidarDistribucion(Connection con, DistribucionCuentaForm forma, DistribucionCuenta mundo, ActionMapping mapping, PersonaBasica paciente, UsuarioBasico usuario, HttpServletRequest request) throws IPSException
	{
		ActionErrors errores = new ActionErrors(); 
		boolean backupExitoso = false;

		//Manejo historicos de cargos eliminados en el proceso de liquidacion automatica
		backupExitoso = crearHistoricoCargosXLiquidacionDistribucion(forma, usuario, paciente, ConstantesIntegridadDominio.acronimoAutomatica, null, forma.getTipoDistribucion());
		
		if(backupExitoso){
		
			boolean transaccion = false;
			
			try	{
				transaccion=UtilidadBD.iniciarTransaccion(con);
				
				if(transaccion)
				{
					// se agregan los campos de PersonaBasica paciente, UsuarioBasico usuario debido a que se debe realizar la insercion en la tabla ax_pacien en la funcion  reacomodarPrioridades en el sqlBase
					//modificado por anexo 779
					//----------------------------------------------------------------------------------------------------------
					ResultadoBoolean resultadoBoolean=mundo.reacomodarPrioridades(con,forma.getCodigoIngreso(), paciente, usuario );
					transaccion=resultadoBoolean.isTrue();
					if (!transaccion)
					{
						forma.setMostrarMensaje(new ResultadoBoolean(true,resultadoBoolean.getDescripcion()));
					
						return mapping.findForward("principal");
					}
				}
				//------------------------------------------------------------------------------------------------
				
				//actualizar/insertar datos encabezado distribucion.
				if(transaccion)
				{
					HashMap vo=new HashMap();
					vo.put("tipoDistribucion",forma.getTipoDistribucion());
					vo.put("usuarioModifica", usuario.getLoginUsuario());
					vo.put("fechaModifica",UtilidadFecha.getFechaActual());
					vo.put("horaModifica", UtilidadFecha.getHoraActual());
					vo.put("ingreso", forma.getCodigoIngreso());
					HashMap mapa = mundo.consultarEncabezadoUltimaDistribucion(con,forma.getCodigoIngreso());
					if(Utilidades.convertirAEntero(mapa.get("numRegistros")+"", false)>0){
						transaccion=mundo.actualizarEncabezadoDistribucion(con,vo);
					}
					else
					{
						transaccion=mundo.insertarEncabezadoDistribucion(con,vo);
					}
				}
				
				
				if(transaccion)
					transaccion=procesoLiquidacionAutomatica(con,forma,mundo,usuario,paciente);
				if(transaccion)
				{
					for(int a=0;a<forma.getResponsablesEliminados().size();a++)
					{
						transaccion=mundo.eliminarSubCuenta(con,forma.getResponsablesEliminados().get(a),forma.getResponsables().get(forma.getResponsables().size()-1).getSubCuentaDouble(),forma.getResponsables().get(forma.getResponsables().size()-1).getConvenio().getCodigo(),true);
						if(!transaccion)
							a=forma.getResponsablesEliminados().size();
					}
					
					//si se elimina algun responsable, se deben reacomodar las prioridades
					if(forma.getResponsablesEliminados().size()>0)
					{
						if(transaccion)
						{
							// se agregan los campos de PersonaBasica paciente, UsuarioBasico usuario debido a que se debe realizar la insercion en la tabla ax_pacien en la funcion  reacomodarPrioridades en el sqlBase
							//modificado por anexo 779
							//----------------------------------------------------------------------------------------------------------
							ResultadoBoolean resultadoBoolean=mundo.reacomodarPrioridades(con,forma.getCodigoIngreso(), paciente, usuario );
							transaccion=resultadoBoolean.isTrue();
							if (!transaccion)
							{
								forma.setMostrarMensaje(new ResultadoBoolean(true,resultadoBoolean.getDescripcion()));
							
								return mapping.findForward("principal");
							}
						}
					}
	
				}
		
			}
			catch(IPSException ipse){
				transaccion=false;
				UtilidadBD.abortarTransaccion(con);
				Log4JManager.error(ipse.getMessage(), ipse);
				errores.add("", new ActionMessage(ipse.getErrorCode().toString()));
			}catch(Exception e){
				transaccion=false;
				UtilidadBD.abortarTransaccion(con);
				Log4JManager.error(e.getMessage(), e);
				errores.add("", new ActionMessage(KEY_ERROR_NO_ESPECIFIC, e.getMessage()));
			}

			if(!errores.isEmpty()){
				saveErrors(request, errores);
			}
			
		if(transaccion)
		{
			UtilidadBD.finalizarTransaccion(con);
			forma.setMostrarMensaje(new ResultadoBoolean(true,"Liquidacion realizada exitosamente."));
			if(forma.getResponsablesEliminados().size()>0)
				forma.setMostrarMensaje(new ResultadoBoolean(true,"Liquidacion realizada exitosamente.\nResponsable(s) eliminado(s) exitosamente."));
			
			this.cargarPacienteIngresoSession(con,forma,paciente,usuario,paciente.getCodigoPersona(),request);

		}
		else
		{
			UtilidadBD.abortarTransaccion(con);
			forma.setMostrarMensaje(new ResultadoBoolean(true,"No se pudo liquidar la Distribucion"));
			if(forma.getResponsablesEliminados().size()>0)
				forma.setMostrarMensaje(new ResultadoBoolean(true,"No se pudo liquidar la Distribucion. No se pudieron eliminar los responsables."));
		}
			
		}else {
			errores.add("", new ActionMessage("error.distribucion.backupCancelado"));
			saveErrors(request, errores);
		}
		
		logger.info("\n\n\n\n\n\n\n\n\nTERMINA EL PROCESO DE DISTRIBUCION --->"+UtilidadFecha.getHoraSegundosActual(con)+"\n\n\n\n\n\n\n\n\n");
		return this.accionCargarInformacionGeneral(con,forma,mundo,paciente,usuario,mapping,request);
	}
	
	

	/**
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param usuario
	 * @param paciente
	 * @param request 
	 * @return
	 */
	private boolean procesoLiquidacionAutomatica(Connection con, DistribucionCuentaForm forma, DistribucionCuenta mundo, UsuarioBasico usuario, PersonaBasica paciente) throws IPSException
	{
		boolean exitoso=true;
		String[] excluirResponsables = new String[forma.getResponsablesEliminados().size()];
		String nroAutorizacion = "";
		
		try {
		
			for(int a=0;a<forma.getResponsablesEliminados().size();a++)
			{
				excluirResponsables[a]=forma.getResponsablesEliminados().get(a).getSubCuenta();
			}
			//se consultan los responsables que no han sido facturados.
			forma.setResponsables(UtilidadesHistoriaClinica.obtenerResponsablesIngreso(con,forma.getCodigoIngreso(),true,excluirResponsables,false, "" /*subCuenta*/,forma.getUltimaViaIngreso()));
			
			//Utilidades.imprimirArrayList(forma.getResponsables());
			String[] subCuentasResponsables = new String[forma.getResponsables().size()];
			///cargar los filtros de la distribucion
			for(int a=0;a<forma.getResponsables().size();a++)
			{
				this.accionCargarFiltroDistribucionResponsable(con,forma,mundo,a);
				subCuentasResponsables[a]=forma.getResponsables().get(a).getSubCuenta();
			}
			//se consultan las solicitudes que se distribuiran automaticamente.
			if(forma.getTipoDistribucion().trim().equals(ConstantesIntegridadDominio.acronimoTipoDistribucionCantidad))
			{
				int[] estados={ConstantesBD.codigoEstadoFCargada,ConstantesBD.codigoEstadoFExento,ConstantesBD.codigoEstadoFPendiente,ConstantesBD.codigoEstadoFAnulada,ConstantesBD.codigoEstadoFInactiva};
				forma.setDetSolicitudesPaciente(mundo.consultarDetSolicitudesPaciente(con,estados,false,excluirResponsables,subCuentasResponsables,true));
			}
			else
			{
				int[] estados={ConstantesBD.codigoEstadoFCargada,ConstantesBD.codigoEstadoFExento,ConstantesBD.codigoEstadoFAnulada,ConstantesBD.codigoEstadoFInactiva};
				forma.setDetSolicitudesPaciente(mundo.consultarDetSolicitudesPaciente(con,estados,false,excluirResponsables,subCuentasResponsables,true));
			}
			
			if(forma.getTipoDistribucion().trim().equals(ConstantesIntegridadDominio.acronimoTipoDistribucionPorcentual))
			{
				//logger.info("\n\n ........> pase por aqui --> 2 ....................");
				HashMap esquemasCirugias=new HashMap();
				int numRegistros=0;
				HashMap distribucionInsertar=new HashMap();
				//recorro cada solicitud.
				for(int a=0;a<Utilidades.convertirAEntero(forma.getDetSolicitudesPaciente("numRegistros")+"");a++)
				{
					//si tiene responsables facturados no se toma en cuenta para la liquidacion.
					if((Utilidades.convertirAEntero(forma.getDetSolicitudesPaciente("numresfactservicio_"+a)+"")+Utilidades.convertirAEntero(forma.getDetSolicitudesPaciente("numresfactarticulo_"+a)+""))==0)
					{
						//recorro cada responsable.
						int indiceRespValido=ConstantesBD.codigoNuncaValido;
						
						int numSolicitud=Utilidades.convertirAEntero(forma.getDetSolicitudesPaciente("solicitud_"+a)+"");
						String fechaCalculosCargos=Cargos.obtenerFechaCalculoCargo(con, numSolicitud);
						
						
						for(int j=0;j<forma.getResponsables().size();j++)
						{
							//logger.info("\n\n ........> pase por aqui --> "+forma.getResponsables().get(j));
							///analizar a que responsable le aplica el filtro.
							//@todo pendiente que margarita informe si se debe validar la tarifa
							if(validoServArtParaResponsable(con,forma,a,j,false,true,false,numSolicitud,usuario,forma.getResponsables().get(j)))
							{
								indiceRespValido=j;
								j=forma.getResponsables().size();
							}
						}
		
						//no es contenido en ninguno. se asigna al ultimo responsable
						if(indiceRespValido==ConstantesBD.codigoNuncaValido)
							indiceRespValido=(forma.getResponsables().size()-1);
		
						
						double valorTarifaBase=0;
						
						int esquemaTarifario=ConstantesBD.codigoNuncaValido;
						
						if(!UtilidadTexto.isEmpty(forma.getDetSolicitudesPaciente("servicio_"+a)+""))
						{
							esquemaTarifario=forma.getResponsables().get(indiceRespValido).getEsquemaTarifarioServiciosPpalOoriginal(con,forma.getResponsables().get(indiceRespValido).getSubCuenta(),forma.getResponsables().get(indiceRespValido).getContrato(),Utilidades.convertirAEntero(forma.getDetSolicitudesPaciente("servicio_"+a)+""),fechaCalculosCargos, Cargos.obtenerCentroAtencionCargoSolicitud(con,numSolicitud));
							valorTarifaBase=Cargos.obtenerValorTarifaYExcepcion(con, forma.getResponsables().get(indiceRespValido), esquemaTarifario, usuario, Utilidades.convertirAEntero(forma.getDetSolicitudesPaciente("servicio_"+a)+""), true, Utilidades.convertirAEntero(forma.getDetSolicitudesPaciente("solicitud_"+a)+""),fechaCalculosCargos);
						}
						else if(!UtilidadTexto.isEmpty(forma.getDetSolicitudesPaciente("articulo_"+a)+""))
						{
							esquemaTarifario=forma.getResponsables().get(indiceRespValido).getEsquemaTarifarioArticuloPpalOoriginal(con,forma.getResponsables().get(indiceRespValido).getSubCuenta(),forma.getResponsables().get(indiceRespValido).getContrato(),Utilidades.convertirAEntero(forma.getDetSolicitudesPaciente("articulo_"+a)+""),fechaCalculosCargos, Cargos.obtenerCentroAtencionCargoSolicitud(con,numSolicitud));
							valorTarifaBase=Cargos.obtenerValorTarifaYExcepcion(con, forma.getResponsables().get(indiceRespValido), esquemaTarifario, usuario, Utilidades.convertirAEntero(forma.getDetSolicitudesPaciente("articulo_"+a)+""), false, Utilidades.convertirAEntero(forma.getDetSolicitudesPaciente("solicitud_"+a)+""),fechaCalculosCargos);
						}
						
						if(Integer.parseInt(forma.getDetSolicitudesPaciente("tiposolicitud_"+a)+"")==ConstantesBD.codigoTipoSolicitudCirugia&&!UtilidadTexto.isEmpty(forma.getDetSolicitudesPaciente("servicio_"+a)+""))
						{
							if(esquemasCirugias.containsKey("esquema_"+forma.getDetSolicitudesPaciente("solicitud_"+a)+"_"+forma.getDetSolicitudesPaciente("servicio_"+a)+"_"+forma.getDetSolicitudesPaciente("serviciocx_"+a)+"_"+forma.getDetSolicitudesPaciente("detcxhonorarios_"+forma.getIndice())+"_"+forma.getDetSolicitudesPaciente("detascxsalmat_"+forma.getIndice())+""))
							{
								esquemaTarifario=Utilidades.convertirAEntero((esquemasCirugias.get("esquema_"+forma.getDetSolicitudesPaciente("solicitud_"+a)+"_"+forma.getDetSolicitudesPaciente("servicio_"+a)+"_"+forma.getDetSolicitudesPaciente("serviciocx_"+a)+"_"+forma.getDetSolicitudesPaciente("detcxhonorarios_"+forma.getIndice())+"_"+forma.getDetSolicitudesPaciente("detascxsalmat_"+forma.getIndice())+""))+"");
							}
							else
							{
								esquemaTarifario=UtilidadesFacturacion.obtenerEsquemaTarifarioBase(con,forma.getDetSolicitudesPaciente("solicitud_"+a)+"",forma.getDetSolicitudesPaciente("servicio_"+a)+"",forma.getDetSolicitudesPaciente("serviciocx_"+a)+"",forma.getDetSolicitudesPaciente("detcxhonorarios_"+a)+"",forma.getDetSolicitudesPaciente("detascxsalmat_"+a)+"",true,ConstantesBD.acronimoNo);
								esquemasCirugias.put("esquema_"+forma.getDetSolicitudesPaciente("solicitud_"+a)+"_"+forma.getDetSolicitudesPaciente("servicio_"+a)+"_"+forma.getDetSolicitudesPaciente("serviciocx_"+a)+"_"+forma.getDetSolicitudesPaciente("detcxhonorarios_"+forma.getIndice())+"_"+forma.getDetSolicitudesPaciente("detascxsalmat_"+forma.getIndice()),esquemaTarifario+"");
							}
							if(esquemasCirugias.containsKey("valor_"+forma.getDetSolicitudesPaciente("solicitud_"+a)+"_"+forma.getDetSolicitudesPaciente("servicio_"+a)+"_"+forma.getDetSolicitudesPaciente("serviciocx_"+a)+"_"+forma.getDetSolicitudesPaciente("detcxhonorarios_"+a)+"_"+forma.getDetSolicitudesPaciente("detascxsalmat_"+a)))
							{
								valorTarifaBase=UtilidadesFacturacion.obtenerValorUnitarioCargadoConvenioBase(con,forma.getDetSolicitudesPaciente("solicitud_"+a)+"",forma.getDetSolicitudesPaciente("servicio_"+a)+"",forma.getDetSolicitudesPaciente("serviciocx_"+a)+"",forma.getDetSolicitudesPaciente("detcxhonorarios_"+a)+"",forma.getDetSolicitudesPaciente("detascxsalmat_"+a)+"",true,ConstantesBD.acronimoNo);
							}
							else
							{
								valorTarifaBase=UtilidadesFacturacion.obtenerValorUnitarioCargadoConvenioBase(con,forma.getDetSolicitudesPaciente("solicitud_"+a)+"",forma.getDetSolicitudesPaciente("servicio_"+a)+"",forma.getDetSolicitudesPaciente("serviciocx_"+a)+"",forma.getDetSolicitudesPaciente("detcxhonorarios_"+a)+"",forma.getDetSolicitudesPaciente("detascxsalmat_"+a)+"",true,ConstantesBD.acronimoNo);
								esquemasCirugias.put("valor_"+forma.getDetSolicitudesPaciente("solicitud_"+a)+"_"+forma.getDetSolicitudesPaciente("servicio_"+a)+"_"+forma.getDetSolicitudesPaciente("serviciocx_"+a)+"_"+forma.getDetSolicitudesPaciente("detcxhonorarios_"+a)+"_"+forma.getDetSolicitudesPaciente("detascxsalmat_"+a),valorTarifaBase+"");
							}
						}
						
						if(!UtilidadTexto.isEmpty(forma.getDetSolicitudesPaciente("servicio_"+a)+""))
							exitoso=mundo.eliminarSolicitudSubCuentaDetCargoXSolServArt(con,numSolicitud,forma.getDetSolicitudesPaciente("servicio_"+a)+"",forma.getDetSolicitudesPaciente("serviciocx_"+a)+"",forma.getDetSolicitudesPaciente("detcxhonorarios_"+a)+"",forma.getDetSolicitudesPaciente("detascxsalmat_"+a)+"",true,Utilidades.convertirAEntero(forma.getDetSolicitudesPaciente("estadocargo_"+a)+""));
						else if(!UtilidadTexto.isEmpty(forma.getDetSolicitudesPaciente("articulo_"+a)+""))
							exitoso=mundo.eliminarSolicitudSubCuentaDetCargoXSolServArt(con,numSolicitud,forma.getDetSolicitudesPaciente("articulo_"+a)+"","","","",false,Utilidades.convertirAEntero(forma.getDetSolicitudesPaciente("estadocargo_"+a)+""));
	
						/*
						if(exitoso)
						{
							if(!UtilidadTexto.isEmpty(forma.getDetSolicitudesPaciente("servicio_"+a)+""))
								exitoso=mundo.eliminarSolicitudSubCuentaXSolServArt(con,numSolicitud,forma.getDetSolicitudesPaciente("servicio_"+a)+"",forma.getDetSolicitudesPaciente("serviciocx_"+a)+"",true);
							else if(!UtilidadTexto.isEmpty(forma.getDetSolicitudesPaciente("articulo_"+a)+""))
								exitoso=mundo.eliminarSolicitudSubCuentaXSolServArt(con,numSolicitud,forma.getDetSolicitudesPaciente("articulo_"+a)+"","",false);
						}
						*/
		
						if(exitoso)
						{
							int cantidadCargada=Utilidades.convertirAEntero(forma.getDetSolicitudesPaciente("cantidad_"+a)+"");
							
							double porcentajeFaltaAsignar=100; //al iniciar falta el 100%
							double valorTotalFaltaAsignar=cantidadCargada*valorTarifaBase;//falta todo el valor por asignar
			
							for(int j=indiceRespValido;j<forma.getResponsables().size();j++)
							{
								boolean cargoAnulInactivo=(forma.getDetSolicitudesPaciente("estadocargo_"+a)+"").trim().equals(ConstantesBD.codigoEstadoFAnulada+"")||(forma.getDetSolicitudesPaciente("estadocargo_"+a)+"").trim().equals(ConstantesBD.codigoEstadoFInactiva+"");
								if(!cargoAnulInactivo||(cargoAnulInactivo&&j==(forma.getResponsables().size()-1)))//incluir las anuladas e inactivas, solo si es el ultimo responsalbe.
								{
									if(valorTotalFaltaAsignar>0)
									{
										boolean puedoDistribuir=true;
										
										//validacion solicitudes pyp
										boolean esSolPyp = Utilidades.esSolicitudPYP(con, numSolicitud); 
										if(esSolPyp)
										{
											if((UtilidadValidacion.esConvenioPYP(con, forma.getResponsables().get(j).getConvenio().getCodigo()+"")&&Utilidades.esActividadSolicitudPYPCubiertaConvenio(con,numSolicitud+"",paciente.getCodigoPersona()+"",forma.getResponsables().get(j).getConvenio().getCodigo()+""))||j==(forma.getResponsables().size()-1))
												puedoDistribuir=true;
											else
												puedoDistribuir=false;
										}
		
										//validar los filtros del responsable o sino asignarlos al ultimo.
										//solo filtrar los filtros de la distribucion
		
										if(puedoDistribuir&&(validoServArtParaResponsable(con,forma,a,j,false,false,false,numSolicitud, usuario,forma.getResponsables().get(j))||j==(forma.getResponsables().size()-1)))
										{
					
											DtoSubCuentas responsable=forma.getResponsables().get(j);
		
											InfoTarifa descuento=new InfoTarifa();
											
											if(!UtilidadTexto.isEmpty(forma.getDetSolicitudesPaciente("servicio_"+a)+""))
											{
												//en caso de que la cobertura y el descuento sea por cirugia debe hacerse esta validacion  para el serviciocx_
												if(Integer.parseInt(forma.getDetSolicitudesPaciente("tiposolicitud_"+a)+"")==ConstantesBD.codigoTipoSolicitudCirugia&&!UtilidadTexto.isEmpty(forma.getDetSolicitudesPaciente("servicio_"+a)+""))
												{
													descuento=Cargos.obtenerDescuentoComercialXConvenioServicio(con, Utilidades.convertirAEntero(forma.getDetSolicitudesPaciente("viaingreso_"+a)+""), forma.getDetSolicitudesPaciente("tipopaciente_"+a)+"",responsable.getContrato(), Utilidades.convertirAEntero(forma.getDetSolicitudesPaciente("serviciocx_"+a)+""), usuario.getCodigoInstitucionInt(),fechaCalculosCargos);
												}
												else
												{
													descuento=Cargos.obtenerDescuentoComercialXConvenioServicio(con, Utilidades.convertirAEntero(forma.getDetSolicitudesPaciente("viaingreso_"+a)+""), forma.getDetSolicitudesPaciente("tipopaciente_"+a)+"",responsable.getContrato(), Utilidades.convertirAEntero(forma.getDetSolicitudesPaciente("servicio_"+a)+""), usuario.getCodigoInstitucionInt(),fechaCalculosCargos);
												}
		
					
											}
	
											distribucionInsertar.put("solicitud_"+numRegistros,numSolicitud+"");
											distribucionInsertar.put("cuenta_"+numRegistros,Utilidades.getCuentaSolicitud(con, numSolicitud));
											distribucionInsertar.put("subcuenta_"+numRegistros,responsable.getSubCuenta());
											distribucionInsertar.put("servicio_"+numRegistros, forma.getDetSolicitudesPaciente("servicio_"+a)+"");
											distribucionInsertar.put("articulo_"+numRegistros, forma.getDetSolicitudesPaciente("articulo_"+a)+"");
											distribucionInsertar.put("cantidad_"+numRegistros, cantidadCargada+"");
											distribucionInsertar.put("cubierto_"+numRegistros, ConstantesBD.acronimoSi+"");
											distribucionInsertar.put("tiposolicitud_"+numRegistros, forma.getDetSolicitudesPaciente("tiposolicitud_"+a)+"");
											distribucionInsertar.put("serviciocx_"+numRegistros, forma.getDetSolicitudesPaciente("serviciocx_"+a)+"");
											distribucionInsertar.put("detcxhonorarios_"+numRegistros, forma.getDetSolicitudesPaciente("detcxhonorarios_"+a)+"");
											distribucionInsertar.put("detascxsalmat_"+numRegistros, forma.getDetSolicitudesPaciente("detascxsalmat_"+a)+"");
											distribucionInsertar.put("tipoasocio_"+numRegistros, UtilidadTexto.isEmpty(forma.getDetSolicitudesPaciente("tipoasocio_"+a)+"")?ConstantesBD.codigoNuncaValido:Utilidades.convertirAEntero(forma.getDetSolicitudesPaciente("tipoasocio_"+a)+""));
											distribucionInsertar.put("tipodistribucion_"+numRegistros, forma.getTipoDistribucion());
											distribucionInsertar.put("paquetizada_"+numRegistros, ConstantesBD.acronimoNo);
											distribucionInsertar.put("solsubcuentapadre_"+numRegistros, "");
											
											///si es el ultimo responsable se le asigna el restante.
											double porAsignar=Utilidades.convertirADouble(responsable.getPorcentajeAutorizado(),true);
											if(j==(forma.getResponsables().size()-1))
												porAsignar=porcentajeFaltaAsignar;
											distribucionInsertar.put("porcentaje_"+numRegistros, porAsignar);
											porcentajeFaltaAsignar=porcentajeFaltaAsignar-porAsignar;
											
											double valTotalCarg=cantidadCargada*valorTarifaBase*porAsignar/100;
											if(j==(forma.getResponsables().size()-1))
												valTotalCarg=valorTotalFaltaAsignar;
											distribucionInsertar.put("valortotalcargado_"+numRegistros, valTotalCarg+"");
											valorTotalFaltaAsignar=valorTotalFaltaAsignar-valTotalCarg;
											
											nroAutorizacion = forma.getDetSolicitudesPaciente("nro_autorizacion_"+a)+"";
											if(UtilidadTexto.isEmpty(nroAutorizacion))
											{
												nroAutorizacion = responsable.getNroAutorizacion();
											}
											distribucionInsertar.put("autorizacion_"+numRegistros, nroAutorizacion);
											distribucionInsertar.put("valorbasedistribucion_"+numRegistros, valorTarifaBase);
											distribucionInsertar.put("valorunitariocargado_"+numRegistros, valTotalCarg/cantidadCargada);
											distribucionInsertar.put("esquematarifario_"+numRegistros, esquemaTarifario);
											
											if(descuento.getPorcentajes().size()>0)
												distribucionInsertar.put("porcentajedcto_"+numRegistros, descuento.getPorcentajes().get(0));
											else
												distribucionInsertar.put("porcentajedcto_"+numRegistros, "");
											
											distribucionInsertar.put("valorunitariodcto_"+numRegistros, descuento.getValor());
											if (forma.getDetSolicitudesPaciente("estadocargo_"+a).toString().equals("5"))
											{distribucionInsertar.put("estado_"+numRegistros, forma.getDetSolicitudesPaciente("estadocargo_"+a)+"");}
											else{
											if(cargoAnulInactivo)
												{distribucionInsertar.put("estado_"+numRegistros, forma.getDetSolicitudesPaciente("estadocargo_"+a)+"");}
											else
												{distribucionInsertar.put("estado_"+numRegistros, ConstantesBD.codigoEstadoFCargada);}
											}distribucionInsertar.put("requiereautorizacion_"+numRegistros,ConstantesBD.acronimoNo+"");
											distribucionInsertar.put("convenio_"+numRegistros, responsable.getConvenio().getCodigo());
											distribucionInsertar.put("contrato_"+numRegistros, responsable.getContrato());
											distribucionInsertar.put("viaingreso_"+numRegistros, forma.getDetSolicitudesPaciente("viaingreso_"+a)+"");
											numRegistros++;
										}
									}	
								}
							}
						}
						else
						{
							exitoso=false;
							a=Utilidades.convertirAEntero(forma.getDetSolicitudesPaciente("numRegistros")+"");
						}
					}
				}
				distribucionInsertar.put("numRegistros",numRegistros);
				exitoso=guardarDistribucionMapa(con,distribucionInsertar,mundo,usuario);
			}
			////////////////////FIN DISTRIBUCION TIPO PORCENTUAL
			else if(forma.getTipoDistribucion().trim().equals(ConstantesIntegridadDominio.acronimoTipodistribucionMonto))
			{
				//logger.info("\n\n ........> pase por aqui --> 3 ....................");
				int numRegistros=0;
				HashMap distribucionInsertar=new HashMap();
				HashMap esquemasCirugias=new HashMap();
				//recorro cada solicitud.
				
				for(int a=0;a<Utilidades.convertirAEntero(forma.getDetSolicitudesPaciente("numRegistros")+"");a++)
				{
					//si tiene responsables facturados no se toma en cuenta para la liquidacion.
					if((Utilidades.convertirAEntero(forma.getDetSolicitudesPaciente("numresfactservicio_"+a)+"")+Utilidades.convertirAEntero(forma.getDetSolicitudesPaciente("numresfactarticulo_"+a)+""))==0)
					{
						//recorro cada responsable.
						int indiceRespValido=ConstantesBD.codigoNuncaValido;
						
						int numSolicitud=Utilidades.convertirAEntero(forma.getDetSolicitudesPaciente("solicitud_"+a)+"");
						String fechaCalculosCargos=Cargos.obtenerFechaCalculoCargo(con, numSolicitud);
						
						for(int j=0;j<forma.getResponsables().size();j++)
						{
							///analizar a que responsable le aplica el filtro.
							//@todo pendiente que margarita informe si se debe validar la tarifa
		
							if(validoServArtParaResponsable(con,forma,a,j,true,true,true,numSolicitud , usuario, forma.getResponsables().get(j)))
							{
								indiceRespValido=j;
								j=forma.getResponsables().size();
							}
						}
		
						//no es contenido en ninguno. se asigna al ultimo responsable
						if(indiceRespValido==ConstantesBD.codigoNuncaValido)
							indiceRespValido=(forma.getResponsables().size()-1);
						
						double valorTarifaBase=0;
						
						int esquemaTarifario=ConstantesBD.codigoNuncaValido;
						if(!UtilidadTexto.isEmpty(forma.getDetSolicitudesPaciente("servicio_"+a)+""))
						{
							esquemaTarifario=forma.getResponsables().get(indiceRespValido).getEsquemaTarifarioServiciosPpalOoriginal(con,forma.getResponsables().get(indiceRespValido).getSubCuenta(),forma.getResponsables().get(indiceRespValido).getContrato(), Utilidades.convertirAEntero(forma.getDetSolicitudesPaciente("servicio_"+a)+""),fechaCalculosCargos, Cargos.obtenerCentroAtencionCargoSolicitud(con,numSolicitud));
							valorTarifaBase=Cargos.obtenerValorTarifaYExcepcion(con, forma.getResponsables().get(indiceRespValido), esquemaTarifario, usuario, Utilidades.convertirAEntero(forma.getDetSolicitudesPaciente("servicio_"+a)+""), true, Utilidades.convertirAEntero(forma.getDetSolicitudesPaciente("solicitud_"+a)+""),fechaCalculosCargos);
						}
						else if(!UtilidadTexto.isEmpty(forma.getDetSolicitudesPaciente("articulo_"+a)+""))
						{
							esquemaTarifario=forma.getResponsables().get(indiceRespValido).getEsquemaTarifarioArticuloPpalOoriginal(con,forma.getResponsables().get(indiceRespValido).getSubCuenta(),forma.getResponsables().get(indiceRespValido).getContrato(), Utilidades.convertirAEntero(forma.getDetSolicitudesPaciente("articulo_"+a)+""),fechaCalculosCargos, Cargos.obtenerCentroAtencionCargoSolicitud(con,numSolicitud));
							valorTarifaBase=Cargos.obtenerValorTarifaYExcepcion(con, forma.getResponsables().get(indiceRespValido), esquemaTarifario, usuario, Utilidades.convertirAEntero(forma.getDetSolicitudesPaciente("articulo_"+a)+""), false, Utilidades.convertirAEntero(forma.getDetSolicitudesPaciente("solicitud_"+a)+""),fechaCalculosCargos);
						}
						if(Integer.parseInt(forma.getDetSolicitudesPaciente("tiposolicitud_"+a)+"")==ConstantesBD.codigoTipoSolicitudCirugia&&!UtilidadTexto.isEmpty(forma.getDetSolicitudesPaciente("servicio_"+a)+""))
						{
							if(esquemasCirugias.containsKey("esquema_"+forma.getDetSolicitudesPaciente("solicitud_"+a)+"_"+forma.getDetSolicitudesPaciente("servicio_"+a)+"_"+forma.getDetSolicitudesPaciente("serviciocx_"+a)+"_"+forma.getDetSolicitudesPaciente("detcxhonorarios_"+a)+"_"+forma.getDetSolicitudesPaciente("detascxsalmat_"+a)))
							{
								esquemaTarifario=Utilidades.convertirAEntero((esquemasCirugias.get("esquema_"+forma.getDetSolicitudesPaciente("solicitud_"+a)+"_"+forma.getDetSolicitudesPaciente("servicio_"+a)+"_"+forma.getDetSolicitudesPaciente("serviciocx_"+a)+"_"+forma.getDetSolicitudesPaciente("detcxhonorarios_"+a)+"_"+forma.getDetSolicitudesPaciente("detascxsalmat_"+a)))+"");
							}
							else
							{
								esquemaTarifario=UtilidadesFacturacion.obtenerEsquemaTarifarioBase(con,forma.getDetSolicitudesPaciente("solicitud_"+a)+"",forma.getDetSolicitudesPaciente("servicio_"+a)+"",forma.getDetSolicitudesPaciente("serviciocx_"+a)+"",forma.getDetSolicitudesPaciente("detcxhonorarios_"+a)+"",forma.getDetSolicitudesPaciente("detascxsalmat_"+a)+"",true,ConstantesBD.acronimoNo);
								esquemasCirugias.put("esquema_"+forma.getDetSolicitudesPaciente("solicitud_"+a)+"_"+forma.getDetSolicitudesPaciente("servicio_"+a)+"_"+forma.getDetSolicitudesPaciente("serviciocx_"+a)+"_"+forma.getDetSolicitudesPaciente("detcxhonorarios_"+a)+"_"+forma.getDetSolicitudesPaciente("detascxsalmat_"+a),esquemaTarifario+"");
							}
							if(esquemasCirugias.containsKey("valor_"+forma.getDetSolicitudesPaciente("solicitud_"+a)+"_"+forma.getDetSolicitudesPaciente("servicio_"+a)+"_"+forma.getDetSolicitudesPaciente("serviciocx_"+a)+"_"+forma.getDetSolicitudesPaciente("detcxhonorarios_"+a)+"_"+forma.getDetSolicitudesPaciente("detascxsalmat_"+a)))
							{
								valorTarifaBase=UtilidadesFacturacion.obtenerValorUnitarioCargadoConvenioBase(con,forma.getDetSolicitudesPaciente("solicitud_"+a)+"",forma.getDetSolicitudesPaciente("servicio_"+a)+"",forma.getDetSolicitudesPaciente("serviciocx_"+a)+"",forma.getDetSolicitudesPaciente("detcxhonorarios_"+a)+"",forma.getDetSolicitudesPaciente("detascxsalmat_"+a)+"",true,ConstantesBD.acronimoNo);
							}
							else
							{
								valorTarifaBase=UtilidadesFacturacion.obtenerValorUnitarioCargadoConvenioBase(con,forma.getDetSolicitudesPaciente("solicitud_"+a)+"",forma.getDetSolicitudesPaciente("servicio_"+a)+"",forma.getDetSolicitudesPaciente("serviciocx_"+a)+"",forma.getDetSolicitudesPaciente("detcxhonorarios_"+a)+"",forma.getDetSolicitudesPaciente("detascxsalmat_"+a)+"",true,ConstantesBD.acronimoNo);
								esquemasCirugias.put("valor_"+forma.getDetSolicitudesPaciente("solicitud_"+a)+"_"+forma.getDetSolicitudesPaciente("servicio_"+a)+"_"+forma.getDetSolicitudesPaciente("serviciocx_"+a)+"_"+forma.getDetSolicitudesPaciente("detcxhonorarios_"+a)+"_"+forma.getDetSolicitudesPaciente("detascxsalmat_"+a),valorTarifaBase+"");
							}
						}
						if(!UtilidadTexto.isEmpty(forma.getDetSolicitudesPaciente("servicio_"+a)+""))
							exitoso=mundo.eliminarSolicitudSubCuentaDetCargoXSolServArt(con,numSolicitud,forma.getDetSolicitudesPaciente("servicio_"+a)+"",forma.getDetSolicitudesPaciente("serviciocx_"+a)+"",forma.getDetSolicitudesPaciente("detcxhonorarios_"+a)+"",forma.getDetSolicitudesPaciente("detascxsalmat_"+a)+"",true,Utilidades.convertirAEntero(forma.getDetSolicitudesPaciente("estadocargo_"+a)+""));
						else if(!UtilidadTexto.isEmpty(forma.getDetSolicitudesPaciente("articulo_"+a)+""))
							exitoso=mundo.eliminarSolicitudSubCuentaDetCargoXSolServArt(con,numSolicitud,forma.getDetSolicitudesPaciente("articulo_"+a)+"","","","",false,Utilidades.convertirAEntero(forma.getDetSolicitudesPaciente("estadocargo_"+a)+""));
						
						/*
						if(exitoso)
						{
							if(!UtilidadTexto.isEmpty(forma.getDetSolicitudesPaciente("servicio_"+a)+""))
								exitoso=mundo.eliminarSolicitudSubCuentaXSolServArt(con,numSolicitud,forma.getDetSolicitudesPaciente("servicio_"+a)+"",forma.getDetSolicitudesPaciente("serviciocx_"+a)+"",true);
							else if(!UtilidadTexto.isEmpty(forma.getDetSolicitudesPaciente("articulo_"+a)+""))
								exitoso=mundo.eliminarSolicitudSubCuentaXSolServArt(con,numSolicitud,forma.getDetSolicitudesPaciente("articulo_"+a)+"","",false);
						}
						*/
		
						if(exitoso)
						{
							int cantidadCargada=Utilidades.convertirAEntero(forma.getDetSolicitudesPaciente("cantidad_"+a)+"");
							
							double valTotalCarg=cantidadCargada*valorTarifaBase;
							double valorTotalServicioTemporal=valTotalCarg;
							
							for(int j=indiceRespValido;j<forma.getResponsables().size();j++)
							{
								boolean cargoAnulInactivo=(forma.getDetSolicitudesPaciente("estadocargo_"+a)+"").trim().equals(ConstantesBD.codigoEstadoFAnulada+"")||(forma.getDetSolicitudesPaciente("estadocargo_"+a)+"").trim().equals(ConstantesBD.codigoEstadoFInactiva+"");
								if(!cargoAnulInactivo||(cargoAnulInactivo&&j==(forma.getResponsables().size()-1)))//incluir las anuladas e inactivas, solo si es el ultimo responsalbe.
								{
									if(valTotalCarg>0)
									{
										boolean puedoDistribuir=true;
		
										//validacion solicitudes pyp
										boolean esSolPyp = Utilidades.esSolicitudPYP(con, numSolicitud); 
										if(esSolPyp)
										{
											if((UtilidadValidacion.esConvenioPYP(con, forma.getResponsables().get(j).getConvenio().getCodigo()+"")&&Utilidades.esActividadSolicitudPYPCubiertaConvenio(con,numSolicitud+"",paciente.getCodigoPersona()+"",forma.getResponsables().get(j).getConvenio().getCodigo()+""))||j==(forma.getResponsables().size()-1))
												puedoDistribuir=true;
											else
												puedoDistribuir=false;
										}
										
										//validar los filtros del responsable o sino asignarlos al ultimo.
										if((puedoDistribuir&&(validoServArtParaResponsable(con,forma,a,j,false,false,true,numSolicitud,usuario,forma.getResponsables().get(j)))||j==(forma.getResponsables().size()-1)))
										{
											DtoSubCuentas responsable=forma.getResponsables().get(j);
											
											InfoCobertura infoCobertura=new InfoCobertura();
											InfoTarifa descuento=new InfoTarifa();
											
											if(!UtilidadTexto.isEmpty(forma.getDetSolicitudesPaciente("servicio_"+a)+""))
											{
												//en caso de que la cobertura y el descuento sea por cirugia debe hacerse esta validacion  para el serviciocx_
												if(Integer.parseInt(forma.getDetSolicitudesPaciente("tiposolicitud_"+a)+"")==ConstantesBD.codigoTipoSolicitudCirugia)
												{
													infoCobertura=Cobertura.validacionCoberturaServicioDadoResponsable(con, responsable, Utilidades.convertirAEntero(forma.getDetSolicitudesPaciente("viaingreso_"+a)+""),forma.getDetSolicitudesPaciente("tipopaciente_"+a)+"",Utilidades.convertirAEntero(forma.getDetSolicitudesPaciente("serviciocx_"+a)+""), usuario.getCodigoInstitucionInt());
													descuento=Cargos.obtenerDescuentoComercialXConvenioServicio(con, Utilidades.convertirAEntero(forma.getDetSolicitudesPaciente("viaingreso_"+a)+""), forma.getDetSolicitudesPaciente("tipopaciente_"+a)+"",responsable.getContrato(), Utilidades.convertirAEntero(forma.getDetSolicitudesPaciente("serviciocx_"+a)+""), usuario.getCodigoInstitucionInt(),fechaCalculosCargos);
												}
												else
												{
													infoCobertura=Cobertura.validacionCoberturaServicioDadoResponsable(con, responsable, Utilidades.convertirAEntero(forma.getDetSolicitudesPaciente("viaingreso_"+a)+""), forma.getDetSolicitudesPaciente("tipopaciente_"+a)+"",Utilidades.convertirAEntero(forma.getDetSolicitudesPaciente("servicio_"+a)+""), usuario.getCodigoInstitucionInt());
													descuento=Cargos.obtenerDescuentoComercialXConvenioServicio(con, Utilidades.convertirAEntero(forma.getDetSolicitudesPaciente("viaingreso_"+a)+""), forma.getDetSolicitudesPaciente("tipopaciente_"+a)+"",responsable.getContrato(), Utilidades.convertirAEntero(forma.getDetSolicitudesPaciente("servicio_"+a)+""), usuario.getCodigoInstitucionInt(),fechaCalculosCargos);
												}
					
											}
											else if(!UtilidadTexto.isEmpty(forma.getDetSolicitudesPaciente("articulo_"+a)+""))
											{
												infoCobertura=Cobertura.validacionCoberturaArticuloDadoResponsable(con, responsable,Utilidades.convertirAEntero(forma.getDetSolicitudesPaciente("viaingreso_"+a)+""), forma.getDetSolicitudesPaciente("tipopaciente_"+a)+"" ,Utilidades.convertirAEntero(forma.getDetSolicitudesPaciente("articulo_"+a)+""), usuario.getCodigoInstitucionInt());
											}
											
											distribucionInsertar.put("solicitud_"+numRegistros,numSolicitud+"");
											distribucionInsertar.put("cuenta_"+numRegistros,Utilidades.getCuentaSolicitud(con, numSolicitud));
											distribucionInsertar.put("subcuenta_"+numRegistros,responsable.getSubCuenta());
											distribucionInsertar.put("servicio_"+numRegistros, forma.getDetSolicitudesPaciente("servicio_"+a)+"");
											distribucionInsertar.put("articulo_"+numRegistros, forma.getDetSolicitudesPaciente("articulo_"+a)+"");
											distribucionInsertar.put("cantidad_"+numRegistros, cantidadCargada+"");
											distribucionInsertar.put("cubierto_"+numRegistros, infoCobertura.getIncluidoStr()+"");
											distribucionInsertar.put("tiposolicitud_"+numRegistros, forma.getDetSolicitudesPaciente("tiposolicitud_"+a)+"");
											distribucionInsertar.put("serviciocx_"+numRegistros, forma.getDetSolicitudesPaciente("serviciocx_"+a)+"");
											distribucionInsertar.put("detcxhonorarios_"+numRegistros, forma.getDetSolicitudesPaciente("detcxhonorarios_"+a)+"");
											distribucionInsertar.put("detascxsalmat_"+numRegistros, forma.getDetSolicitudesPaciente("detascxsalmat_"+a)+"");
											distribucionInsertar.put("tipoasocio_"+numRegistros, UtilidadTexto.isEmpty(forma.getDetSolicitudesPaciente("tipoasocio_"+a)+"")?ConstantesBD.codigoNuncaValido:Utilidades.convertirAEntero(forma.getDetSolicitudesPaciente("tipoasocio_"+a)+""));
											distribucionInsertar.put("tipodistribucion_"+numRegistros, forma.getTipoDistribucion());
											distribucionInsertar.put("paquetizada_"+numRegistros, ConstantesBD.acronimoNo);
											distribucionInsertar.put("solsubcuentapadre_"+numRegistros, "");
											
											///si es el ultimo responsable se le asigna el restante.
											distribucionInsertar.put("porcentaje_"+numRegistros, ConstantesBD.codigoNuncaValido+"");
											double valorFacturadoOtroIngresosSoat=mundo.obtenerValorFacturadoSoat(con,forma.getCodigoIngreso(),responsable.getConvenio().getCodigo());
											if(j==(forma.getResponsables().size()-1))
											{
												distribucionInsertar.put("valortotalcargado_"+numRegistros, valTotalCarg+"");
												if(!UtilidadTexto.isEmpty(responsable.getMontoAutorizado()))
													responsable.setMontoAutorizado((Utilidades.convertirADouble(responsable.getMontoAutorizado(),true)-valTotalCarg)+"");
												valTotalCarg=0;
											}
											else
											{
												if(!UtilidadTexto.isEmpty(responsable.getMontoAutorizado()))
												{
													double valorTotalPaquetesResponsable = Utilidades.obtenerValorTotalPaquetesResponsable(con, responsable.getSubCuenta());
													if((Utilidades.convertirADouble(responsable.getMontoAutorizado(),true)-valorTotalPaquetesResponsable-valorFacturadoOtroIngresosSoat-Utilidades.convertirADouble(responsable.getValorUtilizadoSoat(),true))>valTotalCarg)
													{
														
														distribucionInsertar.put("valortotalcargado_"+numRegistros, valTotalCarg+"");
														responsable.setMontoAutorizado((Utilidades.convertirADouble(responsable.getMontoAutorizado(),true)-valTotalCarg)+"");										
														valTotalCarg=0;
													}
													else
													{
														double valorAsignar=(Utilidades.convertirADouble(responsable.getMontoAutorizado(),true)-valorTotalPaquetesResponsable-valorFacturadoOtroIngresosSoat-Utilidades.convertirADouble(responsable.getValorUtilizadoSoat(),true));
														distribucionInsertar.put("valortotalcargado_"+numRegistros, valorAsignar+"");
														valTotalCarg=valTotalCarg-(Utilidades.convertirADouble(responsable.getMontoAutorizado(),true)-valorTotalPaquetesResponsable-valorFacturadoOtroIngresosSoat-Utilidades.convertirADouble(responsable.getValorUtilizadoSoat(),true));
														responsable.setMontoAutorizado((Utilidades.convertirADouble(responsable.getMontoAutorizado(),true)-valorAsignar)+"");												
													}
												}
												else
												{
													distribucionInsertar.put("valortotalcargado_"+numRegistros, valTotalCarg+"");
													valTotalCarg=0;
												}
			
											}
											nroAutorizacion = forma.getDetSolicitudesPaciente("nro_autorizacion_"+a)+"";
											if(UtilidadTexto.isEmpty(nroAutorizacion))
											{
												nroAutorizacion = responsable.getNroAutorizacion();
											}
											distribucionInsertar.put("autorizacion_"+numRegistros, nroAutorizacion);
											distribucionInsertar.put("valorbasedistribucion_"+numRegistros, valorTarifaBase);
											distribucionInsertar.put("valorunitariocargado_"+numRegistros, valTotalCarg/cantidadCargada);
											distribucionInsertar.put("esquematarifario_"+numRegistros, esquemaTarifario);
											if(descuento.getPorcentajes().size()>0)
												distribucionInsertar.put("porcentajedcto_"+numRegistros, descuento.getPorcentajes().get(0));
											else
												distribucionInsertar.put("porcentajedcto_"+numRegistros, "");
											distribucionInsertar.put("valorunitariodcto_"+numRegistros, descuento.getValor());
											if (forma.getDetSolicitudesPaciente("estadocargo_"+a).toString().equals("5"))
											{distribucionInsertar.put("estado_"+numRegistros, forma.getDetSolicitudesPaciente("estadocargo_"+a)+"");}
											else{
											if(cargoAnulInactivo)
												{distribucionInsertar.put("estado_"+numRegistros, forma.getDetSolicitudesPaciente("estadocargo_"+a)+"");}
											else
												{distribucionInsertar.put("estado_"+numRegistros, ConstantesBD.codigoEstadoFCargada);}
											}distribucionInsertar.put("requiereautorizacion_"+numRegistros,infoCobertura.getRequiereAutorizacionStr());
											distribucionInsertar.put("convenio_"+numRegistros, responsable.getConvenio().getCodigo());
											distribucionInsertar.put("contrato_"+numRegistros, responsable.getContrato());
											distribucionInsertar.put("viaingreso_"+numRegistros, forma.getDetSolicitudesPaciente("viaingreso_"+a)+"");
											distribucionInsertar.put("varlRealServicio_"+numRegistros, valorTotalServicioTemporal+"");
											numRegistros++;
											
											
										}
									}
								}
							}
						}
						else
						{
							exitoso=false;
							a=Utilidades.convertirAEntero(forma.getDetSolicitudesPaciente("numRegistros")+"");
						}
					}
				}
				distribucionInsertar.put("numRegistros",numRegistros);
				exitoso=guardarDistribucionMapa(con,distribucionInsertar,mundo,usuario);
			}
			////////////////FIN DISTRIBUCION POR MONTO
			
			else if(forma.getTipoDistribucion().trim().equals(ConstantesIntegridadDominio.acronimoTipoDistribucionCantidad))
			{
				//logger.info("\n\n ........> pase por aqui --> 4 ....................");
				////limpiar
				forma.setCoberturaAsignadas(mundo.consultarCantidadesSubCuentaFacturadas(con,forma.getCodigoIngreso()));
				
				//----------------------
				int numRegistros=0;
				HashMap distribucionInsertar=new HashMap();
				HashMap esquemasCirugias=new HashMap();
				//recorro cada solicitud.
				for(int a=0;a<Utilidades.convertirAEntero(forma.getDetSolicitudesPaciente("numRegistros")+"");a++)
				{
					//si tiene responsables facturados no se toma en cuenta para la liquidacion.
					if((Utilidades.convertirAEntero(forma.getDetSolicitudesPaciente("numresfactservicio_"+a)+"")+Utilidades.convertirAEntero(forma.getDetSolicitudesPaciente("numresfactarticulo_"+a)+""))==0)
					{
						//logger.info("\n\n ........> pase por aqui --> 4.1 .................... "+forma.getDetSolicitudesPaciente("solicitud_"+a));
						//recorro cada responsable.
						int indiceRespValido=ConstantesBD.codigoNuncaValido;
						
						int numSolicitud=Utilidades.convertirAEntero(forma.getDetSolicitudesPaciente("solicitud_"+a)+"");
						String fechaCalculosCargos=Cargos.obtenerFechaCalculoCargo(con, numSolicitud);
						
						
						for(int j=0;j<forma.getResponsables().size();j++)
						{
							///analizar a que responsable le aplica el filtro.
							//logger.info("\n\n ........> pase por aqui --> 4.2 .................... "+forma.getResponsables().get(j));
							if(validoServArtParaResponsable(con,forma,a,j,true,false,false,numSolicitud,usuario,forma.getResponsables().get(j)))
							{
								indiceRespValido=j;
								j=forma.getResponsables().size();
							}
						}
		
						//no es contenido en ninguno. se asigna al ultimo responsable
						if(indiceRespValido==ConstantesBD.codigoNuncaValido)
							indiceRespValido=(forma.getResponsables().size()-1);
		
						
						
						int esquemaTarifario=ConstantesBD.codigoNuncaValido;
		
						double valorUnitarioCargado=0;
						
						if(Integer.parseInt(forma.getDetSolicitudesPaciente("tiposolicitud_"+a)+"")==ConstantesBD.codigoTipoSolicitudCirugia&&!UtilidadTexto.isEmpty(forma.getDetSolicitudesPaciente("servicio_"+a)+""))
						{
							if(esquemasCirugias.containsKey("esquema_"+forma.getDetSolicitudesPaciente("solicitud_"+a)+"_"+forma.getDetSolicitudesPaciente("servicio_"+a)+"_"+forma.getDetSolicitudesPaciente("serviciocx_"+a)+"_"+forma.getDetSolicitudesPaciente("detcxhonorarios_"+a)+"_"+forma.getDetSolicitudesPaciente("detascxsalmat_"+a)))
							{
								esquemaTarifario=Utilidades.convertirAEntero((esquemasCirugias.get("esquema_"+forma.getDetSolicitudesPaciente("solicitud_"+a)+"_"+forma.getDetSolicitudesPaciente("servicio_"+a)+"_"+forma.getDetSolicitudesPaciente("serviciocx_"+a)+"_"+forma.getDetSolicitudesPaciente("detcxhonorarios_"+a)+"_"+forma.getDetSolicitudesPaciente("detascxsalmat_"+a)))+"");
							}
							else
							{
								esquemaTarifario=UtilidadesFacturacion.obtenerEsquemaTarifarioBase(con,forma.getDetSolicitudesPaciente("solicitud_"+a)+"",forma.getDetSolicitudesPaciente("servicio_"+a)+"",forma.getDetSolicitudesPaciente("serviciocx_"+a)+"",forma.getDetSolicitudesPaciente("detcxhonorarios_"+a)+"",forma.getDetSolicitudesPaciente("detascxsalmat_"+a)+"",true,ConstantesBD.acronimoNo);
								esquemasCirugias.put("esquema_"+forma.getDetSolicitudesPaciente("solicitud_"+a)+"_"+forma.getDetSolicitudesPaciente("servicio_"+a)+"_"+forma.getDetSolicitudesPaciente("serviciocx_"+a)+"_"+forma.getDetSolicitudesPaciente("detcxhonorarios_"+a)+"_"+forma.getDetSolicitudesPaciente("detascxsalmat_"+a),esquemaTarifario+"");
								
							}
							if(esquemasCirugias.containsKey("valor_"+forma.getDetSolicitudesPaciente("solicitud_"+a)+"_"+forma.getDetSolicitudesPaciente("servicio_"+a)+"_"+forma.getDetSolicitudesPaciente("serviciocx_"+a)+"_"+forma.getDetSolicitudesPaciente("detcxhonorarios_"+a)+"_"+forma.getDetSolicitudesPaciente("detascxsalmat_"+a)))
							{
								valorUnitarioCargado=UtilidadesFacturacion.obtenerValorUnitarioCargadoConvenioBase(con,forma.getDetSolicitudesPaciente("solicitud_"+a)+"",forma.getDetSolicitudesPaciente("servicio_"+a)+"",forma.getDetSolicitudesPaciente("serviciocx_"+a)+"",forma.getDetSolicitudesPaciente("detcxhonorarios_"+a)+"",forma.getDetSolicitudesPaciente("detascxsalmat_"+a)+"",true,ConstantesBD.acronimoNo);
							}
							else
							{
								valorUnitarioCargado=UtilidadesFacturacion.obtenerValorUnitarioCargadoConvenioBase(con,forma.getDetSolicitudesPaciente("solicitud_"+a)+"",forma.getDetSolicitudesPaciente("servicio_"+a)+"",forma.getDetSolicitudesPaciente("serviciocx_"+a)+"",forma.getDetSolicitudesPaciente("detcxhonorarios_"+a)+"",forma.getDetSolicitudesPaciente("detascxsalmat_"+a)+"",true,ConstantesBD.acronimoNo);
								esquemasCirugias.put("valor_"+forma.getDetSolicitudesPaciente("solicitud_"+a)+"_"+forma.getDetSolicitudesPaciente("servicio_"+a)+"_"+forma.getDetSolicitudesPaciente("serviciocx_"+a)+"_"+forma.getDetSolicitudesPaciente("detcxhonorarios_"+a)+"_"+forma.getDetSolicitudesPaciente("detascxsalmat_"+a),valorUnitarioCargado+"");
							}
						}
						
						if(!UtilidadTexto.isEmpty(forma.getDetSolicitudesPaciente("servicio_"+a)+""))
							exitoso=mundo.eliminarSolicitudSubCuentaDetCargoXSolServArt(con,numSolicitud,forma.getDetSolicitudesPaciente("servicio_"+a)+"",forma.getDetSolicitudesPaciente("serviciocx_"+a)+"",forma.getDetSolicitudesPaciente("detcxhonorarios_"+a)+"",forma.getDetSolicitudesPaciente("detascxsalmat_"+a)+"",true,Utilidades.convertirAEntero(forma.getDetSolicitudesPaciente("estadocargo_"+a)+""));
						else if(!UtilidadTexto.isEmpty(forma.getDetSolicitudesPaciente("articulo_"+a)+""))
							exitoso=mundo.eliminarSolicitudSubCuentaDetCargoXSolServArt(con,numSolicitud,forma.getDetSolicitudesPaciente("articulo_"+a)+"","","","",false,Utilidades.convertirAEntero(forma.getDetSolicitudesPaciente("estadocargo_"+a)+""));
						
						/*
						if(exitoso)
						{
							if(!UtilidadTexto.isEmpty(forma.getDetSolicitudesPaciente("servicio_"+a)+""))
								exitoso=mundo.eliminarSolicitudSubCuentaXSolServArt(con,numSolicitud,forma.getDetSolicitudesPaciente("servicio_"+a)+"",forma.getDetSolicitudesPaciente("serviciocx_"+a)+"",true);
							else if(!UtilidadTexto.isEmpty(forma.getDetSolicitudesPaciente("articulo_"+a)+""))
								exitoso=mundo.eliminarSolicitudSubCuentaXSolServArt(con,numSolicitud,forma.getDetSolicitudesPaciente("articulo_"+a)+"","",false);
						}
						*/
						if(exitoso)
						{
							int cantidadCargada=Utilidades.convertirAEntero(forma.getDetSolicitudesPaciente("cantidad_"+a)+"");
							
							for(int j=indiceRespValido;j<forma.getResponsables().size();j++)
							{
								boolean cargoAnulInactivo=(forma.getDetSolicitudesPaciente("estadocargo_"+a)+"").trim().equals(ConstantesBD.codigoEstadoFAnulada+"")||(forma.getDetSolicitudesPaciente("estadocargo_"+a)+"").trim().equals(ConstantesBD.codigoEstadoFInactiva+"");
								if(!cargoAnulInactivo||(cargoAnulInactivo&&j==(forma.getResponsables().size()-1)))//incluir las anuladas e inactivas, solo si es el ultimo responsalbe.
								{
									if(cantidadCargada>0)
									{
										
										
										boolean puedoDistribuir=true;
		
										//validacion solicitudes pyp
										boolean esSolPyp = Utilidades.esSolicitudPYP(con, numSolicitud); 
										if(esSolPyp)
										{
											if((UtilidadValidacion.esConvenioPYP(con, forma.getResponsables().get(j).getConvenio().getCodigo()+"")&&Utilidades.esActividadSolicitudPYPCubiertaConvenio(con,numSolicitud+"",paciente.getCodigoPersona()+"",forma.getResponsables().get(j).getConvenio().getCodigo()+""))||j==(forma.getResponsables().size()-1))
												puedoDistribuir=true;
											else
												puedoDistribuir=false;
										}
										//no es necesario volver a validar la cobertura, solo los filtros.
		
										if(puedoDistribuir&&(validoServArtParaResponsable(con,forma,a,j,false,false,false,numSolicitud,usuario,forma.getResponsables().get(j))||j==(forma.getResponsables().size()-1)))
										{
					
											DtoSubCuentas responsable=forma.getResponsables().get(j);
											
											if(Integer.parseInt(forma.getDetSolicitudesPaciente("tiposolicitud_"+a)+"")!=ConstantesBD.codigoTipoSolicitudCirugia)
											{
												if(!UtilidadTexto.isEmpty(forma.getDetSolicitudesPaciente("servicio_"+a)+""))
												{
													esquemaTarifario=forma.getResponsables().get(indiceRespValido).getEsquemaTarifarioServiciosPpalOoriginal(con,forma.getResponsables().get(indiceRespValido).getSubCuenta(),forma.getResponsables().get(indiceRespValido).getContrato(),Utilidades.convertirAEntero(forma.getDetSolicitudesPaciente("servicio_"+a)+""),fechaCalculosCargos, Cargos.obtenerCentroAtencionCargoSolicitud(con,numSolicitud));
													//logger.info("1 --- Esquema tarifario: "+esquemaTarifario);
												}
												else if(!UtilidadTexto.isEmpty(forma.getDetSolicitudesPaciente("articulo_"+a)+""))
												{
													esquemaTarifario=forma.getResponsables().get(indiceRespValido).getEsquemaTarifarioArticuloPpalOoriginal(con,forma.getResponsables().get(indiceRespValido).getSubCuenta(),forma.getResponsables().get(indiceRespValido).getContrato(),Utilidades.convertirAEntero(forma.getDetSolicitudesPaciente("articulo_"+a)+""),fechaCalculosCargos, Cargos.obtenerCentroAtencionCargoSolicitud(con,numSolicitud));					
												}
											}
											//logger.info("Esquema tarifario: "+esquemaTarifario);
											
											
											InfoCobertura infoCobertura=new InfoCobertura();
											InfoTarifa descuento=new InfoTarifa();
											if(!UtilidadTexto.isEmpty(forma.getDetSolicitudesPaciente("servicio_"+a)+""))
											{
		//										en caso de que la cobertura y el descuento sea por cirugia debe hacerse esta validacion  para el serviciocx_
												if(Integer.parseInt(forma.getDetSolicitudesPaciente("tiposolicitud_"+a)+"")==ConstantesBD.codigoTipoSolicitudCirugia)
												{
													infoCobertura=Cobertura.validacionCoberturaServicioDadoResponsable(con, responsable, Utilidades.convertirAEntero(forma.getDetSolicitudesPaciente("viaingreso_"+a)+""), forma.getDetSolicitudesPaciente("tipopaciente_"+a)+"", Utilidades.convertirAEntero(forma.getDetSolicitudesPaciente("serviciocx_"+a)+""), usuario.getCodigoInstitucionInt());
													descuento=Cargos.obtenerDescuentoComercialXConvenioServicio(con, Utilidades.convertirAEntero(forma.getDetSolicitudesPaciente("viaingreso_"+a)+""), forma.getDetSolicitudesPaciente("tipopaciente_"+a)+"", responsable.getContrato(), Utilidades.convertirAEntero(forma.getDetSolicitudesPaciente("serviciocx_"+a)+""), usuario.getCodigoInstitucionInt(),fechaCalculosCargos);
												}
												else
												{
													infoCobertura=Cobertura.validacionCoberturaServicioDadoResponsable(con, responsable, Utilidades.convertirAEntero(forma.getDetSolicitudesPaciente("viaingreso_"+a)+""), forma.getDetSolicitudesPaciente("tipopaciente_"+a)+"", Utilidades.convertirAEntero(forma.getDetSolicitudesPaciente("servicio_"+a)+""), usuario.getCodigoInstitucionInt());
													descuento=Cargos.obtenerDescuentoComercialXConvenioServicio(con, Utilidades.convertirAEntero(forma.getDetSolicitudesPaciente("viaingreso_"+a)+""), forma.getDetSolicitudesPaciente("tipopaciente_"+a)+"", responsable.getContrato(), Utilidades.convertirAEntero(forma.getDetSolicitudesPaciente("servicio_"+a)+""), usuario.getCodigoInstitucionInt(),fechaCalculosCargos);
												}
					
											}
											else if(!UtilidadTexto.isEmpty(forma.getDetSolicitudesPaciente("articulo_"+a)+""))
											{
												infoCobertura=Cobertura.validacionCoberturaArticuloDadoResponsable(con, responsable, Utilidades.convertirAEntero(forma.getDetSolicitudesPaciente("viaingreso_"+a)+""), forma.getDetSolicitudesPaciente("tipopaciente_"+a)+"",Utilidades.convertirAEntero(forma.getDetSolicitudesPaciente("articulo_"+a)+""), usuario.getCodigoInstitucionInt());
											}
											
											distribucionInsertar.put("solicitud_"+numRegistros,numSolicitud+"");
											distribucionInsertar.put("cuenta_"+numRegistros,Utilidades.getCuentaSolicitud(con, numSolicitud));
											distribucionInsertar.put("subcuenta_"+numRegistros,responsable.getSubCuenta());
											distribucionInsertar.put("servicio_"+numRegistros, forma.getDetSolicitudesPaciente("servicio_"+a)+"");
											distribucionInsertar.put("articulo_"+numRegistros, forma.getDetSolicitudesPaciente("articulo_"+a)+"");
											if(j==(forma.getResponsables().size()-1))
											{
												String cubierto=ConstantesBD.acronimoNo;
												distribucionInsertar.put("cantidad_"+numRegistros, cantidadCargada+"");
												if(infoCobertura.getCantidad()>0 &&infoCobertura.getCantidad()<cantidadCargada)
												{
													cubierto=ConstantesBD.acronimoNo;
												}
												else
												{
													cubierto=infoCobertura.getIncluidoStr();
												}
												distribucionInsertar.put("cubierto_"+numRegistros,cubierto+"");
												cantidadCargada=0;
												
												//logger.info("\n\n ........> pase por aqui --> 4.3 .................... "+cubierto);
											}
											else
											{
												////////////evaluar aca.
												if(infoCobertura.getCantidad()>=0)
												{
													String servart="";
													if(!UtilidadTexto.isEmpty(forma.getDetSolicitudesPaciente("servicio_"+a)+""))
													{
														//en caso de que la cobertura y el descuento sea por cirugia debe hacerse esta validacion  para el serviciocx_
														servart=forma.getDetSolicitudesPaciente("servicio_"+a)+"";
													}
													else
													{
														servart=forma.getDetSolicitudesPaciente("articulo_"+a)+"";
													}
													int cantidadCubierta=infoCobertura.getCantidad();
													if(forma.getCoberturaAsignadas().containsKey(responsable.getSubCuenta()+"_"+servart))
													{
														cantidadCubierta=infoCobertura.getCantidad()-Utilidades.convertirAEntero(forma.getCoberturaAsignadas(responsable.getSubCuenta()+"_"+servart)+"");
													}
													int cantidadAsignada=0;
													if(cantidadCargada>cantidadCubierta)
													{
														cantidadAsignada=cantidadCubierta;
														distribucionInsertar.put("cantidad_"+numRegistros, cantidadCubierta+"");
														cantidadCargada=cantidadCargada-cantidadCubierta;
													}
													else
													{
														cantidadAsignada=cantidadCargada;
														distribucionInsertar.put("cantidad_"+numRegistros, cantidadCargada+"");
														cantidadCargada=0;
													}
													
													//llevando acumulado.
													if(forma.getCoberturaAsignadas().containsKey(responsable.getSubCuenta()+"_"+servart))
													{
	//													Utilidades.imprimirMapa(forma.getCoberturaAsignadas());
														forma.setCoberturaAsignadas(responsable.getSubCuenta()+"_"+servart,(Utilidades.convertirAEntero(forma.getCoberturaAsignadas(responsable.getSubCuenta()+"_"+servart)+"")+cantidadAsignada));
													}
													else
													{
														forma.setCoberturaAsignadas(responsable.getSubCuenta()+"_"+servart,(cantidadAsignada));
													}
													
	//												Utilidades.imprimirMapa(forma.getCoberturaAsignadas());
												}
												else
												{
													distribucionInsertar.put("cantidad_"+numRegistros, cantidadCargada+"");
													cantidadCargada=0;
												}
												distribucionInsertar.put("cubierto_"+numRegistros, infoCobertura.getIncluidoStr()+"");
												
												//logger.info("\n\n ........> pase por aqui --> 4.3 .................... "+cantidadCargada);
											}
											
											distribucionInsertar.put("tiposolicitud_"+numRegistros, forma.getDetSolicitudesPaciente("tiposolicitud_"+a)+"");
											distribucionInsertar.put("serviciocx_"+numRegistros, forma.getDetSolicitudesPaciente("serviciocx_"+a)+"");
											distribucionInsertar.put("detcxhonorarios_"+numRegistros, forma.getDetSolicitudesPaciente("detcxhonorarios_"+a)+"");
											distribucionInsertar.put("detascxsalmat_"+numRegistros, forma.getDetSolicitudesPaciente("detascxsalmat_"+a)+"");
											
											distribucionInsertar.put("tipoasocio_"+numRegistros, UtilidadTexto.isEmpty(forma.getDetSolicitudesPaciente("tipoasocio_"+a)+"")?ConstantesBD.codigoNuncaValido:Utilidades.convertirAEntero(forma.getDetSolicitudesPaciente("tipoasocio_"+a)+""));
											distribucionInsertar.put("tipodistribucion_"+numRegistros, forma.getTipoDistribucion());
											distribucionInsertar.put("paquetizada_"+numRegistros, ConstantesBD.acronimoNo);
											distribucionInsertar.put("solsubcuentapadre_"+numRegistros, "");
		
											if(Integer.parseInt(forma.getDetSolicitudesPaciente("tiposolicitud_"+a)+"")==ConstantesBD.codigoTipoSolicitudCirugia)
											{
												distribucionInsertar.put("valortotalcargado_"+numRegistros, (Utilidades.convertirAEntero(distribucionInsertar.get("cantidad_"+numRegistros)+"")*valorUnitarioCargado)+"");
											}
											
											nroAutorizacion = forma.getDetSolicitudesPaciente("nro_autorizacion_"+a)+"";
											if(UtilidadTexto.isEmpty(nroAutorizacion))
											{
												nroAutorizacion = responsable.getNroAutorizacion();
											} 
											distribucionInsertar.put("autorizacion_"+numRegistros, nroAutorizacion);
											distribucionInsertar.put("esquematarifario_"+numRegistros, esquemaTarifario);
											
											if(descuento.getPorcentajes().size()>0)
												distribucionInsertar.put("porcentajedcto_"+numRegistros, descuento.getPorcentajes().get(0));
											else
												distribucionInsertar.put("porcentajedcto_"+numRegistros, "");
											distribucionInsertar.put("valorunitariodcto_"+numRegistros, descuento.getValor());
											
											if (forma.getDetSolicitudesPaciente("estadocargo_"+a).toString().equals("5"))
											{distribucionInsertar.put("estado_"+numRegistros, forma.getDetSolicitudesPaciente("estadocargo_"+a)+"");}
											else{
											if(cargoAnulInactivo)
												{distribucionInsertar.put("estado_"+numRegistros, forma.getDetSolicitudesPaciente("estadocargo_"+a)+"");}
											else
												{distribucionInsertar.put("estado_"+numRegistros, ConstantesBD.codigoEstadoFCargada);}
											}	distribucionInsertar.put("requiereautorizacion_"+numRegistros,infoCobertura.getRequiereAutorizacionStr());
											distribucionInsertar.put("convenio_"+numRegistros, responsable.getConvenio().getCodigo());
											distribucionInsertar.put("contrato_"+numRegistros, responsable.getContrato());
											distribucionInsertar.put("codccsolicita_"+numRegistros, forma.getDetSolicitudesPaciente("codccsolicita_"+a)+"");
											distribucionInsertar.put("codccejecuta_"+numRegistros, forma.getDetSolicitudesPaciente("codccejecuta_"+a)+"");
											distribucionInsertar.put("estadohc_"+numRegistros, forma.getDetSolicitudesPaciente("codestadohc_"+a)+"");
											distribucionInsertar.put("viaingreso_"+numRegistros, forma.getDetSolicitudesPaciente("viaingreso_"+a)+"");
											numRegistros++;
										}
									}
								}
							}
						}
						else
						{
							exitoso=false;
							a=Utilidades.convertirAEntero(forma.getDetSolicitudesPaciente("numRegistros")+"");
						}
					}
				}
				distribucionInsertar.put("numRegistros",numRegistros);
				exitoso=guardarDistribucionMapaCasoCantidad(con,forma,distribucionInsertar,mundo,usuario);
				
			}
			///////////////FIN DISTRIBUCION TIPO CANTIDAD.
			
			
			return exitoso;
		}
		catch (IPSException ipsme) {
			throw ipsme;
		}
		catch (Exception e) {
			Log4JManager.error(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
	}
	
	
	/**
	 * 
	 * @param con
	 * @param forma 
	 * @param distribucionInsertar
	 * @param mundo
	 * @param usuario 
	 * @return
	 */
	private boolean guardarDistribucionMapaCasoCantidad(Connection con, DistribucionCuentaForm forma, HashMap distribucionInsertar, DistribucionCuenta mundo, UsuarioBasico usuario) throws IPSException  
	{
		boolean exitoso=true;
		double solicitudSubCuenta = ConstantesBD.codigoNuncaValido;
		
		try {

			for(int a=0;a<Utilidades.convertirAEntero(distribucionInsertar.get("numRegistros")+"");a++)
			{
				//estadohc_
				int codigoEstadoHC=Utilidades.convertirAEntero(distribucionInsertar.get("estadohc_"+a)+"");
				boolean dejarPendiente=false;
				if(codigoEstadoHC==ConstantesBD.codigoEstadoHCSolicitada)
					dejarPendiente=true;
	//			distribucionInsertar.put("valorbasedistribucion_"+numRegistros, valorTarifaBase);
	//			distribucionInsertar.put("valorunitariocargado_"+numRegistros, valorTarifaBase);valortotalcargado_
				
				
				
				if(exitoso)
				{
					boolean valido=false;
					if(Integer.parseInt(distribucionInsertar.get("tiposolicitud_"+a)+"")!=ConstantesBD.codigoTipoSolicitudCirugia||(Integer.parseInt(forma.getDetSolicitudesPaciente("tiposolicitud_"+a)+"")==ConstantesBD.codigoTipoSolicitudCirugia&&UtilidadTexto.isEmpty(forma.getDetSolicitudesPaciente("servicio_"+a)+"")))
					{
						solicitudSubCuenta = mundo.insertarSolicitudSubCuenta(	con,
																	Utilidades.convertirAEntero(distribucionInsertar.get("solicitud_"+a)+""),
																	Utilidades.convertirAEntero(distribucionInsertar.get("cuenta_"+a)+""),
																	distribucionInsertar.get("subcuenta_"+a)+"",
																	distribucionInsertar.get("servicio_"+a)+"",
																	distribucionInsertar.get("articulo_"+a)+"",
																	Utilidades.convertirAEntero(distribucionInsertar.get("cantidad_"+a)+""),
																	distribucionInsertar.get("cubierto_"+a)+"",
																	Utilidades.convertirAEntero(distribucionInsertar.get("tiposolicitud_"+a)+""),
																	distribucionInsertar.get("serviciocx_"+a)+"",
																	distribucionInsertar.get("detcxhonorarios_"+a)+"",
																	distribucionInsertar.get("detascxsalmat_"+a)+"",
																	Utilidades.convertirAEntero(distribucionInsertar.get("tipoasocio_"+a)+""),
																	distribucionInsertar.get("tipodistribucion_"+a)+"",
																	usuario.getLoginUsuario(),
																	"",//porcentaje
																	"",//monto
																	distribucionInsertar.get("paquetizada_"+a)+"",
																	distribucionInsertar.get("solsubcuentapadre_"+a)+""
																	);
						logger.info("solicitudSubCuenta--->"+solicitudSubCuenta);
						valido=solicitudSubCuenta>0;
					}
					else
					{
						valido=true;
					}
					logger.info("solicitudSubCuenta--->"+solicitudSubCuenta);
					
					
					if(valido)
					{
						Cargos cargos=new Cargos();
	
						int numeroSolicitud=Utilidades.convertirAEntero(distribucionInsertar.get("solicitud_"+a)+"");
						String fechaCalculosCargos=Cargos.obtenerFechaCalculoCargo(con, numeroSolicitud);
	
						
						if(!UtilidadTexto.isEmpty(distribucionInsertar.get("servicio_"+a)+""))
						{
							if(Integer.parseInt(distribucionInsertar.get("tiposolicitud_"+a)+"")==ConstantesBD.codigoTipoSolicitudCirugia)
							{
								cargos.generarCargoSolicitudesCxYSolSubCuenta(con,
										Utilidades.convertirAEntero(distribucionInsertar.get("solicitud_"+a)+""), 
										distribucionInsertar.get("cuenta_"+a)+"",
										distribucionInsertar.get("subcuenta_"+a)+"",
										distribucionInsertar.get("servicio_"+a)+"", 
										distribucionInsertar.get("cubierto_"+a)+"", 
										distribucionInsertar.get("serviciocx_"+a)+"",
										Utilidades.convertirAEntero(distribucionInsertar.get("tipoasocio_"+a)+""), 
										Utilidades.convertirAEntero(distribucionInsertar.get("esquematarifario_"+a)+""), 
										usuario, 
										Utilidades.convertirADouble(distribucionInsertar.get("valortotalcargado_"+a)+""), 
										distribucionInsertar.get("requiereautorizacion_"+a)+"",  
										usuario.getLoginUsuario(),
										Utilidades.convertirAEntero(distribucionInsertar.get("detcxhonorarios_"+a)+""),
										Utilidades.convertirAEntero(distribucionInsertar.get("detascxsalmat_"+a)+""),
										usuario.getCodigoInstitucionInt(),
										true,
										UtilidadesSalas.esCxPorConsumoMateriales(con,numeroSolicitud), 
										UtilidadesSalas.obtenerCodigoTipoServicioAsocio(con, Utilidades.convertirAEntero(distribucionInsertar.get("tipoasocio_"+a)+"")),
										0,0
										);
							}
							else
							{
								if(UtilidadValidacion.esServicioViaIngresoCargoProceso(con, Solicitudes.obtenerViaIngresoSolicitud(con,Utilidades.convertirAEntero(distribucionInsertar.get("solicitud_"+a)+""))+"", distribucionInsertar.get("servicio_"+a)+"", usuario.getCodigoInstitucion())
									    	|| UtilidadValidacion.esServicioViaIngresoCargoSolicitud(con, Solicitudes.obtenerViaIngresoSolicitud(con,Utilidades.convertirAEntero(distribucionInsertar.get("solicitud_"+a)+""))+"",distribucionInsertar.get("servicio_"+a)+"", usuario.getCodigoInstitucion()))
									    {
									    	dejarPendiente=false;
									    }
							 
								 exitoso=!cargos.generarCargoServicio(con, 
															dejarPendiente, 
															false, 
															Utilidades.convertirAEntero(distribucionInsertar.get("solicitud_"+a)+""), 
															Utilidades.convertirAEntero(distribucionInsertar.get("viaingreso_"+a)+""), 
															Utilidades.convertirAEntero(distribucionInsertar.get("contrato_"+a)+""), 
															forma.getTipoComplejidad(), 
															usuario.getCodigoInstitucionInt(), 
															"", 
															usuario.getLoginUsuario(), 
															Utilidades.convertirADouble(distribucionInsertar.get("subcuenta_"+a)+""), 
															solicitudSubCuenta, 
															distribucionInsertar.get("cubierto_"+a)+"",
															ConstantesBD.codigoNuncaValidoDouble, 
															ConstantesBD.acronimoNo, 
															Utilidades.convertirAEntero(distribucionInsertar.get("cantidad_"+a)+""),
															distribucionInsertar.get("requiereautorizacion_"+a)+"", 
															distribucionInsertar.get("tipodistribucion_"+a)+"", 
															ConstantesBD.codigoNuncaValido,  
															Utilidades.convertirAEntero(distribucionInsertar.get("servicio_"+a)+""), 
															ConstantesBD.codigoNuncaValidoDouble,
															//Utilidades.convertirADouble(distribucionInsertar.get("valorbasedistribucion_"+a)+""), 
															Utilidades.convertirAEntero(distribucionInsertar.get("convenio_"+a)+""), 
															Utilidades.convertirAEntero(distribucionInsertar.get("esquematarifario_"+a)+""), 
															Utilidades.convertirAEntero(distribucionInsertar.get("tiposolicitud_"+a)+""), 
															Utilidades.convertirAEntero(distribucionInsertar.get("codccsolicita_"+a)+""), 
															"",/*distribucionInsertar.get("autorizacion_"+a)+"",*/ 
															Utilidades.convertirADouble(distribucionInsertar.get("porcentajedcto_"+a)+""), 
															Utilidades.convertirADouble(distribucionInsertar.get("valorunitariodcto_"+a)+""), 
															true, 
															true,
															""/*esPortatil*/,false,fechaCalculosCargos,
															0 /*porcentajeDctoPromocionServicio*/, 
															BigDecimal.ZERO /*valorDescuentoPromocionServicio*/, 
															0 /*porcentajeHonorarioPromocionServicio*/, 
															BigDecimal.ZERO /*valorHonorarioPromocionServicio*/,
															0/*programa*/,
															0/*porcentajeDctoBono*/,
															BigDecimal.ZERO/*valorDescuentoBono*/, 
															0/*porcentajeDctoOdontologico*/, 
															BigDecimal.ZERO/*valorDescuentoOdontologico*/,
															0 /*detallePaqueteOdonConvenio*/).getTieneErroresCodigo();
								 
							
							}
						}
						else if(!UtilidadTexto.isEmpty(distribucionInsertar.get("articulo_"+a)+""))
						{
							logger.info("\n\n\n\n\nGENERANDO EL CARGO PARA EL ARTICULO .\n\n\n\n\n");
							logger.info("solicitudSubCuenta--->"+solicitudSubCuenta);
							cargos.generarCargoArticulo(con, 
															dejarPendiente,
															Utilidades.convertirAEntero(distribucionInsertar.get("solicitud_"+a)+""), 
															Utilidades.convertirAEntero(distribucionInsertar.get("contrato_"+a)+""), 
															Utilidades.convertirAEntero(distribucionInsertar.get("articulo_"+a)+""), 
															Utilidades.convertirAEntero(distribucionInsertar.get("viaingreso_"+a)+""), 
															forma.getTipoComplejidad(), 
															usuario.getCodigoInstitucionInt(), 
															Utilidades.convertirADouble(distribucionInsertar.get("subcuenta_"+a)+""), 
															solicitudSubCuenta, 
															distribucionInsertar.get("cubierto_"+a)+"", 
															ConstantesBD.acronimoNo, 
															ConstantesBD.codigoNuncaValidoDouble, 
															distribucionInsertar.get("requiereautorizacion_"+a)+"", 
															distribucionInsertar.get("tipodistribucion_"+a)+"", 
															Utilidades.convertirAEntero(distribucionInsertar.get("esquematarifario_"+a)+""), 
															Utilidades.convertirAEntero(distribucionInsertar.get("convenio_"+a)+""), 
															ConstantesBD.codigoNuncaValidoDouble,
															//Utilidades.convertirADouble(distribucionInsertar.get("valorbasedistribucion_"+a)+""), 
															Utilidades.convertirAEntero(distribucionInsertar.get("tiposolicitud_"+a)+""), 
															Utilidades.convertirAEntero(distribucionInsertar.get("codccsolicita_"+a)+""), 
															Utilidades.convertirAEntero(distribucionInsertar.get("cantidad_"+a)+""), 
															/*distribucionInsertar.get("autorizacion_"+a)+"",*/ 
															Utilidades.convertirADouble(distribucionInsertar.get("porcentajedcto_"+a)+""), 
															Utilidades.convertirADouble(distribucionInsertar.get("valorunitariodcto_"+a)+""), 
															usuario.getLoginUsuario(), 
															"", 
															true, 
															true,
	    		    										false /*cancelarInsercionSiExisteError*/,fechaCalculosCargos,false /*tarifaNoModificada*/).getTieneErroresCodigo();
							
						}
						
						
						if((distribucionInsertar.get("estado_"+a)+"").trim().equals(ConstantesBD.codigoEstadoFAnulada+"")||(distribucionInsertar.get("estado_"+a)+"").trim().equals(ConstantesBD.codigoEstadoFInactiva+"")||(distribucionInsertar.get("estado_"+a)+"").trim().equals(ConstantesBD.codigoEstadoFExento+""))
							Cargos.modificarEstadoCargo(con, cargos.getDtoDetalleCargo().getCodigoDetalleCargo(), Utilidades.convertirAEntero(distribucionInsertar.get("estado_"+a)+""));
					}
					else
					{
						a=Utilidades.convertirAEntero(distribucionInsertar.get("numRegistros")+"");
						exitoso=false;
					}
				}
				else
				{
					a=Utilidades.convertirAEntero(distribucionInsertar.get("numRegistros")+"");
				}
			}
			return exitoso;
		}
		catch (IPSException ipsme) {
			throw ipsme;
		}
		catch (Exception e) {
			Log4JManager.error(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}	
	}
	
	/**
	 * 
	 * @param con
	 * @param distribucionInsertar
	 * @param mundo
	 * @param usuario 
	 * @return
	 */
	private boolean guardarDistribucionMapa(Connection con, HashMap distribucionInsertar, DistribucionCuenta mundo, UsuarioBasico usuario) throws IPSException
	{
		boolean exitoso=true;
		double solicitudSubCuenta = ConstantesBD.codigoNuncaValido;
		
		try {
			for(int a=0;a<Utilidades.convertirAEntero(distribucionInsertar.get("numRegistros")+"");a++)
			{
				if(exitoso)
				{
					solicitudSubCuenta = mundo.insertarSolicitudSubCuenta(	con,
																	Utilidades.convertirAEntero(distribucionInsertar.get("solicitud_"+a)+""),
																	Utilidades.convertirAEntero(distribucionInsertar.get("cuenta_"+a)+""),
																	distribucionInsertar.get("subcuenta_"+a)+"",
																	distribucionInsertar.get("servicio_"+a)+"",
																	distribucionInsertar.get("articulo_"+a)+"",
																	Utilidades.convertirAEntero(distribucionInsertar.get("cantidad_"+a)+""),
																	distribucionInsertar.get("cubierto_"+a)+"",
																	Utilidades.convertirAEntero(distribucionInsertar.get("tiposolicitud_"+a)+""),
																	distribucionInsertar.get("serviciocx_"+a)+"",
																	distribucionInsertar.get("detcxhonorarios_"+a)+"",
																	distribucionInsertar.get("detascxsalmat_"+a)+"",
																	Utilidades.convertirAEntero(distribucionInsertar.get("tipoasocio_"+a)+""),
																	distribucionInsertar.get("tipodistribucion_"+a)+"",
																	usuario.getLoginUsuario(),
																	distribucionInsertar.get("porcentaje_"+a)+"",
																	distribucionInsertar.get("valortotalcargado_"+a)+"",
																	distribucionInsertar.get("paquetizada_"+a)+"",
																	distribucionInsertar.get("solsubcuentapadre_"+a)+""
																	);
					
					if(solicitudSubCuenta>0)
					{
						DtoDetalleCargo detCargo=new DtoDetalleCargo();
						detCargo.setTipoDistribucion(distribucionInsertar.get("tipodistribucion_"+a)+"");
						//detCargo.setNumeroAutorizacion(distribucionInsertar.get("autorizacion_"+a)+"");
						detCargo.setCantidadCargada(Utilidades.convertirAEntero(distribucionInsertar.get("cantidad_"+a)+""));
						detCargo.setPorcentajeCargado(Utilidades.convertirADouble(distribucionInsertar.get("porcentaje_"+a)+""));
						detCargo.setValorUnitarioTarifa(Utilidades.convertirADouble(distribucionInsertar.get("valorbasedistribucion_"+a)+""));
						detCargo.setValorTotalCargado(Utilidades.convertirADouble(distribucionInsertar.get("valortotalcargado_"+a)+""));
						detCargo.setValorUnitarioCargado(detCargo.getValorTotalCargado()/detCargo.getCantidadCargada());
						detCargo.setCodigoEsquemaTarifario(Utilidades.convertirAEntero(distribucionInsertar.get("esquematarifario_"+a)+""));
						detCargo.setPorcentajeDescuento(Utilidades.convertirADouble(distribucionInsertar.get("porcentajedcto_"+a)+""));
						detCargo.setValorUnitarioDescuento(Utilidades.convertirADouble(distribucionInsertar.get("valorunitariodcto_"+a)+""));
						detCargo.setEstado(Utilidades.convertirAEntero(distribucionInsertar.get("estado_"+a)+""));
						detCargo.setCubierto(distribucionInsertar.get("cubierto_"+a)+"");
						detCargo.setRequiereAutorizacion(distribucionInsertar.get("requiereautorizacion_"+a)+"");
			
						///datos requeridos.
						detCargo.setCodigoSubcuenta(Utilidades.convertirADouble(distribucionInsertar.get("subcuenta_"+a)+""));
						detCargo.setCodigoConvenio(Utilidades.convertirAEntero(distribucionInsertar.get("convenio_"+a)+""));
						detCargo.setCodigoEsquemaTarifario(Utilidades.convertirAEntero(distribucionInsertar.get("esquematarifario_"+a)+""));
						detCargo.setNumeroSolicitud(Utilidades.convertirAEntero(distribucionInsertar.get("solicitud_"+a)+""));
						detCargo.setCodigoServicio(Utilidades.convertirAEntero(distribucionInsertar.get("servicio_"+a)+""));
						detCargo.setCodigoArticulo(Utilidades.convertirAEntero(distribucionInsertar.get("articulo_"+a)+""));
						detCargo.setCodigoServicioCx(Utilidades.convertirAEntero(distribucionInsertar.get("serviciocx_"+a)+""));
						detCargo.setDetCxHonorarios(Utilidades.convertirAEntero(distribucionInsertar.get("detcxhonorarios_"+a)+""));
						detCargo.setDetAsocioCxSalasMat(Utilidades.convertirAEntero(distribucionInsertar.get("detascxsalmat_"+a)+""));
						detCargo.setCodigoTipoAsocio(Utilidades.convertirAEntero(distribucionInsertar.get("tipoasocio_"+a)+""));
						detCargo.setFacturado(ConstantesBD.acronimoNo);
						detCargo.setCodigoTipoSolicitud(Utilidades.convertirAEntero(distribucionInsertar.get("tiposolicitud_"+a)+""));
						detCargo.setPaquetizado(distribucionInsertar.get("paquetizada_"+a)+"");
						detCargo.setCodigoSolicitudSubCuenta(solicitudSubCuenta);
						detCargo.setCodigoContrato(Utilidades.convertirAEntero(distribucionInsertar.get("contrato_"+a)+""));
						detCargo.setCodigoDetalleCargo(Cargos.insertarDetalleCargos(con, detCargo, usuario.getLoginUsuario()));
						exitoso=detCargo.getCodigoDetalleCargo()>0;
						
						///insertar el consumo para el caso de materiales.
						if(detCargo.getCodigoTipoSolicitud()==ConstantesBD.codigoTipoSolicitudCirugia)
						{
							boolean esCirugiaPorConsumo=UtilidadesSalas.esCxPorConsumoMateriales(con,detCargo.getNumeroSolicitud()); 
							String tipoServicioAsocio=UtilidadesSalas.obtenerCodigoTipoServicioAsocio(con, detCargo.getCodigoTipoAsocio());
							double valorTarifa=detCargo.getValorTotalCargado();
							if(tipoServicioAsocio.equals(ConstantesBD.codigoServicioMaterialesCirugia+"") && esCirugiaPorConsumo && valorTarifa>0)
							{	
								double porcentaje=detCargo.getPorcentajeCargado();
								int servicioCx=detCargo.getCodigoServicioCx();
								double codigoDetalleCargo=detCargo.getCodigoDetalleCargo();
								guardarDistribucionConsumoMateriales(con,porcentaje,servicioCx,codigoDetalleCargo,detCargo.getNumeroSolicitud(),usuario.getLoginUsuario(),detCargo.getTipoDistribucion());
							}
							
							//MT 6584 Actualiza tabla solicitud_cirugias
							int consecutivoServicioCx= SolicitudesCx.obtenerConsecutivoServicioCx(con, Utilidades.convertirAEntero(distribucionInsertar.get("serviciocx_"+a)+""), distribucionInsertar.get("solicitud_"+a)+"");
							if(consecutivoServicioCx==1)
							{
								if(!SolicitudesCx.actualizarSubCuentaCx(con, Utilidades.convertirADouble(distribucionInsertar.get("subcuenta_"+a)+""), distribucionInsertar.get("solicitud_"+a)+""))
								{
									exitoso=false;
								}
							}
						}
					}
					else
					{
						a=Utilidades.convertirAEntero(distribucionInsertar.get("numRegistros")+"");
						exitoso=false;
						
					}
				}
				else
				{
					a=Utilidades.convertirAEntero(distribucionInsertar.get("numRegistros")+"");
				}
			}
			return exitoso;
		}
		catch (IPSException ipsme) {
			throw ipsme;
		}
		catch (Exception e) {
			Log4JManager.error(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}	
	}

	/**
	 * 
	 * @param con
	 * @param analizarTarifa
	 * @param validarMonto 
	 * @param numSolicitud 
	 * @param usuario 
	 * @param servicio
	 * @param indice, Indice del mapa Filtro, para aplicar.
	 * @param analizarCobertura, hacer analisis de coberturas.
	 * @return
	 */
	private boolean validoServArtParaResponsable(Connection con, DistribucionCuentaForm forma, int indiceSol,int indice, boolean analizarCobertura, boolean analizarTarifa, boolean validarMonto, int numSolicitud, UsuarioBasico usuario,DtoSubCuentas responsable) throws IPSException 
	{
		boolean valido=true;
		try {
			if(!UtilidadTexto.isEmpty(forma.getFiltroDistribucion("viaingreso_"+indice)+""))
			{
				valido=((forma.getDetSolicitudesPaciente("viaingreso_"+indiceSol)+"").equals(forma.getFiltroDistribucion("viaingreso_"+indice)+""));
			}
			if(!UtilidadTexto.isEmpty(forma.getFiltroDistribucion("tipopaciente_"+indice)+""))
			{
				valido=((forma.getDetSolicitudesPaciente("tipopaciente_"+indiceSol)+"").equals(forma.getFiltroDistribucion("tipopaciente_"+indice)+""));
			}
			if(!UtilidadTexto.isEmpty(forma.getFiltroDistribucion("ccsol_"+indice)+""))
			{
				valido=((forma.getDetSolicitudesPaciente("codccsolicita_"+indiceSol)+"").equals(forma.getFiltroDistribucion("ccsol_"+indice)+""));
			}
			if(!UtilidadTexto.isEmpty(forma.getFiltroDistribucion("cceje_"+indice)+""))
			{
				valido=((forma.getDetSolicitudesPaciente("codccejecuta_"+indiceSol)+"").equals(forma.getFiltroDistribucion("cceje_"+indice)+""));
			}
			if(!UtilidadTexto.isEmpty(forma.getFiltroDistribucion("fechainicial_"+indice)+"")&&!UtilidadTexto.isEmpty(forma.getFiltroDistribucion("fechafinal_"+indice)+""))
			{
				valido=UtilidadFecha.validarFechaRango(UtilidadFecha.conversionFormatoFechaAAp(forma.getFiltroDistribucion("fechainicial_"+indice)+""),UtilidadFecha.conversionFormatoFechaAAp(forma.getFiltroDistribucion("fechafinal_"+indice)+""),forma.getDetSolicitudesPaciente("fechasolicitud_"+indiceSol)+"");
			}
			if(valido&&analizarCobertura)
			{
				if(!UtilidadTexto.isEmpty(forma.getDetSolicitudesPaciente("servicio_"+indiceSol)+""))
				{
					InfoCobertura infoCobertura=new InfoCobertura();
					//en caso de que la cobertura y el descuento sea por cirugia debe hacerse esta validacion  para el serviciocx_
					if(Integer.parseInt(forma.getDetSolicitudesPaciente("tiposolicitud_"+indiceSol)+"")==ConstantesBD.codigoTipoSolicitudCirugia)
					{
						infoCobertura=Cobertura.validacionCoberturaServicioDadoResponsable(con, responsable, Utilidades.convertirAEntero(forma.getDetSolicitudesPaciente("viaingreso_"+indiceSol)+""),forma.getDetSolicitudesPaciente("tipopaciente_"+indiceSol)+"", Utilidades.convertirAEntero(forma.getDetSolicitudesPaciente("serviciocx_"+indiceSol)+""), usuario.getCodigoInstitucionInt());
						
						
					}
					else
					{
						infoCobertura=Cobertura.validacionCoberturaServicioDadoResponsable(con, responsable, Utilidades.convertirAEntero(forma.getDetSolicitudesPaciente("viaingreso_"+indiceSol)+""), forma.getDetSolicitudesPaciente("tipopaciente_"+indiceSol)+"",Utilidades.convertirAEntero(forma.getDetSolicitudesPaciente("servicio_"+indiceSol)+""), usuario.getCodigoInstitucionInt());
					}
					valido=infoCobertura.incluido();
				}
				else if(!UtilidadTexto.isEmpty(forma.getDetSolicitudesPaciente("articulo_"+indiceSol)+""))
				{
					InfoCobertura infoCobertura=Cobertura.validacionCoberturaArticuloDadoResponsable(con, responsable, Utilidades.convertirAEntero(forma.getDetSolicitudesPaciente("viaingreso_"+indiceSol)+""), forma.getDetSolicitudesPaciente("tipopaciente_"+indiceSol)+"",Utilidades.convertirAEntero(forma.getDetSolicitudesPaciente("articulo_"+indiceSol)+""), usuario.getCodigoInstitucionInt());
					valido=infoCobertura.incluido();
				}
				
			}
			if(valido&&analizarTarifa&&Utilidades.convertirAEntero(forma.getDetSolicitudesPaciente("tiposolicitud_"+indiceSol)+"")!=ConstantesBD.codigoTipoSolicitudCirugia)
			{
				int numeroSolicitud=Utilidades.convertirAEntero(forma.getDetSolicitudesPaciente("solicitud_"+forma.getIndice())+"");
				String fechaCalculosCargos=Cargos.obtenerFechaCalculoCargo(con, numeroSolicitud);
				
				if(!UtilidadTexto.isEmpty(forma.getDetSolicitudesPaciente("servicio_"+indiceSol)+""))
				{
					int esquemaTarifario=forma.getResponsables().get(indice).getEsquemaTarifarioServiciosPpalOoriginal(con,forma.getResponsables().get(indice).getSubCuenta(),forma.getResponsables().get(indice).getContrato(),Utilidades.convertirAEntero(forma.getDetSolicitudesPaciente("servicio_"+indiceSol)+""),fechaCalculosCargos, Cargos.obtenerCentroAtencionCargoSolicitud(con,numeroSolicitud));
					valido=Cargos.obtenerTarifaBaseServicio(con, EsquemaTarifario.obtenerTarifarioOficialXCodigoEsquemaTar(con, esquemaTarifario), Utilidades.convertirAEntero(forma.getDetSolicitudesPaciente("servicio_"+indiceSol)+""), esquemaTarifario, fechaCalculosCargos).isExiste();
				}
				else if(!UtilidadTexto.isEmpty(forma.getDetSolicitudesPaciente("articulo_"+indiceSol)+""))
				{
					int esquemaTarifario=forma.getResponsables().get(indice).getEsquemaTarifarioArticuloPpalOoriginal(con,forma.getResponsables().get(indice).getSubCuenta(),forma.getResponsables().get(indice).getContrato(),Utilidades.convertirAEntero(forma.getDetSolicitudesPaciente("articulo_"+indiceSol)+""),fechaCalculosCargos, Cargos.obtenerCentroAtencionCargoSolicitud(con,numeroSolicitud));
					valido=Cargos.obtenerTarifaBaseArticulo(con, Utilidades.convertirAEntero(forma.getDetSolicitudesPaciente("articulo_"+indiceSol)+""), esquemaTarifario, fechaCalculosCargos)>0;
				}
			}
		
			if(valido&&validarMonto)
			{
				if(UtilidadTexto.isEmpty(forma.getResponsables().get(indice).getMontoAutorizado()))
				{
					valido=true;
				}
				else
				{
					double valorTotalPaquetesResponsable = Utilidades.obtenerValorTotalPaquetesResponsable(con, forma.getResponsables().get(indice).getSubCuenta());
					valido=(Utilidades.convertirADouble(responsable.getMontoAutorizado(),true)-valorTotalPaquetesResponsable-DistribucionCuenta.obtenerValorFacturadoSoatEstatico(con,forma.getCodigoIngreso(),responsable.getConvenio().getCodigo())-Utilidades.convertirADouble(responsable.getValorUtilizadoSoat(),true))>0;
				}
			}
			return valido;
		}
		catch (IPSException ipsme) {
			throw ipsme;
		}
		catch (Exception e) {
			Log4JManager.error(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
	}


	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mapping 
	 * @return
	 */
	private ActionForward accionEliminarResponsables(Connection con, DistribucionCuentaForm forma, ActionMapping mapping) 
	{
		logger.info("Voy a elimiar el responsable "+forma.getIndiceReponsableEliminar());
		forma.getResponsablesEliminados().add(forma.getResponsables().get(Integer.parseInt(forma.getIndiceReponsableEliminar())));
		
		///reasignando el numero de prioridad.
		for(int i=Integer.parseInt(forma.getIndiceReponsableEliminar());i<forma.getResponsables().size();i++)
		{
			if(!UtilidadTexto.getBoolean(forma.getResponsables().get(i).getFacturado()))
			{
				int nroPrioridad=forma.getResponsables().get(i).getNroPrioridad();
				forma.getResponsables().get(i).setNroPrioridad(nroPrioridad-1);
			}
		}
		forma.getResponsables().remove(Integer.parseInt(forma.getIndiceReponsableEliminar()));
		
		for (int i = 0; i < forma.getResponsables().size(); i++) {
					forma.getResponsables().get(i).setNroPrioridad(i+1);
		}
		
		
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}

	/**
	 * 
	 * @param con
	 * @param forma
	 * @param response
	 * @return
	 */
	private ActionForward accionFiltroFechaAfiliacion(Connection con, DistribucionCuentaForm forma, HttpServletResponse response) 
	{
		String aux = "";
		
		if(UtilidadFecha.validarFecha(forma.getFechaAfiliacion()))
		{
			String fechaSistema = UtilidadFecha.getFechaActual(con);
			if(UtilidadFecha.esFechaMenorIgualQueOtraReferencia(forma.getFechaAfiliacion(), fechaSistema))
			{
				aux ="<fecha-valida>"+ConstantesBD.acronimoSi+"</fecha-valida>";
				
				int numeroDias = UtilidadFecha.numeroDiasEntreFechas(forma.getFechaAfiliacion(), fechaSistema);
				int numeroSemanas = (numeroDias/7) + (numeroDias%7!=0?1:0);
				aux += "<semanas>"+numeroSemanas+"</semanas>";
				aux += "<meses>"+UtilidadFecha.numeroMesesEntreFechas(forma.getFechaAfiliacion(), fechaSistema,false)+"</meses>";
			}
			else
			{
				aux ="<fecha-valida>"+ConstantesBD.acronimoNo+"</fecha-valida>";
				aux +="<mensaje>La fecha de afiliaciï¿½n debe ser anterior o igual a la fecha actual.</mensaje>";
			}
			
		}
		else
		{
			aux ="<fecha-valida>"+ConstantesBD.acronimoNo+"</fecha-valida>";
			aux +="<mensaje>La fecha de afiliaciï¿½n no es una fecha vï¿½lida. Debe estar en formato dd/mm/aaaa.</mensaje>";
		}
		
		
		
		UtilidadBD.closeConnection(con);
		//**********SE GENERA RESPUESTA PARA AJAX EN XML**********************************************
		try
		{
			response.setContentType("text/xml");
			response.setHeader("Cache-Control", "no-cache");
	        response.getWriter().write("<respuesta>");
	        response.getWriter().write(aux);
	        response.getWriter().write("</respuesta>");
		}
		catch(IOException e)
		{
			logger.error("Error al enviar respuesta AJAX en accionFiltroFechaAfiliacion: "+e);
		}
		return null;
	}

	@SuppressWarnings({ "unchecked", "rawtypes", "unused" })
	private ActionForward filtroTipoAfiliado(Connection con, DistribucionCuentaForm forma, UsuarioBasico usuario, HttpServletResponse response) throws IPSException{
		HashMap mapaRespuesta = new HashMap();
		String aux = "", codigoRegimen = "";
		HashMap mapaAux = new HashMap();
		ArrayList<HashMap<String, Object>> arregloAux = new ArrayList<HashMap<String,Object>>();
		
		Convenio mundoConvenio = new Convenio();
		int codConvenio = forma.getIndice() == ConstantesBD.codigoNuncaValido ? forma.getCodigoConvenio() : 
			forma.getResponsables().get(forma.getIndice()).getConvenio().getCodigo();
		mundoConvenio.cargarResumen(con, codConvenio);
		
		HashMap tiposAfiliado = UtilidadesFacturacion.cargarTiposAfiliadoXEstrato(
				con, usuario.getCodigoInstitucionInt(),ConstantesBD.acronimoSi, 
				mundoConvenio.getCodigo(),
				Utilidades.convertirAEntero(forma.getInfoIngreso("codigoViaIngreso")+""),
				Utilidades.convertirAEntero(forma.getInfoIngreso("codigoEstratoSocial").toString()),
				Utilidades.capturarFechaBD());
		
		forma.setTiposAfiliadoConvenio(tiposAfiliado);
		
		forma.setNaturalezasPaciente(
				Utilidades.obtenerNaturalezasPacienteXTipoAfiliadoEstrato(
						con,forma.getInfoIngreso("codigoTipoRegimen")+"", 
						mundoConvenio.getCodigo(),
						Utilidades.convertirAEntero(forma.getInfoIngreso("codigoViaIngreso")+""),
						forma.getTiposAfiliadoConvenio().get("acronimo_0").toString(), 
						Utilidades.convertirAEntero(forma.getInfoIngreso("codigoEstratoSocial").toString()),  
						Utilidades.capturarFechaBD()));
		
		aux = "<tipos-afiliado>";
		for(int i=0;i<Utilidades.convertirAEntero(forma.getTiposAfiliadoConvenio().get("numRegistros").toString());i++)
			aux += "" +
				"<tipo-afiliado>" +
					"<acronimo>"+forma.getTiposAfiliadoConvenio().get("acronimo_"+i)+"</acronimo>" +
					"<nombre>"+forma.getTiposAfiliadoConvenio().get("nombre_"+i)+"</nombre>" +
				"</tipo-afiliado>";
		aux += "</tipos-afiliado>";
		mapaRespuesta.put("respuestaTiposAfiliado", aux);

		aux = "<naturalezas-paciente>";
		for(int i=0;i<forma.getNaturalezasPaciente().size();i++)
			aux += "" +
				"<naturaleza-paciente>" +
					"<codigo>"+ forma.getNaturalezasPaciente().get(i).getId()+ "</codigo>" +
					"<nombre>"+ forma.getNaturalezasPaciente().get(i).getNombre()+ "</nombre>" +
				"</naturaleza-paciente>";
		aux += "</naturalezas-paciente>";
		mapaRespuesta.put("respuestaNaturaleza", aux);
		
		UtilidadBD.closeConnection(con);
		//**********SE GENERA RESPUESTA PARA AJAX EN XML**********************************************
		try
		{
			response.setCharacterEncoding("UTF-8");
            response.setContentType("text/xml");
			response.setHeader("Cache-Control", "no-cache");
			response.getWriter().write("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
	        response.getWriter().write("<respuesta>");
	        response.getWriter().write(mapaRespuesta.get("respuestaTiposAfiliado").toString());
	        response.getWriter().write(mapaRespuesta.get("respuestaNaturaleza").toString());
	        response.getWriter().write("</respuesta>");
		}
		catch(IOException e)
		{
			logger.error("Error al enviar respuesta AJAX en accionFiltroConvenio: "+e);
		}
		return null;
	}
	
	
	@SuppressWarnings({ "unchecked", "rawtypes", "unused" })
	private ActionForward filtroNaturalezaPaciente(Connection con, DistribucionCuentaForm forma, HttpServletResponse response) {
		HashMap mapaRespuesta = new HashMap();
		String aux = "", codigoRegimen = "";
		HashMap mapaAux = new HashMap();
		ArrayList<HashMap<String, Object>> arregloAux = new ArrayList<HashMap<String,Object>>();
		
		forma.setNaturalezasPaciente(
				Utilidades.obtenerNaturalezasPacienteXTipoAfiliadoEstrato(
						con,forma.getInfoIngreso("codigoTipoRegimen")+"", 
						forma.getCodigoConvenio(),
						Utilidades.convertirAEntero(forma.getInfoIngreso("codigoViaIngreso")+""),
						forma.getInfoIngreso("codigoTipoAfiliado")+"", 
						Utilidades.convertirAEntero(forma.getInfoIngreso("codigoEstratoSocial").toString()),  
						Utilidades.capturarFechaBD()));
		
		aux = "<naturalezas-paciente>";
		for(int i=0;i<forma.getNaturalezasPaciente().size();i++)
			aux += "" +
				"<naturaleza-paciente>" +
				"<codigo>"+ forma.getNaturalezasPaciente().get(i).getCodigo()+ "</codigo>" +
				"<nombre>"+ forma.getNaturalezasPaciente().get(i).getNombre()+ "</nombre>" +
				"</naturaleza-paciente>";
		aux += "</naturalezas-paciente>";
		mapaRespuesta.put("respuestaNaturaleza", aux);
		
		//**********SE GENERA RESPUESTA PARA AJAX EN XML**********************************************
		try
		{
			response.setCharacterEncoding("UTF-8");
            response.setContentType("text/xml");
			response.setHeader("Cache-Control", "no-cache");
			response.getWriter().write("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
	        response.getWriter().write("<respuesta>");
	        response.getWriter().write(mapaRespuesta.get("respuestaNaturaleza").toString());
	        response.getWriter().write("</respuesta>");
		}
		catch(IOException e)
		{
			logger.error("Error al enviar respuesta AJAX en accionFiltroConvenio: "+e);
		}
		return null;
	}
	
	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param paciente
	 * @param usuario
	 * @param mapping
	 * @param request 
	 * @return
	 */
	private ActionForward accionInsertarInfoResponsable(Connection con, DistribucionCuentaForm forma, DistribucionCuenta mundo, PersonaBasica paciente, UsuarioBasico usuario, ActionMapping mapping, HttpServletRequest request) 
	{
		ActionErrors errores = new ActionErrors();
		boolean transaccion=UtilidadBD.iniciarTransaccion(con);
		
		Utilidades.imprimirMapa(forma.getInfoIngreso());
		
		logger.info("CODIGO CONTRATO ->"+Integer.parseInt(forma.getInfoIngreso().get("codigoContrato")+""));
		
		String sinContrato ="";
		sinContrato = UtilidadesFacturacion.esSinContrato(con, Integer.parseInt(forma.getInfoIngreso().get("codigoContrato")+""));
		
		logger.info("SIN CONTRATO ->"+sinContrato);
		
		String controlAnticipos ="";
		controlAnticipos = UtilidadesFacturacion.esControlAnticipos(con, Integer.parseInt(forma.getInfoIngreso().get("codigoContrato")+""));
		
		logger.info("CONTROL ANTICIPOS ->"+controlAnticipos);
		
		
		ArrayList<String> aux = new ArrayList<String>();
		
		//	PREGUNTO SI EL CONTRATO TIENE ACTIVO EL CAMPO SIN CONTRATO
		if(sinContrato.equals(ConstantesBD.acronimoSi))
		{
			// PREGUNTO SI EL CONTRATO TIENE ACTIVO EL CAMPO CONTROLA ANTICIPOS
			if(controlAnticipos.equals(ConstantesBD.acronimoSi))
			{
				logger.info("Es Sin Contrato y Control Anticipos");
				ElementoApResource elemento = new ElementoApResource("errors.notEspecific");
				elemento.agregarAtributo("Convenio del Paciente Sin Contrato y Contrato del Paciente Requiere Anticipo. Por Favor Verificar.");
				forma.getMensajesAlerta().add(elemento);
				
			}
			else
			{
				logger.info("Es solo Sin contrato");
				ElementoApResource elemento = new ElementoApResource("errors.notEspecific");
				elemento.agregarAtributo("Convenio del Paciente Sin Contrato. Por Favor Verificar.");
				forma.getMensajesAlerta().add(elemento);
				//aux.add("Convenio del Paciente Sin Contrato. Por Favor Verificar. ");
				//forma.setMensajes(aux);
			}
			
			// Pregunto si el control boolean del mensaje esta activo
			if(forma.getControlMensaje().equals(false))
			{
				forma.setControlMensaje(true);
				UtilidadBD.closeConnection(con);
				saveErrors(request, errores);
				return mapping.findForward("infoResponsable");
			}
			
		}
		else
		{
			if(controlAnticipos.equals(ConstantesBD.acronimoSi))
			{
				logger.info("Es Sin Contrato y Control Anticipos");
				ElementoApResource elemento = new ElementoApResource("errors.notEspecific");
				elemento.agregarAtributo("Contrato del Paciente Requiere Anticipo. Por Favor Verificar.");
				forma.getMensajesAlerta().add(elemento);
				//aux.add("Contrato del Paciente Requiere Anticipo. Por Favor Verificar. ");
				//forma.setMensajes(aux);
				
				//	Pregunto si el control boolean del mensaje esta activo
				if(forma.getControlMensaje().equals(false))
				{
					forma.setControlMensaje(true);
					UtilidadBD.closeConnection(con);
					saveErrors(request, errores);
					return mapping.findForward("infoResponsable");
				}
			}
		}
		
		
		
		try
		{	
			
			IValidacionTipoCobroPacienteServicio servicioValidacion=FacturacionServicioFabrica.crearValidacionTipoCobroPacienteServicio();
			DtoValidacionTipoCobroPaciente validacion=servicioValidacion.validarTipoCobroPacienteServicioConvenioContrato(Utilidades.convertirAEntero(forma.getInfoIngreso("codigoContrato")+""));
			forma.setInfoIngreso("tipoCobroPaciente",validacion.getTipoCobroPaciente()+"");
		//	forma.setInfoIngreso("tipoMontoCobro","");
			forma.setInfoIngreso("porcentajeMontoCobro","");

			

			if(!UtilidadTexto.getBoolean(validacion.getMostrarCalisificacion()))
				forma.setInfoIngreso("codigoEstratoSocial",ConstantesBD.codigoNuncaValido+"");
			if(!UtilidadTexto.getBoolean(validacion.getMostrarTipoAfiliado()))
			//	forma.setInfoIngreso("codigoTipoAfiliado","");
			if(!UtilidadTexto.getBoolean(validacion.getMostrarNaturalezaPaciente()))
				forma.setInfoIngreso("codigoNaturaleza",ConstantesBD.codigoNuncaValido+"");
			
			if(UtilidadTexto.getBoolean(validacion.getManejaMontos()))
			{
				IMontosCobroServicio servicioMontoCobro=FacturacionServicioFabrica.crearMontosCobroServicio();
				DTOResultadoBusquedaDetalleMontos dtoMonto=servicioMontoCobro.obtenerDetalleMontoCobroPorId(Utilidades.convertirAEntero(forma.getInfoIngreso("codigoMontoCobro")+""));
				forma.setInfoIngreso("tipoMontoCobro",dtoMonto.getTipoDetalleAcronimo()+"");
			}
			else
			{
				forma.setInfoIngreso("codigoMontoCobro",ConstantesBD.codigoNuncaValido+"");
				forma.setInfoIngreso("porcentajeMontoCobro",validacion.getPorcentajeMontoCobro()+"");
			}
			con=UtilidadBD.abrirConexion();
			transaccion=mundo.insertarSubCuenta(con,(HashMap)forma.getInfoIngreso().clone(),(HashMap)forma.getRequisitosPaciente().clone(),usuario.getLoginUsuario(),forma.getUltimaViaIngreso(),(HashMap)forma.getVerificacion().clone());
		 	forma.setMontosCombro( mundo.consultarMontoCobro(con, Integer.valueOf(String.valueOf( forma.getInfoIngreso("codigoMontoCobro")))));
			HashMap montos = new HashMap();
			montos.put("codigo", forma.getMontosCombro().getCodigoMonto());
			montos.put("descripcion", forma.getMontosCombro().getDescripcion());
			forma.setMontosCobroResumen(montos);
		 	
		 	//UtilidadBD.closeConnection(con);
			
			forma.setTiposAfiliadolista(forma.getTiposAfiliadoConvenio());
			
			//asignarTipoAfiliadoSeleccionado(forma);
		}
		catch(Exception e)
		{
			transaccion=false;
			e.printStackTrace();
		}
		
		if(transaccion)
		{
			UtilidadBD.finalizarTransaccion(con);
		}
		else
		{
			UtilidadBD.abortarTransaccion(con);
		}
		UtilidadBD.closeConnection(con);
		return mapping.findForward("resumenInfoResponsable");
	}

	/**
	 * 
	 * @param forma
	 */
	private void asignarTipoAfiliadoSeleccionado(DistribucionCuentaForm forma) 
	{
		HashMap campos = new HashMap();
		campos.put("institucion","2");
		/*
		campos.put("activo","true");
		campos.put("tipoRegimen", "1");
		campos.put("codigoConvenio", "1");
		campos.put("codigoViaIngreso", "A");
		*/
		campos.put("tipoAfiliado", "C");
		
		int inctitucion = 2;
		int convenio = 1;
		
		Connection con = null;
		con = UtilidadBD.abrirConexion();
		forma.setEstratosSocialesConvenio(
				UtilidadesFacturacion.cargarEstratosSociales(con, inctitucion, ConstantesBD.acronimoSi, 
						forma.getInfoIngreso("codigoTipoRegimen")+"", convenio,
						Utilidades.convertirAEntero(forma.getInfoIngreso("codigoViaIngreso")+""),
						Utilidades.capturarFechaBD()));
		
	}

	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mundo 
	 * @param paciente
	 * @param usuario
	 * @param mapping
	 * @return
	 */
	private ActionForward accionModificarInfoResponsable(Connection con, DistribucionCuentaForm forma, DistribucionCuenta mundo, PersonaBasica paciente, UsuarioBasico usuario, ActionMapping mapping) 
	{
		boolean transaccion=UtilidadBD.iniciarTransaccion(con);
		try
		{
			
			IValidacionTipoCobroPacienteServicio servicioValidacion=FacturacionServicioFabrica.crearValidacionTipoCobroPacienteServicio();
			DtoValidacionTipoCobroPaciente validacion=servicioValidacion.validarTipoCobroPacienteServicioConvenioContrato(Utilidades.convertirAEntero(forma.getInfoIngreso("codigoContrato")+""));
			forma.setInfoIngreso("tipoCobroPaciente",validacion.getTipoCobroPaciente()+"");
			forma.setInfoIngreso("tipoMontoCobro","");
			forma.setInfoIngreso("porcentajeMontoCobro","");

			

			if(!UtilidadTexto.getBoolean(validacion.getMostrarCalisificacion()))
				forma.setInfoIngreso("codigoEstratoSocial",ConstantesBD.codigoNuncaValido+"");
			if(!UtilidadTexto.getBoolean(validacion.getMostrarTipoAfiliado()))
				forma.setInfoIngreso("codigoTipoAfiliado","");
			if(!UtilidadTexto.getBoolean(validacion.getMostrarNaturalezaPaciente()))
				forma.setInfoIngreso("codigoNaturaleza",ConstantesBD.codigoNuncaValido+"");
			
			if(UtilidadTexto.getBoolean(validacion.getManejaMontos()))
			{
				IMontosCobroServicio servicioMontoCobro=FacturacionServicioFabrica.crearMontosCobroServicio();
				DTOResultadoBusquedaDetalleMontos dtoMonto=servicioMontoCobro.obtenerDetalleMontoCobroPorId(Utilidades.convertirAEntero(forma.getInfoIngreso("codigoMontoCobro")+""));
				forma.setInfoIngreso("tipoMontoCobro",dtoMonto.getTipoDetalleAcronimo()+"");
			}
			else
			{
				forma.setInfoIngreso("codigoMontoCobro",ConstantesBD.codigoNuncaValido+"");
				forma.setInfoIngreso("porcentajeMontoCobro",validacion.getPorcentajeMontoCobro()+"");
			}
			
			
			transaccion=mundo.modificarSubCuenta(con,(HashMap)forma.getInfoIngreso().clone(),(HashMap)forma.getRequisitosPaciente().clone(),usuario.getLoginUsuario(), (HashMap)forma.getVerificacion().clone());
		}
		catch(Exception e)
		{
			transaccion=false;
			e.printStackTrace();

		}
		
		forma.setMontosCombro( mundo.consultarMontoCobro(con, Integer.valueOf(String.valueOf( forma.getInfoIngreso("codigoMontoCobro")))));
		HashMap montos = new HashMap();
		montos.put("codigo", forma.getMontosCombro().getCodigoMonto());
		montos.put("descripcion", forma.getMontosCombro().getDescripcion());
		forma.setMontosCobroResumen(montos);

		
		
		if(transaccion)
		{
			UtilidadBD.finalizarTransaccion(con);
		}
		else
		{
			UtilidadBD.abortarTransaccion(con);
		}

		UtilidadBD.closeConnection(con);
		return mapping.findForward("resumenInfoResponsable");
	}


	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mundo 
	 * @param mapping
	 * @param request 
	 * @return
	 */
	private ActionForward accionCargarInfoResponsable(Connection con, DistribucionCuentaForm forma,DistribucionCuenta mundo, PersonaBasica paciente,UsuarioBasico usuario, ActionMapping mapping, HttpServletRequest request) 
	{

		try {
			int codNaturaleza = ConstantesBD.codigoNuncaValido;
			forma.setPuedoGrabarConvenioAdicional(true);
			if(forma.isModificacionResponsable())
			{
				CuentasDelegate cuentaDao=new CuentasDelegate();
				Cuentas cuenta=cuentaDao.findById(forma.getCuenta());
				
				//Se carga la informaciï¿½n del convenio
				Convenio mundoConvenio = new Convenio();
				mundoConvenio.cargarResumen(con, forma.getResponsables().get(forma.getIndice()).getConvenio().getCodigo());
				
				forma.setInfoIngreso("cuenta",paciente.getCodigoCuenta());
				forma.setInfoIngreso("ingreso",forma.getCodigoIngreso());
				forma.setInfoIngreso("responsable",forma.getResponsables().get(forma.getIndice()).getSubCuenta());
				forma.setInfoIngreso("codigoConvenio", mundoConvenio.getCodigo()+"");
				forma.setInfoIngreso("codigoContrato", forma.getResponsables().get(forma.getIndice()).getContrato());
				forma.setInfoIngreso("codigoViaIngreso", cuenta.getViasIngreso().getCodigo());
				
				forma.setInfoIngreso("codigoTipoPaciente", cuenta.getTiposPaciente().getAcronimo());
				
				forma.setInfoIngreso("nombreConvenio", mundoConvenio.getNombre());
				forma.setInfoIngreso("esConvenioPyp", UtilidadTexto.getBoolean(mundoConvenio.getPyp())?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
				forma.setInfoIngreso("esConvenioCapitado", mundoConvenio.getTipoContrato()==ConstantesBD.codigoTipoContratoCapitado?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
				forma.setInfoIngreso("esConvenioPoliza", UtilidadTexto.getBoolean(mundoConvenio.getCheckInfoAdicCuenta())?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
				forma.setInfoIngreso("esConvenioSoat", esConvenioTipoConventioEventoCatTrans(mundoConvenio.getCodigo())?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
				forma.setInfoIngreso("esRequiereAutorizacionServicio", mundoConvenio.getRequiere_autorizacion_servicio());
				forma.setInfoIngreso("esReporteAtencionInicialUrgencias", mundoConvenio.getReporte_atencion_ini_urg());
				
				forma.setInfoIngreso("valorUtilizadoSoat", forma.getResponsables().get(forma.getIndice()).getValorUtilizadoSoat());
				
					
				forma.setInfoIngreso("esConvenioSoat", esConvenioTipoConventioEventoCatTrans(mundoConvenio.getCodigo())?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
				forma.setInfoIngreso("codigoTipoRegimen", mundoConvenio.getTipoRegimen());
				forma.setInfoIngreso("codigoTipoCobertura", forma.getResponsables().get(forma.getIndice()).getCodigoTipoCobertura());
				forma.setInfoIngreso("nombreTipoCobertura", forma.getResponsables().get(forma.getIndice()).getNombreTipoCobertura());
				forma.setInfoIngreso("nombreTipoRegimen", mundoConvenio.getDescripcionTipoRegimen());
				forma.setInfoIngreso("codigoNaturaleza", ConstantesBD.codigoNaturalezaPacientesNinguno+"");
				forma.setInfoIngreso("requiereCarnet", mundoConvenio.getRequiereNumeroCarnet());
				
				forma.setInfoIngreso("manejoComplejidad", mundoConvenio.getManejaComplejidad());
				
				forma.setInfoIngreso("isConvenioManejaMontos", mundoConvenio.isConvenioManejaMontoCobro());
				
				/*if(UtilidadTexto.getBoolean(forma.getInfoIngreso("esConvenioPoliza")+""))
				{
					HashMap mapaInfoPoliza=mundo.consultarInformacionPoliza(con,forma.getResponsables().get(forma.getIndice()).getSubCuenta());
					forma.setInfoIngreso("apellidosPoliza", mapaInfoPoliza.get("apellidosPoliza")+"");
					forma.setInfoIngreso("nombresPoliza", mapaInfoPoliza.get("nombresPoliza")+"");
					forma.setInfoIngreso("tipoIdPoliza", mapaInfoPoliza.get("tipoIdPoliza")+"");
					forma.setInfoIngreso("numeroIdPoliza", mapaInfoPoliza.get("numeroIdPoliza")+"");
					forma.setInfoIngreso("direccionPoliza", mapaInfoPoliza.get("direccionPoliza")+"");
					forma.setInfoIngreso("telefonoPoliza", mapaInfoPoliza.get("telefonoPoliza")+"");
					forma.setInfoIngreso("autorizacionPoliza", mapaInfoPoliza.get("autorizacionPoliza")+"");
					forma.setInfoIngreso("valorPoliza", mapaInfoPoliza.get("valorPoliza")+"");
					forma.setInfoIngreso("saldoPoliza", mapaInfoPoliza.get("saldoPoliza")+"");
					forma.setInfoIngreso("fechaAutorizacionPoliza", mapaInfoPoliza.get("fechaAutorizacionPoliza")+"");
				}*/
					
				
				//Se cargan los contratos del convenio
				if(UtilidadTexto.getBoolean(forma.getInfoIngreso("esConvenioCapitado").toString()))
					forma.setContratosConvenio(UtilidadesManejoPaciente.obtenerContratosVigentesUsuarioCapitado(con, paciente.getCodigoPersona(), mundoConvenio.getCodigo()));
				else
					forma.setContratosConvenio(Utilidades.obtenerContratos(con, mundoConvenio.getCodigo(), false, true));
				
				if(forma.getContratosConvenio().size()==1){
					final int unicoContrato=0;
					verificarMontoCobro(forma,forma.getContratosConvenio().get(unicoContrato).get("codigo").toString());
				}
				
				//se cargan los estratos sociales del contrato
				forma.setEstratosSocialesConvenio(
						UtilidadesFacturacion.cargarEstratosSociales(con, usuario.getCodigoInstitucionInt(), 
								ConstantesBD.acronimoSi, forma.getInfoIngreso("codigoTipoRegimen")+"", 
								mundoConvenio.getCodigo(),
								Utilidades.convertirAEntero(forma.getInfoIngreso("codigoViaIngreso")+""),
								Utilidades.capturarFechaBD()));
				
				int codigoEstrato = forma.getResponsables().get(forma.getIndice()).getClasificacionSocioEconomica();
				
				forma.setInfoIngreso("codigoEstratoSocial", codigoEstrato);
				
				forma.setTiposAfiliadoConvenio(
						UtilidadesFacturacion.cargarTiposAfiliadoXEstrato(
								con, usuario.getCodigoInstitucionInt(),ConstantesBD.acronimoSi, 
								mundoConvenio.getCodigo(),
								Utilidades.convertirAEntero(forma.getInfoIngreso("codigoViaIngreso")+""),
								codigoEstrato,
								Utilidades.capturarFechaBD()));
				
				String codigoTipoAfiliado = forma.getResponsables().get(forma.getIndice()).getTipoAfiliado();
				
				forma.setInfoIngreso("codigoTipoAfiliado", codigoTipoAfiliado);
				//***********SE CONSULTAN LAS NATURALEZAS DEL PACIENTE ****************************************************************
				//forma.setNaturalezasPaciente(Utilidades.obtenerNaturalezasPaciente(con,forma.getInfoIngreso("codigoTipoRegimen")+"", mundoConvenio.getCodigo(),Utilidades.convertirAEntero(forma.getInfoIngreso("codigoViaIngreso")+"")));
				forma.setNaturalezasPaciente(
						Utilidades.obtenerNaturalezasPacienteXTipoAfiliadoEstrato(
								con,forma.getInfoIngreso("codigoTipoRegimen")+"", 
								mundoConvenio.getCodigo(),
								Utilidades.convertirAEntero(forma.getInfoIngreso("codigoViaIngreso")+""),
								codigoTipoAfiliado, 
								codigoEstrato,  
								Utilidades.capturarFechaBD()));
				
				codNaturaleza = forma.getResponsables().get(forma.getIndice()).getNaturalezaPaciente() == 0 ? 
						ConstantesBD.codigoNuncaValido : forma.getResponsables().get(forma.getIndice()).getNaturalezaPaciente();
				forma.setInfoIngreso("codigoNaturaleza", codNaturaleza);
				
				if(UtilidadTexto.getBoolean(forma.getInfoIngreso("esConvenioCapitado").toString()))
				{
					//si solo tiene un contrato vigente se debe postular la informacion
					if(forma.getContratosConvenio().size()==1)
					{
						int codClasi=Utilidades.convertirAEntero(forma.getContratosConvenio().get(0).get("codigoestratosocial")+"");
						if(codClasi>0)
						{
							String tipoRegimenCSE=Utilidades.obtenerTipoRegimenClasificacionSocioEconomica(con, codClasi);
							if(mundoConvenio.getTipoRegimen().equals(tipoRegimenCSE))
							{
								forma.setInfoIngreso("codigoEstratoSocial", codClasi);
								HashMap tempo=new HashMap();
								tempo.put("numRegistros", "0");
								HashMap tempo1=new HashMap();
								tempo1.put("numRegistros", "0");
								for(int i=0;i<Utilidades.convertirAEntero(forma.getEstratosSocialesConvenio().get("numRegistros")+"");i++)
								{
									if(Utilidades.convertirAEntero(forma.getEstratosSocialesConvenio().get("codigo_"+i)+"")==codClasi)
									{
										tempo.put("codigo_0", forma.getEstratosSocialesConvenio().get("codigo_"+i)+"");
										tempo.put("descripcion_0", forma.getEstratosSocialesConvenio().get("descripcion_"+i)+"");
										tempo.put("numRegistros", "1");
										break;
									}
								}
								String tipoAfil=forma.getContratosConvenio().get(0).get("tipoafiliado")+"";
								if(!UtilidadTexto.isEmpty(tipoAfil+""))
								{
									//{numRegistros=2, acronimo_0=B, nombre_1=Cotizante, nombre_0=Beneficiario, acronimo_1=C}
									forma.setInfoIngreso("codigoTipoAfiliado", tipoAfil);
									
									for(int i=0;i<Utilidades.convertirAEntero(forma.getTiposAfiliadoConvenio().get("numRegistros")+"");i++)
									{
										if((forma.getTiposAfiliadoConvenio().get("acronimo_"+i)+"").equals(tipoAfil+""))
										{
											tempo1.put("acronimo_0", forma.getTiposAfiliadoConvenio().get("acronimo_"+i)+"");
											tempo1.put("nombre_0", forma.getTiposAfiliadoConvenio().get("nombre_"+i)+"");
											tempo1.put("numRegistros", "1");
											break;
										}
									}
			
								}
								if(ValoresPorDefecto.getPermitirModificarDatosUsuariosCapitados(usuario.getCodigoInstitucionInt()).equals(ConstantesBD.acronimoNo))
								{
									forma.setEstratosSocialesConvenio(tempo);
									if(!UtilidadTexto.isEmpty(tipoAfil))
									{
										forma.setTiposAfiliadoConvenio(tempo1);
									}
								}	
								

								HashMap<String, Object>infoIngreos=forma.getInfoIngreso();
								if(infoIngreos.get("codigoContrato")!=null&&!((String)infoIngreos.get("codigoContrato")).trim().isEmpty()
										&&!((String)infoIngreos.get("codigoContrato")).trim().equals(ConstantesBD.codigoNuncaValido+"")){
									/**VERIFICAR CONTRATOS, si hay uno solo consultar validacion tipo cobro paciente para verificar la obligatoriedad los campos:
									 *  
									 * Clasificacion socioeconomica
									 * Tipo afiliado
									 * Naturaleza Paciente
									 * Porcentaje de cobertura
									 * Cuota Verificacion
									 * 
									 * MT 4836
									 * */
									DtoValidacionTipoCobroPaciente validacion=null;
									if(!forma.getContratosConvenio().isEmpty()&&forma.getContratosConvenio().size()==1){
										IValidacionTipoCobroPacienteServicio validacionTipoCobroPacienteServicio=FacturacionServicioFabrica.crearValidacionTipoCobroPacienteServicio();
										validacion=validacionTipoCobroPacienteServicio.validarTipoCobroPacienteServicioConvenioContrato(Integer.parseInt(infoIngreos.get("codigoContrato").toString()));
									}
									if(validacion!=null&&UtilidadTexto.getBoolean(validacion.getMostrarCalisificacion())&&
											Utilidades.convertirAEntero(forma.getEstratosSocialesConvenio().get("numRegistros")+"")<=0)
									{
										forma.setPuedoGrabarConvenioAdicional(false);
										ElementoApResource mensaje = new ElementoApResource("errors.notEspecific");
										mensaje.agregarAtributo("La Clasificaciï¿½n socio econï¿½mica del paciente no corresponde a los Montos del Convenio. No se puede asignar este Convenio. Por favor verifique.");
										forma.getMensajesAlerta().add(mensaje);
									}
									if(validacion!=null&&UtilidadTexto.getBoolean(validacion.getMostrarTipoAfiliado())&&
											!UtilidadTexto.isEmpty(tipoAfil))
									{
										if(Utilidades.convertirAEntero(forma.getTiposAfiliadoConvenio().get("numRegistros")+"")<=0)
										{
											forma.setPuedoGrabarConvenioAdicional(false);
											ElementoApResource mensaje = new ElementoApResource("errors.notEspecific");
											mensaje.agregarAtributo("El Tipo Afiliado del paciente no corresponde a los Montos del Convenio. No se puede asignar este Convenio. Por favor verifique.");
											forma.getMensajesAlerta().add(mensaje);
										}
									}
								}
							}
							else
							{
									forma.setEstratosSocialesConvenio(new HashMap());
									forma.setTiposAfiliadoConvenio(new HashMap());
									forma.setPuedoGrabarConvenioAdicional(false);
									ElementoApResource mensaje = new ElementoApResource("errors.notEspecific");
									mensaje.agregarAtributo("El Tipo de rï¿½gimen de la Clasificaciï¿½n socio  econï¿½mica no corresponde con el Tipo de rï¿½gimen del Convenio. No se puede asignar este Convenio. Por favor verifique.");
									forma.getMensajesAlerta().add(mensaje);
									
							}
						}
						int natPaciente=Utilidades.convertirAEntero(forma.getContratosConvenio().get(0).get("naturalezapaciente")+"");
						if(natPaciente>0)
						{
							if(UtilidadValidacion.esNaturalezaValidaTipoRegimen(natPaciente,forma.getInfoIngreso("codigoTipoRegimen")+"")) 
							{

								forma.setInfoIngreso("codigoNaturaleza", natPaciente+"");
								Vector<InfoDatosString> naturalezaVector=new Vector<InfoDatosString>();
								for(int i=0;i<forma.getNaturalezasPaciente().size();i++)
								{
									if(Utilidades.convertirAEntero(forma.getNaturalezasPaciente().get(i).getCodigo()) ==natPaciente)
									{
										naturalezaVector.add(forma.getNaturalezasPaciente().get(i));
									}
								}
								if(ValoresPorDefecto.getPermitirModificarDatosUsuariosCapitados(usuario.getCodigoInstitucionInt()).equals(ConstantesBD.acronimoNo))
								{
									forma.setNaturalezasPaciente(naturalezaVector);
								}
								
								if(forma.getNaturalezasPaciente().size()<=0)
								{
									forma.setPuedoGrabarConvenioAdicional(false);
									ElementoApResource mensaje = new ElementoApResource("errors.notEspecific");
									mensaje.agregarAtributo("La Naturaleza Paciente del paciente no corresponde con los Montos Cobro del Convenio. No se puede asignar este Convenio. Por favor verifique.");
									forma.getMensajesAlerta().add(mensaje);
								}
								
							}
							else
							{
								forma.setPuedoGrabarConvenioAdicional(false);
								forma.setNaturalezasPaciente(new Vector<InfoDatosString>());
								ElementoApResource mensaje = new ElementoApResource("errors.notEspecific");
								mensaje.agregarAtributo("El Tipo de rï¿½gimen de la Naturaleza Paciente no corresponde con el Tipo de rï¿½gimen del Convenio. No se puede asignar este Convenio. Por favor verifique.");
								forma.getMensajesAlerta().add(mensaje);
							}
						}
					}
					
				}
				
				
				//Se cargan los montos de cobro
				//forma.setMontosCobroConvenio(UtilidadesFacturacion.obtenerMontosCobroXConvenio(con, mundoConvenio.getCodigo()+"", UtilidadFecha.conversionFormatoFechaABD(Cuenta.obtenerFechaVigenciaTopeCuenta(paciente.getCodigoCuenta()+""))));
				//Se cargan los tipos de cobertura
				forma.setCoberturaSalud(UtilidadesManejoPaciente.obtenerCoberturasSaludXTipoRegimen(con, forma.getInfoIngreso("codigoTipoRegimen").toString(), usuario.getCodigoInstitucionInt()));

				
				
				forma.setInfoIngreso("autorizacionIngreso",forma.getResponsables().get(forma.getIndice()).getNroAutorizacion());
				forma.setInfoIngreso("codigoMontoCobro",forma.getResponsables().get(forma.getIndice()).getMontoCobro());
				
				forma.setInfoIngreso("numeroCarnet",forma.getResponsables().get(forma.getIndice()).getNroCarnet());
				forma.setInfoIngreso("numeroPoliza",forma.getResponsables().get(forma.getIndice()).getNroPoliza());
				forma.setInfoIngreso("fechaAfiliacion",forma.getResponsables().get(forma.getIndice()).getFechaAfiliacion());
				forma.setInfoIngreso("semanasCotizacion",forma.getResponsables().get(forma.getIndice()).getSemanasCotizacion());
				forma.setInfoIngreso("mesesCotizacion",forma.getResponsables().get(forma.getIndice()).getMesesCotizacion());
				forma.setInfoIngreso("codigoRegistroAccidenteTransito",forma.getResponsables().get(forma.getIndice()).getCodigoRegistroAccidenteTransito());
				
				//Validacion colsanitas
				forma.setInfoIngreso("esConvenioColsanitas",  UtilidadesFacturacion.esConvenioColsanitas(con, mundoConvenio.getCodigo(), usuario.getCodigoInstitucionInt())?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
				forma.setInfoIngreso("numeroSolicitudVolante",forma.getResponsables().get(forma.getIndice()).getNumeroSolicitudVolante());
				
				ArrayList<DtoRequsitosPaciente> requisitos=forma.getResponsables().get(forma.getIndice()).getRequisitosPaciente();
				for(int a=0;a<requisitos.size();a++)
				{
					forma.setRequisitosPaciente("codigo_"+a,requisitos.get(a).getCodigo()+"");
					forma.setRequisitosPaciente("subcuenta_"+a,requisitos.get(a).getSubCuenta());
					forma.setRequisitosPaciente("descripcion_"+a,requisitos.get(a).getDescripcion()+"");
					forma.setRequisitosPaciente("tipo_"+a,requisitos.get(a).getTipo());
					forma.setRequisitosPaciente("cumplido_"+a,requisitos.get(a).isCumplido()?"S":"N");
					forma.setRequisitosPaciente("asignado_"+a,requisitos.get(a).isAsignado()?"S":"N");
					
				}
				forma.setRequisitosPaciente("numRegistros", requisitos.size()+"");
				
				///*************VALIDACIï¿½N MENSAJES ALERTA**************************************************
				//1) Validacion de la autorizacion de admision
				if(UtilidadTexto.getBoolean(forma.getInfoIngreso("esRequiereAutorizacionServicio").toString()))
				{
					ArrayList<DtoAutorizacion> autorizaciones = UtilidadesManejoPaciente.obtenerAutorizacionesAdmisionSubCuenta(con, Long.parseLong(forma.getInfoIngreso("responsable").toString()));
					
					//Si no hay estado quiere decir que no tiene autorizacion
					if(autorizaciones.size()==0)
					{
						ElementoApResource elemento = new ElementoApResource("errors.notEspecific");
						elemento.agregarAtributo("El convenio seleccionado Requiere autorizaciï¿½n servicios adicional al inicial y NO cuenta con solicitud de autorizaciï¿½n de admisiï¿½n.");
						forma.getMensajesAlerta().add(elemento);
					}
					else 
					{
						for(DtoAutorizacion autorizacion:autorizaciones)
						{
							if(!autorizacion.getDetalle().get(0).getEstadoSolDetAuto().equals(ConstantesIntegridadDominio.acronimoAutorizado))
							{
								ElementoApResource elemento = new ElementoApResource("errors.notEspecific");
								elemento.agregarAtributo("El convenio seleccionado tiene solicitud de autorizaciï¿½n de admisiï¿½n pero aï¿½n NO cuenta con respuesta de autorizaciï¿½n para "+(autorizacion.getIdCuenta().equals("")?"el asocio de cuenta":autorizacion.getNombreViaIngreso()));
								forma.getMensajesAlerta().add(elemento);
							}
						}
					}
				}
				//*******************************************************************************************
				//Se carga la informaciï¿½n de Verificaciones de Derechos
				forma.setVerificacion(mundo.consultarVeridicacionDerechos(con, Integer.valueOf(forma.getResponsables().get(forma.getIndice()).getSubCuenta())));
				
				
			}
			else
			{
				//Se carga la informaciï¿½n del convenio
				Convenio mundoConvenio = new Convenio();
				mundoConvenio.cargarResumen(con, forma.getCodigoConvenio());
					
				//********SE VERIFICA SI FUE SELECCIONADO CONVENIO**********************
				
				/*
				 * Se definio con margarita el 3 de abril de 2008 que se permite repetir el responsable.
				if(existeResponsable(forma,forma.getCodigoConvenio()))
				{
					ActionErrors errores = new ActionErrors();
					errores.add("Ya fue ingresado", new ActionMessage("errors.yaExiste","El convenio "+mundoConvenio.getNombre()));
					saveErrors(request, errores);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("paginaErroresActionErrorsSinCabezote");
				}
				*/
				
				CuentasDelegate cuentaDao=new CuentasDelegate();
				Cuentas cuenta=cuentaDao.findById(forma.getCuenta());
				
				
				forma.setInfoIngreso("cuenta",paciente.getCodigoCuenta());
				forma.setInfoIngreso("ingreso",forma.getCodigoIngreso());
				forma.setInfoIngreso("paciente",paciente.getCodigoPersona());
				forma.setInfoIngreso("responsable",ConstantesBD.codigoNuncaValido+"");
				forma.setInfoIngreso("codigoConvenio", mundoConvenio.getCodigo()+"");
				
				forma.setInfoIngreso("codigoContrato", forma.getResponsables().get(0).getContrato());
				
				forma.setInfoIngreso("codigoViaIngreso", cuenta.getViasIngreso().getCodigo());
				
				forma.setInfoIngreso("codigoTipoPaciente", cuenta.getTiposPaciente().getAcronimo());
				
				forma.setInfoIngreso("nombreConvenio", mundoConvenio.getNombre());
				forma.setInfoIngreso("esConvenioPyp", UtilidadTexto.getBoolean(mundoConvenio.getPyp())?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
				forma.setInfoIngreso("esConvenioCapitado", mundoConvenio.getTipoContrato()==ConstantesBD.codigoTipoContratoCapitado?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
				forma.setInfoIngreso("esConvenioPoliza", UtilidadTexto.getBoolean(mundoConvenio.getCheckInfoAdicCuenta())?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
				forma.setInfoIngreso("esConvenioSoat", esConvenioTipoConventioEventoCatTrans(mundoConvenio.getCodigo())?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
				forma.setInfoIngreso("esRequiereAutorizacionServicio", mundoConvenio.getRequiere_autorizacion_servicio());
				forma.setInfoIngreso("esReporteAtencionInicialUrgencias", mundoConvenio.getReporte_atencion_ini_urg());
				forma.setInfoIngreso("codigoRegistroAccidenteTransito", ConstantesBD.codigoNuncaValidoLong);

				
				forma.setInfoIngreso("valorUtilizadoSoat", "");
				
					
				forma.setInfoIngreso("esConvenioSoat",esConvenioTipoConventioEventoCatTrans(mundoConvenio.getCodigo())?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
				forma.setInfoIngreso("codigoTipoRegimen", mundoConvenio.getTipoRegimen());
				forma.setInfoIngreso("codigoTipoCobertura", "");
				forma.setInfoIngreso("nombreTipoRegimen", mundoConvenio.getDescripcionTipoRegimen());
				forma.setInfoIngreso("codigoNaturaleza", ConstantesBD.codigoNaturalezaPacientesNinguno+"");
				forma.setInfoIngreso("requiereCarnet", mundoConvenio.getRequiereNumeroCarnet());
				
				forma.setInfoIngreso("manejoComplejidad", mundoConvenio.getManejaComplejidad());
				forma.setInfoIngreso("codigoEstratoSocial", "");
				forma.setInfoIngreso("codigoTipoAfiliado", "");
				
				forma.setInfoIngreso("isConvenioManejaMontos", mundoConvenio.isConvenioManejaMontoCobro());
				
				//Validacion colsanitas
				forma.setInfoIngreso("esConvenioColsanitas",  UtilidadesFacturacion.esConvenioColsanitas(con, mundoConvenio.getCodigo(), usuario.getCodigoInstitucionInt())?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
				forma.setInfoIngreso("numeroSolicitudVolante","");
				
				
				/*
f(UtilidadTexto.getBoolean(forma.getInfoIngreso("esConvenioPoliza")+""))
				{
					forma.setInfoIngreso("apellidosPoliza", paciente.getPrimerApellido()+" "+paciente.getSegundoApellido());
					forma.setInfoIngreso("nombresPoliza", paciente.getPrimerNombre()+" "+paciente.getSegundoNombre());
					forma.setInfoIngreso("tipoIdPoliza", paciente.getCodigoTipoIdentificacionPersona());
					forma.setInfoIngreso("numeroIdPoliza", paciente.getNumeroIdentificacionPersona());
					forma.setInfoIngreso("direccionPoliza", paciente.getDireccion());
					forma.setInfoIngreso("telefonoPoliza", paciente.getTelefono());
					forma.setInfoIngreso("autorizacionPoliza", "");
					forma.setInfoIngreso("valorPoliza", "");
					forma.setInfoIngreso("saldoPoliza", "");
					forma.setInfoIngreso("fechaAutorizacionPoliza", UtilidadFecha.getFechaActual());
				}
				*/
					
				
				//Se cargan los contratos del convenio
				if(UtilidadTexto.getBoolean(forma.getInfoIngreso("esConvenioCapitado").toString()))
					forma.setContratosConvenio(UtilidadesManejoPaciente.obtenerContratosVigentesUsuarioCapitado(con, paciente.getCodigoPersona(), mundoConvenio.getCodigo()));
				else
					forma.setContratosConvenio(Utilidades.obtenerContratos(con, mundoConvenio.getCodigo(), false, true));
				
				if(forma.getContratosConvenio().size()==1){
					final int unicoContrato=0;
					verificarMontoCobro(forma,forma.getContratosConvenio().get(unicoContrato).get("codigo").toString());
				}
				
				//se cargan los estratos sociales del contrato
				forma.setEstratosSocialesConvenio(
						UtilidadesFacturacion.cargarEstratosSociales(con, usuario.getCodigoInstitucionInt(), 
								ConstantesBD.acronimoSi, forma.getInfoIngreso("codigoTipoRegimen")+"",
								mundoConvenio.getCodigo(),
								Utilidades.convertirAEntero(forma.getInfoIngreso("codigoViaIngreso")+""),
								Utilidades.capturarFechaBD()));
				
				//***********SE CONSULTAN LAS NATURALEZAS DEL PACIENTE ****************************************************************
				//forma.setNaturalezasPaciente(Utilidades.obtenerNaturalezasPaciente(con,forma.getInfoIngreso("codigoTipoRegimen")+"", mundoConvenio.getCodigo(),Utilidades.convertirAEntero(forma.getInfoIngreso("codigoViaIngreso")+"")));
				
				/*forma.setTiposAfiliadoConvenio(
						UtilidadesFacturacion.cargarTiposAfiliado(con, usuario.getCodigoInstitucionInt(),
								ConstantesBD.acronimoSi,mundoConvenio.getCodigo(),
								Utilidades.convertirAEntero(forma.getInfoIngreso("codigoViaIngreso")+""),
								Utilidades.capturarFechaBD()));*/
				
				int codigoEstrato = (forma.getEstratosSocialesConvenio().size() > 0 && forma.getEstratosSocialesConvenio().get("codigo_0") != null) ? 
						Utilidades.convertirAEntero(forma.getEstratosSocialesConvenio().get("codigo_0").toString()) : ConstantesBD.codigoNuncaValido ;
				
				forma.setTiposAfiliadoConvenio(
						UtilidadesFacturacion.cargarTiposAfiliadoXEstrato(
							con, usuario.getCodigoInstitucionInt(),ConstantesBD.acronimoSi, 
							mundoConvenio.getCodigo(),
							Utilidades.convertirAEntero(forma.getInfoIngreso("codigoViaIngreso")+""),
							codigoEstrato,
							Utilidades.capturarFechaBD()));

				String codigoTipoAfiliado = (forma.getTiposAfiliadoConvenio().size() > 0 && forma.getTiposAfiliadoConvenio().get("acronimo_0") != null) ? 
						forma.getTiposAfiliadoConvenio().get("acronimo_0").toString() : ConstantesBD.codigoNuncaValido+"";
					
				forma.setNaturalezasPaciente(
						Utilidades.obtenerNaturalezasPacienteXTipoAfiliadoEstrato(
								con,forma.getInfoIngreso("codigoTipoRegimen")+"", 
								mundoConvenio.getCodigo(),
								Utilidades.convertirAEntero(forma.getInfoIngreso("codigoViaIngreso")+""),
								codigoTipoAfiliado, 
								codigoEstrato,  
								Utilidades.capturarFechaBD()));

				forma.setInfoIngreso("codigoEstratoSocial", "");
				forma.setInfoIngreso("codigoTipoAfiliado", "");
				/*String codNaturalezas = UtilidadTexto.isEmpty(forma.getNaturalezasPaciente().get(0).getId()) || 
											forma.getNaturalezasPaciente().get(0).getId().equals("0") ? 
										ConstantesBD.codigoNuncaValido+"" : forma.getNaturalezasPaciente().get(0).getId();*/
				forma.setInfoIngreso("codigoNaturaleza", "");
				
				if(UtilidadTexto.getBoolean(forma.getInfoIngreso("esConvenioCapitado").toString()))
				{
					//si solo tiene un contrato vigente se debe postular la informacion
					if(forma.getContratosConvenio().size()==1)
					{
						int codClasi=Utilidades.convertirAEntero(forma.getContratosConvenio().get(0).get("codigoestratosocial")+"");
						if(codClasi>0)
						{
							String tipoRegimenCSE=Utilidades.obtenerTipoRegimenClasificacionSocioEconomica(con, codClasi);
							if(mundoConvenio.getTipoRegimen().equals(tipoRegimenCSE))
							{
								forma.setInfoIngreso("codigoEstratoSocial", codClasi);
								HashMap tempo=new HashMap();
								tempo.put("numRegistros", "0");
								HashMap tempo1=new HashMap();
								tempo1.put("numRegistros", "0");
								for(int i=0;i<Utilidades.convertirAEntero(forma.getEstratosSocialesConvenio().get("numRegistros")+"");i++)
								{
									if(Utilidades.convertirAEntero(forma.getEstratosSocialesConvenio().get("codigo_"+i)+"")==codClasi)
									{
										tempo.put("codigo_0", forma.getEstratosSocialesConvenio().get("codigo_"+i)+"");
										tempo.put("descripcion_0", forma.getEstratosSocialesConvenio().get("descripcion_"+i)+"");
										tempo.put("numRegistros", "1");
										break;
									}
								}
								String tipoAfil=forma.getContratosConvenio().get(0).get("tipoafiliado")+"";
								if(!UtilidadTexto.isEmpty(tipoAfil+""))
								{
									//{numRegistros=2, acronimo_0=B, nombre_1=Cotizante, nombre_0=Beneficiario, acronimo_1=C}
									forma.setInfoIngreso("codigoTipoAfiliado", tipoAfil);
									
									for(int i=0;i<Utilidades.convertirAEntero(forma.getTiposAfiliadoConvenio().get("numRegistros")+"");i++)
									{
										if((forma.getTiposAfiliadoConvenio().get("acronimo_"+i)+"").equals(tipoAfil+""))
										{
											tempo1.put("acronimo_0", forma.getTiposAfiliadoConvenio().get("acronimo_"+i)+"");
											tempo1.put("nombre_0", forma.getTiposAfiliadoConvenio().get("nombre_"+i)+"");
											tempo1.put("numRegistros", "1");
											break;
										}
									}
			
								}
								if(ValoresPorDefecto.getPermitirModificarDatosUsuariosCapitados(usuario.getCodigoInstitucionInt()).equals(ConstantesBD.acronimoNo))
								{
									forma.setEstratosSocialesConvenio(tempo);
									if(!UtilidadTexto.isEmpty(tipoAfil))
									{
										forma.setTiposAfiliadoConvenio(tempo1);
									}
								}	
								
								HashMap<String, Object>infoIngreos=forma.getInfoIngreso();
								if(infoIngreos.get("codigoContrato")!=null&&!((String)infoIngreos.get("codigoContrato")).trim().isEmpty()
										&&!((String)infoIngreos.get("codigoContrato")).trim().equals(ConstantesBD.codigoNuncaValido+"")){
									/**VERIFICAR CONTRATOS, si hay uno solo consultar validacion tipo cobro paciente para verificar la obligatoriedad los campos:
									 *  
									 * Clasificacion socioeconomica
									 * Tipo afiliado
									 * Naturaleza Paciente
									 * Porcentaje de cobertura
									 * Cuota Verificacion
									 * 
									 * MT 4836
									 * */
									DtoValidacionTipoCobroPaciente validacion=null;
									if(!forma.getContratosConvenio().isEmpty()&&forma.getContratosConvenio().size()==1){
										IValidacionTipoCobroPacienteServicio validacionTipoCobroPacienteServicio=FacturacionServicioFabrica.crearValidacionTipoCobroPacienteServicio();
										validacion=validacionTipoCobroPacienteServicio.validarTipoCobroPacienteServicioConvenioContrato(Integer.parseInt(infoIngreos.get("codigoContrato").toString()));
									}
									
									if(validacion!=null&&UtilidadTexto.getBoolean(validacion.getMostrarCalisificacion())&&
											Utilidades.convertirAEntero(forma.getEstratosSocialesConvenio().get("numRegistros")+"")<=0)
									{
										forma.setPuedoGrabarConvenioAdicional(false);
										ElementoApResource mensaje = new ElementoApResource("errors.notEspecific");
										mensaje.agregarAtributo("La Clasificaciï¿½n socio econï¿½mica del paciente no corresponde a los Montos del Convenio. No se puede asignar este Convenio. Por favor verifique.");
										forma.getMensajesAlerta().add(mensaje);
									}
									if(validacion!=null&&UtilidadTexto.getBoolean(validacion.getMostrarTipoAfiliado())&&
											!UtilidadTexto.isEmpty(tipoAfil))
									{
										if(Utilidades.convertirAEntero(forma.getTiposAfiliadoConvenio().get("numRegistros")+"")<=0)
										{
											forma.setPuedoGrabarConvenioAdicional(false);
											ElementoApResource mensaje = new ElementoApResource("errors.notEspecific");
											mensaje.agregarAtributo("El Tipo Afiliado del paciente no corresponde a los Montos del Convenio. No se puede asignar este Convenio. Por favor verifique.");
											forma.getMensajesAlerta().add(mensaje);
										}
									}
								}
							}
							else
							{
									forma.setEstratosSocialesConvenio(new HashMap());
									forma.setTiposAfiliadoConvenio(new HashMap());
									forma.setPuedoGrabarConvenioAdicional(false);
									ElementoApResource mensaje = new ElementoApResource("errors.notEspecific");
									mensaje.agregarAtributo("El Tipo de rï¿½gimen de la Clasificaciï¿½n socio  econï¿½mica no corresponde con el Tipo de rï¿½gimen del Convenio. No se puede asignar este Convenio. Por favor verifique.");
									forma.getMensajesAlerta().add(mensaje);
									
							}
						}
						int natPaciente=Utilidades.convertirAEntero(forma.getContratosConvenio().get(0).get("naturalezapaciente")+"");
						if(natPaciente>0)
						{
							if(UtilidadValidacion.esNaturalezaValidaTipoRegimen(natPaciente,forma.getInfoIngreso("codigoTipoRegimen")+"")) 
							{

								forma.setInfoIngreso("codigoNaturaleza", natPaciente+"");
								Vector<InfoDatosString> naturalezaVector=new Vector<InfoDatosString>();
								for(int i=0;i<forma.getNaturalezasPaciente().size();i++)
								{
									if(Utilidades.convertirAEntero(forma.getNaturalezasPaciente().get(i).getCodigo()) ==natPaciente)
									{
										naturalezaVector.add(forma.getNaturalezasPaciente().get(i));
									}
								}
								if(ValoresPorDefecto.getPermitirModificarDatosUsuariosCapitados(usuario.getCodigoInstitucionInt()).equals(ConstantesBD.acronimoNo))
								{
									forma.setNaturalezasPaciente(naturalezaVector);
								}
								
								if(forma.getNaturalezasPaciente().size()<=0)
								{
									forma.setPuedoGrabarConvenioAdicional(false);
									ElementoApResource mensaje = new ElementoApResource("errors.notEspecific");
									mensaje.agregarAtributo("La Naturaleza Paciente del paciente no corresponde con los Montos Cobro del Convenio. No se puede asignar este Convenio. Por favor verifique.");
									forma.getMensajesAlerta().add(mensaje);
								}
								
							}
							else
							{
								forma.setPuedoGrabarConvenioAdicional(false);
								forma.setNaturalezasPaciente(new Vector<InfoDatosString>());
								ElementoApResource mensaje = new ElementoApResource("errors.notEspecific");
								mensaje.agregarAtributo("El Tipo de rï¿½gimen de la Naturaleza Paciente no corresponde con el Tipo de rï¿½gimen del Convenio. No se puede asignar este Convenio. Por favor verifique.");
								forma.getMensajesAlerta().add(mensaje);
							}
						}
					}
					
				}
				
				
				//Se cargan los montos de cobro
				//forma.setMontosCobroConvenio(UtilidadesFacturacion.obtenerMontosCobroXConvenio(con, mundoConvenio.getCodigo()+"",UtilidadFecha.conversionFormatoFechaABD(Cuenta.obtenerFechaVigenciaTopeCuenta(paciente.getCodigoCuenta()+""))));

				//Se cargan los tipos de cobertura
				forma.setCoberturaSalud(UtilidadesManejoPaciente.obtenerCoberturasSaludXTipoRegimen(con, forma.getInfoIngreso("codigoTipoRegimen").toString(), usuario.getCodigoInstitucionInt()));
				
				
				HashMap historicoResponsable=mundo.obtenerHirtoricoResponsable(con,mundoConvenio.getCodigo(),forma.getCodigoIngreso());
				
				forma.setInfoIngreso("autorizacionIngreso",historicoResponsable.get("autorizacion"));
				forma.setInfoIngreso("codigoMontoCobro","");
				//forma.setInfoIngreso("codigoNaturaleza","");
				forma.setInfoIngreso("numeroCarnet","");
				forma.setInfoIngreso("numeroPoliza","");
				//forma.setInfoIngreso("numeroPoliza",forma.getResponsables().get(forma.getIndice()).getNroPoliza());
				forma.setInfoIngreso("fechaAfiliacion","");
				forma.setInfoIngreso("semanasCotizacion","");
				forma.setInfoIngreso("mesesCotizacion","");
				
				/******INFORMACION DE PARAMETROS DISTRIBUCION*********/
				forma.setInfoIngreso("porcentajeAutorizado",historicoResponsable.get("porcentaje"));
				forma.setInfoIngreso("montoAutorizado",historicoResponsable.get("monto"));


				
				ArrayList<DtoRequsitosPaciente> requisitos=UtilidadesHistoriaClinica.obtenerRequisitosPacienteConvenio(con,forma.getCodigoConvenio(),Integer.parseInt(forma.getInfoIngreso("codigoViaIngreso").toString()));
				for(int a=0;a<requisitos.size();a++)
				{
					forma.setRequisitosPaciente("codigo_"+a,requisitos.get(a).getCodigo()+"");
					forma.setRequisitosPaciente("subcuenta_"+a,requisitos.get(a).getSubCuenta());
					forma.setRequisitosPaciente("descripcion_"+a,requisitos.get(a).getDescripcion()+"");
					forma.setRequisitosPaciente("tipo_"+a,requisitos.get(a).getTipo());
					forma.setRequisitosPaciente("cumplido_"+a,historicoResponsable.containsKey(requisitos.get(a).getCodigo()+"")?(UtilidadTexto.getBoolean(historicoResponsable.get(requisitos.get(a).getCodigo()+"")+"")?"S":"N"):(requisitos.get(a).isCumplido()?"S":"N"));
					forma.setRequisitosPaciente("asignado_"+a,requisitos.get(a).isAsignado()?"S":"N");

				}
				forma.setRequisitosPaciente("numRegistros", requisitos.size()+"");
				
				//*************VALIDACIï¿½N MENSAJES ALERTA**************************************************
				//1) Validacion de la autorizacion de admision
				if(UtilidadTexto.getBoolean(forma.getInfoIngreso("esRequiereAutorizacionServicio").toString()))
				{
					ElementoApResource elemento = new ElementoApResource("errors.notEspecific");
					elemento.agregarAtributo("El convenio seleccionado Requiere autorizaciï¿½n servicios adicional al inicial y NO cuenta con solicitud de autorizaciï¿½n de admisiï¿½n.");
					forma.getMensajesAlerta().add(elemento);
				}
				//*******************************************************************************************
				
				//Se inicializan las fechas y hora para la secciï¿½n Verificaciï¿½n de Derechos
				String fechaActual=UtilidadFecha.getFechaActual(con);
				String horaActual=UtilidadFecha.getHoraActual(con);
				forma.setVerificacion("fechaSolicitud", fechaActual);
				forma.setVerificacion("horaSolicitud", horaActual);
				forma.setVerificacion("fechaVerificacion", fechaActual);
				forma.setVerificacion("horaVerificacion", horaActual);
				forma.setVerificacion("codigoEstado", "");
				forma.setVerificacion("codigoTipo", "");
				forma.setVerificacion("numero", "");
				forma.setVerificacion("personaSolicita", "");
				forma.setVerificacion("personaContactada", "");
			}
			
		} catch (Exception e) {
			Log4JManager.error("ERROR accionCargarInfoResponsable: ", e);
		}
		
		return mapping.findForward("infoResponsable");
	}
	
	/**
	 * Solucion MT 4836, se verifica si el convenio NO maneja montos
	 * y si el contrato seleccionado obliga o no al paciente a pagar la atencion 
	 * 
	 * @param ingresoPacienteForm
	 * @author jeilones
	 * @created 11/09/2012
	 */
	private void verificarMontoCobro(DistribucionCuentaForm ingresoPacienteForm,String codigoContratoSeleccionado) {
		HashMap<String, Object>contratoSeleccionado=null;
		for(HashMap<String, Object> contrato:ingresoPacienteForm.getContratosConvenio()){
			if(contrato.get("codigo")!=null
					&&ingresoPacienteForm.getInfoIngreso("codigoContrato")!=null
					&&contrato.get("codigo").toString().equals(codigoContratoSeleccionado)){
				contratoSeleccionado=contrato;
				break;
			}
		}

		List<HashMap<String, Object>>montosCobro=new ArrayList<HashMap<String,Object>>(0);
		
		if(ingresoPacienteForm.getInfoIngreso("isConvenioManejaMontos")!=null&&
				contratoSeleccionado!=null&&!(Boolean)ingresoPacienteForm.getInfoIngreso("isConvenioManejaMontos")){
			
			HashMap<String, Object>montoCobro=new HashMap<String, Object>(0);
			
			if(contratoSeleccionado.get("pacientepagaatencion")!=null&&UtilidadTexto.getBoolean(contratoSeleccionado.get("pacientepagaatencion").toString())){
				
				montoCobro.put("codigo", -2);
				montoCobro.put("porcentaje", "100");
				montoCobro.put("valor", "");
				
				montosCobro.add(montoCobro);
				
				//100
			}else{
				montoCobro.put("codigo", -3);
				montoCobro.put("porcentaje", "0");
				montoCobro.put("valor", "");
				
				montosCobro.add(montoCobro);
				//0
			}
			
		}
		
		ingresoPacienteForm.setMontosCobroConvenio((ArrayList<HashMap<String, Object>>) montosCobro);
	}

	/**
	 * 
	 * @param codigoConvenio
	 * @return
	 */
	private boolean esConvenioTipoConventioEventoCatTrans(int codigoConvenio) 
	{
		return Convenio.esConvenioTipoConventioEventoCatTrans(codigoConvenio);
	}

	/**
	 * 
	 * @param forma
	 * @param codigoConvenio
	 * @return
	 */
	private boolean existeResponsable(DistribucionCuentaForm forma, int codigoConvenio) 
	{
		for(int a=0;a<forma.getResponsables().size();a++)
		{
			if(codigoConvenio==forma.getResponsables().get(a).getConvenio().getCodigo()&&!UtilidadTexto.getBoolean(forma.getResponsables().get(a).getFacturado()))
			{
				return true;
			}
		}
		for(int a=0;a<forma.getResponsablesEliminados().size();a++)
		{
			if(codigoConvenio==forma.getResponsablesEliminados().get(a).getConvenio().getCodigo())
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param paciente
	 * @param usuario
	 * @param mapping
	 * @param request
	 * @return
	 */
	private ActionForward accionGuardarEncabezadoDistribucion(Connection con, DistribucionCuentaForm forma, DistribucionCuenta mundo, PersonaBasica paciente, UsuarioBasico usuario, ActionMapping mapping, HttpServletRequest request, HttpServletResponse response) throws IPSException
	{
		boolean backupExitoso = false;
		boolean transaccion = false;
		ActionErrors errores = new ActionErrors();
			
		try {
			
			//Manejo historicos de cargos eliminados en el proceso de liquidacion automatica
			backupExitoso = crearHistoricoCargosXLiquidacionDistribucion(forma, usuario, paciente, ConstantesIntegridadDominio.acronimoAutomatica, null, forma.getTipoDistribucion());
			
			if(backupExitoso){
				
			transaccion=UtilidadBD.iniciarTransaccion(con);
			
			transaccion=guardarEncabezadoDistribucion(con,forma,mundo,usuario);
	
			logger.info("===>Liquidar Automaticamente: "+forma.isLiquidarAutomaticamente());
			if(forma.isLiquidarAutomaticamente())
			{
				if(transaccion)
				{
					//transaccion=mundo.reacomodarPrioridades(con,forma.getCodigoIngreso());
					//se agregan los campos de PersonaBasica paciente, UsuarioBasico usuario debido a que se debe realizar la insercion en la tabla ax_pacien en la funcion  reacomodarPrioridades en el sqlBase 
					//--------------------------------------------------------------------------------------------------------------------
					//modificado anexo 779
					ResultadoBoolean resultadoBoolean=mundo.reacomodarPrioridades(con,forma.getCodigoIngreso(), paciente, usuario );
					logger.info("\n\n\n\n\n\n\n\n\n\n\n\n\n %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%\n***********************************************************************************************************\n"+resultadoBoolean.isTrue());
					transaccion=resultadoBoolean.isTrue();
					if (!transaccion)
					{
						forma.setMostrarMensaje(new ResultadoBoolean(true,resultadoBoolean.getDescripcion()));
						UtilidadBD.abortarTransaccion(con);
						UtilidadBD.closeConnection(con);
						return mapping.findForward("dummyDistribucion");
					}
					//------------------------------------------------------------------------------------------------------------------------
				
					if (transaccion)
						transaccion=mundo.limpiarParametrosDistribucion(con,forma.getCodigoIngreso());
				}
				
				//despues de empezar la autoliquidacion se inicializa la bandera
				forma.setLiquidarAutomaticamente(false);
				if(transaccion)
					transaccion=procesoLiquidacionAutomatica(con,forma,mundo,usuario,paciente);
				
				if(transaccion)
				{
					for(int a=0;a<forma.getResponsablesEliminados().size();a++)
					{
						transaccion=mundo.eliminarSubCuenta(con,forma.getResponsablesEliminados().get(a),forma.getResponsables().get(forma.getResponsables().size()-1).getSubCuentaDouble(),forma.getResponsables().get(forma.getResponsables().size()-1).getConvenio().getCodigo(),true);
						if(!transaccion)
							a=forma.getResponsablesEliminados().size();
					}
				}
				
				
				if(transaccion)
				{
					UtilidadBD.finalizarTransaccion(con);
					//Cambio incluido por la Tarea 52989
					forma.resetParamDistribucion();
					forma.setMostrarMensaje(new ResultadoBoolean(true,"Liquidaciï¿½n realizada exitosamente."));
					if(forma.getResponsablesEliminados().size()>0)
						forma.setMostrarMensaje(new ResultadoBoolean(true,"Liquidaciï¿½n realizada exitosamente.\nResponsable(s) eliminado(s) exitosamente."));
	
					this.cargarPacienteIngresoSession(con,forma,paciente,usuario,paciente.getCodigoPersona(),request);
				}
				else
				{
					UtilidadBD.abortarTransaccion(con);
					forma.setMostrarMensaje(new ResultadoBoolean(true,"No se pudo Liquidar la Distribuciï¿½n"));
					if(forma.getResponsablesEliminados().size()>0)
						forma.setMostrarMensaje(new ResultadoBoolean(true,"No se pudo Liquidar la Distribuciï¿½n. No se pudieron eliminar los responsables."));
				}
				
	
				UtilidadBD.closeConnection(con);
				return mapping.findForward("dummyDistribucion");
			}
			else
			{
				//Cambio incluido por la Tarea 52989
				//Una vez incluido el encabezado y modificado el Tipo de Distribucion 
				//eliminamos los parametros de la distribucion en la subcuenta
				if(transaccion)
				{
					transaccion=mundo.limpiarParametrosDistribucion(con,forma.getCodigoIngreso());
					forma.resetParamDistribucion();
				}
				if(transaccion)
				{
					UtilidadBD.finalizarTransaccion(con);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("dummyDistribucion");
				}
				else
				{
					this.cancelarIngresoProcesoDistribucion(con,  forma, response);
					UtilidadBD.abortarTransaccion(con);
					return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Paciente no Cargado", "errors.noSeGraboInformacion", true);
				}
			}
				} else {
					forma.setMostrarMensaje(new ResultadoBoolean(true," Proceso de Backup no se realizo con exito. No se pudo Liquidar la Distribución" ));
				}
		}
		catch(IPSException ipse){
			UtilidadBD.abortarTransaccion(con);
			forma.setMostrarMensaje(new ResultadoBoolean(true,"No se pudo Liquidar la Distribución "));
			Log4JManager.error(ipse.getMessage(), ipse);
			errores.add("", new ActionMessage(ipse.getErrorCode().toString()));
		}catch(Exception e){
			UtilidadBD.abortarTransaccion(con);
			forma.setMostrarMensaje(new ResultadoBoolean(true,"No se pudo Liquidar la Distribución - ERROR: "+ e.getMessage()));
			Log4JManager.error(e.getMessage(), e);
			errores.add("", new ActionMessage(KEY_ERROR_NO_ESPECIFIC, e.getMessage()));
		}

		if(!errores.isEmpty()){
			saveErrors(request, errores);
		}
		
		return mapping.findForward("dummyDistribucion");
	}

	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param usuario 
	 * @return
	 */
	private boolean guardarEncabezadoDistribucion(Connection con, DistribucionCuentaForm forma, DistribucionCuenta mundo, UsuarioBasico usuario) throws IPSException
	{
		boolean transaccion=true;
		
		try {
			//actualizar tipo complejidad de la cuenta.
			if(transaccion)
			{
				if(forma.getTipoComplejidad()!=Utilidades.obtenerTipoComplejidadCuenta(con,forma.getCuenta()).getCodigo())
				{
					transaccion=mundo.actualizarTipoComplejidadCuenta(con,forma.getTipoComplejidad(),forma.getCuenta());
				}
			}
			
			//actualizar/insertar datos encabezado distribucion.
			if(transaccion)
			{
				HashMap vo=new HashMap();
				vo.put("tipoDistribucion",forma.getTipoDistribucion());
				vo.put("usuarioModifica", usuario.getLoginUsuario());
				vo.put("fechaModifica",UtilidadFecha.getFechaActual());
				vo.put("horaModifica", UtilidadFecha.getHoraActual());
				vo.put("ingreso", forma.getCodigoIngreso());
				HashMap mapa = mundo.consultarEncabezadoUltimaDistribucion(con,forma.getCodigoIngreso());
				if(Utilidades.convertirAEntero(mapa.get("numRegistros")+"", false)>0){
					transaccion=mundo.actualizarEncabezadoDistribucion(con,vo);
				}
				else
				{
					transaccion=mundo.insertarEncabezadoDistribucion(con,vo);
				}
			}
			
			//actualizar prioridades.
			forma.setLiquidarAutomaticamente(false);
			for(int a=0;a<forma.getResponsables().size();a++)
			{
				if(transaccion)
				{
					if(forma.getResponsables().get(a).getNroPrioridad()!=Utilidades.obtenerPrioridadResponsabe(con,forma.getResponsables().get(a).getSubCuenta()))
					{
						transaccion=mundo.actualizarPrioridadResponsable(con,forma.getResponsables().get(a).getSubCuenta(),forma.getResponsables().get(a).getNroPrioridad());
						forma.setLiquidarAutomaticamente(true);
					}
				}
				else
				{
					a=forma.getResponsables().size();
				}
			}
			
			return transaccion;
		}
		catch (IPSException ipsme) {
			throw ipsme;
		}
		catch (Exception e) {
			Log4JManager.error(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
	}

	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param paciente
	 * @param usuario 
	 * @param mapping
	 * @param request 
	 * @return
	 */
	private ActionForward accionCargarInformacionGeneral(Connection con, DistribucionCuentaForm forma, DistribucionCuenta mundo, PersonaBasica paciente, UsuarioBasico usuario, ActionMapping mapping, HttpServletRequest request) throws IPSException 
	{
		forma.setResponsablesEliminados(new ArrayList<DtoSubCuentas>());
		int codigoPaciente=paciente.getCodigoPersona();
		forma.setCodigoIngreso(Utilidades.convertirAEntero(forma.getIngresos().get("idingreso_"+forma.getIndiceIngresoSeleccionado())+"",false));
		forma.setConsecutivoIngreso(forma.getIngresos().get("consecutivo_"+forma.getIndiceIngresoSeleccionado())+"");
		forma.setCuenta(Utilidades.convertirAEntero(forma.getIngresos().get("cuenta_"+forma.getIndiceIngresoSeleccionado())+"",false));
		InfoDatosInt tipoCom=Utilidades.obtenerTipoComplejidadCuenta(con,forma.getCuenta());
		forma.setTipoComplejidad(tipoCom.getCodigo());
		forma.setDescTipoComplejidad(tipoCom.getNombre());		
		forma.setUltimaViaIngreso(Utilidades.convertirAEntero(Utilidades.obtenerViaIngresoCuenta(con, forma.getCuenta()+"")));
		forma.setConvenioDefectoVia(ViasIngreso.obtenerConvenioDefecto(con,forma.getUltimaViaIngreso()));
		
		/*****************************************************************************************************************/
		ArrayList<String> aux = new ArrayList<String>();
        //se valida que el paciente es de entidad subcontratada
		//logger.info("\n VOY A ENTRAR A ENTIDAD  CONSULTAR!!!");
	    if (IngresoGeneral.esIngresoComoEntidadSubContratada(con, forma.getCodigoIngreso()).equals(ConstantesBD.acronimoSi))
	    {
	    //	logger.info("\n YA ENTRE A ENTIDAD CONSULTAR!!!");
	    	aux.add("Ingreso de paciente en entidad subcontratada "+EntidadesSubContratadas.getDescripcionEntidadSubXIngreso(con, paciente.getCodigoIngreso()+""));
	    	forma.setMensajes(aux);
	    	//logger.info("\n VOY A SALIR DE ENTIDAD CONSULTAR!!!");
	    }
	    /*****************************************************************************************************************/

		
		/**
		 * Validar concurrencia
		 * Si ya estï¿½ en proceso de distribucion, no debe dejar entrar
		 **/
		if(UtilidadValidacion.ingresoEstaEnProcesoDistribucion(con,forma.getCodigoIngreso(),usuario.getLoginUsuario()) )
		{
			return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error.facturacion.ingresoEnProcesoDistribucion", "error.facturacion.ingresoEnProcesoDistribucion", true);
		}
			
			//********************SE CARGA EL PACIENTE*******************************************
		if(!forma.isPacienteCargado() && !forma.getEstado().equals("cargarIngresoButton"))
		{
			this.cargarPacienteIngresoSession(con,forma,paciente,usuario,codigoPaciente,request);
			
			if(forma.isRegistrandoDistribucion())
				DistribucionCuenta.empezarProcesoDistribucionIngreso(con, forma.getCodigoIngreso(), usuario.getLoginUsuario());
			/*
			DistribucionCuenta.empezarProcesoDistribucion(con, forma.getCuenta(), usuario.getLoginUsuario());
			DistribucionCuenta.cuentaProcesoDistribucion(con,forma.getCuenta());
			*/
			//***********************************************************************************
		}
		
		///cargar los responsables del ingreso.
		forma.setResponsables(UtilidadesHistoriaClinica.obtenerResponsablesIngreso(con,forma.getCodigoIngreso(),true,new String[0],false, "" /*subCuenta*/,forma.getUltimaViaIngreso()));

		for(int a=0;a<forma.getResponsables().size();a++)
		{	///cargar los filtros de la distribucion
			this.accionCargarFiltroDistribucionResponsable(con,forma,mundo,a);
			//obtiene los valores a pagar del convenio y del paciente
			this.accionObtenerValoresConvenioPaciente(con,forma,mundo,a);
		}

		
		
		if(forma.getResponsables().size()>0)
		{
			validarVigenciasTopesContratos(con,forma);
		}

		//cargar infomacion Ultima Distribucion.
		this.cargarInfoUltimaDistribucion(forma,mundo.consultarEncabezadoUltimaDistribucion(con,forma.getCodigoIngreso()),usuario);
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}

	
	/**
	 * 
	 * @param con
	 * @param forma
	 * @param paciente
	 * @param usuario
	 * @param codigoPaciente
	 * @param request
	 */
	private void cargarPacienteIngresoSession(Connection con, DistribucionCuentaForm forma, PersonaBasica paciente, UsuarioBasico usuario, int codigoPaciente, HttpServletRequest request) 
	{
		ObservableBD observable = (ObservableBD)servlet.getServletContext().getAttribute("observable");
		try 
		{
			paciente.clean();
			paciente.cargar(con, codigoPaciente);
			
			//solo si el ingreso se encuentra en estado abierto, se puede cargar la informacion del ingreso en la sesion.
			//tarea id=25875
			//solo si el ingreso se encuentra en estado abierto o cerrado, se puede cargar la informacion del ingreso en la sesion.
			if((forma.getIngresos().get("estadoingreso_"+forma.getIndiceIngresoSeleccionado())+"").trim().equals(ConstantesIntegridadDominio.acronimoEstadoAbierto)||(forma.getIngresos().get("estadoingreso_"+forma.getIndiceIngresoSeleccionado())+"").trim().equals(ConstantesIntegridadDominio.acronimoEstadoCerrado))
				paciente.cargarPacienteXingreso(con, forma.getCodigoIngreso()+"", usuario.getCodigoInstitucion(), usuario.getCodigoCentroAtencion()+"");
		
		} 
		catch (Exception e) 
		{
			logger.info("Error cargando el paciente.: "+e);
		}
		observable.addObserver(paciente);
		request.getSession().removeAttribute("pacienteActivo");
		request.getSession().setAttribute("pacienteActivo",paciente);
		UtilidadSesion.notificarCambiosObserver(paciente.getCodigoPersona(),servlet.getServletContext());
		
		forma.setPacienteCargado(true);
	}


	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param indice
	 */
	private void accionCargarFiltroDistribucionResponsable(Connection con, DistribucionCuentaForm forma, DistribucionCuenta mundo, int indice) throws IPSException
	{
		try{
			HashMap tempo=mundo.consultarFiltroDistribucionResponsable(con,forma.getResponsables().get(indice).getSubCuenta());
			if(Utilidades.convertirAEntero(tempo.get("numRegistros")+"", false)>0)
			{
				forma.setFiltroDistribucion("subcuenta_"+indice,forma.getResponsables().get(indice).getSubCuenta());
				forma.setFiltroDistribucion("viaingreso_"+indice,tempo.get("viaingreso")+"");
				forma.setFiltroDistribucion("ccsol_"+indice,tempo.get("ccsol")+"");
				forma.setFiltroDistribucion("cceje_"+indice,tempo.get("cceje")+"");
				forma.setFiltroDistribucion("tipopaciente_"+indice,tempo.get("tipopaciente")+"");
				forma.setFiltroDistribucion("fechainicial_"+indice,tempo.get("fechainicial")+"");
				forma.setFiltroDistribucion("fechafinal_"+indice,tempo.get("fechafinal")+"");
				forma.setFiltroDistribucion("tiporegistro_"+indice,"BD");
			}
			else
			{
				forma.setFiltroDistribucion("subcuenta_"+indice,forma.getResponsables().get(indice).getSubCuenta());
				forma.setFiltroDistribucion("viaingreso_"+indice,"");
				forma.setFiltroDistribucion("ccsol_"+indice,"");
				forma.setFiltroDistribucion("cceje_"+indice,"");
				forma.setFiltroDistribucion("tipopaciente_"+indice,"");
				forma.setFiltroDistribucion("fechainicial_"+indice,"");
				forma.setFiltroDistribucion("fechafinal_"+indice,"");
				forma.setFiltroDistribucion("tiporegistro_"+indice,"MEM");
			}
		}
		catch (IPSException ipsme) {
			throw ipsme;
		}
		catch (Exception e) {
			Log4JManager.error(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
	}

	/**
	 * 
	 * @param forma
	 * @param encabezadoDistribucion
	 * @param usuario 
	 */
	private void cargarInfoUltimaDistribucion(DistribucionCuentaForm forma, HashMap<String, Object> encabezadoDistribucion, UsuarioBasico usuario) 
	{
		if(Utilidades.convertirAEntero(encabezadoDistribucion.get("numRegistros")+"", false)>0)
		{
			forma.setFechaHoraUltimaLiquidacion(UtilidadFecha.conversionFormatoFechaAAp(encabezadoDistribucion.get("fechaliquidacion")+"")+" "+encabezadoDistribucion.get("horaliquidacion")+"");
			forma.setTipoDistribucion(encabezadoDistribucion.get("tipodistribucion")+"");
			forma.setTipoDistribucionOriginal(forma.getTipoDistribucion());
			forma.setUsuarioUltimaLiquidacion(encabezadoDistribucion.get("usuarioliquidacion")+"");
			forma.setExisteDistribucionPrevia(true);
		}
		else
		{
			forma.setFechaHoraUltimaLiquidacion("");
			forma.setTipoDistribucion(ConstantesIntegridadDominio.acronimoTipoDistribucionCantidad);
			forma.setUsuarioUltimaLiquidacion("");
		}
	}
	
	/**
	 * 
	 * @param con
	 * @param idCuenta
	 * @param subCuenta
	 * @param forma
	 * @param response
	 * @return
	 */
	private boolean cancelarIngresoProcesoDistribucion(Connection con, DistribucionCuentaForm forma, HttpServletResponse response)
	{
		boolean resultado=DistribucionCuenta.cancelarIngresoProcesoDistribucion(con, forma.getCodigoIngreso());
		String paginaSiguiente=forma.getPaginaSiguiente();
		if(!paginaSiguiente.equals(""))
		{
			try
			{
				response.sendRedirect(paginaSiguiente);
				return false;
			}
			catch (IOException e)
			{
				logger.error("Error redireccionando a la pï¿½gina seleccionada "+e);
			}
		}
		return resultado;
	}

	
	/**
	 * 
	 * @param con
	 * @param paciente
	 * @param forma
	 * @param mapping
	 * @param response
	 * @return
	 */
	private ActionForward accionDistribucionCancelada(Connection con, PersonaBasica paciente, DistribucionCuentaForm forma, ActionMapping mapping, HttpServletResponse response)
	{
		boolean noRedirecciono=cancelarIngresoProcesoDistribucion(con, forma, response);
		UtilidadBD.closeConnection(con);
	    if(noRedirecciono)
	    {
	    	return mapping.findForward("distribucionCancelada");
	    }
	    else
	    {
	    	return null;
	    }
	}
	
	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param indice
	 */
	private void accionObtenerValoresConvenioPaciente (Connection con, DistribucionCuentaForm forma, DistribucionCuenta mundo, int indice) throws IPSException {
		DtoInfoCobroPaciente calculo=new DtoInfoCobroPaciente();				
		ICalculoValorCobrarPaciente calculoServicio= FacturacionServicioFabrica.crearCalculoValorCobrarPaciente();
		HashMap<String,Object> subCuentasMapTemp=subCuentasMapTemp= new HashMap<String,Object>();	
		Double total=Utilidades.obtenerTotalCargadoResponsable(con,forma.getResponsables().get(indice).getSubCuenta());

		calculo = calculoServicio.valorCobrarAPaciente(Utilidades.convertirAEntero(forma.getResponsables().get(indice).getSubCuenta()));
		
		if(calculo.isGenerado()){
			
			Double valorBrutoPac=calculo.getValorCargoPaciente().doubleValue();
			subCuentasMapTemp.put("valorpaciente", UtilidadTexto.formatearValores(valorBrutoPac+"","#"));
			logger.info("valor paciente ->"+subCuentasMapTemp.get("valorpaciente"));
			logger.info("valor total responsable->"+total.toString());
			subCuentasMapTemp.put("valorconvenio", total - Double.parseDouble(subCuentasMapTemp.get("valorpaciente").toString()));
			logger.info("valor convenio->"+subCuentasMapTemp.get("valorconvenio"));
		}else{
			
			subCuentasMapTemp.put("valorpaciente", "0");
			logger.info("valor paciente->"+subCuentasMapTemp.get("valorpaciente"));
			subCuentasMapTemp.put("valorconvenio", total.toString());
			logger.info("valor convenio->"+subCuentasMapTemp.get("valorconvenio"));
		}
		
		forma.getResponsables().get(indice).setValoresSubCuenta(subCuentasMapTemp);	
		
	}
	
	/**
	 * Metodo encargado de crear el backup para la Distribucion de Cuenta
	 * @param forma
	 * @param usuario
	 * @param paciente
	 * @param claseDistribucion
	 * @return
	 * @author hermorhu
	 */
	private boolean crearHistoricoCargosXLiquidacionDistribucion( DistribucionCuentaForm forma, UsuarioBasico usuario, PersonaBasica paciente, String claseDistribucion, List<Integer>numeroSolicitudes, String tipoDistribucion) throws IPSException {
		
		FacturacionFacade facturacionFacade = null;
		boolean procesoExitoso = false;
		InfoCreacionHistoricoCargosDto infoCreacionHistoricoCargosDto = null;
		
		try {
		
			facturacionFacade = new FacturacionFacade();
	
			//encabezado
			infoCreacionHistoricoCargosDto = new InfoCreacionHistoricoCargosDto();
			infoCreacionHistoricoCargosDto.setTipoDistribucion(tipoDistribucion);
			infoCreacionHistoricoCargosDto.setUsuario(usuario.getLoginUsuario());
			infoCreacionHistoricoCargosDto.setFecha(UtilidadFecha.getFechaActualTipoBD());
			infoCreacionHistoricoCargosDto.setHora(UtilidadFecha.getHoraActual());
			infoCreacionHistoricoCargosDto.setCodigoIngreso(forma.getCodigoIngreso());
			infoCreacionHistoricoCargosDto.setDescripcion(DESCRIPCION_BACKUP);
			infoCreacionHistoricoCargosDto.setCodigoPaciente(paciente.getCodigoPersona());
			infoCreacionHistoricoCargosDto.setClaseDistribucion(claseDistribucion);
			infoCreacionHistoricoCargosDto.setNumeroSolicitudes(numeroSolicitudes);
			
			procesoExitoso = facturacionFacade.guardarHistoricoCargosXLiquidacionDistribucion(infoCreacionHistoricoCargosDto);
		
		} catch(IPSException ipse){
			throw ipse;
		}catch (Exception e) {
			Log4JManager.error(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}

		return procesoExitoso;
	}
}
