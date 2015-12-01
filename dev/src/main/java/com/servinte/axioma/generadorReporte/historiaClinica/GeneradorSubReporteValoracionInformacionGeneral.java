package com.servinte.axioma.generadorReporte.historiaClinica;

import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;
import static net.sf.dynamicreports.report.builder.DynamicReports.report;
import static net.sf.dynamicreports.report.builder.DynamicReports.stl;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.builder.component.HorizontalListBuilder;
import net.sf.dynamicreports.report.constant.HorizontalAlignment;
import net.sf.dynamicreports.report.constant.VerticalAlignment;

import org.apache.struts.util.MessageResources;

import util.reportes.dinamico.EstilosReportesDinamicosHistoriaClinica;


public class GeneradorSubReporteValoracionInformacionGeneral {
	
	/**
	 * Mensajes parametrizados de los reportes 
	 */ 
	private static MessageResources messageResource = MessageResources.getMessageResources("com.servinte.mensajes.historiaclinica.impresionHistoriaClinica");
		
	public GeneradorSubReporteValoracionInformacionGeneral() {
	}
	
	
	public JasperReportBuilder generarReporteSeccionInformacionBasica(){
		JasperReportBuilder informacionGeneral = report();
		ComponentBuilder<?, ?> componentBuilder = null;
		HorizontalListBuilder varolacionUrgenciasTituloSeccion = null;
		HorizontalListBuilder informacionGeneralSeccion = null;
		HorizontalListBuilder motivoConsultaSeccion = null;
		HorizontalListBuilder enfermedadActualSeccion = null;
		HorizontalListBuilder signosVitalesSeccion = null;
		
		List<HorizontalListBuilder> listaHorizonatBuilder= new ArrayList<HorizontalListBuilder>();
		ComponentBuilder[] listaComponentesAPintar;
		GeneradorDisenoSubReportes generadorDiseñoSubReportes = new GeneradorDisenoSubReportes();
		
		
		
		
		varolacionUrgenciasTituloSeccion=	cmp.horizontalList(cmp.verticalList(cmp.horizontalList(
				cmp.text(messageResource.getMessage("historia_clinica_lable_valoracion_Urgencias_titulo")).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloTitulo).setHorizontalAlignment(HorizontalAlignment.LEFT))
		)))	;
		listaHorizonatBuilder.add(varolacionUrgenciasTituloSeccion);
		
		
		informacionGeneralSeccion=	cmp.horizontalList(cmp.verticalList(cmp.horizontalList(
				cmp.text(messageResource.getMessage("historia_clinica_lable_valoracion_Informacion_general")).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloTitulo).setHorizontalAlignment(HorizontalAlignment.LEFT)),
				cmp.text("Fecha: 20/10/2009").setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloSubTitulo).setHorizontalAlignment(HorizontalAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE)),
				cmp.text("Hora: 02:41").setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloSubTitulo).setHorizontalAlignment(HorizontalAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE))
				).setStyle(stl.style().setBorder(stl.pen1Point().setLineColor(Color.BLACK)).setPadding(2)),
				cmp.horizontalList(
						/**
						 * MT 7001: Se debe quitar el campo "Fue accidente de trabajo" de la impresion de valoraciones en historia clinica
						 * @author jeilones
						 * @date 28/05/2013 
						 * */
						//cmp.text("¿Fue accidente de trabajo?: No").setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloSubTitulo).setHorizontalAlignment(HorizontalAlignment.LEFT)),
						cmp.text("¿El paciente llegó por sus propios medios?: Sí").setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloSubTitulo).setHorizontalAlignment(HorizontalAlignment.LEFT)),
						cmp.text("Estado de embriaguez: No").setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloSubTitulo).setHorizontalAlignment(HorizontalAlignment.RIGHT))
						).setStyle(stl.style().setBorder(stl.pen1Point().setLineColor(Color.BLACK)).setPadding(2))
		));
		listaHorizonatBuilder.add(informacionGeneralSeccion);
		
		
		
		
		motivoConsultaSeccion=	cmp.horizontalList(cmp.verticalList(
				cmp.text(messageResource.getMessage("historia_clinica_lable_valoracion_motivoCOnsulta")).setStyle(EstilosReportesDinamicosHistoriaClinica.estiloSubTitulo).setHorizontalAlignment(HorizontalAlignment.LEFT),
				cmp.text("MC: Tengo los pies hinchados y dolor bajito EA: Paciente G1P0, con EMB 35. SS por FUM del 11 Abril. Trae ECO Tardía del" +
						" 4 Oct para 25.1 SS Extrapolada 35. SS. Paciente refiere cuadro de 3 Días de Evolución consistente en Cefalea Intensa," +
						" Acufenos, Fosfenos. Refiere Edema en Miembros Inferiores. Concomitante al Cuadro Refiere Dolor Abdominal tipo Cólico" +
						" leve sin Irradiación. Movimientos Fetales  presentes. Niega Genitorragía, Niega Amniorrea. Refiere Orina Colurica.").setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloSubTituloSinBold).setHorizontalAlignment(HorizontalAlignment.LEFT))
		).setStyle(stl.style().setBorder(stl.pen1Point().setLineColor(Color.BLACK)).setPadding(2)))	; 
		listaHorizonatBuilder.add(motivoConsultaSeccion);
		
		
		
		enfermedadActualSeccion=	cmp.horizontalList(cmp.verticalList(
				cmp.text(messageResource.getMessage("historia_clinica_lable_valoracion_enfermedadActual")).setStyle(EstilosReportesDinamicosHistoriaClinica.estiloSubTitulo).setHorizontalAlignment(HorizontalAlignment.LEFT),
				cmp.text("Antecedente Ginecológico: MENARCA:12 años, SEXARCA: 19 años, ETS: No Refiere. FUM:11 Abril. " +
						"FUC: Negativa para Malignidad. Antecedente Obstetrico:G0P0 Embarazo Actual: Ecografía 4 Oct: FUVC," +
						" Placenta GI, ILA 12.95, FCF 136. Peso Fetal 703. Feto con Crecimiento Simétrico para 25.1 SS Ecografía" +
						" 10 Dic: FUVC, FCF 133, ILA 13.2. Placenta GII-III. Peso Fetal 2629, Edad Gestacional 35.2. Paraclínicos:" +
						" HB:12.1, HTO37.6, PLT 230. Urocultivo Neg. HIV Neg. VDRL No Reactiva. HC: O+").setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloSubTituloSinBold).setHorizontalAlignment(HorizontalAlignment.LEFT))
		).setStyle(stl.style().setBorder(stl.pen1Point().setLineColor(Color.BLACK)).setPadding(2)))	; 
		listaHorizonatBuilder.add(enfermedadActualSeccion); 
		
		
		
		
		signosVitalesSeccion=	cmp.horizontalList(cmp.verticalList(
				cmp.text(messageResource.getMessage("historia_clinica_lable_valoracion_signosVitales")).setStyle(EstilosReportesDinamicosHistoriaClinica.estiloSubTitulo).setHorizontalAlignment(HorizontalAlignment.LEFT)
				)
		).setStyle(stl.style().setBorder(stl.pen1Point().setLineColor(Color.BLACK)).setPadding(2))	;
		listaHorizonatBuilder.add(signosVitalesSeccion);
		
		
		
		
		
		
		
		listaComponentesAPintar = new ComponentBuilder[listaHorizonatBuilder.size()];
		for (int i = 0; i < listaComponentesAPintar.length; i++) {
			listaComponentesAPintar[i]=listaHorizonatBuilder.get(i);
		}
		
		
		componentBuilder = cmp.verticalList(listaComponentesAPintar);
		informacionGeneral
		.summary(componentBuilder)
		.setTemplate(generadorDiseñoSubReportes.crearPlantillaReporte())
		.build();
		
		return informacionGeneral;
		
	}

}
