/*
 * @(#)AntecedentesTransfusionalesAction.java
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

import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadTexto;
import util.UtilidadValidacion;

import com.princetonsa.actionform.AntecedentesTransfusionalesForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.antecedentes.AntecedenteTransfusional;
import com.princetonsa.mundo.antecedentes.AntecedentesTransfusionales;

/**
 * Action, controla todo el módulo de antecedentes transfusionales
 * @version 1.0, Septiembre 3, 2003
 * @author <a href="mailto:Liliana@PrincetonSA.com">Liliana Caballero</a>,
 * @see org.apache.struts.action.ActionForm#validate(ActionMapping,
 * HttpServletRequest)
 */
public class AntecedentesTransfusionalesAction extends Action
{
	/**
	 * Para hacer logs de está funcionalidad.
	 */	
	private Logger logger = Logger.getLogger(AntecedentesTransfusionalesAction.class);
	
	/**
	 * Usuario que está trabajando actualmente en el sistema.
	 */	
	private UsuarioBasico usuario;
	
	/**
	 * Método encargado de el flujo y control de la funcionalidad
	 * @see org.apache.struts.action.Action#execute(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse)
	 */	
	public ActionForward execute(	ActionMapping mapping, 
														ActionForm form, 
														HttpServletRequest request,
														HttpServletResponse response) throws Exception
														{

		Connection con = null;
		try {
			if( form instanceof AntecedentesTransfusionalesForm )
			{	
				AntecedentesTransfusionalesForm transfusionesForm = (AntecedentesTransfusionalesForm)form;
				String estado = transfusionesForm.getEstado();
				logger.warn("\n\n [AntecedentesTransfusionalesAction] Estado ["+estado+"] \n\n ");
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

					transfusionesForm.reset();

					logger.warn("Problemas con la base de datos "+e);
					request.setAttribute("codigoDescripcionError", "errors.problemasBd");
					return mapping.findForward("paginaError");
				}

				HttpSession session=request.getSession();			
				PersonaBasica paciente = (PersonaBasica)session.getAttribute("pacienteActivo");
				usuario = (UsuarioBasico)session.getAttribute("usuarioBasico");

				if( usuario == null )
				{
					if( logger.isDebugEnabled() )
					{
						logger.debug("Usuario no válido (null)");
					}

					if( con != null && !con.isClosed() )
						UtilidadBD.closeConnection(con);

					transfusionesForm.reset();					

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

						transfusionesForm.reset();					

						request.setAttribute("codigoDescripcionError", "errors.paciente.noCargado");
						return mapping.findForward("paginaError");				
					}
					else	
						if( estado == null )
						{
							if( logger.isDebugEnabled() )
							{
								logger.debug("estado no valido dentro del flujo de antecedentes transfusionales (null) ");
							}

							if( con != null && !con.isClosed() )
								UtilidadBD.closeConnection(con);

							transfusionesForm.reset();

							request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
							return mapping.findForward("descripcionError");				
						}
						else
							if( !UtilidadValidacion.esProfesionalSalud(usuario) )
							{
								UtilidadBD.cerrarConexion(con);
								transfusionesForm.reset();

								logger.warn("El usuario no tiene permisos de acceder a esta funcionalidad "+usuario.getLoginUsuario());				
								request.setAttribute("codigoDescripcionError", "errors.usuario.noAutorizado");
								return mapping.findForward("paginaError");								
							}	
							else	
								if( estado.equals("adicionarTransfusion") )
								{
									transfusionesForm.setNumTransfusiones(transfusionesForm.getNumTransfusiones()+1);
									UtilidadBD.cerrarConexion(con);
									return mapping.findForward("transfusionalesPpal");
								}
								else	
									if( estado.equals("modificarTransfusion") )
									{
										UtilidadBD.cerrarConexion(con);
										return mapping.findForward("transfusionalesPpal");
									}
									else
										if( estado.equals("cargar") )
										{
											transfusionesForm.reset();

											AntecedentesTransfusionales transfusiones = new AntecedentesTransfusionales();

											transfusiones.cargar(con, paciente.getCodigoPersona());

											cargarForm(transfusiones, transfusionesForm);

											if( con != null && !con.isClosed() )
												UtilidadBD.closeConnection(con);

											return mapping.findForward("transfusionalesPpal");				
										}
										else
											if( estado.equals("resumen") )
											{
												transfusionesForm.reset();
												AntecedentesTransfusionales transfusiones = new AntecedentesTransfusionales();

												transfusiones.cargar(con, paciente.getCodigoPersona());

												cargarForm(transfusiones, transfusionesForm);

												if( con != null && !con.isClosed() )
													UtilidadBD.closeConnection(con);

												return mapping.findForward("resumenTransfusionales");
											}
											else
												if( estado.equals("finalizar") )
												{
													AntecedentesTransfusionales transfusiones = new AntecedentesTransfusionales();

													cargarObjeto(transfusiones, transfusionesForm);
													//cambio para que mantenga las observaciones actualizadas.
													transfusiones.setObservaciones(cargarObservaciones(transfusionesForm.getObservacionesNuevas(), ""));
													ResultadoBoolean resultado = transfusiones.updateTransaccional(con, paciente.getCodigoPersona());

													if( resultado.isTrue() )
													{
														String paginaSiguiente=request.getParameter("paginaSiguiente");

														transfusionesForm.reset();
														transfusiones.reset();

														if( paginaSiguiente != null && !paginaSiguiente.equals("") )
														{
															UtilidadBD.closeConnection(con);
															response.sendRedirect(paginaSiguiente);
														}												

														transfusiones.cargar(con, paciente.getCodigoPersona());

														cargarForm(transfusiones, transfusionesForm);

														if( con != null && !con.isClosed() )
															UtilidadBD.closeConnection(con);

														return mapping.findForward("transfusionalesPpal");					
													}
													else
													{
														logger.error("No se pudo hacer la inserción de los antecedentes, se presentó el siguiente error : "+resultado.getDescripcion());
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
														transfusionesForm.reset();

														if( con != null && !con.isClosed() )
														{
															UtilidadBD.closeConnection(con);
														}
														response.sendRedirect(paginaSiguiente);

														return null;
													}			
													else
													{
														logger.error("Se intento acceder con un estado inválido "+usuario.getLoginUsuario()+" Estado --> "+estado);
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

	/**
	 * Carga  el form (bean) con la información pertinente contenida en el
	 * objeto
	 * @param 	AntecedentesTransfusionales, transfusiones
	 * @param 	AntecedentesTransfusionalesForm, transfusionesForm
	 */	
	private void cargarForm(AntecedentesTransfusionales transfusiones, AntecedentesTransfusionalesForm transfusionesForm)
	{
		if( transfusiones.getObservaciones() != null )
		{
			if( transfusionesForm.getEstado().equals("resumen") )
				transfusionesForm.setObservacionesAnteriores(transfusiones.getObservaciones().replaceAll("\n", "<br>"));
			else
				transfusionesForm.setObservacionesAnteriores(transfusiones.getObservaciones().replaceAll("<br>", "\n"));	
		}

		int tam = transfusiones.getTransfusiones().size();
		
		transfusionesForm.setNumTransfusiones(tam);
		transfusionesForm.setNumTransfusionesBD(tam);
		
		for(int i =1; i<=tam; i++)
		{
			AntecedenteTransfusional transfusion = transfusiones.getTransfusion(i-1);
			
			transfusionesForm.setTransfusion("componente_"+i, transfusion.getComponente());			
			if( noVacio(transfusion.getComponente()) )
				transfusionesForm.setTransfusion("co_grabadoBD_"+i, "true");	
			
			transfusionesForm.setTransfusion("fecha_"+i, transfusion.getFecha());
			if( noVacio(transfusion.getFecha()) )
				transfusionesForm.setTransfusion("f_grabadoBD_"+i, "true");				
			
			transfusionesForm.setTransfusion("causa_"+i, transfusion.getCausa());
			if( noVacio(transfusion.getCausa()) )
				transfusionesForm.setTransfusion("ca_grabadoBD_"+i, "true");				
			
			transfusionesForm.setTransfusion("lugar_"+i, transfusion.getLugar());
			if( noVacio(transfusion.getLugar()) )
				transfusionesForm.setTransfusion("l_grabadoBD_"+i, "true");				
			
			transfusionesForm.setTransfusion("edad_"+i, transfusion.getEdad());
			if( noVacio(transfusion.getEdad()) )
				transfusionesForm.setTransfusion("e_grabadoBD_"+i, "true");				

			transfusionesForm.setTransfusion("donante_"+i, transfusion.getDonante());
			if( noVacio(transfusion.getDonante()) )
				transfusionesForm.setTransfusion("d_grabadoBD_"+i, "true");				
			
			transfusionesForm.setTransfusion("observacionesAnteriores_"+i, transfusion.getObservaciones());
		}				
	}
	
	/**
	 * Carga en el objeto la información pertinente captada en el form
	 * @param transfusiones
	 * @param transfusionesForm
	 */
	@SuppressWarnings("rawtypes")
	private void cargarObjeto(AntecedentesTransfusionales transfusiones, AntecedentesTransfusionalesForm transfusionesForm)
	{
		transfusiones.setObservaciones(cargarObservaciones(transfusionesForm.getObservacionesNuevas(), transfusionesForm.getObservacionesAnteriores()));
		
		int tam = transfusionesForm.getNumTransfusiones();
		
		transfusiones.setTransfusiones(new ArrayList());
		
		for(int i=1; i<=tam; i++)
		{
			String componente = (String)transfusionesForm.getTransfusion("componente_"+i);
			String fecha = (String)transfusionesForm.getTransfusion("fecha_"+i);
			String causa = (String)transfusionesForm.getTransfusion("causa_"+i);
			String lugar = (String)transfusionesForm.getTransfusion("lugar_"+i);
			String edad = (String)transfusionesForm.getTransfusion("edad_"+i);
			String donante = (String)transfusionesForm.getTransfusion("donante_"+i);
			String observaciones = this.cargarObservacionesTransfusion(	(String)transfusionesForm.getTransfusion("observaciones_"+i),
																													(String)transfusionesForm.getTransfusion("observacionesAnteriores_"+i),
																													fecha,
																													causa,
																													lugar,
																													edad,
																													donante,
																													(String)transfusionesForm.getTransfusion("co_grabadoBD_"+i),
																													(String)transfusionesForm.getTransfusion("f_grabadoBD_"+i),
																													(String)transfusionesForm.getTransfusion("ca_grabadoBD_"+i),
																													(String)transfusionesForm.getTransfusion("l_grabadoBD_"+i),
																													(String)transfusionesForm.getTransfusion("e_grabadoBD_"+i),
																													(String)transfusionesForm.getTransfusion("d_grabadoBD_"+i) );
			
			if( noVacio(componente) || noVacio(fecha) || noVacio(causa) || noVacio(lugar) || noVacio(edad) || noVacio(edad) )
			{
				transfusiones.setTransfusion(new AntecedenteTransfusional(i, componente, fecha, causa, lugar, edad, donante, observaciones)); 
			}			
		}
	}
	
	public String cargarObservaciones(String nuevas, String anteriores)
	{
		///si no se editaron nuevas observaciones no se hace nada
		if(nuevas.equals(""))
			return anteriores;
		else
			return UtilidadTexto.agregarTextoAObservacion(anteriores, nuevas, usuario, true);		
	}
	
	public String cargarObservacionesTransfusion(	String nuevas, 
																String anteriores, 
																String fecha, 
																String causa, 
																String lugar, 
																String edad, 
																String donante, 
																String grabadoBDComponente,
																String grabadoBDFecha,
																String grabadoBDCausa,
																String grabadoBDLugar,
																String grabadoBDEdad,
																String grabadoBDDonante )
	{
		String observaciones = "";

	    if( UtilidadCadena.noEsVacio(anteriores))
	        observaciones=anteriores;

		if( UtilidadCadena.noEsVacio(nuevas))
		    nuevas="<b>"+nuevas+"</b>";
	
		if( !UtilidadCadena.noEsVacio(grabadoBDComponente) )
		    nuevas += "\nSe grabó el antecedente.";			
	
		if( UtilidadCadena.noEsVacio(fecha) && !noVacio(grabadoBDFecha) )
		    nuevas += "\nSe grabó información en el campo \"Fecha\" del antecedente";
		if( UtilidadCadena.noEsVacio(causa) && !noVacio(grabadoBDCausa) )
		    nuevas += "\nSe grabó información en el campo \"Causa\" del antecedente";
		if( UtilidadCadena.noEsVacio(lugar) && !noVacio(grabadoBDLugar) )
		    nuevas += "\nSe grabó información en el campo \"Lugar\" del antecedente";
		if( UtilidadCadena.noEsVacio(edad) && !noVacio(grabadoBDEdad) )
		    nuevas += "\nSe grabó información en el campo \"Edad\" del antecedente";
		if( UtilidadCadena.noEsVacio(donante) && !noVacio(grabadoBDDonante) )
		    nuevas += "\nSe grabó información en el campo \"Donante\" del antecedente";
					
		if(UtilidadCadena.noEsVacio(nuevas))
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
