package com.princetonsa.action.manejoPaciente;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Vector;

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
import util.UtilidadFecha;
import util.UtilidadValidacion;
import util.Utilidades;
import util.manejoPaciente.ConstantesBDManejoPaciente;
import util.manejoPaciente.UtilidadesManejoPaciente;

import com.princetonsa.actionform.manejoPaciente.CalidadAtencionForm;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.manejoPaciente.CalidadAtencion;
import com.princetonsa.util.birt.reports.DesignEngineApi;
import com.princetonsa.util.birt.reports.ParamsBirtApplication;

/**
 * Clase para el manejo de los reportes
 * de calidad en atención
 * Date: 2008-08-11
 * @author garias@princetonsa.com
 */
public class CalidadAtencionAction extends Action 
{
	
	/**
	 * logger 
	 * */
	Logger logger = Logger.getLogger(CalidadAtencionAction.class);
	
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
    		if(form instanceof CalidadAtencionForm)
    		{

    			con = UtilidadBD.abrirConexion();

    			if(con == null)
    			{	
    				request.setAttribute("CodigoDescripcionError","errors.problemasBd");
    				return mapping.findForward("paginaError");
    			}
    			CalidadAtencionForm forma = (CalidadAtencionForm)form;
    			String estado = forma.getEstado();

    			ActionErrors errores = new ActionErrors();

    			logger.info("\n\n\n ESTADO (EstadisticasCalidadAtencionForm) ---> "+estado+"\n\n");

    			UsuarioBasico usuario = (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
    			PersonaBasica paciente = (PersonaBasica)request.getSession().getAttribute("pacienteActivo");

    			if(estado == null)
    			{ 
    				forma.reset();
    				logger.warn("Estado No Valido Dentro Del Flujo de estadistica calidad de atención (null)");
    				request.setAttribute("CodigoDescripcionError","Errors.estadoInvalido");
    				UtilidadBD.cerrarConexion(con);
    				return mapping.findForward("principal");
    			}
    			else 
    				/*------------------------------
    				 * 		ESTADO > empezar
    				 *-------------------------------*/
    				if(estado.equals("empezar"))
    				{   
    					return accionEmpezar(con, mapping, forma, usuario);
    				}
    				else
    					/*------------------------------
    					 * 		ESTADO > cambiarTipoReporte
    					 *-------------------------------*/
    					if(estado.equals("cambiarTipoReporte"))
    					{   
    						return accionCambiaTipoReporte(con, mapping, forma, usuario);
    					}
    					else
    						/*------------------------------
    						 * 		ESTADO > generarReporte
    						 *-------------------------------*/
    						if(estado.equals("generarReporte"))
    						{   
    							return accionGenerarReporte(con, mapping, forma, usuario, request);
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
     * @param con
     * @param mapping
     * @param forma
     * @param usuario
     * @return
     */
    private ActionForward accionGenerarReporte(Connection con, ActionMapping mapping, CalidadAtencionForm forma, UsuarioBasico usuario, HttpServletRequest request) {
    	
    	if (forma.getFiltros("tipoReporte").equals(ConstantesBDManejoPaciente.tipoReporteSatisfaccionGeneral+""))
    		generarReporteSatisfaccionGeneral(con, forma, usuario, request);
    	else
    		generarMotCalificacionCalidadAtencion(con, forma, usuario, request);
    	
    	UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}

    /**
     * 
     * @param con
     * @param forma
     * @param usuario
     */
	private void generarMotCalificacionCalidadAtencion(Connection con, CalidadAtencionForm forma, UsuarioBasico usuario,HttpServletRequest request) {
		String nombreRptDesign = "MotCalificacionCalidadAtencion.rptdesign";
    	CalidadAtencion mundo = new CalidadAtencion();
		InstitucionBasica ins = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
		String viaIngreso="";
		String tipoPaciente="";
		Vector v;
		
		//***************** INFORMACIÓN DEL CABEZOTE
        DesignEngineApi comp; 
        comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"manejoPaciente/",nombreRptDesign);
         
        // Logo
        comp.insertImageHeaderOfMasterPage1(0, 0, ins.getLogoReportes());
        
        // Nombre Institución, titulo y rango de fechas
        comp.insertGridHeaderOfMasterPageWithName(0,1,1,2, "titulo");
        v=new Vector();
        v.add(ins.getRazonSocial());
        v.add("\nSATISFACCIÓN GENERAL\nPeriodo: ["+forma.getFiltros("fechaInicial")+" - "+forma.getFiltros("fechaFinal")+"]");
        comp.insertLabelInGridOfMasterPage(0,1,v);
        
        // Parametros de Generación
        comp.insertGridHeaderOfMasterPageWithName(1,0,1,1,"param");
        v=new Vector();
        
    	// Centro de Atención
    	String filtroo="[Centro de Atención: "+Utilidades.obtenerNombreCentroAtencion(con, Utilidades.convertirAEntero(forma.getFiltros("centroAtencion").toString()))+"]  ";
    	
    	// Via de ingreso - tipo paciente
    	boolean todos=true;
    	logger.info("numViaIngresoTipoPaciente ---> "+forma.getFiltros("numViaIngresoTipoPaciente").toString());
    	filtroo+="[Vía de Ingreso - Tipo Paciente: ";
    	if (Integer.parseInt(forma.getFiltros("numViaIngresoTipoPaciente").toString())>0){
    		for(int i=0; i<Integer.parseInt(forma.getFiltros("numViaIngresoTipoPaciente").toString()); i++){
    			logger.info("viaIngresoTipoPaciente_ ---> "+forma.getFiltros("viaIngresoTipoPaciente_"+i));
    			if(forma.getFiltros("viaIngresoTipoPaciente_"+i).equals(ConstantesBD.acronimoSi)){
    				filtroo+=((HashMap)forma.getViaIngresoTipoPaciente().get(i)).get("viaingresotipopac")+", ";
    				viaIngreso+=((HashMap)forma.getViaIngresoTipoPaciente().get(i)).get("codvia")+", ";
    				tipoPaciente+="'"+((HashMap)forma.getViaIngresoTipoPaciente().get(i)).get("codtipopac")+"', ";
    				todos=false;
    			}
    		}
    		viaIngreso+=ConstantesBD.codigoNuncaValido+"";
    		tipoPaciente+="'"+ConstantesBD.codigoNuncaValido+"'";
    	} 
    	if(todos)
    		filtroo+="Todos] ";
    	else
    		filtroo+="]  ";
    	
    	// Tipo de motivo
    	if(forma.getFiltros().containsKey("tipoMotivo"))
    		if (!forma.getFiltros("tipoMotivo").equals("")){
    			if(forma.getFiltros("tipoMotivo").equals(ConstantesIntegridadDominio.acronimoSatisfaccion))
    				filtroo+="[Tipo Motivo: Satisfacción]";
    			else
    				filtroo+="[Tipo Motivo: Insatisfacción]";
    		}	
    	
    	v.add(filtroo);
    	comp.insertLabelInGridOfMasterPage(1,0,v);
        
        // Fecha hora de proceso y usuario
        comp.insertLabelInGridPpalOfFooter(0,0,"Fecha: "+UtilidadFecha.getFechaActual()+" Hora: "+UtilidadFecha.getHoraActual());
        comp.insertLabelInGridPpalOfFooter(0,1,"Usuario: "+usuario.getLoginUsuario());
        //******************
		
        mundo.setFiltrosMap(forma.getFiltros());
        mundo.setViasDeIngreso(viaIngreso);
        mundo.setTiposPaciente(tipoPaciente);
        mundo.setUsuario(usuario.getLoginUsuario());
        
        //***************** NUEVO WHERE DEL REPORTE
        String where = mundo.crearWhereMotCalificacionCalidadAtencion(con, mundo);
        
        // Registro generación del reporte en BD
	    UtilidadesManejoPaciente.insertarLogReportes(con, ConstantesBDManejoPaciente.tipoReporteSatisfaccionGeneral, usuario.getLoginUsuario(),usuario.getCodigoInstitucionInt());
        
       comp.obtenerComponentesDataSet("dataSet1");
       String newquery=comp.obtenerQueryDataSet().replaceAll("1=1", where);
       
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
	}

	/**
	 * 
	 * @param con
	 * @param forma
	 * @param usuario
	 */
	private void generarReporteSatisfaccionGeneral(Connection con, CalidadAtencionForm forma, UsuarioBasico usuario, HttpServletRequest request) {
		String nombreRptDesign = "SatisfaccionGeneral.rptdesign";
		CalidadAtencion mundo = new CalidadAtencion();
		InstitucionBasica ins = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
		String viaIngreso="";
		String tipoPaciente="";
		Vector v;
		
		//***************** INFORMACIÓN DEL CABEZOTE
        DesignEngineApi comp; 
        comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"manejoPaciente/",nombreRptDesign);
         
        // Logo
        comp.insertImageHeaderOfMasterPage1(0, 0, ins.getLogoReportes());
        
        // Nombre Institución, titulo y rango de fechas
        comp.insertGridHeaderOfMasterPageWithName(0,1,1,2, "titulo");
        v=new Vector();
        v.add(ins.getRazonSocial());
        v.add("\nSATISFACCIÓN GENERAL\nPeriodo: ["+forma.getFiltros("fechaInicial")+" - "+forma.getFiltros("fechaFinal")+"]");
        comp.insertLabelInGridOfMasterPage(0,1,v);
        
        // Parametros de Generación
        comp.insertGridHeaderOfMasterPageWithName(1,0,1,1,"param");
        v=new Vector();
        try{
        	// Centro de Atención
        	String filtroo="[Centro de Atención: "+Utilidades.obtenerNombreCentroAtencion(con, Utilidades.convertirAEntero(forma.getFiltros("centroAtencion").toString()))+"]  ";
        	
        	// Via de ingreso - tipo paciente
        	logger.info("numViaIngresoTipoPaciente ---> "+forma.getFiltros("numViaIngresoTipoPaciente").toString());
        	boolean todos=true;
        	filtroo+="[Vía de Ingreso - Tipo Paciente: ";
        	if (Integer.parseInt(forma.getFiltros("numViaIngresoTipoPaciente").toString())>0){
        		for(int i=0; i<Integer.parseInt(forma.getFiltros("numViaIngresoTipoPaciente").toString()); i++){
        			logger.info("viaIngresoTipoPaciente_ ---> "+forma.getFiltros("viaIngresoTipoPaciente_"+i));
        			if(forma.getFiltros("viaIngresoTipoPaciente_"+i).equals(ConstantesBD.acronimoSi)){
        				filtroo+=((HashMap)forma.getViaIngresoTipoPaciente().get(i)).get("viaingresotipopac")+", ";
        				viaIngreso+=((HashMap)forma.getViaIngresoTipoPaciente().get(i)).get("codvia")+", ";
        				tipoPaciente+="'"+((HashMap)forma.getViaIngresoTipoPaciente().get(i)).get("codtipopac")+"', ";
        				todos=false;
        			}
        		}
        		viaIngreso+=ConstantesBD.codigoNuncaValido+"";
        		tipoPaciente+=" '"+ConstantesBD.codigoNuncaValido+"'";
        	} 
        	if(todos)
        		filtroo+="Todos] ";
        	else
        		filtroo+="]  ";
        		
        	
        	// Centro de Costo
        	if(forma.getFiltros().containsKey("centroCosto"))
        		if (!forma.getFiltros("centroCosto").equals(""))
        			filtroo+="[Centro de costo: "+UtilidadValidacion.getNombreCentroCosto(con, Utilidades.convertirAEntero(forma.getFiltros("centroCosto").toString()))+"]  ";
        	
        	v.add(filtroo);
        	
        }catch(SQLException e){}
        comp.insertLabelInGridOfMasterPage(1,0,v);
        
        // Fecha hora de proceso y usuario
        comp.insertLabelInGridPpalOfFooter(0,0,"Fecha: "+UtilidadFecha.getFechaActual()+" Hora: "+UtilidadFecha.getHoraActual());
        comp.insertLabelInGridPpalOfFooter(0,1,"Usuario: "+usuario.getLoginUsuario());
        //******************
		
        //***************** NUEVO WHERE DEL REPORTE
        
        mundo.setFiltrosMap(forma.getFiltros());
        mundo.setViasDeIngreso(viaIngreso);
        mundo.setTiposPaciente(tipoPaciente);
        mundo.setUsuario(usuario.getLoginUsuario());
        
        //***************** NUEVO WHERE DEL REPORTE
        String where = mundo.crearWhereSatisfaccionGeneral(con, mundo);
        
        // Registro generación del reporte en BD
	    UtilidadesManejoPaciente.insertarLogReportes(con, ConstantesBDManejoPaciente.tipoReporteMotCalificacionCalidadAtencion, usuario.getLoginUsuario(),usuario.getCodigoInstitucionInt());

       comp.obtenerComponentesDataSet("dataSet1");
       String newquery=comp.obtenerQueryDataSet().replaceAll("1=1", where);
       
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
	}

	/**
     * 
     * @param con
     * @param mapping
     * @param forma
     * @param usuario
     * @return
     */
    private ActionForward accionCambiaTipoReporte(Connection con, ActionMapping mapping, CalidadAtencionForm forma, UsuarioBasico usuario) {
    	UtilidadBD.closeConnection(con);
    	if(!forma.getFiltros("tipoReporte").toString().equals("")){
    		int tipoReporte = Integer.parseInt(forma.getFiltros("tipoReporte").toString());
    		forma.setFiltros(new HashMap());
        	forma.setFiltros("tipoReporte",tipoReporte);
        	forma.setFiltros("numViaIngresoTipoPaciente",forma.getViaIngresoTipoPaciente().size());
    		forma.setFiltros("centroAtencion", usuario.getCodigoCentroAtencion());
    	}
		return mapping.findForward("principal");
	}

	/**
     * 
     * @param con
     * @param mapping
     * @param forma
     * @param usuario
     * @return
     */
	private ActionForward accionEmpezar(Connection con, ActionMapping mapping, CalidadAtencionForm forma, UsuarioBasico usuario) {
		forma.reset();
		forma.setCentrosAtencion(UtilidadesManejoPaciente.obtenerCentrosAtencion(con, usuario.getCodigoInstitucionInt(),""));
		forma.setCentrosCosto(UtilidadesManejoPaciente.obtenerCentrosCosto(con, usuario.getCodigoInstitucionInt(), "", false, 0));
		forma.setViaIngresoTipoPaciente(Utilidades.obtenerViasIngresoTipoPaciente(con));
		forma.setFiltros("numViaIngresoTipoPaciente",forma.getViaIngresoTipoPaciente().size());
		forma.setFiltros("centroAtencion", usuario.getCodigoCentroAtencion());
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}
}