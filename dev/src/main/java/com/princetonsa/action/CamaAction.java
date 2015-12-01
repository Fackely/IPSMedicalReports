/*
 * @(#)CamaAction.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */
package com.princetonsa.action;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.axioma.util.log.Log4JManager;

import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.UtilidadValidacion;

import com.princetonsa.actionform.CamaForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.AdmisionHospitalaria;
import com.princetonsa.mundo.Cama;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;

/**
 * Action, tiene la función de control de flujo de las acciones y eventos
 * realizados sobre una cama
 * 
 * @version 1.0, Septiembre 16, 2003
 * @author <a href="mailto:Liliana@PrincetonSA.com">Liliana Caballero</a>
 */
public class CamaAction extends Action
{
	/**
	 * Para hacer logs de esta funcionalidad.
	 */	
	private Logger logger = Logger.getLogger(CamaAction.class);
	
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
			if( form instanceof CamaForm )
			{
				CamaForm camaForm = (CamaForm)form;
				String estado = camaForm.getEstado();
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

					camaForm.reset();

					logger.warn("Problemas con la base de datos "+e);
					request.setAttribute("codigoDescripcionError", "errors.problemasBd");
					return mapping.findForward("paginaError");
				}

				HttpSession session=request.getSession();			
				PersonaBasica paciente = (PersonaBasica)session.getAttribute("pacienteActivo");
				UsuarioBasico usuario = (UsuarioBasico)session.getAttribute("usuarioBasico");
				logger.warn("[CamasAction] estado->"+estado);
				if( usuario == null )
				{
					return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Usuario no válido (null)", "errors.usuario.noCargado", true);
				}
				else
					if( paciente == null || paciente.getTipoIdentificacionPersona().equals("") )
					{
						return ComunAction.accionSalirCasoError(mapping, request, con, logger, "El paciente no es válido (null)", "errors.paciente.noCargado", true);
					}
					else	
						if( estado == null )
						{
							camaForm.reset();

							return ComunAction.accionSalirCasoError(mapping, request, con, logger, "estado no valido dentro del flujo de cama (null) ", "errors.estadoInvalido", true);
						}
						else
							if( estado.equals("cargarInfoTraslado") )
							{
								String fechaHora = new String();		
								camaForm.reset();
								AdmisionHospitalaria admision = new AdmisionHospitalaria();
								admision.init(tipoBD);

								try
								{
									admision.cargarUltimaAdmision(con, paciente.getCodigoPersona());
									fechaHora = admision.cargarUltimaFechaHoraRegistroCama(con, paciente.getCodigoPersona());
									if (admision.getCodigoAdmisionHospitalaria()==paciente.getCodigoAdmision()&&paciente.getCodigoAdmision()!=0)
									{
										if(UtilidadValidacion.tieneEgreso_sinManejoSemiEgreso(con, paciente.getCodigoCuenta()))
										{
											//Solo puede realizar traslado de camas si no tiene egreso
											request.setAttribute("codigoDescripcionError", "error.trasladocama.egresoAbierto");
											if( con != null && !con.isClosed() )
												UtilidadBD.closeConnection(con);

											camaForm.reset();

											return mapping.findForward("paginaError");																	
										}
										/**
										 * Validar concurrencia
										 * Si ya está en proceso de distribucion, no debe dejar entrar
										 **/
										if(UtilidadValidacion.estaEnProcesoDistribucion(con, paciente.getCodigoPersona(), usuario.getLoginUsuario()) )
										{
											return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error.facturacion.cuentaEnProcesoDistribucion", "error.facturacion.cuentaEnProcesoDistribucion", true);
										}
									}
									else
									{
										//La última admisión no coincide con la que se va a editar
										logger.error("El paciente no tiene ninguna admisión de hospitalización abierta ");
										request.setAttribute("codigoDescripcionError", "errors.paciente.noCuentaHospitalizacion");

										if( con != null && !con.isClosed() )
											UtilidadBD.closeConnection(con);

										camaForm.reset();

										return mapping.findForward("paginaError");																	

									}

								}
								catch(SQLException e)
								{
									logger.error("El paciente no tiene ninguna admisión de hospitalización abierta "+e);
									request.setAttribute("codigoDescripcionError", "errors.paciente.noAdmision");

									if( con != null && !con.isClosed() )
										UtilidadBD.closeConnection(con);

									camaForm.reset();

									return mapping.findForward("paginaError");																	
								}				

								//	 			No está hospitalizado el paciente
								if( admision.getCodigoEstado() != 1 )
								{
									logger.error("El paciente no tiene ninguna admisión de hospitalización abierta ");
									request.setAttribute("codigoDescripcionError", "errors.paciente.noCuentaHospitalizacion");

									if( con != null && !con.isClosed() )
										UtilidadBD.closeConnection(con);

									camaForm.reset();

									return mapping.findForward("paginaError");																						
								}

								//				La cama está ocupada
								if( admision.getEstadoCama() == 1 )
								{
									cargarDatosTraslado(camaForm, admision);
									camaForm.setNombrePaciente(paciente.getNombrePersona());
									camaForm.setCodSexoPaciente(paciente.getCodigoSexo());
									camaForm.setEdadPaciente(paciente.getEdad());
									camaForm.setFechaTraslado(UtilidadFecha.getFechaActual());
									camaForm.setHoraTraslado(UtilidadFecha.getHoraActual());

									if( !UtilidadCadena.noEsVacio(fechaHora) )
									{
										camaForm.setFechaUltTraslado(UtilidadFecha.conversionFormatoFechaAAp(admision.getFecha()));
										camaForm.setHoraUltTraslado(admision.getHora());
									}
									else
									{
										String[] fechaHoraArr =fechaHora.split("-", 2);
										if( fechaHoraArr.length == 2 )
										{
											camaForm.setFechaUltTraslado(fechaHoraArr[0]);
											camaForm.setHoraUltTraslado(fechaHoraArr[1]);
										}
									}

									if( con != null && !con.isClosed() )
										UtilidadBD.closeConnection(con);

									return mapping.findForward("trasladoPpal");
								}
								else
								{
									logger.error("La cama no se encuentra en estado ocupada");
									request.setAttribute("descripcionError", "La cama no se encuentra en estado ocupada");

									if( con != null && !con.isClosed() )
										UtilidadBD.closeConnection(con);

									camaForm.reset();

									return mapping.findForward("paginaError");																	
								}
							}
							else
								if( estado.equals("finalizarTraslado") )
								{
									Cama cama = new Cama();
									cama.init(tipoBD);

									String fechaHora[] = cama.getFechaHoraUltimoUsoCama(con, camaForm.getCodigo());

									if( fechaHora != null )
									{
										ResultadoBoolean resultado = UtilidadFecha.compararFechas(camaForm.getFechaTraslado(), camaForm.getHoraTraslado(), fechaHora[0], fechaHora[1]);

										if( !resultado.isTrue() )
										{
											ActionErrors errores = new ActionErrors();
											if( UtilidadCadena.noEsVacio(resultado.getDescripcion()) )
											{
												errores.add("Problemas en la validación de las fechas", new ActionMessage("error.trasladocama.errorValiacionFechas"));
											}
											else
											{
												errores.add("La fecha de traslado no es valida, se cruza con la fecha de ocupación de la cama", new ActionMessage("error.trasladocama.fechaNoValida"));
											}						

											saveErrors(request, errores);

											if( con != null && !con.isClosed() )
												UtilidadBD.closeConnection(con);

											return mapping.findForward("trasladoPpal");					
										}
									}



									if( con != null && !con.isClosed() )
										UtilidadBD.closeConnection(con);

									//return mapping.findForward("trasladoPpal");
									return mapping.findForward("trasladoResumen");	
								}
								else
								{
									logger.error("Se intento acceder con un estado inválido "+usuario.getLoginUsuario()+" Estado --> "+estado);
									request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");

									if( con != null && !con.isClosed() )
										UtilidadBD.closeConnection(con);

									camaForm.reset();

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
	
	public void cargarDatosTraslado(CamaForm camaForm, AdmisionHospitalaria admision)
	{
		camaForm.setCodCamaInicial(admision.getCodigoCama());
		camaForm.setNumCamaInicial(admision.getNombreCama());
		camaForm.setCodigoAdmision(admision.getCodigoAdmisionHospitalaria());
		camaForm.setFechaAdmision(admision.getFecha());
		
		if( admision.getHora() != null  )
			camaForm.setHoraAdmision(admision.getHora().substring(0,5));
	}
}
