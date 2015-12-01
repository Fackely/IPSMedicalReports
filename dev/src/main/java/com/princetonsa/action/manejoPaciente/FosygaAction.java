/*
 * @(#)FosygaAction.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK jdk1.5.0_07
 *
 */
package com.princetonsa.action.manejoPaciente;

import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.Listado;
import util.UtilidadBD;

import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.manejoPaciente.FosygaForm;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.manejoPaciente.Fosyga;

/**
 * Clase para el manejo del workflow de la funcionalidad
 * @author Wilson Rios
 * wrios@princetonsa.com
 */
public class FosygaAction extends Action  
{
	/**
     * Objeto para manejar los logs de esta clase
    */
    private Logger logger = Logger.getLogger(FosygaAction.class);
    
    /**
     * 
     */
    private static String[] indices={"tipoevento_", "consecutivo_", "cuenta_", "ingreso_", "paciente_", "fechahoraingresoformatobd_", "fechahoraingreso_", "fechahoraegreso_", "centroatencion_", "codigovia_", "via_","estado_" };
    
    /**
	 * Metodo excute del Action
	 */
    public ActionForward execute(   ActionMapping mapping,
            						ActionForm form,
            						HttpServletRequest request,
            						HttpServletResponse response ) throws Exception
            						{
    	Connection con=null;
    	try{
    		if (response==null); 
    		if(form instanceof FosygaForm)
    		{

    			con = UtilidadBD.abrirConexion();
    			if(con == null)
    			{
    				request.setAttribute("codigoDescripcionError", "errors.problemasBd");
    				return mapping.findForward("paginaError");
    			}

    			UsuarioBasico usuario= (UsuarioBasico)request.getSession().getAttribute("usuarioBasico"); 
    			PersonaBasica paciente= (PersonaBasica)request.getSession().getAttribute("pacienteActivo");
    			FosygaForm forma =(FosygaForm)form;
    			String estado=forma.getEstado();
    			logger.warn("\n\n\nEl estado en Fosyga es------->"+estado+"\n");

    			if(estado == null)
    			{
    				forma.reset(); 
    				logger.warn("Estado no valido dentro del flujo de Fosyga (null) ");
    				request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
    				UtilidadBD.cerrarConexion(con);
    				return mapping.findForward("paginaError");
    			}
    			else if(estado.equals("empezarConsultaRangos"))
    			{
    				//Como es por Consultar Rangos lo llenamos en True
    				forma.setPorConsultarImprimir(true);
    				logger.info("===>Por Consultar Imprimir: "+forma.isPorConsultarImprimir());
    				return this.accionEmpezarConsultaRangos(forma, mapping, con, usuario);
    			}
    			else if(estado.equals("empezarConsultaPaciente"))
    			{
    				//Como es por Consultar Paciente lo llenamos en True
    				forma.setPorConsultarImprimir(true);
    				logger.info("===>Por Consultar Imprimir: "+forma.isPorConsultarImprimir());
    				return this.accionEmpezarConsultaPaciente(forma, mapping, con, usuario, paciente, request);
    			}
    			else if(estado.equals("empezarConsultaPacienteDummy"))
    			{
    				//Como es por Modificar Amparos y Reclamaciones lo llenamos en False
    				forma.setPorConsultarImprimir(false);
    				logger.info("===>Por Consultar Imprimir: "+forma.isPorConsultarImprimir());
    				return this.accionEmpezarConsultaPaciente(forma, mapping, con, usuario, paciente, request);
    			}
    			else if(estado.equals("busquedaAvanzadaXPeriodo"))
    			{
    				return accionBusquedaAvanzada(forma, mapping, con, usuario);
    			}
    			else if(estado.equals("busquedaAvanzadaXPaciente"))
    			{
    				return accionBusquedaAvanzada(forma, mapping, con, usuario);
    			}
    			else if(estado.equals("mostrarErroresXPeriodo"))
    			{
    				UtilidadBD.closeConnection(con);
    				return mapping.findForward("busquedaAvanzada");
    			}
    			else if(estado.equals("ordenar"))
    			{
    				this.accionOrdenarMapa(forma);
    				UtilidadBD.closeConnection(con);
    				return mapping.findForward("busquedaAvanzada");
    			}
    			else
    			{
    				forma.reset(); 
    				logger.warn("Estado no valido dentro del flujo de Fosyga -> "+estado);
    				request.setAttribute("codigoDescripcionError", "errors.formaTipoInvalido");
    				UtilidadBD.cerrarConexion(con);
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
     * 
     * @param forma
     * @param mapping
     * @param con
     * @param usuario
     * @return
     */
	private ActionForward accionEmpezarConsultaRangos(FosygaForm forma, ActionMapping mapping, Connection con, UsuarioBasico usuario) 
	{
		forma.reset();
		forma.setCriteriosBusquedaMap("esBusquedaAvanzadaXPeriodo", ConstantesBD.acronimoSi);
		UtilidadBD.closeConnection(con);
		return mapping.findForward("busquedaAvanzada");
	}
	
	/**
	 * 
	 * @param forma
	 * @param mapping
	 * @param con
	 * @param usuario
	 * @return
	 */
	private ActionForward accionEmpezarConsultaPaciente(FosygaForm forma, ActionMapping mapping, Connection con, UsuarioBasico usuario, PersonaBasica paciente, HttpServletRequest request) 
	{
		forma.reset();
		ActionForward forwardPaginaErrores=accionValidacionesAcceso(paciente, mapping, request, con);
		if(forwardPaginaErrores!=null)
			return forwardPaginaErrores;
		forma.setCriteriosBusquedaMap("codigoPersona", paciente.getCodigoPersona()+"");
		forma.setEstado("busquedaAvanzadaXPaciente");
		return accionBusquedaAvanzada(forma, mapping, con, usuario);
	}
	
	
	/**
	 * 
	 * @param forma
	 * @param mapping
	 * @param con
	 * @return
	 */
	private ActionForward accionBusquedaAvanzada(FosygaForm forma, ActionMapping mapping, Connection con, UsuarioBasico usuario) 
	{
		forma.getCriteriosBusquedaMap().put("institucion", usuario.getCodigoInstitucion());
		forma.setListadoMap(Fosyga.busquedaAvanzada(con, forma.getCriteriosBusquedaMap()));
		armarLinkVolver(forma);
		UtilidadBD.closeConnection(con);
		return mapping.findForward("busquedaAvanzada");
	}

	/**
	 * 
	 * @param forma
	 */
	private void armarLinkVolver(FosygaForm forma) 
	{
		forma.setLinkVolver("../consultarFosyga/consultarFosyga.do?estado="+forma.getEstado());
	}


	/**
	 * validaciones de acceso
	 * @param paciente
	 * @param mapping
	 * @param request
	 * @param con
	 * @return
	 */
	protected ActionForward accionValidacionesAcceso(	PersonaBasica paciente, 
														ActionMapping mapping, 
														HttpServletRequest request, 
														Connection con)
	{
		if(paciente.getCodigoPersona()<1)
		{
		    return ComunAction.accionSalirCasoError(mapping, request, con, logger, "paciente null o sin  id", "errors.paciente.noCargado", true);
		}
		return null;
	}	
    
	/**
	 * 
	 * @param forma
	 */
	private void accionOrdenarMapa(FosygaForm forma) 
	{
		int numReg=Integer.parseInt(forma.getListadoMap("numRegistros")+"");
		forma.setListadoMap(Listado.ordenarMapa(indices,forma.getPatronOrdenar(),forma.getUltimoPatron(),forma.getListadoMap(),numReg));
		forma.setUltimoPatron(forma.getPatronOrdenar());
		forma.setListadoMap("numRegistros",numReg+"");	
	}
    
}
