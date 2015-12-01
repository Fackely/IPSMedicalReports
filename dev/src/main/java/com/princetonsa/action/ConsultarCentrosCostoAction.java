/*
 * @(#)ConsultaLogCuposExtraAction.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_04
 *
 */

package com.princetonsa.action;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Vector;

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
import util.UtilidadBD;

import com.princetonsa.actionform.ConsultarCentrosCostoForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.ConsultarCentrosCosto;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.util.birt.reports.DesignEngineApi;
import com.princetonsa.util.birt.reports.ParamsBirtApplication;

/**
 * Clase encargada del control de la funcionalidad de Consulta de Centros de Costo

 * @author <a href="mailto:cperalta@PrincetonSA.com">Carlos Peralta</a>
 * @version 1.0, 11 /May/ 2006
 */
public class ConsultarCentrosCostoAction extends Action
{
	/**
	 * Objeto para almacenar los logs que aparezcan en el Action 
	 */
	private Logger logger= Logger.getLogger(ConsultarCentrosCostoAction.class);
	boolean esNuevo=false;

	/**
	 * Método excute del Action
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)throws Exception
	{
		Connection con = null;
		try{
			if(form instanceof ConsultarCentrosCostoForm)
			{


				/**Intentamos abrir una conexion con la fuente de datos**/ 
				con = openDBConnection(con); 
				if(con == null)
				{
					request.setAttribute("codigoDescripcionError", "errors.problemasBd");
					return mapping.findForward("paginaError");
				}

				ConsultarCentrosCosto mundo =new ConsultarCentrosCosto();
				ConsultarCentrosCostoForm forma=(ConsultarCentrosCostoForm)form;
				HttpSession session=request.getSession();		
				UsuarioBasico usuario = (UsuarioBasico)session.getAttribute("usuarioBasico");

				String estado = forma.getEstado();
				logger.warn("[ConsultarCentrosCostoAction] estado->"+estado);

				if(estado == null)
				{
					logger.warn("Estado no valido dentro del flujo de ConsultarCentrosCostoAction (null) ");
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
				else if(estado.equals("imprimir"))
				{
					this.generarReporte(con, forma, usuario, request);                
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("resultadoBusqueda");
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
	
	
	private ActionForward accionBusquedaAvanzada (ConsultarCentrosCostoForm forma, ActionMapping mapping,  Connection con, ConsultarCentrosCosto mundo) throws SQLException
	{
		forma.setMapaCentrosCosto(mundo.consultarCentrosCosto(con, forma.getCodCentroAtencion(), forma.getIdentificador(), forma.getDescripcion(), forma.getCodigoTipoArea(), forma.getManejoCamas(), forma.getAcronimoUnidadFuncional(), forma.getCodigo_interfaz(), forma.getActivo(),forma.getTipoEntidad()));
		UtilidadBD.closeConnection(con);
		return mapping.findForward("resultadoBusqueda");		
		
	}
	
	/**
	 * Accion para ordenar por columnas el resultado de la consulta
	 * @param con
	 * @param forma
	 * @param response
	 * @return
	 */
	private ActionForward accionOrdenarColumna(Connection con, ConsultarCentrosCostoForm forma, HttpServletResponse response) 
    {
        String[] indices={
				            "codigocentroatencion_", 
				            "nombrecentroatencion_", 
				            "codigocentrocosto_", 
							"identificador_",
				            "descripcion_",
				            "codigotipoarea_",
							"nombretipoarea_",
							"manejocamas_",
							"unidadfuncional_",
							"nombreunidadfuncional_",
							"codigo_interfaz_",
							"activo_"
	            		};
        
        int tmp=Integer.parseInt(forma.getMapaCentrosCosto("numRegistros")+"");
        forma.setMapaCentrosCosto(Listado.ordenarMapa(indices,forma.getPatronOrdenar(),forma.getUltimoPatron(),forma.getMapaCentrosCosto(),Integer.parseInt(forma.getMapaCentrosCosto("numRegistros")+"")));
        forma.setUltimoPatron(forma.getPatronOrdenar());
        forma.setMapaCentrosCosto("numRegistros",tmp+"");
        UtilidadBD.closeConnection(con);
        return this.redireccionColumna(con, forma, response,"resultadoBusqueda.jsp");
    }
	
	
	/**
	 * Accion para redireccionar a la pagina del pager en la que se encontraba al momento de realizar 
	 * la organizacion por columnas
	 * @param con
	 * @param forma
	 * @param response
	 * @param enlace
	 * @return
	 */
	public ActionForward redireccionColumna(Connection con, ConsultarCentrosCostoForm forma, HttpServletResponse response, String enlace)
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
	 * Método para generar el reporte de la consulta de los centros de costo
	 * @param con
	 * @param forma
	 * @param usuario
	 * @param request
	 */
	private void generarReporte(Connection con, ConsultarCentrosCostoForm forma, UsuarioBasico usuario, HttpServletRequest request) 
    {
			DesignEngineApi comp;
			InstitucionBasica institucionBasica = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
            comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"manejoPaciente/","CentrosCosto.rptdesign");
            comp.insertImageHeaderOfMasterPage1(0, 0, institucionBasica.getLogoReportes());
            comp.insertGridHeaderOfMasterPage(0,1,1,4);
            
            String parametros ="";
            Vector v=new Vector();
            v.add(institucionBasica.getRazonSocial());
            v.add(institucionBasica.getTipoIdentificacion()+"         "+institucionBasica.getNit());
            
            //Si no se posee actividad economica no mostrar el campo
            if(!institucionBasica.getActividadEconomica().equals(""))
            {
                v.add("Actividad Económica: "+institucionBasica.getActividadEconomica());
            }
            v.add(institucionBasica.getDireccion()+"          "+institucionBasica.getTelefono());
            comp.insertLabelInGridOfMasterPage(0,1,v);
            
            if(forma.getCodCentroAtencion()!=ConstantesBD.codigoNuncaValido)
            	parametros+="Centro Atencion:"+forma.getDescripcionCentroAtencion();
            
            if(!forma.getIdentificador().equals(""))
            	parametros+="  Codigo:"+forma.getIdentificador();
            
            if(!forma.getDescripcion().equals(""))
            	parametros+="  Descripcion:"+forma.getDescripcion();
            
            if(forma.getCodigoTipoArea()!=ConstantesBD.codigoNuncaValido)
            	parametros+="  Tipo Area:"+forma.getDescripcionArea().toLowerCase();
            
            if(!forma.getManejoCamas().equals(""))
            	parametros+="  Manejo de Camas: Si";
                        
            if(!forma.getAcronimoUnidadFuncional().trim().equals("-1"))
            	parametros+="  Unidad Funcional:"+forma.getDescripcionUnidadFuncional();
            
            if(!forma.getActivo().equals(""))
            	parametros+="  Activo: Si";
            
            
            comp.insertLabelInGridPpalOfHeader(2, 0, parametros);
          
            comp.obtenerComponentesDataSet("listadoCentrosCosto");            
            String oldQuery=comp.obtenerQueryDataSet();
            String newQuery=oldQuery+fragmentAndInReport(forma); 
            comp.modificarQueryDataSet(newQuery);
            //debemos arreglar los alias para que funcione oracle, debido a que este los convierte a MAYUSCULAS
      	comp.lowerAliasDataSet();
		String newPathReport = comp.saveReport1(false);
            if(!newPathReport.equals(""))
            {
                request.setAttribute("isOpenReport", "true");
                request.setAttribute("newPathReport", newPathReport);
            }            
            comp.updateJDBCParameters(newPathReport);
    }
	
	
	
	/**
	 * Metodo para editar la consulta para el reporte en birt
	 * @param forma
	 * @return
	 */
	private String fragmentAndInReport(ConsultarCentrosCostoForm forma)
	{
        String avanzadaStr = "";
		if(forma.getCodCentroAtencion() != -1)
		{
			avanzadaStr+=" AND cc.centro_atencion="+forma.getCodCentroAtencion();
		}
		if(!forma.getIdentificador().trim().equals(""))
		{
			avanzadaStr+=" AND UPPER(cc.identificador) LIKE UPPER('%"+forma.getIdentificador()+"%') ";
		}
		if(!forma.getDescripcion().trim().equals(""))
		{
			avanzadaStr+=" AND UPPER(cc.nombre) LIKE UPPER('%"+forma.getDescripcion()+"%') ";
		}
		if(forma.getCodigoTipoArea() != -1)
		{
			avanzadaStr+=" AND cc.tipo_area="+forma.getCodigoTipoArea();
		}
		if(!forma.getManejoCamas().trim().equals(""))
		{
			avanzadaStr+=" AND cc.manejo_camas="+forma.getManejoCamas();
		}
		if(!forma.getAcronimoUnidadFuncional().trim().equals("-1"))
		{
			avanzadaStr+=" AND cc.unidad_funcional="+forma.getAcronimoUnidadFuncional();
		}
		if(!forma.getActivo().trim().equals(""))
		{
			avanzadaStr+=" AND cc.es_activo="+forma.getActivo();
		}
		avanzadaStr+=" ORDER BY ca.descripcion asc, cc.nombre asc ";
        return avanzadaStr;
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