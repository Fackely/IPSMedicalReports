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

public class GeneradorSubReporteValoracionesRevisionesPorSistema {
	
	/**
	 * Mensajes parametrizados de los reportes 
	 */ 
	private static MessageResources messageResource = MessageResources.getMessageResources("com.servinte.mensajes.historiaclinica.impresionHistoriaClinica");
	
	public GeneradorSubReporteValoracionesRevisionesPorSistema() {
	}

	public JasperReportBuilder generarReporteSeccionRevisionesSistema(){
		JasperReportBuilder revisionesSistema = report();
		ComponentBuilder<?, ?> componentBuilder = null;
		HorizontalListBuilder revisionesSistemaTituloSeccion = null;
		
		List<HorizontalListBuilder> listaHorizonatBuilder= new ArrayList<HorizontalListBuilder>();
		ComponentBuilder[] listaComponentesAPintar;
		GeneradorDisenoSubReportes generadorDisenoSubReportes = new GeneradorDisenoSubReportes();
		
		
		
		revisionesSistemaTituloSeccion=	cmp.horizontalList(cmp.verticalList(cmp.horizontalList(
				cmp.text(messageResource.getMessage("historia_clinica_lable_valoracion_revisionesSistema")).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloTitulo).setBorder(stl.pen1Point().setLineColor(Color.BLACK)).setPadding(2).setHorizontalAlignment(HorizontalAlignment.LEFT))
		),
		cmp.text("Apariencia General: EN BUENAS CONDICIONES GENERALES").setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloSubTituloSinBold).setHorizontalAlignment(HorizontalAlignment.LEFT).setBorder(stl.pen1Point().setLineColor(Color.BLACK)).setPadding(2)),
		cmp.text("Esfera Mental: ORIENTADA").setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloSubTituloSinBold).setHorizontalAlignment(HorizontalAlignment.LEFT).setBorder(stl.pen1Point().setLineColor(Color.BLACK)).setPadding(2)),
		cmp.text("Cabeza: NORMAL").setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloSubTituloSinBold).setHorizontalAlignment(HorizontalAlignment.LEFT).setBorder(stl.pen1Point().setLineColor(Color.BLACK)).setPadding(2)),
		cmp.text("Ojos: NORMAL").setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloSubTituloSinBold).setHorizontalAlignment(HorizontalAlignment.LEFT).setBorder(stl.pen1Point().setLineColor(Color.BLACK)).setPadding(2)),
		cmp.text("Otorrino: NORMAL").setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloSubTituloSinBold).setHorizontalAlignment(HorizontalAlignment.LEFT).setBorder(stl.pen1Point().setLineColor(Color.BLACK)).setPadding(2)),
		cmp.text("Cuello: NORMAL").setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloSubTituloSinBold).setHorizontalAlignment(HorizontalAlignment.LEFT).setBorder(stl.pen1Point().setLineColor(Color.BLACK)).setPadding(2)),
		cmp.text("Tórax Mamas: NORMAL").setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloSubTituloSinBold).setHorizontalAlignment(HorizontalAlignment.LEFT).setBorder(stl.pen1Point().setLineColor(Color.BLACK)).setPadding(2)),
		cmp.text("Corazón: NORMAL").setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloSubTituloSinBold).setHorizontalAlignment(HorizontalAlignment.LEFT).setBorder(stl.pen1Point().setLineColor(Color.BLACK)).setPadding(2)),
		cmp.text("Pulmones: NORMAL").setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloSubTituloSinBold).setHorizontalAlignment(HorizontalAlignment.LEFT).setBorder(stl.pen1Point().setLineColor(Color.BLACK)).setPadding(2)),
		cmp.text("Abdomen: UTERO GRAVIDO. AU 34, FUVC DORSO DERECHO, FCF 145, SIN ACTIVIDAD UTERINA. MOVIMIENTOS FETALES PRESENTES.").setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloSubTituloSinBold).setHorizontalAlignment(HorizontalAlignment.LEFT).setBorder(stl.pen1Point().setLineColor(Color.BLACK)).setPadding(2)),
		cmp.text("Genitourinario: TV CUELLO POSTERIOR, BLANDO, LARGO, PERMEABLE A 1 DEDO. NO GENITORRAGIA, NO AMNIORREA.").setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloSubTituloSinBold).setHorizontalAlignment(HorizontalAlignment.LEFT).setBorder(stl.pen1Point().setLineColor(Color.BLACK)).setPadding(2)),
		cmp.text("Osteomuscular: EDEMA EN MIEMBROS INFERIORES").setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloSubTituloSinBold).setHorizontalAlignment(HorizontalAlignment.LEFT).setBorder(stl.pen1Point().setLineColor(Color.BLACK)).setPadding(2)),
		cmp.text("SNC: NORMAL").setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloSubTituloSinBold).setHorizontalAlignment(HorizontalAlignment.LEFT).setBorder(stl.pen1Point().setLineColor(Color.BLACK)).setPadding(2)),
		cmp.text("Piel y Faneras: NORMAL").setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloSubTituloSinBold).setHorizontalAlignment(HorizontalAlignment.LEFT).setBorder(stl.pen1Point().setLineColor(Color.BLACK)).setPadding(2))
		))	;
		
		
		
		
		
		listaHorizonatBuilder.add(revisionesSistemaTituloSeccion);
		
		
		
		
		
		
		listaComponentesAPintar = new ComponentBuilder[listaHorizonatBuilder.size()];
		for (int i = 0; i < listaComponentesAPintar.length; i++) {
			listaComponentesAPintar[i]=listaHorizonatBuilder.get(i);
		}
		
		
		componentBuilder = cmp.verticalList(listaComponentesAPintar);
		revisionesSistema.summary(componentBuilder)
		.setTemplate(generadorDisenoSubReportes.crearPlantillaReporte())
		.build();
		
		return revisionesSistema;
		
	}
}
