package com.servinte.axioma.generadorReporte.historiaClinica;

import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;
import static net.sf.dynamicreports.report.builder.DynamicReports.col;
import static net.sf.dynamicreports.report.builder.DynamicReports.grp;
import static net.sf.dynamicreports.report.builder.DynamicReports.report;
import static net.sf.dynamicreports.report.builder.DynamicReports.stl;
import static net.sf.dynamicreports.report.builder.DynamicReports.type;

import java.awt.Color;
import java.sql.Connection;
import java.util.HashMap;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.builder.component.HorizontalListBuilder;
import net.sf.dynamicreports.report.builder.group.ColumnGroupBuilder;
import net.sf.dynamicreports.report.constant.HorizontalAlignment;
import net.sf.dynamicreports.report.constant.LineStyle;
import net.sf.dynamicreports.report.constant.SplitType;
import net.sf.dynamicreports.report.constant.VerticalAlignment;
import net.sf.jasperreports.engine.JRDataSource;

import org.apache.struts.util.MessageResources;

import util.UtilidadBD;
import util.UtilidadTexto;
import util.Utilidades;
import util.historiaClinica.UtilidadesHistoriaClinica;
import util.reportes.dinamico.DataSource;
import util.reportes.dinamico.EstilosReportesDinamicosHistoriaClinica;

import com.princetonsa.dto.historiaClinica.DtoMedicamentosOriginales;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;

public class GeneradorSubReporteAdministracionMedicamentosHojaAdministracionMedicamentos {

	/**
	 * Mensajes parametrizados del reporte.
	 */
	private static MessageResources messageResource = MessageResources.getMessageResources("com.servinte.mensajes.historiaclinica.impresionHistoriaClinica");

	public GeneradorSubReporteAdministracionMedicamentosHojaAdministracionMedicamentos() {
	}

	public JasperReportBuilder generarReporteSeccionHojaAdministracionMedicamentos(DtoImpresionHistoriaClinica dto
			,UsuarioBasico usuario, PersonaBasica paciente) throws NumberFormatException, Exception{
		JasperReportBuilder hojaMedicamentos = report();

		TextColumnBuilder []cols = new TextColumnBuilder[9];
		ComponentBuilder<?, ?> componentBuilder = null;
		HorizontalListBuilder tituloSeccion = null;
		GeneradorDisenoSubReportes disenio = new GeneradorDisenoSubReportes();

		tituloSeccion=	cmp.horizontalList(cmp.verticalList(cmp.horizontalList(
				cmp.text(messageResource.getMessage("historia_clinica_lable_valoracion_titulo_administracionMedicamentos")).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloTituloUnderline).underline().setHorizontalAlignment(HorizontalAlignment.LEFT))
		)))	;


		componentBuilder = cmp.verticalList(tituloSeccion);
		hojaMedicamentos
		.summary(componentBuilder)
		.summary(cmp.subreport(generarReporteSeccionHojaAdministracionMedicamentosDetail(dto, usuario, paciente)))
		.setTemplate(disenio.crearPlantillaReporte())
		.setPageMargin(disenio.crearMagenesReporte())
		.build();

		return hojaMedicamentos;
	}


	public JasperReportBuilder generarReporteSeccionHojaAdministracionMedicamentosDetail(DtoImpresionHistoriaClinica dto
			,UsuarioBasico usuario, PersonaBasica paciente) throws NumberFormatException, Exception{
		JasperReportBuilder hojaMedicamentos = report();

		TextColumnBuilder []cols = new TextColumnBuilder[9];
		ComponentBuilder<?, ?> componentBuilder = null;
		HorizontalListBuilder tituloSeccion = null;
		GeneradorDisenoSubReportes disenio = new GeneradorDisenoSubReportes();



		TextColumnBuilder<String>     codigo = col.column("Código",  "codigo",  type.stringType()).setTitleStyle(stl.style().setBackgroundColor(Color.LIGHT_GRAY).setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK)));
		TextColumnBuilder<String>     fechaHora = col.column("Fecha/Hora",  "fechaHora",  type.stringType()).setTitleStyle(stl.style().setBackgroundColor(Color.LIGHT_GRAY).setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK)));
		TextColumnBuilder<String>     dosisOrdenada = col.column("Dosis Ordenada",  "dosisOrdenada",  type.stringType()).setTitleStyle(stl.style().setBackgroundColor(Color.LIGHT_GRAY).setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK)));
		TextColumnBuilder<String>     frecuencia = col.column("Frecuencia",  "frecuencia",  type.stringType()).setTitleStyle(stl.style().setBackgroundColor(Color.LIGHT_GRAY).setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK)));
		TextColumnBuilder<String>     via = col.column("Vía",  "via",  type.stringType()).setTitleStyle(stl.style().setBackgroundColor(Color.LIGHT_GRAY).setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK)));
		TextColumnBuilder<String>     dosisAdministrada = col.column("Dosis Administrada",  "dosisAdministrada",  type.stringType()).setTitleStyle(stl.style().setBackgroundColor(Color.LIGHT_GRAY).setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK)));
		TextColumnBuilder<String>     unidadesConsumidas = col.column("Unidades Consumidas",  "unidadesConsumidas",  type.stringType()).setTitleStyle(stl.style().setBackgroundColor(Color.LIGHT_GRAY).setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK)));
		TextColumnBuilder<String>     responsable = col.column("Responsable",  "responsable",  type.stringType()).setTitleStyle(stl.style().setBackgroundColor(Color.LIGHT_GRAY).setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK)));
		TextColumnBuilder<String>     observaciones = col.column("Observaciones",  "observaciones",  type.stringType()).setTitleStyle(stl.style().setBackgroundColor(Color.LIGHT_GRAY).setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK)));

		cols[0]=codigo;
		cols[1]=fechaHora.setWidth(130);
		cols[2]=dosisOrdenada.setStretchWithOverflow(true);
		cols[3]=frecuencia.setStretchWithOverflow(true);
		cols[4]=via.setStretchWithOverflow(true);
		cols[5]=dosisAdministrada.setStretchWithOverflow(true);
		cols[6]=unidadesConsumidas.setStretchWithOverflow(true);
		cols[7]=responsable.setStretchWithOverflow(true);
		cols[8]=observaciones.setStretchWithOverflow(true);	



		ColumnGroupBuilder codigoGroup = grp.group(codigo)
		.setStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.LEFT).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK)))
		.setPadding(0);

		
		hojaMedicamentos.columns(cols)
		.groupBy(codigoGroup)
		.setDataSource(crearDatasourceHojaAdministracionMedicamentos(dto.getAdminMedicamente(),dto.getIdIngreso()))
		.setTemplate(disenio.crearPlantillaReporte())
		.setDetailSplitType(SplitType.PREVENT)
		.setPageMargin(disenio.crearMagenesSubReporte())
		.build();

		return hojaMedicamentos;
	}


	public JRDataSource crearDatasourceHojaAdministracionMedicamentos (HashMap adminMedicamentos,String idIngreso) throws NumberFormatException, Exception{
		String agrupador="";

		DataSource dataSource = new DataSource("codigo","fechaHora",
				"dosisOrdenada","frecuencia","via","dosisAdministrada","unidadesConsumidas",
				"responsable","observaciones");

		Integer tamanio=Utilidades.convertirAEntero(String.valueOf(adminMedicamentos.get("numRegistros")));
		String[] datosDataSource = new String[9];
		Connection con = UtilidadBD.abrirConexion();
		if (tamanio>0) {

			for (int i = 0; i < tamanio; i++) {
				if ( !String.valueOf(adminMedicamentos.get("articulo_"+i)).equals("")) {


					DtoMedicamentosOriginales medicamentoOriginal = UtilidadesHistoriaClinica.
					consultarMedicamentosOriginales(
							Integer.valueOf(String.valueOf(adminMedicamentos.get("articulo_" + i))),
							Integer.valueOf(String.valueOf(adminMedicamentos.get("codigo_admin_" + i))), con,
							Integer.valueOf(idIngreso));
					
					agrupador=String.valueOf(adminMedicamentos.get("articulo_"+i)).toLowerCase()+" ";
					agrupador+=" -"+String.valueOf(adminMedicamentos.get("medicamento_"+i)).toLowerCase()+" ";
					agrupador+=" - CONC "+String.valueOf(adminMedicamentos.get("concentracion_"+i)).toLowerCase()+" ";
					agrupador+=" - F.F "+String.valueOf(adminMedicamentos.get("forma_farmaceutica_"+i)).toLowerCase()+" ";
					agrupador+=" - U.M  "+String.valueOf(adminMedicamentos.get("unidad_medida_"+i)).toLowerCase()+" ";


					//obtenerUnidadMedidaMedicamentoPrincipal
					HashMap mapaDetalle = (HashMap)adminMedicamentos.get("detalle_"+i);
					Integer numDetalle = Utilidades.convertirAEntero(mapaDetalle.get("numRegistros").toString());

					for (int j = 0; j < numDetalle; j++) {
						String agrupadorTmp=agrupador.trim();
						String agrupadorMedicamentoPrincipal="";
						
						
						if(!medicamentoOriginal.getCodigo().equals(new Integer(0))){
						
						
												if(!UtilidadTexto.isEmpty(String.valueOf(medicamentoOriginal.getCodigo()))){
													agrupadorMedicamentoPrincipal=String.valueOf(medicamentoOriginal.getCodigo())+" ";
												}else{
													agrupadorMedicamentoPrincipal="";
												}
						
												if(!UtilidadTexto.isEmpty(medicamentoOriginal.getMedicamento())){
													agrupadorMedicamentoPrincipal+=" -"+medicamentoOriginal.getMedicamento().toLowerCase()+" ";
												}else{
													agrupadorMedicamentoPrincipal+=" ";
												}
						
						
						
												if(!UtilidadTexto.isEmpty(medicamentoOriginal.getConcentracion())){
													agrupadorMedicamentoPrincipal+=" - CONC "+medicamentoOriginal.getConcentracion().toLowerCase()+" ";
												}else{
													agrupadorMedicamentoPrincipal+=" - CONC ";
												}
						
												if(!UtilidadTexto.isEmpty(medicamentoOriginal.getFormaFarmaceutica())){
													agrupadorMedicamentoPrincipal+=" - F.F "+medicamentoOriginal.getFormaFarmaceutica().toLowerCase()+" ";
												}else{
													agrupadorMedicamentoPrincipal+=" - F.F ";
												}
						
												if(!UtilidadTexto.isEmpty(medicamentoOriginal.getUnidadMedida())){
													agrupadorMedicamentoPrincipal+=" - U.M  "+medicamentoOriginal.getUnidadMedida().toLowerCase()+" ";
												}else{
													agrupadorMedicamentoPrincipal+=" - U.M  ";
												}
						
												agrupadorTmp+=" (Ppal.: "+agrupadorMedicamentoPrincipal.trim()+")";
											}


						String unidadMedida="";

						unidadMedida=UtilidadesHistoriaClinica.obtenerUnidadMedidaMedicamentoPrincipal(con, Integer.valueOf(String.valueOf(mapaDetalle.get("numero_solicitud_"+j))), Integer.valueOf(String.valueOf(Integer.valueOf(String.valueOf(adminMedicamentos.get("articulo_"+i))))));
						datosDataSource[0]=agrupadorTmp;
						datosDataSource[1]=String.valueOf( mapaDetalle.get("fecha_"+j))+"-"+String.valueOf(mapaDetalle.get("hora_"+j));
						datosDataSource[2]=String.valueOf( mapaDetalle.get("dosis_"+j))+"-"+unidadMedida;
						datosDataSource[3]=String.valueOf( mapaDetalle.get("frecuencia_"+j))+"-"+String.valueOf(mapaDetalle.get("tipo_frecuencia_"+j));
						datosDataSource[4]=String.valueOf( mapaDetalle.get("via_"+j));
						datosDataSource[5]=String.valueOf( mapaDetalle.get("dosis_"+j)) +"-"+unidadMedida;
						datosDataSource[6]=String.valueOf( mapaDetalle.get("unidades_consumidas_"+j));
						datosDataSource[7]=String.valueOf( mapaDetalle.get("responsable_"+j));
						datosDataSource[8]=String.valueOf( mapaDetalle.get("observaciones_"+j));
						dataSource.add(datosDataSource);
						datosDataSource = new String[9];
					}



				}
			}




		}
		UtilidadBD.closeConnection(con);

		return dataSource;
	}

}
