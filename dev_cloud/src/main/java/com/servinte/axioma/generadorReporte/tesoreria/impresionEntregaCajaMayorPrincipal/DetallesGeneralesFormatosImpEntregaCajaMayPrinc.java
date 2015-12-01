package com.servinte.axioma.generadorReporte.tesoreria.impresionEntregaCajaMayorPrincipal;

import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;
import static net.sf.dynamicreports.report.builder.DynamicReports.stl;

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
 * Impresión Entrega Caja Mayor Principal resumido y detallado DCU 1039
 * @author Fabián Becerra
 *
 */
public class DetallesGeneralesFormatosImpEntregaCajaMayPrinc {

	private DtoFiltroReportesArqueosCierres filtroEntregaCajaMayor;
	public static final ComponentBuilder<?, ?> footerComponent;
	public static final StyleBuilder stNegrilla;
	public static final StyleBuilder stNormal;
	public static final StyleBuilder stTitulo;
	
	/**
	 * Método constructor de la clase
	 * @param filtroArqueoCaja contiene información de la consulta
	 */
	public DetallesGeneralesFormatosImpEntregaCajaMayPrinc(DtoFiltroReportesArqueosCierres filtroEntregaCajaMayor) {
		this.filtroEntregaCajaMayor=filtroEntregaCajaMayor;
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
		String ubicacionLogo = filtroEntregaCajaMayor.getUbicacionLogo();
		String rutaLogo = "../"+filtroEntregaCajaMayor.getRutaLogo();
		//boolean existeLogo = existeLogo(rutaLogo);
		HorizontalListBuilder bandaImagen = null;
		
		//if (existeLogo) {
			if (ubicacionLogo.equals(ConstantesIntegridadDominio.acronimoUbicacionDerecha)) {
				bandaImagen=	cmp.horizontalFlowList(
										cmp.verticalList(
												cmp.text("").setFixedWidth(130)
										),
										cmp.verticalList(
												cmp.text(filtroEntregaCajaMayor.getRazonSocial()).setStyle(stTitulo),
												cmp.text("NIT "+filtroEntregaCajaMayor.getNit()).setStyle(stTitulo),
												cmp.text(filtroEntregaCajaMayor.getActividadEconomica()).setStyle(stTitulo),
												cmp.text("Dirección: "+filtroEntregaCajaMayor.getDireccion()+", Teléfono: "+filtroEntregaCajaMayor.getTelefono()).setStyle(stTitulo),
												cmp.text("Centro de atención: "+filtroEntregaCajaMayor.getCentroAtencion()).setStyle(stTitulo)
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
												cmp.text(filtroEntregaCajaMayor.getRazonSocial()).setStyle(stTitulo),
												cmp.text("NIT "+filtroEntregaCajaMayor.getNit()).setStyle(stTitulo),
												cmp.text(filtroEntregaCajaMayor.getActividadEconomica()).setStyle(stTitulo),
												cmp.text("Dirección: "+filtroEntregaCajaMayor.getDireccion()+", Teléfono: "+filtroEntregaCajaMayor.getTelefono()).setStyle(stTitulo),
												cmp.text("Centro de atención: "+filtroEntregaCajaMayor.getCentroAtencion()).setStyle(stTitulo)
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
													cmp.text("ENTREGA CAJA MAYOR PRINCIPAL").setStyle(stNegrilla).setHorizontalAlignment(HorizontalAlignment.CENTER)
											),
											cmp.horizontalList(
													cmp.text("Nro Consecutivo: ").setStyle(stNegrilla).setWidth(70),
													cmp.text(filtroEntregaCajaMayor.getNroConsecutivo()).setStyle(stl.style()).setWidth(150).setHorizontalAlignment(HorizontalAlignment.LEFT),
													cmp.text("Fecha y Hora de Generación Arqueo: ").setStyle(stNegrilla).setWidth(130),
													cmp.text(filtroEntregaCajaMayor.getFechaGeneracion()+ " - "+
															filtroEntregaCajaMayor.getHoraGeneracion()).setStyle(stl.style()).setHorizontalAlignment(HorizontalAlignment.LEFT)
											),
											cmp.horizontalList(
													cmp.text("Usuario que Generó Arqueo: ").setStyle(stNegrilla).setWidth(65),
													cmp.text(filtroEntregaCajaMayor.getUsuarioGeneracion()).setStyle(stl.style()).setWidth(69).setHorizontalAlignment(HorizontalAlignment.LEFT),
													cmp.text("Fecha Arqueo: ").setStyle(stNegrilla).setWidth(40),
													cmp.text(filtroEntregaCajaMayor.getFechaCierre()).setStyle(stl.style()).setHorizontalAlignment(HorizontalAlignment.LEFT)
											),
											cmp.horizontalList(
													cmp.text("Cajero: ").setStyle(stNegrilla).setWidth(14),
													cmp.text(filtroEntregaCajaMayor.getCajero()).setStyle(stl.style()).setWidth(90).setHorizontalAlignment(HorizontalAlignment.LEFT),
													cmp.text("Caja: ").setStyle(stNegrilla).setWidth(10),
													cmp.text(filtroEntregaCajaMayor.getCaja()).setStyle(stl.style()).setHorizontalAlignment(HorizontalAlignment.LEFT)
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
		
		String impresion=(filtroEntregaCajaMayor.isEsConsulta()?"Reimpresión: ":"Impresión: ");
		
		HorizontalListBuilder resumenImpresion= cmp.horizontalFlowList(
				 cmp.text(" ").setWidth(10),
				 cmp.text(impresion).setHorizontalAlignment(HorizontalAlignment.RIGHT).setStyle(stNegrilla).setWidth(50),
				 cmp.text(sdf.format(new Date())).setStyle(stNormal).setWidth(80),
				 cmp.text(filtroEntregaCajaMayor.getNombreUsuario()).setHorizontalAlignment(HorizontalAlignment.CENTER).setStyle(stNormal).setWidth(120),
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
				 cmp.text("____________________________").setStyle(stNegrilla).setHorizontalAlignment(HorizontalAlignment.CENTER),
				 cmp.text("").setWidth(20),
				 cmp.text("____________________________").setStyle(stNegrilla).setHorizontalAlignment(HorizontalAlignment.CENTER),
				 cmp.text("").setWidth(20)
				 );
		
		HorizontalListBuilder nombres= cmp.horizontalList(
				 cmp.text("").setWidth(10),
				 cmp.text("Cajero Genera Entrega").setStyle(stNegrilla).setHorizontalAlignment(HorizontalAlignment.CENTER),
				 cmp.text("").setWidth(20),
				 cmp.text("Usuario Caja Mayor/Principal que Recibe").setStyle(stNegrilla).setHorizontalAlignment(HorizontalAlignment.CENTER),
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
