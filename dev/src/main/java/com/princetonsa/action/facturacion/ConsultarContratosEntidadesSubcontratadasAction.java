package com.princetonsa.action.facturacion;
import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.Utilidades;

import com.princetonsa.actionform.facturacion.ConsultarContratosEntidadesSubcontratadasForm;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.facturacion.ConsultarContratosEntidadesSubcontratadas;

/**
 * Clase para el manejo de la parametrizacion 
 * de los soportes de facturas
 * Date: 2009-01-26
 * @author jfhernandez@princetonsa.com
 */
public class ConsultarContratosEntidadesSubcontratadasAction extends Action 
{
	/**
	 * logger 
	 * */
	Logger logger = Logger.getLogger(SoportesFacturasAction.class);
	
	/**
	 * Metodo excute del Action
	 */
    @SuppressWarnings("unused")
	public ActionForward execute(   ActionMapping mapping,
            						ActionForm form,
            						HttpServletRequest request,
            						HttpServletResponse response ) throws Exception
    {
    	Connection con = null;
    	try{
    	if(response==null);
    	if(form instanceof ConsultarContratosEntidadesSubcontratadasForm)
    	{
    		
    		con = UtilidadBD.abrirConexion();
    		
    		if(con == null)
    		{	
    			request.setAttribute("CodigoDescripcionError","errors.problemasBd");
    			return mapping.findForward("paginaError");
    		}
    		
    		ConsultarContratosEntidadesSubcontratadasForm forma = (ConsultarContratosEntidadesSubcontratadasForm)form;
    		String estado = forma.getEstado();
    		
    		ActionErrors errores = new ActionErrors();
    	
    		logger.info("\n\n\n ESTADO ---> "+estado+"\n\n");
    		
    		UsuarioBasico usuario = (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
    		
    		if(estado == null)
    		{
    			forma.reset();
    			logger.warn("Estado No Valido Dentro Del Flujo de la Parametrización de Consultar Entidades Subcontratadas (null)");
    			request.setAttribute("CodigoDescripcionError","Errors.estadoInvalido");
    			UtilidadBD.cerrarConexion(con);
    			return mapping.findForward("paginaError");
    		}	
    		
    		if(estado.equals("empezar"))
    		{
    			return accionEmpezar(con, forma, usuario, request, mapping);
    		}
    		
    		if(estado.equals("buscarContratos"))
    		{
    			return accionBuscarContratos(con, forma, usuario, request, mapping);
    		}
    		
    		if(estado.equals("mostrarDetalle"))
    		{
    			return accionMostrarDetalle(con, forma, usuario, request, mapping);
    		}
    		if(estado.equals("mostrarFiltroTarifa")){
    			if(forma.getFiltros("tipoTarifa").toString().equals(ConstantesIntegridadDominio.acronimoTipoTarifaPropia))
    				forma.setMostrarFiltroTarifa(ConstantesBD.acronimoSi);
    			else
    				forma.setMostrarFiltroTarifa(ConstantesBD.acronimoNo);
    		
				return mapping.findForward("principal");
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
	 * @param con
	 * @param forma
	 * @param usuario
	 * @param request
	 * @param mapping
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private ActionForward accionEmpezar(Connection con, ConsultarContratosEntidadesSubcontratadasForm forma, UsuarioBasico usuario, HttpServletRequest request, ActionMapping mapping) {
		forma.reset();
		forma.setEntidades(ConsultarContratosEntidadesSubcontratadas.obtenerEntidades(con));
		forma.setInventarios(ConsultarContratosEntidadesSubcontratadas.obtenerClaseInventarios(con));
		forma.setEsquemas(ConsultarContratosEntidadesSubcontratadas.obtenerEsquemas(con));
		forma.setGrupoServicio(ConsultarContratosEntidadesSubcontratadas.obtenerGruposServicio(con));
		forma.setEsquemasProcedimientos(ConsultarContratosEntidadesSubcontratadas.obtenerEsquemasProcedimientos(con));
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}
	
    /**
	 * @param con
	 * @param forma
	 * @param usuario
	 * @param request
	 * @param mapping
	 * @return
	 */
	private ActionForward accionBuscarContratos(Connection con, ConsultarContratosEntidadesSubcontratadasForm forma, UsuarioBasico usuario, HttpServletRequest request, ActionMapping mapping) 
	{
		Utilidades.imprimirMapa(forma.getFiltros());
		forma.setResultadosBusqueda("encabezado",ConsultarContratosEntidadesSubcontratadas.consultaContratos(con,forma.getFiltros(),forma.getInfoEntidadInv(),forma.getInfoEntidadServ()));
		forma.setResultadosBusqueda("tarifasinventarios",ConsultarContratosEntidadesSubcontratadas.consultarEsquemasInventarios(con));
		forma.setResultadosBusqueda("tarifasprocedimientos",ConsultarContratosEntidadesSubcontratadas.consultarEsquemasServicios(con));
		UtilidadBD.closeConnection(con);
		return mapping.findForward("resultadoBusqueda");
	}
	
	/**
	 * @param con
	 * @param forma
	 * @param usuario
	 * @param request
	 * @param mapping
	 * @return
	 */
	private ActionForward accionMostrarDetalle(Connection con, ConsultarContratosEntidadesSubcontratadasForm forma, UsuarioBasico usuario, HttpServletRequest request, ActionMapping mapping) 
	{
		UtilidadBD.closeConnection(con);
		return mapping.findForward("detalle");
	}
}