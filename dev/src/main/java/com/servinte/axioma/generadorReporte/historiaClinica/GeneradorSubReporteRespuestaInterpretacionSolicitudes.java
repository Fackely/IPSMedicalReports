package com.servinte.axioma.generadorReporte.historiaClinica;

import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;
import static net.sf.dynamicreports.report.builder.DynamicReports.col;
import static net.sf.dynamicreports.report.builder.DynamicReports.margin;
import static net.sf.dynamicreports.report.builder.DynamicReports.report;
import static net.sf.dynamicreports.report.builder.DynamicReports.stl;
import static net.sf.dynamicreports.report.builder.DynamicReports.type;

import java.awt.Color;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.builder.MarginBuilder;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.builder.component.HorizontalListBuilder;
import net.sf.dynamicreports.report.constant.HorizontalAlignment;
import net.sf.dynamicreports.report.constant.LineStyle;
import net.sf.dynamicreports.report.constant.SplitType;
import net.sf.dynamicreports.report.constant.VerticalAlignment;
import net.sf.jasperreports.engine.JRDataSource;

import org.apache.struts.util.MessageResources;

import util.ConstantesBD;
import util.ConstantesCamposParametrizables;
import util.InfoDatosString;
import util.UtilidadBD;
import util.UtilidadTexto;
import util.ValoresPorDefecto;
import util.reportes.dinamico.DataSource;
import util.reportes.dinamico.EstilosReportesDinamicosHistoriaClinica;

import com.princetonsa.dto.historiaClinica.parametrizacion.DtoSeccionFija;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.historiaClinica.Plantillas;
import com.princetonsa.mundo.ordenesmedicas.procedimientos.RespuestaProcedimientos;
import com.princetonsa.mundo.solicitudes.DocumentoAdjunto;
import com.servinte.axioma.generadorReporte.historiaClinica.generacionDinamicaFormularioTexto.ServicioGenerarFormularioDinamicoTexto;

public class GeneradorSubReporteRespuestaInterpretacionSolicitudes {
	
	/**
	 * Mensajes parametrizados de los reportes 
	 */
	private static MessageResources messageResource = MessageResources.getMessageResources("com.servinte.mensajes.historiaclinica.impresionHistoriaClinica");
	
	
	public GeneradorSubReporteRespuestaInterpretacionSolicitudes() {
	}
	
	
	
	
	public   JasperReportBuilder respuestaInterpretacion(DtoImpresionHistoriaClinica dto
			,UsuarioBasico usuario, PersonaBasica paciente){
		
		
		
		JasperReportBuilder respuestaInterprestacionTotal = report();
		
		ComponentBuilder<?, ?> componentBuilder = null;
		List<ComponentBuilder> listaComponentes = new ArrayList<ComponentBuilder>();
		ComponentBuilder[] componentesTotales;
		HorizontalListBuilder tituloSeccion = null;
		List<DtoRespuestaInterprestacion> listaRespuestas = new ArrayList<DtoRespuestaInterprestacion>();
		dto.setPaciente(paciente);
		dto.setUsuario(usuario);
		GeneradorDisenioReporteHistoriaClinica disenio = new GeneradorDisenioReporteHistoriaClinica();
		listaRespuestas=consultaRespuestasInterpretacion(dto);
		
		
		if (listaRespuestas.size()>0) {
			tituloSeccion=	cmp.horizontalList(cmp.verticalList(cmp.horizontalList(
					cmp.text("Paraclínicos" ).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloTituloUnderline).underline().setHorizontalAlignment(HorizontalAlignment.LEFT))
			)))	;

		}
		
		TextColumnBuilder<String>     fechaRta = col.column("Fecha Rta",  "fechaRta",  type.stringType()).setTitleStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK)));
		TextColumnBuilder<String>     servicio = col.column("Servicio",  "servicio",  type.stringType()).setTitleStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK)));
		TextColumnBuilder<String>     fechaOrden = col.column("Fecha Orden",  "fechaOrden",  type.stringType()).setTitleStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK)));
		TextColumnBuilder<String>     resp = col.column("Respuesta",  "resp",  type.stringType()).setTitleStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK)));
		TextColumnBuilder<String>     adjunto = col.column("Adjunto",  "adjunto",  type.stringType()).setTitleStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK)));
		TextColumnBuilder<String>     interpretacion = col.column("Interpretación",  "interpretacion",  type.stringType()).setTitleStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK)));
		
		if (listaRespuestas.size()>0) {
			
			componentBuilder = cmp.verticalList(tituloSeccion);
			respuestaInterprestacionTotal
			.summary(componentBuilder);
			
			
			respuestaInterprestacionTotal
			.summary(cmp.subreport(respuestaInterpretacionTable(dto, usuario, paciente)))
			.setPageMargin(crearMagenesReporte())
			.setTemplate(disenio.crearPlantillaReporte());
			
		}
		
		respuestaInterprestacionTotal
		.build();
		return respuestaInterprestacionTotal;
		
	}
	
	
	
	public   JasperReportBuilder respuestaInterpretacionTable(DtoImpresionHistoriaClinica dto
			,UsuarioBasico usuario, PersonaBasica paciente){
		
		
		
		JasperReportBuilder respuestaInterprestacionTotal = report();
		
		ComponentBuilder<?, ?> componentBuilder = null;
		List<ComponentBuilder> listaComponentes = new ArrayList<ComponentBuilder>();
		ComponentBuilder[] componentesTotales;
		HorizontalListBuilder tituloSeccion = null;
		List<DtoRespuestaInterprestacion> listaRespuestas = new ArrayList<DtoRespuestaInterprestacion>();
		dto.setPaciente(paciente);
		dto.setUsuario(usuario);
		GeneradorDisenioReporteHistoriaClinica disenio = new GeneradorDisenioReporteHistoriaClinica();
		listaRespuestas=consultaRespuestasInterpretacion(dto);
		
		
		
		TextColumnBuilder<String>     fechaRta = col.column("Fecha Rta",  "fechaRta",  type.stringType()).setTitleStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK)));
		TextColumnBuilder<String>     servicio = col.column("Servicio",  "servicio",  type.stringType()).setTitleStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK)));
		TextColumnBuilder<String>     fechaOrden = col.column("Fecha Orden",  "fechaOrden",  type.stringType()).setTitleStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK)));
		TextColumnBuilder<String>     resp = col.column("Respuesta",  "resp",  type.stringType()).setTitleStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK)));
		TextColumnBuilder<String>     adjunto = col.column("Adjunto",  "adjunto",  type.stringType()).setTitleStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK)));
		TextColumnBuilder<String>     interpretacion = col.column("Interpretación",  "interpretacion",  type.stringType()).setTitleStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK)));
		
		if (listaRespuestas.size()>0) {
			respuestaInterprestacionTotal
			.setTemplate(disenio.crearPlantillaReporte())
			.setPageMargin(disenio.crearMagenesNulasSubReporte())
			.columns(fechaRta.setWidth(50),servicio.setWidth(50),fechaOrden.setWidth(30),resp.setWidth(140),adjunto.setWidth(55),interpretacion.setWidth(70))
			.setDataSource(crearDatasourceOInterpretacionProcedimientos(dto, listaRespuestas))
			.setDetailSplitType(SplitType.PREVENT);
		}
		
		respuestaInterprestacionTotal
		.build();
		return respuestaInterprestacionTotal;
		
	}
	

	
	
	
	
	
	public JRDataSource crearDatasourceOInterpretacionProcedimientos (DtoImpresionHistoriaClinica dto,List<DtoRespuestaInterprestacion> listaRespuestas){
		DataSource dataSource = new DataSource("fechaRta","servicio","fechaOrden","resp","adjunto","interpretacion");

		for (int i = 0; i < listaRespuestas.size(); i++) {
			DtoRespuestaInterprestacion dtoResp=listaRespuestas.get(i);
			dataSource.add(obtenerFechaResultado(dtoResp.getResultados()),
					dtoResp.getProcedimientoDto().getNombreServicioSolicitado(),
					dtoResp.getFechaEjecucion(),
					obtenerRespuesta(dtoResp.getResultados(),dtoResp),
					obtenerAdjuntos(dtoResp),
					obtenerInterpretacion(String.valueOf(dtoResp.getProcedimientoDto().getRespuestaProceEspecificoDto().getInterpretacion()))
					
					);
		}
		return dataSource;
	}
	
	
	public String obtenerRespuesta(String respuesta ,DtoRespuestaInterprestacion dtoResp){
		String res ="";
		ServicioGenerarFormularioDinamicoTexto servicioGenerarFormularioDinamicoTexto=new ServicioGenerarFormularioDinamicoTexto();
		
		/* INICIO SECCION FORMULARIO DINAMICO */
		
//		if(!UtilidadTexto.isEmpty(dtoResp.getPlantillaDto().getCodigo()) ||
//				!UtilidadTexto.isEmpty(dtoResp.getPlantillaDto().getNombre())	){
//			
//			res = "INFORMACIÓN PLANTILLA \n";
//		}
//		
//		if(!UtilidadTexto.isEmpty(dtoResp.getPlantillaDto().getCodigo())){
//			res +="Código: "+String.valueOf(dtoResp.getPlantillaDto().getCodigo()); 
//		}
//		
//		if(!UtilidadTexto.isEmpty(dtoResp.getPlantillaDto().getNombre())){
//			res +=" Nombre: "+String.valueOf(dtoResp.getPlantillaDto().getNombre()); 
//		}
//		res+="\n ";
		for (DtoSeccionFija seccionFija : dtoResp.getPlantillaDto().getSeccionesFijas()) {

			if(seccionFija.isVisible()){
				Integer valor = seccionFija.getCodigoSeccion();
				if(valor==ConstantesCamposParametrizables.seccionFijaInformacionGeneral){
//					if(!res.equals("")){
//						res+="\n"+seccionFijaInformacionGeneral(seccionFija,dtoResp);
//						res+="\n ";
//					}
				}else  if (valor==ConstantesCamposParametrizables.seccionFijaResultados){
					if(!res.equals("")){
						res+="\n"+seccionFijaresultados(seccionFija, dtoResp);
						res+="\n ";
					}
				}else if (valor==ConstantesCamposParametrizables.seccionFijaDiagnosticos){
					if(!res.equals("")){
						res+="\n"+seccionFijaDiagnosticos(seccionFija, dtoResp);
						res+="\n ";
					}
				}else if (valor==ConstantesCamposParametrizables.seccionFijaObservaciones){
					if(!res.equals("")){
						res+="\n"+seccionFijaObservaciones(seccionFija, dtoResp);
						res+="\n ";
					}
				}else if (valor==ConstantesCamposParametrizables.seccionFijaOtrosComentarios){
					if(!res.equals("")){
						res+="\n"+seccionFijaOtrosComentarios(seccionFija, dtoResp);
						res+="\n ";
					}
				}
				res+=servicioGenerarFormularioDinamicoTexto.generarCampoDinamicoTexto(seccionFija)+"\n";
			}
		}
		if( !dtoResp.getProcedimientoDto().getRespuestaProceEspecificoDto().getEsMuerto().equals("N")){
			res+=seccionFijaMuerto( dtoResp);
		}
		/* FIN SECCION FORMULARIO DINAMICO */
		
	//	res += respuesta.replace("<br>", "\n");
		
		return limpiarCadenaRepsuesta(res);
	}
	
	
	public String limpiarCadenaRepsuesta(String res){
		
		res=res.replace("<br>", "\n");
		
		for (int i = 0; i < res.length(); i++) {
			res=	res.replace("\n\n", "\n");
		}
		
		return res;
	}
	
	
	
	public String seccionFijaInterpretacion(DtoRespuestaInterprestacion dtoResp){
		String res = "INTERPRETACIÓN";
		
		if(dtoResp.getProcedimientoDto().isMostrarInterpretacionEnResumen()){
			if(!UtilidadTexto.isEmpty(dtoResp.getProcedimientoDto().getInterpretacion())){
				res+="\n "+dtoResp.getProcedimientoDto().getInterpretacion();
			}
		}
		return res;
	}
	
	
	public String seccionFijaOtrosComentarios(DtoSeccionFija seccionFija,DtoRespuestaInterprestacion dtoResp){
		String res = "";
		ArrayList<InfoDatosString> otrosComentariosArray= new ArrayList<InfoDatosString>();
		
		
		if(dtoResp.getProcedimientoDto().getRespuestaProceEspecificoDto().getOtrosComentariosArray().size()>0){
			res="OTROS COMENTARIOS";
			otrosComentariosArray = dtoResp.getProcedimientoDto().getRespuestaProceEspecificoDto().getOtrosComentariosArray();
			for (int i = 0; i < otrosComentariosArray.size(); i++) {
				res+="\n Médico:"+otrosComentariosArray.get(i).getNombre()+" Descripción: "+otrosComentariosArray.get(i).getDescripcion();
			}
			
		}
		
		
		return res;
	}
	
	public String seccionFijaMuerto(DtoRespuestaInterprestacion dtoResp){
		String res ="";
		String diagnosticoMuerte="";
		
		res="FALLECIO";
		if(!UtilidadTexto.isEmpty( dtoResp.getProcedimientoDto().getRespuestaProceEspecificoDto().getEsMuerto())){
			res+="\n Fallece: ";
			if(dtoResp.getProcedimientoDto().getRespuestaProceEspecificoDto().getEsMuerto().equals(ConstantesBD.acronimoNo)){
				res+=" No";
			}else{
				res+=" Si";
			}
		}
		
		if(!UtilidadTexto.isEmpty(dtoResp.getProcedimientoDto().getRespuestaProceEspecificoDto().getDiagnosticoMuerteCadenaCompleta())){
			diagnosticoMuerte=dtoResp.getProcedimientoDto().getRespuestaProceEspecificoDto().getDiagnosticoMuerteCadenaCompleta();
			res +="\n Diagnostico Muerte: "+diagnosticoMuerte.split(ConstantesBD.separadorSplit)[0]+"-"+diagnosticoMuerte.split(ConstantesBD.separadorSplit)[1]+" "+diagnosticoMuerte.split(ConstantesBD.separadorSplit)[2]; 
		}
		
		
		
		if(!UtilidadTexto.isEmpty(dtoResp.getProcedimientoDto().getRespuestaProceEspecificoDto().getFechaMuerte())){
				res+="\n Fecha de Muerte (dd/mm/aaaa):"+dtoResp.getProcedimientoDto().getRespuestaProceEspecificoDto().getFechaMuerte();
		}
		if(!UtilidadTexto.isEmpty(dtoResp.getProcedimientoDto().getRespuestaProceEspecificoDto().getHoraMuerte())){
				res+="\n Hora de Muerte (hh:mm):"+dtoResp.getProcedimientoDto().getRespuestaProceEspecificoDto().getHoraMuerte();
		}
		if(!UtilidadTexto.isEmpty(dtoResp.getProcedimientoDto().getRespuestaProceEspecificoDto().getCertificadoDefuncion())){
				
			res+="\n Certificado Defunción: ";
			if(dtoResp.getProcedimientoDto().getRespuestaProceEspecificoDto().getCertificadoDefuncion().equals(ConstantesBD.acronimoSi)){
				res+="Si";
			}else{
				res+="No";
			}
		}
		
		return res;
	}
	
	
	public String seccionFijaObservaciones(DtoSeccionFija seccionFija,DtoRespuestaInterprestacion dtoResp){
		String res="";
			
		
		
		if(!UtilidadTexto.isEmpty(dtoResp.getObservaciones())){
			res="OBSERVACIONES";
			res+="\n"+dtoResp.getObservaciones().replace("<br>", "\n");
		}
		
		return res;
	}
	
	
	
	
	
	public String seccionFijaDiagnosticos(DtoSeccionFija seccionFija,DtoRespuestaInterprestacion dtoResp){
		String res ="";
		String sinDiagnosticos = "";
		HashMap diagnosticos = new HashMap();
		String dxPrincipal="";
		String dxComplicacion="";
		Boolean flag = false;
		
		
		if(!UtilidadTexto.isEmpty(dtoResp.getProcedimientoDto().getRespuestaProceEspecificoDto().getNombreFinalidadServicio())){
			res = "FINALIDAD :"+dtoResp.getProcedimientoDto().getRespuestaProceEspecificoDto().getNombreFinalidadServicio();
			sinDiagnosticos = "FINALIDAD :"+dtoResp.getProcedimientoDto().getRespuestaProceEspecificoDto().getNombreFinalidadServicio();
		}
		
		res +="\nDIAGNÓSTICOS";
		
		diagnosticos= dtoResp.getDiagnosticos();
		if(!UtilidadTexto.isEmpty(String.valueOf(diagnosticos.get("acronimoPrincipal"))) ){
			flag =true;
			dxPrincipal+="\n ("+String.valueOf(diagnosticos.get("acronimoPrincipal"));
		}
		
		if(!UtilidadTexto.isEmpty(String.valueOf(diagnosticos.get("tipoCiePrincipal"))) ){
			flag =true;
			dxPrincipal+=" - "+String.valueOf(diagnosticos.get("tipoCiePrincipal"))+") ";
		}
		
		if(!UtilidadTexto.isEmpty(String.valueOf(diagnosticos.get("nombrePrincipal"))) ){
			flag =true;
			dxPrincipal+=String.valueOf(diagnosticos.get("nombrePrincipal"));
		}
		
		if(!UtilidadTexto.isEmpty(dxPrincipal)){
			flag =true;
			res+="\nDx Principal: "+dxPrincipal; 
		}
		
		
		
		
		if(Integer.valueOf(String.valueOf(diagnosticos.get("numRegistrosDiagonosticosRelacionados")))>0){
			flag =true;
			for (int i = 0; i < Integer.valueOf(String.valueOf(diagnosticos.get("numRegistrosDiagonosticosRelacionados"))); i++) {
				res+="\nDiagnóstico Relacionado: "+String.valueOf(diagnosticos.get("numeroRel_"+i))
				+" ("+String.valueOf(diagnosticos.get("acronimoRel_"+i))+"-"+String.valueOf(diagnosticos.get("tipoCieRel_"+i))+")\n"+
				diagnosticos.get("nombreRel_"+i);
				;
			}
			
			
			
		}
		
		
		
		
		if(!UtilidadTexto.isEmpty(String.valueOf(diagnosticos.get("acronimoComplicacion"))) ){
			flag =true;
			dxComplicacion+="\n ("+String.valueOf(diagnosticos.get("acronimoComplicacion"));
		}
		
		if(!UtilidadTexto.isEmpty(String.valueOf(diagnosticos.get("tipoCieComplicacion"))) ){
			flag =true;
			dxComplicacion+="- "+String.valueOf(diagnosticos.get("tipoCieComplicacion"))+") ";
		}
		
		if(!UtilidadTexto.isEmpty(String.valueOf(diagnosticos.get("nombreComplicacion"))) ){
			flag =true;
			dxComplicacion+=String.valueOf(diagnosticos.get("nombreComplicacion"));
		}
		
		
		if(!UtilidadTexto.isEmpty(dxComplicacion)){
			flag =true;
			res+="\n Dx Complicación :"+dxComplicacion; 
		}
		
		if(flag){
			return res;
		}else{
			return sinDiagnosticos;
		}
	}
	
	   
//	public Integer cantidadRegistrosDiagnosticosRelacionados(){
//	}
	
	public String seccionFijaresultados(DtoSeccionFija seccionFija,DtoRespuestaInterprestacion dtoResp){
		String res ="";
		
		res += "\nRESULTADOS ";
		
	
		
		
		if(!UtilidadTexto.isEmpty(dtoResp.getProcedimientoDto().getRespuestaProceEspecificoDto().getNumeroResSolProcHistorico()) && 
			dtoResp.getProcedimientoDto().getRespuestaProceEspecificoDto().getNumeroResSolProcHistorico()!=ConstantesBD.codigoNuncaValido	){
			res +="\nConsecutivo Respuesta :"+String.valueOf(dtoResp.getProcedimientoDto().getRespuestaProceEspecificoDto().getNumeroResSolProcHistorico());
		}
		
		if(!UtilidadTexto.isEmpty(dtoResp.getComentariosHistoriaClinica())){
			String comentariosAdicionales=dtoResp.getComentariosHistoriaClinica().replace("<br>", "\n");
			res+="\nComentarios Adicionales Historia Clínica: "+comentariosAdicionales;
		}
		
		
		if(!UtilidadTexto.isEmpty(dtoResp.getResultados())){
			res+="\nResultados: \n"+dtoResp.getResultados().replace("<br>", "\n");
		}
		
		if(!UtilidadTexto.isEmpty(dtoResp.getResultadosAnteriores())){
			res+="\nResultados Anteriores: \n"+dtoResp.getResultadosAnteriores().replace("<br>", "\n");
		}else{
			res+="\nResultados Anteriores: \n No Existen Resultados Anteriores ";
		}
		
		
		
		
		return res;
	}
	
	public String seccionFijaInformacionGeneral(DtoSeccionFija seccionFija,DtoRespuestaInterprestacion dtoResp){
		String res ="";
		
		res = "INFORMACIÓN GENERAL";
		
		if(!UtilidadTexto.isEmpty(dtoResp.getFechaEjecucion())){
			res +="\n"+"Fecha Ejecución : (dd/mm/aaaa) "+ dtoResp.getFechaEjecucion();
		}
		
		if(!UtilidadTexto.isEmpty(dtoResp.getNombreTipoRecargo())){
			res +="\nTipo Recargo: "+dtoResp.getNombreTipoRecargo() ;
		}
		
		if(!UtilidadTexto.isEmpty(dtoResp.getProcedimientoDto().getNombreServicioSolicitado())){
			String servicioInfoGeneral="";
			if(!UtilidadTexto.isEmpty( String.valueOf( dtoResp.getProcedimientoDto().getCodigoServicioSolicitado()))){
				servicioInfoGeneral=String.valueOf( dtoResp.getProcedimientoDto().getCodigoServicioSolicitado())+" - ";
			}
			res +="\n"+"Servicio: "+servicioInfoGeneral+dtoResp.getProcedimientoDto().getNombreServicioSolicitado();
		}
		
		
		return res;
	}
	
	
	
	
	public String obtenerAdjuntos(DtoRespuestaInterprestacion adjuntos){
		String res = "";
		
		for (int i = 0; i < adjuntos.getNumDocumentosAdjuntos(); i++) {
			if (adjuntos.getDocumentoAdjuntoGenerado("original_"+i)!=null && !String.valueOf(adjuntos.getDocumentoAdjuntoGenerado("original_"+i)).equals("")) {
				res+=String.valueOf(adjuntos.getDocumentoAdjuntoGenerado("original_"+i))+"\n";
			}
		}
		return res;
	}
	
	
	
	public String obtenerInterpretacion(String interpretacionCompleta){
		String res="";
		for (int i = 0; i <interpretacionCompleta.split("\n").length; i++) {
			if (i!=1 && !interpretacionCompleta.split("\n")[i].equals("\n")) {
				res+=interpretacionCompleta.split("\n")[i]+"\n";
			}
		}
		return res;
		
		
	}
	
	
	
	public String obtenerFechaResultado(String fecha){
		String res = "";
		if(fecha!=null && 
				fecha.length()>0 && fecha.contains("<br>") ){
			if (fecha.split("<br>").length>=2 && fecha.split("<br>")[1]!=null){
				res = fecha.split("<br>")[1];
			}
		}
		return res;
	}
	
	
	
	
	
	public   JasperReportBuilder generarReporteRespuesta(DtoImpresionHistoriaClinica dto
			,UsuarioBasico usuario, PersonaBasica paciente,DtoRespuestaInterprestacion respuesta,Integer i){
		JasperReportBuilder reporteRespuesta = report();
		
		ComponentBuilder<?, ?> componentBuilder = null;
		List<ComponentBuilder> listaComponentes = new ArrayList<ComponentBuilder>();
		ComponentBuilder[] componentesTotales;
		HorizontalListBuilder tituloSeccion = null;
		HorizontalListBuilder subtituloSeccion = null;
		HorizontalListBuilder fechaEjecucionSeccion = null;
		HorizontalListBuilder comentariosAdicionalesSeccion = null;
		HorizontalListBuilder consecutivoRespuestaSeccion = null;
		HorizontalListBuilder resultadosSeccion = null;
		HorizontalListBuilder resultadosAnterioresSeccion = null;
		HorizontalListBuilder interpretacionSeccion = null;
		HorizontalListBuilder profesionalrespondeSeccion = null;
		
		
		if (i==0) {
			tituloSeccion=	cmp.horizontalList(cmp.verticalList(cmp.horizontalList(
					cmp.text(messageResource.getMessage("historia_clinica_lable_tituloIntepretacionRespuesta")).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloTituloUnderline).underline().setHorizontalAlignment(HorizontalAlignment.LEFT))
			)))	;
			listaComponentes.add(tituloSeccion);
		}
	
			subtituloSeccion=cmp.horizontalList(cmp.verticalList(cmp.horizontalList(cmp.text("Información General").setStyle(EstilosReportesDinamicosHistoriaClinica.estiloBordeNegrilla))));
			listaComponentes.add(subtituloSeccion);
		
		
		
		if (!String.valueOf(respuesta.getFechaEjecucion()).equals("")) {
			fechaEjecucionSeccion=cmp.horizontalList(cmp.verticalList(cmp.horizontalList(
					cmp.text("Fecha de Ejecución: "+respuesta.getFechaEjecucion()).setStyle(EstilosReportesDinamicosHistoriaClinica.estiloBordeNegrilla)	
			)));
			listaComponentes.add(fechaEjecucionSeccion);
		}
	
		if (!String.valueOf(respuesta.getComentariosHistoriaClinica()).equals("")) {
			comentariosAdicionalesSeccion = cmp.horizontalList(cmp.verticalList(cmp.horizontalList(
					cmp.text("Comentarios Adicionales Historia Clínica : "+respuesta.getComentariosHistoriaClinica()).setStyle(EstilosReportesDinamicosHistoriaClinica.estiloBordeNegrilla)	
			)));
			listaComponentes.add(comentariosAdicionalesSeccion);
			
		}
		
		if ( (!String.valueOf(respuesta.getProcedimientoDto().getRespuestaProceEspecificoDto().getNumeroResSolProcHistorico()).equals(""))&&
		(!String.valueOf(respuesta.getProcedimientoDto().getRespuestaProceEspecificoDto().getNumeroResSolProcHistorico()).equals(String.valueOf(ConstantesBD.codigoNuncaValido)))){
			consecutivoRespuestaSeccion=cmp.horizontalList(cmp.verticalList(cmp.horizontalList(cmp.text("Consecutivo Respuesta: "+respuesta.getProcedimientoDto().getRespuestaProceEspecificoDto().getNumeroResSolProcHistorico()).setStyle(EstilosReportesDinamicosHistoriaClinica.estiloBordeNegrilla))));
			listaComponentes.add(consecutivoRespuestaSeccion);
		}
		
		
		if (!String.valueOf(respuesta.getResultados()).equals("")) {
			resultadosSeccion=cmp.horizontalList(cmp.verticalList(cmp.horizontalList(cmp.text("Resultados: "+respuesta.getResultados().replace("<br>", "\n")).setStyle(EstilosReportesDinamicosHistoriaClinica.estiloBordeNegrillaJustificado))));
			listaComponentes.add(resultadosSeccion);
		}
		
		
		if (!String.valueOf(respuesta.getRespuestaOtrosAnterior()).equals("")) {
			resultadosAnterioresSeccion=cmp.horizontalList(cmp.verticalList(cmp.horizontalList(cmp.text("Resultados Anteriores: "+respuesta.getRespuestaOtrosAnterior()).setStyle(EstilosReportesDinamicosHistoriaClinica.estiloBordeNegrilla))));
			listaComponentes.add(resultadosAnterioresSeccion);
		}
		
		if (!String.valueOf(respuesta.getProcedimientoDto().getRespuestaProceEspecificoDto().getInterpretacion()).equals("")) {
			interpretacionSeccion=cmp.horizontalList(cmp.verticalList(cmp.horizontalList(cmp.text("Interpretacion: "+String.valueOf(respuesta.getProcedimientoDto().getRespuestaProceEspecificoDto().getInterpretacion())).setStyle(EstilosReportesDinamicosHistoriaClinica.estiloBordeNegrilla))));
			listaComponentes.add(interpretacionSeccion);
		}
		
		InstitucionBasica instBasica = new InstitucionBasica();
		instBasica.cargar(dto.getUsuario().getCodigoInstitucionInt(), dto.getUsuario().getCodigoCentroAtencion());
		
		String folderFirmas=System.getProperty("ADJUNTOS");
		folderFirmas=folderFirmas.replace("../","");
		String firmaStr = ValoresPorDefecto.getDirectorioAxiomaBase()+folderFirmas+System.getProperty("FIRMADIGITAL")+"/"+usuario.getFirmaDigital();
		
		profesionalrespondeSeccion=cmp.horizontalList(cmp.verticalList(cmp.horizontalList(
				cmp.text("Profesional responde: "+respuesta.getDatosMedicoResponde()).setStyle(EstilosReportesDinamicosHistoriaClinica.estiloBordeNegrilla)),
				cmp.horizontalList(cmp.text("")),
				cmp.horizontalList(cmp.image(firmaStr).setDimension(90, 60)),
				cmp.horizontalList(cmp.text(instBasica.getPieHistoriaClinica()).setStyle(stl.style().setFontSize(5)))
		));
		listaComponentes.add(profesionalrespondeSeccion);
		
		
		componentesTotales= new ComponentBuilder[listaComponentes.size()];
		for (int j = 0; j < listaComponentes.size(); j++) {
			componentesTotales[j]=listaComponentes.get(j);
		}
		componentBuilder = cmp.verticalList(componentesTotales);
		
		reporteRespuesta.
		summary(componentBuilder)
		.build();
		return reporteRespuesta;
	}
	
	
	public List<DtoRespuestaInterprestacion> consultaRespuestasInterpretacion(DtoImpresionHistoriaClinica dto){
		
		HashMap mapaInterprestacionRespuesta = new HashMap();
		mapaInterprestacionRespuesta=dto.getInterpretacionRespuesta();
		List<DtoRespuestaInterprestacion> listaRespuestas = new ArrayList<DtoRespuestaInterprestacion>();
		Integer tamano =Integer.valueOf(String.valueOf(mapaInterprestacionRespuesta.get("numRegistros")));
		
		for (int i = 0; i < tamano; i++) {
			DtoRespuestaInterprestacion res = new DtoRespuestaInterprestacion(); 
				res = consultarDatosRespuesta(String.valueOf(mapaInterprestacionRespuesta.get("numerosolicitud_"+i)),
						String.valueOf(mapaInterprestacionRespuesta.get("codigorespuesta_"+i)), dto);
				listaRespuestas.add(res);
		}
		
		return listaRespuestas;
	}
	
	
	
	public DtoRespuestaInterprestacion consultarDatosRespuesta(String numeroSolicitud, String codigoRespuesta,DtoImpresionHistoriaClinica dto){
	
		DtoRespuestaInterprestacion forma = new DtoRespuestaInterprestacion();
		
		Connection con = UtilidadBD.abrirConexion();
		RespuestaProcedimientos respuestaProcedimientos = new RespuestaProcedimientos();
		respuestaProcedimientos.cargarRespuestaProcedimientos(numeroSolicitud, true, codigoRespuesta,"");
		cargarFormRespuesta(con,forma, respuestaProcedimientos, false,dto.getPaciente(),dto.getUsuario(),numeroSolicitud);
		
		forma.setDatosMedicoResponde(dto.getUsuario().getInformacionGeneralPersonalSalud());
		
		UtilidadBD.closeConnection(con);
		
		return forma;
		
	}
	
	/**
	 * @param procedimientoForm
	 * @param procedimiento
	 * @param esModificar
	 */
	private void cargarFormRespuesta(
			Connection con,
			DtoRespuestaInterprestacion procedimientoForm,
			RespuestaProcedimientos procedimiento,
			boolean esModificar,
			PersonaBasica paciente,
			UsuarioBasico usuario,
			String  numeroSolicitud)
	{
		if( esModificar )
		{
			procedimientoForm.setResultadosAnteriores(procedimiento.getResultados().replaceAll("<br>", "\n"));
			procedimientoForm.setObservacionesAnteriores(procedimiento.getObservaciones().replaceAll("<br>", "\n"));
			procedimientoForm.setComentariosHistoriaClinicaAnteriores(procedimiento.getComentariosHistoriaClinica().replaceAll("<br>", "\n"));
		}
		else
		{
			procedimientoForm.setResultados(procedimiento.getResultados().replaceAll("\n", "<br>"));
			procedimientoForm.setObservaciones(procedimiento.getObservaciones().replaceAll("\n", "<br>"));
			
			if(procedimiento.getComentariosHistoriaClinica() != null)
				procedimientoForm.setComentariosHistoriaClinica(procedimiento.getComentariosHistoriaClinica().replaceAll("\n", "<br>"));
			else
				procedimientoForm.setComentariosHistoriaClinica("");
		}
		
		//procedimientoForm.setNumeroAutorizacion(procedimiento.get);
		procedimientoForm.setNombreTipoRecargo(procedimiento.getNombreTipoRecargo());
		procedimientoForm.setCodigoTipoRecargo(procedimiento.getCodigoTipoRecargo());
		procedimientoForm.setFechaEjecucion(procedimiento.getFechaEjecucion());
		
		//se asignan los diagnósticos
		procedimientoForm.setDiagnosticos(procedimiento.getDiagnosticos());
		procedimientoForm.setNumDiagnosticos(procedimiento.getNumDiagnosticos());
		
		int numAdjuntos = procedimiento.getDocumentosAdjuntos().getNumDocumentosAdjuntos();
		procedimientoForm.setNumDocumentosAdjuntos(numAdjuntos);
		
		for( int i=0; i<numAdjuntos; i++ )
		{
			DocumentoAdjunto documento = procedimiento.getDocumentosAdjuntos().getDocumentoAdjunto(i);
			
			procedimientoForm.setDocumentoAdjuntoGenerado("original_"+i, documento.getNombreOriginal());
			procedimientoForm.setDocumentoAdjuntoGenerado("generado_"+i, documento.getNombreGenerado());
			procedimientoForm.setDocumentoAdjuntoGenerado("codigo_"+i, documento.getCodigoArchivo()+"");
			procedimientoForm.setDocumentoAdjuntoGenerado(""+i, documento.getNombreGenerado()+"@"+documento.getNombreOriginal());
		}
		
//		if (!(request.getParameter("indicadorDummy")+"").equals("") && !(request.getParameter("indicadorDummy")+"").equals("null"))		
//			procedimientoForm.setIndicadorDummy(request.getParameter("indicadorDummy").toString());
//		else
//			procedimientoForm.setIndicadorDummy(ConstantesBD.acronimoNo);
		
		//Carga la informacion de la Plantilla Relacioanada		
		procedimientoForm.setPlantillaDto(Plantillas.cargarPlantillaXRespuestaProcedimiento(
				con,
				Plantillas.consultarBasicaPlantillasResProc(con,procedimiento.getCodigoRespuesta()+"").getId()+"", 
				usuario.getCodigoInstitucion(),
				procedimiento.getCodigoRespuesta()+""));		
		
		accionCargarDtoProcedimiento(con,procedimientoForm,paciente,usuario,numeroSolicitud);
	}
	
	/**
	 * Carga el Dto de Procedimiento
	 * @param Connection con
	 * @param ProcedimientoForm forma
	 * @param PersonaBasica paciente
	 * */
	private void accionCargarDtoProcedimiento(
			Connection con,
			DtoRespuestaInterprestacion forma, 
			PersonaBasica paciente, 
			UsuarioBasico usuario,
			String numeroSolicitud)
	{
		forma.setProcedimientoDto(RespuestaProcedimientos.cargarDtoProcedimiento(
				con,
				paciente.getCodigoPersona()+"",
				numeroSolicitud+"",
				forma.getCodigoRespuesta(),
				usuario.getCodigoInstitucionInt(),
				usuario.getCodigoCentroAtencion())
				);
		
		//Valida si se debe mostrar o no la sección de Muerto
		if(forma.getProcedimientoDto().getCodigoViaIngreso() == ConstantesBD.codigoViaIngresoAmbulatorios || 
				forma.getProcedimientoDto().getCodigoViaIngreso() == ConstantesBD.codigoViaIngresoConsultaExterna)
			forma.getProcedimientoDto().getRespuestaProceEspecificoDto().setMostrarSeccionMuerto(ConstantesBD.acronimoSi);
		else
			forma.getProcedimientoDto().getRespuestaProceEspecificoDto().setMostrarSeccionMuerto(ConstantesBD.acronimoNo);
	}
	
	
	/**
	 * Creacion de margenes del reporte
	 * @return MarginBuilder
	 */
	public MarginBuilder crearMagenesReporte()
	{
		MarginBuilder margin;
		margin = margin()
		.setTop(1)
		.setBottom(30)
		.setLeft(0)
		.setRight(0)
		;

		return margin;
	}
}
