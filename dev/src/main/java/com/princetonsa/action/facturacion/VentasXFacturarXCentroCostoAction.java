package com.princetonsa.action.facturacion;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadValidacion;
import util.Utilidades;
import util.manejoPaciente.UtilidadesManejoPaciente;

import com.princetonsa.actionform.facturacion.VentasXFacturarXCentroCostoForm;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.util.birt.reports.DesignEngineApi;
import com.princetonsa.util.birt.reports.ParamsBirtApplication;

/**
 * Clase para el manejo de los reportes de ventas por facturar por centro de costo
 * Date: 2008-06-11
 * @author garias@princetonsa.com
 */
public class VentasXFacturarXCentroCostoAction extends Action 
{
	
	/**
	 * logger 
	 * */
	Logger logger = Logger.getLogger(VentasXFacturarXCentroCostoAction.class);
	
	/**
	 * Metodo excute del Action
	 */
    public ActionForward execute(   ActionMapping mapping,
            						ActionForm form,
            						HttpServletRequest request,
            						HttpServletResponse response ) throws Exception
    {
    	Connection con = null;
    	try{
    	if(response==null);
    	if(form instanceof VentasXFacturarXCentroCostoForm)
    	{
    		
    		con = UtilidadBD.abrirConexion();
    		
    		if(con == null)
    		{	
    			request.setAttribute("CodigoDescripcionError","errors.problemasBd");
    			return mapping.findForward("paginaError");
    		}
    		VentasXFacturarXCentroCostoForm forma = (VentasXFacturarXCentroCostoForm)form;
    		String estado = forma.getEstado();
    		
    		ActionErrors errores = new ActionErrors();
    	
    		logger.info("\n\n\n ESTADO (REPORTE VENTAS POR FACTURAR POR CENTRO DE COSTO) ---> "+estado+"\n\n");
    		
    		UsuarioBasico usuario = (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
    		
    		if(estado == null)
    		{
    			forma.reset();
    			logger.warn("Estado No Valido Dentro Del Flujo de la Parametrización de los Eventos Adversos (null)");
    			request.setAttribute("CodigoDescripcionError","Errors.estadoInvalido");
    			UtilidadBD.cerrarConexion(con);
    			return mapping.findForward("paginaError");
    		}
    		else 
			/*------------------------------
			 * 		ESTADO > empezar
			 *-------------------------------*/
			if(estado.equals("empezar"))
			{   
				return accionEmpezar(con, mapping, request, errores, forma, usuario);
			}
			else 
			/*------------------------------
			 * 		ESTADO > cambiarCentroAtencion
			 *-------------------------------*/
			if(estado.equals("cambiarCentroAtencion"))
			{   
				return accionCambiarCentroAtencion(con, mapping, request, errores, forma, usuario);
			}
			else 
			/*------------------------------
			 * 		ESTADO > generarReporte
			 *-------------------------------*/
			if(estado.equals("generarReporte"))
			{   
				return accionGenerarReporte(con, mapping, request, errores, forma, usuario);
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
    
    private ActionForward accionGenerarReporte(Connection con, ActionMapping mapping, HttpServletRequest request, ActionErrors errores, VentasXFacturarXCentroCostoForm forma, UsuarioBasico usuario) {
    
    	errores = validarDatos(errores, forma);
    	
    	if (errores.isEmpty()){
    		//if (UtilidadesFacturacion.hayVentasXFacturar(con, forma.getFiltrosMap("anoCorte").toString(), forma.getFiltrosMap("mesCorte").toString(), forma.getFiltrosMap("centroAtencion").toString(), forma.getFiltrosMap("centroCosto").toString())){
	    		String nombreRptDesign = "VentasXFacturarXCentroCosto.rptdesign";
		    	
				InstitucionBasica ins = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
				
				//Informacion del Cabezote
		        DesignEngineApi comp; 
		        comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"facturacion/",nombreRptDesign);
		        comp.insertImageHeaderOfMasterPage1(0, 0, ins.getLogoReportes());
		        comp.insertGridHeaderOfMasterPage(0,1,1,4);
		        Vector v=new Vector();
		        v.add(ins.getRazonSocial());
		        v.add(Utilidades.getDescripcionTipoIdentificacion(con,ins.getTipoIdentificacion())+"  "+ins.getNit());     
		        v.add("VENTAS POR FACTURAR POR CENTRO DE COSTO");
		        try{
		        	
		        	String filtroo="Centro de Atención: ";
		        	if (!forma.getFiltrosMap("centroAtencion").equals("")) 
		        		filtroo+=Utilidades.obtenerNombreCentroAtencion(con, Utilidades.convertirAEntero(forma.getFiltrosMap("centroAtencion").toString()));
		        	else
		        		filtroo+="Todos";
		        	
		        	filtroo+="\nCentro de costo: ";
		        	if (forma.getFiltrosMap("centroCosto").equals(""))
		        		filtroo += "Todos ";
		        	else
		        		filtroo += UtilidadValidacion.getNombreCentroCosto(con, Utilidades.convertirAEntero(forma.getFiltrosMap("centroCosto").toString())); 
		        	
		        	filtroo += "\nFecha de Corte: "+forma.getFiltrosMap("mesCorte")+"/"+forma.getFiltrosMap("anoCorte");
		        	
		        	v.add(filtroo);
		        }catch(SQLException e){}		        
		        comp.insertLabelInGridOfMasterPage(0,1,v);
		        
		        comp.insertLabelInGridPpalOfHeader(0,2,"Fecha de proceso: "+UtilidadFecha.getFechaActual()+"\nHora de Proceso: "+UtilidadFecha.getHoraActual()+"\nUsuario Proceso: "+usuario.getLoginUsuario());
		        		        
		       String where = "cl.tipo_solicitud <> "+ConstantesBD.codigoTipoSolicitudPaquetes+" AND to_char(cl.fecha_corte, 'YYYY/MM')='"+forma.getFiltrosMap("anoCorte")+"/"+forma.getFiltrosMap("mesCorte")+"' AND cl.centro_costo_solicitante NOT IN (0, -1) ";
		       
		       String filtroTipos1= " AND cl.tipo_solicitud NOT IN ("+ConstantesBD.codigoTipoSolicitudCargosDirectosArticulos+","+ConstantesBD.codigoTipoSolicitudMedicamentos+") ";
		       String filtroTipos2= " AND cl.tipo_solicitud IN ("+ConstantesBD.codigoTipoSolicitudCargosDirectosArticulos+","+ConstantesBD.codigoTipoSolicitudMedicamentos+") ";

		       if (!forma.getFiltrosMap("centroAtencion").equals("")) 
		    	   where += "AND cl.centro_atencion="+forma.getFiltrosMap("centroAtencion")+" "; 
		       
		       if (!forma.getFiltrosMap("centroCosto").toString().equals("")) 
		    	   where+="AND ((cl.tipo_solicitud not in ("+ConstantesBD.codigoTipoSolicitudMedicamentos+","+ConstantesBD.codigoTipoSolicitudCargosDirectosArticulos+") AND cl.centro_costo_solicitante = "+forma.getFiltrosMap("centroCosto")+") OR (cl.tipo_solicitud in ("+ConstantesBD.codigoTipoSolicitudMedicamentos+","+ConstantesBD.codigoTipoSolicitudCargosDirectosArticulos+") AND cl.centro_costo_principal = "+forma.getFiltrosMap("centroCosto")+")) ";
		       
		       comp.obtenerComponentesDataSet("prueba");
		       String newquery = comp.obtenerQueryDataSet().replaceAll("1=1", where+" "+filtroTipos1);
		       newquery = newquery.replaceAll("2=2", where+" "+filtroTipos2);
		       
		       logger.info("Query >>>"+newquery);
		       comp.modificarQueryDataSet(newquery);
		       //debemos arreglar los alias para que funcione oracle, debido a que este los convierte a MAYUSCULAS
      	comp.lowerAliasDataSet();
		String newPathReport = comp.saveReport1(false);
		       comp.updateJDBCParameters(newPathReport);        
		       
		       if(!newPathReport.equals(""))
		        {
		        	request.setAttribute("isOpenReport", "true");
		        	request.setAttribute("newPathReport", newPathReport);
		        }
		       
    		/*}  else {
    			forma.setEstado("mostrarMensajeSinResultados");
    		}*/
    	}
    	saveErrors(request, errores);
       	return mapping.findForward("principal");
	}

	private ActionErrors validarDatos(ActionErrors errores, VentasXFacturarXCentroCostoForm forma) {
		
		if (forma.getFiltrosMap("anoCorte").toString().length()!=4)
			errores.add("", new ActionMessage("errors.formatoAnoInvalido", "de Corte "+forma.getFiltrosMap("anoCorte")));
			
		if (forma.getFiltrosMap("anoCorte").equals(""))
			errores.add("año de corte requerido", new ActionMessage("errors.required","Año de Corte "));
		if (forma.getFiltrosMap("mesCorte").equals(""))
			errores.add("mes de corte requerido", new ActionMessage("errors.required","Mes de Corte "));
		
		if(!forma.getFiltrosMap("anoCorte").equals("") && !forma.getFiltrosMap("mesCorte").equals("")){
			if (Utilidades.convertirAEntero(forma.getFiltrosMap("mesCorte").toString())<10 && Utilidades.convertirAEntero(forma.getFiltrosMap("mesCorte").toString())>0){
				forma.setFiltrosMap("mesCorte", "0"+Utilidades.convertirAEntero(forma.getFiltrosMap("mesCorte").toString()));
			}
			if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia("01/"+forma.getFiltrosMap("mesCorte")+"/"+forma.getFiltrosMap("anoCorte"),UtilidadFecha.getFechaActual()))
				errores.add("Fecha inicial mayor a la fecha del sistema",new ActionMessage("errors.fechaPosteriorIgualActual","inicial","actual"));
		}
		
		
		logger.info("fecha actual"+UtilidadFecha.getFechaActual());
		return errores;
	}

	private ActionForward accionCambiarCentroAtencion(Connection con, ActionMapping mapping, HttpServletRequest request, ActionErrors errores, VentasXFacturarXCentroCostoForm forma, UsuarioBasico usuario) {
		if (!forma.getFiltrosMap("centroAtencion").equals("")) 
			forma.setCentrosCosto(Utilidades.obtenerCentrosCosto(con, usuario.getCodigoInstitucionInt(), ConstantesBD.codigoTipoAreaDirecto+ConstantesBD.separadorSplit+ConstantesBD.codigoTipoAreaSubalmacen, true, Integer.parseInt(forma.getFiltrosMap("centroAtencion").toString()),false));
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}

	private ActionForward accionEmpezar(Connection con, ActionMapping mapping, HttpServletRequest request, ActionErrors errores, VentasXFacturarXCentroCostoForm forma, UsuarioBasico usuario) {
		forma.reset();
		forma.setFiltrosMap("centroAtencion",usuario.getCodigoCentroAtencion());
		forma.setCentrosAtencion(UtilidadesManejoPaciente.obtenerCentrosAtencion(con, usuario.getCodigoInstitucionInt(),""));
		forma.setCentrosCosto(Utilidades.obtenerCentrosCosto(con, usuario.getCodigoInstitucionInt(), ConstantesBD.codigoTipoAreaDirecto+ConstantesBD.separadorSplit+ConstantesBD.codigoTipoAreaSubalmacen, true, usuario.getCodigoCentroAtencion(),false));
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}


	
}