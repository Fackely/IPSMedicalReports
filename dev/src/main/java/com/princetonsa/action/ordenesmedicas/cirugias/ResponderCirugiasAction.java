/*
 * @(#)ResponderCirugiasAction.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_04
 *
 */

package com.princetonsa.action.ordenesmedicas.cirugias;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.Listado;
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadTexto;
import util.UtilidadValidacion;
import util.Utilidades;
import util.historiaClinica.UtilidadesHistoriaClinica;
import util.ordenesMedicas.UtilidadesOrdenesMedicas;

import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.ordenesmedicas.cirugias.ResponderCirugiasForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dto.inventario.DtoAutorizacionEntSubcontratadasCapitacion;
import com.princetonsa.dto.inventario.DtoServiciosAutorizaciones;
import com.princetonsa.dto.ordenes.DtoSolicitud;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.ordenesmedicas.cirugias.ResponderCirugias;
import com.servinte.axioma.orm.AutorizacionesEntSubServi;
import com.servinte.axioma.orm.Convenios;
import com.servinte.axioma.persistencia.UtilidadTransaccion;
import com.servinte.axioma.servicio.fabrica.ManejoPacienteServicioFabrica;
import com.servinte.axioma.servicio.fabrica.facturacion.FacturacionServicioFabrica;
import com.servinte.axioma.servicio.interfaz.facturacion.convenio.IConvenioServicio;
import com.servinte.axioma.servicio.interfaz.inventario.IAurorizacionesEntSubCapitacionServicio;

/**
 * Clase encargada del control de la funcionalidad de Responder Cirugìas

 * @author <a href="mailto:cperalta@PrincetonSA.com">Carlos Peralta</a>
 * @version 1.0, 03 /Nov/ 2005
 */
public class ResponderCirugiasAction extends Action
{
	
	/**
	 * Objeto para almacenar los logs que aparezcan en el Action 
	*/
	private Logger logger= Logger.getLogger(ResponderCirugiasAction.class);
	
	
	/**
	 * Servicios necesarios para la implementación de la funcionalidad
	 */
	IAurorizacionesEntSubCapitacionServicio aurorizacionesEntSubCapitacionServicio = ManejoPacienteServicioFabrica.crearAurorizacionesEntSubCapitacionServicio(); 
	IConvenioServicio convenioServicio = FacturacionServicioFabrica.crearConvenioServicio();

	
	/** * Contiene la lista de mensajes correspondiente a esta forma */
	MessageResources fuenteMensaje = MessageResources.getMessageResources("com.servinte.mensajes.manejoPaciente.AutorizacionesIngresoEstanciaForm");
	
	
	
	/**
	 * Método excute del Action
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)throws Exception
	{
		Connection con = null;
		try {
			if(form instanceof ResponderCirugiasForm)
			{
				/**Intentamos abrir una conexion con la fuente de datos**/ 
				con = openDBConnection(con); 
				if(con == null)
				{
					request.setAttribute("codigoDescripcionError", "errors.problemasBd");
					return mapping.findForward("paginaError");
				}

				ResponderCirugiasForm responderCirugiasForm=(ResponderCirugiasForm)form;

				HttpSession session = request.getSession();
				PersonaBasica paciente= (PersonaBasica)session.getAttribute("pacienteActivo");
				UsuarioBasico medico= (UsuarioBasico)session.getAttribute("usuarioBasico");

				ResponderCirugias mundo =new ResponderCirugias();
				String estado = responderCirugiasForm.getEstado();
				logger.warn("[ResponderCirugiasAction] estado->"+estado);

				if( !UtilidadValidacion.esProfesionalSalud(medico) )
				{				
					if(UtilidadValidacion.estaMedicoInactivo(con,medico.getCodigoPersona(),medico.getCodigoInstitucionInt()))
						request.setAttribute("codigoDescripcionError", "errors.profesionalSaludInactivo");
					else
						request.setAttribute("codigoDescripcionError", "errors.usuario.noAutorizado");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");				
				}

				if(estado == null)
				{
					logger.warn("Estado no valido dentro del flujo de FormatoImpresionPresupuestoAction (null) ");
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.closeConnection(con);
					return mapping.findForward("paginaError");
				}

				else if(estado.equals("empezarPaciente"))
				{
					/**Validacion del paciente cargado**/
					if(paciente == null|| paciente.getCodigoTipoIdentificacionPersona().equals(""))
					{
						return ComunAction.accionSalirCasoError(mapping, request, con,logger, "paciente null o sin id","errors.paciente.noCargado", true);
					}
					///Validar que el usuario no se autoatienda
					ResultadoBoolean respuesta = UtilidadesHistoriaClinica.esUsuarioAutoatendido(medico, paciente);
					if(respuesta.isTrue())
						return ComunAction.accionSalirCasoError(mapping, request, con, logger, "El paciente no puede ser autoatendido", respuesta.getDescripcion(), true);

					return this.accionEmpezarPaciente(mapping, request,  responderCirugiasForm,con, paciente, mundo);
				}
				else if(estado.equals("empezarMedico"))
				{
					return this.accionEmpezarMedico(mapping, request,  responderCirugiasForm,con, medico, mundo);
				}
				else if(estado.equals("ordenarColumnaPaciente"))
				{
					return this.accionOrdenarColumnaPaciente(mapping, con, responderCirugiasForm);
				}
				else if(estado.equals("ordenarColumnaMedico"))
				{
					return this.accionOrdenarColumnaMedico(mapping, con, responderCirugiasForm);
				}
				else if(estado.equals("responderCirugiaMedico"))
				{
					return this.accionResponderCirugiaMedico(response, responderCirugiasForm, con, paciente, medico);  // ok
				}
				else if(estado.equals("responderCirugiaPaciente"))
				{
					return this.accionResponderCirugiaPaciente(response, responderCirugiasForm, con, medico); // ok
				}
				else if(estado.equals("responderCirugiaProcedimiento"))
				{
					return this.accionResponderCirugiaProcedimiento(mapping, con, responderCirugiasForm,mundo, paciente, request, medico); //ok
				}
				else if(estado.equals("verEstadoAutorizacion"))
				{
					return this.accionEstadosAutorizacion(responderCirugiasForm, con,mapping);
				}

			}
			else
			{
				logger.error("El form no es compatible con el form de Consulta de Facturas");
				request.setAttribute("codigoDescripcionError", "errors.formaTipoInvalido");
				return mapping.findForward("paginaError");
			}	
			return null;
		} catch (Exception e) {
			Log4JManager.error(e);
			return null;
		}
		finally{
			UtilidadBD.closeConnection(con);
		}
	}


	/**
	 * 
	 * @param mapping
	 * @param con
	 * @param responderCirugiasForm
	 * @param mundo
	 * @param paciente
	 * @param request
	 * @param medico
	 * @return
	 * @throws SQLException
	 */
	private ActionForward accionResponderCirugiaProcedimiento(ActionMapping mapping, Connection con, ResponderCirugiasForm responderCirugiasForm, ResponderCirugias mundo, 
			PersonaBasica paciente, HttpServletRequest request, UsuarioBasico medico)throws SQLException 
	{
		responderCirugiasForm.reset();
		int numeroRegistros=Utilidades.convertirAEntero(responderCirugiasForm.getMapaPeticionesPaciente("numRegistros")+"");
		responderCirugiasForm.setConsecutivoOrdenesMedicas(Utilidades.convertirAEntero(request.getParameter("orden")+""));
		responderCirugiasForm.setMapaPeticionesPaciente("fechaCirugia_"+numeroRegistros, request.getParameter("fechaCirugia"));
		responderCirugiasForm.setMapaPeticionesPaciente("consecutivoOrdenes_"+numeroRegistros, request.getParameter("orden"));
		responderCirugiasForm.setMapaPeticionesPaciente("estadoMedico_"+numeroRegistros, request.getParameter("estadoMedico"));
		responderCirugiasForm.setMapaPeticionesPaciente("solicitante_"+numeroRegistros, request.getParameter("solicitante"));
		responderCirugiasForm.setMapaPeticionesPaciente("especialidad_"+numeroRegistros, request.getParameter("especialidad"));
		responderCirugiasForm.setMapaPeticionesPaciente("codigoPeticion_"+numeroRegistros, request.getParameter("codigoPeticion"));
		responderCirugiasForm.setMapaPeticionesPaciente("numeroSolicitud_"+numeroRegistros, request.getParameter("numeroSolicitud"));
		responderCirugiasForm.setMapaPeticionesPaciente("detPeticion_"+numeroRegistros, responderCirugiasForm.getMapaDetallePeticion());
		responderCirugiasForm.setMapaPeticionesPaciente("tieneOrden_"+numeroRegistros, "SI");
		responderCirugiasForm.setMapaPeticionesPaciente("numRegistros",(numeroRegistros+1));
		
		
    	// FIXME
    	// Anexo 179 - Cambio 1.50
    	responderCirugiasForm.setNumeroSolicitudValidacionCapitacion(request.getParameter("numeroSolicitud")+"");
    	hacerLlamadoValidacionCapitacion(responderCirugiasForm, medico);
    	// La forma solo tiene Advertencias cuando NO tiene Autorización de Ingreso Estancia
    	/*
    	String tieneAutoIE ="&tieneAutoIE=true";
    	if(!Utilidades.isEmpty(responderCirugiasForm.getListaAdvertencias())){
    		tieneAutoIE ="&tieneAutoIE=false";
    	}
    	*/
    	//------------------------------------
    	
    	
		return mapping.findForward("peticionesPaciente");
	}



	/**
	 * Acción de empezar con el flujo de las peticiones de un  Paciente
	 * @param mapping
	 * @param request
	 * @param responderCirugiasForm
	 * @param con
	 * @param paciente
	 * @param mundo
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "deprecation" })
	private ActionForward accionEmpezarPaciente(ActionMapping mapping, HttpServletRequest request,  ResponderCirugiasForm responderCirugiasForm, Connection con, PersonaBasica paciente, ResponderCirugias mundo) throws Exception
	{
		/*************************/
		/** Se verifica que el ingreso no sea realizado atraves de entidades subcontratadas*/
		if (paciente.esIngresoEntidadSubcontratada())
		{
			request.setAttribute("codigoDescripcionError", "error.ingresoEntidadSubContratada");
			return mapping.findForward("paginaError");
		}
		
		
		//***********************************VALIDACIONES GENERALES****************************************************************************
		if(!UtilidadesHistoriaClinica.obtenerEstadoIngreso(con, paciente.getCodigoIngreso()).getCodigo().trim().equals(ConstantesIntegridadDominio.acronimoEstadoAbierto))
		{
			return ComunAction.accionSalirCasoError(mapping, request, con, logger, "errors.ingresoEstadoDiferente", "errors.ingresoEstadoDiferenteAbierto", true);
		}
		if(UtilidadValidacion.esCuentaValida(con, paciente.getCodigoCuenta())<1)
		{
			return ComunAction.accionSalirCasoError(mapping, request, con, logger, "errors.paciente.cuentaNoValida", "errors.paciente.cuentaNoValida", true);
		}
		
		if(paciente.getCodigoUltimaViaIngreso()==ConstantesBD.codigoViaIngresoHospitalizacion || paciente.getCodigoUltimaViaIngreso()==ConstantesBD.codigoViaIngresoUrgencias)
		{
			/**Validamos que el paciente tenga la Valoracion Inicial**/
			if(!UtilidadValidacion.tieneValoraciones(con, paciente.getCodigoCuenta()))
			{
				
				return ComunAction.accionSalirCasoError(mapping,request,con,logger, "paciente sin valoracion inicial","error.salasCirugia.noTieneValoracion", true);
			}
		}
		//*****************************************************************************************************************************************
		
		responderCirugiasForm.reset();
		responderCirugiasForm.setMapaPeticionesPaciente(mundo.cargarPeticionesPaciente(con, paciente.getCodigoPersona(), Integer.parseInt(paciente.getTipoInstitucion()), paciente.getCodigoIngreso(),responderCirugiasForm.getConsecutivoOrdenesMedicas()));
		int cantidadRegistros=Integer.parseInt(responderCirugiasForm.getMapaPeticionesPaciente("numRegistros")+"");
		if(cantidadRegistros>0)
		{
			for(int i=0; i<cantidadRegistros; i++)
			{
				if(Utilidades.tieneSolicitudPeticionQxStr(con, Integer.parseInt(responderCirugiasForm.getMapaPeticionesPaciente("codigoPeticion_"+i)+"")))
				{
					responderCirugiasForm.setMapaPeticionesPaciente("tieneOrden_"+i, "SI");
				}
				else
				{
					responderCirugiasForm.setMapaPeticionesPaciente("tieneOrden_"+i, "NO");
				}
			}
		}
		if(cantidadRegistros<=0)
		{
			UtilidadBD.closeConnection(con);
			ArrayList atributosError = new ArrayList();
			atributosError.add(" Paciente sin Peticiones");
			request.setAttribute("codigoDescripcionError", "error.salasCirugia.pacienteSinPeticiones");				
			request.setAttribute("atributosError", atributosError);
			return mapping.findForward("paginaError");	
		}
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("peticionesPaciente");
	}
	
	/**
	 * Accion de empezar por el flujo de Mèdico
	 * @param mapping
	 * @param request
	 * @param responderCirugiasForm
	 * @param con
	 * @param medico
	 * @param mundo
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private ActionForward accionEmpezarMedico(ActionMapping mapping, HttpServletRequest request,  ResponderCirugiasForm responderCirugiasForm, Connection con, UsuarioBasico medico, ResponderCirugias mundo) throws Exception
	{
		responderCirugiasForm.reset();
		responderCirugiasForm.setMapaPeticionesMedico(mundo.cargarPeticionesMedico(con,medico.getLoginUsuario(),medico.getCodigoInstitucionInt(),medico.getCodigoCentroCosto(), medico.getCodigoCentroAtencion()));
		int cantidadRegistros=Integer.parseInt(responderCirugiasForm.getMapaPeticionesMedico("numRegistros")+"");
		if(cantidadRegistros>0)
		{
			for(int i=0; i<cantidadRegistros; i++)
			{
				if(Utilidades.tieneSolicitudPeticionQxStr(con, Integer.parseInt(responderCirugiasForm.getMapaPeticionesMedico("codigoPeticion_"+i)+"")))
				{
					responderCirugiasForm.setMapaPeticionesMedico("tieneOrden_"+i, "SI");
				}
				else
				{
					responderCirugiasForm.setMapaPeticionesMedico("tieneOrden_"+i, "NO");
				}
			}
		}
		if(cantidadRegistros<=0)
		{
			UtilidadBD.closeConnection(con);
			ArrayList atributosError = new ArrayList();
			atributosError.add(" Medico sin Peticiones");
			request.setAttribute("codigoDescripcionError", "error.salasCirugia.medicoSinPeticiones");				
			request.setAttribute("atributosError", atributosError);
			return mapping.findForward("paginaError");	
		}
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("peticionesMedico");
	}
	
 
	/**
	 * Acción que permite el ordenamiento por cualquiera de las columnas
	 * en el listado de peticiones de una paciente
	 * @param con
	 * @param responderCirugiasForm
	 * @param response
	 * @return
	 */
    private ActionForward accionOrdenarColumnaPaciente(ActionMapping mapping, Connection con, ResponderCirugiasForm responderCirugiasForm) 
    {
        String[] indices={"codigoPeticion_", "fechaCirugia_", "consecutivoOrdenes_","estadoMedico_", "numeroSolicitud_", "solicitante_", "especialidad_", "detPeticion_", "tieneOrden_"};
        
        int tmp=Integer.parseInt(responderCirugiasForm.getMapaPeticionesPaciente("numRegistros")+"");
        responderCirugiasForm.setMapaPeticionesPaciente(Listado.ordenarMapa(indices,responderCirugiasForm.getPatronOrdenar(),responderCirugiasForm.getUltimoPatron(),responderCirugiasForm.getMapaPeticionesPaciente(),Integer.parseInt(responderCirugiasForm.getMapaPeticionesPaciente("numRegistros")+"")));
        responderCirugiasForm.setUltimoPatron(responderCirugiasForm.getPatronOrdenar());
        responderCirugiasForm.setMapaPeticionesPaciente("numRegistros",tmp+"");
        UtilidadBD.closeConnection(con);
	        return mapping.findForward("peticionesPaciente");
    }
    
    
    /**
     * Acción que permite el ordenamiento por cualquiera de las columnas
	 * en el listado de peticiones por el flujo de medico
     * @param con
     * @param responderCirugiasForm
     * @param response
     * @return
     */
    private ActionForward accionOrdenarColumnaMedico(ActionMapping mapping, Connection con, ResponderCirugiasForm responderCirugiasForm) 
    {
        String[] indices={"codigoPeticion_", "fechaCirugia_", "consecutivoOrdenes_","estadoMedico_", "numeroSolicitud_", "tipoId_", "paciente_", "codigoPaciente_", "detPeticion_", "tieneOrden_"};
        
        int tmp=Integer.parseInt(responderCirugiasForm.getMapaPeticionesMedico("numRegistros")+"");
        responderCirugiasForm.setMapaPeticionesMedico(Listado.ordenarMapa(indices,responderCirugiasForm.getPatronOrdenar(),responderCirugiasForm.getUltimoPatron(),responderCirugiasForm.getMapaPeticionesMedico(),Integer.parseInt(responderCirugiasForm.getMapaPeticionesMedico("numRegistros")+"")));
        responderCirugiasForm.setUltimoPatron(responderCirugiasForm.getPatronOrdenar());
        responderCirugiasForm.setMapaPeticionesMedico("numRegistros",tmp+"");
        UtilidadBD.closeConnection(con);
	        return mapping.findForward("peticionesMedico");
    }
    
    
    /**
     * Método para responder una cirugia dependiendo del tipo de respuesta que elija por 
     * el flujo del medico
     * @param mapping
     * @param response
     * @param responderCirugiasForm
     * @param con
     * @return
     * @throws Exception
     */
    private ActionForward accionResponderCirugiaMedico(HttpServletResponse response,  ResponderCirugiasForm responderCirugiasForm, Connection con, PersonaBasica paciente, UsuarioBasico medico) throws Exception
	{
    	String respuesta=responderCirugiasForm.getTipoRespuesta().trim();
    	int numeroSolicitud=0;
    	int posicionMapa=responderCirugiasForm.getPosicionMapa();
    	
    	String numeroSolTemp=responderCirugiasForm.getMapaPeticionesMedico("numeroSolicitud_"+posicionMapa)+"";
    	String numeroSolicitudValidacionCapitacion = numeroSolTemp;
    	
    	/**Validamos que tenga un numero de solicitud**/
    	if(!numeroSolTemp.equals(null)&&!numeroSolTemp.equals("null")&&!numeroSolTemp.equals(""))
    	{
    		numeroSolicitud=Integer.parseInt(responderCirugiasForm.getMapaPeticionesMedico("numeroSolicitud_"+posicionMapa)+"");
    		numeroSolicitudValidacionCapitacion = responderCirugiasForm.getMapaPeticionesMedico("numeroSolicitud_"+posicionMapa)+"";
    	}
    	
    	int numeroPeticion=Integer.parseInt(responderCirugiasForm.getMapaPeticionesMedico("codigoPeticion_"+posicionMapa)+"");
    	
    	
    	// FIXME
    	// Anexo 179 - Cambio 1.50
    	responderCirugiasForm.setNumeroSolicitudValidacionCapitacion(numeroSolicitudValidacionCapitacion);
    	hacerLlamadoValidacionCapitacion(responderCirugiasForm, medico);
    	
    	// La forma solo tiene Advertencias cuando NO tiene Autorización de Ingreso Estancia
    	String tieneAutoIE ="&tieneAutoIE=true";
    	if(!Utilidades.isEmpty(responderCirugiasForm.getListaAdvertencias())){
    		tieneAutoIE ="&tieneAutoIE=false";
    	}
    	//------------------------------------
    	
    	
    	
    	
    	if(respuesta.equals("hojaQuirurgica"))
    	{
   		    /**para cargar el paciente que corresponda a la orden**/
		    paciente.setCodigoPersona((Integer.parseInt(responderCirugiasForm.getMapaPeticionesMedico("codigoPaciente_"+posicionMapa)+"")));
		    paciente.cargar(con,(Integer.parseInt(responderCirugiasForm.getMapaPeticionesMedico("codigoPaciente_"+posicionMapa)+"")));
		    paciente.cargarPaciente(con, (Integer.parseInt(responderCirugiasForm.getMapaPeticionesMedico("codigoPaciente_"+posicionMapa)+"")),medico.getCodigoInstitucion(),medico.getCodigoCentroAtencion()+"");
		   
		    //respuesta desde l anueva hoja quirurgica
    		response.sendRedirect("../hojaQuirurgicaDummy/hojaQuirurgica.do?estado=cargarHQxDummy&peticion="+numeroPeticion+"&esDummy="+true+"&funcionalidad=RespCx&esModificable="+true+tieneAutoIE);
		    
    	}
    	else if(respuesta.equals("hojaAnestesia"))
    	{
   		    /**para cargar el paciente que corresponda a la orden**/
		    paciente.setCodigoPersona((Integer.parseInt(responderCirugiasForm.getMapaPeticionesMedico("codigoPaciente_"+posicionMapa)+"")));
		    paciente.cargar(con,(Integer.parseInt(responderCirugiasForm.getMapaPeticionesMedico("codigoPaciente_"+posicionMapa)+"")));			 
		    paciente.cargarPaciente(con, (Integer.parseInt(responderCirugiasForm.getMapaPeticionesMedico("codigoPaciente_"+posicionMapa)+"")),medico.getCodigoInstitucion(),medico.getCodigoCentroAtencion()+"");
		   
    		response.sendRedirect("../ingresarHojaAnestesiaDummy/ingresar.do?estado=empezarDummy&solicitud="+numeroSolicitud+"&peticion="+numeroPeticion+"&esDummy=true&funcionalidad=RespCx&esModificableDummy=true&ocultarEncabezado=false"+tieneAutoIE);
    	}
    	else if(respuesta.equals("notasGenerales"))
    	{
   		    /**para cargar el paciente que corresponda a la orden**/
		    paciente.setCodigoPersona((Integer.parseInt(responderCirugiasForm.getMapaPeticionesMedico("codigoPaciente_"+posicionMapa)+"")));
		    paciente.cargar(con,(Integer.parseInt(responderCirugiasForm.getMapaPeticionesMedico("codigoPaciente_"+posicionMapa)+"")));
		    paciente.cargarPaciente(con, (Integer.parseInt(responderCirugiasForm.getMapaPeticionesMedico("codigoPaciente_"+posicionMapa)+"")),medico.getCodigoInstitucion(),medico.getCodigoCentroAtencion()+"");
	
		    response.sendRedirect("../notasGeneralesEnfermeriaDummy/notasGeneralesEnfermeria.do?estado=empezar&numeroSolicitud="+numeroSolicitud+"&esResumen=0"+tieneAutoIE);
    	}
    	else if(respuesta.equals("notasRecuperacion"))
    	{
   		    /**para cargar el paciente que corresponda a la orden**/
		    paciente.setCodigoPersona((Integer.parseInt(responderCirugiasForm.getMapaPeticionesMedico("codigoPaciente_"+posicionMapa)+"")));
		    paciente.cargar(con,(Integer.parseInt(responderCirugiasForm.getMapaPeticionesMedico("codigoPaciente_"+posicionMapa)+"")));
		    paciente.cargarPaciente(con, (Integer.parseInt(responderCirugiasForm.getMapaPeticionesMedico("codigoPaciente_"+posicionMapa)+"")),medico.getCodigoInstitucion(),medico.getCodigoCentroAtencion()+"");
		    
    		response.sendRedirect("../notasRecuperacionDummy/notasRecuperacion.do?estado=empezar&numeroSolicitud="+numeroSolicitud+"&numeroPeticion="+numeroPeticion+"&esResumen=0"+tieneAutoIE);
    	}  
    	
    	UtilidadBD.closeConnection(con);
    	
		return null;
	}
	
    
    
    /**
     * Método para responder una cirugia dependiendo del tipo de respuesta que elija por el flujo del paciente.
     * 
     * @param mapping
     * @param response
     * @param responderCirugiasForm
     * @param con
     * @param medico
     * @return
     * @throws Exception
     */
    private ActionForward accionResponderCirugiaPaciente(HttpServletResponse response,  ResponderCirugiasForm 
    		responderCirugiasForm, Connection con, UsuarioBasico usuarioMedico) throws Exception
	{
    	String respuesta=responderCirugiasForm.getTipoRespuesta().trim();
    	int posicionMapa=responderCirugiasForm.getPosicionMapa();
    	int numeroSolicitud=0;
    	
    	String numeroSolTemp=responderCirugiasForm.getMapaPeticionesPaciente("numeroSolicitud_"+posicionMapa)+"";
    	
    	String numeroSolicitudValidacionCapitacion = numeroSolTemp;
    	
    	/**Validamos que tenga un numero de solicitud**/
    	if(!numeroSolTemp.equals(null)&&!numeroSolTemp.equals("null")&&!numeroSolTemp.equals(""))
    	{
    		numeroSolicitud=Integer.parseInt(responderCirugiasForm.getMapaPeticionesPaciente("numeroSolicitud_"+posicionMapa)+"");
    		numeroSolicitudValidacionCapitacion = responderCirugiasForm.getMapaPeticionesPaciente("numeroSolicitud_"+posicionMapa)+"";
    	}
    	
    	int numeroPeticion=Integer.parseInt(responderCirugiasForm.getMapaPeticionesPaciente("codigoPeticion_"+posicionMapa)+"");
    	UtilidadBD.closeConnection(con);
    	
    	
    	// FIXME
    	// Anexo 179 - Cambio 1.50
    	if(!UtilidadTexto.isEmpty(numeroSolicitudValidacionCapitacion))
    	{
    		responderCirugiasForm.setNumeroSolicitudValidacionCapitacion(numeroSolicitudValidacionCapitacion);
        	hacerLlamadoValidacionCapitacion(responderCirugiasForm, usuarioMedico);
    	}
    	
    	// La forma solo tiene Advertencias cuando NO tiene Autorización de Ingreso Estancia
    	String tieneAutoIE ="&tieneAutoIE=false";
    	if(Utilidades.isEmpty(responderCirugiasForm.getListaAdvertencias())){
    		tieneAutoIE ="&tieneAutoIE=true";
    	}
    	//------------------------------------
    	
    	
    	if(respuesta.equals("hojaQuirurgica"))
    	{
		    //respuesta desde la nueva hoja quirurgica
    		//Se envia parametro de validacion capitacion a la hoja quirurgica para validar si es capitado y si tiene orden
    		response.sendRedirect("../hojaQuirurgicaDummy/hojaQuirurgica.do?estado=cargarHQxDummy&peticion="+numeroPeticion+"&esDummy="+true+"&funcionalidad=RespCx&esModificable="+true+tieneAutoIE+"&validacionCapitacion="+responderCirugiasForm.isSinAutorizacionEntidadsubcontratada());
    	}
    	else if(respuesta.equals("hojaQuirurgicaSinSolicitud"))
	    {
		    //respuesta desde la nueva hoja quirurgica
	    	response.sendRedirect("../hojaQuirurgicaDummy/hojaQuirurgica.do?estado=listadoPeticiones&peticion="+numeroPeticion+"&esDummy="+true+"&funcionalidad=RespCx&esModificable="+true+tieneAutoIE);
	    }
    		//Se envia parametro de validacion capitacion a la hoja de anestesia para validar si es capitado y si tiene orden
    	else if(respuesta.equals("hojaAnestesia"))
    	{
    		response.sendRedirect("../ingresarHojaAnestesiaDummy/ingresar.do?estado=empezarDummy&solicitud="+numeroSolicitud+"&peticion="+numeroPeticion+"&esDummy=true&funcionalidad=RespCx&esModificableDummy=true&ocultarEncabezado=false"+tieneAutoIE+"&validacionCapitacion="+responderCirugiasForm.isSinAutorizacionEntidadsubcontratada());
    	}
    	else if(respuesta.equals("notasGenerales"))
    	{
    		response.sendRedirect("../notasGeneralesEnfermeriaDummy/notasGeneralesEnfermeria.do?estado=empezar&numeroSolicitud="+numeroSolicitud+"&numeroPeticion="+numeroPeticion+"&esResumen=0"+tieneAutoIE);
    	}
    	else if(respuesta.equals("notasRecuperacion"))
    	{
    		response.sendRedirect("../notasRecuperacionDummy/notasRecuperacion.do?estado=empezar&numeroSolicitud="+numeroSolicitud+"&numeroPeticion="+numeroPeticion+"&esResumen=0"+tieneAutoIE);
    	}
    	
    	
		return null;
	}
	
    
    
	/**
	 * Abrir la conceccion con la Base de Datos
	 * @param con
	 * @return
	 */
	 public Connection openDBConnection(Connection con)
	 {
		 if(con != null)
		 {
			 return con;
		 }
		 try
		 {
			 String tipoBD = System.getProperty("TIPOBD");
			 DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			 con = myFactory.getConnection();
		 }
		 catch(Exception e)
		 {
			 logger.warn("Problemas con la base de datos al abrir la conexion "+e.toString());
			 return null;
		 }
	
		return con;
	}
 
	 
	 
	 /**
	  * 
	  * @param listarSolicitudesForm
	  * @param con
	  * @param mapping
	  * @return
	  */
	 private ActionForward accionEstadosAutorizacion(ResponderCirugiasForm cirugiasForm, Connection con,ActionMapping mapping) 
	 {
		 logger.info("El numero de solicitud es: "+ cirugiasForm.getNumeroSolicitud());
		 cirugiasForm.setEstadoAuto(UtilidadesOrdenesMedicas.obtenerConvenioEstadoSolicitud(cirugiasForm.getNumeroSolicitudInt()));
		 return mapping.findForward("estadosAutorizacion");
	 }
	 
	 
	 
	/**
	 * Hace el llamado a la validacion de capitación. Anexo 178 Cambio 1.50
	 * @param forma
	 * @param usuario
	 */
	 private void hacerLlamadoValidacionCapitacion(ResponderCirugiasForm forma, UsuarioBasico usuario)
	 {
		forma.setSinAutorizacionEntidadsubcontratada(false);
		ArrayList<String> listaAdvertencias = new ArrayList<String>();
		
		listaAdvertencias = validacionCapitacion(forma, usuario);
	
		if(!Utilidades.isEmpty(listaAdvertencias))
		{
			forma.setSinAutorizacionEntidadsubcontratada(true);
		}
		
		forma.setListaAdvertencias(listaAdvertencias);
	 }
	 
	 
	 
	 
	/**
	 * Realiza las validaciones de capitación referentes al cambio número 1.50 del Anexo 179
	 * @param forma
	 * @param usuario
	 * @return ArrayList<String>
	 * @author Cristhian Murillo
	 */
	private ArrayList<String> validacionCapitacion(ResponderCirugiasForm forma, UsuarioBasico usuario)
	{
		ArrayList<String> listaAdvertencias = new ArrayList<String>();
		
		UtilidadTransaccion.getTransaccion().begin();
		String numeroSolicitud = forma.getNumeroSolicitudValidacionCapitacion();
		
		boolean tieneConvenioCapitado 			= false;
		boolean servicioAutorizado 				= false;
					
		DtoSolicitud dtoSolicitud = new DtoSolicitud();
		ArrayList<DtoSolicitud> listasolicitudes = new ArrayList<DtoSolicitud>();
		dtoSolicitud.setNumeroSolicitud(Integer.parseInt(numeroSolicitud));
		dtoSolicitud.setInstitucion(usuario.getCodigoInstitucionInt());
		// Las cirugias en especial no tiene convenio y contrato asociado a la subcuenta sino al cargo
		dtoSolicitud.setSolicitudDeCirugia(true);
		
		/* Si se le envía el número de la solicitud, se supone que solo debe retornar una. Por eso se toma  listasolicitudes.get(0) */
		listasolicitudes.addAll(aurorizacionesEntSubCapitacionServicio.obtenerSolicitudesSubcuenta(dtoSolicitud));
		
		
		if(!Utilidades.isEmpty(listasolicitudes))
		{
			dtoSolicitud = new DtoSolicitud(); dtoSolicitud = listasolicitudes.get(0);
			
			/* Se obtiene el convenio de la solicitud para validar si es capitado */
			Convenios convenios = new Convenios();
			convenios = convenioServicio.findById(dtoSolicitud.getCodigoConvenio());
			
			
			/* Se valida que el convenio sea capitado al igual que sus contratod y que a su ves los contratos esten vigentes */
			if(convenios.getCapitacionSubcontratada() != null)
			{
				if(convenios.getCapitacionSubcontratada().equals(ConstantesBD.acronimoSiChar))
				{
					tieneConvenioCapitado =  true;
				}
				else {
					/* El convenio no maneja capitación */
					tieneConvenioCapitado =  false;
				}
			}
			/* -------------------------------------------------------------------------------------------------------------------- */
			
			
			if(tieneConvenioCapitado)
			{
				/* Se toma la solicitud y se cargan todas las autorizaciones de entidad subcontratada asociadas */
				DtoAutorizacionEntSubcontratadasCapitacion dtoAutorizacionEntSubcontratadasCapitacion = new DtoAutorizacionEntSubcontratadasCapitacion();
				dtoAutorizacionEntSubcontratadasCapitacion.setNumeroOrden(dtoSolicitud.getNumeroSolicitud());
				ArrayList<DtoAutorizacionEntSubcontratadasCapitacion> listaAutorizacionesEntSubPorSolicitud = 
					aurorizacionesEntSubCapitacionServicio.obtenerAutorizacionesPorEntSubPorNumeroSolicitud(dtoAutorizacionEntSubcontratadasCapitacion);
			
				ArrayList<AutorizacionesEntSubServi> listaTodosServiciosAutorizados = new ArrayList<AutorizacionesEntSubServi>();
					
				
				/* Busco las autorizaciones que estén Autorizadas y cargo todos los servicios de estas */
				for (DtoAutorizacionEntSubcontratadasCapitacion autorizacionPorSolicitud : listaAutorizacionesEntSubPorSolicitud) 
				{
					if(autorizacionPorSolicitud.getEstado().equals(ConstantesIntegridadDominio.acronimoAutorizado))
					{
						// Se carga la lista de Lista Servicios por Autorización de entidad Subcontratada
				    	DtoAutorizacionEntSubcontratadasCapitacion dtoAutorizacionEntSubcontratadasCapitacionServi = new  DtoAutorizacionEntSubcontratadasCapitacion();
				    	dtoAutorizacionEntSubcontratadasCapitacionServi.setAutorizacion(autorizacionPorSolicitud.getAutorizacion());
				    	listaTodosServiciosAutorizados.addAll(aurorizacionesEntSubCapitacionServicio.listarAutorizacionesEntSubServiPorAutoEntSub(dtoAutorizacionEntSubcontratadasCapitacionServi));
					}
				}
				//----------------------------------------------------------------------------------------------------------------------------
				
				
				/*  Comparo los servicios a responder contra los autorizados a ver si concuerdan */
				for (DtoServiciosAutorizaciones dtoServiciosAutorizaciones : dtoSolicitud.getListaServicios()) 
				{
					// Cortar el ciclo para mejorar rendimiento
					if(servicioAutorizado){ break; }
					
					for (AutorizacionesEntSubServi autorizacionesEntSubServi : listaTodosServiciosAutorizados) {
						Log4JManager.info(dtoServiciosAutorizaciones.getCodigoServicio()+ "=" + autorizacionesEntSubServi.getServicios().getCodigo());
						if(dtoServiciosAutorizaciones.getCodigoServicio() == autorizacionesEntSubServi.getServicios().getCodigo()){
							servicioAutorizado 	= true;
							break;
						}
					}
				}
				
				
				if(servicioAutorizado){
					/* Si SI tiene asociada una Autorización de Capitación Subcontratada, 
					 * se debe continuar con el flujo actual de la funcionalidad. */
					listaAdvertencias = new ArrayList<String>();
				}
				else{
					/* Si NO tiene asociada una Autorización de Capitación Subcontratada, 
					 * se debe mostrar el siguiente mensaje informativo, permitiendo informarle al  
					 * usuario que la orden que responde no tiene asociada una autorización de capitación subcontratada: */
					String mensajeConcreto = fuenteMensaje.getMessage("AutorizacionesIngresoEstanciaForm.ordenSinAutorizacionIE");
					listaAdvertencias.add(mensajeConcreto);
				}
			}
			else
			{
				/* Si NO maneja Capitación Subcontratada, se debe continuar con el 
				 * flujo actual de la funcionalidad. */
				listaAdvertencias = new ArrayList<String>();
			}
		}
		
		UtilidadTransaccion.getTransaccion().commit();
		
		return listaAdvertencias;
	}

	
}