/*
 * @(#)TarifasAction.java
 * 
 * Created on 04-May-2004
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados
 * 
 * Lenguaje : Java
 * Compilador : J2SDK 1.4
 */
package com.princetonsa.action.cargos;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.InfoDatosInt;
import util.Listado;
import util.LogsAxioma;
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.UtilidadValidacion;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.facturacion.UtilidadesFacturacion;

import com.princetonsa.actionform.cargos.ListadoTarifasISSForm;
import com.princetonsa.actionform.cargos.ListadoTarifasSOATForm;
import com.princetonsa.actionform.cargos.TarifaISSForm;
import com.princetonsa.actionform.cargos.TarifaSOATForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.cargos.EsquemaTarifario;
import com.princetonsa.mundo.cargos.TarifaISS;
import com.princetonsa.mundo.cargos.TarifaSOAT;
import com.princetonsa.mundo.cargos.TarifasISS;
import com.princetonsa.mundo.cargos.TarifasSOAT;
import com.princetonsa.mundo.parametrizacion.Servicio;
import com.princetonsa.mundo.parametrizacion.Servicios;

/**
 * Clase para el manejo del tarifas iss y soat, ingreso, modificación,
 * eliminación y consulta
 * 
 * @version 1.0, 04-May-2004
 * @author <a href="mailto:Liliana@PrincetonSA.com">Liliana Caballero</a>
 */
public class TarifasAction extends Action {
	/**
	 * estado del form
	 */
	private String estado;

	/**
	 * tipo de modificacion que se va a realizar sobre la busqueda
	 */
	private String tipoModificacion;
	/**
	 * Para hacer logs de debug / warn / error de esta funcionalidad.
	 */
	private Logger logger = Logger.getLogger(TarifasAction.class);

	/**
	 * Método execute del action
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Connection con=null;
		try{
		
		String tipoBD;

		try {
			tipoBD = System.getProperty("TIPOBD");
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			con = myFactory.getConnection();
		} catch (Exception e) {
			e.printStackTrace();

			logger.warn("Problemas con la base de datos " + e);
			request
					.setAttribute("codigoDescripcionError",
							"errors.problemasBd");
			return mapping.findForward("paginaError");
		}

		HttpSession session = request.getSession();
		UsuarioBasico usuario = (UsuarioBasico) session
				.getAttribute("usuarioBasico");

		if (usuario == null) {
			this.closeConnection(con);

			logger.warn("El usuario no esta cargado (null)");
			request.setAttribute("codigoDescripcionError",
					"errors.usuario.noCargado");
			return mapping.findForward("paginaError");
		}

		if (form instanceof TarifaISSForm) {
			return processTarifaISS(con, usuario, mapping, form, request);
		}

		if (form instanceof TarifaSOATForm) {
			return processTarifaSOAT(con, usuario, mapping, form, request);
		}

		if (form instanceof ListadoTarifasISSForm) {
			return processListadoTarifasISS(con, usuario, mapping, form,
					request);
		}

		if (form instanceof ListadoTarifasSOATForm) {
			return processListadoTarifasSOAT(con, mapping, form, request,
					usuario);
		}

		this.closeConnection(con);
		logger
				.error("El form no es compatible con el form de TarifaISSForm o TarifaSOATForm o TarifasForm");
		request.setAttribute("codigoDescripcionError",
				"errors.formaTipoInvalido");
		
		}catch (Exception e) {
			Log4JManager.error(e);
		}
		finally{
			UtilidadBD.closeConnection(con);
		}
		return mapping.findForward("paginaError");
	}

	/**
	 * @param con
	 * @param usuario
	 * @param mapping
	 * @param form
	 * @param request
	 * @return
	 */
	public ActionForward processTarifaISS(Connection con,
			UsuarioBasico usuario, ActionMapping mapping, ActionForm form,
			HttpServletRequest request) {
		TarifaISSForm tarifaISSForm = (TarifaISSForm) form;

		String estado = tarifaISSForm.getEstado();
		TarifaISSForm forma = (TarifaISSForm) form;
		TarifaISS mundo = new TarifaISS();

		logger.warn("ESTADO->" + estado);

		try {

			if (estado.equalsIgnoreCase("empezar")) {
				int codigoEsquemaTarifario = tarifaISSForm
						.getCodigoEsquemaTarifario();
				// tarifaISSForm.adicionarTarifaInsertadaModificada(mundo.
				// getCodigoServicio());
				
				if (codigoEsquemaTarifario > 0) 
				{
					
					
					tarifaISSForm.reset();
					tarifaISSForm
							.setCodigoEsquemaTarifario(codigoEsquemaTarifario);

					EsquemaTarifario esquema = new EsquemaTarifario();
					
					
					//Por tarea 3565 - Traigo todos los tarifarios existentes. Si existe sonria se muestra, si no, se muestra ISS
					
					ArrayList<HashMap> tarifariosOficiales=new ArrayList<HashMap>();
					tarifariosOficiales=Utilidades.obtenerTarifariosOficiales(con, "");
					
					for(int i=0; i<tarifariosOficiales.size(); i++)
					{
						if(Utilidades.convertirAEntero(tarifariosOficiales.get(i).get("codigotarifario").toString())==ConstantesBD.codigoTarifarioSonria)
							tarifaISSForm.setEsTarifarioSonria(true);
						
					}
					
					logger.info("EXISTE EL TARIFARIO SONRIA QUE ES DIFERENTE A ISS (1)?------>"+tarifaISSForm.isEsTarifarioSonria());
					
					//Fin tarea 3565
					
					
					try {
						esquema.cargarXcodigo(con, codigoEsquemaTarifario,
								usuario.getCodigoInstitucionInt());
						tarifaISSForm.setAcronimoMetodoAjuste(esquema
								.getMetodoAjuste().getAcronimo());
						tarifaISSForm.setNombreEsquemaTarifario(esquema
								.getNombre());
					} catch (SQLException e) {
						logger
								.error("Error cargando Esquema Tarifario en aproximarTarifa de TarifaSOAT: "
										+ e);
					}
					this.closeConnection(con);
					return mapping.findForward("paginaPrincipal");
				} else {
					ActionErrors errores = new ActionErrors();
					errores.add("seleccion esquema tarifario requerido ",
							new ActionMessage("errors.required",
									"El esquema tarifario"));
					saveErrors(request, errores);
					closeConnection(con);
					return mapping.findForward("paginaSeleccionEsquema");
				}
			}

			else if (estado.equalsIgnoreCase("definirtarifainsertar")
					|| estado.equals("definirtarifamodificar")) {
				String paginaRetorno = "";

				if (estado.equalsIgnoreCase("definirtarifainsertar")) {
					paginaRetorno = "paginaPrincipal";
				} else if (estado.equalsIgnoreCase("definirtarifamodificar")) {
					paginaRetorno = "paginaModificar";
				}

				ActionForward salida = this.definirTarifaISS(tarifaISSForm,
						con, mapping, request, paginaRetorno, usuario
								.getCodigoInstitucionInt());

				if (salida == null) { // Definición de la tarifa exitosa
					this.closeConnection(con);
					return mapping.findForward(paginaRetorno);
				} else {
					this.closeConnection(con);
					return salida;
				}
			}

			else if (estado.equals("eliminar")) {
				return accionEliminarISS(con, forma, mundo, mapping, usuario);
			}

			// **********Cambios Tarea 70012************//
			else if (estado.equals("nuevoTarifaISS")) {
				return accionNuevoTarifaISS(con, forma, mundo, mapping, usuario);
			} else if (estado.equals("modificaTarifaISS")) {
				return accionModificaTarifaISS(con, forma, mundo, mapping,
						usuario);
			} else if (estado.equals("guardarNuevoISS")) {
				ActionErrors errores = new ActionErrors();
				errores = validarFechaVigenciaISS(forma);

				if (!errores.isEmpty()) {
					forma.setEstado("nuevoTarifaISS");
					saveErrors(request, errores);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("paginaPrincipal");
				}
				return accionInsertarTarifaISS(con, forma, mundo, mapping,
						usuario);
			} else if (estado.equals("guardarModificacionISS")) {
				ActionErrors errores = new ActionErrors();
				errores = validarFechaVigenciaISS(forma);

				if (!errores.isEmpty()) {
					forma.setEstado("modificaTarifaISS");
					saveErrors(request, errores);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("paginaPrincipal");
				}
				return accionGuardarModTarifaISS(con, forma, mundo, mapping,
						usuario);
			}
			// ****************Fin**********************//

			else if (estado.equalsIgnoreCase("guardar")) {
				logger.info("====>Valor Tarifa >> " + forma.getValorTarifa());
				logger.info("====>Unidades >> " + forma.getUnidades());
				TarifaISS mundoBD = new TarifaISS();
				TarifaISS mundoNuevo = new TarifaISS();
				mundoBD.cargar(con, forma.getCodigoServicio(), forma
						.getCodigoEsquemaTarifario(), "");
				mundoNuevo.cargar(con, forma.getCodigoServicio(), forma
						.getCodigoEsquemaTarifario(), "");
				llenarMundoISS(forma, mundoNuevo, usuario);
				forma.setModificado(true);
				if (UtilidadValidacion
						.existeTarifaISSParaServicio(con, forma
								.getCodigoServicio(), forma
								.getCodigoEsquemaTarifario())) {
					mundo.cargar(con, forma.getCodigoServicio(), forma
							.getCodigoEsquemaTarifario(), "");
					forma.adicionarTarifaInsertadaModificada(mundo
							.getCodigoServicio());
					// se verifica si el registro fue modificado

					//logger.info("compara >> "+forma.getLiquidarAsocios()+" >> "
					// +mundoBD.getLiquidarAsocios());

					if (forma.getValorTarifa() == mundoBD.getValorTarifa()
							&& forma.getPorcentajeIva() == mundoBD
									.getPorcentajeIva()
							&& forma.getCodigoTipoLiquidacion() == mundoBD
									.getCodigoTipoLiquidacion()
							&& forma.getUnidades() == mundoBD.getUnidades()
							&& forma.getLiquidarAsocios() == mundoBD
									.getLiquidarAsocios()) {
						llenarFormaISS(mundo, forma);
						closeConnection(con);
						return mapping.findForward("paginaPrincipal");
					}

					else {
						llenarMundoISS(forma, mundo, usuario);
						ResultadoBoolean resultado = mundo.modificarTarifa(con,
								mundoBD);
						if (resultado.isTrue()) // Modificación exitosa
						{
							this.generarLogTarifaISS(mundoBD, mundoNuevo,
									usuario, true);
						} else {
							ActionErrors errores = new ActionErrors();
							errores.add("modificacion tarifa ",
									new ActionMessage(resultado
											.getDescripcion()));
							// saveErrors(request, errores);
							closeConnection(con);
							return mapping.findForward("paginaPrincipal");
						}
					}
				} else {
					llenarMundoISS(forma, mundo, usuario);
					logger.info("para insertar >> "
							+ mundo.getLiquidarAsocios() + " >> "
							+ mundo.getLiquidarAsociosOriginal());
					mundo.insertarTarifa(con);
					mundo.cargar(con, forma.getCodigoServicio(), forma
							.getCodigoEsquemaTarifario(), "");
					llenarFormaISS(mundo, forma);
				}
				forma.adicionarTarifaInsertadaModificada(mundo
						.getCodigoServicio());
				// forma.setEstado("ingresarModificar");

				closeConnection(con);
				return mapping.findForward("paginaPrincipal");
			}

			else if (estado.equals("ingresarModificar")) {
				return accionIngresarModificarISS(con, tarifaISSForm, mapping,
						mundo, usuario);
			}

			else if (estado.equalsIgnoreCase("resumen")) {
				if (tarifaISSForm.getCodigo() > 0) {
					TarifaISS tarifaISS = new TarifaISS();
					ResultadoBoolean resultado = tarifaISS.consultarTarifa(con,
							tarifaISSForm.getCodigo());

					if (resultado.isTrue()) {
						tarifaISSForm.reset();
						BeanUtils.copyProperties(tarifaISSForm, tarifaISS);
						this.closeConnection(con);
						return mapping.findForward("paginaResumen");
					} else {
						this.closeConnection(con);
						request.setAttribute("codigoDescripcionError",
								resultado.getDescripcion());
						return mapping.findForward("paginaError");
					}
				} else {
					this.closeConnection(con);
					logger
							.error("El código de la tarifa que se intentó consultar no es válido"
									+ tarifaISSForm.getCodigo());
					ArrayList atributosError = new ArrayList();
					atributosError.add("El código de la tarifa ISS");
					request.setAttribute("atributosError", atributosError);
					request.setAttribute("codigoDescripcionError",
							"errors.invalid");
					return mapping.findForward("paginaError");
				}
			} else {
				this.closeConnection(con);
				logger.error("Se intento acceder con un estado inválido "
						+ usuario.getLoginUsuario() + " Estado --> " + estado);
				request.setAttribute("codigoDescripcionError",
						"errors.estadoInvalido");
				return mapping.findForward("paginaError");
			}
		} catch (Exception e) {
			logger
					.error("Se produjo la siguiente excepcion durante la ejecucion de TarifasISS: "
							+ e);
			this.closeConnection(con);
			return null;
		}
	}

	public ActionForward processTarifaSOAT(Connection con,
			UsuarioBasico usuario, ActionMapping mapping, ActionForm form,
			HttpServletRequest request) {
		TarifaSOATForm tarifaSOATForm = (TarifaSOATForm) form;
		TarifaSOAT mundo = new TarifaSOAT();
		String estado = tarifaSOATForm.getEstado();
		TarifaSOATForm forma = (TarifaSOATForm) form;

		try {
			logger.info("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
			logger.info("<<Estado TarifasAction=> " + estado);

			if (estado.equalsIgnoreCase("empezar")) {
				int codigoEsquemaTarifario = tarifaSOATForm
						.getCodigoEsquemaTarifario();

				if (codigoEsquemaTarifario > 0) {
					tarifaSOATForm.reset();
					tarifaSOATForm
							.setCodigoEsquemaTarifario(codigoEsquemaTarifario);

					EsquemaTarifario esquema = new EsquemaTarifario();
					try {
						esquema.cargarXcodigo(con, codigoEsquemaTarifario,
								usuario.getCodigoInstitucionInt());
						tarifaSOATForm.setAcronimoMetodoAjuste(esquema
								.getMetodoAjuste().getAcronimo());
						tarifaSOATForm.setNombreEsquemaTarifario(esquema
								.getNombre());
						double valorTarifa = esquema.getCantidad() / 30;
						tarifaSOATForm.setValorBase(valorTarifa);
					} catch (SQLException e) {
						logger
								.error("Error cargando Esquema Tarifario en aproximar Tarifa de TarifasAction: "
										+ e);
					}
					this.closeConnection(con);
					return mapping.findForward("paginaPrincipal");
				} else {
					ActionErrors errores = new ActionErrors();
					errores.add("seleccion esquema tarifario requerido ",
							new ActionMessage("errors.required",
									"El esquema tarifario"));
					saveErrors(request, errores);
					closeConnection(con);
					return mapping.findForward("paginaSeleccionEsquema");
				}
			} else
			// estado definir nuevo y definir modificar
			if (estado.equalsIgnoreCase("definirtarifainsertar")
					|| estado.equals("definirtarifamodificar")) {
				String paginaRetorno = "";

				if (estado.equalsIgnoreCase("definirtarifainsertar")) {
					logger.info("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
					logger
							.info("<< va a definir tarifa insertar y retorna al principal");
					paginaRetorno = "paginaPrincipal";
				} else if (estado.equalsIgnoreCase("definirtarifamodificar")) {
					logger.info("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
					logger
							.info("<< va a definir tarifa modificar y retorna al Modificar");
					paginaRetorno = "paginaModificar";
				}
				ActionForward salida = this.definirTarifaSoat(tarifaSOATForm,
						con, mapping, request, paginaRetorno, usuario
								.getCodigoInstitucionInt());
				if (salida == null) { // Definición de la tarifa exitosa
					return mapping.findForward(paginaRetorno);
				} else {
					this.closeConnection(con);
					return salida;
				}
			}

			// estado guardar
			else if (estado.equalsIgnoreCase("guardar")) {
				TarifaSOAT mundoBD = new TarifaSOAT();
				TarifaSOAT mundoNuevo = new TarifaSOAT();

				mundoBD.cargar(con, forma.getCodigoServicio(), forma
						.getCodigoEsquemaTarifario(), "");
				mundoNuevo.cargar(con, forma.getCodigoServicio(), forma
						.getCodigoEsquemaTarifario(), "");

				llenarMundoSOAT(forma, mundoNuevo, usuario);

				forma.setModificado(true);
				if (UtilidadValidacion
						.existeTarifaSOATParaServicio(con, forma
								.getCodigoServicio(), forma
								.getCodigoEsquemaTarifario())) {
					mundo.cargar(con, forma.getCodigoServicio(), forma
							.getCodigoEsquemaTarifario(), "");
					forma.adicionarTarifaInsertadaModificada(mundo
							.getCodigoServicio());

					// verificar si hubo modificación
					if (forma.getValorTarifa() == mundoBD.getValorTarifa()
							&& forma.getPorcentajeIva() == mundoBD
									.getPorcentajeIva()
							&& forma.getCodigoTipoLiquidacion() == mundoBD
									.getCodigoTipoLiquidacion()
							&& Utilidades.convertirADouble(forma.getGrupoString(),true) == mundoBD.getGrupo()
							&& forma.getLiquidarAsocios() == mundoBD
									.getLiquidarAsocios()) {
						llenarFormaSOAT(mundo, forma);
						closeConnection(con);

						logger.info("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<que hizo");
						logger.info("<< se metio al guardar y.. ");

						return mapping.findForward("paginaPrincipal");
					} else {
						llenarMundoSOAT(forma, mundo, usuario);
						ResultadoBoolean resultado = mundo.modificarTarifa(con,
								mundoBD);
						if (resultado.isTrue()) { // Modificación exitosa
							this.generarLogTarifaSOAT(mundoBD, mundoNuevo,
									usuario, true);
						} else {
							ActionErrors errores = new ActionErrors();
							errores.add("modificacion tarifa ",
									new ActionMessage(resultado
											.getDescripcion()));
							// saveErrors(request, errores);
							closeConnection(con);
							logger
									.info("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<que hizo aca");
							logger
									.info("<< se metio al guardar e hizo esto.. ");

							return mapping.findForward("paginaPrincipal");
						}
					}

				} else {
					llenarMundoSOAT(forma, mundo, usuario);
					mundo.insertarTarifa(con);
					mundo.cargar(con, forma.getCodigoServicio(), forma
							.getCodigoEsquemaTarifario(), "");
					llenarFormaSOAT(mundo, forma);
				}
				forma.adicionarTarifaInsertadaModificada(mundo
						.getCodigoServicio());
				// forma.setEstado("ingresarModificar");
				forma.setEstado("guardar");

				logger.info("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<que hizo esto");
				logger.info("<< se metio al guardar y.. esto que o ke ");
				closeConnection(con);
				return mapping.findForward("paginaPrincipal");

			}

			else if (estado.equals("ingresarModificar")) {
				return accionIngresarModificarSOAT(con, tarifaSOATForm,
						mapping, mundo, usuario);
			}

			else if (estado.equals("eliminar")) {
				return accionEliminarSOAT(con, forma, mundo, mapping, usuario);
			}

			// **********Cambios Tarea 70012************//
			else if (estado.equals("nuevoTarifaSOAT")) {
				return accionNuevoTarifaSOAT(con, forma, mundo, mapping,
						usuario);
			} else if (estado.equals("modificaTarifaSOAT")) {
				return accionModificaTarifaSOAT(con, forma, mundo, mapping,
						usuario);
			} else if (estado.equals("guardarNuevoSOAT")) {
				ActionErrors errores = new ActionErrors();
				errores = validarFechaVigenciaSOAT(forma);

				if (!errores.isEmpty()) {
					forma.setEstado("nuevoTarifaSOAT");
					saveErrors(request, errores);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("paginaPrincipal");
				}
				return accionInsertarTarifaSOAT(con, forma, mundo, mapping,
						usuario);
			} else if (estado.equals("guardarModificacionSOAT")) {
				ActionErrors errores = new ActionErrors();
				errores = validarFechaVigenciaSOAT(forma);

				if (!errores.isEmpty()) {
					forma.setEstado("modificaTarifaSOAT");
					saveErrors(request, errores);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("paginaPrincipal");
				}
				return accionGuardarModTarifaSOAT(con, forma, mundo, mapping,
						usuario);
			}
			// ****************Fin**********************//

			else if (estado.equalsIgnoreCase("resumen")) {
				if (tarifaSOATForm.getCodigo() > 0) {
					TarifaSOAT tarifaSOAT = new TarifaSOAT();
					ResultadoBoolean resultado = tarifaSOAT.consultarTarifa(
							con, tarifaSOATForm.getCodigo());

					if (resultado.isTrue()) {
						tarifaSOATForm.reset();
						BeanUtils.copyProperties(tarifaSOATForm, tarifaSOAT);
						this.closeConnection(con);
						return mapping.findForward("paginaResumen");
					} else {
						this.closeConnection(con);
						request.setAttribute("codigoDescripcionError",
								resultado.getDescripcion());
						return mapping.findForward("paginaError");
					}
				} else {
					this.closeConnection(con);
					logger
							.error("El código de la tarifa que se intentó consultar no es válido"
									+ tarifaSOATForm.getCodigo());
					ArrayList atributosError = new ArrayList();
					atributosError.add("El código de la tarifa soat");
					request.setAttribute("atributosError", atributosError);
					request.setAttribute("codigoDescripcionError",
							"errors.invalid");
					return mapping.findForward("paginaError");
				}
			} else {
				this.closeConnection(con);
				logger.error("Se intento acceder con un estado inválido "
						+ usuario.getLoginUsuario() + " Estado --> " + estado);
				request.setAttribute("codigoDescripcionError",
						"errors.estadoInvalido");
				return mapping.findForward("paginaError");
			}
		} catch (Exception e) {
			logger
					.error("Se produjo la siguiente excepcion durante la ejecucion de TarifasSOAT: "
							+ e);
			this.closeConnection(con);
			return null;
		}
	}

	public ActionForward processListadoTarifasISS(Connection con,
			UsuarioBasico usuario, ActionMapping mapping, ActionForm form,
			HttpServletRequest request) {
		ListadoTarifasISSForm listadoTarifasISSForm = (ListadoTarifasISSForm) form;
		TarifasISS mundo = new TarifasISS();
		this.tipoModificacion = listadoTarifasISSForm.getTipoModificacion();
		this.estado = listadoTarifasISSForm.getEstado();
		logger.info("\nprocessListadoTarifasISS---------------------->"
				+ estado + "\n");
		logger.info("===>Código del Servicio: "
				+ listadoTarifasISSForm.getCodigoServicioPropietario());
		try {
			if (estado.equals("iniciarBusqueda")) {
				if (listadoTarifasISSForm.getCodigoEsquemaTarifario() > 0) {
					return this.accionEmpezarBusquedaISS(mapping, con,
							listadoTarifasISSForm, usuario);
				} else {
					ActionErrors errores = new ActionErrors();
					errores.add("seleccion esquema tarifario requerido ",
							new ActionMessage("errors.required",
									"El esquema tarifario"));
					saveErrors(request, errores);
					closeConnection(con);
					return mapping.findForward("paginaSeleccionEsquema");
				}
			} else if (estado.equals("buscar")) {
				return this.accionBuscarISS(mapping, usuario, request, con,
						listadoTarifasISSForm);
			} else if (estado.equals("consultarFechas")) {
				return this.accionConsultarFechasISS(mapping, usuario, request,
						con, listadoTarifasISSForm, mundo);
			} else if (this.estado.equals("ordenar")) {
				return accionOrdenarTarifaISS(mapping, con,
						listadoTarifasISSForm);
			} else if (this.estado.equals("menu")) {
				listadoTarifasISSForm.reset(usuario.getCodigoInstitucionInt());
				this.closeConnection(con);
				return mapping.findForward("menu");
			} else if (this.estado.equals("imprimir")) {
				listadoTarifasISSForm.setFechasVigenciaMap(mundo
						.consultarFechasVigencia(con, listadoTarifasISSForm
								.getCodigoEsquemaTarifario()
								+ "", "", listadoTarifasISSForm
								.getCadenaCodigosServicios()));
				this.closeConnection(con);
				return mapping.findForward("imprimir");
			}
		} catch (Exception e) {
			logger
					.error("Se produjo la siguiente excepcion durante la ejecucion de TarifasISS: "
							+ e);
			e.printStackTrace();
			this.closeConnection(con);
			return null;
		}
		this.closeConnection(con);
		return null;
	}

	public ActionForward processListadoTarifasSOAT(Connection con,
			ActionMapping mapping, ActionForm form, HttpServletRequest request,
			UsuarioBasico usuario) {

		ListadoTarifasSOATForm listadoTarifasSOATForm = (ListadoTarifasSOATForm) form;
		TarifasSOAT mundo = new TarifasSOAT();
		this.tipoModificacion = listadoTarifasSOATForm.getTipoModificacion();
		this.estado = listadoTarifasSOATForm.getEstado();
		logger.info("ESTADO LISTADO TARIFAS SOAT=> " + estado);
		logger.info("===>Código del Servicio: "
				+ listadoTarifasSOATForm.getCodigoServicioPropietario());
		try {

			if (estado.equals("iniciarBusqueda")) {
				if (listadoTarifasSOATForm.getCodigoEsquemaTarifario() > 0) {
					return this.accionEmpezarBusquedaSOAT(mapping, con,
							listadoTarifasSOATForm, usuario);
				} else {
					ActionErrors errores = new ActionErrors();
					errores.add("seleccion esquema tarifario requerido ",
							new ActionMessage("errors.required",
									"El esquema tarifario"));
					saveErrors(request, errores);
					closeConnection(con);
					return mapping.findForward("paginaSeleccionEsquema");
				}
			} else if (estado.equals("buscar")) {
				return this.accionBuscarSOAT(mapping, request, con,
						listadoTarifasSOATForm);
			} else if (estado.equals("consultarFechas")) {
				return this.accionConsultarFechasSOAT(mapping, request, con,
						listadoTarifasSOATForm, mundo);
			} else if (this.estado.equals("ordenar")) {
				return accionOrdenarTarifaSOAT(mapping, con,
						listadoTarifasSOATForm);
			} else if (this.estado.equals("menu")) {
				listadoTarifasSOATForm.reset(usuario.getCodigoInstitucionInt());
				this.closeConnection(con);
				return mapping.findForward("menu");
			} else if (this.estado.equals("imprimir")) {
				listadoTarifasSOATForm.setFechasVigenciaMap(mundo
						.consultarFechasVigencia(con, listadoTarifasSOATForm
								.getCodigoEsquemaTarifario()
								+ "", "", listadoTarifasSOATForm
								.getCadenaCodigosServicios()));
				this.closeConnection(con);
				return mapping.findForward("imprimir");
			}
		} catch (Exception e) {
			logger
					.error("Se produjo la siguiente excepcion durante la ejecucion de TarifasISS: "
							+ e);
			e.printStackTrace();
			this.closeConnection(con);
			return null;
		}
		this.closeConnection(con);
		return null;
	}

	/**
	 * Cierra la conexión en el caso que esté abierta
	 * 
	 * @param con
	 * @throws SQLException
	 */
	private void closeConnection(Connection con) {

		try {
			if (con != null && !con.isClosed())
				UtilidadBD.closeConnection(con);
		} catch (SQLException e) {
			logger.warn("no se pudo cerrar la conexion con la base de datos: "
					+ e);
		}
	}

	/**
	 * Define el valor de la tarifa soat en el caso de las unidades
	 * 
	 * @param tarifaSOATForm
	 *            , form donde se encuentran los datos de la tarifa a definir
	 * @param con
	 *            , conexion abierta con la base de datos
	 * @param mapping
	 *            , action mapping para hacer los forward
	 * @param request
	 *            , request
	 * @return null si la asignacion de la tarifa fue exitosa,o un actionforward
	 *         de lo contrario
	 * @throws Exception
	 */
	private ActionForward definirTarifaSoat(TarifaSOATForm tarifaSOATForm,
			Connection con, ActionMapping mapping, HttpServletRequest request,
			String paginaRetorno, int institucion) throws Exception {

		if (tarifaSOATForm.getCodigoTipoLiquidacion() == ConstantesBD.codigoTipoLiquidacionSoatUnidades) // Por
																											// unidades
																											// se
																											// calcula
																											// automáticamente
																											// el
																											// valor
		{

			if (tarifaSOATForm.getCodigoServicio() > 0) {
				// Se cargan las unidades Soat del servicio
				Servicio servicio = new Servicio(tarifaSOATForm
						.getCodigoServicio());
				servicio.cargarCodigosParticulares(con,institucion);

				// se carga los datos del esquema tarifario (salario minimo)
				EsquemaTarifario esquemaTar = new EsquemaTarifario();
				esquemaTar.cargarXcodigo(con, tarifaSOATForm
						.getCodigoEsquemaTarifario(), institucion);

				double valorTarifa = esquemaTar.getCantidad() / 30;
				// se alamcena el valor base en la forma
				tarifaSOATForm.setValorBase(Double.parseDouble(UtilidadTexto
						.darFormatoADouble(valorTarifa)));

				// se alamcenan las unidades
				tarifaSOATForm.setGrupoString(servicio.getUnidadesSoatFloat()+"");
				valorTarifa *= servicio.getUnidadesSoatFloat();

				// se hace la aproximación del valor según el esquema tarifario

				valorTarifa = UtilidadValidacion
						.aproximarMetodoAjuste(esquemaTar.getMetodoAjuste()
								.getAcronimo(), valorTarifa);

				tarifaSOATForm.setValorTarifa(Double.parseDouble(UtilidadTexto
						.darFormatoADouble(valorTarifa)));

				return null;
			} else {
				tarifaSOATForm.setCodigoTipoLiquidacion(-1);
				tarifaSOATForm.setValorTarifa(0.0);
				tarifaSOATForm.setValorTarifaString("0");
				ActionErrors errores = new ActionErrors();
				errores.add("seleccionar servicio liquidacion por unidades",
						new ActionMessage(
								"error.tarifaSOAT.seleccionarServicio"));
				saveErrors(request, errores);
				closeConnection(con);
				return mapping.findForward(paginaRetorno);
			}

		} else if (tarifaSOATForm.getCodigoTipoLiquidacion() == ConstantesBD.codigoTipoLiquidacionSoatValor
				|| tarifaSOATForm.getCodigoTipoLiquidacion() == ConstantesBD.codigoTipoLiquidacionSoatGrupo) // Por
																												// unidades
		{
			tarifaSOATForm.setValorTarifa(0.0);
			tarifaSOATForm.setValorTarifaString("0");
			tarifaSOATForm.setGrupoString("0.0");
			return null;
		} else {
			return null;
		}
	}

	/**
	 * realizar busqueda de tarifa SOAT
	 * 
	 * @param mapping
	 * @param request
	 * @param con
	 * @param listadoTarifasSOATForm
	 * @return ActionForward
	 * @throws SQLException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */

	private ActionForward accionBuscarSOAT(ActionMapping mapping,
			HttpServletRequest request, Connection con,
			ListadoTarifasSOATForm listadoTarifasSOATForm) throws SQLException,
			IllegalAccessException, InvocationTargetException {

		Collection listado;
		boolean parametros[] = new boolean[9];
		for (int j = 0; j < parametros.length; j++) {
			parametros[j] = false;
		}
		String criteriosBusqueda[] = listadoTarifasSOATForm
				.getCriteriosBusqueda();
		if (criteriosBusqueda != null) {
			for (int i = 0; i < criteriosBusqueda.length; i++) {
				if (criteriosBusqueda[i].equals("codigo")) {
					parametros[0] = true;
				}

				if (criteriosBusqueda[i].equals("esquemaTarifario")) {
					parametros[1] = true;
				}
				if (criteriosBusqueda[i].equals("grupo")) {
					parametros[2] = true;
				}
				if (criteriosBusqueda[i].equals("valorTarifa")) {
					parametros[3] = true;
				}
				if (criteriosBusqueda[i].equals("porcentajeIva")) {
					parametros[4] = true;
				}
				if (criteriosBusqueda[i].equals("codigoTipoLiquidacion")) {
					parametros[5] = true;
				}
				if (criteriosBusqueda[i].equals("codigoServicio")) {
					parametros[6] = true;
				}
				if (criteriosBusqueda[i].equals("nombreServicio")) {
					parametros[7] = true;
				}
				if (criteriosBusqueda[i].equals("liquidarAsocios")) {
					parametros[8] = true;
				}

			}
		}

		// siempre se debe filtrar por esquema tarifario
		parametros[1] = true;

		TarifasSOAT ts = new TarifasSOAT();
		ts.consultar(con, listadoTarifasSOATForm.getCodigo(), parametros[0],
				listadoTarifasSOATForm.getCodigoEsquemaTarifario(),
				parametros[1], listadoTarifasSOATForm
						.getCodigoServicioPropietario(), parametros[6],
				listadoTarifasSOATForm.getGrupo(), parametros[2],
				listadoTarifasSOATForm.getCodigoTipoLiquidacion(),
				parametros[5], listadoTarifasSOATForm.getValorTarifa(),
				parametros[3], listadoTarifasSOATForm.getPorcentajeIva(),
				parametros[4], listadoTarifasSOATForm.getNombreServicio(),
				parametros[7], listadoTarifasSOATForm
						.getManejaConversionMoneda(), listadoTarifasSOATForm
						.getLiquidarAsocios(), parametros[8],
				listadoTarifasSOATForm.getIndex(), listadoTarifasSOATForm
						.getTiposMonedaTagMap());

		listado = ts.getTarifasSOAT();
		listado = validacionListadoSOAT(listado);
		// añadirlo al form
		listadoTarifasSOATForm.setTarifasSOAT(new ArrayList(listado));

		listadoTarifasSOATForm.setCadenaCodigosServicios(ts
				.getCadenaCodigosServicios());

		request.getSession().setAttribute("ultimaBusquedaTarifaSOAT",
				"listadoTarifasSOAT.do?estado=" + this.estado);
		this.closeConnection(con);

		return mapping.findForward("listado");

	}

	private ActionForward accionConsultarFechasSOAT(ActionMapping mapping,
			HttpServletRequest request, Connection con,
			ListadoTarifasSOATForm forma, TarifasSOAT mundo) {
		forma.setFechasVigenciaMap(mundo.consultarFechasVigencia(con, forma
				.getCodigoEsquemaTarifario()
				+ "", forma.getCodigoServicio() + "", ""));
		this.closeConnection(con);
		return mapping.findForward("fechasVigenciaSOAT");
	}

	/**
	 * Realizar busqueda de tarifa ISS
	 * 
	 * @param mapping
	 * @param usuario
	 * @param request
	 * @param con
	 * @param listadoTarifasISSForm
	 * @return ActionForward
	 * @throws SQLException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	private ActionForward accionBuscarISS(ActionMapping mapping,
			UsuarioBasico usuario, HttpServletRequest request, Connection con,
			ListadoTarifasISSForm listadoTarifasISSForm) throws SQLException,
			IllegalAccessException, InvocationTargetException {
		listadoTarifasISSForm.setMaxPageItems(Integer
				.parseInt(ValoresPorDefecto.getMaxPageItems(usuario
						.getCodigoInstitucionInt())));
		Collection listado;
		boolean parametros[] = new boolean[8];
		for (int j = 0; j < parametros.length; j++) {
			parametros[j] = false;
		}
		String criteriosBusqueda[] = listadoTarifasISSForm
				.getCriteriosBusqueda();
		if (criteriosBusqueda != null) {
			for (int i = 0; i < criteriosBusqueda.length; i++) {
				if (criteriosBusqueda[i].equals("codigo")) {
					parametros[0] = true;
				}
				if (criteriosBusqueda[i].equals("codigoEsquemaTarifario")) {
					parametros[1] = true;
				}
				/*
				 * if(criteriosBusqueda[i].equals("puntos")){
				 * parametros[2]=true; }
				 */
				if (criteriosBusqueda[i].equals("valorTarifa")) {
					parametros[2] = true;
				}
				if (criteriosBusqueda[i].equals("porcentajeIva")) {
					parametros[3] = true;
				}
				if (criteriosBusqueda[i].equals("codigoServicio")) {
					parametros[4] = true;
				}
				if (criteriosBusqueda[i].equals("nombreServicio")) {
					parametros[5] = true;
				}
				if (criteriosBusqueda[i].equals("codigoTipoLiquidacion")) {
					parametros[6] = true;
				}
				if (criteriosBusqueda[i].equals("liquidarAsocios")) {
					parametros[7] = true;
				}
			}
		}
		TarifasISS ti = new TarifasISS();
		ti.consultar(con,
				listadoTarifasISSForm.getCodigo(),
				parametros[0],
				listadoTarifasISSForm.getCodigoEsquemaTarifario(),
				parametros[1],
				listadoTarifasISSForm.getCodigoServicioPropietario(),
				parametros[4],
				// listadoTarifasISSForm.getPuntos(),parametros[2],
				listadoTarifasISSForm.getValorTarifa(), parametros[2],
				listadoTarifasISSForm.getPorcentajeIva(), parametros[3],
				listadoTarifasISSForm.getNombreServicio(), parametros[5],
				listadoTarifasISSForm.getCodigoTipoLiquidacion(),
				parametros[6], listadoTarifasISSForm
						.getManejaConversionMoneda(), listadoTarifasISSForm
						.getLiquidarAsocios(), parametros[7],
				listadoTarifasISSForm.getIndex(), listadoTarifasISSForm
						.getTiposMonedaTagMap(), usuario
						.getCodigoInstitucionInt());
		listadoTarifasISSForm.setCadenaCodigosServicios(ti
				.getCadenaCodigosServicios());

		listado = ti.getTarifasISS();
		listado = validacionListadoISS(listado);
		// añadirlo al form
		listadoTarifasISSForm.setTarifasISS(new ArrayList(listado));
		if (listado.size() > 0)
			listadoTarifasISSForm
					.setNombreEsquemaTarifario(((TarifaISSForm) listadoTarifasISSForm
							.getTarifasISS().get(0))
							.getNombreEsquemaTarifario());

		request.getSession().setAttribute("ultimaBusquedaTarifaISS",
				"listadoTarifasISS.do?estado=" + this.estado);
		this.closeConnection(con);

		return mapping.findForward("listado");

	}

	/**
	 * Metodo que consulta todas las fechas por tarifa iss segun esquema
	 * servicio
	 * 
	 * @param mapping
	 * @param usuario
	 * @param request
	 * @param con
	 * @param listadoTarifasISSForm
	 * @return
	 * @throws SQLException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	private ActionForward accionConsultarFechasISS(ActionMapping mapping,
			UsuarioBasico usuario, HttpServletRequest request, Connection con,
			ListadoTarifasISSForm forma, TarifasISS mundo) {
		forma.setFechasVigenciaMap(mundo.consultarFechasVigencia(con, forma
				.getCodigoEsquemaTarifario()
				+ "", forma.getCodigoServicio() + "", ""));
		this.closeConnection(con);
		return mapping.findForward("fechasVigenciaISS");
	}

	/**
	 * Method accionEmpezarBusqueda.
	 * 
	 * @param mapping
	 * @param request
	 * @param con
	 * @param listadoRecargosTarifasForm
	 * @return ActionForward
	 */
	private ActionForward accionEmpezarBusquedaSOAT(ActionMapping mapping,
			Connection con, ListadoTarifasSOATForm listadoTarifasSOATForm,
			UsuarioBasico usuario) throws SQLException {
		this.closeConnection(con);
		int codigoesuqemaTarifario = listadoTarifasSOATForm
				.getCodigoEsquemaTarifario();
		listadoTarifasSOATForm.reset(usuario.getCodigoInstitucionInt());
		listadoTarifasSOATForm
				.setCodigoEsquemaTarifario(codigoesuqemaTarifario);
		return mapping.findForward("busquedaAvanzadaSOAT");

	}

	/**
	 * Ordenar tarifas SOAT
	 * 
	 * @param mapping
	 * @param request
	 * @param response
	 * @param con
	 * @param listadoTarifasSOATForm
	 * @return ActionForward
	 * @throws Exception
	 */
	private ActionForward accionOrdenarTarifaSOAT(ActionMapping mapping,
			Connection con, ListadoTarifasSOATForm listadoTarifasSOATForm)
			throws Exception {

		listadoTarifasSOATForm.setTarifasSOAT(Listado.ordenarColumna(
				listadoTarifasSOATForm.getTarifasSOAT(), listadoTarifasSOATForm
						.getUltimaPropiedad(), listadoTarifasSOATForm
						.getColumna()));
		listadoTarifasSOATForm.setUltimaPropiedad(listadoTarifasSOATForm
				.getColumna());
		closeConnection(con);
		return mapping.findForward("listado");
	}

	private ActionForward accionOrdenarTarifaISS(ActionMapping mapping,
			Connection con, ListadoTarifasISSForm listadoTarifasISSForm)
			throws Exception {

		listadoTarifasISSForm.setTarifasISS(Listado.ordenarColumna(
				listadoTarifasISSForm.getTarifasISS(), listadoTarifasISSForm
						.getUltimaPropiedad(), listadoTarifasISSForm
						.getColumna()));
		listadoTarifasISSForm.setUltimaPropiedad(listadoTarifasISSForm
				.getColumna());

		closeConnection(con);
		return mapping.findForward("listado");
	}

	/**
	 * @param listado
	 *            de solicitudes resultado de la busqueda
	 * @return lista validada con las reglas de solicitudes y modificada para
	 *         mostrar con el tag display:*
	 */
	private Collection validacionListadoSOAT(Collection listado)
			throws SQLException, IllegalAccessException,
			InvocationTargetException {
		// añadir a listado definitivo
		ArrayList listadoTemporal = new ArrayList(listado);
		Collection c = new ArrayList();

		// recorrer el listado
		for (int i = 0; i < listadoTemporal.size(); i++) {

			// validar condiciones
			TarifaSOAT temp = (TarifaSOAT) listadoTemporal.get(i);
			TarifaSOATForm cf = new TarifaSOATForm();
			BeanUtils.copyProperties(cf, temp);
			if (this.tipoModificacion.equals("modificar")) {
				cf
						.setEnlace("modificarTarifaSOAT.do?estado=empezarmodificar&codigo="
								+ temp.getCodigo());
			} else if (this.tipoModificacion.equals("consulta")) {
				cf.setEnlace("tarifaSOAT.do?estado=resumen&codigo="
						+ temp.getCodigo());
			}
			c.add(cf);
		}
		return c;
	}

	private Collection validacionListadoISS(Collection listado)
			throws SQLException, IllegalAccessException,
			InvocationTargetException {
		// añadir a listado definitivo
		ArrayList listadoTemporal = new ArrayList(listado);
		Collection c = new ArrayList();

		// recorrer el listado
		for (int i = 0; i < listadoTemporal.size(); i++) {

			// validar condiciones
			TarifaISS temp = (TarifaISS) listadoTemporal.get(i);
			TarifaISSForm cf = new TarifaISSForm();
			BeanUtils.copyProperties(cf, temp);
			if (this.tipoModificacion.equals("modificar")) {
				cf
						.setEnlace("modificarTarifaISS.do?estado=empezarmodificar&codigo="
								+ temp.getCodigo());
			} else if (this.tipoModificacion.equals("consulta")) {
				cf.setEnlace("tarifaISS.do?estado=resumen&codigo="
						+ temp.getCodigo());
			}
			c.add(cf);
		}
		return c;
	}

	/**
	 * Returns the estado.
	 * 
	 * @return String
	 */
	public String getEstado() {
		return estado;
	}

	/**
	 * Returns the tipoModificacion.
	 * 
	 * @return String
	 */
	public String getTipoModificacion() {
		return tipoModificacion;
	}

	/**
	 * Sets the estado.
	 * 
	 * @param estado
	 *            The estado to set
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	}

	/**
	 * Sets the tipoModificacion.
	 * 
	 * @param tipoModificacion
	 *            The tipoModificacion to set
	 */
	public void setTipoModificacion(String tipoModificacion) {
		this.tipoModificacion = tipoModificacion;
	}

	/**
	 * enviar a pagina de busqueda ISS
	 * 
	 * @param mapping
	 * @param request
	 * @param con
	 * @param listadoTarifasISSForm
	 * @return ActionForward
	 * @throws SQLException
	 */
	private ActionForward accionEmpezarBusquedaISS(ActionMapping mapping,
			Connection con, ListadoTarifasISSForm listadoTarifasISSForm,
			UsuarioBasico usuario) throws SQLException {
		this.closeConnection(con);
		int codEsquemaTarifario = listadoTarifasISSForm
				.getCodigoEsquemaTarifario();
		listadoTarifasISSForm.reset(usuario.getCodigoInstitucionInt());
		listadoTarifasISSForm.setCodigoEsquemaTarifario(codEsquemaTarifario);
		return mapping.findForward("busquedaAvanzadaISS");
	}

	/**
	 * Genera un log en el archivo de logs de tarifas
	 * 
	 * @param tarifaISSOriginal
	 *            , null en el caso de eliminar, la tarifa original en el caso
	 *            de modificar
	 * @param tarifaISSNueva
	 * @param usuario
	 * @param esModificar
	 */
	private void generarLogTarifaISS(TarifaISS tarifaISSOriginal,
			TarifaISS tarifaISSNueva, UsuarioBasico usuario, boolean esModificar) {
		String logCambiosTarifa = new String();
		logCambiosTarifa = "\nTARIFA ISS";
		int tipoRegistro = 0;

		/*
		 * Si es modificar se deben meter tanto los datos originales como los
		 * nuevos
		 */
		if (esModificar) {
			tipoRegistro = ConstantesBD.tipoRegistroLogModificacion;

			logCambiosTarifa += "\nDATOS ORIGINALES\n";
			logCambiosTarifa += tarifaISSOriginal.getCadenaLogTarifa();
		} else {
			tipoRegistro = ConstantesBD.tipoRegistroLogEliminacion;
		}

		logCambiosTarifa += "\nDATOS TARIFA\n";
		logCambiosTarifa += tarifaISSNueva.getCadenaLogTarifa();
		logCambiosTarifa += "\n\n";

		LogsAxioma.enviarLog(ConstantesBD.logTarifasCodigo, logCambiosTarifa,
				tipoRegistro, usuario.getLoginUsuario());
	}

	/**
	 * Genera un log en el archivo de logs de tarifas
	 * 
	 * @param tarifaSOATOriginal
	 *            , null en el caso de eliminar, la tarifa original en el caso
	 *            de modificar
	 * @param tarifaSOATNueva
	 * @param usuario
	 * @param esModificar
	 */
	private void generarLogTarifaSOAT(TarifaSOAT tarifaSOATOriginal,
			TarifaSOAT tarifaSOATNueva, UsuarioBasico usuario,
			boolean esModificar) {
		String logCambiosTarifa = new String();
		logCambiosTarifa = "\nTARIFA SOAT";
		int tipoRegistro = 0;
		/*
		 * Si es modificar se deben meter tanto los datos originales como los
		 * nuevos
		 */
		if (esModificar) {
			tipoRegistro = ConstantesBD.tipoRegistroLogModificacion;

			logCambiosTarifa += "\nDATOS ORIGINALES\n";
			logCambiosTarifa += tarifaSOATOriginal.getCadenaLogTarifa();
		} else {
			tipoRegistro = ConstantesBD.tipoRegistroLogEliminacion;
		}

		logCambiosTarifa += "\nDATOS TARIFA\n";
		logCambiosTarifa += tarifaSOATNueva.getCadenaLogTarifa();
		logCambiosTarifa += "\n\n";

		LogsAxioma.enviarLog(ConstantesBD.logTarifasCodigo, logCambiosTarifa,
				tipoRegistro, usuario.getInformacionGeneralPersonalSalud());
	}

	/**
	 * Define el valor de la tarifa iss en el caso de las unidades
	 * 
	 * @param tarifaISSForm
	 *            , form donde se encuentran los datos de la tarifa a definir
	 * @param con
	 *            , conexion abierta con la base de datos
	 * @param mapping
	 *            , action mapping para hacer los forward
	 * @param request
	 *            , request
	 * @return null si la asignacion de la tarifa fue exitosa,o un actionforward
	 *         de lo contrario
	 * @throws Exception
	 */
	private ActionForward definirTarifaISS(TarifaISSForm tarifaISSForm,
			Connection con, ActionMapping mapping, HttpServletRequest request,
			String paginaRetorno, int institucion) throws Exception {
		if (tarifaISSForm.getCodigoTipoLiquidacion() == ConstantesBD.codigoTipoLiquidacionSoatUnidades) // Por
																										// unidades
																										// se
																										// calcula
																										// automáticamente
																										// el
																										// valor
		{
			tarifaISSForm.setValorTarifa(0);
			if (tarifaISSForm.getCodigoServicio() > 0) {
				Servicio servicio = new Servicio(tarifaISSForm
						.getCodigoServicio());
				servicio.consultaUnidadesUvr(con);
				try {

					if (servicio.getUnidadesUvr() <= 0) {
						ActionErrors errores = new ActionErrors();
						errores
								.add(
										"inserción excepción",
										new ActionMessage(
												"errors.notEspecific",
												"No Existen Unidades ISS. Por favor verifique la parametrizacion del servicio."));
						logger.warn("No esta parametrizado el UVR");
						saveErrors(request, errores);
						closeConnection(con);
						return mapping.findForward("paginaRetorno");
					}
					// se almacena el valor de las unidades UVR
					tarifaISSForm.setUnidades(servicio.getUnidadesUvr());
					// se hace la aproximación del valor según el esquema
					// tarifario
					EsquemaTarifario esquemaTar = new EsquemaTarifario();
					esquemaTar.cargarXcodigo(con, tarifaISSForm
							.getCodigoEsquemaTarifario(), institucion);
					// se obtiene el valor base del esquema tarifario
					double valorTarifaISS = esquemaTar.getCantidad();

					// se asigna el valor base a la forma
					tarifaISSForm.setValorBase(valorTarifaISS);
					if (valorTarifaISS <= 0) {
						ActionErrors errores = new ActionErrors();
						errores.add("inserción excepción", new ActionMessage(
								"errors.faltaParametrizacion",
								"UVR en el esquema tarifario"));
						logger.warn("No esta parametrizado el UVR");
						saveErrors(request, errores);
						closeConnection(con);
						return mapping.findForward("paginaRetorno");
					}
					valorTarifaISS *= servicio.getUnidadesUvr();

					valorTarifaISS = UtilidadValidacion.aproximarMetodoAjuste(
							esquemaTar.getMetodoAjuste().getAcronimo(),
							valorTarifaISS);

					tarifaISSForm.setValorTarifa(valorTarifaISS);
					return null;
				} catch (NumberFormatException e) {
					ActionErrors errores = new ActionErrors();
					errores.add("inserción excepción", new ActionMessage(
							"errors.faltaParametrizacion", "UVR"));
					logger.warn("No esta parametrizado el UVR");
					saveErrors(request, errores);
					closeConnection(con);
					return mapping.findForward("paginaRetorno");
				}
			} else {
				tarifaISSForm.setCodigoTipoLiquidacion(-1);
				tarifaISSForm.setValorTarifa(0.0);
				tarifaISSForm.setValorTarifaString("0");
				ActionErrors errores = new ActionErrors();
				errores.add("seleccionar servicio liquidacion por unidades",
						new ActionMessage(
								"error.tarifaSOAT.seleccionarServicio"));
				saveErrors(request, errores);
				closeConnection(con);
				return mapping.findForward(paginaRetorno);
			}
		} else if (tarifaISSForm.getCodigoTipoLiquidacion() == ConstantesBD.codigoTipoLiquidacionSoatValor
				|| tarifaISSForm.getCodigoTipoLiquidacion() == ConstantesBD.codigoTipoLiquidacionSoatUvr) // Por
																											// valor
		{
			tarifaISSForm.setValorTarifa(0.0);
			tarifaISSForm.setUnidades(0.0);
			tarifaISSForm.setValorTarifaString("0");
			return null;
		} else {
			return null;
		}
	}

	/**
	 * Método que controla la página de ingreso y modificación
	 * 
	 * @param con
	 * @param forma
	 * @param mapping
	 * @param mundo
	 * @return Página de Ingreso Modificación
	 */
	private ActionForward accionIngresarModificarISS(Connection con,
			TarifaISSForm forma, ActionMapping mapping, TarifaISS mundo,
			UsuarioBasico usuario) {
		
		
		// **********Modificacion Tarea 70012**********//
		forma.setTarifasMap(mundo.consultaTodasTarifas(con, forma
				.getCodigoEsquemaTarifario(), forma.getCodigoServicio() + "", ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(usuario.getCodigoInstitucionInt())));
		for (int i = 0; i < Utilidades.convertirAEntero(forma
				.getTarifasMap("numRegistros")
				+ ""); i++) {
			if (forma.getTarifasMap("fechavigencia_" + i).toString() != null
					&& !forma.getTarifasMap("fechavigencia_" + i).toString()
							.equals("")) {
				if (UtilidadFecha.esFechaMenorIgualQueOtraReferencia(
						UtilidadFecha.getFechaActual(), UtilidadFecha
								.conversionFormatoFechaAAp(forma
										.getTarifasMap("fechavigencia_" + i)
										+ "")))
					forma.setTarifasMap("simodifica_" + i, "S");
				else
					forma.setTarifasMap("simodifica_" + i, "N");
			} else
				forma.setTarifasMap("simodifica_" + i, "S");
		}

		String servicio = forma.getCodigoServicio() + "";
		//Servicios.obtenerCodigoTarifarioServicioConDesc(forma.getCodigoServicio
		// (), ConstantesBD.codigoTarifarioISS);
		servicio += UtilidadTexto.isEmpty(servicio) ? "" : " - "
				+ Servicios.obtenerNombreServicio(con, forma
						.getCodigoServicio(), Utilidades.convertirAEntero(ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(usuario.getCodigoInstitucionInt())));
		forma.setNombreServicio(servicio);
		InfoDatosInt espe = UtilidadesFacturacion.obtenerEspecialidadServicio(
				con, forma.getCodigoServicio());
		forma
				.setNombreEspecialidad(espe.getCodigo() + " - "
						+ espe.getNombre());

		// ******************Fin***********************//
		// logger.info("====>Valor Tarifa ISS 1: "+forma.getValorTarifa());
		//logger.info("====>Valor Tarifa ISS 2: "+forma.getValorTarifaString());
		this.closeConnection(con);
		return mapping.findForward("paginaPrincipal");
	}

	/**
	 * Metodo para prepara el JSP para nueva tarifa por esquema tarifario y
	 * servicio
	 * 
	 * @param con
	 * @param forma
	 * @param mapping
	 * @param mundo
	 * @param usuario
	 * @return
	 */
	private ActionForward accionNuevoTarifaISS(Connection con,
			TarifaISSForm forma, TarifaISS mundo, ActionMapping mapping,
			UsuarioBasico usuario) {
		forma.setModifInser("Nuevo");
		forma.setValorTarifa(0);
		forma.setValorTarifaString(0 + "");
		forma.setPorcentajeIva(0);
		forma.setUnidades(0);
		forma.setCodigoTipoLiquidacion(-1);
		// forma.setModificar(false);
		forma.setLiquidarAsocios(ConstantesBD.acronimoNo);
		forma.setFechaVigencia("");
		this.closeConnection(con);
		return mapping.findForward("paginaPrincipal");
	}

	/**
	 * Metodo para preparar la modificacion de una tarifa
	 * 
	 * @param con
	 * @param forma
	 * @param mapping
	 * @param mundo
	 * @param usuario
	 * @return
	 */
	private ActionForward accionModificaTarifaISS(Connection con,
			TarifaISSForm forma, TarifaISS mundo, ActionMapping mapping,
			UsuarioBasico usuario) {
		forma.setModifInser("Modifica");
		forma.setCodigoTipoLiquidacion(Utilidades.convertirAEntero(forma
				.getTarifasMap("codigotipoliquidacion_" + forma.getCodigoT())
				+ ""));
		forma.setValorTarifaString(forma.getTarifasMap("valortarifa_"
				+ forma.getCodigoT())
				+ "");
		forma.setLiquidarAsocios(forma.getTarifasMap("liqasocios_"
				+ forma.getCodigoT())
				+ "");
		forma.setFechaVigencia(UtilidadFecha.conversionFormatoFechaAAp(forma
				.getTarifasMap("fechavigencia_" + forma.getCodigoT())
				+ ""));
		this.closeConnection(con);
		return mapping.findForward("paginaPrincipal");
	}

	/**
	 * Metodo para Insertar una Nueva Tarifa ISS
	 * 
	 * @param con
	 * @param forma
	 * @param mapping
	 * @param mundo
	 * @param usuario
	 * @return
	 */
	private ActionForward accionInsertarTarifaISS(Connection con,
			TarifaISSForm forma, TarifaISS mundo, ActionMapping mapping,
			UsuarioBasico usuario) {
		llenarMundoISS(forma, mundo, usuario);
		if (mundo.insertar(con).isTrue()) {

		}
		forma.setTarifasMap(mundo.consultaTodasTarifas(con, forma
				.getCodigoEsquemaTarifario(), forma.getCodigoServicio() + "", ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(usuario.getCodigoInstitucionInt())));
		for (int i = 0; i < Utilidades.convertirAEntero(forma
				.getTarifasMap("numRegistros")
				+ ""); i++) {
			if (forma.getTarifasMap("fechavigencia_" + i).toString() != null
					&& !forma.getTarifasMap("fechavigencia_" + i).toString()
							.equals("")) {
				if (UtilidadFecha.esFechaMenorIgualQueOtraReferencia(
						UtilidadFecha.getFechaActual(), UtilidadFecha
								.conversionFormatoFechaAAp(forma
										.getTarifasMap("fechavigencia_" + i)
										+ "")))
					forma.setTarifasMap("simodifica_" + i, "S");
				else
					forma.setTarifasMap("simodifica_" + i, "N");
			} else
				forma.setTarifasMap("simodifica_" + i, "S");
		}
		this.closeConnection(con);
		return mapping.findForward("paginaPrincipal");
	}

	/**
	 * Metodo para Insertar una Nueva Tarifa ISS
	 * 
	 * @param con
	 * @param forma
	 * @param mapping
	 * @param mundo
	 * @param usuario
	 * @return
	 */
	private ActionForward accionGuardarModTarifaISS(Connection con,
			TarifaISSForm forma, TarifaISS mundo, ActionMapping mapping,
			UsuarioBasico usuario) {
		forma.setCodigo(Utilidades.convertirAEntero(forma
				.getTarifasMap("codigo_" + forma.getCodigoT())
				+ ""));
		llenarMundoISS(forma, mundo, usuario);
		if (mundo.modificar(con).isTrue()) {

		}
		forma.setTarifasMap(mundo.consultaTodasTarifas(con, forma
				.getCodigoEsquemaTarifario(), forma.getCodigoServicio() + "", ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(usuario.getCodigoInstitucionInt())));
		for (int i = 0; i < Utilidades.convertirAEntero(forma
				.getTarifasMap("numRegistros")
				+ ""); i++) {
			if (forma.getTarifasMap("fechavigencia_" + i).toString() != null
					&& !forma.getTarifasMap("fechavigencia_" + i).toString()
							.equals("")) {
				if (UtilidadFecha.esFechaMenorIgualQueOtraReferencia(
						UtilidadFecha.getFechaActual(), UtilidadFecha
								.conversionFormatoFechaAAp(forma
										.getTarifasMap("fechavigencia_" + i)
										+ "")))
					forma.setTarifasMap("simodifica_" + i, "S");
				else
					forma.setTarifasMap("simodifica_" + i, "N");
			} else
				forma.setTarifasMap("simodifica_" + i, "S");
		}
		this.closeConnection(con);
		return mapping.findForward("paginaPrincipal");
	}

	/**
	 * Metodo para validar las fechas de vigencia ISS
	 * 
	 * @param forma
	 * @return
	 */
	public ActionErrors validarFechaVigenciaISS(TarifaISSForm forma) {
		ActionErrors errors = new ActionErrors();
		String fecha = UtilidadFecha.getFechaActual();
		int cont = 0;
		int aux = 0;

		/*for (int z = 0; z < Utilidades.convertirAEntero(forma.getTarifasMap("numRegistros")+""); z++) {
			if (!(forma.getTarifasMap("codigo_" + z) + "").equals((forma.getTarifasMap("codigo_" + forma.getCodigoT()) + ""))
					&& (forma.getTarifasMap("fechavigencia_" + z) + "").equals(""))
				cont = 1;
			if (!forma.getFechaVigencia().equals("")) {
				if (!(forma.getTarifasMap("codigo_" + z) + "").equals((forma.getTarifasMap("codigo_" + forma.getCodigoT()) + ""))
						&& UtilidadFecha.conversionFormatoFechaABD(forma.getFechaVigencia()).equals(forma.getTarifasMap("fechavigencia_" + z) + ""))
					aux = 1;
			}
		}*/

		if (forma.getEstado().equals("guardarNuevoISS")) {
			for (int z = 0; z < Utilidades.convertirAEntero(forma.getTarifasMap("numRegistros")+""); z++) {
				if((forma.getTarifasMap("fechavigencia_" + z) + "").equals(""))
					cont=1;
				if (!forma.getFechaVigencia().equals("") 
						&& UtilidadFecha.conversionFormatoFechaABD(forma.getFechaVigencia()).equals(forma.getTarifasMap("fechavigencia_" + z) + ""))
					aux=1;
			}

			if (cont == 1) {
				if (forma.getFechaVigencia().equals(""))
					errors.add("descripcion", new ActionMessage(
							"errors.required", "La Fecha de Vigencia"));
				else {
					/*
					 * if
					 * (!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(fecha
					 * ,forma.getFechaVigencia())) errors.add("descripcion",new
					 * ActionMessage
					 * ("errors.fechaAnteriorAOtraDeReferencia",forma
					 * .getFechaVigencia(),fecha+" actual"));
					 */
				}
			} else {
				/*
				 * if(!forma.getFechaVigencia().equals("")) { if
				 * (!UtilidadFecha.
				 * esFechaMenorIgualQueOtraReferencia(fecha,forma
				 * .getFechaVigencia())) errors.add("descripcion",new
				 * ActionMessage
				 * ("errors.fechaAnteriorAOtraDeReferencia",forma.getFechaVigencia
				 * (),fecha+" actual")); }
				 */
			}
			if (!forma.getFechaVigencia().equals("") && aux == 1)
				errors
						.add(
								"descripcion",
								new ActionMessage("prompt.generico",
										"La Fecha Vigencia que digito ya existe, por favor cambiela."));
		}

		if (forma.getEstado().equals("guardarModificacionISS")) {
			cont=0;
			aux=0;
			for (int z = 0; z < Utilidades.convertirAEntero(forma.getTarifasMap("numRegistros")+""); z++) {
				if(!(forma.getTarifasMap("codigo_" + z) + "").equals((forma.getTarifasMap("codigo_" + forma.getCodigoT()) + "")) 
						&& (forma.getTarifasMap("fechavigencia_" + z) + "").equals(""))
					cont=1;
				if (!(forma.getTarifasMap("codigo_" + z) + "").equals((forma.getTarifasMap("codigo_" + forma.getCodigoT()) + "")) 
						&& !forma.getFechaVigencia().equals("") 
							&& UtilidadFecha.conversionFormatoFechaABD(forma.getFechaVigencia()).equals(forma.getTarifasMap("fechavigencia_" + z) + ""))
					aux=1;
			}
			if (cont == 1) {
				if (forma.getFechaVigencia().equals(""))
					errors.add("descripcion", new ActionMessage(
							"errors.required", "La Fecha de Vigencia"));
				else {
					/*
					 * if
					 * (!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(fecha
					 * ,forma.getFechaVigencia())) errors.add("descripcion",new
					 * ActionMessage
					 * ("errors.fechaAnteriorAOtraDeReferencia",forma
					 * .getFechaVigencia(),fecha+" actual"));
					 */
				}
			} else {
				if (!forma.getFechaVigencia().equals("")) {
					/*
					 * if
					 * (!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(fecha
					 * ,forma.getFechaVigencia())) errors.add("descripcion",new
					 * ActionMessage
					 * ("errors.fechaAnteriorAOtraDeReferencia",forma
					 * .getFechaVigencia(),fecha+" actual"));
					 */
				}
			}
			if (!forma.getFechaVigencia().equals("") && aux == 1)
				errors
						.add(
								"descripcion",
								new ActionMessage("prompt.generico",
										"La Fecha Vigencia que digito ya existe, por favor cambiela."));
		}

		if (forma.getCodigoTipoLiquidacion() != ConstantesBD.codigoTipoLiquidacionSoatUvr) {
			if (forma.getAcronimoMetodoAjuste().equals(
					ConstantesBD.metodoAjusteUnidad)) {
				forma.setValorTarifa(Math.round(forma.getValorTarifa()));
			} else if (forma.getAcronimoMetodoAjuste().equals(
					ConstantesBD.metodoAjusteDecena)) {
				forma.setValorTarifa(Math.round(forma.getValorTarifa()));
				forma.setValorTarifa(UtilidadCadena
						.redondearALaDecenaLong((long) forma.getValorTarifa()));
			} else if (forma.getAcronimoMetodoAjuste().equals(
					ConstantesBD.metodoAjusteCentena)) {
				forma.setValorTarifa(Math.round(forma.getValorTarifa()));
				forma
						.setValorTarifa(UtilidadCadena
								.redondearALaCentenaLong((long) forma
										.getValorTarifa()));
			}
		}

		if (forma.getCodigoTipoLiquidacion() <= ConstantesBD.codigoTipoLiquidacionInvalido)
			errors.add("tipo de liquidación requerido", new ActionMessage(
					"errors.required", "Valor (tipo liquidación) "));

		if (Utilidades.convertirADouble(forma.getValorTarifaString()) <= 0.0
				&& forma.getCodigoTipoLiquidacion() == ConstantesBD.codigoTipoLiquidacionSoatUvr)
			errors.add("valor negativo", new ActionMessage(
					"errors.floatMayorQue", "El valor UVR", "0"));

		if (Utilidades.convertirADouble(forma.getValorTarifaString()) < 0.0
				&& forma.getCodigoTipoLiquidacion() != ConstantesBD.codigoTipoLiquidacionSoatUvr
				&& !UtilidadTexto.getBoolean(forma.getLiquidarAsocios()))
			errors.add("valor negativo", new ActionMessage(
					"errors.floatMayorOIgualQue", "La tarifa por valor", "0"));

		return errors;
	}

	/**
	 * Metodo para validar las fechas de vigencia SOAT
	 * 
	 * @param forma
	 * @return
	 */
	public ActionErrors validarFechaVigenciaSOAT(TarifaSOATForm forma) {
		ActionErrors errors = new ActionErrors();
		int cont = 0;
		int aux = 0;
		/*for (int z = 0; z < Utilidades.convertirAEntero(forma.getTarifasMap("numRegistros")+""); z++) {
			if (!(forma.getTarifasMap("codigo_" + z) + "").equals((forma.getTarifasMap("codigo_" + forma.getCodigoT()) + ""))
					&& (forma.getTarifasMap("fechavigencia_" + z) + "").equals(""))
				cont = 1;
			if (!forma.getFechaVigencia().equals("")) {
				if (!(forma.getTarifasMap("codigo_" + z) + "").equals((forma.getTarifasMap("codigo_" + forma.getCodigoT()) + ""))
						&& UtilidadFecha.conversionFormatoFechaABD(forma.getFechaVigencia()).equals(forma.getTarifasMap("fechavigencia_" + z) + ""))
					aux = 1;
			}
		}*/
		if (forma.getEstado().equals("guardarNuevoSOAT")) {
			for (int z = 0; z < Utilidades.convertirAEntero(forma.getTarifasMap("numRegistros")+""); z++) {
				if((forma.getTarifasMap("fechavigencia_" + z) + "").equals(""))
					cont=1;
				if (!forma.getFechaVigencia().equals("") 
						&& UtilidadFecha.conversionFormatoFechaABD(forma.getFechaVigencia()).equals(forma.getTarifasMap("fechavigencia_" + z) + ""))
					aux=1;
			}
			if (cont == 1) {
				if (forma.getFechaVigencia().equals(""))
					errors.add("descripcion", new ActionMessage(
							"errors.required", "La Fecha de Vigencia"));
				else {
					/*
					 * if
					 * (!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(fecha
					 * ,forma.getFechaVigencia())) errors.add("descripcion",new
					 * ActionMessage
					 * ("errors.fechaAnteriorAOtraDeReferencia",forma
					 * .getFechaVigencia(),fecha+" actual"));
					 */
				}
			} else {
				/*
				 * if(!forma.getFechaVigencia().equals("")) { if
				 * (!UtilidadFecha.
				 * esFechaMenorIgualQueOtraReferencia(fecha,forma
				 * .getFechaVigencia())) errors.add("descripcion",new
				 * ActionMessage
				 * ("errors.fechaAnteriorAOtraDeReferencia",forma.getFechaVigencia
				 * (),fecha+" actual")); }
				 */
			}
			if (!forma.getFechaVigencia().equals("") && aux == 1)
				errors
						.add(
								"descripcion",
								new ActionMessage("prompt.generico",
										"La Fecha Vigencia que digito ya existe, por favor cambiela."));

			if (forma.getAcronimoMetodoAjuste().equals(
					ConstantesBD.metodoAjusteUnidad)) {
				forma.setValorTarifa(Math.round(forma.getValorTarifa()));
			} else if (forma.getAcronimoMetodoAjuste().equals(
					ConstantesBD.metodoAjusteDecena)) {
				forma.setValorTarifa(UtilidadCadena
						.redondearALaDecenaLong((long) forma.getValorTarifa()));
			} else if (forma.getAcronimoMetodoAjuste().equals(
					ConstantesBD.metodoAjusteCentena)) {
				forma
						.setValorTarifa(UtilidadCadena
								.redondearALaCentenaLong((long) forma
										.getValorTarifa()));
			}

			if (forma.getValorTarifa() < 0.0
					&& forma.getCodigoTipoLiquidacion() != ConstantesBD.codigoTipoLiquidacionSoatGrupo
					&& !UtilidadTexto.getBoolean(forma.getLiquidarAsocios()))
				errors.add("valor negativo", new ActionMessage(
						"errors.floatMayorOIgualQue", "La tarifa por valor", "0"));

			if (Utilidades.convertirADouble(forma.getGrupoString(),true) <= 0.0
					&& forma.getCodigoTipoLiquidacion() == ConstantesBD.codigoTipoLiquidacionSoatGrupo)
				errors.add("valor negativo", new ActionMessage(
						"errors.floatMayorQue", "El Grupo", "0"));

			if (forma.getCodigoTipoLiquidacion() <= 0) {
				errors.add("tipo liquidación requerido", new ActionMessage(
						"errors.required", "El tipo de liquidación"));
			}

		}
		if (forma.getEstado().equals("guardarModificacionSOAT")) {
			cont=0;
			aux=0;
			for (int z = 0; z < Utilidades.convertirAEntero(forma.getTarifasMap("numRegistros")+""); z++) {
				if(!(forma.getTarifasMap("codigo_" + z) + "").equals((forma.getTarifasMap("codigo_" + forma.getCodigoT()) + "")) 
						&& (forma.getTarifasMap("fechavigencia_" + z) + "").equals(""))
					cont=1;
				if (!(forma.getTarifasMap("codigo_" + z) + "").equals((forma.getTarifasMap("codigo_" + forma.getCodigoT()) + "")) 
						&& !forma.getFechaVigencia().equals("") 
							&& UtilidadFecha.conversionFormatoFechaABD(forma.getFechaVigencia()).equals(forma.getTarifasMap("fechavigencia_" + z) + ""))
					aux=1;
			}
			if (cont == 1) {
				if (forma.getFechaVigencia().equals(""))
					errors.add("descripcion", new ActionMessage(
							"errors.required", "La Fecha de Vigencia"));
				else {
					/*
					 * if
					 * (!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(fecha
					 * ,forma.getFechaVigencia())) errors.add("descripcion",new
					 * ActionMessage
					 * ("errors.fechaAnteriorAOtraDeReferencia",forma
					 * .getFechaVigencia(),fecha+" actual"));
					 */
				}
			} else {
				if (!forma.getFechaVigencia().equals("")) {
					/*
					 * if
					 * (!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(fecha
					 * ,forma.getFechaVigencia())) errors.add("descripcion",new
					 * ActionMessage
					 * ("errors.fechaAnteriorAOtraDeReferencia",forma
					 * .getFechaVigencia(),fecha+" actual"));
					 */
				}
			}
			if (!forma.getFechaVigencia().equals("") && aux == 1)
				errors
						.add(
								"descripcion",
								new ActionMessage("prompt.generico",
										"La Fecha Vigencia que digito ya existe, por favor cambiela."));
			if (forma.getAcronimoMetodoAjuste().equals(
					ConstantesBD.metodoAjusteUnidad)) {
				forma.setValorTarifa(Math.round(forma.getValorTarifa()));
			} else if (forma.getAcronimoMetodoAjuste().equals(
					ConstantesBD.metodoAjusteDecena)) {
				forma.setValorTarifa(UtilidadCadena
						.redondearALaDecenaLong((long) forma.getValorTarifa()));
			} else if (forma.getAcronimoMetodoAjuste().equals(
					ConstantesBD.metodoAjusteCentena)) {
				forma
						.setValorTarifa(UtilidadCadena
								.redondearALaCentenaLong((long) forma
										.getValorTarifa()));
			}

			if (forma.getValorTarifa() < 0.0
					&& forma.getCodigoTipoLiquidacion() != ConstantesBD.codigoTipoLiquidacionSoatGrupo
					&& !UtilidadTexto.getBoolean(forma.getLiquidarAsocios()))
				errors.add("valor negativo", new ActionMessage(
						"errors.floatMayorOIgualQue", "La tarifa por valor", "0"));

			if (Utilidades.convertirADouble(forma.getGrupoString(),true) <= 0.0
					&& forma.getCodigoTipoLiquidacion() == ConstantesBD.codigoTipoLiquidacionSoatGrupo)
				errors.add("valor negativo", new ActionMessage(
						"errors.floatMayorQue", "El Grupo", "0"));

			if (forma.getCodigoTipoLiquidacion() <= 0) {
				errors.add("tipo liquidación requerido", new ActionMessage(
						"errors.required", "El tipo de liquidación"));
			}
		}

		/*
		 * if(forma.getCodigoTipoLiquidacion()!=ConstantesBD.codigoTipoLiquidacionSoatUvr
		 * ) {
		 * if(forma.getAcronimoMetodoAjuste().equals(ConstantesBD.metodoAjusteUnidad
		 * )) { forma.setValorTarifa(Math.round(forma.getValorTarifa())); } else
		 * if
		 * (forma.getAcronimoMetodoAjuste().equals(ConstantesBD.metodoAjusteDecena
		 * )) { forma.setValorTarifa(Math.round(forma.getValorTarifa()));
		 * forma.setValorTarifa
		 * (UtilidadCadena.redondearALaDecenaLong((long)forma
		 * .getValorTarifa())); } else
		 * if(forma.getAcronimoMetodoAjuste().equals(
		 * ConstantesBD.metodoAjusteCentena)) {
		 * forma.setValorTarifa(Math.round(forma.getValorTarifa()));
		 * forma.setValorTarifa
		 * (UtilidadCadena.redondearALaCentenaLong((long)forma
		 * .getValorTarifa())); } }
		 * 
		 * if(forma.getCodigoTipoLiquidacion()<=ConstantesBD.
		 * codigoTipoLiquidacionInvalido)
		 * errors.add("tipo de liquidación requerido", new
		 * ActionMessage("errors.required", "Valor (tipo liquidación) "));
		 * 
		 * if(Utilidades.convertirADouble(forma.getValorTarifaString()) <= 0.0
		 * && forma.getCodigoTipoLiquidacion() ==
		 * ConstantesBD.codigoTipoLiquidacionSoatUvr)
		 * errors.add("valor negativo", new
		 * ActionMessage("errors.floatMayorQue", "El valor UVR", "0"));
		 * 
		 * if( Utilidades.convertirADouble(forma.getValorTarifaString()) <= 0.0
		 * && forma.getCodigoTipoLiquidacion() !=
		 * ConstantesBD.codigoTipoLiquidacionSoatUvr &&
		 * !UtilidadTexto.getBoolean(forma.getLiquidarAsocios()))
		 * errors.add("valor negativo", new
		 * ActionMessage("errors.floatMayorQue", "La tarifa por valor", "0"));
		 */
		return errors;
	}

	/**
	 * Metodo para prepara el JSP para nueva tarifa SOAT por esquema tarifario y
	 * servicio
	 * 
	 * @param con
	 * @param forma
	 * @param mapping
	 * @param mundo
	 * @param usuario
	 * @return
	 */
	private ActionForward accionNuevoTarifaSOAT(Connection con,
			TarifaSOATForm forma, TarifaSOAT mundo, ActionMapping mapping,
			UsuarioBasico usuario) {
		forma.setModifInser("Nuevo");
		forma.setValorTarifa(0);
		forma.setValorTarifaString(0 + "");
		forma.setPorcentajeIva(0);
		// forma.setUnidades(0);
		forma.setCodigoTipoLiquidacion(-1);
		// forma.setModificar(false);
		forma.setLiquidarAsocios(ConstantesBD.acronimoNo);
		forma.setFechaVigencia("");
		this.closeConnection(con);
		return mapping.findForward("paginaPrincipal");
	}

	/**
	 * Metodo para insertar una nueva tarifa SOAT por esquema tarifario servicio
	 * 
	 * @param con
	 * @param forma
	 * @param mapping
	 * @param mundo
	 * @param usuario
	 * @return
	 */
	private ActionForward accionInsertarTarifaSOAT(Connection con,
			TarifaSOATForm forma, TarifaSOAT mundo, ActionMapping mapping,
			UsuarioBasico usuario) {
		llenarMundoSOAT(forma, mundo, usuario);
		try {
			if (mundo.insertar(con).isTrue()) {

			}
		} catch (Exception e) {
			logger.info("\n\nERROR. INSERTANDO TARIFA SOAT>>>>>>" + e);
		}
		forma.setTarifasMap(mundo.consultarTodasTarifas(con, forma
				.getCodigoEsquemaTarifario(), forma.getCodigoServicio() + ""));
		for (int i = 0; i < Utilidades.convertirAEntero(forma
				.getTarifasMap("numRegistros")
				+ ""); i++) {
			if (forma.getTarifasMap("fechavigencia_" + i).toString() != null
					&& !forma.getTarifasMap("fechavigencia_" + i).toString()
							.equals("")) {
				if (UtilidadFecha.esFechaMenorIgualQueOtraReferencia(
						UtilidadFecha.getFechaActual(), UtilidadFecha
								.conversionFormatoFechaAAp(forma
										.getTarifasMap("fechavigencia_" + i)
										+ "")))
					forma.setTarifasMap("simodifica_" + i, "S");
				else
					forma.setTarifasMap("simodifica_" + i, "N");
			} else
				forma.setTarifasMap("simodifica_" + i, "S");
		}
		this.closeConnection(con);
		return mapping.findForward("paginaPrincipal");
	}

	/**
	 * Metodo para prepara la modificacion de una Tarifa SOAT
	 * 
	 * @param con
	 * @param forma
	 * @param mapping
	 * @param mundo
	 * @param usuario
	 * @return
	 */
	private ActionForward accionModificaTarifaSOAT(Connection con,
			TarifaSOATForm forma, TarifaSOAT mundo, ActionMapping mapping,
			UsuarioBasico usuario) {
		forma.setModifInser("Modifica");
		forma.setCodigoTipoLiquidacion(Utilidades.convertirAEntero(forma
				.getTarifasMap("codigotipoliquidacion_" + forma.getCodigoT())
				+ ""));
		forma.setValorTarifaString(forma.getTarifasMap("valortarifa_"
				+ forma.getCodigoT())
				+ "");
		forma.setValorTarifa(Utilidades.convertirADouble(forma
				.getTarifasMap("valortarifa_" + forma.getCodigoT())
				+ ""));
		
		
		logger.info("unidades: "+forma
				.getTarifasMap("unidades_" + forma.getCodigoT()));
		forma.setGrupoString(forma
				.getTarifasMap("unidades_" + forma.getCodigoT())
				+ "");
		forma.setLiquidarAsocios(forma.getTarifasMap("liqasocios_"
				+ forma.getCodigoT())
				+ "");
		forma.setFechaVigencia(UtilidadFecha.conversionFormatoFechaAAp(forma
				.getTarifasMap("fechavigencia_" + forma.getCodigoT())
				+ ""));
		
		logger.info("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
		logger.info("Valor Base: " + forma.getValorBase());
		logger.info("Valor Tarifa: " + forma.getValorTarifa());
		logger.info("Grupo: " + forma.getGrupoString());
		logger.info("Unidad: " + forma.getTarifasMap("unidades_" + forma.getCodigoT()) );
		
		this.closeConnection(con);
		return mapping.findForward("paginaPrincipal");
	}

	/**
	 * Metodo para prepara la modificacion de una Tarifa SOAT
	 * 
	 * @param con
	 * @param forma
	 * @param mapping
	 * @param mundo
	 * @param usuario
	 * @return
	 */
	private ActionForward accionGuardarModTarifaSOAT(Connection con,
			TarifaSOATForm forma, TarifaSOAT mundo, ActionMapping mapping,
			UsuarioBasico usuario) {
		logger.info("\n\nENTROOOO >>>>>>777777777");
		forma.setCodigo(Utilidades.convertirAEntero(forma
				.getTarifasMap("codigo_" + forma.getCodigoT())
				+ ""));
		llenarMundoSOAT(forma, mundo, usuario);
		logger.info("\n\nENTROOOO >>>>>>888888888888888");
		if (mundo.modificar(con).isTrue()) {

		}
		forma.setTarifasMap(mundo.consultarTodasTarifas(con, forma
				.getCodigoEsquemaTarifario(), forma.getCodigoServicio() + ""));
		for (int i = 0; i < Utilidades.convertirAEntero(forma
				.getTarifasMap("numRegistros")
				+ ""); i++) {
			if (forma.getTarifasMap("fechavigencia_" + i).toString() != null
					&& !forma.getTarifasMap("fechavigencia_" + i).toString()
							.equals("")) {
				if (UtilidadFecha.esFechaMenorIgualQueOtraReferencia(
						UtilidadFecha.getFechaActual(), UtilidadFecha
								.conversionFormatoFechaAAp(forma
										.getTarifasMap("fechavigencia_" + i)
										+ "")))
					forma.setTarifasMap("simodifica_" + i, "S");
				else
					forma.setTarifasMap("simodifica_" + i, "N");
			} else
				forma.setTarifasMap("simodifica_" + i, "S");
		}
		logger.info("\n\nENTROOOO >>>>>>9999999999");
		this.closeConnection(con);
		return mapping.findForward("paginaPrincipal");
	}

	/**
	 * Método que controla la página de ingreso y modificación
	 * 
	 * @param con
	 * @param forma
	 * @param mapping
	 * @param mundo
	 * @param institucion
	 * @return Página de Ingreso Modificación
	 */
	private ActionForward accionIngresarModificarSOAT(Connection con,
			TarifaSOATForm forma, ActionMapping mapping, TarifaSOAT mundo,
			UsuarioBasico usuario) {
		
		// **********Modificacion Tarea 70012**********//
		forma.setTarifasMap(mundo.consultarTodasTarifas(con, forma.getCodigoEsquemaTarifario(), forma.getCodigoServicio() + ""));
		
		Utilidades.imprimirMapa(forma.getTarifasMap());
		
		for (int i = 0; i < Utilidades.convertirAEntero(forma.getTarifasMap("numRegistros")+ ""); i++) 
		{
			if (forma.getTarifasMap("fechavigencia_" + i).toString() != null
					&& !forma.getTarifasMap("fechavigencia_" + i).toString()
							.equals("")) 
			{
				if (UtilidadFecha.esFechaMenorIgualQueOtraReferencia(
						UtilidadFecha.getFechaActual(), UtilidadFecha
								.conversionFormatoFechaAAp(forma
										.getTarifasMap("fechavigencia_" + i)
										+ "")))
				{
					forma.setTarifasMap("simodifica_" + i, "S");
				}
				else
					forma.setTarifasMap("simodifica_" + i, "N");
			}
			else
				forma.setTarifasMap("simodifica_" + i, "S");
		}

		String servicio = forma.getCodigoServicio() + "";
		//Servicios.obtenerCodigoTarifarioServicioConDesc(forma.getCodigoServicio
		// (), ConstantesBD.codigoTarifarioSoat);
		servicio += UtilidadTexto.isEmpty(servicio) ? "" : " - "
				+ Servicios.obtenerNombreServicio(con, forma
						.getCodigoServicio(), Utilidades.convertirAEntero(ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(usuario.getCodigoInstitucionInt())));
		forma.setNombreServicio(servicio);
		InfoDatosInt espe = UtilidadesFacturacion.obtenerEspecialidadServicio(
				con, forma.getCodigoServicio());
		forma
				.setNombreEspecialidad(espe.getCodigo() + " - "
						+ espe.getNombre());

		// ******************Fin***********************//
		logger.info("====>Valor Tarifa SOAT 1: " + forma.getValorTarifa());
		logger
				.info("====>Valor Tarifa SOAT 2: "
						+ forma.getValorTarifaString());
		this.closeConnection(con);
		return mapping.findForward("paginaPrincipal");
	}

	/**
	 * Método para eliminar un registro de la tarifa ISS
	 * 
	 * @param con
	 *            Conexión con la BD
	 * @param forma
	 * @param mundo
	 * @param mapping
	 * @return Pagina Principal
	 */
	private ActionForward accionEliminarISS(Connection con,
			TarifaISSForm forma, TarifaISS mundo, ActionMapping mapping,
			UsuarioBasico usuario) {
		logger.info("codigoServicio=> " + forma.getCodigoServicio());
		logger.info("eliminado=> " + forma.getEliminado());
		llenarMundoISS(forma, mundo, usuario);
		mundo.cargar(con, forma.getEliminado(), forma
				.getCodigoEsquemaTarifario(), "");

		mundo.eliminarTarifa(con); // alejo esta linea no estaba

		/*
		 * lo documente - alejo //Se verifica si existe la tarifa registrada en
		 * la base de datos if(mundo.getCodigo()>0) {
		 * logger.info("eliminando registro"); mundo.cargar(con,
		 * forma.getEliminado(), forma.getCodigoEsquemaTarifario());
		 * mundo.eliminarTarifa(con);
		 * 
		 * String[] codSer=forma.getTarifasISSAnteriores().split(","); String
		 * cadena=""; int pos=0; for(int i=0;i<codSer.length;i++) {
		 * if(forma.getEliminado()!=Utilidades.convertirAEntero(codSer[i])) {
		 * if(pos > 0) cadena=cadena+","; cadena=cadena+codSer[i]; pos++; } }
		 * forma.setTarifasISSAnteriores(cadena);
		 * 
		 * int ultimoServicio=forma.getCodigoServicio();
		 * //forma.setCodigoServicio(forma.getEliminado()); boolean
		 * mod=forma.isModificado(); forma.resetEliminacion();
		 * forma.setCodigoServicio(ultimoServicio); forma.setModificado(mod);
		 * logger.info("------>"+forma.getTarifasISSAnteriores()); } else
		 * forma.resetEliminacion();
		 */
		logger.info("===> Eliminar ");
		forma.setCodigoServicio(forma.getEliminado());
		forma.setValorTarifa(0);
		forma.setValorTarifaString(0 + "");
		forma.setValorBase(0);
		// forma.setGrupo(0);
		forma.setPorcentajeIva(0);
		forma.setCodigoTipoLiquidacion(-1);
		forma.setLiquidarAsocios(ConstantesBD.acronimoNo);
		forma.setModificar(false); // true

		// this.eliminado=0;
		// this.unidades = 0;
		// this.nombreTipoLiquidacion = "";

		closeConnection(con);
		return mapping.findForward("paginaPrincipal");
	}

	/**
	 * Método para eliminar un registro de la tarifa SOAT
	 * 
	 * @param con
	 *            Conexión con la BD
	 * @param forma
	 * @param mundo
	 * @param mapping
	 * @return Pagina Principal
	 */
	private ActionForward accionEliminarSOAT(Connection con,
			TarifaSOATForm forma, TarifaSOAT mundo, ActionMapping mapping,
			UsuarioBasico usuario) {
		llenarMundoSOAT(forma, mundo, usuario);
		mundo.cargar(con, forma.getEliminado(), forma
				.getCodigoEsquemaTarifario(), "");
		mundo.eliminarTarifa(con);

		logger.info("codigoServicio=> " + forma.getCodigoServicio());
		logger.info("eliminado=> " + forma.getEliminado());

		/*
		 * este era el codigo que habia antes..
		 * if(forma.getCodigoServicio()==forma.getEliminado()) {
		 * forma.setCodigoServicio(forma.getEliminado());
		 * forma.resetEliminacion(); } else { String[]
		 * codSer=forma.getTarifasSOATAnteriores().split(","); String cadena="";
		 * int pos=0; for(int i=0;i<codSer.length;i++) {
		 * if(forma.getEliminado()!=Utilidades.convertirAEntero(codSer[i])) {
		 * if(pos>0) cadena=cadena+","; cadena=cadena+codSer[i]; pos++; } }
		 * forma.setTarifasSOATAnteriores(cadena);
		 * 
		 * int ultimoServicio=forma.getCodigoServicio();
		 * //forma.setCodigoServicio(forma.getEliminado()); boolean
		 * mod=forma.isModificado(); forma.resetEliminacion();
		 * forma.setCodigoServicio(ultimoServicio); forma.setModificado(mod); }
		 */

		logger.info("===> Eliminar ");
		forma.setCodigoServicio(forma.getEliminado());
		forma.setValorTarifa(0);
		forma.setValorTarifaString(0 + "");
		forma.setValorBase(0);
		forma.setGrupoString("0");
		forma.setPorcentajeIva(0);
		forma.setCodigoTipoLiquidacion(-1);
		forma.setLiquidarAsocios(ConstantesBD.acronimoNo);
		forma.setModificar(false);

		closeConnection(con);
		return mapping.findForward("paginaPrincipal");

	}

	/**
	 * Método que pasa los atributos del mundo al form
	 * 
	 * @param mundo
	 * @param forma
	 */
	private void llenarFormaISS(TarifaISS mundo, TarifaISSForm forma) {
		forma.setCodigo(mundo.getCodigo());
		forma.setCodigoEsquemaTarifario(mundo.getCodigoEsquemaTarifario());
		forma.setCodigoServicio(mundo.getCodigoServicio());
		forma.setCodigoTipoLiquidacion(mundo.getCodigoTipoLiquidacion());
		forma.setValorTarifa(mundo.getValorTarifa());
		forma.setValorTarifaString(mundo.getValorTarifaString());
		forma.setPorcentajeIva(mundo.getPorcentajeIva());
		forma.setUnidades(mundo.getUnidades());
		forma.setLiquidarAsocios(mundo.getLiquidarAsocios());
		forma.setFechaVigencia(mundo.getFechaVigencia());
	}

	/**
	 * Método que pasa los atributos del form al mundo
	 * 
	 * @param forma
	 * @param mundo
	 */
	private void llenarMundoISS(TarifaISSForm forma, TarifaISS mundo,
			UsuarioBasico usuario) {
		mundo.setCodigo(forma.getCodigo());
		mundo.setCodigoServicio(forma.getCodigoServicio());
		mundo.setCodigoEsquemaTarifario(forma.getCodigoEsquemaTarifario());
		mundo.setPorcentajeIva(forma.getPorcentajeIva());
		mundo.setLoginUsuario(usuario.getLoginUsuario());
		mundo.setLiquidarAsocios(forma.getLiquidarAsocios());
		mundo.setFechaVigencia(forma.getFechaVigencia());
		
		mundo.setCodigoTipoLiquidacion(forma.getCodigoTipoLiquidacion());

		if(forma.getCodigoTipoLiquidacion() == ConstantesBD.codigoTipoLiquidacionSoatUvr) {
			logger.info("Uvr : Si");
			mundo.setValorTarifa(forma.getValorTarifa());
			mundo.setValorTarifaString(forma.getValorTarifaString());
			mundo.setUnidades(Utilidades.convertirADouble(forma.getValorTarifaString()));
		}
		else {
			logger.info("Uvr : No");
			mundo.setValorTarifa(forma.getValorTarifa());
			mundo.setValorTarifaString(forma.getValorTarifaString());
			mundo.setUnidades(forma.getUnidades());
		}
	}

	/**
	 * Método que pasa los atributos del mundo al form
	 * 
	 * @param mundo
	 * @param forma
	 */
	private void llenarFormaSOAT(TarifaSOAT mundo, TarifaSOATForm forma) {
		forma.setCodigo(mundo.getCodigo());
		forma.setCodigoServicio(mundo.getCodigoServicio());
		forma.setCodigoEsquemaTarifario(mundo.getCodigoEsquemaTarifario());
		forma.setCodigoTipoLiquidacion(mundo.getCodigoTipoLiquidacion());
		forma.setValorTarifa(mundo.getValorTarifa());
		forma.setValorTarifaString(mundo.getValorTarifaString());
		forma.setPorcentajeIva(mundo.getPorcentajeIva());
		forma.setGrupoString(mundo.getGrupo()+"");
		forma.setLiquidarAsocios(mundo.getLiquidarAsocios());
	}

	/**
	 * Método que pasa los atributos del form al mundo
	 * 
	 * @param forma
	 * @param mundo
	 * @param usuario
	 */
	private void llenarMundoSOAT(TarifaSOATForm forma, TarifaSOAT mundo,
			UsuarioBasico usuario) {
		mundo.setCodigo(forma.getCodigo());
		mundo.setCodigoServicio(forma.getCodigoServicio());
		mundo.setCodigoEsquemaTarifario(forma.getCodigoEsquemaTarifario());
		mundo.setCodigoTipoLiquidacion(forma.getCodigoTipoLiquidacion());
		mundo.setPorcentajeIva(forma.getPorcentajeIva());
		mundo.setValorTarifa(forma.getValorTarifa());
		mundo.setValorTarifaString(forma.getValorTarifaString());
		mundo.setGrupo(Utilidades.convertirADouble(forma.getGrupoString(),true));
		mundo.setLiquidarAsocios(forma.getLiquidarAsocios());
		mundo.setLoginUsuario(usuario.getLoginUsuario());
		mundo.setFechaVigencia(forma.getFechaVigencia());
	}
}