/**
 * 
 */
package com.servinte.axioma.generadorReporte.historiaClinica;

import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;
import static net.sf.dynamicreports.report.builder.DynamicReports.margin;
import static net.sf.dynamicreports.report.builder.DynamicReports.report;
import static net.sf.dynamicreports.report.builder.DynamicReports.stl;
import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.builder.MarginBuilder;
import net.sf.dynamicreports.report.builder.component.HorizontalListBuilder;
import net.sf.dynamicreports.report.builder.component.TextFieldBuilder;
import net.sf.dynamicreports.report.constant.HorizontalAlignment;
import net.sf.dynamicreports.report.constant.SplitType;
import util.ConstantesBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.reportes.dinamico.EstilosReportesDinamicosHistoriaClinica;

/**
 * @author JorOsoVe
 *  
 */
public class GeneradorSubReporteOrdenaAmbulatorias 
{

	public JasperReportBuilder generarReporte(DtoImpresionHistoriaClinica dto) 
	{
		JasperReportBuilder report = report();

		GeneradorDisenoSubReportes generadorDiseñoSubReportes = new GeneradorDisenoSubReportes();
		report.setTemplate(generadorDiseñoSubReportes.crearPlantillaReporte());
		
		HorizontalListBuilder itemComponent;
		
		
		TextFieldBuilder<String> texto;
		TextFieldBuilder<String> titulo;
		
		int tamanio=Utilidades.convertirAEntero(dto.getOrdenesAmbulatorias().get("numRegistros")+"");
		
		if(tamanio>0){
		titulo=cmp.text("Ordenes Ambulatorias").setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloTituloUnderline).underline().setHorizontalAlignment(HorizontalAlignment.LEFT));
		itemComponent=cmp.horizontalList(titulo);
		
		report.summary(cmp.verticalList(itemComponent));
		
		}

		for(int i=0;i<tamanio;i++)
		  {
			  // Si es una orden de articulos
			  if(Utilidades.convertirAEntero(dto.getOrdenesAmbulatorias().get("tipoorden_"+i)+"") == ConstantesBD.codigoTipoOrdenAmbulatoriaArticulos)
			  {
				  	JasperReportBuilder reporteInterno=report();
					reporteInterno.setTemplate(generadorDiseñoSubReportes.crearPlantillaReporte());
					reporteInterno.setPageMargin(crearMagenesReporte());
					HorizontalListBuilder itemComponentInterno;
					
					String tempo="Nro Orden de Medicamentos: "+dto.getOrdenesAmbulatorias().get("numero_"+i)+" Fecha/Hora: "+UtilidadFecha.conversionFormatoFechaAAp(dto.getOrdenesAmbulatorias().get("fecha_"+i)+"")+" - "+dto.getOrdenesAmbulatorias().get("hora_"+i);
					titulo=cmp.text(tempo).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeNegrilla).setHorizontalAlignment(HorizontalAlignment.LEFT));
					itemComponentInterno=cmp.horizontalList(titulo);
					
					itemComponentInterno.newRow();
					
					tempo="Código";
					texto=cmp.text(tempo).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeNegrilla).setHorizontalAlignment(HorizontalAlignment.CENTER));
					itemComponentInterno.add(texto);
					
					tempo="Descripción";
					texto=cmp.text(tempo).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeNegrilla).setHorizontalAlignment(HorizontalAlignment.CENTER));
					itemComponentInterno.add(texto);
					
					tempo="Dosis";
					texto=cmp.text(tempo).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeNegrilla).setHorizontalAlignment(HorizontalAlignment.CENTER));
					itemComponentInterno.add(texto);
					
					tempo="Frecuencia";
					texto=cmp.text(tempo).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeNegrilla).setHorizontalAlignment(HorizontalAlignment.CENTER));
					itemComponentInterno.add(texto);
					
					tempo="Vía";
					texto=cmp.text(tempo).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeNegrilla).setHorizontalAlignment(HorizontalAlignment.CENTER));
					itemComponentInterno.add(texto);
					
					tempo="Cantidad";
					texto=cmp.text(tempo).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeNegrilla).setHorizontalAlignment(HorizontalAlignment.CENTER));
					itemComponentInterno.add(texto);
					
					tempo="Días Trat.";
					texto=cmp.text(tempo).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeNegrilla).setHorizontalAlignment(HorizontalAlignment.CENTER));
					itemComponentInterno.add(texto);
					
					tempo="Indicaciones";
					texto=cmp.text(tempo).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeNegrilla).setHorizontalAlignment(HorizontalAlignment.CENTER));
					itemComponentInterno.add(texto);
					
					itemComponentInterno.newRow();
					
					tempo=dto.getOrdenesAmbulatorias().get("codigo_"+i)+"";
					texto=cmp.text(tempo).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde).setFontSize(5).setHorizontalAlignment(HorizontalAlignment.CENTER));
					itemComponentInterno.add(texto);
					
					tempo=dto.getOrdenesAmbulatorias().get("descripcion_"+i)+"";
					texto=cmp.text(tempo).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde).setHorizontalAlignment(HorizontalAlignment.CENTER));
					itemComponentInterno.add(texto);
					
					tempo=dto.getOrdenesAmbulatorias().get("dosis_"+i)+"";
					texto=cmp.text(tempo).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde).setHorizontalAlignment(HorizontalAlignment.CENTER));
					itemComponentInterno.add(texto);
					
					tempo=dto.getOrdenesAmbulatorias().get("frecuencia_"+i)+" "+dto.getOrdenesAmbulatorias().get("nomtipofrecuencia_"+i);
					texto=cmp.text(tempo).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde).setHorizontalAlignment(HorizontalAlignment.CENTER));
					itemComponentInterno.add(texto);
					
					tempo=dto.getOrdenesAmbulatorias().get("nomvia_"+i)+"";
					texto=cmp.text(tempo).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde).setHorizontalAlignment(HorizontalAlignment.CENTER));
					itemComponentInterno.add(texto);
					
					tempo=dto.getOrdenesAmbulatorias().get("cantidad_"+i)+"";
					texto=cmp.text(tempo).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde).setHorizontalAlignment(HorizontalAlignment.CENTER));
					itemComponentInterno.add(texto);
					
					tempo=dto.getOrdenesAmbulatorias().get("duraciontratamiento_"+i)+"";
					texto=cmp.text(tempo).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde).setHorizontalAlignment(HorizontalAlignment.CENTER));
					itemComponentInterno.add(texto);
					
					tempo=dto.getOrdenesAmbulatorias().get("indicaciones_"+i)+"";
					texto=cmp.text(tempo).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde).setHorizontalAlignment(HorizontalAlignment.CENTER));
					itemComponentInterno.add(texto);
					
					itemComponentInterno.newRow();
					
					tempo="Indicaciones Generales ";
					texto=cmp.text(tempo).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeNegrilla).setHorizontalAlignment(HorizontalAlignment.CENTER));
					itemComponentInterno.add(texto);
					
					tempo=dto.getOrdenesAmbulatorias().get("observaciones_"+i)+"";
					texto=cmp.text(tempo).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde).setHorizontalAlignment(HorizontalAlignment.CENTER));
					itemComponentInterno.add(texto);
					
					itemComponentInterno.newRow();
					
					tempo="Profesional que Prescribe ";
					texto=cmp.text(tempo).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeNegrilla).setHorizontalAlignment(HorizontalAlignment.CENTER));
					itemComponentInterno.add(texto);
					
					tempo=dto.getOrdenesAmbulatorias().get("profesional_"+i)+" "+dto.getOrdenesAmbulatorias().get("especialidades_"+i);
					texto=cmp.text(tempo).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde).setHorizontalAlignment(HorizontalAlignment.CENTER));
					itemComponentInterno.add(texto);
					
					
					reporteInterno.summary(cmp.verticalList(itemComponentInterno));
					
					reporteInterno.build();
					
					report.summary(cmp.subreport(reporteInterno));
					report.setSummarySplitType(SplitType.IMMEDIATE);
			  }
			  else if (Utilidades.convertirAEntero(dto.getOrdenesAmbulatorias().get("tipoorden_"+i)+"") == ConstantesBD.codigoTipoOrdenAmbulatoriaServicios ) 
			  {
				  	JasperReportBuilder reporteInterno=report();
					reporteInterno.setTemplate(generadorDiseñoSubReportes.crearPlantillaReporte());
					reporteInterno.setPageMargin(crearMagenesReporte());
					HorizontalListBuilder itemComponentInterno;
					
					String tempo="Nro Orden de Servicios: "+dto.getOrdenesAmbulatorias().get("numero_"+i)+" Fecha/Hora: "+UtilidadFecha.conversionFormatoFechaAAp(dto.getOrdenesAmbulatorias().get("fecha_"+i)+"")+" - "+dto.getOrdenesAmbulatorias().get("hora_"+i);
					titulo=cmp.text(tempo).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeNegrilla).setHorizontalAlignment(HorizontalAlignment.LEFT));
					itemComponentInterno=cmp.horizontalList(titulo);
					
					itemComponentInterno.newRow();
					
					tempo="Código";
					texto=cmp.text(tempo).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeNegrilla).setHorizontalAlignment(HorizontalAlignment.CENTER));
					itemComponentInterno.add(texto);
					
					tempo="Descripción";
					texto=cmp.text(tempo).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeNegrilla).setHorizontalAlignment(HorizontalAlignment.CENTER));
					itemComponentInterno.add(texto);
					
					tempo="Cantidad";
					texto=cmp.text(tempo).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeNegrilla).setHorizontalAlignment(HorizontalAlignment.CENTER));
					itemComponentInterno.add(texto);
					
					itemComponentInterno.newRow();
					
					tempo=dto.getOrdenesAmbulatorias().get("codigo_"+i)+"";
					texto=cmp.text(tempo).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde).setHorizontalAlignment(HorizontalAlignment.CENTER));
					itemComponentInterno.add(texto);
					
					tempo=dto.getOrdenesAmbulatorias().get("descripcion_"+i)+"";
					texto=cmp.text(tempo).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde).setHorizontalAlignment(HorizontalAlignment.CENTER));
					itemComponentInterno.add(texto);
					
					tempo=dto.getOrdenesAmbulatorias().get("cantidad_"+i)+"";
					texto=cmp.text(tempo).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde).setHorizontalAlignment(HorizontalAlignment.CENTER));
					itemComponentInterno.add(texto);
										
					itemComponentInterno.newRow();
					
					tempo="Indicaciones Generales ";
					texto=cmp.text(tempo).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeNegrilla).setHorizontalAlignment(HorizontalAlignment.CENTER));
					itemComponentInterno.add(texto);
					
					tempo=dto.getOrdenesAmbulatorias().get("observaciones_"+i)+"";
					texto=cmp.text(tempo).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde).setHorizontalAlignment(HorizontalAlignment.CENTER));
					itemComponentInterno.add(texto);
					
					if(!UtilidadTexto.isEmpty(dto.getOrdenesAmbulatorias().get("resultado_"+i)+""))
					{
						itemComponentInterno.newRow();
						
						tempo="Resultado ";
						texto=cmp.text(tempo).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeNegrilla).setHorizontalAlignment(HorizontalAlignment.CENTER));
						itemComponentInterno.add(texto);
						
						tempo=dto.getOrdenesAmbulatorias().get("resultado_"+i)+"";
						texto=cmp.text(tempo).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde).setHorizontalAlignment(HorizontalAlignment.CENTER));
						itemComponentInterno.add(texto);
					
					}
					
					itemComponentInterno.newRow();
					
					tempo="Profesional que Prescribe ";
					texto=cmp.text(tempo).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeNegrilla).setHorizontalAlignment(HorizontalAlignment.CENTER));
					itemComponentInterno.add(texto);
					
					tempo=dto.getOrdenesAmbulatorias().get("profesional_"+i)+" "+dto.getOrdenesAmbulatorias().get("especialidades_"+i);
					texto=cmp.text(tempo).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde).setHorizontalAlignment(HorizontalAlignment.CENTER));
					itemComponentInterno.add(texto);
					
					 
					reporteInterno.summary(cmp.verticalList(itemComponentInterno));
					
					reporteInterno.build();
					
					report.summary(cmp.subreport(reporteInterno));
					report.setSummarySplitType(SplitType.IMMEDIATE);
			  }
		  }
		
		
		
		report
		.setPageMargin(crearMagenesReporte())
		.setDetailSplitType(SplitType.PREVENT)
		.build();
		
		return report;
		
	}
	
	

	

	public MarginBuilder crearMagenesReporte()
	 {
	  MarginBuilder margin;
	  margin = margin()
	  .setLeft(0)
	  .setRight(0)
	  ;

	  return margin;
	 }

}  



