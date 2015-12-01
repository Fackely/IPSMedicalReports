package com.servinte.axioma.generadorReporte.historiaClinica;

import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;
import static net.sf.dynamicreports.report.builder.DynamicReports.report;

import java.util.Map;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.builder.component.HorizontalListBuilder;
import net.sf.dynamicreports.report.constant.HorizontalAlignment;

import org.apache.struts.util.MessageResources;

import util.reportes.dinamico.EstilosReportesDinamicosHistoriaClinica;

import com.servinte.axioma.generadorReporteHistoriaClinica.comun.IConstantesReporteHistoriaClinica;

public class SubReporteEncabezadoInformacionBasica {
	
	
	/**
	 * Mensajes parametrizados del reporte.
	 */
	private static MessageResources messageResource = MessageResources.getMessageResources("com.servinte.mensajes.historiaclinica.impresionHistoriaClinica");

	public SubReporteEncabezadoInformacionBasica() {
		// TODO Auto-generated constructor stub
	}
	
	
	
	public JasperReportBuilder crearEncabezadoInformacionbasica(Map<String, String> params) {
		
		//messageResource.getMessage("consolidacion_cierres_label_reporte_saldo_total")
		JasperReportBuilder infoBasica = report();
		ComponentBuilder<?, ?> componentBuilder = null;
		
		String nombrepaciente	=messageResource.getMessage("historia_clinica_lable_nombrepaciente")+ params.get(IConstantesReporteHistoriaClinica.nombrepaciente);
		String fechaNacimiento	= messageResource.getMessage("historia_clinica_lable_fechaNacimiento")+params.get(IConstantesReporteHistoriaClinica.fechaNacimiento);
		String estadoCivil	=messageResource.getMessage("historia_clinica_lable_estadoCivil")+ params.get(IConstantesReporteHistoriaClinica.estadoCivil);
		String residencia	=messageResource.getMessage("historia_clinica_lable_residencia")+ params.get(IConstantesReporteHistoriaClinica.residencia);
		/*
		 * MT7215
		 * davgarhe - 09/01/2014
		 */
		String telefonoPaciente = messageResource.getMessage("historia_clinica_lable_telPaciente") + params.get(IConstantesReporteHistoriaClinica.telefonoPaciente);
		String fechahoraingreso	=messageResource.getMessage("historia_clinica_lable_fechahoraingreso")+ params.get(IConstantesReporteHistoriaClinica.fechahoraingreso);
		String fechahoraEgreso	=messageResource.getMessage("historia_clinica_lable_fechahoraEgreso")+ params.get(IConstantesReporteHistoriaClinica.fechahoraEgreso);
		
		String acompanantePaciente	=messageResource.getMessage("historia_clinica_lable_acompanantePaciente");
		if (params.get(IConstantesReporteHistoriaClinica.acompanantePaciente)!=null && !params.get(IConstantesReporteHistoriaClinica.acompanantePaciente).equals("")) {
			acompanantePaciente+=params.get(IConstantesReporteHistoriaClinica.acompanantePaciente);
				
		}
		
		
		String responsablePaciente	=messageResource.getMessage("historia_clinica_lable_responsablePaciente");
		if (!String.valueOf(params.get(IConstantesReporteHistoriaClinica.responsablePaciente)).equals("")) {
			responsablePaciente+=params.get(IConstantesReporteHistoriaClinica.responsablePaciente);
				
		}
		
		
		String convenioPaciente	=messageResource.getMessage("historia_clinica_lable_convenioPaciente")+ params.get(IConstantesReporteHistoriaClinica.convenioPaciente);
		String tipoNumeroID	=messageResource.getMessage("historia_clinica_lable_tipoNumeroID")+ params.get(IConstantesReporteHistoriaClinica.tipoNumeroID);
		String edad	=messageResource.getMessage("historia_clinica_lable_edad")+ params.get(IConstantesReporteHistoriaClinica.edad);
		String ocupacion	=messageResource.getMessage("historia_clinica_lable_ocupacion")+ params.get(IConstantesReporteHistoriaClinica.ocupacion);
	
		 
		String viaIngreso	=messageResource.getMessage("historia_clinica_lable_viaIngreso")+ params.get(IConstantesReporteHistoriaClinica.viaIngreso);
		
		
		
		String viaEgreso	=messageResource.getMessage("historia_clinica_lable_viaEgreso")+ params.get(IConstantesReporteHistoriaClinica.viaEgreso);
		String telParentescoUno	=messageResource.getMessage("historia_clinica_lable_telParentescoUno");
		
		
		
		if (!String.valueOf(params.get(IConstantesReporteHistoriaClinica.telParentescoUno)).equals("")) {
			telParentescoUno+= params.get(IConstantesReporteHistoriaClinica.telParentescoUno);
		}
			
		String telParentescoDos	=messageResource.getMessage("historia_clinica_lable_telParentescoDos");
		if (!String.valueOf(params.get(IConstantesReporteHistoriaClinica.telParentescoDos)).equals("")) {
			telParentescoDos+= params.get(IConstantesReporteHistoriaClinica.telParentescoDos);
		}
		
		
		String tipoAfiliado	=messageResource.getMessage("historia_clinica_lable_tipoAfiliado")+ params.get(IConstantesReporteHistoriaClinica.tipoAfiliado);
		String sexo	=messageResource.getMessage("historia_clinica_lable_sexo")+ params.get(IConstantesReporteHistoriaClinica.sexo);
		String regimen	=messageResource.getMessage("historia_clinica_lable_regimen")+ params.get(IConstantesReporteHistoriaClinica.regimen);
		String nombreParentescoUno	=messageResource.getMessage("historia_clinica_lable_nombreParentescoUno");
		if (!String.valueOf(params.get(IConstantesReporteHistoriaClinica.nombreParentescoUno)).equals("")) {
			nombreParentescoUno+=params.get(IConstantesReporteHistoriaClinica.nombreParentescoUno); 
				
		}
		
		
		
		String nombreparentescoDos	=messageResource.getMessage("historia_clinica_lable_nombreparentescoDos");
		if (!String.valueOf(params.get(IConstantesReporteHistoriaClinica.nombreparentescoDos)).equals("")) {
			nombreparentescoDos+=params.get(IConstantesReporteHistoriaClinica.nombreparentescoDos);
				
		}
		
		HorizontalListBuilder encabezado = null;
		encabezado=	cmp.horizontalList( cmp.verticalList(
						cmp.text(nombrepaciente).setStyle(EstilosReportesDinamicosHistoriaClinica.estiloSubTituloFont5).setHorizontalAlignment(HorizontalAlignment.LEFT),
						cmp.text(fechaNacimiento).setStyle(EstilosReportesDinamicosHistoriaClinica.estiloSubTituloFont5).setHorizontalAlignment(HorizontalAlignment.LEFT),
						cmp.text(estadoCivil).setStyle(EstilosReportesDinamicosHistoriaClinica.estiloSubTituloFont5).setHorizontalAlignment(HorizontalAlignment.LEFT),
						cmp.text(residencia).setStyle(EstilosReportesDinamicosHistoriaClinica.estiloSubTituloFont5).setHorizontalAlignment(HorizontalAlignment.LEFT),
						cmp.text(fechahoraingreso).setStyle(EstilosReportesDinamicosHistoriaClinica.estiloSubTituloFont5).setHorizontalAlignment(HorizontalAlignment.LEFT),
						cmp.text(fechahoraEgreso).setStyle(EstilosReportesDinamicosHistoriaClinica.estiloSubTituloFont5).setHorizontalAlignment(HorizontalAlignment.LEFT),
						cmp.text(acompanantePaciente).setStyle(EstilosReportesDinamicosHistoriaClinica.estiloSubTituloFont5).setHorizontalAlignment(HorizontalAlignment.LEFT),
						cmp.text(responsablePaciente).setStyle(EstilosReportesDinamicosHistoriaClinica.estiloSubTituloFont5).setHorizontalAlignment(HorizontalAlignment.LEFT)
				),
				cmp.verticalList(
						cmp.text(tipoNumeroID).setStyle(EstilosReportesDinamicosHistoriaClinica.estiloSubTituloFont5).setHorizontalAlignment(HorizontalAlignment.LEFT),
						cmp.text(edad).setStyle(EstilosReportesDinamicosHistoriaClinica.estiloSubTituloFont5).setHorizontalAlignment(HorizontalAlignment.LEFT),
						cmp.text(ocupacion).setStyle(EstilosReportesDinamicosHistoriaClinica.estiloSubTituloFont5).setHorizontalAlignment(HorizontalAlignment.LEFT),
						/*
						 * MT7215
						 * davgarhe - 09/01/2014
						 */
						cmp.text(telefonoPaciente).setStyle(EstilosReportesDinamicosHistoriaClinica.estiloSubTituloFont5).setHorizontalAlignment(HorizontalAlignment.LEFT),
						cmp.text(viaIngreso).setStyle(EstilosReportesDinamicosHistoriaClinica.estiloSubTituloFont5).setHorizontalAlignment(HorizontalAlignment.LEFT),
						cmp.text(viaEgreso).setStyle(EstilosReportesDinamicosHistoriaClinica.estiloSubTituloFont5).setHorizontalAlignment(HorizontalAlignment.LEFT),
						cmp.text(telParentescoUno).setStyle(EstilosReportesDinamicosHistoriaClinica.estiloSubTituloFont5).setHorizontalAlignment(HorizontalAlignment.LEFT),
						cmp.text(telParentescoDos).setStyle(EstilosReportesDinamicosHistoriaClinica.estiloSubTituloFont5).setHorizontalAlignment(HorizontalAlignment.LEFT)
				),
				cmp.verticalList(
						cmp.text("").setStyle(EstilosReportesDinamicosHistoriaClinica.estiloSubTituloFont5).setHorizontalAlignment(HorizontalAlignment.LEFT),
						cmp.text(sexo).setStyle(EstilosReportesDinamicosHistoriaClinica.estiloSubTituloFont5).setHorizontalAlignment(HorizontalAlignment.LEFT),
						/*
						 * MT7215
						 * davgarhe - 09/01/2014
						 * Se modifica la posicion para ajustar al ejemplo
						 */
						cmp.text(tipoAfiliado).setStyle(EstilosReportesDinamicosHistoriaClinica.estiloSubTituloFont5).setHorizontalAlignment(HorizontalAlignment.LEFT),
						cmp.text("").setStyle(EstilosReportesDinamicosHistoriaClinica.estiloSubTituloFont5).setHorizontalAlignment(HorizontalAlignment.LEFT),
						cmp.text("").setStyle(EstilosReportesDinamicosHistoriaClinica.estiloSubTituloFont5).setHorizontalAlignment(HorizontalAlignment.LEFT),
						cmp.text("").setStyle(EstilosReportesDinamicosHistoriaClinica.estiloSubTituloFont5).setHorizontalAlignment(HorizontalAlignment.LEFT),
						cmp.text(nombreParentescoUno).setStyle(EstilosReportesDinamicosHistoriaClinica.estiloSubTituloFont5).setHorizontalAlignment(HorizontalAlignment.LEFT),
						cmp.text(nombreparentescoDos).setStyle(EstilosReportesDinamicosHistoriaClinica.estiloSubTituloFont5).setHorizontalAlignment(HorizontalAlignment.LEFT)
				)
		);
		
		
		componentBuilder = cmp.verticalList(encabezado,cmp.text(convenioPaciente).setStyle(EstilosReportesDinamicosHistoriaClinica.estiloSubTituloFont5).setHorizontalAlignment(HorizontalAlignment.LEFT));
		infoBasica.pageHeader(componentBuilder).build();  
		
		
		
		return infoBasica;
	}
	
	
	
	

}
