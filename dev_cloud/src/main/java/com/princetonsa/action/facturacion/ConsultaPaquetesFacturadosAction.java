package com.princetonsa.action.facturacion;

import java.sql.Connection;
import java.util.Vector;

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
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;

import com.princetonsa.actionform.facturacion.ConsultaPaquetesFacturadosForm;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.facturacion.ConsultaPaquetesFacturados;
import com.princetonsa.util.birt.reports.DesignEngineApi;
import com.princetonsa.util.birt.reports.ParamsBirtApplication;

public class ConsultaPaquetesFacturadosAction extends Action 
{

	
	
	/**
	 * objeto para manejar los logs de esta clase
	 */
	Logger logger =Logger.getLogger(ConsultaPaquetesFacturadosAction.class);
	
	
	/**
	 * 
	 */
	private String[] indices={
								"descripcionpaquete_",
								"codigopaquete_",
								"valorpaquete_",
								"factura_",
								"convenio_",
								"usuariofactura_",
								"fechafactura_",
								"horafactura_",
								"detallefactura_",
								"valorcontenidopaquete_"
							 }; 
	
	
	/**
	 * 
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)throws Exception
	{
		Connection con=null;
		try{
			if (form instanceof ConsultaPaquetesFacturadosForm) 
			{
				ConsultaPaquetesFacturadosForm forma=(ConsultaPaquetesFacturadosForm) form;

				String estado=forma.getEstado();

				logger.info("Estado -->"+estado);

				con=UtilidadBD.abrirConexion();
				UsuarioBasico usuario=Utilidades.getUsuarioBasicoSesion(request.getSession());

				ConsultaPaquetesFacturados mundo=new ConsultaPaquetesFacturados();

				if(estado == null)
				{
					logger.warn("Estado no valido dentro del flujo de ConsultaPaquetesFacturadosAction (null) ");
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.closeConnection(con);
					return mapping.findForward("paginaError");
				}
				else if(estado.equals("empezar"))
				{
					forma.reset();
					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");
				}
				else if(estado.equals("buscar"))
				{
					forma.setMapaListadoPaquetes(mundo.consultarPaquetesFacturados(con, forma.getCodigoConvenio(), forma.getCodigoPaquete(), forma.getFechaInicial(), forma.getFechaFinal()));
					UtilidadBD.closeConnection(con);
					return mapping.findForward("listado");
				}
				else if(estado.equals("detallePaquete"))
				{
					forma.setMapadetallePaquete(mundo.consultarDetallePaquetes(con, forma.getMapaListadoPaquetes().get("detallefactura_"+forma.getIndiceDetalle())+""));
					UtilidadBD.closeConnection(con);
					return mapping.findForward("detalle");
				}
				else if(estado.equals("detalleAsocio"))
				{
					forma.setMapaAsociosCirugia(mundo.consultarAsociosCirugia(con, forma.getMapadetallePaquete().get("solicitud_"+forma.getIndiceAsocio())+"", forma.getMapadetallePaquete().get("serviarticulo_"+forma.getIndiceAsocio())+"", forma.getMapadetallePaquete().get("codigodetfact_"+forma.getIndiceAsocio())+"") );
					UtilidadBD.closeConnection(con);
					return mapping.findForward("asocio");
				}
				else if(estado.equals("nuevaBusqueda"))
				{
					forma.reset();
					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");
				}
				else if(estado.equals("ordernar"))
				{
					this.accionOrdenar(forma);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("listado");
				}
				//ESTADO UTILIZADO PARA EL PAGER
				else if (estado.equals("redireccion")) 
				{			    
					UtilidadBD.cerrarConexion(con);
					forma.getLinkSiguiente();
					response.sendRedirect(forma.getLinkSiguiente());
					return null;
				}
				else if (estado.equals("imprimir"))
				{
					this.generarReporte(con, forma, mapping, request, usuario, mundo);                
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("listado");
				}
				else if(estado.equals("imprimirDetalle"))
				{
					this.generarReporteDetallado(con, forma, mapping, request, usuario, mundo);
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("detalle");
				}
				else
				{
					forma.reset();
					logger.warn("Estado no valido dentro del flujo de CONSULTA PAQUETES FACTURADOS ");
					request.setAttribute("codigoDescripcionError", "errors.formaTipoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}		
			}
			else
			{
				logger.error("El form no es compatible con el form de ConsultaPaquetesFacturadosForm");
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
	 * 
	 * @param forma
	 */
	private void accionOrdenar(ConsultaPaquetesFacturadosForm forma) 
	{
		int numReg=Utilidades.convertirAEntero(forma.getMapaListadoPaquetes("numRegistros")+"");
		forma.setMapaListadoPaquetes(Listado.ordenarMapa(indices,forma.getPatronOrdenar(),forma.getUltimoPatron(),forma.getMapaListadoPaquetes(),numReg));
		forma.setUltimoPatron(forma.getPatronOrdenar());
		forma.setMapaListadoPaquetes("numRegistros",numReg+"");
		
	}
	
	
	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mapping
	 * @param request
	 * @param usuarioActual
	 * @param mundo
	 * @return
	 */
	private ActionForward generarReporte(Connection con, ConsultaPaquetesFacturadosForm forma, ActionMapping mapping, HttpServletRequest request, UsuarioBasico usuarioActual, ConsultaPaquetesFacturados mundo) 
	{
		String nombreRptDesign = "ConsultaPaquetesFacturados.rptdesign";
		InstitucionBasica institucion = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
		//PRIMERO SE INSERTA LA INFORMACION DEL CABEZOTE************************************************
		DesignEngineApi comp;
        comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"facturacion/",nombreRptDesign);
        comp.insertImageHeaderOfMasterPage1(0, 0, institucion.getLogoReportes());
        comp.insertGridHeaderOfMasterPage(0,1,1,4);
        Vector v=new Vector();
        v.add(institucion.getRazonSocial());
        v.add(Utilidades.getDescripcionTipoIdentificacion(con,institucion.getTipoIdentificacion())+"  -  "+institucion.getNit());
        v.add(institucion.getDireccion());
        v.add("Tels. "+institucion.getTelefono());
        comp.insertLabelInGridOfMasterPage(0,1,v);
        
        //SE MODIFICA LA CONSULTA PARA QUE TOME TODOS LOS CODIGOS DE LOS DETALLES CARGO INSERTADOS
        comp.obtenerComponentesDataSet("consultaPaquetes");            
        String oldQuery=comp.obtenerQueryDataSet();
        
        String condiciones="";
        if(!UtilidadTexto.isEmpty(forma.getCodigoConvenio()))
        {
        	condiciones+=" AND f.convenio="+forma.getCodigoConvenio()+" ";
        }
        if(!UtilidadTexto.isEmpty(forma.getCodigoPaquete()))
        {
        	condiciones+=" AND pc.paquete='"+forma.getCodigoPaquete()+"' ";
        }
        
        String newQuery=comp.obtenerQueryDataSet()+" AND to_char(f.fecha,'yyyy-mm-dd') between '"+UtilidadFecha.conversionFormatoFechaABD(forma.getFechaInicial())+"' and '"+UtilidadFecha.conversionFormatoFechaABD(forma.getFechaFinal())+"' "+condiciones;
        comp.modificarQueryDataSet(newQuery);
        
        //debemos arreglar los alias para que funcione oracle, debido a que este los convierte a MAYUSCULAS
      	comp.lowerAliasDataSet();
		String newPathReport = comp.saveReport1(false);
        comp.updateJDBCParameters(newPathReport);
        
       if(!newPathReport.equals(""))
       {
    	   request.setAttribute("isOpenReport", "true");
    	   request.setAttribute("newPathReport", newPathReport);
       }
        
		UtilidadBD.closeConnection(con);
		return mapping.findForward("listado");
	}
	
	
	
	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mapping
	 * @param request
	 * @param usuario
	 * @param mundo
	 */
	private ActionForward generarReporteDetallado(Connection con, ConsultaPaquetesFacturadosForm forma, ActionMapping mapping, HttpServletRequest request, UsuarioBasico usuario, ConsultaPaquetesFacturados mundo) 
	{
		String nombreRptDesign = "DetallePaquetesFacturados.rptdesign";
		InstitucionBasica institucion = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
		//PRIMERO SE INSERTA LA INFORMACION DEL CABEZOTE************************************************
		DesignEngineApi comp;
        comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"facturacion/",nombreRptDesign);
        comp.insertImageHeaderOfMasterPage1(0, 0, institucion.getLogoReportes());
        comp.insertGridHeaderOfMasterPage(0,1,1,4);
        Vector v=new Vector();
        v.add(institucion.getRazonSocial());
        v.add(Utilidades.getDescripcionTipoIdentificacion(con,institucion.getTipoIdentificacion())+"  -  "+institucion.getNit());
        v.add(institucion.getDireccion());
        v.add("Tels. "+institucion.getTelefono());
        comp.insertLabelInGridOfMasterPage(0,1,v);
        
        comp.obtenerComponentesDataSet("DetallePaquetes");            
        String oldQuery=comp.obtenerQueryDataSet();
        
        logger.info("oldQuery >>>>>>>>>>>>>"+oldQuery);
        
        
        
        String newQuery=oldQuery.replaceAll("CODIGODETALLEFACTURA", forma.getMapaListadoPaquetes("detallefactura_"+forma.getIndiceDetalle())+"");
        logger.info("nueva consulta "+newQuery);
        comp.modificarQueryDataSet(newQuery);
        
        
        comp.lowerAliasDataSet();
		String newPathReport = comp.saveReport1(false);
        comp.updateJDBCParameters(newPathReport);
        
        //se mandan los parámetros al reporte
        //newPathReport += "&detalleFactura="+Utilidades.convertirAEntero(forma.getMapaListadoPaquetes("detallefactura_"+forma.getIndiceDetalle())+"");
        
       if(!newPathReport.equals(""))
       {
    	   request.setAttribute("isOpenReport", "true");
    	   request.setAttribute("newPathReport", newPathReport);
       }
        
		UtilidadBD.closeConnection(con);
		return mapping.findForward("detalle");
		
	}
	
	
	
}
