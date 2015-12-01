package com.princetonsa.pdf;

import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;
import static net.sf.dynamicreports.report.builder.DynamicReports.col;
import static net.sf.dynamicreports.report.builder.DynamicReports.report;
import static net.sf.dynamicreports.report.builder.DynamicReports.stl;
import static net.sf.dynamicreports.report.builder.DynamicReports.type;

import java.awt.Color;
import java.util.HashMap;
import java.util.List;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.builder.component.VerticalListBuilder;
import net.sf.dynamicreports.report.constant.HorizontalAlignment;
import net.sf.dynamicreports.report.constant.LineStyle;
import net.sf.dynamicreports.report.constant.SplitType;
import net.sf.dynamicreports.report.constant.VerticalAlignment;
import net.sf.dynamicreports.report.exception.DRException;
import net.sf.jasperreports.engine.JRDataSource;
import util.UtilidadTexto;
import util.reportes.dinamico.DataSource;

import com.princetonsa.dto.facturacion.DtoDetalleCirugiasFacturaAgrupada;
import com.princetonsa.dto.facturacion.DtoFacturaAgrupada;

public class GeneradorReporteFacturacionAgrupado {





	/**
	 *Contructor de clase  
	 */
	public GeneradorReporteFacturacionAgrupado() {
	}



	/**
	 * @param params
	 * @param detalleFactura
	 * @return JasperReportBuilder con el reporte generado
	 */
	public JasperReportBuilder generarReporteFacturacionAgrupado(HashMap<String, String> params,List<DtoFacturaAgrupada> detalleFactura){

		//Inicialización del reporte
		JasperReportBuilder reportFormatoFacturacion = report();

		//instancia de diseno del reporte
		GeneradorDisenoReporteFacturacionAgrupado diseno = new GeneradorDisenoReporteFacturacionAgrupado();

		//se crea el encabezado del rpeorte 
		VerticalListBuilder pageHeader = cmp.verticalList(
				cmp.verticalList(
						diseno.crearComponenteEncabezado(params)
				)
		);


		//parametros de encabezado
		String saltoLinea=" ";
		String paciente ="";
		String identificacion="";
		String noIngreso="";
		String 	direccion="";
		String telefono ="";
		String fechaIngreso="";
		String fechaEgreso="";

		//Se valida el null o el vacio en los parametros 
		if(!UtilidadTexto.isEmpty(params.get(IConstantesReporteAgrupadoFacturacion.nombrePaciente))){
			paciente =params.get(IConstantesReporteAgrupadoFacturacion.nombrePaciente);
		}

		if(!UtilidadTexto.isEmpty(params.get(IConstantesReporteAgrupadoFacturacion.identificacionPaciente))){
			identificacion =params.get(IConstantesReporteAgrupadoFacturacion.identificacionPaciente);
		}

		if(!UtilidadTexto.isEmpty(params.get(IConstantesReporteAgrupadoFacturacion.noIngreso))){
			noIngreso =params.get(IConstantesReporteAgrupadoFacturacion.noIngreso);
		}

		if(!UtilidadTexto.isEmpty(params.get(IConstantesReporteAgrupadoFacturacion.direccion))){
			direccion =params.get(IConstantesReporteAgrupadoFacturacion.direccion);
		}

		if(!UtilidadTexto.isEmpty(params.get(IConstantesReporteAgrupadoFacturacion.telefonoPaciente))){
			telefono =params.get(IConstantesReporteAgrupadoFacturacion.telefonoPaciente);
		}

		if(!UtilidadTexto.isEmpty(params.get(IConstantesReporteAgrupadoFacturacion.fechaIngresoPaciente))){
			fechaIngreso =params.get(IConstantesReporteAgrupadoFacturacion.fechaIngresoPaciente);
		}

		if(!UtilidadTexto.isEmpty(params.get(IConstantesReporteAgrupadoFacturacion.fechaEgreso))){
			fechaEgreso =params.get(IConstantesReporteAgrupadoFacturacion.fechaEgreso);
		}





		//datos del paciente en un rectangulo 
		reportFormatoFacturacion.summary(cmp.verticalList(cmp.horizontalList(
				cmp.text(paciente).setStyle(stl.style(EstilosReportesDinamicosReporteFacturaAgrupada.estiloSubTituloSinBold).setFontSize(7)).setHorizontalAlignment(HorizontalAlignment.LEFT),
				cmp.text(identificacion).setStyle(stl.style(EstilosReportesDinamicosReporteFacturaAgrupada.estiloSubTituloSinBold).setFontSize(7)).setHorizontalAlignment(HorizontalAlignment.CENTER),
				cmp.text(noIngreso).setStyle(stl.style(EstilosReportesDinamicosReporteFacturaAgrupada.estiloSubTituloSinBold).setFontSize(7)).setHorizontalAlignment(HorizontalAlignment.RIGHT)
		),
		cmp.horizontalList(
				cmp.text(direccion).setStyle(stl.style(EstilosReportesDinamicosReporteFacturaAgrupada.estiloSubTituloSinBold).setFontSize(7)).setHorizontalAlignment(HorizontalAlignment.LEFT),
				cmp.text(telefono).setStyle(stl.style(EstilosReportesDinamicosReporteFacturaAgrupada.estiloSubTituloSinBold).setFontSize(7)).setHorizontalAlignment(HorizontalAlignment.CENTER),
				cmp.text(fechaIngreso).setStyle(stl.style(EstilosReportesDinamicosReporteFacturaAgrupada.estiloSubTituloSinBold).setFontSize(7)).setHorizontalAlignment(HorizontalAlignment.CENTER),
				cmp.text(fechaEgreso).setStyle(stl.style(EstilosReportesDinamicosReporteFacturaAgrupada.estiloSubTituloSinBold).setFontSize(7)).setHorizontalAlignment(HorizontalAlignment.RIGHT)
		)

		).setStyle(stl.style(EstilosReportesDinamicosReporteFacturaAgrupada.estiloBordeSencillo)));

		//se adiciona un espacio de linea 
		reportFormatoFacturacion.summary(cmp.text(saltoLinea));

		//se adiciona la tabla con los detalles de la factura
		reportFormatoFacturacion.summary(cmp.subreport(crearTablaFacturaAgrupada(detalleFactura,params)));


		//tabla inferior con los totales 
		String valorEnLetras=IConstantesReporteAgrupadoFacturacion.valorEnletras;
		String totalCargos=IConstantesReporteAgrupadoFacturacion.totalCargos;
		String cuotaModeradora=params.get("nombreTipoMontoCobro");
		String totalEmpresa=IConstantesReporteAgrupadoFacturacion.totalEmpresa;

		if(!UtilidadTexto.isEmpty(params.get(IConstantesReporteAgrupadoFacturacion.valorEnLetras))){
			valorEnLetras+=params.get(IConstantesReporteAgrupadoFacturacion.valorEnLetras);
		}


		//variables con los totales
		String cantidadTotalCargo="";
		String cantidadCuotaModeradora="";
		String cantidadTotalEmpresa="";

		//se validan vacios y null 
		if(!UtilidadTexto.isEmpty(params.get(IConstantesReporteAgrupadoFacturacion.totalCargo))){
			cantidadTotalCargo=params.get(IConstantesReporteAgrupadoFacturacion.totalCargo);
		}

		if(!UtilidadTexto.isEmpty(params.get(IConstantesReporteAgrupadoFacturacion.valorBrutoPaciente))){
			cantidadCuotaModeradora=params.get(IConstantesReporteAgrupadoFacturacion.valorBrutoPaciente);
		}

		if(!UtilidadTexto.isEmpty(params.get(IConstantesReporteAgrupadoFacturacion.valorConvenio))){
			cantidadTotalEmpresa=params.get(IConstantesReporteAgrupadoFacturacion.valorConvenio);
		}



		//se adiciona el valor en letras y los totales de cargos , cuota y total empresa
		reportFormatoFacturacion.summary(cmp.horizontalList()
				.newRow().add(cmp.text(saltoLinea))

				.newRow().add(cmp.horizontalList(cmp.text(valorEnLetras)
						.setStyle(stl.style(EstilosReportesDinamicosReporteFacturaAgrupada.estiloSubTitulo.setFontSize(7).setVerticalAlignment(VerticalAlignment.MIDDLE)))
						.setHorizontalAlignment(HorizontalAlignment.CENTER)
				).setStyle(stl.style(EstilosReportesDinamicosReporteFacturaAgrupada.estiloBordeSencillo)),

				cmp.horizontalList(cmp.verticalList(
						cmp.text(totalCargos).setStyle(stl.style(EstilosReportesDinamicosReporteFacturaAgrupada.estiloSubTitulo).setFontSize(7)).setHorizontalAlignment(HorizontalAlignment.LEFT),
						cmp.text(cuotaModeradora).setStyle(stl.style(EstilosReportesDinamicosReporteFacturaAgrupada.estiloSubTitulo).setFontSize(7)).setHorizontalAlignment(HorizontalAlignment.LEFT),
						cmp.text(totalEmpresa).setStyle(stl.style(EstilosReportesDinamicosReporteFacturaAgrupada.estiloSubTitulo).setFontSize(7)).setHorizontalAlignment(HorizontalAlignment.LEFT)
				))
				.setStyle(stl.style(EstilosReportesDinamicosReporteFacturaAgrupada.estiloBordeSencillo)),

				cmp.horizontalList(cmp.verticalList(
						cmp.text(cantidadTotalCargo).setWidth(20).setStyle(stl.style(EstilosReportesDinamicosReporteFacturaAgrupada.estiloSubTituloSinBold).setFontSize(7)).setHorizontalAlignment(HorizontalAlignment.RIGHT),
						cmp.text(cantidadCuotaModeradora).setWidth(20).setStyle(stl.style(EstilosReportesDinamicosReporteFacturaAgrupada.estiloSubTituloSinBold).setFontSize(7)).setHorizontalAlignment(HorizontalAlignment.RIGHT),
						cmp.text(cantidadTotalEmpresa).setWidth(20).setStyle(stl.style(EstilosReportesDinamicosReporteFacturaAgrupada.estiloSubTituloSinBold).setFontSize(7)).setHorizontalAlignment(HorizontalAlignment.RIGHT)

				)).setStyle(stl.style(EstilosReportesDinamicosReporteFacturaAgrupada.estiloBordeSencillo)))


		);

		//se adiciona un espacio para separa
		reportFormatoFacturacion.summary(cmp.text(saltoLinea));

		//se adiciona los datos de firmas 
		String lineaFirma= IConstantesReporteAgrupadoFacturacion.lineaFirma;
		String firmaFacturador=IConstantesReporteAgrupadoFacturacion.firmaFacturadorParentesisAbierto+params.get("loginusuario")+IConstantesReporteAgrupadoFacturacion.parentesisCerrado; 
		String firmaPaciente=IConstantesReporteAgrupadoFacturacion.firmapaciente;

		//se valida si el reporte va con copia o Origina
		String tipoPiePagina="";
		if(params.get(IConstantesReporteAgrupadoFacturacion.tipoPieDePagina).equals(IConstantesReporteAgrupadoFacturacion.original)){
			tipoPiePagina=IConstantesReporteAgrupadoFacturacion.originalMayuscula;
		}else{
			tipoPiePagina=IConstantesReporteAgrupadoFacturacion.copia;
		}


		//seccion de pie de pagina 
		reportFormatoFacturacion.summary(
				cmp.verticalList(
						cmp.horizontalList(cmp.text(lineaFirma).setHorizontalAlignment(HorizontalAlignment.CENTER),
								cmp.text(lineaFirma).setHorizontalAlignment(HorizontalAlignment.CENTER)),
								cmp.horizontalList(cmp.text(firmaFacturador).setStyle(stl.style(EstilosReportesDinamicosReporteFacturaAgrupada.estiloSubTituloSinBold))
										.setHorizontalAlignment(HorizontalAlignment.CENTER),
										cmp.text(firmaPaciente).setStyle(stl.style(EstilosReportesDinamicosReporteFacturaAgrupada.estiloSubTituloSinBold))
										.setHorizontalAlignment(HorizontalAlignment.CENTER)),
										cmp.text(saltoLinea),
										cmp.text(saltoLinea),
										cmp.horizontalList(cmp.horizontalList(cmp.text(params.get(IConstantesReporteAgrupadoFacturacion.piefactura))
												.setStyle(stl.style().setFontSize(7).bold()).setHorizontalAlignment(HorizontalAlignment.LEFT)
										)
										.setStyle(stl.style(EstilosReportesDinamicosReporteFacturaAgrupada.estiloBordeSencillo)))
										.newRow().add(
												cmp.text(tipoPiePagina).setHorizontalAlignment(HorizontalAlignment.CENTER).setStyle(stl.style(EstilosReportesDinamicosReporteFacturaAgrupada.estiloSubTituloSinBold))
										).newRow().add(cmp.text(" "))
				));



		// adicion de template a reporte , margenes y encabezado de  primera página
		reportFormatoFacturacion
		.setTemplate(diseno.crearPlantillaReporte(params.get(IConstantesReporteAgrupadoFacturacion.tamanoReporte))) 
		.setPageMargin(diseno.crearMagenesReporte())
		.pageHeader(pageHeader).
		pageFooter(diseno.crearcomponentePiePagina(params))

		;

		//se hace el build del reporte 
		reportFormatoFacturacion.build();
			
		


		//reporte
		return reportFormatoFacturacion;
	}




	/**
	 * Metodo que genera el subreporte con el detalle 
	 * @param detalleFactura
	 * @param params
	 * @return JasperReportBuilder con el reporte de detalle de factura
	 */
	public JasperReportBuilder crearTablaFacturaAgrupada(List<DtoFacturaAgrupada> detalleFactura,HashMap<String, String> params){

		//instancia de reporte
		JasperReportBuilder tablaDatosFactura = report();

		//instancia de deiseño de subreporte
		GeneradorDisenoSubReportesFacturaAgrupada generadorDisenoSubReportesFacturaAgrupada= new GeneradorDisenoSubReportesFacturaAgrupada();

		//se crean las columnas de la tabla 
		TextColumnBuilder<String>     descripcion = col.column(IConstantesReporteAgrupadoFacturacion.tituloDescripcion,  IConstantesReporteAgrupadoFacturacion.descripcion,  type.stringType()).setTitleStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK)));
		TextColumnBuilder<String>     detalleProcedimiento = col.column(IConstantesReporteAgrupadoFacturacion.tituloDetalleProcedimiento,  IConstantesReporteAgrupadoFacturacion.detalleProcedimiento,  type.stringType()).setTitleStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK)));
		TextColumnBuilder<String>     valorTotal = col.column(IConstantesReporteAgrupadoFacturacion.titulovalorTotal,  IConstantesReporteAgrupadoFacturacion.valorTotal,  type.stringType()).setTitleStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK)))
		.setStyle(stl.style().setHorizontalAlignment(HorizontalAlignment.RIGHT).setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK)));

		//se adicionan las tablas al reporte y se llama al datasource
		tablaDatosFactura
		.columns(descripcion,detalleProcedimiento,valorTotal.setWidth(20))
		.setDataSource(crearDataSourceFacturaAgrupada(detalleFactura,params))
		.setDetailSplitType(SplitType.IMMEDIATE)
		.setTemplate(generadorDisenoSubReportesFacturaAgrupada.crearPlantillaReporte(params.get(IConstantesReporteAgrupadoFacturacion.tamanoReporte)))
		.setPageMargin(generadorDisenoSubReportesFacturaAgrupada.crearMagenesSubReporte())
		.build();





		//retorno del reporte
		return tablaDatosFactura;

	}

	/**
	 * Metodo que llena el datasource 
	 * @param detalleFactura
	 * @param params
	 * @return JRDataSource con los valores a mostrar
	 */
	public JRDataSource crearDataSourceFacturaAgrupada(List<DtoFacturaAgrupada> detalleFactura,HashMap<String, String> params ){


		//se crea el datasource con las columans
		DataSource dataSource = new DataSource(IConstantesReporteAgrupadoFacturacion.descripcion ,IConstantesReporteAgrupadoFacturacion.detalleProcedimiento ,IConstantesReporteAgrupadoFacturacion.valorTotal );

		//se valida si usa decimales o no 
		if(params.get(IConstantesReporteAgrupadoFacturacion.usarDecimales).equals(IConstantesReporteAgrupadoFacturacion.falseCadena)){

			//se recorre la factura y de da formato a valores
			for (DtoFacturaAgrupada dtoFacturaAgrupada : detalleFactura) {
				dataSource.add(dtoFacturaAgrupada.getDescripcion(),dtoFacturaAgrupada.getDetalleProcedimiento(),UtilidadTexto.formatearValoresSinDecimales(dtoFacturaAgrupada.getValorTotal()));

				//si tiene detalles en caso de ser cirugias 
				for (DtoDetalleCirugiasFacturaAgrupada dtoDetalleCirugiasFacturaAgrupada : dtoFacturaAgrupada.getDetallesCirugias()) {
					dataSource.add("",dtoDetalleCirugiasFacturaAgrupada.getNombreServicioCirugia(),UtilidadTexto.formatearValoresSinDecimales(dtoDetalleCirugiasFacturaAgrupada.getValor()));
				}

			}
		}else{

			//se recorre la factura y de da formato a valores
			for (DtoFacturaAgrupada dtoFacturaAgrupada : detalleFactura) {

				if(dtoFacturaAgrupada.getValorTotal()!=null && !dtoFacturaAgrupada.getValorTotal().equals("") && !dtoFacturaAgrupada.getValorTotal().equals(" ")){
					dataSource.add(dtoFacturaAgrupada.getDescripcion(),dtoFacturaAgrupada.getDetalleProcedimiento(),UtilidadTexto.formatearValores(dtoFacturaAgrupada.getValorTotal()));
				}else{
					dataSource.add(dtoFacturaAgrupada.getDescripcion(),dtoFacturaAgrupada.getDetalleProcedimiento()," ");
				}
				//si tiene detalles en caso de ser cirugias 
				for (DtoDetalleCirugiasFacturaAgrupada dtoDetalleCirugiasFacturaAgrupada : dtoFacturaAgrupada.getDetallesCirugias()) {
					dataSource.add("",dtoDetalleCirugiasFacturaAgrupada.getNombreServicioCirugia(),UtilidadTexto.formatearValores(dtoDetalleCirugiasFacturaAgrupada.getValor()));
				}

			}
		}

		//retorno del datasource
		return dataSource;
	}

}
