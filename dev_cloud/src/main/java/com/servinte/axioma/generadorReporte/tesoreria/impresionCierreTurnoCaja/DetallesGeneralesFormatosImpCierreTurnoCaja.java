package com.servinte.axioma.generadorReporte.tesoreria.impresionCierreTurnoCaja;

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
 * Cierre Turno Caja resumido y detallado DCU 1038
 * @author Fabián Becerra
 *
 */
public class DetallesGeneralesFormatosImpCierreTurnoCaja {
	
	public static final ComponentBuilder<?, ?> footerComponent;
	private DtoFiltroReportesArqueosCierres filtroCierreTurno;
	public static final StyleBuilder stNegrilla;
	public static final StyleBuilder stNormal;
	public static final StyleBuilder stTitulo;
	
	/**
	 * Método constructor de la clase
	 * @param filtroArqueoCaja contiene información de la consulta
	 */
	public DetallesGeneralesFormatosImpCierreTurnoCaja(DtoFiltroReportesArqueosCierres filtroCierreTurno) {
		this.filtroCierreTurno=filtroCierreTurno;
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
		String ubicacionLogo = filtroCierreTurno.getUbicacionLogo();
		String rutaLogo = "../"+filtroCierreTurno.getRutaLogo();
		//boolean existeLogo = existeLogo(rutaLogo);
		HorizontalListBuilder bandaImagen = null;
		
		//if (existeLogo) {
			if (ubicacionLogo.equals(ConstantesIntegridadDominio.acronimoUbicacionDerecha)) {
				bandaImagen=	cmp.horizontalFlowList(
										cmp.verticalList(
												cmp.text("").setFixedWidth(130)
										),
										cmp.verticalList(
												cmp.text(filtroCierreTurno.getRazonSocial()).setStyle(stTitulo),
												cmp.text("NIT "+filtroCierreTurno.getNit()).setStyle(stTitulo),
												cmp.text(filtroCierreTurno.getActividadEconomica()).setStyle(stTitulo),
												cmp.text("Dirección: "+filtroCierreTurno.getDireccion()+", Teléfono: "+filtroCierreTurno.getTelefono()).setStyle(stTitulo),
												cmp.text("Centro de atención: "+filtroCierreTurno.getCentroAtencion()).setStyle(stTitulo)
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
												cmp.text(filtroCierreTurno.getRazonSocial()).setStyle(stTitulo),
												cmp.text("NIT "+filtroCierreTurno.getNit()).setStyle(stTitulo),
												cmp.text(filtroCierreTurno.getActividadEconomica()).setStyle(stTitulo),
												cmp.text("Dirección: "+filtroCierreTurno.getDireccion()+", Teléfono: "+filtroCierreTurno.getTelefono()).setStyle(stTitulo),
												cmp.text("Centro de atención: "+filtroCierreTurno.getCentroAtencion()).setStyle(stTitulo)
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
													cmp.text("CIERRE TURNO DE CAJA").setStyle(stNegrilla).setHorizontalAlignment(HorizontalAlignment.CENTER)
											),
											cmp.horizontalList(
													cmp.text("Nro Consecutivo Cierre: ").setStyle(stNegrilla).setWidth(65),
													cmp.text(filtroCierreTurno.getNroConsecutivo()).setStyle(stl.style()).setHorizontalAlignment(HorizontalAlignment.LEFT).setWidth(250)
											),
											cmp.horizontalList(
													cmp.text("Fecha y hora de generación cierre: ").setStyle(stNegrilla).setWidth(130),
													cmp.text(filtroCierreTurno.getFechaGeneracion()+ " - "+
															filtroCierreTurno.getHoraGeneracion()).setStyle(stl.style()).setHorizontalAlignment(HorizontalAlignment.LEFT),
													cmp.text("Usuario que Genera cierre: ").setStyle(stNegrilla).setWidth(100),
													cmp.text(filtroCierreTurno.getUsuarioGeneracion()).setStyle(stl.style()).setWidth(120).setHorizontalAlignment(HorizontalAlignment.LEFT)
											),
											cmp.horizontalList(
													cmp.text("Cajero: ").setStyle(stNegrilla).setWidth(30),
													cmp.text(filtroCierreTurno.getCajero()).setStyle(stl.style()).setHorizontalAlignment(HorizontalAlignment.LEFT),
													cmp.text("Caja: ").setStyle(stNegrilla).setWidth(20),
													cmp.text(filtroCierreTurno.getCaja()).setStyle(stl.style()).setHorizontalAlignment(HorizontalAlignment.LEFT),
													cmp.text("Fecha cierre: ").setStyle(stNegrilla).setWidth(50),
													cmp.text(filtroCierreTurno.getFechaCierre()).setStyle(stl.style()).setHorizontalAlignment(HorizontalAlignment.LEFT).setWidth(50)
											),
											cmp.horizontalList(
													cmp.text("Caja Mayor/Principal: ").setStyle(stNegrilla).setWidth(60),
													cmp.text(filtroCierreTurno.getCajaMayorPrincipal()).setStyle(stl.style()).setWidth(250).setHorizontalAlignment(HorizontalAlignment.LEFT)
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
		
		String impresion=(filtroCierreTurno.isEsConsulta()?"Reimpresión: ":"Impresión: ");
		
		HorizontalListBuilder resumenImpresion= cmp.horizontalFlowList(
				 cmp.text(" ").setWidth(10),
				 cmp.text(impresion).setHorizontalAlignment(HorizontalAlignment.RIGHT).setStyle(stNegrilla).setWidth(50),
				 cmp.text(sdf.format(new Date())).setStyle(stNormal).setWidth(80),
				 cmp.text(filtroCierreTurno.getNombreUsuario()).setHorizontalAlignment(HorizontalAlignment.CENTER).setStyle(stNormal).setWidth(120),
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
				 cmp.text("Usuario Genera Arqueo").setStyle(stNegrilla).setHorizontalAlignment(HorizontalAlignment.CENTER),
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
