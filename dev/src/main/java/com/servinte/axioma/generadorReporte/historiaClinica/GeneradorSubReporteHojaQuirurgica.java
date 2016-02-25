package com.servinte.axioma.generadorReporte.historiaClinica;

import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;
import static net.sf.dynamicreports.report.builder.DynamicReports.margin;
import static net.sf.dynamicreports.report.builder.DynamicReports.report;
import static net.sf.dynamicreports.report.builder.DynamicReports.stl;

import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.builder.MarginBuilder;
import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.builder.component.HorizontalListBuilder;
import net.sf.dynamicreports.report.builder.component.TextFieldBuilder;
import net.sf.dynamicreports.report.builder.component.VerticalListBuilder;
import net.sf.dynamicreports.report.constant.HorizontalAlignment;
import net.sf.dynamicreports.report.constant.SplitType;
import net.sf.dynamicreports.report.constant.StretchType;
import util.ConstantesBD;
import util.UtilidadTexto;
import util.reportes.dinamico.EstilosReportesDinamicosHistoriaClinica;

import com.princetonsa.dto.manejoPaciente.DtoDiagnostico;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.servinte.axioma.dto.salascirugia.CampoNotaRecuperacionDto;
import com.servinte.axioma.dto.salascirugia.EspecialidadDto;
import com.servinte.axioma.dto.salascirugia.InformacionActoQxDto;
import com.servinte.axioma.dto.salascirugia.InformeQxDto;
import com.servinte.axioma.dto.salascirugia.IngresoSalidaPacienteDto;
import com.servinte.axioma.dto.salascirugia.NotaAclaratoriaDto;
import com.servinte.axioma.dto.salascirugia.NotaEnfermeriaDto;
import com.servinte.axioma.dto.salascirugia.NotaRecuperacionDto;
import com.servinte.axioma.dto.salascirugia.ProfesionalHQxDto;
import com.servinte.axioma.dto.salascirugia.ServicioHQxDto;
import com.servinte.axioma.dto.salascirugia.TipoCampoNotaRecuperacionDto;
import com.servinte.axioma.generadorReporteHistoriaClinica.comun.IConstantesReporteHistoriaClinica;

public class GeneradorSubReporteHojaQuirurgica {


	/**
	 * Genera la lista con cada seccion del reporte de la hoja quirurgica
	 * @param dtoHojaQuirurgicaAnestesia
	 * @param usuario
	 * @param paciente
	 * @param solicitud
	 * @param reportFormatoHc 
	 * @return List<JasperReportBuilder>  con partes de la Hoja Quirurgica
	 */ 
	public   List<JasperReportBuilder>  reporteHojaQuirurgica(DtoHojaQuirurgicaAnestesia  dtoHojaQuirurgicaAnestesia
			,UsuarioBasico usuario, PersonaBasica paciente,Integer solicitud, JasperReportBuilder reportFormatoHc){

		//Lista de componentes por cada una generada 
		List<ComponentBuilder> listaComponentes = new ArrayList<ComponentBuilder>();

		//Lista de reportes generados
		List<JasperReportBuilder> listaReportes = new ArrayList<JasperReportBuilder>();

		//Componente temporal que contiene cada seccion generada para ser validad 
		ComponentBuilder tmp = null;

		//flags para saber si se pintan titulos dependiendo de si hay o no informacion
		Boolean flagHayInformacionIncial=false;
		HorizontalListBuilder itemComponent=null;

		//Se genera la seccion de Informacion general de la Hoja Qx
		//tmp = createComponentInformacionGeneralUrgencias(dtoHojaQuirurgicaAnestesia.getMapaHojaQuirur(),solicitud);
		ResourceBundle etiquetas=ResourceBundle.getBundle("com.servinte.mensajes.salasCirugia.HojaQuirurgicaForm");
		TextFieldBuilder<String> texto=cmp.text(etiquetas.getString("reporteHC.titulo")).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeNegrilla).setBackgroundColor(Color.LIGHT_GRAY).setHorizontalAlignment(HorizontalAlignment.LEFT));

		HorizontalListBuilder matriz=cmp.horizontalFlowList(texto);
		
		tmp= createComponentIngresoSalidaPaciente(dtoHojaQuirurgicaAnestesia.getMapaIngresoSalidaPaciente().get(solicitud),matriz);
		if(tmp!=null){
			listaComponentes.add(tmp);
			matriz=cmp.horizontalFlowList();
		}else{
			matriz=cmp.horizontalFlowList(texto);
		}
		
		tmp= createComponentInformacionActoQx(dtoHojaQuirurgicaAnestesia.getMapaInformacionActoQuirurgico().get(solicitud), matriz);
		listaComponentes.add(tmp);

		matriz=cmp.horizontalFlowList();
		List<EspecialidadDto>especialidades=dtoHojaQuirurgicaAnestesia.getMapaEspecialidadesXSolicitud().get(solicitud);
		
		List<ProfesionalHQxDto>otrosProfesionales=new ArrayList<ProfesionalHQxDto>(0); 
		
		if(especialidades!=null&&!especialidades.isEmpty()){
			for (EspecialidadDto especialidadDto : especialidades) {
				matriz=cmp.horizontalFlowList();
				List<ComponentBuilder> tmpList = createComponentInformeQx(especialidadDto,dtoHojaQuirurgicaAnestesia.getMapaDescripcionesOperatorias().get(solicitud).get(especialidadDto.getCodigo()),
						dtoHojaQuirurgicaAnestesia.getMapaNotasAclaratorias().get(solicitud).get(especialidadDto.getCodigo())
						, matriz);
				if(otrosProfesionales.isEmpty()){
					otrosProfesionales=dtoHojaQuirurgicaAnestesia.getMapaDescripcionesOperatorias().get(solicitud).get(especialidadDto.getCodigo()).getOtrosProfesionales();
				}
				listaComponentes.addAll(tmpList);
			}
			matriz=cmp.horizontalFlowList();
		}
		if(!otrosProfesionales.isEmpty()){	
			texto=cmp.text(etiquetas.getString("msgHojaQuirurgicaForm.informeQx.otrosProfesionales")).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeNegrilla).setBackgroundColor(Color.LIGHT_GRAY).setHorizontalAlignment(HorizontalAlignment.LEFT));
			matriz.add(texto);
			
			matriz.newRow();
			boolean esOtrosProfesionales=true;
			createComponentProfesionalHQxDto(otrosProfesionales, matriz,esOtrosProfesionales);
		}
		
		List<NotaEnfermeriaDto>notasEnfermeriaDtos=dtoHojaQuirurgicaAnestesia.getMapaNotasEnfermeria().get(solicitud);
		if(notasEnfermeriaDtos!=null&&!notasEnfermeriaDtos.isEmpty()){
			matriz=cmp.horizontalFlowList();
			List<ComponentBuilder>tmpList=createComponentNotasEnfermeria(notasEnfermeriaDtos, matriz);
			listaComponentes.addAll(tmpList);
		}
		
		List<NotaRecuperacionDto>notasRecuperacionDtos=dtoHojaQuirurgicaAnestesia.getMapaNotasRecuperacionCirugia().get(solicitud);
		if(notasRecuperacionDtos!=null&&!notasRecuperacionDtos.isEmpty()){
			matriz=cmp.horizontalFlowList();
			List<ComponentBuilder>tmpList=createComponentNotasRecuperacion(notasRecuperacionDtos, matriz);
			listaComponentes.addAll(tmpList);
		}
		//Si el compomnente es null no se agrega a la lista 
		/*if(tmp!=null){
			listaComponentes.add(tmp);

			//Si hay información se coloca el flag del titulo en true osea que viene desde el componenete el titulo
			flagHayInformacionIncial=true;
		}


		Integer numReg = Integer.valueOf(String.valueOf(dtoHojaQuirurgicaAnestesia.getMapaHojaQuirur().get("numRegCir")));
		TextFieldBuilder<String>  contenido=null;
		TextFieldBuilder<String> titulo2=null;
		HorizontalListBuilder itemComponentGrande=null;

		if(numReg>0){
			contenido=cmp.text(" ").setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloSubTituloSinBold).setHorizontalAlignment(HorizontalAlignment.LEFT));
			titulo2=cmp.text(IConstantesReporteHistoriaClinica.constanteServicios).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeBRNegrilla).setHorizontalAlignment(HorizontalAlignment.CENTER));
			itemComponentGrande=cmp.horizontalList(contenido);
			itemComponentGrande.newRow().add(titulo2).setStyle(EstilosReportesDinamicosHistoriaClinica.estiloBorde);
		}

		for (int i = 0; i <numReg; i++) {

			if(i==0){
				//Se genera la seccion de Servicios 
				tmp =createComponentServicios(dtoHojaQuirurgicaAnestesia.getMapaHojaQuirur(), solicitud,i,itemComponentGrande);
			}else{
				itemComponentGrande=cmp.horizontalList(contenido);
				tmp =createComponentServicios(dtoHojaQuirurgicaAnestesia.getMapaHojaQuirur(), solicitud,i,itemComponentGrande);
			}

			//Si el compomnente es null no se agrega a la lista 
			if(tmp!=null){

				//Se adiciona un componente a lista 
				listaComponentes.add(tmp);
			}
		}



		//Se geenra la seccion de acto quirurgico 
		tmp =createComponentActoQuirurgico(dtoHojaQuirurgicaAnestesia.getMapaHojaQuirur(), solicitud);

		//Si el compomnente es null no se agrega a la lista 
		if(tmp!=null){
			//Se adiciona un componente a lista 
			listaComponentes.add(tmp);
		}

		
		Integer tamanoMapa=Integer.valueOf(String.valueOf(dtoHojaQuirurgicaAnestesia.getMapaHojaQuirur().get("numRegOb")));
		HorizontalListBuilder itemComponentGrande2=null;

		for (int d = 0;d <tamanoMapa; d++) {

			if(d==0){
				//Se genera la seccion de onservaciones generales y de patologia 
				tmp=createComponentPatologiaObservacionesGenerales(dtoHojaQuirurgicaAnestesia.getMapaHojaQuirur(),solicitud,d,itemComponentGrande2);
			}else{
				itemComponentGrande=cmp.horizontalList(contenido);
				tmp=createComponentPatologiaObservacionesGenerales(dtoHojaQuirurgicaAnestesia.getMapaHojaQuirur(),solicitud,d,itemComponentGrande2);
			}
			//Si el compomnente es null no se agrega a la lista 
			if(tmp!=null){
				//Se adiciona un componente a lista 
				listaComponentes.add(tmp);
			}
		}

		//Se genera la seccion de Notas Aclaratorias
		tmp=createComponentNotasAclaratorias(dtoHojaQuirurgicaAnestesia.getMapaHojaQuirur(), solicitud);

		//Si el compomnente es null no se agrega a la lista 
		if(tmp!=null){
			//Se adiciona un componente a lista 
			listaComponentes.add(tmp);
		}

		//Si la lista contiene informacion  PERO EN LA INFORMACION GENERAL NO SE PINTO NADA ENTONCES SE COLOCA EL TITULO 
		if(listaComponentes.size()>0 && !flagHayInformacionIncial){

			//Se adiciona el titulo de la seccion 
			TextFieldBuilder<String> titulo=cmp.text(IConstantesReporteHistoriaClinica.constanteTituloHojaQuirurgica).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeNegrilla).setBackgroundColor(Color.LIGHT_GRAY).setHorizontalAlignment(HorizontalAlignment.LEFT));
			TextFieldBuilder<String> vacio=cmp.text("").setStyle(stl.style().setHorizontalAlignment(HorizontalAlignment.LEFT));
			itemComponent=cmp.horizontalList(titulo);
		}*/


		//la lista de secciones se recorre y se crea un reporte y se adiciona a la lista 
		for (int i = 0; i < listaComponentes.size(); i++) {
			if(listaComponentes.get(i)!=null){

				//Se crea un nuevo reporte
				JasperReportBuilder hojaQuirurgica2 = report();

				//Se adiciona al summary el componente y se adiciona el splittype y las margenes 
				hojaQuirurgica2
				.setPageMargin(crearMagenesSubReporte())
				.summary((HorizontalListBuilder) listaComponentes.get(i))
				.setSummarySplitType(SplitType.PREVENT) 
				.build();

				reportFormatoHc.summary(cmp.subreport(hojaQuirurgica2));

				//Se adiciona a la lista a retornar 
				listaReportes.add(hojaQuirurgica2);
			}
		}

		if(!listaReportes.isEmpty()){
			//seccion de separación
			reportFormatoHc.summary(cmp.text("").setHeight(5));
		}
		
		//Lista con reportes de cada seccion 
		return listaReportes;
	}

	/**
	 * Crea el componente del reporte que muestra la seccion de Ingreso-Salida paciente de la Hoja Qx
	 * 
	 * @param ingresoSalidaPacienteDto
	 * @param matriz
	 * @return
	 * @author jeilones
	 * @created 25/07/2013
	 */
	private ComponentBuilder<?,?> createComponentIngresoSalidaPaciente(IngresoSalidaPacienteDto ingresoSalidaPacienteDto,HorizontalListBuilder matriz) 
	{
		if(ingresoSalidaPacienteDto==null){
			return matriz;
		}
		
		ResourceBundle etiquetas=ResourceBundle.getBundle("com.servinte.mensajes.salasCirugia.HojaQuirurgicaForm");

		matriz.newRow();
		
		TextFieldBuilder<String> tituloIngresoSalidaPaciente=cmp.text(etiquetas.getString("reporteHC.ingresoSalidaPaciente.subtitulo")).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeNegrilla).setHorizontalAlignment(HorizontalAlignment.LEFT));
		matriz.add(tituloIngresoSalidaPaciente);
		
		matriz.newRow();
		
		boolean hayNuevaFila=false;
		int contador=0;
		
		TextFieldBuilder<String>texto=cmp.text(etiquetas.getString("msgHojaQuirurgicaForm.ingresoSalida.tipoSala.label")).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeBRNegrilla).setHorizontalAlignment(HorizontalAlignment.LEFT));
		TextFieldBuilder<String>  contenido=null;
		HorizontalListBuilder componenteLabel = null;
		if(ingresoSalidaPacienteDto.getTipoSala()!=null&&ingresoSalidaPacienteDto.getTipoSala().getCodigoTipoSala()>0){
			contenido=cmp.text(ingresoSalidaPacienteDto.getTipoSala().getDescripcionTipoSala()).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloSubTituloSinBold).setHorizontalAlignment(HorizontalAlignment.LEFT));
			
			componenteLabel=cmp.horizontalList(texto);
			componenteLabel.add(contenido).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde));
			matriz.add(componenteLabel);
			
			hayNuevaFila=true;
		}
		
		
		
		SimpleDateFormat simpleDateFormat=new SimpleDateFormat(ConstantesBD.formatoFechaApp);
		StringBuffer fecha=null;
		
		texto=cmp.text(etiquetas.getString("reporteHC.ingresoSalidaPaciente.fechaIngreso")).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeBRNegrilla).setHorizontalAlignment(HorizontalAlignment.LEFT));
		if(ingresoSalidaPacienteDto.getFechaIngresoSala()!=null){
			fecha=new StringBuffer(simpleDateFormat.format(ingresoSalidaPacienteDto.getFechaIngresoSala()));
			if(ingresoSalidaPacienteDto.getHoraIngresoSala()!=null){
				fecha.append(" ").append(ingresoSalidaPacienteDto.getHoraIngresoSala());
			}
			contenido=cmp.text(fecha.toString()).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloSubTituloSinBold).setHorizontalAlignment(HorizontalAlignment.LEFT));
		}else{
			fecha=new StringBuffer();
			if(ingresoSalidaPacienteDto.getHoraIngresoSala()!=null){
				fecha.append(ingresoSalidaPacienteDto.getHoraIngresoSala());
				contenido=cmp.text(fecha.toString()).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloSubTituloSinBold).setHorizontalAlignment(HorizontalAlignment.LEFT));
			}else{
				contenido=null;
			}
		}
		
		if(contenido!=null){
			componenteLabel=cmp.horizontalList(texto);
			componenteLabel.add(contenido).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde));
			matriz.add(componenteLabel);
			
			hayNuevaFila=true;
		}
		
		texto=cmp.text(etiquetas.getString("reporteHC.ingresoSalidaPaciente.fechaSalida")).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeBRNegrilla).setHorizontalAlignment(HorizontalAlignment.LEFT));
		if(ingresoSalidaPacienteDto.getFechaSalidaSala()!=null){
			fecha=new StringBuffer(simpleDateFormat.format(ingresoSalidaPacienteDto.getFechaSalidaSala()));
			if(ingresoSalidaPacienteDto.getHoraSalidaSala()!=null){
				fecha.append(" ").append(ingresoSalidaPacienteDto.getHoraSalidaSala());
			}
			contenido=cmp.text(fecha.toString()).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloSubTituloSinBold).setHorizontalAlignment(HorizontalAlignment.LEFT));
		}else{
			fecha=new StringBuffer();
			if(ingresoSalidaPacienteDto.getHoraSalidaSala()!=null){
				fecha.append(ingresoSalidaPacienteDto.getHoraSalidaSala());
				contenido=cmp.text(fecha.toString()).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloSubTituloSinBold).setHorizontalAlignment(HorizontalAlignment.LEFT));
			}else{
				contenido=null;
			}
		}
		
		if(contenido!=null){
			componenteLabel=cmp.horizontalList(texto);
			componenteLabel.add(contenido).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde));
			matriz.add(componenteLabel);
			
			hayNuevaFila=true;
		}
		
		texto=cmp.text(etiquetas.getString("msgHojaQuirurgicaForm.ingresoSalida.duracionFinalCx.label")).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeBRNegrilla).setHorizontalAlignment(HorizontalAlignment.LEFT));
		if(ingresoSalidaPacienteDto.getDuracionFinalCirugia()!=null){
			contenido=cmp.text(ingresoSalidaPacienteDto.getDuracionFinalCirugia()).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloSubTituloSinBold).setHorizontalAlignment(HorizontalAlignment.LEFT));
			
			componenteLabel=cmp.horizontalList(texto);
			componenteLabel.add(contenido).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde));
			matriz.add(componenteLabel);
			
			hayNuevaFila=true;
		}

		if(hayNuevaFila){
			matriz.newRow();
			contador++;
		}
		
		hayNuevaFila=false;
		
		texto=cmp.text(etiquetas.getString("msgHojaQuirurgicaForm.ingresoSalida.sala.label")).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeBRNegrilla).setHorizontalAlignment(HorizontalAlignment.LEFT));
		if(ingresoSalidaPacienteDto.getSalaCirugia()!=null&&ingresoSalidaPacienteDto.getSalaCirugia().getCodigoSala()>0){
			contenido=cmp.text(ingresoSalidaPacienteDto.getSalaCirugia().getDescripcionSala()).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloSubTituloSinBold).setHorizontalAlignment(HorizontalAlignment.LEFT));
			
			componenteLabel=cmp.horizontalList(texto);
			componenteLabel.add(contenido).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde));
			matriz.add(componenteLabel);
			
			hayNuevaFila=true;
		}
		
		texto=cmp.text(etiquetas.getString("msgHojaQuirurgicaForm.ingresoSalida.fechaInicioAnestesia.titulo")).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeBRNegrilla).setHorizontalAlignment(HorizontalAlignment.LEFT));
		if(ingresoSalidaPacienteDto.getFechaInicioAnestesiaSala()!=null){
			fecha=new StringBuffer(simpleDateFormat.format(ingresoSalidaPacienteDto.getFechaInicioAnestesiaSala()));
			if(ingresoSalidaPacienteDto.getHoraInicioAnestesiaSala()!=null){
				fecha.append(" ").append(ingresoSalidaPacienteDto.getHoraInicioAnestesiaSala());
			}
			contenido=cmp.text(fecha.toString()).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloSubTituloSinBold).setHorizontalAlignment(HorizontalAlignment.LEFT));
		}else{
			fecha=new StringBuffer();
			if(ingresoSalidaPacienteDto.getHoraInicioAnestesiaSala()!=null){
				fecha.append(ingresoSalidaPacienteDto.getHoraInicioAnestesiaSala());
				contenido=cmp.text(fecha.toString()).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloSubTituloSinBold).setHorizontalAlignment(HorizontalAlignment.LEFT));
			}else{
				contenido=null;
			}
			
		}
		
		if(contenido!=null){
			componenteLabel=cmp.horizontalList(texto);
			componenteLabel.add(contenido).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde));
			matriz.add(componenteLabel);
			
			hayNuevaFila=true;
		}
		
		texto=cmp.text(etiquetas.getString("msgHojaQuirurgicaForm.ingresoSalida.fechafinAnestesia.titulo")).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeBRNegrilla).setHorizontalAlignment(HorizontalAlignment.LEFT));
		if(ingresoSalidaPacienteDto.getFechaFinAnestesiaSala()!=null){
			fecha=new StringBuffer(simpleDateFormat.format(ingresoSalidaPacienteDto.getFechaFinAnestesiaSala()));
			if(ingresoSalidaPacienteDto.getHoraFinAnestesiaSala()!=null){
				fecha.append(" ").append(ingresoSalidaPacienteDto.getHoraFinAnestesiaSala());
			}
			contenido=cmp.text(fecha.toString()).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloSubTituloSinBold).setHorizontalAlignment(HorizontalAlignment.LEFT));
		}else{
			fecha=new StringBuffer();
			if(ingresoSalidaPacienteDto.getHoraFinAnestesiaSala()!=null){
				fecha.append(ingresoSalidaPacienteDto.getHoraFinAnestesiaSala());
				contenido=cmp.text(fecha.toString()).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloSubTituloSinBold).setHorizontalAlignment(HorizontalAlignment.LEFT));
			}else{
				contenido=null;
			}
		}
		
		if(contenido!=null){
			componenteLabel=cmp.horizontalList(texto);
			componenteLabel.add(contenido).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde));
			matriz.add(componenteLabel);
			
			hayNuevaFila=true;
		}
		
		texto=cmp.text(etiquetas.getString("msgHojaQuirurgicaForm.ingresoSalida.fechaInicioActoQuirurgico.titulo")).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeBRNegrilla).setHorizontalAlignment(HorizontalAlignment.LEFT));
		if(ingresoSalidaPacienteDto.getFechaInicioActoQxSala()!=null){
			fecha=new StringBuffer(simpleDateFormat.format(ingresoSalidaPacienteDto.getFechaInicioActoQxSala()));
			if(ingresoSalidaPacienteDto.getHoraInicioActoQxSala()!=null){
				fecha.append(" ").append(ingresoSalidaPacienteDto.getHoraInicioActoQxSala());
			}
			contenido=cmp.text(fecha.toString()).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloSubTituloSinBold).setHorizontalAlignment(HorizontalAlignment.LEFT));
		}else{
			fecha=new StringBuffer();
			if(ingresoSalidaPacienteDto.getHoraInicioActoQxSala()!=null){
				fecha.append(ingresoSalidaPacienteDto.getHoraInicioActoQxSala());
				contenido=cmp.text(fecha.toString()).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloSubTituloSinBold).setHorizontalAlignment(HorizontalAlignment.LEFT));
			}else{
				contenido=null;
			}
		}
		
		if(contenido!=null){
			componenteLabel=cmp.horizontalList(texto);
			componenteLabel.add(contenido).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde));
			matriz.add(componenteLabel);
			
			hayNuevaFila=true;
		}
		
		texto=cmp.text(etiquetas.getString("msgHojaQuirurgicaForm.ingresoSalida.fechaFinActoQuirurgico.titulo")).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeBRNegrilla).setHorizontalAlignment(HorizontalAlignment.LEFT));
		if(ingresoSalidaPacienteDto.getFechaFinActoQxSala()!=null){
			fecha=new StringBuffer(simpleDateFormat.format(ingresoSalidaPacienteDto.getFechaFinActoQxSala()));
			if(ingresoSalidaPacienteDto.getHoraFinActoQxSala()!=null){
				fecha.append(" ").append(ingresoSalidaPacienteDto.getHoraFinActoQxSala());
			}
			contenido=cmp.text(fecha.toString()).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloSubTituloSinBold).setHorizontalAlignment(HorizontalAlignment.LEFT));
		}else{
			fecha=new StringBuffer();
			if(ingresoSalidaPacienteDto.getHoraFinActoQxSala()!=null){
				fecha.append(ingresoSalidaPacienteDto.getHoraFinActoQxSala());
				contenido=cmp.text(fecha.toString()).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloSubTituloSinBold).setHorizontalAlignment(HorizontalAlignment.LEFT));
			}else{
				contenido=null;
			}
		}
		
		if(contenido!=null){
			componenteLabel=cmp.horizontalList(texto);
			componenteLabel.add(contenido).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde));
			matriz.add(componenteLabel);
			
			hayNuevaFila=true;
		}
		
		if(hayNuevaFila){
			matriz.newRow();
			contador++;
		}
		
		texto=cmp.text(etiquetas.getString("msgHojaQuirurgicaForm.ingresoSalida.destinoPaciente.label")).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeBRNegrilla).setHorizontalAlignment(HorizontalAlignment.LEFT));
		if(ingresoSalidaPacienteDto.getDestinoPaciente()!=null&&ingresoSalidaPacienteDto.getDestinoPaciente().getCodigoDestino()>0){
			contenido=cmp.text(ingresoSalidaPacienteDto.getDestinoPaciente().getDescripcionDestino()).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloSubTituloSinBold).setHorizontalAlignment(HorizontalAlignment.LEFT));
			
			componenteLabel=cmp.horizontalList(texto.setWidth(16));
			componenteLabel.add(contenido).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde));
			matriz.add(componenteLabel);
			
			contador++;
		}
		
		if(contador>0){
			return matriz;
		}else{
			return null;
		}
	}
	
	/**
	 * Crea el componente del reporte que muestra la seccion de informacion del Acto Qx
	 * 
	 * @param informacionActoQxDto
	 * @param matriz
	 * @return
	 * @author jeilones
	 * @created 25/07/2013
	 */
	private ComponentBuilder<?,?> createComponentInformacionActoQx(InformacionActoQxDto informacionActoQxDto,HorizontalListBuilder matriz) 
	{
		if(informacionActoQxDto==null){
			return matriz;
		}
		
		ResourceBundle etiquetas=ResourceBundle.getBundle("com.servinte.mensajes.salasCirugia.HojaQuirurgicaForm");

		matriz.newRow();

		TextFieldBuilder<String> tituloInformacionActoQx=cmp.text(etiquetas.getString("reporteHC.informacionActoQx.subtitulo")).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeNegrilla).setBackgroundColor(Color.LIGHT_GRAY).setHorizontalAlignment(HorizontalAlignment.LEFT));
		matriz.add(tituloInformacionActoQx);
		
		matriz.newRow();

		TextFieldBuilder<String>texto=null;
		TextFieldBuilder<String>  contenido=null;
		HorizontalListBuilder componenteLabel=null;
		if(informacionActoQxDto.getDiagnosticoPrincipal()!=null&&informacionActoQxDto.getDiagnosticoPrincipal().getCodigoDxPreoperatorio()>0
				||!informacionActoQxDto.getDiagnosticosRelacionados().isEmpty()){
			tituloInformacionActoQx=cmp.text(etiquetas.getString("msgHojaQuirurgicaForm.informacionActoQx.dxPreoperatorio.label")).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeNegrilla).setHorizontalAlignment(HorizontalAlignment.LEFT));
			matriz.add(tituloInformacionActoQx);
			
			matriz.newRow();
			
			texto=cmp.text(etiquetas.getString("msgHojaQuirurgicaForm.informacionActoQx.dxPrincipal.label")).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeBRNegrilla).setHorizontalAlignment(HorizontalAlignment.LEFT));
			if(informacionActoQxDto.getDiagnosticoPrincipal()!=null&&informacionActoQxDto.getDiagnosticoPrincipal().getCodigoDxPreoperatorio()>0){
				contenido=cmp.text(informacionActoQxDto.getDiagnosticoPrincipal().getNombreCompletoDiagnostico()).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloSubTituloSinBold).setHorizontalAlignment(HorizontalAlignment.LEFT));
				
				componenteLabel=cmp.horizontalList(texto.setWidth(16));
				componenteLabel.add(contenido).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde));
				matriz.add(componenteLabel);
				
				matriz.newRow();
			}
			
			if(!informacionActoQxDto.getDiagnosticosRelacionados().isEmpty()){
				texto=cmp.text(etiquetas.getString("msgHojaQuirurgicaForm.informacionActoQx.dxRelacionados.label")).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloNegrillaL).setHorizontalAlignment(HorizontalAlignment.LEFT));
			
			
				VerticalListBuilder componenteVerticalLabel=cmp.verticalList(texto);
				TextFieldBuilder<String> vacio=cmp.text("").setStyle(stl.style().setHorizontalAlignment(HorizontalAlignment.LEFT));
				
				
				for (DtoDiagnostico diagnostico : informacionActoQxDto.getDiagnosticosRelacionados()) {
					contenido=cmp.text(diagnostico.getNombreCompletoDiagnostico()).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloSubTituloSinBold).setHorizontalAlignment(HorizontalAlignment.LEFT));
					
					componenteLabel=cmp.horizontalList(vacio.setWidth(5));
					componenteLabel.add(contenido);
					
					componenteVerticalLabel.add(componenteLabel);
				}
		
				componenteLabel=cmp.horizontalList(componenteVerticalLabel).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde));
				matriz.add(componenteLabel);
				
				matriz.newRow();
			}
		}
		
		texto=cmp.text(etiquetas.getString("msgHojaQuirurgicaForm.informacionActoQx.politrauma")).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeBRNegrilla).setHorizontalAlignment(HorizontalAlignment.LEFT));
		if(UtilidadTexto.getBoolean(informacionActoQxDto.getPolitrauma())){
			contenido=cmp.text("Si").setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloSubTituloSinBold).setHorizontalAlignment(HorizontalAlignment.LEFT));
		}else{
			contenido=cmp.text("No").setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloSubTituloSinBold).setHorizontalAlignment(HorizontalAlignment.LEFT));
		}
		
		componenteLabel=cmp.horizontalList(texto.setWidth(16));
		componenteLabel.add(contenido).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde));
		matriz.add(componenteLabel);
		
		matriz.newRow();
		
		texto=cmp.text(etiquetas.getString("msgHojaQuirurgicaForm.informacionActoQx.datosAnestesiologo")).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeBRNegrilla).setHorizontalAlignment(HorizontalAlignment.LEFT));
		
		componenteLabel=cmp.horizontalList(texto);
		componenteLabel.setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde));
		
		matriz.add(componenteLabel);
		
		matriz.newRow();
		
		texto=cmp.text(etiquetas.getString("msgHojaQuirurgicaForm.informacionActoQx.participaAnestesiologo")).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeBRNegrilla).setHorizontalAlignment(HorizontalAlignment.LEFT));
		if(UtilidadTexto.getBoolean(informacionActoQxDto.getParticipaAnestesiologo())){
			contenido=cmp.text("Si").setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloSubTituloSinBold).setHorizontalAlignment(HorizontalAlignment.LEFT));
		}else{
			contenido=cmp.text("No").setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloSubTituloSinBold).setHorizontalAlignment(HorizontalAlignment.LEFT));
		}
		
		componenteLabel=cmp.horizontalList(texto);
		componenteLabel.add(contenido).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde));
		matriz.add(componenteLabel);
		
		texto=cmp.text(etiquetas.getString("msgHojaQuirurgicaForm.informacionActoQx.tipoAnestesia")).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeBRNegrilla).setHorizontalAlignment(HorizontalAlignment.LEFT));
		if(informacionActoQxDto.getTipoAnestesia()!=null&&informacionActoQxDto.getTipoAnestesia().getCodigo()>0){
			contenido=cmp.text(informacionActoQxDto.getTipoAnestesia().getDescripcion()).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloSubTituloSinBold).setHorizontalAlignment(HorizontalAlignment.LEFT));
			
			componenteLabel=cmp.horizontalList(texto);
			componenteLabel.add(contenido).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde));
			matriz.add(componenteLabel);
		}
		
		matriz.newRow();
		
		texto=cmp.text(etiquetas.getString("msgHojaQuirurgicaForm.informacionActoQx.anestesiologo")).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeBRNegrilla).setHorizontalAlignment(HorizontalAlignment.LEFT));
		if(informacionActoQxDto.getAnestesiologo()!=null&&informacionActoQxDto.getAnestesiologo().getIdMedico()>0){
			StringBuffer nombreAnestesiologo=new StringBuffer();
			if(informacionActoQxDto.getAnestesiologo().getPrimerNombre()!=null){
				nombreAnestesiologo.append(informacionActoQxDto.getAnestesiologo().getPrimerNombre());
				nombreAnestesiologo.append(" ");
			}
			if(informacionActoQxDto.getAnestesiologo().getSegundoNombre()!=null){
				nombreAnestesiologo.append(informacionActoQxDto.getAnestesiologo().getSegundoNombre());
				nombreAnestesiologo.append(" ");
			}
			if(informacionActoQxDto.getAnestesiologo().getPrimerApellido()!=null){
				nombreAnestesiologo.append(informacionActoQxDto.getAnestesiologo().getPrimerApellido());
				nombreAnestesiologo.append(" ");
			}
			if(informacionActoQxDto.getAnestesiologo().getSegundoApellido()!=null){
				nombreAnestesiologo.append(informacionActoQxDto.getAnestesiologo().getSegundoApellido());
			}
			
				
			contenido=cmp.text(nombreAnestesiologo.toString()).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloSubTituloSinBold).setHorizontalAlignment(HorizontalAlignment.LEFT));
			
			componenteLabel=cmp.horizontalList(texto.setWidth(16));
			componenteLabel.add(contenido).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde));
			matriz.add(componenteLabel);
		}
		
		return matriz;
	}
	
	/**
	 * Crea el componente del reporte que muestra la seccion de la descripcion Qx
	 * 
	 * @param especialidadDto
	 * @param informeQxDto
	 * @param notasAclaratorias
	 * @param matriz
	 * @return
	 * @author jeilones
	 * @created 26/07/2013
	 */
	private List<ComponentBuilder> createComponentInformeQx(EspecialidadDto especialidadDto, InformeQxDto informeQxDto,List<NotaAclaratoriaDto> notasAclaratorias, HorizontalListBuilder matriz) 
	{
		List<ComponentBuilder> listaComponentes=new ArrayList<ComponentBuilder>(0);
		if(informeQxDto==null){
			return listaComponentes;
		}
		
		ResourceBundle etiquetas=ResourceBundle.getBundle("com.servinte.mensajes.salasCirugia.HojaQuirurgicaForm");

		matriz.newRow();

		TextFieldBuilder<String> tituloInformacionActoQx=cmp.text(etiquetas.getString("reporteHC.informeQx.subtitulo")).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeNegrilla).setBackgroundColor(Color.LIGHT_GRAY).setHorizontalAlignment(HorizontalAlignment.LEFT));
		matriz.add(tituloInformacionActoQx);
		
		matriz.newRow();
		
		TextFieldBuilder<String>texto=cmp.text(etiquetas.getString("msgHojaQuirurgicaForm.informeQx.especialidadInterviene")).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeBRNegrilla).setHorizontalAlignment(HorizontalAlignment.LEFT));
		TextFieldBuilder<String>  contenido=null;
		HorizontalListBuilder componenteLabel=null;
		if(especialidadDto!=null&&especialidadDto.getCodigo()>0){
			contenido=cmp.text(especialidadDto.getNombre()).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloSubTituloSinBold).setHorizontalAlignment(HorizontalAlignment.LEFT));
			
			componenteLabel=cmp.horizontalList(texto.setWidth(16));
			componenteLabel.add(contenido).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde));
			matriz.add(componenteLabel);
			
			matriz.newRow();
		}
		
		listaComponentes.add(matriz);
		matriz=cmp.horizontalFlowList();
		
		
		boolean hayNuevaFila=false;
		
		if(!informeQxDto.getInformeQxEspecialidad().getServicios().isEmpty()){
			
			hayNuevaFila=true;
			
			List<ComponentBuilder>tmpServicios=createComponentServiciosQx(informeQxDto.getInformeQxEspecialidad().getServicios(), informeQxDto.getInformeQxEspecialidad().getProfesionales(), matriz);
			listaComponentes.addAll(tmpServicios);
			matriz=cmp.horizontalFlowList();
		}
		
		texto=cmp.text(etiquetas.getString("msgHojaQuirurgicaForm.informeQx.descripcionOperatoria")).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeBRNegrilla).setHorizontalAlignment(HorizontalAlignment.LEFT));
		if(informeQxDto.getInformeQxEspecialidad().getUltimaDescripcionOperatoria()!=null&&
				!UtilidadTexto.isEmpty(informeQxDto.getInformeQxEspecialidad().getUltimaDescripcionOperatoria().getDescripcion())){
			
			contenido=cmp.text(informeQxDto.getInformeQxEspecialidad().getUltimaDescripcionOperatoria().getDescripcion()).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloSubTituloSinBold).setHorizontalAlignment(HorizontalAlignment.LEFT));
			
			componenteLabel=cmp.horizontalList(texto.setWidth(16)).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde));
			
			matriz.add(componenteLabel);
			
			matriz.newRow();
			
			componenteLabel=cmp.horizontalList(contenido).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde));
			
			matriz.add(componenteLabel);
			
			matriz.newRow();
		}
		
		if((informeQxDto.getInformeQxEspecialidad().getDiagnosticoPostOperatorioPrincipal()!=null
				&&informeQxDto.getInformeQxEspecialidad().getDiagnosticoPostOperatorioPrincipal().getCodigoDxPostOperatorio()>0)
				||!informeQxDto.getInformeQxEspecialidad().getDiagnosticosPostOperatorioRelacionados().isEmpty()
				||(informeQxDto.getInformeQxEspecialidad().getDiagnosticoPostOperatorioComplicacion()!=null
						&&informeQxDto.getInformeQxEspecialidad().getDiagnosticoPostOperatorioComplicacion().getCodigoDxPostOperatorio()>0)){
			tituloInformacionActoQx=cmp.text(etiquetas.getString("msgHojaQuirurgicaForm.informeQx.dxPostoperatorio")).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeNegrilla).setHorizontalAlignment(HorizontalAlignment.LEFT));
			matriz.add(tituloInformacionActoQx);
			
			matriz.newRow();
			
			texto=cmp.text(etiquetas.getString("msgHojaQuirurgicaForm.informeQx.dxPrincipal")).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeBRNegrilla).setHorizontalAlignment(HorizontalAlignment.LEFT));
			if(informeQxDto.getInformeQxEspecialidad().getDiagnosticoPostOperatorioPrincipal()!=null
					&&informeQxDto.getInformeQxEspecialidad().getDiagnosticoPostOperatorioPrincipal().getCodigoDxPostOperatorio()>0){
				contenido=cmp.text(informeQxDto.getInformeQxEspecialidad().getDiagnosticoPostOperatorioPrincipal().getNombreCompletoDiagnostico()).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloSubTituloSinBold).setHorizontalAlignment(HorizontalAlignment.LEFT));
				
				componenteLabel=cmp.horizontalList(texto.setWidth(16));
				componenteLabel.add(contenido).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde));
				matriz.add(componenteLabel);
				
				matriz.newRow();
			}
			
			if(!informeQxDto.getInformeQxEspecialidad().getDiagnosticosPostOperatorioRelacionados().isEmpty()){
				texto=cmp.text(etiquetas.getString("msgHojaQuirurgicaForm.informeQx.dxRelacionados")).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloNegrillaL).setHorizontalAlignment(HorizontalAlignment.LEFT));
			
			
				VerticalListBuilder componenteVerticalLabel=cmp.verticalList(texto);
				TextFieldBuilder<String> vacio=cmp.text("").setStyle(stl.style().setHorizontalAlignment(HorizontalAlignment.LEFT));
				
				
				for (DtoDiagnostico diagnostico : informeQxDto.getInformeQxEspecialidad().getDiagnosticosPostOperatorioRelacionados()){
					contenido=cmp.text(diagnostico.getNombreCompletoDiagnostico()).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloSubTituloSinBold).setHorizontalAlignment(HorizontalAlignment.LEFT));
					
					componenteLabel=cmp.horizontalList(vacio.setWidth(5));
					componenteLabel.add(contenido);
					
					componenteVerticalLabel.add(componenteLabel);
				}
		
				componenteLabel=cmp.horizontalList(componenteVerticalLabel).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde));
				matriz.add(componenteLabel);
				
				matriz.newRow();
			}
			
			texto=cmp.text(etiquetas.getString("msgHojaQuirurgicaForm.informeQx.hallazgos")).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeBRNegrilla).setHorizontalAlignment(HorizontalAlignment.LEFT));
			if(!UtilidadTexto.isEmpty(informeQxDto.getInformeQxEspecialidad().getHallazgos())){
				contenido=cmp.text(informeQxDto.getInformeQxEspecialidad().getHallazgos()).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloSubTituloSinBold).setHorizontalAlignment(HorizontalAlignment.LEFT));
				
				componenteLabel=cmp.horizontalList(texto).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde));
				matriz.add(componenteLabel);
				matriz.newRow();
				
				componenteLabel=cmp.horizontalList(contenido).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde));
				matriz.add(componenteLabel);
				matriz.newRow();
			}
			
			texto=cmp.text(etiquetas.getString("msgHojaQuirurgicaForm.informeQx.tipoHerida")).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeBRNegrilla).setHorizontalAlignment(HorizontalAlignment.LEFT));
			if(informeQxDto.getInformeQxEspecialidad().getTipoHerida()!=null
					&&!UtilidadTexto.isEmpty(informeQxDto.getInformeQxEspecialidad().getTipoHerida().getAcronimo())){
				contenido=cmp.text(informeQxDto.getInformeQxEspecialidad().getTipoHerida().getNombre()).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloSubTituloSinBold).setHorizontalAlignment(HorizontalAlignment.LEFT));
				
				componenteLabel=cmp.horizontalList(texto.setWidth(16));
				componenteLabel.add(contenido).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde));
				matriz.add(componenteLabel);
				
				matriz.newRow();
			}
			
			texto=cmp.text(etiquetas.getString("msgHojaQuirurgicaForm.informeQx.dxComplicacionPostoperatorio")).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeBRNegrilla).setHorizontalAlignment(HorizontalAlignment.LEFT));
			if(informeQxDto.getInformeQxEspecialidad().getDiagnosticoPostOperatorioComplicacion()!=null
					&&informeQxDto.getInformeQxEspecialidad().getDiagnosticoPostOperatorioComplicacion().getCodigoDxPostOperatorio()>0){
				contenido=cmp.text(informeQxDto.getInformeQxEspecialidad().getDiagnosticoPostOperatorioComplicacion().getNombreCompletoDiagnostico()).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloSubTituloSinBold).setHorizontalAlignment(HorizontalAlignment.LEFT));
				
				componenteLabel=cmp.horizontalList(texto.setWidth(16));
				componenteLabel.add(contenido).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde));
				matriz.add(componenteLabel);
				
				matriz.newRow();
			}
		}else{
			texto=cmp.text(etiquetas.getString("msgHojaQuirurgicaForm.informeQx.tipoHerida")).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeBRNegrilla).setHorizontalAlignment(HorizontalAlignment.LEFT));
			if(informeQxDto.getInformeQxEspecialidad().getTipoHerida()!=null
					&&!UtilidadTexto.isEmpty(informeQxDto.getInformeQxEspecialidad().getTipoHerida().getAcronimo())){
				contenido=cmp.text(informeQxDto.getInformeQxEspecialidad().getTipoHerida().getNombre()).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloSubTituloSinBold).setHorizontalAlignment(HorizontalAlignment.LEFT));
				
				componenteLabel=cmp.horizontalList(texto.setWidth(16));
				componenteLabel.add(contenido).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde));
				matriz.add(componenteLabel);
				
				matriz.newRow();
			}
		}
		
		texto=cmp.text(etiquetas.getString("msgHojaQuirurgicaForm.informeQx.complicaciones")).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeBRNegrilla).setHorizontalAlignment(HorizontalAlignment.LEFT));
		if(!UtilidadTexto.isEmpty(informeQxDto.getInformeQxEspecialidad().getComplicaciones())){
			
			contenido=cmp.text(informeQxDto.getInformeQxEspecialidad().getComplicaciones()).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloSubTituloSinBold).setHorizontalAlignment(HorizontalAlignment.LEFT));
			
			componenteLabel=cmp.horizontalList(texto.setWidth(16)).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde));
			
			matriz.add(componenteLabel);
			
			matriz.newRow();
			
			componenteLabel=cmp.horizontalList(contenido).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde));
			
			matriz.add(componenteLabel);
			
			matriz.newRow();
		}
		
		texto=cmp.text(etiquetas.getString("msgHojaQuirurgicaForm.informeQx.materialesEspeciales")).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeBRNegrilla).setHorizontalAlignment(HorizontalAlignment.LEFT));
		if(informeQxDto.getInformeQxEspecialidad().isUsaMaterialesEspeciales()){
			contenido=cmp.text("Si").setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloSubTituloSinBold).setHorizontalAlignment(HorizontalAlignment.LEFT));
		}else{
			contenido=cmp.text("No").setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloSubTituloSinBold).setHorizontalAlignment(HorizontalAlignment.LEFT));
		}
		
		componenteLabel=cmp.horizontalList(texto.setWidth(16));
		componenteLabel.add(contenido).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde));
		matriz.add(componenteLabel);
		
		matriz.newRow();
		
		texto=cmp.text(etiquetas.getString("msgHojaQuirurgicaForm.informeQx.observacionesMaterialesEspeciales")).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeBRNegrilla).setHorizontalAlignment(HorizontalAlignment.LEFT));
		if(!UtilidadTexto.isEmpty(informeQxDto.getInformeQxEspecialidad().getObservacionesMaterialesEspeciales())){
			
			contenido=cmp.text(informeQxDto.getInformeQxEspecialidad().getObservacionesMaterialesEspeciales()).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloSubTituloSinBold).setHorizontalAlignment(HorizontalAlignment.LEFT));
			
			componenteLabel=cmp.horizontalList(texto.setWidth(16)).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde));
			
			matriz.add(componenteLabel);
			
			matriz.newRow();
			
			componenteLabel=cmp.horizontalList(contenido).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde));
			
			matriz.add(componenteLabel);
			
			matriz.newRow();
		}
		
		texto=cmp.text(etiquetas.getString("msgHojaQuirurgicaForm.informeQx.patologia")).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeBRNegrilla).setHorizontalAlignment(HorizontalAlignment.LEFT));
		if(informeQxDto.getInformeQxEspecialidad().getUltimaPatologia()!=null
				&&!UtilidadTexto.isEmpty(informeQxDto.getInformeQxEspecialidad().getUltimaPatologia().getDescripcion())){
			
			contenido=cmp.text(informeQxDto.getInformeQxEspecialidad().getUltimaPatologia().getDescripcion()).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloSubTituloSinBold).setHorizontalAlignment(HorizontalAlignment.LEFT));
			
			componenteLabel=cmp.horizontalList(texto.setWidth(16)).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde));
			
			matriz.add(componenteLabel);
			
			matriz.newRow();
			
			componenteLabel=cmp.horizontalList(contenido).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde));
			
			matriz.add(componenteLabel);
			
			matriz.newRow();
		}
		
		createComponentNotaAclaratoria(notasAclaratorias, matriz);
		
		if(!informeQxDto.getInformeQxEspecialidad().getProfesionales().isEmpty()){	
			tituloInformacionActoQx=cmp.text(etiquetas.getString("msgHojaQuirurgicaForm.informeQx.profesionalesXEspeciadad")).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeNegrilla).setBackgroundColor(Color.LIGHT_GRAY).setHorizontalAlignment(HorizontalAlignment.LEFT));
			matriz.add(tituloInformacionActoQx);
			
			matriz.newRow();
			
			boolean esOtrosProfesionales=false;
			createComponentProfesionalHQxDto(informeQxDto.getInformeQxEspecialidad().getProfesionales(), matriz,esOtrosProfesionales);
		}
		listaComponentes.add(matriz);
		return listaComponentes;
	}
	
	/**
	 * Crea el componente del reporte que muestra la seccion de Servicios de una especialidad que participa en el acto Qx
	 * 
	 * @param servicios
	 * @param profesionalesXEspecialidad
	 * @param matriz
	 * @return
	 * @author jeilones
	 * @created 25/07/2013
	 */
	private List<ComponentBuilder> createComponentServiciosQx(List<ServicioHQxDto> servicios,List<ProfesionalHQxDto> profesionalesXEspecialidad, HorizontalListBuilder matriz){
		List<ComponentBuilder>tmpServicios=new ArrayList<ComponentBuilder>(0);
		if(servicios==null||servicios.isEmpty()){
			return tmpServicios;
		}
		
		ResourceBundle etiquetas=ResourceBundle.getBundle("com.servinte.mensajes.salasCirugia.HojaQuirurgicaForm");
		
		TextFieldBuilder<String>texto=cmp.text(etiquetas.getString("msgHojaQuirurgicaForm.informeQx.servicios.subtitulo")).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeNegrilla).setBackgroundColor(Color.LIGHT_GRAY).setHorizontalAlignment(HorizontalAlignment.CENTER));
		matriz.add(texto);
		
		tmpServicios.add(matriz);
		
		for (int i = 0; i < servicios.size(); i++) {
			matriz=cmp.horizontalFlowList();
			ServicioHQxDto servicioHQxDto=servicios.get(i);
			StringBuffer nombreServicio=new StringBuffer(servicioHQxDto.getCodigoReferenciaServicio())
			.append(" - ").append(servicioHQxDto.getDescripcionReferenciaServicio());
			if(servicioHQxDto.isEsPos()){
				nombreServicio.append(" - ").append(etiquetas.getString("reporteHC.informeQx.servicio.esPOS"));
			}else{
				nombreServicio.append(" - ").append(etiquetas.getString("reporteHC.informeQx.servicio.esNOPOS"));
			}
			texto=cmp.text(nombreServicio.toString()).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloSubTituloSinBold).setHorizontalAlignment(HorizontalAlignment.LEFT));
			TextFieldBuilder<String> vacio=cmp.text("Servicio "+(i+1)+": ").setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloNegrillaL).setHorizontalAlignment(HorizontalAlignment.LEFT));
				
			HorizontalListBuilder componenteLabel=cmp.horizontalList(vacio.setWidth(10));
			componenteLabel.add(texto).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde));
				
			matriz.add(componenteLabel);
			
			tmpServicios.add(matriz);
			if(!servicioHQxDto.getProfesionalesXServicio().isEmpty()){
				List<ProfesionalHQxDto>profesionalesAPintar=new ArrayList<ProfesionalHQxDto>(0);
				for (int j = 0; j < servicioHQxDto.getProfesionalesXServicio().size(); j++) {
					ProfesionalHQxDto profesionalHQxDto=servicioHQxDto.getProfesionalesXServicio().get(j);
					boolean existe=false;
					for(ProfesionalHQxDto profesionalEspecialidad:profesionalesXEspecialidad){
						if(profesionalHQxDto.getTipoProfesional().getCodigo()==profesionalEspecialidad.getTipoProfesional().getCodigo()
								&&profesionalHQxDto.getIdMedico()==profesionalEspecialidad.getIdMedico()){
							existe=true;
						}
					}
					if(!existe){
						profesionalesAPintar.add(profesionalHQxDto);
					}
				}
				servicioHQxDto.setProfesionalesXServicio(profesionalesAPintar);
				if(!profesionalesAPintar.isEmpty()){
					matriz=cmp.horizontalFlowList();
					texto=cmp.text(etiquetas.getString("msgHojaQuirurgicaForm.informeQx.servicios.profesionales")).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeNegrilla).setBackgroundColor(Color.LIGHT_GRAY).setHorizontalAlignment(HorizontalAlignment.LEFT));
					matriz.add(texto);
					
					matriz.newRow();
					
					boolean esOtrosProfesionales=false;
					ComponentBuilder tmp=createComponentProfesionalHQxDto(servicioHQxDto.getProfesionalesXServicio(), matriz,esOtrosProfesionales);
					tmpServicios.add(tmp);
				}
			}
		}
		return tmpServicios;
	}
	
	/**
	 * Crea el componente del reporte que muestra la seccion de Notas Aclaratorias de una especialidad que participa en el acto Qx
	 * 
	 * @param notasAclaratoriaDto
	 * @param matriz
	 * @return
	 * @author jeilones
	 * @created 27/07/2013
	 */
	private ComponentBuilder<?,?> createComponentNotaAclaratoria(List<NotaAclaratoriaDto> notasAclaratoriaDto,HorizontalListBuilder matriz){
		if(notasAclaratoriaDto==null||notasAclaratoriaDto.isEmpty()){
			return matriz;
		}
		
		ResourceBundle etiquetas=ResourceBundle.getBundle("com.servinte.mensajes.salasCirugia.HojaQuirurgicaForm");

		//////////////////
		
		TextFieldBuilder<String>texto=cmp.text(etiquetas.getString("msgHojaQuirurgicaForm.informeQx.notasAclaratorias")).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeBRNegrilla).setHorizontalAlignment(HorizontalAlignment.LEFT));
		TextFieldBuilder<String>  contenido=null;
		HorizontalListBuilder componenteLabel=null;
		
		if(!notasAclaratoriaDto.isEmpty()){
			
			texto=cmp.text(etiquetas.getString("msgHojaQuirurgicaForm.informeQx.notasAclaratorias")).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeNegrilla).setHorizontalAlignment(HorizontalAlignment.LEFT));
			matriz.add(texto);
			
			matriz.newRow();
		
			TextFieldBuilder<String> vacio=cmp.text("").setStyle(stl.style().setHorizontalAlignment(HorizontalAlignment.LEFT));
			
			for(NotaAclaratoriaDto notaAclaratoriaDto:notasAclaratoriaDto){
				StringBuffer fecha=new StringBuffer();
				fecha.append(etiquetas.getString("msgHojaQuirurgicaForm.informeQx.notasAclaratorias.fecha"));
				fecha.append(" ");
				SimpleDateFormat format=new SimpleDateFormat(ConstantesBD.formatoFechaApp);
				if(notaAclaratoriaDto.getFechaGrabacion()!=null){
					fecha.append(format.format(notaAclaratoriaDto.getFechaGrabacion()));
					fecha.append(" ");
				}
				fecha.append(etiquetas.getString("msgHojaQuirurgicaForm.informeQx.notasAclaratorias.hora"));
				fecha.append(" ");
				if(!UtilidadTexto.isEmpty(notaAclaratoriaDto.getHoraGrabacion())){
					fecha.append(notaAclaratoriaDto.getHoraGrabacion());
				}
				
				 //CASO: 14439-Se agrega cada componenteLabel a la matriz
				contenido=cmp.text(fecha.toString()).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloNegrillaL).setHorizontalAlignment(HorizontalAlignment.LEFT));
				
				componenteLabel=cmp.horizontalList(vacio.setWidth(5));
				componenteLabel.add(contenido);
				matriz.add(componenteLabel);
				matriz.newRow();
				
				contenido=cmp.text(notaAclaratoriaDto.getDescripcion()).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloSubTituloSinBold).setHorizontalAlignment(HorizontalAlignment.LEFT));
				
				componenteLabel=cmp.horizontalList(vacio.setWidth(5));
				componenteLabel.add(contenido);
				matriz.add(componenteLabel);
				matriz.newRow();
				
				
				StringBuffer usuario=new StringBuffer();
				usuario.append(etiquetas.getString("msgHojaQuirurgicaForm.informeQx.notasAclaratorias.usuario"));
				usuario.append(" ");
				contenido=cmp.text(usuario.toString()).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloNegrillaL).setHorizontalAlignment(HorizontalAlignment.LEFT));
				
				componenteLabel=cmp.horizontalList(vacio.setWidth(5));
				componenteLabel.add(contenido.setWidth(7));
				
				usuario=new StringBuffer(notaAclaratoriaDto.getDatosMedico());
				contenido=cmp.text(usuario.toString()).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloSubTituloSinBold).setHorizontalAlignment(HorizontalAlignment.LEFT));
				
				componenteLabel.add(contenido);
				
				matriz.add(componenteLabel);
				matriz.newRow();
			}
			matriz.newRow();
			
		}
		
		////////////////////
		return matriz;
	}
	
	/**
	 * Crea el componente del reporte que muestra la seccion de Profesionales que participa en el acto Qx
	 * 
	 * @param profesionalHQxDtos
	 * @param matriz
	 * @param esOtrosProfesionales
	 * @return
	 * @author jeilones
	 * @created 27/07/2013
	 */
	private ComponentBuilder<?,?> createComponentProfesionalHQxDto(List<ProfesionalHQxDto> profesionalHQxDtos, HorizontalListBuilder matriz, boolean esOtrosProfesionales){
		if(profesionalHQxDtos==null){
			return matriz;
		}

		ResourceBundle etiquetas=ResourceBundle.getBundle("com.servinte.mensajes.salasCirugia.HojaQuirurgicaForm");
		
		HorizontalListBuilder componenteLabel=null;
		
		TextFieldBuilder<String>texto=null;
		if(esOtrosProfesionales){
			texto=cmp.text(etiquetas.getString("msgHojaQuirurgicaForm.informeQx.numero")).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeBRNegrilla).setHorizontalAlignment(HorizontalAlignment.CENTER));
			componenteLabel=cmp.horizontalList(texto.setWidth(16)).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde));
			matriz.add(componenteLabel);
		}
		
		texto=cmp.text(etiquetas.getString("msgHojaQuirurgicaForm.informeQx.tipoProfesional")).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeBRNegrilla).setHorizontalAlignment(HorizontalAlignment.CENTER));
		componenteLabel=cmp.horizontalList(texto).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde));
		matriz.add(componenteLabel);
		
		texto=cmp.text(etiquetas.getString("msgHojaQuirurgicaForm.informeQx.profesional")).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeBRNegrilla).setHorizontalAlignment(HorizontalAlignment.CENTER));
		componenteLabel=cmp.horizontalList(texto).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde));
		matriz.add(componenteLabel);
		
		if(!esOtrosProfesionales){
			texto=cmp.text(etiquetas.getString("msgHojaQuirurgicaForm.informeQx.especialidad")).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeBRNegrilla).setHorizontalAlignment(HorizontalAlignment.CENTER));
			componenteLabel=cmp.horizontalList(texto).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde));
			matriz.add(componenteLabel);
		}
		
		matriz.newRow();
		
		for (int i = 0; i < profesionalHQxDtos.size(); i++) {
			ProfesionalHQxDto profesionalHQxDto=profesionalHQxDtos.get(i);
				
			if(profesionalHQxDto.isEsOtroProfesionalEspecialidad()){
				texto=cmp.text(""+(i+1)).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloSubTituloSinBold).setHorizontalAlignment(HorizontalAlignment.CENTER));
				componenteLabel=cmp.horizontalList(texto.setWidth(16)).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde));
				matriz.add(componenteLabel);
			}
			
			texto=cmp.text(profesionalHQxDto.getTipoProfesional().getNombreAsocio()).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloSubTituloSinBold).setHorizontalAlignment(HorizontalAlignment.LEFT));
			componenteLabel=cmp.horizontalList(texto).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde));
			matriz.add(componenteLabel);
			StringBuffer nombreCompleto=new StringBuffer();
			
			if(!UtilidadTexto.isEmpty(profesionalHQxDto.getPrimerNombre())){
				nombreCompleto.append(profesionalHQxDto.getPrimerNombre());
				nombreCompleto.append(" ");
			}
			if(!UtilidadTexto.isEmpty(profesionalHQxDto.getSegundoNombre())){
				nombreCompleto.append(profesionalHQxDto.getSegundoNombre());
				nombreCompleto.append(" ");
			}
			if(!UtilidadTexto.isEmpty(profesionalHQxDto.getPrimerApellido())){
				nombreCompleto.append(profesionalHQxDto.getPrimerApellido());
				nombreCompleto.append(" ");
			}
			if(!UtilidadTexto.isEmpty(profesionalHQxDto.getSegundoApellido())){
				nombreCompleto.append(profesionalHQxDto.getSegundoApellido());
			}
			
			texto=cmp.text(nombreCompleto.toString()).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloSubTituloSinBold).setHorizontalAlignment(HorizontalAlignment.LEFT));
			componenteLabel=cmp.horizontalList(texto).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde));
			matriz.add(componenteLabel);
			
			if(!profesionalHQxDto.isEsOtroProfesionalEspecialidad()){
				texto=cmp.text(profesionalHQxDto.getEspecialidad().getNombre()).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloSubTituloSinBold).setHorizontalAlignment(HorizontalAlignment.LEFT));
				componenteLabel=cmp.horizontalList(texto).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde));
				matriz.add(componenteLabel);
			}
			
			matriz.newRow();
		}
		
		return matriz;
	}
	
	/**
	 * Crea el componente del reporte que muestra la seccion de Notas de enfermeria de la Hoja Qx
	 * 
	 * @param notasEnfermeria
	 * @param matriz
	 * @return
	 * @author jeilones
	 * @created 27/07/2013
	 */
	private List<ComponentBuilder> createComponentNotasEnfermeria(List<NotaEnfermeriaDto> notasEnfermeria, HorizontalListBuilder matriz){
		List<ComponentBuilder>listaComponentes=new ArrayList<ComponentBuilder>(0);
		if(notasEnfermeria==null||notasEnfermeria.isEmpty()){
			return listaComponentes;
		}
		
		ResourceBundle etiquetas=ResourceBundle.getBundle("com.servinte.mensajes.salasCirugia.HojaQuirurgicaForm");
		
		matriz.newRow();

		TextFieldBuilder<String> tituloInformacionActoQx=cmp.text(etiquetas.getString("msgHojaQuirurgicaForm.enfermeria.subtitulo")).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeNegrilla).setBackgroundColor(Color.LIGHT_GRAY).setHorizontalAlignment(HorizontalAlignment.LEFT));
		matriz.add(tituloInformacionActoQx);
		
		matriz.newRow();
		
		HorizontalListBuilder componenteLabel=null;
		
		TextFieldBuilder<String>texto=cmp.text(etiquetas.getString("msgHojaQuirurgicaForm.enfermeria.fechaHora")).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeBRNegrilla).setHorizontalAlignment(HorizontalAlignment.CENTER));
		componenteLabel=cmp.horizontalList(texto.setWidth(45)).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde));
		matriz.add(componenteLabel);
		texto=cmp.text(etiquetas.getString("msgHojaQuirurgicaForm.enfermeria.notas")).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeBRNegrilla).setHorizontalAlignment(HorizontalAlignment.CENTER));
		componenteLabel=cmp.horizontalList(texto).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde));
		matriz.add(componenteLabel);
		texto=cmp.text(etiquetas.getString("msgHojaQuirurgicaForm.enfermeria.enfermera")).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeBRNegrilla).setHorizontalAlignment(HorizontalAlignment.CENTER));
		componenteLabel=cmp.horizontalList(texto).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde));
		
		matriz.add(componenteLabel);
		
		listaComponentes.add(matriz);
		
		for (NotaEnfermeriaDto notaEnfermeriaDto : notasEnfermeria) {
			matriz=cmp.horizontalFlowList();
			StringBuffer fechaHora=new StringBuffer();
			
			if(notaEnfermeriaDto.getFechaGrabacion()!=null){
				SimpleDateFormat format=new SimpleDateFormat(ConstantesBD.formatoFechaApp);
				fechaHora.append(format.format(notaEnfermeriaDto.getFechaGrabacion()));
				fechaHora.append(" - ");
			}
			
			if(!UtilidadTexto.isEmpty(notaEnfermeriaDto.getHoraGrabacion())){
				fechaHora.append(notaEnfermeriaDto.getHoraGrabacion());
			}
			
			texto=cmp.text(fechaHora.toString()).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloSubTituloSinBold).setHorizontalAlignment(HorizontalAlignment.CENTER));
			componenteLabel=cmp.horizontalList(texto.setWidth(45)).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeSencillo));
			
			matriz.add(componenteLabel);
			
			texto=cmp.text(notaEnfermeriaDto.getDescripcion()).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloSubTituloSinBold).setHorizontalAlignment(HorizontalAlignment.LEFT));
			componenteLabel=cmp.horizontalList(texto).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeSencillo));
			
			matriz.add(componenteLabel);
			
			texto=cmp.text(notaEnfermeriaDto.getEnfermera()).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloSubTituloSinBold).setHorizontalAlignment(HorizontalAlignment.LEFT));
			componenteLabel=cmp.horizontalList(texto).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeSencillo));
			
			matriz.add(componenteLabel);
			
			listaComponentes.add(matriz);
		}
		return listaComponentes;
	}
	
	/**
	 * Crea el componente del reporte que muestra la seccion de Notas de recuperacion de la Hoja Qx
	 * 
	 * @param notasreRecuperacion
	 * @param matriz
	 * @return
	 * @author jeilones
	 * @created 27/07/2013
	 */
	private List<ComponentBuilder> createComponentNotasRecuperacion(List<NotaRecuperacionDto> notasreRecuperacion, HorizontalListBuilder matriz){
		List<ComponentBuilder>listaComponentes=new ArrayList<ComponentBuilder>(0);
		if(notasreRecuperacion==null||notasreRecuperacion.isEmpty()){
			return listaComponentes;
		}
		
		ResourceBundle etiquetas=ResourceBundle.getBundle("com.servinte.mensajes.salasCirugia.HojaQuirurgicaForm");
		
		matriz.newRow();

		TextFieldBuilder<String> tituloInformacionActoQx=cmp.text(etiquetas.getString("msgHojaQuirurgicaForm.recuperacion.subtitulo")).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeNegrilla).setBackgroundColor(Color.LIGHT_GRAY).setHorizontalAlignment(HorizontalAlignment.LEFT));
		matriz.add(tituloInformacionActoQx);
		
		listaComponentes.add(matriz);
		
		HorizontalListBuilder componenteLabel=null;
		
		SimpleDateFormat format=new SimpleDateFormat(ConstantesBD.formatoFechaApp);
		
		for (NotaRecuperacionDto notaRecuperacionDto : notasreRecuperacion) {
			for (TipoCampoNotaRecuperacionDto tipo:notaRecuperacionDto.getTiposCampoNotaRecuperacion()) {
				for(CampoNotaRecuperacionDto campo:tipo.getCamposNotaRecuperacion()){
					if(!UtilidadTexto.isEmpty(campo.getValor())){
						matriz=cmp.horizontalFlowList();
						
						TextFieldBuilder<String>texto=cmp.text(tipo.getNombreTipo()).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeBRNegrilla).setHorizontalAlignment(HorizontalAlignment.LEFT));
						componenteLabel=cmp.horizontalList(texto.setWidth(25)).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde));
						matriz.add(componenteLabel);
						
						texto=cmp.text(campo.getNombre()).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeBRNegrilla).setHorizontalAlignment(HorizontalAlignment.LEFT));
						componenteLabel=cmp.horizontalList(texto.setWidth(25)).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde));
						matriz.add(componenteLabel);
						
						StringBuffer descripcion=new StringBuffer();
						
						String espacio=" ";
						if(notaRecuperacionDto.getFechaGrabacion()!=null){
							descripcion.append(format.format(notaRecuperacionDto.getFechaGrabacion())).append(espacio)
								.append(notaRecuperacionDto.getHoraGrabacion()).append(espacio);
						}else{
							if(notaRecuperacionDto.getFechaNota()!=null){
								descripcion.append(format.format(notaRecuperacionDto.getFechaNota())).append(espacio)
								.append(notaRecuperacionDto.getHoraNota()).append(espacio);
							}
						}
						
						descripcion.append(campo.getValor()).append(espacio)
							.append(notaRecuperacionDto.getDatosMedico()).append(espacio)
							.append(notaRecuperacionDto.getEspecialidadesMedico());
						
						texto=cmp.text(descripcion.toString()).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloSubTituloSinBold).setHorizontalAlignment(HorizontalAlignment.LEFT));
						componenteLabel=cmp.horizontalList(texto).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde));
						matriz.add(componenteLabel);
						
						listaComponentes.add(matriz);
					}
				}
			}
		}
		
		return listaComponentes;
	}
	
	/**
	 * Genera la seccion de informacion general 
	 * @param mapa
	 * @param solicitud
	 * @return ComponentBuilder con la seccion 
	 */
	private ComponentBuilder createComponentInformacionGeneralUrgencias(HashMap mapa,Integer solicitud) 
	{

		//HorizontalListBuilder que va a contener toda la seccion 
		HorizontalListBuilder itemComponent;
		Integer numReg = util.UtilidadCadena.vInt(String.valueOf(mapa.get("numRegEnca")));

		//Titulo de la hoja Qx
		TextFieldBuilder<String> titulo=cmp.text(IConstantesReporteHistoriaClinica.constanteTituloHojaQuirurgica).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeNegrilla).setBackgroundColor(Color.LIGHT_GRAY).setHorizontalAlignment(HorizontalAlignment.LEFT));
		TextFieldBuilder<String> vacio=cmp.text("").setStyle(stl.style().setHorizontalAlignment(HorizontalAlignment.LEFT));

		//Se inicializa la lista con el titulo
		itemComponent=cmp.horizontalList(titulo);

		//Flags de validacion de existencia de informacion 
		Boolean flagExisteInformacion=false;
		Boolean primerSeccion=false;
		Boolean segundaSeccion=false;
		Boolean terceraSeccion=false;

		for (int j = 0; j < numReg; j++) {

			//Se valida que sea la solicitud indicada a imrpimir 
			if(solicitud.equals(Integer.valueOf(String.valueOf(mapa.get("iq_solicitud_" +j))))){

				//Las validadiciones son sacadas de los JSP 
				if((!UtilidadTexto.isEmpty(String.valueOf(mapa.get("iq_fi_"+j))))
						&&(!UtilidadTexto.isEmpty(String.valueOf(mapa.get("iq_hi_"+j))))){

					//titulo de las Seccion 
					titulo=cmp.text(IConstantesReporteHistoriaClinica.constanteFechaHoraCirugia).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeBRNegrilla).setHorizontalAlignment(HorizontalAlignment.LEFT));

					//Se obtiene el contendio de la seccion 
					TextFieldBuilder<String>  contenido=cmp.text(String.valueOf(mapa.get("iq_fi_" +j))+"  "+ String.valueOf(mapa.get("iq_hi_"+j))).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloSubTituloSinBold).setHorizontalAlignment(HorizontalAlignment.LEFT));

					//Se adiciona el titulo y el contenido 
					HorizontalListBuilder itemComponent1=cmp.horizontalList(titulo);
					itemComponent1.add(contenido);

					//Se adiciona el titulo y el contendo a itemCOmponent  
					itemComponent.newRow().add(itemComponent1.setStyle(EstilosReportesDinamicosHistoriaClinica.estiloBorde));

					//Se indica que si existe informacion en este campo
					flagExisteInformacion=true;
					primerSeccion=true;
				}
				if(!UtilidadTexto.isEmpty(String.valueOf(mapa.get("iq_duracion_"+j)))){

					//SE REALIZA EN MISMO PROCESO QUE EN EL CAMPO UNO 
					titulo=cmp.text(IConstantesReporteHistoriaClinica.constanteDuracionFinalCirugia).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeBRNegrilla).setHorizontalAlignment(HorizontalAlignment.LEFT));
					TextFieldBuilder<String>  contenido=cmp.text(String.valueOf(mapa.get("iq_duracion_"+j))).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloSubTituloSinBold).setHorizontalAlignment(HorizontalAlignment.LEFT));
					HorizontalListBuilder itemComponent1=cmp.horizontalList(titulo);
					itemComponent1.add(contenido);
					if(primerSeccion){
						itemComponent.add(itemComponent1.setStyle(EstilosReportesDinamicosHistoriaClinica.estiloBorde));
					}else{
						itemComponent.newRow().add(itemComponent1.setStyle(EstilosReportesDinamicosHistoriaClinica.estiloBorde));
					}
					flagExisteInformacion=true;
				}
				if((!UtilidadTexto.isEmpty(String.valueOf(mapa.get("iq_fi_"+j))))
						&&(!UtilidadTexto.isEmpty(String.valueOf(mapa.get("iq_hi_"+j))))){

					//SE REALIZA EN MISMO PROCESO QUE EN EL CAMPO UNO 
					titulo=cmp.text(IConstantesReporteHistoriaClinica.constanteFechaHoraIngresoSala).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeBRNegrilla).setHorizontalAlignment(HorizontalAlignment.LEFT));
					TextFieldBuilder<String>  contenido=cmp.text(String.valueOf(mapa.get("iq_fi_"+j))+"  "+ String.valueOf(mapa.get("iq_hi_"+j))).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloSubTituloSinBold).setHorizontalAlignment(HorizontalAlignment.LEFT));
					HorizontalListBuilder itemComponent1=cmp.horizontalList(titulo);
					itemComponent1.add(contenido);
					itemComponent.newRow().add(itemComponent1.setStyle(EstilosReportesDinamicosHistoriaClinica.estiloBorde));
					flagExisteInformacion=true;
					segundaSeccion=true;
				}
				if((!UtilidadTexto.isEmpty(String.valueOf(mapa.get("iq_ff_"+j))))
						&&(!UtilidadTexto.isEmpty(String.valueOf(mapa.get("iq_hi_"+j))))){

					//SE REALIZA EN MISMO PROCESO QUE EN EL CAMPO UNO 
					titulo=cmp.text(IConstantesReporteHistoriaClinica.constanteFechaHoraSalidaSala).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeBRNegrilla).setHorizontalAlignment(HorizontalAlignment.LEFT));
					TextFieldBuilder<String>  contenido=cmp.text(String.valueOf(mapa.get("iq_ff_"+j))+"  "+ String.valueOf(mapa.get("iq_hi_"+j))).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloSubTituloSinBold).setHorizontalAlignment(HorizontalAlignment.LEFT));
					HorizontalListBuilder itemComponent1=cmp.horizontalList(titulo);
					itemComponent1.add(contenido);

					flagExisteInformacion=true;
					if(segundaSeccion){
						itemComponent.add(itemComponent1.setStyle(EstilosReportesDinamicosHistoriaClinica.estiloBorde));
						segundaSeccion=true;
					}else{
						itemComponent.newRow().add(itemComponent1.setStyle(EstilosReportesDinamicosHistoriaClinica.estiloBorde));
						segundaSeccion=true;
					}
				}
				if(!UtilidadTexto.isEmpty(String.valueOf(mapa.get("enca_nombre_sala_"+j)))){

					//SE REALIZA EN MISMO PROCESO QUE EN EL CAMPO UNO 
					titulo=cmp.text(IConstantesReporteHistoriaClinica.constanteSalaDeSalidaPaciente).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeBRNegrilla).setHorizontalAlignment(HorizontalAlignment.LEFT));
					TextFieldBuilder<String>  contenido=cmp.text(String.valueOf(mapa.get("enca_nombre_sala_"+j))).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloSubTituloSinBold).setHorizontalAlignment(HorizontalAlignment.RIGHT));
					HorizontalListBuilder itemComponent1=cmp.horizontalList(titulo);
					itemComponent1.add(contenido);

					flagExisteInformacion=true;
					if(segundaSeccion){
						itemComponent.add(itemComponent1.setStyle(EstilosReportesDinamicosHistoriaClinica.estiloBorde));
					}else{
						itemComponent.newRow().add(itemComponent1.setStyle(EstilosReportesDinamicosHistoriaClinica.estiloBorde));
					}


				}
				if(!UtilidadTexto.isEmpty(String.valueOf(mapa.get("diag_principal_"+j)))){

					//SE REALIZA EN MISMO PROCESO QUE EN EL CAMPO UNO 
					titulo=cmp.text(IConstantesReporteHistoriaClinica.constanteDiagnosticosPreOperatorio).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeBRNegrilla).setHorizontalAlignment(HorizontalAlignment.LEFT));
					TextFieldBuilder<String>  contenido=cmp.text("");
					Boolean flag =false;
					HorizontalListBuilder itemComponent1=cmp.horizontalList(titulo);
					itemComponent.newRow().add(itemComponent1.setStyle(EstilosReportesDinamicosHistoriaClinica.estiloBorde));

					titulo=cmp.text(IConstantesReporteHistoriaClinica.constanteDxPrincipal).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeBRNegrilla).setHorizontalAlignment(HorizontalAlignment.LEFT));
					itemComponent1=cmp.horizontalList(titulo);

					Integer numRegsP=Integer.valueOf(String.valueOf(mapa.get("numRegDiag")));

					for (int k = 0; k < numRegsP; k++) {
						if(String.valueOf(mapa.get("diag_principal_"+k)).equals("1")){
							contenido=cmp.text(String.valueOf(mapa.get("diag_diagnostico_"+k))+"-"+String.valueOf(mapa.get("diag_tipo_cie_0"))
									+" "+ String.valueOf(mapa.get("diag_nombre_"+k))).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloSubTituloSinBold).setHorizontalAlignment(HorizontalAlignment.RIGHT));
							itemComponent1.add(contenido);
							flag=true;
						}
					}

					itemComponent1.add(vacio);
					if(flag){
						itemComponent.add(itemComponent1.setStyle(EstilosReportesDinamicosHistoriaClinica.estiloBorde));
						flagExisteInformacion=true;
					}
					Integer numRegs=Integer.valueOf(String.valueOf(mapa.get("numRegDiag")));

					//Se llena la seccion de DX relacionado
					if(numRegs>0){
						Integer contador=0;
						for (int i = 0; i < numRegs; i++) {

							if(i==0){
								if(String.valueOf(mapa.get("diag_principal_"+i)).equals("0")){
									contador=contador+1;
									titulo=cmp.text(IConstantesReporteHistoriaClinica.constanteDxRelacionado+contador+": ").setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeBRNegrilla).setHorizontalAlignment(HorizontalAlignment.LEFT));
									contenido=cmp.text( String.valueOf(mapa.get("diag_diagnostico_"+i))+" - "+String.valueOf(mapa.get("diag_tipo_cie_0"))+" "+
											String.valueOf(mapa.get("diag_nombre_"+i))).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloSubTituloSinBold).setHorizontalAlignment(HorizontalAlignment.RIGHT));
									itemComponent1=cmp.horizontalList(titulo,contenido);
									terceraSeccion=true;
								}
							}else{
								if(String.valueOf(mapa.get("diag_principal_"+i)).equals("0")){
									contador=contador+1;
									titulo=cmp.text(IConstantesReporteHistoriaClinica.constanteDxRelacionado+contador+": ").setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeBRNegrilla).setHorizontalAlignment(HorizontalAlignment.LEFT));
									contenido=cmp.text(String.valueOf(mapa.get("diag_diagnostico_"+i))+" - "+String.valueOf(mapa.get("diag_tipo_cie_0"))+" "+
											String.valueOf(mapa.get("diag_nombre_"+i))).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloSubTituloSinBold).setHorizontalAlignment(HorizontalAlignment.RIGHT));
									itemComponent1.newRow().add(titulo);   
									itemComponent1.add(contenido);
								}
							}
						}
					}

					//Se valida si existe informacion para este campo
					if(terceraSeccion){
						itemComponent.newRow().add(itemComponent1).setStyle(EstilosReportesDinamicosHistoriaClinica.estiloBorde);
						flagExisteInformacion=true;
					}

				}
			}
		}
		itemComponent.newRow();

		//Si no existe informacion se retorna null
		if(!flagExisteInformacion){
			return null;
		}else{
			return itemComponent;
		}
	}

	/**
	 * Se genera la seccion de Patologia y obs generales 
	 * @param mapa
	 * @param solicitud
	 * @return ComponentBuilder seccion Patologica y obs generales
	 */
	private ComponentBuilder createComponentPatologiaObservacionesGenerales(HashMap mapa,Integer solicitud,Integer j,HorizontalListBuilder itemComponent) 
	{
		
		HorizontalListBuilder itemComponent1;
		Integer numReg = util.UtilidadCadena.vInt(String.valueOf(mapa.get("numRegEnca")));
		TextFieldBuilder<String> vacio=cmp.text("").setStyle(stl.style().setHorizontalAlignment(HorizontalAlignment.LEFT));
		Boolean hayInformacion=false;

//		Integer tamanoMapa=Integer.valueOf(String.valueOf(mapa.get("numRegOb")));
//
//
//		for (int j = 0;j <tamanoMapa; j++) {
			if(solicitud.equals(Integer.valueOf(String.valueOf(mapa.get("enca_solicitud_" +j))))){
				if(!UtilidadTexto.isEmpty(String.valueOf(mapa.get("o_patologia_"+j)))){
					String cadenaContenido=String.valueOf(mapa.get("o_patologia_"+j));
					cadenaContenido=cadenaContenido.replace("\\n", " \n ");
					if(!cadenaContenido.trim().equals(String.valueOf(ConstantesBD.codigoNuncaValido))){
						TextFieldBuilder<String> titulo=cmp.text(IConstantesReporteHistoriaClinica.constantePatologia).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeBRNegrilla).setHorizontalAlignment(HorizontalAlignment.LEFT));
						TextFieldBuilder<String>  contenido=cmp.text(cadenaContenido).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloSubTituloSinBold).setHorizontalAlignment(HorizontalAlignment.LEFT));

						itemComponent1=cmp.horizontalList(titulo);
						itemComponent1.newRow().add(contenido);
						if(itemComponent==null){
							itemComponent =cmp.horizontalList(itemComponent1).setStyle(EstilosReportesDinamicosHistoriaClinica.estiloBorde);
						}else{
							itemComponent.newRow().add(itemComponent1).setStyle(EstilosReportesDinamicosHistoriaClinica.estiloBorde);
						}
						hayInformacion=true;
					}
				}
				if(!UtilidadTexto.isEmpty(String.valueOf(mapa.get("o_observaciones_"+j)))){
					String cadenaContenido = String.valueOf(mapa.get("o_observaciones_"+j));

					cadenaContenido=cadenaContenido.replace("\\n", " \n ");
					if(!cadenaContenido.trim().equals(String.valueOf(ConstantesBD.codigoNuncaValido))){
						TextFieldBuilder<String> titulo=cmp.text(IConstantesReporteHistoriaClinica.constanteObservacionesGenerales).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeBRNegrilla).setHorizontalAlignment(HorizontalAlignment.LEFT));
						TextFieldBuilder<String>  contenido=cmp.text(cadenaContenido).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloSubTituloSinBold).setHorizontalAlignment(HorizontalAlignment.LEFT));


						itemComponent1=cmp.horizontalList(titulo);
						itemComponent1.newRow().add(contenido);
						if(itemComponent==null){
							itemComponent =cmp.horizontalList(itemComponent1).setStyle(EstilosReportesDinamicosHistoriaClinica.estiloBorde);
						}else{
							itemComponent.newRow().add(itemComponent1).setStyle(EstilosReportesDinamicosHistoriaClinica.estiloBorde);	
						}
						hayInformacion=true;
					}
				}

			}
	//	}

		if(itemComponent!=null && hayInformacion){
			return itemComponent;
		}else{
			itemComponent=null;
			return itemComponent;

		}
	}



	/**
	 * Se genera la seccion de notas aclaratorias
	 * @param mapa
	 * @param solicitud
	 * @return Seccion de notas aclaratorias 
	 */
	private ComponentBuilder createComponentNotasAclaratorias(HashMap mapa,Integer solicitud) 
	{
		HorizontalListBuilder itemComponent=null;
		HorizontalListBuilder itemComponent1;
		TextFieldBuilder<String>  contenido=null;
		Integer tamanoMapa=Integer.valueOf(String.valueOf(mapa.get("numRegOb")));


		for (int j = 0;j <tamanoMapa; j++) {
			if(!UtilidadTexto.isEmpty(String.valueOf(mapa.get("notaclara_"+j)))){
				if(solicitud.equals(Integer.valueOf(String.valueOf(mapa.get("enca_solicitud_"+j))))){
					String cadenaContenido=String.valueOf(mapa.get("notaclara_"+j));
					cadenaContenido=cadenaContenido.replace("\\n", " \n ");

					if(!cadenaContenido.trim().equals(String.valueOf(ConstantesBD.codigoNuncaValido))){
						TextFieldBuilder<String> titulo=cmp.text(IConstantesReporteHistoriaClinica.constanteNotasAclaratorias).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeBRNegrilla).setHorizontalAlignment(HorizontalAlignment.LEFT));
						contenido=cmp.text(cadenaContenido).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloSubTituloSinBold).setHorizontalAlignment(HorizontalAlignment.LEFT));
						itemComponent1=cmp.horizontalList(titulo);
						itemComponent1.newRow().add(contenido);
						itemComponent =cmp.horizontalList(itemComponent1).setStyle(EstilosReportesDinamicosHistoriaClinica.estiloBorde);
					}
				}
			}
		}

		if(itemComponent!=null){
			contenido=cmp.text(" ").setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloSubTituloSinBold).setHorizontalAlignment(HorizontalAlignment.LEFT));
			itemComponent.newRow().add(contenido);
		}

		return itemComponent;
	}


	/**
	 * @param mapa
	 * @param solicitud
	 * @return Seccion de actor quirurgico
	 */
	private ComponentBuilder createComponentActoQuirurgico(HashMap mapa,Integer solicitud) 
	{
		HorizontalListBuilder itemComponent=null;
		HorizontalListBuilder itemComponent1;

		Integer numReg=Integer.valueOf(String.valueOf(mapa.get("numRegCir")));

		for (int j = 0; j < numReg; j++) {
			if(solicitud.equals(Integer.valueOf(String.valueOf(mapa.get("cir_solicitud_" +j))))){

				if(!UtilidadTexto.isEmpty(String.valueOf(mapa.get("iq_observaciones_"+j)))
						&& 	!String.valueOf(mapa.get("iq_observaciones_"+j)).equals(String.valueOf(ConstantesBD.codigoNuncaValido))	
				){
					String cadenaContenido=String.valueOf(mapa.get("iq_observaciones_"+j));
					cadenaContenido=cadenaContenido.replace("\\n", " \n ");

					TextFieldBuilder<String> titulo=cmp.text(IConstantesReporteHistoriaClinica.constanteDescripcionActoQuirurgico).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeBRNegrilla).setHorizontalAlignment(HorizontalAlignment.LEFT));
					TextFieldBuilder<String>  contenido=cmp.text(cadenaContenido).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloSubTituloSinBold).setHorizontalAlignment(HorizontalAlignment.LEFT));
					itemComponent1=cmp.horizontalList(titulo);
					itemComponent1.newRow().add(contenido);
					itemComponent =cmp.horizontalList(itemComponent1).setStyle(EstilosReportesDinamicosHistoriaClinica.estiloBorde);
				}
			}
		}
		return itemComponent;
	}


	/**
	 * @param mapa
	 * @param solicitud
	 * @return Seccion de servicios 
	 */
	private ComponentBuilder createComponentServicios(HashMap mapa,Integer solicitud,Integer j,HorizontalListBuilder itemComponent) 
	{
		//HorizontalListBuilder itemComponent=null;
		HorizontalListBuilder itemComponent1=null;
		HorizontalListBuilder itemComponent2=null;
		//	Integer numReg = Integer.valueOf(String.valueOf(mapa.get("numRegCir")));
		TextFieldBuilder<String>  contenido=cmp.text(" ").setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloSubTituloSinBold).setHorizontalAlignment(HorizontalAlignment.LEFT));
		TextFieldBuilder<String>  espacio=cmp.text(" ").setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloSubTituloSinBold).setHorizontalAlignment(HorizontalAlignment.LEFT));
		TextFieldBuilder<String> titulo=cmp.text(" ").setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeBRNegrilla).setHorizontalAlignment(HorizontalAlignment.CENTER));
		Boolean hayInformacion= new Boolean(false);

		//		if(numReg>0){	
		//			TextFieldBuilder<String> titulo=cmp.text(IConstantesReporteHistoriaClinica.constanteServicios).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeBRNegrilla).setHorizontalAlignment(HorizontalAlignment.CENTER));
		//			itemComponent=cmp.horizontalList(contenido);
		//			itemComponent.newRow().add(titulo).setStyle(EstilosReportesDinamicosHistoriaClinica.estiloBorde);
		//for (int j = 0; j < numReg; j++) {
		itemComponent1=null;
		itemComponent2=null;
		if ( solicitud.equals( Integer.valueOf(String.valueOf(mapa.get("cir_solicitud_"+j))))  )
		{

			String codSer = mapa.get("cir_servicio_" +j)+"";

			if(!UtilidadTexto.isEmpty(String.valueOf(mapa.get("especialidad_interviene_"+j)))){
				titulo=cmp.text(IConstantesReporteHistoriaClinica.constanteEspecialidadQueInterviene).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeBRNegrilla).setHorizontalAlignment(HorizontalAlignment.LEFT));
				contenido=cmp.text(String.valueOf(mapa.get("especialidad_interviene_"+j))).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloSubTituloSinBold).setHorizontalAlignment(HorizontalAlignment.LEFT));
				itemComponent1=cmp.horizontalList(titulo); 
				itemComponent2=cmp.horizontalList(contenido); 
				itemComponent.newRow().add(itemComponent1.setStyle(EstilosReportesDinamicosHistoriaClinica.estiloBorde));
				itemComponent.add(itemComponent2.setStyle(EstilosReportesDinamicosHistoriaClinica.estiloBorde));
				hayInformacion=true;
			}


			if(!UtilidadTexto.isEmpty(String.valueOf(mapa.get("cir_cirugia_"+j)))){


				String contenidoCirugiaServicio="";
				contenidoCirugiaServicio=String.valueOf(mapa.get("cir_cirugia_"+j));
				if(!UtilidadTexto.isEmpty(String.valueOf(mapa.get("cir_espos_"+j)))){
					if(UtilidadTexto.getBoolean(String.valueOf(mapa.get("cir_espos_"+j)))){
						contenidoCirugiaServicio+=IConstantesReporteHistoriaClinica.constantePos;
					}else{
						contenidoCirugiaServicio+=IConstantesReporteHistoriaClinica.constanteNoPos;
					}
				}

				titulo=cmp.text(IConstantesReporteHistoriaClinica.constanteServicio).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeBRNegrilla).setHorizontalAlignment(HorizontalAlignment.LEFT));
				contenido=cmp.text(contenidoCirugiaServicio).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloSubTituloSinBold).setHorizontalAlignment(HorizontalAlignment.LEFT));
				itemComponent1=cmp.horizontalList(titulo); 
				itemComponent2=cmp.horizontalList(contenido); 
				itemComponent.newRow().add(itemComponent1.setStyle(EstilosReportesDinamicosHistoriaClinica.estiloBorde));
				itemComponent.add(itemComponent2.setStyle(EstilosReportesDinamicosHistoriaClinica.estiloBorde));
				hayInformacion=true;
			}





			if(!UtilidadTexto.isEmpty(String.valueOf(mapa.get("infoqx_"+j)))){
				titulo=cmp.text(IConstantesReporteHistoriaClinica.constanteDescripcionQuirurgica).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeBRNegrilla).setHorizontalAlignment(HorizontalAlignment.LEFT));
				itemComponent.newRow().add(titulo).setStyle(EstilosReportesDinamicosHistoriaClinica.estiloBorde);
				String contenidoDescripcion = String.valueOf(mapa.get("infoqx_"+j));
				contenidoDescripcion=contenidoDescripcion.replace("\\n","\n");
				contenido=cmp.text(contenidoDescripcion).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloSubTituloSinBold).setHorizontalAlignment(HorizontalAlignment.LEFT));
				itemComponent.newRow().add(contenido).setStyle(EstilosReportesDinamicosHistoriaClinica.estiloBorde);
				hayInformacion=true;
			}



			titulo=cmp.text(IConstantesReporteHistoriaClinica.constanteDiagnosticosPostOperatorio).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeBRNegrilla).setHorizontalAlignment(HorizontalAlignment.LEFT));
			itemComponent.newRow().add(titulo).setStyle(EstilosReportesDinamicosHistoriaClinica.estiloBorde);
			Integer  numRegD = util.UtilidadCadena.vInt(mapa.get("numRegDs")+"");
			ArrayList<String> listDxRelacionado = new ArrayList<String>();
			for (int i = 0; i < numRegD; i++) {

				if ( String.valueOf(solicitud).equals(mapa.get("ds_solicitud_" +i)+"") && codSer.equals(mapa.get("ds_servicio_" +i)+"") )
				{

					itemComponent1=null;
					if(UtilidadTexto.getBoolean(String.valueOf(mapa.get("ds_principal_"+i)+"")) &&
							!UtilidadTexto.getBoolean(String.valueOf(mapa.get("ds_complicacion_"+i)))
					){
						if(String.valueOf(mapa.get("ds_principal_"+i)).equals("1")){
							titulo=cmp.text(IConstantesReporteHistoriaClinica.constanteDxPrincipal).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeBRNegrilla).setHorizontalAlignment(HorizontalAlignment.LEFT));
							contenido=cmp.text(String.valueOf(mapa.get("ds_diagnostico_" +i))).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloSubTituloSinBold).setHorizontalAlignment(HorizontalAlignment.LEFT));
							itemComponent1=cmp.horizontalList(titulo.setHeight(25));
							itemComponent1.add(contenido.setHeight(25));
							itemComponent.newRow().add(itemComponent1.setStyle(EstilosReportesDinamicosHistoriaClinica.estiloBorde));
							hayInformacion=true;
						}
					}
					if ( !UtilidadTexto.getBoolean(mapa.get("ds_principal_"+i)+"") &&
							UtilidadTexto.getBoolean(mapa.get("ds_complicacion_"+i)+"") ) {
						if(String.valueOf(mapa.get("ds_complicacion_"+i)).equals("1")){
							titulo=cmp.text(IConstantesReporteHistoriaClinica.constanteDxComplicacion).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeBRNegrilla).setHorizontalAlignment(HorizontalAlignment.LEFT));
							contenido=cmp.text(String.valueOf(mapa.get("ds_diagnostico_" +i))).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloSubTituloSinBold).setHorizontalAlignment(HorizontalAlignment.LEFT));
							itemComponent1=cmp.horizontalList(titulo.setHeight(25));
							itemComponent1.add(contenido.setHeight(25));
							itemComponent.newRow().add(itemComponent1.setStyle(EstilosReportesDinamicosHistoriaClinica.estiloBorde).setStretchType(StretchType.RELATIVE_TO_TALLEST_OBJECT));
							hayInformacion=true;
						}
					}

					if ( !UtilidadTexto.getBoolean(mapa.get("ds_principal_"+i)+"") &&
							!UtilidadTexto.getBoolean(mapa.get("ds_complicacion_"+i)+"") ) {
						if(String.valueOf(mapa.get("ds_complicacion_"+i)).equals("0")
								&& String.valueOf(mapa.get("ds_complicacion_"+i)).equals("0")){
							listDxRelacionado.add(String.valueOf(mapa.get("ds_diagnostico_" +i)));
						}
					}

				}
			}



			Integer tmp=0;
			for (int i = 0; i < listDxRelacionado.size(); i++) {
				tmp=i+1;
				if(i==0){
					titulo=cmp.text(IConstantesReporteHistoriaClinica.constanteDxRelacionado+tmp+": ").setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeBRNegrilla).setHorizontalAlignment(HorizontalAlignment.LEFT));
					contenido=cmp.text(listDxRelacionado.get(i)).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloSubTituloSinBold).setHorizontalAlignment(HorizontalAlignment.RIGHT));
					itemComponent1=cmp.horizontalList(titulo,contenido);
					hayInformacion=true;
				}else{
					titulo=cmp.text(IConstantesReporteHistoriaClinica.constanteDxRelacionado+tmp+": ").setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeBRNegrilla).setHorizontalAlignment(HorizontalAlignment.LEFT));
					contenido=cmp.text(listDxRelacionado.get(i)).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloSubTituloSinBold).setHorizontalAlignment(HorizontalAlignment.RIGHT));
					itemComponent1.newRow().add(titulo);   
					itemComponent1.add(contenido);
					hayInformacion=true;
				}
			}
			if(listDxRelacionado.size()>0){
				hayInformacion=true;
				itemComponent.newRow().add(itemComponent1).setStyle(EstilosReportesDinamicosHistoriaClinica.estiloBorde);
			}

		}


		//	}
		//		}


		if(itemComponent!=null && hayInformacion){
			contenido=cmp.text(" ").setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloSubTituloSinBold).setHorizontalAlignment(HorizontalAlignment.LEFT));
			itemComponent.newRow().add(contenido);
			return itemComponent;
		}else{
			return null;
		}


	}

	/**
	 * @return Margenes de reporte 
	 */
	public MarginBuilder crearMagenesSubReporte()
	{
		MarginBuilder margin;
		margin = margin()
		.setTop(0)
		.setBottom(0)
		.setLeft(0)
		.setRight(0)
		;

		return margin;
	}

}
