package com.servinte.axioma.generadorReporte.historiaClinica;

import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;
import static net.sf.dynamicreports.report.builder.DynamicReports.col;
import static net.sf.dynamicreports.report.builder.DynamicReports.report;
import static net.sf.dynamicreports.report.builder.DynamicReports.stl;
import static net.sf.dynamicreports.report.builder.DynamicReports.type;

import java.awt.Color;
import java.util.HashMap;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.builder.component.VerticalListBuilder;
import net.sf.dynamicreports.report.constant.HorizontalAlignment;
import net.sf.dynamicreports.report.constant.LineStyle;
import net.sf.dynamicreports.report.constant.SplitType;
import net.sf.dynamicreports.report.constant.VerticalAlignment;
import net.sf.jasperreports.engine.JRDataSource;
import util.UtilidadTexto;
import util.reportes.dinamico.DataSource;
import util.reportes.dinamico.EstilosReportesDinamicosHistoriaClinica;

public class GeneradorSubReporteNotasRecuperacionHojaQuirurgica {



	private Integer cantidadRegistros= new Integer(0);

	public   JasperReportBuilder reporteNotasRecuperacionCirugia(DtoHojaQuirurgicaAnestesia datosCirugia) throws NumberFormatException, Exception{
		JasperReportBuilder reportNotasRecuperacion = report();
		GeneradorDisenoSubReportes disenio = new GeneradorDisenoSubReportes();

		VerticalListBuilder listaTitulo=cmp.verticalList(cmp.text("Notas de Recuperación Acto Quirúrgico").setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloSubTituloFont5).setHorizontalAlignment(HorizontalAlignment.LEFT)));



		reportNotasRecuperacion
		.summary(cmp.horizontalList(listaTitulo).setStyle(EstilosReportesDinamicosHistoriaClinica.estiloBorde))
		.summary(cmp.subreport(reporteNotasRecuperacionCirugiaDetalle(datosCirugia)))
		.setDetailSplitType(SplitType.PREVENT)
		.setTemplate(disenio.crearPlantillaReporte())
		.setPageMargin(disenio.crearMagenesSubReporte());

		if(!UtilidadTexto.isEmpty(String.valueOf(datosCirugia.getMapaNotasRecuperacion().get("o_medicamentos_0")))){
			reportNotasRecuperacion
			.summary(cmp.subreport(reporteNotasRecuperacionCirugiaMedicamentos(datosCirugia)));
		}

		if(!UtilidadTexto.isEmpty(String.valueOf(datosCirugia.getMapaNotasRecuperacion().get("o_observaciones_0")))){
			reportNotasRecuperacion
			.summary(cmp.subreport(reporteNotasRecuperacionCirugiaObservaciones(datosCirugia)));
		}

		if(this.cantidadRegistros==17){

		}

		reportNotasRecuperacion.build();
		return reportNotasRecuperacion;
	}



	public   JasperReportBuilder reporteNotasRecuperacionCirugiaObservaciones(DtoHojaQuirurgicaAnestesia datosCirugia) throws NumberFormatException, Exception{
		JasperReportBuilder reportNotasRecuperacion = report();
		GeneradorDisenoSubReportes disenio = new GeneradorDisenoSubReportes();

		VerticalListBuilder listaObservaciones=cmp.verticalList(cmp.text("Observaciones").setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloSubTituloFont5).setHorizontalAlignment(HorizontalAlignment.LEFT)));
		listaObservaciones.add(cmp.text(String.valueOf(datosCirugia.getMapaNotasRecuperacion().get("o_observaciones_0"))).setStyle(stl.style().setPadding(1).setFontSize(7)));


		reportNotasRecuperacion
		.setSummarySplitType(SplitType.PREVENT)
		.setTemplate(disenio.crearPlantillaReporte())
		.setPageMargin(disenio.crearMagenesSubReporte());

		if(!UtilidadTexto.isEmpty(String.valueOf(datosCirugia.getMapaNotasRecuperacion().get("o_observaciones_0")))){
			reportNotasRecuperacion
			.summary(cmp.horizontalList(listaObservaciones).setStyle(EstilosReportesDinamicosHistoriaClinica.estiloBorde));
		}


		reportNotasRecuperacion.build();
		return reportNotasRecuperacion;
	}


	public   JasperReportBuilder reporteNotasRecuperacionCirugiaMedicamentos(DtoHojaQuirurgicaAnestesia datosCirugia) throws NumberFormatException, Exception{
		JasperReportBuilder reportNotasRecuperacion = report();
		GeneradorDisenoSubReportes disenio = new GeneradorDisenoSubReportes();

		VerticalListBuilder listaServicios=cmp.verticalList(cmp.text("Medicamentos").setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloSubTituloFont5).setHorizontalAlignment(HorizontalAlignment.LEFT)));
		listaServicios.add(cmp.text(String.valueOf(datosCirugia.getMapaNotasRecuperacion().get("o_medicamentos_0"))).setStyle(stl.style().setPadding(1).setFontSize(7)));



		reportNotasRecuperacion
		.setSummarySplitType(SplitType.PREVENT)
		.setTemplate(disenio.crearPlantillaReporte())
		.setPageMargin(disenio.crearMagenesSubReporte());

		if(!UtilidadTexto.isEmpty(String.valueOf(datosCirugia.getMapaNotasRecuperacion().get("o_medicamentos_0")))){
			reportNotasRecuperacion
			.summary(cmp.horizontalList(listaServicios).setStyle(EstilosReportesDinamicosHistoriaClinica.estiloBorde));
		}


		reportNotasRecuperacion.build();
		return reportNotasRecuperacion;
	}



	public   JasperReportBuilder reporteNotasRecuperacionCirugiaDetalle(DtoHojaQuirurgicaAnestesia datosCirugia) throws NumberFormatException, Exception{
		JasperReportBuilder reportNotasRecuperacion = report();
		GeneradorDisenoSubReportes disenio = new GeneradorDisenoSubReportes();
		TextColumnBuilder []cols = new TextColumnBuilder[3];

		TextColumnBuilder<String>     agrupador = col.column( "agrupador",  type.stringType()).setTitleStyle(stl.style().setBackgroundColor(Color.LIGHT_GRAY).setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK)));
		TextColumnBuilder<String>     descripcion = col.column( "descripcion",  type.stringType()).setTitleStyle(stl.style().setBackgroundColor(Color.LIGHT_GRAY).setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK)));
		TextColumnBuilder<String>     observaciones = col.column(  "observaciones",  type.stringType()).setTitleStyle(stl.style().setBackgroundColor(Color.LIGHT_GRAY).setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK)));


		cols[0]=agrupador.setWidth(28);
		cols[1]=descripcion.setStretchWithOverflow(true).setWidth(30);
		cols[2]=observaciones.setStretchWithOverflow(true).setWidth(180);

		reportNotasRecuperacion
		.columns(cols)
		.setDataSource(crearDataSSourceNotasRecuperacionQuirurgica(datosCirugia))
		.setDetailSplitType(SplitType.PREVENT)
		.setTemplate(disenio.crearPlantillaReporte())
		.setPageMargin(disenio.crearMagenesSubReporte())
		.build() ;



		return reportNotasRecuperacion;
	}


	public JRDataSource crearDataSSourceNotasRecuperacionQuirurgica(DtoHojaQuirurgicaAnestesia datosCirugia){
		String[] datosDataSource = new String[3];
		DataSource dataSource = new DataSource("agrupador","descripcion","observaciones");

		for (int i = 0; i < 18; i++) {
			datosDataSource[0]=obtenerAgrupador(String.valueOf(datosCirugia.getMapaNotasRecuperacionDetalle().get("codigotipo_"+i)));
			datosDataSource[1]=String.valueOf(datosCirugia.getMapaNotasRecuperacionDetalle().get("nombrecampo_"+i));

			String tmp = obtenerListaNotas( (HashMap) datosCirugia.getMapaNotasRecuperacionDetalle().get("detNota_"+i));
			datosDataSource[2]=tmp;
			if(tmp.equals("-1")){
				cantidadRegistros++;	
			}

			if(!tmp.equals("-1")){
				dataSource.add(datosDataSource);
			}
		}


		return dataSource;
	}


	public String obtenerListaNotas(HashMap notas){
		//numRegistros
		String res="";
		Integer tam =0;
		if(notas!=null){
			tam = Integer.valueOf(String.valueOf(notas.get("numRegistros")));
			for (int i = 0; i < tam; i++) {
				if(!UtilidadTexto.isEmpty(String.valueOf(notas.get("valornota_"+i)))){
					res+=String.valueOf(notas.get("valornota_"+i))+"\n";
				}
				res+=String.valueOf(notas.get("fechagrabacion_"+i))+" "+String.valueOf(notas.get("horarecuperacion_"+i))+"\n";
				res+=String.valueOf(notas.get("enfermera_"+i))+"\n\n";
			}
		}
		if(tam>0){
			if(res!=null){
				return res;
			}else{
				return "";
			}
		}else{
			return "-1";
		}


	}


	public String obtenerAgrupador(String agru){
		String valor = "";
		if(agru.equals("1")){
			valor="Color";
		}else if(agru.equals("2")){
			valor="Conciencia";
		}else if(agru.equals("3")){
			valor="Respuesta Motora";
		}else if(agru.equals("4")){
			valor="Signos Vitales";
		}else if(agru.equals("5")){
			valor="Ventilacion";
		}
		return valor;
	}



	/**
	 * @return the cantidadRegistros
	 */
	public Integer getCantidadRegistros() {
		return cantidadRegistros;
	}



	/**
	 * @param cantidadRegistros the cantidadRegistros to set
	 */
	public void setCantidadRegistros(Integer cantidadRegistros) {
		this.cantidadRegistros = cantidadRegistros;
	}




}
