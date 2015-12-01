/*
 * @(#)ConsultaGruposEtareosAction.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_04
 *
 */

package com.princetonsa.action.capitacion;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.axioma.util.log.Log4JManager;

import util.Listado;
import util.UtilidadBD;

import com.princetonsa.actionform.capitacion.ConsultaGruposEtareosForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.capitacion.ConsultaGruposEtareos;

/**
 * Clase encargada del control de la funcionalidad de Consulta de LOG de Cupos Extra

 * @author <a href="mailto:cperalta@PrincetonSA.com">Carlos Peralta</a>
 * @version 1.0, 09 /May/ 2006
 */
public class ConsultaGruposEtareosAction extends Action
{
	/**
	 * Objeto para almacenar los logs que aparezcan en el Action 
	 */
	private Logger logger= Logger.getLogger(ConsultaGruposEtareosAction.class);

	/**
	 * Método excute del Action
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)throws Exception
	{
		 Connection con = null;
		 try{
		if(form instanceof ConsultaGruposEtareosForm)
	    {
	       
		    
		    /**Intentamos abrir una conexion con la fuente de datos**/ 
			con = openDBConnection(con); 
			if(con == null)
			{
				request.setAttribute("codigoDescripcionError", "errors.problemasBd");
				return mapping.findForward("paginaError");
			}
			HttpSession session = request.getSession();
			UsuarioBasico usuario= (UsuarioBasico)session.getAttribute("usuarioBasico");
			ConsultaGruposEtareos mundo =new ConsultaGruposEtareos();
			ConsultaGruposEtareosForm forma=(ConsultaGruposEtareosForm)form;
			
			String estado = forma.getEstado();
			logger.warn("[ConsultaGruposEtareosAction] estado->"+estado);
			
			if(estado == null)
			{
				logger.warn("Estado no valido dentro del flujo de ConsultaGruposEtareosAction (null) ");
				request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
				UtilidadBD.closeConnection(con);
				return mapping.findForward("paginaError");
			}
			
			else if(estado.equals("empezar"))
			{
				forma.reset();
				UtilidadBD.closeConnection(con);
				return mapping.findForward("inicioBusqueda");
			}
			else if(estado.equals("buscar"))
			{
				return this.accionBusquedaAvanzada(forma, mapping, con, mundo, usuario);
			}
			else if (estado.equals("redireccion"))
			{
				UtilidadBD.closeConnection(con);
				response.sendRedirect(forma.getLinkSiguiente());
				return null;
			}
			else if(estado.equals("ordenarColumna"))
			{
				return this.accionOrdenarColumna(con, forma, response);
			}
	    }
		else
		{
			logger.error("El form no es compatible con el form de Formato de Impresion de Factura");
			request.setAttribute("codigoDescripcionError", "errors.formaTipoInvalido");
			return mapping.findForward("paginaError");
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
	 * Action para realizar la busqueda avanzada de los grupos etareos
	 * @param forma
	 * @param mapping
	 * @param con
	 * @param mundo
	 * @param usuario
	 * @return
	 * @throws SQLException
	 */
	private ActionForward accionBusquedaAvanzada (ConsultaGruposEtareosForm forma, ActionMapping mapping,  Connection con, ConsultaGruposEtareos mundo, UsuarioBasico usuario) throws SQLException
	{
		forma.setMapaGruposEtareos(mundo.consultarGruposEtareos(con, forma.getFechaInicial(), forma.getFechaFinal(), forma.getCodigoConvenio(), usuario.getCodigoInstitucionInt()));
		UtilidadBD.closeConnection(con);
		return mapping.findForward("paginaPrincipal");		
		
	}
	
	/**
	 * Action para ordenar por cualquiera de la columnas del mapa
	 * @param con
	 * @param forma
	 * @param response
	 * @return
	 */
	private ActionForward accionOrdenarColumna(Connection con, ConsultaGruposEtareosForm forma, HttpServletResponse response) 
    {
        String[] indices={
				            "codigogrupoetareo_", 
				            "codigoconvenio_", 
				            "institucion_", 
							"edadfinal_",
				            "edadinicial_",
				            "fechainicial_",
							"fechafinal_",
							"codigosexo_",
							"nombresexo_",
							"valor_",
							"pyp_"
	            		};
        
        int tmp=Integer.parseInt(forma.getMapaGruposEtareos("numRegistros")+"");
        forma.setMapaGruposEtareos(Listado.ordenarMapa(indices,forma.getPatronOrdenar(),forma.getUltimoPatron(),forma.getMapaGruposEtareos(),Integer.parseInt(forma.getMapaGruposEtareos("numRegistros")+"")));
        forma.setUltimoPatron(forma.getPatronOrdenar());
        forma.setMapaGruposEtareos("numRegistros",tmp+"");
        UtilidadBD.closeConnection(con);
        return this.redireccionColumna(con, forma, response,"resultadoBusqueda.jsp");
    }
	
	/**
	 * Método para que al ordenar se quede posicionado en la página del 
     * pager en la cual se encontraba
	 * @param con
	 * @param forma
	 * @param response
	 * @param enlace
	 * @return
	 */
    public ActionForward redireccionColumna(Connection con, ConsultaGruposEtareosForm forma, HttpServletResponse response, String enlace)
    {
            try 
            {
                UtilidadBD.closeConnection(con);
                response.sendRedirect(enlace+"?pager.offset="+forma.getOffset());
            }
            catch (IOException e)
			{
            	e.printStackTrace();
			}
            
         UtilidadBD.closeConnection(con);
         return null;
    }
	
	/**
	 * Abrir la conceccion con la Base de Datos
	 * @param con
	 * @return
	 */
	public Connection openDBConnection(Connection con)
	{
		if(con != null)
		{
			return con;
		}
		try
		{
			String tipoBD = System.getProperty("TIPOBD");
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			con = myFactory.getConnection();
		}
		catch(Exception e)
		{
			logger.warn("Problemas con la base de datos al abrir la conexion "+e.toString());
			return null;
		}
	
		return con;
	}
}