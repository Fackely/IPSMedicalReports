package com.servinte.axioma.generadorReporte.historiaClinica;

import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;
import static net.sf.dynamicreports.report.builder.DynamicReports.col;
import static net.sf.dynamicreports.report.builder.DynamicReports.margin;
import static net.sf.dynamicreports.report.builder.DynamicReports.report;
import static net.sf.dynamicreports.report.builder.DynamicReports.stl;
import static net.sf.dynamicreports.report.builder.DynamicReports.template;
import static net.sf.dynamicreports.report.builder.DynamicReports.type;

import java.awt.Color;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.builder.MarginBuilder;
import net.sf.dynamicreports.report.builder.ReportTemplateBuilder;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.builder.component.HorizontalListBuilder;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.constant.HorizontalAlignment;
import net.sf.dynamicreports.report.constant.LineStyle;
import net.sf.dynamicreports.report.constant.PageOrientation;
import net.sf.dynamicreports.report.constant.PageType;
import net.sf.dynamicreports.report.constant.SplitType;
import net.sf.dynamicreports.report.constant.VerticalAlignment;
import net.sf.jasperreports.engine.JRDataSource;
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.UtilidadTexto;
import util.ValoresPorDefecto;
import util.reportes.dinamico.DataSource;
import util.reportes.dinamico.EstilosReportesDinamicosHistoriaClinica;

import com.princetonsa.dto.ordenesmedicas.DtoHemodialisis;
import com.princetonsa.dto.ordenesmedicas.DtoPrescripcionDialisis;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.ordenesmedicas.OrdenMedica;

public class GeneradorSubReporteOrdenesMedicas {

	/**
	 * atributo del valor de la mezcla se crea aca para cumplir con el diseño del reporte
	 */
	private String  valorMezcal="";
	
	
	/**
	 *Flag que indica si existe o no informacion de la dieta 
	 */
	private Boolean existeInformacionDieta;
	
	
	private Integer flagAdicionDatos;
	

	/**
	 *Constructor de clase 
	 */
	public GeneradorSubReporteOrdenesMedicas() {
		this.valorMezcal="";
		this.existeInformacionDieta=false;
		this.flagAdicionDatos=0;
		
	}



	/**
	 * Genera el reporte de ordenes medicas
	 * @param orden
	 * @param usuario
	 * @param paciente
	 * @param i
	 * @return reporte de ordenes medicas
	 */
	public   JasperReportBuilder  ordenesMedicamentos(HashMap orden
			,UsuarioBasico usuario, PersonaBasica paciente,Integer i,String numeroCuenta){
		JasperReportBuilder ordenesMedicamentos = report();
		HashMap mapa = new HashMap();
		mapa = orden;

		//seccion de soporte respiratorio
		ordenesMedicamentos.summary(cmp.subreport(soporteRespiratorio(orden, usuario, paciente, i)));
		
		//seccion de dieta ordenada
		ordenesMedicamentos.summary(cmp.subreport(dietaOrdenada(orden, usuario, paciente, i,numeroCuenta)));
		
		//seccion de separador 
		ordenesMedicamentos.summary(cmp.text("").setHeight(3));
		
		//seccion de cuidados de enfermeria
		ordenesMedicamentos.summary(cmp.subreport(cuidadosControlEnfermeria(orden, usuario, paciente, i)));
		
		//seccion de mezclas
		ordenesMedicamentos.summary(cmp.subreport(mezcla(orden, usuario, paciente, i)));
		
		//generacion de reporte 
		ordenesMedicamentos
		.setPageMargin(crearMagenesSubReporte())
		.setDataSource(crearDatasourceOrdenesMedicas((HashMap) orden.get("soporterespiratorio_"+i)))
		.build();
		return ordenesMedicamentos;
	}
	
	
	/**
	 * Template del reporte, no se usa uno generico ya que el reporteador genera problemas
	 * @return template del reporte
	 */
	public ReportTemplateBuilder crearPlantillaReporte()
	{
		ReportTemplateBuilder reportTemplate;
		//Diseño de template
		StyleBuilder rootStyle 				= stl.style().setPadding(1).setFontSize(7);
		StyleBuilder columnStyle 			= stl.style(rootStyle).setVerticalAlignment(VerticalAlignment.MIDDLE).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK));
		StyleBuilder columnTitleStyle		= stl.style(columnStyle).setHorizontalAlignment(HorizontalAlignment.RIGHT)
		.setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK));
		StyleBuilder boldStyle          	= stl.style(rootStyle).bold();
		StyleBuilder groupStyle				= stl.style(boldStyle).setPadding(1).setFontSize(7).setHorizontalAlignment(HorizontalAlignment.LEFT);
		StyleBuilder subtotalStyle      	= stl.style(EstilosReportesDinamicosHistoriaClinica.estiloSubTotalRSB);

		reportTemplate = template()
		.setLocale(Locale.ENGLISH)
		//Se define el tamano de la hoja y la orientación de la misma
		.setPageFormat(PageType.LETTER, PageOrientation.PORTRAIT)
		.setColumnStyle(columnStyle)
		.setColumnTitleStyle(columnTitleStyle)
		.setGroupStyle(groupStyle)
		.setGroupTitleStyle(groupStyle)
		.setSubtotalStyle(subtotalStyle)
		;

		//retorno de template
		return reportTemplate;
	}

	/**
	 * Crear margenes del reporte de ordenes medicas
	 * @return MarginBuilder
	 */
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



	/**
	 * Seccion de mezclas
	 * @param orden
	 * @param usuario
	 * @param paciente
	 * @param i
	 * @return reprote de mezclas
	 */
	public   JasperReportBuilder  mezcla(HashMap orden
			,UsuarioBasico usuario, PersonaBasica paciente,Integer i){
		JasperReportBuilder mezclaReport = report();
		HashMap mapa = new HashMap();
		mapa = orden;
		GeneradorDisenoSubReportes generadorDisenoSubReportes = new GeneradorDisenoSubReportes();

		//columman de reporte
		TextColumnBuilder<String>     mezcla = col.column("Mezcla",  "mezcla",  type.stringType()).setTitleStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK)));
		TextColumnBuilder<String>     tipoAdministracion = col.column("Tipo Administración",  "tipoAdministracion",  type.stringType()).setHorizontalAlignment(HorizontalAlignment.CENTER).setTitleStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK)));
		TextColumnBuilder<String>     descripcion = col.column("Descripción",  "descripcion",  type.stringType()).setTitleStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK)));
		TextColumnBuilder<String>     volumenTotal = col.column("Volumen Total",  "volumenTotal",  type.stringType()).setHorizontalAlignment(HorizontalAlignment.CENTER).setTitleStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK)));
		TextColumnBuilder<String>     velocidadinfusion = col.column("Velocidad de Infusión",  "velocidadinfusion",  type.stringType()).setHorizontalAlignment(HorizontalAlignment.CENTER).setTitleStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK)));
		TextColumnBuilder<String>     dosificacion = col.column("Dosificasión",  "dosificacion",  type.stringType()).setHorizontalAlignment(HorizontalAlignment.CENTER).setTitleStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK)));

		//creacion del reporte y llenado del datasource
		mezclaReport
		.columns(mezcla,tipoAdministracion,descripcion,volumenTotal,velocidadinfusion,dosificacion)
		.setDataSource(crearDatasourceMezclas((HashMap) orden.get("mezcla_"+i),orden,i))
		.setTemplate(generadorDisenoSubReportes.crearPlantillaReporte())
		.setPageMargin(generadorDisenoSubReportes.crearMagenesSubReporte())
		.setDetailSplitType(SplitType.PREVENT)
		.summary(cmp.subreport(valorMezcla()))
		.build();
		
		//reporte de mezclas
		return mezclaReport;

	}


	/**
	 * Genera el valor de la mezcla
	 * @return valor de la mezcla
	 */
	public   JasperReportBuilder  valorMezcla(){
		JasperReportBuilder mezclaReport = report();
		ComponentBuilder<?, ?> componentBuilder = null;
		HorizontalListBuilder tituloSeccion = null;
		HashMap mapa = new HashMap();
		GeneradorDisenoSubReportes generadorDisenoSubReportes = new GeneradorDisenoSubReportes();
		
		//se hace uso de atributo global
		tituloSeccion=	cmp.horizontalList(cmp.verticalList(cmp.horizontalList(
				cmp.text(this.valorMezcal).setStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.WHITE).setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK)))
		)))	;
		
		
		if(!UtilidadTexto.isEmpty(this.valorMezcal)){
			this.flagAdicionDatos++;
		}
		
		this.valorMezcal="";
		componentBuilder = cmp.verticalList(tituloSeccion);
		
		//generacion del reporte
		mezclaReport
		.summary(componentBuilder)
		.summary(cmp.subreport(valorMezclaSinTitulo()))
		.setTemplate(generadorDisenoSubReportes.crearPlantillaReporte())
		.setPageMargin(generadorDisenoSubReportes.crearMagenesSubReporte())
		.build();
		
		//se retorna el reporte
		return mezclaReport;
	}


	/**
	 * @return valor de la mezcla
	 */
	public   JasperReportBuilder  valorMezclaSinTitulo(){
		JasperReportBuilder mezclaReport = report();
		HashMap mapa = new HashMap();
		GeneradorDisenoSubReportes generadorDisenoSubReportes = new GeneradorDisenoSubReportes();
		
		//se obtiene el valor de la mezcla
		this.valorMezcal="";
		mezclaReport
		.setTemplate(generadorDisenoSubReportes.crearPlantillaReporte())
		.setPageMargin(generadorDisenoSubReportes.crearMagenesSubReporte())
		.build();
		
		//retorno de la mezcla
		return mezclaReport;
	}


	/**
	 * Genera e datasource de las mezclas
	 * @param mezcla
	 * @param ordenesMedicas
	 * @param i
	 * @return datasource con la informacion de mezclas
	 */
	public JRDataSource crearDatasourceMezclas(HashMap mezcla,HashMap ordenesMedicas,Integer i){

		Integer  tamanioSeccion=Integer.parseInt(String.valueOf(mezcla.get("numRegistros")));
		DataSource dataSource = new DataSource("mezcla","tipoAdministracion","descripcion","volumenTotal","velocidadinfusion","dosificacion");
		if (tamanioSeccion>0) {
			
			//se recorre el mapa y se llena el datasource
			for (int k = 0; k < tamanioSeccion; k++) {
				dataSource.add(String.valueOf(mezcla.get("mezcla_"+k)),
						String.valueOf(mezcla.get("tipo_"+k) ),
						String.valueOf(ordenesMedicas.get("descdietapar_"+i)),
						String.valueOf(mezcla.get("volumentotal_"+k))+" ml",
						String.valueOf(mezcla.get("velocidadinfusion_"+k))+" ml",
						String.valueOf(mezcla.get("dosificacion_"+k))+" ml"
				);
				this.flagAdicionDatos++;
				listaDescriocionMezcla(mezcla, k);
			}
		}

		// se retorna el datasource
		return dataSource;
	}


	/**
	 * Se obtiene el valor del atributo valorMezcla
	 * @param mezcla
	 * @param k
	 */
	public void listaDescriocionMezcla(HashMap mezcla,Integer k){
		HashMap mapa =(HashMap)mezcla.get("articulos_"+k);
		String res ="";
		int tamTemp=Integer.parseInt(mapa.get("numRegistros")+"");
		if(tamTemp>0)
		{
			for(int j=0;j<tamTemp;j++)
			{
				res +=mapa.get("articulo_"+j)+": ";
				res +=mapa.get("volumen_"+j)+"ml";
			}
		}
		
		//Se asigna valor
		this.valorMezcal=res;
	}





	/**
	 * Genera el reporte de soporte respiratorio
	 * @param orden
	 * @param usuario
	 * @param paciente
	 * @param i
	 * @return reporte de soporte respiratorio
	 */
	public   JasperReportBuilder  soporteRespiratorio(HashMap orden
			,UsuarioBasico usuario, PersonaBasica paciente,Integer i){
		JasperReportBuilder soporte = report();
		HorizontalListBuilder tituloSeccion = null;
		HashMap mapa = new HashMap();
		mapa = orden;
		GeneradorDisenoSubReportes generadorDisenoSubReportes = new GeneradorDisenoSubReportes();

		//tirulo de la seccion 
		tituloSeccion=	cmp.horizontalList(cmp.verticalList(cmp.horizontalList(
				cmp.text("")
		)))	;

		//Se crean las columnas del reporte
		TextColumnBuilder<String>     sopoteRespiratorio = col.column("Soporte Respiratorio",  "sopoteRespiratorio",  type.stringType()).setTitleStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK)));
		TextColumnBuilder<String>     cantidad = col.column("Cantidad Lt/min ",  "cantidad",  type.stringType()).setHorizontalAlignment(HorizontalAlignment.CENTER).setTitleStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK)));
		TextColumnBuilder<String>     equipoUtilizado = col.column("Por",  "equipoUtilizado",  type.stringType()).setHorizontalAlignment(HorizontalAlignment.CENTER).setTitleStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK)));
		TextColumnBuilder<String>     finalizar = col.column("Finalizar",  "finalizar",  type.stringType()).setHorizontalAlignment(HorizontalAlignment.CENTER).setTitleStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK)));
		TextColumnBuilder<String>     descripcion = col.column("Descripción",  "descripcion",  type.stringType()).setTitleStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK)));

		//se crea el reporte 
		soporte
		.columns(sopoteRespiratorio,cantidad,equipoUtilizado,finalizar, descripcion)
		.setTemplate(generadorDisenoSubReportes.crearPlantillaReporte())
		.setPageMargin(generadorDisenoSubReportes.crearMagenesSubReporteBordeSuperiorEspacio())
		.setDataSource(crearDatasourceOrdenesMedicas((HashMap) orden.get("soporterespiratorio_"+i)))
		.setDetailSplitType(SplitType.PREVENT)
		.build();

		//se retorna el reporte creado
		return soporte;
	}


	/**
	 * Se encarga de crear el datasource de ordenes medicas
	 * @param soporteRespiratorio
	 * @return datasource de ordenes medicas 
	 */
	public JRDataSource crearDatasourceOrdenesMedicas(HashMap soporteRespiratorio){

		Integer  tamanioSeccion=Integer.parseInt(String.valueOf(soporteRespiratorio.get("numRegistros")));
		DataSource dataSource = new DataSource("sopoteRespiratorio","cantidad","equipoUtilizado","finalizar", "descripcion");

		if (tamanioSeccion>0) {
			
			//se recorre el mapa y se llena el datasource 
			for (int k = 0; k < tamanioSeccion; k++) {
				if (String.valueOf(soporteRespiratorio.get("oxigeno_"+k)).equals("Si")) {
					dataSource.add("Oxigeno",
							String.valueOf(soporteRespiratorio.get("cantidad_"+k)),
							String.valueOf(soporteRespiratorio.get("equipo_"+k)),
							String.valueOf(soporteRespiratorio.get("oxigeno_"+k)),
							String.valueOf(soporteRespiratorio.get("descripcionsoporte_"+k))
					);
					this.flagAdicionDatos++;
				} else {
					dataSource.add("",
							"",
							"",
							String.valueOf(soporteRespiratorio.get("oxigeno_"+k)),
							String.valueOf(soporteRespiratorio.get("descripcionsoporte_"+k))
					);
					this.flagAdicionDatos++;
					
				}
			}
		}
		
		//retorno soliitado
		return dataSource;

	}



	/**
	 * Seccion de dieta ordenada
	 * @param orden
	 * @param usuario
	 * @param paciente
	 * @param i
	 * @return  reporte de dieta 
	 */
	public   JasperReportBuilder  dietaOrdenada(HashMap orden
			,UsuarioBasico usuario, PersonaBasica paciente,Integer i,String numeroCuenta){
		JasperReportBuilder dieta = report();
		List<ComponentBuilder> listaComponentes = new ArrayList<ComponentBuilder>();
		HorizontalListBuilder tituloSeccion = null;
		HorizontalListBuilder observacionesSeccion = null;
		TextColumnBuilder []cols = new TextColumnBuilder[6];
		HashMap mapa = new HashMap();
		mapa = orden;
		GeneradorDisenoSubReportes generadorDisenoSubReportes = new GeneradorDisenoSubReportes();
		Connection con = null;
		
		//se abre a conexion 
		con = UtilidadBD.abrirConexion();
		OrdenMedica mundoOrdenMedica = new OrdenMedica();
		
		//se consulta la dieta
		ArrayList<String> collec =(ArrayList) mundoOrdenMedica.cargarOtrosDietaReproteHC(con, Integer.valueOf(numeroCuenta));
		 
		//se cierra la conexion
		UtilidadBD.closeConnection(con);
		
		//Se crean las columnas del reporte
		TextColumnBuilder<String>     dietaOrdenada = col.column("Dieta Ordenada",  "dietaOrdenada",  type.stringType()).setTitleStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK)));
		TextColumnBuilder<String>     tipoDieta = col.column("Tipo Dieta",  "tipoDieta",  type.stringType()).setTitleStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK)));
		TextColumnBuilder<String>     descripcion = col.column("Descripción",  "descripcion",  type.stringType()).setTitleStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK)));
		TextColumnBuilder<String>     suspender = col.column("Suspender",  "suspender",  type.stringType()).setHorizontalAlignment(HorizontalAlignment.CENTER).setTitleStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK)));

		//Se crea el reporte y se llama al metodo de llenado del datasource
		dieta
		.columns(dietaOrdenada,tipoDieta,descripcion,suspender)
		.setTemplate(generadorDisenoSubReportes.crearPlantillaReporte())
		.setPageMargin(generadorDisenoSubReportes.crearMagenesSubReporteBordeSuperiorEspacio())
		.setDataSource(crearDatasourceDietaOrdenada((ArrayList<DtoDietaHistoriaClinica>) orden.get("dietasFull_"+i), collec,orden))
		.setSummarySplitType(SplitType.PREVENT)
		.build();

		//se retorna el reporte generado
		return dieta;


	}
	
	/**
	 * @return margenes 
	 */
	public MarginBuilder crearMagenesSubReporte2()
	{
		MarginBuilder margin;
		margin = margin()
		.setTop(1)
		.setBottom(1)
		.setLeft(1)
		.setRight(1)
		;

		return margin;
	}

	/**
	 * Se crear el datasource de las dietas ordenadas
	 * @param dietaOrdenada
	 * @param collec
	 * @return datadsource de dieta ordenada
	 */
	public JRDataSource crearDatasourceDietaOrdenada(ArrayList<DtoDietaHistoriaClinica> dietaOrdenada,ArrayList<String> collec,HashMap orden){

		//Integer  tamanioSeccion=Integer.parseInt(String.valueOf(dietaOrdenada.get("numRegistros")));
		DataSource dataSource = new DataSource("dietaOrdenada","tipoDieta","descripcion","suspender");
		if(dietaOrdenada.size()>0){
			dataSource.add("Vía Oral",
					dietaOrdenada.get(dietaOrdenada.size()-1).getNombresDieta(),
					obtenerDescripcion(dietaOrdenada.get(dietaOrdenada.size()-1).getDescripcion(), String.valueOf(orden.get("datos_medico_0"))),
					obtenerSuspenderDieta(dietaOrdenada.get(dietaOrdenada.size()-1).getSuspender())
			);
			this.flagAdicionDatos++;
		}
		
		
		return dataSource;
	}

	
	public String obtenerDescripcion(String descDieta , String descMapaOrden){
		String observacion="";
		String mapaOrden=descMapaOrden.replace("\n\n", "");

		if(mapaOrden.split("\n").length>0){
			mapaOrden=mapaOrden.split("\n")[0];

			if(!UtilidadTexto.isEmpty(descDieta)){
				String[] comentariosPorFecha=descDieta.split("\r\n\r\n");


				for (int i = 0; i < comentariosPorFecha.length; i++) {
					if(comentariosPorFecha[i].contains(mapaOrden)){
						observacion= comentariosPorFecha[i];
					}

				}
			}
		}
		return observacion;
	}
	
	

	/**
	 * @param valor
	 * @return utilidad para saber sis se suspende o no la dieta
	 */
	public String obtenerSuspenderDieta(String valor){
		String res ="";
		if (valor!=null && !valor.equals("")) {
			if(valor.equals("0")){
				res="No";
			}else{
				res="Si";
			}
		}
		
		//Se retorna si o no 
		return res;
	}

	/**
	 * crea el data source de parentales
	 * @param parentalesEnterales
	 * @return datasource 
	 */
	public JRDataSource crearDatasourceParentalesEnterales(HashMap parentalesEnterales){

		Integer  tamanioSeccion=Integer.parseInt(String.valueOf(parentalesEnterales.get("numRegistros")));
		DataSource dataSource  = new DataSource("mezcla","tipoAdmon","articuloVolumen","volumenTotal","velocidadInfusion","fechaSuspension");
	
		//Se recorre el mapa y se llena el datasource
		for (int i = 0; i < tamanioSeccion; i++) {
			dataSource.add(String.valueOf(parentalesEnterales.get("mezcla_"+i)),
					String.valueOf(parentalesEnterales.get("tipo_"+i)),
					String.valueOf(calcularArticuloVolumen(parentalesEnterales, i)),
					String.valueOf(parentalesEnterales.get("volumentotal_"+i))+" ml",
					String.valueOf(parentalesEnterales.get("velocidadinfusion_"+i))+" ml");		
		}

		//retorna datasource
		return dataSource;
	}


	/**
	 * @param parentalesEnterales
	 * @param k
	 * @return calcula el volumen de un articulo
	 */
	public String calcularArticuloVolumen(HashMap parentalesEnterales,Integer k){

		HashMap mapaTemp=(HashMap)parentalesEnterales.get("articulos_"+k);
		int tamTemp=Integer.parseInt(String.valueOf(mapaTemp.get("numRegistros")));
		String valor="";
		List<String> listaarticuloVolumen= new ArrayList<String>();
		String valorTotal="";
		if(tamTemp>0)
		{
			for(int j=0;j<tamTemp;j++){
				valor="";
				if (!String.valueOf(mapaTemp.get("articulo_"+j)).equals("")) {
					valor=String.valueOf(mapaTemp.get("articulo_"+j));
				}

				if (!String.valueOf(mapaTemp.get("articulo_"+j)).equals("") &&
						!String.valueOf(mapaTemp.get("articulo_"+j)).equals("")) {
					valor+=" - ";
				}

				if (!String.valueOf(mapaTemp.get("volumen_"+j)).equals("")) {
					valor+=String.valueOf(mapaTemp.get("volumen_"+j))+" ml";
				}

				listaarticuloVolumen.add(valor);
			}
		}


		for (String articuloVolumen : listaarticuloVolumen) {

			valorTotal+=articuloVolumen+" \n";
		}


		return valorTotal;
	}

	/**
	 * Se genera el reporte de control de enfermeria
	 * @param orden
	 * @param usuario
	 * @param paciente
	 * @param i
	 * @return reporte de control de enfermeria
	 */
	public   JasperReportBuilder  cuidadosControlEnfermeria(HashMap orden
			,UsuarioBasico usuario, PersonaBasica paciente,Integer i){
		JasperReportBuilder cuidadosControlEnfermeriaReport = report();
		HashMap mapa = new HashMap();
		HashMap mapaSeccion = new HashMap();
		Integer tamanioSeccion= new Integer(0);
		mapa = orden;
		GeneradorDisenoSubReportes generadorDisenoSubReportes = new GeneradorDisenoSubReportes();

		//Se crean las colummnas
		TextColumnBuilder<String>     cuidados = col.column("Cuidados y Controles de Enfermería ",  "cuidados",  type.stringType()).setTitleStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK)));
		TextColumnBuilder<String>     frecuencia = col.column("Frecuencia ",  "frecuencia",  type.stringType()).setHorizontalAlignment(HorizontalAlignment.CENTER).setTitleStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK)));
		TextColumnBuilder<String>     periodo = col.column("Periodo",  "periodo",  type.stringType()).setHorizontalAlignment(HorizontalAlignment.CENTER).setTitleStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK)));
		TextColumnBuilder<String>     observaciones = col.column("Observaciones",  "observaciones",  type.stringType()).setTitleStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK)));

		//Se crea el reporte
		cuidadosControlEnfermeriaReport
		.columns(cuidados,frecuencia,periodo,observaciones)
		.setTemplate(generadorDisenoSubReportes.crearPlantillaReporte())
		.setPageMargin(generadorDisenoSubReportes.crearMagenesSubReporte())
		.setDataSource(crearDatasourceCuidadosControlEnfermeria((HashMap)mapa.get("cuidadosespeciales_"+i)))
		.build();
		
		//Se retorna el reporte generado
		return cuidadosControlEnfermeriaReport;
	}

	/**
	 * Se crea el datasource de control de enfermeria
	 * @param parentalesEnterales
	 * @return datasource
	 */
	public JRDataSource crearDatasourceCuidadosControlEnfermeria(HashMap parentalesEnterales){

		Integer  tamanioSeccion=Integer.parseInt(String.valueOf(parentalesEnterales.get("numRegistros")));
		DataSource dataSource  = new DataSource("cuidados","frecuencia","periodo","observaciones");
		
		//Se llena el datasource a partir del mapa 
		for (int i = 0; i < tamanioSeccion; i++) {
			dataSource.add(String.valueOf(parentalesEnterales.get("cuidado_"+i))+" - Si",
					obtenerFrecuncia( String.valueOf(parentalesEnterales.get("frecuencia_"+i)),String.valueOf(parentalesEnterales.get("tipofrecuencia_"+i))),
					String.valueOf(parentalesEnterales.get("periodo_"+i)),
					String.valueOf(parentalesEnterales.get("descripcion_"+i))
			);
			this.flagAdicionDatos++;
		}
		
		//se retorna el mapa 
		return dataSource;
	}

	/**
	 * Utilidad que obtine el valor en letras de la frecuencia 
	 * @param frec
	 * @param tipoFrec
	 * @return frecuencia
	 */
	public String obtenerFrecuncia(String frec,String tipoFrec){
		String res ="";
		if (tipoFrec.equals("1")) {
			res =frec+" Horas";
		}else if (tipoFrec.equals("2")) {
			res =frec+" Minutos";
		}else if (tipoFrec.equals("3")) {
			res =frec+" Días";
		}
		
		
		//Se retorna la frecuencia 
		return res;
	}



	/**
	 * Se genera el reporte de dialisis
	 * @param orden
	 * @param usuario
	 * @param paciente
	 * @param i
	 * @return reporte de dialisis
	 */
	public   JasperReportBuilder  dialisis(HashMap orden
			,UsuarioBasico usuario, PersonaBasica paciente,Integer i){
		JasperReportBuilder dialisisReport = report();
		HorizontalListBuilder tituloSeccion = null;
		HorizontalListBuilder terapiaSeccion = null;
		HashMap mapa = new HashMap();
		mapa = orden;
		GeneradorDisenoSubReportes generadorDiseñoSubReportes = new GeneradorDisenoSubReportes();


		//Se adicionan los titulos
		DtoPrescripcionDialisis dialisis = (DtoPrescripcionDialisis)mapa.get("prescripcionDialisis_"+i);
		if(!dialisis.getCodigoHistoEnca().equals(""))
		{
			tituloSeccion=	cmp.horizontalList(cmp.verticalList(cmp.horizontalList(
					cmp.text("PRESCRIPCION DIALISIS").setStyle(EstilosReportesDinamicosHistoriaClinica.estiloBordeNegrilla)
			)))	;

			terapiaSeccion=	cmp.horizontalList(cmp.verticalList(cmp.horizontalList(
					cmp.text("Modalidad terapia: "+ValoresPorDefecto.getIntegridadDominio(String.valueOf(dialisis.getModalidadTerapia()))).setStyle(EstilosReportesDinamicosHistoriaClinica.estiloBordeNegrilla)
			)))	;

			if(dialisis.getModalidadTerapia().equals(ConstantesIntegridadDominio.acronimoHemodialisis))
			{
				//Se adicionan los subreportes
				if (dialisis.getHemodialisis().size()>0) {
					dialisisReport.summary(cmp.subreport(registroPrincipalHemodialisis(orden, usuario, paciente, i)));
				}
				if(dialisis.existeHemodialisisEnfermeria()){
					dialisisReport.summary(cmp.subreport(HemodialisisEnfermeria(orden, usuario, paciente, i)));
				}



			}
		}

		return null;

	}

	/**
	 * @param orden
	 * @param usuario
	 * @param paciente
	 * @param i
	 * @return
	 */
	public   JasperReportBuilder  registroPrincipalHemodialisis(HashMap orden
			,UsuarioBasico usuario, PersonaBasica paciente,Integer i){
		JasperReportBuilder registroPrincipalHemodialisisReport = report();
		HashMap mapa = new HashMap();
		HashMap mapaSeccion = new HashMap();
		Integer tamanioSeccion= new Integer(0);
		mapa = orden;
		GeneradorDisenoSubReportes generadorDiseñoSubReportes = new GeneradorDisenoSubReportes();


		TextColumnBuilder<String>     tiempo = col.column("Tiempo (horas): ",  "tiempo",  type.stringType()).setTitleStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(9).setBorder(stl.pen1Point().setLineColor(Color.BLACK)));
		TextColumnBuilder<String>     pesoSeco = col.column("Peso seco (Kg): ",  "pesoSeco",  type.stringType()).setTitleStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(9).setBorder(stl.pen1Point().setLineColor(Color.BLACK)));
		TextColumnBuilder<String>     tipoMembrana = col.column("Tipo membrana: ",  "tipoMembrana",  type.stringType()).setTitleStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(9).setBorder(stl.pen1Point().setLineColor(Color.BLACK)));
		TextColumnBuilder<String>     filtro = col.column("Filtro: ",  "filtro",  type.stringType()).setTitleStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(9).setBorder(stl.pen1Point().setLineColor(Color.BLACK)));
		TextColumnBuilder<String>     flujoBomba = col.column("Flujo bomba: ",  "flujoBomba",  type.stringType()).setTitleStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(9).setBorder(stl.pen1Point().setLineColor(Color.BLACK)));
		TextColumnBuilder<String>     flujoDializado = col.column("Flujo dializado: ",  "flujoDializado",  type.stringType()).setTitleStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(9).setBorder(stl.pen1Point().setLineColor(Color.BLACK)));
		TextColumnBuilder<String>     uf = col.column("UF: ",  "uf",  type.stringType()).setTitleStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(9).setBorder(stl.pen1Point().setLineColor(Color.BLACK)));
		TextColumnBuilder<String>     accesoVascular = col.column("Acceso vascular: ",  "accesoVascular",  type.stringType()).setTitleStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(9).setBorder(stl.pen1Point().setLineColor(Color.BLACK)));
		TextColumnBuilder<String>     antiCuagulacion = col.column("Anticoagulación: ",  "antiCuagulacion",  type.stringType()).setTitleStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(9).setBorder(stl.pen1Point().setLineColor(Color.BLACK)));
		TextColumnBuilder<String>     pesoPre = col.column("Peso pre (Kg): ",  "pesoPre",  type.stringType()).setTitleStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(9).setBorder(stl.pen1Point().setLineColor(Color.BLACK)));
		TextColumnBuilder<String>     pesoPos = col.column("Peso pos (Kg): ",  "pesoPos",  type.stringType()).setTitleStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(9).setBorder(stl.pen1Point().setLineColor(Color.BLACK)));


		registroPrincipalHemodialisisReport
		.columns(tiempo,pesoSeco,tipoMembrana,filtro,flujoBomba,flujoDializado,uf,accesoVascular,antiCuagulacion,pesoPre,pesoPos)
		.setTemplate(generadorDiseñoSubReportes.crearPlantillaReporte()).setPageMargin(generadorDiseñoSubReportes.crearMagenesReporte())
		.setDataSource(crearDatasourceregistroPrincipalHemodialisis(   ((DtoPrescripcionDialisis)mapa.get("prescripcionDialisis_"+i)).getHemodialisis()))
		.build();
		return registroPrincipalHemodialisisReport;
	}


	/**
	 * @param listaHemo
	 * @return
	 */
	public JRDataSource crearDatasourceregistroPrincipalHemodialisis(List<DtoHemodialisis>    listaHemo){

		DataSource dataSource  = new DataSource("tiempo","pesoSeco","tipoMembrana","filtro","flujoBomba","flujoDializado","uf",
				"accesoVascular","antiCuagulacion","pesoPre","pesoPos");


		for (DtoHemodialisis hemo : listaHemo) {

			if(hemo.getConsecutivoHemodialisisPadre().equals(""))
			{
				dataSource.add(String.valueOf(hemo.getTiempo()),String.valueOf(hemo.getPesoSeco()),String.valueOf(hemo.getNombreTipoMembrana()),
						String.valueOf(hemo.getNombreFiltro()),String.valueOf(hemo.getNombreFlujoBomba()),String.valueOf(hemo.getNombreFlujoDializado() ),String.valueOf(hemo.getUp())
						,String.valueOf(hemo.getNombreAccesoVascular()),String.valueOf(hemo.getAnticoagulacion()),String.valueOf(hemo.getPesoPre()),
						String.valueOf(hemo.getPesoPos()));
			}
		}


		return dataSource;
	}

	/**
	 * @param orden
	 * @param usuario
	 * @param paciente
	 * @param i
	 * @return
	 */
	public   JasperReportBuilder  HemodialisisEnfermeria(HashMap orden
			,UsuarioBasico usuario, PersonaBasica paciente,Integer i){
		JasperReportBuilder HemodialisisEnfermeriaReport = report();
		HorizontalListBuilder tituloSeccion = null;
		HashMap mapa = new HashMap();
		HashMap mapaSeccion = new HashMap();
		Integer tamanioSeccion= new Integer(0);
		mapa = orden;
		GeneradorDisenoSubReportes generadorDiseñoSubReportes = new GeneradorDisenoSubReportes();

		tituloSeccion=	cmp.horizontalList(cmp.verticalList(cmp.horizontalList(
				cmp.text("Registros de enfermería").setStyle(EstilosReportesDinamicosHistoriaClinica.estiloBordeNegrilla)
		)))	;

		TextColumnBuilder<String>     pesoSeco = col.column("Peso seco (Kg)",  "pesoSeco",  type.stringType()).setTitleStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(9).setBorder(stl.pen1Point().setLineColor(Color.BLACK)));
		TextColumnBuilder<String>     filtro = col.column("Filtro",  "filtro",  type.stringType()).setTitleStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(9).setBorder(stl.pen1Point().setLineColor(Color.BLACK)));
		TextColumnBuilder<String>     uf = col.column("UF",  "uf",  type.stringType()).setTitleStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(9).setBorder(stl.pen1Point().setLineColor(Color.BLACK)));
		TextColumnBuilder<String>     pesoPre = col.column("Peso Pre (Kg)",  "pesoPre",  type.stringType()).setTitleStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(9).setBorder(stl.pen1Point().setLineColor(Color.BLACK)));
		TextColumnBuilder<String>     pesoPos = col.column("Peso Pos (Kg)",  "pesoPos",  type.stringType()).setTitleStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(9).setBorder(stl.pen1Point().setLineColor(Color.BLACK)));
		TextColumnBuilder<String>     fechaHora = col.column("Fecha/hora",  "fechaHora",  type.stringType()).setTitleStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(9).setBorder(stl.pen1Point().setLineColor(Color.BLACK)));
		TextColumnBuilder<String>     profesional = col.column("Profesional",  "profesional",  type.stringType()).setTitleStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(9).setBorder(stl.pen1Point().setLineColor(Color.BLACK)));


		HemodialisisEnfermeriaReport
		.columns(pesoSeco,filtro,uf,pesoPre,pesoPos,fechaHora,profesional)
		.setTemplate(generadorDiseñoSubReportes.crearPlantillaReporte()).setPageMargin(generadorDiseñoSubReportes.crearMagenesReporte())
		.setDataSource(crearDatasourceregistroPrincipalHemodialisisCompleto(((DtoPrescripcionDialisis)mapa.get("prescripcionDialisis_"+i)).getHemodialisis()))
		.build();
		return HemodialisisEnfermeriaReport;

	}


	/**
	 * @param listaHemo
	 * @return
	 */
	public JRDataSource crearDatasourceregistroPrincipalHemodialisisCompleto(List<DtoHemodialisis>    listaHemo){

		DataSource dataSource  = new DataSource("pesoSeco","filtro","uf","pesoPre","pesoPos","fechaHora","profesional");


		for (DtoHemodialisis hemo : listaHemo) {

			if(!hemo.getConsecutivoHemodialisisPadre().equals(""))
			{
				dataSource.add(String.valueOf(hemo.getPesoSeco()),String.valueOf(hemo.getNombreFiltro()),String.valueOf(hemo.getUp()),
						String.valueOf(hemo.getPesoPre()),String.valueOf(hemo.getPesoPos()),String.valueOf(hemo.getFechaModifica())+"/"+String.valueOf(hemo.getHoraModifica()),
						String.valueOf(hemo.getUsuarioModifica().getNombreUsuario()));
			}
		}
		return dataSource;
	}



	/**
	 * @return the existeInformacionDieta
	 */
	public Boolean getExisteInformacionDieta() {
		return existeInformacionDieta;
	}



	/**
	 * @param existeInformacionDieta the existeInformacionDieta to set
	 */
	public void setExisteInformacionDieta(Boolean existeInformacionDieta) {
		this.existeInformacionDieta = existeInformacionDieta;
	}



	/**
	 * @return the flagAdicionDatos
	 */
	public Integer getFlagAdicionDatos() {
		return flagAdicionDatos;
	}



	/**
	 * @param flagAdicionDatos the flagAdicionDatos to set
	 */
	public void setFlagAdicionDatos(Integer flagAdicionDatos) {
		this.flagAdicionDatos = flagAdicionDatos;
	}




}
