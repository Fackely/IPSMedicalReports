/*
 * EvolucionPdf.java 
 * Autor			:  coviedo
 * Creado el	:  15-oct-2004
 * 
 * Lenguaje		: Java
 * Compilador	: J2SDK 1.4.1_01
 * 
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 */
package com.princetonsa.pdf;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.atencion.Diagnostico;
import com.princetonsa.mundo.atencion.SignoVital;
import util.ConstantesBD;
import util.InfoDatosInt;
import util.UtilidadTexto;
import util.ValoresPorDefecto;
import util.pdf.PdfReports;
import util.pdf.iTextBaseDocument;
import util.pdf.iTextBaseFont;

/**
 * Clase para manejar la generación de PDF's para las evoluciones.
 *
 * @version 1.0, 15-oct-2004
 * @author <a href="mailto:carlos@princetonsa.com">Carlos Oviedo</a>
 */
public class EvolucionPdf {

	
	/**
	 * @param filename nombre del archivo pdf a generar.
	 * @param datos mapa de datos cuyas llaves son las siguientes:
	 * @param paciente 
	 */
	public static void pdfEvolucion(String filename, HashMap datos, PersonaBasica paciente, UsuarioBasico usuario) {
		float[] w;
		
		//Verificar que se hayan suministrado los datos
		if (datos == null || datos.size() == 0) 
			return;
		
		
		PdfReports report = new PdfReports();
			

		 //Obtener datos del mapa
		//String institucion = (String) datos.get("institucion");
		//String nitInstitucion = (String) datos.get("nitInstitucion");
		String fechaEvolucion = (String) datos.get("fechaEvolucion");
		String horaEvolucion = (String) datos.get("horaEvolucion");

	
		//Armar el encabezado
		
		InstitucionBasica institucionBasica= new InstitucionBasica();
		institucionBasica.cargarXConvenio(usuario.getCodigoInstitucionInt(), paciente.getCodigoConvenio());
		
		report.setReportBaseHeader1(institucionBasica, institucionBasica.getNit(), "EVOLUCION DEL PACIENTE Fecha: " + fechaEvolucion + " " + horaEvolucion);
		
			
	   
		report.font.useFont(iTextBaseFont.FONT_COURIER, true, true, true);
		report.openReport(ValoresPorDefecto.getFilePath() +filename);
		
		report.font.setFontAttributes(0x000000, 11, true, false, false);
		
		report.font.useFont(iTextBaseFont.FONT_TIMES, false, false, false);
		
		report.setReportTitleSectionAtributes(0xFFFFFF, 0xFFFFFF,0x000000, 11f);
		report.setReportDataSectionAtributes(0xFFFFFF, 0xFFFFFF, 0x000000, 11f);
			
			
			
		//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

		//Se adicionan los datos al documento
		String[] sectionData = new String[3];
			
		//Apellidos y nombres
		String apellidosNombres = paciente.getApellidosNombresPersona(false);
		String textData = (apellidosNombres != null ? "Apellidos y Nombres: " + apellidosNombres : "Apellidos y Nombres: ");
		sectionData[0] = textData;
		
		//Identificacion
		String tipoId = (String)datos.get("tipoIdentificacion");
		String numeroId = (String)datos.get("numeroIdentificacion");
		String ciudadId = (String)datos.get("ciudadIdentificacion");
		textData = (tipoId != null && numeroId != null && ciudadId != null ? tipoId + " No. " + numeroId + " De: " + ciudadId : "Sin identificación");
		sectionData[1] = textData;
		//Edad y Sexo
		String edad = (String)datos.get("edad");
		textData = (edad != null ? "Edad: " + edad + "    Sexo: " : "Edad: " + "    Sexo: ");
		String sexo = (String)datos.get("sexo");
		if (sexo != null) textData += sexo;
		sectionData[1]  += "   " +textData;
		
		//Cama
		String cama = (String)datos.get("numeroCama");
		String descripcionCama = (String)datos.get("descripcionCama");
		textData = (cama != null ? "Cama: " + cama : "Cama: ");
		textData  += " " +descripcionCama;
		sectionData[2] = textData;
		//Area
		String area = (String)datos.get("area");
		textData = (area != null ? "Area: " + area : "Area: ");
		sectionData[2]  += "   " +textData;
		//Tipo de cuidado
		String tipoCuidado = (String)datos.get("tipoCuidado");
		textData = (tipoCuidado != null && tipoCuidado.length() != 0 ? "Tipo de Cuidado: " + tipoCuidado : "    ");
		sectionData[2]  += "   " +textData;
		
		
		//Seccion: Información personal
		report.createSection("infoPersonal", "infoPersonalTable", 4, 1, 10);
		report.addSectionTitle("infoPersonal", "infoPersonalTable", "Información Personal");
		report.addSectionData("infoPersonal", "infoPersonalTable", sectionData);
		report.addSectionToDocument("infoPersonal");
			
		//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
			
			
			
		//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
			
		sectionData = new String[1];
		
		//Datos subjetivos
		String datosSubjetivos = (String)datos.get("datosSubjetivos");
		boolean hayDatos = (datosSubjetivos != null && datosSubjetivos.length() >0);
		textData = (datosSubjetivos != null && hayDatos  ? datosSubjetivos :  "");
		sectionData[0] = textData;
		
		//Seccion: Datos subjetivos
		if (hayDatos) {
			report.createSection("datosSubjetivos",  "datosSubjetivosTable", 1, 2, 10);

			w = new float [2];
			w[0] = 20f; 
			w[1] = 80f;
			report.setColumnsWidths("datosSubjetivos", "datosSubjetivosTable",  w);

			report.font.setFontAttributes(0x000000, 11, true, false, false);
			report.getSectionReference("datosSubjetivos").addTableTextCell("datosSubjetivosTable", "Datos Subjetivos", report.font);
			report.font.setFontAttributes(0x000000, 11, false, false, false);
			report.getSectionReference("datosSubjetivos").addTableTextCell("datosSubjetivosTable", sectionData[0], report.font);
			report.addSectionToDocument("datosSubjetivos");
		}

		HashMap mapaParam=(HashMap)datos.get("datos_subj_param");
		int numRegistrosParam=Integer.parseInt(mapaParam.get("numRegistros")+"");
		if(numRegistrosParam>0)
		{
			if(hayDatos)
			{
				report.createSection("datosSubjetivosParam",  "datosSubjetivosTable", numRegistrosParam, 2, 10);
			}
			else
			{
				report.createSection("datosSubjetivosParam",  "datosSubjetivosTable", numRegistrosParam+1, 2, 10);
				report.font.setFontAttributes(0x000000, 11, true, false, false);
				report.getSectionReference("datosSubjetivosParam").setTableCellsColSpan(2);
				report.getSectionReference("datosSubjetivosParam").addTableTextCell("datosSubjetivosTable", "Datos Subjetivos", report.font);
				report.getSectionReference("datosSubjetivosParam").setTableCellsColSpan(1);
			}

			w = new float [2];
			w[0] = 20f; 
			w[1] = 80f;
			report.setColumnsWidths("datosSubjetivosParam", "datosSubjetivosTable",  w);

			for(int i=0; i<numRegistrosParam; i++)
			{
				int tipo=Integer.parseInt(mapaParam.get("tipo_"+i)+"");
				String nombre=mapaParam.get("nombre_"+i)+"";
				report.font.setFontAttributes(0x000000, 11, true, false, false);
				report.getSectionReference("datosSubjetivosParam").addTableTextCell("datosSubjetivosTable", nombre, report.font);
				String valor=mapaParam.get("valor_"+i)+"";
				if(tipo==3)
				{
					if(UtilidadTexto.getBoolean(valor))
					{
						valor="Si";
					}
					else
					{
						valor="No";
					}
				}
				report.font.setFontAttributes(0x000000, 11, false, false, false);
				report.getSectionReference("datosSubjetivosParam").addTableTextCell("datosSubjetivosTable", valor, report.font);
			}
			report.addSectionToDocument("datosSubjetivosParam");
		}

			
		//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
			
			

			
		//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
			
		sectionData = new String[30];
		hayDatos = false;
		
		//Datos Objetivos
		Collection signosVitales =  (Collection) datos.get("signosVitales");

		/*
		String liquidosAdministrados = (String) datos.get("liquidosAdministrados");
		String liquidosEliminados = (String) datos.get("liquidosEliminados");
		String aporteHidrico = (String) datos.get("aporteHidrico");
		String aporteCalorico = (String) datos.get("aporteCalorico");
		String diuresis = (String) datos.get("dioresis");
		*/
		Vector<Vector> balance=(Vector)datos.get("balanceLiquidos");
		hayDatos = (balance!=null && balance.size()>0) || (!(signosVitales != null && signosVitales.isEmpty()))/*  || (liquidosAdministrados.length() > 0) || (liquidosEliminados.length() > 0) || (aporteHidrico.length() > 0) || (aporteCalorico.length() > 0) || (diuresis.length() > 0)*/;
		
			
		if (hayDatos)
		{
			Iterator it = signosVitales.iterator();
			String etiqueta, valor, unidades;
			int i, numSignosVitales=0, numBalanceDeLiquidos=0;
			
			//Obtener los signos vitales
			i = 0;
			while (it.hasNext())
			{
				SignoVital sv = (SignoVital) it.next();
			
				etiqueta = sv.getValue();
				valor = sv.getValorSignoVital();
				unidades = sv.getUnidadMedida();
				textData = (etiqueta.length() >0 && valor.length() > 0 ? etiqueta + ":" + "   " + valor + " " + unidades : "    " );
				sectionData[i] = textData;
				i++;
				numSignosVitales ++;
			}
		
			// Obtener Balance de liquidos
			
			numBalanceDeLiquidos=balance.size();
			for(int zz=0;zz<numBalanceDeLiquidos;zz++)
			{
				Vector fila=balance.elementAt(zz);
				sectionData[i]=(fila.elementAt(0)+"")+":    "+(fila.elementAt(1)+"");
				i++;
			}
			/*
			if(!liquidosAdministrados.equals("")){
				sectionData[i] = "Liquidos Administrados: " +liquidosAdministrados;
				i++;
				numBalanceDeLiquidos ++;
			}
			
			if(!liquidosEliminados.equals("")){
				sectionData[i] = "Liquidos Eliminados: " +liquidosEliminados;
				i++;
				numBalanceDeLiquidos ++;
			}
			
			if(!aporteHidrico.equals("")){
				sectionData[i] = "Aporte Hidrico: " +aporteHidrico;
				i++;
				numBalanceDeLiquidos ++;
			}
			
			if(!aporteCalorico.equals("")){
				sectionData[i] = "Aporte Calorico: " +aporteCalorico;
				i++;
				numBalanceDeLiquidos ++;
			}

			if(!diuresis.equals("")){
				sectionData[i] = "Diuresis: " +diuresis;
				i++;
				numBalanceDeLiquidos ++;
			}
			*/
			
			
			//Seccion: Datos objetivos (signos vitales y Balance de liquidos)
			if(numSignosVitales >0 || numBalanceDeLiquidos > 0){
				report.createSection("datosObjetivos", "datosObjetivosTable", 1, 1, 10);
				report.addSectionTitle("datosObjetivos", "datosObjetivosTable", "Datos Objetivos");
				report.addSectionToDocument("datosObjetivos");
				
				if (numSignosVitales > 0){
					String[] signosVitalesArray = new String[numSignosVitales];
					for(int k =0; k <numSignosVitales; k++)
						signosVitalesArray[k] = sectionData[k];
					
					
					report.createSection("datosObjetivos1", "signosVitalesTable", 1, numSignosVitales, 2);
					report.addSectionData("datosObjetivos1", "signosVitalesTable", signosVitalesArray);
					report.addSectionToDocument("datosObjetivos1");
				}

				if (numBalanceDeLiquidos > 0){
					String[] balanceDeLiquidosArray = new String[numBalanceDeLiquidos];
					for(int k =0; k <numBalanceDeLiquidos; k++)
						balanceDeLiquidosArray[k] = sectionData[(k+numSignosVitales)];
					
					report.createSection("datosObjetivos2", "balanceDeLiquidosTable", numBalanceDeLiquidos, 1, 2);
					report.addSectionData("datosObjetivos2", "balanceDeLiquidosTable", balanceDeLiquidosArray);
					report.addSectionToDocument("datosObjetivos2");
				}
						
				
			}
		   }
		   
			
			sectionData = new String[1];
			
			//Hallazgos Importantes
			String hallazgosImportantes = (String) datos.get("hallazgosImportantes");
			hayDatos = (hallazgosImportantes != null && hallazgosImportantes.length() > 0);
			
			//Seccion: Hallazgos Importantes
			if (hayDatos) {
				sectionData[0] = hallazgosImportantes;
				report.createSection("hallazgosImportantes", "hallazgosImportantesTable", 1, 2, 10);

					w = new float [2];
					w[0] = 15f; 
					w[1] = 85f;
					report.setColumnsWidths("hallazgosImportantes", "hallazgosImportantesTable",  w);

					report.font.setFontAttributes(0x000000, 11, true, false, false);
					report.getSectionReference("hallazgosImportantes").addTableTextCell("hallazgosImportantesTable", "Hallazgos Importantes", report.font);
					report.font.setFontAttributes(0x000000, 11, false, false, false);
					report.getSectionReference("hallazgosImportantes").addTableTextCell("hallazgosImportantesTable", sectionData[0], report.font);
					report.addSectionToDocument("hallazgosImportantes");
		    }
		    
		    mapaParam=(HashMap)datos.get("datos_ogj_param");
		    numRegistrosParam=Integer.parseInt(mapaParam.get("numRegistros")+"");
		    if(numRegistrosParam>0)
		    {
		    	if(hayDatos)
		    	{
			    	report.createSection("hallazgosImportantesParam",  "hallazgosImportantesTable", numRegistrosParam, 2, 10);
		    	}
		    	else
		    	{
			    	report.createSection("hallazgosImportantesParam",  "hallazgosImportantesTable", numRegistrosParam+1, 2, 10);
			    	report.font.setFontAttributes(0x000000, 11, true, false, false);
			    	report.getSectionReference("hallazgosImportantesParam").setTableCellsColSpan(2);
			    	report.getSectionReference("hallazgosImportantesParam").addTableTextCell("hallazgosImportantesTable", "Hallazgos Importantes", report.font);
			    	report.getSectionReference("hallazgosImportantesParam").setTableCellsColSpan(1);
		    	}

		    	w = new float [2];
		    	w[0] = 20f; 
		    	w[1] = 80f;
		    	report.setColumnsWidths("hallazgosImportantesParam", "hallazgosImportantesTable",  w);

			    for(int i=0; i<numRegistrosParam; i++)
		    	{
		    		int tipo=Integer.parseInt(mapaParam.get("tipo_"+i)+"");
					String nombre=mapaParam.get("nombre_"+i)+"";
			    	report.font.setFontAttributes(0x000000, 11, true, false, false);
			    	report.getSectionReference("hallazgosImportantesParam").addTableTextCell("hallazgosImportantesTable", nombre, report.font);
					String valor=mapaParam.get("valor_"+i)+"";
		    		if(tipo==3)
		    		{
		    			if(UtilidadTexto.getBoolean(valor))
		    			{
		    				valor="Si";
		    			}
		    			else
		    			{
		    				valor="No";
		    			}
		    		}
			    	report.font.setFontAttributes(0x000000, 11, false, false, false);
			    	report.getSectionReference("hallazgosImportantesParam").addTableTextCell("hallazgosImportantesTable", valor, report.font);
		    	}
		    	report.addSectionToDocument("hallazgosImportantesParam");
		    }

		    //Analisis
		    String analisis  = (String) datos.get("analisis");
		    hayDatos = (analisis != null && analisis.length() > 0);
		    
		    //Seccion Analisis
		    if (hayDatos) {
		    	sectionData[0] = analisis;
		    	report.createSection("analisis", "analisisTable", 1, 2, 10);

		    	w = new float [2];
		    	w[0] = 10f; 
		    	w[1] = 90f;
		    	report.setColumnsWidths("analisis", "analisisTable",  w);

		    	report.font.setFontAttributes(0x000000, 11, true, false, false);
		    	report.getSectionReference("analisis").addTableTextCell("analisisTable", "Análisis", report.font);
		    	report.font.setFontAttributes(0x000000, 11, false, false, false);
		    	report.getSectionReference("analisis").addTableTextCell("analisisTable", sectionData[0], report.font);
		    	report.addSectionToDocument("analisis");
		    }
		    
		    mapaParam=(HashMap)datos.get("analisis_param");
		    numRegistrosParam=Integer.parseInt(mapaParam.get("numRegistros")+"");
		    if(numRegistrosParam>0)
		    {
		    	if(hayDatos)
		    	{
			    	report.createSection("analisisParam",  "analisisTable", numRegistrosParam, 2, 10);
		    	}
		    	else
		    	{
			    	report.createSection("analisisParam",  "analisisTable", numRegistrosParam+1, 2, 10);
			    	report.font.setFontAttributes(0x000000, 11, true, false, false);
			    	report.getSectionReference("analisisParam").setTableCellsColSpan(2);
			    	report.getSectionReference("analisisParam").addTableTextCell("analisisTable", "Análisis", report.font);
			    	report.getSectionReference("analisisParam").setTableCellsColSpan(1);
		    	}

		    	w = new float [2];
		    	w[0] = 20f; 
		    	w[1] = 80f;
		    	report.setColumnsWidths("analisisParam", "analisisTable",  w);

			    for(int i=0; i<numRegistrosParam; i++)
		    	{
		    		int tipo=Integer.parseInt(mapaParam.get("tipo_"+i)+"");
					String nombre=mapaParam.get("nombre_"+i)+"";
			    	report.font.setFontAttributes(0x000000, 11, true, false, false);
			    	report.getSectionReference("analisisParam").addTableTextCell("analisisTable", nombre, report.font);
					String valor=mapaParam.get("valor_"+i)+"";
		    		if(tipo==3)
		    		{
		    			if(UtilidadTexto.getBoolean(valor))
		    			{
		    				valor="Si";
		    			}
		    			else
		    			{
		    				valor="No";
		    			}
		    		}
			    	report.font.setFontAttributes(0x000000, 11, false, false, false);
			    	report.getSectionReference("analisisParam").addTableTextCell("analisisTable", valor, report.font);
		    	}
		    	report.addSectionToDocument("analisisParam");
		    }

		    
		    //Plan
		    String plan = (String) datos.get("plan");
		    hayDatos = (plan != null && plan.length() > 0);
		    
		    //Seccion Plan
		    if (hayDatos) {
		    	sectionData[0] = plan;
		    	report.createSection("plan", "planTable", 1, 2, 10);

		    	w = new float [2];
		    	w[0] = 10f; 
		    	w[1] = 90f;
		    	report.setColumnsWidths("plan", "planTable",  w);

		    	report.font.setFontAttributes(0x000000, 11, true, false, false);
		    	report.getSectionReference("plan").addTableTextCell("planTable", "Plan", report.font);
		    	report.font.setFontAttributes(0x000000, 11, false, false, false);
		    	report.getSectionReference("plan").addTableTextCell("planTable", sectionData[0], report.font);
		    	report.addSectionToDocument("plan");
		    }
		    
		    mapaParam=(HashMap)datos.get("plan_param");
		    numRegistrosParam=Integer.parseInt(mapaParam.get("numRegistros")+"");
		    if(numRegistrosParam>0)
		    {
		    	if(hayDatos)
		    	{
			    	report.createSection("planParam",  "planTable", numRegistrosParam, 2, 10);
		    	}
		    	else
		    	{
			    	report.createSection("planParam",  "planTable", numRegistrosParam+1, 2, 10);
			    	report.font.setFontAttributes(0x000000, 11, true, false, false);
			    	report.getSectionReference("planParam").setTableCellsColSpan(2);
			    	report.getSectionReference("planParam").addTableTextCell("planTable", "Plan", report.font);
			    	report.getSectionReference("planParam").setTableCellsColSpan(1);
		    	}

		    	w = new float [2];
		    	w[0] = 20f; 
		    	w[1] = 80f;
		    	report.setColumnsWidths("planParam", "planTable",  w);

			    for(int i=0; i<numRegistrosParam; i++)
		    	{
		    		int tipo=Integer.parseInt(mapaParam.get("tipo_"+i)+"");
					String nombre=mapaParam.get("nombre_"+i)+"";
			    	report.font.setFontAttributes(0x000000, 11, true, false, false);
			    	report.getSectionReference("planParam").addTableTextCell("planTable", nombre, report.font);
					String valor=mapaParam.get("valor_"+i)+"";
		    		if(tipo==3)
		    		{
		    			if(UtilidadTexto.getBoolean(valor))
		    			{
		    				valor="Si";
		    			}
		    			else
		    			{
		    				valor="No";
		    			}
		    		}
			    	report.font.setFontAttributes(0x000000, 11, false, false, false);
			    	report.getSectionReference("planParam").addTableTextCell("planTable", valor, report.font);
		    	}
		    	report.addSectionToDocument("planParam");
		    }


		    		    
		    String dxIngresoStr = "";
		    String dxPrincipalStr = "";
		    String dxComplicacionStr = "";
		    ArrayList dxRelacionadosArray = new ArrayList();
		    String diagnosticoStr = "";
		    Iterator it;
		    
		    
		    
		    //Diagnosticos
		    Map dxIngreso              = (Map) datos.get("dxIngreso");
		    Collection dxDefinitivos = (Collection) datos.get("dxDefinitivos");
		    dxComplicacionStr = (String) datos.get("dxComplicacion");
		    
		    hayDatos = (dxIngreso != null && dxIngreso.size() > 0 );
		    //Seccion Diagnosticos
		    if (hayDatos) 
		    {
		    	
		    	dxIngresoStr = dxIngreso.get("acronimo") + "-" + dxIngreso.get("tipoCie") + " " + dxIngreso.get("nombre");
		    	
		    	
		      if (dxDefinitivos != null && dxDefinitivos.size() > 0) {
		        	it = dxDefinitivos.iterator();
		        	
		        	while (it.hasNext()) {
		        		Diagnostico diag = (Diagnostico) it.next();
		        		
		        		if (diag.isPrincipal()) {
		        			dxPrincipalStr = diag.getAcronimo() +"-" + diag.getTipoCIE() +"-" +diag.getNombre();		        			 
		        		}
		        		else {
		        			diagnosticoStr = diag.getNumero() + "-" +diag.getAcronimo() +"-" + diag.getTipoCIE() +"-" +diag.getNombre();
		        			dxRelacionadosArray.add(diagnosticoStr);
		        		}
		        	}
		      }
		        	

		      if(dxPrincipalStr.length() > 0  || dxIngresoStr.length() > 0 || dxRelacionadosArray.size() > 0  || dxComplicacionStr.length() >0){
		    		//report.createSection("diagnosticos", "diagnosticosTable", 1, 2, 2);
		    		//report.addSectionToDocument("diagnosticos");
		    		
		    	  	String codigoViaIngreso = datos.get("viaIngreso") + "";
			    	
			    	//Solo Hospitalizaciones tienen diagnóstico de ingreso
		    		if (dxIngresoStr.length() > 0 && codigoViaIngreso.equals(ConstantesBD.codigoViaIngresoHospitalizacion+""))
		    		{
				    	report.createSection("diagnosticos0", "diagnosticosTable", 1, 2, 2);
				    	
				    	w = new float [2];
				    	w[0] = 20f; 
				    	w[1] = 80f;
				    	report.setColumnsWidths("diagnosticos0", "diagnosticosTable",  w);
				    	
				    	report.font.setFontAttributes(0x000000, 11, true, false, false);
				    	
				    	
				    	report.getSectionReference("diagnosticos0").addTableTextCell("diagnosticosTable", "Dx de Ingreso: ", report.font);
				    	report.font.setFontAttributes(0x000000, 11, false, false, false);
				    	report.getSectionReference("diagnosticos0").addTableTextCell("diagnosticosTable", dxIngresoStr, report.font);
				    	report.addSectionToDocument("diagnosticos0");
				    }
				    	
				    if (dxPrincipalStr.length() > 0 ){
				    	report.createSection("diagnosticos1", "diagnosticosTable", 1, 2, 2);

				    	w = new float [2];
				    	w[0] = 20f; 
				    	w[1] = 80f;
				    	report.setColumnsWidths("diagnosticos1", "diagnosticosTable",  w);

				    	report.font.setFontAttributes(0x000000, 11, true, false, false);
				    	report.getSectionReference("diagnosticos1").addTableTextCell("diagnosticosTable", "Dx Principal: ", report.font);
				    	report.font.setFontAttributes(0x000000, 11, false, false, false);
				    	report.getSectionReference("diagnosticos1").addTableTextCell("diagnosticosTable", dxPrincipalStr, report.font);
				    	report.addSectionToDocument("diagnosticos1");

				    	/*
				    	 * Tipo de diagnóstico principal
				    	 */
				    	report.createSection("tipoDiagnostico", "diagnosticosTable", 1, 2, 2);

				    	w = new float [2];
				    	w[0] = 20f; 
				    	w[1] = 80f;
				    	report.setColumnsWidths("tipoDiagnostico", "diagnosticosTable",  w);

				    	report.font.setFontAttributes(0x000000, 11, true, false, false);
				    	report.getSectionReference("tipoDiagnostico").addTableTextCell("diagnosticosTable", "Tipo Dx Principal: ", report.font);
				    	report.font.setFontAttributes(0x000000, 11, false, false, false);
				    	report.getSectionReference("tipoDiagnostico").addTableTextCell("diagnosticosTable", datos.get("tipoDiagnosticoPrincipal")+"", report.font);
				    	report.addSectionToDocument("tipoDiagnostico");

				    }
		    						    

				    if (dxComplicacionStr.length() > 0 ){
				    	report.createSection("diagnosticos2", "diagnosticosTable", 1, 2, 2);

				    	w = new float [2];
				    	w[0] = 20f; 
				    	w[1] = 80f;
				    	report.setColumnsWidths("diagnosticos2", "diagnosticosTable",  w);

				    	report.font.setFontAttributes(0x000000, 11, true, false, false);
				    	report.getSectionReference("diagnosticos2").addTableTextCell("diagnosticosTable", "Dx Complicación: ", report.font);
				    	report.font.setFontAttributes(0x000000, 11, false, false, false);
				    	report.getSectionReference("diagnosticos2").addTableTextCell("diagnosticosTable", dxComplicacionStr, report.font);
				    	report.addSectionToDocument("diagnosticos2");
				    }

				    
				    
				    if (dxRelacionadosArray.size() > 0 ){
				    	report.createSection("diagnosticos3", "diagnosticosTable", dxRelacionadosArray.size(), 2, 2);
				    	
				    	w = new float [2];
				    	w[0] = 20f; 
				    	w[1] = 80f;
				    	report.setColumnsWidths("diagnosticos3", "diagnosticosTable",  w);
				    	
				    	report.getSectionReference("diagnosticos3").setTableCellsRowSpan(dxRelacionadosArray.size());
							report.font.setFontAttributes(0x000000, 11, true, false, false);
				    	report.getSectionReference("diagnosticos3").addTableTextCellAligned("diagnosticosTable", "Dx Relacionados: ", report.font, iTextBaseDocument.ALIGNMENT_MIDDLE, iTextBaseDocument.ALIGNMENT_TOP);
				    	report.font.setFontAttributes(0x000000, 11, false, false, false);
				    	report.getSectionReference("diagnosticos3").setTableCellsRowSpan(1);
				    	
				    	it = dxRelacionadosArray.iterator();
				    	while(it.hasNext()){
				    		diagnosticoStr = it.next() + ""; 
				    		report.getSectionReference("diagnosticos3").addTableTextCell("diagnosticosTable", diagnosticoStr, report.font);
				    	}
				    
				    	report.addSectionToDocument("diagnosticos3");
				    }
				  }
		    }
		    
		    
		    
		    
		    
		    sectionData = new String[4];
		    
		    //Informacion del Egreso
		    Map infoEgreso = (Map)datos.get("infoEgreso");
		    hayDatos = (infoEgreso.get("tieneEgreso") + "").equals("true");
		    boolean vivo=true;
		    
		    //Seccion Información del Egreso
		    if (hayDatos) {
		    	int index = 0;
		    	
		    	//Destino salida
		    	String value = (String)infoEgreso.get("destinoSalida");
		    	if (value != null && value.length() > 0) {
		    		sectionData[index] = "Destino salida: " + value;
		    	}
		    	else{
		    		sectionData[index] = "";
		    	}
		    	//Estado a la salida
		    	value = (String)infoEgreso.get("estadoSalida");
		    	if (value != null && value.length() > 0) {
		    		sectionData[index]  += "   " +(value.equalsIgnoreCase("true") ? "Estado a la salida: Muerto" : "Estado a la salida: Vivo");
		    		if (value.equalsIgnoreCase("true")) vivo=false;
		    		index++;
		    	}
		    	
		    	//Dx. de Muerte
		    	if (!vivo) {
		    		value = (String)infoEgreso.get("acronimoMuerte");
		    		String tipoCIE = (String)infoEgreso.get("tipoCIEMuerte");
		    		String diagMuerte = (String)infoEgreso.get("diagnosticoMuerte");
		    		if ((value != null && value.length()>0) && 
		    				(tipoCIE != null && tipoCIE.length()>0) && 
							(diagMuerte != null && diagMuerte.length()>0)) {
		    		    sectionData[index] = "Dx. de Muerte: " + value + "-" + tipoCIE + "-" + diagMuerte;
		    		    index++;
		    		}
		    	}
		    	
		    	//Rellenar de espacios sectionData (si se necesita)
		    	if (index < 4) {
		    		while (index < 4) {
		    			sectionData[index] = null;
		    			index++;
		    		}
		    	}
		    	
		    	report.createSection("infoEgreso", "infoEgresoTable", 3, 1, 10);
		    	report.addSectionTitle("infoEgreso", "infoEgresoTable", "Información del Egreso");
		    	report.addSectionData("infoEgreso", "infoEgresoTable", sectionData);
		    	report.addSectionToDocument("infoEgreso");
		    }
		    
		    
		    
		    //Medico Responsable
		    String nombreUsuario = (String)datos.get("nombreUsuario");
		    String numRegistroMedico = (String)datos.get("numRegistroMedico");
		    InfoDatosInt[] idi = (InfoDatosInt[])datos.get("idi");
		    hayDatos = (nombreUsuario != null && nombreUsuario.length() > 0);
		    
		    //Seccion Medico Responsable
		    if (hayDatos) {
		    	int size = 2, index = 0;
		    	if (numRegistroMedico != null && numRegistroMedico.length() > 0) size++;
		    	if (idi != null && idi.length > 0) size += idi.length;
		    	
		    	sectionData = new String[size];
		    	
		    	textData = nombreUsuario;
		    	
		    	//Numero Registro Medico
		    	if (numRegistroMedico != null && numRegistroMedico.length() > 0) {
		    		textData += " ";
		    		textData += numRegistroMedico;
		    	}
		    	
		       sectionData[0] = textData;
		        
		        //Especialidades 
		        if (idi != null && idi.length > 0) {
		        	while (index < idi.length) {
		        		sectionData[(index+2)] = idi[index].getNombre();
		        		index++;
		        	}
		        }
		        
		      report.createSection("medResponsable", "medResponsableTable", (size+1), 1, 7);
		    	report.addSectionTitle("medResponsable", "medResponsableTable", "Profesional Responsable");
		    	report.addSectionData("medResponsable", "medResponsableTable", sectionData);
		    	report.addSectionToDocument("medResponsable");
		    }

		    //Cerrar el reporte
		    report.closeReport();
    		return;
	}

}
