package com.princetonsa.action.manejoPaciente;

import java.sql.Connection;
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
import util.RespuestaValidacion;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.UtilidadValidacion;
import util.Utilidades;
import util.historiaClinica.UtilidadesHistoriaClinica;
import util.manejoPaciente.UtilidadesManejoPaciente;

import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.manejoPaciente.ListadoIngresosForm;
import com.princetonsa.dto.manejoPaciente.DtoAdjAutorizacion;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.cargos.Convenio;
import com.princetonsa.mundo.manejoPaciente.ListadoIngresos;

/**
 * 
 * @author Jhony Alexander Duque A.
 * @modificated Felipe Pérez Granda
 *
 *Clase utilizada para el manejo del listado de ingresos en 
 *actualizacion de autorizaciones del modulo manejo paciente.
 */


public class ListadoIngresosAction extends Action{

		/**
	 * Objeto para manejar los logs de esta clase
	*/
	private static Logger logger = Logger.getLogger(ListadoIngresosAction.class);
	
	

	/*----------------------------------------------------------------------------------
	 * 						METODO EXECUTE DEL ACTION
	 ----------------------------------------------------------------------------------*/
	public ActionForward execute (ActionMapping mapping,
									ActionForm form,
									HttpServletRequest request,
									HttpServletResponse response) throws Exception{

		Connection connection=null;
		try{
			if(form instanceof ListadoIngresosForm)
			{

				connection = UtilidadBD.abrirConexion();

				//se verifica si la conexion esta nula
				if (connection == null)
				{
					// de ser asi se envia a una pagina de error. 
					request.setAttribute("CodigoDescripcionError","erros.problemasBd");
					return mapping.findForward("paginaError");
				}

				//se obtiene el usuario cargado en sesion.
				UsuarioBasico usuario = (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");

				//se obtiene el paciente cargado en sesion.
				PersonaBasica paciente = (PersonaBasica)request.getSession().getAttribute("pacienteActivo");

				//se instancia la forma
				ListadoIngresosForm forma = (ListadoIngresosForm)form; 

				//se instancia el mundo
				ListadoIngresos mundo = new ListadoIngresos();

				//optenemos el estado que contiene la forma.
				String estado = forma.getEstado();

				//indices
				String [] indicesListado = mundo.indicesListado;
				String [] indicesServiciosArticulos = mundo.indicesServiciosArticulos;
				String [] indicesXRangos = mundo.indicesXRangos;

				ActionErrors errores = new ActionErrors();

				//se obtiene la institucion
				InstitucionBasica institucion = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");


				logger.info("\n\n***************************************************************************");
				logger.info(" 	    EL ESTADO DE ListadoIngresosForm ES ====>> "+estado);
				logger.info("\n***************************************************************************");

				/*----------------------------------------------------------------------
				 * 	ESTADOS PARA ACTUALIZACION AUTORIZACIONES MEDICAS MODIFICADO SHAIO
			 ----------------------------------------------------------------------*/

				/*----------------------------------------------
				 * ESTADO =================>>>>>  NULL
			 ---------------------------------------------*/

				if (estado == null)
				{
					//inicializamos los valores de los HashMap.
					forma.reset();
					//inicializamos los valores de los atributos del pager.
					forma.resetPager();
					//se saca un log con el siguiente texto.
					logger.warn("Estado no Valido dentro del Flujo de Actualizacion Autorizaciones Medicas (null)");
					//se asigana un error a mostar por ser un estado invalido
					request.setAttribute("CodigoDescripcionError","errors.estadoInvalido");
					//se cierra la conexion con la BD
					UtilidadBD.cerrarConexion(connection);

					//se retorna el error al forward paginaError.
					return mapping.findForward("paginaError");
				}
				/*----------------------------------------------
				 * ESTADO =================>>>>>  MENU
			 ---------------------------------------------*/
				else if (estado.equals("menu"))
				{
					return mapping.findForward("menu");	
				}
				/*
				 * Estado empezar por rangos
				 */
				else if (estado.equals("empezarPorRangos"))
				{
					forma.resetPorRangos();
					forma.resetMensaje();
					forma.setOperacionTrue(false);
					mundo.cargarCamposSeleccion(connection, forma, usuario);
					return mapping.findForward("buscarAutorizaciones");
				}
				/*
				 * Estado buscar por rangos
				 */
				else if(estado.equals("buscarXRangos"))
				{
					String estados = "";
					String[] viaIngreso = forma.getViaIngresoSeleccionado();
					int numReg = viaIngreso.length;
					forma.setOperacionTrue(false);
					HashMap tmp = new HashMap();
					if (numReg==1)
					{
						if (!(forma.getViaIngresoSeleccionado()[0]).equals(""))
							forma.setCriterios("viaIngreso",""+forma.getViaIngresoSeleccionado()[0]+"");
					}
					else
					{
						if (numReg>1)
							for (int i=0;i<numReg;i++)
								if (i==0)
									estados=""+forma.getViaIngresoSeleccionado()[i]+"";
								else
									estados+=","+forma.getViaIngresoSeleccionado()[i]+"";

						forma.setCriterios("viaIngreso", estados);
					}


					errores = this.validaciones(connection, forma.getCriterios(), estado, forma);
					forma.setXRangoIngreso(true);

					if(!errores.isEmpty())
					{
						saveErrors(request,errores);	
						UtilidadBD.closeConnection(connection);
						return mapping.findForward("buscarAutorizaciones");
					}
					tmp.putAll(mundo.consultaXRangosParaVista(connection, forma.getCriterios()));
					forma.setResultados(tmp);

					return mapping.findForward("resultadoBuscarAutorizaciones");
				}
				else if (estado.equals("generarXrangos"))
				{
					forma.setOperacionTrue(false);
					HashMap tmp = new HashMap();

					tmp.putAll(mundo.consultaXRangos(connection, forma.getCriterios()));
					forma.resetMensaje();
					mundo.generar(connection, forma, usuario, request, mapping, institucion);
					UtilidadBD.cerrarConexion(connection);
					return mapping.findForward("resultadoBuscarAutorizaciones");
				}

				else if (estado.equals("ordenarXRango"))
				{
					Utilidades.imprimirArreglo(indicesXRangos);
					forma.setResultados(mundo.accionOrdenarMapa(forma.getResultados(), forma, indicesXRangos ));

					UtilidadBD.closeConnection(connection);
					return mapping.findForward("resultadoBuscarAutorizaciones");	
				}
				
				else if(estado.equals("cargarPisosXCentroAtencion")){
					mundo.cargarPisosXCentroAtencion(connection, forma, usuario);
					UtilidadBD.closeConnection(connection);
					return mapping.findForward("buscarAutorizaciones");
				}

				/*
				 * Estado empezarPacienteCargado
				 * Es el mismo estado empezar, con la diferencia que el paciente se carga automáticamente una vez seleccionado su numero de ingreso
				 */
				else if (estado.equals("empezarPacienteCargado"))
				{ 
					//inicializamos los valores de los HashMap. menos el del index
					forma.resetMenosIndex();
					//inicializamos los valores de los atributos del pager.
					forma.resetPager();

					/*
					 * Cargamos al paciente
					 */
					paciente.setCodigoPersona(
							Integer.parseInt(forma.getResultados().get("codigo_"+forma.getIndex())+""));
					UtilidadesManejoPaciente.cargarPaciente(connection, usuario, paciente, request);

					/**********************************************
					 *  Validacion Cargado Paciente
					 **********************************************/
					RespuestaValidacion resp = UtilidadValidacion.esValidoPacienteCargado(connection, paciente);

					if(!resp.puedoSeguir)
						return ComunAction.accionSalirCasoError(mapping, request, connection, logger, resp.textoRespuesta, resp.textoRespuesta, true);
					else
						forma.setListadoIngresos(mundo.cargarListadoIngresos(connection, paciente.getCodigoPersona(), usuario.getCodigoInstitucionInt()));

					if (Utilidades.convertirAEntero(forma.getListadoIngresos("numRegistros")+"")==1)
					{
						forma.setIndex("0");
						mundo.cargarDetalle(connection, forma,usuario);

						if(forma.getXRangoIngreso()==true)
						{
							forma.setEstado("cargarIngreso");
						}
						return mapping.findForward("principal");
					}
					else
					{
						int numRegistros = Integer.parseInt(forma.getListadoIngresos("numRegistros")+"");
						forma.setIndex((numRegistros-1)+"");
						mundo.cargarDetalle(connection, forma,usuario);
						forma.setVolverXRango(true);
						return mapping.findForward("principal");
					}

				}

				/*----------------------------------------------
				 * ESTADO =================>>>>>  EMPEZAR
			 ---------------------------------------------*/
				else
					if (estado.equals("empezar"))
					{ 
						//inicializamos los valores de los HashMap.
						forma.reset();
						//inicializamos los valores de los atributos del pager.
						forma.resetPager();

						/**********************************************
						 *  Validacion Cargado Paciente
						 **********************************************/
						RespuestaValidacion resp = esValidoPacienteCargado(paciente);
						if(!resp.puedoSeguir)
							return ComunAction.accionSalirCasoError(mapping, request, connection, logger, resp.textoRespuesta, resp.textoRespuesta, true);
						else
							forma.setListadoIngresos(mundo.cargarListadoIngresos(connection, paciente.getCodigoPersona(), usuario.getCodigoInstitucionInt()));

						if (Utilidades.convertirAEntero(forma.getListadoIngresos("numRegistros")+"")==1)
						{
							forma.setIndex("0");
							mundo.cargarDetalle(connection, forma,usuario);
							if(forma.getXRangoIngreso()==true)
								forma.setEstado("cargarIngreso");
							return mapping.findForward("principal");
						}
						return mapping.findForward("listado");
					}
					else/*----------------------------------------------
					 * ESTADO =================>>>>>  ORDENAR
					 ---------------------------------------------*/
						if (estado.equals("ordenar"))
						{
							forma.setListadoIngresos(mundo.accionOrdenarMapa(forma.getListadoIngresos(), forma,indicesListado ));
							UtilidadBD.closeConnection(connection);
							return mapping.findForward("listado");	
						}
						else/*----------------------------------------------
						 * ESTADO =================>>>>>  CARGARINGRESO
						 ---------------------------------------------*/
							if (estado.equals("cargarIngreso"))
							{
								mundo.cargarDetalle(connection, forma,usuario);
								forma.setXRangoIngreso(false);
								return mapping.findForward("principal");	
							}
							else/*----------------------------------------------
							 * ESTADO =================>>>>>  CARGARSERVICIOSARTICULOS
							 ---------------------------------------------*/
								if (estado.equals("cargarServiciosArticulo"))
								{
									forma.resetBusqueda();
									forma.setCuerpoDetalle(mundo.cargarServiciosArticulos(connection, forma));
									forma.setCuerpoDetalleOld((HashMap)forma.getCuerpoDetalle().clone());
									return mapping.findForward("principal");	
								}
								else/*----------------------------------------------
								 * ESTADO =================>>>>>  BUSQUEDAAVANZADA
								 ---------------------------------------------*/
									if (estado.equals("busquedaAvanzada"))
									{
										forma.setCuerpoDetalle(mundo.cargarServiciosArticulos(connection, forma));
										return mapping.findForward("principal");	
									}
									else/*----------------------------------------------
									 * ESTADO =================>>>>>  ORDENARDETALLE
									 ---------------------------------------------*/
										if (estado.equals("ordenarDetalle"))
										{
											forma.setCuerpoDetalle(mundo.accionOrdenarMapa(forma.getCuerpoDetalle(), forma,indicesServiciosArticulos));
											UtilidadBD.closeConnection(connection);
											return mapping.findForward("principal");	
										}
										else/*----------------------------------------------
										 * ESTADO =================>>>>>  GUARDAR
										 ---------------------------------------------*/
											if (estado.equals("guardar"))
											{
												mundo.guardar(connection, forma,usuario);
												return mapping.findForward("principal");	
											}
											else /*----------------------------
											 * 	ESTADO ==> REDIRECCION
											 ----------------------------*/
												if (estado.equals("redireccion"))
												{
													UtilidadBD.closeConnection(connection);
													response.sendRedirect(forma.getLinkSiguiente());
													return null;
												}
				//Estado usado para mostrar las opciones de edición de autorización
												else if(estado.equals("iniciarEditarAutorizacion"))
												{
													return accionIniciarEditarAutorizacion(connection,forma,mapping);
												}
				//Estado para realizar el envío de una autorización
												else if(estado.equals("enviarAutorizacion"))
												{
													return accionEnviarAutorizacion(connection,forma,mapping,usuario);
												}
				//Estado usado para iniciar la adición de adjuntos
												else if(estado.equals("iniciarAdjuntarArchivos"))
												{
													return accionIniciarAdjuntarArchivos(connection,forma,mapping,usuario);
												}
				//Estado usado para guardar el envío de autorizacon
												else if (estado.equals("guardarEnviarAutorizacion"))
												{
													return accionGuardarEnviarAutorizacion(connection,forma,mapping,usuario);
												}




			}
		}catch (Exception e) {
			Log4JManager.error(e);
		}
		finally{
			UtilidadBD.closeConnection(connection);
		}
		return null;	

	}
	
	/**
	 * Método para guardar el envío de una autorización
	 * @param con
	 * @param forma
	 * @param mapping
	 * @param usuario
	 * @return
	 */
	private ActionForward accionGuardarEnviarAutorizacion(Connection con, ListadoIngresosForm forma,ActionMapping mapping, UsuarioBasico usuario) 
	{
		/**
		 * PENDIENTE CONSTRUCCION
		 */
		
		return null;
	}

	/**
	 * Método implementado para iniciar la adicion de archivos adjuntos
	 * @param connection
	 * @param forma
	 * @param mapping
	 * @param usuario 
	 * @return
	 */
	private ActionForward accionIniciarAdjuntarArchivos(Connection connection,ListadoIngresosForm forma, ActionMapping mapping, UsuarioBasico usuario) 
	{
		//Si es un nuevo adjunto se adiciona a la estructura
		if(forma.getPosAdjuntos()==ConstantesBD.codigoNuncaValido)
		{
			DtoAdjAutorizacion adjAutorizacion = new DtoAdjAutorizacion();
			adjAutorizacion.setUsuarioModifica(usuario);
			forma.getAutorizacion().getAdjuntos().add(adjAutorizacion);
			forma.setPosAdjuntos(forma.getAutorizacion().getAdjuntos().size()-1);
		}
		
		UtilidadBD.closeConnection(connection);
		return mapping.findForward("seleccionarArchivo");
	}

	/**
	 * Método implementado para enviar una autorización
	 * @param connection
	 * @param forma
	 * @param mapping
	 * @param usuario 
	 * @return
	 */
	private ActionForward accionEnviarAutorizacion(Connection connection,ListadoIngresosForm forma, ActionMapping mapping, UsuarioBasico usuario) 
	{
		forma.setAutorizacion(ListadoIngresos.prepararDatosEnviar(connection, forma.getCuerpoDetalle(), forma.getPosSolicitud(), forma.getEncabezadoDetalle(),usuario));
		
		//Se cargan los arreglos
		forma.setTiposSerSol(UtilidadesManejoPaciente.obtenerTiposServicioSolicitados(connection, usuario.getCodigoInstitucionInt(), true));
		//Si no hay coberturas se carga el arreglo de coberturas
		if(forma.getAutorizacion().getCodigoTipoCobertura()<=0)
		{
			forma.setCoberturasSalud(UtilidadesManejoPaciente.obtenerCoberturasSaludXTipoRegimenSubCuenta(connection, forma.getCuerpoDetalle(ListadoIngresos.indicesServiciosArticulos[34]+forma.getPosSolicitud()).toString(), usuario.getCodigoInstitucionInt()));
		}
		//Si no hay origen atencion se carga el arreglo de causas externas
		if(forma.getAutorizacion().getCodigoOrigenAtencion()<=0)
		{
			forma.setOrigenesAtencion(UtilidadesHistoriaClinica.obtenerCausasExternas(connection,true));
		}
		//Se cargan los medios de envío
		forma.setMediosEnvio(Convenio.cargarMediosEnvio(connection, forma.getCodigoConvenio()));
		//Se cargan las entidades de envio
		forma.setEntidadesEnvio(UtilidadesManejoPaciente.cargarEntidadesEnvio(connection, usuario.getCodigoInstitucionInt(), forma.getCodigoConvenio()));
		
		UtilidadBD.closeConnection(connection);
		return mapping.findForward("enviarAutorizacion");
	}

	/**
	 * Método implementado para iniciar la edición de la autorizacion
	 * @param connection
	 * @param forma
	 * @param mapping
	 * @return
	 */
	private ActionForward accionIniciarEditarAutorizacion(Connection connection, ListadoIngresosForm forma,ActionMapping mapping) 
	{
		forma.setEstadoAutorizacionSolicitud(forma.getCuerpoDetalle(ListadoIngresos.indicesServiciosArticulos[29]+forma.getPosSolicitud()).toString());
		forma.setTipoTramiteSolicitud(forma.getCuerpoDetalle(ListadoIngresos.indicesServiciosArticulos[30]+forma.getPosSolicitud()).toString());
		forma.setEsVigenteSolicitud(UtilidadTexto.getBoolean(forma.getCuerpoDetalle(ListadoIngresos.indicesServiciosArticulos[32]+forma.getPosSolicitud()).toString()));
		forma.setCodigoConvenio(Integer.parseInt(forma.getCuerpoDetalle(ListadoIngresos.indicesServiciosArticulos[35]+forma.getPosSolicitud()).toString()));
		
		if(forma.getEstadoAutorizacionSolicitud().equals("")||forma.getEstadoAutorizacionSolicitud().equals(ConstantesIntegridadDominio.acronimoEstadoSolicitado))
		{
			forma.setPuedoEnviar(true);
			forma.setPuedoRegistrarRespuesta(true);
			forma.setPuedoAnularRespuesta(false);
		}
		else if(forma.getEstadoAutorizacionSolicitud().equals(ConstantesIntegridadDominio.acronimoAutorizado))
		{
			if(forma.isEsVigenteSolicitud())
			{
				forma.setPuedoEnviar(false);
				forma.setPuedoAnularRespuesta(true);
			}
			else
			{
				forma.setPuedoEnviar(true);
				forma.setPuedoAnularRespuesta(false);
			}
			forma.setPuedoRegistrarRespuesta(false);
			
		}
		else if(forma.getEstadoAutorizacionSolicitud().equals(ConstantesIntegridadDominio.acronimoEstadoNegado))
		{
			forma.setPuedoEnviar(false);
			forma.setPuedoRegistrarRespuesta(false);
			if(forma.isEsVigenteSolicitud())
			{
				forma.setPuedoAnularRespuesta(true);
			}
			else
			{
				forma.setPuedoAnularRespuesta(false);
			}
		}
		else if(forma.getEstadoAutorizacionSolicitud().equals(ConstantesIntegridadDominio.acronimoEstadoAnulado))
		{
			forma.setPuedoEnviar(true);
			forma.setPuedoRegistrarRespuesta(true);
			forma.setPuedoAnularRespuesta(false);
		}
		else
		{
			forma.setPuedoEnviar(false);
			forma.setPuedoRegistrarRespuesta(false);
			forma.setPuedoAnularRespuesta(false);
		}
		
		UtilidadBD.closeConnection(connection);
		return mapping.findForward("editarAutorizacion");
	}
	/**
	 * Método encargado de realizar todas las validaciones
	 * @param connection
	 * @param criterios
	 * @return
	 */
	public static ActionErrors validaciones (Connection con, HashMap criterios, String estado, ListadoIngresosForm forma)
	{
		ActionErrors errores = new ActionErrors();
		
		logger.info("===> Entramos a validaciones !!!");
		
		if (estado.equals("buscarXRangos") || estado.equals("generarXrangos"))
		{
			
			forma.setOperacionTrue(false);
			boolean fechaIngreso=true;
			boolean fechaSolicitud=true;
			/*
			 * Posicion 0 = centroAtencion
			 */
			if(!UtilidadCadena.noEsVacio(forma.getCriterios("centroAtencion")+"") && 
				(forma.getCriterios("centroAtencion")+"").equals(ConstantesBD.codigoNuncaValido+"" ))
				errores.add("centroAtencion", new ActionMessage("errors.required", "ES NECESARIO SELECCIONAR UN CENTRO DE ATENCIÓN, "));
			
			/*
			 * Validamos el centro de atención
			 * El usuario DEBE de seleccionar uno en especial o la opción de TODOS
			 */
			if(!UtilidadCadena.noEsVacio(forma.getCriterios("centroAtencion")+"") || 
			  (forma.getCriterios("centroAtencion")+"").equals(ConstantesBD.codigoNuncaValido+""))
				errores.add("centroAtencion", new ActionMessage("errors.required","ES NECESARIO SELECCIONAR UN CENTRO DE ATENCIÓN, "));
			
			/*
			 * Validamos el Campo Tipo de Salida que es Requerido
			 */
			if(!UtilidadCadena.noEsVacio(forma.getCriterios("tipoSalida")+"") || 
				(forma.getCriterios("tipoSalida")+"").equals(ConstantesBD.codigoNuncaValido+""))
				errores.add("tipoSalida", new ActionMessage("errors.required","El Tipo de Salida "));
			
			/*
			 * Validación del campo: Indicativo Orden Es requerido
			 */
			if(!UtilidadCadena.noEsVacio(forma.getCriterios("indicativoOrden")+"") || 
				(forma.getCriterios("indicativoOrden")+"").equals(ConstantesBD.codigoNuncaValido+""))
				errores.add("indicativoOrden", new ActionMessage("errors.required","El Indicativo de Orden "));
			
			/*
			 * Validación de las Fechas
			 * Almenos 1 de 2 tipos de fechas debe de estar seleccionado
			 * Fechas de Ingreso o
			 * Fechas de Solciitud
			 */
			
			/*
			 * Caso 1: Si todas las fechas vienen sin llenar
			 */
			
			if(!UtilidadCadena.noEsVacio(forma.getCriterios("fechaIngresoInicial")+"") &&
				!UtilidadCadena.noEsVacio(forma.getCriterios("fechaIngresoFinal")+"") &&
				!UtilidadCadena.noEsVacio(forma.getCriterios("fechaSolicitudInicial")+"") &&
				!UtilidadCadena.noEsVacio(forma.getCriterios("fechaSolicitudFinal")+""))
			{
				fechaIngreso=false;
				fechaSolicitud=false;
				errores.add("descripcion",new ActionMessage("errors.required","Debe de seleccionar por lo menos 1 de los 2 " +
						"tipos de fechas (Ingreso o Solicitud) para realizar la búsqueda"));
			}
			
			else
			{
				/*
				 * Caso 2: Si las fechas de solicitud vienen vacías, valido las fechas de ingreso
				 */
				if(!UtilidadCadena.noEsVacio(forma.getCriterios("fechaSolicitudInicial")+"") &&
						!UtilidadCadena.noEsVacio(forma.getCriterios("fechaSolicitudFinal")+""))
				{
					//1)Validacion: Requerido Fecha Ingreso Incial
					if (!UtilidadCadena.noEsVacio(forma.getCriterios("fechaIngresoInicial")+""))
					{
						fechaIngreso=false;
						errores.add("descripcion",new ActionMessage("errors.required","Seleccionar la Fecha de Ingreso Inicial"));
					}
					
					else//2)Validacion: La Fecha Ingreso Inicial sea menor a la fecha actual del sistema
						if (!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(forma.getCriterios("fechaIngresoInicial")+"",UtilidadFecha.getFechaActual()))
						{
							fechaIngreso=false;
							errores.add("", new ActionMessage("errors.fechaPosteriorIgualActual", "Inicial "+
									forma.getCriterios("fechaIngresoInicial"), "Actual "+UtilidadFecha.getFechaActual()));
						}
					
					//3)Validacion: Requerido Fecha Ingreso Final
					if (!UtilidadCadena.noEsVacio(forma.getCriterios("fechaIngresoFinal")+""))
					{
						fechaIngreso=false;
						errores.add("descripcion",new ActionMessage("errors.required","Seleccionar la Fecha de Ingreso Final"));
					}
					
					else
					//4)Validacion: La fecha Ingreso final sea menor o igual a la fecha actual
						if (!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(forma.getCriterios("fechaIngresoFinal")+"",UtilidadFecha.getFechaActual()))
						{
							fechaIngreso=false;
							errores.add("", new ActionMessage("errors.fechaPosteriorIgualActual", "Final "+
									forma.getCriterios("fechaIngresoFinal"), "Actual "+UtilidadFecha.getFechaActual()));
						}
					
					//5) Validacion: La fecha final debe ser mayor a la fecha incial
					
					if(!(forma.getCriterios("fechaIngresoInicial")+"").equals("") && !(forma.getCriterios("fechaIngresoFinal")+"").equals(""))
					{
						if (UtilidadFecha.numeroMesesEntreFechasExacta(forma.getCriterios("fechaIngresoInicial")+"", 
								forma.getCriterios("fechaIngresoFinal")+"") == -1)
						{
							fechaIngreso=false;
							errores.add("", new ActionMessage("errors.fechaPosteriorIgualActual", "Inicial "+
									forma.getCriterios("fechaIngresoInicial"), "Final "+forma.getCriterios("fechaIngresoFinal")+""));
						}
					}
					
					if (fechaIngreso)
					{
						if(UtilidadFecha.numeroDiasEntreFechas(forma.getCriterios("fechaIngresoInicial")+"", forma.getCriterios("fechaIngresoFinal")+"")>123)
							//En donde el numero final al cual se compara, es el numero de meses del rango.
							errores.add("", new ActionMessage("errors.debeSerNumeroMenorIgual","El rango de dias entre fechas", "120 Días"));
					}
				}
				
				else
				{
					/*
					 * Caso 4: Si la fecha de ingreso inicial viene llena y la fecha de ingreso final está vacía
					 */
					if(!UtilidadCadena.noEsVacio(forma.getCriterios("fechaIngresoInicial")+"") &&
						UtilidadCadena.noEsVacio(forma.getCriterios("fechaIngresoFinal")+""))
					{
						fechaIngreso=false;
						errores.add("descripcion",new ActionMessage("errors.required","Seleccionar la Fecha de Ingreso Inicial"));
					}
					
					/*
					 * Caso 5: Si la fecha de ingreso inicial viene vacía y la fecha de ingreso final está llena
					 */
					if(UtilidadCadena.noEsVacio(forma.getCriterios("fechaIngresoInicial")+"") &&
						!UtilidadCadena.noEsVacio(forma.getCriterios("fechaIngresoFinal")+""))
					{
						fechaIngreso=false;
						errores.add("descripcion",new ActionMessage("errors.required","Seleccionar la Fecha de Ingreso Final"));
					}
				}
				
				/*
				 * Caso 3: Si las fechas de ingreso vienen vacías, valido las fechas de solicitud
				 */
				if(!UtilidadCadena.noEsVacio(forma.getCriterios("fechaIngresoInicial")+"") &&
						!UtilidadCadena.noEsVacio(forma.getCriterios("fechaIngresoFinal")+""))
				{
					//6)Validacion: Requerido Fecha Solicitud Incial
					if (!UtilidadCadena.noEsVacio(forma.getCriterios("fechaSolicitudInicial")+""))
					{
						fechaSolicitud=false;
						errores.add("descripcion",new ActionMessage("errors.required","Seleccionar la Fecha de Solicitud Inicial"));
					}
					
					else//7)Validacion: La Fecha Solicitud Inicial sea menor a la fecha actual del sistema
						if (!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(forma.getCriterios("fechaSolicitudInicial")+"",UtilidadFecha.getFechaActual()))
						{
							fechaSolicitud=false;
							errores.add("", new ActionMessage("errors.fechaPosteriorIgualActual", "Inicial "+
									forma.getCriterios("fechaSolicitudInicial"), "Actual "+UtilidadFecha.getFechaActual()));
						}
					
					//8)Validacion: Requerido Fecha Solicitud Final
					if (!UtilidadCadena.noEsVacio(forma.getCriterios("fechaSolicitudFinal")+""))
					{
						fechaSolicitud=false;
						errores.add("descripcion",new ActionMessage("errors.required","Seleccionar la Fecha de Solicitud Final"));
					}
					
					else
					//9)Validacion: La fecha Solicitud final sea menor o igual a la fecha actual
						if (!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(forma.getCriterios("fechaSolicitudFinal")+"",UtilidadFecha.getFechaActual()))
						{
							fechaSolicitud=false;
							errores.add("", new ActionMessage("errors.fechaPosteriorIgualActual", "Final "+
									forma.getCriterios("fechaSolicitudFinal"), "Actual "+UtilidadFecha.getFechaActual()));
						}
					
					//10) Validacion: La fecha final debe ser mayor a la fecha incial
					
					if(!(forma.getCriterios("fechaSolicitudInicial")+"").equals("") && !(forma.getCriterios("fechaSolicitudFinal")+"").equals(""))
					{
						if (UtilidadFecha.numeroMesesEntreFechasExacta(forma.getCriterios("fechaSolicitudInicial")+"", 
								forma.getCriterios("fechaSolicitudFinal")+"") == -1)
						{
							fechaSolicitud=false;
							errores.add("", new ActionMessage("errors.fechaPosteriorIgualActual", "Inicial "+
									forma.getCriterios("fechaSolicitudInicial"), "Final "+forma.getCriterios("fechaSolicitudFinal")+""));
						}
					}
					
					if (fechaSolicitud)
					{
						if(UtilidadFecha.numeroDiasEntreFechas(forma.getCriterios("fechaSolicitudInicial")+"", forma.getCriterios("fechaSolicitudFinal")+"")>123)
							//En donde el numero final al cual se compara, es el numero de meses del rango.
							errores.add("", new ActionMessage("errors.debeSerNumeroMenorIgual","El rango de dias entre fechas", "120 Días"));
					}
				}
				else
				{
					/*
					 * Caso 6: Si la fecha de ingreso inicial viene llena y la fecha de ingreso final está vacía
					 */
					if(!UtilidadCadena.noEsVacio(forma.getCriterios("fechaSolicitudInicial")+"") &&
						UtilidadCadena.noEsVacio(forma.getCriterios("fechaSolicitudFinal")+""))
					{
						fechaIngreso=false;
						errores.add("descripcion",new ActionMessage("errors.required","Seleccionar la Fecha de Solicitud Inicial"));
					}
					
					/*
					 * Caso 7: Si la fecha de ingreso inicial viene vacía y la fecha de ingreso final está llena
					 */
					if(UtilidadCadena.noEsVacio(forma.getCriterios("fechaSolicitudInicial")+"") &&
						!UtilidadCadena.noEsVacio(forma.getCriterios("fechaSolicitudFinal")+""))
					{
						fechaIngreso=false;
						errores.add("descripcion",new ActionMessage("errors.required","Seleccionar la Fecha de Solicitud Final"));
					}
				}
			}
		}
		return errores;
	}
	
	
	
	/**
	 * 	Valida el Paciente Cargado en Session     
	 *	@param Connection con
	 *	@param PersonaBasica personabasica
	 * */
	public static RespuestaValidacion esValidoPacienteCargado(PersonaBasica paciente)
	{
		
		
		if(paciente.getCodigoPersona()<1)
		{		    
		    return new RespuestaValidacion("errors.paciente.noCargado",false);
		}
			
			
		return new RespuestaValidacion("",true);		
	}
	
}
