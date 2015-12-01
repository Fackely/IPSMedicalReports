/*
 * @(#)ConsultaLogCuposExtraAction.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_04
 *
 */

package com.princetonsa.action.agenda;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.Listado;
import util.UtilidadBD;
import util.consultaExterna.UtilidadesConsultaExterna;

import com.princetonsa.actionform.agenda.ConsultaLogCuposExtraForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.agenda.ConsultaLogCuposExtra;

/**
 * Clase encargada del control de la funcionalidad de Consulta de LOG de Cupos Extra

 * @author <a href="mailto:cperalta@PrincetonSA.com">Carlos Peralta</a>
 * @version 1.0, 09 /May/ 2006
 */
public class ConsultaLogCuposExtraAction extends Action
{
	/**
	 * Objeto para almacenar los logs que aparezcan en el Action 
	 */
	private Logger logger= Logger.getLogger(ConsultaLogCuposExtraAction.class);
	boolean esNuevo=false;

	/**
	 * Mï¿½todo excute del Action
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)throws Exception
	{
		 Connection con = null;
		 try {
		if(form instanceof ConsultaLogCuposExtraForm)
	    {
		    
		    /**Intentamos abrir una conexion con la fuente de datos**/ 
			con = openDBConnection(con); 
			if(con == null)
			{
				request.setAttribute("codigoDescripcionError", "errors.problemasBd");
				return mapping.findForward("paginaError");
			}
			
			UsuarioBasico usuario=(UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
			
			ConsultaLogCuposExtra mundo =new ConsultaLogCuposExtra();
			ConsultaLogCuposExtraForm forma=(ConsultaLogCuposExtraForm)form;
			
			String estado = forma.getEstado();
			logger.warn("[ConsultaLogCuposExtraAction] estado->"+estado);
			
			if(estado == null)
			{
				logger.warn("Estado no valido dentro del flujo de ConsultaLogCuposExtraAction (null) ");
				request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
				UtilidadBD.closeConnection(con);
				return mapping.findForward("paginaError");
			}
			
			else if(estado.equals("empezar"))
			{
				forma.reset();
				forma.setCentroAtencion(usuario.getCodigoCentroAtencion());
				
				// Obtener centros de atención validos para el usuario
				forma.setCentrosAtencionAutorizados(UtilidadesConsultaExterna.centrosAtencionValidosXUsuario(con, usuario.getLoginUsuario(), ConstantesBD.codigoActividadAutorizadaCuposExtra));
				forma.setUnidadesAgendaAutorizadas(UtilidadesConsultaExterna.unidadesAgendaXUsuario(con, usuario.getLoginUsuario(), usuario.getCodigoCentroAtencion(), ConstantesBD.codigoActividadAutorizadaCuposExtra,ConstantesIntegridadDominio.acronimoTipoAtencionGeneral));
				
				UtilidadBD.closeConnection(con);
				return mapping.findForward("inicioBusqueda");
			}
			else if(estado.equals("resultadoBusqueda"))
			{
				return this.accionBusquedaAvanzada(forma, mapping, con, mundo);
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
			else if(estado.equals("cambiarCentroAtencion"))
			{
				forma.setUnidadesAgendaAutorizadas(UtilidadesConsultaExterna.unidadesAgendaXUsuario(con, usuario.getLoginUsuario(), forma.getCentroAtencion(), ConstantesBD.codigoActividadAutorizadaCuposExtra,ConstantesIntegridadDominio.acronimoTipoAtencionGeneral));
				UtilidadBD.closeConnection(con);
				return mapping.findForward("inicioBusqueda");
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
	 * Action para realizar la busqueda avanzada de los LOGS de los Cupos Extras
	 * @param forma
	 * @param mapping
	 * @param con
	 * @param mundo
	 * @return
	 * @throws SQLException
	 */
	private ActionForward accionBusquedaAvanzada (ConsultaLogCuposExtraForm forma, ActionMapping mapping,  Connection con, ConsultaLogCuposExtra mundo) throws SQLException
	{
		organizarInfo(forma);
		
		logger.info("****************************************");
		logger.info("centro = "+forma.getCentroAtencion());
		logger.info("centros ="+forma.getCentrosAtencion());
		logger.info("unidad = "+forma.getCodigoUnidadConsulta());
		logger.info("unidades ="+forma.getUnidadesAgenda());
		logger.info("****************************************\n");
		
		forma.setMapaLogCuposExtras(mundo.consultarLogCuposExtra(con, forma.getFechaInicial(), forma.getFechaFinal(), forma.getCodigoMedico(), forma.getCodigoUnidadConsulta(), forma.getCentroAtencion(), forma.getCentrosAtencion(), forma.getUnidadesAgenda()));
		UtilidadBD.closeConnection(con);
		return mapping.findForward("resultadoBusqueda");		
		
	}
	
	private void organizarInfo(ConsultaLogCuposExtraForm forma) {
		
		// Capturar los centros de atencion si se ha seleccionado la opcion todos
		if (forma.getCentroAtencion()==ConstantesBD.codigoNuncaValido){
			forma.setCentrosAtencion(forma.getCentrosAtencionAutorizados("todos").toString());
		}
		
		// Capturar las unidades de agenda si se ha seleccionado la opcion todos
		if (forma.getCodigoUnidadConsulta()==ConstantesBD.codigoNuncaValido){
			forma.setUnidadesAgenda(forma.getUnidadesAgendaAutorizadas("todos").toString());
		}
	}

	/**
	 * Action para ordenar por cualquiera de las columnas del mapa
	 * @param con
	 * @param forma
	 * @param response
	 * @return
	 */
	private ActionForward accionOrdenarColumna(Connection con, ConsultaLogCuposExtraForm forma, HttpServletResponse response) 
    {
        String[] indices={
				            "fecha_", 
				            "hora_", 
				            "usuario_", 
							"fechacita_",
				            "horacita_",
				            "unidadconsulta_",
							"profesional_",
							"cuposdisponibles_",
							"cuposextra_"
	            		};
        
        int tmp=Integer.parseInt(forma.getMapaLogCuposExtras("numRegistros")+"");
        forma.setMapaLogCuposExtras(Listado.ordenarMapa(indices,forma.getPatronOrdenar(),forma.getUltimoPatron(),forma.getMapaLogCuposExtras(),Integer.parseInt(forma.getMapaLogCuposExtras("numRegistros")+"")));
        forma.setUltimoPatron(forma.getPatronOrdenar());
        forma.setMapaLogCuposExtras("numRegistros",tmp+"");
        UtilidadBD.closeConnection(con);
		/**return mapping.findForward("busquedaAvanzada");**/
        return this.redireccionColumna(con,forma, response,"resultadoBusqueda.jsp");
    }
	
	/**
	 * Mï¿½todo para que al ordenar se quede posicionado en la pï¿½gina del 
     * pager en la cual se encontraba
	 * @param con
	 * @param forma
	 * @param response
	 * @param enlace
	 * @return
	 */
    public ActionForward redireccionColumna(Connection con, ConsultaLogCuposExtraForm forma, HttpServletResponse response, String enlace)
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