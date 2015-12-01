package com.princetonsa.action.tesoreria;

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
import org.hibernate.HibernateException;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadTexto;
import util.ValoresPorDefecto;

import com.princetonsa.actionform.tesoreria.ConceptoNotasPacientesForm;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.sort.odontologia.SortGenerico;
import com.servinte.axioma.dto.tesoreria.DtoConcNotaPacCuentaCont;
import com.servinte.axioma.dto.tesoreria.DtoConceptoNotasPacientes;
import com.servinte.axioma.mundo.fabrica.TesoreriaFabricaMundo;
import com.servinte.axioma.mundo.interfaz.tesoreria.IConceptoNotaPacCuentaContMundo;
import com.servinte.axioma.mundo.interfaz.tesoreria.IConceptoNotasPacientesMundo;
import com.servinte.axioma.orm.ConcNotaPacCuentaCont;
import com.servinte.axioma.orm.EmpresasInstitucion;
import com.servinte.axioma.orm.Instituciones;
import com.servinte.axioma.persistencia.UtilidadTransaccion;

public class ConceptoNotasPacientesAction extends Action {
	
	/** * Contiene la lista de mensajes correspondiente a esta forma */
	private MessageResources fuenteMensaje = 
		MessageResources.getMessageResources("com.servinte.mensajes.tesoreria.ConceptoNotasPacientesForm");
	
	private IConceptoNotasPacientesMundo conceptoNotasPacientesMundo = 
		TesoreriaFabricaMundo.crearConceptoNotasPacientes();
	
	private IConceptoNotaPacCuentaContMundo conceptoNotaPacCuentaContMundo = 
		TesoreriaFabricaMundo.crearConceptoNotaPacCuentaCont();
	/**
	 * Método encargado de el flujo y control de la funcionalidad
	 * 
	 * @see org.apache.struts.action.Action#execute(ActionMapping, ActionForm, 
	 * HttpServletRequest, HttpServletResponse)
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, 
								HttpServletResponse response )throws Exception {
		
		if(form instanceof ConceptoNotasPacientesForm) {
			ConceptoNotasPacientesForm forma = (ConceptoNotasPacientesForm)form;
			String estado = forma.getEstado(); 
			Log4JManager.info("Estado: ConceptosNotasPacientesAction --> "+estado);
			UsuarioBasico usuario 	= (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
			ActionErrors errores = new ActionErrors();
			if(estado.equals("inicio")) {
				forma.resetForma();
				forma.resetBusqueda();
				if(ValoresPorDefecto.getNaturalezaNotasPacientesManejar(usuario.getCodigoInstitucionInt()) != 
					ConstantesBD.codigoNuncaValido+"" && 
					!UtilidadTexto.isEmpty(ValoresPorDefecto.getNaturalezaNotasPacientesManejar(usuario.getCodigoInstitucionInt()))) {
					return mapping.findForward(this.consultarConceptosNotasPacientes(forma, request, errores, usuario));
				} else {
					errores.add("PacienteNoCargado", 
							new ActionMessage("error.parametrosGenerales.faltaDefinirParametroModulo", 
									"Naturaleza de Notas de Pacientes a Manejar", "Tesorería"));
					saveErrors(request, errores);
					return mapping.findForward("paginaMensajeBloqueo");
				}
			} else if (estado.equals("guardarConceptoNotasPaciente")) {
				forma.resetBusqueda();
				forma.setOperacionExitosa(false);
				Instituciones institucion = new Instituciones();
				institucion.setCodigo(usuario.getCodigoInstitucionInt());
				return mapping.findForward(this.operacionesConceptoNotasPaciente(forma, request, errores, institucion));
			} else if (estado.equals("ordenarConceptosNotasPacientes")) {
				return mapping.findForward(this.accionOrdenar(forma, "conceptosNotasPacientes"));
			} else if (estado.equals("mostrarBusquedaAvanzada")) {
				forma.resetBusqueda();
				return mapping.findForward("busquedaAvanzada");
			} else if (estado.equals("busquedaAvanzada")) {
				forma.resetForma();
				forma.setBusquedaAvanzada(true);
				return mapping.findForward(this.consultarConceptosNotasPacientes(forma, request, errores, usuario));
			} else if (estado.equals("cargarCuentasEmpresaInstitucion")) {
				this.cargarCuentasContablesEmpresaInstitucion(forma, request, errores);
			} else if (estado.equals("modificarCuentasEmpresaInstitucion")) {
				this.modificarCuentasContablesEmpresaInstitucion(forma, request, errores);
			} else if (estado.equals("volverPrincipal")) {
				forma.resetBusqueda();
				return mapping.findForward(this.consultarConceptosNotasPacientes(forma, request, errores, usuario));
			} else {
				errores.add("Error", new ActionMessage("errors.procesoNoExitoso", "Funcionalidad no valida"));
				return mapping.findForward("paginaError");
			}
		}
		return null;
	}	

	
	private String consultarConceptosNotasPacientes(ConceptoNotasPacientesForm forma, 
			HttpServletRequest request, ActionErrors errores, UsuarioBasico usuario) {
		String naturalezaNotas = ValoresPorDefecto.getNaturalezaNotasPacientesManejar(usuario.getCodigoInstitucionInt());
		String paginaCargar = "principal";
		boolean institucionMultiempresa = UtilidadTexto.getBoolean(
				ValoresPorDefecto.getInstitucionMultiempresa(usuario.getCodigoInstitucionInt()));
		ArrayList<DtoConceptoNotasPacientes> listaDtoConceptoNotasPacientes = new ArrayList<DtoConceptoNotasPacientes>();
			
		if(institucionMultiempresa) {
			UtilidadTransaccion.getTransaccion().begin();
			ArrayList<EmpresasInstitucion> empresasInstitucion = 
				conceptoNotasPacientesMundo.obtenerEmpresaInstitucion(usuario.getCodigoInstitucionInt());
			UtilidadTransaccion.getTransaccion().commit();
			forma.setListaEmpresasInstitucion(empresasInstitucion);
		} 
		if (!forma.isBusquedaAvanzada()) {
			if (naturalezaNotas.equals(ConstantesIntegridadDominio.acronimoNaturalezaNotaPacienteCreditoDebito)) {
				listaDtoConceptoNotasPacientes = conceptoNotasPacientesMundo.listarConceptoNotasPacientes();
			} else {
				DtoConceptoNotasPacientes dtoConceptoNotasPacientes = new DtoConceptoNotasPacientes();
				dtoConceptoNotasPacientes.setNaturaleza(naturalezaNotas);
				listaDtoConceptoNotasPacientes = conceptoNotasPacientesMundo.listarConceptoNotasPacientesBusquedaAvanzada(
						dtoConceptoNotasPacientes);
			}
			forma.setListaConceptosNotasPacientes(listaDtoConceptoNotasPacientes);
			forma.setListaConceptosNotasPacientesGuardada(listaDtoConceptoNotasPacientes);
			forma.setMapaCuentasContablesMultiInsitucion(
					conceptoNotasPacientesMundo.obtenerMapaNotasPacientesCuentasContables(listaDtoConceptoNotasPacientes));
		}  else {
			DtoConceptoNotasPacientes dtoConceptoNotasPacientes = new DtoConceptoNotasPacientes();
			if (!UtilidadTexto.isEmpty(forma.getCodigoBusqueda())) {
				dtoConceptoNotasPacientes.setCodigo(forma.getCodigoBusqueda());
			}
			if (!UtilidadTexto.isEmpty(forma.getDescripcionBusqueda())) {
				dtoConceptoNotasPacientes.setDescripcion(forma.getDescripcionBusqueda());
			}
			if(!UtilidadTexto.isEmpty(forma.getNaturalezaDebitoBusqueda()) && 
					!UtilidadTexto.isEmpty(forma.getNaturalezaCreditoBusqueda())){
				dtoConceptoNotasPacientes.setNaturaleza(null);
			} else if (!UtilidadTexto.isEmpty(forma.getNaturalezaDebitoBusqueda())) {
				dtoConceptoNotasPacientes.setNaturaleza(forma.getNaturalezaDebitoBusqueda());
			} else if (!UtilidadTexto.isEmpty(forma.getNaturalezaCreditoBusqueda())) {
				dtoConceptoNotasPacientes.setNaturaleza(forma.getNaturalezaCreditoBusqueda());
			}
			if (!UtilidadTexto.isEmpty(forma.getActivoBusqueda())) {
				dtoConceptoNotasPacientes.setActivo(ConstantesBD.acronimoSi);
			}
			ArrayList<DtoConceptoNotasPacientes> listaDtoConceptosBusqueda = 
				conceptoNotasPacientesMundo.listarConceptoNotasPacientesBusquedaAvanzada(
						dtoConceptoNotasPacientes);
			
			forma.setListaConceptosNotasPacientes(listaDtoConceptosBusqueda);
			forma.setListaConceptosNotasPacientesInicial(listaDtoConceptosBusqueda);
			forma.setListaConceptosNotasPacientesGuardada(
					conceptoNotasPacientesMundo.listarConceptoNotasPacientes());
			forma.setMapaCuentasContablesMultiInsitucion(
					conceptoNotasPacientesMundo.obtenerMapaNotasPacientesCuentasContables(listaDtoConceptosBusqueda));
			
			if (listaDtoConceptosBusqueda.isEmpty()) {
				errores.add("SinResultados", new ActionMessage("errores.modTesoreria.noResultados"));
				paginaCargar = "busquedaAvanzada";
			}
		}
		
		forma.setNaturalezaNotasPacientes(naturalezaNotas);
		forma.setInstitucionMultiempresa(institucionMultiempresa);
		saveErrors(request, errores);
		return paginaCargar;
	}
	
	private String operacionesConceptoNotasPaciente(ConceptoNotasPacientesForm forma, 
			HttpServletRequest request, ActionErrors errores, Instituciones institucion) {
		boolean resultado = true;
		
		ArrayList<DtoConceptoNotasPacientes> dtoConcetpoNotasPacientes = new ArrayList<DtoConceptoNotasPacientes>();
		try {
			UtilidadTransaccion.getTransaccion().begin();
			if(this.modificarConceptosNotasPacientes(forma, request, errores, institucion)) {
				if(this.guardarConceptosNotasPacientes(forma, request, errores, institucion)) {
					UtilidadTransaccion.getTransaccion().commit();
					if (forma.getNaturalezaNotasPacientes().equals(ConstantesIntegridadDominio.acronimoNaturalezaNotaPacienteCreditoDebito)) {
						dtoConcetpoNotasPacientes = conceptoNotasPacientesMundo.listarConceptoNotasPacientes();
					} else {
						DtoConceptoNotasPacientes dtoConceptoNotasPacientes = new DtoConceptoNotasPacientes();
						dtoConceptoNotasPacientes.setNaturaleza(forma.getNaturalezaNotasPacientes());
						dtoConcetpoNotasPacientes = conceptoNotasPacientesMundo.listarConceptoNotasPacientesBusquedaAvanzada(
								dtoConceptoNotasPacientes);
					}
					forma.setListaConceptosNotasPacientes(dtoConcetpoNotasPacientes);
					forma.setListaConceptosNotasPacientesGuardada(dtoConcetpoNotasPacientes);
					forma.setListaConceptosNotasPacientesInicial(new ArrayList<DtoConceptoNotasPacientes>());
					forma.setMapaCuentasContablesMultiInsitucion(
							conceptoNotasPacientesMundo.obtenerMapaNotasPacientesCuentasContables(dtoConcetpoNotasPacientes));
					forma.setListaConceptosNotasPacientesTmp(new ArrayList<DtoConceptoNotasPacientes>());
					forma.setMapaCuentasContablesMultiInsitucionTmp(new HashMap<String, ArrayList<DtoConcNotaPacCuentaCont>>());
					forma.setContadorFilasNuevas(0);
					forma.setOperacionExitosa(true);
				} else {
					resultado = false;
				}
			} else {
				resultado = false;
			}
			if (!resultado) {
				UtilidadTransaccion.getTransaccion().rollback();
			}
		} catch (Exception e) {
			UtilidadTransaccion.getTransaccion().rollback();
			e.printStackTrace();
			Log4JManager.info(e.getCause() + " - " + e.getMessage());
			errores.add("Error", new ActionMessage("errors.procesoNoExitoso", e.getCause() + " - " + e.getMessage()));
			saveErrors(request, errores);
		}
		return "principal";
	}
	
	private boolean guardarConceptosNotasPacientes(ConceptoNotasPacientesForm forma, 
			HttpServletRequest request, ActionErrors errores, Instituciones institucion) {
		ArrayList<DtoConceptoNotasPacientes> listaConceptosNotasPacientes = 
			new ArrayList<DtoConceptoNotasPacientes>();
		ArrayList<DtoConceptoNotasPacientes> listaConceptosNotasPacientesTmp = 
			new ArrayList<DtoConceptoNotasPacientes>();
		boolean existeError = false;
		boolean existeErrorCodigoUnico = false;
		boolean existeErrorDescUnica = false;
		boolean resultado = true;
		for (int i = 0; i < forma.getContadorFilasNuevas(); i++) {
			DtoConceptoNotasPacientes dtoConceptos = new DtoConceptoNotasPacientes();
			ArrayList<DtoConcNotaPacCuentaCont> listaConcNotaPacCuentaCont = new ArrayList<DtoConcNotaPacCuentaCont>();
			if (request.getParameter("codigo_"+i) != null) {
				if(!request.getParameter("codigo_"+i).equals("")) {
					if (this.buscarCoincidencias(request.getParameter("codigo_"+i), 
							 forma.getListaConceptosNotasPacientesGuardada(), "codigo", 1)) {
						existeErrorCodigoUnico = true;
						existeError = true;
					}
					if (this.buscarCoincidencias(request.getParameter("codigo_"+i), 
							listaConceptosNotasPacientes, "codigo", 1)) {
						existeErrorCodigoUnico = true;
						existeError = true;
					}
				} else {
					errores.add("Requerido", 
							new ActionMessage("errors.required", fuenteMensaje.
									getMessage("ConceptoNotasPacientesForm.codigo")));
					saveErrors(request, errores);
					existeError = true;
				}
				dtoConceptos.setCodigo(request.getParameter("codigo_"+i));
				if (request.getParameter("descripcion_"+i) != null) {
					if(!request.getParameter("descripcion_"+i).equals("")) {
						if (this.buscarCoincidencias(request.getParameter("descripcion_"+i), 
								 forma.getListaConceptosNotasPacientesGuardada(), "descripcion", 1)) {
							existeErrorDescUnica = true;
							existeError = true;
						}
						if (this.buscarCoincidencias(request.getParameter("descripcion_"+i), 
								 listaConceptosNotasPacientes, "descripcion", 1)) {
							existeErrorDescUnica = true;
							existeError = true;
						}
					} else {
						errores.add("Requerido", 
								new ActionMessage("errors.required", fuenteMensaje.
										getMessage("ConceptoNotasPacientesForm.descripcion")));
						saveErrors(request, errores);
						existeError = true;
					}
					dtoConceptos.setDescripcion(request.getParameter("descripcion_"+i));
				}
				if (forma.getNaturalezaNotasPacientes().equals(ConstantesIntegridadDominio.acronimoNaturalezaNotaPacienteCreditoDebito)) {
					if (request.getParameter("naturaleza_"+i) != null && !request.getParameter("naturaleza_"+i).equals("")) {
						dtoConceptos.setNaturaleza(request.getParameter("naturaleza_"+i));
					}
				} else if (forma.getNaturalezaNotasPacientes().equals(ConstantesIntegridadDominio.acronimoDebito)) {
					dtoConceptos.setNaturaleza(ConstantesIntegridadDominio.acronimoDebito);
				} else if (forma.getNaturalezaNotasPacientes().equals(ConstantesIntegridadDominio.acronimoCredito)) {
					dtoConceptos.setNaturaleza(ConstantesIntegridadDominio.acronimoCredito);
				}
				long codigoCuenta = -1;
				if (forma.isInstitucionMultiempresa()) {
					for (EmpresasInstitucion empresaInstitucion : forma.getListaEmpresasInstitucion()) {
						ArrayList<DtoConcNotaPacCuentaCont> dtoCuentaConcepto =
							forma.getMapaCuentasContablesMultiInsitucionTmp().get("codigo_cuenta_"+i);
						if (dtoCuentaConcepto != null) {
							for (DtoConcNotaPacCuentaCont dtoConcNotaPacCuentaCont : dtoCuentaConcepto) {
								if (dtoConcNotaPacCuentaCont.getCodigoEmpresasInstitucion() == empresaInstitucion.getCodigo()) {
									DtoConcNotaPacCuentaCont concNotaPacCuentaCont = new DtoConcNotaPacCuentaCont();
									concNotaPacCuentaCont.setEmpresasInstitucion(empresaInstitucion);
									concNotaPacCuentaCont.setInstituciones(institucion);
									concNotaPacCuentaCont.setCuentasContables(dtoConcNotaPacCuentaCont.getCuentasContables());
									listaConcNotaPacCuentaCont.add(concNotaPacCuentaCont);
								}
							}
						}
					}
					dtoConceptos.setListaConceptoNotaPacCuentaCont(listaConcNotaPacCuentaCont);
				} else {
					if(request.getParameter("codigo_cuenta_"+i) != null && 
							!request.getParameter("codigo_cuenta_"+i).equals("-1") &&
							!request.getParameter("codigo_cuenta_"+i).equals("")) {
						DtoConcNotaPacCuentaCont concNotaPacCuentaCont = new DtoConcNotaPacCuentaCont();
						codigoCuenta = Long.parseLong(request.getParameter("codigo_cuenta_"+i));
						concNotaPacCuentaCont.setCuentasContables(conceptoNotasPacientesMundo.obtenerCuentaContable(codigoCuenta));
						concNotaPacCuentaCont.setEmpresasInstitucion(null);
						concNotaPacCuentaCont.setInstituciones(institucion);
						listaConcNotaPacCuentaCont.add(concNotaPacCuentaCont);
						dtoConceptos.setListaConceptoNotaPacCuentaCont(listaConcNotaPacCuentaCont);
					}
				}
				if(request.getParameter("activo_"+i) != null) {
					dtoConceptos.setActivo(ConstantesBD.acronimoSi);
				} else {
					dtoConceptos.setActivo(ConstantesBD.acronimoNo);
				}
				if (dtoConceptos.getCodigo() != null && dtoConceptos.getDescripcion() != null) {
					listaConceptosNotasPacientes.add(dtoConceptos);
				}
				listaConceptosNotasPacientesTmp.add(dtoConceptos);
			}
		}
		forma.setListaConceptosNotasPacientesTmp(listaConceptosNotasPacientesTmp);
		if (!existeError) {
			try {
				if(!listaConceptosNotasPacientes.isEmpty()) {
					resultado = conceptoNotasPacientesMundo.guardarConceptosNotasPacientes(listaConceptosNotasPacientes);
				}
			}  catch (HibernateException e) {
				errores.add("CamposUnicos", 
						new ActionMessage("error.errorEnBlanco", fuenteMensaje.
								getMessage("ConceptoNotasPacientesForm.error.descripcionUnica")));
				saveErrors(request, errores);
				Log4JManager.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> " + e.getCause());
			} catch (RuntimeException e) {
				Log4JManager.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> " + e.getCause());
			} catch (Exception e) {
				Log4JManager.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> " + e.getCause());
			}
		} else {
			resultado = false;
			if (existeErrorCodigoUnico) {
				errores.add("CamposUnicos", 
						new ActionMessage("error.errorEnBlanco", fuenteMensaje.
								getMessage("ConceptoNotasPacientesForm.error.codigoUnico")));
				saveErrors(request, errores);
			}
			if (existeErrorDescUnica) {
				errores.add("CamposUnicos", 
						new ActionMessage("error.errorEnBlanco", fuenteMensaje.
								getMessage("ConceptoNotasPacientesForm.error.descripcionUnica")));
				saveErrors(request, errores);
			}
		}
		return resultado;
	}
	
	private boolean modificarConceptosNotasPacientes(ConceptoNotasPacientesForm forma, 
			HttpServletRequest request, ActionErrors errores, Instituciones institucion) {
		ArrayList<DtoConceptoNotasPacientes> listaConceptosNotasPacientes = 
			new ArrayList<DtoConceptoNotasPacientes>();
		ArrayList<DtoConceptoNotasPacientes> listaConceptosNotasPacientesIterar = 
			new ArrayList<DtoConceptoNotasPacientes>();
		boolean existeError = false;
		boolean resultado = true;
		boolean eliminar = false;
		int cantidadCoincidencias = 1;
		if (forma.isBusquedaAvanzada()) {
			listaConceptosNotasPacientesIterar = forma.getListaConceptosNotasPacientesInicial();
		} else {
			listaConceptosNotasPacientesIterar = forma.getListaConceptosNotasPacientes();
		}
		for (DtoConceptoNotasPacientes conceptos : listaConceptosNotasPacientesIterar) {
			DtoConceptoNotasPacientes dtoConceptos = new DtoConceptoNotasPacientes();
			ArrayList<DtoConcNotaPacCuentaCont> listaConcNotaPacCuentaCont = new ArrayList<DtoConcNotaPacCuentaCont>();
			if (request.getParameter("eliminar_"+conceptos.getCodigoPk()) != null &&
					!request.getParameter("eliminar_"+conceptos.getCodigoPk()).equals("")) {
				if (!UtilidadTexto.getBoolean(request.getParameter("eliminar_"+conceptos.getCodigoPk()))) {
					if (request.getParameter("codigoPK_"+conceptos.getCodigoPk()) != null && 
							!request.getParameter("codigoPK_"+conceptos.getCodigoPk()).equals("")) {
						String relacionNotaPaciente = 
							(UtilidadTexto.isEmpty(request.getParameter("relacionNotaPaciente_"+conceptos.getCodigoPk())) ? 
									null : request.getParameter("relacionNotaPaciente_"+conceptos.getCodigoPk()));
						dtoConceptos.setNumero(conceptos.getNumero());
						dtoConceptos.setCodigoPk(Long.parseLong(request.getParameter("codigoPK_"+conceptos.getCodigoPk())));
						dtoConceptos.setRelacionNaturalezaNotaPaciente(relacionNotaPaciente);
					}
					if (request.getParameter("codigo_mod_"+conceptos.getCodigoPk()) != null) {
						if(!request.getParameter("codigo_mod_"+conceptos.getCodigoPk()).equals("")) {
							if (!request.getParameter("codigo_mod_"+conceptos.getCodigoPk()).equals(conceptos.getCodigo())) {
								if (this.buscarCoincidencias(request.getParameter("codigo_mod_"+conceptos.getCodigoPk()), 
										forma.getListaConceptosNotasPacientesGuardada(), "codigo", cantidadCoincidencias) || 
										this.buscarCoincidencias(request.getParameter("codigo_mod_"+conceptos.getCodigoPk()), 
												listaConceptosNotasPacientes, "codigo", 1)) {
									errores.add("CamposUnicos", 
											new ActionMessage("error.errorEnBlanco", fuenteMensaje.
													getMessage("ConceptoNotasPacientesForm.error.codigoUnico")));
									saveErrors(request, errores);
									existeError = true;
								}
							}
						} else {
							errores.add("Requerido", 
									new ActionMessage("errors.required", fuenteMensaje.
											getMessage("ConceptoNotasPacientesForm.codigo")));
							saveErrors(request, errores);
							existeError = true;
						}
						dtoConceptos.setCodigo(request.getParameter("codigo_mod_"+conceptos.getCodigoPk()));
					}
					if (request.getParameter("descripcion_mod_"+conceptos.getCodigoPk()) != null) {
						if(!request.getParameter("descripcion_mod_"+conceptos.getCodigoPk()).equals("")) {
							if (!request.getParameter("descripcion_mod_"+conceptos.getCodigoPk()).equals(conceptos.getDescripcion())) {
								if (this.buscarCoincidencias(request.getParameter("descripcion_mod_"+conceptos.getCodigoPk()), 
										forma.getListaConceptosNotasPacientesGuardada(), "descripcion", cantidadCoincidencias) ||
										this.buscarCoincidencias(request.getParameter("descripcion_mod_"+conceptos.getCodigoPk()), 
												listaConceptosNotasPacientes, "descripcion", 1)) {
									errores.add("CamposUnicos", 
											new ActionMessage("error.errorEnBlanco", fuenteMensaje.
													getMessage("ConceptoNotasPacientesForm.error.descripcionUnica")));
									saveErrors(request, errores);
									existeError = true;
								}
							}
						} else {
							errores.add("Requerido", 
									new ActionMessage("errors.required", fuenteMensaje.
											getMessage("ConceptoNotasPacientesForm.descripcion")));
							saveErrors(request, errores);
							existeError = true;
						}
						dtoConceptos.setDescripcion(request.getParameter("descripcion_mod_"+conceptos.getCodigoPk()));
					}
					if (forma.getNaturalezaNotasPacientes().equals(ConstantesIntegridadDominio.acronimoNaturalezaNotaPacienteCreditoDebito)) {
						if (request.getParameter("naturaleza_mod_"+conceptos.getCodigoPk()) != null && 
								!request.getParameter("naturaleza_mod_"+conceptos.getCodigoPk()).equals("")) {
							dtoConceptos.setNaturaleza(request.getParameter("naturaleza_mod_"+conceptos.getCodigoPk()));
						}
					} else if (forma.getNaturalezaNotasPacientes().equals(ConstantesIntegridadDominio.acronimoDebito)) {
						dtoConceptos.setNaturaleza(ConstantesIntegridadDominio.acronimoDebito);
					} else if (forma.getNaturalezaNotasPacientes().equals(ConstantesIntegridadDominio.acronimoCredito)) {
						dtoConceptos.setNaturaleza(ConstantesIntegridadDominio.acronimoCredito);
					}
					if (forma.isInstitucionMultiempresa()) {
						for (EmpresasInstitucion empresaInstitucion : forma.getListaEmpresasInstitucion()) {
							ArrayList<DtoConcNotaPacCuentaCont> dtoCuentaConcepto =
								forma.getMapaCuentasContablesMultiInsitucion().get("codigo_cuenta_mod_"+conceptos.getCodigoPk());
							if (dtoCuentaConcepto != null) {
								ArrayList<DtoConcNotaPacCuentaCont> dtoConcNotaPacCuentaContEliminar = 
									conceptoNotaPacCuentaContMundo.listarConceptoNotaPacCuentaCont(dtoConceptos.getCodigoPk());
								for (DtoConcNotaPacCuentaCont dtoConcNotaPacCuentaCont : dtoConcNotaPacCuentaContEliminar) {
									ConcNotaPacCuentaCont concNotaPacCuentaContEliminar = new ConcNotaPacCuentaCont();
									concNotaPacCuentaContEliminar.setCodigo(dtoConcNotaPacCuentaCont.getCodigo());
									concNotaPacCuentaContEliminar.setConceptoNotaPaciente(dtoConcNotaPacCuentaCont.getConceptoNotaPaciente());
									concNotaPacCuentaContEliminar.setCuentasContables(dtoConcNotaPacCuentaCont.getCuentasContables());
									concNotaPacCuentaContEliminar.setEmpresasInstitucion(dtoConcNotaPacCuentaCont.getEmpresasInstitucion());
									concNotaPacCuentaContEliminar.setInstituciones(dtoConcNotaPacCuentaCont.getInstituciones());
									conceptoNotaPacCuentaContMundo.eliminarConceptoNotaPacCuentaCont(concNotaPacCuentaContEliminar);
								}
								for (DtoConcNotaPacCuentaCont dtoConcNotaPacCuentaCont : dtoCuentaConcepto) {
									if (dtoConcNotaPacCuentaCont.getCodigoEmpresasInstitucion() == empresaInstitucion.getCodigo()) {
										DtoConcNotaPacCuentaCont concNotaPacCuentaCont = new DtoConcNotaPacCuentaCont();
										concNotaPacCuentaCont.setEmpresasInstitucion(empresaInstitucion);
										concNotaPacCuentaCont.setInstituciones(institucion);
										concNotaPacCuentaCont.setCuentasContables(dtoConcNotaPacCuentaCont.getCuentasContables());
										listaConcNotaPacCuentaCont.add(concNotaPacCuentaCont);
									}
								}
							}
						}
						dtoConceptos.setListaConceptoNotaPacCuentaCont(listaConcNotaPacCuentaCont);
					} else {
						DtoConcNotaPacCuentaCont concNotaPacCuentaCont = new DtoConcNotaPacCuentaCont();
						if(request.getParameter("codigo_cuenta_mod_"+conceptos.getCodigoPk()) != null && 
								!request.getParameter("codigo_cuenta_mod_"+conceptos.getCodigoPk()).equals("")) {
							long codigoCuenta = Long.parseLong(request.getParameter("codigo_cuenta_mod_"+conceptos.getCodigoPk()));
							long codigoNotaPcCuenCont = 0;
							if(!request.getParameter("codigo_concNotaCuentaCont_mod_"+conceptos.getCodigoPk()).equals("")){
								codigoNotaPcCuenCont = Long.parseLong(request.getParameter("codigo_concNotaCuentaCont_mod_"+conceptos.getCodigoPk()));
							}	
							concNotaPacCuentaCont.setCodigo(codigoNotaPcCuenCont);
							concNotaPacCuentaCont.setCuentasContables(conceptoNotasPacientesMundo.obtenerCuentaContable(codigoCuenta));
							concNotaPacCuentaCont.setEmpresasInstitucion(null);
							concNotaPacCuentaCont.setInstituciones(institucion);
							listaConcNotaPacCuentaCont.add(concNotaPacCuentaCont);
							dtoConceptos.setListaConceptoNotaPacCuentaCont(listaConcNotaPacCuentaCont);
						} else { 
							if(!request.getParameter("codigo_concNotaCuentaCont_mod_"+conceptos.getCodigoPk()).equals("")){
								long codigoNotaPcCuenCont = Long.parseLong(request.getParameter("codigo_concNotaCuentaCont_mod_"+conceptos.getCodigoPk()));
								concNotaPacCuentaCont.setCodigo(codigoNotaPcCuenCont);
								concNotaPacCuentaCont.setCuentasContables(null);
								listaConcNotaPacCuentaCont.add(concNotaPacCuentaCont);
								dtoConceptos.setListaConceptoNotaPacCuentaCont(listaConcNotaPacCuentaCont);
							}
						}
					}
					if(request.getParameter("activo_mod_"+conceptos.getCodigoPk()) != null) {
						dtoConceptos.setActivo(ConstantesBD.acronimoSi);
					} else {
						dtoConceptos.setActivo(ConstantesBD.acronimoNo);
					}
				} else {
					conceptoNotasPacientesMundo.eliminarConceptoNotaPaciente(conceptos.getCodigoPk());
					eliminar = true;
				}
			}
			if (!eliminar) {
				listaConceptosNotasPacientes.add(dtoConceptos);
			}
			eliminar = false;
		}
		forma.setListaConceptosNotasPacientes(listaConceptosNotasPacientes);
		if (!existeError) {
			if (!listaConceptosNotasPacientes.isEmpty()) {
				resultado = conceptoNotasPacientesMundo.modificarConceptosNotasPacientes(listaConceptosNotasPacientes);
			}
		} else {
			resultado = false;
		}
		return resultado;
	}

	/**
	 * 
	 * @param forma
	 * @param request
	 * @param errores
	 */
	private void cargarCuentasContablesEmpresaInstitucion(ConceptoNotasPacientesForm forma, 
			HttpServletRequest request, ActionErrors errores) {
		HashMap<String, ArrayList<DtoConcNotaPacCuentaCont>> mapaCuentasContablesMultiInsitucion = 
			forma.getMapaCuentasContablesMultiInsitucionTmp();
		ArrayList<DtoConcNotaPacCuentaCont> listaConcNotaPacCuentaCont = new ArrayList<DtoConcNotaPacCuentaCont>();
		
		try {
			String clave = request.getParameter("codigoCuenta_");
			Log4JManager.info(">>>>>>>>>>>>>>>>>>>>>>>>>>> CLAVE: " + clave);
			for(EmpresasInstitucion empresaInstitucion : forma.getListaEmpresasInstitucion()) {
				String codigoCuenta = request.getParameter("codigo_cuenta_sub_" + empresaInstitucion.getCodigo());
				Log4JManager.info(">>>>>>>>>>>>>>>>>>>>>>>>>>> VALOR: " + codigoCuenta);
				String codigoEmpresaInstitucion = request.getParameter("empresa_institucion_" + empresaInstitucion.getCodigo());
				Log4JManager.info(">>>>>>>>>>>>>>>>>>>>>>>>>>> Empresa: " + empresaInstitucion.getCodigo());
				if (codigoCuenta != null && !codigoCuenta.equals("")) {
					if (empresaInstitucion != null && !empresaInstitucion.equals("")) {
						DtoConcNotaPacCuentaCont dtoConcNotaPacCuentaCont = new DtoConcNotaPacCuentaCont();
						UtilidadTransaccion.getTransaccion().begin();
						dtoConcNotaPacCuentaCont.setCuentasContables(
								conceptoNotasPacientesMundo.obtenerCuentaContable(Long.parseLong(codigoCuenta)));
						UtilidadTransaccion.getTransaccion().commit();
						dtoConcNotaPacCuentaCont.setCodigoEmpresasInstitucion(Long.parseLong(codigoEmpresaInstitucion));
						listaConcNotaPacCuentaCont.add(dtoConcNotaPacCuentaCont);
					}
				}
			}
			mapaCuentasContablesMultiInsitucion.put(clave, listaConcNotaPacCuentaCont);
			forma.setMapaCuentasContablesMultiInsitucionTmp(mapaCuentasContablesMultiInsitucion);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @param forma
	 * @param request
	 * @param errores
	 */
	private void modificarCuentasContablesEmpresaInstitucion(ConceptoNotasPacientesForm forma, 
			HttpServletRequest request, ActionErrors errores) {
		HashMap<String, ArrayList<DtoConcNotaPacCuentaCont>> mapaCuentasContablesMultiInsitucion = 
			forma.getMapaCuentasContablesMultiInsitucion();
		ArrayList<DtoConcNotaPacCuentaCont> listaConcNotaPacCuentaCont = new ArrayList<DtoConcNotaPacCuentaCont>();
		
		try {
			String clave = request.getParameter("codigoCuenta_mod_");
			for(EmpresasInstitucion empresaInstitucion : forma.getListaEmpresasInstitucion()) {
				String codigoCuenta = request.getParameter("codigo_cuenta_sub_mod_" + empresaInstitucion.getCodigo());
				String codigoEmpresaInstitucion = request.getParameter("empresa_institucion_mod_" + empresaInstitucion.getCodigo());
				if (codigoCuenta != null && !codigoCuenta.equals("")) {
					if (empresaInstitucion != null && !empresaInstitucion.equals("")) {
						DtoConcNotaPacCuentaCont dtoConcNotaPacCuentaCont = new DtoConcNotaPacCuentaCont();
						UtilidadTransaccion.getTransaccion().begin();
						dtoConcNotaPacCuentaCont.setCuentasContables(
								conceptoNotasPacientesMundo.obtenerCuentaContable(Long.parseLong(codigoCuenta)));
						UtilidadTransaccion.getTransaccion().commit();
						dtoConcNotaPacCuentaCont.setCodigoEmpresasInstitucion(Long.parseLong(codigoEmpresaInstitucion));
						dtoConcNotaPacCuentaCont.setEmpresasInstitucion(empresaInstitucion);
						listaConcNotaPacCuentaCont.add(dtoConcNotaPacCuentaCont);
					}
				}
			}
			mapaCuentasContablesMultiInsitucion.put(clave, listaConcNotaPacCuentaCont);
			forma.setMapaCuentasContablesMultiInsitucion(mapaCuentasContablesMultiInsitucion);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	private boolean buscarCoincidencias(String valorBuscar, ArrayList<DtoConceptoNotasPacientes> lista, String tipo, int coincidencias) {
		int cont = 0;
		if (lista != null && !lista.isEmpty()) {
			for (DtoConceptoNotasPacientes conceptoNotaPaciente : lista) {
				if ("codigo".equals(tipo) && conceptoNotaPaciente.getCodigo() != null) {
					if (valorBuscar.toLowerCase().equals(conceptoNotaPaciente.getCodigo().toLowerCase())) {
						cont++;
						if(cont >= coincidencias )
							return true;
					}
				}
				if ("descripcion".equals(tipo) && conceptoNotaPaciente.getDescripcion() != null) {
					if (valorBuscar.toLowerCase().equals(conceptoNotaPaciente.getDescripcion().toLowerCase())) {
						cont++;
						if(cont >= coincidencias )
							return true;
					}
				}
			}
		}
		return false;
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
	public String accionOrdenar(ConceptoNotasPacientesForm forma, String tipo){
		String retorno = "";
		boolean ordenamiento = false;
		if(forma.getEsDescendente().equals(forma.getPatronOrdenar() + "descendente")) {
			ordenamiento = true;
		}
		SortGenerico sortG = new SortGenerico(forma.getPatronOrdenar(),ordenamiento);
		if ("conceptosNotasPacientes".equals(tipo)) {
			Collections.sort(forma.getListaConceptosNotasPacientes(), sortG);
			retorno = "principal";
		}
		return retorno;
	}
}
