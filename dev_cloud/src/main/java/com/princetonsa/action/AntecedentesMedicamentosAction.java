/*
 * @(#)AntecedentesMedicamentosAction.java
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
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.SimpleTimeZone;

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
import util.UtilidadTexto;
import util.UtilidadValidacion;

import com.princetonsa.actionform.AntecedentesMedicamentosForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.antecedentes.AntecedenteMedicamento;
import com.princetonsa.mundo.antecedentes.AntecedentesMedicamentos;

/**
 * Action, controla todo el módulo de antecedentes medicamentos
 * @version 1.0, Agosto 26, 2003
 * @author <a href="mailto:Liliana@PrincetonSA.com">Liliana Caballero</a>,
 * @see org.apache.struts.action.ActionForm#validate(ActionMapping,
 * HttpServletRequest)
 */
public class AntecedentesMedicamentosAction extends Action
{
	/**
	 * Para hacer logs de está funcionalidad.
	 */
	private Logger logger = Logger.getLogger(AntecedentesMedicamentosAction.class);
	
	/**
	 * Usuario que está trabajando actualmente en el sistema.
	 */
	private UsuarioBasico usuario;
	
	/**
	 * Método encargado de el flujo y control de la funcionalidad
	 * 
	 * @see org.apache.struts.action.Action#execute(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse)
	 */
	public ActionForward execute(	ActionMapping mapping, 
														ActionForm form, 
														HttpServletRequest request,
														HttpServletResponse response) throws Exception
														{

		Connection con = null;

		try {
			if( form instanceof AntecedentesMedicamentosForm )
			{
				AntecedentesMedicamentosForm medicamentosForm = (AntecedentesMedicamentosForm)form;
				String estado = medicamentosForm.getEstado();

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

					medicamentosForm.reset();

					logger.warn("Problemas con la base de datos "+e);
					request.setAttribute("codigoDescripcionError", "errors.problemasBd");
					return mapping.findForward("paginaError");
				}

				HttpSession session=request.getSession();			
				PersonaBasica paciente = (PersonaBasica)session.getAttribute("pacienteActivo");
				usuario = (UsuarioBasico)session.getAttribute("usuarioBasico");
				medicamentosForm.setOperacionExistosa(ConstantesBD.acronimoNo);	

				logger.info("\n\nEstado AntecedentesMedicamentos >> "+estado+"\n\n");

				if( usuario == null )
				{
					if( logger.isDebugEnabled() )
					{
						logger.debug("Usuario no válido (null)");
					}

					if( con != null && !con.isClosed() )
						UtilidadBD.closeConnection(con);

					medicamentosForm.reset();					

					request.setAttribute("codigoDescripcionError", "errors.usuario.noCargado");
					return mapping.findForward("paginaError");				
				}
				else
					if( paciente == null || paciente.getTipoIdentificacionPersona().equals("") )
					{
						if( logger.isDebugEnabled() )
						{
							logger.debug("El paciente no es válido (null)");
						}

						if( con != null && !con.isClosed() )
							UtilidadBD.closeConnection(con);

						medicamentosForm.reset();					

						request.setAttribute("codigoDescripcionError", "errors.paciente.noCargado");
						return mapping.findForward("paginaError");				
					}
					else	
						if( estado == null )
						{
							if( logger.isDebugEnabled() )
							{
								logger.debug("estado no valido dentro del flujo de valoración (null) ");
							}

							if( con != null && !con.isClosed() )
								UtilidadBD.closeConnection(con);

							medicamentosForm.reset();

							request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
							return mapping.findForward("descripcionError");				
						}
						else
							if( !UtilidadValidacion.esProfesionalSalud(usuario) )
							{
								if( con != null && !con.isClosed() )
									UtilidadBD.closeConnection(con);

								medicamentosForm.reset();

								logger.warn("El usuario no tiene permisos de acceder a esta funcionalidad "+usuario.getLoginUsuario());				
								request.setAttribute("codigoDescripcionError", "errors.usuario.noAutorizado");
								return mapping.findForward("paginaError");								
							}	
							else	
								if( estado.equals("adicionarMedicamento") || estado.equals("adicionarMedicamentoSalida"))
								{
									medicamentosForm.setNumMedicamentos(medicamentosForm.getNumMedicamentos()+1);

									if( con != null && !con.isClosed() )
										UtilidadBD.closeConnection(con);

									return mapping.findForward("medicamentosPpal");
								}
								else
									if( estado.equals("modificarMedicamento") )
									{
										if( con != null && !con.isClosed() )
											UtilidadBD.closeConnection(con);

										return mapping.findForward("medicamentosPpal");				
									}
									else
										if(estado.equals("cargarArt"))
										{
											return this.accionconsultarM(medicamentosForm, con, mapping, usuario);
										}			
										else
											if( estado.equals("cargar") )
											{
												medicamentosForm.reset();
												AntecedentesMedicamentos medicamentos = new AntecedentesMedicamentos();

												medicamentos.cargar(con, paciente.getCodigoPersona());

												cargarForm(medicamentos, medicamentosForm);

												if( con != null && !con.isClosed() )
													UtilidadBD.closeConnection(con);

												return mapping.findForward("medicamentosPpal");
											}
											else
												if( estado.equals("resumen") )
												{
													medicamentosForm.reset();
													AntecedentesMedicamentos medicamentos = new AntecedentesMedicamentos();

													medicamentos.cargar(con, paciente.getCodigoPersona());

													cargarForm(medicamentos, medicamentosForm);

													if( con != null && !con.isClosed() )
														UtilidadBD.closeConnection(con);

													return mapping.findForward("resumenMedicamentos");
												}
												else
													if( estado.equals("finalizar") )
													{
														medicamentosForm.setOperacionExistosa(ConstantesBD.acronimoNo);				
														AntecedentesMedicamentos medicamentos = new AntecedentesMedicamentos();

														cargarObjeto(medicamentos, medicamentosForm, usuario);

														ResultadoBoolean resultado = medicamentos.updateTransaccional(con, paciente.getCodigoPersona());

														if( resultado.isTrue() )
														{
															medicamentosForm.reset();
															medicamentos.reset();

															String paginaSiguiente=request.getParameter("paginaSiguiente");

															if( paginaSiguiente != null && !paginaSiguiente.equals("") )
															{
																UtilidadBD.closeConnection(con);
																medicamentosForm.setOperacionExistosa(ConstantesBD.acronimoSi);
																response.sendRedirect(paginaSiguiente);
															}

															medicamentos.cargar(con, paciente.getCodigoPersona());
															cargarForm(medicamentos, medicamentosForm);

															if( con != null && !con.isClosed() )
																UtilidadBD.closeConnection(con);

															medicamentosForm.setOperacionExistosa(ConstantesBD.acronimoSi);
															return mapping.findForward("medicamentosPpal");					
														}
														else
														{
															medicamentosForm.setOperacionExistosa(ConstantesBD.acronimoNo);
															logger.error("No se pudo hacer la inserción de los antecedentes se presentó el siguiente error : "+resultado.getDescripcion());
															request.setAttribute("descripcionError", "No se pudo hacer la inserción se presentó el siguiente error : "+resultado.getDescripcion());

															if( con != null && !con.isClosed() )
																UtilidadBD.closeConnection(con);

															return mapping.findForward("paginaError");																	
														}
													}
													else
														if( estado.equals("cancelar") )
														{
															String paginaSiguiente=request.getParameter("paginaSiguiente");
															medicamentosForm.reset();

															if( con != null && !con.isClosed() )
															{
																UtilidadBD.closeConnection(con);
															}
															response.sendRedirect(paginaSiguiente);
														}			
														else
														{
															logger.error("Se intento acceder con un estado inválido "+usuario.getLoginUsuario()+" Estado --> "+estado);
															request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");

															if( con != null && !con.isClosed() )
																UtilidadBD.closeConnection(con);

															return mapping.findForward("paginaError");																	
														}
				return null;
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
	
	
	/**
	 * 
	 */
	@SuppressWarnings("static-access")
	private ActionForward accionconsultarM(AntecedentesMedicamentosForm medicamentosForm, Connection con, ActionMapping mapping, UsuarioBasico usuario)
	{
		AntecedentesMedicamentos medicamentos = new AntecedentesMedicamentos();
		medicamentosForm.setFormaFconcMap(medicamentos.consultaFormaConc(con, Integer.parseInt(medicamentosForm.getInformacionArtPrin("codigoArticulo").toString())));
		UtilidadBD.closeConnection(con);
		return mapping.findForward("medicamentosPpal");
	}
	
	
	/**
	 * Carga el form (bean) con la información pertinente contenida en el objeto
	 * @param 	AntecedentesMedicamentos, medicamentos
	 * @param 	AntecedentesMedicamentosForm, medicamentosForm
	 */
	public void cargarForm(AntecedentesMedicamentos medicamentos, AntecedentesMedicamentosForm medicamentosForm)
	{
		if( medicamentos.getObservaciones() != null )
		{
			if( medicamentosForm.getEstado().equals("resumen") )
				medicamentosForm.setObservacionesAnteriores(medicamentos.getObservaciones().replaceAll("\n", "<br>"));
			else
				medicamentosForm.setObservacionesAnteriores(medicamentos.getObservaciones().replaceAll("<br>", "\n"));	
		}

		
		int tam = medicamentos.getMedicamentos().size();
		
		medicamentosForm.setNumMedicamentos(tam);
		medicamentosForm.setNumMedicamentosBD(tam);
		
		logger.debug("El número de medicamentos es : (desde el mundo)"+tam);
	
		for(int i =1; i<=tam; i++)
		{
			AntecedenteMedicamento medicamento = medicamentos.getMedicamento(i-1);
			
			medicamentosForm.setMedicamento("nombre_"+i, medicamento.getNombre());			
			if( noVacio(medicamento.getNombre()) )
				medicamentosForm.setMedicamento("n_grabadoBD_"+i, "true");
			
			medicamentosForm.setMedicamento("codigo_"+i, medicamento.getCodigoA());			
			if( noVacio(medicamento.getCodigoA()))
				medicamentosForm.setMedicamento("ca_grabadoBD_"+i, "true");
			
			medicamentosForm.setMedicamento("dosis_"+i, medicamento.getDosis());
			if( noVacio(medicamento.getDosis()) )
				medicamentosForm.setMedicamento("d_grabadoBD_"+i, "true");				
			
			medicamentosForm.setMedicamento("frecuencia_"+i, medicamento.getFrecuencia());
			if( noVacio(medicamento.getFrecuencia()) )
				medicamentosForm.setMedicamento("fr_grabadoBD_"+i, "true");
			
			medicamentosForm.setMedicamento("tipofrecuencia_"+i, medicamento.getTipoFrecuencia());
			if( noVacio(medicamento.getTipoFrecuencia()) )
				medicamentosForm.setMedicamento("tfr_grabadoBD_"+i, "true");				

			
			medicamentosForm.setMedicamento("unidosis_"+i, medicamento.getUnidosis());
			if( noVacio(medicamento.getUnidosis()) )
				medicamentosForm.setMedicamento("uni_grabadoBD_"+i, "true");				
			
			
			medicamentosForm.setMedicamento("fechaInicio_"+i, medicamento.getFechaInicio());
			if( noVacio(medicamento.getFechaInicio()) )
				medicamentosForm.setMedicamento("fi_grabadoBD_"+i, "true");
			
			medicamentosForm.setMedicamento("fechaFin_"+i, medicamento.getFechaFin());
			if( noVacio(medicamento.getFechaFin()) )
				medicamentosForm.setMedicamento("ff_grabadoBD_"+i, "true");

			medicamentosForm.setMedicamento("tiempot_"+i, medicamento.getTiempoT());
			if( noVacio(medicamento.getTiempoT()) )
				medicamentosForm.setMedicamento("tt_grabadoBD_"+i, "true");

			medicamentosForm.setMedicamento("dosisd_"+i, medicamento.getDosisD());
			if( noVacio(medicamento.getDosisD()) )
				medicamentosForm.setMedicamento("dd_grabadoBD_"+i, "true");

			if(medicamento.getCantidad() != null)
				medicamentosForm.setMedicamento("cantidad_"+i, medicamento.getCantidad());
			else
				medicamentosForm.setMedicamento("cantidad_"+i, "0");
			
			if( noVacio(medicamento.getCantidad()) )
				medicamentosForm.setMedicamento("c_grabadoBD_"+i, "true");

			
			medicamentosForm.setMedicamento("observacionesAnteriores_"+i, medicamento.getObservaciones());

			logger.info("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>");
			logger.info("cantidades: " + medicamento.getCantidad());
			logger.info("dosis: " + medicamento.getDosis());
			logger.info("dosis d: " + medicamento.getDosisD());
			logger.info("Unidosis: " + medicamento.getUnidosis() );
			logger.info("Frecuencia: " + medicamento.getFrecuencia() );
			logger.info("Tipo Frecuencia : " + medicamento.getTipoFrecuencia() );
		}		
	}
	
	/**
	 * Carga en el objeto la información pertinente captada en el form
	 * @param 	AntecedentesMedicamentos, antecedentesMedicamentos
	 * @param 	AntecedentesMedicamentosForm, medicamentosForm
	 */
	@SuppressWarnings("rawtypes")
	public void cargarObjeto(AntecedentesMedicamentos antecedentesMedicamentos, AntecedentesMedicamentosForm medicamentosForm, UsuarioBasico medico)
	{
		antecedentesMedicamentos.setObservaciones(UtilidadTexto.agregarTextoAObservacion(medicamentosForm.getObservacionesAnteriores(), medicamentosForm.getObservacionesNuevas(), medico, true));
		
		
		int tam = medicamentosForm.getNumMedicamentos();
		
		antecedentesMedicamentos.setMedicamentos(new ArrayList());
		
		logger.debug("El número de medicamentos es : (desde el form)"+tam);
				
		for(int i=1; i<=tam; i++)
		{
			String codigoA = (String)medicamentosForm.getMedicamento("codigo_"+i);
			String nombre = (String)medicamentosForm.getMedicamento("nombre_"+i);
			String dosis = (String)medicamentosForm.getMedicamento("dosis_"+i);
			String frecuencia = (String)medicamentosForm.getMedicamento("frecuencia_"+i);
			String fechaInicio = (String)medicamentosForm.getMedicamento("fechaInicio_"+i);
			String fechaFin = (String)medicamentosForm.getMedicamento("fechaFin_"+i);
			String tiempoT = (String)medicamentosForm.getMedicamento("tiempot_"+i);
			String dosisD = (String)medicamentosForm.getMedicamento("dosisd_"+i);
			String cantidad = (String)medicamentosForm.getMedicamento("cantidad_"+i);

			String tipofrecuencia = (String)medicamentosForm.getMedicamento("tipofrecuencia_"+i);
			String unidosis = (String)medicamentosForm.getMedicamento("unidosis_"+i);

			String observaciones = cargarObservacionesMedicamento((String)medicamentosForm.getMedicamento("observaciones_"+i),
																						(String)medicamentosForm.getMedicamento("observacionesAnteriores_"+i),
																						dosis,
																						frecuencia,
																						fechaInicio,
																						fechaFin,
																						tiempoT,
																						dosisD,
																						cantidad,
																						(String)medicamentosForm.getMedicamento("n_grabadoBD_"+i),
																						(String)medicamentosForm.getMedicamento("d_grabadoBD_"+i),
																						(String)medicamentosForm.getMedicamento("fr_grabadoBD_"+i),
																						(String)medicamentosForm.getMedicamento("fi_grabadoBD_"+i),
																						(String)medicamentosForm.getMedicamento("ff_grabadoBD_"+i),
																						(String)medicamentosForm.getMedicamento("tt_grabadoBD_"+i),
																						(String)medicamentosForm.getMedicamento("dd_grabadoBD_"+i),
																						(String)medicamentosForm.getMedicamento("c_grabadoBD_"+i),
																						tipofrecuencia,
																						unidosis,
																						(String)medicamentosForm.getMedicamento("tfr_grabadoBD_"+i),
																						(String)medicamentosForm.getMedicamento("uni_grabadoBD_"+i)
																						);																						
		
			if( noVacio(nombre) || noVacio(dosis) || noVacio(frecuencia) || noVacio(fechaInicio) || noVacio(fechaFin) )
			{
				antecedentesMedicamentos.setMedicamento(new AntecedenteMedicamento(i, codigoA, nombre, dosis, frecuencia, fechaInicio, fechaFin, observaciones,cantidad,dosisD,tiempoT, tipofrecuencia, unidosis));				
			}					
		}
	}
	
	/**
	 * Las observaciones de cada antecedente a medicamento tienen un formato y
	 * una información especial, este método es el encargado de dados unos datos
	 * de entrada, producir de forma adecuada las observaciones para el
	 * antecedente médico.
	 * @param 	String, nuevas. Observaciones nuevas
	 * @param 	String, anteriores. Observaciones previamente ingresadas.
	 * @param 	String, dosis. Dosis del medicamento.
	 * @param 	String, frecuencia. Frecuencia del medicamento.
	 * @param 	String, fechaInic. Fecha en la que se empezo a tomar el medicamento
	 * @param 	String, fechaFin. Fecha en la que se deja de tomar el medicamento
	 * @param 	String, grabadoBDNombre. Si se ha grabado previamente el
	 * medicamento
	 * @param 	String, grabadoBDDosis. Si se ha grabado previamente la dosis
	 * del medicamento
	 * @param 	String, grabadoBDFrecuencia. Si se ha grabado previamente la
	 * frecuencia.
	 * @param 	String, grabadoBDFechaInic. Si se ha grabado previamente la
	 * fecha de inicio.
	 * @param 	String, grabadoBDFechaFin. Si se ha grabado previamente la fecha
	 * de finalización.
	 * @return 		String, cadena con las observaciones a guardar.
	 */
	public String cargarObservacionesMedicamento(	String nuevas, 
																String anteriores, 
																String dosis, 
																String frecuencia, 
																String fechaInic, 
																String fechaFin,
																String tiempoT,
																String dosisD,
																String cantidad,
																String grabadoBDNombre,
																String grabadoBDDosis,
																String grabadoBDFrecuencia,
																String grabadoBDFechaInic,
																String grabadoBDFechaFin,
																String grabadoBDTiempoT,
																String grabadoBDDosisD,
																String grabadoBDCantidad,
																String tipofrecuencia,
																String unidosis,
																String grabadoBDTipoFrecuencia,
																String grabadoBDUnidosis
																)
	{
	    String observaciones="";
	    if( noVacio(anteriores))
	        observaciones=anteriores;
	    
		if( noVacio(nuevas) )
			nuevas="<b>"+nuevas+"</b>";
		
		if( !noVacio(grabadoBDNombre) )
		    nuevas += "\nSe grabó el antecedente.";			
	
		if( noVacio(dosis) && !noVacio(grabadoBDDosis) )
		    nuevas += "\nSe grabó información en el campo \"Dosis\" del antecedente";
		if( noVacio(frecuencia) && !noVacio(grabadoBDFrecuencia) )
		    nuevas += "\nSe grabó información en el campo \"Frecuencia\" del antecedente";
		if( noVacio(fechaInic) && !noVacio(grabadoBDFechaInic) )
		    nuevas += "\nSe grabó información en el campo \"Fecha de Inicio\" del antecedente";
		if( noVacio(fechaFin) && !noVacio(grabadoBDFechaFin) )
		    nuevas += "\nSe grabó información en el campo \"Fecha de Finalización\" del antecedente";
		if( noVacio(tiempoT) && !noVacio(grabadoBDTiempoT) )
		    nuevas += "\nSe grabó información en el campo \"Tiempo Tratamiento\" del antecedente";
		if( noVacio(dosisD) && !noVacio(grabadoBDDosisD) )
		    nuevas += "\nSe grabó información en el campo \"Dosis Diaria\" del antecedente";
		if( noVacio(cantidad) && !noVacio(grabadoBDCantidad) )
		    nuevas += "\nSe grabó información en el campo \"Cantidad\" del antecedente";


		if( noVacio(tipofrecuencia) && !noVacio(grabadoBDTipoFrecuencia) )
		    nuevas += "\nSe grabó información en el campo \"Tipo Frecuencia\" del antecedente";

		if( noVacio(unidosis) && !noVacio(grabadoBDUnidosis) )
		    nuevas += "\nSe grabó información en el campo \"Unidosis\" del antecedente";
		
		
		
		if(noVacio(nuevas))
		    observaciones=UtilidadTexto.agregarTextoAObservacion(observaciones, nuevas, usuario, true);
		    
		return observaciones;
		
	}
	
	/**
	 * Retorna las especialidades del usuario que esta en la aplicación.
	 * @param usuario
	 * @return String
	 */
	public String getEspecialidadesUsuario(UsuarioBasico usuario)
	{
		return UsuarioBasico.getEspecialidadesMedico(usuario);
	}

	/**
	 * Retorna la hora actual, en formato hh:MM
	 * @return String
	 */
	public String getHoraActual()
	{
		GregorianCalendar calendar = new GregorianCalendar(new SimpleTimeZone(-18000000, "America/Bogota"));
		
		String minute = calendar.get(Calendar.MINUTE) + "";
		minute = (minute.length() < 2) ? "0"+minute : minute;
		String hour = calendar.get(Calendar.HOUR_OF_DAY) + "";
		hour = (hour.length() < 2) ? "0"+hour : hour;
		String horaAct = hour + ":" + minute;
		
		return horaAct;
	}
	
	/**
	 * Retorna true su la cadena no es vacia, es decir, no es null, ni vacia,
	 * false de lo contrario.
	 * @param valor
	 * @return boolean
	 */
	private boolean noVacio(String valor)
	{
		if( valor != null && !valor.equals("") )
			return true;
		else
			return false; 
	}
}
