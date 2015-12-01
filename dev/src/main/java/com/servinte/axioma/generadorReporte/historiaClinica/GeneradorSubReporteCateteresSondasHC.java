package com.servinte.axioma.generadorReporte.historiaClinica;

import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;
import static net.sf.dynamicreports.report.builder.DynamicReports.col;
import static net.sf.dynamicreports.report.builder.DynamicReports.report;
import static net.sf.dynamicreports.report.builder.DynamicReports.stl;
import static net.sf.dynamicreports.report.builder.DynamicReports.type;

import java.awt.Color;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.builder.component.HorizontalListBuilder;
import net.sf.dynamicreports.report.constant.HorizontalAlignment;
import net.sf.dynamicreports.report.constant.LineStyle;
import net.sf.dynamicreports.report.constant.SplitType;
import net.sf.dynamicreports.report.constant.VerticalAlignment;
import net.sf.jasperreports.engine.JRDataSource;

import org.apache.struts.util.MessageResources;

import util.UtilidadTexto;
import util.Utilidades;
import util.reportes.dinamico.DataSource;
import util.reportes.dinamico.EstilosReportesDinamicosHistoriaClinica;

import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;

/**
 * Clase utilizada para generar el subreporte de la sección de Cateteres y sondas
 * 
 * @author Ricardo Ruiz 
 *
 */
public class GeneradorSubReporteCateteresSondasHC {

	/**
	 * Mensajes parametrizados del reporte.
	 */
	private static MessageResources messageResource = MessageResources.getMessageResources("com.servinte.mensajes.historiaclinica.impresionHistoriaClinica");

	
	/**
	 * Constructor de la clase
	 */
	public GeneradorSubReporteCateteresSondasHC() {
		
	}
	
	/**
	 * Método encargado de construir el subreporte de la secci[on Cateteres y Sondas
	 * @param dto
	 * @param usuario
	 * @param paciente
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public JasperReportBuilder generarReporteSeccionCateteresSondas(DtoImpresionHistoriaClinica dto
			,UsuarioBasico usuario, PersonaBasica paciente){
		
		JasperReportBuilder subReportCateteresSondas = report();
		
		int numColsDinamicas=0;
		if(dto.getColumnasDinamicasCateteresSondas() != null && !dto.getColumnasDinamicasCateteresSondas().isEmpty()){
			numColsDinamicas=dto.getColumnasDinamicasCateteresSondas().size();
		}
		//Columnas del reporte incluyendo estáticas y dinámicas
		TextColumnBuilder []cols = new TextColumnBuilder[7+numColsDinamicas];
		String []colsData = new String[7+numColsDinamicas];
		GeneradorDisenoSubReportes generadorDisenioSubReportes = new GeneradorDisenoSubReportes();
		
		//Componente del Encabezado del Subreporte o titulo de la sección
		HorizontalListBuilder tituloSeccion=	cmp.horizontalList(cmp.verticalList(cmp.horizontalList(
				cmp.text(messageResource.getMessage("impresionHistoriaClinica.seccionCateteresSondas.titulo"))
					.setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloTituloUnderline).underline()
					.setHorizontalAlignment(HorizontalAlignment.LEFT))
		)))	;
		
		
		TextColumnBuilder<String>     colFechaInsercion = col.column(messageResource.getMessage("impresionHistoriaClinica.seccionCateteresSondas.fechaInsercion"),
																"fechainsercion",  type.stringType()).setTitleStyle(stl.style().setBackgroundColor(Color.LIGHT_GRAY).setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK)));
		TextColumnBuilder<String>     colFechaRetiro = col.column(messageResource.getMessage("impresionHistoriaClinica.seccionCateteresSondas.fechaRetiro"),
																"fecharetiro",  type.stringType()).setTitleStyle(stl.style().setBackgroundColor(Color.LIGHT_GRAY).setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK)));
		TextColumnBuilder<String>     colTipo = col.column(messageResource.getMessage("impresionHistoriaClinica.seccionCateteresSondas.tipo"),
															"tipo",  type.stringType()).setTitleStyle(stl.style().setBackgroundColor(Color.LIGHT_GRAY).setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK)));
		TextColumnBuilder<String>     colViaInsercion = col.column(messageResource.getMessage("impresionHistoriaClinica.seccionCateteresSondas.viaInsercion"),
																"viainsercion",  type.stringType()).setTitleStyle(stl.style().setBackgroundColor(Color.LIGHT_GRAY).setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK)));
		TextColumnBuilder<String>     colCuraciones = col.column(messageResource.getMessage("impresionHistoriaClinica.seccionCateteresSondas.curaciones"),
																"curaciones",  type.stringType()).setTitleStyle(stl.style().setBackgroundColor(Color.LIGHT_GRAY).setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK)));
		TextColumnBuilder<String>     colObservaciones = col.column(messageResource.getMessage("impresionHistoriaClinica.seccionCateteresSondas.observaciones"),
																"observaciones",  type.stringType()).setTitleStyle(stl.style().setBackgroundColor(Color.LIGHT_GRAY).setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK)));
		TextColumnBuilder<String>     colUsuario = col.column(messageResource.getMessage("impresionHistoriaClinica.seccionCateteresSondas.usuario"),
															"usuario",  type.stringType()).setTitleStyle(stl.style().setBackgroundColor(Color.LIGHT_GRAY).setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK)));
		
		//Se Asignan las columnas estáticas
		cols[0]=colFechaInsercion.setWidth(40);
		cols[1]=colFechaRetiro.setWidth(40);
		cols[2]=colTipo.setWidth(80);
		cols[3]=colViaInsercion.setWidth(40);
		cols[4]=colCuraciones;
		cols[5]=colObservaciones;
		
		//Se Asignan las columnas del DataSource estáticas
		colsData[0]="fechainsercion";
		colsData[1]="fecharetiro";
		colsData[2]="tipo";
		colsData[3]="viainsercion";
		colsData[4]="curaciones";
		colsData[5]="observaciones";
		
		if(dto.getColumnasDinamicasCateteresSondas() != null && !dto.getColumnasDinamicasCateteresSondas().isEmpty()){
			//Construimos las columnas Dinámicas
			Iterator  iterator = dto.getColumnasDinamicasCateteresSondas().iterator();
			int i=0;
			while(iterator.hasNext()){
				HashMap element = (HashMap)iterator.next();
				String nombreCol=element.get("nombre").toString();
				TextColumnBuilder<String> colDinamica = col.column(nombreCol,
						nombreCol,  type.stringType()).setTitleStyle(stl.style().setBackgroundColor(Color.LIGHT_GRAY).setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK)));
				//Se Asigna la columna a las columnas del reporte
				cols[6+i]=colDinamica;
				colsData[6+i]=nombreCol;
				i++;
			}
			
		}
		cols[6+numColsDinamicas]=colUsuario.setWidth(40);
		colsData[6+numColsDinamicas]="usuario";
		subReportCateteresSondas
		.summary(cmp.horizontalList(tituloSeccion))
		.summary(cmp.subreport(generarReporteSeccionCateteresSondasDetail(dto, usuario, paciente)))
		.setTemplate(generadorDisenioSubReportes.crearPlantillaReporte())
		.setPageMargin(generadorDisenioSubReportes.crearMagenesReporte())
		.build();
		return subReportCateteresSondas;
		
	}
	
	
	/**
	 * Método encargado de construir el subreporte de la secci[on Cateteres y Sondas
	 * @param dto
	 * @param usuario
	 * @param paciente
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public JasperReportBuilder generarReporteSeccionCateteresSondasDetail(DtoImpresionHistoriaClinica dto
			,UsuarioBasico usuario, PersonaBasica paciente){
		
		JasperReportBuilder subReportCateteresSondas = report();
		
		int numColsDinamicas=0;
		if(dto.getColumnasDinamicasCateteresSondas() != null && !dto.getColumnasDinamicasCateteresSondas().isEmpty()){
			numColsDinamicas=dto.getColumnasDinamicasCateteresSondas().size();
		}
		//Columnas del reporte incluyendo estáticas y dinámicas
		TextColumnBuilder []cols = new TextColumnBuilder[7+numColsDinamicas];
		String []colsData = new String[7+numColsDinamicas];
		GeneradorDisenoSubReportes generadorDisenioSubReportes = new GeneradorDisenoSubReportes();
		
		//Componente del Encabezado del Subreporte o titulo de la sección
		HorizontalListBuilder tituloSeccion=	cmp.horizontalList(cmp.verticalList(cmp.horizontalList(
				cmp.text(messageResource.getMessage("impresionHistoriaClinica.seccionCateteresSondas.titulo"))
					.setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloTituloUnderline).underline()
					.setHorizontalAlignment(HorizontalAlignment.LEFT))
		)))	;
		
		
		TextColumnBuilder<String>     colFechaInsercion = col.column(messageResource.getMessage("impresionHistoriaClinica.seccionCateteresSondas.fechaInsercion"),
																"fechainsercion",  type.stringType()).setTitleStyle(stl.style().setBackgroundColor(Color.LIGHT_GRAY).setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK)));
		TextColumnBuilder<String>     colFechaRetiro = col.column(messageResource.getMessage("impresionHistoriaClinica.seccionCateteresSondas.fechaRetiro"),
																"fecharetiro",  type.stringType()).setTitleStyle(stl.style().setBackgroundColor(Color.LIGHT_GRAY).setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK)));
		TextColumnBuilder<String>     colTipo = col.column(messageResource.getMessage("impresionHistoriaClinica.seccionCateteresSondas.tipo"),
															"tipo",  type.stringType()).setTitleStyle(stl.style().setBackgroundColor(Color.LIGHT_GRAY).setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK)));
		TextColumnBuilder<String>     colViaInsercion = col.column(messageResource.getMessage("impresionHistoriaClinica.seccionCateteresSondas.viaInsercion"),
																"viainsercion",  type.stringType()).setTitleStyle(stl.style().setBackgroundColor(Color.LIGHT_GRAY).setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK)));
		TextColumnBuilder<String>     colCuraciones = col.column(messageResource.getMessage("impresionHistoriaClinica.seccionCateteresSondas.curaciones"),
																"curaciones",  type.stringType()).setTitleStyle(stl.style().setBackgroundColor(Color.LIGHT_GRAY).setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK)));
		TextColumnBuilder<String>     colObservaciones = col.column(messageResource.getMessage("impresionHistoriaClinica.seccionCateteresSondas.observaciones"),
																"observaciones",  type.stringType()).setTitleStyle(stl.style().setBackgroundColor(Color.LIGHT_GRAY).setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK)));
		TextColumnBuilder<String>     colUsuario = col.column(messageResource.getMessage("impresionHistoriaClinica.seccionCateteresSondas.usuario"),
															"usuario",  type.stringType()).setTitleStyle(stl.style().setBackgroundColor(Color.LIGHT_GRAY).setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK)));
		
		//Se Asignan las columnas estáticas
		cols[0]=colFechaInsercion.setWidth(40);
		cols[1]=colFechaRetiro.setWidth(40);
		cols[2]=colTipo.setWidth(80);
		cols[3]=colViaInsercion.setWidth(40);
		cols[4]=colCuraciones;
		cols[5]=colObservaciones;
		
		//Se Asignan las columnas del DataSource estáticas
		colsData[0]="fechainsercion";
		colsData[1]="fecharetiro";
		colsData[2]="tipo";
		colsData[3]="viainsercion";
		colsData[4]="curaciones";
		colsData[5]="observaciones";
		
		if(dto.getColumnasDinamicasCateteresSondas() != null && !dto.getColumnasDinamicasCateteresSondas().isEmpty()){
			//Construimos las columnas Dinámicas
			Iterator  iterator = dto.getColumnasDinamicasCateteresSondas().iterator();
			int i=0;
			while(iterator.hasNext()){
				HashMap element = (HashMap)iterator.next();
				String nombreCol=element.get("nombre").toString();
				TextColumnBuilder<String> colDinamica = col.column(nombreCol,
						nombreCol,  type.stringType()).setTitleStyle(stl.style().setBackgroundColor(Color.LIGHT_GRAY).setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK)));
				//Se Asigna la columna a las columnas del reporte
				cols[6+i]=colDinamica;
				colsData[6+i]=nombreCol;
				i++;
			}
			
		}
		cols[6+numColsDinamicas]=colUsuario.setWidth(40);
		colsData[6+numColsDinamicas]="usuario";
		subReportCateteresSondas.columns(cols)
		.setDetailSplitType(SplitType.PREVENT)
			.setDataSource(crearDataSourceCateteresSondas(dto.getCateteresSondas(), dto.getColumnasDinamicasCateteresSondas(), colsData))
			.setTemplate(generadorDisenioSubReportes.crearPlantillaReporte())
			.setPageMargin(generadorDisenioSubReportes.crearMagenesSubReporte())
			.build();
		return subReportCateteresSondas;
		
	}
	
	
	/**
	 * Método encargado de construir el Data Source del subreporte de
	 * la sección de Cateteres y Sondas 
	 * 
	 * @param mapaCateteresSondas
	 * @param columnasDinamicasCateteresSondas
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public JRDataSource crearDataSourceCateteresSondas(HashMap mapaCateteresSondas, Collection columnasDinamicasCateteresSondas, String[] columnasDataSource){
		DataSource dataSource = new DataSource(columnasDataSource);
		
		Integer tamanio=Utilidades.convertirAEntero(mapaCateteresSondas.get("numRegistros").toString());
		if (tamanio>0) {
			int numColsDinamicas=0;
			if(columnasDinamicasCateteresSondas != null && !columnasDinamicasCateteresSondas.isEmpty()){
				numColsDinamicas=columnasDinamicasCateteresSondas.size();
			}
			for (int i = 0; i <tamanio; i++) {
				String[] datos = new String[7+numColsDinamicas];
				datos[0]=String.valueOf(mapaCateteresSondas.get("fechaInsercion_"+i))+" "+String.valueOf(mapaCateteresSondas.get("horaInsercion_"+i));
				datos[1]=String.valueOf(mapaCateteresSondas.get("fechaRetiro_"+i))+" "+String.valueOf(mapaCateteresSondas.get("horaRetiro_"+i));
				datos[2]=String.valueOf(mapaCateteresSondas.get("nombreArticulo_"+i));
				datos[3]=String.valueOf(mapaCateteresSondas.get("viaInsercion_"+i));
				String curacionesStr=UtilidadTexto.observacionAHTML(String.valueOf(mapaCateteresSondas.get("curaciones_"+i)));
				datos[4]=curacionesStr.replaceAll("<br>", "\n");
				String observacionesStr=UtilidadTexto.observacionAHTML(String.valueOf(mapaCateteresSondas.get("observaciones_"+i)));
				datos[5]=observacionesStr.replaceAll("<br>", "\n");
				if(columnasDinamicasCateteresSondas != null && !columnasDinamicasCateteresSondas.isEmpty()){
					//Se obtienen los datos de las columnas Dinámicas
					Iterator  iterator = columnasDinamicasCateteresSondas.iterator();
					int j=0;
					while(iterator.hasNext()){
						HashMap element = (HashMap)iterator.next();
						String valorCol="";
						if(mapaCateteresSondas.get("valorParam_"+element.get("codigo_tipo")+"_"+i) != null){
							valorCol=String.valueOf(mapaCateteresSondas.get("valorParam_"+element.get("codigo_tipo")+"_"+i));
						}
						//Se Asigna el valor de la columna a los datos del reporte
						datos[6+j]=valorCol;
						j++;
					}
					
				}
				datos[6+numColsDinamicas]=String.valueOf(mapaCateteresSondas.get("nombre_usuario_"+i));
				dataSource.add(datos);
			}	
		}
		
		return dataSource;
	}

}
