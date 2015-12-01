package com.princetonsa.action.facturacion;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import util.Listado;
import util.LogsAxioma;
import util.RespuestaValidacion;
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadSesion;
import util.UtilidadTexto;
import util.UtilidadValidacion;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.facturacion.UtilidadesFacturacion;
import util.historiaClinica.UtilidadesHistoriaClinica;
import util.manejoPaciente.UtilidadesManejoPaciente;

import com.princetonsa.actionform.facturacion.RevisionCuentaPacienteForm;
import com.princetonsa.mundo.Cuenta;
import com.princetonsa.mundo.IngresoGeneral;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.cargos.Contrato;
import com.princetonsa.mundo.carteraPaciente.DocumentosGarantia;
import com.princetonsa.mundo.facturacion.EntidadesSubContratadas;
import com.princetonsa.mundo.facturacion.RevisionCuenta;
import com.servinte.axioma.fwk.exception.IPSException;

/**
 * @author Jose Eduardo Arias Doncel
 * jearias@princetonsa.com
 * */
public class RevisionCuentaPacienteAction extends Action
{	
	
	Logger logger = Logger.getLogger(RevisionCuentaPacienteAction.class);
	
	
	/**
	 * Metodo execute del action
	 * */	
	public ActionForward execute(ActionMapping mapping,
								 ActionForm form, 
								 HttpServletRequest request, 
								 HttpServletResponse response) throws Exception
								 {		
		Connection con = null;
		try{
			if(response == null);

			if (form instanceof RevisionCuentaPacienteForm) 
			{

				con = UtilidadBD.abrirConexion();

				if(con == null)
				{
					request.setAttribute("CodigoDescripcionError","erros.problemasBd");
					return mapping.findForward("paginaError");
				}

				UsuarioBasico usuario = (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");			 
				//paciente cargado en sesion 
				PersonaBasica paciente= (PersonaBasica)request.getSession().getAttribute("pacienteActivo");


				RevisionCuentaPacienteForm forma = (RevisionCuentaPacienteForm)form;
				//inicia la cuenta a tomar 
				iniciarCuenta(forma, paciente);
				String estado = forma.getEstado();

				ActionErrors errores = new ActionErrors();

				logger.info("-------------------------------------");
				logger.info("Valor del Estado >> "+forma.getEstado());
				logger.info("-------------------------------------");

				if(estado == null)
				{
					forma.reset();
					logger.warn("Estado no Valido dentro del Flujo de Revision de la Cuenta (null)");				 
					request.setAttribute("CodigoDescripcionError","errors.estadoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}


				//*******************************************************************************
				//********************************Estados del Action*****************************



				//************************************************************************************
				//********************************Estados para Responsables***************************

				//Estado de Ordenamiento del mapa responsables Map			 
				else if(estado.equals("ordenarResponsables"))
				{	
					String temporal1;
					String temporal2;				 

					temporal1 = forma.getResponsablesMap("funcionalidadModificarTarifas")+"";
					temporal2 = forma.getResponsablesMap("funcionalidadRevisionCuenta")+"";				 

					forma.setResponsablesMap(this.accionOrdenarMapa(forma.getResponsablesMap(),forma));

					forma.setResponsablesMap("funcionalidadModificarTarifas", temporal1);
					forma.setResponsablesMap("funcionalidadRevisionCuenta", temporal2);

					validacionesCuenta(con,forma,paciente,usuario);

					UtilidadBD.closeConnection(con);
					return mapping.findForward("listarResponsable");					
				}

				//Estado Inicial para cargar los responsables en Subcuenta
				if(estado.equals("cargarResponsables"))
				{				
					errores = validarIngresoFuncionalidad(con,forma,errores,paciente,usuario);
					accionPrepararBusqueda(forma);

					forma.setOffset(0);
					forma.setMaxPageItems(ValoresPorDefecto.getMaxPageItemsInt(usuario.getCodigoInstitucionInt()));

					if(!errores.isEmpty())
					{
						saveErrors(request,errores);	
						UtilidadBD.closeConnection(con);			
						return mapping.findForward("listarResponsable");
					}				

					accionCargarResponsable(con, forma, paciente,usuario);				
					validacionesCuenta(con,forma,paciente,usuario);


					UtilidadBD.closeConnection(con);
					return mapping.findForward("listarResponsable");				
				}

				//muestra la jsp asociada al forward
				if(estado.equals("irListarResponsable"))
				{ 
					validacionesCuenta(con,forma,paciente,usuario);

					forma.setLinkSiguiente("");
					forma.setPatronOrdenar("");
					forma.setUltimoPatron("");
					forma.setOffset(0);
					forma.setMaxPageItems(ValoresPorDefecto.getMaxPageItemsInt(usuario.getCodigoInstitucionInt()));			 	

					return UtilidadSesion.redireccionar(forma.getLinkSiguiente(), forma.getMaxPageItems(), Integer.parseInt(forma.getResponsablesMap("numRegistros")+""), response, request, "../revisionCuenta/listarResponsable.jsp", false);
				}


				//************************************************************************************
				//********************************Estados para Requisitos del Ingreso*****************

				//carga los requisitos de ingreso de tipo ingreso
				else if(estado.equals("requisitosIngreso"))
				{
					accionCargarRequisitos(con,forma,ConstantesIntegridadDominio.acronimoIngreso);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("listarRequisitos");				 
				}

				//carga los requisitos de ingreso de tipo egreso
				else if(estado.equals("requisitosEgreso"))
				{
					accionCargarRequisitos(con,forma,ConstantesIntegridadDominio.acronimoEgreso);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("listarRequisitos");				 
				}

				//modifica los requisitos de ingreso de cualquier tipo
				else if(estado.equals("modificarRequisitos"))
				{
					accionModificarRequisitos(con,forma,usuario);				 
					UtilidadBD.closeConnection(con);
					return mapping.findForward("listarRequisitos");
				}


				//************************************************************************************
				//********************************Estados para Solicitudes por Responsable************

				//carga las solicitudes por el responsable escogido   
				else if(estado.equals("irCargarSolicitudesResponsable"))
				{					 
					accionCargarListadoSolicitudesReponsable(con,forma);				 			 
					accionValidacionIngresoSolicitudes(con,forma,paciente);						 
					UtilidadBD.closeConnection(con);				 
					return mapping.findForward("listarSolicitudesResponsable");					 
				}

				//carga el listado de solicitudes por responsable
				else if(estado.equals("irListadoSolicitudesResponsable"))				 
				{
					accionCargarListadoSolicitudesReponsable(con,forma);				 			 
					accionValidacionIngresoSolicitudes(con,forma,paciente);						 
					UtilidadBD.closeConnection(con);				 
					return mapping.findForward("listarSolicitudesResponsable");

					//Se moddifico por la Tarea 50917. Donde indica que el botón volver debe
					//carga la modificación de los datos. Se copio lo que hace el estado 
					//irCargarSolicitudesResponsable, con la intención de no dañar el
					//funcionamiento actual
					/*UtilidadBD.closeConnection(con);
				 //actualiza el indicador a no paquete porque 
				 //el listado de la solicitud por responsable contiene
				 //el paquete
				 forma.setIndicadorTipoListadoDetalle("noPaquete");
				 return mapping.findForward("listarSolicitudesResponsable");*/				 
				}

				//carga el listado de solicutudes por paquete
				else if(estado.equals("irListadoSolicitudesDetallePaquete"))
				{
					UtilidadBD.closeConnection(con);
					forma.setIndicadorTipoListadoDetalle("paquete");
					return mapping.findForward("listarSolicitudesDetallePaquete");	 
				}

				//busqueda avanzada de solicitudes
				else if(estado.equals("busquedaAvanzadaSolicitudes"))
				{
					accionPrepararBusqueda(forma);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("busquedaAvanzadaSolicitudes");
				}

				//Estado de Ordenamiento del mapa Listar Solicitudes			 
				else if(estado.equals("ordenarListarSolicitudes"))
				{	
					forma.setListadoSolicitudesMap(this.accionOrdenarMapa(forma.getListadoSolicitudesMap(),forma));				 
					accionValidacionIngresoSolicitudes(con,forma,paciente);						 
					UtilidadBD.closeConnection(con);

					return mapping.findForward("listarSolicitudesResponsable");					
				}	

				// Estado del Pager para el listado Solicitudes  
				else if (estado.equals("redireccion"))
				{
					logger.info("ESTADO TODOS -->"+forma.getTodosEstadoCargo()+"<-");
					UtilidadBD.closeConnection(con);
					response.sendRedirect(forma.getLinkSiguiente());
					return null;
				}

				//Estado de Ordenamiento del mapa Detalle Solicitudes Paquete
				else if(estado.equals("ordenarListarDetallePaquete"))
				{
					forma.setListadoDetallePaqueteMap(this.accionOrdenarMapa(forma.getListadoDetallePaqueteMap(),forma));				 
					UtilidadBD.closeConnection(con);				 
					return mapping.findForward("listarSolicitudesDetallePaquete");
				}		 

				//************************************************************************************
				//********************************Estados para Detalle de Solicitudes*****************

				//dirige el flujo al detalle de la solicitud
				else if(estado.equals("irDetalleSolicitud"))
				{
					return cargarInterfazSolicitudDetalle(con,forma,mapping);				  
				}

				//guarda los cambios generados en el detalle de la solicitud
				else if(estado.equals("guardarDetalleSolicitudes"))
				{
					errores = accionGuardarDetalleSolicitud(con,forma,errores,usuario);

					if(!errores.isEmpty())
					{
						saveErrors(request,errores);	
						UtilidadBD.closeConnection(con);			
						return mapping.findForward(forma.getIndexForwardDetalleSolicitudes());
					}				

					return cargarInterfazSolicitudDetalle(con,forma,mapping);				
				}

				//************************************************************************************
				//********************************Estados para Pooles*********************************
				else if(estado.equals("listarPooles"))
				{
					cargarListadoPooles(con,forma);
					forma.setIndicadorTipoListadoDetalle("noPaquete");
					UtilidadBD.closeConnection(con);
					return mapping.findForward("listarPooles");
				}

				//	Estado de Ordenamiento del mapa Listar Solicitudes Pooles			 
				else if(estado.equals("ordenarListarSolicitudesPool"))
				{	
					forma.setListadoSolicitudesMap(this.accionOrdenarMapa(forma.getListadoSolicitudesMap(),forma));									 
					UtilidadBD.closeConnection(con);				 
					return mapping.findForward("listarPooles");					
				}

				// Estado del Pager para el listado de Pooles  
				else if (estado.equals("redireccionPool"))
				{
					UtilidadBD.closeConnection(con);
					response.sendRedirect(forma.getLinkSiguiente());
					return null;
				}

				// Carga los pooles del medico 
				else if(estado.equals("listarPoolMedico"))
				{				
					cargarInterfazPoolesMedico(con,forma,"listado");			
					UtilidadBD.closeConnection(con);
					return mapping.findForward("listarPoolesMedico");
				}

				// Carga los pooles del medico  
				else if(estado.equals("listarPoolCirugiaMedico"))
				{
					cargarInterfazPoolesMedico(con,forma,"cirugia");			
					UtilidadBD.closeConnection(con);
					return mapping.findForward("listarPoolesMedico");
				}

				// Carga los pooles del medico desde el listado de un paquete  
				else if(estado.equals("listarPoolPaquete"))
				{
					cargarInterfazPoolesMedico(con,forma,"paquete");			
					UtilidadBD.closeConnection(con);
					return mapping.findForward("listarPoolesMedico");
				}


				//modifica el pool por medico  
				else if(estado.equals("modificarPool"))
				{
					modificarPoolesMedico(con,forma,errores);							
					cargarListadoPooles(con,forma);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("listarPooles");
				}

				//modifica el pool del liostado de paquete
				else if(estado.equals("modificarPoolPaquete"))
				{
					modificarPoolesPaquete(con,forma,errores);							
					cargarListadoPooles(con,forma);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("listarPooles");				
				}

				//carga el detalle de la solicitud 
				else if(estado.equals("irDetalleSolicitudPool"))
				{
					return cargarInterfazSolicitudDetallePool(con,forma,mapping);						
				}

				//muestra el listado de pooles
				else if(estado.equals("irListarPooles"))
				{
					UtilidadBD.closeConnection(con);
					forma.setIndicadorTipoListadoDetalle("noPaquete");
					return mapping.findForward("listarPooles");						
				}

				//modifica los pooles de un tipo de solicitud cirugia
				else if(estado.equals("modificarPoolCirugia"))
				{
					modificarPoolesCirugia(con, forma, errores);							
					return cargarInterfazSolicitudDetallePool(con,forma,mapping);	
				}

				//carga el listado de pooles por paquete
				else if(estado.equals("irListadoPoolPaquete"))
				{
					UtilidadBD.closeConnection(con);
					forma.setIndicadorTipoListadoDetalle("paquete");
					return mapping.findForward("detSolicitudPaquetePool");	 
				}
				else if(estado.equals("guardarEstadoCargo"))
				{	 
					accionGuardarEstadoCargo(con, forma, errores, usuario);
					//	 forma.setEsExcenta("");
					//	 accionCargarListadoSolicitudesReponsable(con,forma);				 			 
					//	 accionValidacionIngresoSolicitudes(con,forma,paciente);						 
					UtilidadBD.closeConnection(con);				 
					return mapping.findForward("listarSolicitudesResponsable");					 
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
	
	//*********************************************************************************************
	//************************************Metodos**************************************************
	//*********************************************************************************************
	
	
	/*******************************************************************************************************************
	 * Metodos Responsables
	 ******************************************************************************************************************/
	
	/**
	 * Valida el Ingreso a la Funcionalidad 
	 * @param ActionErrors errores
	 * @param PersonaBasica persona
	 * */
	private ActionErrors validarIngresoFuncionalidad(Connection con,
													 RevisionCuentaPacienteForm forma,
													 ActionErrors errores,
													 PersonaBasica paciente,
													 UsuarioBasico usuario)	
	{
		forma.reset();
		
		if(paciente.getCodigoPersona()<1)
			errores.add("descripcion",new ActionMessage("errors.invalid","paciente null o sin  id. Paciente "));
	
		RespuestaValidacion respuesta = new RespuestaValidacion("el viejo ",true);		
		RespuestaValidacion respPrevia = UtilidadValidacion.validacionPreviaIngresoPaciente(con, paciente.getCodigoTipoIdentificacionPersona(), paciente.getNumeroIdentificacionPersona(), usuario.getCodigoInstitucion() );
		respuesta = UtilidadValidacion.esValidoPacienteCargado(con, paciente);
		
		if(respuesta.puedoSeguir == false)
			errores.add("descripcion",new ActionMessage(respuesta.textoRespuesta));
		
		if(paciente.getCodigoIngreso()==0 || (paciente.getCodigoIngreso()!=0 && UtilidadesHistoriaClinica.obtenerEstadoIngreso(con, paciente.getCodigoIngreso()).equals(ConstantesIntegridadDominio.acronimoEstadoCerrado)))
		{
			if(!respPrevia.puedoSeguir)
				errores.add("descripcion",new ActionMessage(respPrevia.textoRespuesta, false));
		}
		
		return errores;
	}
	
	
	
	/**
	 * Carga la informacion del Listado de Responsables
	 * @param Connection con
	 * @param RevisionPacienteForm forma 
	 * */
	private void accionCargarResponsable(Connection con, 
										 RevisionCuentaPacienteForm forma,
										 PersonaBasica paciente,
										 UsuarioBasico usuario)	
	{		
		HashMap parametros = new HashMap();
		parametros.put("ingreso",paciente.getCodigoIngreso());
		 
				
		forma.setResponsablesMap(RevisionCuenta.consultarResponsables(con, parametros));
		
		//incia los valores para la busqueda avanzada
		forma.setEstadosSolicitudMap(RevisionCuenta.consultarEstadosSolicitudHistoriaC(con));
		forma.setTiposSolicitudMap(RevisionCuenta.consultarTiposSolicitud(con));
		
		//Carga los permisos sobre las funcionalidades		
		forma.setResponsablesMap("funcionalidadModificarTarifas",Utilidades.tieneRolFuncionalidad(con,usuario.getLoginUsuario(),80)?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
		forma.setResponsablesMap("funcionalidadRevisionCuenta",Utilidades.tieneRolFuncionalidad(con,usuario.getLoginUsuario(),211)?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);

		//carga la cuenta del paciente
		forma.setResponsablesMap("cuenta",forma.getCuenta());
		
		parametros.put("cuenta",forma.getCuenta());		
				
		if(RevisionCuenta.verificarPoolesPendientes(con, parametros)>0)
			forma.setHayPoolesPendientes(ConstantesBD.acronimoSi);			
		else
			forma.setHayPoolesPendientes(ConstantesBD.acronimoNo);				
	}
	
	
	
	
	/**
	 * Validaciones solicitadas en la funcionalidad
	 * @param usuario 
	 * @param Connection con
	 * @param RevisionCuentaPacienteForm forma
	 * @param PersonaBasic paciente
	 * @throws SQLException 
	 * */
	private void validacionesCuenta(Connection con, 
											RevisionCuentaPacienteForm forma, 
											PersonaBasica paciente, UsuarioBasico usuario) 
	{
		
		try
		{
			forma.setMensajes(new ArrayList());
			forma.setParametros("");
			
			/*Para los pacientes de Urgencias u Hospitalizacion que aun no tienen orden de salida o egreso se debe
			 * mostrar mensaje de advertencia*/
			
			if((paciente.getCodigoUltimaViaIngreso()==ConstantesBD.codigoViaIngresoHospitalizacion||paciente.getCodigoUltimaViaIngreso()==ConstantesBD.codigoViaIngresoUrgencias)&&
					!UtilidadValidacion.existeEgresoAutomatico(con, paciente.getCodigoCuenta()).puedoSeguir&&
					!UtilidadValidacion.existeEgresoCompleto(con, paciente.getCodigoCuenta()))
						forma.getMensajes().add(new ActionMessage("error.facturacion.pacienteSinSalidaoEgreso").getKey());						
									
			
			/*Para Pacientes que en la cuenta el campo Tipo Evento = Evento Accidente Transito y el Registro de Accidente de Transito 
			 * aun no se encuentre en estado Procesado, se debe mostrar mensaje de advertencia.*/			
			
			if(util.UtilidadValidacion.esAccidenteTransito(con, paciente.getCodigoIngreso()))
			{
				if(!util.UtilidadValidacion.esAccidenteTransitoEstadoDado(con, paciente.getCodigoIngreso(), ConstantesIntegridadDominio.acronimoEstadoProcesado))
				{
					forma.getMensajes().add(new ActionMessage("error.facturacion.EventoAccidenteNoProcesado").getKey());					
				}
			}
			
			
			/*Para Pacientes que en la cuenta el campo Tipo Evento = Evento Castastrofico y el Registro de Evento Catastrofico 
			 * aun no se encuentre en estado Finalizado, se debe mostrar mensaje de advertencia.*/
			
			else if(util.UtilidadValidacion.esEventoCatastrofico(con, paciente.getCodigoIngreso()))
			{
				if(!util.UtilidadValidacion.esEventoCatastroficoEstadoDado(con, paciente.getCodigoIngreso(), ConstantesIntegridadDominio.acronimoEstadoFinalizado))
				{
					forma.getMensajes().add(new ActionMessage("error.facturacion.EventoCatastroficoNoFinalizado").getKey());								
				}
			}	
			
			/*Algun Covenio asociado al ingreso del paciente se tiene definida en su parametrizacion el campo maneja Complejidad = S 
			 * verificar que en la cuenta del paciente se tenga definida la complejidad, se debe mostrar mensaje.*/			
			
			if(UtilidadValidacion.convenioManejaComplejidad(con, paciente.getCodigoConvenio()))
			{
				if(Cuenta.obtenerTipoComplejidad(con,paciente.getCodigoCuenta()+"")==ConstantesBD.codigoNuncaValido ||
						Cuenta.obtenerTipoComplejidad(con,paciente.getCodigoCuenta()+"") == 0)
				{
					forma.getMensajes().add(new ActionMessage("error.facturacion.ConvenioSinComplejidad").getKey());					
				}
			}			
			
			//Se evalua si el ingreso del paciente es un ingreso con entidad subcontratada para sacar mensaje de advetencia
			if((IngresoGeneral.esIngresoComoEntidadSubContratada(con,paciente.getCodigoIngreso())+"").equals(ConstantesBD.acronimoSi))
			{
				forma.setParametros(EntidadesSubContratadas.getDescripcionEntidadSubXIngreso(con,paciente.getCodigoIngreso()+""));
				forma.getMensajes().add("errors.paciente.ingresoPacienteEntidadSub"+ConstantesBD.separadorSplit+EntidadesSubContratadas.getDescripcionEntidadSubXIngreso(con,paciente.getCodigoIngreso()+""));
			}
	
			
			//***************VALIDACIONES DEUDOR Y DOCUMENTOS DE GARANTÍA**********************************************************************
			boolean esRequeridoDeudor = false;
			boolean deboValidarDocumentoGarantias = false;
			boolean existeDeudor = DocumentosGarantia.existeDeudorXIngreso(con, paciente.getCodigoIngreso(), usuario.getCodigoInstitucionInt());
			
			//Si no existe deudor no se realizan validaciones
			if(!existeDeudor)
				for(int i=0;i<Integer.parseInt(forma.getResponsablesMap("numRegistros")+"");i++)
				{
					if(!UtilidadTexto.getBoolean(UtilidadesFacturacion.esConvenioExcentoDeudor(con, Integer.parseInt(forma.getResponsablesMap("convenio_"+i)+""), usuario.getCodigoInstitucionInt())))
					{
						deboValidarDocumentoGarantias = true;
						esRequeridoDeudor = UtilidadesManejoPaciente.esRequeridoDeudor(
								con, 
								forma.getResponsablesMap("codigotiporegimen_"+i)+"", 
								paciente.getCodigoTipoIdentificacionPersona(), 
								usuario.getCodigoInstitucionInt()
							);
						
						//Con el hecho de que un convenio requiera deudor se cierra ciclo
						if(esRequeridoDeudor)
							i = Integer.parseInt(forma.getResponsablesMap("numRegistros")+"");
					}
				}
			
			if(esRequeridoDeudor)
				forma.getMensajes().add("errors.paciente.requeridoIngresoDe"+ConstantesBD.separadorSplit+"del deudor");
			
			if(deboValidarDocumentoGarantias&&UtilidadesManejoPaciente.esRequeridoDocumentoGarantia(con, paciente.getCodigoUltimaViaIngreso(), paciente.getCodigoTipoPaciente(), paciente.getCodigoTipoIdentificacionPersona(), usuario.getCodigoInstitucionInt())&&
				!DocumentosGarantia.existenDocumentosGarantiaXIngreso(con, paciente.getCodigoIngreso(), paciente.getCodigoPersona(), usuario.getCodigoInstitucionInt()))
				forma.getMensajes().add("errors.paciente.requeridoIngresoDe"+ConstantesBD.separadorSplit+(esRequeridoDeudor?"del documento de garantías":"del deudor y documento de garantías"));
			//*******************************************************************************************************************************
			
			//**************VALIDACIONES DE LA VERIFICACIÓN DE DERECHOS******************************************************************
			for(int i=0;i<Integer.parseInt(forma.getResponsablesMap("numRegistros")+"");i++)
			{
				if(UtilidadesManejoPaciente.esRequeridaVerificacionDerechos(con, paciente.getCodigoUltimaViaIngreso(), forma.getResponsablesMap("codigotiporegimen_"+i)+"")&&
					!UtilidadesManejoPaciente.existeVerificacionDerechosSubcuenta(con, forma.getResponsablesMap("subcuenta_"+i)+""))
					forma.getMensajes().add("errors.paciente.requeridoIngresoDe"+ConstantesBD.separadorSplit+"de la verificación de derechos para el responsable "+forma.getResponsablesMap("descripcionconvenio_"+i));
			}
			//***************************************************************************************************************************
			
			forma.setSizeMensajes(forma.getMensajes().size());
			
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}		
	}
	
	
	
	
	/**
	 * Carga los requisitos de ingreso del paciente 
	 * @param Connection con
	 * @param RevisionCuentaPacienteForm forma
	 * */
	private void accionCargarRequisitos(Connection con,
										RevisionCuentaPacienteForm forma,
										String tiporegistro)
	{
		HashMap parametros = new HashMap();
		parametros.put("tiporequisito",tiporegistro);
		parametros.put("subcuenta",forma.getResponsablesMap("subcuenta_"+forma.getIndexResponsablesMap())+"");		
		parametros.put("ingreso",forma.getResponsablesMap("ingreso_"+forma.getIndexResponsablesMap())+"");
		
		forma.setRequisitosMap(RevisionCuenta.consultarRequisitosConvenio(con, parametros));		
	}		
	
	
	
	/**
	 *	Modifica el estado de los requisitos del paciente
	 *	@param Connection con
	 *	@param RevisionCuentaPacienteForm forma
	 *	@param UsuarioBasico usuario
	 * */
	private void accionModificarRequisitos(Connection con, 
											RevisionCuentaPacienteForm forma, 
											UsuarioBasico usuario)
	{
		int numRegistro = Integer.parseInt(forma.getRequisitosMap("numRegistros")+"");
		String [] indices = (String [])forma.getRequisitosMap("INDICES_MAP");
		String estado = "ninguno";
		HashMap parametros = new HashMap();
		parametros.put("INDICES_MAP",indices);
		boolean transacction = UtilidadBD.iniciarTransaccion(con);		
		
		if(numRegistro > 0)
		{
			for(int i=0; i<numRegistro ; i++)
			{
				if(!forma.getRequisitosMap("cumplido_"+i).equals(forma.getRequisitosMap("cumplidoold_"+i)+""))
				{
					transacction = RevisionCuenta.actualizarRequisitosConvenio(con,forma.getRequisitosMap("requisitopaciente_"+i)+"",
							forma.getRequisitosMap("subcuenta_"+i)+"",forma.getRequisitosMap("cumplido_"+i)+"");
					
					if(transacction)
					{	
						//HashMap para el log de tipo Archivo
						parametros.put("requisitopaciente_0",forma.getRequisitosMap("requisitopaciente_"+i)+"");
						parametros.put("descripcion_0",forma.getRequisitosMap("descripcion_"+i)+"");
						parametros.put("subcuenta_0",forma.getRequisitosMap("subcuenta_"+i)+"");					
						parametros.put("cumplido_0",forma.getRequisitosMap("cumplidoold_"+i)+"");
						parametros.put("tiporequisito_0",forma.getRequisitosMap("tiporequisito_"+i)+"");					
						
						Utilidades.generarLogGenerico( forma.getRequisitosMap(),parametros, usuario.getNombreUsuario(), false, i, ConstantesBD.logRevisionCuentaRequisitosCodigo, indices);
						estado = "operacionTrue";
					}	
					else
					{
						UtilidadBD.abortarTransaccion(con);
						estado = "ninguno";
						return;
					}	
				}
			}
			
			//se vuelva a consultar el listado de requerimientos
			accionCargarRequisitos(con, forma,forma.getRequisitosMap("tiporequisito_0")+"");
		}	
		
		
		forma.setEstado(estado);
		UtilidadBD.finalizarTransaccion(con);		
	}
	
	
	
	/**
	 *  Ordena Un Mapa HashMap a partir del patron de ordenamiento
	 *  @param HashMap mapaOrdenar
	 *  @param String patronOrdenar
	 *  @param String ultimoPatron
	 *  @return Mapa Ordenado
	 **/
	public HashMap accionOrdenarMapa(HashMap mapaOrdenar,
									 RevisionCuentaPacienteForm forma)
	{					
		String[] indices = (String[])mapaOrdenar.get("INDICES_MAP");
		int numReg = Integer.parseInt(mapaOrdenar.get("numRegistros")+"");	
		mapaOrdenar = (Listado.ordenarMapa(indices,forma.getPatronOrdenar(),forma.getUltimoPatron(),mapaOrdenar,numReg));		
		forma.setUltimoPatron(forma.getPatronOrdenar());
		mapaOrdenar.put("numRegistros",numReg+"");
		mapaOrdenar.put("INDICES_MAP",indices);		
		return mapaOrdenar;
	}	
	
	
	
		
	/*******************************************************************************************************************
	 * Metodos Solicitudes Responsable
	 *******************************************************************************************************************/
	
	/**
	 * 	validaciones generales 
	 *  @param Connection con
	 *  @param RevisionCuentaPacienteForm forma
	 * */
	public void accionValidacionIngresoSolicitudes(Connection con, 
													RevisionCuentaPacienteForm forma, 
													PersonaBasica paciente) throws IPSException
	{
		
		ResultadoBoolean resultado;
		boolean indicador = false;
		
		//********************validaciones para el contrato
				
		//consultar el contrato de la cuenta o subcuenta a revisar
		Contrato contrato= new Contrato();				
		contrato.cargar(con,forma.getResponsablesMap("contrato_"+forma.getIndexResponsablesMap())+"");
		
		Cuenta cuenta = new Cuenta();
		
		try
		{			
			//inicializa el valor para identificar si existe validaciones que mostrar
			forma.setListadoSolicitudesMap("hayValidaciones",ConstantesBD.acronimoNo);
			
			//se toma al descripcion del Responsable
			forma.setListadoSolicitudesMap("descripcionconvenio",forma.getResponsablesMap("descripcionconvenio_"+forma.getIndexResponsablesMap()));
			
			
			//evalua que cuenta tomar		
			cuenta.cargarFechaCreacionCuenta(con,paciente.getCodigoCuenta());
			
			//valida que la fecha de la cuenta se encuentre entre el rango de fechas del contrato			
			resultado = UtilidadFecha.compararFechas(cuenta.getDiaApertura()+"/"+cuenta.getMesApertura()+"/"+cuenta.getAnioApertura(),"00:00",UtilidadFecha.conversionFormatoFechaAAp(contrato.getFechaInicial()),"00:00");
			
			if(resultado.isTrue())
			{
				resultado = UtilidadFecha.compararFechas(cuenta.getDiaApertura()+"/"+cuenta.getMesApertura()+"/"+cuenta.getAnioApertura(),"00:00",UtilidadFecha.conversionFormatoFechaAAp(contrato.getFechaFinal()),"00:00");
				if(!resultado.isTrue())			
					indicador = true;				
			}
			
			//no paso las validaciones de vigencia del contrato
			if(!indicador)
			{
				forma.setListadoSolicitudesMap("esConstratoVencido",ConstantesBD.acronimoSi);
				forma.setListadoSolicitudesMap("contratoFechaInicial",UtilidadFecha.conversionFormatoFechaAAp(contrato.getFechaInicial()));
				forma.setListadoSolicitudesMap("contratoFechaFinal",UtilidadFecha.conversionFormatoFechaAAp(contrato.getFechaFinal()));
				forma.setListadoSolicitudesMap("hayValidaciones",ConstantesBD.acronimoSi);
			}			
			
			
			//valido los topes del valor del contrato
			if(contrato.getValorContrato() != 0)
			{
				if(contrato.getValorContrato()<contrato.getValorAcumulado())
				{
					forma.setListadoSolicitudesMap("esValorContratoCompleto",ConstantesBD.acronimoSi);
					forma.setListadoSolicitudesMap("totalContrato",UtilidadTexto.formatearValores(contrato.getValorContrato()));
					forma.setListadoSolicitudesMap("totalContratoAcumulado",UtilidadTexto.formatearValores(contrato.getValorAcumulado()));
					forma.setListadoSolicitudesMap("hayValidaciones",ConstantesBD.acronimoSi);
				}
			}			
		}
		
		catch(SQLException e)
		{
			e.printStackTrace();
		}		
	}		
	
	
	/**
	 * carga las solicitudes cargadas a los responsables o subcuentas
	 * @param Connection con 
	 * @param RevisioCuentaPacienteForm forma
	 * @param UsuarioBasico usuario
	 * */
	public void accionCargarListadoSolicitudesReponsable(Connection con, 
														 RevisionCuentaPacienteForm forma)
	{
		forma.resetSolicitudes();
		HashMap parametros = new HashMap();
		
		//variable que indica cual es el primer campo despues del WHERE para que
		//lo que siguen si existen mas parametros de busqueda se han añadidos con la 
		//palabra AND, esto se hace en el SQLbasico 
		String where="";
		
		where="**";	//FIXME Se inicializa linea para que no agregue en la consulta WHERE. ya que el where ya se encuentra en la consulta y por 
					//eso no funcionaba la busqueda, Y si trae parametros de busqueda debe evaluarlos todos. Por lo tanto en el SqlBase no 
					//evaluara los if con (WHERE) y solo agregara AND
		
		//parametros para la busqueda del Listado de Solicitudes
		if(!(forma.getBusqueda("numerosolicitudC")+"").equals(""))
		{
			parametros.put("numerosolicitud",forma.getBusqueda("numerosolicitudC"));
			where="numerosolicitud";
		}
		
		if(!(forma.getBusqueda("tiposolicitudC")+"").equals(""))
		{
			parametros.put("tiposolicitud",forma.getBusqueda("tiposolicitudC"));
			if(where.equals(""))
			 where="tiposolicitud";		
		}	
		
		if(!(forma.getBusqueda("fechasolicitudCini")+"").equals(""))
		{
			//valida la fecha
			if(UtilidadFecha.validarFecha(forma.getBusqueda("fechasolicitudCini")+""))
			{
				parametros.put("fechasolicitudinicial",forma.getBusqueda("fechasolicitudCini"));
				if(where.equals(""))
					where="fechasolicitudinicial";
			}			
		}
						
		if(!(forma.getBusqueda("estadohcC")+"").equals(""))
		{
			parametros.put("estadohc",forma.getBusqueda("estadohcC"));
			if(where.equals(""))
				where="estadohc";
		}			
		
		
		if(!(forma.getBusqueda("fechasolicitudCfin")+"").equals(""))
		{
			//valida la fecha
			if(UtilidadFecha.validarFecha(forma.getBusqueda("fechasolicitudCfin")+""))
			{
				parametros.put("fechasolicitudfinal",forma.getBusqueda("fechasolicitudCfin"));
				if(where.equals(""))
					where="fechasolicitudfinal";
			}			
		}
		
		parametros.put("where",where);
		parametros.put("ingreso",forma.getResponsablesMap("ingreso_"+forma.getIndexResponsablesMap()));
		parametros.put("convenio",forma.getResponsablesMap("convenio_"+forma.getIndexResponsablesMap()));
		
		parametros.put("subcuenta",forma.getResponsablesMap("subcuenta_"+forma.getIndexResponsablesMap()));
		parametros.put("mostrarIndicativo",ConstantesBD.acronimoSi);
		parametros.put("espaquetizada",ConstantesBD.acronimoNo);						
		
		logger.info("valor del mapa >> "+parametros+" >> "+forma.getIndexResponsablesMap());
		
		forma.setListadoSolicitudesMap(RevisionCuenta.consultarListadoSolicitudes(con, parametros));
		forma.setIndicadorTipoListadoDetalle("noPaquete");		
		accionPrepararBusqueda(forma);	
	}
	
	
	/**
	 * Prepara los datos para la consulta
	 * @param RevisionCuentaPacienteForm forma
	 * */
	public void accionPrepararBusqueda(RevisionCuentaPacienteForm forma)	
	{
		forma.setBusqueda("numerosolicitudC","");
		forma.setBusqueda("tiposolicitudC","");
		forma.setBusqueda("fechaC","");
		forma.setBusqueda("estadoC","");		
		forma.setBusqueda("fechasolicitudCini","");
		forma.setBusqueda("estadohcC","");
		forma.setBusqueda("esPortatil", "");
		forma.setBusqueda("fechasolicitudCfin","");
		forma.setBusqueda("horasolicitudCini","");
		forma.setBusqueda("horasolicitudCfin","");
		forma.setBusqueda("fecharespuestaini","");
		forma.setBusqueda("fecharespuestafin","");
		forma.setBusqueda("horarespuestaini","");
		forma.setBusqueda("horarespuestafin","");
		
		logger.info("MAPA PARA LA BUSQUEDA ->"+forma.getBusqueda());
	}
	
	
	
	
	/*******************************************************************************************************************
	 * Metodos Detalle Solicitudes
	 *******************************************************************************************************************/
	
	
	/**
	 * Carga la interfaz de detalle de solicitudes, determina que flujo seguir deacuerdo al 
	 * tipo de solicitud
	 * @param Connection con 
	 * @param RevisionCuentaPacienteForm forma
	 * */
	private ActionForward cargarInterfazSolicitudDetalle(Connection con,
														RevisionCuentaPacienteForm forma,
														ActionMapping mapping)
	{
		
		//Captura el tipo de solicitud
		String tiposolicitud = "";
		String numeroSolicutud="";
		String esPaquetizado = "";		
		String consecutivoordenesmedicas = "";
		forma.setIndexForwardDetalleSolicitudes("");		
	
		//define de que tipo de listado de solicitudes se 
		//tomara la informacion para listar la informacion del DETALLE
		//de la solicitud
		if(forma.getIndicadorTipoListadoDetalle().equals("paquete"))
		{
			numeroSolicutud = forma.getListadoDetallePaqueteMap("numerosolicitud_"+forma.getIndexListadoDetallePaquete())+"";
			tiposolicitud   = forma.getListadoDetallePaqueteMap("tiposolicitud_"+forma.getIndexListadoDetallePaquete())+"";
			consecutivoordenesmedicas = forma.getListadoDetallePaqueteMap("consecutivoordenesmedicas_"+forma.getIndexListadoDetallePaquete())+"";
			esPaquetizado = ConstantesBD.acronimoSi;
		}	
		else if(forma.getIndicadorTipoListadoDetalle().equals("noPaquete"))
		{
			numeroSolicutud = forma.getListadoSolicitudesMap("numerosolicitud_"+forma.getIndexSolicitudesMap())+"";
			tiposolicitud   = forma.getListadoSolicitudesMap("tiposolicitud_"+forma.getIndexSolicitudesMap())+"";
			consecutivoordenesmedicas = forma.getListadoSolicitudesMap("consecutivoordenesmedicas_"+forma.getIndexSolicitudesMap())+"";																		   
			esPaquetizado = ConstantesBD.acronimoNo;
		}
		
		
		//************************************************************************************************************************
		//Solicitudes de Farmacia, Cargos Directos de Articulos
		if(tiposolicitud.equals(ConstantesBD.codigoTipoSolicitudMedicamentos+"") ||
				tiposolicitud.equals(ConstantesBD.codigoTipoSolicitudCargosDirectosArticulos+""))
		{
			//consulta la informacion de los detalles de la solicitud 
			accionConsultarSolicitudDetalle(con,forma,esPaquetizado,ConstantesBD.acronimoSi,numeroSolicutud);
			
			//carga el numero de la solicitud padre del detalle o cargo
			forma.setDetalleSolicitudesMap("numerosolicitud",numeroSolicutud);
			
			//carga el numero de consecutivo de la solicitud
			forma.setDetalleSolicitudesMap("consecutivoordenesmedicas",consecutivoordenesmedicas);
			
			//carga el tipo de solicitud en la cual se encuenta el detalle
			forma.setDetalleSolicitudesMap("tiposolicitudpadre",tiposolicitud);
			
			//carga las validaciones de los detalles de solicitud
			aplicarValidacionesSolicitudesDetalle(forma);			
			
			//carga el indicativo del TIPO de detalle del servicio
			forma.setIndexForwardDetalleSolicitudes("detSolicitudFarmacia");													
		}
		
		
		//************************************************************************************************************************
		//solicitudes de cirugia
		else if(tiposolicitud.equals(ConstantesBD.codigoTipoSolicitudCirugia+""))
		{
			//consulta la informacion de los detalles de la solicitud Tipo Cirugia		
			accionConsultarDetalleCirugia(con,forma,esPaquetizado,numeroSolicutud);
			
			//carga el numero de la solicitud padre del detalle o cargo
			forma.setDetalleSolicitudesMap("numerosolicitud",numeroSolicutud);
			
			//carga el numero deL cosecutivo de orden medica
			forma.setDetalleSolicitudesMap("consecutivoordenesmedicas",consecutivoordenesmedicas);
						
			//carga el tipo de solicitud en la cual se encuenta el detalle
			forma.setDetalleSolicitudesMap("tiposolicitudpadre",tiposolicitud);
			
			//carga las validaciones de los detalles de solicitud
			aplicarValidacionesSolicitudesDetalle(forma);			
			
			//carga el indicativo del TIPO de detalle del servicio
			forma.setIndexForwardDetalleSolicitudes("detSolicitudCirugia");
		}		
		
		
		//************************************************************************************************************************
		//solicitud de tipo Paquete
		else if(tiposolicitud.equals(ConstantesBD.codigoTipoSolicitudPaquetes+""))
		{		
			//carga las solicitudes del paquete
			accionCargarListadoDetallePaquete(con,forma);	
			
			//carga el indicativo del TIPO de detalle del servicio
			forma.setIndexForwardDetalleSolicitudes("listarSolicitudesDetallePaquete");
			
			//carga el numero de solicitud del paquete
			forma.setListadoDetallePaqueteMap("numerosolicitud",numeroSolicutud);
			
			//carga el numero de consecutivo de la solicitud
			forma.setListadoDetallePaqueteMap("consecutivoordenesmedicas",consecutivoordenesmedicas);
			
			//toma la informacion del paquete listado en las solicitudes del responsable
			forma.setListadoDetallePaqueteMap("informacionPaquete",forma.getListadoSolicitudesMap("descripcionservarticulo_"+forma.getIndexSolicitudesMap())+"");
			
		}
		
		
		//************************************************************************************************************************
		//Solicitudes de tipo servicio interconsulta, procedimiento o consulta
		else if(!tiposolicitud.equals(ConstantesBD.codigoTipoSolicitudMedicamentos+"") && 
					!tiposolicitud.equals(ConstantesBD.codigoTipoSolicitudCargosDirectosArticulos+"") &&
						!(tiposolicitud+"").equals(ConstantesBD.codigoTipoSolicitudCirugia+"") &&
							!tiposolicitud.equals(ConstantesBD.codigoTipoSolicitudPaquetes+""))
							{														
								//consulta la informacion de los detalles de la solicitud 
								accionConsultarSolicitudDetalle(con,forma,esPaquetizado,ConstantesBD.acronimoSi,numeroSolicutud);
								
								//carga el numero de la solicitud padre del detalle o cargo
								forma.setDetalleSolicitudesMap("numerosolicitud",numeroSolicutud);
								
								//carga el tipo de solicitud en la cual se encuenta el detalle
								forma.setDetalleSolicitudesMap("tiposolicitudpadre",tiposolicitud);
								
								//carga el numero de consecutivo de la solicitud
								forma.setDetalleSolicitudesMap("consecutivoordenesmedicas",consecutivoordenesmedicas);
								
								//carga las validaciones de los detalles de solicitud
								aplicarValidacionesSolicitudesDetalle(forma);								
								
								//carga el indicativo del TIPO de detalle del servicio
								forma.setIndexForwardDetalleSolicitudes("detSolicitudInterConsulta");																								
							}
		
		
		
		
		UtilidadBD.closeConnection(con);				
		return mapping.findForward(forma.getIndexForwardDetalleSolicitudes());				
	}
	
	
	
	
	
	/**
	 * Consulta la informacion del detalle de la solicitud
	 * @param Connection con 
	 * @param RevisionCuentaPacienteForm forma
	 * */
	private void accionConsultarSolicitudDetalle(Connection con, 
												RevisionCuentaPacienteForm forma,
												String esPaquetizado,
												String hayIndicativo,
												String numeroSolicitud)
	{
		HashMap parametros = new HashMap();
		
		//parametros de la busqueda
		parametros.put("numerosolicitud",numeroSolicitud);
		parametros.put("subcuenta",forma.getResponsablesMap("subcuenta_"+forma.getIndexResponsablesMap()));		
		parametros.put("paquetizado",esPaquetizado);
		parametros.put("hayIndicativo",hayIndicativo);
	
		
		forma.setDetalleSolicitudesMap(RevisionCuenta.consultarDetalleSolicitud(con, parametros));		
	}
	
	
	
	
	/**
	 * Consulta la informacion del detalle de la Cirugia
	 * @param Connection con 
	 * @param RevisionCuentaPacienteForm forma
	 * */
	private void accionConsultarDetalleCirugia(Connection con, 
											   RevisionCuentaPacienteForm forma,
											   String esPaquetizado,											   
											   String numeroSolicitud)
	{
		HashMap parametros = new HashMap();
		
		//parametros de la busqueda
		parametros.put("numerosolicitud",numeroSolicitud);
		parametros.put("subcuenta",forma.getResponsablesMap("subcuenta_"+forma.getIndexResponsablesMap()));		
		parametros.put("paquetizado",esPaquetizado);	
		parametros.put("hayIndicativo",ConstantesBD.acronimoSi);
		
		forma.setDetalleSolicitudesMap(RevisionCuenta.consultarDetalleCirugia(con, parametros));		
	}
	
	
	
	
	
	/**
	 * carga las solicitudes cargadas al paquete
	 * @param Connection con 
	 * @param RevisioCuentaPacienteForm forma
	 * @param UsuarioBasico usuario
	 * */
	public void accionCargarListadoDetallePaquete(Connection con, 
												  RevisionCuentaPacienteForm forma)
	{
		forma.resetDetallePaquete();
		HashMap parametros = new HashMap();	
		
		//variable que indica cual es el primer campo despues del WHERE para que
		//los que siguen si existen mas parametros de busqueda se han añadidos con la 
		//palabra AND, esto se hace en el SQLbasico
		//parametros para la busqueda del Listado de Solicitudes
		String where="solsubcuentapadre";				
		parametros.put("where",where);
		
		parametros.put("ingreso",forma.getResponsablesMap("ingreso_"+forma.getIndexResponsablesMap()));
		parametros.put("convenio",forma.getResponsablesMap("convenio_"+forma.getIndexResponsablesMap()));
		parametros.put("solsubcuentapadre",forma.getListadoSolicitudesMap("codigo_"+forma.getIndexSolicitudesMap()));
		
		
		//valores obligatorios de la busqueda		
		parametros.put("subcuenta",forma.getResponsablesMap("subcuenta_"+forma.getIndexResponsablesMap()));
		parametros.put("mostrarIndicativo",ConstantesBD.acronimoSi);
		parametros.put("espaquetizada",ConstantesBD.acronimoSi);		
				
		forma.setListadoDetallePaqueteMap(RevisionCuenta.consultarListadoSolicitudes(con, parametros));
		forma.setIndicadorTipoListadoDetalle("paquete");
	}
	
	
	
	
	/**
	 * Guarda la informacion modificada del detalle de la solicitud
	 * @param Connection con
	 * @param RevisionCuentaPacienteForm forma
	 * @param ActionErrors errores
	 * */
	private ActionErrors accionGuardarDetalleSolicitud(Connection con,
													   RevisionCuentaPacienteForm forma,
													   ActionErrors errores,
													   UsuarioBasico usuario)
	{
		int numRegistros = Integer.parseInt(forma.getDetalleSolicitudesMap("numRegistros")+"");
		HashMap parametros = new HashMap();
		Double valorTotal; 
		String estado = "";
		
		
		boolean transacction = UtilidadBD.iniciarTransaccion(con);	
		 int estAnterior=-1;
		 int estActual=-1;	
		 int codDetalleCargo=0;
		//recorre el HashMap de Detalle de Solicitudes
		for(int i = 0; i < numRegistros ; i++)
		{
		   
			//captura los valores por defecto para la actualizacion, sin evaluar si se han modificado
			parametros.put("valorunitariocargado",forma.getDetalleSolicitudesMap("valorunitariocargadoold_"+i)+"");
			parametros.put("estado",forma.getDetalleSolicitudesMap("estadoold_"+i)+"");
		
			parametros.put("valortotalcargado",forma.getDetalleSolicitudesMap("valortotalcargado_"+i)+"");
			//parametros.put("nroautorizacion",forma.getDetalleSolicitudesMap("nroautorizacionold_"+i)+"");
			parametros.put("codigodetallecargo",forma.getDetalleSolicitudesMap("codigodetallecargo_"+i)+"");
											
			//se evalua las diferencias entre el numero de autorizacion actual y el anterior
			/*if(!forma.getDetalleSolicitudesMap("nroautorizacion_"+i)+"".equals((forma.getDetalleSolicitudesMap("nroautorizacionold_"+i)+"")))
			{
				parametros.put("nroautorizacion",forma.getDetalleSolicitudesMap("nroautorizacion_"+i)+"");					
				estado = "modificar";
			}						
			*/				
			//valida si es posible modificar el estado del cargo 
			if(forma.getDetalleSolicitudesMap("validacionEstado_"+i).equals(ConstantesBD.acronimoSi))			 
			{							
				if(!(forma.getDetalleSolicitudesMap("estado_"+i)+"").equals((forma.getDetalleSolicitudesMap("estadoold_"+i)+"")))
				{
					parametros.put("estado",forma.getDetalleSolicitudesMap("estado_"+i)+"");
					estAnterior=Integer.parseInt(forma.getDetalleSolicitudesMap("estadoold_"+i).toString());
					estActual=Integer.parseInt(forma.getDetalleSolicitudesMap("estado_"+i).toString());
					codDetalleCargo=Integer.parseInt(forma.getDetalleSolicitudesMap("codigodetallecargo_"+i).toString());
					estado = "modificar";
				}																
			}						
			
			
			//valida si es posible el cambio del valor unitario							
			if(forma.getDetalleSolicitudesMap("validacionValorUnitario_"+i).equals(ConstantesBD.acronimoSi) && 
					!(forma.getDetalleSolicitudesMap("valorunitariocargado_"+i)+"").equals(""))
			{				
				
				try
				{
					Double.parseDouble(forma.getDetalleSolicitudesMap("valorunitariocargado_"+i)+"");
					if(Double.parseDouble(forma.getDetalleSolicitudesMap("valorunitariocargado_"+i)+"") <= 0)
						errores.add("descripcion",new ActionMessage("errors.MayorQue","Valor Unitario","cero"));							
				}
				catch(Exception e)
				{
						errores.add("descripcion",new ActionMessage("errors.invalid","Valor Flotante, Valor Unitario  "));							
				}		
				
				if(!errores.isEmpty())
				{
					UtilidadBD.abortarTransaccion(con);
					return errores;
				}
				else
				{
					try
					{
						if(!(forma.getDetalleSolicitudesMap("valorunitariocargado_"+i)+"").equals((forma.getDetalleSolicitudesMap("valorunitariocargadoold_"+i)+"")))
						{
							valorTotal = Double.parseDouble(forma.getDetalleSolicitudesMap("valorunitariocargado_"+i)+"") * Integer.parseInt(forma.getDetalleSolicitudesMap("cantidadcargada_"+i)+"");
						
							parametros.put("valorunitariocargado",forma.getDetalleSolicitudesMap("valorunitariocargado_"+i)+"");
							parametros.put("valortotalcargado", valorTotal);					
							estado = "modificar";
						}	
					}
					catch(Exception e)
					{
							errores.add("descripcion",new ActionMessage("errors.invalid","Valor Flotante, Error al Calcular el Valor Total  "));							
					}
				}
			}
			
			if(estado.equals("modificar"))
			{				
				transacction = RevisionCuenta.actualizarDetalleSolicitud(con, parametros);
				if (estActual!=-1)
				{
					if (estActual!=estAnterior) // probablemente sobre esta validacion, pero se deja de todas maneras como un seguro
					{
						logger.info("SI EXISTE UN CAMBIO PARA INSERTAR EN EL LOG");
						logger.info("------------------------------");
						logger.info("CODIGO DETALLE CARGO ->"+codDetalleCargo);
						logger.info("ESTADO ANTERIOR ->"+estAnterior);
						logger.info("ESTADO ACTUAL ->"+estActual);
						logger.info("FECHA ->"+UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
						logger.info("HORA ->"+UtilidadFecha.getHoraActual());
						logger.info("USUARIO ->"+usuario.getLoginUsuario());
						logger.info("------------------------------");
						
						RevisionCuenta.insertarLogEstadoCargo(con, String.valueOf(codDetalleCargo), 
																	String.valueOf(estAnterior),
																	String.valueOf(estActual),
																	usuario.getLoginUsuario());
						
					}
					
				}
				generarLogAutorizacion(forma, usuario,i);					
				estado = "operacionTrue";
			}				
		}	
				
		
		if(transacction)
		{
			UtilidadBD.finalizarTransaccion(con);
			
			if(estado.equals("operacionTrue"))
			 logger.info("----->MODIFICO DETALLE SOLICITUD");			
		}
		else
			UtilidadBD.abortarTransaccion(con);			
		
		
		forma.setEstado(estado);
		return errores;
	}
	
	
	
	
	/**
	 * Aplica las validaciones a los detalles de las solicitudes para mostrar
	 * @param RevisionCuentaForm forma
	 * */
	private void aplicarValidacionesSolicitudesDetalle(RevisionCuentaPacienteForm forma)
	{		
		int numRegistros = Integer.parseInt(forma.getDetalleSolicitudesMap("numRegistros")+"");
		
		for(int i=0; i<numRegistros; i++)
		{
			//************************************************Validaciones para el numero de autorizacion
			//*******************************************************************************************
			forma.setDetalleSolicitudesMap("validacionAutorizacion_"+i,ConstantesBD.acronimoNo);
			
			if(forma.getIndicadorTipoListadoDetalle().equals("paquete"))
				forma.setDetalleSolicitudesMap("validacionAutorizacion_"+i,ConstantesBD.acronimoNo);
			else if(forma.getIndicadorTipoListadoDetalle().equals("noPaquete"))
				forma.setDetalleSolicitudesMap("validacionAutorizacion_"+i,ConstantesBD.acronimoSi);		
			
			
			//*********************************************** Validaciones del estado del cargo
			//********************************************************************************
			forma.setDetalleSolicitudesMap("validacionEstado_"+i,ConstantesBD.acronimoNo);
			
			if(forma.getIndicadorTipoListadoDetalle().equals("paquete"))				
				forma.setDetalleSolicitudesMap("validacionEstado_"+i,ConstantesBD.acronimoNo);
			else if(forma.getIndicadorTipoListadoDetalle().equals("noPaquete"))
			{
				if(forma.getResponsablesMap("funcionalidadModificarTarifas").equals(ConstantesBD.acronimoSi))
				{
					if(forma.getResponsablesMap("funcionalidadRevisionCuenta").equals(ConstantesBD.acronimoSi))
					{
						if((forma.getDetalleSolicitudesMap("estadoold_"+i)+"").equals(ConstantesBD.codigoEstadoFCargada+"") && 
								((forma.getDetalleSolicitudesMap("facturado_"+i)+"").equals(ConstantesBD.acronimoNo)))
						{
							forma.setDetalleSolicitudesMap("validacionEstado_"+i,ConstantesBD.acronimoSi);						
						}
					}
				}			
			}	
			
			//**************************************************** Validaciones Valor unitario
			//********************************************************************************
			forma.setDetalleSolicitudesMap("validacionValorUnitario_"+i,ConstantesBD.acronimoNo);
			
			
			if(forma.getIndicadorTipoListadoDetalle().equals("paquete"))				
				forma.setDetalleSolicitudesMap("validacionValorUnitario_"+i,ConstantesBD.acronimoNo);			
			//no aplica validacion para tipos de solicitud cirugia
			else if(forma.getIndicadorTipoListadoDetalle().equals("noPaquete") && !(forma.getDetalleSolicitudesMap("tiposolicitudpadre")+"").equals(ConstantesBD.codigoTipoSolicitudCirugia+""))
			{			
				if(forma.getResponsablesMap("funcionalidadModificarTarifas").equals(ConstantesBD.acronimoSi))
				{
					if(forma.getResponsablesMap("funcionalidadRevisionCuenta").equals(ConstantesBD.acronimoSi))
					{
						if((forma.getDetalleSolicitudesMap("estadoold_"+i)+"").equals(ConstantesBD.codigoEstadoFCargada+"") && 
								((forma.getDetalleSolicitudesMap("facturado_"+i)+"").equals(ConstantesBD.acronimoNo)))
						{
							//verifica si posee distribucion
							if(Integer.parseInt(forma.getDetalleSolicitudesMap("cantidaddistribucion_"+i)+"") > 1)
							{					
								if(!(forma.getDetalleSolicitudesMap("tipodistribucion_"+i)+"").equals(ConstantesIntegridadDominio.acronimoTipodistribucionMonto) && 
										!(forma.getDetalleSolicitudesMap("tipodistribucion_"+i)+"").equals(ConstantesIntegridadDominio.acronimoTipoDistribucionPorcentual))						
									forma.setDetalleSolicitudesMap("validacionValorUnitario_"+i,ConstantesBD.acronimoSi);						
								else
									forma.setDetalleSolicitudesMap("validacionValorUnitario_"+i,ConstantesBD.acronimoNo);
							}
							else
								forma.setDetalleSolicitudesMap("validacionValorUnitario_"+i,ConstantesBD.acronimoSi);
						}
					}
				}		
			}		
		}	
	}
	
	
	
	
	
	/*******************************************************************************************************************
	 * Metodos Pooles Pendientes
	 ******************************************************************************************************************/
	
	
	/**
	 * incia el valor de la cuenta para los listados
	 * @param forma
	 * @param PersonaBasica paciente
	 * */
	private void iniciarCuenta(RevisionCuentaPacienteForm forma,PersonaBasica paciente)
	{
		String cuenta;
		
		//evalua que cuentas tomar para el listar las solicitudes		
		if(paciente.getExisteAsocio() && paciente.getCodigoCuentaAsocio() != 0)
			cuenta = "'"+paciente.getCodigoCuentaAsocio()+"','"+paciente.getCodigoCuenta()+"'";
		else
			cuenta = paciente.getCodigoCuenta()+"";
		
		forma.setCuenta(cuenta);
	}
	
	
	/**
	 * Inicia el listado de Pooles pendientes
	 * @param Conenction con
	 * @param forma
	 * @param UsuarioBasico	 
	 * */
	private void cargarListadoPooles(Connection con,
									 RevisionCuentaPacienteForm forma) 
									 
	{
		HashMap parametros = new HashMap();				
		forma.resetSolicitudes();		
				
		//parametros de la busqueda
		parametros.put("cuenta",forma.getCuenta());
		parametros.put("espaquetizada",ConstantesBD.acronimoNo);
		parametros.put("solsubcuentapadre","0");
		
		//Si se ejecuta desde la revisión de cuenta la consutla cambiará mostrando las solicitudes sin importar los estados de historia clinica y verificando si la solicitud se encuentra ligada a una cita 
		parametros.put("filtrarRevisionCuenta", "");
		
		forma.setListadoSolicitudesMap(RevisionCuenta.consultarListadoPooles(con, parametros));		
		forma.setListadoSolicitudesMap("cuenta",forma.getCuenta());
		forma.setIndicadorTipoListadoDetalle("noPaquete");
	}
	
	
	
	
	/**
	 * Inicia el listado de Pooles pendientes Para el paquete
	 * @param Conenction con
	 * @param forma
	 * @param UsuarioBasico	 
	 * */
	private void cargarListadoPoolesPaquetes(Connection con,
									 		 RevisionCuentaPacienteForm forma,
									 		 String cuenta,
									 		 String solSubcuentaPadre) 
									 
	{
		HashMap parametros = new HashMap();
		forma.resetDetallePaquete();
				
		//parametros de la busqueda
		parametros.put("cuenta",cuenta);
		parametros.put("espaquetizada",ConstantesBD.acronimoSi);
		parametros.put("solsubcuentapadre",solSubcuentaPadre);
		forma.setListadoDetallePaqueteMap(RevisionCuenta.consultarListadoPooles(con, parametros));		
		forma.setIndicadorTipoListadoDetalle("paquete");
	}
	
	
	
	/**
	 * Carga los pooles disponibles para el medico
	 * @param Connection con
	 * @param RevisionCuentaPacienteForm forma
	 * */
	private void cargarListadoPoolesMedico(Connection con, 
										   RevisionCuentaPacienteForm forma,
										   HashMap datosMap,
										   String index)
	{
		HashMap parametros = new HashMap();	
		forma.setListadoPoolesMedico("numRegistros","0");
		
		parametros.put("medico",datosMap.get("codigomedicoresponde_"+index));
		
		//Agrego la fecha de la solicitud para agregar un filtro adicional a los pooles TAREA 146033
		parametros.put("fechageneracionsolicitud",datosMap.get("fechasolicitud_"+index));
		
		forma.setListadoPoolesMedico(RevisionCuenta.consultarListadoPoolesMedico(con, parametros));
		
		forma.setListadoPoolesMedico("pool",datosMap.get("pool_"+index));
		forma.setListadoPoolesMedico("numerosolicitud",datosMap.get("numerosolicitud_"+index));
		forma.setListadoPoolesMedico("consecutivoordenesmedicas",datosMap.get("consecutivoordenesmedicas_"+index));
		forma.setListadoPoolesMedico("datosmedicoresponde",datosMap.get("datosmedicoresponde_"+index));
		forma.setListadoPoolesMedico("fecharespuestasolicitud",datosMap.get("fecharespuesta_"+index));
	}
	
	
	

	/**
	 * Consulta la informacion del detalle de la Cirugia POOL
	 * @param Connection con 
	 * @param RevisionCuentaPacienteForm forma
	 * */
	private void accionConsultarDetalleCirugiaPool(Connection con, 
											   	   RevisionCuentaPacienteForm forma,
											   	   String numeroSolicitud,
											   	   String esPaquetizado)
	{
		HashMap parametros = new HashMap();
		
		//parametros de la busqueda
		parametros.put("numerosolicitud",numeroSolicitud);			
		parametros.put("paquetizado",esPaquetizado);	
		
		forma.setDetalleSolicitudesMap(RevisionCuenta.consultarDetalleCirugiaPool(con, parametros));		
	}

	
	
	
	/**
	 * Carga el flujo para los detalle de las solicitudes tipo paquete y cirugia
	 * @param Connection con
	 * @param RevisionCuentaForm forma
	 * @param ActionMapping mapping
	 * */
	private ActionForward cargarInterfazSolicitudDetallePool(Connection con,
															 RevisionCuentaPacienteForm forma, 
															 ActionMapping mapping)
	{
		//Captura el tipo de solicitud
		String tiposolicitud = "";
		String numeroSolicutud="";
		String esPaquetizado = "";
		String consecutivoordenesmedicas= "";
		forma.setIndexForwardDetalleSolicitudes("");
		
		
		//define de que tipo de listado de solicitudes se 
		//tomara la informacion para listar la informacion del DETALLE
		//de la solicitud
		if(forma.getIndicadorTipoListadoDetalle().equals("paquete"))
		{
			numeroSolicutud = forma.getListadoDetallePaqueteMap("numerosolicitud_"+forma.getIndexListadoDetallePaquete())+"";
			tiposolicitud   = forma.getListadoDetallePaqueteMap("tiposolicitud_"+forma.getIndexListadoDetallePaquete())+"";
			consecutivoordenesmedicas = forma.getListadoDetallePaqueteMap("consecutivoordenesmedicas_"+forma.getIndexListadoDetallePaquete())+"";
			esPaquetizado = ConstantesBD.acronimoSi;
		}	
		else if(forma.getIndicadorTipoListadoDetalle().equals("noPaquete"))
		{
			numeroSolicutud = forma.getListadoSolicitudesMap("numerosolicitud_"+forma.getIndexSolicitudesMap())+"";
			tiposolicitud   = forma.getListadoSolicitudesMap("tiposolicitud_"+forma.getIndexSolicitudesMap())+"";
			consecutivoordenesmedicas = forma.getListadoSolicitudesMap("consecutivoordenesmedicas_"+forma.getIndexSolicitudesMap())+"";	
			esPaquetizado = ConstantesBD.acronimoNo;
		}	
			
		//************************************************************************************************************************		
		//solicitudes de cirugia
		if(tiposolicitud.equals(ConstantesBD.codigoTipoSolicitudCirugia+""))
		{
			//consulta la informacion de los detalles de la solicitud Tipo Cirugia
			accionConsultarDetalleCirugiaPool(con,forma,numeroSolicutud,esPaquetizado);
			
			//carga el numero de la solicitud padre del detalle o cargo
			forma.setDetalleSolicitudesMap("numerosolicitud",numeroSolicutud);
						
			//carga el tipo de solicitud en la cual se encuenta el detalle
			forma.setDetalleSolicitudesMap("tiposolicitudpadre",tiposolicitud);
			
			//carga el numero de consecutivo de la solicitud
			forma.setDetalleSolicitudesMap("consecutivoordenesmedicas",consecutivoordenesmedicas);
			
			//carga el indicativo del TIPO de detalle del servicio
			forma.setIndexForwardDetalleSolicitudes("detSolicitudCirugiaPool");
		}		
		
		
		//************************************************************************************************************************
		//solicitud de tipo Paquete
		else if(tiposolicitud.equals(ConstantesBD.codigoTipoSolicitudPaquetes+""))
		{		
			//carga las solicitudes del paquete
			cargarListadoPoolesPaquetes(con, forma,forma.getListadoSolicitudesMap("cuenta")+"",forma.getListadoSolicitudesMap("codigosolicitud_"+forma.getIndexSolicitudesMap())+"");			
			
			//carga el numero de solicitud del paquete
			forma.setListadoDetallePaqueteMap("numerosolicitud",numeroSolicutud);
			
			//carga el numero de consecutivo de la solicitud
			forma.setListadoDetallePaqueteMap("consecutivoordenesmedicas",consecutivoordenesmedicas);
			
			//toma el valor de la cuenta dentro del HashMap
			forma.setListadoDetallePaqueteMap("cuenta",forma.getListadoSolicitudesMap("cuenta")+"");
			
			//toma el codigo de la solicitud tipo paquete dentro del HashMap
			forma.setListadoDetallePaqueteMap("codigosolicitud",forma.getListadoSolicitudesMap("codigosolicitud_"+forma.getIndexSolicitudesMap())+"");
			
			//toma la informacion del paquete listado en las solicitudes del responsable
			forma.setListadoDetallePaqueteMap("descripcionservicio",forma.getListadoSolicitudesMap("descripcionservicio_"+forma.getIndexSolicitudesMap())+"");
			
			if((forma.getListadoSolicitudesMap("fechasolicitud_"+forma.getIndexSolicitudesMap())+"").length() > 12)
				forma.setListadoDetallePaqueteMap("fechasolicitud",forma.getListadoSolicitudesMap("fechasolicitud_"+forma.getIndexSolicitudesMap())+"".substring(0, 12));
			else
				forma.setListadoDetallePaqueteMap("fechasolicitud",forma.getListadoSolicitudesMap("fechasolicitud_"+forma.getIndexSolicitudesMap())+"");
				
			
			//carga el indicativo del TIPO de detalle del servicio
			forma.setIndexForwardDetalleSolicitudes("detSolicitudPaquetePool");						
		}	
		
		UtilidadBD.closeConnection(con);				
		return mapping.findForward(forma.getIndexForwardDetalleSolicitudes());						
	}	
	
	
	
	
	/**
	 * Validaciones para el cambio de Pooles
	 * @param RevisionCuentaPacienteForm forma
	 * @param String fecharespsolicitud
	 * */
	private void validacionesListadoPoolesMedico(RevisionCuentaPacienteForm forma, String fechaRespSolicitud, int estadoHistoriaClinica)
	{
		int numRegistros = Integer.parseInt(forma.getListadoPoolesMedico("numRegistros")+"");
		int poolesNoValidos=0;

		logger.info("EL ESTADO DE HISTORIA CLINICA------>"+estadoHistoriaClinica);
		
		for (int i=0; i<numRegistros; i++)
		{
			forma.setListadoPoolesMedico("validacionFecha_"+i,ConstantesBD.acronimoNo);			
			
			if(!fechaRespSolicitud.equals(""))
			{								
				if(!forma.getListadoPoolesMedico("fechaingreso_"+i).equals(""))
				{						
					if(UtilidadFecha.compararFechas(fechaRespSolicitud.split(ConstantesBD.separadorSplit)[0],fechaRespSolicitud.split(ConstantesBD.separadorSplit)[1],forma.getListadoPoolesMedico("fechaingreso_"+i)+"",forma.getListadoPoolesMedico("horaingreso_"+i)+"").isTrue())
					{
						if(!forma.getListadoPoolesMedico("fecharetiro_"+i).equals(""))
						{
							if(UtilidadFecha.compararFechas(forma.getListadoPoolesMedico("fecharetiro_"+i)+"",forma.getListadoPoolesMedico("horaretiro_"+i)+"",fechaRespSolicitud.split(ConstantesBD.separadorSplit)[0],fechaRespSolicitud.split(ConstantesBD.separadorSplit)[1]).isTrue())
								forma.setListadoPoolesMedico("validacionFecha_"+i,ConstantesBD.acronimoSi);
							else
							{
								forma.setListadoPoolesMedico("validacionFecha_"+i,ConstantesBD.acronimoNo);
								poolesNoValidos++;
							}
						}
						else
							forma.setListadoPoolesMedico("validacionFecha_"+i,ConstantesBD.acronimoSi);
					}
					else
					{
						forma.setListadoPoolesMedico("validacionFecha_"+i,ConstantesBD.acronimoNo);
						poolesNoValidos++;
					}
				}
				else
				{
					forma.setListadoPoolesMedico("validacionFecha_"+i,ConstantesBD.acronimoNo);
					poolesNoValidos++;
				}	
			}
			
			//Agregada la valdiacion para cuando venga en estado solicitada
			else if (estadoHistoriaClinica==ConstantesBD.codigoEstadoHCSolicitada)
			{
				forma.setListadoPoolesMedico("validacionFecha_"+i,ConstantesBD.acronimoSi);
			}

			else
			{
				forma.setListadoPoolesMedico("validacionFecha_"+i,ConstantesBD.acronimoNo);
				poolesNoValidos++;
			}	
		}			
		
		if((numRegistros - poolesNoValidos) == 0)
			forma.setListadoPoolesMedico("numRegistros","0");
	}
	
	
	
	
	
	
	/**
	 * Modifica la informacion de los pooles por medico
	 * @param Connection con
	 * @param RevisionCuentaPacienteForm forma
	 * */
	private ActionErrors modificarPoolesMedico(Connection con,
											   RevisionCuentaPacienteForm forma, 
											   ActionErrors errores)
	{
		int numRegistros = Integer.parseInt(forma.getListadoSolicitudesMap("numRegistros")+"");
		HashMap parametros = new HashMap();		 
		String estado = "";		
		
		boolean transacction = UtilidadBD.iniciarTransaccion(con);
		
		for(int i=0; i<numRegistros ; i++)
		{
			if(!(forma.getListadoSolicitudesMap("pool_"+i)+"").equals("") && 
					!(forma.getListadoSolicitudesMap("pool_"+i)+"").equals(ConstantesBD.codigoNuncaValido+""))
			{			
				if(!errores.isEmpty())
					return errores;
				
				parametros.put("pool",forma.getListadoSolicitudesMap("pool_"+i)+"");
				parametros.put("numerosolicitud",forma.getListadoSolicitudesMap("numerosolicitud_"+i)+"");
				transacction = RevisionCuenta.actualizarPoolesMedico(con, parametros);
				estado = "operacionTrue";					
			}
		}			
	
		if(transacction)
		{
			UtilidadBD.finalizarTransaccion(con);
			
			if(estado.equals("operacionTrue"))
			 logger.info("----->MODIFICO POOL");			
		}
		else
			UtilidadBD.abortarTransaccion(con);
	
		forma.setEstado(estado);				
		
		return errores;
	}
	
	
	
	/**
	 * Modifica la informacion de los pooles del listado del paquete
	 * @param Connection con
	 * @param RevisionCuentaPacienteForm forma
	 * */
	private ActionErrors modificarPoolesPaquete(Connection con,
											   RevisionCuentaPacienteForm forma, 
											   ActionErrors errores)
	{
		int numRegistros = Integer.parseInt(forma.getListadoDetallePaqueteMap("numRegistros")+"");
		HashMap parametros = new HashMap();		 
		String estado = "";		
		
		boolean transacction = UtilidadBD.iniciarTransaccion(con);
		
		for(int i=0; i<numRegistros ; i++)
		{
			if(!(forma.getListadoDetallePaqueteMap("pool_"+i)+"").equals(""))
			{			
				if(!errores.isEmpty())
					return errores;
				
				parametros.put("pool",forma.getListadoDetallePaqueteMap("pool_"+i)+"");
				parametros.put("numerosolicitud",forma.getListadoDetallePaqueteMap("numerosolicitud_"+i)+"");
				transacction = RevisionCuenta.actualizarPoolesMedico(con, parametros);
				estado = "operacionTrue";					
			}
		}			
	
		if(transacction)
		{
			UtilidadBD.finalizarTransaccion(con);
			
			if(estado.equals("operacionTrue"))
			 logger.info("----->MODIFICO POOL LISTADO PAQUETE");			
		}
		else
			UtilidadBD.abortarTransaccion(con);
	
		forma.setEstado(estado);				
		
		return errores;
	}
	
		
	/**
	 * Modifica la informacion de los pooles por medico en la Cirugia
	 * @param Connection con
	 * @param RevisionCuentaPacienteForm forma
	 * */
	private ActionErrors modificarPoolesCirugia(Connection con,
											    RevisionCuentaPacienteForm forma, 
											    ActionErrors errores)
	{
		int numRegistros = Integer.parseInt(forma.getDetalleSolicitudesMap("numRegistros")+"");
		HashMap parametros = new HashMap();		 
		String estado = "";		
		
		boolean transacction = UtilidadBD.iniciarTransaccion(con);
		
		for(int i=0; i<numRegistros ; i++)
		{
			if(!(forma.getDetalleSolicitudesMap("pool_"+i)+"").equals(""))
			{			
				if(!errores.isEmpty())
					return errores;
				
				parametros.put("pool",forma.getDetalleSolicitudesMap("pool_"+i)+"");
				parametros.put("codigo",forma.getDetalleSolicitudesMap("codigocxhonorarios_"+i)+"");
				transacction = RevisionCuenta.actualizarPoolesMedicoCirugia(con, parametros);
				estado = "operacionTrue";					
			}
		}			
	
		if(transacction)
		{
			UtilidadBD.finalizarTransaccion(con);
			
			if(estado.equals("operacionTrue"))
			 logger.info("----->MODIFICO POOL CIRUGIA");			
		}
		else
			UtilidadBD.abortarTransaccion(con);
	
		forma.setEstado(estado);				
		
		return errores;
	}
	
	
	
	
	
	/**
	 * Carga el flujo del listado de pooles por medico dependiendo del indicador
	 * @param Connection con
	 * @param RevisionCuentaPacienteForm forma
	 * @param String indicador
	 * */
	private void cargarInterfazPoolesMedico(Connection con,
											RevisionCuentaPacienteForm forma,
											String indicador)	
	{
				
		//Cargo el estado historia clinica para los valores cuando sea solicitada
		int estadoHistoriaClinica=Utilidades.convertirAEntero(forma.getListadoSolicitudesMap("estadohistoriaclinica_"+forma.getIndexSolicitudesMap())+"");
		
		//carga los pooles y evalua de que listado o HasMap se toman los datos
		if(indicador.equals("listado"))		
		{
			cargarListadoPoolesMedico(con,forma,forma.getListadoSolicitudesMap(),forma.getIndexSolicitudesMap());
			validacionesListadoPoolesMedico(forma,forma.getListadoSolicitudesMap("fecharespuesta_"+forma.getIndexSolicitudesMap())+"", estadoHistoriaClinica);
		}
		else if(indicador.equals("cirugia"))
		{
			cargarListadoPoolesMedico(con,forma,forma.getDetalleSolicitudesMap(),forma.getIndexDetalleSolicitudes());
			validacionesListadoPoolesMedico(forma,forma.getDetalleSolicitudesMap("fecharespuesta_"+forma.getIndexDetalleSolicitudes())+"", estadoHistoriaClinica);
		}
		else if(indicador.equals("paquete"))
		{
			cargarListadoPoolesMedico(con, forma,forma.getListadoDetallePaqueteMap(),forma.getIndexListadoDetallePaquete());
			validacionesListadoPoolesMedico(forma,forma.getListadoDetallePaqueteMap("fecharespuesta_"+forma.getIndexListadoDetallePaquete())+"", estadoHistoriaClinica);
		}
	}
	
	
	
	/***************************************************
	 * Metodos LoG
	 ***************************************************/
	
	/**
	 * Método que genera los Logs de la Modificación del Número de Autorización
	 * de la solicitud 
	 * @param forma
	 * @param usuario, user
	 * @param contador de solicitud
	 */
	private void generarLogAutorizacion(RevisionCuentaPacienteForm forma,UsuarioBasico usuario,int pos)
	{
	    String log;
	    
		    log="\n            ====INFORMACION ORIGINAL DEL NÚMERO DE AUTORIZACIÓN DE LA SOLICITUD===== ";
		    log+="\n Número de Solicitud   [" +forma.getListadoSolicitudesMap("numerosolicitud_"+forma.getIndexSolicitudesMap())+"] ";
		    //log+="\n Número Autorización   [" +forma.getDetalleSolicitudesMap("nroautorizacionold_"+pos)+"] ";		    
		    
		    log+="\n\n            ====INFORMACION DESPUÉS DE LA MODIFICACIÓN DEL NÚMERO DE AUTORIZACIÓN DE LA SOLICITUD===== " ;	    
		    
		    log+="\n Número de Solicitud   [" +forma.getListadoSolicitudesMap("numerosolicitud_"+forma.getIndexSolicitudesMap())+"] ";
		    //log+="\n Número Autorización   [" +forma.getDetalleSolicitudesMap("nroautorizacion_"+pos)+"] ";		    
		    	
		    log+="\n========================================================\n\n\n " ;
		    
			LogsAxioma.enviarLog(ConstantesBD.logNumeroAutorizacionCodigo, log, ConstantesBD.tipoRegistroLogModificacion,usuario.getInformacionGeneralPersonalSalud());	    
	}
	
	
	
	/**
	 * Metodo para cambiar el estado de las subcuentas de Excento a Cargada y viseversa
	 * @param con
	 * @param forma
	 * @param errores
	 * @param usuario 
	 */
	private void accionGuardarEstadoCargo(Connection con, RevisionCuentaPacienteForm forma, ActionErrors errores, UsuarioBasico usuario) 
	{
		forma.setMensajes(new ArrayList());
		forma.setSizeMensajes(0);
		
		// PREGUNTO SI LA DOS VARIABLES DEL ESTADO SON IGUALES
		if(forma.getEsExcenta().equals(""))
		{
			forma.getMensajes().add(new ActionMessage("errors.paciente.mensajeEstadoCargo").getKey());
			forma.setSizeMensajes(forma.getMensajes().size());
		}
		else
		{
			
			//Utilidades.imprimirMapa(forma.getListadoSolicitudesMap());
			
			int numRegistros = Utilidades.convertirAEntero(forma.getListadoSolicitudesMap().get("numRegistros")+"");

			logger.info("NUMEREGISTROS ->"+numRegistros);
			
			boolean estadoCargo=false;
			boolean actualizacionExitosa=false;
			
			int nuevoEstadoCargo=ConstantesBD.codigoNuncaValido;
			
			// SI esta activo la excenta se cambia la cargada para el cambio
			if(forma.getEsExcenta().equals(ConstantesBD.acronimoSi))
				nuevoEstadoCargo=ConstantesBD.codigoEstadoCuentaExcenta;
			else
				if(forma.getEsExcenta().equals(ConstantesBD.acronimoNo))
						nuevoEstadoCargo=ConstantesBD.codigoEstadoCuentaAsociada;
			
			logger.info("------- EL ESTADO A CAMBIAR ES -> "+nuevoEstadoCargo);
			
			// recorro el mapa del detalle de solicitudes
			for(int x=0;x<numRegistros;x++)
			{
				String numeroSolicitud="";
				
				// Pregunto si esta activa la orden para el cambio de estado
				if(forma.getListadoSolicitudesMap().get("estadocargo_"+x).equals(ConstantesBD.acronimoSi))
				{
					logger.info("-------CAMBIA VARIABLE ESTADO -------");
					estadoCargo=true;
					numeroSolicitud=forma.getListadoSolicitudesMap("numerosolicitud_"+x)+"";
					
					forma.setDetEstadoAnterior(new HashMap());
					
					forma.setDetEstadoAnterior(RevisionCuenta.detalleEstadoSolicitud(con, numeroSolicitud));
					
				    int ex=0;
				    int exo=0;
					logger.info("MAPA ANTES DEL CAMBIO ANTERIOR ->>>> "+forma.getDetEstadoAnterior());
					
					actualizacionExitosa=RevisionCuenta.actualizarEstadoCargo(con, nuevoEstadoCargo, numeroSolicitud);
					
					//-------------- LOG DE REGISTRO INFORMACION-----------
					if(actualizacionExitosa == true)
					{
						
						forma.setDetEstadoActual(new HashMap());
						forma.setDetEstadoActual(RevisionCuenta.detalleEstadoSolicitud(con, numeroSolicitud));
						
						logger.info("MAPA ANTES DEL CAMBIO ACTUAL ->>>> "+forma.getDetEstadoActual());
						
						int numRegEstadoActual = Utilidades.convertirAEntero(forma.getDetEstadoActual().get("numRegistros")+"");
						
						int numRegEstadoAnterior = Utilidades.convertirAEntero(forma.getDetEstadoAnterior().get("numRegistros")+"");
						
						if(numRegEstadoActual == numRegEstadoAnterior)
						{
							
							logger.info("NUMERO DE REGISTROS ESTADO ACTUAL ->"+numRegEstadoActual);
							
							for(int y=0;y<numRegEstadoActual;y++)
							{
								logger.info("\n\n VUELTA "+y);
								// VALIDO QUE LOS CODIGOS DE DETALLE DEL CARGO SEAN IGUALES
								if((forma.getDetEstadoAnterior().get("codigo_detalle_cargo_"+y)+"").equals(forma.getDetEstadoActual().get("codigo_detalle_cargo_"+y)+""))
								{
									// VALIDO SI EXITE CAMBIO EN EL ESTADO DEL CODIGO DE ANTERIOR A ACTUAL
									if(!(forma.getDetEstadoAnterior().get("estado_"+y)+"").equals(forma.getDetEstadoActual().get("estado_"+y)+""))
									{
										logger.info("SI EXISTE UN CAMBIO PARA INSERTAR EN EL LOG");
										logger.info("------------------------------");
										logger.info("CODIGO DETALLE CARGO ->"+forma.getDetEstadoActual().get("codigo_detalle_cargo_"+y));
										logger.info("ESTADO ANTERIOR ->"+forma.getDetEstadoAnterior().get("estado_"+y));
										logger.info("ESTADO ACTUAL ->"+forma.getDetEstadoActual().get("estado_"+y));
										logger.info("FECHA ->"+UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
										logger.info("HORA ->"+UtilidadFecha.getHoraActual());
										logger.info("USUARIO ->"+usuario.getLoginUsuario());
										logger.info("------------------------------");
										
										RevisionCuenta.insertarLogEstadoCargo(con, forma.getDetEstadoActual().get("codigo_detalle_cargo_"+y)+"", 
																					forma.getDetEstadoAnterior().get("estado_"+y)+"",
																					forma.getDetEstadoActual().get("estado_"+y)+"",
																					usuario.getLoginUsuario());
										
									}
								}
							}
						}						
					}
					//-------------- FIN LOG DE REGISTRO INFORMACION-----------
					
				}
				
				// Para cambiar el estado del Check Box de SI a NO
				forma.setListadoSolicitudesMap("estadocargo_"+x, ConstantesBD.acronimoNo);
				forma.setTodosEstadoCargo(ConstantesBD.acronimoNo);
				forma.setEsExcenta("");
				
			}
			
			// SI EL BOLEANO DEL ESTADO CARGO ES FALSE SIGNIFICA QUE NO HAY NINGUNA ORDEN ACTIVA
			if(estadoCargo == false)
			{
				forma.getMensajes().add(new ActionMessage("errors.paciente.mensajeEstadoCargo").getKey());
				forma.setSizeMensajes(forma.getMensajes().size());
			}
			
			// VALIDACION PARA MOSTRAR LOS MENSAJES DE INFORMACION AL USUARIO
			if(actualizacionExitosa == true)
			{
				forma.getMensajes().add(new ActionMessage("prompt.registroExitosoDetalle").getKey());
				forma.setSizeMensajes(forma.getMensajes().size());
			}
			else
			{
				forma.getMensajes().add(new ActionMessage("errors.procesoNoExitoso").getKey());
				forma.setSizeMensajes(forma.getMensajes().size());
			}
		}

		
	}
}	