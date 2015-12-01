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
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.manejoPaciente.UtilidadesManejoPaciente;
import util.odontologia.InfoDetallePlanTramiento;
import util.odontologia.InfoHallazgoSuperficie;
import util.odontologia.InfoOdontograma;
import util.odontologia.InfoProgramaServicioPlan;
import util.odontologia.UtilidadOdontologia;

import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.odontologia.ValoracionOdontologicaForm;
import com.princetonsa.dto.historiaClinica.parametrizacion.DtoPlantilla;
import com.princetonsa.dto.odontologia.DtoAntecendenteOdontologico;
import com.princetonsa.dto.odontologia.DtoCitaOdontologica;
import com.princetonsa.enu.impresion.EnumTiposSalida;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.atencion.Diagnostico;
import com.princetonsa.mundo.historiaClinica.Plantillas;
import com.princetonsa.mundo.historiaClinica.Valoraciones;
import com.princetonsa.mundo.odontologia.AtencionCitasOdontologia;
import com.princetonsa.mundo.odontologia.CitaOdontologica;
import com.princetonsa.mundo.odontologia.ComponenteAnteOdoto;
import com.princetonsa.mundo.odontologia.ComponenteIndicePlaca;
import com.princetonsa.mundo.odontologia.ComponenteOdontograma;
import com.princetonsa.mundo.odontologia.PlanTratamiento;
import com.princetonsa.mundo.odontologia.ValoracionOdontologica;
import com.servinte.axioma.generadorReporte.odontologia.plantillasOdontologicas.GeneradorReportePlantillasOdontologicas;
import com.servinte.axioma.orm.CentroAtencion;
import com.servinte.axioma.servicio.impl.administracion.CentroAtencionServicio;

public class ValoracionOdontologicaAction extends Action
{
	Logger logger = Logger.getLogger(ValoracionOdontologicaAction.class);
	
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	{
		Connection con=null;
		try{
			if(form instanceof ValoracionOdontologicaForm)
			{
				ValoracionOdontologicaForm forma=(ValoracionOdontologicaForm)form;
				String estado=forma.getEstado();

				HttpSession session=request.getSession();
				ActionErrors errores = new ActionErrors();
				UsuarioBasico usuario = (UsuarioBasico)session.getAttribute("usuarioBasico");
				PersonaBasica paciente = (PersonaBasica)session.getAttribute("pacienteActivo");
				forma.setInstitucionBasica((InstitucionBasica)session.getAttribute("institucionBasica"));

				logger.info("valor del estado >> "+estado);
				forma.setMostrarMensaje(new ResultadoBoolean(false));
				forma.setAncla("");
				if(estado==null)
				{
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					return mapping.findForward("paginaError");
				}
				else if(estado.equals("empezar"))
				{			
					forma.setRedireccion("redireccionEmpezar");
					if (paciente.getCodigoPersona()<1)
					{
						request.setAttribute("codigoDescripcionError", "errors.paciente.noCargado");
						return mapping.findForward("paginaError");     
					}
					else
					{

						return empezar(forma, paciente, usuario, request, mapping, true);
					}
				}
				else if(estado.equals("odontograma"))
				{
					con=UtilidadBD.abrirConexion();
					return mapping.findForward(accionOdontograma(con,paciente,usuario,forma,request, ConstantesBD.codigoNuncaValido));
				}
				else if(estado.equals("nuevosHallazgosEvolucion"))
				{
					con=UtilidadBD.abrirConexion();
					forma.getInfoCompOdont().setCodigoEvolucion(Utilidades.convertirAEntero(forma.getCodigoEvolucionAsociada()));
					ActionForward retorno=mapping.findForward(accionOdontograma(con,paciente,usuario,forma,request, ConstantesBD.codigoNuncaValido));
					forma.getInfoCompOdont().setCodigoEvolucion(Utilidades.convertirAEntero(forma.getCodigoEvolucionAsociada()));
					return retorno;
				}

				else if(estado.equals("insertar"))
				{
					con=UtilidadBD.abrirConexion();
					return accionInsertar(forma,con,paciente,usuario, errores,request, mapping);
				}

				else if(estado.equals("antecedenteOdonto"))
				{
					con=UtilidadBD.abrirConexion();
					return accionAntecedenteOdontologico(mapping,paciente,forma,usuario,request,con);
				}
				else if(estado.equals("indicePlaca"))
				{
					con=UtilidadBD.abrirConexion();
					return accionIndicePlaca(mapping, paciente, forma, usuario, request, con);
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
					return accionValidarUsuario(forma,usuario,mapping, request);
				}
				else if(forma.getEstado().equals("guardarConfirmacion"))
				{
					con=UtilidadBD.abrirConexion();
					forma.setRedireccion("redireccionEmpezar");
					return accionGuardarConfirmacion(con,forma,usuario,mapping,request, paciente,errores);
				}
				else if (forma.getEstado().equals("irResumen"))
				{
					forma.setRedireccion("redireccionResumen");
					logger.info("\n\n\n");
					logger.info("ENTRE A IR RESUMEN !!!!!!!CONSULTE E INSERTE Y EL VALOR DEL ESTADO Y CERRAR VENTANA ES------->"+forma.getEstado()+"-"+forma.getCerrarVentanaConf());
					logger.info("\n\n\n");
					//cargarResumenValoracion(forma, con, paciente, usuario, errores, request, mapping);

					return mapping.findForward("resumen");
				}
				else if (forma.getEstado().equals("imprimir"))
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
				else if(forma.getEstado().equals("imprimirPlantillaHistorico")){
					con=UtilidadBD.abrirConexion();
					forma.setNombreArchivoGenerado(null);
					forma.setEnumTipoSalida(null);
					return accionImprimirPlantillaSeleccionada(con,forma,usuario,paciente,mapping,request);
				}
				/**
				 * Tarea 157105 -Se agregan los estados para arreglar el problema de secciones dependientes de otras. No existia el estad al que debia volver al guardar lso elements de la seccion.
				 */
				else if(forma.getEstado().equals("recargarPlantilla"))
				{
					return mapping.findForward("seccionesDependientes");
				}
				else if(forma.getEstado().equals("consultaFacturaAutomatica"))
				{
					if(forma.getFacturasAutomaticas().getErroresFactura().size()>0)
					{
						request.setAttribute("ocultarEncabezadoErrores", "true");
						request.setAttribute("utilizaParent", true);
						return ComunAction.envioMultiplesErrores(mapping, request, null, "label.facturacion.facturacionOdontologicaCancelada", forma.getFacturasAutomaticas().getErroresFactura(), logger);
					}
					return null;
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
				else
				{
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					return mapping.findForward("paginaError");
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

	
	private ActionForward empezar(ValoracionOdontologicaForm forma, PersonaBasica paciente, UsuarioBasico usuario, HttpServletRequest request, ActionMapping mapping, boolean borrarCodigoCita)
	{
		Connection con = UtilidadBD.abrirConexion();	
		accionEmpezar(forma,con,paciente,usuario,request,borrarCodigoCita);
		if(forma.getPlantilla().existeComponente(ConstantesCamposParametrizables.tipoComponenteAntecendentesOdontologicos))
		{
			accionEmpezarAnteOdo(request, forma, usuario, paciente, con);
		}
		if(forma.getPlantilla().existeComponente(ConstantesCamposParametrizables.tipoComponenteOdontogramaDiag))
		{
			forma.setEstadoSecundario("empezar");
			accionOdontograma(con, paciente, usuario, forma, request, ConstantesBD.codigoNuncaValido);
			//cuando es el empezar principal no se debe usar ancla.
			forma.setAncla("");
		}
		//forma.getInfoCompOdont()
		if(forma.getInfoCompOdont().getDtoInfoPlanTratamiento().getEstado().equalsIgnoreCase(ConstantesIntegridadDominio.acronimoEstadoActivo)||forma.getInfoCompOdont().getDtoInfoPlanTratamiento().getEstado().equalsIgnoreCase(ConstantesIntegridadDominio.acronimoEstadoEnProceso))
		{
			if(!forma.getInfoCompOdont().getDtoInfoPlanTratamiento().getUsuarioModifica().getUsuarioModifica().equalsIgnoreCase(usuario.getLoginUsuario()))
			{
				forma.setMostrarMensaje(new ResultadoBoolean(true,"Valoración y Plan de Tratamiento confirmados por "+Utilidades.obtenerNombreUsuarioXLogin(forma.getInfoCompOdont().getDtoInfoPlanTratamiento().getUsuarioModifica().getUsuarioModifica(), false)));
			}
		}
		try 
		{
			UtilidadBD.cerrarConexion(con);
		} 
		catch (SQLException e) 
		{
			logger.error("Error cerrando la conexión", e);
		}
		
		return mapping.findForward("principal");
	}


	/**
	 * Método implementado para realizar validaciones del usuario al confirmar
	 * @param forma
	 * @param usuario Usuario en sesión, para verificar que sea el mismo ingresado en los campos de validación
	 * @param mapping
	 * @param request 
	 * @return
	 */
	private ActionForward accionValidarUsuario(
			ValoracionOdontologicaForm forma, UsuarioBasico usuario,
			ActionMapping mapping, HttpServletRequest request) 
	{
		AtencionCitasOdontologia mundoAtencion = new AtencionCitasOdontologia();
		
		mundoAtencion.validarUsuarioFinConfirmacion(forma.getDtoCita(), usuario, forma.getDtoAtencionCita());
		
		if(!mundoAtencion.getErrores().isEmpty())
		{
			saveErrors(request, mundoAtencion.getErrores());
			forma.setEstado("iniciarConfirmacion");
		}
		return mapping.findForward("popUpConfirmacion");
	}


	/**
	 * Método implementado para realizar la impresión de la cita
	 * @param con
	 * @param forma
	 * @param usuario
	 * @param mapping
	 * @param request 
	 * @return
	 */
	private ActionForward accionImprimir(Connection con,ValoracionOdontologicaForm forma, UsuarioBasico usuario,ActionMapping mapping, HttpServletRequest request) 
	{
		this.cargarHistorico(con,forma,usuario,request);
		UtilidadBD.closeConnection(con);
		return mapping.findForward("imprimir");
	}
	
	/**
	 * Método implementado para realizar la impresión de la plantilla seleccionada
	 * en el historico
	 * @param con
	 * @param forma
	 * @param usuario
 	 * @param paciente
	 * @param mapping
	 * @param request 
	 * @return
	 */
	private ActionForward accionImprimirPlantillaSeleccionada(Connection con,ValoracionOdontologicaForm forma, UsuarioBasico usuario, PersonaBasica paciente, ActionMapping mapping, HttpServletRequest request) 
	{
		this.cargarPlantillaSeleccionadaDeHistorico(con,forma,usuario,paciente,request);
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
	private ActionForward accionImprimirPDF(ValoracionOdontologicaForm forma, UsuarioBasico usuario,ActionMapping mapping, HttpServletRequest request) 
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
	 * 
	 * @param con
	 * @param forma
	 * @param usuario
	 * @param request
	 */
	private void cargarHistorico(Connection con,ValoracionOdontologicaForm forma, UsuarioBasico usuario, HttpServletRequest request) 
	{
		//Se toma la informacion de la institucion basica
		//forma.setInstitucionBasica((InstitucionBasica)request.getSession().getAttribute("institucionBasica"));
		logger.info("\n\n\n >>>> Digito Verificacion >> "+forma.getInstitucionBasica().getDigitoVerificacion());
		logger.info("\n\n\n >>>> IMGEN  ubicacion >> "+forma.getInstitucionBasica().getLogoParametrizadoJSP());
		
		AtencionCitasOdontologia mundoAtencion = new AtencionCitasOdontologia();
		mundoAtencion.capturarDatosImpresion(con, new BigDecimal(forma.getCita()), usuario);
		CentroAtencionServicio centroAtencionServicio = new CentroAtencionServicio();
		CentroAtencion centroAtencion = new CentroAtencion();
		centroAtencion = centroAtencionServicio.buscarPorCodigoPK(usuario.getCodigoCentroAtencion());
		forma.getInstitucionBasica().setDireccion(centroAtencion.getDireccion());
		forma.getInstitucionBasica().setTelefono(centroAtencion.getTelefono());
		forma.setPaciente(mundoAtencion.getPaciente());
		forma.setPlantillas(mundoAtencion.getPlantillas());
		
		if(!Utilidades.isEmpty(forma.getDtoAtencionCita().getPlantillas()))
			forma.setPlantillaBase(forma.getDtoAtencionCita().getPlantillas().get(0).getNombreFuncionalidad());
		else 
			forma.setPlantillaBase("");
		forma.setUsuarioResumen(usuario);
		forma.setFechaResumen(UtilidadFecha.getFechaActual(con));
		forma.setHoraResumen(UtilidadFecha.getHoraActual(con));
		
		forma.getValoracionUrgencias().setCompIndicePlaca(forma.getCompIndicePlaca());
		forma.getValoracionUrgencias().setInfoCompOdont(forma.getInfoCompOdont());
		
		forma.setTitulo("INFORMACIÓN DE LA CITA ODONTOLÓGICA");
		
	}

	/**
	 * Método que carga la información de la plantilla seleccionada en el
	 * historico
	 * @param con
	 * @param forma
	 * @param usuario
	 * @param paciente
	 * @param request
	 */
	private void cargarPlantillaSeleccionadaDeHistorico(Connection con,ValoracionOdontologicaForm forma, UsuarioBasico usuario, PersonaBasica paciente, HttpServletRequest request) 
	{
		AtencionCitasOdontologia mundoAtencion = new AtencionCitasOdontologia();
		forma.setDtoCita(CitaOdontologica.obtenerCitaOdontologica(con, Utilidades.convertirAEntero(forma.getCita()+""),usuario.getCodigoInstitucionInt()));
		mundoAtencion.capturarDatosImpresionXPlantillaSeleccionada(con, new BigDecimal(forma.getCita()), usuario, new BigDecimal(forma.getCodigoPlantilla()));//----
		
		forma.setPaciente(mundoAtencion.getPaciente());
		ArrayList<DtoPlantilla> plantillas=new ArrayList<DtoPlantilla>();
		plantillas.add(mundoAtencion.getPlantillaSeleccionadaHistorico());
		forma.setPlantillas(plantillas);
		
		if(!Utilidades.isEmpty(forma.getPlantillas()))
			forma.setPlantillaBase(forma.getPlantillas().get(0).getNombreFuncionalidad());
		else 
			forma.setPlantillaBase("");
		
		forma.setUsuarioResumen(usuario);
		forma.setFechaResumen(UtilidadFecha.getFechaActual(con));
		forma.setHoraResumen(UtilidadFecha.getHoraActual(con));
		
		forma.getValoracionUrgencias().setCompIndicePlaca(forma.getCompIndicePlaca());
		forma.getValoracionUrgencias().setInfoCompOdont(forma.getInfoCompOdont());
		
		forma.setTitulo("INFORMACIÓN DE LA CITA ODONTOLÓGICA");
		
	}

	
	public void cargarDiagnosticosValoracion (ValoracionOdontologicaForm forma,Connection con,PersonaBasica paciente,UsuarioBasico usuario, ActionErrors errores, HttpServletRequest request, ActionMapping mapping)
	{
		///Cargo la plantilla que acabo de ingresar
		logger.info("\n\n\n\nENTRE A CONSULTAR LA PLANTILLA!!!!!!!!");
		//BigDecimal ingreso = UtilidadOdontologia.consultarIngresoCitaOdontologica(con, new BigDecimal(forma.getCita()));
		//forma.setPlantilla(Plantillas.cargarPlantillaValoracionOdon(con, usuario.getCodigoInstitucionInt(), usuario, paciente.getCodigoPersona(), "", forma.getCodigoPlantilla(),ingreso.intValue(),forma.getMundoValoracion().getDtoValoracion().getCodigoPk()));
		forma.setListadoDiagnosticosValoracion(ValoracionOdontologica.consultaDiagnosticosValOdo(forma.getMundoValoracion().getDtoValoracion().getCodigoPk()));
		forma.getInfoCompOdont().setEsResumen(ConstantesBD.acronimoSi);
	 }
	
	
	/**
	 * 
	 * @param forma
	 * @param con
	 * @param paciente
	 * @param usuario
	 * @param errores
	 * @param request
	 * @param mapping
	 * @return
	 */
	public ActionForward accionInsertar(ValoracionOdontologicaForm forma,Connection con,PersonaBasica paciente,UsuarioBasico usuario, ActionErrors errores, HttpServletRequest request, ActionMapping mapping)
	{	
		//Asigno los valores recibidos por parametro a sus respectivos dto
		//Lleno el DTO de valoracions odontologicas
		forma.getDtoValoraciones().setCita(forma.getCita());
		forma.getDtoValoraciones().setFechaModifica(UtilidadFecha.getFechaActual());
		forma.getDtoValoraciones().setHoraModifica(UtilidadFecha.getHoraActual());
		forma.getDtoValoraciones().setUsuarioModifica(usuario.getLoginUsuario());
		forma.getDtoValoraciones().setHisAntecedenteOdo(ConstantesBD.codigoNuncaValidoDouble); //quedó deprecado el uso de este campo
		forma.getDtoValoraciones().setTipoDiagnostico(forma.getValoracionUrgencias().getValoracionConsulta().getCodigoTipoDiagnostico());
		
		ValoracionOdontologica mundoValoracion = new ValoracionOdontologica();
		
		/*
		logger.info("NUMERO DE DIAGNOSTICOS DE LA VALORACION: "+forma.getValoracionUrgencias().getDiagnosticos().size());
		*/
		logger.info("CODIGO PK PLANTILLA INGRESO: "+forma.getDtoIngreso().getCodigoPK());
		
		//Validación de los campos FIJOS de la valoración
		if (forma.getPorConfirmar().equals(ConstantesBD.acronimoSi))
		{
			errores = forma.getDtoValoraciones().validate(
				forma.getValoracionUrgencias().getDiagnosticos(), 
				forma.getValoracionUrgencias().getValoracionConsulta().getCodigoTipoDiagnostico(), 
				errores, 
				"",
				forma.getPlantilla()//nombre plantilla
				);
			
			errores = Plantillas.validacionCamposPlantilla(forma.getPlantilla(), errores);
			
			forma.getInfoCompOdont().setPorConfirmar(forma.getPorConfirmar());
			
			String porConfirmar = UtilidadTexto.getBoolean(forma.getPorConfirmar())?ConstantesBD.acronimoNo:ConstantesBD.acronimoSi;
			
	        boolean existeComponenteOdontograma = forma.getPlantilla().existeComponente(ConstantesCamposParametrizables.tipoComponenteOdontogramaDiag);
	        
			errores.add(validarGuardarInformacion(forma.getInfoCompOdont(), porConfirmar, forma.getCita(), existeComponenteOdontograma));
			
		}
		
		//Utilidades.imprimirMapa(forma.getDiagnosticosRelacionados());
		//logger.info("SE IMPRIMIO EL MAPA");
		//Si no hay errores se continua normalmente el flujo
		if (errores.isEmpty())
		{
			mundoValoracion.setDtoValoracion(forma.getDtoValoraciones());
			mundoValoracion.setDtoPlantilla(forma.getPlantilla());
			mundoValoracion.setDtoPlantillasIngreso(forma.getDtoIngreso());
			mundoValoracion.setDiagnosticos(forma.getValoracionUrgencias().getDiagnosticos());
			mundoValoracion.setDiagnosticosRelacionados(forma.getDiagnosticosRelacionados());
			//logger.info("Mostrar POR: "+forma.getValoracionUrgencias().getAntecedenteOdontologico().getMostrarPor());
			mundoValoracion.setInfoAntecedenteOdonto(forma.getValoracionUrgencias().getAntecedenteOdontologico());
			mundoValoracion.setUsuario(usuario);
			
			/*
			 * Setteamos la cita 
			 * Para guardar las Especialidades 
			 */
			mundoValoracion.setDtoCita(forma.getDtoCita());
			
			//************************* COMPONENTE INDICE DE PLACA ***************************
			forma.getCompIndicePlaca().setPorConfirmar(forma.getPorConfirmar());
			logger.info("CODIGO DEL INDICE DE PLACA: "+forma.getCompIndicePlaca().getCodigoPk());
			forma.getCompIndicePlaca().setPorActualizar(forma.getCompIndicePlaca().getCodigoPk()>0?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
			/*if(forma.getCompIndicePlaca().isErroresOdontograma())
				forma.getCompIndicePlaca().setPorActualizar(ConstantesBD.acronimoNo);
			*/mundoValoracion.setIndicePlaca(forma.getCompIndicePlaca());
			//************************* FIN COMPONENTE INDICE DE PLACA ***********************
			
			//************************* COMPONENTE ODONTOGRAMA DIAGNOSTICO********************
			mundoValoracion.setInfoOdontograma(forma.getInfoCompOdont());
			forma.getInfoCompOdont().setPorConfirmar(forma.getPorConfirmar());
			//************************* FIN COMPONENTE ODONTOGRAMA DIAGNOSTICO****************
			
			UtilidadBD.iniciarTransaccion(con);
			
			forma.setMundoValoracion(mundoValoracion);
			
			//*************************SE REALIZA LA INSERCIÓN***************
			/*
			 * Nota * El valor porConfirmar que se envía al siguiente método maneja una lógica inversa al por Confirmar el ActionForm
			 */
			/*
			 * paso 2
			 */
			mundoValoracion.guardar(con, paciente, UtilidadTexto.getBoolean(forma.getPorConfirmar())?ConstantesBD.acronimoNo:ConstantesBD.acronimoSi);
			errores = mundoValoracion.getErrores();
			accionParametrosReqAnteOdo(request);
			//***************************FIN DE LA INSERCIÓN*****************
			
			if(errores.isEmpty())
			{
				//Como la cita no esta confirmada entonces entra a estar POR CONFIRMAR en estado S
				if (forma.getPorConfirmar().equals(ConstantesBD.acronimoNo))
				{
					//SI SE HACE BIEN LA INSERCIÓN REALIZÓ LA ACTUALIZACIÓN DE LA CITA
					//*************************ACTUALIZO LA CITA*********************************
					logger.info("ESTOY ACTUALIZANDO EL ESTADO POR CONFIRMAR DE LA CITA------->"+forma.getCita());
					DtoCitaOdontologica citaModificada = CitaOdontologica.obtenerCitaOdontologica(con, Utilidades.convertirAEntero(forma.getCita()+""),usuario.getCodigoInstitucionInt());
					          
					citaModificada.setUsuarioModifica(usuario.getLoginUsuario());
					citaModificada.setUsuarioRegistra(usuario);
					citaModificada.setPorConfirmar(ConstantesBD.acronimoSi);
					if(!CitaOdontologica.actualizarCitaOdontologica(con, citaModificada))
					{
						errores.add("", new ActionMessage("", new ActionMessage("errors.problemasGenericos","activando la cita para dejarla marcada por confirmar")));
					}
					else
					{
						errores.add("", new ActionMessage("errors.problemasGenericos","registrando el log de la cita"));
					}
					
					//***********************FIN ACTUALIZACION DE LA CITA*************************
				}
				
				UtilidadBD.finalizarTransaccion(con);
				
				//****************************************************************************************************************
				//Recarga la Información del Odontograma
				if(forma.getPlantilla().existeComponente(ConstantesCamposParametrizables.tipoComponenteOdontogramaDiag) && 
						mundoValoracion.getInfoOdontograma().getIndicadorAuxBd().doubleValue() > 0)
				{
					logger.info("\n\n carga el de nuevo el odonto, codigo de la valoración "+((Double)mundoValoracion.getDtoValoracion().getCodigoPk()).intValue());
					forma.getInfoCompOdont().setEvaluarEstadosPlanTratamiento(false);
					///OJO CAMBIAR AQUI----------------------------------------------------------------
					/*
					 *CODIGO VALORACION 
					 */
					accionOdontograma(con, paciente, usuario, forma, request, forma.getInfoCompOdont().getCodigoValoracion());
					//cuando se inserta no se debe usar ancla.
					forma.setAncla("");
				}
				//****************************************************************************************************************
				
				
				UtilidadBD.closeConnection(con);
				//Si se genera el resumen se debe cerrar la ventana y se debe generar el resumen
				forma.setGuardo(true);
				if (forma.getPorConfirmar().equals(ConstantesBD.acronimoSi))
					return mapping.findForward("popUpConfirmacion");
				else
					return empezar(forma, paciente, usuario, request, mapping, false);
					//return mapping.findForward("principal");
			}                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                      
			else
			{
				saveErrors(request, errores);
				UtilidadBD.abortarTransaccion(con);
				UtilidadBD.closeConnection(con);
				forma.setGuardo(false);
				return mapping.findForward("principal");
			}
		}
		else
		{
			saveErrors(request, errores);
			UtilidadBD.closeConnection(con);
			accionParametrosReqAnteOdo(request);
			forma.setGuardo(false);
			return mapping.findForward("principal");
		}
	}

	
	
	/**
	 * Realiza Validaciones Generales antes de guardar la información
	 * 
	 * @param InfoOdontograma info
	 * @param porConfirmar
	 * @param codigoCita 
	 * @param existeComponenteOdontograma 
	 * 
	 */
	private ActionErrors validarGuardarInformacion(InfoOdontograma info, String porConfirmar, double codigoCita, boolean existeComponenteOdontograma)
	{
		ActionErrors errores = new ActionErrors();
		
		//boolean encontroDiente = false;
		String ubicacion = "";
		int reg = 1;
		
		//SECCION DE PLAN DE TRATAMIENTO
		//Verifica que los hallazgos tengan clasificación
		for(InfoDetallePlanTramiento pieza:info.getInfoPlanTrata().getSeccionHallazgosDetalle())
		{
			if(pieza.getExisteBD().isActivo())
			{
				//encontroDiente = true;
				for(InfoHallazgoSuperficie superficie:pieza.getDetalleSuperficie())
				{
					if(superficie.getExisteBD().isActivo())
					{
						if(superficie.getSuperficieOPCIONAL().getCodigo() > 0)
						{
							ubicacion = " de la Superficie "+superficie.getSuperficieOPCIONAL().getNombre()+" en la Pieza Dental "+pieza.getPieza().getCodigo()+" de la Sección Plan Tratamiento Incial";
						}
						else
						{
							ubicacion = " del diente "+superficie.getSuperficieOPCIONAL().getNombre()+" en la Pieza Dental "+pieza.getPieza().getCodigo()+" de la Sección Plan Tratamiento Incial";
						}

						/*
						 * Tarea 149277
						 * No hacer requerida la clasificación para diente sano
						 */
						if(superficie.getClasificacion().getValue()!=null && superficie.getClasificacion().getValue().equals("") && (porConfirmar==null || porConfirmar.equals(ConstantesBD.acronimoNo))  && superficie.getNumProgramasServiciosActivos()>0)
						{
							errores.add("errors.notEspecific",new ActionMessage("errors.notEspecific","No existe Información de Clasificación para el Hallazgo ["+superficie.getHallazgoREQUERIDO().getNombre().toLowerCase()+"] "+ubicacion));
						}
						
						/*
						 * tarea 148223
						if(superficie.getNumProgramasServiciosActivos() <= 0)
							adicionarError("errors.notEspecific","No existen Programas/Servicios para el Hallazgo ["+superficie.getHallazgoREQUERIDO().getNombre().toLowerCase()+"] "+ubicacion);
						*/
					}
				}
				
			}
		}
		
		//SECCION DE OTROS HALLAZGOS
		reg = 1;
		for(InfoDetallePlanTramiento pieza: info.getInfoPlanTrata().getSeccionOtrosHallazgos())
		{
			if(pieza.getExisteBD().isActivo())
			{
				//encontroDiente = true;
				
				if(pieza.getPieza().getCodigo() <= 0)
					errores.add("errors.notEspecific",new ActionMessage("errors.notEspecific","No existe Pieza para el Registro Nro. "+reg+" de la Sección Otros Hallazgos del Odontograma de Diagnostico."));
			
				for(InfoHallazgoSuperficie hallazgo:pieza.getDetalleSuperficie())
				{
					if(hallazgo.getExisteBD().isActivo())
					{	
						if(hallazgo.getHallazgoREQUERIDO().getCodigo() <= 0)
							errores.add("errors.notEspecific",new ActionMessage("errors.notEspecific","No existe hallazgo para el Registro Nro. "+reg+" de la Sección Otros Hallazgos del Odontograma de Diagnostico."));
						else
						{
							/*
							//tarea 148317 xplanner2008, se solicita que la superficie no sea requerido.
							if(hallazgo.getHallazgoREQUERIDO().getCodigo2() == ComponenteOdontograma.codigoTipoHallazgoSuper)
							{
								if(hallazgo.getSuperficieOPCIONAL().getCodigo() <= 0)
									adicionarError("errors.notEspecific","No existe Superficie para el Registro Nro. "+reg+" de la Sección Otros Hallazgos del Odontograma de Diagnostico.");
							}
							*/
							/*
							 * tarea 148314
							if(hallazgo.getNumProgramasServiciosActivos() <= 0)
								adicionarError("errors.notEspecific","No existen Programas/Servicios para el Registro Nro. "+reg+" de la Sección Otros Hallazgos del Odontograma de Diagnostico.");
							*/
						}
					}
					//verficar otros hallazgos con el plan de tratamiento para encontrar repetidos.
					for(InfoDetallePlanTramiento piezaPlan:info.getInfoPlanTrata().getSeccionHallazgosDetalle())
					{
						if(piezaPlan.getExisteBD().isActivo())
						{
							//encontroDiente = true;
							for(InfoHallazgoSuperficie superficie:piezaPlan.getDetalleSuperficie())
							{
								@SuppressWarnings("unused")
								boolean superficieTerminada=superficie.getSuperficieTerminada();
								if(/*!superficieTerminada &&*/ piezaPlan.getPieza().getCodigo()==pieza.getPieza().getCodigo()
										&&
										hallazgo.getSuperficieOPCIONAL().getCodigo()==superficie.getSuperficieOPCIONAL().getCodigo()
										&&
										hallazgo.getHallazgoREQUERIDO().getCodigo()==superficie.getHallazgoREQUERIDO().getCodigo()
								)
								{
									for(InfoProgramaServicioPlan programasPlan:superficie.getProgramasOservicios())
									{
										if(programasPlan.getExisteBD().isActivo())
										{
											for(InfoProgramaServicioPlan programas:hallazgo.getProgramasOservicios())
											{
												if(programas.getExisteBD().isActivo())
												{
													if(programasPlan.getCodigoAmostrar().equals(programas.getCodigoAmostrar()))
													{
														errores.add("errors.notEspecific",new ActionMessage("errors.notEspecific"," El Registro Nro. "+reg+" de la Sección Otros Hallazgos del Odontograma de Diagnóstico, se encuentra repetido entre en el odontograma y la sección otros hallazgos."));
													}
												}
											}
										}
									}
								}
							}
							
						}
					}
					//verficar otros hallazgos con si misma para encontrar repetidos.
					for(int tempo=0;tempo<(reg-1);tempo++)
					{
						InfoDetallePlanTramiento piezaPlan=info.getInfoPlanTrata().getSeccionOtrosHallazgos().get(tempo);
						if(piezaPlan.getExisteBD().isActivo())
						{
							//encontroDiente = true;
							for(InfoHallazgoSuperficie superficie:piezaPlan.getDetalleSuperficie())
							{
								@SuppressWarnings("unused")
								boolean superficieTerminada=superficie.getSuperficieTerminada();
								if(/*!superficieTerminada && */piezaPlan.getPieza().getCodigo()==pieza.getPieza().getCodigo()
										&&
										hallazgo.getSuperficieOPCIONAL().getCodigo()==superficie.getSuperficieOPCIONAL().getCodigo()
										&&
										hallazgo.getHallazgoREQUERIDO().getCodigo()==superficie.getHallazgoREQUERIDO().getCodigo()
								)
								{
									programaExterno:for(InfoProgramaServicioPlan programasPlan:superficie.getProgramasOservicios())
									{
										for(InfoProgramaServicioPlan programas:hallazgo.getProgramasOservicios())
										{
											if(programasPlan.getCodigoAmostrar().equals(programas.getCodigoAmostrar()))
											{
												errores.add("errors.notEspecific",new ActionMessage("errors.notEspecific"," El Registro Nro. "+reg+" de la Sección Otros Hallazgos del Odontograma de Diagnóstico, se encuentra repetido."));
												break programaExterno;
											}
										}
									}
								}
							}
							
						}
					}
				}
				reg++;
			}
		}
		
		
		
		//SECCION HALLAZGOS BOCA
		reg = 1;
		//for(InfoHallazgoSuperficie hallazgo: info.getInfoPlanTrata().getSeccionHallazgosBoca())
		for(int i=0;i<info.getInfoPlanTrata().getSeccionHallazgosBoca().size();i++)
		{
			InfoHallazgoSuperficie hallazgo=info.getInfoPlanTrata().getSeccionHallazgosBoca().get(i);
			//verificarRepetidos.
			for(int j=0;j<i;j++)
			{
				InfoHallazgoSuperficie hallazgoTemporal=info.getInfoPlanTrata().getSeccionHallazgosBoca().get(j);
				boolean superficieTerminada=hallazgoTemporal.getSuperficieTerminada();
				if(!superficieTerminada && (hallazgo.getHallazgoREQUERIDO().getCodigo()==hallazgoTemporal.getHallazgoREQUERIDO().getCodigo()) && hallazgo.getExisteBD().isActivo() && hallazgoTemporal.getExisteBD().isActivo())
				{
					errores.add("errors.notEspecific",new ActionMessage("errors.notEspecific","El hallazgo para el Registro Nro. "+reg+" de la Sección Hallazgos Boca del Odontograma de Diagnostico se encuentra repetido."));
				}
				
			}
			if(hallazgo.getExisteBD().isActivo())
			{
				//encontroDiente = true; 
				
				if(hallazgo.getHallazgoREQUERIDO().getCodigo() <= 0)
					errores.add("errors.notEspecific",new ActionMessage("errors.notEspecific","No existe hallazgo para el Registro Nro. "+reg+" de la Sección Hallazgos Boca del Odontograma de Diagnostico."));
				else
				{
					if(hallazgo.getNumProgramasServiciosActivos() <= 0)
						errores.add("errors.notEspecific",new ActionMessage("errors.notEspecific","No existen Programas/Servicios para el Registro Nro. "+reg+" de la Sección Hallazgos Boca del Odontograma de Diagnostico."));
				}
			
				reg++;
			}
		}
		
		/*
		 * Se evalua si la plantilla tiene el componente Odontograma y si existe al menos un registro del 
		 * Plan de Tratamiento sin clasificación.
		 */
		if(!existeComponenteOdontograma && PlanTratamiento.existeDetPlanTratamientoSinClasificacion(new BigDecimal(codigoCita).intValue())){
			
			errores.add("errors.notEspecific",new ActionMessage("errors.atencionodontologica.planTratamientoSinClasificacion"));
		}
		

		/*
		 * Solo si existe odontograma se debe evaluar y agregar este mensaje
		 */
		
		/* Nota: esta validación se documenta ya que no esta definido que debe bloquear el
		proceso de atención de la cita cuando no registran hallazgos. */
		
//		if(existeComponenteOdontograma){
//			
//			if(!encontroDiente && porConfirmar.equals(ConstantesBD.acronimoNo))
//			{
//				errores.add("errors.notEspecific",new ActionMessage("errors.atencionodontologica.hallazgosRequeridos", null));
//			}
//		}
		
		return errores;
	}
	
	
	public void accionEmpezar(ValoracionOdontologicaForm forma,Connection con,PersonaBasica paciente,UsuarioBasico usuario,HttpServletRequest request, boolean borrarCodigoCita)
	{	
		forma.clean(request, borrarCodigoCita);

		int sexo=ConstantesBD.codigoNuncaValido;
		int centroCosto=ConstantesBD.codigoNuncaValido;
		
		//Recibo cita y plantilla por parametro
		BigDecimal bigCita = new BigDecimal(forma.getCita());
		forma.getDtoCita().setCodigoPk(bigCita.intValue());
		
		//Lleno el mundo de la atención odontologica
		AtencionCitasOdontologia mundoAtencion = new AtencionCitasOdontologia();
		mundoAtencion.cargarInfoAtencionCitaOdo(forma.getDtoCita(), usuario, true);
		
		
		
		forma.setDtoAtencionCita(mundoAtencion.getInfoAtencionCitaOdo());
		
		//Consulta si ya existe una valoracion para el paciente
		forma.setDtoIngreso(ValoracionOdontologica.consultarPacienteTieneValoracion(paciente.getCodigoPersona(), forma.getCita(),forma.getCodigoPlantilla()));
		
		logger.info("\n\n CODIGO INGRESOOO....!! >>>> " +forma.getDtoIngreso().getCodigoPK());
		//Si la búsqueda encuentra una valoración para el paciente, entonces la valoracion esta por confirmar y es para actualizala
		//Sino, se prepara una valoracion nueva para trabajar
		if (forma.getDtoIngreso().getCodigoPK()==ConstantesBD.codigoNuncaValidoDouble)
		{
			forma.setPorActualizar(ConstantesBD.acronimoNo);
		}
		else
		{
			forma.setPorActualizar(ConstantesBD.acronimoSi);
		}
		
		logger.info("\n\n\nPOR ACTUALIZAR VALUE--------->"+forma.getPorActualizar());
		
		// Crear un mensaje si ya existe una valoración confirmada por un profesional diferente (Tarea 154195)
		/*
		HashMap<String, Object> profPrimVal = UtilidadOdontologia.obtenerProfesionalPrimeraValoracion(paciente.getCodigoPersona(), paciente.getCodigoIngreso());
		if(!profPrimVal.get("usuario").toString().equals(usuario.getLoginUsuario()))
			forma.setMensaje("Valoración y Plan de Tratamiento confirmados por "+profPrimVal.get("profesional"));
		 */			
		//SE INICIALIZA LA PLANTILLA PARA CREAR UNA NUEVA 
		if (forma.getPorActualizar().equals(ConstantesBD.acronimoNo))
		{ 
			
			DtoAntecendenteOdontologico dtoAntecedente = new DtoAntecendenteOdontologico();
			dtoAntecedente.setCodigoPaciente(paciente.getCodigoPersona());
			UtilidadOdontologia.obtenerUltimoRegistroAntecedentesOdontologia(con, dtoAntecedente,true);
			
			logger.info("CREO NUEVA PLANTILLA codigoValiracionOdo=>"+dtoAntecedente.getValoracion()+", codEvolucionOdo=>"+dtoAntecedente.getEvolucion());
			forma.setPlantilla(Plantillas.cargarPlantillaParametrica(
												con, 
												forma.getCodigoPlantilla(), 
												usuario.getCodigoInstitucionInt(), 
												ConstantesCamposParametrizables.funcParametrizableValoracionConsultaExternaOdontologia, 
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
			
			
			//Se carga los valores predeterminados para fecha, hora de consulta y edad paciente para seccion gral
			forma.getDtoValoraciones().setFechaConsulta(UtilidadFecha.getFechaActual());
			forma.getDtoValoraciones().setHoraConsulta(UtilidadFecha.getHoraActual());
			forma.getDtoValoraciones().setEdadPaciente(paciente.getEdad()+"");
			
			//Se realiza una precarga de la valoración
			Valoraciones mundoValoracion = new Valoraciones();
			mundoValoracion.setPlantilla(forma.getPlantilla());
			mundoValoracion.setNumeroSolicitud(forma.getValoracionUrgencias().getNumeroSolicitud());
			mundoValoracion.precargarBaseUrgencias(con, paciente, usuario);
			mundoValoracion.getValoracionUrgencias().setNumeroSolicitud(forma.getValoracionUrgencias().getNumeroSolicitud());
			mundoValoracion.getValoracionUrgencias().setCodigoEstadoHistoriaClinica(forma.getValoracionUrgencias().getCodigoEstadoHistoriaClinica());
			
			//Se cargan las finalidades de la seccion de la finalidad de consulta
			forma.setValoracionUrgencias(mundoValoracion.getValoracionUrgencias());
			forma.setEstadosConciencia(mundoValoracion.getEstadosConciencia());
			forma.setCausasExternas(mundoValoracion.getCausasExternas());
			forma.setFinalidades(mundoValoracion.getFinalidades());
			forma.setConductasValoracion(mundoValoracion.getConductasValoracion());
			forma.setTiposDiagnostico(mundoValoracion.getTiposDiagnostico());
			forma.setTiposMonitoreo(mundoValoracion.getTiposMonitoreo());
			
		}
		
		//SE CARGA LA PLANTILLA EXISTENTE PARA LA VALORACIÓN DE USUARIO
		else
		{
			logger.info("CARGO PLANTILLA EXISTENTE");
			forma.setDtoValoraciones(ValoracionOdontologica.consultarValoracionPaciente(paciente.getCodigoPersona(),forma.getCita(),forma.getCodigoPlantilla()));
			logger.info("EL VALOR DE PLANTILLA INGRESO-------->"+forma.getDtoIngreso().getCodigoPK()+", VALOR DE VALORACION ODONTO: "+forma.getDtoIngreso().getValoracionOdonto());
			forma.setPlantilla(Plantillas.cargarPlantillaValoracionOdon(con, usuario.getCodigoInstitucionInt(), usuario, paciente.getCodigoPersona(), "", forma.getCodigoPlantilla(),forma.getDtoIngreso().getIngreso(),forma.getDtoIngreso().getValoracionOdonto()));

			//SE REVISA EL CONSECUTIVO HISTORIVO QUE VIENE DESDE QUE SE CARGA LA PLANTILLA
			/*
			logger.info("+++++++++++++ANALISIS DE LA INFORMACION DEL APLANTILLA+++++++++++++++++++++++++++++++");
			for(DtoSeccionFija seccionFija:forma.getPlantilla().getSeccionesFijas())
			{
				for(DtoElementoParam elemento:seccionFija.getElementos())
				{
					if(elemento.isComponente())
					{
						
						for(DtoElementoParam subelemento:elemento.getElementos())
						{
						
							if(subelemento.isSeccion())
							{
								for(DtoSeccionParametrizable seccion:subelemento.getSecciones())
								{
									logger.info("Nombre Seccion: "+seccion.getDescripcion());
									
									
									for(DtoCampoParametrizable campo:seccion.getCampos())
									{
										logger.info("-----------------------------------------");
										logger.info("nombre campo: "+campo.getEtiqueta());
										logger.info("valor campo: "+campo.getValor());
										logger.info("consecutivo historico: "+campo.getConsecutivoHistorico());
										
										for(DtoOpcionCampoParam opcion:campo.getOpciones())
										{
											logger.info("valor opcion-> "+opcion.getValor());
											logger.info("consecutivo historico opcion-> "+opcion.getCodigoHistorico());
										}
										logger.info("-----------------------------------------------");
									}
									
								}
							}
						}
					}
				}
			}
			logger.info("+++++++++++++FIN ANALISIS DE LA INFORMACION DEL APLANTILLA+++++++++++++++++++++++++++++++");
			*/
			//Consulta los diagostocos de esa valoracion
			forma.setListadoDiagnosticosValoracion(ValoracionOdontologica.consultaDiagnosticosValOdo(forma.getDtoIngreso().getValoracionOdonto()));
			
			//Seteo los diagnosticos con las variables correspondientes en el form para msotrar en la actualziacion los que ya ahy parametrizados
			int numRegsDx=forma.getListadoDiagnosticosValoracion().size();
			
			Valoraciones mundoValoracion = new Valoraciones();
			forma.setValoracionUrgencias(mundoValoracion.getValoracionUrgencias());
			
			/**
			 * SECCION DE DIAGNOSTICOS
			 */
			
			//Variable para manejar los dx relacionados
			int numDxRel=0;
			
			//Inicio el ciclo para la agregar los dx
			for (int i=0;i<numRegsDx;i++)
			{
				//Se agrega el diagnostico principal
				if (forma.getListadoDiagnosticosValoracion().get(i).getPrincipal().equals(ConstantesBD.acronimoSi))
				{
					Diagnostico diag = new Diagnostico();
					diag.setNombre(forma.getListadoDiagnosticosValoracion().get(i).getNombreDiagnostico());
					diag.setTipoCIE(forma.getListadoDiagnosticosValoracion().get(i).getTipoCleDiagnostico());
					diag.setAcronimo(forma.getListadoDiagnosticosValoracion().get(i).getAcronimoDiagnostico());
					forma.getValoracionUrgencias().getDiagnosticos().add(0,diag);
				}
				//Se agregan los diagnosticos relacionados
				else if (forma.getListadoDiagnosticosValoracion().get(i).getPrincipal().equals(ConstantesBD.acronimoNo))
				{
					forma.setNumDiagRelacionados(numDxRel+1);
					//Se ponen todos como activos
					forma.getDiagnosticosRelacionados().put("checkbox_"+numDxRel,"true");
					//Agrego al mapa con nombre de numero por campo, los elementos  concatenados con els eparador split
					forma.getDiagnosticosRelacionados().put(""+numDxRel+"",""+forma.getListadoDiagnosticosValoracion().get(i).getAcronimoDiagnostico()+""+ConstantesBD.separadorSplit+""+forma.getListadoDiagnosticosValoracion().get(i).getTipoCleDiagnostico()+""+ConstantesBD.separadorSplit+""+forma.getListadoDiagnosticosValoracion().get(i).getNombreDiagnostico());
					numDxRel++;
				}
			}
			//Si no había diagnósticos se ingresa uno vacío por defecto en principal
			if(forma.getValoracionUrgencias().getDiagnosticos().size()==0)
			{
				forma.getValoracionUrgencias().getDiagnosticos().add(new Diagnostico("",ConstantesBD.codigoNuncaValido,true));
			}
			//Se agrega el tipo de diagonstico
			forma.getValoracionUrgencias().getValoracionConsulta().setCodigoTipoDiagnostico(forma.getDtoValoraciones().getTipoDiagnostico());
			
			/**
			 * FIN SECCION DIAGNSOTICOS
			 */
			
			logger.info("\n\n\n\n\n\n*******EL MAPA DE DIAGNOSTICO ES******\n\n\n\n\n");
			Utilidades.imprimirMapa(forma.getDiagnosticosRelacionados());
			
			//Se cargan los demas elementos de la plantilla
			//Se carga los valores predeterminados para fecha, hora de consulta y edad paciente para seccion gral
			forma.getDtoValoraciones().setFechaConsulta(UtilidadFecha.getFechaActual());
			forma.getDtoValoraciones().setHoraConsulta(UtilidadFecha.getHoraActual());
			forma.getDtoValoraciones().setEdadPaciente(paciente.getEdad()+"");
			
			//Se realiza una precarga de la valoración
			//Valoraciones mundoValoracion = new Valoraciones();
			mundoValoracion.setPlantilla(forma.getPlantilla());
			mundoValoracion.setNumeroSolicitud(forma.getValoracionUrgencias().getNumeroSolicitud());
			mundoValoracion.precargarBaseUrgencias(con, paciente, usuario);
			mundoValoracion.getValoracionUrgencias().setNumeroSolicitud(forma.getValoracionUrgencias().getNumeroSolicitud());
			mundoValoracion.getValoracionUrgencias().setCodigoEstadoHistoriaClinica(forma.getValoracionUrgencias().getCodigoEstadoHistoriaClinica());
			
			//Se cargan las finalidades de la seccion de la finalidad de consulta
			//forma.setValoracionUrgencias(mundoValoracion.getValoracionUrgencias());
			forma.setEstadosConciencia(mundoValoracion.getEstadosConciencia());
			forma.setCausasExternas(mundoValoracion.getCausasExternas());
			forma.setFinalidades(mundoValoracion.getFinalidades());
			forma.setConductasValoracion(mundoValoracion.getConductasValoracion());
			forma.setTiposDiagnostico(mundoValoracion.getTiposDiagnostico());
			forma.setTiposMonitoreo(mundoValoracion.getTiposMonitoreo());
			
		}
		
	}

	/**
	 * M&eacute;todo Centralizado para el manejo de las acciones sobre el odontograma
	 * @param con
	 * @param paciente
	 * @param usuario
	 * @param forma
	 * @param request
	 * @param codigoPkValoracion
	 * @return
	 */
	public String accionOdontograma(
			Connection con,
			PersonaBasica paciente,
			UsuarioBasico usuario,
			ValoracionOdontologicaForm forma,
			HttpServletRequest request,
			int codigoPkValoracion)
	{
		logger.info("Estado para el odontograma >> "+forma.getEstadoSecundario());
				
		ComponenteOdontograma odonto = new ComponenteOdontograma();
		forma.getInfoCompOdont().setInstitucion(usuario.getCodigoInstitucionInt());
		
		
		if(forma.getEstadoSecundario().equals("empezar") 
				|| forma.getEstadoSecundario().equals("empezarPopUp"))
		{
			forma.setInfoCompOdont(odonto.accionEmpezar(
														forma.getInfoCompOdont(),
														paciente.getCodigoPersona(),
														paciente.getCodigoIngreso(),
														paciente.getEdad(),
														Utilidades.convertirAEntero(forma.getCita()+""),
														codigoPkValoracion, 
														ConstantesBD.codigoNuncaValido, 
														ConstantesIntegridadDominio.acronimoInicial, 
														ConstantesIntegridadDominio.acronimoOdontogramaDiagnostico, 
														usuario,
														forma.getEstadoSecundario(),
														request.getContextPath(),
														forma.getEstadoSecundario().equals("empezarPopUp")?true:false));
			
			Log4JManager.info("Imagen----------->"+forma.getInfoCompOdont().getInfoPlanTrata().getImagen());
			
		}
		else
		{
			forma.setInfoCompOdont(odonto.centralAcciones(forma.getInfoCompOdont(),forma.getEstadoSecundario(), new BigDecimal(codigoPkValoracion)));
		}
		if(!odonto.getErrores().isEmpty())
		{
			saveErrors(request,odonto.getErrores());
		}
		
		request.setAttribute("nombreAtributoOdontograma","infoCompOdont");
		request.setAttribute("nombreForm","ValoracionOdontologicaForm");
		request.setAttribute("pathAction","/valoracionOdontologica/valoracion.do");
		
		forma.setEstadoSecundario(odonto.getEstadoInterno());
		UtilidadBD.closeConnection(con);
		
		//atributo para redireccionar siempre el navegador al ancla de la pagina.
		forma.setAncla("odontograma");
		if(odonto.getForwardOdont().equals("") && !forma.getInfoCompOdont().isMostrarSoloOdontograma())
		{
			return "principal";
		}
		else if (odonto.getForwardOdont().equals("") && forma.getInfoCompOdont().isMostrarSoloOdontograma())
		{
			return ComponenteOdontograma.forwardOdontVerSoloOdonto;
		}
		else
		{
			return odonto.getForwardOdont();
		}
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
		forma.getValoracionUrgencias().getAntecedenteOdontologico().setUtilizaProgOdonto(ValoresPorDefecto.getUtilizanProgramasOdontologicosEnInstitucion(usuario.getCodigoInstitucionInt()));
		logger.info("Estado para el Antecedente odontologico >> "+forma.getEstadoSecundario());
		logger.info("mostrar por: "+forma.getValoracionUrgencias().getAntecedenteOdontologico().getMostrarPor());
		if(forma.getEstadoSecundario().equals("empezar"))
		{
			accionEmpezarAnteOdo(request, forma, usuario, paciente, con);
		}else{
		
			ActionErrors errores=ComponenteAnteOdoto.accionesEstados(forma.getValoracionUrgencias().getAntecedenteOdontologico(),forma.getEstadoSecundario(),false);
			//anteOdonto.cargarComponenteAnteOdonto(forma.getInfoCompAnteOdont());
		
			if(!errores.isEmpty())
			{
				saveErrors(request,errores);
			}
		
			accionParametrosReqAnteOdo(request);
			
		}
		forma.setEstadoSecundario("");
		try {
			UtilidadBD.cerrarConexion(con);
		} catch (SQLException e) {
			logger.info("Error cerrando la conexión", e);
		}

		//atributo para redireccionar siempre el navegador al ancla de la pagina.
		forma.setAncla("antecedentes");
		
		return mapping.findForward(forma.getValoracionUrgencias().getAntecedenteOdontologico().getForward());
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
		ActionErrors errores=ComponenteAnteOdoto.llenadoInicialInterfaz(forma.getValoracionUrgencias().getAntecedenteOdontologico(), con);
		if(errores.isEmpty())
		{
			// con este parametro savemos que tipo de antecedentee realizar
			forma.getValoracionUrgencias().getAntecedenteOdontologico().setPorConfirmar(forma.getPorActualizar());
			if(forma.getPorActualizar().equals(ConstantesBD.acronimoSi))
				forma.getValoracionUrgencias().getAntecedenteOdontologico().setCodigoAnteOdon(Utilidades.convertirAEntero(Double.toString(forma.getDtoIngreso().getValoracionOdonto())));
			
			// se verifica y se llena el parametro general utiliza programa odontologico
			forma.getValoracionUrgencias().getAntecedenteOdontologico().setUtilizaProgOdonto(ValoresPorDefecto.getUtilizanProgramasOdontologicosEnInstitucion(usuario.getCodigoInstitucionInt()));
			
			forma.getValoracionUrgencias().getAntecedenteOdontologico().setCodigoPaciente(paciente.getCodigoPersona());
			forma.getValoracionUrgencias().getAntecedenteOdontologico().setMostrarPor(UtilidadTexto.getBoolean(forma.getValoracionUrgencias().getAntecedenteOdontologico().getUtilizaProgOdonto())
					?ConstantesIntegridadDominio.acronimoMostrarProgramas
					:ConstantesIntegridadDominio.acronimoMostrarServicios);
			
			// control de los acciones internas del componente
			errores=ComponenteAnteOdoto.accionesEstados(forma.getValoracionUrgencias().getAntecedenteOdontologico(),forma.getEstadoSecundario(),false);
			
			accionParametrosReqAnteOdo(request);
		}
		saveErrors(request,errores);
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
		request.setAttribute("nombreForm","ValoracionOdontologicaForm");
		request.setAttribute("pathAction","/valoracionOdontologica/valoracion.do");
	
		forma.setEstadoSecundario("");
		//atributo para redireccionar siempre el navegador al ancla de la pagina.
		forma.setAncla("indicePlaca");
		return mapping.findForward(forma.getCompIndicePlaca().getForward());
	}
	
	/**
	 * Método implementado para redireccionar la plantilla
	 * @param forma
	 * @param response
	 * @param request 
	 * @param usuario 
	 * @return
	 */
	private ActionForward redireccionarPlantilla(ValoracionOdontologicaForm forma, HttpServletResponse response, HttpServletRequest request, UsuarioBasico usuario)
	{
		//Se carga el paciente en sesion
		PersonaBasica paciente = (PersonaBasica)request.getSession().getAttribute("pacienteActivo");
		paciente.setCodigoPersona(paciente.getCodigoPersona());
		Connection con = UtilidadBD.abrirConexion();
		UtilidadesManejoPaciente.cargarPaciente(con, usuario, paciente, request);
		UtilidadBD.closeConnection(con);
		try 
		{
			logger.info("\n\n\n***************************************Redirecciono la plantilla al estado empezar!!!**********************\n\n\n\n" +
					"cita:"+forma.getCita()+"\n\n"+"\n\nPlantilla"+forma.getDtoAtencionCita().getPlantillas().get(forma.getPosicionPlantilla()).getCodigoPK()+"\n\n\n");
			response.sendRedirect("../valoracionOdontologica/valoracion.do?estado=empezar&cita="+forma.getCita()+"&codigoPlantilla="+forma.getDtoAtencionCita().getPlantillas().get(forma.getPosicionPlantilla()).getCodigoPK()+"&posicionPlantilla="+forma.getPosicionPlantilla());
		} 
		catch (IOException e) 
		{
			logger.error("Error tratando de redireccionar la valoracion odontologica: ",e);
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
	private ActionForward accionIniciarConfirmacion(ValoracionOdontologicaForm forma, UsuarioBasico usuario,ActionMapping mapping) 
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
	 */
	private ActionForward accionGuardarConfirmacion(Connection con,ValoracionOdontologicaForm forma, UsuarioBasico usuario,ActionMapping mapping, HttpServletRequest request, PersonaBasica paciente, ActionErrors errores) 
	{
		AtencionCitasOdontologia mundoAtencion = new AtencionCitasOdontologia();
		logger.info("VALOR DE POR CONFIRMAR----->"+forma.getPorConfirmar());
		
		
		
		logger.info("\n\n\n\n ");
		logger.info(" Especiadlidad Unidad Agenda--->"+forma.getDtoCita().getAgendaOdon().getEspecialidadUniAgen());
		logger.info("\n\n\n\n ");
		//Realizo la inserción
		logger.info("#######################INICIO INSERCION VALORACION############################################################");
		/**
		 * ***************
		 * paso 1
		 */
		accionInsertar(forma, con, paciente, usuario, errores, request, mapping);
		try 
		{
			if(con.isClosed())
			{
				con=UtilidadBD.abrirConexion();
			}
			logger.info("#######################FIN INSERCION VALORACION############################################################");
			if(errores.isEmpty() && forma.isGuardo())
			{
				
				logger.info("#######################INICIO PROCESO CONFIRMACION############################################################");
				
				if (forma.getPorConfirmar().equals(ConstantesBD.acronimoSi))
				{
					//valida si el Componente de Odontograma se encuentra habilitado y recarga el codigoPk en caso de 
					//encontrar
				
					/*
					 * codigo Valoracion modificacion?
					 */
					InstitucionBasica institucion = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
					
					forma.setInstitucionBasica(institucion);
					
					mundoAtencion.confirmarCita(forma.getDtoCita(), usuario, forma.getDtoAtencionCita(),paciente,false,true, request.getSession().getId(), forma.getInfoCompOdont().getCodigoValoracion(), forma.getInstitucionBasica());
					forma.setFacturasAutomaticas(mundoAtencion.getFacturasAutomaticas());
					
				}
				logger.info("#######################FIN PROCESO CONFIRMACION############################################################");
						
				if (forma.getPorConfirmar().equals(ConstantesBD.acronimoSi))
				{
					forma.setEstado("irResumen");
				}
				else if (forma.getPorConfirmar().equals(ConstantesBD.acronimoNo))
				{
					forma.setEstado("empezar");
				}
				
				forma.setCerrarVentanaConf(ConstantesBD.acronimoSi);
				
				logger.info("\n\n\n");
				logger.info("ENTRE CONSULTE E INSERTÉ Y EL VALOR DEL ESTADO Y CERRAR VENTANA ES------->"+forma.getEstado()+"-"+forma.getCerrarVentanaConf());
				logger.info("\n\n\n");
				
				//Consulto los diagnosticos d ela valoracion
				this.cargarDiagnosticosValoracion(forma, con, paciente, usuario, errores, request, mapping);
				
				if(!mundoAtencion.getErrores().isEmpty())
				{
					forma.setEstado("iniciarConfirmacion");
					forma.setCerrarVentanaConf(ConstantesBD.acronimoNo);
					saveErrors(request, mundoAtencion.getErrores());
				}
			}else
			{
				UtilidadBD.cerrarConexion(con);
				//mundoAtencion.cargarPlantillasAtencion(forma.getDtoCita(), usuario);
				//forma.getDtoAtencionCita().setPlantillas(mundoAtencion.getInfoAtencionCitaOdo().getPlantillas());
				logger.info("\n\n >>>>>>>>>>>>> TIENE ERRORES >>>>>>>>>< ");
				return mapping.findForward("principal");
			}
					
			
			if(mundoAtencion.getErrores().size()==0&&forma.getPorConfirmar().equals(ConstantesBD.acronimoSi)&&errores.size()==0 && forma.isGuardo())
			{
				this.cargarHistorico(con,forma,usuario,request);				
				forma.setListadoDiagnosticosValoracion(ValoracionOdontologica.consultaDiagnosticosValOdo(forma.getInfoCompOdont().getCodigoValoracion()));
				mundoAtencion.cargarPlantillasAtencion(forma.getDtoCita(), usuario);
				//forma.getDtoAtencionCita().setPlantillas(mundoAtencion.getInfoAtencionCitaOdo().getPlantillas());// Mayo 14 
				UtilidadBD.cerrarConexion(con);
				return mapping.findForward("resumen");
			}
			else
			{
				if(mundoAtencion.getErrores().size()>0)
				{			
					mundoAtencion.cargarPlantillasAtencion(forma.getDtoCita(), usuario);
					forma.getDtoAtencionCita().setPlantillas(mundoAtencion.getInfoAtencionCitaOdo().getPlantillas());
					return mapping.findForward("principal");
				}
				else
				{
					mundoAtencion.cargarPlantillasAtencion(forma.getDtoCita(), usuario);
					forma.getDtoAtencionCita().setPlantillas(mundoAtencion.getInfoAtencionCitaOdo().getPlantillas());
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("principal");
					//return empezar(forma, paciente, usuario, request, mapping, false);
				}
			}
		} catch (Exception e) {
			logger.error("ERROR",e);
			return null;
		}
	}
	
	
	/**
	 * 
	 * @param request
	 */
	private void accionParametrosReqAnteOdo(HttpServletRequest request) {
		request.setAttribute("nombreAtributoAnteOdonto","valoracionUrgencias.antecedenteOdontologico");
		request.setAttribute("nombreForm","ValoracionOdontologicaForm");
		request.setAttribute("pathAction","/valoracionOdontologica/valoracion.do");
	}
	
}
