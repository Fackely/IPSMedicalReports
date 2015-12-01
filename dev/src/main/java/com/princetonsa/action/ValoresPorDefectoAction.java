/*
 * @(#)ValoresPorDefectoAction.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_04
 *
 */
package com.princetonsa.action;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

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
import util.ConstantesValoresPorDefecto;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.actionform.ValoresPorDefectoForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dto.administracion.DtoFirmasValoresPorDefecto;
import com.princetonsa.dto.odontologia.DtoDetalleHallazgoProgramaServicio;
import com.princetonsa.dto.odontologia.DtoMotivoDescuento;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.glosas.ParametrosFirmasImpresionResp;
import com.princetonsa.mundo.odontologia.DetalleHallazgoProgramaServicio;
import com.princetonsa.mundo.odontologia.MotivosDescuentos;
import com.servinte.axioma.bl.administracion.facade.AdministracionFacade;
import com.servinte.axioma.cron.ProcesoActivacionInactivacionPasswordUsuariosCron;
import com.servinte.axioma.cron.ProcesoActivacionInactivacionUsuariosCron;
import com.servinte.axioma.fwk.exception.IPSException;
import com.servinte.axioma.mundo.fabrica.facturacion.FacturacionFabricaMundo;
import com.servinte.axioma.mundo.fabrica.odontologia.manejopaciente.ManejoPacienteFabricaMundo;
import com.servinte.axioma.mundo.interfaz.facturacion.IEsquemasTarifariosMundo;
import com.servinte.axioma.mundo.interfaz.manejoPaciente.ITiposPacienteMundo;
import com.servinte.axioma.mundo.interfaz.manejoPaciente.IViasIngresoMundo;
import com.servinte.axioma.persistencia.UtilidadTransaccion;

/**
 * @author Juan David Ramirez Lopez
 * @version 1.0 18/08/2004
 * 
 *          Princeton S.A.
 */
public class ValoresPorDefectoAction extends Action {
	/**
	 * Manejo de estados para el flujo de la funcionalidad
	 */
	private String estado = "";

	/**
	 * Manejador de logs de la clase
	 */
	private Logger logger = Logger.getLogger(ValoresPorDefectoAction.class);

	/**
	 * Mï¿½todo encargado de el flujo y control de la funcionalidad
	 * 
	 * @see org.apache.struts.action.Action#execute(ActionMapping, ActionForm,
	 *      HttpServletRequest, HttpServletResponse)
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		ValoresPorDefectoForm forma;
		Connection con =null;
		try{
			if (form instanceof ValoresPorDefectoForm) {
				UsuarioBasico usuario = (UsuarioBasico) request.getSession()
				.getAttribute("usuarioBasico");
				forma = (ValoresPorDefectoForm) form;
				estado = forma.getEstado();

				logger.warn("En ValoresPorDefectoAction El Estado Es [" + estado
						+ "]\n\n ");

				if (estado != null && !estado.equals("")) {
					if (estado.equals("empezar")) {
						// try
						{
							forma.reset();

							llenarDatosForma(forma);

							try {
								HashMap<String, Object> existeFacturasVarias = new HashMap<String, Object>();
								con = DaoFactory.getDaoFactory(
										System.getProperty("TIPOBD"))
										.getConnection();
								existeFacturasVarias = ValoresPorDefecto
								.consultarFacturasVarias(con);
								logger.info("===> Existe Facturas Varias ??? "
										+ existeFacturasVarias);
								forma.setExisteFacturaVaria(existeFacturasVarias);
							} catch (Exception e) {
								logger
								.error("Error consultando la existencia de registros en facturas_varias "
										+ e);
							}

							// Connection con =
							// DaoFactory.getDaoFactory(System.getProperty
							// ("TIPOBD")).getConnection();
							/*
							 * ValoresPorDefecto.setModulo(forma.getModulo());
							 * ValoresPorDefecto.cargarValoresIniciales(con);
							 * llenarFormaLargo(forma, usuario);
							 */
							// util.UtilidadBD.closeConnection(con);
							return mapping.findForward("principal");
						}
						/*
						 * catch(SQLException e) {logger.error(
						 * "Error copiando los valores por defecto al form "+e); }
						 */
					}
					if (estado.equals("generarFiltroModulo")) {
						try {
							con = DaoFactory.getDaoFactory(
									System.getProperty("TIPOBD")).getConnection();
							ValoresPorDefecto.setModulo(forma.getModulo());
							forma.setMapaEtiquetas(ValoresPorDefecto
									.cargarEtiquetas(con));

							//Se agrega cambio por versión 1.3 del CU 63 parámetros generales						
							realizarValidacionesParametros(forma);

							Utilidades.imprimirMapa(forma.getMapaEtiquetas());
							ValoresPorDefecto.cargarValoresIniciales(con);

							// Cargar Convenios Para su utilizaciÃ³n en algunos parametros
							forma.setConvenios(Utilidades.obtenerConveniosContrato(con, "","", false, UtilidadFecha.getFechaActual(), false));

							// Cargar Motivos de atencion odontológica
							forma.setMotivosAtencionOdontologica(Utilidades.obtenerMotivosAtencionOdontologica(con, 1));

							llenarFormaLargo(forma, usuario);

							if(forma.getModulo()==ConstantesBD.codigoModuloManejoPaciente)
							{
								forma.setEscalas(Utilidades.consultarEscalasParam(con, "", ConstantesBD.acronimoSi));
								if(forma.getEntidadSubcontratadaCentrosCostoAutorizacionCapitacionSubcontratada() != null
										&& !forma.getEntidadSubcontratadaCentrosCostoAutorizacionCapitacionSubcontratada().trim().isEmpty()){
									String[] datosEntidad=forma.getEntidadSubcontratadaCentrosCostoAutorizacionCapitacionSubcontratada().split("-");
									try{
										int codigoEntidad=Integer.valueOf(datosEntidad[0]);
										AdministracionFacade administracionFacade= new AdministracionFacade();
										if(administracionFacade.existeParametrizacionEntidadSubcontratadaPorCentroCosto(codigoEntidad)){
											forma.setDesactivarEntidadSubPorCentroCosto(true);
										}
										else{
											forma.setDesactivarEntidadSubPorCentroCosto(false);
										}										 
									}
									catch(NumberFormatException nfe){
										Log4JManager.error(nfe);
									}
									catch (IPSException ipse) {
										Log4JManager.error(ipse);
									}
									catch (Exception e) {
										Log4JManager.error(e);
									}
								}
							}

							util.UtilidadBD.closeConnection(con);
							return mapping.findForward("principal");
						} catch (SQLException e) {
							logger
							.error("Error copiando los valores por defecto al form "
									+ e);
						}

					}
					if (estado.equals("consultar")) {
						forma.reset();
						try {
							// Connection con =
							// DaoFactory.getDaoFactory(System.getProperty
							// ("TIPOBD")).getConnection();
							// ValoresPorDefecto.cargarValoresIniciales(con);
							// llenarFormaLargo(forma, usuario);
							// util.UtilidadBD.closeConnection(con);
							return mapping.findForward("consultar");
						} catch (Exception e) {
							logger
							.error("Error copiando los valores por defecto al form "
									+ e);
						}
					}
					if (estado.equals("generarConsulta")) {
						try {
							con = DaoFactory.getDaoFactory(
									System.getProperty("TIPOBD")).getConnection();
							ValoresPorDefecto.setModulo(forma.getModulo());
							forma.setMapaEtiquetas(ValoresPorDefecto.cargarResumen(
									con, usuario.getCodigoInstitucionInt()));

							//Se agrega cambio por versión 1.3 del CU 63 parámetros generales						
							realizarValidacionesParametros(forma);

							forma.setCentroCostoTerceros(ValoresPorDefecto
									.cargarResumenCentrosCostoTerceros(con));
							forma.setClasesInventariosPaqMatQx(ValoresPorDefecto
									.cargarResumenClasesInventariosPaqMatQx(con));
							forma.setHorasReproceso(ValoresPorDefecto
									.cargarResumenHorasReproceso(con));

							forma.setServiciosManejoTransPrimario(ValoresPorDefecto.cargarResumenServiciosManejoTransPrimario(usuario.getCodigoInstitucionInt(), con));
							forma.setServiciosManejoTransSecundario(ValoresPorDefecto.cargarResumenServiciosManejoTransSecundario(usuario.getCodigoInstitucionInt(), con));

							//Anexo 958
							forma.setListadoConceptos(Utilidades.obtenerConceptosIngresoTesoreria(ConstantesBD.codigoTipoIngresoTesoreriaOtrasCxC, ConstantesIntegridadDominio.acronimoFactura));
							//Tarea 141287 - Cargo lso convenios para mostrarlos en la consulta
							forma.setConveniosAMostrarPresupuestoOdo(ValoresPorDefecto.getConveniosAMostrarPresupuestoOdo());

							DtoMotivoDescuento dtoMotivos=new DtoMotivoDescuento();
							dtoMotivos.setTipo(ConstantesIntegridadDominio.acronimoEstadoAnulado);
							forma.setListMotivosDescuentos(MotivosDescuentos.cargar(dtoMotivos));

							forma.setEstado("consultar");
							util.UtilidadBD.closeConnection(con);
							return mapping.findForward("consultar");
						} catch (Exception e) {
							logger
							.error("Error copiando los valores por defecto al form "
									+ e);
						}
					}
					if (estado.equals("guardar")) {
						try {
							con = DaoFactory.getDaoFactory(
									System.getProperty("TIPOBD")).getConnection();
							ValoresPorDefecto.setModulo(forma.getModulo());
							if(forma.getInstitucionManejaMultasPorIncumplimiento().equals(ConstantesBD.acronimoNo))
							{
								forma.setBloqueaCitasReservaAsignReprogPorIncump("");
								forma.setBloqueaAtencionCitasPorIncump("");
								forma.setFechaInicioControlMultasIncumplimientoCitas("");
								forma.setValorMultaPorIncumplimientoCitas("");
							}
							guardarValores(con, forma, usuario.getLoginUsuario(),
									Integer
									.parseInt(usuario
											.getCodigoInstitucion()));
							forma.reset();
							forma.setMapaEtiquetas(ValoresPorDefecto.cargarResumen(
									con, usuario.getCodigoInstitucionInt()));

							//Se agrega cambio por versión 1.3 del CU 63 parámetros generales						
							realizarValidacionesParametros(forma);

							forma.setCentroCostoTerceros(ValoresPorDefecto
									.cargarResumenCentrosCostoTerceros(con));
							forma.setClasesInventariosPaqMatQx(ValoresPorDefecto
									.cargarResumenClasesInventariosPaqMatQx(con));
							forma.setHorasReproceso(ValoresPorDefecto
									.cargarResumenHorasReproceso(con));

							forma.setServiciosManejoTransPrimario(ValoresPorDefecto.cargarResumenServiciosManejoTransPrimario(usuario.getCodigoInstitucionInt(), con));
							forma.setServiciosManejoTransSecundario(ValoresPorDefecto.cargarResumenServiciosManejoTransSecundario(usuario.getCodigoInstitucionInt(), con));

							// llenarFormaLargo(forma, usuario);

							//Anexo 958
							forma.setListadoConceptos(Utilidades.obtenerConceptosIngresoTesoreria(ConstantesBD.codigoTipoIngresoTesoreriaOtrasCxC, ConstantesIntegridadDominio.acronimoFactura));
							//Tarea 141287 - Cargo lso convenios para mostrarlos en la consulta
							forma.setConveniosAMostrarPresupuestoOdo(ValoresPorDefecto.getConveniosAMostrarPresupuestoOdo());

							DtoMotivoDescuento dtoMotivos=new DtoMotivoDescuento();
							dtoMotivos.setTipo(ConstantesIntegridadDominio.acronimoEstadoAnulado);
							forma.setListMotivosDescuentos(MotivosDescuentos.cargar(dtoMotivos));

							forma.setEstado("empezar");
							util.UtilidadBD.closeConnection(con);
							return mapping.findForward("resumen");
						} catch (SQLException e) {
							logger.error("Error abriendo la conexiï¿½n " + e);
							request.setAttribute("codigoDescripcionError",
							"errors.problemasBd");
							return mapping.findForward("paginaError");
						}
					}
					// estado usado para la consulta de barrio al seleccionar una
					// ciudad de residencia
					if (estado.equals("consultaBarrio")) {
						try {
							con = DaoFactory.getDaoFactory(
									System.getProperty("TIPOBD")).getConnection();
							forma.setEstado("empezar");
							forma.setBarrioResidencia("");
							util.UtilidadBD.closeConnection(con);
							return mapping.findForward("principal");
						} catch (SQLException e) {
							logger.error("Error abriendo la conexiï¿½n " + e);
							request.setAttribute("codigoDescripcionError",
							"errors.problemasBd");
							return mapping.findForward("paginaError");
						}
					}

					if (estado.equals("consultaCiudadResidencia")) {
						forma.setEstado("empezar");
						forma.setCiudadVivienda(" - - ");
						return mapping.findForward("principal");
					}

					if (estado.equals("consultaCiudadNacimiento")) {
						forma.setEstado("empezar");
						forma.setCiudadNacimiento(" - - ");
						return mapping.findForward("principal");
					}

					if (estado.equals("adicionarCentroCostoTercero")) {
						// no se habre ni cierra conexion
						int numRegistros = Integer.parseInt(forma
								.getCentroCostoTerceros("numRegistros").toString());
						forma.setCentroCostoTerceros("centrocosto_" + numRegistros,
								forma.getCentroCostoTerceros("valorAdicionado"));
						forma.setCentroCostoTerceros("nombrecentrocosto_"
								+ numRegistros, "");
						forma.setCentroCostoTerceros("institucion_" + numRegistros,
								usuario.getCodigoInstitucion());
						forma
						.setCentroCostoTerceros("estabd_" + numRegistros,
						"no");
						forma.setCentroCostoTerceros("eseliminada_" + numRegistros,
						"no");
						forma.setCentroCostoTerceros("numRegistros",
								(numRegistros + 1) + "");
						forma.setCentroCostoTerceros("valorAdicionado", "");
						forma.setEstado("generarFiltroModulo");
						return mapping.findForward("principal");
					}

					if (estado.equals("adicionarClaseInventario")) {
						// no se habre ni cierra conexion
						int numRegistros = Utilidades.convertirAEntero(forma
								.getClasesInventariosPaqMatQx("numRegistros")
								+ "");
						forma.setClasesInventariosPaqMatQx("codigoclase_"
								+ numRegistros, forma
								.getCentroCostoTerceros("valorAdicionado"));
						forma.setClasesInventariosPaqMatQx("nombreclaseinventario_"
								+ numRegistros, "");
						forma.setClasesInventariosPaqMatQx("institucion_"
								+ numRegistros, usuario.getCodigoInstitucion());
						forma.setClasesInventariosPaqMatQx(
								"estabd_" + numRegistros, ConstantesBD.acronimoNo);
						forma.setClasesInventariosPaqMatQx("eseliminada_"
								+ numRegistros, ConstantesBD.acronimoNo);
						forma.setClasesInventariosPaqMatQx("fechamodifica_"
								+ numRegistros, UtilidadFecha.getFechaActual());
						forma.setClasesInventariosPaqMatQx("horamodifica_"
								+ numRegistros, UtilidadFecha.getHoraActual());
						forma.setClasesInventariosPaqMatQx("usuariomodifica_"
								+ numRegistros, usuario.getLoginUsuario());
						forma.setClasesInventariosPaqMatQx("numRegistros",
								(numRegistros + 1) + "");
						forma.setClasesInventariosPaqMatQx("valorAdicionado", "");
						forma.setEstado("generarFiltroModulo");
						return mapping.findForward("principal");
					}

					if (estado.equals("adicionarHoraReproceso")) {
						int numRegistros = Integer.parseInt(forma
								.getHorasReproceso("numRegistros").toString());

						if (forma.getHorasReproceso("valorAdicionadoViaIngreso")
								.toString().equals("todos")) {
							// primero se eliminan todos los de esa institucion
							for (int w = 0; w < numRegistros; w++) {
								if (forma.getHorasReproceso("eseliminadahora_" + w)
										.toString().equals("no")) {
									if (forma.getHorasReproceso("institucion_" + w)
											.toString().equals(
													usuario.getCodigoInstitucion())) {
										forma.setHorasReproceso("eseliminadahora_"
												+ w, "si");
									}
								}
							}
							// luego se adicionan todas las vias de ingreso con esa
							// hora
							HashMap viasIngreso = (HashMap) Utilidades
							.obtenerViasIngreso(false);
							for (int w = 0; w < Integer.parseInt(viasIngreso.get(
							"numRegistros").toString()); w++) {
								forma.setHorasReproceso("codigoviaingreso_"
										+ numRegistros, viasIngreso.get("codigo_"
												+ w));
								forma.setHorasReproceso("nombreviaingreso_"
										+ numRegistros, viasIngreso.get("nombre_"
												+ w));
								forma
								.setHorasReproceso(
										"hora_" + numRegistros,
										forma
										.getHorasReproceso("valorAdicionadoHora"));
								forma.setHorasReproceso("institucion_"
										+ numRegistros, usuario
										.getCodigoInstitucion());
								forma.setHorasReproceso("estabd_" + numRegistros,
								"no");
								forma.setHorasReproceso("eseliminadahora_"
										+ numRegistros, "no");
								numRegistros++;
								forma.setHorasReproceso("numRegistros",
										numRegistros + "");
							}
							forma
							.setHorasReproceso("valorAdicionadoViaIngreso",
							"");
							forma.setHorasReproceso("valorAdicionadoHora", "");
						} else {
							// no se habre ni cierra conexion
							forma
							.setHorasReproceso(
									"codigoviaingreso_" + numRegistros,
									forma
									.getHorasReproceso("valorAdicionadoViaIngreso"));
							forma.setHorasReproceso("nombreviaingreso_"
									+ numRegistros, "");
							forma.setHorasReproceso("hora_" + numRegistros, forma
									.getHorasReproceso("valorAdicionadoHora"));
							forma.setHorasReproceso("institucion_" + numRegistros,
									usuario.getCodigoInstitucion());
							forma.setHorasReproceso("estabd_" + numRegistros, "no");
							forma.setHorasReproceso("eseliminadahora_"
									+ numRegistros, "no");
							forma.setHorasReproceso("numRegistros",
									(numRegistros + 1) + "");
							forma
							.setHorasReproceso("valorAdicionadoViaIngreso",
							"");
							forma.setHorasReproceso("valorAdicionadoHora", "");
						}
						forma.setEstado("generarFiltroModulo");
						return mapping.findForward("principal");
					}

					if(estado.equals("agregarConvenioPresupuestoOdo")){
						if(!forma.getConveniosAMostrarXDefectoPresupuestoOdo().equals("")){

							String codigoConvenio=forma.getConveniosAMostrarXDefectoPresupuestoOdo().split(ConstantesBD.separadorSplit)[0];
							String descConvenio=forma.getConveniosAMostrarXDefectoPresupuestoOdo().split(ConstantesBD.separadorSplit)[1];
							String codigoContrato=forma.getConveniosAMostrarXDefectoPresupuestoOdo().split(ConstantesBD.separadorSplit)[2];
							String numeroContrato=forma.getConveniosAMostrarXDefectoPresupuestoOdo().split(ConstantesBD.separadorSplit)[3];
							ActionErrors errores = new ActionErrors();

							boolean existeConvenio = false;
							int numConveniosValidos = 0;
							for (int i=0; i<forma.getConveniosAMostrarPresupuestoOdo().size(); i++) {
								HashMap convenioExistente = forma.getConveniosAMostrarPresupuestoOdo().get(i);
								if(convenioExistente.get("codigoConvenio").toString().equals(codigoConvenio) && convenioExistente.get("codigoContrato").toString().equals(codigoContrato) && convenioExistente.get("eliminado").toString().equals(ConstantesBD.acronimoNo))
									existeConvenio = true;
								if(convenioExistente.get("eliminado").toString().equals(ConstantesBD.acronimoNo))
									numConveniosValidos++;
							}

							if(!existeConvenio && numConveniosValidos<3){
								HashMap convenioNuevo = new HashMap<String, Object>();
								convenioNuevo.put("codigoConvenio", codigoConvenio);
								convenioNuevo.put("descripcionConvenio", descConvenio);
								convenioNuevo.put("codigoContrato", codigoContrato);
								convenioNuevo.put("numeroContrato", numeroContrato);
								convenioNuevo.put("nuevo", ConstantesBD.acronimoSi);
								convenioNuevo.put("eliminado", ConstantesBD.acronimoNo);
								forma.getConveniosAMostrarPresupuestoOdo().add(convenioNuevo);
							}
							if(numConveniosValidos>=3)
							{
								errores.add("",	new ActionMessage("errors.notEspecific","No pueden existir más de 3 Convenios parametrizados."));
								saveErrors(request, errores);
							}
							if (existeConvenio)
							{
								errores.add("",	new ActionMessage("errors.notEspecific","El convenio ya se encuentra parametrizado."));
								saveErrors(request, errores);
							}
						}
						forma.setConveniosAMostrarXDefectoPresupuestoOdo("");
						return mapping.findForward("principal");
					}

					if(estado.equals("eliminarConvenioPresupuestoOdo")){

						int posConvenioAEliminar = Utilidades.convertirAEntero(forma.getConveniosAMostrarXDefectoPresupuestoOdo());
						forma.getConveniosAMostrarPresupuestoOdo().get(posConvenioAEliminar).put("eliminado", ConstantesBD.acronimoSi);

						forma.setConveniosAMostrarXDefectoPresupuestoOdo("");
						return mapping.findForward("principal");
					}

					if (estado.equals("continuar")) {
						forma.setEstado("generarFiltroModulo");
						return mapping.findForward("principal");
					}


					//Anexo 992
					if (estado.equals("paramFirmas"))
					{
						forma.getDtoFirmas().resetConValorXDefecto();
						forma.getMensaje().setDescripcion("");

						//Seteo el nombre del valor x defecto y la institucion 
						forma.getDtoFirmas().setValorPorDefecto(forma.getValorDefecto());
						forma.getDtoFirmas().setInstitucion(usuario.getCodigoInstitucionInt());

						//Aca se compara para determinar de que tipo es el valor x defecto
						if (forma.getValorDefecto().equals(ConstantesValoresPorDefecto.nombreValoresDefectoImprimirFirmasImpresionCCCapitacion))
							forma.getDtoFirmas().setTipo(ConstantesIntegridadDominio.acronimoTipoFirmaValoresPorDefectoCapitacion);
						if (forma.getValorDefecto().equals(ConstantesValoresPorDefecto.nombreValoresDefectoImprimirFirmasEnImpresionCC))
							forma.getDtoFirmas().setTipo(ConstantesIntegridadDominio.acronimoTipoFirmaValoresPorDefectoCartera);

						//Consulto las firmas cforrespondients al valor por defecto
						forma.setListadoFirmas(ValoresPorDefecto.consultarFirmasValoresPorDefecto(forma.getDtoFirmas()));




						return mapping.findForward("firmas");
					}

					if (estado.equals("guardarFirmas"))
					{
						ActionErrors errores = new ActionErrors();

						if (forma.getDtoFirmas().getUsuario().equals(""))
						{
							forma.getMensaje().setDescripcion("");
							errores.add("",new ActionMessage("errors.required","El usuario "));
							saveErrors(request, errores);
						}
						if (forma.getDtoFirmas().getCargo().equals(""))
						{
							forma.getMensaje().setDescripcion("");
							errores.add("",new ActionMessage("errors.required","El cargo "));
							saveErrors(request, errores);
						}
						if (forma.getListadoFirmas().size()==4)
						{
							forma.getMensaje().setDescripcion("");
							errores.add("",new ActionMessage("errors.notEspecific","No pueden haber más de 4 firmas para éste parámetro "));
							saveErrors(request, errores);
						}


						if (errores.isEmpty())
						{
							//Inserto los elementos faltantes en el dto
							forma.getDtoFirmas().setUsuarioModifica(usuario.getLoginUsuario());
							forma.getDtoFirmas().setFechaModifica(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
							forma.getDtoFirmas().setHoraModifica(UtilidadFecha.getHoraActual());


							forma.setMensaje(ValoresPorDefecto.insertarFirmasValoresPorDefecto(forma.getDtoFirmas()));

							forma.setListadoFirmas(ValoresPorDefecto.consultarFirmasValoresPorDefecto(forma.getDtoFirmas()));

							forma.getDtoFirmas().resetConValorXDefecto();	
						}

						return mapping.findForward("firmas");
					}
					if (estado.equals("eliminarFirmas"))
					{
						DtoFirmasValoresPorDefecto dto=new DtoFirmasValoresPorDefecto();
						dto=forma.getListadoFirmas().get(forma.getPosFirma());

						forma.setMensaje(ValoresPorDefecto.eliminarFirma(dto));

						forma.setListadoFirmas(ValoresPorDefecto.consultarFirmasValoresPorDefecto(forma.getDtoFirmas()));

						return mapping.findForward("firmas");
					}
					//Fin anexo 992

					// ***************** Inicio / AreaAperturaCuentaAutoPYP
					if (estado.equals("paramAreaAperturaCuentaAutoPYP"))
					{
						logger.info("Usuario: "+usuario.getCodigoInstitucionInt());
						forma.getAreaAperturaCuentaAutoPYP().setArea(ConstantesBD.codigoNuncaValido);
						forma.setCentrosAtencion(Utilidades.obtenerCentrosAtencionInactivos(usuario.getCodigoInstitucionInt(), false)); 
						forma.setListadoAreasAperturaCuentaAutoPYP(ValoresPorDefecto.consultarAreasAperturaCuentaAutoPYP(usuario.getCodigoInstitucionInt(), ConstantesBD.codigoNuncaValido));
						Utilidades.imprimirMapa(forma.getCentrosAtencion());
						return mapping.findForward("areaAperturaCuentaAuto");
					}
					if (estado.equals("filtrarAreaAperturaCuentaAutoPYP"))
					{
						forma.setCentrosCosto(Utilidades.obtenerCentrosCosto(usuario.getCodigoInstitucionInt(), "", true, forma.getAreaAperturaCuentaAutoPYP().getCentroAtencion(), true)); 
						return mapping.findForward("areaAperturaCuentaAuto");
					}
					if (estado.equals("guardarAreaAperturaCuentaAutoPYP"))
					{
						//Verificar si el centro de Atención ya se encuentra parametrizado
						boolean existeCentroAtencion=false;
						for(int i=0; i<forma.getListadoAreasAperturaCuentaAutoPYP().size();i++){
							if(forma.getListadoAreasAperturaCuentaAutoPYP().get(i).getCentroAtencion()==forma.getAreaAperturaCuentaAutoPYP().getCentroAtencion())
								existeCentroAtencion = true;
						}

						if(!existeCentroAtencion){
							forma.getAreaAperturaCuentaAutoPYP().setInstitucion(usuario.getCodigoInstitucionInt());
							ValoresPorDefecto.guardarAreaAperturaCuentaAutoPYP(forma.getAreaAperturaCuentaAutoPYP());
							forma.setListadoAreasAperturaCuentaAutoPYP(ValoresPorDefecto.consultarAreasAperturaCuentaAutoPYP(usuario.getCodigoInstitucionInt(), ConstantesBD.codigoNuncaValido));
						}

						return mapping.findForward("areaAperturaCuentaAuto");
					}
					if (estado.equals("eliminarAreaAperturaCuentaAutoPYP"))
					{
						ValoresPorDefecto.eliminarAreaAperturaCuentaAutoPYP(forma.getListadoAreasAperturaCuentaAutoPYP().get(forma.getPos()));
						forma.setListadoAreasAperturaCuentaAutoPYP(ValoresPorDefecto.consultarAreasAperturaCuentaAutoPYP(usuario.getCodigoInstitucionInt(), ConstantesBD.codigoNuncaValido));
						return mapping.findForward("areaAperturaCuentaAuto");
					}
					if(estado.equals("recargarSeviciosPrimSecSeleccionados"))
					{
						String temporal="";
						for(int i=0;i<forma.getServiciosManejoTransPrimario().size();i++)
						{
							if(!UtilidadTexto.isEmpty(temporal))
								temporal=temporal+",";
							temporal=temporal+forma.getServiciosManejoTransPrimario().get(i).intValue()+"";
						}
						for(int i=0;i<forma.getServiciosManejoTransSecundario().size();i++)
						{
							if(!UtilidadTexto.isEmpty(temporal))
								temporal=temporal+",";
							temporal=temporal+forma.getServiciosManejoTransSecundario().get(i).intValue()+"";
						}
						forma.setServiciosSeleccionados(temporal);
						return mapping.findForward("serviciosSeleccionados");
					}
					if(estado.equals("adicionarServicioPrimario"))
					{
						forma.getServiciosManejoTransPrimario().add(Integer.parseInt(forma.getServicioTemporal()));
						return mapping.findForward("serviciosPrimarios");
					}
					if(estado.equals("adicionarServicioSecundario"))
					{	
						forma.getServiciosManejoTransSecundario().add(Integer.parseInt(forma.getServicioTemporal()));
						return mapping.findForward("serviciosSecundarios");
					}
					if(estado.equals("eliminarServicioPrimario"))
					{
						forma.getServiciosManejoTransPrimario().remove(forma.getIndiceServicioSeleccionado());
						return mapping.findForward("serviciosPrimarios");
					}
					if(estado.equals("eliminarServicioSecundario"))
					{
						forma.getServiciosManejoTransSecundario().remove(forma.getIndiceServicioSeleccionado());
						return mapping.findForward("serviciosSecundarios");
					}
					// ****************** Fin / AreaAperturaCuentaAutoPYP

					//Anexo 888
					else if (forma.getEstado().equals("buscarServicio")) 
					{
						return mapping.findForward("principal");
					}
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
	 * metodo para guardar los parametros generales
	 * 
	 * @param con
	 *            Connection
	 * @param forma
	 * @param usuario
	 * @param institucion
	 */
	private void guardarValores(Connection con, ValoresPorDefectoForm forma,
			String usuario, int institucion) {
		/**
		 * NOTA: Insertar los correspondientes set en el modulo que corresponde
		 * si no existe por favor crearlo
		 */
		switch (forma.getModulo()) {
		/******************************************************************
		 * CONSULTA EXTERNA
		 ******************************************************************/
		case ConstantesBD.codigoModuloConsultaExterna:
			ValoresPorDefecto
				.modificar(
						con,
						ConstantesValoresPorDefecto.codigoValoresDefectoMinutosEsperaAsignarCitasOdoCaducadas,
						forma.getMinutosEsperaAsignarCitasOdoCaducadas(),
						forma.getMinutosEsperaAsignarCitasOdoCaducadas(),
						usuario, institucion);
			ValoresPorDefecto
					.modificar(
							con,
							ConstantesValoresPorDefecto.codigoValoresDefectoDiasPreviosNotificacionProximoControl,
							forma.getDiasPreviosNotificacionProximoControl(),
							forma.getDiasPreviosNotificacionProximoControl(),
							usuario, institucion);
			ValoresPorDefecto
					.modificar(
							con,
							ConstantesValoresPorDefecto.codigoValoresDefectoMinutosEsperaCitaCaduca,
							forma.getMinutosEsperaCitaCaduca(), forma
									.getMinutosEsperaCitaCaduca(), usuario,
							institucion);
			ValoresPorDefecto
					.modificar(
							con,
							ConstantesValoresPorDefecto.codigoValoresDefectoDatosCuentaRequeridoReservaCitas,
							forma.getDatosCuentaRequeridoReservaCitas(), forma
									.getDatosCuentaRequeridoReservaCitas(),
							usuario, institucion);
			ValoresPorDefecto.modificar(con,
					ConstantesValoresPorDefecto.codigoCrearCuentaAtencionCitas, forma
							.getCrearCuentaAtencionCitas(), forma
							.getCrearCuentaAtencionCitas(), usuario,
					institucion);
			ValoresPorDefecto
					.modificar(
							con,
							ConstantesValoresPorDefecto.codigoValoresDefectoDiasRestriccionCitasIncumplidas,
							forma.getDiasRestriccionCitasIncumplidas(), forma
									.getDiasRestriccionCitasIncumplidas(),
							usuario, institucion);
			ValoresPorDefecto
					.modificar(
							con,
							ConstantesValoresPorDefecto.codigoInstitucionManejaMultasPorIncumplimiento,
							forma.getInstitucionManejaMultasPorIncumplimiento(),
							forma.getInstitucionManejaMultasPorIncumplimiento(),
							usuario, institucion);
			ValoresPorDefecto.modificar(con,
					ConstantesValoresPorDefecto.codigoBloqueaCitasReservaAsignReprogPorIncump,
					forma.getBloqueaCitasReservaAsignReprogPorIncump(), forma
							.getBloqueaCitasReservaAsignReprogPorIncump(),
					usuario, institucion);
			ValoresPorDefecto.modificar(con,
					ConstantesValoresPorDefecto.codigoBloqueaAtencionCitasPorIncump, forma
							.getBloqueaAtencionCitasPorIncump(), forma
							.getBloqueaAtencionCitasPorIncump(), usuario,
					institucion);
			ValoresPorDefecto
					.modificar(
							con,
							ConstantesValoresPorDefecto.codigoFechaInicioControlMultasIncumplimientoCitas,
							forma
									.getFechaInicioControlMultasIncumplimientoCitas(),
							forma
									.getFechaInicioControlMultasIncumplimientoCitas(),
							usuario, institucion);
			ValoresPorDefecto.modificar(con,
					ConstantesValoresPorDefecto.codigoValorMultaPorIncumplimientoCitas, forma
							.getValorMultaPorIncumplimientoCitas(), forma
							.getValorMultaPorIncumplimientoCitas(), usuario,
					institucion);
			ValoresPorDefecto.modificar(con,
					ConstantesValoresPorDefecto.codigoValoresDefectoLasCitasDeControlSePermitenAsignarA, forma
							.getLasCitasDeControlSePermitenAsignarA(), forma
							.getLasCitasDeControlSePermitenAsignarA(), usuario,
					institucion);
			ValoresPorDefecto.modificar(con,
					ConstantesValoresPorDefecto.codigoValoresDefectoValidaEstadoContratoNominaALosProfesionalesSalud, forma
							.getValidaEstadoContratoNominaALosProfesionalesSalud(), forma
							.getValidaEstadoContratoNominaALosProfesionalesSalud(), usuario,
					institucion);
			
			ValoresPorDefecto.modificar(con,
					ConstantesValoresPorDefecto.codigoValoresDefectoFormatoImpReservaCitaOdonto, forma
							.getFormatoImpReservaCitaOdonto(), forma
							.getFormatoImpReservaCitaOdonto(), usuario,
					institucion);
			
			ValoresPorDefecto.modificar(con,
					ConstantesValoresPorDefecto.codigoValoresDefectoFormatoImpAsignacionCitaOdonto, forma
							.getFormatoImpAsignacionCitaOdonto(), forma
							.getFormatoImpAsignacionCitaOdonto(), usuario,
					institucion);
			
			ValoresPorDefecto.modificar(con, 
					ConstantesValoresPorDefecto.codigoValoresDefectoCancelaCitasOdontologicasReprogramar, 
					forma.getCancelarCitaAutoEstadoReprogramar(), forma.getCancelarCitaAutoEstadoReprogramar(), 
					usuario, institucion);
			
			
			break;
			
		/******************************************************************
		 * FACTURACION
		 ******************************************************************/
		case ConstantesBD.codigoModuloFacturacion:
			ValoresPorDefecto
					.modificar(
							con,
							ConstantesValoresPorDefecto.codigoValoresDefectoCerrarCuentaAnulacionFactura,
							forma.getCerrarCuentaAnulacionFactura(), forma
									.getCerrarCuentaAnulacionFactura(),
							usuario, institucion);
			ValoresPorDefecto.modificar(con,
					ConstantesValoresPorDefecto.codigoValoresDefectoManejoTopesPaciente, forma
							.getManejoTopesPaciente(), forma
							.getManejoTopesPaciente(), usuario, institucion);
			ValoresPorDefecto.modificar(con,
					ConstantesValoresPorDefecto.codigoValoresDefectoValidarContratosVencidos,
					forma.getValidarContratosVencidos(), forma
							.getValidarContratosVencidos(), usuario,
					institucion);
			ValoresPorDefecto.modificar(con,
					ConstantesValoresPorDefecto.codigoValoresDefectoValorUVR, forma
							.getValorUVR(), forma.getValorUVR(), usuario,
					institucion);
			// ValoresPorDefecto.modificar(con,
			// ConstantesValoresPorDefecto.codigoValoresDefectoCiudadResidencia,
			// forma.getCiudadVivienda().split("-")[2],
			// forma.getCiudadVivienda(), usuario, institucion);
			ValoresPorDefecto.modificar(con,
					ConstantesValoresPorDefecto.codigoValoresDefectoConvenioFisalud, forma
							.getConvenioFisalud().split("@@")[1], forma
							.getConvenioFisalud().split("@@")[0], usuario,
					institucion);
			ValoresPorDefecto
					.modificar(
							con,
							ConstantesValoresPorDefecto.codigoValoresDefectoValidarEgresoAdministrativoPaquetizar,
							forma.getValidarEgresoAdministrativoPaquetizar(),
							forma.getValidarEgresoAdministrativoPaquetizar(),
							usuario, institucion);
			ValoresPorDefecto
					.modificar(
							con,
							ConstantesValoresPorDefecto.codigoValoresDefectoMaxCantidPaquetesValidosIngresoPaciente,
							forma.getMaxCantidPaquetesValidosIngresoPaciente(),
							forma.getMaxCantidPaquetesValidosIngresoPaciente(),
							usuario, institucion);
			ValoresPorDefecto
					.modificar(
							con,
							ConstantesValoresPorDefecto.codigoValoresDefectoAsignarValorPacienteValorCargos,
							forma.getAsignarValorPacienteValorCargos(), forma
									.getAsignarValorPacienteValorCargos(),
							usuario, institucion);
			ValoresPorDefecto.modificar(con,
					ConstantesValoresPorDefecto.codigoValoresDefectoConsolidarCargos, forma
							.getConsolidarCargos(),
					forma.getConsolidarCargos(), usuario, institucion);
			ValoresPorDefecto.modificar(con,
					ConstantesValoresPorDefecto.codigoValoresDefectoEntidadManejaRips, forma
							.getEntidadManejaRips(), forma
							.getEntidadManejaRips(), usuario, institucion);
			ValoresPorDefecto
					.modificar(
							con,
							ConstantesValoresPorDefecto.codigoValoresDefectoInstitucionMultiempresa,
							forma.getInstitucionMultiempresa(), forma
									.getInstitucionMultiempresa(), usuario,
							institucion);
			ValoresPorDefecto
				.modificar(
						con,
						ConstantesValoresPorDefecto.codigoManejoEspecialInstitucionesOdontologia,
						forma.getManejoEspecialInstitucionesOdontologia(), forma
								.getManejoEspecialInstitucionesOdontologia(), usuario,
						institucion);
			ValoresPorDefecto.modificar(con,
					ConstantesValoresPorDefecto.codigoValoresDefectoGenerarEstanciaAutomatica,
					forma.getGeneraEstanciaAutomatica(), forma
							.getGeneraEstanciaAutomatica(), usuario,
					institucion);
			ValoresPorDefecto
					.modificar(
							con,
							ConstantesValoresPorDefecto.codigoValoresDefectoHoraGenerarEstanciaAutomatica,
							forma.getHoraGeneraEstanciaAutomatica(), forma
									.getHoraGeneraEstanciaAutomatica(),
							usuario, institucion);
			ValoresPorDefecto
					.modificar(
							con,
							ConstantesValoresPorDefecto.codigoValoresDefectoIncluirTipoPacienteCirugiaAmbulatoria,
							forma.getIncluirTipoPacientesCirugiaAmbulatoria(),
							forma.getIncluirTipoPacientesCirugiaAmbulatoria(),
							usuario, institucion);
			// Se evalï¿½a si el parï¿½metro Path Archivo Plano viene con ï¿½ sin el
			// Eslas para concatenarselo
			if (forma.getPathArchivosPlanosFacturacion().endsWith("/"))
				ValoresPorDefecto
						.modificar(
								con,
								ConstantesValoresPorDefecto.codigoValoresDefectoPathArchivosPlanosFacturacion,
								forma.getPathArchivosPlanosFacturacion(), forma
										.getPathArchivosPlanosFacturacion(),
								usuario, institucion);
			else {
				forma.setPathArchivosPlanosFacturacion(forma
						.getPathArchivosPlanosFacturacion()
						+ "/");
				ValoresPorDefecto
						.modificar(
								con,
								ConstantesValoresPorDefecto.codigoValoresDefectoPathArchivosPlanosFacturacion,
								forma.getPathArchivosPlanosFacturacion(), forma
										.getPathArchivosPlanosFacturacion(),
								usuario, institucion);
			}
			ValoresPorDefecto
					.modificar(
							con,
							ConstantesValoresPorDefecto.codigoPermitirFacturarReingresosIndependientes,
							forma.getPermitirFacturarReingresosIndependientes(),
							forma.getPermitirFacturarReingresosIndependientes(),
							usuario, institucion);
			ValoresPorDefecto
					.modificar(
							con,
							ConstantesValoresPorDefecto.codigoValoresDefectoRequeridaInfoRipsCargosDirectos,
							forma.getRequeridoInfoRipsCargoDirecto(), forma
									.getRequeridoInfoRipsCargoDirecto(),
							usuario, institucion);
			ValoresPorDefecto
					.modificar(
							con,
							ConstantesValoresPorDefecto.codigoValoresDefectoRequiereAutorizarAnularFacturas,
							forma.getRequiereAutorizarAnularFacturas(), forma
									.getRequiereAutorizarAnularFacturas(),
							usuario, institucion);
			
			ValoresPorDefecto.modificar(con,
					ConstantesValoresPorDefecto.codigoValoresDefectoValidarPoolesFact, forma
							.getValidarPoolesFact(), forma
							.getValidarPoolesFact(), usuario, institucion);
			
			ValoresPorDefecto.modificar(con,
					ConstantesValoresPorDefecto.codigoValoresDefectoManejaConsecutivoFacturaPorCentroAtencion, forma
							.getManejaConsecutivoFacturaPorCentroAtencion(), forma
							.getManejaConsecutivoFacturaPorCentroAtencion(), usuario, institucion);
			
			ValoresPorDefecto.modificar(con, ConstantesValoresPorDefecto.codigoValoresDefectoPermitirFacturarReclamarCuentasConRegistroPendientes, forma.getPermitirFacturarReclamarCuentasConRegistroPendientes(), forma.getPermitirFacturarReclamarCuentasConRegistroPendientes(), usuario, institucion);
			
			
			
			ValoresPorDefecto.modificar(con,
					ConstantesValoresPorDefecto.codigoValoresDefectoRequiereAutorizacionCapitacionSubcontratadaParaFacturar, forma
							.getRequiereAutorizacionCapitacionSubcontratadaParaFacturar(), forma
							.getRequiereAutorizacionCapitacionSubcontratadaParaFacturar(), usuario, institucion);
			
			ValoresPorDefecto.modificar(con,
					ConstantesValoresPorDefecto.codigoValoresDefectoManejaConsecutivoFacturasVariasPorCentroAtencion, forma
							.getManejaConsecutivoFacturasVariasPorCentroAtencion(), forma
							.getManejaConsecutivoFacturasVariasPorCentroAtencion(), usuario, institucion);
			

			ValoresPorDefecto.modificar(con,
					ConstantesValoresPorDefecto.codigoValoresDefectoPermitirfacturarReclamCuentaRATREC, forma
							.getPermitirfacturarReclamCuentaRATREC(), forma
							.getPermitirfacturarReclamCuentaRATREC(), usuario, institucion);
			
			ValoresPorDefecto.modificar(con,
					ConstantesValoresPorDefecto.codigoValoresDefectoHacerRequeridoValorAbonoAplicadoAlFacturar, forma
							.getHacerRequeridoValorAbonoAplicadoAlFacturar(), forma
							.getHacerRequeridoValorAbonoAplicadoAlFacturar(), usuario, institucion);
			
			break;
		/******************************************************************
		 * MANEJO DEL PACIENTE
		 ******************************************************************/
		case ConstantesBD.codigoModuloManejoPaciente:
			ValoresPorDefecto
					.modificar(
							con,
							ConstantesValoresPorDefecto.codigoValoresDefectoCentroCostoConsultaExterna,
							forma.getCentroCostoConsultaExterna().split("@@")[1],
							forma.getCentroCostoConsultaExterna().split("@@")[0],
							usuario, institucion);
			ValoresPorDefecto.modificar(con,
					ConstantesValoresPorDefecto.codigoValoresDefectoCentroCostoAmbulatorios,
					forma.getCentroCostoAmbulatorios().split("@@")[1], forma
							.getCentroCostoAmbulatorios().split("@@")[0],
					usuario, institucion);
			ValoresPorDefecto.modificar(con,
					ConstantesValoresPorDefecto.codigoValoresDefectoCentroCostoUrgencias,
					forma.getCentroCostoUrgencias().split("@@")[1], forma
							.getCentroCostoUrgencias().split("@@")[0], usuario,
					institucion);
			ValoresPorDefecto.modificar(con,
					ConstantesValoresPorDefecto.codigoValoresDefectoCiudadNacimiento, forma
							.getCiudadNacimiento().split("-")[2], forma
							.getCiudadNacimiento(), usuario, institucion);
			ValoresPorDefecto.modificar(con,
					ConstantesValoresPorDefecto.codigoValoresDefectoCiudadResidencia, forma
							.getCiudadVivienda().split("-")[2], forma
							.getCiudadVivienda(), usuario, institucion);
			ValoresPorDefecto.modificar(con,
					ConstantesValoresPorDefecto.codigoValoresDefectoPaisResidencia, forma
							.getPaisResidencia().split("-")[1], forma
							.getPaisResidencia(), usuario, institucion);
			ValoresPorDefecto.modificar(con,
					ConstantesValoresPorDefecto.codigoValoresDefectoPaisNacimiento, forma
							.getPaisNacimiento().split("-")[1], forma
							.getPaisNacimiento(), usuario, institucion);
			if (!forma.getBarrioResidencia().equals(""))
				ValoresPorDefecto.modificar(con,
						ConstantesValoresPorDefecto.codigoValoresDefectoBarrioResidencia,
						forma.getBarrioResidencia().split("-")[0] + "-"
								+ forma.getBarrioResidencia().split("-")[1],
						forma.getBarrioResidencia(), usuario, institucion);
			else
				ValoresPorDefecto.modificar(con,
						ConstantesValoresPorDefecto.codigoValoresDefectoBarrioResidencia,
						forma.getBarrioResidencia(), forma
								.getBarrioResidencia(), usuario, institucion);
			ValoresPorDefecto.modificar(con,
					ConstantesValoresPorDefecto.codigoValoresDefectoDireccion, forma
							.getDireccionPaciente(), forma
							.getDireccionPaciente(), usuario, institucion);
			ValoresPorDefecto.modificar(con,
					ConstantesValoresPorDefecto.codigoValoresDefectoExcepcionRipsConsultorios,
					forma.getExcepcionRipsConsultorios(), forma
							.getExcepcionRipsConsultorios(), usuario,
					institucion);
			// ValoresPorDefecto.modificar(con,
			// ConstantesValoresPorDefecto.codigoValoresDefectoCarnetRequerido,
			// forma.getCarnetR/equerido(), forma.getCarnetRequerido(), usuario,
			// institucion);
			ValoresPorDefecto.modificar(con,
					ConstantesValoresPorDefecto.codigoValoresDefectoEstadoCamaEgreso, forma
							.getCodigoEstadoCama().split("@@")[1], forma
							.getCodigoEstadoCama().split("@@")[0], usuario,
					institucion);
			ValoresPorDefecto.modificar(con,
					ConstantesValoresPorDefecto.codigoValoresDefectoHistoriaClinica, forma
							.getHistoriaClinica(), forma.getHistoriaClinica(),
					usuario, institucion);
			ValoresPorDefecto.modificar(con,
					ConstantesValoresPorDefecto.codigoValoresDefectoIngresoEdad, forma
							.getFechaNacimiento(), forma.getFechaNacimiento(),
					usuario, institucion);
			ValoresPorDefecto.modificar(con,
					ConstantesValoresPorDefecto.codigoValoresDefectoTipoId, forma.getTipoId()
							.split("@@")[1], forma.getTipoId().split("@@")[0],
					usuario, institucion);
			ValoresPorDefecto.modificar(con,
					ConstantesValoresPorDefecto.codigoValoresDefectoValidarInterpretadas,
					forma.getValidarEstadoSolicitudesInterpretadas(), forma
							.getValidarEstadoSolicitudesInterpretadas(),
					usuario, institucion);
			ValoresPorDefecto.modificar(con,
					ConstantesValoresPorDefecto.codigoValoresDefectoDomicilio, forma
							.getZonaDomicilio().split("@@")[1], forma
							.getZonaDomicilio().split("@@")[0], usuario,
					institucion);
			ValoresPorDefecto
					.modificar(
							con,
							ConstantesValoresPorDefecto.codigoValoresDefectoValidarEdadResponsablePaciente,
							forma.getValidarEdadResponsablePaciente(), forma
									.getValidarEdadResponsablePaciente(),
							usuario, institucion);
			ValoresPorDefecto.modificar(con,
					ConstantesValoresPorDefecto.codigoValoresDefectoValidarEdadDeudorPaciente,
					forma.getValidarEdadDeudorPaciente(), forma
							.getValidarEdadDeudorPaciente(), usuario,
					institucion);
			ValoresPorDefecto.modificar(con,
					ConstantesValoresPorDefecto.codigoValoresDefectoAniosBaseEdadAdulta, forma
							.getAniosBaseEdadAdulta(), forma
							.getAniosBaseEdadAdulta(), usuario, institucion);
			ValoresPorDefecto
					.modificar(
							con,
							ConstantesValoresPorDefecto.codigoValoresDefectoHoraCorteHistoricoCamas,
							forma.getHoraCorteHistoricoCamas(), forma
									.getHoraCorteHistoricoCamas(), usuario,
							institucion);
			ValoresPorDefecto
					.modificar(
							con,
							ConstantesValoresPorDefecto.codigoValoresDefectoMinutosLimiteAlertaReserva,
							forma.getMinutosLimiteAlertaReserva(), forma
									.getMinutosLimiteAlertaReserva(), usuario,
							institucion);
			ValoresPorDefecto
					.modificar(
							con,
							ConstantesValoresPorDefecto.codigoValoresDefectoMinutosLimiteAlertaPacienteConSalidaHospitalizacion,
							forma
									.getMinutosLimiteAlertaPacienteConSalidaHospitalizacion(),
							forma
									.getMinutosLimiteAlertaPacienteConSalidaHospitalizacion(),
							usuario, institucion);
			ValoresPorDefecto
					.modificar(
							con,
							ConstantesValoresPorDefecto.codigoValoresDefectoMinutosLimiteAlertaPacienteConSalidaUrgencias,
							forma
									.getMinutosLimiteAlertaPacienteConSalidaUrgencias(),
							forma
									.getMinutosLimiteAlertaPacienteConSalidaUrgencias(),
							usuario, institucion);
			ValoresPorDefecto
					.modificar(
							con,
							ConstantesValoresPorDefecto.codigoValoresDefectominutosLimiteAlertaPacientePorRemitirHospitalizacion,
							forma
									.getMinutosLimiteAlertaPacientePorRemitirHospitalizacion(),
							forma
									.getMinutosLimiteAlertaPacientePorRemitirHospitalizacion(),
							usuario, institucion);
			ValoresPorDefecto
					.modificar(
							con,
							ConstantesValoresPorDefecto.codigoValoresDefectoMinutosLimiteAlertaPacientePorRemitirUrgencias,
							forma
									.getMinutosLimiteAlertaPacientePorRemitirUrgencias(),
							forma
									.getMinutosLimiteAlertaPacientePorRemitirUrgencias(),
							usuario, institucion);
			ValoresPorDefecto.modificar(con,
					ConstantesValoresPorDefecto.codigoValoresDefectoEntidadManejaHospitalDia,
					forma.getEntidadManejaHospitalDia(), forma
							.getEntidadManejaHospitalDia(), usuario,
					institucion);
			ValoresPorDefecto
					.modificar(
							con,
							ConstantesValoresPorDefecto.codigoValoresDefectoValoracionUrgenciasEnHospitalizacion,
							forma.getValoracionUrgenciasEnHospitalizacion(),
							forma.getValoracionUrgenciasEnHospitalizacion(),
							usuario, institucion);
			ValoresPorDefecto
					.modificar(
							con,
							ConstantesValoresPorDefecto.codigoValoresDefectoUbicacionPlanosEntidadesSubcontratadas,
							ValoresPorDefecto
									.getIntegridadDominio(
											forma
													.getUbicacionPlanosEntidadesSubcontratadas())
									.toString(),
							forma.getUbicacionPlanosEntidadesSubcontratadas(),
							usuario, institucion);
			// Se evalï¿½a si el parï¿½metro Path Archivo Plano viene con ï¿½ sin el
			// Eslas para concatenarselo
			if (forma.getArchivosPlanosReportes().endsWith("/"))
				ValoresPorDefecto.modificar(con,
						ConstantesValoresPorDefecto.codigoValoresDefectoPath, forma
								.getArchivosPlanosReportes(), forma
								.getArchivosPlanosReportes(), usuario,
						institucion);
			else {
				forma.setArchivosPlanosReportes(forma
						.getArchivosPlanosReportes()
						+ "/");
				ValoresPorDefecto.modificar(con,
						ConstantesValoresPorDefecto.codigoValoresDefectoPath, forma
								.getArchivosPlanosReportes(), forma
								.getArchivosPlanosReportes(), usuario,
						institucion);
			}
			ValoresPorDefecto.modificar(con,
					ConstantesValoresPorDefecto.codigoValoresDefectoGeneracionForecatEnRips,
					forma.getGenForecatRips(), forma.getGenForecatRips(),
					usuario, institucion);
			ValoresPorDefecto
					.modificar(
							con,
							ConstantesValoresPorDefecto.codigoValoresDefectoAsignaValoracionCxAmbulatoriaHospitalizado,
							forma.getAsignaValoracionCxAmbulaHospita(), forma
									.getAsignaValoracionCxAmbulaHospita(),
							usuario, institucion);
			ValoresPorDefecto
					.modificar(
							con,
							ConstantesValoresPorDefecto.codigoLiberarCamaHospitalizacionDespuesFacturar,
							forma
									.getLiberarCamaHospitalizacionDespuesFacturar(),
							forma
									.getLiberarCamaHospitalizacionDespuesFacturar(),
							usuario, institucion);
			ValoresPorDefecto
					.modificar(
							con,
							ConstantesValoresPorDefecto.codigoValoresDefectoPathArchivosPlanosManejoPaciente,
							forma.getPathArchivosPlanosManejoPaciente(), forma
									.getPathArchivosPlanosManejoPaciente(),
							usuario, institucion);
			ValoresPorDefecto.modificar(con,
					ConstantesValoresPorDefecto.codigoTiempoMaximoReingresoUrgencias, forma
							.getTiempoMaximoReingresoUrgencias(), forma
							.getTiempoMaximoReingresoUrgencias(), usuario,
					institucion);
			ValoresPorDefecto.modificar(con,
					ConstantesValoresPorDefecto.codigoTiempoMaximoReingresoHospitalizacion,
					forma.getTiempoMaximoReingresoHospitalizacion(), forma
							.getTiempoMaximoReingresoHospitalizacion(),
					usuario, institucion);
			
			ValoresPorDefecto.modificar(con,ConstantesValoresPorDefecto.codigoValoresDefectoUsuarioaReportarenSolicitAuto,
					forma.getTipoUsuarioaReportarSol(),
					forma.getTipoUsuarioaReportarSol(),
					usuario, institucion);
			
			ValoresPorDefecto.modificar(con, ConstantesValoresPorDefecto.codigoValoresDefectoPermitirRegistrarReclamacionCuentasNoFacturadas,
					forma.getPermitirRegistrarReclamacionCuentasNoFacturadas(), 
					forma.getPermitirRegistrarReclamacionCuentasNoFacturadas(), 
					usuario, institucion);		
			
			ValoresPorDefecto.modificar(con, ConstantesValoresPorDefecto.codigoValoresDefectoManejoOxigenoFurips, forma.getManejoOxigenoFurips(), forma.getManejoOxigenoFurips(), usuario, institucion);
			
			ValoresPorDefecto.modificar(con, ConstantesValoresPorDefecto.codigoValoresDefectoMesesMaxAdminAutoCapVencidas, 
					forma.getMesesMaxAdminAutoCapVencidas(), 
					forma.getMesesMaxAdminAutoCapVencidas(), 
					usuario, institucion);
			
			ValoresPorDefecto.modificar(con, ConstantesValoresPorDefecto.codigoValoresDefectoDiasMaxProrrogaAutorizacionArticulo, 
					forma.getDiasMaxProrrogaAutorizacionArticulo(), 
					forma.getDiasMaxProrrogaAutorizacionArticulo(), 
					usuario, institucion);
			
			ValoresPorDefecto.modificar(con, ConstantesValoresPorDefecto.codigoValoresDefectoDiasMaxProrrogaAutorizacionServicio, 
					forma.getDiasMaxProrrogaAutorizacionServicio(), 
					forma.getDiasMaxProrrogaAutorizacionServicio(), 
					usuario, institucion);
			
			if(!forma.getEscalaPacientePerfil().equals(""))
			{
				String[] vectorEscala = forma.getEscalaPacientePerfil().split("@@");
				ValoresPorDefecto.modificar(con, 
						ConstantesValoresPorDefecto.codigoValoresDefectoEscalaPerfilPaciente, 
						vectorEscala[1], vectorEscala[0], usuario, institucion);
			}
			else
			{
				ValoresPorDefecto.modificar(
						con, 
						ConstantesValoresPorDefecto.codigoValoresDefectoEscalaPerfilPaciente, 
						forma.getEscalaPacientePerfil(), 
						forma.getEscalaPacientePerfil(), usuario, institucion);
			}
			
			// Se evalï¿½a si el parï¿½metro Path Archivo Plano FURIPS viene con ï¿½
			// sin el Eslas para concatenarselo
			if (forma.getPathArchivosPlanosFurips().endsWith("/"))
				ValoresPorDefecto
						.modificar(
								con,
								ConstantesValoresPorDefecto.codigoValoresDefectoPathArchivosPlanosFurips,
								forma.getPathArchivosPlanosFurips(), forma
										.getPathArchivosPlanosFurips(),
								usuario, institucion);
			else {
				forma.setPathArchivosPlanosFurips(forma
						.getPathArchivosPlanosFurips()
						+ "/");
				ValoresPorDefecto
						.modificar(
								con,
								ConstantesValoresPorDefecto.codigoValoresDefectoPathArchivosPlanosFurips,
								forma.getPathArchivosPlanosFurips(), forma
										.getPathArchivosPlanosFurips(),
								usuario, institucion);
			}
			
			ValoresPorDefecto.modificar(
					con,
					ConstantesValoresPorDefecto.codigoValoresDefectoEntidadSubcontratadaCentrosCostoAutorizacionCapitacionSubcontratada, 
					forma.getEntidadSubcontratadaCentrosCostoAutorizacionCapitacionSubcontratada().split("-")[1], 
					forma.getEntidadSubcontratadaCentrosCostoAutorizacionCapitacionSubcontratada(), usuario, institucion);
			
			
			ValoresPorDefecto.modificar(con,
					ConstantesValoresPorDefecto.codigoValoresDefectoPrioridadEntidadSubcontratada, forma
							.getPrioridadEntidadSubcontratada(), forma
							.getPrioridadEntidadSubcontratada(), usuario, institucion);
			
			ValoresPorDefecto.modificar(con,
					ConstantesValoresPorDefecto.codigoValoresDefectoFormatoImpresionAutorEntidadSub, forma
							.getFormatoImpresionAutorEntidadSub(), forma
							.getFormatoImpresionAutorEntidadSub(), usuario, institucion);
			
			ValoresPorDefecto.modificar(con,
					ConstantesValoresPorDefecto.codigoValoresDefectoEncFormatoImpresionAutorEntidadSub, forma
							.getEncFormatoImpresionAutorEntidadSub(), forma
							.getEncFormatoImpresionAutorEntidadSub(), usuario, institucion);
			
			ValoresPorDefecto.modificar(con,
					ConstantesValoresPorDefecto.codigoValoresDefectoPiePagFormatoImpresionAutorEntidadSub, forma
							.getPiePagFormatoImpresionAutorEntidadSub(), forma
							.getPiePagFormatoImpresionAutorEntidadSub(), usuario, institucion);
			
			ValoresPorDefecto.modificar(con,
					ConstantesValoresPorDefecto.codigoValoresDefectoDiasVigenciaAutorIndicativoTemp, forma
							.getDiasVigenciaAutorIndicativoTemp(), forma
							.getDiasVigenciaAutorIndicativoTemp(), usuario, institucion);
			
			ValoresPorDefecto.modificar(con,
					ConstantesValoresPorDefecto.codigoValoresDefectoDiasCalcularFechaVencAutorizacionArticulo, forma
							.getDiasCalcularFechaVencAutorizacionArticulo(), forma
							.getDiasCalcularFechaVencAutorizacionArticulo(), usuario, institucion);
			
			ValoresPorDefecto.modificar(con,
					ConstantesValoresPorDefecto.codigoValoresDefectoDiasCalcularFechaVencAutorizacionServicio, forma
							.getDiasCalcularFechaVencAutorizacionServicio(), forma
							.getDiasCalcularFechaVencAutorizacionServicio(), usuario, institucion);
			
			ValoresPorDefecto.modificar(con,
					ConstantesValoresPorDefecto.codigoValoresDefectoDiasProrrogaAutorizacion, forma
							.getDiasProrrogaAutorizacion(), forma
							.getDiasProrrogaAutorizacion(), usuario, institucion);
			
			ValoresPorDefecto.modificar(con,
					ConstantesValoresPorDefecto.codigoValoresDefectoDiasVigentesNuevaAutorizacionEstanciaSerArt, forma
							.getDiasVigentesNuevaAutorizacionEstanciaSerArt(), forma
							.getDiasVigentesNuevaAutorizacionEstanciaSerArt(), usuario, institucion);
			
			ValoresPorDefecto.modificar(con,
					ConstantesValoresPorDefecto.codigoValoresDefectoNumMaximoReclamacionesAccEventoXFactura, forma
							.getNumMaximoReclamacionesAccEventoXFactura(), forma
							.getNumMaximoReclamacionesAccEventoXFactura(), usuario, institucion);
			
			ValoresPorDefecto.modificar(con,
					ConstantesValoresPorDefecto.codigoValoresDefectoPermitirModificarDatosUsuariosCapitados, forma
							.getPermitirModificarDatosUsuariosCapitados(), forma
							.getPermitirModificarDatosUsuariosCapitados(), usuario, institucion);
			
			
			ValoresPorDefecto.modificar(con,
					ConstantesValoresPorDefecto.codigoValoresDefectoPermitirModificarDatosUsuariosCapitadosModificarCuenta, forma
							.getPermitirModificarDatosUsuariosCapitadosModificarCuenta(), forma
							.getPermitirModificarDatosUsuariosCapitadosModificarCuenta(), usuario, institucion);
			
			ValoresPorDefecto.modificar(con,
					ConstantesValoresPorDefecto.codigoValoresDefectoEsquemaTarifarioAutocapitaSubCirugiasNoCurentos, 
					forma.getEsquemaTarifarioAutocapitaSubCirugiasNoCurentos().split("@@")[1], 
					forma.getEsquemaTarifarioAutocapitaSubCirugiasNoCurentos().split("@@")[0], usuario, institucion);
			
			
			ValoresPorDefecto.modificar(con,
					ConstantesValoresPorDefecto.codigoValoresDefectoValidarDisponibilidadSaldoPresupuestoAutorizacionesCapotaAutomatica, forma
							.getValidarDisponibilidadSaldoPresupuestoAutorizacionesCapotaAutomatica(), forma
							.getValidarDisponibilidadSaldoPresupuestoAutorizacionesCapotaAutomatica(), usuario, institucion);
			
			ValoresPorDefecto.insertarServiciosManejoTransPrimario(con, forma.getServiciosManejoTransPrimario(),institucion);
			
			ValoresPorDefecto.insertarServiciosManejoTransSecundario(con, forma.getServiciosManejoTransSecundario(),institucion);
			
			ValoresPorDefecto.modificar(con,
					ConstantesValoresPorDefecto.codigoValoresDefectoViaIngresoValidacionesOrdenesAmbulatorias, 
					forma.getViaIngresoValidacionesOrdenesAmbulatorias().split("@@")[0], 
					forma.getViaIngresoValidacionesOrdenesAmbulatorias().split("@@")[1], usuario, institucion);
			
			ValoresPorDefecto.modificar(con,
					ConstantesValoresPorDefecto.codigoValoresDefectoViaIngresoValidacionesPeticiones, 
					forma.getViaIngresoValidacionesPeticiones().split("@@")[0], 
					forma.getViaIngresoValidacionesPeticiones().split("@@")[1], usuario, institucion);	
			
			ValoresPorDefecto.modificar(con,
					ConstantesValoresPorDefecto.codigoValoresDefectoTipoPacienteValidacionesOrdenesAmbulatorias, 
					forma.getTipoPacienteValidacionesOrdenesAmbulatorias().split("@@")[0], 
					forma.getTipoPacienteValidacionesOrdenesAmbulatorias().split("@@")[1], usuario, institucion);
			
			ValoresPorDefecto.modificar(con,
					ConstantesValoresPorDefecto.codigoValoresDefectoTipoPacienteValidacionesPeticiones, 
					forma.getTipoPacienteValidacionesPeticiones().split("@@")[0], 
					forma.getTipoPacienteValidacionesPeticiones().split("@@")[1], usuario, institucion);
			
			
		break;
			
		/******************************************************************
		 * ADMINISTRACION
		 ******************************************************************/
		case ConstantesBD.codigoModuloAdministracion:
			ValoresPorDefecto.modificar(con,
					ConstantesValoresPorDefecto.codigoValoresDefectoCentinela, forma
							.getFlagCentinela(), forma.getFlagCentinela(),
					usuario, institucion);
			ValoresPorDefecto.modificar(con,
					ConstantesValoresPorDefecto.codigoValoresDefectoMaxPageItems, forma
							.getMaxPageItems(), forma.getMaxPageItems(),
					usuario, institucion);
			ValoresPorDefecto.modificar(con,
					ConstantesValoresPorDefecto.codigoValoresDefectoOcupacion, forma
							.getOcupacion().split("@@")[1], forma
							.getOcupacion().split("@@")[0], usuario,
					institucion);
			ValoresPorDefecto.modificar(con,
					ConstantesValoresPorDefecto.codigoValoresDefectoInfoAdicIngresoConvenios,
					forma.getFlagInfoAdicIngresoConvenios(), forma
							.getFlagInfoAdicIngresoConvenios(), usuario,
					institucion);
			ValoresPorDefecto
					.modificar(
							con,
							ConstantesValoresPorDefecto.codigoValoresDefectoModificarMinutosEsperaCuentasProcFact,
							forma.getMinutosEsperaCuentasProcFact(), forma
									.getMinutosEsperaCuentasProcFact(),
							usuario, institucion);
			ValoresPorDefecto
					.modificar(
							con,
							ConstantesValoresPorDefecto.codigoValoresDefectoManejaConversionMonedaExtranjera,
							forma.getManejaConversionMonedaExtranjera(), forma
									.getManejaConversionMonedaExtranjera(),
							usuario, institucion);
			ValoresPorDefecto.modificar(con,
					ConstantesValoresPorDefecto.codigoValoresDefectoImpresionMediaCarta, forma
							.getImpresionMediaCarta(), forma
							.getImpresionMediaCarta(), usuario, institucion);
			ValoresPorDefecto
					.modificar(
							con,
							ConstantesValoresPorDefecto.codigoValoresDefectoIdentificadorInstitucionArchivosColsanitas,
							forma
									.getIdentificadorInstitucionArchivosColsanitas(),
							forma
									.getIdentificadorInstitucionArchivosColsanitas(),
							usuario, institucion);
			ValoresPorDefecto
					.modificar(
							con,
							ConstantesValoresPorDefecto.codigoValoresDefectoCodigoManualEstandarBusquedaServicios,
							forma.getCodigoManualEstandarBusquedaServicios()
									.split("@@")[1], forma
									.getCodigoManualEstandarBusquedaServicios()
									.split("@@")[0], usuario, institucion);
			ValoresPorDefecto.modificar(con,
					ConstantesValoresPorDefecto.codigoTiempoMaximoReingresoUrgencias, forma
							.getTiempoMaximoReingresoUrgencias(), forma
							.getTiempoMaximoReingresoUrgencias(), usuario,
					institucion);
			ValoresPorDefecto.modificar(con,
					ConstantesValoresPorDefecto.codigoTiempoMaximoReingresoHospitalizacion,
					forma.getTiempoMaximoReingresoHospitalizacion(), forma
							.getTiempoMaximoReingresoHospitalizacion(),
					usuario, institucion);
			ValoresPorDefecto
					.modificar(
							con,
							ConstantesValoresPorDefecto.codigoPermitirFacturarReingresosIndependientes,
							forma.getPermitirFacturarReingresosIndependientes(),
							forma.getPermitirFacturarReingresosIndependientes(),
							usuario, institucion);
			ValoresPorDefecto
					.modificar(
							con,
							ConstantesValoresPorDefecto.codigoValoresDefectoComprobacionDerCapitacionObliga,
							forma
									.getComprobacionDerechosCapitacionObligatoria(),
							forma
									.getComprobacionDerechosCapitacionObligatoria(),
							usuario, institucion);
			ValoresPorDefecto
					.modificar(
							con,
							ConstantesValoresPorDefecto.codigoValoresDefectoCodigoManualEstandarBusquedaArticulos,
							forma.getCodigoManualEstandarBusquedaArticulos()
									.split("@@")[1], forma
									.getCodigoManualEstandarBusquedaArticulos()
									.split("@@")[0], usuario, institucion);
			ValoresPorDefecto
					.modificar(
							con,
							ConstantesValoresPorDefecto.codigoValoresDefectoNumeroDiasBusquedaReportes,
							forma.getNumeroDiasBusquedaReportes(), forma
									.getNumeroDiasBusquedaReportes(), usuario,
							institucion);
			ValoresPorDefecto.modificar(con,
					ConstantesValoresPorDefecto.codigoValoresDefectoNumDigitosCaptNumIdPac,
					forma.getNumDigCaptNumIdPac(), forma
							.getNumDigCaptNumIdPac(), usuario, institucion);
			
			ValoresPorDefecto
			.modificar(
					con,
					ConstantesValoresPorDefecto.codigoValoresDefectoContabilizacionRequeridoProcesoAsocioFacCuentaCapitacion,
					forma.getContabilizacionRequeridoProcesoAsocioFacCuentaCapitacion(), 
					forma.getContabilizacionRequeridoProcesoAsocioFacCuentaCapitacion(), 
					usuario, 
					institucion);
			
			ValoresPorDefecto
			.modificar(
					con,
					ConstantesValoresPorDefecto.codigoValoresDefectoManejaInterfazUsuariosSistemaERP,
					forma.getManejaInterfazUsuariosSistemaERP(), 
					forma.getManejaInterfazUsuariosSistemaERP(), 
					usuario, 
					institucion);
			
			ValoresPorDefecto
			.modificar(
					con,
					ConstantesValoresPorDefecto.codigoValoresDefectoFechaInicioCierreOrdenMedica,
					forma.getFechaInicioCierreOrdenMedica(), 
					forma.getFechaInicioCierreOrdenMedica(), 
					usuario, 
					institucion);
			
			ValoresPorDefecto
			.modificar(
					con,
					ConstantesValoresPorDefecto.codigoValoresDefectoHoraEjecProcesoInactivarUsuarioInacSistema,
					forma.getHoraEjecProcesoInactivarUsuarioInacSistema(),
					forma.getHoraEjecProcesoInactivarUsuarioInacSistema(), 
					usuario, institucion);
			
			ValoresPorDefecto
			.modificar(
					con,
					ConstantesValoresPorDefecto.codigoValoresDefectoHoraEjeProcesoCaduContraInacSistema,
					forma.getHoraEjeProcesoCaduContraInacSistema(),
					forma.getHoraEjeProcesoCaduContraInacSistema(), 
					usuario, institucion);
			
			ValoresPorDefecto
			.modificar(
					con,
					ConstantesValoresPorDefecto.codigoValoresDefectoDiasVigenciaContraUsuario,
					forma.getDiasVigenciaContraUsuario(),
					forma.getDiasVigenciaContraUsuario(), 
					usuario, institucion);
			
			ValoresPorDefecto
			.modificar(
					con,
					ConstantesValoresPorDefecto.codigoValoresDefectoDiasFinalesVigenciaContraMostrarAlerta,
					forma.getDiasFinalesVigenciaContraMostrarAlerta(),
					forma.getDiasFinalesVigenciaContraMostrarAlerta(), 
					usuario, institucion);
			
			////REINICIAR PROCESOS AUTOMATICOS.
			ProcesoActivacionInactivacionUsuariosCron.reinicarProceso(institucion);
			ProcesoActivacionInactivacionPasswordUsuariosCron.reinicarProceso(institucion);
			
			
			
			
			break;
		/******************************************************************
		 * HISTORIA CLINICA
		 ******************************************************************/
		case ConstantesBD.codigoModuloHistoriaClinica:
			ValoresPorDefecto.modificar(con,
					ConstantesValoresPorDefecto.codigoValoresDefectoCausaExterna, forma
							.getCausaExterna().split("@@")[1], forma
							.getCausaExterna().split("@@")[0], usuario,
					institucion);
			if (forma.getFinalidad().equals(""))
				ValoresPorDefecto.modificar(con,
						ConstantesValoresPorDefecto.codigoValoresDefectoFinalidadConsulta, "",
						"", usuario, institucion);
			else
				ValoresPorDefecto.modificar(con,
						ConstantesValoresPorDefecto.codigoValoresDefectoFinalidadConsulta,
						forma.getFinalidad().split("@@")[1], forma
								.getFinalidad().split("@@")[0], usuario,
						institucion);
			ValoresPorDefecto
					.modificar(
							con,
							ConstantesValoresPorDefecto.codigoValoresDefectoCodigoOcupacionMedicoEspecialista,
							forma.getOcupacionMedicoEspecialista().split("@@")[1],
							forma.getOcupacionMedicoEspecialista().split("@@")[0],
							usuario, institucion);
			ValoresPorDefecto
					.modificar(
							con,
							ConstantesValoresPorDefecto.codigoValoresDefectoCodigoOcupacionMedicoGeneral,
							forma.getOcupacionMedicoGeneral().split("@@")[1],
							forma.getOcupacionMedicoGeneral().split("@@")[0],
							usuario, institucion);
			ValoresPorDefecto.modificar(con,
					ConstantesValoresPorDefecto.codigoValoresDefectoCodigoOcupacionEnfermera,
					forma.getOcupacionEnfermera().split("@@")[1], forma
							.getOcupacionEnfermera().split("@@")[0], usuario,
					institucion);
			ValoresPorDefecto
					.modificar(
							con,
							ConstantesValoresPorDefecto.codigoValoresDefectoCodigoOcupacionAuxiliarEnfermeria,
							forma.getOcupacionAuxiliarEnfermeria().split("@@")[1],
							forma.getOcupacionAuxiliarEnfermeria().split("@@")[0],
							usuario, institucion);
			ValoresPorDefecto.modificar(con,
					ConstantesValoresPorDefecto.codigoValoresDefectoRedNoAdscrita, forma
							.getRedNoAdscrita().split("@@")[1], forma
							.getRedNoAdscrita().split("@@")[0], usuario,
					institucion);
			ValoresPorDefecto
					.modificar(
							con,
							ConstantesValoresPorDefecto.codigoValoresDefectoHorasCaducidadReferenciasExternas,
							forma.getHorasCaducidadReferenciasExternas(), forma
									.getHorasCaducidadReferenciasExternas(),
							usuario, institucion);
			ValoresPorDefecto
					.modificar(
							con,
							ConstantesValoresPorDefecto.codigoValoresDefectoLlamadoAutomaticoReferencia,
							forma.getLlamadoAutomaticoReferencia(), forma
									.getLlamadoAutomaticoReferencia(), usuario,
							institucion);
			ValoresPorDefecto
					.modificar(
							con,
							ConstantesValoresPorDefecto.codigoValoresDefectoValidacionOcupacionJustificacionNoPosArticulos,
							forma
									.getValidacionOcupacionJustificacionNoPosArticulos(),
							forma
									.getValidacionOcupacionJustificacionNoPosArticulos(),
							usuario, institucion);
			ValoresPorDefecto
					.modificar(
							con,
							ConstantesValoresPorDefecto.codigoValoresDefectoValidacionOcupacionJustificacionNoPosServicios,
							forma
									.getValidacionOcupacionJustificacionNoPosServicios(),
							forma
									.getValidacionOcupacionJustificacionNoPosServicios(),
							usuario, institucion);
			ValoresPorDefecto
					.modificar(
							con,
							ConstantesValoresPorDefecto.codigoValoresDefectoPermitirModificarTiempoTratamientoJustificacionNopos,
							forma
									.getPermitirModificarTiempoTratamientoJustificacionNopos(),
							forma
									.getPermitirModificarTiempoTratamientoJustificacionNopos(),
							usuario, institucion);
			ValoresPorDefecto
					.modificar(
							con,
							ConstantesValoresPorDefecto.codigoValoresDefectoValidarAdministracionMedicamentosEgresoMedico,
							forma.getValidadAdministracionMedEgresoMedico(),
							forma.getValidadAdministracionMedEgresoMedico(),
							usuario, institucion);
			ValoresPorDefecto
					.modificar(
							con,
							ConstantesValoresPorDefecto.codigoControlaInterpretacionProcedimientosParaEvolucionar,
							forma
									.getControlaInterpretacionProcedimientosEvolucion(),
							forma
									.getControlaInterpretacionProcedimientosEvolucion(),
							usuario, institucion);
			ValoresPorDefecto.modificar(con,
					ConstantesValoresPorDefecto.codigoValoresDefectoMaxPageItemsEpicrisis,
					forma.getMaxPageItemsEpicrisis(), forma
							.getMaxPageItemsEpicrisis(), usuario, institucion);
			ValoresPorDefecto
					.modificar(
							con,
							ConstantesValoresPorDefecto.codigoValoresDefectoMostrarAntecedentesEpicrisis,
							forma
									.getMostrarAntecedentesParametrizadosEpicrisis(),
							forma
									.getMostrarAntecedentesParametrizadosEpicrisis(),
							usuario, institucion);
			ValoresPorDefecto
					.modificar(
							con,
							ConstantesValoresPorDefecto.codigoValoresDefectoPermitirConsultarEpicrisisSoloProf,
							forma.getPermitirConsultarEpicrisisSoloProf(),
							forma.getPermitirConsultarEpicrisisSoloProf(),
							usuario, institucion);
			ValoresPorDefecto
					.modificar(
							con,
							ConstantesValoresPorDefecto.codigoValoresDefectoMostrarEnviarEpicrisisEvol,
							forma.getMostrarEnviarEpicrisisEvol(), forma
									.getMostrarEnviarEpicrisisEvol(), usuario,
							institucion);
			
			ValoresPorDefecto.modificar(
					con, ConstantesValoresPorDefecto.codigoValorDefectoMostrarGraficaCalculoIndicePlaca, 
					forma.getMostrarGraficaCalculoIndicePlaca(), forma.getMostrarGraficaCalculoIndicePlaca(), usuario, institucion);
			
			
			ValoresPorDefecto.modificar(
					con, ConstantesValoresPorDefecto.codigoValoresDefectoMaximoDiasConsultarIngresosHistoriaClinica, 
					forma.getMaximoDiasIngresosAConsultar(), forma.getMaximoDiasIngresosAConsultar(), usuario, institucion);
			
			
			
			
			
			
			try {
				
				ValoresPorDefecto
				.modificar(
						con,
						ConstantesValoresPorDefecto.codigoOcupacionOdontologo,
						forma.getOcupacionOdontologo().split("@@")[1], forma
								.getOcupacionOdontologo().split("@@")[0], usuario,
							institucion);
		
				ValoresPorDefecto
					.modificar(
					con,
						ConstantesValoresPorDefecto.codigoOcupacionAuxiliarOdontologo,
						forma.getOcupacionAuxiliarOdontologo().split("@@")[1], forma
								.getOcupacionAuxiliarOdontologo().split("@@")[0], usuario,
						institucion);
				
			} catch (Exception e) {
				Log4JManager.error("codigoOcupacionOdontologo - codigoOcupacionAuxiliarOdontologo son Nulos");
			}
			
			
			break;
		/******************************************************************
		 * ORDENES
		 ******************************************************************/
		case ConstantesBD.codigoModuloOrdenes:
			ValoresPorDefecto
					.modificar(
							con,
							ConstantesValoresPorDefecto.codigoValoresDefectoJustificacionServiciosRequerida,
							forma.getJustificacionServiciosRequerida(), forma
									.getJustificacionServiciosRequerida(),
							usuario, institucion);
			ValoresPorDefecto.modificar(con,
					ConstantesValoresPorDefecto.codigoValoresDefectoOcupacionSolicitada, forma
							.getOcupacionSolicitada().split("@@")[1], forma
							.getOcupacionSolicitada().split("@@")[0], usuario,
					institucion);
			ValoresPorDefecto
					.modificar(
							con,
							ConstantesValoresPorDefecto.codigoValoresDefectoIngresoCantidadSolMedicamentos,
							forma.getIngresoCantidadSolMedicamentos(), forma
									.getIngresoCantidadSolMedicamentos(),
							usuario, institucion);
			ValoresPorDefecto
					.modificar(
							con,
							ConstantesValoresPorDefecto.codigoValoresDefectoNumDiasTratamientoMedicamentos,
							forma.getNumDiasTratamientoMedicamentos(), forma
									.getNumDiasTratamientoMedicamentos(),
							usuario, institucion);
			ValoresPorDefecto
					.modificar(
							con,
							ConstantesValoresPorDefecto.codigoValoresDefectoNumDiasGenerarOrdenesArticulos,
							forma.getNumDiasGenerarOrdenesArticulos(), forma
									.getNumDiasGenerarOrdenesArticulos(),
							usuario, institucion);
			ValoresPorDefecto
					.modificar(
							con,
							ConstantesValoresPorDefecto.codigoValoresDefectoNumDiasEgresoOrdenesAmbulatorias,
							forma.getNumDiasEgresoOrdenesAmbulatorias(), forma
									.getNumDiasEgresoOrdenesAmbulatorias(),
							usuario, institucion);
			ValoresPorDefecto
					.modificar(
							con,
							ConstantesValoresPorDefecto.codigoValoresDefectoNumeroDiasControMedOrdenados,
							forma.getNumeroDiasControlMedicamentosOrdenados(),
							forma.getNumeroDiasControlMedicamentosOrdenados(),
							usuario, institucion);
			ValoresPorDefecto
					.modificar(
							con,
							ConstantesValoresPorDefecto.codigoValidarRegistroEvolucionesParaGenerarOrdenes,
							forma.getValidarRegistroEvolucionGenerarOrdenes(),
							forma.getValidarRegistroEvolucionGenerarOrdenes(),
							usuario, institucion);
			ValoresPorDefecto
					.modificar(
							con,
							ConstantesValoresPorDefecto.codigoValoresDefectoRequeridoComentariosSolicitar,
							forma.getRequeridoComentariosSolicitar(), forma
									.getRequeridoComentariosSolicitar(),
							usuario, institucion);
			ValoresPorDefecto
					.modificar(
							con,
							ConstantesValoresPorDefecto.codigoValoresDefectoPermIntOrdRespMulSinFin,
							forma.getPermIntOrdRespMulSinFin(), forma
									.getPermIntOrdRespMulSinFin(), usuario,
									institucion);
			ValoresPorDefecto
					.modificar(
							con,
							ConstantesValoresPorDefecto.codigonumeroMaximoDiasGenOrdenesAmbServicios,
							forma.getNumeroMaximoDiasGenOrdenesAmbServicios(), forma
									.getNumeroMaximoDiasGenOrdenesAmbServicios(), usuario,
							institucion);

			ValoresPorDefecto.modificar(con, ConstantesValoresPorDefecto.codigoValoresDefectoPermitirOrdenarMedicamentosPacienteUrgenciasConEgreso, forma.getPermitirOrdenarMedicamentosPacienteUrgenciasConEgreso(), forma.getPermitirOrdenarMedicamentosPacienteUrgenciasConEgreso(), usuario, institucion);
			
			ValoresPorDefecto.modificar(con,
					ConstantesValoresPorDefecto.codigoObligarRegIncapaPacienteHospitalizado,
					forma.getObligarRegIncapaPacienteHospitalizado(), forma
							.getObligarRegIncapaPacienteHospitalizado(), usuario, institucion);
					// Se guarda la estructura de centro costo terceros
			ValoresPorDefecto.insertarCentrosCostoTercero(con, forma
					.getCentroCostoTerceros());
			ValoresPorDefecto.insertarHorasReproceso(con, forma
					.getHorasReproceso());
			
			ValoresPorDefecto.modificar(con,
					ConstantesValoresPorDefecto.codigoValoresDefectoActivarBotonGenerarSolicitudOrdenAmbulatora,
					forma.getActivarBotonGenerarSolicitudOrdenAmbulatora(), forma
							.getActivarBotonGenerarSolicitudOrdenAmbulatora(), usuario, institucion);						
			break;
		/******************************************************************
		 * CARTERA
		 ******************************************************************/
		case ConstantesBD.codigoModuloCartera:
			ValoresPorDefecto.modificar(con,
					ConstantesValoresPorDefecto.codigoValoresDefectoFechaCorteSaldoInicialC,
					forma.getFechaCorteSaldoInicial(), forma
							.getFechaCorteSaldoInicial(), usuario, institucion);
			ValoresPorDefecto
					.modificar(
							con,
							ConstantesValoresPorDefecto.codigoValoresDefectoAjustarCuentaCobroRadicada,
							forma.getAjustarCuentaCobroRadicada(), forma
									.getAjustarCuentaCobroRadicada(), usuario,
							institucion);
			ValoresPorDefecto.modificar(con,
					ConstantesValoresPorDefecto.codigoValoresDefectoRipsPorFactura, forma
							.getRipsPorFactura(), forma.getRipsPorFactura(),
					usuario, institucion);
			ValoresPorDefecto.modificar(con,
					ConstantesValoresPorDefecto.codigoValoresDefectoTopeConsecutivoCxCSaldoI,
					forma.getTopeConsecutivoCxC(), forma
							.getTopeConsecutivoCxC(), usuario, institucion);
			ValoresPorDefecto.modificar(con,
					ConstantesValoresPorDefecto.codigoValoresDefectoConfimraAjustePool, forma
							.getConfirmarAjustesPooles(), forma
							.getConfirmarAjustesPooles(), usuario, institucion);
			
			
			//Anexo 922
			ValoresPorDefecto.modificar(con,
					ConstantesValoresPorDefecto.codigoValoresDefectoImprimirFirmasEnImpresionCC,
					forma.getImprimirFirmasEnImpresionCC(), forma
							.getImprimirFirmasEnImpresionCC(), usuario, institucion);
			
			ValoresPorDefecto.modificar(con,
					ConstantesValoresPorDefecto.codigoValoresDefectoNumeroMesesAMostrarEnReportesPresupuestoCapitacion,
					forma.getNumeroMesesAMostrarEnReportesPresupuestoCapitacion(), forma
							.getNumeroMesesAMostrarEnReportesPresupuestoCapitacion(), usuario, institucion);
			
			//Fin Anexo 992
			
			break;
		/******************************************************************
		 * SALAS DE CIRUGIA
		 ******************************************************************/
		case ConstantesBD.codigoModuloSalasCirugia:
			ValoresPorDefecto
					.modificar(
							con,
							ConstantesValoresPorDefecto.codigoValoresDefectoEspecialidadAnestesiologia,
							forma.getCodigoEspecialidadAnestesiologia().split(
									"@@")[1], forma
									.getCodigoEspecialidadAnestesiologia()
									.split("@@")[0], usuario, institucion);
			ValoresPorDefecto
					.modificar(
							con,
							ConstantesValoresPorDefecto.codigoValoresDefectoHoraInicioProgramacionSalas,
							forma.getHoraInicioProgramacionSalas(), forma
									.getHoraInicioProgramacionSalas(), usuario,
							institucion);
			ValoresPorDefecto.modificar(con,
					ConstantesValoresPorDefecto.codigoValoresDefectoHoraFinProgramacionSalas,
					forma.getHoraFinProgramacionSalas(), forma
							.getHoraFinProgramacionSalas(), usuario,
					institucion);
			ValoresPorDefecto.modificar(con,
					ConstantesValoresPorDefecto.codigoValoresDefectoMaterialesPorActo, forma
							.getMaterialesPorActo(), forma
							.getMaterialesPorActo(), usuario, institucion);
			ValoresPorDefecto
					.modificar(
							con,
							ConstantesValoresPorDefecto.codigoValoresDefectoLiquidacionAutomaticaCirugias,
							forma.getLiquidacionAutomaticaCirugias(), forma
									.getLiquidacionAutomaticaCirugias(),
							usuario, institucion);
			ValoresPorDefecto.modificar(con,
					ConstantesValoresPorDefecto.codigoValoresDefectoLiquidacionAutomaticaNoQx,
					forma.getLiquidacionAutomaticaNoQx(), forma
							.getLiquidacionAutomaticaNoQx(), usuario,
					institucion);
			ValoresPorDefecto
					.modificar(
							con,
							ConstantesValoresPorDefecto.codigoValoresDefectoManejoProgramacionSalasSolicitudes,
							forma.getManejoProgramacionSalasSolicitudesDyt(),
							forma.getManejoProgramacionSalasSolicitudesDyt(),
							usuario, institucion);
			ValoresPorDefecto
					.modificar(
							con,
							ConstantesValoresPorDefecto.codigoValoresDefectoRequridaDescripcionEspecialidadCirugias,
							forma.getRequeridaDescripcionEspecialidadCirugias(),
							forma.getRequeridaDescripcionEspecialidadCirugias(),
							usuario, institucion);
			ValoresPorDefecto
					.modificar(
							con,
							ConstantesValoresPorDefecto.codigoValoresDefectoRequridaDescripcionEspecialidadNoCruentos,
							forma
									.getRequeridaDescripcionEspecialidadNoCruentos(),
							forma
									.getRequeridaDescripcionEspecialidadNoCruentos(),
							usuario, institucion);
			ValoresPorDefecto
					.modificar(
							con,
							ConstantesValoresPorDefecto.codigoValoresDefectoIndicativoCobrableHonorariosCirugia,
							forma.getIndicativoCobrableHonorariosCirugia(),
							forma.getIndicativoCobrableHonorariosCirugia(),
							usuario, institucion);
			ValoresPorDefecto
					.modificar(
							con,
							ConstantesValoresPorDefecto.codigoValoresDefectoIndicativoCobrableHonorariosNoCruento,
							forma.getIndicativoCobrableHonorariosNoCruento(),
							forma.getIndicativoCobrableHonorariosNoCruento(),
							usuario, institucion);
			ValoresPorDefecto
					.modificar(
							con,
							ConstantesValoresPorDefecto.codigoValoresDefectoModificarInformacionDescripcionQuirurgicaiDiagnosticos,
							forma
									.getModificarInformacionDescripcionQuirurgica(),
							forma
									.getModificarInformacionDescripcionQuirurgica(),
							usuario, institucion);
			ValoresPorDefecto
					.modificar(
							con,
							ConstantesValoresPorDefecto.codigoValoresDefectoMinutosRegistroNotasCirugia,
							forma.getMinutosRegistroNotasCirugia(), forma
									.getMinutosRegistroNotasCirugia(), usuario,
							institucion);
			ValoresPorDefecto
					.modificar(
							con,
							ConstantesValoresPorDefecto.codigoValoresDefectoMinutosRegistroNotasNoCruentos,
							forma.getMinutosRegistroNotasNoCruentos(), forma
									.getMinutosRegistroNotasNoCruentos(),
							usuario, institucion);
			ValoresPorDefecto
					.modificar(
							con,
							ConstantesValoresPorDefecto.codigoValoresDefectoModificarInformacionQuirurgica,
							forma.getModificarInformacionQuirurgica(), forma
									.getModificarInformacionQuirurgica(),
							usuario, institucion);
			ValoresPorDefecto
					.modificar(
							con,
							ConstantesValoresPorDefecto.codigoValoresDefectoMinutosMaximosRegistroAnestesia,
							forma.getMinutosMaximosRegistroAnestesia(), forma
									.getMinutosMaximosRegistroAnestesia(),
							usuario, institucion);
			if (UtilidadCadena.noEsVacio(forma.getAsocioAyudantia()))
				ValoresPorDefecto.modificar(con,
						ConstantesValoresPorDefecto.codigoValoresDefectoAsocioAyudantia, forma
								.getAsocioAyudantia().split("@@")[1], forma
								.getAsocioAyudantia().split("@@")[0], usuario,
						institucion);
			else
				ValoresPorDefecto.modificar(con,
						ConstantesValoresPorDefecto.codigoValoresDefectoAsocioAyudantia, forma
								.getAsocioAyudantia(), forma
								.getAsocioAyudantia(), usuario, institucion);

			if (UtilidadCadena.noEsVacio(forma.getAsocioCirujano()))
				ValoresPorDefecto.modificar(con,
						ConstantesValoresPorDefecto.codigoValoresDefectoAsocioCirujano, forma
								.getAsocioCirujano().split("@@")[1], forma
								.getAsocioCirujano().split("@@")[0], usuario,
						institucion);
			else
				ValoresPorDefecto.modificar(con,
						ConstantesValoresPorDefecto.codigoValoresDefectoAsocioCirujano, forma
								.getAsocioCirujano(),
						forma.getAsocioCirujano(), usuario, institucion);

			if (UtilidadCadena.noEsVacio(forma.getAsocioAnestesia()))
				ValoresPorDefecto.modificar(con,
						ConstantesValoresPorDefecto.codigoValoresDefectoAsocioAnestesia, forma
								.getAsocioAnestesia().split("@@")[1], forma
								.getAsocioAnestesia().split("@@")[0], usuario,
						institucion);
			else
				ValoresPorDefecto.modificar(con,
						ConstantesValoresPorDefecto.codigoValoresDefectoAsocioAnestesia, forma
								.getAsocioAnestesia(), forma
								.getAsocioAnestesia(), usuario, institucion);
			ValoresPorDefecto
					.modificar(
							con,
							ConstantesValoresPorDefecto.codigoValoresDefectoPostularFechasEnRespuestasDyT,
							forma.getPostularFechasEnRespuestasDyT(), forma
									.getPostularFechasEnRespuestasDyT(),
							usuario, institucion);
			ValoresPorDefecto.insertarClasesInventariosPaqMatQx(con, forma
					.getClasesInventariosPaqMatQx());
			
			ValoresPorDefecto.modificar(con,ConstantesValoresPorDefecto.codigoValoresDefectoManejaHojaAnestesia, forma.getManejaHojaAnestesia(), forma.getManejaHojaAnestesia(), usuario,institucion);

			
			break;
		/******************************************************************
		 * INVENTARIOS
		 ******************************************************************/
		case ConstantesBD.codigoModuloInventarios:
			ValoresPorDefecto
					.modificar(
							con,
							ConstantesValoresPorDefecto.codigoValoresDefectoIngresoCantidadFarmacia,
							forma.getIngresoCantidadFarmacia(), forma
									.getIngresoCantidadFarmacia(), usuario,
							institucion);
			ValoresPorDefecto.modificar(con,
					ConstantesValoresPorDefecto.codigoValoresDefectoManejoConsecutivoTransInv,
					forma.getManejoConsecTransInv(), forma
							.getManejoConsecTransInv(), usuario, institucion);
			ValoresPorDefecto.modificar(con,
					ConstantesValoresPorDefecto.codigoValoresDefectoPorcentajeAlertaCostosInv,
					forma.getPorcentajesCostoInv(), forma
							.getPorcentajesCostoInv(), usuario, institucion);
			ValoresPorDefecto.modificar(con,
					ConstantesValoresPorDefecto.codigoValoresDefectoCodigoTransSoliPacientes,
					forma.getCodigoTransSolicPacientes().split("@@")[1], forma
							.getCodigoTransSolicPacientes().split("@@")[0],
					usuario, institucion);
			ValoresPorDefecto
					.modificar(
							con,
							ConstantesValoresPorDefecto.codigoValoresDefectoCodigoTransDevolucionesPaciente,
							forma.getCodigoTransDevolPacientes().split("@@")[1],
							forma.getCodigoTransDevolPacientes().split("@@")[0],
							usuario, institucion);
			ValoresPorDefecto.modificar(con,
					ConstantesValoresPorDefecto.codigoValoresDefectoCodigoTransPedidos, forma
							.getCodigoTransPedidos().split("@@")[1], forma
							.getCodigoTransPedidos().split("@@")[0], usuario,
					institucion);
			ValoresPorDefecto
					.modificar(
							con,
							ConstantesValoresPorDefecto.codigoValoresDefectoCodigoTransDevolucionesPedidos,
							forma.getCodigoTransDvolPedidos().split("@@")[1],
							forma.getCodigoTransDvolPedidos().split("@@")[0],
							usuario, institucion);
			ValoresPorDefecto
					.modificar(
							con,
							ConstantesValoresPorDefecto.codigoValoresDefectoModificarFechaaInventarios,
							forma.getPermitirModificarFechaInv(), forma
									.getPermitirModificarFechaInv(), usuario,
							institucion);
			ValoresPorDefecto.modificar(con,
					ConstantesValoresPorDefecto.codigoValoresDefectoPorcentajePuntoPedido,
					forma.getPorcentajePuntoPedido(), forma
							.getPorcentajePuntoPedido(), usuario, institucion);
			ValoresPorDefecto.modificar(con,
					ConstantesValoresPorDefecto.codigoValoresDefectoDiasAlertaVigencia, forma
							.getDiasAlertaVigencia(), forma
							.getDiasAlertaVigencia(), usuario, institucion);
			ValoresPorDefecto
					.modificar(
							con,
							ConstantesValoresPorDefecto.codigoValoresDefectoCodigoTransTrasladoAlmacenes,
							forma.getCodigoTransTrasladoAlmacenes().split("@@")[1],
							forma.getCodigoTransTrasladoAlmacenes().split("@@")[0],
							usuario, institucion);
			ValoresPorDefecto.modificar(con,
					ConstantesValoresPorDefecto.codigoValoresDefectoCodigoTransCompras, forma
							.getCodigoTransCompras().split("@@")[1], forma
							.getCodigoTransCompras().split("@@")[0], usuario,
					institucion);
			ValoresPorDefecto
					.modificar(
							con,
							ConstantesValoresPorDefecto.codigoValoresDefectoCodigoTransDevolucionCompras,
							forma.getCodigoTransDevolCompras().split("@@")[1],
							forma.getCodigoTransDevolCompras().split("@@")[0],
							usuario, institucion);
			// Se evalï¿½a si el parï¿½metro Path Archivo Plano viene con ï¿½ sin el
			// Eslas para concatenarselo
			if (forma.getArchivosPlanosReportesInventarios().endsWith("/"))
				ValoresPorDefecto.modificar(con,
						ConstantesValoresPorDefecto.codigoValoresDefectoPathInventarios, forma
								.getArchivosPlanosReportesInventarios(), forma
								.getArchivosPlanosReportesInventarios(),
						usuario, institucion);
			else {
				forma.setArchivosPlanosReportesInventarios(forma
						.getArchivosPlanosReportesInventarios()
						+ "/");
				ValoresPorDefecto.modificar(con,
						ConstantesValoresPorDefecto.codigoValoresDefectoPathInventarios, forma
								.getArchivosPlanosReportesInventarios(), forma
								.getArchivosPlanosReportesInventarios(),
						usuario, institucion);
			}
			ValoresPorDefecto
					.modificar(
							con,
							ConstantesValoresPorDefecto.codigoValoresDefectoConteosValidosAjustarInvFisico,
							forma.getConteosValidosAjustarInventarioFisico(),
							forma.getConteosValidosAjustarInventarioFisico(),
							usuario, institucion);
			ValoresPorDefecto
					.modificar(
							con,
							ConstantesValoresPorDefecto.codigoValoresDefectoEntidadControlaDespachoSaldosMultidosis,
							forma.getEntidadControlaDespachoSaldosMultidosis(),
							forma.getEntidadControlaDespachoSaldosMultidosis(),
							usuario, institucion);
			ValoresPorDefecto.modificar(con,
					ConstantesValoresPorDefecto.codigoValoresDefectoConceptoParaAjusteEntrada,
					forma.getConceptoParaAjusteEntrada().split("@@")[1], forma
							.getConceptoParaAjusteEntrada().split("@@")[0],
					usuario, institucion);
			ValoresPorDefecto.modificar(con,
					ConstantesValoresPorDefecto.codigoValoresDefectoConceptoParaAjusteSalida,
					forma.getConceptoParaAjusteSalida().split("@@")[1], forma
							.getConceptoParaAjusteSalida().split("@@")[0],
					usuario, institucion);
			ValoresPorDefecto
					.modificar(
							con,
							ConstantesValoresPorDefecto.codigoValoresDefectoPermitirModificarConceptosAjuste,
							forma.getPermitirModificarConceptosAjuste(), forma
									.getPermitirModificarConceptosAjuste(),
							usuario, institucion);
			ValoresPorDefecto
					.modificar(
							con,
							ConstantesValoresPorDefecto.codigoValoresDefectoPermitirModificarFechaSolicitudPedidos,
							forma.getPermitirModificarFechaSolicitudPedidos(),
							forma.getPermitirModificarFechaSolicitudPedidos(),
							usuario, institucion);
			ValoresPorDefecto
					.modificar(
							con,
							ConstantesValoresPorDefecto.codigoValoresDefectoPermitirModificarFechaSolicitudTraslado,
							forma.getPermitirModificarFechaSolicitudTraslado(),
							forma.getPermitirModificarFechaSolicitudTraslado(),
							usuario, institucion);
			break;
		/******************************************************************
		 * ENFERMERï¿½A
		 ******************************************************************/
		case ConstantesBD.codigoModuloEnfermeria:
			ValoresPorDefecto.modificar(con,
					ConstantesValoresPorDefecto.codigoValoresDefectoHoraInicioPrimerTurno,
					forma.getHoraInicioPrimerTurno(), forma
							.getHoraInicioPrimerTurno(), usuario, institucion);
			ValoresPorDefecto.modificar(con,
					ConstantesValoresPorDefecto.codigoValoresDefectoHoraFinUltimoTurno, forma
							.getHoraFinUltimoTurno(), forma
							.getHoraFinUltimoTurno(), usuario, institucion);
			ValoresPorDefecto.modificar(con,
					ConstantesValoresPorDefecto.codigoValoresDefectoTiempoMaximoGrabacion,
					forma.getTiempoMaximoGrabacion(), forma
							.getTiempoMaximoGrabacion(), usuario, institucion);
			
			ValoresPorDefecto.modificar(con, ConstantesValoresPorDefecto.codigoValoresDefectoMostrarAdminMedicamentosArticulosDespachoCero, forma.getMostrarAdminMedicamentosArticulosDespachoCero(), forma.getMostrarAdminMedicamentosArticulosDespachoCero(), usuario, institucion);
			
			break;
		/******************************************************************
		 * CAPITACIï¿½N
		 ******************************************************************/
		case ConstantesBD.codigoModuloCapitacion:
			ValoresPorDefecto.modificar(con,
					ConstantesValoresPorDefecto.codigoValoresDefectoTipoConsecutivoCapitacion,
					forma.getTipoConsecutivoCapitacion().split("@@")[1], forma
							.getTipoConsecutivoCapitacion().split("@@")[0],
					usuario, institucion);
			ValoresPorDefecto
					.modificar(
							con,
							ConstantesValoresPorDefecto.codigoValoresDefectoFechaCorteSaldoInicialCCapitacion,
							forma.getFechaCorteSaldoInicialCapitacion(), forma
									.getFechaCorteSaldoInicialCapitacion(),
							usuario, institucion);
			ValoresPorDefecto
					.modificar(
							con,
							ConstantesValoresPorDefecto.codigoValoresDefectoTopeConsecutivoCxCSaldoICapitacion,
							forma.getTopeConsecutivoCxCCapitacion(), forma
									.getTopeConsecutivoCxCCapitacion(),
							usuario, institucion);
			
			//Anexo 922
			ValoresPorDefecto
			.modificar(
					con,
					ConstantesValoresPorDefecto.codigoValoresDefectoImprimirFirmasImpresionCCCapitacion,
					forma.getImprimirFirmasImpresionCCCapitacion(), forma
							.getImprimirFirmasImpresionCCCapitacion(),
					usuario, institucion);
			ValoresPorDefecto
			.modificar(
					con,
					ConstantesValoresPorDefecto.codigoValoresDefectoEncabezadoFormatoImpresionFacturaOCCCapitacion,
					forma.getEncabezadoFormatoImpresionFacturaOCCCapitacion(), forma
							.getEncabezadoFormatoImpresionFacturaOCCCapitacion(),
					usuario, institucion);
			ValoresPorDefecto
			.modificar(
					con,
					ConstantesValoresPorDefecto.codigoValoresDefectoPiePaginaFormatoImpresionFacturaOCCCapitacion,
					forma.getPiePaginaFormatoImpresionFacturaOCCCapitacion(), forma
							.getPiePaginaFormatoImpresionFacturaOCCCapitacion(),
					usuario, institucion);
			
			ValoresPorDefecto.modificar(con,
					ConstantesValoresPorDefecto.codigoValoresDefectoHoraProcesoCierreCapitacion, forma
							.getHoraProcesoCierreCapitacion(), forma
							.getHoraProcesoCierreCapitacion(), usuario, institucion);
			
			ValoresPorDefecto.modificar(con,
					ConstantesValoresPorDefecto.codigoValoresDefectoEsquemaTariServiciosValorizarOrden, 
					forma.getEsquemaTariServiciosValorizarOrden().split("-")[1], 
					forma.getEsquemaTariServiciosValorizarOrden(), 
					usuario, institucion);
			
			ValoresPorDefecto.modificar(con,
					ConstantesValoresPorDefecto.codigoValoresDefectoEsquemaTariMedicamentosValorizarOrden, 
					forma.getEsquemaTariMedicamentosValorizarOrden().split("-")[1], 
					forma.getEsquemaTariMedicamentosValorizarOrden(), 
					usuario, institucion);
			
			//Anexo 922
			ValoresPorDefecto.modificar(con,
					ConstantesValoresPorDefecto.codigoValoresDefectoHacerRequeridaSeleccionCentroAtencionAsignadoSubirPacienteIndividual, 
					forma.getHacerRequeridaSeleccionCentroAtencionAsignadoSubirPacienteIndividual().split("-")[0], 
					forma.getHacerRequeridaSeleccionCentroAtencionAsignadoSubirPacienteIndividual(), 
					usuario, institucion);
			
			break;

		/* *****************************************************************
		 * INTERFAZ
		 ******************************************************************/
		case ConstantesBD.codigoModuloInterfaz:
			ValoresPorDefecto.modificar(con,
					ConstantesValoresPorDefecto.codigoValoresDefectoInterfazPaciente, forma
							.getInterfazPacientes(), forma
							.getInterfazPacientes(), usuario, institucion);
			ValoresPorDefecto
					.modificar(
							con,
							ConstantesValoresPorDefecto.codigoValoresDefectoInterfazAbonosTesoreria,
							forma.getInterfazAbonosTesoreria(), forma
									.getInterfazAbonosTesoreria(), usuario,
							institucion);
			ValoresPorDefecto.modificar(con,
					ConstantesValoresPorDefecto.codigoValoresDefectoInterfazCompras, forma
							.getInterfazCompras(), forma.getInterfazCompras(),
					usuario, institucion);
			ValoresPorDefecto.modificar(con,
					ConstantesValoresPorDefecto.codigoValoresDefectoArticuloInventario, forma
							.getArticuloInventario(), forma
							.getArticuloInventario(), usuario, institucion);
			ValoresPorDefecto.modificar(con,
					ConstantesValoresPorDefecto.codigoValoresDefectoLoginUsuario, forma
							.getLoginUsuario(), forma.getLoginUsuario(),
					usuario, institucion);
			ValoresPorDefecto.modificar(con,
					ConstantesValoresPorDefecto.codigoValoresDefectoInterfazContableFacturas,
					forma.getInterfazContableFacturas(), forma
							.getInterfazContableFacturas(), usuario,
					institucion);
			ValoresPorDefecto.modificar(con,
					ConstantesValoresPorDefecto.codigoValoresDefectoInterfazTerceros, forma
							.getInterfazTerceros(),
					forma.getInterfazTerceros(), usuario, institucion);
			ValoresPorDefecto.modificar(con,
					ConstantesValoresPorDefecto.codigoValoresDefectoInterfazCarteraPacientes,
					forma.getInterfazCarteraPacientes(), forma
							.getInterfazCarteraPacientes(), usuario,
					institucion);
			ValoresPorDefecto.modificar(con,
					ConstantesValoresPorDefecto.codigoValoresDefectoInterfazRips, forma
							.getInterfazRips(), forma.getInterfazRips(),
					usuario, institucion);
			ValoresPorDefecto
					.modificar(
							con,
							ConstantesValoresPorDefecto.codigoValoresDefectoInterfazContableRecibosCajaERP,
							forma.getInterfazContableRecibosCajaERP(), forma
									.getInterfazContableRecibosCajaERP(),
							usuario, institucion);
			ValoresPorDefecto
					.modificar(
							con,
							ConstantesValoresPorDefecto.codigoValoresDefectoManejoInterfazConsecutivoFacturasOtroSistema,
							forma.getInterfazConsecutivoFacturasOtroSistema(),
							forma.getInterfazConsecutivoFacturasOtroSistema(),
							usuario, institucion);
			ValoresPorDefecto.modificar(con,
					ConstantesValoresPorDefecto.codigoValoresDefectoInterfazNutricion, forma
							.getInterfazNutricion(), forma
							.getInterfazNutricion(), usuario, institucion);
			ValoresPorDefecto
					.modificar(
							con,
							ConstantesValoresPorDefecto.codigoValoresDefectoProduccionEnParaleloConSistemaAnterior,
							forma.getProduccionEnParaleloConSistemaAnterior(),
							forma.getProduccionEnParaleloConSistemaAnterior(),
							usuario, institucion);
			ValoresPorDefecto.modificar(con,
					ConstantesValoresPorDefecto.codigoValoresDefectoTiemSegVeriInterShaioProc,
					forma.getTiemSegVeriInterShaioProc(), forma
							.getTiemSegVeriInterShaioProc(), usuario,
					institucion);

			break;

		/* *****************************************************************
		 * EPIDEMIOLOGÍA
		 ******************************************************************/
		case ConstantesBD.codigoModuloEpidemiologia:
			ValoresPorDefecto.modificar(con,
					ConstantesValoresPorDefecto.codigoValoresDefectoAlertaFichasPendientes,
					forma.getAlertaFichasPendientes(), forma
							.getAlertaFichasPendientes(), usuario, institucion);
			ValoresPorDefecto.modificar(con,
					ConstantesValoresPorDefecto.codigoValoresDefectoAlertaCasoVigilancia,
					forma.getAlertaCasoVigilancia(), forma
							.getAlertaCasoVigilancia(), usuario, institucion);
			ValoresPorDefecto.modificar(con,
					ConstantesValoresPorDefecto.codigoValoresDefectoVigilarAccRabico, forma
							.getVigilarAccRabico(),
					forma.getVigilarAccRabico(), usuario, institucion);
			break;

		/* *****************************************************************
		 * FACTURAS VARIAS
		 ******************************************************************/
		case ConstantesBD.codigoModuloFacturasVarias:
			ValoresPorDefecto.modificar(con,
					ConstantesValoresPorDefecto.codigoValoresDefectoTipoConsecutivoManejar,
					forma.getTipoConsecutivoManejar(), forma
							.getTipoConsecutivoManejar(), usuario, institucion);
			ValoresPorDefecto.modificar(con,
					ConstantesValoresPorDefecto.codigoValoresDefectoReciboCajaAutomaticoGeneracionFacturaVaria,
					forma.getReciboCajaAutomaticoGeneracionFacturaVaria(), forma
							.getReciboCajaAutomaticoGeneracionFacturaVaria(), usuario, institucion);
			ValoresPorDefecto.modificar(con,
					ConstantesValoresPorDefecto.codigoValoresDefectoConceptoIngresoFacturasVarias,
					forma.getConceptoIngresoFacturasVarias(), forma
							.getConceptoIngresoFacturasVarias(), usuario, institucion);
			ValoresPorDefecto.modificar(con,
					ConstantesValoresPorDefecto.codigoValoresDefectoFormatoFacturaVaria,
					forma.getFormatoFacturaVaria(), forma
							.getFormatoFacturaVaria(), usuario, institucion);
			
			ValoresPorDefecto.modificar(con,
					ConstantesValoresPorDefecto.codigoValoresDefectoManejaConsecutivoFacturasVariasPorCentroAtencion,
					forma.getManejaConsecutivoFacturasVariasPorCentroAtencion(), forma
							.getManejaConsecutivoFacturasVariasPorCentroAtencion(), usuario, institucion);			
			break;


		/* *****************************************************************
		 * TESORERIA
		 ******************************************************************/
		case ConstantesBD.codigoModuloTesoreria:
			ValoresPorDefecto.modificar(con,
					ConstantesValoresPorDefecto.codigoValoresDefectoFormaPagoEfectivo, forma
							.getFormaPagoEfectivo().split("@@")[1], forma
							.getFormaPagoEfectivo().split("@@")[0], usuario,
					institucion);
			
			ValoresPorDefecto
			.modificar(
					con,
					ConstantesValoresPorDefecto.codigoValoresDefectoManejaConsecutivosTesoreriaPorCentroAtencion,
					forma.getManejaConsecutivosTesoreriaPorCentroAtencion(), forma
							.getManejaConsecutivosTesoreriaPorCentroAtencion(),
					usuario, institucion);
			
			ValoresPorDefecto
			.modificar(
					con,
					ConstantesValoresPorDefecto.codigoValoresDefectoTamanioImpresionRC,
					forma.getTamanioImpresionRC(), forma
							.getTamanioImpresionRC(),
					usuario, institucion);
			
			ValoresPorDefecto
			.modificar(
					con,
					ConstantesValoresPorDefecto.codigoValoresDefectoRequiereAperturaCierreCaja,
					forma.getRequiereAperturaCierreCaja(), forma
							.getRequiereAperturaCierreCaja(),
					usuario, institucion);
			
			
			
			ValoresPorDefecto
			.modificar(
					con,
					ConstantesValoresPorDefecto.codigoValoresDefectoEsRequeridoTestigoSolicitudAceptacionTrasladoCaja,
					forma.getEsRequeridoTestigoSolicitudAceptacionTrasladoCaja(), forma
							.getEsRequeridoTestigoSolicitudAceptacionTrasladoCaja(),
					usuario, institucion);
			
			ValoresPorDefecto
			.modificar(
					con,
					ConstantesValoresPorDefecto.codigoValoresDefectoInstitucionManejaCajaPrincipal,
					forma.getInstitucionManejaCajaPrincipal(), forma
							.getInstitucionManejaCajaPrincipal(),
					usuario, institucion);

			ValoresPorDefecto
			.modificar(
					con,
					ConstantesValoresPorDefecto.codigoValoresDefectoInstitucionManejaTrasladoOtraCajaRecaudo,
					forma.getInstitucionManejaTrasladoOtraCajaRecaudo(), forma
							.getInstitucionManejaTrasladoOtraCajaRecaudo(),
					usuario, institucion);
			
			ValoresPorDefecto
			.modificar(
					con,
					ConstantesValoresPorDefecto.codigoValoresDefectoInstitucionManejaEntregaATransportadoraValores,
					forma.getInstitucionManejaEntregaATransportadoraValores(), forma
							.getInstitucionManejaEntregaATransportadoraValores(),
					usuario, institucion);
			
			ValoresPorDefecto
			.modificar(
					con,
					ConstantesValoresPorDefecto.codigoValoresDefectoTrasladoAbonosPacienteSoloPacientesConPresupuestoContratado,
					forma.getTrasladoAbonosPacienteSoloPacientesConPresupuestoContratado(), forma
							.getTrasladoAbonosPacienteSoloPacientesConPresupuestoContratado(),
					usuario, institucion);
			/* 
			 * Parámetro que estaba asociado al módulo de Facturación, pero que ahora se asocia al módulo 
			 * de Tesorería.
			 */
			ValoresPorDefecto.modificar(con,
					ConstantesValoresPorDefecto.codigoValoresDefectoControlarAbonoPacientePorNroIngreso, forma
							.getControlarAbonoPacientePorNroIngreso(), forma
							.getControlarAbonoPacientePorNroIngreso(), usuario, institucion);
			
			/* Cambio RQF-0138 Aplica SONRIA-VERSALLES */
			ValoresPorDefecto
					.modificar(con, 
							   ConstantesValoresPorDefecto.codigoValoresDefectoManejaConsecutivosNotasPacientesCentroAtencion, 
							   forma.getManejaConsecutivosNotasPacientesCentroAtencion(), 
							   forma.getManejaConsecutivosNotasPacientesCentroAtencion(), usuario, institucion);
			
			ValoresPorDefecto
					.modificar(con, 
							   ConstantesValoresPorDefecto.codigoValoresDefectoNaturalezaNotasPacientesManejar,
							   forma.getNaturalezaNotasPacientesManejar(), 
							   forma.getNaturalezaNotasPacientesManejar(), usuario, institucion);
			
			ValoresPorDefecto
			.modificar(con, 
					ConstantesValoresPorDefecto.codigoValoresDefectoPermitirRecaudosCajaMayor,
					forma.getPermitirRecaudosCajaMayor(), 
					forma.getPermitirRecaudosCajaMayor(), usuario, institucion);
			/* */
			
			break;

		/* *****************************************************************
		 * GLOSAS
		 ******************************************************************/
		case ConstantesBD.codigoModuloGlosas:
			ParametrosFirmasImpresionResp mundofirma = new ParametrosFirmasImpresionResp();

			ValoresPorDefecto
					.modificar(
							con,
							ConstantesValoresPorDefecto.codigoValoresDefectoGenerarAjusteAutoRegRespuesta,
							forma.getGenerarAjusteAutoRegRespuesta(), forma
									.getGenerarAjusteAutoRegRespuesta(),
							usuario, institucion);
			ValoresPorDefecto
					.modificar(
							con,
							ConstantesValoresPorDefecto.codigoValoresDefectoGenerarAjusteAutoRegRespuesConciliacion,
							forma.getGenerarAjusteAutoRegRespuesConciliacion(),
							forma.getGenerarAjusteAutoRegRespuesConciliacion(),
							usuario, institucion);
			ValoresPorDefecto
					.modificar(
							con,
							ConstantesValoresPorDefecto.codigoValoresDefectoFormatoImpresionRespuesGlosa,
							forma.getFormatoImpresionRespuesGlosa(), forma
									.getFormatoImpresionRespuesGlosa(),
							usuario, institucion);
			ValoresPorDefecto
				.modificar(
						con,
						ConstantesValoresPorDefecto.codigoFormatoImpresionConciliacion,
						forma.getFormatoImpresionConciliacion(), forma
								.getFormatoImpresionConciliacion(),
						usuario, institucion);
			ValoresPorDefecto
			.modificar(
					con,
					ConstantesValoresPorDefecto.codigoValidarGlosaReiterada,
					forma.getValidarGlosaReiterada(), forma
							.getValidarGlosaReiterada(),
					usuario, institucion);			
			ValoresPorDefecto.modificar(con,
					ConstantesValoresPorDefecto.codigoValoresDefectoNumeroDiasResponderGlosas,
					forma.getNumeroDiasResponderGlosas(), forma
							.getNumeroDiasResponderGlosas(), usuario,
					institucion);
			ValoresPorDefecto
					.modificar(
							con,
							ConstantesValoresPorDefecto.codigoValoresDefectoImprimirFirmasImpresionRespuesGlosa,
							forma.getImprimirFirmasImpresionRespuesGlosa(),
							forma.getImprimirFirmasImpresionRespuesGlosa(),
							usuario, institucion);

			ValoresPorDefecto.modificar(con,
					ConstantesValoresPorDefecto.codigoValoresDefectoValidarAuditor, forma
							.getValidarAuditor(), forma.getValidarAuditor(),
					usuario, institucion);
			ValoresPorDefecto.modificar(con,
					ConstantesValoresPorDefecto.codigoValoresDefectoValidarUsuarioGlosa, forma
							.getValidarUsuarioGlosa(), forma
							.getValidarUsuarioGlosa(), usuario, institucion);
			ValoresPorDefecto
					.modificar(
							con,
							ConstantesValoresPorDefecto.codigoValoresDefectoNumeroGlosasRegistradasXFactura,
							forma.getNumeroGlosasRegistradasXFactura(), forma
									.getNumeroGlosasRegistradasXFactura(),
							usuario, institucion);
			ValoresPorDefecto.modificar(con,
					ConstantesValoresPorDefecto.codigoValoresDefectoNumeroDiasNotificarGlosa,
					forma.getNumeroDiasNotificarGlosa(), forma
							.getNumeroDiasNotificarGlosa(), usuario,
					institucion);
			ValoresPorDefecto.modificar(con,
					ConstantesValoresPorDefecto.codigoValoresDefectoRequiereGlosaInactivar,
					forma.getRequiereGlosaInactivar(), forma
							.getRequiereGlosaInactivar(), usuario, institucion);
			
			ValoresPorDefecto.modificar(con,
					ConstantesValoresPorDefecto.codigoValoresDefectoAprobarGlosaRegistro,
					forma.getAprobarGlosaRegistro(), forma
							.getAprobarGlosaRegistro(), usuario, institucion);
			
			ValoresPorDefecto.modificar(con,
					ConstantesValoresPorDefecto.codigoValoresDefectoMostrarDetalleGlosasFacturaSolicFactura,
					forma.getViaIngresoDetaleGlosaConcatenado(), 
					forma.getViaIngresoDetaleGlosaConcatenado(), usuario, institucion);


			if (forma.getImprimirFirmasImpresionRespuesGlosa().equals(
					ConstantesBD.acronimoNo)) {
				logger.info("============================");
				logger
						.info("Se escogio eliminar todas las firmas almacenadas en la BD");
				mundofirma.eliminarTodaFirmaImpresion(con);
			}
			break;
			
		/* *****************************************************************
		 * CARTERA PACIENTE
		 ******************************************************************/
		case ConstantesBD.codigoModuloCarteraPaciente:
				
				ValoresPorDefecto.modificar(con,
						ConstantesValoresPorDefecto.codigoMaximoNumeroCuotasFinanciacion,
						forma.getMaximoNumeroCuotasFinanciacion(), forma
								.getMaximoNumeroCuotasFinanciacion(), usuario, institucion);
				
				ValoresPorDefecto.modificar(con,
						ConstantesValoresPorDefecto.codigoMaximoNumeroDiasFinanciacionPorCuota,
						forma.getMaximoNumeroDiasFinanciacionPorCuota(), forma
								.getMaximoNumeroDiasFinanciacionPorCuota(), usuario, institucion);
				
				ValoresPorDefecto.modificar(con,
						ConstantesValoresPorDefecto.codigoFormatoDocumentosGarantia_Pagare,
						forma.getFormatoDocumentosGarantia_Pagare(), forma
								.getFormatoDocumentosGarantia_Pagare(), usuario, institucion);
				
				ValoresPorDefecto.modificar(
						con,
						ConstantesValoresPorDefecto.codigoValoresDefectoDiasParaDefinirMoraXDeudaPacientes,
						forma.getDiasParaDefinirMoraXDeudaPacientes(), 
						forma.getDiasParaDefinirMoraXDeudaPacientes(), 
						usuario, institucion);
				
				ValoresPorDefecto.modificar(
						con,
						ConstantesValoresPorDefecto.codigoValoresDefectoCuentaContableLetra,
						forma.getCuentaContableLetra(), 
						forma.getCuentaContableLetra(), 
						usuario, institucion);
				
				ValoresPorDefecto.modificar(
						con,
						ConstantesValoresPorDefecto.codigoValoresDefectoCuentaContablePagare,
						forma.getCuentaContablePagare(), 
						forma.getCuentaContablePagare(), 
						usuario, institucion);
				
				break;
				
		/* *****************************************************************
		 * ODONTOLOGÍA
		 ******************************************************************/
		case ConstantesBD.codigoModuloOdontologia:
				
				ValoresPorDefecto.modificar(con,
						ConstantesValoresPorDefecto.codigoEdadFinalNinez,
						forma.getEdadFinalNinez(), forma
								.getEdadFinalNinez(), usuario, institucion);
				
				ValoresPorDefecto.modificar(con,
						ConstantesValoresPorDefecto.codigoEdadInicioAdulto,
						forma.getEdadInicioAdulto(), forma
								.getEdadInicioAdulto(), usuario, institucion);
				
				ValoresPorDefecto
				.modificar(
						con,
						ConstantesValoresPorDefecto.codigoMultiploMinGeneracionCita,
						forma.getMultiploMinGeneracionCita(), forma
								.getMultiploMinGeneracionCita(), usuario,
						institucion);
				ValoresPorDefecto
				.modificar(
						con,
						ConstantesValoresPorDefecto.codigoNumDiasAntFActualAgendaOd,
						forma.getNumDiasAntFActualAgendaOd(), forma
								.getNumDiasAntFActualAgendaOd(), usuario,
						institucion);
				
				ValoresPorDefecto
				.modificar(
						con,
						ConstantesValoresPorDefecto.codigoValoresDefectoUtilizanProgramasOdontologicosEnInstitucion,
						forma.getUtilizanProgramasOdontologicosEnInstitucion(), forma
								.getUtilizanProgramasOdontologicosEnInstitucion(), usuario,
						institucion);
				
				ValoresPorDefecto.modificar(
						con,
						ConstantesValoresPorDefecto.codigoValoresDefectoTiempoVigenciaPresupuestoOdo,
						forma.getTiempoVigenciaPresupuestoOdo(), 
						forma.getTiempoVigenciaPresupuestoOdo(), 
						usuario, institucion);
				
				ValoresPorDefecto.modificar(
						con,
						ConstantesValoresPorDefecto.codigoValoresDefectoPermiteCambiarServiciosCitaAtencionOdo,
						forma.getPermiteCambiarServiciosCitaAtencionOdo(), 
						forma.getPermiteCambiarServiciosCitaAtencionOdo(), 
						usuario, institucion);
				
				ValoresPorDefecto.modificar(
						con,
						ConstantesValoresPorDefecto.codigoValoresDefectoValidaPresupuestoOdoContratado,
						forma.getValidaPresupuestoOdoContratado(), 
						forma.getValidaPresupuestoOdoContratado(), 
						usuario, institucion);
				
				Utilidades.actualizarConveniosAMostrarPresupuestoOdo(con, forma.getConveniosAMostrarPresupuestoOdo(), usuario, institucion);
				
				ValoresPorDefecto.modificar(
						con,
						ConstantesValoresPorDefecto.codigoValoresDefectoTiempoMaxEsperaInactivarPresupuestoOdoSuspTemp,
						forma.getTiempoMaxEsperaInactivarPresupuestoOdoSuspTemp(), 
						forma.getTiempoMaxEsperaInactivarPresupuestoOdoSuspTemp(), 
						usuario, institucion);
				
				ValoresPorDefecto.modificar(
						con,
						ConstantesValoresPorDefecto.codigoValoresDefectoEjecutarProcesoAutoActualizacionEstadosOdo,
						forma.getEjecutarProcesoAutoActualizacionEstadosOdo(), 
						forma.getEjecutarProcesoAutoActualizacionEstadosOdo(), 
						usuario, institucion);
				
				ValoresPorDefecto.modificar(
						con,
						ConstantesValoresPorDefecto.codigoValoresDefectoHoraEjecutarProcesoAutoActualizacionEstadosOdo,
						forma.getHoraEjecutarProcesoAutoActualizacionEstadosOdo(), 
						forma.getHoraEjecutarProcesoAutoActualizacionEstadosOdo(), 
						usuario, institucion);
				
				ValoresPorDefecto.modificar(
						con,
						ConstantesValoresPorDefecto.codigoValoresDefectoMotivoCancelacionPresupuestoSuspendidoTemp,
						forma.getMotivoCancelacionPresupuestoSuspendidoTemp(), 
						forma.getMotivoCancelacionPresupuestoSuspendidoTemp(), 
						usuario, institucion);
				
				ValoresPorDefecto.modificar(
						con,
						ConstantesValoresPorDefecto.codigoValoresDefectoMaximoTiempoSinEvolucionarParaInactivarPlanTratamiento,
						forma.getMaximoTiempoSinEvolucionarParaInactivarPlanTratamiento(), 
						forma.getMaximoTiempoSinEvolucionarParaInactivarPlanTratamiento(), 
						usuario, institucion);
				
				ValoresPorDefecto.modificar(
						con,
						ConstantesValoresPorDefecto.codigoValoresDefectoPrioridadParaAplicarPromocionesOdo,
						forma.getPrioridadParaAplicarPromocionesOdo(), 
						forma.getPrioridadParaAplicarPromocionesOdo(), 
						usuario, institucion);
				
				ValoresPorDefecto
				.modificar(
						con,
						ConstantesValoresPorDefecto.codigoValoresDefectoInstitucionRegistraAtencionExterna,
						forma.getInstitucionRegistraAtencionExterna(), forma
								.getInstitucionRegistraAtencionExterna(), usuario,
						institucion);
				
				//Anexo 888
				ValoresPorDefecto
				.modificar(
						con,
						ConstantesValoresPorDefecto.codigoValoresDefectoEsRequeridoProgramarCitaAlContratarPresupuestoOdon,
						forma.getEsRequeridoProgramarCitaAlContratarPresupuestoOdon(), forma
								.getEsRequeridoProgramarCitaAlContratarPresupuestoOdon(), usuario,
						institucion);
				
				ValoresPorDefecto
				.modificar(
						con,
						ConstantesValoresPorDefecto.codigoValoresDefectoMotivoAnulacionSolicitudAutorizacionDescuentoPresupeustoOdontologico,
						forma.getMotivoAnulacionSolicitudAutorizacionDescuentoPresupeustoOdontologico(), forma
								.getMotivoAnulacionSolicitudAutorizacionDescuentoPresupeustoOdontologico(), usuario,
						institucion);
				
				
				//Fin anexo 888
				
				//Anexo 888 Pt II
				ValoresPorDefecto
				.modificar(
						con,
						ConstantesValoresPorDefecto.codigoValoresDefectoRequeridoProgramarCitaControlAlTerminarPresupuestoOdon,
						forma.getRequeridoProgramarCitaControlAlTerminarPresupuestoOdon(), forma
								.getRequeridoProgramarCitaControlAlTerminarPresupuestoOdon(), usuario,
						institucion);
				
				ValoresPorDefecto
				.modificar(
						con,
						ConstantesValoresPorDefecto.codigoValoresDefectoRequeridoProgramarCitaControlAlTerminarPresupuestoOdonServicio,
						forma.getRequeridoProgramarCitaControlAlTerminarPresupuestoOdonServicio(), forma
								.getRequeridoProgramarCitaControlAlTerminarPresupuestoOdonServicio(), usuario,
						institucion);
				
				ValoresPorDefecto
				.modificar(
						con,
						ConstantesValoresPorDefecto.codigoValoresDefectoTiempoDiasParaAgendarCitaControlAlTerminarPresupuestoOdon,
						forma.getTiempoDiasParaAgendarCitaControlAlTerminarPresupuestoOdon(), forma
								.getTiempoDiasParaAgendarCitaControlAlTerminarPresupuestoOdon(), usuario,
						institucion);
				
				ValoresPorDefecto
				.modificar(
						con,
						ConstantesValoresPorDefecto.codigoValoresDefectoInstitucionManejaFacturacionAutomatica,
						forma.getInstitucionManejaFacturacionAutomatica(), forma
								.getInstitucionManejaFacturacionAutomatica(), usuario,
						institucion);
				
				//Fin  Anexo 888 Pt II
				
				//Los siguientes elementos se pasaron de administracion a odontologia por anexo 63
				ValoresPorDefecto
				.modificar(
						con,
						ConstantesValoresPorDefecto.codigoValoresDefectoMinutosCaducaCitaReservada,
						forma.getMinutosCaducaCitasReservadas(), 
						forma.getMinutosCaducaCitasReservadas(), 
						usuario, 
						institucion);
		
				ValoresPorDefecto
						.modificar(
								con,
								ConstantesValoresPorDefecto.codigoValoresDefectoMinutosCaducaCitaAsignadasReprogramadas,
								forma.getMinutosCaducaCitasAsignadasReprogramadas(), 
								forma.getMinutosCaducaCitasAsignadasReprogramadas(), 
								usuario, 
								institucion);
				
				ValoresPorDefecto
						.modificar(
								con,
								ConstantesValoresPorDefecto.codigoValoresDefectoEjecutarProcAutoActualizacionCitasOdo,
								forma.getEjecutarProcAutoActualizacionCitasOdoNoAsistio(), 
								forma.getEjecutarProcAutoActualizacionCitasOdoNoAsistio(), 
								usuario, 
								institucion);
				
				ValoresPorDefecto
					.modificar(
							con,
							ConstantesValoresPorDefecto.codigoValoresDefectoHoraEjecutarProcAutoActualizacionCitasOdo,
							forma.getHoraEjecutarProcAutoActualizacionCitasOdoNoAsistio(), 
							forma.getHoraEjecutarProcAutoActualizacionCitasOdoNoAsistio(), 
							usuario, 
							institucion);
				
				ValoresPorDefecto
				.modificar(
						con,
						ConstantesValoresPorDefecto.codigoValoresDefectoRequeridoProgramarProximaCitaEnAtencion,
						forma.getRequeridoProgramarProximaCitaEnAtencion(), 
						forma.getRequeridoProgramarProximaCitaEnAtencion(), 
						usuario, 
						institucion);
				
				//Fin Anexo 63
				
				ValoresPorDefecto
				.modificar(
						con,
						ConstantesValoresPorDefecto.codigoValoresDefectoRequierGenerarSolicitudCambioServicio,
						forma.getRequierGenerarSolicitudCambioServicio(), 
						forma.getRequierGenerarSolicitudCambioServicio(), 
						usuario, 
						institucion);
				//**
				ValoresPorDefecto
				.modificar(
						con,
						ConstantesValoresPorDefecto.codigoManejaVentaTarjetaClienteOdontosinEmision,
						forma.getManejaVentaTarjetaClienteOdontoSinEmision(), 
						forma.getManejaVentaTarjetaClienteOdontoSinEmision(), 
						usuario, 
						institucion);
				
				ValoresPorDefecto.modificar(con, ConstantesValoresPorDefecto.codigoValoresDefectoValidarPacienteParaVentaTarjeta, forma.getValidarPacienteParaVentaTarjeta(), forma.getValidarPacienteParaVentaTarjeta(), usuario, institucion);
				
				ValoresPorDefecto.modificar(con, ConstantesValoresPorDefecto.codigoValoresDefectoReciboCajaAutomaticoVentaTarjeta, forma.getReciboCajaAutomaticoVentaTarjeta(), forma.getReciboCajaAutomaticoVentaTarjeta(), usuario, institucion);
			
				ValoresPorDefecto.modificar(con, ConstantesValoresPorDefecto.codigoValoresDefectoModificarFechaHoraInicioAtencionOdonto, forma.getModificarFechaHoraInicioAtencionOdonto(), forma.getModificarFechaHoraInicioAtencionOdonto(), usuario, institucion);
				
				
				ValoresPorDefecto
				.modificar(
						con,
						ConstantesValoresPorDefecto.codigoValoresDefectoSolicitudCitaInterconsultaOdontoCitaProgramada,
						forma.getSolicitudCitaInterconsultaOdontoCitaProgramada(), 
						forma.getSolicitudCitaInterconsultaOdontoCitaProgramada(), 
						usuario, institucion);
				
				break;

		/**
		 * NOTA: Insertar los correspondientes set en el modulo que corresponde
		 * si no existe por favor crearlo
		 */
		}
		ValoresPorDefecto.cargarValoresIniciales(con);
	}

	/**
	 * @param con
	 * @param forma
	 */
	private void llenarFormaLargo(ValoresPorDefectoForm forma, UsuarioBasico usuario)
	{
		/**
		 * NOTA: 
		 * Insertar los correspondientes set en el modulo que corresponde si no existe 
		 * por favor crearlo
		 */
	   // try{		    		    
		    int codigoInstitucion=usuario.getCodigoInstitucionInt();
			forma.setDireccionPaciente(ValoresPorDefecto.getDireccionPaciente(codigoInstitucion));
			forma.setArchivosPlanosReportes(ValoresPorDefecto.getArchivosPlanosReportes(codigoInstitucion));
			forma.setCentroCostoConsultaExterna(ValoresPorDefecto.getCentroCostoConsultaExternaLargo(codigoInstitucion));
			forma.setConvenioFisalud(ValoresPorDefecto.getConvenioFisaludLargo(codigoInstitucion));
			forma.setFormaPagoEfectivo(ValoresPorDefecto.getFormaPagoEfectivoLargo(codigoInstitucion));
			forma.setCodigoManualEstandarBusquedaServicios(ValoresPorDefecto.getCodigoManualEstandarBusquedaServiciosLargo(codigoInstitucion));
			forma.setCodigoManualEstandarBusquedaArticulos(ValoresPorDefecto.getCodigoManualEstandarBusquedaArticulosLargo(codigoInstitucion));
			forma.setCausaExterna(ValoresPorDefecto.getCausaExternaLargo(codigoInstitucion));
			forma.setFinalidad(ValoresPorDefecto.getFinalidadLargo(codigoInstitucion));
			forma.setCiudadNacimiento(ValoresPorDefecto.getCiudadNacimiento(codigoInstitucion));
			forma.setCiudadVivienda(ValoresPorDefecto.getCiudadVivienda(codigoInstitucion));
			forma.setZonaDomicilio(ValoresPorDefecto.getZonaDomicilioLargo(codigoInstitucion));
			forma.setOcupacion(ValoresPorDefecto.getOcupacionLargo(codigoInstitucion));
			forma.setTipoId(ValoresPorDefecto.getTipoIdLargo(codigoInstitucion));
			forma.setFechaNacimiento(ValoresPorDefecto.getFechaNacimiento(codigoInstitucion));
			forma.setHistoriaClinica(ValoresPorDefecto.getHistoriaClinica(codigoInstitucion));
			forma.setFlagCentinela(ValoresPorDefecto.getFlagCentinela(codigoInstitucion));
			forma.setCentroCostoUrgencias(ValoresPorDefecto.getCentroCostoUrgenciasLargo(codigoInstitucion));
			forma.setOcupacionSolicitada(ValoresPorDefecto.getOcupacionSolicitadaLargo(codigoInstitucion));
			forma.setCodigoEstadoCama(ValoresPorDefecto.getCodigoEstadoCamaLargo(codigoInstitucion));
			forma.setValorUVR(ValoresPorDefecto.getValorUVR(codigoInstitucion));
			//forma.setCarnetRequerido(ValoresPorDefecto.getCarnetRequerido(codigoInstitucion));
			forma.setValidarEstadoSolicitudesInterpretadas(ValoresPorDefecto.getValidarEstadoSolicitudesInterpretadas(codigoInstitucion));
			forma.setValidarContratosVencidos(ValoresPorDefecto.getValidarContratosVencidos(codigoInstitucion));
			forma.setManejoTopesPaciente(ValoresPorDefecto.getManejoTopesPaciente(codigoInstitucion));
			forma.setCentroCostoAmbulatorios(ValoresPorDefecto.getCentroCostoAmbulatoriosLargo(codigoInstitucion));
			forma.setJustificacionServiciosRequerida(ValoresPorDefecto.getJustificacionServiciosRequerida(codigoInstitucion));
			forma.setIngresoCantidadFarmacia(ValoresPorDefecto.getIngresoCantidadFarmacia(codigoInstitucion));
			forma.setRipsPorFactura(ValoresPorDefecto.getRipsPorFactura(codigoInstitucion));
			forma.setFechaCorteSaldoInicial(ValoresPorDefecto.getFechaCorteSaldoInicialC(codigoInstitucion));
			forma.setConfirmarAjustesPooles(ValoresPorDefecto.getConfirmarAjustesPooles(codigoInstitucion));
			forma.setFechaCorteSaldoInicialCapitacion(ValoresPorDefecto.getFechaCorteSaldoInicialCCapitacion(codigoInstitucion));
			forma.setTopeConsecutivoCxC(ValoresPorDefecto.getTopeConsecutivoCxCSaldoI(codigoInstitucion));
			forma.setTopeConsecutivoCxCCapitacion(ValoresPorDefecto.getTopeConsecutivoCxCSaldoICapitacion(codigoInstitucion));
			forma.setMaxPageItems(ValoresPorDefecto.getMaxPageItems(codigoInstitucion));
			forma.setMinutosEsperaCuentasProcFact(ValoresPorDefecto.getModificarMinutosEsperaCuentasProcFact(codigoInstitucion));
			forma.setExcepcionRipsConsultorios(ValoresPorDefecto.getExcepcionRipsConsultorios(codigoInstitucion));
			forma.setAjustarCuentaCobroRadicada(ValoresPorDefecto.getAjustarCuentaCobroRadicada(codigoInstitucion));
			forma.setCerrarCuentaAnulacionFactura(ValoresPorDefecto.getCerrarCuentaAnulacionFactura(codigoInstitucion));
			forma.setBarrioResidencia(ValoresPorDefecto.getBarrioResidencia(codigoInstitucion));
			forma.setMaterialesPorActo(ValoresPorDefecto.getMaterialesPorActo(codigoInstitucion));
			forma.setFlagInfoAdicIngresoConvenios(ValoresPorDefecto.getInfoAdicIngresoConvenios(codigoInstitucion));
			forma.setHoraInicioPrimerTurno(ValoresPorDefecto.getHoraInicioPrimerTurno(codigoInstitucion));
			forma.setMostrarAdminMedicamentosArticulosDespachoCero(ValoresPorDefecto.getMostrarAdminMedicamentosArticulosDespachoCero(codigoInstitucion));
			forma.setHoraFinUltimoTurno(ValoresPorDefecto.getHoraFinUltimoTurno(codigoInstitucion));
			forma.setTiempoMaximoGrabacion(ValoresPorDefecto.getTiempoMaximoGrabacion(codigoInstitucion));
			forma.setIngresoCantidadSolMedicamentos(ValoresPorDefecto.getIngresoCantidadSolMedicamentos(codigoInstitucion));
			forma.setCentroCostoTerceros(ValoresPorDefecto.getCentroCostoTerceros());
			forma.setHorasReproceso(ValoresPorDefecto.getHorasReproceso());
			forma.setInterfazPacientes(ValoresPorDefecto.getInterfazPaciente(codigoInstitucion));
			forma.setInterfazAbonosTesoreria(ValoresPorDefecto.getInterfazAbonosTesoreria(codigoInstitucion));
			forma.setInterfazCompras(ValoresPorDefecto.getInterfazCompras(codigoInstitucion));
			forma.setArticuloInventario(ValoresPorDefecto.getArticuloInventario(codigoInstitucion));
			forma.setLoginUsuario(ValoresPorDefecto.getLoginUsuario(codigoInstitucion));
			forma.setValidarEdadResponsablePaciente(ValoresPorDefecto.getValidarEdadResponsablePaciente(codigoInstitucion));
			forma.setValidarEdadDeudorPaciente(ValoresPorDefecto.getValidarEdadDeudorPaciente(codigoInstitucion));
			forma.setAniosBaseEdadAdulta(ValoresPorDefecto.getAniosBaseEdadAdulta(codigoInstitucion));
			forma.setValidarEgresoAdministrativoPaquetizar(ValoresPorDefecto.getValidarEgresoAdministrativoPaquetizar(codigoInstitucion));
			forma.setMaxCantidPaquetesValidosIngresoPaciente(ValoresPorDefecto.getMaxCantidPaquetesValidosIngresoPaciente(codigoInstitucion));
			forma.setAsignarValorPacienteValorCargos(ValoresPorDefecto.getAsignarValorPacienteValorCargos(codigoInstitucion));
			
			forma.setPaisResidencia(ValoresPorDefecto.getPaisResidencia(codigoInstitucion));
			forma.setPaisNacimiento(ValoresPorDefecto.getPaisNacimiento(codigoInstitucion));
			forma.setInterfazCarteraPacientes(ValoresPorDefecto.getInterfazCarteraPacientes(codigoInstitucion));
			forma.setInterfazContableFacturas(ValoresPorDefecto.getInterfazContableFacturas(codigoInstitucion));
			forma.setInterfazTerceros(ValoresPorDefecto.getInterfazTerceros(codigoInstitucion));
			forma.setInterfazRips(ValoresPorDefecto.getInterfazRips(codigoInstitucion));
			forma.setConsolidarCargos(ValoresPorDefecto.getConsolidarCargos(codigoInstitucion));
			forma.setManejaConversionMonedaExtranjera(ValoresPorDefecto.getManejaConversionMonedaExtranjera(codigoInstitucion));
			forma.setImpresionMediaCarta(ValoresPorDefecto.getImpresionMediaCarta(codigoInstitucion));
			
			forma.setInstitucionMultiempresa(ValoresPorDefecto.getInstitucionMultiempresa(codigoInstitucion));
			forma.setManejoEspecialInstitucionesOdontologia(ValoresPorDefecto.getManejoEspecialInstitucionesOdontologia(codigoInstitucion));
			
			forma.setEntidadControlaDespachoSaldosMultidosis(ValoresPorDefecto.getEntidadControlaDespachoSaldosMultidosis(codigoInstitucion));
			forma.setNumeroDiasControlMedicamentosOrdenados(ValoresPorDefecto.getNumeroDiasControlMedicamentosOrdenados(codigoInstitucion));
			
			forma.setTipoConsecutivoManejar(ValoresPorDefecto.getTipoConsecutivoManejar(codigoInstitucion));
			forma.setInterfazContableRecibosCajaERP(ValoresPorDefecto.getInterfazContableRecibosCajaERP(codigoInstitucion));
			forma.setValidadAdministracionMedEgresoMedico(ValoresPorDefecto.getValidarAdministracionMedEgresoMedico(codigoInstitucion));
			
			forma.setInterfazConsecutivoFacturasOtroSistema(ValoresPorDefecto.getInterfazConsecutivoFacturasOtroSistema(codigoInstitucion));
			forma.setInterfazNutricion(ValoresPorDefecto.getInterfazNutricion(codigoInstitucion));
			
			forma.setProduccionEnParaleloConSistemaAnterior(ValoresPorDefecto.getProduccionEnParaleloConSistemaAnterior(codigoInstitucion));
			

			//Anexo 992
			/******************************************************************
			 * CARTERA
			 ******************************************************************/
			if(forma.getModulo()==ConstantesBD.codigoModuloCartera)
			{			    
				forma.setImprimirFirmasEnImpresionCC(ValoresPorDefecto.getImprimirFirmasEnImpresionCC(codigoInstitucion));
				forma.setNumeroMesesAMostrarEnReportesPresupuestoCapitacion(ValoresPorDefecto.getNumeroMesesAMostrarEnReportesPresupuestoCapitacion(codigoInstitucion));
			}
			//Fin Anexo 992
			
			/******************************************************************
			 * INVENTARIOS
			 ******************************************************************/
			if(forma.getModulo()==ConstantesBD.codigoModuloInventarios)
			{			    
			    forma.setManejoConsecTransInv(ValoresPorDefecto.getManejoConsecutivoTransInv(codigoInstitucion));			    
			    forma.setPorcentajesCostoInv(ValoresPorDefecto.getPorcentajeCostosInv(codigoInstitucion));
			    forma.setCodigoTransSolicPacientes(ValoresPorDefecto.getCodigoTransSoliPacientes(codigoInstitucion,false));
			    forma.setCodigoTransDevolPacientes(ValoresPorDefecto.getCodigoTransDevolPacientes(codigoInstitucion,false));
			    forma.setCodigoTransPedidos(ValoresPorDefecto.getCodigoTransaccionPedidos(codigoInstitucion,false));
			    forma.setCodigoTransDvolPedidos(ValoresPorDefecto.getCodigoTransDevolucionPedidos(codigoInstitucion,false));
			    forma.setCodigoTransCompras(ValoresPorDefecto.getCodigoTransCompra(codigoInstitucion,false));
			    forma.setCodigoTransDevolCompras(ValoresPorDefecto.getCodigoTransDevolCompra(codigoInstitucion,false));
			    forma.setPermitirModificarFechaInv(ValoresPorDefecto.getModificacionFechaInventario(codigoInstitucion));
			    forma.setPorcentajePuntoPedido(ValoresPorDefecto.getPorcentajePuntoPedido(codigoInstitucion));
			    forma.setDiasAlertaVigencia(ValoresPorDefecto.getDiasAlertaVigencia(codigoInstitucion));
			    forma.setCodigoTransTrasladoAlmacenes(ValoresPorDefecto.getCodigoTransTrasladoAlmacenes(codigoInstitucion,false));
			    forma.setArchivosPlanosReportesInventarios(ValoresPorDefecto.getArchivosPlanosReportesInventarios(codigoInstitucion));
			    forma.setConteosValidosAjustarInventarioFisico(ValoresPorDefecto.getConteosValidosAjustarInventarioFisico(codigoInstitucion));
			    forma.setConceptoParaAjusteEntrada(ValoresPorDefecto.getConceptoParaAjusteEntrada(codigoInstitucion, false));
			    forma.setConceptoParaAjusteSalida(ValoresPorDefecto.getConceptoParaAjusteSalida(codigoInstitucion, false));
			    forma.setPermitirModificarConceptosAjuste(ValoresPorDefecto.getPermitirModificarConceptosAjuste(codigoInstitucion));
			    forma.setPermitirModificarFechaSolicitudPedidos(ValoresPorDefecto.getPermitirModificarFechaSolicitudPedidos(codigoInstitucion));
			    forma.setPermitirModificarFechaSolicitudTraslado(ValoresPorDefecto.getPermitirModificarFechaSolicitudTraslado(codigoInstitucion));
			}
			/******************************************************************
			 * CONSULTA EXTERNA
			 ******************************************************************/
			if(forma.getModulo()==ConstantesBD.codigoModuloConsultaExterna)
			{	
				 forma.setDiasPreviosNotificacionProximoControl(ValoresPorDefecto.getDiasPreviosNotificacionProximoControl(codigoInstitucion));
				 forma.setMinutosEsperaCitaCaduca(ValoresPorDefecto.getMinutosEsperaCitaCaduca(codigoInstitucion));
				 forma.setDatosCuentaRequeridoReservaCitas(ValoresPorDefecto.getDatosCuentaRequeridoReservaCitas(codigoInstitucion));
				 forma.setCrearCuentaAtencionCitas(ValoresPorDefecto.getCrearCuentaAtencionCitas(codigoInstitucion));
				 forma.setDiasRestriccionCitasIncumplidas(ValoresPorDefecto.getValoresDefectoDiasRestriccionCitasIncumplidas(codigoInstitucion));
				 forma.setInstitucionManejaMultasPorIncumplimiento(ValoresPorDefecto.getInstitucionManejaMultasPorIncumplimiento(codigoInstitucion));
				 forma.setBloqueaCitasReservaAsignReprogPorIncump(ValoresPorDefecto.getBloqueaCitasReservaAsignReprogPorIncump(codigoInstitucion));
				 forma.setBloqueaAtencionCitasPorIncump(ValoresPorDefecto.getBloqueaAtencionCitasPorIncump(codigoInstitucion));
				 forma.setFechaInicioControlMultasIncumplimientoCitas(ValoresPorDefecto.getFechaInicioControlMultasIncumplimientoCitas(codigoInstitucion));
				 forma.setValorMultaPorIncumplimientoCitas(ValoresPorDefecto.getValorMultaPorIncumplimientoCitas(codigoInstitucion));
				 forma.setMinutosEsperaAsignarCitasOdoCaducadas(ValoresPorDefecto.getMinutosEsperaAsignarCitasOdoCaducadas(codigoInstitucion));
				 forma.setLasCitasDeControlSePermitenAsignarA(ValoresPorDefecto.getLasCitasDeControlSePermitenAsignarA(codigoInstitucion));
				 forma.setValidaEstadoContratoNominaALosProfesionalesSalud(ValoresPorDefecto.getValidaEstadoContratoNominaALosProfesionalesSalud(codigoInstitucion));
				 
				 if (forma.getInstitucionManejaMultasPorIncumplimiento().equals(ConstantesBD.acronimoSi))
				 {
					 forma.setReadOnly(false);
				 }
				 else
				 {
					 forma.setBloqueaCitasReservaAsignReprogPorIncump("");
					 forma.setBloqueaAtencionCitasPorIncump("");
					 forma.setFechaInicioControlMultasIncumplimientoCitas("");
					 forma.setValorMultaPorIncumplimientoCitas("");
					 forma.setReadOnly(true);
				 }
				 
				 forma.setFormatoImpReservaCitaOdonto(ValoresPorDefecto.getFormatoImpReservaCitaOdonto(codigoInstitucion));
				 forma.setFormatoImpAsignacionCitaOdonto(ValoresPorDefecto.getFormatoImpAsignacionCitaOdonto(codigoInstitucion));
				 
				 forma.setCancelarCitaAutoEstadoReprogramar(ValoresPorDefecto.getAlCancelarCitasOdontoDejarAutoEstadoReprogramar(codigoInstitucion));
					
					
			}
			/******************************************************************
			 * HISTORIA CLï¿½NICA
			 ******************************************************************/
			if(forma.getModulo() == ConstantesBD.codigoModuloHistoriaClinica)
			{
				forma.setOcupacionMedicoEspecialista(ValoresPorDefecto.getCodigoOcupacionMedicoEspecialista(codigoInstitucion,false));
				forma.setOcupacionMedicoGeneral(ValoresPorDefecto.getCodigoOcupacionMedicoGeneral(codigoInstitucion,false));
				forma.setOcupacionEnfermera(ValoresPorDefecto.getCodigoOcupacionEnfermera(codigoInstitucion,false));
				forma.setOcupacionAuxiliarEnfermeria(ValoresPorDefecto.getCodigoOcupacionAuxiliarEnfermeria(codigoInstitucion,false));
				forma.setRedNoAdscrita(ValoresPorDefecto.getRedNoAdscrita(codigoInstitucion));
				forma.setHorasCaducidadReferenciasExternas(ValoresPorDefecto.getHorasCaducidadReferenciasExternas(codigoInstitucion));
				forma.setLlamadoAutomaticoReferencia(ValoresPorDefecto.getLlamadoAutomaticoReferencia(codigoInstitucion));
				forma.setValidacionOcupacionJustificacionNoPosArticulos(ValoresPorDefecto.getValidacionOcupacionJustificacionNoPosArticulos(codigoInstitucion));
				forma.setValidacionOcupacionJustificacionNoPosServicios(ValoresPorDefecto.getValidacionOcupacionJustificacionNoPosServicios(codigoInstitucion));
				forma.setPermitirModificarTiempoTratamientoJustificacionNopos(ValoresPorDefecto.getPermitirModificarTiempoTratamientoJustificacionNopos(codigoInstitucion));
				forma.setControlaInterpretacionProcedimientosEvolucion(ValoresPorDefecto.getControlaInterpretacionProcedimientosEvoluciones(codigoInstitucion));
				forma.setMaxPageItemsEpicrisis(ValoresPorDefecto.getMaxPageItemsEpicrisis(codigoInstitucion));
				forma.setMostrarAntecedentesParametrizadosEpicrisis(ValoresPorDefecto.getMostrarAntecedentesParametrizadosEpicrisis(codigoInstitucion));
				forma.setPermitirConsultarEpicrisisSoloProf(ValoresPorDefecto.getPermitirConsultarEpicrisisSoloProfesionales(codigoInstitucion));
				forma.setMostrarEnviarEpicrisisEvol(ValoresPorDefecto.getValoresDefectoMostrarEnviarEpicrisisEvol(codigoInstitucion));
				forma.setOcupacionOdontologo(ValoresPorDefecto.getOcupacionOdontologoLargo(codigoInstitucion));
				forma.setOcupacionAuxiliarOdontologo(ValoresPorDefecto.getOcupacionAuxiliarOdontologoLargo(codigoInstitucion));
				forma.setMaximoDiasIngresosAConsultar(ValoresPorDefecto.getMaximoDiasConsultarIngresos(codigoInstitucion));
				forma.setMostrarGraficaCalculoIndicePlaca(ValoresPorDefecto.getMostrarGraficaCalculoIndicePlaca(codigoInstitucion));
			}
			
			/******************************************************************
			 * EPIDEMIOLOGIA
			 ******************************************************************/
			if (forma.getModulo()==ConstantesBD.codigoModuloEpidemiologia) {
			    forma.setVigilarAccRabico(ValoresPorDefecto.getVigilarAccRabico(codigoInstitucion));
			}
			
			/******************************************************************
			 * CAPITACIï¿½N
			 ******************************************************************/
			if(forma.getModulo()==ConstantesBD.codigoModuloCapitacion)
			{	
				 forma.setTipoConsecutivoCapitacion(ValoresPorDefecto.getTipoConsecutivoCapitacionLargo(codigoInstitucion));
				 forma.setImprimirFirmasImpresionCCCapitacion(ValoresPorDefecto.getImprimirFirmasImpresionCCCapitacion(codigoInstitucion));
				 forma.setEncabezadoFormatoImpresionFacturaOCCCapitacion(ValoresPorDefecto.getEncabezadoFormatoImpresionFacturaOCCCapitacion(codigoInstitucion));
				 forma.setPiePaginaFormatoImpresionFacturaOCCCapitacion(ValoresPorDefecto.getPiePaginaFormatoImpresionFacturaOCCCapitacion(codigoInstitucion));
				 forma.setHoraProcesoCierreCapitacion(ValoresPorDefecto.getHoraProcesoCierreCapitacion(codigoInstitucion));
				 forma.setEsquemaTariServiciosValorizarOrden(ValoresPorDefecto.getEsquemaTariServiciosValorizarOrden(codigoInstitucion));
				 forma.setEsquemaTariMedicamentosValorizarOrden(ValoresPorDefecto.getEsquemaTariMedicamentosValorizarOrden(codigoInstitucion));
				 forma.setHacerRequeridaSeleccionCentroAtencionAsignadoSubirPacienteIndividual(ValoresPorDefecto.getHacerRequeridaSeleccionCentroAtencionAsignadoSubirPacienteIndividual(codigoInstitucion));
			}
			
			/******************************************************************
			 * ORDENES
			 ******************************************************************/
			if(forma.getModulo()==ConstantesBD.codigoModuloOrdenes)
			{	
				forma.setNumDiasTratamientoMedicamentos(ValoresPorDefecto.getNumDiasTratamientoMedicamentosLargo(codigoInstitucion));
				forma.setNumDiasGenerarOrdenesArticulos(ValoresPorDefecto.getNumDiasGenerarOrdenesArticulosLargo(codigoInstitucion));
				forma.setNumDiasEgresoOrdenesAmbulatorias(ValoresPorDefecto.getNumDiasEgresoOrdenesAmbulatoriasLargo(codigoInstitucion));
				forma.setTipoConsecutivoCapitacion(ValoresPorDefecto.getTipoConsecutivoCapitacionLargo(codigoInstitucion));
				forma.setValidarRegistroEvolucionGenerarOrdenes(ValoresPorDefecto.getValidarRegistroEvolucionesGenerarOrdenes(codigoInstitucion));
				forma.setRequeridoComentariosSolicitar(ValoresPorDefecto.getRequeridoComentariosSolicitar(codigoInstitucion));
				forma.setPermIntOrdRespMulSinFin(ValoresPorDefecto.getPermIntOrdRespMulSinFin(codigoInstitucion));
				forma.setNumeroMaximoDiasGenOrdenesAmbServicios(ValoresPorDefecto.getNumeroMaximoDiasGenOrdenesAmbServicios(codigoInstitucion));
				forma.setObligarRegIncapaPacienteHospitalizado(ValoresPorDefecto.getObligarRegIncapaPacienteHospitalizado(codigoInstitucion));
				forma.setActivarBotonGenerarSolicitudOrdenAmbulatora(ValoresPorDefecto.getActivarBotonGenerarSolicitudOrdenAmbulatora(codigoInstitucion));
				forma.setPermitirOrdenarMedicamentosPacienteUrgenciasConEgreso(ValoresPorDefecto.getPermitirOrdenarMedicamentosPacienteUrgenciasConEgreso(codigoInstitucion));
			
			}
			/***********************************************************
			 * MANEJO PACIENTE
			 **********************************************************/
			if(forma.getModulo()==ConstantesBD.codigoModuloManejoPaciente)
			{
				forma.setHoraCorteHistoricoCamas(ValoresPorDefecto.getHoraCorteHistoricoCamas(codigoInstitucion));
				forma.setMinutosLimiteAlertaReserva(ValoresPorDefecto.getMinutosLimiteAlertaReserva(codigoInstitucion));
				forma.setMinutosLimiteAlertaPacienteConSalidaHospitalizacion(ValoresPorDefecto.getMinutosLimiteAlertaPacienteConSalidaHospitalizacion(codigoInstitucion));
				forma.setMinutosLimiteAlertaPacienteConSalidaUrgencias(ValoresPorDefecto.getMinutosLimiteAlertaPacienteConSalidaUrgencias(codigoInstitucion));
				forma.setMinutosLimiteAlertaPacientePorRemitirHospitalizacion(ValoresPorDefecto.getMinutosLimiteAlertaPacientePorRemitirHospitalizacion(codigoInstitucion));
				forma.setMinutosLimiteAlertaPacientePorRemitirUrgencias(ValoresPorDefecto.getMinutosLimiteAlertaPacientePorRemitirUrgencias(codigoInstitucion));
				forma.setEntidadManejaHospitalDia(ValoresPorDefecto.getEntidadManejaHospitalDia(codigoInstitucion));
				forma.setValoracionUrgenciasEnHospitalizacion(ValoresPorDefecto.getValoracionUrgenciasEnHospitalizacion(codigoInstitucion));
				forma.setUbicacionPlanosEntidadesSubcontratadas(ValoresPorDefecto.getUbicacionPlanosEntidadesSubcontratadas(codigoInstitucion));
				forma.setGenForecatRips(ValoresPorDefecto.getGenForecatRips(codigoInstitucion));
				forma.setAsignaValoracionCxAmbulaHospita(ValoresPorDefecto.getAsignaValoracionCxAmbulaHospita(codigoInstitucion));
				forma.setLiberarCamaHospitalizacionDespuesFacturar(ValoresPorDefecto.getLiberarCamaHospitalizacionDespuesFacturar(codigoInstitucion));
				forma.setPathArchivosPlanosManejoPaciente(ValoresPorDefecto.getValoresDefectoPathArchivosPlanosManejoPaciente(codigoInstitucion));
				forma.setTiempoMaximoReingresoUrgencias(ValoresPorDefecto.getTiempoMaximoReingresoUrgencias(codigoInstitucion));
				forma.setTiempoMaximoReingresoHospitalizacion(ValoresPorDefecto.getTiempoMaximoReingresoHospitalizacion(codigoInstitucion));
				forma.setPathArchivosPlanosFurips(ValoresPorDefecto.getValoresDefectoPathArchivosPlanosFurips(codigoInstitucion));
				forma.setTipoUsuarioaReportarSol(ValoresPorDefecto.getValoresDefectoUsuarioaReportarenSolicitAuto(codigoInstitucion));
				forma.setEscalaPacientePerfil(ValoresPorDefecto.getEscalaPacientePerfilLargo(codigoInstitucion));
				forma.setPermitirRegistrarReclamacionCuentasNoFacturadas(ValoresPorDefecto.getPermitirRegistrarReclamacionCuentasNoFacturadas(codigoInstitucion));
				forma.setEntidadSubcontratadaCentrosCostoAutorizacionCapitacionSubcontratada(ValoresPorDefecto.getEntidadSubcontratadaCentrosCostoAutorizacionCapitacionSubcontratada(codigoInstitucion));
				forma.setPrioridadEntidadSubcontratada(ValoresPorDefecto.getPrioridadEntidadSubcontratada(codigoInstitucion));
				forma.setFormatoImpresionAutorEntidadSub(ValoresPorDefecto.getFormatoImpresionAutorEntidadSub(codigoInstitucion));
				forma.setEncFormatoImpresionAutorEntidadSub(ValoresPorDefecto.getEncFormatoImpresionAutorEntidadSub(codigoInstitucion));
				forma.setPiePagFormatoImpresionAutorEntidadSub(ValoresPorDefecto.getPiePagFormatoImpresionAutorEntidadSub(codigoInstitucion));
				forma.setDiasVigenciaAutorIndicativoTemp(ValoresPorDefecto.getDiasVigenciaAutorIndicativoTemp(codigoInstitucion));
				forma.setDiasCalcularFechaVencAutorizacionArticulo(ValoresPorDefecto.getDiasCalcularFechaVencAutorizacionArticulo(codigoInstitucion));
				forma.setDiasCalcularFechaVencAutorizacionServicio(ValoresPorDefecto.getDiasCalcularFechaVencAutorizacionServicio(codigoInstitucion));
				forma.setDiasVigentesNuevaAutorizacionEstanciaSerArt(ValoresPorDefecto.getDiasVigentesNuevaAutorizacionEstanciaSerArt(codigoInstitucion));
				forma.setDiasProrrogaAutorizacion(ValoresPorDefecto.getDiasProrrogaAutorizacion(codigoInstitucion));
				forma.setNumMaximoReclamacionesAccEventoXFactura(ValoresPorDefecto.getNumMaximoReclamacionesAccEventoXFactura(codigoInstitucion));	
				forma.setPermitirModificarDatosUsuariosCapitados(ValoresPorDefecto.getPermitirModificarDatosUsuariosCapitados(codigoInstitucion));
				forma.setPermitirModificarDatosUsuariosCapitadosModificarCuenta(ValoresPorDefecto.getPermitirModificarDatosUsuariosCapitadosModificarCuenta(codigoInstitucion));
				forma.setEsquemaTarifarioAutocapitaSubCirugiasNoCurentos(ValoresPorDefecto.getEsquemaTarifarioAutocapitaSubCirugiasNoCurentosLargo(codigoInstitucion));
				forma.setValidarDisponibilidadSaldoPresupuestoAutorizacionesCapotaAutomatica(ValoresPorDefecto.getValidarDisponibilidadSaldoPresupuestoAutorizacionesCapotaAutomatica(codigoInstitucion));
				forma.setManejoOxigenoFurips(ValoresPorDefecto.getManejoOxigenoFurips(codigoInstitucion));
				Connection con=UtilidadBD.abrirConexion();
				forma.setServiciosManejoTransPrimario(ValoresPorDefecto.cargarResumenServiciosManejoTransPrimario(usuario.getCodigoInstitucionInt(), con));
				forma.setServiciosManejoTransSecundario(ValoresPorDefecto.cargarResumenServiciosManejoTransSecundario(usuario.getCodigoInstitucionInt(), con));
				String temporal="";
				for(int i=0;i<forma.getServiciosManejoTransPrimario().size();i++)
				{
					if(!UtilidadTexto.isEmpty(temporal))
						temporal=temporal+",";
					temporal=temporal+forma.getServiciosManejoTransPrimario().get(i).intValue()+"";
				}
				for(int i=0;i<forma.getServiciosManejoTransSecundario().size();i++)
				{
					if(!UtilidadTexto.isEmpty(temporal))
						temporal=temporal+",";
					temporal=temporal+forma.getServiciosManejoTransSecundario().get(i).intValue()+"";
				}
				forma.setServiciosSeleccionados(temporal);
				forma.setViaIngresoValidacionesOrdenesAmbulatorias(ValoresPorDefecto.getViaIngresoValidacionesOrdenesAmbulatoriasLargo(codigoInstitucion));
				forma.setViaIngresoValidacionesPeticiones(ValoresPorDefecto.getViaIngresoValidacionesPeticionesLargo(codigoInstitucion));
				
				forma.setMesesMaxAdminAutoCapVencidas(ValoresPorDefecto.getMesesMaxAdminAutoCapVencidas(codigoInstitucion));
				
				forma.setDiasMaxProrrogaAutorizacionArticulo(ValoresPorDefecto.getDiasMaxProrrogaAutorizacionArticulo(codigoInstitucion));
				forma.setDiasMaxProrrogaAutorizacionServicio(ValoresPorDefecto.getDiasMaxProrrogaAutorizacionServicio(codigoInstitucion));
				
				forma.setTipoPacienteValidacionesOrdenesAmbulatorias(ValoresPorDefecto.getTipoPacienteValidacionesOrdenesAmbulatoriasLargo(codigoInstitucion));
				forma.setTipoPacienteValidacionesPeticiones(ValoresPorDefecto.getTipoPacienteValidacionesPeticionesLargo(codigoInstitucion));
				
				UtilidadBD.closeConnection(con);

			}
			
			/***********************************************************
			 * ADMINISTRACION
			 **********************************************************/
			if(forma.getModulo()==ConstantesBD.codigoModuloAdministracion)
			{
				forma.setIdentificadorInstitucionArchivosColsanitas(ValoresPorDefecto.getIdentificadorInstitucionArchivosColsanitas(codigoInstitucion));			
				//forma.setPathArchivosPlanosFacturacion(ValoresPorDefecto.getValoresDefectoPathArchivosPlanosFacturacion(codigoInstitucion));
				forma.setComprobacionDerechosCapitacionObligatoria(ValoresPorDefecto.getValoresDefectoComprobacionDerechosCapitacionObligatoria(codigoInstitucion));
				forma.setNumeroDiasBusquedaReportes(ValoresPorDefecto.getNumeroDiasBusquedaReportes(codigoInstitucion));
				forma.setNumDigCaptNumIdPac(ValoresPorDefecto.getNumDigCaptNumIdPac(codigoInstitucion));
				forma.setContabilizacionRequeridoProcesoAsocioFacCuentaCapitacion(ValoresPorDefecto.getContabilizacionRequeridoProcesoAsocioFacCuentaCapitacion(codigoInstitucion));
				forma.setManejaInterfazUsuariosSistemaERP(ValoresPorDefecto.getManejaInterfazUsuariosSistemaERP(codigoInstitucion));
				forma.setFechaInicioCierreOrdenMedica(ValoresPorDefecto.getFechaInicioCierreOrdenMedica(codigoInstitucion));
				forma.setHoraEjecProcesoInactivarUsuarioInacSistema(ValoresPorDefecto.getHoraEjecProcesoInactivarUsuarioInacSistema(codigoInstitucion));
				forma.setHoraEjeProcesoCaduContraInacSistema(ValoresPorDefecto.getHoraEjeProcesoCaduContraInacSistema(codigoInstitucion));
				forma.setDiasVigenciaContraUsuario(ValoresPorDefecto.getDiasVigenciaContraUsuario(codigoInstitucion));
				forma.setDiasFinalesVigenciaContraMostrarAlerta(ValoresPorDefecto.getDiasFinalesVigenciaContraMostrarAlerta(codigoInstitucion));
				
			}
			/***********************************************************
			 * FACTURACION
			 **********************************************************/
			if(forma.getModulo()==ConstantesBD.codigoModuloFacturacion)
			{
				forma.setEntidadManejaRips(ValoresPorDefecto.getEntidadManejaRips(codigoInstitucion));
				forma.setGeneraEstanciaAutomatica(ValoresPorDefecto.getGenerarEstanciaAutomatica(codigoInstitucion));
				forma.setHoraGeneraEstanciaAutomatica(ValoresPorDefecto.getHoraGenerarEstanciaAutomatica(codigoInstitucion));
				forma.setIncluirTipoPacientesCirugiaAmbulatoria(ValoresPorDefecto.getIncluirTipoPacienteCirugiaAmbulatoria(codigoInstitucion));
				forma.setPermitirFacturarReingresosIndependientes(ValoresPorDefecto.getPermitirFacturarReingresosIndependientes(codigoInstitucion));
				forma.setRequeridoInfoRipsCargoDirecto(ValoresPorDefecto.getRequeridaInfoRipsCagosDirectos(codigoInstitucion));
				forma.setRequiereAutorizarAnularFacturas(ValoresPorDefecto.getValoresDefectoRequiereAutorizarAnularFacturas(codigoInstitucion));
				forma.setValidarPoolesFact(ValoresPorDefecto.getValoresDefectoValidarPoolesFact(codigoInstitucion));
				forma.setPathArchivosPlanosFacturacion(ValoresPorDefecto.getValoresDefectoPathArchivosPlanosFacturacion(codigoInstitucion));
				forma.setManejaConsecutivoFacturaPorCentroAtencion(ValoresPorDefecto.getManejaConsecutivoFacturaPorCentroAtencion(codigoInstitucion));
				forma.setPermitirFacturarReclamarCuentasConRegistroPendientes(ValoresPorDefecto.getPermitirFacturarReclamarCuentasConRegistroPendientes(codigoInstitucion));
				forma.setRequiereAutorizacionCapitacionSubcontratadaParaFacturar(ValoresPorDefecto.getRequiereAutorizacionCapitacionSubcontratadaParaFacturar(codigoInstitucion));
				forma.setPermitirfacturarReclamCuentaRATREC(ValoresPorDefecto.getPermitirfacturarReclamCuentaRATREC(codigoInstitucion));
				forma.setHacerRequeridoValorAbonoAplicadoAlFacturar(ValoresPorDefecto.getHacerRequeridoValorAbonoAplicadoAlFacturar(codigoInstitucion));
				
			}
			/***********************************************************
			 * SALAS CIRUGIA
			 **********************************************************/
			if(forma.getModulo()==ConstantesBD.codigoModuloSalasCirugia)
			{
				forma.setHoraInicioProgramacionSalas(ValoresPorDefecto.getHoraInicioProgramacionSalas(codigoInstitucion));
				forma.setHoraFinProgramacionSalas(ValoresPorDefecto.getHoraFinProgramacionSalas(codigoInstitucion));
				forma.setCodigoEspecialidadAnestesiologia(ValoresPorDefecto.getEspecialidadAnestesiologia(codigoInstitucion, false));
				forma.setLiquidacionAutomaticaCirugias(ValoresPorDefecto.getLiquidacionAutomaticaCirugias(codigoInstitucion));
				forma.setLiquidacionAutomaticaNoQx(ValoresPorDefecto.getLiquidacionAutomaticaNoQx(codigoInstitucion));
				forma.setManejoProgramacionSalasSolicitudesDyt(ValoresPorDefecto.getManejoProgramacionSalasSolicitudesDyt(codigoInstitucion));
				forma.setRequeridaDescripcionEspecialidadCirugias(ValoresPorDefecto.getRequeridaDescripcionEspecialidadCirugias(codigoInstitucion));
				forma.setRequeridaDescripcionEspecialidadNoCruentos(ValoresPorDefecto.getRequeridaDescripcionEspecialidadNoCruentos(codigoInstitucion));
				forma.setAsocioAyudantia(ValoresPorDefecto.getAsocioAyudantiaLargo(codigoInstitucion));
				forma.setIndicativoCobrableHonorariosCirugia(ValoresPorDefecto.getIndicativoCobrableHonorariosCirugia(codigoInstitucion));
				forma.setIndicativoCobrableHonorariosNoCruento(ValoresPorDefecto.getIndicativoCobrableHonorariosNoCruento(codigoInstitucion));
				forma.setAsocioCirujano(ValoresPorDefecto.getAsocioCirujanoLargo(codigoInstitucion));
				forma.setAsocioAnestesia(ValoresPorDefecto.getAsocioAnestesiaLargo(codigoInstitucion));
				forma.setModificarInformacionDescripcionQuirurgica(ValoresPorDefecto.getModificarInformacionDescripcionQuirurgica(codigoInstitucion));
				forma.setMinutosRegistroNotasCirugia(ValoresPorDefecto.getMinutosRegistroNotasCirugia(codigoInstitucion));
				forma.setMinutosRegistroNotasNoCruentos(ValoresPorDefecto.getMinutosRegistroNotasNoCruentos(codigoInstitucion));
				forma.setModificarInformacionQuirurgica(ValoresPorDefecto.getModificarInformacionQuirurgica(codigoInstitucion));
				forma.setMinutosMaximosRegistroAnestesia(ValoresPorDefecto.getMinutosMaximosRegistroAnestesia(codigoInstitucion));
				forma.setPostularFechasEnRespuestasDyT(ValoresPorDefecto.getPostularFechasEnRespuestaDyT(codigoInstitucion));
				forma.setClasesInventariosPaqMatQx(ValoresPorDefecto.getClasesInventariosPaqMatQx());
				forma.setManejaHojaAnestesia(ValoresPorDefecto.getManejaHojaAnestesia(codigoInstitucion));
			}
			
			/***********************************************************
			 * FACTURAS VARIAS
			 **********************************************************/
			if(forma.getModulo()==ConstantesBD.codigoModuloFacturasVarias)
			{
				forma.setTipoConsecutivoManejar(ValoresPorDefecto.getTipoConsecutivoManejar(codigoInstitucion));
				//Anexo 938
				
				forma.setReciboCajaAutomaticoGeneracionFacturaVaria(ValoresPorDefecto.getReciboCajaAutomaticoGeneracionFacturaVaria(codigoInstitucion));
				forma.setConceptoIngresoFacturasVarias(ValoresPorDefecto.getConceptoIngresoFacturasVarias(codigoInstitucion));
				
				forma.setListadoConceptos(Utilidades.obtenerConceptosIngresoTesoreria(ConstantesBD.codigoTipoIngresoTesoreriaOtrasCxC, ConstantesIntegridadDominio.acronimoFactura));
				//Fin anexo 938
				
				forma.setFormatoFacturaVaria(ValoresPorDefecto.getFormatoFacturaVaria(codigoInstitucion));
				forma.setManejaConsecutivoFacturasVariasPorCentroAtencion(ValoresPorDefecto.getManejaConsecutivoFacturasVariasPorCentroAtencion(codigoInstitucion));
			
			}
			
			/***********************************************************
			 * GLOSAS
			 **********************************************************/			
			if(forma.getModulo()==ConstantesBD.codigoModuloGlosas) {
				forma.setGenerarAjusteAutoRegRespuesta(ValoresPorDefecto.getGenerarAjusteAutoRegRespuesta(codigoInstitucion));
				forma.setGenerarAjusteAutoRegRespuesConciliacion(ValoresPorDefecto.getGenerarAjusteAutoRegRespuesConciliacion(codigoInstitucion));
				forma.setFormatoImpresionRespuesGlosa(ValoresPorDefecto.getFormatoImpresionRespuesGlosa(codigoInstitucion));
				forma.setFormatoImpresionConciliacion(ValoresPorDefecto.getFormatoImpresionConciliacion(codigoInstitucion));
				forma.setImprimirFirmasImpresionRespuesGlosa(ValoresPorDefecto.getImprimirFirmasImpresionRespuesGlosa(codigoInstitucion));
				forma.setNumeroDiasResponderGlosas(ValoresPorDefecto.getNumeroDiasResponderGlosas(codigoInstitucion));
				
				forma.setValidarAuditor(ValoresPorDefecto.getValidarAuditor(codigoInstitucion));
				forma.setValidarUsuarioGlosa(ValoresPorDefecto.getValidarUsuarioGlosa(codigoInstitucion));
				forma.setValidarGlosaReiterada(ValoresPorDefecto.getValidarGlosaReiterada(codigoInstitucion));
				forma.setNumeroGlosasRegistradasXFactura(ValoresPorDefecto.getNumeroGlosasRegistradasXFactura(codigoInstitucion));
				forma.setNumeroDiasNotificarGlosa(ValoresPorDefecto.getNumeroDiasNotificarGlosa(codigoInstitucion));
				forma.setRequiereGlosaInactivar(ValoresPorDefecto.getRequiereGlosaInactivar(codigoInstitucion));
				forma.setAprobarGlosaRegistro(ValoresPorDefecto.getAprobarGlosaRegistro(codigoInstitucion));				
				forma.setViaIngresoDetaleGlosa(ValoresPorDefecto.getMostrarDetalleGlosasFacturaSolicFactura(codigoInstitucion));
			}
			
			/******************************************************************
			 * INTERFAZ
			 ******************************************************************/
			if(forma.getModulo()==ConstantesBD.codigoModuloInterfaz)
			{	
				forma.setTiemSegVeriInterShaioProc(ValoresPorDefecto.getTiemSegVeriInterShaioProc(codigoInstitucion));
				
			}
			
			/******************************************************************
			 * CARTERA PACIENTE
			 ******************************************************************/
			if(forma.getModulo()==ConstantesBD.codigoModuloCarteraPaciente)
			{	
				forma.setMaximoNumeroCuotasFinanciacion(ValoresPorDefecto.getMaximoNumeroCuotasFinanciacion(codigoInstitucion));
				forma.setMaximoNumeroDiasFinanciacionPorCuota(ValoresPorDefecto.getMaximoNumeroDiasFinanciacionPorCuota(codigoInstitucion));
				forma.setFormatoDocumentosGarantia_Pagare(ValoresPorDefecto.getFormatoDocumentosGarantia_Pagare(codigoInstitucion));
				forma.setDiasParaDefinirMoraXDeudaPacientes(ValoresPorDefecto.getDiasParaDefinirMoraXDeudaPacientes(codigoInstitucion));
				forma.setCuentaContableLetra(ValoresPorDefecto.getCuentaContableLetra(codigoInstitucion));
				forma.setCuentaContablePagare(ValoresPorDefecto.getCuentaContablePagare(codigoInstitucion));
			}
			
			/******************************************************************
			 * ODONTOLOGï¿½A
			 ******************************************************************/
			if(forma.getModulo()==ConstantesBD.codigoModuloOdontologia)
			{	
				forma.setEdadFinalNinez(ValoresPorDefecto.getEdadFinalNinez(codigoInstitucion));
				forma.setEdadInicioAdulto(ValoresPorDefecto.getEdadInicioAdulto(codigoInstitucion));
				
				//Miro si hay agendas para no permitir modificar el elemento 
				forma.setModificarMultiploMinGeneracionCita(ValoresPorDefecto.existeAgendaOdon());
				forma.setMultiploMinGeneracionCita(ValoresPorDefecto.getMultiploMinGeneracionCita(codigoInstitucion));
				
				
				forma.setNumDiasAntFActualAgendaOd(ValoresPorDefecto.getNumDiasAntFActualAgendaOd(codigoInstitucion));
				forma.setUtilizanProgramasOdontologicosEnInstitucion(ValoresPorDefecto.getUtilizanProgramasOdontologicosEnInstitucion(codigoInstitucion));
				forma.setTiempoVigenciaPresupuestoOdo(ValoresPorDefecto.getTiempoVigenciaPresupuestoOdo(codigoInstitucion));
				forma.setPermiteCambiarServiciosCitaAtencionOdo(ValoresPorDefecto.getPermiteCambiarServiciosCitaAtencionOdo(codigoInstitucion));
				
				//Tarea 150787 - Verifico la Existencia de presupuestos, si existen queda en si, si no existen se muestra normalmente
				forma.setExistePresupuesto(ValoresPorDefecto.existePresupesto());
				if (!forma.isExistePresupuesto())
					forma.setValidaPresupuestoOdoContratado(ValoresPorDefecto.getValidaPresupuestoOdoContratado(codigoInstitucion));
				else
					forma.setValidaPresupuestoOdoContratado(ConstantesBD.acronimoSi);
				
				forma.setConveniosAMostrarPresupuestoOdo(ValoresPorDefecto.getConveniosAMostrarPresupuestoOdo());
				
				forma.setTiempoMaxEsperaInactivarPresupuestoOdoSuspTemp(ValoresPorDefecto.getTiempoMaxEsperaInactivarPresupuestoOdoSuspTemp(codigoInstitucion));
				forma.setEjecutarProcesoAutoActualizacionEstadosOdo(ValoresPorDefecto.getEjecutarProcesoAutoActualizacionEstadosOdo(codigoInstitucion));
				forma.setHoraEjecutarProcesoAutoActualizacionEstadosOdo(ValoresPorDefecto.getHoraEjecutarProcesoAutoActualizacionEstadosOdo(codigoInstitucion));
				forma.setMotivoCancelacionPresupuestoSuspendidoTemp(ValoresPorDefecto.getMotivoCancelacionPresupuestoSuspendidoTemp(codigoInstitucion));
				forma.setMaximoTiempoSinEvolucionarParaInactivarPlanTratamiento(ValoresPorDefecto.getMaximoTiempoSinEvolucionarParaInactivarPlanTratamiento(codigoInstitucion));
				forma.setPrioridadParaAplicarPromocionesOdo(ValoresPorDefecto.getPrioridadParaAplicarPromocionesOdo(codigoInstitucion));
				forma.setInstitucionRegistraAtencionExterna(ValoresPorDefecto.getInstitucionRegistraAtencionExterna(codigoInstitucion));
				
				//Se carga este arraylist para determinar si se debe o no ermitir modificar el parametro UtilizanProgramasOdontologicosEnInstitucion
				DtoDetalleHallazgoProgramaServicio dto=new DtoDetalleHallazgoProgramaServicio();
				forma.setListadoExistenDHPS(DetalleHallazgoProgramaServicio.cargar(dto, usuario.getCodigoInstitucionInt()));
				if (forma.getListadoExistenDHPS().size()>0)
					forma.setModificarUPOEI(true);
				
				//Anexo 888
				forma.setEsRequeridoProgramarCitaAlContratarPresupuestoOdon(ValoresPorDefecto.getEsRequeridoProgramarCitaAlContratarPresupuestoOdon(codigoInstitucion));
				forma.setMotivoAnulacionSolicitudAutorizacionDescuentoPresupeustoOdontologico(ValoresPorDefecto.getMotivoAnulacionSolicitudAutorizacionDescuentoPresupeustoOdontologico(codigoInstitucion));
				
				DtoMotivoDescuento dtoMotivos=new DtoMotivoDescuento();
				dtoMotivos.setTipo(ConstantesIntegridadDominio.acronimoEstadoAnulado);
				forma.setListMotivosDescuentos(MotivosDescuentos.cargar(dtoMotivos));
				//Fin anexo 888
				
				//Anexo 888 Pt II
				forma.setRequeridoProgramarCitaControlAlTerminarPresupuestoOdon(ValoresPorDefecto.getRequeridoProgramarCitaControlAlTerminarPresupuestoOdon(codigoInstitucion));
				forma.setRequeridoProgramarCitaControlAlTerminarPresupuestoOdonServicio(ValoresPorDefecto.getRequeridoProgramarCitaControlAlTerminarPresupuestoOdonServicio(codigoInstitucion));
				forma.setTiempoDiasParaAgendarCitaControlAlTerminarPresupuestoOdon(ValoresPorDefecto.getTiempoDiasParaAgendarCitaControlAlTerminarPresupuestoOdon(codigoInstitucion));
				forma.setInstitucionManejaFacturacionAutomatica(ValoresPorDefecto.getInstitucionManejaFacturacionAutomatica(codigoInstitucion));
				//Fun Anexo 888 Pt II
				
				//Los siguientes elementos se pasaron de administracion a odontologia por Anexo 63
				forma.setMinutosCaducaCitasReservadas(ValoresPorDefecto.getMinutosCaducaCitasReservadas(codigoInstitucion));
				forma.setMinutosCaducaCitasAsignadasReprogramadas(ValoresPorDefecto.getMinutosCaducaCitasAsignadasReprogramadas(codigoInstitucion));
				forma.setEjecutarProcAutoActualizacionCitasOdoNoAsistio(ValoresPorDefecto.getEjecutarProcAutoActualizacionCitasOdo(codigoInstitucion));
				forma.setHoraEjecutarProcAutoActualizacionCitasOdoNoAsistio(ValoresPorDefecto.getHoraEjecutarProcAutoActualizacionCitasOdo(codigoInstitucion));
				//Fin Anexo 63
				
				forma.setRequeridoProgramarProximaCitaEnAtencion(ValoresPorDefecto.getRequeridoProgramarProximaCitaEnAtencion(codigoInstitucion));
				
				forma.setRequierGenerarSolicitudCambioServicio(ValoresPorDefecto.getRequierGenerarSolicitudCambioServicio(codigoInstitucion));
				
				forma.setManejaVentaTarjetaClienteOdontoSinEmision(ValoresPorDefecto.getManejaVentaTarjetaClienteOdontosinEmision(codigoInstitucion));

				

				forma.setValidarPacienteParaVentaTarjeta(ValoresPorDefecto.getValidarPacienteParaVentaTarjeta(codigoInstitucion));

				forma.setReciboCajaAutomaticoVentaTarjeta(ValoresPorDefecto.getReciboCajaAutomaticoVentaTarjeta(codigoInstitucion));
				
				forma.setModificarFechaHoraInicioAtencionOdonto(ValoresPorDefecto.getModificarFechaHoraInicioAtencionOdonto(codigoInstitucion));
				
				forma.setSolicitudCitaInterconsultaOdontoCitaProgramada(ValoresPorDefecto.getSolicitudCitaInterconsultaOdontoCitaProgramada(codigoInstitucion));
			}
			
			/******************************************************************
			 * TESORERIA
			 ******************************************************************/
			if(forma.getModulo()==ConstantesBD.codigoModuloTesoreria)
			{
				forma.setManejaConsecutivosTesoreriaPorCentroAtencion(ValoresPorDefecto.getManejaConsecutivosTesoreriaPorCentroAtencion(codigoInstitucion));
				forma.setTamanioImpresionRC(ValoresPorDefecto.getTamanioImpresionRC(codigoInstitucion));
				forma.setRequiereAperturaCierreCaja(ValoresPorDefecto.getRequiereAperturaCierreCaja(codigoInstitucion));
				forma.setEsRequeridoTestigoSolicitudAceptacionTrasladoCaja(ValoresPorDefecto.getEsRequeridoTestigoSolicitudAceptacionTrasladoCaja(codigoInstitucion));
				forma.setInstitucionManejaCajaPrincipal(ValoresPorDefecto.getInstitucionManejaCajaPrincipal(codigoInstitucion));
				forma.setInstitucionManejaTrasladoOtraCajaRecaudo(ValoresPorDefecto.getInstitucionManejaTrasladoOtraCajaRecaudo(codigoInstitucion));
				forma.setInstitucionManejaEntregaATransportadoraValores(ValoresPorDefecto.getInstitucionManejaEntregaATransportadoraValores(codigoInstitucion));
				forma.setTrasladoAbonosPacienteSoloPacientesConPresupuestoContratado(ValoresPorDefecto.getTrasladoAbonosPacienteSoloPacientesConPresupuestoContratado(codigoInstitucion));
				/* 
				 * Parámetro que estaba asociado al módulo de Facturación, pero que ahora se asocia al módulo 
				 * de Tesorería.
				 */
				forma.setControlarAbonoPacientePorNroIngreso(ValoresPorDefecto.getControlarAbonoPacientePorNroIngreso(codigoInstitucion));
				/* Cambio RQF-0138 Aplica SONRIA-VERSALLES */
				forma.setManejaConsecutivosNotasPacientesCentroAtencion(ValoresPorDefecto.getManejaConsecutivosNotasPacientesCentroAtencion(codigoInstitucion));
				forma.setNaturalezaNotasPacientesManejar(ValoresPorDefecto.getNaturalezaNotasPacientesManejar(codigoInstitucion));
				/* */
				forma.setPermitirRecaudosCajaMayor(ValoresPorDefecto.getPermitirRecaudosCajaMayor(codigoInstitucion));
			}
			
			
			/**
			 * NOTA: 
			 * Insertar los correspondientes set en el modulo que corresponde si no existe 
			 * por favor crearlo
			 */
		/**}
		catch (Exception e)
		{
			logger.error("Error copiando los valores por defecto al form " +e);
		}**/
	}
	
	/**
	 * 
	 * Este Método se encarga de realizar las validaciones
	 * específicas de cada parámetro general del sistema
	 * 
	 * @param ValoresPorDefectoForm forma
	 * @author, Angela Maria Aguirre
	 *
	 */
	private void realizarValidacionesParametros(ValoresPorDefectoForm forma){
		
		if(forma.getMapaEtiquetas()!=null && forma.getMapaEtiquetas().size()>0){
			Iterator it = forma.getMapaEtiquetas().entrySet().iterator();
			
			while (it.hasNext()) {
				Map.Entry e = (Map.Entry)it.next();	
				
				//Si el parámetro es Tiempo máximo de espera para inactivar presupuesto odontológico con suspensión temporal
				if((String.valueOf(e.getValue())).equals(
						ConstantesValoresPorDefecto.nombreValoresDefectoTiempoMaxEsperaInactivarPresupuestoOdoSuspTemp)){
					
					String[] codigos =  (String.valueOf(e.getKey()).split("_"));
					String labelParametro = (String)forma.getMapaEtiquetas().get("etiqueta_"+codigos[1]) + " (Días)";									
					forma.getMapaEtiquetas().remove(forma.getMapaEtiquetas().get("etiqueta_"+codigos[1]));
					forma.getMapaEtiquetas().put("etiqueta_"+codigos[1],labelParametro);
					continue;
				}
				
				//Si el parámetro es Máximo tiempo sin evolucionar para inactivar plan de tratamiento
				if((String.valueOf(e.getValue())).equals(
								ConstantesValoresPorDefecto.nombreValoresDefectoMaximoTiempoSinEvolucionarParaInactivarPlanTratamiento)){
					
					String[] codigos =  (String.valueOf(e.getKey()).split("_"));
					String labelParametro = (String)forma.getMapaEtiquetas().get("etiqueta_"+codigos[1]) + " (Días)";									
					forma.getMapaEtiquetas().remove(forma.getMapaEtiquetas().get("etiqueta_"+codigos[1]));
					forma.getMapaEtiquetas().put("etiqueta_"+codigos[1],labelParametro);
					continue;
				}
			}												
		}
	}
	
	
	
	
	
	/**
	 * Llena los datos de la forma en una transacción de Hibernate
	 * @param forma
	 *
	 * @autor Cristhian Murillo
	*/
	private void llenarDatosForma(ValoresPorDefectoForm forma)
	{
		IViasIngresoMundo viasIngresoMundo 				= ManejoPacienteFabricaMundo.crearViasIngresoMundo();;
		IEsquemasTarifariosMundo esquemasTarifarioMundo = FacturacionFabricaMundo.crearEsquemasTarifariosMundo();
		ITiposPacienteMundo tipopaITiposPacienteMundo  	= ManejoPacienteFabricaMundo.crearTiposPacienteMundo();
		
		UtilidadTransaccion.getTransaccion().begin();
		
		/*DTOEstanciaViaIngCentroCosto parametrosViasIngreso = new DTOEstanciaViaIngCentroCosto();
		parametrosViasIngreso.setViaIngreso(ConstantesBD.codigoNuncaValido);
		forma.setListaviasIngreso(viasIngresoMundo.buscarVias(parametrosViasIngreso));*/
		
		forma.setListaviasIngreso(viasIngresoMundo.buscarViasIngreso());
		
		/**
		 * Inc 1679
		 * Se debe mostrar los esquemas tarifarios solo de servicios y que se encuentren activos
		 * Diana Ruiz
		 */
		
		forma.setListaEsquemasTarifarios(esquemasTarifarioMundo.listaEsquemaTarifarioServicios());
		
		forma.setListaTiposPaciente(tipopaITiposPacienteMundo.buscarTiposPaciente());
		
		
		
		UtilidadTransaccion.getTransaccion().commit();
	}

	
	
	
}
