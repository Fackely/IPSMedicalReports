package com.princetonsa.action.carteraPaciente;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Random;
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

import util.BackUpBaseDatos;
import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.ResultadoBoolean;
import util.TxtFile;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.UtilidadFileUpload;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.reportes.ConsultasBirt;

import com.princetonsa.actionform.carteraPaciente.ReportePagosCarteraPacienteForm;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.carteraPaciente.ReportePagosCarteraPaciente;
import com.princetonsa.util.birt.reports.DesignEngineApi;
import com.princetonsa.util.birt.reports.ParamsBirtApplication;


public class ReportePagosCarteraPacienteAction extends Action
{
	private Logger logger = Logger.getLogger(ReportePagosCarteraPacienteAction.class);
	
	public ActionForward execute(   ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response ) throws Exception
			{	
		Connection con = null;
		try{
			if(form instanceof ReportePagosCarteraPacienteForm)
			{


				//se obtiene el usuario cargado en sesion.
				UsuarioBasico usuario = (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");

				//se obtiene el paciente cargado en sesion.
				PersonaBasica paciente = (PersonaBasica)request.getSession().getAttribute("pacienteActivo");

				//se obtiene la institucion
				InstitucionBasica institucion = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");

				//se instancia la forma
				ReportePagosCarteraPacienteForm forma = (ReportePagosCarteraPacienteForm)form;		

				//se instancia el mundo
				ReportePagosCarteraPaciente mundo = new ReportePagosCarteraPaciente();

				//optenemos el estado que contiene la forma.
				String estado = forma.getEstado();

				//se instancia la variable para manejar los errores.
				ActionErrors errores=new ActionErrors();

				forma.setMensaje(new ResultadoBoolean(false));

				//se instancia la variable para manejar los errores.


				logger.info("\n\n***************************************************************************");
				logger.info(" 	  EL ESTADO DE REPORTE PAGOS CARTERA PACIENTE ES ====>> "+estado);
				logger.info("\n***************************************************************************");

				// ESTADO --> NULL
				if(estado == null)
				{
					forma.reset(usuario.getCodigoInstitucionInt());
					request.setAttribute("CodigoDescripcionError","Errors.estadoInvalido");

					return mapping.findForward("paginaError");
				}			
				else if(estado.equals("empezar"))
				{
					return this.accionEmpezar(con, forma, mundo, mapping, usuario,request);					   			
				}
				else if(estado.equals("buscar"))				
				{
					return this.accionBuscar(con, forma, mundo, mapping, usuario,request);
				}
				else if(estado.equals("cambioTipoP"))
				{
					return mapping.findForward("principal");
				}
				else if(estado.equals("imprimirReporte"))
				{
					return this.accionImprimirReporte(con, forma, mapping, usuario, request);
				}
				else if(estado.equals("generarArchivo"))
				{
					return this.accionGenerarArchivo(con, forma, mundo, mapping, usuario, request);
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
	 * Accion Generar Archivo
	 * @param con
	 * @param forma
	 * @param mapping
	 * @param usuario
	 * @param request 
	 * @return
	 */
	private ActionForward accionGenerarArchivo(Connection con, ReportePagosCarteraPacienteForm forma, ReportePagosCarteraPaciente mundo, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request) 
	{
		boolean OperacionTrue=false,existeTxt=false;
		Random r = new Random();
		int ban=ConstantesBD.codigoNuncaValido;
		
		con= UtilidadBD.abrirConexion();
		
		//arma el nombre del archivo
		String nombreReport="Reporte_Pagos_Cartera_Paciente"+"-"+UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual())+r.nextInt();
		//se genera el documento con la informacion
	
		String path=ValoresPorDefecto.getFilePath()+System.getProperty("file.separator");
		String url="../upload"+System.getProperty("file.separator");
		String txt = mundo.plano(con, forma, usuario);
			 	
		if (UtilidadCadena.noEsVacio(txt))
				OperacionTrue=TxtFile.generarTxt(new StringBuffer(txt) , nombreReport,path,".csv");
		
			if (OperacionTrue)
			{
				//se genera el archivo en formato Zip
				ban=BackUpBaseDatos.EjecutarComandoSO("zip -j "+path+nombreReport+".zip -j"+" "+path+nombreReport+".csv");
				//se ingresa la direccion donde se almaceno el archivo
				forma.setRuta(path+nombreReport+".csv");
				//se ingresa la ruta para poder descargar el archivo
				forma.setUrlArchivo(url+nombreReport+".zip");	
				//se valida si existe el txt
				existeTxt=UtilidadFileUpload.existeArchivo(path, nombreReport+".csv");
				//se valida si existe el zip
				forma.setExisteArchivo(UtilidadFileUpload.existeArchivo(path, nombreReport+".zip"));
			}
			if (existeTxt )
				forma.setOperacionTrue(true);
		
		return mapping.findForward("principal");
	}
	
	/**
	 * @param con
	 * @param forma
	 * @param mapping
	 * @param usuario
	 * @param request 
	 * @return
	 */
	private ActionForward accionImprimirReporte(Connection con, ReportePagosCarteraPacienteForm forma, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request) 
	{
		con= UtilidadBD.abrirConexion();
		
		String nombreRptDesign = "reportePagosCarteraPaciente.rptdesign";
		InstitucionBasica ins = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
		
		Vector v;
		
		//***************** INFORMACIÓN DEL CABEZOTE
        DesignEngineApi comp; 
        comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"carteraPaciente/",nombreRptDesign);
         
        // Logo
        comp.insertImageHeaderOfMasterPage1(0, 0, ins.getLogoReportes());
        
        // Nombre Institución, titulo y rango de fechas
        comp.insertGridHeaderOfMasterPageWithName(0,1,1,2, "titulo");
        v=new Vector();
        v.add("HOSPITAL "+ins.getRazonSocial());
        v.add("NIT: "+ins.getNit()+"\n\nReporte Pagos Cartera Paciente");
        comp.insertLabelInGridOfMasterPage(0,1,v);
        
        // Parametros de Generación
        comp.insertGridHeaderOfMasterPageWithName(1,0,1,1,"param");
        v=new Vector();
        
        String filtro="Parametros de Generación:";
                    
        if(!forma.getTipoDoc().equals("-1"))
        {
        	if(forma.getTipoDoc().equals(ConstantesIntegridadDominio.acronimoTipoDocumentoLetra))
        		filtro += " Tipo Documento: Letra";
        
        	else if(forma.getTipoDoc().equals(ConstantesIntegridadDominio.acronimoTipoDocumentoPagare))
	    		filtro += " Tipo Documento: Pagare";
        }
        	
        if(!forma.getTipoPeriodo().equals("-1"))
        {
        	if(forma.getTipoPeriodo().equals(ConstantesIntegridadDominio.acronimoTipoPeriodoMensual))
        		filtro += "  Tipo Periodo: Mensual";     
        	else if(forma.getTipoPeriodo().equals(ConstantesIntegridadDominio.acronimoTipoPeriodoAnual))
        		filtro += "  Tipo Periodo: Anual";   
        }
      
        if(forma.getTipoPeriodo().equals(ConstantesIntegridadDominio.acronimoTipoPeriodoMensual))
        {
	        if(!forma.getFechaIni().equals("") && !forma.getFechaFin().equals(""))
	        	filtro += "  Fecha Inicial: "+forma.getFechaIni()+"  Fecha Final: "+forma.getFechaFin();
        }
        else if(forma.getTipoPeriodo().equals(ConstantesIntegridadDominio.acronimoTipoPeriodoAnual))
        {
        	if(forma.getAnioIni() > 0 && forma.getAnioFin() > 0)
        		filtro += "  Año Inicial: "+forma.getAnioIni()+"  Año Final: "+forma.getAnioFin();
        }        
        
        v.add(filtro);
        comp.insertLabelInGridOfMasterPageWithProperties(1,0,v,DesignChoiceConstants.TEXT_ALIGN_LEFT);
        
        // Fecha hora de proceso y usuario
        comp.insertLabelInGridPpalOfFooter(0,0,"Fecha: "+UtilidadFecha.getFechaActual()+" Hora: "+UtilidadFecha.getHoraActual());
        comp.insertLabelInGridPpalOfFooter(0,1,"Usuario: "+usuario.getLoginUsuario());
        	        
        //***************** NUEVO WHERE DEL REPORTE
       comp.obtenerComponentesDataSet("documentos");
       String newquery = ConsultasBirt.reportePagosCarteraPaciente(
				UtilidadFecha.conversionFormatoFechaABD(forma.getFechaIni()), 
				UtilidadFecha.conversionFormatoFechaABD(forma.getFechaFin()), 
				forma.getAnioIni(),
				forma.getAnioFin(),
				forma.getCentroAtencion(), 
				forma.getTipoDoc(), 
				forma.getTipoPeriodo());
       
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
             
       UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}

	/**
     * @param forma
     * @param con
     * @param mapping
     * @return
	 * @throws SQLException 
     */
	private ActionForward accionBuscar(Connection con, ReportePagosCarteraPacienteForm forma, ReportePagosCarteraPaciente mundo, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request)
	{		
		forma.setListaDatosFinanciacion(mundo.consultarDocumentos(forma.getCentroAtencion(),forma.getTipoDoc()));
		
		HashMap<String, Object> aplicacionPagos= new HashMap<String, Object>();
		
		for(int i=0;i<(forma.getListaDatosFinanciacion().size());i++)
		{								
			aplicacionPagos= mundo.consultarAplicPagos(forma.getListaDatosFinanciacion().get(i).getCodigoPk(),forma.getFechaIni(),forma.getFechaFin(),forma.getAnioIni(),forma.getAnioFin(), forma.getTipoPeriodo());
			forma.getListaAplicPagosCar().add(aplicacionPagos);
		}	
		
		
		int l=0;
		
		for(int i=0;i<(forma.getListaAplicPagosCar().size());i++)
		{		
			double valorMes=0, valorAnio=0;;
			String mesAnt="", anioAnt="";
			
			for(int k=0;k<(Utilidades.convertirAEntero(forma.getListaAplicPagosCar().get(i).get("numRegistros")+""));k++)
			{		
				int j= k-1;
				if(k > 0)
				{			
					mesAnt=(forma.getListaAplicPagosCar().get(i).get("fecha_"+j)+"").substring(5,7);
					String mes=(forma.getListaAplicPagosCar().get(i).get("fecha_"+k)+"").substring(5,7);
					anioAnt=(forma.getListaAplicPagosCar().get(i).get("fecha_"+j)+"").substring(0,4);
					String anio=(forma.getListaAplicPagosCar().get(i).get("fecha_"+k)+"").substring(0, 4);
					if(forma.getTipoPeriodo().equals(ConstantesIntegridadDominio.acronimoTipoPeriodoMensual))
					{											
						if(mesAnt.equals(mes))
							valorMes+=Utilidades.convertirADouble(forma.getListaAplicPagosCar().get(i).get("valor_"+k)+"");
					}
					else if(forma.getTipoPeriodo().equals(ConstantesIntegridadDominio.acronimoTipoPeriodoAnual))
					{		
						if(anioAnt.equals(anio))
							valorAnio+=Utilidades.convertirADouble(forma.getListaAplicPagosCar().get(i).get("valor_"+k)+"");			
					}					
				}	
				else
				{
					if(forma.getTipoPeriodo().equals(ConstantesIntegridadDominio.acronimoTipoPeriodoMensual))
					{
						valorMes+=Utilidades.convertirADouble(forma.getListaAplicPagosCar().get(i).get("valor_"+k)+"");
					}
					else if(forma.getTipoPeriodo().equals(ConstantesIntegridadDominio.acronimoTipoPeriodoAnual))
					{	
						valorAnio+=Utilidades.convertirADouble(forma.getListaAplicPagosCar().get(i).get("valor_"+k)+"");	
					}
				}
					
				forma.setPagos("datosfin_"+l, Utilidades.convertirAEntero(forma.getListaAplicPagosCar().get(i).get("datosfin_"+k)+""));				
			}
			if(forma.getTipoPeriodo().equals(ConstantesIntegridadDominio.acronimoTipoPeriodoMensual))
			{
				if(valorMes > 0)
				{
					forma.setPagos("valor_"+l, valorMes);
					forma.setPagos("fecha_"+l, UtilidadFecha.obtenerNombreMes(Utilidades.convertirAEntero(mesAnt)));				
					l++;
				}
			}
			else if(forma.getTipoPeriodo().equals(ConstantesIntegridadDominio.acronimoTipoPeriodoAnual))
			{
				if(valorAnio > 0)
				{
					forma.setPagos("valor_"+l, valorAnio);
					forma.setPagos("fecha_"+l, Utilidades.convertirAEntero(anioAnt));				
					l++;
				}
			}
		}
		
		forma.setPagos("numRegistros", l);
		
		for(int m=0;m<l;m++)
		{
			logger.info("\n\nfecha: "+forma.getPagos("fecha_"+m));
			logger.info("\n\nvalor: "+forma.getPagos("valor_"+m));
		}
				
		return mapping.findForward("principal");
	}
	
	/**
     * @param forma
     * @param con
     * @param mapping
     * @return
	 * @throws SQLException 
     */
	private ActionForward accionEmpezar(Connection con, ReportePagosCarteraPacienteForm forma, ReportePagosCarteraPaciente mundo, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request)
	{
		forma.reset(usuario.getCodigoInstitucionInt());
				
		forma.setCentrosAtencion(Utilidades.obtenerCentrosAtencionInactivos(usuario.getCodigoInstitucionInt(), false));
		
		forma.setEstado("empezar");
				
		return mapping.findForward("principal");
	}	
}