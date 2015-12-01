package com.servinte.axioma.generadorReporte.historiaClinica;

import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;
import static net.sf.dynamicreports.report.builder.DynamicReports.report;
import static net.sf.dynamicreports.report.builder.DynamicReports.stl;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.builder.component.HorizontalListBuilder;
import net.sf.dynamicreports.report.constant.HorizontalAlignment;

import org.apache.struts.util.MessageResources;

import util.reportes.dinamico.EstilosReportesDinamicosHistoriaClinica;

public class GeneradorSubReporteValoracionesObservaciones {
	
	/**
	 * Mensajes parametrizados de los reportes 
	 */ 
	private static MessageResources messageResource = MessageResources.getMessageResources("com.servinte.mensajes.historiaclinica.impresionHistoriaClinica");
	
	
	public GeneradorSubReporteValoracionesObservaciones() {
	}
	
	public JasperReportBuilder generarReporteSeccionObservaciones(){
		JasperReportBuilder diagnosticos = report();
		ComponentBuilder<?, ?> componentBuilder = null;
		
		List<HorizontalListBuilder> listaHorizonatBuilder= new ArrayList<HorizontalListBuilder>();
		ComponentBuilder[] listaComponentesAPintar;
		GeneradorDisenoSubReportes generadorDiseñoSubReportes = new GeneradorDisenoSubReportes();
		HorizontalListBuilder diagnosticosTituloSeccion = null;
		
		
		diagnosticosTituloSeccion=	cmp.horizontalList(cmp.verticalList(cmp.horizontalList(
				cmp.text(messageResource.getMessage("historia_clinica_lable_valoracion_observacionesTitulo")).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloTitulo).setBorder(stl.pen1Point().setLineColor(Color.BLACK)).setPadding(2).setHorizontalAlignment(HorizontalAlignment.LEFT))
	     ),
	     cmp.verticalList(
	    		 cmp.text("Plan Diagnóstico y terapéutico:"),
	    		 cmp.text("DX: 1.G1P0 2. EMB 35.5 SS X FUM 3.FUVC 4.HIE A CLASIFICAR 5.APP PLAN:  DELAR EN OBSERVACION AFINAMIENTO TA CADA 15 MINUTOS " +
	    		 		"POR 1 HORA. SE SOLICITA PERFIL TOXEMICO, PCR, PO + GRAM X SONDA Y FFV.  NIFEDIPINO 2 CAP X 10 MG VO AHORA" +
	    		 		" BROMURO DE HIOSCINA + DIPIRONA 1 AMP IV LENTO Y DILUIDO SE SOLICITA PERFIL TOXEMICO, PCR, PO + GRAM X SONDA Y" +
	    		 		" FFV.  SE TOMA MONITORIA FETAL, REACTIVA, ACTIVIDAD UTERINA IRREGULAR.  17/12/2010 - 15:51 ERIKA VIVIANA QUINTERO RIOS - R.M. 07271/02 CC. 30393585 MEDICINA GENERAL")),
	    		 		cmp.text("Comentarios Generales:"),
	    		 		cmp.text("PNS: REACTIVA 17/12/2010 - 15:51 ERIKA VIVIANA QUINTERO RIOS -  R.M. 07271/02 CC. 30393585 MEDICINA GENERAL")
		))	;
		listaHorizonatBuilder.add(diagnosticosTituloSeccion);
		
		
		
		
		
		
		listaComponentesAPintar = new ComponentBuilder[listaHorizonatBuilder.size()];
		for (int i = 0; i < listaComponentesAPintar.length; i++) {
			listaComponentesAPintar[i]=listaHorizonatBuilder.get(i);
		}
		
		
		componentBuilder = cmp.verticalList(listaComponentesAPintar);
		diagnosticos.summary(componentBuilder)
		.setTemplate(generadorDiseñoSubReportes.crearPlantillaReporte())
		.build();
		return diagnosticos;
	}
	

}
