package com.servinte.axioma.generadorReporte.historiaClinica;

import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;
import static net.sf.dynamicreports.report.builder.DynamicReports.report;
import static net.sf.dynamicreports.report.builder.DynamicReports.stl;

import java.util.ArrayList;
import java.util.List;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.builder.component.HorizontalListBuilder;
import net.sf.dynamicreports.report.constant.HorizontalAlignment;

import org.apache.struts.util.MessageResources;

import util.reportes.dinamico.EstilosReportesDinamicosHistoriaClinica;

public class GeneradorSubReporteValoracionesUrgenciasHospitalizacion {
	/**
	 * Mensajes parametrizados del reporte.
	 */ 
	private static MessageResources messageResource = MessageResources.getMessageResources("com.servinte.mensajes.historiaclinica.impresionHistoriaClinica");


	
	public JasperReportBuilder generarSubReporte( ) {
		JasperReportBuilder valoracion = report();
		ComponentBuilder<?, ?> componentBuilder = null;
		ComponentBuilder[] listaComponentesAPintar;
		List<HorizontalListBuilder> listaHorizonatBuilder= new ArrayList<HorizontalListBuilder>();
		HorizontalListBuilder tituloSeccion = null;
		HorizontalListBuilder varolacionUrgenciasTituloSeccion = null;
		HorizontalListBuilder informacionGeneralSeccion = null;
		
		
		tituloSeccion=	cmp.horizontalList(cmp.verticalList(cmp.horizontalList(
				cmp.text(messageResource.getMessage("historia_clinica_lable_antecedentes_titulo")).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloTitulo).setHorizontalAlignment(HorizontalAlignment.LEFT))
		)))	;
		listaHorizonatBuilder.add(tituloSeccion);
		
		
		
		listaComponentesAPintar = new ComponentBuilder[listaHorizonatBuilder.size()];
		for (int i = 0; i < listaComponentesAPintar.length; i++) {
			listaComponentesAPintar[i]=listaHorizonatBuilder.get(i);
		}
		
		
		componentBuilder = cmp.verticalList(listaComponentesAPintar);
		valoracion.summary(componentBuilder);
		
		GeneradorSubReporteValoracionAntecedentes generadorSubReporteValoracionAntecedentes = new GeneradorSubReporteValoracionAntecedentes();
		valoracion.summary(cmp.subreport(generadorSubReporteValoracionAntecedentes.generarSubReporteOtrosAntecedentes()));
		
		GeneradorSubReporteValoracionInformacionGeneral generadorSubReporteValoracionInformacionGeneral = new GeneradorSubReporteValoracionInformacionGeneral();
		valoracion.summary(cmp.subreport(generadorSubReporteValoracionInformacionGeneral.generarReporteSeccionInformacionBasica()));
		
		GeneradorSubReporteValoracionSignosVitales generadorSubReporteValoracionSignosVitales = new GeneradorSubReporteValoracionSignosVitales();
		valoracion.summary(cmp.subreport(generadorSubReporteValoracionSignosVitales.generarReporteSeccionInformacionBasica()));
		
		
		GeneradorSubReporteValoracionesRevisionesPorSistema generadorSubReporteValoracionesRevisionesPorSistema = new GeneradorSubReporteValoracionesRevisionesPorSistema();
		valoracion.summary(cmp.subreport(generadorSubReporteValoracionesRevisionesPorSistema.generarReporteSeccionRevisionesSistema()));
		
		GeneradorSubReporteValoracionesDiagnosticos generadorSubReporteValoracionesDiagnosticos = new GeneradorSubReporteValoracionesDiagnosticos();
		valoracion.summary(cmp.subreport(generadorSubReporteValoracionesDiagnosticos.generarReporteSeccionDiagnosticos()));
		
		
		
		
		GeneradorSubReporteValoracionesObservaciones generadorSubReporteValoracionesObservaciones= new GeneradorSubReporteValoracionesObservaciones();
		valoracion.summary(cmp.subreport(generadorSubReporteValoracionesObservaciones.generarReporteSeccionObservaciones()));
		
		valoracion.build();
		return valoracion;
	}
	
	
	public JasperReportBuilder generarValoraciones(){
		JasperReportBuilder valoracionesTotales = report();
		valoracionesTotales.summary  (cmp.subreport( generarSubReporte())).build();
		
		return valoracionesTotales;
	} 
	

	
	
}
