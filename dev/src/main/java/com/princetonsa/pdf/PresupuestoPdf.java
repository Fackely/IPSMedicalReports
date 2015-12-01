/** 
 * @(#)PresupuestoPdf.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_04
 *
 */

package com.princetonsa.pdf;

import java.sql.Connection;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.Listado;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.Administracion.UtilConversionMonedas;
import util.pdf.PdfReports;
import util.pdf.iTextBaseDocument;
import util.pdf.iTextBaseFont;

import com.princetonsa.dto.administracion.DtoTiposMoneda;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.presupuesto.ConsultaPresupuesto;
import com.princetonsa.mundo.presupuesto.FormatoImpresionPresupuesto;

/**
 * Clase para manejar la generación de PDF's para
 * la consulta impresion de presupuesto
 * 
 *	
 *  @author <a href="mailto:cperalta@princetonsa.com">Carlos Peralta</a>
 */
public class PresupuestoPdf
{
	/**
     * Clase para manejar los logs de esta clase
     */
    private static Logger logger=Logger.getLogger(PresupuestoPdf.class);
    
    
    
    /**
     * Metodo para imprimir el Presupuesto completo según el Formato de 
     * Impresión de Presupuesto escogido
     * @param filename
     * @param forma
     * @param usuario
     * @param paciente
     */
	public static void pdfPresupuesto(String filename, Connection con, UsuarioBasico usuario, int formato, int codigoPresupuesto,int index,HashMap mapaMoneda, HttpServletRequest request)
	{
		
		ConsultaPresupuesto mundo = new ConsultaPresupuesto();
		FormatoImpresionPresupuesto mundoFormato = new FormatoImpresionPresupuesto();
		
		int presupuesto = codigoPresupuesto;
		int codigoFormato = formato;
		/**Mapas para la informacion parametrizada**/
		HashMap mapaParamPresupuestoBasico = new HashMap();
		HashMap mapaParamServicios = new HashMap();
		HashMap mapaParamArticulos = new HashMap();
		/**Mapas para la informacion del presupuesto**/
		HashMap mapaInfoPresupuestoBasico = new HashMap();
		HashMap mapaInfoServicios = new HashMap();
		HashMap mapaInfoArticulos = new HashMap();
		HashMap mapaInfoArticulosMedicamentos = new HashMap();
		HashMap mapaInfoArticulosInsumos = new HashMap();
		HashMap mapaInfoIntervenciones = new HashMap();
		HashMap tiposMonedaTagMap=new HashMap();
		DtoTiposMoneda dto=UtilConversionMonedas.obtenerTipoMonedaManejaInstitucion(usuario.getCodigoInstitucionInt());
		
		try
		{
			/**Consultamos la imformacion aprametrizada para el formato de impresion de rpesupuesto escogido**/
			mapaParamPresupuestoBasico = mundoFormato.consultarFormatoImpresion(con, codigoFormato);
			mapaParamServicios = mundoFormato.consultarDetServicios(con, codigoFormato);
			mapaParamArticulos = mundoFormato.consultarDetAritculos(con, codigoFormato);
			
			/**Consultamos los datos del presupuesto**/
			mapaInfoPresupuestoBasico = mundo.consultarDetallePresupuesto(con, presupuesto);
			mapaInfoServicios = mundo.consultarServicios(con, presupuesto);
			mapaInfoArticulos = mundo.consultarArticulos(con, presupuesto);
			mapaInfoArticulosMedicamentos = mundo.consultarArticulosMedIns(con, presupuesto, true);
			mapaInfoArticulosInsumos = mundo.consultarArticulosMedIns(con, presupuesto, false);
			mapaInfoIntervenciones = mundo.consultarIntenervenciones(con, presupuesto);
			
			/**
			 * Consultar los tipos de moneda
			 */
			int institucion=usuario.getCodigoInstitucionInt();
			tiposMonedaTagMap=UtilConversionMonedas.obtenerTiposMonedaTagMap(institucion, false);
			
		}
		catch(Exception e)
		{
			logger.warn("Problemas cargando la informacion de los datos parametrizados y de los datos que se desean imprimir");
		}
		
		//logger.info("\n\n\nMAPA INFO ARTICULOS: "+mapaParamArticulos);
		
		
		PdfReports report = new PdfReports(mapaParamPresupuestoBasico.get("fechahora_0")+"");
		/**Numero de Registros de todos los mapas que se van a utilizar**/
		int numRegServParam=mapaParamServicios.containsKey("numRegistros")?Integer.parseInt(mapaParamServicios.get("numRegistros")+""):0;
        //int numRegArtParam=mapaParamArticulos.containsKey("numRegistros")?Integer.parseInt(mapaParamArticulos.get("numRegistros")+""):0;
        int numRegServInfo=mapaInfoServicios.containsKey("numRegistros")?Integer.parseInt(mapaInfoServicios.get("numRegistros")+""):0;
        int numRegArtInfo=mapaInfoArticulos.containsKey("numRegistros")?Integer.parseInt(mapaInfoArticulos.get("numRegistros")+""):0;
        
		String filePath=ValoresPorDefecto.getFilePath();
		String nombresApellidos=mapaInfoPresupuestoBasico.get("primerapellido_0")+" "+mapaInfoPresupuestoBasico.get("segundoapellido_0")+" "+mapaInfoPresupuestoBasico.get("primernombre_0")+" "+mapaInfoPresupuestoBasico.get("segundonombre_0")+"";
		String tipoNumeroId=mapaInfoPresupuestoBasico.get("tipoid_0")+"  "+mapaInfoPresupuestoBasico.get("numeroid_0")+"";
		
		InstitucionBasica institucionBasica = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
		report.setReportBaseHeader1(institucionBasica.getLogoReportes(), institucionBasica.getRazonSocial(), institucionBasica.getNit(), (String)mapaParamPresupuestoBasico.get("tituloformato_0")+"".toUpperCase(),nombresApellidos, tipoNumeroId, presupuesto+"", mapaInfoPresupuestoBasico.get("fechapresupuesto_0")+"");
	    

	    report.font.useFont(iTextBaseFont.FONT_COURIER, false, true, true);
	    report.openReport(filename);
	    report.font.useFont(iTextBaseFont.FONT_TIMES, false, false, false);
	    report.setReportTitleSectionAtributes(0x000000, 0xFFFFFF,0x000000, 14);
	    report.setReportDataSectionAtributes(0x000000, 0xFFFFFF, 0x000000, 12);
	    
	    /**********************************Seccion de la informacion personal del paciente**********************************/
	    report.createSection("paciente","tablaPaciente",2,4,0);
	    report.getSectionReference("paciente").getTableReference("tablaPaciente").setPadding(0.0f);
        report.getSectionReference("paciente").getTableReference("tablaPaciente").setSpacing(0.0f);
        report.getSectionReference("paciente").setTableBorder("tablaPaciente", 0xFFFFFF, 0.0f);
        report.getSectionReference("paciente").setTableCellBorderWidth("tablaPaciente", 0.2f);
        report.getSectionReference("paciente").setTableCellsDefaultColors("tablaPaciente", 0xFFFFFF, 0xFFFFFF);

        report.font.setFontSizeAndAttributes(8,false,false,false);
        String[] temp=((String)mapaInfoPresupuestoBasico.get("fechanacimiento_0")+"").split("/");
        String edad=UtilidadFecha.calcularEdadDetallada(Integer.parseInt(temp[2]), Integer.parseInt(temp[1]), Integer.parseInt(temp[0]),UtilidadFecha.getMesAnioDiaActual("dia"), UtilidadFecha.getMesAnioDiaActual("mes"),UtilidadFecha.getMesAnioDiaActual("anio"));
        report.getSectionReference("paciente").setTableCellsColSpan(1);
        report.getSectionReference("paciente").addTableTextCell("tablaPaciente", "Edad: "+edad, report.font);
        report.getSectionReference("paciente").addTableTextCell("tablaPaciente", "Sexo: "+mapaInfoPresupuestoBasico.get("sexo_0"), report.font);
        report.getSectionReference("paciente").setTableCellsColSpan(2);
        report.getSectionReference("paciente").addTableTextCell("tablaPaciente", "Teléfono: "+mapaInfoPresupuestoBasico.get("telefono_0"), report.font);
        report.getSectionReference("paciente").setTableCellsColSpan(4);
        report.getSectionReference("paciente").addTableTextCell("tablaPaciente", "Dirección: "+mapaInfoPresupuestoBasico.get("direccion_0"), report.font);
        
        report.font.setFontSizeAndAttributes(10,false,false,false);
        report.getSectionReference("paciente").setTableCellsColSpan(1);
        report.getSectionReference("paciente").addTableTextCellAligned("tablaPaciente", "Responsable: ",report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
        report.font.setFontSizeAndAttributes(8,false,false,false);
        report.getSectionReference("paciente").setTableCellsColSpan(3);
        report.getSectionReference("paciente").addTableTextCellAligned("tablaPaciente", ""+mapaInfoPresupuestoBasico.get("convenio_0"), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
        report.setReportDataSectionAtributes(0xFFFFFF, 0xFFFFFF, 0x000000,8);
        report.setReportTitleSectionAtributes(0x000000, 0xFFFFFF,0x000000,8);
        report.addSectionToDocument("paciente");
        /********************************** FIN Seccion de la informacion personal del paciente**********************************/
        
        /********************************* Seccion de las Intervenciones **************************************************/
        int numRegIntervenciones=mapaInfoIntervenciones.containsKey("numRegistros")?Integer.parseInt(mapaInfoIntervenciones.get("numRegistros")+""):0;
        report.createSection("intervenciones","tablaIntervenciones",1,2,0);
        report.getSectionReference("intervenciones").getTableReference("tablaIntervenciones").setPadding(0.0f);
        report.getSectionReference("intervenciones").getTableReference("tablaIntervenciones").setSpacing(0.0f);
        report.getSectionReference("intervenciones").setTableBorder("tablaIntervenciones", 0xFFFFFF, 0.0f);
        report.getSectionReference("intervenciones").setTableCellBorderWidth("tablaIntervenciones", 0.2f);
        report.getSectionReference("intervenciones").setTableCellsDefaultColors("tablaIntervenciones", 0xFFFFFF, 0xFFFFFF);
        report.font.setFontSizeAndAttributes(10,false,false,false);
        report.getSectionReference("intervenciones").setTableCellsColSpan(2);
        report.getSectionReference("intervenciones").addTableTextCellAligned("tablaIntervenciones", "Intervenciones",report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
        if(numRegIntervenciones>0)
        {
	        report.font.setFontSizeAndAttributes(8,false,false,false);
	        report.getSectionReference("intervenciones").setTableCellsColSpan(2);
	        for(int i = 0; i < Integer.parseInt(mapaInfoIntervenciones.get("numRegistros")+"") ; i++)
	        {
	        	report.getSectionReference("intervenciones").addTableTextCellAligned("tablaIntervenciones", ""+mapaInfoIntervenciones.get("intervencion_"+i)+",    Especialidad: "+mapaInfoIntervenciones.get("especialidad_"+i),report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
	        }
        }
        report.font.setFontSizeAndAttributes(8,false,false,false);
        report.getSectionReference("intervenciones").setTableCellsColSpan(1);
        report.getSectionReference("intervenciones").addTableTextCellAligned("tablaIntervenciones", "Profesional Tratante: "+mapaInfoPresupuestoBasico.get("medicotratante_0"), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
        report.getSectionReference("intervenciones").addTableTextCellAligned("tablaIntervenciones", "Diagnóstico: "+mapaInfoPresupuestoBasico.get("diagnostico_0"), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
        report.addSectionToDocument("intervenciones");
        
        /******************************* FIN Seccion de las Intervenciones ************************************************/
        
        /*********************************** Seccion de Servicios y Articulos ***************************************************/
        String cantidad=mapaParamPresupuestoBasico.get("cantidad_0")+"";
        String valorUnitario=mapaParamPresupuestoBasico.get("valorunitario_0")+"";
        String valorTotal=mapaParamPresupuestoBasico.get("valortotal_0")+"";
        
        int prioridadArt=Integer.parseInt(mapaParamArticulos.get("prioridad_0")+"");
        int rows=10+numRegServInfo+numRegArtInfo;
        int cols=1;
        if(cantidad.equals("true"))
        {
        	cols+=1;
        }
        if(valorUnitario.equals("true"))
        {
        	cols+=1;
        }
        if(valorTotal.equals("true"))
        {
        	cols+=1;
        }
        
        
        int tmp=numRegServParam;
        String[] indices={"codigoformato_","codigogrupo_","grupo_","prioridad_","detalle_","valoresdetalle_","subtotalgrupo_"};
        mapaParamServicios=(Listado.ordenarMapa(indices,"prioridad_","",mapaParamServicios,Integer.parseInt(mapaParamServicios.get("numRegistros")+"")));
        mapaParamServicios.put("numRegistros",tmp+"");
        
        
        report.createSection("servArt","tablaServArt",1,cols,0);
        if(cols>1)
        {
    		try
            {
    			if(cols==2)
    			{
    				float widths[]={(float) 8,(float) 2};
	    			report.getSectionReference("servArt").getTableReference("tablaServArt").setWidths(widths);
	    		}
    			if(cols==3)
    			{
    				float widths[]={(float) 7,(float) 1.5,(float) 1.5};
	    			report.getSectionReference("servArt").getTableReference("tablaServArt").setWidths(widths);
	    		}
    			if(cols==4)
    			{
    				float widths[]={(float) 6,(float) 1,(float) 1.5,(float) 1.5};
	    			report.getSectionReference("servArt").getTableReference("tablaServArt").setWidths(widths);
	    		}
            }
    		catch (Exception e)
	        {
	          	e.printStackTrace();
	        }
        }
        report.getSectionReference("servArt").getTableReference("tablaServArt").setPadding(0.0f);
        report.getSectionReference("servArt").getTableReference("tablaServArt").setSpacing(0.0f);
        report.getSectionReference("servArt").setTableBorder("tablaServArt", 0xFFFFFF, 0.0f);
        report.getSectionReference("servArt").setTableCellBorderWidth("tablaServArt", 0.2f);
        report.getSectionReference("servArt").setTableCellsDefaultColors("tablaServArt", 0xFFFFFF, 0xFFFFFF);
        report.font.setFontSizeAndAttributes(10,false,false,false);
        report.getSectionReference("servArt").setTableCellsColSpan(1);
        report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", "Concepto",report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
        if(cantidad.equals("true"))
        {
        	report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", ""+mapaParamPresupuestoBasico.get("desccantidad_0"),report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_CENTER);
        }
        if(valorUnitario.equals("true"))
        {
        	report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", ""+mapaParamPresupuestoBasico.get("descvalunitario_0"),report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_RIGHT);
        }
        if(valorTotal.equals("true"))
        {
        	report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", ""+mapaParamPresupuestoBasico.get("descvaltotal_0"),report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_RIGHT);
        }
        
        boolean condicion=false;
        report.font.setFontSizeAndAttributes(8,false,false,false);
        int numRegInfoXGrupo=0;
        String tituloDivision="";
        
        String medicamentosArticulos=(mapaParamArticulos.get("medicamentosarticulos_0")+"");
        
        if(medicamentosArticulos.equals(ConstantesBD.acronimoSi))
        {
	        for(int k = 0; k < numRegServParam ; k++)
	        {
	        	numRegInfoXGrupo=numeroServiciosPorGrupo(Integer.parseInt(mapaParamServicios.get("codigogrupo_"+k).toString()),mapaInfoServicios,numRegServInfo);
	        	if(numRegInfoXGrupo>0)
	        	{ 
		        	if(prioridadArt<Integer.parseInt(mapaParamServicios.get("prioridad_"+k)+"")&&!condicion)
		        	{
		        		condicion=seccionArticulos(mapaInfoArticulos,mapaParamArticulos,report,cols,numRegArtInfo,cantidad,valorUnitario,valorTotal,presupuesto,con,true,tituloDivision);
		        	}
		        	//else
		        	//{	
		        			//Solo el nombre del grupo con su subtotal
		        			if((mapaParamServicios.get("detalle_"+k)+"").equals("false")&&(mapaParamServicios.get("valoresdetalle_"+k)+"").equals("false")&&(mapaParamServicios.get("subtotalgrupo_"+k)+"").equals("false"))
		        			{
		        				report.getSectionReference("servArt").setTableCellsColSpan(cols);
		        				report.font.setFontSizeAndAttributes(8,false,false,false);
		        				report.getSectionReference("servArt").setTableCellsColSpan(cols-1);
		        				report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", ""+mapaParamServicios.get("grupo_"+k),report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
		        				report.getSectionReference("servArt").setTableCellsColSpan(1);
		        				report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", " "+UtilidadTexto.formatearValores(""+Utilidades.obtenertotalGrupoServicios(con, presupuesto, Utilidades.convertirAEntero(mapaParamServicios.get("codigogrupo_"+k)+"")),2,true,true),report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_RIGHT);
		        			}
		        			//Valores detalle
		        			else if((mapaParamServicios.get("detalle_"+k)+"").equals("true")&&(mapaParamServicios.get("valoresdetalle_"+k)+"").equals("false")&&(mapaParamServicios.get("subtotalgrupo_"+k)+"").equals("false"))
		        			{
		        				boolean cond=false;
		        				for (int j = 0,m=0 ; j < numRegInfoXGrupo && m<numRegServInfo ; m++)
		        				{
		        					cond=false;
		        					for(int i = 0 ; i < m ; i++)
		        					{
		        						if((mapaInfoServicios.get("nombregrupo_"+m).toString()).equals(mapaInfoServicios.get("nombregrupo_"+i).toString()))
		        						{
		        							cond=true;
		        						}
		        					}
		        					if(!cond&&mapaInfoServicios.get("codigogrupo_"+m).toString().equals(mapaParamServicios.get("codigogrupo_"+k)+""))
		        					{
				        				report.font.setFontSizeAndAttributes(8,false,false,false);
				        				report.getSectionReference("servArt").setTableCellsColSpan(cols);
				        				report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", ""+mapaInfoServicios.get("nombregrupo_"+m),report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
				        				cond=true;
			        				}
			    					if(mapaInfoServicios.get("codigogrupo_"+m).toString().equals(mapaParamServicios.get("codigogrupo_"+k)+""))
			    					{
				        				report.font.setFontSizeAndAttributes(8,false,false,false);
				        				report.getSectionReference("servArt").setTableCellsColSpan(cols);
				        				report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", ""+mapaInfoServicios.get("servicio_"+m),report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
				        				j++;
			    					}
		        				}
		        				
		        			}
		        			//Valores detalle
		        			else if((mapaParamServicios.get("detalle_"+k)+"").equals("true")&&(mapaParamServicios.get("valoresdetalle_"+k)+"").equals("true")&&(mapaParamServicios.get("subtotalgrupo_"+k)+"").equals("false"))
		        			{
		        				boolean cond=false;
		        				for (int j = 0, m = 0 ; j < numRegInfoXGrupo && m < numRegServInfo ; m++)
		        				{
		        					cond=false;
		        					for(int i = 0 ; i < m ; i++)
		        					{
		        						if((mapaInfoServicios.get("nombregrupo_"+m).toString()).equals(mapaInfoServicios.get("nombregrupo_"+i).toString()))
		        						{
		        							cond=true;
		        						}
		        					}
		        					if(!cond&&mapaInfoServicios.get("codigogrupo_"+m).toString().equals(mapaParamServicios.get("codigogrupo_"+k)+""))
		        					{
				        				report.font.setFontSizeAndAttributes(8,false,false,false);
				        				report.getSectionReference("servArt").setTableCellsColSpan(cols);
				        				report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", ""+mapaInfoServicios.get("nombregrupo_"+m),report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
				        				cond=true;
		        					}
		        					if(mapaInfoServicios.get("codigogrupo_"+m).toString().equals(mapaParamServicios.get("codigogrupo_"+k)+""))
		        					{
				        				report.font.setFontSizeAndAttributes(8,false,false,false);
				        				report.getSectionReference("servArt").setTableCellsColSpan(1);
				        				report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", ""+mapaInfoServicios.get("servicio_"+m),report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
				        				if (cantidad.equals("true"))
				        				{
				        					report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", ""+mapaInfoServicios.get("cantidad_"+m),report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_CENTER);
				        				}
				        				if (valorUnitario.equals("true"))
				        				{
				        					report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", ""+UtilidadTexto.formatearValores((""+mapaInfoServicios.get("valorunitario_"+m).toString()),2, true, true),report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_RIGHT);
				        				}
				        				if (valorTotal.equals("true"))
				        				{
				        					report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", ""+UtilidadTexto.formatearValores((""+mapaInfoServicios.get("valortotal_"+m).toString()),2, true, true),report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_RIGHT);
				        				}
				        				j++;
		        					}
		        				}
		        				
		        			}
		        			//Detalle - Valores detalle y Subgrupo
		        			else if((mapaParamServicios.get("detalle_"+k)+"").equals("true")&&(mapaParamServicios.get("valoresdetalle_"+k)+"").equals("true")&&(mapaParamServicios.get("subtotalgrupo_"+k)+"").equals("true"))
		        			{
		        				String nombreGrupo="";
		        				boolean cond=false;
		        				for (int j = 0,m=0 ; j < numRegInfoXGrupo && m < numRegServInfo ; m++)
		        				{
		        					cond=false;
		        					for(int i = 0 ; i < m ; i++)
		        					{
		        						if((mapaInfoServicios.get("nombregrupo_"+m).toString()).equals(mapaInfoServicios.get("nombregrupo_"+i).toString()))
		        						{
		        							cond=true;
		        						}
		        					}
		        					if(!cond&&mapaInfoServicios.get("codigogrupo_"+m).toString().equals(mapaParamServicios.get("codigogrupo_"+k)+""))
		        					{
				        				report.font.setFontSizeAndAttributes(8,false,false,false);
				        				report.getSectionReference("servArt").setTableCellsColSpan(cols);
				        				report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", ""+mapaInfoServicios.get("nombregrupo_"+m),report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
				        				cond=true;
				        				nombreGrupo=mapaInfoServicios.get("nombregrupo_"+m)+"";
		        					}
		        					if(mapaInfoServicios.get("codigogrupo_"+m).toString().equals(mapaParamServicios.get("codigogrupo_"+k)+""))
		        					{
		        						
				        				report.font.setFontSizeAndAttributes(8,false,false,false);
				        				report.getSectionReference("servArt").setTableCellsColSpan(1);
				        				report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", ""+mapaInfoServicios.get("servicio_"+m),report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
				        				if (cantidad.equals("true"))
				        				{
				        					report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", ""+mapaInfoServicios.get("cantidad_"+m),report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_CENTER);
				        				}
				        				if (valorUnitario.equals("true"))
				        				{
				        					report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", ""+UtilidadTexto.formatearValores((""+mapaInfoServicios.get("valorunitario_"+m).toString()),2, true, true),report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_RIGHT);
				        				}
				        				if (valorTotal.equals("true"))
				        				{
				        					report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", ""+UtilidadTexto.formatearValores((""+mapaInfoServicios.get("valortotal_"+m).toString()),2, true, true),report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_RIGHT);
				        				}
				        				j++;
		        					}
		        				}
		        				//Subtotal del grupo de servicios
		        				report.font.setFontSizeAndAttributes(8,false,false,false);
		        				report.getSectionReference("servArt").setTableCellsColSpan(1);
		        				report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", "Sub Total "+nombreGrupo+": ",report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
		        				report.getSectionReference("servArt").setTableCellsColSpan(cols-1);
		        				report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", ""+UtilidadTexto.formatearValores(""+Utilidades.obtenertotalGrupoServicios(con, presupuesto, Utilidades.convertirAEntero(mapaParamServicios.get("codigogrupo_"+k)+"")), 2, true, true),report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_RIGHT);
		        			}
		        			//Valores detalle
		        			else if((mapaParamServicios.get("detalle_"+k)+"").equals("false")&&(mapaParamServicios.get("valoresdetalle_"+k)+"").equals("true")&&(mapaParamServicios.get("subtotalgrupo_"+k)+"").equals("false"))
		        			{
		        				for (int j = 0; j < numRegInfoXGrupo; j++)
		        				{
			        				report.getSectionReference("servArt").setTableCellsColSpan(1);
			        				report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", ""+mapaInfoServicios.get("nombregrupo_"+j),report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
			        				
			        				report.getSectionReference("servArt").setTableCellsColSpan(1);
			        				if (cantidad.equals("true"))
			        				{
			        					report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", ""+mapaInfoServicios.get("cantidad_"+j),report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_CENTER);
			        				}
			        				if (valorUnitario.equals("true"))
			        				{
			        					report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", ""+UtilidadTexto.formatearValores((""+mapaInfoServicios.get("valorunitario_"+j).toString()),2, true, true),report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_RIGHT);
			        				}
			        				if (valorTotal.equals("true"))
			        				{
			        					report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", ""+UtilidadTexto.formatearValores((""+mapaInfoServicios.get("valortotal_"+j).toString()),2, true, true),report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_RIGHT);
			        				}
		        				}
		        			}
		        			//Valores detalle y subgrupo
		        			else if((mapaParamServicios.get("detalle_"+k)+"").equals("false")&&(mapaParamServicios.get("valoresdetalle_"+k)+"").equals("true")&&(mapaParamServicios.get("subtotalgrupo_"+k)+"").equals("true"))
		        			{
		        				for (int j = 0; j < numRegInfoXGrupo; j++)
		        				{
		        					
	        						report.font.setFontSizeAndAttributes(8,false,false,false);
			        				report.getSectionReference("servArt").setTableCellsColSpan(1);
			        				report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", ""+mapaInfoServicios.get("nombregrupo_"+j),report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
		        					report.font.setFontSizeAndAttributes(8,false,false,false);
			        				if (cantidad.equals("true"))
			        				{
			        					report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", ""+mapaInfoServicios.get("cantidad_"+j),report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_CENTER);
			        				}
			        				if (valorUnitario.equals("true"))
			        				{
			        					report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", ""+UtilidadTexto.formatearValores((""+mapaInfoServicios.get("valorunitario_"+j).toString()),2, true, true),report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_RIGHT);
			        				}
			        				if (valorTotal.equals("true"))
			        				{
			        					report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", ""+UtilidadTexto.formatearValores((""+mapaInfoServicios.get("valortotal_"+j).toString()),2, true, true),report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_RIGHT);
			        				}
		        				}
		        			}
		        			//Solo subgrupo
		        			else if((mapaParamServicios.get("detalle_"+k)+"").equals("false")&&(mapaParamServicios.get("valoresdetalle_"+k)+"").equals("false")&&(mapaParamServicios.get("subtotalgrupo_"+k)+"").equals("true"))
		        			{
		        				report.font.setFontSizeAndAttributes(8,false,false,false);
		        				report.getSectionReference("servArt").setTableCellsColSpan(cols-1);
		        				report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", ""+mapaParamServicios.get("grupo_"+k),report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
		        				report.getSectionReference("servArt").setTableCellsColSpan(1);
		        				report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", ""+UtilidadTexto.formatearValores(""+Utilidades.obtenertotalGrupoServicios(con, presupuesto, Utilidades.convertirAEntero(mapaParamServicios.get("codigogrupo_"+k)+"")),2,true,true),report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
		        			}
		        	//}
	        	}
	        }
        }
        else if(medicamentosArticulos.equals(ConstantesBD.acronimoNo))
        {
        	for(int k = 0; k < numRegServParam ; k++)
	        {
	        	numRegInfoXGrupo=numeroServiciosPorGrupo(Integer.parseInt(mapaParamServicios.get("codigogrupo_"+k).toString()),mapaInfoServicios,numRegServInfo);
	        	if(numRegInfoXGrupo>0)
	        	{ 
		        	if(prioridadArt<Integer.parseInt(mapaParamServicios.get("prioridad_"+k)+"")&&!condicion)
		        	{
		        		numRegArtInfo=mapaInfoArticulosMedicamentos.containsKey("numRegistros")?Integer.parseInt(mapaInfoArticulosMedicamentos.get("numRegistros")+""):0;
		        		tituloDivision = "MEDICAMENTOS";
		            	condicion=seccionArticulos(mapaInfoArticulosMedicamentos,mapaParamArticulos,report,cols,numRegArtInfo,cantidad,valorUnitario,valorTotal,presupuesto,con,true,tituloDivision);
		            	numRegArtInfo=mapaInfoArticulosInsumos.containsKey("numRegistros")?Integer.parseInt(mapaInfoArticulosInsumos.get("numRegistros")+""):0;
		        		tituloDivision = "INSUMOS";
		            	condicion=seccionArticulos(mapaInfoArticulosInsumos,mapaParamArticulos,report,cols,numRegArtInfo,cantidad,valorUnitario,valorTotal,presupuesto,con,false,tituloDivision);
		        		//condicion=seccionArticulos(mapaInfoArticulos,mapaParamArticulos,report,cols,numRegArtInfo,cantidad,valorUnitario,valorTotal,presupuesto,con,true,tituloDivision);
		        	}
		        	//else
		        	//{	
		        			//Solo el nombre del grupo con su subtotal
		        			if((mapaParamServicios.get("detalle_"+k)+"").equals("false")&&(mapaParamServicios.get("valoresdetalle_"+k)+"").equals("false")&&(mapaParamServicios.get("subtotalgrupo_"+k)+"").equals("false"))
		        			{
		        				report.getSectionReference("servArt").setTableCellsColSpan(cols);
		        				report.font.setFontSizeAndAttributes(8,false,false,false);
		        				report.getSectionReference("servArt").setTableCellsColSpan(cols-1);
		        				report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", ""+mapaParamServicios.get("grupo_"+k),report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
		        				report.getSectionReference("servArt").setTableCellsColSpan(1);
		        				report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", " "+UtilidadTexto.formatearValores(""+Utilidades.obtenertotalGrupoServicios(con, presupuesto, Utilidades.convertirAEntero(mapaParamServicios.get("codigogrupo_"+k)+"")),2,true,true),report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_RIGHT);
		        			}
		        			//Valores detalle
		        			else if((mapaParamServicios.get("detalle_"+k)+"").equals("true")&&(mapaParamServicios.get("valoresdetalle_"+k)+"").equals("false")&&(mapaParamServicios.get("subtotalgrupo_"+k)+"").equals("false"))
		        			{
		        				boolean cond=false;
		        				for (int j = 0,m=0 ; j < numRegInfoXGrupo && m<numRegServInfo ; m++)
		        				{
		        					cond=false;
		        					for(int i = 0 ; i < m ; i++)
		        					{
		        						if((mapaInfoServicios.get("nombregrupo_"+m).toString()).equals(mapaInfoServicios.get("nombregrupo_"+i).toString()))
		        						{
		        							cond=true;
		        						}
		        					}
		        					if(!cond&&mapaInfoServicios.get("codigogrupo_"+m).toString().equals(mapaParamServicios.get("codigogrupo_"+k)+""))
		        					{
				        				report.font.setFontSizeAndAttributes(8,false,false,false);
				        				report.getSectionReference("servArt").setTableCellsColSpan(cols);
				        				report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", ""+mapaInfoServicios.get("nombregrupo_"+m),report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
				        				cond=true;
			        				}
			    					if(mapaInfoServicios.get("codigogrupo_"+m).toString().equals(mapaParamServicios.get("codigogrupo_"+k)+""))
			    					{
				        				report.font.setFontSizeAndAttributes(8,false,false,false);
				        				report.getSectionReference("servArt").setTableCellsColSpan(cols);
				        				report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", ""+mapaInfoServicios.get("servicio_"+m),report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
				        				j++;
			    					}
		        				}
		        				
		        			}
		        			//Valores detalle
		        			else if((mapaParamServicios.get("detalle_"+k)+"").equals("true")&&(mapaParamServicios.get("valoresdetalle_"+k)+"").equals("true")&&(mapaParamServicios.get("subtotalgrupo_"+k)+"").equals("false"))
		        			{
		        				boolean cond=false;
		        				for (int j = 0, m = 0 ; j < numRegInfoXGrupo && m < numRegServInfo ; m++)
		        				{
		        					cond=false;
		        					for(int i = 0 ; i < m ; i++)
		        					{
		        						if((mapaInfoServicios.get("nombregrupo_"+m).toString()).equals(mapaInfoServicios.get("nombregrupo_"+i).toString()))
		        						{
		        							cond=true;
		        						}
		        					}
		        					if(!cond&&mapaInfoServicios.get("codigogrupo_"+m).toString().equals(mapaParamServicios.get("codigogrupo_"+k)+""))
		        					{
				        				report.font.setFontSizeAndAttributes(8,false,false,false);
				        				report.getSectionReference("servArt").setTableCellsColSpan(cols);
				        				report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", ""+mapaInfoServicios.get("nombregrupo_"+m),report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
				        				cond=true;
		        					}
		        					if(mapaInfoServicios.get("codigogrupo_"+m).toString().equals(mapaParamServicios.get("codigogrupo_"+k)+""))
		        					{
				        				report.font.setFontSizeAndAttributes(8,false,false,false);
				        				report.getSectionReference("servArt").setTableCellsColSpan(1);
				        				report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", ""+mapaInfoServicios.get("servicio_"+m),report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
				        				if (cantidad.equals("true"))
				        				{
				        					report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", ""+mapaInfoServicios.get("cantidad_"+m),report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_CENTER);
				        				}
				        				if (valorUnitario.equals("true"))
				        				{
				        					report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", ""+UtilidadTexto.formatearValores((""+mapaInfoServicios.get("valorunitario_"+m).toString()),2, true, true),report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_RIGHT);
				        				}
				        				if (valorTotal.equals("true"))
				        				{
				        					report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", ""+UtilidadTexto.formatearValores((""+mapaInfoServicios.get("valortotal_"+m).toString()),2, true, true),report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_RIGHT);
				        				}
				        				j++;
		        					}
		        				}
		        				
		        			}
		        			//Detalle - Valores detalle y Subgrupo
		        			else if((mapaParamServicios.get("detalle_"+k)+"").equals("true")&&(mapaParamServicios.get("valoresdetalle_"+k)+"").equals("true")&&(mapaParamServicios.get("subtotalgrupo_"+k)+"").equals("true"))
		        			{
		        				String nombreGrupo="";
		        				boolean cond=false;
		        				for (int j = 0,m=0 ; j < numRegInfoXGrupo && m < numRegServInfo ; m++)
		        				{
		        					cond=false;
		        					for(int i = 0 ; i < m ; i++)
		        					{
		        						if((mapaInfoServicios.get("nombregrupo_"+m).toString()).equals(mapaInfoServicios.get("nombregrupo_"+i).toString()))
		        						{
		        							cond=true;
		        						}
		        					}
		        					if(!cond&&mapaInfoServicios.get("codigogrupo_"+m).toString().equals(mapaParamServicios.get("codigogrupo_"+k)+""))
		        					{
				        				report.font.setFontSizeAndAttributes(8,false,false,false);
				        				report.getSectionReference("servArt").setTableCellsColSpan(cols);
				        				report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", ""+mapaInfoServicios.get("nombregrupo_"+m),report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
				        				cond=true;
				        				nombreGrupo=mapaInfoServicios.get("nombregrupo_"+m)+"";
		        					}
		        					if(mapaInfoServicios.get("codigogrupo_"+m).toString().equals(mapaParamServicios.get("codigogrupo_"+k)+""))
		        					{
		        						
				        				report.font.setFontSizeAndAttributes(8,false,false,false);
				        				report.getSectionReference("servArt").setTableCellsColSpan(1);
				        				report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", ""+mapaInfoServicios.get("servicio_"+m),report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
				        				if (cantidad.equals("true"))
				        				{
				        					report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", ""+mapaInfoServicios.get("cantidad_"+m),report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_CENTER);
				        				}
				        				if (valorUnitario.equals("true"))
				        				{
				        					report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", ""+UtilidadTexto.formatearValores((""+mapaInfoServicios.get("valorunitario_"+m).toString()),2, true, true),report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_RIGHT);
				        				}
				        				if (valorTotal.equals("true"))
				        				{
				        					report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", ""+UtilidadTexto.formatearValores((""+mapaInfoServicios.get("valortotal_"+m).toString()),2, true, true),report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_RIGHT);
				        				}
				        				j++;
		        					}
		        				}
		        				//Subtotal del grupo de servicios
		        				report.font.setFontSizeAndAttributes(8,false,false,false);
		        				report.getSectionReference("servArt").setTableCellsColSpan(1);
		        				report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", "Sub Total "+nombreGrupo+": ",report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
		        				report.getSectionReference("servArt").setTableCellsColSpan(cols-1);
		        				report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", ""+UtilidadTexto.formatearValores(""+Utilidades.obtenertotalGrupoServicios(con, presupuesto, Utilidades.convertirAEntero(mapaParamServicios.get("codigogrupo_"+k)+"")), 2, true, true),report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_RIGHT);
		        			}
		        			//Valores detalle
		        			else if((mapaParamServicios.get("detalle_"+k)+"").equals("false")&&(mapaParamServicios.get("valoresdetalle_"+k)+"").equals("true")&&(mapaParamServicios.get("subtotalgrupo_"+k)+"").equals("false"))
		        			{
		        				for (int j = 0; j < numRegInfoXGrupo; j++)
		        				{
			        				report.getSectionReference("servArt").setTableCellsColSpan(1);
			        				report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", ""+mapaInfoServicios.get("nombregrupo_"+j),report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
			        				
			        				report.getSectionReference("servArt").setTableCellsColSpan(1);
			        				if (cantidad.equals("true"))
			        				{
			        					report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", ""+mapaInfoServicios.get("cantidad_"+j),report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_CENTER);
			        				}
			        				if (valorUnitario.equals("true"))
			        				{
			        					report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", ""+UtilidadTexto.formatearValores((""+mapaInfoServicios.get("valorunitario_"+j).toString()),2, true, true),report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_RIGHT);
			        				}
			        				if (valorTotal.equals("true"))
			        				{
			        					report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", ""+UtilidadTexto.formatearValores((""+mapaInfoServicios.get("valortotal_"+j).toString()),2, true, true),report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_RIGHT);
			        				}
		        				}
		        			}
		        			//Valores detalle y subgrupo
		        			else if((mapaParamServicios.get("detalle_"+k)+"").equals("false")&&(mapaParamServicios.get("valoresdetalle_"+k)+"").equals("true")&&(mapaParamServicios.get("subtotalgrupo_"+k)+"").equals("true"))
		        			{
		        				for (int j = 0; j < numRegInfoXGrupo; j++)
		        				{
		        					
	        						report.font.setFontSizeAndAttributes(8,false,false,false);
			        				report.getSectionReference("servArt").setTableCellsColSpan(1);
			        				report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", ""+mapaInfoServicios.get("nombregrupo_"+j),report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
		        					report.font.setFontSizeAndAttributes(8,false,false,false);
			        				if (cantidad.equals("true"))
			        				{
			        					report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", ""+mapaInfoServicios.get("cantidad_"+j),report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_CENTER);
			        				}
			        				if (valorUnitario.equals("true"))
			        				{
			        					report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", ""+UtilidadTexto.formatearValores((""+mapaInfoServicios.get("valorunitario_"+j).toString()),2, true, true),report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_RIGHT);
			        				}
			        				if (valorTotal.equals("true"))
			        				{
			        					report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", ""+UtilidadTexto.formatearValores((""+mapaInfoServicios.get("valortotal_"+j).toString()),2, true, true),report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_RIGHT);
			        				}
		        				}
		        			}
		        			//Solo subgrupo
		        			else if((mapaParamServicios.get("detalle_"+k)+"").equals("false")&&(mapaParamServicios.get("valoresdetalle_"+k)+"").equals("false")&&(mapaParamServicios.get("subtotalgrupo_"+k)+"").equals("true"))
		        			{
		        				report.font.setFontSizeAndAttributes(8,false,false,false);
		        				report.getSectionReference("servArt").setTableCellsColSpan(cols-1);
		        				report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", ""+mapaParamServicios.get("grupo_"+k),report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
		        				report.getSectionReference("servArt").setTableCellsColSpan(1);
		        				report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", ""+UtilidadTexto.formatearValores(""+Utilidades.obtenertotalGrupoServicios(con, presupuesto, Utilidades.convertirAEntero(mapaParamServicios.get("codigogrupo_"+k)+"")),2,true,true),report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
		        			}
		        	//}
	        	}
	        }
        }
		
        if(medicamentosArticulos.equals(ConstantesBD.acronimoSi))
        {
             if(!condicion)
            	condicion=seccionArticulos(mapaInfoArticulos,mapaParamArticulos,report,cols,numRegArtInfo,cantidad,valorUnitario,valorTotal,presupuesto,con,true,tituloDivision);
        }
        else if(medicamentosArticulos.equals(ConstantesBD.acronimoNo))
        {
        	if(!condicion)
        	{
        		/*report.font.setFontSizeAndAttributes(10,true,false,false);
        		report.document.addParagraph("Articulos", 10);
        		report.font.setFontSizeAndAttributes(10,false,false,false);*/
        		numRegArtInfo=mapaInfoArticulosMedicamentos.containsKey("numRegistros")?Integer.parseInt(mapaInfoArticulosMedicamentos.get("numRegistros")+""):0;
        		tituloDivision = "MEDICAMENTOS";
            	condicion=seccionArticulos(mapaInfoArticulosMedicamentos,mapaParamArticulos,report,cols,numRegArtInfo,cantidad,valorUnitario,valorTotal,presupuesto,con,true,tituloDivision);

        		/*report.font.setFontSizeAndAttributes(10,true,false,false);
        		report.document.addParagraph("Insumos", 10);
        		report.font.setFontSizeAndAttributes(10,false,false,false);*/
        		numRegArtInfo=mapaInfoArticulosInsumos.containsKey("numRegistros")?Integer.parseInt(mapaInfoArticulosInsumos.get("numRegistros")+""):0;
        		tituloDivision = "INSUMOS";
            	condicion=seccionArticulos(mapaInfoArticulosInsumos,mapaParamArticulos,report,cols,numRegArtInfo,cantidad,valorUnitario,valorTotal,presupuesto,con,false,tituloDivision);
        	}
        }
        
        report.font.setFontSizeAndAttributes(10,false,false,false);
		report.getSectionReference("servArt").setTableCellsColSpan(cols-1);
		report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", "TOTAL PRESUPUESTO: ",report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
		report.getSectionReference("servArt").setTableCellsColSpan(1);
		report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", dto.getDescripcion()+" "+dto.getSimbolo()+" "+UtilidadTexto.formatearValores(""+Utilidades.obtenerTotalPresupuesto(con, codigoPresupuesto)),report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_RIGHT);
		if(index!=ConstantesBD.codigoNuncaValido)
		{
			report.getSectionReference("servArt").setTableCellsColSpan(1);
			report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", "Conversion a otro tipo de Moneda: "+mapaMoneda.get("descripciontipomoneda_"+index)+" "+mapaMoneda.get("simbolotipomoneda_"+index)+" "+UtilidadTexto.formatearValores(mapaMoneda.get("factorconversion_"+index)+""),report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
			report.getSectionReference("servArt").setTableCellsColSpan(1);
			double pres=Utilidades.convertirADouble(Utilidades.obtenerTotalPresupuesto(con, codigoPresupuesto)+"");
			double factor=Utilidades.convertirADouble(mapaMoneda.get("factorconversion_"+index)+"");
			report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", UtilidadTexto.formatearValores(pres/factor),report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_RIGHT);
		}
        report.addSectionToDocument("servArt");
        /********************************* FIN Seccion de Servicios y Articulos *************************************************/
        
        
        /********************************* Seccion de la Nota de Pie de Página **************************************************/
        report.createSection("notaPiePagina","tablaNota",1,cols,0);
        report.getSectionReference("notaPiePagina").setTableBorder("tablaNota", 0xFFFFFF, 0.0f);
        report.getSectionReference("notaPiePagina").getTableReference("tablaNota").setPadding(0.0f);
        report.getSectionReference("notaPiePagina").getTableReference("tablaNota").setSpacing(0.0f);
        report.getSectionReference("notaPiePagina").setTableCellBorderWidth("tablaNota", 0.0f);
        report.getSectionReference("notaPiePagina").setTableCellsDefaultColors("tablaNota", 0xFFFFFF, 0xFFFFFF);
        report.font.setFontSizeAndAttributes(8,false,false,false);
        report.getSectionReference("notaPiePagina").setTableCellsColSpan(cols);
        report.getSectionReference("notaPiePagina").addTableTextCellAligned("tablaNota", ""+mapaParamPresupuestoBasico.get("notapiepagina_0"), report.font,iTextBaseDocument.ALIGNMENT_JUSTIFIED,iTextBaseDocument.ALIGNMENT_JUSTIFIED);
        report.addSectionToDocument("notaPiePagina");
        /****************************** FIN Seccion de la Nota de Pie de Página *************************************************/
        
        
        report.closeReport(); 
	}


	/**
	 * @param codigoGrupo
	 * @param mapaInfoServicios
	 * @param numRegServInfo
	 * @return
	 */
	private static int numeroServiciosPorGrupo(int codigoGrupo, HashMap mapaInfoServicios, int numRegServInfo)
	{
		int cont=0;
		for(int i=0;i<numRegServInfo;i++)
		{
			if(codigoGrupo==Integer.parseInt(mapaInfoServicios.get("codigogrupo_"+i).toString()))
			{
				cont++;
			}
		}
		return cont;
	}
	
	
	
	

	/**
	 * Método para el diseño de la seccion de los articulos con todas sus casos posibles
	 * @param mapaInfoArticulos
	 * @param mapaParamArticulos
	 * @param report
	 * @param cols
	 * @param numRegArtInfo
	 * @param cantidad
	 * @param valorUnitario
	 * @param valorTotal
	 * @param presupuesto
	 * @param con
	 * @return
	 */
	private static boolean seccionArticulos(HashMap mapaInfoArticulos, HashMap mapaParamArticulos, PdfReports report,int cols,int numRegArtInfo, String cantidad, String valorUnitario, String valorTotal, int presupuesto, Connection con, boolean tituloParametro, String tituloDivision)
	{
		String descSeccion=""+mapaParamArticulos.get("descseccionarticulo_0");
		String medicamentosArticulos=(mapaParamArticulos.get("medicamentosarticulos_0")+"");
		
		if(tituloParametro)
		{
			if(!(mapaParamArticulos.get("detarticulo_0")+"").equals("false"))
			{
				report.font.setFontSizeAndAttributes(10,false,false,false);
				report.getSectionReference("servArt").setTableCellsColSpan(cols);
				report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", descSeccion,report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
				report.getSectionReference("servArt").setTableCellsColSpan(cols);
				report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", "", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
			}
			
			if((mapaParamArticulos.get("detarticulo_0")+"").equals("false"))
			{
				report.font.setFontSizeAndAttributes(8,false,false,false);
				report.getSectionReference("servArt").setTableCellsColSpan(1);
				report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", descSeccion, report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
				report.getSectionReference("servArt").setTableCellsColSpan(cols-1);report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", ""+UtilidadTexto.formatearValores(""+Utilidades.obtenerTotalArticulosPresupuesto(con, presupuesto),2,true,true),report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_RIGHT);
				report.getSectionReference("servArt").setTableCellsColSpan(cols);
				report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", "", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
			}
		}
		
		if(medicamentosArticulos.equals(ConstantesBD.acronimoNo))
		{
			report.font.setFontSizeAndAttributes(10,false,false,false);
			report.getSectionReference("servArt").setTableCellsColSpan(cols);
			report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", tituloDivision,report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
		}
		
		//Nivel 1 -- Solo la clase de inventarios y su total
		if((mapaParamArticulos.get("detarticulo_0")+"").equals("true")&&Integer.parseInt(mapaParamArticulos.get("nivel_0")+"")==1&&(mapaParamArticulos.get("detallenivel_0")+"").equals("false")&&(mapaParamArticulos.get("valoresdetalle_0")+"").equals("false")&&(mapaParamArticulos.get("subtotalnivel_0")+"").equals("false"))
		{
			boolean cond=false;
			for(int k = 0; k < numRegArtInfo ; k++)
			{
				cond=false;
				for(int i = 0 ; i < k ; i++)
				{
					if(mapaInfoArticulos.get("claseinventario_"+k).toString().equals(mapaInfoArticulos.get("claseinventario_"+i).toString()))
					{
						cond=true;
					}
				}
				if(!cond)
				{
					report.getSectionReference("servArt").setTableCellsColSpan(cols);
					report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", ""+mapaInfoArticulos.get("claseinventario_"+k),report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
				}
			}
		}
		//Nivel 1 -- Con todos true
		if((mapaParamArticulos.get("detarticulo_0")+"").equals("true")&&Integer.parseInt(mapaParamArticulos.get("nivel_0")+"")==1&&(mapaParamArticulos.get("detallenivel_0")+"").equals("true")&&(mapaParamArticulos.get("valoresdetalle_0")+"").equals("true")&&(mapaParamArticulos.get("subtotalnivel_0")+"").equals("true"))
		{
			boolean cond=false;
			boolean temp=true;
			for(int k = 0; k < numRegArtInfo ; k++)
			{
				cond=false;
				for(int i = 0 ; i < k ; i++)
				{ 
					if(mapaInfoArticulos.get("claseinventario_"+k).toString().equals(mapaInfoArticulos.get("claseinventario_"+i).toString()))
					{
						cond=true;
					}
				}
				if(!cond)
				{
					report.font.setFontSizeAndAttributes(8,false,false,false);
					report.getSectionReference("servArt").setTableCellsColSpan(cols);
					report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", ""+mapaInfoArticulos.get("claseinventario_"+k),report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
				}
				report.font.setFontSizeAndAttributes(8,false,false,false);
				report.getSectionReference("servArt").setTableCellsColSpan(1);
				report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", ""+mapaInfoArticulos.get("articulo_"+k),report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
				if(cantidad.equals("true"))
				{
					report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", ""+mapaInfoArticulos.get("cantidad_"+k),report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_CENTER);
				}
				if(valorUnitario.equals("true"))
				{
					report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", ""+UtilidadTexto.formatearValores((""+mapaInfoArticulos.get("valorunitario_"+k).toString()),2, true, true),report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_RIGHT);
				}
				if(valorTotal.equals("true"))
				{
					report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", ""+UtilidadTexto.formatearValores((""+mapaInfoArticulos.get("valortotal_"+k).toString()),2, true, true),report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_RIGHT);
				}
				/**para poner el subtotal de la clase de inventarios**/
				{
					if((k+1)==numRegArtInfo)
					{
						temp=false;
					}
					else if(!(mapaInfoArticulos.get("claseinventario_"+k).toString().trim()).equals(mapaInfoArticulos.get("claseinventario_"+(k+1)).toString().trim()))
					{
						temp=false;
					}
				}
				if(!temp)
				{
					report.font.setFontSizeAndAttributes(8,false,false,false);
					report.getSectionReference("servArt").setTableCellsColSpan(1);
					report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", "Sub total "+mapaInfoArticulos.get("claseinventario_"+k),report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);						
					report.getSectionReference("servArt").setTableCellsColSpan(cols-1);
					report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", ""+UtilidadTexto.formatearValores(""+Utilidades.obtenerTotalClaseInventarioArticulos(con, presupuesto, Integer.parseInt(mapaInfoArticulos.get("codigoclase_"+k)+"")),2,true,true),report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_RIGHT);
					temp=true;
				}
			}
		}
		//Nivel 1 
		if((mapaParamArticulos.get("detarticulo_0")+"").equals("true")&&Integer.parseInt(mapaParamArticulos.get("nivel_0")+"")==1&&(mapaParamArticulos.get("detallenivel_0")+"").equals("true")&&(mapaParamArticulos.get("valoresdetalle_0")+"").equals("false")&&(mapaParamArticulos.get("subtotalnivel_0")+"").equals("false"))
		{
			boolean cond=false;
			for(int k = 0 ; k < numRegArtInfo ; k++)
			{
				cond=false;
				for(int i = 0 ; i < k ; i++)
				{
					if(mapaInfoArticulos.get("claseinventario_"+k).toString().equals(mapaInfoArticulos.get("claseinventario_"+i).toString()))
					{
						cond=true;
					}
				}
				if(!cond)
				{
					report.font.setFontSizeAndAttributes(8,false,false,false);
					report.getSectionReference("servArt").setTableCellsColSpan(cols);
					report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", ""+mapaInfoArticulos.get("claseinventario_"+k),report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
				}
				report.font.setFontSizeAndAttributes(8,false,false,false);
				report.getSectionReference("servArt").setTableCellsColSpan(cols);
				report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", ""+mapaInfoArticulos.get("articulo_"+k),report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
     		}
			report.font.setFontSizeAndAttributes(8,false,false,false);
			report.getSectionReference("servArt").setTableCellsColSpan(1);
			report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", "TOTAL ARTICULOS: ",report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
			report.getSectionReference("servArt").setTableCellsColSpan(cols-1);
			report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", ""+UtilidadTexto.formatearValores(""+Utilidades.obtenerTotalArticulosPresupuesto(con, presupuesto),2,true,true),report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_RIGHT);
		}
		//Nivel 1 
		if((mapaParamArticulos.get("detarticulo_0")+"").equals("true")&&Integer.parseInt(mapaParamArticulos.get("nivel_0")+"")==1&&(mapaParamArticulos.get("detallenivel_0")+"").equals("true")&&(mapaParamArticulos.get("valoresdetalle_0")+"").equals("true")&&(mapaParamArticulos.get("subtotalnivel_0")+"").equals("false"))
		{
			boolean cond=false;
			for(int k = 0 ; k < numRegArtInfo ; k++)
			{
				cond=false;
				for(int i = 0 ; i < k ; i++)
				{
					if(mapaInfoArticulos.get("claseinventario_"+k).toString().equals(mapaInfoArticulos.get("claseinventario_"+i).toString()))
					{
						cond=true;
					}
				}
				if(!cond)
				{
					report.font.setFontSizeAndAttributes(8,false,false,false);
					report.getSectionReference("servArt").setTableCellsColSpan(cols);
					report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", ""+mapaInfoArticulos.get("claseinventario_"+k),report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
				}
				report.font.setFontSizeAndAttributes(8,false,false,false);
				report.getSectionReference("servArt").setTableCellsColSpan(1);
				report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", ""+mapaInfoArticulos.get("articulo_"+k),report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
				if(cantidad.equals("true"))
				{
					report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", ""+mapaInfoArticulos.get("cantidad_"+k),report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_CENTER);
				}
				if(valorUnitario.equals("true"))
				{
					report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", ""+UtilidadTexto.formatearValores((""+mapaInfoArticulos.get("valorunitario_"+k).toString()),2, true, true),report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_RIGHT);
				}
				if(valorTotal.equals("true"))
				{
					report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", ""+UtilidadTexto.formatearValores((""+mapaInfoArticulos.get("valortotal_"+k).toString()),2, true, true),report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_RIGHT);
				}
     		}
		}
		//Nivel 1 
		if((mapaParamArticulos.get("detarticulo_0")+"").equals("true")&&Integer.parseInt(mapaParamArticulos.get("nivel_0")+"")==1&&(mapaParamArticulos.get("detallenivel_0")+"").equals("false")&&(mapaParamArticulos.get("valoresdetalle_0")+"").equals("true")&&(mapaParamArticulos.get("subtotalnivel_0")+"").equals("false"))
		{
			boolean cond=false;
			for(int k = 0; k < numRegArtInfo ; k++)
			{
				cond=false;
				for(int i = 0 ; i < k ; i++)
				{
					if(mapaInfoArticulos.get("claseinventario_"+k).toString().equals(mapaInfoArticulos.get("claseinventario_"+i).toString()))
					{
						cond=true;
					}
				}
				if(!cond)
				{
					report.font.setFontSizeAndAttributes(8,false,false,false);
					report.getSectionReference("servArt").setTableCellsColSpan(cols);
					report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", ""+mapaInfoArticulos.get("claseinventario_"+k),report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
				}
				report.font.setFontSizeAndAttributes(8,false,false,false);
				report.getSectionReference("servArt").setTableCellsColSpan(1);
				report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", ""+mapaInfoArticulos.get("articulo_"+k),report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
				if(cantidad.equals("true"))
				{
					report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", ""+mapaInfoArticulos.get("cantidad_"+k),report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_CENTER);
				}
				if(valorUnitario.equals("true"))
				{
					report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", ""+UtilidadTexto.formatearValores((""+mapaInfoArticulos.get("valorunitario_"+k).toString()),2, true, true),report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_RIGHT);
				}
				if(valorTotal.equals("true"))
				{
					report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", ""+UtilidadTexto.formatearValores((""+mapaInfoArticulos.get("valortotal_"+k).toString()),2, true, true),report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_RIGHT);
				}
     		}
			report.font.setFontSizeAndAttributes(8,false,false,false);
			report.getSectionReference("servArt").setTableCellsColSpan(1);
			report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", "Total: ",report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
			report.getSectionReference("servArt").setTableCellsColSpan(cols-1);report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", ""+UtilidadTexto.formatearValores(""+Utilidades.obtenerTotalArticulosPresupuesto(con, presupuesto),2,true,true),report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_RIGHT);
		}
		//Nivel 2 -- Clase de Inventarios y el Grupo Total por grupo
		if((mapaParamArticulos.get("detarticulo_0")+"").equals("true")&&Integer.parseInt(mapaParamArticulos.get("nivel_0")+"")==2&&(mapaParamArticulos.get("detallenivel_0")+"").equals("false")&&(mapaParamArticulos.get("valoresdetalle_0")+"").equals("false")&&(mapaParamArticulos.get("subtotalnivel_0")+"").equals("false"))
		{
			boolean cond=false;
			for(int k = 0 ; k < numRegArtInfo ; k++)
			{
					cond=false;
					for(int i = 0 ; i <  k ; i++)
					{
						if(mapaInfoArticulos.get("claseinventario_"+k).toString().equals(mapaInfoArticulos.get("claseinventario_"+i).toString()))
						{
							if(mapaInfoArticulos.get("grupo_"+k).toString().equals(mapaInfoArticulos.get("grupo_"+i).toString()))
							{
								cond=true;
							}
						}
					}
					if(!cond)
					{
						report.font.setFontSizeAndAttributes(8,false,false,false);
						report.getSectionReference("servArt").setTableCellsColSpan(cols);
						report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", ""+mapaInfoArticulos.get("claseinventario_"+k),report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
						report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", ""+mapaInfoArticulos.get("grupo_"+k),report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
					}
			}
		}
		//Nivel 2
		if((mapaParamArticulos.get("detarticulo_0")+"").equals("true")&&Integer.parseInt(mapaParamArticulos.get("nivel_0")+"")==2&&(mapaParamArticulos.get("detallenivel_0")+"").equals("true")&&(mapaParamArticulos.get("valoresdetalle_0")+"").equals("true")&&(mapaParamArticulos.get("subtotalnivel_0")+"").equals("true"))
		{
			boolean cond=false;
			boolean clase=false;
			boolean temp=true;
			for(int k = 0 ; k < numRegArtInfo ; k++)
			{
				cond=false;
				for(int  i = 0 ; i < k ; i++)
				{
					if(mapaInfoArticulos.get("claseinventario_"+k).toString().equals(mapaInfoArticulos.get("claseinventario_"+i).toString()))
					{
						if(mapaInfoArticulos.get("grupo_"+k).toString().equals(mapaInfoArticulos.get("grupo_"+i).toString()))
						{
							cond=true;
						}
						clase=true;
					}
				}
				
				if(!cond)
				{
					report.font.setFontSizeAndAttributes(8,false,false,false);
					report.getSectionReference("servArt").setTableCellsColSpan(cols);
					if(!clase)
					{
						report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", ""+mapaInfoArticulos.get("claseinventario_"+k),report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
					}
					report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", ""+mapaInfoArticulos.get("grupo_"+k),report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
				}
				report.font.setFontSizeAndAttributes(8,false,false,false);
				report.getSectionReference("servArt").setTableCellsColSpan(1);
				report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", ""+mapaInfoArticulos.get("articulo_"+k),report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
				if(cantidad.equals("true"))
				{
					report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", ""+mapaInfoArticulos.get("cantidad_"+k),report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_CENTER);
				}
				if(valorUnitario.equals("true"))
				{
					report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", ""+UtilidadTexto.formatearValores((""+mapaInfoArticulos.get("valorunitario_"+k).toString()),2, true, true),report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_RIGHT);
				}
				if(valorTotal.equals("true"))
				{
					report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", ""+UtilidadTexto.formatearValores((""+mapaInfoArticulos.get("valortotal_"+k).toString()),2, true, true),report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_RIGHT);
				}
				
				/**para poner el subtotal del grupo**/
				{
					if((k+1)==numRegArtInfo)
					{
						temp=false;
					}
					else if(!(mapaInfoArticulos.get("grupo_"+k).toString().trim()).equals(mapaInfoArticulos.get("grupo_"+(k+1)).toString().trim()))
					{
						temp=false;
					}
				}
				if(!temp)
				{
					report.font.setFontSizeAndAttributes(8,false,false,false);
					report.getSectionReference("servArt").setTableCellsColSpan(1);
					report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", "Sub total "+mapaInfoArticulos.get("grupo_"+k),report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);						
					report.getSectionReference("servArt").setTableCellsColSpan(cols-1);
					report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", ""+UtilidadTexto.formatearValores(""+Utilidades.obtenerTotalGrupoArticulos(con, presupuesto, Integer.parseInt(mapaInfoArticulos.get("codigogrupo_"+k)+"")),2,true,true),report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_RIGHT);
					temp=true;
				}
				/**para poner el subtotal de la calse inventario**/
				{
					if((k+1)==numRegArtInfo)
					{
						temp=false;
					}
					else if(!(mapaInfoArticulos.get("claseinventario_"+k).toString().trim()).equals(mapaInfoArticulos.get("claseinventario_"+(k+1)).toString().trim()))
					{
						temp=false;
					}
				}
				if(!temp)
				{
					report.font.setFontSizeAndAttributes(8,false,false,false);
					report.getSectionReference("servArt").setTableCellsColSpan(1);
					report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", "Sub total "+mapaInfoArticulos.get("claseinventario_"+k),report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);						
					report.getSectionReference("servArt").setTableCellsColSpan(cols-1);
					report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", ""+UtilidadTexto.formatearValores(""+Utilidades.obtenerTotalClaseInventarioArticulos(con, presupuesto, Integer.parseInt(mapaInfoArticulos.get("codigoclase_"+k)+"")),2,true,true),report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_RIGHT);
					temp=true;
				}
			}
		}
		//Nivel 2
		if((mapaParamArticulos.get("detarticulo_0")+"").equals("true")&&Integer.parseInt(mapaParamArticulos.get("nivel_0")+"")==2&&(mapaParamArticulos.get("detallenivel_0")+"").equals("true")&&(mapaParamArticulos.get("valoresdetalle_0")+"").equals("true")&&(mapaParamArticulos.get("subtotalnivel_0")+"").equals("false"))
		{
			boolean cond=false;
			boolean clase=false;
			for(int k = 0 ; k < numRegArtInfo ; k++)
			{
				cond=false;
				for(int i = 0 ; i < k ; i++)
				{
					if(mapaInfoArticulos.get("claseinventario_"+k).toString().equals(mapaInfoArticulos.get("claseinventario_"+i).toString()))
					{
						if(mapaInfoArticulos.get("grupo_"+k).toString().equals(mapaInfoArticulos.get("grupo_"+i).toString()))
						{
							cond=true;
						}
						clase=true;
					}
				}
				if(!cond)
				{
					report.font.setFontSizeAndAttributes(8,false,false,false);
					report.getSectionReference("servArt").setTableCellsColSpan(cols);
					if(!clase)
					{
						report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", ""+mapaInfoArticulos.get("claseinventario_"+k),report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
					}
					report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", ""+mapaInfoArticulos.get("grupo_"+k),report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
				}
				report.font.setFontSizeAndAttributes(8,false,false,false);
				report.getSectionReference("servArt").setTableCellsColSpan(1);
				report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", ""+mapaInfoArticulos.get("articulo_"+k),report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
				if(cantidad.equals("true"))
				{
					report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", ""+mapaInfoArticulos.get("cantidad_"+k),report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_CENTER);
				}
				if(valorUnitario.equals("true"))
				{
					report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", ""+UtilidadTexto.formatearValores((""+mapaInfoArticulos.get("valorunitario_"+k).toString()),2, true, true),report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_RIGHT);
				}
				if(valorTotal.equals("true"))
				{
					report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", ""+UtilidadTexto.formatearValores((""+mapaInfoArticulos.get("valortotal_"+k).toString()),2, true, true),report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_RIGHT);
				}
     		}
			report.font.setFontSizeAndAttributes(8,false,false,false);
			report.getSectionReference("servArt").setTableCellsColSpan(1);
			report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", "Total: ",report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
			report.getSectionReference("servArt").setTableCellsColSpan(cols-1);
			report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", ""+UtilidadTexto.formatearValores(""+Utilidades.obtenerTotalArticulosPresupuesto(con, presupuesto),2,true,true),report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_RIGHT);
		}
		//Nivel 2
		if((mapaParamArticulos.get("detarticulo_0")+"").equals("true")&&Integer.parseInt(mapaParamArticulos.get("nivel_0")+"")==2&&(mapaParamArticulos.get("detallenivel_0")+"").equals("true")&&(mapaParamArticulos.get("valoresdetalle_0")+"").equals("false")&&(mapaParamArticulos.get("subtotalnivel_0")+"").equals("false"))
		{
			boolean cond=false;
			boolean clase=false;
			for(int k = 0 ; k < numRegArtInfo ; k++)
			{
				cond=false;
				for(int i = 0  ; i < k ; i++)
				{
					if(mapaInfoArticulos.get("claseinventario_"+k).toString().equals(mapaInfoArticulos.get("claseinventario_"+i).toString()))
					{
						if(mapaInfoArticulos.get("grupo_"+k).toString().equals(mapaInfoArticulos.get("grupo_"+i).toString()))
						{
							cond=true;
						}
						clase=true;
					}
				}
				if(!cond)
				{
					report.font.setFontSizeAndAttributes(8,false,false,false);
					report.getSectionReference("servArt").setTableCellsColSpan(cols);
					if(!clase)
					{
						report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", ""+mapaInfoArticulos.get("claseinventario_"+k),report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
					}
					report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", ""+mapaInfoArticulos.get("grupo_"+k),report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
				}
				report.font.setFontSizeAndAttributes(8,false,false,false);
				report.getSectionReference("servArt").setTableCellsColSpan(cols);
				report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", ""+mapaInfoArticulos.get("articulo_"+k),report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
     		}
			report.font.setFontSizeAndAttributes(8,false,false,false);
			report.getSectionReference("servArt").setTableCellsColSpan(1);
			report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", "Total: ",report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
			report.getSectionReference("servArt").setTableCellsColSpan(cols-1);
			report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", ""+UtilidadTexto.formatearValores(""+Utilidades.obtenerTotalArticulosPresupuesto(con, presupuesto),2,true,true),report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_RIGHT);

		}
		//Nivel 2
		if((mapaParamArticulos.get("detarticulo_0")+"").equals("true")&&Integer.parseInt(mapaParamArticulos.get("nivel_0")+"")==2&&(mapaParamArticulos.get("detallenivel_0")+"").equals("false")&&(mapaParamArticulos.get("valoresdetalle_0")+"").equals("true")&&(mapaParamArticulos.get("subtotalnivel_0")+"").equals("false"))
		{
			boolean cond=false;
			boolean clase=false;
			for(int k = 0 ; k < numRegArtInfo ; k++)
			{
				cond=false;
				for(int  i = 0 ;  i < k ; i++)
				{
					if(mapaInfoArticulos.get("claseinventario_"+k).toString().equals(mapaInfoArticulos.get("claseinventario_"+i).toString()))
					{
						if(mapaInfoArticulos.get("grupo_"+k).toString().equals(mapaInfoArticulos.get("grupo_"+i).toString()))
						{
							cond=true;
						}
						clase=true;
					}
				}
				if(!cond)
				{
					report.font.setFontSizeAndAttributes(8,false,false,false);
					report.getSectionReference("servArt").setTableCellsColSpan(cols);
					if(!clase)
					{
						report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", ""+mapaInfoArticulos.get("claseinventario_"+k),report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
					}
					report.getSectionReference("servArt").setTableCellsColSpan(1);
					report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", ""+mapaInfoArticulos.get("grupo_"+k),report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
				}
				report.font.setFontSizeAndAttributes(8,false,false,false);
				report.getSectionReference("servArt").setTableCellsColSpan(3);
				if(cantidad.equals("true"))
				{
					int cant=0;
					boolean esta=false;
					for(int i = 0 ; i < k ; i++)
					{
						if(mapaInfoArticulos.get("claseinventario_"+k).toString().equals(mapaInfoArticulos.get("claseinventario_"+i).toString())&&mapaInfoArticulos.get("grupo_"+k).toString().equals(mapaInfoArticulos.get("grupo_"+i).toString()))
						{
							esta=true;
						}
					}
					if(!esta)
					{
						for(int i=0;i<numRegArtInfo;i++)
						{
							if(mapaInfoArticulos.get("claseinventario_"+k).toString().equals(mapaInfoArticulos.get("claseinventario_"+i).toString()))
							{
								if(mapaInfoArticulos.get("grupo_"+k).toString().equals(mapaInfoArticulos.get("grupo_"+i).toString()))
								{
									cant+=Integer.parseInt(mapaInfoArticulos.get("cantidad_"+k)+"");
								}
							}
						}
						report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", ""+cant,report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
					}
				}
     		}
			report.font.setFontSizeAndAttributes(8,false,false,false);
			report.getSectionReference("servArt").setTableCellsColSpan(1);
			report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", "Total: ",report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
			report.getSectionReference("servArt").setTableCellsColSpan(cols-1);
			report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", ""+UtilidadTexto.formatearValores(""+Utilidades.obtenerTotalArticulosPresupuesto(con, presupuesto),2,true,true),report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_RIGHT);

		}
		//Nivel 3 
		if((mapaParamArticulos.get("detarticulo_0")+"").equals("true")&&Integer.parseInt(mapaParamArticulos.get("nivel_0")+"")==3&&(mapaParamArticulos.get("detallenivel_0")+"").equals("false")&&(mapaParamArticulos.get("valoresdetalle_0")+"").equals("false")&&(mapaParamArticulos.get("subtotalnivel_0")+"").equals("false"))
		{
			boolean cond=false;
			boolean clase=false;
			for(int k = 0 ; k < numRegArtInfo ; k++)
			{
				
				cond=false;
				for(int i = 0  ; i < k ; i++)
				{
					if(mapaInfoArticulos.get("claseinventario_"+k).toString().equals(mapaInfoArticulos.get("claseinventario_"+i).toString()))
					{
						if(mapaInfoArticulos.get("grupo_"+k).toString().equals(mapaInfoArticulos.get("grupo_"+i).toString()))
						{
							if(mapaInfoArticulos.get("subgrupo_"+k).toString().equals(mapaInfoArticulos.get("subgrupo_"+i).toString()))
							{
								cond=true;
							}
						}
						clase=true;
					}
				}
				if(!cond)
				{
					report.font.setFontSizeAndAttributes(8,false,false,false);
					report.getSectionReference("servArt").setTableCellsColSpan(cols);
					if(!clase)
					{
						report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", ""+mapaInfoArticulos.get("claseinventario_"+k),report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
					}
					report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", ""+mapaInfoArticulos.get("grupo_"+k),report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
					report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", ""+mapaInfoArticulos.get("subgrupo_"+k),report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
				}
			}
				
		}
		//Nivel 3
		if((mapaParamArticulos.get("detarticulo_0")+"").equals("true")&&Integer.parseInt(mapaParamArticulos.get("nivel_0")+"")==3&&(mapaParamArticulos.get("detallenivel_0")+"").equals("true")&&(mapaParamArticulos.get("valoresdetalle_0")+"").equals("true")&&(mapaParamArticulos.get("subtotalnivel_0")+"").equals("true"))
		{
			boolean cond=false;
			boolean temp=true;
			boolean clase=false;
			for(int k = 0 ; k < numRegArtInfo ; k++)
			{
				cond=false;
				for(int  i = 0 ; i < k  ; i++)
				{
					if(mapaInfoArticulos.get("claseinventario_"+k).toString().equals(mapaInfoArticulos.get("claseinventario_"+i).toString()))
					{
						if(mapaInfoArticulos.get("grupo_"+k).toString().equals(mapaInfoArticulos.get("grupo_"+i).toString()))
						{
							if(mapaInfoArticulos.get("subgrupo_"+k).toString().equals(mapaInfoArticulos.get("subgrupo_"+i).toString()))
							{
								cond=true;
							}
						}
						clase=true;
					}
				}
				if(!cond)
				{
					report.font.setFontSizeAndAttributes(8,false,false,false);
					report.getSectionReference("servArt").setTableCellsColSpan(cols);
					if(!clase)
					{
						report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", ""+mapaInfoArticulos.get("claseinventario_"+k),report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
					}
					report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", ""+mapaInfoArticulos.get("grupo_"+k),report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
					report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", ""+mapaInfoArticulos.get("subgrupo_"+k),report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
				}
				report.font.setFontSizeAndAttributes(8,false,false,false);
				report.getSectionReference("servArt").setTableCellsColSpan(1);
				report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", ""+mapaInfoArticulos.get("articulo_"+k),report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
				if(cantidad.equals("true"))
				{
					report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", ""+mapaInfoArticulos.get("cantidad_"+k),report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_CENTER);
				}
				if(valorUnitario.equals("true"))
				{
					report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", ""+UtilidadTexto.formatearValores((""+mapaInfoArticulos.get("valorunitario_"+k).toString()),2, true, true),report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_RIGHT);
				}
				if(valorTotal.equals("true"))
				{
					report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", ""+UtilidadTexto.formatearValores((""+mapaInfoArticulos.get("valortotal_"+k).toString()),2, true, true),report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_RIGHT);
				}
				
				/**para poner el subtotal del sub grupo**/
				{
					if((k+1)==numRegArtInfo)
					{
						temp=false;
					}
					else if(!(mapaInfoArticulos.get("subgrupo_"+k).toString().trim()).equals(mapaInfoArticulos.get("subgrupo_"+(k+1)).toString().trim()))
					{
						temp=false;
					}
				}
				if(!temp)
				{
					report.font.setFontSizeAndAttributes(8,false,false,false);
					report.getSectionReference("servArt").setTableCellsColSpan(1);
					report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", "Sub total "+mapaInfoArticulos.get("subgrupo_"+k),report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);						
					report.getSectionReference("servArt").setTableCellsColSpan(cols-1);
					report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", ""+UtilidadTexto.formatearValores(""+Utilidades.obtenerTotalSubGrupoArticulos(con, presupuesto, Integer.parseInt(mapaInfoArticulos.get("codigosubgrupo_"+k)+"")),2,true,true),report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_RIGHT);
					temp=true;
				}
				/**para poner el subtotal del grupo**/
				{
					if((k+1)==numRegArtInfo)
					{
						temp=false;
					}
					else if(!(mapaInfoArticulos.get("grupo_"+k).toString().trim()).equals(mapaInfoArticulos.get("grupo_"+(k+1)).toString().trim()))
					{
						temp=false;
					}
				}
				if(!temp)
				{
					report.font.setFontSizeAndAttributes(8,false,false,false);
					report.getSectionReference("servArt").setTableCellsColSpan(1);
					report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", "Sub total "+mapaInfoArticulos.get("grupo_"+k),report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);						
					report.getSectionReference("servArt").setTableCellsColSpan(cols-1);
					report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", ""+UtilidadTexto.formatearValores(""+Utilidades.obtenerTotalGrupoArticulos(con, presupuesto, Integer.parseInt(mapaInfoArticulos.get("codigogrupo_"+k)+"")),2,true,true),report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_RIGHT);
					temp=true;
				}
				/**para poner el subtotal de la calse inventario**/
				{
					if((k+1)==numRegArtInfo)
					{
						temp=false;
					}
					else if(!(mapaInfoArticulos.get("claseinventario_"+k).toString().trim()).equals(mapaInfoArticulos.get("claseinventario_"+(k+1)).toString().trim()))
					{
						temp=false;
					}
				}
				if(!temp)
				{
					report.font.setFontSizeAndAttributes(8,false,false,false);
					report.getSectionReference("servArt").setTableCellsColSpan(1);
					report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", "Sub total "+mapaInfoArticulos.get("claseinventario_"+k),report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);						
					report.getSectionReference("servArt").setTableCellsColSpan(cols-1);
					report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", ""+UtilidadTexto.formatearValores(""+Utilidades.obtenerTotalClaseInventarioArticulos(con, presupuesto, Integer.parseInt(mapaInfoArticulos.get("codigoclase_"+k)+"")),2,true,true),report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_RIGHT);
					temp=true;
				}
			}
		}
		//Nivel 3
		if((mapaParamArticulos.get("detarticulo_0")+"").equals("true")&&Integer.parseInt(mapaParamArticulos.get("nivel_0")+"")==3&&(mapaParamArticulos.get("detallenivel_0")+"").equals("true")&&(mapaParamArticulos.get("valoresdetalle_0")+"").equals("true")&&(mapaParamArticulos.get("subtotalnivel_0")+"").equals("false"))
		{
			boolean cond=false;
			boolean clase=false;
			for(int k = 0 ; k < numRegArtInfo ; k++)
			{
				cond=false;
				for(int i = 0  ; i < k ; i++)
				{
					if(mapaInfoArticulos.get("claseinventario_"+k).toString().equals(mapaInfoArticulos.get("claseinventario_"+i).toString()))
					{
						if(mapaInfoArticulos.get("grupo_"+k).toString().equals(mapaInfoArticulos.get("grupo_"+i).toString()))
						{
							if(mapaInfoArticulos.get("subgrupo_"+k).toString().equals(mapaInfoArticulos.get("subgrupo_"+i).toString()))
							{
								cond=true;
							}
						}
						clase=true;
					}
				}
				if(!cond)
				{
					report.font.setFontSizeAndAttributes(8,false,false,false);
					report.getSectionReference("servArt").setTableCellsColSpan(cols);
					if(!clase)
					{
						report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", ""+mapaInfoArticulos.get("claseinventario_"+k),report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
					}
					report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", ""+mapaInfoArticulos.get("grupo_"+k),report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
					report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", ""+mapaInfoArticulos.get("subgrupo_"+k),report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
				}
				report.font.setFontSizeAndAttributes(8,false,false,false);
				report.getSectionReference("servArt").setTableCellsColSpan(1);
				report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", ""+mapaInfoArticulos.get("articulo_"+k),report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
				if(cantidad.equals("true"))
				{
					report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", ""+mapaInfoArticulos.get("cantidad_"+k),report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_CENTER);
				}
				if(valorUnitario.equals("true"))
				{
					report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", ""+UtilidadTexto.formatearValores((""+mapaInfoArticulos.get("valorunitario_"+k).toString()),2, true, true),report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_RIGHT);
				}
				if(valorTotal.equals("true"))
				{
					report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", ""+UtilidadTexto.formatearValores((""+mapaInfoArticulos.get("valortotal_"+k).toString()),2, true, true),report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_RIGHT);
				}
     		}
			report.font.setFontSizeAndAttributes(8,false,false,false);
			report.getSectionReference("servArt").setTableCellsColSpan(1);
			report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", "Total: ",report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
			report.getSectionReference("servArt").setTableCellsColSpan(cols-1);
			report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", ""+UtilidadTexto.formatearValores(""+Utilidades.obtenerTotalArticulosPresupuesto(con, presupuesto),2,true,true),report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_RIGHT);
		}
		//Nivel 3
		if((mapaParamArticulos.get("detarticulo_0")+"").equals("true")&&Integer.parseInt(mapaParamArticulos.get("nivel_0")+"")==3&&(mapaParamArticulos.get("detallenivel_0")+"").equals("true")&&(mapaParamArticulos.get("valoresdetalle_0")+"").equals("false")&&(mapaParamArticulos.get("subtotalnivel_0")+"").equals("false"))
		{
			boolean cond=false;
			boolean clase=false;
			for(int k = 0 ; k < numRegArtInfo ; k++)
			{
				cond=false;
				for(int i = 0 ; i < k ; i++)
				{
					if(mapaInfoArticulos.get("claseinventario_"+k).toString().equals(mapaInfoArticulos.get("claseinventario_"+i).toString()))
					{
						if(mapaInfoArticulos.get("grupo_"+k).toString().equals(mapaInfoArticulos.get("grupo_"+i).toString()))
						{
							if(mapaInfoArticulos.get("subgrupo_"+k).toString().equals(mapaInfoArticulos.get("subgrupo_"+i).toString()))
							{
								cond=true;
							}
						}
						clase=true;
					}
				}
				if(!cond)
				{
					report.font.setFontSizeAndAttributes(8,false,false,false);
					report.getSectionReference("servArt").setTableCellsColSpan(cols);
					if(!clase)
					{
						report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", ""+mapaInfoArticulos.get("claseinventario_"+k),report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
					}
					report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", ""+mapaInfoArticulos.get("grupo_"+k),report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
					report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", ""+mapaInfoArticulos.get("subgrupo_"+k),report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
				}
				report.font.setFontSizeAndAttributes(8,false,false,false);
				report.getSectionReference("servArt").setTableCellsColSpan(cols);
				report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", ""+mapaInfoArticulos.get("articulo_"+k),report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
     		}
			report.font.setFontSizeAndAttributes(8,false,false,false);
			report.getSectionReference("servArt").setTableCellsColSpan(1);
			report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", "Total: ",report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
			report.getSectionReference("servArt").setTableCellsColSpan(cols-1);
			report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", ""+UtilidadTexto.formatearValores(""+Utilidades.obtenerTotalArticulosPresupuesto(con, presupuesto),2,true,true),report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_RIGHT);
		}
		//Nivel 3
		if((mapaParamArticulos.get("detarticulo_0")+"").equals("true")&&Integer.parseInt(mapaParamArticulos.get("nivel_0")+"")==3&&(mapaParamArticulos.get("detallenivel_0")+"").equals("true")&&(mapaParamArticulos.get("valoresdetalle_0")+"").equals("false")&&(mapaParamArticulos.get("subtotalnivel_0")+"").equals("true"))
		{
			boolean cond=false;
			boolean temp=true;
			boolean clase=false;
			for(int k = 0 ; k < numRegArtInfo ; k++)
			{
				cond=false;
				for(int i = 0 ; i < k ; i++)
				{
					if(mapaInfoArticulos.get("claseinventario_"+k).toString().equals(mapaInfoArticulos.get("claseinventario_"+i).toString()))
					{
						if(mapaInfoArticulos.get("grupo_"+k).toString().equals(mapaInfoArticulos.get("grupo_"+i).toString()))
						{
							if(mapaInfoArticulos.get("subgrupo_"+k).toString().equals(mapaInfoArticulos.get("subgrupo_"+i).toString()))
							{
								cond=true;
							}
						}
						clase=true;
					}
				}
				if(!cond)
				{
					report.font.setFontSizeAndAttributes(8,false,false,false);
					report.getSectionReference("servArt").setTableCellsColSpan(cols);
					if(!clase)
					{
						report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", ""+mapaInfoArticulos.get("claseinventario_"+k),report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
					}
					report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", ""+mapaInfoArticulos.get("grupo_"+k),report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
					report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", ""+mapaInfoArticulos.get("subgrupo_"+k),report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
				}
				report.font.setFontSizeAndAttributes(8,false,false,false);
				report.getSectionReference("servArt").setTableCellsColSpan(cols);
				report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", ""+mapaInfoArticulos.get("articulo_"+k),report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
				
				
				/**para poner el subtotal del sub grupo**/
				{
					if((k+1)==numRegArtInfo)
					{
						temp=false;
					}
					else if(!(mapaInfoArticulos.get("subgrupo_"+k).toString().trim()).equals(mapaInfoArticulos.get("subgrupo_"+(k+1)).toString().trim()))
					{
						temp=false;
					}
				}
				if(!temp)
				{
					report.font.setFontSizeAndAttributes(8,false,false,false);
					report.getSectionReference("servArt").setTableCellsColSpan(1);
					report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", "Sub total "+mapaInfoArticulos.get("subgrupo_"+k),report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);						
					report.getSectionReference("servArt").setTableCellsColSpan(cols-1);
					report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", ""+UtilidadTexto.formatearValores(""+Utilidades.obtenerTotalSubGrupoArticulos(con, presupuesto, Integer.parseInt(mapaInfoArticulos.get("codigosubgrupo_"+k)+"")),2,true,true),report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_RIGHT);
					temp=true;
				}
				/**para poner el subtotal del grupo**/
				{
					if((k+1)==numRegArtInfo)
					{
						temp=false;
					}
					else if(!(mapaInfoArticulos.get("grupo_"+k).toString().trim()).equals(mapaInfoArticulos.get("grupo_"+(k+1)).toString().trim()))
					{
						temp=false;
					}
				}
				if(!temp)
				{
					report.font.setFontSizeAndAttributes(8,false,false,false);
					report.getSectionReference("servArt").setTableCellsColSpan(1);
					report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", "Sub total "+mapaInfoArticulos.get("grupo_"+k),report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);						
					report.getSectionReference("servArt").setTableCellsColSpan(cols-1);
					report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", ""+UtilidadTexto.formatearValores(""+Utilidades.obtenerTotalGrupoArticulos(con, presupuesto, Integer.parseInt(mapaInfoArticulos.get("codigogrupo_"+k)+"")),2,true,true),report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_RIGHT);
					temp=true;
				}
				/**para poner el subtotal de la calse inventario**/
				{
					if((k+1)==numRegArtInfo)
					{
						temp=false;
					}
					else if(!(mapaInfoArticulos.get("claseinventario_"+k).toString().trim()).equals(mapaInfoArticulos.get("claseinventario_"+(k+1)).toString().trim()))
					{
						temp=false;
					}
				}
				if(!temp)
				{
					report.font.setFontSizeAndAttributes(8,false,false,false);
					report.getSectionReference("servArt").setTableCellsColSpan(1);
					report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", "Sub total "+mapaInfoArticulos.get("claseinventario_"+k),report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);						
					report.getSectionReference("servArt").setTableCellsColSpan(cols-1);
					report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", ""+UtilidadTexto.formatearValores(""+Utilidades.obtenerTotalClaseInventarioArticulos(con, presupuesto, Integer.parseInt(mapaInfoArticulos.get("codigoclase_"+k)+"")),2,true,true),report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_RIGHT);
					temp=true;
				}
     		}
			
		}
		//Nivel 3
		if((mapaParamArticulos.get("detarticulo_0")+"").equals("true")&&Integer.parseInt(mapaParamArticulos.get("nivel_0")+"")==3&&(mapaParamArticulos.get("detallenivel_0")+"").equals("false")&&(mapaParamArticulos.get("valoresdetalle_0")+"").equals("true")&&(mapaParamArticulos.get("subtotalnivel_0")+"").equals("false"))
		{
			boolean cond=false;
			boolean clase=false;
			for(int k = 0 ; k < numRegArtInfo ; k++)
			{
				cond=false;
				for(int i = 0 ; i < k ;  i++)
				{
					if(mapaInfoArticulos.get("claseinventario_"+k).toString().equals(mapaInfoArticulos.get("claseinventario_"+i).toString()))
					{
						if(mapaInfoArticulos.get("grupo_"+k).toString().equals(mapaInfoArticulos.get("grupo_"+i).toString()))
						{
							if(mapaInfoArticulos.get("subgrupo_"+k).toString().equals(mapaInfoArticulos.get("subgrupo_"+i).toString()))
							{
								cond=true;
							}
						}
						clase=true;
					}
				}
				if(!cond)
				{
					report.font.setFontSizeAndAttributes(8,false,false,false);
					report.getSectionReference("servArt").setTableCellsColSpan(cols);
					if(!clase)
					{
						report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", ""+mapaInfoArticulos.get("claseinventario_"+k),report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
					}
					report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", ""+mapaInfoArticulos.get("grupo_"+k),report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
					report.getSectionReference("servArt").setTableCellsColSpan(1);
					report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", ""+mapaInfoArticulos.get("subgrupo_"+k),report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
				}
				report.font.setFontSizeAndAttributes(8,false,false,false);
				report.getSectionReference("servArt").setTableCellsColSpan(3);
				if(cantidad.equals("true"))
				{
					int cant=0;
					boolean esta=false;
					for(int  i = 0 ; i < k ; i++)
					{
						if(mapaInfoArticulos.get("claseinventario_"+k).toString().equals(mapaInfoArticulos.get("claseinventario_"+i).toString())&&mapaInfoArticulos.get("grupo_"+k).toString().equals(mapaInfoArticulos.get("grupo_"+i).toString()))
						{
							esta=true;
						}
					}
					if(!esta)
					{
						for(int i = 0 ; i < numRegArtInfo ; i++)
						{
							if(mapaInfoArticulos.get("claseinventario_"+k).toString().equals(mapaInfoArticulos.get("claseinventario_"+i).toString()))
							{
								if(mapaInfoArticulos.get("grupo_"+k).toString().equals(mapaInfoArticulos.get("grupo_"+i).toString()))
								{
									cant+=Integer.parseInt(mapaInfoArticulos.get("cantidad_"+k)+"");
								}
							}
						}
						report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", ""+cant,report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
					}
				}
     		}
			report.font.setFontSizeAndAttributes(8,false,false,false);
			report.getSectionReference("servArt").setTableCellsColSpan(1);
			report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", "Total: ",report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
			report.getSectionReference("servArt").setTableCellsColSpan(cols-1);
			report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", ""+UtilidadTexto.formatearValores(""+Utilidades.obtenerTotalArticulosPresupuesto(con, presupuesto),2,true,true),report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_RIGHT);
		}
		//Nivel 4 
		if((mapaParamArticulos.get("detarticulo_0")+"").equals("true")&&Integer.parseInt(mapaParamArticulos.get("nivel_0")+"")==4&&(mapaParamArticulos.get("detallenivel_0")+"").equals("false")&&(mapaParamArticulos.get("valoresdetalle_0")+"").equals("false")&&(mapaParamArticulos.get("subtotalnivel_0")+"").equals("false"))
		{
			for(int k = 0 ; k < numRegArtInfo ; k++)
			{
				
				report.font.setFontSizeAndAttributes(8,false,false,false);
				report.getSectionReference("servArt").setTableCellsColSpan(cols);
				report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", ""+mapaInfoArticulos.get("articulo_"+k),report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
     		}
		}
		//Nivel 4 
		if((mapaParamArticulos.get("detarticulo_0")+"").equals("true")&&Integer.parseInt(mapaParamArticulos.get("nivel_0")+"")==4&&(mapaParamArticulos.get("detallenivel_0")+"").equals("false")&&(mapaParamArticulos.get("valoresdetalle_0")+"").equals("true")&&(mapaParamArticulos.get("subtotalnivel_0")+"").equals("false"))
		{
			for(int k = 0 ; k < numRegArtInfo ; k++)
			{
				
				report.font.setFontSizeAndAttributes(8,false,false,false);
				report.getSectionReference("servArt").setTableCellsColSpan(1);
				report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", ""+mapaInfoArticulos.get("articulo_"+k),report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
				if(cantidad.equals("true"))
				{
					report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", ""+mapaInfoArticulos.get("cantidad_"+k),report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_CENTER);
				}
				if(valorUnitario.equals("true"))
				{
					report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", ""+UtilidadTexto.formatearValores((""+mapaInfoArticulos.get("valorunitario_"+k).toString()),2, true, true),report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_RIGHT);
				}
				if(valorTotal.equals("true"))
				{
					report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", ""+UtilidadTexto.formatearValores((""+mapaInfoArticulos.get("valortotal_"+k).toString()),2, true, true),report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_RIGHT);
				}
     		}
		}
		//Nivel 4 
		if((mapaParamArticulos.get("detarticulo_0")+"").equals("true")&&Integer.parseInt(mapaParamArticulos.get("nivel_0")+"")==4&&(mapaParamArticulos.get("valoresdetalle_0")+"").equals("true")&&(mapaParamArticulos.get("subtotalnivel_0")+"").equals("true"))
		{
			for(int k = 0 ; k < numRegArtInfo ; k++)
			{
				
				report.font.setFontSizeAndAttributes(8,false,false,false);
				report.getSectionReference("servArt").setTableCellsColSpan(1);
				report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", ""+mapaInfoArticulos.get("articulo_"+k),report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
				if(cantidad.equals("true"))
				{
					report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", ""+mapaInfoArticulos.get("cantidad_"+k),report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_CENTER);
				}
				if(valorUnitario.equals("true"))
				{
					report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", ""+UtilidadTexto.formatearValores((""+mapaInfoArticulos.get("valorunitario_"+k).toString()),2, true, true),report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_RIGHT);
				}
				if(valorTotal.equals("true"))
				{
					report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", ""+UtilidadTexto.formatearValores((""+mapaInfoArticulos.get("valortotal_"+k).toString()),2, true, true),report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_RIGHT);
				}
     		}
			
			report.font.setFontSizeAndAttributes(8,false,false,false);
			report.getSectionReference("servArt").setTableCellsColSpan(1);
			report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", "Total: ",report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
			report.getSectionReference("servArt").setTableCellsColSpan(cols-1);
			report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", ""+UtilidadTexto.formatearValores(""+Utilidades.obtenerTotalArticulosPresupuesto(con, presupuesto),2,true,true),report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_RIGHT);
			
		}
		//Nivel 4 
		if((mapaParamArticulos.get("detarticulo_0")+"").equals("true")&&Integer.parseInt(mapaParamArticulos.get("nivel_0")+"")==4&&(mapaParamArticulos.get("valoresdetalle_0")+"").equals("true")&&(mapaParamArticulos.get("subtotalnivel_0")+"").equals("false"))
		{
			for(int k = 0 ; k < numRegArtInfo ; k++)
			{
				
				report.font.setFontSizeAndAttributes(8,false,false,false);
				report.getSectionReference("servArt").setTableCellsColSpan(1);
				report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", ""+mapaInfoArticulos.get("articulo_"+k),report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
				if(cantidad.equals("true"))
				{
					report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", ""+mapaInfoArticulos.get("cantidad_"+k),report.font,iTextBaseDocument.ALIGNMENT_CENTER,iTextBaseDocument.ALIGNMENT_CENTER);
				}
				if(valorUnitario.equals("true"))
				{
					report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", ""+UtilidadTexto.formatearValores((""+mapaInfoArticulos.get("valorunitario_"+k).toString()),2, true, true),report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_RIGHT);
				}
				if(valorTotal.equals("true"))
				{
					report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", ""+UtilidadTexto.formatearValores((""+mapaInfoArticulos.get("valortotal_"+k).toString()),2, true, true),report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_RIGHT);
				}
     		}
			
			report.font.setFontSizeAndAttributes(8,false,false,false);
			report.getSectionReference("servArt").setTableCellsColSpan(1);
			report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", "Total: ",report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
			report.getSectionReference("servArt").setTableCellsColSpan(cols-1);
			report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", ""+UtilidadTexto.formatearValores(""+Utilidades.obtenerTotalArticulosPresupuesto(con, presupuesto),2,true,true),report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_RIGHT);
			
		}
		//Nivel 4 
		if((mapaParamArticulos.get("detarticulo_0")+"").equals("true")&&Integer.parseInt(mapaParamArticulos.get("nivel_0")+"")==4&&(mapaParamArticulos.get("valoresdetalle_0")+"").equals("false")&&(mapaParamArticulos.get("subtotalnivel_0")+"").equals("true"))
		{
			for(int k = 0 ; k < numRegArtInfo ; k++)
			{
				
				report.font.setFontSizeAndAttributes(8,false,false,false);
				report.getSectionReference("servArt").setTableCellsColSpan(cols);
				report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", ""+mapaInfoArticulos.get("articulo_"+k),report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
     		}
			
			report.font.setFontSizeAndAttributes(8,false,false,false);
			report.getSectionReference("servArt").setTableCellsColSpan(1);
			report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", "Total: ",report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
			report.getSectionReference("servArt").setTableCellsColSpan(cols-1);
			report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", ""+UtilidadTexto.formatearValores(""+Utilidades.obtenerTotalArticulosPresupuesto(con, presupuesto),2,true,true),report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_RIGHT);
			
		}
//		Nivel 4 
		if((mapaParamArticulos.get("detarticulo_0")+"").equals("true")&&Integer.parseInt(mapaParamArticulos.get("nivel_0")+"")==4&&(mapaParamArticulos.get("valoresdetalle_0")+"").equals("false")&&(mapaParamArticulos.get("subtotalnivel_0")+"").equals("false"))
		{
			for(int k = 0 ; k < numRegArtInfo ; k++)
			{
				
				report.font.setFontSizeAndAttributes(8,false,false,false);
				report.getSectionReference("servArt").setTableCellsColSpan(cols);
				report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", ""+mapaInfoArticulos.get("articulo_"+k),report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
     		}
			
			report.font.setFontSizeAndAttributes(8,false,false,false);
			report.getSectionReference("servArt").setTableCellsColSpan(1);
			report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", "Total: ",report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
			report.getSectionReference("servArt").setTableCellsColSpan(cols-1);
			report.getSectionReference("servArt").addTableTextCellAligned("tablaServArt", ""+UtilidadTexto.formatearValores(""+Utilidades.obtenerTotalArticulosPresupuesto(con, presupuesto),2,true,true),report.font,iTextBaseDocument.ALIGNMENT_RIGHT,iTextBaseDocument.ALIGNMENT_RIGHT);
			
		}
		return true;
	}
}