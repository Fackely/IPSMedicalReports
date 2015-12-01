package com.servinte.axioma.generadorReporte.historiaClinica;

import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;
import static net.sf.dynamicreports.report.builder.DynamicReports.col;
import static net.sf.dynamicreports.report.builder.DynamicReports.report;
import static net.sf.dynamicreports.report.builder.DynamicReports.stl;
import static net.sf.dynamicreports.report.builder.DynamicReports.type;

import java.awt.Color;
import java.util.List;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.builder.component.HorizontalListBuilder;
import net.sf.dynamicreports.report.constant.HorizontalAlignment;
import net.sf.dynamicreports.report.constant.LineStyle;
import net.sf.dynamicreports.report.constant.SplitType;
import net.sf.dynamicreports.report.constant.VerticalAlignment;
import net.sf.jasperreports.engine.JRDataSource;
import util.reportes.dinamico.DataSource;

import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.servinte.axioma.dto.manejoPaciente.DtoNotaAclaratoria;

public class GeneradorSubReporteNotasAclaratorias {




	/**
	 * Metodo encargado de armar el reporte de notas aclaratorias 
	 * @param notasAclaratorias
	 * @param usuario
	 * @param paciente
	 * @return JasperReportBuilder con el reporte
	 */
	public   JasperReportBuilder reporteNotasAclaratorias(List<DtoNotaAclaratoria> notasAclaratorias,  UsuarioBasico usuario, PersonaBasica paciente){
		
		//INSTACIA DE REPORTE VACIO
		JasperReportBuilder reportNotasAclaratorias = report();
		
		//HORIZONTAL  PARA TITULO 
		HorizontalListBuilder tituloSeccion = null;
		
		//CONTENEDOR DE COMPONENT
		ComponentBuilder<?, ?> componentBuilder = null;
		
		//CLASE CON DISEÑO DE Y PLANTILLAS
		GeneradorDisenoSubReportes disenio = new GeneradorDisenoSubReportes();

		
		//TITULO DE SECCION DE NOTAS 
		tituloSeccion=	cmp.horizontalList(cmp.verticalList(cmp.horizontalList(
				cmp.text("Notas Aclaratorias").setStyle(stl.style().bold().setBackgroundColor(Color.LIGHT_GRAY).setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.LEFT).setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK)))
		)))	;
		//ADICION DE TIRULO A COMPONENT BUILDER
		componentBuilder = cmp.verticalList(tituloSeccion);

		//VALIDACION DE APARICION DE TITULO CUANDO HAY DATOS A MOSTRAR
		if(notasAclaratorias.size()>0){
			
			//ADICION DE TITULO CUANDO TIENE INFORMACION, SUBREPORTE DE DETAIL DE NOTAS
			reportNotasAclaratorias
			.summary(componentBuilder)
			.summary(cmp.subreport(generarReporteNotasAclaratoriasDetail(notasAclaratorias, usuario, paciente)))
			.setTemplate(disenio.crearPlantillaReporte())
			.setPageMargin(disenio.crearMagenesReporte())
			.build();
		}else{
			
			//REPORTE CUANDO NO HAY DATOS LO ENVIA VACIO
			reportNotasAclaratorias
			.build();
		}
		
		//REPORTE SOLICITADO
		return  reportNotasAclaratorias;
	}



	/**
	 * Metodo encargado de generar el subreporte-detail de notas aclaratorias 
	 * @param notasAclaratoriasList
	 * @param usuario
	 * @param paciente
	 * @return retorna subreporte con detail de las notas aclaratorias
	 */
	public JasperReportBuilder generarReporteNotasAclaratoriasDetail(List<DtoNotaAclaratoria> notasAclaratoriasList
			,UsuarioBasico usuario, PersonaBasica paciente){
		
		//INSTANCIA DE REPORTE VACIO
		JasperReportBuilder reportNotasAclaratorias = report();

		//ARREGLO PARA  COLUMMAS 
		TextColumnBuilder []cols = new TextColumnBuilder[4];
		
		//INSTANCIA DE CLASE CON TEMPLATES Y MARGENES
		GeneradorDisenoSubReportes disenio = new GeneradorDisenoSubReportes();

		//DEFINICION DE COLUMNAS 
		TextColumnBuilder<String>     fechaHora = col.column("Fecha y Hora",  "fechaHora",  type.stringType()).setTitleStyle(stl.style().setBackgroundColor(Color.LIGHT_GRAY).setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK)));
		TextColumnBuilder<String>     notasAclaratorias = col.column("Notas Aclaratorias",  "notasAclaratorias",  type.stringType()).setTitleStyle(stl.style().setBackgroundColor(Color.LIGHT_GRAY).setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK)));
		TextColumnBuilder<String>     profesionalSalud = col.column("Profesional de la Salud",  "profesionalSalud",  type.stringType()).setTitleStyle(stl.style().setBackgroundColor(Color.LIGHT_GRAY).setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK)));
		TextColumnBuilder<String>     cantArchivos = col.column("Cant. Archivos",  "cantArchivos",  type.stringType()).setTitleStyle(stl.style().setBackgroundColor(Color.LIGHT_GRAY).setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK))).setHorizontalAlignment(HorizontalAlignment.RIGHT);

		//ADICION DE TAMAÑOS DE CADA COLUMNA 
		cols[0]=fechaHora.setWidth(35);
		cols[1]=notasAclaratorias.setWidth(55);
		cols[2]=profesionalSalud.setWidth(80);
		cols[3]=cantArchivos.setWidth(25);

		//ADICION DE COLUMMNAS, DATASORUCE , TEMPLATE Y MARGENES
		reportNotasAclaratorias
		.columns(cols)
		.setDataSource(crearDatasourceNotasAclaratorias(notasAclaratoriasList))
		.setTemplate(disenio.crearPlantillaReporte())
		.setDetailSplitType(SplitType.PREVENT)
		.setPageMargin(disenio.crearMagenesSubReporte())
		.build();

		//REPORTE SOLICITADO
		return reportNotasAclaratorias;

	}

	/**
	 * metodo encargado de llenar el datasource
	 * @param notasAclaratoriasList
	 * @return datasource con los datos a pintar
	 */
	public JRDataSource crearDatasourceNotasAclaratorias(List<DtoNotaAclaratoria> notasAclaratoriasList){
		
		//DEFINICION DE DATASOURCE
		DataSource dataSource = new DataSource("fechaHora","notasAclaratorias",
				"profesionalSalud","cantArchivos");
		
		//ARREGLO CON INFORMACION DE DATASORUCE
		String[] datosDataSource = new String[4];

		//CICLO QUE LLENA EL DATA SOURCE 
		for (DtoNotaAclaratoria dtoNotaAclaratoria : notasAclaratoriasList) {
			datosDataSource[0]=String.valueOf(dtoNotaAclaratoria.getFecha())+" "+dtoNotaAclaratoria.getHora();
			datosDataSource[1]=dtoNotaAclaratoria.getDescripcion();
			datosDataSource[2]=dtoNotaAclaratoria.getNombreCompletoProfesional()+" "+
			dtoNotaAclaratoria.getNumeroRegistroProfesional()+" "+dtoNotaAclaratoria.getEspecialidadesProfesional();
			datosDataSource[3]=String.valueOf(dtoNotaAclaratoria.getNumeroDocumentosAdjuntos());

			dataSource.add(datosDataSource);
			datosDataSource = new String[4];
		}

		//DATASOURCE SOLICITADO 
		return dataSource;
	}
}
