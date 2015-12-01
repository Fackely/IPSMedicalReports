/*
 * @(#)AntecedentesGinecoObstetricosAction.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */
package com.princetonsa.action;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.InfoDatos;
import util.InfoDatosInt;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadTexto;
import util.UtilidadValidacion;
import util.Utilidades;

import com.princetonsa.actionform.AntecedentesGinecoObstetricosForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.antecedentes.AntecedentesGinecoObstetricos;
import com.princetonsa.mundo.antecedentes.AntecedentesGinecoObstetricosHistorico;
import com.princetonsa.mundo.antecedentes.Embarazo;
import com.princetonsa.mundo.antecedentes.HijoBasico;

/**
 * Action, controla todo el módulo de antecedentes ginecoobstétricos
 * @version 1.0
 * @author <a href="mailto:Liliana@PrincetonSA.com">Liliana Caballero</a>
 * @see org.apache.struts.action.ActionForm#validate(ActionMapping,
 * HttpServletRequest)
 */
@SuppressWarnings({"deprecation","rawtypes"})
public class AntecedentesGinecoObstetricosAction extends Action
{
	/**
	 * Maneja los logs del módulo de control de antecedentes ginecoobstétricos
	 */
	private Logger logger = Logger.getLogger(AntecedentesGinecoObstetricosAction.class);
	
	/**
	 * Método para el manejo de las acciones y estados de las funcionalidades
	 * relacionadas con los antecedentes ginecoobstétricos
	 * @see org.apache.struts.action.Action#execute(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse)
	 */
	public ActionForward execute(	ActionMapping mapping, 
														ActionForm form, 
														HttpServletRequest request,
														HttpServletResponse response) throws Exception
														{
		Connection con = null;

		try {
			if( form instanceof AntecedentesGinecoObstetricosForm )
			{
				AntecedentesGinecoObstetricosForm antecedentesBean = (AntecedentesGinecoObstetricosForm)form;
				String estado = antecedentesBean.getEstado();

				logger.warn("\n\n EN AntecedentesGinecoObstetricosAction EL Estado ["+ estado + "]\n\n");

				try
				{
					String tipoBD = System.getProperty("TIPOBD");
					DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
					con = myFactory.getConnection();
				}
				catch(Exception e)
				{
					e.printStackTrace();
					request.setAttribute("codigoDescripcionError", "errors.problemasBd");
					return mapping.findForward("paginaError");
				}

				HttpSession session=request.getSession();			
				PersonaBasica paciente = (PersonaBasica)session.getAttribute("pacienteActivo");
				UsuarioBasico usuario = (UsuarioBasico)session.getAttribute("usuarioBasico");
				antecedentesBean.setEsProcesoExitoso(ConstantesBD.acronimoNo);

				if( usuario == null )
				{
					return ComunAction.accionSalirCasoError(mapping, request, con, logger, "No existe el usuario", "errors.usuario.noCargado", true);
				}
				else
					if( !UtilidadValidacion.esProfesionalSalud(usuario) )
					{
						antecedentesBean.reset();
						return ComunAction.accionSalirCasoError(mapping, request, con, logger, "El usuario no tiene permisos de acceder a esta funcionalidad "+usuario.getLoginUsuario(), "errors.usuario.noAutorizado", true);
					}				
					else
						if( paciente == null || paciente.getCodigoTipoIdentificacionPersona().equals("") )
						{
							return ComunAction.accionSalirCasoError(mapping, request, con, logger, "paciente null o sin  id", "errors.paciente.noCargado", true);
						}
						else
							if( paciente.getCodigoSexo() == ConstantesBD.codigoSexoMasculino )
							{
								return ComunAction.accionSalirCasoError(mapping, request, con, logger, "paciente HOMBRE", "error.antecedentes.ginecoobstetricos.validacionSexo", true);
							}
							else
								if( estado == null || estado.equals("") )
								{
									return ComunAction.accionSalirCasoError(mapping, request, con, logger, "La accion específicada no esta definida", "errors.estadoInvalido", true);
								}
								else
									if( estado.equals("cancelar") )
									{
										String paginaSiguiente=request.getParameter("paginaSiguiente");
										antecedentesBean.reset();

										UtilidadBD.cerrarConexion(con);
										response.sendRedirect(paginaSiguiente);
									}
									else
										if( estado.equals("cargar") )
										{
											antecedentesBean.reset();

											if( logger.isDebugEnabled() )
											{
												logger.debug("Está cargando los antecedentes ginecoobstétricos del paciente si los tiene ");
											}

											//-Genera el mundo para enviar datos a la base de datos 
											AntecedentesGinecoObstetricos antecedentes = new AntecedentesGinecoObstetricos();
											antecedentes.setPaciente(paciente);

											antecedentes.cargar(con, 0);
											AntecedentesGinecoObstetricosHistorico antGH = antecedentes.cargarUltimoRegInfoEmbarazos(con);

											this.cargarBean(antecedentes, antecedentesBean);
											postularInfoEmbarazos(antGH, antecedentesBean);


											//Debemos ponerlo como atributo de sesión, para verlo en caso de error
											session.setAttribute("numeroEmbarazos", "" + antecedentesBean.getNumeroEmbarazos());

											//Aunque con iterate podemos repetir por cada número de hijos,
											//necesitamos estos números como variables para generar los nombres
											//de los atributos propios de los hijos, luego lo vamos a pasar como un
											//atributo del request

											int numHijosPorEmbarazo[]=new int[antecedentesBean.getNumeroEmbarazos()];

											for (int k=0;k<antecedentesBean.getNumeroEmbarazos(); k++)
											{
												numHijosPorEmbarazo[k]=( (Embarazo) antecedentes.getEmbarazos().get(k) ).getHijos().size();
												for(int j=1; j<=numHijosPorEmbarazo[k]; j++)
												{
													String numeroTiposPartoVaginalTemp=(String)antecedentesBean.getValue("numTiposPartoVaginal_"+(k+1)+"_"+j);
													if (numeroTiposPartoVaginalTemp!=null)
													{
														session.setAttribute("numeroTiposPartoVaginal_"+(k+1)+"_"+j, numeroTiposPartoVaginalTemp);
													}
													else
													{
														session.setAttribute("numeroTiposPartoVaginal_"+(k+1)+"_"+j, "0");
													}
												}
											}

											session.setAttribute("numHijosPorEmbarazo", numHijosPorEmbarazo);

											UtilidadBD.cerrarConexion(con);	
											return mapping.findForward("ginecoObstetricosPpal");
										}
										else
											if( estado.equals("resumen") )
											{
												antecedentesBean.reset();
												if( logger.isDebugEnabled() )
												{
													logger.debug("Está mostrando el resumen de los antecedentes ginecoobstétricos del paciente si los tiene ");
												}

												// Se carga toda la información de la bd en el momento para mostrar un resumen completo
												AntecedentesGinecoObstetricos antecedentes = new AntecedentesGinecoObstetricos();
												antecedentes.setPaciente(paciente);

												antecedentes.cargar(con, 0);

												cargarBeanCompleto(antecedentes, antecedentesBean);

												request.setAttribute("numeroEmbarazos", ""+antecedentesBean.getNumeroEmbarazos());
												request.setAttribute("numMetodosAnticonceptivos", ""+antecedentesBean.getNumMetodosAnticonceptivos());

												for(int i=1; i<=antecedentesBean.getNumeroEmbarazos(); i++)
												{
													String nH = (String)antecedentesBean.getValue("numeroHijos_" +i);
													int numH = Integer.parseInt(nH);
													request.setAttribute("numeroHijos_"+i, nH);
													for(int j=1; j<=numH; j++)
													{


														String numeroTiposPartoVaginalTemp=(String)antecedentesBean.getValue("numTiposPartoVaginal_"+i+"_"+j);
														if (numeroTiposPartoVaginalTemp!=null)
														{
															request.setAttribute("numTiposPartoVaginal_"+i+"_"+j, antecedentesBean.getValue("numTiposPartoVaginal_"+i+"_"+j));
														}
														else
														{
															request.setAttribute("numTiposPartoVaginal_"+i+"_"+j, "0");
														}

													}
												}

												UtilidadBD.cerrarConexion(con);

												return mapping.findForward("paginaResumen");
											}
											else
												if( estado.equals("principal") )
												{
													if( logger.isDebugEnabled() )
													{
														logger.debug("Está volviendo a la página principal ");
													}

													UtilidadBD.cerrarConexion(con);

													if( UtilidadCadena.noEsVacio(antecedentesBean.getObservacionesViejas()) )
														antecedentesBean.setObservacionesViejas(antecedentesBean.getObservacionesViejas().replaceAll("<br>", "\n"));

													return mapping.findForward("ginecoObstetricosPpal");
												}
												else
													if( estado.equals("cancelar") )
													{
														if( logger.isDebugEnabled() )
														{
															logger.debug("Está cancelando la acción (borrando el bean)");
														}

														antecedentesBean.reset();

														UtilidadBD.cerrarConexion(con);

														return mapping.findForward("ginecoObstetricosPpal");
													}
													else
														if( estado.equals("finalizar"))
														{
															//ConstantesBD.acronimoDiagnosticoNoSeleccionado
															if( logger.isDebugEnabled() )
															{
																logger.debug("Va a insertar un/os nuevo/s registro/s de antecedentes ginecoobstétricos a la base de datos");
															}

															AntecedentesGinecoObstetricos antecedentes = new AntecedentesGinecoObstetricos();
															antecedentes.setPaciente(paciente);
															antecedentes.setLoginUsuario(usuario.getLoginUsuario());

															String identificacionMedico = usuario.getNombreUsuario()+"  "+usuario.getNumeroRegistroMedico();

															//Si las tiene hay que agregarle al profesional de la salud
															InfoDatosInt especialidadesMedico[]=usuario.getEspecialidades();

															if (especialidadesMedico!=null&&especialidadesMedico.length>0)
															{
																//identificacionMedico+=" Especialidades: ";

																for (int i=0;i<especialidadesMedico.length;i++)
																{
																	if (i!=especialidadesMedico.length-1)
																	{
																		identificacionMedico+=" " + especialidadesMedico[i].getNombre()+ ",";
																	}
																	else
																	{
																		identificacionMedico+=" " + especialidadesMedico[i].getNombre();
																	}

																}
															}

															int embarazosPreviamenteCargados;

															try
															{
																embarazosPreviamenteCargados=Integer.parseInt((String)session.getAttribute("numeroEmbarazos")); 	
															}
															catch (Exception e)
															{
																embarazosPreviamenteCargados=0;
															}

															//-- Verificar si hay informacion a registrar .....
															boolean hayInfo = this.cargarObjeto(antecedentes, antecedentesBean, usuario, embarazosPreviamenteCargados);

															if (hayInfo)
															{

																antecedentes.modificar(con);

																if( logger.isDebugEnabled() )
																{
																	logger.debug("Insertó un/os nuevo/s registro/s de antecedentes ginecoobstétricos a la base de datos");
																}
															}	

															antecedentesBean.reset();
															antecedentesBean.setEsProcesoExitoso(ConstantesBD.acronimoSi);
															UtilidadBD.cerrarConexion(con);				
															return mapping.findForward("limpieza");
														}
														else
															if( estado.equals("salir"))
															{
																if( logger.isDebugEnabled() )
																{
																	logger.debug("Va a insertar un/os nuevo/s registro/s de antecedentes ginecoobstétricos a la base de datos");
																}

																AntecedentesGinecoObstetricos antecedentes = new AntecedentesGinecoObstetricos();
																antecedentes.setPaciente(paciente);
																antecedentes.setLoginUsuario(usuario.getLoginUsuario());

																//String identificacionMedico = medico.getLoginUsuario()+"  "+medico.getNumeroRegistroMedico();

																int embarazosPreviamenteCargados;

																try
																{
																	embarazosPreviamenteCargados=Integer.parseInt((String)session.getAttribute("numeroEmbarazos")); 	
																}
																catch (Exception e)
																{
																	embarazosPreviamenteCargados=0;
																}

																this.cargarObjeto(antecedentes, antecedentesBean, usuario, embarazosPreviamenteCargados);

																antecedentes.modificar(con);

																if( logger.isDebugEnabled() )
																{
																	logger.debug("Insertó un/os nuevo/s registro/s de antecedentes ginecoobstétricos a la base de datos");
																}

																antecedentesBean.reset();

																String paginaSiguiente=request.getParameter("paginaSiguiente");

																UtilidadBD.cerrarConexion(con);					
																response.sendRedirect(paginaSiguiente);
															}

			}
			else
			{

				logger.error("El form no es compatible con el form de Antecedentes GinecoObstétricos ");

			}

			return null;
		} catch (Exception e) {
			Log4JManager.error(e);
			e.printStackTrace();
			return null;
		}
		finally{
			UtilidadBD.closeConnection(con);
		}
	}
	
	/**
	 * Carga el form de antecedentes ginecoobstétricos con la información
	 * pertinente contenida en el objeto.
	 * @param 	AntecedentesGinecoObstetricos, antecedentes
	 * @param 	AntecedentesGinecoObstetricosForm, bean
	 */
	public void cargarBean(AntecedentesGinecoObstetricos antecedentes, AntecedentesGinecoObstetricosForm bean)
	{	
		//---No historicos y no modificables despues de grabados
		if( !antecedentes.getRangoEdadMenarquia().getAcronimo().equals("") )
		{
			bean.setRangoEdadMenarquia(antecedentes.getRangoEdadMenarquia().getCodigo());
			if( antecedentes.getRangoEdadMenarquia().getCodigo() == -1 )
				bean.setNombreRangoEdadMenarquia("No se ha grabado información");
			else
			{
				bean.setNombreRangoEdadMenarquia(antecedentes.getRangoEdadMenarquia().getValue());
				bean.setExisteRangoEdadMenarquia(true);
			}
		}
		bean.setOtraEdadMenarquia(antecedentes.getOtroEdadMenarquia());
		if( !antecedentes.getRangoEdadMenopausia().getAcronimo().equals("") )
		{
			bean.setRangoEdadMenopausia(antecedentes.getRangoEdadMenopausia().getCodigo());
			if( antecedentes.getRangoEdadMenopausia().getCodigo() == -1 )
				bean.setNombreRangoEdadMenopausia("No se ha grabado información");
			else
			{
				bean.setNombreRangoEdadMenopausia(antecedentes.getRangoEdadMenopausia().getValue());
				bean.setExisteRangoEdadMenopausia(true);
			}
		}
		bean.setOtraEdadMenopausia(antecedentes.getOtroEdadMenopausia());
		
		if(antecedentes.getInicioVidaSexual() == 0 ) 
			bean.setInicioVidaSexual("");
		else
			bean.setInicioVidaSexual(""+antecedentes.getInicioVidaSexual());

		if(antecedentes.getInicioVidaObstetrica() == 0 ) 
			bean.setInicioVidaObstetrica("");
		else
			bean.setInicioVidaObstetrica(""+antecedentes.getInicioVidaObstetrica());
		
		String observacionesStr =  antecedentes.getObservaciones();
		if( observacionesStr == null )
			observacionesStr = "";
		
		if( bean.estado.equals("resumen") )
			bean.setObservacionesViejas(observacionesStr.replaceAll("\n", "<br>"));
		else
			bean.setObservacionesViejas(observacionesStr.replaceAll("<br>", "\n"));
		//		Fin no historicos y no modificables despues de grabados
		
		// 		Métodos anticonceptivos
		ArrayList metodosAnticonceptivos = antecedentes.getMetodosAnticonceptivos();
		bean.setNumMetodosAnticonceptivos(metodosAnticonceptivos.size());

		for( int i=0; i < metodosAnticonceptivos.size(); i++ )
		{
			InfoDatos metodo = (InfoDatos)metodosAnticonceptivos.get(i);
			bean.setValue("metodosAnticonceptivos_"+metodo.getAcronimo(), metodo.getAcronimo());
			bean.setValue("nombreMetodosAnticonceptivos_"+metodo.getAcronimo(), metodo.getValue());
			bean.setValue("descMetodosAnticonceptivos_"+metodo.getAcronimo(), metodo.getDescripcion());
			bean.setValue("existeMetodoAnticonceptivo_"+metodo.getAcronimo(), "true");
			if( !metodo.getDescripcion().trim().equals("") )
				bean.setValue("existeDescMetodoAnticonceptivo_"+metodo.getAcronimo(), "true");
		}
		//		Fin métodos anticonceptivos
		
		
		//
		int indiceUltimo = antecedentes.getAntecedentesHistoricos().size() - 1;
		AntecedentesGinecoObstetricosHistorico ultimo = (AntecedentesGinecoObstetricosHistorico) antecedentes.getAntecedentesHistoricos().get(indiceUltimo);
		
		bean.setFechaUltimaMamografia(ultimo.getFechaUltimaMamografia());
		bean.setDescUltimaMamografia(ultimo.getDescripcionUltimaMamografia());
		
		bean.setFechaUltimaCitologia(ultimo.getFechaUltimaCitologia());
		bean.setDescUltimaCitologia(ultimo.getDescripcionUltimaCitologia());
		
		bean.setFechaUltimaEcografia(ultimo.getFechaUltimaEcografia());
		bean.setDescUltimaEcografia(ultimo.getDescripcionUltimaEcografia());
		
		bean.setFechaUltimaDensimetriaOsea(ultimo.getFechaUltimaDensimetriaOsea());
		bean.setDescUltimaDensimetriaOsea(ultimo.getDescUltimaDensimetriaOsea());
		
		bean.setDescUltimoProcedimiento(ultimo.getDescripcionProcedimientosGinecologicos());
		
		bean.setEnferTransSexual(ultimo.getEnferTransSexual());
		bean.setCualEnferTransSex(ultimo.getCualEnferTransSex());
		
		bean.setCirugiaGineco(ultimo.getCirugiaGineco());
		bean.setCualCirugiaGineco(ultimo.getCualCirugiaGineco());
		
		bean.setHistoriaInfertilidad(ultimo.getHistoriaInfertilidad());
		bean.setCualHistoInfertilidad(ultimo.getCualHistoInfertilidad());
		
		bean.setCicloMenstrual(ultimo.getCicloMenstrual()+"");
		bean.setDuracionMenstruacion(ultimo.getDuracionMenstruacion()+"");
		
		bean.setFechaUltimaRegla(ultimo.getFechaUltimaRegla());
		bean.setDolorMenstruacion(UtilidadTexto.isEmpty(ultimo.getDolorMenstruacion()) ? "" : (UtilidadTexto.getBoolean(ultimo.getDolorMenstruacion())+""));
		
		bean.setConceptoMenstruacion(Utilidades.convertirAEntero(ultimo.getConceptoMenstruacion().getId()));
		bean.setSangradoAnormal(ultimo.getSangradoAnormal());
		bean.setFlujoVaginal(ultimo.getFlujoVaginal());
		bean.setObservacionesMenstruacion(ultimo.getObservacionesMenstruacion());
		
		bean.setPrematuros(ultimo.getPrematuros());
		bean.setMultiples(ultimo.getMultiples());
		bean.setVag(ultimo.getVag());
		bean.setMInfoEmbarazos(ultimo.getMInfoEmbarazos());
		bean.setVInfoEmbarazos(ultimo.getVInfoEmbarazos());
		bean.setVivosActualmente(ultimo.getVivosActualmente());
		bean.setMuertePerinatal(UtilidadTexto.getBoolean(ultimo.getMuertePerinatal())+"");
		bean.setRetencionPlacentaria(UtilidadTexto.getBoolean(ultimo.getRetencionPlacentaria())+"");
		bean.setInfeccionPostparto(UtilidadTexto.getBoolean(ultimo.getInfeccionPostparto())+"");
		bean.setMalformacion(UtilidadTexto.getBoolean(ultimo.getMalformacion())+"");
		bean.setTipoEmbarazo(ultimo.getTipoEmbarazo());
		
		//
		
		//		Embarazos
		ArrayList embarazos = antecedentes.getEmbarazos();
		
		bean.setNumeroEmbarazos(embarazos.size());
		bean.setNumGestaciones(bean.getNumeroEmbarazos());
		for(int i=1; i<=embarazos.size(); i++)
		{
			Embarazo embarazo = (Embarazo)embarazos.get(i-1);
			
			bean.setValue("codigo_"+i, embarazo.getCodigo()+"");
			
			//Guardamos el número de hijos por embarazo
			bean.setValue("numeroHijos_" + i, embarazo.getHijos().size() + "");
			
			bean.setValue("mesesGestacion_"+i, Float.toString(embarazo.getMesesGestacion()));
			bean.setValue("fechaTerminacion_"+i, embarazo.getFechaTerminacion());
			
			bean.setValue("duracion_"+i,embarazo.getDuracion());
			bean.setValue("ruptura_"+i,embarazo.getTiempoRupturaMembranas());
			bean.setValue("legrado_"+i,embarazo.getLegrado());
			
			int compTempo[]=embarazo.getComplicacion();
			for(int y=0;y<compTempo.length;y++)
			{
				if(compTempo[y]!=0)
				{
					bean.setValue("complicacionEmbarazo_"+i+"_"+compTempo[y],compTempo[y]+"");
				}
			}
			Vector nombresComplicaciones=embarazo.getNombresComplicaciones();
			for(int y=0;y<nombresComplicaciones.size();y++)
			{
				bean.setValue("nombreComplicacionEmbarazo_"+i+"_"+y,nombresComplicaciones.elementAt(y));
			}
			
			/*if( embarazo.getComplicacion().getCodigo() == -1 )
				bean.setValue("nombreComplicacionEmbarazo_"+i, "");
			else
				bean.setValue("nombreComplicacionEmbarazo_"+i, embarazo.getComplicacion().getValue());
				*/
			Vector otrasComplicaciones=embarazo.getOtraComplicacion();
			for(int j=0; j<otrasComplicaciones.size();j++)
			{
				bean.setValue("otraComplicacionEmbarazo_"+i+"_"+j, otrasComplicaciones.elementAt(j)+"");
			}
			bean.setValue("numOtraComplicacion_"+i, new Integer(otrasComplicaciones.size()));
			
			
			bean.setValue("tipoTrabajoParto_"+i, Integer.toString(embarazo.getTrabajoParto().getCodigo()));
			
			if( embarazo.getTrabajoParto().getCodigo() == -1 )
				bean.setValue("nombreTipoTrabajoParto_"+i, "");
			else
				bean.setValue("nombreTipoTrabajoParto_"+i, embarazo.getTrabajoParto().getValue());
				
			bean.setValue("otroTipoTrabajoParto_"+i, embarazo.getOtroTrabajoParto());

			//	Hijos embarazo
			ArrayList hijos = embarazo.getHijos();
			ArrayList formasPartoVaginal;
			bean.setValue("numeroHijos_"+i, ""+hijos.size());
			for(int j=1; j<=hijos.size(); j++)
			{
				HijoBasico hijo = new HijoBasico(); 
				hijo = (HijoBasico)hijos.get(j-1);
				
				boolean partoVaginal = true;
				
				if( hijo.isVivo() )
				{
					bean.setNumVivos(bean.getNumVivos()+1);
					bean.setNumVivosGrabados(bean.getNumVivos());
					bean.setValue("vitalidad_"+i+"_"+j, "vivo");
				}
				else
				{
					bean.setNumMuertos(bean.getNumMuertos()+1);
					bean.setNumMuertosGrabados(bean.getNumMuertos());
					bean.setValue("vitalidad_"+i+"_"+j, "muerto");
				}
								
				if( hijo.isAborto() )
				{
					bean.setValue("cargadoBD_"+i+"_"+j, "true");
					bean.setValue("tiposParto_"+i+"_"+j, new String("4"));
					bean.setValue("nombreTiposParto_"+i+"_"+j, new String("Mortinato"));
					partoVaginal = false;
					bean.setNumAbortos(bean.getNumAbortos()+1);
					bean.setNumAbortosGrabados(bean.getNumAbortos());
				} 
				else
				if( hijo.isCesarea() )
				{
					bean.setValue("cargadoBD_"+i+"_"+j, "true");
					bean.setValue("tiposParto_"+i+"_"+j, new String("5"));
					bean.setValue("nombreTiposParto_"+i+"_"+j, new String("Cesarea"));
					partoVaginal = false;
					bean.setNumCesareas(bean.getNumCesareas()+1);
					bean.setNumCesareasGrabadas(bean.getNumCesareas());
				} 
				else
				if( hijo.getOtroTipoParto() != null && !hijo.getOtroTipoParto().equals("") )
				{
					bean.setValue("cargadoBD_"+i+"_"+j, "true");
					bean.setValue("tiposParto_"+i+"_"+j, new String("0"));
					bean.setValue("nombreTiposParto_"+i+"_"+j, new String("Otro"));
					bean.setValue("otroTipoParto_" +i+"_"+j, hijo.getOtroTipoParto());
					partoVaginal = false;
				}
				else
				if( ( formasPartoVaginal = hijo.getFormasNacimientoVaginal() ).size() > 0 )
				{
					bean.setValue("cargadoBD_"+i+"_"+j, "true");
					bean.setNumPartos(bean.getNumPartos()+1);
					bean.setNumPartosGrabados(bean.getNumPartos());
					
					boolean esvalido = true;					
					if(formasPartoVaginal.size()==1)
					{
						InfoDatos tipoPVInfo = (InfoDatos)formasPartoVaginal.get(0);
						// En este caso no debemos mostrarlo de forma tradicional
						if( tipoPVInfo.getCodigo() == -2 )
						{
							bean.setValue("tiposParto_"+i+"_"+j, new String("3"));
							bean.setValue("nombreTiposParto_"+i+"_"+j, new String("Parto Vaginal"));
							bean.setValue("numTiposPartoVaginal_"+i+"_"+j, "0");
							esvalido = false;
						}
					}
					if( esvalido )
					{

						bean.setValue("tiposParto_"+i+"_"+j, new String("3"));
						bean.setValue("nombreTiposParto_"+i+"_"+j, new String("Parto Vaginal"));
						bean.setValue("numTiposPartoVaginal_"+i+"_"+j, ""+formasPartoVaginal.size());
								
						for(int k=0; k<formasPartoVaginal.size(); k++)
						{
							InfoDatos tipoPVInfo = (InfoDatos)formasPartoVaginal.get(k);
							bean.setValue("tipoPartoVaginal_"+i+"_"+j+"_"+k, tipoPVInfo.getAcronimo());
							bean.setValue("nombreTipoPartoVaginal_"+i+"_"+j+"_"+k, tipoPVInfo.getValue());
						}

					}
					
				}
				if( partoVaginal && hijo.getOtraFormaNacimientoVaginal() != null && !hijo.getOtraFormaNacimientoVaginal().equals("") )
				{
					bean.setValue("otroTipoPartoVaginal_"+i+"_"+j, hijo.getOtraFormaNacimientoVaginal());
					bean.setValue("tiposParto_"+i+"_"+j, new String("3"));
					bean.setValue("nombreTiposParto_"+i+"_"+j, new String("Parto Vaginal"));
				}
				String tempoSexo=hijo.getSexo()+"";
				if(tempoSexo!=null && !tempoSexo.equals("null") && (tempoSexo.equals(ConstantesBD.codigoSexoMasculino+"") || tempoSexo.equals(ConstantesBD.codigoSexoFemenino+"")))
				{
					bean.setValue("sexo_"+i+"_"+j,hijo.getSexo()+"");
				}
				else
				{
					bean.setValue("sexo_"+i+"_"+j,"-1");
				}
				bean.setValue("peso_"+i+"_"+j, hijo.getPeso());
				bean.setValue("lugar_"+i+"_"+j, hijo.getLugar());
			}
			//	Fin hijos embarazo			
		}
		//		Fin embarazos
	}
	
	/**
	 * Carga el objeto (Representación en el mundo de los antecedentes
	 * ginecoobstetricos) con la información pertinente contenido en el form de
	 * los mismos.
	 * @param 	AntecedentesGinecoObstetricos, antecedentes
	 * @param 	AntecedentesGinecoObstetricosForm, bean
	 * @param 	String, medico
	 * @param 	int, embarazosPreviamenteCargados
	 */
	///----------ARMANDO
	@SuppressWarnings("unchecked")
	public boolean cargarObjeto(AntecedentesGinecoObstetricos antecedentes, AntecedentesGinecoObstetricosForm bean, UsuarioBasico medico, int embarazosPreviamenteCargados)
	{
		boolean hayInfoParaGrabar = false;
		
		InfoDatos temp = new InfoDatos();

		//--Verificar si hay Informacion para guardar DATOS.
		if (bean.getRangoEdadMenarquia()!=-1) {	hayInfoParaGrabar = true; }
		if ( UtilidadCadena.noEsVacio(bean.getOtraEdadMenarquia()) ) { hayInfoParaGrabar = true;}
		
		//No historicos y no modificables despues de grabados
		temp.setCodigo(bean.getRangoEdadMenarquia());
		antecedentes.setRangoEdadMenarquia(temp);
		antecedentes.setOtroEdadMenarquia(bean.getOtraEdadMenarquia());
		
		//--Verificar si hay Informacion para guardar DATOS.
		if (bean.getRangoEdadMenopausia()!=-1) { hayInfoParaGrabar = true;	}
		if ( UtilidadCadena.noEsVacio(bean.getOtraEdadMenopausia()) ) { hayInfoParaGrabar = true;	}
				
		temp = new InfoDatos();
		temp.setCodigo(bean.getRangoEdadMenopausia());
		antecedentes.setRangoEdadMenopausia(temp);		
		antecedentes.setOtroEdadMenopausia(bean.getOtraEdadMenopausia());
		
		if( UtilidadCadena.noEsVacio(bean.getInicioVidaSexual()) )
		{
			antecedentes.setInicioVidaSexual(Integer.parseInt(bean.getInicioVidaSexual()));
			hayInfoParaGrabar = true;
		}	
			
		if( UtilidadCadena.noEsVacio(bean.getInicioVidaObstetrica()) )
		{
			antecedentes.setInicioVidaObstetrica(Integer.parseInt(bean.getInicioVidaObstetrica()));
			hayInfoParaGrabar = true;
		}	
		
		if( bean.getObservacionesNuevas() != null && !bean.getObservacionesNuevas().equals("") )
		{
			hayInfoParaGrabar = true;
			antecedentes.setObservaciones(UtilidadTexto.agregarTextoAObservacion(bean.getObservacionesViejas(),bean.getObservacionesNuevas(), medico, true));
		}
		else
		{
			if ( UtilidadCadena.noEsVacio(bean.getObservacionesViejas()) )
			{
				hayInfoParaGrabar = true;
			}
			antecedentes.setObservaciones(bean.getObservacionesViejas());
		}
		//		Fin No historicos y no modificables despues de grabados
		
		//		Métodos anticonceptivos
		ArrayList metodosAnticonceptivos = new ArrayList();

		for( int i=1; i <= bean.getNumMetodosAnticonceptivos(); i++)
		{

			boolean insertar = false;
			temp = new InfoDatos();
			String valor = (String)bean.getValue("metodosAnticonceptivos_"+i);
			
			logger.info("valor "+valor);
			
			if( valor != null )
			{
				if( !valor.equals("") )
				{
					hayInfoParaGrabar = true;
					temp.setCodigo(Integer.parseInt(valor));
					insertar = true;
				}		
			}
			String desc = (String)bean.getValue("descMetodosAnticonceptivos_"+i);
			if( desc != null )
			{
				if(  !desc.equals("") )
				{
					hayInfoParaGrabar = true;
					temp.setCodigo(i);//Integer.parseInt(valor));
					temp.setDescripcion(desc);
					insertar = true;
				}		
			}

			if( insertar )
				metodosAnticonceptivos.add(temp);
				
				
		}
		antecedentes.setMetodosAnticonceptivos(metodosAnticonceptivos);
		
		//		Fin métodos anticonceptivos
		
		/// 	Históricos
		AntecedentesGinecoObstetricosHistorico antHistoricos = new AntecedentesGinecoObstetricosHistorico();
			
		if( UtilidadCadena.noEsVacio(bean.getDuracionMenstruacion()) )
		{
			antHistoricos.setDuracionMenstruacion(Integer.parseInt(bean.getDuracionMenstruacion()));
			hayInfoParaGrabar = true;
		}
			
		antHistoricos.setDolorMenstruacion(bean.getDolorMenstruacion());
		antHistoricos.setFechaUltimaRegla(bean.getFechaUltimaRegla());
		
		if ( UtilidadCadena.noEsVacio(bean.getDolorMenstruacion()) ||
			 UtilidadCadena.noEsVacio(bean.getFechaUltimaRegla()) ||
			 UtilidadCadena.noEsVacio(bean.getObservacionesMenstruacion()) ||
			 (bean.getConceptoMenstruacion() > 0) 
			)
		{
			hayInfoParaGrabar = true;		
		}
		
		if( UtilidadCadena.noEsVacio(bean.getCicloMenstrual()) )
		{
			antHistoricos.setCicloMenstrual(Integer.parseInt(bean.getCicloMenstrual()));
			hayInfoParaGrabar = true;	
		}
			
		antHistoricos.setObservacionesMenstruacion(bean.getObservacionesMenstruacion());
		
		if( bean.getConceptoMenstruacion() != 0 )
			antHistoricos.setConceptoMenstruacion(new InfoDatos(bean.getConceptoMenstruacion()+"", ""));
		else
			antHistoricos.setConceptoMenstruacion(new InfoDatos("-1", ""));
			
		antHistoricos.setFechaUltimaMamografia(bean.getFechaUltimaMamografia());
		antHistoricos.setDescripcionUltimaMamografia(bean.getDescUltimaMamografia());
		antHistoricos.setFechaUltimaCitologia(bean.getFechaUltimaCitologia());
		antHistoricos.setDescripcionUltimaCitologia(bean.getDescUltimaCitologia());
		antHistoricos.setDescripcionProcedimientosGinecologicos(bean.getDescUltimoProcedimiento());
		antHistoricos.setFechaUltimaEcografia(bean.getFechaUltimaEcografia());
		antHistoricos.setDescripcionUltimaEcografia(bean.getDescUltimaEcografia());
		antHistoricos.setFechaUltimaDensimetriaOsea(bean.getFechaUltimaDensimetriaOsea());
		antHistoricos.setDescUltimaDensimetriaOsea(bean.getDescUltimaDensimetriaOsea());
		
		antHistoricos.setGInfoEmbarazos(bean.getGInfoEmbarazos());
		antHistoricos.setPInfoEmbarazos(bean.getPInfoEmbarazos());
		antHistoricos.setAInfoEmbarazos(bean.getAInfoEmbarazos());
		antHistoricos.setCInfoEmbarazos(bean.getCInfoEmbarazos());
		antHistoricos.setVInfoEmbarazos(bean.getVInfoEmbarazos());
		antHistoricos.setMInfoEmbarazos(bean.getMInfoEmbarazos());
		
		antHistoricos.setVag(bean.getVag());
		antHistoricos.setTipoEmbarazo(bean.getTipoEmbarazo());
		antHistoricos.setVivosActualmente(bean.getVivosActualmente());
		antHistoricos.setMuertosAntes1Semana(bean.getMuertosAntes1Semana());	
		antHistoricos.setMuertosDespues1Semana(bean.getMuertosDespues1Semana());		
		antHistoricos.setRetencionPlacentaria(bean.getRetencionPlacentaria());
		antHistoricos.setInfeccionPostparto(bean.getInfeccionPostparto());
		antHistoricos.setMalformacion(bean.getMalformacion());
		antHistoricos.setMuertePerinatal(bean.getMuertePerinatal());
		
		//--nuevos campos seccion Historia Mestrual 
		antHistoricos.setSangradoAnormal(bean.getSangradoAnormal());			
		antHistoricos.setFlujoVaginal(bean.getFlujoVaginal());			
		antHistoricos.setEnferTransSexual(bean.getEnferTransSexual());			
		antHistoricos.setCualEnferTransSex(bean.getCualEnferTransSex());			
		antHistoricos.setCirugiaGineco(bean.getCirugiaGineco());			
		antHistoricos.setCualCirugiaGineco(bean.getCualCirugiaGineco());			
		antHistoricos.setHistoriaInfertilidad(bean.getHistoriaInfertilidad());			
		antHistoricos.setCualHistoInfertilidad(bean.getCualHistoInfertilidad());			
		
		
		if (
				UtilidadCadena.noEsVacio(bean.getFechaUltimaMamografia()) 		||
				UtilidadCadena.noEsVacio(bean.getDescUltimaMamografia())  		||
				UtilidadCadena.noEsVacio(bean.getFechaUltimaCitologia())  		||	
				UtilidadCadena.noEsVacio(bean.getDescUltimaCitologia())  		||
				UtilidadCadena.noEsVacio(bean.getDescUltimoProcedimiento()) 	||
				UtilidadCadena.noEsVacio(bean.getFechaUltimaEcografia())		||
				UtilidadCadena.noEsVacio(bean.getDescUltimaEcografia())			||
				UtilidadCadena.noEsVacio(bean.getFechaUltimaDensimetriaOsea())	||
				UtilidadCadena.noEsVacio(bean.getDescUltimaDensimetriaOsea())	||
				UtilidadCadena.noEsVacio(bean.getGInfoEmbarazos())				||
				UtilidadCadena.noEsVacio(bean.getPInfoEmbarazos())				||
				UtilidadCadena.noEsVacio(bean.getAInfoEmbarazos())				||
				UtilidadCadena.noEsVacio(bean.getCInfoEmbarazos())				||
				UtilidadCadena.noEsVacio(bean.getVInfoEmbarazos())				||
				UtilidadCadena.noEsVacio(bean.getMInfoEmbarazos())				||	
				(bean.getVag()!=0) 											||
				UtilidadCadena.noEsVacio(bean.getTipoEmbarazo())  			||
				UtilidadCadena.noEsVacio(bean.getVivosActualmente())  			||
				UtilidadCadena.noEsVacio(bean.getMuertosAntes1Semana())  			||	
				UtilidadCadena.noEsVacio(bean.getMuertosDespues1Semana())		||
				UtilidadCadena.noEsVacio(bean.getRetencionPlacentaria())	||
				UtilidadCadena.noEsVacio(bean.getInfeccionPostparto())		||
				UtilidadCadena.noEsVacio(bean.getMalformacion())			||
				UtilidadCadena.noEsVacio(bean.getMuertePerinatal())		
		   )
		{
			hayInfoParaGrabar = true;
		}
		
		
		//-Esta validacion se hace para que el mundo reciva "" si no se chequeo el campo por que esta enviando false
		if ( (bean.getP2500()==null)  || bean.getP2500().trim().equals("false"))
		{
			antHistoricos.setP2500("");  
		}
		else
		{
			hayInfoParaGrabar = true;			
			antHistoricos.setP2500(bean.getP2500());
		}
		
		if ((bean.getP4000()==null)  || bean.getP4000().trim().equals("false"))
		{
			antHistoricos.setP4000("");
		}
		else
		{
			antHistoricos.setP4000(bean.getP4000());	
			hayInfoParaGrabar = true;			
		}
		
		if ( (bean.getMayorA2()==null)  || bean.getMayorA2().trim().equals("false"))
		{
			antHistoricos.setMayorA2("");
		}
		else
		{
			antHistoricos.setMayorA2(bean.getMayorA2());
			hayInfoParaGrabar = true;		
		}	
		
		antHistoricos.setFinEmbarazoAnterior(bean.getFinEmbarazoAnterior());
		antHistoricos.setFinEmbarazoMayor1o5(bean.getFinEmbarazoMayor1o5());
		antHistoricos.setPrematuros(bean.getPrematuros());
		antHistoricos.setEctropicos(bean.getEctropicos());
		antHistoricos.setMultiples(bean.getMultiples());
		
		if (
				UtilidadCadena.noEsVacio(bean.getFinEmbarazoAnterior()) ||
				UtilidadCadena.noEsVacio(bean.getFinEmbarazoMayor1o5()) ||
				UtilidadCadena.noEsVacio(bean.getPrematuros())          ||
				UtilidadCadena.noEsVacio(bean.getEctropicos())          ||
				UtilidadCadena.noEsVacio(bean.getMultiples())
		   )
		{
			hayInfoParaGrabar = true;	
		}	
		
		
		antecedentes.addHistorico(antHistoricos);
		//		Fin historicos
		//		Embarazos, incluyo los que ya estaban cargados
		
		if ( (bean.getNumeroEmbarazos() + embarazosPreviamenteCargados) > 0 )
		{
			hayInfoParaGrabar = true;	
		}
		
		for(int i=1; i<=bean.getNumeroEmbarazos() + embarazosPreviamenteCargados; i++)
		{
			Embarazo embarazo = new Embarazo();
			embarazo.setCodigo(i);
			
			String mesesGestacion = (String)bean.getValue("mesesGestacion_"+i);
			if( mesesGestacion != null && !mesesGestacion.equals("") )
				embarazo.setMesesGestacion(Float.parseFloat(mesesGestacion));
			
			String fechaTerminacion = (String)bean.getValue("fechaTerminacion_"+i);
			if( fechaTerminacion != null && !fechaTerminacion.equals("") )
				embarazo.setFechaTerminacion(fechaTerminacion);
			else
				embarazo.setFechaTerminacion(null);

			embarazo.setDuracion((String)bean.getValue("duracion_"+i));
			embarazo.setTiempoRupturaMembranas((String)bean.getValue("ruptura_"+i));
			embarazo.setLegrado((String)bean.getValue("legrado_"+i));
			
			int numeroComplicacionesEmbarazo=Integer.parseInt(bean.getValue("numeroComplicacionesEmbarazo")+"");
			
			int compTempo[]=new int[numeroComplicacionesEmbarazo];
			for(int j=0; j<numeroComplicacionesEmbarazo;j++)
			{
				compTempo[j]=0;
				String tempoPropiedadComp=(String)bean.getValue("complicacionEmbarazo_"+bean.getValue("codigo_"+i)+"_"+j);
				if(tempoPropiedadComp!=null && !tempoPropiedadComp.trim().equals(""))
				{
					compTempo[j]=Integer.parseInt(tempoPropiedadComp);
				}
				if(tempoPropiedadComp==null)
				{
					tempoPropiedadComp=(String)bean.getValue("complicacionEmbarazo_"+embarazo.getCodigo()+"_"+j);
					if(tempoPropiedadComp!=null && !tempoPropiedadComp.equals(""))
					{
						compTempo[j]=Integer.parseInt(tempoPropiedadComp);
					}
				}
			}

			embarazo.setComplicacion(compTempo);

			
			String numOtrasComplicacionesStr=(String)bean.getValue("numeroComplicacionesOtras_"+i);
			
			Vector otrasComplicaciones=new Vector();
//			logger.info("numOtrasComplicacionesStr "+numOtrasComplicacionesStr);
			if(numOtrasComplicacionesStr!=null && !numOtrasComplicacionesStr.equals(""))
			{
				int numOtrasComplicaciones=Integer.parseInt(numOtrasComplicacionesStr);
				for(int j=0; j<numOtrasComplicaciones; j++)
				{
					String tempoPropiedadComp=(String)bean.getValue("otraComplicacion_"+i+"_"+j);
//					logger.info("tempoPropiedadComp "+tempoPropiedadComp);
					if(tempoPropiedadComp!=null && !tempoPropiedadComp.equals(""))
					{
						otrasComplicaciones.add(tempoPropiedadComp);
					}
				}
			}
			
			embarazo.setOtraComplicacion(otrasComplicaciones);
			
			String tipoTrabajoParto = (String)bean.getValue("tipoTrabajoParto_"+i);
			if( tipoTrabajoParto != null && !tipoTrabajoParto.equals("") )
			{
				temp = new InfoDatos();
				temp.setCodigo(Integer.parseInt(tipoTrabajoParto));
				embarazo.setTrabajoParto(temp);
			}
			
			String otroTipoTrabajoParto = (String)bean.getValue("otroTipoTrabajoParto_"+i);
			if( otroTipoTrabajoParto != null && !otroTipoTrabajoParto.equals("") && tipoTrabajoParto.equals("0"))
				embarazo.setOtroTrabajoParto(otroTipoTrabajoParto);
					
			//		Hijos Embarazo
			String numeroHijos = (String)bean.getValue("numeroHijos_" +i);
			int numHijos = Integer.parseInt(numeroHijos);
			for(int j=1; j<=numHijos; j++)
			{
				HijoBasico hijo = new HijoBasico();
				hijo.setNumeroHijoEmbarazo(j);
				String vitalidad = (String)bean.getValue("vitalidad_"+i+"_"+j);
				String vitalidadValor = (String)bean.getValue("vitalidadValor_"+i+"_"+j);
				logger.info(i+" "+j);
				logger.info("vitalidadValor "+vitalidadValor);
				if( vitalidadValor == null )
					vitalidadValor = "";
				
				if( vitalidad == null ||vitalidad.equals("muerto")||vitalidadValor.equals("muerto"))
					hijo.setVivo(false);
				else
					hijo.setVivo(true);

				hijo.setSexo(Integer.parseInt((String)bean.getValue("sexo_"+i+"_"+j)));
				hijo.setPeso((String)bean.getValue("peso_"+i+"_"+j));
				hijo.setLugar((String)bean.getValue("lugar_"+i+"_"+j));
			
				String tipoParto = ((String)bean.getValue("tiposParto_"+i+"_"+j)).trim();				

				// Está atado a los códigos de tipo de parto de la base de datos!!!!!
				// Código 3 ---> PARTO VAGINAL
				if( tipoParto.equals("3") )
				{
					boolean existePartoVaginal = false;
					
					String otroTPV = (String)bean.getValue("otroTipoPartoVaginal_"+i+"_"+j);

					if( otroTPV != null )
					{
						if( !otroTPV.equals("") )
							existePartoVaginal = true;
							
						hijo.setOtraFormaNacimientoVaginal(otroTPV);
					}
					
					
					// Hay 2 tipos de parto actualmente mas la opcion de "otro"
					for(int k=1; k<=2; k++)
					{
						String tipoPV = (String)bean.getValue("tipoPartoVaginal_"+i+"_"+j+"_"+k);
										
						if( tipoPV != null )
						{
							InfoDatos tipoPVInfo = new InfoDatos(tipoPV, "");
							hijo.addFormaNacimientoVaginal(tipoPVInfo);
							existePartoVaginal = true;
						}
					}
				
					if( !existePartoVaginal )
					{
						InfoDatos tipoPVInfo = new InfoDatos("-2", "");
						hijo.addFormaNacimientoVaginal(tipoPVInfo);
					}
				}
	
				// Código 4 ---> ABORTO
				else
				if( tipoParto.equals("4") )
				{
					hijo.setVivo(false);
					hijo.setAborto(true);

				// Código 5 ---> CESAREA
				} 
				else
				if( tipoParto.equals("5") )
					hijo.setCesarea(true);
				
				// Código 0 ---> Otro Tipo Parto
				else
				if( tipoParto.equals("0") )
				{
					String otroTP = (String)bean.getValue("otroTipoParto_" +i+"_"+j);
					hijo.setOtroTipoParto(otroTP);
				}
				embarazo.addHijo(hijo);
			}
			//		Fin hijos embarazo
			
			antecedentes.addEmbarazo(embarazo);
		}
		//		Fin embarazos
		
		
		return hayInfoParaGrabar;
	}
	
	/**
	 * Carga la información ingresada previamente (si existe), en el form (bean)
	 * para ser postulada en la tabla de informacion general de los embarazos
	 * @param 	AntecedentesGinecoObstetricosHistorico, antGH
	 * @param 	AntecedentesGinecoObstetricosForm, bean
	 */
	public void postularInfoEmbarazos(AntecedentesGinecoObstetricosHistorico antGH, AntecedentesGinecoObstetricosForm bean)
	{
		bean.setGInfoEmbarazos(antGH.getGInfoEmbarazos());
		bean.setPInfoEmbarazos(antGH.getPInfoEmbarazos());
		bean.setAInfoEmbarazos(antGH.getAInfoEmbarazos());
		bean.setCInfoEmbarazos(antGH.getCInfoEmbarazos());
		
		//no postular la informacion de nacidos vivos y nacidos muertos.
		//bean.setVInfoEmbarazos(antGH.getVInfoEmbarazos());
		//bean.setMInfoEmbarazos(antGH.getMInfoEmbarazos());
		
		//-Postular los nuevos valores
		bean.setP2500(antGH.getP2500());
		bean.setP4000(antGH.getP4000());
		bean.setMayorA2(antGH.getMayorA2());
		bean.setFinEmbarazoAnterior(antGH.getFinEmbarazoAnterior());
		bean.setFinEmbarazoMayor1o5(antGH.getFinEmbarazoMayor1o5());
		bean.setPrematuros(antGH.getPrematuros());
		bean.setEctropicos(antGH.getEctropicos());
		bean.setMultiples(antGH.getMultiples());
		
		//--- seccion "Informacion embarazos nuevos"
		//-- Campos de Información Embarazos Nuevos
		//modificacion por la tarea 283881
		/*
		 En el módulo Historia Clínica, en la funcionalidad Antecedentes Gineco-obstétricos, en la sección Información Embarazos, los siguientes campos no deben tener ningún valor por defecto:
		- Ant Abor Esp
		- Infecc. Postparto
		- Malformación
		- A Término
		- Prolongado
		- Muertes Perinatales
		- Emb. Planeado/Deseado
		- Retención Placentaria
		Adicionalmente, al momento de guardar cualquier valor en algunos de estos campos, se debe mostrar en el resumen. 
		*/
		
		
		bean.setVag(antGH.getVag());
		
		/*
		if (!antGH.getAntAborEsp().trim().equals(""))
		{
			if (UtilidadTexto.getBoolean(antGH.getAntAborEsp()))
				bean.setAntAborEsp( ValoresPorDefecto.getValorTrueParaConsultas() );
			else
				bean.setAntAborEsp( ValoresPorDefecto.getValorFalseParaConsultas() );
		}
		else
		{
			bean.setAntAborEsp("");
		}
		*/

		/*
		if (!antGH.getAlTermino().trim().equals(""))
		{
			if (UtilidadTexto.getBoolean(antGH.getAlTermino()))
				bean.setAlTermino( ValoresPorDefecto.getValorTrueParaConsultas() );
			else
				bean.setAlTermino( ValoresPorDefecto.getValorFalseParaConsultas() );
		}
		else
		{
			bean.setAlTermino("");
		}
		
		if (!antGH.getProlongado().trim().equals(""))
		{
			if (UtilidadTexto.getBoolean(antGH.getProlongado()))
				bean.setProlongado( ValoresPorDefecto.getValorTrueParaConsultas());
			else
				bean.setProlongado( ValoresPorDefecto.getValorFalseParaConsultas() );
		}
		else
		{
			bean.setProlongado("");
		}	
		
		if (!antGH.getEmbarazoPlaneado().trim().equals(""))
		{
			if (UtilidadTexto.getBoolean(antGH.getEmbarazoPlaneado()))
				bean.setEmbarazoPlaneado( ValoresPorDefecto.getValorTrueParaConsultas() );
			else
				bean.setEmbarazoPlaneado( ValoresPorDefecto.getValorFalseParaConsultas() );
		}
		else
		{
			bean.setEmbarazoPlaneado("");
		}	
		
		if (!antGH.getRetencionPlacentaria().trim().equals(""))
		{
			if (UtilidadTexto.getBoolean(antGH.getRetencionPlacentaria()))
				bean.setRetencionPlacentaria( ValoresPorDefecto.getValorTrueParaConsultas() );
			else
				bean.setRetencionPlacentaria( ValoresPorDefecto.getValorFalseParaConsultas() );
		}
		else
		{
			bean.setRetencionPlacentaria("");
		}	
		
			
		if (!antGH.getInfeccionPostparto().trim().equals(""))
		{
			if (UtilidadTexto.getBoolean(antGH.getInfeccionPostparto()))
				bean.setInfeccionPostparto(ValoresPorDefecto.getValorTrueParaConsultas());
			else
				bean.setInfeccionPostparto(ValoresPorDefecto.getValorFalseParaConsultas());
		}
		else
		{
			bean.setInfeccionPostparto("");
		}

		//if (UtilidadCadena.noEsVacio(antGH.getCualInfeccionPostparto()))
			bean.setCualInfeccionPostparto( antGH.getCualInfeccionPostparto() );

		if (!antGH.getMalformacion().trim().equals(""))
		{
			if (UtilidadTexto.getBoolean(antGH.getMalformacion()))
				bean.setMalformacion(ValoresPorDefecto.getValorTrueParaConsultas());
			else
				bean.setMalformacion(ValoresPorDefecto.getValorFalseParaConsultas());
		}
		else
		{
			bean.setMalformacion("");
		}
			
		
		//if (UtilidadCadena.noEsVacio(antGH.getCualMalformacion()))
			bean.setCualMalformacion( antGH.getCualMalformacion() );
			
		if (!antGH.getMuertePerinatal().trim().equals(""))
		{
			if (UtilidadTexto.getBoolean(antGH.getMuertePerinatal()))
				bean.setMuertePerinatal(ValoresPorDefecto.getValorTrueParaConsultas());
			else
				bean.setMuertePerinatal(ValoresPorDefecto.getValorFalseParaConsultas());
		}
		else
		{
			bean.setMuertePerinatal("");
		}	
		
		//if (UtilidadCadena.noEsVacio(antGH.getCausaMuertePerinatal()))
			bean.setCausaMuertePerinatal( antGH.getCausaMuertePerinatal() );
		*/
	}
	
	/**
	 * Carga el bean de antecedentes ginecoobstetricos con los historicos
 	 */
	public void cargarBeanCompleto(AntecedentesGinecoObstetricos antecedentes, AntecedentesGinecoObstetricosForm bean)
	{
		// carga los basicos del bean
		cargarBean(antecedentes, bean);
		
		// los historicos
		ArrayList historicos = antecedentes.getAntecedentesHistoricos();
		bean.setHistoricos(historicos);
	}
}