package com.servinte.axioma.generadorReporte.historiaClinica;

import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;
import static net.sf.dynamicreports.report.builder.DynamicReports.col;
import static net.sf.dynamicreports.report.builder.DynamicReports.report;
import static net.sf.dynamicreports.report.builder.DynamicReports.stl;
import static net.sf.dynamicreports.report.builder.DynamicReports.type;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.builder.component.HorizontalListBuilder;
import net.sf.dynamicreports.report.builder.component.VerticalListBuilder;
import net.sf.dynamicreports.report.constant.HorizontalAlignment;
import net.sf.dynamicreports.report.constant.LineStyle;
import net.sf.dynamicreports.report.constant.SplitType;
import net.sf.dynamicreports.report.constant.VerticalAlignment;
import net.sf.jasperreports.engine.JRDataSource;

import org.apache.struts.util.MessageResources;

import util.UtilidadTexto;
import util.Utilidades;
import util.reportes.dinamico.DataSource;

import com.princetonsa.dto.manejoPaciente.DtoObservacionesGeneralesOrdenesMedicas;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;

public class GeneradorSubReporteProcedimientosOrdenes {


	/**
	 * Mensajes parametrizados de los reportes 
	 */
	private static MessageResources messageResource = MessageResources.getMessageResources("com.servinte.mensajes.historiaclinica.impresionHistoriaClinica");



	public GeneradorSubReporteProcedimientosOrdenes() {
	}



	public   JasperReportBuilder  ProcedimientosOrdenes(ArrayList<HashMap> listaProcedimientos
			,UsuarioBasico usuario, PersonaBasica paciente){
		JasperReportBuilder ordenesProcedimientos = report();

		ComponentBuilder<?, ?> componentBuilder = null;
		List<ComponentBuilder> listaComponentes = new ArrayList<ComponentBuilder>();
		ComponentBuilder[] componentesTotales;
		HorizontalListBuilder tituloSeccion = null;
		HorizontalListBuilder informacionSeccion = null;
		HorizontalListBuilder anulacionSeccion = null;
		HorizontalListBuilder cupsNombrePosSeccion = null;
		HorizontalListBuilder centroCostoEspecialidadSeccion = null;
		HorizontalListBuilder frecuenciaCantidadSeccion = null;
		HorizontalListBuilder observacionesSeccion = null;
		HorizontalListBuilder nombreUsuarioSeccion = null;
		String cantidad="";
		String frecuencia="";
		String datosMedico="";
		HashMap mapa = new HashMap();
		HashMap proc = listaProcedimientos.get(0);
		mapa = listaProcedimientos.get(0);
		GeneradorDisenioReporteHistoriaClinica disenio = new GeneradorDisenioReporteHistoriaClinica();

		TextColumnBuilder<String>     procedimientos = col.column("Procedimientos",  "procedimientos",  type.stringType()).setTitleStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK)));
		TextColumnBuilder<String>     urgente = col.column("Urg.",  "urgente",  type.stringType()).setHorizontalAlignment(HorizontalAlignment.CENTER).setTitleStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK)));
		TextColumnBuilder<String>     pos = col.column("Pos",  "pos",  type.stringType()).setHorizontalAlignment(HorizontalAlignment.CENTER).setTitleStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK)));
		TextColumnBuilder<String>     observaciones = col.column("Observaciones",  "observaciones",  type.stringType()).setTitleStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK)));




		if(!UtilidadTexto.isEmpty(String.valueOf(proc.get("fecha_0")))){
			datosMedico=String.valueOf(proc.get("fecha_0"));
		}

		if(!UtilidadTexto.isEmpty(String.valueOf(proc.get("hora_0")))){
			datosMedico+=" "+String.valueOf(proc.get("hora_0"));
		}


		if(!UtilidadTexto.isEmpty(String.valueOf(proc.get("medico_0")))){
			datosMedico+=" "+String.valueOf(proc.get("medico_0"));

		}



		Integer tamanio=Utilidades.convertirAEntero(String.valueOf(mapa.get("numRegistros")));

		ordenesProcedimientos
		.columns(procedimientos.setWidth(250),urgente.setWidth(20),pos.setWidth(20),observaciones.setWidth(80))
		.setTemplate(disenio.crearPlantillaReporte())
		.setDataSource(crearDatasourceOrdenesProcedimientos(listaProcedimientos))
		.setDetailSplitType(SplitType.PREVENT)
		.setPageMargin(disenio.crearMagenesSubReporte())
		.summary(cmp.text(datosMedico)
				.setStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.LEFT).setBackgroundColor(Color.WHITE).setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK))));

		ordenesProcedimientos.build();

		return ordenesProcedimientos;

	}

	public JRDataSource crearDatasourceOrdenesProcedimientos (ArrayList<HashMap>  proc){


		DataSource dataSource = new DataSource("procedimientos","urgente","pos","observaciones");

		for (int j = 0; j < proc.size(); j++) {
			HashMap mapa = new HashMap();
			mapa = proc.get(j);
			Integer tamanio=Utilidades.convertirAEntero(String.valueOf(mapa.get("numRegistros")));
			for (int i = 0; i < tamanio; i++) {
				dataSource.add(String.valueOf(mapa.get("codigocups_"+i))+" - "+String.valueOf(mapa.get("servicio_"+i)),
						String.valueOf(mapa.get("urgente_"+i)),
						String.valueOf(mapa.get("pos_"+i)),
						String.valueOf(mapa.get("observaciones_"+i)));
			}
		}




		return dataSource;
	}



	public  List<JasperReportBuilder> procedimientosOrdenesTotales(ArrayList<HashMap> proc
			,UsuarioBasico usuario, PersonaBasica paciente, JasperReportBuilder reportFormatoHc){
		List<JasperReportBuilder> subreports = new ArrayList<JasperReportBuilder>();

		JasperReportBuilder jasperReport=ProcedimientosOrdenes(proc, usuario, paciente);
		
		reportFormatoHc.summary(cmp.subreport(jasperReport));

		// se adiciona espacio entre subreportes 
		reportFormatoHc.summary(cmp.text("").setHeight(5));

		return subreports;
	}




	public   JasperReportBuilder  ordenesMedicasMedicoResponsable(HashMap orden
			,UsuarioBasico usuario, PersonaBasica paciente,Integer hayDatosOrdenesMedicas){
		JasperReportBuilder ordenesProcedimientos = report();

		ComponentBuilder<?, ?> componentBuilder = null;
		ComponentBuilder<?, ?> componentBuilder2 = null;
		List<ComponentBuilder> listaComponentes = new ArrayList<ComponentBuilder>();
		ComponentBuilder[] componentesTotales;
		HorizontalListBuilder anulacionSeccion = null;
		HorizontalListBuilder cupsNombrePosSeccion = null;
		HorizontalListBuilder centroCostoEspecialidadSeccion = null;
		HorizontalListBuilder frecuenciaCantidadSeccion = null;
		VerticalListBuilder observacionesSeccion = null;
		HorizontalListBuilder nombreUsuarioSeccion = null;
		GeneradorDisenioReporteHistoriaClinica disenio = new GeneradorDisenioReporteHistoriaClinica();

		if(!UtilidadTexto.isEmpty(String.valueOf(orden.get("datos_medico_0")))
				&& hayDatosOrdenesMedicas>0){
			ordenesProcedimientos.summary(cmp.text(String.valueOf(orden.get("datos_medico_0")))
					.setStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.LEFT).setBackgroundColor(Color.WHITE).setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK))));
		}

		ordenesProcedimientos
		.setTemplate(disenio.crearPlantillaReporte())
		.setPageMargin(disenio.crearMagenesNulasSubReporte())
		.build();


		return ordenesProcedimientos;
	}







	public   JasperReportBuilder  ordenesMedicasHojaObservaciones(HashMap orden
			,UsuarioBasico usuario, PersonaBasica paciente,Integer hayDatosOrdenesMedicas,DtoResultadoImpresionHistoriaClinica dto
			,List<DtoObservacionesGeneralesOrdenesMedicas> listaObservacion, boolean observHojaNeurologicaSel){
		JasperReportBuilder ordenesProcedimientos = report();

		ComponentBuilder<?, ?> componentBuilder = null;
		ComponentBuilder<?, ?> componentBuilder2 = null;
		List<ComponentBuilder> listaComponentes = new ArrayList<ComponentBuilder>();
		ComponentBuilder[] componentesTotales;
		HorizontalListBuilder anulacionSeccion = null;
		HorizontalListBuilder cupsNombrePosSeccion = null;
		HorizontalListBuilder centroCostoEspecialidadSeccion = null;
		HorizontalListBuilder frecuenciaCantidadSeccion = null;
		VerticalListBuilder observacionesSeccion = null;
		HorizontalListBuilder nombreUsuarioSeccion = null;
		String cantidad="";
		String frecuencia="";
		HashMap mapa = new HashMap();
		mapa = orden;
		GeneradorDisenioReporteHistoriaClinica disenio = new GeneradorDisenioReporteHistoriaClinica();

		TextColumnBuilder<String>     hojaNeurologica = col.column("Hoja Neurológica",  "hojaNeurologica",  type.stringType()).setHorizontalAlignment(HorizontalAlignment.CENTER).setTitleStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK)));
		TextColumnBuilder<String>     observaciones = col.column("Observaciones",  "observaciones",  type.stringType()).setTitleStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK)));
		TextColumnBuilder<String>     suspencion = col.column("Suspension",  "suspencion",  type.stringType()).setHorizontalAlignment(HorizontalAlignment.CENTER).setTitleStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK)));


		observacionesSeccion=cmp.verticalList(cmp.text(" "))	;

		String comentsGen="";
//		for (int i = 0; i <observacionesOrdenMedica.size(); i++) {
//			if (mapa.get("observacionesgenerales_"+i)!=null && !String.valueOf(mapa.get("observacionesgenerales_"+i)).equals("")) {
//				comentsGen+= mapa.get("observacionesgenerales_"+i)+"\n";
//			}
//
//		}
		
		
		
		
		for (int i = 0; i < listaObservacion.size(); i++) {
			if(listaObservacion.get(i)!=null){
				comentsGen +="\n";
				if(listaObservacion.get(i).getFechaGeneracion()!=null){
					comentsGen +="\n"+listaObservacion.get(i).getFechaGeneracion();
				}
				
				if(listaObservacion.get(i).getFechaOrden()!=null){
					comentsGen +="\n"+listaObservacion.get(i).getFechaOrden();
				}
				
				if(listaObservacion.get(i).getObservaciones()!=null){
					comentsGen+="\n"+listaObservacion.get(i).getObservaciones();
				}
				
				if(listaObservacion.get(i).getDatosMedico()!=null){
					comentsGen +="\n"+listaObservacion.get(i).getDatosMedico();
				}
				
			
			}
		}
		
		
		
		
		
		
//		
//		comentsGen = comentsGen.replace("\r\n", "\n\n");
//		String []Observaciones2 = comentsGen.split("\n\n");
//		List<String> listaObs = new ArrayList<String>();
//		String obsGrupal="";
//		
//		for (int i = 0; i < Observaciones2.length; i++) {
//			obsGrupal+=Observaciones2[i]+"\n";
//			if(Observaciones2[i].contains("R.M")){
//				listaObs.add(obsGrupal);
//				obsGrupal="";
//			}
//		}
//		
//
//
//		
//		comentsGen="";
//		for (int i = 0; i < listaObs.size(); i++) {
//			if(listaObs.get(i).contains("Fecha Orden: "+dto.getFecha()+" - "+dto.getHora())){
//				comentsGen+=listaObs.get(i)+"\n";
//			}
//		}
		
		
		
		if (!comentsGen.equals("")) {
			observacionesSeccion=cmp.verticalList(cmp.text(" "),
					cmp.text("Observaciones Generales").setHorizontalAlignment(HorizontalAlignment.LEFT).setStyle( stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK))),
					cmp.text(comentsGen).setHorizontalAlignment(HorizontalAlignment.LEFT).setStyle(stl.style().setFontSize(7).setForegroudColor(Color.BLACK). setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK)))
			);
		}



		componentBuilder2 = cmp.verticalList(observacionesSeccion);
		ordenesProcedimientos
		.setTemplate(disenio.crearPlantillaReporte())
		;

		
		if (!String.valueOf(mapa.get("observacioneshoja_"+0)).equals("") && !observHojaNeurologicaSel) {

			/**
			 * MT 7550
			 * @author leoquico
			 * @fecha 31/07/2013
			 * @descripcion No muestra mas de una observacion general de ordenes medicas
			 */
			if(listaObservacion.isEmpty()) {
				ordenesProcedimientos
				.columns(hojaNeurologica,observaciones,suspencion)
				.setDataSource(crearDatasourcehojaObstetrica(orden))
				.setDetailSplitType(SplitType.PREVENT);	
				
			}
			
		}

		if (!comentsGen.equals("")) {
			ordenesProcedimientos
			.summary(componentBuilder2)
			//	.setPageMargin(disenio.crearMagenesSubReporte())
			.setSummarySplitType(SplitType.STRETCH);
		}



		ordenesProcedimientos
		.setPageMargin(disenio.crearMagenesSubReporte())
		.setTemplate(disenio.crearPlantillaReporte())
//		.summary(cmp.subreport(ordenesMedicasMedicoResponsable(orden, usuario, paciente,hayDatosOrdenesMedicas)))
		.build();


		return ordenesProcedimientos;
	}

	public JRDataSource crearDatasourcehojaObstetrica (HashMap orden){


		DataSource dataSource = new DataSource("hojaNeurologica","observaciones","suspencion");
		String res = "No";
		HashMap mapa = new HashMap();
		mapa = orden;

		if (!String.valueOf(mapa.get("observacioneshoja_"+0)).equals("")) {


			if (!String.valueOf(mapa.get("fechasuspencionhoja_"+0)).equals("")) {
				res="Si";
			}

			dataSource.add("Si",
					String.valueOf(mapa.get("observacioneshoja_"+0)),
					res);

		}

		return dataSource; 
	}


}
