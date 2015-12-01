package com.princetonsa.action.odontologia;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.jasperreports.engine.JasperPrint;

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
import util.InfoDatosDouble;
import util.InfoDatosInt;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.manejoPaciente.UtilidadesManejoPaciente;
import util.odontologia.InfoServicios;
import util.odontologia.UtilidadOdontologia;

import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.odontologia.EvolucionOdontologicaForm;
import com.princetonsa.actionform.odontologia.ValoracionOdontologicaForm;
import com.princetonsa.dto.historiaClinica.parametrizacion.DtoPlantilla;
import com.princetonsa.dto.odontologia.DtoAntecendenteOdontologico;
import com.princetonsa.dto.odontologia.DtoPlanTratamientoOdo;
import com.princetonsa.dto.odontologia.DtoProcesoCitaProgramada;
import com.princetonsa.dto.odontologia.DtoProgramasServiciosPlanT;
import com.princetonsa.dto.odontologia.DtoServicioCitaOdontologica;
import com.princetonsa.enu.impresion.EnumTiposSalida;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.historiaClinica.Plantillas;
import com.princetonsa.mundo.odontologia.AtencionCitasOdontologia;
import com.princetonsa.mundo.odontologia.ComponenteAnteOdoto;
import com.princetonsa.mundo.odontologia.ComponenteIndicePlaca;
import com.princetonsa.mundo.odontologia.ComponenteOdontogramaEvo;
import com.princetonsa.mundo.odontologia.EvolucionOdontologica;
import com.princetonsa.mundo.odontologia.PlanTratamiento;
import com.servinte.axioma.fwk.exception.BDException;
import com.servinte.axioma.generadorReporte.odontologia.plantillasOdontologicas.GeneradorReportePlantillasOdontologicas;
import com.servinte.axioma.orm.CentroAtencion;
import com.servinte.axioma.persistencia.UtilidadPersistencia;
import com.servinte.axioma.persistencia.UtilidadTransaccion;
import com.servinte.axioma.servicio.fabrica.odontologia.OdontologiaServicioFabrica;
import com.servinte.axioma.servicio.impl.administracion.CentroAtencionServicio;
import com.servinte.axioma.servicio.interfaz.odontologia.agendaOdontologica.ICitaOdontologicaServicio;

/**
 * 
 * @author axioma
 *
 */
public class EvolucionOdontologicaAction extends Action
{
	private Logger logger = Logger.getLogger(EvolucionOdontologicaAction.class);
	
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	{
		Connection con=null;
		try{
			if(form instanceof EvolucionOdontologicaForm)
			{
				EvolucionOdontologicaForm forma=(EvolucionOdontologicaForm)form;
				String estado=forma.getEstado();

				HttpSession session=request.getSession();
				ActionErrors errores = new ActionErrors();
				UsuarioBasico usuario = (UsuarioBasico)session.getAttribute("usuarioBasico");
				PersonaBasica paciente = (PersonaBasica)session.getAttribute("pacienteActivo");

				logger.info("valor del estado >> "+estado);
				forma.setAncla("");

				if(estado==null)
				{
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					return mapping.findForward("paginaError");
				}
				else if(estado.equals("empezar"))
				{
					String  multiplo=ValoresPorDefecto.getMultiploMinGeneracionCita(usuario.getCodigoInstitucionInt());
					forma.setRedireccion("redireccionEmpezar");
					if(!UtilidadTexto.isEmpty(multiplo)) 
					{
						forma.setMutiploMinutosGeneracionCita(Utilidades.convertirAEntero(multiplo));
					}


					if (paciente.getCodigoPersona()<1)
					{
						request.setAttribute("codigoDescripcionError", "errors.paciente.noCargado");
						return mapping.findForward("paginaError");     
					}
					else
					{
						con=UtilidadBD.abrirConexion();	
						accionEmpezar(forma,con,paciente,usuario,request);
						try
						{
							UtilidadBD.cerrarConexion(con);
						} catch (SQLException e)
						{
							logger.error("Error cerrando la conexión",e);
						}
						return mapping.findForward("principal");
					}
				}
				else if (forma.getEstado().equals("abrirPlantilla"))
				{
					return redireccionarPlantilla(forma, response, request, usuario);
				}
				else if(forma.getEstado().equals("iniciarConfirmacion"))
				{ 
					return accionIniciarConfirmacion(forma,usuario,mapping);
				}
				else if (forma.getEstado().equals("validarUsuario"))
				{
					return accionValidarUsuario(forma,usuario,mapping, request, paciente);
				}
				else if(forma.getEstado().equals("guardarConfirmacion"))
				{
					/*
					 *Es requerido programar citas
					 */

					return accionGuardarConfirmacion(forma,usuario,mapping,request, paciente,errores);
				}
				else if (forma.getEstado().equals("irResumen"))
				{
					forma.setRedireccion("redireccionResumen");
					con=UtilidadBD.abrirConexion();	
					cargarResumenEvolucion(forma, con, paciente, usuario, errores, request, mapping);
					try
					{
						UtilidadBD.cerrarConexion(con);
					} catch (SQLException e)
					{
						logger.error("Error cerrando la conexión",e);
					}
					return mapping.findForward("resumen");
				}
				else if(estado.equals("odontograma"))
				{
					con=UtilidadBD.abrirConexion();
					String forward=accionOdontograma(con,paciente,usuario,forma,request,errores);
					try
					{
						UtilidadBD.cerrarConexion(con);
					} catch (SQLException e)
					{
						logger.error("Error cerrando la conexión",e);
					}
					forma.setAncla("odontograma");
					return mapping.findForward(forward);

				}

				else if(estado.equals("indicePlaca"))
				{
					con=UtilidadBD.abrirConexion();
					accionIndicePlaca(paciente, forma, usuario, request, con);
					request.setAttribute("nombreAtributoIndicePlaca","compIndicePlaca");
					request.setAttribute("nombreAtributoPlantillaHistorico","dtoEvolucionOdo.codigoPlantillaEvolucion");
					request.setAttribute("nombreForm","EvolucionOdontologicaForm");
					request.setAttribute("pathAction","/evolucionOdontologica/evolucion.do");
					try
					{
						UtilidadBD.cerrarConexion(con);
					} catch (SQLException e)
					{
						logger.error("Error cerrando la conexión",e);
					}
					forma.setAncla("indicePlaca");
					return mapping.findForward(forma.getCompIndicePlaca().getForward());
				}
				else if(estado.equals("imprimir"))
				{
					con=UtilidadBD.abrirConexion();
					forma.setNombreArchivoGenerado(null);
					forma.setEnumTipoSalida(null);
					return accionImprimir(con,forma,usuario,mapping,request);
				}
				else if (forma.getEstado().equals("imprimirPDF"))
				{
					return accionImprimirPDF(forma,usuario,mapping,request);
				}
				else if(forma.getEstado().equals("consultaHistoria"))
				{
					request.setAttribute("nombreForm", "AtencionCitasOdontologiaForm");
					try 
					{
						response.sendRedirect("../atencionCitasOdontologia/atencionCitasOdontologia.do?estado=consultaHistoria&volver="+forma.getRedireccion()+"&codigoPlantilla="+forma.getCodigoPlantilla());
					} 
					catch (IOException e) 
					{
						Log4JManager.error("Error tratando de redireccionar la atencion odontologica: ",e);
					}
					return null;
				}
				else if(estado.equals("antecedenteOdonto"))
				{
					con=UtilidadBD.abrirConexion();
					ActionForward forward=accionAntecedenteOdontologico(mapping,paciente,forma,usuario,request,con);
					try
					{
						UtilidadBD.cerrarConexion(con);
					} catch (SQLException e)
					{
						logger.error("Error cerrando la conexión",e);
					}
					forma.setAncla("antecedentes");
					return forward;
				}
				else if(estado.equals("consultaFacturaAutomatica"))
				{
					if(forma.getFacturasAutomaticas().getErroresFactura().size()>0)
					{
						request.setAttribute("ocultarEncabezadoErrores", "true");
						request.setAttribute("utilizaParent", true);
						return ComunAction.envioMultiplesErrores(mapping, request, null, "label.facturacion.facturacionOdontologicaCancelada", forma.getFacturasAutomaticas().getErroresFactura(), logger);
					}

				}else if (forma.getEstado().equals("volverDesdeProcesoProximaCita")){

					String forward = null;

					if(request.getSession().getAttribute("codigoProximaCitaRegistrada")!=null && UtilidadTexto.isNumber(request.getSession().getAttribute("codigoProximaCitaRegistrada")+"")){

						int codigoProximaCitaRegistrada = (Integer) request.getSession().getAttribute("codigoProximaCitaRegistrada");
						request.getSession().removeAttribute("codigoProximaCitaRegistrada");

						forma.setCodigoProximaCitaRegistrada(codigoProximaCitaRegistrada);

						forward = verificarRegistroProximaCita(mapping, forma, usuario, paciente, request, errores);
					}

					if(forward!=null){

						/*
						 * Se registraron errores, por lo que se debe eliminar el registro generado de próxima cita.
						 */
						if(!errores.isEmpty()){

							eliminarRegistroProximaCita(forma);
						}

						forma.setEstadoTemporal(forward);

					}else{

						forma.setEstadoTemporal("principal");
					}

					return mapping.findForward("cerrarPopUpProximaCitaProgramada");

				}else if (forma.getEstado().equals("cargarEstadoTemporal")){

					return mapping.findForward(forma.getEstadoTemporal());

				}else if (forma.getEstado().equals("volver")) {
					forma.setSecSeleccionarFormulario(ConstantesBD.acronimoNo);
					return mapping.findForward("principal");
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
	
	
	/**
	 * Método para manejar los estados de los antecedentes odontológicos
	 * @param mapping
	 * @param paciente
	 * @param forma
	 * @param usuario
	 * @param request
	 * @param con
	 * @return
	 */
	private ActionForward accionAntecedenteOdontologico(ActionMapping mapping,
			PersonaBasica paciente, EvolucionOdontologicaForm forma,
			UsuarioBasico usuario, HttpServletRequest request, Connection con) 
	{
		forma.getInfoCompAnteOdont().setUtilizaProgOdonto(ValoresPorDefecto.getUtilizanProgramasOdontologicosEnInstitucion(usuario.getCodigoInstitucionInt()));
		logger.info("Estado para el Antecedente odontologico >> "+forma.getEstadoSecundario());
		logger.info("mostrar por: "+forma.getInfoCompAnteOdont().getMostrarPor());
		if(forma.getEstadoSecundario().equals("empezar"))
		{
			accionEmpezarAnteOdo(request, forma, usuario, paciente, con);
		}else{
		
			ActionErrors errores=ComponenteAnteOdoto.accionesEstados(forma.getInfoCompAnteOdont(),forma.getEstadoSecundario(),true);
			//anteOdonto.cargarComponenteAnteOdonto(forma.getInfoCompAnteOdont());
		
			saveErrors(request,errores);
		
			accionParametrosReqAnteOdo(request);
			
		}
		forma.setEstadoSecundario("");
		logger.info("forward: "+forma.getInfoCompAnteOdont().getForward());
		return mapping.findForward(forma.getInfoCompAnteOdont().getForward());
	}

	private void accionEmpezarAnteOdo(HttpServletRequest request,
			EvolucionOdontologicaForm forma, UsuarioBasico usuario,
			PersonaBasica paciente, Connection con) 
	{
		forma.setEstadoSecundario("empezar");
		logger.info("codigo paciente: "+paciente.getCodigoPersona());
		ActionErrors errores=ComponenteAnteOdoto.llenadoInicialInterfaz(forma.getInfoCompAnteOdont(), con);
		if(errores.isEmpty())
		{
			// con este parámetro sabemos que tipo de antecedente realizar
			forma.getInfoCompAnteOdont().setPorConfirmar(forma.getPorActualizar());
			
			if(forma.getPorActualizar().equals(ConstantesBD.acronimoSi))
				forma.getInfoCompAnteOdont().setCodigoAnteOdon(Utilidades.convertirAEntero(Double.toString(forma.getDtoEvolucionOdo().getCodigoPk())));
			
			// se verifica y se llena el parámetro general utiliza programa odontológico
			forma.getInfoCompAnteOdont().setUtilizaProgOdonto(ValoresPorDefecto.getUtilizanProgramasOdontologicosEnInstitucion(usuario.getCodigoInstitucionInt()));
			
			forma.getInfoCompAnteOdont().setCodigoPaciente(paciente.getCodigoPersona());
			forma.getInfoCompAnteOdont().setMostrarPor(UtilidadTexto.getBoolean(forma.getInfoCompAnteOdont().getUtilizaProgOdonto())
					?ConstantesIntegridadDominio.acronimoMostrarProgramas
					:ConstantesIntegridadDominio.acronimoMostrarServicios);
			
			// control de los acciones internas del componente
			errores=ComponenteAnteOdoto.accionesEstados(forma.getInfoCompAnteOdont(),forma.getEstadoSecundario(), true);
			
			accionParametrosReqAnteOdo(request);
		}
		saveErrors(request,errores);
		
	}

	/**
	 * Método para realizar la validación de usuarios al confirmar la cita
	 * @param forma
	 * @param usuario
	 * @param mapping
	 * @param request
	 * @param paciente 
	 * @return
	 */
	private ActionForward accionValidarUsuario(EvolucionOdontologicaForm forma,
			UsuarioBasico usuario, ActionMapping mapping,
			HttpServletRequest request, PersonaBasica paciente) 
	{
		AtencionCitasOdontologia mundoAtencion = new AtencionCitasOdontologia();
		logger.info("llama al mundo a validar usuario");
		mundoAtencion.validarUsuarioFinConfirmacion(forma.getDtoCita(), usuario, forma.getDtoAtencionCita());
		logger.info("***************llego del mundo******************************");
		if(!mundoAtencion.getErrores().isEmpty())
		{
			saveErrors(request, mundoAtencion.getErrores());
			forma.setEstado("iniciarConfirmacion");
			return mapping.findForward("popUpConfirmacion");
		
		}
//		else if(ValoresPorDefecto.getRequeridoProgramarProximaCitaEnAtencion(usuario.getCodigoInstitucionInt()).equals(ConstantesBD.acronimoSi)){
//			
//			forma.setEstado("");
//			return mapping.findForward(programarProximaCita(forma, request, paciente));
//
//		}
		else{
			
			return mapping.findForward("popUpConfirmacion");
		}
	}
	
	/**
	 * M&eacute;todo implementado para realizar la impresi&oacute;n de la atenci&oacute;n de la cita
	 * @param con
	 * @param forma
	 * @param usuario
	 * @param mapping
	 * @param request
	 * @return
	 */
	private ActionForward accionImprimir(Connection con,
			EvolucionOdontologicaForm forma, UsuarioBasico usuario,
			ActionMapping mapping, HttpServletRequest request) 
	{
		//Se toma la información de la institución básica
		forma.setInstitucionBasica((InstitucionBasica)request.getSession().getAttribute("institucionBasica"));
		logger.info("Evolucion CODIGO CITA >>"+forma.getCita() +" >> "+forma.getDtoEvolucionOdo().getCita());
		AtencionCitasOdontologia mundoAtencion = new AtencionCitasOdontologia();
		mundoAtencion.capturarDatosImpresion(con, new BigDecimal(forma.getCita()), usuario);
		CentroAtencionServicio centroAtencionServicio = new CentroAtencionServicio();
		CentroAtencion centroAtencion = new CentroAtencion();
		centroAtencion = centroAtencionServicio.buscarPorCodigoPK(usuario.getCodigoCentroAtencion());
		forma.getInstitucionBasica().setDireccion(centroAtencion.getDireccion());
		forma.getInstitucionBasica().setTelefono(centroAtencion.getTelefono());
		
		forma.setPaciente(mundoAtencion.getPaciente());
		forma.setPlantillas(mundoAtencion.getPlantillas());
		
		forma.setUsuarioResumen(usuario);
		forma.setFechaResumen(UtilidadFecha.getFechaActual(con));
		forma.setHoraResumen(UtilidadFecha.getHoraActual(con));
		forma.setTitulo("INFORMACIÓN DE LA CITA ODONTOLÓGICA");
		if(!Utilidades.isEmpty(forma.getDtoAtencionCita().getPlantillas()))
			forma.setPlantillaBase(forma.getDtoAtencionCita().getPlantillas().get(0).getNombreFuncionalidad());
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("imprimir");
	}
	
	/**
	 * Método implementado para realizar la impresión de las plantillas
	 * atención de la cita en PDF
	 * @param forma
	 * @param usuario
	 * @param mapping
	 * @param request 
	 * @return
	 */
	private ActionForward accionImprimirPDF(EvolucionOdontologicaForm forma, UsuarioBasico usuario,ActionMapping mapping, HttpServletRequest request) 
	{
		String nombreArchivo="";
                	
		GeneradorReportePlantillasOdontologicas generadorReporte=null;
		JasperPrint reporte=null;
		generadorReporte = new GeneradorReportePlantillasOdontologicas(forma,forma.getPlantillas());
	            		reporte = generadorReporte.generarReporte();
	                    forma.setEnumTipoSalida(EnumTiposSalida.PDF);
	                    
	    nombreArchivo = generadorReporte.exportarReportePDF(reporte, "PlantillasOdontologicas");
	    forma.setNombreArchivoGenerado(nombreArchivo);
	  
		return mapping.findForward("imprimir");
	}

	/**
	 * M&eacute;todo implementado para manejar los procesos del &iacute;ndice de placa
	 * @param mapping
	 * @param paciente
	 * @param forma
	 * @param usuario
	 * @param request
	 * @param con
	 * @return
	 */
	private void accionIndicePlaca(PersonaBasica paciente, EvolucionOdontologicaForm forma,UsuarioBasico usuario, HttpServletRequest request, Connection con) 
	{
		logger.info("segundo Estado. "+forma.getEstadoSecundario());
		if(forma.getEstadoSecundario().equals("empezar"))
		{
			// inicializar los atributos de edad y codigo de institucion
			forma.getCompIndicePlaca().setPlantillaEvolucionOdo(forma.getDtoEvolucionOdo().getCodigoPlantillaEvolucion().intValue());
			forma.getCompIndicePlaca().setCodigoPaciente(paciente.getCodigoPersona());
			forma.getCompIndicePlaca().setEdadPaciente(paciente.getEdad());
			forma.getCompIndicePlaca().setCodigoInstitucion(usuario.getCodigoInstitucionInt());
			forma.getCompIndicePlaca().setUsuarioModifica(usuario.getLoginUsuario());
			forma.getCompIndicePlaca().setPorActualizar(forma.getPorActualizar());
		}
		
		ComponenteIndicePlaca.accionesEstados(forma.getCompIndicePlaca(),forma.getEstadoSecundario());
		
		forma.setEstadoSecundario("");
		
	}

	/**
	 * 
	 */
	public ActionForward cargarResumenEvolucion (EvolucionOdontologicaForm forma,Connection con,PersonaBasica paciente,UsuarioBasico usuario, ActionErrors errores, HttpServletRequest request, ActionMapping mapping)
	{
		BigDecimal ingreso = UtilidadOdontologia.consultarIngresoCitaOdontologica(con, new BigDecimal(forma.getCita()));
		///Cargo la plantilla que acabo de ingresar
		logger.info("\n\n\n\nENTRE A CONSULTAR LA PLANTILLA!!!!!!!!");
		//Equeda pendiente este resumen
		forma.setPlantilla(Plantillas.cargarPlantillaEvolucionOdon(con, usuario.getCodigoInstitucionInt(), usuario, paciente.getCodigoPersona(), "", forma.getCodigoPlantilla(),ingreso.intValue(),forma.getDtoEvolucionOdo().getCodigoPk()));
		
		return mapping.findForward("resumen");
	}
	
	/**
	 * Método para insertar la plantilla de evolución
	 */
	public ActionErrors accionInsertar(EvolucionOdontologicaForm forma,Connection con,PersonaBasica paciente,UsuarioBasico usuario, ActionErrors errores)
	{	
		//lleno el dto de la evolución para hacer la inserción
		forma.getDtoEvolucionOdo().setCita(forma.getCita());
		forma.getDtoEvolucionOdo().setFechaModifica(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
		forma.getDtoEvolucionOdo().setHora(UtilidadFecha.getHoraActual());
		forma.getDtoEvolucionOdo().setUsuario(usuario.getLoginUsuario());
		
		//Lleno el mundo de la evolución
		EvolucionOdontologica mundoEvolucion= new EvolucionOdontologica();
		mundoEvolucion.setDtoEvolucion(forma.getDtoEvolucionOdo());
		mundoEvolucion.setDtoPlantilla(forma.getPlantilla());
		mundoEvolucion.setInfoOdontograma(forma.getInfoCompOdont());
		mundoEvolucion.setInfoAntecedenteOdonto(forma.getInfoCompAnteOdont());
		mundoEvolucion.setUsuario(usuario);
		mundoEvolucion.setDtoCita(forma.getDtoCita());
		
		//************************* COMPONENTE INDICE DE PLACA ***************************
		forma.getCompIndicePlaca().setPorConfirmar(forma.getPorConfirmar());
		
		if(forma.getCompIndicePlaca().getCodigoPk() > 0){
			
			forma.getCompIndicePlaca().setPorActualizar(forma.getPorActualizar());
		
		}else{
			
			forma.getCompIndicePlaca().setPorActualizar(ConstantesBD.acronimoNo);
		}

		mundoEvolucion.setIndicePlaca(forma.getCompIndicePlaca());
		//************************* FIN COMPONENTE INDICE DE PLACA ***********************
		
		/*
		 * Antes de registrar la evolución se debe guardar este objeto para poder
		 * realizar el proceso de programación de la próxima cita.
		 */
		forma.getDtoProcesoCitaProgramada().setInfoPlanTratamiento(forma.getInfoCompOdont().getInfoPlanTrata());

		//Si se va a confirmar la cita se valida la información requerida
		if(UtilidadTexto.getBoolean(forma.getPorConfirmar()))
		{
			//***************VALIDACION DE CAMPOS REQUERIDOS********************************************************
			//Se pregunta si la plantilla tiene el componente de evolucion
			if(mundoEvolucion.getDtoPlantilla().existeComponente(ConstantesCamposParametrizables.tipoComponenteOdontogramaEvol))
			{
				mundoEvolucion.getInfoOdontograma().getInfoPlanTrata().validateOdontogramaEvolucion( errores,UtilidadTexto.getBoolean(mundoEvolucion.getInfoOdontograma().getEsBuscarPorPrograma()));
			}
			
			errores = Plantillas.validacionCamposPlantilla(forma.getPlantilla(), errores);
			//*******************************************************************************************************
		}
		
		if(errores.isEmpty())
		{
		
			logger.info("**************************INICIO INSERTAR EVOLUCION ************************************");
			
			mundoEvolucion.insertarEvolucion(con, paciente, UtilidadTexto.getBoolean(forma.getPorConfirmar())?ConstantesBD.acronimoNo:ConstantesBD.acronimoSi);

			/*
			 * Antes de registrar la evolución se debe guardar este objeto para poder
			 * realizar el proceso de programación de la próxima cita.
			 */
			forma.getDtoProcesoCitaProgramada().setInfoPlanTratamiento(mundoEvolucion.getInfoOdontograma().getInfoPlanTrata());

			
			logger.info("Genero las solicitudes de servicios incluidos para la próxima cita");
			
			//TAREA XPLANNER 161019, SE GENERA CARGO EN LA ASIGNACION, EN LA ATENCION NO SE DEBE INSERTAR SOLICITUD NI CARGO DEBIDO A QUE YA EXISTE SOLICITUD
			//LO UNICO QUE SE DEBE HACER ES CAMBIAR EL ESTADO DE HISTORIA CLINICA A INTERPRETADA
			
			/*for(InfoServicios serv:forma.getInfoCompOdont().getArrayServProxCita())
			{
				logger.info("Entro a for "+serv.getServicio().getCodigo());
				int codOcupacionMedica = Utilidades.obtenerOcupacionMedica(con, usuario.getCodigoPersona());
				generarSolicitudesServiciosIncluidos(con, serv, forma.getDtoCita(), codOcupacionMedica, paciente, usuario);
			}*/
			UtilidadBD.closeConnection(con);

			logger.info("**************************FIN INSERTAR EVOLUCION ************************************"+errores);
			errores = mundoEvolucion.getErrores();
			if(errores.isEmpty())
			{
			
				//Se toma de nuevo el plan de tratamiento
				forma.setInfoCompOdont(mundoEvolucion.getInfoOdontograma());
				
				//se toma el nuevo indice de placa.
				forma.setCompIndicePlaca(mundoEvolucion.getIndicePlaca());
				
				//Se marca la plantilla como si ya no estuviera en proceso
				for(DtoPlantilla plantilla:forma.getDtoAtencionCita().getPlantillas())
				{
					if(plantilla.getCodigoPK().equals(forma.getPlantilla().getCodigoPK()))
					{
						plantilla.setPlantillaEnProceso(false);
					}
				}
			}
			
			logger.info("********NO entra al if ********");
		}
		
		
		return errores;
	}
	
	
//	/**
//	 * 
//	 * @param con
//	 * @param servicio
//	 * @param citaTemp
//	 * @param codOcupacionMedica
//	 * @param paciente
//	 * @param usuario
//	 */
//	@SuppressWarnings("unused")
//	private void generarSolicitudesServiciosIncluidos(Connection con, InfoServicios servicio, DtoCitaOdontologica citaTemp, int codOcupacionMedica, PersonaBasica paciente, UsuarioBasico usuario)
//	{
//		ActionErrors errores=new ActionErrors();
//		String fechaActual = UtilidadFecha.getFechaActual(con);
//		String horaActual = UtilidadFecha.getHoraActual(con);
//
//		//2) Generación de las solicitudes para los servicios incluidos
//		DtoServiPpalIncluido servicioPrincipal = new DtoServiPpalIncluido();
//		servicioPrincipal.getServicio().setCodigo(servicio.getServicio().getCodigo());
//		servicioPrincipal.setInstitucion(usuario.getCodigoInstitucionInt());
//		servicioPrincipal.setAtencionOdontologica(true);
//		
//		UtilidadesFacturacion.cargarServiciosArticulosIncluidos(con, servicioPrincipal);
//		
//		
//		logger.info("ya cargó los incluidos "+servicioPrincipal.getCodigo());
//		//Iteracion de los servicios incluidos de un servicio principal
//		for(DtoServiIncluidoServiPpal servicioIncluido:servicioPrincipal.getServiciosIncluidos())
//		{
//			logger.info("Servicios incluidos "+servicioIncluido.getCodigo());
//			for(int i=servicioIncluido.getCantidad();i>=1;i--)
//			{
//				DtoServicioCitaOdontologica servicioCita = new DtoServicioCitaOdontologica();
//				servicioCita.setServicio(servicioIncluido.getServicio().getCodigo());
//				servicioCita.setNombreServicio(servicioIncluido.getServicio().getNombre()+" (servicio incluido)");
//				servicioCita.setCodigoTipoServicio(ConstantesBD.codigoServicioProcedimiento+"");
//				servicioCita.getInfoTarifa().setEstadoFacturacion(ConstantesBD.codigoEstadoFExento);
//				
//				
//				BigDecimal numeroSolicitud = CitaOdontologica.generarSolicitud(con, servicioCita, paciente, usuario, servicioIncluido.getCentroCosto().getCodigo(), citaTemp.getAgendaOdon().getEspecialidadUniAgen(), citaTemp.getAgendaOdon().getCodigoMedico(), codOcupacionMedica, errores, false);
//				
//				if(numeroSolicitud.doubleValue()>0)
//				{
//					//***************INSERCION DE LA JUSTIFICACION***************************************************
//					if(UtilidadJustificacionPendienteArtServ.validarNOPOS(
//							con,
//							numeroSolicitud.intValue(), 
//							servicioIncluido.getServicio().getCodigo(), 
//							false, 
//							false, 
//							ConstantesBD.codigoNuncaValido /*codigoConvenio , para esta parte no es necesario*/))
//					{
//						double subcuenta = Cargos.obtenerCodigoSubcuentaDetalleCargo(con, servicioIncluido.getServicio().getCodigo(), numeroSolicitud.intValue(), ConstantesBD.codigoNuncaValido, false);
//						
//						if(!UtilidadJustificacionPendienteArtServ.insertarJusNP(con, numeroSolicitud.intValue(), servicioIncluido.getServicio().getCodigo(), 1, usuario.getLoginUsuario(), false, false, Utilidades.convertirAEntero(subcuenta+""),""))
//						{
//							errores.add("", new ActionMessage("errors.problemasGenericos","registrando justificacion No POS pendiente para el servicio incluido "+servicioIncluido.getServicio().getNombre()+", del servicio "+servicioPrincipal.getServicio().getNombre()));
//						}
//					}
//					//********************************************************************************
//					
//					//*******************ASOCIACIÓN DEL SERVICIO INCLUIDO CON EL SERVICIO DEL PLAN DE TRATAMIENTO**************************++
//					DtoServArtIncCitaOdo servicioPlanT = new DtoServArtIncCitaOdo();
//					servicioPlanT.getServicio().setCodigo(servicioIncluido.getServicio().getCodigo());
//					servicioPlanT.setCantidad(1);
//					servicioPlanT.getSolicitud().setCodigo(numeroSolicitud.intValue());
//					
//					//Se usa este metodo para obtener el codigo servicio cita Tarea 154554
//					//servicioPlanT.setServicioCitaOdo(CitaOdontologica.obtenerCodigoPkUltimaCitaProgServPlanT(servicio.getCodigoPkProgServ(),""));
//					servicioPlanT.setServicioCitaOdo(servicio.getCodigoPkProgServ());
//					
//					DtoInfoFechaUsuario infoFechaUsuario = new DtoInfoFechaUsuario();
//					infoFechaUsuario.setFechaModifica(fechaActual);
//					infoFechaUsuario.setHoraModifica(horaActual);
//					infoFechaUsuario.setUsuarioModifica(usuario.getLoginUsuario());
//					
//					if(!PlanTratamiento.guardarServArtIncPlant(con,servicioPlanT))
//					{
//						errores.add("", new ActionMessage("errors.problemasGenericos","registrando la relación entre el servicio incluido "+servicioIncluido.getServicio().getNombre()+" y el servicio "+servicioPrincipal.getServicio().getNombre()+" del plan de tratamiento"));
//					}
//					//****************************************************************************************************************************
//				}
//				
//			}
//		}
//	}

	
	/**
	 * Metodo para emepzar cargando la plantilla de evolucion
	 * @param request 
	 * */
	public void accionEmpezar(EvolucionOdontologicaForm forma,Connection con,PersonaBasica paciente,UsuarioBasico usuario, HttpServletRequest request)
	{	
		forma.reset(request);
		
		int sexo=ConstantesBD.codigoNuncaValido;
		int centroCosto=ConstantesBD.codigoNuncaValido;
		//forma.setCita(24);
		
		//Se reciben la cita y la plantilla por parametro
		
		//Agrego al Dto de la busqueda para saber si ya existe una plantilla para ese paciente
		forma.getDtoEvolucionOdo().setCita(forma.getCita());
		forma.getDtoEvolucionOdo().setCodigoPaciente(paciente.getCodigoPersona());
		forma.getDtoEvolucionOdo().setPlantilla(forma.getCodigoPlantilla());
		
		//Agrego el Dto de la cita para la atencion de citas
		BigDecimal bigCita = new BigDecimal(forma.getCita());
		forma.getDtoCita().setCodigoPk(bigCita.intValue());
		
		//Lleno el mundo de la atencion odontologica
		AtencionCitasOdontologia mundoAtencion = new AtencionCitasOdontologia();
		mundoAtencion.cargarInfoAtencionCitaOdo(forma.getDtoCita(), usuario, true);
		
		
		
		forma.setDtoAtencionCita(mundoAtencion.getInfoAtencionCitaOdo());
		
		if(!Utilidades.isEmpty(forma.getDtoAtencionCita().getPlantillas()))
			forma.setPlantillaBase(forma.getDtoAtencionCita().getPlantillas().get(0).getNombreFuncionalidad());
		
		
		forma.setListadoEvoluciones(EvolucionOdontologica.consultarEvolucion(forma.getDtoEvolucionOdo()));
		int listadoEvoSize=forma.getListadoEvoluciones().size();
		
		//Si ya existe una evolucion para el paciente se carga la existente, sino 
		if (listadoEvoSize==0)
			forma.setPorActualizar(ConstantesBD.acronimoNo);
		else
			forma.setPorActualizar(ConstantesBD.acronimoSi);
		
		logger.info("\n\n\n\n#######################################################################");
		logger.info("Por Actualizar: "+forma.getPorActualizar());
		logger.info("Codigo Cita: "+forma.getCita());
		logger.info("size: "+listadoEvoSize);
		logger.info("codigoPlantilla forma: "+forma.getCodigoPlantilla());
		logger.info("codigoPlantilla evolucion: "+forma.getDtoEvolucionOdo().getPlantilla());
		logger.info("#######################################################################\n\n\n\n");
		
		//Si no existe se carga una plantilla nueva para llenar
		if (forma.getPorActualizar().equals(ConstantesBD.acronimoNo))
		{
			//borrar
			//forma.getDtoEvolucionOdo().setPlantilla(8000071);
			DtoAntecendenteOdontologico dtoAntecedente = new DtoAntecendenteOdontologico();
			dtoAntecedente.setCodigoPaciente(paciente.getCodigoPersona());
			UtilidadOdontologia.obtenerUltimoRegistroAntecedentesOdontologia(con, dtoAntecedente,true);
			
			
			logger.info("CREO NUEVA PLANTILLA");
	
			//Cargo la plantilla de evolucion
			forma.setPlantilla(Plantillas.cargarPlantillaParametrica(
												con, 
												forma.getCodigoPlantilla(), 
												usuario.getCodigoInstitucionInt(), 
												ConstantesCamposParametrizables.funcParametrizableEvolucionOdontologica, 
												centroCosto, 
												sexo,
												ConstantesBD.codigoNuncaValido,
												ConstantesBD.codigoNuncaValido,
												ConstantesBD.codigoNuncaValido,
												false,
												"",
												"",
												"",
												dtoAntecedente.getValoracion(),
												dtoAntecedente.getEvolucion()));

			

		}
		else
		{
			//logger.info("CARGO PLANTILLA EXISTENTE ");
			//Agrego al Dto de la evolucion el ultimo registro dentro de la lista
			forma.setDtoEvolucionOdo(forma.getListadoEvoluciones().get(listadoEvoSize-1));
			
			
			logger.info("CARGO PLANTILLA EXISTENTE "+forma.getDtoEvolucionOdo().getCodigoPk());
			
			forma.setCodigoPlantilla(forma.getDtoEvolucionOdo().getPlantilla());
			
			//Cargo la plantilla para actualizar
			forma.setPlantilla(Plantillas.cargarPlantillaEvolucionOdon(con, usuario.getCodigoInstitucionInt(), usuario, paciente.getCodigoPersona(), "", forma.getDtoEvolucionOdo().getPlantilla(),UtilidadOdontologia.consultarIngresoCitaOdontologica(con, bigCita).intValue(),forma.getDtoEvolucionOdo().getCodigoPk()));
			
			//Miro si el usuario creación que se cargo en el listado es el mismo que hay en sesión
			logger.info("USUARIO CREO EVOLIUION------->"+forma.getListadoEvoluciones().get(listadoEvoSize-1).getUsuarioCreacion());
			if(forma.getListadoEvoluciones().get(listadoEvoSize-1).getUsuarioCreacion().equals(usuario.getLoginUsuario()))
				forma.setEsMedicoConfirmacion(ConstantesBD.acronimoSi);
			if(!forma.getListadoEvoluciones().get(listadoEvoSize-1).getUsuarioCreacion().equals(usuario.getLoginUsuario()))
				forma.setEsMedicoConfirmacion(ConstantesBD.acronimoNo);
			if(forma.getListadoEvoluciones().get(listadoEvoSize-1).getUsuarioCreacion().equals(""))
				forma.setEsMedicoConfirmacion("");
			
			
			
			
		}
		
		//TODO esto se elimina temporalmente para cargar el odontograma sin importar la plantilla.
		
//		if(forma.getPlantilla().existeComponente(ConstantesCamposParametrizables.tipoComponenteOdontogramaEvol))
//		{
			forma.setEstadoSecundario("empezar");
			accionOdontograma(con, paciente, usuario, forma, request,new ActionErrors());
//		}
		
		
		if(forma.getPlantilla().existeComponente(ConstantesCamposParametrizables.tipoComponenteIndicePlaca))
		{
			forma.setEstadoSecundario("empezar");
			accionIndicePlaca(paciente, forma, usuario, request, con);
			
		}

		logger.info("Se va a verificar si tiene componente de antecedentes.");
		if(forma.getPlantilla().existeComponente(ConstantesCamposParametrizables.tipoComponenteAntecendentesOdontologicos))
		{
			logger.info("Tiene componente de antecedentes!!");
			
			accionEmpezarAnteOdo(request, forma, usuario, paciente, con);
		}
	}
	
	/**
	 * Método implementado para redireccionar la plantilla
	 * @param forma
	 * @param response
	 * @param request 
	 * @param usuario 
	 * @return
	 */
	private ActionForward redireccionarPlantilla(EvolucionOdontologicaForm forma, HttpServletResponse response, HttpServletRequest request, UsuarioBasico usuario)
	{
		//Se carga el paciente en sesion
		PersonaBasica paciente = (PersonaBasica)request.getSession().getAttribute("pacienteActivo");
		paciente.setCodigoPersona(paciente.getCodigoPersona());
		Connection con = UtilidadBD.abrirConexion();
		UtilidadesManejoPaciente.cargarPaciente(con, usuario, paciente, request);
		UtilidadBD.closeConnection(con);
		try 
		{
			logger.info("\n\n\n***************************************ENTRE A LA PARTE DE LA EVOLUCION!!!**********************\n\n\n\n" +
					"cita:"+forma.getCita()+"\n\n"+
					"\n\nPlantilla"+forma.getDtoAtencionCita().getPlantillas().get(forma.getPosicionPlantilla()).getCodigoPK()+
					"\n\nposicionPlantilla"+forma.getPosicionPlantilla()+
					"\n\n\n");
			response.sendRedirect("../evolucionOdontologica/evolucion.do?estado=empezar&cita="+forma.getCita()+"&codigoPlantilla="+forma.getDtoAtencionCita().getPlantillas().get(forma.getPosicionPlantilla()).getCodigoPK()+"&posicionPlantilla="+forma.getPosicionPlantilla());
		} 
		catch (IOException e) 
		{
			logger.error("Error tratando de redireccionar la evolucion odontologica: ",e);
		}
		
		return null;
	}
	
	/**
	 * Método implementado para iniciar la confirmacion de la cita
	 * @param forma
	 * @param usuario
	 * @param mapping
	 * @return
	 */
	private ActionForward accionIniciarConfirmacion(EvolucionOdontologicaForm forma, UsuarioBasico usuario,ActionMapping mapping) 
	{
	
		logger.info("¿por confirmar? "+forma.getPorConfirmar());
		AtencionCitasOdontologia.validarUsuarioInicioConfirmacion(forma.getDtoCita(), usuario, forma.getDtoAtencionCita());
		return mapping.findForward("popUpConfirmacion");
	}
	
	/**
	 * Método implementado para realizar la confirmación de la cita
	 * @param forma
	 * @param usuario
	 * @param mapping
	 * @param request 
	 * @return
	 * @throws BDException 
	 */
	private ActionForward accionGuardarConfirmacion(EvolucionOdontologicaForm forma, UsuarioBasico usuario,ActionMapping mapping, HttpServletRequest request, PersonaBasica paciente, ActionErrors errores) throws BDException 
	{
		Connection con = UtilidadPersistencia.getPersistencia().obtenerConexion();
		
		AtencionCitasOdontologia mundoAtencion = new AtencionCitasOdontologia();
		
		if (forma.getPorConfirmar().equals(ConstantesBD.acronimoSi))
		{
			boolean existeComponenteOdontograma = forma.getPlantilla().existeComponente(ConstantesCamposParametrizables.tipoComponenteOdontogramaEvol);
			
			if(existeComponenteOdontograma){
								
				errores = forma.validacionesEsProgramasServiciosRealizadosInterno(usuario.getCodigoInstitucionInt());
				
			}else{
				
				validarServiciosRealizadosInternoPlantillasSinOdontograma(forma, errores, usuario.getCodigoInstitucionInt());
			}
		}
		
		if(errores.isEmpty())
		{	
			//Realizo la inserción
			errores = accionInsertar(forma, con, paciente, usuario, errores);
		}
		
		// Genero las solicitudes de servicios incluidos para la próxima cita
		
		if(errores.isEmpty())
		{
			if (forma.getPorConfirmar().equals(ConstantesBD.acronimoSi))
			{
				logger.info("********************INICIO CONFIRMACION CITA************************************");

				if(validarIniciarProcesoProximaCita(forma, usuario)){
					
					forma.setEstado("");
					
					return mapping.findForward(programarProximaCita(forma, request, paciente));

				}else{
					
					continuarProcesoGuardarConfirmacion(forma, usuario,	request, paciente, mundoAtencion);
				}
			
			}else if (forma.getPorConfirmar().equals(ConstantesBD.acronimoNo))
			{
				forma.setEstado("empezar");
			}
			
			forma.setCerrarVentanaConf(ConstantesBD.acronimoSi);
			
			if(!mundoAtencion.getErrores().isEmpty())
			{
				forma.setEstado("iniciarConfirmacion");
				forma.setCerrarVentanaConf(ConstantesBD.acronimoNo);
				saveErrors(request, mundoAtencion.getErrores());
			}
		}
		else
		{
			saveErrors(request, errores);
		}
		
		return mapping.findForward(terminarConfirmacionCita(forma, usuario, mapping, errores, mundoAtencion));
	}


	/**
	 * Metodo Centralizado para el manejo de las acciones sobre el antecedente odontologico
	 * 
	 * */
	public ActionForward accionAntecedenteOdontologico(
			ActionMapping mapping,
			PersonaBasica paciente,
			ValoracionOdontologicaForm forma,
			UsuarioBasico usuario,
			HttpServletRequest request,
			Connection con)
	{
		forma.getInfoCompAnteOdont().setUtilizaProgOdonto(ValoresPorDefecto.getUtilizanProgramasOdontologicosEnInstitucion(usuario.getCodigoInstitucionInt()));
		logger.info("Estado para el Antecedente odontologico >> "+forma.getEstadoSecundario());
		logger.info("mostrar por: "+forma.getInfoCompAnteOdont().getMostrarPor());
		if(forma.getEstadoSecundario().equals("empezar"))
		{
			accionEmpezarAnteOdo(request, forma, usuario, paciente, con);
		}else{
		
			ActionErrors errores=ComponenteAnteOdoto.accionesEstados(forma.getInfoCompAnteOdont(),forma.getEstadoSecundario(), true);
			//anteOdonto.cargarComponenteAnteOdonto(forma.getInfoCompAnteOdont());
		
			if(!errores.isEmpty())
			{
				saveErrors(request,errores);
			}
		
			accionParametrosReqAnteOdo(request);
			
		}
		forma.setEstadoSecundario("");
		return mapping.findForward(forma.getInfoCompAnteOdont().getForward());
	}
	
	/**
	 * metodo que inicializa variables para el componenete de antecedentes odoontologicos
	 * @param request
	 * @param forma
	 * @param usuario
	 * @param paciente
	 * @param con
	 */
	private void accionEmpezarAnteOdo(HttpServletRequest request,
			ValoracionOdontologicaForm forma, UsuarioBasico usuario,
			PersonaBasica paciente, Connection con) 
	{
		forma.setEstadoSecundario("empezar");
		logger.info("codigo paciente: "+paciente.getCodigoPersona());
		ActionErrors errores=ComponenteAnteOdoto.llenadoInicialInterfaz(forma.getInfoCompAnteOdont(), con);
		if(errores.isEmpty())
		{
			// con este parametro savemos que tipo de antecedentee realizar
			forma.getInfoCompAnteOdont().setPorConfirmar(forma.getPorActualizar());
			if(forma.getPorActualizar().equals(ConstantesBD.acronimoSi))
				forma.getInfoCompAnteOdont().setCodigoAnteOdon(Utilidades.convertirAEntero(Double.toString(forma.getDtoIngreso().getValoracionOdonto())));
			
			// se verifica y se llena el parametro general utiliza programa odontologico
			forma.getInfoCompAnteOdont().setUtilizaProgOdonto(ValoresPorDefecto.getUtilizanProgramasOdontologicosEnInstitucion(usuario.getCodigoInstitucionInt()));
			
			forma.getInfoCompAnteOdont().setCodigoPaciente(paciente.getCodigoPersona());
			forma.getInfoCompAnteOdont().setMostrarPor(UtilidadTexto.getBoolean(forma.getInfoCompAnteOdont().getUtilizaProgOdonto())
					?ConstantesIntegridadDominio.acronimoMostrarProgramas
					:ConstantesIntegridadDominio.acronimoMostrarServicios);
			
			// control de los acciones internas del componente
			errores=ComponenteAnteOdoto.accionesEstados(forma.getInfoCompAnteOdont(),forma.getEstadoSecundario(), true);
			
			accionParametrosReqAnteOdo(request);
			
		}
		if(!errores.isEmpty())
		{
			saveErrors(request, errores);
		}
	}
	
	
	/**
	 * Metodo Centralizado para el manejo de las acciones sobre el componente Indice de Placa
	 * */
	public ActionForward accionIndicePlaca(
			ActionMapping mapping,
			PersonaBasica paciente,
			ValoracionOdontologicaForm forma,
			UsuarioBasico usuario,
			HttpServletRequest request,
			Connection con)
	{
		logger.info("segundo Estado. "+forma.getEstadoSecundario());
		if(forma.getEstadoSecundario().equals("empezar"))
		{
			// inicializar los atributos de edad y codigo de institucion
			forma.getCompIndicePlaca().setPlantillaIngreso(Utilidades.convertirAEntero(forma.getDtoIngreso().getCodigoPK()+""));
			forma.getCompIndicePlaca().setCodigoPaciente(paciente.getCodigoPersona());
			forma.getCompIndicePlaca().setEdadPaciente(paciente.getEdad());
			forma.getCompIndicePlaca().setCodigoInstitucion(usuario.getCodigoInstitucionInt());
			forma.getCompIndicePlaca().setUsuarioModifica(usuario.getLoginUsuario());
			forma.getCompIndicePlaca().setPorActualizar(forma.getPorActualizar());
		}
		
		ComponenteIndicePlaca.accionesEstados(forma.getCompIndicePlaca(),forma.getEstadoSecundario());
		
		request.setAttribute("nombreAtributoIndicePlaca","compIndicePlaca");
		request.setAttribute("nombreForm","EvolucionOdontologicaForm");
		request.setAttribute("pathAction","/evolucionOdontologica/evolucion.do");
		
		forma.setEstadoSecundario("");
		return mapping.findForward(forma.getCompIndicePlaca().getForward());
	}
	
	private void accionParametrosReqAnteOdo(HttpServletRequest request) {
		request.setAttribute("nombreAtributoAnteOdonto","infoCompAnteOdont");
		request.setAttribute("nombreForm","EvolucionOdontologicaForm");
		request.setAttribute("pathAction","/evolucionOdontologica/evolucion.do");
	}
	
	/**
	 * Metodo Centralizado para el manejo de las acciones sobre el odontograma
	 * @param ActionMapping mapping
	 * @param ValoracionOdontologicaForm forma
	 * @param HttpServletRequest request
	 * */
	public String accionOdontograma(
			Connection con,
			PersonaBasica paciente,
			UsuarioBasico usuario,
			EvolucionOdontologicaForm forma,
			HttpServletRequest request,
			ActionErrors errores)
	{
		logger.info("Estado para el odontograma Evolucion >> "+forma.getEstadoSecundario());
				
		ComponenteOdontogramaEvo odonto = new ComponenteOdontogramaEvo();
		forma.getInfoCompOdont().setInstitucion(usuario.getCodigoInstitucionInt());
		
        
		if(forma.getEstadoSecundario().equals("empezar"))
		{
			logger.info("\n\n\n\n\n\n\n\n\n\n\n");
			logger.info("Seccion Aplica: "+forma.getInfoCompOdont().getSeccionAplica());
			logger.info("\n\n\n\n\n\n\n\n\n\n\n");
			forma.getInfoCompOdont().setPathNombreContexo(request.getContextPath());
			forma.setInfoCompOdont(odonto.accionEmpezar(
														forma.getInfoCompOdont(),
														paciente.getCodigoPersona(),//paciente.getCodigoPersona(),
														paciente.getCodigoIngreso(),//paciente.getCodigoIngreso(),
														paciente.getEdad(),
														Utilidades.convertirAEntero(forma.getCita()+""),
														ConstantesBD.codigoNuncaValido, 
														ConstantesBD.codigoNuncaValido, 
														ConstantesIntegridadDominio.acronimoInicial, 
														ConstantesIntegridadDominio.acronimoOdontogramaTratamiento, 
														usuario,
														forma.getEstadoSecundario(),
														paciente));
			
		}
		else if(forma.getEstadoSecundario().equals("salvarInfoValo"))
		{
			odonto.setErrores(this.accionInsertar(forma, con, paciente, usuario, errores));
		}
		else if(forma.getEstadoSecundario().equals("empezarTodo"))
		{
           
			forma.setInfoCompOdont(odonto.accionEmpezar(
					forma.getInfoCompOdont(),
					paciente.getCodigoPersona(),//paciente.getCodigoPersona(),
					paciente.getCodigoIngreso(),//paciente.getCodigoIngreso(),
					paciente.getEdad(),
					Utilidades.convertirAEntero(forma.getCita()+""),
					ConstantesBD.codigoNuncaValido, 
					ConstantesBD.codigoNuncaValido, 
					ConstantesIntegridadDominio.acronimoInicial, 
					ConstantesIntegridadDominio.acronimoOdontogramaDiagnostico, 
					usuario,
					forma.getEstadoSecundario(),
					paciente));
			
			logger.info("\n\n >>>>>>>> Va a recargar Odontograma  ");
			//**** Se Agrega Marzo 31 de 2010  
		    if(forma.getPlantilla().existeComponente(ConstantesCamposParametrizables.tipoComponenteOdontogramaEvol))
			{
				forma.setEstadoSecundario("empezar");
				accionOdontograma(con, paciente, usuario, forma, request,new ActionErrors());
			}
		    //*****
		    
			odonto.setForwardOdont("");
		}
		else
		{
			odonto.setConInterna(con);
			forma.setInfoCompOdont(odonto.centralAcciones(forma.getInfoCompOdont(),forma.getEstadoSecundario(),paciente));
			UtilidadBD.closeConnection(con);
		}
		
		saveErrors(request,odonto.getErrores());
		
		request.setAttribute("nombreAtributoOdontograma","infoCompOdont");
		request.setAttribute("nombreForm","EvolucionOdontologicaForm");
		request.setAttribute("pathAction","/evolucionOdontologica/evolucion.do");
		
		logger.info("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
		logger.info("Seccion Aplica: ["+forma.getInfoCompOdont().getSeccionAplica()+"]");
		logger.info("Contenedor: ["+forma.getInfoCompOdont().getContenedor()+"]");
		logger.info("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");

		if(forma.getInfoCompOdont().getSeccionAplica().equals(ConstantesIntegridadDominio.acronimoOtro)){
			request.setAttribute("seccionAplica",ConstantesIntegridadDominio.acronimoOtro);
			request.setAttribute("contenedor","seccionOtrosHallazgos");
		}else if(forma.getInfoCompOdont().getSeccionAplica().equals(ConstantesIntegridadDominio.acronimoDetalle)){
			request.setAttribute("seccionAplica",ConstantesIntegridadDominio.acronimoDetalle);
			request.setAttribute("contenedor","seccionHallazgosDetalle");
		}else if(forma.getInfoCompOdont().getSeccionAplica().equals(ConstantesIntegridadDominio.acronimoBoca)){
			request.setAttribute("seccionAplica",ConstantesIntegridadDominio.acronimoBoca);
			request.setAttribute("contenedor","seccionHallazgosBoca");		
		}else if(forma.getInfoCompOdont().getSeccionAplica().equals(ConstantesIntegridadDominio.acronimoInclusion)){
			request.setAttribute("seccionAplica",ConstantesIntegridadDominio.acronimoInclusion);
		}else if(forma.getInfoCompOdont().getSeccionAplica().equals(ConstantesIntegridadDominio.acronimoDetalle+ConstantesBD.separadorSplit+ConstantesIntegridadDominio.acronimoOtro)){
			request.setAttribute("seccionAplica",ConstantesIntegridadDominio.acronimoDetalle+ConstantesBD.separadorSplit+ConstantesIntegridadDominio.acronimoOtro);
			request.setAttribute("contenedor","seccionProgServCita");
		}
		else if(forma.getInfoCompOdont().getSeccionAplica().equals(ConstantesIntegridadDominio.acronimoInclusion+ConstantesIntegridadDominio.acronimoDetalle))
		{
			request.setAttribute("seccionAplica",ConstantesIntegridadDominio.acronimoDetalle);
			request.setAttribute("contenedor","seccionOtrosHallazgos");
			request.setAttribute("nombreAtributoOdontograma","infoCompOdont");
			request.setAttribute("nombreForm","EvolucionOdontologicaForm");
			request.setAttribute("pathAction","/evolucionOdontologica/evolucion.do");
		}
		else if(forma.getInfoCompOdont().getSeccionAplica().equals(ConstantesIntegridadDominio.acronimoInclusion+ConstantesIntegridadDominio.acronimoOtro))
		{
			request.setAttribute("seccionAplica",ConstantesIntegridadDominio.acronimoOtro);
			request.setAttribute("contenedor","seccionOtrosHallazgos");
			request.setAttribute("nombreAtributoOdontograma","infoCompOdont");
			request.setAttribute("nombreForm","EvolucionOdontologicaForm");
			request.setAttribute("pathAction","/evolucionOdontologica/evolucion.do");
		}
		/*else if(forma.getInfoCompOdont().getSeccionAplica().equals(ConstantesIntegridadDominio.acronimoDetalle+ConstantesBD.separadorSplit+ConstantesIntegridadDominio.acronimoOtro)){
			request.setAttribute("seccionAplica",ConstantesIntegridadDominio.acronimoDetalle+ConstantesBD.separadorSplit+ConstantesIntegridadDominio.acronimoOtro);
			request.setAttribute("conte
			nedor","seccionProgServCita");		
		}*/
		/*logger.info("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
		logger.info("Seccion Aplica: ["+request.getAttribute("seccionAplica")!=null?request.getAttribute("seccionAplica").toString():"--]");
		logger.info("Contenedor: ["+request.getAttribute("contenedor")!=null?request.getAttribute("contenedor").toString():"--]");
		logger.info("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");*/

		Log4JManager.info("forma.getEstadoSecundario() "+forma.getEstadoSecundario().equals("salvarInfoValo"));
		if(forma.getEstadoSecundario().equals("salvarInfoValo"))
		{
			forma.setEstadoSecundario("abrirPopUpNuevosHallazgos");
		}
		else
		{
			if(odonto.getEstadoInterno().equals("IngresoExitoInclusion"))
			{	
				forma.setEstadoSecundario(odonto.getEstadoInterno());
			}
		}
		
		if(odonto.getForwardOdont().equals(""))
		{
			logger.info("Principal");
			return "principal";
			
		}else
		{
			logger.info(odonto.getForwardOdont());
			return odonto.getForwardOdont();
		}
	}
	
	
	/**
	 * Método que se encarga de realizar las respectivas validaciones para determinar
	 * si se debe o no iniciar el proceso centralizado de Próxima Cita
	 * 
	 * @param forma
	 * @param usuario
	 * @return
	 */
	private boolean validarIniciarProcesoProximaCita(EvolucionOdontologicaForm forma, UsuarioBasico usuario){
		
		boolean resultado = false;
		
		boolean existeComponenteOdontograma = forma.getPlantilla().existeComponente(ConstantesCamposParametrizables.tipoComponenteOdontogramaEvol);
		
		if(ValoresPorDefecto.getRequeridoProgramarProximaCitaEnAtencion(usuario.getCodigoInstitucionInt()).equals(ConstantesBD.acronimoSi)){
			
			String estadoPlanTratamiento = "";
			
			if(existeComponenteOdontograma){
				
				estadoPlanTratamiento = forma.getInfoCompOdont().getDtoInfoPlanTratamiento().getEstado();
				
			}else{
				
				DtoPlanTratamientoOdo planTratamiento = obtenerPlanTratamientoPaciente(forma, usuario);
				
				estadoPlanTratamiento = planTratamiento.getEstado();
			}
			
			if(estadoPlanTratamiento.equals(ConstantesIntegridadDominio.acronimoEstadoEnProceso)
				|| estadoPlanTratamiento.equals(ConstantesIntegridadDominio.acronimoEstadoActivo)){
				
				if(ValoresPorDefecto.getValidaPresupuestoOdoContratado(usuario.getCodigoInstitucionInt()).equals(ConstantesBD.acronimoSi)){
					
					boolean servPendientes = forma.validacionExistenServiPendienteXEvolucionar(usuario.getCodigoInstitucionInt(), existeComponenteOdontograma).equals(ConstantesBD.acronimoSi);
				
					if(forma.getDtoCita().getTipo().equals(ConstantesIntegridadDominio.acronimoTipoAtencionTratamiento) && servPendientes){
						
						resultado = true;
					}
					
				}else if(forma.getDtoCita().getTipo().equals(ConstantesIntegridadDominio.acronimoTipoAtencionTratamiento) ||
					forma.getDtoCita().getTipo().equals(ConstantesIntegridadDominio.acronimoTipoAtencionValoracionInicial) ||
					forma.getDtoCita().getTipo().equals(ConstantesIntegridadDominio.acronimoRevaloracion)){
					
					resultado = true;
				}
			}
		}
		
		return resultado;
		
	}

	
	/**
	 * 
	 * Método que se encarga de cargar la información necesaria para que el proceso centralizado
	 * pueda guardar la programación de la próxima cita.
	 * 
	 * @param forma
	 * @param request
	 * @param paciente 
	 * @return
	 */
	private String programarProximaCita(EvolucionOdontologicaForm forma, HttpServletRequest request, PersonaBasica paciente) {
		
		DtoProcesoCitaProgramada dtoProcesoCitaProgramada = forma.getDtoProcesoCitaProgramada();
		
		dtoProcesoCitaProgramada.setCodigoPersona(forma.getDtoCita().getCodigoPaciente());
		dtoProcesoCitaProgramada.setCodigoEvolucion(forma.getInfoCompOdont().getCodigoEvolucion());

		request.getSession().setAttribute("informacionProximaCitaProgramada", dtoProcesoCitaProgramada);
	  
		forma.setAbrePopUpProximaCita(true);
		
		return "principal";
	
	}

	/**
	 * Método que se encarga de validar si se ha generado adecuadamente la Próxima cita
	 * desde el proceso centralizado antes de iniciar el proceso de guardar la evolución
	 * 
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @param paciente
	 * @param request
	 * @param precontratar
	 * @return 
	 * @throws BDException 
	 */
	private String verificarRegistroProximaCita (ActionMapping mapping,
			EvolucionOdontologicaForm forma, 
			UsuarioBasico usuario,
			PersonaBasica paciente, 
			HttpServletRequest request, ActionErrors errores) throws BDException {
		
		//se guardo la proxima cita, entonces se debe continuar con el proceso
		if(forma.getCodigoProximaCitaRegistrada()>0)
		{
			forma.setAbrePopUpProximaCita(false);
			
			//ComponenteOdontogramaEvo compOdonEvo = new ComponenteOdontogramaEvo();
			//compOdonEvo.centralAcciones(forma.getInfoCompOdont(),ComponenteOdontograma.codigoEstadoGuardarOdonto, paciente);
			
			AtencionCitasOdontologia mundoAtencion = new AtencionCitasOdontologia();
			
			continuarProcesoGuardarConfirmacion(forma, usuario,	request, paciente, mundoAtencion);
			
			if (forma.getPorConfirmar().equals(ConstantesBD.acronimoNo))
			{
				forma.setEstado("empezar");
			}
			
			forma.setCerrarVentanaConf(ConstantesBD.acronimoSi);
			
			if(!mundoAtencion.getErrores().isEmpty())
			{
				forma.setEstado("iniciarConfirmacion");
				forma.setCerrarVentanaConf(ConstantesBD.acronimoNo);
				saveErrors(request, mundoAtencion.getErrores());
			}
			
			return terminarConfirmacionCita(forma, usuario, mapping, errores, mundoAtencion);
			
		}else{
			   
		   /*
			* Si no se guarda, no debe hacer nada, el proceso no es exitoso.
			*/
			   
			Log4JManager.info("no se guardo la cita correctamente");
			forma.setAbrePopUpProximaCita(true);
			return "principal";
		}
	}
	
	/**
	 * 
	 * Método en donde se da continuidad al proceso de confirmación de la 
	 * evolución Odontológica.
	 * 
	 * @param forma
	 * @param usuario
	 * @param request
	 * @param paciente
	 * @param mundoAtencion
	 * @throws BDException 
	 */
	private void continuarProcesoGuardarConfirmacion(
			EvolucionOdontologicaForm forma, UsuarioBasico usuario,
			HttpServletRequest request, PersonaBasica paciente,
			AtencionCitasOdontologia mundoAtencion) throws BDException {
		
		InstitucionBasica institucion = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
		
		forma.setInstitucionBasica(institucion);
		
		mundoAtencion.confirmarCita(forma.getDtoCita(), usuario, forma.getDtoAtencionCita(),paciente,false,true, request.getSession().getId(), forma.getInfoCompOdont().getCodigoValoracion(), forma.getInstitucionBasica());				
		forma.setFacturasAutomaticas(mundoAtencion.getFacturasAutomaticas());
		forma.setEstado("irResumen");
	}
	
	
	/**
	 * 
	 * Método que termina el proceso de confirmación de la Evolución Odontológica.
	 * 
	 * @param forma
	 * @param usuario
	 * @param mapping
	 * @param errores
	 * @param mundoAtencion
	 * @return
	 */
	private String terminarConfirmacionCita(
			EvolucionOdontologicaForm forma, UsuarioBasico usuario,
			ActionMapping mapping, ActionErrors errores,
			AtencionCitasOdontologia mundoAtencion) {
		
		
		if(mundoAtencion.getErrores().size()==0 && forma.getPorConfirmar().equals(ConstantesBD.acronimoSi) && errores.size()==0)
		{
			mundoAtencion.cargarPlantillasAtencion(forma.getDtoCita(), usuario);
			forma.getDtoAtencionCita().setPlantillas(mundoAtencion.getInfoAtencionCitaOdo().getPlantillas());
			forma.setEstado("irResumen");
			return "resumen";
		}
		else
		{
			mundoAtencion.cargarPlantillasAtencion(forma.getDtoCita(), usuario);
			forma.getDtoAtencionCita().setPlantillas(mundoAtencion.getInfoAtencionCitaOdo().getPlantillas());
			return "principal";
		}
	}
	
	/**
	 * Método que se encarga de eliminar el registro de 
	 * Próxima cita generado
	 * 
	 * @param forma
	 */
	private void eliminarRegistroProximaCita(EvolucionOdontologicaForm forma) {
		
		ICitaOdontologicaServicio citaOdonServicio = OdontologiaServicioFabrica.crearCitaOdontologicaServicio();
		
		if(forma.getCodigoProximaCitaRegistrada() > 0){
			
			UtilidadTransaccion.getTransaccion().begin();

			citaOdonServicio.eliminarCitaProgramada(forma.getCodigoProximaCitaRegistrada());
			
			UtilidadTransaccion.getTransaccion().commit();
		}
	}
	
	/**
	 * Método para obtener el plan de tratamiento del paciente
	 * que está siendo atendido
	 * 
	 * @param forma
	 * @param usuario
	 * @return
	 */
	private DtoPlanTratamientoOdo obtenerPlanTratamientoPaciente(EvolucionOdontologicaForm forma, UsuarioBasico usuario) {
		
		BigDecimal codigoPlanTratamiento = PlanTratamiento.obtenerUltimoCodigoPlanTratamiento(forma.getDtoCita().getCodigoPaciente());
		
		DtoPlanTratamientoOdo parametros = new DtoPlanTratamientoOdo();
		parametros.setCodigoPk(codigoPlanTratamiento);
		parametros.setInstitucion(usuario.getCodigoInstitucionInt());
		DtoPlanTratamientoOdo planTratamiento = PlanTratamiento.consultarPlanTratamiento(parametros);
		return planTratamiento;
	}
	
	
	/**
	 * @param forma
	 * @param errores
	 */
	private void validarServiciosRealizadosInternoPlantillasSinOdontograma(EvolucionOdontologicaForm forma, ActionErrors errores, int codigoInstitucion) {
		
		for (DtoServicioCitaOdontologica servicio : forma.getDtoCita().getServiciosCitaOdon()) {
			
			BigDecimal detPlanTratamiento = new BigDecimal(PlanTratamiento.obtenerDetPlanTratamientoXProgramaHallazgoPieza(servicio.getCodigoProgramaHallazgoPieza()));
			
			ArrayList<InfoServicios> listaServiciosContratados = new ArrayList<InfoServicios>();
			
			String codigoTarifarioServicios = ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(codigoInstitucion);

			//ArrayList<String> estados = new ArrayList<String>();
			//estados.add(ConstantesIntegridadDominio.acronimoEnProceso);
			
			//ArrayList<BigDecimal> codigosPlanTratamiento = PlanTratamiento.obtenerCodigoPlanTratamiento(forma.getDtoCita().getCodigoPaciente(), estados , ConstantesBD.acronimoNo);
			int codigoPlanTratamiento = forma.getInfoCompOdont().getInfoPlanTrata().getCodigoPk().intValue();
			
			if(codigoPlanTratamiento > ConstantesBD.codigoNuncaValido && detPlanTratamiento!=null && detPlanTratamiento.intValue() > ConstantesBD.codigoNuncaValido){
	
				DtoProgramasServiciosPlanT parametrosProgServ = new DtoProgramasServiciosPlanT();
				parametrosProgServ.setEstadoServicio(ConstantesIntegridadDominio.acronimoRealizadoInterno);
				parametrosProgServ.setCodigoTarifario(codigoTarifarioServicios);
				parametrosProgServ.setCodigoPlanTratamiento(codigoPlanTratamiento);
				parametrosProgServ.setDetPlanTratamiento(detPlanTratamiento);
				parametrosProgServ.setServicio(new InfoDatosInt(servicio.getServicio()));
				
				BigDecimal  programa = new BigDecimal(servicio.getCodigoPrograma());

				parametrosProgServ.setPrograma(new InfoDatosDouble(programa.doubleValue(), ""));
				
				listaServiciosContratados = PlanTratamiento.cargarServiciosDeProgramasPlanT(parametrosProgServ);
				
				/*
				 * Si el listado no tiene un registro, el servicio no esta marcado como realizado interno, por lo cual
				 * se debe retornar un error.
				 */
				if(listaServiciosContratados.size() == 0){
					
					errores.add("", new ActionMessage("errors.notEspecific","El servicio " + servicio.getCodigoPropietarioServicio() +" "+ servicio.getNombreServicio() +" del programa Contratado debe ser marcado como Realizado Interno"));
					//break;
				}
			}
		}
	}
}