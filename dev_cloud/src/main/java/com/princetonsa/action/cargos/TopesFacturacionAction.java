/*
 * @(#)TopesFacturacionAction.java
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
import util.InfoDatos;
import util.InfoDatosInt;
import util.InfoDatosString;
import util.LogsAxioma;
import util.UtilidadBD;
import util.UtilidadFecha;

import com.princetonsa.actionform.cargos.TopesFacturacionForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.cargos.TopeFacturacionException;
import com.princetonsa.mundo.cargos.TopesFacturacion;

/**
 *   Action, controla todas las opciones dentro del registro de
 *  las topes de facturacion, incluyendo los posibles casos de error. 
 *   Y los casos de flujo.
 * @version 1.0, Julio 13, 2004
 * @author <a href="mailto:wilson@PrincetonSA.com">Wilson Ríos</a>
 */
public class TopesFacturacionAction  extends Action
{
	/**
	 * Objeto para manejar los logs de esta clase
	*/
	private Logger logger = Logger.getLogger(TopesFacturacionAction.class);
		
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
		if(form instanceof TopesFacturacionForm)
		{
				
				try
				{
						con = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection();	
				}
				catch(SQLException e)
				{
						logger.warn("No se pudo abrir la conexión"+e.toString());
				}
				
				//UsuarioBasico usuario= (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
				TopesFacturacionForm topesForm =(TopesFacturacionForm)form;
				
				String estado=topesForm.getEstado(); 
						
				logger.info("\n\nESTADO TOPES FACTURACION******"+estado);
				
				if(estado == null)
				{
						topesForm.reset();	
						logger.warn("Estado no valido dentro del flujo de topes facturacion (null) ");
						request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
						this.cerrarConexion(con);
						return mapping.findForward("paginaError");
				}
				else if (estado.equals("empezar"))
				{
						return this.accionEmpezar(topesForm,mapping, con);
				}
				else if(estado.equals("salir"))
				{
						return this.accionSalir(topesForm,mapping,request,con);
				}
				else if(estado.equals("eliminar"))
				{
						return this.accionEliminar(topesForm,request,mapping,con);
				}
				else if(estado.equals("resumen"))
				{
						return this.accionResumen(topesForm,mapping,request, con);
				}
				else if(estado.equals("listar")||estado.equals("listarModificar"))
				{
					return this.accionListar(topesForm,mapping,con,estado);
				}
				else if(estado.equals("imprimir"))
				{
						this.cerrarConexion(con);
						return mapping.findForward("imprimir");	
				}				
				else if(estado.equals("resumenModificar"))
				{
						return this.accionResumenModificar(topesForm,mapping,request, con);
				}
				else if(estado.equals("modificar"))
				{
						return this.accionModificar(topesForm,request,mapping,con);
				}
				else if(estado.equals("guardarModificacion"))
				{
						return this.accionGuardarModificacion(topesForm,request,mapping,con);
				}
				else if (estado.equals("consultarPosteriores")||estado.equals("modificarPosteriores"))
				{
						return this.accionListarPosteriores(topesForm,mapping,con,estado);
				}
				else
				{
					topesForm.reset();
					logger.warn("Estado no valido dentro del flujo de TopesFacturacion (null) ");
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
	 * @param topesForm TopesFacturacionForm
	 * 				para pre-llenar datos si es necesario
	 * @param request HttpServletRequest para obtener los 
	 * 				datos
	 * @param mapping Mapping para manejar la navegación
	 * @param con Conexión con la fuente de datos
	 * @return ActionForward a la página principal "topesFacturacion.jsp"
	 * 
	 */
	private ActionForward accionEmpezar(TopesFacturacionForm topesForm, 
																	 ActionMapping mapping, 
																	 Connection con) throws SQLException 
	{
		//Limpiamos lo que venga del form
		topesForm.reset();
		topesForm.setEstado("empezar");
		this.cerrarConexion(con);
		return mapping.findForward("principal");		
	}

	/**
	 * Este método especifica las acciones a realizar en el estado
	 * salir.
	 * Se copian las propiedades del objeto topesFacturacion
	 * en el objeto mundo
	 * 
	 * @param topesForm TopesFacturacionForm
	 * @param request HttpServletRequest para obtener 
	 * 					datos de la session 
	 * @param mapping Mapping para manejar la navegación
	 * @param con Conexión con la fuente de datos
	 * 
	 * @return ActionForward "topes.do?estado=resumen"
	 * @throws Exception 
	 * @throws SQLException
	*/
	private ActionForward accionSalir(TopesFacturacionForm topesForm,
															 ActionMapping mapping,
															 HttpServletRequest request, 
		 													 Connection con) throws Exception 
	{
		TopesFacturacion mundoTopes=new TopesFacturacion();  
		llenarMundo(topesForm, mundoTopes, request);
		
		try
		{
			mundoTopes.insertarTope(con);
			cerrarConexion(con);
		}
		catch (TopeFacturacionException e) {
		    ActionErrors errores = new ActionErrors();
			errores.add("Error en la insercion del tope facturacion", new ActionMessage("error.facturacion.topeFacturacionRepetido"));
			logger.warn("Error en la insercion del tope facturacion repetido");
			saveErrors(request, errores);	
			topesForm.setEstado("empezar");
			return mapping.findForward("principal");
		} catch (Exception e) {
			throw e;
		}
		
		return mapping.findForward("funcionalidadResumenTopesFacturacion");
	}

	/**
	 * Este método especifica las acciones a realizar en el estado
	 * resumen
	 * @param TopesFacturacionForm topesForm
	 * @param mapping Mapping para manejar la navegación
	 * @param con Conexión con la fuente de datos
	 * @return ActionForward a la página de resumen "resumenTopesFacturacion.jsp"
	 * @throws SQLException
	 */
	private ActionForward accionResumen(	TopesFacturacionForm topesForm,
																		ActionMapping mapping, 
																		HttpServletRequest request, 
																		Connection con) throws SQLException
	{
		TopesFacturacion mundoTopes=new TopesFacturacion();
		boolean validarCargarUltimoCodigo=mundoTopes.cargarUltimoCodigo(con);
		if(validarCargarUltimoCodigo)
		{ 
			boolean validarCargar=mundoTopes.cargarResumen(con,mundoTopes.getCodigo());
			if(validarCargar)
			{
				llenarForm(topesForm,mundoTopes);
				this.cerrarConexion(con);		
				return mapping.findForward("paginaResumenTopesFacturacion");
			}
			else
			{
				logger.warn("Código tope inválido "+topesForm.getCodigo());
				this.cerrarConexion(con);
				topesForm.reset();
				ArrayList atributosError = new ArrayList();
				atributosError.add(" Código tope ");
				request.setAttribute("codigoDescripcionError", "errors.invalid");				
				request.setAttribute("atributosError", atributosError);
				return mapping.findForward("paginaError");		
			}
		}
		else
		{
				logger.warn("(Cargar última inserción)Código tope inválido "+topesForm.getCodigo());
				this.cerrarConexion(con);
				topesForm.reset();
				ArrayList atributosError = new ArrayList();
				atributosError.add(" Código tope");
				request.setAttribute("codigoDescripcionError", "errors.invalid");				
				request.setAttribute("atributosError", atributosError);
				return mapping.findForward("paginaError");		
		}
	}

	/**
	 * Este método especifica las acciones a realizar en el estado
	 * listar topes
	 * @param TopesFacturacionForm topesForm
	 * @param mapping Mapping para manejar la navegación
	 * @param con Conexión con la fuente de datos
	 * @return ActionForward  a la página "listadoTopesFacturacion.jsp"
	 * @throws SQLException
	 */
	private ActionForward accionListar	(	TopesFacturacionForm topesForm,
																	ActionMapping mapping,
																	Connection con,String estado) throws SQLException 
	{
		TopesFacturacion mundoTopes=new TopesFacturacion();
		topesForm.setEstado(estado);
		topesForm.setCol(mundoTopes.listadoTopes(con));
		this.cerrarConexion(con);
		return mapping.findForward("paginaListar");	
	}	
	
	/**
	 * Este método especifica las acciones a realizar en el estado
	 * modificar topes
	 * @param TopesFacturacionForm topesForm
	 * @param mapping Mapping para manejar la navegación
	 * @param con Conexión con la fuente de datos
	 * @return ActionForward  a la página "topesFacturacion.jsp"
	 * @throws SQLException
	 */
	private ActionForward accionModificar(TopesFacturacionForm topesForm, 
																	HttpServletRequest request, 
																	ActionMapping mapping, 
																	Connection con) throws SQLException
	{
		TopesFacturacion mundoTopes=new TopesFacturacion();
		mundoTopes.reset();
		mundoTopes.cargarResumen(con,topesForm.getCodigo());
		
		llenarForm(topesForm,mundoTopes);
		topesForm.setEstado("modificar");
		
		if(topesForm.getCodigo()<0)
		{
			logger.warn("No se pudo cargar el Tope: "+topesForm.getCodigo());
			this.cerrarConexion(con);
			topesForm.reset();
			ArrayList atributosError = new ArrayList();
			atributosError.add("El código del Tope inválido ");
			request.setAttribute("codigoDescripcionError", "errors.invalid");				
			request.setAttribute("atributosError", atributosError);
			return mapping.findForward("paginaError");	
		}
		else
		{
			String log="\n            ====INFORMACION ORIGINAL===== " +
					"\n*  Código Tope [" +mundoTopes.getCodigo() +"] "+
					"\n* 	Tipo Régimen ["+mundoTopes.getNombreTipoRegimen()+"] "+
					"\n*  Clasificación socioeconómica ["+mundoTopes.getDescripcionEstrato()+"] " +
					"\n*  Tipo de Monto ["+mundoTopes.getNombreTipoMonto()+"]" +
					"\n*  Tope por evento ["+mundoTopes.getTopeEvento()+"] " +
					"\n*  Topes por año calendario ["+mundoTopes.getTopeAnioCalendario()+"]"+
					"\n*  Fecha de Vigencia Inicial ["+mundoTopes.getFechaVigenciaInicial()+"]" ;
				
			topesForm.setLogInfoOriginal(log);		
				
			this.cerrarConexion(con);
			return mapping.findForward("principal");
		}
	}	

	/**
	 * Este método especifica las acciones a realizar en el estado
	 * guardarModificacion
	 * @param  TopesFacturacion topesForm
	 * @param request HttpServletRequest para obtener 
	 *					datos de la session 
	 * @param mapping Mapping para manejar la navegación
	 * @param con Conexión con la fuente de datos
	 * @return ActionForward "topes.do?estado=modificar"
	 * @throws SQLException
	 */
	private ActionForward accionGuardarModificacion( TopesFacturacionForm topesForm,
																						HttpServletRequest request,			
																						ActionMapping mapping, 
																						Connection con)	
																						throws SQLException
	{
	    boolean existioModificacion= existeModificacion(topesForm, con);
		if(existioModificacion)
		{
			TopesFacturacion  mundoTopes=new TopesFacturacion(); 
			llenarMundo(topesForm, mundoTopes,request);
			UsuarioBasico usuario = (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
			mundoTopes.modificarTopes(con);
			String log=topesForm.getLogInfoOriginal()+
					"\n          =====INFORMACION DESPUES DE LA MODIFICACION===== " +
					"\n*  Código Tope [" +mundoTopes.getCodigo() +"] "+
					"\n* 	Tipo Régimen ["+mundoTopes.getNombreTipoRegimen()+"] "+
					"\n*  Clasificación socioeconómica ["+mundoTopes.getDescripcionEstrato()+"] " +
					"\n*  Tipo de Monto ["+mundoTopes.getNombreTipoMonto()+"]" +
					"\n*  Tope por evento ["+mundoTopes.getTopeEvento()+"] " +
					"\n*  Topes por año calendario ["+mundoTopes.getTopeAnioCalendario()+"]" +
					"\n*   Institución ["+usuario.getInstitucion()+"]" +
					"\n*   Fecha de Vigencia Inicial ["+mundoTopes.getFechaVigenciaInicial()+"]";
			
			log+="\n========================================================\n\n\n " ;		
		
			LogsAxioma.enviarLog(ConstantesBD.logTopesFacturacionCodigo, log, ConstantesBD.tipoRegistroLogModificacion, usuario.getInformacionGeneralPersonalSalud());
	
			this.cerrarConexion(con);									
			return mapping.findForward("funcionalidadResumenModificarTopesFacturacion");
		}
		else
		{
		    topesForm.setEstado("modificar");
		    this.cerrarConexion(con);
			return mapping.findForward("principal");
		}
	}

	/**
	 * Metodo que evalua si existio o no modificacion en el convenio
	 * @param topesFacturacionForm
	 * @param con
	 * @return
	 * @throws SQLException
	 */
	private boolean existeModificacion (TopesFacturacionForm topesFacturacionForm, Connection con)throws SQLException
	{
	    TopesFacturacion mundoTopesFacturacion=new TopesFacturacion();
		mundoTopesFacturacion.reset();
		mundoTopesFacturacion.cargarResumen(con,topesFacturacionForm.getCodigo());
		String tempComparacionTopeEvento="", tempComparacionTopeAnio="";
		if(topesFacturacionForm.getTopeEvento().equals(""))
		    tempComparacionTopeEvento="0.0";
		else
		    tempComparacionTopeEvento=topesFacturacionForm.getTopeEvento();
		if(topesFacturacionForm.getTopeAnioCalendario().equals(""))
		    tempComparacionTopeAnio="0.0";
		else
		    tempComparacionTopeAnio=topesFacturacionForm.getTopeAnioCalendario();
	    if(mundoTopesFacturacion.getAcronimoTipoRegimen().equals( topesFacturacionForm.getAcronimoTipoRegimen()))
		{
		    if((mundoTopesFacturacion.getDescripcionEstrato()).equals(topesFacturacionForm.getDescripcionEstrato()))
		    {
		        if(mundoTopesFacturacion.getCodigoTipoMonto()==topesFacturacionForm.getCodigoTipoMonto())
		        {
		            if((mundoTopesFacturacion.getTopeEvento()+"").equals(tempComparacionTopeEvento) )
		            {
		                if((mundoTopesFacturacion.getTopeAnioCalendario()+"").equals(tempComparacionTopeAnio))
		                {
		                    return false;
		                }
		            }
		        }
		    }
		}
	    return true;
	}
	
	/**
	 * Accion eliminar
	 * @param topesForm
	 * @param request
	 * @param mapping
	 * @param con
	 * @return
	 * @throws SQLException
	 */
	private ActionForward accionEliminar(		TopesFacturacionForm topesForm,
																HttpServletRequest request,															
																ActionMapping mapping, 
																Connection con)	
																throws SQLException
	{
			TopesFacturacion mundo=new TopesFacturacion ();  
			mundo.cargarResumen(con,topesForm.getCodigo());
			mundo.eliminar(con);
			String log="\n            ====INFORMACION ORIGINAL===== " +
			"\n*  Código Tope [" +mundo.getCodigo() +"] "+
			"\n* 	Tipo Régimen ["+mundo.getNombreTipoRegimen()+"] "+
			"\n*  Clasificación socioeconómica ["+mundo.getDescripcionEstrato()+"] " +
			"\n*  Tipo de Monto ["+mundo.getNombreTipoMonto()+"]" +
			"\n*  Tope por evento ["+mundo.getTopeEvento()+"] " +
			"\n*  Topes por año calendario ["+mundo.getTopeAnioCalendario()+"] " +
			"\n* 	====>>>> FUE ELIMINADO\n\n\n";
			UsuarioBasico usuario = (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
			LogsAxioma.enviarLog(ConstantesBD.logTopesFacturacionCodigo, log, ConstantesBD.tipoRegistroLogEliminacion, usuario.getInformacionGeneralPersonalSalud());
			
			this.cerrarConexion(con);
			return mapping.findForward("paginaMensajeBorrado");
   }

	
	/**
	 * Este método especifica las acciones a realizar en el estado
	 * resumenModificar
	 * @param TopesFacturacionForm topesForm
	 * @param mapping Mapping para manejar la navegación
	 * @param con Conexión con la fuente de datos
	 * @return ActionForward a la página de resumen "resumenTopesFacturacion.jsp"
	 * @throws SQLException
	 */
	private ActionForward accionResumenModificar(		TopesFacturacionForm topesForm, 
																							ActionMapping mapping, 
																							HttpServletRequest request, 
																							Connection con) throws SQLException
	{
		TopesFacturacion  mundoTopes=new TopesFacturacion();  	
		boolean validarCargar=mundoTopes.cargarResumen(con,topesForm.getCodigo());
		if(validarCargar)
		{
			llenarForm(topesForm,mundoTopes);
			this.cerrarConexion(con);		
			return mapping.findForward("paginaResumenTopesFacturacion");
		}
		else
		{
			logger.warn("Código topes inválido "+topesForm.getCodigo());
			this.cerrarConexion(con);
			topesForm.reset();
			ArrayList atributosError = new ArrayList();
			atributosError.add(" Código tope");
			request.setAttribute("codigoDescripcionError", "errors.invalid");				
			request.setAttribute("atributosError", atributosError);
			return mapping.findForward("paginaError");		
		}
	}

	/**
	 * Este método carga los datos pertinentes a la forma 
	 * @param topesForm (form)
	 * @param mundoTopes (mundo)
	 */
	protected void llenarForm(TopesFacturacionForm topesForm, TopesFacturacion mundoTopes)
	{		
		topesForm.setCodigo(mundoTopes.getCodigo());
		
		topesForm.setAcronimoTipoRegimen(mundoTopes.getAcronimoTipoRegimen());
		topesForm.setNombreTipoRegimen(mundoTopes.getNombreTipoRegimen());
		
		topesForm.setCodigoEstrato(mundoTopes.getCodigoEstrato());
		topesForm.setDescripcionEstrato(mundoTopes.getDescripcionEstrato());
		
		topesForm.setCodigoTipoMonto(mundoTopes.getCodigoTipoMonto());
		topesForm.setNombreTipoMonto(mundoTopes.getNombreTipoMonto());
		
		topesForm.setFechaVigenciaInicial(UtilidadFecha.conversionFormatoFechaAAp(mundoTopes.getFechaVigenciaInicial().getId()));
		
		if(mundoTopes.getTopeEvento()==0)
		    topesForm.setTopeEvento("");
		else
			topesForm.setTopeEvento(mundoTopes.getTopeEventoString());
		
		if(mundoTopes.getTopeAnioCalendario()==0)
		    topesForm.setTopeAnioCalendario("");
		else
			topesForm.setTopeAnioCalendario(mundoTopes.getTopeAnioCalendarioString());
		
	}

	/**
	 * Método que carga los datos pertinentes desde el 
	 * form TopesFacturacionForm para el mundo de TopesFacturacion 
	 * @param TopesFacturacionForm topesFacturacionForm (forma)
	 * @param mundoTopes TopesFacturacion (mundo)
	 */
	protected void llenarMundo(	TopesFacturacionForm topesForm, 
												TopesFacturacion mundoTopes, HttpServletRequest request )
	{
		UsuarioBasico usuario = (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
		mundoTopes.setCodigo(topesForm.getCodigo());
		mundoTopes.setTipoRegimen(new InfoDatos(topesForm.getAcronimoTipoRegimen(), topesForm.getNombreTipoRegimen()));
		mundoTopes.setEstrato(new InfoDatosInt (topesForm.getCodigoEstrato(), topesForm.getDescripcionEstrato()));
		mundoTopes.setTipoMonto(new InfoDatosInt(topesForm.getCodigoTipoMonto(), topesForm.getNombreTipoMonto()));
		
		/**
		 * Si los topes son cero eso quiere decir que no existe tope 
		 */
		if(topesForm.getTopeEvento().equals(""))
		    mundoTopes.setTopeEvento(0);
		else
		    mundoTopes.setTopeEvento(Double.parseDouble(topesForm.getTopeEvento()));
		if(topesForm.getTopeAnioCalendario().equals(""))
		   mundoTopes.setTopeAnioCalendario(0);
		else
		    mundoTopes.setTopeAnioCalendario(Double.parseDouble(topesForm.getTopeAnioCalendario()));
		mundoTopes.setInstitucion(new InfoDatosInt(Integer.parseInt(usuario.getCodigoInstitucion()), usuario.getInstitucion()));
		mundoTopes.setFechaVigenciaInicial(new InfoDatosString(topesForm.getFechaVigenciaInicial()));
		
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

	/**
	 * Este método especifica las acciones a realizar en el estado
	 * listar topes posteriores a la fecha d evigencia inicial
	 * @param TopesFacturacionForm topesForm
	 * @param mapping Mapping para manejar la navegación
	 * @param con Conexión con la fuente de datos
	 * @return ActionForward  a la página "listadoTopesFacturacion.jsp"
	 * @throws SQLException
	 */
	private ActionForward accionListarPosteriores	(	TopesFacturacionForm topesForm,
																	ActionMapping mapping,
																	Connection con,String estado) throws SQLException 
	{
		TopesFacturacion mundoTopes=new TopesFacturacion();
		topesForm.setEstado(estado);
		topesForm.setCol(mundoTopes.listadoTopesPosteriores(con));
		this.cerrarConexion(con);
		return mapping.findForward("paginaListar");	
	}	
	
	
}
