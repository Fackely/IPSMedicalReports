package com.servinte.axioma.generadorReporte.capitacion.GeneradorReporteTotalServiciosArticulosValorizadosPorConvenio;

import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;
import static net.sf.dynamicreports.report.builder.DynamicReports.col;
import static net.sf.dynamicreports.report.builder.DynamicReports.ctab;
import static net.sf.dynamicreports.report.builder.DynamicReports.margin;
import static net.sf.dynamicreports.report.builder.DynamicReports.report;
import static net.sf.dynamicreports.report.builder.DynamicReports.stl;
import static net.sf.dynamicreports.report.builder.DynamicReports.template;
import static net.sf.dynamicreports.report.builder.DynamicReports.type;

import java.awt.Color;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Locale;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.builder.MarginBuilder;
import net.sf.dynamicreports.report.builder.ReportBuilder;
import net.sf.dynamicreports.report.builder.ReportTemplateBuilder;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.builder.component.HorizontalListBuilder;
import net.sf.dynamicreports.report.builder.crosstab.CrosstabBuilder;
import net.sf.dynamicreports.report.builder.crosstab.CrosstabColumnGroupBuilder;
import net.sf.dynamicreports.report.builder.crosstab.CrosstabMeasureBuilder;
import net.sf.dynamicreports.report.builder.crosstab.CrosstabRowGroupBuilder;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.constant.Calculation;
import net.sf.dynamicreports.report.constant.HorizontalAlignment;
import net.sf.dynamicreports.report.constant.OrderType;
import net.sf.dynamicreports.report.constant.PageOrientation;
import net.sf.dynamicreports.report.constant.PageType;
import net.sf.dynamicreports.report.constant.StretchType;
import net.sf.dynamicreports.report.constant.VerticalAlignment;

import org.axioma.util.log.Log4JManager;

import util.ConstantesIntegridadDominio;
import util.Utilidades;
import util.reportes.dinamico.EstilosReportesDinamicos;
import util.reportes.dinamico.complex.ReportDesign;

import com.princetonsa.dto.capitacion.DtoImpresionReporteTotalServiciosArticulosValorizadosPorConvenio;
import com.princetonsa.dto.capitacion.DtoMesesTotalServiciosArticulosValorizadosPorConvenio;
import com.princetonsa.dto.capitacion.DtoNivelesAtencionTotalServiciosArticulosValorizadosPorConvenio;
import com.servinte.axioma.generadorReporte.comun.IConstantesReporte;

/**
 * @author Cristhian Murillo
 */
public class GeneradorReporteTotalServArtPorConvenioDinamicoDisenio implements ReportDesign<GeneradorReporteTotalServArtPorConvenioDinamicoDatos> 
{

	/** * Datos del reporte */
	private DtoImpresionReporteTotalServiciosArticulosValorizadosPorConvenio datosReporte = null;
	
	/** * Cantidad de columnas a mostrar */
	private Integer cantidadColumnas;
	
	/** * Tamaño de la columna */
	private Integer tamanioColMes;
	
	/** * Tamaño del eje, donde se encuentran los titulos de columnas y filas */
	private Integer tamanioEje;
	
	/** * Formato de la página */
	private PageType pageType;
	
	/** * Titulos de las columnas */
	private ArrayList<String> listaTitulosColumnas;
	
	
	
	/**
	 * Constructor de la clase
	 * @author Cristhian Murillo
	*/
	public GeneradorReporteTotalServArtPorConvenioDinamicoDisenio(DtoImpresionReporteTotalServiciosArticulosValorizadosPorConvenio datosReporte) 
	{
		this.datosReporte 	= datosReporte;
		this.pageType = PageType.A4;
	}
	
	
	/**
	 * Organiza de una forma genérica los valores d elso formatos para 
	 * @autor Cristhian Murillo
	 */
	private void organizarFormatosImpresionParaValidaciones()
	{
		if(!Utilidades.isEmpty(this.datosReporte.getListaNivelesAtencionArticulos()))
		{
			// Articulos
			if(this.datosReporte.getIdFormatoArticulos() == IConstantesReporte.formatoPlanoA){
				this.datosReporte.setFormatoSinEncabezado(true);
				this.datosReporte.setIdFormatoArticulos(IConstantesReporte.formatoA);
			}
			else if(this.datosReporte.getIdFormatoArticulos() == IConstantesReporte.formatoPlanoB){
				this.datosReporte.setFormatoSinEncabezado(true);
				this.datosReporte.setIdFormatoArticulos(IConstantesReporte.formatoB);
			}
			else if(this.datosReporte.getIdFormatoArticulos() == IConstantesReporte.formatoPlanoC){
				this.datosReporte.setFormatoSinEncabezado(true);
				this.datosReporte.setIdFormatoArticulos(IConstantesReporte.formatoC);
			}
		}
		

		if(!Utilidades.isEmpty(this.datosReporte.getListaNivelesAtencionServicios()))
		{
			// Servicios
			if(this.datosReporte.getIdFormatoServicios() == IConstantesReporte.formatoPlanoA){
				this.datosReporte.setFormatoSinEncabezado(true);
				this.datosReporte.setIdFormatoServicios(IConstantesReporte.formatoA);
			}
			else if(this.datosReporte.getIdFormatoServicios() == IConstantesReporte.formatoPlanoB){
				this.datosReporte.setFormatoSinEncabezado(true);
				this.datosReporte.setIdFormatoServicios(IConstantesReporte.formatoB);
			}
			else if(this.datosReporte.getIdFormatoServicios() == IConstantesReporte.formatoPlanoC){
				this.datosReporte.setFormatoSinEncabezado(true);
				this.datosReporte.setIdFormatoServicios(IConstantesReporte.formatoC);
			}
		}
	}
	
	
	
	/**
	 * Crea el diseño y configuracion de la presentación del reporte
	 * 
	 * @author Cristhian Murillo
	 */
	public void configureReport(ReportBuilder<?> rb, GeneradorReporteTotalServArtPorConvenioDinamicoDatos invoiceData) 
	{
		organizarFormatosImpresionParaValidaciones();
		
		if(this.datosReporte.getIdFormatoArticulos() == this.datosReporte.getIdFormatoServicios() 
				&& this.datosReporte.getIdFormatoArticulos() == IConstantesReporte.formatoA)
		{
			JasperReportBuilder subreportServicioArticuloFormatoA = new JasperReportBuilder();
			subreportServicioArticuloFormatoA = report();
			subreportServicioArticuloFormatoA.setDataSource(invoiceData.createDataSourceServicioArticuloFormatoA(this.datosReporte));
			
			if(!this.datosReporte.isFormatoSinEncabezado())
			{
				//ArticuloServicio
				subreportServicioArticuloFormatoA.setTemplate(crearPlantillaReporte()); 
				configurarTamanioSubReporte(subreportServicioArticuloFormatoA);
				subreportServicioArticuloFormatoA.summary(crearCrosstab(this.datosReporte.getNombreAgrupacion()));
				subreportServicioArticuloFormatoA.setPageMargin(crearMagenesSubReporte());
				rb.detail(cmp.subreport(subreportServicioArticuloFormatoA));
			}
			else
			{
				//ArticuloServicio Plano
				subreportServicioArticuloFormatoA.columns(crearColumnasPlano(true));
				subreportServicioArticuloFormatoA.title(crearComponenteEncabezadoPlano(true));
				subreportServicioArticuloFormatoA.setShowColumnTitle(false);
				rb.summary((cmp.subreport(subreportServicioArticuloFormatoA)));
			}
		}
		else
		{
			JasperReportBuilder subreportServicio = new JasperReportBuilder();
			subreportServicio = report();
			
			JasperReportBuilder subreportArticulo = new JasperReportBuilder();
			subreportArticulo = report();
			
			if(!this.datosReporte.isFormatoSinEncabezado())
			{
				// Servicio
				subreportServicio.setDataSource(invoiceData.createDataSourceServicio(this.datosReporte));
				subreportServicio.setTemplate(crearPlantillaReporte()); 
				configurarTamanioSubReporte(subreportServicio);
				subreportServicio.summary(crearCrosstab(this.datosReporte.getNombreAgrupacion()));
				subreportServicio.setPageMargin(crearMagenesSubReporte());
				rb.summary((cmp.subreport(subreportServicio)));
				
				// Articulo
				subreportArticulo.setDataSource(invoiceData.createDataSourceArticulo(this.datosReporte));
				subreportArticulo.setTemplate(crearPlantillaReporte()); 
				configurarTamanioSubReporte(subreportArticulo);
				subreportArticulo.summary(crearCrosstab(this.datosReporte.getNombreAgrupacion()));
				subreportArticulo.setPageMargin(crearMagenesSubReporte());
				rb.summary(cmp.subreport(subreportArticulo));
			}
			else
			{
				boolean formatoServiciosA = false;
				boolean formatoArticulosA = false;
				
				if(this.datosReporte.getIdFormatoServicios() == IConstantesReporte.formatoA){
					formatoServiciosA = true;
				}
				if(this.datosReporte.getIdFormatoArticulos() == IConstantesReporte.formatoA){
					formatoArticulosA = true;
				}
				
				rb.title(crearComponenteEncabezadoPlano(formatoArticulosA));
				
				
				// Servicio Plano
				subreportServicio.setDataSource(invoiceData.createDataSourceServicio(this.datosReporte));
				subreportServicio.columns(crearColumnasPlano(formatoServiciosA));
				//subreportServicio.setShowColumnTitle(false);
				subreportServicio.lastPageFooter(cmp.text(""));
				rb.summary((cmp.subreport(subreportServicio)));
				
				
				// Servicio Plano
				subreportArticulo.setDataSource(invoiceData.createDataSourceArticulo(this.datosReporte));
				subreportArticulo.columns(crearColumnasPlano(formatoArticulosA));
				//subreportArticulo.setShowColumnTitle(false);
				rb.summary((cmp.subreport(subreportArticulo)));
				
			}
		}
		
		
		if(!this.datosReporte.isFormatoSinEncabezado())
		{
			rb.setTemplate(crearPlantillaReporte());
			rb.title(crearComponenteEncabezado());
			rb.pageFooter(crearPiePagina());
			rb.pageFooter(crearPager()); 
			rb.setSummaryWithPageHeaderAndFooter(true);
		}
		
	}

	
	
	/**
	 * Crea el pie de página para el reporte
	 * @author Cristhian Murillo
	 * @return ComponentBuilder
	 */
	public ComponentBuilder<?, ?> crearPiePagina() 
	{
		String espacio = " ";
		
		return	cmp.horizontalList(cmp.text(
				"Impresión: "+this.datosReporte.getFechaProceso()+
				espacio+
				this.datosReporte.getUsuarioProceso() +espacio)
			.setStretchWithOverflow(true)
			.setHorizontalAlignment(HorizontalAlignment.CENTER));
	}
	
	
	/**
	 * Crea el pie de página para el reporte
	 * @author Cristhian Murillo
	 * @return ComponentBuilder
	 */
	public ComponentBuilder<?, ?> crearPager() 
	{
		return	cmp.horizontalList(cmp.pageXofY().setFormatExpression("Pag. {0} de {1}")
				.setHorizontalAlignment(HorizontalAlignment.CENTER));
	}
	
	
	/**
	 * Crea el encabezado del reporte (títulos y subtítulos).
	 * 
	 * @author Cristhian Murillo
	 * 
	 * @return ComponentBuilder
	 */
	public ComponentBuilder<?, ?> crearComponenteEncabezado() 
	{
		String lineaTitulo	= this.datosReporte.getNombreHospital();
		String linea2		= this.datosReporte.getNitHospital();
		String linea3		= IConstantesReporte.ordCapSub;
		String fecha		= this.datosReporte.getFecha();
		String convenio		= this.datosReporte.getNombreConvenio(); 
		String contrato		= this.datosReporte.getNombreContrato();
		String fechaconveniocontrato="Fecha: "+fecha+"      Convenio: "+convenio;
		if(contrato != null){
			fechaconveniocontrato+="        Contrato:"+contrato;
		}
		
		ComponentBuilder<?, ?> dynamicReportsComponent = null;
		
		
		
		if(!this.datosReporte.isFormatoSinEncabezado())
		{
			//this.datosReporte.setRutaLogo("C:\\logo_presidencia.gif"); 
			Log4JManager.info("Ruta Logo: "+this.datosReporte.getRutaLogo());
			
			if(this.datosReporte.getUbicacionLogo().equals(ConstantesIntegridadDominio.acronimoUbicacionIzquierda))
			{
				// Izquierda
				dynamicReportsComponent = 
					cmp.verticalList(
						cmp.horizontalList(
							cmp.image(this.datosReporte.getRutaLogo())
									.setDimension(60, 30)
									.setStyle(stl.style().setLeftPadding(30))
									.setWidth(60)
							,cmp.verticalList(
								cmp.text(lineaTitulo).setStyle(EstilosReportesDinamicos.estiloTitulo).setHorizontalAlignment(HorizontalAlignment.CENTER),
								cmp.text(linea2).setStyle(EstilosReportesDinamicos.estiloTitulo).setHorizontalAlignment(HorizontalAlignment.CENTER),
								cmp.text(linea3).setStyle(EstilosReportesDinamicos.estiloSubTitulo).setHorizontalAlignment(HorizontalAlignment.CENTER)
							)
						).setStretchType(StretchType.RELATIVE_TO_TALLEST_OBJECT)
						,cmp.verticalList(
							cmp.text(fechaconveniocontrato).setStyle(EstilosReportesDinamicos.estiloDetalle).setHorizontalAlignment(HorizontalAlignment.LEFT)
						).setStyle(EstilosReportesDinamicos.estiloEjeCentradoMedioBordeSinBorde)
					).setStyle(EstilosReportesDinamicos.estiloEjeCentradoMedioBordeSinBorde);
			}
			else
			{
				// Derecha
				dynamicReportsComponent = 
					cmp.verticalList(
						cmp.horizontalList(
							cmp.verticalList(
								cmp.text(lineaTitulo).setStyle(EstilosReportesDinamicos.estiloTitulo).setHorizontalAlignment(HorizontalAlignment.CENTER),
								cmp.text(linea2).setStyle(EstilosReportesDinamicos.estiloTitulo).setHorizontalAlignment(HorizontalAlignment.CENTER),
								cmp.text(linea3).setStyle(EstilosReportesDinamicos.estiloSubTitulo).setHorizontalAlignment(HorizontalAlignment.CENTER)//,
							),
							cmp.image(this.datosReporte.getRutaLogo())
									.setDimension(60, 30)
									.setStyle(stl.style().setLeftPadding(30))
									.setWidth(60)
						).setStretchType(StretchType.RELATIVE_TO_TALLEST_OBJECT)
						,cmp.verticalList(
							cmp.text(fechaconveniocontrato).setStyle(EstilosReportesDinamicos.estiloDetalle).setHorizontalAlignment(HorizontalAlignment.LEFT)
						).setStyle(EstilosReportesDinamicos.estiloEjeCentradoMedioBordeSinBorde)
					).setStyle(EstilosReportesDinamicos.estiloEjeCentradoMedioBordeSinBorde);
			}
				
			return dynamicReportsComponent;
		}
		else
		{
			HorizontalListBuilder horizontalListBuilder = cmp.horizontalList();
			for(int i =0; i<this.listaTitulosColumnas.size(); i++)
			{
				horizontalListBuilder.add(cmp.text(this.listaTitulosColumnas.get(i))); 
			}
			
			dynamicReportsComponent = 
				cmp.verticalList(
					cmp.text("Reporte: "+lineaTitulo),
					cmp.text("Tipo Consulta: "+this.datosReporte.getNombreAgrupacion()),
					cmp.text(fechaconveniocontrato),
					horizontalListBuilder
				);
			return dynamicReportsComponent;
		}
	}
	
	
	/**
	 * Define las medidas de als columnas y filas del reporte.
	 * 
	 * @param listaColumnas
	 * @author Cristhian Murillo
	*/
	private void configurarTamanioSubReporte(ReportBuilder<?> rb)
	{
		rb.setPageFormat(this.pageType, PageOrientation.PORTRAIT);
		
		int tamanioReporte 		= this.pageType.getWidth();
		this.tamanioEje 		= 150;
		int tamanioParaColumnas = tamanioReporte - tamanioEje; 
		
		this.cantidadColumnas 	= this.datosReporte.getCantidadMesesMostrar();
		
		this.tamanioColMes 		= tamanioParaColumnas/this.cantidadColumnas;
	}
	
	
	/**
	 * Crea la plantilla del reporte.
	 * 
	 * @author Cristhian Murillo.
	 * 
	 * @return ReportTemplateBuilder
	 */
	private ReportTemplateBuilder crearPlantillaReporte()
	{
		ReportTemplateBuilder reportTemplate;
		
		StyleBuilder rootStyle 				= stl.style().setPadding(1);
		StyleBuilder columnStyle 			= stl.style(rootStyle).setVerticalAlignment(VerticalAlignment.MIDDLE);
		StyleBuilder columnTitleStyle		= stl.style(columnStyle)
												.setTopBorder(stl.pen1Point().setLineWidth(1f)) 
										        .setHorizontalAlignment(HorizontalAlignment.CENTER)
										        .setBackgroundColor(Color.WHITE).bold().setFontSize(7);
		StyleBuilder boldStyle          	= stl.style(rootStyle).bold();
		StyleBuilder groupStyle				= stl.style(boldStyle).setHorizontalAlignment(HorizontalAlignment.LEFT);
		StyleBuilder subtotalStyle      	= stl.style(boldStyle).setTopBorder(stl.pen1Point().setLineWidth(0f));
		StyleBuilder crosstabGroupStyle     = stl.style(columnTitleStyle).setFontSize(7);
		StyleBuilder crosstabGroupTotalStyle= stl.style(columnTitleStyle).setFontSize(6);
		StyleBuilder crosstabGrandTotalStyle= 	stl.style(rootStyle).setVerticalAlignment(VerticalAlignment.MIDDLE)
												.setTopBorder(stl.pen1Point().setLineWidth(0f))
										        .setHorizontalAlignment(HorizontalAlignment.CENTER)
												.setBackgroundColor(Color.LIGHT_GRAY).setFontSize(5).bold();
		//StyleBuilder crosstabCellStyle      = stl.style(columnStyle).setBorder(stl.pen1Point().setLineWidth(3f));
		
		reportTemplate = template()
	        .setLocale(Locale.ENGLISH)
	        .setColumnStyle(columnStyle)
	        .setColumnTitleStyle(columnTitleStyle)
	        .setGroupStyle(groupStyle)
	        .setGroupTitleStyle(groupStyle)
	        .setSubtotalStyle(subtotalStyle)
	        .highlightDetailEvenRows()
	        .crosstabHighlightEvenRows() 						//intercalado
	        .setCrosstabGroupStyle(crosstabGroupStyle) 			//L inv
	        .setCrosstabGroupTotalStyle(crosstabGroupTotalStyle)
	        .setCrosstabGrandTotalStyle(crosstabGrandTotalStyle) // total de abajo
	        //.setCrosstabCellStyle(crosstabCellStyle)
	    ;
		
		return reportTemplate;
	}

	
	/**
	 * Crea el crosstab con los datos d ela consulta.
	 * 
	 * @param tipoA 
	 * 
	 * @autor Cristhian Murillo
	 * 
	 * @return CrosstabBuilder
	 */
	@SuppressWarnings("rawtypes")
	private TextColumnBuilder[] crearColumnasPlano(boolean tipoA)
	{
		this.listaTitulosColumnas  = new ArrayList<String>();
		TextColumnBuilder[] colums = new TextColumnBuilder[this.datosReporte.getCantidadMesesMostrar()*2+3];
		
		if(tipoA)
		{
			//Se definen las columnas que tendra el Reporte
			TextColumnBuilder<String> convenioColumn 	= col.column("Convenio"			, "convenio"		, type.stringType());
			TextColumnBuilder<String> nivelColumn  		= col.column("Nivel Atención"	, "nivelAtencion"	, type.stringType());
			TextColumnBuilder<String> tipoColumn  		= col.column("Tipo"				, "tipo"			, type.stringType());
			
			colums[0]=convenioColumn;	this.listaTitulosColumnas.add("Convenio");
			colums[1]=nivelColumn;		this.listaTitulosColumnas.add("Nivel Atención");
			colums[2]=tipoColumn;		this.listaTitulosColumnas.add("Tipo");
		}
		else
		{
			//Se definen las columnas que tendra el Reporte
			TextColumnBuilder<String> tipoColumn  		= col.column("Tipo"				, "tipo"			, type.stringType());
			TextColumnBuilder<String> nivelColumn  		= col.column("Nivel Atención"	, "nivelAtencion"	, type.stringType());
			TextColumnBuilder<String> descripcionColumn = col.column("Descripción"		, "descripcion"		, type.stringType());
			
			colums[0]=tipoColumn;			this.listaTitulosColumnas.add("Tipo");
			colums[1]=nivelColumn;			this.listaTitulosColumnas.add("Nivel Atención");
			colums[2]=descripcionColumn;	this.listaTitulosColumnas.add("Descripción");
		}
		
		ArrayList<DtoNivelesAtencionTotalServiciosArticulosValorizadosPorConvenio> listaArticulosServicios = new ArrayList<DtoNivelesAtencionTotalServiciosArticulosValorizadosPorConvenio>();
		listaArticulosServicios.addAll(this.datosReporte.getListaNivelesAtencionArticulos());
		listaArticulosServicios.addAll(this.datosReporte.getListaNivelesAtencionServicios());

		int numCol=3;
		ArrayList<Integer> listaMeses = new ArrayList<Integer>();
		
		for(DtoMesesTotalServiciosArticulosValorizadosPorConvenio keyMes:listaArticulosServicios.get(0).getListaMesesTotales())
		{
			if(!listaMeses.contains(keyMes.getNumeroMes()))
			{
				TextColumnBuilder<Integer> dymamicColumnCant = col.column(keyMes.getNombreMes()+"_Cant", keyMes.getNombreMes()+"Cant",  type.integerType());
				colums[numCol] = dymamicColumnCant;	
				this.listaTitulosColumnas.add(keyMes.getNombreMes()+"_Cant");
				
				numCol +=1;
				
				TextColumnBuilder<BigDecimal> dymamicColumnVal = col.column(keyMes.getNombreMes()+"_Vlr", keyMes.getNombreMes()+"Vlr",  type.bigDecimalType());
				colums[numCol]=dymamicColumnVal;	
				this.listaTitulosColumnas.add(keyMes.getNombreMes()+"_Vlr");
				
				numCol +=1;
				
				listaMeses.add(keyMes.getNumeroMes());
			}
		}
		
		return colums;
	}
	
	
	/**
	 * Crea el crosstab con los datos d ela consulta.
	 * @param header
	 * 
	 * @autor Cristhian Murillo
	 * 
	 * @return CrosstabBuilder
	 */
	private CrosstabBuilder crearCrosstab(String header)
	{
		CrosstabRowGroupBuilder<String> rowNivelGroup 		= ctab.rowGroup("nivelAtencion", String.class).setShowTotal(true).setTotalHeader("TOTAL CONVENIO").setHeaderWidth(35);
		CrosstabRowGroupBuilder<String> rowTipoGroup 		= ctab.rowGroup("tipo", String.class).setHeaderWidth(95).setShowTotal(false);
		
		CrosstabColumnGroupBuilder<String> columnMesGroup 	= ctab.columnGroup("nombreMes", String.class).setShowTotal(false).setHeaderStretchWithOverflow(true);
		CrosstabColumnGroupBuilder<Integer> columnMesIdGroup= ctab.columnGroup("numeroMes", Integer.class).setShowTotal(false).setOrderType(OrderType.ASCENDING).setHeaderStyle(stl.style(estiloOcultarNumeroMes()));
		
		CrosstabMeasureBuilder<Integer> cantidadMeasure 	= ctab.measure("Cant.", "cantidad", Integer.class, Calculation.SUM); 
		CrosstabMeasureBuilder<BigDecimal> valorMeasure 	= ctab.measure("Vlr.", "valor", BigDecimal.class, Calculation.SUM); valorMeasure.setPattern("#,###.##");
		
		cantidadMeasure.setStyle(EstilosReportesDinamicos.estiloDetalleRNoborde); 
		valorMeasure.setStyle(EstilosReportesDinamicos.estiloDetalleRNoborde);

		CrosstabBuilder crosstab = ctab.crosstab();
			crosstab.setCellWidth(this.tamanioColMes);
			crosstab.rowGroups(rowNivelGroup, rowTipoGroup);
		    crosstab.columnGroups(columnMesIdGroup, columnMesGroup);
		    crosstab.measures(cantidadMeasure, valorMeasure);
		    crosstab.headerCell(cmp.text(header).setStyle(EstilosReportesDinamicos.estiloCentradoMedioNoBorde)); 
		  
		return crosstab;
	}
	
	
	/**
	 * Organiza los meses y oculta el número del mes 
	 * @return StyleBuilder
	 *
	 * @autor Cristhian Murillo
	 */
	private StyleBuilder estiloOcultarNumeroMes()
	{
		return stl.style(stl.style(stl.style().setPadding(0)))
			.setBorder(stl.pen1Point().setLineWidth(0f).setLineColor(Color.BLACK)) 
			.setBackgroundColor(Color.WHITE).setForegroudColor(Color.WHITE); 
	}
	
	
	/**
	 * Crea el componente de las margenes del reporte de Ordenes 
	 * de Capitación Subcontratada para todos los formatos. 
	 * @return MarginBuilder
	 */
	public MarginBuilder crearMagenesSubReporte()
	{
		MarginBuilder margin;
		
		margin = margin()
	        .setTop(0)
			.setBottom(5)
	        .setLeft(0)
	        .setRight(0)
	    ;
		
		return margin;
	}
	
	
	/**
	 * Crea el encabezado del reporte (títulos y subtítulos).
	 * 
	 * @param tipoA
	 * 
	 * @author Cristhian Murillo
	 * 
	 * @return ComponentBuilder
	 */
	public ComponentBuilder<?, ?> crearComponenteEncabezadoPlano(boolean tipoA) 
	{
		String lineaTitulo	= IConstantesReporte.ordCapSub;
		String fecha		= "Fecha: "+this.datosReporte.getFecha();
		String convenio		= this.datosReporte.getNombreConvenio(); 
		String contrato		= this.datosReporte.getNombreContrato();
		String fechaconveniocontrato=fecha+"      Convenio: "+convenio;
		
		if(contrato != null){
			fechaconveniocontrato+="        Contrato:"+contrato;
		}
		
		String lineaFecha = "";
		if (tipoA){
			lineaFecha = fecha;
		} else{
			lineaFecha = fechaconveniocontrato;
		}
		
		ComponentBuilder<?, ?> dynamicReportsComponent = null;
		
		/*
		HorizontalListBuilder horizontalListBuilder = cmp.horizontalList();
		for (String nombreColumna : this.listaTitulosColumnas) {
			horizontalListBuilder.add(cmp.text(nombreColumna)); 
		}
		*/	
		dynamicReportsComponent = 
			cmp.verticalList(
				cmp.text("Reporte: "+lineaTitulo),
				cmp.text(lineaFecha)
				//,horizontalListBuilder
			);
		
		return dynamicReportsComponent;
	}
	
}