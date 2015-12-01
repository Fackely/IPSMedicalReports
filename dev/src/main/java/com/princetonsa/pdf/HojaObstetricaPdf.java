/*
 * Creado el 09-jul-2005
 * por Julian Montoya
 */
package com.princetonsa.pdf;

import java.sql.Connection;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;


import java.util.HashMap;
import org.apache.log4j.Logger;

import util.UtilidadFecha;
import util.pdf.PdfReports;
import util.pdf.iTextBaseDocument;
import util.pdf.iTextBaseFont;

import com.princetonsa.actionform.hojaObstetrica.HojaObstetricaForm;
import com.princetonsa.mundo.hojaObstetrica.HojaObstetrica;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;

/**
 * @author Julian Montoya
 * @author amruiz
 * 
 * Princeton S.A. (ParqueSoft Manizales)
 */
public class HojaObstetricaPdf {

	  
	  /**
     * Metodo para manejar los logs de esta clase
     */
  // private static Logger categoriaPdfLogger=Logger.getLogger(CategoriaPdf.class);
	
	private static Logger logger = Logger.getLogger(HojaObstetricaPdf.class);
	
   
   
	/**
	 * @param filename nombre del archivo pdf a generar.
	 * @param paciente
	 * @param datos mapa de datos cuyas llaves son las siguientes:
	 */
	public static void pdfHojaObstetrica(Connection con, String filename, HojaObstetricaForm hojaObstetricaForm,UsuarioBasico medico, PersonaBasica paciente)
	{	
       //variable para manejar el reporte
        PdfReports report = new PdfReports();
        
        //Instancio el mundo de hoja obstétrica
        HojaObstetrica mundoHojaObstetrica = new HojaObstetrica();
        
        //titulo del reporte
        String tituloReporte="HOJA OBSTETRICA  (" + UtilidadFecha.getFechaActual() + " - " + UtilidadFecha.getHoraActual() +")";

        InstitucionBasica institucionBasica= new InstitucionBasica();
        institucionBasica.cargarXConvenio(medico.getCodigoInstitucionInt(), paciente.getCodigoConvenio());
        
        //generacion de la cabecera, validando el tipo de separador e utilizado en el path
        report.setReportBaseHeader1(institucionBasica, institucionBasica.getNit(), tituloReporte);
        
        //----Abrir reporte
        report.openReport(filename);
		report.font.useFont(iTextBaseFont.FONT_COURIER, true, true, true);
		 
		//----Informacion del Paciente
        report.font.setFontSizeAndAttributes(10,true,false,false);
        report.document.addParagraph("INFORMACIÓN PERSONAL ", report.font,iTextBaseDocument.ALIGNMENT_LEFT,25);
        report.font.setFontSizeAndAttributes(9,false,false,false);
        report.document.addParagraph("Identificación : "+ paciente.getCodigoTipoIdentificacionPersona() + " "+  paciente.getNumeroIdentificacionPersona() ,report.font,iTextBaseDocument.ALIGNMENT_LEFT,15);
        report.document.addParagraph("Apellidos y Nombres : " + paciente.getApellidosNombresPersona(false) ,report.font,iTextBaseDocument.ALIGNMENT_LEFT,15);
        report.document.addParagraph("Edad : " + paciente.getEdadDetallada()+ "      Sexo: "+paciente.getSexo() ,report.font,iTextBaseDocument.ALIGNMENT_LEFT,15);
                    
        //-----Crear la primera seccion de informacion de embarazo
        report.font.setFontSizeAndAttributes(10,true,false,false);
        report.createSection("seccionInformacionEmbarazo","tablaConsulta",4,2,0);
        report.getSectionReference("seccionInformacionEmbarazo").setTableBorder("tablaConsulta", 0xFFFFFF, 0.0f);
        report.getSectionReference("seccionInformacionEmbarazo").setTableCellBorderWidth("tablaConsulta", 0.5f);
        report.getSectionReference("seccionInformacionEmbarazo").setTableCellsDefaultColors("tablaConsulta", 0xFFFFFF, 0xFFFFFF);
        report.getSectionReference("seccionInformacionEmbarazo").setTableCellsColSpan(2);
        report.getSectionReference("seccionInformacionEmbarazo").addTableTextCellAligned("tablaConsulta", "INFORMACIÓN DEL EMBARAZO Nro  "+hojaObstetricaForm.getNumeroEmbarazo(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
        report.font.setFontSizeAndAttributes(9,false,false,false);
        report.getSectionReference("seccionInformacionEmbarazo").setTableCellsColSpan(1);
        
        report.getSectionReference("seccionInformacionEmbarazo").addTableTextCellAligned("tablaConsulta", "Fecha Orden : "+hojaObstetricaForm.getFechaOrden(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
        report.getSectionReference("seccionInformacionEmbarazo").addTableTextCellAligned("tablaConsulta", "FUR : "+hojaObstetricaForm.getFur(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
        
        report.getSectionReference("seccionInformacionEmbarazo").setTableCellsColSpan(1);
        
        report.getSectionReference("seccionInformacionEmbarazo").addTableTextCellAligned("tablaConsulta", "FPP : "+hojaObstetricaForm.getFpp(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
        report.getSectionReference("seccionInformacionEmbarazo").addTableTextCellAligned("tablaConsulta", "Edad Gestacional : "+hojaObstetricaForm.getEdadGestacional()+" semanas", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
        //report.getSectionReference("seccionInformacionEmbarazo").addTableTextCellAligned("tablaConsulta", "Fecha Ultrasonido : "+hojaObstetricaForm.getFechaUltrasonido(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
        
        report.getSectionReference("seccionInformacionEmbarazo").setTableCellsColSpan(1);
        
        //report.getSectionReference("seccionInformacionEmbarazo").addTableTextCellAligned("tablaConsulta", "FPP por Ultrasonido : "+hojaObstetricaForm.getFppUltrasonido(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);     
        
        report.getSectionReference("seccionInformacionEmbarazo").addTableTextCellAligned("tablaConsulta", "Edad al Parto : "+hojaObstetricaForm.getEdadParto()+ " años", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
                      
        String finEmbarazo="";
        
        if (hojaObstetricaForm.isFinalizacionEmbarazo())
        	finEmbarazo="Embarazo Finalizado";
        else
        	finEmbarazo="Embarazo Sin Finalizar";
        	
        report.getSectionReference("seccionInformacionEmbarazo").addTableTextCellAligned("tablaConsulta", finEmbarazo, report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
        
        //-Adionar SEccion informacion embarazo 
        report.addSectionToDocument("seccionInformacionEmbarazo");
        
        //-------------------------------------------------------SECCION DE RESUMEN GESTACIONAL---------------------------------------//
              
       Collection ColResumenGest=mundoHojaObstetrica.consultarResumenGestacional(con, paciente.getCodigoPersona(), hojaObstetricaForm.getNumeroEmbarazo(), true);
       Collection ColTipoResumenGest=mundoHojaObstetrica.consultarTiposResumenGestacional(con, medico.getCodigoInstitucionInt());
             
        //Se calcula el número de registros que hay en el resumen gestacional
       int registros=ColResumenGest.size();
       
       if(registros > 0)
       {
	       Iterator iterador5=ColResumenGest.iterator();
	       
	       /*Se guarda en cada uno de los vectores el codigo del historico resumen, la edad gestacional y la fecha y hora*/
	       String codigoTemp="";
	       String edadTemp="";
	       String fechasHorasTemp="";
	       Vector codigosResumen=new Vector();
	       Vector edadesGest=new Vector();
	       Vector fechasHoras=new Vector();
	       Vector numReg=new Vector();
	       int num=0;
	       for (int c=0; c<registros; c++)
	       {
	    	   HashMap fila5=(HashMap)iterador5.next();
		       	if(c==0)
		       	{
		       		codigoTemp=fila5.get("codigo_resumen")+"";
		       		edadTemp=fila5.get("edad_gestacional")+"";
		       		fechasHorasTemp=fila5.get("fecha")+"  "+fila5.get("hora");
		       	}
		       			       	
		      if(codigoTemp.equals(fila5.get("codigo_resumen")+""))
		       	{
		       		num++;
		       	}
		       	if(!codigoTemp.equals(fila5.get("codigo_resumen")+"") || c==registros-1)
		       	{
		       		numReg.add(num+"");
		       		codigosResumen.add(codigoTemp);
		       		edadesGest.add(edadTemp);
		       		fechasHoras.add(fechasHorasTemp);
		       		codigoTemp=fila5.get("codigo_resumen")+"";
		       		edadTemp=fila5.get("edad_gestacional")+"";
		       		fechasHorasTemp=fila5.get("fecha")+"  "+fila5.get("hora");
		       		
		       		num=1;
		       	}
	       }
	       
	      for(int cont=0;cont<codigosResumen.size();cont++)
	       {
	      	   int numFilas=Integer.parseInt(numReg.elementAt(cont)+"");
	      	   numFilas+=3;
		       //-Crear nueva seccion para el resumen gestacional
		        report.font.setFontSizeAndAttributes(10,true,false,false);
		        report.createSection("seccionResumenGest_"+cont,"tablaConsulta1_"+cont,numFilas,2,0);
		        report.getSectionReference("seccionResumenGest_"+cont).setTableBorder("tablaConsulta1_"+cont, 0xFFFFFF, 0.0f);
		        report.getSectionReference("seccionResumenGest_"+cont).setTableCellBorderWidth("tablaConsulta1_"+cont, 0.5f);
		        report.getSectionReference("seccionResumenGest_"+cont).setTableCellsDefaultColors("tablaConsulta1_"+cont, 0xFFFFFF, 0xFFFFFF);
		        report.getSectionReference("seccionResumenGest_"+cont).setTableCellsColSpan(2);
		        int temp=cont+1;
		        report.getSectionReference("seccionResumenGest_"+cont).addTableTextCellAligned("tablaConsulta1_"+cont, "RESUMEN GESTACIONAL Nro "+temp, report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
		        
		        report.getSectionReference("seccionResumenGest_"+cont).setTableCellsColSpan(1);
		        report.font.setFontSizeAndAttributes(9,false,false,false);
		        report.getSectionReference("seccionResumenGest_"+cont).addTableTextCellAligned("tablaConsulta1_"+cont, "Fecha/Hora: "+fechasHoras.elementAt(cont), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
		        report.getSectionReference("seccionResumenGest_"+cont).addTableTextCellAligned("tablaConsulta1_"+cont, "Edad Gestacional: "+edadesGest.elementAt(cont)+" semanas", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
		        report.font.setFontSizeAndAttributes(9,true,false,false);
		        report.getSectionReference("seccionResumenGest_"+cont).addTableTextCellAligned("tablaConsulta1_"+cont, "Tipo Resumen ", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
		        report.getSectionReference("seccionResumenGest_"+cont).addTableTextCellAligned("tablaConsulta1_"+cont, "Valor ", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
		        
		        report.font.setFontSizeAndAttributes(9,false,false,false);
		        //report.getSectionReference("seccionResumenGest").setTableCellsColSpan(1);
		        
		        String valorTipoResumen="";
		        int tipoResumen=0;
		        Iterator iterador=ColResumenGest.iterator();
		        String nombreTipoResumen="";
		        
		        for (int i=0; i<registros; i++)
		        {
		        	HashMap fila=(HashMap)iterador.next();
		        	tipoResumen= Integer.parseInt(fila.get("tipo_resumen_gest")+"");
		        	Iterator iterador2=ColTipoResumenGest.iterator();
		        	if(codigosResumen.elementAt(cont).equals(fila.get("codigo_resumen")+""))
		        	{
		        	  	for (int j=0; j<ColTipoResumenGest.size(); j++)
			        	{
		        	  		HashMap fila2=(HashMap)iterador2.next();
			              if (tipoResumen==Integer.parseInt(fila2.get("codigo")+""))
			        		{
			        			nombreTipoResumen=fila2.get("nombre")+"";
			        			break;
			        		}       		
			        	}
			        	valorTipoResumen= fila.get("valor")+"";
			            report.getSectionReference("seccionResumenGest_"+cont).addTableTextCellAligned("tablaConsulta1_"+cont, nombreTipoResumen, report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
				        report.getSectionReference("seccionResumenGest_"+cont).addTableTextCellAligned("tablaConsulta1_"+cont, valorTipoResumen, report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
				  	}//if	
		        }//for
		        
		        //-Adicionar sección de resumen gestacional
		        report.addSectionToDocument("seccionResumenGest_"+cont);
	       }//for
	        
       }//if
        
       //-------------------------------------------SECCION DE EXAMENES DE LABORATORIO----------------------------------------------------------//
        
        Collection ColExamenesLab=mundoHojaObstetrica.consultarExamenesLaboratorio(con, paciente.getCodigoPersona(), hojaObstetricaForm.getNumeroEmbarazo(), true, 1);
        Collection ColTipoExamenLab=mundoHojaObstetrica.consultarTiposExamenLaboratorio(con, medico.getCodigoInstitucionInt());
        
         //Se calcula el número de registros que hay en los exámenes de laboratorio
        int registros2=ColExamenesLab.size();
        
        if(registros2 > 0)
        {
        	Iterator iterador6=ColExamenesLab.iterator();
        	
        	 /*Se guarda en cada uno de los vectores el codigo del historico exámen laboratorioa, la edad gestacional y la fecha y hora*/
 	       String codigoTempExam="";
 	       String edadTempExam="";
 	       String fechasHorasTempExam="";
 	       Vector codigosExam=new Vector();
 	       Vector edadesGestExam=new Vector();
 	       Vector fechasHorasExam=new Vector();
 	       Vector numRegExam=new Vector();
 	       int num2=0;
 	       for (int c=0; c<registros2; c++)
 	       {
 	    	  HashMap fila6=(HashMap)iterador6.next();
 		       	if(c==0)
 		       	{
 		       		codigoTempExam=fila6.get("codigo_histo_examen")+"";
 		       		edadTempExam=fila6.get("edad_gestacional")+"";
 		       		fechasHorasTempExam=fila6.get("fecha")+"  "+fila6.get("hora");
 		       	}
 		       			       	
 		      if(codigoTempExam.equals(fila6.get("codigo_histo_examen")+""))
 		       	{
 		       		num2++;
 		       	}
 		       	if(!codigoTempExam.equals(fila6.get("codigo_histo_examen")+"") || c==registros2-1)
 		       	{
 		       		numRegExam.add(num2+"");
 		       		codigosExam.add(codigoTempExam);
 		       		edadesGestExam.add(edadTempExam);
 		       		fechasHorasExam.add(fechasHorasTempExam);
 		       		codigoTempExam=fila6.get("codigo_histo_examen")+"";
 		       		edadTempExam=fila6.get("edad_gestacional")+"";
 		       		fechasHorasTempExam=fila6.get("fecha")+"  "+fila6.get("hora");
 		       		
 		       		num2=1;
 		       	}
 	       }
 	       
 	       for(int cont=0;cont<codigosExam.size();cont++)
 	       {
		 	      int numFilasExam=Integer.parseInt(numRegExam.elementAt(cont)+"");
		 	      numFilasExam+=3;
		 	               	         
		        //-Crear nueva seccion para los exámenes de laboratorio
		         report.font.setFontSizeAndAttributes(10,true,false,false);
		         report.createSection("seccionExamenesLab_"+cont,"tablaConsulta2_"+cont,numFilasExam,3,0);
		         report.getSectionReference("seccionExamenesLab_"+cont).setTableBorder("tablaConsulta2_"+cont, 0xFFFFFF, 0.0f);
		         report.getSectionReference("seccionExamenesLab_"+cont).setTableCellBorderWidth("tablaConsulta2_"+cont, 0.5f);
		         report.getSectionReference("seccionExamenesLab_"+cont).setTableCellsDefaultColors("tablaConsulta2_"+cont, 0xFFFFFF, 0xFFFFFF);
		         report.getSectionReference("seccionExamenesLab_"+cont).setTableCellsColSpan(3);
		         int numExam=cont+1;
		         report.getSectionReference("seccionExamenesLab_"+cont).addTableTextCellAligned("tablaConsulta2_"+cont, "EXÁMENES DE LABORATORIO Nro "+numExam, report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
		         
		         report.getSectionReference("seccionExamenesLab_"+cont).setTableCellsColSpan(1);
			     report.font.setFontSizeAndAttributes(9,false,false,false);
			     report.getSectionReference("seccionExamenesLab_"+cont).addTableTextCellAligned("tablaConsulta2_"+cont, "Fecha/Hora: "+fechasHorasExam.elementAt(cont), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
			     report.getSectionReference("seccionExamenesLab_"+cont).addTableTextCellAligned("tablaConsulta2_"+cont, "", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
			     report.getSectionReference("seccionExamenesLab_"+cont).addTableTextCellAligned("tablaConsulta2_"+cont, "Edad Gestacional: "+edadesGestExam.elementAt(cont)+" semanas", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);

			     report.getSectionReference("seccionExamenesLab_"+cont).setTableCellsColSpan(3);
		         	         
		         report.font.setFontSizeAndAttributes(9,false,false,false);
		         		         
		         String resultadoTipoExamen="";
		         String observacionesTipoExamen="";
		         int tipoExamen=0;
		         Iterator iterador3=ColExamenesLab.iterator();
		         String nombreTipoExamen="";
		         
		         for (int i=0; i<registros2; i++)
		         {
		        	 HashMap fila3=(HashMap)iterador3.next();
		         	tipoExamen= Integer.parseInt(fila3.get("tipo_examen_lab")+"");
		         	Iterator iterador4=ColTipoExamenLab.iterator();
		         	if(codigosExam.elementAt(cont).equals(fila3.get("codigo_histo_examen")+""))
		         	{
			         	for (int z=0; z<ColTipoExamenLab.size(); z++)
			         	{
			         		HashMap fila4=(HashMap)iterador4.next();
			         		if(tipoExamen==-1)
			         		{
			         			nombreTipoExamen=fila3.get("descripcion_otro")+"";
			         			break;
			         		}
			         		
			               if (tipoExamen==Integer.parseInt(fila4.get("codigo")+""))
			         		{
			               		nombreTipoExamen=fila4.get("nombre")+"";
			         			break;
			         		}  			               
			         	}
			         	
			         	resultadoTipoExamen= fila3.get("resultado")+"";
			         	observacionesTipoExamen=fila3.get("observaciones")+"";
			         	
			         	report.getSectionReference("seccionExamenesLab_"+cont).addTableTextCellAligned("tablaConsulta2_"+cont,"- Exámen: "+ nombreTipoExamen, report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
			         	report.getSectionReference("seccionExamenesLab_"+cont).addTableTextCellAligned("tablaConsulta2_"+cont,"- Resultado: "+ resultadoTipoExamen, report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
			         	report.getSectionReference("seccionExamenesLab_"+cont).addTableTextCellAligned("tablaConsulta2_"+cont, "- Observaciones: "+observacionesTipoExamen, report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
		         	}	//if
		         }//for
		         
		         //-Adicionar sección de exámenes de laboratorio
		         report.addSectionToDocument("seccionExamenesLab_"+cont);
 	       }//for de codigosExam
        }//if
         
        //-------------------------------------------SECCION DE ULTRASONIDO----------------------------------------------------------//
        
        Collection ColHistoUltrasonidos=mundoHojaObstetrica.consultarHistoricoUltrasonidos(con, paciente.getCodigoPersona(), hojaObstetricaForm.getNumeroEmbarazo(), true);
        Collection ColTipoUltrasonidos=mundoHojaObstetrica.consultarTiposUltrasonido(con, medico.getCodigoInstitucionInt());
        
         //Se calcula el número de registros que hay en los ultrasonidos
        int registros3=ColHistoUltrasonidos.size();
        
        
        if(registros3 > 0)
        {
        	Iterator iterador7=ColHistoUltrasonidos.iterator();
        	
        	 /*Se guarda en cada uno de los vectores el codigo del historico ultrasonidos,fecha y hora*/
 	       String codigoTempUltra="";
 	       String fechasHorasTempUltra="";
 	       Vector codigosUltra=new Vector();
 	       Vector fechasHorasUltra=new Vector();
 	       Vector numRegUltra=new Vector();
 	       int num3=0;
 	       for (int c=0; c<registros3; c++)
 	       {
 	    	  HashMap fila7=(HashMap)iterador7.next();
 		       	if(c==0)
 		       	{
 		       		codigoTempUltra=fila7.get("codigo_histo_ultrasonido")+"";
 		       		fechasHorasTempUltra=fila7.get("fecha")+"  "+fila7.get("hora");
 		       	}
 		       			       	
 		      if(codigoTempUltra.equals(fila7.get("codigo_histo_ultrasonido")+""))
 		       	{
 		       		num3++;
 		       	}
 		       	if(!codigoTempUltra.equals(fila7.get("codigo_histo_ultrasonido")+"") || c==registros3-1)
 		       	{
 		       		numRegUltra.add(num3+"");
 		       		codigosUltra.add(codigoTempUltra);
 		       		fechasHorasUltra.add(fechasHorasTempUltra);
 		       		codigoTempUltra=fila7.get("codigo_histo_ultrasonido")+"";
 		       		fechasHorasTempUltra=fila7.get("fecha")+"  "+fila7.get("hora");
 		       		
 		       		num3=1;
 		       	}
 	       }
		   
 	       
 	      for(int cont=0;cont<codigosUltra.size();cont++)
	       {
		 	      int numFilasUltra=Integer.parseInt(numRegUltra.elementAt(cont)+"");
		 	      numFilasUltra+=2;
		 	               	         
		        //-Crear nueva seccion para los ultrasonidos
		         report.font.setFontSizeAndAttributes(10,true,false,false);
		         report.createSection("seccionUltrasonido_"+cont,"tablaConsulta3_"+cont,numFilasUltra,2,0);
		         report.getSectionReference("seccionUltrasonido_"+cont).setTableBorder("tablaConsulta3_"+cont, 0xFFFFFF, 0.0f);
		         report.getSectionReference("seccionUltrasonido_"+cont).setTableCellBorderWidth("tablaConsulta3_"+cont, 0.5f);
		         report.getSectionReference("seccionUltrasonido_"+cont).setTableCellsDefaultColors("tablaConsulta3_"+cont, 0xFFFFFF, 0xFFFFFF);
		         report.getSectionReference("seccionUltrasonido_"+cont).setTableCellsColSpan(2);
		         int numUltra=cont+1;
		         report.getSectionReference("seccionUltrasonido_"+cont).addTableTextCellAligned("tablaConsulta3_"+cont, "ULTRASONIDO Nro "+numUltra, report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
		         
		         report.font.setFontSizeAndAttributes(9,false,false,false);
			     report.getSectionReference("seccionUltrasonido_"+cont).addTableTextCellAligned("tablaConsulta3_"+cont, "Fecha/Hora: "+fechasHorasUltra.elementAt(cont), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
			     			     		         
			     report.getSectionReference("seccionUltrasonido_"+cont).setTableCellsColSpan(1);
			     report.font.setFontSizeAndAttributes(9,true,false,false);
		         report.getSectionReference("seccionUltrasonido_"+cont).addTableTextCellAligned("tablaConsulta3_"+cont, "Tipo Ultrasonido", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
		         report.getSectionReference("seccionUltrasonido_"+cont).addTableTextCellAligned("tablaConsulta3_"+cont, "Valor", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
		         		         
		         report.font.setFontSizeAndAttributes(9,false,false,false);
		         		         
		         String valorTipoUltrasonido="";
		         int tipoUltrasonido=0;
		         Iterator iterador8=ColHistoUltrasonidos.iterator();
		         String nombreTipoUltrasonido="";
		         
		         for (int i=0; i<registros3; i++)
		         {
		        	 HashMap fila8=(HashMap)iterador8.next();
		         	tipoUltrasonido= Integer.parseInt(fila8.get("tipo_ultrasonido")+"");
		         	Iterator iterador9=ColTipoUltrasonidos.iterator();
		         	if(codigosUltra.elementAt(cont).equals(fila8.get("codigo_histo_ultrasonido")+""))
		         	{
			         	for (int z=0; z<ColTipoUltrasonidos.size(); z++)
			         	{
			         		HashMap fila9=(HashMap)iterador9.next();
			               if (tipoUltrasonido==Integer.parseInt(fila9.get("codigo")+""))
			         		{
			               		nombreTipoUltrasonido=fila9.get("nombre")+"";
			         			break;
			         		}       		
			         	}
			         	
			         	valorTipoUltrasonido= fila8.get("valor")+"";
			         				         	
			         	report.getSectionReference("seccionUltrasonido_"+cont).addTableTextCellAligned("tablaConsulta3_"+cont, nombreTipoUltrasonido, report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
			         	report.getSectionReference("seccionUltrasonido_"+cont).addTableTextCellAligned("tablaConsulta3_"+cont, valorTipoUltrasonido, report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
			         	}	//if
		         }//for
		         
		         //-Adicionar sección de exámenes de laboratorio
		         report.addSectionToDocument("seccionUltrasonido_"+cont);
	       }//for de codigosExam
       }//if
        
        
         //-------------------------------------------SECCION DE OBSERVACIONES GENERALES----------------------------------------------------------//
	        	                
	        //-Crear nueva seccion para las observaciones generales
	         report.font.setFontSizeAndAttributes(10,true,false,false);
	         report.createSection("seccionObservacionesGrales","tablaConsulta5_",1,1,0);
	         report.getSectionReference("seccionObservacionesGrales").setTableBorder("tablaConsulta5_", 0xFFFFFF, 0.0f);
	         report.getSectionReference("seccionObservacionesGrales").setTableCellBorderWidth("tablaConsulta5_", 0.5f);
	         report.getSectionReference("seccionObservacionesGrales").setTableCellsDefaultColors("tablaConsulta5_", 0xFFFFFF, 0xFFFFFF);
	         
	         report.getSectionReference("seccionObservacionesGrales").addTableTextCellAligned("tablaConsulta5_", "Observaciones Generales ", report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
	         
	         report.font.setFontSizeAndAttributes(9,false,false,false);
	         report.getSectionReference("seccionObservacionesGrales").addTableTextCellAligned("tablaConsulta5_", hojaObstetricaForm.getObservacionesGrales(), report.font,iTextBaseDocument.ALIGNMENT_LEFT,iTextBaseDocument.ALIGNMENT_LEFT);
	         
	         //-Adicionar sección de observaciones generales
	         report.addSectionToDocument("seccionObservacionesGrales");
	         
	      
		//-Cerrando el reporte
		report.closeReport(); 
   }
	
	
}
//-----------------------------------------------------------------------------------------------------------------------------------------------
//-----------------------------------------------------------------------------------------------------------------------------------------------
