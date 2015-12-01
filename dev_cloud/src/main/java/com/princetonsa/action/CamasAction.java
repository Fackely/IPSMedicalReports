/*
 * @(#)CamasAction.java
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
import java.util.ArrayList;

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
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;

import com.princetonsa.actionform.CamaForm;
import com.princetonsa.actionform.CamasForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.Cama;
import com.princetonsa.mundo.Camas;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;

/**
 * Action, controla todas las opciones dentro de la cama, incluyendo los
 * posibles casos de error. Y los casos de flujo.
 * 
 * @version 1.0, Junio 9, 2003
 * @author <a href="mailto:Liliana@PrincetonSA.com">Liliana Caballero</a>,
 * @see org.apache.struts.action.ActionForm#validate(ActionMapping,
 * HttpServletRequest)
 */
public class CamasAction extends Action
{
	/**
	 * Para hacer logs de esta funcionalidad.
	 */	
	private Logger logger = Logger.getLogger(CamasAction.class);

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
			if( form instanceof CamasForm )
			{
				CamasForm camasForm = (CamasForm)form;
				String estado = camasForm.getEstado();

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
					request.setAttribute("codigoDescripcionError", "errors.problemasBd");
					return mapping.findForward("paginaError");
				}

				logger.warn("[CamasAction] --->"+estado);
				UsuarioBasico usuario = (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");

				if( estado == null )
				{
					if( logger.isDebugEnabled() )
					{
						logger.debug("estado no valido dentro del flujo de camas (null) ");
					}
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");

					if( con != null && !con.isClosed() )
						UtilidadBD.closeConnection(con);

					return mapping.findForward("descripcionError");				
				}
				else
					if(estado.equals("inicio"))
					{
						camasForm.resetBusqueda();
						UtilidadBD.closeConnection(con);
						return mapping.findForward("principal");
					}
				if(estado.equals("busquedaAvanzada"))
				{
					return this.accionBusquedaAvanzada(camasForm,mapping,con);
				}
				else 
					if(estado.equals("resultadoBusquedaAvanzada"))
					{
						return this.accionResultadoBusquedaAvanzada(camasForm,mapping,con);
					}
					else 
						if(estado.equals("imprimir"))
						{
							UtilidadBD.cerrarConexion(con);
							return mapping.findForward("imprimir");
						}
						else 
							if(estado.equals("imprimirPorPaciente"))
							{
								UtilidadBD.cerrarConexion(con);
								return mapping.findForward("imprimirPorPaciente");
							}
							else 
								if(estado.equals("listarLinks"))
								{
									return this.accionListarLinks(camasForm,mapping,request,con, estado);
								}
								else 
									if(estado.equals("resultadoLink"))
									{
										return this.accionResultadoLink(camasForm,mapping,request,con, estado);
									}
									else
										if( estado.equals("listarCamasDesinfeccion"))
										{
											camasForm.reset();

											if( con != null && !con.isClosed() )
												UtilidadBD.closeConnection(con);

											return mapping.findForward("paginaLiberarCamaDesinfeccion");
										}
										else
											if( estado.equals("seleccionarCentroCostoCenso"))
											{
												return accionSeleccionarCentroCostoCenso(con,camasForm,mapping,usuario,request);

											}
											else
												if( estado.equals("cancelar") )
												{
													camasForm.reset();

													if( con != null && !con.isClosed() )
														UtilidadBD.closeConnection(con);

													return null;
												}			
												else
													if( estado.equals("cambiarEstadoCamaDesinfeccion") )
													{
														Cama cama = new Cama();
														cama.init(tipoBD);
														for(int i=1; i<=camasForm.getNumCamas(); i++)
														{
															String codigoCama = (String)camasForm.getCama(""+i);

															try
															{
																if( codigoCama != null && con != null )
																{
																	cama.cambiarEstadoCama(con, codigoCama, 0);
																}
															}
															catch(SQLException sqle)
															{
																logger.warn("problemas cambiando el estado a la cama "+sqle);

																request.setAttribute("descripcionError", "No se pudo cambiar el estado de las camas seleccionadas");

																if( con != null && !con.isClosed() )
																	UtilidadBD.closeConnection(con);

																return mapping.findForward("descripcionError");										
															}
														}
														request.setAttribute("numCamas", ""+camasForm.getNumCamas());

														if( con != null && !con.isClosed() )
															UtilidadBD.closeConnection(con);

														return mapping.findForward("paginaResumen");
													}
													else if(estado.equals("ordenar"))
													{
														if( con != null && !con.isClosed() )
															UtilidadBD.closeConnection(con);
														return accionOrdenar(camasForm,mapping);
													}

				if( con != null && !con.isClosed() )
					UtilidadBD.closeConnection(con);

				return null;
			}

			else
			{
				if( logger.isDebugEnabled() )
				{
					logger.debug("El form no corresponde con el form de las Camas");
				}
				request.setAttribute("codigoDescripcionError", "errors.formaTipoInvalido");
				return mapping.findForward("descripcionError");	
			}
		} catch (Exception e) {
			Log4JManager.error(e);
			return null;
		}
		finally{
			UtilidadBD.closeConnection(con);
		}
	}
	
	private ActionForward accionSeleccionarCentroCostoCenso(Connection con, CamasForm camasForm, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request) 
	{
		camasForm.reset();
		camasForm.setCentroCosto(ConstantesBD.codigoCentroCostoNoSeleccionado);
		if(usuario!=null)
		{
			camasForm.setCodigoCentroAtencion(usuario.getCodigoCentroAtencion());
			camasForm.setInstitucion(usuario.getCodigoInstitucion());
			
	        this.cerrarConexion(con);
			return mapping.findForward("paginaCentroCosto");
		}
		else
		{
			ActionErrors errores = new ActionErrors();
			errores.add("Usuario no cargado",new ActionMessage("errors.usuario.noCargado"));
			saveErrors(request,errores);
			this.cerrarConexion(con);
			return mapping.findForward("paginaCentroCosto");
		}
	}

	/**
	 * Método usado para ordenar el arreglo de camas en el resumen 
	 * de censo de camas, se utiliza el método burbuja
	 * @param camasForm
	 * @param mapping
	 * @return
	 */
	private ActionForward accionOrdenar(CamasForm camasForm, ActionMapping mapping) 
	{
		ArrayList listado=camasForm.getCamasAL();
		String criterio=camasForm.getCriterio();
		int centroCosto=camasForm.getCentroCosto();
		
		//se verifica tipo de ordenacion
		if(camasForm.getEstadoOrdenacion().equals("ASC"))
			camasForm.setEstadoOrdenacion("DESC");
		else if(camasForm.getEstadoOrdenacion().equals("DESC"))
			camasForm.setEstadoOrdenacion("ASC");
		
		//se recorre el listado con método de ordenación burbuja********
		for(int i=0;i<camasForm.getNumCamas();i++)
		{
			CamaForm cama1=(CamaForm)listado.get(i);
			for(int j=(camasForm.getNumCamas()-1);j>i;j--)
			{
				CamaForm cama2=(CamaForm)listado.get(j);
				//se filtran solo los registros que hacen parte del mismo centro de costo
				if(centroCosto==cama2.getCentroCosto()&&centroCosto==cama1.getCentroCosto())
				{
				
					if(criterio.equals("habitacion"))
						listado=this.verificacionOrdenacion(listado,cama1,cama2,camasForm.getEstadoOrdenacion(),"numerico",i,j,cama1.getHabitacion(),cama2.getHabitacion());
					else if(criterio.equals("codigo"))
						listado=this.verificacionOrdenacion(listado,cama1,cama2,camasForm.getEstadoOrdenacion(),"cadena",i,j,cama1.getNumero(),cama2.getNumero());
					else if(criterio.equals("descripcion"))
						listado=this.verificacionOrdenacion(listado,cama1,cama2,camasForm.getEstadoOrdenacion(),"cadena",i,j,cama1.getDescripcion(),cama2.getDescripcion());
					else if(criterio.equals("estado"))
						listado=this.verificacionOrdenacion(listado,cama1,cama2,camasForm.getEstadoOrdenacion(),"cadena",i,j,cama1.getNombreEstadoCama(),cama2.getNombreEstadoCama());
					else if(criterio.equals("paciente"))
						listado=this.verificacionOrdenacion(listado,cama1,cama2,camasForm.getEstadoOrdenacion(),"cadena",i,j,cama1.getNombrePaciente(),cama2.getNombrePaciente());
					else if(criterio.equals("identificacion"))
						listado=this.verificacionOrdenacion(listado,cama1,cama2,camasForm.getEstadoOrdenacion(),"cadena",i,j,cama1.getTipoIdPaciente()+" "+cama1.getNumeroIdPaciente(),cama2.getTipoIdPaciente()+" "+cama2.getNumeroIdPaciente());
					else if(criterio.equals("edad"))
						listado=this.verificacionOrdenacion(listado,cama1,cama2,camasForm.getEstadoOrdenacion(),"numerico",i,j,cama1.getEdadPaciente()+"",cama2.getEdadPaciente()+"");
					else if(criterio.equals("sexo"))
						listado=this.verificacionOrdenacion(listado,cama1,cama2,camasForm.getEstadoOrdenacion(),"cadena",i,j,cama1.getSexoPaciente(),cama2.getSexoPaciente());
					else if(criterio.equals("fecha"))
						listado=this.verificacionOrdenacion(listado,cama1,cama2,camasForm.getEstadoOrdenacion(),"fecha",i,j,cama1.getFechaAdmision()+"@@@"+cama1.getHoraAdmision(),cama2.getFechaAdmision()+"@@@"+cama2.getHoraAdmision());
					else if(criterio.equals("dias"))
						listado=this.verificacionOrdenacion(listado,cama1,cama2,camasForm.getEstadoOrdenacion(),"numerico",i,j,cama1.getNumDiasEstancia()+"",cama2.getNumDiasEstancia()+"");
					else if(criterio.equals("responsable"))
						listado=this.verificacionOrdenacion(listado,cama1,cama2,camasForm.getEstadoOrdenacion(),"cadena",i,j,cama1.getResponsableCuenta(),cama2.getResponsableCuenta());
					
					cama1=(CamaForm)listado.get(i);
				}
			}
		}
		//****************************************************************
		camasForm.setCamasAL(listado);
		
		return mapping.findForward("paginaResumen");
		
	}
	
	/**
	 * Método implementado para realizar el intermacio de elementos del arreglo donde se encuentran
	 * las camas listadas para efectuar la ordenación. Este proceso es muy específico debido a que se maneja
	 * un estado de ascendente/descendente desde el formbean y se revisa el tipo de dato usado para saber como
	 * efectuar la comparación de los atributos en cada par de elementos.
	 * @param listado (Arreglo de las camas)
	 * @param cama1 (elemento A del arreglo a comparar)
	 * @param cama2 (elemento B del arreglo a comparar)
	 * @param estado (ordenacion ASC/DESC)
	 * @param tipoDato (tipos de dato que se van a comparar String,int, etc...)
	 * @param i (posicion cama1 en listado)
	 * @param j (poosicion de cama2 en listado)
	 * @param dato1 (dato de cama1 que se va a comparar)
	 * @param dato2 (dato de cama2 que se va a comparar)
	 * @return
	 */
	private ArrayList verificacionOrdenacion(ArrayList listado,CamaForm cama1,CamaForm cama2,String estado,String tipoDato,int i,int j,String dato1,String dato2)
	{
	
		if(estado.equals("ASC"))
		{
			if(tipoDato.equals("cadena"))
			{
				
				boolean esNumericoDato1 = false;
				boolean esNumericoDato2 = true;
				//int numericoDato1 = 0;
				//int numericoDato2 = 0;
				//verificar si el dato1 es numérico
				try
				{
					//numericoDato1 = 
					Integer.parseInt(dato1);
					esNumericoDato1 = true;
				}
				catch(Exception e)
				{
					esNumericoDato1 = false;
				}
				//verificar si el dato2 es numérico
				try
				{
					//numericoDato2 = 
					Integer.parseInt(dato2);
					esNumericoDato2 = true;
				}
				catch(Exception e)
				{
					esNumericoDato2 = false;
				}
				
				//si ambos campos son cadena
				if(!esNumericoDato1&&!esNumericoDato2)
				{ 
					if(dato1.trim().toLowerCase().compareTo(dato2.trim().toLowerCase())>=0)
					{
						listado.set(i,cama2);
						listado.set(j,cama1);
					}
				}
				//si ambos campos son solo numeros
				else if(esNumericoDato1&&esNumericoDato2)
				{
					if(Integer.parseInt(dato1)>=Integer.parseInt(dato2))
					{
						listado.set(i,cama2);
						listado.set(j,cama1);
					}
				}
				//si dato2 es numérico y dato1 es cadena
				else if(!esNumericoDato1&&esNumericoDato2)
				{
					listado.set(i,cama2);
					listado.set(j,cama1);
				}
				
			}
			else if(tipoDato.equals("fecha"))
			{
				
				if(dato1.trim().equals("@@@")&&dato2.trim().equals("@@@")||
					!dato1.trim().equals("@@@")&&dato2.trim().equals("@@@"))
					;
				else if(dato1.trim().equals("@@@")&&!dato2.trim().equals("@@@"))
				{
					listado.set(i,cama2);
					listado.set(j,cama1);
				}
				else
				{
					String aux1[] = dato1.split("@@@");
					String aux2[] = dato2.split("@@@");
					
					if(UtilidadFecha.compararFechas(aux1[0],aux1[1],aux2[0],aux2[1]).isTrue())
					{
						listado.set(i,cama2);
						listado.set(j,cama1);
					}
				}
			}
			else
			{
				if(Integer.parseInt(dato1)>=Integer.parseInt(dato2))
				{
					listado.set(i,cama2);
					listado.set(j,cama1);
				}
			}
		}
		else
		{
			if(tipoDato.equals("cadena"))
			{
				boolean esNumericoDato1 = false;
				boolean esNumericoDato2 = false;
				//int numericoDato1 = 0;
				//int numericoDato2 = 0;
				//verificar si el dato1 es numérico
				try
				{
					//numericoDato1 = 
					Integer.parseInt(dato1);
					esNumericoDato1 = true;
				}
				catch(Exception e)
				{
					esNumericoDato1 = false;
				}
				//verificar si el dato2 es numérico
				try
				{
					//numericoDato2 = 
					Integer.parseInt(dato2);
					esNumericoDato2 = true;
				}
				catch(Exception e)
				{
					esNumericoDato2 = false;
				}
				
				//si ambos campos son cadena
				if(!esNumericoDato1&&!esNumericoDato2)
				{
					if(dato1.trim().toLowerCase().compareTo(dato2.trim().toLowerCase())<0)
					{
						listado.set(i,cama2);
						listado.set(j,cama1);
					}
				}
				//si ambos campos son solo numeros
				else if(esNumericoDato1&&esNumericoDato2)
				{
					if(Integer.parseInt(dato1)<Integer.parseInt(dato2))
					{
						listado.set(i,cama2);
						listado.set(j,cama1);
					}
				}
				//si dato1 es numérico y dato2 es cadena
				else if(esNumericoDato1&&!esNumericoDato2)
				{
					listado.set(i,cama2);
					listado.set(j,cama1);
				}
				
				
				
			}
			else if(tipoDato.equals("fecha"))
			{
				
				if(dato1.trim().equals("@@@")&&dato2.trim().equals("@@@")||
						dato1.trim().equals("@@@")&&!dato2.trim().equals("@@@"))
					;
				else if(!dato1.trim().equals("@@@")&&dato2.trim().equals("@@@"))
				{
					listado.set(i,cama2);
					listado.set(j,cama1);
				}
				else
				{
					String aux1[] = dato1.split("@@@");
					String aux2[] = dato2.split("@@@");
					
					if(!UtilidadFecha.compararFechas(aux1[0],aux1[1],aux2[0],aux2[1]).isTrue())
					{
						listado.set(i,cama2);
						listado.set(j,cama1);
					}
				}
			}
			else
			{
				if(Integer.parseInt(dato1)<Integer.parseInt(dato2))
				{
					listado.set(i,cama2);
					listado.set(j,cama1);
				}
			}
		}
		
		return listado;
	}

	private StringBuffer getPorcentaje(double parte, double total)
	{
		double porcentaje = (parte/total)*100;
		
		return UtilidadTexto.getDoubleConFormatoEspecifico (porcentaje, "##0.00");
	}
	
	/**
	 * Este método especifica las acciones a realizar en el estado
	 * busquedaAvanzada de un traslado de camas
	 * 
	 * @param camasForm CamasForm
	 * 				para pre-llenar datos si es necesario
	 * @param request HttpServletRequest para obtener los 
	 * 				datos
	 * @param mapping Mapping para manejar la navegación
	 * @param con Conexión con la fuente de datos
	 * @return ActionForward a la página funcionalidadBuscarCamas"busquedaCamas.jsp"
	 * @throws SQLException
	 */
	private ActionForward accionBusquedaAvanzada(	CamasForm camasForm, 
																						ActionMapping mapping, 
																						Connection con) throws SQLException
	{
		//Limpiamos lo que venga del form
		camasForm.resetBusqueda();
		camasForm.setEstado("busquedaAvanzada");
		UtilidadBD.cerrarConexion(con);
		return mapping.findForward("paginaBusqueda");		
	}

	/**
	 * Este método especifica las acciones a realizar en el estado
	 * resultadoBusquedaAvanzada de un traslado de cama
	 * 
	 * @param camasForm CamasForm
	 * 				para pre-llenar datos si es necesario
	 * @param request HttpServletRequest para obtener los 
	 * 				datos
	 * @param mapping Mapping para manejar la navegación
	 * @param con Conexión con la fuente de datos
	 * @return ActionForward a la página funcionalidadBuscarCamas "busquedaCamas.jsp"
	 * @throws SQLException
	 */
	private ActionForward accionResultadoBusquedaAvanzada(CamasForm camasForm, 
																										ActionMapping mapping, 
																										Connection con) throws SQLException
	{
		Camas mundoCamas= new Camas();
		mundoCamas.resetBusqueda();
		enviarItemsSeleccionadosBusqueda(camasForm, mundoCamas);
		camasForm.resetCriteriosBusqueda();
		camasForm.setCol(mundoCamas.resultadoBusquedaTrasladoCamas(con));
		UtilidadBD.cerrarConexion(con);
		return mapping.findForward("paginaListar");
	}

	private void enviarItemsSeleccionadosBusqueda(CamasForm camasForm, Camas mundoCamas)
	{
		String bb[]= camasForm.getCriteriosBusqueda();
		for(int i=0; i<bb.length; i++)
		{
			try
			{
				if(bb[i].equals("numeroCama"))
					mundoCamas.setNumeroCama(camasForm.getNumeroCama());
				if(bb[i].equals("rangoFechas"))
				{
					mundoCamas.setFechaTrasladoInicial(camasForm.getFechaTrasladoInicial());
					mundoCamas.setFechaTrasladoFinal(camasForm.getFechaTrasladoFinal());
				}
				if(bb[i].equals("rangoHoras"))
				{
					mundoCamas.setHoraTrasladoInicial(camasForm.getHoraTrasladoInicial());
					mundoCamas.setHoraTrasladoFinal(camasForm.getHoraTrasladoFinal());
				}	
				if(bb[i].equals("usuario"))
					mundoCamas.setUsuario(camasForm.getUsuario());
			}
			catch (Exception e)
			{
				logger.warn("Error en enviarItemsSeleccionados "+e);
			}
		}		
	}

	/**
	 * Este método especifica las acciones a realizar en el estado
	 * listarLinks
	 * @param camasForm CamasForm
	 * @param mapping Mapping para manejar la navegación
	 * @param con Conexión con la fuente de datos
	 * @return ActionForward  a la página "listadoLinks.jsp"
	 * @throws SQLException
	 */
	private ActionForward accionListarLinks	(	CamasForm camasForm, 
																			ActionMapping mapping,
																			HttpServletRequest request, 
																			Connection con,String estado) throws SQLException 
	{
		Camas mundoCamas= new Camas();
		camasForm.setEstado(estado);
		PersonaBasica persona= (PersonaBasica)request.getSession().getAttribute("pacienteActivo");
		if (persona.getCodigoPersona()<1)
		{
			logger.warn("No hay ningún paciente cargado");

			request.setAttribute("codigoDescripcionError", "errors.paciente.noCargado");
			UtilidadBD.cerrarConexion(con);
			return mapping.findForward("paginaError");
		}
		else
		{
			camasForm.setCol(mundoCamas.linksConsultaTrasladoCamasPorPaciente(con, persona.getCodigoPersona()));
			UtilidadBD.cerrarConexion(con);
			return mapping.findForward("paginaListarLinks")	;	
		}	
	}	

	private ActionForward accionResultadoLink	(	CamasForm camasForm, 
																					ActionMapping mapping,
																					HttpServletRequest request, 
																					Connection con,String estado) throws SQLException 
	{
		Camas mundoCamas= new Camas();
		camasForm.setEstado(estado);
		PersonaBasica persona= (PersonaBasica)request.getSession().getAttribute("pacienteActivo");
		if (persona.getCodigoPersona()<1)
		{
			logger.warn("No hay ningún paciente cargado");

			request.setAttribute("codigoDescripcionError", "errors.paciente.noCargado");
			UtilidadBD.cerrarConexion(con);
			return mapping.findForward("paginaError");
		}
		else
		{
			camasForm.setCol(mundoCamas.busquedaConsultaTrasladoCamasPorPaciente(con, camasForm.getCuenta()));
			UtilidadBD.cerrarConexion(con);
			return mapping.findForward("paginaListar")	;	
		}	
	}
	
	/**
	 * Método en que se cierra la conexión (Buen manejo
	 * recursos), usado ante todo al momento de hacer un forward
	 * @param con Conexión con la fuente de datos
	 */
	private void cerrarConexion (Connection con)
	{
	    try
		{
	        UtilidadBD.cerrarConexion(con);
	    }
	    catch(Exception e)
		{
	        logger.error(e+"Error al tratar de cerrar la conexion con la fuente de datos. \n Excepcion: " +e.toString());
	    }
	}

}
