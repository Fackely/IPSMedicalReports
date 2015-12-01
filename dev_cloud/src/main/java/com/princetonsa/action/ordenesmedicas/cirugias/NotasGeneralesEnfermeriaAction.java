
/*
 * Creado   1/11/2005
 *
 *	Copyright Princeton S.A. &copy;&reg; 2004. Todos los Derechos Reservados.
 *
 * Lenguaje		:Java
 * Compilador	:J2SDK 1.5.0_03
 */
package com.princetonsa.action.ordenesmedicas.cirugias;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.Listado;
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.UtilidadValidacion;
import util.Utilidades;
import util.historiaClinica.UtilidadesHistoriaClinica;

import java.sql.Connection;
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

import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.ordenesmedicas.cirugias.NotasGeneralesEnfermeriaForm;
import com.princetonsa.mundo.Cuenta;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.ordenesmedicas.cirugias.NotasGeneralesEnfermeria;
import com.princetonsa.mundo.ordenesmedicas.cirugias.ResponderCirugias;

/**
* Clase para manejar el workflow de  
 * las notas generales enfermeria 
 *
 * @version 1.0, 1/11/2005
 * @author <a href="mailto:cperalta@PrincetonSA.com">Carlos Peralta</a>
 */
public class NotasGeneralesEnfermeriaAction extends Action 
{
    /**
     * manejador de los logs de la clase
     */
    private Logger logger=Logger.getLogger(NotasGeneralesEnfermeriaAction.class);
    
    
    
    /** * Contiene la lista de mensajes correspondiente a esta forma */
	MessageResources fuenteMensaje = MessageResources.getMessageResources("com.servinte.mensajes.manejoPaciente.AutorizacionesIngresoEstanciaForm");
	
	
    
    
    /**
	 * Método execute del action
	 */
	@SuppressWarnings({ "deprecation", "unchecked" })
	public ActionForward execute(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception
	{

		Connection con = null;		  

		try {
			if(form instanceof NotasGeneralesEnfermeriaForm)
			{
				/**Intentamos abrir una conexion con la fuente de datos**/ 
				con = UtilidadBD.abrirConexion();
				if(con == null)
				{
					request.setAttribute("codigoDescripcionError", "errors.problemasBd");
					return mapping.findForward("paginaError");
				}

				NotasGeneralesEnfermeriaForm formNotasGernerales=(NotasGeneralesEnfermeriaForm)form;
				NotasGeneralesEnfermeria mundo=new NotasGeneralesEnfermeria();

				HttpSession sesion = request.getSession();			
				PersonaBasica paciente= (PersonaBasica)sesion.getAttribute("pacienteActivo");
				UsuarioBasico usuario= (UsuarioBasico)sesion.getAttribute("usuarioBasico");
				String estado=formNotasGernerales.getEstado();
				Cuenta cuenta=new Cuenta();


				logger.warn("[NotasGenerlesEnfermeriaAction] estado=>"+estado);

				if(estado == null)
				{
					logger.warn("Estado no valido dentro del flujo de NotasGeneralesEnfermeriaAction (null) ");
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}	
				else if(estado.equals("empezar") )
				{			    
					return metodoEmpezar(con,request,mapping,paciente,usuario,formNotasGernerales,mundo,cuenta);					
				}
				else if(estado.equals("guardarNota"))
				{
					ActionForward forward=this.accionGuardarNota(mapping,request,formNotasGernerales,con,mundo, usuario, paciente);
					sesion.setAttribute("esResumen", "0");
					return forward;
				}
				else if(estado.equals("guardarNotaEnfermera"))
				{
					ActionForward forward=this.accionGuardarNota(mapping,request,formNotasGernerales,con,mundo, usuario, paciente);
					sesion.setAttribute("esResumen", "1");
					return forward;
				}
				//***********************************************************************
				//Anexo 550.*************************************************************
				else if(estado.equals("mostrarOpciones"))
				{
					UtilidadBD.closeConnection(con); 
					return mapping.findForward("paginaOpciones");
				}
				else if(estado.equals("ordenarColumnaPaciente"))
				{
					return this.accionOrdenarColumnaPaciente(mapping, con, formNotasGernerales);
				}
				else if(estado.equals("ordenarColumnaMedico"))
				{
					return this.accionOrdenarColumnaMedico(mapping, con, formNotasGernerales);
				}
				else if(estado.equals("empezarPorPaciente"))
				{				
					formNotasGernerales.setNumeroSolicitud(
							Integer.parseInt(formNotasGernerales.getMapaPeticionesPaciente("numeroSolicitud_"+formNotasGernerales.getPosicionMapa())+""));
					formNotasGernerales.setNumeroPeticion(
							Integer.parseInt(formNotasGernerales.getMapaPeticionesPaciente("codigoPeticion_"+formNotasGernerales.getPosicionMapa())+""));

					return metodoEmpezar(
							con,
							request,
							mapping,
							paciente,
							usuario,
							formNotasGernerales,
							mundo,
							cuenta);				

				}
				else if(estado.equals("empezarPorMedico"))
				{
					formNotasGernerales.setNumeroSolicitud(
							Integer.parseInt(formNotasGernerales.getMapaPeticionesMedico("numeroSolicitud_"+formNotasGernerales.getPosicionMapa())+""));
					formNotasGernerales.setNumeroPeticion(
							Integer.parseInt(formNotasGernerales.getMapaPeticionesMedico("codigoPeticion_"+formNotasGernerales.getPosicionMapa())+""));

					/**para cargar el paciente que corresponda a la orden**/
					paciente.setCodigoPersona((Integer.parseInt(formNotasGernerales.getMapaPeticionesMedico("codigoPaciente_"+formNotasGernerales.getPosicionMapa())+"")));
					paciente.cargar(con,(Integer.parseInt(formNotasGernerales.getMapaPeticionesMedico("codigoPaciente_"+formNotasGernerales.getPosicionMapa())+"")));
					paciente.cargarPaciente(con, (Integer.parseInt(formNotasGernerales.getMapaPeticionesMedico("codigoPaciente_"+formNotasGernerales.getPosicionMapa())+"")),usuario.getCodigoInstitucion(),usuario.getCodigoCentroAtencion()+"");	    		

					return metodoEmpezar(
							con,
							request,
							mapping,
							paciente,
							usuario,
							formNotasGernerales,
							mundo,
							cuenta);			
				}
				else if(estado.equals("peticionesPaciente"))
				{
					/**Validamos que haya un paciente cargado en session**/
					if(paciente == null|| paciente.getCodigoTipoIdentificacionPersona().equals(""))
					{
						return ComunAction.accionSalirCasoError(mapping, request, con,logger, "paciente null o sin id","errors.paciente.noCargado", true);
					}				

					/**
					 * Cambio por responder cirugia ANEXO-395
					 */
					if(!UtilidadesHistoriaClinica.obtenerEstadoIngreso(con, paciente.getCodigoIngreso()).getCodigo().trim().equals(ConstantesIntegridadDominio.acronimoEstadoAbierto))
					{
						return ComunAction.accionSalirCasoError(mapping, request, con, logger, "errors.ingresoEstadoDiferente", "errors.ingresoEstadoDiferenteAbierto", true);
					}
					if(UtilidadValidacion.esCuentaValida(con, paciente.getCodigoCuenta())<1)
					{
						return ComunAction.accionSalirCasoError(mapping, request, con, logger, "errors.paciente.cuentaNoValida", "errors.paciente.cuentaNoValida", true);
					}
					//Validar que el usuario no se autoatienda
					ResultadoBoolean respuesta = UtilidadesHistoriaClinica.esUsuarioAutoatendido(usuario, paciente);
					if(respuesta.isTrue())
						return ComunAction.accionSalirCasoError(mapping, request, con, logger, "El paciente no puede ser autoatendido", respuesta.getDescripcion(), true);


					formNotasGernerales.reset();
					ResponderCirugias responder = new ResponderCirugias();
					formNotasGernerales.setMapaPeticionesPaciente(
							responder.cargarPeticionesPacienteConPeticion(con, 
									paciente.getCodigoPersona(), 
									Integer.parseInt(paciente.getTipoInstitucion()), 
									paciente.getCodigoCuenta(),
									ConstantesBD.codigoNuncaValido));

					int cantidadRegistros=Integer.parseInt(formNotasGernerales.getMapaPeticionesPaciente("numRegistros")+"");
					if(cantidadRegistros>0)
					{
						for(int i=0; i<cantidadRegistros; i++)
						{
							if(Utilidades.tieneSolicitudPeticionQxStr(con, Integer.parseInt(formNotasGernerales.getMapaPeticionesPaciente("codigoPeticion_"+i)+"")))
							{
								formNotasGernerales.setMapaPeticionesPaciente("tieneOrden_"+i, "SI");
							}
							else
							{
								formNotasGernerales.setMapaPeticionesPaciente("tieneOrden_"+i, "NO");
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
				else if(estado.equals("peticionesMedico"))	    
				{
					formNotasGernerales.reset();
					ResponderCirugias responder = new ResponderCirugias();
					formNotasGernerales.setMapaPeticionesMedico(responder.cargarPeticionesMedico(con,usuario.getLoginUsuario(),usuario.getCodigoInstitucionInt(),usuario.getCodigoCentroCosto(), usuario.getCodigoCentroAtencion()));
					int cantidadRegistros=Integer.parseInt(formNotasGernerales.getMapaPeticionesMedico("numRegistros")+"");

					if(cantidadRegistros>0)
					{
						for(int i=0; i<cantidadRegistros; i++)
						{
							if(Utilidades.tieneSolicitudPeticionQxStr(con, Integer.parseInt(formNotasGernerales.getMapaPeticionesMedico("codigoPeticion_"+i)+"")))
							{
								formNotasGernerales.setMapaPeticionesMedico("tieneOrden_"+i, "SI");
							}
							else
							{
								formNotasGernerales.setMapaPeticionesMedico("tieneOrden_"+i, "NO");
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
				//***********************************************************************
				//***********************************************************************
			}
			else
			{
				logger.error("El form no es compatible con el form de NotasGeneralesEnfermeriaForm");
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
	 * Accion para empezar el flujo de notas
	 * @param mapping
	 * @param request
	 * @param formaNotasGenerales
	 * @param con
	 * @param paciente
	 * @param mundo
	 * @return
	 * @throws Exception
	 */
	private ActionForward accionEmpezar(ActionMapping mapping, HttpServletRequest request, NotasGeneralesEnfermeriaForm formaNotasGenerales, Connection con, NotasGeneralesEnfermeria mundo, PersonaBasica paciente) throws Exception
	{
		formaNotasGenerales.reset();
		
		if(request.getParameter("esResumen") != null)
			formaNotasGenerales.setEsResumen(Utilidades.convertirAEntero(request.getParameter("esResumen")+""));
		
		/**Ponemos la fecha y hora del sistema por defecto en el momento de empezar**/
		formaNotasGenerales.setFechaNota(UtilidadFecha.getFechaActual());
		formaNotasGenerales.setHoraNota(UtilidadFecha.getHoraActual());
		int numeroSolicitud=formaNotasGenerales.getNumeroSolicitud();
		
		formaNotasGenerales.setCodigoCuenta(paciente.getCodigoCuenta());
		formaNotasGenerales.setViaIngreso(paciente.getCodigoUltimaViaIngreso());
		Cuenta cue=new Cuenta();
		cue.cargarCuenta(con, formaNotasGenerales.getCodigoCuenta()+"");
		
		
		formaNotasGenerales.setFechaAperturaCuenta(cue.getDiaApertura()+"/"+cue.getMesApertura()+"/"+cue.getAnioApertura());
		formaNotasGenerales.setHoraApertura(cue.getHoraApertura());
		if(formaNotasGenerales.getViaIngreso()==ConstantesBD.codigoViaIngresoHospitalizacion)
		{
			formaNotasGenerales.setFechaAdmision(Utilidades.obtenerFechaAdmisionHosp(con, paciente.getCodigoCuenta()));
			formaNotasGenerales.setHoraAdmision(Utilidades.obtenerHoraAdmisionHosp(con, paciente.getCodigoCuenta()));
		}
		else if(formaNotasGenerales.getViaIngreso()==ConstantesBD.codigoViaIngresoUrgencias)
		{
			formaNotasGenerales.setFechaAdmision(Utilidades.obtenerFechaAdmisionUrg(con, paciente.getCodigoCuenta()));
			formaNotasGenerales.setHoraAdmision(Utilidades.obtenerHoraAdmisionUrg(con, paciente.getCodigoCuenta()));
		}
		
		formaNotasGenerales.setMapaNotasGeneralesEnfermeria(mundo.cargarNotasPaciente(con,numeroSolicitud));
		UtilidadBD.closeConnection(con); 
		
		
		
		// FIXME
		// Anexo 179 - Cambio 1.50
		String tieneAutoIEstr = request.getParameter("tieneAutoIE");
		if(!UtilidadTexto.isEmpty(tieneAutoIEstr))
		{
			boolean tieneAutoIE = UtilidadTexto.getBoolean(tieneAutoIEstr);
			
			if(tieneAutoIE)
			{
				formaNotasGenerales.setSinAutorizacionEntidadsubcontratada(false);
			}
			else
			{
				formaNotasGenerales.setSinAutorizacionEntidadsubcontratada(true);
				
				String mensajeConcreto = fuenteMensaje.getMessage("AutorizacionesIngresoEstanciaForm.ordenSinAutorizacionIE");
				
				if(Utilidades.isEmpty(formaNotasGenerales.getListaAdvertencias())){
					formaNotasGenerales.setListaAdvertencias(new ArrayList<String>());
				}
				formaNotasGenerales.getListaAdvertencias().add(mensajeConcreto);
			}
		}

		//FIXME aca voy
		return mapping.findForward("paginaNotas");
	}

	
	
	
	
	
	
	//***************************************************************************************
	
	/**
	 * Accion para guardar la nolta que ingresa la enfermera
	 * @param mapping
	 * @param request
	 * @param formaNotasGenerales
	 * @param con
	 * @param mundo
	 * @param enfermera
	 * @return
	 * @throws Exception
	 */
	private ActionForward accionGuardarNota(
			ActionMapping mapping, 
			HttpServletRequest request,  
			NotasGeneralesEnfermeriaForm formaNotasGenerales, 
			Connection con, 
			NotasGeneralesEnfermeria mundo, 
			UsuarioBasico enfermera, 
			PersonaBasica paciente) throws Exception
	{
		Cuenta cuenta= new Cuenta();
		cuenta.cargarCuenta(con,(paciente.getCodigoCuenta()+""));
		int estadoCuenta=Integer.parseInt(cuenta.getCodigoEstadoCuenta());
		if(estadoCuenta!=0)
		{
			return ComunAction.accionSalirCasoError(mapping,request,con,logger, "cuenta no activa","errors.paciente.cuentaNoActiva", true);
		}
		else
		{
			int numeroSolicitud=formaNotasGenerales.getNumeroSolicitud();
			int respuesta=mundo.insertarNota(con, numeroSolicitud,UtilidadFecha.conversionFormatoFechaABD(formaNotasGenerales.getFechaNota()),formaNotasGenerales.getHoraNota(),formaNotasGenerales.getNota(),enfermera.getCodigoPersona(),enfermera.getCodigoInstitucionInt(),UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()),UtilidadFecha.getHoraActual());
			if(respuesta>0)
			{
				formaNotasGenerales.setMapaNotasGeneralesEnfermeria(mundo.cargarNotasPaciente(con,numeroSolicitud));
			}
			else
			{
				return ComunAction.accionSalirCasoError(mapping,request,con,logger, "no se logro guardar la nota","error.salasCirugia.errorGuardandoNotaGeneral", true);
			}
			formaNotasGenerales.setNota("");
			formaNotasGenerales.setFechaNota(UtilidadFecha.getFechaActual());
			formaNotasGenerales.setHoraNota(UtilidadFecha.getHoraActual());
			UtilidadBD.closeConnection(con);
			return mapping.findForward("paginaNotas");
		}
	}
	
	//*****************************************************************************************
	
	/**
	 * Metodo para empezar la funcionalidad  
	 * @param Connection con
	 * @param HttpServletRequest request
	 * @param ActionMapping mapping
	 * @param PersonaBasica paciente
	 * @param UsuarioBasico usuario
	 * @param NotasGeneralesEnfermeriaForm formNotasGernerales
	 * @param NotasGeneralesEnfermeria mundo
	 * @param Cuenta cuenta
	 * */
	@SuppressWarnings("deprecation")
	public ActionForward metodoEmpezar(
			Connection con,
			HttpServletRequest request,
			ActionMapping mapping,
			PersonaBasica paciente,
			UsuarioBasico usuario,
			NotasGeneralesEnfermeriaForm formNotasGernerales,
			NotasGeneralesEnfermeria mundo,
			Cuenta cuenta)
	{
		if(paciente == null|| paciente.getCodigoTipoIdentificacionPersona().equals(""))
		{
			UtilidadBD.closeConnection(con);
			return ComunAction.accionSalirCasoError(mapping, request, con,logger, "paciente null o sin id","errors.paciente.noCargado", true);
		}		

		/**
		 * Cambio por responder cirugia ANEXO-395
		 */
		if(!UtilidadesHistoriaClinica.obtenerEstadoIngreso(con, paciente.getCodigoIngreso()).getCodigo().trim().equals(ConstantesIntegridadDominio.acronimoEstadoAbierto))
		{
			UtilidadBD.closeConnection(con);
			return ComunAction.accionSalirCasoError(mapping, request, con, logger, "errors.ingresoEstadoDiferente", "errors.ingresoEstadoDiferenteAbierto", true);
		}
		if(UtilidadValidacion.esCuentaValida(con, paciente.getCodigoCuenta())<1)
		{
			UtilidadBD.closeConnection(con);
			return ComunAction.accionSalirCasoError(mapping, request, con, logger, "errors.paciente.cuentaNoValida", "errors.paciente.cuentaNoValida", true);
		}
		
		if(formNotasGernerales.getEsResumen()==0)
		{
			/**Validamos que la ocupacion medica sea enfermera**/
			String mensaje = UtilidadValidacion.esEnfermera(usuario); 
			if(!mensaje.equals(""))
			{
				//se verifica si se ha definido la ocupación enfermera o auxiliar de enfermería
				//en parámetros generales
				if(mensaje.equals("errors.noOcupacionEnfermera"))
				{
					UtilidadBD.closeConnection(con);
					return ComunAction.accionSalirCasoError(mapping, request, con,logger, "ocupacion enfermera no definida",mensaje, true);
				}
				else
				{
					UtilidadBD.closeConnection(con);
					return ComunAction.accionSalirCasoError(mapping, request, con,logger, "ocupacion no valida","error.salasCirugia.ocupacionNoValida", true);
				}
			}
			else
			{
				cuenta.cargarCuenta(con,(paciente.getCodigoCuenta()+""));
				if(!cuenta.getCodigoEstadoCuenta().trim().equals(""))
				{
					int estadoCuenta=Integer.parseInt(cuenta.getCodigoEstadoCuenta());
					int indicadorError=0;
					if(estadoCuenta!=0)
					{
						indicadorError=1;
						UtilidadBD.closeConnection(con);
						return ComunAction.accionSalirCasoError(mapping,request,con,logger, "cuenta no activa","errors.paciente.cuentaNoActiva", true);
					}
					if(indicadorError==0)
					{
						try{
							return this.accionEmpezar(mapping,request,formNotasGernerales,con,mundo, paciente);
						}
						catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
		else
		{
			/**Si es RESUMEN sol validamos que sea profesional de la salud**/
			if(!UtilidadValidacion.esProfesionalSalud(usuario))
			{
				UtilidadBD.closeConnection(con);
				return ComunAction.accionSalirCasoError(mapping, request, con, logger, "errors.noProfesionalSalud", "errors.noProfesionalSalud", true);
			}
			else
			{
				try{
					return this.accionEmpezar(mapping,request,formNotasGernerales,con,mundo, paciente);
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("paginaNotas");
	}
	
	//***************************************************************************************
	
	/**
	 * Acción que permite el ordenamiento por cualquiera de las columnas
	 * en el listado de peticiones de una paciente
	 * @param con
	 * @param responderCirugiasForm
	 * @param response
	 * @return
	 */
    private ActionForward accionOrdenarColumnaPaciente(ActionMapping mapping, Connection con, NotasGeneralesEnfermeriaForm forma) 
    {
        String[] indices={"codigoPeticion_", "fechaCirugia_", "consecutivoOrdenes_","estadoMedico_", "numeroSolicitud_", "solicitante_", "especialidad_", "detPeticion_", "tieneOrden_"};
        
        int tmp=Integer.parseInt(forma.getMapaPeticionesPaciente("numRegistros")+"");
        forma.setMapaPeticionesPaciente(Listado.ordenarMapa(indices,forma.getPatronOrdenar(),forma.getUltimoPatron(),forma.getMapaPeticionesPaciente(),Integer.parseInt(forma.getMapaPeticionesPaciente("numRegistros")+"")));
        forma.setUltimoPatron(forma.getPatronOrdenar());
        forma.setMapaPeticionesPaciente("numRegistros",tmp+"");
        UtilidadBD.closeConnection(con);

        return mapping.findForward("peticionesPaciente");
    }
    
    //***************************************************************************************
    
    /**
     * Acción que permite el ordenamiento por cualquiera de las columnas
	 * en el listado de peticiones por el flujo de medico
     * @param con
     * @param responderCirugiasForm
     * @param response
     * @return
     */
    private ActionForward accionOrdenarColumnaMedico(ActionMapping mapping, Connection con, NotasGeneralesEnfermeriaForm forma) 
    {
        String[] indices={"codigoPeticion_", "fechaCirugia_", "consecutivoOrdenes_","estadoMedico_", "numeroSolicitud_", "tipoId_", "paciente_", "codigoPaciente_", "detPeticion_", "tieneOrden_"};
        
        int tmp=Integer.parseInt(forma.getMapaPeticionesMedico("numRegistros")+"");
        forma.setMapaPeticionesMedico(Listado.ordenarMapa(indices,forma.getPatronOrdenar(),forma.getUltimoPatron(),forma.getMapaPeticionesMedico(),Integer.parseInt(forma.getMapaPeticionesMedico("numRegistros")+"")));
        forma.setUltimoPatron(forma.getPatronOrdenar());
        forma.setMapaPeticionesMedico("numRegistros",tmp+"");
        UtilidadBD.closeConnection(con);

        return mapping.findForward("peticionesMedico");
    }
    
    //***************************************************************************************
}