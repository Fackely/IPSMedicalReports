package com.princetonsa.action.interfaz;

import java.sql.Connection;
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
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.CsvFile;
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.reportes.ConsultasBirt;

import com.princetonsa.actionform.interfaz.ReporteMovTipoDocForm;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.interfaz.ReporteMovTipoDoc;
import com.princetonsa.util.birt.reports.DesignEngineApi;
import com.princetonsa.util.birt.reports.ExtractCSV;
import com.princetonsa.util.birt.reports.ParamsBirtApplication;

/**
 * @author Jairo Gómez Fecha Junio de 2009
 */
public class ReporteMovTipoDocAction extends Action {

	/**
	 * Objeto para manejar los logs de esta clase
	 */
	Logger logger = Logger.getLogger(ReporteMovTipoDocAction.class);
	
	private ReportDesignHandle reportDesignHandle;
	
	private TableHandle tableHandle;

	/**
	 * Método execute del Action
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {


		Connection connection = null;
		try{

			if (form instanceof ReporteMovTipoDocForm) {


				connection = UtilidadBD.abrirConexion();

				// se verifica si la conexion esta nula
				if (connection == null) {
					// de ser asi se envia a una pagina de error.
					request.setAttribute("CodigoDescripcionError","erros.problemasBd");
					return mapping.findForward("paginaError");
				}

				// se obtiene el usuario cargado en sesion.
				UsuarioBasico usuario = (UsuarioBasico) request.getSession().getAttribute("usuarioBasico");

				// se obtiene el usuario cargado en sesion.
				PersonaBasica paciente = (PersonaBasica) request.getSession().getAttribute("pacienteActivo");

				// obtenemos el valor de la forma.
				ReporteMovTipoDocForm forma = (ReporteMovTipoDocForm) form;

				// obtenemos el estado que contiene la forma.
				String estado = forma.getEstado();

				logger.info("\n\n***************************************************************************");
				logger.info("EL ESTADO DE ReporteMovTipoDocForm ES ====>> "+ forma.getEstado());
				logger.info("\n***************************************************************************");

				if (estado == null) {
					//				se asigana un error a mostar por ser un estado invalido
					request.setAttribute("CodigoDescripcionError","errors.estadoInvalido");
					//				se cierra la conexion con la BD
					UtilidadBD.cerrarConexion(connection);
					//				se retorna el error al forward paginaError.
					return mapping.findForward("paginaError");

				} 
				else {
					//				se evaluan los estados
					if (estado.equals("empezar")) 
					{
						forma.reset();
						UtilidadBD.closeConnection(connection);
						return mapping.findForward("busqueda");

					} else if (estado.equals("buscar")) 
					{
						if(accionValidate(mapping, request, forma))
						{
							return accionBuscar(connection, forma, response, mapping, request);
						}
						else
						{
							//forma.reset();
							UtilidadBD.closeConnection(connection);
							return mapping.findForward("busqueda");
						}

					}else if (estado.equals("imprimirGenerarArchivo")) 
					{
						return accionImprimirGenerarArchivo(connection, forma, mapping, usuario, request);

					}
				}
			}
		}catch (Exception e) {
			Log4JManager.error(e);
		}
		finally{
			UtilidadBD.closeConnection(connection);
		}
		return null;
}

	private ActionForward accionImprimirGenerarArchivo(Connection con, ReporteMovTipoDocForm forma, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request) 
	{
		String nombreRptDesign = "MovTotalesTipoDoc.rptdesign";
		InstitucionBasica ins = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
		
		Vector v;
		
		//***************** INFORMACIÓN DEL CABEZOTE
        DesignEngineApi comp; 
        comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"interfaz/",nombreRptDesign);
         
        // Logo
        comp.insertImageHeaderOfMasterPage1(0, 0, ins.getLogoReportes());
        
        // Nombre Institución, titulo y rango de fechas
        comp.insertGridHeaderOfMasterPageWithName(0,1,1,2, "titulo");
        v=new Vector();
        v.add(ins.getRazonSocial());
        v.add("NIT: "+ins.getNit()+"\n\nDirección: " + ins.getDireccion() +
        		"\nTeléfono: " + ins.getTelefono() +
        		"\n\nREPORTE MOVIMIENTOS TOTALES POR TIPO DE DOCUMENTO");
        comp.insertLabelInGridOfMasterPage(0,1,v);
        
        // Parametros de Generación
        comp.insertGridHeaderOfMasterPageWithName(1,0,1,1,"param");
        v=new Vector();
        
        String filtro="Parametros de Generación:";
        
        // Fechas
        if(!forma.getCriterios(forma.getIndicesCriterios()[0]).equals("") && !forma.getCriterios(forma.getIndicesCriterios()[1]).equals(""))
        	filtro += "  Fecha Inicial: "+forma.getCriterios(forma.getIndicesCriterios()[0])+"  Fecha Final: "+forma.getCriterios(forma.getIndicesCriterios()[1]);
        
        boolean vacio = false, factPaciente = false, factVarias = false, recCaja = false;
		for(int i = 0; i < forma.getTipoDocumento().length; i++){
			if (forma.getTipoDocumento()[i].equals(ConstantesIntegridadDominio.acronimoFacturasPaciente)){
				factPaciente = true;
			}else if (forma.getTipoDocumento()[i].equals(ConstantesIntegridadDominio.acronimoFacturasVarias)){
				factVarias = true;
			}else if (forma.getTipoDocumento()[i].equals(ConstantesIntegridadDominio.acronimoRecivoCaja)){
				recCaja = true;
			}
		}
        logger.info("tamanio selecciobn impresion:::::"+forma.getTipoDocumento().length );
        // Consecutivo Facturas
        if(forma.getTipoDocumento().length > 0){
        	String filtroTipo = "  Tipo Documento: ";
        	if(factPaciente){
        		if(filtroTipo.equals("  Tipo Documento: "))
        			filtroTipo += "Facturas Pacientes";
        		else
        			filtroTipo += "- Facturas Pacientes";
        	}
        	if(factVarias){
        		if(filtroTipo.equals("  Tipo Documento: "))
        			filtroTipo += "Facturas Varias";
        		else
        			filtroTipo += "- Facturas Varias";
        	}
        	if(recCaja){
        		if(filtroTipo.equals("  Tipo Documento: "))
        			filtroTipo += "Recibos de Caja";
        		else
        			filtroTipo += "- Recibos de Caja";
        	}
        	filtro += filtroTipo;
        }
        
        v.add(filtro);
        comp.insertLabelInGridOfMasterPageWithProperties(1,0,v,DesignChoiceConstants.TEXT_ALIGN_LEFT);
        
        // Fecha hora de proceso y usuario
//        comp.insertLabelInGridPpalOfFooter(0,0,"Fecha: "+UtilidadFecha.getFechaActual()+" Hora: "+UtilidadFecha.getHoraActual());
        comp.insertLabelInGridPpalOfFooter(0,0,"Usuario: "+usuario.getLoginUsuario());
        	        
        //***************** NUEVO WHERE DEL REPORTE
       
        String newquery = "";
        
        if (forma.getTipoDocumento().length > 0)
        {
        	if (factPaciente)
        	{
//        		obtenerComponentesGridBody("tablaRecibosCaja", comp.getReportDesignHandle());
//        		obtenerComponentesGridBody("tablaFacturasVarias", comp.getReportDesignHandle());
        		
//        		Asignación query a los dataSet requeridos
        		
//        		facturasPacientes
        		comp.obtenerComponentesDataSet("facturasPacientes");
        	    newquery = ConsultasBirt.listadoFacturasPacientes(con, forma.getCriterios());
        	    comp.modificarQueryDataSet(newquery);
        	    
//        	    ingresos
        		comp.obtenerComponentesDataSet("ingresos");
        		newquery = ConsultasBirt.listadoIngresos(con, forma.getCriterios());
        	    comp.modificarQueryDataSet(newquery);
        	}
        	else{
        		obtenerComponentesGridBody("tablaPacientes", comp.getReportDesignHandle());
        		obtenerComponentesGridBody("tablaIngresos", comp.getReportDesignHandle());
        	}
        	if (factVarias)
        	{
//        		obtenerComponentesGridBody("tablaPacientes", comp.getReportDesignHandle());
//        		obtenerComponentesGridBody("tablaIngresos", comp.getReportDesignHandle());
//        		obtenerComponentesGridBody("tablaRecibosCaja", comp.getReportDesignHandle());
        		
//        		Asignación query a los dataSet requeridos
        		
//        		facturasVarias
        		comp.obtenerComponentesDataSet("facturasVarias");
        	    newquery = ConsultasBirt.listadoFacturasVarias(con, forma.getCriterios());
        	    comp.modificarQueryDataSet(newquery);
        	}
        	else{
        		obtenerComponentesGridBody("tablaFacturasVarias", comp.getReportDesignHandle());
        	}
        	if (recCaja)
        	{
//        		obtenerComponentesGridBody("tablaPacientes", comp.getReportDesignHandle());
//        		obtenerComponentesGridBody("tablaIngresos", comp.getReportDesignHandle());
//        		obtenerComponentesGridBody("tablaFacturasVarias", comp.getReportDesignHandle());
        		
//        		Asignación query a los dataSet requeridos
        		
//        		recibosCaja
        		comp.obtenerComponentesDataSet("recibosCaja");
        	    newquery = ConsultasBirt.listadoRecibosCaja(con, forma.getCriterios());
        	    comp.modificarQueryDataSet(newquery);
        	}else{
        		obtenerComponentesGridBody("tablaRecibosCaja", comp.getReportDesignHandle());
        	}
        }
       
       logger.info("\n\n valor de la sql "+comp.obtenerQueryDataSet());
              
       //debemos arreglar los alias para que funcione oracle, debido a que este los convierte a MAYUSCULAS
      	comp.lowerAliasDataSet();
		String newPathReport = comp.saveReport1(false);
       comp.updateJDBCParameters(newPathReport);
       logger.info("NEWPATHREPORT------>>>"+newPathReport);
       if(!newPathReport.equals(""))
        {
        	request.setAttribute("isOpenReport", "true");
        	request.setAttribute("newPathReport", newPathReport);
        }
       
//       ******************* GENERAR ARCHIVO PLANO
       if(forma.getCriterios(forma.getIndicesCriterios()[3]).toString().equals(ConstantesIntegridadDominio.acronimoTipoSalidaArchivo))
	    {
	    	String encabezado = "Reporte: Movimientos Totales Por Tipo Documento\n" +
	    						filtro+"\n";
	    							    	
		   String nombre=CsvFile.armarNombreArchivo("MovimientosTotalesPorTipoDocumento", usuario);
		   ResultadoBoolean resultado = ExtractCSV.extraerArchivoCsvConEncabezadoComprimido(newPathReport, nombre, encabezado);
		   if(resultado.isTrue())
	       {
	       		String[] rutas = resultado.getDescripcion().split(ConstantesBD.separadorSplit);
	       		forma.setUrlArchivoPlano(rutas[0]);
	       		forma.setPathArchivoPlano(rutas[2]);
	       		logger.info("setUrlArchivoPlano"+forma.getUrlArchivoPlano());
	       		logger.info("setPathArchivoPlano"+forma.getPathArchivoPlano());
	       }
	       else
	       {
	       		ActionErrors errores = new ActionErrors();
	       		errores.add("", new ActionMessage("errors.notEspecific",resultado.getDescripcion()));
	       		saveErrors(request, errores);
	       }
		}
             
       UtilidadBD.closeConnection(con);
       return mapping.findForward("principal");
	}
	
	public void obtenerComponentesGridBody(String gridName,ReportDesignHandle report)
    {
        String tmp = null;
        SlotHandle sloth = report.getBody();
        
        java.util.Iterator iter=sloth.iterator();        
        while (iter.hasNext())
        {
            tableHandle = (TableHandle) iter.next();
            tmp=tableHandle.getName();
            if(tmp.equals(gridName))
            {
            	try {
					tableHandle.dropAndClear();
				} catch (SemanticException e) {
					e.printStackTrace();
				}
            }
        }        
    }

	private ActionForward accionBuscar(Connection con,
			ReporteMovTipoDocForm forma, 
			HttpServletResponse response, ActionMapping mapping, HttpServletRequest request) {
//		Instancia mundo
		ReporteMovTipoDoc mundo = new ReporteMovTipoDoc();
		boolean vacio = false, factPaciente = false, factVarias = false, recCaja = false;
		for(int i = 0; i < forma.getTipoDocumento().length; i++){
			if (forma.getTipoDocumento()[i].equals(ConstantesIntegridadDominio.acronimoFacturasPaciente)){
				factPaciente = true;
			}else if (forma.getTipoDocumento()[i].equals(ConstantesIntegridadDominio.acronimoFacturasVarias)){
				factVarias = true;
			}else if (forma.getTipoDocumento()[i].equals(ConstantesIntegridadDominio.acronimoRecivoCaja)){
				recCaja = true;
			}
		}
//		carga de los arreglos con las consultas según el tipo de documento seleccionado
		if (vacio)
		{
//			se realizan las consultas
			forma.setArrayFacturasPacientes(mundo.consultarFacturasPacientes(con, forma.getCriterios()));
			forma.setArrayIngresos(mundo.consultarIngresos(con, forma.getCriterios()));
			forma.setArrayRecibosCaja(mundo.consultarRecibosCaja(con, forma.getCriterios()));
			forma.setArrayFacturasVarias(mundo.consultarFacturasVarias(con, forma.getCriterios()));
//			se asignan los tamaños del resultado de cada arreglo
			forma.setTamanioArrayFacturasPacientes(forma.getArrayFacturasPacientes().size());
			forma.setTamanioArrayIngresos(forma.getArrayIngresos().size());
			forma.setTamanioArrayRecibosCaja(forma.getArrayRecibosCaja().size());
			forma.setTamanioArrayFacturasVarias(forma.getArrayFacturasVarias().size());
//			se asigna el mayor tamanio a la variable tamanioPacientesIngresos para sabe si se muestra o no los campos
			if (forma.getTamanioArrayFacturasPacientes() > forma.getTamanioArrayIngresos())
				forma.setTamanioPacientesIngresos(forma.getTamanioArrayFacturasPacientes());
			else
				forma.setTamanioPacientesIngresos(forma.getTamanioArrayIngresos());
		}
		if (factPaciente)
		{
//			se realizan las consultas
			forma.setArrayFacturasPacientes(mundo.consultarFacturasPacientes(con, forma.getCriterios()));
			forma.setArrayIngresos(mundo.consultarIngresos(con, forma.getCriterios()));
//			se asignan los tamaños del resultado de cada arreglo
			forma.setTamanioArrayFacturasPacientes(forma.getArrayFacturasPacientes().size());
			forma.setTamanioArrayIngresos(forma.getArrayIngresos().size());
//			se asigna el mayor tamanio a la variable tamanioPacientesIngresos para sabe si se muestra o no los campos
			if (forma.getTamanioArrayFacturasPacientes() > forma.getTamanioArrayIngresos())
				forma.setTamanioPacientesIngresos(forma.getTamanioArrayFacturasPacientes());
			else
				forma.setTamanioPacientesIngresos(forma.getTamanioArrayIngresos());
		}if (factVarias)
		{
//			se realizan las consultas
			forma.setArrayFacturasVarias(mundo.consultarFacturasVarias(con, forma.getCriterios()));
//			se asignan los tamaños del resultado de cada arreglo
			forma.setTamanioArrayFacturasVarias(forma.getArrayFacturasVarias().size());
		}if (recCaja)
		{
//			se realizan las consultas
			forma.setArrayRecibosCaja(mundo.consultarRecibosCaja(con, forma.getCriterios()));
//			se asignan los tamaños del resultado de cada arreglo
			forma.setTamanioArrayRecibosCaja(forma.getArrayRecibosCaja().size());
		}
		
		UtilidadBD.closeConnection(con);
		
		if (forma.getTamanioArrayFacturasPacientes() > 0 || forma.getTamanioArrayFacturasVarias() > 0 ||
					forma.getTamanioArrayIngresos() > 0 || forma.getTamanioArrayRecibosCaja() > 0)
		{
			return mapping.findForward("principal");
		}
		else
		{
			forma.reset();
			ActionErrors errores = new ActionErrors();
			errores.add("Eror Busqueda", new ActionMessage("prompt.generico", "No se Obtuvo Resultados de Busqueda"));
			saveErrors(request, errores);
			return mapping.findForward("busqueda");
		}
		
	}

	private boolean accionValidate(ActionMapping mapping, HttpServletRequest request, ReporteMovTipoDocForm forma) 
	{
		ActionErrors errores = new ActionErrors();
		if (forma.getEstado().equals("buscar")) {
			
			if (!forma.getCriterios(forma.getIndicesCriterios()[0]).equals("") && !forma.getCriterios(forma.getIndicesCriterios()[0]).equals(""))
			{
				int diferenciaFechas = ConstantesBD.codigoNuncaValido;
				
				if (!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(forma.getCriterios(forma.getIndicesCriterios()[0]).toString(),UtilidadFecha.getFechaActual())) 
				{
					errores.add("Campo Fecha Inicial ", new ActionMessage("errors.fechaPosteriorIgualActual", "Inicial ", "Actual "));
				}
				
				if (!UtilidadFecha.esFechaMenorIgualQueOtraReferencia( forma.getCriterios(forma.getIndicesCriterios()[1]).toString(), UtilidadFecha.getFechaActual())) 
				{
					errores.add("Campo Fecha Final", new ActionMessage("errors.fechaPosteriorIgualActual", "Final ", "Actual "));
				}
				
				if (UtilidadFecha.esFechaMenorQueOtraReferencia(forma.getCriterios(forma.getIndicesCriterios()[1]).toString(), forma.getCriterios(forma.getIndicesCriterios()[0]).toString())) 
				{
					errores.add("Campo Fecha Final requerido", new ActionMessage("errors.fechaAnteriorIgualActual", "Final ", "Inicial "));
				}
				
				if (errores.isEmpty())
				{
					diferenciaFechas = UtilidadFecha.numeroDiasEntreFechas(forma.getCriterios(forma.getIndicesCriterios()[0]).toString(), forma.getCriterios(forma.getIndicesCriterios()[1]).toString());
					
					if(diferenciaFechas > 90)
					{logger.info("entre a la fecha 90 dias");
						errores.add("Campo Fecha Final", new ActionMessage("errors.fechaSuperaOtraPorDias", "Inicial ", "90", "Final "));
					}					
					else if (diferenciaFechas >= 0)
					{
						if (forma.getTipoDocumento().length == 0)
						{
							errores.add("Campo Tipo Documento requerido", new ActionMessage("errors.required", "El Tipo Documento "));
						}
					}
					
				}
				
				
			}
			else
			{
				if (forma.getCriterios(forma.getIndicesCriterios()[0]).equals("")) 
				{
					errores.add("Campo Fecha Inicial requerido", new ActionMessage("errors.required", "La Fecha Inicial "));
				}
				
				if (forma.getCriterios(forma.getIndicesCriterios()[1]).equals("")) 
				{
					errores.add("Campo Fecha Final requerido", new ActionMessage("errors.required", "La Fecha Final "));
				} 
			}
			if (forma.getCriterios(forma.getIndicesCriterios()[3]).equals(""))
			{
				errores.add("Campo Tipo Salida requerido", new ActionMessage("errors.required", "El Tipo Salida "));
			}
		}
		saveErrors(request, errores);
		if(errores.isEmpty())
			return true;
		else
			return false;
	}	
}