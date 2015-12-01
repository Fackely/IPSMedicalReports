/*
 * Created on 31-ago-2004
 *
 * Jorge Armando Osorio Velasquez
 * Princeton
 */
package com.princetonsa.action.parametrizacion;

import java.sql.Connection;
import java.sql.SQLException;
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
import util.Listado;
import util.LogsAxioma;
import util.UtilidadBD;


import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.parametrizacion.SustitutosInventarioForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.parametrizacion.SustitutosInventarios;


/**
 * Clase encargada del control de la funcionalidad de
 * que maneja la consulta / modificación e inserción
 * de Sustitutos Inventarios
 * @author armando
 *
 * Princeton 31-ago-2004
 */
public class SustitutosInventarioAction extends Action 
{
    
	/**
	 * Objeto para almacenar los logs que aparezcan en el Action 
	 * de Axioma
	 */
    private Logger logger = Logger.getLogger(DiagnosticosAction.class);

    
    /**
     * Constante entera privada que define el
     * código con el que se identificará internamente
     * la acción de ingresar
     */
    private static final int accionIngresar = 1;

    /**
     * Constante entera privada que define el
     * código con el que se identificará internamente
     * la acción de modificar
     */
    private static final int accionModificar =2;

    /**
     * Constante entera privada que define el
     * código con el que se identificará internamente
     * la acción de consultar
     */
    private static final int accionConsultar =2;
    
	/**
	 * Método encargado de el flujo y control de la funcionalidad
	 * 
	 * @see org.apache.struts.action.Action#execute(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse)
	 */
    public ActionForward execute(ActionMapping mapping,
			 ActionForm form,
			 HttpServletRequest request,
			 HttpServletResponse response)
	throws Exception
	{
    	Connection con = null;

    	try {
    		if (form instanceof SustitutosInventarioForm)
    		{
    			SustitutosInventarioForm sustitutosForm=(SustitutosInventarioForm)form;
    			String estado=sustitutosForm.getEstado();
    			try
    			{

    				String tipoBD = System.getProperty("TIPOBD");
    				DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
    				con = myFactory.getConnection();
    				//Lo primero que vamos a hacer es validar que se
    				//cumplan las condiciones.

    				HttpSession session=request.getSession();
    				UsuarioBasico medico = (UsuarioBasico)session.getAttribute("usuarioBasico");

    				//Primera Condición: El usuario debe existir
    				//la validación de si es médico o no solo se hace en insertar
    				if( medico == null )
    				{
    					UtilidadBD.cerrarConexion(con);
    					return ComunAction.accionSalirCasoError(mapping, request, con, logger, "No existe el usuario", "errors.usuario.noCargado", true);
    				}

    				else if (estado==null||estado.equals(""))
    				{
    					sustitutosForm.reset();
    					UtilidadBD.cerrarConexion(con);
    					return ComunAction.accionSalirCasoError(mapping, request, con, logger, "La accion específicada no esta definida ", "errors.estadoInvalido", true);
    				}
    				else if (estado.equals("ingresar"))
    				{
    					return this.accionComunIngresarModificarConsultar(mapping, con, sustitutosForm, accionIngresar);
    				}
    				else if (estado.equals("modificar"))
    				{
    					sustitutosForm.setEnModificar(true);
    					return this.accionComunIngresarModificarConsultar(mapping, con, sustitutosForm, accionModificar);
    				}
    				else if (estado.equals("consultar"))
    				{
    					sustitutosForm.setEnModificar(false);
    					return this.accionComunIngresarModificarConsultar(mapping, con, sustitutosForm, accionConsultar);
    				}
    				else if(estado.equals("eliminar"))
    				{
    					sustitutosForm.setEnModificar(true);
    					sustitutosForm.setAccionAFinalizar("resumenConsultaAvanzada");
    					llenarLogHistorial(sustitutosForm);
    					return eliminarDatos(con,sustitutosForm,mapping,request,session);
    				}
    				else if(estado.equals("ordenar"))
    				{
    					this.accionOrdenar(sustitutosForm);
    					UtilidadBD.cerrarConexion(con);
    					return mapping.findForward("busquedaAvanzada");
    					//return null; 
    				}

    				else if(estado.equals("prepararModificacion"))
    				{

    					llenarLogHistorial(sustitutosForm);
    					sustitutosForm.setAccionAFinalizar("modificar");
    					sustitutosForm.setCodSustitutoOld(sustitutosForm.getCodSustituto());
    					UtilidadBD.cerrarConexion(con);
    					return mapping.findForward("modificarSustituto");
    				}

    				else if (estado.equals("salir"))
    				{
    					if (sustitutosForm.getAccionAFinalizar().equals("ingresar"))
    					{
    						//debido que al recargar trata de insertar nuevamente, ya que queda en el estado salir, por esto se cambia de estado

    						sustitutosForm.setAccionAFinalizar("resumenIngresarInsercion");
    						return ingresarDatos(con,sustitutosForm,mapping,request);
    					}
    					else if(sustitutosForm.getAccionAFinalizar().equals("consultar"))
    					{
    						//para poder recargar, al cambiar al estado no accede de nuevo a la B.D
    						sustitutosForm.setAccionAFinalizar ("resumenConsultaAvanzada");

    						return consultaAvanzada(con,sustitutosForm,mapping);
    					}
    					else if(sustitutosForm.getAccionAFinalizar().equals("modificar"))
    					{
    						return modificarDatos(con,sustitutosForm,mapping,request,session,response);
    					}
    					else if(sustitutosForm.getAccionAFinalizar().equals("resumenConsultaAvanzada"))
    					{
    						return consultaAvanzada(con,sustitutosForm,mapping);
    					}
    					else if(sustitutosForm.getAccionAFinalizar().equals("resumenIngresarInsercion"))
    					{
    						UtilidadBD.cerrarConexion(con);
    						return mapping.findForward("resumenSustituto");
    					}
    					else    
    					{
    						//Caso modificar, hago lo necesario
    						UtilidadBD.cerrarConexion(con);
    						return null;
    					}
    				}
    				else
    				{
    					sustitutosForm.reset();
    					return ComunAction.accionSalirCasoError(mapping, request, con, logger, "La accion específicada no esta definida ", "errors.estadoInvalido", true);
    				}
    			}
    			catch(Exception e)
    			{
    				e.printStackTrace();
    				//No se cierra conexión porque si llega aca ocurrió un
    				//error al abrirla
    				request.setAttribute("codigoDescripcionError", "errors.problemasBd");
    				return mapping.findForward("paginaError");
    			}
    		}
    		else
    		{
    			//Todavía no existe conexión, por eso no se cierra
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
	 * ActionForward que encapsula las funcionalidades
	 * (muy similares) de los inicios de las acciones
	 * modificar, consultar e ingresar
	 * 
	 * @param mapping Mapping con los recursos / sitios
	 * disponibles por struts
	 * @param con Conexión con la fuente de datos
	 * @param diagnosticosForm Forma de este action
	 * @param codigoAccionRealizar Variable entera que
	 * define que acción se quiere realizar
 * @return
	 * @throws Exception
	 */
	private ActionForward accionComunIngresarModificarConsultar (ActionMapping mapping, Connection con, SustitutosInventarioForm sustitutoForm, int codigoAccionRealizar) throws Exception
	{
	    String accionAFinalizar="", forward="";
	    
	    if (codigoAccionRealizar==accionConsultar)
	    {
	        accionAFinalizar="consultar";
	        forward="busquedaAvanzada";
	    }
	    else if (codigoAccionRealizar==accionModificar)
	    {
	        accionAFinalizar="modificar";
	        forward="busquedaAvanzada";
	    }
	    else if (codigoAccionRealizar==accionIngresar)
	    {
	        accionAFinalizar="ingresar";
	        forward="ingresarSustituto";
	    }
	    
	    sustitutoForm.reset();
	    sustitutoForm.setAccionAFinalizar(accionAFinalizar);
	    UtilidadBD.cerrarConexion(con);
	    return mapping.findForward(forward);
	}
	
	/**
	 *Metodo Para ingresar los datos 
	 * @param con, conexion
	 * @param diagnosticosForm, Form
	 * @return ActionForward, siguiente salto.
	 */
    private ActionForward ingresarDatos(Connection con,SustitutosInventarioForm sustitutoForm,ActionMapping mapping,HttpServletRequest request) 
    {
        SustitutosInventarios mundoSustitutosInventarios=new SustitutosInventarios(sustitutoForm.getCodPrincipal(),sustitutoForm.getCodSustituto());
        if(!mundoSustitutosInventarios.existeSustitutos(con,sustitutoForm.getCodPrincipal(),sustitutoForm.getCodSustituto()))
        {
	        if(mundoSustitutosInventarios.insertarSustitutosInventarios(con)<0)
	        {
	            try
	            {
	                return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Error en la insercion", "Error ingresando los datos en la Base de Datos.", true);
	            } 
	        	catch (Exception e) 
	        	{
	        	    logger.warn("Error generando el error"+e.toString());
	            }
	        }
	        try 
	        {
	            UtilidadBD.cerrarConexion(con);
	        } 
	        catch (SQLException e) 
	        {
	             logger.warn("error cerrando la BD"+e.toString());
	             return mapping.findForward("paginaError");
	        }
	        return mapping.findForward("resumenSustituto");
        }
        else
        {
            try {
                return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Registro ya existe en la B.D.", "El registro ya existe en la Base de Datos.", false);
            } catch (Exception e) {
                logger.warn("error generando el mapping a la pagina de error "+e.toString());
            }
        }
        return null;
    }
    
    private ActionForward modificarDatos(Connection con,SustitutosInventarioForm sustitutoForm,ActionMapping mapping, HttpServletRequest request,HttpSession session, HttpServletResponse response)throws Exception
    {
    	if(sustitutoForm.getCodSustituto()!=sustitutoForm.getCodSustitutoOld())
    	{
	        SustitutosInventarios  mundoSustitutosInventarios=new SustitutosInventarios(sustitutoForm.getCodPrincipal(),sustitutoForm.getCodSustituto(),sustitutoForm.getCodSustitutoOld());
	        if(!mundoSustitutosInventarios.existeSustitutos(con,sustitutoForm.getCodPrincipal(),sustitutoForm.getCodSustituto()))
	        {
	        	if(mundoSustitutosInventarios.modificarSustitutosInventarios(con)<0)
		        {
		            try
		            {
		                return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Error en la modificacion", "Error modificando los datos de la Base de Datos.", true);
		            } 
		        	catch (Exception e) 
		        	{
		        	    logger.warn("Error generando el error"+e.toString());
		            }
		        }
		       else
		        {
		            generarLogModificacion(sustitutoForm,session);
		       
		       }
		        try 
		        {
		            UtilidadBD.cerrarConexion(con);
		        } 
		        catch (SQLException e) 
		        {
		             logger.warn("error cerrando la BD"+e.toString());
		             return mapping.findForward("paginaError");
		        }
		        return mapping.findForward("resumenSustituto");
	        }
	        else
	        {
	            try {
	                return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Registro ya existe en la B.D.", "El registro ya existe en la Base de Datos.", false);
	            } catch (Exception e) {
	                logger.warn("error generando el mapping a la pagina de error "+e.toString());
	            }
	        }
	       
    	}
    	else
		{
    			UtilidadBD.cerrarConexion(con);
    			response.sendRedirect("sustituto.do?estado=prepararModificacion&codPrincipal="+sustitutoForm.getCodPrincipal()+"&codSustituto="+sustitutoForm.getCodSustituto());
    		
		}
    	return null;
	   
    }
    
    /**
     * Metodo para eliminar un Diagnostico, dado el codigo del diagnostico y el codigo del tipo CIE
     * @param con, conexion
     * @param diagnosticosForm, form
     * @param mapping
     * @param request
     */
    private ActionForward eliminarDatos(Connection con, SustitutosInventarioForm sustitutoForm, ActionMapping mapping, HttpServletRequest request,HttpSession session) throws Exception 
    {
        SustitutosInventarios  mundoSustitutosInventarios=new SustitutosInventarios(sustitutoForm.getCodPrincipal(),sustitutoForm.getCodSustituto());
        if(mundoSustitutosInventarios.eliminarSustitutosInventarios(con)<0)
        {
            try
            {
                return ComunAction.accionSalirCasoError(mapping, request, con, logger, "mensajedebug", "El diagnostico es utilizado en otras funcionalidades, no se puede eliminar", false);
            } 
        	catch (Exception e) 
        	{
        	    logger.warn("Error generando el error"+e.toString());
            }
        }
        else
        {
            generarLogEliminacion(sustitutoForm,session);
        }
            //UtilidadBD.cerrarConexion(con);
           // response.sendRedirect("sustituto.do?estado=salir");
        sustitutoForm.setCodPrincipalBusqueda(sustitutoForm.getCodPrincipal());
        return consultaAvanzada(con,sustitutoForm,mapping);
    }
    
    /**
     * Metodo para realizar la consulta avanzada para la tabla sustitutos inventario
     * @param con, conexion
     * @param sustitutosForm, form
     * @param mapping
     * @return forward
     */
    private ActionForward consultaAvanzada(Connection con, SustitutosInventarioForm sustitutosForm, ActionMapping mapping)throws Exception 
    {
        SustitutosInventarios mundoSustitutosInventarios=new SustitutosInventarios();
        sustitutosForm.setColeccion(mundoSustitutosInventarios.consultarSustitutosInventarios(con,sustitutosForm.getCodPrincipalBusqueda()));
        sustitutosForm.setCodPrincipal(sustitutosForm.getCodPrincipalBusqueda());
        sustitutosForm.setCodPrincipalBusqueda(ConstantesBD.codigoNuncaValido);
        sustitutosForm.setAccionAFinalizar("consultar");
        UtilidadBD.cerrarConexion(con);
        sustitutosForm.setUltimaPropiedad("codigo");
        sustitutosForm.setColumna("descripcion");
        this.accionOrdenar(sustitutosForm);
        //forward anterior resultadoBusquedaAvanzada
        return mapping.findForward("busquedaAvanzada");
    }
    
    /**
     * Metodo que llena el log con la informacion inicial
     * @param diagnosticoForm
     */
    private void llenarLogHistorial(SustitutosInventarioForm sustitutosForm)
    {
        sustitutosForm.setLogHistorial("\n            ====INFORMACION ORIGINAL===== "+
        		"\n*  Código del Articulo Principal [" +sustitutosForm.getCodPrincipal()+"] "+
        		"\n*  Código del Articulo Sustituto [" +sustitutosForm.getCodSustituto()+"] ");
    }
    private void generarLogModificacion(SustitutosInventarioForm sustitutosForm,HttpSession session)
    {
        UsuarioBasico usuario;
        String log=sustitutosForm.getLogHistorial() +
		"\n            ====INFORMACION DESPUES DE LA MODIFICACION===== " +
		"\n*  Código del Articulo Principal [" +sustitutosForm.getCodPrincipal()+"] "+
		"\n*  Código del Articulo Sustituto [" +sustitutosForm.getCodSustituto()+"] "+
		"\n========================================================\n\n\n " ;
        usuario=(UsuarioBasico)session.getAttribute("usuarioBasico");
        LogsAxioma.enviarLog(ConstantesBD.logSustitutosInventario, log, ConstantesBD.tipoRegistroLogModificacion, usuario.getLoginUsuario());
    }
    private void generarLogEliminacion(SustitutosInventarioForm sustitutosForm,HttpSession session)
    {
        UsuarioBasico usuario;
        String log="\n                REGISTRO ELIMINADO                " +
        sustitutosForm.getLogHistorial() +
		"\n========================================================\n\n\n " ;
        usuario=(UsuarioBasico)session.getAttribute("usuarioBasico");
        LogsAxioma.enviarLog(ConstantesBD.logSustitutosInventario, log, ConstantesBD.tipoRegistroLogEliminacion, usuario.getLoginUsuario());
    }
    /**
     * Procedimiento que ordena los datos del resumen segun la columna seleccionada
     * @param documentoForm
     * @param mapping
     * 
     */
    private void accionOrdenar(SustitutosInventarioForm sustitutosForm)
    {
        
        try
        {
            sustitutosForm.setColeccion(Listado.ordenarColumna(new ArrayList(sustitutosForm.getColeccion()), sustitutosForm.getUltimaPropiedad(),sustitutosForm.getColumna()));
            sustitutosForm.setUltimaPropiedad(sustitutosForm.getColumna());
        }
        catch (Exception e)
        {
            logger.warn("Error en el listado de documentos");
            e.printStackTrace();
        }
         
    }

}
