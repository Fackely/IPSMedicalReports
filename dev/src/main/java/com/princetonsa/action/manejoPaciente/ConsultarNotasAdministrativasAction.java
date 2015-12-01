package com.princetonsa.action.manejoPaciente;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import util.ConstantesIntegridadDominio;
import util.UtilidadFecha;

import com.princetonsa.action.historiaClinica.OtrosDiagnosticosAction;
import com.princetonsa.actionform.manejoPaciente.ConsultarNotasAdministrativasForm;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.servinte.axioma.hibernate.HibernateUtil;
import com.servinte.axioma.orm.Cuentas;
import com.servinte.axioma.orm.DetNotasAdministrativas;
import com.servinte.axioma.orm.Ingresos;
import com.servinte.axioma.orm.IngresosHome;
import com.servinte.axioma.orm.NotasAdministrativas;
import com.servinte.axioma.orm.delegate.UsuariosDelegate;
import com.servinte.axioma.orm.delegate.administracion.CuentasDelegate;
import com.servinte.axioma.orm.delegate.administracion.DetNotasAdministrativasDelegate;
import com.servinte.axioma.orm.delegate.administracion.NotasAdministrativasDelegate;
import com.servinte.axioma.orm.delegate.manejoPaciente.IngresosDelegate;


public class ConsultarNotasAdministrativasAction extends Action {

	/**
	 * Objeto para manejar los logs de esta clase
	 */
	private Logger logger = Logger.getLogger(OtrosDiagnosticosAction.class);

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		if (form instanceof ConsultarNotasAdministrativasForm) {
			// OBJETOS A USAR
			/**
			 * Formulario
			 */
			ConsultarNotasAdministrativasForm forma = (ConsultarNotasAdministrativasForm) form;

			/**
			 * Usuario
			 */
			UsuarioBasico usuario = (UsuarioBasico) request.getSession()
					.getAttribute("usuarioBasico");
			PersonaBasica paciente = (PersonaBasica) request.getSession()
					.getAttribute("pacienteActivo");

			String estado = forma.getEstado();

			logger.warn("estado notasAdministrativasAction--> " + estado);

			if (estado == null) {
				request.setAttribute("codigoDescripcionError",
						"errors.estadoInvalido");
				return mapping.findForward("paginaError");
			}

			// empezar
			else if ((estado.equals("empezar")) || (estado.equals("resumen"))) {

				return accionEmpezar(mapping, forma, usuario, paciente,request);
			}

			// inicio
			else if ((estado.equals("inicio")) || (estado.equals("resumen"))) {

				return accionEmpezar(mapping, forma, usuario, paciente,request);
			}

			// nueva nota
			else if (estado.equals("notaNueva")) {
				return accionNotaNueva(mapping, forma, usuario);
			}
			
			// nuevo comentario
			else if (estado.equals("detallenuevo")) {
				return accionDetalleNuevo(mapping, forma, usuario);
			}
						
			// ingreso en DB de la nota
			else if(estado.equals("nuevo"))
			{
				
				return accionNuevo(mapping, forma, usuario, request, paciente);
			}

			// nuevo comentario ingreso a DB
			else if (estado.equals("nuevocomentario")) {
				return accionNuevoComentario(mapping, forma, usuario, request, paciente);
			}
			
			// guardar
//			else if (estado.equals("guardar")) {
//				return accionNuevo(mapping, forma, usuario, request, paciente);
//			}
			
			// ver detalle nota 
			else if(estado.equals("verdetalle")) { return
				accionVerDetalle(mapping,forma, usuario, request); }
			
			
			
			/*
			 * // ordenar else if(estado.equals("ordenar")) { return
			 * accionOrdenar(mapping, forma, usuario); }
			 * 
			 * // eliminar else if(estado.equals("eliminar")) { return
			 * accionEliminar(mapping, forma, usuario); }
			 * 
			 * 
			 * 
			 * // guardarmodificar /*else if(estado.equals("guardarmodificar"))
			 * { return accionGuardarModificar(mapping, forma, usuario,
			 * request); }
			 */

			// volver
			else if (estado.equals("volver")) {
				return accionEmpezar(mapping, forma, usuario, paciente,request);
			}

		}
		return null;
	}

	/**
	 * @param mapping
	 * @param forma.
	 * @param usuario
	 * @return
	 */
	private ActionForward accionEmpezar(ActionMapping mapping,
			ConsultarNotasAdministrativasForm forma, UsuarioBasico usuario,
			PersonaBasica paciente, HttpServletRequest request) {
		
		forma.reset();
		
		HibernateUtil.beginTransaction();
		
		ArrayList<Ingresos> listaIngresos = new IngresosDelegate().listarCuentasPorIngreso(paciente.getCodigoPersona()); 
		
		

		for(Ingresos ingreso : listaIngresos){
			ingreso.getHoraEgreso();
		}

		forma.setListaDtoIngresos(listaIngresos);	

		
		HibernateUtil.endTransaction();
		
		//forma.setPersona(persona);
		
		

		return mapping.findForward("principal");
	}

	/**
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @return
	 */
	private ActionForward accionNuevo(ActionMapping mapping,
			ConsultarNotasAdministrativasForm forma, UsuarioBasico usuario,HttpServletRequest request, 
			PersonaBasica persona) {
		
	
		HibernateUtil.beginTransaction();
				
		forma.getDto().setIngresos(new IngresosHome().findById(persona.getCodigoIngreso()));
		forma.getDto().setUsuarios(new UsuariosDelegate().findById(usuario.getLoginUsuario()));
		
		
		new NotasAdministrativasDelegate().persist(forma.getDto());
		HibernateUtil.endTransaction();
		forma.reset();
		ArrayList<NotasAdministrativas> listaNotas = new NotasAdministrativasDelegate().listarNotas(persona.getCodigoIngreso()); 
		forma.setListaDto(listaNotas);	
		forma.setEstado("empezar");
		return mapping.findForward("principal");
	}
	
	
	/**
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @return
	 */
	private ActionForward accionNuevoComentario(ActionMapping mapping,
			ConsultarNotasAdministrativasForm forma, UsuarioBasico usuario,HttpServletRequest request, 
			PersonaBasica persona) {
		
	
		HibernateUtil.cerrarSession();
		HibernateUtil.beginTransaction();
		
				
		forma.getDtoDet().setNotasAdministrativas(forma.getDto());
		forma.getDtoDet().setUsuarios(new UsuariosDelegate().findById(usuario.getLoginUsuario()));
	
		new DetNotasAdministrativasDelegate().persist(forma.getDtoDet());
		HibernateUtil.endTransaction();
		
		
		
		int codigoNotaAdministrativa = forma.getDto().getCodigoPk(); 
		ArrayList<DetNotasAdministrativas> listaDetalles = new DetNotasAdministrativasDelegate()
		.listarDetallesDeNotaAdministrativa(codigoNotaAdministrativa);
		forma.resetDos();
		
		
		forma.setListaDetDto(listaDetalles);
		NotasAdministrativas x = new NotasAdministrativas();
		x.setCodigoPk(codigoNotaAdministrativa);
		forma.getDtoDet().setNotasAdministrativas(x);
			
		forma.setEstado("verdetalle");
		return mapping.findForward("principal");
	}

	
	/**
	 * 
	 * @param forma
	 * @param usuario
	 */
	private void inicializarDto(ConsultarNotasAdministrativasForm forma, UsuarioBasico usuario)
	{
		forma.getDto().setFechaModifica(UtilidadFecha.getFechaActualTipoBD());
		forma.getDto().setHoraModifica(UtilidadFecha.getHoraActual());
		forma.getDto().setUsuarios(new UsuariosDelegate().findById(usuario.getLoginUsuario()));
	}
	
	/**
	 * 
	 * @param forma
	 * @param usuario
	 */
	private void inicializarDtoDet(ConsultarNotasAdministrativasForm forma, UsuarioBasico usuario)
	{
		forma.getDtoDet().setFechaModifica(UtilidadFecha.getFechaActualTipoBD());
		forma.getDtoDet().setHoraModifica(UtilidadFecha.getHoraActual());
		forma.getDtoDet().setUsuarios(new UsuariosDelegate().findById(usuario.getLoginUsuario()));
	}
	
	
	/**
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @return
	 */
	private ActionForward accionNotaNueva(ActionMapping mapping,
			ConsultarNotasAdministrativasForm forma, UsuarioBasico usuario) {
		//forma.reset();
		inicializarDto(forma, usuario);
		forma.setEstado("empezar");
		forma.setMostrarFormularioIngreso(true);
		return mapping.findForward("principal");
	}

	/**
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @return
	 */
	private ActionForward accionDetalleNuevo(ActionMapping mapping,
			ConsultarNotasAdministrativasForm forma, UsuarioBasico usuario) {
		//forma.reset();
		inicializarDtoDet(forma, usuario);
		forma.setMostrarFormularioIngresoDetalle(true);
		forma.setEstado("verdetalle");
		return mapping.findForward("principal");
	}
	
	

	/**
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @param request
	 * @return
	 */
	private ActionForward accionVerDetalle(ActionMapping mapping,
			ConsultarNotasAdministrativasForm forma, UsuarioBasico usuario,
			HttpServletRequest request) {
		
		
		forma.setDto(forma.getListaDto().get(forma.getPosArray()));
		
		
		//-------
		int codigoNotaAdministrativa = forma.getDto().getCodigoPk(); 
		ArrayList<DetNotasAdministrativas> listaDetalles = new DetNotasAdministrativasDelegate()
			.listarDetallesDeNotaAdministrativa(codigoNotaAdministrativa);

		//forma.setListaDetDto(listaDetalles);
		//-------		
		
		
		HibernateUtil.beginTransaction();
		
		for(DetNotasAdministrativas detNota : listaDetalles){
			detNota.getNotasAdministrativas().getCodigoPk();
		}

		forma.setListaDetDto(listaDetalles);	

		HibernateUtil.endTransaction();
		
		forma.setEstado("verdetalle");
		
		return mapping.findForward("principal");
	}
	
	
	/**
	 * Muestra un mensaje indicando que no hay paciente cargado
	 * @param forma
	 * @param request
	 */
	private void mostrarErrorPacienteNoCargado(ConsultarNotasAdministrativasForm forma, HttpServletRequest request) 
	{
		ActionErrors errores = new ActionErrors();
		errores.add("error no_paciente_cargado", new ActionMessage("errors.paciente.noCargado"));
		saveErrors(request, errores);
		forma.setEstado("empezar");
	}
	
	/**
	 * validacion de cuenta activa, asociada o facturada parcial.
	 * @param forma
	 * @param request
	 * @param paciente
	 */
	private void mostrarErroresCuenta(ConsultarNotasAdministrativasForm forma, HttpServletRequest request, PersonaBasica paciente) 
	{
		int cuentaPaciente =paciente.getCodigoCuenta();
		
		Cuentas cuenta = new CuentasDelegate().findById(cuentaPaciente);
		
		
		int estado=1;
		try {
			estado = cuenta.getEstadosCuenta().getCodigo();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		if(estado != 0 && estado != 3 && estado == 6)
		{
			ActionErrors errores = new ActionErrors();
			errores.add("error paciente_estado_invalido", new ActionMessage("error.paciente.estadoInvalido","activa, asociada o facturada parcial"));
			saveErrors(request, errores);
			forma.setEstado("inicio");
						
			}
	}
	
	/**
	 * validacion de cuenta abierta
	 * 
	 * @param forma
	 * @param request
	 * @param paciente
	 */
	
	private void mostrarErrorNoIngreso(ConsultarNotasAdministrativasForm forma, HttpServletRequest request, PersonaBasica paciente) 
	{
		int IngresoPaciente =paciente.getCodigoIngreso();
		
		Ingresos ingreso = new IngresosHome().findById(IngresoPaciente);
		
		if(!ingreso.getEstado().equals(ConstantesIntegridadDominio.acronimoEstadoAbierto))
		{
			ActionErrors errores = new ActionErrors();
			errores.add("error no_paciente_cargado", new ActionMessage("errors.paciente.noCargado"));
			saveErrors(request, errores);
			forma.setEstado("inicio");
						
		}
		
	}



}
