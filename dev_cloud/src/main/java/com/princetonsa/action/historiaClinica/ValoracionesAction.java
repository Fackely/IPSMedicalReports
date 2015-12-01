/*
 * Mayo 6, 2008
 */
package com.princetonsa.action.historiaClinica;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
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
import util.InfoDatosInt;
import util.Listado;
import util.RespuestaValidacion;
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.UtilidadValidacion;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.facturacion.UtilidadesFacturacion;
import util.historiaClinica.UtilidadesHistoriaClinica;
import util.manejoPaciente.UtilidadesManejoPaciente;

import com.princetonsa.actionform.historiaClinica.ValoracionesForm;
import com.princetonsa.dto.historiaClinica.DtoValoracion;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.atencion.SignoVital;
import com.princetonsa.mundo.historiaClinica.Plantillas;
import com.princetonsa.mundo.historiaClinica.Valoraciones;
import com.princetonsa.mundo.ordenesmedicas.RegistroIncapacidades;
import com.princetonsa.mundo.solicitudes.Solicitud;
import com.princetonsa.mundo.triage.Triage;
import com.servinte.axioma.bl.historiaClinica.facade.HistoriaClinicaFacade;
import com.servinte.axioma.dto.historiaClinica.CurvaCrecimientoPacienteDto;
import com.servinte.axioma.dto.historiaClinica.DatosAlmacenarCurvaCrecimientoDto;
import com.servinte.axioma.dto.historiaClinica.HistoricoImagenPlantillaDto;
import com.servinte.axioma.fwk.exception.IPSException;

import com.servinte.axioma.fwk.exception.IPSException;

import com.sysmedica.util.UtilidadFichas;
import com.princetonsa.dto.historiaClinica.DtoValoracionObservaciones;
/**
 * @author Sebastián Gómez 
 *
 * Clase usada para controlar los procesos de la funcionalidad
 * Valoraciones
 */
public class ValoracionesAction extends Action 
{
	/**
	 * Objeto para manejar los logs de esta clase
	*/
	private Logger logger = Logger.getLogger(ValoracionesAction.class);
	
	/**
	 * Método encargado de el flujo y control de la funcionalidad
	 * 
	 * @see org.apache.struts.action.Action#execute(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse)
	 */
	public ActionForward execute(	ActionMapping mapping,
													ActionForm form,
													HttpServletRequest request,
													HttpServletResponse response ) throws Exception
	{
		Connection con=null;
		try {
		if (response==null); //Para evitar que salga el warning
		if(form instanceof ValoracionesForm)
		{
			
			con = UtilidadBD.abrirConexion();
			
			//SE ABRE CONEXION
			/*try
			{
				con = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection();	
			}
			catch(SQLException e)
			{
				logger.warn("No se pudo abrir la conexión"+e.toString());
			}*/
						
			//OBJETOS A USAR
			ValoracionesForm valoracionForm =(ValoracionesForm)form;
			HttpSession session=request.getSession();		
			UsuarioBasico usuario = (UsuarioBasico)session.getAttribute("usuarioBasico");
			PersonaBasica paciente = (PersonaBasica)session.getAttribute("pacienteActivo");
			
			if(Utilidades.tieneRolFuncionalidad(usuario.getLoginUsuario(), ConstantesBD.permisoImprimirDetalleItemHC)){
				valoracionForm.setImprimirDetalleHC(true); 
			}
			
			String estado=valoracionForm.getEstado(); 
			logger.warn("estado ValoracionesAction-->"+estado);

		try{	
			
			
			if(estado != null)
			{
				if(estado.equals("empezar"))
				{
					if(!validarPacienteEnValoracion(con, mapping, request, paciente, usuario).isEmpty())
					{
						return mapping.findForward("paginaErroresActionErrors");
					}
				}
				Valoraciones.cambiarPacienteEnEvaloracion(con, paciente.getCodigoPersona(), false, usuario.getLoginUsuario(), request.getSession().getId());
			}
			if(estado == null)
			{
				valoracionForm.reset();	
				logger.warn("Estado no valido dentro del flujo de VALORACIONES (null) ");
				request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
				UtilidadBD.cerrarConexion(con);
				return mapping.findForward("paginaError");
			}
			else if (estado.equals("empezar"))
			{
				return accionEmpezar(con, valoracionForm, mapping, paciente,usuario,request);
			}
			else if (estado.equals("insertar"))
			{
				return accionInsertar(con, valoracionForm, mapping, paciente, usuario, request);
			}
			else if (estado.equals("modificar"))
			{
				return accionModificar(con, valoracionForm, mapping, paciente, usuario, request);
			}	
			else if (estado.equals("resumen")||estado.equals("imprimir"))
			{
				return accionResumen(con,valoracionForm,mapping,paciente, usuario, request);
			}
			else if (estado.equals("imprimirIncapacidad"))
			{
				request = accionImprimirIncapacidad(con, request, usuario, paciente.getCodigoIngreso());
				return mapping.findForward("resumenUrgencias");
				
			}
			//Estados Consulta de la valoración de urgencias y las evoluciones del asocio (aplica para hospitalización)-------------------
			else if (estado.equals("historico"))
			{
				return accionHistorico(con,valoracionForm,mapping, paciente, usuario);
			}
			else if (estado.equals("ordenarHistorico"))
			{
				return accionOrdenarHistorico(con,valoracionForm,mapping);
			}
			else if (estado.equals("redireccionHistorico"))
			{
				return accionRedireccionHistorico(con,valoracionForm,mapping,request,response);
			}
			else if (estado.equals("detalleHistorico"))
			{
				return accionDetalleHistorico(con,valoracionForm,mapping,paciente,usuario,request,response);
			}
			//------------------------------------------------------------------------------------------------------------------------------
			else if (estado.equals("registrarReingreso"))
			{
				return accionRegistrarReingreso(con,valoracionForm,mapping,paciente,request);
			}
			//Estado que es llamado desde la hoja obstétrica/antecedentes ginecobstétricos para recargar la seccion Historia Menstrual (si está como componente)
			else if (estado.equals("recargarHistoricoMenstrual"))
			{
				return accionRecargarHistoricoMenstrual(con, valoracionForm, paciente, mapping);
			}
			//Estado para recargar la plantilla cuando se abre popUp
			else if (estado.equals("recargarPlantilla"))
			{
				UtilidadBD.closeConnection(con);
				//Este forward es GLOBAL
				return mapping.findForward("seccionesValoresParametrizables");
			}


					/**
					 * MT 2901: No deja valorar paciente
					 * Valida que cuando se cierre o seleccione otro menu desde
				       el proceso de valoracion se cancele el proceso y se libere el paciente.
					 * @author Diana Ruiz
					 * 
					 */			
					else if(estado.equals("cancelarProcesoValoracionPaciente"))
					{
						Valoraciones.cambiarPacienteEnEvaloracion(con, paciente.getCodigoPersona(), false, usuario.getLoginUsuario(), request.getSession().getId());
						return null;
					} 

			else
			{
				valoracionForm.reset();
				logger.warn("Estado no valido dentro del flujo de ValoracionesAction (null) ");
				request.setAttribute("codigoDescripcionError", "errors.formaTipoInvalido");
				UtilidadBD.cerrarConexion(con);
				return mapping.findForward("paginaError");
			}
		}			
		catch (Exception e) {
			e.printStackTrace();
			logger.error("Error General: "+e);	
	  } 
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

	private ActionErrors validarPacienteEnValoracion(Connection con,
			ActionMapping mapping, HttpServletRequest request, PersonaBasica paciente, UsuarioBasico usuario) {

		ActionErrors errores = new ActionErrors();
		boolean validacionEnValoracion=UtilidadValidacion.validarPacienteEnValoracionDiferenteUsuario(con, paciente.getCodigoPersona(),usuario.getLoginUsuario());
		logger.info("\n\n >>> ValidacionEnValoracion "+validacionEnValoracion);
		if(validacionEnValoracion)
		{
			errores.add("", new ActionMessage("error.valoracion.valoracionEnProceso"));
		}
		if(!errores.isEmpty())
		{
			saveErrors(request, errores);
			UtilidadBD.closeConnection(con);
		}
		return errores;
	}

	private HttpServletRequest accionImprimirIncapacidad(Connection con,
			HttpServletRequest request, UsuarioBasico usuario, int codigoIngreso) {
		RegistroIncapacidades mundoIncapacidades = new RegistroIncapacidades();
		return mundoIncapacidades.imprimirIncapacidad(con, request, usuario, codigoIngreso);
	}

	/**
	 * Método empleado para recargar el histórico menstrual como petición desde la hoja obstétrica
	 * @param con
	 * @param valoracionForm
	 * @param paciente 
	 * @param mapping
	 * @return
	 */
	private ActionForward accionRecargarHistoricoMenstrual(Connection con, ValoracionesForm valoracionForm, PersonaBasica paciente, ActionMapping mapping) 
	{
		//Esta validación solo se hace cuando se tiene un componente de ginecología
		if(valoracionForm.getPlantilla().existeComponente(ConstantesCamposParametrizables.tipoComponenteGinecologia))
		{
		
			//Se recarga el histórico menstrual dependiendo de la valoración que se esté manejando
			if(valoracionForm.getCodigoViaIngreso()==ConstantesBD.codigoViaIngresoUrgencias)
				valoracionForm.getValoracionUrgencias().setHistoriaMenstrual(UtilidadesHistoriaClinica.cargarHistoriaMenstrual(con, paciente.getCodigoPersona()+"", ""));
			else
				valoracionForm.getValoracionHospitalizacion().setHistoriaMenstrual(UtilidadesHistoriaClinica.cargarHistoriaMenstrual(con, paciente.getCodigoPersona()+"", ""));
		}
			
		
		UtilidadBD.closeConnection(con);
		//Se divide el flujo dependiendo de la vía de ingreso
		if(valoracionForm.getCodigoViaIngreso()==ConstantesBD.codigoViaIngresoUrgencias)
			return mapping.findForward("ingresarUrgencias");
		else
			return mapping.findForward("ingresarHospitalizacion");
	}

	/**
	 * Método implementado para registrar el reingreso de una cuenta de urgencias
	 * @param con
	 * @param valoracionForm
	 * @param mapping
	 * @param paciente
	 * @param request
	 * @return
	 */
	private ActionForward accionRegistrarReingreso(Connection con, ValoracionesForm valoracionForm, ActionMapping mapping, PersonaBasica paciente, HttpServletRequest request) 
	{
		
		//Si se aceptó el reingreso se intenta registrarlo
		if(valoracionForm.isAceptarReingreso())
			if(!Utilidades.marcarReingreso(con, paciente.getCodigoIngreso(), paciente.getCodigoPersona()))
			{
				ActionErrors errores = new ActionErrors();
				errores.add("", new ActionMessage("errors.noSeGraboInformacion","DEL REGISTRO DE REINGRESO"));
				saveErrors(request, errores);
			}
		
		//Ya no hay necesidad de validar el reingreso 
		valoracionForm.setValidacionReingreso(false);
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("resumenUrgencias");
	}

	/**
	 * Método implementado para acceder al detalle del histórico (Valoracion y/o Evolucion)
	 * @param con
	 * @param valoracionForm
	 * @param mapping
	 * @param request 
	 * @param usuario 
	 * @param paciente 
	 * @param response 
	 * @return
	 */
	private ActionForward accionDetalleHistorico(Connection con, ValoracionesForm valoracionForm, ActionMapping mapping, PersonaBasica paciente, UsuarioBasico usuario, HttpServletRequest request, HttpServletResponse response) 
	{
		String[] indices = (String[])valoracionForm.getHistorico("INDICES");
		int pos = valoracionForm.getPosHistorico();
		
		//SE toma la información necesaria del mapa
		int viaIngreso = Integer.parseInt(valoracionForm.getHistorico(indices[6]+pos).toString());
		String consecutivo = valoracionForm.getHistorico(indices[0]+pos).toString();
		int codigoTipo = Integer.parseInt(valoracionForm.getHistorico(indices[2]+pos).toString());
		
		try
		{
			//***************SE LLMA EL RESUMEN DE LA EVOLUCION************************************
			if(codigoTipo==ConstantesBD.codigoTipoSolicitudEvolucion)
			{
				UtilidadBD.closeConnection(con);
				String path = "../resumenAtenciones/evolucion.do?estado=resumenListado"+
					"&numeroSolicitud="+consecutivo+
					"&imprimirPagina=resumen_en_pdf"+
					"&ocultarEncabezado=true";
				response.sendRedirect(path);
				return null;
			}
			//***************SE LLAMA EL RESUMEN DE LA VALORACION***********************************************
			else 
			{
				//SE carga de nuevo la plantilla con los valores
				valoracionForm.setPlantillaHistorico(Plantillas.cargarPlantillaXSolicitud(
					con, 
					usuario.getCodigoInstitucionInt(), 
					viaIngreso==ConstantesBD.codigoViaIngresoUrgencias?ConstantesCamposParametrizables.funcParametrizableValoracionUrgencias:ConstantesCamposParametrizables.funcParametrizableValoracionHospitalizacion, 
					ConstantesBD.codigoNuncaValido, //centro de costo 
					ConstantesBD.codigoNuncaValido, // sexo 
					ConstantesBD.codigoNuncaValido,   //especialidad
					Plantillas.obtenerCodigoPlantillaXIngreso(con, paciente.getCodigoIngreso(), paciente.getCodigoPersona(), Integer.parseInt(consecutivo)),
					true, 
					paciente.getCodigoPersona(), 
					paciente.getCodigoIngreso(), 
					Integer.parseInt(consecutivo),
					ConstantesBD.codigoNuncaValido,
					ConstantesBD.codigoNuncaValido,
					false
					));
				
				//SE carga de nuevo la valoracion
				Valoraciones mundoValoracion = new Valoraciones();
				
				//logger.info("viaIngreso => "+viaIngreso);
				//logger.info("consecutivo=> "+consecutivo);
				//Vía de ingreso de URGENCIAS
				if(viaIngreso == ConstantesBD.codigoViaIngresoUrgencias)
				{
					//logger.info("Voy a cargar el historico de la valoración de urgencias");
					mundoValoracion.setNumeroSolicitud(consecutivo);
					mundoValoracion.cargarUrgencias(con,usuario,paciente,paciente.getCodigoPersona(),false);
					valoracionForm.setValoracionUrgenciasHistorico(mundoValoracion.getValoracionUrgencias());
					UtilidadBD.closeConnection(con);
					return mapping.findForward("resumenUrgenciasHistorico");
				}
				//Vía de ingreso de HOSPITALIZACION
				else 
				{
					//logger.info("Voy a cargar el historico de la valoración de hospitalizacion");
					mundoValoracion.setNumeroSolicitud(consecutivo);
					mundoValoracion.cargarHospitalizacion(con,usuario,paciente,false);
					valoracionForm.setValoracionHospitalizacionHistorico(mundoValoracion.getValoracionHospitalizacion());
					UtilidadBD.closeConnection(con);
					return mapping.findForward("resumenHospitalizacionHistorico");
				}
				
			}
			//************************************************************************
		}
		catch(IOException e)
		{
			logger.error("Error en accionDetalleHistorico de ValoracionesAction: "+e);
			ActionErrors errores = new ActionErrors();
			errores.add("",new ActionMessage("errors.problemasDatos"));
			saveErrors(request, errores);
			UtilidadBD.closeConnection(con);
			return mapping.findForward("historico");
		}
		//*************************************************************************************************
	}

	/**
	 * Método implementado para realizar la paginacion del listado del histórico de la valoración
	 * @param con
	 * @param valoracionForm
	 * @param mapping
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward accionRedireccionHistorico(Connection con, ValoracionesForm valoracionForm, ActionMapping mapping, HttpServletRequest request, HttpServletResponse response) 
	{
		try
		{
			
		    UtilidadBD.cerrarConexion(con);
			response.sendRedirect(valoracionForm.getLinkSiguiente());
			return null;
		}
		catch(Exception e)
		{
			logger.error("Error en accionRedireccion de ValoracionesAction: "+e);
			ActionErrors errores = new ActionErrors();
			errores.add("",new ActionMessage("errors.problemasDatos"));
			saveErrors(request, errores);
			UtilidadBD.closeConnection(con);
			return mapping.findForward("historico");
		}
	}

	/**
	 * Método implementado para realizar el ordenamiento del histórico
	 * @param con
	 * @param valoracionForm
	 * @param mapping
	 * @return
	 */
	private ActionForward accionOrdenarHistorico(Connection con, ValoracionesForm valoracionForm, ActionMapping mapping) 
	{
		String[] indices = (String[])valoracionForm.getHistorico("INDICES");
		int numRegistros = valoracionForm.getNumHistorico();
		
		valoracionForm.setHistorico(Listado.ordenarMapa(indices,
				valoracionForm.getIndice(),
				valoracionForm.getUltimoIndice(),
				valoracionForm.getHistorico(),
				numRegistros));
		
		valoracionForm.setNumHistorico(numRegistros);
		valoracionForm.setHistorico("INDICES",indices);
		
		valoracionForm.setUltimoIndice(valoracionForm.getIndice());
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("historico");
	}

	/**
	 * Método implementado para consultar el histórico (valoracion y evoluciones)
	 * @param con
	 * @param valoracionForm
	 * @param mapping
	 * @param paciente 
	 * @param usuario 
	 * @return
	 */
	private ActionForward accionHistorico(Connection con, ValoracionesForm valoracionForm, ActionMapping mapping, PersonaBasica paciente, UsuarioBasico usuario) 
	{
		valoracionForm.setHistorico(Valoraciones.cargarHistorico(con, paciente.getCodigoIngreso()+""));
		
		valoracionForm.setMaxPageItems(ValoresPorDefecto.getMaxPageItemsInt(usuario.getCodigoInstitucionInt()));
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("historico");
	}

	/**
	 * Método implementado para cargar el resumen
	 * @param con
	 * @param valoracionForm
	 * @param mapping
	 * @param paciente
	 * @param usuario
	 * @param request
	 * @return
	 */
	private ActionForward accionResumen(Connection con, ValoracionesForm valoracionForm, ActionMapping mapping, PersonaBasica paciente, UsuarioBasico usuario, HttpServletRequest request) 
	{
		ActionErrors errores = new ActionErrors();
		valoracionForm.setOcultarInfoPaciente(true);
		RegistroIncapacidades mundoIncapacidades = new RegistroIncapacidades();
		if(mundoIncapacidades.consultarIncapacidadPorIngreso(con, paciente.getCodigoIngreso()).getEstado().equals("NADA")){
			valoracionForm.setOcultarImpIncapacidad(ConstantesBD.acronimoSi);
		}
		
		//Se verifica si se debe ocultar el encabezado-----------------------------------------------------------------------
		if(valoracionForm.getEstado().equals("resumen")){
			logger.info("---------------------- ocultarEncabezado --- "+UtilidadTexto.getBoolean(request.getParameter("ocultarEncabezado")));
			valoracionForm.setOcultarEncabezado(UtilidadTexto.getBoolean(request.getParameter("ocultarEncabezado")));
		}	
		
		
		if(valoracionForm.getCodigoViaIngreso()==ConstantesBD.codigoViaIngresoUrgencias)
		{
			//**********************RESUMEN VALORACION URGENCIAS*****************************************************
			
			//Se verifica si viene de historia de atencions-----------------------------------------------------------------------
			valoracionForm.setVieneDeHistoriaAtenciones(UtilidadTexto.getBoolean(request.getParameter("vieneDeHistoriaAtenciones")));
			
			
			
			logger.info("\n\n\n\n\n\n\n\n\n\n\n==================================================================================================");
			logger.info("HA->"+valoracionForm.isVieneDeHistoriaAtenciones()+" E->"+valoracionForm.isVieneDeEpicrisis());
			logger.info("==================================================================================================\n\n\n\n\n\n\n\n\n\n\n\n");
			
			if(valoracionForm.isVieneDeHistoriaAtenciones() ||valoracionForm.isOcultarEncabezado())
			{
				valoracionForm.reset();
				valoracionForm.setEstado(request.getParameter("estado"));
				valoracionForm.setCodigoViaIngreso(Integer.parseInt(request.getParameter("codigoViaIngreso")));
				valoracionForm.setNumeroSolicitudUrgencias(request.getParameter("numeroSolicitudUrgencias"));
				valoracionForm.setCodigoFuncionalidad(ConstantesCamposParametrizables.funcParametrizableValoracionUrgencias);
				valoracionForm.setVieneDeHistoriaAtenciones(true);
				valoracionForm.setVieneDeEpicrisis(false);
			}
			else if(valoracionForm.isVieneDeEpicrisis())
			{
				valoracionForm.reset();
				valoracionForm.setEstado(request.getParameter("estado"));
				valoracionForm.setCodigoViaIngreso(Integer.parseInt(request.getParameter("codigoViaIngreso")));
				valoracionForm.setNumeroSolicitudUrgencias(request.getParameter("numeroSolicitudUrgencias"));
				valoracionForm.setCodigoFuncionalidad(ConstantesCamposParametrizables.funcParametrizableValoracionUrgencias);
				valoracionForm.setVieneDeHistoriaAtenciones(false);
				valoracionForm.setVieneDeEpicrisis(true);
			}
			
			//------------------------------------------------------------------------------------------------------------------
			
			//Si el estado es resumen o viene de Historia de atenciones, se debe cargar el resumen
			if(valoracionForm.getEstado().equals("resumen")||valoracionForm.isVieneDeHistoriaAtenciones()|| valoracionForm.isVieneDeEpicrisis())
				//Se carga el resumen de la valoración
				this.cargarResumenValoracion(
					con, 
					valoracionForm, 
					paciente.getCodigoPersona(), //codigoPaciente
					ConstantesBD.codigoNuncaValido, //codigoIngreso
					valoracionForm.getNumeroSolicitudUrgencias(), 
					usuario,
					paciente);
			
			logger.info("HA->"+valoracionForm.isVieneDeHistoriaAtenciones()+" E->"+valoracionForm.isVieneDeEpicrisis());
			
			
			//Curvas de crecimiento
			int idValoracion = -1;
			
			if(valoracionForm.getValoracionUrgencias().getNumeroSolicitud() != null && !valoracionForm.getValoracionUrgencias().getNumeroSolicitud().equals(""))
				idValoracion = Integer.valueOf(valoracionForm.getValoracionUrgencias().getNumeroSolicitud());
			if(valoracionForm.getValoracionHospitalizacion().getNumeroSolicitud() != null && !valoracionForm.getValoracionHospitalizacion().getNumeroSolicitud().equals(""))
				idValoracion = Integer.valueOf(valoracionForm.getValoracionHospitalizacion().getNumeroSolicitud());
				
			List<HistoricoImagenPlantillaDto> dtohipTemp = new ArrayList<HistoricoImagenPlantillaDto>();
			try {
				dtohipTemp = new HistoriaClinicaFacade().valoracionesPorId(idValoracion);
			} catch (IPSException e) {
				Log4JManager.error(e);
			}
			valoracionForm.setDtoHistoricoImagenPlantilla(dtohipTemp);
			
			valoracionForm.setMostrarDetalles(false);
			if(valoracionForm.getIndiceCurvaSeleccionada()!=null){
				valoracionForm.setCurvaSeleccionada(valoracionForm.getDtoHistoricoImagenPlantilla().get(valoracionForm.getIndiceCurvaSeleccionada()));
				
				Calendar c = Calendar.getInstance();
				c.setTime(valoracionForm.getCurvaSeleccionada().getFecha());
		
				valoracionForm.setEdadCalculada(paciente.calcularEdadEnFechaDetallada(c.get(Calendar.DAY_OF_MONTH), c.get(Calendar.MONTH)+1, c.get(Calendar.YEAR)));
				valoracionForm.setMostrarDetalles(true);
				valoracionForm.setIndiceCurvaSeleccionada(null);
			}
			//Fin curvas de crecimiento
			
			
			if(valoracionForm.isVieneDeEpicrisis())
			{
				logger.info("entra!!!!!!!!!!!!!");
				UtilidadBD.closeConnection(con);
				return mapping.findForward("resumenUrgenciasEpicrisis");
			}
			
			
			//Dependiendo del estado se hacen cosas diferentes
			if(valoracionForm.getEstado().equals("resumen"))
			{
				/**
				 * Alberto Ovalle
				 * mt5749
				 * inicia proceso vistaValoraciones para accionEmpezarModificarUrgencias
				 */
			
				Valoraciones mundoValoracion = new Valoraciones();
				List<DtoValoracion> vistaValoracion =new ArrayList<DtoValoracion>();
				
				try {
					if(valoracionForm.getValoracionUrgencias().getNumeroSolicitud() != null && !valoracionForm.getValoracionUrgencias().getNumeroSolicitud().equals("")) {
						vistaValoracion = mundoValoracion.obtenerValoracionesOrdenada(con,usuario, paciente,valoracionForm.getValoracionUrgencias().getNumeroSolicitud());
						for (DtoValoracion dtoValoracion:vistaValoracion) {
							mundoValoracion.validarFechaObservaciones(vistaValoracion);
							dtoValoracion.setVistaobservaciones(vistaValoracion);
							valoracionForm.setVistaobservaciones(vistaValoracion);
							valoracionForm.setDtoValoracion(dtoValoracion);	
					    }	
					}
				} catch(Exception e) {
					logger.error("Exception: vistaValoracion resumenUrgencias"+e);
					e.printStackTrace();
				}
				/*********************************************/
				UtilidadBD.closeConnection(con);
				return mapping.findForward("resumenUrgencias");
			}
			else
			{
				//Se cargan los datos de la institucion
				valoracionForm.getInstitucion().cargar(con, usuario.getCodigoInstitucionInt());
				valoracionForm.setNombreCentroAtencion(usuario.getCentroAtencion());
				
				valoracionForm.setUsuarioResumen(usuario);
				valoracionForm.setFechaResumen(UtilidadFecha.getFechaActual(con));
				valoracionForm.setHoraResumen(UtilidadFecha.getHoraActual(con));
				UtilidadBD.closeConnection(con);
				
				if(!valoracionForm.isVieneDeHistoriaAtenciones() && 
						!valoracionForm.isVieneDeEpicrisis())
					valoracionForm.setOcultarInfoPaciente(false);
				/**
				 * Alberto Ovalle
				 * mt5749
				 * inicia proceso vistaValoraciones para accionEmpezarModificarUrgencias
				 */
	
				Valoraciones mundoValoracion = new Valoraciones();
				List<DtoValoracion> vistaValoracion =new ArrayList<DtoValoracion>();
				try {
					con = UtilidadBD.abrirConexion();
					if(valoracionForm.getValoracionUrgencias().getNumeroSolicitud() != null && !valoracionForm.getValoracionUrgencias().getNumeroSolicitud().equals("")) {
					vistaValoracion = mundoValoracion.obtenerValoracionesOrdenada(con,usuario, paciente,valoracionForm.getValoracionUrgencias().getNumeroSolicitud());
							for(DtoValoracion dtoValoracion:vistaValoracion) {
							mundoValoracion.validarFechaObservaciones(vistaValoracion);
							dtoValoracion.setVistaobservaciones(vistaValoracion);
							valoracionForm.setVistaobservaciones(vistaValoracion);
							valoracionForm.setDtoValoracion(dtoValoracion);	
					    }
							UtilidadBD.closeConnection(con);
				    }
				} catch(Exception e) {
					logger.error("Exception: al iterar la lista vistaValoracion imprimirUrgencias"+e);
					e.printStackTrace();
				}
				/*********************************************/
				return mapping.findForward("imprimirUrgencias");
			}
			
				
			//************************************************************************************************
			
		}
		else if(valoracionForm.getCodigoViaIngreso()==ConstantesBD.codigoViaIngresoHospitalizacion)
		{
			//*************************RESUMEN VALORACION HOSPITALIZACION*****************************************
			//Se verifica si viene de historia de atencions-------------------------------------------------------------------------
			valoracionForm.setVieneDeHistoriaAtenciones(UtilidadTexto.getBoolean(request.getParameter("vieneDeHistoriaAtenciones")));
			
			if(valoracionForm.isVieneDeHistoriaAtenciones()||valoracionForm.isOcultarEncabezado())
			{
				valoracionForm.reset();
				valoracionForm.setEstado(request.getParameter("estado"));
				valoracionForm.setCodigoViaIngreso(Integer.parseInt(request.getParameter("codigoViaIngreso")));
				valoracionForm.setNumeroSolicitudHospitalizacion(request.getParameter("numeroSolicitudHospitalizacion"));
				valoracionForm.setCodigoFuncionalidad(ConstantesCamposParametrizables.funcParametrizableValoracionHospitalizacion);
				valoracionForm.setVieneDeHistoriaAtenciones(true);
				valoracionForm.setVieneDeEpicrisis(false);
				valoracionForm.setVieneResumenVariasValoraciones(Boolean.parseBoolean(request.getParameter("vieneResumenVariasValoraciones")));
			}
			
			else if(valoracionForm.isVieneDeEpicrisis())
			{
				valoracionForm.reset();
				valoracionForm.setEstado(request.getParameter("estado"));
				valoracionForm.setCodigoViaIngreso(Integer.parseInt(request.getParameter("codigoViaIngreso")));
				valoracionForm.setNumeroSolicitudHospitalizacion(request.getParameter("numeroSolicitudHospitalizacion"));
				valoracionForm.setCodigoFuncionalidad(ConstantesCamposParametrizables.funcParametrizableValoracionHospitalizacion);
				valoracionForm.setVieneDeHistoriaAtenciones(false);
				valoracionForm.setVieneDeEpicrisis(true);
			}
			
			
			
			
			//Curvas de crecimiento
			int idValoracion = -1;
			
			if(valoracionForm.getValoracionHospitalizacion().getNumeroSolicitud() != null && !valoracionForm.getValoracionHospitalizacion().getNumeroSolicitud().equals(""))
				idValoracion = Integer.valueOf(valoracionForm.getValoracionHospitalizacion().getNumeroSolicitud());
				
			List<HistoricoImagenPlantillaDto> dtohipTemp = new ArrayList<HistoricoImagenPlantillaDto>();
			try {
				dtohipTemp = new HistoriaClinicaFacade().valoracionesPorId(idValoracion);
			} catch (IPSException e) {
				Log4JManager.error(e);
			}
			valoracionForm.setDtoHistoricoImagenPlantilla(dtohipTemp);
			
			valoracionForm.setMostrarDetalles(false);
			if(valoracionForm.getIndiceCurvaSeleccionada()!=null){
				valoracionForm.setCurvaSeleccionada(valoracionForm.getDtoHistoricoImagenPlantilla().get(valoracionForm.getIndiceCurvaSeleccionada()));
				
				Calendar c = Calendar.getInstance();
				c.setTime(valoracionForm.getCurvaSeleccionada().getFecha());
		
				valoracionForm.setEdadCalculada(paciente.calcularEdadEnFechaDetallada(c.get(Calendar.DAY_OF_MONTH), c.get(Calendar.MONTH)+1, c.get(Calendar.YEAR)));
				valoracionForm.setMostrarDetalles(true);
				valoracionForm.setIndiceCurvaSeleccionada(null);
			}
			//Fin curvas de crecimiento
			
			
			
			
			//----------------------------------------------------------------------------------------------------------------------
			logger.info("el estado por acà es=> "+valoracionForm.getEstado());
			logger.info("el nùmero solicitud hospitalizacion por acà es=> "+valoracionForm.getNumeroSolicitudHospitalizacion());
			logger.info("la ruta referencia por acà es=> "+valoracionForm.getRutaReferencia());
			//Si el estado es resumen o viene de historia de atenciones se carga el resumen
			if(valoracionForm.getEstado().equals("resumen")||valoracionForm.isVieneDeHistoriaAtenciones()||valoracionForm.isVieneDeEpicrisis())
				//Se carga el resumen de la valoración
				this.cargarResumenValoracion(
					con, 
					valoracionForm, 
					paciente.getCodigoPersona(), //codigoPaciente
					ConstantesBD.codigoNuncaValido, //codigoIngreso
					valoracionForm.getNumeroSolicitudHospitalizacion(), 
					usuario,
					paciente);
			
			
		
				
			if(valoracionForm.isVieneDeEpicrisis())
			{
				UtilidadBD.closeConnection(con);
				return mapping.findForward("resumenHospitalizacionEpicrisis");
			}
			
			//Dependiendo del estado se hacen cosas diferentes
			if(valoracionForm.getEstado().equals("resumen"))
			{
				UtilidadBD.closeConnection(con);
				
				return mapping.findForward("resumenHospitalizacion");
			}
			else
			{
				//Se cargan los datos de la institucion
				valoracionForm.getInstitucion().cargar(con, usuario.getCodigoInstitucionInt());
				valoracionForm.setNombreCentroAtencion(usuario.getCentroAtencion());
				
				valoracionForm.setUsuarioResumen(usuario);
				valoracionForm.setFechaResumen(UtilidadFecha.getFechaActual(con));
				valoracionForm.setHoraResumen(UtilidadFecha.getHoraActual(con));
				UtilidadBD.closeConnection(con);
				
				if(!valoracionForm.isVieneDeHistoriaAtenciones() && 
						!valoracionForm.isVieneDeEpicrisis())
					valoracionForm.setOcultarInfoPaciente(false);
							
				return mapping.findForward("imprimirHospitalizacion");
			}
			
			
			//*********************************************************************************************************
		}
		else
		{
			errores.add("", new ActionMessage("errors.problemasGenericos","al tratar de ingresar información de la valoración"));
			saveErrors(request, errores);
			UtilidadBD.closeConnection(con);
			return mapping.findForward("paginaErroresActionErrors");
		}
	}

	/**
	 * Método implementado para modificar una valoración
	 * @param con
	 * @param valoracionForm
	 * @param mapping
	 * @param paciente
	 * @param usuario
	 * @param request
	 * @return
	 * @throws IPSException 
	 */
	private ActionForward accionModificar(Connection con, ValoracionesForm valoracionForm, ActionMapping mapping, PersonaBasica paciente, UsuarioBasico usuario, HttpServletRequest request) throws IPSException 
	{
		//****************VALIDACION CAMPOS*********************************************************************
		ActionErrors errores = validacionModificar(con,valoracionForm,paciente);
		if(!errores.isEmpty())
		{
			saveErrors(request, errores);
			UtilidadBD.closeConnection(con);
			if(valoracionForm.getCodigoViaIngreso()==ConstantesBD.codigoViaIngresoUrgencias)
				return mapping.findForward("modificarUrgencias");
			else
				return null;
		}
		//********************************************************************************************************
		DtoValoracionObservaciones dtoValoracionObservaciones = new  DtoValoracionObservaciones();
		
		Valoraciones mundoValoracion = new Valoraciones();
		
		if(valoracionForm.getCodigoViaIngreso()==ConstantesBD.codigoViaIngresoUrgencias)
		{
			//**********************MODIFICAR VALORACION URGENCIAS*****************************************************
			valoracionForm.getValoracionUrgencias().setProfesional(usuario); //se asigna como profesional el profesional cargado en sesión
			mundoValoracion.setValoracionUrgencias(valoracionForm.getValoracionUrgencias());
			mundoValoracion.setPuedoModificarConductaValoracion(valoracionForm.isPuedoModificarConductaValoracion());
			UtilidadBD.iniciarTransaccion(con);
			/**
			 * Alberto Ovalle
			 * mt5749
			 * modificar 
			 */
	     	dtoValoracionObservaciones.setPlanDiagnostico(valoracionForm.getPlanDiagnostico());
			dtoValoracionObservaciones.setComentariosGenerales(valoracionForm.getComentariosGenerales());
			dtoValoracionObservaciones.setExpliqueDosDeberesDerechos(valoracionForm.getExpliqueDosDeberesDerechos());
			dtoValoracionObservaciones.setValor(valoracionForm.getPlanDiagnostico());
			dtoValoracionObservaciones.setValor(valoracionForm.getComentariosGenerales());
			dtoValoracionObservaciones.setValor(valoracionForm.getExpliqueDosDeberesDerechos());
			mundoValoracion.modificarUrgencias(con, paciente);
			mundoValoracion.modificarObsevaciones(con, dtoValoracionObservaciones);
			/******************************************************************/
					
			if(mundoValoracion.getErrores().isEmpty())
			{
				UtilidadBD.finalizarTransaccion(con);
				
				//*********************validacion de la referencia**************************************************
				//Se verifica si se debe abrir la referencia
				valoracionForm.setDeboAbrirReferencia(mundoValoracion.isDeboAbrirReferencia());
				//Si se debe abrir la referencia se añade la ruta
				if(valoracionForm.isDeboAbrirReferencia())
					valoracionForm.setRutaReferencia("location.href='../referenciaDummy/referenciaDummy.do?estado=empezarReferencia"+
							"&tipoIdentificacion="+paciente.getCodigoTipoIdentificacionPersona()+"-"+paciente.getTipoIdentificacionPersona(false)+
							"&numeroIdentificacion="+paciente.getNumeroIdentificacionPersona()+
							"&tipoReferencia="+ConstantesIntegridadDominio.acronimoInterna+
							"&filtrarTipoReferencia=true'");
				//***********************************************************************************************
				//*******************se toman datos del resultado de la validación del reingreso*****************
				valoracionForm.setFechaEgresoAnterior(mundoValoracion.getFechaEgresoAnterior());
				valoracionForm.setHoraEgresoAnterior(mundoValoracion.getHoraEgresoAnterior());
				valoracionForm.setDiagnosticoEgresoAnterior(mundoValoracion.getDiagnosticoEgresoAnterior());
				valoracionForm.setValidacionReingreso(mundoValoracion.isValidacionReingreso());
				valoracionForm.setAceptarReingreso(false);
				//**********************************************************************************************
				
				
				valoracionForm.setOcultarEncabezado(false);
				//Se carga el resumen de la valoración
				this.cargarResumenValoracion(
					con, 
					valoracionForm, 
					paciente.getCodigoPersona(), 
					paciente.getCodigoIngreso(),
					valoracionForm.getValoracionUrgencias().getNumeroSolicitud(), 
					usuario,
					paciente);
				
				//Se recarga el paciente en sesión
				UtilidadesManejoPaciente.cargarPaciente(con, usuario, paciente, request);
				/**
				 * Alberto Ovalle
				 * mt5749
				 * inicia proceso vistaValoraciones para accionEmpezarModificarUrgencias
				 */
	
				List<DtoValoracion> vistaValoracion =new ArrayList<DtoValoracion>();
				try {
					if(valoracionForm.getValoracionUrgencias().getNumeroSolicitud() != null && !valoracionForm.getValoracionUrgencias().getNumeroSolicitud().equals("")) {
					vistaValoracion = mundoValoracion.obtenerValoracionesOrdenada(con,usuario, paciente,valoracionForm.getValoracionUrgencias().getNumeroSolicitud());
							for(DtoValoracion dtoValoracion:vistaValoracion) {
							mundoValoracion.validarFechaObservaciones(vistaValoracion);
							dtoValoracion.setVistaobservaciones(vistaValoracion);
							valoracionForm.setVistaobservaciones(vistaValoracion);
							valoracionForm.setDtoValoracion(dtoValoracion);	
					    }
				    }
				} catch(Exception e) {
					logger.error("Exception: al iterar la lista vistaValoracion resumenUrgencias"+e);
					e.printStackTrace();
				}
				/*********************************************/
				UtilidadBD.closeConnection(con);
				
				return mapping.findForward("resumenUrgencias");
			}
			else
			{
				saveErrors(request, mundoValoracion.getErrores());
				UtilidadBD.abortarTransaccion(con);
				UtilidadBD.closeConnection(con);
				/**
				 * Alberto Ovalle
				 * mt5749
				 * inicia proceso vistaValoraciones para accionEmpezarModificarUrgencias
				 */
	
				List<DtoValoracion> vistaValoracion =new ArrayList<DtoValoracion>();
				try {
					if(valoracionForm.getValoracionUrgencias().getNumeroSolicitud() != null && !valoracionForm.getValoracionUrgencias().getNumeroSolicitud().equals("")) {
					vistaValoracion = mundoValoracion.obtenerValoracionesOrdenada(con,usuario, paciente,valoracionForm.getValoracionUrgencias().getNumeroSolicitud());
							for(DtoValoracion dtoValoracion:vistaValoracion) {
							mundoValoracion.validarFechaObservaciones(vistaValoracion);
							dtoValoracion.setVistaobservaciones(vistaValoracion);
							valoracionForm.setVistaobservaciones(vistaValoracion);
							valoracionForm.setDtoValoracion(dtoValoracion);	
					    }
				    }
				} catch(Exception e) {
					logger.error("Exception: al iterar la lista vistaValoracion modificarUrgencias"+e);
					e.printStackTrace();
				}
				/*********************************************/
				return mapping.findForward("modificarUrgencias");
			}
			//************************************************************************************************
			
		}
		else if(valoracionForm.getCodigoViaIngreso()==ConstantesBD.codigoViaIngresoHospitalizacion)
		{
			//*************************+MODIFICAR VALORACION HOSPITALIZACION*****************************************
			mundoValoracion.setValoracionHospitalizacion(valoracionForm.getValoracionHospitalizacion());
			UtilidadBD.iniciarTransaccion(con);
			/**
			 * Alberto Ovalle
			 * mt5749
			 * inicia proceso vistaValoraciones para accionEmpezarModificarHospitalizacion
			 */
			dtoValoracionObservaciones.setPlanDiagnostico(valoracionForm.getPlanDiagnostico());
			dtoValoracionObservaciones.setComentariosGenerales(valoracionForm.getComentariosGenerales());
			dtoValoracionObservaciones.setExpliqueDosDeberesDerechos(valoracionForm.getExpliqueDosDeberesDerechos());
			dtoValoracionObservaciones.setValor(valoracionForm.getPlanDiagnostico());
			dtoValoracionObservaciones.setValor(valoracionForm.getComentariosGenerales());
			dtoValoracionObservaciones.setValor(valoracionForm.getExpliqueDosDeberesDerechos());
			mundoValoracion.modificarHospitalizacion(con, paciente,dtoValoracionObservaciones);
			/******************************************************************/
			
			if(mundoValoracion.getErrores().isEmpty())
			{
				UtilidadBD.finalizarTransaccion(con);
				
				
				valoracionForm.setOcultarEncabezado(false);
				//Se carga el resumen de la valoración
				this.cargarResumenValoracion(
					con, 
					valoracionForm, 
					paciente.getCodigoPersona(), 
					paciente.getCodigoIngreso(),
					valoracionForm.getValoracionHospitalizacion().getNumeroSolicitud(), 
					usuario,
					paciente);
				/*Alberto Ovalle mt 5749 se recarga para visualizar las valoraciones*/
				accionEmpezar(con, valoracionForm, mapping, paciente,usuario,request);
				
				UtilidadBD.closeConnection(con);
				/**
				 * Alberto Ovalle
				 * mt5749
				 * inicia proceso vistaValoraciones para accionEmpezarModificarHospitalizacion
				 */

				List<DtoValoracion> vistaValoracion =new ArrayList<DtoValoracion>();
				try {
					if(valoracionForm.getValoracionHospitalizacion().getNumeroSolicitud()!= null && !valoracionForm.getValoracionHospitalizacion().getNumeroSolicitud().equals("")) {
					vistaValoracion = mundoValoracion.obtenerValoracionesObservacion(con,usuario, paciente,valoracionForm.getValoracionHospitalizacion().getNumeroSolicitud());
							for(DtoValoracion dtoValoracion:vistaValoracion) {
							mundoValoracion.validarFechaObservaciones(vistaValoracion);
							dtoValoracion.setVistaobservaciones(vistaValoracion);
							valoracionForm.setVistaobservaciones(vistaValoracion);
							valoracionForm.setDtoValoracion(dtoValoracion);	
					    }
				    }
				} catch(Exception e) {
					logger.error("Exception: al iterar la lista vistaValoracion modificarHospitalizacion"+e);
					e.printStackTrace();
				}
								
				return mapping.findForward("resumenHospitalizacion");
			}
			else
			{
				saveErrors(request, mundoValoracion.getErrores());
				UtilidadBD.abortarTransaccion(con);
				UtilidadBD.closeConnection(con);
				/**
				 * Alberto Ovalle
				 * mt5749
				 * inicia proceso vistaValoraciones para accionEmpezarModificarHospitalizacion
				 */
	
				List<DtoValoracion> vistaValoracion =new ArrayList<DtoValoracion>();
				try {
					if(valoracionForm.getValoracionHospitalizacion().getNumeroSolicitud() != null && !valoracionForm.getValoracionHospitalizacion().getNumeroSolicitud().equals("")) {
					vistaValoracion = mundoValoracion.obtenerValoracionesObservacion(con,usuario, paciente,valoracionForm.getValoracionHospitalizacion().getNumeroSolicitud());
							for(DtoValoracion dtoValoracion:vistaValoracion) {
							mundoValoracion.validarFechaObservaciones(vistaValoracion);
							dtoValoracion.setVistaobservaciones(vistaValoracion);
							valoracionForm.setVistaobservaciones(vistaValoracion);
							valoracionForm.setDtoValoracion(dtoValoracion);	
					    }
				    }
				} catch(Exception e) {
					logger.error("Exception: al iterar la lista vistaValoracion modificarHospitalizacion"+e);
					e.printStackTrace();
				}
	
				return mapping.findForward("modificarHospitalizacion");
			}
			//*********************************************************************************************************
		}
		else
		{
			errores.add("", new ActionMessage("errors.problemasGenericos","al tratar de ingresar información de la valoración"));
			saveErrors(request, errores);
			UtilidadBD.closeConnection(con);
			return mapping.findForward("paginaErroresActionErrors");
		}
	}

	/**
	 * Metodo implementado para insertar una valoración
	 * @param con
	 * @param valoracionForm
	 * @param mapping
	 * @param paciente
	 * @param usuario
	 * @param request
	 * @return
	 */
	private ActionForward accionInsertar(Connection con, ValoracionesForm valoracionForm, ActionMapping mapping, PersonaBasica paciente, UsuarioBasico usuario, HttpServletRequest request) 
	{
		
		//****************VALIDACION CAMPOS*********************************************************************
		
		ActionErrors errores = validacionIngresar(con,valoracionForm,paciente);
		if(!errores.isEmpty())
		{
			saveErrors(request, errores);
			UtilidadBD.closeConnection(con);
			if(valoracionForm.getCodigoViaIngreso()==ConstantesBD.codigoViaIngresoUrgencias)
				return mapping.findForward("ingresarUrgencias");
			else
				return mapping.findForward("ingresarHospitalizacion");
		}
		//********************************************************************************************************
		
	try{	
		Valoraciones mundoValoracion = new Valoraciones();
		mundoValoracion.setDiagnosticosRelacionados(valoracionForm.getDiagnosticosRelacionados());
		mundoValoracion.setPlantilla(valoracionForm.getPlantilla());
		
		if(valoracionForm.getCodigoViaIngreso()==ConstantesBD.codigoViaIngresoUrgencias)
		{
			//**********************INSERTAR VALORACION URGENCIAS*****************************************************
			mundoValoracion.setValoracionUrgencias(valoracionForm.getValoracionUrgencias());
			UtilidadBD.iniciarTransaccion(con);
			mundoValoracion.insertarUrgencias(con, paciente);
			
			if(mundoValoracion.getErrores().isEmpty())
			{
				UtilidadBD.finalizarTransaccion(con);
				
				//*********************validacion de la referencia**************************************************
				//Se verifica si se debe abrir la referencia
				valoracionForm.setDeboAbrirReferencia(mundoValoracion.isDeboAbrirReferencia());
				//Si se debe abrir la referencia se añade la ruta
				if(valoracionForm.isDeboAbrirReferencia())
					valoracionForm.setRutaReferencia("location.href='../referenciaDummy/referenciaDummy.do?estado=empezarReferencia"+
							"&tipoIdentificacion="+paciente.getCodigoTipoIdentificacionPersona()+"-"+paciente.getTipoIdentificacionPersona(false)+
							"&numeroIdentificacion="+paciente.getNumeroIdentificacionPersona()+
							"&tipoReferencia="+ConstantesIntegridadDominio.acronimoInterna+
							"&filtrarTipoReferencia=true'");
				//***********************************************************************************************
				//*******************se toman datos del resultado de la validación del reingreso*****************
				valoracionForm.setFechaEgresoAnterior(mundoValoracion.getFechaEgresoAnterior());
				valoracionForm.setHoraEgresoAnterior(mundoValoracion.getHoraEgresoAnterior());
				valoracionForm.setDiagnosticoEgresoAnterior(mundoValoracion.getDiagnosticoEgresoAnterior());
				valoracionForm.setValidacionReingreso(mundoValoracion.isValidacionReingreso());
				valoracionForm.setAceptarReingreso(false);
				//**********************************************************************************************
				
				valoracionForm.setOcultarEncabezado(false);
				//Se carga el resumen de la valoración
				this.cargarResumenValoracion(
					con, 
					valoracionForm, 
					paciente.getCodigoPersona(), 
					paciente.getCodigoIngreso(),
					valoracionForm.getValoracionUrgencias().getNumeroSolicitud(), 
					usuario,
					paciente);
				
				//Se recarga el paciente en sesión
				UtilidadesManejoPaciente.cargarPaciente(con, usuario, paciente, request);				
				
				UtilidadBD.closeConnection(con);
				
				
				//Almacenan las Curvas de Crecimiento modificadas del paciente
				
				HistoriaClinicaFacade historiaClinicaFacade = new HistoriaClinicaFacade();
				
				HttpSession session=request.getSession();
				@SuppressWarnings("unchecked")
				List<CurvaCrecimientoPacienteDto> curvasCrecimientoPaciente = (List<CurvaCrecimientoPacienteDto>)session.getAttribute("curvasCrecimientoPaciente");
				
				//Verifica que se halla guardado en el componente de Curvas de Crecimiento
				if(curvasCrecimientoPaciente != null){
					//Recorre la Lista de Curvas de Crecimiento del paciente 
					for(CurvaCrecimientoPacienteDto curvaCrecimiento : curvasCrecimientoPaciente){
						//Verifica que la Curva de Crecimiento halla sido modificada
						if(curvaCrecimiento.isGraficaDiligenciada()){
							
							DatosAlmacenarCurvaCrecimientoDto dtoDatosAlmacenarCurvaCrecimiento = new DatosAlmacenarCurvaCrecimientoDto();
							dtoDatosAlmacenarCurvaCrecimiento.setNumeroSolicitud(Integer.parseInt(valoracionForm.getValoracionUrgencias().getNumeroSolicitud()));
							dtoDatosAlmacenarCurvaCrecimiento.setCodigoCurvaParametrizada(curvaCrecimiento.getDtoCurvaCrecimientoParametrizab().getId());
							dtoDatosAlmacenarCurvaCrecimiento.setImagenBase64(curvaCrecimiento.getImagenBase64());
							dtoDatosAlmacenarCurvaCrecimiento.setCodigoImagenParametrizada(curvaCrecimiento.getDtoCurvaCrecimientoParametrizab().getDtoImagenesParametrizadas().getId());
							dtoDatosAlmacenarCurvaCrecimiento.setCoordenadasPuntos(curvaCrecimiento.getCoordenadasCurvaCrecimiento());
							dtoDatosAlmacenarCurvaCrecimiento.setEsValoracion(true);
							
							try{
								//Se almacena la Curva de Crecimiento asociada a la Valoracion de Urgencias
								historiaClinicaFacade.guardarCurvaCrecimiento(dtoDatosAlmacenarCurvaCrecimiento);
							}
							catch(IPSException ipse){
								Log4JManager.error(ipse);
							}	
						}
					}
					
					//Se elimina la variable en session de curvas de crecimiento
					session.removeAttribute("curvasCrecimientoPaciente");
				}
				
				
				
				//Curvas de crecimiento
				int idValoracion = -1;
				
				if(valoracionForm.getValoracionUrgencias().getNumeroSolicitud() != null && !valoracionForm.getValoracionUrgencias().getNumeroSolicitud().equals(""))
					idValoracion = Integer.valueOf(valoracionForm.getValoracionUrgencias().getNumeroSolicitud());
				if(valoracionForm.getValoracionHospitalizacion().getNumeroSolicitud() != null && !valoracionForm.getValoracionHospitalizacion().getNumeroSolicitud().equals(""))
					idValoracion = Integer.valueOf(valoracionForm.getValoracionHospitalizacion().getNumeroSolicitud());
				
				if(idValoracion != -1)
				{
					List<HistoricoImagenPlantillaDto> dtohipTemp = new ArrayList<HistoricoImagenPlantillaDto>();
					try {
						dtohipTemp = new HistoriaClinicaFacade().valoracionesPorId(idValoracion);
					} catch (IPSException e) {
						Log4JManager.error(e);
					}
					valoracionForm.setDtoHistoricoImagenPlantilla(dtohipTemp);
					
					valoracionForm.setMostrarDetalles(false);
					if(valoracionForm.getIndiceCurvaSeleccionada()!=null){
						valoracionForm.setCurvaSeleccionada(valoracionForm.getDtoHistoricoImagenPlantilla().get(valoracionForm.getIndiceCurvaSeleccionada()));
						
						Calendar c = Calendar.getInstance();
						c.setTime(valoracionForm.getCurvaSeleccionada().getFecha());
				
						valoracionForm.setEdadCalculada(paciente.calcularEdadEnFechaDetallada(c.get(Calendar.DAY_OF_MONTH), c.get(Calendar.MONTH)+1, c.get(Calendar.YEAR)));
						valoracionForm.setMostrarDetalles(true);
						valoracionForm.setIndiceCurvaSeleccionada(null);
					}
				}
				//Fin curvas crecimiento
				
				/**
				 * Alberto Ovalle
				 * mt5749
				 * inicia proceso vistaValoraciones para accionEmpezarModificarUrgencias
				 */
	
				List<DtoValoracion> vistaValoracion =new ArrayList<DtoValoracion>();
				try {
					con = UtilidadBD.abrirConexion();
					if(valoracionForm.getValoracionUrgencias().getNumeroSolicitud() != null && !valoracionForm.getValoracionUrgencias().getNumeroSolicitud().equals("")) {
					vistaValoracion = mundoValoracion.obtenerValoracionesOrdenada(con,usuario, paciente,valoracionForm.getValoracionUrgencias().getNumeroSolicitud());
							for(DtoValoracion dtoValoracion:vistaValoracion) {
							mundoValoracion.validarFechaObservaciones(vistaValoracion);
							dtoValoracion.setVistaobservaciones(vistaValoracion);
							valoracionForm.setVistaobservaciones(vistaValoracion);
							valoracionForm.setDtoValoracion(dtoValoracion);	
							UtilidadBD.closeConnection(con);
							}
							
				    }
				} catch(Exception e) {
					logger.error("Exception: al iterar la lista vistaValoracion resumenUrgencias"+e);
					e.printStackTrace();
				}
				/*********************************************/
				return mapping.findForward("resumenUrgencias");
			}
			else
			{
				saveErrors(request, mundoValoracion.getErrores());
				UtilidadBD.abortarTransaccion(con);
				UtilidadBD.closeConnection(con);
				return mapping.findForward("ingresarUrgencias");
			}
			//************************************************************************************************
			
		}
		else if(valoracionForm.getCodigoViaIngreso()==ConstantesBD.codigoViaIngresoHospitalizacion)
		{
			//*******************INSERTAR VALORACIÓN HOSPITALIZACION*******************************************
			mundoValoracion.setValoracionHospitalizacion(valoracionForm.getValoracionHospitalizacion());
			UtilidadBD.iniciarTransaccion(con);
			mundoValoracion.insertarHospitalizacion(con, paciente,true); //tratando de generar cargo
			
			if(mundoValoracion.getErrores().isEmpty())
			{
				UtilidadBD.finalizarTransaccion(con);
				
				valoracionForm.setOcultarEncabezado(false);
				//Se carga el resumen de la valoración
				this.cargarResumenValoracion(
					con, 
					valoracionForm, 
					paciente.getCodigoPersona(), 
					paciente.getCodigoIngreso(),
					valoracionForm.getValoracionHospitalizacion().getNumeroSolicitud(), 
					usuario,
					paciente);
				
				//Se recarga el paciente en sesión
				UtilidadesManejoPaciente.cargarPaciente(con, usuario, paciente, request);
				
				//Almacenan las Curvas de Crecimiento modificadas del paciente
				
				HistoriaClinicaFacade historiaClinicaFacade = new HistoriaClinicaFacade();
				
				HttpSession session=request.getSession();
				@SuppressWarnings("unchecked")
				List<CurvaCrecimientoPacienteDto> curvasCrecimientoPaciente = (List<CurvaCrecimientoPacienteDto>)session.getAttribute("curvasCrecimientoPaciente");
				
				//Verifica que se halla guardado en el componente de Curvas de Crecimiento
				if(curvasCrecimientoPaciente != null){
					//Recorre la Lista de Curvas de Crecimiento del paciente 
					for(CurvaCrecimientoPacienteDto curvaCrecimiento : curvasCrecimientoPaciente){
						//Verifica que la Curva de Crecimiento halla sido modificada
						if(curvaCrecimiento.isGraficaDiligenciada()){
							
							DatosAlmacenarCurvaCrecimientoDto dtoDatosAlmacenarCurvaCrecimiento = new DatosAlmacenarCurvaCrecimientoDto();
							dtoDatosAlmacenarCurvaCrecimiento.setNumeroSolicitud(Integer.parseInt(valoracionForm.getValoracionHospitalizacion().getNumeroSolicitud()));
							dtoDatosAlmacenarCurvaCrecimiento.setCodigoCurvaParametrizada(curvaCrecimiento.getDtoCurvaCrecimientoParametrizab().getId());
							dtoDatosAlmacenarCurvaCrecimiento.setImagenBase64(curvaCrecimiento.getImagenBase64());
							dtoDatosAlmacenarCurvaCrecimiento.setCodigoImagenParametrizada(curvaCrecimiento.getDtoCurvaCrecimientoParametrizab().getDtoImagenesParametrizadas().getId());
							dtoDatosAlmacenarCurvaCrecimiento.setCoordenadasPuntos(curvaCrecimiento.getCoordenadasCurvaCrecimiento());
							dtoDatosAlmacenarCurvaCrecimiento.setEsValoracion(true);
							
							try{
								//Se almacena la Curva de Crecimiento asociada a la Valoracion de Urgencias
								historiaClinicaFacade.guardarCurvaCrecimiento(dtoDatosAlmacenarCurvaCrecimiento);
							}
							catch(IPSException ipse){
								Log4JManager.error(ipse);
							}	
						}
					}
					
					//Se elimina la variable en session de curvas de crecimiento
					session.removeAttribute("curvasCrecimientoPaciente");
				}
				
				
				
				//Curvas de crecimiento
				int idValoracion = -1;
				
				if(valoracionForm.getValoracionUrgencias().getNumeroSolicitud() != null && !valoracionForm.getValoracionUrgencias().getNumeroSolicitud().equals(""))
					idValoracion = Integer.valueOf(valoracionForm.getValoracionUrgencias().getNumeroSolicitud());
				if(valoracionForm.getValoracionHospitalizacion().getNumeroSolicitud() != null && !valoracionForm.getValoracionHospitalizacion().getNumeroSolicitud().equals(""))
					idValoracion = Integer.valueOf(valoracionForm.getValoracionHospitalizacion().getNumeroSolicitud());
				
				if(idValoracion != -1)
				{
					List<HistoricoImagenPlantillaDto> dtohipTemp = new ArrayList<HistoricoImagenPlantillaDto>();
					try {
						dtohipTemp = new HistoriaClinicaFacade().valoracionesPorId(idValoracion);
					} catch (IPSException e) {
						Log4JManager.error(e);
					}
					valoracionForm.setDtoHistoricoImagenPlantilla(dtohipTemp);
					
					valoracionForm.setMostrarDetalles(false);
					if(valoracionForm.getIndiceCurvaSeleccionada()!=null){
						valoracionForm.setCurvaSeleccionada(valoracionForm.getDtoHistoricoImagenPlantilla().get(valoracionForm.getIndiceCurvaSeleccionada()));
						
						Calendar c = Calendar.getInstance();
						c.setTime(valoracionForm.getCurvaSeleccionada().getFecha());
				
						valoracionForm.setEdadCalculada(paciente.calcularEdadEnFechaDetallada(c.get(Calendar.DAY_OF_MONTH), c.get(Calendar.MONTH)+1, c.get(Calendar.YEAR)));
						valoracionForm.setMostrarDetalles(true);
						valoracionForm.setIndiceCurvaSeleccionada(null);
					}
				}
				//fin curvas
				
				UtilidadBD.closeConnection(con);
								
				return mapping.findForward("resumenHospitalizacion");
			}
			else
			{
				saveErrors(request, mundoValoracion.getErrores());
				UtilidadBD.abortarTransaccion(con);
				UtilidadBD.closeConnection(con);
				return mapping.findForward("ingresarHospitalizacion");
			}
			//**************************************************************************************************
		}
		else
		{
			errores.add("", new ActionMessage("errors.problemasGenericos","al tratar de ingresar información de la valoración"));
			saveErrors(request, errores);
			UtilidadBD.closeConnection(con);
			return mapping.findForward("paginaErroresActionErrors");
		}
		
	}
	catch (Exception e) {
		e.printStackTrace();
		logger.error("Error al registrar la evolución: "+e);	
		}
	return null;
		
	}
	
	/**
	 * Método para cargar el resumen de la valoración
	 * @param con
	 * @param valoracionForm
	 * @param codigoCentroCosto
	 * @param codigoSexo
	 * @param codigoPaciente
	 * @param codigoIngreso
	 * @param numeroSolicitud
	 * @param usuario
	 * @param paciente 
	 */
	private void cargarResumenValoracion(Connection con,ValoracionesForm valoracionForm,int codigoPaciente, int codigoIngreso,String numeroSolicitud, UsuarioBasico usuario, PersonaBasica paciente)
	{
		//SE carga de nuevo la plantilla con los valores
		valoracionForm.setPlantilla(Plantillas.cargarPlantillaXSolicitud(
			con, 
			usuario.getCodigoInstitucionInt(), 
			valoracionForm.getCodigoFuncionalidad(), 
			ConstantesBD.codigoNuncaValido, //centro costo 
			ConstantesBD.codigoNuncaValido, //sexo 
			ConstantesBD.codigoNuncaValido, 
			//Si no hay centro de costo se intenta buscar el consecutivo de la plantilla 
			Plantillas.obtenerCodigoPlantillaXIngreso(con, codigoIngreso, codigoPaciente, Integer.parseInt(numeroSolicitud)),
			true, 
			codigoPaciente, 
			codigoIngreso, 
			Integer.parseInt(numeroSolicitud),
			ConstantesBD.codigoNuncaValido,
			ConstantesBD.codigoNuncaValido,
			false
			));
		//SE carga de nuevo la valoracion
		Valoraciones mundoValoracion = new Valoraciones();
		mundoValoracion.setPlantilla(valoracionForm.getPlantilla());
		
		if(valoracionForm.getCodigoViaIngreso()==ConstantesBD.codigoViaIngresoUrgencias)
		{
			mundoValoracion.setNumeroSolicitud(valoracionForm.getNumeroSolicitudUrgencias());
			mundoValoracion.cargarUrgencias(con,usuario,paciente,codigoPaciente,false);
			valoracionForm.setValoracionUrgencias(mundoValoracion.getValoracionUrgencias());
		}
		else if(valoracionForm.getCodigoViaIngreso()==ConstantesBD.codigoViaIngresoHospitalizacion)
		{
			mundoValoracion.setNumeroSolicitud(valoracionForm.getNumeroSolicitudHospitalizacion());
			mundoValoracion.cargarHospitalizacion(con,usuario,paciente,false);
			valoracionForm.setValoracionHospitalizacion(mundoValoracion.getValoracionHospitalizacion());
		}
		valoracionForm.setDiagnosticosRelacionados(mundoValoracion.getDiagnosticosRelacionados());
	}

	/**
	 * Método implementado para validar la insercion de la valoracion
	 * @param con 
	 * @param valoracionForm
	 * @param paciente 
	 * @return
	 */
	private ActionErrors validacionIngresar(Connection con, ValoracionesForm valoracionForm, PersonaBasica paciente) 
	{
		ActionErrors errores = new ActionErrors();
		
		String fechaSistema = UtilidadFecha.getFechaActual(con);
		String horaSistema = UtilidadFecha.getHoraActual(con);
		String fechaIngreso = paciente.getFechaIngreso();
		String horaIngreso = paciente.getHoraIngreso();
		
		DtoValoracion valoracion = null;
		
		if(valoracionForm.getCodigoViaIngreso()==ConstantesBD.codigoViaIngresoUrgencias)
		{
			if(valoracionForm.getValoracionUrgencias().getEspecialidadResponde().equals(""))
			{
				errores.add("", new ActionMessage("errors.required","La Especialidad del Profesional que Responde"));
			}
			valoracion = valoracionForm.getValoracionUrgencias();
		}
		else
		{
			if(valoracionForm.getValoracionHospitalizacion().getEspecialidadProfResponde().equals(""))
			{
				errores.add("", new ActionMessage("errors.required","La Especialidad del Profesional que Responde"));
			}
			valoracion = valoracionForm.getValoracionHospitalizacion();
		}
		
		boolean fechaValida = false, horaValida = false;
		
		//Fecha Valoracion
		if(valoracion.getFechaValoracion().equals(""))
			errores.add("", new ActionMessage("errors.required","La fecha de valoración"));
		else if(!UtilidadFecha.validarFecha(valoracion.getFechaValoracion()))
			errores.add("", new ActionMessage("errors.formatoFechaInvalido","de valoración"));
		else
			fechaValida = true;
		
		//Hora Valoracion
		if(valoracion.getHoraValoracion().equals(""))
			errores.add("", new ActionMessage("errors.required","La hora de valoración"));
		else if(!UtilidadFecha.validacionHora(valoracion.getHoraValoracion()).puedoSeguir)
			errores.add("", new ActionMessage("errors.formatoHoraInvalido","de valoración"));
		else
			horaValida = true;
		
		if(fechaValida&&horaValida)
		{
			if(!UtilidadFecha.compararFechas(fechaSistema, horaSistema, valoracion.getFechaValoracion(), valoracion.getHoraValoracion()).isTrue())
				errores.add("", new ActionMessage("errors.fechaHoraPosteriorIgualActual","de valoración","del sistema: "+fechaSistema+" - "+horaSistema));
			
			if(!UtilidadFecha.compararFechas(valoracion.getFechaValoracion(), valoracion.getHoraValoracion(), fechaIngreso, horaIngreso).isTrue())
				errores.add("", new ActionMessage("errors.fechaHoraAnteriorIgualActual","de valoración","del ingreso del paciente: "+fechaIngreso+" - "+horaIngreso));
		}
		
		
			
		
		//Estado Embriaguez (SOLO APLICA PARA LA VALORACION DE URGENCIAS)
		if(valoracionForm.getCodigoViaIngreso()==ConstantesBD.codigoViaIngresoUrgencias)
		{
			//Si el tipo evento de la cuenta es Accidente de Transito el campo estado embreaguez es requerido
			if(valoracionForm.getValoracionUrgencias().getCodigoTipoEvento().equals(ConstantesIntegridadDominio.acronimoAccidenteTransito) &&
				valoracionForm.getValoracionUrgencias().getEstadoEmbriaguez()==null)
				errores.add("", new ActionMessage("errors.required","El paciente sufrió un accidente de transito, por este motivo el estado de embriaguez"));
		}
		
		//Causa externa
		if(valoracion.getCodigoCausaExterna()==0)
			errores.add("", new ActionMessage("errors.required","La causa externa"));
		
		//Finalidad de la consulta
		if(valoracion.getValoracionConsulta().getCodigoFinalidadConsulta().equals(""))
			errores.add("", new ActionMessage("errors.required","La finalidad de la consulta"));
		
		//Conducta a seguir
		errores = validacionConductaValoracion(valoracionForm,errores, fechaSistema, horaSistema, fechaIngreso, horaIngreso);
		
		//Diagnóstico de ingreso (SOLO APLICA PARA VALORACION DE HOSPITALIZACION)
		if(valoracionForm.getCodigoViaIngreso()==ConstantesBD.codigoViaIngresoHospitalizacion)
			if(valoracionForm.getValoracionHospitalizacion().getDiagnosticoIngreso().getAcronimo().equals(""))
				errores.add("", new ActionMessage("errors.required","El diagnóstico de ingreso"));
		
		//Diagnóstico Principal
		if(valoracion.getDiagnosticos().get(0).getAcronimo().equals(""))
			errores.add("", new ActionMessage("errors.required","El diagnóstico principal"));
		
		//Tipo Diagnóstico principal
		if(valoracion.getValoracionConsulta().getCodigoTipoDiagnostico()==ConstantesBD.codigoNuncaValido)
			errores.add("", new ActionMessage("errors.required","El tipo de diagnóstico principal"));
		
		//Se realizan las validaciones de los campos de la plantilla
		errores = Plantillas.validacionCamposPlantilla(valoracionForm.getPlantilla(), errores);
		
		//************VALIDACIONES COMPONENTES***********************************
		//Se realizan las validaciones de los campos de la historia mentrual
		if(valoracionForm.getPlantilla().existeComponente(ConstantesCamposParametrizables.tipoComponenteGinecologia))
			errores = valoracion.getHistoriaMenstrual().validate(errores);
		//Se realiza las validaciones de los campos de los signos vitales
		if(valoracionForm.getPlantilla().existeComponente(ConstantesCamposParametrizables.tipoComponenteSignosVitales))
			errores = SignoVital.validate(errores, valoracion.getSignosVitales());
		//Se realizan las validaciones de los campos de oftalmología
		if(valoracionForm.getPlantilla().existeComponente(ConstantesCamposParametrizables.tipoComponenteOftalmologia))
			errores = valoracion.getOftalmologia().validate(errores);
		//************************************************************************
		
		//logger.info("NUMERO DE SOLICITUD EN OFTALMOLOGÍA=> "+valoracionForm.getValoracionUrgencias().getOftalmologia().getNumeroSolicitud());
		return errores;
	}
	
	/**
	 * Método implementado para validar la insercion de la valoracion
	 * @param con 
	 * @param valoracionForm
	 * @param paciente 
	 * @return
	 */
	private ActionErrors validacionModificar(Connection con, ValoracionesForm valoracionForm, PersonaBasica paciente) 
	{
		ActionErrors errores = new ActionErrors();
		
		String fechaSistema = UtilidadFecha.getFechaActual(con);
		String horaSistema = UtilidadFecha.getHoraActual(con);
		String fechaIngreso = paciente.getFechaIngreso();
		String horaIngreso = paciente.getHoraIngreso();
		
		//Conducta a seguir
		errores = validacionConductaValoracion(valoracionForm, errores, fechaSistema, horaSistema, fechaIngreso, horaIngreso);
		
		return errores;
	}

	/**
	 * Método implementado para realizar las validaciones que tienen que ver con la conducta de la valoracion
	 * @param valoracionForm
	 * @param errores
	 * @param horaIngreso 
	 * @param fechaIngreso 
	 * @param horaSistema 
	 * @param fechaSistema 
	 * @return
	 */
	private ActionErrors validacionConductaValoracion(ValoracionesForm valoracionForm, ActionErrors errores, String fechaSistema, String horaSistema, String fechaIngreso, String horaIngreso) 
	{
		//Conducta a seguir
		//Solo aplica para urgencias
		if(valoracionForm.getCodigoViaIngreso()==ConstantesBD.codigoViaIngresoUrgencias)
		{
			if(valoracionForm.getValoracionUrgencias().getCodigoConductaValoracion()==ConstantesBD.codigoNuncaValido)
				errores.add("", new ActionMessage("errors.required","La conducta a seguir"));
			else
			{
				//Dependiendo de la conducta a seguir se realizan validaciones adicionales
				switch(valoracionForm.getValoracionUrgencias().getCodigoConductaValoracion())
				{
					case ConstantesBD.codigoConductaSeguirRemitirMayorComplejidad: 
					case ConstantesBD.codigoConductaSeguirSalidaSinObservacion:
						//Se verifica el campo estado a la salida
						if(valoracionForm.getValoracionUrgencias().getEstadoSalida().equals(""))
						{
							errores.add("", new ActionMessage("errors.required","El estado a la salida"));
							//
							logger.info("ACTUALIZA ESTADO SALIDA >>>");
							valoracionForm.getValoracionUrgencias().setEstadoSalida(ConstantesBD.acronimoSi);
						}
							//Si el estado de salida fue muerto se validan los datos de muerte
						else if(!UtilidadTexto.getBoolean(valoracionForm.getValoracionUrgencias().getEstadoSalida()))
						{
							//Diagnóstico de muerte
							if(valoracionForm.getValoracionUrgencias().getDiagnosticoMuerte().getAcronimo().equals(ConstantesBD.acronimoDiagnosticoNoSeleccionado))
								errores.add("",new ActionMessage("errors.required","El diagnóstico de muerte"));
							
							boolean fechaValida = false, horaValida = false;
							
							//Fecha de muerte
							if(valoracionForm.getValoracionUrgencias().getFechaMuerte().equals(""))
								errores.add("", new ActionMessage("errors.required","La fecha de muerte"));
							else if(!UtilidadFecha.validarFecha(valoracionForm.getValoracionUrgencias().getFechaMuerte()))
								errores.add("", new ActionMessage("errors.formatoFechaInvalido","de muerte"));
							else
								fechaValida = true;
							
							//Hora de muerte
							if(valoracionForm.getValoracionUrgencias().getHoraMuerte().equals(""))
								errores.add("", new ActionMessage("errors.required","La hora de muerte"));
							else if(!UtilidadFecha.validacionHora(valoracionForm.getValoracionUrgencias().getHoraMuerte()).puedoSeguir)
								errores.add("", new ActionMessage("errors.formatoHoraInvalido","de muerte"));
							else
								horaValida = true;
							
							if(fechaValida&&horaValida)
							{
								if(!UtilidadFecha.compararFechas(fechaSistema, horaSistema, valoracionForm.getValoracionUrgencias().getFechaMuerte(), valoracionForm.getValoracionUrgencias().getHoraMuerte()).isTrue())
									errores.add("", new ActionMessage("errors.fechaHoraPosteriorIgualActual","de muerte","del sistema: "+fechaSistema+" - "+horaSistema));
								
								if(!UtilidadFecha.compararFechas(valoracionForm.getValoracionUrgencias().getFechaMuerte(), valoracionForm.getValoracionUrgencias().getHoraMuerte(), fechaIngreso, horaIngreso).isTrue())
									errores.add("", new ActionMessage("errors.fechaHoraAnteriorIgualActual","de muerte","del ingreso del paciente: "+fechaIngreso+" - "+horaIngreso));
							}
							
							//Certificado defuncion
							if(valoracionForm.getValoracionUrgencias().getCertificadoDefuncion().equals(""))
								errores.add("", new ActionMessage("errors.required","El certificado defunción"));
							
							
						}
						else
						{
							//Se verifica si se ingresó número de días y que sea número valido
							if(!valoracionForm.getValoracionUrgencias().getValoracionConsulta().getNumeroDiasIncapacidad().equals("")&&
								Utilidades.convertirAEntero(valoracionForm.getValoracionUrgencias().getValoracionConsulta().getNumeroDiasIncapacidad())==ConstantesBD.codigoNuncaValido)
								errores.add("",new ActionMessage("errors.integer","el campo número de días (incapacidad funcional)"));
						}
					break;
					case ConstantesBD.codigoConductaSeguirTrasladoCuidadoEspecial:
						//Tipo monitoreo
						if(valoracionForm.getValoracionUrgencias().getCodigoTipoMonitoreo()==ConstantesBD.codigoNuncaValido)
							errores.add("", new ActionMessage("errors.required","El tipo de monitoreo"));
					break;
				}
			}
		}
		
		return errores;
	}

	/**
	 * Método implementado para iniciar el flujo de las valoraciones
	 * @param con
	 * @param valoracionForm
	 * @param mapping
	 * @param paciente
	 * @param usuario
	 * @param request
	 * @return
	 */
	private ActionForward accionEmpezar(Connection con, ValoracionesForm valoracionForm, ActionMapping mapping, PersonaBasica paciente, UsuarioBasico usuario, HttpServletRequest request) 
	{
		valoracionForm.reset();
		
		//Se elimina la variable en session de curvas de crecimiento
		HttpSession session=request.getSession();
		session.removeAttribute("curvasCrecimientoPaciente");
		
//		********************ERLIMINAR INCAOPACIDADES NO GUARDADAS********************************
		RegistroIncapacidades mundoIncapacidades = new RegistroIncapacidades();
		
		if (mundoIncapacidades.eliminarIncapacidadesInactivasXRegistro(con, paciente.getCodigoIngreso()).equals(ConstantesBD.acronimoSi)){
			logger.info("se han eliminmado satisfactoriamente los registros de incapacidades con campo activo N");
		}
		
		if (mundoIncapacidades.consultarIncapacidadesCambios(con, paciente.getCodigoIngreso()).equals(ConstantesBD.acronimoSi)){
			logger.info("se han reversado satisfactoriamente los registros de incapacidades con campo activo C");
		}
		
		//**********************VALIDACIONES INICIALES**********************************************************
		ActionErrors errores = validacionesIniciales(con,paciente,usuario, valoracionForm, request);
		if(!errores.isEmpty())
		{
			saveErrors(request, errores);
			UtilidadBD.closeConnection(con);
			return mapping.findForward("paginaErroresActionErrors");
		}
		//**********************FIN VALIDACIONES INICIALES*******************************************************
		
		//Se eliminan las fichas de epidemiología inactivas del paciente
		UtilidadFichas.eliminarFichasInactivas(con, usuario.getLoginUsuario(), paciente.getCodigoPersona());
		
		
		//Se divide el flujo dependiendo de la vía de ingreso
		if(valoracionForm.getCodigoViaIngreso()==ConstantesBD.codigoViaIngresoUrgencias)
		{
			Valoraciones.cambiarPacienteEnEvaloracion(con, paciente.getCodigoPersona(), true, usuario.getLoginUsuario(), request.getSession().getId());
			logger.info("getValoracionUrgencias...___"+valoracionForm.getValoracionUrgencias().getCodigoEstadoHistoriaClinica());
			
			valoracionForm.setDtoTriage(Triage.consultarInfoResumenTriagePorCuenta(paciente.getCodigoCuenta()));
			//Si el estado médico es solicitada quiere decir que se ingresará por primera vez
			if(valoracionForm.getValoracionUrgencias().getCodigoEstadoHistoriaClinica()==ConstantesBD.codigoEstadoHCSolicitada)
			{
				//**********************INSERTAR ********************************************************************
				return accionEmpezarInsertarUrgencias(con,valoracionForm,usuario,paciente,request,mapping);
				//***************************************************************************************************
			}
			else if(valoracionForm.getValoracionUrgencias().getCodigoEstadoHistoriaClinica()==ConstantesBD.codigoEstadoHCInterpretada)
			{
				//*******************MODIFICAR*********************************************************************
				return accionEmpezarModificarUrgencias(con, valoracionForm, usuario,paciente,request,mapping);
				//**************************************************************************************************
			}
		}
		//Flujo de hospitalizacion
		else if(valoracionForm.getCodigoViaIngreso()==ConstantesBD.codigoViaIngresoHospitalizacion)
		{
			//Se verifica si existe asocio
			valoracionForm.setAsocio(paciente.getExisteAsocio());
			
			//Si el estado médico es solicitada quiere decir que se ingresará por primera vez
			if(valoracionForm.getValoracionHospitalizacion().getCodigoEstadoHistoriaClinica()==ConstantesBD.codigoEstadoHCSolicitada)
			{
				//**********************INSERTAR ********************************************************************
				return accionEmpezarInsertarHospitalizacion(con,valoracionForm,usuario,paciente,request,mapping);
				//***************************************************************************************************
			}
			else if(valoracionForm.getValoracionHospitalizacion().getCodigoEstadoHistoriaClinica()==ConstantesBD.codigoEstadoHCInterpretada)
			{
				//*******************MODIFICAR*********************************************************************
				return accionEmpezarModificarHospitalizacion(con, valoracionForm, usuario,paciente,request,mapping);
				//**************************************************************************************************
			}
		}
		
		errores.add("", new ActionMessage("errors.problemasGenericos","al tratar de ingresar información de la valoración"));
		saveErrors(request, errores);
		UtilidadBD.closeConnection(con);
		return mapping.findForward("paginaErroresActionErrors");
	}

	/**
	 * Método 
	 * @param con
	 * @param valoracionForm
	 * @param usuario
	 * @param paciente
	 * @param request
	 * @param mapping
	 * @return
	 */
	private ActionForward accionEmpezarModificarHospitalizacion(Connection con, ValoracionesForm valoracionForm, UsuarioBasico usuario, PersonaBasica paciente, HttpServletRequest request, ActionMapping mapping) 
	{
	 	//Se carga la plantilla x el codigoPK de la plantilla registrada
		//por esta razón la mayoria de los campos se manda codigoNuncaValido
		valoracionForm.setPlantilla(Plantillas.cargarPlantillaXSolicitud(
			con, 
			usuario.getCodigoInstitucionInt(), 
			valoracionForm.getCodigoFuncionalidad(), 
			ConstantesBD.codigoNuncaValido, //centro costo (no aplica) 
			ConstantesBD.codigoNuncaValido, //sexo (no aplica) 
			ConstantesBD.codigoNuncaValido, //especialidad (no aplica) 
			Plantillas.obtenerCodigoPlantillaXIngreso(con, ConstantesBD.codigoNuncaValido, ConstantesBD.codigoNuncaValido, Integer.parseInt(valoracionForm.getNumeroSolicitudHospitalizacion())),
			true, // se consulta información 
			paciente.getCodigoPersona(), 
			ConstantesBD.codigoNuncaValido, //codigo Ingreso (no aplica) 
			Integer.parseInt(valoracionForm.getNumeroSolicitudHospitalizacion()),
			ConstantesBD.codigoNuncaValido, // codigo sexo
			ConstantesBD.codigoNuncaValido, //dias edad paciente
			false));
		
		//Se verifica que se hayan encontrado secciones fijas
		if(valoracionForm.getPlantilla().getSeccionesFijas().size()==0)
		{
			ActionErrors errores = new ActionErrors();
			errores.add("",new ActionMessage("errors.noExiste2","parametrización de secciones fijas para la Valoración de Hospitalización. Por favor verifique"));
			saveErrors(request, errores);
			UtilidadBD.closeConnection(con);
			return mapping.findForward("paginaErroresActionErrors");
			
		}
		
		//Se carga la valoracion
		Valoraciones mundoValoracion = new Valoraciones();
		mundoValoracion.setPlantilla(valoracionForm.getPlantilla());
		mundoValoracion.setNumeroSolicitud(valoracionForm.getValoracionHospitalizacion().getNumeroSolicitud());
		mundoValoracion.getValoracionHospitalizacion().setNumeroSolicitud(valoracionForm.getValoracionHospitalizacion().getNumeroSolicitud());
		mundoValoracion.getValoracionHospitalizacion().setCodigoEstadoHistoriaClinica(valoracionForm.getValoracionHospitalizacion().getCodigoEstadoHistoriaClinica());
		mundoValoracion.cargarHospitalizacion(con, usuario, paciente, true);
	
	
		valoracionForm.setValoracionHospitalizacion(mundoValoracion.getValoracionHospitalizacion());
		valoracionForm.setDiagnosticosRelacionados(mundoValoracion.getDiagnosticosRelacionados());
		
		//Curvas de crecimiento
		int idValoracion = -1;
		
		if(valoracionForm.getValoracionUrgencias().getNumeroSolicitud() != null && !valoracionForm.getValoracionUrgencias().getNumeroSolicitud().equals(""))
			idValoracion = Integer.valueOf(valoracionForm.getValoracionUrgencias().getNumeroSolicitud());
		if(valoracionForm.getValoracionHospitalizacion().getNumeroSolicitud() != null && !valoracionForm.getValoracionHospitalizacion().getNumeroSolicitud().equals(""))
			idValoracion = Integer.valueOf(valoracionForm.getValoracionHospitalizacion().getNumeroSolicitud());
		
		if(idValoracion != -1)
		{
			List<HistoricoImagenPlantillaDto> dtohipTemp = new ArrayList<HistoricoImagenPlantillaDto>();
			try {
				dtohipTemp = new HistoriaClinicaFacade().valoracionesPorId(idValoracion);
			} catch (IPSException e) {
				Log4JManager.error(e);
			}
			valoracionForm.setDtoHistoricoImagenPlantilla(dtohipTemp);
			
			valoracionForm.setMostrarDetalles(false);
			if(valoracionForm.getIndiceCurvaSeleccionada()!=null){
				valoracionForm.setCurvaSeleccionada(valoracionForm.getDtoHistoricoImagenPlantilla().get(valoracionForm.getIndiceCurvaSeleccionada()));
				
				Calendar c = Calendar.getInstance();
				c.setTime(valoracionForm.getCurvaSeleccionada().getFecha());
		
				valoracionForm.setEdadCalculada(paciente.calcularEdadEnFechaDetallada(c.get(Calendar.DAY_OF_MONTH), c.get(Calendar.MONTH)+1, c.get(Calendar.YEAR)));
				valoracionForm.setMostrarDetalles(true);
				valoracionForm.setIndiceCurvaSeleccionada(null);
			}
		}
		//Fin curvas de crecimiento
		/**
		 * Alberto Ovalle
		 * mt5749
		 * inicia proceso vistaValoraciones para accionEmpezarModificarUrgencias
		 */

		List<DtoValoracion> vistaValoracion =new ArrayList<DtoValoracion>();
		try {
			if(valoracionForm.getValoracionHospitalizacion().getNumeroSolicitud()!= null && !valoracionForm.getValoracionHospitalizacion().getNumeroSolicitud().equals("")) {
			vistaValoracion = mundoValoracion.obtenerValoracionesObservacion(con,usuario, paciente,valoracionForm.getValoracionHospitalizacion().getNumeroSolicitud());
					for(DtoValoracion dtoValoracion:vistaValoracion) {
					mundoValoracion.validarFechaObservaciones(vistaValoracion);
					dtoValoracion.setVistaobservaciones(vistaValoracion);
					valoracionForm.setVistaobservaciones(vistaValoracion);
					valoracionForm.setDtoValoracion(dtoValoracion);	
			    }
		    }
		} catch(Exception e) {
			logger.error("Exception: al iterar la lista vistaValoracion modificarHospitalizacion"+e);
			e.printStackTrace();
		}
		/*********************************************/
		UtilidadBD.closeConnection(con);
		return mapping.findForward("modificarHospitalizacion");
	}

	/**
	 * Método implementado para empezar a insertar una valoración de hospitalización
	 * @param con
	 * @param valoracionForm
	 * @param usuario
	 * @param paciente
	 * @param request
	 * @param mapping
	 * @return
	 */
	private ActionForward accionEmpezarInsertarHospitalizacion(Connection con, ValoracionesForm valoracionForm, UsuarioBasico usuario, PersonaBasica paciente, HttpServletRequest request, ActionMapping mapping) 
	{
		ActionErrors errores = new ActionErrors();
		
		///Se carga la plantilla
		valoracionForm.setPlantilla(Plantillas.cargarPlantillaXSolicitud(
			con, 
			usuario.getCodigoInstitucionInt(), 
			valoracionForm.getCodigoFuncionalidad(), 
			paciente.getCodigoArea(), 
			paciente.getCodigoSexo(), 
			ConstantesBD.codigoNuncaValido,
			ConstantesBD.codigoNuncaValido, 
			false, //no se consulta información 
			paciente.getCodigoSexo(), 
			paciente.getCodigoIngreso(), 
			Integer.parseInt(valoracionForm.getValoracionHospitalizacion().getNumeroSolicitud()),
			paciente.getCodigoSexo(),
			UtilidadFecha.numeroDiasEntreFechas(paciente.getFechaNacimiento(), UtilidadFecha.getFechaActual(con)),
			true));
		
		//Se verifica que se hayan encontrado secciones fijas
		if(valoracionForm.getPlantilla().getSeccionesFijas().size()==0)
		{
			errores.add("",new ActionMessage("errors.noExiste2","parametrización de secciones fijas para la Valoración de Hospitalización. Por favor verifique"));
			saveErrors(request, errores);
			UtilidadBD.closeConnection(con);
			return mapping.findForward("paginaErroresActionErrors");
			
		}
		else
			valoracionForm.getPlantilla().cargarEdadesPaciente(paciente);
		
		//Se realiza una precarga de la valoración
		Valoraciones mundoValoracion = new Valoraciones();
		mundoValoracion.setPlantilla(valoracionForm.getPlantilla());
		
		mundoValoracion.setNumeroSolicitud(valoracionForm.getValoracionHospitalizacion().getNumeroSolicitud());
		mundoValoracion.precargarBaseHospitalizacion(con, paciente, usuario);
		mundoValoracion.getValoracionHospitalizacion().setNumeroSolicitud(valoracionForm.getValoracionHospitalizacion().getNumeroSolicitud());
		mundoValoracion.getValoracionHospitalizacion().setCodigoEstadoHistoriaClinica(valoracionForm.getValoracionHospitalizacion().getCodigoEstadoHistoriaClinica());
		mundoValoracion.getValoracionHospitalizacion().setCuidadoEspecial(valoracionForm.getValoracionHospitalizacion().isCuidadoEspecial());
		
		valoracionForm.setValoracionHospitalizacion(mundoValoracion.getValoracionHospitalizacion());
		valoracionForm.setCausasExternas(mundoValoracion.getCausasExternas());
		valoracionForm.setFinalidades(mundoValoracion.getFinalidades());
		valoracionForm.setTiposDiagnostico(mundoValoracion.getTiposDiagnostico());
		
		//Se verifica componentes
		if(valoracionForm.getPlantilla().existeComponente(ConstantesCamposParametrizables.tipoComponenteGinecologia))
		{
			valoracionForm.setRangosEdadMenarquia(mundoValoracion.getRangosEdadMenarquia());
			valoracionForm.setRangosEdadMenopausia(mundoValoracion.getRangosEdadMenopausia());
			valoracionForm.setConceptosMenstruacion(mundoValoracion.getConceptosMenstruacion());
		}
		
	
		for (int i = 0; i < valoracionForm.getPlantilla().getSeccionesFijas().size(); i++) {
			if(valoracionForm.getPlantilla().getSeccionesFijas().get(i).getSeccion().getCodigo()==ConstantesCamposParametrizables.seccionFijaProfesionalQueResponde){
				valoracionForm.getPlantilla().getSeccionesFijas().get(i).setVisible(true);
			}
		}
		
		
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("ingresarHospitalizacion");
	}
	
	
	

	/**
	 * Método implementado para iniciar la modificación de urgencias
	 * @param con
	 * @param valoracionForm
	 * @param usuario
	 * @param paciente
	 * @param request
	 * @param mapping
	 * @return
	 */
	private ActionForward accionEmpezarModificarUrgencias(Connection con, ValoracionesForm valoracionForm, UsuarioBasico usuario, PersonaBasica paciente, HttpServletRequest request, ActionMapping mapping) 
	{
		logger.info("\n\n\nNUMERO SOLICITUD Urgencias >>"+valoracionForm.getNumeroSolicitudUrgencias());
		int numeroSolicitudUrg=0;
		//Se carga la plantilla x el codigo de la plantilla que se haya ingresado
		//por esta razón la mayoria de los campos se manda codigoNuncaValido
		valoracionForm.setPlantilla(Plantillas.cargarPlantillaXSolicitud(
			con, 
			usuario.getCodigoInstitucionInt(), 
			valoracionForm.getCodigoFuncionalidad(), 
			ConstantesBD.codigoNuncaValido, //centro costo (no aplica) 
			ConstantesBD.codigoNuncaValido, //sexo (no aplica)
			ConstantesBD.codigoNuncaValido, //especialidad (no aplica)
			Plantillas.obtenerCodigoPlantillaXIngreso(con, ConstantesBD.codigoNuncaValido, ConstantesBD.codigoNuncaValido, Integer.parseInt(valoracionForm.getNumeroSolicitudUrgencias())), 
			true, // se consulta información 
			paciente.getCodigoPersona(), 
			ConstantesBD.codigoNuncaValido, //codigo Ingreso (no aplica) 
			Integer.parseInt(valoracionForm.getNumeroSolicitudUrgencias()),
			ConstantesBD.codigoNuncaValido, //codigo sexo paciente
			ConstantesBD.codigoNuncaValido, //días edad paciente
			false));
		
	
		
		
		//Curvas de crecimiento
		int idValoracion = -1;
		
		if(valoracionForm.getValoracionUrgencias().getNumeroSolicitud() != null && !valoracionForm.getValoracionUrgencias().getNumeroSolicitud().equals(""))
			idValoracion = Integer.valueOf(valoracionForm.getValoracionUrgencias().getNumeroSolicitud());
		if(valoracionForm.getValoracionHospitalizacion().getNumeroSolicitud() != null && !valoracionForm.getValoracionHospitalizacion().getNumeroSolicitud().equals(""))
			idValoracion = Integer.valueOf(valoracionForm.getValoracionHospitalizacion().getNumeroSolicitud());
			
		if(idValoracion!=-1)
		{
			List<HistoricoImagenPlantillaDto> dtohipTemp = new ArrayList<HistoricoImagenPlantillaDto>();
			try {
				dtohipTemp = new HistoriaClinicaFacade().valoracionesPorId(idValoracion);
			} catch (IPSException e) {
				Log4JManager.error(e);
			}
			valoracionForm.setDtoHistoricoImagenPlantilla(dtohipTemp);
			
			valoracionForm.setMostrarDetalles(false);
			if(valoracionForm.getIndiceCurvaSeleccionada()!=null){
				valoracionForm.setCurvaSeleccionada(valoracionForm.getDtoHistoricoImagenPlantilla().get(valoracionForm.getIndiceCurvaSeleccionada()));
				
				Calendar c = Calendar.getInstance();
				c.setTime(valoracionForm.getCurvaSeleccionada().getFecha());
		
				valoracionForm.setEdadCalculada(paciente.calcularEdadEnFechaDetallada(c.get(Calendar.DAY_OF_MONTH), c.get(Calendar.MONTH)+1, c.get(Calendar.YEAR)));
				valoracionForm.setMostrarDetalles(true);
				valoracionForm.setIndiceCurvaSeleccionada(null);
			}
		}
		//Fin curvas de crecimiento
		
		
		
		
		try {
		valoracionForm.setCheckEnlaceOrdenesAMbulatorias(	Plantillas.consultarVisibilidadPlantillaFijaSinOrdenUrgencias(con,
					Plantillas.obtenerCodigoPlantillaXIngreso(con, ConstantesBD.codigoNuncaValido, ConstantesBD.codigoNuncaValido, Integer.parseInt(valoracionForm.getNumeroSolicitudUrgencias()))
					,paciente.getCodigoSexo()
					,ConstantesBD.codigoNuncaValido,
					ConstantesCamposParametrizables.formatoUrgencias));
		
		
		
		
		//Se verifica que se hayan encontrado secciones fijas
		if(valoracionForm.getPlantilla().getSeccionesFijas().size()==0)
		{
			ActionErrors errores = new ActionErrors();
			errores.add("",new ActionMessage("errors.noExiste2","parametrización de secciones fijas para la Valoración de Urgencias. Por favor verifique"));
			saveErrors(request, errores);
			UtilidadBD.closeConnection(con);
			return mapping.findForward("paginaErroresActionErrors");
			
		}
		
		logger.info("\n\n\nNUMERO SOLICITUD Urgencias 2>>"+valoracionForm.getNumeroSolicitudUrgencias());
		numeroSolicitudUrg=Utilidades.convertirAEntero(valoracionForm.getNumeroSolicitudUrgencias()+"");
		//Se carga la valoracion
		Valoraciones mundoValoracion = new Valoraciones();
		mundoValoracion.setPlantilla(valoracionForm.getPlantilla());
		mundoValoracion.setNumeroSolicitud(numeroSolicitudUrg+"");
		mundoValoracion.getValoracionUrgencias().setNumeroSolicitud(numeroSolicitudUrg+"");
		mundoValoracion.getValoracionUrgencias().setCodigoEstadoHistoriaClinica(valoracionForm.getValoracionUrgencias().getCodigoEstadoHistoriaClinica());
		mundoValoracion.cargarUrgencias(con, usuario, paciente, paciente.getCodigoPersona(),true);
		/**
		 * MT 5568
		 */
		try {
			InfoDatosInt centroCostoPaciente = mundoValoracion.obtenerDatosCentroCostoXSolcitud(con, numeroSolicitudUrg);
			mundoValoracion.getValoracionUrgencias().setDatoAreaPaciente(centroCostoPaciente);				
		} catch (Exception e) {
			logger.error(e);
		}	
		/**
		 * Fin MT 5568
		 */	
		valoracionForm.setValoracionUrgencias(mundoValoracion.getValoracionUrgencias());
		
		
		valoracionForm.setDiagnosticosRelacionados(mundoValoracion.getDiagnosticosRelacionados());
		valoracionForm.setConductasValoracion(mundoValoracion.getConductasValoracion());
		valoracionForm.setTiposMonitoreo(mundoValoracion.getTiposMonitoreo());
		valoracionForm.setPuedoModificarConductaValoracion(mundoValoracion.isPuedoModificarConductaValoracion());
		valoracionForm.setAdvertencias(mundoValoracion.getAdvertencias());
		logger.info("\n\n\n\n NUMERO SOLICITUD>>> "+valoracionForm.getValoracionUrgencias().getNumeroSolicitud());
		logger.info("\n\n\n\n NUMERO SOLICITUD2>>> "+numeroSolicitudUrg);
		valoracionForm.setSolicitudPendiente(UtilidadesFacturacion.esSolicitudTotalPendiente(con,numeroSolicitudUrg+""));
		
		//Validación adicional de la conductas
		if(valoracionForm.getValoracionUrgencias().getCodigoConductaValoracion()==ConstantesBD.codigoConductaSeguirSalidaSinObservacion||
			valoracionForm.getValoracionUrgencias().getCodigoConductaValoracion()==ConstantesBD.codigoConductaSeguirRemitirMayorComplejidad){
			valoracionForm.setSeccionEstadoSalida(true);
		}else if(valoracionForm.getValoracionUrgencias().getCodigoConductaValoracion()==ConstantesBD.codigoConductaSeguirTrasladoCuidadoEspecial)
			valoracionForm.setSeccionTipoMonitoreo(true);
		
		
		/**
		 * Alberto Ovalle
		 * mt5749
		 * inicia proceso vistaValoraciones para accionEmpezarModificarUrgencias
		 */

		List<DtoValoracion> vistaValoracion =new ArrayList<DtoValoracion>();
		try {
			if(valoracionForm.getValoracionUrgencias().getNumeroSolicitud() != null && !valoracionForm.getValoracionUrgencias().getNumeroSolicitud().equals("")) {
			vistaValoracion = mundoValoracion.obtenerValoracionesOrdenada(con,usuario, paciente,valoracionForm.getValoracionUrgencias().getNumeroSolicitud());
					for(DtoValoracion dtoValoracion:vistaValoracion) {
					mundoValoracion.validarFechaObservaciones(vistaValoracion);
					dtoValoracion.setVistaobservaciones(vistaValoracion);
					valoracionForm.setVistaobservaciones(vistaValoracion);
					valoracionForm.setDtoValoracion(dtoValoracion);	
			    }
		    }
		} catch(Exception e) {
			logger.error("Exception: al iterar la lista vistaValoracion modificarUrgencias"+e);
			e.printStackTrace();
		}
		/*********************************************/
		UtilidadBD.closeConnection(con);
		
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return mapping.findForward("modificarUrgencias");
	}

	/**
	 * Método implementado para empezar a insertar una valoración de urgencias
	 * @param con
	 * @param valoracionForm
	 * @param usuario
	 * @param paciente
	 * @param request
	 * @param mapping 
	 * @return
	 */
	private ActionForward accionEmpezarInsertarUrgencias(Connection con, ValoracionesForm valoracionForm, UsuarioBasico usuario, PersonaBasica paciente, HttpServletRequest request, ActionMapping mapping) 
	{
		ActionErrors errores = new ActionErrors();
		
		///Se carga la plantilla
		valoracionForm.setPlantilla(Plantillas.cargarPlantillaXSolicitud(
			con, 
			usuario.getCodigoInstitucionInt(), 
			valoracionForm.getCodigoFuncionalidad(), 
			paciente.getCodigoArea(), 
			paciente.getCodigoSexo(), 
			ConstantesBD.codigoNuncaValido, 
			ConstantesBD.codigoNuncaValido, 
			false, //no se consulta información 
			paciente.getCodigoSexo(), 
			paciente.getCodigoIngreso(), 
			Integer.parseInt(valoracionForm.getValoracionUrgencias().getNumeroSolicitud()),
			paciente.getCodigoSexo(),
			UtilidadFecha.numeroDiasEntreFechas(paciente.getFechaNacimiento(), UtilidadFecha.getFechaActual(con)),
			true));
		
		
		try {
			valoracionForm.setCheckEnlaceOrdenesAMbulatorias(	Plantillas.consultarVisibilidadPlantillaFijaSinOrdenUrgencias(con,
					ConstantesBD.codigoNuncaValido
					,paciente.getCodigoSexo()
					,paciente.getCodigoArea(),
					ConstantesCamposParametrizables.formatoUrgencias));
		
		//Se verifica que se hayan encontrado secciones fijas
		if(valoracionForm.getPlantilla().getSeccionesFijas().size()==0)
		{
			errores.add("",new ActionMessage("errors.noExiste2","parametrización de secciones fijas para la Valoración de Urgencias. Por favor verifique"));
			saveErrors(request, errores);
			UtilidadBD.closeConnection(con);
			return mapping.findForward("paginaErroresActionErrors");
			
		}
		else
			valoracionForm.getPlantilla().cargarEdadesPaciente(paciente);
		
		//Se realiza una precarga de la valoración
		Valoraciones mundoValoracion = new Valoraciones();
		mundoValoracion.setPlantilla(valoracionForm.getPlantilla());
		mundoValoracion.setNumeroSolicitud(valoracionForm.getValoracionUrgencias().getNumeroSolicitud());
		mundoValoracion.precargarBaseUrgencias(con, paciente, usuario);
		mundoValoracion.getValoracionUrgencias().setNumeroSolicitud(valoracionForm.getValoracionUrgencias().getNumeroSolicitud());
		mundoValoracion.getValoracionUrgencias().setCodigoEstadoHistoriaClinica(valoracionForm.getValoracionUrgencias().getCodigoEstadoHistoriaClinica());
		
		valoracionForm.setValoracionUrgencias(mundoValoracion.getValoracionUrgencias());
		valoracionForm.setEstadosConciencia(mundoValoracion.getEstadosConciencia());
		valoracionForm.setCausasExternas(mundoValoracion.getCausasExternas());
		valoracionForm.setFinalidades(mundoValoracion.getFinalidades());
		valoracionForm.setConductasValoracion(mundoValoracion.getConductasValoracion());
		valoracionForm.setTiposDiagnostico(mundoValoracion.getTiposDiagnostico());
		valoracionForm.setTiposMonitoreo(mundoValoracion.getTiposMonitoreo());
		
		//Se verifica componentes
		if(valoracionForm.getPlantilla().existeComponente(ConstantesCamposParametrizables.tipoComponenteGinecologia))
		{
			valoracionForm.setRangosEdadMenarquia(mundoValoracion.getRangosEdadMenarquia());
			valoracionForm.setRangosEdadMenopausia(mundoValoracion.getRangosEdadMenopausia());
			valoracionForm.setConceptosMenstruacion(mundoValoracion.getConceptosMenstruacion());
		}
		
		//logger.info("NUMERO DE SOLICITUD EN OFTALMOLOGÍA=> "+valoracionForm.getValoracionUrgencias().getOftalmologia().getNumeroSolicitud());
		
		
		
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally{
			UtilidadBD.closeConnection(con);
		}
		return mapping.findForward("ingresarUrgencias");

	}

	/**
	 * Método implementado para realizar las validaciones iniciales
	 * @param con
	 * @param paciente
	 * @param usuario
	 * @param valoracionForm 
	 * @param request 
	 * @return
	 */
	private ActionErrors validacionesIniciales(Connection con, PersonaBasica paciente, UsuarioBasico usuario, ValoracionesForm valoracionForm, HttpServletRequest request) 
	{
		ActionErrors errores = new ActionErrors();
	
		
		//Validación de autoatencion
		ResultadoBoolean respuesta = UtilidadesHistoriaClinica.esUsuarioAutoatendido(usuario, paciente);
		if(respuesta.isTrue())
			errores.add("", new ActionMessage(respuesta.getDescripcion()));
		//******************************************************************************************************************
		//***************************VALIDACIONES DE LA CUENTA***************************************************************
		//*******************************************************************************************************************
		if(paciente.getCodigoCuenta()>0)
		{
			/**
			 * Validar concurrencia
			 * Si ya está en proceso de facturación, no debe dejar entrar
			 **/
			if(UtilidadValidacion.estaEnProcesofacturacion(con, paciente.getCodigoPersona(), "") )
				errores.add("", new ActionMessage("error.facturacion.cuentaEnProcesoFact"));
			
			/**
			 * Validar concurrencia
			 * Si ya está en proceso de distribucion, no debe dejar entrar
			 **/
			else if(UtilidadValidacion.estaEnProcesoDistribucion(con, paciente.getCodigoPersona(), usuario.getLoginUsuario()) )
				errores.add("", new ActionMessage("error.facturacion.cuentaEnProcesoDistribucion"));
		}
		
		
		///Se realiza la validacion del paciente cargado, ingreso cargado y cuenta valida
		RespuestaValidacion resp = UtilidadValidacion.esValidoPacienteCargado(con, paciente);
		
		if(resp.puedoSeguir)
		{
			//Se verifica que el ingreso se encuentre abierto
			if(UtilidadesHistoriaClinica.obtenerEstadoIngreso(con, paciente.getCodigoIngreso()).getAcronimo().equals(ConstantesIntegridadDominio.acronimoEstadoAbierto))
			{
				valoracionForm.setCodigoViaIngreso(paciente.getCodigoUltimaViaIngreso());
				//Se verifica que la vía de ingreso sea urgencias u hospitalización
				if(valoracionForm.getCodigoViaIngreso()==ConstantesBD.codigoViaIngresoHospitalizacion||valoracionForm.getCodigoViaIngreso()==ConstantesBD.codigoViaIngresoUrgencias)
				{
					//Se debe verificar que no sea por entidad subcontratada
					if(paciente.esIngresoEntidadSubcontratada())
						errores.add("Es paciente por entidad subcintratada",new ActionMessage("error.ingresoEntidadSubContratada"));
					
					//Se verifica que no sea un preingreso
					if(UtilidadesManejoPaciente.ingresoConPreingresoPendiente(con, usuario.getCodigoInstitucionInt(), paciente.getCodigoIngreso()))
						errores.add("Es paciente con preingreso",new ActionMessage("prompt.generico","No se puede valorar un paciente de preingreso"));
					
					if(valoracionForm.getCodigoViaIngreso()==ConstantesBD.codigoViaIngresoHospitalizacion)
						valoracionForm.setCodigoFuncionalidad(ConstantesCamposParametrizables.funcParametrizableValoracionHospitalizacion);
					else if(valoracionForm.getCodigoViaIngreso()==ConstantesBD.codigoViaIngresoUrgencias)
						valoracionForm.setCodigoFuncionalidad(ConstantesCamposParametrizables.funcParametrizableValoracionUrgencias);
						
				}
				else
					errores.add("Solo Urgencias/Hospitalizacion",new ActionMessage("errors.opcionDisponible","pacientes en hospitalización y/o urgencias"));
			
			}
			else
				errores.add("Paciente sin ingreso abierto",new ActionMessage("errors.paciente.noIngreso"));
			
		}
		else
		{
			//Se verifica si el paciente tiene cuentas abiertas en otros centros de atencion
			//consultan el nombre del centro de atencion de alguna cuenta activa que tenga el paciente
			String nomCentroAtencion = Utilidades.getNomCentroAtencionIngresoAbierto(con,paciente.getCodigoPersona()+"");
			if(nomCentroAtencion.equals(""))
				errores.add("error validacion paciente",new ActionMessage(resp.textoRespuesta));
			else
				errores.add("Paciente con ingreso abierto en otro centro de atencion",new ActionMessage("errores.paciente.ingresoAbiertoCentroAtencion",nomCentroAtencion));
		}
		//******************************************************************************************************************
		//************************VALIDACIONES DEL USUARIO*******************************************************************
		//*******************************************************************************************************************
		//Si no han habido errores hasta ahora se prosigue a validar
		if(errores.isEmpty())
		{
			String validacionUsuario;
			try 
			{
				validacionUsuario = UtilidadValidacion.esMedicoTratante(con, usuario,paciente);
				if(!validacionUsuario.equals(""))
					errores.add("Error en la validacion del usuario",new ActionMessage(validacionUsuario));
			} 
			catch (SQLException e) 
			{
				errores.add("",new ActionMessage("errors.problemasGenericos","al validar el usuario"));
			}
		}
		//**********************************************************************************************************
		//***********************VALIDACIONES SOLICITUD************************************************************
		//**********************************************************************************************************
		//Si no han habido errores hasta ahora se prosigue a validar
		if(errores.isEmpty())
		{
			
			//Se verifica que exista una solicitud
			String[] datosSolicitud = UtilidadValidacion.existeSolicitudValoracionCuenta(con, paciente);
			
			if(valoracionForm.getCodigoViaIngreso()==ConstantesBD.codigoViaIngresoUrgencias)
			{
				valoracionForm.getValoracionUrgencias().setNumeroSolicitud(datosSolicitud[0]);
				valoracionForm.getValoracionUrgencias().setCodigoEstadoHistoriaClinica(Integer.parseInt(datosSolicitud[1]));
				valoracionForm.getValoracionUrgencias().setCuidadoEspecial(false); //cuidado especial no aplica para urgencias
				
				
				if(Integer.parseInt(valoracionForm.getValoracionUrgencias().getNumeroSolicitud())<=0)
					errores.add("",new ActionMessage("errors.noExiste2","valoración de urgencias pendiente de registrar para el paciente"));
				else if(valoracionForm.getValoracionUrgencias().getCodigoEstadoHistoriaClinica()!=ConstantesBD.codigoEstadoHCInterpretada&&valoracionForm.getValoracionUrgencias().getCodigoEstadoHistoriaClinica()!=ConstantesBD.codigoEstadoHCSolicitada)
					errores.add("",new ActionMessage("errors.invalid","El estado médico de la valoración de urgencias"));
			}
			else
			{
				//Se toma por request el campo cuidado especial
				boolean cuidadoEspecial = UtilidadTexto.getBoolean(request.getParameter("cuidadoEspecial"));
			
				valoracionForm.getValoracionHospitalizacion().setNumeroSolicitud(datosSolicitud[0]);
				valoracionForm.getValoracionHospitalizacion().setCodigoEstadoHistoriaClinica(Integer.parseInt(datosSolicitud[1]));
				valoracionForm.getValoracionHospitalizacion().setCuidadoEspecial(cuidadoEspecial);
				
				if(Integer.parseInt(valoracionForm.getValoracionHospitalizacion().getNumeroSolicitud())<=0)
					errores.add("",new ActionMessage("errors.noExiste2","valoración de hospitalización pendiente de registrar para el paciente"));
				else if(valoracionForm.getValoracionHospitalizacion().getCodigoEstadoHistoriaClinica()!=ConstantesBD.codigoEstadoHCInterpretada&&valoracionForm.getValoracionHospitalizacion().getCodigoEstadoHistoriaClinica()!=ConstantesBD.codigoEstadoHCSolicitada)
					errores.add("",new ActionMessage("errors.invalid","El estado médico de la valoración de hospitalización"));
			}
			
			//*******PROCESO ADICIONAL CUIDADO ESPECIAL**********************************************
			/**
			 * Si el flujo es cuidado especial y no se encontró una solicitud de valoracion en estado médico
			 * solicitada, entonces se debe crear una nueva
			 */
			if(valoracionForm.getCodigoViaIngreso()==ConstantesBD.codigoViaIngresoHospitalizacion&&
				valoracionForm.getValoracionHospitalizacion().isCuidadoEspecial()&&
				valoracionForm.getValoracionHospitalizacion().getCodigoEstadoHistoriaClinica()!=ConstantesBD.codigoEstadoHCSolicitada)
			{
				Solicitud solicitud=new Solicitud();
				solicitud.llenarSolicitudValoracionInicial( 					 
					new InfoDatosInt(paciente.getCodigoArea(), "centroCostoSolicitante"), /*Centro costo solicitante*/ 
					new InfoDatosInt (paciente.getCodigoArea(), "centroCostoSolicitado"), /*Centro costo solicitado*/ 
					paciente.getCodigoCuenta(), 
					true, /*cobrable*/ 
					new InfoDatosInt (ConstantesBD.codigoTipoSolicitudInicialHospitalizacion,"")
				);
				
				try
				{
					int numeroSolicitud = solicitud.insertarSolicitudValoracionInicialTransaccional(con, ConstantesBD.continuarTransaccion);
					if(numeroSolicitud<=0)
						errores.add("Error ingresando solicitud valoracion hospitalizacion",new ActionMessage("errors.noSeGraboInformacion","DE LA SOLICITUD VALORACIÓN DE HOSPITALIZACIÓN"));
					else
					{
						valoracionForm.getValoracionHospitalizacion().setNumeroSolicitud(numeroSolicitud+"");
						valoracionForm.getValoracionHospitalizacion().setCodigoEstadoHistoriaClinica(ConstantesBD.codigoEstadoHCSolicitada);
					}
					
				}
				catch(Exception e)
				{
					errores.add("Error ingresando solicitud valoracion hospitalizacion",new ActionMessage("errors.noSeGraboInformacion","DE LA SOLICITUD VALORACIÓN DE HOSPITALIZACIÓN"));
				}
			}
			//***************************************************************************************
		}
		
		
		return errores;
	}
}
