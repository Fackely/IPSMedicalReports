
/*
 * Creado   25/01/2006
 *
 *Copyright Princeton S.A. &copy;&reg; 2004. Todos los Derechos Reservados.
 *
 * Lenguaje		:Java
 * Compilador	:J2SDK 1.5.0_06
 * author Joan Lopez
 */
package com.princetonsa.action.inventarios;

import java.sql.Connection;
import java.util.HashMap;
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

import util.ConstantesIntegridadDominio;
import util.Listado;
import util.UtilidadBD;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.actionform.inventarios.ConsultaImpresionTrasladosForm;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.inventarios.ConsultaImpresionTraslados;
import com.princetonsa.util.birt.reports.DesignEngineApi;
import com.princetonsa.util.birt.reports.ParamsBirtApplication;
/**
 * 
 * Clase que implementa los metodos para manejar el WorkFlows
 *
 * @version 1.0, 25/01/2006
 * @author <a href="mailto:joan@PrincetonSA.com">Joan L&oacute;pez</a>
 */
public class ConsultaImpresionTrasladosAction extends Action 
{
    /**
     * manejador de los logs de la clase
     */
    private Logger logger=Logger.getLogger(ConsultaImpresionTrasladosAction.class);
    /**
     * Método execute del action
     */
    public ActionForward execute(   ActionMapping mapping,  
                                                            ActionForm form, 
                                                            HttpServletRequest request, 
                                                            HttpServletResponse response) throws Exception
                                                            {

    	Connection con = null;
    	try{

    		if(form instanceof ConsultaImpresionTrasladosForm)
    		{
    			ConsultaImpresionTrasladosForm forma=(ConsultaImpresionTrasladosForm)form;
    			ConsultaImpresionTraslados mundo = new ConsultaImpresionTraslados();
    			HttpSession sesion = request.getSession();          
    			UsuarioBasico usuario = null;
    			usuario = Utilidades.getUsuarioBasicoSesion(sesion);
    			String estado=forma.getEstado();

    			//se obtiene la institucion
    			InstitucionBasica institucion = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");

    			logger.warn("[consultaImpresionTrasladoAction] --> "+estado);


    			con = UtilidadBD.abrirConexion();
    			if(con == null)
    			{
    				request.setAttribute("codigoDescripcionError", "errors.problemasBd");
    				UtilidadBD.cerrarConexion(con);
    				return mapping.findForward("paginaError");
    			}           

    			if(estado == null)
    			{
    				logger.warn("Estado no valido dentro del flujo de DespachoTrasladoAlmacenAction (null) ");
    				request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
    				UtilidadBD.cerrarConexion(con);
    				return mapping.findForward("paginaError");
    			}

    			if(estado.equals("empezar"))
    			{
    				forma.reset();
    				forma.resetListados();

    				mundo.initCriterios(con, usuario, forma);
    				UtilidadBD.cerrarConexion(con);
    				return mapping.findForward("paginaBusqueda");
    			}

    			if(estado.equals("generarBusqueda"))
    			{
    				forma.resetListados();             
    				this.accionGenerarBusquedaAvanzada(con,forma,mundo,usuario);

    				UtilidadBD.cerrarConexion(con);
    				return mapping.findForward("paginaBusqueda");
    			}

    			if(estado.equals("ordenarListadoTraslados"))
    			{             
    				this.accionOrdenarMapaSolicitudes(forma);
    				UtilidadBD.cerrarConexion(con);
    				return mapping.findForward("paginaBusqueda");
    			}

    			if(estado.equals("cargarTraslado"))
    			{ 
    				this.accionGenerarConsultaDetalleTraslado(con,forma,mundo);
    				UtilidadBD.cerrarConexion(con);
    				return mapping.findForward("paginaDetalle");
    			}

    			if(estado.equals("generarReporteDetalle"))
    			{     

    				InstitucionBasica ins = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");

    				DesignEngineApi comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"inventarios/","DespachoTrasladoAlmacen.rptdesign");
    				comp.insertImageHeaderOfMasterPage1(0, 0, ins.getLogoReportes());
    				comp.insertGridHeaderOfMasterPage(0,1,1,4);
    				Vector v=new Vector();
    				v.add(ins.getRazonSocial());
    				v.add(ins.getTipoIdentificacion()+"     "+ins.getNit());  
    				if(!ins.getActividadEconomica().equals(""))//si no se posee actividad economica no mostrar el campo
    					v.add("Actividad Económica: "+ins.getActividadEconomica());
    				v.add(ins.getDireccion()+"     "+ins.getTelefono());
    				comp.insertLabelInGridOfMasterPage(0,1,v);
    				comp.insertLabelInGridPpalOfHeader(1,0,ins.getEncabezadoFactura());


    				//83962
    				String DataSet = "detalleListadoArticulos", consulta = "";

    				//Modificamos el DataSet
    				comp.obtenerComponentesDataSet(DataSet);
    				consulta = comp.obtenerQueryDataSet();

    				if(ValoresPorDefecto.getCodigoManualEstandarBusquedaArticulos(usuario.getCodigoInstitucionInt()).equals(ConstantesIntegridadDominio.acronimoInterfaz))
    				{
    					consulta = consulta.replace("1=1", "coalesce(getCodArticuloAxiomaInterfaz(dts.articulo, '"+ConstantesIntegridadDominio.acronimoInterfaz+"'), '')"); //interfaz
    					logger.info("interfaz");
    				}
    				else
    				{
    					consulta = consulta.replace("1=1", "dts.articulo");
    					logger.info("articulo");
    				}
    				comp.modificarQueryDataSet(consulta);

    				//debemos arreglar los alias para que funcione oracle, debido a que este los convierte a MAYUSCULAS
    				comp.lowerAliasDataSet();
    				String newPathReport = comp.saveReport1(false);                
    				if(!newPathReport.equals(""))
    				{
    					request.setAttribute("isOpenReport", "true");
    					request.setAttribute("newPathReport", newPathReport);
    				}
    				//              por ultimo se modifica la conexion a BD
    				comp.updateJDBCParameters(newPathReport);
    				UtilidadBD.cerrarConexion(con);
    				return mapping.findForward("paginaDetalle");

    			}
    			else/*----------------------------------------------
    			 * ESTADO =================>>>>> GENERAR
				 ---------------------------------------------*/
    				if (estado.equals("generar"))
    				{
    					mundo.generar(con, usuario, forma, request, institucion, mapping);
    					return mapping.findForward("paginaBusqueda");
    				}
    				else
    				{
    					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
    					UtilidadBD.cerrarConexion(con);
    					return mapping.findForward("paginaError");
    				}


    		}
    		else
    		{
    			logger.error("El form no es compatible con el form de DespachoTrasladoAlmacenForm");
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
     * metodo para realizar la consulta del detalle
     * de la solicitud
     * @param con
     * @param forma
     * @param mundo
     */
    private void accionGenerarConsultaDetalleTraslado(Connection con, ConsultaImpresionTrasladosForm forma, ConsultaImpresionTraslados mundo) 
    {
      HashMap mapa=new HashMap();
      mapa=mundo.consultaDetalleSolicitud(con,Integer.parseInt(forma.getMapaListadoTraslados("numero_traslado_"+forma.getRegSeleccionado())+""));
      forma.setMapaListadoTraslados("detalleTraslado",mapa);
    }
    /**
     * metodo para realizar la busqueda avanzada
     * @param con
     * @param forma
     * @param mundo 
     * @param usuario
     */
    private void accionGenerarBusquedaAvanzada(Connection con, ConsultaImpresionTrasladosForm forma, ConsultaImpresionTraslados mundo, UsuarioBasico usuario) 
    {
        forma.setMapaAtributosBusqueda("institucion",usuario.getCodigoInstitucionInt()+"");
        forma.setMapaListadoTraslados(mundo.ejecutarBusquedaAvanzadaTraslados(con,forma.getMapaAtributosBusqueda()));   
        if(Integer.parseInt(forma.getMapaListadoTraslados("numRegistros")+"")>0)
            forma.setMostrarListado("true");
        else if(Integer.parseInt(forma.getMapaListadoTraslados("numRegistros")+"")==0)
            forma.setMostrarListado("false");       
    }
    /**
     * metodo para ordenar las solicitudes
     * @param con
     * @param forma
     * @param mapping
     * @return
     * @author jarloc
     */ 
    private void accionOrdenarMapaSolicitudes(ConsultaImpresionTrasladosForm forma) 
    {         
        Object temp=forma.getMapaListadoTraslados("numRegistros");
        String[] indices={
                            "numero_traslado_", 
                            "cod_almacen_despacha_", 
                            "nom_almacen_despacha_", 
                            "cod_almacen_solicita_",
                            "nom_almacen_solicita_",   
                            "cod_estado_",    
                            "nom_estado_", 
                            "prioritario_",
                            "fecha_solicitud_",
                            "usuario_solicita_",
                            "fecha_despacho_" ,
                            "usuario_despacho_" ,
                            "hora_elaboracion_" ,
                            "hora_despacho_" ,
                            "observaciones_",
                            "centro_atencion_solicitado_",
                            "centro_atencion_solicita_"
                        };
        forma.setMapaListadoTraslados(Listado.ordenarMapa(indices,forma.getPatronOrdenar(),forma.getUltimoPatron(),forma.getMapaListadoTraslados(),Integer.parseInt(temp+"")));
        forma.setMapaListadoTraslados("numRegistros",temp);
        for(int k=0;k<Integer.parseInt(forma.getMapaListadoTraslados("numRegistros")+"");k++)
        {
            
            if((forma.getMapaListadoTraslados("fecha_despacho_"+k)+"").equals("null"))
                forma.setMapaListadoTraslados("fecha_despacho_"+k,"");   
            if((forma.getMapaListadoTraslados("usuario_despacho_"+k)+"").equals("null"))
                forma.setMapaListadoTraslados("usuario_despacho_"+k,"");
        }
        forma.setUltimoPatron(forma.getPatronOrdenar());        
    }
}
