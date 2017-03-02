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
import util.ConstantesBD;
import util.ConstantesCamposParametrizables;
import util.UtilidadBD;
import util.UtilidadFileUpload;
import util.UtilidadImpresion;
import util.UtilidadTexto;
import util.ValoresPorDefecto;
import util.historiaClinica.UtilidadesHistoriaClinica;
import util.reportes.dinamico.EstilosReportesDinamicosHistoriaClinica;

import com.princetonsa.dto.historiaClinica.DtoEvolucion;
import com.princetonsa.dto.historiaClinica.DtoEvolucionComentarios;
import com.princetonsa.dto.historiaClinica.componentes.DtoHistoriaMenstrual;
import com.princetonsa.dto.historiaClinica.componentes.DtoOftalmologia;
import com.princetonsa.dto.historiaClinica.componentes.DtoPediatria;
import com.princetonsa.dto.historiaClinica.parametrizacion.DtoElementoParam;
import com.princetonsa.dto.historiaClinica.parametrizacion.DtoPlantilla;
import com.princetonsa.dto.historiaClinica.parametrizacion.DtoSeccionFija;
import com.princetonsa.dto.historiaClinica.parametrizacion.DtoSeccionParametrizable;
import com.princetonsa.mundo.Persona;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.atencion.Diagnostico;
import com.princetonsa.mundo.historiaClinica.Evoluciones;
import com.princetonsa.mundo.historiaClinica.Plantillas;
import com.princetonsa.mundo.historiaClinica.Valoraciones;
import com.servinte.axioma.bl.historiaClinica.facade.HistoriaClinicaFacade;
import com.servinte.axioma.dto.historiaClinica.HistoricoImagenPlantillaDto;
import com.servinte.axioma.dto.manejoPaciente.InformacionCentroCostoValoracionDto;
import com.servinte.axioma.fwk.exception.IPSException;

/** 
 * @author JorOsoVe
 *
 */
public class GeneradorSubReporteEvoluciones 
{
	
	/**
	 * Mensajes
	 */
	private static final MessageResources MENSAJES = MessageResources.getMessageResources("mensajes.ApplicationResources");

	/**
	 * 
	 * @param dto
	 * @param usuario
	 * @param paciente
	 * @param reportFormatoHc 
	 * @return
	 * @throws Exception 
	 * @throws NumberFormatException 
	 */
	public List<JasperReportBuilder> generarReporte(UsuarioBasico usuario, PersonaBasica paciente,int codigoEvolucion, JasperReportBuilder reportFormatoHc) throws NumberFormatException, Exception 
	{
		List<JasperReportBuilder> reportes = new ArrayList<JasperReportBuilder>();
		
		List<ComponentBuilder> listaComponentes = new ArrayList<ComponentBuilder>();
		TextFieldBuilder<String> medicoReponsable;
		Connection con=UtilidadBD.abrirConexion();
		
		DtoPlantilla plantilla=Plantillas.cargarPlantillaXEvolucion(con,
				usuario.getCodigoInstitucionInt(), 
				ConstantesBD.codigoNuncaValido,
				ConstantesBD.codigoNuncaValido, 
				Plantillas.obtenerCodigoPlantillaXEvolucion(con,codigoEvolucion),
				true,
				ConstantesBD.codigoNuncaValido,
				ConstantesBD.codigoNuncaValido,
				codigoEvolucion,
				ConstantesBD.codigoNuncaValido,
				ConstantesBD.codigoNuncaValido,
				false);
		DtoEvolucion evolucion=Evoluciones.cargarEvolucion(con, codigoEvolucion+"");
		
		
			
		//PONER TITULO DE LA EVOLUCION
		{
			HorizontalListBuilder itemComponentTitulo;
			
			
			TextFieldBuilder<String> texto;
			TextFieldBuilder<String> titulo;
			
			/**
			 * MT 5568
			 * @author javrammo
			 */
			String tituloEvolucion = "Evolución";
			InformacionCentroCostoValoracionDto infoCentroCostoValoracion = evolucion.getInfoCentroCostoValoracion();
			if(infoCentroCostoValoracion != null){
				if(infoCentroCostoValoracion.getIdTipoMonitoreo() != null){
					//Si se cuenta con informacion de tipo monitoreo, muestra area y tipo monitoreo
					tituloEvolucion = MENSAJES.getMessage("label.evolucion.titulo.area.conMonitoreo", new String[]{infoCentroCostoValoracion.getDescripcionCentroCosto(),infoCentroCostoValoracion.getDescripcionTipoMonitoreo()});
				}else {
					//Solo muestra info del area.
					tituloEvolucion = MENSAJES.getMessage("label.evolucion.titulo.area.sinMonitoreo", new String[]{infoCentroCostoValoracion.getDescripcionCentroCosto()});
				}				
			}
			/**
			 * Fin MT 5568
			 */
			
			titulo=cmp.text(tituloEvolucion).setStyle(stl.style().setVerticalAlignment(VerticalAlignment.MIDDLE).setHorizontalAlignment(HorizontalAlignment.LEFT).setBackgroundColor(Color.LIGHT_GRAY).bold().setFontSize(7).setBorder(stl.pen(new Float(0.4), LineStyle.SOLID).setLineColor(Color.BLACK)));
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
						if(!UtilidadTexto.isEmpty(evolucion.getFechaEvolucion()) &&
								!UtilidadTexto.isEmpty(evolucion.getHoraEvolucion())){
							listaComponentes.add(createComponentInformacionGeneral(seccionFija, evolucion));
						}
					break;
					case ConstantesCamposParametrizables.seccionFijaDiagnosticos:
						listaComponentes.add(createComponentFijaDiagnosticos(seccionFija, evolucion));
					break;
					case ConstantesCamposParametrizables.seccionFijaDatosSubjetivos:
						if(!UtilidadTexto.isEmpty(evolucion.getInformacionDadaPaciente()))
						{
							listaComponentes.add(createComponentDatosSubjetivos(seccionFija, evolucion));
						}
					break;
					case ConstantesCamposParametrizables.seccionfijaHallazgosImportantes:
						if(!UtilidadTexto.isEmpty(evolucion.getHallazgosImportantes()))
						{
							listaComponentes.add(createComponentHallazgosImportantes(seccionFija, evolucion));
						}
					break;
					case ConstantesCamposParametrizables.seccionFijaAnalisis:
						if(!UtilidadTexto.isEmpty(evolucion.getDescComplicacion()))
						{
							listaComponentes.add(createComponentFijaAnalisis(seccionFija, evolucion));
						}
					break;
					case ConstantesCamposParametrizables.seccionFijaPlanManejo:
						if(!UtilidadTexto.isEmpty(evolucion.getPronostico()))
						{
							listaComponentes.add(createComponentFijaPlanManejo(seccionFija, evolucion));
						}
					break;
					case ConstantesCamposParametrizables.seccionFijaComentariosGenerales:
						if(evolucion.getComentarios().size()>0)
						{
							listaComponentes.add(createComponentComentariosGenerales(seccionFija, evolucion));
						}
					break;
					case ConstantesCamposParametrizables.seccionFijaConductaSeguir:
						listaComponentes.add(createComponentConductaSeguir(seccionFija, evolucion));
					break;
				}
				
				List<HistoricoImagenPlantillaDto> dtohip = new ArrayList<HistoricoImagenPlantillaDto>();
				try {
					dtohip = new HistoriaClinicaFacade().evolucionesPorId(codigoEvolucion);
				} catch (IPSException e) {
					e.printStackTrace();
				}
				
				ArrayList<HorizontalListBuilder> list=ComunImpresionHistoriaClinica.seccionesParametrizables(seccionFija,paciente,evolucion.getSignosVitales(),new DtoHistoriaMenstrual(),new DtoOftalmologia(),new DtoPediatria(),dtohip);
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
		
		listaComponentes.add(createComponentProfesionalQueResponde(evolucion));
		
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

			//espacio entre subreportes 
			reportFormatoHc.summary(cmp.text("").setHeight(5));
		}
		
		UtilidadBD.closeConnection(con);
		return reportes;
		
		
		
	}

	/**
	 * 
	 * @param dto
	 * @param seccionFija
	 * @param evolucion
	 * @return
	 * @throws Exception 
	 * @throws NumberFormatException 
	 */
	private ComponentBuilder createComponentConductaSeguir(DtoSeccionFija seccionFija,DtoEvolucion evolucion) throws NumberFormatException, Exception 
	{
		HorizontalListBuilder itemComponent=cmp.horizontalList();
		String tempo="";
		
		TextFieldBuilder<String> titulo;
		
		
		TextFieldBuilder<String> texto;

		if(!UtilidadTexto.isEmpty(evolucion.getNombreConductaSeguir())){
		tempo="Conducta a seguir: "+evolucion.getNombreConductaSeguir();
		texto=cmp.text(tempo).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde).setHorizontalAlignment(HorizontalAlignment.LEFT));
		itemComponent=cmp.horizontalList(texto);
		}
		
		if(evolucion.getCodigoConductaSeguir()==ConstantesBD.codigoConductaASeguirRemitirEvolucion)
		{
			if(!UtilidadTexto.isEmpty(String.valueOf(ValoresPorDefecto.getIntegridadDominio(evolucion.getTipoReferencia())))){
			tempo="Tipo Referencia: "+ValoresPorDefecto.getIntegridadDominio(evolucion.getTipoReferencia());
			texto=cmp.text(tempo).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde).setHorizontalAlignment(HorizontalAlignment.LEFT));
			itemComponent.add(texto);
			}
		}
		//mt5887
		else if(evolucion.getCodigoConductaSeguir()==ConstantesBD.codigoConductaASeguirCambioTipoMonitoreo || evolucion.getCodigoConductaSeguir()==ConstantesBD.codigoConductaASeguirTrasladoCuidadoEspecial)//codigoConductaASeguirTrasladoCuidadoEspecial)
		{
			
			if(!UtilidadTexto.isEmpty(evolucion.getProcedQuirurgicosObst())){
				Connection con = UtilidadBD.abrirConexion(); 
				String nombreTipomonitoreo=UtilidadesHistoriaClinica.consultarTipoMonitoreoXCodigo(Integer.valueOf(evolucion.getProcedQuirurgicosObst()), con);
				UtilidadBD.closeConnection(con);

				tempo="Tipo Monitoreo: "+nombreTipomonitoreo;
			}else{
				tempo="Tipo Monitoreo: "+evolucion.getProcedQuirurgicosObst();
			}
			
			if(!UtilidadTexto.isEmpty(tempo)){
			texto=cmp.text(tempo).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde).setHorizontalAlignment(HorizontalAlignment.LEFT));
			itemComponent.add(texto);
			}
			
			
		}
		
		if(evolucion.getCodigoConductaSeguir()==ConstantesBD.codigoConductaASeguirCambioTipoMonitoreo)
		{
			//MT5887
			if(!UtilidadTexto.isEmpty(evolucion.getCentroCostoMonitoreo())){
				Connection con = UtilidadBD.abrirConexion(); 
				String centroCostosMonitoreo=UtilidadesHistoriaClinica.consultarCentroCostosMonitoreoXCodigo(Integer.valueOf(evolucion.getCentroCostoMonitoreo()), con);
				UtilidadBD.closeConnection(con);
		
				tempo="Centro Costos: "+centroCostosMonitoreo;
			}else{
				tempo="Centro Costos: "+evolucion.getCentroCostoMonitoreo();
			}
			
			if(!UtilidadTexto.isEmpty(tempo)){
			texto=cmp.text(tempo).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde).setHorizontalAlignment(HorizontalAlignment.LEFT));
			itemComponent.add(texto);
			}
			//Fin MT
		}
		if(evolucion.isOrdenSalida())
		{
			itemComponent.newRow();
			tempo="Generar Orden Egreso: Si "+evolucion.getNombreDestinoSalida()+" "+(evolucion.getCodigoDestinoSalida()==ConstantesBD.codigoDestinoSalidaOtro&&!UtilidadTexto.isEmpty(evolucion.getOtroDestinoSalida())?("Cual?: "+evolucion.getOtroDestinoSalida()):"");
			tempo=tempo+"  "+(UtilidadTexto.getBoolean(evolucion.getMuerto())?"Estado a la Salida: Muerto":"Estado a la Salida: Vivo");
			texto=cmp.text(tempo).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde).setHorizontalAlignment(HorizontalAlignment.LEFT));
			itemComponent.add(texto);
		}
		else
		{
			itemComponent.newRow();
			tempo="Generar Orden Egreso: No ";
			texto=cmp.text(tempo).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde).setHorizontalAlignment(HorizontalAlignment.LEFT));
			itemComponent.add(texto);
		}
		
		if(UtilidadTexto.getBoolean(evolucion.getMuerto()))
		{
			itemComponent.newRow();
			tempo="Diagnóstico Muerte. ";
			texto=cmp.text(tempo).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde).setHorizontalAlignment(HorizontalAlignment.LEFT));
			itemComponent.add(texto);
			
			if(UtilidadTexto.isEmpty(evolucion.getDiagnosticoMuerte().getAcronimo()))
			{
				itemComponent.newRow();
				tempo="Diagnóstico Muerte. \n";
				tempo=""+evolucion.getDiagnosticoMuerte().getAcronimo()+"-"+evolucion.getDiagnosticoMuerte().getTipoCIE()+" "+evolucion.getDiagnosticoMuerte().getNombre();
				texto=cmp.text(tempo).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde).setHorizontalAlignment(HorizontalAlignment.LEFT));
				itemComponent.add(texto);
				
			}
			
			itemComponent.newRow();
			tempo="Fecha Muerte: "+evolucion.getFechaMuerte()+" Hora: "+evolucion.getHoraEgreso()+"    Certificado Difunción: "+ValoresPorDefecto.getIntegridadDominio(evolucion.getCertificadoDefuncion());
			texto=cmp.text(tempo).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde).setHorizontalAlignment(HorizontalAlignment.LEFT));
			itemComponent.add(texto);
		}
		else
		{
			if(!UtilidadTexto.isEmpty(evolucion.getDiasIncapacidad())||!UtilidadTexto.isEmpty(evolucion.getObservacionesIncapacidad()))
			{
				itemComponent.newRow();
				tempo="Incapacidad Funcional.       ";
				if(!UtilidadTexto.isEmpty(evolucion.getDiasIncapacidad()))
					tempo=tempo+" No Días: "+evolucion.getDiasIncapacidad();
				if(!UtilidadTexto.isEmpty(evolucion.getObservacionesIncapacidad()))
					tempo=tempo+" Observaciones "+evolucion.getObservacionesIncapacidad();
					
							
				texto=cmp.text(tempo).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde).setHorizontalAlignment(HorizontalAlignment.LEFT));
				itemComponent.add(texto);
			}
		}
		
		return itemComponent;
	}

	/**
	 * 
	 * @param dto
	 * @param seccionFija
	 * @param evolucion
	 * @return
	 */
	private ComponentBuilder createComponentComentariosGenerales(DtoSeccionFija seccionFija,DtoEvolucion evolucion) 
	{
		HorizontalListBuilder itemComponent;
		
		
		TextFieldBuilder<String> texto;
		TextFieldBuilder<String> titulo;
		TextFieldBuilder<String>  medicoReponsable;
		
		titulo=cmp.text(seccionFija.getNombreSeccion()).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeNegrilla).setHorizontalAlignment(HorizontalAlignment.LEFT));
		itemComponent=cmp.horizontalList(titulo);
		
		String tempo="";
		String medicoResponsable="";
		int contadorObservaciones=0;
		for(DtoEvolucionComentarios comentarios:evolucion.getComentarios())
		{
			if(contadorObservaciones==0)
			tempo=tempo+UtilidadImpresion.arreglarCampoStringImpresion(comentarios.getValor())+comentarios.getFecha()+" - "+comentarios.getHora()+"\n"+comentarios.getProfesional().getInformacionGeneralPersonalSalud();
			contadorObservaciones++;
		}
		itemComponent.newRow();
		texto=cmp.text(tempo).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde).setHorizontalAlignment(HorizontalAlignment.LEFT));
		itemComponent.add(texto);
		
	
		
		
		return itemComponent;
	}

	/**
	 * 
	 * @param dto
	 * @param seccionFija
	 * @param evolucion
	 * @return
	 */
	private ComponentBuilder createComponentFijaPlanManejo(DtoSeccionFija seccionFija,DtoEvolucion evolucion) 
	{
		HorizontalListBuilder itemComponent;
		
		
		TextFieldBuilder<String> texto;
		TextFieldBuilder<String> titulo;
		
		titulo=cmp.text(seccionFija.getNombreSeccion()).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeNegrilla).setHorizontalAlignment(HorizontalAlignment.LEFT));
		itemComponent=cmp.horizontalList(titulo);
		itemComponent.newRow();
		texto=cmp.text(UtilidadImpresion.arreglarCampoStringImpresion(evolucion.getPronostico())).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde).setHorizontalAlignment(HorizontalAlignment.LEFT));
		itemComponent.add(texto);
		
		return itemComponent;
	}

	/**
	 * 
	 * @param dto
	 * @param seccionFija
	 * @param evolucion
	 * @return
	 */
	private ComponentBuilder createComponentFijaAnalisis(DtoSeccionFija seccionFija,DtoEvolucion evolucion) 
	{
		HorizontalListBuilder itemComponent;
		
		
		TextFieldBuilder<String> texto;
		TextFieldBuilder<String> titulo;
		
		titulo=cmp.text(seccionFija.getNombreSeccion()).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeNegrilla).setHorizontalAlignment(HorizontalAlignment.LEFT));
		itemComponent=cmp.horizontalList(titulo);
		itemComponent.newRow();
		texto=cmp.text(UtilidadImpresion.arreglarCampoStringImpresion(evolucion.getDescComplicacion())).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde).setHorizontalAlignment(HorizontalAlignment.LEFT));
		itemComponent.add(texto);
		
		return itemComponent;
	}

	/**
	 * 
	 * @param dto
	 * @param seccionFija
	 * @param evolucion
	 * @return
	 */
	private ComponentBuilder createComponentHallazgosImportantes(DtoSeccionFija seccionFija,DtoEvolucion evolucion) 
	{
		HorizontalListBuilder itemComponent;
		
		
		TextFieldBuilder<String> texto;
		TextFieldBuilder<String> titulo;
		
		titulo=cmp.text(seccionFija.getNombreSeccion()).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeNegrilla).setHorizontalAlignment(HorizontalAlignment.LEFT));
		itemComponent=cmp.horizontalList(titulo);
		
		itemComponent.newRow();
		
		texto=cmp.text(UtilidadImpresion.arreglarCampoStringImpresion(evolucion.getHallazgosImportantes())).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde).setHorizontalAlignment(HorizontalAlignment.LEFT));
		itemComponent.add(texto);
		
		return itemComponent;
	}

	/**
	 * 
	 * @param dto
	 * @param seccionFija
	 * @param evolucion
	 * @return
	 */
	private ComponentBuilder createComponentDatosSubjetivos(DtoSeccionFija seccionFija,DtoEvolucion evolucion) 
	{
		HorizontalListBuilder itemComponent;
		
		
		TextFieldBuilder<String> texto;
		TextFieldBuilder<String> titulo;
		
		titulo=cmp.text(seccionFija.getNombreSeccion()).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeNegrilla).setHorizontalAlignment(HorizontalAlignment.LEFT));
		itemComponent=cmp.horizontalList(titulo);
		
		itemComponent.newRow();
		
		texto=cmp.text(UtilidadImpresion.arreglarCampoStringImpresion(evolucion.getInformacionDadaPaciente())).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde).setHorizontalAlignment(HorizontalAlignment.LEFT));
		itemComponent.add(texto);
		
		return itemComponent;
	}

	/**
	 * 
	 * @param dto
	 * @param seccionFija
	 * @param evolucion
	 * @return
	 */
	private ComponentBuilder createComponentFijaDiagnosticos(DtoSeccionFija seccionFija,DtoEvolucion evolucion) 
	{
		HorizontalListBuilder itemComponent;
		
			
		TextFieldBuilder<String> texto;
		TextFieldBuilder<String> titulo;
		
		String tempo="Diagnósticos";
		titulo=cmp.text(tempo).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeNegrilla).setHorizontalAlignment(HorizontalAlignment.LEFT));
		itemComponent=cmp.horizontalList(titulo);
		
		Diagnostico diagnostico=evolucion.getDiagnosticoIngreso();
		if(!diagnostico.getAcronimo().trim().equals(""))
		{
			if(!UtilidadTexto.isEmpty(diagnostico.getAcronimo()) || !UtilidadTexto.isEmpty(diagnostico.getTipoCIE()) 
					|| !UtilidadTexto.isEmpty(diagnostico.getNombre())){
				itemComponent.newRow();
				texto=cmp.text("Dx. Ingreso").setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeNegrilla).setHorizontalAlignment(HorizontalAlignment.LEFT));
				itemComponent.add(texto);
				texto=cmp.text(diagnostico.getAcronimo()+"-"+diagnostico.getTipoCIE()+"  "+diagnostico.getNombre()+"").setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde).setHorizontalAlignment(HorizontalAlignment.LEFT));
				itemComponent.add(texto);
			}

		}
		
		diagnostico=evolucion.getDiagnosticoComplicacion1();
		if(!diagnostico.getAcronimo().trim().equals("")&&!diagnostico.getAcronimo().trim().equals("1"))
		{
			if(!UtilidadTexto.isEmpty(diagnostico.getAcronimo())   || !UtilidadTexto.isEmpty( diagnostico.getTipoCIE())  || !UtilidadTexto.isEmpty(diagnostico.getNombre()) ){
				itemComponent.newRow();
				texto=cmp.text("Dx Complicación").setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeNegrilla).setHorizontalAlignment(HorizontalAlignment.LEFT));
				itemComponent.add(texto);
				texto=cmp.text(diagnostico.getAcronimo()+"-"+diagnostico.getTipoCIE()+"  "+diagnostico.getNombre()+"").setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde).setHorizontalAlignment(HorizontalAlignment.LEFT));
				itemComponent.add(texto);
			}

		}
		
		
		
		boolean ponerTitulo=true;
		int contador=1;
		for(Diagnostico diagnosticos:evolucion.getDiagnosticos())
		{
			if(diagnosticos.isPrincipal())
			{
				diagnostico=diagnosticos;
				if(!diagnostico.getAcronimo().trim().equals(""))
				{
					if(!UtilidadTexto.isEmpty(evolucion.getNombreTipoDiagnosticoPrincipal()) ||
							( !UtilidadTexto.isEmpty(diagnostico.getAcronimo())   || !UtilidadTexto.isEmpty(diagnostico.getTipoCIE())
									||  !UtilidadTexto.isEmpty(diagnostico.getNombre())          )
									||  (!UtilidadTexto.isEmpty(evolucion.getNombreTipoDiagnosticoPrincipal())) ){
						itemComponent.newRow();

						if(!UtilidadTexto.isEmpty(evolucion.getNombreTipoDiagnosticoPrincipal())){
							texto=cmp.text("Dx. Principal").setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeNegrilla).setHorizontalAlignment(HorizontalAlignment.LEFT));
							itemComponent.add(texto);
						}

						if(( !UtilidadTexto.isEmpty(diagnostico.getAcronimo())   || !UtilidadTexto.isEmpty(diagnostico.getTipoCIE())
								||  !UtilidadTexto.isEmpty(diagnostico.getNombre())          )){
							texto=cmp.text(diagnostico.getAcronimo()+"-"+diagnostico.getTipoCIE()+"  "+diagnostico.getNombre()+"").setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde).setHorizontalAlignment(HorizontalAlignment.LEFT));
							itemComponent.add(texto);
						}

						if(!UtilidadTexto.isEmpty(evolucion.getNombreTipoDiagnosticoPrincipal())){
							texto=cmp.text("Tipo de Dx Principal: "+evolucion.getNombreTipoDiagnosticoPrincipal()).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde).setHorizontalAlignment(HorizontalAlignment.LEFT));
							itemComponent.add(texto);
						}
					}
				}
			}
		}
		
		
		
		for(Diagnostico diagnosticoRelacionados:evolucion.getDiagnosticos())
		{
			if(!diagnosticoRelacionados.isPrincipal())
			{
				if(ponerTitulo)
				{
					texto=cmp.text("Dx. Relacionados").setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeNegrilla).setHorizontalAlignment(HorizontalAlignment.LEFT));
					itemComponent.newRow().add(texto);
					ponerTitulo=false;
				}

				if(!UtilidadTexto.isEmpty(diagnosticoRelacionados.getAcronimo())  
						||  !UtilidadTexto.isEmpty(diagnosticoRelacionados.getTipoCIE())  
						|| !UtilidadTexto.isEmpty(diagnosticoRelacionados.getNombre())){
					String temp="Diagnósticos relacionado No. "+contador+":";
					texto=cmp.text(temp+"").setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeNegrilla).setHorizontalAlignment(HorizontalAlignment.LEFT));
					itemComponent.newRow().add(texto);

					//String[] vector = diagRel.split(ConstantesBD.separadorSplit); 
					//temp=vector[0]+"-"+vector[1]+"  "+vector[2];
					temp=diagnosticoRelacionados.getAcronimo()+"-"+diagnosticoRelacionados.getTipoCIE()+"  "+diagnosticoRelacionados.getNombre();
					texto=cmp.text(temp).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde).setHorizontalAlignment(HorizontalAlignment.LEFT));

					itemComponent.add(texto);
				}
				contador++;
			}			
		}
		
		return itemComponent;
	}

	/**
	 * 
	 * @param dto
	 * @param seccionFija
	 * @param evolucion
	 * @return
	 */
	private ComponentBuilder createComponentInformacionGeneral(DtoSeccionFija seccionFija,DtoEvolucion evolucion) 
	{
		HorizontalListBuilder itemComponent;
		
		TextFieldBuilder<String> titulo=cmp.text(seccionFija.getNombreSeccion()).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeNegrilla).setHorizontalAlignment(HorizontalAlignment.LEFT));
		itemComponent=cmp.horizontalList(titulo);
		TextFieldBuilder<String> texto=cmp.text(" Fecha: "+evolucion.getFechaEvolucion()+" Hora: "+evolucion.getHoraEvolucion()).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde).setHorizontalAlignment(HorizontalAlignment.LEFT));
		itemComponent.newRow().add(texto);
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
	private ComponentBuilder createComponentProfesionalQueResponde(DtoEvolucion evolucion) 
	{
		HorizontalListBuilder itemComponent=null;
		String firmaConsultada ="";
		TextFieldBuilder<String> texto1=null;
		TextFieldBuilder<String> texto2=null;
		
		Connection con=UtilidadBD.abrirConexion();
		
		firmaConsultada=Persona.obtenerFirmaDigitalMedico(con, evolucion.getProfesional().getCodigoPersona());
		UtilidadBD.closeConnection(con);
		//String folderFirmas=System.getProperty("ADJUNTOS");
		String path = ValoresPorDefecto.getDirectorioAxiomaBase();
		String directorio = "upload" + System.getProperty("file.separator");
		//folderFirmas=folderFirmas.replace("../","");
		
		String firmaStr = path + directorio +  System.getProperty("file.separator")+System.getProperty("FIRMADIGITAL")+System.getProperty("file.separator")+firmaConsultada;
		LineBuilder linea=cmp.line().setDimension(5, 1);
			
		texto1=cmp.text(evolucion.getProfesional().getNombreyRMPersonalSaludEsp()).setHorizontalAlignment(HorizontalAlignment.LEFT).setStretchWithOverflow(Boolean.TRUE);
		texto2=cmp.text(evolucion.getFechaGrabacion()+" "+evolucion.getHoraGrabacion()).setHorizontalAlignment(HorizontalAlignment.LEFT).setStretchWithOverflow(Boolean.TRUE);
			
		if(!UtilidadTexto.isEmpty2(firmaConsultada) && UtilidadFileUpload.existeArchivoRutaCompelta(firmaStr)){		
			itemComponent=cmp.horizontalList(cmp.verticalList(cmp.image(firmaStr).setDimension(170, 60)
					,linea,
					texto1,
					texto2));
		} else {
			itemComponent=cmp.horizontalList(cmp.verticalList(
					texto1,
					texto2));
		}
		
		itemComponent.add(cmp.text(""));
		itemComponent.add(cmp.text(""));
				
		return cmp.horizontalList(itemComponent.setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeNegrilla)));
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
