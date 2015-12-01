/*
 * @(#)ConsultaReferenciaContrareferenciaAction.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK jdk1.5.0_07
 *
 */
package com.princetonsa.action.historiaClinica;

import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.axioma.util.log.Log4JManager;

import util.Listado;
import util.UtilidadBD;

import com.princetonsa.actionform.historiaClinica.ConsultaReferenciaContrareferenciaForm;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.historiaClinica.ConsultaReferenciaContrareferencia;

/**
 * Clase para el manejo del workflow de la funcionalidad
 * @author Wilson Rios
 * wrios@princetonsa.com
 */
public class ConsultaReferenciaContrareferenciaAction extends Action
{
	/**
     * Objeto para manejar los logs de esta clase
    */
    private Logger logger = Logger.getLogger(ConsultaReferenciaContrareferenciaAction.class);
    
    /**
	 * Metodo excute del Action
	 */
    public ActionForward execute(   ActionMapping mapping,
            						ActionForm form,
            						HttpServletRequest request,
            						HttpServletResponse response ) throws Exception
            						{

    	Connection con=null;
    	try {
    		if (response==null); 
    		if(form instanceof ConsultaReferenciaContrareferenciaForm)
    		{
    			con = UtilidadBD.abrirConexion();
    			if(con == null)
    			{
    				request.setAttribute("codigoDescripcionError", "errors.problemasBd");
    				return mapping.findForward("paginaError");
    			}

    			UsuarioBasico usuario= (UsuarioBasico)request.getSession().getAttribute("usuarioBasico"); 
    			PersonaBasica paciente = (PersonaBasica)request.getSession().getAttribute("pacienteActivo");
    			ConsultaReferenciaContrareferenciaForm forma =(ConsultaReferenciaContrareferenciaForm)form;
    			String estado=forma.getEstado();
    			logger.warn("\n\n\nEl estado en ConsultaReferenciaContrareferenciaAction es------->"+estado+"\n");

    			if(estado == null)
    			{
    				forma.reset(); 
    				logger.warn("Estado no valido dentro del flujo de Consulta Referencia Contrareferencia (null) ");
    				request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
    				UtilidadBD.cerrarConexion(con);
    				return mapping.findForward("paginaError");
    			}
    			else if(estado.equals("empezarXPaciente"))
    			{
    				//validacionAcceso
    				if(paciente == null|| paciente.getCodigoTipoIdentificacionPersona().equals(""))
    				{
    					logger.warn("Paciente no válido (null)");			
    					request.setAttribute("codigoDescripcionError", "errors.paciente.noCargado");
    					return mapping.findForward("paginaError");
    				}
    				return this.accionEmpezarXPaciente(con, forma, mapping, paciente);
    			}
    			else if(estado.equals("empezarXPeriodo"))
    			{
    				return this.accionEmpezarXPeriodo(con, forma, mapping, usuario);
    			}
    			else if(estado.equals("resultadoBusquedaXPeriodo"))
    			{
    				return this.accionResultadoBusquedaXPeriodo(con, forma, mapping);
    			}
    			else if(estado.equals("continuarErroresXPeriodo"))
    			{
    				UtilidadBD.closeConnection(con);
    				return mapping.findForward("principalXPeriodo");
    			}
    			else if(estado.equals("ordenar"))
    			{
    				this.accionOrdenarMapa(forma);
    				UtilidadBD.closeConnection(con);
    				if(forma.getEsBusquedaXpaciente())
    					return mapping.findForward("principalXPaciente");
    				else
    					return mapping.findForward("principalXPeriodo");
    			}
    			else
    			{
    				forma.reset(); 
    				logger.warn("Estado no valido dentro del flujo de Consulta Referencia Contrareferencia estado: "+estado);
    				request.setAttribute("codigoDescripcionError", "errors.formaTipoInvalido");
    				UtilidadBD.cerrarConexion(con);
    				return mapping.findForward("paginaError");
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
     * 
     * @param forma
     */
    private void accionOrdenarMapa(ConsultaReferenciaContrareferenciaForm forma) 
    {
    	String[] indices= (String[])forma.getMapaListado("INDICES_MAPA");
		int numReg=Integer.parseInt(forma.getMapaListado("numRegistros")+"");
		forma.setMapaListado(Listado.ordenarMapa(indices,forma.getPatronOrdenar(),forma.getUltimoPatron(),forma.getMapaListado(),numReg));
		forma.setUltimoPatron(forma.getPatronOrdenar());
		forma.setMapaListado("numRegistros",numReg+"");	
		forma.setMapaListado("INDICES_MAPA", indices);
		
	}

	/**
     * 
     * @param con
     * @param forma
     * @param mapping
     * @param paciente
     * @return
     */
	private ActionForward accionEmpezarXPaciente(Connection con, ConsultaReferenciaContrareferenciaForm forma, ActionMapping mapping, PersonaBasica paciente) 
	{
		forma.reset();
		ConsultaReferenciaContrareferencia mundo= new ConsultaReferenciaContrareferencia();
		llenarMundoFormaXPaciente(forma, mundo, paciente);
		forma.setMapaListado(ConsultaReferenciaContrareferencia.busquedaReferenciaContrareferencia(con, mundo));
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principalXPaciente");
	}
	
	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mapping
	 * @param usuario
	 * @return
	 */
	private ActionForward accionEmpezarXPeriodo(Connection con, ConsultaReferenciaContrareferenciaForm forma, ActionMapping mapping, UsuarioBasico usuario) 
	{
		forma.reset();
		forma.setCodigoInstitucion(usuario.getCodigoInstitucionInt());
		forma.setEsBusquedaXpaciente(false);
		forma.inicializarTagsMap(usuario.getCodigoInstitucionInt());
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principalXPeriodo");
	}

	/**
	 * 
	 * @param forma
	 * @param mundo
	 * @param paciente
	 */
	private void llenarMundoFormaXPaciente(ConsultaReferenciaContrareferenciaForm forma, ConsultaReferenciaContrareferencia mundo, PersonaBasica paciente) 
	{
		forma.setEsBusquedaXpaciente(true);
		forma.setCodigoPaciente(paciente.getCodigoPersona()+"");
		mundo.setEsBusquedaXpaciente(true);
		mundo.setCodigoPaciente(forma.getCodigoPaciente());
		armarLinkVolver(forma);
	}
	
	/**
	 * 
	 * @param mundo
	 * @param forma
	 */
	private void llenarMundoXPeriodo(ConsultaReferenciaContrareferencia mundo, ConsultaReferenciaContrareferenciaForm forma)
	{
		mundo.setEsBusquedaXpaciente(false);
		mundo.setCodigoInstitucion(forma.getCodigoInstitucion());
		mundo.setCodigoInstitucionOrigen(forma.getCodigoInstitucionOrigen());
		mundo.setFechaFinalizacionFinal(forma.getFechaFinalizacionFinal());
		mundo.setFechaFinalizacionInicial(forma.getFechaFinalizacionInicial());
		mundo.setRangoNroReferenciaFinal(forma.getRangoNroReferenciaFinal());
		mundo.setRangoNroReferenciaInicial(forma.getRangoNroReferenciaInicial());
		mundo.setTipoContrareferencia(forma.getTipoContrareferencia());
		mundo.setTipoReferenciaInterna(forma.getTipoReferenciaInterna());
		armarLinkVolver(forma);
	}
	

	/**
	 * 
	 * @param forma
	 */
	private void armarLinkVolver(ConsultaReferenciaContrareferenciaForm forma) 
	{
		String link="\"";
		if(forma.getEsBusquedaXpaciente())
		{	
			link="../consultarReferenciaContrareferenciaXPaciente/consultarReferenciaContrareferenciaXPaciente.do?estado=empezarXPaciente";
			//al empezar carga la informacion del paciente cargado no es necesario volverlo a indicar en el url
		}
		else
		{
			link="../consultarReferenciaContrareferenciaXPeriodo/consultarReferenciaContrareferenciaXPeriodo.do?estado=resultadoBusquedaXPeriodo";
			link+="&codigoInstitucion="+forma.getCodigoInstitucion();
			link+="&codigoInstitucionOrigen="+forma.getCodigoInstitucionOrigen();
			link+="&fechaFinalizacionFinal="+forma.getFechaFinalizacionFinal();
			link+="&fechaFinalizacionInicial="+forma.getFechaFinalizacionInicial();
			link+="&rangoNroReferenciaFinal="+forma.getRangoNroReferenciaFinal();
			link+="&rangoNroReferenciaInicial="+forma.getRangoNroReferenciaInicial();
			link+="&rangoTipoContrareferencia="+forma.getTipoContrareferencia();
			link+="&rangoTipoReferenciaInterna="+forma.getTipoReferenciaInterna();
		}
		link+="\"";
		forma.setLinkVolver(link);
	}
	
	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mapping
	 * @return
	 */
	private ActionForward accionResultadoBusquedaXPeriodo(Connection con, ConsultaReferenciaContrareferenciaForm forma, ActionMapping mapping) 
	{
		ConsultaReferenciaContrareferencia mundo= new ConsultaReferenciaContrareferencia();
		this.llenarMundoXPeriodo(mundo, forma);
		forma.setMapaListado(ConsultaReferenciaContrareferencia.busquedaReferenciaContrareferencia(con, mundo));
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principalXPeriodo");
	}
	
    
}
