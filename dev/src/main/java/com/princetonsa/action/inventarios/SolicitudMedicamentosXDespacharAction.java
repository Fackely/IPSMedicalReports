package com.princetonsa.action.inventarios;

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
import org.apache.struts.action.ActionMessage;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.CsvFile;
import util.Listado;
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.Utilidades;
import util.historiaClinica.UtilidadesHistoriaClinica;
import util.manejoPaciente.UtilidadesManejoPaciente;

import com.princetonsa.actionform.inventarios.SolicitudMedicamentosXDespacharForm;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.inventarios.SolicitudMedicamentosXDespachar;
import com.princetonsa.mundo.manejoPaciente.EstadisticasIngresos;
import com.princetonsa.util.birt.reports.DesignEngineApi;
import com.princetonsa.util.birt.reports.ExtractCSV;
import com.princetonsa.util.birt.reports.ParamsBirtApplication;

/**
 * Clase para el manejo de los reportes
 * de solicitud medicamentos por despachar
 * Date: 2008-09-24
 * @author cgarias@princetonsa.com
 */
public class SolicitudMedicamentosXDespacharAction extends Action 
{
	
	/**
	 * logger 
	 * */
	Logger logger = Logger.getLogger(SolicitudMedicamentosXDespacharAction.class);
	
	/**
	 * Metodo execute del Action
	 */
    public ActionForward execute(   ActionMapping mapping,
            						ActionForm form,
            						HttpServletRequest request,
            						HttpServletResponse response ) throws Exception
            						{
    	Connection con = null;
    	try{
    		if(response==null);
    		if(form instanceof SolicitudMedicamentosXDespacharForm)
    		{

    			con = UtilidadBD.abrirConexion();

    			if(con == null)
    			{	
    				request.setAttribute("CodigoDescripcionError","errors.problemasBd");
    				return mapping.findForward("paginaError");
    			}
    			SolicitudMedicamentosXDespacharForm forma = (SolicitudMedicamentosXDespacharForm)form;
    			String estado = forma.getEstado();

    			ActionErrors errores = new ActionErrors();

    			logger.info("\n\n\n ESTADO (SolicitudMedicamentosXDespacharAction) ---> "+estado+"\n\n");

    			UsuarioBasico usuario = (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
    			PersonaBasica paciente = (PersonaBasica)request.getSession().getAttribute("pacienteActivo");
    			//se obtiene la institucion
    			InstitucionBasica ins = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");

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
    					return accionEmpezar(con, mapping, forma, usuario, paciente);
    				}
    				else
    					/*------------------------------
    					 * 		ESTADO > cambiarTipoReporte
    					 *-------------------------------*/
    					if(estado.equals("cambiarTipoReporte"))
    					{   
    						return accionCambiaTipoReporte(con, mapping, forma, usuario, paciente);
    					}
    					else
    						/*------------------------------
    						 * 		ESTADO > generarReporte
    						 *-------------------------------*/
    						if(estado.equals("generarReporte"))
    						{   
    							return accionGenerarReporte(con, mapping, forma, usuario, request, paciente, ins);
    						}
    			/*------------------------------
    			 * 		ESTADO > generarReporte
    			 *-------------------------------*/
    			if(estado.equals("cambiarCentroAtencion"))
    			{   
    				return cambiarCentroAtencion(con, mapping, forma, usuario, request);
    			}
    			/*------------------------------
    			 * 		ESTADO > generarReporte
    			 *-------------------------------*/
    			if(estado.equals("ordenarIngresarXPaciente"))
    			{   
    				return accionOrdenarIngresarXPaciente(con, mapping, forma);
    			}
    			/*------------------------------
    			 * 		ESTADO > generarReporte
    			 *-------------------------------*/
    			if(estado.equals("mostrarFiltroPorPaciente"))
    			{   
    				return accionMostrarFiltroPorPaciente(con, mapping, forma, usuario, request);
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
    
    private ActionForward accionMostrarFiltroPorPaciente(Connection con, ActionMapping mapping, SolicitudMedicamentosXDespacharForm forma, 
    		UsuarioBasico usuario, HttpServletRequest request)
    {
    	/* ***************************
    	 * INICIO Solución Tarea 61368
    	 * Felipe Pérez Granda
    	 * ***************************/
    	int codigoCentroAtencion = 001;
    	String nombreCentroAtencion = forma.getIngresosPorPacienteMap("nom_centro_atencion_"+forma.getPosMap())+"";
    	//codigoCentroAtencion = SolicitudMedicamentosXDespachar.obtenerCodigoCentroAtencion(con, nombreCentroAtencion);
    	codigoCentroAtencion = Utilidades.obtenerCodigoCentroAtencion(con, nombreCentroAtencion);
    	
    	logger.info("===> Entré a accionMostrarFiltroPorPaciente");
		logger.info("===> La posición es: "+forma.getPosMap());
		logger.info("===> Centro de Atención = "+nombreCentroAtencion);
		logger.info("===> Codigo Centro Atención = "+codigoCentroAtencion);

		/*
		 * Vamos a cargar los almacenes según el centro de atención seleccionado
		 */
		forma.setAlmacenes(Utilidades.obtenerCentrosCosto(con, 
				usuario.getCodigoInstitucionInt(), ConstantesBD.codigoTipoAreaSubalmacen+"", true, codigoCentroAtencion, false));
		
		/*
		 * Vamos a cargar los centros de costo según el centro de atención seleccionado
		 */
		forma.setCentrosCostoSolicitantes(Utilidades.obtenerCentrosCosto(con, 
				usuario.getCodigoInstitucionInt(), ConstantesBD.codigoTipoAreaDirecto+"", true, codigoCentroAtencion, false));
		
		/*
		 * Vamos a cargar los pisos según el centro de atención seleccionado
		 */
		forma.setPisos(UtilidadesManejoPaciente.obtenerPisos(con, usuario.getCodigoInstitucionInt(), codigoCentroAtencion));
		
		logger.info("===> Los almacenes son: "+forma.getAlmacenes());
		logger.info("===> Los centros de costo solicitantes son: "+forma.getCentrosCostoSolicitantes());
		logger.info("===> Los pisos son: "+forma.getPisos());
		
		/* ************************
    	 * FIN Solución Tarea 61368
    	 * Felipe Pérez Granda
    	 * ************************/
    	forma.setFiltros("cuenta", forma.getIngresosPorPacienteMap("num_cuenta_"+forma.getPosMap()));
    	UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}

	/**
     * 
     * @param forma 
     * @param con 
     * @param mapping 
     * @param forma
     */
	private ActionForward accionOrdenarIngresarXPaciente(Connection con, ActionMapping mapping, SolicitudMedicamentosXDespacharForm forma) {
		String[] indices = {"centro_atencion_", "nom_centro_atencion_", "via_ingreso_", "num_ingreso_", "fecha_ingreso_", "fecha_egreso_", "estado_ingreso_", "nom_estado_ingreso_", "num_cuenta_", "nom_estado_cuenta_", "estado_cuenta_",""};
		int numReg = Integer.parseInt(forma.getIngresosPorPacienteMap("numRegistros")+"");
		forma.setIngresosPorPacienteMap(Listado.ordenarMapa(indices, forma.getPatronOrdenar(), forma.getUltimoPatron(), forma.getIngresosPorPacienteMap(), numReg));
		forma.setUltimoPatron(forma.getPatronOrdenar());
		forma.setIngresosPorPacienteMap("numRegistros",numReg+"");
		forma.setIngresosPorPacienteMap("INDICES_MAPA",indices);
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("ingresos");
	}

    /**
     * 
     * @param con
     * @param mapping
     * @param forma
     * @param usuario
     * @param request
     * @return
     */
	private ActionForward cambiarCentroAtencion(Connection con, ActionMapping mapping, SolicitudMedicamentosXDespacharForm forma,UsuarioBasico usuario, HttpServletRequest request) {
		// Cargar los almacenes
		forma.setAlmacenes(Utilidades.obtenerCentrosCosto(con, usuario.getCodigoInstitucionInt(), ConstantesBD.codigoTipoAreaSubalmacen+"", true, Integer.parseInt(forma.getFiltros("centroAtencion").toString()), false));
		
		// Cargar centros de costo solicitante
		forma.setCentrosCostoSolicitantes(Utilidades.obtenerCentrosCosto(con, usuario.getCodigoInstitucionInt(), ConstantesBD.codigoTipoAreaDirecto+"", true, Integer.parseInt(forma.getFiltros("centroAtencion").toString()), false));
	
		// Cargar pisos
		forma.setPisos(UtilidadesManejoPaciente.obtenerPisos(con, usuario.getCodigoInstitucionInt(), Integer.parseInt(forma.getFiltros("centroAtencion").toString())));

		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}

	/**
     * Método encargado de generar los reportes correspondientes según sea:
     * Medicamentos por Despachar (1)
     * Medicamentos Despachados (2)
     * Medicamentos Despachados por Paciente (3)
     * @param con Connection
     * @param mapping ActionMapping
     * @param forma SolicitudMedicamentosXDespacharForm
     * @param usuario UsuarioBasico
	 * @param paciente PersonaBasica
     * @return ActionForward
     */
    private ActionForward accionGenerarReporte(
    		Connection con, 
    		ActionMapping mapping, 
    		SolicitudMedicamentosXDespacharForm forma, 
    		UsuarioBasico usuario, 
    		HttpServletRequest request, 
    		PersonaBasica paciente,
    		InstitucionBasica ins)
    {
    	if (forma.getFiltros("tipoReporte").equals("1"))
    		generarMedicamentosXDespachar(con, forma, usuario, request);
    	
    	if (forma.getFiltros("tipoReporte").equals("2"))
    		generarMedicamentosDespachados(con, forma, usuario, request, ins);
    	
    	if (forma.getFiltros("tipoReporte").equals("3"))
    		generarMedicamentosDespachadosXPaciente(con, forma, usuario, request, paciente);
    	
    	UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}
    
    /**
     * Método encargado de generar la solicitud de medicamentos despachados por paciente
     * @param con
     * @param forma
     * @param usuario
     * @param request
     * @param paciente
     */
    private void generarMedicamentosDespachadosXPaciente(Connection con, SolicitudMedicamentosXDespacharForm forma, UsuarioBasico usuario, 
    		HttpServletRequest request, PersonaBasica paciente)
    {
    	String nombreRptDesign = "MedicamentosDespachadosXPaciente.rptdesign";
		EstadisticasIngresos mundo = new EstadisticasIngresos();
		InstitucionBasica ins = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
		String viaIngreso="";
		String tipoPaciente="";
		Vector v;
		
		/* ***************************
		 * INICIO Solución Tarea 61455
		 * ***************************/
		
		//***************** INFORMACIÓN DEL CABEZOTE
	    DesignEngineApi comp;
	    comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"inventarios/",nombreRptDesign);
	     
	    // Logo
	    //comp.insertImageHeaderOfMasterPage1(0, 2, ins.getLogoReportes());
	    comp.insertImageHeaderOfMasterPage1(0, 2, ins.getLogoReportes());
	    
	    // Nombre Institución, titulo y rango de fechas
	    comp.insertGridHeaderOfMasterPageWithName(0,1,1,2, "titulo");
	    
	    v=new Vector();
	    //v.add(ins.getRazonSocial());
	    
	    /*
	     * numeroDeIngreso contiene el identificador de ingreso del paciente cargado en sesión
	     * con posMap, podemos saber que se paciente se trata
	     */
	    String numeroDeIngreso = forma.getIngresosPorPacienteMap("num_ingreso_"+forma.getPosMap()+"")+"";
	    logger.info("===> numeroDeIngreso = "+numeroDeIngreso);
	    
	    v.add(ins.getRazonSocial()+"\n"
	    		+Utilidades.getDescripcionTipoIdentificacion(con,ins.getTipoIdentificacion())+"  -  "+ins.getNit()+
	    		//"\nMEDICAMENTOS DESPACHADOS POR PACIENTE\nPeriodo: ["+forma.getFiltros("fechaInicial")+" - "+forma.getFiltros("fechaFinal")+
	    		"\nSOLICITUD MEDICAMENTOS DESPACHADOS POR PACIENTE"+
	    		//"]\nPaciente: "+paciente.getNombrePersona()+" Ingreso: "+paciente.getConsecutivoIngreso()
	    		"\nPaciente: "+paciente.getNombrePersona()+" \nIngreso: "+numeroDeIngreso+""
	    		);
	    //v.add("\nMEDICAMENTOS DESPACHADOS POR PACIENTE\nPeriodo: ["+forma.getFiltros("fechaInicial")+" - "+forma.getFiltros("fechaFinal")+"]\nPaciente: "+paciente.getNombrePersona()+" Ingreso: "+paciente.getConsecutivoIngreso());
	    comp.insertLabelInGridOfMasterPage(0,1,v);
		
		/*
		//***************** INFORMACIÓN DEL CABEZOTE
	    DesignEngineApi comp;
	    comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"inventarios/",nombreRptDesign);
	     
	    // Logo
	    comp.insertImageHeaderOfMasterPage1(0, 2, ins.getLogoReportes());
	    
	    // Nombre Institución, titulo y rango de fechas
	    comp.insertGridHeaderOfMasterPageWithName(0,1,1,2, "titulo");
	    
	    v=new Vector();
	    v.add(ins.getRazonSocial());
	    v.add("\nMEDICAMENTOS DESPACHADOS POR PACIENTE\nPeriodo: ["+forma.getFiltros("fechaInicial")+" - "+forma.getFiltros("fechaFinal")+"]\nPaciente: "+paciente.getNombrePersona()+" Ingreso: "+paciente.getConsecutivoIngreso());
	    comp.insertLabelInGridOfMasterPage(0,1,v);
	    */
	    
	    /* ************************
		 * FIN Solución Tarea 61455
		 * ************************/
	    
	    /* *********************************
	     * INICIO Aquí se arma el encabezado
	     * *********************************/
	    
	    String encabezado = "";
	    /*
	    logger.info("===> la posición del mapa es: "+forma.getPosMap());
	    logger.info("===> El numero de ingreso del paciente es: "+paciente.getConsecutivoIngreso());
	    logger.info("===> El codigo ingreso paciente es: "+paciente.getCodigoIngreso());
	    logger.info("===> El mapa IngresosPorPacienteMap = "+forma.getIngresosPorPacienteMap());
	    logger.info("===> El codigo ingreso paciente es: "+forma.getIngresosPorPacienteMap("num_ingreso_"+forma.getPosMap()+""));
	     */
	    
	    //logger.info("===> El numero de ingreso del paciente es: "+forma.getIngresosPorPacienteMap();
	    
	    /* ***************************
	     * INICIO Solución Tarea 62080
	     * ***************************/
	    
	    /*
	    //Razon social institucion
	    encabezado += (ins.getRazonSocial()+"\n");
		//Nit
	    encabezado += (Utilidades.getDescripcionTipoIdentificacion(con,ins.getTipoIdentificacion())+"  -  "+ins.getNit()+"\n");
	    */
	    
	    /* ************************
	     * FIN Solución Tarea 62080
	     * ************************/
	    
		//Titulo del reporte
	    encabezado += ("SOLICITUD MEDICAMENTOS DESPACHADOS POR PACIENTE \n\n");
	    //Información del paciente
	    encabezado += "Paciente: "+paciente.getNombrePersona()+" \nNumero de Ingreso: "+numeroDeIngreso+"\n";
	    
	    /* ******************************
	     * FIN Aquí se arma el encabezado
	     * ******************************/
	    
	    // Parametros de Generación
	    comp.insertGridHeaderOfMasterPageWithName(1,0,1,1,"param");
	    v=new Vector();
    	
    	// Almacen
    	String filtroo = "[Almacén: "+Utilidades.obtenerNombreCentroCosto(con, 
    			Utilidades.convertirAEntero(forma.getFiltros("almacen").toString()), usuario.getCodigoInstitucionInt())+"]     ";
    	
    	// Centro Costo Solicitante
    	if(forma.getFiltros().containsKey("centroCostoSolicitante"))
    		if(!forma.getFiltros("centroCostoSolicitante").toString().equals(""))
    			filtroo+="[Centro Costo Solicitante: "+Utilidades.obtenerNombreCentroCosto(con, 
    				Utilidades.convertirAEntero(forma.getFiltros("centroCostoSolicitante").toString()), 
    					usuario.getCodigoInstitucionInt())+"]     ";
    	
    	// Piso
    	if(forma.getFiltros().containsKey("piso"))
    		if(!forma.getFiltros("piso").toString().equals(""))
    			filtroo+="[Piso: "+Utilidades.obtenerNombrePiso(con, forma.getFiltros("piso").toString())+"]  ";
    	
    	// codigo axioma
		if (forma.getFiltros("tipoCodigo").toString().equals("axioma"))
			filtroo+="[Tipo Código: Axioma]     ";
		
		// codigo interfaz
		if (forma.getFiltros("tipoCodigo").toString().equals("interfaz"))
			filtroo+="[Tipo Código: Interfaz]     ";
			
		// ambos
		if (forma.getFiltros("tipoCodigo").toString().equals("ambos"))
			filtroo+="[Tipo Código: Axioma - Interfaz]     ";
		
		filtroo += "[Periodo: "+forma.getFiltros("fechaInicial")+" - "+forma.getFiltros("fechaFinal")+"]     ";
    	
    	v.add(filtroo);
	    
	    comp.insertLabelInGridOfMasterPage(1,0,v);
	    
	    // Fecha hora de proceso y usuario
	    comp.insertLabelInGridPpalOfFooter(0,1,"Fecha: "+UtilidadFecha.getFechaActual()+" Hora: "+UtilidadFecha.getHoraActual());
	    comp.insertLabelInGridPpalOfFooter(0,0,"Usuario: "+usuario.getLoginUsuario());
	    //******************

	   //****************** MODIFICAR CONSULTA
	   comp.obtenerComponentesDataSet("dataSet");
	   String newquery = SolicitudMedicamentosXDespachar.crearConsultaMedicamentosDespachadosXPaciente(con, forma.getFiltros());
	   comp.modificarQueryDataSet(newquery);
	   
	   logger.info("Query >>>"+newquery);
	   
	   /*
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
	   */
	   
	   /* ***************************
	    * INICIO Solución Tarea 61451
	    *****************************/
	   
	   //debemos arreglar los alias para que funcione oracle, debido a que este los convierte a MAYUSCULAS
      	comp.lowerAliasDataSet();
		String newPathReport = comp.saveReport1(false);
	   //comp.updateJDBCParameters(newPathReport);   
	   
	   //newPathReport+="&i=0";
	   if(!newPathReport.equals(""))
	   {
		   request.setAttribute("isOpenReport", "true");
		   request.setAttribute("newPathReport", newPathReport);
	   }
	   
	   
	   /* **************************************************************************
	    * INICIO Ejecutor de la nueva librería para la generación de archivos planos
	    ****************************************************************************/
	   comp.updateJDBCParameters(newPathReport);
	   encabezado += filtroo;
	   if(forma.getFiltros("tipoSalida"+"").equals(ConstantesIntegridadDominio.acronimoTipoSalidaArchivo))
	   {
		   logger.info("===> Estoy dentro de la generación del archivo plano para Medicamentos Despachados Por Paciente");
		   String nombre=CsvFile.armarNombreArchivo("ReporteMedicamentosDespachadosPorPaciente", usuario);
		   ResultadoBoolean resultado = ExtractCSV.extraerArchivoCsvConEncabezadoComprimido(newPathReport, nombre, encabezado);
		   if(resultado.isTrue())
	       	{
	       		//Se toman las rutas
	       		String[] rutas = resultado.getDescripcion().split(ConstantesBD.separadorSplit);
	       		//logger.info("===> la longitud del vector es: "+rutas.length);
	       		String nombreArchivoCsv= rutas[2];
	       		forma.setUrlArchivoPlano(rutas[0]);
	       		forma.setPathArchivoPlano(nombreArchivoCsv);
	       	}
	       	else
	       	{
	       		ActionErrors errores = new ActionErrors();
	       		errores.add("", new ActionMessage("errors.notEspecific",resultado.getDescripcion()));
	       		saveErrors(request, errores);
	       		//forma.setEstado("seleccionTipoReporte");
	       	}
	   }
	   /* ***********************************************************************
	    * FIN Ejecutor de la nueva librería para la generación de archivos planos
	    *************************************************************************/
	   
	   /* ************************
	    * FIN Solución Tarea 61451 
	    **************************/
	}

    /**
     * Método encargado de generar la solicitud de medicamentos despachados
     * @param con
     * @param forma
     * @param usuario
     * @param request
     * @param ins
     */
	private void generarMedicamentosDespachados(Connection con, SolicitudMedicamentosXDespacharForm forma, UsuarioBasico usuario, 
			HttpServletRequest request, InstitucionBasica ins)
	{
    	String nombreRptDesign = "MedicamentosDespachados.rptdesign";
		EstadisticasIngresos mundo = new EstadisticasIngresos();
		String viaIngreso="";
		String tipoPaciente="";
		Vector v;
		
		/* ***************************
		 * INICIO Solución Tarea 61455
		 * ***************************/
		
		//***************** INFORMACIÓN DEL CABEZOTE
	    DesignEngineApi comp; 
	    comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"inventarios/",nombreRptDesign);
	     
	    // Logo
	    //comp.insertImageHeaderOfMasterPage1(0, 2, ins.getLogoReportes());
	    comp.insertImageHeaderOfMasterPage1(0, 2, ins.getLogoReportes());
	    
	    // Nombre Institución, titulo y rango de fechas
	    comp.insertGridHeaderOfMasterPageWithName(0,1,1,2, "titulo");
	    v=new Vector();
	    v.add(ins.getRazonSocial()+
	    		"\n"+Utilidades.getDescripcionTipoIdentificacion(con,ins.getTipoIdentificacion())+"  -  "+ins.getNit()+
	    		"\nSOLICITUD MEDICAMENTOS DESPACHADOS\n");
	    comp.insertLabelInGridOfMasterPage(0,1,v);
	    
	    /* ************************
		 * FIN Solución Tarea 61455
		 * ************************/
		
		/*
		//***************** INFORMACIÓN DEL CABEZOTE
	    DesignEngineApi comp; 
	    comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"inventarios/",nombreRptDesign);
	     
	    // Logo
	    comp.insertImageHeaderOfMasterPage1(0, 2, ins.getLogoReportes());
	    
	    // Nombre Institución, titulo y rango de fechas
	    comp.insertGridHeaderOfMasterPageWithName(0,1,1,2, "titulo");
	    v=new Vector();
	    v.add(ins.getRazonSocial());
	    v.add("\nSOLICITUD MEDICAMENTOS DESPACHADOS\nPeriodo: ["+forma.getFiltros("fechaInicial")+" - "+forma.getFiltros("fechaFinal")+"]");
	    comp.insertLabelInGridOfMasterPage(0,1,v);
	    */
	    
	    /* *********************************
	     * INICIO Aquí se arma el encabezado
	     * *********************************/
	    
	    String encabezado = "";
	    
	    /* ***************************
	     * INICIO Solución Tarea 62080
	     * ***************************/
	    
	    /*
	    //Razon social institucion
	    encabezado += (ins.getRazonSocial()+"\n");
		//Nit
	    encabezado += (Utilidades.getDescripcionTipoIdentificacion(con,ins.getTipoIdentificacion())+"  -  "+ins.getNit()+"\n");
	    */
	    
	    /* ************************
	     * FIN Solución Tarea 62080
	     * ************************/
	    
		//Titulo del reporte
	    encabezado += ("SOLICITUD MEDICAMENTOS DESPACHADOS \n\n");
	    
	    /* ******************************
	     * FIN Aquí se arma el encabezado
	     * ******************************/
	    
	    // Parametros de Generación
	    comp.insertGridHeaderOfMasterPageWithName(1,0,1,1,"param");
	    v=new Vector();
	   
    	// Centro de Atención
    	String filtroo="[Centro de Atención: "+Utilidades.obtenerNombreCentroAtencion(con, Utilidades.convertirAEntero(
    			forma.getFiltros("centroAtencion").toString()))+"]    ";
    	
    	// Almacen
    	filtroo+="[Almacén: "+Utilidades.obtenerNombreCentroCosto(con,  Utilidades.convertirAEntero(forma.getFiltros("almacen").toString()), 
    			usuario.getCodigoInstitucionInt())+"]    ";
    	
    	// Centro Costo Solicitante
    	if(forma.getFiltros().containsKey("centroCostoSolicitante"))
    		if(!forma.getFiltros("centroCostoSolicitante").toString().equals(""))
    			filtroo+="[Centro Costo Solicitante: "+Utilidades.obtenerNombreCentroCosto(con, 
    				Utilidades.convertirAEntero(forma.getFiltros("centroCostoSolicitante").toString()), 
    					usuario.getCodigoInstitucionInt())+"]     ";
    	
    	// Piso
    	if(forma.getFiltros().containsKey("piso"))
    		if(!forma.getFiltros("piso").toString().equals(""))
    			filtroo+="[Piso: "+Utilidades.obtenerNombrePiso(con, forma.getFiltros("piso").toString())+"]     ";
    	
    	// codigo axioma
		if (forma.getFiltros("tipoCodigo").toString().equals("axioma"))
			filtroo+="[Tipo Código: Axioma]    ";
		
		// codigo interfaz
		if (forma.getFiltros("tipoCodigo").toString().equals("interfaz"))
			filtroo+="[Tipo Código: Interfaz]    ";
			
		// ambos
		if (forma.getFiltros("tipoCodigo").toString().equals("ambos"))
			filtroo+="[Tipo Código: Axioma - Interfaz]    ";
		
		filtroo += "[Periodo: "+forma.getFiltros("fechaInicial")+" - "+forma.getFiltros("fechaFinal")+"]     ";
    	
    	v.add(filtroo);
	    
	    comp.insertLabelInGridOfMasterPage(1,0,v);
	    
	    // Fecha hora de proceso y usuario
	    comp.insertLabelInGridPpalOfFooter(0,1,"Fecha: "+UtilidadFecha.getFechaActual()+" Hora: "+UtilidadFecha.getHoraActual());
	    comp.insertLabelInGridPpalOfFooter(0,0,"Usuario: "+usuario.getLoginUsuario());
	    //******************

	    //****************** MODIFICAR CONSULTA
	    comp.obtenerComponentesDataSet("dataSet");
	    String newquery = SolicitudMedicamentosXDespachar.crearConsultaMedicamentosXEntregar(con, forma.getFiltros());
	    comp.modificarQueryDataSet(newquery);
	    
	    logger.info("Query >>>"+newquery);
	    /*
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
	    */
	   
	    /* ***************************
	     * INICIO Solución Tarea 61451
	     * ***************************/
	   
	    //debemos arreglar los alias para que funcione oracle, debido a que este los convierte a MAYUSCULAS
      	comp.lowerAliasDataSet();
		String newPathReport = comp.saveReport1(false);
	    //comp.updateJDBCParameters(newPathReport);   
	    //newPathReport+="&i=0";
	    if(!newPathReport.equals(""))
	    {
	    	request.setAttribute("isOpenReport", "true");
	    	request.setAttribute("newPathReport", newPathReport);
	    }
	   
	    /* **************************************************************************
	     * INICIO Ejecutor de la nueva librería para la generación de archivos planos
	     * **************************************************************************/
	    
	    //se obtiene la institucionInstitucionBasica institucion = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
	    comp.updateJDBCParameters(newPathReport);
	    encabezado += filtroo;
	    if(forma.getFiltros("tipoSalida"+"").equals(ConstantesIntegridadDominio.acronimoTipoSalidaArchivo))
	    {
	    	logger.info("===> Estoy dentro de la generación del archivo plano para Medicamentos Despachados");
	    	String nombre=CsvFile.armarNombreArchivo("ReporteMedicamentosDespachados", usuario);
	    	ResultadoBoolean resultado = ExtractCSV.extraerArchivoCsvConEncabezadoComprimido(newPathReport, nombre, encabezado);
	    	
	    	if(resultado.isTrue())
	    	{
	    		//Se toman las rutas
	    		String[] rutas = resultado.getDescripcion().split(ConstantesBD.separadorSplit);
	    		forma.setUrlArchivoPlano(rutas[0]);
	    		forma.setPathArchivoPlano(rutas[2]);
	    	}
	    	else
	    	{
	    		ActionErrors errores = new ActionErrors();
	       		errores.add("", new ActionMessage("errors.notEspecific",resultado.getDescripcion()));
	       		saveErrors(request, errores);
	       		//forma.setEstado("seleccionTipoReporte");
	    	}
	    }
	   /* ***********************************************************************
	    * FIN Ejecutor de la nueva librería para la generación de archivos planos
	    *************************************************************************/

	    /* ************************
	    * FIN Solución Tarea 61451 
	    **************************/
	}

	/**
     * Método encargado de generar la solicitud de medicamentos por despachar
     * @param con
     * @param forma
     * @param usuario
     * @param request
     */
    private void generarMedicamentosXDespachar(Connection con,SolicitudMedicamentosXDespacharForm forma, UsuarioBasico usuario, 
    		HttpServletRequest request)
    {
    	String nombreRptDesign = "MedicamentosXDespachar.rptdesign";
		EstadisticasIngresos mundo = new EstadisticasIngresos();
		InstitucionBasica ins = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
		String viaIngreso="";
		String tipoPaciente="";
		Vector v;
		
		/* ***************************
		 * INICIO Solución Tarea 61455
		 * ***************************/
		
		//***************** INFORMACIÓN DEL CABEZOTE
	    DesignEngineApi comp; 
	    comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"inventarios/",nombreRptDesign);
	     
	    // Logo
	    //comp.insertImageHeaderOfMasterPage1(0, 2, ins.getLogoReportes());
	    comp.insertImageHeaderOfMasterPage1(0, 2, ins.getLogoReportes());
	    
	    // Nombre Institución, titulo y rango de fechas
	    comp.insertGridHeaderOfMasterPageWithName(0,1,1,2, "titulo");
	    v=new Vector();
	    v.add(ins.getRazonSocial()+
	    		"\n"+Utilidades.getDescripcionTipoIdentificacion(con,ins.getTipoIdentificacion())+"  -  "+ins.getNit()+
	    		"\nSOLICITUD MEDICAMENTOS POR DESPACHAR\n");
	    comp.insertLabelInGridOfMasterPage(0,1,v);
	    
	    /* ************************
		 * FIN Solución Tarea 61455
		 * ************************/
		
		/*
		//***************** INFORMACIÓN DEL CABEZOTE
	    DesignEngineApi comp; 
	    comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"inventarios/",nombreRptDesign);
	     
	    // Logo
	    comp.insertImageHeaderOfMasterPage1(0, 2, ins.getLogoReportes());
	    
	    // Nombre Institución, titulo y rango de fechas
	    comp.insertGridHeaderOfMasterPageWithName(0,1,1,2, "titulo");
	    v=new Vector();
	    v.add(ins.getRazonSocial());
	    v.add("\nSOLICITUD MEDICAMENTOS POR DESPACHAR\nPeriodo: ["+forma.getFiltros("fechaInicial")+" - "+forma.getFiltros("fechaFinal")+"]");
	    comp.insertLabelInGridOfMasterPage(0,1,v);
	    */
	    
	    /* *********************************
	     * INICIO Aquí se arma el encabezado
	     * *********************************/
	    
	    String encabezado = "";
	    
	    /* ***************************
	     * INICIO Solución Tarea 62080
	     * ***************************/
	    
	    /*
	    //Razon social institucion
	    encabezado += (ins.getRazonSocial()+"\n");
		//Nit
	    encabezado += (Utilidades.getDescripcionTipoIdentificacion(con,ins.getTipoIdentificacion())+"  -  "+ins.getNit()+"\n");
	    */
	    
	    /* ************************
	     * FIN Solución Tarea 62080
	     * ************************/
	    
		//Titulo del reporte
	    encabezado += ("SOLICITUD MEDICAMENTOS POR DESPACHAR \n\n");
	    
	    /* ******************************
	     * FIN Aquí se arma el encabezado
	     * ******************************/
	    
	    // Parametros de Generación
	    comp.insertGridHeaderOfMasterPageWithName(1,0,1,1,"param");
	    v=new Vector();
	   
    	// Centro de Atención
    	String filtroo="[Centro de Atención: "+Utilidades.obtenerNombreCentroAtencion(con, Utilidades.convertirAEntero(
    			forma.getFiltros("centroAtencion").toString()))+"]    ";
    	
    	// Almacen
    	filtroo+="[Almacén: "+Utilidades.obtenerNombreCentroCosto(con,  Utilidades.convertirAEntero(forma.getFiltros("almacen").toString()), 
    			usuario.getCodigoInstitucionInt())+"]    ";
    	
    	// Centro Costo Solicitante
    	if(forma.getFiltros().containsKey("centroCostoSolicitante"))
    		if(!forma.getFiltros("centroCostoSolicitante").toString().equals(""))
    			filtroo+="[Centro Costo Solicitante: "+Utilidades.obtenerNombreCentroCosto(con, 
    					Utilidades.convertirAEntero(forma.getFiltros("centroCostoSolicitante").toString()), 
    						usuario.getCodigoInstitucionInt())+"]    ";
    	
    	// Piso
    	if(forma.getFiltros().containsKey("piso"))
    		if(!forma.getFiltros("piso").toString().equals(""))
    			filtroo+="[Piso: "+Utilidades.obtenerNombrePiso(con, forma.getFiltros("piso").toString())+"]    ";
    	
    	// codigo axioma
		if (forma.getFiltros("tipoCodigo").toString().equals("axioma"))
			filtroo+="[Tipo Código: Axioma]    ";
		
		// codigo interfaz
		if (forma.getFiltros("tipoCodigo").toString().equals("interfaz"))
			filtroo+="[Tipo Código: Interfaz]    ";
			
		// ambos
		if (forma.getFiltros("tipoCodigo").toString().equals("ambos"))
			filtroo+="[Tipo Código: Axioma - Interfaz]    ";
		
		filtroo += "[Periodo: "+forma.getFiltros("fechaInicial")+" - "+forma.getFiltros("fechaFinal")+"]     ";
    	
    	v.add(filtroo);
	    
	    comp.insertLabelInGridOfMasterPage(1,0,v);
	    
	    // Fecha hora de proceso y usuario
	    comp.insertLabelInGridPpalOfFooter(0,1,"Fecha: "+UtilidadFecha.getFechaActual()+" Hora: "+UtilidadFecha.getHoraActual());
	    comp.insertLabelInGridPpalOfFooter(0,0,"Usuario: "+usuario.getLoginUsuario());
	    //******************
	   
	   //****************** MODIFICAR CONSULTA
	   comp.obtenerComponentesDataSet("dataSet");
	   String newquery = SolicitudMedicamentosXDespachar.crearConsultaMedicamentosXDespachar(con, forma.getFiltros());
	   comp.modificarQueryDataSet(newquery);
	   
	   logger.info("Query >>>"+newquery);
	   
	   //logger.info("===> Los filtros Son: "+forma.getFiltros());
	   
	   /* ***************************
	    * INICIO Solución Tarea 61451
	    *****************************/
	   
	   //debemos arreglar los alias para que funcione oracle, debido a que este los convierte a MAYUSCULAS
      	comp.lowerAliasDataSet();
		String newPathReport = comp.saveReport1(false);
	   //comp.updateJDBCParameters(newPathReport);   
	   
	   //newPathReport+="&i=0";
	   if(!newPathReport.equals(""))
	   {
		   request.setAttribute("isOpenReport", "true");
		   request.setAttribute("newPathReport", newPathReport);
	   }
	   
	   
	   /* **************************************************************************
	    * INICIO Ejecutor de la nueva librería para la generación de archivos planos
	    ****************************************************************************/
	   comp.updateJDBCParameters(newPathReport);
	   encabezado += filtroo;
	   if(forma.getFiltros("tipoSalida"+"").equals(ConstantesIntegridadDominio.acronimoTipoSalidaArchivo))
	   {
		   logger.info("===> Estoy dentro de la generación del archivo plano para Medicamentos Por Despachar");
		   String nombre=CsvFile.armarNombreArchivo("ReporteMedicamentosPorDespachar", usuario);
		   ResultadoBoolean resultado = ExtractCSV.extraerArchivoCsvConEncabezadoComprimido(newPathReport, nombre, encabezado);
		   if(resultado.isTrue())
	       	{
	       		//Se toman las rutas
	       		String[] rutas = resultado.getDescripcion().split(ConstantesBD.separadorSplit);
	       		forma.setUrlArchivoPlano(rutas[0]);
	       		forma.setPathArchivoPlano(rutas[2]);
	       	}
	       	else
	       	{
	       		ActionErrors errores = new ActionErrors();
	       		errores.add("", new ActionMessage("errors.notEspecific",resultado.getDescripcion()));
	       		saveErrors(request, errores);
	       		//forma.setEstado("seleccionTipoReporte");
	       	}
	   }
	   /* ***********************************************************************
	    * FIN Ejecutor de la nueva librería para la generación de archivos planos
	    *************************************************************************/
	   
	   /* ************************
	    * FIN Solución Tarea 61451 
	    **************************/
	}
    
	/**
     * Método encargado de modificar el tipo de reporte seleccionado por el usuario
     * @param con
     * @param mapping
     * @param forma
     * @param usuario
	 * @param paciente 
     * @return ActionForward
     */
    private ActionForward accionCambiaTipoReporte(Connection con, ActionMapping mapping, SolicitudMedicamentosXDespacharForm forma, 
    		UsuarioBasico usuario, PersonaBasica paciente)
    {
    	String forward="principal";
    	if (!forma.getFiltros("tipoReporte").toString().equals(""))
    	{
	    	int tipoReporte = Integer.parseInt(forma.getFiltros("tipoReporte").toString());
	    	forma.setFiltros(new HashMap());
	    	forma.setFiltros("tipoReporte",tipoReporte);
	    	forma.setFiltros("centroAtencion", usuario.getCodigoCentroAtencion());
    	}
    	if (forma.getFiltros("tipoReporte").toString().equals("3"))
    	{
    		forma.setIngresosPorPacienteMap(UtilidadesHistoriaClinica.consultarIngresosCuentaXPaciente(con, paciente.getCodigoPersona()));
    		forward="ingresos";
    	}
    	UtilidadBD.closeConnection(con);
		return mapping.findForward(forward);
	}

	/**
     * 
     * @param con
     * @param mapping
     * @param forma
     * @param usuario
	 * @param paciente 
     * @return
     */
	private ActionForward accionEmpezar(Connection con, ActionMapping mapping, SolicitudMedicamentosXDespacharForm forma, UsuarioBasico usuario, 
			PersonaBasica paciente)
	{
		forma.reset();
		
		// Cargar Centros de Atencion y postular el del usuario
		forma.setCentrosAtencion(UtilidadesManejoPaciente.obtenerCentrosAtencion(con, usuario.getCodigoInstitucionInt(),""));
		forma.setFiltros("centroAtencion", usuario.getCodigoCentroAtencion());

		// Cargar los almacenes
		forma.setAlmacenes(Utilidades.obtenerCentrosCosto(con, usuario.getCodigoInstitucionInt(), ConstantesBD.codigoTipoAreaSubalmacen+"", true, usuario.getCodigoCentroAtencion(), false));
		
		// Cargar centros de costo solicitante
		forma.setCentrosCostoSolicitantes(Utilidades.obtenerCentrosCosto(con, usuario.getCodigoInstitucionInt(), ConstantesBD.codigoTipoAreaDirecto+"", true, usuario.getCodigoCentroAtencion(), false));
		
		// Cargar pisos
		forma.setPisos(UtilidadesManejoPaciente.obtenerPisos(con, usuario.getCodigoInstitucionInt(), usuario.getCodigoCentroAtencion()));
		
		// Codigo del Paciente cargado
		forma.setCodPacienteCargado(paciente.getCodigoPersona());
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}
}