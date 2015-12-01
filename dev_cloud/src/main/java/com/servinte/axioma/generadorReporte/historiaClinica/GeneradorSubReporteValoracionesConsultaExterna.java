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
import util.ConstantesBD;
import util.ConstantesCamposParametrizables;
import util.ConstantesIntegridadDominio;
import util.InfoDatosInt;
import util.UtilidadBD;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.ordenesMedicas.UtilidadesOrdenesMedicas;
import util.reportes.dinamico.EstilosReportesDinamicosHistoriaClinica;

import com.princetonsa.dto.historiaClinica.DtoRevisionSistema;
import com.princetonsa.dto.historiaClinica.DtoValoracion;
import com.princetonsa.dto.historiaClinica.DtoValoracionObservaciones;
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
import com.princetonsa.mundo.solicitudes.Solicitud;
import com.servinte.axioma.dto.historiaClinica.HistoricoImagenPlantillaDto;

/**
 * @author JorOsoVe
 *
 */ 
public class GeneradorSubReporteValoracionesConsultaExterna 
{

	/**
	 * 
	 * @param dto
	 * @param usuario
	 * @param paciente
	 * @param numeroSolicitud
	 * @param reportFormatoHc 
	 * @param codigoEspecialidad
	 * @param codigoCita
	 * @return
	 * @throws Exception 
	 */
	public List<JasperReportBuilder> generarReporte(int numeroSolicitud,DtoFiltroImpresionHistoriaClinica filtro,UsuarioBasico usuario, PersonaBasica paciente, JasperReportBuilder reportFormatoHc) throws Exception 
	{
		Connection con=UtilidadBD.abrirConexion();
		int codigoCita=Solicitud.obtenerCodigoCita(con,numeroSolicitud);
		int codigoEspecialidad=Solicitud.obtenerEspecilidadSolicitadaCita(con, numeroSolicitud);
		List<JasperReportBuilder> reportes = new ArrayList<JasperReportBuilder>();


		if(Solicitud.esConsultaExterna(con, numeroSolicitud)){

			//SE carga de nuevo la plantilla con los valores a travï¿½s del codigoPK de la plantilla ingresada
			DtoPlantilla plantilla=Plantillas.cargarPlantillaXSolicitud(
					con, 
					usuario.getCodigoInstitucionInt(), 
					ConstantesCamposParametrizables.funcParametrizableValoracionConsulta,
					ConstantesBD.codigoNuncaValido, //centro de costo 
					ConstantesBD.codigoNuncaValido, // codigo sexo 
					ConstantesBD.codigoNuncaValido, // especialidad 
					Plantillas.obtenerCodigoPlantillaXIngreso(con, ConstantesBD.codigoNuncaValido, paciente.getCodigoPersona(), numeroSolicitud),
					true, 
					paciente.getCodigoPersona(), 
					ConstantesBD.codigoNuncaValido, //codigo inngreso no aplica
					numeroSolicitud,
					ConstantesBD.codigoNuncaValido,
					ConstantesBD.codigoNuncaValido,
					false
			);

			Valoraciones mundoValoracion = new Valoraciones();
			//SE carga de nuevo la valoracion
			mundoValoracion.cargarConsulta(con, paciente,numeroSolicitud+"", codigoCita+"");



			HashMap diagnosticosRelacionado=mundoValoracion.getDiagnosticosRelacionados();




			List<ComponentBuilder> listaComponentes = new ArrayList<ComponentBuilder>();



			//PONER TITULO DE LA EVOLUCION
			{
				HorizontalListBuilder itemComponentTitulo;


				TextFieldBuilder<String> texto;
				TextFieldBuilder<String> titulo;

				InfoDatosInt especialidad=UtilidadesOrdenesMedicas.obtenerEspecialidadSolicitadaInterconsulta(con, numeroSolicitud+"");

				titulo=cmp.text("Valoración "+Utilidades.getNombreEspecialidad(con, codigoEspecialidad))
				.setStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.LEFT).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK)));
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
						listaComponentes.add(createComponentInformacionGeneral(seccionFija, mundoValoracion.getValoracion()));
						break;
					case ConstantesCamposParametrizables.seccionFijaRevisionSistemas:
						listaComponentes.add(createComponentRevisionPorSistema(seccionFija, mundoValoracion.getValoracion().getRevisionesSistemas()));						
						break;
					case ConstantesCamposParametrizables.seccionFijaCausaExterna:
						listaComponentes.add(createComponentCausaExterna(seccionFija, mundoValoracion));						
						break;
					case ConstantesCamposParametrizables.seccionFijaFinalidadConsulta:
						listaComponentes.add(createComponentFinalidadConsulta(seccionFija, mundoValoracion));						
						break;

					case ConstantesCamposParametrizables.seccionFijaDiagnosticos:						
						listaComponentes.add(createComponentDiagnosticos(seccionFija, mundoValoracion.getValoracion().getDiagnosticos(),diagnosticosRelacionado,mundoValoracion.getValoracion().getValoracionConsulta().getNombreTipoDiagnostico(),false,null));						
						break;

					case ConstantesCamposParametrizables.seccionFijaConceptoConsulta:
						ComponentBuilder item=createComponentConceptoConsulta(seccionFija, mundoValoracion.getValoracion());
						if(item!=null)
							listaComponentes.add(item);						
						break;

					case ConstantesCamposParametrizables.seccionFijaObservaciones:						
						ArrayList<HorizontalListBuilder> list=createComponentObservaciones(seccionFija, mundoValoracion.getValoracion().getObservaciones());
						for(HorizontalListBuilder comHor:list)
						{
							listaComponentes.add(comHor);
						}
						break;
					case ConstantesCamposParametrizables.seccionFijaFechaProximoControl:						
						item=createComponentFechaProximoControl(seccionFija, mundoValoracion.getValoracion());
						if(item!=null)
							listaComponentes.add(item);												
						break;

					case ConstantesCamposParametrizables.seccionFijaProfesionalResponsable:						
						listaComponentes.add(createComponentProfesionalResponsable(seccionFija, mundoValoracion,usuario,numeroSolicitud));						
						break;

					}
					ArrayList<HorizontalListBuilder> list=ComunImpresionHistoriaClinica.seccionesParametrizables(seccionFija,paciente,mundoValoracion.getValoracion().getSignosVitales(),mundoValoracion.getValoracion().getHistoriaMenstrual(),mundoValoracion.getValoracion().getOftalmologia(),mundoValoracion.getValoracion().getPediatria(),new ArrayList<HistoricoImagenPlantillaDto>());
					for(HorizontalListBuilder comHor:list)
					{
						listaComponentes.add(comHor);
					}
				}
			}
			for(DtoElementoParam elemento:plantilla.getSeccionesValor())
			{
				DtoSeccionParametrizable seccionValor = (DtoSeccionParametrizable)elemento;

				ArrayList<HorizontalListBuilder> list=ComunImpresionHistoriaClinica.seccionesValorParametrizables(seccionValor,paciente);
				for(HorizontalListBuilder comHor:list)
				{
					listaComponentes.add(comHor);
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

			UtilidadBD.closeConnection(con);
		}
		if(!reportes.isEmpty()){
			//espacio entre reportes para que no queden pegados
			reportFormatoHc.summary(cmp.text("").setHeight(5));
		}
		return reportes;
	}
	
	

	/**
	 * 
	 * @param dto
	 * @param seccionFija
	 * @param mundoValoracion
	 * @return
	 */
	private ComponentBuilder createComponentProfesionalResponsable(DtoSeccionFija seccionFija,Valoraciones mundoValoracion,UsuarioBasico usuario, Integer numeroSolicitud) 
	{
		HorizontalListBuilder itemComponent=null;
		
		String firmaConsultada ="";
		
		Connection con=UtilidadBD.abrirConexion();
		
		firmaConsultada=Persona.obtenerFirmaDigitalMedico(con, numeroSolicitud);
		UtilidadBD.closeConnection(con);
		
		
		String folderFirmas=System.getProperty("ADJUNTOS");
		
		folderFirmas=folderFirmas.replace("../","");
		
		String firmaStr = ValoresPorDefecto.getDirectorioAxiomaBase()+folderFirmas+System.getProperty("FIRMADIGITAL")+"/"+firmaConsultada;
		LineBuilder linea=cmp.line().setDimension(5, 1);
		TextFieldBuilder<String>  imagen=cmp.text("    ");

		TextFieldBuilder<String> texto1=cmp.text(mundoValoracion.getValoracion().getProfesional().getNombreyRMPersonalSalud()).setHorizontalAlignment(HorizontalAlignment.LEFT);
		TextFieldBuilder<String> texto2=cmp.text(mundoValoracion.getValoracion().getFechaGrabacion()+" "+mundoValoracion.getValoracion().getHoraGrabacion()).setHorizontalAlignment(HorizontalAlignment.LEFT);
		itemComponent=cmp.horizontalList(cmp.verticalList(cmp.image(firmaStr).setDimension(90, 60),
				linea,
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
	 * @param valoracion
	 * @return
	 */
	private ComponentBuilder createComponentFechaProximoControl(DtoSeccionFija seccionFija,DtoValoracion valoracion) 
	{
		HorizontalListBuilder itemComponent=null;
		TextFieldBuilder<String> texto;
		
		
		if(!UtilidadTexto.isEmpty(valoracion.getValoracionConsulta().getFechaProximoControl()))
		{
			texto=cmp.text(seccionFija.getNombreSeccion()).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeNegrilla).setHorizontalAlignment(HorizontalAlignment.LEFT));
			itemComponent=cmp.horizontalList(texto);
			
			texto=cmp.text(valoracion.getValoracionConsulta().getFechaProximoControl()).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde).setHorizontalAlignment(HorizontalAlignment.LEFT));
			itemComponent.add(texto);
			
			
		}
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
	private ArrayList<HorizontalListBuilder> createComponentObservaciones(DtoSeccionFija seccionFija,ArrayList<DtoValoracionObservaciones> arrayObservaciones) 
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
	 * @param valoracion
	 * @return
	 */
	private ComponentBuilder createComponentConceptoConsulta(DtoSeccionFija seccionFija,DtoValoracion valoracion) 
	{
		HorizontalListBuilder itemComponent=null;
		TextFieldBuilder<String> texto;
		
		
		for(DtoValoracionObservaciones obs :valoracion.getObservaciones())
		{
			if(obs.getTipo().equals(ConstantesIntegridadDominio.acronimoConceptoConsulta))
			{
				texto=cmp.text(obs.getLabel()).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeNegrilla).setHorizontalAlignment(HorizontalAlignment.LEFT));
				itemComponent=cmp.horizontalList(texto);
				
				texto=cmp.text(obs.getValor()).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde).setHorizontalAlignment(HorizontalAlignment.LEFT));
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
	private ComponentBuilder createComponentDiagnosticos(DtoSeccionFija seccionFija,ArrayList arrayDiagnosticos, HashMap diagnosticosRelacionado,String nombreTipoDiagnosticos,boolean esHospitalizacion,Diagnostico diagnosticoIngreso) 
	{

		int numDiagRelacionados=Utilidades.convertirAEntero(diagnosticosRelacionado.get("numRegistros")+"", true);

		if((esHospitalizacion&&!diagnosticoIngreso.getAcronimo().trim().equals(""))||
				(arrayDiagnosticos.size()>0)||
				(numDiagRelacionados>0)){

			HorizontalListBuilder itemComponent;
			TextFieldBuilder<String> texto;
			TextFieldBuilder<String> titulo;

			String tempo="Diagnósticos";
			titulo=cmp.text(tempo).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeNegrilla).setHorizontalAlignment(HorizontalAlignment.LEFT));
			itemComponent=cmp.horizontalList(titulo);


			if(esHospitalizacion&&!diagnosticoIngreso.getAcronimo().trim().equals(""))
			{
				itemComponent.newRow();
				texto=cmp.text("Dx. Ingreso").setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeNegrilla).setHorizontalAlignment(HorizontalAlignment.LEFT));
				itemComponent.add(texto);
				texto=cmp.text(diagnosticoIngreso.getAcronimo()+"-"+diagnosticoIngreso.getTipoCIE()+"  "+diagnosticoIngreso.getNombre()+"").setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde).setHorizontalAlignment(HorizontalAlignment.LEFT));
				itemComponent.add(texto);

			}


			if(arrayDiagnosticos.size()>0)
			{
				itemComponent.newRow();
				texto=cmp.text("Dx. Principal").setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeNegrilla).setHorizontalAlignment(HorizontalAlignment.LEFT));
				itemComponent.add(texto);
				texto=cmp.text(((Diagnostico)arrayDiagnosticos.get(0)).getAcronimo()+"-"+((Diagnostico)arrayDiagnosticos.get(0)).getTipoCIE()+"  "+((Diagnostico)arrayDiagnosticos.get(0)).getNombre()+"").setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde).setHorizontalAlignment(HorizontalAlignment.LEFT));
				itemComponent.add(texto);
				texto=cmp.text("Tipo de Dx Principal: "+nombreTipoDiagnosticos).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde).setHorizontalAlignment(HorizontalAlignment.LEFT));
				itemComponent.add(texto);



			}

			if(numDiagRelacionados>0)
			{
				texto=cmp.text("Dx. Relacionados").setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeNegrilla).setHorizontalAlignment(HorizontalAlignment.LEFT));
				itemComponent.newRow().add(texto);
				for(int i=0;i<numDiagRelacionados;i++)
				{
					String diagRel=diagnosticosRelacionado.get(i+"")+"";
					String temp="Diagnósticos relacionado No. "+(i+1)+":";
					texto=cmp.text(temp+"").setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeNegrilla).setHorizontalAlignment(HorizontalAlignment.LEFT));
					itemComponent.newRow().add(texto);
					String[] vector = diagRel.split(ConstantesBD.separadorSplit); 
					temp=vector[0]+"-"+vector[1]+"  "+vector[2];
					texto=cmp.text(temp).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde).setHorizontalAlignment(HorizontalAlignment.LEFT));
					itemComponent.add(texto);
				}			
			}
			return itemComponent;
		}else{
			TextFieldBuilder<String> vacio=cmp.text("");
			HorizontalListBuilder itemComponentVacio;
			itemComponentVacio=cmp.horizontalList(vacio);
			return itemComponentVacio;
		}

	}
	
	
	
	/**
	 * 
	 * @param dto
	 * @param seccionFija
	 * @param mundoValoracion
	 * @return
	 */
	private ComponentBuilder createComponentFinalidadConsulta(DtoSeccionFija seccionFija,Valoraciones mundoValoracion) 
	{
		HorizontalListBuilder itemComponent;
		
			
		TextFieldBuilder<String> texto;
		TextFieldBuilder<String> titulo;
		if(!UtilidadTexto.isEmpty(mundoValoracion.getValoracion().getValoracionConsulta().getNombreFinalidadConsulta())){
		titulo=cmp.text(seccionFija.getNombreSeccion()).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeNegrilla).setHorizontalAlignment(HorizontalAlignment.LEFT));
		itemComponent=cmp.horizontalList(titulo);
		
		texto=cmp.text(mundoValoracion.getValoracion().getValoracionConsulta().getNombreFinalidadConsulta()).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde).setHorizontalAlignment(HorizontalAlignment.LEFT));
		itemComponent.add(texto);
		}else{
			titulo=cmp.text("");
			itemComponent=cmp.horizontalList(titulo);
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
	private ComponentBuilder createComponentCausaExterna(DtoSeccionFija seccionFija,Valoraciones mundoValoracion) 
	{
		HorizontalListBuilder itemComponent;
		
			
		TextFieldBuilder<String> texto;
		TextFieldBuilder<String> titulo;
		
		if(!UtilidadTexto.isEmpty(mundoValoracion.getValoracion().getNombreCausaExterna())){
		titulo=cmp.text(seccionFija.getNombreSeccion()).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeNegrilla).setHorizontalAlignment(HorizontalAlignment.LEFT));
		itemComponent=cmp.horizontalList(titulo);
		
		texto=cmp.text(mundoValoracion.getValoracion().getNombreCausaExterna()).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde).setHorizontalAlignment(HorizontalAlignment.LEFT));
		itemComponent.add(texto);
		}else{
			titulo=cmp.text("");
			itemComponent=cmp.horizontalList(titulo);
		}
		
		return itemComponent;
		
	}

	
	/**
	 * 
	 * @param dto
	 * @param seccionFija
	 * @param revisionArray
	 * @return
	 */
	private ComponentBuilder createComponentRevisionPorSistema(DtoSeccionFija seccionFija,ArrayList<DtoRevisionSistema> revisionArray) 
	{
		HorizontalListBuilder itemComponent;
		TextFieldBuilder<String> texto;
		TextFieldBuilder<String> titulo;
		
		if(revisionArray.size()>0){
			titulo=cmp.text(seccionFija.getNombreSeccion()).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeNegrilla).setHorizontalAlignment(HorizontalAlignment.LEFT));
		}else{
			titulo=cmp.text("");
		}
		
		itemComponent=cmp.horizontalList(titulo);
		
		for(DtoRevisionSistema revisionSistema:revisionArray)
		{
			String temp=revisionSistema.getNombre()+"";
			if(revisionSistema.isMultiple())
			{
				for(int i=0;i<revisionSistema.getOpciones().size();i++)
				{
					if(i>0)
						temp=temp+" - ";
					temp=temp+revisionSistema.getOpciones().get(i).getNombre();
				}
				
			}
			else
			{
				temp=temp+"    "+revisionSistema.getValor()+" "+revisionSistema.getUnidadMedida();
			}
			if(revisionSistema.getEstadoNormal()!=null&&revisionSistema.getEstadoNormal().booleanValue())
				temp=temp+"    "+revisionSistema.getValorVerdadero();
			if(revisionSistema.getEstadoNormal()!=null&&!revisionSistema.getEstadoNormal().booleanValue())
				temp=temp+"    "+revisionSistema.getValorFalso();
			
			temp=temp+"    Descripción: "+revisionSistema.getDescripcion();

			texto=cmp.text(temp).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde).setHorizontalAlignment(HorizontalAlignment.LEFT));
			itemComponent.newRow().add(texto);
		}
		return itemComponent;
	}
	

	/**
	 * 
	 * @param dto
	 * @param seccionFija
	 * @param valoracion
	 * @return
	 */
	private ComponentBuilder createComponentInformacionGeneral(DtoSeccionFija seccionFija,DtoValoracion valoracion) 
	{
		HorizontalListBuilder itemComponent;
		TextFieldBuilder<String> texto;
		
		TextFieldBuilder<String> titulo=cmp.text(seccionFija.getNombreSeccion()).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeNegrilla).setHorizontalAlignment(HorizontalAlignment.LEFT));
		itemComponent=cmp.horizontalList(titulo);
		
		String fila1="";
		
		if(!UtilidadTexto.isEmpty(valoracion.getFechaValoracion())){
			fila1+="Fecha de la consulta: "+valoracion.getFechaValoracion();
		}
		
		if(!UtilidadTexto.isEmpty(valoracion.getHoraValoracion())){
			fila1+=" Hora de la consulta: "+valoracion.getHoraValoracion();
		}
		
		texto=cmp.text(fila1).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeNegrilla).setHorizontalAlignment(HorizontalAlignment.LEFT));
		itemComponent.newRow().add(texto);
		
		
		String fila2="";
		
		if(!UtilidadTexto.isEmpty(valoracion.getFechaGrabacion())){
			fila2+="Fecha atención cita: "+valoracion.getFechaGrabacion();
		}
		
		
		if(!UtilidadTexto.isEmpty(valoracion.getHoraGrabacion())){
			fila2+=" Hora atención cita: "+valoracion.getHoraGrabacion();
			
		}
		texto=cmp.text(fila2).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeNegrilla).setHorizontalAlignment(HorizontalAlignment.LEFT));
		itemComponent.newRow().add(texto);
		
		String tempo="";
		/**
		 * MT 7001: Se debe quitar el campo "Fue accidente de trabajo" de la impresion de valoraciones en historia clinica
		 * @author jeilones
		 * @date 28/05/2013 
		 * */
		/*String tempo="¿Fue accidente de trabajo?: "+(valoracion.getEventoFueAccidenteTrabajo()?"Si":"No");
		texto=cmp.text(tempo).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde).setHorizontalAlignment(HorizontalAlignment.LEFT));
		itemComponent.add(texto);*/

		if(!UtilidadTexto.isEmpty( String.valueOf(valoracion.getValoracionConsulta().getPrimeraVez()))){
			tempo="Consulta de : "+(valoracion.getValoracionConsulta().getPrimeraVez()?"primera vez":"control");
			texto=cmp.text(tempo).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde).setHorizontalAlignment(HorizontalAlignment.LEFT));
			itemComponent.add(texto);
		}
		
		
		for(DtoValoracionObservaciones obs :valoracion.getObservaciones())
		{
			if(obs.getTipo().equals(ConstantesIntegridadDominio.acronimoEnfermedadActual)||obs.getTipo().equals(ConstantesIntegridadDominio.acronimoMotivoConsulta))
			{
				titulo=cmp.text(obs.getLabel()).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeNegrilla).setHorizontalAlignment(HorizontalAlignment.LEFT));
				itemComponent.newRow().add(titulo);
				
				texto=cmp.text(obs.getValor()).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde).setHorizontalAlignment(HorizontalAlignment.LEFT));
				itemComponent.newRow().add(texto);
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
		StyleBuilder columnStyle 			= EstilosReportesDinamicosHistoriaClinica.estiloDetalleC.setBorder(stl.pen1Point().setLineColor(Color.BLACK));
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
	

}
