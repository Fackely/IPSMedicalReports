package com.servinte.axioma.generadorReporte.tesoreria.impresionEntregaATransportadora;

import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;
import static net.sf.dynamicreports.report.builder.DynamicReports.stl;

import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.Date;

import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.builder.component.HorizontalListBuilder;
import net.sf.dynamicreports.report.builder.component.VerticalListBuilder;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.constant.HorizontalAlignment;
import util.ConstantesIntegridadDominio;

import com.servinte.axioma.dto.tesoreria.DtoFiltroReportesArqueosCierres;

/**
 * Esta clase genera titulos, encabezados, pie de página de los reportes
 * Impresión Entrega Transportadora DCU 1025
 * @author Fabián Becerra
 *
 */
public class DetallesGeneralesImpresionEntregaTransportadora {

	public static final ComponentBuilder<?, ?> footerComponent;
	private DtoFiltroReportesArqueosCierres filtroEntregaTransportadora;
	public static final StyleBuilder stNegrilla;
	public static final StyleBuilder stNormal;
	public static final StyleBuilder stTitulo;
	
	/**
	 * Método constructor de la clase
	 * @param filtroArqueoCaja contiene información de la consulta
	 */
	public DetallesGeneralesImpresionEntregaTransportadora(DtoFiltroReportesArqueosCierres filtroEntregaTransportadora) {
		this.filtroEntregaTransportadora=filtroEntregaTransportadora;
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
		String ubicacionLogo = filtroEntregaTransportadora.getUbicacionLogo();
		String rutaLogo = "../"+filtroEntregaTransportadora.getRutaLogo();
		//boolean existeLogo = existeLogo(rutaLogo);
		HorizontalListBuilder bandaImagen = null;
		
		//if (existeLogo) {
			if (ubicacionLogo.equals(ConstantesIntegridadDominio.acronimoUbicacionDerecha)) {
				bandaImagen=	cmp.horizontalFlowList(
										cmp.verticalList(
												cmp.text("").setFixedWidth(130)
										),
										cmp.verticalList(
												cmp.text(filtroEntregaTransportadora.getRazonSocial()).setStyle(stTitulo),
												cmp.text("NIT "+filtroEntregaTransportadora.getNit()).setStyle(stTitulo),
												cmp.text(filtroEntregaTransportadora.getActividadEconomica()).setStyle(stTitulo),
												cmp.text("Dirección: "+filtroEntregaTransportadora.getDireccion()+", Teléfono: "+filtroEntregaTransportadora.getTelefono()).setStyle(stTitulo),
												cmp.text("Centro de atención: "+filtroEntregaTransportadora.getCentroAtencion()).setStyle(stTitulo)
										),
										cmp.verticalList(
												cmp.image(rutaLogo).setFixedDimension(130, 50)
										)
								);
			}else
			{
				bandaImagen=	cmp.horizontalFlowList(
										cmp.verticalList(
												cmp.image(rutaLogo).setFixedDimension(130, 50)
										),
										cmp.verticalList(
												cmp.text(filtroEntregaTransportadora.getRazonSocial()).setStyle(stTitulo),
												cmp.text("NIT "+filtroEntregaTransportadora.getNit()).setStyle(stTitulo),
												cmp.text(filtroEntregaTransportadora.getActividadEconomica()).setStyle(stTitulo),
												cmp.text("Dirección: "+filtroEntregaTransportadora.getDireccion()+", Teléfono: "+filtroEntregaTransportadora.getTelefono()).setStyle(stTitulo),
												cmp.text("Centro de atención: "+filtroEntregaTransportadora.getCentroAtencion()).setStyle(stTitulo)
										),
										cmp.verticalList(
												cmp.text("").setFixedWidth(130)
										)
								);
			}
		/*}else{
			bandaImagen=	cmp.horizontalFlowList(
										cmp.verticalList(
												cmp.text("")
										),
										cmp.verticalList(
												cmp.text(filtroConsultaOrdenes.getRazonSocial()).setStyle(stTitulo),
												cmp.text("NIT "+filtroConsultaOrdenes.getNit()).setStyle(stTitulo),
												cmp.text("TOTAL ORDENES AUTORIZADAS A ENTIDADES SUBCONTRATADAS").setStyle(stTitulo),
												cmp.text("Tipo Consulta : "+filtroConsultaOrdenes.getNombreTipoConsulta()).setStyle(stTitulo)
										),
										cmp.verticalList(
												cmp.text("")
										)
								);
		}*/
		
		return bandaImagen;
	}
	
	
	/**
	 * Método que crea el encabezado del reporte
	 * @return VerticalListBuilder componente que contiene el encabezado
	 */
	public VerticalListBuilder crearEncabezadoReporte()
	{
		VerticalListBuilder encabezado= cmp.verticalList(
												crearBandaLogos(),
												cmp.text("")
		   	    		  				);	
		
		return encabezado;
	}
	
	
	public VerticalListBuilder crearEncabezado()
	{
		VerticalListBuilder encabezado= cmp.verticalList(
											cmp.horizontalList(
													cmp.text("ENTREGA A TRANSPORTADORA DE VALORES").setStyle(stl.style().bold().setFontSize(9)
															.setBackgroundColor(Color.LIGHT_GRAY)).setHorizontalAlignment(HorizontalAlignment.CENTER)
											),
											cmp.horizontalList(
													cmp.text("No Entrega: ").setStyle(stNegrilla).setWidth(35),
													cmp.text(filtroEntregaTransportadora.getNroConsecutivo()).setStyle(stl.style()).setHorizontalAlignment(HorizontalAlignment.LEFT).setWidth(150),
													cmp.text("Fecha - Hora de Entrega: ").setStyle(stNegrilla).setWidth(80),
													cmp.text(filtroEntregaTransportadora.getFechaGeneracion()+ " - "+
															filtroEntregaTransportadora.getHoraGeneracion()).setStyle(stl.style()).setHorizontalAlignment(HorizontalAlignment.LEFT).setWidth(100)
											),
											cmp.horizontalList(
													cmp.text("Caja: ").setStyle(stNegrilla).setWidth(15),
													cmp.text(filtroEntregaTransportadora.getCaja()).setStyle(stl.style().setFontSize(9)).setHorizontalAlignment(HorizontalAlignment.LEFT).setWidth(166),
													cmp.text("Cajero: ").setStyle(stNegrilla).setWidth(20),
													cmp.text(filtroEntregaTransportadora.getCajero()).setStyle(stl.style()).setHorizontalAlignment(HorizontalAlignment.LEFT).setWidth(140)
											),
											cmp.horizontalList(
													cmp.text("Transportadora: ").setStyle(stNegrilla).setWidth(40),
													cmp.text(filtroEntregaTransportadora.getTransportadora()).setStyle(stl.style()).setHorizontalAlignment(HorizontalAlignment.LEFT).setWidth(120),
													cmp.text("NIT: ").setStyle(stNegrilla).setWidth(20),
													cmp.text(filtroEntregaTransportadora.getNitTransportadora()).setStyle(stl.style()).setWidth(120).setHorizontalAlignment(HorizontalAlignment.LEFT)
											),
											cmp.horizontalList(
													cmp.text("Responsable Recibe: ").setStyle(stNegrilla).setWidth(58),
													cmp.text(filtroEntregaTransportadora.getResponsableRecibe()).setStyle(stl.style()).setHorizontalAlignment(HorizontalAlignment.LEFT).setWidth(116),
													cmp.text("No. Carné: ").setStyle(stNegrilla).setWidth(32),
													cmp.text(filtroEntregaTransportadora.getNumCarnet()).setStyle(stl.style()).setHorizontalAlignment(HorizontalAlignment.LEFT).setWidth(120)
											),
											cmp.horizontalList(
													cmp.text("No. Guía: ").setStyle(stNegrilla).setWidth(55),
													cmp.text(filtroEntregaTransportadora.getNumGuia()).setStyle(stl.style()).setWidth(300).setHorizontalAlignment(HorizontalAlignment.LEFT),
													cmp.text("No. Carro: ").setStyle(stNegrilla).setWidth(60),
													cmp.text(filtroEntregaTransportadora.getNumCarro()).setStyle(stl.style()).setWidth(250).setHorizontalAlignment(HorizontalAlignment.LEFT)
											),
											cmp.horizontalList(
													cmp.text("Entidad Financiera: ").setStyle(stNegrilla).setWidth(90),
													cmp.text(filtroEntregaTransportadora.getEntidadFinanciera()).setStyle(stl.style()).setWidth(250).setHorizontalAlignment(HorizontalAlignment.LEFT).setWidth(120),
													cmp.text("Tipo y No. Cta Bancaria: ").setStyle(stNegrilla).setWidth(120),
													cmp.text(filtroEntregaTransportadora.getTipoNroCuentaBancaria()).setStyle(stl.style()).setWidth(250).setHorizontalAlignment(HorizontalAlignment.LEFT)
											),
											cmp.text("")
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
				 cmp.text(" ").setWidth(10),
				 cmp.text("Impresión: ").setHorizontalAlignment(HorizontalAlignment.RIGHT).setStyle(stNegrilla).setWidth(50),
				 cmp.text(sdf.format(new Date())).setStyle(stNormal).setWidth(80),
				 cmp.text(filtroEntregaTransportadora.getNombreUsuario()).setHorizontalAlignment(HorizontalAlignment.CENTER).setStyle(stNormal).setWidth(120),
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
	
		
	/**
	 * Método que crea las firma de los reportes
	 * @return VerticalListBuilder componente que contiene las firmas
	 */
	public VerticalListBuilder crearFirmas()
	{
		HorizontalListBuilder lineas= cmp.horizontalList(
				 cmp.text("").setWidth(10),
				 cmp.text("______________________________________").setStyle(stNegrilla).setHorizontalAlignment(HorizontalAlignment.CENTER),
				 cmp.text("").setWidth(20),
				 cmp.text("_____________________________________").setStyle(stNegrilla).setHorizontalAlignment(HorizontalAlignment.CENTER),
				 cmp.text("").setWidth(20)
				 );
		
		HorizontalListBuilder nombres= cmp.horizontalList(
				 cmp.text("").setWidth(10),
				 cmp.text("Firma Responsable Transportadora de Valores").setStyle(stNegrilla).setHorizontalAlignment(HorizontalAlignment.CENTER),
				 cmp.text("").setWidth(20),
				 cmp.text("Firma Cajero Entrega").setStyle(stNegrilla).setHorizontalAlignment(HorizontalAlignment.CENTER),
				 cmp.text("").setWidth(20)
				 );
		
		VerticalListBuilder firmas = cmp.verticalList(
				   cmp.text(""),
				   cmp.text(""),
				   lineas,
				   nombres
				);
		return firmas;
	}
	
}
