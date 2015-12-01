package com.servinte.axioma.generadorReporte.tesoreria.impresionTrasladoEntreCajas;

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
import util.ValoresPorDefecto;

import com.princetonsa.dto.tesoreria.DtoConsultaTrasladosCajasRecaudo;
import com.servinte.axioma.dto.tesoreria.DtoFiltroReportesArqueosCierres;

/**
 * Esta clase genera titulos, encabezados, pie de página de los reportes
 * Impresión Traslado Entre Cajas DCU 1024
 * @author Fabián Becerra
 *
 */
public class DetallesGeneralesImpresionTrasladoEntreCajas {
	
	private DtoFiltroReportesArqueosCierres filtroTraslado;
	private DtoConsultaTrasladosCajasRecaudo dtoConsulta;
	public static final ComponentBuilder<?, ?> footerComponent;
	public static final StyleBuilder stNegrilla;
	public static final StyleBuilder stNormal;
	public static final StyleBuilder stTitulo;
	
	/**
	 * Método constructor de la clase
	 * @param filtroTraslado contiene información de la consulta
	 */
	public DetallesGeneralesImpresionTrasladoEntreCajas(DtoFiltroReportesArqueosCierres filtroTraslado,DtoConsultaTrasladosCajasRecaudo dtoConsulta) {
		this.filtroTraslado=filtroTraslado;
		this.dtoConsulta=dtoConsulta;
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
		String ubicacionLogo = filtroTraslado.getUbicacionLogo();
		String rutaLogo = "../"+filtroTraslado.getRutaLogo();
		//boolean existeLogo = existeLogo(rutaLogo);
		HorizontalListBuilder bandaImagen = null;
		
		//if (existeLogo) {
			if (ubicacionLogo.equals(ConstantesIntegridadDominio.acronimoUbicacionDerecha)) {
				bandaImagen=	cmp.horizontalFlowList(
										cmp.verticalList(
												cmp.text("").setFixedWidth(130)
										),
										cmp.verticalList(
												cmp.text(filtroTraslado.getRazonSocial()).setStyle(stTitulo),
												cmp.text("NIT "+filtroTraslado.getNit()).setStyle(stTitulo),
												cmp.text(filtroTraslado.getActividadEconomica()).setStyle(stTitulo),
												cmp.text("Dirección: "+filtroTraslado.getDireccion()).setStyle(stTitulo),
												cmp.text("Teléfono: "+filtroTraslado.getTelefono()).setStyle(stTitulo),
												cmp.text("Centro de atención: "+filtroTraslado.getCentroAtencion()).setStyle(stTitulo)
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
												cmp.text(filtroTraslado.getRazonSocial()).setStyle(stTitulo),
												cmp.text("NIT "+filtroTraslado.getNit()).setStyle(stTitulo),
												cmp.text(filtroTraslado.getActividadEconomica()).setStyle(stTitulo),
												cmp.text("Dirección: "+filtroTraslado.getDireccion()).setStyle(stTitulo),
												cmp.text("Teléfono: "+filtroTraslado.getTelefono()).setStyle(stTitulo),
												cmp.text("Centro de atención: "+filtroTraslado.getCentroAtencion()).setStyle(stTitulo)
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
													cmp.text("SOLICITUD TRASLADO A CAJA DE RECAUDO").setStyle(stNegrilla).setHorizontalAlignment(HorizontalAlignment.CENTER)
											),
											cmp.horizontalList(
													cmp.text("Solicitud Trasl.: ").setStyle(stl.style().bold()).setWidth(20),
													cmp.text(filtroTraslado.getNroConsecutivo()).setStyle(stl.style()).setHorizontalAlignment(HorizontalAlignment.LEFT).setWidth(20),
													cmp.text("Fecha/hora Solicitud Trasl.: ").setStyle(stl.style().bold()).setWidth(35),
													cmp.text(filtroTraslado.getFechaGeneracion()+ " - "+
															filtroTraslado.getHoraGeneracion()).setStyle(stl.style()).setHorizontalAlignment(HorizontalAlignment.LEFT).setWidth(30),
													cmp.text("Estado: ").setStyle(stl.style().bold()).setWidth(10),
													cmp.text(filtroTraslado.getEstadoSolicitud()).setStyle(stl.style()).setWidth(25).setHorizontalAlignment(HorizontalAlignment.LEFT)
											),
											cmp.horizontalList(
													cmp.text("Caja Traslada: ").setStyle(stl.style().bold()).setWidth(40),
													cmp.text(filtroTraslado.getCajaTraslada()).setStyle(stl.style()).setWidth(69).setHorizontalAlignment(HorizontalAlignment.LEFT),
													cmp.text("Cajero Traslada: ").setStyle(stl.style().bold()).setWidth(40),
													cmp.text(filtroTraslado.getCajeroTraslada()).setStyle(stl.style()).setHorizontalAlignment(HorizontalAlignment.LEFT)
											),
											(dtoConsulta.getEstadoSolicitud().equals((String)ValoresPorDefecto.getIntegridadDominio(
				    								ConstantesIntegridadDominio.acronimoEstadoSolicitudTrasladoCajaSolicitado))
				    												?cmp.text(""):
																	cmp.horizontalList(
																			cmp.text("Fecha/Hora Aceptación: ").setStyle(stl.style().bold()).setWidth(40),
																			cmp.text(filtroTraslado.getFechaAceptacion()+ " - "+
																				filtroTraslado.getHoraAceptacion()).setStyle(stl.style()).setHorizontalAlignment(HorizontalAlignment.LEFT).setWidth(35),
																			cmp.text("Caja Acepta:  ").setStyle(stl.style().bold()).setWidth(20),
																			cmp.text(filtroTraslado.getCajaAcepta()).setStyle(stl.style()).setHorizontalAlignment(HorizontalAlignment.LEFT).setWidth(25),
																			cmp.text("Cajero Acepta:   ").setStyle(stl.style().bold()).setWidth(25),
																			cmp.text(filtroTraslado.getCajeroAcepta()).setStyle(stl.style()).setHorizontalAlignment(HorizontalAlignment.LEFT).setWidth(30)	
																	)
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
				 cmp.text(filtroTraslado.getNombreUsuario()).setHorizontalAlignment(HorizontalAlignment.CENTER).setStyle(stNormal).setWidth(120),
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
				 cmp.text("Cajero Entrega.").setStyle(stNegrilla).setHorizontalAlignment(HorizontalAlignment.CENTER),
				 cmp.text("").setWidth(20),
				 cmp.text("Testigo Entrega.").setStyle(stNegrilla).setHorizontalAlignment(HorizontalAlignment.CENTER),
				 cmp.text("").setWidth(20)
				 );
		
		HorizontalListBuilder nombres2= cmp.horizontalList(
				 cmp.text("").setWidth(10),
				 cmp.text("Cajero Recibe.").setStyle(stNegrilla).setHorizontalAlignment(HorizontalAlignment.CENTER),
				 cmp.text("").setWidth(20),
				 cmp.text("Testigo Recibes.").setStyle(stNegrilla).setHorizontalAlignment(HorizontalAlignment.CENTER),
				 cmp.text("").setWidth(20)
				 );
		
		VerticalListBuilder firmas = cmp.verticalList(
				   cmp.text(""),
				   cmp.text(""),
				   lineas,
				   nombres,
				   cmp.text(""),
				   cmp.text(""),
				   lineas,
				   nombres2
				);
		return firmas;
	}
	

}
