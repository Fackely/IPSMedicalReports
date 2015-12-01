package com.princetonsa.action.general;

import java.sql.Connection;
import java.util.Observable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.axioma.util.log.Log4JManager;

import util.TipoNumeroId;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadSesion;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.Administracion.UtilidadesAdministracion;

import com.princetonsa.actionform.general.LoginForm;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.servinte.axioma.hibernate.HibernateUtil;
import com.servinte.axioma.orm.Usuarios;
import com.servinte.axioma.orm.delegate.UsuariosDelegate;

public class LoginAction extends Action{
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse respose) throws Exception {
		
		Connection con=null;
		ActionForward forward=null;
		boolean usuarioInactivo = false;
		try{
			HibernateUtil.beginTransaction();
			if(form instanceof LoginForm)
			{
				HttpSession session=request.getSession();
				LoginForm forma=(LoginForm)form;
				UsuariosDelegate usuariosDelegate=new UsuariosDelegate();
				Usuarios usuarioBD=usuariosDelegate.validarUsuario(forma.getUsuario(), forma.getPassword());
	
				session.setAttribute("mensajeInactivo", "");
				
				if(usuarioBD!=null)
				{
					if(!UtilidadTexto.getBoolean(usuarioBD.getActivo()))
					{
						session.setAttribute("mensajeInactivo", "El Usuario se Encuentra Inactivo. Por favor intente de nuevo.");
						usuarioInactivo = true;
						forward = mapping.findForward("loginInactivo");
					}
					
					
	
					String fecha=UtilidadesAdministracion.fechaUltimoLoginUsuario(usuarioBD.getLogin());
					String fechaUltimoLogin=UtilidadFecha.conversionFormatoFechaAAp(fecha);
					String fechaActivacion=UtilidadFecha.conversionFormatoFechaAAp(usuarioBD.getFechaUltimaActivaUsu());
					String fechaEvaluar=fechaUltimoLogin;
					if(!UtilidadTexto.isEmpty(fechaEvaluar) && !UtilidadTexto.isEmpty(fechaActivacion))
					{
						if(UtilidadFecha.esFechaMenorQueOtraReferencia(fechaUltimoLogin, fechaActivacion))
						{
							fechaEvaluar=fechaActivacion;
						}
					}
					
					String fechaActual=UtilidadFecha.getFechaActual();
					if(!UtilidadTexto.isEmpty(fechaEvaluar))
					{
						if(usuarioBD.getDiasInactivarUsuario()!=null&&usuarioBD.getDiasInactivarUsuario()>0)
						{
							
							String fechaIncrementada=UtilidadFecha.incrementarDiasAFecha(fechaEvaluar, usuarioBD.getDiasInactivarUsuario(), false);
							if(UtilidadFecha.esFechaMenorQueOtraReferencia(UtilidadFecha.conversionFormatoFechaAAp(fechaIncrementada), fechaActual))
							{
								UtilidadesAdministracion.inactivarUsuario(usuarioBD.getLogin());
								session.setAttribute("mensajeInactivo", "El Usuario se Encuentra Inactivo. Por favor intente de nuevo.");
								usuarioInactivo = true;
								forward = mapping.findForward("loginInactivo");
							}
						}
					}
					
					
					fechaActivacion=UtilidadFecha.conversionFormatoFechaAAp(usuarioBD.getFechaUltimaActivaPasswd());
					fechaEvaluar=fechaUltimoLogin;
					if(!UtilidadTexto.isEmpty(fechaEvaluar) && !UtilidadTexto.isEmpty(fechaActivacion))
					{
						if(UtilidadFecha.esFechaMenorQueOtraReferencia(fechaUltimoLogin, fechaActivacion))
						{
							fechaEvaluar=fechaActivacion;
						}
					}
					if(!UtilidadTexto.getBoolean(usuarioBD.getPasswordActivo()))
					{
						session.setAttribute("mensajeInactivo", "La Contraseña a Caducado por Inactividad. Por favor comuniquese con el Administrador.");
						usuarioInactivo = true;
						forward = mapping.findForward("loginInactivo");
					}
					if(usuarioBD.getDiasCaducidadPassword()!=null&&usuarioBD.getDiasCaducidadPassword()>0)
					{
						if(!UtilidadTexto.isEmpty(fechaEvaluar))
						{
							
							
							String fechaIncrementada=UtilidadFecha.incrementarDiasAFecha(fechaEvaluar, usuarioBD.getDiasCaducidadPassword(), false);
							if(UtilidadFecha.esFechaMenorQueOtraReferencia(UtilidadFecha.conversionFormatoFechaAAp(fechaIncrementada), fechaActual))
							{
								UtilidadesAdministracion.inactivarPasswordUsuario(usuarioBD.getLogin());
								session.setAttribute("mensajeInactivo", "La Contraseña a Caducado. Por favor comuniquese con el Administrador.");
								usuarioInactivo = true;
								forward = mapping.findForward("loginInactivo");
							}
						}
					}
					
					
					
					String diasVigencia=ValoresPorDefecto.getDiasVigenciaContraUsuario(usuarioBD.getInstituciones().getCodigo());
					if(!UtilidadTexto.isEmpty(diasVigencia))
					{
						if(usuarioBD.getDiasCaducidadPassword()!=null&&usuarioBD.getDiasCaducidadPassword()>0)
						{
							fecha=UtilidadFecha.conversionFormatoFechaAAp(usuarioBD.getFechaUltimaActivaPasswd());
							if(!UtilidadTexto.isEmpty(fecha))
							{
								String fechaIncrementada=UtilidadFecha.incrementarDiasAFecha(fecha, usuarioBD.getDiasCaducidadPassword(), false);
								if(UtilidadFecha.esFechaMenorQueOtraReferencia(fechaIncrementada, fechaActual))
								{
									UtilidadesAdministracion.inactivarPasswordUsuario(usuarioBD.getLogin());
									session.setAttribute("mensajeInactivo", "La Contraseña a Caducado. Por favor comuniquese con el Administrador.");
									usuarioInactivo = true;
									forward = mapping.findForward("loginInactivo");
								}
								else
								{
									String diasAlertaParametro=ValoresPorDefecto.getDiasFinalesVigenciaContraMostrarAlerta(usuarioBD.getInstituciones().getCodigo());
									if(!UtilidadTexto.isEmpty(diasAlertaParametro))
									{
										int diasMostrarAlerta=UtilidadFecha.numeroDiasEntreFechas(fechaActual,fechaIncrementada);
										if(diasMostrarAlerta<=Utilidades.convertirAEntero(diasAlertaParametro))
										{
											session.setAttribute("mensajeInactivo", "Su contraseña caduca en "+diasMostrarAlerta+" días. Recuerde que debe realizar su cambio de contraseña periodicamente.");
										}
									}
								}
							}
							else
							{
								UtilidadesAdministracion.inactivarPasswordUsuario(usuarioBD.getLogin());
								session.setAttribute("mensajeInactivo", "La Contraseña a Caducado. Por favor comuniquese con el Administrador.");
								usuarioInactivo = true;
								forward = mapping.findForward("loginInactivo");
							}
						}
					}
					
					if (!usuarioInactivo) {

						con=UtilidadBD.abrirConexion();

						UsuarioBasico usuario = (UsuarioBasico) session.getAttribute("usuarioBasico");
						PersonaBasica pacienteActivo = (PersonaBasica) session.getAttribute("pacienteActivo");

						usuario = new UsuarioBasico();
						usuario.cargarUsuarioBasico(con, usuarioBD.getLogin());


						if (usuario.getEsSoloPaciente())
						{
							//pacienteActivo.car
							// Código necesario para notificar a todos los observadores que el ingreso/cuenta/admisión del paciente en sesión pudo haber cambiado
							TipoNumeroId identificacion = new TipoNumeroId(usuario.getCodigoTipoIdentificacion(), usuario.getNumeroIdentificacion());
							if (pacienteActivo==null)
							{
								pacienteActivo=new PersonaBasica();
							}
							pacienteActivo.cargar(con, identificacion);
							pacienteActivo.cargarPaciente2(con, identificacion, usuario.getCodigoInstitucion(),usuario.getCodigoCentroAtencion()+"");
							// Código necesario para registrar este paciente como Observer
							Observable observable = (Observable) session.getServletContext().getAttribute("observable");
							if (observable != null) {
								pacienteActivo.setObservable(observable);
								// Si ya lo habíamos añadido, la siguiente línea no hace nada
								observable.addObserver(pacienteActivo);
							}
							session.setAttribute("pacienteActivo", pacienteActivo);
						}
						session.setAttribute("usuarioBasico", usuario);
						session.setAttribute("loginUsuario", usuario.getLoginUsuario());
						session.setAttribute("userName", usuario.getLoginUsuario());

						int codigoSession=UtilidadBD.obtenerSiguienteValorSecuencia(con,"seq_login_usuarios");
						session.setAttribute("codigoSession", codigoSession);

						UtilidadSesion.insertarUsuarioSession(con,codigoSession,usuario.getLoginUsuario(),session.getId());

						UtilidadSesion estadoSesion= (UtilidadSesion)session.getAttribute("estadoSesion");

						if (estadoSesion == null) {
							estadoSesion = new UtilidadSesion();
							session.setAttribute("estadoSesion", estadoSesion);
						}

						/*
						 * Por último ponemos un objeto de tipo PersonaBasica en memoria (Manejo del paciente actual),
						 * que aunque no tiene nada, es necesario al momento de la presentación (para que el JSP no
						 * se tenga que preocupar de nulos y demás).
						 */
						if (pacienteActivo == null) {

							pacienteActivo = new PersonaBasica();

							pacienteActivo.setAnioAdmision(0);
							pacienteActivo.setCodigoAdmision(0);
							pacienteActivo.setCodigoCuenta(0);
							pacienteActivo.setCodigoIngreso(0);
							pacienteActivo.setCodigoSexo(0);
							pacienteActivo.setCodigoTipoIdentificacionPersona("");
							pacienteActivo.setCodigoTipoRegimen(' ');
							pacienteActivo.setCodigoUltimaViaIngreso(0);
							pacienteActivo.setConvenioPersonaResponsable("");
							pacienteActivo.setEdad(0);
							pacienteActivo.setNombrePersona("No se ha cargado paciente");
							pacienteActivo.setNumeroIdentificacionPersona("");
							pacienteActivo.setTipoIdentificacionPersona("");
							pacienteActivo.setUltimaViaIngreso("");

							session.setAttribute("pacienteActivo", pacienteActivo);
						}

						/////////validaciones de usuario y password inactivo.
						//



						//respose.sendRedirect(request.getContextPath()+"/common/validacionCentroCosto/loginCentroCosto.jsp");
						forward = mapping.findForward("centroCosto");
						//return null;
					}
				}
				else
				{
					forward = mapping.findForward("login");
					ActionErrors errores=new ActionErrors();
					errores.add("error_usuario_contraseña", new ActionMessage("errors.usuarioContrasenaInvalido"));
					saveErrors(request, errores);
				}	
			}
			HibernateUtil.endTransaction();
		}
		catch(Exception e){
			Log4JManager.error("Error Execute LoginAction ", e);
			HibernateUtil.abortTransaction();
		}
		finally{
			UtilidadBD.closeConnection(con);
		}
		return forward;
	}
}
