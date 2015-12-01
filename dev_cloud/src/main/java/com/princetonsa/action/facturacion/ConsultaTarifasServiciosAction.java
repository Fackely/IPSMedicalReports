/*
 * @(#)ConsultaTarifasServiciosAction.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_04
 *
 */

package com.princetonsa.action.facturacion;

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

import util.Listado;
import util.UtilidadBD;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.actionform.facturacion.ConsultaTarifasServiciosForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.facturacion.ConsultaTarifasServicios;

/**
 * Clase encargada del control de la funcionalidad de 
 * Consulta De Tarfias de Servicios

 * @author <a href="mailto:cperalta@PrincetonSA.com">Carlos Peralta</a>
 * @version 1.0, 27 /Mar/ 2006
 */
public class ConsultaTarifasServiciosAction extends Action 
{
	/**
	 * Objeto para almacenar los logs que aparezcan en el Action 
	 */
	private Logger logger= Logger.getLogger(ConsultaTarifasServiciosAction.class);

	/**
	 * Método excute del Action
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)throws Exception
	{
		Connection con = null;

		try{
			if(form instanceof ConsultaTarifasServiciosForm)
			{

				/**Intentamos abrir una conexion con la fuente de datos**/ 
				con = openDBConnection(con); 
				if(con == null)
				{
					request.setAttribute("codigoDescripcionError", "errors.problemasBd");
					return mapping.findForward("paginaError");
				}

				ConsultaTarifasServiciosForm forma=(ConsultaTarifasServiciosForm)form;
				ConsultaTarifasServicios mundo =new ConsultaTarifasServicios();
				String estado = forma.getEstado();
				logger.warn("[ConsultaTarifasServiciosAction] estado->"+estado);
				UsuarioBasico usuario=Utilidades.getUsuarioBasicoSesion(request.getSession());

				if(estado == null)
				{
					logger.warn("Estado no valido dentro del flujo de ConsultaTarifasServiciosAction (null) ");
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.closeConnection(con);
					return mapping.findForward("paginaError");
				}
				else if(estado.equals("empezar"))
				{
					forma.reset(usuario.getCodigoInstitucionInt());
					forma.setTipoTarifario(ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(usuario.getCodigoInstitucionInt()));
					UtilidadBD.closeConnection(con);
					return mapping.findForward("inicioBusqueda");
				}
				else if(estado.equals("resultadoBusqueda"))
				{
					return this.accionBusquedaAvanzada(forma, mundo, mapping, con);
				}
				else if (estado.equals("redireccionBusqueda"))
				{
					UtilidadBD.closeConnection(con);
					response.sendRedirect(forma.getLinkSiguiente());
					return null;
				}
				else if(estado.equals("ordenarColumnaBusqueda"))
				{
					return this.accionOrdenarColumnaBusqueda(con, forma, response);
				}
				else if(estado.equals("ordenarColumnaDetalle"))
				{
					return this.accionOrdenarColumnaDetalle(con, forma, mapping);
				}
				else if(estado.equals("detalleServicio"))
				{
					return this.accionDetalleTarifaServicios(forma, mundo, mapping, con);
				}

			}
			else
			{
				logger.error("El form no es compatible con el form de Consulta de Tarifas de Servicios");
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
 	 * Action que permite el ordenamiento por la columna que se desee en el resultado
 	 * de la busqueda de tarifas de servicios
 	 * @param con
 	 * @param forma
 	 * @param response
 	 * @return
 	 */
    private ActionForward accionOrdenarColumnaBusqueda(Connection con, ConsultaTarifasServiciosForm forma, HttpServletResponse response) 
    {
        String[] indices={
        					"codigoservicio_",
        					"codserviciocompuesto_",
        					"codigocupsservicio_",
				            "codigopropietario_", 
				            "descripcionservicio_", 
				            "codigoespecialidad_", 
				            "nombreespecialidad_",
				            "codigotiposervicio_",
							"nombretiposervicio_",
							"acronimonaturaleza_",
							"nombrenaturaleza_",
							"codigogruposervicio_",
							"nombregruposervicio_",
							"estadoservicio_"
	            		};
        int tmp=Integer.parseInt(forma.getMapaServicios("numRegistros")+"");
        forma.setMapaServicios(Listado.ordenarMapa(indices,forma.getPatronOrdenar(),forma.getUltimoPatron(),forma.getMapaServicios(),Integer.parseInt(forma.getMapaServicios("numRegistros").toString())));
        forma.setUltimoPatron(forma.getPatronOrdenar());
        forma.setMapaServicios("numRegistros", tmp+"");
        UtilidadBD.closeConnection(con);
        return this.redireccionBusqueda(con, forma, response, "resultadoBusqueda.jsp");
    }
	    
	    
    /**
     * Método para no perder la pagina del pager en la que se encontraba ubicado al momento
     * de realizar el ordenamiento
     * @param con
     * @param forma
     * @param response
     * @param enlace
     * @return
     */
    public ActionForward redireccionBusqueda (Connection con, ConsultaTarifasServiciosForm forma,HttpServletResponse response,String enlace)
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
	 * Action que permite el ordenamiento por cualquier columna en el detalle
	 * de una tarifa de servicio para un servicio dado
	 * @param con
	 * @param forma
	 * @param response
	 * @return
	 */
    private ActionForward accionOrdenarColumnaDetalle(Connection con, ConsultaTarifasServiciosForm forma, ActionMapping mapping) 
    {
        String[] indices={
				            "codigotipoliquidacion_", 
				            "codigoservicio_", 
				            "codigoesquematarifario_", 
							"esquematarifario_",
				            "codigotarifariooficial_",
				            "tipomanual_",
				            "valorbase_",
				            "tipoliquidacion_",
				            "valorliquidacion_",
				            "valortarifa_",
				            "valorconversiontarifa_",
				            "liquidar_asocios_"
	            		};
        int tmp=Integer.parseInt(forma.getMapaCuerpoDetalle("numRegistros")+"");
        forma.setMapaCuerpoDetalle(Listado.ordenarMapa(indices,forma.getPatronOrdenar(),forma.getUltimoPatron(),forma.getMapaCuerpoDetalle(),Integer.parseInt(forma.getMapaCuerpoDetalle("numRegistros")+"")));
        forma.setUltimoPatron(forma.getPatronOrdenar());
        forma.setMapaCuerpoDetalle("numRegistros",tmp+"");
        UtilidadBD.closeConnection(con);
        return mapping.findForward("detalleTarifa");  
    }
    
	    
    /**
     * Action para el detalle de una tarifa de servicios para un servicio dado.
     * Me lleva a ver el detalle completo de una tarifa
     * @param forma
     * @param mundo
     * @param mapping
     * @param con
     * @return
     * @throws SQLException
     */
	private ActionForward accionDetalleTarifaServicios(ConsultaTarifasServiciosForm forma, ConsultaTarifasServicios mundo, ActionMapping mapping, Connection con) throws SQLException
	{
		forma.setMapaEncabezadoDetalle(mundo.consultaEncabezadoDetalle(con, forma.getCodigoDetalle()));
		forma.setMapaCuerpoDetalle(mundo.consultaCuerpoDetalle(con, forma.getCodigoDetalle()));
		
		adicionarValorConversionMoneda(forma);
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("detalleTarifa");		
	}
		
	/**
	 * 
	 * @param forma
	 */	
	private void adicionarValorConversionMoneda(ConsultaTarifasServiciosForm forma) 
	{
		//obtenermos el factor de conversion
		boolean asigno=false;
		
		logger.info("forma.getManejaConversionMoneda()->"+forma.getManejaConversionMoneda()+" index->"+forma.getIndex());
		 if(forma.getManejaConversionMoneda() && forma.getIndex()>0)
		 {
	       	double factor= Double.parseDouble(forma.getTiposMonedaTagMap("factorconversion_"+forma.getIndex())+"");
	       	if(factor>0)
	       	{
	       		for(int w=0; w<Integer.parseInt(forma.getMapaCuerpoDetalle("numRegistros").toString()); w++)
	       		{
	       			logger.info("\n\nvalorTarifa->"+forma.getMapaCuerpoDetalle("valortarifa_"+w)+"\n\n");
	       			if(!UtilidadTexto.isEmpty(forma.getMapaCuerpoDetalle("valortarifa_"+w)+""))
	       			{
	       				forma.setMapaCuerpoDetalle("valorconversiontarifa_"+w, Double.parseDouble(forma.getMapaCuerpoDetalle("valortarifa_"+w)+"")/factor);
	       			}
	       			else
	       			{
	       				forma.setMapaCuerpoDetalle("valorconversiontarifa_"+w, 0);
	       			}
	       			asigno=true;
	       		}
	       	}
	     }
		 if(!asigno)
		 {
			 logger.info("no asigno");
			 for(int w=0; w<Integer.parseInt(forma.getMapaCuerpoDetalle("numRegistros").toString()); w++)
       		 {
       			forma.setMapaCuerpoDetalle("valorconversiontarifa_"+w, 0);
       			asigno=true;
       		 } 
		 }
	}


	/**
	 * Action para la busqueda avanzada de las tarifas de servicios segun los campos
	 * ingresados
	 * @param forma
	 * @param mundo
	 * @param mapping
	 * @param con
	 * @return
	 * @throws SQLException
	 */
	private ActionForward accionBusquedaAvanzada (ConsultaTarifasServiciosForm forma, ConsultaTarifasServicios mundo, ActionMapping mapping,  Connection con) throws SQLException
	{	
		forma.setMapaServicios(mundo.busquedaServicios(con, forma.getCodigoInterno(),forma.getTipoTarifario(),forma.getCodigoServicio(),forma.getDescripcionServicio(), forma.getCodigoEspecialidad(), forma.getAcronimoTipoServicio(), forma.getAcronimoNaturaleza(), forma.getCodigoGrupoServicio()));
		UtilidadBD.closeConnection(con);
		return mapping.findForward("busquedaAvanzada");		
	}
	    
	    
	/**
	 * Abrir la conceccion con la Base de Datos
	 * @param con
	 * @return
	 */
	public Connection openDBConnection(Connection con)
	{

		if(con != null)
			return con;
		
		try{
			String tipoBD = System.getProperty("TIPOBD");
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			con = myFactory.getConnection();
		}
		catch(Exception e)
		{
			logger.warn("Problemas con la base de datos al abrir la conexion");
			return null;
		}
	
		return con;
	}
}