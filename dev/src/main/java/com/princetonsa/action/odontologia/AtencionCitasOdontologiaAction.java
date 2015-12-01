/*
 * Nov 05, 2009 
 */
package com.princetonsa.action.odontologia;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesCamposParametrizables;
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.manejoPaciente.UtilidadesManejoPaciente;

import com.princetonsa.actionform.odontologia.AtencionCitasOdontologiaForm;
import com.princetonsa.dto.historiaClinica.parametrizacion.DtoPlantilla;
import com.princetonsa.dto.odontologia.DtoCitaOdontologica;
import com.princetonsa.dto.odontologia.DtoInicioAtencionCita;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.odontologia.AtencionCitasOdontologia;
import com.servinte.axioma.fwk.exception.BDException;
import com.servinte.axioma.fwk.exception.IPSException;
import com.servinte.axioma.hibernate.HibernateUtil;
import com.servinte.axioma.orm.InicioAtencionCita;
import com.servinte.axioma.servicio.fabrica.ManejoPacienteServicioFabrica;
import com.servinte.axioma.servicio.fabrica.odontologia.OdontologiaServicioFabrica;
import com.servinte.axioma.servicio.interfaz.manejoPaciente.IIngresosServicio;
import com.servinte.axioma.servicio.interfaz.odontologia.agendaOdontologica.ICitaOdontologicaServicio;
import com.servinte.axioma.servicio.interfaz.odontologia.agendaOdontologica.IInicioAtencionCitaServicio;

/**
 * Controlador para el manejo del flujo de atenci&oacute;n de citas odontol&oacute;gicas
 * y llamado a las plantillas de respuestas
 * @author Sebasti&aacute;n G&oacute;mez R.
 */
public class AtencionCitasOdontologiaAction extends Action 
{
	/**
	 * M&eacute;todo herencia del Action para la recepción de todas las peticiones 
	 */
	public ActionForward execute(	ActionMapping mapping,
		 	ActionForm form,
		 	HttpServletRequest request,
		 	HttpServletResponse response) throws Exception
		 	
 	{
		if(form instanceof AtencionCitasOdontologiaForm)
		{
			AtencionCitasOdontologiaForm forma = (AtencionCitasOdontologiaForm)form;	
			UsuarioBasico usuario= (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
			PersonaBasica paciente = (PersonaBasica)request.getSession().getAttribute("pacienteActivo");
			InstitucionBasica ins = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
			
			Log4JManager.info("EL ESTADO DE Atencion Citas Odontologia=====>"+forma.getEstado());
			
			if(forma.getInstitucion() == null){
				
				InstitucionBasica institucion = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
				
				forma.setInstitucion(institucion);
			}
			
			if (forma.getEstado().equals("empezar"))
			{
				return accionEmpezar(forma,usuario,mapping,request, ins);
			}
			else if (forma.getEstado().equals("buscar"))
			{
				return accionBuscar(forma,usuario,mapping);
			}
			else if (forma.getEstado().equals("atender"))
			{
				return accionAtender(forma,usuario,mapping,request, response, paciente);
			}
			else if (forma.getEstado().equals("volverListadoCitas"))
			{
				return mapping.findForward("listado");
			}
			else if (forma.getEstado().equals("abrirPlantilla"))
			{
				return redireccionarPlantilla(forma, response, request, usuario);
			}
			else if(forma.getEstado().equals("iniciarConfirmacion"))
			{
				return accionIniciarConfirmacion(forma,usuario,mapping);
			}
			else if(forma.getEstado().equals("guardarConfirmacion"))
			{
				return accionGuardarConfirmacion(forma,usuario,mapping,request,paciente);
			}
			else if(forma.getEstado().equals("consultaHistoria")){
				return accionConsultarHistoriaClinica(forma,usuario,mapping,request,response);
			}
			else if(forma.getEstado().equals("redireccion")){
				return accionRedireccionar(forma,usuario,mapping,request,response,paciente);
			}
			else if(forma.getEstado().equals("abrirIngresosPaciente")){
				return accionAbrirIngresosPaciente(forma,usuario,mapping,request);
			}
			else if(forma.getEstado().equals("recargar")){
				return accionRecargarHistoriaClinica(forma,usuario,mapping,request,response);
			}
			//*********ESTADOS USADOS PARA EL MANEJO DEL MOTIVO DE NO ATENCION*****************+
			else if (forma.getEstado().equals("abrirPopUpNoAtencion")||forma.getEstado().equals("guardarPopUpNoAtencion"))
			{
				return accionPopUpNoAtencion(forma,mapping,request);
			}
			//***********************************************************************************
		}
		return null;

 	}

	
	/**
	 * Método implementado para redireccionar en el historico dependiendo la accion anterior
	 * @param forma
	 * @param usuario
	 * @param mapping
	 * @param request 
	 * @param paciente 
	 * @return
	 */
	private ActionForward accionRedireccionar(AtencionCitasOdontologiaForm forma, UsuarioBasico usuario,ActionMapping mapping, HttpServletRequest request, HttpServletResponse response, PersonaBasica paciente) throws IPSException 
	{
		
		AtencionCitasOdontologia mundoAtencion = new AtencionCitasOdontologia();
		DtoCitaOdontologica cita=forma.getCitas().get(forma.getPosicion());
		mundoAtencion.cargarPlantillasAtencion(cita,usuario);
		forma.setInfoAtencionCitaOdo(mundoAtencion.getInfoAtencionCitaOdo());
		
		if(forma.getCodigoPlantilla()==ConstantesBD.codigoNuncaValido){
			return accionAtender(forma,usuario,mapping,request, response, paciente);
		}
		for(DtoPlantilla dtoPlan : forma.getInfoAtencionCitaOdo().getPlantillas()){
			if(dtoPlan.getCodigoPK().equals(Integer.toString(forma.getCodigoPlantilla()))){
				if(dtoPlan.getCodigoFuncionalidad()==ConstantesCamposParametrizables.funcParametrizableValoracionConsultaExternaOdontologia)
				{
					try 
					{
						if(forma.getVolver().equals("redireccionResumen"))
							response.sendRedirect("../valoracionOdontologica/valoracion.do?estado=irResumen&cita="+forma.getCitas().get(forma.getPosicion()).getCodigoPk()+"&codigoPlantilla="+dtoPlan.getCodigoPK());
						else
						if(forma.getVolver().equals("redireccionEmpezar"))
							response.sendRedirect("../valoracionOdontologica/valoracion.do?estado=empezar&cita="+forma.getCitas().get(forma.getPosicion()).getCodigoPk()+"&codigoPlantilla="+dtoPlan.getCodigoPK());
					} 
					catch (IOException e) 
					{
						Log4JManager.error("Error tratando de redireccionar la valoracion odontologica: ",e);
					}
				}
				else if(forma.getInfoAtencionCitaOdo().getPlantillas().get(forma.getPosicionPlantilla()).getCodigoFuncionalidad()==ConstantesCamposParametrizables.funcParametrizableEvolucionOdontologica)
				{
					try 
					{
						if(forma.getVolver().equals("redireccionResumen"))
							response.sendRedirect("../evolucionOdontologica/evolucion.do?estado=irResumen&cita="+forma.getCitas().get(forma.getPosicion()).getCodigoPk()+"&codigoPlantilla="+dtoPlan.getCodigoPK());
						else
						if(forma.getVolver().equals("redireccionEmpezar"))
							response.sendRedirect("../evolucionOdontologica/evolucion.do?estado=empezar&cita="+forma.getCitas().get(forma.getPosicion()).getCodigoPk()+"&codigoPlantilla="+dtoPlan.getCodigoPK());
					} 
					catch (IOException e) 
					{
						Log4JManager.error("Error tratando de redireccionar la evolucion odontologica: ",e);
					}
				}
			}
			
		}
		
		return null;
	}
	
	
	/**
	 * M&eacute;todo implementado para consultar la historia clinica por el paciente en session y el ingreso
	 * @param forma
	 * @param usuario
	 * @param mapping
	 * @param request 
	 * @param paciente 
	 * @return
	 */
	private ActionForward accionAbrirIngresosPaciente(AtencionCitasOdontologiaForm forma, UsuarioBasico usuario,ActionMapping mapping, HttpServletRequest request) 
	{
		PersonaBasica paciente = (PersonaBasica)request.getSession().getAttribute("pacienteActivo");
		paciente.setCodigoPersona(forma.getCitas().get(forma.getPosicion()).getCodigoPaciente());
		Connection con = UtilidadBD.abrirConexion();
		UtilidadesManejoPaciente.cargarPaciente(con, usuario, paciente, request);
		UtilidadBD.closeConnection(con);
		
		IIngresosServicio servicioIngresos=ManejoPacienteServicioFabrica.crearIngresosServicio();
	
		String[] listaEstadosIngreso= {   
				ConstantesIntegridadDominio.acronimoEstadoAbierto,
				ConstantesIntegridadDominio.acronimoEstadoCerrado
		};
	
		forma.setListaIngresosPorPaciente(servicioIngresos.obtenerIngresosPacientePorEstado(paciente.getCodigoPersona(), listaEstadosIngreso));
		
		return mapping.findForward("popUpIngresosPaciente");
	}
	
	/**
	 * M&eacute;todo implementado para consultar la historia clinica por el paciente en session y el ingreso
	 * @param forma
	 * @param usuario
	 * @param mapping
	 * @param request 
	 * @param paciente 
	 * @return
	 */
	private ActionForward accionConsultarHistoriaClinica(AtencionCitasOdontologiaForm forma, UsuarioBasico usuario,ActionMapping mapping, HttpServletRequest request,HttpServletResponse response) 
	{
		AtencionCitasOdontologia mundoAtencion = new AtencionCitasOdontologia();
		PersonaBasica paciente = (PersonaBasica)request.getSession().getAttribute("pacienteActivo");
		paciente.setCodigoPersona(forma.getCitas().get(forma.getPosicion()).getCodigoPaciente());
		Connection con = UtilidadBD.abrirConexion();
		UtilidadesManejoPaciente.cargarPaciente(con, usuario, paciente, request);
		UtilidadBD.closeConnection(con);
		
		
		if(UtilidadTexto.isEmpty(forma.getIngresoSeleccionado())){
			forma.getInfoAtencionCitaOdo().setPlantillas(mundoAtencion.consultarPlantillasPorIngresos(paciente.getCodigoPersona(),paciente.getConsecutivoIngreso()));
			if(Utilidades.isEmpty(forma.getInfoAtencionCitaOdo().getPlantillas())){
				ActionErrors errores=null;
				if(Utilidades.isEmpty(forma.getInfoAtencionCitaOdo().getPlantillas())){
					errores= new ActionErrors();
					errores.add("",new ActionMessage("error.historicoOdontologico.noResultadosPlantillas"));
					saveErrors(request, errores);
				}
			}
		}
		else{
			forma.getInfoAtencionCitaOdo().setPlantillas(mundoAtencion.consultarPlantillasPorIngresos(paciente.getCodigoPersona(),forma.getIngresoSeleccionado()));
			try 
			{
				response.sendRedirect("../atencionCitasOdontologia/atencionCitasOdontologia.do?estado=recargar");
			} 
			catch (IOException e) 
			{
				Log4JManager.error("Error tratando de redireccionar la atencion odontologica: ",e);
			}
			
		}
			
		
		
		return mapping.findForward("consultaHistoriaClinica");
	}
	
	
	/**
	 * M&eacute;todo implementado para consultar la historia clinica por el paciente en session y el ingreso
	 * @param forma
	 * @param usuario
	 * @param mapping
	 * @param request 
	 * @param paciente 
	 * @return
	 */
	private ActionForward accionRecargarHistoriaClinica(AtencionCitasOdontologiaForm forma, UsuarioBasico usuario,ActionMapping mapping, HttpServletRequest request,HttpServletResponse response) 
	{
		PersonaBasica paciente = (PersonaBasica)request.getSession().getAttribute("pacienteActivo");
		paciente.setCodigoPersona(forma.getCitas().get(forma.getPosicion()).getCodigoPaciente());
		Connection con = UtilidadBD.abrirConexion();
		UtilidadesManejoPaciente.cargarPaciente(con, usuario, paciente, request);
		UtilidadBD.closeConnection(con);
		AtencionCitasOdontologia mundoAtencion = new AtencionCitasOdontologia();
		forma.getInfoAtencionCitaOdo().setPlantillas(mundoAtencion.consultarPlantillasPorIngresos(paciente.getCodigoPersona(),forma.getIngresoSeleccionado()));
		
		ActionErrors errores=null;
		if(Utilidades.isEmpty(forma.getInfoAtencionCitaOdo().getPlantillas())){
			errores= new ActionErrors();
			errores.add("",new ActionMessage("error.historicoOdontologico.noResultadosPlantillas"));
			saveErrors(request, errores);
		}
		return mapping.findForward("consultaHistoriaClinica");
	}
	
	/**
	 * M&eacute;todo implementado para realizar la confirmación de la cita
	 * @param forma
	 * @param usuario
	 * @param mapping
	 * @param request 
	 * @param paciente 
	 * @return
	 * @throws BDException 
	 */
	private ActionForward accionGuardarConfirmacion(AtencionCitasOdontologiaForm forma, UsuarioBasico usuario,ActionMapping mapping, HttpServletRequest request, PersonaBasica paciente) throws BDException 
	{
		AtencionCitasOdontologia mundoAtencion = new AtencionCitasOdontologia();
		
		//Se realizan las validaciones de la confirmación del usuario
		mundoAtencion.validarUsuarioFinConfirmacion(forma.getCitas().get(forma.getPosicion()), usuario, forma.getInfoAtencionCitaOdo());
		
		
		if(mundoAtencion.getErrores().isEmpty())
		{
			mundoAtencion.confirmarCita(forma.getCitas().get(forma.getPosicion()), usuario, forma.getInfoAtencionCitaOdo(),paciente,true,false, request.getSession().getId() , ConstantesBD.codigoNuncaValido, forma.getInstitucion());
			forma.setFacturasAutomaticas(mundoAtencion.getFacturasAutomaticas());
		}
		else
		{
			forma.setEstado("iniciarConfirmacion");
			saveErrors(request, mundoAtencion.getErrores());
		}
		
		return mapping.findForward("popUpConfirmacion");
	}

	/**
	 * M&eacute;todo implementado para iniciar la confirmaci&oacute;n de la cita
	 * @param forma
	 * @param usuario
	 * @param mapping
	 * @return
	 */
	private ActionForward accionIniciarConfirmacion(AtencionCitasOdontologiaForm forma, UsuarioBasico usuario,ActionMapping mapping) 
	{
		AtencionCitasOdontologia.validarUsuarioInicioConfirmacion(forma.getCitas().get(forma.getPosicion()), usuario, forma.getInfoAtencionCitaOdo());
		return mapping.findForward("popUpConfirmacion");
	}

	/**
	 * M&eacute;todo para el manejo de la atenci&oacute;n de la cita
	 * @param forma
	 * @param usuario
	 * @param mapping
	 * @param request
	 * @param response 
	 * @param paciente 
	 * @return
	 */
	private ActionForward accionAtender(AtencionCitasOdontologiaForm forma,
			UsuarioBasico usuario, ActionMapping mapping,
			HttpServletRequest request, HttpServletResponse response, PersonaBasica paciente) throws IPSException 
	{
		
		int codigoCita=forma.getCitas().get(forma.getPosicion()).getCodigoPk();
		IInicioAtencionCitaServicio servicio = OdontologiaServicioFabrica.crearInicioAtencionCitaServicio();
		InicioAtencionCita iac=servicio.buscarRegistroInicioAtencionCitaOdontoPorID(codigoCita);
		if(iac!=null){
			forma.getFiltroInicioAtencion().setFechaInicioAtencionGuardada(iac.getFechaInicio());
			forma.getFiltroInicioAtencion().setHoraInicioAtencionGuardada(iac.getHoraInicio());
		}
		
		AtencionCitasOdontologia mundoAtencion = new AtencionCitasOdontologia();
		
		DtoCitaOdontologica cita=forma.getCitas().get(forma.getPosicion());
		
		Connection con = UtilidadBD.abrirConexion();
		paciente=new PersonaBasica();
		paciente.setCodigoPersona(cita.getCodigoPaciente());
		UtilidadesManejoPaciente.cargarPaciente(con, usuario, paciente, request);
		mundoAtencion.getPaciente().setCodigo(cita.getCodigoPaciente());
		mundoAtencion.getPaciente().setIdIngreso(new BigDecimal(paciente.getCodigoIngreso()));
		UtilidadBD.closeConnection(con);
		
		forma.setIngresoSeleccionado(null);
		
		mundoAtencion.atenderCita(cita, usuario);
		
		//Dependiendo del estado se gestiona la salida
		if(cita.getEstado().equals(ConstantesIntegridadDominio.acronimoNoAsistio)||cita.getEstado().equals(ConstantesIntegridadDominio.acronimoNoAtencion))
		{
			if(mundoAtencion.getErrores().isEmpty())
			{
				if (cita.getEstado().equals(ConstantesIntegridadDominio.acronimoNoAsistio)) {
					asignarIndicativoCambioEstadoCita(forma);
				}
				
				return accionBuscar(forma, usuario, mapping);
			}
			else
			{
				saveErrors(request, mundoAtencion.getErrores());
				return mapping.findForward("listado");
			}
		}
		else
		{
			con = UtilidadBD.abrirConexion();
			paciente.setCodigoPersona(cita.getCodigoPaciente());
			UtilidadesManejoPaciente.cargarPaciente(con, usuario, paciente, request);
			
			UtilidadBD.closeConnection(con);
			
			forma.setInfoAtencionCitaOdo(mundoAtencion.getInfoAtencionCitaOdo());
			//Si no habían plantillas que aplican para la cita se muestra error
			if(forma.getInfoAtencionCitaOdo().getPlantillas().size()==0)
			{
				ActionErrors errores = new ActionErrors();
				errores.add("",new ActionMessage("errors.noExiste2","plantilla que aplique según la especialidad "+forma.getCitas().get(forma.getPosicion()).getAgendaOdon().getNombreEspecialidadUniAgen()+" y el tipo de cita "+ValoresPorDefecto.getIntegridadDominio(forma.getCitas().get(forma.getPosicion()).getTipo())));
				saveErrors(request, errores);
				return mapping.findForward("listado");
			}
			//Si solo había una plantilla se manda de una vez al formulario de respuesta dependiendo si es valoracion o evolucion
			else if(forma.getInfoAtencionCitaOdo().getPlantillas().size()==1)
			{
				forma.setPosicionPlantilla(0);
				return redireccionarPlantilla(forma, response, request,usuario);
			}
			else 
			{
				return mapping.findForward("confirmacion");
			}
			
		}
		
		
	}
	
	
	/**
	 * M&eacute;todo implementado para redireccionar la plantilla
	 * @param forma
	 * @param response
	 * @param request 
	 * @param usuario 
	 * @return
	 */
	private ActionForward redireccionarPlantilla(AtencionCitasOdontologiaForm forma, HttpServletResponse response, HttpServletRequest request, UsuarioBasico usuario)
	{
		
		//IInicioAtencionCitaServicio servicio = OdontologiaServicioFabrica.crearInicioAtencionCitaServicio();
		if(UtilidadTexto.isEmpty(forma.getPorConfirmar())){	
			
			//Guardar la fecha y hora seleccionada para inicio de atencion de cita odontologica
			IInicioAtencionCitaServicio servicio = OdontologiaServicioFabrica.crearInicioAtencionCitaServicio();
			
			DtoInicioAtencionCita dto=new DtoInicioAtencionCita();
			dto.setFechaInicioAtencion(forma.getFiltroInicioAtencion().getFechaInicioAtencion());
			dto.setFechaModificacion(UtilidadFecha.getFechaActualTipoBD());
			dto.setHoraInicioAtencion(forma.getFiltroInicioAtencion().getHoraInicioAtencion());
			dto.setHoraModificacion(UtilidadFecha.getHoraActual());
			int codigoCita=forma.getCitas().get(forma.getPosicion()).getCodigoPk();
			dto.setCodigoCita(codigoCita);
			dto.setLoginUsuario(usuario.getLoginUsuario());
			dto.setUsuario(usuario);
			dto.setCita(forma.getCitas().get(forma.getPosicion()));
			InicioAtencionCita iac=servicio.buscarRegistroInicioAtencionCitaOdontoPorID(codigoCita);
			if(iac!=null){
				servicio.actualizarInicioAtencionCitaOdonto(dto);
			}else{
				servicio.guardarInicioAtencionCitaOdonto(dto);
			}
		}
		
		
		//Se carga el paciente en sesion
		PersonaBasica paciente = (PersonaBasica)request.getSession().getAttribute("pacienteActivo");
		paciente.setCodigoPersona(forma.getCitas().get(forma.getPosicion()).getCodigoPaciente());
		Connection con = UtilidadBD.abrirConexion();
		UtilidadesManejoPaciente.cargarPaciente(con, usuario, paciente, request);
		UtilidadBD.closeConnection(con);
		
		if(forma.getInfoAtencionCitaOdo().getPlantillas().get(forma.getPosicionPlantilla()).getCodigoFuncionalidad()==ConstantesCamposParametrizables.funcParametrizableValoracionConsultaExternaOdontologia)
		{
			try 
			{
				response.sendRedirect("../valoracionOdontologica/valoracion.do?estado=empezar&cita="+forma.getCitas().get(forma.getPosicion()).getCodigoPk()+"&codigoPlantilla="+forma.getInfoAtencionCitaOdo().getPlantillas().get(forma.getPosicionPlantilla()).getCodigoPK()+"&posicionPlantilla="+forma.getPosicionPlantilla());
			} 
			catch (IOException e) 
			{
				Log4JManager.error("Error tratando de redireccionar la valoracion odontologica: ",e);
			}
		}
		else if(forma.getInfoAtencionCitaOdo().getPlantillas().get(forma.getPosicionPlantilla()).getCodigoFuncionalidad()==ConstantesCamposParametrizables.funcParametrizableEvolucionOdontologica)
		{
			try 
			{
				response.sendRedirect("../evolucionOdontologica/evolucion.do?estado=empezar&cita="+forma.getCitas().get(forma.getPosicion()).getCodigoPk()+"&codigoPlantilla="+forma.getInfoAtencionCitaOdo().getPlantillas().get(forma.getPosicionPlantilla()).getCodigoPK()+"&posicionPlantilla="+forma.getPosicionPlantilla());
			} 
			catch (IOException e) 
			{
				Log4JManager.error("Error tratando de redireccionar la evolucion odontologica: ",e);
			}
		}
		return null;
	}

	/**
	 * M&eacute;todo para el manejo del popUp motivo no atencion
	 * @param forma
	 * @param mapping
	 * @param request
	 * @return
	 */
	private ActionForward accionPopUpNoAtencion(AtencionCitasOdontologiaForm forma, ActionMapping mapping,HttpServletRequest request) 
	{
		ActionErrors errores = new ActionErrors();
		
		if(forma.getEstado().equals("guardarPopUpNoAtencion"))
		{
			if(forma.getCitas().get(forma.getPosicion()).getMotivoNoAtencion().trim().equals(""))
			{
				forma.setEstado("abrirPopUpNoAtencion");
				errores.add("", new ActionMessage("errors.required","El motivo de no atención"));
				
			}
			else 
			{
				if(UtilidadCadena.tieneCaracteresEspecialesGeneral(forma.getCitas().get(forma.getPosicion()).getMotivoNoAtencion()))
				{
					forma.setEstado("abrirPopUpNoAtencion");
					errores.add("", new ActionMessage("errors.caracteresInvalidos","El motivo de no atención"));
				}
				if(forma.getCitas().get(forma.getPosicion()).getMotivoNoAtencion().trim().length()>256)
				{
					forma.setEstado("abrirPopUpNoAtencion");
					errores.add("", new ActionMessage("errors.maxlength","El motivo de no atención","256"));
				}
			}
			
		}
		
		saveErrors(request, errores);
		
		return mapping.findForward("motivoNoAtencion");
	}

	/**
	 * M&eacute;todo para realizar la b&uacute;squeda de las citas a atender después de haber cambiado los parámetros de búsqueda
	 * @param forma
	 * @param usuario
	 * @param mapping
	 * @return
	 */
	private ActionForward accionBuscar(AtencionCitasOdontologiaForm forma,UsuarioBasico usuario, ActionMapping mapping) 
	{
		AtencionCitasOdontologia mundoAtencion = new AtencionCitasOdontologia();
		forma.setCitas(mundoAtencion.consultarCitas(forma.getFecha(), Integer.parseInt(forma.getProfesionalAgendado()),usuario.getCodigoCentroAtencion()));
		
		return mapping.findForward("listado");
	}

	/**
	 * M&eacute;todo para iniciar el flujo de la atención de citas
	 * @param forma
	 * @param usuario
	 * @param mapping
	 * @param request
	 * @param ins 
	 * @return
	 */
	private ActionForward accionEmpezar(AtencionCitasOdontologiaForm forma,UsuarioBasico usuario, ActionMapping mapping, HttpServletRequest request, InstitucionBasica ins) 
	{
		forma.reset();
		AtencionCitasOdontologia mundoAtencion = new AtencionCitasOdontologia();
		mundoAtencion.setUsuario(usuario);
		mundoAtencion.validacionesIngreso();
		int codigoInstitucion = usuario.getCodigoInstitucionInt();
		
		Connection con=HibernateUtil.obtenerConexion();
		forma.setPath(Utilidades.obtenerPathFuncionalidad(con,
		        ConstantesBD.codigoFuncionalidadMenuReportesCitasOdonto));
		
		if(mundoAtencion.getErrores().isEmpty())
		{
			forma.setFecha(UtilidadFecha.getFechaActual());
			forma.setOdontologo(mundoAtencion.isOdontologo());
			forma.setCitas(mundoAtencion.consultarCitas(forma.getFecha(), usuario));
			forma.setProfesionales(mundoAtencion.getArregloProfesionales());
			forma.setProfesionalAgendado(usuario.getCodigoPersona()+"");
			
			String deshabilitaFechaHora = ValoresPorDefecto.getModificarFechaHoraInicioAtencionOdonto(codigoInstitucion).toString();
			
			if (deshabilitaFechaHora.trim().equals(ConstantesBD.acronimoSi)) {
				forma.setDeshabilitaFechaHora(false);
			}else{
				forma.setDeshabilitaFechaHora(true);
			}
			
			return mapping.findForward("listado");
		}
		else
		{
			saveErrors(request, mundoAtencion.getErrores());
			return mapping.findForward("paginaErroresActionErrors");
		}
	}
	
	/**
	 * Este mètodo se encarga de insertar en el registro de citas el 
	 * indicativo de cambio de estado de la cita.
	 *
	 * @param forma
	 * @param mapping
	 * @param request
	 *
	 * @autor Yennifer Guerrero
	 */
	@SuppressWarnings("unused")
	private void asignarIndicativoCambioEstadoCita(
			AtencionCitasOdontologiaForm forma) {
		
		ICitaOdontologicaServicio servicio = OdontologiaServicioFabrica.crearCitaOdontologicaServicio();
		int codigoCita=forma.getCitas().get(forma.getPosicion()).getCodigoPk();
		
		boolean actualizado = servicio.actualizarIndicativoCambioEstadoCita(codigoCita);
		
	}
}
