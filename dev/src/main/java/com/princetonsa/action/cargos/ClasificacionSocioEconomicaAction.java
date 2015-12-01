/*
 * @(#)ClasificacionSocioEconomicaAction.java
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
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import util.ConstantesBD;
import util.InfoDatos;
import util.LogsAxioma;
import util.UtilidadBD;

import com.princetonsa.actionform.cargos.ClasificacionSocioEconomicaForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.cargos.ClasificacionSocioEconomica;
 
/**  
 *   Action, controla todas las opciones dentro del registro de
 *  las clasificaciones económicas, incluyendo los posibles casos de error. 
 *   Y los casos de flujo.
 * @version 1.0, Junio 11, 2004
 * @author <a href="mailto:wilson@PrincetonSA.com">Wilson Ríos</a>
 */
public class ClasificacionSocioEconomicaAction extends Action 
{
	/**
	 * Objeto para manejar los logs de esta clase
	*/
	private Logger logger = Logger.getLogger(ClasificacionSocioEconomicaAction.class);
		
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
		if (response==null); //Para evitar que salga el warning
		if(form instanceof ClasificacionSocioEconomicaForm)
		{
				Connection con=null;
				try
				{
						con = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection();	
				}
				catch(SQLException e)
				{
						logger.warn("No se pudo abrir la conexión"+e.toString());
				}
				
				ClasificacionSocioEconomicaForm clasificacionForm =(ClasificacionSocioEconomicaForm)form;
				UsuarioBasico usuario= (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
				
				
				String estado=clasificacionForm.getEstado(); 
						
				if(estado == null)
				{
						clasificacionForm.reset();	
						logger.warn("Estado no valido dentro del flujo de la clasificación de estrato (null) ");
						request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
						this.cerrarConexion(con);
						return mapping.findForward("paginaError");
				}
				else if (estado.equals("empezar"))
				{
						return this.accionEmpezar(clasificacionForm,mapping, con);
				}
				else if(estado.equals("salir"))
				{
						return this.accionSalir(clasificacionForm,mapping,con,usuario.getCodigoInstitucionInt());
				}
				else if(estado.equals("resumen"))
				{
						return this.accionResumen(clasificacionForm,mapping,request, con);
				}
				else if(estado.equals("listar")||estado.equals("listarModificar"))
				{
						return this.accionListar(clasificacionForm,mapping,con,estado,usuario.getCodigoInstitucionInt());
				}
				else if(estado.equals("busquedaAvanzada"))
				{
						return this.accionBusquedaAvanzada(clasificacionForm,mapping,con);
				}
				else if(estado.equals("resultadoBusquedaAvanzada"))
				{
						return this.accionResultadoBusquedaAvanzada(clasificacionForm,mapping,con, usuario.getCodigoInstitucionInt());
				}
				else if(estado.equals("imprimir"))
				{
						this.cerrarConexion(con);
						return mapping.findForward("imprimir");	
				}				
				else if(estado.equals("resumenModificar"))
				{
						return this.accionResumenModificar(clasificacionForm,mapping,request, con);
				}
				else if(estado.equals("modificar"))
				{
						return this.accionModificar(clasificacionForm,request,mapping,con);
				}
				else if(estado.equals("guardarModificacion"))
				{
						return this.accionGuardarModificacion(clasificacionForm,request,mapping,con);
				}
				else
				{
						clasificacionForm.reset();
						logger.warn("Estado no valido dentro del flujo de clasificación (null) ");
						request.setAttribute("codigoDescripcionError", "errors.formaTipoInvalido");
						this.cerrarConexion(con);
						return mapping.findForward("paginaError");
				}
		}			
		return null;	
	}	
	
	/**
	 * Este método especifica las acciones a realizar en el estado
	 * empezar.
	 * 
	 * @param clasificacionForm ClasificacionForm
	 * 				para pre-llenar datos si es necesario
	 * @param request HttpServletRequest para obtener los 
	 * 				datos
	 * @param mapping Mapping para manejar la navegación
	 * @param con Conexión con la fuente de datos
	 * @return ActionForward a la página principal "clasificacionSocioEconomica.jsp"
	 * 
	 */
	private ActionForward accionEmpezar(ClasificacionSocioEconomicaForm clasificacionForm, 
																	 ActionMapping mapping, 
																	 Connection con) throws SQLException 
	{
		//Limpiamos lo que venga del form
		clasificacionForm.reset();
		clasificacionForm.setEstado("empezar");
		this.cerrarConexion(con);
		return mapping.findForward("principal");		
	}

	/**
	 * Este método especifica las acciones a realizar en el estado
	 * salir.
	 * Se copian las propiedades del objeto clasificacionSocioEconomica
	 * en el objeto mundo
	 * 
	 * @param clasificacionForm ClasificacionForm
	 * @param request HttpServletRequest para obtener 
	 * 					datos de la session 
	 * @param mapping Mapping para manejar la navegación
	 * @param con Conexión con la fuente de datos
	 * @param institucion Codigo de la institucion a la que pertenece el usuario
	 * 
	 * @return ActionForward "clasificacion.do?estado=resumen"
	 * @throws SQLException
	*/
	private ActionForward accionSalir(ClasificacionSocioEconomicaForm clasificacionForm,
															 ActionMapping mapping,
															 Connection con, int institucion) throws SQLException
	{
		ClasificacionSocioEconomica mundoClasificacion=new ClasificacionSocioEconomica();  
		llenarMundo(clasificacionForm, mundoClasificacion);
		
		mundoClasificacion.insertarClasificacion(con,institucion);
		this.cerrarConexion(con);									
		return mapping.findForward("funcionalidadResumenClasificacionSocioEconomica");
	}

	/**
	 * Este método especifica las acciones a realizar en el estado
	 * resumen
	 * @param ClasificacionSocioEconomicaForm clasificacionForm
	 * @param mapping Mapping para manejar la navegación
	 * @param con Conexión con la fuente de datos
	 * @return ActionForward a la página de resumen "resumenClasificacionSocioEconomica.jsp"
	 * @throws SQLException
	 */
	private ActionForward accionResumen(	ClasificacionSocioEconomicaForm clasificacionForm,
																		ActionMapping mapping, 
																		HttpServletRequest request, 
																		Connection con) throws SQLException
	{
		ClasificacionSocioEconomica mundoClasificacion=new ClasificacionSocioEconomica();
		boolean validarCargarUltimoCodigo=mundoClasificacion.cargarUltimoCodigo(con);
		if(validarCargarUltimoCodigo)
		{ 
			boolean validarCargar=mundoClasificacion.cargarResumen(con,mundoClasificacion.getCodigo());
			if(validarCargar)
			{
				llenarForm(clasificacionForm,mundoClasificacion);
				this.cerrarConexion(con);		
				return mapping.findForward("paginaResumenClasificacionSocioEconomica");
			}
			else
			{
				logger.warn("Código clasificación inválido "+clasificacionForm.getCodigo());
				this.cerrarConexion(con);
				clasificacionForm.reset();
				ArrayList atributosError = new ArrayList();
				atributosError.add(" Código estrato ");
				request.setAttribute("codigoDescripcionError", "errors.invalid");				
				request.setAttribute("atributosError", atributosError);
				return mapping.findForward("paginaError");		
			}
		}
		else
		{
				logger.warn("(Cargar última inserción)Código estrato inválido "+clasificacionForm.getCodigo());
				this.cerrarConexion(con);
				clasificacionForm.reset();
				ArrayList atributosError = new ArrayList();
				atributosError.add(" Código estrato");
				request.setAttribute("codigoDescripcionError", "errors.invalid");				
				request.setAttribute("atributosError", atributosError);
				return mapping.findForward("paginaError");		
		}
	}

	/**
	 * Este método especifica las acciones a realizar en el estado
	 * listar clasificacion
	 * @param ClasificacionSocioEconomicaForm clasificacionForm
	 * @param mapping Mapping para manejar la navegación
	 * @param con Conexión con la fuente de datos
	 * @param , int, institucion, Codigo de la institucion a la que pertenece el usuario para filtrar la consulta
	 * @return ActionForward  a la página "listadoTercero.jsp"
	 * @throws SQLException
	 */
	private ActionForward accionListar	(	ClasificacionSocioEconomicaForm clasificacionForm,
																	ActionMapping mapping,
																	Connection con,String estado, int institucion) throws SQLException 
	{
		ClasificacionSocioEconomica mundoClasificacion=new ClasificacionSocioEconomica();
		clasificacionForm.setEstado(estado);
		clasificacionForm.setCol(mundoClasificacion.listadoClasificacion(con,institucion));
		this.cerrarConexion(con);
		return mapping.findForward("paginaListar")	;	
	}	

	/**
	 * Este método especifica las acciones a realizar en el estado
	 * busquedaAvanzada
	 * 
	 * @param ClasificacionSocioEconomicaForm clasificacionForm
	 * 				para pre-llenar datos si es necesario
	 * @param request HttpServletRequest para obtener los 
	 * 				datos
	 * @param mapping Mapping para manejar la navegación
	 * @param con Conexión con la fuente de datos
	 * @return ActionForward a la página funcionalidadBuscarTercero "busquedaClasificacionSocioEconomica.jsp"
	 * @throws SQLException
	 */
	private ActionForward accionBusquedaAvanzada(	ClasificacionSocioEconomicaForm clasificacionForm,
																						ActionMapping mapping, 
																						Connection con) throws SQLException
	{
		//Limpiamos lo que venga del form
		clasificacionForm.reset();
		clasificacionForm.setEstado("busquedaAvanzada");
		this.cerrarConexion(con);
		return mapping.findForward("paginaBusqueda");
	}
	
	/**
	 * Este método especifica las acciones a realizar en el estado
	 * resultadoBusquedaAvanzada.
	 * 
	 * @param ClasificacionSocioEconomicaForm clasificacionForm
	 * 				para pre-llenar datos si es necesario
	 * @param request HttpServletRequest para obtener los 
	 * 				datos
	 * @param mapping Mapping para manejar la navegación
	 * @param con Conexión con la fuente de datos
	 * @param  int, institucion, Codigo de la institucion a la que pertenece el usuario para filtrar la consulta
	 * @return ActionForward a la página funcionalidadBuscarClasificacion "busquedaClasificacionSocioEconomica.jsp"
	 * @throws SQLException
	 */
	private ActionForward accionResultadoBusquedaAvanzada(ClasificacionSocioEconomicaForm clasificacionForm,
																										ActionMapping mapping, 
																										Connection con, int institucion) throws SQLException
	{
		ClasificacionSocioEconomica mundoClasificacion=new ClasificacionSocioEconomica();
		mundoClasificacion.reset();
		enviarItemsSeleccionadosBusqueda(clasificacionForm, mundoClasificacion);
		clasificacionForm.resetCriteriosBusqueda();
		clasificacionForm.setCol(mundoClasificacion.resultadoBusquedaAvanzada(con, institucion));
		this.cerrarConexion(con);
		return mapping.findForward("paginaListar");
	}
	
	private void enviarItemsSeleccionadosBusqueda(	ClasificacionSocioEconomicaForm clasificacionForm, 
																						ClasificacionSocioEconomica mundoClasificacion)
	{
		String bb[]= clasificacionForm.getCriteriosBusqueda();
		for(int i=0; i<bb.length; i++)
		{
			try
			{	
				if(bb[i].equals("codigo"))
					mundoClasificacion.setCodigo(clasificacionForm.getCodigo());
				
				if(bb[i].equals("descripcion"))
					mundoClasificacion.setDescripcion(clasificacionForm.getDescripcion());
				if(bb[i].equals("nombreTipoRegimen"))
				{
					InfoDatos infodatos = new InfoDatos(clasificacionForm.getAcronimoTipoRegimen(), clasificacionForm.getNombreTipoRegimen());
					mundoClasificacion.setTipoRegimen(infodatos);
				}
				if(bb[i].equals("activaAux"))
					mundoClasificacion.setActivaAux(clasificacionForm.getActivaAux());		
			}
			catch (Exception e)
			{
				logger.warn("Error en enviarItemsSeleccionados "+e);
			}
		}		
	}

	/**
	 * Este método especifica las acciones a realizar en el estado
	 * modificar clasificación socioeconómica
	 * @param ClasificacionSocioEconomicaForm clasificacionForm
	 * @param mapping Mapping para manejar la navegación
	 * @param con Conexión con la fuente de datos
	 * @return ActionForward  a la página "clasificacionSocioEconomica.jsp"
	 * @throws SQLException
	 */
	private ActionForward accionModificar(ClasificacionSocioEconomicaForm clasificacionForm, 
																	HttpServletRequest request, 
																	ActionMapping mapping, 
																	Connection con) throws SQLException
	{
		ClasificacionSocioEconomica mundoClasificacion=new ClasificacionSocioEconomica();
		mundoClasificacion.reset();
		mundoClasificacion.cargarResumen(con,clasificacionForm.getCodigo());
		
		llenarForm(clasificacionForm,mundoClasificacion);
		clasificacionForm.setEstado("modificar");
		
		if(clasificacionForm.getCodigo()<0)
		{
			logger.warn("No se pudo cargar el Estrato: "+clasificacionForm.getCodigo());
			this.cerrarConexion(con);
			clasificacionForm.reset();
			ArrayList atributosError = new ArrayList();
			atributosError.add("El código del Estrato inválido ");
			request.setAttribute("codigoDescripcionError", "errors.invalid");				
			request.setAttribute("atributosError", atributosError);
			return mapping.findForward("paginaError");	
		}
		else
		{
			String log="\n            ====INFORMACION ORIGINAL===== " +
					"\n*  Código Clasificación SocioEconómica [" +mundoClasificacion.getCodigo() +"] "+
					"\n* 	Descripción ["+mundoClasificacion.getDescripcion()+"] "+					
					"\n* 	Tipo Régimen ["+mundoClasificacion.getNombreTipoRegimen()+"] ";
				
					if(mundoClasificacion.getActiva())
						log += "\n*  Activa [ SI ]";
					else
						log += "\n*  Activa [ NO ]";
				
				clasificacionForm.setLogInfoOriginal(log);		
				
			this.cerrarConexion(con);
			return mapping.findForward("principal");
		}
	}	

	/**
	 * Este método especifica las acciones a realizar en el estado
	 * guardarModificacion
	 * @param  ClasificacionSocioEconomicaForm clasificacionForm
	 * @param request HttpServletRequest para obtener 
	 *					datos de la session 
	 * @param mapping Mapping para manejar la navegación
	 * @param con Conexión con la fuente de datos
	 * @return ActionForward "convenio.do?estado=modificar"
	 * @throws SQLException
	 */
	private ActionForward accionGuardarModificacion( ClasificacionSocioEconomicaForm clasificacionForm,
																						HttpServletRequest request,			
																						ActionMapping mapping, 
																						Connection con)	
																						throws SQLException
	{
		
		ClasificacionSocioEconomica  mundoClasificacion=new ClasificacionSocioEconomica(); 
		mundoClasificacion.cargarResumen(con,clasificacionForm.getCodigo());
				
		if(!clasificacionForm.getDescripcion().equals(mundoClasificacion.getDescripcion()) ||  clasificacionForm.getAcronimoTipoRegimen()!=mundoClasificacion.getAcronimoTipoRegimen() ||  clasificacionForm.getActiva()!=mundoClasificacion.getActiva())
		{
			llenarMundo(clasificacionForm, mundoClasificacion);
			UsuarioBasico usuario = (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
			
			mundoClasificacion.modificarClasificacion(con);
			String log=clasificacionForm.getLogInfoOriginal()+
					"\n          =====INFORMACION DESPUES DE LA MODIFICACION===== " +
					"\n*  Código Clasificación SocioEconómica [" +clasificacionForm.getCodigo() +"] "+
					"\n*  	Descripción ["+mundoClasificacion.getDescripcion()+"] "+	
					"\n* 	Tipo Régimen ["+mundoClasificacion.getNombreTipoRegimen()+"] " ;
				
			if(mundoClasificacion.getActiva())
				log += "\n*  Activa [ SI ]";
			else
				log += "\n*  Activa [ NO ]";
			
			log+="\n========================================================\n\n\n " ;
			LogsAxioma.enviarLog(ConstantesBD.logClasificacionSocioEconomicaCodigo, log, ConstantesBD.tipoRegistroLogModificacion, usuario.getInformacionGeneralPersonalSalud());
		}
		this.cerrarConexion(con);									
		return mapping.findForward("funcionalidadResumenModificarClasificacionSocioEconomica");
	}


	/**
	 * Este método especifica las acciones a realizar en el estado
	 * resumenModificar
	 * @param ClasificacionSocioEconomicaForm clasificacionForm
	 * @param mapping Mapping para manejar la navegación
	 * @param con Conexión con la fuente de datos
	 * @return ActionForward a la página de resumen "resumenClasificacionSocioEconomica.jsp"
	 * @throws SQLException
	 */
	private ActionForward accionResumenModificar(		ClasificacionSocioEconomicaForm clasificacionForm, 
																							ActionMapping mapping, 
																							HttpServletRequest request, 
																							Connection con) throws SQLException
	{
		ClasificacionSocioEconomica  mundoClasificacion=new ClasificacionSocioEconomica();  	
		boolean validarCargar=mundoClasificacion.cargarResumen(con,clasificacionForm.getCodigo());
		if(validarCargar)
		{
			llenarForm(clasificacionForm,mundoClasificacion);
			this.cerrarConexion(con);		
			return mapping.findForward("paginaResumenClasificacionSocioEconomica");
		}
		else
		{
			logger.warn("Código estrato inválido "+clasificacionForm.getCodigo());
			this.cerrarConexion(con);
			clasificacionForm.reset();
			ArrayList atributosError = new ArrayList();
			atributosError.add(" Código estrato");
			request.setAttribute("codigoDescripcionError", "errors.invalid");				
			request.setAttribute("atributosError", atributosError);
			return mapping.findForward("paginaError");		
		}
	}


	/**
	 * Este método carga los datos pertinentes a la forma 
	 * @param clasificacionForm (form)
	 * @param mundoClasificacion (mundo)
	 */
	protected void llenarForm(ClasificacionSocioEconomicaForm clasificacionForm, ClasificacionSocioEconomica mundoClasificacion)
	{		
		clasificacionForm.setCodigo(mundoClasificacion.getCodigo());
		clasificacionForm.setDescripcion(mundoClasificacion.getDescripcion());
		clasificacionForm.setAcronimoTipoRegimen(mundoClasificacion.getAcronimoTipoRegimen());
		clasificacionForm.setNombreTipoRegimen(mundoClasificacion.getNombreTipoRegimen());
		clasificacionForm.setActiva(mundoClasificacion.getActiva());
	}

	/**
	 * Método que carga los datos pertinentes desde el 
	 * form ClasificacionSocioEconomicaForm para el mundo de ClasificacionSocioEconomica 
	 * @param ClasificacionSocioEconomicaForm clasificacionForm (forma)
	 * @param mundoClasificacion ClasificacionSocioEconomica (mundo)
	 */
	protected void llenarMundo(	ClasificacionSocioEconomicaForm clasificacionForm, 
														ClasificacionSocioEconomica mundoClasificacion)
	{
		mundoClasificacion.setCodigo(clasificacionForm.getCodigo());
		mundoClasificacion.setDescripcion(clasificacionForm.getDescripcion());
		mundoClasificacion.setTipoRegimen(new InfoDatos(clasificacionForm.getAcronimoTipoRegimen(), clasificacionForm.getNombreTipoRegimen()));
		mundoClasificacion.setActiva(clasificacionForm.getActiva());
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
