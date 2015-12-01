package com.servinte.axioma.generadorReporte.historiaClinica;

import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;
import static net.sf.dynamicreports.report.builder.DynamicReports.col;
import static net.sf.dynamicreports.report.builder.DynamicReports.report;
import static net.sf.dynamicreports.report.builder.DynamicReports.stl;
import static net.sf.dynamicreports.report.builder.DynamicReports.type;

import java.awt.Color;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.builder.component.VerticalListBuilder;
import net.sf.dynamicreports.report.constant.HorizontalAlignment;
import net.sf.dynamicreports.report.constant.LineStyle;
import net.sf.dynamicreports.report.constant.SplitType;
import net.sf.dynamicreports.report.constant.VerticalAlignment;
import net.sf.jasperreports.engine.JRDataSource;
import util.UtilidadBD;
import util.historiaClinica.UtilidadesHistoriaClinica;
import util.reportes.dinamico.DataSource;
import util.reportes.dinamico.EstilosReportesDinamicosHistoriaClinica;

public class GeneradorSubReporteNotasGeneralesHojaQuirurgica {

	public   JasperReportBuilder reporteNotasGeneralesCirugia(DtoHojaQuirurgicaAnestesia datosCirugia) throws NumberFormatException, Exception{
		JasperReportBuilder reportNotasGenerales = report();
		GeneradorDisenoSubReportes disenio = new GeneradorDisenoSubReportes();


		VerticalListBuilder listaServicios=cmp.verticalList(cmp.text("Notas Enfermería Acto quirúrgico").setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloSubTituloFont5).setHorizontalAlignment(HorizontalAlignment.LEFT)));
		listaServicios.add(cmp.text("Cirugías:").setStyle(stl.style().setPadding(1).setFontSize(7)));
		
		if(datosCirugia.getMapaEncabezadosHojaAnestesia().containsKey("numRegistros")
				&&Integer.parseInt(datosCirugia.getMapaEncabezadosHojaAnestesia().get("numRegistros").toString())>0){
			Connection con = UtilidadBD.abrirConexion();
			ArrayList<String> servicios=UtilidadesHistoriaClinica.serviciosHojaQuirurgica(con, Integer.valueOf(String.valueOf(   datosCirugia.getMapaEncabezadosHojaAnestesia().get("numero_solicitud_0") ))   );
			UtilidadBD.closeConnection(con);
	
	
			for (int i = 0; i < servicios.size(); i++) {
				listaServicios.add(cmp.text(servicios.get(i)).setStyle(stl.style().setPadding(1).setFontSize(7)));
			}
	
	
			HashMap mapaNotas=datosCirugia.getMapaNotasEnfer();
			Integer numReg= Integer.valueOf(String.valueOf(mapaNotas.get("numRegistros")));
	
			if(servicios.size()>0 && numReg>0){
				reportNotasGenerales
				.summary(cmp.horizontalList(listaServicios).setStyle(EstilosReportesDinamicosHistoriaClinica.estiloBorde))
				.setDetailSplitType(SplitType.PREVENT)
				.summary(cmp.subreport(subReporteNotasGeneralesCirugia(datosCirugia)))
				.setTemplate(disenio.crearPlantillaReporte())
				.setPageMargin(disenio.crearMagenesSubReporte())
				.build()
				;
			}else{
				reportNotasGenerales
				.setDetailSplitType(SplitType.PREVENT)
				.summary(cmp.subreport(subReporteNotasGeneralesCirugia(datosCirugia)))
				.setTemplate(disenio.crearPlantillaReporte())
				.setPageMargin(disenio.crearMagenesSubReporte())
				.build()
				;
			}
		}
		return reportNotasGenerales;
	}


	public   JasperReportBuilder subReporteNotasGeneralesCirugia(DtoHojaQuirurgicaAnestesia datosCirugia) throws NumberFormatException, Exception{
		JasperReportBuilder reportNotasGenerales = report();
		GeneradorDisenoSubReportes disenio = new GeneradorDisenoSubReportes();


		TextColumnBuilder []cols = new TextColumnBuilder[3];
		VerticalListBuilder listaServicios=cmp.verticalList();

		TextColumnBuilder<String>     fechaHora = col.column("Fecha/hora",  "fechaHora",  type.stringType()).setTitleStyle(stl.style().setBackgroundColor(Color.LIGHT_GRAY).setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK)));
		TextColumnBuilder<String>     anotacionesEnfermeria = col.column("Anotaciones Enfermería",  "anotacionesEnfermeria",  type.stringType()).setTitleStyle(stl.style().setBackgroundColor(Color.LIGHT_GRAY).setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK)));
		TextColumnBuilder<String>     usuario = col.column("Usuario",  "usuario",  type.stringType()).setTitleStyle(stl.style().setBackgroundColor(Color.LIGHT_GRAY).setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK)));

		cols[0]=fechaHora.setWidth(40);
		cols[1]=anotacionesEnfermeria.setStretchWithOverflow(true);
		cols[2]=usuario.setStretchWithOverflow(true).setWidth(130);

		reportNotasGenerales
		.columns(cols)
		.setDataSource(crearDataSSourceNotasGeneralesQuirurgica(datosCirugia))
		.setDetailSplitType(SplitType.PREVENT)
		.setTemplate(disenio.crearPlantillaReporte())
		.setPageMargin(disenio.crearMagenesSubReporte())
		.build()
		;

		return reportNotasGenerales;
	}

	public JRDataSource crearDataSSourceNotasGeneralesQuirurgica(DtoHojaQuirurgicaAnestesia datosCirugia){
		String[] datosDataSource = new String[3];
		ArrayList<String[]> listaData  = new ArrayList<String[]>();

		HashMap mapaNotas=datosCirugia.getMapaNotasEnfer();
		Integer numReg= Integer.valueOf(String.valueOf(mapaNotas.get("numRegistros")));
		for (int j = 0; j < numReg; j++) {
			datosDataSource[0]=String.valueOf(mapaNotas.get("fecha_grabacion_"+j))+"  "+String.valueOf(mapaNotas.get("hora_grabacion_"+j));
			datosDataSource[1]=String.valueOf(mapaNotas.get("nota_"+j));
			datosDataSource[2]=String.valueOf(mapaNotas.get("enfermero_"+j))+" "+String.valueOf(mapaNotas.get("especialidades_"+j));
			listaData.add(datosDataSource);
			datosDataSource = new String[3];
		}
		return imvertirDataSource(listaData);
	}

	public DataSource imvertirDataSource(ArrayList<String[]> listaData){

		DataSource dataSource = new DataSource("fechaHora","anotacionesEnfermeria","usuario");
		Integer tamLista=listaData.size()-1;
		for (int i = 0; i <= tamLista; i++) {
			dataSource.add(listaData.get(tamLista-i));
		}


		return dataSource;
	}


}
