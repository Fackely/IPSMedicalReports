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
 * Clase que maneja la construcción del subreporte de la sección de Cuidados especiales
 * 
 * @author Ricardo Ruiz
 *
 */
public class GeneradorSubReporteCuidadosEspecialesHC {

	/**
	 * Mensajes parametrizados del reporte.
	 */
	private static MessageResources messageResource = MessageResources.getMessageResources("com.servinte.mensajes.historiaclinica.impresionHistoriaClinica");
	
	/**
	 * C
	 */
	public GeneradorSubReporteCuidadosEspecialesHC() {

	}
	
	
	/**
	 * Método encargado de construir el subreporte de la sección Cuidados Especiales
	 * @param dto
	 * @param usuario
	 * @param paciente
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public JasperReportBuilder generarReporteSeccionCuidadosEspeciales(DtoImpresionHistoriaClinica dto
			,UsuarioBasico usuario, PersonaBasica paciente){
		
		JasperReportBuilder subReportCuidadosEspeciales = report();
		
		//Columnas del reporte 
		TextColumnBuilder []cols = new TextColumnBuilder[3];
		String []colsData = new String[3];
		GeneradorDisenoSubReportes generadorDisenioSubReportes = new GeneradorDisenoSubReportes();
		
		//Componente del Encabezado del Subreporte o titulo de la sección
		HorizontalListBuilder tituloSeccion=	cmp.horizontalList(cmp.verticalList(cmp.horizontalList(
				cmp.text(messageResource.getMessage("impresionHistoriaClinica.seccionCuidadosEspeciales.titulo"))
					.setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloTituloUnderline).underline()
					.setHorizontalAlignment(HorizontalAlignment.LEFT))
		)))	;
		
		
		subReportCuidadosEspeciales
		.summary(cmp.horizontalList(tituloSeccion))
		.summary(cmp.subreport(generarReporteSeccionCuidadosEspecialesDetail(dto, usuario, paciente)))
		.setTemplate(generadorDisenioSubReportes.crearPlantillaReporte())
		.setDetailSplitType(SplitType.STRETCH)
		.setPageMargin(generadorDisenioSubReportes.crearMagenesReporte())
		.build();
		return subReportCuidadosEspeciales;
		
	}
	
	
	/**
	 * Método encargado de construir el subreporte de la sección Cuidados Especiales
	 * @param dto
	 * @param usuario
	 * @param paciente
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public JasperReportBuilder generarReporteSeccionCuidadosEspecialesDetail(DtoImpresionHistoriaClinica dto
			,UsuarioBasico usuario, PersonaBasica paciente){
		
		JasperReportBuilder subReportCuidadosEspeciales = report();
		
		//Columnas del reporte 
		TextColumnBuilder []cols = new TextColumnBuilder[3];
		String []colsData = new String[3];
		GeneradorDisenoSubReportes generadorDisenioSubReportes = new GeneradorDisenoSubReportes();
		
		TextColumnBuilder<String>     colFechaHora = col.column(messageResource.getMessage("impresionHistoriaClinica.seccionCuidadosEspeciales.fechaHora"),
																"fechahora",  type.stringType()).setTitleStyle(stl.style().setBackgroundColor(Color.LIGHT_GRAY).setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK)));
		TextColumnBuilder<String>      colCuidadosControles = col.column(messageResource.getMessage("impresionHistoriaClinica.seccionCuidadosEspeciales.cuidadosControles"),
																"cuidadoscontroles", type.stringType()).setTitleStyle(stl.style().setBackgroundColor(Color.LIGHT_GRAY).setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK)));
		TextColumnBuilder<String>     colUsuario = col.column(messageResource.getMessage("impresionHistoriaClinica.seccionCuidadosEspeciales.usuario"),
															"usuario",  type.stringType()).setTitleStyle(stl.style().setBackgroundColor(Color.LIGHT_GRAY).setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK)));
		
		//Se Asignan las columnas estáticas
		cols[0]=colFechaHora.setWidth(40);
		cols[1]=colCuidadosControles;
		cols[2]=colUsuario.setWidth(40);
		
		//Se Asignan las columnas del DataSource
		colsData[0]="fechahora";
		colsData[1]="cuidadoscontroles";
		colsData[2]="usuario";
		
		subReportCuidadosEspeciales.columns(cols)
			.setDataSource(crearDataSourceCuidadosEspeciales(dto.getCuidadosEspeciales(), colsData))
			.setDetailSplitType(SplitType.PREVENT)
			.setTemplate(generadorDisenioSubReportes.crearPlantillaReporte())
			.setPageMargin(generadorDisenioSubReportes.crearMagenesSubReporte())
			.build();
		return subReportCuidadosEspeciales;
		
	}
	
	
	
	/**
	 * Método encargado de construir el Data Source del subreporte de
	 * la sección de Cuidados Especiales 
	 * 
	 * @param mapaCuidadosEspeciales
	 * @param columnasDataSource
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public JRDataSource crearDataSourceCuidadosEspeciales(HashMap mapaCuidadosEspeciales, String[] columnasDataSource){
		DataSource dataSource = new DataSource(columnasDataSource);
		
		Integer tamanio=Utilidades.convertirAEntero(mapaCuidadosEspeciales.get("numRegistros").toString());
		if (tamanio>0) {
			for (int i = 0; i <tamanio; i++) {
				Object[] datos = new Object[3];
				datos[0]=String.valueOf(mapaCuidadosEspeciales.get("fecha_registro_"+i))+" "+String.valueOf(mapaCuidadosEspeciales.get("hora_registro_"+i));
				int numRegDet=0;
				numRegDet=Utilidades.convertirAEntero(String.valueOf(mapaCuidadosEspeciales.get("numRegEnca_"+mapaCuidadosEspeciales.get("codigo_enca_"+i))));
				if(numRegDet>0){
					for(int j=0;j<numRegDet;j++){
						String dataDetail= "";
						if(UtilidadTexto.getBoolean(String.valueOf(mapaCuidadosEspeciales.get("controlespecial_"+mapaCuidadosEspeciales.get("codigo_enca_"+i)+"_"+j))))
						{
							dataDetail+=messageResource.getMessage("impresionHistoriaClinica.seccionCuidadosEspeciales.controlEspecial")+":  ";
						}
						else{
							dataDetail+=messageResource.getMessage("impresionHistoriaClinica.seccionCuidadosEspeciales.procedimiento")+":  ";
						}
						dataDetail+=mapaCuidadosEspeciales.get("nombre_cuidado_"+mapaCuidadosEspeciales.get("codigo_enca_"+i)+"_"+j)+" ";
						dataDetail+=mapaCuidadosEspeciales.get("presenta_"+mapaCuidadosEspeciales.get("codigo_enca_"+i)+"_"+j);
						if(String.valueOf(mapaCuidadosEspeciales.get("observaciones_"+mapaCuidadosEspeciales.get("codigo_enca_"+i)+"_"+j)).split("\n").length>0
								&& !String.valueOf(mapaCuidadosEspeciales.get("observaciones_"+mapaCuidadosEspeciales.get("codigo_enca_"+i)+"_"+j)).split("\n")[0].equals("")){
							dataDetail+="- "+String.valueOf(mapaCuidadosEspeciales.get("observaciones_"+mapaCuidadosEspeciales.get("codigo_enca_"+i)+"_"+j)).split("\n")[0]+" ";
						}
						
						datos[1]=dataDetail;
						datos[2]=String.valueOf(mapaCuidadosEspeciales.get("nombre_usuario_"+i));
						dataSource.add(datos);
					}
				}
				
			}	
		}
		
		return dataSource;
	}

}
