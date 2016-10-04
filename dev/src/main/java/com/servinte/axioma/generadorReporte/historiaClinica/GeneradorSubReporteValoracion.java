/**
 * 
 */
package com.servinte.axioma.generadorReporte.historiaClinica;

import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;
import static net.sf.dynamicreports.report.builder.DynamicReports.margin;
import static net.sf.dynamicreports.report.builder.DynamicReports.report;
import static net.sf.dynamicreports.report.builder.DynamicReports.stl;
import static net.sf.dynamicreports.report.builder.DynamicReports.template;

import java.awt.Color;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.apache.struts.util.MessageResources;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.builder.MarginBuilder;
import net.sf.dynamicreports.report.builder.ReportTemplateBuilder;
import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.builder.component.HorizontalListBuilder;
import net.sf.dynamicreports.report.builder.component.LineBuilder;
import net.sf.dynamicreports.report.builder.component.TextFieldBuilder;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.constant.HorizontalAlignment;
import net.sf.dynamicreports.report.constant.LineStyle;
import net.sf.dynamicreports.report.constant.PageOrientation;
import net.sf.dynamicreports.report.constant.PageType;
import net.sf.dynamicreports.report.constant.VerticalAlignment;
import net.sf.dynamicreports.report.definition.expression.DRIPropertyExpression;
import util.ConstantesBD;
import util.ConstantesCamposParametrizables;
import util.ConstantesIntegridadDominio;
import util.InfoDatosInt;
import util.UtilidadBD;
import util.UtilidadImpresion;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.reportes.dinamico.EstilosReportesDinamicosHistoriaClinica;

import com.princetonsa.dto.historiaClinica.DtoRevisionSistema;
import com.princetonsa.dto.historiaClinica.DtoValoracion;
import com.princetonsa.dto.historiaClinica.DtoValoracionHospitalizacion;
import com.princetonsa.dto.historiaClinica.DtoValoracionObservaciones;
import com.princetonsa.dto.historiaClinica.DtoValoracionUrgencias;
import com.princetonsa.dto.historiaClinica.parametrizacion.DtoElementoParam;
import com.princetonsa.dto.historiaClinica.parametrizacion.DtoPlantilla;
import com.princetonsa.dto.historiaClinica.parametrizacion.DtoSeccionFija;
import com.princetonsa.dto.historiaClinica.parametrizacion.DtoSeccionParametrizable;
import com.princetonsa.mundo.Persona;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.atencion.Diagnostico;
import com.princetonsa.mundo.historiaClinica.Plantillas;
import com.princetonsa.mundo.historiaClinica.Valoraciones;
import com.servinte.axioma.bl.historiaClinica.facade.HistoriaClinicaFacade;
import com.servinte.axioma.dto.historiaClinica.HistoricoImagenPlantillaDto;
import com.servinte.axioma.fwk.exception.IPSException;

/**
 * @author JorOsoVe
 *
 */
public class GeneradorSubReporteValoracion 
{
	
	/**
	 * Mensajes
	 */	
	private static final MessageResources MENSAJES = MessageResources.getMessageResources("mensajes.ApplicationResources");
	
	/**
	 * @param numeroSolicitud
	 * @param filtro
	 * @param usuario
	 * @param paciente
	 * @param viaIngreso
	 * @param reportFormatoHc
	 * @param comentariosValorUrgHospIguales - MT13612 - Parametro usado para validar si las observaciones de Urgencias son iguales a las de Hospitalización.
	 * @return
	 */
	public List<JasperReportBuilder> generarReporte(int numeroSolicitud,DtoFiltroImpresionHistoriaClinica filtro, UsuarioBasico usuario, PersonaBasica paciente,int viaIngreso, JasperReportBuilder reportFormatoHc, boolean comentariosValorUrgHospIguales) 
	{
		Connection con=UtilidadBD.abrirConexion();
		Boolean tieneValoraciones=false;
		tieneValoraciones=Valoraciones.valoracionDao().tieneValoraciones(con,String.valueOf( numeroSolicitud));
		Boolean esUrgencias=false;
		
		if (tieneValoraciones) {
			
		
		
		
		int codigoFuncionalidad=ConstantesBD.codigoNuncaValido;
		if(viaIngreso==ConstantesBD.codigoViaIngresoUrgencias)
			codigoFuncionalidad=ConstantesCamposParametrizables.funcParametrizableValoracionUrgencias;
		else if(viaIngreso==ConstantesBD.codigoViaIngresoHospitalizacion)
			codigoFuncionalidad=ConstantesCamposParametrizables.funcParametrizableValoracionHospitalizacion;
		
		//SE carga de nuevo la plantilla con los valores
		DtoPlantilla plantilla=Plantillas.cargarPlantillaXSolicitud(
									con, 
									usuario.getCodigoInstitucionInt(), 
									codigoFuncionalidad, 
									ConstantesBD.codigoNuncaValido, //centro costo 
									ConstantesBD.codigoNuncaValido, //sexo 
									ConstantesBD.codigoNuncaValido, 
									//Si no hay centro de costo se intenta buscar el consecutivo de la plantilla 
									Plantillas.obtenerCodigoPlantillaXIngreso(con, filtro.getIngreso(), paciente.getCodigoPersona(), numeroSolicitud),
									true, 
									paciente.getCodigoPersona(), 
									filtro.getIngreso(), 
									numeroSolicitud,
									ConstantesBD.codigoNuncaValido,
									ConstantesBD.codigoNuncaValido,
									false
									);
		//SE carga de nuevo la valoracion
		Valoraciones mundoValoracion = new Valoraciones();
		mundoValoracion.setPlantilla(plantilla);
		
		
		
		String nombreValoracion="Valoración de ";
		if(viaIngreso==ConstantesBD.codigoViaIngresoUrgencias)
		{
			mundoValoracion.setNumeroSolicitud(numeroSolicitud+"");
			mundoValoracion.cargarUrgencias(con,usuario,paciente,paciente.getCodigoPersona(),false);
			nombreValoracion="Urgencias";
			/**
			 * MT 5568
			 * @author javrammo
			 */
			InfoDatosInt datoAreaPaciente = mundoValoracion.getValoracionUrgencias().getDatoAreaPaciente();
			if(datoAreaPaciente != null){
				nombreValoracion = MENSAJES.getMessage("label.tituloValoracionesUrgenciasImpresion", new String[]{datoAreaPaciente.getDescripcion()}) ;
			}
			/**
			 * Fin MT 5568
			 */
			
		}
		else if(viaIngreso==ConstantesBD.codigoViaIngresoHospitalizacion)
		{
			mundoValoracion.setNumeroSolicitud(numeroSolicitud+"");
			mundoValoracion.cargarHospitalizacion(con,usuario,paciente,false);
			nombreValoracion="Hospitalización";
			/**
			 * MT 5568
			 * @author javrammo
			 */
			InfoDatosInt datoAreaPaciente = mundoValoracion.getValoracionHospitalizacion().getDatoAreaPaciente();
			if(datoAreaPaciente != null){
				nombreValoracion = MENSAJES.getMessage("label.tituloValoracionesHospitalizacionImpresion", new String[]{datoAreaPaciente.getDescripcion()}) ;
			}
			/**
			 * Fin MT 5568
			 */
		}
		

		
		
		HashMap diagnosticosRelacionado=mundoValoracion.getDiagnosticosRelacionados();
		
		
		List<JasperReportBuilder> reportes = new ArrayList<JasperReportBuilder>();
		
		List<ComponentBuilder> listaComponentes = new ArrayList<ComponentBuilder>();
		
		
		
		//PONER TITULO DE LA EVOLUCION
		{
			HorizontalListBuilder itemComponentTitulo;
			
			
			TextFieldBuilder<String> texto;
			TextFieldBuilder<String> titulo;
			
			titulo=cmp.text(nombreValoracion).setStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.LEFT).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK)));
			itemComponentTitulo=cmp.horizontalList(titulo);
			
			
			listaComponentes.add(itemComponentTitulo);
		}
		
		for(DtoSeccionFija seccionFija:plantilla.getSeccionesFijas())
		{
			if(seccionFija.isVisible())
			{
				switch(seccionFija.getCodigoSeccion())
				{
				
					case ConstantesCamposParametrizables.seccionFijaInformacionGeneral:
						if(viaIngreso==ConstantesBD.codigoViaIngresoUrgencias)
							//listo campos en blanco
							listaComponentes.add(createComponentInformacionGeneralUrgencias( seccionFija, mundoValoracion.getValoracionUrgencias()));
						else if(viaIngreso==ConstantesBD.codigoViaIngresoHospitalizacion && comentariosValorUrgHospIguales)
							//listo campos en blanco
							listaComponentes.add(createComponentInformacionGeneralHospitalizacion( seccionFija, mundoValoracion.getValoracionHospitalizacion()));
					break;
					case ConstantesCamposParametrizables.seccionFijaRevisionSistemas:
						if(viaIngreso==ConstantesBD.codigoViaIngresoUrgencias)
							//listo campos en blanco
							listaComponentes.add(createComponentRevisionPorSistema( seccionFija, mundoValoracion.getValoracionUrgencias().getRevisionesSistemas()));
						else if(viaIngreso==ConstantesBD.codigoViaIngresoHospitalizacion && comentariosValorUrgHospIguales)
							//listo campos en blanco
							listaComponentes.add(createComponentRevisionPorSistema( seccionFija, mundoValoracion.getValoracionHospitalizacion().getRevisionesSistemas()));
					break;
					case ConstantesCamposParametrizables.seccionFijaExamenFisico:
						if(viaIngreso==ConstantesBD.codigoViaIngresoUrgencias) //solo aplica para urgencias.
						{
//							if(mundoValoracion.getValoracionUrgencias().isExisteInformacionExamenFisico())
//							{
								listaComponentes.add(createComponentExamenFisico(seccionFija, mundoValoracion));


								for(DtoElementoParam elemento:plantilla.getSeccionesValor())
								{
									DtoSeccionParametrizable seccionValor = (DtoSeccionParametrizable)elemento;
									if(seccionValor.getCampos().size()>0 && seccionValor.getCampos().get(0).getEtiqueta().equals("Apariencia General")){
										ArrayList<HorizontalListBuilder> list=ComunImpresionHistoriaClinica.seccionesValorParametrizables(seccionValor,paciente);
										for(HorizontalListBuilder comHor:list)
										{
											listaComponentes.add(comHor);
										}	
									}
								}
						}
					break;
					case ConstantesCamposParametrizables.seccionFijaCausaExterna:
						if(viaIngreso==ConstantesBD.codigoViaIngresoUrgencias){

							if(!UtilidadTexto.isEmpty(seccionFija.getNombreSeccion()) &&
									!UtilidadTexto.isEmpty(mundoValoracion.getValoracionUrgencias().getNombreCausaExterna())){
								listaComponentes.add(createComponentCausaExternaUrgencias( seccionFija, mundoValoracion));
							}
						}
						else if(viaIngreso==ConstantesBD.codigoViaIngresoHospitalizacion && comentariosValorUrgHospIguales){
							if(!UtilidadTexto.isEmpty(seccionFija.getNombreSeccion()) && 
									!UtilidadTexto.isEmpty(mundoValoracion.getValoracionHospitalizacion().getNombreCausaExterna())){
								listaComponentes.add(createComponentCausaExternaHospitalizacion( seccionFija, mundoValoracion));
							}
						}
					break;
					case ConstantesCamposParametrizables.seccionFijaFinalidadConsulta:
						if(viaIngreso==ConstantesBD.codigoViaIngresoUrgencias){
							if(!UtilidadTexto.isEmpty(seccionFija.getNombreSeccion()) ||
									!UtilidadTexto.isEmpty(mundoValoracion.getValoracionUrgencias().getValoracionConsulta().getNombreFinalidadConsulta())){
								listaComponentes.add(createComponentFinalidadConsultaUrgencias( seccionFija, mundoValoracion));
							}
						}
						else if(viaIngreso==ConstantesBD.codigoViaIngresoHospitalizacion && comentariosValorUrgHospIguales){
							if(!UtilidadTexto.isEmpty(seccionFija.getNombreSeccion()) &&
									!UtilidadTexto.isEmpty(mundoValoracion.getValoracionHospitalizacion().getValoracionConsulta().getNombreFinalidadConsulta())){
							listaComponentes.add(createComponentFinalidadConsultaHospitalizacion( seccionFija, mundoValoracion));
							}
						}
						
					break;
					case ConstantesCamposParametrizables.seccionFijaConductaSeguir:
						if(viaIngreso==ConstantesBD.codigoViaIngresoUrgencias) { //solo aplica para urgencias.
							listaComponentes.add(createComponentConductaSeguir(seccionFija, mundoValoracion));
								/* Alberto Ovalle mt 5749: se modifica la vista de la valoracion de Urgencias*/
								List<DtoValoracion> listaOrdenada = mundoValoracion.obtenerValoracionesOrdenada(con,usuario, paciente,String.valueOf(numeroSolicitud));
								for (DtoValoracion valoracion : listaOrdenada) {
									valoracion.setValor(UtilidadImpresion.arreglarCampoStringImpresion(valoracion.getValor()));
								}
								List <HorizontalListBuilder> listaOrden = createComponentConductaVistaObservaciones(seccionFija,listaOrdenada);
								for(HorizontalListBuilder comHor:listaOrden) {
								listaComponentes.add(comHor);
								} 
				        }	
					break;
					case ConstantesCamposParametrizables.seccionFijaDiagnosticos:						
						if(viaIngreso==ConstantesBD.codigoViaIngresoUrgencias)
							listaComponentes.add(createComponentDiagnosticos( seccionFija, mundoValoracion.getValoracionUrgencias().getDiagnosticos(),diagnosticosRelacionado,mundoValoracion.getValoracionUrgencias().getValoracionConsulta().getNombreTipoDiagnostico(),false,null));
						else if(viaIngreso==ConstantesBD.codigoViaIngresoHospitalizacion && comentariosValorUrgHospIguales)
							listaComponentes.add(createComponentDiagnosticos( seccionFija, mundoValoracion.getValoracionHospitalizacion().getDiagnosticos(),diagnosticosRelacionado,mundoValoracion.getValoracionHospitalizacion().getValoracionConsulta().getNombreTipoDiagnostico(),false,mundoValoracion.getValoracionHospitalizacion().getDiagnosticoIngreso()));
					break;
					
					case ConstantesCamposParametrizables.seccionFijaObservaciones:
//						if(viaIngreso==ConstantesBD.codigoViaIngresoUrgencias)
//						{
//							if(mundoValoracion.getValoracionUrgencias().isExisteInformacionSeccionObservaciones())
//							{
//								
//								//ArrayList<HorizontalListBuilder> list=createComponentObservaciones( seccionFija, mundoValoracion.getValoracionUrgencias().getObservaciones());
//								//for(HorizontalListBuilder comHor:list)
//								//{
//								//	listaComponentes.add(comHor);
//								//}
//								
//								List<DtoValoracion> listaOrdenada = mundoValoracion.obtenerValoracionesOrdenada(con,usuario, paciente,String.valueOf( numeroSolicitud));
//								List <HorizontalListBuilder> listaOrden = createComponentConductaVistaObservaciones(seccionFija,listaOrdenada);
//								for(HorizontalListBuilder comHor:listaOrden)
//								{
//								listaComponentes.add(comHor);
//								}
//							}
//						}
//						else if(viaIngreso==ConstantesBD.codigoViaIngresoHospitalizacion)
//						{
//							if(mundoValoracion.getValoracionHospitalizacion().isExisteInformacionSeccionObservaciones())
//							{
//								
//								//ArrayList<HorizontalListBuilder> list=createComponentObservaciones( seccionFija, mundoValoracion.getValoracionHospitalizacion().getObservaciones());
//								//for(HorizontalListBuilder comHor:list)
//								//{
//								//	listaComponentes.add(comHor);
//								//}
//								List<DtoValoracion> listaOrdenada = mundoValoracion.obtenerValoracionesObservacion(con,usuario, paciente,String.valueOf( numeroSolicitud));
//								List <HorizontalListBuilder> listaOrden = createComponentObservacionesHospitalizacion(seccionFija,listaOrdenada);
//								for(HorizontalListBuilder comHor:listaOrden)
//								{
//								listaComponentes.add(comHor);
//								}
//								
//							}
//						}
						
						if(viaIngreso==ConstantesBD.codigoViaIngresoHospitalizacion) {
							/* Alberto Ovalle mt 5749: se modifica para que se visualice las observaciones de Hospitalizacion*/
							if (mundoValoracion.getValoracionHospitalizacion().isExisteInformacionSeccionObservaciones()) {
									List<DtoValoracion> listaOrdenada = mundoValoracion.obtenerValoracionesObservacion(con,usuario, paciente,String.valueOf( numeroSolicitud));
									List <HorizontalListBuilder> listaOrden = createComponentObservacionesHospitalizacion(seccionFija,listaOrdenada);
									for(HorizontalListBuilder comHor:listaOrden) {
									listaComponentes.add(comHor);
									}
							 } 
							 

						}
					break;
					
					case ConstantesCamposParametrizables.seccionFijaProfesionalResponsable:
						if(viaIngreso==ConstantesBD.codigoViaIngresoUrgencias){ 
							if(!UtilidadTexto.isEmpty(mundoValoracion.getValoracionUrgencias().getProfesional().getInformacionGeneralPersonalSalud()) &&
									!UtilidadTexto.isEmpty(mundoValoracion.getValoracionUrgencias().getFechaGrabacion()+" "+mundoValoracion.getValoracionUrgencias().getHoraGrabacion().trim())	 ){
								listaComponentes.add(createComponentProfesionalResponsableUrgencias( seccionFija, mundoValoracion));
							}
						}
						else if(viaIngreso==ConstantesBD.codigoViaIngresoHospitalizacion && comentariosValorUrgHospIguales){
							if(!UtilidadTexto.isEmpty(mundoValoracion.getValoracionHospitalizacion().getProfesional().getInformacionGeneralPersonalSalud()) 
									&& !UtilidadTexto.isEmpty(mundoValoracion.getValoracionHospitalizacion().getFechaGrabacion()+" "+mundoValoracion.getValoracionHospitalizacion().getHoraGrabacion().trim())){
								listaComponentes.add(createComponentProfesionalResponsableHospitalizacion( seccionFija, mundoValoracion));
							}
						}
					break;
					
					case ConstantesCamposParametrizables.seccionFijaProfesionalQueResponde:
						if(viaIngreso==ConstantesBD.codigoViaIngresoUrgencias){ //solo aplica para urgencias.
							esUrgencias=true;	
						}
						
						listaComponentes.add(createComponentProfesionalQueResponde( seccionFija, mundoValoracion,usuario,numeroSolicitud,esUrgencias));
						
					break;
				}
				if(viaIngreso==ConstantesBD.codigoViaIngresoUrgencias) 
				{
					List<HistoricoImagenPlantillaDto> dtohip = new ArrayList<HistoricoImagenPlantillaDto>();
					try {
						dtohip = new HistoriaClinicaFacade().valoracionesPorId(numeroSolicitud);
					} catch (IPSException e) {
						e.printStackTrace();
					}
					
					ArrayList<HorizontalListBuilder> list=ComunImpresionHistoriaClinica.seccionesParametrizables(seccionFija,paciente,mundoValoracion.getValoracionUrgencias().getSignosVitales(),mundoValoracion.getValoracionUrgencias().getHistoriaMenstrual(),mundoValoracion.getValoracionUrgencias().getOftalmologia(),mundoValoracion.getValoracionUrgencias().getPediatria(),dtohip);
					for(HorizontalListBuilder comHor:list)
					{
						listaComponentes.add(comHor);
					}
				}
				else if(viaIngreso==ConstantesBD.codigoViaIngresoHospitalizacion && comentariosValorUrgHospIguales)
				{
					List<HistoricoImagenPlantillaDto> dtohip = new ArrayList<HistoricoImagenPlantillaDto>();
					try {
						dtohip = new HistoriaClinicaFacade().valoracionesPorId(numeroSolicitud);
					} catch (IPSException e) {
						e.printStackTrace();
					}
					
					ArrayList<HorizontalListBuilder> list=ComunImpresionHistoriaClinica.seccionesParametrizables(seccionFija,paciente,mundoValoracion.getValoracionHospitalizacion().getSignosVitales(),mundoValoracion.getValoracionHospitalizacion().getHistoriaMenstrual(),mundoValoracion.getValoracionHospitalizacion().getOftalmologia(),mundoValoracion.getValoracionHospitalizacion().getPediatria(),dtohip);
					for(HorizontalListBuilder comHor:list)
					{
						listaComponentes.add(comHor);
					}
				}
			}
		}
		for(DtoElementoParam elemento:plantilla.getSeccionesValor())
		{
			DtoSeccionParametrizable seccionValor = (DtoSeccionParametrizable)elemento;
			if(seccionValor.getCampos().size()>0 && !seccionValor.getCampos().get(0).getEtiqueta().equals("Apariencia General")){
				ArrayList<HorizontalListBuilder> list=ComunImpresionHistoriaClinica.seccionesValorParametrizables(seccionValor,paciente);
				for(HorizontalListBuilder comHor:list)
				{
					listaComponentes.add(comHor);
				}	
			}
		}

		
		ComponentBuilder[] componentesArray= new ComponentBuilder[listaComponentes.size()];
		for (int j = 0; j < listaComponentes.size(); j++) 
		{
			JasperReportBuilder reporteInterno=report();
			reporteInterno.setTemplate(crearPlantillaReporte());
			reporteInterno.setPageMargin(crearMagenesReporte());
			
			reporteInterno.summary(cmp.verticalList(listaComponentes.get(j)));
			
			reporteInterno.build();
			
			reportFormatoHc.summary(cmp.subreport(reporteInterno));
				
			reportes.add(reporteInterno);
		}
		
			if(!reportes.isEmpty()){
				//espacio entre valoraciones 
				reportFormatoHc.summary(cmp.text("").setHeight(8));
			}
			
		UtilidadBD.closeConnection(con);
		return reportes;
		
		}else{
			List<JasperReportBuilder> reportes = new ArrayList<JasperReportBuilder>();
			return reportes;
		}
	}
	
	

	



	

	/**
	 * 
	 * @param dto
	 * @param seccionFija
	 * @param mundoValoracion
	 * @param diagnosticosRelacionado
	 * @return
	 */
	private ComponentBuilder createComponentProfesionalQueResponde( DtoSeccionFija seccionFija,Valoraciones mundoValoracion,UsuarioBasico usuario,Integer numeroSolicitud,Boolean esUrgencias) 
	{
		HorizontalListBuilder itemComponent=null;
		String firmaConsultada ="";
		TextFieldBuilder<String> texto1=null;
		TextFieldBuilder<String> texto2=null;
		
		Connection con=UtilidadBD.abrirConexion();
		
		firmaConsultada=Persona.obtenerFirmaDigitalMedico(con, numeroSolicitud);
		UtilidadBD.closeConnection(con);
		//String folderFirmas=System.getProperty("ADJUNTOS");
		String path = ValoresPorDefecto.getDirectorioAxiomaBase();
		String directorio = "upload" + System.getProperty("file.separator");
		//folderFirmas=folderFirmas.replace("../","");
		
		String firmaStr = path + directorio +  System.getProperty("file.separator")+System.getProperty("FIRMADIGITAL")+System.getProperty("file.separator")+firmaConsultada;
		LineBuilder linea=cmp.line().setDimension(5, 1);
		TextFieldBuilder<String>  imagen=cmp.text("    ");

		if(esUrgencias){
			texto1=cmp.text(mundoValoracion.getValoracionUrgencias().getProfesional().getNombreyRMPersonalSalud()).setHorizontalAlignment(HorizontalAlignment.LEFT).setStretchWithOverflow(Boolean.TRUE);
			texto2=cmp.text(mundoValoracion.getValoracionUrgencias().getFechaGrabacion()+" "+mundoValoracion.getValoracionUrgencias().getHoraGrabacion()).setHorizontalAlignment(HorizontalAlignment.LEFT).setStretchWithOverflow(Boolean.TRUE);
		}else{
			texto1=cmp.text(mundoValoracion.getValoracionHospitalizacion().getProfesional().getNombreyRMPersonalSalud()).setHorizontalAlignment(HorizontalAlignment.LEFT).setStretchWithOverflow(Boolean.TRUE);
			texto2=cmp.text(mundoValoracion.getValoracionHospitalizacion().getFechaGrabacion()+" "+mundoValoracion.getValoracionHospitalizacion().getHoraGrabacion()).setHorizontalAlignment(HorizontalAlignment.LEFT).setStretchWithOverflow(Boolean.TRUE);
		}
		itemComponent=cmp.horizontalList(cmp.verticalList(cmp.image(firmaStr).setDimension(90, 60)
				,linea,
				texto1,
				texto2));
		itemComponent.add(cmp.text(""));
		itemComponent.add(cmp.text(""));
				
		
		return cmp.horizontalList(itemComponent.setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeNegrilla)));
	}

	


	/**
	 * 
	 * @param dto
	 * @param seccionFija
	 * @param mundoValoracion
	 * @return
	 */
	private ComponentBuilder createComponentProfesionalResponsableUrgencias( DtoSeccionFija seccionFija,Valoraciones mundoValoracion) 
	{
		HorizontalListBuilder itemComponent=null;
		
			
		TextFieldBuilder<String> texto;
		TextFieldBuilder<String> titulo;
		titulo=cmp.text(seccionFija.getNombreSeccion()).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeNegrilla).setHorizontalAlignment(HorizontalAlignment.LEFT));
		itemComponent=cmp.horizontalList(titulo);
		
		texto=cmp.text(mundoValoracion.getValoracionUrgencias().getProfesional().getInformacionGeneralPersonalSalud()).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde).setHorizontalAlignment(HorizontalAlignment.LEFT)).setStretchWithOverflow(Boolean.TRUE);
		itemComponent.add(texto);
		
		texto=cmp.text(mundoValoracion.getValoracionUrgencias().getFechaGrabacion()+" "+mundoValoracion.getValoracionUrgencias().getHoraGrabacion()).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde).setHorizontalAlignment(HorizontalAlignment.LEFT)).setStretchWithOverflow(Boolean.TRUE);
		itemComponent.add(texto);
		
		return itemComponent;
	}

	
	/**
	 * 
	 * @param dto
	 * @param seccionFija
	 * @param mundoValoracion
	 * @return
	 */
	private ComponentBuilder createComponentProfesionalResponsableHospitalizacion( DtoSeccionFija seccionFija,Valoraciones mundoValoracion) 
	{
		HorizontalListBuilder itemComponent=null;
		
			
		TextFieldBuilder<String> texto;
		TextFieldBuilder<String> titulo;
		titulo=cmp.text(seccionFija.getNombreSeccion()).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeNegrilla).setHorizontalAlignment(HorizontalAlignment.LEFT));
		itemComponent=cmp.horizontalList(titulo);
		
		texto=cmp.text(mundoValoracion.getValoracionHospitalizacion().getProfesional().getInformacionGeneralPersonalSalud()).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde).setHorizontalAlignment(HorizontalAlignment.LEFT)).setStretchWithOverflow(Boolean.TRUE);
		itemComponent.add(texto);
		
		texto=cmp.text(mundoValoracion.getValoracionHospitalizacion().getFechaGrabacion()+" "+mundoValoracion.getValoracionHospitalizacion().getHoraGrabacion()).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde).setHorizontalAlignment(HorizontalAlignment.LEFT)).setStretchWithOverflow(Boolean.TRUE);
		itemComponent.add(texto);
		
		return itemComponent;
	}

	

	

	/**
	 * 
	 * @param dto
	 * @param seccionFija
	 * @param mundoValoracion
	 * @param diagnosticosRelacionado
	 * @return
	 */
	private ArrayList<HorizontalListBuilder> createComponentObservaciones( DtoSeccionFija seccionFija,ArrayList<DtoValoracionObservaciones> arrayObservaciones) 
	{
		ArrayList<HorizontalListBuilder> itemComponentArray=new ArrayList<HorizontalListBuilder>();
			
		TextFieldBuilder<String> texto;
		TextFieldBuilder<String> titulo;

		for(DtoValoracionObservaciones observacion: arrayObservaciones )
		{
			if(observacion.getTipo().equals(ConstantesIntegridadDominio.acronimoPlanDiagnosticoTerapeutico))
			{
				String tempo=ValoresPorDefecto.getIntegridadDominio(ConstantesIntegridadDominio.acronimoPlanDiagnosticoTerapeutico)+"";
				titulo=cmp.text(tempo).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeNegrilla).setHorizontalAlignment(HorizontalAlignment.LEFT));
				HorizontalListBuilder itemComponent=cmp.horizontalList(titulo);
				if(!observacion.getValor().equals(""))
				{
					tempo=observacion.getValor()+" \n "+observacion.getFecha()+" - "+observacion.getHora()+" \n "+observacion.getProfesional().getInformacionGeneralPersonalSalud();
					texto=cmp.text(tempo).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde).setHorizontalAlignment(HorizontalAlignment.LEFT));
					itemComponent.newRow().add(texto);
				}
				itemComponentArray.add(itemComponent);
			}
			else if(observacion.getTipo().equals(ConstantesIntegridadDominio.acronimoComentariosGenerales))
			{
				String tempo=ValoresPorDefecto.getIntegridadDominio(ConstantesIntegridadDominio.acronimoComentariosGenerales)+"";
				titulo=cmp.text(tempo).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeNegrilla).setHorizontalAlignment(HorizontalAlignment.LEFT));
				HorizontalListBuilder itemComponent=cmp.horizontalList(titulo);
				if(!observacion.getValor().equals(""))
				{
					tempo=observacion.getValor()+" \n "+observacion.getFecha()+" - "+observacion.getHora()+" \n "+observacion.getProfesional().getInformacionGeneralPersonalSalud();
					texto=cmp.text(tempo).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde).setHorizontalAlignment(HorizontalAlignment.LEFT));
					itemComponent.newRow().add(texto);
				}
				itemComponentArray.add(itemComponent);
			}
			else if(observacion.getTipo().equals(ConstantesIntegridadDominio.acronimoPronostico))
			{
				String tempo=ValoresPorDefecto.getIntegridadDominio(ConstantesIntegridadDominio.acronimoPronostico)+"";
				titulo=cmp.text(tempo).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeNegrilla).setHorizontalAlignment(HorizontalAlignment.LEFT));
				HorizontalListBuilder itemComponent=cmp.horizontalList(titulo);
				
				if(!observacion.getValor().equals(""))
				{
					tempo=observacion.getValor()+" \n "+observacion.getFecha()+" - "+observacion.getHora()+" \n "+observacion.getProfesional().getInformacionGeneralPersonalSalud();
					texto=cmp.text(tempo).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde).setHorizontalAlignment(HorizontalAlignment.LEFT));
					itemComponent.newRow().add(texto);
				}
				itemComponentArray.add(itemComponent);
			}
		}
	
		
		
		return itemComponentArray;
	}
	
	
	

	/**
	 * 
	 * @param dto
	 * @param seccionFija
	 * @param mundoValoracion
	 * @return
	 */
	private ComponentBuilder createComponentDiagnosticos( DtoSeccionFija seccionFija,ArrayList arrayDiagnosticos, HashMap diagnosticosRelacionado,String nombreTipoDiagnosticos,boolean esHospitalizacion,Diagnostico diagnosticoIngreso) 
	{
		HorizontalListBuilder itemComponent;
		
			
		TextFieldBuilder<String> texto;
		TextFieldBuilder<String> titulo;
		
		String tempo="Diagnósticos";
		titulo=cmp.text(tempo).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeNegrilla).setHorizontalAlignment(HorizontalAlignment.LEFT));
		itemComponent=cmp.horizontalList(titulo);
		
		
		if(esHospitalizacion&&!diagnosticoIngreso.getAcronimo().trim().equals(""))
		{
			itemComponent.newRow();
			texto=cmp.text("Dx. Ingreso").setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeNegrilla).setHorizontalAlignment(HorizontalAlignment.LEFT)).setStretchWithOverflow(Boolean.TRUE);
			itemComponent.add(texto);
			texto=cmp.text(diagnosticoIngreso.getAcronimo()+"-"+diagnosticoIngreso.getTipoCIE()+"  "+diagnosticoIngreso.getNombre()+"").setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde).setHorizontalAlignment(HorizontalAlignment.LEFT)).setStretchWithOverflow(Boolean.TRUE);
			itemComponent.add(texto);

		}
		
		
		if(arrayDiagnosticos.size()>0)
		{
			itemComponent.newRow();
			texto=cmp.text("Dx. Principal").setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeNegrilla).setHorizontalAlignment(HorizontalAlignment.LEFT)).setStretchWithOverflow(Boolean.TRUE);
			itemComponent.add(texto);
			texto=cmp.text(((Diagnostico)arrayDiagnosticos.get(0)).getAcronimo()+"-"+((Diagnostico)arrayDiagnosticos.get(0)).getTipoCIE()+"  "+((Diagnostico)arrayDiagnosticos.get(0)).getNombre()+"").setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde).setHorizontalAlignment(HorizontalAlignment.LEFT)).setStretchWithOverflow(Boolean.TRUE);
			itemComponent.add(texto);
			texto=cmp.text("Tipo de Dx Principal: "+nombreTipoDiagnosticos).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde).setHorizontalAlignment(HorizontalAlignment.LEFT)).setStretchWithOverflow(Boolean.TRUE);
			itemComponent.add(texto);
			
			
			
		}
		int numDiagRelacionados=Utilidades.convertirAEntero(diagnosticosRelacionado.get("numRegistros")+"", true);
		if(numDiagRelacionados>0)
		{
			texto=cmp.text("Dx. Relacionados").setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeNegrilla).setHorizontalAlignment(HorizontalAlignment.LEFT)).setStretchWithOverflow(Boolean.TRUE);
			itemComponent.newRow().add(texto);
			for(int i=0;i<numDiagRelacionados;i++)
			{
				String diagRel=diagnosticosRelacionado.get(i+"")+"";
				String temp="Diagnósticos relacionado No. "+(i+1)+":";
				texto=cmp.text(temp+"").setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeNegrilla).setHorizontalAlignment(HorizontalAlignment.LEFT)).setStretchWithOverflow(Boolean.TRUE);
				itemComponent.newRow().add(texto);
				String[] vector = diagRel.split(ConstantesBD.separadorSplit); 
				temp=vector[0]+"-"+vector[1]+"  "+vector[2];
				texto=cmp.text(temp).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde).setHorizontalAlignment(HorizontalAlignment.LEFT)).setStretchWithOverflow(Boolean.TRUE);
				itemComponent.add(texto);
			}			
		}
		return itemComponent;
	}

	

	/**
	 * 
	 * @param dto
	 * @param seccionFija
	 * @param mundoValoracion
	 * @return
	 */
	private ComponentBuilder createComponentConductaSeguir( DtoSeccionFija seccionFija,Valoraciones mundoValoracion) 
	{
		HorizontalListBuilder itemComponent;
		
			
		TextFieldBuilder<String> texto;
		TextFieldBuilder<String> titulo;
		
		String tempo=seccionFija.getNombreSeccion()+": ";
		/*Alberto Ovalle mt 5749 se modifica la visualizacion de la conducta a seguir */
		//tempo=tempo+mundoValoracion.getValoracionUrgencias().getNombreConductaValoracion()+"NOMBRECONDUCTAVALORACION";
		if(!UtilidadTexto.isEmpty(mundoValoracion.getValoracionUrgencias().getNombreTipoMonitoreo()))
		{
			tempo=tempo+"    "+mundoValoracion.getValoracionUrgencias().getNombreTipoMonitoreo();
		}
		
		titulo=cmp.text(tempo).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeNegrilla).setHorizontalAlignment(HorizontalAlignment.LEFT));
		itemComponent=cmp.horizontalList(titulo);
		
		
		if(!mundoValoracion.getValoracionUrgencias().getNombreAutorizador().trim().equals("")||!mundoValoracion.getValoracionUrgencias().getRelacionAutorizador().trim().equals(""))
		{
			tempo="Con la aprobación de: "+"Nombre:"+mundoValoracion.getValoracionUrgencias().getNombreAutorizador()+"     Relación: "+mundoValoracion.getValoracionUrgencias().getRelacionAutorizador();
			texto=cmp.text(tempo).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde).setHorizontalAlignment(HorizontalAlignment.LEFT));
			itemComponent.newRow().add(texto);
		}
		/*Alberto Ovalle mt 5749 se modifica la visualizacion de la conducta a seguir */
//		if(!mundoValoracion.getValoracionUrgencias().getHistoricoConductasValoracion().toString().trim().equals(""))
//		{
//			tempo="Descripción: \n "+"AQUI ES DESCRIPCION"+mundoValoracion.getValoracionUrgencias().getHistoricoConductasValoracion()+"HISTORICOCONDUCTAVALORACION";
//			texto=cmp.text(tempo).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde).setHorizontalAlignment(HorizontalAlignment.LEFT));
//			itemComponent.newRow().add(texto);
//		}
		
		if(mundoValoracion.getValoracionUrgencias().getCodigoConductaValoracion()==ConstantesBD.codigoConductaSeguirSalidaSinObservacion||mundoValoracion.getValoracionUrgencias().getCodigoConductaValoracion()==ConstantesBD.codigoConductaSeguirRemitirMayorComplejidad)
		{
			tempo="Estado a la Salida: ";
			if(mundoValoracion.getValoracionUrgencias().getEstadoSalida().equals(ConstantesBD.acronimoSi))
			{
				tempo=tempo+" Vivo.";
				texto=cmp.text(tempo).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde).setHorizontalAlignment(HorizontalAlignment.LEFT));
				itemComponent.newRow().add(texto);
				if(!mundoValoracion.getValoracionUrgencias().getValoracionConsulta().getNumeroDiasIncapacidad().trim().equals("")||!mundoValoracion.getValoracionUrgencias().getValoracionConsulta().getObservacionesIncapacidad().trim().equals(""))
				{
					tempo=" Incapacidad Funcional.";
					if(!mundoValoracion.getValoracionUrgencias().getValoracionConsulta().getNumeroDiasIncapacidad().trim().equals(""))
					{
						tempo=tempo+"  Número de días: "+mundoValoracion.getValoracionUrgencias().getValoracionConsulta().getNumeroDiasIncapacidad();
					}
					if(!mundoValoracion.getValoracionUrgencias().getValoracionConsulta().getObservacionesIncapacidad().trim().equals(""))
					{
						tempo=tempo+"  "+mundoValoracion.getValoracionUrgencias().getValoracionConsulta().getObservacionesIncapacidad();
					}
					texto=cmp.text(tempo).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde).setHorizontalAlignment(HorizontalAlignment.LEFT));
					itemComponent.newRow().add(texto);
				}
				
			}
			else if(mundoValoracion.getValoracionUrgencias().getEstadoSalida().equals(ConstantesBD.acronimoNo))
			{
				tempo=tempo+" Muerto.";
				texto=cmp.text(tempo).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde).setHorizontalAlignment(HorizontalAlignment.LEFT));
				itemComponent.newRow().add(texto);
				
				if(!UtilidadTexto.isEmpty(mundoValoracion.getValoracionUrgencias().getDiagnosticoMuerte().getValor()))
				{
					tempo="Diagnóstico de muerte: "+mundoValoracion.getValoracionUrgencias().getDiagnosticoMuerte().getAcronimo()+"-"+mundoValoracion.getValoracionUrgencias().getDiagnosticoMuerte().getTipoCIE()+"   "+mundoValoracion.getValoracionUrgencias().getDiagnosticoMuerte().getNombre();
					texto=cmp.text(tempo).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde).setHorizontalAlignment(HorizontalAlignment.LEFT));
					itemComponent.newRow().add(texto);
					
					tempo="Fecha de muerte: "+mundoValoracion.getValoracionUrgencias().getFechaMuerte()+"   Hora Muerte: "+mundoValoracion.getValoracionUrgencias().getHoraMuerte()+"   Certificado defunción:"+ValoresPorDefecto.getIntegridadDominio(mundoValoracion.getValoracionUrgencias().getCertificadoDefuncion());
					texto=cmp.text(tempo).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde).setHorizontalAlignment(HorizontalAlignment.LEFT));
					itemComponent.newRow().add(texto);
				}
				
			}
		}
		
		return itemComponent;
		
	}


	/**
	 * 
	 * @param dto
	 * @param seccionFija
	 * @param mundoValoracion
	 * @return
	 */
	private ComponentBuilder createComponentFinalidadConsultaUrgencias( DtoSeccionFija seccionFija,Valoraciones mundoValoracion) 
	{
		HorizontalListBuilder itemComponent;
		
			
		TextFieldBuilder<String> texto;
		TextFieldBuilder<String> titulo;
		
		titulo=cmp.text(seccionFija.getNombreSeccion()).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeNegrilla).setHorizontalAlignment(HorizontalAlignment.LEFT));
		itemComponent=cmp.horizontalList(titulo);
		
		texto=cmp.text(mundoValoracion.getValoracionUrgencias().getValoracionConsulta().getNombreFinalidadConsulta()).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde).setHorizontalAlignment(HorizontalAlignment.LEFT)).setStretchWithOverflow(Boolean.TRUE);
		itemComponent.add(texto);
		
		return itemComponent;
		
	}
	
	/**
	 * 
	 * @param dto
	 * @param seccionFija
	 * @param mundoValoracion
	 * @return
	 */
	private ComponentBuilder createComponentFinalidadConsultaHospitalizacion( DtoSeccionFija seccionFija,Valoraciones mundoValoracion) 
	{
		HorizontalListBuilder itemComponent;
		
			
		TextFieldBuilder<String> texto;
		TextFieldBuilder<String> titulo;
		
		titulo=cmp.text(seccionFija.getNombreSeccion()).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeNegrilla).setHorizontalAlignment(HorizontalAlignment.LEFT));
		itemComponent=cmp.horizontalList(titulo);
		
		texto=cmp.text(mundoValoracion.getValoracionHospitalizacion().getValoracionConsulta().getNombreFinalidadConsulta()).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde).setHorizontalAlignment(HorizontalAlignment.LEFT)).setStretchWithOverflow(Boolean.TRUE);
		itemComponent.add(texto);
		
		return itemComponent;
		
	}




	

	/**
	 * 
	 * @param dto
	 * @param seccionFija
	 * @param mundoValoracion
	 * @return
	 */
	private ComponentBuilder createComponentCausaExternaUrgencias( DtoSeccionFija seccionFija,Valoraciones mundoValoracion) 
	{
		HorizontalListBuilder itemComponent;
		
			
		TextFieldBuilder<String> texto;
		TextFieldBuilder<String> titulo;
		
		titulo=cmp.text(seccionFija.getNombreSeccion()).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeNegrilla).setHorizontalAlignment(HorizontalAlignment.LEFT));
		itemComponent=cmp.horizontalList(titulo);
		
		texto=cmp.text(mundoValoracion.getValoracionUrgencias().getNombreCausaExterna()).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde).setHorizontalAlignment(HorizontalAlignment.LEFT)).setStretchWithOverflow(Boolean.TRUE);
		itemComponent.add(texto);
		
		return itemComponent;
		
	}
	
	/**
	 * 
	 * @param dto
	 * @param seccionFija
	 * @param mundoValoracion
	 * @return
	 */
	private ComponentBuilder createComponentCausaExternaHospitalizacion( DtoSeccionFija seccionFija,Valoraciones mundoValoracion) 
	{
		HorizontalListBuilder itemComponent;
		
			
		TextFieldBuilder<String> texto;
		TextFieldBuilder<String> titulo;
		
		titulo=cmp.text(seccionFija.getNombreSeccion()).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeNegrilla).setHorizontalAlignment(HorizontalAlignment.LEFT));
		itemComponent=cmp.horizontalList(titulo);
		
		texto=cmp.text(mundoValoracion.getValoracionHospitalizacion().getNombreCausaExterna()).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde).setHorizontalAlignment(HorizontalAlignment.LEFT)).setStretchWithOverflow(Boolean.TRUE);
		itemComponent.add(texto);
		
		return itemComponent;
		
	}
	
	


	/**
	 * 
	 * @param dto
	 * @param seccionFija
	 * @param mundoValoracion
	 * @return
	 */
	private ComponentBuilder createComponentExamenFisico( DtoSeccionFija seccionFija,Valoraciones mundoValoracion) 
	{
		HorizontalListBuilder itemComponent;
		HorizontalListBuilder itemComponentVacio;
		
			
		TextFieldBuilder<String> texto;
		TextFieldBuilder<String> titulo;
		
		titulo=cmp.text(seccionFija.getNombreSeccion()).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeNegrilla).setHorizontalAlignment(HorizontalAlignment.LEFT));
		itemComponent=cmp.horizontalList(titulo);
		itemComponentVacio=cmp.horizontalList();
		
		String tempo="";
		if(!UtilidadTexto.isEmpty(mundoValoracion.getValoracionUrgencias().getNombreEstadoConciencia()) &&
				!mundoValoracion.getValoracionUrgencias().getNombreEstadoConciencia().trim().equals("No definido")	)
		{
			tempo=tempo+" Estado de conciencia: "+mundoValoracion.getValoracionUrgencias().getNombreEstadoConciencia() ;
			tempo=tempo+"   Descripción: "+mundoValoracion.getValoracionUrgencias().getDescripcionEstadoConciencia() ;
			texto=cmp.text(tempo).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde).setHorizontalAlignment(HorizontalAlignment.LEFT)).setStretchWithOverflow(Boolean.TRUE);
			itemComponent.newRow().add(texto);
			return itemComponent;
		}
		
		
		return itemComponentVacio;
		
	}

	




	private ComponentBuilder createComponentRevisionPorSistema( DtoSeccionFija seccionFija,ArrayList<DtoRevisionSistema> revisionArray) 
	{
		HorizontalListBuilder itemComponent;
		TextFieldBuilder<String> texto;
		TextFieldBuilder<String> titulo;
		Boolean pintar=false;
		
		titulo=cmp.text(seccionFija.getNombreSeccion()).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeNegrilla).setHorizontalAlignment(HorizontalAlignment.LEFT));
		itemComponent=cmp.horizontalList(titulo);
		
		for(DtoRevisionSistema revisionSistema:revisionArray)
		{
			pintar=false;
			String temp=revisionSistema.getNombre()+"";
			if(revisionSistema.isMultiple())
			{
				for(int i=0;i<revisionSistema.getOpciones().size();i++)
				{
					if(i>0)
						temp=temp+" - ";
					temp=temp+revisionSistema.getOpciones().get(i).getNombre();
				}
				if(!UtilidadTexto.isEmpty(temp)){
					pintar=true;
				}
				
			}
			else
			{
				temp=temp+"    "+revisionSistema.getValor()+" "+revisionSistema.getUnidadMedida();
				if(!UtilidadTexto.isEmpty(revisionSistema.getValor()) ||
						!UtilidadTexto.isEmpty(revisionSistema.getUnidadMedida())){
					pintar=true;
				}
			}
			if(revisionSistema.getEstadoNormal()!=null&&revisionSistema.getEstadoNormal().booleanValue()){
				temp=temp+"    "+revisionSistema.getValorVerdadero();
				if(!UtilidadTexto.isEmpty(revisionSistema.getValorVerdadero())){
					pintar=true;
				}
			}
			if(revisionSistema.getEstadoNormal()!=null&&!revisionSistema.getEstadoNormal().booleanValue()){
				temp=temp+"    "+revisionSistema.getValorFalso();
				if(!UtilidadTexto.isEmpty(revisionSistema.getValorFalso())){
					pintar=true;
				}
			}
			
			temp=temp+"    Descripción: "+revisionSistema.getDescripcion();
			if(!UtilidadTexto.isEmpty(revisionSistema.getDescripcion())){
				pintar=true;
			}
			if(pintar){
				texto=cmp.text(temp).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde).setHorizontalAlignment(HorizontalAlignment.LEFT)).setStretchWithOverflow(Boolean.TRUE);
				itemComponent.newRow().add(texto);
			}
		}
		return itemComponent;
	}

	
	/**
	 * 
	 * @param dto
	 * @param seccionFija
	 * @param mundoValoracion
	 * @return
	 */
	private ComponentBuilder createComponentInformacionGeneralUrgencias( DtoSeccionFija seccionFija,DtoValoracionUrgencias valoracion) 
	{
		HorizontalListBuilder itemComponent;
		TextFieldBuilder<String> texto;
		Boolean pintar1=false;
		TextFieldBuilder<String> titulo=cmp.text(seccionFija.getNombreSeccion()).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeNegrilla).setHorizontalAlignment(HorizontalAlignment.LEFT));
		TextFieldBuilder<String> titulo1=cmp.text(" Fecha: "+valoracion.getFechaValoracion()+" Hora: "+valoracion.getHoraValoracion()).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeNegrilla).setHorizontalAlignment(HorizontalAlignment.LEFT));
		
		if(!UtilidadTexto.isEmpty(String.valueOf(valoracion.getFechaValoracion()))  || 
				!UtilidadTexto.isEmpty(String.valueOf(valoracion.getHoraValoracion())) 	){
			itemComponent=cmp.horizontalList(titulo,titulo1);
		}else{
			itemComponent=cmp.horizontalList(titulo);
		}
		
		String tempo="";
		
		
		if(!UtilidadTexto.isEmpty(String.valueOf(valoracion.getEventoFueAccidenteTrabajo())))
		{// davgommo: Incidencia 6631, no se debe imprimir este campo
//			pintar1=true;
//			tempo="¿Fue accidente de trabajo?: "+(valoracion.getEventoFueAccidenteTrabajo()?"Si":"No");
		}
		if(!UtilidadTexto.isEmpty(valoracion.getEstadoLlegada()+""))
		{
			pintar1=true;
			tempo=tempo+" ¿El paciente llegó por sus propios medios?: "+(valoracion.getEstadoLlegada() ?"Si":"No");
		}
		
		if(!UtilidadTexto.isEmpty(valoracion.getDescripcionEstadoLlegada())  &&
				!valoracion.getEstadoLlegada()){
			pintar1=true;
			tempo=tempo+" ¿Cuál?: "+valoracion.getDescripcionEstadoLlegada();
			
		}
		
		
		if(!UtilidadTexto.isEmpty(valoracion.getEstadoEmbriaguez()+""))
		{
			pintar1=true;
			tempo=tempo+" ¿Estado de embriaguez:?: "+(valoracion.getEstadoEmbriaguez() ?"Si":"No");
		}
		
		
		
		
		
		if(pintar1){
			texto=cmp.text(tempo).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde).setHorizontalAlignment(HorizontalAlignment.LEFT));
			itemComponent.newRow().add(texto);
		}
		
		for(DtoValoracionObservaciones obs :valoracion.getObservaciones())
		{
			if(obs.getTipo().equals(ConstantesIntegridadDominio.acronimoEnfermedadActual)||obs.getTipo().equals(ConstantesIntegridadDominio.acronimoMotivoConsulta))
			{
				titulo=cmp.text(obs.getLabel()).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeNegrilla).setHorizontalAlignment(HorizontalAlignment.LEFT));
				
				obs.setValor(UtilidadImpresion.arreglarCampoStringImpresion(obs.getValor()));
				
				texto=cmp.text(obs.getValor()).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde).setHorizontalAlignment(HorizontalAlignment.LEFT)).setStretchWithOverflow(Boolean.TRUE);
				if(!UtilidadTexto.isEmpty(String.valueOf(texto))){
				itemComponent.newRow().add(titulo);
				itemComponent.newRow().add(texto);
				}
			}
		}
		return itemComponent;
	}
	
	/**
	 * 
	 * @param dto
	 * @param seccionFija
	 * @param mundoValoracion
	 * @return
	 */
	private ComponentBuilder createComponentInformacionGeneralHospitalizacion( DtoSeccionFija seccionFija,DtoValoracionHospitalizacion valoracion) 
	{
		HorizontalListBuilder itemComponent;
		TextFieldBuilder<String> texto;
		
		TextFieldBuilder<String> titulo=cmp.text(seccionFija.getNombreSeccion()).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeNegrilla).setHorizontalAlignment(HorizontalAlignment.LEFT));
		TextFieldBuilder<String> titulo1=cmp.text(" Fecha: "+valoracion.getFechaValoracion()+" Hora: "+valoracion.getHoraValoracion()).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeNegrilla).setHorizontalAlignment(HorizontalAlignment.LEFT));
		
		if(!UtilidadTexto.isEmpty(valoracion.getFechaValoracion())||
			!UtilidadTexto.isEmpty(valoracion.getHoraValoracion())	){
			itemComponent=cmp.horizontalList(titulo,titulo1);
		}else{
			itemComponent=cmp.horizontalList(titulo);
		}
		
		
		
		String tempo="";
		Boolean banderaPintar=false;
		
		if(!UtilidadTexto.isEmpty(String.valueOf(valoracion.getEventoFueAccidenteTrabajo()))){
//			davgommo:incidencia 6631, no se debe imprimir este campo
			//		banderaPintar=true;
//					tempo="¿Fue accidente de trabajo?: "+(valoracion.getEventoFueAccidenteTrabajo()?"Si":"No");
				}
		
		if(!UtilidadTexto.isEmpty(valoracion.getNombreOrigenAdmision())){
			banderaPintar=true;
			tempo=tempo+" Origen de la admisión: "+valoracion.getNombreOrigenAdmision();
		}
		
		if(valoracion.getCodigoOrigenAdmision()!=ConstantesBD.codigoOrigenAdmisionHospitalariaEsUrgencias)
		{
			banderaPintar=true;
			tempo=tempo+" Descripción: "+valoracion.getTextoOrigenNoUrgencias();
		}	

		if(banderaPintar){
			texto=cmp.text(tempo).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde).setHorizontalAlignment(HorizontalAlignment.LEFT));
			itemComponent.newRow().add(texto);
		}

		for(DtoValoracionObservaciones obs :valoracion.getObservaciones())
		{
			if(obs.getTipo().equals(ConstantesIntegridadDominio.acronimoEnfermedadActual)||obs.getTipo().equals(ConstantesIntegridadDominio.acronimoMotivoConsulta))
			{
				titulo=cmp.text(obs.getLabel()).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeNegrilla).setHorizontalAlignment(HorizontalAlignment.LEFT));
				
				
				texto=cmp.text(obs.getValor()).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde).setHorizontalAlignment(HorizontalAlignment.LEFT)).setStretchWithOverflow(Boolean.TRUE);
				if(!UtilidadTexto.isEmpty(String.valueOf(obs.getValor()))){
					itemComponent.newRow().add(titulo);
					itemComponent.newRow().add(texto);
				}
			}
		}
		return itemComponent;
	}
	


	/**
	 * @return templante para columnas de tablas de subreportes
	 */
	public ReportTemplateBuilder crearPlantillaReporte()
	{
		ReportTemplateBuilder reportTemplate;

		StyleBuilder rootStyle 				= stl.style().setPadding(1).setFontSize(9);
		StyleBuilder columnStyle 			= EstilosReportesDinamicosHistoriaClinica.estiloDetalleL.setBorder(stl.pen1Point().setLineColor(Color.BLACK));
		StyleBuilder columnTitleStyle		= EstilosReportesDinamicosHistoriaClinica.estiloSubTituloSombra.setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.CENTER).setBorder(stl.pen1Point().setLineColor(Color.BLACK)).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7).setBorder(stl.pen1Point().setLineColor(Color.BLACK));
		
		reportTemplate = template();
		reportTemplate.setLocale(Locale.ENGLISH);
		//Se define el tamano de la hoja y la orientación de la misma
		reportTemplate.setPageFormat(PageType.LETTER, PageOrientation.PORTRAIT);
		reportTemplate.setColumnStyle(columnStyle);
		reportTemplate.setColumnTitleStyle(columnTitleStyle);
		
		
		
		return reportTemplate;
	}
	
	
	
	
	public MarginBuilder crearMagenesReporte()
	 {
	  MarginBuilder margin;
	  margin = margin()
	  .setLeft(0)
	  .setRight(0)
	  ;

	  return margin;
	 }
	
	/**
	 * Alberto Ovalle
	 *  mt5749
	 * metodo que imprime el componente de valoración Coducta Observaciones
	 * @param seccionFija
	 * @param lista
	 * @return List
	 */
	public List<HorizontalListBuilder>createComponentConductaVistaObservaciones(DtoSeccionFija seccionFija,List<DtoValoracion> lista){
		
  		
		ArrayList<HorizontalListBuilder> itemComponentArray=null;
  		
  		try {

  	    	TextFieldBuilder<String> texto1;
  	       	TextFieldBuilder<String> texto5;
  	    	TextFieldBuilder<String> texto6;
  	    	TextFieldBuilder<String> texto7;
  	    	TextFieldBuilder<String> texto8;
  	    	TextFieldBuilder<String> titulo;

  	    	String mensaje="";
  	  	    Valoraciones mundoValoracion = new  Valoraciones();
  		    itemComponentArray=new ArrayList<HorizontalListBuilder>(); 	
	  		for(DtoValoracion observacion: lista)
			{		
	  			
	  		String tempo="";
	  		String tempo1="";
	  		String tempo5="";
	  		String tempo6="";
	  		String tempo7="";
	  		String tempo8="";

			String fecha = mundoValoracion.conversionFormatoFechaAAp(observacion.getFechaHora());
			observacion.setFormatoFechaHora(fecha);	
			mundoValoracion.validarFechaObservaciones(lista);
			
				if(observacion.getValor()!=null &&observacion.getObservacion()!=null){
					
					tempo=(observacion.getImprimeFechaHora()!=null  && observacion.getImprimeFechaHora()? observacion.getFormatoFechaHora():"");
					tempo1=(observacion.getObservacion()!=null ? observacion.getObservacion():"");
					tempo5=(observacion.getLabel()!=null? observacion.getLabel():"");
				    tempo6=(observacion.getValor()!=null ? observacion.getValor():"");
				    tempo8=(observacion.getProfesion()!= null ? observacion.getProfesion():"")+" "+
				    (observacion.getRegistromedico()!=null? observacion.getRegistromedico():"")+" "+
				    (observacion.getEspecialidades()!=null? observacion.getEspecialidades():" ");
				    titulo=cmp.text(tempo).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeNegrilla).setHorizontalAlignment(HorizontalAlignment.LEFT));
				    HorizontalListBuilder itemComponent=cmp.horizontalList(titulo);


				    texto1=cmp.text(tempo1).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde).setHorizontalAlignment(HorizontalAlignment.LEFT));
					texto5=cmp.text(tempo5).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde).setHorizontalAlignment(HorizontalAlignment.LEFT));
					texto6=cmp.text(tempo6).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde).setHorizontalAlignment(HorizontalAlignment.LEFT));
					texto8=cmp.text(tempo8).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde).setHorizontalAlignment(HorizontalAlignment.LEFT));

					itemComponent.newRow().add(texto1);
					itemComponent.newRow().add(texto5);
					itemComponent.newRow().add(texto6);
					itemComponent.newRow().add(texto8);
					itemComponentArray.add(itemComponent);
				}
				
				else if(!(observacion.getValor()!=null) && (observacion.getObservacion()!=null)){
					
					tempo=(observacion.getImprimeFechaHora()!=null  && observacion.getImprimeFechaHora()? observacion.getFormatoFechaHora():"");
					tempo1=(observacion.getObservacion()!=null ? observacion.getObservacion():"");
					tempo8=(observacion.getProfesion()!= null ? observacion.getProfesion():"")+" "+
					(observacion.getRegistromedico()!=null? observacion.getRegistromedico():"")+" "+
					(observacion.getEspecialidades()!=null? observacion.getEspecialidades():"");
					titulo=cmp.text(tempo).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeNegrilla).setHorizontalAlignment(HorizontalAlignment.LEFT));
					HorizontalListBuilder itemComponent=cmp.horizontalList(titulo);
					texto1=cmp.text(tempo1).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde).setHorizontalAlignment(HorizontalAlignment.LEFT));
					texto8=cmp.text(tempo8).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde).setHorizontalAlignment(HorizontalAlignment.LEFT));

					itemComponent.newRow().add(texto1);
					itemComponent.newRow().add(texto8);
					itemComponentArray.add(itemComponent);
					
				}
				
				else if((observacion.getValor()!=null) && !(observacion.getObservacion()!=null)){
					
					tempo=(observacion.getImprimeFechaHora()!=null  && observacion.getImprimeFechaHora()? observacion.getFormatoFechaHora():"");
					tempo5=(observacion.getLabel()!=null? observacion.getLabel():"");
				    tempo6=(observacion.getValor()!=null ? observacion.getValor():"");
				    tempo7=(observacion.getProfesional().getInformacionObservacionesGeneralPersonalSalud()!=null ?
							observacion.getProfesional().getInformacionObservacionesGeneralPersonalSalud():"");
				    titulo=cmp.text(tempo).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeNegrilla).setHorizontalAlignment(HorizontalAlignment.LEFT));
				    HorizontalListBuilder itemComponent=cmp.horizontalList(titulo);

					texto5=cmp.text(tempo5).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde).setHorizontalAlignment(HorizontalAlignment.LEFT));
					//MT 14683 Se agrega salto de línea
					texto6=cmp.text(tempo6+"\n\n").setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde).setHorizontalAlignment(HorizontalAlignment.LEFT));
					texto7=cmp.text(tempo7).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde).setHorizontalAlignment(HorizontalAlignment.LEFT));				
					

					itemComponent.newRow().add(texto5);
					itemComponent.newRow().add(texto6);
					itemComponent.newRow().add(texto7);
					itemComponentArray.add(itemComponent);
				}
				else{
					
					mensaje = MENSAJES.getMessage("No hay parametros de consulta, Consulte Con el Administrador del Sistema") ;
				}
								
				
				
		    }
	  		
  		}
  		catch(Exception e){
  			e.printStackTrace();
  		}
  		return itemComponentArray;
      }
      
      /**
    	 * Alberto Ovalle
    	 *  mt5749
    	 * metodo que imprime el componente de valoración Observaciones Hospitalizacion
    	 * @param seccionFija
    	 * @param lista
    	 * @return List
    	 */
	
       public List<HorizontalListBuilder>createComponentObservacionesHospitalizacion(DtoSeccionFija seccionFija,List<DtoValoracion> lista){

    	   ArrayList<HorizontalListBuilder> itemComponentArray=null;
     		
     		try {

     	    	TextFieldBuilder<String> texto1;
     	       	TextFieldBuilder<String> texto5;
     	    	TextFieldBuilder<String> texto6;
     	    	TextFieldBuilder<String> texto7;
     	    	TextFieldBuilder<String> texto8;
     	    	TextFieldBuilder<String> titulo;

     	    	String mensaje="";
     	  	    Valoraciones mundoValoracion = new  Valoraciones();
     		    itemComponentArray=new ArrayList<HorizontalListBuilder>(); 	
   	  		for(DtoValoracion observacion: lista)
   			{		
   	  			
   	  		String tempo="";
   	  		String tempo1="";
   	  		String tempo5="";
   	  		String tempo6="";
   	  		String tempo7="";
   	  		String tempo8="";


   			String fecha = mundoValoracion.conversionFormatoFechaAAp(observacion.getFechaHora());
   			observacion.setFormatoFechaHora(fecha);	
   			mundoValoracion.validarFechaObservaciones(lista);
   			
   				if(observacion.getValor()!=null &&observacion.getObservacion()!=null){
   					
   					tempo=(observacion.getImprimeFechaHora()!=null  && observacion.getImprimeFechaHora()? observacion.getFormatoFechaHora():"");
   					tempo1=(observacion.getObservacion()!=null ? observacion.getObservacion():"");
   					tempo5=(observacion.getLabel()!=null? observacion.getLabel():"");
   				    tempo6=(observacion.getValor()!=null ? observacion.getValor():"");
   				    tempo8=(observacion.getProfesion()!= null ? observacion.getProfesion():"")+" "+
   				    (observacion.getRegistromedico()!=null? observacion.getRegistromedico():"")+" "+
   				    (observacion.getEspecialidades()!=null? observacion.getEspecialidades():"");
   				    titulo=cmp.text(tempo).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeNegrilla).setHorizontalAlignment(HorizontalAlignment.LEFT));
   				    HorizontalListBuilder itemComponent=cmp.horizontalList(titulo);


   				    texto1=cmp.text(tempo1).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde).setHorizontalAlignment(HorizontalAlignment.LEFT));
   					texto5=cmp.text(tempo5).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde).setHorizontalAlignment(HorizontalAlignment.LEFT));
   					texto6=cmp.text(tempo6).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde).setHorizontalAlignment(HorizontalAlignment.LEFT));
   					texto8=cmp.text(tempo8).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde).setHorizontalAlignment(HorizontalAlignment.LEFT));

   					itemComponent.newRow().add(texto1);
   					itemComponent.newRow().add(texto5);
   					itemComponent.newRow().add(texto6);
   					itemComponent.newRow().add(texto8);
   					itemComponentArray.add(itemComponent);
   				}
   				
   				else if(!(observacion.getValor()!=null) && (observacion.getObservacion()!=null)){
   					
   					tempo=(observacion.getImprimeFechaHora()!=null  && observacion.getImprimeFechaHora()? observacion.getFormatoFechaHora():"");
   					tempo1=(observacion.getObservacion()!=null ? observacion.getObservacion():"");
   					tempo8=(observacion.getProfesion()!= null ? observacion.getProfesion():"")+" "+
   					(observacion.getRegistromedico()!=null? observacion.getRegistromedico():"")+" "+
   					(observacion.getEspecialidades()!=null? observacion.getEspecialidades():"");
   					titulo=cmp.text(tempo).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeNegrilla).setHorizontalAlignment(HorizontalAlignment.LEFT));
   					HorizontalListBuilder itemComponent=cmp.horizontalList(titulo);
   					texto1=cmp.text(tempo1).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde).setHorizontalAlignment(HorizontalAlignment.LEFT));
   					texto8=cmp.text(tempo8).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde).setHorizontalAlignment(HorizontalAlignment.LEFT));

   					itemComponent.newRow().add(texto1);
   					itemComponent.newRow().add(texto8);
   					itemComponentArray.add(itemComponent);
   					
   				}
   				
   				else if((observacion.getValor()!=null) && !(observacion.getObservacion()!=null)){
   					
   					tempo=(observacion.getImprimeFechaHora()!=null  && observacion.getImprimeFechaHora()? observacion.getFormatoFechaHora():"");
   					tempo5=(observacion.getLabel()!=null? observacion.getLabel():"");
   				    tempo6=(observacion.getValor()!=null ? observacion.getValor():"");
   				    tempo7=(observacion.getProfesional().getInformacionObservacionesGeneralPersonalSalud()!=null ?
   							observacion.getProfesional().getInformacionObservacionesGeneralPersonalSalud():"");
   				    titulo=cmp.text(tempo).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeNegrilla).setHorizontalAlignment(HorizontalAlignment.LEFT));
   				    HorizontalListBuilder itemComponent=cmp.horizontalList(titulo);

   					texto5=cmp.text(tempo5).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde).setHorizontalAlignment(HorizontalAlignment.LEFT));
   					texto6=cmp.text(tempo6).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde).setHorizontalAlignment(HorizontalAlignment.LEFT));
   					texto7=cmp.text(tempo7).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde).setHorizontalAlignment(HorizontalAlignment.LEFT));				
   					

   					itemComponent.newRow().add(texto5);
   					itemComponent.newRow().add(texto6);
   					itemComponent.newRow().add(texto7);
   					itemComponentArray.add(itemComponent);
   				}
   				else{
   					
   					mensaje = MENSAJES.getMessage("No hay parametros de consulta, Consulte Con el Administrador del Sistema") ;
   				}
   								
   				
   				
   		    }
   	  		
     		}
     		catch(Exception e){
     			e.printStackTrace();
     		}
     		return itemComponentArray;
         }
      
}
