package com.servinte.axioma.generadorReporte.historiaClinica;

import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;
import static net.sf.dynamicreports.report.builder.DynamicReports.col;
import static net.sf.dynamicreports.report.builder.DynamicReports.margin;
import static net.sf.dynamicreports.report.builder.DynamicReports.report;
import static net.sf.dynamicreports.report.builder.DynamicReports.stl;
import static net.sf.dynamicreports.report.builder.DynamicReports.type;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
import util.reportes.dinamico.EstilosReportesDinamicosHistoriaClinica;

import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;

public class GeneradorSubReporteOrdenesMedicamentos {

	/**
	 * Mensajes parametrizados de los reportes 
	 */
	private static MessageResources messageResource = MessageResources.getMessageResources("com.servinte.mensajes.historiaclinica.impresionHistoriaClinica");

	private String  valorInsumos;

	public GeneradorSubReporteOrdenesMedicamentos() {
		this.valorInsumos="";
	}




	public   JasperReportBuilder  ordenesMedicamentos(HashMap med
			,UsuarioBasico usuario, PersonaBasica paciente,Integer i){
		JasperReportBuilder ordenesMedicamentos = report();
		ComponentBuilder<?, ?> componentBuilder = null;
		ComponentBuilder[] componentesTotales = null;
		List<ComponentBuilder> listaComponentes = new ArrayList<ComponentBuilder>();
		HorizontalListBuilder tituloSeccion = null;
		HorizontalListBuilder informacionSeccion = null;
		HorizontalListBuilder fechaAnulacionSeccion = null;
		HorizontalListBuilder tituloMedicamentosSeccion = null;
		TextColumnBuilder []cols = new TextColumnBuilder[6];
		HashMap mapa = new HashMap();
		mapa = med;
		GeneradorDisenoSubReportes generadorDisenoSubReportes = new GeneradorDisenoSubReportes();

		TextColumnBuilder<String>     fechaHora = col.column("Fecha",  "fechaHora",  type.stringType()).setTitleStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK)));
		TextColumnBuilder<String>     medicamento= col.column("Medicamentos",  "medicamento",  type.stringType()).setTitleStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK)));
		TextColumnBuilder<String>     dosis = col.column("Dosis",  "dosis",  type.stringType()).setHorizontalAlignment(HorizontalAlignment.CENTER).setTitleStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK)));
		TextColumnBuilder<String>     frecuencia = col.column("Frecuencia",  "frecuencia",  type.stringType()).setHorizontalAlignment(HorizontalAlignment.CENTER).setTitleStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK)));
		TextColumnBuilder<String>     via = col.column("Vía",  "via",  type.stringType()).setHorizontalAlignment(HorizontalAlignment.CENTER).setTitleStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK)));
		TextColumnBuilder<String>     cantidad = col.column("Cantidad",  "cantidad",  type.stringType()).setHorizontalAlignment(HorizontalAlignment.CENTER).setTitleStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK)));
		TextColumnBuilder<String>     observacion = col.column("Observaciones",  "observacion",  type.stringType()).setTitleStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK)));



		ordenesMedicamentos
		.columns(fechaHora.setWidth(100),medicamento.setWidth(150),dosis.setWidth(50),frecuencia.setWidth(50),via.setWidth(50),cantidad.setWidth(50),observacion)
		.setDataSource(crearDatasourceOrdenesMedicas((HashMap) med.get("medicamentos_"+i),med,i))
		.setTemplate(generadorDisenoSubReportes.crearPlantillaReporte())
		.setPageMargin(crearMagenesSubReporte())
		.setDetailSplitType(SplitType.PREVENT)
		//.summary(cmp.subreport(valorPos(med, usuario, paciente, i)))
		.build();

		return ordenesMedicamentos;
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

	public   JasperReportBuilder  valorPos(HashMap med
			,UsuarioBasico usuario, PersonaBasica paciente,Integer i){
		JasperReportBuilder mezclaReport = report();
		ComponentBuilder<?, ?> componentBuilder = null;
		ComponentBuilder<?, ?> componentBuilderFooter = null;
		ComponentBuilder[] componentesTotales;
		List<ComponentBuilder> listaComponentes = new ArrayList<ComponentBuilder>();
		HorizontalListBuilder tituloSeccion = null;
		HorizontalListBuilder observacionesSeccion = null;
		TextColumnBuilder []cols = new TextColumnBuilder[6];
		HashMap mapa = new HashMap();
		GeneradorDisenoSubReportes generadorDisenoSubReportes = new GeneradorDisenoSubReportes();
		String comentsGen="";

		if (!valorInsumos.trim().equals("")) {
			tituloSeccion=	cmp.horizontalList(cmp.verticalList(cmp.horizontalList(
					cmp.text(this.valorInsumos).setStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.LEFT).setBackgroundColor(Color.WHITE).setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK)))
			)))	;
			componentBuilder = cmp.verticalList(tituloSeccion);
			mezclaReport
			.summary(componentBuilder)
			.summary(cmp.subreport(valorPosDetail(med, usuario, paciente, i)))
			.setTemplate(generadorDisenoSubReportes.crearPlantillaReporte())
			.setPageMargin(generadorDisenoSubReportes.crearMagenesSubReporteVacias())
			.build();
		}else{
			mezclaReport
			.summary(cmp.subreport(valorPosDetail(med, usuario, paciente, i)))
			.setTemplate(generadorDisenoSubReportes.crearPlantillaReporte())
			.setPageMargin(generadorDisenoSubReportes.crearMagenesSubReporteVacias())
			.build();
		}
		this.valorInsumos=""; 

		return mezclaReport;
	}


	public   JasperReportBuilder  valorPosDetail(HashMap med
			,UsuarioBasico usuario, PersonaBasica paciente,Integer i){
		JasperReportBuilder mezclaReport = report();
		ComponentBuilder<?, ?> componentBuilder = null;
		ComponentBuilder<?, ?> componentBuilderFooter = null;
		ComponentBuilder[] componentesTotales;
		List<ComponentBuilder> listaComponentes = new ArrayList<ComponentBuilder>();
		HorizontalListBuilder tituloSeccion = null;
		HorizontalListBuilder observacionesSeccion = null;
		TextColumnBuilder []cols = new TextColumnBuilder[6];
		HashMap mapa = new HashMap();
		GeneradorDisenoSubReportes generadorDisenoSubReportes = new GeneradorDisenoSubReportes();
		String comentsGen="";


		if(!UtilidadTexto.isEmpty(String.valueOf(med.get("medico_0")))){
			comentsGen=String.valueOf(med.get("medico_0"));
		}

		if (comentsGen.length()>4 && comentsGen.substring(0,2).equals("\n\n")) {
			comentsGen=comentsGen.substring(2,comentsGen.length());
		}


		mezclaReport
		.setTemplate(generadorDisenoSubReportes.crearPlantillaReporte())
		.setPageMargin(generadorDisenoSubReportes.crearMagenesSubReporteVacias())
		.summary(cmp.subreport(ordenesMedicamentosInsumos(med, usuario, paciente, i)))
		.summary(cmp.text(comentsGen)
				.setStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.LEFT).setBackgroundColor(Color.WHITE).setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK))))
				.build();
		return mezclaReport;
	}








	public JRDataSource crearDatasourceOrdenesMedicas (HashMap mapa,HashMap med,Integer j){


		DataSource dataSource = new DataSource("fechaHora","medicamento","dosis","frecuencia","via","cantidad","observacion");
		Integer tama=Utilidades.convertirAEntero(String.valueOf(mapa.get("numRegistros")));
		HashMap mapaOrdenesMedicamentos=med;
		String res = "";
		for (int i = 0; i < tama; i++) {
		String naturaleza = 	mapa.get("naturaleza_"+i).toString();
		Integer codigoArticulo=	Integer.valueOf(mapa.get("articulo_"+i).toString());
		int codigoInstitucion=		Integer.valueOf(mapaOrdenesMedicamentos.get("codigoInstitucion").toString());
		String codigoInterfaz=	 mapa.get("codigo_interfaz_"+i).toString();
		String codigoCum =	 mapa.get("codigocum_"+i).toString();
			
		String presentacionCUM  = Utilidades.getPresentacionCodigoArticulo(naturaleza, codigoArticulo, codigoInstitucion, codigoInterfaz, codigoCum);
			dataSource.add(String.valueOf(mapaOrdenesMedicamentos.get("fecha_"+j))+" "+String.valueOf(mapaOrdenesMedicamentos.get("hora_"+j)),
					String.valueOf(presentacionCUM)+"-"+String.valueOf(mapa.get("medicamento_"+i)),
					String.valueOf(mapa.get("dosis_"+i))+" " +String.valueOf(mapa.get("unidadmedida_"+i)),
					String.valueOf(mapa.get("frecuencia_"+i)),
					String.valueOf(mapa.get("via_"+i)),
					String.valueOf(mapa.get("cantidad_"+i)),
					String.valueOf(mapa.get("observaciones_"+i)));

			res = obtenerJustificacionNoPos(mapa, i);
		}

		String obsGenerales= String.valueOf(mapaOrdenesMedicamentos.get("obsgenerales_"+j));

		if (obsGenerales!=null) {
			this.valorInsumos=res+" "+obsGenerales;
		}else{
			this.valorInsumos=res;
		}
		return dataSource;
	}


















	public JRDataSource crearDatasourceOrdenesMedicamentos (HashMap medicamentos){
		Integer tamano=Utilidades.convertirAEntero(String.valueOf(medicamentos.get("numRegistros")));
		DataSource dataSource = new DataSource("medicamento","dosis","frecuencia","via","cantidad","observacion","justifiacion","fechaSuspencion");
		if (tamano>0) {
			for (int k = 0; k < tamano; k++) {
				dataSource.add(String.valueOf(medicamentos.get("articulo_"+k))+"-"+String.valueOf(medicamentos.get("medicamento_"+k)),
						String.valueOf(medicamentos.get("dosis_"+k))+" " +String.valueOf(medicamentos.get("unidadmedida_"+k)),
						String.valueOf(medicamentos.get("frecuencia_"+k)),
						String.valueOf(medicamentos.get("via_"+k)),
						String.valueOf(medicamentos.get("cantidad_"+k)),
						String.valueOf(medicamentos.get("observaciones_"+k)),
						obtenerJustificacionNoPos(medicamentos, k),
						obtenerFechaSuspension(medicamentos, k)
				);
			}
		}
		return dataSource;
	}


	public String obtenerJustificacionNoPos(HashMap medicamentos,Integer k){
		String res = "";
		if(medicamentos.containsKey("atributosnopos_"+k)){
			HashMap atributos=(HashMap)medicamentos.get("atributosnopos_"+k);
			res = "JUSTIFICACION NO POS. "+medicamentos.get("medicamento_"+k)+"\n";
			for(int j=0;j<Utilidades.convertirAEntero(String.valueOf(atributos.get("numRegistros")));j++){
				if(!(String.valueOf(atributos.get("valor_"+j))).trim().equals(""))
				{
					res+="* "+String.valueOf(atributos.get("atributo_"+j))+": "+String.valueOf(atributos.get("valor_"+j))+"\n";
				}
			}
		}

		return res;

	}

	public String obtenerFechaSuspension(HashMap medicamentos,Integer k){
		String res = "";
		if(!(String.valueOf(medicamentos.get("fechasuspencion_"+k))).trim().equals("")){
			res = String.valueOf(medicamentos.get("fechasuspencion_"+k));
		}
		return res;
	}



	public   JasperReportBuilder  ordenesMedicamentosInsumos(HashMap med
			,UsuarioBasico usuario, PersonaBasica paciente,Integer   i){
		JasperReportBuilder ordenesMedicamentosInsumos = report();
		ComponentBuilder<?, ?> componentBuilder = null;
		ComponentBuilder[] componentesTotales;
		List<ComponentBuilder> listaComponentes = new ArrayList<ComponentBuilder>();
		HorizontalListBuilder tituloSeccion = null;
		HorizontalListBuilder informacionSeccion = null;
		HorizontalListBuilder fechaAnulacionSeccion = null;
		HorizontalListBuilder tituloMedicamentosSeccion = null;
		TextColumnBuilder []cols = new TextColumnBuilder[6];
		HashMap mapa = new HashMap();
		mapa = med;
		HashMap insumos=(HashMap)mapa.get("insumos_"+i);
		GeneradorDisenoSubReportes generadorDisenoSubReportes = new GeneradorDisenoSubReportes();

		tituloSeccion=	cmp.horizontalList(cmp.verticalList(cmp.horizontalList(
				cmp.text(messageResource.getMessage("historia_clinica_lable_subtituloMedicamentosInsumos")).setStyle(EstilosReportesDinamicosHistoriaClinica.estiloBordeNegrilla)
		)))	;
		listaComponentes.add(tituloSeccion);

		componentesTotales= new ComponentBuilder[listaComponentes.size()];
		for (int j = 0; j <listaComponentes.size(); j++) {
			componentesTotales[j]=listaComponentes.get(j);
		}
		componentBuilder = cmp.verticalList(componentesTotales);

		componentBuilder = cmp.verticalList(componentBuilder);
		ordenesMedicamentosInsumos
		.summary(componentBuilder)
		.summary(cmp.subreport(ordenesMedicamentosInsumosDetail(med, usuario, paciente, i)))
		.setTemplate(generadorDisenoSubReportes.crearPlantillaReporte())
		.setPageMargin(generadorDisenoSubReportes.crearMagenesSubReporteVacias())
		.build();

		return ordenesMedicamentosInsumos;
	}



	public   JasperReportBuilder  ordenesMedicamentosInsumosDetail(HashMap med
			,UsuarioBasico usuario, PersonaBasica paciente,Integer   i){
		JasperReportBuilder ordenesMedicamentosInsumos = report();
		ComponentBuilder<?, ?> componentBuilder = null;
		ComponentBuilder[] componentesTotales;
		List<ComponentBuilder> listaComponentes = new ArrayList<ComponentBuilder>();
		HorizontalListBuilder tituloSeccion = null;
		HorizontalListBuilder informacionSeccion = null;
		HorizontalListBuilder fechaAnulacionSeccion = null;
		HorizontalListBuilder tituloMedicamentosSeccion = null;
		TextColumnBuilder []cols = new TextColumnBuilder[6];
		HashMap mapa = new HashMap();
		mapa = med;
		HashMap insumos=(HashMap)mapa.get("insumos_"+i);
		GeneradorDisenoSubReportes generadorDisenoSubReportes = new GeneradorDisenoSubReportes();


		TextColumnBuilder<String>     articulo = col.column("Insumos",  "articulo",  type.stringType()).setTitleStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK)));
		TextColumnBuilder<String>     cantidad = col.column("Cantidad",  "cantidad",  type.stringType()).setTitleStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK)));

		ordenesMedicamentosInsumos
		.columns(articulo,cantidad)
		.setDataSource(crearDatasourceOrdenesMedicamentosInsumos((HashMap) med.get("insumos_"+i)))
		.setTemplate(generadorDisenoSubReportes.crearPlantillaReporte())
		.setDetailSplitType(SplitType.PREVENT)
		.setPageMargin(generadorDisenoSubReportes.crearMagenesSubReporteVacias())
		.build();

		return ordenesMedicamentosInsumos;
	}

	public JRDataSource crearDatasourceOrdenesMedicamentosInsumos (HashMap medicamentos){
		Integer tamano=Utilidades.convertirAEntero(String.valueOf(medicamentos.get("numRegistros")));
		DataSource dataSource = new DataSource("articulo","cantidad");
		if (tamano>0) {
			for (int k = 0; k < tamano; k++) {
				dataSource.add(String.valueOf(medicamentos.get("articulo_"+k))+"-"+String.valueOf(medicamentos.get("insumo_"+k)),
						String.valueOf(medicamentos.get("cantidad_"+k))
				);
			}
		}
		return dataSource;
	}





}
