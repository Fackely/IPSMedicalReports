package com.princetonsa.action.manejoPaciente;

import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.axioma.util.log.Log4JManager;

import util.BackUpBaseDatos;
import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.Listado;
import util.TxtFile;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.UtilidadFileUpload;
import util.UtilidadSesion;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.manejoPaciente.UtilidadesManejoPaciente;

import com.princetonsa.actionform.manejoPaciente.CensoCamasForm;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.TrasladoCamas;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.manejoPaciente.CensoCamas;
import com.princetonsa.pdf.CensoCamasPdf;


/**
 *@author Jhony Alexander Duque A.
 *jduque@princetonsa.com 
 */
 
public class CensoCamasAction extends Action
{
	/**
	 * Para manjar los logger de la clase CensoCamasAction
	 */
	Logger logger = Logger.getLogger(CensoCamasAction.class);
	

	
	/*----------------------------------------------------------------------------------
	 * 						METODO EXECUTE DEL ACTION
	 ----------------------------------------------------------------------------------*/
	public ActionForward execute (ActionMapping mapping,
									ActionForm form,
									HttpServletRequest request,
									HttpServletResponse response) throws Exception
									{

		Connection connection = null;
		try{
			if (form instanceof CensoCamasForm) 
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

				//optenemos el valor de la forma.
				CensoCamasForm forma = (CensoCamasForm) form;

				//optenemos el estado que contiene la forma.
				String estado = forma.getEstado();

				logger.info("\n\n***************************************************************************");
				logger.info(" 	           EL ESTADO DE CensoCamasForm ES ====>> "+forma.getEstado());
				logger.info("\n***************************************************************************");

				permisoRol(connection, forma, usuario);
				/*-------------------------------------------------------------------
				 * 					ESTADOS PARA EL CENSO DE CAMAS
			 -------------------------------------------------------------------*/


				/*----------------------------------------------
				 * ESTADO =================>>>>>  NULL
			 ---------------------------------------------*/

				if (estado == null)
				{
					//inicializamos los valores de los HashMap.
					forma.resetCensoCamas();
					//inicializamos los valores de los atributos del pager.
					forma.resetpager();
					//se saca un log con el siguiente texto.
					logger.warn("Estado no Valido dentro del Flujo de Censo de Camas (null)");
					//se asigana un error a mostar por ser un estado invalido
					request.setAttribute("CodigoDescripcionError","errors.estadoInvalido");
					//se cierra la conexion con la BD
					UtilidadBD.cerrarConexion(connection);

					//se retorna el error al forward paginaError.
					return mapping.findForward("paginaError");
				}


				/*----------------------------------------------
				 * ESTADO =================>>>>>  INICIAL
			 ---------------------------------------------*/
				else
					if (estado.equals("inicial"))
					{ 
						forma.resetconsulta();
						//inicializamos los valores de los HashMap.
						forma.resetCensoCamas();
						//inicializamos los valores de los atributos del pager.
						forma.resetpager();

						return this.initBusquedaCensoCamas(connection, forma, mapping, usuario);
					}
					else/*----------------------------------------------
					 * ESTADO =================>>>>>  CENSARCAMAS
					 ---------------------------------------------*/
						if (estado.equals("censarcamas"))
						{
							//estado que hace el censo.
							return this.censarCamas(connection, forma, mapping, usuario,request,response);
							//Utilidades.imprimirArreglo(forma.getEstadoCamaId());
							//return mapping.findForward("principal");
						}
						else /*----------------------------------------------
						 * ESTADO =================>>>>>  REDIRECCION
						 ---------------------------------------------*/
							if (estado.equals("redireccion"))
							{
								UtilidadBD.closeConnection(connection);
								response.sendRedirect(forma.getLinkSiguiente());
								return null;
							}
							else/*----------------------------------------------
							 * ESTADO =================>>>>>  ORDENAR
							 ---------------------------------------------*/
								if (estado.equals("ordenar"))//estado encargado de ordenar el HashMap del censo.
								{
									forma.setCensoCamasMap(this.accionOrdenarMapa(forma.getCensoCamasMap(),forma));				 
									UtilidadBD.closeConnection(connection);
									return mapping.findForward("principal");	

								}

				//****************ESTADOS PARA LOS FILTROS AJAX*******************************************
								else
									if (estado.equals("filtroCentroAtencion"))
									{
										return accionFiltrar(connection,forma,response,usuario);
									}
				//*****************************************************************************************
									else/*------------------------------------------------
									 * ESTADO =================>>>>>  CARGARPACIENTE
									 ------------------------------------------------*/
										if (estado.equals("cargarPaciente"))
										{
											return this.cargarPaciente(connection, forma, mapping, usuario, paciente, request);
										}
										else
											/*------------------------------------------------
											 * ESTADO =================>>>>>  IMPRIMIR
										 ------------------------------------------------*/
											if (estado.equals("generar"))
											{
												return generar(connection, mapping, request, forma, usuario);
											}
											else
												/*------------------------------------------------
												 * ESTADO =================>>>>>  CAMBIOESATADOCAMA
											 ------------------------------------------------*/
												if (estado.equals("cambioestado"))
												{
													return this.cambiarestadocama(connection, Integer.parseInt(forma.getNuevoEstadoCama()), forma, usuario,mapping,request,response);
												}
												else
													/*------------------------------------------------
													 * ESTADO =================>>>>>  ALERTAS
												 ------------------------------------------------*/
													if (estado.equals("alertas"))
													{
														return this.mostrarAlertas(forma, mapping);
													}



				/*-------------------------------------------------------------------
				 * 					FIN ESTADOS PARA EL CENSO DE CAMAS
			 -------------------------------------------------------------------*/



			}
		}catch (Exception e) {
			Log4JManager.error(e);
		}
		finally{
			UtilidadBD.closeConnection(connection);
		}
		return null;
	}
	
	/*-------------------------------------------------------------------------------------------------------------------
	 * 								METODOS PARA EL CENSO DE CAMAS
	 --------------------------------------------------------------------------------------------------------------------*/
	
	
	public ActionForward mostrarAlertas (CensoCamasForm forma,  ActionMapping mapping)
	{
		forma.resetAlertas();
		int numRegistros=Integer.parseInt(forma.getAlertasMap("numRegistros")+"");
		
		logger.info("\n\n *** entro a mostara alertas"); 
		if (Integer.parseInt(forma.getCensoCamasMap("numRegistros")+"")>0 && forma.getCensoCamasMap().containsKey("alertasTotal") && Integer.parseInt(forma.getCensoCamasMap("alertasTotal")+"")>0)
		{
			for (int i=0;i< Integer.parseInt(forma.getCensoCamasMap("numRegistros")+"");i++)
			{
				if (Utilidades.convertirAEntero(forma.getCensoCamasMap("alerta_"+i)+"")>0)
				{
					forma.setAlertasMap(Listado.copyMapOnIndexMap(Listado.copyOnIndexMap(forma.getCensoCamasMap(), i+"", (String[])forma.getCensoCamasMap("INDICES_MAPA")),forma.getAlertasMap(), (String[])forma.getCensoCamasMap("INDICES_MAPA"),numRegistros));
					numRegistros++;
					logger.info("\n entro en "+i);
				}
			}
			logger.info("\n\n ** > el valor del hashmap"+forma.getAlertasMap());
			numRegistros=Integer.parseInt(forma.getAlertasMap("numRegistros")+"");
			forma.setAlertasMap(Listado.ordenarMapa((String [])forma.getCensoCamasMap("INDICES_MAPA"), "nombreestadocama_", "", forma.getAlertasMap(), Integer.parseInt(forma.getAlertasMap("numRegistros")+"")));
			forma.setAlertasMap("numRegistros", numRegistros);
			
		}
		logger.info("\n\n *** salio a mostara alertas");
		return mapping.findForward("alertas");
	}
	
	
	/**
	 * Metodo en cargado de cargar las alertas para el censo de camas
	 */
	public void cargarAlertas (Connection connection, CensoCamasForm forma, UsuarioBasico usuario)
	{
		String fechaOcupacion="";
		String [] datostmp;
		boolean existReser=false,existConSalida=false,existPorRemitir=false;
		forma.setCensoCamasMap("alertasTotal", 0);
		
		//se pregunta si el parametro "minutosLimiteAlertaReserva" esta definido en parametros generales
		existReser=UtilidadCadena.noEsVacio(ValoresPorDefecto.getMinutosLimiteAlertaReserva(usuario.getCodigoInstitucionInt()));
		//se pregunta si el parametro "minutosLimiteAlertaPacienteConSalidaUrgencias" esta definido en parametros generales
		existConSalida=UtilidadCadena.noEsVacio(ValoresPorDefecto.getMinutosLimiteAlertaPacienteConSalidaUrgencias(usuario.getCodigoInstitucionInt()));
		//se pregunta si el parametro "minutosLimiteAlertaPacientePorRemitirUrgencias" esta definido en parametros generales
		existPorRemitir=UtilidadCadena.noEsVacio(ValoresPorDefecto.getMinutosLimiteAlertaPacientePorRemitirUrgencias(usuario.getCodigoInstitucionInt()));
		
		for (int i=0;i<Integer.parseInt(forma.getCensoCamasMap("numRegistros")+"");i++)
		{
			if(!forma.getCensoCamasMap().containsKey("alerta_"+i))
			{
				logger.info("\n******** el estado de la cama es************ "+forma.getCensoCamasMap("estadocama_"+i));
				if(existReser && (forma.getCensoCamasMap("estadocama_"+i)+"").equals(ConstantesBD.codigoEstadoCamaReservada+""))
				{
				logger.info("\n******** 1 ************\n");
					//se consulta la fecha de posible ocupacion y devuelve el siguiente formato DD/MM/YYYY@@@@@HH:MM
					fechaOcupacion=CensoCamas.ConsultaFechaOcupacionReserva(connection, Integer.parseInt(forma.getCensoCamasMap("codigocama_"+i)+""));
					logger.info("\n fecha ocupacion cama reservada --> "+fechaOcupacion);
					//se evalua por seguridad de que la cadena no se encuentre vacia
					if (!fechaOcupacion.equals(""))
					{
						//separamos la fecha de la hora
						datostmp=fechaOcupacion.split(ConstantesBD.separadorSplit);
						int min=0;					
						min=UtilidadFecha.numeroMinutosEntreFechas(datostmp[0], datostmp[1], UtilidadFecha.getFechaActual(), UtilidadFecha.getHoraActual());
						logger.info("\n::::::::::::::::::: (reserva)EL VALOR DE LA DIRENCIA DE FECHAS "+min); 
						logger.info("\n minutos entre fechas "+min+" valor min reserva "+ValoresPorDefecto.getMinutosLimiteAlertaReserva(usuario.getCodigoInstitucionInt()));
						min=(min -Integer.parseInt(ValoresPorDefecto.getMinutosLimiteAlertaReserva(usuario.getCodigoInstitucionInt())));
						//logger.info("\n:::::::::::::::::::EL VALOR DE MIN  "+min);
						
						////////////////////////////////////////////////////////////////////////////////////////////////////
						//se almacena en el mapa la fecha y hora de la reserva
						//Modificado por tarea 17272
						forma.setCensoCamasMap("fechaHoraReserva_"+i, datostmp[0]+" - "+datostmp[1]);
						/////////////////////////////////////////////////////////////////////////////////////////////////////
						
						
						if(min>0)
						{
							forma.setCensoCamasMap("alerta_"+i, min);
							forma.setCensoCamasMap("alertasTotal", Integer.parseInt(forma.getCensoCamasMap("alertasTotal")+"")+1);
						}
						
					}
					
					
				}
				else
					if(existConSalida && (forma.getCensoCamasMap("estadocama_"+i)+"").equals(ConstantesBD.codigoEstadoCamaConSalida+""))
					{
						
						logger.info("\n******** 2 ************\n");
						logger.info("\n codigo cama -->"+forma.getCensoCamasMap("codigocama_"+i));
						fechaOcupacion=CensoCamas.ConsultaFechaOcupacionOtrosEst(connection, Utilidades.convertirAEntero(forma.getCensoCamasMap("cuenta_"+i)+""));
						
						if (!fechaOcupacion.equals(""))
						{
							//separamos la fecha de la hora
							datostmp=fechaOcupacion.split(ConstantesBD.separadorSplit);
							int min=0;		
							
							min=UtilidadFecha.numeroMinutosEntreFechas(datostmp[0], datostmp[1], UtilidadFecha.getFechaActual(), UtilidadFecha.getHoraActual());
							if (forma.getCensoCamasMap("viaingreso_"+i).equals(ConstantesBD.codigoViaIngresoUrgencias))
								min=(min -Integer.parseInt(ValoresPorDefecto.getMinutosLimiteAlertaPacienteConSalidaUrgencias(usuario.getCodigoInstitucionInt())));
							else
								if (forma.getCensoCamasMap("viaingreso_"+i).equals(ConstantesBD.codigoViaIngresoHospitalizacion))
									min=(min -Integer.parseInt(ValoresPorDefecto.getMinutosLimiteAlertaPacienteConSalidaHospitalizacion(usuario.getCodigoInstitucionInt())));
							if(min>0)
							{
								forma.setCensoCamasMap("alerta_"+i, min);
								forma.setCensoCamasMap("alertasTotal", Integer.parseInt(forma.getCensoCamasMap("alertasTotal")+"")+1);
							}
							
						}
						
					}
					else
						if(existPorRemitir && (forma.getCensoCamasMap("estadocama_"+i)+"").equals(ConstantesBD.codigoEstadoCamaPendientePorRemitir+""))
						{
							logger.info("\n******** 3 ************\n");
							fechaOcupacion=CensoCamas.ConsultaFechaOcupacionOtrosEst(connection, Utilidades.convertirAEntero(forma.getCensoCamasMap("cuenta_"+i)+""));
							
							logger.info("\n******** fehca ocupacion  ************ --> "+fechaOcupacion);
							if (!fechaOcupacion.equals(""))
							{
								//separamos la fecha de la hora
								datostmp=fechaOcupacion.split(ConstantesBD.separadorSplit);
								int min=0;		
								logger.info("\n minutos --> "+UtilidadFecha.numeroMinutosEntreFechas(datostmp[0], datostmp[1], UtilidadFecha.getFechaActual(), UtilidadFecha.getHoraActual()));
								
								min=UtilidadFecha.numeroMinutosEntreFechas(datostmp[0], datostmp[1], UtilidadFecha.getFechaActual(), UtilidadFecha.getHoraActual());
								logger.info("\n minutos entre fechas "+min+" valor min urgencias -->"+ValoresPorDefecto.getMinutosLimiteAlertaPacientePorRemitirUrgencias(usuario.getCodigoInstitucionInt())+" valor min hospitalizacion "+ValoresPorDefecto.getMinutosLimiteAlertaPacientePorRemitirHospitalizacion(usuario.getCodigoInstitucionInt()));
								if (forma.getCensoCamasMap("viaingreso_"+i).equals(ConstantesBD.codigoViaIngresoUrgencias))
									min=(min -Integer.parseInt(ValoresPorDefecto.getMinutosLimiteAlertaPacientePorRemitirUrgencias(usuario.getCodigoInstitucionInt())));
								else           
									if (forma.getCensoCamasMap("viaingreso_"+i).equals(ConstantesBD.codigoViaIngresoHospitalizacion))
										min=(min -Integer.parseInt(ValoresPorDefecto.getMinutosLimiteAlertaPacientePorRemitirHospitalizacion(usuario.getCodigoInstitucionInt())));
								if(min>0)
								{
									forma.setCensoCamasMap("alerta_"+i, min);
									forma.setCensoCamasMap("alertasTotal", Integer.parseInt(forma.getCensoCamasMap("alertasTotal")+"")+1);
								}
							}
						}
						else
							forma.setCensoCamasMap("alerta_"+i, 0);
			}
		}
	}
	
	
	
	/**
	 * Metodo encargado de cambiar el estado de la cama.
	 */
	public ActionForward cambiarestadocama (Connection connection, int estado, CensoCamasForm forma, UsuarioBasico usuario, ActionMapping mapping, HttpServletRequest request, HttpServletResponse response)
	{
		
		logger.info("\n\n ********CAMBIAR ESDO DE LA CAMA *************");
		logger.info("\n NUEVO ESTADO ==> "+forma.getNuevoEstadoCama());
		logger.info("\n NUEVO ESTADO ==> "+forma.getCensoCamasMap("codigocama_"+forma.getIndexCensoCamasMap()));
		int operacionInt=0;
		TrasladoCamas trasladoCamas = new TrasladoCamas();
		try 
		{
			operacionInt=trasladoCamas.modificarEstadoCama(connection, estado, Integer.parseInt(forma.getCensoCamasMap("codigocama_"+forma.getIndexCensoCamasMap())+""), usuario.getCodigoInstitucionInt());	
			
			if (operacionInt>0)
				UtilidadesManejoPaciente.insertarLogCambioEstadoCama(connection,  Integer.parseInt(forma.getCensoCamasMap("codigocama_"+forma.getIndexCensoCamasMap())+""), Integer.parseInt(forma.getCensoCamasMap("estadocama_"+forma.getIndexCensoCamasMap())+""), estado, UtilidadFecha.getFechaActual(), UtilidadFecha.getHoraActual(), usuario.getLoginUsuario(), forma.getCensoCamasMap("cuenta_"+forma.getIndexCensoCamasMap())+"", usuario.getCodigoInstitucionInt(), usuario.getCodigoCentroAtencion());
		} catch (Exception e) {
			logger.error("Problema Cambiando el estado de la cama"+e);
		}
		
		return this.censarCamas(connection, forma, mapping, usuario, request, response);
		
	}
	
	
	
	/**
	 * Metodo en cargado de verificar si se tiene el rol
	 * para poder habilitar unas funcionalidades.
	 * @param connection
	 * @param forma
	 * @param usuario
	 */
	public void permisoRol(Connection  connection,CensoCamasForm forma,UsuarioBasico usuario)
	{
		//se verifica si tiene permiso para poder imprimir.
		forma.setPermisoImpresion(Utilidades.tieneRolFuncionalidad(connection, usuario.getLoginUsuario(),635));
		//se verifica si tiene el permiso para llamar la funcionalidad de traslado de camas.
		forma.setAccesoTrasladoCamas(Utilidades.tieneRolFuncionalidad(connection, usuario.getLoginUsuario(), 273));
		//se verifica si tiene el permiso para llamar la funcionalidad de reserva de camas.
		forma.setAccesoReservaCamas(Utilidades.tieneRolFuncionalidad(connection, usuario.getLoginUsuario(), 594));
		//se verifica si tiene el permiso para cambiar el estado de la cama a pendiente por remitir
		forma.setCambioPendientePorRemitir(Utilidades.tieneRolFuncionalidad(connection, usuario.getLoginUsuario(), 654));
		//se verifica si tiene el permiso para cambiar el estado de la cama a pendiente por trasladar.
		forma.setCambioPendientePorTrasladar(Utilidades.tieneRolFuncionalidad(connection, usuario.getLoginUsuario(), 653));
		//se verifica si tiene el permiso para llamar a la funcionalidad consultar liberar camas reservadas
		forma.setAccesoLiberCamRes(Utilidades.tieneRolFuncionalidad(connection, usuario.getLoginUsuario(),639));
		//para poder dar permiso de cambiar el estado de la cama a ocupada, es necesario que el usuario 
		//tenga uno de estos dos permisos (CambioPendientePorRemitir -- CambioPendientePorTrasladar);
		//de ser asi se le da el permiso de poder pasar la cama a estado ocupada.
		if (forma.isCambioPendientePorRemitir() || forma.isCambioPendientePorTrasladar())
			forma.setCambioOcupada(true);
		/*
		logger.info("\n\n **************************** ROLES *******************");
		logger.info("\n *** imprimir ==> "+forma.isPermisoImpresion());
		logger.info("\n *** traslado de camas ==> "+forma.isAccesoTrasladoCamas());
		logger.info("\n *** reserva de camas ==> "+forma.isAccesoReservaCamas());
		logger.info("\n *** Remitir  ==> "+forma.isCambioPendientePorRemitir());
		logger.info("\n *** trasladar ==> "+forma.isCambioPendientePorTrasladar());
		logger.info("\n *** Ocupada ==> "+forma.isCambioOcupada());
		logger.info("\n *** liberar ==> "+forma.isAccesoLiberCamRes());
		logger.info("\n\n **************************** FIN ROLES *******************\n\n");
		*/
	}
	
	
	/**
	 * Metodo encargado de cargar el paciente en sesion.
	 * @param connection
	 * @param forma
	 * @param mapping
	 * @param usuario
	 * @param paciente
	 * @param request
	 */
	public ActionForward cargarPaciente (Connection connection,CensoCamasForm forma, ActionMapping mapping,UsuarioBasico usuario,PersonaBasica paciente ,HttpServletRequest request)
	{
		
		paciente.setCodigoPersona(Integer.parseInt(forma.getCensoCamasMap().get("codigopersona_"+forma.getIndexCensoCamasMap()).toString()));
		
		UtilidadesManejoPaciente.cargarPaciente(connection, usuario, paciente, request);
		
		return mapping.findForward("principal");
	}
	
	
	
	
	/**
	 * Metodo encargado de inicializar los critrios de busqueda.
	 * @param connection
	 * @param forma
	 * @param mapping
	 * @param usuario
	 */
	public ActionForward initBusquedaCensoCamas (Connection connection,CensoCamasForm forma,ActionMapping mapping,UsuarioBasico usuario)
	{
		//se cargan los valores de los criterios de busqueda.
		forma.initBusquedaCensoCamas(connection, usuario.getCodigoInstitucionInt(),usuario.getCodigoCentroAtencion());
		forma.setCentroAtencionId(usuario.getCodigoCentroAtencion()+"");
		/*
		logger.info("centro de atencion es"+forma.getCentroAtencionId());
		logger.info("\n\n************************************************************");
		
		logger.info("centro atencion MAp"+forma.getCentrosAtencionMap().get(1)	);
		logger.info("\ncentro costo MAp"+forma.getCentroCostoMap().get(1)	);
		logger.info("\nconvenios "+forma.getConveniosMap().get(1)	);
		logger.info("\npisos MAp "+forma.getPisosMap().get(1)	);
		logger.info("\nestado MAp"+forma.getEstadosCamaMap().get(1)	);*/
		
		UtilidadBD.closeConnection(connection);
		
		
		return mapping.findForward("principal");

	}
	
	
	/**
	 * Metodo Que ordena el mapa.
	 * @param mapaOrdenar
	 * @param forma
	 * @return
	 */
	public HashMap accionOrdenarMapa (HashMap mapaOrdenar, CensoCamasForm forma)
	{
		HashMap tmp = new HashMap ();
		tmp=((HashMap)mapaOrdenar.clone());
		String tipoRompe=forma.getTipoRompimiento()+"_";
		String [] indices = ((String[])mapaOrdenar.get("INDICES_MAPA"));
		int numReg = Integer.parseInt(mapaOrdenar.get("numRegistros")+"");
		/*
		logger.info("\n\n******************************************************************");
		logger.info("hashmap original "+mapaOrdenar);
		logger.info("\n\n******************************************************************");
		*/		
		mapaOrdenar=Listado.ordenarMapaRompimiento(indices, forma.getPatronOrdenar(), forma.getUltimoPatron(), mapaOrdenar, tipoRompe);
		/*
		  logger.info("\n\n******************************************************************");
		logger.info("hashmap despues de ordenado "+mapaOrdenar);
	//	logger.info("hashmap tmp "+tmp);
		logger.info("\n\n");
		logger.info("\n\n******************************************************************");
		*/
		
		
		forma.setUltimoPatron(forma.getPatronOrdenar());
		mapaOrdenar.put("numRegistros",numReg+"");
		mapaOrdenar.put("INDICES_MAPA",indices);	
		//logger.info("sale de ordenar ");
	
		//se cargan en el HashMap los valores que no tienen sub indice
		//ya que al momento de ordenarlos se pierden.
		mapaOrdenar.put("disponible", tmp.get("disponible"));
		mapaOrdenar.put("ocupada", tmp.get("ocupada"));
		mapaOrdenar.put("desinfeccion", tmp.get("desinfeccion"));
		mapaOrdenar.put("mantenimiento", tmp.get("mantenimiento"));
		mapaOrdenar.put("fueradeservicio", tmp.get("fueradeservicio"));
		mapaOrdenar.put("reservada", tmp.get("reservada"));
		mapaOrdenar.put("remitir",tmp.get("remitir"));
		mapaOrdenar.put("trasladar",tmp.get("trasladar"));
		mapaOrdenar.put("consalida", tmp.get("consalida"));
		mapaOrdenar.put("total", tmp.get("total"));
		
		mapaOrdenar.put("disponibleporcent", tmp.get("disponibleporcent"));
		mapaOrdenar.put("ocupadaporcent", tmp.get("ocupadaporcent"));
		mapaOrdenar.put("desinfeccionporcent", tmp.get("desinfeccionporcent"));
		mapaOrdenar.put("mantenimientoporcent", tmp.get("mantenimientoporcent"));
		mapaOrdenar.put("fueradeservicioporcent", tmp.get("fueradeservicioporcent"));
		mapaOrdenar.put("reservadaporcent", tmp.get("reservadaporcent"));
		
		mapaOrdenar.put("remitirporcent", tmp.get("remitirporcent"));
		mapaOrdenar.put("trasladarporcent", tmp.get("trasladarporcent"));
		mapaOrdenar.put("consalidaporcent", tmp.get("consalidaporcent"));
		
		mapaOrdenar.put("totalporcent", tmp.get("totalporcent"));
		
		mapaOrdenar.put("tipoRompimiento", tmp.get("tipoRompimiento"));
		
		mapaOrdenar.put("fechaconsulta", tmp.get("fechaconsulta"));
		mapaOrdenar.put("horaconsulta", tmp.get("horaconsulta"));
		mapaOrdenar.put("alertasTotal", tmp.get("alertasTotal"));
		
		
		return mapaOrdenar;
	}
	
	/**
	 * Metodo encargado de darle el formato a los
	 * parametros para hacer la busqueda.
	 * @param connection
	 * @param forma
	 * @param mapping
	 * @param usuario
	 * @return
	 */
	public ActionForward censarCamas (Connection connection,CensoCamasForm forma,ActionMapping mapping,UsuarioBasico usuario, HttpServletRequest request, HttpServletResponse response)
	{
		forma.resetconsulta();
		//se inicializa el pager
		forma.resetpager();
		forma.resetDescarga();
		HashMap parametros = new HashMap ();
		
		/*----------------------------------------------------
		 * aqui se organizan los criterios de busqueda para 
		 * enviarlos al metodo consultaCenso.
		 ----------------------------------------------------*/		
		//este parametros es obligatorio, por tal motivo va sin condicional
		parametros.put("institucion", usuario.getCodigoInstitucionInt());
		
		// estos parametros son opcionales por eso se pregunta si estan llenos 
		//antes de enviarlos a la consulta para filtrar por ellos.
		if(!forma.getConvenioId().equals(""))
			parametros.put("convenio", Integer.parseInt(forma.getConvenioId().toString()));
		if (!forma.getCentroAtencionId().equals(""))
			parametros.put("centroatencion", Integer.parseInt(forma.getCentroAtencionId().toString()));
		if (!forma.getCentroCostoId().equals(""))
			parametros.put("centrocosto", Integer.parseInt(forma.getCentroCostoId().toString()));
		if (!forma.getPisoId().equals(""))
			parametros.put("piso", Integer.parseInt(forma.getPisoId().toString()));
		//Incluir en resumen camas asignables en Admon
		parametros.put("incluirAsignableAdmin", forma.getIncluirAsignableAdmin());
		//////////////////////////////////////////////////////////////////////////////
		String estados = "";
			int numReg = forma.getEstadoCamaId().length;
			if (numReg==1)
			{
				if (!(forma.getEstadoCamaId()[0]).equals(""))
					parametros.put("estado","'"+forma.getEstadoCamaId()[0]+"'");
			}
			else
			{
				if (numReg>1)
					for (int i=0;i<numReg;i++)
						if (i==0)
							estados="'"+forma.getEstadoCamaId()[i]+"'";
						else
							estados+=",'"+forma.getEstadoCamaId()[i]+"'";
				
				parametros.put("estado", estados);
			}
			
		//if (!forma.getEstadoCamaId().equals(""))
			//parametros.put("estado", Integer.parseInt(forma.getEstadoCamaId().toString()));
			//logger.info("llegue hasta censarCamas "+parametros);
		/////////////////////////////////////////////////////////////////////////
			
		if (!forma.getTipoRompimiento().equals(""))
			parametros.put("tiporompimiento", forma.getTipoRompimiento());
			//se realiza la consulta del censo
			forma.setCensoCamasMap(CensoCamas.consultaCenso(connection, parametros));
			//se cierra la conexion a la BD.
			
			//logger.info("el hashmap de censocamas ==> "+forma.getCensoCamasMap());
							
			//se ingresa fecha y hora de la consulta.
			forma.setCensoCamasMap("fechaconsulta", UtilidadFecha.getFechaActual());
			forma.setCensoCamasMap("horaconsulta", UtilidadFecha.getHoraActual());
			
			// hace el calculo en porcentaje de la cantidad de camas en cada estado
			if (Integer.parseInt(forma.getCensoCamasMap("numRegistros").toString())!=0 )
			{
				/*
				logger.info("\n\n*********************************************************************");
				logger.info("disponible ==> "+forma.getCensoCamasMap().get("disponible").toString());
				logger.info("ocupada ==> "+forma.getCensoCamasMap().get("ocupada").toString());
				logger.info("desinfeccion ==> "+forma.getCensoCamasMap().get("desinfeccion").toString());
				logger.info("mantenimiento ==> "+forma.getCensoCamasMap().get("mantenimiento").toString());
				logger.info("fueradeservicio ==> "+forma.getCensoCamasMap().get("fueradeservicio").toString());
				logger.info("reservada ==> "+forma.getCensoCamasMap().get("reservada").toString());
				logger.info("total ==> "+forma.getCensoCamasMap("total").toString());
				logger.info("\n*********************************************************************\n\n\n");
				*/
				
				int totalCamas = Utilidades.obtenerNumeroCamas(connection, usuario.getCodigoInstitucion(), forma.getCentroAtencionId());
				forma.setCensoCamasMap("total",totalCamas);
				
				forma.setCensoCamasMap("disponibleporcent",UtilidadTexto.formatearValores((((Float.parseFloat(forma.getCensoCamasMap("disponible").toString()))/totalCamas)*100),"0.00")+" %");
				forma.setCensoCamasMap("ocupadaporcent",UtilidadTexto.formatearValores((((Float.parseFloat(forma.getCensoCamasMap("ocupada").toString()))/totalCamas)*100),"0.00")+" %");
				forma.setCensoCamasMap("desinfeccionporcent",UtilidadTexto.formatearValores((((Float.parseFloat(forma.getCensoCamasMap("desinfeccion").toString()))/totalCamas)*100),"0.00")+" %");
				forma.setCensoCamasMap("mantenimientoporcent",UtilidadTexto.formatearValores((((Float.parseFloat(forma.getCensoCamasMap("mantenimiento").toString()))/totalCamas)*100),"0.00")+" %");
				forma.setCensoCamasMap("fueradeservicioporcent",UtilidadTexto.formatearValores((((Float.parseFloat(forma.getCensoCamasMap("fueradeservicio").toString()))/totalCamas)*100),"0.00")+" %");
				forma.setCensoCamasMap("reservadaporcent",UtilidadTexto.formatearValores((((Float.parseFloat(forma.getCensoCamasMap("reservada").toString()))/totalCamas)*100),"0.00")+" %");
				
				forma.setCensoCamasMap("remitirporcent",UtilidadTexto.formatearValores((((Float.parseFloat(forma.getCensoCamasMap("remitir").toString()))/totalCamas)*100),"0.00")+" %");
				forma.setCensoCamasMap("trasladarporcent",UtilidadTexto.formatearValores((((Float.parseFloat(forma.getCensoCamasMap("trasladar").toString()))/totalCamas)*100),"0.00")+" %");
				forma.setCensoCamasMap("consalidaporcent",UtilidadTexto.formatearValores((((Float.parseFloat(forma.getCensoCamasMap("consalida").toString()))/totalCamas)*100),"0.00")+" %");
				
				
				forma.setCensoCamasMap("totalporcent",UtilidadTexto.formatearValores((((Float.parseFloat(forma.getCensoCamasMap("total").toString()))/(Float.parseFloat(forma.getCensoCamasMap("total").toString())))*100),"0.00")+" %");
				
				
				/*
				logger.info("\n\n*********************************************************************");
				logger.info("disponibleporcent ==> "+forma.getCensoCamasMap("disponibleporcent").toString());
				logger.info("ocupadaporcent ==> "+forma.getCensoCamasMap("ocupadaporcent").toString());
				logger.info("desinfeccionporcent ==> "+forma.getCensoCamasMap("desinfeccionporcent").toString());
				logger.info("mantenimientoporcent ==> "+forma.getCensoCamasMap("mantenimientoporcent").toString());
				logger.info("fueradeservicioporcent ==> "+forma.getCensoCamasMap("fueradeservicioporcent").toString());
				logger.info("reservadaporcent ==> "+forma.getCensoCamasMap("reservadaporcent").toString());
				logger.info("\n*********************************************************************\n\n\n");
				*/
				this.cargarAlertas(connection, forma, usuario);
			}
			
			UtilidadBD.closeConnection(connection);
			
			
			//logger.info("\n los datos de camas "+forma.getCensoCamasMap());
			
			
			
			return UtilidadSesion.redireccionar(forma.getLinkSiguiente(),ValoresPorDefecto.getMaxPageItemsInt(usuario.getCodigoInstitucionInt()), Integer.parseInt(forma.getCensoCamasMap("numRegistros")+""), response, request, "censoCamas.jsp", false);
	}
	
	
	/**
	 * Método que realiza el filtro de los Centros de Costo y Pisos
	 * por Centro de Atencion e interactua con el ajax.
	 * @param connection
	 * @param forma
	 * @param response
	 * @param usuario
	 * @return
	 */
	private ActionForward accionFiltrar(Connection connection, CensoCamasForm forma, HttpServletResponse response,UsuarioBasico usuario) 
	{
		String resultado = "<respuesta>";
		String centroAtencionId = "";
		HashMap parametros = new HashMap();
		ArrayList<HashMap<String, Object>> arregloAux = new ArrayList<HashMap<String,Object>>();
		ArrayList<HashMap<String, Object>> arregloAuxP = new ArrayList<HashMap<String,Object>>();
		
		//Se filtran las ciudades segun estado
		if(forma.getEstado().equals("filtroCentroAtencion"))
		{
			centroAtencionId = forma.getCentroAtencionId();
			parametros.put("institucion", usuario.getCodigoInstitucion());
			//logger.info("calor del centro de atencion "+centroAtencionId);
			
			//se pregunta si el centro de atencion viene vacio
			//para si llenar los arrayList con toda la informacion de la consulta
			if (centroAtencionId.equals(""))
			{
				//logger.info("valor del centro de atencion ifual"+centroAtencionId);
				
				//esta consulta nos devuelve todos los centros de costo, esto es porque 
				//en el ultimo parametro lleva el Cero.
				/**
				 * MT 6998
				 * @author javrammo
				 * No estaba filtrando por los centros de costo que manejan cama
				 */
				//arregloAux=UtilidadesManejoPaciente.obtenerCentrosCosto(connection, Integer.parseInt(parametros.get("institucion").toString()), "", true, 0);
				arregloAux=UtilidadesManejoPaciente.obtenerCentrosCosto(connection, Integer.parseInt(parametros.get("institucion").toString()), "", true, 0,String.valueOf(true));
				/**
				 * Fin MT 6998
				 */
				//aqui tambien se cargan todos los pisos porque el unico parametro que 
				//lleva es la institucion.
				arregloAuxP=UtilidadesManejoPaciente.obtenerPisos(connection, parametros);
				//logger.info("valor de centros costo "+arregloAux);
				//logger.info("\nvalor de pisos "+arregloAuxP);
				
				//Se carga toda la informacion de centros de costo en formato 
				//XML para armar la respuesta que recibira el ajax.
				for(int i=0;i<arregloAux.size();i++)
				{
					HashMap elemento = (HashMap)arregloAux.get(i);
				    //se formatea a modo de XML
					resultado += "<centrocosto>" +
							"<codigo-centrocosto>"+elemento.get("codigo")+"</codigo-centrocosto>"+
							/**
							 * MT 6998
							 * @author javrammo
							 * No estaba mostrando el centro de atencion como lo hace en la JSP inicial
							 */
							"<nombre-centrocosto>"+elemento.get("nomcentrocosto")+" ("+elemento.get("nomcentroatencion")+")"+"</nombre-centrocosto>"+						
						 "</centrocosto>";
				}
				//Se carga toda la informacion de los pisos en formato 
				//XML para armar la respuesta que recibira el ajax.
				for(int j=0;j<arregloAuxP.size();j++)
				{
					HashMap elemento = (HashMap)arregloAuxP.get(j);
					//se formatea a modo de XML
					resultado += "<piso>" +
						"<codigo-piso>"+elemento.get("codigo")+"</codigo-piso>"+
						"<nombre-piso>"+elemento.get("nombre")+'('+ elemento.get("nombrecentroatencion")+')'+"</nombre-piso>"+
					
					 "</piso>";
				}
				
							
			}
			else
				//se pregunta si el centro de atencion viene lleno
				//de ser asi se filtran las consulta segun el id del
				//centro de atencion.
				if (!centroAtencionId.equals(""))
				{
					//logger.info("valor del centro de atencion diferente "+centroAtencionId);
					
					//se ingresa el criterio de busqueda centro de atencion al hashmap parametros
					parametros.put("centroatencion", centroAtencionId);
					
					//se realizan las busquedas de centros de costo y de pisos
					//que pertenescan a ese centro de atencion
					/**
					 * MT 6998
					 * @author javrammo
					 * No estaba filtrando por los centros de costo que manejan cama
					 */
					//arregloAux=UtilidadesManejoPaciente.obtenerCentrosCosto(connection, usuario.getCodigoInstitucionInt(), "", true, Integer.parseInt(parametros.get("centroatencion").toString()));
					arregloAux=UtilidadesManejoPaciente.obtenerCentrosCosto(connection, usuario.getCodigoInstitucionInt(), "", true, Integer.parseInt(parametros.get("centroatencion").toString()),String.valueOf(true));
					/**
					 * Fin MT 6998
					 */
					arregloAuxP=UtilidadesManejoPaciente.obtenerPisos(connection, parametros);
						
					//Se carga toda la informacion de centros de costo filtrado por el centro de atencion
					//en formato XML para armar la respuesta que recibira el ajax.
					for(int i=0;i<arregloAux.size();i++)
					{
						HashMap elemento = (HashMap)arregloAux.get(i);
						//se verifica nuevamente si el centro de atencion coincide
						if(elemento.get("codcentroatencion").toString().equals(centroAtencionId))
							//se formatea a modo de XML
							resultado += "<centrocosto>" +
								"<codigo-centrocosto>"+elemento.get("codigo")+"</codigo-centrocosto>"+
								/**
								 * MT 6998
								 * @author javrammo
								 * No estaba mostrando el centro de atencion como lo hace en la JSP inicial
								 */
								"<nombre-centrocosto>"+elemento.get("nomcentrocosto")+" ("+elemento.get("nomcentroatencion")+")"+"</nombre-centrocosto>"+							
							 "</centrocosto>";
					}
					//Se carga toda la informacion de los pisos filtrado por el centro de atencion
					//en formato XML para armar la respuesta que recibira el ajax.
					for(int j=0;j<arregloAuxP.size();j++)
					{
						HashMap elemento = (HashMap)arregloAuxP.get(j);
						//se verifica nuevamente si el centro de atencion coincide
						if(elemento.get("idcentroatencion").toString().equals(centroAtencionId))
							//se formatea a modo de XML
							resultado += "<piso>" +
							"<codigo-piso>"+elemento.get("codigo")+"</codigo-piso>"+
							"<nombre-piso>"+elemento.get("nombre")+"</nombre-piso>"+
						
						 "</piso>";
					}
				}
		
		}
		
		resultado += "</respuesta>";
		
		UtilidadBD.closeConnection(connection);
		//**********SE GENERA RESPUESTA PARA AJAX EN XML**********************************************
		try
		{   
			//se indica de que tipo es la respuesta
			//y se envia la respuesta hacia la jsp
			response.setContentType("text/xml");
			response.setHeader("Cache-Control", "no-cache");
	        response.getWriter().write(resultado);
	        //logger.info("ajax "+resultado);
		}
		catch(IOException e)
		{
			logger.error("Error al enviar respuesta AJAX en accionFiltrar: "+e);
		}
		return null;
	}

	
public ActionForward generar (Connection connection,ActionMapping mapping,HttpServletRequest request,CensoCamasForm forma,UsuarioBasico usuario)
{
	if (forma.getTipoSalida().equals(ConstantesIntegridadDominio.acronimoTipoSalidaImpresion))
		return accionImprimir(mapping, request, connection, forma, usuario);
	else
	{
		crearCvs(connection, forma, usuario);
		return mapping.findForward("principal");
	}
}
	
	
	private ActionForward accionImprimir(ActionMapping mapping, HttpServletRequest request, Connection connection, CensoCamasForm forma, UsuarioBasico usuario)
	{
		
		String nombreArchivo;

		Random r= new Random();
		nombreArchivo= "/censoCamas"+forma.getTipoReporte()+ r.nextInt() + ".pdf";
		
		CensoCamasPdf.imprimir(connection, ValoresPorDefecto.getFilePath()+nombreArchivo, forma, usuario, request);


		UtilidadBD.closeConnection(connection);
		
	
			request.setAttribute("nombreArchivo",nombreArchivo);
			request.setAttribute("nombreVentana", "Censo de Camas");
			return mapping.findForward("abrirPdf");
		
	}
	
	
	/**
	 * metodo encargado de organoizar la informacion para mostrar el archivo plano
	 * @param connection
	 * @param forma
	 * @param request
	 * @param usuario
	 * @return
	 */
	private StringBuffer generarTxt (Connection connection, CensoCamasForm forma, UsuarioBasico usuario)
	{
		StringBuffer plano = new StringBuffer();
		
		int numRegistros = Integer.parseInt(forma.getCensoCamasMap("numRegistros").toString());
		Vector cambios = new Vector ();
		cambios=Listado.detectarCambioRompimiento(forma.getCensoCamasMap(), numRegistros, forma.getTipoRompimiento()+"_");
	
		numRegistros=numRegistros+cambios.size();
		
		String tituloDocumentos="";
		if (forma.getTipoReporte().toString().equals("resumido"))
			tituloDocumentos="CENSO DE CAMAS RESUMIDO  "+forma.getCensoCamasMap("fechaconsulta").toString()+" "+forma.getCensoCamasMap("horaconsulta");
		else 
			tituloDocumentos="CENSO DE CAMAS DETALLADO  "+forma.getCensoCamasMap("fechaconsulta").toString()+" "+forma.getCensoCamasMap("horaconsulta");
		CensoCamasPdf censo = new  CensoCamasPdf();
		
		String criterios = "";
		criterios=censo.cargarCriteriosMostrar(connection, forma, usuario,false);
		
		plano.append("Reporte: "+tituloDocumentos+" \n");
		
		
		plano.append(criterios+" \n");
		      
        //aqui se crea la seccion donde se va a mostrar los datos del reporte
        if (forma.getTipoReporte().toString().equals("resumido"))
        {
        	
        	int numReg=0,init=0,cant=0;
    		//logger.info("\n\n el valor de cambios es "+cambios.size());
    		//logger.info("el mapa es "+forma.getCensoCamasMap());
        	
        	plano.append("PISO, ");
		    plano.append("HABITACIÓN, ");
		    plano.append("TIPO HABITACIÓN, ");
		    plano.append("CAMA, ");
	    	plano.append("ESTADO CAMA, ");
	    	plano.append("TIPO USUARIO, ");
	    	plano.append("SEXO, ");
	    	plano.append("RESTRICCIÓN, ");
	    	plano.append("CONVENIO, ");		    	
		    plano.append("CENTRO COSTO ");
		    plano.append("\n");
        	
        	for (int k=0;k<cambios.size();k++)
        	{	
	        	//	logger.info("\n el valor de cambios en k "+k+" ==> "+cambios.get(k));
	        		if (numReg==0)
	        		{
	        			cant=Integer.parseInt(cambios.get(k)+"");
	        			numReg=(cant+1);
	        		}
	        			
	        		else
	        		{
	        			init=numReg;
	        			cant=Integer.parseInt(cambios.get(k)+"")-Integer.parseInt(cambios.get(k-1)+"");
	        			numReg=numReg+cant;
	        		}
	        					 
			  //se valida para saber si el informe lleva este campo o no		    
			    
			    for (int i=init;i<numReg;i++)
			    {		
			    		//se valida para saber si el informe lleva este campo o no
			    	if (forma.getCensoCamasMap("cama_"+i)!=null)
			    	{
			    		plano.append(forma.getCensoCamasMap("nombrepiso_"+i).toString().toLowerCase()+", ");			    				
			    		plano.append(forma.getCensoCamasMap("nombrehabitacion_"+i).toString().toLowerCase()+", ");
			    		plano.append(forma.getCensoCamasMap("nombretipohabitacion_"+i).toString().toLowerCase()+", ");
			    		plano.append(forma.getCensoCamasMap("cama_"+i).toString().toLowerCase()+", ");
					  	plano.append(forma.getCensoCamasMap("nombreestadocama_"+i).toString().toLowerCase()+", ");
				    	plano.append(forma.getCensoCamasMap("tipousuariocama_"+i).toString().toLowerCase()+", ");
				    	plano.append(forma.getCensoCamasMap("sexocama_"+i).toString().toLowerCase()+", ");
				    	plano.append(forma.getCensoCamasMap("restriccioncama_"+i).toString()+", ");
			    		plano.append(forma.getCensoCamasMap("convenio_"+i).toString().toLowerCase()+", ");
					    plano.append(forma.getCensoCamasMap("nombrecentrocosto_"+i).toString().toLowerCase()+" ");
					    
					    plano.append("\n");
			    	}
			    }
        	}
        }
        else
        	if (forma.getTipoReporte().toString().equals("detallado"))
        	{
        		int numReg=0,init=0,cant=0;
        		//logger.info("\n\n el valor de cambios es "+cambios.size());
        		//logger.info("el mapa es "+forma.getCensoCamasMap());
        		
        		plano.append("PISO, ");
			    plano.append("CAMA, ");
		    	plano.append("ESTADO CAMA, ");
			    plano.append("PACIENTE, ");
			    plano.append("ID, ");
			    plano.append("DIAGNOSTICO, ");
			    plano.append("ING, ");
			    plano.append("H.C, ");
			    plano.append("EDAD, ");
			    plano.append("S, ");
			    plano.append("FECHA ING, ");
			    plano.append("D.EST, ");
			    plano.append("CONVENIO, ");
		    	plano.append("C.CTO ");
			    plano.append("\n");
        		
        		
	        	for (int k=0;k<cambios.size();k++)
	        	{	
	        		//logger.info("\n el valor de cambios en k "+k+" ==> "+cambios.get(k));
	        		if (numReg==0)
	        		{
	        			cant=Integer.parseInt(cambios.get(k)+"");
	        			numReg=(cant+1);
	        		}
	        			
	        		else
	        		{
	        			init=numReg;
	        			cant=Integer.parseInt(cambios.get(k)+"")-Integer.parseInt(cambios.get(k-1)+"");
	        			numReg=numReg+cant;
	        		}		    
				    
				    for (int i=init;i<numReg;i++)
				    {
				    	//logger.info("\n\n ****el valor de i es ==> "+i);
				    	//se cargan todos los datos del reporte detallado.
				    	//se valida para saber si el informe lleva este campo o no
				    	if (forma.getCensoCamasMap("cama_"+i)!=null)
				    	{
				    		plano.append(forma.getCensoCamasMap("nombrepiso_"+i).toString().toLowerCase()+", ");				    		
				    		plano.append( "C."+forma.getCensoCamasMap("cama_"+i).toString().toLowerCase()+" H."+forma.getCensoCamasMap("codhabitmostar_"+i).toString().toLowerCase()+", ");
							plano.append(forma.getCensoCamasMap("nombreestadocama_"+i).toString().toLowerCase()+", ");					    
				    		plano.append(forma.getCensoCamasMap("nombrepac_"+i).toString().toLowerCase()+", ");				    		 
						    plano.append(forma.getCensoCamasMap("tipoidentificacionpac_"+i).toString().toLowerCase()+" "+forma.getCensoCamasMap("identificacionpac_"+i).toString()+", ");
						   	if(!UtilidadTexto.isEmpty(forma.getCensoCamasMap("diagnostico_"+i)+""))
						   		plano.append(forma.getCensoCamasMap("diagnostico_"+i).toString().toLowerCase().replace(",","")+", ");
						    plano.append(forma.getCensoCamasMap("consecutivoingreso_"+i).toString().toLowerCase()+", ");
						    plano.append(forma.getCensoCamasMap("historiaclinica_"+i).toString().toLowerCase()+", ");
						    plano.append(forma.getCensoCamasMap("edadpac_"+i).toString().toLowerCase()+", ");

						    if (!forma.getCensoCamasMap("nombresexopac_"+i).toString().equals(""))
						    	plano.append(forma.getCensoCamasMap("nombresexopac_"+i).toString().substring(0,1)+", ");
						    else
						    	plano.append(forma.getCensoCamasMap("nombresexopac_"+i).toString()+", ");

						   	plano.append(forma.getCensoCamasMap("fecha_"+i).toString()+" "+forma.getCensoCamasMap("hora_"+i).toString()+", ");

							plano.append(forma.getCensoCamasMap("diasestancia_"+i).toString()+", ");
						   	plano.append(forma.getCensoCamasMap("convenio_"+i).toString().toLowerCase()+", ");
						  	plano.append(forma.getCensoCamasMap("nombrecentrocosto_"+i).toString().toLowerCase()+" ");
						    
						    plano.append("\n");
						}
				    }
	        	}
        	}
        
        //validacion por tarea 64276
	    if (UtilidadTexto.getBoolean(forma.getMostrarResumen()))
	    {
		    /*-----------------------------------------------------------------------------------------
		     * 				SE CREA LA SECCION PARA EL RESUMEN DEL CENSO DE CAMAS
		     -----------------------------------------------------------------------------------------*/
	        
	        plano.append("RESUMEN CENSO DE CAMAS (CANTIDADES / PORCENTAJES) \n");
	        
	        plano.append(" ESTADOS, CANTIDAD, PORCENTAJE \n");
		    
	        //filas de las Disponibles
	        plano.append("Disponibles, "+forma.getCensoCamasMap("disponible").toString()+", "+forma.getCensoCamasMap("disponibleporcent").toString()+"\n");
		    
		    //fila de las las ocupadas
	        plano.append("Ocupadas, "+forma.getCensoCamasMap("ocupada").toString()+", "+forma.getCensoCamasMap("ocupadaporcent").toString()+"\n");
		    
		    //fila de desinfeccion
	        plano.append("Desinfección, "+forma.getCensoCamasMap("desinfeccion").toString()+", "+forma.getCensoCamasMap("desinfeccionporcent").toString()+"\n");
		    
	        //fila de Mantenimiento
	        plano.append("Mantenimiento, "+forma.getCensoCamasMap("mantenimiento").toString()+", "+forma.getCensoCamasMap("mantenimientoporcent").toString()+"\n");
		    
		  	//fila de Fuera de servicio
	        plano.append("Fuera de Servicio, "+forma.getCensoCamasMap("fueradeservicio").toString()+", "+forma.getCensoCamasMap("fueradeservicioporcent").toString()+"\n");
		    
	 	  	//fila de Reservadas
	        plano.append("Reservadas, "+forma.getCensoCamasMap("reservada").toString()+", "+forma.getCensoCamasMap("reservadaporcent").toString()+"\n");
		    
		    //fila de Pendiente por Trasladar
	        plano.append("Pendiente por Trasladar, "+forma.getCensoCamasMap("trasladar").toString()+", "+forma.getCensoCamasMap("trasladarporcent").toString()+"\n");
		    
		    //fila de Pendiente por Remitir
	        plano.append("Pendiente por Remitir, "+forma.getCensoCamasMap("remitir").toString()+", "+forma.getCensoCamasMap("remitirporcent").toString()+"\n");
		    
		    //fila de Con Salida
	        plano.append("Con Salida, "+forma.getCensoCamasMap("consalida").toString()+", "+ forma.getCensoCamasMap("consalidaporcent").toString()+"\n");
		    
		    //fila de Total
	        plano.append("Total, "+forma.getCensoCamasMap("total").toString()+", "+ forma.getCensoCamasMap("totalporcent").toString());
		    
		    /*-----------------------------------------------------------------------------------------
		     * 				FIN DE LA SECCION PARA EL RESUMEN DEL CENSO DE CAMAS
		     -----------------------------------------------------------------------------------------*/
	    }
		return plano;
        
	}
	
	
	public void crearCvs (Connection connection, CensoCamasForm forma,UsuarioBasico usuario)
	{
		boolean OperacionTrue=false,existeTxt=false;
		int ban=ConstantesBD.codigoNuncaValido;
		//		arma el nombre del reporte
		String nombreReport="reporte-"+forma.getTipoReporte()+"-"+UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual());
		//se genera el documento con la informacion
		String path=ValoresPorDefecto.getReportPath()+ValoresPorDefecto.getArchivosPlanosReportes(usuario.getCodigoInstitucionInt())+System.getProperty("file.separator");
		String url=ValoresPorDefecto.getReportUrl()+ValoresPorDefecto.getArchivosPlanosReportes(usuario.getCodigoInstitucionInt())+System.getProperty("file.separator");
		if (Utilidades.convertirAEntero(forma.getCensoCamasMap("numRegistros")+"")>0)
			OperacionTrue=TxtFile.generarTxt(generarTxt(connection, forma, usuario), nombreReport,path,".csv");
		
			if (OperacionTrue)
		{
			//se genera el archivo en formato Zip
			ban=BackUpBaseDatos.EjecutarComandoSO("zip  -j "+path+nombreReport+".zip"+" "+path+nombreReport+".csv");
			//se ingresa la direccion donde se almaceno el archivo
			forma.setRuta(path+nombreReport+".csv");
			//se ingresa la ruta para poder descargar el archivo
			forma.setUrlArchivo(url+nombreReport+".zip");
			

			//se valida si existe el txt
			existeTxt=UtilidadFileUpload.existeArchivo(path, nombreReport+".csv");
			//se valida si existe el zip
			forma.setExisteArchivo(UtilidadFileUpload.existeArchivo(path, nombreReport+".zip"));
									
			if (existeTxt )
				forma.setOperacionTrue(true);
		}
	}
	
	
	/*-------------------------------------------------------------------------------------------------------------------
	 * 							  FIN METODOS PARA EL CENSO DE CAMAS
	 --------------------------------------------------------------------------------------------------------------------*/
	
}