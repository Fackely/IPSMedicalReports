/**
 * 
 */
package com.servinte.axioma.generadorReporte.historiaClinica;

import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;
import static net.sf.dynamicreports.report.builder.DynamicReports.margin;
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
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.atencion.Diagnostico;
import com.princetonsa.mundo.historiaClinica.Plantillas;
import com.princetonsa.mundo.historiaClinica.Valoraciones;
import com.servinte.axioma.dto.historiaClinica.HistoricoImagenPlantillaDto;

/**
 * @author JorOsoVe
 *
 */
public class GeneradorSubReporteRespuestaInterconsultas 
{
	
	/**
	 * Mensajes
	 */	
	private static final MessageResources MENSAJES = MessageResources.getMessageResources("mensajes.ApplicationResources");
		
 
	@SuppressWarnings("rawtypes")
	public List<ComponentBuilder> generarReporte(int numeroSolicitud,DtoFiltroImpresionHistoriaClinica filtro,UsuarioBasico usuario, PersonaBasica paciente, JasperReportBuilder reportFormatoHc)
	{
		Connection con=UtilidadBD.abrirConexion();
		Boolean flagEntroProfesional=false;
		
		//SE carga de nuevo la plantilla con los valores a través del codigoPK de la plantilla ingresada
		DtoPlantilla plantilla=Plantillas.cargarPlantillaXSolicitud(
				con, 
				usuario.getCodigoInstitucionInt(), 
				ConstantesCamposParametrizables.funcParametrizableValoracionInterconsulta,
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
				false);
		
		//SE carga de nuevo la valoracion
		Valoraciones mundoValoracion = new Valoraciones();
		mundoValoracion.setPlantilla(plantilla);
		
		mundoValoracion.cargarInterconsulta(con, paciente, numeroSolicitud+"");
		
		
		HashMap diagnosticosRelacionado=mundoValoracion.getDiagnosticosRelacionados();
		
		
		List<JasperReportBuilder> reportes = new ArrayList<JasperReportBuilder>();

		List<ComponentBuilder> listaComponentes = new ArrayList<ComponentBuilder>();
		
		
		
		//PONER TITULO DE LA EVOLUCION
		{
			HorizontalListBuilder itemComponentTitulo;
			
			
			TextFieldBuilder<String> texto;
			TextFieldBuilder<String> titulo;
			
			InfoDatosInt especialidad=UtilidadesOrdenesMedicas.obtenerEspecialidadSolicitadaInterconsulta(con, numeroSolicitud+"");
			String tituloValoracion = "Interconsulta "+especialidad.getNombre();
			/**
			 * MT 5568
			 * @author javrammo
			 */
			InfoDatosInt datoAreaPaciente = mundoValoracion.getValoracion().getDatoAreaPaciente();
			if(datoAreaPaciente != null){
				tituloValoracion = MENSAJES.getMessage("label.tiutloValoracionInterconsulta", new String[]{especialidad.getNombre(),datoAreaPaciente.getDescripcion()}) ;
			}
			/**
			 * Fin MT 5568
			 */			
			
			titulo=cmp.text(tituloValoracion).setStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.LEFT).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK)));
			itemComponentTitulo=cmp.horizontalList(titulo);
			
			reportFormatoHc.summary(itemComponentTitulo);
			listaComponentes.add(itemComponentTitulo);
		}
		
		for(DtoSeccionFija seccionFija:plantilla.getSeccionesFijas())
		{
			if(seccionFija.isVisible())
			{
				ComponentBuilder componente=null;
				switch(seccionFija.getCodigoSeccion())
				{
					case ConstantesCamposParametrizables.seccionFijaInformacionGeneral:
						componente=createComponentInformacionGeneral( seccionFija, mundoValoracion.getValoracion());
						reportFormatoHc.summary(componente);
						listaComponentes.add(componente);
					break;
					case ConstantesCamposParametrizables.seccionFijaRevisionSistemas:
						componente=createComponentRevisionPorSistema( seccionFija, mundoValoracion.getValoracion().getRevisionesSistemas());
						reportFormatoHc.summary(componente);
						listaComponentes.add(componente);						
					break;
					case ConstantesCamposParametrizables.seccionFijaCausaExterna:
						componente=createComponentCausaExterna( seccionFija, mundoValoracion);
						reportFormatoHc.summary(componente);
						listaComponentes.add(componente);						
					break;
					case ConstantesCamposParametrizables.seccionFijaFinalidadConsulta:
						componente=createComponentFinalidadConsulta( seccionFija, mundoValoracion);
						reportFormatoHc.summary(componente);
						listaComponentes.add(componente);						
					break;
					
					case ConstantesCamposParametrizables.seccionFijaDiagnosticos:						
						componente=createComponentDiagnosticos( seccionFija, mundoValoracion.getValoracion().getDiagnosticos(),diagnosticosRelacionado,mundoValoracion.getValoracion().getValoracionConsulta().getNombreTipoDiagnostico(),false,null);
						reportFormatoHc.summary(componente);
						listaComponentes.add(componente);		
					break;
					
					case ConstantesCamposParametrizables.seccionFijaConceptoConsulta:
						ComponentBuilder item=createComponentConceptoConsulta( seccionFija, mundoValoracion.getValoracion());
						if(item!=null){
							reportFormatoHc.summary(item);
							listaComponentes.add(item);						
						}
					break;
					
					case ConstantesCamposParametrizables.seccionFijaObservaciones:						
						ArrayList<HorizontalListBuilder> list=createComponentObservaciones( seccionFija, mundoValoracion.getValoracion().getObservaciones());
						for(HorizontalListBuilder comHor:list)
						{
							reportFormatoHc.summary(comHor);
							listaComponentes.add(comHor);
						}
					break;
					case ConstantesCamposParametrizables.seccionFijaFechaProximoControl:						
						item=createComponentFechaProximoControl( seccionFija, mundoValoracion.getValoracion());
						if(item!=null){
							reportFormatoHc.summary(item);
							listaComponentes.add(item);												
						}
					break;
					case ConstantesCamposParametrizables.seccionFijaProfesionalQueResponde:		
						if(!flagEntroProfesional){
							componente=createComponentProfesionalResponsable( seccionFija, mundoValoracion);
							reportFormatoHc.summary(componente);		
							listaComponentes.add(componente);
							flagEntroProfesional=true;
						}
					break;
					
				}
				ArrayList<HorizontalListBuilder> list=ComunImpresionHistoriaClinica.seccionesParametrizables(seccionFija,paciente,mundoValoracion.getValoracion().getSignosVitales(),mundoValoracion.getValoracion().getHistoriaMenstrual(),mundoValoracion.getValoracion().getOftalmologia(),mundoValoracion.getValoracion().getPediatria(),new ArrayList<HistoricoImagenPlantillaDto>());
				for(HorizontalListBuilder comHor:list)
				{
					reportFormatoHc.summary(comHor);
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
				reportFormatoHc.summary(comHor);
				listaComponentes.add(comHor);
			}		
		}
		
		
		UtilidadBD.closeConnection(con);
		return listaComponentes;
	}
	
	/**
	 * 
	 * @param dto
	 * @param seccionFija
	 * @param mundoValoracion
	 * @return
	 */
	private ComponentBuilder createComponentProfesionalResponsable(DtoSeccionFija seccionFija,Valoraciones mundoValoracion) 
	{
		HorizontalListBuilder itemComponent=null;
		
		
		TextFieldBuilder<String> texto;
		TextFieldBuilder<String> titulo;
		titulo=cmp.text(seccionFija.getNombreSeccion()).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeNegrilla).setHorizontalAlignment(HorizontalAlignment.LEFT));
		itemComponent=cmp.horizontalList(titulo);
		
		texto=cmp.text(mundoValoracion.getValoracion().getProfesional().getInformacionGeneralPersonalSalud()).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde).setHorizontalAlignment(HorizontalAlignment.LEFT));
		itemComponent.add(texto);
		
		texto=cmp.text(mundoValoracion.getValoracion().getFechaGrabacion()+" "+mundoValoracion.getValoracion().getHoraGrabacion()).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde).setHorizontalAlignment(HorizontalAlignment.LEFT));
		itemComponent.add(texto);
		
		return itemComponent;
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
					tempo= UtilidadTexto.partirTextoNCaracteres(observacion.getValor(), 120) + " \n "+observacion.getFecha()+" - "+observacion.getHora()+" \n "+observacion.getProfesional().getInformacionGeneralPersonalSalud();
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
					tempo= UtilidadTexto.partirTextoNCaracteres(observacion.getValor(), 120) +" \n "+observacion.getFecha()+" - "+observacion.getHora()+" \n "+observacion.getProfesional().getInformacionGeneralPersonalSalud();
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
					tempo= UtilidadTexto.partirTextoNCaracteres(observacion.getValor(), 120) +" \n "+observacion.getFecha()+" - "+observacion.getHora()+" \n "+observacion.getProfesional().getInformacionGeneralPersonalSalud();
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
				
				texto=cmp.text(UtilidadTexto.partirTextoNCaracteres(obs.getValor(), 120)).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde).setHorizontalAlignment(HorizontalAlignment.LEFT));
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
		int numDiagRelacionados=Utilidades.convertirAEntero(diagnosticosRelacionado.get("numRegistros")+"", true);
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
		
		titulo=cmp.text(seccionFija.getNombreSeccion()).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeNegrilla).setHorizontalAlignment(HorizontalAlignment.LEFT));
		itemComponent=cmp.horizontalList(titulo);
		
		texto=cmp.text(UtilidadTexto.partirTextoNCaracteres(mundoValoracion.getValoracion().getValoracionConsulta().getNombreFinalidadConsulta(), 120)).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde).setHorizontalAlignment(HorizontalAlignment.LEFT));
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
	private ComponentBuilder createComponentCausaExterna(DtoSeccionFija seccionFija,Valoraciones mundoValoracion) 
	{
		HorizontalListBuilder itemComponent;
		
			
		TextFieldBuilder<String> texto;
		TextFieldBuilder<String> titulo;
		
		titulo=cmp.text(seccionFija.getNombreSeccion()).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeNegrilla).setHorizontalAlignment(HorizontalAlignment.LEFT));
		itemComponent=cmp.horizontalList(titulo);
		
		texto=cmp.text(UtilidadTexto.partirTextoNCaracteres(mundoValoracion.getValoracion().getNombreCausaExterna(), 120)).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde).setHorizontalAlignment(HorizontalAlignment.LEFT));
		itemComponent.add(texto);
		
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
		
		titulo=cmp.text(seccionFija.getNombreSeccion()).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeNegrilla).setHorizontalAlignment(HorizontalAlignment.LEFT));
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
	 * @param valoracionConsulta
	 * @return
	 */
	private ComponentBuilder createComponentInformacionGeneral(DtoSeccionFija seccionFija,DtoValoracion valoracion) 
	{
		HorizontalListBuilder itemComponent;
		TextFieldBuilder<String> texto;
		
		TextFieldBuilder<String> titulo=cmp.text(seccionFija.getNombreSeccion()).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeNegrilla).setHorizontalAlignment(HorizontalAlignment.LEFT));
		itemComponent=cmp.horizontalList(titulo);
		
		texto=cmp.text("Fecha de la consulta: "+valoracion.getFechaValoracion()+" Hora de la consulta: "+valoracion.getHoraValoracion()).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeNegrilla).setHorizontalAlignment(HorizontalAlignment.LEFT));
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
		
		tempo="Consulta de : "+(valoracion.getValoracionConsulta().getPrimeraVez()?"primera vez":"control");;
		texto=cmp.text(tempo).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde).setHorizontalAlignment(HorizontalAlignment.LEFT));
		itemComponent.add(texto);
		
		
		for(DtoValoracionObservaciones obs :valoracion.getObservaciones())
		{
			if(obs.getTipo().equals(ConstantesIntegridadDominio.acronimoEnfermedadActual)||obs.getTipo().equals(ConstantesIntegridadDominio.acronimoMotivoConsulta))
			{
				titulo=cmp.text(obs.getLabel()).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeNegrilla).setHorizontalAlignment(HorizontalAlignment.LEFT));
				itemComponent.newRow().add(titulo);
				
				texto=cmp.text(UtilidadTexto.partirTextoNCaracteres(obs.getValor(), 120)).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde).setHorizontalAlignment(HorizontalAlignment.LEFT));
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
