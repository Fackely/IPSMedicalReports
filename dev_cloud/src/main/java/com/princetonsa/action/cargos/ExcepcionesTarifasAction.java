/*
 * @(#)ExcepcionesTarifariasAction.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */
package com.princetonsa.action.cargos;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

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

import util.ConstantesBD;
import util.Listado;
import util.LogsAxioma;
import util.UtilidadBD;

import com.princetonsa.actionform.cargos.ExcepcionesTarifasForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.cargos.ExcepcionesTarifas;

/**
 *   Action, controla todas las opciones dentro del registro de
 *   contratos, incluyendo los posibles casos de error. 
 *   Y los casos de flujo.
 * @version 1.0, Oct 20, 2004
 * @author wrios
 */
public class ExcepcionesTarifasAction extends Action
{
	/**
	 * Para hacer logs de debug / warn / error de esta funcionalidad.
	 */
	private Logger logger = Logger.getLogger(ExcepcionesTarifasAction.class);
	
	/**
	 * para almacenar datos generales.
	 */
	HashMap mapa = new HashMap ();
	
	/**
	 * Método encargado de el flujo y control de la funcionalidad
	 * 
	 * @see org.apache.struts.action.Action#execute(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse)
	 */
	public ActionForward execute(	ActionMapping mapping,
														ActionForm form,
														HttpServletRequest request,
														HttpServletResponse response ) throws Exception
	{
		Connection con=null;
		try{
		if (response==null); //Para evitar que salga el warning
		if(form instanceof ExcepcionesTarifasForm)
		{
				
				try
				{
						con = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection();	
				}
				catch(SQLException e)
				{
						logger.warn("No se pudo abrir la conexión"+e.toString());
				}
						
				ExcepcionesTarifasForm excForm =(ExcepcionesTarifasForm)form;
				HttpSession session=request.getSession();
				
				String estado=excForm.getEstado(); 
				logger.warn("estado->"+estado);
				if(estado == null)
				{
						excForm.reset();	
						logger.warn("Estado no valido dentro del flujo de Excepciones tarifas (null) ");
						request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
						this.cerrarConexion(con);
						return mapping.findForward("paginaError");
				}
				else if (estado.equals("empezar"))
				{
						return this.accionEmpezar(excForm,mapping, con);
				}
				else if(estado.equals("empezarInsertar"))
				{
						return this.accionEmpezarInsertar(excForm,mapping, con);
				}
				else if(estado.equals("guardar"))
				{
						return this.accionGuardar(excForm,request,mapping,con);
				}
				else if(estado.equals("empezarBuscarModificar"))
				{
						return this.accionEmpezarBuscarModificar(excForm,mapping,con);
				}
				else if(estado.equals("listarModificar"))
				{
						return this.accionListarModificar(excForm,mapping,con);
				}
				else if(estado.equals("listarModificarConErrores"))
				{
						return this.accionListarModificar(excForm,mapping,con);
				}
				else if (estado.equals("redireccion"))
				{
					UtilidadBD.cerrarConexion(con);
					response.sendRedirect(excForm.getLinkSiguiente());
					return null;
				}
				else if(estado.equals("eliminar"))
				{
					return this.accionEliminar(excForm,request,mapping,con);
				}
				else if(estado.equals("guardarModificar"))
				{
					return this.accionGuardarModificar(excForm,request,mapping,con,session);

				}
				else if(estado.equals("empezarConsultar"))
				{
					return this.accionEmpezarConsultar(excForm,mapping, con);
				}
				else if(estado.equals("empezarBuscarConsultar"))
				{
						return this.accionEmpezarBuscarConsultar(excForm,mapping,con);
				}
				else if(estado.equals("listarConsultar"))
				{
						return this.accionListarConsultar(excForm,mapping,con);
				}
				/*else 
				if(estado.equals("imprimir"))
				{
					this.cerrarConexion(con);
					return mapping.findForward("imprimir");
				}*/
				else
				{
					excForm.reset();
					logger.warn("Estado no valido dentro del flujo de registro Excepciones Tarifarias (null) ");
					request.setAttribute("codigoDescripcionError", "errors.formaTipoInvalido");
					this.cerrarConexion(con);
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
	
	/**
	 * Este método especifica las acciones a realizar en el estado
	 * empezar.
	 * 
	 * @param excForm ExcepcionestarifasForm
	 * 				para pre-llenar datos si es necesario
	 * @param request HttpServletRequest para obtener los 
	 * 				datos
	 * @param mapping Mapping para manejar la navegación
	 * @param con Conexión con la fuente de datos
	 * @return ActionForward a la página principal "filtrarConvenioContrato.jsp"
	 * @throws SQLException
	 */
	private ActionForward accionEmpezar(		ExcepcionesTarifasForm excForm, 
																	 ActionMapping mapping, 
																	 Connection con) throws SQLException
	{
		//Limpiamos lo que venga del form
		excForm.reset();
		excForm.setEstado("empezar");
		this.cerrarConexion(con);
		return mapping.findForward("paginaFiltro");		
	}
	
	/**
	 * Este método especifica las acciones a realizar en el estado
	 * empezarConsultar.
	 * 
	 * @param excForm ExcepcionestarifasForm
	 * 				para pre-llenar datos si es necesario
	 * @param request HttpServletRequest para obtener los 
	 * 				datos
	 * @param mapping Mapping para manejar la navegación
	 * @param con Conexión con la fuente de datos
	 * @return ActionForward a la página principal "filtrarConvenioContrato.jsp"
	 * @throws SQLException
	 */
	private ActionForward accionEmpezarConsultar(		 ExcepcionesTarifasForm excForm, 
																				 ActionMapping mapping, 
																				 Connection con) throws SQLException
	{
		//Limpiamos lo que venga del form
		excForm.reset();
		excForm.setEstado("empezarConsultar");
		this.cerrarConexion(con);
		return mapping.findForward("paginaFiltro");		
	}
	
	/**
	 * Este método especifica las acciones a realizar en el estado
	 * empezarInsertar.
	 * 
	 * @param excForm ExcepcionestarifasForm
	 * 				para pre-llenar datos si es necesario
	 * @param request HttpServletRequest para obtener los 
	 * 				datos
	 * @param mapping Mapping para manejar la navegación
	 * @param con Conexión con la fuente de datos
	 * @return ActionForward a la página principal "excepcionesTarifa.jsp"
	 * @throws SQLException
	 */
	private ActionForward accionEmpezarInsertar		(		ExcepcionesTarifasForm excForm, 
																					 ActionMapping mapping, 
																					 Connection con) throws SQLException
	{
		excForm.setEstado("empezarInsertar");
		this.cerrarConexion(con);
		return mapping.findForward("paginaPrincipal");		
	}
	
	/**
	 * Este método especifica las acciones a realizar en el estado
	 * empezarBuscarModificar
	 * 
	 * @param excForm ExcepcionestarifasForm
	 * 				para pre-llenar datos si es necesario
	 * @param request HttpServletRequest para obtener los 
	 * 				datos
	 * @param mapping Mapping para manejar la navegación
	 * @param con Conexión con la fuente de datos
	 * @return ActionForward a la página principal "excepcionesTarifa.jsp"
	 * @throws SQLException
	 */
	private ActionForward accionEmpezarBuscarModificar		(		ExcepcionesTarifasForm excForm, 
																								ActionMapping mapping, 
																								Connection con) throws SQLException
	{
		excForm.setEstado("empezarBuscarModificar");
		this.cerrarConexion(con);
		return mapping.findForward("paginaBuscarModificar");		
	}
	
	/**
	 * Este método especifica las acciones a realizar en el estado
	 * empezarBuscarConsultar
	 * 
	 * @param excForm ExcepcionestarifasForm
	 * 				para pre-llenar datos si es necesario
	 * @param request HttpServletRequest para obtener los 
	 * 				datos
	 * @param mapping Mapping para manejar la navegación
	 * @param con Conexión con la fuente de datos
	 * @return ActionForward a la página principal "excepcionesTarifa.jsp"
	 * @throws SQLException
	 */
	private ActionForward accionEmpezarBuscarConsultar		(		ExcepcionesTarifasForm excForm, 
																								ActionMapping mapping, 
																								Connection con) throws SQLException
	{
		excForm.setEstado("empezarBuscarConsultar");
		this.cerrarConexion(con);
		return mapping.findForward("paginaBuscarConsultar");		
	}
	
	
	/**
	 * Este método especifica las acciones a realizar en el estado
	 * listarModificar
	 * 
	 * @param excForm ExcepcionestarifasForm
	 * 				para pre-llenar datos si es necesario
	 * @param request HttpServletRequest para obtener los 
	 * 				datos
	 * @param mapping Mapping para manejar la navegación
	 * @param con Conexión con la fuente de datos
	 * @return ActionForward a la página principal "modTarifa.jsp"
	 * @throws SQLException
	 */
	private ActionForward accionListarModificar	(	ExcepcionesTarifasForm excForm, 
																			ActionMapping mapping, 
																			Connection con) throws SQLException
	{
		ExcepcionesTarifas mundo=new ExcepcionesTarifas();  
		mundo.reset();
		//tengo que resetear el hashMap para que no coloque datos erroneos
		excForm.getExcepcionMap().clear();
		enviarItemsSeleccionadosBusqueda(excForm, mundo);
		excForm.setCol(mundo.resultadoBusquedaAvanzada(con));
		if (!excForm.getCol().isEmpty())
		{
			String[] columns={
						        "codigo", 
						        "signoporcentaje",
						        "porcentaje",
						        "signovalorajuste",
						        "valorajuste",
						        "nuevatarifa",
						        "codigoviaingreso",
						        "nombreviaingreso",
						        "codigoespecialidad",
						        "nombreespecialidad",
						        "codigoservicio",
						        "nombreservicio",
						        "codigocontrato",
						        "numerocontrato",
						        "nombreconvenio"
						     };		
			mapa = (HashMap)Listado.convertirCollection(columns,excForm.getCol(),1);			
		}
		this.cerrarConexion(con);
		return mapping.findForward("paginaListarModificar");		
	}
	
	/**
	 * Este método especifica las acciones a realizar en el estado
	 * listarConsultar
	 * 
	 * @param excForm ExcepcionesTarifasForm
	 * 				para pre-llenar datos si es necesario
	 * @param request HttpServletRequest para obtener los 
	 * 				datos
	 * @param mapping Mapping para manejar la navegación
	 * @param con Conexión con la fuente de datos
	 * @return ActionForward a la página principal "modTarifa.jsp"
	 * @throws SQLException
	 */
	private ActionForward accionListarConsultar	(	ExcepcionesTarifasForm excForm, 
																			ActionMapping mapping, 
																			Connection con) throws SQLException
	{
		ExcepcionesTarifas mundo=new ExcepcionesTarifas();  
		mundo.reset();
		enviarItemsSeleccionadosBusqueda(excForm, mundo);
		excForm.setCol(mundo.resultadoBusquedaAvanzada(con));
		this.cerrarConexion(con);
		return mapping.findForward("paginaBuscarConsultar");		
	}
	
	
	/**
	 * Este método especifica las acciones a realizar en el estado
	 * listarResumenModificar
	 * 
	 * @param excForm ExcepcionestarifasForm
	 * 				para pre-llenar datos si es necesario
	 * @param request HttpServletRequest para obtener los 
	 * 				datos
	 * @param mapping Mapping para manejar la navegación
	 * @param con Conexión con la fuente de datos
	 * @return ActionForward a la página principal "modTarifa.jsp"
	 * @throws SQLException
	 */
	private ActionForward accionListarResumenModificar	(	ExcepcionesTarifasForm excForm, 
																						ActionMapping mapping, 
																						Connection con) throws SQLException
	{
		ExcepcionesTarifas mundo=new ExcepcionesTarifas();  
		mundo.reset();
		if(!excForm.getCodigosExcepcionesModificadas().isEmpty())
		{
			excForm.setCol(mundo.cargarResumenModificacion(con, excForm.getCodigosExcepcionesModificadas()));
			this.cerrarConexion(con);
			return mapping.findForward("paginaListarResumenModificar");
		}
		else
		{
			excForm.getCol().clear();
			this.cerrarConexion(con);
			return this.accionListarModificar(excForm,mapping,con);
		}			
	}
	
	
	
	/**
	 * Este método especifica las acciones a realizar en el estado
	 * guardar.
	 * Se copian las propiedades del objeto excepcionesTarifasForm
	 * en el objeto mundo
	 * 
	 * @param excForm ExcepcionesTarifasForm
	 * @param request HttpServletRequest para obtener 
	 * 					datos de la session 
	 * @param mapping Mapping para manejar la navegación
	 * @param con Conexión con la fuente de datos
	 * 
	 * @return ActionForward "excepcionTarifa.do?estado=resumen"
	 * @throws SQLException
	*/
	private ActionForward accionGuardar	(ExcepcionesTarifasForm excForm,
															 HttpServletRequest request, 
															 ActionMapping mapping,
															 Connection con) throws SQLException
	{
		ExcepcionesTarifas mundo=new ExcepcionesTarifas();  
		llenarMundo(excForm,mundo);
		boolean existeExcepcionEnBD =mundo.existeExcepcion(con, -1);
		
		if(!existeExcepcionEnBD)
		{
			boolean validarInsertar=mundo.insertar(con);
				
			if(!validarInsertar)
			{
				logger.warn("No se pudo insertar la Excepción de Tarifas para el contrato: "+excForm.getCodigoContrato());
				this.cerrarConexion(con);
				excForm.reset();
				ArrayList atributosError = new ArrayList();
				request.setAttribute("codigoDescripcionError", "errors.problemasBd");				
				request.setAttribute("atributosError", atributosError);
				return mapping.findForward("paginaError");	
			}
			else
			{
				mundo.reset();
				excForm.reset();
				mundo.cargarResumen(con);
				llenarForm(excForm, mundo);
				this.cerrarConexion(con);									
				return mapping.findForward("paginaResumen");
			}
		}
		else
		{
			ActionErrors errores = new ActionErrors();
			errores.add("inserción excepción", new ActionMessage("error.excepcion.definida"));
			logger.warn("entra al error de Excepción ya existente");
			saveErrors(request, errores);	
			excForm.setEstado("empezarInsertar");
			this.cerrarConexion(con);									
			return mapping.findForward("paginaPrincipal");
		}
	}

	/**
	 * Este método especifica las acciones a realizar en el estado
	 * eliminar.
	 * 
	 * @param excForm ExcepcionesTarifasForm
	 * @param request HttpServletRequest para obtener 
	 * 					datos de la session 
	 * @param mapping Mapping para manejar la navegación
	 * @param con Conexión con la fuente de datos
	 * 
	 * @return ActionForward "excepcionTarifa.do"
	 * @throws SQLException
	*/
	private ActionForward accionEliminar	(ExcepcionesTarifasForm excForm,
															 HttpServletRequest request, 
															 ActionMapping mapping,
															 Connection con) throws SQLException
	{
		ExcepcionesTarifas mundo=new ExcepcionesTarifas();  
		//envio cod a eliminar
		mundo.setCodigoExcepcion(excForm.getCodigoExcepcion());
		
		boolean elimino =mundo.eliminar(con);
		
		if(elimino)
		{
			excForm.setEstado("listarModificarConErrores");
			mundo.reset();
			enviarItemsSeleccionadosBusqueda(excForm, mundo);
			excForm.setCol(mundo.resultadoBusquedaAvanzada(con));
			this.cerrarConexion(con);
			return mapping.findForward("paginaListarModificar");
		}
		else
		{
			ActionErrors errores = new ActionErrors();
			errores.add("eliminar excepción", new ActionMessage("error.excepcion.eliminacion"));
			logger.warn("entra al error NO eliminar la Excepción");
			saveErrors(request, errores);	
			excForm.setEstado("listarModificar");
			this.cerrarConexion(con);									
			return mapping.findForward("paginaListarModificar");
		}
	}
	

	
	/**
	 * Este método especifica las acciones a realizar en el estado
	 * guardarModificar.
	 * 
	 * @param excForm ExcepcionesTarifasForm
	 * @param request HttpServletRequest para obtener 
	 * 					datos de la session 
	 * @param mapping Mapping para manejar la navegación
	 * @param con Conexión con la fuente de datos
	 * 
	 * @return ActionForward "excepcionTarifa.do"
	 * @throws SQLException
	*/
	private ActionForward accionGuardarModificar	(	 ExcepcionesTarifasForm excForm,
																			 HttpServletRequest request, 
																			 ActionMapping mapping,
																			 Connection con,
																			 HttpSession session) throws SQLException
	{
		ExcepcionesTarifas mundo=new ExcepcionesTarifas();  
		DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		excForm.codigosExcepcionesModificadas.clear();
		int indexVector=0;
		
		for (int i=0; i<excForm.getColSize(); i++)
		{
			int tempoCodigoExcepcion=-1, tempoCodigoViaIngreso=-1 , tempoCodigoEspecialidad=-1, tempoCodigoServicio=-1;
			double tempoPorcentaje=0.0,  tempoValorAjuste=0.0, tempoNuevaTarifa=0.0;
			boolean validarModificar=false;
			
			if(excForm.getExcepcionMap("codigosExcepciones_"+i)!=null )
			{
				if(Integer.parseInt(excForm.getExcepcionMap("codigosExcepciones_"+i)+"")>0)
				{	
					tempoCodigoExcepcion=Integer.parseInt(excForm.getExcepcionMap("codigosExcepciones_"+i)+"");
					//solo se valida que sea nulo entonces=-1 para que No lo ponga en el query
					if(excForm.getExcepcionMap("viaIngresoExcepcion_"+i)==null)
					{	
						tempoCodigoViaIngreso=-1;
					}
					//cuando es cero entonces es TODOS, y el resto en BD
					else
					{
						tempoCodigoViaIngreso=Integer.parseInt(excForm.getExcepcionMap("viaIngresoExcepcion_"+i)+"");
					}
					//si el radio1 de la especialidad es diferente de nulo
					if(excForm.getExcepcionMap("especialidadRadio1_"+i)!=null)
					{
						if((!(excForm.getExcepcionMap("especialidadRadio1_"+i)+"").equals("")))
						{	
							try
							{
								//si el radio1 de la especialidad es uno entonces es que la especialidad es todos
								if(Integer.parseInt(excForm.getExcepcionMap("especialidadRadio1_"+i)+"")==1)
								{
									tempoCodigoEspecialidad=0;
								}
							}
							catch(NumberFormatException e)
							{
								tempoCodigoEspecialidad=-1;
							}
						}	
					}	
					else 
					{
						if(excForm.getExcepcionMap("especialidadRadio_"+i)!=null  )
						{
							if(		!(excForm.getExcepcionMap("codigoEspecialidad_"+i)+"").equals("")) 
							{	
								try
								{
									tempoCodigoEspecialidad= Integer.parseInt(excForm.getExcepcionMap("codigoEspecialidad_"+i)+"");
								}
								catch(NumberFormatException e)
								{
									tempoCodigoEspecialidad=-1;
								}	
							}	
						}
						else
						{
							tempoCodigoEspecialidad=-1;
						}
					}
										
					if(excForm.getExcepcionMap("servicioRadio1_"+i)!=null )
					{
						if(		!(excForm.getExcepcionMap("servicioRadio1_"+i)+"").equals(""))
						{	
							try
							{
								//si el radio1 del servicio es uno entonces es que el servicio es todos
								if(Integer.parseInt(excForm.getExcepcionMap("servicioRadio1_"+i)+"")==1)
								{
									tempoCodigoServicio=0;
								}
							}
							catch(NumberFormatException e)
							{
								tempoCodigoServicio=-1;
							}	
						}	
					}
					else  
					{
						if(excForm.getExcepcionMap("servicioRadio_"+i)!=null)
						{
							if(!(excForm.getExcepcionMap("servicioRadio_"+i)+"").equals("")) 
							{
								try
								{
									tempoCodigoServicio= Integer.parseInt(excForm.getExcepcionMap("codigoServicio_"+i)+"");
								}
								catch(NumberFormatException e)
								{
									tempoCodigoServicio=-1;
								}	
							}	
						}
						else
						{
							tempoCodigoServicio=-1;
						}
					}
					
					ActionErrors errores = new ActionErrors();
					errores= excForm.validateHashMap(i);
					
					if( !errores.isEmpty() )
					{
						saveErrors(request, errores);	
						excForm.setEstado("listarModificarConErrores");
						this.cerrarConexion(con);									
						return mapping.findForward("paginaListarModificar");
					}
					else
					{
						try
						{
							if(excForm.getExcepcionMap("porcentaje_"+i) !=null && !(excForm.getExcepcionMap("porcentaje_"+i)+"").equals(""))
							{	
								if((excForm.getExcepcionMap("signoPorcentaje1_"+i)+"").equals("-"))
									tempoPorcentaje= (-1)*Double.valueOf(excForm.getExcepcionMap("porcentaje_"+i)+"").doubleValue();
								else
									tempoPorcentaje= Double.valueOf(excForm.getExcepcionMap("porcentaje_"+i)+"").doubleValue();
							}	
							if(excForm.getExcepcionMap("valorAjuste_"+i) !=null && !(excForm.getExcepcionMap("valorAjuste_"+i)+"").equals(""))	
							{
								if((excForm.getExcepcionMap("signoValorAjuste1_"+i)+"").equals("-"))
									tempoValorAjuste= (-1)*Double.valueOf(excForm.getExcepcionMap("valorAjuste_"+i)+"").doubleValue();
								else
									tempoValorAjuste= Double.valueOf(excForm.getExcepcionMap("valorAjuste_"+i)+"").doubleValue();
							}	
							if(excForm.getExcepcionMap("nuevaTarifa_"+i) !=null && !(excForm.getExcepcionMap("nuevaTarifa_"+i)+"").equals(""))
							{	
								tempoNuevaTarifa= Double.valueOf(excForm.getExcepcionMap("nuevaTarifa_"+i)+"").doubleValue();
							}	
						}
						catch(NumberFormatException e)
						{
							errores.add("", new ActionMessage("errors.floatMayorQue", "El (porcentaje o valor ajuste o nueva tarifa) con código de excepción "+tempoCodigoExcepcion, "0"));
							logger.warn("entra al error formato la Excepción =>"+e);
							saveErrors(request, errores);	
							excForm.setEstado("listarModificar");
							this.cerrarConexion(con);									
							return mapping.findForward("paginaListarModificar");
						}
						
					}
				}
				if(	tempoCodigoViaIngreso==-1 
					&& tempoCodigoEspecialidad==-1 
					&& tempoCodigoServicio==-1 
					&& tempoPorcentaje==0.0
					&& tempoNuevaTarifa==0.0 
					&& tempoValorAjuste==0.0);
				else
				{	
					//se deben validar para los casos todos que inserta null y no sirve el unique de BD
					if(ExcepcionesTarifas.existeExcepcion(con, tempoCodigoViaIngreso, tempoCodigoEspecialidad, excForm.getCodigoContrato(), tempoCodigoServicio, tempoCodigoExcepcion))
					{
						ActionErrors errores = new ActionErrors();
						errores.add("modificar excepción", new ActionMessage("error.excepcion.modificacion", ""+tempoCodigoExcepcion));
						logger.warn("entra al error NO modificar la Excepción ");
						saveErrors(request, errores);	
						excForm.setEstado("listarModificar");
						this.cerrarConexion(con);									
						return mapping.findForward("paginaListarModificar");
					}
					
				    validarModificar= mundo.modificarTransaccional(con, tempoCodigoExcepcion, tempoCodigoViaIngreso, tempoCodigoEspecialidad, tempoCodigoServicio, tempoPorcentaje,tempoValorAjuste, tempoNuevaTarifa, ConstantesBD.inicioTransaccion);
					//en caso de que modifique entonces se aniaden los códigos modificados a un vector para armar la consulta del resumen
					if(validarModificar)
					{
					    excForm.setCodigosExcepcionesModificadas(indexVector, tempoCodigoExcepcion+"");
						indexVector++;							
						if(!excForm.getCodigosExcepcionesModificadas().isEmpty())
						{
						    llenarloghistorial(excForm,tempoCodigoExcepcion);
							Collection colLogs=mundo.cargarResumenModificacion(con, excForm.getCodigosExcepcionesModificadas());
							generarLog(excForm,colLogs,tempoCodigoExcepcion,session);
						}						
					}
					else if(!validarModificar)
					{
						ActionErrors errores = new ActionErrors();
						errores.add("modificar excepción", new ActionMessage("error.excepcion.modificacion", ""+tempoCodigoExcepcion));
						logger.warn("entra al error NO modificar la Excepción ");
						saveErrors(request, errores);	
						excForm.setEstado("listarModificar");
						this.cerrarConexion(con);									
						return mapping.findForward("paginaListarModificar");
					}
				}	
			}	
		}
		myFactory.endTransaction(con);
		return accionListarResumenModificar(excForm, mapping, con);
	}
	
	 /**
	  * Metodo para cargar el Log con los datos actuales, antes de la 
	  * modificación.
	  * @param excForm, ExcepcionesTarifasForm Instancia del Form ExcepcionesTarifasForm. 
	  */
	 private void llenarloghistorial(ExcepcionesTarifasForm excForm, int codigoExc)
	 {
	     int pos=0;
	    
	     for(pos=0; pos<mapa.size(); pos++)
	     {    
	         if( (mapa.get("codigo_"+pos)+"").equals(codigoExc+"") )
	         {
	             excForm.setLog("\n          ====INFORMACION ORIGINAL===== " +
								"\n*  Via de Ingreso ["+mapa.get("nombreviaingreso_"+pos)+""+"] " +
				 	 			"\n*  Especialidad  ["+mapa.get("nombreespecialidad_"+pos)+""+"] "+
				 	 			"\n*  Servicio ["+mapa.get("nombreservicio_"+pos)+""+"] "+
				 	 			"\n*  % Excepción ["+mapa.get("signoporcentaje_"+pos)+""+" "+mapa.get("porcentaje_"+pos)+""+"] "+
				 	 			"\n*  Valor de ajuste ["+mapa.get("signovalorajuste_"+pos)+""+" "+mapa.get("valorajuste_"+pos)+""+"] "+	 			
				 	 			"\n*  Nueva tarifa ["+mapa.get("nuevatarifa_"+pos)+""+"] "+										
	             				"\n========================================================\n\n\n " );
	             pos=mapa.size();
	         }
	     }	
	 }
	 
	 /**
	  * Metodo para generar el Log, y añadir los cambios realizados.
	  * @param excForm, Instancia del Form ExcepcionesTarifasForm.
	  * @param session, Session para obtener el usuario.
	  */
	 private void generarLog( ExcepcionesTarifasForm forma, Collection colLogs, int codigoExc,  HttpSession session)
	    {
	        UsuarioBasico usuario;
	        int pos=0;
	        
	        if (!colLogs.isEmpty())
			{
				String[] columns={
							        "codigo", 
							        "signoporcentaje",
							        "porcentaje",
							        "signovalorajuste",
							        "valorajuste",
							        "nuevatarifa",
							        "codigoviaingreso",
							        "nombreviaingreso",
							        "codigoespecialidad",
							        "nombreespecialidad",
							        "codigoservicio",
							        "nombreservicio",
							        "codigocontrato",
							        "numerocontrato",
							        "nombreconvenio"
							     };		
				HashMap mapa2 = (HashMap)Listado.convertirCollection(columns,colLogs,1);			
				String log="";
				
				for(pos=0; pos<mapa2.size(); pos++)
				{    
					if( (mapa2.get("codigo_"+pos)+"").equals(codigoExc+"") )
					{
					    log=forma.getLog() +
													"\n            ====INFORMACION DESPUES DE LA MODIFICACION===== " +
													"\n*  Via de Ingreso ["+mapa2.get("nombreviaingreso_"+pos)+""+"] " +
									 	 			"\n*  Especialidad  ["+mapa2.get("nombreespecialidad_"+pos)+""+"] "+
									 	 			"\n*  Servicio ["+mapa2.get("nombreservicio_"+pos)+""+"] "+
									 	 			"\n*  % Excepción ["+mapa2.get("signoporcentaje_"+pos)+""+" "+mapa2.get("porcentaje_"+pos)+""+"] "+
									 	 			"\n*  Valor de ajuste ["+mapa2.get("signovalorajuste_"+pos)+""+" "+mapa2.get("valorajuste_"+pos)+""+"] "+	 			
									 	 			"\n*  Nueva tarifa ["+mapa2.get("nuevatarifa_"+pos)+""+"] "+										
												"\n========================================================\n\n\n " ;
					    pos=mapa2.size();
					}
				}	
				if(!log.equals(""))
				{	
				    usuario=(UsuarioBasico)session.getAttribute("usuarioBasico");
					LogsAxioma.enviarLog(ConstantesBD.logExcepcionesTarifasCodigo,log,ConstantesBD.tipoRegistroLogModificacion,usuario.getLoginUsuario());
				}
			}
	        
	    }
	
	/**
	 * Método que carga los datos pertinentes desde el 
	 * form ExcepcionesTarifasForm para el mundo de ExcepcionesTarifas
	 * @param excForm ExcepcionesTarifasForm (forma)
	 * @param mundo ExcepcionesTarifas (mundo)
	 */
	protected void llenarMundo(ExcepcionesTarifasForm excForm, ExcepcionesTarifas mundo)
	{
		mundo.setCodigoContrato(excForm.getCodigoContrato());
		mundo.setCodigoServicio(excForm.getCodigoServicio());
		mundo.setCodigoEspecialidad(excForm.getCodigoEspecialidad());
		mundo.setCodigoViaIngreso(excForm.getCodigoViaIngreso());
		
		if( excForm.getRadioExcepcion() == 1 )
			mundo.setPorcentaje(excForm.getPorcentaje());
		else	if(excForm.getRadioExcepcion() == 2 )
			mundo.setValorAjuste(excForm.getValorAjuste());
		else if( excForm.getRadioExcepcion() == 3 )
			mundo.setNuevaTarifa(excForm.getNuevaTarifa());
	}
	
	/**
	 * Método que carga los datos pertinentes desde el 
	 * form ExcepcionesTarifasForm para el mundo de ExcepcionesTarifas
	 * para hacer la respectiva busqueda
	 * @param excForm ExcepcionesTarifasForm (forma)
	 * @param mundo ExcepcionesTarifas (mundo)
	 */
	protected void enviarItemsSeleccionadosBusqueda(ExcepcionesTarifasForm excForm, ExcepcionesTarifas mundo)
	{	
		int tempo=0;
		if(excForm.getRadioBusquedaPorCodigoNombreServicio()==1)
			mundo.setNombreServicio(excForm.getCriterioBusquedaServicio());
		else if(excForm.getRadioBusquedaPorCodigoNombreServicio()==2)
		{	
			try
			{
				tempo= Integer.parseInt(excForm.getCriterioBusquedaServicio());
			}
			catch(NumberFormatException e)
			{
				logger.warn("entra al error formato numerico servicio");
				tempo=0;
			}
			mundo.setCodigoServicio(tempo);
		}	
		else
		{
			mundo.setNombreServicio("");
			mundo.setCodigoServicio(tempo);
		}
		if(excForm.getRadioBusquedaPorCodigoNombreEspecialidad()==1)
			mundo.setNombreEspecialidad(excForm.getCriterioBusquedaEspecialidad());
		else if(excForm.getRadioBusquedaPorCodigoNombreEspecialidad()==2)
		{
			try
			{
				tempo= Integer.parseInt(excForm.getCriterioBusquedaEspecialidad());
			}
			catch(NumberFormatException e)
			{
				logger.warn("entra al error formato numerico especialidad");
				tempo=-1;
			}
			mundo.setCodigoEspecialidad(tempo);
		}	
		else
		{
			mundo.setNombreEspecialidad("");
			mundo.setCodigoEspecialidad(-1);
		}
		mundo.setCodigoContrato(excForm.getCodigoContrato());
		mundo.setCodigoViaIngreso(excForm.getCodigoViaIngreso());
		
		if( excForm.getRadioExcepcion() == 1 )
		{
			if(excForm.getSignoPorcentaje()=='-')
				mundo.setPorcentaje((-1)*excForm.getPorcentaje());
			else
				mundo.setPorcentaje(excForm.getPorcentaje());
		}	
		else	if(excForm.getRadioExcepcion() == 2 )
		{	
			if(excForm.getSignoValorAjuste()=='-')
				mundo.setValorAjuste((-1)*excForm.getValorAjuste());
			else
				mundo.setValorAjuste(excForm.getValorAjuste());
		}	
		else if( excForm.getRadioExcepcion() == 3 )
			mundo.setNuevaTarifa(excForm.getNuevaTarifa());
	}

	
	/**
	 * Método que carga los datos pertinentes desde  
	 * el mundo de ExcepcionesTarifas para el form ExcepcionesTarifasForm 
	 * @param excForm ExcepcionesTarifasForm (forma)
	 * @param mundo ExcepcionesTarifas (mundo)
	 */
	protected void llenarForm(ExcepcionesTarifasForm excForm, ExcepcionesTarifas mundo)
	{
		excForm.setCodigoContrato(mundo.getCodigoContrato());
		excForm.setNumeroContrato(mundo.getNumeroContrato());
		excForm.setCodigoServicio(mundo.getCodigoServicio());
		excForm.setNombreServicio(mundo.getNombreServicio());
		excForm.setCodigoEspecialidad(mundo.getCodigoEspecialidad());
		excForm.setNombreEspecialidad(mundo.getNombreEspecialidad());
		excForm.setCodigoViaIngreso(mundo.getCodigoViaIngreso());
		excForm.setNombreViaIngreso(mundo.getNombreViaIngreso());
		
		excForm.setPorcentaje(mundo.getPorcentaje());
		excForm.setValorAjuste(mundo.getValorAjuste());
		excForm.setNuevaTarifa(mundo.getNuevaTarifa());
	}
	
	/**
	 * Método en que se cierra la conexión (Buen manejo
	 * recursos), usado ante todo al momento de hacer
	 * un forward
	 * @param con Conexión con la fuente de datos
	 * @throws SQLException
	 */
	public void cerrarConexion (Connection con) throws SQLException
	{
			if (con!=null&&!con.isClosed())
			{
				UtilidadBD.closeConnection(con);
			}
	}
	
}
