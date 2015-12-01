/*
 * @(#)GuardarInterconsultaAction.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */

package com.princetonsa.action.ordenesmedicas.interconsultas;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.InfoDatosInt;
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.UtilidadValidacion;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.facturacion.InfoResponsableCobertura;
import util.manejoPaciente.UtilidadesManejoPaciente;
import util.ordenesMedicas.UtilidadesOrdenesMedicas;

import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.ordenesmedicas.interconsultas.GuardarInterconsultaForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dto.manejoPaciente.DtoDiagnostico;
import com.princetonsa.dto.manejoPaciente.DtoSolicitudesSubCuenta;
import com.princetonsa.dto.manejoPaciente.DtoSubCuentas;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.inventarios.FormatoJustServNopos;
import com.princetonsa.mundo.ordenesmedicas.OrdenMedica;
import com.princetonsa.mundo.ordenesmedicas.OrdenesAmbulatorias;
import com.princetonsa.mundo.solicitudes.DocumentoAdjunto;
import com.princetonsa.mundo.solicitudes.Solicitud;
import com.princetonsa.mundo.solicitudes.SolicitudInterconsulta;
import com.princetonsa.mundo.solicitudes.ValidacionesSolicitud;
import com.servinte.axioma.bl.manejoPaciente.facade.ManejoPacienteFacade;
import com.servinte.axioma.bl.ordenes.facade.OrdenesFacade;
import com.servinte.axioma.dto.facturacion.ContratoDto;
import com.servinte.axioma.dto.facturacion.ConvenioDto;
import com.servinte.axioma.dto.facturacion.MontoCobroDto;
import com.servinte.axioma.dto.manejoPaciente.AnulacionAutorizacionSolicitudDto;
import com.servinte.axioma.dto.manejoPaciente.AutorizacionCapitacionDto;
import com.servinte.axioma.dto.manejoPaciente.DatosPacienteAutorizacionDto;
import com.servinte.axioma.dto.ordenes.OrdenAutorizacionDto;
import com.servinte.axioma.dto.ordenes.ServicioAutorizacionOrdenDto;
import com.servinte.axioma.fwk.exception.IPSException;
import com.servinte.axioma.fwk.exception.util.CODIGO_ERROR_NEGOCIO;
import com.servinte.axioma.hibernate.HibernateUtil;
import com.servinte.axioma.orm.Medicos;

/**
 *   Action, controla todas las opciones dentro de la solicitud de
 *   interconsultas, incluyendo los posibles casos de error. Y los casos de flujo.
 * @version 1.0, Feb 12, 2004
 */
public class GuardarInterconsultaAction extends Action
{
	/**
	 * Objeto para manejar los logs de esta clase
	*/
	private Logger logger = Logger.getLogger(GuardarInterconsultaAction.class);
		
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
			if(form instanceof GuardarInterconsultaForm)
			{
				try
				{
					con = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection();	
				}
				catch(SQLException e)
				{
					logger.warn("No se pudo abrir la conexión"+e.toString());
				}

				GuardarInterconsultaForm guardarForm =(GuardarInterconsultaForm)form;
				String estado=guardarForm.getEstado(); 

				//guardarForm.setMostrarImprimirAutorizacion(false);
				//guardarForm.setListaNombresReportes(new ArrayList<String>());

				logger.warn("En GuardarInterconsultaAction ["  + estado + "]\n\n ");

				UsuarioBasico medico= (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
				PersonaBasica paciente = (PersonaBasica)request.getSession().getAttribute("pacienteActivo");
				ActionForward validacionesGenerales = this.validacionesUsuario(mapping, request);

				if (validacionesGenerales != null)
				{
					this.cerrarConexion(con);
					return validacionesGenerales ;
				}

				if(estado == null)
				{
					guardarForm.reset();	
					logger.warn("Estado no valido dentro del flujo de Solicitar (null) ");
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}
				else if(estado.equals("resumen"))
				{
					return this.accionResumen(guardarForm,mapping,request, con, medico);
				}
				else if (estado.equals("empezar"))
				{
					return this.accionEmpezar(guardarForm,request,mapping, paciente,medico, con);
				}
				else if(estado.equals("salir"))
				{
					return this.accionSalir(guardarForm,request,mapping,medico,con,response,paciente);
				}
				else if(estado.equals("modificar"))
				{
					return this.accionModificar(guardarForm,request,mapping,medico,paciente,con);
				}
				else if(estado.equals("guardarModificacion"))
				{										
					return this.accionGuardarModificacion(guardarForm,mapping, request, medico, con, paciente);
				}
				else
				{
					guardarForm.reset();
					logger.warn("Estado no valido dentro del flujo de solicitar (null) ");
					request.setAttribute("codigoDescripcionError", "errors.formaTipoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
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

    /**
	 * Este método especifica las acciones a realizar en el estado
	 * empezar.
	 * 
	 * @param guardarForm GuardarInterconsultaForm
	 * 				para pre-llenar datos si es necesario
	 * @param request HttpServletRequest para obtener los 
	 * 				datos de la solicitud genérica
	 * @param mapping Mapping para manejar la navegación
     * @param medico
	 * @param con Conexión con la fuente de datos
	 * @return ActionForward a la página principal "solicitud.jsp"
	 * @throws SQLException
	 */
	private ActionForward accionEmpezar(GuardarInterconsultaForm guardarForm, 
										 HttpServletRequest request, 
										 ActionMapping mapping,
										 PersonaBasica paciente,
										 UsuarioBasico medico, Connection con) throws SQLException
	{
		
		guardarForm.reset();
		//Limpiamos lo que venga del form	
		HttpSession session=request.getSession();
		
		//se obtiene el objeto que contiene los datos de la solicitud genérica
		Solicitud solGeneral=(Solicitud )session.getAttribute("solicitudGeneral");
		
		//para obtener el comentario de la solicitud genérica
		guardarForm.setComentario((String) session.getAttribute("comentario"));
		
		String parametroSolicitud =(String) session.getAttribute("parametroSolicitud");
		String nombreProcedimiento=(String) session.getAttribute("nombreProcedimiento");
		String temp=(String) session.getAttribute("esSolicitudOtros");
		
		guardarForm.setSolPYP(UtilidadTexto.getBoolean(session.getAttribute("pyp")+""));
		guardarForm.setAccionPYP(session.getAttribute("accionPYP")+"");
		if(solGeneral==null||temp==null||parametroSolicitud==null)
		{
				guardarForm.reset();
				logger.warn("El usuario intentó acceder a Interconsulta sin pasar por Solicitud general");
				request.setAttribute("codigoDescripcionError", "errors.accesoInvalido");
				UtilidadBD.cerrarConexion(con);									
				return mapping.findForward("paginaError");
		}
		
		boolean esSolicitudOtros;
		
		if (temp==null)
		{
				esSolicitudOtros=false;
		}
		else if (temp.equals("true"))
		{
				esSolicitudOtros=true;
				guardarForm.setNombreOtros(parametroSolicitud);
		}
		else
		{
			guardarForm.setNombreOtros(null);
				esSolicitudOtros=false;
				try
				{
						guardarForm.setCodigoServicioInterconsulta(Integer.parseInt(parametroSolicitud));
						guardarForm.setNombreServicio(nombreProcedimiento);
				}
				catch (NumberFormatException e)
				{
						guardarForm.reset();
						this.cerrarConexion(con);
						logger.warn("El usuario intentó acceder a Interconsulta sin pasar por Solicitud general");
						request.setAttribute("codigoDescripcionError", "errors.accesoInvalido");
						return mapping.findForward("paginaError");
				}
		}

		guardarForm.setEsSolicitudOtros(esSolicitudOtros);
		
//		session.removeAttribute("parametroSolicitud");
		session.removeAttribute("esSolicitudOtros");
		session.removeAttribute("nombreProcedimiento");
		session.removeAttribute("comentario");
		
		//para en caso de que sea médico lo deje transferir manejo y manejo conjunto && que el centro costo solicitado sea diferente al centro de costo solicitante
		if ((solGeneral.getOcupacionSolicitado().getCodigo()==Integer.parseInt(ValoresPorDefecto.getCodigoOcupacionMedicoGeneral(medico.getCodigoInstitucionInt(),true))||
				solGeneral.getOcupacionSolicitado().getCodigo()==Integer.parseInt(ValoresPorDefecto.getCodigoOcupacionMedicoEspecialista(medico.getCodigoInstitucionInt(),true)))
				&& (solGeneral.getCentroCostoSolicitante().getCodigo()!=solGeneral.getCentroCostoSolicitado().getCodigo()))
		{
				guardarForm.setEsMedico(true);
		}	
		
		if(solGeneral.getCentroCostoSolicitado().getCodigo()==ConstantesBD.codigoCentroCostoExternos &&
			(solGeneral.getOcupacionSolicitado().getCodigo()==Integer.parseInt(ValoresPorDefecto.getCodigoOcupacionMedicoGeneral(medico.getCodigoInstitucionInt(),true))||
				solGeneral.getOcupacionSolicitado().getCodigo()==Integer.parseInt(ValoresPorDefecto.getCodigoOcupacionMedicoEspecialista(medico.getCodigoInstitucionInt(),true))))
		{
				guardarForm.setEsMedico(false);
		}
		
		guardarForm.setTieneSolicitudesCambioTratantePrevias(UtilidadValidacion.existeSolicitudTransferenciaManejoPrevia(con,solGeneral.getCodigoCuenta()));
		
		if(UtilidadValidacion.existeSolicitudTransferenciaManejoPrevia(con,solGeneral.getCodigoCuenta())==true && 
				(solGeneral.getOcupacionSolicitado().getCodigo()==Integer.parseInt(ValoresPorDefecto.getCodigoOcupacionMedicoGeneral(medico.getCodigoInstitucionInt(),true))||
						solGeneral.getOcupacionSolicitado().getCodigo()==Integer.parseInt(ValoresPorDefecto.getCodigoOcupacionMedicoEspecialista(medico.getCodigoInstitucionInt(),true))))
		{
			guardarForm.setExisteSolicitudTransferenciaPrevia(true);
		}
		
		guardarForm.setManejoConjunto(UtilidadValidacion.existeManejoConjuntoActivoSolInterconsulta(con, paciente.getCodigoCuenta(),solGeneral.getCentroCostoSolicitado().getCodigo()));
		
		//*******SE CARGA EL ARREGLO DE LAS OPCIONES DE MANEJO DE INTERCONSULTA***************************************
		if(paciente.getCodigoUltimaViaIngreso()==ConstantesBD.codigoViaIngresoHospitalizacion)
		{
			boolean esCentroCostoSolicitadoTipoMonitoreo = false;
			int codigoTipoMonitoreo = UtilidadesManejoPaciente.obtenerCodigoTipoMonitoreoIngresoCuidadoEspecial(con, paciente.getCodigoIngreso());
			if(codigoTipoMonitoreo!=ConstantesBD.codigoNuncaValido)
				esCentroCostoSolicitadoTipoMonitoreo = UtilidadesManejoPaciente.estaCentroCostoEnTipoMonitoreo(con, solGeneral.getCentroCostoSolicitado().getCodigo(), codigoTipoMonitoreo);
			
			if(!esCentroCostoSolicitadoTipoMonitoreo)
				guardarForm.setOpcionesManejo(UtilidadesOrdenesMedicas.obtenerOpcionesManejoInterconsulta(con, ""));
			else
				guardarForm.setOpcionesManejo(UtilidadesOrdenesMedicas.obtenerOpcionesManejoInterconsulta(con, ConstantesBD.codigoSeTransfiereManejoPaciente+""));
		}
		else
			guardarForm.setOpcionesManejo(UtilidadesOrdenesMedicas.obtenerOpcionesManejoInterconsulta(con, ConstantesBD.codigoSeDeseaConceptoSolamente+""));
		//**************************************************************************************************************
		
		this.cerrarConexion(con);
		return mapping.findForward("principal");
	}

	
	/**
		* Este método especifica las acciones a realizar en el estado
		* salir.
		* Se copian las propiedades del objeto solicitudGeneral
		* en el objeto mundoInterconsulta
		* 
		* @param guardarForm GuardarInterconsultaForm
		* @param request HttpServletRequest para obtener 
		* 					de la session el objeto de solicitud general
		* @param mapping Mapping para manejar la navegación
		* @param UsuarioBasico medico (para cargar el histórico)
		* @param con Conexión con la fuente de datos
	 * @param paciente 
	 * @param response 
		* 
		* @return ActionForward "guardarInterconsulta.do?estado=resumen"
		* @throws SQLException
	*/
	@SuppressWarnings("deprecation")
	private ActionForward accionSalir(GuardarInterconsultaForm guardarForm,
									 HttpServletRequest request, 
									 ActionMapping mapping, UsuarioBasico medico,
									 Connection con, HttpServletResponse response, PersonaBasica paciente) throws SQLException
	{
		ActionErrors errores = null;
		List<InfoResponsableCobertura> listaCoberturaServicio;
		try{
			errores = new ActionErrors();
			listaCoberturaServicio = new ArrayList<InfoResponsableCobertura>(); 
			HttpSession session=request.getSession();
			Solicitud solGeneral=(Solicitud)session.getAttribute("solicitudGeneral");
			String justificacion = (String)session.getAttribute("valorJustificacion");
			String parametroSolicitud = (String)session.getAttribute("parametroSolicitud"); 
			
			if(guardarForm.isSolPYP())
				solGeneral.setTipoSolicitud(new InfoDatosInt(3));
									
			session.removeAttribute("solicitudGeneral");
			session.removeAttribute("valorJustificacion");
			session.removeAttribute("parametroSolicitud");
			
			SolicitudInterconsulta mundoInterconsulta=new SolicitudInterconsulta ();  
	
			try
			{
				PropertyUtils.copyProperties(mundoInterconsulta, solGeneral);
				
				Log4JManager.info("-->"+mundoInterconsulta.getCodigoPaciente()) ;
			
			}
			catch (Exception e)
			{
				guardarForm.reset();
				logger.warn("-- > Error en la Copia de La Informacion de La Solicitud ");
				return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Error en GuardarInterconsultaAction", "errors.problemasDatos", true);
			}
			
			//if(guardarForm.getSeleccionManejo()!=ConstantesBD.codigoSeDeseaConceptoSolamente&&mundoInterconsulta.getOcupacionSolicitado().getCodigo()>ConstantesBD.codigoSeTransfiereManejoPaciente)
			if(guardarForm.getSeleccionManejo()!=ConstantesBD.codigoSeDeseaConceptoSolamente && 
					solGeneral.getOcupacionSolicitado().getCodigo()!=ConstantesBD.codigoOcupacionMedicaTodos && 
						solGeneral.getOcupacionSolicitado().getCodigo()!=ConstantesBD.codigoOcupacionMedicaNinguna && 
							solGeneral.getOcupacionSolicitado().getCodigo()!=Integer.parseInt(ValoresPorDefecto.getCodigoOcupacionMedicoGeneral(medico.getCodigoInstitucionInt(),true)) && 
								solGeneral.getOcupacionSolicitado().getCodigo()!=Integer.parseInt(ValoresPorDefecto.getCodigoOcupacionMedicoEspecialista(medico.getCodigoInstitucionInt(),true)))
			{
						guardarForm.reset();
						logger.warn("Para las opciones de tranferir manejo y manejo conjunto, la ocupacion a la cual se hace solicitud debe ser Médico");
						request.setAttribute("codigoDescripcionError", "error.solicitudinterconsulta.validacionSolicitudMedico");
						UtilidadBD.cerrarConexion(con);
						return mapping.findForward("paginaError");
			}
			//-Este IF se adiciono para verificar que la fecha no este vacia.
			if ( !UtilidadCadena.noEsVacio(mundoInterconsulta.getFechaSolicitud()) )
			{
				guardarForm.reset();
				logger.warn(" (LA FECHA DE LA SOLICITUD ES NULA) Error en la Copia de La Informacion de La Solicitud ");
				return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Error en GuardarInterconsultaAction", "errors.problemasDatos", true);
			}
			else
			{
					this.cargarHistoricoObjeto(mundoInterconsulta, guardarForm, medico);
					llenarMundo(guardarForm, mundoInterconsulta, medico);
					
					//Se agrega diagnóstico a la solicitud por Anexo Solicitud de Interconsulta-35 V 1.50 
			        DtoDiagnostico dtoDiagnostico = Utilidades.getDiagnosticoPacienteCuenta(paciente.getCodigoCuenta());
			        mundoInterconsulta.setDtoDiagnostico(dtoDiagnostico);
				  	guardarForm.setDtoDiagnostico(dtoDiagnostico);
																										
					//Inserta la Interconsulta
					int resp = mundoInterconsulta.Insertar_Interconsulta_Transaccional(con, "empezar", medico, paciente,listaCoberturaServicio);
					if (resp == -1)
					{
					    return ComunAction.accionSalirCasoError(mapping, request, con, logger, "SolicitudMismoServicio", "error.validacionessolicitud.interpretar.solicitudInterconsultaIgualServicio", true);
					}
					
					String generaAlertaEnfermeria = (String)session.getAttribute("generaAlertaEnfermeria");
					
					/**
		             * Se inserta el registro de alerta para registro de enfermería MT-3438
		             */
					if (UtilidadValidacion.esMedico(medico).equals("") && 
							!UtilidadTexto.isEmpty(generaAlertaEnfermeria) && 
							generaAlertaEnfermeria.equals(ConstantesBD.acronimoSi)) {
						OrdenMedica ordenMedica = new OrdenMedica();
						ordenMedica.registrarAlertaOrdenMedica(con, 
								ConstantesBD.seccionInterconsultas, 
								new Long(paciente.getCodigoCuenta()), medico.getLoginUsuario());
					}
					
					session.removeAttribute("generaAlertaEnfermeria");
					/*
					Vector justificacion=solGeneral.getJustificacion();
					if(justificacion!=null && mundoInterconsulta.getCentroCostoSolicitado().getCodigo()!=ConstantesBD.codigoCentroCostoExternos)
					{
						for(int i=0; i<justificacion.size(); i++)
						{
							Vector atributo=(Vector)justificacion.elementAt(i);
							if(Utilidades.verificarAutorizacion(con,((Integer)atributo.elementAt(0)).intValue()))
							{
							    mundoInterconsulta.actualizarNumeroAutorizacionTransaccional(con, ((String)atributo.elementAt(1)), mundoInterconsulta.getNumeroSolicitud(), ConstantesBD.continuarTransaccion);
							}
							else
							{
							    mundoInterconsulta.ingresarAtributoTransaccional(con, mundoInterconsulta.getCodigoServicioSolicitado(), ((Integer)atributo.elementAt(0)).intValue(), ((String)atributo.elementAt(1)), ConstantesBD.continuarTransaccion);  
							}
						}
					}*/
					
					if(guardarForm.getSeleccionManejo()==ConstantesBD.codigoSeTransfiereManejoPaciente)
						{
						mundoInterconsulta.cambiarSolicitudMedicoTratante(con,"finalizar");
						}
					else
					{
						(DaoFactory.getDaoFactory(System.getProperty("TIPOBD"))).endTransaction(con);
					}
	
					guardarForm.setNumeroSolicitud(mundoInterconsulta.getNumeroSolicitud());
					guardarForm.setCentroCostoSolicitado(String.valueOf(mundoInterconsulta.getCentroCostoSolicitado().getCodigo()));
					guardarForm.setEstadoHistoriaClinica(mundoInterconsulta.getEstadoHistoriaClinica());									
					guardarForm.setInfoCoberturaServicio(listaCoberturaServicio);
					
					
					//SI EL INDICADOR DE JUSTIFICAR ES VERDADERO O PENDIENTE SE HACE EL INGRESO DE LA JUSTIFICACIï¿½N NO POS
					FormatoJustServNopos fjsn = new FormatoJustServNopos();
	            		
						if(justificacion.toString().equals("true")){
							HashMap justificacionMap=(HashMap) request.getSession().getAttribute(parametroSolicitud+"MAPAJUSSERV");
							
		                	fjsn.ingresarJustificacion(
		                    		con,
		                    		medico.getCodigoInstitucionInt(), 
		                    		medico.getLoginUsuario(), 
		                    		justificacionMap, 
		                    		mundoInterconsulta.getNumeroSolicitud(),
		                    		ConstantesBD.codigoNuncaValido,
		                    		Integer.parseInt(parametroSolicitud.toString()),
		                    		medico.getCodigoPersona());
				}
	
				//Se captura la excepcion para no bloquear el flujo
				try {
					//VALIDACIONES PARA GENERACION DE AUTORIZACION O ASOCIACION CON LA/LAS NUEVAS SOLICITUDES
					cargarInfoVerificarGeneracionAutorizacion(con, guardarForm, medico, paciente,errores);
					saveErrors(request, errores);
				}  catch (IPSException e) {
					Log4JManager.error(e);
					ActionMessages mensajeError = new ActionMessages();
					mensajeError.add("", new ActionMessage(String.valueOf(e.getErrorCode())));
					saveErrors(request, mensajeError);
				}
		
				this.cerrarConexion(con);									
				return mapping.findForward("funcionalidadResumenInterconsulta");
			}
		}catch(SQLException e) {
			UtilidadBD.abortarTransaccion(con);
			Log4JManager.error("Error en accionSalirProcedimiento: ", e);
		} catch (Exception e) {
			UtilidadBD.abortarTransaccion(con);
			Log4JManager.error(e.getMessage(),e);
		}
		return null;
	}

	/**
	 * Este método especifica las acciones a realizar en el estado
	 * resumen
	 * @param guardarForm GuardarInterconsultaForm
	 * @param mapping Mapping para manejar la navegación
	 * @param con Conexión con la fuente de datos
	 * @return ActionForward a la página de resumen "resumenGuardarInterconsulta.jsp"
	 * @throws SQLException
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private ActionForward accionResumen(GuardarInterconsultaForm guardarForm, ActionMapping mapping, 
			HttpServletRequest request, Connection con, UsuarioBasico medico) throws SQLException
	{			
				SolicitudInterconsulta mundoInterconsulta=new SolicitudInterconsulta ();  	
				boolean validarCargar=mundoInterconsulta.cargar(con,guardarForm.getNumeroSolicitud());
				
				if(validarCargar)
				{
						guardarForm.resetNuevosQueModifican();
			
						if(mundoInterconsulta.getNombreOtros()==null||mundoInterconsulta.getNombreOtros().equals(""))
						{
								mundoInterconsulta.cargarCodigoServicioSolicitadoInterconsulta(con,guardarForm.getNumeroSolicitud());
								mundoInterconsulta.cargarDiasTramiteServicioSolicitado(con, mundoInterconsulta.getCodigoServicioSolicitado(), mundoInterconsulta.getUrgente());
						}		
						
						mundoInterconsulta.cargarMotivoAnulacionInterconsulta(con,guardarForm.getNumeroSolicitud());
						llenarForm(con, guardarForm,mundoInterconsulta, medico);
						this.cargarObservacionesFormResumen(mundoInterconsulta, guardarForm);
						
						this.cerrarConexion(con);
						
						return mapping.findForward("paginaResumenInterconsulta");
				}
				else
				{
						logger.warn("Número de solicitud inválido "+guardarForm.getNumeroSolicitud());
						this.cerrarConexion(con);
						guardarForm.reset();
						ArrayList atributosError = new ArrayList();
						atributosError.add("El número de solicitud");
						request.setAttribute("codigoDescripcionError", "errors.invalid");				
						request.setAttribute("atributosError", atributosError);
						return mapping.findForward("paginaError");		
				}
	}
	
	/**
	 * Este método especifica las acciones a realizar en el estado
	 * modificar interconsulta
	 * @param guardarForm GuardarInterconsultaForm
	 * @param mapping Mapping para manejar la navegación
	 * @param con Conexión con la fuente de datos
	 * @param medico UsuarioBasico    (para validaciones)
	 * @param paciente PersonaBasica (para validaciones)
	 * @return ActionForward  a la página "modificarSolicitudInterconsulta.jsp"
	 * @throws SQLException
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private ActionForward accionModificar(GuardarInterconsultaForm guardarForm, 
																	HttpServletRequest request, 
																	ActionMapping mapping, 
																	UsuarioBasico medico,
																	PersonaBasica paciente,
																	Connection con) throws SQLException
	{			
				SolicitudInterconsulta mundoInterconsulta=new SolicitudInterconsulta ();  
				
				/////toca obtener el numero de la solicitud de lo que nos entregue el request del listado
				guardarForm.resetNuevosQueModifican();
				mundoInterconsulta.clean();
				mundoInterconsulta.cargar(con,guardarForm.getNumeroSolicitud());														
				
				if(mundoInterconsulta.getNombreOtros()==null||mundoInterconsulta.getNombreOtros().equals(""))
				{
					mundoInterconsulta.cargarCodigoServicioSolicitadoInterconsulta(con,guardarForm.getNumeroSolicitud());
				}
				
				llenarForm(con, guardarForm,mundoInterconsulta, medico);
			
				ValidacionesSolicitud validaciones = new ValidacionesSolicitud(con, guardarForm.getNumeroSolicitud(), medico, paciente);
				ResultadoBoolean permisosModificar = validaciones.puedoModificarSolicitudSolicitada();
				
				/*Validar si el numero de solicitud es correcto*/
				if( guardarForm.getNumeroSolicitud() < 1 )
				{
							logger.warn("Número de solicitud inválido "+guardarForm.getNumeroSolicitud());
							this.cerrarConexion(con);
							guardarForm.reset();
							ArrayList atributosError = new ArrayList();
							atributosError.add("El número de solicitud");
							request.setAttribute("codigoDescripcionError", "errors.invalid");				
							request.setAttribute("atributosError", atributosError);
							return mapping.findForward("paginaError");				
				}
				
				/*validar si tiene permisos para modificar*/
				if(!permisosModificar.isTrue())
				{
							this.cerrarConexion(con);
							request.setAttribute("codigoDescripcionError", permisosModificar.getDescripcion());
							return mapping.findForward("paginaError");									
				}
				else
				{
							this.cerrarConexion(con);
							return mapping.findForward("paginaModificarInterconsulta");
				}
	}

	/**
	 * Este método especifica las acciones a realizar en el estado
	 * guardarModificacion
	 * @param guardarForm GuardarInterconsultaForm
	 * @param mapping Mapping para manejar la navegación
	 * @param medico UsuarioBasico para cargar el historico 
	 * 				y obtener el código de éste cuando genera una 
	 * 				anulación
	 * @param con Conexión con la fuente de datos
	 * @return ActionForward "guardarInterconsulta.do?estado=modificar"
	 * @throws SQLException
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private ActionForward accionGuardarModificacion(
			GuardarInterconsultaForm guardarForm,
			ActionMapping mapping,
			HttpServletRequest request,
			UsuarioBasico medico, 
			Connection con, PersonaBasica paciente)	throws Exception
	{
		SolicitudInterconsulta mundoInterconsulta=null;
		ActionMessages errores = new ActionMessages();
		try{
			
			mundoInterconsulta	= new SolicitudInterconsulta ();
			String codigoOrden  = ConstantesBD.codigoNuncaValido+"";
			boolean modificacionSolicitud = false;
			mundoInterconsulta.setNumeroSolicitud(guardarForm.getNumeroSolicitud());
			if(!guardarForm.getAnularSolicitud())
			{
				mundoInterconsulta.setMotivoSolicitudNueva(guardarForm.getMotivoSolicitudNueva());					
				mundoInterconsulta.setResumenHistoriaClinicaNueva(guardarForm.getResumenHistoriaClinicaNueva());
				mundoInterconsulta.setComentarioNuevo(guardarForm.getComentarioNuevo());
			
			//mundoInterconsulta.setNumeroAutorizacion(guardarForm.getNumeroAutorizacion());
			
				if(!guardarForm.getJustificacionSolicitudNueva().trim().equals(""))
					mundoInterconsulta.setJustificacionSolicitudNueva(UtilidadTexto.agregarTextoAObservacionFechaGrabacion(guardarForm.getJustificacionSolicitud(),guardarForm.getJustificacionSolicitudNueva(),medico,false));
				else
					mundoInterconsulta.setJustificacionSolicitudNueva("");
			}
			
			mundoInterconsulta.setMotivoAnulacion(guardarForm.getMotivoAnulacion());
			mundoInterconsulta.setCodigoMedicoAnulacion(medico.getCodigoPersona());
			this.cargarHistoricoObjeto(mundoInterconsulta, guardarForm, medico);
			
			mundoInterconsulta.getDocumentosAdjuntos().cargarDocumentosAdjuntos(con,guardarForm.getNumeroSolicitud(),true, "");
			int numAdjuntos = guardarForm.getNumDocumentosAdjuntos();
			mundoInterconsulta.setNumDocumentosAdjuntos(numAdjuntos);
	
			if(!guardarForm.getMotivoSolicitudNueva().trim().equals("") ||
					!guardarForm.getResumenHistoriaClinicaNueva().trim().equals("") ||
					!guardarForm.getComentarioNuevo().trim().equals("") ||
					!guardarForm.getJustificacionSolicitudNueva().trim().equals("") ||
					!guardarForm.getMotivoAnulacion().trim().equals("") ||
					numAdjuntos > mundoInterconsulta.getDocumentosAdjuntos().getNumDocumentosAdjuntos()) {
				modificacionSolicitud = true;
			}
			
			
			for( int i=0; i<numAdjuntos; i++ )
			{
				String nombre = (String)guardarForm.getDocumentoAdjuntoGenerado(""+i);
				String checkbox = (String)guardarForm.getDocumentoAdjuntoGenerado("checkbox_"+i);
										
				if( nombre != null && checkbox != null && !checkbox.equals("off") )
				{
					String[] nombres = nombre.split("@");
					if( nombres.length == 2 )
					{
						String codigoStr = (String)guardarForm.getDocumentoAdjuntoGenerado("codigo_"+i);
						int codigo = 0;
	
						if( UtilidadCadena.noEsVacio(codigoStr) )
							codigo = Integer.parseInt(codigoStr);
	
						DocumentoAdjunto documentoAdjunto= new DocumentoAdjunto(nombres[1],nombres[0],true,codigo,medico.getCodigoPersona(),"");  
						mundoInterconsulta.addDocumentoAdjunto(documentoAdjunto);
					}
				}
						
				else
				if( nombre != null && checkbox != null && checkbox.equals("off") )
				{	
					String[] nombres = nombre.split("@");
					String codigoStr = (String)guardarForm.getDocumentoAdjuntoGenerado("codigo_"+i);
					int codigo = 0;
	
					if( UtilidadCadena.noEsVacio(codigoStr) )
						codigo = Integer.parseInt(codigoStr);
	
					if( nombres.length == 2 )
					{
						DocumentoAdjunto documentoAdjunto= new DocumentoAdjunto(nombres[1],nombres[0],true,codigo, false);  
						mundoInterconsulta.addDocumentoAdjunto(documentoAdjunto);
					}		
					
					modificacionSolicitud = true;
				}										
			}						
			
			mundoInterconsulta.getDocumentosAdjuntos().insertarEliminarDocumentosAdjuntosTransaccional(con,guardarForm.getNumeroSolicitud(),"continuar");
			
			if (!guardarForm.getAnularSolicitud())
			{
				mundoInterconsulta.Modificar_Interconsulta(con);
				
				//actualiza la justificacion de la solicitud, en caso de que exista cambio
				if(!mundoInterconsulta.getJustificacionSolicitudNueva().trim().equals(""))
					Solicitud.actualizarJustificacionSolicitud(con, mundoInterconsulta.getJustificacionSolicitudNueva(),mundoInterconsulta.getNumeroSolicitud()+"");
			}
			else
			{
			  /**FIXME INICIO ANULACION SOLICITUD Y AUTORIZACION----------------------------------------------------*/
				int resp = mundoInterconsulta.ModificarInterconsultaTransaccional(con, "empezar");
			
				/*if(resp>1){
					//Se Cambia el estado de este metodo para que no haga commit todavia hasta q' anule la autorizacion
					mundoInterconsulta.anularSolicitudTransaccional(con, "continuar");
				}else{
					mundoInterconsulta.anularSolicitudTransaccional(con, "empezar");
				}*/
				
				//Se hace commit de la anulacion de la solicitud
				//mundoInterconsulta.cerrarTransaccionAnularSolicitudTransaccional(con, "finalizar");
				
				//Validaciones para la solicitud que se anula, si esta asociada a una autorizacion
				cargarInfoParaAnulacionAutorizacion(mundoInterconsulta,medico);
			  /**FIN ANULACION SOLICITUD Y AUTORIZACION-------------------------------------------------------*/
				
				//actualiza la justificacion de la solicitud en caso de que exista cambio
				if(!mundoInterconsulta.getJustificacionSolicitudNueva().trim().equals("")){
				Solicitud.actualizarJustificacionSolicitud(con, mundoInterconsulta.getJustificacionSolicitudNueva(),mundoInterconsulta.getNumeroSolicitud()+"");
				}

				///cambio en pyp y ordenes ambulatorias////
		        if(Utilidades.esSolicitudPYP(con,guardarForm.getNumeroSolicitud()))
		        {
		        	String codActProg=Utilidades.obtenerCodigoActividadProgramaPypPacienteDadaSolicitud(con,guardarForm.getNumeroSolicitud());
		        	if(Integer.parseInt(Utilidades.obtenerCodigoOrdenAmbulatoriaActividad(con,guardarForm.getNumeroSolicitud()+""))>0)
		        	{
		        		Utilidades.actualizarEstadoActividadProgramaPypPaciente(con,codActProg,ConstantesBD.codigoEstadoProgramaPYPProgramado,medico.getLoginUsuario(),"");
		        		Utilidades.asignarSolicitudToActividadPYP(con,Utilidades.obtenerCodigoOrdenAmbulatoriaActividad(con,guardarForm.getNumeroSolicitud()+""),guardarForm.getNumeroSolicitud()+"");
		        	}
		        	else
		        	{
		        		Utilidades.actualizarEstadoActividadProgramaPypPaciente(con,codActProg,ConstantesBD.codigoEstadoProgramaPYPCancelado,medico.getLoginUsuario(),"SOLICITUD CANCELADA");
		        	}
		        }
		        //si la solicitud es de orden ambulatoria
		        codigoOrden = Utilidades.obtenerCodigoOrdenAmbulatoria(con,guardarForm.getNumeroSolicitud()+"");
		        if(Integer.parseInt(codigoOrden)>0)
		    	{
		        	OrdenesAmbulatorias.actualizarEstadoOrdenAmbulatoria(con,codigoOrden,ConstantesBD.codigoEstadoOrdenAmbulatoriaPendiente);
		        	HashMap campos=new HashMap();
		        	campos.put("estadoOrden",ConstantesBD.codigoEstadoOrdenAmbulatoriaPendiente);
		        	campos.put("numeroSolicitud",ConstantesBD.codigoNuncaValido);
		        	campos.put("numeroOrden",codigoOrden);
		        	OrdenesAmbulatorias.actualizarSolicitudEnOrdenAmbulatoria(con,campos);
		    	}
		        ///fin cambio en pyp y ordenes ambulatorias////
			}
			
			/**
	         * Se inserta el registro de alerta para registro de enfermería MT-3438
	         */
			if (modificacionSolicitud && UtilidadValidacion.esMedico(medico).equals("") && 
					Integer.parseInt(codigoOrden) <= 0) {
				OrdenMedica ordenMedica = new OrdenMedica();
				ordenMedica.registrarAlertaOrdenMedica(con, 
						ConstantesBD.seccionInterconsultas, 
						new Long(paciente.getCodigoCuenta()), medico.getLoginUsuario());
			}
			
			this.cerrarConexion(con);
			
		}catch(IPSException ipse){
			mundoInterconsulta.cerrarTransaccionAnularSolicitudTransaccional(con, "abortar");
			errores.add("ERROR Negocio", new ActionMessage(ipse.getErrorCode().toString()));
			if(!errores.isEmpty()){
				saveErrors(request, errores);
			}
			return mapping.findForward("paginaModificarInterconsulta");
		}
		catch(Exception e){
			Log4JManager.error(e);
			errores.add("ERROR no Controlado", new ActionMessage("errors.notEspecific", e.getMessage()));
		}
		if(!errores.isEmpty()){
			saveErrors(request, errores);
		}
		return mapping.findForward("funcionalidadResumenInterconsulta");
	}
	
				
	/**
	 * Método que carga los datos pertinentes desde el 
	 * form GuardarInterconsultaForm para el
	 * mundo de interconsulta SolicitudInterconsulta
	 * @param guardarForm GuardarInterconsultaForm (forma)
	 * @param mundoInterconsulta SolicitudInterconsulta (mundo)
	 */
	protected void llenarMundo(GuardarInterconsultaForm guardarForm, SolicitudInterconsulta mundoInterconsulta,UsuarioBasico medico)
	{
		mundoInterconsulta.setCodigoServicioSolicitado(guardarForm.getCodigoServicioInterconsulta());		
		mundoInterconsulta.setNombreOtros(guardarForm.getNombreOtros());
		mundoInterconsulta.setNombreCodigoServicoSolicitado(guardarForm.getNombreCodigoServicioSolicitado());
		
		mundoInterconsulta.setMotivoSolicitud(guardarForm.getMotivoSolicitud());
		mundoInterconsulta.setResumenHistoriaClinica(guardarForm.getResumenHistoriaClinica());
		mundoInterconsulta.setComentario(guardarForm.getComentario());
		mundoInterconsulta.setcodigoManejointerconsulta(guardarForm.getSeleccionManejo());
				
		int numAdjuntos = guardarForm.getNumDocumentosAdjuntos();
		mundoInterconsulta.setNumDocumentosAdjuntos(numAdjuntos);

		for( int i=0; i<numAdjuntos; i++ )
		{
			String nombre = (String)guardarForm.getDocumentoAdjuntoGenerado(""+i);
			String checkbox = (String)guardarForm.getDocumentoAdjuntoGenerado("checkbox_"+i);
	
			if( nombre != null && checkbox != null && !checkbox.equals("off") )
			{
				String[] nombres = nombre.split("@");
				String codigoStr = (String)guardarForm.getDocumentoAdjuntoGenerado("codigo_"+i);
				int codigo = 0;
	
				if( UtilidadCadena.noEsVacio(codigoStr) )
					codigo = Integer.parseInt(codigoStr);

				if( nombres.length == 2 )
				{
					DocumentoAdjunto documentoAdjunto= new DocumentoAdjunto(nombres[1],nombres[0],true,codigo,medico.getCodigoPersona(), "");  
					mundoInterconsulta.addDocumentoAdjunto(documentoAdjunto);
				}
			}
		}							
	}
	
	
	/**
	 * Este método carga los datos pertinentes a la forma 
	 * @param guardarForm (form)
	 * @param mundoInterconsulta (mundo)
	 */
	protected void llenarForm(Connection con, GuardarInterconsultaForm guardarForm, SolicitudInterconsulta mundoInterconsulta, UsuarioBasico medico)
	{		
			
			guardarForm.setNombreCodigoServicioSolicitado(mundoInterconsulta.getNombreCodigoServicoSolicitado());			
			guardarForm.setCodigoServicioInterconsulta(mundoInterconsulta.getCodigoServicioSolicitado());
			guardarForm.setFechaSolicitud(UtilidadFecha.conversionFormatoFechaAAp(mundoInterconsulta.getFechaSolicitud()));
			guardarForm.setHoraSolicitud(UtilidadFecha.convertirHoraACincoCaracteres(mundoInterconsulta.getHoraSolicitud()));
			//guardarForm.setNumeroAutorizacion(mundoInterconsulta.getNumeroAutorizacion());
			guardarForm.setEspecialidadSolicitante(mundoInterconsulta.getEspecialidadSolicitante().getNombre());
			guardarForm.setOcupacionSolicitada(mundoInterconsulta.getOcupacionSolicitado().getNombre());
			guardarForm.setCodigoOcupacionSolicitada(mundoInterconsulta.getOcupacionSolicitado().getCodigo()+"");
			guardarForm.setCentroCostoSolicitado(mundoInterconsulta.getCentroCostoSolicitado().getNombre());
			guardarForm.setNombreOtros(mundoInterconsulta.getNombreOtros());
			guardarForm.setFechaGrabacion(UtilidadFecha.conversionFormatoFechaAAp(mundoInterconsulta.getFechaGrabacion()));
			guardarForm.setHoraGrabacion(UtilidadFecha.convertirHoraACincoCaracteres(mundoInterconsulta.getHoraGrabacion()));
			guardarForm.setCentroCostoSolicitante(mundoInterconsulta.getCentroCostoSolicitante().getNombre());
			guardarForm.setUrgente(mundoInterconsulta.isUrgente());
			guardarForm.setSeleccionManejo(mundoInterconsulta.getSeleccionManejo().getCodigo());
			
			guardarForm.setAcronimoDiasTramite(mundoInterconsulta.getAcronimoDiasTramite());
			
			guardarForm.setMotivoSolicitud(mundoInterconsulta.getMotivoSolicitud());
			guardarForm.setResumenHistoriaClinica(mundoInterconsulta.getResumenHistoriaClinica());
			guardarForm.setComentario(mundoInterconsulta.getComentario());
			guardarForm.setJustificacionSolicitud(mundoInterconsulta.getJustificacionSolicitud());
			guardarForm.setNumeroSolicitud(mundoInterconsulta.getNumeroSolicitud());
			guardarForm.setConsecutivoOrdenesMedicas(mundoInterconsulta.getConsecutivoOrdenesMedicas());
			guardarForm.setMotivoAnulacion(mundoInterconsulta.getMotivoAnulacion());
			guardarForm.setNumDocumentosAdjuntos(0);
			guardarForm.setEstadoHistoriaClinica(mundoInterconsulta.getEstadoHistoriaClinica());
			
			ResultadoBoolean resultado = mundoInterconsulta.getDocumentosAdjuntos().cargarDocumentosAdjuntos(con, mundoInterconsulta.getNumeroSolicitud(), true, "");
			if(!resultado.isTrue())
			{
				if(UtilidadCadena.noEsVacio(resultado.getDescripcion()))
				{
					logger.warn("Error cargando los documentos adjuntos: "+resultado.getDescripcion());
				}
			}
			else
			{
				int numDocumentosAdjuntos=mundoInterconsulta.getDocumentosAdjuntos().getNumDocumentosAdjuntos();
				guardarForm.setNumDocumentosAdjuntos(numDocumentosAdjuntos);
				
				for(int i=0;i<numDocumentosAdjuntos;i++)
				{
					DocumentoAdjunto documento = mundoInterconsulta.getDocumentosAdjuntos().getDocumentoAdjunto(i);
					guardarForm.setDocumentoAdjuntoGenerado("checkbox_"+i, "on");
					guardarForm.setDocumentoAdjuntoGenerado("original_"+i, documento.getNombreOriginal());
					guardarForm.setDocumentoAdjuntoGenerado("generado_"+i, documento.getNombreGenerado());
					guardarForm.setDocumentoAdjuntoGenerado("codigo_"+i, documento.getCodigoArchivo()+"");
					if(medico.getCodigoPersona()==documento.getCodigoMedico()){
						guardarForm.setDocumentoAdjuntoGenerado("codigomedico_"+i, "on");
					}else{
						guardarForm.setDocumentoAdjuntoGenerado("codigomedico_"+i, "off");
					}
					guardarForm.setDocumentoAdjuntoGenerado(""+i, documento.getNombreGenerado()+"@"+documento.getNombreOriginal());								
				}
			}
	}
	
	protected void llenarFormConCuenta(GuardarInterconsultaForm guardarForm, SolicitudInterconsulta mundoInterconsulta)
	{
			guardarForm.setCodigoCuenta(mundoInterconsulta.getCodigoCuenta());
	}
	
	/**
	 * Método que implementa las validaciones generales de entrada 
	 * @param map ActionMapping
	 * @param req HttpServletRequest
	 * @return ActionForward a la paginaError si existe un acceso errado
	 */
	protected ActionForward validacionesUsuario(ActionMapping map, HttpServletRequest req)
	{
			HttpSession session=req.getSession();
			PersonaBasica paciente = (PersonaBasica)session.getAttribute("pacienteActivo");
			UsuarioBasico medico = (UsuarioBasico)session.getAttribute("usuarioBasico");

			if(medico == null)
			{
					logger.warn("Profesional de la salud no válido (null)");			
					req.setAttribute("codigoDescripcionError", "errors.usuario.noCargado");
					return map.findForward("paginaError");
			}
			else
			if( !UtilidadValidacion.esProfesionalSalud(medico) )
			{
					req.setAttribute("codigoDescripcionError", "errors.usuario.noAutorizado");
					return map.findForward("paginaError");
			}		
			else 
			if( paciente == null || paciente.getTipoIdentificacionPersona().equals("") )
			{
					logger.warn("El paciente no es válido (null)");
					req.setAttribute("codigoDescripcionError", "errors.paciente.noCargado");
					return map.findForward("paginaError");
			}
		return null;
	}
	
	/**
	 * Método en que se cierra la conexión (Buen manejo
	 * recursos), usado ante todo al momento de hacer
	 * un forward
	 * @param con Conexión con la fuente de datos
	 * @throws SQLException
	 */
	public void cerrarConexion (Connection con) throws SQLException
	{
			if (con!=null&&!con.isClosed())
			{
				UtilidadBD.closeConnection(con);
			}
	}
	
	/**
	 * Método para insertar saltos de línea
	 * para la vista en el estado resumen
	 * @param mundoInterconsulta
	 * @param guardarForm
 	*/	
	protected void cargarObservacionesFormResumen(SolicitudInterconsulta mundoInterconsulta, GuardarInterconsultaForm guardarForm)
	{
			guardarForm.setMotivoSolicitud((mundoInterconsulta.getMotivoSolicitud()).replaceAll("\n", "<br>"));
			guardarForm.setResumenHistoriaClinica((mundoInterconsulta.getResumenHistoriaClinica()).replaceAll("\n", "<br>"));
			guardarForm.setComentario((mundoInterconsulta.getComentario()).replaceAll("\n", "<br>"));
			if(mundoInterconsulta.getMotivoAnulacion() != null && !mundoInterconsulta.getMotivoAnulacion().equals(""))
				guardarForm.setMotivoAnulacion((mundoInterconsulta.getMotivoAnulacion()).replaceAll("\n", "<br>"));
	}
	
	/**
	 * Método para insertar el historico
	 * @param mundoInterconsulta para pre-llenar datos
	 * @param guardarForm para obtener datos
	 * @param medico UsuarioBasico  para insertar historico del medico
	 */			
	protected void cargarHistoricoObjeto(SolicitudInterconsulta mundoInterconsulta, GuardarInterconsultaForm guardarForm, UsuarioBasico medico)
	{
				String historico;
				String identificacionMedico = medico.getNombreUsuario() +"  "+medico.getNumeroRegistroMedico() + ".   ";
				
				identificacionMedico += medico.getEspecialidadesMedico();
			
			if(!guardarForm.getAnularSolicitud())
			{		
				if( guardarForm.getMotivoSolicitudNueva() != null && !guardarForm.getMotivoSolicitudNueva().equals("") )
				{
							historico = new String();
							historico = guardarForm.getMotivoSolicitud().replaceAll("<br>", "\n");
							historico += "\n"+UtilidadFecha.getFechaActual()+"  "+UtilidadFecha.getHoraActual();
							historico += "\n"+guardarForm.getMotivoSolicitudNueva();
							historico += "\n"+identificacionMedico+"\n\n";
							mundoInterconsulta.setMotivoSolicitudNueva(historico);
				}
				else
				{
							mundoInterconsulta.setMotivoSolicitudNueva(guardarForm.getMotivoSolicitud());
				}

				if( guardarForm.getResumenHistoriaClinicaNueva() != null && !guardarForm.getResumenHistoriaClinicaNueva().equals(""))
				{
							historico = new String();
							historico = guardarForm.getResumenHistoriaClinica().replaceAll("<br>", "\n");
							historico += "\n"+UtilidadFecha.getFechaActual()+"  "+UtilidadFecha.getHoraActual();
							historico += "\n"+guardarForm.getResumenHistoriaClinicaNueva();
							historico += "\n"+identificacionMedico+"\n\n";
							mundoInterconsulta.setResumenHistoriaClinicaNueva(historico);
				}
				else
				{
							mundoInterconsulta.setResumenHistoriaClinicaNueva(guardarForm.getResumenHistoriaClinica());
				}
				
				
				if( guardarForm.getMotivoAnulacion()!= null && !guardarForm.getMotivoAnulacion().equals(""))
				{
							historico = new String();
							historico = UtilidadFecha.getFechaActual()+"  "+UtilidadFecha.getHoraActual();
							historico += "\n"+guardarForm.getMotivoAnulacion();
							historico += "\n"+identificacionMedico+"\n\n";
							mundoInterconsulta.setMotivoAnulacion(historico);
				}
				
				if( guardarForm.getMotivoSolicitud() != null && !guardarForm.getMotivoSolicitud().equals("") )
				{
							historico = new String();
							historico = UtilidadFecha.getFechaActual()+"  "+UtilidadFecha.getHoraActual();
							historico += "\n"+guardarForm.getMotivoSolicitud();
							historico += "\n"+identificacionMedico+"\n\n";
							guardarForm.setMotivoSolicitud(historico);
				}
				
				if( guardarForm.getResumenHistoriaClinica() != null && !guardarForm.getResumenHistoriaClinica().equals("") )
				{
							historico = new String();
							historico = UtilidadFecha.getFechaActual()+"  "+UtilidadFecha.getHoraActual();
							historico += "\n"+guardarForm.getResumenHistoriaClinica();
							historico += "\n"+identificacionMedico+"\n\n";
							guardarForm.setResumenHistoriaClinica(historico);
				}
				
				if( guardarForm.getComentarioNuevo()!= null && !guardarForm.getComentarioNuevo().equals(""))
				{
							historico = new String();
							historico = guardarForm.getComentario().replaceAll("<br>", "\n");
							historico += "\n"+UtilidadFecha.getFechaActual()+"  "+UtilidadFecha.getHoraActual();
							historico += "\n"+guardarForm.getComentarioNuevo();
							historico += "\n"+identificacionMedico+"\n\n";
							mundoInterconsulta.setComentarioNuevo(historico);
				}
				
				if( guardarForm.getComentario() != null && !guardarForm.getComentario().equals("") )
				{
							historico = new String();
							historico = UtilidadFecha.getFechaActual()+"  "+UtilidadFecha.getHoraActual();
							historico += "\n"+guardarForm.getComentario();
							historico += "\n"+identificacionMedico+"\n\n";
							guardarForm.setComentario(historico);
				}
			}
	}
	
	/**
	 * Método que se encarga de validar si se generar autorizacion para el servicio de la orden.
	 * DCU 35 - Solicitud de Interconsulta v2.1
	 * MT 4681
	 * 
	 * @author Camilo Gomez
	 * @param con
	 * @param InterconsultasForm
	 * @param usuario
	 * @param errores
	 * @throws IPSException
	 */
	private void cargarInfoVerificarGeneracionAutorizacion(Connection con,GuardarInterconsultaForm interForm,
			UsuarioBasico usuario, PersonaBasica paciente, ActionErrors errores)throws IPSException
	{
		DtoSubCuentas dtoSubCuenta = null;
		AutorizacionCapitacionDto autorizacionCapitacionDto = null;
		MontoCobroDto montoCobroAutorizacion				= null;
		DatosPacienteAutorizacionDto datosPacienteAutorizar = null;
		OrdenAutorizacionDto ordenAutorizacionDto 			= null;
		List<OrdenAutorizacionDto> listaOrdenesAutorizar	= null;
		List<ServicioAutorizacionOrdenDto> listaServiciosPorAutorizar = null;
		ContratoDto contratoDto = null;
		ConvenioDto convenioDto = null;
		DtoSolicitudesSubCuenta dtoSolicitudSubCuenta = null; 
		ManejoPacienteFacade manejoPacienteFacade = null;
		OrdenesFacade ordenesFacade	= null;
		List<AutorizacionCapitacionDto> listaAutorizacionCapitacion = null;
		try{
			UtilidadBD.iniciarTransaccion(con);
			listaOrdenesAutorizar 	= new ArrayList<OrdenAutorizacionDto>();
			ordenesFacade			= new OrdenesFacade();
			
			dtoSubCuenta =  interForm.getInfoCoberturaServicio().get(0).getDtoSubCuenta();
			
			for(InfoResponsableCobertura infoCoberturaSer : interForm.getInfoCoberturaServicio()){
				
				dtoSolicitudSubCuenta = infoCoberturaSer.getDtoSubCuenta().getSolicitudesSubcuenta().get(0);
				
				convenioDto = new ConvenioDto();
				convenioDto.setCodigo(infoCoberturaSer.getDtoSubCuenta().getConvenio().getCodigo());
				convenioDto.setNombre(infoCoberturaSer.getDtoSubCuenta().getConvenio().getNombre());
				if(infoCoberturaSer.getDtoSubCuenta().getDatosConvenio().getManejaPreupuestoCapitacion()!=null &&
						infoCoberturaSer.getDtoSubCuenta().getDatosConvenio().getManejaPreupuestoCapitacion().equals(ConstantesBD.acronimoSiChar+"")){
					convenioDto.setConvenioManejaPresupuesto(true);
				}else{
					convenioDto.setConvenioManejaPresupuesto(false);
				}
				contratoDto = new ContratoDto();
				contratoDto.setConvenio(convenioDto);
				contratoDto.setCodigo(infoCoberturaSer.getDtoSubCuenta().getContrato());
				contratoDto.setNumero(infoCoberturaSer.getDtoSubCuenta().getNumeroContrato());
				
				ordenAutorizacionDto = new OrdenAutorizacionDto();
				ordenAutorizacionDto.setCodigoOrden(Utilidades.convertirALong(interForm.getNumeroSolicitud()+""));
				ordenAutorizacionDto.setConsecutivoOrden(dtoSolicitudSubCuenta.getConsecutivoSolicitud());
				ordenAutorizacionDto.setContrato(contratoDto);
				ordenAutorizacionDto.setCodigoCentroCostoEjecuta(Utilidades.convertirAEntero(interForm.getCentroCostoSolicitado()));
				ordenAutorizacionDto.setEsPyp(interForm.isSolPYP());
				ordenAutorizacionDto.setCodigoViaIngreso(paciente.getCodigoUltimaViaIngreso());
				ordenAutorizacionDto.setClaseOrden(ConstantesBD.claseOrdenOrdenMedica);
				ordenAutorizacionDto.setTipoOrden(ConstantesBD.codigoTipoSolicitudInterconsulta);
				
				//Se consultan datos del servicio
				listaServiciosPorAutorizar = null;
				listaServiciosPorAutorizar = ordenesFacade.obtenerServiciosPorAutorizar(Utilidades.convertirAEntero(ordenAutorizacionDto.getCodigoOrden()+""),
						ordenAutorizacionDto.getClaseOrden(), ordenAutorizacionDto.getTipoOrden());
				listaServiciosPorAutorizar.get(0).setFinalidad(dtoSolicitudSubCuenta.getFinalidadSolicitud());
				long cantidad = 1;
				listaServiciosPorAutorizar.get(0).setCantidad(cantidad);
				listaServiciosPorAutorizar.get(0).setAutorizar(true);
				if(dtoSolicitudSubCuenta.isUrgenteSolicitud()){
					ordenAutorizacionDto.setEsUrgente(true);
					listaServiciosPorAutorizar.get(0).setUrgente(ConstantesBD.acronimoSiChar);
				}else{
					ordenAutorizacionDto.setEsUrgente(false);
					listaServiciosPorAutorizar.get(0).setUrgente(ConstantesBD.acronimoNoChar);
				}
				
				ordenAutorizacionDto.setServiciosPorAutorizar(listaServiciosPorAutorizar);
				listaOrdenesAutorizar.add(ordenAutorizacionDto);
			}	
			//boolean manejaMonto = UtilidadTexto.isEmpty(dtoSubCuenta.getMontoCobro()) ? false : true;
			
			boolean manejaMonto= true;
			if (dtoSubCuenta.getMontoCobro()== 0){
				manejaMonto=false;
			}
			
			datosPacienteAutorizar	= new DatosPacienteAutorizacionDto();
			datosPacienteAutorizar.setCodigoPaciente(dtoSubCuenta.getCodigoPaciente());
			datosPacienteAutorizar.setCodigoIngresoPaciente(dtoSubCuenta.getIngreso());
			datosPacienteAutorizar.setTipoPaciente(dtoSubCuenta.getTipoPaciente());
			datosPacienteAutorizar.setClasificacionSocieconomica(dtoSubCuenta.getClasificacionSocioEconomica());
			datosPacienteAutorizar.setTipoAfiliado(dtoSubCuenta.getTipoAfiliado());
			datosPacienteAutorizar.setNaturalezaPaciente(dtoSubCuenta.getNaturalezaPaciente());
			datosPacienteAutorizar.setCodConvenioCuenta(paciente.getCodigoConvenio());
			datosPacienteAutorizar.setCuentaAbierta(true);
			datosPacienteAutorizar.setCuentaManejaMontos(manejaMonto);
			datosPacienteAutorizar.setPorcentajeMontoCuenta(dtoSubCuenta.getPorcentajeMontoCobro());
			
			montoCobroAutorizacion	= new MontoCobroDto();
			montoCobroAutorizacion.setCantidadMonto(dtoSubCuenta.getCantidadMontoGeneral());
			montoCobroAutorizacion.setCodDetalleMonto(dtoSubCuenta.getMontoCobro());
			montoCobroAutorizacion.setPorcentajeMonto(dtoSubCuenta.getPorcentajeMontoGeneral());
			montoCobroAutorizacion.setTipoDetalleMonto(dtoSubCuenta.getTipoDetalleMonto());
			montoCobroAutorizacion.setTipoMonto(dtoSubCuenta.getTipoMonto());
			montoCobroAutorizacion.setValorMonto(dtoSubCuenta.getValorMontoGeneral());
			
			autorizacionCapitacionDto = new AutorizacionCapitacionDto();
			autorizacionCapitacionDto.setCodigoInstitucion(usuario.getCodigoInstitucionInt());
			autorizacionCapitacionDto.setLoginUsuario(usuario.getLoginUsuario());
			autorizacionCapitacionDto.setCodigoPersonaUsuario(usuario.getCodigoPersona());
			autorizacionCapitacionDto.setTipoAutorizacion(ConstantesIntegridadDominio.acronimoAutomatica);
			autorizacionCapitacionDto.setCentroAtencion(usuario.getCodigoCentroAtencion());
			autorizacionCapitacionDto.setDatosPacienteAutorizar(datosPacienteAutorizar);
			autorizacionCapitacionDto.setMontoCobroAutorizacion(montoCobroAutorizacion);
			autorizacionCapitacionDto.setOrdenesAutorizar(listaOrdenesAutorizar);
			
			//Proceso que se encarga de verificar si se generara o asociara autorizacion para la orden.
			manejoPacienteFacade = new ManejoPacienteFacade();
			listaAutorizacionCapitacion = manejoPacienteFacade.verificarGenerarAutorizacionCapitacion(autorizacionCapitacionDto);
			
			
			if(listaAutorizacionCapitacion!=null && !listaAutorizacionCapitacion.isEmpty())
			{	//Se adiciona mensaje para los servicio que no se autorizaron
				manejoPacienteFacade.obtenerMensajesError(listaAutorizacionCapitacion, errores);
			}
			UtilidadBD.finalizarTransaccion(con);
		}catch (IPSException ipsme) {
			UtilidadBD.abortarTransaccion(con);
			HibernateUtil.abortTransaction();
			throw ipsme;
		}
		catch (Exception e) {
			UtilidadBD.abortarTransaccion(con);
			HibernateUtil.abortTransaction();
			Log4JManager.error(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO,e);
		}
	}
	
	/**
	 * Metodo que se encarga de enviar los datos necesarios para validar la anulacion de la
	 * autorizacion
	 * 
	 * @param mundoInterconsulta
	 * @param usuario
	 * @param numeroSolicitud
	 * @throws IPSException
	 */
	public void cargarInfoParaAnulacionAutorizacion(SolicitudInterconsulta mundoInterconsulta,
			UsuarioBasico usuario)throws IPSException
	{		
		AnulacionAutorizacionSolicitudDto anulacionDto				= null;
		ManejoPacienteFacade manejoPacienteFacade		= null;
		Medicos medicos	= null;
		try{
			anulacionDto= new AnulacionAutorizacionSolicitudDto();
			anulacionDto.setMotivoAnulacion(mundoInterconsulta.getMotivoAnulacion());
			anulacionDto.setFechaAnulacion(UtilidadFecha.getFechaActualTipoBD());
			anulacionDto.setHoraAnulacion(UtilidadFecha.getHoraActual());
			medicos		= new Medicos(); 
			medicos.setCodigoMedico(usuario.getCodigoPersona());
			anulacionDto.setMedicoAnulacion(medicos);
			anulacionDto.setLoginUsuarioAnulacion(usuario.getLoginUsuario());
			anulacionDto.setNumeroSolicitud(mundoInterconsulta.getNumeroSolicitud());
		
			manejoPacienteFacade = new ManejoPacienteFacade();
			manejoPacienteFacade.validarAnulacionAutorizacionCapitaSolictud(anulacionDto,
					ConstantesBD.claseOrdenOrdenMedica,ConstantesBD.codigoTipoSolicitudInterconsulta,null,usuario.getCodigoInstitucionInt());
		
		}catch (IPSException ipsme) {
			HibernateUtil.abortTransaction();
			throw ipsme;
		}
		catch (Exception e) {
			HibernateUtil.abortTransaction();
			Log4JManager.error(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO,e);
		}
	}
}
