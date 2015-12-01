/*
 * @(#)ExcepcionesTarifasAction.java
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
import util.InfoDatosInt;
import util.Listado;
import util.LogsAxioma;
import util.UtilidadBD;

import com.princetonsa.actionform.cargos.ExcepcionesServiciosForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.cargos.ExcepcionesServicios;

/**
 *   Action, controla todas las opciones dentro de las excepciones 
 * 	 de servicios incluyendo los posibles casos de error. 
 *   Y los casos de flujo.
 * @version 1.0, Julio 21, 2004
 * @author <a href="mailto:wilson@PrincetonSA.com">Wilson Ríos</a>
 */
public class ExcepcionesServiciosAction extends Action 
{
	/**
	 * Objeto para manejar los logs de esta clase
	*/
	private Logger logger = Logger.getLogger(ExcepcionesServiciosAction.class);
		
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
		if(form instanceof ExcepcionesServiciosForm)
		{
				
				try
				{
						con = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection();	
				}
				catch(SQLException e)
				{
						logger.warn("No se pudo abrir la conexión"+e.toString());
				}
				
				UsuarioBasico usuario= (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
				ExcepcionesServiciosForm excForm =(ExcepcionesServiciosForm)form;
				
				String estado=excForm.getEstado(); 
						
				if(estado == null)
				{
						excForm.reset();	
						logger.warn("Estado no valido dentro del flujo de ExcepcionesServicios (null) ");
						request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
						this.cerrarConexion(con);
						return mapping.findForward("paginaError");
				}
				else if (estado.equals("empezar"))
				{
						return this.accionEmpezarFiltro(excForm,mapping, con);
				}
				else if (estado.equals("ingresar"))
				{
						return this.accionIngresar(excForm,mapping, con);
				}
				else if(estado.equals("salir"))
				{
						return this.accionSalir(excForm,mapping,request,con);
				}
				else if(estado.equals("eliminar"))
				{
						return this.accionEliminar(excForm,mapping,usuario,con);
				}
				else if(estado.equals("resumen"))
				{
						return this.accionResumen(excForm,mapping,request, con);
				}
				else if(estado.equals("listar")||estado.equals("listarModificar"))
				{
						return this.accionListarExc(excForm,mapping,con,estado);
				}
				else if(estado.equals("ordenar"))
				{
					   return this.accionOrdenar(excForm,mapping,request,con);
				}
				else if(estado.equals("busquedaAvanzada"))
				{
						return this.accionBusquedaAvanzada(excForm,mapping,con);
				}
				else if(estado.equals("resultadoBusquedaAvanzada"))
				{
						return this.accionResultadoBusquedaAvanzada(excForm,mapping,con);
				}
				else if(estado.equals("resumenModificar"))
				{
						return this.accionResumenModificar(excForm,mapping,request, con);
				}
				else if(estado.equals("modificar"))
				{
						return this.accionModificar(excForm,request,mapping,con);
				}
				else if(estado.equals("guardarModificacion"))
				{
						return this.accionGuardarModificacion(excForm,mapping,usuario,con);
				}
				else
				{
						excForm.reset();
						logger.warn("Estado no valido dentro del flujo de ExcepcionesServicios (null) ");
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
	 * @param excForm ExcepcionesServiciosForm
	 * 				para pre-llenar datos si es necesario
	 * @param request HttpServletRequest para obtener los 
	 * 				datos
	 * @param mapping Mapping para manejar la navegación
	 * @param con Conexión con la fuente de datos
	 * @return ActionForward a la página principal "excepcionesServicios.jsp"
	 * 
	 */
	private ActionForward accionEmpezarFiltro(ExcepcionesServiciosForm excForm, 
																	 ActionMapping mapping, 
																	 Connection con) throws SQLException 
	{
		//Limpiamos lo que venga del form
		excForm.reset();
		excForm.setEstado("empezar");
		this.cerrarConexion(con);
		return mapping.findForward("principal");		
	}
	
	private ActionForward accionIngresar(		ExcepcionesServiciosForm excForm, 
																ActionMapping mapping, 
																Connection con) throws SQLException 
	{
		//Limpiamos lo que venga del form
		excForm.setEstado("ingresar");
		this.cerrarConexion(con);
		return mapping.findForward("principal");		
	}

	/**
	 * Este método especifica las acciones a realizar en el estado
	 * salir.
	 * Se copian las propiedades del objeto ExcepcionesServiciosForm
	 * en el objeto mundo
	 * 
	 * @param excForm ExcepcionesServiciosForm
	 * @param request HttpServletRequest para obtener 
	 * 					datos de la session 
	 * @param mapping Mapping para manejar la navegación
	 * @param con Conexión con la fuente de datos
	 * 
	 * @return ActionForward "exc.do?estado=resumen"
	 * @throws SQLException
	*/
	private ActionForward accionSalir(ExcepcionesServiciosForm excForm,
															 ActionMapping mapping,
															 HttpServletRequest request, 
															 Connection con) throws SQLException
	{
		ExcepcionesServicios mundoExc=new ExcepcionesServicios();  
		llenarMundo(excForm, mundoExc);
		boolean validarRepeticion= mundoExc.hayRepetidas(con);
		if(validarRepeticion)
		{
			ActionErrors errores = new ActionErrors();
			errores.add("", new ActionMessage("error.cargo.hayRepetidos"));
			logger.warn("entra al de datos repetidos en la excepción de servicios");
			saveErrors(request, errores);	
			excForm.setEstado("ingresar");
			this.cerrarConexion(con);									
			return mapping.findForward("principal");
		}
		else
		{	
			mundoExc.insertarExc(con);
			this.cerrarConexion(con);									
			return mapping.findForward("funcionalidadResumenExc");
		}	
	}
	
	/**
	 * Este método especifica las acciones a realizar en el estado
	 * resumen
	 * @param excForm ExcepcionesServiciosForm
	 * @param mapping Mapping para manejar la navegación
	 * @param con Conexión con la fuente de datos
	 * @return ActionForward a la página de resumen "resumenExcepcionesServicios.jsp"
	 * @throws SQLException
	 */
	private ActionForward accionResumen(	ExcepcionesServiciosForm excForm, 
																	 	ActionMapping mapping, 
																		HttpServletRequest request, 
																	 	Connection con) throws SQLException
	{
		ExcepcionesServicios mundoExc=new ExcepcionesServicios();  	
		boolean validarCargar=mundoExc.cargarResumen(con,excForm.getCodigoServicio(), excForm.getCodigoContrato());
		if(validarCargar)
		{
			llenarForm(excForm,mundoExc);
			this.cerrarConexion(con);		
			return mapping.findForward("paginaResumenExc");
		}
		else
		{
			logger.warn("Código Servicio inválido "+excForm.getCodigoServicio());
			this.cerrarConexion(con);
			excForm.reset();
			ArrayList atributosError = new ArrayList();
			atributosError.add(" Código Servicio ");
			request.setAttribute("codigoDescripcionError", "errors.invalid");				
			request.setAttribute("atributosError", atributosError);
			return mapping.findForward("paginaError");		
		}
	}
	
	/**
	 * Este método especifica las acciones a realizar en el estado
	 * listar ExcepcionesServicios
	 * @param excForm ExcepcionesServiciosForm
	 * @param mapping Mapping para manejar la navegación
	 * @param con Conexión con la fuente de datos
	 * @return ActionForward  a la página "listadoExcepcionesServicios.jsp"
	 * @throws SQLException
	 */
	private ActionForward accionListarExc	(	ExcepcionesServiciosForm excForm,
																	ActionMapping mapping,
																	Connection con,String estado) throws SQLException 
	{
		ExcepcionesServicios mundoExc= new ExcepcionesServicios();
		excForm.setEstado(estado);
		excForm.setCol(mundoExc.listadoExc(con));
		this.cerrarConexion(con);
		return mapping.findForward("paginaListar")	;	
	}	

	private ActionForward accionOrdenar(	ExcepcionesServiciosForm excForm,
			ActionMapping mapping,
			HttpServletRequest request, 
			Connection con) throws SQLException
	{
		try
		{
			excForm.setCol(Listado.ordenarColumna(new ArrayList(excForm.getCol()),excForm.getUltimaPropiedad(),excForm.getColumna()));
			excForm.setUltimaPropiedad(excForm.getColumna());
			this.cerrarConexion(con);
		}
		catch(Exception e)
		{
			logger.warn("Error en el listado de excepciones ");
			this.cerrarConexion(con);
			excForm.reset();
			ArrayList atributosError = new ArrayList();
			atributosError.add(" Listado ExcepcionesServicios");
			request.setAttribute("codigoDescripcionError", "errors.invalid");				
			request.setAttribute("atributosError", atributosError);
			return mapping.findForward("paginaError");		
		}
		excForm.setEstado(excForm.getEstadoTemp());
		return mapping.findForward("paginaListar")	;
	}	

	/**
	 * Este método especifica las acciones a realizar en el estado
	 * busquedaAvanzada
	 * 
	 * @param excForm ExcepcionesServiciosForm
	 * 				para pre-llenar datos si es necesario
	 * @param request HttpServletRequest para obtener los 
	 * 				datos
	 * @param mapping Mapping para manejar la navegación
	 * @param con Conexión con la fuente de datos
	 * @return ActionForward a la página funcionalidadBuscarExc "busquedaExcepcionesServicios.jsp"
	 * @throws SQLException
	 */
	private ActionForward accionBusquedaAvanzada(	ExcepcionesServiciosForm excForm, 
																				ActionMapping mapping, 
																				Connection con) throws SQLException
	{
		//Limpiamos lo que venga del form
		excForm.reset();
		excForm.setEstado("busquedaAvanzada");
		this.cerrarConexion(con);
		return mapping.findForward("paginaBusqueda");		
	}
	
	/**
	 * Este método especifica las acciones a realizar en el estado
	 * resultadoBusquedaAvanzada.
	 * 
	 * @param excForm ExcepcionesServiciosForm
	 * 				para pre-llenar datos si es necesario
	 * @param request HttpServletRequest para obtener los 
	 * 				datos
	 * @param mapping Mapping para manejar la navegación
	 * @param con Conexión con la fuente de datos
	 * @return ActionForward a la página funcionalidadBuscarExc "busquedaExcepcionesServicios.jsp"
	 * @throws SQLException
	 */
	private ActionForward accionResultadoBusquedaAvanzada(		ExcepcionesServiciosForm excForm, 
																								ActionMapping mapping, 
																								Connection con) throws SQLException
	{
		ExcepcionesServicios mundoExc= new ExcepcionesServicios();
		mundoExc.reset();
		enviarItemsSeleccionadosBusqueda(excForm, mundoExc);
		excForm.resetCriteriosBusqueda();
		excForm.setCol(mundoExc.resultadoBusquedaAvanzada(con));
		this.cerrarConexion(con);
		return mapping.findForward("paginaListar");
	}

	private void enviarItemsSeleccionadosBusqueda(ExcepcionesServiciosForm excForm, ExcepcionesServicios mundoExc)
	{
		String bb[]= excForm.getCriteriosBusqueda();
		for(int i=0; i<bb.length; i++)
		{
			try
			{
				if(bb[i].equals("descripcionServicio"))
					mundoExc.setDescripcionServicio(excForm.getDescripcionServicio());
				if(bb[i].equals("nombreConvenio"))
					mundoExc.setNombreConvenio(excForm.getNombreConvenio());
				if(bb[i].equals("numeroContrato"))
					mundoExc.setNumeroContrato(excForm.getNumeroContrato());
				if(bb[i].equals("esposAux"))
					mundoExc.setEsposAux(excForm.getEsposAux());
			}
			catch (Exception e)
			{
				logger.warn("Error en enviarItemsSeleccionados "+e);
			}
		}		
	}

	/**
	 * Este método especifica las acciones a realizar en el estado
	 * modificar ExcepcionesServicios
	 * @param excForm ExcepcionesServiciosForm
	 * @param mapping Mapping para manejar la navegación
	 * @param con Conexión con la fuente de datos
	 * @return ActionForward  a la página "modificarExcepcionesServicios.jsp"
	 * @throws SQLException
	 */
	private ActionForward accionModificar(ExcepcionesServiciosForm excForm, 
																	HttpServletRequest request, 
																	ActionMapping mapping, 
																	Connection con) throws SQLException
	{
		ExcepcionesServicios mundoExc=new ExcepcionesServicios();
		mundoExc.reset();
		mundoExc.cargarResumen(con, excForm.getCodigoServicio(), excForm.getCodigoContrato());
		
		llenarForm(excForm,mundoExc);
		excForm.setEstado("modificar");
		excForm.setCodigoServicioTemp(excForm.getCodigoServicio());
		
		
		if(excForm.getCodigoServicio()<0)
		{
			logger.warn("No se pudo cargar la excepción de servicio: "+excForm.getCodigoServicio());
			this.cerrarConexion(con);
			excForm.reset();
			ArrayList atributosError = new ArrayList();
			atributosError.add("El código del servicio ");
			request.setAttribute("codigoDescripcionError", "errors.invalid");				
			request.setAttribute("atributosError", atributosError);
			return mapping.findForward("paginaError");	
		}
		else
		{
				String log="\n            ====INFORMACION ORIGINAL===== " +
				"\n*  Código Servicio [" +mundoExc.getCodigoServicio() +"] "+
				"\n*  Código Axioma ["+mundoExc.getCodigoAxioma()+"] "+
				"\n*  Código Convenio ["+mundoExc.getCodigoConvenio()+"] " +
				"\n*  Nombre del Convenio["+mundoExc.getNombreConvenio()+"] " +
				"\n*  Código Contrato["+mundoExc.getCodigoContrato()+"]" +
				"\n*  Número  del Contrato["+mundoExc.getNumeroContrato()+"]"; 
				
			excForm.setLogInfoOriginal(log);
				
			this.cerrarConexion(con);
			return mapping.findForward("principal");
		}
	}	

	/**
	 * Este método especifica las acciones a realizar en el estado
	 * guardarModificacion
	 * @param excForm ExcepcionesServiciosForm
	 * @param request HttpServletRequest para obtener 
	 *					datos de la session 
	 * @param mapping Mapping para manejar la navegación
	 * @param con Conexión con la fuente de datos
	 * @return ActionForward "exc.do?estado=modificar"
	 * @throws SQLException
	 */
	private ActionForward accionGuardarModificacion(			ExcepcionesServiciosForm excForm,
																						ActionMapping mapping, 
																						UsuarioBasico usuario,
																						Connection con)	
																						throws SQLException
	{
		ExcepcionesServicios mundoExc=new ExcepcionesServicios ();  
		llenarMundo(excForm, mundoExc);
		
		boolean validarRepeticion= mundoExc.hayRepetidas(con);
		if(validarRepeticion)
		{
				/*ActionErrors errores = new ActionErrors();
				errores.add("", new ActionMessage("error.cargo.hayRepetidos"));
				logger.warn("entra al de datos repetidos en la excepción de servicios");
				saveErrors(request, errores);*/	
				excForm.setEstado("modificar");
				this.cerrarConexion(con);									
				return mapping.findForward("principal");
		}
		else
		{	
				mundoExc.setCodigoServicioNuevo(excForm.getCodigoServicio());
				mundoExc.setCodigoServicio(excForm.getCodigoServicioTemp());
				int a= mundoExc.modificarExc(con);
				boolean validarCargar= mundoExc.cargarResumen(con,excForm.getCodigoServicio(), excForm.getCodigoContrato());
				
				if(a>0 && validarCargar==true)
				{
					String log=excForm.getLogInfoOriginal()+
							"\n          =====INFORMACION DESPUES DE LA MODIFICACION===== " +
							"\n*  Código Servicio [" +mundoExc.getCodigoServicioNuevo() +"] "+
							"\n*  Código Axioma ["+mundoExc.getCodigoAxioma()+"] "+
							"\n*  Código Convenio ["+mundoExc.getCodigoConvenio()+"] " +
							"\n*  Nombre del Convenio["+mundoExc.getNombreConvenio()+"] " +
							"\n*  Código Contrato["+mundoExc.getCodigoContrato()+"]" +
							"\n*  Número  del Contrato["+mundoExc.getNumeroContrato()+"]"; 
						
					log+="\n========================================================\n\n\n " ;		
						
					LogsAxioma.enviarLog(ConstantesBD.logExcepcionesServiciosCodigo, log, ConstantesBD.tipoRegistroLogModificacion, usuario.getLoginUsuario());
				}
				this.cerrarConexion(con);									
				return mapping.findForward("funcionalidadResumenModificarExc");
			}	
	}
	
	private ActionForward accionEliminar(			ExcepcionesServiciosForm excForm,
																	ActionMapping mapping, 
																	UsuarioBasico usuario,
																	Connection con)	
																	throws SQLException
    {
		ExcepcionesServicios mundoExc=new ExcepcionesServicios ();  
		mundoExc.cargarResumen(con, excForm.getCodigoServicio(), excForm.getCodigoContrato());
		mundoExc.eliminar(con);
		String log="\n            ====INFORMACION ORIGINAL===== " +
		"\n*  Código Servicio [" +mundoExc.getCodigoServicio() +"] "+
		"\n*  Código Axioma ["+mundoExc.getCodigoAxioma()+"] "+
		"\n*  Código Convenio ["+mundoExc.getCodigoConvenio()+"] " +
		"\n*  Nombre del Convenio["+mundoExc.getNombreConvenio()+"] " +
		"\n*  Código Contrato["+mundoExc.getCodigoContrato()+"]" +
		"\n*  Número  del Contrato["+mundoExc.getNumeroContrato()+"] "+ 
		"\n* 	====>>>> FUE ELIMINADO\n\n\n";
		LogsAxioma.enviarLog(ConstantesBD.logExcepcionesServiciosCodigo, log, ConstantesBD.tipoRegistroLogEliminacion, usuario.getInformacionGeneralPersonalSalud());
			
		this.cerrarConexion(con);
		return mapping.findForward("paginaMensajeBorrado");
	}


	/**
	 * Este método especifica las acciones a realizar en el estado
	 * resumenModificar
	 * @param excForm ExcepcionesServiciosForm
	 * @param mapping Mapping para manejar la navegación
	 * @param con Conexión con la fuente de datos
	 * @return ActionForward a la página de resumen "resumenExcepcionesServicios.jsp"
	 * @throws SQLException
	 */
	private ActionForward accionResumenModificar(		ExcepcionesServiciosForm excForm, 
																							ActionMapping mapping, 
																							HttpServletRequest request, 
																							Connection con) throws SQLException
	{
		ExcepcionesServicios mundoExc = new ExcepcionesServicios();  	
		boolean validarCargar=mundoExc.cargarResumen(con,excForm.getCodigoServicio(), excForm.getCodigoContrato());
		if(validarCargar)
		{
			llenarForm(excForm,mundoExc);
			this.cerrarConexion(con);		
			return mapping.findForward("paginaResumenExc");
		}
		else
		{
			logger.warn("Excepcion de Servicio modificado por otro usuario codServicio=>"+excForm.getCodigoServicio());
			this.cerrarConexion(con);
			excForm.reset();
			ArrayList atributosError = new ArrayList();
			atributosError.add(" Excepción de servicio ");
			request.setAttribute("codigoDescripcionError", "errors.excepcionSQL.registroYaActualizado");				
			request.setAttribute("atributosError", atributosError);
			return mapping.findForward("paginaError");		
		}
	}

	/**
	 * Este método carga los datos pertinentes a la forma 
	 * @param excForm (form)
	 * @param mundoExc (mundo)
	 */
	protected void llenarForm(ExcepcionesServiciosForm excForm, ExcepcionesServicios mundoExc)
	{
		excForm.setCodigoServicio(mundoExc.getCodigoServicio());
		excForm.setDescripcionServicio(mundoExc.getDescripcionServicio());
		excForm.setCodigoConvenio(mundoExc.getCodigoConvenio());
		excForm.setNombreConvenio(mundoExc.getNombreConvenio());
		excForm.setCodigoContrato(mundoExc.getCodigoContrato());
		excForm.setNumeroContrato(mundoExc.getNumeroContrato());

		excForm.setCodigoServicioNuevo(mundoExc.getCodigoServicioNuevo());
		excForm.setDescripcionServicioNuevo(mundoExc.getDescripcionServicioNuevo());
		
		excForm.setCodigoAxioma(mundoExc.getCodigoAxioma());
		excForm.setFechasVigenciaContrato(mundoExc.getFechasVigenciaContrato());
		excForm.setTipoPos(mundoExc.getTipoPos());
	}
	
	/**
	 * Método que carga los datos pertinentes desde el 
	 * form ExcepcionesServiciosForm para el mundo de ExcepcionesServicios
	 * @param excForm ExcepcionesServiciosForm (forma)
	 * @param mundoExc ExcepcionesServicios (mundo)
	 */
	protected void llenarMundo(ExcepcionesServiciosForm excForm, ExcepcionesServicios mundoExc)
	{
		mundoExc.setServicio(new InfoDatosInt(excForm.getCodigoServicio(), excForm.getDescripcionServicio()));
		mundoExc.setConvenio(new InfoDatosInt(excForm.getCodigoConvenio(), excForm.getNombreConvenio()));
		mundoExc.setContrato(new InfoDatosInt(excForm.getCodigoContrato(), excForm.getNumeroContrato()));
		
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
