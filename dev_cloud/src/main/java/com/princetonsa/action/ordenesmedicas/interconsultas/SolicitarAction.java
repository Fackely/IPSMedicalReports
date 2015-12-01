/*
* @(#)SolicitarAction.java
*
* Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
*
* Lenguaje		: Java
* Compilador	: J2SDK 1.4.2_03
*/

package com.princetonsa.action.ordenesmedicas.interconsultas;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Vector;

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
import org.apache.struts.util.MessageResources;
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
import util.facturacion.UtilidadesFacturacion;
import util.historiaClinica.UtilidadesHistoriaClinica;
import util.laboratorios.InterfazLaboratorios;
import util.manejoPaciente.UtilidadesManejoPaciente;
import util.odontologia.UtilidadOdontologia;
import util.ordenesMedicas.UtilidadesOrdenesMedicas;

import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.ordenesmedicas.interconsultas.SolicitarForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dto.comun.DtoCheckBox;
import com.princetonsa.dto.facturacion.DtoArticuloIncluidoSolProc;
import com.princetonsa.dto.facturacion.DtoServicioIncluidoSolProc;
import com.princetonsa.dto.manejoPaciente.DtoDiagnostico;
import com.princetonsa.dto.manejoPaciente.DtoSolicitudesSubCuenta;
import com.princetonsa.dto.manejoPaciente.DtoSubCuentas;
import com.princetonsa.dto.ordenes.DtoOrdenesAmbulatorias;
import com.princetonsa.dto.ordenes.DtoProcedimiento;
import com.princetonsa.mundo.Cama;
import com.princetonsa.mundo.Cuenta;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.atencion.Egreso;
import com.princetonsa.mundo.cargos.Cargos;
import com.princetonsa.mundo.cargos.Convenio;
import com.princetonsa.mundo.cargos.UtilidadJustificacionPendienteArtServ;
import com.princetonsa.mundo.facturacion.Cobertura;
import com.princetonsa.mundo.facturacion.Servicios_ArticulosIncluidosEnOtrosProcedimientos;
import com.princetonsa.mundo.inventarios.FormatoJustServNopos;
import com.princetonsa.mundo.manejoPaciente.Autorizaciones;
import com.princetonsa.mundo.ordenesmedicas.OrdenMedica;
import com.princetonsa.mundo.ordenesmedicas.OrdenesAmbulatorias;
import com.princetonsa.mundo.ordenesmedicas.cirugias.SolicitudesCx;
import com.princetonsa.mundo.salasCirugia.Peticion;
import com.princetonsa.mundo.solicitudes.Solicitud;
import com.princetonsa.mundo.solicitudes.SolicitudProcedimiento;
import com.princetonsa.pdf.ResumenGuardarProcedimientoPdf;
import com.princetonsa.util.birt.reports.DesignEngineApi;
import com.princetonsa.util.birt.reports.ParamsBirtApplication;
import com.servinte.axioma.bl.manejoPaciente.facade.ManejoPacienteFacade;
import com.servinte.axioma.bl.ordenes.facade.OrdenesFacade;
import com.servinte.axioma.common.ErrorMessage;
import com.servinte.axioma.dto.capitacion.DtoAutorizacionCapitacionOrdenAmbulatoria;
import com.servinte.axioma.dto.facturacion.ContratoDto;
import com.servinte.axioma.dto.facturacion.ConvenioDto;
import com.servinte.axioma.dto.facturacion.MontoCobroDto;
import com.servinte.axioma.dto.manejoPaciente.AutorizacionCapitacionDto;
import com.servinte.axioma.dto.manejoPaciente.DatosPacienteAutorizacionDto;
import com.servinte.axioma.dto.ordenes.OrdenAutorizacionDto;
import com.servinte.axioma.dto.ordenes.ServicioAutorizacionOrdenDto;
import com.servinte.axioma.fwk.exception.IPSException;
import com.servinte.axioma.fwk.exception.util.CODIGO_ERROR_NEGOCIO;
import com.servinte.axioma.hibernate.HibernateUtil;
import com.servinte.axioma.mundo.fabrica.capitacion.CapitacionFabricaMundo;
import com.servinte.axioma.mundo.fabrica.ordenes.OrdenesFabricaMundo;
import com.servinte.axioma.mundo.interfaz.capitacion.IAutorizacionCapitacionOrdenesAmbulatoriasMundo;
import com.servinte.axioma.mundo.interfaz.ordenes.IOrdenesAmbulatoriasMundo;
import com.servinte.axioma.orm.Ingresos;
import com.servinte.axioma.orm.IngresosHome;
import com.servinte.axioma.orm.delegate.manejoPaciente.IngresosDelegate;

/**
* Esta clase encapsula el flujo relacionado con una Solicitud General, antes de pasar a la opciï¿½n
* especï¿½fica
*
* @version 1.0, Feb 10, 2004
*/
public class SolicitarAction extends Action
{
	/** Objeto para manejar los logs de esta clase */
	private transient Logger logger = Logger.getLogger(SolicitarAction.class);
	
	/**
	 * 
	 */
	public int numeroSolicitudServIncluida=ConstantesBD.codigoNuncaValido;
	
	MessageResources messageResource = MessageResources.getMessageResources("com.servinte.mensajes.ordenes.SolicitarForm");
	
	/**
	 * Mï¿½todo encargado de el flujo y control de la funcionalidad
	 * 
	 * @see org.apache.struts.action.Action#execute(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse)
	 */
	@SuppressWarnings({ "rawtypes"})
	public ActionForward execute(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)throws Exception
		{
		Connection con=null;
		try {
			if (response==null); //Para evitar que salga el warning
			if(form instanceof SolicitarForm)
			{
				try
				{
					con = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection();
				}
				catch(SQLException e)
				{
					logger.warn("No se pudo abrir la conexiï¿½n"+e.toString());
				}

				Utilidades.obtenerCodigoActividadDadaOrdenAmbulatoria(con,"");
				PersonaBasica paciente=(PersonaBasica)request.getSession().getAttribute("pacienteActivo");
				SolicitarForm solicitarForm =(SolicitarForm)form;
				String estado=solicitarForm.getEstado();
				UsuarioBasico usuario=obtenerUsuario(con,request,solicitarForm);

				solicitarForm.setMostrarImprimirAutorizacion(false);
				solicitarForm.setListaNombresReportes(new ArrayList<String>());
				solicitarForm.setMensajesAdvertenciaMap(new HashMap());

				logger.info("ESTADO -----"+estado);

				//inicializar la ficha epidemiologica
				if(estado.equals("empezar"))
				{
					logger.info(">---------SE INICIALIZA EL SEGUNDO NIVEL----------<");
					ActionForward validacionesAccesoForward= this.validacionesComunes(mapping, request, paciente, usuario, con,!solicitarForm.isFichaEpidemiologica(),solicitarForm);
					if (validacionesAccesoForward != null)
					{
						UtilidadBD.closeConnection(con);
						return validacionesAccesoForward ;
					}
					else
					{
						solicitarForm.setFichaEpidemiologica(false);
					}

					solicitarForm.setRequiereJustificacionServicio(Utilidades.requiererJustificacionServiciosArticulos(con,paciente.getCodigoIngreso(),true));

				}

				///para poner el el request una variable que me indica que es ficha epidemiologica, y en caso de llagar
				///al comun errores, no muestr los encabezados
				request.setAttribute("ocultarEncabezadoErrores", solicitarForm.isFichaEpidemiologica()+"");
				solicitarForm.setEscruento(false);
				logger.info("\n\n  EN interconsultas.SolicitarAction  El ESTADO ["+estado + "]  \n\n ");

				if(!UtilidadValidacion.esProfesionalSalud(usuario) && !UtilidadValidacion.esMedico(usuario).equals("") && UtilidadCadena.noEsVacio(UtilidadValidacion.esEnfermera(usuario)))
				{
					solicitarForm.reset();
					return ComunAction.accionSalirCasoError(mapping,request,con, logger, "errors.noProfesionalSaludMedico", UtilidadValidacion.esMedico(usuario), true);//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
				}
				///////esta validacion la puso Armando.   /////para un errror de xplanner llamado Nov2404 - Solicitud Servicio - Validacion tratante o adjunto
				if((paciente.getCodigoUltimaViaIngreso()==ConstantesBD.codigoViaIngresoHospitalizacion || paciente.getCodigoUltimaViaIngreso()==ConstantesBD.codigoViaIngresoUrgencias) && request.getParameter("accionPYP")==null )
				{
					boolean permisos;
					permisos=UtilidadValidacion.esMedicoTratante(con, usuario,paciente).equals("") || UtilidadValidacion.esAdjuntoCuenta(con,paciente.getCodigoCuenta(),usuario.getLoginUsuario()) || !UtilidadCadena.noEsVacio(UtilidadValidacion.esEnfermera(usuario));
					if(!permisos && !UtilidadValidacion.esProfesionalSalud(usuario))
					{
						solicitarForm.reset();
						String mensaje = UtilidadValidacion.esMedicoTratante(con, usuario,paciente);
						//se verifica si el error fue por la validaciï¿½n del mï¿½dico tratante
						if(!mensaje.equals(""))
							return ComunAction.accionSalirCasoError(mapping,request,con, logger, "El médico no es tratante", mensaje, true);//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
						else
							return ComunAction.accionSalirCasoError(mapping,request,con, logger, "El médico no es adjunto", "error.validacionessolicitud.medicoNoTratanteNiAdjunto", true);//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
					}
				}



				////////////////termina la validacion//////////////////

				ActionForward validacionesGenerales = this.validacionesComunes(mapping, request, paciente, usuario, con,!solicitarForm.isFichaEpidemiologica(),solicitarForm);
				if (validacionesGenerales != null)
				{
					//iniciar el indicativo para hacer la validacion desde el menu
					solicitarForm.setValidarEgresoPaciente(false);
					UtilidadBD.closeConnection(con);
					return validacionesGenerales ;
				}

				if(estado == null)
				{
					return ComunAction.accionSalirCasoError(mapping, request, con, logger, "La accion específicada no esta definida", "errors.estadoInvalido", true);
				}
				else if (estado.equals("empezar"))
				{
					//iniciar el indicativo para hacer la validacion desde el menu
					solicitarForm.setValidarEgresoPaciente(false);

					/**
					 * Validar concurrencia
					 * Si ya estï¿½ en proceso de distribucion, no debe dejar entrar
					 **/
					if(UtilidadValidacion.estaEnProcesoDistribucion(con, paciente.getCodigoPersona(), usuario.getLoginUsuario()) )
					{
						return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error.facturacion.cuentaEnProcesoDistribucion", "error.facturacion.cuentaEnProcesoDistribucion", true);
					}
					/**
					 * Validar concurrencia
					 * Si ya estï¿½ en proceso de facturaciï¿½n, no debe dejar entrar
					 **/
					else if(UtilidadValidacion.estaEnProcesofacturacion(con, paciente.getCodigoPersona(),"") )
					{
						return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error.facturacion.cuentaEnProcesoFact", "error.facturacion.cuentaEnProcesoFact", true);
					}
					else
					{
						return this.accionEmpezar(mapping, con, solicitarForm, paciente, usuario);
					}


				}
				else if (estado.equals("insertar"))
				{
					solicitarForm.resetSevArtIncluidos();
					solicitarForm.resetJus();
					solicitarForm.setNumerosSolicitudes(new HashMap<String, Object>());
					solicitarForm.setNumerosSolicitudes("numRegistros", "0");				

					if(solicitarForm.isOtraSolicitud())
					{
						solicitarForm.resetNuevaSolicitud();
						inicializarSolicitud(solicitarForm,usuario,paciente,con);
					}
					return this.accionInsertar(request, mapping,solicitarForm, paciente, usuario, con);
				}
				else if (estado.equals("salir"))
				{
					request.getSession().removeAttribute("MAPAJUS");
					return this.accionSalir(response,request, mapping, solicitarForm, paciente, usuario, con);
				}
				else if(estado.equals("buscarServicio"))
				{
					cargarServiciosArticulosIncluidos(con, solicitarForm, usuario);

					//Si el centro de costo de la autorizacion no corresponde con el de la cuenta del paciente
					//(DCU 40-V 1.52)
					/*if(solicitarForm.isIndicativoOrdenAmbulatoria())
				{	if( solicitarForm.isExisteAutorizacionOrden())
					{	if( !solicitarForm.isBotonGenerarSolicitud() )
						{	ActionErrors errores=new ActionErrors();
							errores.add("centroCostoNoCorres", new ActionMessage("errors.notEspecific",messageResource.getMessage("solicitarForm.centroCostoNoCorresponde")));
							saveErrors(request, errores);
						}
					}
				}*/

					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");
				}
				else if(estado.equals("eliminarServicio"))
				{
					String [] indices={
							"hidden_multiple_",
							"codigo_",
							"nombre_",
							"codigoPropietario_",
							"urgente_",
							"frecuencia_",
							"codigoEspecialidad_",
							"centro_costo_",
							"esSerConvCapitado_",
							"esMultiple_",
							"multiple_",
							"cantidad_",
							"especialidad_",
							"esMultiple_",
							"formulario_",
							"estadoCheckBox_",
							"finalidad_",
							"tipo_frecuencia_",
							"esPos_",
							"grupoServicio_",
							"justificar_",
							"portatil_"
					};

					solicitarForm.setValores("numRegistros",solicitarForm.getNumeroServicios()+"");
					Utilidades.eliminarRegistroMapaGenerico(solicitarForm.getValores(),new HashMap(),solicitarForm.getPosEliminar(),indices,"numRegistros","tiporegistro_","BD",false);
					solicitarForm.setNumeroServicios(solicitarForm.getNumeroServicios()-1);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");
				}

				else if (estado.equals("cancelar"))
				{
					return this.accionCancelar(request, mapping, con);
				}
				else if (estado.equals("resumenProcedimiento"))
				{
					return this.accionResumen(con, mapping);
				}
				else if (estado.equals("imprimir"))
				{
					// Evaluamos si es media carta o no
					if(UtilidadTexto.getBoolean(ValoresPorDefecto.getImpresionMediaCarta(usuario.getCodigoInstitucionInt())))
						return this.accionImprimirSolicitud(con, solicitarForm, usuario, request, paciente, mapping,true);
					else
						return this.accionImprimirSolicitud(con, solicitarForm, usuario, request, paciente, mapping,false);						
				}
				else if (estado.equals("adicionarJus"))
				{
					logger.info("Servicio: "+Utilidades.convertirAEntero(solicitarForm.getCodigoServicioPostular()));
					logger.info("OrdenAmbulatoria: "+Utilidades.obtenerCodigoOrdenAmbulatoria(con,solicitarForm.getOrdenAmbulatoria(),usuario.getCodigoInstitucionInt()));

					/* 	Si el codigo del servicio postulado y el codigo de orden ambulatoria son validos entonces se evalua si ya se ha ingresado la justificacion no pos
					y se cambia la llave de justificar indicando que se debe actualizar el codigo de solicitud cuando esta se formalise */
					if (Utilidades.convertirAEntero(solicitarForm.getCodigoServicioPostular())>0 
							&& Utilidades.convertirAEntero(solicitarForm.getOrdenAmbulatoria())>0
							&& UtilidadJustificacionPendienteArtServ.existeJustificacionDeOrdenAmbulatoria(con, Utilidades.convertirAEntero(Utilidades.obtenerCodigoOrdenAmbulatoria(con,solicitarForm.getOrdenAmbulatoria(),usuario.getCodigoInstitucionInt())), Utilidades.convertirAEntero(solicitarForm.getCodigoServicioPostular()), false))
						solicitarForm.setValores("justificar_0", "ordenAmbulatoria");

					if(request.getSession().getAttribute("MAPAJUSSERV")!=null&&request.getSession().getAttribute("MAPAJUSSERVFORM")!=null)
					{
						HashMap justificacion = new HashMap();
						HashMap justificacionForma = new HashMap();
	    		    	justificacion= (HashMap)request.getSession().getAttribute("MAPAJUSSERV");
	    		    	justificacionForma = (HashMap)request.getSession().getAttribute("MAPAJUSSERVFORM");
	    		    	
	    		    	for(int i=0;i<solicitarForm.getNumeroServicios();i++)
	    		    	{
	    		    		if((justificacion.get("0_servicio")+"").equals(solicitarForm.getJustificacionesServicios(i+"_servicio")+""))
	    		    		{
	    		    			solicitarForm.getJustificacionesServicios().put(i+"_mapasecciones", justificacion.get("0_mapasecciones"));
	    		    			
	    		    			HashMap seccionesMap=(HashMap) justificacion.get("0_mapasecciones");
	    		    			int numRegistrosSecciones=Integer.parseInt(seccionesMap.get("numRegistros").toString());
	    		    			for(int j=0;j<numRegistrosSecciones;j++){
	    		    				
	    		    				solicitarForm.getJustificacionesServicios().put(i+"_numRegistrosXSec_"+seccionesMap.get("codigo_"+j), justificacion.get("0_numRegistrosXSec_"+seccionesMap.get("codigo_"+j).toString()));
	    		    				
	    		    				int numRegistrosXSeccion=Integer.parseInt(justificacion.get("0_numRegistrosXSec_"+seccionesMap.get("codigo_"+j).toString()).toString());;
	    		    				String codigoSeccion=seccionesMap.get("codigo_"+j).toString();
	    		    				for(int k=0;k<numRegistrosXSeccion;k++){
	    		    					
	    		    					solicitarForm.getJustificacionesServicios().put(i+"_tipo_"+codigoSeccion+"_"+k, justificacion.get("0_tipo_"+codigoSeccion+"_"+k));
	    		    					solicitarForm.getJustificacionesServicios().put(i+"_numhijos_"+seccionesMap.get("codigo_"+j).toString()+"_"+k, justificacion.get("0_numhijos_"+seccionesMap.get("codigo_"+j).toString()+"_"+k));
	    		    					
	    		    					if(justificacion.get("0_tipo_"+codigoSeccion+"_"+k).toString().equals("CHEC")){
	    		    						for (int h=0; h<Utilidades.convertirAEntero(justificacion.get("0_numhijos_"+seccionesMap.get("codigo_"+j).toString()+"_"+k).toString()); h++)
    		    							{
    		    								solicitarForm.getJustificacionesServicios().put(i+"_valorcampohijo_"+seccionesMap.get("codigo_"+j).toString()+"_"+ConstantesBD.JusOrdenCampoRiesgo+"_"+h, justificacion.get("0_valorcampohijo_"+seccionesMap.get("codigo_"+j).toString()+"_"+ConstantesBD.JusOrdenCampoRiesgo+"_"+h));
    		    								solicitarForm.getJustificacionesServicios().put(i+"_etiguetaseccion_"+seccionesMap.get("codigo_"+j).toString(), justificacion.get("0_etiguetaseccion_"+seccionesMap.get("codigo_"+j).toString()));
    		    								solicitarForm.getJustificacionesServicios().put(i+"_codigocampohijo_"+seccionesMap.get("codigo_"+j).toString()+"_"+ConstantesBD.JusOrdenCampoRiesgo+"_"+h, justificacion.get("0_codigocampohijo_"+seccionesMap.get("codigo_"+j).toString()+"_"+ConstantesBD.JusOrdenCampoRiesgo+"_"+h));
    		    								solicitarForm.getJustificacionesServicios().put(i+"_etiquetacampohijo_"+seccionesMap.get("codigo_"+j).toString()+"_"+ConstantesBD.JusOrdenCampoRiesgo+"_"+h, justificacion.get("0_etiquetacampohijo_"+seccionesMap.get("codigo_"+j).toString()+"_"+ConstantesBD.JusOrdenCampoRiesgo+"_"+h));
    		    								solicitarForm.getJustificacionesServicios().put(i+"_codigoparamjuscampohijo_"+seccionesMap.get("codigo_"+j).toString()+"_"+ConstantesBD.JusOrdenCampoRiesgo+"_"+h, justificacion.get("0_codigoparamjuscampohijo_"+seccionesMap.get("codigo_"+j).toString()+"_"+ConstantesBD.JusOrdenCampoRiesgo+"_"+h));
    		    							}
	    		    					}else
	    		    					{
	    		    						solicitarForm.getJustificacionesServicios().put(i+"_valorcampo_"+codigoSeccion+"_"+k, justificacion.get("0_valorcampo_"+codigoSeccion+"_"+k));
    		    							solicitarForm.getJustificacionesServicios().put(i+"_etiquetaseccion_"+codigoSeccion+"_"+k, justificacion.get("0_etiquetaseccion_"+codigoSeccion+"_"+k));
    		    							solicitarForm.getJustificacionesServicios().put(i+"_codigocampo_"+codigoSeccion+"_"+k, justificacion.get("0_codigocampo_"+codigoSeccion+"_"+k));
    		    							solicitarForm.getJustificacionesServicios().put(i+"_etiquetacampo_"+codigoSeccion+"_"+k, justificacion.get("0_etiquetacampo_"+codigoSeccion+"_"+k));
    		    							solicitarForm.getJustificacionesServicios().put(i+"_codigoparametrizacion_"+codigoSeccion+"_"+k, justificacion.get("0_codigoparametrizacion_"+codigoSeccion+"_"+k));
	    		    					}
	    		    							
	    		    							
	    		    							
	    		    				}
	    		    			}
	    		    			
	    		    			solicitarForm.getJustificacionesServicios().put(i+"_mapajustservform", justificacionForma);
	    		    		}
	    		    	}
					}
					
					request.getSession().setAttribute("MAPAJUS", solicitarForm.getJustificacionesServicios());
    		    	
					/*logger.info("__________________________________________ mapa para subir "+solicitarForm.getJustificacionesServicios());
				logger.info("\n\n\n/////////////////////////////////////////////////////////////////////////////////////////");
				Utilidades.imprimirMapa(solicitarForm.getJustificacionesServicios());
				logger.info("/////////////////////////////////////////////////////////////////////////////////////////\n\n\n");*/
					//////PARTE DE SERVICIOS
					cargarServiciosArticulosIncluidos(con, solicitarForm, usuario);
					solicitarForm.setEstado("salir");
					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");
				}
				else if(estado.equals("imprimirExterno"))
				{
					return this.accionImprimirExterno(con, solicitarForm.getNumeroSolicitud(), mapping, usuario, paciente, request, solicitarForm);
				}
				else if(estado.equals("insertarOrdenAmbulatoria"))
				{
					return this.accionInsertarOrdenAmbulatoria(con,solicitarForm,usuario,paciente,mapping,request);
				}
				else if(estado.equals("postulandoServiciosEpidemiologia"))
				{
					return this.accionPostularServiciosOrdenEpidemiologia(con,solicitarForm,usuario,paciente,mapping,request);
				}
				else
				{
					return ComunAction.accionSalirCasoError(mapping, request, con, logger, "La accion especificada no esta definida", "errors.estadoInvalido", true);
				}
			}
			else
			{
				logger.warn("Tipo de forma invalida, (Se esperaba SolicitarForm)");

				request.setAttribute("codigoDescripcionError", "errors.formaTipoInvalido");
				return mapping.findForward("paginaError");
			}
		} catch (IPSException e) {
			Log4JManager.error(e);
			ActionMessages mensajeError = new ActionMessages();
			mensajeError.add("", new ActionMessage(String.valueOf(e.getErrorCode())));
			saveErrors(request, mensajeError);
			return mapping.findForward("principal");
		} catch (Exception e) {
			Log4JManager.error(e);
			return ComunAction.accionSalirCasoError(mapping, request, con, logger, "La accion especificada no esta definida", "errors.estadoInvalido", true);
		}
		finally{
			UtilidadBD.closeConnection(con);
		}
	}
	
	/**
	 * Metodo encargado de consultar la informacion de los servicios y articulos incluidos
	 * @param con
	 * @param solicitarForm
	 * @param usuario
	 */
	private void cargarServiciosArticulosIncluidos(Connection con, SolicitarForm solicitarForm, UsuarioBasico usuario) 
	{
		//segun documentacion (2008-09-24) los flujos de ordenes amb y pyp no deben cargar los servicios incluidos
		//segun calidad(27-08-2009), deberia funcionar igual que ordenes, es decir si tiene incluidos en todo momento debe mostrarlos
		//if(!solicitarForm.isSolPYP() && !solicitarForm.isIndicativoOrdenAmbulatoria())
		{	
			Utilidades.imprimirMapa( solicitarForm.getValores());
			logger.info("num serv-->"+solicitarForm.getNumeroServicios());
			
			int codigoServicio= Utilidades.convertirAEntero(solicitarForm.getValores("codigo_"+(solicitarForm.getNumeroServicios()-1))+"");
			//(si el servicio no estaba en el mapa de valores es por que viene de pyp o de ordenes ambulatorias, y el codigo del servicio ya esta cargado en la forma.
			if(codigoServicio<=0)
			{
				codigoServicio=Utilidades.convertirAEntero(solicitarForm.getCodigoServicioPostular());
			}
			logger.info("codigo servicio->"+codigoServicio);
			
			logger.info("\n\n*****************************CARGAR INFORMACION DE LOS SERVICIOS Y ARTICULOS INCLUIDOS**************************************");

			solicitarForm.setServiciosIncluidos(new HashMap<Object, Object>());
			solicitarForm.setArticulosIncluidos(new HashMap<Object, Object>());

			if(!solicitarForm.getServiciosIncluidos().containsKey("numRegistros_"+codigoServicio))
			{	
				solicitarForm.getServiciosIncluidos().putAll(Servicios_ArticulosIncluidosEnOtrosProcedimientos.cargarServiciosIncluidosServicioPrincipal(con, codigoServicio, true /*activo*/, usuario.getCodigoInstitucionInt()));
				Utilidades.imprimirMapa(solicitarForm.getServiciosIncluidos());
				solicitarForm.getArticulosIncluidos().putAll(Servicios_ArticulosIncluidosEnOtrosProcedimientos.cargarArticulosIncluidosServicioPrincipal(con, codigoServicio, true /*activo*/, usuario.getCodigoInstitucionInt()));
				Utilidades.imprimirMapa(solicitarForm.getArticulosIncluidos());
			}	
			
			logger.info("\n\n*****************************FINNNNNNN  CARGAR INFORMACION DE LOS SERVICIOS Y ARTICULOS INCLUIDOS**************************************");
		}
	}


	/**
	 * 
	 * @param con
	 * @param request
	 * @param solicitarForm
	 * @return
	 */
	private UsuarioBasico obtenerUsuario(Connection con, HttpServletRequest request, SolicitarForm solicitarForm) 
	{
		UsuarioBasico usuario=new UsuarioBasico();
		if(solicitarForm.isIndicativoOrdenAmbulatoria())
		{
			String infoSol=OrdenesAmbulatorias.obtenerInfoServicioProcOrdenAmbulatoria(con,Utilidades.obtenerCodigoOrdenAmbulatoria(con,solicitarForm.getOrdenAmbulatoria(),((UsuarioBasico)request.getSession().getAttribute("usuarioBasico")).getCodigoInstitucionInt()), usuario.getCodigoInstitucionInt());
								
			if(!infoSol.trim().equals(""))
			{
				String[] vectorInfoSol=infoSol.split(ConstantesBD.separadorSplit);
										
				solicitarForm.setFechaOrdenAmb(vectorInfoSol[8]);
				
				int centroCosto=((UsuarioBasico)request.getSession().getAttribute("usuarioBasico")).getCodigoCentroCosto();
				String nomCentroCosto=((UsuarioBasico)request.getSession().getAttribute("usuarioBasico")).getCentroCosto();
				String nomCentroAtencion=((UsuarioBasico)request.getSession().getAttribute("usuarioBasico")).getCentroAtencion();
				int centroAtencion=((UsuarioBasico)request.getSession().getAttribute("usuarioBasico")).getCodigoCentroAtencion();
				String codigoUPGD=((UsuarioBasico)request.getSession().getAttribute("usuarioBasico")).getCodigoUPGD();
				
				try {
					usuario.cargarUsuarioBasico (con,vectorInfoSol[3]);
				} catch (SQLException e) {
					e.printStackTrace();
				}
				logger.info("***************************************************************************"+usuario.getLoginUsuario());
				
				//la inactivacion de los usuarios lo que hace es poner en la institucion 0 entonces tendriamos que ponerle la insticion del usuario en sesion
				if(usuario.getCodigoInstitucionInt()==0)
				{
					usuario.setCodigoInstitucion(((UsuarioBasico)request.getSession().getAttribute("usuarioBasico")).getCodigoInstitucion());
				}
				
				if(usuario.getCodigoCentroCosto()<=0)
				{	
					usuario.setCodigoCentroCosto(centroCosto);
					usuario.setCentroCosto(nomCentroCosto);
					usuario.setCentroAtencion(nomCentroAtencion);
					usuario.setCodigoCentroAtencion(centroAtencion);
					usuario.setCodigoUPGD(codigoUPGD);
				}
			}
			else
			{
				usuario=(UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
			}
			
		}
		else
		{
			usuario=(UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
		}
		return usuario;
	}


	/**
	 * 
	 * @param con
	 * @param solicitarForm
	 * @param usuario
	 * @param paciente
	 * @param mapping
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	@SuppressWarnings("rawtypes")
	private ActionForward accionPostularServiciosOrdenEpidemiologia(Connection con, SolicitarForm solicitarForm, UsuarioBasico usuario, PersonaBasica paciente, ActionMapping mapping, HttpServletRequest request) throws Exception
	{
		solicitarForm.resetNuevaSolicitud();
		inicializarSolicitud(solicitarForm,usuario,paciente,con);
		if(!solicitarForm.getCodigoEnfermedadEpidemiologia().trim().equals(""))
		{
			ArrayList codigoServicio=Utilidades.consultarServiciosEnfermedadEpidemiologia(con,solicitarForm.getCodigoEnfermedadEpidemiologia(),usuario.getCodigoInstitucion());
			if(codigoServicio.size()>0)
				cargarServiciosPostular(con,paciente,solicitarForm,codigoServicio, usuario);
			solicitarForm.setCodigoEnfermedadEpidemiologia("");
		}
		return this.accionInsertarProcedimientos(mapping, solicitarForm, request, paciente, usuario, con);
	}

	@SuppressWarnings("rawtypes")
	private void cargarServiciosPostular(Connection con, PersonaBasica paciente, SolicitarForm solicitarForm, ArrayList codigoServicio, UsuarioBasico usuario)
	{
		for(int i=0;i<codigoServicio.size();i++)
		{
			HashMap servicios=Utilidades.consultarInformacionServicio(con,codigoServicio.get(i)+"", usuario.getCodigoInstitucionInt());
			if(Integer.parseInt(servicios.get("numRegistros")+"")>0)
			{
				solicitarForm.setValores("codigo_"+i, servicios.get("codigo")+"");
				solicitarForm.setValores("codigoPropietario_"+i, servicios.get("codigopropietario")+"");
				solicitarForm.setValores("nombre_"+i, servicios.get("nombre")+"");
				solicitarForm.setValores("esPos_"+i, servicios.get("espos")+"");
				solicitarForm.setValores("especialidad_"+i, servicios.get("nombreespecialidad")+"");
				solicitarForm.setValores("codigoEspecialidad_"+i, servicios.get("especialidad")+"");
				solicitarForm.setValores("esExcepcion_"+i, servicios.get("esexcepcion")+"");
				solicitarForm.setValores("formulario_"+i, servicios.get("formulario")+"");
				solicitarForm.setValores("grupoServicio_"+i, servicios.get("gruposervicio")+"");
				solicitarForm.setValores("hidden_multiple_"+i,(UtilidadValidacion.esServicioMultiple(con,servicios.get("codigo")+"")));
				solicitarForm.setValores("tiposervicio_"+i, servicios.get("tiposervicio"));
				solicitarForm.setValores("frecuencia_"+i, "");
				solicitarForm.setValores("tipo_frecuencia_"+i, "1");
				solicitarForm.setValores("cantidad_"+i, "1");
				solicitarForm.setValores("esMultiple_"+i, "false");
				solicitarForm.setValores("urgente_"+i, "false");
				solicitarForm.setValores("estadoCheckBox_"+i, "false");
				
				//////////////////////////////////////////////////////////////////////////////////////
				// se agrega el manejo del portatil anexo 591
				solicitarForm.setValores("portatil_"+i, ConstantesBD.codigoNuncaValido+"");
				solicitarForm.setValores("portatilCheckBox_"+i, "false");
				//////////////////////////////////////////////////////////////////////////////////////
				
				solicitarForm.setValores("esSerConvCapitado_"+i,(Convenio.esConvenioCapitado(con, paciente.getCodigoCuenta()+"")&&!Utilidades.esNivelServicioContratado(con,paciente.getCodigoCuenta()+"",servicios.get("codigo")+""))+"");
			}
		}
		logger.info("\n\nMAPA SERVICIOS-->>"+solicitarForm.getValores()+"\n\n");
		solicitarForm.setNumeroServicios(codigoServicio.size());
	}

	/**
	 * 
	 * @param con
	 * @param solicitarForm
	 * @param mapping 
	 * @param usuario 
	 * @param paciente 
	 * @param request 
	 * @return
	 */
	private ActionForward accionInsertarOrdenAmbulatoria(Connection con, SolicitarForm solicitarForm, UsuarioBasico usuario, PersonaBasica paciente, ActionMapping mapping, HttpServletRequest request) throws SQLException 
	{
		solicitarForm.setFechaSolicitud(UtilidadFecha.getFechaActual());
		solicitarForm.setHoraSolicitud(UtilidadFecha.getHoraActual());
		solicitarForm.setNumerosSolicitudes(new HashMap<String, Object>());
		solicitarForm.setNumerosSolicitudes("numRegistros", "0");		
		solicitarForm.setNumeroServiciosMaximo(1);
		solicitarForm.setNumeroServicios(0);
		String infoSol=OrdenesAmbulatorias.obtenerInfoServicioProcOrdenAmbulatoria(con,Utilidades.obtenerCodigoOrdenAmbulatoria(con,solicitarForm.getOrdenAmbulatoria(),usuario.getCodigoInstitucionInt()), usuario.getCodigoInstitucionInt());
		String codigoOA=Utilidades.obtenerCodigoOrdenAmbulatoria(con,solicitarForm.getOrdenAmbulatoria(),usuario.getCodigoInstitucionInt());
		String centroCostoDes = OrdenesAmbulatorias.obtenerCentroCostoSolicitanteDesc(con,codigoOA);
		 
		logger.info("\n\nORDEN AMBULATORIA------------>"+solicitarForm.getOrdenAmbulatoria());
		
		if(!centroCostoDes.equals(""))
		{
			solicitarForm.setCentroCostoSolicitante(centroCostoDes.split(ConstantesBD.separadorSplit)[0]);
			solicitarForm.setDescripcionCentroCostoSolicitado(centroCostoDes.split(ConstantesBD.separadorSplit)[1]);
		}
		else		
			solicitarForm.setCentroCostoSolicitante(ConstantesBD.codigoNuncaValido+"");		
		
		
		if(!infoSol.trim().equals(""))
		{
			//   *            *                *						*					*					*			*          *
			//urgente - observaciones - centro_atencion_solicita - usuario_solicita - especialidad_solicita - servicio - finalidad - servicio cups
			logger.info("\n\n INFORMACION SOLICITUD >>" +infoSol);
			String[] vectorInfoSol=infoSol.split(ConstantesBD.separadorSplit);
			solicitarForm.setCodigoTipoSolicitud(Utilidades.obtenerTipoServicio(con,vectorInfoSol[5]).charAt(0));
			solicitarForm.setEspecialidadSolicitante(Integer.parseInt(vectorInfoSol[4]));
			solicitarForm.setComentario(vectorInfoSol[1]);
			solicitarForm.setPostularServicio(true);
			solicitarForm.setCodigoServicioPostular(vectorInfoSol[5]);
			solicitarForm.setFinalidad(Integer.parseInt(vectorInfoSol[6]));
			solicitarForm.setServicioAmbUrgente(UtilidadTexto.getBoolean(vectorInfoSol[0]));
			solicitarForm.setCantidadServicioOrdenAmbulatoria(vectorInfoSol[7]);
			solicitarForm.resetJus();
			
			cargarServiciosArticulosIncluidos(con, solicitarForm, usuario); // segun documentación
			
			/*
			solicitarForm.setServiciosIncluidos(new HashMap<Object, Object>());
			solicitarForm.setArticulosIncluidos(new HashMap<Object, Object>());
			*/
			
			//------------------------------------------------------------------------------------------------------------
			
			solicitarForm.resetAutorizOrden();
			//ActionErrors errores=new ActionErrors();
			ArrayList<DtoAutorizacionCapitacionOrdenAmbulatoria> listaAutorizaciones = null;
			DtoAutorizacionCapitacionOrdenAmbulatoria dtoAutorizCapitaOrdenAmbu=new DtoAutorizacionCapitacionOrdenAmbulatoria();
			dtoAutorizCapitaOrdenAmbu.getDtoOrdenesAmbulatorias().setNumeroOrden(solicitarForm.getOrdenAmbulatoria());
			listaAutorizaciones	= consultarExisteAutorizacionCapitaOrdenAmbulatoria(dtoAutorizCapitaOrdenAmbu);
			
			//dtoAutorizCapitaOrdenAmbu = autorizacionCapitacionOrdenesAmbulatoriasMundo.existeAutorizacionOrdenAmbul(dtoAutorizCapitaOrdenAmbu);
			/*dtoAutorizCapitaOrdenAmbu = new DtoAutorizacionCapitacionOrdenAmbulatoria();
			dtoAutorizCapitaOrdenAmbu.setCentroAtencionCorresponde(false);*/
			
			
			if(!Utilidades.isEmpty(listaAutorizaciones))
			{
				for(DtoAutorizacionCapitacionOrdenAmbulatoria listaAutoriz : listaAutorizaciones)
				{
				/*if( !dtoAutorizCapitaOrdenAmbu.isCentroAtencionCorresponde())
				{//Si el centro de costo de la autorizacion no corresponde con el de la cuenta del paciente
					solicitarForm.setBotonGenerarSolicitud(false);
					solicitarForm.setExisteAutorizacionOrden(true);
					errores.add("centroCostoNoCorres", new ActionMessage("errors.notEspecific",messageResource.getMessage("solicitarForm.centroCostoNoCorresponde")));
					saveErrors(request, errores);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");
				}else*/
					//{	
					//Validacion del centro de costo de la autorizacion se postula 
						listaAutoriz.setCentroAtencionAutoriz(listaAutoriz.getCodigoCentroAtencionAutoriz()+" "+listaAutoriz.getCentroAtencionAutoriz());
						listaAutoriz.setCentrosCostoSolicitadoAutoriz(listaAutoriz.getCodigoCentrosCostoSolicitadoAutoriz()+" "+listaAutoriz.getCentrosCostoSolicitadoAutoriz());
					solicitarForm.setExisteAutorizacionOrden(true);
					solicitarForm.setBotonGenerarSolicitud(true);
					
					/*dtoAutorizCapitaOrdenAmbu.setCentrosCostoSolicitadoAutoriz(1049+" "+"Centro Costo jiji");//--1049=jr
					dtoAutorizCapitaOrdenAmbu.setCentroAtencionAutoriz(01+" "+"Centro Atencion jaja");//--1=CLINICA VERSALLES PRINCIPAL.*/
					
						String centroCostoCentroAtencion=listaAutoriz.getCentrosCostoSolicitadoAutoriz()+" ("+listaAutoriz.getCentroAtencionAutoriz()+")";
					
					DtoCheckBox dtoCentroCosto=new DtoCheckBox();
						dtoCentroCosto.setCodigo(listaAutoriz.getCodigoCentrosCostoSolicitadoAutoriz().toString());
					dtoCentroCosto.setNombre(centroCostoCentroAtencion);
					solicitarForm.setCentroCostoAutorizacion(dtoCentroCosto);
					//}
				}
			}else{//Si no existe autorizacion debe cargar los centro de costo de la orden como estaba y mostrar el boton generar solicitud 
				solicitarForm.setBotonGenerarSolicitud(true);
			}
			//------------------------------------------------------------------------------------------------------------
			
			UtilidadBD.closeConnection(con);
			return mapping.findForward("principal");
		}
		else
		{
			return ComunAction.accionSalirCasoError(mapping, request, con, logger, "No se encontro la Orden Ambulatoria "+solicitarForm.getOrdenAmbulatoria(), "No se encontro la Orden Ambulatoria "+solicitarForm.getOrdenAmbulatoria(), false);
		}
	}

	/**
	 * @param mapping
	 * @param solicitarForm
	 * @return
	 */
	private ActionForward accionResumen(Connection con, ActionMapping mapping)
	{
		
			//solicitarForm.setEstado("resumen");
			UtilidadBD.closeConnection(con);
		
		return mapping.findForward("resumenSolicitudProcedimiento");
	}

	/**
	* Este mï¿½todo especifica las acciones a realizar en el estado
	* empezar, se ayuda de mï¿½todos auxiliares para cada tipo
	* de Servicio
	*
	* @param mapping Mapping para manejar la navegaciï¿½n
	* @param con Conexiï¿½n con la fuente de datos
	* @param solicitarForm Form para pre llenar datos, si es
	* necesario
	* @return
	* @throws SQLException
	*/
	private ActionForward accionEmpezar(ActionMapping mapping, Connection con, SolicitarForm solicitarForm, PersonaBasica paciente, UsuarioBasico usuario) throws SQLException
	{
		logger.info("!!!AQUI!!!");
		//Limpiamos lo que venga del form
		solicitarForm.reset();
		inicializarSolicitud(solicitarForm,usuario,paciente,con);
		UtilidadBD.closeConnection(con);
		return mapping.findForward("inicial");
	}
	
	
	/**
	 * Metodo que iniciliza el form para una insercion de la solicitud
	 * @param solicitarForm
	 * @param usuario
	 * @param con
	 * @param paciente
	 */
	private void inicializarSolicitud(SolicitarForm solicitarForm, UsuarioBasico usuario, PersonaBasica paciente, Connection con)
	{
		try
		{
			solicitarForm.setCentroCostoSolicitante(Utilidades.obtenerNombreCentroCosto(con,paciente.getCodigoArea(),usuario.getCodigoInstitucionInt()));
			solicitarForm.setCentroCostoSolicitado(ConstantesBD.codigoCentroCostoNoSeleccionado);
			solicitarForm.setOcupacionSolicitada(Integer.parseInt(ValoresPorDefecto.getOcupacionSolicitada(usuario.getCodigoInstitucionInt())));
			solicitarForm.setFechaHoraValoracionInicial(UtilidadValidacion.obtenerFechaYHoraPrimeraValoracion(con,paciente.getCodigoCuenta()));
		}
		catch(SQLException e)
		{
			logger.info("error al cargar la fecha de la valoraciï¿½n inicial "+e);
		}
	}
	
	/**
	 * 
	 * @param request
	 * @param mapping
	 * @param solicitarForm
	 * @param paciente
	 * @param medico
	 * @param con
	 * @return
	 * @throws Exception
	 */
	
	private ActionForward accionInsertar(HttpServletRequest request, ActionMapping mapping, SolicitarForm solicitarForm, PersonaBasica paciente, UsuarioBasico medico, Connection con) throws Exception
	{
		
		//VALIDAR PARAMETRO GENERAL REGISTRO EVOLUCIONES PARA ORDENES -*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-
		String  parametroEvoluciones = Solicitud.consultarParametroEvoluciones(con); 
		boolean validacionViaIngreso=false;
		
		//Inicializa los valores pasados por parametros
		if(request.getParameter("codigoServicioPostular")!=null)
			solicitarForm.setCodigoServicioPostular(request.getParameter("codigoServicioPostular").toString());
		else
			solicitarForm.setCodigoServicioPostular(ConstantesBD.codigoNuncaValido+"");
		
		if(parametroEvoluciones.equals(ConstantesBD.acronimoSi))
		{
			logger.info("<<<<<<< PARAMETRO EVOLUCIONES ->>>>>>"+parametroEvoluciones+" | ESTA ACTIVADA | ");
			
			logger.info("FECHAAAAAAAA->"+UtilidadFecha.conversionFormatoFechaABD(solicitarForm.getFechaSolicitud())+"<-");
			
			int codigoCuenta = paciente.getCodigoCuenta();
			
			if(paciente.getCodigoUltimaViaIngreso()==ConstantesBD.codigoViaIngresoHospitalizacion)
			{
				logger.info("ES VIA INGRESO HOSPITALIZACION");
				validacionViaIngreso=true;
			}
			else
				if(paciente.getCodigoUltimaViaIngreso()==ConstantesBD.codigoViaIngresoUrgencias && 
						UtilidadValidacion.tieneValoracionConConductaCamaObservacion(con, codigoCuenta))
				{
					logger.info("ES VIA INGRESO URGENCIAS EN OBSERVACION");
					validacionViaIngreso=true;
				}
			
			// PREGUNTO SI PASA EL FILTRO DE LA VIA INGRESO
			if(validacionViaIngreso == true)
			{
				String fechaValoracionInicial = UtilidadesOrdenesMedicas.consultarFechaValoracionInicial(con, paciente.getCodigoCuenta());
				logger.info(".......... FECHA VALORACION INICIAL ----->"+fechaValoracionInicial);
				logger.info(".......... FECHA SOLICITUD--------- ----->"+UtilidadFecha.conversionFormatoFechaABD(solicitarForm.getFechaSolicitud()));
				if(! fechaValoracionInicial.equals(UtilidadFecha.conversionFormatoFechaABD(solicitarForm.getFechaSolicitud())))
				{
					// PERMITO GENERAR LA SOLICITUD SIN VALIDAR AUE EXISTAN LAS EVOLUCIONES
					boolean evoluciones = UtilidadValidacion.tieneEvolucionesParaElDia(con, paciente.getCodigoCuenta(), UtilidadFecha.conversionFormatoFechaABD(solicitarForm.getFechaSolicitud()));
					
					if(evoluciones == false)
						return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Registrar Evolucion al Paciente antes de Continuar", "error.solicitudgeneral.validarEvolucion", true);
				}
			}
		}
		
		//-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-
		
		
		
		char codigoSolicitud=solicitarForm.getCodigoTipoSolicitud();

		if(solicitarForm.isSolPYP())
		{
			int finalidad = solicitarForm.getFinalidad();
			String nombreFinalidad = solicitarForm.getNombreFinalidad();
			
			//inicializar la solicitud
			solicitarForm.resetCasoPYP();
			
			solicitarForm.setFinalidad(finalidad);
			solicitarForm.setNombreFinalidad(nombreFinalidad);
			
			inicializarSolicitud(solicitarForm,medico,paciente,con);
		}

		
		//Se toma el parámetro de HACER REQUERIDO COMENTARIOS AL SOLICITAR
		solicitarForm.setRequeridoComentarios(UtilidadTexto.getBoolean(ValoresPorDefecto.getRequeridoComentariosSolicitar(medico.getCodigoInstitucionInt())));
		
		if(codigoSolicitud==ConstantesBD.codigoServicioInterconsulta)
		{
			return this.accionInsertarInterconsulta(request, mapping, solicitarForm, paciente, medico, con);
		}
		else if(codigoSolicitud==ConstantesBD.codigoServicioProcedimiento)
		{
			return this.accionInsertarProcedimientos(mapping, solicitarForm, request, paciente, medico, con);
		}
		else
		{
		    return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Tipo de Solicitud no implementada", "Tipo de Solicitud no implementada", false);
		}
		
		
	}
	
	/**
	 * Mï¿½todo que permite insertar una interconsulta validando la vï¿½a de ingreso del paciente,
	 * si es diferente a Consulta Externa y Ambulatorios valï¿½da el mï¿½dico tratante
	 * @param request
	 * @param mapping
	 * @param solicitarForm
	 * @param paciente
	 * @param medico
	 * @param con
	 * @return
	 * @throws Exception
	 */
	private ActionForward accionInsertarInterconsulta(HttpServletRequest request, ActionMapping mapping, SolicitarForm solicitarForm, PersonaBasica paciente, UsuarioBasico medico, Connection con) throws Exception
	{
			/**Si la via de ingreso es diferente a Consulta Externa y Ambulatorios validamos el medico tratante**/
			//if(!UtilidadValidacion.esViaIngresoConsultaExterna(con, paciente.getCodigoCuenta())&&paciente.getCodigoUltimaViaIngreso()!=ConstantesBD.codigoViaIngresoAmbulatorios)
		
			//	tarea 158457 -- solo hace la validaciones de adjuntos y de tratantes si el ingreso no es odontologico.
		try{	
			HibernateUtil.beginTransaction();
			IngresosHome ingresoDao=new IngresosHome();
			Ingresos ingreso=ingresoDao.findById(paciente.getCodigoIngreso());
			if(!UtilidadTexto.isEmpty(ingreso.getTipoIngreso()) && !ingreso.getTipoIngreso().equals(ConstantesIntegridadDominio.acronimoTipoIngresoOdontologico))
			{
				String mensaje = UtilidadValidacion.esMedicoTratante(con, medico, paciente);
				boolean esAdjunto= UtilidadValidacion.esAdjuntoCuenta(con,paciente.getCodigoCuenta(),medico.getLoginUsuario());
				
				if(mensaje.equals("") || esAdjunto)
				{
					//de lo contrario sigue derecho
					solicitarForm.setNumeroServiciosMaximo(1);
					UtilidadBD.closeConnection(con);
					HibernateUtil.endTransaction();
					return mapping.findForward("principal");
				}
				if(!mensaje.equals(""))
				{	
					//logger.info("2");
					solicitarForm.reset();
					HibernateUtil.endTransaction();
					if(mensaje.equals("errors.noOcupacionMedica"))
						return ComunAction.accionSalirCasoError(mapping, request, con, logger, "No hay ocupaciï¿½n mï¿½dica definida", mensaje, true);
					else
						return ComunAction.accionSalirCasoError(mapping, request, con, logger, "No es mï¿½dico Tratante", "error.valoracion.medicoNoGrupoTratante", true);
				}
				if(!esAdjunto){
					HibernateUtil.endTransaction();
					return ComunAction.accionSalirCasoError(mapping,request,con, logger, "El mï¿½dico no es adjunto", "error.validacionessolicitud.medicoNoTratanteNiAdjunto", true);
				}
			}
			else
			{
				solicitarForm.setNumeroServiciosMaximo(1);
				UtilidadBD.cerrarConexion(con);
				HibernateUtil.endTransaction();
				return mapping.findForward("principal");
			}
			HibernateUtil.endTransaction();
		}catch(Exception e){
			Log4JManager.error("ERROR accionInsertarInterconsulta", e);
			HibernateUtil.abortTransaction();
		}
		return null;
	}
	
	/**private ActionForward accionInsertarInterconsulta(HttpServletRequest request, ActionMapping mapping, SolicitarForm solicitarForm, PersonaBasica paciente, UsuarioBasico medico, Connection con) throws Exception
	{
		if (UtilidadValidacion.esMedicoTratante(con, medico, paciente))
		{
			//La ï¿½ltima validaciï¿½n a tener en cuenta es que en
			//Consulta Externa NO se puede solicitar interconsulta
			if (UtilidadValidacion.esViaIngresoConsultaExterna(con, paciente.getCodigoCuenta()))
			{
				solicitarForm.reset();
			    return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Consulta Externa NO se puede solicitar interconsulta", "error.solicitudgeneral.interconsulta.consultaExterna", true);
			}

			solicitarForm.setNumeroServiciosMaximo(1);
			UtilidadBD.cerrarConexion(con);
			return mapping.findForward("principal");
		}
		else
		{
			solicitarForm.reset();
			return ComunAction.accionSalirCasoError(mapping, request, con, logger, "No es mï¿½dico Tratante", "error.solicitudgeneral.interconsulta.consultaExterna", true);
		}
	}**/

	private ActionForward accionInsertarProcedimientos(
		ActionMapping		aam_mapping,
		SolicitarForm		asf_form,
		HttpServletRequest	ahsr_request,
		PersonaBasica		apb_paciente,
		UsuarioBasico		aub_medico,
		Connection			ac_con
	)throws Exception
	{
		boolean lb_permisos;
		
		lb_permisos =
			UtilidadValidacion.esMedicoTratante(ac_con, aub_medico, apb_paciente).equals("") ||
			UtilidadValidacion.esAdjuntoCuenta(
				ac_con, apb_paciente.getCodigoCuenta(), aub_medico.getLoginUsuario()
			)
			//ya no aplica,las validaciones son iguales para todos los centros de costo.
			//||
			//apb_paciente.getCodigoUltimaViaIngreso()==ConstantesBD.codigoViaIngresoAmbulatorios
			//||
			//apb_paciente.getCodigoUltimaViaIngreso()==ConstantesBD.codigoViaIngresoConsultaExterna
			;

		
		//si es vï¿½a de ingreso Ambulatorios o Consulta Externa, se postula el
		//centro de costo correspondiente a la vï¿½a de Ingreso
		/*
		if(apb_paciente.getCodigoUltimaViaIngreso()==ConstantesBD.codigoViaIngresoAmbulatorios)
			asf_form.setCentroCostoSolicitante(UtilidadValidacion.getNombreCentroCosto(ac_con,Integer.parseInt(ValoresPorDefecto.getCentroCostoAmbulatorios(aub_medico.getCodigoInstitucionInt()))));
		else if(apb_paciente.getCodigoUltimaViaIngreso()==ConstantesBD.codigoViaIngresoConsultaExterna)
			asf_form.setCentroCostoSolicitante(UtilidadValidacion.getNombreCentroCosto(ac_con,Integer.parseInt(ValoresPorDefecto.getCentroCostoConsultaExterna(aub_medico.getCodigoInstitucionInt()))));
		*/
		asf_form.setCentroCostoSolicitante(Utilidades.obtenerNombreCentroCosto(ac_con,apb_paciente.getCodigoArea(),aub_medico.getCodigoInstitucionInt()));
		
		/*
		 * Esta validaciï¿½n ya no aplica
		 *
		if (UtilidadValidacion.esViaIngresoConsultaExterna(ac_con, apb_paciente.getCodigoCuenta()))
		{
			if(ValidacionesSolicitud.tieneCitasAtendidas(ac_con,apb_paciente.getCodigoCuenta(),aub_medico.getCodigoPersona()))
				lb_permisos=true;
			else
			{
				lb_permisos=false;
				asf_form.reset();
				UtilidadBD.cerrarConexion(ac_con);
				return ComunAction.accionSalirCasoError(aam_mapping, ahsr_request, ac_con, logger, "El mï¿½dico no atendio al paciente", "error.cita.noRespondida", true);

			}
				
		}*/
		if(apb_paciente.getCodigoCuenta()==0)
		{
			asf_form.setUnicamenteExternos(true);
			asf_form.setCentroCostoSolicitante(aub_medico.getCentroCosto());
		}
		if(lb_permisos || asf_form.getUnicamenteExternos() || ahsr_request.getParameter("accionPYP")!=null)
		{
			asf_form.setNumeroServiciosMaximo(100);
			UtilidadBD.closeConnection(ac_con);
			return aam_mapping.findForward("principal");
		}
		else
		{
			asf_form.reset();
			String mensaje = UtilidadValidacion.esMedicoTratante(ac_con, aub_medico, apb_paciente);
			if(mensaje.equals("errors.noOcupacionMedica"))
				return ComunAction.accionSalirCasoError(aam_mapping, ahsr_request, ac_con, logger, "No se ha definido ocupaciï¿½n mï¿½dica", mensaje, true);
			else
				return ComunAction.accionSalirCasoError(aam_mapping, ahsr_request, ac_con, logger, "El mï¿½dico no es tratante ni adjunto", "error.validacionessolicitud.medicoNoTratanteNiAdjunto", true);
		}
	}

	/**
	* Conjunto de acciones a realizar cuando el usuario quiere salir
	* de esta funcionalidad
	 * @param response 
	*
	* @param request Request de http
	* @param mapping Mapping manejo recursos / direcciones
	* @param solicitarForm Forma con los datos llenados
	* @param paciente Paciente al que se le estï¿½ creando la solicitud
	* @param medico Mï¿½dico que estï¿½ trabajando en esta solicitud
	* @param con Conexiï¿½n con la fuente de datos
	* @return
	* @throws SQLException
	*/
	@SuppressWarnings("unused")
	private ActionForward accionSalir(HttpServletResponse response, HttpServletRequest request, ActionMapping mapping, SolicitarForm solicitarForm, PersonaBasica paciente, UsuarioBasico medico, Connection con) throws Exception
	{
		
//      VALIDAR PARAMETRO GENERAL REGISTRO EVOLUCIONES PARA ORDENES -*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-
		Solicitud solicitud = new Solicitud();
		String  parametroEvoluciones = Solicitud.consultarParametroEvoluciones(con); 
		boolean validacionViaIngreso=false;
		
		
		if(parametroEvoluciones.equals(ConstantesBD.acronimoSi))
		{
			logger.info("<<<<<<< PARAMETRO EVOLUCIONES ->>>>>>"+parametroEvoluciones+" | ESTA ACTIVADA | ");
			
			logger.info("FECHAAAAAAAA->"+UtilidadFecha.conversionFormatoFechaABD(solicitarForm.getFechaSolicitud())+"<-");
			
			int codigoCuenta = paciente.getCodigoCuenta();
			
			if(paciente.getCodigoUltimaViaIngreso()==ConstantesBD.codigoViaIngresoHospitalizacion)
			{
				logger.info("ES VIA INGRESO HOSPITALIZACION");
				validacionViaIngreso=true;
			}
			else
				if(paciente.getCodigoUltimaViaIngreso()==ConstantesBD.codigoViaIngresoUrgencias && 
						UtilidadValidacion.tieneValoracionConConductaCamaObservacion(con, codigoCuenta))
				{
					logger.info("ES VIA INGRESO URGENCIAS EN OBSERVACION");
					validacionViaIngreso=true;
				}
			
			// PREGUNTO SI PASA EL FILTRO DE LA VIA INGRESO
			if(validacionViaIngreso == true)
			{
				String fechaValoracionInicial = UtilidadesOrdenesMedicas.consultarFechaValoracionInicial(con, paciente.getCodigoCuenta());
				logger.info(".......... FECHA VALORACION INICIAL ----->"+fechaValoracionInicial);
				logger.info(".......... FECHA SOLICITUD--------- ----->"+UtilidadFecha.conversionFormatoFechaABD(solicitarForm.getFechaSolicitud()));
				if(! fechaValoracionInicial.equals(UtilidadFecha.conversionFormatoFechaABD(solicitarForm.getFechaSolicitud())))
				{
					// PERMITO GENERAR LA SOLICITUD SIN VALIDAR AUE EXISTAN LAS EVOLUCIONES
					boolean evoluciones = UtilidadValidacion.tieneEvolucionesParaElDia(con, paciente.getCodigoCuenta(), UtilidadFecha.conversionFormatoFechaABD(solicitarForm.getFechaSolicitud()));
					
					if(evoluciones == false)
						return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Registrar Evolucion al Paciente antes de Continuar", "error.solicitudgeneral.validarEvolucion", true);
				}
			}
		}
			
			//-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-
	        	
		
		
		char codigoSolicitud=solicitarForm.getCodigoTipoSolicitud();
				
		if(codigoSolicitud==ConstantesBD.codigoServicioInterconsulta)
		{				
			return this.accionSalirInterconsulta(request, mapping, solicitarForm, paciente, medico, con);
		}
		else if(codigoSolicitud==ConstantesBD.codigoServicioProcedimiento)
		{
			return this.accionSalirProcedimiento(response,request, mapping, solicitarForm, paciente, medico, con);
		}
		else
		{
		}
		return mapping.findForward("principal");
	}

	/**
	* Este mï¿½todo define el conjunto de acciones a realizar al
	* momento de salir de la funcionalidad de Solicitar para
	* interconsultas
	*
	* @param request Request de http
	* @param mapping Mapping manejo recursos / direcciones
	* @param solicitarForm Forma con los datos llenados
	* @param paciente Paciente al que se le estï¿½ creando la solicitud
	* @param medico Mï¿½dico que estï¿½ trabajando en esta solicitud
	* @param con Conexiï¿½n con la fuente de datos
	* @return
	* @throws SQLException
	*/
	private ActionForward accionSalirInterconsulta (HttpServletRequest request, ActionMapping mapping, SolicitarForm solicitarForm, PersonaBasica paciente, UsuarioBasico medico, Connection con) throws SQLException
	{
		HttpSession session=request.getSession();
		UtilidadBD.closeConnection(con);
		Solicitud solicitudGeneral=this.llenarSolicitud(solicitarForm, paciente, medico);

		session.setAttribute("solicitudGeneral", solicitudGeneral);
		session.setAttribute("comentario", solicitarForm.getComentario());
		
		session.setAttribute("valorJustificacion", solicitarForm.getValores("justificar_0"));
		
		if (solicitarForm.getSeleccionarServicio().equals("buscado"))
		{
			session.setAttribute("esSolicitudOtros", "false");
			session.setAttribute("parametroSolicitud", solicitarForm.getValores("codigo_0"));
			session.setAttribute("nombreProcedimiento",solicitarForm.getValores("nombre_0"));
			
		}
		else
		{
			session.setAttribute("esSolicitudOtros", "true");
			session.setAttribute("parametroSolicitud", solicitarForm.getOtroServicio());
		}

		session.setAttribute("accionPYP", solicitarForm.getAccionPYP());
		session.setAttribute("pyp", solicitarForm.isSolPYP());
		session.setAttribute("generaAlertaEnfermeria", solicitarForm.getGeneraAlertaEnfermeria());
		
		
		return mapping.findForward("guardarInterconsulta");
	}

	/**
	* Este método define el conjunto de acciones a realizar al
	* momento de salir de la funcionalidad de Solicitar para
	* procedimientos
	 * @param response 
	*
	* @param request Request de http
	* @param mapping Mapping manejo recursos / direcciones
	* @param solicitarForm Forma con los datos llenados
	* @param paciente Paciente al que se le está creando la solicitud
	* @param usuario Médico que está trabajando en esta solicitud
	* @param con Conexión con la fuente de datos
	* @return
	* @throws SQLException
	*/
	@SuppressWarnings({ "rawtypes", "unchecked", "deprecation"})
	private ActionForward accionSalirProcedimiento(HttpServletResponse response, HttpServletRequest	request, ActionMapping mapping,
			SolicitarForm forma, PersonaBasica paciente, UsuarioBasico	usuario, Connection	con) throws Exception
	{
		SolicitudProcedimiento	solicitud;
		boolean inserto=false;
		solicitud = new SolicitudProcedimiento();
		ActionErrors errores = new ActionErrors();
		//mapa para el manejo de la informacion de interfaz
		HashMap infoInterfaz = new HashMap();
		int conInterfaz = 0;
		int temp = 0;
		List<InfoResponsableCobertura> listaCoberturaServicio;
		
		//********************************NUEVO CÓDIGO SOLICITUD DE PROCEDIMIENTOS**********************************
		try
		{
			/* Obtener los datos bï¿½sico de la solicitud */
			PropertyUtils.copyProperties(solicitud, llenarSolicitud(forma, paciente, usuario));
			solicitud.setComentario(forma.getComentario());
			solicitud.setNombreOtros(forma.getOtroServicio() );
			int numeroDocumento=solicitud.numeroDocumentoSiguiente(con);
			listaCoberturaServicio=new ArrayList<InfoResponsableCobertura>();
			
			
			//*********************************************************************************************
			//****************EDICION ENCABEZADO INTERFAZ LABORATORIOS*************************************
			//**********************************************************************************************
			//Se llenan datos necesarios para interfaz laboratorios
			infoInterfaz.put("numeroDocumento",numeroDocumento+"");
			infoInterfaz.put("fechaSolicitud",solicitud.getFechaSolicitud());
			infoInterfaz.put("horaSolicitud",solicitud.getHoraSolicitud());
			infoInterfaz.put("codigoMedico",solicitud.getCodigoMedicoSolicitante()+"");
			infoInterfaz.put("nombreMedico",usuario.getNombreUsuario());
			infoInterfaz.put("institucion",usuario.getCodigoInstitucion());
			infoInterfaz.put("observaciones",solicitud.getComentario());
			Cama cama = new Cama();
			cama.cargarCama(con,paciente.getCodigoCama()+"");
			infoInterfaz.put("numeroCama",cama.getDescripcionCama());
			if(ValoresPorDefecto.getCliente().equals(ConstantesBD.clienteSHAIO))
			{
				//infoInterfaz.put("comentarioDiagnostico", UtilidadesHistoriaClinica.obtenerDescripcionDxIngresoPaciente(con, paciente.getCodigoCuenta(), paciente.getCodigoUltimaViaIngreso()));
				infoInterfaz.put("horaSistema",UtilidadFecha.getHoraSegundosActual(con));
				infoInterfaz.put("nitEmpresa",UtilidadesFacturacion.obtenerNitEmpresaConvenio(con, paciente.getCodigoConvenio()));
				infoInterfaz.put("nroCarnet",UtilidadesManejoPaciente.obtenerNroCarnetIngresoPaciente(con, paciente.getCodigoIngreso()));
				infoInterfaz.put("codigoEspecialidadSolicitante",solicitud.getEspecialidadSolicitante().getCodigo());
				//infoInterfaz.put("ciePrevio",Utilidades.consultarDiagnosticosPaciente(con, paciente.getCodigoCuenta()+"", paciente.getCodigoUltimaViaIngreso()));
				
			}
			//**********************************************************************************************
			///*********************************************************************************************
			//Inicio de transaccion
			UtilidadBD.iniciarTransaccion(con);
			
			
			// Objeto formato de justificaciï¿½n de servicios No POS
	        FormatoJustServNopos fjsn = new FormatoJustServNopos();
	        Boolean yaSeActualizoUnaJustificacionNoPos = new Boolean(false);
			
	        //Se pasan las cantidades de los servicios a llave temporal
	        for(int	i=0, servicios=forma.getNumeroServicios();i<servicios;i++)
			{
	        	forma.setValores("cantidadInicial_"+i, forma.getValores("cantidad_"+i));
			}
	        
			/* El(los) servicio(s) de la solicitud si estï¿½(n) parametrizado(s) */
			for(int	i=0, servicios=forma.getNumeroServicios();i<servicios;i++)
			{
				logger.info("valor cantidad ["+i+"] "+forma.getValores("cantidad_"+i));
				//********MIENTRAS QUE LA CANTIDAD DEL SERVICIO SEA > 0 Y NO ESTÉ PENDIENTE DE JUSTIFICAR**********************
				if (!forma.getValores("justificar_"+i).toString().equals("pendiente")
					&&
					Integer.parseInt((String)forma.getValores("cantidad_" + i))>0)
				{
				
					//Cambio version 1.50 Anexo Solicitud de Procedimientos 40
					//Si tiene indicativo de Orden Ambulatoria, consulto el diagnóstico y se lo entrego
					//a la solicitud
					//Diana Carolina G
					if(forma.isIndicativoOrdenAmbulatoria()){
						String codigoOA=Utilidades.obtenerCodigoOrdenAmbulatoria(con,forma.getOrdenAmbulatoria(),usuario.getCodigoInstitucionInt());
						int codigoOrdenAmbulatoria = Integer.parseInt(codigoOA);
						DtoDiagnostico dtoDiagnostico = OrdenesAmbulatorias.consultarDiagnosticoOrdenAmbulatoria(con, codigoOrdenAmbulatoria);
						
						if(!UtilidadTexto.isEmpty(dtoDiagnostico.getAcronimoDiagnostico())&&
								(!UtilidadTexto.isEmpty(dtoDiagnostico.getTipoCieDiagnostico()))){
							forma.setDtoDiagnostico(dtoDiagnostico);
						}else{
							forma.setDtoDiagnostico(null);
						}
					}
					
					//**************************SE CARGA INFORMACION DE CADA SERVICIO********************************
					llenarMundoServicioProcedimiento(con,forma,i,solicitud, paciente, usuario.getCodigoInstitucionInt());
					//*********************************************************************************
					//*****************GENERACION DE LA SOLICITUD Y CARGO*********************************
					if(solicitud.getCodigoTipoServicio().trim().equals(ConstantesBD.codigoServicioNoCruentos+""))
					{
						//*********************GENERACION SOLICITUD TIPO CIRUGIA***********************************************
						temp = crearSolicitudCirugia(con,usuario,forma,solicitud,paciente,ConstantesBD.continuarTransaccion,
								listaCoberturaServicio);
						inserto = temp>0?true:false;
						forma.setEscruento(true);// los servicios no curentos no generan a autorizacion de esta manera, y mandan error al ejectuirar este metodo
					//*********************************************************************************************
					}
					else
					{
						solicitud.setTipoSolicitud(new InfoDatosInt(ConstantesBD.codigoTipoSolicitudProcedimiento));
						//*********************GENERACION SOLICITUD TIPO PROCEDIMIENTO****************************************
						inserto = crearSolicitudProcedimiento(con,usuario,forma,solicitud,paciente,ConstantesBD.continuarTransaccion,numeroDocumento,
								listaCoberturaServicio);
						//******************************************************************************************************
					    
					}
					if(inserto)
					{
						//********INICIO INSERTAMOS LAS SOLICITUDES DE SERVICIOS EN EL MAPA**********
						forma.setNumerosSolicitudes("numeroSolicitud_"+forma.getNumerosSolicitudes("numRegistros"), solicitud.getNumeroSolicitud());
						forma.setNumerosSolicitudes("numRegistros", (Utilidades.convertirAEntero(forma.getNumerosSolicitudes("numRegistros")+"",true) +1));
						//**********FIN INSERTAMOS LAS SOLICITUDES DE SERVICIOS EN EL MAPA***********
						
						forma.setNumeroSolicitud(solicitud.getNumeroSolicitud());
						
						forma.setIidi_estadoHistoriaClinica(solicitud.getEstadoHistoriaClinica());
						if(!forma.getOrdenAmbulatoria().trim().equals(""))
						{
							String codigoOrden=Utilidades.obtenerCodigoOrdenAmbulatoria(con,forma.getOrdenAmbulatoria(),usuario.getCodigoInstitucionInt());
							HashMap vo=new HashMap();
							vo.put("estadoOrden",ConstantesBD.codigoEstadoOrdenAmbulatoriaSolicitada+"");
							vo.put("numeroSolicitud",solicitud.getNumeroSolicitud()+"");
							vo.put("numeroOrden",codigoOrden+"");
							vo.put("usuario",usuario.getLoginUsuario());
							OrdenesAmbulatorias.confirmarOrdenAmbulatoria(con,vo);
							if(forma.isSolPYP())
							{
								OrdenesAmbulatorias.actualizarEstadoActividadProgramaPYPPAcienteNumOrdenEstatico(con,codigoOrden,ConstantesBD.codigoEstadoProgramaPYPSolicitado,usuario.getLoginUsuario(),"");
								Utilidades.asignarSolicitudToActividadPYP(con,codigoOrden,solicitud.getNumeroSolicitud()+"");
							}
							
							logger.info("INFO DEL CARGO  CODIGO " +forma.getCodigoDetalleCargo()+"  SUbcuenta"+ forma.getCodigoSubCuentaCargo());
							if(Autorizaciones.actualizarAutorizacionyDetalle(con, codigoOrden, paciente.getCodigoCuenta()+"", forma.getCodigoConvenioCargo()+"", solicitud.getNumeroSolicitud()+"", forma.getCodigoDetalleCargo()+"", forma.getCodigoSubCuentaCargo()+"","")!=1)
							{
								errores.add("",new ActionMessage("errors.notEspecific","Problemas Actulizando los Datos de Autorizacion "));
							}
						}
						
						//*************** JUSTIFICACIÓN NO POS ***************************************************
						inserto = registrarJustificacionNoPosProcedimientos(request,con,forma,i,usuario,solicitud,fjsn,yaSeActualizoUnaJustificacionNoPos);
						//************** FIN JUSTIFICACIÓN NO POS *********************************************************
						
						if(!inserto)
						{
							errores.add("",new ActionMessage("errors.notEspecific","Problemas registrando la justificacion no pos del servicio: "+solicitud.getNombreServicioSolicitado()));
						}
						else
						{
							///*****************************************************************************************
							//**********EDICION DETALLE INFORMACION DE INTERFAZ DE LABORATORIOS***********************
							//La interfaz de laboratorios solo aplica para servicios que son distintos a no cruentos
							if(!solicitud.getCodigoTipoServicio().trim().equals(ConstantesBD.codigoServicioNoCruentos+""))
							{
								//Edicion de informaciï¿½n de Interfaz Laboratorio
								infoInterfaz.put("numeroSolicitud_"+conInterfaz,solicitud.getNumeroSolicitud()+"");
								
								infoInterfaz.put("estado_"+conInterfaz,ConstantesBD.codigoEstadoHCSolicitada+"");
								infoInterfaz.put("centroCosto_"+conInterfaz,solicitud.getCentroCostoSolicitado().getCodigo()+"");
								infoInterfaz.put("urgente_"+conInterfaz,solicitud.getUrgente()+"");
								infoInterfaz.put("codigoCUPS_"+conInterfaz,Utilidades.obtenerCodigoPropietarioServicio(con,solicitud.getCodigoServicioSolicitado()+"",ConstantesBD.codigoTarifarioCups));
								
								/*if(ValoresPorDefecto.getCliente().equals(ConstantesBD.clienteSHAIO))
								{
									infoInterfaz.put("codigoLaboratorio_"+conInterfaz,UtilidadLaboratorios.obtenerCodigoLaboratorioServicio(con, solicitud.getCodigoServicioSolicitado()));
									HashMap informacionServ = Utilidades.consultarInformacionServicio(con, solicitud.getCodigoServicioSolicitado()+"", usuario.getCodigoInstitucionInt());
									infoInterfaz.put("codigoCUPS_"+conInterfaz,informacionServ.get("codigopropietario")+"");
									infoInterfaz.put("nombreServicio_"+conInterfaz,informacionServ.get("nombre")+"");
								}
								else
								{
									infoInterfaz.put("codigoCUPS_"+conInterfaz,Utilidades.obtenerCodigoPropietarioServicio(con,solicitud.getCodigoServicioSolicitado()+"",ConstantesBD.codigoTarifarioCups));
								}*/
								conInterfaz ++;
							}
							//**********************************************************************************************
							
							/***************************************SERVICIOS INCLUIDOS*************************************************/
							inserto= crearSolicitudesServiciosIncluidos(con, usuario, forma, solicitud, paciente, ConstantesBD.continuarTransaccion);
							if(!inserto)
							{
								errores.add("",new ActionMessage("errors.notEspecific","Error al generar las solicitud de procedimientos incluidos del servicio: "+solicitud.getNombreServicioSolicitado()));
							}
							/**********************************************************************************************************/
						}
					}
					else
					{
						errores.add("",new ActionMessage("errors.notEspecific","Problemas generando la orden del servicio: "+solicitud.getNombreServicioSolicitado()));
					}
					
					//Se disminuye la cantidad del servicio
					forma.setValores("cantidad_"+i, (Utilidades.convertirAEntero(forma.getValores("cantidad_"+i)+"",true)-1)+"");
					
				} //Fin if validacion cantidad
				
				
				//Si yá se llegó al último servicio se genera el archivo de la orden
				if(i+1==servicios)
				{
					//****************LLAMADO A INTERFAZ LABORATORIOS*****************************************
					infoInterfaz.put("numRegistros",conInterfaz+"");
					errores = InterfazLaboratorios.generarRegistroArchivo(infoInterfaz,paciente,ValoresPorDefecto.getCliente(),errores);
					//************************************************************************************
					
					
					//Si todavía existen procedimientos pendientes por generar solicitud se genera nueva orden
					if(forma.existenServiciosCantidadesPendientes())
					{
						i = -1;
						//Se genera nuevo documento
						numeroDocumento=solicitud.numeroDocumentoSiguiente(con);
						//Se asigna el nuevo número de orden al archivo de interfaz de laboratorios
						infoInterfaz.put("numeroDocumento",numeroDocumento+"");
						//Se reinicia el número de estudios
						conInterfaz = 0;
					}
				}
				
			} //Fin For servicios
			//Se adiciona a la lista los servicio evaluados con cobertura
			forma.setInfoCoberturaServicio(listaCoberturaServicio);
			
			
			//Se recuperan las cantidades de la orden
	        for(int	i=0, servicios=forma.getNumeroServicios();i<servicios;i++)
			{
	        	forma.setValores("cantidad_"+i, forma.getValores("cantidadInicial_"+i));
			}
			
			if(!errores.isEmpty())
			{
				UtilidadBD.abortarTransaccion(con);
				saveErrors(request, errores);
				request.getSession().removeAttribute("MAPAJUSSERV");
				return mapping.findForward("paginaErroresActionErrors");
			}
			
			
			UtilidadBD.finalizarTransaccion(con);
			request.getSession().removeAttribute("MAPAJUSSERV");
			
			/**
             * Se inserta el registro de alerta para registro de enfermería MT-3438
             */
			if(UtilidadValidacion.esMedico(usuario).equals("") && 
					(!UtilidadTexto.isEmpty(forma.getGeneraAlertaEnfermeria()) && 
					forma.getGeneraAlertaEnfermeria().equals(ConstantesBD.acronimoSi) || forma.isSolPYP())) 
			{
				int tipoSolicitud = ConstantesBD.codigoNuncaValido;
				if(solicitud.getCodigoTipoServicio().trim().equals(ConstantesBD.codigoServicioNoCruentos+"")) {
					tipoSolicitud = ConstantesBD.seccionCirugias;
				} else {
					tipoSolicitud = ConstantesBD.seccionProcedimientos;
				}
				OrdenMedica ordenMedica = new OrdenMedica();
				ordenMedica.registrarAlertaOrdenMedica(con,	tipoSolicitud, 
					new Long(paciente.getCodigoCuenta()), usuario.getLoginUsuario());
			}
			forma.setGeneraAlertaEnfermeria(ConstantesBD.acronimoNo);
		//*************************************************************************************************************
		
			//Se captura la excepcion para no bloquear el flujo
			try {
				//VALIDACIONES PARA GENERACION DE AUTORIZACION O ASOCIACION CON LA/LAS NUEVAS SOLICITUDES
				cargarInfoVerificarGeneracionAutorizacion(con, forma, usuario, paciente,errores);
				saveErrors(request, errores);
			}  catch (IPSException e) {
				Log4JManager.error(e);
				ActionMessages mensajeError = new ActionMessages();
				mensajeError.add("", new ActionMessage(String.valueOf(e.getErrorCode())));
				saveErrors(request, mensajeError);
		}
		
		/**DCU 40->Cambio 1.52****************************************/
		if(forma.isIndicativoOrdenAmbulatoria())
		{	// Se cambia solo en la presentacion la validacion de cambio en la descripcion
			if(!Utilidades.isEmpty(forma.getListaServiciosImpimirOrden()))
			{
				for(int k=0;k<forma.getNumeroServicios();k++)
				{
					forma.setValores("nombre_"+k, forma.getListaServiciosImpimirOrden().get(k));
				}
			}
		}
		/************************************************************/
		UtilidadBD.closeConnection(con);

		/* Actualizar los datos en el formulario para su posterior visulización */
		forma.setCentroCostoSolicitante(solicitud.getCentroCostoSolicitante().getNombre() );
		forma.setFechaGrabacion(solicitud.getFechaGrabacion() );
		forma.setHoraGrabacion(solicitud.getHoraGrabacion() );
		forma.setNombreCentroCostoSolicitado(solicitud.getCentroCostoSolicitado().getNombre() );
		forma.setNombreEspecialidadSolicitante(solicitud.getEspecialidadSolicitante().getNombre() );
		forma.setNombreOcupacionSolicitada(solicitud.getOcupacionSolicitado().getNombre() );
		forma.setConsecutivoOrdenesMedicas(solicitud.getConsecutivoOrdenesMedicas());
		
		forma.setConsecutivosOrdenesMedicas(Solicitud.obtenerConsecutivosOrdenesMedicas(forma.getNumerosSolicitudes()));

		
		if(forma.isSolPYP())
		{
				if(forma.getAccionPYP().equals("ejecutar")){
				String pagina="../solicitudes/listarSolicitudes.do?estado=resumen&tipoServicio=responder&numeroSolicitud="+solicitud.getNumeroSolicitud()+"&codigoPaciente="+paciente.getCodigoPersona()+"&estadoHistoriaClinicaResumen="+solicitud.getEstadoHistoriaClinica().getCodigo()+"&codigoTipoSolicitud="+solicitud.getTipoSolicitud().getCodigo();//&ind=0";
				response.sendRedirect(pagina);
				}else{
				//aqui podria enviar algun parametro por el request para que me cierre la pop-up
				forma.setEstado("resumenProcedimiento");
				return mapping.findForward("resumenSolicitudProcedimiento");
			}
			}else{
				//Se guardan los mensajes de error de los servicios que no se autorizaron
				if(!errores.isEmpty()){
					saveErrors(request, errores);
		}
			
			forma.setEstado("resumenProcedimiento");
			return mapping.findForward("resumenSolicitudProcedimiento");
		}
		
		} catch(SQLException e) {
			Log4JManager.error("Error en accionSalirProcedimiento: ", e);
			request.getSession().removeAttribute("MAPAJUSSERV");
			return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Guardando el procedimiento", "errors.estadoInvalido", true);
		} catch (Exception e) {
			UtilidadBD.abortarTransaccion(con);
			Log4JManager.error(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO,e);
		}
		
		return null;

	}

	
	

	/**
	 * Método para registrar la justificacion no pos del procedimiento
	 * @param request 
	 * @param con
	 * @param forma
	 * @param i
	 * @param usuario
	 * @param solicitud
	 * @param yaSeActualizoUnaJustificacionNoPos 
	 * @param fjsn 
	 * @return
	 */
	@SuppressWarnings({"rawtypes" })
	private boolean registrarJustificacionNoPosProcedimientos(HttpServletRequest request, Connection con,
			SolicitarForm forma, int i, UsuarioBasico usuario,
			SolicitudProcedimiento solicitud, FormatoJustServNopos fjsn, Boolean yaSeActualizoUnaJustificacionNoPos) 
	{
		boolean inserto = true; 
		// SI EL INDICADOR DE JUSTIFICAR ES VERDADERO SE HACE EL INGRESO DE LA JUSTIFICACION NO POS
		
		
		
		if (forma.getValores("justificar_"+i).toString().equals("true")){
			HashMap justificacion=(HashMap) request.getSession().getAttribute(forma.getValores("codigo_"+i)+"MAPAJUSSERV");
			
        	fjsn.ingresarJustificacion(
        		con,
        		usuario.getCodigoInstitucionInt(), 
        		usuario.getLoginUsuario(), 
        		//forma.getJustificacionesServicios(),
        		justificacion,
        		solicitud.getNumeroSolicitud(),
        		ConstantesBD.codigoNuncaValido,
        		Integer.parseInt(forma.getValores("codigo_"+i).toString()),
        		usuario.getCodigoPersona());
        }
		
		// SI EL INDICADOR DE JUSTIFICAR INDICA QUE ES UNA ORDEN AMBULATORIA SE ACTUALIZA LA JUSTIFICACION YA INGRESADA CON EL NUMERO DE SOLICITUD
		if (forma.getValores("justificar_"+i).toString().equals("ordenAmbulatoria"))
		{
			if(!yaSeActualizoUnaJustificacionNoPos.booleanValue())
			{
				UtilidadJustificacionPendienteArtServ.actualizarSolicitudJusOrdenAmbulatoria(con, Utilidades.convertirAEntero(Utilidades.obtenerCodigoOrdenAmbulatoria(con,forma.getOrdenAmbulatoria(), usuario.getCodigoInstitucionInt())+""), Utilidades.convertirAEntero(forma.getCodigoServicioPostular()), solicitud.getNumeroSolicitud(), false);
				yaSeActualizoUnaJustificacionNoPos = new Boolean(true);
			} 
			else 
			{
				inserto = UtilidadJustificacionPendienteArtServ.ingresarJustificacionSegunOrdenAmbulatoria(con, Utilidades.convertirAEntero(Utilidades.obtenerCodigoOrdenAmbulatoria(con,forma.getOrdenAmbulatoria(), usuario.getCodigoInstitucionInt())+""), Utilidades.convertirAEntero(forma.getCodigoServicioPostular()), solicitud.getNumeroSolicitud(), false, usuario.getCodigoInstitucionInt());
			}
		}
		return inserto;
	}

	/**
	 * Método para llenar el mundo del servicio procedimiento
	 * @param con
	 * @param forma
	 * @param i
	 * @param solicitud
	 * @param codigoInstitucion 
	 */
	@SuppressWarnings("rawtypes")
	private void llenarMundoServicioProcedimiento(Connection con,
			SolicitarForm forma, int i, SolicitudProcedimiento solicitud, PersonaBasica paciente, int codigoInstitucion) 
	{
		/*Obtener el codigo de la finalidad*/
		solicitud.setFinalidad(Integer.parseInt(forma.getValores("finalidad_"+i).toString()));
		/* Obtener el cï¿½digo del servicio de la solicitud */
		solicitud.setCodigoServicioSolicitado(Integer.parseInt((String)forma.getValores("codigo_" + i)));
		HashMap datosServicio = Utilidades.consultarInformacionServicio(con, solicitud.getCodigoServicioSolicitado()+"", codigoInstitucion);
		solicitud.setNombreServicioSolicitado("("+datosServicio.get("codigopropietario")+") "+datosServicio.get("nombre"));
		//Se obtiene el tipo de servicio de la solicitud
		solicitud.setCodigoTipoServicio(Utilidades.obtenerTipoServicio(con, solicitud.getCodigoServicioSolicitado()+""));
		logger.info("\n\nTipo de Servicio: "+solicitud.getCodigoTipoServicio());
		//por defecto en la creacion de la solicitud la finalizacion de la respuesta multiple es false;
		solicitud.setFinalizadaRespuesta(false);
		//el indicativo de si la solicitud es de respuesta multiple depende de la parametrizacion del servicio
		solicitud.setRespuestaMultiple(Utilidades.esServicioRespuestaMultiple(con,(String)forma.getValores("codigo_" + i)));
		/* Establecer la urgencia de la solicitud */
		solicitud.setUrgente((Boolean.valueOf((String)forma.getValores("urgente_" + i) ) ).booleanValue());
		String acronimoDias="";
		if(solicitud.isUrgente()){
			if(datosServicio.get("acrodiasurgente") != null && !datosServicio.get("acrodiasurgente").toString().isEmpty()){
				acronimoDias = datosServicio.get("acrodiasurgente").toString();
			}
		}
		else{
			if(datosServicio.get("acrodiasnormal") != null && !datosServicio.get("acrodiasnormal").toString().isEmpty()){
				acronimoDias = datosServicio.get("acrodiasnormal").toString();
			}
		}
		forma.setValores("acronimodias_"+i, acronimoDias);
		/* Obtener dato soicitud mï¿½ltiple*/
		solicitud.setMultiple((Boolean.valueOf((String)forma.getValores("hidden_multiple_" + i) ) ).booleanValue());
		InfoDatosInt centroCosto=new InfoDatosInt();
		centroCosto.setCodigo(Integer.parseInt((String)forma.getValores("centro_costo_" + i)));
		solicitud.setCentroCostoSolicitado(centroCosto);
		if(forma.isIndicativoOrdenAmbulatoria())
		{
			solicitud.setCentroCostoSolicitante(new InfoDatosInt(Integer.parseInt(forma.getCentroCostoSolicitante()+"")));
		}
		String frecuenciaStr=(String)forma.getValores("frecuencia_" + i);
		//////////////////////////////////////////////////////////////////////////////////////////////////
		//adicionado por anexo 591 (portatil)
		if (UtilidadTexto.getBoolean(forma.getValores("portatilCheckBox_"+i)+""))
		{
			//se adiciona el portatil
			solicitud.setPortatil(forma.getValores("portatil_"+i)+"");
		}
		else
		{
			//se adiciona el portatil
			solicitud.setPortatil(ConstantesBD.codigoNuncaValido+"");
		}
		//////////////////////////////////////////////////////////////////////////////////////////////////
		float frecuencia=-1;
		if(frecuenciaStr!=null && !frecuenciaStr.equals(""))
		{
			frecuencia=Float.parseFloat(frecuenciaStr);
		}
		solicitud.setFrecuencia(frecuencia);
		if(forma.getValores("tipo_frecuencia_" + i)!=null&&!UtilidadTexto.isEmpty(forma.getValores("tipo_frecuencia_" + i)+""))
		{
			int tipoFrecuencia=Integer.parseInt((String)forma.getValores("tipo_frecuencia_" + i));
			logger.info("tipoFrecuencia "+tipoFrecuencia);
			solicitud.setTipoFrecuencia(tipoFrecuencia);
		}
		
		//Se agrega cambio por versión 1.52 del Anexo Solicitud de Medicamentos 65
		//Guardar último Diagnóstico y Tipo de CIE del Paciente 
		//@author Diana Carolina G
		DtoDiagnostico dtoDiagnostico = null;
		if(forma.isIndicativoOrdenAmbulatoria()){
			if(forma.getDtoDiagnostico()!=null)
			{
				dtoDiagnostico = new DtoDiagnostico();
				dtoDiagnostico=forma.getDtoDiagnostico();
			}
		}else{
			dtoDiagnostico = Utilidades.getDiagnosticoPacienteCuenta(paciente.getCodigoCuenta());
		}
		solicitud.setDtoDiagnostico(dtoDiagnostico);
		
	}

	/**
	 * Método implementado para generar una nueva solicitud de procedimientos
	 * @param con
	 * @param usuario
	 * @param forma
	 * @param solicitud
	 * @param paciente
	 * @param estado
	 * @param numeroDocumento 
	 * @return
	 */
	private boolean crearSolicitudProcedimiento(Connection con,
			UsuarioBasico usuario, SolicitarForm forma,
			SolicitudProcedimiento solicitud, PersonaBasica paciente,
			String estado, int numeroDocumento,
			List<InfoResponsableCobertura>listaCoberturaServicio) throws IPSException 
	{
		boolean inserto = false;
		boolean dejarPendiente=true;
		if(UtilidadValidacion.esServicioViaIngresoCargoSolicitud(con, paciente.getCodigoUltimaViaIngreso()+"", ""+solicitud.getCodigoServicioSolicitado(), usuario.getCodigoInstitucion()))
		{
			dejarPendiente=false;
		}
		
		/* Genera ls solicitud */
		try
		{
			if(solicitud.insertarTransaccional(con, estado, numeroDocumento, paciente.getCodigoCuenta(), true,solicitud.getPortatil())>0)
			{
				inserto = true;
			}
		}
		catch(SQLException e)
		{
			logger.error("Error a generar la solicitud de procedimiento: "+e);
			inserto = false;
			
		}
		
		if(inserto)
		{
			logger.info("\n");
			logger.info("FECHA SOLICITUD PARA ANALISIS DEL CARGO: -->"+solicitud.getFechaSolicitud());
			logger.info("\n");
			
			 //GENERACION DEL CARGO Y SUBCUENTA - EVALUACION COBERTURA 
		    Cargos cargos= new Cargos();
		    cargos.setPyp(forma.isSolPYP());
		    inserto= cargos.generarSolicitudSubCuentaCargoServiciosEvaluandoCobertura(	con, 
				    																			usuario, 
				    																			paciente, 
				    																			dejarPendiente/*dejarPendiente*/, 
				    																			solicitud.getNumeroSolicitud(), 
				    																			ConstantesBD.codigoTipoSolicitudProcedimiento /*codigoTipoSolicitudOPCIONAL*/, 
				    																			paciente.getCodigoCuenta() /*codigoCuentaOPCIONAL*/, 
				    																			solicitud.getCentroCostoSolicitado().getCodigo()/*codigoCentroCostoEjecutaOPCIONAL*/, 
				    																			solicitud.getCodigoServicioSolicitado()/*codigoServicioOPCIONAL*/, 
				    																			1 /*cantidadServicioOPCIONAL*/, 
				    																			ConstantesBD.codigoNuncaValidoDouble/*valorTarifaOPCIONAL*/, 
				    																			ConstantesBD.codigoNuncaValido /*codigoEvolucionOPCIONAL*/,
				    																			/*numeroAutorizacionOPCIONAL*/
				    																			""/*esPortatil*/,
				    																			false,
				    																			solicitud.getFechaSolicitud(),"");
		    
		    
		    if(inserto){
	    		 logger.info("INSERTO cargos :  CODIGO DETALLE CARGO-->"+cargos.getDtoDetalleCargo().getCodigoDetalleCargo());
	    		 forma.setCodigoDetalleCargo(Utilidades.convertirAEntero(String.valueOf(cargos.getDtoDetalleCargo().getCodigoDetalleCargo())));	 
	    		 forma.setCodigoConvenioCargo(cargos.getDtoDetalleCargo().getCodigoConvenio());
	    		 forma.setCodigoSubCuentaCargo(Utilidades.convertirAEntero(String.valueOf(cargos.getDtoDetalleCargo().getCodigoSubcuenta())));
	    		 
    		    /**Se adiciona a cada Servicio la informacion correspondiente de la cobertura para 
        	     * evaluacion posterior en la autorizacion de Capitacion sub*/
        	    DtoSolicitudesSubCuenta dtoSolicitudesSubCuenta=new DtoSolicitudesSubCuenta();
        	    dtoSolicitudesSubCuenta.getServicio().setCodigo(solicitud.getCodigoServicioSolicitado()+"");
        	    dtoSolicitudesSubCuenta.setNumeroSolicitud(solicitud.getNumeroSolicitud()+"");
        	    dtoSolicitudesSubCuenta.setConsecutivoSolicitud(solicitud.getConsecutivoOrdenesMedicas()+"");
        	    dtoSolicitudesSubCuenta.setUrgenteSolicitud(solicitud.getUrgente());
        	    dtoSolicitudesSubCuenta.setFinalidadSolicitud(solicitud.getFinalidad());
        	    cargos.getInfoResponsableCoberturaGeneral().getDtoSubCuenta().getSolicitudesSubcuenta().add(dtoSolicitudesSubCuenta);
        	    listaCoberturaServicio.add(cargos.getInfoResponsableCoberturaGeneral());
	    	 }
		    
		    
		    //si es portatil
		    if( solicitud.getPortatilInt()>0 && inserto)
		    {
		    	cargos= new Cargos();
		    	cargos.setPyp(forma.isSolPYP());
		    	logger.info("VOY A INGRESAR EL PORTATIL!!!!!!!!!!!!!");
		 		if(UtilidadValidacion.esServicioViaIngresoCargoSolicitud(con, paciente.getCodigoUltimaViaIngreso()+"", ""+solicitud.getPortatil(), usuario.getCodigoInstitucion()))
				{
					dejarPendiente=false;
				}
		 		else
		 		{
		 			dejarPendiente=true;
		 		}

		    	inserto= cargos.generarSolicitudSubCuentaCargoServiciosEvaluandoCobertura(		con, 
					    																		usuario, 
					    																		paciente, 
					    																		dejarPendiente/*dejarPendiente*/, 
					    																		solicitud.getNumeroSolicitud(), 
					    																		ConstantesBD.codigoTipoSolicitudProcedimiento /*codigoTipoSolicitudOPCIONAL*/, 
					    																		paciente.getCodigoCuenta() /*codigoCuentaOPCIONAL*/, 
					    																		solicitud.getCentroCostoSolicitado().getCodigo()/*codigoCentroCostoEjecutaOPCIONAL*/, 
					    																		solicitud.getPortatilInt()/*codigoServicioOPCIONAL*/, 
					    																		1 /*cantidadServicioOPCIONAL*/, 
					    																		ConstantesBD.codigoNuncaValidoDouble/*valorTarifaOPCIONAL*/, 
					    																		ConstantesBD.codigoNuncaValido /*codigoEvolucionOPCIONAL*/,
					    																		/*numeroAutorizacionOPCIONAL*/
					    																		ConstantesBD.acronimoSi/*esPortatil*/,
					    																		false,
					    																		solicitud.getFechaSolicitud(),"");
		    	 
		    }
		}
		logger.info("valor de la variable: "+inserto);
		return inserto;
	}

	/**
	 * 
	 * @param con
	 * @param usuario
	 * @param forma
	 * @param solicitud
	 * @param paciente
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private boolean crearSolicitudesServiciosIncluidos(Connection con, UsuarioBasico usuario, SolicitarForm forma, SolicitudProcedimiento solicitud, PersonaBasica paciente, String estado) throws IPSException 
	{
		logger.info("******************************************************************************************************************************************");
		logger.info("\n\n\n****************************************CREAR SOLICITUDES PARA SERVICIOS INCLUIDOS*************************************************");
		logger.info("******************************************************************************************************************************************");
		int numeroSolicitudPpal= forma.getNumeroSolicitud();
		int codigoServicioPpal= solicitud.getCodigoServicioSolicitado();
		boolean inserto=true;
		List<InfoResponsableCobertura>listaCoberturaServicio = null;
		
		
		logger.info("NUMERO SOLICITUD PPAL--->"+numeroSolicitudPpal+" SERVICIO PPAL->"+codigoServicioPpal+"\n\n");
		
		//////PARTE DE SERVICIOS
		for(int w=0; w<Utilidades.convertirAEntero(forma.getServiciosIncluidos("numRegistros_"+codigoServicioPpal)+"");w++)
		{
			logger.info("*******************SERVICIO INCLUIDO CON CANTIDAD->"+forma.getServiciosIncluidos("cantidad_"+w+"_"+codigoServicioPpal)+" CONFIRMADO->"+forma.getServiciosIncluidos("confirmar_"+w+"_"+codigoServicioPpal));
			
			if(UtilidadTexto.getBoolean(forma.getServiciosIncluidos("confirmar_"+w+"_"+codigoServicioPpal)+""))
			{	
				for(int x=0; x<Utilidades.convertirAEntero(forma.getServiciosIncluidos("cantidad_"+w+"_"+codigoServicioPpal)+""); x++)
				{	
					DtoServicioIncluidoSolProc dto= new DtoServicioIncluidoSolProc();
					dto.setSolicitudPpal(numeroSolicitudPpal);
					dto.setUsuarioModifica(usuario.getLoginUsuario());
					dto.setServicioPpal(codigoServicioPpal);
					dto.setServicioIncluido(Utilidades.convertirAEntero(forma.getServiciosIncluidos("cod_serv_incluido_"+w+"_"+codigoServicioPpal)+""));
					dto.setCentroCostoEjecuta(Utilidades.convertirAEntero(forma.getServiciosIncluidos("cod_centro_costo_ejecuta_"+w+"_"+codigoServicioPpal)+""));
					dto.setEsPos(UtilidadTexto.getBoolean(forma.getServiciosIncluidos("es_pos_serv_incluido_"+w+"_"+codigoServicioPpal)+""));
					
					dto.log();
					logger.info("****************************************************************************************************************************************");
					logger.info("servicio incluido-->"+dto.getServicioIncluido());
					
					//primero evaluamos si es un servicio no cruento o procedimiento
					String tipoServicio= Utilidades.obtenerTipoServicio(con, dto.getServicioIncluido()+"");
					
					if(tipoServicio.trim().equals(ConstantesBD.codigoServicioNoCruentos+""))
					{
						SolicitudProcedimiento solicitudProcedimientoNueva= llenarSolicitudProc(con, solicitud, dto, ConstantesBD.codigoTipoSolicitudCirugia);
						logger.info("servicio no cruento por lo tanto inserta una cx!!!!!!!!!!!!");
						inserto = crearSolicitudCirugia(con,usuario,forma,solicitudProcedimientoNueva,paciente,estado,listaCoberturaServicio)>0?true:false;
						dto.setSolicitudIncluida(this.numeroSolicitudServIncluida);
						dto.log();
						if(!inserto)
							return false;
					}
					else
					{
						SolicitudProcedimiento solicitudProcedimientoNueva= llenarSolicitudProc(con, solicitud, dto, ConstantesBD.codigoTipoSolicitudProcedimiento);
						logger.info("servicio procedimiento inserta proc!!!!!!!!!!!!!!!!!!");
						inserto=crearSolicitudProcedimientosIncluidos(con, forma, solicitudProcedimientoNueva, usuario, paciente, estado);
						dto.setSolicitudIncluida(this.numeroSolicitudServIncluida);
						dto.log();
						if(!inserto)
							return false;
						else
						{
							HashMap infoInterfaz = new HashMap();
							////*********************************************************************************************
							//****************EDICION ENCABEZADO INTERFAZ LABORATORIOS*************************************
							//**********************************************************************************************
							//Se llenan datos necesarios para interfaz laboratorios
							infoInterfaz.put("numeroDocumento",solicitudProcedimientoNueva.getNumeroDocumento()+"");
							infoInterfaz.put("fechaSolicitud",solicitudProcedimientoNueva.getFechaSolicitud());
							infoInterfaz.put("horaSolicitud",solicitudProcedimientoNueva.getHoraSolicitud());
							infoInterfaz.put("codigoMedico",solicitudProcedimientoNueva.getCodigoMedicoSolicitante()+"");
							infoInterfaz.put("nombreMedico",usuario.getNombreUsuario());
							infoInterfaz.put("institucion",usuario.getCodigoInstitucion());
							infoInterfaz.put("observaciones",solicitudProcedimientoNueva.getComentario());
							Cama cama = new Cama();
							try 
							{
								cama.cargarCama(con,paciente.getCodigoCama()+"");
								infoInterfaz.put("numeroCama",cama.getDescripcionCama());
							} 
							catch (SQLException e) 
							{
								logger.error("Error cargando la cama: "+e);
								infoInterfaz.put("numeroCama","");
							}
							
							if(ValoresPorDefecto.getCliente().equals(ConstantesBD.clienteSHAIO))
							{
								//infoInterfaz.put("comentarioDiagnostico", UtilidadesHistoriaClinica.obtenerDescripcionDxIngresoPaciente(con, paciente.getCodigoCuenta(), paciente.getCodigoUltimaViaIngreso()));
								infoInterfaz.put("horaSistema",UtilidadFecha.getHoraSegundosActual(con));
								infoInterfaz.put("nitEmpresa",UtilidadesFacturacion.obtenerNitEmpresaConvenio(con, paciente.getCodigoConvenio()));
								infoInterfaz.put("nroCarnet",UtilidadesManejoPaciente.obtenerNroCarnetIngresoPaciente(con, paciente.getCodigoIngreso()));
								infoInterfaz.put("codigoEspecialidadSolicitante",solicitudProcedimientoNueva.getEspecialidadSolicitante().getCodigo());
								//infoInterfaz.put("ciePrevio",Utilidades.consultarDiagnosticosPaciente(con, paciente.getCodigoCuenta()+"", paciente.getCodigoUltimaViaIngreso()));
								
							}
							//**********************************************************************************************
							///*********************************************************************************************
							///******************se completa informacion de la generación de archivo plano interfaz laboratorios*************************************
							infoInterfaz.put("numeroSolicitud_0",solicitudProcedimientoNueva.getNumeroSolicitud()+"");
							infoInterfaz.put("estado_0",ConstantesBD.codigoEstadoHCSolicitada+"");
							infoInterfaz.put("centroCosto_0",solicitudProcedimientoNueva.getCentroCostoSolicitado().getCodigo()+"");
							infoInterfaz.put("urgente_0",solicitudProcedimientoNueva.getUrgente()+"");
							infoInterfaz.put("codigoCUPS_0",Utilidades.obtenerCodigoPropietarioServicio(con,solicitudProcedimientoNueva.getCodigoServicioSolicitado()+"",ConstantesBD.codigoTarifarioCups));
							infoInterfaz.put("numRegistros","1");
							ActionErrors errores = InterfazLaboratorios.generarRegistroArchivo(infoInterfaz,paciente,ValoresPorDefecto.getCliente(),new ActionErrors());
							if(!errores.isEmpty())
								return false;
							//****************************************************************************************************************************************
						}
					}
					
					////////insertar las justificaciones pendientes
					insertarJustificacionesNoPosIncluidos(con, dto, usuario);
					
					///////////se inserta en tablas art_inclu_sol_proc y serv_inclu_sol_proc
					inserto=insertarServiciosIncluidosConArticulosIncluidos(con, forma, dto, w, usuario);
					
					if(!inserto)
						return false;
					
					
					logger.info("****************************************************************************************************************************************");
					
				}
			}	
		}
		
		////PARTE DE ARTICULOS
		for(int w=0; w<Utilidades.convertirAEntero(forma.getArticulosIncluidos("numRegistros_"+codigoServicioPpal)+"");w++)
		{
			logger.info("*******************ARTICULO "+forma.getArticulosIncluidos("cod_art_incluido_"+w+"_"+codigoServicioPpal)+" INCLUIDO CON CANTIDAD->"+forma.getArticulosIncluidos("cantidad_"+w+"_"+codigoServicioPpal));
			
			DtoArticuloIncluidoSolProc dtoArt= new DtoArticuloIncluidoSolProc();
			dtoArt.setArticuloIncluido(Utilidades.convertirAEntero(forma.getArticulosIncluidos("cod_art_incluido_"+w+"_"+codigoServicioPpal)+""));
			dtoArt.setCantidad(Utilidades.convertirAEntero(forma.getArticulosIncluidos("cantidad_"+w+"_"+codigoServicioPpal)+""));
			dtoArt.setCantidadMaxima(Utilidades.convertirAEntero(forma.getArticulosIncluidos("cantidad_"+w+"_"+codigoServicioPpal)+""));
			dtoArt.setEsServicioIncluido(false);
			dtoArt.setFarmacia(Utilidades.convertirAEntero(forma.getArticulosIncluidos("cod_farmacia_"+w+"_"+codigoServicioPpal)+""));
			dtoArt.setServicioPpal(codigoServicioPpal);
			dtoArt.setSolicitudIncluida(ConstantesBD.codigoNuncaValido); //esta se inserta en la respuesta
			dtoArt.setSolicitudPpal(numeroSolicitudPpal);
			dtoArt.setUsuarioModifica(usuario.getLoginUsuario());
			
			logger.info("INSERCION DE LOS ARTICULOS INCLUIDOS DEL SERVICIO PPAL-->"+codigoServicioPpal);
			dtoArt.log();
			
			inserto=Servicios_ArticulosIncluidosEnOtrosProcedimientos.insertarArticulosIncluidosSolicitudProcedimientos(con, dtoArt);
			
		}
		
		forma.setNumeroSolicitud(numeroSolicitudPpal);
		
		logger.info("******************************************************************************************************************************************");
		logger.info("\n\n\n****************************FINNNNNNNNNNNNNNNNNNNNNN    CREAR SOLICITUDES PARA SERVICIOS INCLUIDOS*************************************************");
		logger.info("******************************************************************************************************************************************");
		
		return inserto;
	}
	
	/**
	 * 
	 * @param con
	 * @param forma
	 * @param dto
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private boolean insertarServiciosIncluidosConArticulosIncluidos(Connection con, SolicitarForm forma, DtoServicioIncluidoSolProc dto, int indice, UsuarioBasico usuario) 
	{
		//insertamos el servicio incluido
		boolean inserto= Servicios_ArticulosIncluidosEnOtrosProcedimientos.insertarServiciosIncluidosSolicitudProcedimientos(con, dto);
		//insertamos los articulos incluidos del servicio incluido
		
		HashMap<Object, Object> articulosMap= (HashMap<Object, Object>)((HashMap<Object, Object>)forma.getServiciosIncluidos("ARTICULOS_INCLUIDOS_SERVICIO_INCLUIDO_"+indice+"_"+dto.getServicioPpal())).clone();
		
		Utilidades.imprimirMapa(articulosMap);
		
		for(int w=0; w<Utilidades.convertirAEntero(articulosMap.get("numRegistros_"+dto.getServicioIncluido())+""); w++)
		{
			DtoArticuloIncluidoSolProc dtoArt= new DtoArticuloIncluidoSolProc();
			dtoArt.setArticuloIncluido( Utilidades.convertirAEntero(articulosMap.get("cod_art_incluido_"+w)+""));
			dtoArt.setCantidad(Utilidades.convertirAEntero(articulosMap.get("cantidad_"+w)+""));
			dtoArt.setCantidadMaxima(Utilidades.convertirAEntero(articulosMap.get("cantidad_"+w)+""));
			dtoArt.setEsServicioIncluido(true);
			dtoArt.setFarmacia(Utilidades.convertirAEntero(articulosMap.get("cod_farmacia_"+w)+""));
			dtoArt.setServicioPpal(dto.getServicioIncluido()); // en este caso el servicio ppal es el servicio incluido
			dtoArt.setSolicitudIncluida(ConstantesBD.codigoNuncaValido); //nunca se genera solicitud de articulos incluidos
			dtoArt.setSolicitudPpal(dto.getSolicitudIncluida()); //en este caso la sol ppal del articulo es la solicitud del servicio incluido
			dtoArt.setUsuarioModifica(usuario.getLoginUsuario());
			
			logger.info("INSERCION DE LOS ARTICULOS INCLUIDOS DEL SERVICIO INCLUUIDO-->"+dto.getServicioIncluido());
			dtoArt.log();
			
			inserto=Servicios_ArticulosIncluidosEnOtrosProcedimientos.insertarArticulosIncluidosSolicitudProcedimientos(con, dtoArt);
			if(!inserto)
				w=Utilidades.convertirAEntero(articulosMap.get("numRegistros_"+dto.getServicioIncluido())+"");
		}
		
		return inserto;
	}

	/**
	 * 
	 * @param con
	 * @param dto
	 */
	private void insertarJustificacionesNoPosIncluidos(Connection con,DtoServicioIncluidoSolProc dto, UsuarioBasico usuario) throws IPSException 
	{
		logger.info("\n********************JUSTIFICACION NO POS ->");
		logger.info("********************SOLICITUD INCLUIDA->"+dto.getSolicitudIncluida()+" servicio incluido->"+dto.getServicioIncluido());
		if(!dto.getEsPos())
		{
			logger.info("solicitud no pos!!!!");
			//verificamos la param del convenio que lo cubre
			ArrayList<DtoSubCuentas> dtoResponsable= UtilidadesHistoriaClinica.obtenerResponsablesSolServArt(con, dto.getSolicitudIncluida(), dto.getServicioIncluido(), true);
			if(dtoResponsable.size()==1)
			{
				logger.info("convenio responsable-->"+dtoResponsable.get(0).getConvenio().getCodigo()+" - "+dtoResponsable.get(0).getConvenio().getNombre());
				if (UtilidadesFacturacion.requiereJustificacioServ(con, dtoResponsable.get(0).getConvenio().getCodigo()))
				{	
					double subcuenta = Cargos.obtenerCodigoSubcuentaDetalleCargo(con, dto.getServicioIncluido(), dto.getSolicitudIncluida(), dtoResponsable.get(0).getConvenio().getCodigo(), false);
					logger.info("REQUIERE JUSTIFICACION!!!!!!!!!!!");
					UtilidadJustificacionPendienteArtServ.insertarJusNP(con, dto.getSolicitudIncluida(), dto.getServicioIncluido(), 1, usuario.getLoginUsuario(), false, false, Utilidades.convertirAEntero(subcuenta+""),"");
				}	
			}
		}
		logger.info("********************FINNNNNNNNN    JUSTIFICACION NO POS \n");
	}

	/**
	 * 
	 * @param con
	 * @param forma
	 * @param solicitud
	 * @param usuario
	 * @param paciente
	 * @return
	 */
	private boolean crearSolicitudProcedimientosIncluidos(Connection con,SolicitarForm forma, SolicitudProcedimiento solicitud,UsuarioBasico usuario, PersonaBasica paciente, String estado) throws IPSException 
	{
		boolean inserto=true;
		boolean dejarPendiente=true;
		
		if(UtilidadValidacion.esServicioViaIngresoCargoSolicitud(con, paciente.getCodigoUltimaViaIngreso()+"", ""+solicitud.getCodigoServicioSolicitado(), usuario.getCodigoInstitucion()))
		{
			dejarPendiente=false;
		}
		
		/* Genera ls solicitud */
		try 
		{
			solicitud.insertarTransaccional(con, estado, solicitud.getNumeroDocumento(), paciente.getCodigoCuenta(), true,solicitud.getPortatil());
			logger.info("numero solicitud generado incluido->"+solicitud.getNumeroSolicitud());
			this.numeroSolicitudServIncluida=solicitud.getNumeroSolicitud();

			adicionarServicioAMapaSolicitudes(forma, solicitud.getNumeroSolicitud());
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
		 //GENERACION DEL CARGO Y SUBCUENTA - EVALUACION COBERTURA 
	    Cargos cargos= new Cargos();
	    cargos.setPyp(forma.isSolPYP());
	    inserto= cargos.generarSolicitudSubCuentaCargoServiciosEvaluandoCobertura(	con, 
	    																			usuario, 
	    																			paciente, 
	    																			dejarPendiente/*dejarPendiente*/, 
	    																			solicitud.getNumeroSolicitud(), 
	    																			ConstantesBD.codigoTipoSolicitudProcedimiento /*codigoTipoSolicitudOPCIONAL*/, 
	    																			paciente.getCodigoCuenta() /*codigoCuentaOPCIONAL*/, 
	    																			solicitud.getCentroCostoSolicitado().getCodigo()/*codigoCentroCostoEjecutaOPCIONAL*/, 
	    																			solicitud.getCodigoServicioSolicitado()/*codigoServicioOPCIONAL*/, 
	    																			1 /*cantidadServicioOPCIONAL*/, 
	    																			ConstantesBD.codigoNuncaValidoDouble/*valorTarifaOPCIONAL*/, 
	    																			ConstantesBD.codigoNuncaValido /*codigoEvolucionOPCIONAL*/,
	    																			/* "" -- numeroAutorizacionOPCIONAL*/
	    																			""/*esPortatil*/,false,solicitud.getFechaSolicitud(),"");
	    
	    
	    logger.info("inserto????"+inserto);
	    
	    return inserto;
	}

	private void adicionarServicioAMapaSolicitudes(SolicitarForm forma, int numeroSolicitud) {
		//********INICIO INSERTAMOS LAS SOLICITUDES DE SERVICIOS INCLUIDOS EN EL MAPA**********
		forma.setNumerosSolicitudes("numeroSolicitud_"+(forma.getNumerosSolicitudes("numRegistros")), numeroSolicitud);
		forma.setNumerosSolicitudes("numRegistros", (Utilidades.convertirAEntero(forma.getNumerosSolicitudes("numRegistros")+"") +1));
		//**********FIN INSERTAMOS LAS SOLICITUDES DE SERVICIOS INCLUIDOS EN EL MAPA***********
	}

	/**
	 * 
	 * @param solicitud
	 * @return
	 */
	private SolicitudProcedimiento llenarSolicitudProc(Connection con, SolicitudProcedimiento solicitud, DtoServicioIncluidoSolProc dto, int tipoSolicitud) 
	{
		SolicitudProcedimiento sol= new SolicitudProcedimiento();
		sol.setApellidoMedico(solicitud.getApellidoMedico());
		sol.setApellidoMedicoResponde(solicitud.getApellidoMedicoResponde());
		sol.setApellidoPaciente(solicitud.getApellidoPaciente());
		sol.setCama(solicitud.getCama());
		sol.setCentroCostoSolicitado(new InfoDatosInt(dto.getCentroCostoEjecuta()));
		sol.setCentroCostoSolicitante(solicitud.getCentroCostoSolicitante());
		sol.setCobrable(solicitud.isCobrable());
		sol.setCodigoCentroAtencionCuentaSol(solicitud.getCodigoCentroAtencionCuentaSol());
		sol.setCodigoCuenta(solicitud.getCodigoCuenta());
		sol.setCodigoMedicoAnulacion(solicitud.getCodigoMedicoAnulacion());
		sol.setCodigoMedicoInterpretacion(solicitud.getCodigoMedicoAnulacion());
		sol.setCodigoMedicoSolicitante(solicitud.getCodigoMedicoSolicitante());
		sol.setCodigoPaciente(solicitud.getCodigoPaciente());
		sol.setCodigoServicioSolicitado(dto.getServicioIncluido());
		sol.setComentario(solicitud.getComentario());
		sol.setComentarioAdicional("");
		
		//@todo verificar el consecutivo de orden medica insertado.........
		
		/////revisar el consecutivo de ordenes medicas
		sol.setDatosMedico(solicitud.getDatosMedico());
		sol.setDatosMedicoResponde(solicitud.getDatosMedicoResponde());
		sol.setEspecialidadSolicitada(solicitud.getEspecialidadSolicitada());
		sol.setEspecialidadSolicitante(solicitud.getEspecialidadSolicitante());
		sol.setEsPos(UtilidadTexto.convertirSN(dto.getEsPos()+""));
		sol.setEsRespuestaMultiple(false);
		sol.setEstadoHistoriaClinica(solicitud.getEstadoHistoriaClinica());
		sol.setEstaFinalizadaRespuestaMultiple(false); 
		sol.setFechaAnulacion(solicitud.getFechaAnulacion());
		sol.setFechaGrabacion(solicitud.getFechaGrabacion());
		sol.setFechaInterpretacion(solicitud.getFechaInterpretacion());
		sol.setFechaRespuesta(solicitud.getFechaRespuesta());
		sol.setFechaSolicitud(solicitud.getFechaSolicitud());
		sol.setFinalidad(solicitud.getFinalidad());
		sol.setFinalizadaRespuesta(solicitud.isFinalizadaRespuesta());
		sol.setFrecuencia(solicitud.getFrecuencia());
		sol.setHoraAnulacion(solicitud.getHoraAnulacion());
		sol.setHoraGrabacion(solicitud.getHoraGrabacion());
		sol.setHoraInterpretacion(solicitud.getHoraInterpretacion());
		sol.setHoraRespuesta(solicitud.getHoraRespuesta());
		sol.setHoraSolicitud(solicitud.getHoraSolicitud());
		sol.setImpresion(solicitud.getImpresion());
		sol.setInterpretacion(solicitud.getInterpretacion());
		
		//@todo revisar lo de las justificaciones
		
		sol.setLiquidarAsocio(solicitud.getLiquidarAsocio());
		sol.setLoginMedico(solicitud.getLoginMedico());
		sol.setMotivoAnulacion(solicitud.getMotivoAnulacion());
		sol.setMotivoAnulacionPort(solicitud.getMotivoAnulacionPort());
		sol.setMultiple(false);
		sol.setNombreCentroAtencionCuentaSol(solicitud.getNombreCentroAtencionCuentaSol());
		sol.setNombreCentroCostoSolicitado(solicitud.getNombreCentroCostoSolicitado());
		sol.setNombreFinalidad(solicitud.getNombreFinalidad());
		sol.setNombreMedico(solicitud.getNombreMedico());
		sol.setNombreMedicoResponde(solicitud.getNombreMedicoResponde());
		sol.setNombreOtros(solicitud.getNombreOtros());
		sol.setNombrePaciente(solicitud.getNombrePaciente());
		sol.setNombreServicioSolicitado("");
		//sol.setNumeroAutorizacion(solicitud.getNumeroAutorizacion());
		sol.setNumeroDocumento(solicitud.numeroDocumentoSiguiente(con));
		
		//@todo setear el numero de solicitud
		
		sol.setOcupacionSolicitado(solicitud.getOcupacionSolicitado());
		sol.setOrdenAmbulatoria(solicitud.getOrdenAmbulatoria());
		sol.setPoolMedico(solicitud.getPoolMedico());
		sol.setPortatil(ConstantesBD.codigoNuncaValido+"");
		sol.setRespuestaMultiple(Utilidades.esServicioRespuestaMultiple(con, dto.getServicioIncluido()+""));
		logger.info("es multiple-->"+sol.getEsRespuestaMultiple());
		sol.setSolPYP(solicitud.isSolPYP());
		sol.setTieneCita(solicitud.isTieneCita());
		sol.setTipoFrecuencia(solicitud.getTipoFrecuencia());
		sol.setTipoSolicitud(new InfoDatosInt(tipoSolicitud));
		sol.setUrgente(solicitud.getUrgente());
		sol.setVaAEpicrisis(solicitud.isVaAEpicrisis());
		
		return sol;
		
	}

	/**
	 * 
	 * @param con
	 * @param user
	 * @param forma
	 * @param solProcedimiento
	 * @param paciente
	 * @param estado
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private int crearSolicitudCirugia( Connection con,UsuarioBasico user, SolicitarForm forma,
			SolicitudProcedimiento solProcedimiento, PersonaBasica paciente, String estado,
			List<InfoResponsableCobertura>listaCoberturaServicio) throws IPSException 
	{
		int numeroSolicitud = ConstantesBD.codigoNuncaValido;
		
		if(estado.equals(ConstantesBD.inicioTransaccion))
			UtilidadBD.iniciarTransaccion(con);
		//*******************SE INSERTA UNA SOLICITUD Bï¿½SICA*******************************************
        
		solProcedimiento.setTipoSolicitud(new InfoDatosInt(ConstantesBD.codigoTipoSolicitudCirugia));
        try
        {
            numeroSolicitud=solProcedimiento.insertarSolicitudGeneralTransaccional(con, ConstantesBD.continuarTransaccion);
            logger.info("numeroSolicitud CX->"+numeroSolicitud);            
            forma.setNumeroSolicitud(numeroSolicitud);
            this.numeroSolicitudServIncluida=numeroSolicitud;
        }
        catch(SQLException sqle)
        {
            logger.warn("Error al generar la solicitud basica de cirugï¿½as: "+sqle);
            return ConstantesBD.codigoNuncaValido;
            
        }
        //************************************************************************************
        
		SolicitudesCx mundoSolCx= new SolicitudesCx();
        
        int codigoPeticion=ConstantesBD.codigoNuncaValido;
        String manejoProgSalas="";
        HashMap peticionEncabezadoMap= new HashMap();
        Cuenta mundoCuenta= new Cuenta();
        mundoCuenta.cargarCuenta(con, paciente.getCodigoCuenta()+"");
        peticionEncabezadoMap.put("tipoPaciente",   mundoCuenta.getCodigoTipoPaciente());
        peticionEncabezadoMap.put("fechaPeticion", forma.getFechaSolicitud());
        peticionEncabezadoMap.put("horaPeticion", forma.getHoraSolicitud());
        peticionEncabezadoMap.put("solicitante", user.getCodigoPersona()+"");
        peticionEncabezadoMap.put("duracion", "");
        manejoProgSalas=ValoresPorDefecto.getManejoProgramacionSalasSolicitudesDyt(user.getCodigoInstitucionInt());
        if(manejoProgSalas.equals(ConstantesBD.acronimoNo))
        	peticionEncabezadoMap.put("programable", ConstantesBD.acronimoNo);
        else if(manejoProgSalas.equals(ConstantesBD.acronimoSi))
        	peticionEncabezadoMap.put("programable", ConstantesBD.acronimoSi);
        
        //Datos del servicio
        HashMap serviciosPeticionMap=new HashMap();
        serviciosPeticionMap.put("numeroFilasMapaServicios", "1");
        serviciosPeticionMap.put("fueEliminadoServicio_0", "false");
        serviciosPeticionMap.put("codigoServicio_0", solProcedimiento.getCodigoServicioSolicitado()+"");
        serviciosPeticionMap.put("codigoEspecialidad_0", ""); //va vaío
        serviciosPeticionMap.put("codigoTipoCirugia_0", "-1"); //no se ingresa informacion
        serviciosPeticionMap.put("numeroServicio_0", "1");
        serviciosPeticionMap.put("observaciones_0", solProcedimiento.getComentario());
        
        HashMap articulosPeticionMap= new HashMap();
        articulosPeticionMap.put("numeroMateriales", 0);
        articulosPeticionMap.put("numeroOtrosMateriales", 0);
        HashMap profesionalesMap= new HashMap();
        profesionalesMap.put("numeroProfesionales", 0);
        Peticion mundoPeticion= new Peticion();
        
        double subCuenta=ConstantesBD.codigoNuncaValido;
    	//evaluamos la cobertura
		InfoResponsableCobertura infoResponsableCobertura	= null;
		int codigoViaIngreso= Cuenta.obtenerCodigoViaIngresoCuenta(con, paciente.getCodigoCuenta()+"");
		infoResponsableCobertura=Cobertura.validacionCoberturaServicio(con, paciente.getCodigoIngreso()+"", codigoViaIngreso, paciente.getCodigoTipoPaciente(), solProcedimiento.getCodigoServicioSolicitado(), user.getCodigoInstitucionInt(),false, "" /*subCuentaCoberturaOPCIONAL*/);
		subCuenta=infoResponsableCobertura.getDtoSubCuenta().getSubCuentaDouble();
		
		if(infoResponsableCobertura.getInfoCobertura().incluido() && infoResponsableCobertura.getInfoCobertura().existe()) {
			serviciosPeticionMap.put("cubierto_0", ConstantesBD.acronimoSi);
		} else {	
			serviciosPeticionMap.put("cubierto_0", ConstantesBD.acronimoNo);
		}	
		
		serviciosPeticionMap.put("contrato_convenio_0", infoResponsableCobertura.getDtoSubCuenta().getContrato());
        
        //hermorhu - MT6505
        //Se guarda el codigo de ingreso del paciente ya que es necesario al ingresar a la Hoja Qx
        int codigoPeticionYNumeroInserciones[]= mundoPeticion.insertar(con, peticionEncabezadoMap, serviciosPeticionMap, profesionalesMap, articulosPeticionMap, paciente.getCodigoPersona(), paciente.getCodigoIngreso()/*ConstantesBD.codigoNuncaValido*/, user, true, false);
        codigoPeticion=codigoPeticionYNumeroInserciones[1];

		
		 /**Se adiciona a cada Servicio la informacion correspondiente de la cobertura para 
	     * evaluacion posterior en la autorizacion de Capitacion sub*/
	    DtoSolicitudesSubCuenta dtoSolicitudesSubCuenta=new DtoSolicitudesSubCuenta();
	    dtoSolicitudesSubCuenta.getServicio().setCodigo(solProcedimiento.getCodigoServicioSolicitado()+"");
	    dtoSolicitudesSubCuenta.setNumeroSolicitud(solProcedimiento.getNumeroSolicitud()+"");
	    dtoSolicitudesSubCuenta.setConsecutivoSolicitud(solProcedimiento.getConsecutivoOrdenesMedicas()+"");
	    dtoSolicitudesSubCuenta.setUrgenteSolicitud(solProcedimiento.getUrgente());
	    dtoSolicitudesSubCuenta.setFinalidadSolicitud(solProcedimiento.getFinalidad());
	    infoResponsableCobertura.getDtoSubCuenta().getSolicitudesSubcuenta().add(dtoSolicitudesSubCuenta);
	    listaCoberturaServicio.add(infoResponsableCobertura);
		
		//LUEGO SE INSERTAN LOS SERVICIOS DE LA ORDEN
        String temporalCodigoServicio="";
        int temporalCodigoCirugia= ConstantesBD.codigoNuncaValido;
        int temporalConsecutivo= 1;
        String temporalObservaciones="";
        temporalCodigoServicio= solProcedimiento.getCodigoServicioSolicitado()+"";
        try {
        	mundoSolCx.insertarSolicitudCxGeneralTransaccional1(con, numeroSolicitud+"", codigoPeticion+"", false, ConstantesBD.inicioTransaccion, subCuenta, ConstantesIntegridadDominio.acronimoIndicativoCargoNoCruento);
			mundoSolCx.insertarSolicitudCxXServicioTransaccional(
                	con, 
                	numeroSolicitud+"", 
                	temporalCodigoServicio, 
                	temporalCodigoCirugia, 
                	temporalConsecutivo, 
                	ConstantesBD.codigoNuncaValido, //esquema tarifario
                	ConstantesBD.codigoNuncaValidoDouble, //grupo o uvr
                	user.getCodigoInstitucionInt(), 
                	/*"", -- autorizacion*/  
                	solProcedimiento.getFinalidad(), //finalidad
                	temporalObservaciones, 
                	"", //via Cx 
                	"", //indicativo bilateral
                	"", //indicativo via de acceso
                	ConstantesBD.codigoNuncaValido, //codigo de la especialidad
                	"", //liquidar servicio
                	ConstantesBD.continuarTransaccion,
                	"",
                	null);
			
			
		} 
        catch (SQLException e) 
		{
			e.printStackTrace();
		}
        logger.info("inserta 100% cx");
        
        adicionarServicioAMapaSolicitudes(forma, numeroSolicitud);
		return numeroSolicitud;
	}

	/**
	* Este mï¿½todo especifica las acciones a realizar en el estado
	* cancelar, por ahora no tiene implementaciï¿½n
	*
	* @param request Request de http
	* @param mapping Mapping de la aplicaciï¿½n
	* @param con Conexiï¿½n con la fuente de datos
	* @return
	* @throws SQLException
	*/
	private ActionForward accionCancelar(HttpServletRequest request, ActionMapping mapping, Connection con) throws Exception
	{
	    return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Accion no implementada", "Accion no implementada", false);
	}

	/**
	* Método que se encarga de realizar las validaciones comunes
	* a todos los estados / tipos en la solicitud general. Valida que
	* el usuario sea válido, que sea profesional de la salud y que
	* haya al menos un paciente cargado
	*
	* @param map Mapping de la aplicación
	* @param req Request de Http
	* @param paciente paciente cargado
	* @param medico medico / usuario que intenta acceder a la
	* funcionalidad
	 * @param validarValoracionInicial 
	 * @param solicitarForm 
	* @return
	*/
	@SuppressWarnings("deprecation")
	private ActionForward validacionesComunes(ActionMapping map, HttpServletRequest req, PersonaBasica paciente, UsuarioBasico medico, Connection con, boolean validarValoracionInicial, SolicitarForm solicitarForm ) throws Exception
	{
		ActionForward forward = null;
		try{
			logger.info("\n\n\n\nHACIENDO VALIDACIONES COMUNES\n\n\n\n");
			logger.info("--->"+paciente.getCodigoAdmision());
			logger.info("estado cuenta ppal--->"+UtilidadesHistoriaClinica.obtenerEstadoCuenta(con, paciente.getCodigoCuenta()).getCodigo());
			//Validación de autoatencion
			ResultadoBoolean respuesta = UtilidadesHistoriaClinica.esUsuarioAutoatendido(medico, paciente);
			if(respuesta.isTrue())
				forward = ComunAction.accionSalirCasoError(map, req, con, logger, "El paciente no puede ser autoatendido", respuesta.getDescripcion(), true);
			
	
			if(paciente.getCodigoPersona()==ConstantesBD.codigoNuncaValido){
				forward = ComunAction.accionSalirCasoError(map, req, con, logger, "No hay ningún paciente cargado. Para acceder a esta funcionalidad debe cargar un paciente", "errors.paciente.noCargado", true);
			}
			else if (paciente.getCodigoCuenta()<1)
			{
				forward = ComunAction.accionSalirCasoError(map, req, con, logger, "El paciente no tiene cuenta abierta.", "errors.paciente.cuentaNoAbierta", true);
			}
			//Para los casos dif. a Adm de hosp y Urgencias hay
			//que validar que no tenga egreso mï¿½dico. Caso
			//de urgencias maneja semi-egreso
			/*********************************************************************************************************************/
			/** Se verifica que el ingreso no sea realizado atraves de entidades subcontratadas*/
			else if (paciente.esIngresoEntidadSubcontratada())
			{
				req.setAttribute("codigoDescripcionError", "error.ingresoEntidadSubContratada");
				forward = map.findForward("paginaError");
			}
			/*********************************************************************************************************************/
			else if (paciente.getCodigoAdmision()>0)
			{			
				//Nueva valición tarea 44656 permitir ordenes estado cuenta asociada
				if(UtilidadValidacion.tieneEgreso(con, paciente.getCodigoCuenta()) 
						&& !UtilidadValidacion.puedoCrearCuentaAsocio(con,paciente.getCodigoIngreso()))
				{
					if(UtilidadValidacion.tieneEgreso(con, paciente.getCodigoCuenta()) 
							&& !UtilidadValidacion.puedoCrearCuentaAsocio(con,paciente.getCodigoIngreso()))
					{
						//tarea 163947, es no aplica cuando la via de ingresos es urgencias y es sol de medicamentos.
						//si apilca cuando es procedimientos o interconsulta, para cirugias y medicamentos las validaciones son propias en los action.
						if(solicitarForm.isValidarEgresoPaciente())
						{
							forward = ComunAction.accionSalirCasoError(map, req, con, logger, "El paciente es de urg/hosp pero ya tiene egreso mï¿½dico", "error.solicitudgeneral.tieneOrdenSalida", true);
						}
						else if(UtilidadValidacion.tieneEgreso(con, paciente.getCodigoCuenta())&& paciente.getCodigoUltimaViaIngreso()!=ConstantesBD.codigoViaIngresoUrgencias)
						{
							forward = ComunAction.accionSalirCasoError(map, req, con, logger, "El paciente es de urg/hosp pero ya tiene egreso mï¿½dico", "error.solicitudgeneral.tieneOrdenSalida", true);
						}
					}
				}
				else if (validarValoracionInicial&&!UtilidadValidacion.tieneValoraciones(con, paciente.getCodigoCuenta()))
				{
					//XPLANNER 55400
					//if(!solicitarForm.isIndicativoOrdenAmbulatoria())
					{
						//if(!solicitarForm.isIndicativoOrdenAmbulatoria())
						{
							forward = ComunAction.accionSalirCasoError(map, req, con, logger, "El paciente es de urg/hosp pero no tiene valoracion inicial", "error.solicitudgeneral.tieneValoracionInicial", true);
						}
						/*else
						{
							if(paciente.getCodigoUltimaViaIngreso() != ConstantesBD.codigoViaIngresoHospitalizacion && paciente.getCodigoUltimaViaIngreso() != ConstantesBD.codigoViaIngresoUrgencias)
								return ComunAction.accionSalirCasoError(map, req, con, logger, "El paciente es de urg/hosp pero no tiene valoracion inicial", "error.solicitudgeneral.tieneValoracionInicial", true);
						}*/
					}
				}
				else
				{
					int aux=Utilidades.verificarValoracionIngresoCuidadosEspeciales(con, paciente.getCodigoIngreso());
					if(aux==3)
					{
						forward = ComunAction.accionSalirCasoError(map, req, con, logger, "El paciente es de cuidados especiales pero no tiene valoracion inicial", "error.solicitudgeneral.tieneValoracionInicialCuidadosEspeciales", true);
					}
				}
			}
			else if(paciente.getCodigoUltimaViaIngreso()==ConstantesBD.codigoViaIngresoHospitalizacion || paciente.getCodigoUltimaViaIngreso()==ConstantesBD.codigoViaIngresoUrgencias)
			{
				int codigoCuentaPaciente= paciente.getCodigoCuenta();
				/*
				 * No es cama de observaciï¿½n
				 */
				if(!UtilidadValidacion.tieneValoracionConConductaCamaObservacion(con, codigoCuentaPaciente))
				{
					if(UtilidadValidacion.tieneEgreso(con, codigoCuentaPaciente) && UtilidadesHistoriaClinica.obtenerEstadoCuenta(con, codigoCuentaPaciente).getCodigo()!=ConstantesBD.codigoEstadoCuentaAsociada)
					{
						/*
						 * Si no es hospitalizar en piso, muestra error de egreso
						 */
						int[] conductas={ConstantesBD.codigoConductaSeguirHospitalizarPiso};
						if(!UtilidadValidacion.validacionConductaASeguir(con, codigoCuentaPaciente, conductas))
						{
							forward = ComunAction.accionSalirCasoError(map, req, con, logger, "Opciï¿½n disponible solo para pacientes con Admisiï¿½n Abierta", "errors.paciente.noAdmisionEgreso", true);
						}
					}
				}
				/*
				 * Si es conducta a seguir cama de observaciï¿½n
				 */
				else
				{
					/*
					 * Validar si tiene egreso
					 * Si lo tiene, debe validar el destino a
					 * la salida que sea diferente de hospitalizaciï¿½n
					 * De lo contrario muestra error de egreso
					 */
					if(UtilidadValidacion.tieneEgreso(con, codigoCuentaPaciente))
					{
						Egreso egreso=new Egreso();
						egreso.cargarEgresoGeneral(con, codigoCuentaPaciente);
						if((egreso.getDestinoSalida().getCodigo()!=ConstantesBD.codigoDestinoSalidaHospitalizacion))
						{
							logger.info("entra 2!!!!!!!!!!!");
							forward = ComunAction.accionSalirCasoError(map, req, con, logger, "Opciï¿½n disponible solo para pacientes con Admisiï¿½n Abierta", "errors.paciente.noAdmisionEgreso", true);
						}
					}
				}
			}
			
			HibernateUtil.beginTransaction();
			IngresosDelegate ingresoDelegate=new IngresosDelegate();
			Ingresos ingreso=ingresoDelegate.findById(paciente.getCodigoIngreso());			
			if(ingreso!=null)
			{	
				if( !UtilidadTexto.isEmpty(ingreso.getTipoIngreso())  && ingreso.getTipoIngreso().equals(ConstantesIntegridadDominio.acronimoTipoIngresoOdontologico))
				{
					if(!UtilidadOdontologia.pacienteConValoracionInicial(paciente.getCodigoPersona(),ingreso.getId()) && (paciente.getCodigoUltimaViaIngreso()==ConstantesBD.codigoViaIngresoHospitalizacion || paciente.getCodigoUltimaViaIngreso()==ConstantesBD.codigoViaIngresoUrgencias))
					{
						forward = ComunAction.accionSalirCasoError(map, req, con, logger, "El paciente es de odontologia y requiere una valoracion previa.", "El paciente es de odontologia y requiere una valoracion previa.", false);
					}
				}
			}
			
			else if(medico == null)
			{
				forward = ComunAction.accionSalirCasoError(map, req, con, logger, "No existe el usuario", "errors.usuario.noCargado", true);
			}
			else if( !UtilidadValidacion.esProfesionalSalud(medico))
			{
				forward = ComunAction.accionSalirCasoError(map, req, con, logger, "Usuario no es profesional de la salud", "errors.usuario.noAutorizado", true);
			}
			else if( paciente == null || paciente.getTipoIdentificacionPersona().equals("") )
			{
				forward = ComunAction.accionSalirCasoError(map, req, con, logger, "paciente null o sin  id", "errors.paciente.noCargado", true);
			}
			else if(!UtilidadesHistoriaClinica.obtenerEstadoIngreso(con, paciente.getCodigoIngreso()).getCodigo().trim().equals(ConstantesIntegridadDominio.acronimoEstadoAbierto))
			{
				forward = ComunAction.accionSalirCasoError(map, req, con, logger, "errors.ingresoEstadoDiferente", "errors.ingresoEstadoDiferenteAbierto", true);
			}
			else if(UtilidadValidacion.esCuentaValida(con, paciente.getCodigoCuenta())<1)
			{
				forward = ComunAction.accionSalirCasoError(map, req, con, logger, "errors.paciente.cuentaNoValida", "errors.paciente.cuentaNoValida", true);
			}
			HibernateUtil.endTransaction();
		}
		catch(Exception e){
			Log4JManager.error("ERROR validacionesComunes"+ e);
			HibernateUtil.abortTransaction();
		}
		return forward;
	}
	
	/**
	 * @param con
	 * @param solicitarForm
	 * @param usuario
	 * @param request
	 * @param paciente
	 * @param mapping
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private ActionForward accionImprimirSolicitud(
			Connection con,
			SolicitarForm solicitarForm, 
			UsuarioBasico usuario, 
			HttpServletRequest request, 
			PersonaBasica paciente, 
			ActionMapping mapping,
			boolean medicaCarta) 
	{
		try{
		logger.info("\n\n\n accionImprimirMediaCarta--->"+solicitarForm.getEstado());
		SolicitudProcedimiento	solicitud = new SolicitudProcedimiento();
		
		DesignEngineApi comp;
		InstitucionBasica institucionBasica= new InstitucionBasica();
        institucionBasica.cargarXConvenio(usuario.getCodigoInstitucionInt(), paciente.getCodigoConvenio());
        
        String reporte = "";
        
        if(medicaCarta)
        	reporte = "solicitudProcedimientosMediaCarta.rptdesign";
        else
        	reporte = "solicitudProcedimientos.rptdesign";
        	
    	//Se valida si se tiene la Firma Digital  y si es así se llama otro reporte diferente
        if(!UtilidadTexto.isEmpty(usuario.getFirmaDigital()) && medicaCarta)        
			reporte = "solicitudProcedimientosMediaCartaFirmaDigital.rptdesign";
        else if(!UtilidadTexto.isEmpty(usuario.getFirmaDigital()) && !medicaCarta)
        	reporte = "solicitudProcedimientosFirmaDigital.rptdesign";
		
        comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"ordenes/",reporte);
        
    	//en la primera celda va el nombre de la institucion y el cod min salud
    	Vector v=new Vector();
        v.add(institucionBasica.getRazonSocial());
        if(Utilidades.convertirAEntero(institucionBasica.getDigitoVerificacion()) != ConstantesBD.codigoNuncaValido)
        	v.add(Utilidades.getDescripcionTipoIdentificacion(con,institucionBasica.getTipoIdentificacion())+". "+institucionBasica.getNit()+" - "+institucionBasica.getDigitoVerificacion());
        else
        	v.add(Utilidades.getDescripcionTipoIdentificacion(con,institucionBasica.getTipoIdentificacion())+". "+institucionBasica.getNit());
        v.add(institucionBasica.getCodMinsalud());
        
        /*---------------Modificacion por tarea 77564*/
        InfoDatosInt centroAten=UtilidadesHistoriaClinica.obtenerCentroAtencionCuenta(con, paciente.getCodigoCuenta());
	    HashMap criterios = new HashMap ();
	    criterios.put("consecutivo", centroAten.getCodigo());
  
	     
	    HashMap tmp=Utilidades.obtenerDatosCentroAtencion(con, criterios);
	    logger.info("\n centro aten -->"+centroAten.getCodigo());
        if (UtilidadCadena.noEsVacio(tmp.get("direccion")+""))
        	v.add(tmp.get("direccion")+"");        				
        else
        	v.add(institucionBasica.getDireccion());
        /*-----------------------------------------------------------*/
        
        
        v.add("Tels. "+institucionBasica.getTelefono());
        comp.insertGridHeaderOfMasterPage(0, 1, 1, v.size());
        comp.insertLabelInGridOfMasterPage(0, 1, v);
        
        //en la segunda celda se coloca el logo de la institucion
        comp.insertImageHeaderOfMasterPage1(0, 2, institucionBasica.getLogoReportes());
        
        if(!UtilidadTexto.isEmpty(usuario.getFirmaDigital()))
        	comp.insertImageBodyPage(0, 0, usuario.getPathFirmaDigital(), "grillaFirmaDigital");
        
        comp.insertLabelBodyPage(0, 0, institucionBasica.getPieHistoriaClinica(), "piehiscli");
        
        
        //*******Modificamos el DataSet de los Detalles de la Solicitud*************
        comp.obtenerComponentesDataSet("detalleSolicitud");
//        logger.info(comp.obtenerQueryDataSet());
//        String newQuery = comp.obtenerQueryDataSet().replace("1=2", solicitud.obtenerWhereFormatoMediaCarta(con, solicitarForm.getNumerosSolicitudes()));
        
        /**DCU 40->Cambio 1.52********************************************/
        //if(solicitarForm.isIndicativoOrdenAmbulatoria())
		{	//Se cambia en la presentacion la validacion de cambio en la descripcion
        	if(!Utilidades.isEmpty(solicitarForm.getListaServiciosImpimirOrden()))
			{
        		for(int k=0;k<solicitarForm.getNumeroServicios();k++)
					solicitarForm.getNumerosSolicitudes().put("descripcionServicio", solicitarForm.getListaServiciosImpimirOrden().get(k));
				
			}else{
				for(int k=0;k<solicitarForm.getNumeroServicios();k++)
				{
					solicitarForm.getNumerosSolicitudes().put("descripcionServicio_"+k,solicitarForm.getValores("nombre_"+k).toString()+" - "+ 
    					solicitarForm.getValores("acronimodias_"+k).toString());
				}	
			}
		}
        /*****************************************************************/
        
        String newQuery[] = solicitud.obtenerConsultaSolicitudProcedimientosReporte(con, solicitarForm.getNumerosSolicitudes());
        //logger.info("valor de la query >>> "+newQuery);
        comp.modificarQueryDataSet(newQuery[1]);

        logger.info("valor de la query  data set detalleSolicitud >>> "+newQuery[1]);
        
        //*******Modificamos el DataSet del Encabezado*************
        comp.obtenerComponentesDataSet("encabezado");
//        newQuery = comp.obtenerQueryDataSet().replace("1=2", solicitud.obtenerWhereFormatoMediaCarta(con, solicitarForm.getNumerosSolicitudes())+" "+ValoresPorDefecto.getValorLimit1()+" 1");
        newQuery[0] = newQuery[0].replaceAll("remplazameXConsecutivosOrdenes", UtilidadTexto.convertirVectorACodigosSeparadosXComas(solicitarForm.getConsecutivosOrdenesMedicas(), false));
        comp.modificarQueryDataSet(newQuery[0]);
        logger.info("valor de la query  data set encabezado >>> "+newQuery[0]);
        
        //*******Modificamos el DataSet del Encabezado*************
        comp.obtenerComponentesDataSet("medico");
//        newQuery = comp.obtenerQueryDataSet().replace("1=2", solicitud.obtenerWhereFormatoMediaCarta(con, solicitarForm.getNumerosSolicitudes())+" "+ValoresPorDefecto.getValorLimit1()+" 1");        
        logger.info("valor de la query  data set medico >>> "+newQuery[2]);
        //if()
        comp.modificarQueryDataSet(newQuery[2]);
        
        //debemos arreglar los alias para que funcione oracle, debido a que este los convierte a MAYUSCULAS
      	comp.lowerAliasDataSet();
		String newPathReport = comp.saveReport1(false);
        comp.updateJDBCParameters(newPathReport);
        if(!newPathReport.equals(""))
        {
            request.setAttribute("isOpenReport", "true");
            request.setAttribute("newPathReport", newPathReport);
        }            
        UtilidadBD.closeConnection(con);
        solicitarForm.setEstado("resumenProcedimiento");
		}catch (Exception e) {
			logger.error("Error generando reporte"+e.getMessage());
		}
		return mapping.findForward("resumenSolicitudProcedimiento");
	}
	
	
	/**
	 * Metodo para la impresion de una solicitud exitosamente guardada
	 * @param con
	 * @param numeroSolicitud
	 * @param mapping
	 * @param usu
	 * @param pacienteActivo
	 * @param request
	 * @return
	 * @throws SQLException
	 */
	@SuppressWarnings("unused")
	private ActionForward accionImprimir(Connection con, int numeroSolicitud, ActionMapping mapping,UsuarioBasico usu,PersonaBasica pacienteActivo, HttpServletRequest request, InstitucionBasica institucionActual)throws SQLException 
    {
		SolicitudProcedimiento solicitud=new SolicitudProcedimiento();
		solicitud.cargarSolicitudProcedimiento(con, numeroSolicitud);
		solicitud.getImpresion();
        String nombreArchivo;
		Random r=new Random();
		
		nombreArchivo="/aBorrar" + r.nextInt()  +".pdf";
		
		ResumenGuardarProcedimientoPdf.pdfImprimirResumen(con, ValoresPorDefecto.getFilePath() +	nombreArchivo, solicitud,usu, pacienteActivo,true, institucionActual);
		request.setAttribute("nombreArchivo", nombreArchivo);
		request.setAttribute("nombreVentana", "Procedimientos");
		UtilidadBD.closeConnection(con);
		return mapping.findForward("abrirPdf");

    }
	/**
	* Mï¿½todo que llena los datos generales consignados por el usuario
	* (e inferidos) necesarios para la solicitud general.
	*
	* @param solicitarForm Form con los datos llenados por el usuario
	* @param paciente Paciente al que se le va a realizar la solicitud
	* @param medico Medico que estï¿½ realizando la solicitud
	* @return
	*/
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Solicitud llenarSolicitud (SolicitarForm solicitarForm, PersonaBasica paciente, UsuarioBasico medico)
	{

		//Por cuestiï¿½n de referencia, se maneja un objeto por cada
		//iteraciï¿½n
		Solicitud solicitudTemp=new Solicitud();
		solicitudTemp.setFechaSolicitud(solicitarForm.getFechaSolicitud());
		solicitudTemp.setHoraSolicitud(solicitarForm.getHoraSolicitud());
		solicitudTemp.setSolPYP(solicitarForm.isSolPYP());
		if(solicitarForm.getCodigoTipoSolicitud()==ConstantesBD.codigoServicioInterconsulta)
		{
			//Definimos el tipo de solicitud especï¿½fico para interconsulta y
			//su valor cobrable
			solicitudTemp.setTipoSolicitud(new InfoDatosInt (ConstantesBD.codigoTipoSolicitudInterconsulta, ""));
			solicitudTemp.setCobrable(true);
		}
		else if(solicitarForm.getCodigoTipoSolicitud()==ConstantesBD.codigoServicioProcedimiento)
		{
			//Definimos el tipo de solicitud especï¿½fico para procedimientos y
			//su valor cobrable
			solicitudTemp.setTipoSolicitud(new InfoDatosInt (ConstantesBD.codigoTipoSolicitudProcedimiento, ""));
			solicitudTemp.setCobrable(true);
		}
		//solicitudTemp.setNumeroAutorizacion("");		
		solicitudTemp.setEspecialidadSolicitante(new InfoDatosInt(solicitarForm.getEspecialidadSolicitante(), ""));
		solicitudTemp.setOcupacionSolicitado(new InfoDatosInt(solicitarForm.getOcupacionSolicitada(), ""));
		/*
		if(solicitarForm.getCodigoTipoSolicitud()==ConstantesBD.codigoServicioProcedimiento)
		{
			if(paciente.getCodigoUltimaViaIngreso()==ConstantesBD.codigoViaIngresoAmbulatorios)
				solicitudTemp.setCentroCostoSolicitante(new InfoDatosInt(Integer.parseInt(ValoresPorDefecto.getCentroCostoAmbulatorios(medico.getCodigoInstitucionInt())), ""));
			else if(paciente.getCodigoUltimaViaIngreso()==ConstantesBD.codigoViaIngresoConsultaExterna)
				solicitudTemp.setCentroCostoSolicitante(new InfoDatosInt(Integer.parseInt(ValoresPorDefecto.getCentroCostoConsultaExterna(medico.getCodigoInstitucionInt())), ""));
			else
				solicitudTemp.setCentroCostoSolicitante(new InfoDatosInt(medico.getCodigoCentroCosto(), ""));
		}
		else
			solicitudTemp.setCentroCostoSolicitante(new InfoDatosInt(medico.getCodigoCentroCosto(), ""));
		*/
		
		//Signacion del centro de costo del paciente.
		logger.info("ASGINANDO EL CENTRO COSTO QUE SOLICITA");
		if(solicitarForm.getUnicamenteExternos())
		{
			solicitudTemp.setCentroCostoSolicitante(new InfoDatosInt(medico.getCodigoCentroCosto(), ""));
		}
		else
		{
			solicitudTemp.setCentroCostoSolicitante(new InfoDatosInt(paciente.getCodigoArea(), ""));
		}
		solicitudTemp.setCentroCostoSolicitado(new InfoDatosInt(solicitarForm.getCentroCostoSolicitado(), ""));
		solicitudTemp.setCodigoMedicoSolicitante(medico.getCodigoPersona());
		solicitudTemp.setCodigoCuenta(paciente.getCodigoCuenta());
		solicitudTemp.setDatosMedico(medico.getInformacionGeneralPersonalSalud());
		
		// Mapa con las justificaciones ingresadas
		solicitudTemp.setJustificacionesMap(solicitarForm.getJustificacionesServicios());
		
		
		//No va a epicrisis porque todavï¿½a no ha sido ejecutada
		solicitudTemp.setVaAEpicrisis(false);

		if (solicitarForm.getSeleccionarServicio().equals("buscado"))
		{
			if (solicitarForm.getValores("urgente_" + 0)!=null&&((String)solicitarForm.getValores("urgente_" + 0)).equals("true"))
			{
				solicitudTemp.setUrgente(true);
			}
			else
			{
				solicitudTemp.setUrgente(false);
			}
		}
		else
		{
			solicitudTemp.setUrgente(solicitarForm.isUrgenteOtros());
		}
		Vector codigosNombresJustificaciones=Utilidades.buscarCodigosNombresJustificaciones(medico.getCodigoInstitucionInt(), false, false);
		Vector justificacionSolicitud=new Vector();
		for(int z=0; z<codigosNombresJustificaciones.size(); z++)
		{
			Vector atributo=(Vector)codigosNombresJustificaciones.elementAt(z);
			logger.info("\n\nMapa Valores=> "+solicitarForm.getValores());
			if(solicitarForm.getValores("just"+atributo.elementAt(0)+"_0")!=null)
			{
				String justificacion=((String)solicitarForm.getValores("just"+atributo.elementAt(0)+"_0"));
				Vector atributos=new Vector();
				atributos.add(new Integer(Integer.parseInt(atributo.elementAt(0)+"")));
				atributos.add(justificacion);
				justificacionSolicitud.add(atributos);
			}
		}

		if(!solicitarForm.getJustificacionSolicitud().trim().equals(""))
			solicitudTemp.setJustificacionSolicitud(UtilidadTexto.agregarTextoAObservacionFechaGrabacion("",solicitarForm.getJustificacionSolicitud(),medico,false));
		else
			solicitudTemp.setJustificacionSolicitud("");
		
		solicitudTemp.setJustificacion(justificacionSolicitud);
		return solicitudTemp;
	}
	
	
	/**
	 * Accion de imprimir un procedimiento externo
	 * @param con
	 * @param numeroSolicitud
	 * @param mapping
	 * @param usu
	 * @param pacienteActivo
	 * @param request
	 * @return
	 * @throws SQLException
	 */
	private ActionForward accionImprimirExterno(Connection con, int numeroSolicitud, ActionMapping mapping,UsuarioBasico usu,PersonaBasica pacienteActivo, HttpServletRequest request, SolicitarForm solicitarForm )throws SQLException 
	{
		SolicitudProcedimiento solicitud=new SolicitudProcedimiento();
		solicitud.cargarSolicitudProcedimiento(con, numeroSolicitud);
		solicitud.getImpresion();
		String nombreArchivo;
		Random r=new Random();
		nombreArchivo="/aBorrar" + r.nextInt()  +".pdf";
		
		ResumenGuardarProcedimientoPdf.pdfImprimirExterno(con, ValoresPorDefecto.getFilePath() +	nombreArchivo, solicitud,usu, pacienteActivo, solicitarForm);
		request.setAttribute("nombreArchivo", nombreArchivo);
		request.setAttribute("nombreVentana", "Procedimientos Externos");
		UtilidadBD.closeConnection(con);
		return mapping.findForward("abrirPdf");
	}
	
	/**
	 * Método que se encarga de validar la descripción del servicio pendiente por autorizar si falló en :
	 * La generación de autorizacion (DCU-1106) mostrar SERVICIO + ACRONIMO de dias tramite del grupo de servicio
	 * En el no de consecutivos disponibles y niveles (DCU-1115), mostrar SERVICIO + DIAS de tramite del grupo de servicios
	 * 
	 *  Cambio DCU 40 Version 1.52
	 *  
	 *  @author Camilo Gómez
	 */	
	private String validacionImpresionDescripcionServicio(int solicitud,int servicio,
			HttpServletRequest request,boolean procesoGeneracion)
	{		//
			//Paso la validacion de contrato=Capitado y capitacionSubcontratada='S' entonces 
			//aplica para autorizacion Capita Orden Ambulatoria
			InstitucionBasica ins = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
			IAutorizacionCapitacionOrdenesAmbulatoriasMundo autorizacionCapitacionOrdenesAmbulatoriasMundo=CapitacionFabricaMundo.crearAutorizacionCapitacionOrdenesAmbulatoriasMundo();
			DtoAutorizacionCapitacionOrdenAmbulatoria dtoAutorizacionCapitacionOrdenAmbulatoria=new DtoAutorizacionCapitacionOrdenAmbulatoria();
				dtoAutorizacionCapitacionOrdenAmbulatoria.setCodigoServicioAutorizar(servicio);//solicitudGenerada.getCodigoServicioCitaAutorizar());
				dtoAutorizacionCapitacionOrdenAmbulatoria.setNumeroSolicitudAutorizar(solicitud);//solicitudGenerada.getNumeroSolicitudCitaAutorizar());
				if(procesoGeneracion)
					dtoAutorizacionCapitacionOrdenAmbulatoria.setProcesoGeneracionAutoriz(true);//indica si viene desde las validaciones de -Generacion Autorizacion- se debe agregar a la descripcion, el ACRONIMO
				else
					dtoAutorizacionCapitacionOrdenAmbulatoria.setProcesoGeneracionAutoriz(false);//indica si viene desde las validaciones de -Consecutivos y Nivel- se debe agregar a la descripcion, los DIAS
			
			autorizacionCapitacionOrdenesAmbulatoriasMundo.validarDescripcionServicio(dtoAutorizacionCapitacionOrdenAmbulatoria, ins);
			
			return dtoAutorizacionCapitacionOrdenAmbulatoria.getNombreServicioAutorizar();
	}

	
	
	
	/**
	 * Hace el llamado al método que verifica si existe una autorización asociada.
	 * Para este flujo (SolicitarAction y SolicitarForm) se está tomando mal el codigo de la orden ya que se está
	 * utilizando para todas las consultas es consecutivo. Lo que se hace en este metodo es que se obtiene el código 
	 * de la orden a partir del consecutivo mientras se hace la consulta y despues se deja todo como venia
	 * 
	 * MT: 1707
	 * 
	 * @param dtoAutorizCapitaOrdenAmbu
	 * @return DtoAutorizacionCapitacionOrdenAmbulatoria
	 *
	 * @autor Cristhian Murillo
	 */
	private ArrayList<DtoAutorizacionCapitacionOrdenAmbulatoria> consultarExisteAutorizacionCapitaOrdenAmbulatoria(DtoAutorizacionCapitacionOrdenAmbulatoria dtoAutorizCapitaOrdenAmbu)
	{
		ArrayList<DtoAutorizacionCapitacionOrdenAmbulatoria>listaAutorizaciones=null;
		try{
			/* Se obtiene el código de la orden a partir del consecutivo mientras se hace la consulta y despues se deja todo como venia */
			IOrdenesAmbulatoriasMundo ordenesAmbulatoriasMundo	=OrdenesFabricaMundo.crearOrdenesAmbulatoriasMundo();		
			ArrayList<com.servinte.axioma.orm.OrdenesAmbulatorias> listaOrdenesAmbulatorias = new  ArrayList<com.servinte.axioma.orm.OrdenesAmbulatorias>(); 
			String numeroOrdenTemp = dtoAutorizCapitaOrdenAmbu.getDtoOrdenesAmbulatorias().getNumeroOrden();
			DtoOrdenesAmbulatorias dtoOrdenesAmbulatorias = new DtoOrdenesAmbulatorias();
			dtoOrdenesAmbulatorias.setConsecutivoOrden(dtoAutorizCapitaOrdenAmbu.getDtoOrdenesAmbulatorias().getNumeroOrden());
			listaOrdenesAmbulatorias = ordenesAmbulatoriasMundo.buscarPorParametros(dtoOrdenesAmbulatorias);
			dtoAutorizCapitaOrdenAmbu.getDtoOrdenesAmbulatorias().setNumeroOrden(listaOrdenesAmbulatorias.get(0).getCodigo()+"");
			dtoAutorizCapitaOrdenAmbu.setEstadoAutorizacion(ConstantesIntegridadDominio.acronimoAutorizado);

			listaAutorizaciones	= ordenesAmbulatoriasMundo.existeAutorizacionCapitaOrdenAmbulatoria(dtoAutorizCapitaOrdenAmbu);

			if(!Utilidades.isEmpty(listaAutorizaciones))
			{
				for (DtoAutorizacionCapitacionOrdenAmbulatoria ordenesAmbulatoria : listaAutorizaciones) {
					if(dtoAutorizCapitaOrdenAmbu != null){
						ordenesAmbulatoria.getDtoOrdenesAmbulatorias().setNumeroOrden(numeroOrdenTemp);
					}
				}
			}
		}catch(Exception e){
			Log4JManager.error("Existen Autorizaciones Capitacion Orden Ambulatoria: " + e);
		}
		return listaAutorizaciones;
	}
		
	/**
	 * Método que se encarga de validar si se generar autorizacion para el servicio de la orden.
	 * DCU 40 - Solicitud de Procedimientos v2.1
	 * MT 4681
	 * 
	 * @author Camilo Gomez
	 * @param con
	 * @param solicitarForm
	 * @param usuario
	 * @param errores
	 * @throws IPSException
	 */
	private void cargarInfoVerificarGeneracionAutorizacion(Connection con,SolicitarForm solicitarForm,
			UsuarioBasico usuario, PersonaBasica paciente, ActionErrors errores)throws IPSException
	{
		DtoSubCuentas dtoSubCuenta = null;
		SolicitudProcedimiento mundoSolicitudProced	= null;
		DtoProcedimiento solicitudProcedimiento 	= null;
		AutorizacionCapitacionDto autorizacionCapitacionDto = null;
		MontoCobroDto montoCobroAutorizacion				= null;
		DatosPacienteAutorizacionDto datosPacienteAutorizar = null;
		OrdenAutorizacionDto ordenAutorizacionDto 			= null;
		List<OrdenAutorizacionDto> listaOrdenesAutorizar	= null;
		List<ServicioAutorizacionOrdenDto> listaServiciosPorAutorizar = null;
		ContratoDto contratoDto = null;
		ConvenioDto convenioDto = null;
		int codigoTipoTarifario = 0;
		//DtoSolicitudesSubCuenta dtoSolicitudSubCuenta = null; 
		ManejoPacienteFacade manejoPacienteFacade = null;
		OrdenesFacade ordenesFacade	= null;
		List<AutorizacionCapitacionDto> listaAutorizacionCapitacion = null;
		try{
			UtilidadBD.iniciarTransaccion(con);
			mundoSolicitudProced	= new SolicitudProcedimiento();
			listaOrdenesAutorizar 	= new ArrayList<OrdenAutorizacionDto>();
			ordenesFacade			= new OrdenesFacade();
						
	        String codigoTarifario = ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(usuario.getCodigoInstitucionInt());
			if(UtilidadTexto.isNumber(codigoTarifario)){
				codigoTipoTarifario = Integer.parseInt(codigoTarifario);
			}
			
			dtoSubCuenta =  solicitarForm.getInfoCoberturaServicio().get(0).getDtoSubCuenta();
			
			for(InfoResponsableCobertura infoCoberturaSer : solicitarForm.getInfoCoberturaServicio()){
				/*
				 * MT 6022
				 */
				
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
				
				for (DtoSolicitudesSubCuenta solicitudesGeneradas : infoCoberturaSer.getDtoSubCuenta().getSolicitudesSubcuenta()){
					//dtoSolicitudSubCuenta = infoCoberturaSer.getDtoSubCuenta().getSolicitudesSubcuenta().get(0);
					
					String numeroSolicitud = solicitudesGeneradas.getNumeroSolicitud();
					if (solicitarForm.isEscruento()) // los servicios no curentos no generan a autorizacion de esta manera, y mandan error al ejectuirar este metodo
					{ 
						DtoProcedimiento solicitudProcedimientoT 	= null;
					boolean mtg=true;
					solicitudProcedimiento = mundoSolicitudProced.buscarServiciosSolicitudProcedimientosC(
		    				con, Utilidades.convertirAEntero(numeroSolicitud+""), codigoTipoTarifario);
					
					
					}
					else{
						solicitudProcedimiento = mundoSolicitudProced.buscarServiciosSolicitudProcedimientos(
			    				con, Utilidades.convertirAEntero(numeroSolicitud+""), codigoTipoTarifario);
					}
				ordenAutorizacionDto = new OrdenAutorizacionDto();
				ordenAutorizacionDto.setCodigoOrden(Utilidades.convertirALong(numeroSolicitud+""));
					ordenAutorizacionDto.setConsecutivoOrden(solicitudesGeneradas.getConsecutivoSolicitud());
				ordenAutorizacionDto.setContrato(contratoDto);
				ordenAutorizacionDto.setCodigoCentroCostoEjecuta(solicitudProcedimiento.getCodigoCentroCostoSolicitado());
				ordenAutorizacionDto.setEsPyp(solicitarForm.isSolPYP());
				ordenAutorizacionDto.setCodigoViaIngreso(paciente.getCodigoUltimaViaIngreso());
				
					ordenAutorizacionDto.setClaseOrden(ConstantesBD.claseOrdenOrdenMedica);
				ordenAutorizacionDto.setTipoOrden(ConstantesBD.codigoTipoSolicitudProcedimiento);
				
				//Se consultan datos del servicio
				int tipoOrdena=0;
				tipoOrdena=ordenAutorizacionDto.getTipoOrden();
				listaServiciosPorAutorizar = null;
				if (solicitarForm.isEscruento()) // los servicios no curentos no generan a autorizacion de esta manera, y mandan error al ejectuirar este metodo
				{	
					tipoOrdena=ConstantesBD.codigoTipoSolicitudCirugia;
				}

				listaServiciosPorAutorizar = ordenesFacade.obtenerServiciosPorAutorizar(Utilidades.convertirAEntero(ordenAutorizacionDto.getCodigoOrden()+""),
						ordenAutorizacionDto.getClaseOrden(), tipoOrdena);
				//listaServiciosPorAutorizar.get(0).setFinalidad(dtoSolicitudSubCuenta.getFinalidadSolicitud());

				long cantidad = 1;
				listaServiciosPorAutorizar.get(0).setCantidad(cantidad);
				listaServiciosPorAutorizar.get(0).setAutorizar(true);
				if(solicitudesGeneradas.isUrgenteSolicitud()){
					ordenAutorizacionDto.setEsUrgente(true);
					listaServiciosPorAutorizar.get(0).setUrgente(ConstantesBD.acronimoSiChar);
				}else{
					ordenAutorizacionDto.setEsUrgente(false);
					listaServiciosPorAutorizar.get(0).setUrgente(ConstantesBD.acronimoNoChar);
				}
				
				if(solicitarForm.isIndicativoOrdenAmbulatoria()){
					ordenAutorizacionDto.setClaseOrden(ConstantesBD.claseOrdenOrdenAmbulatoria);
					String codigoOA=Utilidades.obtenerCodigoOrdenAmbulatoria(con,solicitarForm.getOrdenAmbulatoria(),usuario.getCodigoInstitucionInt());
					ordenAutorizacionDto.setOtroCodigoOrden(Utilidades.convertirALong(codigoOA));
					
				}else if(solicitarForm.isSolPYP()){
					ordenAutorizacionDto.setClaseOrden(ConstantesBD.claseOrdenOrdenAmbulatoria);
					ordenAutorizacionDto.setTipoOrden(ConstantesBD.codigoTipoOrdenAmbulatoriaServicios);
					
				}else{
					ordenAutorizacionDto.setClaseOrden(ConstantesBD.claseOrdenOrdenMedica);
				}
				
				ordenAutorizacionDto.setServiciosPorAutorizar(listaServiciosPorAutorizar);
				listaOrdenesAutorizar.add(ordenAutorizacionDto);
			}	
			}	
			boolean manejaMonto = UtilidadTexto.isEmpty(dtoSubCuenta.getMontoCobro()) || dtoSubCuenta.getMontoCobro() == 0 ? false : true;

			
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
			
			if(listaAutorizacionCapitacion!=null && !listaAutorizacionCapitacion.isEmpty()){//Se adiciona mensaje para los servicio que no se autorizaron
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
	
}