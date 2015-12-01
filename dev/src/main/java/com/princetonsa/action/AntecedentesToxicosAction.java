/*
 * @(#)AntecedentesToxicosAction.java
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
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.UtilidadValidacion;

import com.princetonsa.actionform.AntecedentesToxicosForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.antecedentes.AntecedenteToxico;
import com.princetonsa.mundo.antecedentes.AntecedentesToxicos;
import com.princetonsa.mundo.historiaClinica.ImpresionResumenAtenciones;


/**
 * <code>Action</code> para el Ingreso/Actualizacion de una
 * <code>AntecedentesToxicos</code>, toma los atributos de un objeto
 * <code>AntecedentesToxicosForm</code> y los transforma en el formato
 * apropiado para una<code>AntecedentesToxicos</code>.
 *
 * @version 2.0, Noviembre 27, 2003
 * @author <a href="mailto:Liliana@PrincetonSA.com">Liliana Caballero</a>
 */

public class AntecedentesToxicosAction extends Action 
{
	/**
	 * Para hacer logs de esta funcionalidad.
	 */
	private Logger logger = Logger.getLogger(AntecedentesToxicosAction.class);
	
	/**
	 * Usuario que esta trabajando actualmente en el sistema.
	 */
	private UsuarioBasico usuario;
	
	/**
	 * Ejecuta las acciones correspondientes a antecedentes toxicos: ingresar/cargar/modificar
	 * @param mapping el mapeado usado para elegir esta instancia
	 * @param form objeto con los datos provenientes del formulario
	 * @param request el <i>servlet request</i> que esta siendo procesado en este momento
	 * @param response el <i>servlet response</i> resultado de procesar este request
	 * @return un <code>ActionForward</code> indicando la siguiente pagina
	 * dentro de la navegación
	 */
	public ActionForward execute(	ActionMapping mapping, 
														ActionForm form, 
														HttpServletRequest request, 
														HttpServletResponse response) throws Exception 
														{			
		Connection con = null;
		try {
			if( form instanceof AntecedentesToxicosForm )
			{
				AntecedentesToxicosForm toxicosForm = (AntecedentesToxicosForm)form;
				String estado = toxicosForm.getEstado();

				logger.warn("\n\n  En AntecedentesToxicosAction El Estado  [" + estado + "]  \n\n");

				String tipoBD;
				try
				{
					tipoBD = System.getProperty("TIPOBD");
					DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
					con = myFactory.getConnection();
				}
				catch(Exception e)
				{
					e.printStackTrace();

					toxicosForm.reset();

					logger.warn("Problemas con la base de datos "+e);
					request.setAttribute("codigoDescripcionError", "errors.problemasBd");
					return mapping.findForward("paginaError");
				}

				HttpSession session=request.getSession();			
				PersonaBasica paciente = (PersonaBasica)session.getAttribute("pacienteActivo");
				usuario = (UsuarioBasico)session.getAttribute("usuarioBasico");
				toxicosForm.setOperacionExistosa(ConstantesBD.acronimoNo);

				if( usuario == null )
				{
					if( logger.isDebugEnabled() )
					{
						logger.debug("Usuario no valido (null)");
					}

					if( con != null && !con.isClosed() )
						UtilidadBD.closeConnection(con);

					toxicosForm.reset();					

					request.setAttribute("codigoDescripcionError", "errors.usuario.noCargado");
					return mapping.findForward("paginaError");				
				}
				else
					if( paciente == null || paciente.getTipoIdentificacionPersona().equals("") )
					{
						if( logger.isDebugEnabled() )
						{
							logger.debug("El paciente no es valido (null)");
						}

						if( con != null && !con.isClosed() )
							UtilidadBD.closeConnection(con);

						toxicosForm.reset();					

						request.setAttribute("codigoDescripcionError", "errors.paciente.noCargado");
						return mapping.findForward("paginaError");				
					}
					else	
						if( estado == null )
						{
							toxicosForm.reset();

							if( logger.isDebugEnabled() )
							{
								logger.debug("estado no valido dentro del flujo de valoración (null) ");
							}

							if( con != null && !con.isClosed() )
								UtilidadBD.closeConnection(con);

							request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
							return mapping.findForward("descripcionError");				
						}
						else
							if( !UtilidadValidacion.esProfesionalSalud(usuario) )
							{
								toxicosForm.reset();

								if( con != null && !con.isClosed() )
									UtilidadBD.closeConnection(con);

								logger.warn("El usuario no tiene permisos de acceder a esta funcionalidad "+usuario.getLoginUsuario());				
								request.setAttribute("codigoDescripcionError", "errors.usuario.noAutorizado");
								return mapping.findForward("paginaError");								
							}
							else
								if( estado.equals("cargar") )
								{
									toxicosForm.reset();
									AntecedentesToxicos toxicos = new AntecedentesToxicos();
									toxicos.setPaciente(paciente);

									toxicos.cargar(con);				
									cargarForm(toxicos, toxicosForm);

									if( con != null && !con.isClosed() )
										UtilidadBD.closeConnection(con);

									return mapping.findForward("paginaPrincipal");
								}
								else
									if( estado.equals("resumen") )
									{
										toxicosForm.reset();
										AntecedentesToxicos toxicos = new AntecedentesToxicos();
										toxicos.setPaciente(paciente);

										toxicos.cargar(con);				
										cargarForm(toxicos, toxicosForm);				

										if( con != null && !con.isClosed() )
											UtilidadBD.closeConnection(con);

										return mapping.findForward("paginaResumen");
									}
									else
										if(estado.equals("imprimir"))
										{
											toxicosForm.reset();
											AntecedentesToxicos toxicos = new AntecedentesToxicos();
											toxicos.setPaciente(paciente);

											toxicos.cargar(con);				
											cargarForm(toxicos, toxicosForm);

											//Carga la informacion del paciente para la impresion
											toxicosForm.setEncabezadoImpresion(ImpresionResumenAtenciones.obtenerEncabezadoPaciente(con,paciente.getConsecutivoIngreso()));			

											if( con != null && !con.isClosed() )
												UtilidadBD.closeConnection(con);			

											return mapping.findForward("paginaImprimir");
										}
										else
											if(estado.equals("finalizar"))
											{
												toxicosForm.setOperacionExistosa(ConstantesBD.acronimoNo);
												AntecedentesToxicos toxicos = new AntecedentesToxicos();
												toxicos.setPaciente(paciente);

												cargarObjeto(toxicos, toxicosForm);

												ResultadoBoolean resultado = toxicos.updateTransaccional(con);

												if(resultado.isTrue())
												{
													toxicosForm.reset();				
													toxicos.reset();
													toxicos.setPaciente(paciente);				

													toxicos.cargar(con);
													cargarForm(toxicos, toxicosForm);					

													if( con != null && !con.isClosed() )
														UtilidadBD.closeConnection(con);

													String paginaSiguiente = request.getParameter("paginaSiguiente");
													toxicosForm.setOperacionExistosa(ConstantesBD.acronimoSi);

													if( paginaSiguiente != null && !paginaSiguiente.equals("") )
													{
														UtilidadBD.closeConnection(con);
														response.sendRedirect(paginaSiguiente);
													}

													return mapping.findForward("paginaPrincipal");					
												}
												else
												{
													toxicosForm.reset();

													if( con != null && !con.isClosed() )
														UtilidadBD.closeConnection(con);

													logger.warn("No se pudo hacer la inserción de los antecedentes se presenta el siguiente error : "+resultado.getDescripcion());				
													request.setAttribute("descripcionError", "No se pudo hacer la inserción de los antecedentes se presenta el siguiente error : "+resultado.getDescripcion());
													return mapping.findForward("paginaError");													
												}								
											}			
											else
												if( estado.equals("adicionarOtroToxico") )
												{
													toxicosForm.setNumAntecedentesToxicosOtros(toxicosForm.getNumAntecedentesToxicosOtros()+1);				

													if( con != null && !con.isClosed() )
														UtilidadBD.closeConnection(con);

													return mapping.findForward("paginaPrincipal");				
												}			
												else
													if( estado.equals("cancelar") )
													{
														String paginaSiguiente=request.getParameter("paginaSiguiente");
														toxicosForm.reset();

														if( con != null && !con.isClosed() )
														{
															UtilidadBD.closeConnection(con);
														}
														response.sendRedirect(paginaSiguiente);
														return null; 
													}
													else
													{
														logger.error("Se intento acceder con un estado invalido "+usuario.getLoginUsuario()+" Estado --> "+estado);
														request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");

														if( con != null && !con.isClosed() )
															UtilidadBD.closeConnection(con);

														return mapping.findForward("paginaError");																	
													}			
			}									
			else
			{
				logger.error("El form no coincide con el esperado ");
				request.setAttribute("codigoDescripcionError", "errors.formaTipoInvalido");
				return mapping.findForward("paginaError");													
			}
		} catch (Exception e) {
			Log4JManager.error(e);
			return null;
		}
		finally{
			UtilidadBD.closeConnection(con);
		}
	}
	
	private void cargarObjeto(AntecedentesToxicos toxicos, AntecedentesToxicosForm toxicosForm)
	{		
		toxicos.setObservaciones(cargarObservaciones(toxicosForm.getObservaciones(), toxicosForm.getObservacionesAnteriores()));
		
		int tam = toxicosForm.getNumAntecedentesToxicos() + toxicosForm.getNumPredefinidos();		
				
		for( int i=1; i<=tam; i++)
		{
			String codigoStr = (String)toxicosForm.getAntecedenteToxico("codTipoAnt_"+i);			
			int codTipoAnt = 0;			
			if( UtilidadCadena.noEsVacio(codigoStr) )
				codTipoAnt = Integer.parseInt(codigoStr);
			
			codigoStr = (String)toxicosForm.getAntecedenteToxico("codigo_"+i);	
			int codigo = 0;			
			if( UtilidadCadena.noEsVacio(codigoStr) )
				codigo = Integer.parseInt(codigoStr);	
			
			String habitoActual = (String)toxicosForm.getAntecedenteToxico("actual_"+i);			
			boolean actual = false;
			if( habitoActual != null && habitoActual.equals("true") )
				actual = true;
			
			String tiempo = (String)toxicosForm.getAntecedenteToxico("tiempo_"+i);
			String grabadoBDTiempo = (String)toxicosForm.getAntecedenteToxico("t_grabadoBD_"+i);
			String cantidad = (String)toxicosForm.getAntecedenteToxico("cantidad_"+i);
			String grabadoBDCantidad = (String)toxicosForm.getAntecedenteToxico("c_grabadoBD_"+i);
			String frecuencia = (String)toxicosForm.getAntecedenteToxico("frecuencia_"+i);
			String grabadoBDFrecuencia = (String)toxicosForm.getAntecedenteToxico("f_grabadoBD_"+i);
			String observaciones = (String)toxicosForm.getAntecedenteToxico("observaciones_"+i);
			String observacionesAnt =(String)toxicosForm.getAntecedenteToxico("observacionesAnt_"+i);
			String grabadoBDAnt = 	(String)toxicosForm.getAntecedenteToxico("antecedenteGrabado_"+i);
			
			if( UtilidadCadena.noEsVacio(habitoActual) || UtilidadCadena.noEsVacio(tiempo) || UtilidadCadena.noEsVacio(cantidad) || UtilidadCadena.noEsVacio(frecuencia) || UtilidadCadena.noEsVacio(observaciones) )
			{
				toxicos.addAntecedenteToxicoPredefinido(this.cargarAntecedenteToxico(codigo,
																																		codTipoAnt,
																																		"",
																																		actual,
																																		cantidad,
																																		frecuencia,
																																		tiempo,
																																		observaciones,
																																		observacionesAnt,
																																		grabadoBDTiempo,
																																		grabadoBDCantidad,
																																		grabadoBDFrecuencia,
																																		grabadoBDAnt));
			}			
		}
		
		tam = toxicosForm.getNumAntecedentesToxicosOtros();
		
		for( int i=1; i<=tam; i++ )
		{
			String codigoStr = (String)toxicosForm.getAntecedenteToxicoOtro("codigo_"+i);	
			
			int codigo = 0;			
			if( UtilidadCadena.noEsVacio(codigoStr) )
				codigo = Integer.parseInt(codigoStr);
			else
				codigo = i;	
				
			String nombre = (String)toxicosForm.getAntecedenteToxicoOtro("nombre_"+i);
			String habitoActual = (String)toxicosForm.getAntecedenteToxicoOtro("actual_"+i);
			boolean actual = false;
			if( habitoActual != null && habitoActual.equals("true") )
				actual = true;			
			String tiempo = (String)toxicosForm.getAntecedenteToxicoOtro("tiempo_"+i);
			String grabadoBDTiempo = (String)toxicosForm.getAntecedenteToxicoOtro("t_grabadoBD_"+i);
			String cantidad = (String)toxicosForm.getAntecedenteToxicoOtro("cantidad_"+i);
			String grabadoBDCantidad = (String)toxicosForm.getAntecedenteToxicoOtro("c_grabadoBD_"+i);
			String frecuencia = (String)toxicosForm.getAntecedenteToxicoOtro("frecuencia_"+i);
			String grabadoBDFrecuencia = (String)toxicosForm.getAntecedenteToxicoOtro("f_grabadoBD_"+i);
			String observaciones = (String)toxicosForm.getAntecedenteToxicoOtro("observaciones_"+i);
			String observacionesAnt =(String)toxicosForm.getAntecedenteToxicoOtro("observacionesAnt_"+i);
			String grabadoBDAnt = 	(String)toxicosForm.getAntecedenteToxicoOtro("antecedenteGrabado_"+i);
			
			if( UtilidadCadena.noEsVacio(nombre) )
			{ 
				if( UtilidadCadena.noEsVacio(habitoActual) || UtilidadCadena.noEsVacio(tiempo) || UtilidadCadena.noEsVacio(cantidad) || UtilidadCadena.noEsVacio(frecuencia) || UtilidadCadena.noEsVacio(observaciones) )
				{
					toxicos.addAntecedenteToxicoAdicional(this.cargarAntecedenteToxico(codigo,
																																		0,
																																		nombre,
																																		actual,
																																		cantidad,
																																		frecuencia,
																																		tiempo,
																																		observaciones,
																																		observacionesAnt,
																																		grabadoBDTiempo,
																																		grabadoBDCantidad,
																																		grabadoBDFrecuencia,
																																		grabadoBDAnt));
				}		
			}
		}
	}
	
	
	/**
	 * 
	 * @param codigo
	 * @param codTipoPredefinido
	 * @param nombre
	 * @param habitoActual
	 * @param cantidad
	 * @param frecuencia
	 * @param tiempo
	 * @param observaciones
	 * @param observacionesAnt
	 * @param grabadoBDTiempo
	 * @param grabadoBDCantidad
	 * @param grabadoBDFrecuencia
	 * @param grabadoBDAnt
	 * @return
	 *
	 */
	private AntecedenteToxico cargarAntecedenteToxico(int codigo,
																							int codTipoPredefinido,
																							String nombre,
																							boolean habitoActual,
																							String cantidad,
																							String frecuencia,
																							String tiempo,
																							String observaciones,
																							String observacionesAnt,
																							String grabadoBDTiempo,
																							String grabadoBDCantidad,
																							String grabadoBDFrecuencia,
																							String grabadoBDAnt)																

	{
		AntecedenteToxico antToxico = new AntecedenteToxico();
		
		antToxico.setCodigo(codigo);
		antToxico.setCodigoTipoPredefinido(codTipoPredefinido);
		antToxico.setNombre(nombre);
		antToxico.setActual(habitoActual);
		antToxico.setCantidad(cantidad);
		antToxico.setFrecuencia(frecuencia);
		antToxico.setTiempoHabito(tiempo);
			
		antToxico.setObservaciones(this.cargarObservacionesAntToxico(	observaciones,
																														observacionesAnt, 
																														tiempo,
																														cantidad,
																														frecuencia,
																														grabadoBDTiempo,
																														grabadoBDCantidad,
																														grabadoBDFrecuencia,
																														grabadoBDAnt));				
		return antToxico;
	}

	
	
	/**
	 * Se le da formato a las observaciones generales. Es decir, se pegan a las
	 * anteriores, en caso que existan, se adiciona la fecha y hora de
	 * grabación, la cadena con las observaciones escritas y el medico que las
	 * ingresa con toda su información.
	 * @param 	String, nuevas. Nuevas observaciones.
	 * @param 	String, anteriores. Observaciones previamente ingresadas.
	 * @return 		String, cadena con las observaciones a guardar.
	 */
	private String cargarObservaciones(String nuevas, String anteriores)
	{
	  ///si no se editaron nuevas observaciones no se hace nada
		if(nuevas.equals(""))
			return anteriores;
		else
			return UtilidadTexto.agregarTextoAObservacion(anteriores, nuevas, usuario, true);
	}
	
	
	
	/**
	 * 
	 * @param toxicos
	 * @param toxicosForm
	 *
	 */
	@SuppressWarnings("rawtypes")
	private void cargarForm(AntecedentesToxicos toxicos, AntecedentesToxicosForm toxicosForm)
	{
		if( toxicos.getObservaciones() != null )
		{
			if( toxicosForm.getEstado().equals("resumen") )
			{
				String observaciones = toxicos.getObservaciones();
				observaciones = observaciones.replaceAll("\n", "<br>");
				toxicosForm.setObservacionesAnteriores(observaciones);
			}
			else
			{
				String observaciones = toxicos.getObservaciones().replaceAll("<br>", "\n");
				toxicosForm.setObservacionesAnteriores(observaciones);
			}
		}		
		
		//		TOXICOS PREDEFINIDOS		
		ArrayList antecedentes = toxicos.getAntecedentesToxicosPredefinidos();
		int tam = antecedentes.size();
		
		toxicosForm.setNumAntecedentesToxicos(tam);		
		
		for(int i=0; i<tam; i++)
		{ 
			AntecedenteToxico antToxico = (AntecedenteToxico) antecedentes.get(i);
			cargarMapToxicosPredefinidos(antToxico, toxicosForm, i+1);  
		}
		
		//		TOXICOS OTROS
		antecedentes = toxicos.getAntecedentesToxicosAdicionales();
		tam = antecedentes.size();
		
		toxicosForm.setNumAntecedentesToxicosOtros(tam);
		toxicosForm.setNumOtrosNuevos(tam);	
		
		for(int i=0; i<tam; i++)
		{ 
			AntecedenteToxico antToxico = (AntecedenteToxico) antecedentes.get(i);
			cargarMapToxicosOtros(antToxico, toxicosForm, i+1);  
		}		
	}
	
	
	/**
	 * @param toxico
	 * @param toxicoForm
	 * @param indice
	 */
	private void cargarMapToxicosPredefinidos(	AntecedenteToxico toxico, AntecedentesToxicosForm toxicoForm, int indice)
	{
		toxicoForm.setAntecedenteToxico("codigo_"+indice, ""+toxico.getCodigo());
		
		//indice = toxico.getCodigo();
		
		toxicoForm.setAntecedenteToxico("nombre_"+indice, toxico.getNombre());
		
		toxicoForm.setAntecedenteToxico("actual_"+indice, ""+toxico.isActual());
		
		toxicoForm.setAntecedenteToxico("tiempo_"+indice, toxico.getTiempoHabito());
		if( UtilidadCadena.noEsVacio(toxico.getTiempoHabito()) )
			toxicoForm.setAntecedenteToxico("t_grabadoBD_"+indice, "true");
		
		toxicoForm.setAntecedenteToxico("cantidad_"+indice, toxico.getCantidad());
		if( UtilidadCadena.noEsVacio(toxico.getCantidad()) )
			toxicoForm.setAntecedenteToxico("c_grabadoBD_"+indice, "true");
		
		toxicoForm.setAntecedenteToxico("frecuencia_"+indice, toxico.getFrecuencia());
		if( UtilidadCadena.noEsVacio(toxico.getFrecuencia()) )
			toxicoForm.setAntecedenteToxico("f_grabadoBD_"+indice, "true");
		
		if( UtilidadCadena.noEsVacio(toxico.getObservaciones()) )
		{
			String observaciones = toxico.getObservaciones().replaceAll("\n", "<br>");
			toxicoForm.setAntecedenteToxico("observacionesAnt_"+indice, observaciones);
		}
		
		toxicoForm.setAntecedenteToxico("fechaGrabacion_"+indice, UtilidadFecha.conversionFormatoFechaAAp(toxico.getFechaGrabacion()));
		toxicoForm.setAntecedenteToxico("horaGrabacion_"+indice, UtilidadFecha.convertirHoraACincoCaracteres(toxico.getHoraGrabacion()));
		
		toxicoForm.setAntecedenteToxico("antecedenteGrabado_"+indice, "true");
	}	
	
	
	/**
	 * 
	 * @param toxico
	 * @param toxicoForm
	 * @param indice
	 *
	 */
	private void cargarMapToxicosOtros(	AntecedenteToxico toxico, AntecedentesToxicosForm toxicoForm, int indice)
	{
		toxicoForm.setAntecedenteToxicoOtro("codigo_"+indice, ""+toxico.getCodigo());
			
		//indice = toxico.getCodigo();
			
		toxicoForm.setAntecedenteToxicoOtro("nombre_"+indice, toxico.getNombre());
			
		toxicoForm.setAntecedenteToxicoOtro("actual_"+indice, ""+toxico.isActual());
			
		toxicoForm.setAntecedenteToxicoOtro("tiempo_"+indice, toxico.getTiempoHabito());
		if( UtilidadCadena.noEsVacio(toxico.getTiempoHabito()) )
			toxicoForm.setAntecedenteToxicoOtro("t_grabadoBD_"+indice, "true");
			
		toxicoForm.setAntecedenteToxicoOtro("cantidad_"+indice, toxico.getCantidad());
		if( UtilidadCadena.noEsVacio(toxico.getCantidad()) )
			toxicoForm.setAntecedenteToxicoOtro("c_grabadoBD_"+indice, "true");
			
		toxicoForm.setAntecedenteToxicoOtro("frecuencia_"+indice, toxico.getFrecuencia());
		if( UtilidadCadena.noEsVacio(toxico.getFrecuencia()) )
			toxicoForm.setAntecedenteToxicoOtro("f_grabadoBD_"+indice, "true");
			
		if( UtilidadCadena.noEsVacio(toxico.getObservaciones()) )
		{
			String observaciones = toxico.getObservaciones().replaceAll("\n", "<br>");
			toxicoForm.setAntecedenteToxicoOtro("observacionesAnt_"+indice, observaciones);
		}
		
		toxicoForm.setAntecedenteToxicoOtro("fechaGrabacion_"+indice, UtilidadFecha.conversionFormatoFechaAAp(toxico.getFechaGrabacion()));
		toxicoForm.setAntecedenteToxicoOtro("horaGrabacion_"+indice, UtilidadFecha.convertirHoraACincoCaracteres(toxico.getHoraGrabacion()));
		
		toxicoForm.setAntecedenteToxicoOtro("antecedenteGrabado_"+indice, "true");				
	}	
	
	
	/**
	 * @param nuevas
	 * @param anteriores
	 * @param tiempo
	 * @param cantidad
	 * @param frecuencia
	 * @param grabadoBDTiempo
	 * @param grabadoBDCantidad
	 * @param grabadoBDFrecuencia
	 * @param grabadoBDAnt
	 * @return
	 *
	 */
	private String cargarObservacionesAntToxico(String nuevas, String anteriores, String tiempo,
																					String cantidad,
																					String frecuencia,																					
																					String grabadoBDTiempo,
																					String grabadoBDCantidad,
																					String grabadoBDFrecuencia,
																					String grabadoBDAnt)																				
	{
		String observaciones = "";

	    if( UtilidadCadena.noEsVacio(anteriores))
	        observaciones=anteriores;

		if( UtilidadCadena.noEsVacio(nuevas))
		    nuevas="<b>"+nuevas+"</b>";
	
		if( !UtilidadCadena.noEsVacio(grabadoBDAnt) )
		    nuevas += "\nSe grabó el antecedente.";		 	

		if( UtilidadCadena.noEsVacio(tiempo) && !UtilidadCadena.noEsVacio(grabadoBDTiempo) )
		    nuevas += "\nSe grabó información en el campo \"Tiempo\" del antecedente";
		if( UtilidadCadena.noEsVacio(cantidad) && !UtilidadCadena.noEsVacio(grabadoBDCantidad) )
		    nuevas += "\nSe grabó información en el campo \"Cantidad\" del antecedente";
		if( UtilidadCadena.noEsVacio(frecuencia) && !UtilidadCadena.noEsVacio(grabadoBDFrecuencia) )
		    nuevas += "\nSe grabó información en el campo \"Frecuencia\" del antecedente";
		
		if(UtilidadCadena.noEsVacio(nuevas))
		    observaciones=UtilidadTexto.agregarTextoAObservacion(observaciones, nuevas, usuario, true);
		
		return observaciones;	
	}
}