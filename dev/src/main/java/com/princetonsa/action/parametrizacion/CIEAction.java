/*
 * @(#)CIEAction.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */
package com.princetonsa.action.parametrizacion;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.LogsAxioma;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.ValoresPorDefecto;

import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.parametrizacion.CIEForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.parametrizacion.CIE;

/**
 * Action, controla todas las opciones dentro del registro de
 * vigencia diagnósticos, incluyendo los posibles casos de error. 
 * Y los casos de flujo.
 * @version 1.0, Agosto 17, 2004
 * @author <a href="mailto:wilson@PrincetonSA.com">Wilson Ríos</a>
 */
public class CIEAction extends Action 
{
	/**
	 * Objeto para manejar los logs de esta clase
	*/
	private Logger logger = Logger.getLogger(CIEAction.class);
	
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
		try {
			if (response==null); //Para evitar que salga el warning
			if(form instanceof CIEForm)
			{
				try
				{	
					con = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection();	
				}
				catch(SQLException e)
				{
					logger.warn("No se pudo abrir la conexión"+e.toString());
					return ComunAction.accionSalirCasoError(mapping, request, con, logger, "La accion específicada no esta definida", "errors.estadoInvalido", true);
				}

				CIEForm cieForm =(CIEForm)form;

				String estado=cieForm.getEstado(); 

				if(estado == null)
				{
					cieForm.reset();
					return ComunAction.accionSalirCasoError(mapping, request, con, logger, "La accion específicada no esta definida", "errors.estadoInvalido", true);
				}
				else if (estado.equals("empezar"))
				{
					return this.accionEmpezar(cieForm,mapping, con);
				}
				else if(estado.equals("salir"))
				{
					return this.accionSalir(cieForm,request,mapping,con);
				}
				else if(estado.equals("borrar"))
				{
					return this.accionBorrar(cieForm,request,mapping, con);
				}
				else if(estado.equals("modificar"))
				{
					return this.accionModificar(cieForm,request,mapping,con);
				}
				else if(estado.equals("guardarModificacion"))
				{
					return this.accionGuardarModificacion(cieForm,request,mapping,con);
				}
				else if (estado.equals("consultar"))
				{
					return this.accionConsultar(cieForm,mapping, con);
				}
				else
				{
					cieForm.reset();
					return ComunAction.accionSalirCasoError(mapping, request, con, logger, "La accion específicada no esta definida", "errors.estadoInvalido", true);
				}
			}
			return null;
		} catch (Exception e) {
			Log4JManager.error(e);
			return null;
		}
		finally{
			UtilidadBD.closeConnection(con);
		}
	}
	
	/**
	 * Este método especifica las acciones a realizar en el estado
	 * empezar.
	 * 
	 * @param cieForm CIEForm
	 * 				para pre-llenar datos si es necesario
	 * @param request HttpServletRequest para obtener los 
	 * 				datos
	 * @param mapping Mapping para manejar la navegación
	 * @param con Conexión con la fuente de datos
	 * @return ActionForward a la página principal "cie.jsp"
	 * @throws SQLException
	 */
	private ActionForward accionEmpezar(	CIEForm cieForm, 
																ActionMapping mapping, 
																Connection con) throws SQLException
	{
		//Limpiamos lo que venga del form
		cieForm.reset();
		cieForm.setEstado("empezar");
		CIE mundo= new CIE();
		cieForm.setCol(mundo.listadoCIE(con));
		UtilidadBD.cerrarConexion(con);
		return mapping.findForward("principal");		
	}
	
	/**
	 * Este método especifica las acciones a realizar en el estado
	 * salir.
	 * Se copian las propiedades del objeto CIE
	 * en el objeto mundo
	 * 
	 * @param cieForm CIEForm
	 * @param request HttpServletRequest para obtener 
	 * 					datos de la session 
	 * @param mapping Mapping para manejar la navegación
	 * @param con Conexión con la fuente de datos
	 * 
	 * @return ActionForward "cie.do?estado=salir"
	 * @throws SQLException
	*/
	private ActionForward accionSalir(		CIEForm cieForm,
															 HttpServletRequest request, 
															 ActionMapping mapping,
															 Connection con) throws SQLException
	{
		CIE mundo=new CIE ();  
		boolean existeTipoCIE =mundo.existeTipoCIE(	con,cieForm.getCodigoReal() );	
		boolean existeFechaCIE= mundo.existeFechaInicioVigenciaCIE(con,cieForm.getVigencia());
		
		if(!existeTipoCIE && !existeFechaCIE)
		{
			llenarMundo(cieForm, mundo);
			int validar=mundo.insertarCIE(con);
			ValoresPorDefecto.recargarCieActual(con);
			if(validar<=0)
			{
				logger.warn("No se pudo insertar la vigencia de diagnósticos: "+cieForm.getCodigoReal());
				UtilidadBD.cerrarConexion(con);
				cieForm.reset();
				ArrayList atributosError = new ArrayList();
				request.setAttribute("codigoDescripcionError", "errors.problemasBd");				
				request.setAttribute("atributosError", atributosError);
				cieForm.setEstado("empezar");
				return mapping.findForward("paginaError");	
			}
			else
			{
				UtilidadBD.cerrarConexion(con);	
				return mapping.findForward("insertarExitoso");
			}
		}
		else if(existeTipoCIE)
		{
			ActionErrors errores = new ActionErrors();
			errores.add("Tipo de CIE", new ActionMessage("error.cie.tipoCie"));
			logger.warn("entra al error de tipo cie existente");
			saveErrors(request, errores);	
			cieForm.setEstado("empezar");
			UtilidadBD.cerrarConexion(con);									
			return mapping.findForward("principal");
		}
		else if(existeFechaCIE)
		{
			ActionErrors errores = new ActionErrors();
			errores.add("Fecha Inicio Vigencia", new ActionMessage("error.cie.fechaRepetida"));
			logger.warn("entra al error de fecha vigencia existente");
			saveErrors(request, errores);	
			cieForm.setEstado("empezar");
			UtilidadBD.cerrarConexion(con);									
			return mapping.findForward("principal");
		}
		
		UtilidadBD.cerrarConexion(con);		
		return null;
	}

	/**
	 * Este método especifica las acciones a realizar en el estado
	 * consultar.
	 * 
	 * @param cieForm CIEForm
	 * 				para pre-llenar datos si es necesario
	 * @param request HttpServletRequest para obtener los 
	 * 				datos
	 * @param mapping Mapping para manejar la navegación
	 * @param con Conexión con la fuente de datos
	 * @return ActionForward a la página principal "listadoCie.jsp"
	 * @throws SQLException
	 */
	private ActionForward accionConsultar(	CIEForm cieForm, 
																ActionMapping mapping, 
																Connection con) throws SQLException
	{
		//Limpiamos lo que venga del form
		cieForm.reset();
		cieForm.setEstado("consultar");
		CIE mundo= new CIE();
		cieForm.setCol(mundo.listadoCIE(con));
		UtilidadBD.cerrarConexion(con);
		return mapping.findForward("consultar");		
	}
	
	/**
	 * Este método especifica las acciones a realizar en el estado
	 * modificar Vigencia diagnósticos
	 * @param cieForm CIEForm
	 * @param mapping Mapping para manejar la navegación
	 * @param con Conexión con la fuente de datos
	 * @return ActionForward  a la página "cie.jsp"
	 * @throws SQLException
	 */
	private ActionForward accionModificar(		CIEForm cieForm, 
																	HttpServletRequest request, 
																	ActionMapping mapping, 
																	Connection con) throws SQLException
	{
		CIE mundo=new CIE();
		mundo.reset();
		
		mundo.cargarResumen(con,cieForm.getCodigo());
		llenarForm(cieForm,mundo);
		cieForm.setEstado("modificar");
		
		cieForm.setVigenciaAntigua(UtilidadFecha.conversionFormatoFechaAAp(mundo.getVigencia()));
		
		if(cieForm.getCodigo()<=0)
		{
			logger.warn("No se pudo cargar el código del CIE: "+cieForm.getCodigo());
			UtilidadBD.cerrarConexion(con);
			cieForm.reset();
			ArrayList atributosError = new ArrayList();
			atributosError.add("El CIE ");
			request.setAttribute("codigoDescripcionError", "errors.invalid");				
			request.setAttribute("atributosError", atributosError);
			return mapping.findForward("paginaError");	
		}
		else
		{
			String log="\n            ====INFORMACION ORIGINAL===== " +
			"\n*  Código [" +mundo.getCodigo() +"] "+
			"\n*  Tipo de CIE ["+mundo.getCodigoReal()+"] " +
			"\n*  Fecha Inicio Vigencia ["+UtilidadFecha.conversionFormatoFechaAAp(mundo.getVigencia())+"] \n " ;
		
			cieForm.setLogInfoOriginal(log);
			UtilidadBD.cerrarConexion(con);
			return mapping.findForward("principal");
		}
	}
	
	/**
	 * Este método especifica las acciones a realizar en el estado
	 * guardarModificacion
	 * @param cieForm CIEForm
	 * @param request HttpServletRequest para obtener 
	 *					datos de la session 
	 * @param mapping Mapping para manejar la navegación
	 * @param con Conexión con la fuente de datos
	 * @return ActionForward "cie.do?estado=modificar"
	 * @throws SQLException
	 */
	private ActionForward accionGuardarModificacion(		CIEForm cieForm,
																					HttpServletRequest request, 
																					ActionMapping mapping, 
																					Connection con)	
																					throws SQLException
	{
		CIE mundo=new CIE ();
		UsuarioBasico usuario = (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
		
		llenarMundo(cieForm, mundo);
		
		boolean existeFechaCIE= mundo.existeFechaInicioVigenciaCIE(con,cieForm.getVigencia());
		
		if(!existeFechaCIE)
		{	
			int validar=mundo.modificarCIE(con);
			ValoresPorDefecto.recargarCieActual(con);
			if(validar<=0)
			{
				logger.warn("No se pudo modificar el código del CIE: "+cieForm.getCodigo());
				UtilidadBD.cerrarConexion(con);
				cieForm.reset();
				ArrayList atributosError = new ArrayList();
				atributosError.add("El CIE ");
				request.setAttribute("codigoDescripcionError", "errors.invalid");				
				request.setAttribute("atributosError", atributosError);
				return mapping.findForward("paginaError");	
			}
			else
			{
				String log=cieForm.getLogInfoOriginal()+"\n            ====INFORMACION MODIFICADA===== " +
				"\n*  Código [" +mundo.getCodigo() +"] "+
				"\n*  Tipo de CIE ["+mundo.getCodigoReal()+"] " +
				"\n*  Fecha Inicio Vigencia ["+mundo.getVigencia()+"] \n "+ 
				"========================================================\n\n\n " ;
						
				LogsAxioma.enviarLog(ConstantesBD.logCIECodigo, log, ConstantesBD.tipoRegistroLogModificacion, usuario.getLoginUsuario());
			
				cieForm.setEstado("empezar");
				UtilidadBD.cerrarConexion(con);									
				return mapping.findForward("insertarExitoso");
			}	
		}
		else if(existeFechaCIE)
		{
			ActionErrors errores = new ActionErrors();
			errores.add("Fecha Inicio Vigencia", new ActionMessage("error.cie.fechaRepetida"));
			logger.warn("entra al error de fecha vigencia existente");
			saveErrors(request, errores);	
			cieForm.setEstado("modificar");
			UtilidadBD.cerrarConexion(con);									
			return mapping.findForward("principal");
		}
		UtilidadBD.cerrarConexion(con);									
		return null;
	}

	/**
	 * Este método especifica las acciones a realizar en el estado
	 * borrar Vigencia diagnósticos
	 * @param cieForm CIEForm
	 * @param mapping Mapping para manejar la navegación
	 * @param con Conexión con la fuente de datos
	 * @return ActionForward  a la página "cie.jsp"
	 * @throws SQLException
	 */
	private ActionForward accionBorrar (			CIEForm cieForm, 
																	HttpServletRequest request, 
																	ActionMapping mapping, 
																	Connection con) throws SQLException
	{
		CIE mundo=new CIE ();
		UsuarioBasico usuario = (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
		
		mundo.cargarResumen(con,cieForm.getCodigo());
		
		int validar=mundo.borrarCIE(con);
		ValoresPorDefecto.recargarCieActual(con);
		if(validar<=0)
		{
			logger.warn("No se pudo borrar el código del CIE: "+cieForm.getCodigo());
			ActionErrors errores = new ActionErrors();
			errores.add("Borrar CIE", new ActionMessage("error.cie.borrarCie"));
			saveErrors(request, errores);	
			cieForm.setEstado("empezar");
			UtilidadBD.cerrarConexion(con);									
			return mapping.findForward("principal");			
		}
		else
		{	
			String log=cieForm.getLogInfoOriginal()+
			"\n*  Código [" +mundo.getCodigo() +"] "+
			"\n*  Tipo de CIE ["+mundo.getCodigoReal()+"] " +
			"\n*  Fecha Inicio Vigencia ["+UtilidadFecha.conversionFormatoFechaAAp(mundo.getVigencia())+"] \n "+ 
			"\n 	=================================>>>> FUE ELIMINADO\n\n\n";
					
			LogsAxioma.enviarLog(ConstantesBD.logCIECodigo, log, ConstantesBD.tipoRegistroLogEliminacion, usuario.getLoginUsuario());
			cieForm.setEstado("empezar");
			UtilidadBD.cerrarConexion(con);									
			return mapping.findForward("insertarExitoso");
		}
	}

	
	/**
	 * Este método carga los datos pertinentes a la forma 
	 * @param CIEForm (form)
	 * @param mundo (mundo)
	 */
	protected void llenarForm(CIEForm cieForm, CIE mundo)
	{	
		cieForm.setCodigo(mundo.getCodigo());
		cieForm.setCodigoReal(mundo.getCodigoReal());
		cieForm.setVigencia(UtilidadFecha.conversionFormatoFechaAAp(mundo.getVigencia()));
	}		

	/**
	 * Método que carga los datos pertinentes desde el 
	 * form CIEForm para el mundo de CIE
	 * @param cieForm CIEForm (forma)
	 * @param mundo CIE (mundo)
	 */
	protected void llenarMundo(CIEForm cieForm, CIE mundo)
	{
		mundo.setCodigo(cieForm.getCodigo());
		mundo.setCodigoReal(cieForm.getCodigoReal());
		mundo.setVigencia(cieForm.getVigencia());
	}
	
}
