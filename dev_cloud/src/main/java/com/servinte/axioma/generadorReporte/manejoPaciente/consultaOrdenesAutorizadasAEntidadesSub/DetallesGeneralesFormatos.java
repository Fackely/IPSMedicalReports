package com.servinte.axioma.generadorReporte.manejoPaciente.consultaOrdenesAutorizadasAEntidadesSub;

import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;
import static net.sf.dynamicreports.report.builder.DynamicReports.stl;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.builder.component.HorizontalListBuilder;
import net.sf.dynamicreports.report.builder.component.VerticalListBuilder;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.constant.HorizontalAlignment;
import util.ConstantesIntegridadDominio;
import util.UtilidadFecha;

import com.princetonsa.dto.administracion.DtoIntegridadDominio;
import com.princetonsa.dto.manejoPaciente.DtoBusquedaTotalOrdenesAutorizadasEntSub;
import com.servinte.axioma.generadorReporte.comun.IConstantesReporte;

public class DetallesGeneralesFormatos {

	private DtoBusquedaTotalOrdenesAutorizadasEntSub filtroConsultaOrdenes;
	private ArrayList<DtoIntegridadDominio> listaEstados;
	public static final ComponentBuilder<?, ?> footerComponent;
	public static final StyleBuilder stNegrilla;
	public static final StyleBuilder stNormal;
	public static final StyleBuilder stTitulo;
	
	/**
	 * Método constructor de la clase
	 * @param filtroConsultaOrdenes contiene información de la consulta
	 */
	public DetallesGeneralesFormatos(DtoBusquedaTotalOrdenesAutorizadasEntSub filtroConsultaOrdenes) {
		listaEstados=filtroConsultaOrdenes.getNombresEstadosAutorizaciones();
		this.filtroConsultaOrdenes=filtroConsultaOrdenes;
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
	 * Este metodo se encarga de retornar true si la imagen del logo se encuentra almacenada 
	 * en la ruta indicada. 
	 * @param rutaLogo ruta donde se encuentra almacenado el logo
	 * @return existe true si se encuentra almacenada
	 */
	public boolean existeLogo (String rutaLogo){
		ClassLoader loader = this.getClass().getClassLoader();
		InputStream myInFile = null;
        boolean existe = false;

        try {
	        myInFile = loader.getResourceAsStream(rutaLogo);
	        
	        if (myInFile != null) {
	           existe = true;
	            myInFile.close();
	        }
        }
        catch (Exception e) {
        	e.printStackTrace();
		}
        return existe;
	}
	
	/**
	 * Este método se encarga de crear en el encabezado el logo y su ubicación
	 * @return HorizontalListBuilder banda del reporte que contiene el logo 
	 */
	public HorizontalListBuilder crearBandaLogos()
	{
		String ubicacionLogo = filtroConsultaOrdenes.getUbicacionLogo();
		String rutaLogo = "../"+filtroConsultaOrdenes.getRutaLogo();
		boolean existeLogo = existeLogo(rutaLogo);
		HorizontalListBuilder bandaImagen = null;
		
		if (existeLogo) {
			if (ubicacionLogo.equals(ConstantesIntegridadDominio.acronimoUbicacionDerecha)) {
				bandaImagen=	cmp.horizontalFlowList(
										cmp.verticalList(
												cmp.text("").setFixedWidth(130)
										),
										cmp.verticalList(
												cmp.text(filtroConsultaOrdenes.getRazonSocial()).setStyle(stTitulo),
												cmp.text("NIT "+filtroConsultaOrdenes.getNit()).setStyle(stTitulo),
												cmp.text("TOTAL ORDENES AUTORIZADAS A ENTIDADES SUBCONTRATADAS").setStyle(stTitulo),
												cmp.text("Tipo Consulta : "+filtroConsultaOrdenes.getNombreTipoConsulta()).setStyle(stTitulo)
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
												cmp.text(filtroConsultaOrdenes.getRazonSocial()).setStyle(stTitulo),
												cmp.text("NIT "+filtroConsultaOrdenes.getNit()).setStyle(stTitulo),
												cmp.text("TOTAL ORDENES AUTORIZADAS A ENTIDADES SUBCONTRATADAS").setStyle(stTitulo),
												cmp.text("Tipo Consulta : "+filtroConsultaOrdenes.getNombreTipoConsulta()).setStyle(stTitulo)
										),
										cmp.verticalList(
												cmp.text("").setFixedWidth(130)
										)
								);
			}
		}else{
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
		}
		
		return bandaImagen;
	}
	
	
	/**
	 * Método que crea el encabezado del reporte
	 * @return VerticalListBuilder componente que contiene el encabezado
	 */
	public VerticalListBuilder crearEncabezadoReporte()
	{
		HorizontalListBuilder titulos= cmp.horizontalFlowList(
 	    													  cmp.text("NIVEL DE ATENCIÓN").setStyle(stTitulo).setWidth(180)
															  );
		for(int i=0;i<listaEstados.size();i++)
		{
			titulos.add(cmp.text(listaEstados.get(i).getDescripcion()).setStyle(stTitulo).setWidth(100));
		}
		VerticalListBuilder encabezado= cmp.verticalList(
												crearBandaLogos(),
												cmp.text(""),
		   	    		  						cmp.horizontalFlowList(
		   	    		  								//cmp.text("").setWidth(30),
		   	    		  								cmp.text("Rango Fechas:").setHorizontalAlignment(HorizontalAlignment.LEFT).setWidth(43).setStyle(stNegrilla),
		   	    		  								cmp.text(UtilidadFecha.conversionFormatoFechaAAp(filtroConsultaOrdenes.getFechaInicioBusqueda())).setHorizontalAlignment(HorizontalAlignment.LEFT).setWidth(30).setStyle(stNormal),
		   	    		  								cmp.text(" - ").setHorizontalAlignment(HorizontalAlignment.LEFT).setWidth(10).setStyle(stNormal),
		   	    		  								cmp.text(UtilidadFecha.conversionFormatoFechaAAp(filtroConsultaOrdenes.getFechaFinBusqueda())).setHorizontalAlignment(HorizontalAlignment.LEFT).setWidth(40).setStyle(stNormal),
		   	    		  								cmp.text("").setWidth(150)
		   	    		  						),
		   	    		  						cmp.text(""),
		   	    		  						titulos
		   	    		  				);	
		
		return encabezado;
	}
	
	
	/**
	 * Método que crea el encabezado del reporte para los de tipo detallado
	 * @return VerticalListBuilder componente que contiene el encabezado
	 */
	public VerticalListBuilder crearEncabezadoReporteDetallado()
	{
		HorizontalListBuilder titulos= cmp.horizontalFlowList(
 	    													  cmp.text("SERVICIOS Y MEDICAMENTOS/INSUMOS").setStyle(stTitulo).setWidth(155)
															  );
		for(int i=0;i<listaEstados.size();i++)
		{
			titulos.add(cmp.text(listaEstados.get(i).getDescripcion()).setStyle(stTitulo).setWidth(100));
		}
	          
		
		
		VerticalListBuilder encabezado= cmp.verticalList(
												crearBandaLogos(),
												cmp.text(""),
		   	    		  						cmp.horizontalList(
		   	    		  								cmp.text("Rango Fechas:").setHorizontalAlignment(HorizontalAlignment.LEFT).setWidth(43).setStyle(stNegrilla),
		   	    		  								cmp.text(UtilidadFecha.conversionFormatoFechaAAp(filtroConsultaOrdenes.getFechaInicioBusqueda())).setHorizontalAlignment(HorizontalAlignment.LEFT).setStyle(stNormal).setWidth(30),
		   	    		  								cmp.text(" - ").setHorizontalAlignment(HorizontalAlignment.LEFT).setWidth(10).setStyle(stNegrilla),
		   	    		  								cmp.text(UtilidadFecha.conversionFormatoFechaAAp(filtroConsultaOrdenes.getFechaFinBusqueda())).setHorizontalAlignment(HorizontalAlignment.LEFT).setStyle(stNormal).setWidth(45),
		   	    		  								cmp.text("Entidad subcontratada: ").setStyle(stNegrilla).setHorizontalAlignment(HorizontalAlignment.LEFT).setWidth(90),
		   	    		  								cmp.text(filtroConsultaOrdenes.getNombreEntidadSub()).setStyle(stNormal).setWidth(120)
		   	    		  						),
		   	    		  						cmp.text(""),
		   	    		  						titulos
		   	    		  						
		   	    		  				);	
		
		return encabezado;
	}
	
	
	/**
	 * Método que crea el encabezado del reporte para los de tipo grupo/clase
	 * @return VerticalListBuilder componente que contiene el encabezado
	 */
	public VerticalListBuilder crearEncabezadoReporteGrupoClase()
	{
		HorizontalListBuilder titulos= cmp.horizontalFlowList(
 	    													  cmp.text(filtroConsultaOrdenes.getNombreTipoConsulta()).setStyle(stTitulo).setWidth(155)
															  );
		for(int i=0;i<listaEstados.size();i++)
		{
			titulos.add(cmp.text(listaEstados.get(i).getDescripcion()).setStyle(stTitulo).setWidth(100));
		}
	          
		
		
		VerticalListBuilder encabezado= cmp.verticalList(
												crearBandaLogos(),
												cmp.text(""),
		   	    		  						cmp.horizontalList(
		   	    		  								cmp.text("Rango Fechas: ").setHorizontalAlignment(HorizontalAlignment.LEFT).setWidth(43).setStyle(stNegrilla),
		   	    		  								cmp.text(UtilidadFecha.conversionFormatoFechaAAp(filtroConsultaOrdenes.getFechaInicioBusqueda())).setHorizontalAlignment(HorizontalAlignment.LEFT).setStyle(stNormal).setWidth(30),
		   	    		  								cmp.text(" - ").setHorizontalAlignment(HorizontalAlignment.LEFT).setWidth(10).setStyle(stNegrilla),
		   	    		  								cmp.text(UtilidadFecha.conversionFormatoFechaAAp(filtroConsultaOrdenes.getFechaFinBusqueda())).setHorizontalAlignment(HorizontalAlignment.LEFT).setStyle(stNormal).setWidth(45),
		   	    		  								cmp.text("Entidad subcontratada: ").setStyle(stNegrilla).setHorizontalAlignment(HorizontalAlignment.LEFT).setWidth(90),
		   	    		  								cmp.text(filtroConsultaOrdenes.getNombreEntidadSub()).setStyle(stNormal).setWidth(120)
		   	    		  						),
		   	    		  						cmp.text(""),
		   	    		  						titulos
		   	    		  						
		   	    		  				);	
		
		return encabezado;
	}
	
	
	
	/**
	 * Método que crea el pie de página del reporte 
	 * @return HorizontalListBuilder componente que contiene el pie de página
	 */
	public  VerticalListBuilder crearPiePaginaReporte()
	{
		SimpleDateFormat sdf= new SimpleDateFormat("dd/MM/yyyy HH:mm");
		
		HorizontalListBuilder resumenImpresion= cmp.horizontalFlowList(
				 cmp.text(" ").setWidth(10),
				 cmp.text("Impresión: ").setHorizontalAlignment(HorizontalAlignment.RIGHT).setStyle(stNegrilla).setWidth(50),
				 cmp.text(sdf.format(new Date())).setStyle(stNormal).setWidth(80),
				 cmp.text(filtroConsultaOrdenes.getNombreUsuario()).setHorizontalAlignment(HorizontalAlignment.CENTER).setStyle(stNormal).setWidth(120),
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
	 * Método que crea el encabezado de los reportes en archivo plano
	 * @return VerticalListBuilder componente que contiene el encabezado
	 */
	public VerticalListBuilder crearEncabezadoReporteArchivoPlanoDetalladoGrupoClase()
	{
		VerticalListBuilder encabezado= cmp.verticalList(
												cmp.text("Reporte: Total Autorizaciones a Entidades Subcontratadas"),
												cmp.text("Tipo Consulta : "+filtroConsultaOrdenes.getNombreTipoConsulta()),
												cmp.text("Rango Fechas: "+UtilidadFecha.conversionFormatoFechaAAp(filtroConsultaOrdenes.getFechaInicioBusqueda())+"|"+
														 " - "+UtilidadFecha.conversionFormatoFechaAAp(filtroConsultaOrdenes.getFechaFinBusqueda())),
		   	    		  						cmp.text("Entidad subcontratada: "+filtroConsultaOrdenes.getNombreEntidadSub())
		   	    		  				);	
		
		return encabezado;
	}
	
	/**
	 * Método que crea el encabezado de reporte en archivo plano tipo nivel atencion
	 * @return VerticalListBuilder componente que contiene el encabezado
	 */
	public VerticalListBuilder crearEncabezadoReporteArchivoPlanoTipoNivelAtencion()
	{
		
		VerticalListBuilder encabezado= cmp.verticalList(
												cmp.text("Reporte: Total Autorizaciones a Entidades Subcontratadas"),
												cmp.text("Tipo Consulta : "+filtroConsultaOrdenes.getNombreTipoConsulta()),
												cmp.text("Rango Fechas: "+UtilidadFecha.conversionFormatoFechaAAp(filtroConsultaOrdenes.getFechaInicioBusqueda())+"|"+
														 " - "+UtilidadFecha.conversionFormatoFechaAAp(filtroConsultaOrdenes.getFechaFinBusqueda()))
		   	    		  				);	
		
		return encabezado;
	}
	
}
