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

public class GeneradorSubReporteValoracionesDiagnosticos {
	
	/**
	 * Mensajes parametrizados de los reportes 
	 */ 
	private static MessageResources messageResource = MessageResources.getMessageResources("com.servinte.mensajes.historiaclinica.impresionHistoriaClinica");
	
	public GeneradorSubReporteValoracionesDiagnosticos() {
	}
	
	
	public JasperReportBuilder generarReporteSeccionDiagnosticos(){
		JasperReportBuilder diagnosticos = report();
		ComponentBuilder<?, ?> componentBuilder = null;
		
		List<HorizontalListBuilder> listaHorizonatBuilder= new ArrayList<HorizontalListBuilder>();
		ComponentBuilder[] listaComponentesAPintar;
		GeneradorDisenoSubReportes generadorDiseñoSubReportes = new GeneradorDisenoSubReportes();
		HorizontalListBuilder diagnosticosTituloSeccion = null;
		
		
		diagnosticosTituloSeccion=	cmp.horizontalList(cmp.verticalList(cmp.horizontalList(
				cmp.text(messageResource.getMessage("historia_clinica_lable_valoracion_diagnosticosTitulo")).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloTitulo).setBorder(stl.pen1Point().setLineColor(Color.BLACK)).setPadding(2).setHorizontalAlignment(HorizontalAlignment.LEFT))
		),cmp.verticalList(cmp.text("Dx. Principal: Z321-10 EMBARAZO CONFIRMADO").setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloSubTitulo).setHorizontalAlignment(HorizontalAlignment.LEFT)),
				cmp.text("Tipo de Dx Principal:  Confirmado Repetido ").setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloSubTitulo
				).setHorizontalAlignment(HorizontalAlignment.LEFT))).setStyle(stl.style().setBorder(stl.pen1Point().setLineColor(Color.BLACK)).setPadding(2)),
				cmp.text("Causa Externa:  Enfermedad General").setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloSubTitulo).setBorder(stl.pen1Point().setLineColor(Color.BLACK)).setPadding(2).setHorizontalAlignment(HorizontalAlignment.LEFT)),
				cmp.text("Finalidad de la Consulta: No Aplica").setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloSubTitulo).setBorder(stl.pen1Point().setLineColor(Color.BLACK)).setPadding(2).setHorizontalAlignment(HorizontalAlignment.LEFT)),
				cmp.text("Conducta a seguir:  Cama Observación y/o Reanimación de Urgencias").setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloSubTitulo).setBorder(stl.pen1Point().setLineColor(Color.BLACK)).setPadding(2).setHorizontalAlignment(HorizontalAlignment.LEFT)),
				cmp.verticalList(cmp.text("Descripción:").setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloSubTitulo).setHorizontalAlignment(HorizontalAlignment.LEFT)),
						cmp.text("17/12/2010 15:51 Cama Observación y/o Reanimación de Urgencias ERIKA VIVIANA QUINTERO RIOS. R.M. 07271/02 CC. 30393585. Especialidades: MEDICINA GENERAL.")
						.setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloSubTituloSinBold).setHorizontalAlignment(HorizontalAlignment.LEFT)) 
				).setStyle(stl.style().setBorder(stl.pen1Point().setLineColor(Color.BLACK)).setPadding(2))


		)



		)	;
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
