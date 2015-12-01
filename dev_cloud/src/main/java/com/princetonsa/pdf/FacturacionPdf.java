/*
 * Created on 17-feb-2005
 *
 * Jorge Armando Osorio Velasquez
 * Princeton
 */
package com.princetonsa.pdf;

import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;
import static net.sf.dynamicreports.report.builder.DynamicReports.report;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadN2T;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.facturacion.UtilidadesFacturacion;
import util.pdf.PdfReports;
import util.pdf.iTextBaseDocument;
import util.pdf.iTextBaseFont;
import util.pdf.iTextBaseTable;
import util.reportes.dinamico.GeneradorReporteDinamico;

import com.lowagie.text.BadElementException;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.ParamInstitucionDao;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.facturacion.DtoDatosRepsonsableFactura;
import com.princetonsa.dto.facturacion.DtoDatosResponsableFacturacion;
import com.princetonsa.dto.facturacion.DtoDetalleCirugiasFacturaAgrupada;
import com.princetonsa.dto.facturacion.DtoFactura;
import com.princetonsa.dto.facturacion.DtoFacturaAgrupada;
import com.princetonsa.dto.facturacion.DtoInstitucion;
import com.princetonsa.mundo.AdmisionHospitalaria;
import com.princetonsa.mundo.AdmisionUrgencias;
import com.princetonsa.mundo.Cuenta;
import com.princetonsa.mundo.IngresoGeneral;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.Persona;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.cargos.Contrato;
import com.princetonsa.mundo.cargos.Convenio;
import com.princetonsa.mundo.cargos.Empresa;
import com.princetonsa.mundo.cargos.Tercero;
import com.princetonsa.mundo.facturacion.Factura;
import com.princetonsa.mundo.facturacion.FormatoImpresionFactura;
import com.princetonsa.mundo.facturacion.ImpresionFactura;
import com.princetonsa.mundo.facturacion.ParametrizacionInstitucion;
import com.princetonsa.util.birt.reports.DesignEngineApi;
import com.princetonsa.util.birt.reports.ParamsBirtApplication;
import com.servinte.axioma.fwk.exception.IPSException;

/**    
 * 
 * @author artotor
 *
 */
public class FacturacionPdf 
{
    /**
     * Clase para manejar los logs de esta clase
     */
    private static Logger logger=Logger.getLogger(FacturacionPdf.class);
    /**
     * Color SubTitulo
     */
    public static final int colorSubTitulo=0xF5F5F5;
    /**
     * Color Titulo
     */
    public static final int colorTitulo=0xDEDADA;
	/**
	 * Color Blanco
	 */
	public static final int C_BLANCO=0xFFFFFF;
	/**
	 * Color Negro
	 */
	public static final int C_NEGRO=0x000000;

	/**
	 * Color GRIS
	 */
	public static final int C_GRIS=0xCACACA;
	
	public static String nombreReporteAgrupado="";
	
	/**
	 * Método para cerrar la conexión
	 * @param con
	 */
	private static void cerrarConexion(Connection con)
	{
	    try
		{
			UtilidadBD.cerrarConexion(con);
		}
		catch (SQLException e)
		{
			logger.error("Error cerrando la conexión: "+e);
		}
	}
	
	/**
	 * 
	 * Metodo para imprimir las factura
	 * @param nombreArchivo
	 * @param forma
	 * @param paciente
	 * @param usuario
	 * @param facturasImprimir
	 * @param con 
	 * @param request 
	 * @param nombreVentana 
	 * @param forward
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static ActionForward imprimirFacturas( PersonaBasica paciente, UsuarioBasico usuario, HashMap facturasImprimir, Connection con, HttpServletRequest request, String nombreVentana,ActionMapping mapping,String forward,HttpServletResponse response) throws IPSException 
	{
		logger.info("MAPA--->"+facturasImprimir);
		
		PdfReports report = null;	
        int tipoFormato=ConstantesBD.codigoNuncaValido;
        logger.info("\n\n *********************GENERANDO LA IMPRESION DE LA FACTURA********************************* \n\n");
        Vector archivosGenerados=(Vector) request.getAttribute("archivos");
        
        if(archivosGenerados==null){
        	archivosGenerados=new Vector();
        }
        
        int numFacturasImpresas = 0;
        
        
        
        HashMap archivosGeneradosBirt=(HashMap) request.getAttribute("archivosBirt");
        if(archivosGeneradosBirt==null){
        	archivosGeneradosBirt=new HashMap(0);
        archivosGeneradosBirt.put("numRegistros", "0");
        }else{
        	numFacturasImpresas=Integer.parseInt((String) archivosGeneradosBirt.get("numRegistros"));
        }
        
        String tipoPiePagina=String.valueOf(request.getSession().getAttribute("piePaginaImpresion")); 
        String tipoRetorno=String.valueOf(request.getSession().getAttribute("estadoRetornarFactura")); 
        InstitucionBasica institucionBasica = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
        Boolean tipoRetornoReporte=false;
        
        logger.info("FACTURAS A IMPRIMIR -->"+ Integer.parseInt(facturasImprimir.get("numRegistros")+""));
	    for(int  i = 0 ; i < Integer.parseInt(facturasImprimir.get("numRegistros")+"") ; i++)
		{
	    	//generacion de nombre de archivos
	    	String nombreArchivo = "";
	        Random r;
	    	
	    	ImpresionFactura impFactura = new ImpresionFactura();
            
	    	Utilidades.imprimirMapa(facturasImprimir);
            tipoFormato = Integer.parseInt(facturasImprimir.get("formatoImpresion_"+i)+"");
            logger.info("TIPO FORMATO IMPRESION >>>>>>>>>>>"+tipoFormato);
            /**
             * DE AQUI SE PARTIRÁ PARA DESARROLLAR LA IMPRESIÓN DE LA FACTURA
             * SE DEBE CAMBIAR EL SWITCH POR EL IF
             */
            
            /// SOLO SE VA HA MANEJAR LOS STANDAR PARA LOS DIFERENTES CLIENTES
            
            if(tipoFormato>=10)
            {
            	int codigoFactura = Integer.parseInt(facturasImprimir.get("codigoFactura_"+i)+"");
            	//int consecutivoFactura = Integer.parseInt(Utilidades.obtenerConsecutivoFactura(con, codigoFactura));
            	
            	int consecutivoFactura = Integer.parseInt(Utilidades.obtenerConsecutivoFactura(codigoFactura));
            	
            	DesignEngineApi comp;
                comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"facturacion/","ImprimirFactura.rptdesign");
                //Se editan los datos de la institución en el reporte
                editarDatosInstitucion(comp,con,usuario,tipoFormato,codigoFactura);
                //Se editan los datos restantes de encabezado en el reporte
                editarDatosEncabezado(comp,con,tipoFormato,codigoFactura);
                //Se editan los datos de la seecion de Servicios y Arituclos
                editarDatosCuerpoImpresion(comp, con, usuario, tipoFormato, codigoFactura);
                //Se editan los datos de la Seccion de Totales de la factura
                editarDatosSecTotalesImpresion(comp, con, tipoFormato, consecutivoFactura);
                //Se editan los datos de el pìe de pagina de la impreson
                editarDatosPiePaginaImpresion(comp, con, tipoFormato, consecutivoFactura);
                //Se ediatn los datos de la Seccion de Firmas
                editarDatosFirmasImpresionFactura(comp, con, tipoFormato);
                
                //comp.obtenerComponentesDataSet("listadoAnulacionRC");            
                //String oldQuery=comp.obtenerQueryDataSet();
                //forma.getMapaAnulacionRC("consultaWhere");
                //String newQuery=oldQuery+forma.getMapaAnulacionRC("consultaWhere")+"";                                
                //comp.modificarQueryDataSet(newQuery);
                //debemos arreglar los alias para que funcione oracle, debido a que este los convierte a MAYUSCULAS
		      	comp.lowerAliasDataSet();
				String newPathReport = comp.saveReport1(false);
                if(!newPathReport.equals(""))
                {
                    request.setAttribute("isOpenReport", "true");
                    request.setAttribute("newPathReport", newPathReport);
                }
                //por ultimo se modifica la conexion a BD
                comp.updateJDBCParameters(newPathReport);
                cerrarConexion(con);
                return mapping.findForward(forward);
            }
            else
            {
            	
            	            	
	            switch(tipoFormato)
	            {
	            	case ConstantesBD.codigoFormatoImpresionEstandar:
		            	logger.info("NO SE ENCONTRO EL FORMATO DE IMPRESION\nSE REALIZARÁ LA IMPRESION EN FORMATO ESTANDAR");
						/*if(System.getProperty("PAIS").toString().toUpperCase().equals("VENEZUELA"))
						{
							logger.info("===>Llego para Venezuela ===>Con Formato: "+facturasImprimir.get("formatoImpresionVenezuela_"+i));
							String pathReporte = "";
							//Validamos que tipo de formato se escogio para Venezuela "Factura Media Carta" ó "Factura Página Completa"
							if(facturasImprimir.get("formatoImpresionVenezuela_"+i).equals("media"))
							{
								logger.info("===>Factura Media Carta");
								pathReporte = generarReporteEstandarVenezuelaFormatoPeq(con,usuario,facturasImprimir,i,request);
							}
							else
							{
								logger.info("===>Factura Pagina Completa");
								pathReporte = generarReporteEstandarVenezuela(con,usuario,facturasImprimir,i,request);
							}
							logger.info("ATRIBUTO PARAA EL PATH DEL REPORTE >>>>>>>>>>>>"+pathReporte);
							archivosGeneradosBirt.put("isOpenReport_"+i, "true");
							archivosGeneradosBirt.put("newPathReport_"+i, pathReporte);
						}
		            	else
						{*/
					        r= new Random();        
					        nombreArchivo= "/aBorrar" + r.nextInt() + "fact_"+i+".pdf";//concateno i, porque puede que el rando de un arhivo que ya exista y lo remplace y para expecificar si es factura(fact) o anexo (anx).
					        archivosGenerados.add(nombreArchivo);
					    	nombreArchivo=ValoresPorDefecto.getFilePath() + nombreArchivo;
							logger.info("===>Llego para otro pais diferente a Venezuela");
							//Este es el de oct. igual lo pase pero varia por los parametros que envian lo comentareo por si algo
							//generarReporteEstandar(con,usuario,facturasImprimir,report,nombreArchivo,i);
							generarReporteEstandar(con,usuario,facturasImprimir,report,nombreArchivo,i,request,tipoPiePagina);
						//}
	            	break;
	            	case ConstantesBD.codigoFormatoImpresionShaio:
	            		if(facturasImprimir.containsKey("tipoimpresion_"+i) && facturasImprimir.get("tipoimpresion_"+i).equals("cc"))
	            		{
	            			String pathReporteCC = generarReporteFinalFacturaAgrupadaCC(con,usuario,paciente,facturasImprimir,report,nombreArchivo,i,request,mapping);
	            			archivosGeneradosBirt.put("isOpenReport_"+i, "true");
	    					archivosGeneradosBirt.put("newPathReport_"+i, pathReporteCC);
	            		}
	            		if(facturasImprimir.containsKey("tipoimpresion_"+i) && facturasImprimir.get("tipoimpresion_"+i).equals("it"))
	            		{
	            			String pathReporteIT = generarReporteFinalFacturaAgrupadaIT(con,usuario,paciente,facturasImprimir,report,nombreArchivo,i,request,mapping);
	            			archivosGeneradosBirt.put("isOpenReport_"+i, "true");
	    					archivosGeneradosBirt.put("newPathReport_"+i, pathReporteIT);
	            		}
		            break;
	            	case ConstantesBD.codigoFormatoImpresionVenezuela:
	            		logger.info("===>Llego para Venezuela ===>Con Formato: "+facturasImprimir.get("formatoImpresionVenezuela_"+i));
						String pathReporte = "";
						
						//Validamos que tipo de formato se escogio para Venezuela "Factura Media Carta" ó "Factura Página Completa"
						if(facturasImprimir.get("formatoImpresionVenezuela_"+i).equals("media"))
							pathReporte = generarReporteEstandarVenezuelaFormatoPeq(con,usuario,facturasImprimir,i,request);
						else
							pathReporte = generarReporteEstandarVenezuela(con,usuario,facturasImprimir,i,request);
						
						logger.info("ATRIBUTO PARAA EL PATH DEL REPORTE >>>>>>>>>>>>"+pathReporte);
						archivosGeneradosBirt.put("isOpenReport_"+i, "true");
						archivosGeneradosBirt.put("newPathReport_"+i, pathReporte);
		            break;
	            	case ConstantesBD.codigoFormatoImpresionVersalles:
	            		
	            		
	            		
						    r= new Random();        
					        nombreArchivo= "/aBorrar" + r.nextInt() + "fact_"+i+".pdf";//concateno i, porque puede que el rando de un arhivo que ya exista y lo remplace y para expecificar si es factura(fact) o anexo (anx).
					        archivosGenerados.add(nombreArchivo);
					    	nombreArchivo=ValoresPorDefecto.getFilePath() + nombreArchivo;
							//Este es el de oct. igual lo pase pero varia por los parametros que envian lo comentareo por si algo
							//generarReporteEstandar(con,usuario,facturasImprimir,report,nombreArchivo,i);
							generarReporteVersalles(con,usuario,facturasImprimir,report,nombreArchivo,i,request, tipoPiePagina);
							
							/*
							r= new Random();        
					        nombreArchivo= "/aBorrar" + r.nextInt() + "fact_"+i+".pdf";//concateno i, porque puede que el rando de un arhivo que ya exista y lo remplace y para expecificar si es factura(fact) o anexo (anx).
					        archivosGenerados.add(nombreArchivo);
					    	nombreArchivo=ValoresPorDefecto.getFilePath() + nombreArchivo;
							//Este es el de oct. igual lo pase pero varia por los parametros que envian lo comentareo por si algo
							//generarReporteEstandar(con,usuario,facturasImprimir,report,nombreArchivo,i);
						
						    	generarReporteVersalles(con,usuario,facturasImprimir,report,nombreArchivo,i,request, tipoImpresion);
							*/
//							generarReporteFacturacionAgrupada( paciente,  usuario,  facturasImprimir,  con,tipoPiePagina,response,institucionBasica);
						//}
	            	break;
	            	case ConstantesBD.codigoFormatoImpresionFacturaAgrupada:
	            		
	            		//falg para saber si retorna a imprimir con birt o con dynamic si es true entonces retorna por dynamic
	            		tipoRetornoReporte=true;
	            		
	            		//metodo para generar el reporte agrupado 
	            		generarReporteFacturacionAgrupada( paciente,  usuario,  facturasImprimir,  con,tipoPiePagina,response,institucionBasica);
	            		
	            	break;	
	            	default:
	            		logger.info("NO SE ENCONTRO EL FORMATO DE IMPRESION\nSE REALIZARÁ LA IMPRESION EN FORMATO ESTANDAR");
		            	/*if(System.getProperty("PAIS").toString().toUpperCase().equals("VENEZUELA"))
						{
							logger.info("===>Llego para Venezuela ===>Con Formato: "+facturasImprimir.get("formatoImpresionVenezuela_"+i));
							String pathReporte = "";
							//Validamos que tipo de formato se escogio para Venezuela "Factura Media Carta" ó "Factura Página Completa"
							if(facturasImprimir.get("formatoImpresionVenezuela_"+i).equals("media"))
							{
								logger.info("===>Factura Media Carta");
								pathReporte = generarReporteEstandarVenezuelaFormatoPeq(con,usuario,facturasImprimir,i,request);
							}
							else
							{
								logger.info("===>Factura Pagina Completa");
								pathReporte = generarReporteEstandarVenezuela(con,usuario,facturasImprimir,i,request);
							}
							logger.info("ATRIBUTO PARAA EL PATH DEL REPORTE >>>>>>>>>>>>"+pathReporte);
							archivosGeneradosBirt.put("isOpenReport_"+i, "true");
							archivosGeneradosBirt.put("newPathReport_"+i, pathReporte);
						}
						else
						{*/
					        r= new Random();        
					        nombreArchivo= "/aBorrar" + r.nextInt() + "fact_"+i+".pdf";//concateno i, porque puede que el rando de un arhivo que ya exista y lo remplace y para expecificar si es factura(fact) o anexo (anx).
					        archivosGenerados.add(nombreArchivo);
					    	nombreArchivo=ValoresPorDefecto.getFilePath() + nombreArchivo;
							logger.info("===>Llego para otro pais diferente a Venezuela...");
							//Este es el de oct. igual lo pase pero varia por los parametros que envian lo comentareo por si algo
							//generarReporteEstandar(con,usuario,facturasImprimir,report,nombreArchivo,i);
							generarReporteEstandar(con,usuario,facturasImprimir,report,nombreArchivo,i,request,tipoPiePagina);
						//}
	            	break;
	            }
            }
            logger.info("BOOLEAANNNNNN>>>>>>>>>>>>>>>>>"+facturasImprimir.get("impAnexoArticulo_"+i)+"");
            //--------------------------------------IMPRESION DE ANEXOS----------------------------------------------------////////////
            if(UtilidadTexto.getBoolean(facturasImprimir.get("impAnexoArticulo_"+i)+""))
            {
            	logger.info("TIPO ANEXO A IMPRIMIRRRRR>>>>>>>>>"+facturasImprimir.get("tipoAnexo_"+i)+"");
            	if((facturasImprimir.get("tipoAnexo_"+i)+"").equals("fecha"))
            	{
        	        r= new Random();        
        	        nombreArchivo= "/aBorrar" + r.nextInt() + "anx_"+i+".pdf";//concateno i, porque puede que el rando de un arhivo que ya exista y lo remplace y para expecificar si es factura(fact) o anexo (anx).
        	        archivosGenerados.add(nombreArchivo);
        	    	nombreArchivo=ValoresPorDefecto.getFilePath() + nombreArchivo;
        	    	generarImpresionAnexoMedicamentosFecha(nombreArchivo,paciente,usuario,impFactura,facturasImprimir.get("codigoFactura_"+i)+"",con);
            	}
            	else if((facturasImprimir.get("tipoAnexo_"+i)+"").equals("orden"))
            	{
            		r= new Random();        
        	        nombreArchivo= "/aBorrar" + r.nextInt() + "anx_"+i+".pdf";//concateno i, porque puede que el rando de un arhivo que ya exista y lo remplace y para expecificar si es factura(fact) o anexo (anx).
        	        archivosGenerados.add(nombreArchivo);
        	    	nombreArchivo=ValoresPorDefecto.getFilePath() + nombreArchivo;
        	    	generarImpresionAnexoMedicamentosOrden(nombreArchivo,paciente,usuario,impFactura,facturasImprimir.get("codigoFactura_"+i)+"",con);
            	}
            }
            numFacturasImpresas++;
        }
	    
	    Utilidades.imprimirMapa(facturasImprimir);
	    
	    for(int  i = 0 ; i < Integer.parseInt(facturasImprimir.get("numRegistros")+"") ; i++)
		{
	    	boolean contieneReporte=false;
	    	for(int j=0; j<Integer.parseInt((String) archivosGeneradosBirt.get("numRegistros"));j++){
	    		if(facturasImprimir.containsKey("tipoanexo_"+i)&&facturasImprimir.get("tipoanexo_"+i).toString()
	    				.equals(archivosGeneradosBirt.get("newPathReport_"+j))){
	    			contieneReporte=true;
	    		}
	    	}
	    	if(!contieneReporte&&facturasImprimir.containsKey("tipoanexo_"+i) && !facturasImprimir.get("tipoanexo_"+i).toString().equals(""))
	    	{
	    		archivosGeneradosBirt.put("isOpenReport_"+numFacturasImpresas, "true");
		    	archivosGeneradosBirt.put("newPathReport_"+numFacturasImpresas, facturasImprimir.get("tipoanexo_"+i).toString());
		    	numFacturasImpresas++;
	    	}
		}
	    
	    archivosGeneradosBirt.put("numRegistros", numFacturasImpresas+"");
	    
	    logger.info("nombreVentana--->"+nombreVentana);
	    Utilidades.imprimirMapa(archivosGeneradosBirt);
	    
	    request.setAttribute("archivos", archivosGenerados);
	    request.setAttribute("archivosBirt", archivosGeneradosBirt);
        request.setAttribute("nombreVentana", nombreVentana);
        cerrarConexion(con);
        
        //se valdia si se imprime solo reportes de birt o el de factura agrupada hecho en jasper
        logger.info("llega a retornar abrirNPdfBirt!!!!!!!!");
        if(tipoRetornoReporte){
        	return mapping.findForward(tipoRetorno);
        }else{
        	return mapping.findForward("abrirNPdfBirt");
        }
	}
	
	
	
	
	
	
	/**
	 * Metodo encargado de generar el reporte de factura agrupada
	 * @param paciente
	 * @param usuario
	 * @param facturasImprimir
	 * @param con
	 * @param tipoPiePagina
	 * @param response
	 * @param institucionBasica
	 */
	public static  void generarReporteFacturacionAgrupada(PersonaBasica paciente, UsuarioBasico usuario, HashMap facturasImprimir, Connection con,String tipoPiePagina,HttpServletResponse response
			,InstitucionBasica institucionBasica){

		
		//instancia del generador del reporte 
		GeneradorReporteDinamico generadorReporteDinamico = new GeneradorReporteDinamico();

		//objeto de reporte de factura  
		JasperReportBuilder reporteFacturacionAgrupada=report();
		
		//mapa con los parametros para el reporte 
		HashMap<String, String> paramas;
		

		//instancia de diseno del reporte
		GeneradorDisenoReporteFacturacionAgrupado diseno = new GeneradorDisenoReporteFacturacionAgrupado();
		
		

		//generador del rpeorte agrupado
		GeneradorReporteFacturacionAgrupado generadorReporteFacturacionAgrupado = new GeneradorReporteFacturacionAgrupado();

		try {

			
			//se recorren todas las facturas 
			for (int i = 0; i <Integer.valueOf(String.valueOf(facturasImprimir.get(IConstantesReporteAgrupadoFacturacion.numRegistros))); i++) {
				
				

				//lista con los dto de la factura - tabla - datasource
				List<DtoFacturaAgrupada> detallefactura= new LinkedList<DtoFacturaAgrupada>();
				
				//se consultan todos los elementos de la factura 
				detallefactura=obtenerDatosFactura(con, String.valueOf( facturasImprimir.get(IConstantesReporteAgrupadoFacturacion.codigoFactura+i)),usuario.getCodigoInstitucionInt());
				
				//se cargan los parametros del reporte como en encabezado
				paramas = cargarParametrosReporte( paciente, usuario, facturasImprimir, con,i,detallefactura,institucionBasica);
				
				
				//se verifica el tamaño de impresion si carta o media carta 
				String tamImp=UtilidadesFacturacion.obtenerTamanioImpresionFactura(con,String.valueOf( facturasImprimir.get(IConstantesReporteAgrupadoFacturacion.codigoFactura+i)));
				
				//segun el tamaño de adiciona el valor del parametro
				if(tamImp.equals(ConstantesIntegridadDominio.acronimoMediaCarta)){
					paramas.put(IConstantesReporteAgrupadoFacturacion.tamanoReporte, IConstantesReporteAgrupadoFacturacion.mediaCarta);
				}else{
					paramas.put(IConstantesReporteAgrupadoFacturacion.tamanoReporte, IConstantesReporteAgrupadoFacturacion.carta);
				}
				
				//se adicionan configuracion de reporte y margenes en 0 para contener los subreportes
				reporteFacturacionAgrupada
				.setTemplate(diseno.crearPlantillaReporte(paramas.get(IConstantesReporteAgrupadoFacturacion.tamanoReporte))) 
				.setPageMargin(diseno.crearMagenesReporteGeneral())
				
				;
				
				//se adiciona si el pie de pagina es " original " o " copia " 
				paramas.put(IConstantesReporteAgrupadoFacturacion.tipoPieDePagina, tipoPiePagina);
				
				//se adiciona al mapa la ruta del logo de la institucion 
				paramas.put(IConstantesReporteAgrupadoFacturacion.institucionBasica, institucionBasica.getLogoReportes());
				
				//se genera el reporte 
				reporteFacturacionAgrupada.summary(cmp.subreport(generadorReporteFacturacionAgrupado.generarReporteFacturacionAgrupado(paramas,detallefactura))); 
				
			}



		

		//se compila el rpeorte
		reporteFacturacionAgrupada.build();
		
		
		//SE EXPORTA A PDF
		nombreReporteAgrupado = generadorReporteDinamico.exportarReportePDF(reporteFacturacionAgrupada, IConstantesReporteFacturaAgrupada.nombreReporteFacturaAgrupada);

		}//control de errores 
		catch (SQLException e) {
		
			logger.error("error generando el reporte de facturacion agrupada"+e.getMessage());
		}
	}
	
	
	/**
	 * Metodo que calcula el total de los cargos 
	 * @param detallefactura
	 * @return Integer con el total de los cargos 
	 */
	public static Integer calcularTotalCargosFacturaAgrupada(List<DtoFacturaAgrupada> detallefactura){
		
		//integer con el total de los cargos
		Integer totalCargos=new Integer(0);

		//se recorren los dto consultados para sumar los totales y se validan vacios 
		for (DtoFacturaAgrupada dtoFacturaAgrupada : detallefactura) {
			if(dtoFacturaAgrupada.getValorTotal()!=null  
					&& !dtoFacturaAgrupada.getValorTotal().equals("")
					&& !dtoFacturaAgrupada.getValorTotal().equals(" ")){

				//se suman los cargos
				totalCargos+=Integer.valueOf(dtoFacturaAgrupada.getValorTotal());
			}
			
			//en el caso de cirugias se suman los detalles de cada cirugia 
			for (DtoDetalleCirugiasFacturaAgrupada dtoDetalleCirugiasFacturaAgrupada : dtoFacturaAgrupada.getDetallesCirugias()) {
				if(dtoDetalleCirugiasFacturaAgrupada.getValor()!=null 
						&& !dtoDetalleCirugiasFacturaAgrupada.getValor().equals("") 
						&& !dtoDetalleCirugiasFacturaAgrupada.getValor().equals(" ")){
					totalCargos+=Integer.valueOf(dtoDetalleCirugiasFacturaAgrupada.getValor());
				}
			}
		}

		//suma de totales de los cargos
		return totalCargos;
	}
	
	
	/**
	 * Metodo que consulta la informacion de las facturas 
	 * @param con
	 * @param numeroFactura
	 * @return DtoFacturaAgrupada con la información asociada a la consulta 
	 * @throws SQLException
	 */
	public static  List<DtoFacturaAgrupada> obtenerDatosFactura(Connection con,String numeroFactura,Integer codigoInstitucion) throws SQLException{
		
		//Lista con los detalles a imprimir 
		List<DtoFacturaAgrupada> detallefactura= new LinkedList<DtoFacturaAgrupada>();
		
		//instacia del mundo que tiene consultas 
		Cuenta mundoCuenta= new Cuenta();

		//se consultan los servicios , articulos , paquetes y cirugias con detalles en orden segun DCU - 915
		detallefactura.addAll(mundoCuenta.serviciosFacturaAgrupada(con, numeroFactura));
		detallefactura.addAll(mundoCuenta.articulosFacturaAgrupada(con, numeroFactura));
		detallefactura.addAll(mundoCuenta.paquetesFacturaAgrupada(con, numeroFactura));
		detallefactura.addAll(mundoCuenta.cirugiasFacturaAgrupada(con, numeroFactura,codigoInstitucion));

		//retorno de DCU 
		return detallefactura;
	}
	
	/**
	 * Se consultan los valores del rpeorte como encabezado datos del paciente etc
	 * @param paciente
	 * @param usuario
	 * @param facturasImprimir
	 * @param con
	 * @param index
	 * @param detallefactura
	 * @return HashMap con los parametros del reporte
	 * @throws SQLException
	 */
	public static HashMap<String, String> cargarParametrosReporte(PersonaBasica paciente, UsuarioBasico usuario,
		
			HashMap facturasImprimir, Connection con,Integer index,List<DtoFacturaAgrupada> detallefactura,InstitucionBasica institucionBasica) throws SQLException{
		String nombre="";
		String nombre2="";
		String apellido="";
		String apellido2="";


		//instancia de mapa que contiene los valores para el reporte
		HashMap<String, String> paramas = new HashMap<String, String>();


		//validaciones de nulidad 
		if(!UtilidadTexto.isEmpty(paciente.getPrimerApellido())){
			apellido=paciente.getPrimerApellido();
		}

		if(!UtilidadTexto.isEmpty(paciente.getSegundoApellido())){
			apellido2=paciente.getSegundoApellido();
		}

		if(!UtilidadTexto.isEmpty(paciente.getPrimerNombre())){
			nombre=paciente.getPrimerNombre();
		}

		if(!UtilidadTexto.isEmpty(paciente.getSegundoNombre())){
			nombre2=paciente.getSegundoNombre();
		}


		//nombres del paciente
		paramas.put("nombrePaciente", "PACIENTE: "+apellido+" "+apellido2
				+" "+nombre+" "+nombre2);

		//identificacion del paciente
		paramas.put("identificacionPaciente","IDENTIFICACIÓN: "+paciente.getCodigoTipoIdentificacionPersona()+". " +paciente.getNumeroIdentificacionPersona());

		//ingreso asociado a la factura
		Cuenta mundoCuenta= new Cuenta();
		paramas.put("noIngreso", "No. INGRESO: "+mundoCuenta.obtenerIngresoXNumeroFactura(con,String.valueOf( facturasImprimir.get("codigoFactura_"+index))));

		//direccion paciente
		paramas.put("direccion", "DIRECCIÓN: "+paciente.getDireccion());



		String tel = "";

		if(!UtilidadTexto.isEmpty(paciente.getTelefono())){
			tel=paciente.getTelefono();
		}

		//telefono paciente
		paramas.put("telefonoPaciente", "TELÉFONO: "+tel);




		//se consulta la informacion de la institucion 
		DtoInstitucion dtoInstitucion = mundoCuenta.consultarDatosInstitucionXFactura(con, String.valueOf( facturasImprimir.get("codigoFactura_"+index)));

		//se adicionan los valores de la insitucion al mapa
		paramas.put("razonSocialInst", dtoInstitucion.getNombre());
		paramas.put("nitInst"," NIT "+dtoInstitucion.getNit());
		paramas.put("direccionInst"," Dir. "+ dtoInstitucion.getDireccion());
		paramas.put("telefonoInst"," Tel. "+ dtoInstitucion.getTelefono());
		paramas.put("ciudadInst"," Ciudad. "+ dtoInstitucion.getCiudad());

		//se obtiene la fecha y hora de la factura 
		String fechaHoraAnulacion=mundoCuenta.obtenerDatosAnulacionFactura(con, String.valueOf( facturasImprimir.get("codigoFactura_"+index)));

		//se adiciona la fecha de anulacion si no esta anulada el parametro del mapa va vacio 
		if(!UtilidadTexto.isEmpty(fechaHoraAnulacion)){
			paramas.put("fechaHoraAnulacion"," FACTURA ANULADA "+ fechaHoraAnulacion);
		}else{
			paramas.put("fechaHoraAnulacion"," ");
		}

		//se consulta el centro de atencion y se adiciona al mapa 
		paramas.put("centroAtencionFact", mundoCuenta.obtenerCentroAtencionFactura(con, String.valueOf( facturasImprimir.get("codigoFactura_"+index))));

		//se consulta y se adiciona el numero de la factura de venta 
		//MT6082: se concadena el prefijo de la factura
		String prefijoFactura="";
		
		if( !UtilidadTexto.isEmpty( String.valueOf(facturasImprimir.get("prefijoFactura_"+index))))
		{
			prefijoFactura=String.valueOf( facturasImprimir.get("prefijoFactura_"+index));
			
		}else
		{
			prefijoFactura="";
		}
		logger.info("**************===>prefijoFactura: " + prefijoFactura);
		logger.info("**************===>index: " + index);
		logger.info("**************===>String.valueOf(): " + String.valueOf(facturasImprimir.get("prefijoFactura_"+index)));

		paramas.put("numerofactAsociada","FACTURA DE VENTA: "+ prefijoFactura + mundoCuenta.obtenerNumeroFacturaAsociada(con, String.valueOf( facturasImprimir.get("codigoFactura_"+index))));
		
		logger.info("**************===>paramas.get(\"numerofactAsociada\"): " +paramas.get("numerofactAsociada"));
		
        //Fin MT
		//instancia del mundo ImpresionFactura 
		ImpresionFactura mundoImpresion=new ImpresionFactura();

		//se consulan datos del paciente para llenar el mapa 
		HashMap mapDatosPac= mundoImpresion.consultarSeccionPacienteFormatoVersalles(con,String.valueOf( facturasImprimir.get("codigoFactura_"+index)));

		//se consulta el numero de autoizacion por prioridad 
		//MT6135 Se cambia la variable para traer el dato del mapa
		//Integer numeroAutorizacion = mundoCuenta.consultarNumeroAutorizacion(con,Integer.valueOf( String.valueOf(mapDatosPac.get("convenio")))
		//	, Integer.valueOf(String.valueOf(mapDatosPac.get("consecutivoingreso"))));

		String numeroAutorizacion=mapDatosPac.get("numeroautorizacion")+"";
		//fin mt		


		String fechaIng ="";

		if(!UtilidadTexto.isEmpty(String.valueOf(mapDatosPac.get( "fechaingreso"))  )){
			fechaIng=String.valueOf(mapDatosPac.get( "fechaingreso"));
		}

		//fecha ingreso paciente
		paramas.put("fechaIngresoPaciente", "FECHA DE INGRESO: "+fechaIng);



		//se consultan datos adicionales del paciente 
		DtoDatosResponsableFacturacion dtoDatosResponsableFacturacion=mundoCuenta.obtenerResponsable(con, String.valueOf( facturasImprimir.get("codigoFactura_"+index)));

		DtoDatosRepsonsableFactura dtoDatosRepsonsableFactura=mundoImpresion.obtenerNombreResponsable(con, paciente.getCodigoCuenta(), Integer.valueOf( String.valueOf(mapDatosPac.get("convenio"))));

		String consultarTipoMonto = mundoImpresion.consultarTipoMontoFactura(con,  Integer.valueOf( String.valueOf(mapDatosPac.get("convenio")))
				,Integer.valueOf( String.valueOf(mapDatosPac.get("consecutivoingreso"))));


		if(consultarTipoMonto.equals(IConstantesReporteAgrupadoFacturacion.nombreTipoMontoPagoCuotaModeradora)){
			paramas.put("nombreTipoMontoCobro", IConstantesReporteAgrupadoFacturacion.nombreTipoMontoPagoCuotaModeradora);
		}else{
			paramas.put("nombreTipoMontoCobro", IConstantesReporteAgrupadoFacturacion.nombreTipoMontoPagoCopago);
		}


		//se adicionan al mapa valores consultados del usuario 
		paramas.put("valorConvenio", dtoDatosResponsableFacturacion.getValorConvenio());
		paramas.put("valorBrutoPaciente", dtoDatosResponsableFacturacion.getValorBrutoPaciente());

		if(dtoDatosRepsonsableFactura==null){
			paramas.put("responsableFact"," Responsable: "+paciente.getNombrePersona());
			paramas.put("direccionResponsable",paciente.getDireccion());
			paramas.put("telefonoResponsable",paciente.getTelefono());
		}else if(!dtoDatosRepsonsableFactura.getNombreResponsable().trim().equals("")){
			paramas.put("responsableFact"," Responsable: "+dtoDatosRepsonsableFactura.getNombreResponsable());
			paramas.put("direccionResponsable",dtoDatosRepsonsableFactura.getDireccionResponsable());
			paramas.put("telefonoResponsable", dtoDatosRepsonsableFactura.getTelefonoResponsable());
		}else {
			paramas.put("responsableFact"," Responsable: "+String.valueOf(mapDatosPac.get("nombretercero")));
			paramas.put("direccionResponsable",String.valueOf(mapDatosPac.get("dirtercero")));
			paramas.put("telefonoResponsable", String.valueOf(mapDatosPac.get("telefonotercero")));
		}

		boolean tieneEncabezado=false;
		boolean tienePiePagina=false;

		if(!UtilidadTexto.isEmpty(String.valueOf(mapDatosPac.get("encabezadofacturaconvenio")))){
			paramas.put("encabezadoFactura",String.valueOf(mapDatosPac.get("encabezadofacturaconvenio")) );
			tieneEncabezado=true;
		}else{ 
			paramas.put("encabezadoFactura","" );
		}
		if(!UtilidadTexto.isEmpty(String.valueOf(mapDatosPac.get("piefactura")))){
			paramas.put("piefactura", String.valueOf(mapDatosPac.get("piefactura")));
			tienePiePagina=true;
		}else{
			paramas.put("piefactura","");
		}
		if(!tieneEncabezado){
			if (!UtilidadTexto.isEmpty(institucionBasica.getEncabezadoFactura())){
				paramas.put("encabezadoFactura",institucionBasica.getEncabezadoFactura() );
			}else{
				paramas.put("encabezadoFactura","" );
			}
		}
		if(!tienePiePagina){
			if (!UtilidadTexto.isEmpty(institucionBasica.getPieFactura())){
				paramas.put("piefactura",institucionBasica.getPieFactura());
			}else{
				paramas.put("piefactura","");
			}
		}




		// se consulta la fecha de generacion de la factura 
		paramas.put("fechaFacturacion", mundoCuenta.obtenerFechaGeneracionFactura(con, String.valueOf( facturasImprimir.get("codigoFactura_"+index))));

		//se adiciona al mapa el loguin del usuario 
		paramas.put("loginusuario", usuario.getLoginUsuario());

		//se consulta la fecha de vencimiento 
		paramas.put("fechaVencimiento", UtilidadFecha.incrementarDiasAFecha(UtilidadFecha.conversionFormatoFechaAAp( mapDatosPac.get( "fecha_factura" )+"" ),Utilidades.convertirAEntero(mapDatosPac.get( "diasvencimiento" )+"",true ),false));

		//se adiciona al mapa el numero de autorizacion segun la consulta es el campo  --> numeroid_tercero y  numeroautorizacion
		paramas.put("numeroId",String.valueOf(mapDatosPac.get("numeroid_tercero")));

		//se valida el numero de autorizacion si va vacio el mapa va vacio en ese parametro 
		
		//MT6083 de agrega validación que si el número de autorización es 0 presente vacio --MT6135 se elimina el casteo a String
		if( !UtilidadTexto.isEmpty(numeroAutorizacion)&& !numeroAutorizacion.equals("0"))
		{
			paramas.put("autorizacion",numeroAutorizacion);
		}else
		{
			paramas.put("autorizacion","");
		}

		//se valida la fecha de egreso y se adiciona al mapa 
		if( !UtilidadTexto.isEmpty( String.valueOf(mapDatosPac.get("fechaegreso"))))
		{
			paramas.put("fechaEgreso","FECHA DE EGRESO: "+ String.valueOf( mapDatosPac.get("fechaegreso") ));
		}else
		{
			paramas.put("fechaEgreso","");
		}

		//se adiciona al mapa si se deben tener en cuenta los decimales o no 
		paramas.put("usarDecimales",String.valueOf(mundoCuenta.formatoValoresFormatoFacturaAgrupada(con, dtoDatosResponsableFacturacion.getConvenio())));

		//se adiciona al mapa el total de los cargos 
		paramas.put("totalCargo",String.valueOf( calcularTotalCargosFacturaAgrupada(detallefactura)));

		//se genera el valor en letras del convenio o por total empresa 
		String totalEmpresa=UtilidadN2T.convertirLetras(UtilidadTexto.formatearValores(dtoDatosResponsableFacturacion.getValorConvenio() ).replaceAll(",", ""),ConstantesBD.cadenaUnidades,ConstantesBD.cadenaUnidadesDecimales)+" MCTE.";
		String totalCargos=UtilidadN2T.convertirLetras(UtilidadTexto.formatearValores(paramas.get("totalCargo")+"" ).replaceAll(",", ""),ConstantesBD.cadenaUnidades,ConstantesBD.cadenaUnidadesDecimales)+" MCTE.";

		//se conulta que valor e letras debe ir en el reporte si total empresa o convenio 
		HashMap<String, Object> mapaAux = new HashMap<String, Object>();
		mapaAux = mundoImpresion.consultarValorLetrasValor(con, dtoDatosResponsableFacturacion.getConvenio());

		//de acuerdo al valor del parametro se adiciona al mapa 
		if((mapaAux.get("parametro")+"").equals(ConstantesIntegridadDominio.acronimoValorLetrasFacturaCargo)){
			paramas.put("valorEnLetras", totalCargos.replace("ss", "s"));
		}else{
			paramas.put("valorEnLetras", totalEmpresa.replace("ss", "s"));
		}

		//de acuerdo al valor consulado se usa o no los decimales 
		if(paramas.get("usarDecimales").equals("true")){ 
			paramas.put("totalCargo",UtilidadTexto.formatearValores(String.valueOf( calcularTotalCargosFacturaAgrupada(detallefactura))));
			paramas.put("valorConvenio",UtilidadTexto.formatearValores( dtoDatosResponsableFacturacion.getValorConvenio()));
			paramas.put("valorBrutoPaciente",UtilidadTexto.formatearValores( dtoDatosResponsableFacturacion.getValorBrutoPaciente()));
		}else{
			paramas.put("totalCargo",UtilidadTexto.formatearValoresSinDecimales(String.valueOf( calcularTotalCargosFacturaAgrupada(detallefactura))));
			paramas.put("valorConvenio",UtilidadTexto.formatearValoresSinDecimales( dtoDatosResponsableFacturacion.getValorConvenio()));
			paramas.put("valorBrutoPaciente",UtilidadTexto.formatearValoresSinDecimales( dtoDatosResponsableFacturacion.getValorBrutoPaciente()));
		}


		String esMultiempresa= ValoresPorDefecto.getInstitucionMultiempresa(institucionBasica.getCodigoInstitucionBasica());

		//ubicacion del logo de los reportes
		paramas.put("ubicacionLogoReportes", institucionBasica.getUbicacionLogo());

		//se retorna el mapa con los valores 
		return paramas;
	}
	
	
	

	/**
	 * Metodo que permite generar el reporte estandar para
	 * venezuela usando la herramienta BIRT
	 * @param con
	 * @param usuario
	 * @param facturasImprimir
	 * @param nombreArchivo
	 * @param i
	 * @param request
	 */
	public static String generarReporteEstandarVenezuela(Connection con, UsuarioBasico usuario, HashMap facturasImprimir, int i, HttpServletRequest request)
	{
		String nombreRptDesign = "FacturaEstandarVenezuela.rptdesign";
		String condiciones = "", newQuery = "";
		InstitucionBasica institucion = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
		DesignEngineApi comp;
        comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"facturacion/",nombreRptDesign);
        //comp.insertImageHeaderOfMasterPage1(0, 0, institucion.getLogoReportes());
        comp.insertGridHeaderOfMasterPage(0,1,1,5);
        
        //Informacion del Cabezote
        Vector v = new Vector();
        //v.add(institucion.getRazonSocial());
        //v.add(Utilidades.getDescripcionTipoIdentificacion(con,institucion.getTipoIdentificacion())+"  -  "+institucion.getNit());
        //v.add(institucion.getDireccion());
        //v.add("Tels. "+institucion.getTelefono());
        //v.add(institucion.getEncabezadoFactura());
        comp.insertLabelInGridOfMasterPage(0,1,v);
        
        //Informacion Pie de Pagina
        comp.insertLabelInGridPpalOfFooter(1, 1, usuario.getLoginUsuario());
        
        //Seccion Pacientes
        comp.obtenerComponentesDataSet("SecPaciente");
        condiciones = "f.codigo = "+facturasImprimir.get("codigoFactura_"+i);
        newQuery = comp.obtenerQueryDataSet().replace("1=2", condiciones);
        //logger.info("===>Consulta Sec. Paciente: "+newQuery);
        comp.modificarQueryDataSet(newQuery);


        //Seccion Informacion de Intervencion
        comp.obtenerComponentesDataSet("SecInfoInter");
        condiciones = "fac.codigo = " + facturasImprimir.get("codigoFactura_"+i);
        newQuery = comp.obtenerQueryDataSet().replace("1=2", condiciones);
        comp.modificarQueryDataSet(newQuery);
        logger.info("77777777777777777777777777777");
        //Utilidades.imprimirMapa(facturasImprimir);
        logger.info("Codigo Factura: " + facturasImprimir.get("codigoFactura_"+i));
        logger.info("===>Consulta Sec. Info Interv: " + newQuery);
        
        
        //Seccion Responsables
        comp.obtenerComponentesDataSet("SecResponsable");
        condiciones = "f.codigo = "+facturasImprimir.get("codigoFactura_"+i);
        newQuery = comp.obtenerQueryDataSet().replace("1=2", condiciones);
        //logger.info("===>Consulta Sec. Responsable: "+newQuery);
        comp.modificarQueryDataSet(newQuery);
        
        //Seccion Servicios
        comp.obtenerComponentesDataSet("SecServicios");
        condiciones = "f.codigo = "+facturasImprimir.get("codigoFactura_"+i)+" AND dfs.articulo IS NULL AND dfs.cantidad_cargo > 0 ";
        newQuery = comp.obtenerQueryDataSet().replace("1=2", condiciones);
        //logger.info("===>Consulta Sec. Servicios: "+newQuery);
        comp.modificarQueryDataSet(newQuery);
        
        //Seccion Articulos
        comp.obtenerComponentesDataSet("SecArticulos");
        condiciones = "f.codigo = "+facturasImprimir.get("codigoFactura_"+i)+" AND dfs.servicio IS NULL AND dfs.cantidad_cargo > 0 ";
        newQuery = comp.obtenerQueryDataSet().replace("1=2", condiciones);
        //logger.info("===>Consulta Sec. Articulos: "+newQuery);
        comp.modificarQueryDataSet(newQuery);
        
        //Seccion Totales
        comp.obtenerComponentesDataSet("SecTotales");
        condiciones = "f.codigo = "+facturasImprimir.get("codigoFactura_"+i)+"";
        newQuery = comp.obtenerQueryDataSet().replace("1=2", condiciones);
        //logger.info("===>Consulta Sec. Totales: "+newQuery);
        comp.modificarQueryDataSet(newQuery);
        
        //debemos arreglar los alias para que funcione oracle, debido a que este los convierte a MAYUSCULAS
      	comp.lowerAliasDataSet();
		String newPathReport = comp.saveReport1(false);
        comp.updateJDBCParameters(newPathReport);
        
        if(!newPathReport.equals("")) {
        	return newPathReport;
        }
        return "";
	}
	
	/**
	 * Metodo que permite generar el reporte estandar para
	 * venezuela usando la herramienta BIRT
	 * @param con
	 * @param usuario
	 * @param facturasImprimir
	 * @param nombreArchivo
	 * @param i
	 * @param request
	 */
	public static String generarReporteEstandarVenezuelaFormatoPeq(Connection con, UsuarioBasico usuario, HashMap facturasImprimir, int i, HttpServletRequest request)
	{
		String nombreRptDesign = "FacturaEstandarVenezuelaFormatoPeq.rptdesign";
		String condiciones = "", newQuery = "";
		InstitucionBasica institucion = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
		DesignEngineApi comp;
        comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"facturacion/",nombreRptDesign);
        //comp.insertImageHeaderOfMasterPage1(0, 0, institucion.getLogoReportes());
        comp.insertGridHeaderOfMasterPage(0,1,1,5);
        
        //Informacion del Cabezote
        Vector v = new Vector();
        //v.add(institucion.getRazonSocial());
        //v.add(Utilidades.getDescripcionTipoIdentificacion(con,institucion.getTipoIdentificacion())+"  -  "+institucion.getNit());
        //v.add(institucion.getDireccion());
        //v.add("Tels. "+institucion.getTelefono());
        //v.add(institucion.getEncabezadoFactura());
        comp.insertLabelInGridOfMasterPage(0,1,v);
        
        //Informacion Pie de Pagina
        //comp.insertLabelInGridPpalOfFooter(0, 0, "Usuario: "+usuario.getLoginUsuario()+"\n"+institucion.getPieFactura());
        
        //Seccion Pacientes
        comp.obtenerComponentesDataSet("SecPaciente");
        condiciones = "f.codigo = "+facturasImprimir.get("codigoFactura_"+i);
        newQuery = comp.obtenerQueryDataSet().replace("1=2", condiciones);
        logger.info("===>Consulta Sec. Paciente: "+newQuery);
        comp.modificarQueryDataSet(newQuery);
        
        //Seccion Responsables
        comp.obtenerComponentesDataSet("SecResponsable");
        condiciones = "f.codigo = "+facturasImprimir.get("codigoFactura_"+i);
        newQuery = comp.obtenerQueryDataSet().replace("1=2", condiciones);
        logger.info("===>Consulta Sec. Responsable: "+newQuery);
        comp.modificarQueryDataSet(newQuery);
        
        //Seccion Servicios
        comp.obtenerComponentesDataSet("SecServicios");
        condiciones = "f.codigo = "+facturasImprimir.get("codigoFactura_"+i)+" AND dfs.articulo IS NULL AND dfs.cantidad_cargo > 0 ";
        newQuery = comp.obtenerQueryDataSet().replace("1=2", condiciones);
        logger.info("===>Consulta Sec. Servicios: "+newQuery);
        comp.modificarQueryDataSet(newQuery);
        
        //Seccion Articulos
        comp.obtenerComponentesDataSet("SecArticulos");
        condiciones = "f.codigo = "+facturasImprimir.get("codigoFactura_"+i)+" AND dfs.servicio IS NULL AND dfs.cantidad_cargo > 0 ";
        newQuery = comp.obtenerQueryDataSet().replace("1=2", condiciones);
        logger.info("===>Consulta Sec. Articulos: "+newQuery);
        comp.modificarQueryDataSet(newQuery);
        
        //Seccion Totales
        comp.obtenerComponentesDataSet("SecTotales");
        condiciones = "f.codigo = "+facturasImprimir.get("codigoFactura_"+i)+"";
        newQuery = comp.obtenerQueryDataSet().replace("1=2", condiciones);
        logger.info("===>Consulta Sec. Totales: "+newQuery);
        comp.modificarQueryDataSet(newQuery);
        
        //debemos arreglar los alias para que funcione oracle, debido a que este los convierte a MAYUSCULAS
      	comp.lowerAliasDataSet();
		String newPathReport = comp.saveReport1(false);
        comp.updateJDBCParameters(newPathReport);
        if(!newPathReport.equals(""))
        {
        	return newPathReport;
        }
        return "";
	}
	
	/**
	 * Método implementado para editar los datos del encabezado, cuando se
	 * va a realizar la impresión de factura por formato de impresión específico
	 * @param comp
	 * @param con
	 * @param tipoFormato
	 * @param codigoFactura 
	 */
	private static void editarDatosEncabezado(DesignEngineApi comp, Connection con, int tipoFormato, int codigoFactura) throws IPSException 
	{
		int numFilas = 0; //mantiene el control de las filas que debe tener la cuadrícula
		//por defecto se toman 2 columnas
		String conSpan = ""; //almacena las filas que deben tener colspan (concatenadas con "-")
		String conBold = ""; //almacena las filas que deben tener negrilla
		Vector v = new Vector(); //Arreglo donde se almacenarán los datos del encabezado
		boolean huboDatos = false; //variable usada para berificar si existen datos para esta sección del encabezado
		
		//********CONSULTA DE LOS DATOS DEL ENCABEZADO*****************************************************
		FormatoImpresionFactura formatoEncabezado = new FormatoImpresionFactura();
		HashMap paciente = new HashMap();
		HashMap responsable = new HashMap();
		HashMap atencion = new HashMap();
		int numPaciente = 0;
		int numResponsable = 0;
		int numAtencion = 0;
		int codSeccion = 0; //almacena temporalmente el codigo de la seccion
		
		//Se consultan las prioridades de las secciones en el encabezado
		HashMap prioridades = formatoEncabezado.consultarPrioridadesSeccionEncabezado(con,tipoFormato);
		int numPrioridades = Integer.parseInt(prioridades.get("numRegistros")+"");
		
		if(numPrioridades>0)
		{
			//Si existen prioridades activas se consultan los datos de sus
			//secciones respectivas
			for(int i=0;i<numPrioridades;i++)
			{
				codSeccion = Integer.parseInt(prioridades.get("codigo_seccion_"+i)+"");
				switch(codSeccion)
				{
					case ConstantesBD.codigoSeccionFormatoFacturaPaciente:
						paciente = formatoEncabezado.consultarSubSeccionEncabezado(con,tipoFormato,codSeccion,true);
						numPaciente = Integer.parseInt(paciente.get("numRegistros")+"");
					break;
					case ConstantesBD.codigoSeccionFormatoFacturaResponsable:
						responsable = formatoEncabezado.consultarSubSeccionEncabezado(con,tipoFormato,codSeccion,true);
						numResponsable = Integer.parseInt(responsable.get("numRegistros")+"");
					break;
					case ConstantesBD.codigoSeccionFormatoFacturaAtencion:
						atencion = formatoEncabezado.consultarSubSeccionEncabezado(con,tipoFormato,codSeccion,true);
						numAtencion = Integer.parseInt(atencion.get("numRegistros")+"");
					break;
				}
			}
		}
		//******************************************************************************************************************************
	
		//************CARGA DE LOS DATOS DE LA FACTURA, CUENTA Y PACIENTE**********************************************
		
		PersonaBasica mundoPaciente = new PersonaBasica();
		Cuenta mundoCuenta = new Cuenta();
		Convenio mundoConvenio = new Convenio();
		Empresa mundoEmpresa = new Empresa();
		Tercero mundoTercero = new Tercero();
		Contrato mundoContrato = new Contrato();
		IngresoGeneral mundoIngreso = new IngresoGeneral();
		//Objetos de las admisiones
		AdmisionHospitalaria adminHospi = new AdmisionHospitalaria();
		AdmisionUrgencias adminUrg = new AdmisionUrgencias();
		//datos cuando el convenio tiene info Adicional
		HashMap datosTitular = new HashMap();
		HashMap datosPoliza = new HashMap();
		DtoFactura dtoFactura= Factura.cargarFactura(con, codigoFactura+"", false);
		try 
		{
			mundoPaciente.cargar(con,dtoFactura.getCodigoPaciente());
			mundoPaciente.cargarPaciente(con,dtoFactura.getCodigoPaciente(), dtoFactura.getInstitucion()+"","-1"); //el -1 se debe reemplazar por el codigoCentroAtencion de la sesion
			mundoCuenta.cargarCuenta(con, dtoFactura.getCuentas().get(0)+"");
			mundoConvenio.cargarResumen(con, dtoFactura.getConvenio().getCodigo());
			mundoEmpresa.cargar(con,mundoConvenio.getEmpresa());
			mundoTercero.cargarResumen(con,mundoEmpresa.getTercero());
			mundoContrato.cargarContratoPorSubCuenta(con, dtoFactura.getSubCuenta());
			//Carga de datos Titular y Póliza
			if(Utilidades.convenioTieneIngresoInfoAdic(con,dtoFactura.getConvenio().getCodigo()))
			{
				datosTitular = mundoIngreso.consultarInformacionTitularPoliza(con,Integer.parseInt(dtoFactura.getCuentas().get(0)+""));
				datosPoliza = mundoIngreso.consultarDatosAutorizacionPoliza(con,dtoFactura.getConvenio().getCodigo(), Integer.parseInt(dtoFactura.getCuentas().get(0)+""));
			}
			//Carga de datos de las admisiones
			if(dtoFactura.getViaIngreso().getCodigo()==ConstantesBD.codigoViaIngresoHospitalizacion)
				adminHospi.cargarUltimaAdmision(con,dtoFactura.getCodigoPaciente());
			if(dtoFactura.getViaIngreso().getCodigo()==ConstantesBD.codigoViaIngresoUrgencias)
				adminUrg.cargar(con,Integer.parseInt(dtoFactura.getCuentas().get(0)+""));
			
		} 
		catch (SQLException e) 
		{
			logger.error("Error cargando la factura en editarDatosInstitucion de FacutracionPdf: "+e);
		}
		//********************************************************************************************
		
		//*********EDICIÓN DE LOS DATOS DEL ENCABEZADO********************************************************************************
		int codCampo = 0; //almacena temporalmente el codigo del campo de una sección
		//Los datos se deben editar según el orden de prioridad (el mapa ya viene ordenado desde la consulta)
		for(int i=0;i<numPrioridades;i++)
		{
			codSeccion = Integer.parseInt(prioridades.get("codigo_seccion_"+i)+"");
			switch(codSeccion)
			{
				/** SECCION PACIENTE ****************************************************************************************************** **/
				case ConstantesBD.codigoSeccionFormatoFacturaPaciente:
					
					huboDatos = true;
					//El Título de la sección tiene un colspan
					if(!conSpan.equals(""))
						conSpan += "-";
					conSpan += numFilas ;
					//El Título de la sección lleva negrilla
					if(!conBold.equals(""))
						conBold += "-";
					conBold += numFilas ;
					//Titulo de la seccion
					numFilas ++;
					v.add("Información Paciente");
					
					
					if(numPaciente>0)
					{
						int numCampos = numPaciente;
						
						for(int j=0;j<numPaciente;j++)
						{
							codCampo = Integer.parseInt(paciente.get("codigocampo_"+j)+"");
							//Se verifica el campo para saber que tipo de información ingresar
							switch(codCampo)
							{
								case ConstantesBD.codigoDatoFormatoFacturaTipoNumeroId:
									v.add(paciente.get("descripcion_"+j)+" : "+mundoPaciente.getCodigoTipoIdentificacionPersona()+" "+mundoPaciente.getNumeroIdentificacionPersona());
								break;
								case ConstantesBD.codigoDatoFormatoFacturaDireccion:
									v.add(paciente.get("descripcion_"+j)+" : "+mundoPaciente.getDireccion());
								break;
								case ConstantesBD.codigoDatoFormatoFacturaTelefono:
									if(!mundoPaciente.getTelefono().equals(""))
										v.add(paciente.get("descripcion_"+j)+" : "+mundoPaciente.getTelefono());
									else
										numCampos--; //se omite el campo
								break;
								case ConstantesBD.codigoDatoFormatoFacturaCiudad:
									v.add(paciente.get("descripcion_"+j)+" : "+mundoPaciente.getNombreCiudadVivienda());
								break;
								case ConstantesBD.codigoDatoFormatoFacturaDepartamento:
									v.add(paciente.get("descripcion_"+j)+" : "+mundoPaciente.getNombreDeptoVivienda());
								break;
								case ConstantesBD.codigoDatoFormatoFacturaApellidosNombres:
									v.add(paciente.get("descripcion_"+j)+" : "+mundoPaciente.getApellidosNombresPersona(false));
								break;
								case ConstantesBD.codigoDatoFormatoFacturaFechaNacimiento:
									v.add(paciente.get("descripcion_"+j)+" : "+mundoPaciente.getFechaNacimiento());
								break;
								case ConstantesBD.codigoDatoFormatoFacturaEdad:
									v.add(
										paciente.get("descripcion_"+j) + " : " +
										Persona.calcularEdadDetalladaAFecha_dMy(
										mundoPaciente.getAnioNacimiento(),
										mundoPaciente.getMesNacimiento(),
										mundoPaciente.getDiaNacimiento(),
										mundoCuenta.getDiaApertura()+"/"+mundoCuenta.getMesApertura()+"/"+mundoCuenta.getAnioApertura())
										);
								break;
								case ConstantesBD.codigoDatoFormatoFacturaSexo:
									v.add(paciente.get("descripcion_"+j)+" : "+mundoPaciente.getSexo());
								break;
							}
						}
						
						//Se calcula el número de filas que debe tener la cuadrícula para los campos
						numFilas = numFilas + (numCampos/3 + (numCampos%3>0?1:0)); 
						
						
						//Se calculan la celdas restantes
						int celdasRestantes = ((numCampos/3 + (numCampos%3>0?1:0))*3) - numCampos;
						for(int j=0;j<celdasRestantes;j++)
							//se añaden las celdas vacías
							v.add("   ");
					}
					else
					{
						//Se asignan los datos por defecto
						numFilas += 2;
						v.add("Apellidos y Nombres: "+mundoPaciente.getApellidosNombresPersona(false));
						v.add("Tipo y Nº ID: "+mundoPaciente.getCodigoTipoIdentificacionPersona()+" "+mundoPaciente.getNumeroIdentificacionPersona());
						v.add("Fecha Nacimiento: "+mundoPaciente.getFechaNacimiento());
						v.add("Edad: " +Persona.calcularEdadDetalladaAFecha_dMy(mundoPaciente.getAnioNacimiento(),mundoPaciente.getMesNacimiento(),mundoPaciente.getDiaNacimiento(),mundoCuenta.getDiaApertura()+"/"+mundoCuenta.getMesApertura()+"/"+mundoCuenta.getAnioApertura()));
						v.add("   ");
						v.add("   ");
					}
					
				break;
				
				/** SECCIÓN RESPONSABLE ****************************************************************************************************** **/
				case ConstantesBD.codigoSeccionFormatoFacturaResponsable:
					huboDatos = true;
					//El Título de la sección tiene un colspan
					if(!conSpan.equals(""))
						conSpan += "-";
					conSpan += numFilas ;
					//El Título de la sección lleva negrilla
					if(!conBold.equals(""))
						conBold += "-";
					conBold += numFilas ;
					//Titulo de la seccion
					numFilas ++;
					v.add("Información del Responsable");
					
					
					if(numResponsable>0)
					{
						int numCampos = numResponsable; //se asigna temporalmente el numero de campos (ya que podrían cambiar)
						boolean imprimirTitular = false;
						
						for(int j=0;j<numResponsable;j++)
						{
							codCampo = Integer.parseInt(responsable.get("codigocampo_"+j)+"");
							//Se verifica el campo para saber que tipo de información ingresar
							switch(codCampo)
							{
								case ConstantesBD.codigoDatoFormatoFacturaTipoNumeroId:
									v.add(responsable.get("descripcion_"+j)+" : "+mundoTercero.getNumeroIdentificacion());
								break;
								case ConstantesBD.codigoDatoFormatoFacturaDireccion:
									v.add(responsable.get("descripcion_"+j)+" : "+mundoEmpresa.getDireccion());
								break;
								case ConstantesBD.codigoDatoFormatoFacturaTelefono:
									v.add(responsable.get("descripcion_"+j)+" : "+mundoEmpresa.getTelefono());
								break;
								case ConstantesBD.codigoDatoFormatoFacturaCodigoMinSal:
									v.add(responsable.get("descripcion_"+j)+" : "+mundoConvenio.getCodigoMinSalud());
								break;
								case ConstantesBD.codigoDatoFormatoFacturaEmpresa:
									v.add(responsable.get("descripcion_"+j)+" : "+mundoEmpresa.getRazonSocial());
								break;
								case ConstantesBD.codigoDatoFormatoFacturaConvenio:
									v.add(responsable.get("descripcion_"+j)+" : "+mundoConvenio.getNombre());
								break;
								case ConstantesBD.codigoDatoFormatoFacturaPlanBeneficios:
									v.add(responsable.get("descripcion_"+j)+" : "+mundoConvenio.getPlanBeneficios());
								break;
								case ConstantesBD.codigoDatoFormatoFacturaEmail:
									if(!mundoEmpresa.getCorreo().equals(""))
										v.add(responsable.get("descripcion_"+j)+" : "+mundoEmpresa.getCorreo());
									else
										numCampos--; //no se cuenta como campo
								break;
								case ConstantesBD.codigoDatoFormatoFacturaImprimirTitularParticular:
									//Se verifica si el convenio tiene info. adicional en apertura cuenta
									if(Utilidades.convenioTieneIngresoInfoAdic(con, dtoFactura.getConvenio().getCodigo()))
										//se activa la impresiñon de titular
										imprimirTitular = true;
									numCampos--; //no se cuenta como campo
								break;
								case ConstantesBD.codigoDatoFormatoFacturaContratoTitular:
									if(imprimirTitular)
										v.add(responsable.get("descripcion_"+j)+" : "+mundoContrato.getNumeroContrato());
									else
										numCampos--; //no se toma en cuenta el campo
								break;
								case ConstantesBD.codigoDatoFormatoFacturaApellidosNombresTitular:
									if(imprimirTitular)
										v.add(responsable.get("descripcion_"+j)+" : "+datosTitular.get("apellidotitular_0")+" "+datosTitular.get("nombretitular_0"));
									else
										numCampos--; //no se toma en cuenta el campo
								break;
								case ConstantesBD.codigoDatoFormatoFacturaTipoNumeroIdTitular:
									if(imprimirTitular)
										v.add(responsable.get("descripcion_"+j)+" : "+datosTitular.get("tipoid_0")+" "+datosTitular.get("numeroid_0"));
									else
										numCampos--; //no se toma en cuenta el campo
								break;
								case ConstantesBD.codigoDatoFormatoFacturaDireccionTitular:
									if(imprimirTitular)
										v.add(responsable.get("descripcion_"+j)+" : "+datosTitular.get("direccion_0"));
									else
										numCampos--; //no se toma en cuenta el campo
								break;
								case ConstantesBD.codigoDatoFormatoFacturaTelefonoTitular:
									if(imprimirTitular&&!(datosTitular.get("telefono_0")+"").equals(""))
										v.add(responsable.get("descripcion_"+j)+" : "+datosTitular.get("telefono_0"));
									else
										numCampos--; //no se toma en cuenta el campo
								break;
								case ConstantesBD.codigoDatoFormatoFacturaAutorizacionTitular:
									if(imprimirTitular)
									{
										//Se separan por comas las autorizaciones
										String tempAuth = "";
										for(int k=0;k<Integer.parseInt(datosPoliza.get("numRegistros")+"");k++)
										{
											if(!tempAuth.equals(""))
												tempAuth += ", ";
											tempAuth += datosPoliza.get("numeroautorizacion_"+k);
										}
										v.add(responsable.get("descripcion_"+j)+" : "+tempAuth);
									}
									else
										numCampos--; //no se toma en cuenta el campo
								break;
								case ConstantesBD.codigoDatoFormatoFacturaMontoTitular:
									if(imprimirTitular)
									{
										//Se realiza sumatoria de montos
										double sumMontos = 0;
										for(int k=0;k<Integer.parseInt(datosPoliza.get("numRegistros")+"");k++)
											sumMontos += Double.parseDouble(datosPoliza.get("valormonto_"+k)+"");
										v.add(responsable.get("descripcion_"+j)+" : "+UtilidadTexto.formatearValores(sumMontos));
									}
									else
										numCampos--; //no se toma en cuenta el campo
								break;
									
							}
						}
						
						//Se calcula el número de filas que debe tener la cuadrícula para los campos
						numFilas = numFilas + (numCampos/3 + (numCampos%3>0?1:0)); 
						
						
						//Se calculan la celdas restantes
						int celdasRestantes = ((numCampos/3 + (numCampos%3>0?1:0))*3) - numCampos;
						for(int j=0;j<celdasRestantes;j++)
							//se añaden las celdas vacías
							v.add("   ");
					}
					else
					{
						//Se asignan los datos por defecto
						numFilas ++;
						v.add("Empresa: "+mundoEmpresa.getRazonSocial());
						v.add("Tipo y Nº ID: "+mundoTercero.getNumeroIdentificacion());
						v.add("Convenio: "+mundoConvenio.getNombre());
					}
				break;
				
				/** SECCIÓN ATENCIÓN ********************************************************************************** **/
				case ConstantesBD.codigoSeccionFormatoFacturaAtencion:
					
					if(numAtencion>0)
					{
						huboDatos = true;
						///El Título de la sección tiene un colspan
						if(!conSpan.equals(""))
							conSpan += "-";
						conSpan += numFilas ;
						//El Título de la sección lleva negrilla
						if(!conBold.equals(""))
							conBold += "-";
						conBold += numFilas ;
						//Titulo de la seccion
						numFilas ++;
						v.add("Información Atención");
						
						
						int numCampos = numAtencion; //se asigna temporalmente el numero de campos (ya que podrían cambiar)
						
						for(int j=0;j<numAtencion;j++)
						{
							codCampo = Integer.parseInt(atencion.get("codigocampo_"+j)+"");
							//Se verifica el campo para saber que tipo de información ingresar
							switch(codCampo)
							{
								case ConstantesBD.codigoDatoFormatoFacturaViaIngreso:
									v.add(atencion.get("descripcion_"+j)+" : "+dtoFactura.getViaIngreso().getNombre());
								break;
								case ConstantesBD.codigoDatoFormatoFacturaRegimen:
									v.add(atencion.get("descripcion_"+j)+" : "+dtoFactura.getTipoRegimen().getNombre());
								break;
								case ConstantesBD.codigoDatoFormatoFacturaClasificacionSocioeconomica:
									v.add(atencion.get("descripcion_"+j)+" : "+dtoFactura.getEstratoSocial().getNombre());
								break;
								case ConstantesBD.codigoDatoFormatoFacturaTipoAfiliado:
									v.add(atencion.get("descripcion_"+j)+" : "+dtoFactura.getTipoAfiliado());
								break;
								case ConstantesBD.codigoDatoFormatoFacturaTipoMonto:
									v.add(atencion.get("descripcion_"+j)+" : "+dtoFactura.getTipoMonto().getNombre());
								break;
								case ConstantesBD.codigoDatoFormatoFacturaNaturaleza:
									v.add(atencion.get("descripcion_"+j)+" : "+mundoCuenta.getNaturalezaPaciente());
								break;
								case ConstantesBD.codigoDatoFormatoFacturaCarnet:
									v.add(atencion.get("descripcion_"+j)+" : "+mundoCuenta.getNumeroCarnet());
								break;
								case ConstantesBD.codigoDatoFormatoFacturaAccidenteTransito:
									v.add(atencion.get("descripcion_"+j)+" : "+(mundoCuenta.isIndicativoAccidenteTransito()?"Sí":"No"));
								break;
								case ConstantesBD.codigoDatoFormatoFacturaNoPoliza:
									//Se verifica si el campo existe
									if(mundoCuenta.isIndicativoAccidenteTransito()&&!mundoCuenta.getNumeroPoliza().equals(""))
										v.add(atencion.get("descripcion_"+j)+" : "+mundoCuenta.getNumeroPoliza());
									else
										numCampos--; //no se toma en cuenta el campo
								break;
								/*
								case ConstantesBD.codigoDatoFormatoFacturaAutorizacionAdmision:
									if(dtoFactura.getViaIngreso().getCodigo()==ConstantesBD.codigoViaIngresoHospitalizacion&&!adminHospi.getNumeroAutorizacion().equals(""))
										v.add(atencion.get("descripcion_"+j)+" : "+adminHospi.getNumeroAutorizacion());
									else if(dtoFactura.getViaIngreso().getCodigo()==ConstantesBD.codigoViaIngresoUrgencias&&!adminUrg.getNumeroAutorizacion().equals(""))
										v.add(atencion.get("descripcion_"+j)+" : "+adminUrg.getNumeroAutorizacion());
									else
										numCampos--;//no se toma en cuenta el campo
								break;
								*/
								case ConstantesBD.codigoDatoFormatoFacturaFechaIngreso:
									if(dtoFactura.getViaIngreso().getCodigo()==ConstantesBD.codigoViaIngresoAmbulatorios||
										dtoFactura.getViaIngreso().getCodigo()==ConstantesBD.codigoViaIngresoConsultaExterna)
										v.add(atencion.get("descripcion_"+j)+" : "+mundoCuenta.getDiaApertura()+"/"+mundoCuenta.getMesApertura()+"/"+mundoCuenta.getAnioApertura());
									else if(dtoFactura.getViaIngreso().getCodigo()==ConstantesBD.codigoViaIngresoHospitalizacion)
										v.add(atencion.get("descripcion_"+j)+" : "+UtilidadFecha.conversionFormatoFechaAAp(adminHospi.getFecha()));
									else if(dtoFactura.getViaIngreso().getCodigo()==ConstantesBD.codigoViaIngresoUrgencias)
										v.add(atencion.get("descripcion_"+j)+" : "+adminUrg.getFecha());
									else
										numCampos--; //no se toma en cuenta el campo
								break;
								case ConstantesBD.codigoDatoFormatoFacturaDxIngreso:
									String tempDxIngreso = Utilidades.getNombreDiagnosticoIngreso(con, Integer.parseInt(dtoFactura.getCuentas().get(0).toString()));
									if(dtoFactura.getViaIngreso().getCodigo()==ConstantesBD.codigoViaIngresoHospitalizacion&&!tempDxIngreso.equals(""))
										v.add(atencion.get("descripcion_"+j)+" : "+tempDxIngreso);
									else
										numCampos--; //no se toma en cuenta el campo
								break;
								case ConstantesBD.codigoDatoFormatoFacturaFechaEgreso:
									String tempFechaEgreso = Utilidades.getFechaEgreso(con,Integer.parseInt(dtoFactura.getCuentas().get(0).toString()));
									if(dtoFactura.getViaIngreso().getCodigo()==ConstantesBD.codigoViaIngresoAmbulatorios||
										dtoFactura.getViaIngreso().getCodigo()==ConstantesBD.codigoViaIngresoConsultaExterna)
										v.add(atencion.get("descripcion_"+j)+" : "+mundoCuenta.getDiaApertura()+"/"+mundoCuenta.getMesApertura()+"/"+mundoCuenta.getAnioApertura());
									else if((dtoFactura.getViaIngreso().getCodigo()==ConstantesBD.codigoViaIngresoHospitalizacion||
										dtoFactura.getViaIngreso().getCodigo()==ConstantesBD.codigoViaIngresoUrgencias)&&
										!tempFechaEgreso.equals(""))
										v.add(atencion.get("descripcion_"+j)+" : "+tempFechaEgreso);
									else
										numCampos--; //no se toma en cuenta el campo
								break;
								case ConstantesBD.codigoDatoFormatoFacturaDxEgreso:
									String tempDxEgreso = Utilidades.getNombreDiagnosticoEgreso(con,Integer.parseInt(dtoFactura.getCuentas().get(0).toString()));
									if(!tempDxEgreso.equals(""))
										v.add(atencion.get("descripcion_"+j)+" : "+tempDxEgreso);
									else
										numCampos--; //no se toma en cuenta el campo
								break;
							}
						}
						
						//Se calcula el número de filas que debe tener la cuadrícula para los campos
						numFilas = numFilas + (numCampos/3 + (numCampos%3>0?1:0)); 
						
						
						//Se calculan la celdas restantes
						int celdasRestantes = ((numCampos/3 + (numCampos%3>0?1:0))*3) - numCampos;
						for(int j=0;j<celdasRestantes;j++)
							//se añaden las celdas vacías
							v.add("   ");
					}
				break;
			}
		}
		//****************************************************************************************************************************
		
		//se verifica si hubo datos
		if(huboDatos)
		{
		
			//Se inserta cuadrícula en la segunda celda del encabezado
	        comp.insertGridHeaderOfMasterPageWithName(1,0,3,numFilas,"Encabezado");
	        
	        //*******APLICACION DE LOS COLSPAN********************************
	        String[] posiciones = conSpan.split("-");
	        for(int i=0;i<posiciones.length;i++)
	        	//Se realiza el colspan a la primera celda de la cuadrícula ubicada en la segunda celda del encabezado
	        	comp.colSpanCellGridHeaderOfMasterPage(1,0,Integer.parseInt(posiciones[i]),0,3,DesignChoiceConstants.TEXT_ALIGN_LEFT);
	        //****************************************************************
	        //******APLICACION DE NEGRILLA***********************************
	        posiciones = conBold.split("-");
	        for(int i=0;i<posiciones.length;i++)
	        	//Se aplica NEGRILLA a la primera celda de la cuadrícula ubicada en la tercera celda del encabezado
	        	comp.fontWeightCellGridHeaderOfMasterPage(1,0,Integer.parseInt(posiciones[i]),0,DesignChoiceConstants.FONT_WEIGHT_BOLD);
	        
	        //Se insertan los datos del encabezado en la cuadrícula
	        comp.insertLabelInGridOfMasterPage(1,0,v);
		}
    }
	
	/**
	 * Método implementado para editar los datos de la institucion, cuando se
	 * va a realizar la impresión de factura por formato de impresión específico
	 * @param comp
	 * @param con
	 * @param usuario
	 * @param tipoFormato 
	 * @param consecutivoFactura 
	 */
	private static void editarDatosInstitucion(DesignEngineApi comp, Connection con, UsuarioBasico usuario, int tipoFormato, int codigoFactura) 
	{
		int numFilas = 0; //mantiene el control de las filas que debe tener la cuadrícula de institucion
		//por defecto se toman 2 Columnas
		String conSpan = ""; //almacena las filas que deben tener colspan (concatenadas con "-")
		Vector v=new Vector(); //Arreglo donde se almacenarán los datos del encabezado
		
		//Se asigna el LOGO de la institución en la primera celda de la cuaricula del encabezado
		comp.insertImageHeaderOfMasterPage1(0, 0, ValoresPorDefecto.getDirectorioImagenes()+"logo_clinica_sana_login.gif");
		
		//*****CONSULTA DE LOS DATOS FORMATO INSTITUCION ENCABEZADO**********
		FormatoImpresionFactura formatoInstitucion = new FormatoImpresionFactura();
		HashMap observaciones = formatoInstitucion.consultarDatosSecEncabezado(con,tipoFormato);
		HashMap campos = formatoInstitucion.consultarSubSeccionEncabezado(con,tipoFormato,ConstantesBD.codigoSeccionFormatoFacturaInstitucion,true);
		//se toma el número de registros de las consultas
		int numObservaciones = Integer.parseInt(observaciones.get("numRegistros")+"");
		int numCampos = Integer.parseInt(campos.get("numRegistros")+"");
		//***************************************************************************
		
		//*******SE CARGAN LOS DATOS DE LA INSTITUCION*******************************+
        ParametrizacionInstitucion ins=new ParametrizacionInstitucion();
        ins.cargar(con,usuario.getCodigoInstitucionInt());
        //*********************************************************************************
		
		//1) OBSERVACIONES INICIALES **************************************
		//Se verifica que hayan datos en observaciones y que la observacion
		//inicial esté llena
		if(numObservaciones>0&&!(observaciones.get("obsiniciales")+"").trim().equals(""))
		{
			conSpan += numFilas; //Las observaciones iniciales requieren colspan
			numFilas ++; //se aumenta número de filas
			v.add(observaciones.get("obsiniciales")); //se añade al vector las observaciones iniciales
		}
		//**************************************************************************************
		
		//2) CAMPOS DE LA INSTITUCION **************************************
		//El primer campo de la sección tiene un colspan
		if(!conSpan.equals(""))
			conSpan += "-";
		conSpan += numFilas ;
			
		//Se verifica si hay campos para imprimir desde el formato
		if(numCampos>0)
		{
			int numCamposTemp = numCampos;
			int codigoCampo = 0; //almacena temporalmente el codigo del campo
			//Se iteran los campos
			for(int i=0;i<numCampos;i++)
			{
				codigoCampo = Integer.parseInt(campos.get("codigocampo_"+i)+"");
				//Se verifica que tipo de campo es:
				switch(codigoCampo)
				{
					case ConstantesBD.codigoDatoFormatoFacturaRazonSocial:
						v.add(ins.getRazonSocial().toUpperCase());
					break;
					case ConstantesBD.codigoDatoFormatoFacturaTipoNumeroId:
						v.add(ins.getDescripcionTipoIdentificacion()+" : "+ins.getNit());
					break;
					case ConstantesBD.codigoDatoFormatoFacturaDireccion:
						v.add(campos.get("descripcion_"+i)+" : "+ins.getDireccion());
					break;
					case ConstantesBD.codigoDatoFormatoFacturaTelefono:
						v.add(campos.get("descripcion_"+i)+" : "+ins.getTelefono());
					break;
					case ConstantesBD.codigoDatoFormatoFacturaCiudad:
						v.add(campos.get("descripcion_"+i)+" : "+ins.getCiudad().getNombre());
					break;
					case ConstantesBD.codigoDatoFormatoFacturaDepartamento:
						v.add(campos.get("descripcion_"+i)+" : "+ins.getDepartamento().getNombre());
					break;
					case ConstantesBD.codigoDatoFormatoFacturaCodigoMinSal:
						if(!ins.getCodMinSalud().equals(""))
							v.add(campos.get("descripcion_"+i)+" : "+ins.getCodMinSalud());
						else
							numCamposTemp--; //se omite el campo
					break;
					case ConstantesBD.codigoDatoFormatoFacturaActividadEconomica:
						if(!ins.getActividadEconomica().equals(""))
							v.add(campos.get("descripcion_"+i)+" : "+ins.getActividadEconomica());
						else
							numCamposTemp--; //se omite el campo
					break;
					case ConstantesBD.codigoDatoFormatoFacturaResolucion:
						if(!ins.getResolucion().equals(""))
							v.add(campos.get("descripcion_"+i)+" : "+ins.getResolucion());
						else
							numCamposTemp--; //se omite el campo
					break;
					case ConstantesBD.codigoDatoFormatoFacturaRangoInicialFinalFactura:
						v.add(campos.get("descripcion_"+i)+" : Desde "+ins.getRangoInicialFactura()+" hasta "+ins.getRangoFinalFactura());
					break;
				}
			}
			
			///Se calcula el número de filas que debe tener la cuadrícula para los campos
			numFilas = numFilas + (numCamposTemp/2 + 1); //se suma 1 porque la primera celda tiene colspan y corre las celdas 1 espacio
			
			//Para el caso de que el numero de campos sea par se hace un colspan en la ultima fila
			if(numCamposTemp%2==0)
			{
				//El último campo de la sección también tiene colspan
				if(!conSpan.equals(""))
					conSpan += "-";
				conSpan += (numFilas-1) ;
			}
		}
		else
		{
			//todas las filas en esta parte tienen colspan
			if(!conSpan.equals(""))
				conSpan += "-";
			conSpan += (numFilas+1) ;
			
			//Por defecto se aumentan 2 filas mas
			numFilas += 2;
			v.add(ins.getRazonSocial());
			v.add(ins.getDescripcionTipoIdentificacion()+" : "+ins.getNit());
		}
			
		//******************************************************************
		//3) OBSERVACIONES INSTITUCION **************************************
		//Se verifica que hayan datos en observaciones y que la observacion
		//institucin¡onal esté llena
		if(numObservaciones>0&&!(observaciones.get("obsinstitucion")+"").trim().equals(""))
		{
			if(!conSpan.equals(""))
				conSpan += "-";
			conSpan += numFilas; //Las observaciones iniciales requieren colspan
			numFilas ++; //se aumenta número de filas
			v.add(observaciones.get("obsinstitucion")); //se añade al vector las observaciones institucionales
		}
		//**************************************************************************************
		
		//4) *********DATOS DE LA FACTURA*****************************************************+
		//Se consultan los datos generales de la factura
		int estadoFactura = 0;
		String fechaElaboracion = "";
		String infoAnulacion = "";
		
		DtoFactura dtoFactura= Factura.cargarFactura(con, codigoFactura+"", false);
		
		estadoFactura = dtoFactura.getEstadoFacturacion().getCodigo();
		fechaElaboracion = dtoFactura.getFecha();
		
		
		//Se verifica si la factura está anulada
		if(estadoFactura==ConstantesBD.codigoEstadoFacturacionAnulada)
		{
			infoAnulacion = Utilidades.obtenerInformacionAnulacionFactura(con,codigoFactura+"");
			if(!conSpan.equals(""))
				conSpan += "-";
			conSpan += numFilas; //El reporte de factura anulada requiere colspan
			numFilas ++; //se aumenta número de filas
			///se añade al vector aviso de que la factura está anulada
			v.add("FACTURA ANULADA "+infoAnulacion.split(ConstantesBD.separadorSplit)[1]+" "+UtilidadFecha.convertirHoraACincoCaracteres(infoAnulacion.split(ConstantesBD.separadorSplit)[2])); 
		}
		
		numFilas ++;
		v.add("Factura Nº "+(ins.getPrefijoFacturas().equals("")?"":ins.getPrefijoFacturas()+"-")+dtoFactura.getConsecutivoFactura());
		v.add("Fecha: "+fechaElaboracion);
		
		//*************************************************************************************
		//Se inserta cuadrícula en la segunda celda del encabezado
        comp.insertGridHeaderOfMasterPage(0,1,2,numFilas);
        
        //*******APLICACION DE LOS COLSPAN********************************
        String[] posiciones = conSpan.split("-");
        for(int i=0;i<posiciones.length;i++)
        	//Se realiza el colspan a la primera celda de la cuadrícula ubicada en la segunda celda del encabezado
        	comp.colSpanCellGridHeaderOfMasterPage(0,1,Integer.parseInt(posiciones[i]),0,2,DesignChoiceConstants.TEXT_ALIGN_CENTER);
        //****************************************************************
        
        //Se insertan los datos de la institucion en la cuadrícula
        comp.insertLabelInGridOfMasterPage(0,1,v);
        //comp.insertLabelInGridPpalOfHeader(1,0,"ESTA ES UNA PRUEBA DE BIRT");
	}
	
	/**
	 * Metodo para Editar los datos de la seeccion de totales de la factura.
	 * @param comp
	 * @param con
	 * @param tipoFormato
	 * @param consecutivoFactura
	 */
	private static void editarDatosSecTotalesImpresion(DesignEngineApi comp, Connection con, int tipoFormato, int consecutivoFactura)
	{
		Vector v = new Vector();
		FormatoImpresionFactura mundoFormato = new FormatoImpresionFactura();
		HashMap mapaSecTotales = new HashMap();
		int j = 0;
		
		//Consulamos los datos de la seccion de totales con sus valores correspondientes
		mapaSecTotales = mundoFormato.consultaCamposValoresSecTotales(con, consecutivoFactura, tipoFormato);
		int numFilas = Integer.parseInt(mapaSecTotales.get("numRegistros")+"")-2 ;
		
		//Recorremos para los campos de la seccion de totales con su valor respectivo
		for(int i = 0 ; i < numFilas ; i++)
		{
			v.add(j, mapaSecTotales.get("descripcioncampo_"+i));
			j++;
			v.add(j, UtilidadTexto.formatearValores(mapaSecTotales.get("total_"+i)+""));
			j++;
		}
		
		//Insertamos una grilla fija para la seccion de los totales dentro de una celda especifica
		//determinada por la plantilla de la impresion de la factura
		comp.insertGridInBodyPageWithName(2, 1, 2, numFilas, "tablaTotales");
		
		//Insertar el vector ya con los label's de los datos a Imprimir
		comp.insertLabelInGridOfBodyPage(2, 1, v);
		//Alineamos los valores de los totales a la derecha
		comp.colAlingmentGridBodyPage(2, 1, 1, DesignChoiceConstants.COLUMN_ALIGN_RIGHT);
	}

	/**
	 * Método para insertar el valor en letras y las observaciones del pie de página de la
	 * impresion de la factura
	 * @param comp
	 * @param con
	 * @param tipoFormato
	 * @param consecutivoFactura
	 */
	private static void editarDatosPiePaginaImpresion(DesignEngineApi comp, Connection con, int tipoFormato, int consecutivoFactura)
	{
		Vector v = new Vector();
		FormatoImpresionFactura mundoFormato = new FormatoImpresionFactura();
		HashMap mapaDatosSecNotaPie =  new HashMap();
		HashMap mapaSecTotales = new HashMap();
		HashMap mapaValorLetras = new HashMap();
		String obsActEconomica = "";
		boolean imprimirValLetras = false;
		String descAntesValLetras = "";
		String descDespuesValLetras = "";
		
		try
		{
			//Consulamos los datos de la seccion de totales con sus valores correspondientes
			mapaSecTotales = mundoFormato.consultaCamposValoresSecTotales(con, consecutivoFactura, tipoFormato);
			int numFilas = Integer.parseInt(mapaSecTotales.get("numRegistros")+"")-2 ;
			//Sacamos las dos descripciones del valor en letras por separado
			for(int j = numFilas ; j < Integer.parseInt(mapaSecTotales.get("numRegistros")+"") ; j++ )
			{
				descAntesValLetras = UtilidadTexto.isEmpty(mapaSecTotales.get("descripcioncampo_"+j)+"")?"":mapaSecTotales.get("descripcioncampo_"+j)+"";
				descDespuesValLetras =UtilidadTexto.isEmpty(mapaSecTotales.get("descripcioncampo_"+j)+"")?"":mapaSecTotales.get("descripcioncampo_"+j)+""; 
			}
			
			//SECCION PARA LOS VALORES EN LETRAS
			//Consultamos los datos de la seccion de totales para la parte del valor en letras
			mapaValorLetras = mundoFormato.consultarSecTotales(con, tipoFormato);
			int codValLetras = 0;
			String impValLetras = "";
			//Para ver si tiene el atributio de imprimir firmas
			if(mapaValorLetras.containsKey("impvalletras"))
			{
				if(mapaValorLetras.get("impvalletras")==null)
				{
					imprimirValLetras = false;
				}
				else
				{
					impValLetras = mapaValorLetras.get("impvalletras")+"";
				}
			}
			if(impValLetras.equals("true"))
			{
				imprimirValLetras = true;
				codValLetras = Integer.parseInt(mapaValorLetras.get("codvalletras")+"");
			}
			else
			{
				imprimirValLetras = false ;
				codValLetras =  ConstantesBD.codigoNuncaValido;
			}
			
			if(imprimirValLetras)
			{
				if(codValLetras != ConstantesBD.codigoNuncaValido)
				{
					switch (codValLetras)
					{
						case ConstantesBD.codigoValorLetrasTotalCargos:
							v.add(0, descAntesValLetras +" "+ UtilidadTexto.formatearValores(mundoFormato.consultaValorTotalXFacturaXCampo(con, consecutivoFactura, ConstantesBD.codigoValorLetrasTotalCargos)+"") +" "+ descDespuesValLetras);
						break;
						case ConstantesBD.codigoValorLetrasTotalConvenio:
							v.add(0, descAntesValLetras +" "+ UtilidadTexto.formatearValores(mundoFormato.consultaValorTotalXFacturaXCampo(con, consecutivoFactura, ConstantesBD.codigoValorLetrasTotalConvenio)+"") +" "+ descDespuesValLetras);
						break;
						case ConstantesBD.codigoValorLetrasTotalPaciente:
							v.add(0, descAntesValLetras +" "+ UtilidadTexto.formatearValores(mundoFormato.consultaValorTotalXFacturaXCampo(con, consecutivoFactura, ConstantesBD.codigoValorLetrasTotalPaciente)+"") +" "+ descDespuesValLetras);
						break;
						case ConstantesBD.codigoValorLetrasTotalNetoPaciente:
							v.add(0, descAntesValLetras +" "+ UtilidadTexto.formatearValores(mundoFormato.consultaValorTotalXFacturaXCampo(con, consecutivoFactura, ConstantesBD.codigoValorLetrasTotalNetoPaciente)+"") +" "+ descDespuesValLetras);
						break;
					}
				}
			}
			
			//SECCION PARA LAS OBSERVACIONES DE LA ACTIVIDAD ECONOMICA DE LA INSTITUCION
			mapaDatosSecNotaPie= mundoFormato.consultarDatosSecNotaPie(con, tipoFormato);
			obsActEconomica = mapaDatosSecNotaPie.get("obsactecono")+"";
			//Adicionamos las Observaciones
			//no se ha insertado nada en el mapa, debe insertar 0
			if(v.size()==0)
				v.add("");
			
			v.add(1, obsActEconomica);
			
			//Insertamos una grilla en la posicion 2,0 de la grilla de la plantilla diseñada
			//para la impresion de la factura
			comp.insertGridInBodyPageWithName(3, 0, 1, 2, "tablaObservaciones");
			//Insertar el vector ya con los label's de los datos a Imprimir
			comp.insertLabelInGridOfBodyPage(3, 0, v);
		}
		catch (SQLException e) 
		{
			logger.error("Error en editarDatosPiePaginaImpresion [FacutracionPdf] "+e);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Método para insertar la seccion de firmas en la Impresion dinamica de la factura
	 * @param comp
	 * @param con
	 * @param tipoFormato
	 */
	private static void editarDatosFirmasImpresionFactura(DesignEngineApi comp, Connection con, int tipoFormato)
	{
		Vector v = new Vector();
		FormatoImpresionFactura mundoFormato = new FormatoImpresionFactura();
		HashMap mapaDatosSecNotaPie =  new HashMap();
		HashMap mapaSecFirmas = new HashMap();
		boolean imprimirFirmas = false;
		int numCols = 0;
		
		
		try
		{
			mapaDatosSecNotaPie = mundoFormato.consultarDatosSecNotaPie(con, tipoFormato);
			//SECCION PARA LAS FIRMAS
			String impFirmas = "";
			//Para ver si tiene el atributio de imprimir firmas
			if(mapaDatosSecNotaPie.containsKey("impfirmas"))
			{
				if(mapaDatosSecNotaPie.get("impfirmas") == null)
				{
					imprimirFirmas = false;
				}
				else
				{
					impFirmas = mapaDatosSecNotaPie.get("impfirmas")+"";
				}
			}
			else
			{
				imprimirFirmas = false;
			}
			//Cambiamos la cadena por un boolean segun la cadena que llegue
			if(impFirmas.equals("true"))
			{
				imprimirFirmas = true;
			}
			else
			{
				imprimirFirmas = false;
			}
			if(imprimirFirmas)
			{
				//En este punto ya tenemos las firmas que se deben imprimir
				mapaSecFirmas = mundoFormato.consultarFirmas(con, tipoFormato);
				numCols = Integer.parseInt(mapaSecFirmas.get("numRegistros")+"");
			}
			
			//Recorremos "mapaSecFirmas" con las firmas parameteizadas
			//y las vamos adicionando al vector
			if(mapaSecFirmas.containsKey("numRegistros"))
			{
				for(int j = 0 ; j < Integer.parseInt(mapaSecFirmas.get("numRegistros")+"") ; j++)
				{
					v.add(j, mapaSecFirmas.get("descripcionfirma_"+j)+"");
				}
			}
			//Insertamos una grilla en la posicion 0,3 de la grilla de la plantilla diseñada
			//para la impresion de la factura
			comp.insertGridInBodyPageWithName(4, 0, numCols, 1, "tablaFirmas");
			//Insertar el vector ya con los label's de los datos a Imprimir
			comp.insertLabelInGridOfBodyPage(4, 0, v);
			
		}
		catch (SQLException e) 
		{
			logger.error("Error en editarDatosFirmasImpresionFactura [FacutracionPdf] "+e);
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Método para generar el cupero de la Impresion de la factura segun los datos parametrizados
	 * Insertar un vector con todos los datos organizados (Servicios - Articulos).
	 * @param comp
	 * @param con
	 * @param usuario
	 * @param tipoFormato
	 * @param codigoFactura
	 */
	private static void editarDatosCuerpoImpresion(DesignEngineApi comp, Connection con, UsuarioBasico usuario, int tipoFormato, int codigoFactura)
	{
		Vector v = new Vector(); //Vector para alamcenar los datos
		FormatoImpresionFactura mundoFormato = new FormatoImpresionFactura();
		HashMap mapaParamSecPpal = new HashMap();
		HashMap mapaServicios = new HashMap();
		HashMap mapaArticulos = new HashMap();
		HashMap serviciosQx = new HashMap();
		int numColumnas = 0;
		int numeroFilas = 0;
		boolean existeParamColumnas = false;
		boolean existeParamConseReg = false;
		boolean existeParamCodInterno = false;
		int contadorFilas = 0;
		HashMap mapaRegistrosNegrilla = new HashMap();
		mapaRegistrosNegrilla.put("numRegistros", "0");
		int posMapaNegrilla = Integer.parseInt(mapaRegistrosNegrilla.get("numRegistros")+"");
		int colsAdicionales = 0;
		int consecutivoRegistro = 1;
		
		try
		{
			//Cargamos los datos parametrizados de la seccion Principal del formato de impresion de factura
			mapaParamSecPpal = mundoFormato.consultarDatosSecPpalParametrizados(con, tipoFormato);
			//El numero de registros de este mapa corresponde al numero de columnas
			//que voy a mostrar en el cuerpo de la impresion
			numColumnas = mapaParamSecPpal.containsKey("numRegistros")?Integer.parseInt(mapaParamSecPpal.get("numRegistros")+""):0;
			if(numColumnas > 0)
			{
				existeParamColumnas = true;
			}
			//Traemos los servicios ya organizados y ordenados segun lo parametrizado
			mapaServicios = generarConsultaServicios(con, tipoFormato, usuario, codigoFactura);
			//Traemos los servicios ya organizados y ordenados segun lo parametrizado
			mapaArticulos = generarConsultaArticulos(con, tipoFormato, usuario, codigoFactura);
			//Cargamos los servicios qx de la factura
			serviciosQx = mundoFormato.consultarQx(con, codigoFactura);
			//Adicionamos los titulos de las columas que se parametrizaron
			int i = 0;
			if(existeParamColumnas)
			{
				for(i = 0 ; i < numColumnas ; i++ )
				{
					//Solo la "descripcion" que corresponde al titulo de la columna
					if((mapaParamSecPpal.get("descripcion_"+i)+"").trim().equals(""))
					{
						if(Integer.parseInt(mapaParamSecPpal.get("columna_"+i)+"") == ConstantesBD.codigoColumnaSecPpalConsecutivoRegistro)
						{
							existeParamConseReg = true;
							colsAdicionales++;
							v.add(i, "Consecutivo Registro");
						}
						if(Integer.parseInt(mapaParamSecPpal.get("columna_"+i)+"") == ConstantesBD.codigoColumnaSecPpalCodigoInterno)
						{
							existeParamCodInterno = true;
							colsAdicionales++;
							v.add(i, "Código Interno");
						}
						if(Integer.parseInt(mapaParamSecPpal.get("columna_"+i)+"") == ConstantesBD.codigoColumnaSecPpalCodigoManual)
						{
							v.add(i, "Código Manual");
						}
						if(Integer.parseInt(mapaParamSecPpal.get("columna_"+i)+"") == ConstantesBD.codigoColumnaSecPpalDescripcion)
						{
							v.add(i, "Descripción");
						}
						if(Integer.parseInt(mapaParamSecPpal.get("columna_"+i)+"") == ConstantesBD.codigoColumnaSecPpalCantidad)
						{
							v.add(i, "Cantidad");
						}
						if(Integer.parseInt(mapaParamSecPpal.get("columna_"+i)+"") == ConstantesBD.codigoColumnaSecPpalValorUnitario)
						{
							v.add(i, "Valor Unitario");
						}
						if(Integer.parseInt(mapaParamSecPpal.get("columna_"+i)+"") == ConstantesBD.codigoColumnaSecPpalValorTotal)
						{
							v.add(i, "Valor Total");
						}
					}
					else
					{
						if(Integer.parseInt(mapaParamSecPpal.get("columna_"+i)+"") == ConstantesBD.codigoColumnaSecPpalConsecutivoRegistro)
						{
							existeParamConseReg = true;
							colsAdicionales++;
						}
						if(Integer.parseInt(mapaParamSecPpal.get("columna_"+i)+"") == ConstantesBD.codigoColumnaSecPpalCodigoInterno)
						{
							existeParamCodInterno = true;
							colsAdicionales++;
						}
						v.add(i, mapaParamSecPpal.get("descripcion_"+i)+"");
					}
					
				}
				//Para identificar que filas deben ir resaltadas con negrilla
				posMapaNegrilla = Integer.parseInt(mapaRegistrosNegrilla.get("numRegistros")+"");
				mapaRegistrosNegrilla.put("filanegrilla_"+posMapaNegrilla, contadorFilas+"");
				posMapaNegrilla++;
				mapaRegistrosNegrilla.put("numRegistros", posMapaNegrilla);
				contadorFilas++;
				
			}
			else
			{
				//Solo la "descripcion" que corresponde al titulo de la columna
				v.add(i, "Descripcion");
				i++;
				v.add(i, "Cantidad");
				i++;
				v.add(i, "Valor Unitario");
				i++;
				v.add(i, "Valor Total");
				i++;
				numColumnas = 4;
				//Para identificar que filas deben ir resaltadas con negrilla
				posMapaNegrilla = Integer.parseInt(mapaRegistrosNegrilla.get("numRegistros")+"");
				mapaRegistrosNegrilla.put("filanegrilla_"+posMapaNegrilla, contadorFilas+"");
				posMapaNegrilla++;
				mapaRegistrosNegrilla.put("numRegistros", posMapaNegrilla);
				contadorFilas++;
			}

			//Adicionamos los servicios de manera organizada al vector
			int contRompimientoServ = Integer.parseInt(mapaServicios.get("contadorrompimiento")+"");
			String[] rompimientosServ = new String[contRompimientoServ];
			double subTotales[] = new double[contRompimientoServ];
			int posSubTotal = -1;
			String mostraDetalle = "";
			//logger.info("\n\ncontRompimientoServ->"+contRompimientoServ);
			//logger.info("\nEl mapa\n"+mapaServicios);
			//LLenamos el vector de servicios de manera adecuada para mandarlo a la impresion
			for(int j = 0 ; j < Integer.parseInt(mapaServicios.get("numRegistros")+"") ; j++)
			{
				for (int l = 1 ; l <= contRompimientoServ ; l++)
				{
					//logger.info("\n l=>"+l+"   j=>"+j+" el vector->"+rompimientosServ[l-1]+"<-  el mapa->"+mapaServicios.get("codigorompimiento_"+l+"_"+j)+"<-   servicio en j->"+mapaServicios.get("descripcionservicio_"+j));
					if(rompimientosServ[l-1]==null||!rompimientosServ[l-1].split(ConstantesBD.separadorSplit)[0].equals(mapaServicios.get("codigorompimiento_"+l+"_"+j)+""))
					{
						//logger.info("Entre aca con vector->"+rompimientosServ[l-1]+"<-  mapa->"+mapaServicios.get("codigorompimiento_"+l+"_"+j));
						if(rompimientosServ[l-1]!=null && !rompimientosServ[l-1].split(ConstantesBD.separadorSplit)[0].equals(mapaServicios.get("codigorompimiento_"+l+"_"+j)+""))
						{
							int recorrido = (contRompimientoServ-1) ;
							//logger.info("Recorrido->"+recorrido);
							//Ciclo para cerrar los niveles de rompimiento
							for(int a = recorrido ; a >= (l-1) ; a--)
							{
								//logger.info("\nEl impsubtot ->"+rompimientosServ[a].split(ConstantesBD.separadorSplit)[2]);
								if((rompimientosServ[a].split(ConstantesBD.separadorSplit)[2]+"").equals("true") || (rompimientosServ[a].split(ConstantesBD.separadorSplit)[2]+"").equals("t"))
								{
									if(existeParamConseReg)
									{
										v.add(i, "");
										i++;
									}
									if(existeParamCodInterno)
									{
										v.add(i, "");
										i++;
									}
								
									v.add(i,"Sub-total "+ rompimientosServ[a].split(ConstantesBD.separadorSplit)[1]);
									i++;
									consecutivoRegistro = 1;
									//Para identificar que filas deben ir resaltadas con negrilla
									posMapaNegrilla = Integer.parseInt(mapaRegistrosNegrilla.get("numRegistros")+"");
									mapaRegistrosNegrilla.put("filanegrilla_"+posMapaNegrilla, contadorFilas+"");
									posMapaNegrilla++;
									mapaRegistrosNegrilla.put("numRegistros", posMapaNegrilla);
									contadorFilas++;
									
									
									
									for(int g = 0 ; g < ((numColumnas-1)-colsAdicionales) ; g++)
									{
										if( (g+1) == ((numColumnas-1)-colsAdicionales) )
										{
											v.add(i, UtilidadTexto.formatearValores(subTotales[posSubTotal]+""));
											posSubTotal--;
										}
										else
										{
											v.add(i, "");
										}
										i++;
									}
								}
							}
							//Se vuleve los rompimientos null para empezar de nuevo desde
							//el nivel mas externo de rompimiento nuevo
							for(int y = (l-1) ; y < contRompimientoServ ; y++)
							{
								rompimientosServ[y] = null;
							}
						}
						rompimientosServ[l-1] = mapaServicios.get("codigorompimiento_"+l+"_"+j)+ConstantesBD.separadorSplit+mapaServicios.get("nombrerompimiento_"+l+"_"+j)+ConstantesBD.separadorSplit+mapaServicios.get("mostrarsubtotal_"+l+"_"+j)+ConstantesBD.separadorSplit+mapaServicios.get("mostrardetalle_"+l+"_"+j);
						mostraDetalle = mapaServicios.get("mostrardetalle_"+l+"_"+j)+"";
						
						if(existeParamConseReg)
						{
							v.add(i, "");
							i++;
						}
						if(existeParamCodInterno)
						{
							v.add(i, "");
							i++;
						}
						v.add(i, rompimientosServ[l-1].split(ConstantesBD.separadorSplit)[1]+"");
						i++;
						
						if((rompimientosServ[l-1].split(ConstantesBD.separadorSplit)[2]+"").equals("true") || (rompimientosServ[l-1].split(ConstantesBD.separadorSplit)[2]+"").equals("t"))
						{
							posSubTotal++;
							subTotales[posSubTotal] = 0;
						}
						
						for(int g = 0 ; g < ((numColumnas-1)-colsAdicionales) ; g++)
						{
							v.add(i, "");
							i++;
						}
						
						//Para identificar que filas deben ir resaltadas con negrilla
						posMapaNegrilla = Integer.parseInt(mapaRegistrosNegrilla.get("numRegistros")+"");
						mapaRegistrosNegrilla.put("filanegrilla_"+posMapaNegrilla, contadorFilas+"");
						posMapaNegrilla++;
						mapaRegistrosNegrilla.put("numRegistros", posMapaNegrilla);
						contadorFilas++;
						
					}
				}
				//Si se parametrizo mostrar detalle
				if(mostraDetalle.equals("true") || mostraDetalle.equals("t"))
				{
					//Llenamos las columnas del servicio
					if(existeParamColumnas)
					{
						for(int w = 0 ; w < numColumnas ; w++)
						{
							int codigoColumna = Integer.parseInt(mapaParamSecPpal.get("columna_"+w)+"");
							switch (codigoColumna)
							{
								
								case ConstantesBD.codigoColumnaSecPpalConsecutivoRegistro:
									v.add(i, consecutivoRegistro+"");
									i++;
								break;
								case ConstantesBD.codigoColumnaSecPpalCodigoInterno:
									v.add(i, mapaServicios.get("codservicio_"+j)+"".toLowerCase());
									i++;
								break;
								case ConstantesBD.codigoColumnaSecPpalCodigoManual:
									v.add(i, mapaServicios.get("codigomanual_"+j)+"".toLowerCase());
									i++;
								break;
								case ConstantesBD.codigoColumnaSecPpalDescripcion:
									v.add(i, ((String)(mapaServicios.get("descripcionservicio_"+j)+"")).toLowerCase());
									i++;
								break;
								case ConstantesBD.codigoColumnaSecPpalCantidad:
									v.add(i, mapaServicios.get("cantidad_"+j)+"");
									i++;
								break;
								case ConstantesBD.codigoColumnaSecPpalValorUnitario:
									v.add(i, UtilidadTexto.formatearValores(mapaServicios.get("valorunitario_"+j)+""));
									i++;
								break;
								case ConstantesBD.codigoColumnaSecPpalValorTotal:
									v.add(i, UtilidadTexto.formatearValores(mapaServicios.get("valortotal_"+j)+""));
									for(int conST = 0 ; conST <= posSubTotal ; conST++)
									{
										subTotales[conST] = subTotales[conST] + Double.parseDouble(mapaServicios.get("valortotal_"+j)+"");
									}
									i++;
								break;
							}
							
						}
						contadorFilas++;
						consecutivoRegistro++;
						
					}
					else
					{
						//Impresion por defecto si no se parametrizo nada para las columnas de titulos
						v.add(i, (mapaServicios.get("descripcionservicio_"+j)+"").toLowerCase());
						i++;
						v.add(i, mapaServicios.get("cantidad_"+j)+"".toLowerCase());
						i++;
						v.add(i, UtilidadTexto.formatearValores(mapaServicios.get("valorunitario_"+j)+""));
						i++;
						v.add(i, UtilidadTexto.formatearValores(mapaServicios.get("valortotal_"+j)+""));
						i++;
						for(int conST = 0 ; conST <= posSubTotal ; conST++)
						{
							subTotales[conST] = subTotales[conST] + Double.parseDouble(mapaServicios.get("valortotal_"+j)+"");
						}
						contadorFilas++;
					}
					
				}
			}
			//Cerramos los niveles de rompimientos faltantes 
			for(int a = (contRompimientoServ-1) ; a >= 0 ; a--)
			{
				//Se cierran si se tiene parametrizado como "TRUE" que se debe mostrar el Sub Total de cada rompimiento
				try
				{
					if(rompimientosServ[a]!=null)
					{
						String [] romSer=rompimientosServ[a].split(ConstantesBD.separadorSplit);
						if(UtilidadTexto.getBoolean(romSer[2]))
						{
							if(existeParamConseReg)
							{
								v.add(i, "");
								i++;
							}
							if(existeParamCodInterno)
							{
								v.add(i, "");
								i++;
							}
							v.add(i,"Sub-total "+ rompimientosServ[a].split(ConstantesBD.separadorSplit)[1]);
							i++;
							consecutivoRegistro = 1;
							//Para identificar que filas deben ir resaltadas con negrilla
							posMapaNegrilla = Integer.parseInt(mapaRegistrosNegrilla.get("numRegistros")+"");
							mapaRegistrosNegrilla.put("filanegrilla_"+posMapaNegrilla, contadorFilas+"");
							posMapaNegrilla++;
							mapaRegistrosNegrilla.put("numRegistros", posMapaNegrilla);
							contadorFilas++;
							
							for(int g = 0 ; g < ((numColumnas-1)-colsAdicionales) ; g++)
							{
								if( (g+1) == ((numColumnas-1)-colsAdicionales))
								{
									v.add(i, UtilidadTexto.formatearValores(subTotales[posSubTotal]+""));
									posSubTotal--;
								}
								else
								{
									v.add(i, "");
								}
								i++;
							}
						}
					}
				}
				catch (Exception e) 
				{
					e.printStackTrace();
				}
				
			}
			
			
			//Adicionamos los articulos de manera organizada al vector
			int contRompimientoArt = Integer.parseInt(mapaArticulos.get("contadorrompimiento")+"");
			String[] rompimientosArt = new String[contRompimientoArt];
			double subTotalesArt[] = new double[contRompimientoArt];
			int posSubTotalArt = -1;
			String mostrarDetArt = ""; 
			//logger.info("\n\n contRompimientoArt ->"+contRompimientoArt);
			//logger.info("\nEl mapa\n"+mapaArticulos);
			//LLenamos el vector de articulos de manera adecuada para mandarlo a la impresion
			for(int k = 0 ; k < Integer.parseInt(mapaArticulos.get("numRegistros")+"") ; k++)
			{
				for (int l = 1 ; l <= contRompimientoArt ; l++)
				{
					if(rompimientosArt[l-1]==null||!rompimientosArt[l-1].split(ConstantesBD.separadorSplit)[0].equals(mapaArticulos.get("codigorompimiento_"+l+"_"+k)+""))
					{
						
						if(rompimientosArt[l-1]!=null && !rompimientosArt[l-1].split(ConstantesBD.separadorSplit)[0].equals(mapaArticulos.get("codigorompimiento_"+l+"_"+k)+""))
						{
							int recorrido = (contRompimientoArt-1) ;
							//Ciclo para cerrar los niveles de rompimiento
							for(int a = recorrido ; a >= (l-1) ; a--)
							{
								//Se cierran si se tiene parametrizado como "TRUE" que se debe mostrar el Sub Total de cada rompimiento
								if((rompimientosArt[a].split(ConstantesBD.separadorSplit)[2]+"").equals("true") || (rompimientosArt[a].split(ConstantesBD.separadorSplit)[2]+"").equals("t"))
								{
									if(existeParamConseReg)
									{
										v.add(i, "");
										i++;
									}
									if(existeParamCodInterno)
									{
										v.add(i, "");
										i++;
									}
									
									v.add(i,"Sub-total "+ rompimientosArt[a].split(ConstantesBD.separadorSplit)[1]);
									i++;
									consecutivoRegistro = 1;
									//Para identificar que filas deben ir resaltadas con negrilla
									posMapaNegrilla = Integer.parseInt(mapaRegistrosNegrilla.get("numRegistros")+"");
									mapaRegistrosNegrilla.put("filanegrilla_"+posMapaNegrilla, contadorFilas+"");
									posMapaNegrilla++;
									mapaRegistrosNegrilla.put("numRegistros", posMapaNegrilla);
									contadorFilas++;
									
									for(int g = 0 ; g < ((numColumnas-1)-colsAdicionales) ; g++)
									{
										if( (g+1) == ((numColumnas-1)-colsAdicionales) )
										{
											v.add(i, UtilidadTexto.formatearValores(subTotalesArt[posSubTotalArt]+""));
											posSubTotalArt--;
										}
										else
										{
											v.add(i, "");
										}
										i++;
									}
								}
							}
							//Se vuleve los rompimientos null para empezar de nuevo desde
							//el nivel mas externo de rompimiento nuevo
							for(int y = (l-1) ; y < contRompimientoArt ; y++)
							{
								rompimientosArt[y] = null;
							}
						}
						
						rompimientosArt[l-1] = mapaArticulos.get("codigorompimiento_"+l+"_"+k)+ConstantesBD.separadorSplit+mapaArticulos.get("nombrerompimiento_"+l+"_"+k)+ConstantesBD.separadorSplit+mapaArticulos.get("mostrarsubtotal_"+l+"_"+k)+ConstantesBD.separadorSplit+mapaArticulos.get("mostrardetalle_"+l+"_"+k);
						mostrarDetArt = mapaArticulos.get("mostrardetalle_"+l+"_"+k)+"";
						
						if(existeParamConseReg)
						{
							v.add(i, "");
							i++;
						}
						if(existeParamCodInterno)
						{
							v.add(i, "");
							i++;
						}
						v.add(i, rompimientosArt[l-1].split(ConstantesBD.separadorSplit)[1]+"");
						i++;
						
						//Para identificar que filas deben ir resaltadas con negrilla
						posMapaNegrilla = Integer.parseInt(mapaRegistrosNegrilla.get("numRegistros")+"");
						mapaRegistrosNegrilla.put("filanegrilla_"+posMapaNegrilla, contadorFilas+"");
						posMapaNegrilla++;
						mapaRegistrosNegrilla.put("numRegistros", posMapaNegrilla);
						contadorFilas++;
						
						if((rompimientosArt[l-1].split(ConstantesBD.separadorSplit)[2]+"").equals("true") || (rompimientosArt[l-1].split(ConstantesBD.separadorSplit)[2]+"").equals("t"))
						{
							posSubTotalArt++;
							subTotalesArt[posSubTotalArt] = 0;
						}
						
						for(int g = 0 ; g < ((numColumnas-1)-colsAdicionales) ; g++)
						{
							v.add(i, "");
							i++;
						}
					}
				}
				
				consecutivoRegistro = 1;

				if(mostrarDetArt.equals("true") || mostrarDetArt.equals("t"))
				{
					//Llenamos las columnas del articulos
					if(existeParamColumnas)
					{
						for(int w = 0 ; w < numColumnas ; w++)
						{
							int codigoColumna = Integer.parseInt(mapaParamSecPpal.get("columna_"+w)+"");
							switch (codigoColumna)
							{
								case ConstantesBD.codigoColumnaSecPpalCodigoInterno:
									//Para los articulos no se muestra ningun codigo interno
									v.add(i, consecutivoRegistro+"");
									i++;
								break;
								case ConstantesBD.codigoColumnaSecPpalConsecutivoRegistro:
									v.add(i, "");
									i++;
								break;
								case ConstantesBD.codigoColumnaSecPpalCodigoManual:
									v.add(i, mapaArticulos.get("codarticulo_"+k)+"");
									i++;
								break;
								case ConstantesBD.codigoColumnaSecPpalDescripcion:
									v.add(i, mapaArticulos.get("descripcionarticulo_"+k)+"".toLowerCase());
									i++;
								break;
								case ConstantesBD.codigoColumnaSecPpalCantidad:
									v.add(i, mapaArticulos.get("cantidad_"+k)+"");
									i++;
								break;
								case ConstantesBD.codigoColumnaSecPpalValorUnitario:
									v.add(i, UtilidadTexto.formatearValores(mapaArticulos.get("valorunitario_"+k)+""));
									i++;
								break;
								case ConstantesBD.codigoColumnaSecPpalValorTotal:
									v.add(i, UtilidadTexto.formatearValores(mapaArticulos.get("valortotal_"+k)+""));
									i++;
									for(int conST = 0 ; conST <= posSubTotalArt ; conST++)
									{
										subTotalesArt[conST] = subTotalesArt[conST] + Double.parseDouble(mapaArticulos.get("valortotal_"+k)+"");
									}
								break;
							}
							
						}
						contadorFilas++;
						consecutivoRegistro++;
					}
					else
					{
						//Impresion por defecto si no se parametrizo nada
						v.add(i, mapaArticulos.get("descripcionsarticulo_"+k)+"".toLowerCase());
						i++;
						v.add(i, mapaArticulos.get("cantidad_"+k)+"");
						i++;
						v.add(i, UtilidadTexto.formatearValores(mapaArticulos.get("valorunitario_"+k)+""));
						i++;
						v.add(i, UtilidadTexto.formatearValores(mapaArticulos.get("valortotal_"+k)+""));
						i++;
						for(int conST = 0 ; conST <= posSubTotalArt ; conST++)
						{
							subTotalesArt[conST] = subTotalesArt[conST]+Double.parseDouble(mapaArticulos.get("valortotal_"+k)+"");
						}
						contadorFilas++;
					}
				}
			}
			
			for(int a = (contRompimientoArt-1) ; a >= 0 ; a--)
			{
				//Se cierran si se tiene parametrizado como "TRUE" que se debe mostrar el Sub Total de cada rompimiento
				if((rompimientosArt[a].split(ConstantesBD.separadorSplit)[2]+"").equals("true") || (rompimientosArt[a].split(ConstantesBD.separadorSplit)[2]+"").equals("t"))
				{
					if(existeParamConseReg)
					{
						v.add(i, "");
						i++;
					}
					if(existeParamCodInterno)
					{
						v.add(i, "");
						i++;
					}
					
					v.add(i,"Sub-total "+ rompimientosArt[a].split(ConstantesBD.separadorSplit)[1]);
					i++;
					consecutivoRegistro = 1;
					//Para identificar que filas deben ir resaltadas con negrilla
					posMapaNegrilla = Integer.parseInt(mapaRegistrosNegrilla.get("numRegistros")+"");
					mapaRegistrosNegrilla.put("filanegrilla_"+posMapaNegrilla, contadorFilas+"");
					posMapaNegrilla++;
					mapaRegistrosNegrilla.put("numRegistros", posMapaNegrilla);
					contadorFilas++;
					
					for(int g = 0 ; g < ((numColumnas-1)-colsAdicionales) ; g++)
					{
						if( (g+1) == ((numColumnas-1)-colsAdicionales) )
						{
							v.add(i, UtilidadTexto.formatearValores(subTotalesArt[posSubTotalArt]+""));
							posSubTotalArt--;
						}
						else
						{
							v.add(i, "");
						}
						i++;
					}
				}
			}
			
			//Si se parametizo que se deben mostrar los servicios Qx
			//logger.info("\n\n\n\nSe debn mostrar los servicios Qx->"+Utilidades.getMostrarServiciosQx(con, tipoFormato)+"<- \n\n\n");
			//logger.info("\n\nEl numero de cols->"+numColumnas+"<-   existeparametrizacion->"+existeParamColumnas+"<-\n\n");
			//logger.info("\n\nEl mapa:\n"+serviciosQx);
			
			if(Utilidades.getMostrarServiciosQx(con, tipoFormato))
			{
				for(int m = 0 ; m < Integer.parseInt(serviciosQx.get("numRegistros")+"") ; m++)
				{
					if(existeParamColumnas)
					{
						for(int y = 0 ; y < numColumnas ; y++)
						{
							int codigoColumna = Integer.parseInt(mapaParamSecPpal.get("columna_"+y)+"");
							switch (codigoColumna)
							{
								case ConstantesBD.codigoColumnaSecPpalConsecutivoRegistro:
									//Para los servicos qx no se muestra ningun consecutivo
									v.add(i, "");
									i++;
								break;
								case ConstantesBD.codigoColumnaSecPpalCodigoInterno:
									//Para los servicos qx no se muestra ningun codigo interno
									v.add(i, "");
									i++;
								break;
								case ConstantesBD.codigoColumnaSecPpalCodigoManual:
									v.add(i, serviciosQx.get("codigoservicio_"+m)+"");
									i++;
								break;
								case ConstantesBD.codigoColumnaSecPpalDescripcion:
									v.add(i, (serviciosQx.get("nombreservicio_"+m)+"").toUpperCase());
									i++;
								break;
								case ConstantesBD.codigoColumnaSecPpalCantidad:
									v.add(i, "");
									i++;
								break;
								case ConstantesBD.codigoColumnaSecPpalValorUnitario:
									v.add(i, "");
									i++;
								break;
								case ConstantesBD.codigoColumnaSecPpalValorTotal:
									v.add(i, UtilidadTexto.formatearValores(serviciosQx.get("valorsolicitud_"+m)+""));
									i++;
								break;
							}
						}
						//Para identificar que filas deben ir resaltadas con negrilla
						posMapaNegrilla = Integer.parseInt(mapaRegistrosNegrilla.get("numRegistros")+"");
						mapaRegistrosNegrilla.put("filanegrilla_"+posMapaNegrilla, contadorFilas+"");
						posMapaNegrilla++;
						mapaRegistrosNegrilla.put("numRegistros", posMapaNegrilla);
						contadorFilas++;
						consecutivoRegistro = 1;
						
						HashMap detalleAsocios = (HashMap)((HashMap)serviciosQx.get("detalleasocio_"+m)).clone();
						for( int a = 0 ; a < Integer.parseInt(detalleAsocios.get("numRegistros")+"") ; a++)
						{
							
							for(int y = 0 ; y < numColumnas ; y++)
							{
								int codigoColumna = Integer.parseInt(mapaParamSecPpal.get("columna_"+y)+"");
								
								switch (codigoColumna)
								{
									case ConstantesBD.codigoColumnaSecPpalConsecutivoRegistro:
										//Para los servicos qx no se muestra ningun consecutivo
										v.add(i, consecutivoRegistro+"");
										i++;
									break;
									case ConstantesBD.codigoColumnaSecPpalCodigoInterno:
										//Para los servicos qx no se muestra ningun codigo interno
										v.add(i, "");
										i++;
									break;
									case ConstantesBD.codigoColumnaSecPpalCodigoManual:
										v.add(i, detalleAsocios.get("codigoasocioservicio_"+a)+"");
										i++;
									break;
									case ConstantesBD.codigoColumnaSecPpalDescripcion:
										v.add(i, (detalleAsocios.get("nombreasocio_"+a)+"").toLowerCase());
										i++;
									break;
									case ConstantesBD.codigoColumnaSecPpalCantidad:
										v.add(i, "");
										i++;
									break;
									case ConstantesBD.codigoColumnaSecPpalValorUnitario:
										v.add(i, "");
										i++;
									break;
									case ConstantesBD.codigoColumnaSecPpalValorTotal:
										v.add(i, UtilidadTexto.formatearValores(detalleAsocios.get("valorasocio_"+a)+""));
										i++;
									break;
								}
								
							}
							contadorFilas++;
							consecutivoRegistro++;
						}

					}
					//Si no exiten columnas parametrizadas ponemos las por defecto
					else
					{
						v.add(i, serviciosQx.get("codigoservicio__"+m) + " " + serviciosQx.get("nombreservicio_"+m)+"".toUpperCase());
						i++;
						v.add(i, "");
						i++;
						v.add(i, "");
						i++;
						v.add(i, UtilidadTexto.formatearValores(serviciosQx.get("valorsolicitud_"+m)+""));
						i++;
						
						//Para identificar que filas deben ir resaltadas con negrilla
						posMapaNegrilla = Integer.parseInt(mapaRegistrosNegrilla.get("numRegistros")+"");
						mapaRegistrosNegrilla.put("filanegrilla_"+posMapaNegrilla, contadorFilas+"");
						posMapaNegrilla++;
						mapaRegistrosNegrilla.put("numRegistros", posMapaNegrilla);
						contadorFilas++;
						
						
						//Debemos poner el detalle de los asocios
						HashMap detalleAsocios = (HashMap)((HashMap)serviciosQx.get("detalleasocio_"+m)).clone();
						for( int a = 0 ; a < Integer.parseInt(detalleAsocios.get("numRegistros")+"") ; a++)
						{
							v.add(i, detalleAsocios.get("codigoasocioservicio_"+a) + " " + (((String)(detalleAsocios.get("nombreasocio_"+a)+"")).toLowerCase()));
							i++;
							v.add(i, "");
							i++;
							v.add(i, "");
							i++;
							v.add(i, UtilidadTexto.formatearValores(detalleAsocios.get("valorasocio_"+a)+""));
							i++;
							
							contadorFilas++;
						}
					}
				}
			}
			//Si no se deben mostrar los servicos Qx solo mostramos la solicitud de cirugia 
			//sin el detalle de los asocios
			else
			{
				for(int m = 0 ; m < Integer.parseInt(serviciosQx.get("numRegistros")+"") ; m++)
				{
					if(existeParamColumnas)
					{
						for(int y = 0 ; y < numColumnas ; y++)
						{
							int codigoColumna = Integer.parseInt(mapaParamSecPpal.get("columna_"+y)+"");
							switch (codigoColumna)
							{
								case ConstantesBD.codigoColumnaSecPpalConsecutivoRegistro:
									//Para los servicos qx no se muestra ningun consecutivo
									v.add(i, "");
									i++;
									
									
								break;
								case ConstantesBD.codigoColumnaSecPpalCodigoInterno:
									//Para los servicos qx no se muestra ningun codigo interno
									v.add(i, "");
									i++;
								break;
								case ConstantesBD.codigoColumnaSecPpalCodigoManual:
									v.add(i, serviciosQx.get("codigoservicio_"+m)+"");
									i++;
								break;
								case ConstantesBD.codigoColumnaSecPpalDescripcion:
									v.add(i, serviciosQx.get("nombreservicio_"+m)+"".toUpperCase());
									i++;
								break;
								case ConstantesBD.codigoColumnaSecPpalCantidad:
									v.add(i, "");
									i++;
								break;
								case ConstantesBD.codigoColumnaSecPpalValorUnitario:
									v.add(i, "");
									i++;
								break;
								case ConstantesBD.codigoColumnaSecPpalValorTotal:
									v.add(i, UtilidadTexto.formatearValores(serviciosQx.get("valorsolicitud_"+m)+""));
									i++;
								break;
							}
						}
						//Para identificar que filas deben ir resaltadas con negrilla
						posMapaNegrilla = Integer.parseInt(mapaRegistrosNegrilla.get("numRegistros")+"");
						mapaRegistrosNegrilla.put("filanegrilla_"+posMapaNegrilla, contadorFilas+"");
						posMapaNegrilla++;
						mapaRegistrosNegrilla.put("numRegistros", posMapaNegrilla);
						contadorFilas++;
					}
					//Si no exiten columnas parametrizadas ponemos las por defecto
					else
					{
						v.add(i, serviciosQx.get("codigoservicio_"+m) + " " + serviciosQx.get("nombreservicio_"+m)+"".toUpperCase());
						i++;
						v.add(i, "");
						i++;
						v.add(i, "");
						i++;
						v.add(i, UtilidadTexto.formatearValores(serviciosQx.get("valorsolicitud_"+m)+""));
						i++;
						
						//Para identificar que filas deben ir resaltadas con negrilla
						posMapaNegrilla = Integer.parseInt(mapaRegistrosNegrilla.get("numRegistros")+"");
						mapaRegistrosNegrilla.put("filanegrilla_"+posMapaNegrilla, contadorFilas+"");
						posMapaNegrilla++;
						mapaRegistrosNegrilla.put("numRegistros", posMapaNegrilla);
						contadorFilas++;
					}
				}
			}
			
			
			
			//logger.info("\n\n\nEl cuerpo:  numCols->"+numColumnas+"<-  numeroFilas->"+numeroFilas+"<-   contadorFilas->"+contadorFilas+"<-\n\n");
			//logger.info("\n\nEL mapa->"+mapaRegistrosNegrilla);
			//for(int cont = 0 ; cont < Integer.parseInt(mapaRegistrosNegrilla.get("numRegistros")+"") ; cont++)
			//	logger.info("El mapa de filas ->"+mapaRegistrosNegrilla.get("filanegrilla_"+cont));
			//for(int g = 0 ; g < v.size() ; g++)
			//	logger.info("El vector en la posicion["+g+"] el valor->"+v.get(g));
			
			
			
			//El tamaño del vector dividido el numero de columnas es igual a el numero de filas
			numeroFilas = v.size()/numColumnas;
			//Insertamos una grilla en la posicion 1,0 de la grilla de la plantilla diseñada
			//para la impresion de la factura
			comp.insertGridInBodyPageWithName(1, 0, numColumnas, numeroFilas, "tablaPrincipal");
			//Insertar el vector ya con los label's de los datos a Imprimir
			comp.insertLabelInGridOfBodyPage(1, 0, v);
			
			//Le ponemos negrilla a las filas que lo requieren
			for(int t = 0 ; t < Integer.parseInt(mapaRegistrosNegrilla.get("numRegistros")+"") ; t++)
			{
				comp.fontWeightRowGridBodyPage(1, 0 , Integer.parseInt(mapaRegistrosNegrilla.get("filanegrilla_"+t)+""), DesignChoiceConstants.FONT_WEIGHT_BOLD );
			}
			if(existeParamConseReg)
			{
				//Alineamos la columna del Consecutivo Registro solo en caso de que se haya parametrizado
				comp.colAlingmentGridBodyPage(1, 0, 0, DesignChoiceConstants.COLUMN_ALIGN_CENTER);
			}
			if(existeParamCodInterno)
			{
				//Alineamos la columna del Codigo Interno solo en caso de que se haya parametrizado
				comp.colAlingmentGridBodyPage(1, 0, 1, DesignChoiceConstants.COLUMN_ALIGN_CENTER);
			}
			//Alineamos la columna de Valor Total que siempre estara de ultima
			comp.colAlingmentGridBodyPage(1, 0, numColumnas-1, DesignChoiceConstants.COLUMN_ALIGN_RIGHT);
			//Alineamos la columna de Valor Unitario que siempre estara de pen-ultima
			comp.colAlingmentGridBodyPage(1, 0, numColumnas-2, DesignChoiceConstants.COLUMN_ALIGN_RIGHT);
			//Alineamos la columna de Cantidad que siempre estara de ante-pen-ultima
			comp.colAlingmentGridBodyPage(1, 0, numColumnas-3, DesignChoiceConstants.COLUMN_ALIGN_CENTER);
		}
		catch (SQLException e) 
		{
			logger.error("Error en editarDatosCuerpoImpresion [FacutracionPdf] "+e);
		}
		
	}
	
	
	/**
	 * Metodo para cargar la consulta de la seccion de Servicios segun lo que se haya parametrizado
	 * en el Formato de Impresion Factura en la seccion que le corresponde
	 * @param con
	 * @param tipoFormato
	 * @param usuario
	 * @param codigoFactura
	 * @return
	 */
	private static HashMap generarConsultaServicios(Connection con, int tipoFormato, UsuarioBasico usuario, int codigoFactura)
	{
		FormatoImpresionFactura mundoFormato = new FormatoImpresionFactura();
		HashMap mapaParamSecPpal = new HashMap();
		HashMap mapaParamServicios = new HashMap();
		HashMap mapaParamDetServicios = new HashMap();
		HashMap mapaParamFormatoBasico = new HashMap();
		
		HashMap mapaCodRompimientos =  new HashMap();
		mapaCodRompimientos.put("numRegistros", "0");
		
		boolean tieneCCSolicita = false;
		boolean tieneCCEjecuta = false;
		String consultaServiciosSelect = " 1 as comodin ";
		int contadorRompimientos = 0;
		
		
		String consultaServiciosFROM = " FROM det_factura_solicitud as dfs" +
									   " INNER JOIN solicitudes s ON(dfs.solicitud=s.numero_solicitud) "+
									   " INNER JOIN servicios ser ON(dfs.servicio=ser.codigo) "+
									   " INNER JOIN referencias_servicio rs ON(dfs.servicio=rs.servicio and rs.tipo_tarifario = "+ConstantesBD.codigoTarifarioCups+") ";
		
		String consultaServiciosWhere = " WHERE dfs.factura= "+codigoFactura+" AND s.tipo<>"+ConstantesBD.codigoTipoSolicitudCirugia+" ";
		
		String consultaServiciosOrderBy = "";
		
		try
		{
			//Cargamos los datos parametrizados de la seccion Principal del formato de impresion de factura
			mapaParamSecPpal = mundoFormato.consultarDatosSecPpalParametrizados(con, tipoFormato);
			//Cargamos el mapa con la informacion parametrizada en la seccion de servicios
			mapaParamServicios = mundoFormato.consultarSecServicios(con, tipoFormato);
			//Cargamos el detalle de lo que se parametrizo en la seccion de servicios de el Formato de Impresion de Factura
			mapaParamDetServicios = (HashMap)((HashMap)mapaParamServicios.get("detallenivel")).clone();
			//Cargamos los datos bascios del formato de impresion de facturas parametrizados
			mapaParamFormatoBasico = mundoFormato.consultarFormatoFacturaBasico(con, usuario.getCodigoInstitucionInt(), tipoFormato);
			
			
			
			int codigoTipoRompimiento = 0;
			//Evaluamos los tipos de rompimiento parametrizados en el Formato de Impresion de Factura
			//y segun eso armamos la consulta dinámica respectiva
			//logger.info("\n\n\n"+mapaParamDetServicios+"\n\n");impdetalleserv_
			for (int j = 0 ; j < Integer.parseInt(mapaParamDetServicios.get("numRegistros")+"") ; j++)
			{
				if(mapaParamDetServicios.get("codtiporompimiento_"+j)!=null)
				{
					codigoTipoRompimiento = Integer.parseInt(mapaParamDetServicios.get("codtiporompimiento_"+j)+"");
					//Se verfifica que tipo de rompimiento se parametrizo
					switch(codigoTipoRompimiento)
					{
						case ConstantesBD.codigoRompimientoServTipoServicios:
							mapaCodRompimientos.put("rompimiento_"+contadorRompimientos, ConstantesBD.codigoRompimientoServTipoServicios+"");
							contadorRompimientos++;
							mapaCodRompimientos.put("numRegistros",contadorRompimientos);
							if(!consultaServiciosSelect.equals(""))
							{
								consultaServiciosSelect+=", ";
							}
							consultaServiciosSelect+= "ts.acronimo as codigorompimiento_"+contadorRompimientos+", ts.descripcion as nombrerompimiento_"+contadorRompimientos+", "+mapaParamDetServicios.get("impsubtotserv_"+j)+" as mostrarsubtotal_"+contadorRompimientos+", "+mapaParamDetServicios.get("impdetalleserv_"+j)+" as mostrardetalle_"+contadorRompimientos+" ";
							consultaServiciosFROM+= " INNER JOIN tipos_servicio ts ON(ser.tipo_servicio=ts.acronimo) ";
							if(!consultaServiciosOrderBy.equals(""))
							{
								consultaServiciosOrderBy+=", ";
							}
							consultaServiciosOrderBy+= " ts.acronimo ";
						break;
						case ConstantesBD.codigoRompimientoServGrupoServicios:
							mapaCodRompimientos.put("rompimiento_"+contadorRompimientos, ConstantesBD.codigoRompimientoServGrupoServicios+"");
							contadorRompimientos++;
							mapaCodRompimientos.put("numRegistros",contadorRompimientos);
							if(!consultaServiciosSelect.equals(""))
							{
								consultaServiciosSelect+=", ";
							}
							consultaServiciosSelect+= " gs.acronimo as codigorompimiento_"+contadorRompimientos+", gs.descripcion as nombrerompimiento_"+contadorRompimientos+", "+mapaParamDetServicios.get("impsubtotserv_"+j)+" as mostrarsubtotal_"+contadorRompimientos+", "+mapaParamDetServicios.get("impdetalleserv_"+j)+" as mostrardetalle_"+contadorRompimientos+" "; 
							consultaServiciosFROM+= " INNER JOIN grupos_servicios gs ON(ser.grupo_servicio=gs.codigo) ";
							if(!consultaServiciosOrderBy.equals(""))
							{
								consultaServiciosOrderBy+=", ";
							}
							consultaServiciosOrderBy+= " gs.acronimo ";
						break;
						case ConstantesBD.codigoRompimientoServEspecialidadServicios:
							mapaCodRompimientos.put("rompimiento_"+contadorRompimientos, ConstantesBD.codigoRompimientoServEspecialidadServicios+"");
							contadorRompimientos++;
							mapaCodRompimientos.put("numRegistros",contadorRompimientos);
							if(!consultaServiciosSelect.equals(""))
							{
								consultaServiciosSelect+=", ";
							}
							consultaServiciosSelect+=" e.codigo as codigorompimiento_"+contadorRompimientos+", e.nombre as nombrerompimiento_"+contadorRompimientos+", "+mapaParamDetServicios.get("impsubtotserv_"+j)+" as mostrarsubtotal_"+contadorRompimientos+", "+mapaParamDetServicios.get("impdetalleserv_"+j)+" as mostrardetalle_"+contadorRompimientos+" ";
							consultaServiciosFROM+=" INNER JOIN especialidades e ON(ser.especialidad=e.codigo) ";
							if(!consultaServiciosOrderBy.equals(""))
							{
								consultaServiciosOrderBy+=", ";
							}
							consultaServiciosOrderBy+=" e.codigo ";
						break;
						case ConstantesBD.codigoRompimientoServCCSolicitaServicios:
							mapaCodRompimientos.put("rompimiento_"+contadorRompimientos, ConstantesBD.codigoRompimientoServCCSolicitaServicios+"");
							contadorRompimientos++;
							mapaCodRompimientos.put("numRegistros",contadorRompimientos);
							if(!consultaServiciosSelect.equals(""))
							{
								consultaServiciosSelect+=", ";
							}
							consultaServiciosSelect+=" s.centro_costo_solicitante as codigorompimiento_"+contadorRompimientos+", cc.nombre as nombrerompimiento_"+contadorRompimientos+", "+mapaParamDetServicios.get("impsubtotserv_"+j)+" as mostrarsubtotal_"+contadorRompimientos+", "+mapaParamDetServicios.get("impdetalleserv_"+j)+" as mostrardetalle_"+contadorRompimientos+" ";
							consultaServiciosFROM+=" INNER JOIN centros_costo cc ON(s.centro_costo_solicitante=cc.codigo) ";
							if(!consultaServiciosOrderBy.equals(""))
							{
								consultaServiciosOrderBy+=", ";
							}
							consultaServiciosOrderBy+=" s.centro_costo_solicitante ";
							tieneCCSolicita = true;
						break;
						case ConstantesBD.codigoRompimientoServCCEjecutaServicios:
							mapaCodRompimientos.put("rompimiento_"+contadorRompimientos, ConstantesBD.codigoRompimientoServCCEjecutaServicios+"");
							contadorRompimientos++;
							mapaCodRompimientos.put("numRegistros",contadorRompimientos);
							if(!consultaServiciosSelect.equals(""))
							{
								consultaServiciosSelect+=", ";
							}
							consultaServiciosSelect+=" s.centro_costo_solicitado as codigorompimiento_"+contadorRompimientos+", cc1.nombre  as nombrerompimiento_"+contadorRompimientos+", "+mapaParamDetServicios.get("impsubtotserv_"+j)+" as mostrarsubtotal_"+contadorRompimientos+", "+mapaParamDetServicios.get("impdetalleserv_"+j)+" as mostrardetalle_"+contadorRompimientos+" ";
							consultaServiciosFROM+=" INNER JOIN centros_costo cc1 ON(s.centro_costo_solicitado=cc1.codigo) ";
							if(!consultaServiciosOrderBy.equals(""))
							{
								consultaServiciosOrderBy+=", ";
							}
							consultaServiciosOrderBy+=" s.centro_costo_solicitado ";
							tieneCCEjecuta = true;
						break;
					}
				}
			}
			
			
			int codigoCampo=0;
			
			//Cargamos los datos de los servicios segun las columnas que se deben mostrar parametrizadas
			//para el formato de impresion de factura
			for(int i = 0 ; i < Integer.parseInt(mapaParamSecPpal.get("numRegistros")+"") ; i++)
			{
				codigoCampo = Integer.parseInt(mapaParamSecPpal.get("columna_"+i)+"");
				//Se verifica que tipo de campo es:
				switch(codigoCampo)
				{
					case ConstantesBD.codigoColumnaSecPpalCodigoInterno:
						if(!consultaServiciosSelect.equals(""))
						{
							consultaServiciosSelect+=", ";
						}
						consultaServiciosSelect+=" (ser.especialidad||'-'||ser.codigo) as codaxiomaservicio";
					break;
					case ConstantesBD.codigoColumnaSecPpalCodigoManual:
						if((mapaParamFormatoBasico.get("codigoCodServ")+"").equals(ConstantesBD.codigoTarifarioCups))
						{
							consultaServiciosWhere+=" AND rs.codigo_propietario="+ConstantesBD.codigoTarifarioCups;
						}
						if((mapaParamFormatoBasico.get("codigoCodServ")+"").equals(ConstantesBD.codigoTarifarioISS))
						{
							consultaServiciosWhere+=" AND rs.codigo_propietario="+ConstantesBD.codigoTarifarioISS;
						}
						if((mapaParamFormatoBasico.get("codigoCodServ")+"").equals(ConstantesBD.codigoTarifarioSoat))
						{
							consultaServiciosWhere+=" AND rs.codigo_propietario="+ConstantesBD.codigoTarifarioSoat;
						}
					break;
					
				}
			}
			
			if(!consultaServiciosSelect.equals(""))
			{
				consultaServiciosSelect+=", ";
			}
			
			consultaServiciosSelect+=" rs.servicio as codservicio, " +
									 " rs.codigo_propietario as codigomanual, " +
									 " rs.descripcion as descripcionservicio, " +
									 " dfs.cantidad_cargo as cantidad, "+
									 " dfs.valor_total as valortotal, " +
									 " getValorTotalServicioXSolitud(dfs.servicio, dfs.factura,"+(tieneCCSolicita?"s.centro_costo_solicitante":"0")+", "+(tieneCCEjecuta?"s.centro_costo_solicitado":"0")+") as valorunitario";
			
			//logger.info("\n\nLas consultaFinal de servicios:\n"+" SELECT "+consultaServiciosSelect + consultaServiciosFROM + consultaServiciosWhere + " ORDER BY " + consultaServiciosOrderBy+"\n\n");
			HashMap resultadoConsulta = new HashMap();
			resultadoConsulta = mundoFormato.consultaCuerpoImpresionFacturaServicios(con, "SELECT "+consultaServiciosSelect + consultaServiciosFROM + consultaServiciosWhere + (consultaServiciosOrderBy.trim().equals("")?" ":" ORDER BY "+consultaServiciosOrderBy));
			resultadoConsulta.put("contadorrompimiento", contadorRompimientos);
			
			return resultadoConsulta;
		}
		catch (SQLException e) 
		{
			logger.error("Error en generarConsultaServicios [FacutracionPdf] "+e);
		}
		catch (Exception e) 
		{
			logger.error("ERROR EN LA IMPRESION DE LA FACTURA");
			e.printStackTrace();
		}
		return null;
	}

	
	/**
	 * Metodo para cargar la consulta de la seccion de Articulos segun lo que se haya parametrizado
	 * en el Formato de Impresion Factura en la seccion que le corresponde
	 * @param con
	 * @param tipoFormato
	 * @param usuario
	 * @param codigoFactura
	 * @return
	 */
	private static HashMap generarConsultaArticulos(Connection con, int tipoFormato, UsuarioBasico usuario, int codigoFactura)
	{
		FormatoImpresionFactura mundoFormato = new FormatoImpresionFactura();
		HashMap mapaParamSecPpal = new HashMap();
		HashMap mapaParamArticulos = new HashMap();
		HashMap mapaParamDetArticulo = new HashMap();
		HashMap mapaParamFormatoBasico = new HashMap();
		
		HashMap mapaCodRompimientos =  new HashMap();
		mapaCodRompimientos.put("numRegistros", "0");
		
		int contadorRompimientos = 0;
		boolean tieneCCSolicita = false;
		boolean tieneCCEjecuta = false;
		
		
		String consultaArticulosSelect=" 1 as comodin ";
		
		String consultaArticulosFROM=" FROM det_factura_solicitud as dfs" +
									 " INNER JOIN solicitudes s ON(dfs.solicitud=s.numero_solicitud) "+
									 " INNER JOIN articulo a ON(dfs.articulo=a.codigo) " ;
		
		String consultaArticulosWhere=" WHERE  dfs.factura= "+codigoFactura+" ";
		
		String consultaArticulosOrderBy="";
		
		try
		{
			//Cargamos los datos parametrizados de la seccion Principal del formato de impresion de factura
			mapaParamSecPpal = mundoFormato.consultarDatosSecPpalParametrizados(con, tipoFormato);
			//Cargamos el mapa con la informacion parametrizada en la seccion de articulos
			mapaParamArticulos = mundoFormato.consultarSecArticulos(con, tipoFormato);
			//Cargamos el detalle de lo que se parametrizo en la seccion de articulos de el Formato de Impresion de Factura
			mapaParamDetArticulo = (HashMap)((HashMap)mapaParamArticulos.get("detallenivel")).clone();
			//Cargamos los datos bascios del formato de impresion de facturas parametrizados
			mapaParamFormatoBasico = mundoFormato.consultarFormatoFacturaBasico(con, usuario.getCodigoInstitucionInt(), tipoFormato);
			
			int codigoCampo=0;
			
			//Cargamos los datos de los servicios segun las columnas que se deben mostrar parametrizadas
			//para el formato de impresion de factura para la seccion de servicios
			/*
			 * Al parecer solo aplica para servicios.
			for(int i = 0 ; i < Integer.parseInt(mapaParamSecPpal.get("numRegistros")+"") ; i++)
			{
				if(mapaParamSecPpal.get("columna_"+i)!=null && !(mapaParamSecPpal.get("columna_"+i)+"").equals("null") && !(mapaParamSecPpal.get("columna_"+i)+"").equals(""))
				{
					codigoCampo = Integer.parseInt(mapaParamSecPpal.get("columna_"+i)+"");
					//Se verifica que tipo de campo es:
					switch(codigoCampo)
					{
						case ConstantesBD.codigoColumnaSecPpalCodigoInterno:
							if(!consultaArticulosSelect.equals(""))
							{
								consultaArticulosSelect+=", ";
							}
							consultaArticulosSelect+=", (s.especialidad||'-'||s.codigo) as codaxiomaservicio";
						break;
						case ConstantesBD.codigoColumnaSecPpalCodigoManual:
							if((mapaParamFormatoBasico.get("codigoCodServ")+"").equals(null))
							{
								if(!consultaArticulosSelect.equals(""))
								{
									consultaArticulosSelect+=", ";
								}
								consultaArticulosSelect+=" a.minsalud as minsalud ";
							}
							else
							{
								if(!consultaArticulosSelect.equals(""))
								{
									consultaArticulosSelect+=", ";
								}
								consultaArticulosSelect+=" '' as minsalud ";
							}
						break;
						
					}
				}
			}
			*/
			int codigoTipoRompimiento = 0;
			boolean existeRelacion = false;
			//Evaluamos los tipos de rompimiento parametrizados en el Formato de Impresion de Factura
			//y segun eso armamos la consulta dinámica respectiva para los articulos
			//logger.info("\n\n\n"+mapaParamDetArticulo+"\n\n");impdetalleart_0
			for (int j = 0 ; j < Integer.parseInt(mapaParamDetArticulo.get("numRegistros")+"") ; j++)
			{
				if(mapaParamDetArticulo.get("codtiporompimiento_"+j)!=null && !(mapaParamDetArticulo.get("codtiporompimiento_"+j)+"").equals("null") && !(mapaParamDetArticulo.get("codtiporompimiento_"+j)+"").equals(""))
				{
					codigoTipoRompimiento = Integer.parseInt(mapaParamDetArticulo.get("codtiporompimiento_"+j)+"");
					//Se verfifica que tipo de rompimiento que se parametrizo
					switch(codigoTipoRompimiento)
					{
						case ConstantesBD.codigoRompimientoArtSubAlmacen:
							mapaCodRompimientos.put("rompimiento_"+contadorRompimientos, ConstantesBD.codigoRompimientoArtSubAlmacen+"");
							contadorRompimientos++;
							mapaCodRompimientos.put("numRegistros",contadorRompimientos);
							if(!consultaArticulosSelect.equals(""))
							{
								consultaArticulosSelect+=", ";
							}
							consultaArticulosSelect+=" s.centro_costo_solicitado as codigorompimiento_"+contadorRompimientos+", cc.nombre as nombrerompimiento_"+contadorRompimientos+", "+mapaParamDetArticulo.get("impsubtotart_"+j)+" as mostrarsubtotal_"+contadorRompimientos+", "+mapaParamDetArticulo.get("impdetalleart_"+j)+" as mostrardetalle_"+contadorRompimientos+" ";
							consultaArticulosFROM+=" INNER JOIN centros_costo cc ON(s.centro_costo_solicitado=cc.codigo) ";
							if(!consultaArticulosOrderBy.equals(""))
							{
								consultaArticulosOrderBy+=", ";
							}
							consultaArticulosOrderBy+=" s.centro_costo_solicitado ";
						break;
						case ConstantesBD.codigoRompimientoArtCCSolicita:
							mapaCodRompimientos.put("rompimiento_"+contadorRompimientos, ConstantesBD.codigoRompimientoArtCCSolicita+"");
							contadorRompimientos++;
							mapaCodRompimientos.put("numRegistros",contadorRompimientos);
							if(!consultaArticulosSelect.equals(""))
							{
								consultaArticulosSelect+=", ";
							}
							consultaArticulosSelect+=" s.centro_costo_solicitante as codigorompimiento_"+contadorRompimientos+", cc1.nombre as nombrerompimiento_"+contadorRompimientos+", "+mapaParamDetArticulo.get("impsubtotart_"+j)+" as mostrarsubtotal_"+contadorRompimientos+", "+mapaParamDetArticulo.get("impdetalleart_"+j)+" as mostrardetalle_"+contadorRompimientos+" ";
							consultaArticulosFROM+=" INNER JOIN centros_costo cc1 ON(s.centro_costo_solicitante=cc1.codigo) ";
							if(!consultaArticulosOrderBy.equals(""))
							{
								consultaArticulosOrderBy+=", ";
							}
							consultaArticulosOrderBy+=" s.centro_costo_solicitante ";
						break;
						case ConstantesBD.codigoRompimientoArtClaseInventario:
							mapaCodRompimientos.put("rompimiento_"+contadorRompimientos, ConstantesBD.codigoRompimientoArtClaseInventario+"");
							contadorRompimientos++;
							mapaCodRompimientos.put("numRegistros",contadorRompimientos);
							if(!consultaArticulosSelect.equals(""))
							{
								consultaArticulosSelect+=", ";
							}
							consultaArticulosSelect+=" ci.codigo as codigorompimiento_"+contadorRompimientos+", ci.nombre as nombrerompimiento_"+contadorRompimientos+", "+mapaParamDetArticulo.get("impsubtotart_"+j)+" as mostrarsubtotal_"+contadorRompimientos+", "+mapaParamDetArticulo.get("impdetalleart_"+j)+" as mostrardetalle_"+contadorRompimientos+" ";
							if(!existeRelacion)
							{
								consultaArticulosFROM+=" INNER JOIN subgrupo_inventario sgi ON(a.subgrupo=sgi.codigo) " +
													   " INNER JOIN clase_inventario ci ON(sgi.clase=ci.codigo) ";
								existeRelacion = true;
							}
							else
							{
								consultaArticulosFROM+=" INNER JOIN clase_inventario ci ON(sgi.clase=ci.codigo) ";
							}
							if(!consultaArticulosOrderBy.equals(""))
							{
								consultaArticulosOrderBy+=", ";
							}
							consultaArticulosOrderBy+="ci.codigo ";
						break;
						case ConstantesBD.codigoRompimientoArtGrupo:
							mapaCodRompimientos.put("rompimiento_"+contadorRompimientos, ConstantesBD.codigoRompimientoArtGrupo+"");
							contadorRompimientos++;
							mapaCodRompimientos.put("numRegistros",contadorRompimientos);
							if(!consultaArticulosSelect.equals(""))
							{
								consultaArticulosSelect+=", ";
							}
							consultaArticulosSelect+=" gi.codigo as codigorompimiento_"+contadorRompimientos+", gi.nombre as nombrerompimiento_"+contadorRompimientos+", "+mapaParamDetArticulo.get("impsubtotart_"+j)+" as mostrarsubtotal_"+contadorRompimientos+", "+mapaParamDetArticulo.get("impdetalleart_"+j)+" as mostrardetalle_"+contadorRompimientos+" ";
							if(!existeRelacion)
							{
								consultaArticulosFROM+=" INNER JOIN subgrupo_inventario sgi ON(a.subgrupo=sgi.codigo) " +
												   	   " INNER JOIN grupo_inventario gi ON(sgi.grupo=gi.codigo) ";
								
								existeRelacion = true;
							}
							else
							{
								consultaArticulosFROM+=" INNER JOIN grupo_inventario gi ON(sgi.grupo=gi.codigo) ";
							}
							if(!consultaArticulosOrderBy.equals(""))
							{
								consultaArticulosOrderBy+=", ";
							}
							consultaArticulosOrderBy+=" gi.codigo ";
						break;
						case ConstantesBD.codigoRompimientoArtSubGrupo:
							mapaCodRompimientos.put("rompimiento_"+contadorRompimientos, ConstantesBD.codigoRompimientoArtSubGrupo+"");
							contadorRompimientos++;
							mapaCodRompimientos.put("numRegistros",contadorRompimientos);
							if(!consultaArticulosSelect.equals(""))
							{
								consultaArticulosSelect+=", ";
							}
							consultaArticulosSelect+=" sgi.codigo as codigorompimiento_"+contadorRompimientos+", sgi.nombre as nombrerompimiento_"+contadorRompimientos+", "+mapaParamDetArticulo.get("impsubtotart_"+j)+" as mostrarsubtotal_"+contadorRompimientos+", "+mapaParamDetArticulo.get("impdetalleart_"+j)+" as mostrardetalle_"+contadorRompimientos+" ";
							if(!existeRelacion)
							{
								consultaArticulosFROM+=" INNER JOIN subgrupo_inventario sgi ON(a.subgrupo=sgi.codigo) ";
								existeRelacion =  true;
							}
							if(!consultaArticulosOrderBy.equals(""))
							{
								consultaArticulosOrderBy+=", ";
							}
							consultaArticulosOrderBy+=" sgi.codigo ";
						break;
					}
				}
			}
			
			if(!consultaArticulosSelect.equals(""))
			{
				consultaArticulosSelect+=", ";
			}
			
			consultaArticulosSelect+=" a.codigo as codarticulo, " +
									 " getdescarticulo(a.codigo) as descripcionarticulo, " +
									 " dfs.cantidad_cargo as cantidad, "+
									 " dfs.valor_total as valortotal, " +
									 " getValorTotalArticuloXSolitud(dfs.articulo, dfs.factura,"+(tieneCCSolicita?"s.centro_costo_solicitante":"0")+", "+(tieneCCEjecuta?"s.centro_costo_solicitado":"0")+") as valorunitario";
			
			
			//logger.info("\n\nLas consultaFinal Articulos:\n"+" SELECT "+consultaArticulosSelect + consultaArticulosFROM + consultaArticulosWhere + " ORDER BY "+ consultaArticulosOrderBy+"\n\n");
			HashMap resultadoConsulta = new HashMap();
			resultadoConsulta = mundoFormato.consultaCuerpoImpresionFacturaArticulos(con, " SELECT "+consultaArticulosSelect + consultaArticulosFROM + consultaArticulosWhere + (consultaArticulosOrderBy.trim().equals("")?" ":" ORDER BY " +consultaArticulosOrderBy));
			resultadoConsulta.put("contadorrompimiento", contadorRompimientos);
			return resultadoConsulta;
		}
		catch (SQLException e) 
		{
			logger.error("Error cargando los datos de  generarConsultaArticulos  [FacutracionPdf] "+e);
			return null;
		}
		
		
	}
	
	/**
	 * Metodo para Imprimir una factura con Formato por Centro de Costo
	 * @param con
	 * @param usuario
	 * @param paciente
	 * @param facturasImprimir
	 * @param report
	 * @param nombreArchivo
	 * @param index
	 * @param request
	 * @param mapping
	 */
	public static String generarReporteFinalFacturaAgrupadaCC(Connection con, UsuarioBasico usuario, PersonaBasica paciente, HashMap facturasImprimir, PdfReports report, String nombreArchivo, int index,HttpServletRequest request,ActionMapping mapping) 
	{
		String oldQuery="", nitInstitucion ="";		
    	String nombreRptDesign = "FormatoFacturaAgrupadaCCVertical.rptdesign";

		InstitucionBasica ins = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
		
		//Informacion del Cabezote
		DesignEngineApi comp; 
        comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"facturacion/",nombreRptDesign);
        comp.insertImageHeaderOfMasterPage1(0, 0, ins.getLogoReportes());
        
        //nitInstitucion = Utilidades.getDescripcionTipoIdentificacion(con,ins.getTipoIdentificacion())+". "+ins.getNit();
        
        
        if(Utilidades.convertirAEntero(ins.getDigitoVerificacion()) != ConstantesBD.codigoNuncaValido)
        	nitInstitucion = Utilidades.getDescripcionTipoIdentificacion(con,ins.getTipoIdentificacion())+". "+ins.getNit()+" - "+ins.getDigitoVerificacion();
        else
        	nitInstitucion = Utilidades.getDescripcionTipoIdentificacion(con,ins.getTipoIdentificacion())+". "+ins.getNit();
        
        //*******TOTALES**************//
        HashMap<String, Object> mapa = new HashMap<String, Object> ();
        mapa=Utilidades.consultaTotalesFactura(con, Integer.parseInt(facturasImprimir.get("codigoFactura_"+index).toString()));
        logger.info("\n\n\n>>>>>>>>"+mapa+"\n\n\n");
        double saldos=0;
        saldos = Utilidades.convertirADouble(mapa.get("totalaf_0")+"")-Utilidades.convertirADouble(mapa.get("descuentos_0")+"")-Utilidades.convertirADouble(mapa.get("valornp_0")+"")-Utilidades.convertirADouble(mapa.get("valordp_0")+"");
        //saldos = saldos>0?saldos:0;
        //****************************//
        
        comp.obtenerComponentesDataSet("datosEncabezado1");
        
        oldQuery="SELECT " +
        			"CASE WHEN f.convenio IS NULL THEN '' ELSE " +
		        		"(CASE WHEN c.tipo_regimen='"+ConstantesBD.codigoTipoRegimenParticular+"' THEN " +
							"(CASE WHEN cu.codigo_responsable_paciente IS NOT NULL THEN " +
								"UPPER (rp.primer_nombre || ' ' || rp.primer_apellido) " +
							"ELSE " +
								"UPPER (p.primer_nombre || ' ' || p.primer_apellido) " +
							"END) " +
						"ELSE " +
							"UPPER (c.nombre) " +
						"END) " +
        			"END AS nombrec, " +
        			"CASE WHEN f.convenio IS NULL THEN '' ELSE " +
	    				"(CASE WHEN c.tipo_regimen='"+ConstantesBD.codigoTipoRegimenParticular+"' THEN " +
	    					"'' " +
	    				"ELSE " +
	    					"c.interfaz " +
	    				"END) " +
	    			"END AS codc, " +
        			"CASE WHEN f.convenio IS NULL THEN '' ELSE " +
    					"(CASE WHEN c.tipo_regimen='"+ConstantesBD.codigoTipoRegimenParticular+"' THEN " +
			    			"(CASE WHEN cu.codigo_responsable_paciente IS NOT NULL THEN " +
			    				//"rp.tipo_identificacion || ' ' || rp.numero_identificacion " +
			    				"rp.numero_identificacion " +
							"ELSE " +
								//"p.tipo_identificacion || ' ' || p.numero_identificacion " +
								"p.numero_identificacion " +
							"END) " +
						"ELSE " +
								"t.numero_identificacion " +
						"END) " +
	    			"END AS idec, " +
	    			"CASE WHEN f.convenio IS NULL THEN '' ELSE " +
	    				"(CASE WHEN c.tipo_regimen='"+ConstantesBD.codigoTipoRegimenParticular+"' THEN " +
			    				"(CASE WHEN cu.codigo_responsable_paciente IS NOT NULL THEN " +
									"UPPER(rp.tipo_identificacion) || ':'" +
								"ELSE " +
									"UPPER(p.tipo_identificacion) || ':'" +
								"END) " +
						"ELSE " +
							"'NIT:' " +
	    				"END) " +
	    			"END AS ccec, " +
	    			"CASE WHEN f.convenio IS NULL THEN '' ELSE " +
	    				"(CASE WHEN c.tipo_regimen='"+ConstantesBD.codigoTipoRegimenParticular+"' THEN " +
			    			"(CASE WHEN cu.codigo_responsable_paciente IS NOT NULL THEN " +
								"rp.direccion " +
							"ELSE " +
								"p.direccion " +
							"END) " +
						"ELSE " +
							"e.direccion " +
						"END) " +
	    			"END AS dirc, " +
	    			"CASE WHEN f.convenio IS NULL THEN '' ELSE " +
	    				"(CASE WHEN c.tipo_regimen='"+ConstantesBD.codigoTipoRegimenParticular+"' THEN " +
							"(CASE WHEN cu.codigo_responsable_paciente IS NOT NULL THEN " +
								"rp.telefono " +
							"ELSE " +
								"p.telefono " +
							"END) " +
						"ELSE " +
							"e.telefono " +
						"END) " +
					"END AS telec, " +
					"getconsecutivoingreso(cu.id_ingreso) AS ingrep, " +
					"f.consecutivo_factura AS factp, " +
					"f.fecha AS fechafp, " +
					"'"+ins.getRazonSocial()+"' AS razsoc, " +
					"'"+nitInstitucion+"' AS nitrz, " +
					"'"+mapa.get("etiquetavpl_0")+"' AS etiquetavpl, " +
					"'"+UtilidadTexto.formatearValores(mapa.get("valorpl_0")+"")+"' AS valorvpl, " +
					"'"+UtilidadTexto.formatearValores(mapa.get("valordp_0")+"")+"' AS valordp, " +
					"'"+mapa.get("etiquetavap_0")+"' AS etiquetavap, " +
					"'"+UtilidadTexto.formatearValores(mapa.get("valornp_0")+"")+"' AS valornp, " +
					"'"+UtilidadTexto.formatearValores(mapa.get("descuentos_0")+"")+"' AS descuentos, " +
					"'"+UtilidadTexto.formatearValores(saldos)+"' AS saldo, " +
					"convertirletras("+saldos+") || ' ' || coalesce(gettipomoneda("+usuario.getCodigoInstitucionInt()+"),' Pesos M/CTE') AS letras " +
	        		"FROM facturas f " +
	        			"LEFT OUTER JOIN convenios c ON (f.convenio=c.codigo) " +
	        			"LEFT OUTER JOIN sub_cuentas sc ON (f.sub_cuenta=sc.sub_cuenta) " +
	        			"LEFT OUTER JOIN cuentas cu ON (f.cuenta=cu.id) " +
	        			"LEFT OUTER JOIN responsables_pacientes rp ON (cu.codigo_responsable_paciente=rp.codigo) " +
	        			"LEFT OUTER JOIN personas p ON (cu.codigo_paciente=p.codigo) " +
	        			"LEFT OUTER JOIN empresas e ON (c.empresa=e.codigo) " +
	        			"LEFT OUTER JOIN terceros t ON (e.tercero=t.codigo) " +
	        		"WHERE f.codigo="+facturasImprimir.get("codigoFactura_"+index)+" ";
        
        logger.info("CONSULTA REPORTEEEE FACTURAS DATOS PACIENTE>>>>>>>>>>>>>>>>>>>>"+oldQuery);
        
        comp.modificarQueryDataSet(oldQuery);
        
        comp.obtenerComponentesDataSet("imprimirFactura");
        
        oldQuery="SELECT " +
					"CASE WHEN dfc.servicio IS NOT NULL THEN '400' ELSE getObtenerCodInterfNaturaleza(dfc.articulo) END AS codigo_consulta, " +
					//Se realizo un cambio en la siguiente linea ya que no se estaba
					//mostrando el indicativo medicamento; por ende se valida mejor
					//si el articulo es medicamento para poderlo mostrar como medicamento
					"CASE WHEN dfc.servicio IS NOT NULL THEN 'CUPS' ELSE (CASE WHEN getesmedicamento(dfc.articulo)='1' THEN 'MEDICAMENTOS' ELSE 'ELEMENTOS' END) END AS tipo_concepto, " +
					"CASE WHEN dfc.servicio is not null then " +
						"getidentificadorcentrocosto(getcodigoccejecuta(dfc.solicitud)) || '     -     ' || UPPER(getnomcentrocosto(getcodigoccejecuta(dfc.solicitud))) " +
					" else " +
						"getidentificadorcentrocosto(getCentCostoPrinSolMed(dfc.solicitud)) || '     -     ' || UPPER(getnomcentrocosto(getCentCostoPrinSolMed(dfc.solicitud))) " +
					"end AS centro_costo_concepto, " +
					"sum(coalesce(dfc.valor_total,0)) AS valor, " +
					"UPPER(tp.nombre) AS tipom, " +
    				"coalesce(f.valor_liquidado_paciente, 0) AS valori, " +
    				"CASE WHEN ctc.codigo="+ConstantesBD.codigoClasificacionTipoConvenioParticular+" THEN 'P' ELSE '' END AS tconv, " +
    				"CASE WHEN f.estado_paciente="+ConstantesBD.codigoEstadoFacturacionPacienteCancelada+" THEN coalesce((f.valor_bruto_pac-f.val_desc_pac),0) ELSE f.valor_abonos END AS valori2, " +
    				"CASE WHEN c.tipo_regimen='"+ConstantesBD.codigoTipoRegimenParticular+"' THEN 'N' ELSE 'S' END AS particular " +
				"FROM facturas f " +
						"INNER JOIN det_factura_solicitud dfc ON (f.codigo=dfc.factura) " +
						"LEFT OUTER JOIN tipos_monto tp ON (f.tipo_monto=tp.codigo) " +
						"LEFT OUTER JOIN convenios c ON (f.convenio=c.codigo) " +
	    				"LEFT OUTER JOIN tipos_convenio tc ON (c.tipo_convenio=tc.codigo AND c.institucion=tc.institucion) " +
	    				"LEFT OUTER JOIN clasificacion_tipo_convenio ctc ON (tc.clasificacion=ctc.codigo) " +
				"WHERE f.codigo="+facturasImprimir.get("codigoFactura_"+index)+" " +
				"GROUP BY codigo_consulta,tipo_concepto,centro_costo_concepto,tp.nombre,f.valor_bruto_pac,f.val_desc_pac,f.valor_total,ctc.codigo,f.valor_liquidado_paciente,f.codigo,f.estado_paciente,f.valor_abonos,c.tipo_regimen " +
				"ORDER BY tipo_concepto ";
        
        logger.info("CONSULTA REPORTEEEE FACTURAS>>>>>>>>>>>>>>>>>>>>"+oldQuery);
        
        comp.modificarQueryDataSet(oldQuery);
        
        comp.obtenerComponentesDataSet("datosPaciente");
        
        oldQuery="SELECT " +
        				"getnombrepersona(f.cod_paciente) AS nombrep, " +
        				"'"+paciente.getHistoriaClinica()+"' AS hisclinica, " +
        				"coalesce(getobtenercarnetpaciente(f.convenio,c.id_ingreso),'') AS carnetp, " +
        				"p.tipo_identificacion || '  ' || p.numero_identificacion AS identip, " +
        				"p.direccion AS direcp, " +
        				"p.telefono AS telep, " +
        				"'FECHA INGRESO: ' || to_char(i.fecha_ingreso, 'DD/MM/YYYY') AS fechai, " +
        				"CASE WHEN c.estado_cuenta="+ConstantesBD.codigoEstadoCuentaFacturada+" THEN 'FECHA SALIDA: ' || to_char(i.fecha_egreso, 'DD/MM/YYYY') ELSE (CASE WHEN c.estado_cuenta="+ConstantesBD.codigoEstadoCuentaFacturadaParcial+" THEN 'FECHA SALIDA: Factura Parcial' ELSE 'FECHA SALIDA:' END) END AS fechae, " +
        				"'"+ins.getPieFactura()+"' AS piepag, " +
        				"'"+UtilidadTexto.formatearValores(mapa.get("totalaf_0")+"")+"' AS totalppp, " +
        				"'"+ins.getDireccion()+"' AS dirff, " +
        				"'"+ins.getTelefono()+"' AS telff " +
    			"FROM facturas f " +
    				"INNER JOIN personas p ON (f.cod_paciente=p.codigo) " +
    				"LEFT OUTER JOIN cuentas c ON (f.cuenta=c.id) " +
    				"LEFT OUTER JOIN ingresos i ON (c.id_ingreso=i.id) " +
    			"WHERE f.codigo="+facturasImprimir.get("codigoFactura_"+index)+" ";
        
        comp.modificarQueryDataSet(oldQuery);
        
        logger.info("DATOS PACIENTE>>>>>>>>>>>>>>>>>>>>"+oldQuery);
        
        /*comp.obtenerComponentesDataSet("totales");
        
        oldQuery="SELECT " +
        				"f.valor_total AS totalaf, " +
        				"CASE WHEN ctc.codigo="+ConstantesBD.codigoClasificacionTipoConvenioEps+" " +
        						"OR " +
        						"ctc.codigo="+ConstantesBD.codigoClasificacionTipoConvenioArs+" " +
        						"OR " +
        						"ctc.codigo="+ConstantesBD.codigoClasificacionTipoConvenioPos+" " +
        						"OR " +
        						"ctc.codigo="+ConstantesBD.codigoClasificacionTipoConvenioVinculado+" " +
        						"OR " +
        						"ctc.codigo="+ConstantesBD.codigoClasificacionTipoConvenioPrepagada+" " +
        				"THEN UPPER(tp.nombre) || ' LIQUIDADO' ELSE " +
        					"(CASE WHEN ctc.codigo="+ConstantesBD.codigoClasificacionTipoConvenioArp+" " +
        							"OR " +
        							"ctc.codigo="+ConstantesBD.codigoClasificacionTipoConvenioAseguradora+" " +
        							"OR " +
        							"ctc.codigo="+ConstantesBD.codigoClasificacionTipoConvenioEmpresarial+" " +
        							"OR " +
        							"ctc.codigo="+ConstantesBD.codigoClasificacionTipoConvenioEventoCatastrofico+" " +
        							"OR " +
        							"ctc.codigo="+ConstantesBD.codigoClasificacionTipoConvenioOtra+" " +
        							"OR " +
        							"ctc.codigo="+ConstantesBD.codigoClasificacionTipoConvenioSoat+" " +
        					"THEN (CASE WHEN f.valor_neto_paciente=0 THEN '' ELSE UPPER(tp.nombre) || ' LIQUIDADO' END) " +
        					"ELSE '' END) " +
        				"END AS etiquetavpl, " +
        				"f.valor_liquidado_paciente AS valorpl, " +
        				"CASE WHEN f.val_desc_pac=0 THEN '' ELSE f.val_desc_pac END AS valordp, " +
        				"CASE WHEN ctc.codigo="+ConstantesBD.codigoClasificacionTipoConvenioEps+" " +
								"OR " +
								"ctc.codigo="+ConstantesBD.codigoClasificacionTipoConvenioArs+" " +
								"OR " +
								"ctc.codigo="+ConstantesBD.codigoClasificacionTipoConvenioPos+" " +
								"OR " +
								"ctc.codigo="+ConstantesBD.codigoClasificacionTipoConvenioVinculado+" " +
								"OR " +
								"ctc.codigo="+ConstantesBD.codigoClasificacionTipoConvenioPrepagada+" " +
						"THEN UPPER(tp.nombre) || ' RECAUDADO' ELSE " +
							"(CASE WHEN ctc.codigo="+ConstantesBD.codigoClasificacionTipoConvenioArp+" " +
									"OR " +
									"ctc.codigo="+ConstantesBD.codigoClasificacionTipoConvenioAseguradora+" " +
									"OR " +
									"ctc.codigo="+ConstantesBD.codigoClasificacionTipoConvenioEmpresarial+" " +
									"OR " +
									"ctc.codigo="+ConstantesBD.codigoClasificacionTipoConvenioEventoCatastrofico+" " +
									"OR " +
									"ctc.codigo="+ConstantesBD.codigoClasificacionTipoConvenioOtra+" " +
									"OR " +
									"ctc.codigo="+ConstantesBD.codigoClasificacionTipoConvenioSoat+" " +
							"THEN (CASE WHEN f.valor_neto_paciente=0 THEN '' ELSE UPPER(tp.nombre) || ' RECAUDADO' END) " +
							"ELSE '' END) " +
						"END AS etiquetavap, " +
						"f.valor_neto_paciente AS valornp, " +
						"coalesce(getdescuentoscargos(f.codigo),0) AS descuentos " +
    			"FROM facturas f " +
    				"LEFT OUTER JOIN tipos_monto tp ON (f.tipo_monto=tp.codigo) " +
    				"LEFT OUTER JOIN convenios c ON (f.convenio=c.codigo) " +
    				"LEFT OUTER JOIN tipos_convenio tc ON (c.tipo_convenio=tc.codigo AND c.institucion=tc.institucion) " +
    				"LEFT OUTER JOIN clasificacion_tipo_convenio ctc ON (tc.clasificacion=ctc.codigo) " +
    			"WHERE f.codigo="+facturasImprimir.get("codigoFactura_"+index)+" ";
        
        comp.modificarQueryDataSet(oldQuery);
        
        logger.info("TOTALES>>>>>>>>>>>>>>>>>>>>"+oldQuery);*/
         
        //debemos arreglar los alias para que funcione oracle, debido a que este los convierte a MAYUSCULAS
      	comp.lowerAliasDataSet();
		String newPathReport = comp.saveReport1(false);
        comp.updateJDBCParameters(newPathReport);
         
        if(!newPathReport.equals(""))
        {
        	return newPathReport;
        }
        return "";
	}
	
	/**
	 * Metodo para Imprimir una Factura con Formato por Item
	 * @param con
	 * @param usuario
	 * @param paciente
	 * @param facturasImprimir
	 * @param report
	 * @param nombreArchivo
	 * @param index
	 * @param request
	 * @param mapping
	 */
	public static String generarReporteFinalFacturaAgrupadaIT(Connection con, UsuarioBasico usuario, PersonaBasica paciente, HashMap facturasImprimir, PdfReports report, String nombreArchivo, int index,HttpServletRequest request,ActionMapping mapping) 
	{
		String oldQuery1="",oldQuery2="",oldQuery3="", nitInstitucion;
    	String nombreRptDesign = "FormatoFacturaAgrupadaITVertical.rptdesign";

		InstitucionBasica ins = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
		
		//Informacion del Cabezote
		DesignEngineApi comp; 
        comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"facturacion/",nombreRptDesign);
        comp.insertImageHeaderOfMasterPage1(0, 0, ins.getLogoReportes());
        //nitInstitucion = Utilidades.getDescripcionTipoIdentificacion(con,ins.getTipoIdentificacion())+". "+ins.getNit();
       
        if(Utilidades.convertirAEntero(ins.getDigitoVerificacion()) != ConstantesBD.codigoNuncaValido)
        	nitInstitucion = Utilidades.getDescripcionTipoIdentificacion(con,ins.getTipoIdentificacion())+". "+ins.getNit()+" - "+ins.getDigitoVerificacion();
        else
        	nitInstitucion = Utilidades.getDescripcionTipoIdentificacion(con,ins.getTipoIdentificacion())+". "+ins.getNit();
       
       
        //*******TOTALES**************//
        HashMap<String, Object> mapa = new HashMap<String, Object> ();
        mapa=Utilidades.consultaTotalesFactura(con, Integer.parseInt(facturasImprimir.get("codigoFactura_"+index).toString()));        
        double saldos = 0;
        saldos = Utilidades.convertirADouble(mapa.get("totalaf_0").toString())-Utilidades.convertirADouble(mapa.get("descuentos_0").toString())-Utilidades.convertirADouble(mapa.get("valornp_0").toString())-Utilidades.convertirADouble(mapa.get("valordp_0")+"");
        //saldos = saldos>0?saldos:0;
        //****************************//
        
        comp.obtenerComponentesDataSet("datosEncabezado1");
        
        oldQuery1="SELECT " +
        			"CASE WHEN f.convenio IS NULL THEN '' ELSE " +
        				"(CASE WHEN c.tipo_regimen='"+ConstantesBD.codigoTipoRegimenParticular+"' THEN " +
    						"(CASE WHEN cu.codigo_responsable_paciente IS NOT NULL THEN " +
    							"UPPER (rp.primer_nombre || ' ' || rp.primer_apellido) " +
    						"ELSE " +
    							"UPPER (p.primer_nombre || ' ' || p.primer_apellido) " +
    						"END) " +
        				"ELSE " +
        					"UPPER (c.nombre) " +
        				"END) " +
        			"END AS nombrec, " +
        			
        			"CASE WHEN f.convenio IS NULL THEN '' ELSE " +
	    				"(CASE WHEN c.tipo_regimen='"+ConstantesBD.codigoTipoRegimenParticular+"' THEN " +
	    					"''" +
	    				"ELSE " +
	    					"c.interfaz " +
	    				"END) " +
	    			"END AS codc, " +
	    			
        			"CASE WHEN f.convenio IS NULL THEN '' ELSE " +
	    				"(CASE WHEN c.tipo_regimen='"+ConstantesBD.codigoTipoRegimenParticular+"' THEN " +
			    				"(CASE WHEN cu.codigo_responsable_paciente IS NOT NULL THEN " +
									//"rp.tipo_identificacion || ' ' || rp.numero_identificacion " +
			    					"rp.numero_identificacion " +
								"ELSE " +
									//"p.tipo_identificacion || ' ' || p.numero_identificacion " +
									"p.numero_identificacion " +
								"END) " +
    					"ELSE " +
								"t.numero_identificacion " + 
	    				"END) " +
	    			"END AS idec, " +
	    			
	    			"CASE WHEN f.convenio IS NULL THEN '' ELSE " +
	    				"(CASE WHEN c.tipo_regimen='"+ConstantesBD.codigoTipoRegimenParticular+"' THEN " +
			    				"(CASE WHEN cu.codigo_responsable_paciente IS NOT NULL THEN " +
									"UPPER(rp.tipo_identificacion) || ':'" +
								"ELSE " +
									"UPPER(p.tipo_identificacion) || ':'" +
								"END) " +
						"ELSE " +
							"'NIT:' " +
	    				"END) " +
	    			"END AS ccec, " +
	    			
	    			"CASE WHEN f.convenio IS NULL THEN '' ELSE " +
	    				"(CASE WHEN c.tipo_regimen='"+ConstantesBD.codigoTipoRegimenParticular+"' THEN " +
			    				"(CASE WHEN cu.codigo_responsable_paciente IS NOT NULL THEN " +
									"rp.direccion " +
								"ELSE " +
									"p.direccion " +
								"END) " +
    					"ELSE " +
    						"e.direccion " +
    					"END) " +
	    			"END AS dirc, " +
	    			"CASE WHEN f.convenio IS NULL THEN '' ELSE " +
						"(CASE WHEN c.tipo_regimen='"+ConstantesBD.codigoTipoRegimenParticular+"' THEN " +
								"(CASE WHEN cu.codigo_responsable_paciente IS NOT NULL THEN " +
									"rp.telefono " +
								"ELSE " +
									"p.telefono " +
								"END) " +
						"ELSE " +
							"e.telefono " +
						"END) " +
					"END AS telec, " +
					"getconsecutivoingreso(cu.id_ingreso) AS ingrep, " +
					"f.consecutivo_factura AS factp, " +
					"f.fecha AS fechafp, " +
					"'"+ins.getRazonSocial()+"' AS razsoc, " +
					"'"+nitInstitucion+"' AS nitrz, " +
					"'"+mapa.get("etiquetavpl_0")+"' AS etiquetavpl, " +
					"'"+UtilidadTexto.formatearValores(mapa.get("valorpl_0")+"")+"' AS valorvpl, " +
					"'"+UtilidadTexto.formatearValores(mapa.get("valordp_0")+"")+"' AS valordp, " +
					"'"+mapa.get("etiquetavap_0")+"' AS etiquetavap, " +
					"'"+UtilidadTexto.formatearValores(mapa.get("valornp_0")+"")+"' AS valornp, " +
					"'"+UtilidadTexto.formatearValores(mapa.get("descuentos_0")+"")+"' AS descuentos, " +
					"'"+UtilidadTexto.formatearValores(saldos)+"' AS saldo, " +
					"convertirletras("+saldos+") || ' ' || coalesce(gettipomoneda("+usuario.getCodigoInstitucionInt()+"),' Pesos M/CTE') AS letras " +
					"FROM facturas f " +
	        			"LEFT OUTER JOIN convenios c ON (f.convenio=c.codigo) " +
	        			"LEFT OUTER JOIN sub_cuentas sc ON (f.sub_cuenta=sc.sub_cuenta) " +
	        			"LEFT OUTER JOIN cuentas cu ON (f.cuenta=cu.id) " +
	        			"LEFT OUTER JOIN responsables_pacientes rp ON (cu.codigo_responsable_paciente=rp.codigo) " +
	        			"LEFT OUTER JOIN empresas e ON (c.empresa=e.codigo) " +
	        			"LEFT OUTER JOIN terceros t ON (e.tercero=t.codigo) " +
	        			"LEFT OUTER JOIN personas p ON (cu.codigo_paciente=p.codigo) " +
	        		"WHERE f.codigo="+facturasImprimir.get("codigoFactura_"+index)+" ";
        
        logger.info("DATOS ENCABEZADO REPORTE>>>>>>>>>>>>>>>>>>>>"+oldQuery1);
        
        comp.modificarQueryDataSet(oldQuery1);
        
        comp.obtenerComponentesDataSet("imprimirFacturaPIT");
        
        //Modificada la consulta por la Tarea 35434
        /*oldQuery2="SELECT " +
					"CASE WHEN dfc.servicio IS NOT NULL THEN '400' ELSE getObtenerCodInterfNaturaleza(dfc.articulo) END AS codigo_consulta, " +
					"CASE WHEN dfc.servicio IS NOT NULL THEN 'CUPS' ELSE " +
					"(CASE WHEN getObtenerCodNaturalezaArtic(dfc.articulo)='"+ConstantesBD.codigoNaturalezaArticuloMedicamentoPos+"' " +
					"OR getObtenerCodNaturalezaArtic(dfc.articulo)='"+ConstantesBD.codigoNaturalezaArticuloMedicamentoNoPos+"' " +
					"THEN 'MEDICAMENTOS' ELSE 'ELEMENTOS' END) END AS tipo_concepto, " +
					"CASE WHEN dfc.servicio IS NOT NULL THEN getcodigopropservicio2(dfc.servicio,c.tipo_codigo) ELSE '' END AS codresp, " +
					"CASE WHEN dfc.servicio IS NOT NULL THEN getcodigopropservicio2(dfc.servicio,"+ConstantesBD.codigoTarifarioCups+") ELSE " +
							"getobtenercodigointerfaz(dfc.articulo) END AS codp, " +
					"CASE WHEN dfc.servicio IS NOT NULL THEN getnombreservicio(dfc.servicio,"+ConstantesBD.codigoTarifarioCups+") ELSE " +
							"getdescripcionarticulo(dfc.articulo) END AS descp, " +
					"sum(coalesce(dfc.cantidad_cargo,0)) AS cantp, " +
					"getdivisionvalores((sum(coalesce(dfc.valor_total,0))),(sum(coalesce(dfc.cantidad_cargo,0)))) AS valorup, " +
					"sum(coalesce(dfc.valor_total,0)) AS valor, " +
					"tp.nombre AS tipom, " +
    				"coalesce((f.valor_bruto_pac-f.val_desc_pac),0) AS valori, " +
    				"CASE WHEN ctc.codigo="+ConstantesBD.codigoClasificacionTipoConvenioParticular+" THEN 'P' ELSE '' END AS tconv, " +
    				"CASE WHEN f.estado_paciente="+ConstantesBD.codigoEstadoFacturacionPacienteCancelada+" THEN coalesce((f.valor_bruto_pac-f.val_desc_pac),0) ELSE f.valor_abonos END AS valori2 " +
				"FROM facturas f " +
						"INNER JOIN det_factura_solicitud dfc ON (f.codigo=dfc.factura) " +
						"LEFT OUTER JOIN tipos_monto tp ON (f.tipo_monto=tp.codigo) " +
						"LEFT OUTER JOIN convenios c ON (f.convenio=c.codigo) " +
	    				"LEFT OUTER JOIN tipos_convenio tc ON (c.tipo_convenio=tc.codigo AND c.institucion=tc.institucion) " +
	    				"LEFT OUTER JOIN clasificacion_tipo_convenio ctc ON (tc.clasificacion=ctc.codigo) " +
				"WHERE f.codigo="+facturasImprimir.get("codigoFactura_"+index)+" " +
				"GROUP BY codigo_consulta,tipo_concepto,codresp,codp,descp,dfc.cantidad_cargo,dfc.valor_total,f.valor_total,f.valor_liquidado_paciente,f.val_desc_pac,f.valor_bruto_pac,f.codigo,tp.nombre,ctc.codigo,f.estado_paciente,f.valor_abonos " +
				"ORDER BY tipo_concepto,codresp ";*/

        oldQuery2 = "SELECT " +
    					"tabla1.codigoagrupado, " +
        				"tabla1.codigo_consulta AS codigo_consulta, " +
    					"tabla1.tipo_concepto AS tipo_concepto, " +
    					"tabla1.codresp AS codresp, " +
    					"tabla1.codp AS codp," +
    					"tabla1.descp AS descp, " +
    					"sum(tabla1.cantp) AS cantp, " +
    					"tabla1.valorup AS valorup, " +
    					"sum(tabla1.valor) AS valor, " +
    					"tabla1.tipom AS tipom, " +
    					"tabla1.valori AS valori, " +
    					"tabla1.tconv AS tconv, " +
    					"tabla1.valori2 AS valori2, " +
    					"tabla1.escirugia AS escirugia, " +
    					"tabla1.esasocio AS esasocio, " +
    					"tabla1.particular AS particular " +
        			"FROM " +
			        	"(SELECT * FROM "+
			        			"("+
			        				"(" +
			        					"SELECT "+ 
				        					"CASE WHEN dfc.tipo_solicitud = 14 THEN dfc.codigo ELSE -1 END AS codigoagrupado, " +
			        						"dfc.codigo AS codigodetallefactura, "+
			        						"CASE WHEN dfc.servicio IS NOT NULL THEN '400' ELSE getObtenerCodInterfNaturaleza(dfc.articulo) END AS codigo_consulta, "+
			        						//Se realizo un cambio en la siguiente linea ya que no se estaba
			        						//mostrando el indicativo medicamento; por ende se valida mejor
			        						//si el articulo es medicamento para poderlo mostrar como medicamento
			        						//"CASE WHEN dfc.servicio IS NOT NULL THEN 'CUPS' ELSE (CASE WHEN getObtenerCodNaturalezaArtic(dfc.articulo)='"+ConstantesBD.acronimoNaturalezaArticuloMedicamentoPos+"' OR getobtenercodigonaturalezaarticulo(dfc.articulo)='"+ConstantesBD.acronimoNaturalezaArticuloMedicamentoNoPos+"' THEN 'MEDICAMENTOS' ELSE 'ELEMENTOS' END) END AS tipo_concepto, "+
			        						"CASE WHEN dfc.servicio IS NOT NULL THEN 'CUPS' ELSE (CASE WHEN getesmedicamento(dfc.articulo)='1' THEN 'MEDICAMENTOS' ELSE 'ELEMENTOS' END) END AS tipo_concepto, " +
			        						"CASE WHEN dfc.servicio IS NOT NULL THEN getcodigopropservicio2(dfc.servicio,c.tipo_codigo) ELSE '' END AS codresp, "+
				        					"CASE WHEN dfc.servicio IS NOT NULL THEN getcodigopropservicio2(dfc.servicio,"+ConstantesBD.codigoTarifarioCups+") ELSE getobtenercodigointerfaz(dfc.articulo) END AS codp, "+
				        					"CASE WHEN dfc.servicio IS NOT NULL THEN UPPER(getnombreservicio(dfc.servicio,"+ConstantesBD.codigoTarifarioCups+")) ELSE UPPER(getdescripcionalternaArticulo(dfc.articulo)) END AS descp, "+
				        					"sum(coalesce(dfc.cantidad_cargo,0)) AS cantp, "+
				        					//Modificado por los requerimientos de la Clinica Shaio Tarea
				        					//"CASE WHEN dfc.tipo_solicitud = "+ConstantesBD.codigoTipoSolicitudCirugia+" AND dfc.servicio IS NOT NULL THEN 0 ELSE getdivisionvalores((sum(coalesce(dfc.valor_total,0))),(sum(coalesce(dfc.cantidad_cargo,0)))) END AS valorup, "+
				        					//"CASE WHEN dfc.tipo_solicitud = "+ConstantesBD.codigoTipoSolicitudCirugia+" AND dfc.servicio IS NOT NULL THEN 0 ELSE sum(coalesce(dfc.valor_total,0)) END AS valor, "+
				        					"CASE WHEN dfc.tipo_solicitud = "+ConstantesBD.codigoTipoSolicitudCirugia+" AND dfc.servicio IS NOT NULL THEN "+ConstantesBD.codigoNuncaValido+" ELSE getdivisionvalores((sum(coalesce(dfc.valor_total,0))),(sum(coalesce(dfc.cantidad_cargo,0)))) END AS valorup, " +
				        					"sum(coalesce(dfc.valor_total, 0)) AS valor, "+
				        					"sum(coalesce(dfc.valor_total, 0)) AS valorreal, "+
				        					"UPPER(tp.nombre) AS tipom, "+
				        					"coalesce(f.valor_liquidado_paciente,0) AS valori, "+
				        					"CASE WHEN ctc.codigo = "+ConstantesBD.codigoClasificacionTipoConvenioParticular+" THEN 'P' ELSE '' END AS tconv, "+
				        					"CASE WHEN f.estado_paciente = "+ConstantesBD.codigoEstadoFacturacionPacienteCancelada+" THEN coalesce((f.valor_bruto_pac-f.val_desc_pac),0) ELSE f.valor_abonos END AS valori2, "+
				        					"CASE WHEN dfc.tipo_solicitud = "+ConstantesBD.codigoTipoSolicitudCirugia+" THEN 'S' ELSE 'N' END AS escirugia, "+
				        					"dfc.solicitud AS numerosolicitud, "+
				        					"'"+ConstantesBD.acronimoNo+"' AS esasocio, " +
				        					"CASE WHEN c.tipo_regimen='"+ConstantesBD.codigoTipoRegimenParticular+"' THEN 'N' ELSE 'S' END AS particular " +
				        				"FROM "+ 
				        					"facturas f "+ 
				        					"INNER JOIN det_factura_solicitud dfc ON (f.codigo=dfc.factura) "+ 
											"LEFT OUTER JOIN tipos_monto tp ON (f.tipo_monto=tp.codigo) "+ 
											"LEFT OUTER JOIN convenios c ON (f.convenio=c.codigo) "+ 
											"LEFT OUTER JOIN tipos_convenio tc ON (c.tipo_convenio=tc.codigo AND c.institucion=tc.institucion) "+ 
											"LEFT OUTER JOIN clasificacion_tipo_convenio ctc ON (tc.clasificacion=ctc.codigo) "+
										"WHERE "+ 
											"f.codigo="+facturasImprimir.get("codigoFactura_"+index)+" "+
										"GROUP BY "+
											"codigoagrupado, codigodetallefactura, codigo_consulta, tipo_concepto, dfc.servicio, dfc.articulo, c.tipo_codigo, dfc.cantidad_cargo, dfc.valor_total, f.valor_total, f.valor_liquidado_paciente, f.val_desc_pac, f.valor_bruto_pac, f.codigo, tp.nombre, ctc.codigo, f.estado_paciente, f.valor_abonos, dfc.tipo_solicitud, dfc.solicitud, c.tipo_regimen "+
									") "+
									"UNION "+
									"("+ 
										"SELECT "+ 
											"dfs.codigo as codigoagrupado, " +
											"dfs.codigo AS codigodetallefactura, "+
											"'400' AS codigo_consulta, "+
											"'CUPS' AS tipo_concepto, "+
											"getcodigopropservicio2(adf.servicio_asocio,c.tipo_codigo) AS codresp, "+
											"getcodigopropservicio2(adf.servicio_asocio,"+ConstantesBD.codigoTarifarioCups+") AS codp, "+
											"CASE WHEN ta.codigo_asocio = 'P' THEN UPPER(ta.nombre_asocio) ELSE UPPER(getnombreservicio(adf.servicio_asocio, "+ConstantesBD.codigoTarifarioCups+")) END AS descp, "+
											"dc.cantidad_cargada AS cantp, "+
											"adf.valor_asocio AS valorup, "+
											//Modificado por los requerimientos de la Clinica Shaio Tarea
											//"adf.valor_total AS valor, "+
											"0 AS valor, "+
											"1 AS valorreal, " +
											"UPPER(tp.nombre) AS tipom, "+
											"coalesce(f.valor_liquidado_paciente,0) AS valori, "+
											"CASE WHEN ctc.codigo="+ConstantesBD.codigoClasificacionTipoConvenioParticular+" THEN 'P' ELSE '' END AS tconv, "+
											"CASE WHEN f.estado_paciente="+ConstantesBD.codigoEstadoFacturacionPacienteCancelada+" THEN coalesce((f.valor_bruto_pac-f.val_desc_pac),0) ELSE f.valor_abonos END AS valori2, "+
											"'N' AS escirugia, "+
											"dfs.solicitud AS numerosolicitud, "+
											"'"+ConstantesBD.acronimoSi+"' AS esasocio, " +
											"CASE WHEN c.tipo_regimen='"+ConstantesBD.codigoTipoRegimenParticular+"' THEN 'N' ELSE 'S' END AS particular " +
										"FROM "+ 
											"facturas f "+ 
											"INNER JOIN det_factura_solicitud dfs ON (f.codigo=dfs.factura) "+ 
											"INNER JOIN det_cargos dc ON(dc.codigo_factura=f.codigo AND dc.solicitud=dfs.solicitud) "+
											"INNER JOIN asocios_det_factura adf ON(adf.codigo=dfs.codigo) "+
											"LEFT OUTER JOIN tipos_monto tp ON (f.tipo_monto=tp.codigo) "+ 
											"LEFT OUTER JOIN convenios c ON (f.convenio=c.codigo) "+ 
											"LEFT OUTER JOIN tipos_convenio tc ON (c.tipo_convenio=tc.codigo AND c.institucion=tc.institucion) "+ 
											"LEFT OUTER JOIN clasificacion_tipo_convenio ctc ON (tc.clasificacion=ctc.codigo) "+
											"INNER JOIN tipos_asocio ta ON (adf.tipo_asocio = ta.codigo) " +
										"WHERE "+ 
											"f.codigo="+facturasImprimir.get("codigoFactura_"+index)+" AND dfs.tipo_solicitud="+ConstantesBD.codigoTipoSolicitudCirugia+" AND dc.articulo IS NULL "+
										"GROUP BY " +
											"codigoagrupado, codigodetallefactura, codigo_consulta, tipo_concepto, adf.servicio_asocio, c.tipo_codigo, cantp, valorup, valor, tipom, valori, tconv, valori2, escirugia, numerosolicitud, ta.codigo_asocio, ta.nombre_asocio, c.tipo_regimen "+
									") "+
								") "+
								"tabla " +
						"WHERE " +
							"tabla.valorreal > 0 " +
						"ORDER BY " +
							"numerosolicitud, codigodetallefactura, escirugia DESC, tipo_concepto, codresp" +
					") tabla1 " +
					"GROUP BY " +
						"tabla1.codigoagrupado, " +
						"tabla1.codigo_consulta, " +
    					"tabla1.tipo_concepto, " +
    					"tabla1.codresp, " +
    					"tabla1.codp," +
    					"tabla1.descp, " +
    					"tabla1.valorup, " +
    					"tabla1.tipom, " +
    					"tabla1.valori, " +
    					"tabla1.tconv, " +
    					"tabla1.valori2, " +
    					"tabla1.escirugia, " +
    					"tabla1.esasocio, " +
    					"tabla1.particular " +
    				"ORDER BY " +
    					"tabla1.codigoagrupado, " +
    					"tabla1.escirugia DESC ";
        
        logger.info("CONSULTA REPORTEEEE FACTURAS POR ITEM>>>>>>>>>>>>>>>>>>>>"+oldQuery2);
        
        comp.modificarQueryDataSet(oldQuery2);
        
        comp.obtenerComponentesDataSet("datosPaciente");
        
        oldQuery3="SELECT " +
        				"getnombrepersona(f.cod_paciente) AS nombrep, " +
        				"'"+paciente.getHistoriaClinica()+"' AS hisclinica, " +
        				"coalesce(getobtenercarnetpaciente(f.convenio,c.id_ingreso),'') AS carnetp, " +
        				"p.tipo_identificacion || '  ' || p.numero_identificacion AS identip, " +
        				"p.direccion AS direcp, " +
        				"p.telefono AS telep, " +
        				"'FECHA INGRESO: ' || to_char(i.fecha_ingreso, 'DD/MM/YYYY') AS fechai, " +
        				"CASE WHEN c.estado_cuenta="+ConstantesBD.codigoEstadoCuentaFacturada+" THEN 'FECHA SALIDA: ' || to_char(i.fecha_egreso, 'DD/MM/YYYY') ELSE (CASE WHEN c.estado_cuenta="+ConstantesBD.codigoEstadoCuentaFacturadaParcial+" THEN 'FECHA SALIDA: Factura Parcial' ELSE 'FECHA SALIDA:' END) END AS fechae, " +
        				"'"+ins.getPieFactura()+"' AS piepag, " +
        				"'"+UtilidadTexto.formatearValores(mapa.get("totalaf_0")+"")+"' AS totalppp, " +
        				"'"+ins.getDireccion()+"' AS dirff, " +
        				"'"+ins.getTelefono()+"' AS telff " +
    			"FROM facturas f " +
    				"INNER JOIN personas p ON (f.cod_paciente=p.codigo) " +
    				"LEFT OUTER JOIN cuentas c ON (f.cuenta=c.id) " +
    				"LEFT OUTER JOIN ingresos i ON (c.id_ingreso=i.id) " +
    			"WHERE f.codigo="+facturasImprimir.get("codigoFactura_"+index)+" ";
        
        logger.info("DATOS PACIENTEJOOOO>>>>>>>>>>>>>>>>>>>>"+oldQuery3);
        
        comp.modificarQueryDataSet(oldQuery3);
        
        /*comp.obtenerComponentesDataSet("totales");
        
        oldQuery="SELECT " +
        				"f.valor_total AS totalaf, " +
        				"CASE WHEN ctc.codigo="+ConstantesBD.codigoClasificacionTipoConvenioEps+" " +
        						"OR " +
        						"ctc.codigo="+ConstantesBD.codigoClasificacionTipoConvenioArs+" " +
        						"OR " +
        						"ctc.codigo="+ConstantesBD.codigoClasificacionTipoConvenioPos+" " +
        						"OR " +
        						"ctc.codigo="+ConstantesBD.codigoClasificacionTipoConvenioVinculado+" " +
        						"OR " +
        						"ctc.codigo="+ConstantesBD.codigoClasificacionTipoConvenioPrepagada+" " +
        				"THEN UPPER(tp.nombre) || ' LIQUIDADO' ELSE " +
        					"(CASE WHEN ctc.codigo="+ConstantesBD.codigoClasificacionTipoConvenioArp+" " +
        							"OR " +
        							"ctc.codigo="+ConstantesBD.codigoClasificacionTipoConvenioAseguradora+" " +
        							"OR " +
        							"ctc.codigo="+ConstantesBD.codigoClasificacionTipoConvenioEmpresarial+" " +
        							"OR " +
        							"ctc.codigo="+ConstantesBD.codigoClasificacionTipoConvenioEventoCatastrofico+" " +
        							"OR " +
        							"ctc.codigo="+ConstantesBD.codigoClasificacionTipoConvenioOtra+" " +
        							"OR " +
        							"ctc.codigo="+ConstantesBD.codigoClasificacionTipoConvenioSoat+" " +
        					"THEN (CASE WHEN f.valor_neto_paciente=0 THEN '' ELSE UPPER(tp.nombre) || ' LIQUIDADO' END) " +
        					"ELSE '' END) " +
        				"END AS etiquetavpl, " +
        				"f.valor_liquidado_paciente AS valorpl, " +
        				"CASE WHEN f.val_desc_pac=0 THEN '' ELSE f.val_desc_pac END AS valordp, " +
        				"CASE WHEN ctc.codigo="+ConstantesBD.codigoClasificacionTipoConvenioEps+" " +
								"OR " +
								"ctc.codigo="+ConstantesBD.codigoClasificacionTipoConvenioArs+" " +
								"OR " +
								"ctc.codigo="+ConstantesBD.codigoClasificacionTipoConvenioPos+" " +
								"OR " +
								"ctc.codigo="+ConstantesBD.codigoClasificacionTipoConvenioVinculado+" " +
								"OR " +
								"ctc.codigo="+ConstantesBD.codigoClasificacionTipoConvenioPrepagada+" " +
						"THEN UPPER(tp.nombre) || ' RECAUDADO' ELSE " +
							"(CASE WHEN ctc.codigo="+ConstantesBD.codigoClasificacionTipoConvenioArp+" " +
									"OR " +
									"ctc.codigo="+ConstantesBD.codigoClasificacionTipoConvenioAseguradora+" " +
									"OR " +
									"ctc.codigo="+ConstantesBD.codigoClasificacionTipoConvenioEmpresarial+" " +
									"OR " +
									"ctc.codigo="+ConstantesBD.codigoClasificacionTipoConvenioEventoCatastrofico+" " +
									"OR " +
									"ctc.codigo="+ConstantesBD.codigoClasificacionTipoConvenioOtra+" " +
									"OR " +
									"ctc.codigo="+ConstantesBD.codigoClasificacionTipoConvenioSoat+" " +
							"THEN (CASE WHEN f.valor_neto_paciente=0 THEN '' ELSE UPPER(tp.nombre) || ' RECAUDADO' END) " +
							"ELSE '' END) " +
						"END AS etiquetavap, " +
						"f.valor_neto_paciente AS valornp, " +
						"coalesce(getdescuentoscargos(f.codigo),0) AS descuentos " +
    			"FROM facturas f " +
    				"LEFT OUTER JOIN tipos_monto tp ON (f.tipo_monto=tp.codigo) " +
    				"LEFT OUTER JOIN convenios c ON (f.convenio=c.codigo) " +
    				"LEFT OUTER JOIN tipos_convenio tc ON (c.tipo_convenio=tc.codigo AND c.institucion=tc.institucion) " +
    				"LEFT OUTER JOIN clasificacion_tipo_convenio ctc ON (tc.clasificacion=ctc.codigo) " +
    			"WHERE f.codigo="+facturasImprimir.get("codigoFactura_"+index)+" ";
        
        comp.modificarQueryDataSet(oldQuery);*/
         
        //debemos arreglar los alias para que funcione oracle, debido a que este los convierte a MAYUSCULAS
      	comp.lowerAliasDataSet();
		String newPathReport = comp.saveReport1(false);
        comp.updateJDBCParameters(newPathReport);
         
        if(!newPathReport.equals(""))
        {
        	return newPathReport;
        }
        return "";
	}	
	
	private static void generarReporteVersalles(Connection con, UsuarioBasico usuario, HashMap facturasImprimir, PdfReports report, String nombreArchivo, int index, HttpServletRequest request, String tipo) 
	{
		String tamImp=UtilidadesFacturacion.obtenerTamanioImpresionFactura(con,facturasImprimir.get("codigoFactura_"+index)+"");
		if(tamImp.equals(ConstantesIntegridadDominio.acronimoMediaCarta))
			report = new PdfReports(true);
		else
			report = new PdfReports();
        
        HashMap mapaFactura=new HashMap();
        HashMap mapaDatosInstitucion=new HashMap();                    
        HashMap mapaDatosPaciente=new HashMap();
        HashMap mapaDatosServicios=new HashMap();
        HashMap mapaDatosDetServicios;
        HashMap mapaDatosArticulos=new HashMap();
        
        HashMap mapaDatosMedicamentos= new HashMap();
        HashMap mapaDatosInsumos= new HashMap();
        
        ImpresionFactura mundoImpresion=new ImpresionFactura(); 
        mapaDatosInstitucion=mundoImpresion.consultarSeccionInstitucionFormatoVersalles(con,usuario.getCodigoInstitucionInt(), facturasImprimir.get("codigoFactura_"+index)+"", Double.parseDouble(facturasImprimir.get("empresaInstitucion_"+index)+""));
        logger.info("datos institucion->"+mapaDatosInstitucion+"\n");
        mapaDatosPaciente=mundoImpresion.consultarSeccionPacienteFormatoVersalles(con,facturasImprimir.get("codigoFactura_"+index)+"");
        logger.info("datos paciente->"+mapaDatosPaciente+"\n");
        mapaDatosServicios=mundoImpresion.consultarSeccionServiciosFormatoVersalles(con,facturasImprimir.get("codigoFactura_"+index)+"");
        logger.info("datos servicios->"+mapaDatosServicios+"\n");
        if(!mapaDatosServicios.isEmpty())
        {
            for(int k=0;k<Integer.parseInt(mapaDatosServicios.get("numRegistros")+"");k++)
            {
                mapaDatosDetServicios=new HashMap();
                if((mapaDatosServicios.get("escirugia_"+k)+"").equals("true"))
                {
                    mapaDatosDetServicios=mundoImpresion.consultarSeccionDetServiciosFormatoVersalles(con,(mapaDatosServicios.get("codigodetallefactura_"+k)+""));
                    logger.info("datos detalle servicio->"+mapaDatosDetServicios+"\n");                               
                }
                mapaDatosServicios.put("datosDetServicio_"+k,mapaDatosDetServicios);
            }
        }         
        
        //tomo el pais para determinar que formato debo imprimir
        if(System.getProperty("PAIS").toString().toUpperCase().equals("VENEZUELA"))
        {
        	mapaDatosInsumos=mundoImpresion.consultarSeccionArticulosFormatoVersalles(con,facturasImprimir.get("codigoFactura_"+index)+"", ConstantesBD.acronimoSi);
        	mapaDatosMedicamentos=mundoImpresion.consultarSeccionArticulosFormatoVersalles(con,facturasImprimir.get("codigoFactura_"+index)+"", ConstantesBD.acronimoNo);
        	
        	mapaFactura.put("cuerpoDatosMedicamentos",mapaDatosMedicamentos);
        	mapaFactura.put("cuerpoDatosInsumos", mapaDatosInsumos);
        }
        else
        {
        	//NO SE LE ENVIA EL FILTRO DE MEDICAMENTOS E INSUMOS, TODO SE MUESTRA EN UNO SOLO
        	mapaDatosArticulos=mundoImpresion.consultarSeccionArticulosFormatoVersalles(con,facturasImprimir.get("codigoFactura_"+index)+"", "");
        	mapaFactura.put("cuerpoDatosArticulos",mapaDatosArticulos);
        	logger.info("datos articulos->"+mapaDatosArticulos+"\n");
        }
        
        mapaFactura.put("headerDatosInstitucion",mapaDatosInstitucion);
        mapaFactura.put("headerAndFooterDatosPaciente",mapaDatosPaciente);
        mapaFactura.put("cuerpoDatosServicios",mapaDatosServicios);
        mapaFactura.put("codigoFactura",facturasImprimir.get("codigoFactura_"+index)+"");
        	
        //Modificado por Tarea 138394
        //mapaFactura.put("nroAutorizacion", facturasImprimir.get("nroAutorizacion_"+index)+"");
        mapaFactura.put("nroAutorizacion", mapaDatosPaciente.get("numeroautorizacion").toString());
        
        mapaFactura.put("empresaInstitucion", facturasImprimir.get("empresaInstitucion_"+index)+"");
        
        int codFact=Integer.parseInt(facturasImprimir.get("codigoFactura_"+index)+"");
        String consecutivoFactura=Utilidades.obtenerConsecutivoFactura(codFact);
        String nombreCentroAtencion= Factura.obtenerCentroAtencionFactura(con,consecutivoFactura,  usuario.getCodigoInstitucionInt()).getNombre();
        mapaFactura.put("nomCentroAtencion",nombreCentroAtencion);
         
        //tomo el pais para determinar que formato debo imprimir
        if(System.getProperty("PAIS").toString().toUpperCase().equals("VENEZUELA"))
        {
        	report=crearEncabezadoStandarVenezuela(report,mapaFactura);
        	report.openReport(nombreArchivo);
        	crearCuerpoReporteVenezuela(con,report,mapaFactura,usuario,mundoImpresion);
        	report.closeReport(tipo);
        }
        else
        {	
	        report=crearEncabezadoVersalles(report, mapaFactura, request);
	        report.openReport(nombreArchivo);
	        crearCuerpoReporteVersalles(con,report,mapaFactura,usuario,mundoImpresion);
	        report.setUsuario(usuario.getLoginUsuario());
	        report.closeReport(tipo);
        }    
	}
	
	/**
	 * 
	 * @param report
	 * @param mapaFactura
	 * @param con 
	 * @return
	 */
	private static PdfReports crearEncabezadoVersalles(PdfReports report,HashMap mapaFactura, HttpServletRequest request)
    {        
		
		InstitucionBasica institucionBasica = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
		String logo="";
		String ubicacionLogo="";
		
		try {
			ParamInstitucionDao institucionDao=null;
			DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
			if (myFactory != null) {
				institucionDao = myFactory.getParamInstitucionDao();
			}
			Connection con=UtilidadBD.abrirConexion();
			ResultSetDecorator rs=institucionDao.consultaInstituciones(con,
					institucionBasica.getCodigoInstitucionBasica(), "0", "0", "0", false);
			//MT6829 se agrega la ubicación del logo
			if (rs.next()) {
			
					logo=rs.getString("logo");
					ubicacionLogo=rs.getString("ubicacion_logo_reportes");
				}
				rs.close();
				
				UtilidadBD.cerrarConexion(con);
			
			}catch (SQLException e) {
				e.printStackTrace();
			}
			String filePath="";//ValoresPorDefecto.getFilePath();
			if(UtilidadTexto.isEmpty(logo))
			{
		        filePath= institucionBasica.getLogoReportes();
			}
			else
			{
				filePath=logo;
			}
			//MT6829 se agrega la ubicación del logo 
			if(UtilidadTexto.isEmpty(ubicacionLogo))
			{
				institucionBasica.getUbicacionLogo();
			}
			else
			{
				institucionBasica.setUbicacionLogo(ubicacionLogo);
			}
	        
	        
        
        logger.info("filepath image->"+filePath);
        logger.info("Ubicacion del Logo en el Reporte: " + institucionBasica.getUbicacionLogo());
        
        iTextBaseTable iSection;
        iSection = new iTextBaseTable();
        
        iSection.useTable( PdfReports.REPORT_HEADER_TABLE, 1, 13 );
        iSection.setTableOffset( PdfReports.REPORT_HEADER_TABLE, 0.0f );
        iSection.setTableBorder( PdfReports.REPORT_HEADER_TABLE,C_BLANCO,0.0f );
        
        iSection.setTableBorderColor(PdfReports.REPORT_HEADER_TABLE, C_BLANCO);
        iSection.setTableBorderWidth(PdfReports.REPORT_HEADER_TABLE, 0.0f);
        
        iSection.setTableCellBorderWidth( PdfReports.REPORT_HEADER_TABLE, 0.0f );
        iSection.setTableCellPadding( PdfReports.REPORT_HEADER_TABLE, 0.0f );
        //MT6829 se le agrega espacio entre celdas
        iSection.setTableSpaceBetweenCells( PdfReports.REPORT_HEADER_TABLE, 2.1f );
        iSection.setTableCellsDefaultColors( PdfReports.REPORT_HEADER_TABLE, C_BLANCO, C_BLANCO);
        iSection.setTableCellsDefaultHAlignment( PdfReports.REPORT_HEADER_TABLE, iTextBaseDocument.ALIGNMENT_CENTER );
        iSection.setTableCellsDefaultVAlignment( PdfReports.REPORT_HEADER_TABLE, iTextBaseDocument.ALIGNMENT_JUSTIFIED_ALL );
        iSection.setTableWidth(PdfReports.REPORT_HEADER_TABLE, 100);


        report.font.setFontAttributes( C_NEGRO, 8, false, false, false );

        
        /*********************SECCION DEL TITULO - LOGO - Y DATOS DE LA INSITUCION*********************/
        HashMap mapDatosInst=new HashMap();
        mapDatosInst=(HashMap)mapaFactura.get("headerDatosInstitucion");
        /**********mapa paciente*********/
        HashMap mapDatosPac=new HashMap();
        mapDatosPac=(HashMap)mapaFactura.get("headerAndFooterDatosPaciente");        

        String prefijoFactura=(mapDatosInst.get("prefijo_fact")+"").equals("null")?"":mapDatosInst.get("prefijo_fact")+"";
        //MT6829 Se reubica los datos de NIT
      //Nit
        String NIT = "" + mapDatosInst.get("descid") + " " + mapDatosInst.get("numeroid") + "";
        	//Info Ubicacion Institucion
        String infoUbicacionInst = "Dir. "+mapDatosInst.get("direccion")+" - Tel. "+mapDatosInst.get("telefono")+" - Ciudad. "+mapDatosInst.get("descciudad");
        	//Encabezado parametrizado
        String encabezado= FormatoImpresionFactura.obtenerEncabezadoPieFactura(mapaFactura.get("codigoFactura")+"", Double.parseDouble(mapaFactura.get("empresaInstitucion")+""))[0];
        
        String cadena=NIT+"  -"+infoUbicacionInst;
         
        if(institucionBasica.getUbicacionLogo().equals(ConstantesIntegridadDominio.acronimoUbicacionIzquierda)) {
            //logo izq
            iSection.setTableCellsColSpan(4);
            iSection.setTableCellsRowSpan(2);
            iSection.addTableImageCellAligned( PdfReports.REPORT_HEADER_TABLE,filePath, 100f,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT, 0,0);
         
            //Razon Social
            report.font.setFontAttributes( C_NEGRO, 10, true, false, false );
            iSection.setTableCellsColSpan(9);
            iSection.setTableCellsRowSpan(1); //2 
            iSection.addTableTextCellAligned(PdfReports.REPORT_HEADER_TABLE, mapDatosInst.get("razonsocial")+" - "+mapaFactura.get("nomCentroAtencion"), report.font, iTextBaseDocument.ALIGNMENT_CENTER, iTextBaseDocument.ALIGNMENT_CENTER, 0,4);
            report.font.setFontAttributes( C_NEGRO, 8, false, false, false );
                  
            //Nit
            
            report.font.setFontAttributes(0x000000, 7, false, false, false);
            iSection.setTableCellsColSpan(9); //5
            iSection.setTableCellsRowSpan(1); //2 
            iSection.addTableTextCellAligned(PdfReports.REPORT_HEADER_TABLE, NIT+"  - "+infoUbicacionInst+"\n"+encabezado, report.font, iTextBaseDocument.ALIGNMENT_CENTER, iTextBaseDocument.ALIGNMENT_CENTER,1,4);
            
          
        }
        else {
        	 //MT6829 Se reubica la ubicación del encabezado DER
        	  //razon social
            report.font.setFontAttributes( C_NEGRO, 10, true, false, false );
            iSection.setTableCellsColSpan(9);
            iSection.setTableCellsRowSpan(1); //2
            iSection.addTableTextCellAligned(PdfReports.REPORT_HEADER_TABLE, mapDatosInst.get("razonsocial")+" - "+mapaFactura.get("nomCentroAtencion"), report.font, iTextBaseDocument.ALIGNMENT_CENTER, iTextBaseDocument.ALIGNMENT_CENTER,0,0);
          //  report.font.setFontAttributes( C_NEGRO, 8, false, false, false );
            
          	//logo
         
            iSection.setTableCellsColSpan(4);
            iSection.setTableCellsRowSpan(2);
            iSection.addTableImageCellAligned(PdfReports.REPORT_HEADER_TABLE, filePath, 100f,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT, 0,9 );
          
            //NIT
           
            report.font.setFontAttributes(0x000000, 7, false, false, false);
            iSection.setTableCellsColSpan(9); //5
            iSection.setTableCellsRowSpan(1); //2 
            iSection.addTableTextCellAligned(PdfReports.REPORT_HEADER_TABLE, NIT+"  - "+infoUbicacionInst+"\n"+encabezado, report.font, iTextBaseDocument.ALIGNMENT_CENTER, iTextBaseDocument.ALIGNMENT_CENTER,1,0);
          
          }          
       
              
        //espacio vacio para posible info de la facture si esta anulada por si el logo va a la derecha 
        iSection.setTableCellsColSpan(13);
        iSection.setTableCellsRowSpan(1);
        if((mapDatosPac.get("estadofacturacion")+"").equals(ConstantesBD.codigoEstadoFacturacionAnulada+"")){
            report.font.setFontAttributes(0x000000, 8, true, false, false);
        	iSection.addTableTextCellAligned(PdfReports.REPORT_HEADER_TABLE,"FACTURA ANULADA "+mapDatosPac.get("fechaanulacion")+" "+mapDatosPac.get("horaanulacion"), report.font, iTextBaseDocument.ALIGNMENT_RIGHT, iTextBaseDocument.ALIGNMENT_RIGHT ,2,0);
        }
        else {
        	iSection.addTableTextCellAligned(PdfReports.REPORT_HEADER_TABLE, "", report.font, iTextBaseDocument.ALIGNMENT_CENTER, iTextBaseDocument.ALIGNMENT_CENTER, 2,0);
        }
        //fin fila ---------------------------------------
        
        
        //report.font.setFontAttributes(0x000000, 8, false, false, false);           
       
        
        /***************************SECCION PARA LOS DATOS DE LA FACTURA***************************/
        report.font.setFontAttributes(C_NEGRO, 8, true, false, false);
        
        iSection.setTableCellsColSpan(13);//4
        iSection.addTableTextCellAligned(PdfReports.REPORT_HEADER_TABLE, "FACTURA DE VENTA: "+prefijoFactura+( mapDatosPac.get( "consecutivo_factura" )+"" ),report.font, iTextBaseDocument.ALIGNMENT_RIGHT, iTextBaseDocument.ALIGNMENT_RIGHT, 3,0);
        report.font.setFontAttributes(C_NEGRO, 8, false, false, false);
            
        /***********************SECCION PARA LOS DATOS DEL PACIENTE***********************/
       
        report.font.setFontAttributes(C_NEGRO, 8, false, false, false);
        //fila        
        iSection.setTableCellsColSpan(7);
        iSection.addTableTextCellAligned(PdfReports.REPORT_HEADER_TABLE, "Responsable: "+( mapDatosPac.get( "nombretercero" )+"" ), report.font, iTextBaseDocument.ALIGNMENT_LEFT, iTextBaseDocument.ALIGNMENT_LEFT);
        iSection.setTableCellsColSpan(3);
        iSection.addTableTextCellAligned(PdfReports.REPORT_HEADER_TABLE, "Número ID: "+( mapDatosPac.get( "numeroid_tercero" )+"" ), report.font, iTextBaseDocument.ALIGNMENT_LEFT, iTextBaseDocument.ALIGNMENT_LEFT );
        iSection.setTableCellsColSpan(3);        
        String nroAutorizacion="";
        if(!(mapaFactura.get( "nroAutorizacion" )+"").equals("null"))
        	nroAutorizacion=mapaFactura.get( "nroAutorizacion" )+"";
        iSection.addTableTextCellAligned(PdfReports.REPORT_HEADER_TABLE, "Autorización Nro.: "+nroAutorizacion,report.font, iTextBaseDocument.ALIGNMENT_LEFT, iTextBaseDocument.ALIGNMENT_LEFT );

        //fila  
        iSection.setTableCellsColSpan(4);
        iSection.addTableTextCellAligned(PdfReports.REPORT_HEADER_TABLE, "Dirección: "+( mapDatosPac.get( "dirtercero" )+"" ), report.font, iTextBaseDocument.ALIGNMENT_LEFT, iTextBaseDocument.ALIGNMENT_LEFT );
        iSection.setTableCellsColSpan(3);
        iSection.addTableTextCellAligned(PdfReports.REPORT_HEADER_TABLE, "Teléfono: "+( mapDatosPac.get( "telefonotercero" )+"" ).trim(), report.font, iTextBaseDocument.ALIGNMENT_LEFT, iTextBaseDocument.ALIGNMENT_LEFT );        
        iSection.setTableCellsColSpan(3);
        iSection.addTableTextCellAligned(PdfReports.REPORT_HEADER_TABLE, "Fecha de Fact.: "+UtilidadFecha.conversionFormatoFechaAAp(( mapDatosPac.get( "fecha_factura" )+"" )), report.font, iTextBaseDocument.ALIGNMENT_LEFT, iTextBaseDocument.ALIGNMENT_LEFT );
        iSection.setTableCellsColSpan(3);
        iSection.addTableTextCellAligned(PdfReports.REPORT_HEADER_TABLE, "Fecha Vencimiento: "+UtilidadFecha.incrementarDiasAFecha(UtilidadFecha.conversionFormatoFechaAAp( mapDatosPac.get( "fecha_factura" )+"" ),Utilidades.convertirAEntero(mapDatosPac.get( "diasvencimiento" )+"",true ),false), report.font, iTextBaseDocument.ALIGNMENT_LEFT, iTextBaseDocument.ALIGNMENT_LEFT );        
        
       
        report.usarSeccion( PdfReports.REPORT_HEADER_SECTION, iSection );        
        return report;         
    }
	
	/**
	 * 
	 * @param con
	 * @param report
	 * @param mapaFactura
	 * @param usuario
	 * @param mundoImpresion
	 */
    private static void crearCuerpoReporteVersalles(Connection con, PdfReports report, HashMap mapaFactura, UsuarioBasico usuario, ImpresionFactura mundoImpresion) 
    {
    	
    	 HashMap mapDatosPac=new HashMap();
         mapDatosPac=(HashMap)mapaFactura.get("headerAndFooterDatosPaciente");   
    	
    	
    	report.createSection("SECCION_DATOS_PACIENTE","TABLA_DATOS_PACIENTE",2,4,4);     
    	report.getSectionReference("SECCION_DATOS_PACIENTE").setTableBorder("TABLA_DATOS_PACIENTE", 0x000000, 1.0f);
        report.getSectionReference("SECCION_DATOS_PACIENTE").getTableReference("TABLA_DATOS_PACIENTE").setPadding(0.0f);
        report.getSectionReference("SECCION_DATOS_PACIENTE").getTableReference("TABLA_DATOS_PACIENTE").setSpacing(1.0f);
        report.getSectionReference("SECCION_DATOS_PACIENTE").setTableCellBorderWidth("TABLA_DATOS_PACIENTE", 0.0f);
        report.getSectionReference("SECCION_DATOS_PACIENTE").setTableCellsDefaultColors("TABLA_DATOS_PACIENTE", 0xFFFFFF, 0x000000);
    	
        
        //fila
        report.font.setFontSizeAndAttributes(8,false,false,false);
        report.getSectionReference("SECCION_DATOS_PACIENTE").setTableCellSpacing("TABLA_DATOS_PACIENTE", 0.0F);//cam
        report.getSectionReference("SECCION_DATOS_PACIENTE").setTableCellsRowSpan(1);
        report.getSectionReference("SECCION_DATOS_PACIENTE").setTableCellsColSpan(2);
        report.getSectionReference("SECCION_DATOS_PACIENTE").setTableCellPadding("TABLA_DATOS_PACIENTE", 1.5f);//cam
        report.getSectionReference("SECCION_DATOS_PACIENTE").addTableTextCellAligned("TABLA_DATOS_PACIENTE","PACIENTE: "+( mapDatosPac.get( "nombrepersona" )+"" ), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
        report.getSectionReference("SECCION_DATOS_PACIENTE").setTableCellsColSpan(1);
        report.getSectionReference("SECCION_DATOS_PACIENTE").addTableTextCellAligned("TABLA_DATOS_PACIENTE","IDENTIFICACIÓN: "+ mapDatosPac.get( "tipoid" )+": " +( mapDatosPac.get( "numeroid_paciente" )+"" ), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
        report.getSectionReference("SECCION_DATOS_PACIENTE").addTableTextCellAligned("TABLA_DATOS_PACIENTE",  "No INGRESO: "+( mapDatosPac.get( "consecutivoingreso" )+"" ), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
        
        //FILA
        report.getSectionReference("SECCION_DATOS_PACIENTE").setTableCellsColSpan(1);
        report.getSectionReference("SECCION_DATOS_PACIENTE").setTableCellSpacing("TABLA_DATOS_PACIENTE", 0.0F);//cam
        report.getSectionReference("SECCION_DATOS_PACIENTE").addTableTextCellAligned("TABLA_DATOS_PACIENTE", "DIRECCIÓN: "+( mapDatosPac.get( "direccionpersona" )+"" ), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
        report.getSectionReference("SECCION_DATOS_PACIENTE").addTableTextCellAligned("TABLA_DATOS_PACIENTE",  "TELÉFONO: "+( mapDatosPac.get( "telefonopersona" )+"" ).trim(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
        report.getSectionReference("SECCION_DATOS_PACIENTE").addTableTextCellAligned("TABLA_DATOS_PACIENTE",  "FECHA INGRESO: "+mapDatosPac.get( "fechaingreso" ), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
        report.getSectionReference("SECCION_DATOS_PACIENTE").addTableTextCellAligned("TABLA_DATOS_PACIENTE",  "FECHA EGRESO: "+mapDatosPac.get( "fechaegreso" ), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
        
    	
        report.addSectionToDocument("SECCION_DATOS_PACIENTE");
    	
    	
        HashMap mapDatosSer=new HashMap();
        mapDatosSer=(HashMap)mapaFactura.get("cuerpoDatosServicios");
        HashMap mapDatosArt=new HashMap();
        mapDatosArt=(HashMap)mapaFactura.get("cuerpoDatosArticulos");
        
        if(!mapDatosSer.isEmpty()||!mapDatosArt.isEmpty())
        {
            report.createSection("SECCION_SERVICIOS","TABLA_SERVICIOS",1,7,10);
            report.getSectionReference("SECCION_SERVICIOS").setTableBorder("TABLA_SERVICIOS", 0xFFFFFF, 1.0f);
            report.getSectionReference("SECCION_SERVICIOS").getTableReference("TABLA_SERVICIOS").setPadding(0.0f);
            report.getSectionReference("SECCION_SERVICIOS").getTableReference("TABLA_SERVICIOS").setSpacing(1.0f);
            report.getSectionReference("SECCION_SERVICIOS").setTableCellBorderWidth("TABLA_SERVICIOS", 0.0f);
            report.getSectionReference("SECCION_SERVICIOS").setTableCellsDefaultColors("TABLA_SERVICIOS", 0xFFFFFF, 0x000000);
            //fila 
            report.font.setFontSizeAndAttributes(8,true,false,false);
            report.getSectionReference("SECCION_SERVICIOS").setTableCellPadding("TABLA_SERVICIOS", 1.5f);//cam
            report.getSectionReference("SECCION_SERVICIOS").setTableCellSpacing("TABLA_SERVICIOS", 0.0F);//cam
            report.getSectionReference("SECCION_SERVICIOS").setTableCellsColSpan(1);
            report.getSectionReference("SECCION_SERVICIOS").addTableTextCellAligned("TABLA_SERVICIOS", "AUTORIZ.", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
            report.getSectionReference("SECCION_SERVICIOS").addTableTextCellAligned("TABLA_SERVICIOS", "CÓDIGO", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
            report.getSectionReference("SECCION_SERVICIOS").addTableTextCellAligned("TABLA_SERVICIOS", "DESCRIPCIÓN", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
            report.getSectionReference("SECCION_SERVICIOS").addTableTextCellAligned("TABLA_SERVICIOS", "DETALLE PROC.", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
            report.getSectionReference("SECCION_SERVICIOS").addTableTextCellAligned("TABLA_SERVICIOS", "CANTIDAD", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_CENTER);
            report.getSectionReference("SECCION_SERVICIOS").addTableTextCellAligned("TABLA_SERVICIOS", "Vr. UNIT.", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
            report.getSectionReference("SECCION_SERVICIOS").addTableTextCellAligned("TABLA_SERVICIOS", "Vr. TOTAL", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
            //fila 
            if(!mapDatosSer.isEmpty())
            {
            	for(int k=0;k<Integer.parseInt(mapDatosSer.get("numRegistros")+"");k++)
                {
                	report.font.setFontSizeAndAttributes(8,false,false,false);
                	report.getSectionReference("SECCION_SERVICIOS").setTableCellPadding("TABLA_SERVICIOS", 1.5f);//cam
                	report.getSectionReference("SECCION_SERVICIOS").setTableCellsDefaultColors("TABLA_SERVICIOS", 0xFFFFFF, 0xFFFFFF);//CAM
	                report.getSectionReference("SECCION_SERVICIOS").setTableCellsColSpan(1);
	                report.getSectionReference("SECCION_SERVICIOS").addTableTextCellAligned("TABLA_SERVICIOS", mapDatosSer.get("numeroautorizacion_"+k)+"", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
	                report.getSectionReference("SECCION_SERVICIOS").addTableTextCellAligned("TABLA_SERVICIOS",mapDatosSer.get("codigopropeitario_"+k)+"" , report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
                    report.getSectionReference("SECCION_SERVICIOS").setTableCellsColSpan(2);
	                report.getSectionReference("SECCION_SERVICIOS").addTableTextCellAligned("TABLA_SERVICIOS",mapDatosSer.get("procedimiento_"+k)+"", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
	                if((mapDatosSer.get("escirugia_"+k)+"").equals("false"))
	                {
	                    report.getSectionReference("SECCION_SERVICIOS").setTableCellsColSpan(1);
	                    report.getSectionReference("SECCION_SERVICIOS").addTableTextCellAligned("TABLA_SERVICIOS", mapDatosSer.get("cantidad_"+k)+"", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_CENTER);
	                    report.getSectionReference("SECCION_SERVICIOS").addTableTextCellAligned("TABLA_SERVICIOS", UtilidadTexto.formatearValores(mapDatosSer.get("valorunitario_"+k)+""), report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_RIGHT);
	                    report.getSectionReference("SECCION_SERVICIOS").addTableTextCellAligned("TABLA_SERVICIOS", UtilidadTexto.formatearValores(mapDatosSer.get("valortotal_"+k)+""), report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_RIGHT);
	                }
	                else 
	                {
	                    report.getSectionReference("SECCION_SERVICIOS").setTableCellsColSpan(1);
	                    report.getSectionReference("SECCION_SERVICIOS").addTableTextCellAligned("TABLA_SERVICIOS", "", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_CENTER);
	                    report.getSectionReference("SECCION_SERVICIOS").addTableTextCellAligned("TABLA_SERVICIOS", "", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_CENTER);
	                    report.getSectionReference("SECCION_SERVICIOS").addTableTextCellAligned("TABLA_SERVICIOS", "", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_CENTER);
	                }
	                if(!(mapDatosSer.get("escirugia_"+k)+"").equals("false"))
	                {
	                    //fila
	                    HashMap mapDatosDetSer=new HashMap();
	                    mapDatosDetSer=(HashMap)mapDatosSer.get("datosDetServicio_"+k);
	                    if(!mapDatosDetSer.isEmpty())
	                    {
	                        for(int j=0;j<Integer.parseInt(mapDatosDetSer.get("numRegistros")+"");j++)
	                        {
	                        	report.font.setFontSizeAndAttributes(6,false,false,true);
	        	                logger.info("\n\n\nservicios cirugiia--> \n"+mapDatosDetSer+"\n\n\n");
	        	                report.getSectionReference("SECCION_SERVICIOS").setTableCellsColSpan(1);
	        	                report.getSectionReference("SECCION_SERVICIOS").setTableCellPadding("TABLA_SERVICIOS", 1.5f);//cam
	        	                report.getSectionReference("SECCION_SERVICIOS").addTableTextCellAligned("TABLA_SERVICIOS", "", report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_RIGHT);
	        	                report.getSectionReference("SECCION_SERVICIOS").addTableTextCellAligned("TABLA_SERVICIOS", "", report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_RIGHT);
	        	                report.getSectionReference("SECCION_SERVICIOS").addTableTextCellAligned("TABLA_SERVICIOS", "", report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_RIGHT);
	        	                report.getSectionReference("SECCION_SERVICIOS").addTableTextCellAligned("TABLA_SERVICIOS", mapDatosDetSer.get("nombreasocio_"+j)+"", report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_RIGHT);
	                        	report.font.setFontSizeAndAttributes(8,false,false,false);
	        	                report.getSectionReference("SECCION_SERVICIOS").addTableTextCellAligned("TABLA_SERVICIOS", mapDatosDetSer.get("cantidad_"+j)+"", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_CENTER);
	        	                logger.info(mapDatosDetSer.get("valorunitario_"+j)+"\nj-->"+j);
	        	                logger.info(mapDatosDetSer.get("valorunitario_"+j)==null);
	        	                logger.info(Double.parseDouble(mapDatosDetSer.get("valorunitario_"+j)+""));
	        	                report.getSectionReference("SECCION_SERVICIOS").addTableTextCellAligned("TABLA_SERVICIOS", UtilidadTexto.formatearValores(Double.parseDouble(mapDatosDetSer.get("valorunitario_"+j)+"")), report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_RIGHT);
	        	                report.getSectionReference("SECCION_SERVICIOS").addTableTextCellAligned("TABLA_SERVICIOS", UtilidadTexto.formatearValores(Double.parseDouble(mapDatosDetSer.get("valortotal_"+j)+"")), report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_RIGHT);
	                        }
	                    }
	                }
                }
            } 
            if(!mapDatosArt.isEmpty())
            {    
            	report.font.setFontSizeAndAttributes(8,false,false,false);
            	
	            for(int k=0;k<Integer.parseInt(mapDatosArt.get("numRegistros")+"");k++)
	            {
	            	report.getSectionReference("SECCION_SERVICIOS").setTableCellPadding("TABLA_SERVICIOS", 1.5f);//cam
	            	report.getSectionReference("SECCION_SERVICIOS").setTableCellsColSpan(1);
	                report.getSectionReference("SECCION_SERVICIOS").addTableTextCellAligned("TABLA_SERVICIOS",mapDatosArt.get("numeroautorizacion_"+k)+"" , report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
	                report.getSectionReference("SECCION_SERVICIOS").addTableTextCellAligned("TABLA_SERVICIOS",mapDatosArt.get("codigoarticulo_"+k)+"" , report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
	                report.getSectionReference("SECCION_SERVICIOS").setTableCellsColSpan(2);
	                report.getSectionReference("SECCION_SERVICIOS").addTableTextCellAligned("TABLA_SERVICIOS",mapDatosArt.get("descarticulo_"+k)+"", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
	                report.getSectionReference("SECCION_SERVICIOS").setTableCellsColSpan(1);
	                report.getSectionReference("SECCION_SERVICIOS").addTableTextCellAligned("TABLA_SERVICIOS", mapDatosArt.get("cantidad_"+k)+"", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_CENTER);
	                report.getSectionReference("SECCION_SERVICIOS").addTableTextCellAligned("TABLA_SERVICIOS", UtilidadTexto.formatearValores(mapDatosArt.get("valorunitario_"+k)+""), report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_RIGHT);
	                report.getSectionReference("SECCION_SERVICIOS").addTableTextCellAligned("TABLA_SERVICIOS", UtilidadTexto.formatearValores(mapDatosArt.get("valortotal_"+k)+""), report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_RIGHT);
	            }
        	}
             String textoTotales="Total de Cargos: "+UtilidadTexto.formatearValores(mapDatosPac.get("valortotalfactura")+"")+"   Total del Paciente: "+UtilidadTexto.formatearValores(mapDatosPac.get("valorbrutopaciente")+"")+"   Total Abonos del Paciente: "+UtilidadTexto.formatearValores(mapDatosPac.get("valorabonos")+"")+"   Total Neto a Cargo del Paciente: "+UtilidadTexto.formatearValores(mapDatosPac.get("valornetopaciente")+"" );
             report.getSectionReference("SECCION_SERVICIOS").setTableCellsColSpan(7);
            report.getSectionReference("SECCION_SERVICIOS").addTableTextCellAligned("TABLA_SERVICIOS",textoTotales , report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_CENTER);
            
            
            float widths[]={(float) 1,(float) 1,(float)3,(float) 2,(float) 1,(float) 1,(float) 1};
    		try 
    		{
    			report.getSectionReference("SECCION_SERVICIOS").getTableReference("TABLA_SERVICIOS").setWidths(widths);
    		}
    		catch (BadElementException e) 
    		{
    			e.printStackTrace();
    		}    		
            report.addSectionToDocument("SECCION_SERVICIOS");          
        }
  
       
        //***********Tarea 75069**************//
        HashMap<String, Object> mapaAux = new HashMap<String, Object>();
        mapaAux = mundoImpresion.consultarValorLetrasValor(con, mapDatosPac.get("convenio")+"");
        String letrasCargos="TOTAL A PAGAR: "+UtilidadTexto.formatearValores(mapDatosPac.get("valortotalfactura")+"" )+"        VALOR EN LETRAS: "+UtilidadN2T.convertirLetras(UtilidadTexto.formatearValores(mapDatosPac.get("valortotalfactura")+"" ).replaceAll(",", ""),ConstantesBD.cadenaUnidades,ConstantesBD.cadenaUnidadesDecimales)+" MCTE.";
        String letrasConvenio="TOTAL A PAGAR: "+UtilidadTexto.formatearValores(mapDatosPac.get("valorconvenio")+"" )+"        VALOR EN LETRAS: "+UtilidadN2T.convertirLetras(UtilidadTexto.formatearValores(mapDatosPac.get("valorconvenio")+"" ).replaceAll(",", ""),ConstantesBD.cadenaUnidades,ConstantesBD.cadenaUnidadesDecimales)+" MCTE.";
        
        //fila
        report.createSection("SECCION_VALORES_LETRAS_PARAMETRO","TABLA_VALORES_LETRAS_PARAMETRO",1,1,3);
        report.getSectionReference("SECCION_VALORES_LETRAS_PARAMETRO").setTableBorder("TABLA_VALORES_LETRAS_PARAMETRO", 0xFFFFFF, 1.0f);
        report.getSectionReference("SECCION_VALORES_LETRAS_PARAMETRO").getTableReference("TABLA_VALORES_LETRAS_PARAMETRO").setPadding(0.0f);
        report.getSectionReference("SECCION_VALORES_LETRAS_PARAMETRO").getTableReference("TABLA_VALORES_LETRAS_PARAMETRO").setSpacing(1.0f);
        report.getSectionReference("SECCION_VALORES_LETRAS_PARAMETRO").setTableCellBorderWidth("TABLA_VALORES_LETRAS_PARAMETRO", 0.0f);
        report.getSectionReference("SECCION_VALORES_LETRAS_PARAMETRO").setTableCellsDefaultColors("TABLA_VALORES_LETRAS_PARAMETRO", 0xFFFFFF, 0x000000);
        report.font.setFontSizeAndAttributes(8,true,false,false);
        report.getSectionReference("SECCION_VALORES_LETRAS_PARAMETRO").setTableCellPadding("TABLA_VALORES_LETRAS_PARAMETRO", 1.5f);//cam
        report.getSectionReference("SECCION_VALORES_LETRAS_PARAMETRO").setTableCellsColSpan(1);
                
        if((mapaAux.get("parametro")+"").equals(ConstantesIntegridadDominio.acronimoValorLetrasFacturaCargo))
        	report.getSectionReference("SECCION_VALORES_LETRAS_PARAMETRO").addTableTextCellAligned("TABLA_VALORES_LETRAS_PARAMETRO",letrasCargos, report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
        else
        {
        	if((mapaAux.get("parametro")+"").equals(ConstantesIntegridadDominio.acronimoValorLetrasFacturaConvenio))
            	report.getSectionReference("SECCION_VALORES_LETRAS_PARAMETRO").addTableTextCellAligned("TABLA_VALORES_LETRAS_PARAMETRO",letrasConvenio, report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
        }
        report.addSectionToDocument("SECCION_VALORES_LETRAS_PARAMETRO");
        //***********Fin Tarea***************//
        
        //fila FIXME se elimina seccion de Consulta externa 
 /*       int viaIng=Utilidades.obtenerViaIngresoFactura(con,mapaFactura.get("codigoFactura")+"");
        if(viaIng==ConstantesBD.codigoViaIngresoConsultaExterna)
        {
            report.createSection("SECCION_CITAS","TABLA_CITAS",4,6,10);        
            report.getSectionReference("SECCION_CITAS").setTableBorder("TABLA_CITAS", 0x000000, 0.0f);
            report.getSectionReference("SECCION_CITAS").getTableReference("TABLA_CITAS").setPadding(0.0f);
            report.getSectionReference("SECCION_CITAS").getTableReference("TABLA_CITAS").setSpacing(1.0f);
            report.getSectionReference("SECCION_CITAS").setTableCellBorderWidth("TABLA_CITAS", 0.0f);
            report.getSectionReference("SECCION_CITAS").setTableCellsDefaultColors("TABLA_CITAS", 0xFFFFFF, 0x000000);
            //fila
            report.font.setFontSizeAndAttributes(8,true,false,false);
            report.getSectionReference("SECCION_CITAS").setTableCellsRowSpan(1);
            report.getSectionReference("SECCION_CITAS").setTableCellsColSpan(1);
            report.getSectionReference("SECCION_CITAS").addTableTextCellAligned("TABLA_CITAS", "Unidad de Consulta", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_CENTER);
            report.getSectionReference("SECCION_CITAS").addTableTextCellAligned("TABLA_CITAS", "Profesional de la Salud", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_CENTER);
            report.getSectionReference("SECCION_CITAS").addTableTextCellAligned("TABLA_CITAS", "Fecha Cita", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_CENTER);
            report.getSectionReference("SECCION_CITAS").addTableTextCellAligned("TABLA_CITAS", "Hora Cita", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_CENTER);
            report.getSectionReference("SECCION_CITAS").addTableTextCellAligned("TABLA_CITAS", "Consultorio", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_CENTER);
            report.getSectionReference("SECCION_CITAS").addTableTextCellAligned("TABLA_CITAS", "No. Cita.", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_CENTER);
            String codSubCuenta=Utilidades.obtenerCuentaFactura(con,mapaFactura.get("codigoFactura")+"");
            HashMap citas=mundoImpresion.consultarInfoCitaDadaCuenta(con,codSubCuenta);
            for(int ci=0;ci<Integer.parseInt(citas.get("numRegistros")+"");ci++)
            {
            	 report.font.setFontSizeAndAttributes(8,false,false,false);
                 report.getSectionReference("SECCION_CITAS").addTableTextCellAligned("TABLA_CITAS", citas.get("unidadconsulta_"+ci)+"", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_CENTER);
                 report.getSectionReference("SECCION_CITAS").addTableTextCellAligned("TABLA_CITAS", citas.get("profesional_"+ci)+"", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_CENTER);
                 report.getSectionReference("SECCION_CITAS").addTableTextCellAligned("TABLA_CITAS", citas.get("fecha_"+ci)+"", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_CENTER);
                 report.getSectionReference("SECCION_CITAS").addTableTextCellAligned("TABLA_CITAS", citas.get("hora_"+ci)+"", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_CENTER);
                 report.getSectionReference("SECCION_CITAS").addTableTextCellAligned("TABLA_CITAS", citas.get("consultorio_"+ci)+"", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_CENTER);
                 report.getSectionReference("SECCION_CITAS").addTableTextCellAligned("TABLA_CITAS", citas.get("cita_"+ci)+"", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_CENTER);
            }
            report.addSectionToDocument("SECCION_CITAS");
        }*/
        
        //fila
        report.createSection("SECCION_FOOTER","TABLA_FOOTER",4,2,10);        
        report.getSectionReference("SECCION_FOOTER").setTableBorder("TABLA_FOOTER", 0xFFFFFF, 0.0f);
        report.getSectionReference("SECCION_FOOTER").getTableReference("TABLA_FOOTER").setPadding(0.0f);
        report.getSectionReference("SECCION_FOOTER").getTableReference("TABLA_FOOTER").setSpacing(0.0f);
        report.getSectionReference("SECCION_FOOTER").setTableCellBorderWidth("TABLA_FOOTER", 0.0f);
        report.getSectionReference("SECCION_FOOTER").setTableCellsDefaultColors("TABLA_FOOTER", 0xFFFFFF, 0x000000);
        //fila
        report.font.setFontSizeAndAttributes(8,true,false,false);
        HashMap mapDatosInst=new HashMap();
        mapDatosInst=(HashMap)mapaFactura.get("headerDatosInstitucion");
        report.getSectionReference("SECCION_FOOTER").setTableCellsRowSpan(1);
        report.getSectionReference("SECCION_FOOTER").setTableCellsColSpan(2);
        report.getSectionReference("SECCION_FOOTER").addTableTextCellAligned("TABLA_FOOTER", "", report.font,iTextBaseDocument.ALIGNMENT_JUSTIFIED,iTextBaseDocument.ALIGNMENT_JUSTIFIED);
        report.getSectionReference("SECCION_FOOTER").addTableTextCellAligned("TABLA_FOOTER", "", report.font,iTextBaseDocument.ALIGNMENT_JUSTIFIED,iTextBaseDocument.ALIGNMENT_JUSTIFIED);
        report.getSectionReference("SECCION_FOOTER").setTableCellsRowSpan(1);
        report.getSectionReference("SECCION_FOOTER").setTableCellsColSpan(1);
        report.getSectionReference("SECCION_FOOTER").addTableTextCellAligned("TABLA_FOOTER", "____________________________________________", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_CENTER);
        report.getSectionReference("SECCION_FOOTER").addTableTextCellAligned("TABLA_FOOTER", "_________________________________", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_CENTER);
        report.getSectionReference("SECCION_FOOTER").addTableTextCellAligned("TABLA_FOOTER", "FIRMA FACTURADOR ("+mapDatosPac.get("usuarioelabora")+").", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_CENTER);
        report.getSectionReference("SECCION_FOOTER").addTableTextCellAligned("TABLA_FOOTER", "FIRMA DEL PACIENTE ", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_CENTER);
        /*report.getSectionReference("SECCION_FOOTER").setTableCellsRowSpan(1);
        report.getSectionReference("SECCION_FOOTER").setTableCellsColSpan(2);
        //report.getSectionReference("SECCION_FOOTER").addTableTextCellAligned("TABLA_FOOTER", "______________________________________________________________________________________________________________________________________", report.font,iTextBaseDocument.ALIGNMENT_JUSTIFIED,iTextBaseDocument.ALIGNMENT_JUSTIFIED);
        
        report.font.setFontSizeAndAttributes(6,true,false,false);
        //report.getSectionReference("SECCION_FOOTER").addTableTextCellAligned("TABLA_FOOTER", "Usuario: "+mapDatosPac.get("usuarioelabora"), report.font,iTextBaseDocument.ALIGNMENT_JUSTIFIED,iTextBaseDocument.ALIGNMENT_JUSTIFIED);
        
        String pie= FormatoImpresionFactura.obtenerEncabezadoPieFactura(mapaFactura.get("codigoFactura")+"", Double.parseDouble(mapaFactura.get("empresaInstitucion")+""))[1];
        report.getSectionReference("SECCION_FOOTER").addTableTextCellAligned("TABLA_FOOTER", pie, report.font,iTextBaseDocument.ALIGNMENT_JUSTIFIED,iTextBaseDocument.ALIGNMENT_JUSTIFIED);
        //report.getSectionReference("SECCION_FOOTER").addTableTextCellAligned("TABLA_FOOTER", "______________________________________________________________________________________________________________________________________", report.font,iTextBaseDocument.ALIGNMENT_JUSTIFIED,iTextBaseDocument.ALIGNMENT_JUSTIFIED);
        */
        report.addSectionToDocument("SECCION_FOOTER");
        
        
        //FIXME Tarea 181534 tamaño fuente
        /*********************************/
        report.createSection("SECCION_PIE_PAGINA","SECCION_PIE_PAGINA",1,1,1);        
        report.getSectionReference("SECCION_PIE_PAGINA").setTableBorder("SECCION_PIE_PAGINA", 0x000000, 1.0f);
        report.getSectionReference("SECCION_PIE_PAGINA").getTableReference("SECCION_PIE_PAGINA").setPadding(0.0f);
        report.getSectionReference("SECCION_PIE_PAGINA").getTableReference("SECCION_PIE_PAGINA").setSpacing(0.0f);
        report.getSectionReference("SECCION_PIE_PAGINA").setTableCellBorderWidth("SECCION_PIE_PAGINA", 0.0f);
        report.getSectionReference("SECCION_PIE_PAGINA").setTableCellsDefaultColors("SECCION_PIE_PAGINA", 0xFFFFFF, 0x000000);
        
        report.getSectionReference("SECCION_PIE_PAGINA").setTableCellsRowSpan(1);
        report.getSectionReference("SECCION_PIE_PAGINA").setTableCellsColSpan(1);
        report.font.setFontSizeAndAttributes(6,true,false,false);
        report.getSectionReference("SECCION_PIE_PAGINA").setTableCellPadding("SECCION_PIE_PAGINA", 1.5f);//cam
        
        String pie= FormatoImpresionFactura.obtenerEncabezadoPieFactura(mapaFactura.get("codigoFactura")+"", Double.parseDouble(mapaFactura.get("empresaInstitucion")+""))[1];
        report.getSectionReference("SECCION_PIE_PAGINA").addTableTextCellAligned("SECCION_PIE_PAGINA", pie, report.font,iTextBaseDocument.ALIGNMENT_JUSTIFIED,iTextBaseDocument.ALIGNMENT_JUSTIFIED);
        report.addSectionToDocument("SECCION_PIE_PAGINA");
        
    }
	
	private static void generarReporteEstandar(Connection con, UsuarioBasico usuario, HashMap facturasImprimir, PdfReports report, String nombreArchivo, int index, HttpServletRequest request, String tipoPiePagina) 
	{
		report = new PdfReports(); 
		
		report.setUsuario(usuario.getLoginUsuario());
		
        HashMap mapaFactura=new HashMap();
        HashMap mapaDatosInstitucion=new HashMap();                    
        HashMap mapaDatosPaciente=new HashMap();
        HashMap mapaDatosServicios=new HashMap();
        HashMap mapaDatosDetServicios;
        HashMap mapaDatosArticulos=new HashMap();
        
        HashMap mapaDatosMedicamentos= new HashMap();
        HashMap mapaDatosInsumos= new HashMap();
        
        ImpresionFactura mundoImpresion=new ImpresionFactura(); 
        mapaDatosInstitucion=mundoImpresion.consultarSeccionInstitucionFormatoEstatico(con,usuario.getCodigoInstitucionInt(), facturasImprimir.get("codigoFactura_"+index)+"", Double.parseDouble(facturasImprimir.get("empresaInstitucion_"+index)+""));
        logger.info("datos institucion->"+mapaDatosInstitucion+"\n");
        mapaDatosPaciente=mundoImpresion.consultarSeccionPacienteFormatoEstatico(con,facturasImprimir.get("codigoFactura_"+index)+"");
        logger.info("datos paciente->"+mapaDatosPaciente+"\n");
        mapaDatosServicios=mundoImpresion.consultarSeccionServiciosFormatoEstatico(con,facturasImprimir.get("codigoFactura_"+index)+"");
        logger.info("datos servicios->"+mapaDatosServicios+"\n");
        if(!mapaDatosServicios.isEmpty())
        {
            for(int k=0;k<Integer.parseInt(mapaDatosServicios.get("numRegistros")+"");k++)
            {
                mapaDatosDetServicios=new HashMap();
                if((mapaDatosServicios.get("escirugia_"+k)+"").equals("true"))
                {
                    mapaDatosDetServicios=mundoImpresion.consultarSeccionDetServiciosFormatoEstatico(con,(mapaDatosServicios.get("codigodetallefactura_"+k)+""));
                    logger.info("datos detalle servicio->"+mapaDatosDetServicios+"\n");                               
                }
                mapaDatosServicios.put("datosDetServicio_"+k,mapaDatosDetServicios);
            }
        }         
        
        //tomo el pais para determinar que formato debo imprimir
        if(System.getProperty("PAIS").toString().toUpperCase().equals("VENEZUELA"))
        {
        	mapaDatosInsumos=mundoImpresion.consultarSeccionArticulosFormatoEstatico(con,facturasImprimir.get("codigoFactura_"+index)+"", ConstantesBD.acronimoSi);
        	mapaDatosMedicamentos=mundoImpresion.consultarSeccionArticulosFormatoEstatico(con,facturasImprimir.get("codigoFactura_"+index)+"", ConstantesBD.acronimoNo);
        	
        	mapaFactura.put("cuerpoDatosMedicamentos",mapaDatosMedicamentos);
        	mapaFactura.put("cuerpoDatosInsumos", mapaDatosInsumos);
        }
        else
        {
        	//NO SE LE ENVIA EL FILTRO DE MEDICAMENTOS E INSUMOS, TODO SE MUESTRA EN UNO SOLO
        	mapaDatosArticulos=mundoImpresion.consultarSeccionArticulosFormatoEstatico(con,facturasImprimir.get("codigoFactura_"+index)+"", "");
        	mapaFactura.put("cuerpoDatosArticulos",mapaDatosArticulos);
        	logger.info("datos articulos->"+mapaDatosArticulos+"\n");
        }
        
        mapaFactura.put("headerDatosInstitucion",mapaDatosInstitucion);
        mapaFactura.put("headerAndFooterDatosPaciente",mapaDatosPaciente);
        mapaFactura.put("cuerpoDatosServicios",mapaDatosServicios);
        
        // Modificado por Tarea 138393
        //mapaFactura.put("nroAutorizacion", facturasImprimir.get("nroAutorizacion_"+index)+"");
        mapaFactura.put("nroAutorizacion", mapaDatosPaciente.get("numeroautorizacion").toString());
        
        mapaFactura.put("codigoFactura",facturasImprimir.get("codigoFactura_"+index)+"");
        mapaFactura.put("empresaInstitucion", facturasImprimir.get("empresaInstitucion_"+index)+"");
        
        int codFact=Integer.parseInt(facturasImprimir.get("codigoFactura_"+index)+"");
        String consecutivoFactura=Utilidades.obtenerConsecutivoFactura(codFact);
        String nombreCentroAtencion= Factura.obtenerCentroAtencionFactura(con,consecutivoFactura,  usuario.getCodigoInstitucionInt()).getNombre();
        mapaFactura.put("nomCentroAtencion",nombreCentroAtencion);
        
        //tomo el pais para determinar que formato debo imprimir
        if(System.getProperty("PAIS").toString().toUpperCase().equals("VENEZUELA"))
        {
        	report=crearEncabezadoStandarVenezuela(report,mapaFactura);
        	report.openReport(nombreArchivo);
        	crearCuerpoReporteVenezuela(con,report,mapaFactura,usuario,mundoImpresion);
        	report.closeReport( );
        }
        else
        {	
	        report=crearEncabezadoStandar(report, mapaFactura, request);
	        report.openReport(nombreArchivo);
	        crearCuerpoReporte(con,report,mapaFactura,usuario,mundoImpresion,tipoPiePagina);                    
	        report.closeReport( );
        }    
	}

	/**
	 * 
	 * @param report
	 * @param mapaFactura
	 * @param con 
	 * @return
	 */
	private static PdfReports crearEncabezadoStandar(PdfReports report,HashMap mapaFactura, HttpServletRequest request)
    {        
		logger.info("\n entro a  crearEncabezadoStandar");
        String filePath="";//ValoresPorDefecto.getFilePath();
        
        InstitucionBasica institucionBasica = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
        
        filePath= institucionBasica.getLogoReportes();
        /**********************************************************
         * modificado por tarea 10867
        if(System.getProperty("file.separator").equals("/")) {
        	//logger.info("\n 1 filePath -->"+filePath);
           //filePath=filePath.substring(0, filePath.lastIndexOf("/", (filePath.length()-2)));       
           //filePath+="/imagenes/logo-grey.gif";
           filePath= institucionBasica.getLogoReportes();
           //logger.info("\n 2 filePath2 -->"+institucionBasica.getLogoReportes());
        }
        else {
        	//logger.info("\n 3 filePath -->"+filePath);
          //  filePath=filePath.substring(0, filePath.lastIndexOf("\\", (filePath.length()-2))); 
            //filePath+="\\imagenes\\logo-grey.gif";
            filePath= institucionBasica.getLogoReportes();
        } */
        logger.info("filepath image->"+filePath);
        logger.info("Ubicacion del Logo en el Reporte: " + institucionBasica.getUbicacionLogo());
        
        iTextBaseTable iSection;
        iSection = new iTextBaseTable();
        
        iSection.useTable( PdfReports.REPORT_HEADER_TABLE, 1, 13 );
        iSection.setTableOffset( PdfReports.REPORT_HEADER_TABLE, 0.0f );
        iSection.setTableBorder( PdfReports.REPORT_HEADER_TABLE,C_BLANCO, 0.0f );
        
        iSection.setTableBorderColor(PdfReports.REPORT_HEADER_TABLE, C_BLANCO);
        iSection.setTableBorderWidth(PdfReports.REPORT_HEADER_TABLE, 0.0f);
        
        iSection.setTableCellBorderWidth( PdfReports.REPORT_HEADER_TABLE, 0.0f );
        iSection.setTableCellPadding( PdfReports.REPORT_HEADER_TABLE, 0.0f );
        iSection.setTableSpaceBetweenCells( PdfReports.REPORT_HEADER_TABLE, 0.0f );
        iSection.setTableCellsDefaultColors( PdfReports.REPORT_HEADER_TABLE, C_BLANCO, C_BLANCO);
        iSection.setTableCellsDefaultHAlignment( PdfReports.REPORT_HEADER_TABLE, iTextBaseDocument.ALIGNMENT_CENTER );
        iSection.setTableCellsDefaultVAlignment( PdfReports.REPORT_HEADER_TABLE, iTextBaseDocument.ALIGNMENT_JUSTIFIED_ALL );
        iSection.setTableWidth(PdfReports.REPORT_HEADER_TABLE, 100);


        report.font.setFontAttributes( C_NEGRO, 8, false, false, false );

        
        /*********************SECCION DEL TITULO - LOGO - Y DATOS DE LA INSITUCION*********************/
        HashMap mapDatosInst=new HashMap();
        mapDatosInst=(HashMap)mapaFactura.get("headerDatosInstitucion");
        /**********mapa paciente*********/
        HashMap mapDatosPac=new HashMap();
        mapDatosPac=(HashMap)mapaFactura.get("headerAndFooterDatosPaciente");        

        String prefijoFactura=(mapDatosInst.get("prefijo_fact")+"").equals("null")?"":mapDatosInst.get("prefijo_fact")+"";

        if(institucionBasica.getUbicacionLogo().equals(ConstantesIntegridadDominio.acronimoUbicacionIzquierda)) {
            //logo izq
            iSection.setTableCellsColSpan(4);
            iSection.setTableCellsRowSpan(2);
            iSection.addTableImageCellAligned( PdfReports.REPORT_HEADER_TABLE,filePath, 100f,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT, 0, 0 );
            //Razon Social
            report.font.setFontAttributes( C_NEGRO, 13, true, false, false );
            iSection.setTableCellsColSpan(9);
            iSection.setTableCellsRowSpan(1); //2 
            iSection.addTableTextCellAligned(PdfReports.REPORT_HEADER_TABLE, mapDatosInst.get("razonsocial")+" ", report.font, iTextBaseDocument.ALIGNMENT_CENTER, iTextBaseDocument.ALIGNMENT_CENTER);
            report.font.setFontAttributes( C_NEGRO, 8, false, false, false );
            
            // espacio en blanco
           /* iSection.setTableCellsColSpan(4);
            iSection.setTableCellsRowSpan(2);
            iSection.addTableTextCellAligned(PdfReports.REPORT_HEADER_TABLE, "", report.font, iTextBaseDocument.ALIGNMENT_CENTER, iTextBaseDocument.ALIGNMENT_CENTER);
            */
        }
        else {
            //espacio en blanco
            /*iSection.setTableCellsColSpan(4);
            iSection.setTableCellsRowSpan(2);
            iSection.addTableTextCellAligned(PdfReports.REPORT_HEADER_TABLE, "", report.font, iTextBaseDocument.ALIGNMENT_CENTER, iTextBaseDocument.ALIGNMENT_CENTER);
            */
        	 //logo
        	iSection.setTableCellsColSpan(4);
            iSection.setTableCellsRowSpan(2);//
            iSection.addTableImageCellAligned( PdfReports.REPORT_HEADER_TABLE,filePath, 100f,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT, 0, 0 );
        
            //razon social
            report.font.setFontAttributes( C_NEGRO, 13, true, false, false );
            iSection.setTableCellsColSpan(9);
            iSection.setTableCellsRowSpan(1); //2             
            //MT6084 se agrega centro de atención 
            iSection.addTableTextCellAligned(PdfReports.REPORT_HEADER_TABLE, mapDatosInst.get("razonsocial")+" - "+mapaFactura.get("nomCentroAtencion"), report.font, iTextBaseDocument.ALIGNMENT_CENTER, iTextBaseDocument.ALIGNMENT_CENTER);
            report.font.setFontAttributes( C_NEGRO, 8, false, false, false );
            
           
            }
        
     //   String cadena2 = "\n" + "Centro Atención: "+mapaFactura.get("nomCentroAtencion") + "\n\n" + mapDatosInst.get("descid") + " " + mapDatosInst.get("numeroid") + "";        

        //MT6084 para que presente la información del centro de atención en una sola celda
      //Nit
        String NIT = "" + mapDatosInst.get("descid") + " " + mapDatosInst.get("numeroid") + "";
        	//Info Ubicacion Institucion
        String infoUbicacionInst = "Dir. "+mapDatosInst.get("direccion")+" - Tel. "+mapDatosInst.get("telefono")+" - Ciudad. "+mapDatosInst.get("descciudad");
        	//Encabezado parametrizado
        String encabezado= FormatoImpresionFactura.obtenerEncabezadoPieFactura(mapaFactura.get("codigoFactura")+"", Double.parseDouble(mapaFactura.get("empresaInstitucion")+""))[0];
        
        String cadena=NIT+"   -"+infoUbicacionInst;
        
        report.font.setFontAttributes(0x000000, 7, false, false, false);
        iSection.setTableCellsColSpan(9); //5 
        iSection.setTableCellsRowSpan(1); //2 
        iSection.addTableTextCellAligned(PdfReports.REPORT_HEADER_TABLE, NIT+"  - "+infoUbicacionInst+"\n"+encabezado, report.font, iTextBaseDocument.ALIGNMENT_CENTER, iTextBaseDocument.ALIGNMENT_CENTER);
        report.font.setFontAttributes(0x000000, 8, false, false, false);
        /*iSection.setTableCellsColSpan(5);
        iSection.setTableCellsRowSpan(1); //2 
        iSection.addTableTextCellAligned(PdfReports.REPORT_HEADER_TABLE, cadena2, report.font, iTextBaseDocument.ALIGNMENT_CENTER, iTextBaseDocument.ALIGNMENT_CENTER);
       
        iSection.setTableCellsColSpan(5);
        iSection.setTableCellsRowSpan(1);
        iSection.addTableTextCellAligned(PdfReports.REPORT_HEADER_TABLE, mapDatosInst.get("direccion")+"", report.font, iTextBaseDocument.ALIGNMENT_LEFT, iTextBaseDocument.ALIGNMENT_LEFT );
        iSection.setTableCellsColSpan(2);
        iSection.setTableCellsRowSpan(1);
        iSection.addTableTextCellAligned(PdfReports.REPORT_HEADER_TABLE, mapDatosInst.get("telefono")+"", report.font, iTextBaseDocument.ALIGNMENT_LEFT, iTextBaseDocument.ALIGNMENT_LEFT );
        iSection.setTableCellsColSpan(3);
        iSection.setTableCellsRowSpan(1);
        iSection.addTableTextCellAligned(PdfReports.REPORT_HEADER_TABLE, mapDatosInst.get("descciudad")+"", report.font, iTextBaseDocument.ALIGNMENT_LEFT, iTextBaseDocument.ALIGNMENT_LEFT );
        */
        //espacio vacio para posible info de la facture si esta anulada por si el logo va a la derecha 
        iSection.setTableCellsColSpan(3);
        iSection.setTableCellsRowSpan(1);
        if((mapDatosPac.get("estadofacturacion")+"").equals(ConstantesBD.codigoEstadoFacturacionAnulada+"")){
            report.font.setFontAttributes(0x000000, 8, true, false, false);
        	iSection.addTableTextCellAligned(PdfReports.REPORT_HEADER_TABLE,"FACTURA ANULADA "+mapDatosPac.get("fechaanulacion")+" "+mapDatosPac.get("horaanulacion"), report.font, iTextBaseDocument.ALIGNMENT_CENTER, iTextBaseDocument.ALIGNMENT_CENTER );
        }
        else {
        	iSection.addTableTextCellAligned(PdfReports.REPORT_HEADER_TABLE, "", report.font, iTextBaseDocument.ALIGNMENT_CENTER, iTextBaseDocument.ALIGNMENT_CENTER );
        }
        //fin fila ---------------------------------------
        
        
        report.font.setFontAttributes(0x000000, 8, false, false, false);

        if(mapDatosInst.get("resolucion")!=null && !(mapDatosInst.get("resolucion")+"").trim().equals("null") && !(mapDatosInst.get("resolucion")+"").trim().equals("")) {
	        //fila
	        //espacio vacio
	        iSection.setTableCellsColSpan(4);
	        iSection.setTableCellsRowSpan(1);
	        iSection.addTableTextCellAligned(PdfReports.REPORT_HEADER_TABLE, "Autorización Resolución DIAN:", report.font, iTextBaseDocument.ALIGNMENT_LEFT, iTextBaseDocument.ALIGNMENT_LEFT );
	        iSection.setTableCellsColSpan(3);
	        iSection.setTableCellsRowSpan(1);
	        iSection.addTableTextCellAligned(PdfReports.REPORT_HEADER_TABLE, mapDatosInst.get("resolucion")+"", report.font, iTextBaseDocument.ALIGNMENT_LEFT, iTextBaseDocument.ALIGNMENT_LEFT );
	        iSection.setTableCellsColSpan(2);
	        iSection.setTableCellsRowSpan(1);
	        iSection.addTableTextCellAligned(PdfReports.REPORT_HEADER_TABLE, "Desde", report.font, iTextBaseDocument.ALIGNMENT_LEFT, iTextBaseDocument.ALIGNMENT_LEFT );
	        iSection.setTableCellsColSpan(2);
	        iSection.setTableCellsRowSpan(1);
	        iSection.addTableTextCellAligned(PdfReports.REPORT_HEADER_TABLE, prefijoFactura+( mapDatosInst.get( "rangoincialfactura" )+"" ), report.font, iTextBaseDocument.ALIGNMENT_LEFT, iTextBaseDocument.ALIGNMENT_LEFT );
	
	        iSection.setTableCellsColSpan(2);
	        iSection.setTableCellsRowSpan(1);
	        iSection.addTableTextCellAligned(PdfReports.REPORT_HEADER_TABLE, prefijoFactura+( mapDatosInst.get( "rangofinalfacltura" )+"" ), report.font, iTextBaseDocument.ALIGNMENT_LEFT, iTextBaseDocument.ALIGNMENT_LEFT );
        }

        else {
	        iSection.setTableCellsColSpan(13);
	        iSection.addTableTextCellAligned(PdfReports.REPORT_HEADER_TABLE, "", report.font, iTextBaseDocument.ALIGNMENT_LEFT, iTextBaseDocument.ALIGNMENT_LEFT );
        }
        //fin fila ---------------------------------------         

        
        /*report.font.setFontAttributes(0x000000, 8, false, false, false);
        // crear table para poner demas info dela institucion
        iSection.setTableCellsColSpan(5);
        iSection.setTableCellsRowSpan(1);
        iSection.addTableTextCellAligned(PdfReports.REPORT_HEADER_TABLE, "Centro Atención: "+mapaFactura.get("nomCentroAtencion") , report.font, iTextBaseDocument.ALIGNMENT_LEFT, iTextBaseDocument.ALIGNMENT_LEFT );

        //nit
        iSection.setTableCellsColSpan(1);
        iSection.setTableCellsRowSpan(1);
        iSection.addTableTextCellAligned(PdfReports.REPORT_HEADER_TABLE, mapDatosInst.get("descid")+"", report.font, iTextBaseDocument.ALIGNMENT_LEFT, iTextBaseDocument.ALIGNMENT_LEFT );
        //numero nit
        iSection.setTableCellsColSpan(4);
        iSection.setTableCellsRowSpan(1);
        iSection.addTableTextCellAligned(PdfReports.REPORT_HEADER_TABLE, mapDatosInst.get("numeroid")+"", report.font, iTextBaseDocument.ALIGNMENT_LEFT, iTextBaseDocument.ALIGNMENT_LEFT );
         */      
        
        
        
        iSection.setTableCellsColSpan( 13 ); 
        iSection.setTableCellsRowSpan( 1 );
        
       // String encabezado= FormatoImpresionFactura.obtenerEncabezadoPieFactura(mapaFactura.get("codigoFactura")+"", Double.parseDouble(mapaFactura.get("empresaInstitucion")+""))[0];
        
        iSection.addTableTextCellAligned(PdfReports.REPORT_HEADER_TABLE, encabezado, report.font, iTextBaseDocument.ALIGNMENT_LEFT, iTextBaseDocument.ALIGNMENT_LEFT );
        
        /***************************SECCION PARA LOS DATOS DE LA FACTURA***************************/
        report.font.setFontAttributes(C_NEGRO, 8, true, false, false);
        //fila
        iSection.setTableCellsColSpan(13);
        iSection.addTableTextCellAligned(PdfReports.REPORT_HEADER_TABLE, "__________________________________________________________________________________________________________", report.font, iTextBaseDocument.ALIGNMENT_CENTER, iTextBaseDocument.ALIGNMENT_CENTER );        

        iSection.setTableCellsColSpan(3);//4
        iSection.addTableTextCellAligned(PdfReports.REPORT_HEADER_TABLE, "Factura de Venta Nro: ",report.font, iTextBaseDocument.ALIGNMENT_LEFT, iTextBaseDocument.ALIGNMENT_LEFT );        
        iSection.setTableCellsColSpan(3);
        iSection.addTableTextCellAligned(PdfReports.REPORT_HEADER_TABLE, prefijoFactura+( mapDatosPac.get( "consecutivo_factura" )+"" ), report.font, iTextBaseDocument.ALIGNMENT_LEFT, iTextBaseDocument.ALIGNMENT_LEFT );
        report.font.setFontAttributes(C_NEGRO, 8, false, false, false);
        iSection.setTableCellsColSpan(2);
        iSection.addTableTextCellAligned(PdfReports.REPORT_HEADER_TABLE, "Fecha de Fact.: ", report.font, iTextBaseDocument.ALIGNMENT_LEFT, iTextBaseDocument.ALIGNMENT_LEFT );
        iSection.setTableCellsColSpan(2);
        iSection.addTableTextCellAligned(PdfReports.REPORT_HEADER_TABLE, UtilidadFecha.conversionFormatoFechaAAp(( mapDatosPac.get( "fecha_factura" )+"" )), report.font, iTextBaseDocument.ALIGNMENT_LEFT, iTextBaseDocument.ALIGNMENT_LEFT );
        iSection.setTableCellsColSpan(2);//1
        iSection.addTableTextCellAligned(PdfReports.REPORT_HEADER_TABLE, "Hora de Fact.: ", report.font, iTextBaseDocument.ALIGNMENT_LEFT, iTextBaseDocument.ALIGNMENT_LEFT );
        iSection.setTableCellsColSpan(1);
        iSection.addTableTextCellAligned(PdfReports.REPORT_HEADER_TABLE, ( mapDatosPac.get( "hora_factura" )+"" ), report.font, iTextBaseDocument.ALIGNMENT_LEFT, iTextBaseDocument.ALIGNMENT_LEFT );
                
        /***********************SECCION PARA LOS DATOS DEL PACIENTE***********************/
        report.font.setFontAttributes(C_NEGRO, 8, true, false, false);
        //fila
        iSection.setTableCellsColSpan(13);
        iSection.addTableTextCellAligned(PdfReports.REPORT_HEADER_TABLE, "__________________________________________________________________________________________________________", report.font, iTextBaseDocument.ALIGNMENT_CENTER, iTextBaseDocument.ALIGNMENT_CENTER );
        report.font.setFontAttributes(C_NEGRO, 8, false, false, false);
        iSection.setTableCellsColSpan(5);
        iSection.addTableTextCellAligned(PdfReports.REPORT_HEADER_TABLE, "Responsable: "+( mapDatosPac.get( "nombretercero" )+"" ), report.font, iTextBaseDocument.ALIGNMENT_LEFT, iTextBaseDocument.ALIGNMENT_LEFT );
        iSection.setTableCellsColSpan(5);
        iSection.addTableTextCellAligned(PdfReports.REPORT_HEADER_TABLE, "Número ID: "+( mapDatosPac.get( "numeroid_tercero" )+"" ), report.font, iTextBaseDocument.ALIGNMENT_LEFT, iTextBaseDocument.ALIGNMENT_LEFT );
        iSection.setTableCellsColSpan(3);        
        iSection.addTableTextCellAligned(PdfReports.REPORT_HEADER_TABLE, "Autorización Nro.: "+( mapaFactura.get( "nroAutorizacion" )+"" ),report.font, iTextBaseDocument.ALIGNMENT_LEFT, iTextBaseDocument.ALIGNMENT_LEFT );
        //fila        
        iSection.setTableCellsColSpan(5);
        iSection.addTableTextCellAligned(PdfReports.REPORT_HEADER_TABLE, "Paciente: "+( mapDatosPac.get( "nombrepersona" )+"" ), report.font, iTextBaseDocument.ALIGNMENT_LEFT, iTextBaseDocument.ALIGNMENT_LEFT );
        iSection.setTableCellsColSpan(5);
        iSection.addTableTextCellAligned(PdfReports.REPORT_HEADER_TABLE, mapDatosPac.get( "desctipoid" )+": " +( mapDatosPac.get( "numeroid_paciente" )+"" ), report.font, iTextBaseDocument.ALIGNMENT_LEFT, iTextBaseDocument.ALIGNMENT_LEFT );
        iSection.setTableCellsColSpan(3);
        iSection.addTableTextCellAligned(PdfReports.REPORT_HEADER_TABLE, "Póliza Nro.: "+( mapDatosPac.get( "numeropoliza" )+"" ), report.font, iTextBaseDocument.ALIGNMENT_LEFT, iTextBaseDocument.ALIGNMENT_LEFT );
        //fila  
        iSection.setTableCellsColSpan(5);
        iSection.addTableTextCellAligned(PdfReports.REPORT_HEADER_TABLE, "Dirección: "+( mapDatosPac.get( "direccionpersona" )+"" ), report.font, iTextBaseDocument.ALIGNMENT_LEFT, iTextBaseDocument.ALIGNMENT_LEFT );
        iSection.setTableCellsColSpan(5);
        iSection.addTableTextCellAligned(PdfReports.REPORT_HEADER_TABLE, "Teléfono: "+( mapDatosPac.get( "telefonopersona" )+"" ).trim(), report.font, iTextBaseDocument.ALIGNMENT_LEFT, iTextBaseDocument.ALIGNMENT_LEFT );        
        iSection.setTableCellsColSpan(3);
        iSection.addTableTextCellAligned(PdfReports.REPORT_HEADER_TABLE, "Ingreso: "+( mapDatosPac.get( "nombreviaingreso" )+"" ), report.font, iTextBaseDocument.ALIGNMENT_LEFT, iTextBaseDocument.ALIGNMENT_LEFT );        
//      fila
        String[] infoIngre=(mapDatosPac.get( "infoingreso")+"").split("@");
        iSection.setTableCellsColSpan(5);
        
        //if(UtilidadTexto.isEmpty(infoIngre[0]))
        {
        	infoIngre[0]= FormatoImpresionFactura.obtenerFechaAperturaCuentaFactura(mapaFactura.get("codigoFactura")+"");
        }
        
        iSection.addTableTextCellAligned(PdfReports.REPORT_HEADER_TABLE, "Fecha/Hora Ingreso: "+infoIngre[0], report.font, iTextBaseDocument.ALIGNMENT_LEFT, iTextBaseDocument.ALIGNMENT_LEFT );        
        iSection.setTableCellsColSpan(5);
        String fechaEgresoOCorteFact="";
        String diasEstancia="-1";
        try
        {
        	fechaEgresoOCorteFact=infoIngre[1];
        	diasEstancia=infoIngre[2];
        }
        catch (ArrayIndexOutOfBoundsException e) 
        {
        	fechaEgresoOCorteFact="";
			diasEstancia="-1";
		}
        
        String titulo="Fecha/Hora Egreso:";
        if(UtilidadTexto.isEmpty(fechaEgresoOCorteFact))
        {
        	fechaEgresoOCorteFact= FormatoImpresionFactura.obtenerFechaFactura(mapaFactura.get("codigoFactura")+"");
        	titulo="Fecha/Hora Corte: ";
        }
        
        iSection.addTableTextCellAligned(PdfReports.REPORT_HEADER_TABLE, titulo+fechaEgresoOCorteFact, report.font, iTextBaseDocument.ALIGNMENT_LEFT, iTextBaseDocument.ALIGNMENT_LEFT );        
        iSection.setTableCellsColSpan(3);
        iSection.addTableTextCellAligned(PdfReports.REPORT_HEADER_TABLE, Integer.parseInt(diasEstancia)>=0?"Dias Estancia: "+diasEstancia:"", report.font, iTextBaseDocument.ALIGNMENT_LEFT, iTextBaseDocument.ALIGNMENT_LEFT );
        
        report.usarSeccion( PdfReports.REPORT_HEADER_SECTION, iSection );        
        return report;         
    }
    
	/**
	 * 
	 * @param report
	 * @param mapaFactura
	 * @return
	 */
	private static PdfReports crearEncabezadoStandarVenezuela(PdfReports report,HashMap mapaFactura)
    {        
        String filePath=ValoresPorDefecto.getFilePath();
        if(System.getProperty("file.separator").equals("/"))
        {
           filePath=filePath.substring(0, filePath.lastIndexOf("/", (filePath.length()-2)));       
           filePath+="/imagenes/logo-grey.gif";           
        }
        else
        {
            filePath=filePath.substring(0, filePath.lastIndexOf("\\", (filePath.length()-2))); 
            filePath+="\\imagenes\\logo-grey.gif";
        } 
        logger.info("filepath image->"+filePath);
        iTextBaseTable iSection;
        iSection = new iTextBaseTable();
        iSection.useTable( PdfReports.REPORT_HEADER_TABLE, 1, 13 );
        iSection.setTableOffset( PdfReports.REPORT_HEADER_TABLE, 0 );
        iSection.setTableBorder( PdfReports.REPORT_HEADER_TABLE,C_BLANCO, 0.0f );
        iSection.setTableCellBorderWidth( PdfReports.REPORT_HEADER_TABLE, 0.5f );
        iSection.setTableCellPadding( PdfReports.REPORT_HEADER_TABLE, 1 );
        iSection.setTableSpaceBetweenCells( PdfReports.REPORT_HEADER_TABLE, 0.5f );
        iSection.setTableCellsDefaultColors( PdfReports.REPORT_HEADER_TABLE, C_BLANCO, C_BLANCO );
        iSection.setTableCellsDefaultHAlignment( PdfReports.REPORT_HEADER_TABLE, iTextBaseDocument.ALIGNMENT_CENTER );
        iSection.setTableCellsDefaultVAlignment( PdfReports.REPORT_HEADER_TABLE, iTextBaseDocument.ALIGNMENT_JUSTIFIED_ALL );
        report.font.setFontAttributes( C_NEGRO, 13, true, false, false );
        /*********************SECCION DEL TITULO - LOGO - Y DATOS DE LA INSITUCION*********************/
        HashMap mapDatosInst=new HashMap();
        mapDatosInst=(HashMap)mapaFactura.get("headerDatosInstitucion");
        /**********mapa paciente*********/
        HashMap mapDatosPac=new HashMap();
        mapDatosPac=(HashMap)mapaFactura.get("headerAndFooterDatosPaciente");        

        String prefijoFactura=(mapDatosInst.get("prefijo_fact")+"").equals("null")?"":mapDatosInst.get("prefijo_fact")+"";
        
        //columna
        //logo
        iSection.setTableCellsColSpan( 3 );
        iSection.addTableImageCellAligned( PdfReports.REPORT_HEADER_TABLE,filePath, 100f,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT, 0, 0 );
                
        //columna
        //razon social
        iSection.setTableCellsColSpan( 7 );
        iSection.addTableTextCellAligned(PdfReports.REPORT_HEADER_TABLE, mapDatosInst.get("razonsocial")+"\n"+mapDatosInst.get("tipoid")+" "+mapDatosInst.get("numeroid"), report.font, iTextBaseDocument.ALIGNMENT_LEFT, iTextBaseDocument.ALIGNMENT_LEFT );
        
        //columna
        //Nro factura
        report.font.setFontAttributes(0x000000, 9, true, false, false);
        String etiquetaNroFactura="No.Factura ";
        String etiquetaAnul="";
        if((mapDatosPac.get("estadofacturacion")+"").equals(ConstantesBD.codigoEstadoFacturacionAnulada+""))
        {
        	etiquetaAnul=" ANULADA";
        }
        
        iSection.setTableCellsColSpan( 3 );
        iSection.addTableTextCellAligned(PdfReports.REPORT_HEADER_TABLE, etiquetaNroFactura+" "+prefijoFactura+( mapDatosPac.get( "consecutivo_factura" )+"" )+etiquetaAnul, report.font, iTextBaseDocument.ALIGNMENT_LEFT, iTextBaseDocument.ALIGNMENT_LEFT );
        
        //direccion
        report.font.setFontAttributes(0x000000, 8, false, false, false);
        colDead(iSection, 3, report);
        iSection.setTableCellsColSpan( 7 );
        iSection.addTableTextCellAligned(PdfReports.REPORT_HEADER_TABLE, mapDatosInst.get("direccion")+"", report.font, iTextBaseDocument.ALIGNMENT_LEFT, iTextBaseDocument.ALIGNMENT_LEFT );
        colDead(iSection, 3, report);
        
        //telefono
        colDead(iSection, 3, report);
        iSection.setTableCellsColSpan( 7 );
        iSection.addTableTextCellAligned(PdfReports.REPORT_HEADER_TABLE, "Tel: "+mapDatosInst.get("telefono")+"", report.font, iTextBaseDocument.ALIGNMENT_LEFT, iTextBaseDocument.ALIGNMENT_LEFT );
        colDead(iSection, 3, report);
        
        //encabezado
        colDead(iSection, 3, report);
        iSection.setTableCellsColSpan( 7 );
        iSection.addTableTextCellAligned(PdfReports.REPORT_HEADER_TABLE, mapDatosInst.get("encabezado")+"", report.font, iTextBaseDocument.ALIGNMENT_LEFT, iTextBaseDocument.ALIGNMENT_LEFT );
        colDead(iSection, 3, report);
       
        report.usarSeccion( PdfReports.REPORT_HEADER_SECTION, iSection );        
        return report;         
    }
	
	
	
	
	/**
	 * 
	 * @param iSection
	 * @param numeroColumnas
	 * @param report
	 */
	private static void colDead(iTextBaseTable iSection, int numeroColumnas, PdfReports report)
	{
		iSection.setTableCellsColSpan( numeroColumnas );
        iSection.addTableTextCellAligned(PdfReports.REPORT_HEADER_TABLE, "", report.font, iTextBaseDocument.ALIGNMENT_LEFT, iTextBaseDocument.ALIGNMENT_LEFT );
	}
	
	/**
	 * 
	 * @param con
	 * @param report
	 * @param mapaFactura
	 * @param usuario
	 * @param mundoImpresion
	 */
	private static void crearCuerpoReporteVenezuela(Connection con, PdfReports report, HashMap mapaFactura, UsuarioBasico usuario, ImpresionFactura mundoImpresion) 
	{
		//Seccion del paciente
		
		HashMap mapDatosPac=new HashMap();
		mapDatosPac=(HashMap)mapaFactura.get("headerAndFooterDatosPaciente");
		
		int lenghtMaxConcepto=56;
		
		logger.info("\nmapadatosPac->"+mapDatosPac+"\n");		 
		if(!mapDatosPac.isEmpty())
		{
			report.createSection("SECCION_PACIENTE","TABLA_PACIENTE",1,2,0);
			report.getSectionReference("SECCION_PACIENTE").setTableBorder("TABLA_PACIENTE", 0xFFFFFF, 0.0f);
			report.getSectionReference("SECCION_PACIENTE").getTableReference("TABLA_PACIENTE").setPadding(0.0f);
			report.getSectionReference("SECCION_PACIENTE").setTableCellBorderWidth("TABLA_PACIENTE", 0.0f);
			report.getSectionReference("SECCION_PACIENTE").setTableCellsDefaultColors("TABLA_PACIENTE", 0xFFFFFF, 0xFFFFFF);
			//fila 
			report.font.setFontSizeAndAttributes(8,true,false,false);

			report.getSectionReference("SECCION_PACIENTE").setTableCellsColSpan(2);
	        report.getSectionReference("SECCION_PACIENTE").addTableTextCellAligned("TABLA_PACIENTE", "______________________________________________________________________________________________________________________________________", report.font,iTextBaseDocument.ALIGNMENT_JUSTIFIED,iTextBaseDocument.ALIGNMENT_JUSTIFIED);
	        
			report.getSectionReference("SECCION_PACIENTE").setTableCellsColSpan(1);
			report.getSectionReference("SECCION_PACIENTE").addTableTextCellAligned("TABLA_PACIENTE", "Apellidos y Nombres: "+( mapDatosPac.get( "nombrepersona" )+"" ), report.font, iTextBaseDocument.ALIGNMENT_LEFT, iTextBaseDocument.ALIGNMENT_LEFT );

			report.getSectionReference("SECCION_PACIENTE").setTableCellsColSpan(1);
			report.getSectionReference("SECCION_PACIENTE").addTableTextCellAligned("TABLA_PACIENTE", "Fecha: "+UtilidadFecha.conversionFormatoFechaAAp(( mapDatosPac.get( "fecha_factura" )+"" ))+" - "+ mapDatosPac.get( "hora_factura" ), report.font, iTextBaseDocument.ALIGNMENT_LEFT, iTextBaseDocument.ALIGNMENT_LEFT );

			report.font.setFontSizeAndAttributes(8,false,false,false);

			report.getSectionReference("SECCION_PACIENTE").setTableCellsColSpan(1);
			report.getSectionReference("SECCION_PACIENTE").addTableTextCellAligned("TABLA_PACIENTE", "Tipo Identificación: "+ mapDatosPac.get( "desctipoid" ), report.font, iTextBaseDocument.ALIGNMENT_LEFT, iTextBaseDocument.ALIGNMENT_LEFT );

			report.getSectionReference("SECCION_PACIENTE").setTableCellsColSpan(1);
			report.getSectionReference("SECCION_PACIENTE").addTableTextCellAligned("TABLA_PACIENTE", "Nro Identificación: "+ ( mapDatosPac.get( "numeroid_paciente" )+"" ), report.font, iTextBaseDocument.ALIGNMENT_LEFT, iTextBaseDocument.ALIGNMENT_LEFT );

			report.getSectionReference("SECCION_PACIENTE").setTableCellsColSpan(1);
			report.getSectionReference("SECCION_PACIENTE").addTableTextCellAligned("TABLA_PACIENTE", "Dirección: "+( mapDatosPac.get( "direccionpersona" )+"" ), report.font, iTextBaseDocument.ALIGNMENT_LEFT, iTextBaseDocument.ALIGNMENT_LEFT );

			report.getSectionReference("SECCION_PACIENTE").setTableCellsColSpan(1);
			report.getSectionReference("SECCION_PACIENTE").addTableTextCellAligned("TABLA_PACIENTE", "Teléfono: "+( mapDatosPac.get( "telefonopersona" )+"" ).trim(), report.font, iTextBaseDocument.ALIGNMENT_LEFT, iTextBaseDocument.ALIGNMENT_LEFT );

			report.addSectionToDocument("SECCION_PACIENTE");
		}
	     
		//Seccion de los medicamentos
		
		HashMap mapDatosSer=new HashMap();
        mapDatosSer=(HashMap)mapaFactura.get("cuerpoDatosServicios");
        //Seccion de los articulos
        HashMap mapDatosMedicamentos=new HashMap();
        HashMap mapDatosInsumos=new HashMap();
        
        mapDatosMedicamentos=(HashMap)mapaFactura.get("cuerpoDatosMedicamentos");
        mapDatosInsumos=(HashMap)mapaFactura.get("cuerpoDatosInsumos");
        
        if(!mapDatosSer.isEmpty() || !mapDatosMedicamentos.isEmpty() || !mapDatosInsumos.isEmpty())
        {
            report.createSection("SECCION_SERVICIOS","TABLA_SERVICIOS",1,7,0);
            report.getSectionReference("SECCION_SERVICIOS").setTableBorder("TABLA_SERVICIOS", 0xFFFFFF, 0.0f);
            report.getSectionReference("SECCION_SERVICIOS").getTableReference("TABLA_SERVICIOS").setPadding(0.0f);
            report.getSectionReference("SECCION_SERVICIOS").setTableCellBorderWidth("TABLA_SERVICIOS", 0.0f);
            report.getSectionReference("SECCION_SERVICIOS").setTableCellsDefaultColors("TABLA_SERVICIOS", 0xFFFFFF, 0xFFFFFF);
            
            report.getSectionReference("SECCION_SERVICIOS").setTableCellsColSpan(7);
	        report.getSectionReference("SECCION_SERVICIOS").addTableTextCellAligned("TABLA_SERVICIOS", "______________________________________________________________________________________________________________________________________", report.font,iTextBaseDocument.ALIGNMENT_JUSTIFIED,iTextBaseDocument.ALIGNMENT_JUSTIFIED);
	        
            //fila 
            report.font.setFontSizeAndAttributes(8,true,false,false);
            report.getSectionReference("SECCION_SERVICIOS").setTableCellsColSpan(4);
            report.getSectionReference("SECCION_SERVICIOS").addTableTextCellAligned("TABLA_SERVICIOS", "Concepto", report.font,iTextBaseDocument.ALIGNMENT_JUSTIFIED,iTextBaseDocument.ALIGNMENT_JUSTIFIED);
            //fila 
            report.getSectionReference("SECCION_SERVICIOS").setTableCellsColSpan(1);
            report.getSectionReference("SECCION_SERVICIOS").addTableTextCellAligned("TABLA_SERVICIOS", "Cantidad", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
            //fila 
            report.getSectionReference("SECCION_SERVICIOS").setTableCellsColSpan(1);
            report.getSectionReference("SECCION_SERVICIOS").addTableTextCellAligned("TABLA_SERVICIOS", "Valor Unitario", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
            
            //fila 
            report.getSectionReference("SECCION_SERVICIOS").setTableCellsColSpan(1);
            report.getSectionReference("SECCION_SERVICIOS").addTableTextCellAligned("TABLA_SERVICIOS", "Valor Total", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
        }
        if(!mapDatosSer.isEmpty())
        {
            String codigoGrupoServ="";
            for(int k=0;k<Integer.parseInt(mapDatosSer.get("numRegistros")+"");k++)
            {
            	
            	//descgruposerv
            	if(!codigoGrupoServ.equals(mapDatosSer.get("codgruposerv_"+k)+""))
            	{	
	            	report.font.setFontSizeAndAttributes(8,true,false,false);
	            	report.getSectionReference("SECCION_SERVICIOS").setTableCellsColSpan(7);
	                report.getSectionReference("SECCION_SERVICIOS").addTableTextCellAligned("TABLA_SERVICIOS",mapDatosSer.get("descgruposerv_"+k)+"" , report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
	                codigoGrupoServ=mapDatosSer.get("codgruposerv_"+k)+"";
            	}    
                
            	report.font.setFontSizeAndAttributes(8,false,false,false);
            	report.getSectionReference("SECCION_SERVICIOS").setTableCellsColSpan(4);
            	
            	String concepto=mapDatosSer.get("descservicio_"+k)+"";
            	if(concepto.length()>lenghtMaxConcepto)
            		concepto=concepto.substring(0, lenghtMaxConcepto)+"....";
            	
                report.getSectionReference("SECCION_SERVICIOS").addTableTextCellAligned("TABLA_SERVICIOS",concepto , report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
                report.getSectionReference("SECCION_SERVICIOS").setTableCellsColSpan(1);
                report.getSectionReference("SECCION_SERVICIOS").addTableTextCellAligned("TABLA_SERVICIOS",mapDatosSer.get("cantidad_"+k)+"", report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_RIGHT);
                report.getSectionReference("SECCION_SERVICIOS").setTableCellsColSpan(1);
                report.getSectionReference("SECCION_SERVICIOS").addTableTextCellAligned("TABLA_SERVICIOS", UtilidadTexto.formatearValores(mapDatosSer.get("valorunitario_"+k)+""), report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_RIGHT);
                report.getSectionReference("SECCION_SERVICIOS").setTableCellsColSpan(1);	
                report.getSectionReference("SECCION_SERVICIOS").addTableTextCellAligned("TABLA_SERVICIOS", UtilidadTexto.formatearValores(mapDatosSer.get("valortotal_"+k)+""), report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_RIGHT);
            } 
        }
        
        if(!mapDatosSer.isEmpty() || !mapDatosMedicamentos.isEmpty() || !mapDatosInsumos.isEmpty())
        {
        	report.addSectionToDocument("SECCION_SERVICIOS");
        }	
        
        if(!mapDatosMedicamentos.isEmpty())
        {
            report.createSection("SECCION_ARTICULOS","TABLA_ARTICULOS",1,7,0);
            report.getSectionReference("SECCION_ARTICULOS").setTableBorder("TABLA_ARTICULOS", 0xFFFFFF, 0.0f);
            report.getSectionReference("SECCION_ARTICULOS").getTableReference("TABLA_ARTICULOS").setPadding(0.0f);
            report.getSectionReference("SECCION_ARTICULOS").setTableCellBorderWidth("TABLA_ARTICULOS", 0.0f);
            report.getSectionReference("SECCION_ARTICULOS").setTableCellsDefaultColors("TABLA_ARTICULOS", 0xFFFFFF, 0xFFFFFF);
            
            report.font.setFontSizeAndAttributes(8,true,false,false);
            report.getSectionReference("SECCION_ARTICULOS").setTableCellsColSpan(7);
            report.getSectionReference("SECCION_ARTICULOS").addTableTextCellAligned("TABLA_ARTICULOS", "Medicamentos", report.font,iTextBaseDocument.ALIGNMENT_JUSTIFIED,iTextBaseDocument.ALIGNMENT_JUSTIFIED);
            
            report.font.setFontSizeAndAttributes(8,false,false,false);
            for(int k=0;k<Integer.parseInt(mapDatosMedicamentos.get("numRegistros")+"");k++)
            {
            	report.getSectionReference("SECCION_ARTICULOS").setTableCellsColSpan(4);
            	
            	String concepto=mapDatosSer.get("descservicio_"+k)+"";
            	if(concepto.length()>lenghtMaxConcepto)
            		concepto=concepto.substring(0, lenghtMaxConcepto)+"....";
            	
                report.getSectionReference("SECCION_ARTICULOS").addTableTextCellAligned("TABLA_ARTICULOS",mapDatosMedicamentos.get("descarticulo_"+k)+"", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
                report.getSectionReference("SECCION_ARTICULOS").setTableCellsColSpan(1);
                report.getSectionReference("SECCION_ARTICULOS").addTableTextCellAligned("TABLA_ARTICULOS", mapDatosMedicamentos.get("cantidad_"+k)+"", report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_RIGHT);
                report.getSectionReference("SECCION_ARTICULOS").setTableCellsColSpan(1);
                report.getSectionReference("SECCION_ARTICULOS").addTableTextCellAligned("TABLA_ARTICULOS", UtilidadTexto.formatearValores(mapDatosMedicamentos.get("valorunitario_"+k)+""), report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_RIGHT);
                report.getSectionReference("SECCION_ARTICULOS").setTableCellsColSpan(1);
                report.getSectionReference("SECCION_ARTICULOS").addTableTextCellAligned("TABLA_ARTICULOS", UtilidadTexto.formatearValores(mapDatosMedicamentos.get("valortotal_"+k)+""), report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_RIGHT);
            }
            
            report.font.setFontSizeAndAttributes(8,true,false,false);
            report.getSectionReference("SECCION_ARTICULOS").setTableCellsColSpan(7);
            report.getSectionReference("SECCION_ARTICULOS").addTableTextCellAligned("TABLA_ARTICULOS", "Materiales e Insumos", report.font,iTextBaseDocument.ALIGNMENT_JUSTIFIED,iTextBaseDocument.ALIGNMENT_JUSTIFIED);
            
            report.font.setFontSizeAndAttributes(8,false,false,false);
            for(int k=0;k<Integer.parseInt(mapDatosInsumos.get("numRegistros")+"");k++)
            {
            	report.getSectionReference("SECCION_ARTICULOS").setTableCellsColSpan(4);
            	
            	String concepto=mapDatosSer.get("descservicio_"+k)+"";
            	if(concepto.length()>lenghtMaxConcepto)
            		concepto=concepto.substring(0, lenghtMaxConcepto)+"....";
            	
                report.getSectionReference("SECCION_ARTICULOS").addTableTextCellAligned("TABLA_ARTICULOS",mapDatosInsumos.get("descarticulo_"+k)+"", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
                report.getSectionReference("SECCION_ARTICULOS").setTableCellsColSpan(1);
                report.getSectionReference("SECCION_ARTICULOS").addTableTextCellAligned("TABLA_ARTICULOS", mapDatosInsumos.get("cantidad_"+k)+"", report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_RIGHT);
                report.getSectionReference("SECCION_ARTICULOS").setTableCellsColSpan(1);
                report.getSectionReference("SECCION_ARTICULOS").addTableTextCellAligned("TABLA_ARTICULOS", UtilidadTexto.formatearValores(mapDatosInsumos.get("valorunitario_"+k)+""), report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_RIGHT);
                report.getSectionReference("SECCION_ARTICULOS").setTableCellsColSpan(1);
                report.getSectionReference("SECCION_ARTICULOS").addTableTextCellAligned("TABLA_ARTICULOS", UtilidadTexto.formatearValores(mapDatosInsumos.get("valortotal_"+k)+""), report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_RIGHT);
            }
            
            report.addSectionToDocument("SECCION_ARTICULOS");
        }
	
        report.createSection("SECCION_TOTALES","TABLA_TOTALES",1,7,0);
        report.getSectionReference("SECCION_TOTALES").setTableBorder("TABLA_TOTALES", 0xFFFFFF, 0.0f);
        report.getSectionReference("SECCION_TOTALES").getTableReference("TABLA_TOTALES").setPadding(0.0f);
        report.getSectionReference("SECCION_TOTALES").setTableCellBorderWidth("TABLA_TOTALES", 0.0f);
        report.getSectionReference("SECCION_TOTALES").setTableCellsDefaultColors("TABLA_TOTALES", 0xFFFFFF, 0xFFFFFF);
        
        //fila
        report.font.setFontSizeAndAttributes(8,true,false,false);
        report.getSectionReference("SECCION_TOTALES").setTableCellsColSpan(7);
        report.getSectionReference("SECCION_TOTALES").addTableTextCellAligned("TABLA_TOTALES", "______________________________________________________________________________________________________________________________________", report.font,iTextBaseDocument.ALIGNMENT_JUSTIFIED,iTextBaseDocument.ALIGNMENT_JUSTIFIED);
        
        report.getSectionReference("SECCION_TOTALES").setTableCellsRowSpan(7);
        report.getSectionReference("SECCION_TOTALES").setTableCellsColSpan(4);
        report.getSectionReference("SECCION_TOTALES").addTableTextCellAligned("TABLA_TOTALES","El Valor Neto a Cargo del Paciente son: " , report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
        report.getSectionReference("SECCION_TOTALES").setTableCellsRowSpan(1);
        report.getSectionReference("SECCION_TOTALES").setTableCellsColSpan(2);
        report.getSectionReference("SECCION_TOTALES").addTableTextCellAligned("TABLA_TOTALES","SubTotal" , report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
        report.getSectionReference("SECCION_TOTALES").setTableCellsColSpan(1);
        report.getSectionReference("SECCION_TOTALES").addTableTextCellAligned("TABLA_TOTALES",UtilidadTexto.formatearValores(mapDatosPac.get("valortotalfactura")+"") , report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_RIGHT);
        
        //fila
        logger.info("===>Convenio: "+mapDatosPac.get("convenio"));
        String tipoRegimenConvenio = Utilidades.obtenerCodigoTipoRegimenConvenio(con, mapDatosPac.get("convenio")+"");
        logger.info("===>Tipo Regimen: "+tipoRegimenConvenio);
        if(!tipoRegimenConvenio.equals(ConstantesBD.codigoTipoRegimenParticular))
        {
            report.getSectionReference("SECCION_TOTALES").setTableCellsColSpan(2);
            report.getSectionReference("SECCION_TOTALES").addTableTextCellAligned("TABLA_TOTALES","Total Empresa Responsable" , report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
            report.getSectionReference("SECCION_TOTALES").setTableCellsColSpan(1);
            report.getSectionReference("SECCION_TOTALES").addTableTextCellAligned("TABLA_TOTALES",UtilidadTexto.formatearValores(mapDatosPac.get("valorconvenio")+"") , report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_RIGHT);

            if(Utilidades.convertirADouble(mapDatosPac.get("valorfavorconvenio")+"")>0)
            {
                //fila
	            report.getSectionReference("SECCION_TOTALES").setTableCellsColSpan(2);
	            report.getSectionReference("SECCION_TOTALES").addTableTextCellAligned("TABLA_TOTALES","A Favor Empresa Responsable" , report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
	            report.getSectionReference("SECCION_TOTALES").setTableCellsColSpan(1);
	            report.getSectionReference("SECCION_TOTALES").addTableTextCellAligned("TABLA_TOTALES",UtilidadTexto.formatearValores(mapDatosPac.get("valorfavorconvenio")+"") , report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_RIGHT);
            }
            //fila
            report.getSectionReference("SECCION_TOTALES").setTableCellsColSpan(2);
            report.getSectionReference("SECCION_TOTALES").addTableTextCellAligned("TABLA_TOTALES","Total del Paciente" , report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
            report.getSectionReference("SECCION_TOTALES").setTableCellsColSpan(1);
            report.getSectionReference("SECCION_TOTALES").addTableTextCellAligned("TABLA_TOTALES",UtilidadTexto.formatearValores(mapDatosPac.get("valorbrutopaciente")+"") , report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_RIGHT);
        }
        else
        {
            //fila
            report.getSectionReference("SECCION_TOTALES").setTableCellsColSpan(2);
            report.getSectionReference("SECCION_TOTALES").addTableTextCellAligned("TABLA_TOTALES","Total del Paciente" , report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
            report.getSectionReference("SECCION_TOTALES").setTableCellsColSpan(1);
            report.getSectionReference("SECCION_TOTALES").addTableTextCellAligned("TABLA_TOTALES",UtilidadTexto.formatearValores(mapDatosPac.get("valortotalfactura")+"") , report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_RIGHT);
        }
        //fila
        report.getSectionReference("SECCION_TOTALES").setTableCellsColSpan(2);
        report.getSectionReference("SECCION_TOTALES").addTableTextCellAligned("TABLA_TOTALES","Total Abonos del Paciente" , report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
        report.getSectionReference("SECCION_TOTALES").setTableCellsColSpan(1);
        report.getSectionReference("SECCION_TOTALES").addTableTextCellAligned("TABLA_TOTALES",UtilidadTexto.formatearValores(mapDatosPac.get("valorabonos")+"") , report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_RIGHT);
        //fila
        report.getSectionReference("SECCION_TOTALES").setTableCellsColSpan(2);
        report.getSectionReference("SECCION_TOTALES").addTableTextCellAligned("TABLA_TOTALES","Total Descuento del Paciente" , report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
        report.getSectionReference("SECCION_TOTALES").setTableCellsColSpan(1);
        report.getSectionReference("SECCION_TOTALES").addTableTextCellAligned("TABLA_TOTALES",UtilidadTexto.formatearValores(mapDatosPac.get("valordescuentopaciente")+"") , report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_RIGHT);
        //fila
        report.getSectionReference("SECCION_TOTALES").setTableCellsColSpan(2);
        if(Double.parseDouble(mapDatosPac.get("valornetopaciente")+"")<0)  
            report.getSectionReference("SECCION_TOTALES").addTableTextCellAligned("TABLA_TOTALES","Valor Neto a Favor del Paciente" , report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);       
        else       
            report.getSectionReference("SECCION_TOTALES").addTableTextCellAligned("TABLA_TOTALES","Valor Neto a Cargo del Paciente" , report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
        report.getSectionReference("SECCION_TOTALES").setTableCellsColSpan(1);
        report.getSectionReference("SECCION_TOTALES").addTableTextCellAligned("TABLA_TOTALES",UtilidadTexto.formatearValores(mapDatosPac.get("valornetopaciente")+"" ), report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_RIGHT);
        report.addSectionToDocument("SECCION_TOTALES");
        
        //fila
        report.createSection("SECCION_VALORES_LETRAS","TABLA_VALORES_LETRAS",1,1,10);
        report.getSectionReference("SECCION_VALORES_LETRAS").setTableBorder("TABLA_VALORES_LETRAS", 0xFFFFFF, 0.0f);
        report.getSectionReference("SECCION_VALORES_LETRAS").getTableReference("TABLA_VALORES_LETRAS").setPadding(0.0f);
        report.getSectionReference("SECCION_VALORES_LETRAS").setTableCellBorderWidth("TABLA_VALORES_LETRAS", 0.0f);
        report.getSectionReference("SECCION_VALORES_LETRAS").setTableCellsDefaultColors("TABLA_VALORES_LETRAS", 0xFFFFFF, 0xFFFFFF);
        report.getSectionReference("SECCION_VALORES_LETRAS").setTableCellsColSpan(1);
        
        report.addSectionToDocument("SECCION_VALORES_LETRAS");
        
        String cadena="";
        logger.info("===>Tipo Regimen: "+tipoRegimenConvenio);
        if(!tipoRegimenConvenio.equals(ConstantesBD.codigoTipoRegimenParticular))
        {
        	if(Double.parseDouble(mapDatosPac.get("valorconvenio")+"" )>0)
        	{	
	        	cadena="VALOR DEL RESPONSABLE EN LETRAS:   "+UtilidadN2T.convertirLetras(UtilidadTexto.formatearValores(mapDatosPac.get("valorconvenio")+"" ).replaceAll(",", ""),ConstantesBD.cadenaUnidades,ConstantesBD.cadenaUnidadesDecimales)+" MCTE.";
	            report.getSectionReference("SECCION_VALORES_LETRAS").addTableTextCellAligned("TABLA_VALORES_LETRAS",cadena, report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
        	}    
        	if(Double.parseDouble(mapDatosPac.get("valorbrutopaciente")+"" )>0)
        	{
	            cadena="VALOR DEL PACIENTE EN LETRAS:   "+UtilidadN2T.convertirLetras(UtilidadTexto.formatearValores(mapDatosPac.get("valorbrutopaciente")+"" ).replaceAll(",", ""),ConstantesBD.cadenaUnidades,ConstantesBD.cadenaUnidadesDecimales)+" MCTE.";
	            report.getSectionReference("SECCION_VALORES_LETRAS").addTableTextCellAligned("TABLA_VALORES_LETRAS",cadena, report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
        	}    
        }
        else
        {
        	if(Double.parseDouble(mapDatosPac.get("valortotalfactura")+"" )>0)
        	{	
	        	cadena="VALOR DEL PACIENTE EN LETRAS:   "+UtilidadN2T.convertirLetras(UtilidadTexto.formatearValores(mapDatosPac.get("valortotalfactura")+"" ).replaceAll(",", ""),ConstantesBD.cadenaUnidades,ConstantesBD.cadenaUnidadesDecimales)+" MCTE.";
	            report.getSectionReference("SECCION_VALORES_LETRAS").addTableTextCellAligned("TABLA_VALORES_LETRAS",cadena, report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
        	}    
        }
        
        //fila
        report.createSection("SECCION_FOOTER","TABLA_FOOTER",1,1,0);        
        report.getSectionReference("SECCION_FOOTER").setTableBorder("TABLA_FOOTER", 0xFFFFFF, 0.0f);
        report.getSectionReference("SECCION_FOOTER").getTableReference("TABLA_FOOTER").setPadding(0.0f);
        report.getSectionReference("SECCION_FOOTER").setTableCellBorderWidth("TABLA_FOOTER", 0.0f);
        report.getSectionReference("SECCION_FOOTER").setTableCellsDefaultColors("TABLA_FOOTER", 0xFFFFFF, 0xFFFFFF);
        //fila
        report.font.setFontSizeAndAttributes(8,true,false,false);
        report.getSectionReference("SECCION_FOOTER").setTableCellsColSpan(1);
        report.getSectionReference("SECCION_FOOTER").addTableTextCellAligned("TABLA_FOOTER", "______________________________________________________________________________________________________________________________________", report.font,iTextBaseDocument.ALIGNMENT_JUSTIFIED,iTextBaseDocument.ALIGNMENT_JUSTIFIED);
        report.getSectionReference("SECCION_FOOTER").addTableTextCellAligned("TABLA_FOOTER", "Usuario: "+mapDatosPac.get("usuarioelabora"), report.font,iTextBaseDocument.ALIGNMENT_JUSTIFIED,iTextBaseDocument.ALIGNMENT_JUSTIFIED);
        report.getSectionReference("SECCION_FOOTER").setTableCellsColSpan(1);
        String pie= FormatoImpresionFactura.obtenerEncabezadoPieFactura(mapaFactura.get("codigoFactura")+"", Double.parseDouble(mapaFactura.get("empresaInstitucion")+""))[1];
        report.getSectionReference("SECCION_FOOTER").addTableTextCellAligned("TABLA_FOOTER", pie, report.font,iTextBaseDocument.ALIGNMENT_JUSTIFIED,iTextBaseDocument.ALIGNMENT_JUSTIFIED);
        report.getSectionReference("SECCION_FOOTER").addTableTextCellAligned("TABLA_FOOTER", "______________________________________________________________________________________________________________________________________", report.font,iTextBaseDocument.ALIGNMENT_JUSTIFIED,iTextBaseDocument.ALIGNMENT_JUSTIFIED);
        report.addSectionToDocument("SECCION_FOOTER");
	}
	
	
	/**
	 * 
	 * @param con
	 * @param report
	 * @param mapaFactura
	 * @param usuario
	 * @param mundoImpresion
	 * @param tipoPiePagina 
	 */
    private static void crearCuerpoReporte(Connection con, PdfReports report, HashMap mapaFactura, UsuarioBasico usuario, ImpresionFactura mundoImpresion, String tipoPiePagina) 
    {
        HashMap mapDatosSer=new HashMap();
        mapDatosSer=(HashMap)mapaFactura.get("cuerpoDatosServicios");
        if(!mapDatosSer.isEmpty())
        {
            report.createSection("SECCION_SERVICIOS","TABLA_SERVICIOS",1,7,0);
            report.getSectionReference("SECCION_SERVICIOS").setTableBorder("TABLA_SERVICIOS", 0xFFFFFF, 0.0f);
            report.getSectionReference("SECCION_SERVICIOS").getTableReference("TABLA_SERVICIOS").setPadding(0.0f);
            report.getSectionReference("SECCION_SERVICIOS").getTableReference("TABLA_SERVICIOS").setSpacing(0.0f);
            report.getSectionReference("SECCION_SERVICIOS").setTableCellBorderWidth("TABLA_SERVICIOS", 0.0f);
            report.getSectionReference("SECCION_SERVICIOS").setTableCellsDefaultColors("TABLA_SERVICIOS", 0xFFFFFF, 0xFFFFFF);
            //fila 
            report.font.setFontSizeAndAttributes(8,true,false,false);
            report.getSectionReference("SECCION_SERVICIOS").setTableCellsColSpan(7);
            report.getSectionReference("SECCION_SERVICIOS").addTableTextCellAligned("TABLA_SERVICIOS", "______________________________________________________________________________________________________________________________________", report.font,iTextBaseDocument.ALIGNMENT_JUSTIFIED,iTextBaseDocument.ALIGNMENT_JUSTIFIED);
            //fila 
            report.font.setFontSizeAndAttributes(8,true,false,false);
            report.getSectionReference("SECCION_SERVICIOS").setTableCellsColSpan(1);
            report.getSectionReference("SECCION_SERVICIOS").addTableTextCellAligned("TABLA_SERVICIOS", "Código", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
            report.getSectionReference("SECCION_SERVICIOS").setTableCellsColSpan(2);
            report.getSectionReference("SECCION_SERVICIOS").addTableTextCellAligned("TABLA_SERVICIOS", "Procedimiento", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
            report.getSectionReference("SECCION_SERVICIOS").setTableCellsColSpan(1);
            report.getSectionReference("SECCION_SERVICIOS").addTableTextCellAligned("TABLA_SERVICIOS", "Detalle Procedimiento", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
            report.getSectionReference("SECCION_SERVICIOS").addTableTextCellAligned("TABLA_SERVICIOS", "Cantidad", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_CENTER);
            report.getSectionReference("SECCION_SERVICIOS").addTableTextCellAligned("TABLA_SERVICIOS", "Val. Unitario", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
            report.getSectionReference("SECCION_SERVICIOS").addTableTextCellAligned("TABLA_SERVICIOS", "Val. Total", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
            //fila 
            report.getSectionReference("SECCION_SERVICIOS").setTableCellsColSpan(7);
            report.getSectionReference("SECCION_SERVICIOS").addTableTextCellAligned("TABLA_SERVICIOS", "______________________________________________________________________________________________________________________________________", report.font,iTextBaseDocument.ALIGNMENT_JUSTIFIED,iTextBaseDocument.ALIGNMENT_JUSTIFIED);
            //fila 
            report.font.setFontSizeAndAttributes(8,false,false,false);
            
            for(int k=0;k<Integer.parseInt(mapDatosSer.get("numRegistros")+"");k++)
            {
                report.getSectionReference("SECCION_SERVICIOS").setTableCellsColSpan(1);
                report.getSectionReference("SECCION_SERVICIOS").addTableTextCellAligned("TABLA_SERVICIOS",mapDatosSer.get("codigopropeitario_"+k)+"" , report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
                if((mapDatosSer.get("escirugia_"+k)+"").equals("false"))
                    report.getSectionReference("SECCION_SERVICIOS").setTableCellsColSpan(3);
                else 
                    report.getSectionReference("SECCION_SERVICIOS").setTableCellsColSpan(6);
                report.getSectionReference("SECCION_SERVICIOS").addTableTextCellAligned("TABLA_SERVICIOS",mapDatosSer.get("procedimiento_"+k)+"", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
                if((mapDatosSer.get("escirugia_"+k)+"").equals("false"))
                {
                    report.getSectionReference("SECCION_SERVICIOS").setTableCellsColSpan(1);
                    report.getSectionReference("SECCION_SERVICIOS").addTableTextCellAligned("TABLA_SERVICIOS", mapDatosSer.get("cantidad_"+k)+"", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_CENTER);
                    report.getSectionReference("SECCION_SERVICIOS").addTableTextCellAligned("TABLA_SERVICIOS", UtilidadTexto.formatearValores(mapDatosSer.get("valorunitario_"+k)+""), report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_RIGHT);
                    report.getSectionReference("SECCION_SERVICIOS").addTableTextCellAligned("TABLA_SERVICIOS", UtilidadTexto.formatearValores(mapDatosSer.get("valortotal_"+k)+""), report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_RIGHT);
                }
                else
                {
                    //fila
                    HashMap mapDatosDetSer=new HashMap();
                    mapDatosDetSer=(HashMap)mapDatosSer.get("datosDetServicio_"+k);
                    if(!mapDatosDetSer.isEmpty())
                    {
                        for(int j=0;j<Integer.parseInt(mapDatosDetSer.get("numRegistros")+"");j++)
                        {
        	                logger.info("\n\n\nservicios cirugiia--> \n"+mapDatosDetSer+"\n\n\n");
        	                report.getSectionReference("SECCION_SERVICIOS").setTableCellsColSpan(3);
        	                report.getSectionReference("SECCION_SERVICIOS").addTableTextCellAligned("TABLA_SERVICIOS", "", report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_RIGHT);
        	                report.getSectionReference("SECCION_SERVICIOS").setTableCellsColSpan(1);
        	                report.getSectionReference("SECCION_SERVICIOS").addTableTextCellAligned("TABLA_SERVICIOS", mapDatosDetSer.get("nombreasocio_"+j)+"", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
        	                report.getSectionReference("SECCION_SERVICIOS").addTableTextCellAligned("TABLA_SERVICIOS", mapDatosDetSer.get("cantidad_"+j)+"", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_CENTER);
        	                logger.info(mapDatosDetSer.get("valorunitario_"+j)+"\nj-->"+j);
        	                logger.info(mapDatosDetSer.get("valorunitario_"+j)==null);
        	                logger.info(Double.parseDouble(mapDatosDetSer.get("valorunitario_"+j)+""));
        	                report.getSectionReference("SECCION_SERVICIOS").addTableTextCellAligned("TABLA_SERVICIOS", UtilidadTexto.formatearValores(Double.parseDouble(mapDatosDetSer.get("valorunitario_"+j)+"")), report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_RIGHT);
        	                report.getSectionReference("SECCION_SERVICIOS").addTableTextCellAligned("TABLA_SERVICIOS", UtilidadTexto.formatearValores(Double.parseDouble(mapDatosDetSer.get("valortotal_"+j)+"")), report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_RIGHT);
                        }
                    }
                }
            } 
            report.addSectionToDocument("SECCION_SERVICIOS");
        }
        HashMap mapDatosArt=new HashMap();
        mapDatosArt=(HashMap)mapaFactura.get("cuerpoDatosArticulos");
        if(!mapDatosArt.isEmpty())
        {
            report.createSection("SECCION_ARTICULOS","TABLA_ARTICULOS",1,7,0);
            report.getSectionReference("SECCION_ARTICULOS").setTableBorder("TABLA_ARTICULOS", 0xFFFFFF, 0.0f);
            report.getSectionReference("SECCION_ARTICULOS").getTableReference("TABLA_ARTICULOS").setPadding(0.0f);
            report.getSectionReference("SECCION_ARTICULOS").getTableReference("TABLA_ARTICULOS").setSpacing(0.0f);
            report.getSectionReference("SECCION_ARTICULOS").setTableCellBorderWidth("TABLA_ARTICULOS", 0.0f);
            report.getSectionReference("SECCION_ARTICULOS").setTableCellsDefaultColors("TABLA_ARTICULOS", 0xFFFFFF, 0xFFFFFF);
            
            report.font.setFontSizeAndAttributes(8,true,false,false);
            report.getSectionReference("SECCION_ARTICULOS").setTableCellsColSpan(7);
            report.getSectionReference("SECCION_ARTICULOS").addTableTextCellAligned("TABLA_ARTICULOS", "______________________________________________________________________________________________________________________________________", report.font,iTextBaseDocument.ALIGNMENT_JUSTIFIED,iTextBaseDocument.ALIGNMENT_JUSTIFIED);
            //fila 
            report.font.setFontSizeAndAttributes(8,true,false,false);
            report.getSectionReference("SECCION_ARTICULOS").setTableCellsColSpan(1);
            report.getSectionReference("SECCION_ARTICULOS").addTableTextCellAligned("TABLA_ARTICULOS", "Código", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
            report.getSectionReference("SECCION_ARTICULOS").setTableCellsColSpan(2);
            report.getSectionReference("SECCION_ARTICULOS").addTableTextCellAligned("TABLA_ARTICULOS", "Medicamento", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
            report.getSectionReference("SECCION_ARTICULOS").setTableCellsColSpan(1);
            report.getSectionReference("SECCION_ARTICULOS").addTableTextCellAligned("TABLA_ARTICULOS", "Detalle Medicamento", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
            report.getSectionReference("SECCION_ARTICULOS").addTableTextCellAligned("TABLA_ARTICULOS", "Cantidad", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_CENTER);
            report.getSectionReference("SECCION_ARTICULOS").addTableTextCellAligned("TABLA_ARTICULOS", "Val. Unitario", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
            report.getSectionReference("SECCION_ARTICULOS").addTableTextCellAligned("TABLA_ARTICULOS", "Val. Total", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
            //fila 
            report.getSectionReference("SECCION_ARTICULOS").setTableCellsColSpan(7);
            report.getSectionReference("SECCION_ARTICULOS").addTableTextCellAligned("TABLA_ARTICULOS", "______________________________________________________________________________________________________________________________________", report.font,iTextBaseDocument.ALIGNMENT_JUSTIFIED,iTextBaseDocument.ALIGNMENT_JUSTIFIED);

            
            report.font.setFontSizeAndAttributes(8,false,false,false);
            for(int k=0;k<Integer.parseInt(mapDatosArt.get("numRegistros")+"");k++)
            {
                report.getSectionReference("SECCION_ARTICULOS").setTableCellsColSpan(1);
                report.getSectionReference("SECCION_ARTICULOS").addTableTextCellAligned("TABLA_ARTICULOS",mapDatosArt.get("codigoarticulo_"+k)+"" , report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
                report.getSectionReference("SECCION_ARTICULOS").setTableCellsColSpan(3);
                report.getSectionReference("SECCION_ARTICULOS").addTableTextCellAligned("TABLA_ARTICULOS",mapDatosArt.get("descarticulo_"+k)+"", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
                report.getSectionReference("SECCION_ARTICULOS").setTableCellsColSpan(1);
                report.getSectionReference("SECCION_ARTICULOS").addTableTextCellAligned("TABLA_ARTICULOS", mapDatosArt.get("cantidad_"+k)+"", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_CENTER);
                report.getSectionReference("SECCION_ARTICULOS").addTableTextCellAligned("TABLA_ARTICULOS", UtilidadTexto.formatearValores(mapDatosArt.get("valorunitario_"+k)+""), report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_RIGHT);
                report.getSectionReference("SECCION_ARTICULOS").addTableTextCellAligned("TABLA_ARTICULOS", UtilidadTexto.formatearValores(mapDatosArt.get("valortotal_"+k)+""), report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_RIGHT);
            }
            report.addSectionToDocument("SECCION_ARTICULOS");
        }
        report.createSection("SECCION_TOTALES","TABLA_TOTALES",1,7,0);
        report.getSectionReference("SECCION_TOTALES").setTableBorder("TABLA_TOTALES", 0xFFFFFF, 0.0f);
        report.getSectionReference("SECCION_TOTALES").getTableReference("TABLA_TOTALES").setPadding(0.0f);
        report.getSectionReference("SECCION_TOTALES").getTableReference("TABLA_TOTALES").setSpacing(0.0f);
        report.getSectionReference("SECCION_TOTALES").setTableCellBorderWidth("TABLA_TOTALES", 0.0f);
        report.getSectionReference("SECCION_TOTALES").setTableCellsDefaultColors("TABLA_TOTALES", 0xFFFFFF, 0xFFFFFF);
        //fila
        report.font.setFontSizeAndAttributes(8,true,false,false);
        report.getSectionReference("SECCION_TOTALES").setTableCellsColSpan(7);
        report.getSectionReference("SECCION_TOTALES").addTableTextCellAligned("TABLA_TOTALES", "______________________________________________________________________________________________________________________________________", report.font,iTextBaseDocument.ALIGNMENT_JUSTIFIED,iTextBaseDocument.ALIGNMENT_JUSTIFIED);
        //fila
        HashMap mapDatosPac=new HashMap();
        mapDatosPac=(HashMap)mapaFactura.get("headerAndFooterDatosPaciente");   
        report.getSectionReference("SECCION_TOTALES").setTableCellsRowSpan(7);
        report.getSectionReference("SECCION_TOTALES").setTableCellsColSpan(4);
        report.getSectionReference("SECCION_TOTALES").addTableTextCellAligned("TABLA_TOTALES","" , report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
        report.getSectionReference("SECCION_TOTALES").setTableCellsRowSpan(1);
        report.getSectionReference("SECCION_TOTALES").setTableCellsColSpan(2);
        report.getSectionReference("SECCION_TOTALES").addTableTextCellAligned("TABLA_TOTALES","Total de Cargos" , report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
        report.getSectionReference("SECCION_TOTALES").setTableCellsColSpan(1);
        report.getSectionReference("SECCION_TOTALES").addTableTextCellAligned("TABLA_TOTALES",UtilidadTexto.formatearValores(mapDatosPac.get("valortotalfactura")+"") , report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_RIGHT);
        
        //fila
        logger.info("===>Convenio: "+mapDatosPac.get("convenio"));
        String tipoRegimenConvenio = Utilidades.obtenerCodigoTipoRegimenConvenio(con, mapDatosPac.get("convenio")+"");
        logger.info("===>Tipo Regimen: "+tipoRegimenConvenio);
        if(!tipoRegimenConvenio.equals(ConstantesBD.codigoTipoRegimenParticular))
        {
            report.getSectionReference("SECCION_TOTALES").setTableCellsColSpan(2);
            report.getSectionReference("SECCION_TOTALES").addTableTextCellAligned("TABLA_TOTALES","Total Responsable" , report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
            report.getSectionReference("SECCION_TOTALES").setTableCellsColSpan(1);
            report.getSectionReference("SECCION_TOTALES").addTableTextCellAligned("TABLA_TOTALES",UtilidadTexto.formatearValores(mapDatosPac.get("valorconvenio")+"") , report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_RIGHT);

            if(Utilidades.convertirADouble(mapDatosPac.get("valorfavorconvenio")+"")>0)
            {
                //fila
	            report.getSectionReference("SECCION_TOTALES").setTableCellsColSpan(2);
	            report.getSectionReference("SECCION_TOTALES").addTableTextCellAligned("TABLA_TOTALES","A Favor Responsable" , report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
	            report.getSectionReference("SECCION_TOTALES").setTableCellsColSpan(1);
	            report.getSectionReference("SECCION_TOTALES").addTableTextCellAligned("TABLA_TOTALES",UtilidadTexto.formatearValores(mapDatosPac.get("valorfavorconvenio")+"") , report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_RIGHT);
            }
            //fila
            report.getSectionReference("SECCION_TOTALES").setTableCellsColSpan(2);
            report.getSectionReference("SECCION_TOTALES").addTableTextCellAligned("TABLA_TOTALES","Total del Paciente" , report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
            report.getSectionReference("SECCION_TOTALES").setTableCellsColSpan(1);
            report.getSectionReference("SECCION_TOTALES").addTableTextCellAligned("TABLA_TOTALES",UtilidadTexto.formatearValores(mapDatosPac.get("valorbrutopaciente")+"") , report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_RIGHT);
        }
        else
        {
            //fila
            report.getSectionReference("SECCION_TOTALES").setTableCellsColSpan(2);
            report.getSectionReference("SECCION_TOTALES").addTableTextCellAligned("TABLA_TOTALES","Total del Paciente" , report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
            report.getSectionReference("SECCION_TOTALES").setTableCellsColSpan(1);
            report.getSectionReference("SECCION_TOTALES").addTableTextCellAligned("TABLA_TOTALES",UtilidadTexto.formatearValores(mapDatosPac.get("valortotalfactura")+"") , report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_RIGHT);
        }
        //fila
        report.getSectionReference("SECCION_TOTALES").setTableCellsColSpan(2);
        report.getSectionReference("SECCION_TOTALES").addTableTextCellAligned("TABLA_TOTALES","Total Abonos del Paciente" , report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
        report.getSectionReference("SECCION_TOTALES").setTableCellsColSpan(1);
        report.getSectionReference("SECCION_TOTALES").addTableTextCellAligned("TABLA_TOTALES",UtilidadTexto.formatearValores(mapDatosPac.get("valorabonos")+"") , report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_RIGHT);
        //fila
        report.getSectionReference("SECCION_TOTALES").setTableCellsColSpan(2);
        report.getSectionReference("SECCION_TOTALES").addTableTextCellAligned("TABLA_TOTALES","Total Descuento del Paciente" , report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
        report.getSectionReference("SECCION_TOTALES").setTableCellsColSpan(1);
        report.getSectionReference("SECCION_TOTALES").addTableTextCellAligned("TABLA_TOTALES",UtilidadTexto.formatearValores(mapDatosPac.get("valordescuentopaciente")+"") , report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_RIGHT);
        //fila
        report.getSectionReference("SECCION_TOTALES").setTableCellsColSpan(2);
        if(Double.parseDouble(mapDatosPac.get("valornetopaciente")+"")<0)  
            report.getSectionReference("SECCION_TOTALES").addTableTextCellAligned("TABLA_TOTALES","Valor Neto a Favor del Paciente" , report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);       
        else       
            report.getSectionReference("SECCION_TOTALES").addTableTextCellAligned("TABLA_TOTALES","Valor Neto a Cargo del Paciente" , report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
        report.getSectionReference("SECCION_TOTALES").setTableCellsColSpan(1);
        report.getSectionReference("SECCION_TOTALES").addTableTextCellAligned("TABLA_TOTALES",UtilidadTexto.formatearValores(mapDatosPac.get("valornetopaciente")+"" ), report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_RIGHT);
        report.addSectionToDocument("SECCION_TOTALES");
        
        //fila
        report.createSection("SECCION_VALORES_LETRAS","TABLA_VALORES_LETRAS",1,1,10);
        report.getSectionReference("SECCION_VALORES_LETRAS").setTableBorder("TABLA_VALORES_LETRAS", 0xFFFFFF, 0.0f);
        report.getSectionReference("SECCION_VALORES_LETRAS").getTableReference("TABLA_VALORES_LETRAS").setPadding(0.0f);
        report.getSectionReference("SECCION_VALORES_LETRAS").getTableReference("TABLA_VALORES_LETRAS").setSpacing(0.0f);
        report.getSectionReference("SECCION_VALORES_LETRAS").setTableCellBorderWidth("TABLA_VALORES_LETRAS", 0.0f);
        report.getSectionReference("SECCION_VALORES_LETRAS").setTableCellsDefaultColors("TABLA_VALORES_LETRAS", 0xFFFFFF, 0xFFFFFF);
        report.getSectionReference("SECCION_VALORES_LETRAS").setTableCellsColSpan(1);
        
        report.addSectionToDocument("SECCION_VALORES_LETRAS");
        
        String cadena="";
        logger.info("===>Tipo Regimen: "+tipoRegimenConvenio);
        if(!tipoRegimenConvenio.equals(ConstantesBD.codigoTipoRegimenParticular))
        {
        	if(Double.parseDouble(mapDatosPac.get("valorconvenio")+"" )>0)
        	{	
	        	cadena="VALOR DEL RESPONSABLE EN LETRAS:   "+UtilidadN2T.convertirLetras(UtilidadTexto.formatearValores(mapDatosPac.get("valorconvenio")+"" ).replaceAll(",", ""),ConstantesBD.cadenaUnidades,ConstantesBD.cadenaUnidadesDecimales)+" MCTE.";
	            report.getSectionReference("SECCION_VALORES_LETRAS").addTableTextCellAligned("TABLA_VALORES_LETRAS",cadena, report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
        	}    
        	if(Double.parseDouble(mapDatosPac.get("valorbrutopaciente")+"" )>0)
        	{
	            cadena="VALOR DEL PACIENTE EN LETRAS:   "+UtilidadN2T.convertirLetras(UtilidadTexto.formatearValores(mapDatosPac.get("valorbrutopaciente")+"" ).replaceAll(",", ""),ConstantesBD.cadenaUnidades,ConstantesBD.cadenaUnidadesDecimales)+" MCTE.";
	            report.getSectionReference("SECCION_VALORES_LETRAS").addTableTextCellAligned("TABLA_VALORES_LETRAS",cadena, report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
        	}    
        }
        else
        {
        	if(Double.parseDouble(mapDatosPac.get("valortotalfactura")+"" )>0)
        	{	
	        	cadena="VALOR DEL PACIENTE EN LETRAS:   "+UtilidadN2T.convertirLetras(UtilidadTexto.formatearValores(mapDatosPac.get("valortotalfactura")+"" ).replaceAll(",", ""),ConstantesBD.cadenaUnidades,ConstantesBD.cadenaUnidadesDecimales)+" MCTE.";
	            report.getSectionReference("SECCION_VALORES_LETRAS").addTableTextCellAligned("TABLA_VALORES_LETRAS",cadena, report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
        	}    
        }
        
        //***********Tarea 75069**************//
        HashMap<String, Object> mapaAux = new HashMap<String, Object>();
        mapaAux = mundoImpresion.consultarValorLetrasValor(con, mapDatosPac.get("convenio")+"");
        String letrasCargos=UtilidadN2T.convertirLetras(UtilidadTexto.formatearValores(mapDatosPac.get("valortotalfactura")+"" ).replaceAll(",", ""),ConstantesBD.cadenaUnidades,ConstantesBD.cadenaUnidadesDecimales)+" MCTE.";
        String letrasConvenio=UtilidadN2T.convertirLetras(UtilidadTexto.formatearValores(mapDatosPac.get("valorconvenio")+"" ).replaceAll(",", ""),ConstantesBD.cadenaUnidades,ConstantesBD.cadenaUnidadesDecimales)+" MCTE.";
        logger.info("\n\nVALOR LETRAS CARGOS>>>>>>>>>>JHEEEEEEEEE>>>>>>>>>"+letrasCargos);
        logger.info("\n\nVALOR LETRAS CONVENIO>>>>>>>>>>JHEEEEEEEEE>>>>>>>>>"+letrasConvenio);
        
        //fila
        report.createSection("SECCION_VALORES_LETRAS_PARAMETRO","TABLA_VALORES_LETRAS_PARAMETRO",1,1,10);
        report.getSectionReference("SECCION_VALORES_LETRAS_PARAMETRO").setTableBorder("TABLA_VALORES_LETRAS_PARAMETRO", 0xFFFFFF, 0.0f);
        report.getSectionReference("SECCION_VALORES_LETRAS_PARAMETRO").getTableReference("TABLA_VALORES_LETRAS_PARAMETRO").setPadding(0.0f);
        report.getSectionReference("SECCION_VALORES_LETRAS_PARAMETRO").getTableReference("TABLA_VALORES_LETRAS_PARAMETRO").setSpacing(0.0f);
        report.getSectionReference("SECCION_VALORES_LETRAS_PARAMETRO").setTableCellBorderWidth("TABLA_VALORES_LETRAS_PARAMETRO", 0.0f);
        report.getSectionReference("SECCION_VALORES_LETRAS_PARAMETRO").setTableCellsDefaultColors("TABLA_VALORES_LETRAS_PARAMETRO", 0xFFFFFF, 0xFFFFFF);
        report.font.setFontSizeAndAttributes(8,true,false,false);
        report.getSectionReference("SECCION_VALORES_LETRAS_PARAMETRO").setTableCellsColSpan(1);
                
        if((mapaAux.get("parametro")+"").equals(ConstantesIntegridadDominio.acronimoValorLetrasFacturaCargo))
        	report.getSectionReference("SECCION_VALORES_LETRAS_PARAMETRO").addTableTextCellAligned("TABLA_VALORES_LETRAS_PARAMETRO","TOTAL A PAGAR: "+letrasCargos, report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
        else
        {
        	if((mapaAux.get("parametro")+"").equals(ConstantesIntegridadDominio.acronimoValorLetrasFacturaConvenio))
            	report.getSectionReference("SECCION_VALORES_LETRAS_PARAMETRO").addTableTextCellAligned("TABLA_VALORES_LETRAS_PARAMETRO","TOTAL A PAGAR: "+letrasConvenio, report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
        }
        report.addSectionToDocument("SECCION_VALORES_LETRAS_PARAMETRO");
        //***********Fin Tarea***************//
        
        //fila FIXME se elimina seccion de Consulta externa 
        int viaIng=Utilidades.obtenerViaIngresoFactura(con,mapaFactura.get("codigoFactura")+"");
        if(viaIng==ConstantesBD.codigoViaIngresoConsultaExterna)
        {
        	if(ValoresPorDefecto.getCliente().equals(ConstantesBD.clienteSUBA)){
        		String codSubCuenta=Utilidades.obtenerCuentaFactura(con,mapaFactura.get("codigoFactura")+"");
        		HashMap citas=mundoImpresion.consultarInfoCitaDadaCuenta(con,codSubCuenta);
        		if(Integer.parseInt(citas.get("numRegistros")+"")>0){
        			report.createSection("SECCION_CITAS","TABLA_CITAS",Integer.parseInt(citas.get("numRegistros")+"")+1,6,10);
        			report.getSectionReference("SECCION_CITAS").setTableBorder("TABLA_CITAS", 0x000000, 1.0f);
        			report.getSectionReference("SECCION_CITAS").getTableReference("TABLA_CITAS").setPadding(0.0f);
        			report.getSectionReference("SECCION_CITAS").getTableReference("TABLA_CITAS").setSpacing(0.0f);
        			report.getSectionReference("SECCION_CITAS").setTableCellBorderWidth("TABLA_CITAS", 0.0f);
        			report.getSectionReference("SECCION_CITAS").setTableCellsDefaultColors("TABLA_CITAS", 0xFFFFFF, 0x000000);
        			//fila
        			report.font.setFontSizeAndAttributes(8,true,false,false);
        			//report.getSectionReference("SECCION_CITAS").setTableCellsRowSpan(1);
        			report.getSectionReference("SECCION_CITAS").setTableCellsColSpan(1);
        			report.getSectionReference("SECCION_CITAS").setTableCellPadding("TABLA_CITAS", 1.5f);//cam
        			report.getSectionReference("SECCION_CITAS").addTableTextCellAligned("TABLA_CITAS", "Unidad de Consulta", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_CENTER);
        			report.getSectionReference("SECCION_CITAS").addTableTextCellAligned("TABLA_CITAS", "Profesional de la Salud", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_CENTER);
        			report.getSectionReference("SECCION_CITAS").addTableTextCellAligned("TABLA_CITAS", "Fecha Cita", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_CENTER);
        			report.getSectionReference("SECCION_CITAS").addTableTextCellAligned("TABLA_CITAS", "Hora Cita", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_CENTER);
        			report.getSectionReference("SECCION_CITAS").addTableTextCellAligned("TABLA_CITAS", "Consultorio", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_CENTER);
        			report.getSectionReference("SECCION_CITAS").addTableTextCellAligned("TABLA_CITAS", "No. Cita.", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_CENTER);


        			for(int ci=0;ci<Integer.parseInt(citas.get("numRegistros")+"");ci++)
        			{
        				report.font.setFontSizeAndAttributes(8,false,false,false);
        				report.getSectionReference("SECCION_CITAS").addTableTextCellAligned("TABLA_CITAS", citas.get("unidadconsulta_"+ci)+"", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_CENTER);
        				report.getSectionReference("SECCION_CITAS").addTableTextCellAligned("TABLA_CITAS", citas.get("profesional_"+ci)+"", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_CENTER);
        				report.getSectionReference("SECCION_CITAS").addTableTextCellAligned("TABLA_CITAS", citas.get("fecha_"+ci)+"", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_CENTER);
        				report.getSectionReference("SECCION_CITAS").addTableTextCellAligned("TABLA_CITAS", citas.get("hora_"+ci)+"", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_CENTER);
        				report.getSectionReference("SECCION_CITAS").addTableTextCellAligned("TABLA_CITAS", citas.get("consultorio_"+ci)+"", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_CENTER);
        				report.getSectionReference("SECCION_CITAS").addTableTextCellAligned("TABLA_CITAS", citas.get("cita_"+ci)+"", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_CENTER);
        			}
        			report.addSectionToDocument("SECCION_CITAS");
        		}
        	}else{



        		report.createSection("SECCION_CITAS","TABLA_CITAS",4,6,10);        
        		report.getSectionReference("SECCION_CITAS").setTableBorder("TABLA_CITAS", 0x000000, 0.0f);
        		report.getSectionReference("SECCION_CITAS").getTableReference("TABLA_CITAS").setPadding(0.0f);
        		report.getSectionReference("SECCION_CITAS").getTableReference("TABLA_CITAS").setSpacing(1.0f);
        		report.getSectionReference("SECCION_CITAS").setTableCellBorderWidth("TABLA_CITAS", 0.0f);
        		report.getSectionReference("SECCION_CITAS").setTableCellsDefaultColors("TABLA_CITAS", 0xFFFFFF, 0x000000);
        		//fila
        		report.font.setFontSizeAndAttributes(8,true,false,false);
        		report.getSectionReference("SECCION_CITAS").setTableCellsRowSpan(1);
        		report.getSectionReference("SECCION_CITAS").setTableCellsColSpan(1);
        		report.getSectionReference("SECCION_CITAS").addTableTextCellAligned("TABLA_CITAS", "Unidad de Consulta", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_CENTER);
        		report.getSectionReference("SECCION_CITAS").addTableTextCellAligned("TABLA_CITAS", "Profesional de la Salud", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_CENTER);
        		report.getSectionReference("SECCION_CITAS").addTableTextCellAligned("TABLA_CITAS", "Fecha Cita", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_CENTER);
        		report.getSectionReference("SECCION_CITAS").addTableTextCellAligned("TABLA_CITAS", "Hora Cita", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_CENTER);
        		report.getSectionReference("SECCION_CITAS").addTableTextCellAligned("TABLA_CITAS", "Consultorio", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_CENTER);
        		report.getSectionReference("SECCION_CITAS").addTableTextCellAligned("TABLA_CITAS", "No. Cita.", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_CENTER);
        		String codSubCuenta=Utilidades.obtenerCuentaFactura(con,mapaFactura.get("codigoFactura")+"");
        		HashMap citas=mundoImpresion.consultarInfoCitaDadaCuenta(con,codSubCuenta);
        		for(int ci=0;ci<Integer.parseInt(citas.get("numRegistros")+"");ci++)
        		{
        			report.font.setFontSizeAndAttributes(8,false,false,false);
        			report.getSectionReference("SECCION_CITAS").addTableTextCellAligned("TABLA_CITAS", citas.get("unidadconsulta_"+ci)+"", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_CENTER);
        			report.getSectionReference("SECCION_CITAS").addTableTextCellAligned("TABLA_CITAS", citas.get("profesional_"+ci)+"", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_CENTER);
        			report.getSectionReference("SECCION_CITAS").addTableTextCellAligned("TABLA_CITAS", citas.get("fecha_"+ci)+"", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_CENTER);
        			report.getSectionReference("SECCION_CITAS").addTableTextCellAligned("TABLA_CITAS", citas.get("hora_"+ci)+"", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_CENTER);
        			report.getSectionReference("SECCION_CITAS").addTableTextCellAligned("TABLA_CITAS", citas.get("consultorio_"+ci)+"", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_CENTER);
        			report.getSectionReference("SECCION_CITAS").addTableTextCellAligned("TABLA_CITAS", citas.get("cita_"+ci)+"", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_CENTER);
        		}
        		report.addSectionToDocument("SECCION_CITAS");
        	}

        }

        //fila
        if(ValoresPorDefecto.getCliente().equals(ConstantesBD.clienteSUBA)){
        	report.createSection("SECCION_FOOTER","TABLA_FOOTER",4,2,25);  
        }else{
        	report.createSection("SECCION_FOOTER","TABLA_FOOTER",4,2,0);  
        }
        report.getSectionReference("SECCION_FOOTER").setTableBorder("TABLA_FOOTER", 0xFFFFFF, 0.0f);
        report.getSectionReference("SECCION_FOOTER").getTableReference("TABLA_FOOTER").setPadding(0.0f);
        report.getSectionReference("SECCION_FOOTER").getTableReference("TABLA_FOOTER").setSpacing(0.0f);
        report.getSectionReference("SECCION_FOOTER").setTableCellBorderWidth("TABLA_FOOTER", 0.0f);
        report.getSectionReference("SECCION_FOOTER").setTableCellsDefaultColors("TABLA_FOOTER", 0xFFFFFF, 0xFFFFFF);
        //fila
        report.font.setFontSizeAndAttributes(8,true,false,false);
        HashMap mapDatosInst=new HashMap();
        mapDatosInst=(HashMap)mapaFactura.get("headerDatosInstitucion");
        report.getSectionReference("SECCION_FOOTER").setTableCellsRowSpan(1);
        report.getSectionReference("SECCION_FOOTER").setTableCellsColSpan(2);
        report.getSectionReference("SECCION_FOOTER").addTableTextCellAligned("TABLA_FOOTER", "", report.font,iTextBaseDocument.ALIGNMENT_JUSTIFIED,iTextBaseDocument.ALIGNMENT_JUSTIFIED);
        report.getSectionReference("SECCION_FOOTER").addTableTextCellAligned("TABLA_FOOTER", "", report.font,iTextBaseDocument.ALIGNMENT_JUSTIFIED,iTextBaseDocument.ALIGNMENT_JUSTIFIED);
        report.getSectionReference("SECCION_FOOTER").setTableCellsRowSpan(1);
        report.getSectionReference("SECCION_FOOTER").setTableCellsColSpan(1);
        report.getSectionReference("SECCION_FOOTER").addTableTextCellAligned("TABLA_FOOTER", "____________________________________________", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_CENTER);
        report.getSectionReference("SECCION_FOOTER").addTableTextCellAligned("TABLA_FOOTER", "_________________________________", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_CENTER);
        report.getSectionReference("SECCION_FOOTER").addTableTextCellAligned("TABLA_FOOTER", "Firma Cajero ("+mapDatosPac.get("usuarioelabora")+").", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_CENTER);
        report.getSectionReference("SECCION_FOOTER").addTableTextCellAligned("TABLA_FOOTER", "Firma del Paciente. ", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_CENTER);
        report.getSectionReference("SECCION_FOOTER").setTableCellsRowSpan(1);
        report.getSectionReference("SECCION_FOOTER").setTableCellsColSpan(2);
        report.getSectionReference("SECCION_FOOTER").addTableTextCellAligned("TABLA_FOOTER", "______________________________________________________________________________________________________________________________________", report.font,iTextBaseDocument.ALIGNMENT_JUSTIFIED,iTextBaseDocument.ALIGNMENT_JUSTIFIED);
        
        report.getSectionReference("SECCION_FOOTER").addTableTextCellAligned("TABLA_FOOTER", "Usuario: "+usuario.getNombreUsuario(), report.font,iTextBaseDocument.ALIGNMENT_JUSTIFIED,iTextBaseDocument.ALIGNMENT_JUSTIFIED);
        
        String pie= FormatoImpresionFactura.obtenerEncabezadoPieFactura(mapaFactura.get("codigoFactura")+"", Double.parseDouble(mapaFactura.get("empresaInstitucion")+""))[1];
        report.getSectionReference("SECCION_FOOTER").addTableTextCellAligned("TABLA_FOOTER", pie, report.font,iTextBaseDocument.ALIGNMENT_JUSTIFIED,iTextBaseDocument.ALIGNMENT_JUSTIFIED);
        report.getSectionReference("SECCION_FOOTER").addTableTextCellAligned("TABLA_FOOTER", "______________________________________________________________________________________________________________________________________", report.font,iTextBaseDocument.ALIGNMENT_JUSTIFIED,iTextBaseDocument.ALIGNMENT_JUSTIFIED);
        report.getSectionReference("SECCION_FOOTER").addTableTextCellAligned("TABLA_FOOTER", tipoPiePagina.toUpperCase(), report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_JUSTIFIED);
        report.addSectionToDocument("SECCION_FOOTER");
    }


	
	
	//-------------------------------IMPRESION DE ANEXOS------------------------------------------////////
	/**
	 * @param usuario 
	 * @param impFactura 
	 * 
	 */
	 private static void generarImpresionAnexoMedicamentosFecha(String nombreArchivo, PersonaBasica paciente, UsuarioBasico usuario, ImpresionFactura impFactura, String codigoFactura, Connection con) 
	 {
		PdfReports report = new PdfReports();
		String filePath=ValoresPorDefecto.getFilePath();
		boolean titulos=false;
		
		 InstitucionBasica institucionBasica= new InstitucionBasica();
		 institucionBasica.cargarXConvenio(usuario.getCodigoInstitucionInt(), paciente.getCodigoConvenio());
		
		report.setEncabezadoAnexoMedicamentos1(institucionBasica.getLogoReportes(), institucionBasica.getRazonSocial(), institucionBasica.getNit(),paciente,codigoFactura,Utilidades.obtenerInformacionAnulacionFactura(con,codigoFactura));
		
		report.font.useFont(iTextBaseFont.FONT_COURIER, true, true, true);
		
		report.openReport(nombreArchivo);
		
		
		//********************************* Seccion de Anexos Por Fecha **************************************************/
		HashMap mapaFacturas=impFactura.anexoSolicitudesMedicamentosFechaFactura(con,codigoFactura);
        int numReg=Integer.parseInt(mapaFacturas.get("numRegistros")+"");
        double totalAnexo=0.0;
        report.createSection("seccionMedicamentos","tablaArticulos",1,4,0);
        report.getSectionReference("seccionMedicamentos").getTableReference("tablaArticulos").setPadding(0.0f);
        report.getSectionReference("seccionMedicamentos").getTableReference("tablaArticulos").setSpacing(0.0f);
        report.getSectionReference("seccionMedicamentos").setTableBorder("tablaArticulos", 0xFFFFFF, 0.0f);
        report.getSectionReference("seccionMedicamentos").setTableCellBorderWidth("tablaArticulos", 0.2f);
        report.getSectionReference("seccionMedicamentos").setTableCellsDefaultColors("tablaArticulos", 0xFFFFFF, 0xFFFFFF);
        for(int i=0;i<numReg;i++)
        {
	        report.font.setFontSizeAndAttributes(8,true,false,false);
	        report.getSectionReference("seccionMedicamentos").setTableCellsColSpan(4);
	        report.getSectionReference("seccionMedicamentos").addTableTextCellAligned("tablaArticulos", "Órden de Medicamentos del día "+UtilidadFecha.conversionFormatoFechaAAp(mapaFacturas.get("fechasolicitud_"+i)+""),report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
	        HashMap mapaArticulos = new HashMap();
	        mapaArticulos=(HashMap)mapaFacturas.get("detalleArticulos_"+i);
	        int numRegArticulos=Integer.parseInt(mapaArticulos.get("numRegistros")+"");
	        report.getSectionReference("seccionMedicamentos").setTableCellsColSpan(1);
	        if(!titulos)
	        {
	        	report.getSectionReference("seccionMedicamentos").addTableTextCellAligned("tablaArticulos", "Articulo",report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_CENTER);
	        	report.getSectionReference("seccionMedicamentos").addTableTextCellAligned("tablaArticulos", "Valor Unitario",report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_CENTER);
	        	report.getSectionReference("seccionMedicamentos").addTableTextCellAligned("tablaArticulos", "Cantidad",report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_CENTER);
	        	report.getSectionReference("seccionMedicamentos").addTableTextCellAligned("tablaArticulos", "Valor Total",report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_CENTER);
	        	titulos=true;
	        }
	        report.font.setFontSizeAndAttributes(8,false,false,false);
	        for(int j=0;j<numRegArticulos;j++)
	        {
	        	report.getSectionReference("seccionMedicamentos").addTableTextCellAligned("tablaArticulos", ""+mapaArticulos.get("articulo_"+j),report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
  	        	report.getSectionReference("seccionMedicamentos").addTableTextCellAligned("tablaArticulos", UtilidadTexto.formatearValores(mapaArticulos.get("valorunitario_"+j)+""),report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_RIGHT);
  	        	report.getSectionReference("seccionMedicamentos").addTableTextCellAligned("tablaArticulos", mapaArticulos.get("cantidad_"+j)+"",report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_CENTER);
  	        	report.getSectionReference("seccionMedicamentos").addTableTextCellAligned("tablaArticulos", ""+UtilidadTexto.formatearValores(mapaArticulos.get("valortotal_"+j)+""),report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_RIGHT);
  	        	totalAnexo+=Double.parseDouble(mapaArticulos.get("valortotal_"+j)+"");
	        }
	        report.font.setFontSizeAndAttributes(8,true,false,false);
	        report.getSectionReference("seccionMedicamentos").setTableCellsColSpan(4);
        	report.getSectionReference("seccionMedicamentos").addTableTextCellAligned("tablaArticulos","________________________________________________________________________________________________________________", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
        	report.getSectionReference("seccionMedicamentos").setTableCellsColSpan(3);
        	report.getSectionReference("seccionMedicamentos").addTableTextCellAligned("tablaArticulos", "Total Órdenes de Medicamentos del día "+UtilidadFecha.conversionFormatoFechaAAp(mapaFacturas.get("fechasolicitud_"+i)+""), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
	        report.getSectionReference("seccionMedicamentos").setTableCellsColSpan(1);
	        report.getSectionReference("seccionMedicamentos").addTableTextCellAligned("tablaArticulos", UtilidadTexto.formatearValores(mapaFacturas.get("totalsolicitudes_"+i)+""), report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_RIGHT);
        }
        report.font.setFontSizeAndAttributes(8,true,false,false);
        report.getSectionReference("seccionMedicamentos").setTableCellsColSpan(4);
    	report.getSectionReference("seccionMedicamentos").addTableTextCellAligned("tablaArticulos","________________________________________________________________________________________________________________", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
    	report.getSectionReference("seccionMedicamentos").setTableCellsColSpan(3);
    	report.getSectionReference("seccionMedicamentos").addTableTextCellAligned("tablaArticulos", "Total Anexo ", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
        report.getSectionReference("seccionMedicamentos").setTableCellsColSpan(1);
        report.getSectionReference("seccionMedicamentos").addTableTextCellAligned("tablaArticulos", UtilidadTexto.formatearValores(totalAnexo), report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_RIGHT);
        float widths[]={(float) 5.5,(float) 1.8,(float) 1.2,(float) 1.5};
		try 
		{
			report.getSectionReference("seccionMedicamentos").getTableReference("tablaArticulos").setWidths(widths);
		}
		catch (BadElementException e) 
		{
			e.printStackTrace();
		}
        report.addSectionToDocument("seccionMedicamentos");
        
        
		report.closeReport(); 
	}
	 
	 
	 private static void generarImpresionAnexoMedicamentosOrden(String nombreArchivo, PersonaBasica paciente, UsuarioBasico usuario, ImpresionFactura impFactura, String codigoFactura, Connection con) 
	 {
		PdfReports report = new PdfReports();
		boolean titulos=false;
		
		InstitucionBasica institucionBasica= new InstitucionBasica();
	    institucionBasica.cargarXConvenio(usuario.getCodigoInstitucionInt(), paciente.getCodigoConvenio());
		
		String filePath=ValoresPorDefecto.getFilePath();
		report.setEncabezadoAnexoMedicamentos1(institucionBasica.getLogoReportes(), institucionBasica.getRazonSocial(), institucionBasica.getNit(),paciente,codigoFactura,Utilidades.obtenerInformacionAnulacionFactura(con,codigoFactura));
		
		report.font.useFont(iTextBaseFont.FONT_COURIER, true, true, true);
		
		report.openReport(nombreArchivo);
		
		
		//********************************* Seccion de Anexos Por Orden **************************************************/
		HashMap mapaFacturas=impFactura.anexoSolicitudesMedicamentosOrdenFechaFactura(con,codigoFactura);
        int numReg=Integer.parseInt(mapaFacturas.get("numRegistros")+"");
        double totalAnexo=0.0;
        report.createSection("seccionMedicamentos","tablaArticulos",1,4,0);
        report.getSectionReference("seccionMedicamentos").getTableReference("tablaArticulos").setPadding(0.0f);
        report.getSectionReference("seccionMedicamentos").getTableReference("tablaArticulos").setSpacing(0.0f);
        report.getSectionReference("seccionMedicamentos").setTableBorder("tablaArticulos", 0xFFFFFF, 0.0f);
        report.getSectionReference("seccionMedicamentos").setTableCellBorderWidth("tablaArticulos", 0.2f);
        report.getSectionReference("seccionMedicamentos").setTableCellsDefaultColors("tablaArticulos", 0xFFFFFF, 0xFFFFFF);
        for(int i = 0 ; i < numReg ; i++)
        {
	        report.font.setFontSizeAndAttributes(8,true,false,false);
	        report.getSectionReference("seccionMedicamentos").setTableCellsColSpan(4);
	        report.getSectionReference("seccionMedicamentos").addTableTextCellAligned("tablaArticulos", "Órden de Medicamentos del día "+UtilidadFecha.conversionFormatoFechaAAp(mapaFacturas.get("fechasolicitud_"+i)+""),report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
	        
	        HashMap mapaOrdenes = new HashMap();
	        mapaOrdenes=(HashMap)mapaFacturas.get("detalleOrden_"+i);
	        int numRegOrdenes=Integer.parseInt(mapaOrdenes.get("numRegistros")+"");
	        
	        for(int k = 0 ; k < numRegOrdenes ; k++)
	        {
	        	report.font.setFontSizeAndAttributes(8,true,false,false);
	        	report.getSectionReference("seccionMedicamentos").setTableCellsColSpan(4);
		        report.getSectionReference("seccionMedicamentos").addTableTextCellAligned("tablaArticulos", "Órden "+mapaOrdenes.get("consecutivoorden_"+k),report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
		        HashMap mapaArticulos = new HashMap();
		        mapaArticulos=(HashMap)mapaOrdenes.get("detalleArticulos_"+k);
		        int numRegArticulos=Integer.parseInt(mapaArticulos.get("numRegistros")+"");
		        report.getSectionReference("seccionMedicamentos").setTableCellsColSpan(1);
		        if(!titulos)
		        {
		        	report.getSectionReference("seccionMedicamentos").addTableTextCellAligned("tablaArticulos", "Articulo",report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_CENTER);
		        	report.getSectionReference("seccionMedicamentos").addTableTextCellAligned("tablaArticulos", "Valor Unitario",report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_CENTER);
		        	report.getSectionReference("seccionMedicamentos").addTableTextCellAligned("tablaArticulos", "Cantidad",report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_CENTER);
		        	report.getSectionReference("seccionMedicamentos").addTableTextCellAligned("tablaArticulos", "Valor Total",report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_CENTER);
		        	titulos=true;
		        }
		        report.font.setFontSizeAndAttributes(8,false,false,false);
		        for(int j = 0 ; j < numRegArticulos ; j++)
		        {
		        	report.getSectionReference("seccionMedicamentos").addTableTextCellAligned("tablaArticulos", ""+mapaArticulos.get("articulo_"+j),report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
	  	        	report.getSectionReference("seccionMedicamentos").addTableTextCellAligned("tablaArticulos", UtilidadTexto.formatearValores(mapaArticulos.get("valorunitario_"+j)+""),report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_RIGHT);
	  	        	report.getSectionReference("seccionMedicamentos").addTableTextCellAligned("tablaArticulos", mapaArticulos.get("cantidad_"+j)+"",report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_CENTER);
	  	        	report.getSectionReference("seccionMedicamentos").addTableTextCellAligned("tablaArticulos", ""+UtilidadTexto.formatearValores(mapaArticulos.get("valortotal_"+j)+""),report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_RIGHT);
	  	        	totalAnexo+=Double.parseDouble(mapaArticulos.get("valortotal_"+j)+"");
		        }
		        report.getSectionReference("seccionMedicamentos").setTableCellsColSpan(4);
	        	report.getSectionReference("seccionMedicamentos").addTableTextCellAligned("tablaArticulos","_____________________________________________________________________________________________________________", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
	        	report.font.setFontSizeAndAttributes(8,true,false,false);
	        	report.getSectionReference("seccionMedicamentos").setTableCellsColSpan(3);
	        	report.getSectionReference("seccionMedicamentos").addTableTextCellAligned("tablaArticulos", "Total Órden", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
		        report.getSectionReference("seccionMedicamentos").setTableCellsColSpan(1);
		        report.getSectionReference("seccionMedicamentos").addTableTextCellAligned("tablaArticulos", UtilidadTexto.formatearValores(mapaOrdenes.get("totalsolicitud_"+k)+""), report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_RIGHT);
	        }
	        
	        report.font.setFontSizeAndAttributes(8,true,false,false);
	        report.getSectionReference("seccionMedicamentos").setTableCellsColSpan(4);
        	report.getSectionReference("seccionMedicamentos").addTableTextCellAligned("tablaArticulos","________________________________________________________________________________________________________________", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
        	report.getSectionReference("seccionMedicamentos").setTableCellsColSpan(3);
        	report.getSectionReference("seccionMedicamentos").addTableTextCellAligned("tablaArticulos", "Total Órdenes de Medicamentos del día "+UtilidadFecha.conversionFormatoFechaAAp(mapaFacturas.get("fechasolicitud_"+i)+""), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
	        report.getSectionReference("seccionMedicamentos").setTableCellsColSpan(1);
	        report.getSectionReference("seccionMedicamentos").addTableTextCellAligned("tablaArticulos", UtilidadTexto.formatearValores(mapaFacturas.get("totalsolicitudes_"+i)+""), report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_RIGHT);
	        
        }
        report.font.setFontSizeAndAttributes(8,true,false,false);
        report.getSectionReference("seccionMedicamentos").setTableCellsColSpan(4);
    	report.getSectionReference("seccionMedicamentos").addTableTextCellAligned("tablaArticulos","________________________________________________________________________________________________________________", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
    	report.getSectionReference("seccionMedicamentos").setTableCellsColSpan(3);
    	report.getSectionReference("seccionMedicamentos").addTableTextCellAligned("tablaArticulos", "Total Anexo ", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
        report.getSectionReference("seccionMedicamentos").setTableCellsColSpan(1);
        report.getSectionReference("seccionMedicamentos").addTableTextCellAligned("tablaArticulos", UtilidadTexto.formatearValores(totalAnexo), report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_RIGHT);
        float widths[]={(float) 5.5,(float) 1.8,(float) 1.2,(float) 1.5};
		try 
		{
			report.getSectionReference("seccionMedicamentos").getTableReference("tablaArticulos").setWidths(widths);
		}
		catch (BadElementException e) 
		{
			e.printStackTrace();
		}
        report.addSectionToDocument("seccionMedicamentos");
        
        
		report.closeReport(); 
	}

	 
	 /**
	  * Metodo para imprimir la prefactura.
	  * @param paciente
	  * @param usuario
	  * @param mapaImprimir
	  * @param con
	  * @return
	  */
	public static Vector imprimirPrefactura(PersonaBasica paciente, UsuarioBasico usuario, HashMap mapaPrefactura, DtoFactura dtoFactura, String codigoCuenta) throws IPSException
	{
		PdfReports report = new PdfReports();
		Vector archivosGenerados=new Vector();
		String nombreArchivo;
        Random r= new Random();        
        nombreArchivo= "/aBorrar" + r.nextInt() + "_prefact.pdf";
        archivosGenerados.add(nombreArchivo);
    	nombreArchivo=ValoresPorDefecto.getFilePath() + nombreArchivo;
		String filePath=ValoresPorDefecto.getFilePath();
		
		Connection con= UtilidadBD.abrirConexion();
		String nombreViaIngreso= Utilidades.obtenerNombreViaIngreso(con, Cuenta.obtenerCodigoViaIngresoCuenta(con, codigoCuenta+""));
		UtilidadBD.closeConnection(con);
		
		InstitucionBasica institucionBasica= new InstitucionBasica();
	    institucionBasica.cargarXConvenio(usuario.getCodigoInstitucionInt(), paciente.getCodigoConvenio());
		
		report.setReportBaseHeaderPrefactura1(institucionBasica.getLogoReportes(), institucionBasica.getRazonSocial(), institucionBasica.getNit(), "IMPRESION PREFACTURA",paciente.getNombrePersona(false),paciente.getTipoIdentificacionPersona(false)+" "+paciente.getNumeroIdentificacionPersona(),dtoFactura.getConvenio().getNombre()+"",dtoFactura.getCuentas().get(0)+"", nombreViaIngreso);
		
		report.font.useFont(iTextBaseFont.FONT_COURIER, true, true, true);
		
		report.openReport(nombreArchivo);
		report.document.addParagraph("", 8);
		report.document.addParagraph("E S T E    D O C U M E N T O    E S    I N F O R M A T I V O    Y    R E F L E J A    E L    E S T A D O    D E    LA    C U E N T A.    N O    E S    U N A    F A C T U R A    D E    V E N T A", 12);
		report.document.addParagraph("", 8);
		
		report.createSection("seccionDetalle","tablaSeccionDetalle",1,6,10);
		report.getSectionReference("seccionDetalle").setTableBorderColor("tablaSeccionDetalle",C_NEGRO);
		report.getSectionReference("seccionDetalle").setTableBorder("tablaSeccionDetalle", C_NEGRO, 0.1f);
        report.getSectionReference("seccionDetalle").setTableCellBorderWidth("tablaSeccionDetalle", 0.1f);
        report.getSectionReference("seccionDetalle").setTableCellsDefaultColors("tablaSeccionDetalle", C_BLANCO, C_NEGRO);
        
        //fila 
        report.font.setFontSizeAndAttributes(8,true,false,false);
        report.getSectionReference("seccionDetalle").setTableCellsColSpan(1);
        report.getSectionReference("seccionDetalle").addTableTextCellAligned("tablaSeccionDetalle", "Área", report.font,iTextBaseDocument.ALIGNMENT_JUSTIFIED,iTextBaseDocument.ALIGNMENT_JUSTIFIED);
        report.getSectionReference("seccionDetalle").addTableTextCellAligned("tablaSeccionDetalle", "Cod", report.font,iTextBaseDocument.ALIGNMENT_JUSTIFIED,iTextBaseDocument.ALIGNMENT_JUSTIFIED);
        report.getSectionReference("seccionDetalle").addTableTextCellAligned("tablaSeccionDetalle", "Servicio", report.font,iTextBaseDocument.ALIGNMENT_JUSTIFIED,iTextBaseDocument.ALIGNMENT_JUSTIFIED);
        report.getSectionReference("seccionDetalle").addTableTextCellAligned("tablaSeccionDetalle", "Cantidad", report.font,iTextBaseDocument.ALIGNMENT_JUSTIFIED,iTextBaseDocument.ALIGNMENT_JUSTIFIED);
        report.getSectionReference("seccionDetalle").addTableTextCellAligned("tablaSeccionDetalle", "Vr. Unitario", report.font,iTextBaseDocument.ALIGNMENT_JUSTIFIED,iTextBaseDocument.ALIGNMENT_JUSTIFIED);
        report.getSectionReference("seccionDetalle").addTableTextCellAligned("tablaSeccionDetalle", "Vr. Total", report.font,iTextBaseDocument.ALIGNMENT_JUSTIFIED,iTextBaseDocument.ALIGNMENT_JUSTIFIED);
        //fila 
        report.font.setFontSizeAndAttributes(8,false,false,false);
        report.getSectionReference("seccionDetalle").setTableCellsColSpan(1);
        logger.info("numReg---->"+mapaPrefactura.get("numRegistros")+"");
        
        for(int i=0;i<Integer.parseInt(mapaPrefactura.get("numRegistros")+"");i++)
        {
        	logger.info("*******************************************************************************************************************");
        	logger.info("*******************************************************************************************************************");
        	logger.info("1->"+mapaPrefactura.get("nombrecentrocosto_"+i)+"<--");
        	logger.info("2->"+mapaPrefactura.get("codarticuloservicio_"+i)+"");
        	logger.info("3->"+mapaPrefactura.get("descarticuloservicio_"+i)+"");
        	logger.info("4->"+mapaPrefactura.get("cantidadcargo_"+i)+"");
        	logger.info("5->"+util.UtilidadTexto.formatearValores(mapaPrefactura.get("valorunitario_"+i)+""));
        	logger.info("6->"+util.UtilidadTexto.formatearValores(mapaPrefactura.get("valortotal_"+i)+""));
        	logger.info("*******************************************************************************************************************");
        	logger.info("*******************************************************************************************************************");
        	String centroC= (mapaPrefactura.get("nombrecentrocosto_"+i)+"").replaceAll("                       ", "");
	        report.getSectionReference("seccionDetalle").addTableTextCellAligned("tablaSeccionDetalle", centroC, report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
	        report.getSectionReference("seccionDetalle").addTableTextCellAligned("tablaSeccionDetalle", mapaPrefactura.get("codarticuloservicio_"+i)+"", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_CENTER);
	        report.getSectionReference("seccionDetalle").addTableTextCellAligned("tablaSeccionDetalle", mapaPrefactura.get("descarticuloservicio_"+i)+"", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
	        report.getSectionReference("seccionDetalle").addTableTextCellAligned("tablaSeccionDetalle", mapaPrefactura.get("cantidadcargo_"+i)+"", report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_CENTER);
	        report.getSectionReference("seccionDetalle").addTableTextCellAligned("tablaSeccionDetalle", util.UtilidadTexto.formatearValores(mapaPrefactura.get("valorunitario_"+i)+""), report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_RIGHT);
	        report.getSectionReference("seccionDetalle").addTableTextCellAligned("tablaSeccionDetalle", util.UtilidadTexto.formatearValores(mapaPrefactura.get("valortotal_"+i)+""), report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_RIGHT);
        }
        float widths[]={(float) 1.4,(float) 0.6,(float) 4.4,(float) 1,(float) 1.3,(float) 1.3};
		try 
		{
			report.getSectionReference("seccionDetalle").getTableReference("tablaSeccionDetalle").setWidths(widths);
		}
		catch (BadElementException e) 
		{
			e.printStackTrace();
		}
        report.addSectionToDocument("seccionDetalle");
        
        report.createSection("seccionTotales","tablaTotales",1,2,0);
        report.font.setFontSizeAndAttributes(8,true,false,false);
        report.getSectionReference("seccionTotales").setTableBorder("tablaTotales", C_NEGRO, 0.0f);
        report.getSectionReference("seccionTotales").setTableCellBorderWidth("tablaTotales", 0.0f);
        report.getSectionReference("seccionTotales").setTableCellsDefaultColors("tablaTotales", C_BLANCO, C_BLANCO);
        report.getSectionReference("seccionTotales").addTableTextCellAligned("tablaTotales", "Total Cargos", report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_RIGHT);
        report.getSectionReference("seccionTotales").addTableTextCellAligned("tablaTotales", util.UtilidadTexto.formatearValores(dtoFactura.getValorTotal()), report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_RIGHT);
        report.getSectionReference("seccionTotales").addTableTextCellAligned("tablaTotales", "Total Convenio", report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_RIGHT);
        report.getSectionReference("seccionTotales").addTableTextCellAligned("tablaTotales", util.UtilidadTexto.formatearValores(dtoFactura.getValorConvenio()), report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_RIGHT);
        report.getSectionReference("seccionTotales").addTableTextCellAligned("tablaTotales", "Total Paciente", report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_RIGHT);
        report.getSectionReference("seccionTotales").addTableTextCellAligned("tablaTotales", util.UtilidadTexto.formatearValores(dtoFactura.getValorBrutoPac()), report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_RIGHT);
        report.getSectionReference("seccionTotales").addTableTextCellAligned("tablaTotales", "Abonos Paciente", report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_RIGHT);
        report.getSectionReference("seccionTotales").addTableTextCellAligned("tablaTotales", util.UtilidadTexto.formatearValores(dtoFactura.getValorAbonos()), report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_RIGHT);
        report.getSectionReference("seccionTotales").addTableTextCellAligned("tablaTotales", "Descuento Paciente", report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_RIGHT);
        report.getSectionReference("seccionTotales").addTableTextCellAligned("tablaTotales", util.UtilidadTexto.formatearValores(dtoFactura.getValorAbonos()), report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_RIGHT);
        report.getSectionReference("seccionTotales").addTableTextCellAligned("tablaTotales", "Neto a Cargo del Paciente", report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_RIGHT);
        report.getSectionReference("seccionTotales").addTableTextCellAligned("tablaTotales",  util.UtilidadTexto.formatearValores(dtoFactura.getValorNetoPaciente()), report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_RIGHT);
        
        float width[]={(float) 8,(float) 2};
		try 
		{
			report.getSectionReference("seccionTotales").getTableReference("tablaTotales").setWidths(width);
		}
		catch (BadElementException e) 
		{
			e.printStackTrace();
		}
        
		report.addSectionToDocument("seccionTotales");
		report.closeReport();
		return archivosGenerados;
		
	}
	
	
}
