/*
 * @(#)DiagnosticosAction.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_04
 *
 */

package com.princetonsa.action.parametrizacion;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;

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
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.ValoresPorDefecto;

import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.parametrizacion.DiagnosticosForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.parametrizacion.RegistroDiagnosticos;
import com.princetonsa.pdf.DiagnosticosPdf;

/**
 * Clase encargada del control de la funcionalidad de
 * que maneja la consulta / modificación e inserción
 * de diagnósticos
 * 
 *	@version 1.0, 17/08/2004
 */
public class DiagnosticosAction extends Action
{

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
	 * Objeto para almacenar los logs que aparezcan en el Action 
	 * de Axioma
	 */
	private Logger logger = Logger.getLogger(DiagnosticosAction.class);
    
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
			if (form instanceof DiagnosticosForm)
			{
				DiagnosticosForm diagnosticosForm=(DiagnosticosForm)form;
				String estado=diagnosticosForm.getEstado();

				try
				{

					String tipoBD = System.getProperty("TIPOBD");
					DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
					con = myFactory.getConnection();
					//Lo primero que vamos a hacer es validar que se
					//cumplan las condiciones.

					HttpSession session=request.getSession();
					UsuarioBasico medico = (UsuarioBasico)session.getAttribute("usuarioBasico");

					logger.warn("estado--->"+estado);
					//Primera Condición: El usuario debe existir
					//la validación de si es médico o no solo se hace en insertar
					if( medico == null )
					{
						return ComunAction.accionSalirCasoError(mapping, request, con, logger, "No existe el usuario", "errors.usuario.noCargado", true);
					}

					else if (estado==null||estado.equals(""))
					{
						diagnosticosForm.reset();
						return ComunAction.accionSalirCasoError(mapping, request, con, logger, "La accion específicada no esta definida ", "errors.estadoInvalido", true);
					}
					else if (estado.equals("ingresar"))
					{
						return this.accionComunIngresarModificarConsultar(mapping, con, diagnosticosForm, accionIngresar);
					}
					else if (estado.equals("modificar"))
					{
						diagnosticosForm.setEnModificar(true);
						return this.accionComunIngresarModificarConsultar(mapping, con, diagnosticosForm, accionModificar);
					}
					else if (estado.equals("consultar"))
					{
						diagnosticosForm.setEnModificar(false);
						return this.accionComunIngresarModificarConsultar(mapping, con, diagnosticosForm, accionConsultar);
					}
					else if(estado.equals("eliminar"))
					{
						diagnosticosForm.setEnModificar(true);
						diagnosticosForm.setEstado("consultar");
						diagnosticosForm.setAccionAFinalizar ("resumenConsultaAvanzada");
						llenarLogHistorial(diagnosticosForm);
						return eliminarDatos(con,diagnosticosForm,mapping,request,response,session);
					}
					else if(estado.equals("prepararModificacion"))
					{
						cargarFormRegistroInsertado(con,diagnosticosForm);
						llenarLogHistorial(diagnosticosForm);
						diagnosticosForm.setAccionAFinalizar("modificar");
						UtilidadBD.cerrarConexion(con);
						return mapping.findForward("modificarDiagnostico");
					}
					else if(estado.equals("ordenar"))
					{
						diagnosticosForm.setColeccion(Listado.ordenarColumna(new ArrayList(diagnosticosForm.getColeccion()),diagnosticosForm.getUltimaPropiedad(),diagnosticosForm.getCampo()));
						diagnosticosForm.setUltimaPropiedad(diagnosticosForm.getCampo());
						UtilidadBD.cerrarConexion(con);
						return mapping.findForward("resumenDiagnosticos");
					}
					else if (estado.equals("imprimir"))
					{
						String nombreArchivo;
						Random r=new Random();
						nombreArchivo="/aBorrar" + r.nextInt()  +".pdf";
						try
						{
							DiagnosticosPdf.pdfDiagnosticos(ValoresPorDefecto.getFilePath() + nombreArchivo, diagnosticosForm, medico, request);
						}
						catch (Exception e)
						{
							return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Error de tamaño generando PDF de Diagnosticos", "errors.pdfSuperoMemoria", true);
						}

						UtilidadBD.cerrarConexion(con);
						request.setAttribute("nombreArchivo", nombreArchivo);
						request.setAttribute("nombreVentana", "Consulta Diagnósticos");
						return mapping.findForward("abrirPdf");
					}
					else if (estado.equals("salir"))
					{
						if (diagnosticosForm.getAccionAFinalizar().equals("ingresar"))
						{
							//debido que al recargar trata de insertar nuevamente, ya que queda en el estado salir, por esto se cambia de estado
							diagnosticosForm.setAccionAFinalizar ("resumenIngresarInsercion");
							return ingresarDatos(con,diagnosticosForm,mapping,request);
						}
						else if(diagnosticosForm.getAccionAFinalizar().equals("consultar"))
						{
							//para poder recargar, al cambiar al estado no accede de nuevo a la B.D
							diagnosticosForm.setAccionAFinalizar ("resumenConsultaAvanzada");

							return consultaAvanzada(con,diagnosticosForm,mapping);
						}
						else if(diagnosticosForm.getAccionAFinalizar().equals("modificar"))
						{
							return modificarDatos(con,diagnosticosForm,mapping,request,session);
						}

						else if(diagnosticosForm.getAccionAFinalizar().equals("resumenIngresarModificar"))
						{
							UtilidadBD.cerrarConexion(con);
							return mapping.findForward("resultadoModificacion");
						}
						else if(diagnosticosForm.getAccionAFinalizar().equals("resumenIngresarInsercion"))
						{
							UtilidadBD.cerrarConexion(con);
							return mapping.findForward("resultadoModificacion");
						}
						else if(diagnosticosForm.getAccionAFinalizar().equals("resumenConsultaAvanzada"))
						{
							UtilidadBD.cerrarConexion(con);
							diagnosticosForm.setMensaje(new ResultadoBoolean(true,"OPERACION REALIZADA CON EXITO!!!!!"));
							return mapping.findForward("resumenDiagnosticos");
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
						diagnosticosForm.reset();
						return ComunAction.accionSalirCasoError(mapping, request, con, logger, "La accion específicada no esta definida ", "errors.estadoInvalido", true);
					}
				}
				catch(Exception e)
				{
					e.printStackTrace();
					//No se cierra conexión porque si llega aca ocurrió un
					//error al abrirla
					request.setAttribute("codigoDescripcionError", "errors.problemasBd");
					UtilidadBD.cerrarConexion(con);				
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
	private ActionForward accionComunIngresarModificarConsultar (ActionMapping mapping, Connection con, DiagnosticosForm diagnosticosForm, int codigoAccionRealizar) throws Exception
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
	        forward="ingresarDiagnostico";
	    }
	    
	    diagnosticosForm.reset();
	    diagnosticosForm.setAccionAFinalizar(accionAFinalizar);
	    //
	    RegistroDiagnosticos mundoRegistroDiagnosticos= new RegistroDiagnosticos(diagnosticosForm.getAcronimo(),diagnosticosForm.getCodigoTipoCie(),diagnosticosForm.getDescripcion(),diagnosticosForm.getActivo() , diagnosticosForm.getSexo(), diagnosticosForm.getEdadInicial()=="" ? 0 : Integer.parseInt(diagnosticosForm.getEdadInicial()), diagnosticosForm.getEdadFinal()=="" ? 0 : Integer.parseInt(diagnosticosForm.getEdadFinal()), diagnosticosForm.getEsPrincipal(), diagnosticosForm.getEsMuerte());
	    diagnosticosForm.setHsexo(mundoRegistroDiagnosticos.consultarSexo(con));
	    diagnosticosForm.setNumhSexo(Integer.parseInt(diagnosticosForm.getHsexo("numRegistros").toString()));
	    
	    
	    //
	    UtilidadBD.cerrarConexion(con);
	    return mapping.findForward(forward);
	}
	
	/**
	 *Metodo Para ingresar los datos 
	 * @param con, conexion
	 * @param diagnosticosForm, Form
	 * @return ActionForward, siguiente salto.
	 */
    private ActionForward ingresarDatos(Connection con,DiagnosticosForm diagnosticosForm,ActionMapping mapping,HttpServletRequest request) 
    {
    	
        RegistroDiagnosticos mundoRegistroDiagnosticos= new RegistroDiagnosticos(diagnosticosForm.getAcronimo(),diagnosticosForm.getCodigoTipoCie(),diagnosticosForm.getDescripcion(),diagnosticosForm.getActivo() , diagnosticosForm.getSexo(), diagnosticosForm.getEdadInicial()=="" ? 0 : Integer.parseInt(diagnosticosForm.getEdadInicial()), diagnosticosForm.getEdadFinal()=="" ? 0 : Integer.parseInt(diagnosticosForm.getEdadFinal()), diagnosticosForm.getEsPrincipal(), diagnosticosForm.getEsMuerte());
        if(!mundoRegistroDiagnosticos.existeDiagnostico(con,diagnosticosForm.getAcronimo(),diagnosticosForm.getCodigoTipoCie()))
        {
	        if(mundoRegistroDiagnosticos.insertarDiagnostico(con)<0)
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
	        cargarFormRegistroInsertado(con,diagnosticosForm);
	        try 
	        {
	            UtilidadBD.cerrarConexion(con);
	        } 
	        catch (SQLException e) 
	        {
	             logger.warn("error cerrando la BD"+e.toString());
	             return mapping.findForward("paginaError");
	        }
	        diagnosticosForm.setMensaje(new ResultadoBoolean(true,"OPERACION REALIZADA CON EXITO!!!!!"));
	        return mapping.findForward("resultadoModificacion");
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
    
    
    
    /**
     * Metodo para modificar un registro de la tabla diagnostico.
     * @param con, Conexion
     * @param diagnosticosForm, Form
     * @param mapping
     * @param request
     * @return Forward
     */
    
    
    private ActionForward modificarDatos(Connection con,DiagnosticosForm diagnosticosForm,ActionMapping mapping,HttpServletRequest request,HttpSession session)
    {
        RegistroDiagnosticos mundoRegistroDiagnosticos= new RegistroDiagnosticos(diagnosticosForm.getAcronimo(),diagnosticosForm.getCodigoTipoCie(),diagnosticosForm.getDescripcion(),diagnosticosForm.getActivo(), diagnosticosForm.getSexo(), diagnosticosForm.getEdadInicial()=="" ? 0 : Integer.parseInt(diagnosticosForm.getEdadInicial()), diagnosticosForm.getEdadFinal()=="" ? 0 : Integer.parseInt(diagnosticosForm.getEdadFinal()), diagnosticosForm.getEsPrincipal(), diagnosticosForm.getEsMuerte());
        if(mundoRegistroDiagnosticos.modificarDiagnostico(con)<0)
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
            generarLogModificacion(diagnosticosForm,session);
            cargarFormRegistroInsertado(con,diagnosticosForm);
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
        return mapping.findForward("resultadoModificacion");
    }
    
    
    /**
     * Metodo para eliminar un Diagnostico, dado el codigo del diagnostico y el codigo del tipo CIE
     * @param con, conexion
     * @param diagnosticosForm, form
     * @param mapping
     * @param request
     */
    private ActionForward eliminarDatos(Connection con, DiagnosticosForm diagnosticosForm, ActionMapping mapping, HttpServletRequest request, HttpServletResponse response,HttpSession session) throws Exception 
    {
        RegistroDiagnosticos mundoRegistroDiagnosticos= new RegistroDiagnosticos(diagnosticosForm.getAcronimo(),diagnosticosForm.getCodigoTipoCie(),diagnosticosForm.getDescripcion(),diagnosticosForm.getActivo() , diagnosticosForm.getSexo(), diagnosticosForm.getEdadInicial()=="" ? 0 : Integer.parseInt(diagnosticosForm.getEdadInicial()), diagnosticosForm.getEdadFinal()=="" ? 0 : Integer.parseInt(diagnosticosForm.getEdadFinal()), diagnosticosForm.getEsPrincipal(), diagnosticosForm.getEsMuerte());
        if(mundoRegistroDiagnosticos.eliminarDiagnostico(con)<0)
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
            generarLogEliminacion(diagnosticosForm,session);
            actualizarVista(con,diagnosticosForm,mundoRegistroDiagnosticos);
        }
            UtilidadBD.cerrarConexion(con);
            response.sendRedirect("diagnosticos.do?estado=salir");
        return null;
        //return mapping.findForward("resumenDiagnosticos");
        
    }
    
    /**
     * Metodo que realiza una consulta avanzada sobre la tabla diagnosticos
     * @param con
     * @param diagnosticosForm
     * @param mapping
     * @param request
     * @return ActionForward, siguiente salto
     */
    private ActionForward consultaAvanzada(Connection con,DiagnosticosForm diagnosticosForm,ActionMapping mapping) throws Exception
    {
    
        diagnosticosForm.setAcronimoUltimaBusqueda(diagnosticosForm.getAcronimo());
        diagnosticosForm.setCoditoTipoCieUltimaBusqueda(diagnosticosForm.getCodigoTipoCie());
        diagnosticosForm.setDescripcionUltimaBusqueda(diagnosticosForm.getDescripcion());
        diagnosticosForm.setActivoUltimaBusqueda(diagnosticosForm.getActivo());
        
        diagnosticosForm.setSexoUltimaBusqueda(diagnosticosForm.getSexo());
        diagnosticosForm.setEdadInicialUltimaBusqueda(diagnosticosForm.getEdadInicial().toString());
        diagnosticosForm.setEdadFinalUltimaBusqueda(diagnosticosForm.getEdadFinal().toString());
        diagnosticosForm.setEsPrincipalUltimaBusqueda(diagnosticosForm.getEsPrincipal());
        diagnosticosForm.setEsMuerteUltimaBusqueda(diagnosticosForm.getEsMuerte());        
        
        RegistroDiagnosticos mundoRegistroDiagnosticos= new RegistroDiagnosticos(diagnosticosForm.getAcronimo(),diagnosticosForm.getCodigoTipoCie(),diagnosticosForm.getDescripcion().trim(),diagnosticosForm.getActivo(), diagnosticosForm.getSexo(), diagnosticosForm.getEdadInicial()=="" ? 0 : Integer.parseInt(diagnosticosForm.getEdadInicial()), diagnosticosForm.getEdadFinal()=="" ? 0 : Integer.parseInt(diagnosticosForm.getEdadFinal()), diagnosticosForm.getEsPrincipal(), diagnosticosForm.getEsMuerte());
        
        
        diagnosticosForm.setColeccion(mundoRegistroDiagnosticos.consultaAvanzada(con,diagnosticosForm.getBuscarPorAcronimo(),diagnosticosForm.getBuscarPorTipoCie(),diagnosticosForm.getBuscarPorDescripcion(),diagnosticosForm.getBuscarPorActivo() , diagnosticosForm.getBuscarPorSexo(), diagnosticosForm.getBuscarPorEdadInicial(), diagnosticosForm.getBuscarPorEdadFinal(), diagnosticosForm.getBuscarPorEsPrincipal(), diagnosticosForm.getBuscarPorEsMuerte()  ));
        UtilidadBD.cerrarConexion(con);
        return mapping.findForward("resumenDiagnosticos");
    }
    
    /**
     * Metodo que carga en el form el registro insertado, pero obteniendolo desde la BD.
     * @param con, Conexion a la BD
     * @param diagnosticosForm Forma particular a los
     * diagnosticos
     */
    private void cargarFormRegistroInsertado(Connection con,DiagnosticosForm diagnosticosForm)
    {
        RegistroDiagnosticos mundoRegistroDiagnosticos= new RegistroDiagnosticos();
        mundoRegistroDiagnosticos.consultarDiagnostico(con,diagnosticosForm.getAcronimo(),diagnosticosForm.getCodigoTipoCie());
        llenarForm(mundoRegistroDiagnosticos,diagnosticosForm);
    }
    
    /**
     * Metodo para llenar el Form a partir de los datos que se encuentran en el mundo.
     * @param mundoRegistroDiagnosticos
     * @param diagnosticosForm Forma particular a los
     * diagnosticos
     */
    private void llenarForm(RegistroDiagnosticos mundoRegistroDiagnosticos,DiagnosticosForm diagnosticosForm)
    {
        diagnosticosForm.setAcronimo(mundoRegistroDiagnosticos.getCodigo());
        diagnosticosForm.setCodigoTipoCie(mundoRegistroDiagnosticos.getCIE());
        diagnosticosForm.setNombreTipoCie(mundoRegistroDiagnosticos.getNombreTipoCIE());
        diagnosticosForm.setDescripcion(mundoRegistroDiagnosticos.getDescripcion());
        diagnosticosForm.setActivo(mundoRegistroDiagnosticos.getEstado());
        
        diagnosticosForm.setSexo(mundoRegistroDiagnosticos.getSexo());
        diagnosticosForm.setEdadInicial( mundoRegistroDiagnosticos.getEdadInicial()+"" );
        diagnosticosForm.setEdadFinal(mundoRegistroDiagnosticos.getEdadFinal()+"");
        diagnosticosForm.setEsPrincipal(mundoRegistroDiagnosticos.getEsPrincipal());
        diagnosticosForm.setEsMuerte(mundoRegistroDiagnosticos.getEsMuerte());
        diagnosticosForm.setNomhSexo(mundoRegistroDiagnosticos.getNomhSexo());
    }
    
    /**
     * Método que permite actualizar los resultados de una
     * búsqueda, una vez se haya ejecutado un cambio sobre
     * estos
     * 
     * @param con Conexión con la fuente de datos
     * @param diagnosticosForm Forma particular a los
     * diagnosticos
     * @param mundoRegistroDiagnosticos Objeto del mundo
     * para RegistroDiagnosticos
     */
    private void actualizarVista(Connection con,DiagnosticosForm diagnosticosForm,RegistroDiagnosticos mundoRegistroDiagnosticos)
    {
        diagnosticosForm.setAcronimo(diagnosticosForm.getAcronimoUltimaBusqueda());
        diagnosticosForm.setCodigoTipoCie(diagnosticosForm.getCoditoTipoCieUltimaBusqueda());
        diagnosticosForm.setDescripcion(diagnosticosForm.getDescripcionUltimaBusqueda());
        diagnosticosForm.setActivo(diagnosticosForm.getActivoUltimaBusqueda());
        
        diagnosticosForm.setSexo(diagnosticosForm.getSexoUltimaBusqueda());
        diagnosticosForm.setEdadInicial(diagnosticosForm.getEdadInicialUltimaBusqueda());
        diagnosticosForm.setEdadFinal(diagnosticosForm.getEdadFinalUltimaBusqueda());
        diagnosticosForm.setEsPrincipal(diagnosticosForm.getEsPrincipalUltimaBusqueda());
        diagnosticosForm.setEsMuerte(diagnosticosForm.getEsMuerteUltimaBusqueda());
        
        //
        mundoRegistroDiagnosticos.setCodigo(diagnosticosForm.getAcronimo());
        mundoRegistroDiagnosticos.setCIE(diagnosticosForm.getCodigoTipoCie());
        mundoRegistroDiagnosticos.setDescripcion(diagnosticosForm.getDescripcion());
        mundoRegistroDiagnosticos.setEstado(diagnosticosForm.getActivo());
        
        mundoRegistroDiagnosticos.setSexo(diagnosticosForm.getSexo());
        try
        {
	        mundoRegistroDiagnosticos.setEdadInicial(Integer.parseInt(diagnosticosForm.getEdadInicial()));
	        mundoRegistroDiagnosticos.setEdadFinal(Integer.parseInt(diagnosticosForm.getEdadFinal()));
        }
        catch(Exception e)
        {
        	//Error al tratar de pasar a numerico la edad inicial y final
        	mundoRegistroDiagnosticos.setEdadInicial(0);
        	mundoRegistroDiagnosticos.setEdadFinal(0);
        }
        mundoRegistroDiagnosticos.setEsPrincipal(diagnosticosForm.getEsPrincipal());
        mundoRegistroDiagnosticos.setEsMuerte(diagnosticosForm.getEsMuerte());

        diagnosticosForm.setColeccion(mundoRegistroDiagnosticos.consultaAvanzada(con,diagnosticosForm.getBuscarPorAcronimo(),diagnosticosForm.getBuscarPorTipoCie(),diagnosticosForm.getBuscarPorDescripcion(),diagnosticosForm.getBuscarPorActivo(), diagnosticosForm.getBuscarPorSexo(), diagnosticosForm.getBuscarPorEdadInicial(), diagnosticosForm.getBuscarPorEdadFinal(), diagnosticosForm.getBuscarPorEsPrincipal(), diagnosticosForm.getBuscarPorEsMuerte() ));
    }
    
    /**
     * Método para llenar el log con la historia de 
     * modificaciones / inserciones hechas a diagnósticos
     * 
     * @param diagnosticosForm Forma particular a los
     * diagnosticos
     */
    private void llenarLogHistorial(DiagnosticosForm diagnosticoForm)
    {
        diagnosticoForm.setLogHistorial("\n            ====INFORMACION ORIGINAL===== "+
        		"\n*  Código del diagnóstico [" +diagnosticoForm.getAcronimo()+"] "+
        		"\n*  Código del CIE [" +diagnosticoForm.getCodigoTipoCie()+"] "+
        		"\n*  Nombre CIE [" +diagnosticoForm.getNombreTipoCie()+"] "+
        		"\n*  Estado del diagnóstico [" +diagnosticoForm.getActivo()+"] "+
				"\n*  Descripción [" +diagnosticoForm.getDescripcion()+"] " +
				"\n*  Codigo del Sexo [" +diagnosticoForm.getSexo()+"] " +
				"\n*  Edad Inicial [" +diagnosticoForm.getEdadInicial()+"] " +
				"\n*  Edad Final [" +diagnosticoForm.getEdadFinal()+"] " +
				"\n*  Es Principal [" +diagnosticoForm.getEsPrincipal()+"] " +
				"\n*  Es Muerte [" +diagnosticoForm.getEsMuerte()+"] ");

		
    }
    
    /**
     * Método para llenar el log con la historia de 
     * modificaciones hechas a diagnósticos
     * 
     * @param diagnosticosForm Forma particular a los
     * diagnosticos
     * @param session Objeto para manejar los elementos
     * que se encuentran en sesión
     */
    private void generarLogModificacion(DiagnosticosForm diagnosticoForm,HttpSession session)
    {
        UsuarioBasico usuario;
        String log=diagnosticoForm.getLogHistorial() +
		"\n            ====INFORMACION DESPUES DE LA MODIFICACION===== " +
		"\n*  Código del diagnóstico [" +diagnosticoForm.getAcronimo()+"] "+
		"\n*  Código del CIE [" +diagnosticoForm.getCodigoTipoCie()+"] "+
		"\n*  Nombre CIE [" +diagnosticoForm.getNombreTipoCie()+"] "+
		"\n*  Estado del diagnóstico [" +diagnosticoForm.getActivo()+"]" +
		"\n*  Descripción [" +diagnosticoForm.getDescripcion()+"]" +
		
		"\n*  Codigo del Sexo [" +diagnosticoForm.getSexo()+"]" +
		"\n*  Edad Inicial [" +diagnosticoForm.getEdadInicial()+"]" +
		"\n*  dad Final [" +diagnosticoForm.getEdadFinal()+"]" +
		"\n*  Es Principal [" +diagnosticoForm.getEsPrincipal()+"]" +
		"\n*  Es Muerte [" +diagnosticoForm.getEsMuerte()+"]" +
		"\n========================================================\n\n\n " ;
        usuario=(UsuarioBasico)session.getAttribute("usuarioBasico");
        LogsAxioma.enviarLog(ConstantesBD.logRegistroDiagnosticoCodigo, log, ConstantesBD.tipoRegistroLogModificacion, usuario.getLoginUsuario());
    }
    
    /**
     * Método para llenar el log con la historia de 
     * eliminación de diagnosticos
     * 
     * @param diagnosticosForm Forma particular a los
     * diagnosticos
     * @param session Objeto para manejar los elementos
     * que se encuentran en sesión
     */
    private void generarLogEliminacion(DiagnosticosForm diagnosticoForm,HttpSession session)
    {
        UsuarioBasico usuario;
        String log="\n                REGISTRO ELIMINADO                " +
        			diagnosticoForm.getLogHistorial() +
		"\n========================================================\n\n\n " ;
        usuario=(UsuarioBasico)session.getAttribute("usuarioBasico");
        LogsAxioma.enviarLog(ConstantesBD.logRegistroDiagnosticoCodigo, log, ConstantesBD.tipoRegistroLogEliminacion, usuario.getLoginUsuario());
    }
}
