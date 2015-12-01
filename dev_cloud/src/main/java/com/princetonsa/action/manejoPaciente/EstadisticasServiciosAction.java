package com.princetonsa.action.manejoPaciente;

import java.sql.Connection;
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
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.Utilidades;
import util.facturacion.UtilidadesFacturacion;
import util.manejoPaciente.ConstantesBDManejoPaciente;
import util.manejoPaciente.UtilidadesManejoPaciente;

import com.princetonsa.actionform.manejoPaciente.EstadisticasServiciosForm;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.manejoPaciente.EstadisticasServicios;
import com.princetonsa.util.birt.reports.DesignEngineApi;
import com.princetonsa.util.birt.reports.ParamsBirtApplication;

/**
 * Clase para el manejo de los reportes
 * de las estadisticas de servicios
 * Date: 2008-08-11
 * @author garias@princetonsa.com
 */
public class EstadisticasServiciosAction extends Action 
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
    	if(form instanceof EstadisticasServiciosForm)
    	{
    		
    		con = UtilidadBD.abrirConexion();
    		
    		if(con == null)
    		{	
    			request.setAttribute("CodigoDescripcionError","errors.problemasBd");
    			return mapping.findForward("paginaError");
    		}
    		EstadisticasServiciosForm forma = (EstadisticasServiciosForm)form;
    		String estado = forma.getEstado();
    		
    		ActionErrors errores = new ActionErrors();
    	
    		logger.info("\n\n\n ESTADO (EstadisticasServiciosForm) ---> "+estado+"\n\n");
    		
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
			 * 		ESTADO > adicionarConvenio
			 *-------------------------------*/
			if(estado.equals("adicionarConvenio"))
			{   
				return accionAdicionarConvenio(con, mapping, forma, usuario);
			}
			else
			/*------------------------------
			 * 		ESTADO > eliminarConvenio
			 *-------------------------------*/
			if(estado.equals("eliminarConvenio"))
			{   
				return accionEliminarConvenio(con, mapping, forma, usuario);
			}
			else
			/*------------------------------
			 * 		ESTADO > adicionarEspecialidad
			 *-------------------------------*/
			if(estado.equals("adicionarEspecialidad"))
			{   
				return accionAdicionarEspecialidad(con, mapping, forma, usuario);
			}
			else
			/*------------------------------
			 * 		ESTADO > eliminarEspecialidad
			 *-------------------------------*/
			if(estado.equals("eliminarEspecialidad"))
			{   
				return accionEliminarEspecialidad(con, mapping, forma, usuario);
			}
			else
			/*------------------------------
			 * 		ESTADO > adicionarGrupoServicio
			 *-------------------------------*/
			if(estado.equals("adicionarGrupoServicio"))
			{   
				return accionAdicionarGrupoServicio(con, mapping, forma, usuario);
			}
			else
			/*------------------------------
			 * 		ESTADO > eliminarGrupoServicio
			 *-------------------------------*/
			if(estado.equals("eliminarGrupoServicio"))
			{   
				return accionEliminarGrupoServicio(con, mapping, forma, usuario);
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
	private ActionForward accionAdicionarConvenio(Connection con, ActionMapping mapping, EstadisticasServiciosForm forma, UsuarioBasico usuario) {
		
		if (!forma.getFiltros("convenio").equals("")){
			// Adicionar el convenio
			forma.getConveniosMap().put("codigoConvenio_"+forma.getConveniosMap("numRegistros"), ((HashMap)forma.getConvenios().get(Utilidades.convertirAEntero(forma.getFiltros("convenio")+""))).get("codigoConvenio"));
			forma.getConveniosMap().put("nombreConvenio_"+forma.getConveniosMap("numRegistros"), ((HashMap)forma.getConvenios().get(Utilidades.convertirAEntero(forma.getFiltros("convenio")+""))).get("nombreConvenio"));
			forma.getConveniosMap().put("eliminado_"+forma.getConveniosMap("numRegistros"), ConstantesBD.acronimoNo);
			forma.getConveniosMap().put("posArray_"+forma.getConveniosMap("numRegistros"), forma.getFiltros("convenio"));
			forma.setConveniosMap("numRegistros", Utilidades.convertirAEntero(forma.getConveniosMap("numRegistros").toString())+1);
			// "Quitar" el convenio del select
			HashMap aux = (HashMap)forma.getConvenios().get(Utilidades.convertirAEntero(forma.getFiltros("convenio")+""));
			aux.put("seleccionado", ConstantesBD.acronimoSi);
		}
		//Utilidades.imprimirMapa(forma.getConveniosMap());
		UtilidadBD.closeConnection(con);
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
	private ActionForward accionEliminarConvenio(Connection con,ActionMapping mapping, EstadisticasServiciosForm forma,UsuarioBasico usuario) {
		forma.setConveniosMap("eliminado_"+forma.getPosMap(), ConstantesBD.acronimoSi);
		HashMap aux = (HashMap)forma.getConvenios().get(Utilidades.convertirAEntero(forma.getConveniosMap("posArray_"+forma.getPosMap())+""));
		aux.put("seleccionado", ConstantesBD.acronimoNo);
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}
	
	 /**
     * 
     * @param conveniosMap
     * @return
     */
    private String cadenaConveniosSeleccionados(HashMap conveniosMap) {
		String convenios="";
		for(int i=0; i<Utilidades.convertirAEntero(conveniosMap.get("numRegistros").toString()); i++){
			if(conveniosMap.get("eliminado_"+i).toString().equals(ConstantesBD.acronimoNo)){
				convenios += conveniosMap.get("codigoConvenio_"+i)+", ";
			}
		}
		convenios += ConstantesBD.codigoNuncaValido;
		logger.info("CONVENIOS - "+convenios);
		return convenios;
	}
    
    /**
	 * 
	 * @param con
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @return
	 */
	private ActionForward accionAdicionarEspecialidad(Connection con, ActionMapping mapping, EstadisticasServiciosForm forma, UsuarioBasico usuario) {
		
		if (!forma.getFiltros("especialidad").equals("")){
			// Adicionar el convenio
			forma.getEspecialidadesMap().put("codigoEspecialidad_"+forma.getEspecialidadesMap("numRegistros"), ((HashMap)forma.getEspecialidades().get(Utilidades.convertirAEntero(forma.getFiltros("especialidad")+""))).get("codigoespecialidad"));
			forma.getEspecialidadesMap().put("nombreEspecialidad_"+forma.getEspecialidadesMap("numRegistros"), ((HashMap)forma.getEspecialidades().get(Utilidades.convertirAEntero(forma.getFiltros("especialidad")+""))).get("nombreespecialidad"));
			forma.getEspecialidadesMap().put("eliminado_"+forma.getEspecialidadesMap("numRegistros"), ConstantesBD.acronimoNo);
			forma.getEspecialidadesMap().put("posArray_"+forma.getEspecialidadesMap("numRegistros"), forma.getFiltros("especialidad"));
			forma.setEspecialidadesMap("numRegistros", Utilidades.convertirAEntero(forma.getEspecialidadesMap("numRegistros").toString())+1);
			// "Quitar" el convenio del select
			HashMap aux = (HashMap)forma.getEspecialidades().get(Utilidades.convertirAEntero(forma.getFiltros("especialidad")+""));
			aux.put("seleccionado", ConstantesBD.acronimoSi);
		}
		UtilidadBD.closeConnection(con);
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
	private ActionForward accionEliminarEspecialidad(Connection con,ActionMapping mapping, EstadisticasServiciosForm forma,UsuarioBasico usuario) {
		forma.setEspecialidadesMap("eliminado_"+forma.getPosMap(), ConstantesBD.acronimoSi);
		HashMap aux = (HashMap)forma.getEspecialidades().get(Utilidades.convertirAEntero(forma.getEspecialidadesMap("posArray_"+forma.getPosMap())+""));
		aux.put("seleccionado", ConstantesBD.acronimoNo);
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}
	
	 /**
     * 
     * @param conveniosMap
     * @return
     */
    private String cadenaEspecialidadesSeleccionados(HashMap especialidadesMap) {
		String especialidades="";
		for(int i=0; i<Utilidades.convertirAEntero(especialidadesMap.get("numRegistros").toString()); i++){
			if(especialidadesMap.get("eliminado_"+i).toString().equals(ConstantesBD.acronimoNo)){
				especialidades += especialidadesMap.get("codigoEspecialidad_"+i)+", ";
			}
		}
		especialidades += ConstantesBD.codigoNuncaValido;
		logger.info("CONVENIOS - "+especialidades);
		return especialidades;
	}
    
    /**
	 * 
	 * @param con
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @return
	 */
	private ActionForward accionAdicionarGrupoServicio(Connection con, ActionMapping mapping, EstadisticasServiciosForm forma, UsuarioBasico usuario) {
		
		if (!forma.getFiltros("grupo").equals("")){
			// Adicionar el convenio
			forma.getGruposServiciosMap().put("codigoGrupo_"+forma.getGruposServiciosMap("numRegistros"), ((HashMap)forma.getGrupos().get(Utilidades.convertirAEntero(forma.getFiltros("grupo")+""))).get("codigo"));
			forma.getGruposServiciosMap().put("nombreGrupo_"+forma.getGruposServiciosMap("numRegistros"), ((HashMap)forma.getGrupos().get(Utilidades.convertirAEntero(forma.getFiltros("grupo")+""))).get("descripcion"));
			forma.getGruposServiciosMap().put("eliminado_"+forma.getGruposServiciosMap("numRegistros"), ConstantesBD.acronimoNo);
			forma.getGruposServiciosMap().put("posArray_"+forma.getGruposServiciosMap("numRegistros"), forma.getFiltros("grupo"));
			forma.setGruposServiciosMap("numRegistros", Utilidades.convertirAEntero(forma.getGruposServiciosMap("numRegistros").toString())+1);
			// "Quitar" el convenio del select
			HashMap aux = (HashMap)forma.getEspecialidades().get(Utilidades.convertirAEntero(forma.getFiltros("grupo")+""));
			aux.put("seleccionado", ConstantesBD.acronimoSi);
		}
		UtilidadBD.closeConnection(con);
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
	private ActionForward accionEliminarGrupoServicio(Connection con,ActionMapping mapping, EstadisticasServiciosForm forma,UsuarioBasico usuario) {
		forma.setGruposServiciosMap("eliminado_"+forma.getPosMap(), ConstantesBD.acronimoSi);
		HashMap aux = (HashMap)forma.getGrupos().get(Utilidades.convertirAEntero(forma.getGruposServiciosMap("posArray_"+forma.getPosMap())+""));
		aux.put("seleccionado", ConstantesBD.acronimoNo);
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}
	
	 /**
     * 
     * @param conveniosMap
     * @return
     */
    private String cadenaGruposServiciosSeleccionados(HashMap gruposServiciosMap) {
		String grupos="";
		for(int i=0; i<Utilidades.convertirAEntero(gruposServiciosMap.get("numRegistros").toString()); i++){
			if(gruposServiciosMap.get("eliminado_"+i).toString().equals(ConstantesBD.acronimoNo)){
				grupos += gruposServiciosMap.get("codigoGrupo_"+i)+", ";
			}
		}
		grupos += ConstantesBD.codigoNuncaValido;
		logger.info("GRUPOS - "+grupos);
		return grupos;
	}
    
	/**
     * 
     * @param con
     * @param mapping
     * @param forma
     * @param usuario
     * @return
     */
    private ActionForward accionGenerarReporte(Connection con, ActionMapping mapping, EstadisticasServiciosForm forma, UsuarioBasico usuario, HttpServletRequest request) {
    	
    	if (forma.getFiltros("tipoReporte").equals(ConstantesBDManejoPaciente.tipoReporteServiciosRealizados+"")){
    		forma.setFiltros("convenio", cadenaConveniosSeleccionados(forma.getConveniosMap()));
    		forma.setFiltros("especialidad", cadenaEspecialidadesSeleccionados(forma.getEspecialidadesMap()));
    		forma.setFiltros("grupo", cadenaGruposServiciosSeleccionados(forma.getGruposServiciosMap()));
    		generarServiciosRealizados(con, forma, usuario, request);
    	}	
    	if (forma.getFiltros("tipoReporte").equals(ConstantesBDManejoPaciente.tipoReporteServiciosRealizadosXConvenio+"")){
    		forma.setFiltros("convenio", cadenaConveniosSeleccionados(forma.getConveniosMap()));
    		generarServiciosRealizadosXConvenio(con, forma, usuario, request);
    	}	
    	if (forma.getFiltros("tipoReporte").equals(ConstantesBDManejoPaciente.tipoReporteServiciosRealizadosXEspecialidad+"")){
    		forma.setFiltros("especialidad", cadenaEspecialidadesSeleccionados(forma.getEspecialidadesMap()));
    		forma.setFiltros("grupo", cadenaGruposServiciosSeleccionados(forma.getGruposServiciosMap()));
    		generarServiciosRealizadosXEspecialidad(con, forma, usuario, request);
    	}	
    	
    	UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}

	private void generarServiciosRealizadosXEspecialidad(Connection con,EstadisticasServiciosForm forma, UsuarioBasico usuario,HttpServletRequest request) {
		String nombreRptDesign = "ServiciosRealizadosXEspecialidad.rptdesign";
		EstadisticasServicios mundo = new EstadisticasServicios();
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
	    v.add("\nLISTADO DE SERVICIOS REALIZADOS POR ESPECIALIDAD\nPeriodo: ["+forma.getFiltros("fechaInicial")+" - "+forma.getFiltros("fechaFinal")+"]");
	    comp.insertLabelInGridOfMasterPage(0,1,v);
	    
	    // Parametros de Generación
	    comp.insertGridHeaderOfMasterPageWithName(1,0,1,1,"param");
	    v=new Vector();
	   
    	// Centro de Atención
    	String filtroo="[Centro de Atención: "+Utilidades.obtenerNombreCentroAtencion(con, Utilidades.convertirAEntero(forma.getFiltros("centroAtencion").toString()))+"]  ";
    	
    	// Via de ingreso - tipo paciente
    	logger.info("numViaIngresoTipoPaciente ---> "+forma.getFiltros("numViaIngresoTipoPaciente").toString());
    	if (Integer.parseInt(forma.getFiltros("numViaIngresoTipoPaciente").toString())>0){
    		for(int i=0; i<Integer.parseInt(forma.getFiltros("numViaIngresoTipoPaciente").toString()); i++){
    			logger.info("viaIngresoTipoPaciente_ ---> "+forma.getFiltros("viaIngresoTipoPaciente_"+i));
    			if(forma.getFiltros("viaIngresoTipoPaciente_"+i).equals(ConstantesBD.acronimoSi)){
    				if(i==0)
        				filtroo+="[Vía de Ingreso - Tipo Paciente: ";
    				filtroo+=((HashMap)forma.getViaIngresoTipoPaciente().get(i)).get("viaingresotipopac")+", ";
    				viaIngreso+=((HashMap)forma.getViaIngresoTipoPaciente().get(i)).get("codvia")+", ";
    				tipoPaciente+="'"+((HashMap)forma.getViaIngresoTipoPaciente().get(i)).get("codtipopac")+"', ";
    			}
    		}
    		filtroo+="]  ";
    		viaIngreso+=ConstantesBD.codigoNuncaValido+"";
    		tipoPaciente+=" '"+ConstantesBD.codigoNuncaValido+"'";
    	}
    	
    	// Tipo de Servicio
    	if(forma.getFiltros().containsKey("tipoServicio"))
    		if (!forma.getFiltros("tipoServicio").equals(""))
    			filtroo+="[Tipo de Servicio: "+forma.getFiltros("tipoServicio").toString().split(ConstantesBD.separadorSplit)[1]+"]  ";
    	
    	// Especialidad
		if (forma.getFiltros().containsKey("especialidad") && !forma.getFiltros("especialidad").equals("") && !forma.getFiltros("especialidad").equals(ConstantesBD.codigoNuncaValido+"")){
			int especialidad;
			filtroo+="[Especialidad: ";
			for(int i=0; i<Utilidades.convertirAEntero(forma.getEspecialidadesMap("numRegistros").toString()); i++){
				if(forma.getEspecialidadesMap("eliminado_"+i).toString().equals(ConstantesBD.acronimoNo)){
					filtroo +=  forma.getEspecialidadesMap("nombreEspecialidad_"+i)+", ";
				}
			}
			filtroo += "] ";
		}
    	
		// Grupo de servicio
		if (forma.getFiltros().containsKey("grupo") && !forma.getFiltros("grupo").equals("") && !forma.getFiltros("grupo").equals(ConstantesBD.codigoNuncaValido+"")){
			int especialidad;
			filtroo+="[Grupo(s) de Servicio: ";
			for(int i=0; i<Utilidades.convertirAEntero(forma.getGruposServiciosMap("numRegistros").toString()); i++){
				if(forma.getGruposServiciosMap("eliminado_"+i).toString().equals(ConstantesBD.acronimoNo)){
					filtroo +=  forma.getGruposServiciosMap("nombreGrupo_"+i)+", ";
				}
			}
			filtroo += "] ";
		}
    	
    	v.add(filtroo);
	    
    	comp.insertLabelInGridOfMasterPageWithProperties(1, 0, v, DesignChoiceConstants.TEXT_ALIGN_LEFT);
	    
	    // Fecha hora de proceso y usuario
	    comp.insertLabelInGridPpalOfFooter(0,0,"Fecha: "+UtilidadFecha.getFechaActual()+" Hora: "+UtilidadFecha.getHoraActual());
	    comp.insertLabelInGridPpalOfFooter(0,1,"Usuario: "+usuario.getLoginUsuario());
	    //******************
		
	    //***************** NUEVO WHERE DEL REPORTE
	    
	    mundo.setFiltrosMap(forma.getFiltros());
	    mundo.setViasDeIngreso(viaIngreso);
	    mundo.setTiposPaciente(tipoPaciente);
	    mundo.setUsuario(usuario.getLoginUsuario());
	    
	    // Registro generación del reporte en BD
	    UtilidadesManejoPaciente.insertarLogReportes(con, ConstantesBDManejoPaciente.tipoReporteServiciosRealizadosXEspecialidad, usuario.getLoginUsuario(),usuario.getCodigoInstitucionInt());
	    
	    //***************** NUEVO WHERE DEL REPORTE
	    String where = mundo.crearWhereServiciosRealizadosXEspecialidad(con, mundo);
	    
	    //***************** VALOR DEL CAMPO "egresosPorMes"
	    String egresosPorMes = mundo.crearCampoEgresosPorMes(con, mundo);
		
	   comp.obtenerComponentesDataSet("dataSet");
	   
	   //****************** MODIFICAR CONSULTA
	   String newquery=comp.obtenerQueryDataSet().replaceAll("1=1", where);
	   comp.modificarQueryDataSet(newquery);
	   String newquery1=comp.obtenerQueryDataSet().replaceAll("2=2", egresosPorMes);
	   comp.modificarQueryDataSet(newquery1);
	   
	   logger.info("Query >>>"+newquery1);
	   
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

	private void generarServiciosRealizadosXConvenio(Connection con,EstadisticasServiciosForm forma, UsuarioBasico usuario,HttpServletRequest request) {
		String nombreRptDesign = "ServiciosRealizadosXConvenio.rptdesign";
		EstadisticasServicios mundo = new EstadisticasServicios();
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
	    v.add("\nLISTADO DE SERVICIOS REALIZADOS POR CONVENIO\nPeriodo: ["+forma.getFiltros("fechaInicial")+" - "+forma.getFiltros("fechaFinal")+"]");
	    comp.insertLabelInGridOfMasterPage(0,1,v);
	    
	    // Parametros de Generación
	    comp.insertGridHeaderOfMasterPageWithName(1,0,1,1,"param");
	    v=new Vector();
	   
    	// Centro de Atención
    	String filtroo="[Centro de Atención: "+Utilidades.obtenerNombreCentroAtencion(con, Utilidades.convertirAEntero(forma.getFiltros("centroAtencion").toString()))+"]  ";
    	
    	// Via de ingreso - tipo paciente
    	boolean mostrarLabel=true;
    	logger.info("numViaIngresoTipoPaciente ---> "+forma.getFiltros("numViaIngresoTipoPaciente").toString());
    	if (Integer.parseInt(forma.getFiltros("numViaIngresoTipoPaciente").toString())>0){
    		for(int i=0; i<Integer.parseInt(forma.getFiltros("numViaIngresoTipoPaciente").toString()); i++){
    			logger.info("viaIngresoTipoPaciente_ ---> "+forma.getFiltros("viaIngresoTipoPaciente_"+i));
    			if(forma.getFiltros("viaIngresoTipoPaciente_"+i).equals(ConstantesBD.acronimoSi)){
    				if(mostrarLabel){
        				filtroo+="[Vía de Ingreso - Tipo Paciente: ";
        				mostrarLabel=false;
    				}	
    				filtroo+=((HashMap)forma.getViaIngresoTipoPaciente().get(i)).get("viaingresotipopac")+", ";
    				viaIngreso+=((HashMap)forma.getViaIngresoTipoPaciente().get(i)).get("codvia")+", ";
    				tipoPaciente+="'"+((HashMap)forma.getViaIngresoTipoPaciente().get(i)).get("codtipopac")+"', ";
    			}
    		}
    		filtroo+="]  ";
    		viaIngreso+=ConstantesBD.codigoNuncaValido+"";
    		tipoPaciente+=" '"+ConstantesBD.codigoNuncaValido+"'";
    	}
    	
    	// Tipo de Servicio
    	if(forma.getFiltros().containsKey("tipoServicio"))
    		if (!forma.getFiltros("tipoServicio").equals(""))
    			filtroo+="[Tipo de Servicio: "+forma.getFiltros("tipoServicio").toString().split(ConstantesBD.separadorSplit)[1]+"]  ";
    	
    	// Convenio
		if (forma.getFiltros().containsKey("convenio") && !forma.getFiltros("convenio").equals("") && !forma.getFiltros("convenio").equals(ConstantesBD.codigoNuncaValido+"")){
			int convenio;
			filtroo+="[Convenio: ";
			for (int i=0; i<forma.getFiltros("convenio").toString().split(", ").length; i++){
				convenio = Integer.parseInt(forma.getFiltros("convenio").toString().split(", ")[i]);
				if (convenio != ConstantesBD.codigoNuncaValido)
					filtroo +=  Utilidades.obtenerNombreConvenioOriginal(con, convenio)+", ";
			}
			filtroo += "] ";
		}
    	
    	// sexo
    	if(forma.getFiltros().containsKey("sexo"))
    		if (!forma.getFiltros("sexo").equals(""))
    			filtroo+="[Sexo: "+forma.getFiltros("sexo").toString().split(ConstantesBD.separadorSplit)[1]+"]  ";
    	
    	v.add(filtroo);
	    
    	comp.insertLabelInGridOfMasterPageWithProperties(1, 0, v, DesignChoiceConstants.TEXT_ALIGN_LEFT);
	    
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
	    String where = mundo.crearWhereServiciosRealizadosXConvenio(con, mundo);
	  
	    // Registro generación del reporte en BD
	    UtilidadesManejoPaciente.insertarLogReportes(con, ConstantesBDManejoPaciente.tipoReporteServiciosRealizadosXConvenio, usuario.getLoginUsuario(),usuario.getCodigoInstitucionInt());
		
	   comp.obtenerComponentesDataSet("dataSet");
	   String newquery=comp.obtenerQueryDataSet().replaceAll("1=1", where);
	   
	   if(System.getProperty("TIPOBD").equals("ORACLE")){
		   newquery=newquery.replaceAll("2=2", " IS NOT NULL");
	   }else{
		   if(System.getProperty("TIPOBD").equals("POSTGRESQL")){
			   newquery=newquery.replaceAll("2=2", " <> ''");
		   }
	   }
	   
	   logger.info("Query >>>"+newquery);
	   comp.modificarQueryDataSet(newquery);
	   //debemos arreglar los alias para que funcione oracle, debido a que este los convierte a MAYUSCULAS
      	comp.lowerAliasDataSet();
		String newPathReport = comp.saveReport1(false);
	   comp.updateJDBCParameters(newPathReport);
	   newPathReport+="&i=0";
	   
	   if(!newPathReport.equals(""))
	    {
	    	request.setAttribute("isOpenReport", "true");
	    	request.setAttribute("newPathReport", newPathReport);
	    }
	}

	private void generarServiciosRealizados(Connection con, EstadisticasServiciosForm forma, UsuarioBasico usuario, HttpServletRequest request){
	
		String nombreRptDesign = "ServiciosRealizados.rptdesign";
		EstadisticasServicios mundo = new EstadisticasServicios();
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
	    v.add("\nLISTADO DE SERVICIOS REALIZADOS\nPeriodo: ["+forma.getFiltros("fechaInicial")+" - "+forma.getFiltros("fechaFinal")+"]");
	    comp.insertLabelInGridOfMasterPage(0,1,v);
	    
	    // Parametros de Generación
	    comp.insertGridHeaderOfMasterPageWithName(1,0,1,1,"param");
	    v=new Vector();
	   
    	// Centro de Atención
    	String filtroo="[Centro de Atención: "+Utilidades.obtenerNombreCentroAtencion(con, Utilidades.convertirAEntero(forma.getFiltros("centroAtencion").toString()))+"]  ";
    	
    	// Via de ingreso - tipo paciente
    	boolean mostrarLabel=true;
    	logger.info("numViaIngresoTipoPaciente ---> "+forma.getFiltros("numViaIngresoTipoPaciente").toString());
    	if (Integer.parseInt(forma.getFiltros("numViaIngresoTipoPaciente").toString())>0){
    		for(int i=0; i<Integer.parseInt(forma.getFiltros("numViaIngresoTipoPaciente").toString()); i++){
    			logger.info("viaIngresoTipoPaciente_ ---> "+forma.getFiltros("viaIngresoTipoPaciente_"+i));
    			if(forma.getFiltros("viaIngresoTipoPaciente_"+i).equals(ConstantesBD.acronimoSi)){
    				if(mostrarLabel){
        				filtroo+="[Vía de Ingreso - Tipo Paciente: ";
        				mostrarLabel=false;
    				}	
    				filtroo+=((HashMap)forma.getViaIngresoTipoPaciente().get(i)).get("viaingresotipopac")+", ";
    				viaIngreso+=((HashMap)forma.getViaIngresoTipoPaciente().get(i)).get("codvia")+", ";
    				tipoPaciente+="'"+((HashMap)forma.getViaIngresoTipoPaciente().get(i)).get("codtipopac")+"', ";
    			}
    		}
    		filtroo+="]  ";
    		viaIngreso+=ConstantesBD.codigoNuncaValido+"";
    		tipoPaciente+=" '"+ConstantesBD.codigoNuncaValido+"'";
    	}
    	
    	// Tipo de Servicio
    	if(forma.getFiltros().containsKey("tipoServicio"))
    		if (!forma.getFiltros("tipoServicio").equals(""))
    			filtroo+="[Tipo de Servicio: "+forma.getFiltros("tipoServicio").toString().split(ConstantesBD.separadorSplit)[1]+"]  ";
    	
    	// Especialidad
		if (forma.getFiltros().containsKey("especialidad") && !forma.getFiltros("especialidad").equals("") && !forma.getFiltros("especialidad").equals(ConstantesBD.codigoNuncaValido+"")){
			int especialidad;
			filtroo+="[Especialidad: ";
			for(int i=0; i<Utilidades.convertirAEntero(forma.getEspecialidadesMap("numRegistros").toString()); i++){
				if(forma.getEspecialidadesMap("eliminado_"+i).toString().equals(ConstantesBD.acronimoNo)){
					filtroo +=  forma.getEspecialidadesMap("nombreEspecialidad_"+i)+", ";
				}
			}
			filtroo += "] ";
		}
    	
		// Grupo de servicio
		if (forma.getFiltros().containsKey("grupo") && !forma.getFiltros("grupo").equals("") && !forma.getFiltros("grupo").equals(ConstantesBD.codigoNuncaValido+"")){
			int especialidad;
			filtroo+="[Grupo(s) de Servicio: ";
			for(int i=0; i<Utilidades.convertirAEntero(forma.getGruposServiciosMap("numRegistros").toString()); i++){
				if(forma.getGruposServiciosMap("eliminado_"+i).toString().equals(ConstantesBD.acronimoNo)){
					filtroo +=  forma.getGruposServiciosMap("nombreGrupo_"+i)+", ";
				}
			}
			filtroo += "] ";
		}
    	
    	// Convenio
		if (forma.getFiltros().containsKey("convenio") && !forma.getFiltros("convenio").equals("") && !forma.getFiltros("convenio").equals(ConstantesBD.codigoNuncaValido+"")){
			int convenio;
			filtroo+="[Convenio: ";
			for (int i=0; i<forma.getFiltros("convenio").toString().split(", ").length; i++){
				convenio = Integer.parseInt(forma.getFiltros("convenio").toString().split(", ")[i]);
				if (convenio != ConstantesBD.codigoNuncaValido)
					filtroo +=  Utilidades.obtenerNombreConvenioOriginal(con, convenio)+", ";
			}
			filtroo += "] ";
		}
    	
    	v.add(filtroo);
	    
    	comp.insertLabelInGridOfMasterPageWithProperties(1, 0, v, DesignChoiceConstants.TEXT_ALIGN_LEFT);
	    
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
	    String where = mundo.crearWhereServiciosRealizados(con, mundo);
	    
	 // Registro generación del reporte en BD
	    UtilidadesManejoPaciente.insertarLogReportes(con, ConstantesBDManejoPaciente.tipoReporteServiciosRealizados, usuario.getLoginUsuario(),usuario.getCodigoInstitucionInt());
		
	   comp.obtenerComponentesDataSet("dataSet");
	   String newquery=comp.obtenerQueryDataSet().replaceAll("1=1", where);
	   
	   if(System.getProperty("TIPOBD").equals("ORACLE")){
		   newquery=newquery.replaceAll("2=2", " IS NOT NULL");
	   }else{
		   if(System.getProperty("TIPOBD").equals("POSTGRESQL")){
			   newquery=newquery.replaceAll("2=2", " <> ''");
		   }
	   }
	   
	   logger.info("Query >>>"+newquery);
	   comp.modificarQueryDataSet(newquery);
	   //debemos arreglar los alias para que funcione oracle, debido a que este los convierte a MAYUSCULAS
      	comp.lowerAliasDataSet();
		String newPathReport = comp.saveReport1(false);
	   comp.updateJDBCParameters(newPathReport);        
	   newPathReport+="&i=0";
	   
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
    private ActionForward accionCambiaTipoReporte(Connection con, ActionMapping mapping, EstadisticasServiciosForm forma, UsuarioBasico usuario) {
    	UtilidadBD.closeConnection(con);
    	if(!forma.getFiltros("tipoReporte").toString().equals("")){
	    	int tipoReporte = Integer.parseInt(forma.getFiltros("tipoReporte").toString());
	    	forma.setFiltros(new HashMap());
	    	forma.setFiltros("tipoReporte",tipoReporte);
	    	forma.setFiltros("numViaIngresoTipoPaciente",forma.getViaIngresoTipoPaciente().size());
			forma.setFiltros("centroAtencion", usuario.getCodigoCentroAtencion());
			
			// Inicializar mapa de convenios seleccionados
			HashMap convenios = new HashMap();
	    	convenios.put("numRegistros", 0);
			forma.setConveniosMap(convenios);
			
			// Inicializar mapa de especialidades seleccionadas
			HashMap especialidades = new HashMap();
			especialidades.put("numRegistros", 0);
			forma.setEspecialidadesMap(especialidades);
			
			// Inicializar mapa de grupos de servicios seleccionados
			HashMap grupos = new HashMap();
			grupos.put("numRegistros", 0);
			forma.setGruposServiciosMap(grupos);
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
	private ActionForward accionEmpezar(Connection con, ActionMapping mapping, EstadisticasServiciosForm forma, UsuarioBasico usuario) {
		forma.reset();
		HashMap mapAux = new HashMap();
		String tiposServicio= "'"+ConstantesBD.codigoServicioProcedimiento+"','"+ConstantesBD.codigoServicioQuirurgico+"','"+ConstantesBD.codigoServicioNoCruentos+"','"+ConstantesBD.codigoServicioPaquetes+"','"+ConstantesBD.codigoServicioInterconsulta+"'";
		forma.setCentrosAtencion(UtilidadesManejoPaciente.obtenerCentrosAtencion(con, usuario.getCodigoInstitucionInt(),""));
		forma.setViaIngresoTipoPaciente(Utilidades.obtenerViasIngresoTipoPaciente(con));
		forma.setSexos(Utilidades.obtenerSexos(con));
		forma.setTiposServicio(UtilidadesFacturacion.obtenerTiposServicio(con, tiposServicio, ""));
		mapAux.put("institucion", usuario.getCodigoInstitucion());
		mapAux.put("activo", "true");
		forma.setFiltros("numViaIngresoTipoPaciente",forma.getViaIngresoTipoPaciente().size());
		forma.setFiltros("centroAtencion", usuario.getCodigoCentroAtencion());
		
		// Cargar Grupos de Servicios
		forma.setGrupos(Utilidades.obtenerGrupoServicios(con, mapAux));
		for(int i=0; i<forma.getGrupos().size(); i++){
			HashMap aux = (HashMap)forma.getGrupos().get(i);
			aux.put("seleccionado", ConstantesBD.acronimoNo);
		}
		
		// Cargar Convenios
		forma.setConvenios(Utilidades.obtenerConvenios(con, "", "", true, "", true));
		for(int i=0; i<forma.getConvenios().size(); i++){
			HashMap aux = (HashMap)forma.getConvenios().get(i);
			aux.put("seleccionado", ConstantesBD.acronimoNo);
		}
		
		//Cargar Especialidades
		forma.setEspecialidades(Utilidades.obtenerEspecialidadesEnArray(con, ConstantesBD.codigoNuncaValido, ConstantesBD.codigoNuncaValido));
		for(int i=0; i<forma.getEspecialidades().size(); i++){
			HashMap aux = (HashMap)forma.getEspecialidades().get(i);
			aux.put("seleccionado", ConstantesBD.acronimoNo);
		}
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}
}