/*
 * @(#)EvolucionAction.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */

package com.princetonsa.action;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.Vector;

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
import util.ConstantesIntegridadDominio;
import util.Encoder;
import util.InfoDatos;
import util.InfoDatosInt;
import util.RespuestaValidacion;
import util.RespuestaValidacionTratante;
import util.ResultadoBoolean;
import util.TipoNumeroId;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadSesion;
import util.UtilidadTexto;
import util.UtilidadValidacion;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.historiaClinica.UtilidadesHistoriaClinica;
import util.manejoPaciente.UtilidadesManejoPaciente;

import com.princetonsa.actionform.EvolucionForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.TagDao;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.comun.DtoResultado;
import com.princetonsa.dto.historiaClinica.DtoValoracion;
import com.princetonsa.mundo.AdmisionHospitalaria;
import com.princetonsa.mundo.AdmisionUrgencias;
import com.princetonsa.mundo.Camas1;
import com.princetonsa.mundo.Cuenta;
import com.princetonsa.mundo.IngresoGeneral;
import com.princetonsa.mundo.Paciente;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.TrasladoCamas;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.atencion.AuxiliarDiagnosticos;
import com.princetonsa.mundo.atencion.Diagnostico;
import com.princetonsa.mundo.atencion.Egreso;
import com.princetonsa.mundo.atencion.Evolucion;
import com.princetonsa.mundo.atencion.EvolucionHospitalaria;
import com.princetonsa.mundo.atencion.HistoricoEvoluciones;
import com.princetonsa.mundo.atencion.SignoVital;
import com.princetonsa.mundo.enfermeria.RegistroEnfermeria;
import com.princetonsa.mundo.facturacion.EntidadesSubContratadas;
import com.princetonsa.mundo.historiaClinica.Valoraciones;
import com.princetonsa.mundo.informacionParametrizable.InformacionParametrizable;
import com.princetonsa.mundo.ordenesmedicas.OrdenMedica;
import com.princetonsa.mundo.solicitudes.Solicitud;
import com.princetonsa.mundo.solicitudes.ValidacionesSolicitud;
import com.princetonsa.pdf.EvolucionPdf;
import com.servinte.axioma.fwk.exception.IPSException;
import com.sysmedica.util.UtilidadFichas;

/**
 * Action para la funcionalidad de Evolucion (Ingreso,
 * Modificación, Resumen e Historico) 
 *
 * @version 1.0, May 26, 2003
 */

public class EvolucionAction extends Action
{
    /**
     * Nombre del contexto en que esta corriendo 
     * esta instancia de axioma
     */
	String nombreContexto=null;

    /**
	 * Objeto de tipo Logger para manejar los logs
	 * que genera esta clase
	 */
	private Logger logger = Logger.getLogger(EvolucionAction.class);
	
	//private final String direccionResumen="evolucion.do?estado=resumen&numeroSolicitud=";
	
	/**
	 * Método encargado de el flujo y control de la funcionalidad
	 * 
	 * @see org.apache.struts.action.Action#execute(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse)
	 */
	public ActionForward execute(ActionMapping mapping,
								 ActionForm form,
								 HttpServletRequest request,
								 HttpServletResponse response)
		throws Exception
		{
		Connection con=null;
		try{
			nombreContexto=request.getContextPath();
			if (response==null); //Para evitar que salga el warning
			if( logger.isDebugEnabled() )
			{
				logger.debug("Entro al Action de Evolucion");
			}

			if(form instanceof EvolucionForm)
			{

				EvolucionForm evolucionForm=(EvolucionForm)form;

				String estado=evolucionForm.getEstado();

				logger.info("------------------------------------------------");
				logger.info("[EvolucionAction] ESTADO --> "+estado);
				logger.info("------------------------------------------------\n\n");
				String codigoEvolucionString="";
				int codigoEvolucionInt=0;


				try
				{
					String tipoBD = System.getProperty("TIPOBD");
					DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
					con = myFactory.getConnection();
				}
				catch(Exception e)
				{
					e.printStackTrace();
					//No se cierra conexión porque si llega aca ocurrió un
					//error al abrirla
					request.setAttribute("codigoDescripcionError", "errors.problemasBd");
					return mapping.findForward("paginaError");
				}

				//Lo primero que vamos a hacer es validar que se
				//cumplan las condiciones.

				HttpSession session=request.getSession();
				PersonaBasica paciente = (PersonaBasica)session.getAttribute("pacienteActivo");
				UsuarioBasico medico = (UsuarioBasico)session.getAttribute("usuarioBasico");

				if(paciente == null)
				{
					return ComunAction.accionSalirCasoError(mapping,request,con,logger,"Error paciente no cargado","errors.paciente.noCargado",true);
				}

				/*Paciente pacienteCompleto = new Paciente();
			pacienteCompleto.cargarPaciente(con, paciente.getCodigoPersona());*/


				boolean esUrgencias;
				if(paciente.getCodigoUltimaViaIngreso()==ConstantesBD.codigoViaIngresoUrgencias)
				{
					esUrgencias=true;
				}
				else
				{
					esUrgencias=false;
				}

				//Primera Condición: El usuario debe existir
				//la validación de si es médico o no solo se hace en insertar
				if( medico == null )
				{
					return ComunAction.accionSalirCasoError(mapping, request, con, logger, "No existe el usuario", "errors.usuario.noCargado", true);
				}
				else
					if( paciente == null || paciente.getCodigoTipoIdentificacionPersona().equals("") )
					{
						//Segunda Condición: Debe haber un paciente cargado
						return ComunAction.accionSalirCasoError(mapping, request, con, logger, "paciente null o sin  id", "errors.paciente.noCargado", true);
					}
				//validacion del ingreso
				if(!estado.equals("resumen") && !estado.equals("resumenListado"))
				{	
					if(!UtilidadesHistoriaClinica.obtenerEstadoIngreso(con, paciente.getCodigoIngreso()).getCodigo().trim().equals(ConstantesIntegridadDominio.acronimoEstadoAbierto))
					{
						return ComunAction.accionSalirCasoError(mapping, request, con, logger, "errors.ingresoEstadoDiferente", "errors.ingresoEstadoDiferenteAbierto", true);
					}
					else if(UtilidadValidacion.esCuentaValida(con, paciente.getCodigoCuenta())<1)
					{
						return ComunAction.accionSalirCasoError(mapping, request, con, logger, "errors.paciente.cuentaNoValida", "errors.paciente.cuentaNoValida", true);
					}
				}	
				if (estado==null||estado.equals(""))
				{
					evolucionForm.reset();
					return ComunAction.accionSalirCasoError(mapping, request, con, logger, "La accion específicada no esta definida", "errors.estadoInvalido", true);
				}
				/**
				 * Validar concurrencia
				 * Si ya está en proceso de distribucion, no debe dejar entrar
				 **/
				else if(UtilidadValidacion.estaEnProcesoDistribucion(con, paciente.getCodigoPersona(), medico.getLoginUsuario()) )
				{
					return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error.facturacion.cuentaEnProcesoDistribucion", "error.facturacion.cuentaEnProcesoDistribucion", true);
				}
				/**
				 * Validar concurrencia
				 * Si ya está en proceso de facturación, no debe dejar entrar
				 **/
				else if(UtilidadValidacion.estaEnProcesofacturacion(con, paciente.getCodigoPersona(), "") )
				{
					return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error.facturacion.cuentaEnProcesoFact", "error.facturacion.cuentaEnProcesoFact", true);
				}
				else if (estado.equals("modificar") || estado.equals("modificarListado"))
				{
					//A diferencia de una valoración, la evolución se puede llenar
					//varias veces (dif. evol), así que el proceso de modificar
					//debe ser específico, se debe recibir el número de la evolución
					//a modificar. 

					//Antes que nada, vamos a limpiar la forma (Los
					//datos anteriores NO nos importan)
					evolucionForm.reset();

					evolucionForm.setModoPermitido("modificar");

					codigoEvolucionString=request.getParameter("numeroSolicitud");

					int numSolicitudFormaAnterior=0;

					cargarDiagnosticosValoracion(con, evolucionForm, paciente);

					numSolicitudFormaAnterior=evolucionForm.getNumeroSolicitud();

					if (codigoEvolucionString==null)
					{
						//Cuando el número de solicitud de la forma anterior
						//NO es 0 y el parametro llega con null, nos encontramos
						//en el caso despuès de una búsqueda con el tag "maravilla"
						if (numSolicitudFormaAnterior>0)
						{
							codigoEvolucionInt=numSolicitudFormaAnterior;
						}
						else
						{
							//Si no paso nada de lo anterior el usuario no mando nada
							//y esta tratando de acceder directamente, sin pasar por
							//el menú
							return ComunAction.accionSalirCasoError(mapping, request, con, logger, "El usuario intentó modificar, pero en el request no llegó el código, intento de acceso no permitido", "error.evolucion.numeroSolicitudNoEspecificado", true);
						}
					}
					else
					{
						try
						{
							codigoEvolucionInt=Integer.parseInt(codigoEvolucionString);
						}
						catch (Exception e)
						{
							return ComunAction.accionSalirCasoError(mapping, request, con, logger, "El usuario intentó modificar, pero en el request no llegó el código, intento de acceso no permitido", "error.evolucion.numeroSolicitudNoEspecificado", true);
						}
					}

					//A continuación vamos a revisar si se puede modificar o no
					//y en caso de ser un usuario normal (su codigoCentroCosto es
					//0) debemos decirle a la forma que solo puede añadir a los checkbox

					RespuestaValidacionTratante respVal0;
					//Si la cuenta del paciente está cerrada, se debe buscar la correspondiente
					//a esta evolucion y si no existe mostrar mensaje de error
					RespuestaValidacion respTemporal=UtilidadValidacion.existeCuentaParaEvolucion(con, codigoEvolucionInt);

					if (!respTemporal.puedoSeguir)
					{
						//Codigo de evolución falso
						return ComunAction.accionSalirCasoError(mapping, request, con, logger, "No existe cuenta para la evolución especificada en modificar (Código Ev. " +codigoEvolucionInt +")" , respTemporal.textoRespuesta, false);
					}
					//Ahora lo cargamos con el numero de cuenta encontrado
					respVal0=UtilidadValidacion.validacionModificarEvolucion(con, codigoEvolucionInt, Integer.parseInt(respTemporal.textoRespuesta), medico.getCodigoCentroCosto(), medico.getCodigoInstitucionInt());

					if (respVal0.puedoSeguir)
					{
						//Si el usuario NO es medico solo se le dejan agregar cosas
						if (medico.getCodigoCentroCosto()==0||!respVal0.esTratante)
						{
							evolucionForm.setSoloAnadir("true");
						}
						else
						{
							//La enfermera en ningún caso puede quitar cosas
							//así sea del centro de costo
							if (medico.getOcupacionMedica()==null||UtilidadValidacion.esEnfermera(medico).equals(""))
							{
								evolucionForm.setSoloAnadir("true");
							}
							else
							{
								evolucionForm.setSoloAnadir("false");
							}
						}
					}
					else
					{
						//Si el paciente tiene asocio existe una última oportunidad
						//antes de enviarlo a la página de error
						return ComunAction.accionSalirCasoError(mapping, request, con, logger, respVal0.textoRespuesta, respVal0.textoRespuesta, false) ;
					}

					//En una modificación solo pueden cambiar los boolean
					evolucionForm.setPuedeDarOrdenSalida("false");

					//Lo primero que debemos hacer es revisar 
					//el tipo de la evolucion, si es de hospitalizados
					//muestra checkbox, si no es la misma de resumen

					RespuestaValidacion respVal1=UtilidadValidacion.revisarTipoEvolucion(con, codigoEvolucionInt);
					if (respVal1.puedoSeguir)
					{
						boolean lb_noImprimir;
						String	ls_imprimir;
						lb_noImprimir = (ls_imprimir = request.getParameter("imprimirPagina") ) == null || !ls_imprimir.equals("imprimir");

						if (respVal1.textoRespuesta.equals("Hospitalizado"))
						{
							evolucionForm.setTipoEvolucion('h');
							EvolucionHospitalaria evolucion=new EvolucionHospitalaria();
							evolucion.setCodigo(codigoEvolucionInt);
							evolucion.cargar(con, codigoEvolucionInt);
							//Vamos a llenar los boolean con los datos del objeto
							cargarBeanHospitalizacion(evolucion, evolucionForm);
							request.setAttribute("evolucion", evolucion);
							//Para saber si se muestra el apartado de signos vitales
							//vamos a llenar el form con el número de sig, vit.
							evolucionForm.setNumSignosVitales(evolucion.getSignosVitales().size());
							//Para evitar mostrar los presuntivos si no existen
							//ponemos esta variable, no ponemos las de los definitivos
							//porque siempre hay al menos un definitivo
							evolucionForm.setNumDiagnosticosPresuntivos(evolucion.getDiagnosticosPresuntivos().size());
							evolucionForm.setEsCobrable(evolucion.isCobrable());

							this.cargarObservaciones(evolucion, lb_noImprimir);
						}
						else if (respVal1.textoRespuesta.equals("Urgencias"))
						{
							evolucionForm.setTipoEvolucion('u');

							EvolucionHospitalaria evolucion=new EvolucionHospitalaria();
							evolucion.setCodigo(codigoEvolucionInt);
							evolucion.cargar(con, codigoEvolucionInt);
							//Vamos a llenar los boolean con los datos del objeto
							cargarBeanHospitalizacion(evolucion, evolucionForm);
							request.setAttribute("evolucion", evolucion);
							//Para saber si se muestra el apartado de signos vitales
							//vamos a llenar el form con el tamaño
							evolucionForm.setNumSignosVitales(evolucion.getSignosVitales().size());
							evolucionForm.setNumDiagnosticosPresuntivos(evolucion.getDiagnosticosPresuntivos().size());
							evolucionForm.setEsCobrable(evolucion.isCobrable());

							this.cargarObservaciones(evolucion, lb_noImprimir);
						}
						else
						{
							evolucionForm.setTipoEvolucion('o');

							Evolucion evolucion=new Evolucion();
							evolucion.setCodigo(codigoEvolucionInt);
							evolucion.cargar(con, codigoEvolucionInt);
							request.setAttribute("evolucion", evolucion);
							//Para saber si se muestra el apartado de signos vitales
							//vamos a llenar el form con el tamaño
							evolucionForm.setNumSignosVitales(evolucion.getSignosVitales().size());
							evolucionForm.setNumDiagnosticosPresuntivos(evolucion.getDiagnosticosPresuntivos().size());
							evolucionForm.setEsCobrable(evolucion.isCobrable());

							this.cargarObservaciones(evolucion, lb_noImprimir);
						}
					}
					else
					{
						return ComunAction.accionSalirCasoError(mapping, request, con, logger, respVal1.textoRespuesta , respVal1.textoRespuesta, false) ;
					}


					//En una modificación solo pueden cambiar los boolean
					evolucionForm.setDebeDarMotivoRegresionEgreso("false");

					//Se guarda el número de la solicitud para que al momento de
					//modificar se tenga
					evolucionForm.setNumeroSolicitud(codigoEvolucionInt);

					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("modificarEvolucion");
				}
				else if (estado.equals("empezar"))
				{
					/**
					 * Se verifica que sea profesional de la salud
					 */
					if (!UtilidadValidacion.esProfesionalSalud(medico))
					{
						return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Usuario NO personal de la salud trató de ingresar evolución. Como usuario que no es profesional de la salud usted no esta autorizado para llenar una evolución", "errors.usuario.noAutorizado", true) ;
					}
					/**
					 * Validar concurrencia
					 * Si ya está en proceso de distribucion, no debe dejar entrar
					 **/
					else if(UtilidadValidacion.estaEnProcesoDistribucion(con, paciente.getCodigoPersona(), medico.getLoginUsuario()) )
					{
						return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error.facturacion.cuentaEnProcesoDistribucion", "error.facturacion.cuentaEnProcesoDistribucion", true);
					}
					/**
					 * Validar concurrencia
					 * Si ya está en proceso de facturación, no debe dejar entrar
					 **/
					else if(UtilidadValidacion.estaEnProcesofacturacion(con, paciente.getCodigoPersona(), "") )
					{
						return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error.facturacion.cuentaEnProcesoFact", "error.facturacion.cuentaEnProcesoFact", true);
					}
					else
					{
						//Este estado se maneja solo para aprovechar las
						//validaciones existentes
						UtilidadBD.cerrarConexion(con);
						evolucionForm.reset();
						return mapping.findForward("empezar");
					}
				}
				else if(estado.equals("histoDieta"))
				{
					//--Cargar la dieta de la Funcionalidad de Registro de enfermeria 
					cargarHistoricosDieta(con, medico, paciente, evolucionForm);
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("historicoDieta");
				}
				else if (estado.equals("histoSignosVitales"))
				{
					accionCargarHistoricoSignosVitales(con, evolucionForm, medico.getCodigoInstitucionInt(), UtilidadesManejoPaciente.obtenerCuentasXIngreso(con, paciente.getCodigoIngreso()+""));
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("signosVitalesEnfermeria");

				}
				else if (estado.equals("historicoMostrar") || estado.equals("historicoModificar"))
				{
					//Valida que el paciente no posee el ingreso con entidades subcontratadas
					if(IngresoGeneral.esIngresoComoEntidadSubContratada(con,paciente.getCodigoIngreso()).toString().equals(ConstantesBD.acronimoSi))
					{
						return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Ingreso de Paciente en Entidades Subcontratadas", "Ingreso de Paciente en Entidades Subcontratadas. Entidad Subcontratada : "+EntidadesSubContratadas.getDescripcionEntidadSubXIngreso(con,paciente.getCodigoIngreso()+""), false);
					}

					HistoricoEvoluciones hEvoluciones = new HistoricoEvoluciones();

					//Si la cuenta no esta abierta debemos mostrar todas las evoluciones
					//del mismo
					if (paciente.getCodigoCuenta()==0)
					{
						//La cuenta no esta abierta debemos mostrar todas las evoluciones de este paciente
						hEvoluciones.cargar(con, paciente.getCodigoTipoIdentificacionPersona(), paciente.getNumeroIdentificacionPersona());
					}
					else if (request.getParameter("codigoCuenta")!=null)
					{
						int codigoCuenta=0;
						try
						{
							codigoCuenta=Integer.parseInt(request.getParameter("codigoCuenta"));
						}
						catch(NumberFormatException e)
						{
							codigoCuenta=paciente.getCodigoCuenta();
						}
						hEvoluciones.cargar(con, codigoCuenta);
					}
					else
					{
						//Si no existe asocio, cargamos el de la cuenta del
						//paciente actual
						if (!paciente.getExisteAsocio())
						{
							hEvoluciones.cargar(con, paciente.getCodigoCuenta());
						}
						//Si lo hay cargamos el de las dos cuentas
						else
						{
							hEvoluciones.cargarEvolucionesCuentaYAsocio(con, paciente.getCodigoCuenta(), paciente.getCodigoCuentaAsocio());
						}
					}

					if (hEvoluciones.size()==0) 
					{
						return ComunAction.accionSalirCasoError(mapping, request, con, logger, "No existe ninguna evolucion previa por consultar" , "error.evolucion.historicoNoHayEvolucion", true) ;
					}

					if (estado.equals("historicoMostrar")) {
						request.setAttribute("estado", "resumen");
					}
					else if (estado.equals("historicoModificar")) {
						request.setAttribute("estado", "modificar");
					}

					request.setAttribute("evoluciones", hEvoluciones);
					UtilidadBD.cerrarConexion(con);

					return mapping.findForward("historico");
				}
				else if (estado.equals("resumen") || estado.equals("resumenListado"))
				{
					//A diferencia de una valoración, la evolución se puede llenar
					//varias veces (dif. evol), así que el proceso de resumen
					//debe ser específico, se debe recibir el número de la evolución
					//a modificar. 

					//Antes que nada, vamos a limpiar la forma (Los
					//datos anteriores NO nos importan), pero antes vamos
					//a guardar el número de evolución anterior, de tal manera
					//que si el usuario viene de un insertar o cargar, lo tengamos
					//en cuenta
					int numSolicitudFormaAnterior=0;
					int destinoSalidaAnterior=-1;
					numSolicitudFormaAnterior=evolucionForm.getNumeroSolicitud();
					try
					{
						destinoSalidaAnterior=evolucionForm.getDestinoSalida();
					}
					catch (Exception e) {}
					evolucionForm.reset();
					evolucionForm.setModoPermitido("resumen");
					evolucionForm.setDestinoSalida(destinoSalidaAnterior);
					evolucionForm.setOcultarEncabezado(UtilidadTexto.getBoolean(request.getParameter("ocultarEncabezado")));

					codigoEvolucionString=request.getParameter("numeroSolicitud");
					logger.info("codigoEvolucionString=> "+codigoEvolucionString);

					logger.info("nomSolicitudFormaAnterior=> "+numSolicitudFormaAnterior);
					if (codigoEvolucionString==null)
					{
						//Cuando el número de solicitud de la forma anterior
						//NO es 0 y el parametro llega con null, nos encontramos
						//en el caso despuès de una inserción o modificación,
						//donde se debe mostrar como quedaron los datos
						if (numSolicitudFormaAnterior>0)
						{
							codigoEvolucionInt=numSolicitudFormaAnterior;
						}
						else
						{
							//Si no paso nada de lo anterior el usuario no mando nada
							//y esta tratando de acceder directamente, sin pasar por
							//el menú
							return ComunAction.accionSalirCasoError(mapping, request, con, logger, "El usuario intentó consultar una evolución, pero en el request no llegó el código, intento de acceso no permitido", "error.evolucion.numeroSolicitudNoEspecificado", true);
						}
					}
					else
					{
						try
						{
							codigoEvolucionInt=Integer.parseInt(codigoEvolucionString);
						}
						catch (Exception e)
						{
							return ComunAction.accionSalirCasoError(mapping, request, con, logger, "El usuario intentó modificar, pero en el request no llegó el código, intento de acceso no permitido", "error.evolucion.numeroSolicitudNoEspecificado", true);
						}
					}

					//En una visualización no se puede cambiar nada
					evolucionForm.setPuedeDarOrdenSalida("false");

					//Lo primero que debemos hacer es revisar 
					//el tipo de la evolucion, si es de hospitalizados
					//muestra checkbox, si no es la misma de resumen

					RespuestaValidacion respVal1=UtilidadValidacion.revisarTipoEvolucion(con, codigoEvolucionInt);
					logger.info("respVal1 "+respVal1.puedoSeguir+" "+respVal1.textoRespuesta);
					if (respVal1.puedoSeguir)
					{
						cargarDiagnosticosValoracion(con, evolucionForm, paciente);
						boolean lb_noImprimir;
						String	ls_imprimir;
						lb_noImprimir = (ls_imprimir = request.getParameter("imprimirPagina") ) == null || !ls_imprimir.equals("imprimir");

						if (respVal1.textoRespuesta.equals("Hospitalizado"))
						{
							evolucionForm.setTipoEvolucion('h');
							EvolucionHospitalaria evolucion=new EvolucionHospitalaria();
							evolucion.setCodigo(codigoEvolucionInt);
							evolucion.cargar(con, codigoEvolucionInt);
							//Vamos a llenar los boolean con los datos del objeto
							cargarBeanHospitalizacion(evolucion, evolucionForm);
							request.setAttribute("evolucion", evolucion);
							//Para saber si se muestra el apartado de signos vitales
							//vamos a llenar el form con el número de sig, vit.
							evolucionForm.setNumSignosVitales(evolucion.getSignosVitales().size());
							//Para evitar mostrar los presuntivos si no existen
							//ponemos esta variable, no ponemos las de los definitivos
							//porque siempre hay al menos un definitivo
							evolucionForm.setNumDiagnosticosPresuntivos(evolucion.getDiagnosticosPresuntivos().size());
							evolucionForm.setEsCobrable(evolucion.isCobrable());

							this.cargarObservaciones(evolucion, lb_noImprimir);
						}
						else if (respVal1.textoRespuesta.equals("Urgencias"))
						{
							evolucionForm.setTipoEvolucion('u');

							EvolucionHospitalaria evolucion=new EvolucionHospitalaria();
							evolucion.setCodigo(codigoEvolucionInt);
							evolucion.cargar(con, codigoEvolucionInt);
							cargarBeanHospitalizacion(evolucion, evolucionForm);
							request.setAttribute("evolucion", evolucion);
							//Para saber si se muestra el apartado de signos vitales
							//vamos a llenar el form con el tamaño
							evolucionForm.setNumSignosVitales(evolucion.getSignosVitales().size());
							evolucionForm.setNumDiagnosticosPresuntivos(evolucion.getDiagnosticosPresuntivos().size());
							evolucionForm.setEsCobrable(evolucion.isCobrable());

							this.cargarObservaciones(evolucion, lb_noImprimir);
						}
						else
						{
							evolucionForm.setTipoEvolucion('o');

							Evolucion evolucion=new Evolucion();
							evolucion.setCodigo(codigoEvolucionInt);
							evolucion.cargar(con, codigoEvolucionInt);
							request.setAttribute("evolucion", evolucion);
							//Para saber si se muestra el apartado de signos vitales
							//vamos a llenar el form con el tamaño
							evolucionForm.setNumSignosVitales(evolucion.getSignosVitales().size());
							evolucionForm.setNumDiagnosticosPresuntivos(evolucion.getDiagnosticosPresuntivos().size());
							evolucionForm.setEsCobrable(evolucion.isCobrable());

							this.cargarObservaciones(evolucion, lb_noImprimir);
						}
					}
					else
					{
						return ComunAction.accionSalirCasoError(mapping, request, con, logger, respVal1.textoRespuesta, respVal1.textoRespuesta, false) ;
					}


					//En una modificación solo pueden cambiar los boolean
					evolucionForm.setDebeDarMotivoRegresionEgreso("false");

					//Se guarda el número de la solicitud para que al momento de
					//modificar se tenga
					evolucionForm.setNumeroSolicitud(codigoEvolucionInt);

					cargarInformacionConductaSeguir(con, paciente.getCodigoCuenta(), evolucionForm, codigoEvolucionInt);
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("modificarEvolucion");

				}
				else if (estado.equals("resumenEpicrisis"))
				{
					//Este método muestra un resumen propio para 
					//la epicrisis 

					//Antes que nada, vamos a limpiar la forma (Los
					//datos anteriores NO nos importan), pero antes vamos
					//a guardar el número de evolución anterior, de tal manera
					//que si el usuario viene de un insertar o cargar, lo tengamos
					//en cuenta
					int numSolicitudFormaAnterior=0;

					numSolicitudFormaAnterior=evolucionForm.getNumeroSolicitud();
					evolucionForm.reset();

					evolucionForm.setModoPermitido("resumen");

					codigoEvolucionString=request.getParameter("numeroSolicitud");

					if (codigoEvolucionString==null)
					{
						//Cuando el número de solicitud de la forma anterior
						//NO es 0 y el parametro llega con null, nos encontramos
						//en el caso despuès de una inserción o modificación,
						//donde se debe mostrar como quedaron los datos
						if (numSolicitudFormaAnterior>0)
						{
							codigoEvolucionInt=numSolicitudFormaAnterior;
						}
						else
						{
							//Si no paso nada de lo anterior el usuario no mando nada
							//y esta tratando de acceder directamente, sin pasar por
							//el menú
							return ComunAction.accionSalirCasoError(mapping, request, con, logger, "El usuario intentó consultar una evolución, pero en el request no llegó el código, intento de acceso no permitido", "error.evolucion.numeroSolicitudNoEspecificado", true);
						}
					}
					else
					{
						try
						{
							codigoEvolucionInt=Integer.parseInt(codigoEvolucionString);
						}
						catch (Exception e)
						{
							return ComunAction.accionSalirCasoError(mapping, request, con, logger, "El usuario intentó consultar, pero en el request no llegó el código, intento de acceso no permitido", "error.evolucion.numeroSolicitudNoEspecificado", true);
						}
					}

					//En una visualización no se puede cambiar nada
					evolucionForm.setPuedeDarOrdenSalida("false");

					//Lo primero que debemos hacer es revisar 
					//el tipo de la evolucion, si es de hospitalizados
					//muestra checkbox, si no es la misma de resumen

					RespuestaValidacion respVal1=UtilidadValidacion.revisarTipoEvolucion(con, codigoEvolucionInt);
					if (respVal1.puedoSeguir)
					{
						if (respVal1.textoRespuesta.equals("Hospitalizado")||respVal1.textoRespuesta.equals("Urgencias"))
						{
							boolean lb_noImprimir;
							String	ls_imprimir;
							lb_noImprimir = (ls_imprimir = request.getParameter("imprimirPagina") ) == null || !ls_imprimir.equals("imprimir");

							if(respVal1.textoRespuesta.equals("Hospitalizado"))
								evolucionForm.setTipoEvolucion('h');
							else
								evolucionForm.setTipoEvolucion('u');

							EvolucionHospitalaria evolucion=new EvolucionHospitalaria();
							evolucion.setCodigo(codigoEvolucionInt);
							evolucion.cargar(con, codigoEvolucionInt);
							//Vamos a llenar los boolean con los datos del objeto
							cargarBeanHospitalizacion(evolucion, evolucionForm);
							request.setAttribute("evolucion", evolucion);
							//Para saber si se muestra el apartado de signos vitales
							//vamos a llenar el form con el número de sig, vit.
							evolucionForm.setNumSignosVitales(evolucion.getSignosVitales().size());
							//Para evitar mostrar los presuntivos si no existen
							//ponemos esta variable, no ponemos las de los definitivos
							//porque siempre hay al menos un definitivo
							evolucionForm.setNumDiagnosticosPresuntivos(evolucion.getDiagnosticosPresuntivos().size());
							evolucionForm.setEsCobrable(evolucion.isCobrable());

							this.cargarObservaciones(evolucion, lb_noImprimir);
						}
						else
						{
							//Un resumen de epicrisis SOLO se puede hacer sobre uno
							//hospitalización, así que cualquier otro caso, lo mandamos
							//a la pàgina de error
							return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Un resumen de epicrisis SOLO se puede hacer sobre uno hospitalización" , "error.evolucion.resumenEpicrisisEstadoInvalido", true) ;
						}
					}
					else
					{
						return ComunAction.accionSalirCasoError(mapping, request, con, logger, respVal1.textoRespuesta, respVal1.textoRespuesta, false) ;
					}


					//En una modificación solo pueden cambiar los boolean
					evolucionForm.setDebeDarMotivoRegresionEgreso("false");

					//Se guarda el número de la solicitud para que al momento de
					//modificar se tenga
					evolucionForm.setNumeroSolicitud(codigoEvolucionInt);

					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("modificarEvolucion");

				}
				else if (estado.equals("insertar"))
				{


					if( logger.isDebugEnabled() )
					{
						logger.debug("La accion específicada es insertar");
					}
					//Valida que el paciente no posee el ingreso con entidades subcontratadas
					if(IngresoGeneral.esIngresoComoEntidadSubContratada(con,paciente.getCodigoIngreso()).toString().equals(ConstantesBD.acronimoSi))
					{
						return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Ingreso de Paciente en Entidades Subcontratadas", "Ingreso de Paciente en Entidades Subcontratadas. Entidad Subcontratada : "+EntidadesSubContratadas.getDescripcionEntidadSubXIngreso(con,paciente.getCodigoIngreso()+""), false);
					}

					if (paciente.getCodigoCuenta()==0)
					{
						//El paciente no tiene cuenta, saco error
						return ComunAction.accionSalirCasoError(mapping, request, con, logger, "paciente sin cuenta", "Opcion disponible solo para pacientes de Urgencias y Hospitalizacion", false) ;
					}
					if (paciente.getCodigoUltimaViaIngreso()==ConstantesBD.codigoViaIngresoAmbulatorios||paciente.getCodigoUltimaViaIngreso()==ConstantesBD.codigoViaIngresoConsultaExterna)
					{
						//El paciente tiene cuenta de ambulatorios
						return ComunAction.accionSalirCasoError(mapping, request, con, logger, "paciente con cuenta de ambulatorios", "Opcion disponible solo para pacientes de Urgencias y Hospitalizacion", false) ;
					}
					if (UtilidadValidacion.tieneEgreso(con, paciente.getCodigoCuenta()))
					{
						return ComunAction.accionSalirCasoError(mapping, request, con, logger, "El paciente es de urg/hosp pero ya tiene orden de salida", "error.evolucion.yaExisteOrdenSalida", true);
					}




					//Antes que nada, vamos a limpiar la forma (Los
					//datos anteriores NO nos importan)
					evolucionForm.reset();

					Evolucion evolucion=new Evolucion();
					String parametroControlaInter = parametroControlaInter=Evolucion.consultarParametroControlaInterpretacion(con);

					logger.info("VALOR PARAMETRO GENERAL CONTROLA INTERPRETACION ->"+parametroControlaInter);

					// PREGUNTO SI ESTA ACTIVO EL PARAMETRO GENERAL DE SE CONTROLA PROCEDIMIENTOS PARA PERMITIR EVOLUCIONAR

					if(parametroControlaInter.equals(ConstantesBD.acronimoSi))
					{
						logger.info("///////////// Parametro Activo ///////////////");

						logger.info("LA CUENTA DEL PACIENTE ES ->"+paciente.getCodigoCuenta());

						evolucionForm.setEspecialidadesCuentaMapa(evolucion.consultarEspecialidadesCuenta(con, paciente.getCodigoCuenta())); 

						logger.info(">>>>> MAPA ESPECIALIDADES ->>>"+evolucionForm.getEspecialidadesCuentaMapa());

						int numRegEspecial = Utilidades.convertirAEntero(evolucionForm.getEspecialidadesCuentaMapa().get("numRegistros").toString());

						//CARGO EL USUARIO EN SESION
						UsuarioBasico usuario = (UsuarioBasico)session.getAttribute("usuarioBasico");


						for(int x=0; x<usuario.getEspecialidades().length; x++)
						{
							logger.info("Posicion >>>"+x+" Especialidad ->"+usuario.getEspecialidades()[x].getCodigo());

							for(int y=0; y<numRegEspecial;y++)
							{
								if(usuario.getEspecialidades()[x].getCodigo()== Utilidades.convertirAEntero(evolucionForm.getEspecialidadesCuentaMapa().get("especialidad_"+y).toString()))
								{
									evolucionForm.setControlInterpretacion(true);
									// Si es TRUE SE debe presentar el LINK a la funcionalidad INTERPRETAR PROCEDIMIENTOS del modulo Ordenes
								}
							}

						}

						if(evolucionForm.isControlInterpretacion() == false)
							return ComunAction.accionSalirCasoError(mapping, request, con, logger, "No se permite Evolucionar, Existen Ordenes sin Interpretar, por Favor Verifique", "error.evolucion.controlInterpretacion", true);

						usuario.getEspecialidades();
					}


					logger.info("accionesManejoWarning-->");
					this.accionesManejoWarning(con, evolucionForm, medico, paciente);

					//cargamos la informacion de la conducta a seguir
					cargarInformacionConductaSeguir(con, paciente.getCodigoCuenta(), evolucionForm, ConstantesBD.codigoNuncaValido);

					//eliminamos las fichas inactivas 
					logger.info("ELIMINAMOS LAS FICHAS INACTIVAS!!!!!!!!!!!!!!!!!!!!!!");
					//********SE ELIMINAN LAS FICHAS DE EPIDEMIOLOGÍA INACTIVAS**********
					UtilidadFichas.eliminarFichasInactivas(con, medico.getLoginUsuario(), paciente.getCodigoPersona());
					logger.info("PASO LA ELIMINACION DE LAS FICHAS INACTIVAS!!!!!!!!!!!!!!!!!!!!!!");


					logger.info("1. NumSol=> "+evolucionForm.getNumeroSolicitudPidioConvertirMedicoATratante()+", esAdjunto=> "+evolucionForm.getEsAdjunto());
					//Debemos especificarle a la forma que puede hacer
					//si modificar, insertar o consultar
					evolucionForm.setModoPermitido("insertar");
					cargarDiagnosticosValoracion(con, evolucionForm, paciente);

					//Ahora vamos a sacar la fecha de creación de la cuenta o admisión
					//(Si estamos en medio de una admisión mostramos la de la admisión
					//, si no mostramos la de la cuenta)y la vamos a poner en el form

					RespuestaValidacion fechaCuentaOAdmision=UtilidadValidacion.tieneFechaAdmisionCuenta(con, paciente.getCodigoCuenta(), paciente.getCodigoAdmision(), paciente.getAnioAdmision());
					evolucionForm.setDeboMostrarMensajeCancelacionTratante(UtilidadValidacion.deboMostrarMensajeCancelacionTratanteEnIngresoEvolucion(con, paciente.getCodigoCuenta(), medico.getCodigoCentroCosto()));
					String arregloFechaCuentaOAdmision[]=new String[0];
					if (!fechaCuentaOAdmision.puedoSeguir)
					{
						return ComunAction.accionSalirCasoError(mapping, request, con, logger, fechaCuentaOAdmision.textoRespuesta, fechaCuentaOAdmision.textoRespuesta, false) ;
					}
					else
					{
						arregloFechaCuentaOAdmision=fechaCuentaOAdmision.textoRespuesta.split("-");
					}

					evolucionForm.setFechaCuentaOAdmision(arregloFechaCuentaOAdmision[2] + "/"  + arregloFechaCuentaOAdmision[1] + "/" + arregloFechaCuentaOAdmision[0]  );
					logger.info("2. NumSol=> "+evolucionForm.getNumeroSolicitudPidioConvertirMedicoATratante()+", esAdjunto=> "+evolucionForm.getEsAdjunto());
					String elementos[]=UtilidadValidacion.obtenerMaximaFechaYHoraEvolucion(con, paciente.getCodigoCuenta());
					if (elementos!=null)
					{
						evolucionForm.setFechaMaximaEvolucionFormatoBD(elementos[0]);
						evolucionForm.setHoraMaximaEvolucion(elementos[1]);
					}
					else
					{
						//Solo validamos valoración cuando NO hay evoluciones
						elementos=UtilidadValidacion.obtenerMaximaFechaYHoraValoracion(con, paciente.getCodigoCuenta());
						if (elementos!=null)
						{
							evolucionForm.setFechaMaximaValoracionFormatoBD(elementos[0]);
							evolucionForm.setHoraMaximaValoracion(elementos[1]);
						}
					}

					elementos=UtilidadValidacion.obtenerFechaYHoraPrimeraValoracion(con, paciente.getCodigoCuenta());
					if (elementos!=null)
					{
						evolucionForm.setFechaPrimeraValoracionFormatoBD(elementos[0]);
						evolucionForm.setHoraPrimeraValoracion(elementos[1]);
					}

					/*
					 * Postulación del tipo de diagnóstico principal
					 * ya sea de la última evolución o de la valoración inicial
					 */
					evolucionForm.setTipoDiagnosticoPrincipal(UtilidadValidacion.obtenerTipoDiagnosticoPrincipal(con, paciente.getCodigoCuenta(), UtilidadValidacion.tieneEvoluciones(con, paciente.getCodigoCuenta())));

					/*
					 * Postulación del tipo de monitoreo
					 */
					evolucionForm.setProcedimientosQuirurgicosYObstetricos(Utilidades.obtenerUltimoTipoMonitoreo(con, paciente.getCodigoCuenta(), medico.getCodigoInstitucionInt(), false));

					//Dependiendo de si estamos en una admisión o no, va el mensaje
					//que se pone de error en la validacion del form, como este no debe
					//tener contacto con el mundo le pasamos un parámetro para que
					//sepa que tipo de mensaje debe mostrar en caso de error
					if (paciente.getCodigoAdmision()>0)
					{
						evolucionForm.setEstamosEnAdmision(true);
					}
					else
					{
						evolucionForm.setEstamosEnAdmision(false);
					}
					//Ahora vamos a revisar la existencia de una valoración
					//del mismo centro de costo, pero en caso de asocio cuentas,
					//nos saltamos esa validación
					RespuestaValidacionTratante resp=UtilidadValidacion.validarIngresarEvolucion (con, paciente, medico);

					//Si no puedo seguir me voy a la página de error y como descripción
					//envio el resultado de la validación
					if (!resp.puedoSeguir)
					{
						return ComunAction.accionSalirCasoError(mapping, request, con, logger, resp.textoRespuesta, resp.textoRespuesta, false) ;
					}
					else
					{
						//El médico puede ingresar los datos, vamos a definir
						//si le aparece la opción de salida
						if (resp.esTratante)
						{
							evolucionForm.setEsTratante(true);
							//Si el médico es tratante, puede dar orden de salida
							evolucionForm.setPuedeDarOrdenSalida("true");
							logger.info("3. NumSol=> "+evolucionForm.getNumeroSolicitudPidioConvertirMedicoATratante()+", esAdjunto=> "+evolucionForm.getEsAdjunto());
							//En caso que el médico tenga toda la autoridad
							//de dar la orden de salida, deben haber tantas
							//evoluciones como días desde la primera evolución
							//y si no los hay debemos avisarle al médico que
							//faltan

							GregorianCalendar calendario = new GregorianCalendar();

							int diaActual  = calendario.get(Calendar.DAY_OF_MONTH);
							//Java maneja el mes empezando en 0, así que debemos subirlo
							int mesActual  = calendario.get(Calendar.MONTH)+1;
							int anioActual = calendario.get(Calendar.YEAR);

							//logger.info("1");
							if(!paciente.isHospitalDia())
							{	
								RespuestaValidacion resp1=UtilidadValidacion.existenEvolucionesSuficientesOrdenSalida(con, paciente.getCodigoCuenta(), medico.getCodigoCentroCosto(), diaActual, mesActual, anioActual, esUrgencias, medico.getCodigoInstitucionInt());
								//logger.info("2");
								if (!resp1.puedoSeguir)
								{
									String warningSalida="No se puede dar orden de salida. Faltan las evoluciones de ";
									//En este caso aviso (NO se puede dar orden de salida)
									//Hay que buscar todas las fechas que faltan
									ArrayList fechasQueFaltan=UtilidadValidacion.obtenerListadoEvolucionesNoLlenadas (con, paciente.getCodigoCuenta(),  diaActual, mesActual, anioActual, resp1.textoRespuesta, paciente.getCodigoCuentaAsocio());

									//Cuando no hay ningun elemento en fechas que faltan
									//indica que el usuario está dando una fecha anterior 

									if (fechasQueFaltan!=null&&fechasQueFaltan.size()>0)
									{
										for (int i=0;i<fechasQueFaltan.size();i++)
										{
											if (i==0)
											{
												warningSalida=warningSalida+ " " + UtilidadFecha.conversionFormatoFechaAAp((String)fechasQueFaltan.get(i)); 
											}
											else
											{
												warningSalida=warningSalida+ ", " + UtilidadFecha.conversionFormatoFechaAAp((String) fechasQueFaltan.get(i)); 
											}
										}
										evolucionForm.setWarningOrdenSalida(warningSalida);
									}
								}
							}	

							//validaciones de CLAP
							if(paciente.getCodigoSexo()==ConstantesBD.codigoSexoFemenino)
							{
								String cuentaAsociada="";
								if(paciente.getExisteAsocio())
									cuentaAsociada=paciente.getCodigoCuentaAsocio()+"";

								/**
								 * MT 7378 y MT 7399
								 * Se agrega la validación de destino de salida para mujeres con solicitudes de parto o cirugía.
								 * 
								 * @author jeilones
								 * @date 16/17/2013
								 * */
								Vector warningCLAPVector= UtilidadValidacion.puedoDarOrdenSalidaXClap(con, paciente.getCodigoCuenta()+"", paciente.getCodigoPersona()+"", cuentaAsociada,evolucionForm.getDestinoSalida(),true);
								if(warningCLAPVector.size()>0)
								{
									String warningClapStr="No se puede dar orden de salida. Porque: ";
									for(int w=0; w<warningCLAPVector.size(); w++)
									{
										if(w==0)
											warningClapStr+=warningCLAPVector.get(w);
										else
											warningClapStr+=", "+warningCLAPVector.get(w);
									}
									evolucionForm.setWarningOrdenSalida(warningClapStr);
									//evolucionForm.setPuedeDarOrdenSalida("false");
								}
							}	

							/*if(paciente.getManejoConjunto())
						{
							logger.info("PACIENTE CON MANEJO CONJUNTO!!!!!!!");
							evolucionForm.setWarningOrdenSalida(evolucionForm.getWarningOrdenSalida()+
											"\nNo se puede dar orden de salida ya que " +
											"el paciente tiene manejo conjunto");
							evolucionForm.setPuedeDarOrdenSalida("false");
						}*/
						}
						else
						{
							evolucionForm.setPuedeDarOrdenSalida("false");
							if(evolucionForm.getEsAdjunto())
							{
								/*if(paciente.getManejoConjunto())
							{
								logger.info("PACIENTE CON MANEJO CONJUNTO!!!!!!!");
								evolucionForm.setWarningOrdenSalida(evolucionForm.getWarningOrdenSalida()+
												"\nNo se puede dar orden de salida ya que " +
												"el paciente tiene manejo conjunto");*/
								evolucionForm.setPuedeDarOrdenSalida("true");
								//}
							}
						}
					}

					this.cargarWarningsReferencia(con, evolucionForm, paciente);


					logger.info("\n\n\n\n evolucionForm.getEsAdjunto()-->"+evolucionForm.getEsAdjunto()+"\n\n\n");
					logger.info("\n\n\n\n paciente.getManejoConjunto()-->"+paciente.getManejoConjunto()+"\n\n\n");

					//Dependiendo si es hosp o urg se muestran ciertos campos y
					//ciertas opciones

					//Primero revisamos si tiene Admision
					if (paciente.getCodigoAdmision()!=0)
					{ 
						//Si se tiene anio de admision es de urgencias
						if (paciente.getAnioAdmision()!=0)
						{
							if (UtilidadValidacion.estaEnCamaObservacion(con, paciente.getCodigoCuenta()))
							{
								evolucionForm.setTipoEvolucion('u');
							}
							else
							{
								return ComunAction.accionSalirCasoError(mapping, request, con, logger, "No se puede hacer evolución en urgencias a menos que el paciente este en cama de observación", "No se puede hacer evolución en urgencias a menos que el paciente este en cama de observación", false) ;
							}
						}
						else
						{
							evolucionForm.setTipoEvolucion('h');
						}
					}
					else
					{
						//Si no tiene número de admisión lo ponemos en 'otros'
						evolucionForm.setTipoEvolucion('o');
					}

					//Activamos la siguiente línea si se desea que la evolución
					//tenga el campo de motivo de regresion del egreso

					RespuestaValidacion respMotivoRev=UtilidadValidacion.deboMostrarMotivoReversionEgreso(con, paciente.getCodigoCuenta());
					if (respMotivoRev.puedoSeguir)
					{				
						evolucionForm.setDebeDarMotivoRegresionEgreso("true");
						evolucionForm.setMotivoReversionEgreso(respMotivoRev.textoRespuesta);
						//Se carga la fecha y hora de la reversión
						Egreso egreso=new Egreso();
						egreso.setNumeroCuenta(paciente.getCodigoCuenta());
						/*logger.info("Codigo de la cuenta del paciente=> "+paciente.getCodigoCuenta());
					logger.info("Estado Cargado de datos reversion=> "+egreso.cargarReversionEgreso(con));
					logger.info("Fecha de la reversion => "+egreso.getFechaReversion());
					logger.info("Hora de la reversion => "+egreso.getHoraReversion());*/

						evolucionForm.setFechaEvolucion(egreso.getFechaReversion());
						evolucionForm.setHoraEvolucion(egreso.getHoraReversion());

					}
					else
					{
						evolucionForm.setDebeDarMotivoRegresionEgreso("false");
					}
					logger.info("4. NumSol=> "+evolucionForm.getNumeroSolicitudPidioConvertirMedicoATratante()+", esAdjunto=> "+evolucionForm.getEsAdjunto());
					//Si llegamos a este punto es porque todo esta validado, ahora
					//vamos a proponer los diagnosticos
					this.preCargarDiagnosticos(con, paciente.getCodigoCuenta(), evolucionForm, request);

					logger.info("5. NumSol=> "+evolucionForm.getNumeroSolicitudPidioConvertirMedicoATratante()+", esAdjunto=> "+evolucionForm.getEsAdjunto());
					if(evolucionForm.getFechaEvolucion().trim().equals(""))
						evolucionForm.setFechaEvolucion(UtilidadFecha.getFechaActual());
					if(evolucionForm.getHoraEvolucion().trim().equals(""))
						evolucionForm.setHoraEvolucion(UtilidadFecha.getHoraActual());

					logger.info("DIAGNOSTICO COMPLICACION--->"+evolucionForm.getDiagnosticoComplicacion_1()+"  descripcion-->"+evolucionForm.getDescripcionComplicacion());
					logger.info("DIAGNOSTICO PRESUNTIVO--->"+evolucionForm.getDiagnosticosPresuntivos());
					logger.info("DIAGNOSTICO DEFINITIVO--->"+evolucionForm.getDiagnosticosDefinitivos()); 
					logger.info("NUM DIAGNOSTICO DEFINITIVO--->"+evolucionForm.getNumDiagnosticosDefinitivos());
					logger.info("NUM DIAGNOSTICO presuntivo--->"+evolucionForm.getNumDiagnosticosPresuntivos());

					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("evolucionPrincipal");
				}
				else
					if( estado.equals("cancelar") )
					{

						if( logger.isDebugEnabled() )
						{
							logger.debug("La acción especificada es cancelar");
						}

						String paginaSiguiente=request.getParameter("paginaSiguiente");
						if (paginaSiguiente==null||paginaSiguiente.equals(""))
						{
							UtilidadBD.cerrarConexion(con);
							return mapping.findForward("resumenEvolucion");
						}
						else
						{

							evolucionForm.reset();
							UtilidadBD.cerrarConexion(con);
							response.sendRedirect(paginaSiguiente);
						}
					}
					else if ( estado.equals("salir"))
					{

						logger.info("PASO POR AQUI!! "+evolucionForm.getModoPermitido()+" "+evolucionForm.getTipoEvolucion());

						logger.info("DIAGNOSTICO COMPLICACION--->"+evolucionForm.getDiagnosticoComplicacion_1()+"  descripcion-->"+evolucionForm.getDescripcionComplicacion());
						logger.info("DIAGNOSTICO PRESUNTIVO--->"+evolucionForm.getDiagnosticosPresuntivos());
						logger.info("DIAGNOSTICO DEFINITIVO--->"+evolucionForm.getDiagnosticosDefinitivos()); 
						logger.info("NUM DIAGNOSTICO DEFINITIVO--->"+evolucionForm.getNumDiagnosticosDefinitivos());
						logger.info("NUM DIAGNOSTICO presuntivo--->"+evolucionForm.getNumDiagnosticosPresuntivos());

						logger.info("evolucionForm.getModoPermitido()--->"+evolucionForm.getModoPermitido());
						//La accion salir es genérica y pudo haber sido llamada desde
						//insertar modificar o resumen

						if (evolucionForm.getModoPermitido().equals("resumen"))
						{
							if( logger.isDebugEnabled() )
							{
								logger.debug("La acción especificada es salir(resumen)");
							}

							UtilidadBD.cerrarConexion(con);

							String paginaSiguiente=request.getParameter("paginaSiguiente");
							if (paginaSiguiente==null||paginaSiguiente.equals(""))
							{
								return mapping.findForward("resumenEvolucion");
							}
							else
							{

								evolucionForm.reset();
								response.sendRedirect(paginaSiguiente);
							}
						}

						if (evolucionForm.getModoPermitido().equals("modificar"))
						{
							if( logger.isDebugEnabled() )
							{
								logger.debug("La acción especificada es salir(modificar)");
							}
							//Solo se puede modificar si es de hospitalizacion o de urgencias
							//luego solo se hacen cosas cuando sea h, de resto
							//nos vamos al resumen

							if (evolucionForm.getTipoEvolucion()=='h'||evolucionForm.getTipoEvolucion()=='u')
							{
								//Necesitamos la evolucion anterior pero no la podemos
								//traer de la accion anterior porque pudo haber sido modificada
								//por otro usario mientras el usuario la llenaba
								EvolucionHospitalaria evolucion=new EvolucionHospitalaria();
								evolucion.setCodigo(evolucionForm.getNumeroSolicitud());
								evolucion.cargar(con, evolucionForm.getNumeroSolicitud());
								this.revisarBoolean(evolucion, evolucionForm);

								String actualizarBooleanMotivoReversionEgreso=request.getParameter("actualizarBooleanMotivoReversionEgreso");
								if (actualizarBooleanMotivoReversionEgreso!=null&&actualizarBooleanMotivoReversionEgreso.equals("true"))
								{
									Egreso egreso=new Egreso();
									evolucion.modificarTransaccional(con, evolucion.getCodigo(), "comenzar");
									egreso.actualizarInformacionMotivoReversionSoloBooleanTransaccional(con, evolucionForm.isMotivoReversionEgresoBoolean(), evolucion.getCodigo(), "finalizar");
								}
								else
								{
									evolucion.modificar(con, evolucion.getCodigo());
								}
							}

							UtilidadBD.cerrarConexion(con);
							String paginaSiguiente=request.getParameter("paginaSiguiente");

							//Debemos mantener los casos en los que hayan datos de epicrisis

							if (paginaSiguiente==null||paginaSiguiente.equals(""))
							{
								if (request.getParameter("codigoEpicrisis")!=null&&request.getParameter("tipoEpicrisis")!=null)
								{
									response.sendRedirect(nombreContexto+"/evolucion/evolucion.do?estado=modificar&tipoEpicrisis=" +request.getParameter("tipoEpicrisis")+"&codigoEpicrisis=" + request.getParameter("codigoEpicrisis") + "&numeroSolicitud="+ evolucionForm.getNumeroSolicitud());
								}
								else
								{
									response.sendRedirect(nombreContexto+"/evolucion/evolucion.do?estado=modificar&numeroSolicitud="+ evolucionForm.getNumeroSolicitud());
								}
							}
							else
							{
								evolucionForm.reset();
								response.sendRedirect(paginaSiguiente);
							}
						}


						if (evolucionForm.getModoPermitido().equals("insertar"))
						{

							ActionForward posibleAccionAdjunto=this.accionesManejoError(con, mapping, request, evolucionForm, medico, paciente);
							if (posibleAccionAdjunto!=null)
							{
								logger.info("primera salida");
								return posibleAccionAdjunto;
							}

							if( logger.isDebugEnabled() )
							{
								logger.debug("La acción especificada es salir(insertar)");
							}

							if (paciente.getCodigoCuenta()==0)
							{
								//El paciente no tiene cuenta, saco error
								return ComunAction.accionSalirCasoError(mapping, request, con, logger, "paciente sin cuenta", "Opcion disponible solo para pacientes de Urgencias y Hospitalizacion", false) ;
							}

							if (evolucionForm.getTipoEvolucion()=='h')
							{
								EvolucionHospitalaria evolucion=new EvolucionHospitalaria();
								logger.info("numsol A=> "+evolucionForm.getNumeroSolicitud());
								cargarObjeto(evolucion, evolucionForm);
								logger.info("numsol B=> "+evolucionForm.getNumeroSolicitud());
								cargarObjetoHospitalizacion(evolucion, evolucionForm);
								logger.info("numsol C=> "+evolucionForm.getNumeroSolicitud());
								evolucion.setMedico(medico);
								evolucion.setIdCuenta(paciente.getCodigoCuenta());
								evolucion.setIdCentroCosto(medico.getCodigoCentroCosto());

								evolucion.setCobrable(evolucionForm.isEsCobrable());
								evolucion.setTipoEvolucion(Evolucion.HOSPITALARIA);				

								if (evolucionForm.getOrdenSalida()!=null&&evolucionForm.getOrdenSalida().equals("true"))
								{

									//Si hay orden de Salida debemos crear un objeto de tipo Egreso
									Egreso egreso= new Egreso();
									int numeroEvolucion=this.insercionBasicaEvolucion(request, con, evolucionForm, medico, paciente, evolucion, false);							
									this.cargarEgreso(con, egreso, evolucionForm, numeroEvolucion, paciente, medico);						


									//Se debe revisar que la fecha dada por el usuario cumpla las condiciones
									String fechaEvolucionString[]=evolucion.getFechaEvolucion().split("/");
									int diaEvolucion=Integer.parseInt(fechaEvolucionString[0]);
									int mesEvolucion=Integer.parseInt(fechaEvolucionString[1]);
									int anioEvolucion=Integer.parseInt(fechaEvolucionString[2]);

									//La fecha llega en formato dd/mm/aaaa
									if(!paciente.isHospitalDia())
									{	
										RespuestaValidacion resp1=UtilidadValidacion.existenEvolucionesSuficientesOrdenSalida(con, paciente.getCodigoCuenta(), medico.getCodigoCentroCosto(),  diaEvolucion, mesEvolucion, anioEvolucion, esUrgencias, medico.getCodigoInstitucionInt());

										if (!resp1.puedoSeguir)
										{
											String warningSalida="";
											//En este caso aviso (NO se puede dar orden de salida)

											//Hay que buscar todas las fechas que faltan

											ArrayList fechasQueFaltan=UtilidadValidacion.obtenerListadoEvolucionesNoLlenadas (con, paciente.getCodigoCuenta(),  diaEvolucion, mesEvolucion, anioEvolucion, resp1.textoRespuesta, paciente.getCodigoCuentaAsocio());

											if (fechasQueFaltan!=null&&fechasQueFaltan.size()>0)
											{
												for (int i=0;i<fechasQueFaltan.size();i++)
												{
													if (i==0)
													{
														warningSalida=warningSalida+ " " + UtilidadFecha.conversionFormatoFechaAAp((String)fechasQueFaltan.get(i)); 
													}
													else
													{
														warningSalida=warningSalida+ ", " + UtilidadFecha.conversionFormatoFechaAAp((String)fechasQueFaltan.get(i)); 
													}
												}

												this.accionesManejoWarning(con, evolucionForm, medico, paciente);

												//Solo si el ArrayList no fué vacio mostramos el error
												UtilidadBD.cerrarConexion(con);

												ActionErrors errors=new ActionErrors();
												errors.add("evolInexistentes", new ActionMessage("error.evolucion.faltanevoluciones", warningSalida));
												this.saveErrors(request, errors);
												evolucionForm.setWarningOrdenSalida("");
												return mapping.findForward("evolucionPrincipal");
											}
										}
									}
									//validaciones de CLAP
									if(paciente.getCodigoSexo()==ConstantesBD.codigoSexoFemenino)
									{
										String cuentaAsociada="";
										if(paciente.getExisteAsocio())
											cuentaAsociada=paciente.getCodigoCuentaAsocio()+"";
										/**
										 * MT 7378 y MT 7399
										 * Se agrega la validación de destino de salida para mujeres con solicitudes de parto o cirugía.
										 * 
										 * @author jeilones
										 * @date 16/17/2013
										 * */
										Vector warningCLAPVector= UtilidadValidacion.puedoDarOrdenSalidaXClap(con, paciente.getCodigoCuenta()+"", paciente.getCodigoPersona()+"",cuentaAsociada,evolucionForm.getDestinoSalida(),true);
										if(warningCLAPVector.size()>0)
										{
											String warningClapStr="";
											for(int w=0; w<warningCLAPVector.size(); w++)
											{
												if(w==0)
													warningClapStr+=warningCLAPVector.get(w);
												else
													warningClapStr+=", "+warningCLAPVector.get(w);
											}

											//evolucionForm.setPuedeDarOrdenSalida("false");
											UtilidadBD.cerrarConexion(con);
											ActionErrors errors=new ActionErrors();
											errors.add("", new ActionMessage("error.evolucion.noPuedeDarOrdenSalida", warningClapStr));
											this.saveErrors(request, errors);
											evolucionForm.setWarningOrdenSalida("");
											return mapping.findForward("evolucionPrincipal");
										}
									}	


									ActionForward posibleSalida=revisarQueNoHayaEvolucionAnterior(con, mapping, request, evolucion, paciente);
									if (posibleSalida!=null)
									{
										return posibleSalida;
									}									

									///Se revisa estado del paciente para verificar si está muerto o vivo
									logger.info("Paciente Muerto? "+evolucionForm.getMuerte());
									if(evolucionForm.getMuerte()!=null&&evolucionForm.getMuerte().equals("true"))
										UtilidadValidacion.actualizarPacienteAMuertoTransaccional(con,paciente.getCodigoPersona(),false,evolucionForm.getFechaMuerte(), evolucionForm.getHoraMuerte(), evolucionForm.getCertificadoDefuncion(),ConstantesBD.continuarTransaccion);

									//si es orden de egreso y el destino de salida es: Dado de alta - remitido -salida coluntaria entonces se cambia el estaodo de la cama de ocupada a con salida
									this.cambiarEstadoCama(con, evolucionForm, medico, paciente);

									//Si el paciente es de urgencias u hospitalizacion
									//hay que revisar el caso con el motivo de reversion
									if(evolucionForm.getDebeDarMotivoRegresionEgreso()!=null&&evolucionForm.getDebeDarMotivoRegresionEgreso().equals("true"))
									{
										egreso.crearEgresoDesdeEvolucionTransaccional(con, "continuar");
										egreso.actualizarInformacionMotivoReversionTransaccional(con, evolucionForm.isMotivoReversionEgresoBoolean(), numeroEvolucion, "finalizar");
									}
									else
									{
										egreso.crearEgresoDesdeEvolucionTransaccional(con, "finalizar");
									}


									//							************************************************************************+
									//EN ESTA PARTE SE HACE EL ASOCIO AUTOMATICO Y LA CREACION DE CUENTA HOSPITALIZACION
									//se revisa que la el destino de la salida sea Hospitalizacion o Cirugia Ambulatoria
									logger.info("\n\n");
									logger.info("---------------------------------------------------");
									logger.info("Evalua la generación del nuevo asocio Hospitalizacion-Hospitalizado");

									if(paciente.getCuentasPacienteArray(0).getCodigoTipoPaciente().equals(ConstantesBD.tipoPacienteCirugiaAmbulatoria+"")
											&& evolucionForm.getDestinoSalida()==ConstantesBD.codigoDestinoSalidaHospitalizar)
									{
										/////Para validar y marcar el ingreso como Reingreso/////

										evolucionForm.setMapaE(Utilidades.consultaUltimoEgresoEvolucion(con, paciente.getCodigoPersona(), paciente.getCodigoUltimaViaIngreso()));
										int minutosParametro=0, minutosT=0;
										if(Integer.parseInt(evolucionForm.getMapaE("numRegistros").toString())>0)
										{
											String fechaInicial=evolucionForm.getMapaE("fecha_0").toString();
											String horaInicial="";
											if(evolucionForm.getMapaE("hora_0").equals(""))
												horaInicial="00:00";
											else
												horaInicial=evolucionForm.getMapaE("hora_0").toString();
											String fechaFinal=UtilidadFecha.getFechaActual();
											String horaFinal=UtilidadFecha.getHoraActual();
											if(paciente.getCodigoUltimaViaIngreso()==ConstantesBD.codigoViaIngresoHospitalizacion)
												minutosParametro=Utilidades.convertirAEntero(ValoresPorDefecto.getTiempoMaximoReingresoHospitalizacion(medico.getCodigoInstitucionInt()),true);
											else
												if(paciente.getCodigoUltimaViaIngreso()==ConstantesBD.codigoViaIngresoUrgencias)
													minutosParametro=Utilidades.convertirAEntero(ValoresPorDefecto.getTiempoMaximoReingresoUrgencias(medico.getCodigoInstitucionInt()),true);
											minutosT=UtilidadFecha.numeroMinutosEntreFechas(fechaInicial, horaInicial, fechaFinal, horaFinal);
											if(minutosT<=minutosParametro)
											{
												evolucionForm.setIndI(1);
											}
										}
										////////////////////////////////////////////////////////
										if(egreso.generarEgresoCompleto(
												con,										
												UtilidadFecha.getFechaActual(), 
												UtilidadFecha.getFechaActual(), 
												UtilidadFecha.getHoraActual(), 
												UtilidadFecha.getHoraActual(), 
												medico, 
												paciente,
												false))
										{					
											//Se hace un asocio automatico
											Cuenta cuenta=new Cuenta();
											cuenta.init(System.getProperty("TIPOBD"));
											try
											{
												//logger.info("SE ASOCIA LA CUENTA!");
												if (cuenta.asociarCuenta(con, paciente.getCodigoCuenta(),paciente.getCodigoIngreso(), medico.getLoginUsuario()) <=0)
												{
													if (con!=null&&!con.isClosed())
													{
														UtilidadBD.closeConnection(con);
													}
													request.setAttribute("codigoDescripcionError", "errors.problemasBd");
													return mapping.findForward("paginaError");
												}
											}
											catch (Exception e)
											{
												if (con!=null&&!con.isClosed())
												{
													UtilidadBD.closeConnection(con);
												}
												request.setAttribute("codigoDescripcionError", "errors.problemasBd");
												return mapping.findForward("paginaError");
											}
											// C?igo necesario para notificar a todos los observadores que la cuentadel paciente en sesi? pudo haber cambiado
											UtilidadSesion.notificarCambiosObserver(paciente.getCodigoPersona(),servlet.getServletContext());

											//Quitar la cama en el encabezado de paciente
											PersonaBasica pacienteActivo = (PersonaBasica)session.getAttribute("pacienteActivo");
											pacienteActivo.setCama("");
											UtilidadesManejoPaciente.cargarPaciente(con, medico, pacienteActivo, request);

											logger.info("Respuesta: Fue Creado el Egreso y el Nuevo Asocio");
										}
										else
											logger.info("Respuesta: No se Genero el Egreso, ni el Asocio");
									}																	

									logger.info("---------------------------------------------------\n\n");
									//*************************************************************************************
									//*************************************************************************************							

									//*******************************************************************************************************************
								}
								else
								{
									logger.info("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
									if(evolucionForm.getDebeDarMotivoRegresionEgreso()!=null&&evolucionForm.getDebeDarMotivoRegresionEgreso().equals("true"))
									{
										Egreso egreso= new Egreso();
										evolucion.setTipoEvolucion(Evolucion.HOSPITALARIA);
										int numeroEvolucion=this.insercionBasicaEvolucion(request, con, evolucionForm, medico, paciente, evolucion, false);

										logger.info("numeroEvol1-->"+numeroEvolucion);

										evolucionForm.setNumeroSolicitud(numeroEvolucion);
										egreso.setNumeroCuenta(paciente.getCodigoCuenta());
										egreso.actualizarInformacionMotivoReversionTransaccional(con, evolucionForm.isMotivoReversionEgresoBoolean(), numeroEvolucion, "finalizar");
									}
									else
									{
										evolucion.setTipoEvolucion(Evolucion.HOSPITALARIA);
										evolucionForm.setNumeroSolicitud(this.insercionBasicaEvolucion(request, con, evolucionForm, medico, paciente, evolucion, true));
										logger.info("numeroEvol-->"+evolucionForm.getNumeroSolicitud());
									}

								}
							}
							//************************************************************************************************************************
							//************************************************************************************************************************
							//************************************************************************************************************************
							//************************************************************************************************************************
							//************************************************************************************************************************					
							else if (evolucionForm.getTipoEvolucion()=='u')
							{												
								logger.info("evolucionForm.getTipoEvolucion()-->"+evolucionForm.getTipoEvolucion());	
								EvolucionHospitalaria evolucion=new EvolucionHospitalaria();
								cargarObjeto(evolucion, evolucionForm);
								cargarObjetoHospitalizacion(evolucion, evolucionForm);
								evolucion.setMedico(medico);
								evolucion.setIdCuenta(paciente.getCodigoCuenta());
								evolucion.setIdCentroCosto(medico.getCodigoCentroCosto());

								logger.info("DIAGNOSTICO COMPLICACION1--->"+evolucionForm.getDiagnosticoComplicacion_1()+"  descripcion-->"+evolucionForm.getDescripcionComplicacion());
								logger.info("DIAGNOSTICO PRESUNTIVO1--->"+evolucionForm.getDiagnosticosPresuntivos());
								logger.info("DIAGNOSTICO DEFINITIVO1--->"+evolucionForm.getDiagnosticosDefinitivos()); 
								logger.info("NUM DIAGNOSTICO DEFINITIVO1--->"+evolucionForm.getNumDiagnosticosDefinitivos());
								logger.info("NUM DIAGNOSTICO presuntivo1--->"+evolucionForm.getNumDiagnosticosPresuntivos());


								evolucion.setCobrable(evolucionForm.isEsCobrable());
								if (evolucionForm.getOrdenSalida()!=null&&evolucionForm.getOrdenSalida().equals("true"))
								{	
									//Se debe revisar que la fecha dada por el usuario cumpla las condiciones
									String fechaEvolucionString[]=evolucion.getFechaEvolucion().split("/");
									int diaEvolucion=Integer.parseInt(fechaEvolucionString[0]);
									int mesEvolucion=Integer.parseInt(fechaEvolucionString[1]);
									int anioEvolucion=Integer.parseInt(fechaEvolucionString[2]);

									//La fecha llega en formato dd/mm/aaaa
									if(!paciente.isHospitalDia())
									{	
										RespuestaValidacion resp1=UtilidadValidacion.existenEvolucionesSuficientesOrdenSalida(con, paciente.getCodigoCuenta(), medico.getCodigoCentroCosto(),  diaEvolucion, mesEvolucion, anioEvolucion, esUrgencias, medico.getCodigoInstitucionInt());

										if (!resp1.puedoSeguir)
										{
											String warningSalida="";
											//En este caso aviso (NO se puede dar orden de salida)

											//Hay que buscar todas las fechas que faltan

											ArrayList fechasQueFaltan=UtilidadValidacion.obtenerListadoEvolucionesNoLlenadas (con, paciente.getCodigoCuenta(),  diaEvolucion, mesEvolucion, anioEvolucion, resp1.textoRespuesta, paciente.getCodigoCuentaAsocio());

											if (fechasQueFaltan!=null&&fechasQueFaltan.size()>0)
											{
												for (int i=0;i<fechasQueFaltan.size();i++)
												{
													if (i==0)
													{
														warningSalida=warningSalida+ " " + UtilidadFecha.conversionFormatoFechaAAp((String)fechasQueFaltan.get(i)); 
													}
													else
													{
														warningSalida=warningSalida+ ", " + UtilidadFecha.conversionFormatoFechaAAp((String)fechasQueFaltan.get(i)); 
													}
												}

												UtilidadBD.cerrarConexion(con);

												ActionErrors errors=new ActionErrors();
												errors.add("evolInexistentes", new ActionMessage("error.evolucion.faltanevoluciones", warningSalida,"fdsa"));
												this.saveErrors(request, errors);
												evolucionForm.setWarningOrdenSalida("");
												return mapping.findForward("evolucionPrincipal");
											}

										}
									}	
									//validaciones de CLAP
									/*if(paciente.getCodigoSexo()==ConstantesBD.codigoSexoFemenino)
							{
								String cuentaAsociada="";
								if(paciente.getExisteAsocio())
									cuentaAsociada=paciente.getCodigoCuentaAsocio()+"";

								Vector warningCLAPVector= UtilidadValidacion.puedoDarOrdenSalidaXClap(con, paciente.getCodigoCuenta()+"", paciente.getCodigoPersona()+"",cuentaAsociada);
								if(warningCLAPVector.size()>0)
								{
									String warningClapStr="";
									for(int w=0; w<warningCLAPVector.size(); w++)
									{
										if(w==0)
											warningClapStr+=warningCLAPVector.get(w);
										else
											warningClapStr+=", "+warningCLAPVector.get(w);
									}

									//evolucionForm.setPuedeDarOrdenSalida("false");
									UtilidadBD.cerrarConexion(con);
									ActionErrors errors=new ActionErrors();
									errors.add("", new ActionMessage("error.evolucion.noPuedeDarOrdenSalida", warningClapStr));
									this.saveErrors(request, errors);
									evolucionForm.setWarningOrdenSalida("");
									return mapping.findForward("evolucionPrincipal");
								}
							}	*/


									ActionForward posibleSalida=revisarQueNoHayaEvolucionAnterior(con, mapping, request, evolucion, paciente);
									if (posibleSalida!=null)
									{
										return posibleSalida;
									}

									evolucion.setTipoEvolucion(Evolucion.URGENCIAS);

									int numeroEvolucion=this.insercionBasicaEvolucion(request, con, evolucionForm, medico, paciente, evolucion, false);

									//Si hay orden de Salida debemos crear un objeto de tipo Egreso

									Egreso egreso= new Egreso();
									this.cargarEgreso(con, egreso, evolucionForm, numeroEvolucion, paciente, medico);

									//Se revisa estado del paciente para verificar si está muerto o vivo
									logger.info("Paciente Muerto? "+evolucionForm.getMuerte());
									if(evolucionForm.getMuerte()!=null&&UtilidadTexto.getBoolean(evolucionForm.getMuerte()))
										UtilidadValidacion.actualizarPacienteAMuertoTransaccional(con,paciente.getCodigoPersona(),false,evolucionForm.getFechaMuerte(), evolucionForm.getHoraMuerte(), evolucionForm.getCertificadoDefuncion(),ConstantesBD.continuarTransaccion);

									//si es orden de egreso y el destino de salida es: Dado de alta - remitido -salida coluntaria entonces se cambia el estaodo de la cama de ocupada a con salida
									this.cambiarEstadoCama(con, evolucionForm, medico, paciente);


									//Si el paciente es de urgencias u hospitalizacion
									//hay que revisar el caso con el motivo de reversion
									if(evolucionForm.getDebeDarMotivoRegresionEgreso()!=null&&evolucionForm.getDebeDarMotivoRegresionEgreso().equals("true"))
									{

										egreso.crearEgresoDesdeEvolucionTransaccional(con, "continuar");
										egreso.actualizarInformacionMotivoReversionTransaccional(con, evolucionForm.isMotivoReversionEgresoBoolean(), numeroEvolucion, "finalizar");
									}
									else
									{
										egreso.crearEgresoDesdeEvolucionTransaccional(con, "finalizar");
									}


									//************************************************************************+
									//EN ESTA PARTE SE HACE EL ASOCIO AUTOMATICO Y LA CREACION DE CUENTA HOSPITALIZACION
									//se revisa que la el destino de la salida sea Hospitalizacion o Cirugia Ambulatoria
									logger.info("\n\n");
									logger.info("\n\n");
									logger.info("---------------------------------------------------");
									logger.info("Evalua la generacion del nuevo asocio Hospitalizacion-Hospitalizado o Cirugia Ambulatoria");
									if(paciente.getCodigoCama() != 0 && 
											(evolucionForm.getDestinoSalida()==ConstantesBD.codigoDestinoSalidaHospitalizacion || 
													evolucionForm.getDestinoSalida()==ConstantesBD.codigoDestinoSalidaCirugiaAmbulatoria) 
													&&!paciente.getExisteAsocio())
									{								
										if(egreso.generarEgresoCompleto(
												con,										
												UtilidadFecha.getFechaActual(), 
												UtilidadFecha.getFechaActual(), 
												UtilidadFecha.getHoraActual(), 
												UtilidadFecha.getHoraActual(), 
												medico, 
												paciente,
												true))
										{								
											//Se hace un asocio automatico
											Cuenta cuenta=new Cuenta();
											cuenta.init(System.getProperty("TIPOBD"));
											try
											{										
												if (cuenta.asociarCuenta(con, paciente.getCodigoCuenta(),paciente.getCodigoIngreso(), medico.getLoginUsuario()) <=0)
												{
													if (con!=null&&!con.isClosed())
													{
														UtilidadBD.closeConnection(con);
													}
													request.setAttribute("codigoDescripcionError", "errors.problemasBd");
													return mapping.findForward("paginaError");
												}
												else
												{
													logger.info("Se Genero el Asocio!!.");
												}

											}
											catch (Exception e)
											{
												if (con!=null&&!con.isClosed())
												{
													UtilidadBD.closeConnection(con);
												}
												request.setAttribute("codigoDescripcionError", "errors.problemasBd");
												return mapping.findForward("paginaError");
											}
											// C?igo necesario para notificar a todos los observadores que la cuentadel paciente en sesi? pudo haber cambiado
											UtilidadSesion.notificarCambiosObserver(paciente.getCodigoPersona(),servlet.getServletContext());

											//Quitar la cama en el encabezado de paciente
											PersonaBasica pacienteActivo = (PersonaBasica)session.getAttribute("pacienteActivo");
											pacienteActivo.setCama("");
											UtilidadesManejoPaciente.cargarPaciente(con, medico, pacienteActivo, request);								


											logger.info("Respuesta: Fue Creado el Egreso y el Nuevo Asocio");
										}
										else
											logger.info("Respuesta: No se Genero el Egreso, ni el Asocio");
									}		
									else
										logger.info("Respuesta: No fue creado Nuevo Asocio >> Existe Cama >> "+paciente.getCodigoCama()+" >> Existe Asocio >> "+paciente.getExisteAsocio());

									logger.info("---------------------------------------------------\n\n");
									logger.info("\n\n");
									//*************************************************************************************
									//*************************************************************************************					
									//------------------------------------------------------------------------------------------
								}
								else
								{
									if(evolucionForm.getDebeDarMotivoRegresionEgreso()!=null&&evolucionForm.getDebeDarMotivoRegresionEgreso().equals("true"))
									{
										Egreso egreso= new Egreso();
										evolucion.setTipoEvolucion(Evolucion.URGENCIAS);
										int numeroEvolucion=this.insercionBasicaEvolucion(request, con, evolucionForm, medico, paciente, evolucion, false);

										egreso.setNumeroCuenta(paciente.getCodigoCuenta());
										egreso.actualizarInformacionMotivoReversionTransaccional(con, evolucionForm.isMotivoReversionEgresoBoolean(), numeroEvolucion, "finalizar");
									}
									else
									{
										evolucion.setTipoEvolucion(Evolucion.URGENCIAS);
										evolucionForm.setNumeroSolicitud(this.insercionBasicaEvolucion(request, con, evolucionForm, medico, paciente, evolucion, true));
									}

								}
							}
							else
							{

								Evolucion evolucion=new Evolucion();
								cargarObjeto(evolucion, evolucionForm);
								evolucion.setMedico(medico);
								evolucion.setIdCuenta(paciente.getCodigoCuenta());
								evolucion.setIdCentroCosto(medico.getCodigoCentroCosto());

								evolucion.setCobrable(evolucionForm.isEsCobrable());

								if (evolucionForm.getOrdenSalida()!=null&&evolucionForm.getOrdenSalida().equals("true"))
								{

									//Se debe revisar que la fecha dada por el usuario cumpla las condiciones
									String fechaEvolucionString[]=evolucion.getFechaEvolucion().split("/");
									int diaEvolucion=Integer.parseInt(fechaEvolucionString[0]);
									int mesEvolucion=Integer.parseInt(fechaEvolucionString[1]);
									int anioEvolucion=Integer.parseInt(fechaEvolucionString[2]);

									//La fecha llega en formato dd/mm/aaaa
									if(!paciente.isHospitalDia())
									{	
										RespuestaValidacion resp1=UtilidadValidacion.existenEvolucionesSuficientesOrdenSalida(con, paciente.getCodigoCuenta(), medico.getCodigoCentroCosto(),  diaEvolucion, mesEvolucion, anioEvolucion, esUrgencias, medico.getCodigoInstitucionInt());

										if (!resp1.puedoSeguir)
										{
											String warningSalida="No se pudo guardar la evolucion ni la orden de salida. Faltan las evoluciones de ";
											//En este caso aviso (NO se puede dar orden de salida)

											//Hay que buscar todas las fechas que faltan
											ArrayList fechasQueFaltan=UtilidadValidacion.obtenerListadoEvolucionesNoLlenadas (con, paciente.getCodigoCuenta(),  diaEvolucion, mesEvolucion, anioEvolucion, resp1.textoRespuesta, paciente.getCodigoCuentaAsocio());

											if (fechasQueFaltan!=null&&fechasQueFaltan.size()>0)
											{
												for (int i=0;i<fechasQueFaltan.size();i++)
												{
													if (i==0)
													{
														warningSalida=warningSalida+ " " + UtilidadFecha.conversionFormatoFechaAAp((String)fechasQueFaltan.get(i)); 
													}
													else
													{
														warningSalida=warningSalida+ ", " + UtilidadFecha.conversionFormatoFechaAAp((String)fechasQueFaltan.get(i)); 
													}
												}

												return ComunAction.accionSalirCasoError(mapping, request, con, logger, warningSalida +" sin contar la de hoy", warningSalida +" sin contar la de hoy", false) ;
											}
										}
									}	


									//validaciones de CLAP
									if(paciente.getCodigoSexo()==ConstantesBD.codigoSexoFemenino)
									{
										String cuentaAsociada="";
										if(paciente.getExisteAsocio())
											cuentaAsociada=paciente.getCodigoCuentaAsocio()+"";

										/**
										 * MT 7378 y MT 7399
										 * Se agrega la validación de destino de salida para mujeres con solicitudes de parto o cirugía.
										 * 
										 * @author jeilones
										 * @date 16/17/2013
										 * */
										Vector warningCLAPVector= UtilidadValidacion.puedoDarOrdenSalidaXClap(con, paciente.getCodigoCuenta()+"", paciente.getCodigoPersona()+"",cuentaAsociada,evolucionForm.getDestinoSalida(),true);
										if(warningCLAPVector.size()>0)
										{
											String warningClapStr="No se puede dar orden de salida. Porque: ";
											for(int w=0; w<warningCLAPVector.size(); w++)
											{
												if(w==0)
													warningClapStr+=warningCLAPVector.get(w);
												else
													warningClapStr+=", "+warningCLAPVector.get(w);
											}

											return ComunAction.accionSalirCasoError(mapping, request, con, logger, warningClapStr , warningClapStr, false) ;
										}
									}	

									evolucion.setTipoEvolucion(Evolucion.GENERAL);
									int numeroEvolucion=this.insercionBasicaEvolucion(request, con, evolucionForm, medico, paciente, evolucion, false);

									//Si hay orden de Salida debemos crear un objeto de tipo Egreso

									Egreso egreso= new Egreso();
									this.cargarEgreso(con, egreso, evolucionForm, numeroEvolucion, paciente, medico);
									egreso.crearEgresoDesdeEvolucionTransaccional(con, "finalizar");

								}
								else
								{
									evolucion.setTipoEvolucion(Evolucion.GENERAL);
									evolucionForm.setNumeroSolicitud(this.insercionBasicaEvolucion(request, con, evolucionForm, medico, paciente, evolucion, true));
								}

							}


							/////////////////////////////////////
							///// INICIO EPIDEMIOLOGIA

							//	UsuarioBasico usuario = (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
							/*
					if (Utilidades.tieneRolFuncionalidad(con,medico.getLoginUsuario(),ConstantesBD.codigoFuncionalidadNotificarCasos)) {

						switch (evolucionForm.getCodigoEnfermedadNotificable())
						{
							case 0:
							{
							    break;
							}
							case ConstantesBD.codigoFichaVIH:
							{
								FichaVIH fichaVihMundo = new FichaVIH();
								fichaVihMundo.setEstadoFicha(ConstantesBD.codigoEstadoFichaIncompleta);
								fichaVihMundo.setCodigoDiagnostico(evolucionForm.getCodigoDiagnostico());

							//	PersonaBasica paci = (PersonaBasica)request.getSession().getAttribute("pacienteActivo");

								fichaVihMundo.setLoginUsuario(medico.getLoginUsuario());
								fichaVihMundo.setNumeroSolicitud(evolucionForm.getNumeroSolicitud());
								fichaVihMundo.setCodigoPaciente(paciente.getCodigoPersona());
								fichaVihMundo.setCodigoConvenio(paciente.getCodigoConvenio());
								fichaVihMundo.setNombreProfesional(medico.getNombreUsuario());

								int cod = fichaVihMundo.insertarFicha(con);

								evolucionForm.setCodigoFicha(cod);
								fichaVihMundo.codigoGlobal = cod;

							    break;
							}
							case ConstantesBD.codigoFichaSarampion:
							{
								FichaSarampion fichaSarampionMundo = new FichaSarampion();
								fichaSarampionMundo.setEstado(ConstantesBD.codigoEstadoFichaIncompleta);
								fichaSarampionMundo.setCodigoDiagnostico(evolucionForm.getCodigoDiagnostico());

							//	PersonaBasica paci = (PersonaBasica)request.getSession().getAttribute("pacienteActivo");

								fichaSarampionMundo.setLoginUsuario(medico.getLoginUsuario());
								fichaSarampionMundo.setNumeroSolicitud(evolucionForm.getNumeroSolicitud());
								fichaSarampionMundo.setCodigoPaciente(paciente.getCodigoPersona());
								fichaSarampionMundo.setCodigoConvenio(paciente.getCodigoConvenio());
								fichaSarampionMundo.setNombreProfesional(medico.getNombreUsuario());

								int cod = fichaSarampionMundo.insertarFicha(con);

								evolucionForm.setCodigoFicha(cod);
								fichaSarampionMundo.codigoGlobal = cod;

							    break;
							}
							case ConstantesBD.codigoFichaRubeola:
							{
								FichaSarampion fichaSarampionMundo = new FichaSarampion();
								fichaSarampionMundo.setEstado(ConstantesBD.codigoEstadoFichaIncompleta);
								fichaSarampionMundo.setCodigoDiagnostico(evolucionForm.getCodigoDiagnostico());

							//	PersonaBasica paci = (PersonaBasica)request.getSession().getAttribute("pacienteActivo");

								fichaSarampionMundo.setLoginUsuario(medico.getLoginUsuario());
								fichaSarampionMundo.setNumeroSolicitud(evolucionForm.getNumeroSolicitud());
								fichaSarampionMundo.setCodigoPaciente(paciente.getCodigoPersona());
								fichaSarampionMundo.setCodigoConvenio(paciente.getCodigoConvenio());
								fichaSarampionMundo.setNombreProfesional(medico.getNombreUsuario());

								int cod = fichaSarampionMundo.insertarFicha(con);

								evolucionForm.setCodigoFicha(cod);
								fichaSarampionMundo.codigoGlobal = cod;

							    break;
							}
							case ConstantesBD.codigoFichaRabia:
							{ 
							    FichaRabia fichaRabiaMundo = new FichaRabia();
							    fichaRabiaMundo.setEstado(ConstantesBD.codigoEstadoFichaIncompleta);
							    fichaRabiaMundo.setCodigoDiagnostico(evolucionForm.getCodigoDiagnostico());

							//    PersonaBasica paci = (PersonaBasica)request.getSession().getAttribute("pacienteActivo");

							    fichaRabiaMundo.setLoginUsuario(medico.getLoginUsuario());
							    fichaRabiaMundo.setNumeroSolicitud(evolucionForm.getNumeroSolicitud());
							    fichaRabiaMundo.setCodigoPaciente(paciente.getCodigoPersona());
							    fichaRabiaMundo.setCodigoConvenio(paciente.getCodigoConvenio());
							    fichaRabiaMundo.setNombreProfesional(medico.getNombreUsuario());

							    int cod = fichaRabiaMundo.insertarFichaRabia(con);

							    evolucionForm.setCodigoFicha(cod);
							    fichaRabiaMundo.codigoGlobal = cod;

							    break;
							}
							case ConstantesBD.codigoFichaFiebreAmarilla:
							{
								FichaDengue fichaDengueMundo = new FichaDengue();
								fichaDengueMundo.setEstadoFicha(ConstantesBD.codigoEstadoFichaIncompleta);
								fichaDengueMundo.setCodigoDiagnostico(evolucionForm.getCodigoDiagnostico());

							//	PersonaBasica paci = (PersonaBasica)request.getSession().getAttribute("pacienteActivo");

								fichaDengueMundo.setLoginUsuario(medico.getLoginUsuario());
								fichaDengueMundo.setNumeroSolicitud(evolucionForm.getNumeroSolicitud());
								fichaDengueMundo.setCodigoPaciente(paciente.getCodigoPersona());
								fichaDengueMundo.setCodigoConvenio(paciente.getCodigoConvenio());
								fichaDengueMundo.setNombreProfesional(medico.getNombreUsuario());

								int cod = fichaDengueMundo.insertarFicha(con);

								evolucionForm.setCodigoFicha(cod);
								fichaDengueMundo.codigoGlobal = cod;

							    break;
							}
							case ConstantesBD.codigoFichaDengue:
							{
								FichaDengue fichaDengueMundo = new FichaDengue();
								fichaDengueMundo.setEstadoFicha(ConstantesBD.codigoEstadoFichaIncompleta);
								fichaDengueMundo.setCodigoDiagnostico(evolucionForm.getCodigoDiagnostico());

							//	PersonaBasica paci = (PersonaBasica)request.getSession().getAttribute("pacienteActivo");

								fichaDengueMundo.setLoginUsuario(medico.getLoginUsuario());
								fichaDengueMundo.setNumeroSolicitud(evolucionForm.getNumeroSolicitud());
								fichaDengueMundo.setCodigoPaciente(paciente.getCodigoPersona());
								fichaDengueMundo.setCodigoConvenio(paciente.getCodigoConvenio());
								fichaDengueMundo.setNombreProfesional(medico.getNombreUsuario());

								int cod = fichaDengueMundo.insertarFicha(con);

								evolucionForm.setCodigoFicha(cod);
								fichaDengueMundo.codigoGlobal = cod;

							    break;
							}
							case ConstantesBD.codigoFichaParalisisFlacida:
							{
								FichaParalisis fichaParalisisMundo = new FichaParalisis();
								fichaParalisisMundo.setEstadoFicha(ConstantesBD.codigoEstadoFichaIncompleta);
								fichaParalisisMundo.setCodigoDiagnostico(evolucionForm.getCodigoDiagnostico());

							//	PersonaBasica paci = (PersonaBasica)request.getSession().getAttribute("pacienteActivo");

								if (paciente.getEdad()<15) {

									fichaParalisisMundo.setLoginUsuario(medico.getLoginUsuario());
									fichaParalisisMundo.setNumeroSolicitud(evolucionForm.getNumeroSolicitud());
									fichaParalisisMundo.setCodigoPaciente(paciente.getCodigoPersona());
									fichaParalisisMundo.setCodigoConvenio(paciente.getCodigoConvenio());
									fichaParalisisMundo.setNombreProfesional(medico.getNombreUsuario());

									int cod = fichaParalisisMundo.insertarFicha(con);

									evolucionForm.setCodigoFicha(cod);
									fichaParalisisMundo.codigoGlobal = cod;
								}

							    break;
							}
							case ConstantesBD.codigoFichaSifilisCongenita:
							{
								FichaSifilis fichaSifilisMundo = new FichaSifilis();
								fichaSifilisMundo.setEstadoFicha(ConstantesBD.codigoEstadoFichaIncompleta);
								fichaSifilisMundo.setCodigoDiagnostico(evolucionForm.getCodigoDiagnostico());

							//	PersonaBasica paci = (PersonaBasica)request.getSession().getAttribute("pacienteActivo");

								fichaSifilisMundo.setLoginUsuario(medico.getLoginUsuario());
								fichaSifilisMundo.setNumeroSolicitud(evolucionForm.getNumeroSolicitud());
								fichaSifilisMundo.setCodigoPaciente(paciente.getCodigoPersona());
								fichaSifilisMundo.setCodigoConvenio(paciente.getCodigoConvenio());
								fichaSifilisMundo.setNombreProfesional(medico.getNombreUsuario());

								int cod = fichaSifilisMundo.insertarFicha(con);

								evolucionForm.setCodigoFicha(cod);
								fichaSifilisMundo.codigoGlobal = cod;

							    break;
							}
							case ConstantesBD.codigoFichaTetanosNeo:
							{
								FichaTetanos fichaTetanosMundo = new FichaTetanos();
								fichaTetanosMundo.setEstadoFicha(ConstantesBD.codigoEstadoFichaIncompleta);
								fichaTetanosMundo.setCodigoDiagnostico(evolucionForm.getCodigoDiagnostico());

							//	PersonaBasica paci = (PersonaBasica)request.getSession().getAttribute("pacienteActivo");

								fichaTetanosMundo.setLoginUsuario(medico.getLoginUsuario());
								fichaTetanosMundo.setNumeroSolicitud(evolucionForm.getNumeroSolicitud());
								fichaTetanosMundo.setCodigoPaciente(paciente.getCodigoPersona());
								fichaTetanosMundo.setCodigoConvenio(paciente.getCodigoConvenio());
								fichaTetanosMundo.setNombreProfesional(medico.getNombreUsuario());

								int cod = fichaTetanosMundo.insertarFicha(con);

								evolucionForm.setCodigoFicha(cod);
								fichaTetanosMundo.codigoGlobal = cod;

							    break;
							}
							case ConstantesBD.codigoFichaTuberculosis:
							{
								FichaTuberculosis fichaTuberculosisMundo = new FichaTuberculosis();
								fichaTuberculosisMundo.setEstadoFicha(ConstantesBD.codigoEstadoFichaIncompleta);
								fichaTuberculosisMundo.setCodigoDiagnostico(evolucionForm.getCodigoDiagnostico());

							//	PersonaBasica paci = (PersonaBasica)request.getSession().getAttribute("pacienteActivo");

								fichaTuberculosisMundo.setLoginUsuario(medico.getLoginUsuario());
								fichaTuberculosisMundo.setNumeroSolicitud(evolucionForm.getNumeroSolicitud());
								fichaTuberculosisMundo.setCodigoPaciente(paciente.getCodigoPersona());
								fichaTuberculosisMundo.setCodigoConvenio(paciente.getCodigoConvenio());
								fichaTuberculosisMundo.setNombreProfesional(medico.getNombreUsuario());

								int cod = fichaTuberculosisMundo.insertarFicha(con);

								evolucionForm.setCodigoFicha(cod);
								fichaTuberculosisMundo.codigoGlobal = cod;

							    break;
							}
							case ConstantesBD.codigoFichaMortalidadMaterna:
							{
								FichaMortalidad fichaMortalidadMundo = new FichaMortalidad();
								fichaMortalidadMundo.setEstadoFicha(ConstantesBD.codigoEstadoFichaIncompleta);
								fichaMortalidadMundo.setCodigoDiagnostico(evolucionForm.getCodigoDiagnostico());

								fichaMortalidadMundo.setLoginUsuario(medico.getLoginUsuario());
								fichaMortalidadMundo.setNumeroSolicitud(evolucionForm.getNumeroSolicitud());
								fichaMortalidadMundo.setCodigoPaciente(paciente.getCodigoPersona());
								fichaMortalidadMundo.setCodigoConvenio(paciente.getCodigoConvenio());
								fichaMortalidadMundo.setNombreProfesional(medico.getNombreUsuario());

								int cod = fichaMortalidadMundo.insertarFicha(con);

								evolucionForm.setCodigoFicha(cod);
								fichaMortalidadMundo.codigoGlobal = cod;
							}
							case ConstantesBD.codigoFichaMortalidadPerinatal:
							{
								FichaMortalidad fichaMortalidadMundo = new FichaMortalidad();
								fichaMortalidadMundo.setEstadoFicha(ConstantesBD.codigoEstadoFichaIncompleta);
								fichaMortalidadMundo.setCodigoDiagnostico(evolucionForm.getCodigoDiagnostico());

								fichaMortalidadMundo.setLoginUsuario(medico.getLoginUsuario());
								fichaMortalidadMundo.setNumeroSolicitud(evolucionForm.getNumeroSolicitud());
								fichaMortalidadMundo.setCodigoPaciente(paciente.getCodigoPersona());
								fichaMortalidadMundo.setCodigoConvenio(paciente.getCodigoConvenio());
								fichaMortalidadMundo.setNombreProfesional(medico.getNombreUsuario());

								int cod = fichaMortalidadMundo.insertarFicha(con);

								evolucionForm.setCodigoFicha(cod);
								fichaMortalidadMundo.codigoGlobal = cod;
							}
							default :
							{
								FichaGenerica fichaGenericaMundo = new FichaGenerica();
								fichaGenericaMundo.setEstadoFicha(ConstantesBD.codigoEstadoFichaIncompleta);
								fichaGenericaMundo.setCodigoDiagnostico(evolucionForm.getCodigoDiagnostico());

							//	PersonaBasica paci = (PersonaBasica)request.getSession().getAttribute("pacienteActivo");

								fichaGenericaMundo.setLoginUsuario(medico.getLoginUsuario());
								fichaGenericaMundo.setNumeroSolicitud(evolucionForm.getNumeroSolicitud());
								fichaGenericaMundo.setCodigoPaciente(paciente.getCodigoPersona());
								fichaGenericaMundo.setCodigoConvenio(paciente.getCodigoConvenio());
								fichaGenericaMundo.setNombreProfesional(medico.getNombreUsuario());

								int cod = fichaGenericaMundo.insertarFicha(con);

								evolucionForm.setCodigoFicha(cod);
								fichaGenericaMundo.codigoGlobal = cod;

							    break;
							}
						}
					}
							 */
							///// FIN EPIDEMIOLOGIA

							//**************SE ACTUALIZA LA INFORMACION DE EPIDEMIOLOGÍA****************************
							if(actualizarDatosEpidemiologia(con, evolucionForm, paciente, medico)<=0)
							{
								UtilidadBD.abortarTransaccion(con);
								return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Error actualizando informacion de epidemiología ", "errors.problemasBd", true);
							}
							//*****************************************************************************************


							try
							{
								//		Empieza lo de inf. parametrizable
								InformacionParametrizable infoParametrizable = new InformacionParametrizable();
								//		Es de la funcionalidad 1 (evolucion)
								evolucionForm.setCamposParametrizablesMap("codigoFuncionalidadParametrizable","1");
								evolucionForm.setCamposParametrizablesMap("codigoTabla",	"" + evolucionForm.getNumeroSolicitud());
								infoParametrizable.inicializarParaInsercion(con,evolucionForm.getCamposParametrizablesMapaCompleto(),medico,paciente.getCodigoArea());
								infoParametrizable.insertarTransaccional(con, "empezar");
								con.setAutoCommit(true);

								con.commit();
							}
							catch (SQLException e)
							{

								logger.error(e);
							}

							//					Termina lo de inf. parametrizable

							UtilidadBD.cerrarConexion(con);

							String paginaSiguiente=request.getParameter("paginaSiguiente");
							logger.info("\n\n\n\n\n\n *     *** * * * * * * *  ** * * * * * * * **************************************************paginaSiguiente-->"+paginaSiguiente);

							if (paginaSiguiente==null||paginaSiguiente.equals(""))
							{
								return mapping.findForward("resumenEvolucion");
							}
							else
							{

								evolucionForm.reset();
								response.sendRedirect(paginaSiguiente);
							}
						}
					}
					else if(estado.equals("imprimir_resumen_pdf")) {

						try
						{
							String tipoBD = System.getProperty("TIPOBD");
							DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
							if(con == null)
								con = myFactory.getConnection();
						}
						catch(Exception e)
						{
							e.printStackTrace();
							//No se cierra conexión porque si llega aca ocurrió un
							//error al abrirla
							request.setAttribute("codigoDescripcionError", "errors.problemasBd");
							return mapping.findForward("paginaError");
						}

						return accionImprimir(con, (EvolucionForm)form,paciente, request, mapping, medico);
					}
					else if(estado.equals("marcarReingreso")) {
						return this.accionMarcarReingreso(con, evolucionForm, mapping, paciente);
					}
					else
					{
						evolucionForm.reset();
						if( logger.isDebugEnabled() )
						{
							logger.debug("Usuario intento realizar una acción inexistente");
						}
						//Si llega a este punto es porque la accion no esta definida
						return ComunAction.accionSalirCasoError(mapping, request, con, logger, "El estado no estaba bien definido", "errors.estadoInvalido", true) ;
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
	
	
	private ActionForward accionMarcarReingreso(Connection con, EvolucionForm forma, ActionMapping mapping, PersonaBasica paciente)
	{
		Utilidades.marcarReingreso(con, paciente.getCodigoIngreso(), paciente.getCodigoPersona());
		UtilidadBD.closeConnection(con);
		return mapping.findForward("resumenEvolucion");
	}
	
	/**
	 * si es orden de egreso y el destino de salida es: Dado de alta - remitido -salida coluntaria entonces se cambia el estaodo de la cama de ocupada a con salida
	 * @param con
	 * @param evolucionForm
	 */
	private void cambiarEstadoCama(Connection con, EvolucionForm evolucionForm, UsuarioBasico usuario, PersonaBasica paciente) 
	{
		logger.info("***********************************************************ACTUALIZACION CAMA ******************************************************************");
		logger.info("\n DESTINO SALIDA-->"+evolucionForm.getDestinoSalida());
		if(evolucionForm.getDestinoSalida()==ConstantesBD.codigoDestinoSalidaDadoDeAlta
		 	|| evolucionForm.getDestinoSalida()==ConstantesBD.codigoDestinoSalidaRemitidoOtroNivelComplejidad
		 	|| evolucionForm.getDestinoSalida()==ConstantesBD.codigoDestinoSalidaVoluntaria
		 	|| evolucionForm.getDestinoSalida()==ConstantesBD.codigoDestinoSalidaHospitalizacion
		 	|| evolucionForm.getDestinoSalida()==ConstantesBD.codigoDestinoSalidaCirugiaAmbulatoria)
		{
			TrasladoCamas traslado= new TrasladoCamas();
			int codigoCama=Camas1.obtenerCamaDadaCuenta(con, paciente.getCodigoCuenta()+"");
			logger.info("CAMA-->"+codigoCama);
			if(codigoCama>0)
			{	
				try 
				{
					if(paciente.getCodigoUltimaViaIngreso()==ConstantesBD.codigoViaIngresoUrgencias)
					{
						traslado.modificarEstadoCama(con, Utilidades.convertirAEntero(ValoresPorDefecto.getCodigoEstadoCama(usuario.getCodigoInstitucionInt())), codigoCama, usuario.getCodigoInstitucionInt());
					}
					else
					{
						traslado.modificarEstadoCama(con, ConstantesBD.codigoEstadoCamaConSalida, codigoCama, usuario.getCodigoInstitucionInt());
					}

				} 
				catch (SQLException e) 
				{
					e.printStackTrace();
				}
			}	
		}
		logger.info("***********************************************************FIN CAMA ******************************************************************");
	}

	/**
	 * 
	 * @param con
	 * @param evolucionForm
	 * @param paciente
	 */
	private void cargarWarningsReferencia(Connection con, EvolucionForm evolucionForm, PersonaBasica paciente) 
	{
		String mensajeReferenciaInterna= UtilidadesHistoriaClinica.getMensajeReferenciaParaValidacion(con, paciente.getCodigoIngreso(), ConstantesBD.codigoNuncaValido+"", ConstantesIntegridadDominio.acronimoInterna);
		String mensajeReferenciaExterna= UtilidadesHistoriaClinica.getMensajeReferenciaParaValidacion(con, paciente.getCodigoIngreso(), ConstantesBD.codigoNuncaValido+"", ConstantesIntegridadDominio.acronimoExterna);
		if(!UtilidadTexto.isEmpty(mensajeReferenciaInterna) && !UtilidadTexto.isEmpty(mensajeReferenciaExterna))
			evolucionForm.setWarningReferencias(mensajeReferenciaInterna+"<br>"+mensajeReferenciaExterna);
		else
		{
			if(!UtilidadTexto.isEmpty(mensajeReferenciaInterna))
				evolucionForm.setWarningReferencias(mensajeReferenciaInterna);
			if(!UtilidadTexto.isEmpty(mensajeReferenciaExterna))
				evolucionForm.setWarningReferencias(mensajeReferenciaExterna);
		}
	}

	/**
	 * 
	 * @param con
	 * @param codigoCuenta
	 */
	private void cargarInformacionConductaSeguir(Connection con, int codigoCuenta, EvolucionForm forma, int codigoEvolOPCIONAL) throws IPSException
	{
		HashMap mapa=new HashMap();
		if(codigoEvolOPCIONAL<=0)
			mapa=Evolucion.conductaASeguirUltimaInsertada(con, codigoCuenta);
		else
			mapa=Evolucion.conductaASeguirDadaEvol(con, codigoEvolOPCIONAL);
		//si no carga nada entonces debe postular continuar en hsopi o en observacion 
		//ESTO FUE ACLARADO CON NURY EL 2007-10-25 A LAS 15:16
		if(mapa==null)
		{
			int viaIngreso=Cuenta.obtenerCodigoViaIngresoCuenta(con, codigoCuenta+"");
			if(viaIngreso==ConstantesBD.codigoViaIngresoHospitalizacion)
				forma.setCodigoConductaSeguir(ConstantesBD.codigoConductaASeguirContinuaHospitalOcirugiaAmbulaEvolucion);
			else if(viaIngreso==ConstantesBD.codigoViaIngresoUrgencias)
				forma.setCodigoConductaSeguir(ConstantesBD.codigoConductaASeguirContinuarObservacionEvolucion);
		}
		else
		{
			logger.info("\n\n\n\n\n mapaConductaASeguir->"+mapa);
			if(mapa.containsKey("codigoconductaseguir"))
				forma.setCodigoConductaSeguir(Integer.parseInt(mapa.get("codigoconductaseguir").toString()));
			if(mapa.containsKey("acronimotiporeferencia"))
				forma.setAcronimoTipoReferencia((mapa.get("acronimotiporeferencia").toString()));
		}
		logger.info("cod conducta seguir->"+forma.getCodigoConductaSeguir());
		logger.info("acrom tipo referencia->"+forma.getAcronimoTipoReferencia()+"++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++\n\n\n\n\n");
	}

	/**
	 * M?todo para consultar el hist?rico de los signos vitales de registro enfermer?a
	 * ingresados al paciente en las ?ltimas 24 horas
	 * @param con
	 * @param evolucionForm
	 * @param codigoInstitucion
	 * @param codigoArea
	 * @param codigoCuenta
	 * @param cuentaAsocio
	 */
	private void accionCargarHistoricoSignosVitales(Connection con, EvolucionForm evolucionForm, int codigoInstitucion, String cuentas) 
	{
		logger.info("1");
		RegistroEnfermeria mundo=new RegistroEnfermeria();
		
		logger.info("2");
		//---Se obtiene la fecha y hora del d?a anterior  
		String fechaInicio = UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.incrementarDiasAFecha(UtilidadFecha.getFechaActual(), -1, false)) + "-" + UtilidadFecha.getHoraActual();
		logger.info("3 fechaInicio->"+fechaInicio);
		
		//-----------Se cargan los tipos de signos vitales parametrizados por institucion centro de costo-----------//
		evolucionForm.setSignosVitalesInstitucionCcosto(mundo.consultarTiposInstitucionCCosto(con, codigoInstitucion, cuentas, 1));
		logger.info("4");
		
	 	//----Se quitan los tipos de signos vitales repetidos en la coleccion------//
		evolucionForm.setSignosVitalesInstitucionCcosto(Utilidades.coleccionSinRegistrosRepetidos(evolucionForm.getSignosVitalesInstitucionCcosto(), "codigo_tipo"));
		logger.info("5");
		
	 	//------------- Consulta el hist?rico de los signos vitales fijos--------------------------//
		evolucionForm.setSignosVitalesFijosHisto(mundo.consultarSignosVitalesFijosHisto(con, cuentas, fechaInicio, ""));
		logger.info("6");
		
	 	//------------- Consulta el historico de los signos vitales pamatrizados por institucion centro costo--------------------------//
	 	if(evolucionForm.getSignosVitalesInstitucionCcosto().size()>0)
	 		evolucionForm.setSignosVitalesParamHisto(mundo.consultarSignosVitalesParamHisto(con, cuentas, codigoInstitucion, fechaInicio, ""));
	 	
	 	logger.info("7");
	 	//------------- Consulta los codigo historicos, fecha registro y hora registro de los signos vitales fijos y parametrizados--------------------------//
	 	if(evolucionForm.getSignosVitalesFijosHisto().size()>0 || evolucionForm.getSignosVitalesParamHisto().size()>0)
	 		evolucionForm.setSignosVitalesHistoTodos(mundo.consultarSignosVitalesHistoTodos(con, cuentas, codigoInstitucion, fechaInicio, ""));
	 	logger.info("8");
	}

	/**
	 * Método que adiciona los diagnósticos de la valoración
	 * al hashmap de diagnosticos definitivos
	 * key = "diagval_"+i
	 * @param con
	 * @param evolucionForm
	 * @param paciente
	 */
	private void cargarDiagnosticosValoracion(Connection con, EvolucionForm evolucionForm, PersonaBasica paciente)
	{
		int numeroSolicitud=Utilidades.obtenerUltimaValoracionInterconsulta(con, paciente.getCodigoPersona());
		DtoValoracion valoracion = Valoraciones.cargarBase(con, numeroSolicitud+"");
		
		
		
		try
		{
			evolucionForm.setDiagnosticoDefinitivo("fechaval",valoracion.getFechaValoracion());
			for(int i=0;i<valoracion.getDiagnosticos().size();i++)
			{
				Diagnostico diag=(Diagnostico) valoracion.getDiagnosticos().get(i);
				evolucionForm.setDiagnosticoDefinitivo("diagvalacro"+i,diag.getAcronimo());
				evolucionForm.setDiagnosticoDefinitivo("diagvalnom"+i,diag.getNombre());
				evolucionForm.setDiagnosticoDefinitivo("diagvalcie"+i,diag.getTipoCIE()+"");
				evolucionForm.setDiagnosticoDefinitivo("diagvalprin"+i,diag.isPrincipal()+"");
			}
			evolucionForm.setDiagnosticoDefinitivo("numdiagval",valoracion.getDiagnosticos().size()+"");
		}
		catch (Exception e)
		{
			evolucionForm.setDiagnosticoDefinitivo("numdiagval","0");
			logger.error("Error cargando la valoración "+numeroSolicitud+": "+e);
		}
	}


	/**
	 * Método que toma los valores de balance de liquidos
	 * y los indexa en un vector para mandarlos a la BD
	 * @param evolucionForm
	 * @return Vactor con el balance de líquidos
	 */
	private Vector llenarBalanceLiquidos(HashMap balance)
	{
		if(balance.get("numRegistros")!=null)
		{
			
			int numeroBalanceLiquidos=Integer.parseInt(balance.get("numRegistros")+"");
			Vector balanceLiquidos=new Vector();
			for(int i=0;i<numeroBalanceLiquidos;i++)
			{
				Vector liquido=new Vector();
				liquido.add(balance.get("codigo_"+i)+"");
				liquido.add(balance.get("valor_"+i)+"");
				balanceLiquidos.add(liquido);
			}
			return balanceLiquidos;
		}
		return new Vector();
		
	}







//*************************************************************************************************************************************************	
	
	/**
	 * Genera la vista en formato .pdf del resumen de una evoluci&oacute;n.
	 * @param con conexion con la base de datos.
	 * @param form la forma que contiene los datos de la evoluci&oacute;n.
	 * @param paciente 
	 * @param request la solicitud http.
	 * @param pacienteCompleto el paciente con unos datos adicionales necesarios para la genraci&oacute;n de este tipo de reporte.
	 * @param paciente el paciente con los datos b&aacute;sicos necesarios para la generaci&oacute;n del reporte.
	 * @return un objeto de indicaci&oacute;n del lugar al que se debe redireccionar al cliente.
	 */
	private ActionForward accionImprimir(Connection con, EvolucionForm form, PersonaBasica paciente, HttpServletRequest request, ActionMapping mapping, UsuarioBasico usuario 
											)
	{

		

		// obtenemos el numero de la solicitud actual para cargar e imprimir la evolucion
    	String codigoEvolucionString = request.getParameter("numeroSolicitud");
    	int codigoEvolucionInt = 0, numSolicitudFormaAnterior = ((EvolucionForm)form).getNumeroSolicitud();
    	
    	if (codigoEvolucionString==null){
    		//Cuando el número de solicitud de la forma anterior
			//NO es 0 y el parametro llega con null, nos encontramos
			//en el caso despuès de una inserción o modificación,
			//donde se debe mostrar como quedaron los datos
			if(numSolicitudFormaAnterior>0)
			{
				codigoEvolucionInt=numSolicitudFormaAnterior;
			}
			else
			{
				try {
					//Si no paso nada de lo anterior el usuario no mando nada
					//y esta tratando de acceder directamente, sin pasar por
					//el menú
					return ComunAction.accionSalirCasoError(mapping, request, con, logger, "El usuario intentó consultar una evolución, pero en el request no llegó el código, intento de acceso no permitido", "error.evolucion.numeroSolicitudNoEspecificado", true);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		}
		else{
			try{
				codigoEvolucionInt=Integer.parseInt(codigoEvolucionString);
			}
			catch (Exception e){
				logger.error("El código de la evolución no es un entero");
				try {
					return ComunAction.accionSalirCasoError(mapping, request, con, logger, "El usuario intentó modificar, pero en el request no llegó el código, intento de acceso no permitido", "error.evolucion.numeroSolicitudNoEspecificado", true);
				} 
				catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		}
    	
    	HashMap datosEvolucion = cargarEvolucion(con, request, codigoEvolucionInt, form.getImprimirDiagnosticos(), form.getMapaBalanceLiquidos());
		

    	if(datosEvolucion.size() == 0){
				ArrayList atributosError = new ArrayList();
				atributosError.add("Imposible generar el documento PDF, ocurrio un error en su generacion. ");							
				request.setAttribute("atributosError", atributosError);					
				request.setAttribute("codigoDescripcionError", "errors.notEspecific");
				try
				{
					UtilidadBD.cerrarConexion(con);
				}
				catch (SQLException e)
				{
					logger.error("Error cerrando la conexión "+e);
					return null;
				}
				return mapping.findForward("paginaError");
    	}
    		
		//Generar nombre del archivo .pdf
		String nombreArchivo;
    	Random r=new Random();
    	nombreArchivo="/Evolucion" + r.nextInt()  +".pdf";
		
		
		
    	//Generar el archivo .pdf
    	EvolucionPdf.pdfEvolucion(nombreArchivo, datosEvolucion,paciente, usuario);
    	
 	    try{
			//nomEsquemaTarifario = mundo.getNombreEsquemaTarifario(con, codEsquemaTarifario);
			//logger.info("el nombre del esquema tarifario es: " + nomEsquemaTarifario);
			//TarifasInventarioPdf.pdfTarifasInventario(ValoresPorDefecto.getFilePath() + nombreArchivo , nomEsquemaTarifario, searchTarifasInventario(con, columns, (orderByField.equals("codigo")?"cod_articulo":"des_articulo"), codArticulo, descripcionArticulo, codEsquemaTarifario), usuario);
			UtilidadBD.cerrarConexion(con);
		}
 	    catch (SQLException e){
 	    	logger.error("Error cerrando la conexión");
		}
        request.setAttribute("nombreArchivo", nombreArchivo);
        request.setAttribute("nombreVentana", "Resumen de Evolucion");
        return mapping.findForward("abrirPdf");
	}


	
	
	
	

	/**
	 * obtengo la admision hospitalaria de un paciente si su ultima via de inreso fue por hospitalizacion
	 * @param con   una conexion con la fuente de datos
	 * @param paciente  el paciente al cual se le desea sasber la admision hopitalaria
	 * @return  la admison hospitalaria del paciente cargado, si este ingroso por hopitalizacion, de lo contrario devuleve null
	 */
	private AdmisionHospitalaria cargarAdmisionHospitalaria(Connection con, PersonaBasica paciente, Cuenta cuenta){
		AdmisionHospitalaria admision = new AdmisionHospitalaria();
		
		if(!cuenta.getCodigoViaIngreso().equals(ConstantesBD.codigoViaIngresoHospitalizacion+""))
			return null;
		
		if(paciente.getCodigoAdmision() != 0){
			admision.setCodigoAdmisionHospitalaria(paciente.getCodigoAdmision());
			admision.cargar(con);
		}
		else{
			try{
				admision.cargarUltimaAdmision(con, paciente.getCodigoPersona());
			}
			catch (SQLException e5){
				logger.error("Error consultando la admision hospitalaria despues de hacer egreso administrativo " + e5);
				return null;
			}
		}
		
		return admision;
	}

	

	/**
	 * metodo que carga la admision de urgencias de una paciente 
	 * @param con  una conexion con la fuente de datos
	 * @param paciente  el paciente al cual se le desea cargar la admision de urgencias
	 * @return  la admision de urgencias o null si no pudo ser cargada
	 */
	private AdmisionUrgencias cargarAdmisionUrgencias(Connection con, PersonaBasica paciente, Cuenta cuenta){
		TipoNumeroId id = new TipoNumeroId();
		
		int codigoAdmisionInt;
		int anioAdmisionInt;
		
		if(!cuenta.getViaIngreso().equals("Urgencias"))
			return null;
		
		AdmisionUrgencias admision = new AdmisionUrgencias();
		
		codigoAdmisionInt = paciente.getCodigoAdmision(); 
		anioAdmisionInt = paciente.getAnioAdmision(); 
		
		try{
			// no me pregunte porque, para cargar la admision de urgencias espera 
			// un objeto de este tipo pero con estos valores
			id.setNumeroId((new Integer(codigoAdmisionInt)).toString());
			id.setTipoId((new Integer(anioAdmisionInt)).toString());
		
			admision.cargar(con, id);
		}
		catch(Exception e){
			logger.error("Error consultando la admision de urgencias "+ e);
			return null;
		}
		
		return admision;
	}
	
	
	
	/**
	 * metodo que dependiendo de la ultima via de ingreso del paciente 
	 * devuelve el nombre de la cama
	 * @param cuenta  cuenta del paciente proppietario de la admision
	 * @param admiH  admision hospitalaria cargada
	 * @param admiU  admision urgencias cargada
	 * @return  el nombre de la cama o cadena vacia en caso de error o no encontrarla
	 */
	/*
	 * el llamado de este metodo esta comentarioado y genera un warning.
	private String getNombreCama(Cuenta cuenta, AdmisionHospitalaria admiH, AdmisionUrgencias admiU)
	{
		if(cuenta.getViaIngreso().equals("Hospitalización")){
		  if(admiH != null)
		  	return admiH.getNombreCama();
		  else
		  	return "";
		}

		if(cuenta.getViaIngreso().equals("Urgencias")){
			if(admiU != null)
				return admiU.getNombreCama();
			else
				return "";
		}

		return "";
	}
	*/
	
	
	/**
	 * metodo que dependiendo de la ultima via de ingreso del paciente 
	 * devuelve la descripcion de la cama
	 * @param cuenta  cuenta del paciente proppietario de la admision
	 * @param admiH  admision hospitalaria cargada
	 * @param admiU  admision urgencias cargada
	 * @param codigoPaciente
	 * @return  la descripcion de la cama o cadena vacia en caso de error o no encontrarla
	 */
	private String getDescripcionCama(Connection con,Cuenta cuenta, AdmisionHospitalaria admiH, AdmisionUrgencias admiU, int codigoPaciente){
				
		logger.info("\n via ingreso descripcion = " + cuenta.getViaIngreso()); 
		String[] cama = null;
				
		if(cuenta.getViaIngreso().equals("Hospitalización")){
		  if(admiH != null){
		  	//logger.info("\n admiH diferente de null  -- descripcion = " + admiH.getDescripcionCama());
		  	//return admiH.getDescripcionCama();
		  	cama = AdmisionHospitalaria.getCama(con, admiH.getCodigo(), codigoPaciente);

		  	if(cama != null)
		  		return cama[1];
		  	else
		  		return "";
		  }
		  else{
		  	//logger.info("\n al parecer admiH igual null ");
		  	return "";
		  }
		}

		if(cuenta.getViaIngreso().equals("Urgencias")){
			if(admiU != null)
				return admiU.getDescripcionCama();
			else
				return "";
		}

		return "";
	}
	
	
	
	/**
	 * metodo que carga una evolucion con todos sus datos en un HashMap
	 * @param con    una conexion con la fuente de datos
	 * @param request   el HttpServletRequest activo de esta sesion
	 * @param numeroSolicitud  el numero de la solicitud de la evolucion
	 * @param cargarDiagnosticos  boolean que define si se deben o no cargar los diagnosticos para impresion 
	 * @return   un HashMap con todos los datos de la evolucion
	 */
	public HashMap cargarEvolucion(Connection con, HttpServletRequest request, int numeroSolicitud, boolean cargarDiagnosticos, HashMap<Object, Object> balance){
		HttpSession session=request.getSession();
		PersonaBasica paciente = (PersonaBasica)session.getAttribute("pacienteActivo");
		Paciente pacienteCompleto = new Paciente();
		AdmisionHospitalaria admiH = new AdmisionHospitalaria();
		AdmisionUrgencias admiU = new AdmisionUrgencias();
  	
		
		try{
			pacienteCompleto.cargarPaciente(con, paciente.getCodigoPersona());
		} 
		catch(SQLException e1) {
			logger.error("Error cargando el paciente completo, excepcion: " + e1);
			return new HashMap();
		}
		
				

		
		
			// Datos: encabezado
    	String[] inst = ValidacionesSolicitud.obtenerInstitucion(con, numeroSolicitud, true);
    	String institucion = inst[1];
    	String nitInstitucion = inst[0];
    	

    	/*    	
    	ArrayList evolucionesList = cargarEvoluciones(con, request);
    	InfoEvolucion infoEvolucion = null;

    	logger.info("Listado de las evoluciones de este paciente: ");
    	for(int k=0; k<evolucionesList.size(); k++ ){
    		infoEvolucion = (InfoEvolucion) evolucionesList.get(k);
    		logger.info("evolucion nro " + (k+1));
    		logger.info("numero solicitud: " + infoEvolucion.getNumeroSolicitud() );
    		logger.info("fecha y hora: " + infoEvolucion.getFechaEvolucion() + ":" + infoEvolucion.getHoraEvolucion());
    	}
    	*/  	
    	
 
    	Evolucion evolucion = new Evolucion();

    	evolucion.setCodigo(numeroSolicitud);
		try{
			evolucion.cargar(con, numeroSolicitud);
		}
		catch (SQLException e) {
			logger.error("Error cargando la evolucion: "+e);
			return new HashMap();
		}

		
	  Cuenta cuenta = new Cuenta();
	  
		try{
			if(paciente.getCodigoCuenta()>0)
				cuenta.cargarCuenta(con, (new Integer(paciente.getCodigoCuenta())).toString());
			else
				cuenta.cargarCuenta(con, evolucion.getIdCuenta()+"");
		}
		catch(Exception e){
			logger.error("Error cargando la cuenta del paciente: " + e);
			return new HashMap();
		}

	  logger.info("\n via ingreso paciente: " + cuenta.getViaIngreso());
		
		
		admiH = cargarAdmisionHospitalaria(con, paciente, cuenta);
		admiU = cargarAdmisionUrgencias(con, paciente, cuenta);
		
		
		//Datos: información personal
		String apellidosNombres = paciente.getApellidosNombresPersona();
		String tipoIdentificacion = paciente.getCodigoTipoIdentificacionPersona();
		String numeroIdentificacion = paciente.getNumeroIdentificacionPersona();
		String ciudadIdentificacion = pacienteCompleto.getCiudadIdentificacion(false);
		String edad = paciente.getEdadDetallada();
		String sexo = paciente.getSexo();
		String ciudadResidencia = pacienteCompleto.getCiudad(false);
		String tmp = pacienteCompleto.getBarrio(false);
		StringTokenizer tokenizer = new StringTokenizer(tmp, "-");
		tokenizer.nextToken();
		String barrioResidencia = tokenizer.nextToken();
		//String numeroCama = getNombreCama(cuenta, admiH, admiU);
		String numeroCama = "";
		if(cuenta.getCodigoViaIngreso().equals(ConstantesBD.codigoViaIngresoHospitalizacion+""))
			numeroCama = AdmisionHospitalaria.getCama(con, admiH.getCodigo(), paciente.getCodigoPersona())[0];
		else if(cuenta.getCodigoViaIngreso().equals(ConstantesBD.codigoViaIngresoUrgencias+""))
			numeroCama = AdmisionUrgencias.getCama(con,admiU.getCodigo())[0];
		
		String descripcionCama = getDescripcionCama(con,cuenta, admiH, admiU, paciente.getCodigoPersona());

		String area = paciente.getArea();
		String tipoCuidado = evolucion.getProcedimientosQuirurgicosObstetricos();
  	
		//Datos: datos subjetivos
		String datosSubjetivos = evolucion.getInformacionDadaPaciente();
		Collection signosVitales = evolucion.getSignosVitales();
		
		String fechaEvolucion = evolucion.getFechaEvolucion();
		String horaEvolucion = evolucion.getHoraEvolucion();	
		
    	//Balance de liquidos
		/*
		String liquidosAdministrados = evolucion.getFechaYResultadoExamenesDiagnostico();
		String liquidosEliminados = evolucion.getResultadosTratamiento();
		String aporteHidrico = evolucion.getTratamiento();
		String aporteCalorico = evolucion.getCambiosManejo();
		String dioresis = evolucion.getObservaciones();
		*/
    	
		//Hallazgos Importantes
		String hallazgosImportantes = evolucion.getHallazgosImportantes();
    	
		//Analisis
		String analisis = evolucion.getDescripcionComplicacion();
    	
    	//Plan
		String plan = evolucion.getPronostico();
    	
    	
    	
    	
		//Diagnosticos
		Map dxIngreso = null;
		String dxComplicacionStr = "";
		Collection dxDefinitivos = null;

		
		if(cargarDiagnosticos == true)
		{
			if(cuenta.getCodigoViaIngreso().equals(ConstantesBD.codigoViaIngresoHospitalizacion+""))
				dxIngreso = this.cargarDiagnosticoIngreso(con, evolucion.getIdCuenta());
			else
			{
				dxIngreso = new HashMap();
				dxIngreso.put("acronimo","");
				dxIngreso.put("tipoCie","");
				dxIngreso.put("nombre","");
			}
			
			Diagnostico dxComplicacion = evolucion.getDiagnosticoComplicacion();	  
			dxComplicacionStr = "";
			if(dxComplicacion != null)
			{
				
				// si es diferente de 1 ( no seleccionado )
				if(!dxComplicacion.getAcronimo().equals("1") )
					dxComplicacionStr = dxComplicacion.getAcronimo() +"-" +dxComplicacion.getTipoCIE() + " " +  dxComplicacion.getNombre();
				
			}
		
			dxDefinitivos = evolucion.getDiagnosticosDefinitivos();
			
		}
		

		
		//Informacion del Egreso
		Map infoEgreso = this.cargarEgreso(con, evolucion.getCodigo());
		
		//Médico Responsable
		UsuarioBasico ub = evolucion.getMedico();
		String nombreUsuario = ub.getNombreUsuario();
		String numRegistroMedico = ub.getNumeroRegistroMedico();
		InfoDatosInt[] idi = ub.getEspecialidades();
		
		//Adicionar datos a la colección
		HashMap<Object, Object> datos = new HashMap<Object, Object>();
		
		datos.put("institucion", institucion);
		datos.put("nitInstitucion", nitInstitucion);
		datos.put("fechaEvolucion", fechaEvolucion);
		datos.put("horaEvolucion", horaEvolucion);
		
		datos.put("apellidosNombres", apellidosNombres);
		datos.put("tipoIdentificacion", tipoIdentificacion);
		datos.put("numeroIdentificacion", numeroIdentificacion);
		datos.put("ciudadIdentificacion", ciudadIdentificacion);
		datos.put("edad", edad);
		datos.put("sexo", sexo);
		datos.put("ciudadResidencia", ciudadResidencia);
		datos.put("barrioResidencia", barrioResidencia);
		datos.put("numeroCama", numeroCama);
		datos.put("descripcionCama", descripcionCama);
		datos.put("area", area);
		datos.put("tipoCuidado", tipoCuidado);
		
		datos.put("datosSubjetivos", datosSubjetivos);
		
		datos.put("signosVitales", signosVitales);
		
/*
		datos.put("liquidosAdministrados", liquidosAdministrados);
		datos.put("liquidosEliminados", liquidosEliminados);
		datos.put("aporteHidrico", aporteHidrico);
		datos.put("aporteCalorico", aporteCalorico);
		datos.put("dioresis", dioresis);
*/
		
		if(balance.get("numRegistros")!=null)
		{
			
			int numeroBalanceLiquidos=Integer.parseInt(balance.get("numRegistros")+"");
			Vector<Vector> balanceLiquidos=new Vector<Vector>();
			for(int i=0;i<numeroBalanceLiquidos;i++)
			{
				Vector<String> liquido=new Vector<String>();
				liquido.add(balance.get("nombre_"+i)+"");
				liquido.add(balance.get("valor_"+i)+"");
				balanceLiquidos.add(liquido);
			}
			datos.put("balanceLiquidos", balanceLiquidos);
		}
		
		datos.put("hallazgosImportantes", hallazgosImportantes);
		
		datos.put("analisis", analisis);
		
		datos.put("plan", plan);
		
		datos.put("dxIngreso", dxIngreso);
		datos.put("dxComplicacion", dxComplicacionStr);
		datos.put("dxDefinitivos", dxDefinitivos);
		
		datos.put("infoEgreso", infoEgreso);
		
		datos.put("nombreUsuario", nombreUsuario);
		datos.put("numRegistroMedico", numRegistroMedico);
		datos.put("idi", idi);
		datos.put("tipoDiagnosticoPrincipal", evolucion.getNombreTipoDiagnosticoPrincipal());
		
		datos.put("viaIngreso",cuenta.getCodigoViaIngreso());
		
		HashMap<Object, Object> mapaCamposParam=Utilidades.obtenerInformacionParametrizada(
									con, 
									ub.getCodigoPersona(),
									"1",
									"1",
									paciente.getCodigoArea(),
									ub.getCodigoInstitucion(),
									numeroSolicitud
									);
		
		datos.put("datos_subj_param", mapaCamposParam);

		mapaCamposParam=Utilidades.obtenerInformacionParametrizada(
				con, 
				ub.getCodigoPersona(),
				"1",
				"2",
				paciente.getCodigoArea(),
				ub.getCodigoInstitucion(),
				numeroSolicitud
				);

		datos.put("datos_ogj_param", mapaCamposParam);
		mapaCamposParam=Utilidades.obtenerInformacionParametrizada(
				con, 
				ub.getCodigoPersona(),
				"1",
				"3",
				paciente.getCodigoArea(),
				ub.getCodigoInstitucion(),
				numeroSolicitud
				);

		datos.put("analisis_param", mapaCamposParam);
		mapaCamposParam=Utilidades.obtenerInformacionParametrizada(
				con, 
				ub.getCodigoPersona(),
				"1",
				"4",
				paciente.getCodigoArea(),
				ub.getCodigoInstitucion(),
				numeroSolicitud
				);

		datos.put("plan_param", mapaCamposParam);
		return datos;
	}
	
	
	
	
	/**
	 * Metodo que carga todas la evoluciones del paciente activo
	 * @param con una conexion con la fuente de datos
	 * @param request  el HttpRequest de la sesion actual
	 * @return  un ArrayList que contiene objetos InfoEvolucion de todas las evoluciones del paciente activo
	 */	
	public ArrayList cargarEvoluciones(Connection con, HttpServletRequest request){
		HttpSession session = request.getSession();
		PersonaBasica paciente = (PersonaBasica)session.getAttribute("pacienteActivo");
	    HistoricoEvoluciones hEvoluciones = new HistoricoEvoluciones();

	    try{
	        //Si la cuenta no esta abierta debemos mostrar todas las evoluciones del mismo
			if(paciente.getCodigoCuenta() == 0){
				//La cuenta no esta abierta debemos mostrar todas las evoluciones de este paciente
				hEvoluciones.cargar(con, paciente.getCodigoTipoIdentificacionPersona(), paciente.getNumeroIdentificacionPersona());
			}
			else if(request.getParameter("codigoCuenta")!=null){
				int codigoCuenta=0;
				try{
					codigoCuenta = Integer.parseInt(request.getParameter("codigoCuenta"));
				}
				catch(NumberFormatException e){
					codigoCuenta = paciente.getCodigoCuenta();
				}
				hEvoluciones.cargar(con, codigoCuenta);
			}
			else{
				if(!paciente.getExisteAsocio()){
					//Si no existe asocio, cargamos el de la cuenta del paciente actual
					hEvoluciones.cargar(con, paciente.getCodigoCuenta());
				}
				else{
					//Si lo hay cargamos el de las dos cuentas
					hEvoluciones.cargarEvolucionesCuentaYAsocio(con, paciente.getCodigoCuenta(), paciente.getCodigoCuentaAsocio());
				}
			}
	    }
	    catch(Exception e){
	    	logger.error("Error al obtener el listado de evoluciones del paciente");
	    	return new ArrayList();
	    }

		return hEvoluciones.getEvoluciones(); 
	}
	
	
	
	
	

	
	
	
	/**
	 * Método que recibe un objeto evolucion y un bean
	 * EvolucionForm y llena el objeto con la información 
	 * existe8 nte en la forma
	 * 
	 * @param evolucion Objeto a llenar
	 * @param bean Bean con datos 
	 */
	public void cargarObjeto(Evolucion evolucion, EvolucionForm bean)
	{
		int i=0;
		String arregloTemporal[];
		evolucion.setFechaEvolucion(bean.getFechaEvolucion());
		evolucion.setHoraEvolucion(bean.getHoraEvolucion());
		evolucion.setRecargo(bean.getRecargo());
		ArrayList signosVitalesArray=new ArrayList();
		//Empezamos en 1 porque los signos vitales empiezan en 1
		for (i=0;i<bean.getNumSignosVitales();i++)
		{
			int codigo=Integer.parseInt((String)bean.getSignoVital("desc_"+ i));
			logger.info("codigo "+codigo);
			String valor=(String)bean.getSignoVital(""+ codigo);
			logger.info("valor "+valor);
			if(util.UtilidadCadena.noEsVacio(valor) )
			{
				SignoVital signoVitalTemporal=new SignoVital();
				
				signoVitalTemporal.setCodigo(codigo);
				signoVitalTemporal.setValorSignoVital(valor);
				signoVitalTemporal.setDescripcion("");
				signosVitalesArray.add(signoVitalTemporal);
			}
		}
		evolucion.setSignosVitales(signosVitalesArray);
		evolucion.setCodigoValoracion(bean.getCodigoValoracion());
		
		//El diagnostico de ingreso solo se muestra
		Diagnostico diagnosticoComplicacion=new Diagnostico();

		//Asumimos que el diagnostico de complicación no fué seleccionado
		diagnosticoComplicacion.setAcronimo("1");
		diagnosticoComplicacion.setTipoCIE(0);

		//Nos aseguramos de la presencia del diagnostico
		//de complicación
		
		if (bean.getDiagnosticoComplicacion_1()!=null)
		{
			arregloTemporal=bean.getDiagnosticoComplicacion_1().split(ConstantesBD.separadorSplit, 3);
			
			if (arregloTemporal.length==3)
			{
				diagnosticoComplicacion.setAcronimo(arregloTemporal[0]);
				diagnosticoComplicacion.setTipoCIE(Integer.parseInt(arregloTemporal[1]));
			}
		}
		//Sin importar el diagnostico hay que agregarlo:
		evolucion.setDiagnosticoComplicacion(diagnosticoComplicacion);
		
		//se agrega el diagnostico principal
		if(bean.getDiagnosticosPresuntivos().containsKey("principal"))
		{
			Diagnostico diagnosticoTemporal=new Diagnostico();	
			if ((!bean.getDiagnosticoPresuntivo("principal").toString().equals("null") && !bean.getDiagnosticoPresuntivo("principal").toString().equals("")))
			{
				logger.info("SE ASIGNA EL DIAGNOSTICO PRINCIPAL EN PRESUNTIVOS!!!!!!!");
				arregloTemporal=((String)bean.getDiagnosticoPresuntivo("principal")).split(ConstantesBD.separadorSplit,3);	
				diagnosticoTemporal.setAcronimo(arregloTemporal[0]);
				diagnosticoTemporal.setTipoCIE(Integer.parseInt(arregloTemporal[1]));
				//Por estandar el principal tiene número igual -1
				diagnosticoTemporal.setNumero(-1);
				diagnosticoTemporal.setPrincipal(true);
				evolucion.getDiagnosticosPresuntivos().add(diagnosticoTemporal);
			}
		}
		
		//se agrega el diag ppal
		if(bean.getDiagnosticosDefinitivos().containsKey("principal"))
		{
			Diagnostico diagnosticoTemporal=new Diagnostico();	
			if ((!bean.getDiagnosticoDefinitivo("principal").toString().equals("null") && !bean.getDiagnosticoDefinitivo("principal").toString().equals("")))
			{
				logger.info("SE ASIGNA EL DIAGNOSTICO PRINCIPAL EN DEFINITIVOS!!!!!!!");	
				arregloTemporal=((String)bean.getDiagnosticoDefinitivo("principal")).split(ConstantesBD.separadorSplit,3);	
				diagnosticoTemporal.setAcronimo(arregloTemporal[0]);
				diagnosticoTemporal.setTipoCIE(Integer.parseInt(arregloTemporal[1]));
				//Por estandar el principal tiene número igual -1
				diagnosticoTemporal.setNumero(-1);
				diagnosticoTemporal.setPrincipal(true);
				evolucion.getDiagnosticosDefinitivos().add(diagnosticoTemporal);
			}
		}
		
		//@todo arreglar que esta llegando en 1
		for (i=0;i<bean.getNumDiagnosticosPresuntivos();i++)
		{
			Diagnostico diagnosticoTemporal=new Diagnostico();
			arregloTemporal=((String)bean.getDiagnosticoPresuntivo("relacionado_" + i)).split(ConstantesBD.separadorSplit,3);
			logger.info("RELACIONADOS presun-->"+arregloTemporal);
			if (arregloTemporal.length==3)
			{
				if ((String)bean.getDiagnosticoPresuntivo("checkbox_"+i) != null&& ((String)bean.getDiagnosticoPresuntivo("checkbox_"+i)).equals("true"))
				{
					logger.info("I presun->"+i);
					//Hay que revisar que el checkbox haya sido seleccionado, si no fué 
					//que el usuario la embarró..
					diagnosticoTemporal.setAcronimo(arregloTemporal[0]);
					diagnosticoTemporal.setTipoCIE(Integer.parseInt(arregloTemporal[1]));
					diagnosticoTemporal.setNumero(i+1);
					diagnosticoTemporal.setPrincipal(false);
					evolucion.getDiagnosticosPresuntivos().add(diagnosticoTemporal);
				}
			}
		}

		for (i=0;i<bean.getNumDiagnosticosDefinitivos();i++)
		{
			Diagnostico diagnosticoTemporal=new Diagnostico();
			arregloTemporal=((String)bean.getDiagnosticoDefinitivo("relacionado_" + i)).split(ConstantesBD.separadorSplit,3);
			logger.info("RELACIONADOS def-->"+arregloTemporal);
			if (arregloTemporal.length==3)
			{
				if ((String)bean.getDiagnosticoDefinitivo("checkbox_"+i) != null&& ((String)bean.getDiagnosticoDefinitivo("checkbox_"+i)).equals("true"))
				{
					logger.info("I def->"+i);
					//Hay que revisar que el checkbox haya sido seleccionado, si no fué 
					//que el usuario la embarró..
					diagnosticoTemporal.setAcronimo(arregloTemporal[0]);
					diagnosticoTemporal.setTipoCIE(Integer.parseInt(arregloTemporal[1]));
					diagnosticoTemporal.setNumero(i+1);
					diagnosticoTemporal.setPrincipal(false);
					evolucion.getDiagnosticosDefinitivos().add(diagnosticoTemporal);
				}
			}
		}
		//Información dada por el paciente
		evolucion.setInformacionDadaPaciente(bean.getInformacionDadaPaciente());
		//Hallazgos importantes
		evolucion.setHallazgosImportantes(bean.getHallazgosImportantes());
		//Procedimientos Quirúrgicos y Obstétricos
		evolucion.setProcedimientosQuirurgicosObstetricos(bean.getProcedimientosQuirurgicosYObstetricos());
		//Fecha y Resultado examenes diagnosticos
		evolucion.setFechaYResultadoExamenesDiagnostico(bean.getFechaResultadosExamenesDiagnostico());
		//Descripción de la complicación
		evolucion.setDescripcionComplicacion(bean.getDescripcionComplicacion());
		//Tratamiento
		evolucion.setTratamiento(bean.getTratamiento());
		//Resultados del Tratamiento
		evolucion.setResultadosTratamiento(bean.getResultadosTratamiento());
		//Cambios en el manejo dado al paciente
		evolucion.setCambiosManejo(bean.getCambiosManejo());
		//Pronóstico
		evolucion.setPronostico(bean.getPronostico());
		//Observaciones 
		evolucion.setObservaciones(bean.getObservaciones());
		
		//Ahora el del egreso
		/*if (bean.getDebeDarMotivoRegresionEgreso().equals("true"))
		{
			evolucion.setMotivoReversion(bean.getMotivoReversionEgreso());
		}
		else
		{
			//Si no se va en nulo
			evolucion.setMotivoReversion(null);
		}*/

		//Por alguna extraña razon el checkbox no se comporta como
		//se espera y queda con valor al reves, esta es la razón del !
		evolucion.setCobrable(!bean.isEsCobrable());
		
		if (bean.getOrdenSalida()!=null&&bean.getOrdenSalida().equals("true"))
		{
			evolucion.setOrdenSalida(true);
		}
		else
		{
			evolucion.setOrdenSalida(false);
		}
		
		evolucion.setTipoDiagnosticoPrincipal(bean.getTipoDiagnosticoPrincipal());
		
		evolucion.setCodigoConductaSeguir(bean.getCodigoConductaSeguir());
		evolucion.setAcronimoTipoReferencia(bean.getAcronimoTipoReferencia());
	}

	/**
	 * Método auxiliar que permite llenar los boolean específicos 
	 * a la evolución hospitalaria . Se revisa para los boolean de 
	 * las observaciones que haya  algo escrito, si no lo tiene no se
	 * graba
	 * 
	 * @param evolucionHospitalaria Objeto a llenar
	 * @param bean Bean con datos
	 */
	public void cargarObjetoHospitalizacion(EvolucionHospitalaria evolucionHospitalaria, EvolucionForm bean)
	{
		//Signos Vitales
		if (bean.isSignosVitalesBoolean())
		{
			evolucionHospitalaria.setSignosVitalesEpicrisis(true);		
		}
		else
		{
			evolucionHospitalaria.setSignosVitalesEpicrisis(false);
		}
		
		//Diagnosticos Presuntivos
		if (bean.isDiagnosticosPresuntivosBoolean())
		{
			evolucionHospitalaria.setDiagnosticosPresuntivosEpicrisis(true);
		}
		else
		{
			evolucionHospitalaria.setDiagnosticosPresuntivosEpicrisis(false);
		}
		
		//Diagnosticos Definitivos
		if (bean.isDiagnosticosDefinitivosBoolean())
		{
			evolucionHospitalaria.setDiagnosticosDefinitivosEpicrisis(true);
		}
		else
		{
			evolucionHospitalaria.setDiagnosticosDefinitivosEpicrisis(false);
		}
		
		//Información dada por el paciente
		if (bean.isInformacionDadaPacienteBoolean()&&bean.getInformacionDadaPaciente()!=null&&!bean.getInformacionDadaPaciente().equals(""))
		{
			evolucionHospitalaria.setInfoDadaPacienteEpicrisis(true);
		}
		else
		{
			evolucionHospitalaria.setInfoDadaPacienteEpicrisis(false);
		}
		//Hallazgos Importantes
		if (bean.isHallazgosImportantesBoolean()&&bean.getHallazgosImportantes()!=null&&!bean.getHallazgosImportantes().equals(""))
		{
			evolucionHospitalaria.setHallazgosEpicrisis(true);
		}
		else
		{
			evolucionHospitalaria.setHallazgosEpicrisis(false);
		}
		//Proc. Quirurgicos y Obstetricos
		if (bean.isProcedimientosQuirurgicosYObstetricosBoolean()&&bean.getProcedimientosQuirurgicosYObstetricos()!=null&&!bean.getProcedimientosQuirurgicosYObstetricos().equals(""))
		{
			evolucionHospitalaria.setProcedimientosQuirurgicosObstetricosEpicrisis(true);
		}
		else
		{
			evolucionHospitalaria.setProcedimientosQuirurgicosObstetricosEpicrisis(false);
		}
		//Fecha y res. Examenes Diagnostico
		if (bean.isFechaResultadosExamenesDiagnosticoBoolean()&&bean.getFechaResultadosExamenesDiagnostico()!=null&&!bean.getFechaResultadosExamenesDiagnostico().equals(""))
		{
			evolucionHospitalaria.setFechaYResultadoExamenesDiagnosticoEpicrisis(true);
		}
		else
		{
			evolucionHospitalaria.setFechaYResultadoExamenesDiagnosticoEpicrisis(false);
		}

		//Descripción Complicacion
		if (bean.isDescripcionComplicacionBoolean()&&bean.getDescripcionComplicacion()!=null&&!bean.getDescripcionComplicacion().equals(""))
		{
			evolucionHospitalaria.setComplicacionesEpicrisis(true);
		}
		else
		{
			evolucionHospitalaria.setComplicacionesEpicrisis(false);
		}
		//Tratamiento
		if (bean.isTratamientoBoolean()&&bean.getTratamiento()!=null&&!bean.getTratamiento().equals(""))
		{
			evolucionHospitalaria.setTratamientoEpicrisis(true);
		}
		else
		{
			evolucionHospitalaria.setTratamientoEpicrisis(false);
		}
		//Resultados Tratamiento
		if (bean.isResultadosTratamientoBoolean()&&bean.getResultadosTratamiento()!=null&&!bean.getResultadosTratamiento().equals(""))
		{
			evolucionHospitalaria.setResultadosTratamientoEpicrisis(true);
		}
		else
		{
			evolucionHospitalaria.setResultadosTratamientoEpicrisis(false);
		}
		//Cambios Manejo Paciente
		if (bean.isCambiosManejoBoolean()&&bean.getCambiosManejo()!=null&&!bean.getCambiosManejo().equals(""))
		{
			evolucionHospitalaria.setCambiosManejoEpicrisis(true);
		}
		else
		{
			evolucionHospitalaria.setCambiosManejoEpicrisis(false);
		}
		//Pronostico
		if (bean.isPronosticoBoolean()&&bean.getPronostico()!=null&&!bean.getPronostico().equals(""))
		{
			evolucionHospitalaria.setPronosticoEpicrisis(true);
		}
		else
		{
			evolucionHospitalaria.setPronosticoEpicrisis(false);
		}
		//Observaciones
		if (bean.isObservacionesBoolean()&&bean.getObservaciones()!=null&&!bean.getObservaciones().equals(""))
		{
			evolucionHospitalaria.setObservacionesEpicrisis(true);
		}
		else
		{
			evolucionHospitalaria.setObservacionesEpicrisis(false);
		}
	}

	public void cargarEgreso (Connection con, Egreso egreso, EvolucionForm evolucionForm, int numeroEvolucion, PersonaBasica paciente, UsuarioBasico medico) throws SQLException
	{
		//Ahora le vamos a llenar los datos que sean necesarios a este
		//objeto
		egreso.setNumeroCuenta(paciente.getCodigoCuenta());
		egreso.setNumeroEvolucion(numeroEvolucion);
		
		if (evolucionForm.getMuerte().equals("true"))
		{
			egreso.setEstadoSalida(true);
		}
		else
		{
			egreso.setEstadoSalida(false);
		}
		egreso.setDestinoSalida(new InfoDatos("" + evolucionForm.getDestinoSalida(), "Nombre No Disponible"));
		egreso.setOtroDestinoSalida(evolucionForm.getOtroDestinoSalida());

		//Si estamos llenando el egreso es porque hay una admisión
		//luego la cargamos utilizando la función auxiliar del egreso
		
		egreso.cargarNumeroAutorizacionAdmision(con, paciente.getCodigoAdmision(), evolucionForm.getTipoEvolucion());


		egreso.setCausaExterna(new InfoDatos("1", "No definida"));
		egreso.cargarCausaExternaUltimaValoracionUH(con, paciente.getCodigoCuenta(), evolucionForm.getTipoEvolucion());

		String arregloTemporal[];

		if (evolucionForm.getDiagnosticoMuerte_1()!=null)
		{
			arregloTemporal= evolucionForm.getDiagnosticoMuerte_1().split(ConstantesBD.separadorSplit, 3);
		}
		else
		{
			arregloTemporal=new String[1];
		}

		//Vamos a guardar el diagnostico de muerte

		Diagnostico diagnosticoMuerte=new Diagnostico();
		if (arregloTemporal.length==3)
		{
			diagnosticoMuerte.setAcronimo(arregloTemporal[0]);
			diagnosticoMuerte.setTipoCIE(Integer.parseInt(arregloTemporal[1]));
		}
		else
		{
			//En este caso el diagnostico no fué seleccionado, luego lo ponemos en 
			//codigo 1 y tipo cie 0
			diagnosticoMuerte.setAcronimo("1");
			diagnosticoMuerte.setTipoCIE(0);
		}

		egreso.setDiagnosticoCausaMuerte(diagnosticoMuerte);

		//Vamos a definir dos contadores, uno para recorrer el número de diagnosticos 
		//definitivos guardados en la forma y otro para almacenar el número de los
		//que efectivamente se han ido guardando (No incluye en los que el usuario no
		//selecciono el checkbox) 

		int i, j=0;

		//Vamos a darle un valor inexistente al diagnostico principal, 
		//porque puede ser que el usuario no lo haya llenado

		Diagnostico diagnosticoTemporal2=new Diagnostico();
		diagnosticoTemporal2.setAcronimo("1");
		diagnosticoTemporal2.setTipoCIE(0);

		egreso.setDiagnosticoDefinitivoPrincipal(diagnosticoTemporal2);

		if(evolucionForm.getDiagnosticosDefinitivos().containsKey("principal"))
		{
			Diagnostico diagnosticoTemporal=new Diagnostico();
			arregloTemporal=((String)evolucionForm.getDiagnosticoDefinitivo("principal")).split(ConstantesBD.separadorSplit,3);
			if (arregloTemporal.length==3)
			{
				diagnosticoTemporal.setAcronimo(arregloTemporal[0]);
				diagnosticoTemporal.setTipoCIE(Integer.parseInt(arregloTemporal[1]));
				egreso.setDiagnosticoDefinitivoPrincipal(diagnosticoTemporal);
			}
		}
		
		
		for (i=0;i<evolucionForm.getNumDiagnosticosDefinitivos();i++)
		{
			Diagnostico diagnosticoTemporal=new Diagnostico();
			arregloTemporal=((String)evolucionForm.getDiagnosticoDefinitivo("relacionado_" + i)).split(ConstantesBD.separadorSplit,3);
			
			if (arregloTemporal.length==3)
			{
				if ((String)evolucionForm.getDiagnosticoDefinitivo("checkbox_"+i) != null)
				{
					diagnosticoTemporal.setAcronimo(arregloTemporal[0]);
					diagnosticoTemporal.setTipoCIE(Integer.parseInt(arregloTemporal[1]));
					if (j==0)
					{
						egreso.setDiagnosticoRelacionado_1(diagnosticoTemporal);
					}
					else if (j==1)
					{
						egreso.setDiagnosticoRelacionado_2(diagnosticoTemporal);
					}
					else if (j==2)
					{
						egreso.setDiagnosticoRelacionado_3(diagnosticoTemporal);
					}
					else
					{
						//No necesito más diagnosticos, me puedo salir del for
						i=evolucionForm.getNumDiagnosticosDefinitivos();
					}
					j++;
				}
			}
		}

		//Siempre hay que guardar 3 relacionados, asi que mientras que
		//j sea menor que 4, debemos agregar diagnosticos con valores temporales

		while (j<3)
		{
			if (j==0)
			{
				egreso.setDiagnosticoRelacionado_1(diagnosticoTemporal2);
			}
			else if (j==1)
			{
				egreso.setDiagnosticoRelacionado_2(diagnosticoTemporal2);
			}
			else if (j==2)
			{
				egreso.setDiagnosticoRelacionado_3(diagnosticoTemporal2);
			}
			j++;
		}

		egreso.setMedicoResponsable(medico);
	}

	/**
	* Método auxiliar que permite llenar los boolean específicos 
	* al form de la evolución hospitalaria
	*
	* @param evolucionHospitalaria Objeto del que se sacan los datos
	* @param bean Bean a llenar
	*/
	public void cargarBeanHospitalizacion(EvolucionHospitalaria evolucionHospitalaria, EvolucionForm bean)
	{
		//Signos Vitales
		bean.setSignosVitalesBoolean(evolucionHospitalaria.isSignosVitalesEpicrisis() );

		//Diagnosticos Presuntivos
		bean.setDiagnosticosPresuntivosBoolean(evolucionHospitalaria.isDiagnosticosPresuntivosEpicrisis() );

		//Diagnosticos Definitivos
		bean.setDiagnosticosDefinitivosBoolean(evolucionHospitalaria.isDiagnosticosDefinitivosEpicrisis() );

		//Información dada por el paciente
		bean.setInformacionDadaPacienteBoolean(evolucionHospitalaria.isInfoDadaPacienteEpicrisis() );

		//Hallazgos Importantes
		bean.setHallazgosImportantesBoolean(evolucionHospitalaria.isHallazgosEpicrisis() );

		//Proc. Quirurgicos y Obstetricos
		bean.setProcedimientosQuirurgicosYObstetricosBoolean(evolucionHospitalaria.isProcedimientosQuirurgicosObstetricosEpicrisis() );

		//Fecha y res. Examenes Diagnostico
		bean.setFechaResultadosExamenesDiagnosticoBoolean(evolucionHospitalaria.isFechaYResultadoExamenesDiagnosticoEpicrisis() );

		//Descripción Complicacion
		bean.setDescripcionComplicacionBoolean(evolucionHospitalaria.isComplicacionesEpicrisis() );

		//Tratamiento
		bean.setTratamientoBoolean(evolucionHospitalaria.isTratamientoEpicrisis() );

		//Resultados Tratamiento
		bean.setResultadosTratamientoBoolean(evolucionHospitalaria.isResultadosTratamientoEpicrisis() );

		//Cambios Manejo Paciente
		bean.setCambiosManejoBoolean(evolucionHospitalaria.isCambiosManejoEpicrisis() );

		//Pronostico
		bean.setPronosticoBoolean(evolucionHospitalaria.isPronosticoEpicrisis() );

		//Observaciones
		bean.setObservacionesBoolean(evolucionHospitalaria.isObservacionesEpicrisis() );
		
		HashMap mapaBalance=new HashMap();
		
		Vector mundoBalance=evolucionHospitalaria.getBalanceLiquidos();
		for(int i=0; i<mundoBalance.size(); i++)
		{
			Vector fila=(Vector)mundoBalance.get(i);
			mapaBalance.put("nombre_"+i, fila.get(0)+"");
			mapaBalance.put("codigo_"+i, fila.get(1)+"");
			mapaBalance.put("valor_"+i, fila.get(2)+"");
		}
		mapaBalance.put("numRegistros", new Integer(mundoBalance.size()));
		bean.setMapaBalanceLiquidos(mapaBalance);
	}

	/**
	 * Método auxiliar que cambia el objeto observaciones y le agrega
	 * el texto "No se grabó información" en caso que la observación
	 * esté vacia
	 * 
	 * @param evolucion Objeto donde se guardan las evoluciones
	 * @param ab_noImprimir Indicada si se debe o no llenar la infomación con el texto especificado
	 */

	public void cargarObservaciones(Evolucion evolucion, boolean ab_noImprimir)
	{
		String ls_mensaje = new String("No se grabó Información");

		if(evolucion.getInformacionDadaPaciente() == null || evolucion.getInformacionDadaPaciente().equals("") )
		{
			if(ab_noImprimir)
				evolucion.setInformacionDadaPaciente(ls_mensaje);
			else
				evolucion.setInformacionDadaPaciente(null);
		}

		if(evolucion.getHallazgosImportantes() == null || evolucion.getHallazgosImportantes().equals("") )
		{
			if(ab_noImprimir)
				evolucion.setHallazgosImportantes(ls_mensaje);
			else
				evolucion.setHallazgosImportantes(null);
		}

		if(evolucion.getProcedimientosQuirurgicosObstetricos() == null || evolucion.getProcedimientosQuirurgicosObstetricos().equals("") )
		{
			if(ab_noImprimir)
				evolucion.setProcedimientosQuirurgicosObstetricos(ls_mensaje);
			else
				evolucion.setProcedimientosQuirurgicosObstetricos(null);
		}

		if(evolucion.getFechaYResultadoExamenesDiagnostico() == null || evolucion.getFechaYResultadoExamenesDiagnostico().equals("") )
		{
			if(ab_noImprimir)
				evolucion.setFechaYResultadoExamenesDiagnostico("");
			else
				evolucion.setFechaYResultadoExamenesDiagnostico(null);
		}

		if(evolucion.getDescripcionComplicacion() == null || evolucion.getDescripcionComplicacion().equals("") )
		{
			if(ab_noImprimir)
				evolucion.setDescripcionComplicacion(ls_mensaje);
			else
				evolucion.setDescripcionComplicacion(null);
		}

		if(evolucion.getTratamiento() == null || evolucion.getTratamiento().equals("") )
		{
			if(ab_noImprimir)
				evolucion.setTratamiento("");
			else
				evolucion.setTratamiento(null);
		}

		if(evolucion.getResultadosTratamiento() == null || evolucion.getResultadosTratamiento().equals("") )
		{
			if(ab_noImprimir)
				evolucion.setResultadosTratamiento("");
			else
				evolucion.setResultadosTratamiento(null);
		}

		if(evolucion.getCambiosManejo() == null || evolucion.getCambiosManejo().equals("") )
		{
			if(ab_noImprimir)
				evolucion.setCambiosManejo("");
			else
				evolucion.setCambiosManejo(null);
		}

		if(evolucion.getPronostico() == null || evolucion.getPronostico().equals("") )
		{
			if(ab_noImprimir)
				evolucion.setPronostico(ls_mensaje);
			else
				evolucion.setPronostico(null);
		}

		if(evolucion.getObservaciones() == null || evolucion.getObservaciones().equals("") )
		{
			if(ab_noImprimir)
				evolucion.setObservaciones("");
			else
				evolucion.setObservaciones(null);
		}
	}
	
	/**
	 * Método que se encarga de revisar que no haya una evolución
	 * (Se creo como método para evitar el uso de código repetido)
	 * 
	 * @param con Conexión con la fuente de datos
	 * @param mapping Mapping de struts
	 * @param request Request de http
	 * @param evolucion Objeto Evolucion o EvolucionHospitalaria de
	 * donde se sacan las fechas
	 * @param paciente Objeto con información del paciente al cual
	 * se quiere hacer la evolución con orden de salida
	 * @return
	 * @throws SQLException
	 */
	public ActionForward revisarQueNoHayaEvolucionAnterior(Connection con, ActionMapping mapping, HttpServletRequest request, Evolucion evolucion, PersonaBasica paciente) throws Exception
	{

		if(UtilidadValidacion.existeEvolucionConFechaSuperiorFormatoAp(con, paciente.getCodigoCuenta(), evolucion.getFechaEvolucion(), evolucion.getHoraEvolucion()) )
		{
		    return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Existe una evolucion con fecha superior a esta fecha de egreso" , "Existe una evolucion con fecha superior a esta fecha de egreso", false) ;
		}
		else
		{
			return null;
		}
		
	}
	
	/**
	 * Este método permite cambiar los boolean de una Evolucion 
	 * Hospitalaria de acuerdo a las reglas del mundo
	 * 
	 * @param evolucion Objeto de tipo EvolucionHospitalaria al
	 * cual se le van a cambiar los boolean
	 * @param bean Objeto de tipo EvolucionForm de donde se va
	 * a saber si un boolean se debe cambiar o no 
	 */	
	private void revisarBoolean (EvolucionHospitalaria evolucion, EvolucionForm bean)
	{
		if (bean.getSoloAnadir().equals("true"))
		{
			//Si solo puedo añadir en el objeto voy a cambiar (posiblemente )
			//unicamente los datos que en el mismo esten en false Y
			//si hay algo escrito

			if (!evolucion.isSignosVitalesEpicrisis())
			{
				evolucion.setSignosVitalesEpicrisis(bean.isSignosVitalesBoolean());
			}
			if (!evolucion.isDiagnosticosDefinitivosEpicrisis())
			{
				evolucion.setDiagnosticosDefinitivosEpicrisis(bean.isDiagnosticosDefinitivosBoolean());
			}
			
			if (!evolucion.isDiagnosticosPresuntivosEpicrisis())
			{
				evolucion.setDiagnosticosPresuntivosEpicrisis(bean.isDiagnosticosPresuntivosBoolean());
			}

			if (!evolucion.isCambiosManejoEpicrisis()&&evolucion.getCambiosManejo()!=null&&!evolucion.getCambiosManejo().equals(""))
			{
				evolucion.setCambiosManejoEpicrisis(bean.isCambiosManejoBoolean());
			}

			if (!evolucion.isObservacionesEpicrisis()&&evolucion.getObservaciones()!=null&&!evolucion.getObservaciones().equals(""))
			{
				evolucion.setObservacionesEpicrisis(bean.isObservacionesBoolean());
			}
			
			if (!evolucion.isFechaYResultadoExamenesDiagnosticoEpicrisis()&&evolucion.getFechaYResultadoExamenesDiagnostico()!=null&&!evolucion.getFechaYResultadoExamenesDiagnostico().equals(""))
			{
				evolucion.setFechaYResultadoExamenesDiagnosticoEpicrisis(bean.isFechaResultadosExamenesDiagnosticoBoolean());
			}
			
			if (!evolucion.isHallazgosEpicrisis()&&evolucion.getHallazgosImportantes()!=null&&!evolucion.getHallazgosImportantes().equals(""))
			{
				evolucion.setHallazgosEpicrisis(bean.isHallazgosImportantesBoolean());
			}
			
			if (!evolucion.isPronosticoEpicrisis()&&evolucion.getPronostico()!=null&&!evolucion.getPronostico().equals(""))
			{
				evolucion.setPronosticoEpicrisis(bean.isPronosticoBoolean());
			}
			
			if (!evolucion.isProcedimientosQuirurgicosObstetricosEpicrisis()&&evolucion.getProcedimientosQuirurgicosObstetricos()!=null&&!evolucion.getProcedimientosQuirurgicosObstetricos().equals(""))
			{
				evolucion.setProcedimientosQuirurgicosObstetricosEpicrisis(bean.isProcedimientosQuirurgicosYObstetricosBoolean());
			}
			
			if (!evolucion.isResultadosTratamientoEpicrisis()&&evolucion.getResultadosTratamiento()!=null&&!evolucion.getResultadosTratamiento().equals(""))
			{
				evolucion.setResultadosTratamientoEpicrisis(bean.isResultadosTratamientoBoolean());
			}
			
			if (!evolucion.isTratamientoEpicrisis()&&evolucion.getTratamiento()!=null&&!evolucion.getTratamiento().equals(""))
			{
				evolucion.setTratamientoEpicrisis(bean.isTratamientoBoolean());
			}
			
			if (!evolucion.isInfoDadaPacienteEpicrisis()&&evolucion.getInformacionDadaPaciente()!=null&&!evolucion.getInformacionDadaPaciente().equals(""))
			{
				evolucion.setInfoDadaPacienteEpicrisis(bean.isInformacionDadaPacienteBoolean());
			}
			
			if (!evolucion.isComplicacionesEpicrisis()&&evolucion.getDescripcionComplicacion()!=null&&!evolucion.getDescripcionComplicacion().equals(""))
			{
				evolucion.setComplicacionesEpicrisis(bean.isDescripcionComplicacionBoolean());
			}

		}
		else
		{
			//Si podemos añadir y eliminar sin restricciones 
			//solo hay que revisar que el texto no sea nulo
			evolucion.setSignosVitalesEpicrisis(bean.isSignosVitalesBoolean());
			evolucion.setDiagnosticosDefinitivosEpicrisis(bean.isDiagnosticosDefinitivosBoolean());
			evolucion.setDiagnosticosPresuntivosEpicrisis(bean.isDiagnosticosPresuntivosBoolean());


			if (evolucion.getCambiosManejo()==null||evolucion.getCambiosManejo().equals(""))
			{
				evolucion.setCambiosManejoEpicrisis(false);
			}
			else
			{
				evolucion.setCambiosManejoEpicrisis(bean.isCambiosManejoBoolean());
			}
			if (evolucion.getObservaciones()==null||evolucion.getObservaciones().equals(""))
			{
				evolucion.setObservacionesEpicrisis(false);
			}
			else
			{
				evolucion.setObservacionesEpicrisis(bean.isObservacionesBoolean());
			}
			if (evolucion.getFechaYResultadoExamenesDiagnostico()==null||evolucion.getFechaYResultadoExamenesDiagnostico().equals(""))
			{
				evolucion.setFechaYResultadoExamenesDiagnosticoEpicrisis(false);
			}
			else
			{
				evolucion.setFechaYResultadoExamenesDiagnosticoEpicrisis(bean.isFechaResultadosExamenesDiagnosticoBoolean());
			}
			if (evolucion.getHallazgosImportantes()==null||evolucion.getHallazgosImportantes().equals(""))
			{
				evolucion.setHallazgosEpicrisis(false);
			}
			else
			{
				evolucion.setHallazgosEpicrisis(bean.isHallazgosImportantesBoolean());
			}
			if (evolucion.getDescripcionComplicacion()==null||evolucion.getDescripcionComplicacion().equals(""))
			{
				evolucion.setComplicacionesEpicrisis(false);
			}
			else
			{
				evolucion.setComplicacionesEpicrisis(bean.isDescripcionComplicacionBoolean());
			}
			if (evolucion.getInformacionDadaPaciente()==null||evolucion.getInformacionDadaPaciente().equals(""))
			{
				evolucion.setInfoDadaPacienteEpicrisis(false);
			}
			else
			{
				evolucion.setInfoDadaPacienteEpicrisis(bean.isInformacionDadaPacienteBoolean());
			}
			if (evolucion.getProcedimientosQuirurgicosObstetricos()==null||evolucion.getProcedimientosQuirurgicosObstetricos().equals(""))
			{
				evolucion.setProcedimientosQuirurgicosObstetricosEpicrisis(false);
			}
			else
			{
				evolucion.setProcedimientosQuirurgicosObstetricosEpicrisis(bean.isProcedimientosQuirurgicosYObstetricosBoolean());
			}
			if (evolucion.getPronostico()==null||evolucion.getPronostico().equals(""))
			{
				evolucion.setPronosticoEpicrisis(false);
			}
			else
			{
				evolucion.setPronosticoEpicrisis(bean.isPronosticoBoolean());
			}
			if (evolucion.getTratamiento()==null||evolucion.getTratamiento().equals(""))
			{
				evolucion.setTratamientoEpicrisis(false);
			}
			else
			{
				evolucion.setTratamientoEpicrisis(bean.isTratamientoBoolean());
			}
			if (evolucion.getResultadosTratamiento()==null||evolucion.getResultadosTratamiento().equals(""))
			{
				evolucion.setResultadosTratamientoEpicrisis(false);
			}
			else
			{
				evolucion.setResultadosTratamientoEpicrisis(bean.isResultadosTratamientoBoolean());
			}
			
			
		}
	}
	
	/**
	 * Este método precarga los diagnosticos de la evolución (Diagnosticos
	 * sugeridos) en la forma de Evolucion (EvolucionForm), utilizando la
	 * clase AuxiliarDiagnosticos
	 * 
	 * @param con una conexion abierta con una fuente de datos
	 * @param numeroCuenta Número de la cuenta a la que pertenece esta
	 * evolución
	 * @param bean Objeto de tipo EvolucionForm, donde se almaceran los
	 * diagnosticos
	 * @param request Objeto request donde se van a poner el número de
	 * diagnosticos presuntivos y definitivos que se cargaron
	 * @throws SQLException
	 */
	public void preCargarDiagnosticos (Connection con, int numeroCuenta, EvolucionForm bean, HttpServletRequest request) throws SQLException
	{
		int i;
		//Inicializamos un objeto AuxiliarDiagnosticos para
		//saber que debemos proponer
		AuxiliarDiagnosticos auxiliarDiagnosticosEvolucion=new AuxiliarDiagnosticos(numeroCuenta, true);
		auxiliarDiagnosticosEvolucion.cargar(con);
		//Definimos los tamaños
		bean.setNumDiagnosticosDefinitivos(auxiliarDiagnosticosEvolucion.getNumeroDiagnosticosDefinitivos());
		request.setAttribute("numDiagnosticosDefinitivos", ""+bean.getNumDiagnosticosDefinitivos());
		bean.setNumDiagnosticosPresuntivos(auxiliarDiagnosticosEvolucion.getNumeroDiagnosticosPresuntivos());
		//Este atributo me permite saber si lo debo dejar chequear/deschequear
		bean.setNumDiagnosticosPresuntivosOriginal(auxiliarDiagnosticosEvolucion.getNumeroDiagnosticosPresuntivos());
		request.setAttribute("numDiagnosticosPresuntivos", ""+bean.getNumDiagnosticosPresuntivos());
		
		//Si es el caso precargamos el diagnostico de complicación
		
		Diagnostico diagnosticoComplicacion=auxiliarDiagnosticosEvolucion.getDiagnosticoComplicacion();
		//Solo lo vamos a hacer si es un diagnostico no vacio (Tipo Cie !=0)
		if (diagnosticoComplicacion.getTipoCIE()!=0)
		{
			bean.setDiagnosticoComplicacion_1(diagnosticoComplicacion.getAcronimo() +ConstantesBD.separadorSplit+ diagnosticoComplicacion.getTipoCIE() + ConstantesBD.separadorSplit + diagnosticoComplicacion.getNombre());
		}
		
		boolean centinelaAsignoPrincipal=false;
		int indexRel=0;
		//Vamos a llenar el bean con los datos de estos diagnosticos
		for (i=0;i<bean.getNumDiagnosticosPresuntivos();i++)
		{
			Diagnostico diagnosticoTemporal=auxiliarDiagnosticosEvolucion.getDiagnosticoPresuntivo(i);
			
			//aqui se carga solo el principal
			if(!centinelaAsignoPrincipal && diagnosticoTemporal.isPrincipal())
			{
				bean.setDiagnosticoPresuntivo("principal", diagnosticoTemporal.getAcronimo() + ConstantesBD.separadorSplit+ diagnosticoTemporal.getTipoCIE() + ConstantesBD.separadorSplit + diagnosticoTemporal.getNombre());
				centinelaAsignoPrincipal=true;
			}	
			else
			{	
				bean.setDiagnosticoPresuntivo("relacionado_" + indexRel, diagnosticoTemporal.getAcronimo() + ConstantesBD.separadorSplit+ diagnosticoTemporal.getTipoCIE() + ConstantesBD.separadorSplit + diagnosticoTemporal.getNombre());
				bean.setDiagnosticoPresuntivo("checkbox_"+indexRel, "true");
				if(!bean.getDiagnosticosDefinitivos().containsKey("seleccionados"))
					bean.setDiagnosticoDefinitivo("seleccionados", "'"+diagnosticoTemporal.getAcronimo()+"'");
				else
					bean.setDiagnosticoDefinitivo("seleccionados", bean.getDiagnosticoDefinitivo("seleccionados")+",'"+diagnosticoTemporal.getAcronimo()+"'");
				indexRel++;
			}	
		}
		
		centinelaAsignoPrincipal=false;
		indexRel=0;
		
		for (i=0;i<bean.getNumDiagnosticosDefinitivos();i++)
		{
			Diagnostico diagnosticoTemporal=auxiliarDiagnosticosEvolucion.getDiagnosticoDefinitivo(i);
			//aqui se carga solo el principal
			if(!centinelaAsignoPrincipal && diagnosticoTemporal.isPrincipal())
			{
				bean.setDiagnosticoDefinitivo("principal", diagnosticoTemporal.getAcronimo() + ConstantesBD.separadorSplit+ diagnosticoTemporal.getTipoCIE() + ConstantesBD.separadorSplit + diagnosticoTemporal.getNombre());
				centinelaAsignoPrincipal=true;
			}	
			else
			{
				bean.setDiagnosticoDefinitivo("relacionado_" +indexRel, diagnosticoTemporal.getAcronimo() + ConstantesBD.separadorSplit+ diagnosticoTemporal.getTipoCIE() + ConstantesBD.separadorSplit + diagnosticoTemporal.getNombre());
				bean.setDiagnosticoDefinitivo("checkbox_"+indexRel, "true");
				if(!bean.getDiagnosticosDefinitivos().containsKey("seleccionados"))
					bean.setDiagnosticoDefinitivo("seleccionados", "'"+diagnosticoTemporal.getAcronimo()+"'");
				else
					bean.setDiagnosticoDefinitivo("seleccionados", bean.getDiagnosticoDefinitivo("seleccionados")+",'"+diagnosticoTemporal.getAcronimo()+"'");
				indexRel++;
			}	
		}
		
		//el numero de diagnostico definitivo son solo los que pertenecen a los relacionados
		if(bean.getNumDiagnosticosDefinitivos()>0)
			bean.setNumDiagnosticosDefinitivos(bean.getNumDiagnosticosDefinitivos()-1);
		if(bean.getNumDiagnosticosPresuntivos()>0)
			bean.setNumDiagnosticosPresuntivos(bean.getNumDiagnosticosPresuntivos()-1);
		
	}
	

	/**
	 * Método que maneja las acciones correspondientes a mostrar
	 * o no el warning, revisa el caso de manejo adjunto cuando el 
	 * médico entra a la funcionalidad de insertar una nueva evolución. 
	 * También incluye la inicialización del parametro que dice si tiene 
	 * solicitud de cambio de tratante o no
	 * 
	 * @param con Conexión con la fuente de datos
	 * @param evolucionForm Forma de Evolucion
	 * @param medico Médico que accede a la funcionaldiad
	 * @param paciente Paciente al que pertenece la evolución
	 * a crear
	 * @throws SQLException
	 * @throws IPSException 
	 */	
	public void accionesManejoWarning (Connection con, EvolucionForm evolucionForm, UsuarioBasico medico, PersonaBasica paciente) throws SQLException, IPSException
	{
		logger.info("entra!!!!");
		//Guardamos el número de la solicitud, si este número
		//es mayor que 0, hay que mostrar mensaje
		evolucionForm.setNumeroSolicitudPidioConvertirMedicoATratante(UtilidadValidacion.existeSolicitudTransferenciaManejoPreviaDadoCentroCosto(con, paciente.getCodigoCuenta(), medico.getCodigoCentroCosto()));
		evolucionForm.setEsAdjunto(UtilidadValidacion.esAdjuntoCuenta(con, paciente.getCodigoCuenta() ,medico.getLoginUsuario()));
		
		logger.info("es Adjunto!!!!"+evolucionForm.getEsAdjunto());
		//Si el médico es adjunto revisamos si debemos
		//ponerle el warning
		if (evolucionForm.getEsAdjunto())
		{
			if(!evolucionForm.getEsTratante())
			{		
				ResultadoBoolean respAdjunto=UtilidadValidacion.haySolicitudesIncompletasAdjunto(con, paciente.getCodigoCuenta(), medico.getCodigoCentroCosto(), paciente.getCodigoCuentaAsocio(), medico.getCodigoInstitucionInt());
				if (respAdjunto.isTrue())
				{
					if (evolucionForm.getWarningOrdenSalida()!=null&&!evolucionForm.getWarningOrdenSalida().equals(""))
					{
						evolucionForm.setWarningOrdenSalida(evolucionForm.getWarningOrdenSalida() + "<br/>No puede Finalizar la Atención porque: " + respAdjunto.getDescripcion());
					}
					else
					{
						evolucionForm.setWarningOrdenSalida("No puede Finalizar la Atención porque: " + respAdjunto.getDescripcion());
					}
				}
			}	
		}
		
		//Ahora vamos a revisar si es el tratante y si se le debe
		//mostrar el mensaje de que no va a poder dar orden
		//de salida SI no tiene todas las solicitudes interpretadas
		//anuladas
		if (UtilidadValidacion.esMedicoTratante(con, medico, paciente).equals(""))
		{
			//se verifica que el paciente tenga manejo conjunto
			if(paciente.getManejoConjunto())
				evolucionForm.setPuedoFinalizarManejo(true);
				
			ResultadoBoolean resp=UtilidadValidacion.haySolicitudesIncompletasEnCuenta(con, paciente.getCodigoIngreso(), medico.getCodigoInstitucionInt(), true,UtilidadTexto.getBoolean(ValoresPorDefecto.getValidarAdministracionMedEgresoMedico(medico.getCodigoInstitucionInt())));
			if (resp.isTrue())
			{
				if (evolucionForm.getWarningOrdenSalida()!=null&&!evolucionForm.getWarningOrdenSalida().equals(""))
				{
					evolucionForm.setWarningOrdenSalida(evolucionForm.getWarningOrdenSalida() + "<br/> No puede dar Orden de Salida porque: " + resp.getDescripcion());
					//evolucionForm.setPuedeDarOrdenSalida("false");
				}
				else
				{
					evolucionForm.setWarningOrdenSalida("No puede dar Orden de Salida porque: " + resp.getDescripcion());
					//evolucionForm.setPuedeDarOrdenSalida("false");
				}
			}
		}
		
		//se verifica si se puede finalizar manejo
		if(evolucionForm.getEsAdjunto()||evolucionForm.isPuedoFinalizarManejo())
			evolucionForm.setPuedoFinalizarManejo(true);
	}
	
	
/**	
	 * metodo que carga el diagnostico de ingreso del paciente 
	 * @param con   una conexion con la fuente de datos
	 * @param codigoCuenta   el codigo de la cuenta con la cual esta asociada al paciente
	 * @return  un HashMap con los campos del dignostico de ingreso
	 */
	
	public HashMap cargarDiagnosticoIngreso(Connection con, int codigoCuenta){
		String acronimo = "", nombre = "", tipoCie="";
		TagDao tagDao = (DaoFactory.getDaoFactory(System.getProperty("TIPOBD"))).getTagDao();
		HashMap hmap = new HashMap();
		
		try 
		{

			ResultSetDecorator rs = tagDao.consultaTagMuestraDiagnosticoValoracionHospitalaria (con, codigoCuenta);
			//logger.info("El código de la cuenta es " + codigoCuenta);

			if (rs.next())	
			{
				
				acronimo = rs.getString("acronimo");
				tipoCie=rs.getString("tipo_cie");
				nombre = Encoder.encode(rs.getString("nombre"));
				
			}
			else 
			{
				//Si el paciente se encuentra en asocio de cuenta y no se encuentran datos
				//se busca el código de la cuenta del paciente
				
				return hmap;
			}

		}	
		catch (SQLException sqle) 
		{
				logger.info("Error al cargar el Diagnostico de Ingreso");
		}

		hmap.put("acronimo" ,  acronimo);
		hmap.put("tipoCie" ,  tipoCie);
		hmap.put("nombre", nombre);

		return hmap;
	}

	
	
	
	
	
	/**
	 * metodo que carga el egreso ( de haber uno ) de una evolucion en particular
	 * @param con    una conexion con la fuente de datos
	 * @param idEvolucion   el codigo de la evolucion 
	 * @return  un HashMap con los datos del egreso
	 */	
	public  HashMap cargarEgreso(Connection con, int idEvolucion){
			String estadoSalida = "", acronimoMuerte = "", diagnosticoMuerte="", tipoCIEMuerte="", destinoSalida="", tieneEgreso="";
			TagDao tagDao = (DaoFactory.getDaoFactory(System.getProperty("TIPOBD"))).getTagDao();
			HashMap hmap = new HashMap();
			
			
			try {
				ResultSetDecorator rs=tagDao.consultaTagMuestaDatosSalidaEvolucion (con, idEvolucion);

				if (rs.next())	
				{
					if (rs.getBoolean("estadoSalida"))
					{
						estadoSalida  = "true";
					}
					else
					{
						estadoSalida  = "false";
					}
					
					acronimoMuerte=rs.getString("acronimoMuerte");
					diagnosticoMuerte=rs.getString("diagnosticoMuerte");
					tipoCIEMuerte=rs.getString("tipoCIEMuerte");
					if (rs.getInt("codigoDestinoSalida")==0)
					{
						//Seleccionó la opción otros
						destinoSalida=rs.getString("otroDestinoSalida");
					}
					else
					{
						destinoSalida=rs.getString("destinoSalida");
					}
					tieneEgreso="true";
				}
				else 
				{
					tieneEgreso="false";
				}

			}	
			catch (SQLException sqle) 
			{
					logger.info("Error al cargar el egreso de la evolucion");
			}

			hmap.put("tieneEgreso" , tieneEgreso );
			hmap.put("estadoSalida" ,  estadoSalida);
			hmap.put("acronimoMuerte" , acronimoMuerte );
			hmap.put("tipoCIEMuerte" ,  tipoCIEMuerte);
			hmap.put("diagnosticoMuerte" , diagnosticoMuerte );
			hmap.put("destinoSalida" ,  destinoSalida);
	
			return hmap;
	}
	
	
	
	
	
	
	/**

	
	
	
	
	/**
	 * Método que valida, ya al momento previo de insertar
	 * si es adjunto y si debe impedir la inserción de esta evolución
	 * en caso de ser un adjunto que desea dar finalización de
	 * atención porque tenga solicitudes sin interpretración y/o anuladas
	 * 
	 * @param con Conexión con la fuente de datos
	 * @param mapping Mapping para manejo recursos
	 * físicos en struts
	 * @param request Objeto para manejo del request
	 * de http
	 * @param evolucionForm Forma de Evolucion
	 * @param medico Médico que accede a la funcionaldiad
	 * @param paciente Paciente al que pertenece la evolución
	 * a crear
	 * @return
	 * @throws SQLException
	 * @throws IPSException 
	 */	
	public ActionForward accionesManejoError (Connection con, ActionMapping mapping, HttpServletRequest request, EvolucionForm evolucionForm, UsuarioBasico medico, PersonaBasica paciente) throws SQLException, IPSException
	{
		evolucionForm.setEsAdjunto(UtilidadValidacion.esAdjuntoCuenta(con, paciente.getCodigoCuenta() ,medico.getLoginUsuario()));
							
		//Si es adjunto mostramos errores SOLO Cuando
		//la finalización de la atención esté en true
		ActionErrors errors=new ActionErrors();

		if (evolucionForm.getEsAdjunto()&&evolucionForm.getDeseaFinalizarAtencion()&&UtilidadValidacion.haySolicitudesIncompletasAdjunto(con, paciente.getCodigoCuenta(), medico.getCodigoCentroCosto(), paciente.getCodigoCuentaAsocio(), medico.getCodigoInstitucionInt()).isTrue())
		{
			if (evolucionForm.getOrdenSalida()!=null&&evolucionForm.getOrdenSalida().equals("true"))
			{	
				if(ValoresPorDefecto.getValidarEstadoSolicitudesInterpretadas(medico.getCodigoInstitucionInt()).equals("true"))
				{
					errors.add("adjuntoConSolicitudesPendientes", new ActionMessage("error.evolucion.adjuntoConSolicitudesPendientes"));
				}
				else
				{
					errors.add("adjuntoConSolicitudesPendientes", new ActionMessage("error.evolucion.tratanteConSolicitudesPendientes"));
				}
				this.saveErrors(request, errors);
				evolucionForm.setWarningOrdenSalida("");
				UtilidadBD.cerrarConexion(con);
				return mapping.findForward("evolucionPrincipal");
			}	
		}
		
		if (UtilidadValidacion.esMedicoTratante(con, medico, paciente).equals("")&&evolucionForm.getOrdenSalida()!=null&&evolucionForm.getOrdenSalida().equals("true"))
		{
			//Si el destino a la salida es hospitalización y estamos en 
			//admisión de urgencias NO validamos que queden solicitudes
			//pendientes
			
			if (paciente.getAnioAdmision()>0&&evolucionForm.getDestinoSalida()==ConstantesBD.codigoDestinoSalidaHospitalizacion)
			{
				//En este caso NO se debe validar si hay solicitudes incompletas
				logger.info("En este caso NO se valida si hay solicitudes incompletas");
			}
			else
			{
				//Para cualquier otro caso si se valida
				ResultadoBoolean resp=UtilidadValidacion.haySolicitudesIncompletasEnCuenta(con, paciente.getCodigoIngreso(), medico.getCodigoInstitucionInt(), true,UtilidadTexto.getBoolean(ValoresPorDefecto.getValidarAdministracionMedEgresoMedico(medico.getCodigoInstitucionInt())));
				if (resp.isTrue())
				{
					if(ValoresPorDefecto.getValidarEstadoSolicitudesInterpretadas(medico.getCodigoInstitucionInt()).equals("true"))
					{
						errors.add("tratanteConSolicitudesPendientes", new ActionMessage("error.evolucion.tratanteConSolicitudesPendientes"));
					}
					else
					{
						errors.add("tratanteConSolicitudesPendientes", new ActionMessage("error.evolucion.tratanteConSolicitudesPendientesRespondidas"));
					}
					this.saveErrors(request, errors);
					evolucionForm.setWarningOrdenSalida("");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("evolucionPrincipal");
				}
				
				if(paciente.getCodigoSexo()==ConstantesBD.codigoSexoFemenino)
				{
					String cuentaAsociada="";
					if(paciente.getExisteAsocio())
						cuentaAsociada=paciente.getCodigoCuentaAsocio()+"";
					
					/**
					 * MT 7378 y MT 7399
					 * Se agrega la validación de destino de salida para mujeres con solicitudes de parto o cirugía.
					 * 
					 * @author jeilones
					 * @date 16/17/2013
					 * */
					Vector warningCLAPVector= UtilidadValidacion.puedoDarOrdenSalidaXClap(con, paciente.getCodigoCuenta()+"", paciente.getCodigoPersona()+"",cuentaAsociada,evolucionForm.getDestinoSalida(),true);
					if(warningCLAPVector.size()>0)
					{
						String warningClapStr="";
						for(int w=0; w<warningCLAPVector.size(); w++)
						{
							if(w==0)
								warningClapStr+=warningCLAPVector.get(w);
							else
								warningClapStr+=", "+warningCLAPVector.get(w);
						}
						
						//evolucionForm.setPuedeDarOrdenSalida("false");
						UtilidadBD.cerrarConexion(con);
						ActionErrors errors1=new ActionErrors();
						errors1.add("", new ActionMessage("error.evolucion.noPuedeDarOrdenSalida", warningClapStr));
						this.saveErrors(request, errors1);
						evolucionForm.setWarningOrdenSalida("");
						return mapping.findForward("evolucionPrincipal");
					}
				}
			}
			
		}

		return null;
	}
	
	/**
	 * Método que se encarga realizar todas las inserciones
	 * en la fuente de datos para evolución, el objetivo de
	 * este método es centralizar todos los posibles casos
	 * en un solo sitio
	 * 
	 * @param con Conexión con la fuente de datos
	 * @param evolucionForm Forma que maneja todos los
	 * datos de evolución
	 * @param medico Médico que va a insertar la evolución
	 * @param paciente Paciente al que se le va a insertar
	 * la evolución
	 * @param evolucion Objeto de tipo EvolucionHospitalaria
	 * o Evolucion con los datos a insertar 
	 * @param terminarTransaccion Boolean que me dice si
	 * con la inserción termina la transacción
	 * @return
	 * @throws Exception
	 */
	private int insercionBasicaEvolucion (HttpServletRequest request, Connection con, EvolucionForm evolucionForm, UsuarioBasico medico, PersonaBasica paciente, Evolucion evolucion, boolean terminarTransaccion) throws Exception
	{
		//Si desea terminar la transacción y no se cumplen las condiciones de 
		//adjunto ni de tratante, se inserta la evolución sin el método transaccional
	    /*
		logger.info("tipo monitoreo "+evolucion.getProcedimientosQuirurgicosObstetricos());
		*/
	    if(!evolucion.getProcedimientosQuirurgicosObstetricos().equals("-1") && !evolucion.getProcedimientosQuirurgicosObstetricos().trim().equals(""))
	    {
	    	OrdenMedica ordenMedica=new OrdenMedica();
	    	int codigoOrden=0;
		    if(!ordenMedica.cargarOrdenMedica(con, paciente.getCodigoCuenta(), false))
		    {
		    	DtoResultado resultado=ordenMedica.insertarOrdenMedica(con, paciente.getCodigoCuenta(),true,true);
		    	if(resultado != null){
		    		codigoOrden=Integer.valueOf(resultado.getPk());
		    	}
			}
		    else
		    {
		    	codigoOrden=ordenMedica.getCodigoOrden();
		    }
    		ordenMedica.setFechaOrden(UtilidadFecha.conversionFormatoFechaABD(evolucionForm.getFechaEvolucion()));
    		ordenMedica.setHoraOrden(evolucionForm.getHoraEvolucion());
	    	int codigoHistorico=ordenMedica.insertarEncabezadoOrdenMedica(con, codigoOrden, medico.getLoginUsuario(), UtilidadTexto.agregarTextoAObservacion(null, null, medico, false),true);
	    	ordenMedica.setTipoMonitoreo(Integer.parseInt(evolucion.getProcedimientosQuirurgicosObstetricos()));
	    	ordenMedica.insertarOrdenTipoMonitoreo(con, codigoHistorico,true);
	    	evolucion.setProcedimientosQuirurgicosObstetricos(codigoHistorico+"");
	    }
	    else
	    {
	    	evolucion.setProcedimientosQuirurgicosObstetricos(null);
	    }
		//Temporalmente!!!
		//evolucion.setProcedimientosQuirurgicosObstetricos(null);
		//----------------
	    logger.info("convertir medico a tratante=> "+evolucionForm.getConvertirMedicoATratante());
	    evolucion.setConvertirMedicoTratante(evolucionForm.getConvertirMedicoATratante());
	    Vector balanceLiquidos=llenarBalanceLiquidos(evolucionForm.getMapaBalanceLiquidos());
		if (terminarTransaccion&& !((evolucionForm.getEsAdjunto()&&evolucionForm.getDeseaFinalizarAtencion())||!evolucionForm.getEsAdjunto()) &&(evolucionForm.getConvertirMedicoATratante()=='p' ) )
		{
			return evolucion.insertar(con, paciente, medico, balanceLiquidos, evolucionForm.getCodigoConductaSeguir(), evolucionForm.getAcronimoTipoReferencia());
		}
		int numeroEvolucion=evolucion.insertarTransaccional(con, paciente, medico, "empezar", balanceLiquidos, evolucionForm.getCodigoConductaSeguir(), evolucionForm.getAcronimoTipoReferencia());
		


		evolucionForm.setNumeroSolicitud(numeroEvolucion);

		//El siguiente caso corresponde a cuando SE desea finalizar
		//atención 

		if (evolucionForm.getEsAdjunto()&&evolucionForm.getDeseaFinalizarAtencion())
		{
		    request.setAttribute("manejoConjuntoFinalizado", "true");
			//Tres posibles sub-casos:
			//-a. Desea aceptar médico tratante 
			//-b. Desea dejar Médico tratante pendiente
			//-c. Definitivamente NO quiere ser médico tratante

			if (evolucionForm.getConvertirMedicoATratante()=='s')
			{
				logger.info("Caso 1-a");
				//-a. Desea aceptar médico tratante 
				Solicitud solicitud= new Solicitud();
				if (paciente.getExisteAsocio())
				{
					solicitud.cambiosRequeridosAceptacionTratanteCasoAsocio (con, evolucionForm.getNumeroSolicitudPidioConvertirMedicoATratante(), paciente.getCodigoCuenta());
				}
				ResultadoBoolean resultado = solicitud.inactivarSolicitudCambioTratante(con, evolucionForm.getNumeroSolicitudPidioConvertirMedicoATratante());
				if( !resultado.isTrue() )
				{
					logger.warn("Problemas inactivando la solicitud de cambio de médico tratante "+resultado.getDescripcion());
					throw new SQLException ("Problemas inactivando la solicitud de cambio de médico tratante : "+resultado.getDescripcion());						
				}
				else
				{
					resultado = solicitud.insertarTratante(con, evolucionForm.getNumeroSolicitudPidioConvertirMedicoATratante(), medico.getCodigoPersona(), medico.getCodigoCentroCosto(), true);
					//En este caso NO hay que finalizar la atención de forma 
					//automática, esto se hace al final con la nota del médico
					ResultadoBoolean resultado2 = solicitud.inactivarSolicitudCambioTratante(con, evolucionForm.getNumeroSolicitudPidioConvertirMedicoATratante());
					
					if( !resultado.isTrue()||!resultado2.isTrue())
					{
						logger.warn("Problemas insertando el médico tratante "+resultado.getDescripcion());						
						throw new SQLException ("Problemas insertando el médico tratante  : "+resultado.getDescripcion());											
					}
				}

				Solicitud finalizarAtencionConjunta=new Solicitud();
				
				if (terminarTransaccion)
				{
					finalizarAtencionConjunta.finalizarAtencionConjuntaTransaccional(con, paciente.getCodigoCuenta(), medico.getCodigoCentroCosto(), evolucionForm.getNotaFinalizacionAtencion(), evolucion.getCodigoValoracion(), "finalizar");
				}
				else
				{
					finalizarAtencionConjunta.finalizarAtencionConjuntaTransaccional(con, paciente.getCodigoCuenta(), medico.getCodigoCentroCosto(), evolucionForm.getNotaFinalizacionAtencion(), evolucion.getCodigoValoracion(), "continuar");
				}

			}
			
			if (evolucionForm.getConvertirMedicoATratante()=='p')
			{
				logger.info("Caso 1-b");
				//-b. Desea dejar Médico tratante -
				Solicitud finalizarAtencionConjunta=new Solicitud();
				logger.info("Terminar Transaccion=> "+terminarTransaccion);
				if (terminarTransaccion)
				{
					if(UtilidadValidacion.esMedicoTratante(con,medico,paciente).equals(""))
						finalizarAtencionConjunta.finalizarAtencionConjuntaTransaccional(con, paciente.getCodigoCuenta(), 0, evolucionForm.getNotaFinalizacionAtencion(), evolucion.getCodigoValoracion(), ConstantesBD.finTransaccion);
					else
						finalizarAtencionConjunta.finalizarAtencionConjuntaTransaccional(con, paciente.getCodigoCuenta(), medico.getCodigoCentroCosto(), evolucionForm.getNotaFinalizacionAtencion(), evolucion.getCodigoValoracion(), ConstantesBD.finTransaccion);
						
				}
				else
				{
					if(UtilidadValidacion.esMedicoTratante(con,medico,paciente).equals(""))
						finalizarAtencionConjunta.finalizarAtencionConjuntaTransaccional(con, paciente.getCodigoCuenta(), 0, evolucionForm.getNotaFinalizacionAtencion(), evolucion.getCodigoValoracion(), ConstantesBD.continuarTransaccion);
					else
						finalizarAtencionConjunta.finalizarAtencionConjuntaTransaccional(con, paciente.getCodigoCuenta(), medico.getCodigoCentroCosto(), evolucionForm.getNotaFinalizacionAtencion(), evolucion.getCodigo(), ConstantesBD.continuarTransaccion);
				}
				
				UtilidadSesion.notificarCambiosObserver(paciente.getCodigoPersona(),request.getSession().getServletContext());
			}
			
			if (evolucionForm.getConvertirMedicoATratante()=='n')
			{
				logger.info("Caso 1-c");
				//-c. Definitivamente NO quiere ser médico tratante
				Solicitud solicitud= new Solicitud();
				ResultadoBoolean resultado = solicitud.inactivarSolicitudCambioTratante(con, evolucionForm.getNumeroSolicitudPidioConvertirMedicoATratante());
				if( !resultado.isTrue() )
				{
					logger.warn("Problemas inactivando la solicitud de cambio de médico tratante "+resultado.getDescripcion());
					throw new SQLException ("Problemas inactivando la solicitud de cambio de médico tratante : "+resultado.getDescripcion());						
				}
			
				//Como los anteriores métodos no tienen estados transaccionales
				//el último método en llamarse es el de finalizar atención
				//conjunta

				Solicitud finalizarAtencionConjunta=new Solicitud();
				if (terminarTransaccion)
				{
					finalizarAtencionConjunta.finalizarAtencionConjuntaTransaccional(con, paciente.getCodigoCuenta(), medico.getCodigoCentroCosto(), evolucionForm.getNotaFinalizacionAtencion(), evolucion.getCodigoValoracion(), "finalizar");
				}
				else
				{
					finalizarAtencionConjunta.finalizarAtencionConjuntaTransaccional(con, paciente.getCodigoCuenta(), medico.getCodigoCentroCosto(), evolucionForm.getNotaFinalizacionAtencion(), evolucion.getCodigoValoracion(), "continuar");
				}
			}
			
		}


		//Los siguientes casos corresponden a cuando NO se desea fin.
		//atención (o simplemente No se tenía atención)
		//Caso línea 1: Es adjunto y no desea finalizar atención
		//Caso línea 2: No es adjunto
		
		if (!(evolucionForm.getEsAdjunto()&&evolucionForm.getDeseaFinalizarAtencion())
			||!evolucionForm.getEsAdjunto())
		{
			//Tres posibles sub-casos:
			//-a. Desea aceptar médico tratante 
			//-b. Desea dejar Médico tratante pendiente
			//-c. Definitivamente NO quiere ser médico tratante
			//-d. Tratante que desea finalizar manejo conjunto
			
			if (evolucionForm.getConvertirMedicoATratante()=='s')
			{
				logger.info("Caso 2-a");
				//-a. Desea aceptar médico tratante
				Solicitud solicitud= new Solicitud();
				if (paciente.getExisteAsocio())
				{
					solicitud.cambiosRequeridosAceptacionTratanteCasoAsocio (con, evolucionForm.getNumeroSolicitudPidioConvertirMedicoATratante(), paciente.getCodigoCuenta());
				}
				ResultadoBoolean resultado = solicitud.inactivarSolicitudCambioTratante(con, evolucionForm.getNumeroSolicitudPidioConvertirMedicoATratante());
				if( !resultado.isTrue() )
				{
					logger.warn("Problemas inactivando la solicitud de cambio de médico tratante "+resultado.getDescripcion());
					throw new SQLException ("Problemas inactivando la solicitud de cambio de médico tratante : "+resultado.getDescripcion());						
				}
				else
				{
					resultado = solicitud.insertarTratante(con, evolucionForm.getNumeroSolicitudPidioConvertirMedicoATratante(), medico.getCodigoPersona(), medico.getCodigoCentroCosto(), true);
					//En este caso hay que finalizar la atención de forma automática
				    request.setAttribute("manejoConjuntoFinalizado", "true");
					solicitud.finalizarAtencionConjuntaTransaccional(con, paciente.getCodigoCuenta(), medico.getCodigoCentroCosto(), "Finalización de Atención conjunta generada automáticamente al aceptar convertirse en Médico Tratante", evolucion.getCodigoValoracion(), ConstantesBD.continuarTransaccion);
					ResultadoBoolean resultado2 = solicitud.inactivarSolicitudCambioTratante(con, evolucionForm.getNumeroSolicitudPidioConvertirMedicoATratante());
					
					//@todo en este punto se hace una actualizacion del estado His cli de la solicitud a la cual se pidio convertir a medico tratante
										
					if( !resultado.isTrue()||!resultado2.isTrue())
					{
						logger.warn("Problemas insertando el médico tratante "+resultado.getDescripcion());						
						throw new SQLException ("Problemas insertando el médico tratante  : "+resultado.getDescripcion());											
					}
				}
				if (terminarTransaccion)
				{
					//No tenía Métodos transaccionales!, toca a mano
					DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).endTransaction(con);
					UtilidadSesion.notificarCambiosObserver(paciente.getCodigoPersona(), this.servlet.getServletContext());
				}
			}


			//-b. Desea dejar Médico tratante pendiente:
			//Se maneja con el primer caso (Todo NO)

			//-c. Definitivamente NO quiere ser médico tratante
			if (evolucionForm.getConvertirMedicoATratante()=='n')
			{
				logger.info("Caso 2-c");
				Solicitud solicitud= new Solicitud();
				ResultadoBoolean resultado = solicitud.inactivarSolicitudCambioTratante(con, evolucionForm.getNumeroSolicitudPidioConvertirMedicoATratante());
				if( !resultado.isTrue() )
				{
					logger.warn("Problemas inactivando la solicitud de cambio de médico tratante "+resultado.getDescripcion());
					throw new SQLException ("Problemas inactivando la solicitud de cambio de médico tratante : "+resultado.getDescripcion());						
				}

				if (terminarTransaccion)
				{
					//No tenía Métodos transaccionales!, toca a mano
					DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).endTransaction(con);
				}
			}
			
			//-d. El tratante desea finalizar manejo conjunto
			if (evolucionForm.getConvertirMedicoATratante()=='p'&&!evolucionForm.getEsAdjunto() && evolucionForm.getDeseaFinalizarAtencion())
			{
				logger.info("Caso 2-d entra!!!!!!!!!!!!");
				Solicitud finalizarAtencionConjunta=new Solicitud();
				if (terminarTransaccion)
					finalizarAtencionConjunta.finalizarAtencionConjuntaTransaccional(con, paciente.getCodigoCuenta(), 0, evolucionForm.getNotaFinalizacionAtencion(), evolucion.getCodigoValoracion(), ConstantesBD.finTransaccion);
				else
					finalizarAtencionConjunta.finalizarAtencionConjuntaTransaccional(con, paciente.getCodigoCuenta(), 0, evolucionForm.getNotaFinalizacionAtencion(), evolucion.getCodigoValoracion(), ConstantesBD.continuarTransaccion);
				
				UtilidadSesion.notificarCambiosObserver(paciente.getCodigoPersona(),request.getSession().getServletContext());
			}
			
			
			//si es tratante y tiene la opcion de generar ordebn de salida en true entonces se debe finalizar el manejo conjunto
			logger.info("tratante->"+evolucionForm.getEsTratante()+" ordensalida->"+evolucionForm.getOrdenSalida()+" tiene manejo conjunto->"+paciente.getManejoConjunto());
			
			if(paciente.getManejoConjunto())
			{	
				if(evolucionForm.getEsTratante() && evolucionForm.getOrdenSalida()!=null && evolucionForm.getOrdenSalida().equals("true"))
				{
					logger.info("si es tratante y tiene la opcion de generar ordebn de salida en true entonces se debe finalizar el manejo conjunto");
					Solicitud finalizarAtencionConjunta=new Solicitud();
					if (terminarTransaccion)
						finalizarAtencionConjunta.finalizarAtencionConjuntaTransaccional(con, paciente.getCodigoCuenta(), 0, "egreso", evolucion.getCodigoValoracion(), ConstantesBD.finTransaccion);
					else
						finalizarAtencionConjunta.finalizarAtencionConjuntaTransaccional(con, paciente.getCodigoCuenta(), 0, "egreso", evolucion.getCodigoValoracion(), ConstantesBD.continuarTransaccion);
					UtilidadSesion.notificarCambiosObserver(paciente.getCodigoPersona(),request.getSession().getServletContext());
				}
			}	
		}
		return numeroEvolucion;
	}
	
	/**
	 * Metodo para car4gar los historicos de la dieta 
	 * @param con
	 * @param mapping
	 * @param usuario
	 * @param paciente
	 * @param forma
	 * @param tipoInfomacion : es el tipo de informacion que se va a cargar.
	 * @param request
	 * @return
	 * @throws SQLException 
	 */
	private void cargarHistoricosDieta(Connection con, UsuarioBasico usuario, PersonaBasica paciente, EvolucionForm forma) throws SQLException
	{
		RegistroEnfermeria mundo = new RegistroEnfermeria();
		String fechaFin = "";

			//---Cargar los nobres y los codigos de la informacion parametrizada de liquidos (Administrados y Eliminados). 
			mundo.cargarDietaEvolucion(con, UtilidadesManejoPaciente.obtenerCuentasXIngreso(con, paciente.getCodigoIngreso()+""),paciente.getCodigoArea(), usuario.getCodigoInstitucionInt(), fechaFin, forma.getMapaDieta());

			
			//---Incrementar un dia para Mostrar Todos los Historicos a partir de una fecha determinada. 
			fechaFin = UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.incrementarDiasAFecha(UtilidadFecha.getFechaActual(), -1, false)) + "-" + UtilidadFecha.getHoraActual();
			
			fechaFin = fechaFin + "&&&&" + UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()) + "-" + UtilidadFecha.getHoraActual();
			
			HashMap mp = new HashMap();
			
			//----------------------------Consultar los nombres de los medicamentos Administrados  
			//mp = mundo.cargarHistoricosDieta(con, paciente.getCodigoCuenta(), usuario.getCodigoCentroCosto(), usuario.getCodigoInstitucionInt(), fechaFin, 1, paciente.getCodigoCuentaAsocio());
			mp = mundo.cargarHistoricosDieta(con,UtilidadesManejoPaciente.obtenerCuentasXIngreso(con, paciente.getCodigoIngreso()+""), paciente.getCodigoArea(), usuario.getCodigoInstitucionInt(), fechaFin, 1);
			forma.setMapaDietaHistorico("nroRegMedAdm", mp.get("numRegistros")+"");
			mp.remove("numRegistros");
			forma.getMapaDietaHistorico().putAll(mp);
			
			//------Cargar los codigo y los nombres de los liquidos eliminados. (SOLAMENTE HAY PARAMETRIZABLES).
			mp.clear();
			//mp = mundo.cargarHistoricosDieta(con, paciente.getCodigoCuenta(), usuario.getCodigoCentroCosto(), usuario.getCodigoInstitucionInt(), fechaFin, 2, paciente.getCodigoCuentaAsocio());			
			mp = mundo.cargarHistoricosDieta(con, UtilidadesManejoPaciente.obtenerCuentasXIngreso(con, paciente.getCodigoIngreso()+""), paciente.getCodigoArea(), usuario.getCodigoInstitucionInt(), fechaFin, 2);			
			forma.setMapaDietaHistorico("nroRegMedElim", mp.get("numRegistros")+"");
			mp.remove("numRegistros");
			forma.getMapaDietaHistorico().putAll(mp);
			
			//------Cargar la informacion registrada de liquidos administados ( parametrizables y no parametrizables ) y eliminados (parametrizados).  
			mp.clear();
			//mp = mundo.cargarHistoricosDieta(con, paciente.getCodigoCuenta(), usuario.getCodigoCentroCosto(), usuario.getCodigoInstitucionInt(), fechaFin, 3, paciente.getCodigoCuentaAsocio());
			mp = mundo.cargarHistoricosDieta(con, UtilidadesManejoPaciente.obtenerCuentasXIngreso(con, paciente.getCodigoIngreso()+""), paciente.getCodigoArea(), usuario.getCodigoInstitucionInt(), fechaFin, 3);
			forma.setMapaDietaHistorico("nroRegBalLiqAdm", mp.get("numRegistros")+"");
			mp.remove("numRegistros");
			forma.getMapaDietaHistorico().putAll(mp);
			
			//----Carga el numero de registros eliminados en el mapa.
			mp.clear();
			//mp = mundo.cargarHistoricosDieta(con, paciente.getCodigoCuenta(), usuario.getCodigoCentroCosto(), usuario.getCodigoInstitucionInt(), fechaFin, 4, paciente.getCodigoCuentaAsocio());
			mp = mundo.cargarHistoricosDieta(con, UtilidadesManejoPaciente.obtenerCuentasXIngreso(con, paciente.getCodigoIngreso()+""), paciente.getCodigoArea(), usuario.getCodigoInstitucionInt(), fechaFin, 4);
			forma.setMapaDietaHistorico("nroRegBalLiqElim", mp.get("numRegistros")+"");

			forma.setMapaDietaHistorico("fechaHistoricoDieta","1");
			forma.setMapaDietaHistorico("paginadorLiqAdmin","0");
			forma.setMapaDietaHistorico("paginadorLiqElim","0");
	}
	
	
	/**
	 * Método implementado para actualizar los datos de epidemiología
	 * @param con
	 * @param form
	 */
	protected int actualizarDatosEpidemiologia(Connection con, EvolucionForm form,PersonaBasica paciente,UsuarioBasico usuario)
	{
		logger.info("-----------------------------------------actualizarDatosEpidemiologia--------------");
		String valorFicha = "";
		String[] vector = new String[0];
		int resp = 1;
		
		//Se verifica valor ficha para el diagnostico complicacion
		if(form.getDiagnosticoDefinitivo("valorFichaDxComplicacion")!=null&&!form.getDiagnosticoDefinitivo("valorFichaDxComplicacion").toString().equals(""))
		{
			valorFicha = form.getDiagnosticoDefinitivo("valorFichaDxComplicacion").toString();
			if(valorFicha.equalsIgnoreCase(ConstantesBD.respuestaLogFichaEpidemiologia))
			{
				vector = form.getDiagnosticoComplicacion_1().split(ConstantesBD.separadorSplit);
				resp = UtilidadFichas.insertarLogFichasReportadas(con, usuario.getLoginUsuario(), paciente.getCodigoPersona(), vector[0],Integer.parseInt(vector[1]), form.getNumeroSolicitud());
			}
			else
			{
				vector = valorFicha.split(ConstantesBD.separadorSplit);
				resp = UtilidadFichas.activarFichaPorCodigo(con, Integer.parseInt(vector[0]), Integer.parseInt(vector[1]));
			}
				
		}
		
		//se verifica para el dx ppal
		if(resp>0)
		{
			if(form.getDiagnosticoDefinitivo("valorFichaDxPrincipal")!=null&&!form.getDiagnosticoDefinitivo("valorFichaDxPrincipal").toString().equals(""))
			{
				valorFicha = form.getDiagnosticoDefinitivo("valorFichaDxPrincipal").toString();
				if(valorFicha.equalsIgnoreCase(ConstantesBD.respuestaLogFichaEpidemiologia))
				{
					vector = form.getDiagnosticoDefinitivo("principal").toString().split(ConstantesBD.separadorSplit);
					resp = UtilidadFichas.insertarLogFichasReportadas(con, usuario.getLoginUsuario(), paciente.getCodigoPersona(), vector[0],Integer.parseInt(vector[1]), form.getNumeroSolicitud());
				}
				else
				{
					vector = valorFicha.split(ConstantesBD.separadorSplit);
					resp = UtilidadFichas.activarFichaPorCodigo(con, Integer.parseInt(vector[0]), Integer.parseInt(vector[1]));
				}
					
			}
		}
		
		//Se verifica valor ficha para el diagnostico complicacion
		if(form.getDiagnosticoDefinitivo("valorFichaDxMuerte")!=null&&!form.getDiagnosticoDefinitivo("valorFichaDxMuerte").toString().equals(""))
		{
			valorFicha = form.getDiagnosticoDefinitivo("valorFichaDxMuerte").toString();
			if(valorFicha.equalsIgnoreCase(ConstantesBD.respuestaLogFichaEpidemiologia))
			{
				vector = form.getDiagnosticoMuerte_1().split(ConstantesBD.separadorSplit);
				resp = UtilidadFichas.insertarLogFichasReportadas(con, usuario.getLoginUsuario(), paciente.getCodigoPersona(), vector[0],Integer.parseInt(vector[1]), form.getNumeroSolicitud());
			}
			else
			{
				vector = valorFicha.split(ConstantesBD.separadorSplit);
				resp = UtilidadFichas.activarFichaPorCodigo(con, Integer.parseInt(vector[0]), Integer.parseInt(vector[1]));
			}
				
		}
		
		
		if(resp>0)
		{
			for(int i=0;i<form.getNumDiagnosticosDefinitivos();i++)
			{
				if(form.getDiagnosticosDefinitivos().containsKey("checkbox_"+i))
				{
					if(form.getDiagnosticoDefinitivo("checkbox_"+i).toString().equals("true"))
					{	
						if(form.getDiagnosticoDefinitivo("valorFichaDxRelacionado_"+i)!=null&&!form.getDiagnosticoDefinitivo("valorFichaDxRelacionado_"+i).equals(""))
						{
							valorFicha = form.getDiagnosticoDefinitivo("valorFichaDxRelacionado_"+i).toString();
							if(valorFicha.equalsIgnoreCase(ConstantesBD.respuestaLogFichaEpidemiologia))
							{
								vector = form.getDiagnosticoDefinitivo("relacionado_"+i).toString().split(ConstantesBD.separadorSplit);
								resp = UtilidadFichas.insertarLogFichasReportadas(con, usuario.getLoginUsuario(), paciente.getCodigoPersona(), vector[0], Integer.parseInt(vector[1]),form.getNumeroSolicitud());
							}
							else
							{
								vector = valorFicha.split(ConstantesBD.separadorSplit);
								resp = UtilidadFichas.activarFichaPorCodigo(con, Integer.parseInt(vector[0]), Integer.parseInt(vector[1]));
							}
							
							//Se verifica éxito de transacción
							if(resp<=0)
								i = form.getNumDiagnosticosDefinitivos();
						}
					}	
				}	
			}
		}
		return resp;
	}

}
