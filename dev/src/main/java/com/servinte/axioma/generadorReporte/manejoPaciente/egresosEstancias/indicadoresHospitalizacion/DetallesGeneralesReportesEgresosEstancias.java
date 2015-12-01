package com.servinte.axioma.generadorReporte.manejoPaciente.egresosEstancias.indicadoresHospitalizacion;

import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;
import static net.sf.dynamicreports.report.builder.DynamicReports.stl;

import java.text.SimpleDateFormat;
import java.util.Date;

import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.builder.component.HorizontalListBuilder;
import net.sf.dynamicreports.report.builder.component.VerticalListBuilder;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.constant.HorizontalAlignment;

import com.princetonsa.dto.manejoPaciente.DtoFiltroReporteIndicadoresHospitalizacion;

public class DetallesGeneralesReportesEgresosEstancias {

	public static final ComponentBuilder<?, ?> footerComponent;
	private DtoFiltroReporteIndicadoresHospitalizacion filtroReporte;
	public static final StyleBuilder stNegrilla;
	public static final StyleBuilder stNormal;
	public static final StyleBuilder stTitulo;
	
	/**
	 * Método constructor de la clase
	 * @param filtroArqueoCaja contiene información de la consulta
	 */
	public DetallesGeneralesReportesEgresosEstancias(DtoFiltroReporteIndicadoresHospitalizacion filtroReporte) {
		this.filtroReporte=filtroReporte;
	}
	
	/**
	 * Atributos estáticos
	 */
	static {
		
		stNegrilla= stl.style().bold().setFontSize(9);
		stNormal= stl.style().setFontSize(9);
		stTitulo = stl.style().bold().setFontSize(10).setHorizontalAlignment(HorizontalAlignment.CENTER);
		
		footerComponent = 
				 cmp.pageXofY().setFormatExpression("{0} de {1}").setHorizontalAlignment(HorizontalAlignment.LEFT).setStyle(stNormal).setWidth(20)
				 ;
	}
	
	/**
	 * Este método se encarga de crear en el encabezado el logo y su ubicación
	 * @return HorizontalListBuilder banda del reporte que contiene el logo 
	 */
	public HorizontalListBuilder crearBandaLogos()
	{
		String rutaLogo = "../"+filtroReporte.getRutaLogo();
		HorizontalListBuilder bandaImagen = null;
		
		bandaImagen=	cmp.horizontalFlowList(
								cmp.verticalList(
										cmp.text("").setFixedWidth(130)
								),
								cmp.verticalList(
										cmp.text(filtroReporte.getRazonSocial()).setStyle(stTitulo),
										cmp.text("NIT "+filtroReporte.getNit()).setStyle(stTitulo),
										cmp.text(filtroReporte.getActividadEconomica()).setStyle(stTitulo),
										cmp.text(filtroReporte.getDireccion()+", "+filtroReporte.getTelefono()).setStyle(stTitulo)
								),
								cmp.verticalList(
										cmp.image(rutaLogo).setFixedDimension(130, 50)
								)
							);
		
		return bandaImagen;
	}
	
	
	public VerticalListBuilder crearEncabezado()
	{
		VerticalListBuilder encabezado= cmp.verticalList(
											cmp.horizontalList(
													cmp.text("Centro Atención: ").setStyle(stNegrilla).setWidth(60),
													cmp.text(filtroReporte.getDescripcionCentroAtencion()).setStyle(stNormal).setHorizontalAlignment(HorizontalAlignment.LEFT).setWidth(150),
													cmp.text("Fecha Inicial: ").setStyle(stNegrilla).setHorizontalAlignment(HorizontalAlignment.LEFT).setWidth(45),
													cmp.text(filtroReporte.getFechaInicial()).setStyle(stNormal).setHorizontalAlignment(HorizontalAlignment.LEFT).setWidth(60),
													cmp.text("Fecha Final: ").setStyle(stNegrilla).setHorizontalAlignment(HorizontalAlignment.LEFT).setWidth(45),
													cmp.text(filtroReporte.getFechaFinal()).setStyle(stNormal).setHorizontalAlignment(HorizontalAlignment.LEFT).setWidth(60)
											),
											cmp.horizontalList(
													cmp.text("Via de Ingreso: ").setStyle(stNegrilla).setWidth(45),
													cmp.text(filtroReporte.getDescripcionViaIngreso()).setStyle(stNormal).setHorizontalAlignment(HorizontalAlignment.LEFT).setWidth(110),
													cmp.text("Tipo Paciente: ").setStyle(stNegrilla).setWidth(40),
													cmp.text(filtroReporte.getDescripcionTipoPaciente()).setStyle(stNormal).setWidth(115).setHorizontalAlignment(HorizontalAlignment.LEFT)
											),
											cmp.horizontalList(
													cmp.text("Convenio: ").setStyle(stNegrilla).setWidth(30),
													cmp.text(filtroReporte.getDescripcionConvenio()).setStyle(stNormal).setWidth(130).setHorizontalAlignment(HorizontalAlignment.LEFT),
													cmp.text("Sexo: ").setStyle(stNegrilla).setWidth(20),
													cmp.text(filtroReporte.getDescripcionSexo()).setStyle(stNormal).setHorizontalAlignment(HorizontalAlignment.LEFT).setWidth(140)
											),
											cmp.horizontalList(
													cmp.text("Diagnóstico(s) de egreso: ").setStyle(stNegrilla).setWidth(80),
													cmp.text(filtroReporte.getDescripcionDiagnosticosEgreso()).setStyle(stNormal).setHorizontalAlignment(HorizontalAlignment.LEFT).setWidth(295)
											),
											cmp.text("")
										);
		
		return encabezado;
	}
	
	
	/**
	 * Método que crea el encabezado de los reportes en archivo plano
	 * @return VerticalListBuilder componente que contiene el encabezado
	 */
	public VerticalListBuilder crearEncabezadoReportePlano()
	{
		VerticalListBuilder encabezado= cmp.verticalList(
												cmp.text("Reporte: Indicadores Hospitalización"),
												cmp.text("Centro Atención: "+filtroReporte.getDescripcionCentroAtencion()+"|"+
														"Rango Fechas: "+filtroReporte.getFechaInicial()+" - "+filtroReporte.getFechaFinal()),
												cmp.text("Via de Ingreso: "+filtroReporte.getDescripcionViaIngreso()+"|"+
														"Tipo Paciente: "+filtroReporte.getDescripcionTipoPaciente()),
												cmp.text("Convenio: "+filtroReporte.getDescripcionConvenio()+"|"+
														"Sexo: "+filtroReporte.getDescripcionSexo()),
												cmp.text("Diagnóstico(s) de egreso: "+filtroReporte.getDescripcionDiagnosticosEgreso())
		   	    		  				);	
		
		return encabezado;
	}
	
	
	/**
	 * Método que crea el pie de página del reporte 
	 * @return HorizontalListBuilder componente que contiene el pie de página
	 */
	public VerticalListBuilder crearPiePaginaReporte()
	{
		
		SimpleDateFormat sdf= new SimpleDateFormat("dd/MM/yyyy HH:mm");
		
		HorizontalListBuilder resumenImpresion= cmp.horizontalFlowList(
				 cmp.text(" ").setWidth(60),
				 cmp.text(sdf.format(new Date())).setStyle(stNormal).setWidth(80),
				 cmp.text(filtroReporte.getNombreUsuario()).setHorizontalAlignment(HorizontalAlignment.CENTER).setStyle(stNormal).setWidth(120),
				 cmp.text("Pág. ").setHorizontalAlignment(HorizontalAlignment.RIGHT).setStyle(stNegrilla).setWidth(20),
				 footerComponent,
				 cmp.text(" ").setWidth(50)
				 );
		
		VerticalListBuilder piePagina= cmp.verticalList(
				cmp.text(" "),
				resumenImpresion
				);
		
		return piePagina;
	}
	
		
}
