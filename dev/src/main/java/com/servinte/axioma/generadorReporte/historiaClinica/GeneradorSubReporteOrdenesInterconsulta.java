package com.servinte.axioma.generadorReporte.historiaClinica;

import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;
import static net.sf.dynamicreports.report.builder.DynamicReports.col;
import static net.sf.dynamicreports.report.builder.DynamicReports.margin;
import static net.sf.dynamicreports.report.builder.DynamicReports.report;
import static net.sf.dynamicreports.report.builder.DynamicReports.stl;
import static net.sf.dynamicreports.report.builder.DynamicReports.type;

import java.awt.Color;
import java.sql.Connection;
import java.util.HashMap;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.builder.MarginBuilder;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
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

import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;

public class GeneradorSubReporteOrdenesInterconsulta {
	/**
	 * Mensajes parametrizados de los reportes 
	 */
	private static MessageResources messageResource = MessageResources.getMessageResources("com.servinte.mensajes.historiaclinica.impresionHistoriaClinica");

	private String datosMedico="";

	public GeneradorSubReporteOrdenesInterconsulta() {
	}


	public   JasperReportBuilder  ordenesInterconsulta(HashMap inter
			,UsuarioBasico usuario, PersonaBasica paciente,Integer numeroSolciitud){

		JasperReportBuilder ordenesInterconsulta = report();
		ComponentBuilder<?, ?> componentBuilder = null;
		HorizontalListBuilder tituloSeccion = null;
		GeneradorDisenoSubReportes generadorDiseñoSubReportes = new GeneradorDisenoSubReportes();
		Connection con = null;


		
		TextColumnBuilder<String>     interconsulta = col.column("Interconsulta",  "interconsulta",  type.stringType()).setTitleStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK)));
		TextColumnBuilder<String>     opcionManejo = col.column("Opción Manejo",  "opcionManejo",  type.stringType()).setTitleStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK)));
		TextColumnBuilder<String>     pos = col.column("Pos",  "pos",  type.stringType()).setTitleStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK)));
		TextColumnBuilder<String>     urgente = col.column("Urgente",  "urgente",  type.stringType()).setTitleStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK)));
		TextColumnBuilder<String>     observacion = col.column("Observaciones",  "observacion",  type.stringType()).setTitleStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK)));

		ordenesInterconsulta
		.columns(interconsulta.setWidth(160),
				opcionManejo,
				pos.setWidth(50).setHorizontalAlignment(HorizontalAlignment.CENTER),
				urgente.setWidth(50).setHorizontalAlignment(HorizontalAlignment.CENTER),
				observacion)
		.setDataSource(crearDataSourceOrdenesInterConsulta(inter))
		.summary(cmp.text(this.datosMedico)
					.setStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.LEFT).setBackgroundColor(Color.WHITE).setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK))))
		.setTemplate(generadorDiseñoSubReportes.crearPlantillaReporte())
		.setDetailSplitType(SplitType.PREVENT)
		.setPageMargin(crearMagenesSubReporte())
		.build();
		return ordenesInterconsulta;
	}

	public MarginBuilder crearMagenesSubReporte()
	{
		MarginBuilder margin;
		margin = margin()
		.setTop(0)
		.setBottom(0)
		.setLeft(0)
		.setRight(0)
		;

		return margin;
	}
	
	public JRDataSource crearDataSourceOrdenesInterConsulta(HashMap inter){

		DataSource dataSource = new DataSource("interconsulta","opcionManejo","pos","urgente","observacion");
		HashMap mapa = new HashMap();
		mapa = inter;
		Integer tamanio=Utilidades.convertirAEntero(String.valueOf(mapa.get("numRegistros")));
		this.datosMedico="";
		for (int i = 0; i < tamanio; i++) {
			dataSource.add(String.valueOf(mapa.get("codigocups_"+i))+" "+String.valueOf(mapa.get("servicio_"+i)),
					String.valueOf(mapa.get("manejo_"+i)),
					String.valueOf(mapa.get("pos_"+i)),
					String.valueOf(mapa.get("urgente_"+i)),
					String.valueOf(mapa.get("observaciones_"+i))
			);
			//if(!UtilidadTexto.isEmpty(String.valueOf(mapa.get("observaciones_"+i)))){
			setDatosMedico(obtenerDatosMedicos(mapa,i));
			//}
		}
		return dataSource;
	}

	public String obtenerDatosMedicos(HashMap inter,int posicion){
		/*String res ="";
		String[] datosMedico=observaciones.split("\n");
		if(datosMedico[0]!=null && datosMedico[2]!=null ){
			res=datosMedico[0]+" "+datosMedico[2];
		}*/
		String datosMedico="";
		if(!UtilidadTexto.isEmpty(String.valueOf(inter.get("fecha_"+posicion)))){
			datosMedico=String.valueOf(inter.get("fecha_"+posicion));
		}
		
		if(!UtilidadTexto.isEmpty(String.valueOf(inter.get("hora_"+posicion)))){
			datosMedico+=" "+String.valueOf(inter.get("hora_"+posicion));
		}


		if(!UtilidadTexto.isEmpty(String.valueOf(inter.get("medico_"+posicion)))){
			datosMedico+=" "+String.valueOf(inter.get("medico_"+posicion));

		}
		
		return datosMedico;
	}
	public String obtenerObservacionesDataSource(String observaciones){
		
		String obs ="";
		String[] splitObs= observaciones.split("\n");
		if (splitObs.length==3) {
			obs=splitObs[1];
		}else if(splitObs.length==2){
			obs="";
		}else if(splitObs.length>3){
			obs=splitObs[1];
			for (int i = 2; i < splitObs.length-1; i++) {
				obs+="\n"+splitObs[i];
			}
		}
		
		return obs;
		
		
	}


	/**
	 * @return the datosMedico
	 */
	public String getDatosMedico() {
		return datosMedico;
	}


	/**
	 * @param datosMedico the datosMedico to set
	 */
	public void setDatosMedico(String datosMedico) {
		this.datosMedico = datosMedico;
	}


}
