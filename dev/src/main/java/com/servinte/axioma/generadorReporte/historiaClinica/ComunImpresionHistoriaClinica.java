/**
 * 
 */
package com.servinte.axioma.generadorReporte.historiaClinica;

import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;
import static net.sf.dynamicreports.report.builder.DynamicReports.stl;

import java.awt.Image;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;

import net.sf.dynamicreports.report.builder.component.HorizontalListBuilder;
import net.sf.dynamicreports.report.builder.component.TextFieldBuilder;
import net.sf.dynamicreports.report.constant.HorizontalAlignment;
import net.sf.dynamicreports.report.constant.StretchType;
import net.sf.dynamicreports.report.constant.VerticalAlignment;
import util.ConstantesBD;
import util.ConstantesCamposParametrizables;
import util.ConstantesIntegridadDominio;
import util.PathSolver;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.reportes.dinamico.EstilosReportesDinamicosHistoriaClinica;

import com.princetonsa.dto.historiaClinica.componentes.DtoDesarrolloPediatria;
import com.princetonsa.dto.historiaClinica.componentes.DtoEdadAlimentacionPediatria;
import com.princetonsa.dto.historiaClinica.componentes.DtoHistoriaMenstrual;
import com.princetonsa.dto.historiaClinica.componentes.DtoObservacionesPediatria;
import com.princetonsa.dto.historiaClinica.componentes.DtoOftalmologia;
import com.princetonsa.dto.historiaClinica.componentes.DtoPediatria;
import com.princetonsa.dto.historiaClinica.parametrizacion.DtoCampoParametrizable;
import com.princetonsa.dto.historiaClinica.parametrizacion.DtoComponente;
import com.princetonsa.dto.historiaClinica.parametrizacion.DtoElementoParam;
import com.princetonsa.dto.historiaClinica.parametrizacion.DtoEscala;
import com.princetonsa.dto.historiaClinica.parametrizacion.DtoSeccionFija;
import com.princetonsa.dto.historiaClinica.parametrizacion.DtoSeccionParametrizable;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.atencion.SignoVital;
import com.servinte.axioma.dto.historiaClinica.CurvaCrecimientoParametrizabDto;
import com.servinte.axioma.dto.historiaClinica.HistoricoImagenPlantillaDto;

/**
 * @author JorOsoVe
 * 
 */
public class ComunImpresionHistoriaClinica 
{

	/**
	 * 
	 * @param dataSource
	 * @param dto
	 * @param seccionFija
	 * @param mundoValoracion
	 * @param paciente 
	 * @param dthip 
	 * @param dtoPediatria 
	 * @param dtoOftalmologia 
	 * @param dtoHistoriaMenstrual 
	 * @param arrayList 
	 */
	public static ArrayList<HorizontalListBuilder> seccionesParametrizables( DtoSeccionFija seccionFija, PersonaBasica paciente, ArrayList<SignoVital> signosVitales, DtoHistoriaMenstrual historiaMenstrual, DtoOftalmologia oftalmologia, DtoPediatria pediatria, List<HistoricoImagenPlantillaDto> dthip)
	{
		ArrayList<HorizontalListBuilder> itemComponentArray=new ArrayList<HorizontalListBuilder>();

		TextFieldBuilder<String> texto;
		TextFieldBuilder<String> titulo;
		String tempo="";

		if(seccionFija.getNumElementosVisibles()>0)
		{
			for(DtoElementoParam elemento:seccionFija.getElementos())
			{
				if(elemento.isVisible())
				{
					HorizontalListBuilder itemComponent=cmp.horizontalList();


					boolean puedeIngresarHojaObstetrica=true;
					if(elemento.isComponente())
					{
						if(((DtoComponente)elemento).getCodigoTipo()==ConstantesCamposParametrizables.tipoComponenteHojaObstetrica){
							if(paciente.getCodigoSexo()!=ConstantesBD.codigoSexoFemenino)
							{
								puedeIngresarHojaObstetrica=false;
							}
						}
					}
					if(puedeIngresarHojaObstetrica &&(!elemento.isSeccion()||(elemento.isSeccion()&&!elemento.getCodigo().equals("")&&!elemento.getDescripcion().equals(""))))
					{


						if(elemento.isSeccion())
						{
							DtoSeccionParametrizable seccion = (DtoSeccionParametrizable)elemento;
							if(obtenerCantidadInformacionSeccionesAMostrar(itemComponent, seccion)>0){
								titulo=cmp.text((elemento.getDescripcion()).toUpperCase()).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeNegrilla).setHorizontalAlignment(HorizontalAlignment.LEFT));
								itemComponent=cmp.horizontalList(titulo);
							}
							if(itemComponent==null){
								itemComponent=cmp.horizontalList();
							}
						}else{
							titulo=cmp.text((elemento.getDescripcion()).toUpperCase()).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeNegrilla).setHorizontalAlignment(HorizontalAlignment.LEFT));
							itemComponent=cmp.horizontalList(titulo);
						}
					}
					else{
						itemComponent=cmp.horizontalList();
					}



					//dibular Componentes.
					if(elemento.isComponente())
					{
												DtoComponente componente=(DtoComponente)elemento;
												switch(componente.getCodigoTipo())
												{
													case ConstantesCamposParametrizables.tipoComponenteSignosVitales:
														//listo campos en blanco 1178
														crearComponenteSignosVitales(itemComponent,seccionFija,signosVitales,elemento,paciente);
													break;
													case ConstantesCamposParametrizables.tipoComponenteGinecologia:
														//listo campos en blanco 1178
														crearComponenGinecologia(itemComponent,seccionFija,historiaMenstrual,elemento,paciente);
													break;
													case ConstantesCamposParametrizables.tipoComponenteOftalmologia:
														//listo campos en blanco 1178
														crearComponenOftalmologia(itemComponent,seccionFija,oftalmologia,elemento,paciente);
													break;
													case ConstantesCamposParametrizables.tipoComponentePediatria:
														//listo campos en blanco 1178
														crearComponenPediatria(itemComponent,seccionFija,pediatria,elemento,paciente);
													break;
													case ConstantesCamposParametrizables.tipoComponenteCurvaCrecimiento:
														//listo campos en blanco 1178
														crearComponenteCurvaCrecimiento(itemComponent,dthip);
													break;
													
												}
												for(int a=0;a<componente.getElementos().size();a++)
												{
													if(componente.getElementos().get(a).isVisible())
													{
														if(componente.getElementos().get(a).isEscala())
														{
															DtoEscala escala = (DtoEscala)componente.getElementos().get(a);
															mostrarInformacionEscalas(itemComponent,escala);
														}
														else if(componente.getElementos().get(a).isSeccion())
														{
															DtoSeccionParametrizable seccion = (DtoSeccionParametrizable)componente.getElementos().get(a);
															if(!UtilidadTexto.getBoolean(seccion.getIndicativoRestriccionValCamp()))
															{
																if(!seccion.getCodigo().equals("")&&!seccion.getDescripcion().equals(""))
																{
																	titulo=cmp.text(""+seccion.getDescripcion()).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeNegrilla).setHorizontalAlignment(HorizontalAlignment.LEFT));
																	if(itemComponent==null)
																	{
																		itemComponent=cmp.horizontalList(titulo);
																	}
																	else
																	{
																		itemComponent.newRow().add(titulo);
																	}
																}
																mostrarInformacionSecciones(itemComponent,seccion);
															}
															
														}
													}
												}						
					}
					else if(elemento.isEscala())
					{
						//						DtoEscala escala = (DtoEscala)elemento;
						//						mostrarInformacionEscalas(itemComponent,escala);
					}
					else if(elemento.isSeccion())
					{
						DtoSeccionParametrizable seccion = (DtoSeccionParametrizable)elemento;
						mostrarInformacionSecciones(itemComponent,seccion);
					}

					if(itemComponent!=null){
						itemComponentArray.add(cmp.horizontalList(itemComponent.setStretchType(StretchType.RELATIVE_TO_BAND_HEIGHT)).setStretchType(StretchType.RELATIVE_TO_BAND_HEIGHT));
					}
				}
			}
		}
		return itemComponentArray;
	}

	/**
	 * Se encarga de pintar las secciones parametrizadas, y llamar a métodos que validan 
	 * @param itemComponent
	 * @param seccion
	 */
	public static  void mostrarInformacionSecciones(HorizontalListBuilder itemComponent,DtoSeccionParametrizable seccion) 
	{
		TextFieldBuilder<String> texto;
		ArrayList<ArrayList<String>> listaCadenasComponenetes=new ArrayList<ArrayList<String>>();



		//Se adiciona row inicial - Armando osorio 
		if(itemComponent!=null)
			itemComponent.newRow();

		int campos=0;
		int conta=0;
		// Se recorren los campo a pintar 
		for(DtoCampoParametrizable campo:seccion.getCampos())
		{
			conta++;
			if(seccion.getColumnasSeccion()==campos||seccion.getColumnasSeccion()<(campos+campo.getColumnasOcupadas())||(campo.isUnicoXFila()))
			{

				if(itemComponent!=null)
				{
					// Se validan y adicionan campos a pintar 
					itemComponent=llenarItemComponent(itemComponent, listaCadenasComponenetes);
					
					//Se inicializa la lista de campos
					listaCadenasComponenetes= new ArrayList<ArrayList<String>>();
					
					//nuevo row de separación
					itemComponent.newRow();
				}
				else
				{
					//Cuando itemComponent es null se inicializa con el título de la sección
					TextFieldBuilder<String> titulo=cmp.text((seccion.getDescripcion()+"").toUpperCase()).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeNegrilla).setHorizontalAlignment(HorizontalAlignment.LEFT));
					itemComponent=cmp.horizontalList(titulo);
				}

				//Se inicialzia el contador para validar la cantidad de campos por linea  
				campos=0;
			}
			
			//adiciona de campos a dibujar
			listaCadenasComponenetes.add(dibujarCampoCadena(itemComponent,seccion,campo));
			
			//cuando solo hay un solo campo a pintar entra por este lado
			if(seccion.getCampos().size()==1 || seccion.getCampos().size()==conta){
				itemComponent=llenarItemComponent(itemComponent, listaCadenasComponenetes);
			}

			//se cuentan las columnas ocupadas por lineas 
			campos=campos+campo.getColumnasOcupadas();
		}
		
		//adiciona de secciones internas hasta nivel 3 de profundidad
		for(int i=0;i<seccion.getSecciones().size();i++)
		{
			DtoSeccionParametrizable subseccion2 = seccion.getSecciones().get(i);
			
			//se validad si es visible 
			if(subseccion2.isVisible())
			{
				if(!subseccion2.getDescripcion().equals(""))
				{
					//se valida si se debe pintar el titulo de la sección
					if(pintarTituloSeccionDentroSeccion(subseccion2.getCampos(), subseccion2)){
						itemComponent.newRow();
						texto=cmp.text(subseccion2.getDescripcion()).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde)).setHorizontalAlignment(HorizontalAlignment.LEFT);
						itemComponent.add(texto);
					}
				}	
				
				//Se recorren los campos de seccion interna de nivel 2 
				for(DtoCampoParametrizable campo:subseccion2.getCampos())
				{
					//se valida si se debe pintar campos
					if(validacionDibujarCampoSeccionDentroSeccion(subseccion2, campo)){
						itemComponent.newRow();
						
						//Se pintan campos
						dibujarCampo(itemComponent,subseccion2,campo);
					}
				}
				
				// Se recorren las secciones de nivel 3
				for(int j=0;j<subseccion2.getSecciones().size();j++)
				{
					DtoSeccionParametrizable subseccion3 = subseccion2.getSecciones().get(j);
					
					//Se valida si es visible
					if(subseccion3.isVisible())
					{
						if(!subseccion3.getDescripcion().equals(""))
						{
							//Se valida si se debe pintar el título de la seccion 
							if(pintarTituloSeccionDentroSeccion(subseccion3.getCampos(), subseccion3)){
								itemComponent.newRow();
								texto=cmp.text(subseccion3.getDescripcion()).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde)).setHorizontalAlignment(HorizontalAlignment.LEFT);
								itemComponent.add(texto);
							}
						}	
						
						//Se recorren los campos de nivel 3 de profundidad 
						for(DtoCampoParametrizable campo:subseccion3.getCampos())
						{
							//Se valida si deben se pintados
							if(validacionDibujarCampoSeccionDentroSeccion(subseccion3, campo)){
								itemComponent.newRow();
								
								//Se dibuja el campo a mostrar en el reporte
								dibujarCampo(itemComponent,subseccion3,campo);
							}
						}
					}
				}

			}
		}

	} 

	/**
	 * método que se encarga de validar si se pinta o no el título de una sección dinámica
	 * @param campos
	 * @param subseccion2
	 * @return Si se pinta o no el titulo de la sección paramétrizada
	 */
	public static Boolean pintarTituloSeccionDentroSeccion(ArrayList<DtoCampoParametrizable> campos ,DtoSeccionParametrizable subseccion2){
		Boolean flagpintar=false;

		//Se recorren todos los campos posibles a pintar
		for(DtoCampoParametrizable campo:campos)
		{
			//Se valida si por los menos hay uno completo para pintar 
			if(validacionDibujarCampoSeccionDentroSeccion(subseccion2, campo)){
				flagpintar=true;
			}
		}

		//Se retorna el estado del flag
		return flagpintar;
	}


	/**
	 * Se encarga de validar si se pinta un campo o no cuando este es vacio 
	 * @param seccion
	 * @param campo
	 * @return si se pinta o no un campo
	 */
	public static  Boolean   validacionDibujarCampoSeccionDentroSeccion(DtoSeccionParametrizable seccion, DtoCampoParametrizable campo) 
	{
		TextFieldBuilder<String> texto;

		//Primero se pregunta si el campo se debe mostrar
		if(campo.isMostrar())
		{
			if(campo.getTipoHtml().equals(ConstantesCamposParametrizables.campoTipoTextArea))
			{
				//Si el campo no está vacio entonces se pinta 
				if(!UtilidadTexto.isEmpty(campo.getValor().trim())){
					return true;
				}
			}
			else if(campo.getTipoHtml().equals(ConstantesCamposParametrizables.campoTipoCheckBox))
			{
				//Se valida y asigna el valor de la etiqueta del campo 
				if(!UtilidadTexto.isEmpty(campo.getEtiqueta()))
				{
					texto=cmp.text(campo.getEtiqueta()).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde)).setHorizontalAlignment(HorizontalAlignment.LEFT);
				}
				
				//Se recorren los valores de tipo checkbos 
				for(int i=0;i<campo.getOpciones().size();i++)
				{
					if(UtilidadTexto.getBoolean(campo.getOpciones().get(i).getSeleccionado()))
					{
						
						String tempo=""+campo.getOpciones().get(i).getValor();
						if(campo.tieneValoresOpcionesCampo())
						{
							tempo=tempo+"    "+campo.getOpciones().get(i).getValoresOpcionRegistrado();
						}

						if(!UtilidadTexto.isEmpty(tempo.trim())){
							return true;
						}
					}
				}
			}
			else if(campo.getTipoHtml().equals(ConstantesCamposParametrizables.campoTipoRadio))
			{
				if(!UtilidadTexto.isEmpty(campo.getEtiqueta()))
				{
					texto=cmp.text(campo.getEtiqueta()).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde)).setHorizontalAlignment(HorizontalAlignment.LEFT);
				}

				String tempo=""+(campo.getValor().equals("")?"":(campo.getValor().split(ConstantesBD.separadorSplit).length>1?campo.getValor().split(ConstantesBD.separadorSplit)[1]:campo.getValor()));
				if(!campo.getValoresOpcion().equals(""))
				{
					tempo=tempo+"    "+campo.getValoresOpcion();
				}

				if(!UtilidadTexto.isEmpty(tempo.trim())){
					return true;
				}


			}//
			else if (campo.getTipoHtml().equals(ConstantesCamposParametrizables.campoTipoSelect))
			{

				Integer contadorElementosNoVacios=new Integer(0);
				texto=cmp.text(campo.getEtiqueta()).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde)).setHorizontalAlignment(HorizontalAlignment.LEFT);
				if(campo.getTipoHtml().equals(ConstantesCamposParametrizables.campoTipoSelect) &&campo.getOpciones().size() > 0)
				{
					for(int i=0;i<campo.getOpciones().size();i++)
					{
						if(campo.getOpciones().get(i).getSeleccionado().equals(ConstantesBD.acronimoSi))
						{
							texto=cmp.text(campo.getOpciones().get(i).getValor()).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde)).setHorizontalAlignment(HorizontalAlignment.LEFT);

							if(!UtilidadTexto.isEmpty(campo.getOpciones().get(i).getValor().trim())){
								contadorElementosNoVacios++;
							}
						}
					}
				}
				else
				{
					String tempo=""+(campo.getValor().equals("")?"":(campo.getValor().split(ConstantesBD.separadorSplit).length>1?campo.getValor().split(ConstantesBD.separadorSplit)[1]:campo.getValor()));
					if(!campo.getValoresOpcion().equals(""))
					{
						tempo=tempo+"    "+campo.getValoresOpcion();
					}

					if(!UtilidadTexto.isEmpty(tempo.trim())){
						contadorElementosNoVacios++;
					}
				}

				if(contadorElementosNoVacios>0){
					return true;
				}
			}////
			else if (campo.getTipoHtml().equals(ConstantesCamposParametrizables.campoTipoText))
			{
				texto=cmp.text(campo.getEtiqueta()).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde)).setHorizontalAlignment(HorizontalAlignment.LEFT);

				String tempo=campo.getValor()+(!campo.getNombreUnidad().equals("")&&!campo.getValor().equals("")?" "+campo.getNombreUnidad():"");

				if(!UtilidadTexto.isEmpty(tempo.trim())){
					return true;
				}
			}
			else if (campo.getTipoHtml().equals(ConstantesCamposParametrizables.campoTipoLabel))
			{
				texto=cmp.text(campo.getEtiqueta()).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde)).setHorizontalAlignment(HorizontalAlignment.LEFT);
			}
		}

		return false;
	}

	/**
	 * Método que se encarga de contar cuantos campos hay que pintar
	 * @param itemComponent
	 * @param seccion
	 * @return cantidad de campos a ser pintados en una linea
	 */
	public static  Integer obtenerCantidadInformacionSeccionesAMostrar(HorizontalListBuilder itemComponent,DtoSeccionParametrizable seccion) 
	{
		TextFieldBuilder<String> texto;
		ArrayList<ArrayList<String>> listaCadenasComponenetes=new ArrayList<ArrayList<String>>();
		Integer cantidadCampos = new Integer(0);


		//comentariar esto.
		if(itemComponent!=null){
			itemComponent.newRow();

			int campos=0;
			for(DtoCampoParametrizable campo:seccion.getCampos())
			{
				if(seccion.getColumnasSeccion()==campos||seccion.getColumnasSeccion()<(campos+campo.getColumnasOcupadas())||(campo.isUnicoXFila()))
				{

					if(itemComponent!=null)
					{
						cantidadCampos=cantidadCampos+obtenerCantidadSeccionesAMostrar(itemComponent, listaCadenasComponenetes);
					}
					else
					{
					}

					campos=0;
				}
				listaCadenasComponenetes.add(dibujarCampoCadena(itemComponent,seccion,campo));
				if(seccion.getCampos().size()==1){
					cantidadCampos=cantidadCampos+obtenerCantidadSeccionesAMostrar(itemComponent, listaCadenasComponenetes);
				}


				campos=campos+campo.getColumnasOcupadas();
			}

			for(int i=0;i<seccion.getSecciones().size();i++)
			{
				DtoSeccionParametrizable subseccion2 = seccion.getSecciones().get(i);
				if(subseccion2.isVisible())
				{
					if(!subseccion2.getDescripcion().equals(""))
					{
						itemComponent.newRow();
						texto=cmp.text(subseccion2.getDescripcion()).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde)).setHorizontalAlignment(HorizontalAlignment.LEFT);
						itemComponent.add(texto);
					}	
					for(DtoCampoParametrizable campo:subseccion2.getCampos())
					{
						itemComponent.newRow();
						dibujarCampo(itemComponent,subseccion2,campo);
					}
					for(int j=0;j<subseccion2.getSecciones().size();j++)
					{
						DtoSeccionParametrizable subseccion3 = subseccion2.getSecciones().get(j);
						if(subseccion3.isVisible())
						{
							if(!subseccion3.getDescripcion().equals(""))
							{
								itemComponent.newRow();
								texto=cmp.text(subseccion3.getDescripcion()).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde)).setHorizontalAlignment(HorizontalAlignment.LEFT);
								itemComponent.add(texto);
							}	
							for(DtoCampoParametrizable campo:subseccion3.getCampos())
							{
								itemComponent.newRow();
								dibujarCampo(itemComponent,subseccion3,campo);
							}
						}
					}

				}//seccion2 es visible
			}
		}
		return cantidadCampos;
	}


	/**
	 * Método que se encarga de adicionar a un HorizontalListBuilder los campos con sus titulos y valores a mostar 
	 * @param itemComponent
	 * @param listaCadenasComponenetes
	 * @return HorizontalListBuilder con los campos a pintar
	 */
	public static HorizontalListBuilder llenarItemComponent(HorizontalListBuilder itemComponent,ArrayList<ArrayList<String>> listaCadenasComponenetes){
		Boolean flagContieneVacios=false;
		TextFieldBuilder<String> texto;

		for (int i = 0; i < listaCadenasComponenetes.size(); i++) {
			for (int j = 0; j <listaCadenasComponenetes.get(i).size(); j++) {
				if(UtilidadTexto.isEmpty(listaCadenasComponenetes.get(i).get(j))){
					flagContieneVacios=true;
				}
			}


		}

		for (int i = 0; i < listaCadenasComponenetes.size(); i++) {
			if(!flagContieneVacios){
				for (int j = 0; j <listaCadenasComponenetes.get(i).size(); j++) {
					texto=cmp.text(listaCadenasComponenetes.get(i).get(j)).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde)).setHorizontalAlignment(HorizontalAlignment.LEFT);
					itemComponent.add(texto);
				}
			}
		}
		flagContieneVacios=false;
		return itemComponent;
	}


	/**
	 * Método que se encarga de obtener la cantidad de campos a pintar 
	 * @param itemComponent
	 * @param listaCadenasComponenetes
	 * @return Cantidad de campos a pintar
	 */
	public static Integer obtenerCantidadSeccionesAMostrar(HorizontalListBuilder itemComponent,ArrayList<ArrayList<String>> listaCadenasComponenetes){
		Boolean flagContieneVacios=false;
		Integer contador= new Integer (0);

		for (int i = 0; i < listaCadenasComponenetes.size(); i++) {
			for (int j = 0; j <listaCadenasComponenetes.get(i).size(); j++) {
				if(UtilidadTexto.isEmpty(listaCadenasComponenetes.get(i).get(j))){
					flagContieneVacios=true;
				}
			}


		}

		for (int i = 0; i < listaCadenasComponenetes.size(); i++) {
			if(!flagContieneVacios){
				for (int j = 0; j <listaCadenasComponenetes.get(i).size(); j++) {
					contador++;
				}
			}
		}
		flagContieneVacios=false;
		return contador;
	}


	/**
	 * Método que se encarga de adicionar en una lista los valores de los campos a pintar 
	 * @param itemComponent
	 * @param seccion
	 * @param campo
	 * @return  ArrayList<String> con valores en cadena que se van pintar
	 */
	public static  ArrayList<String> dibujarCampoCadena(HorizontalListBuilder itemComponent,DtoSeccionParametrizable seccion, DtoCampoParametrizable campo) 
	{
		TextFieldBuilder<String> texto;
		ArrayList<String> tmpCampos  = new ArrayList<String>();

		///////DIBUJA UN CAMPO ESPECIFICO  
		{
			//Primero se pregunta si el campo se debe mostrar
			if(campo.isMostrar())
			{
				if(campo.getTipoHtml().equals(ConstantesCamposParametrizables.campoTipoTextArea))
				{
					if(!UtilidadTexto.isEmpty(campo.getValor().trim())){
						tmpCampos.add(campo.getEtiqueta()+"  "+campo.getValor());
					}else{
						tmpCampos.add("");
					}
					return tmpCampos;
				}
				else if(campo.getTipoHtml().equals(ConstantesCamposParametrizables.campoTipoCheckBox))
				{
					if(!UtilidadTexto.isEmpty(campo.getEtiqueta()))
					{
						texto=cmp.text(campo.getEtiqueta()).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde)).setHorizontalAlignment(HorizontalAlignment.LEFT);
						tmpCampos.add(campo.getEtiqueta());
					}
					for(int i=0;i<campo.getOpciones().size();i++)
					{
						if(UtilidadTexto.getBoolean(campo.getOpciones().get(i).getSeleccionado()))
						{
							String tempo=""+campo.getOpciones().get(i).getValor();
							if(campo.tieneValoresOpcionesCampo())
							{
								tempo=tempo+"    "+campo.getOpciones().get(i).getValoresOpcionRegistrado();
							}
							tmpCampos.add(tempo);
						}
					}
					return tmpCampos;
				}
				else if(campo.getTipoHtml().equals(ConstantesCamposParametrizables.campoTipoRadio))
				{
					if(!UtilidadTexto.isEmpty(campo.getEtiqueta()))
					{
						texto=cmp.text(campo.getEtiqueta()).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde)).setHorizontalAlignment(HorizontalAlignment.LEFT);
						tmpCampos.add(campo.getEtiqueta());
					}

					String tempo=""+(campo.getValor().equals("")?"":(campo.getValor().split(ConstantesBD.separadorSplit).length>1?campo.getValor().split(ConstantesBD.separadorSplit)[1]:campo.getValor()));
					if(!campo.getValoresOpcion().equals(""))
					{
						tempo=tempo+"    "+campo.getValoresOpcion();
					}
					texto=cmp.text(tempo).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde)).setHorizontalAlignment(HorizontalAlignment.LEFT);
					tmpCampos.add(tempo);
					return tmpCampos;
				}//
				else if (campo.getTipoHtml().equals(ConstantesCamposParametrizables.campoTipoSelect))
				{
					if(!campo.getEtiqueta().isEmpty()) {
						texto=cmp.text(campo.getEtiqueta()).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde)).setHorizontalAlignment(HorizontalAlignment.LEFT);
						tmpCampos.add(campo.getEtiqueta());
					}	
						
					if(campo.getTipoHtml().equals(ConstantesCamposParametrizables.campoTipoSelect) &&campo.getOpciones().size() > 0)
					{
						for(int i=0;i<campo.getOpciones().size();i++)
						{
							if(campo.getOpciones().get(i).getSeleccionado().equals(ConstantesBD.acronimoSi))
							{
								texto=cmp.text(campo.getOpciones().get(i).getValor()).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde)).setHorizontalAlignment(HorizontalAlignment.LEFT);
								tmpCampos.add(campo.getOpciones().get(i).getValor());
							}
						}
					}
					else
					{
						String tempo=""+(campo.getValor().equals("")?"":(campo.getValor().split(ConstantesBD.separadorSplit).length>1?campo.getValor().split(ConstantesBD.separadorSplit)[1]:campo.getValor()));
						if(!campo.getValoresOpcion().equals(""))
						{
							tempo=tempo+"    "+campo.getValoresOpcion();
						}
						texto=cmp.text(tempo).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde)).setHorizontalAlignment(HorizontalAlignment.LEFT);
						tmpCampos.add(tempo);
					}	

					return tmpCampos;
				}////
				else if (campo.getTipoHtml().equals(ConstantesCamposParametrizables.campoTipoText))
				{
					if(!campo.getEtiqueta().isEmpty()) {
						texto=cmp.text(campo.getEtiqueta()).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde)).setHorizontalAlignment(HorizontalAlignment.LEFT);
						tmpCampos.add(campo.getEtiqueta());
					}

					String tempo=campo.getValor()+(!campo.getNombreUnidad().equals("")&&!campo.getValor().equals("")?" "+campo.getNombreUnidad():"");
					texto=cmp.text(tempo).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde)).setHorizontalAlignment(HorizontalAlignment.LEFT);
					tmpCampos.add(tempo);
					return tmpCampos;
				}
				else if (campo.getTipoHtml().equals(ConstantesCamposParametrizables.campoTipoLabel))
				{
					texto=cmp.text(campo.getEtiqueta()).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde)).setHorizontalAlignment(HorizontalAlignment.LEFT);
					tmpCampos.add(campo.getEtiqueta());
					return tmpCampos;
				}
			}
		}////////FINALIZA LA IMPRESION DE CAMPOS
		return tmpCampos;
	}


	/**
	 * Método que se encarga dibujar un campo según sea el tipo 
	 * @param itemComponent
	 * @param seccion
	 * @param campo
	 */
	public static  void  dibujarCampo(HorizontalListBuilder itemComponent,DtoSeccionParametrizable seccion, DtoCampoParametrizable campo) 
	{
		TextFieldBuilder<String> texto;

		///////DIBUJA UN CAMPO ESPECIFICO  
		{
			//Primero se pregunta si el campo se debe mostrar
			if(campo.isMostrar())
			{
				if(campo.getTipoHtml().equals(ConstantesCamposParametrizables.campoTipoTextArea))
				{
					texto=cmp.text(campo.getEtiqueta()+"  "+campo.getValor())
					.setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde))
					.setHorizontalAlignment(HorizontalAlignment.LEFT);


					itemComponent.add(texto);


				}
				else if(campo.getTipoHtml().equals(ConstantesCamposParametrizables.campoTipoCheckBox))
				{
					if(!UtilidadTexto.isEmpty(campo.getEtiqueta()))
					{
						texto=cmp.text(campo.getEtiqueta()).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde)).setHorizontalAlignment(HorizontalAlignment.LEFT);
						itemComponent.add(texto);
					}
					for(int i=0;i<campo.getOpciones().size();i++)
					{
						if(UtilidadTexto.getBoolean(campo.getOpciones().get(i).getSeleccionado()))
						{
							String tempo=""+campo.getOpciones().get(i).getValor();
							if(campo.tieneValoresOpcionesCampo())
							{
								tempo=tempo+"    "+campo.getOpciones().get(i).getValoresOpcionRegistrado();
							}
							texto=cmp.text(tempo).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde)).setHorizontalAlignment(HorizontalAlignment.LEFT);
							itemComponent.add(texto);
						}
					}
				}
				else if(campo.getTipoHtml().equals(ConstantesCamposParametrizables.campoTipoRadio))
				{
					if(!UtilidadTexto.isEmpty(campo.getEtiqueta()))
					{
						texto=cmp.text(campo.getEtiqueta()).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde)).setHorizontalAlignment(HorizontalAlignment.LEFT);
						itemComponent.add(texto);
					}

					String tempo=""+(campo.getValor().equals("")?"":(campo.getValor().split(ConstantesBD.separadorSplit).length>1?campo.getValor().split(ConstantesBD.separadorSplit)[1]:campo.getValor()));
					if(!campo.getValoresOpcion().equals(""))
					{
						tempo=tempo+"    "+campo.getValoresOpcion();
					}
					texto=cmp.text(tempo).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde)).setHorizontalAlignment(HorizontalAlignment.LEFT);
					itemComponent.add(texto);
				}//
				else if (campo.getTipoHtml().equals(ConstantesCamposParametrizables.campoTipoSelect))
				{
					texto=cmp.text(campo.getEtiqueta()).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde)).setHorizontalAlignment(HorizontalAlignment.LEFT);
					itemComponent.add(texto);
					if(campo.getTipoHtml().equals(ConstantesCamposParametrizables.campoTipoSelect) &&campo.getOpciones().size() > 0)
					{
						for(int i=0;i<campo.getOpciones().size();i++)
						{
							if(campo.getOpciones().get(i).getSeleccionado().equals(ConstantesBD.acronimoSi))
							{
								texto=cmp.text(campo.getOpciones().get(i).getValor()).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde)).setHorizontalAlignment(HorizontalAlignment.LEFT);
								itemComponent.add(texto);
							}
						}
					}
					else
					{
						String tempo=""+(campo.getValor().equals("")?"":(campo.getValor().split(ConstantesBD.separadorSplit).length>1?campo.getValor().split(ConstantesBD.separadorSplit)[1]:campo.getValor()));
						if(!campo.getValoresOpcion().equals(""))
						{
							tempo=tempo+"    "+campo.getValoresOpcion();
						}
						texto=cmp.text(tempo).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde)).setHorizontalAlignment(HorizontalAlignment.LEFT);
						itemComponent.add(texto);
					}						
				}////
				else if (campo.getTipoHtml().equals(ConstantesCamposParametrizables.campoTipoText))
				{
					texto=cmp.text(campo.getEtiqueta()).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde)).setHorizontalAlignment(HorizontalAlignment.LEFT);
					itemComponent.add(texto);

					String tempo=campo.getValor()+(!campo.getNombreUnidad().equals("")&&!campo.getValor().equals("")?" "+campo.getNombreUnidad():"");
					texto=cmp.text(tempo).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde)).setHorizontalAlignment(HorizontalAlignment.LEFT);
					itemComponent.add(texto);
				}
				else if (campo.getTipoHtml().equals(ConstantesCamposParametrizables.campoTipoLabel))
				{
					texto=cmp.text(campo.getEtiqueta()).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde)).setHorizontalAlignment(HorizontalAlignment.LEFT);
					itemComponent.add(texto);	
				}
			}
		}////////FINALIZA LA IMPRESION DE CAMPOS
	}

//FIXME HASTA ACA DOCUMENTO SECCIONES 

	/**
	 * 
	 * @param itemComponent
	 * @param escala
	 */
	public static  void mostrarInformacionEscalas(HorizontalListBuilder itemComponent,DtoEscala escala) 
	{
		itemComponent.newRow();
		TextFieldBuilder<String> texto;

		texto=cmp.text(" ").setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde)).setHorizontalAlignment(HorizontalAlignment.LEFT);
		itemComponent.add(texto);

		texto=cmp.text(escala.getDescripcion()+"").setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde)).setHorizontalAlignment(HorizontalAlignment.LEFT);
		itemComponent.add(texto);

		texto=cmp.text("Valor").setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde)).setHorizontalAlignment(HorizontalAlignment.LEFT);
		itemComponent.add(texto);

		texto=cmp.text("Observación").setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde)).setHorizontalAlignment(HorizontalAlignment.LEFT);
		itemComponent.add(texto);
		for(int i=0;i<escala.getSecciones().size();i++)
		{
			DtoSeccionParametrizable seccion = escala.getSecciones().get(i);
			if(seccion.isVisible())
			{
				texto=cmp.text(seccion.getDescripcion()).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde)).setHorizontalAlignment(HorizontalAlignment.LEFT);
				itemComponent.newFlowRow().add(texto);
				if(seccion.numeroCamposVisibles()>0)
				{
					int contadorCampos = ConstantesBD.codigoNuncaValido;
					for(int j=0;j<seccion.getCampos().size();j++)
					{
						DtoCampoParametrizable campo = seccion.getCampos().get(j);
						if(campo.isMostrar())
						{
							dibujarCampoEscala(itemComponent,contadorCampos,campo,escala);

							contadorCampos++;
						}
					}
				}				
			}
		}//cierre de ciclo for

		itemComponent.newRow();
		texto=cmp.text("Clasificación total:").setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde)).setHorizontalAlignment(HorizontalAlignment.LEFT);
		itemComponent.add(texto);
		texto=cmp.text(escala.getTotalEscala()+"").setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde)).setHorizontalAlignment(HorizontalAlignment.LEFT);
		itemComponent.add(texto);
		texto=cmp.text("Factor Predicción: "+escala.getNombreFactorPrediccion()).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde)).setHorizontalAlignment(HorizontalAlignment.LEFT);
		itemComponent.add(texto);

		if(!escala.getObservaciones().equals(""))
		{
			itemComponent.newRow();
			texto=cmp.text("Observaciones").setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeNegrilla)).setHorizontalAlignment(HorizontalAlignment.LEFT);
			itemComponent.add(texto);
			texto=cmp.text(escala.getObservaciones()+"").setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeNegrilla)).setHorizontalAlignment(HorizontalAlignment.LEFT);
			itemComponent.add(texto);
		}
	}



	public static  void dibujarCampoEscala(HorizontalListBuilder itemComponent,int contadorCampos, DtoCampoParametrizable campo, DtoEscala escala) 
	{
		TextFieldBuilder<String> texto;
		///////dibujar una escala especifica 
		{
			if(contadorCampos==0)
			{
				itemComponent.newRow();				
			}
			texto=cmp.text(campo.getEtiqueta()).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde)).setHorizontalAlignment(HorizontalAlignment.LEFT);
			itemComponent.add(texto);	
			if(campo.getMinimo()==campo.getMaximo()) 
			{
				if(!campo.getValor().equals("")&&Utilidades.convertirADouble(campo.getValor())>0)
				{ 
					texto=cmp.text("X").setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde)).setHorizontalAlignment(HorizontalAlignment.LEFT);
					itemComponent.add(texto);
				}
			}
			else
			{
				texto=cmp.text(campo.getValor()).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde)).setHorizontalAlignment(HorizontalAlignment.LEFT);
				itemComponent.add(texto);
			}

			texto=cmp.text(campo.getObservaciones()).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde)).setHorizontalAlignment(HorizontalAlignment.LEFT);
			itemComponent.add(texto);
		}
	}



	public static  void crearComponenPediatria(HorizontalListBuilder itemComponent, DtoSeccionFija seccionFija,DtoPediatria pediatria, DtoElementoParam elemento,PersonaBasica paciente) 
	{
		TextFieldBuilder<String> texto=null;
		TextFieldBuilder<String> texto1=null;
		TextFieldBuilder<String> texto2=null;

		if(pediatria.isExisteDesarrolloPsicomotor())
		{

			if(pediatria.getDesarrollos().size()>0){
				itemComponent.newRow();

				texto=cmp.text("Desarrollo Psicomotor").setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeNegrilla)).setHorizontalAlignment(HorizontalAlignment.LEFT);
				itemComponent.add(texto);

				for(DtoDesarrolloPediatria desarrollo:pediatria.getDesarrollos())
				{
					if(desarrollo.getCodigoTipo()==ConstantesBD.codigoTipoDesarrolloPsicomotor)
					{

						if((!UtilidadTexto.isEmpty(desarrollo.getNombre().trim()))
								||(!UtilidadTexto.isEmpty(desarrollo.getEdadTexto().trim()))||
								(!UtilidadTexto.isEmpty(desarrollo.getDescripcion().trim()))){
							itemComponent.newRow();

							String tempo=""+desarrollo.getNombre()+"A la edad de "+desarrollo.getEdadTexto()+"   Descripción: "+desarrollo.getDescripcion();

							texto=cmp.text(tempo).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde)).setHorizontalAlignment(HorizontalAlignment.LEFT);
							itemComponent.add(texto);
						}
					}
				}	
			}
		}

		if(pediatria.isExisteDesarrolloLenguaje())
		{
			if(pediatria.getDesarrollos().size()>0){

				itemComponent.newRow();

				texto=cmp.text("Desarrollo Lenguaje").setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeNegrilla)).setHorizontalAlignment(HorizontalAlignment.LEFT);
				itemComponent.add(texto);

				for(DtoDesarrolloPediatria desarrollo:pediatria.getDesarrollos())
				{
					if(desarrollo.getCodigoTipo()==ConstantesBD.codigoTipoDesarrolloLenguaje)
					{
						if((!UtilidadTexto.isEmpty(desarrollo.getNombre().trim()))
								||(!UtilidadTexto.isEmpty(desarrollo.getEdadTexto().trim()))
								||(!UtilidadTexto.isEmpty(desarrollo.getDescripcion().trim()))){
							itemComponent.newRow();

							String tempo=""+desarrollo.getNombre()+"A la edad de "+desarrollo.getEdadTexto()+"   Descripción: "+desarrollo.getDescripcion();

							texto=cmp.text(tempo).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde)).setHorizontalAlignment(HorizontalAlignment.LEFT);
							itemComponent.add(texto);
						}
					}
				}
			}
		}

		if(pediatria.isExistenObservacionesDesarrollo())
		{

			if(pediatria.getObservaciones().size()>0){
				itemComponent.newRow();

				texto=cmp.text(ValoresPorDefecto.getIntegridadDominio(ConstantesIntegridadDominio.acronimoObservacionesDesarrollo)+"").setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeNegrilla)).setHorizontalAlignment(HorizontalAlignment.LEFT);
				itemComponent.add(texto);

				for(DtoObservacionesPediatria observacion:pediatria.getObservaciones())
				{
					if(observacion.getCodigoTipo().equals(ConstantesIntegridadDominio.acronimoObservacionesDesarrollo))
					{
						if((!UtilidadTexto.isEmpty(observacion.getValor().trim()))
								||(!UtilidadTexto.isEmpty(observacion.getFecha().trim()))
								||(!UtilidadTexto.isEmpty(observacion.getHora().trim()))
								||(!UtilidadTexto.isEmpty(observacion.getUsuario().getInformacionGeneralPersonalSalud().trim()))){

							itemComponent.newRow();
							String tempo=""+observacion.getValor()+" "+observacion.getFecha()+" - "+observacion.getHora()+"  "+observacion.getUsuario().getInformacionGeneralPersonalSalud();
							texto=cmp.text(tempo).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde)).setHorizontalAlignment(HorizontalAlignment.LEFT);
							itemComponent.add(texto);
						}
					}
				}	
			}
		}


		if(pediatria.isExistenSueniosHabitos())
		{

			if(pediatria.getObservaciones().size()>0){
				itemComponent.newRow();

				texto=cmp.text(ValoresPorDefecto.getIntegridadDominio(ConstantesIntegridadDominio.acronimoSueniosHabitos)+"").setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeNegrilla)).setHorizontalAlignment(HorizontalAlignment.LEFT);
				itemComponent.add(texto);

				for(DtoObservacionesPediatria observacion:pediatria.getObservaciones())
				{
					if(observacion.getCodigoTipo().equals(ConstantesIntegridadDominio.acronimoObservacionesDesarrollo))
					{
						if((!UtilidadTexto.isEmpty(observacion.getValor().trim()))
								||(!UtilidadTexto.isEmpty(observacion.getFecha().trim()))
								||(!UtilidadTexto.isEmpty(observacion.getHora().trim()))
								||(!UtilidadTexto.isEmpty(observacion.getUsuario().getInformacionGeneralPersonalSalud().trim()))){
							itemComponent.newRow();

							String tempo=""+observacion.getValor()+" "+observacion.getFecha()+" - "+observacion.getHora()+"  "+observacion.getUsuario().getInformacionGeneralPersonalSalud();

							texto=cmp.text(tempo).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde)).setHorizontalAlignment(HorizontalAlignment.LEFT);
							itemComponent.add(texto);
						}
					}
				}	
			}
		}
		if(pediatria.isExisteValoracionNutricional())
		{

			if( ((pediatria.getEdadPaciente()<2)&&((!UtilidadTexto.isEmpty(String.valueOf(pediatria.isExisteLactanciaMaterna())))||(!UtilidadTexto.isEmpty(String.valueOf(pediatria.isExisteOtrasLeches())))||(UtilidadTexto.isEmpty(String.valueOf(pediatria.isExisteAlimentacionComplementaria()))))) 
					||(pediatria.getEdadesAlimentacion().size()>0) ){
				itemComponent.newRow();

				texto=cmp.text("Valoración Nutricional").setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeNegrilla)).setHorizontalAlignment(HorizontalAlignment.LEFT);
				itemComponent.add(texto);

				if(pediatria.getEdadPaciente()<2)
				{
					if(pediatria.isExisteLactanciaMaterna())
					{
						if(!UtilidadTexto.isEmpty(String.valueOf(pediatria.getLactanciaMaterna()))){
							texto=cmp.text("Lactancia Materna "+(pediatria.getLactanciaMaterna()?"Si":"No")).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeNegrilla)).setHorizontalAlignment(HorizontalAlignment.LEFT);
							itemComponent.newRow().add(texto);
						}

					}
					if(pediatria.isExisteOtrasLeches())
					{
						if(!UtilidadTexto.isEmpty(String.valueOf(pediatria.getOtrasLeches()))){
							texto=cmp.text("Otras Leches "+(pediatria.getOtrasLeches()?"Si.       ¿Cuál?: "+pediatria.getDescripcionOtrasLeches():"No.")).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeNegrilla)).setHorizontalAlignment(HorizontalAlignment.LEFT);
							itemComponent.newRow().add(texto);
						}

					}
					if(pediatria.isExisteAlimentacionComplementaria())
					{
						if(!UtilidadTexto.isEmpty(String.valueOf(pediatria.getAlimentacionComplementaria()))){
							texto=cmp.text("Alimentación Complementaria "+(pediatria.getAlimentacionComplementaria()?"Si.       ¿Cuál?: "+pediatria.getDescripcionAlimentacionComplementaria():"No")).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeNegrilla)).setHorizontalAlignment(HorizontalAlignment.LEFT);
							itemComponent.newRow().add(texto);
						}

					}

				}//finaliza if de edad <2
				else
				{
					for(DtoEdadAlimentacionPediatria edad:pediatria.getEdadesAlimentacion())
					{
						if(!UtilidadTexto.isEmpty(edad.getNombreEdad()) && !UtilidadTexto.isEmpty(edad.getEdadTexto())){
							String tempo=""+edad.getNombreEdad()+"  A la edad de "+edad.getEdadTexto();
							texto=cmp.text(tempo).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde)).setHorizontalAlignment(HorizontalAlignment.LEFT);
							itemComponent.add(texto);
						}
					}
				}//

			}
		}//if de existe valoracion nutricional.
		if(!pediatria.getNombreEstadoNutricional().trim().equals(""))
		{
			if(!UtilidadTexto.isEmpty(pediatria.getNombreEstadoNutricional())){
				String tempo="Estado nutricional:"+pediatria.getNombreEstadoNutricional();
				texto=cmp.text(tempo).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde)).setHorizontalAlignment(HorizontalAlignment.LEFT);
				itemComponent.add(texto);
			}
		}
		if(pediatria.getEdadPaciente()<2)
		{
			if(pediatria.isExisteObservacionValNutricionalMenor2Anios())
			{
				if(validacionObservacionesPedriatia(pediatria.getObservaciones())){
					String tempo=ValoresPorDefecto.getIntegridadDominio(ConstantesIntegridadDominio.acronimoValNutricionalMenor2Anios)+" ";

					texto=cmp.text(tempo).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeNegrilla)).setHorizontalAlignment(HorizontalAlignment.LEFT);
					itemComponent.add(texto);

					for(DtoObservacionesPediatria observacion:pediatria.getObservaciones())
					{
						if(observacion.getCodigoTipo().equals(ConstantesIntegridadDominio.acronimoValNutricionalMenor2Anios))
						{
							if((!UtilidadTexto.isEmpty(observacion.getValor()))
									||(!UtilidadTexto.isEmpty(observacion.getFecha()))
									||(!UtilidadTexto.isEmpty(observacion.getHora()))
									||(!UtilidadTexto.isEmpty(observacion.getUsuario().getInformacionGeneralPersonalSalud()))){
								tempo=observacion.getValor()+" "+observacion.getFecha()+" - "+observacion.getHora()+"  "+observacion.getUsuario().getInformacionGeneralPersonalSalud();
								texto=cmp.text(tempo).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde)).setHorizontalAlignment(HorizontalAlignment.LEFT);
								itemComponent.add(texto);
							}
						}
					}
				}
			}

		}//fin del if de la edad.
		else
		{
			if(pediatria.isExisteTipoAlimentacion())
			{

				if(validacionObservacionesPedriatiaTipoAlimentacion(pediatria.getObservaciones())){
					String tempo=ValoresPorDefecto.getIntegridadDominio(ConstantesIntegridadDominio.acronimoTipoAlimentacion)+" ";

					texto=cmp.text(tempo).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeNegrilla)).setHorizontalAlignment(HorizontalAlignment.LEFT);
					itemComponent.add(texto);

					for(DtoObservacionesPediatria observacion:pediatria.getObservaciones())
					{
						if(observacion.getCodigoTipo().equals(ConstantesIntegridadDominio.acronimoTipoAlimentacion))
						{ 

							if((!UtilidadTexto.isEmpty(observacion.getValor()))
									||(!UtilidadTexto.isEmpty(observacion.getFecha()))
									||(!UtilidadTexto.isEmpty(observacion.getHora()))
									||(!UtilidadTexto.isEmpty(observacion.getUsuario().getInformacionGeneralPersonalSalud()))){
								tempo=observacion.getValor()+" "+observacion.getFecha()+" - "+observacion.getHora()+"  "+observacion.getUsuario().getInformacionGeneralPersonalSalud();
								texto=cmp.text(tempo).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde)).setHorizontalAlignment(HorizontalAlignment.LEFT);
								itemComponent.add(texto);
							}
						}
					}
				}
			}


			if(pediatria.isExisteObservacionValNutricionalMayor2Anios())
			{

				if(validacionObservacionesPedriatiaTipoNutricionMayor2Anos(pediatria.getObservaciones())){

					String tempo=ValoresPorDefecto.getIntegridadDominio(ConstantesIntegridadDominio.acronimoValNutricionalMayor2Anios)+" ";

					texto=cmp.text(tempo).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeNegrilla)).setHorizontalAlignment(HorizontalAlignment.LEFT);
					itemComponent.add(texto);

					for(DtoObservacionesPediatria observacion:pediatria.getObservaciones())
					{
						if(observacion.getCodigoTipo().equals(ConstantesIntegridadDominio.acronimoValNutricionalMayor2Anios))
						{
							if((!UtilidadTexto.isEmpty(observacion.getValor()))
									||(!UtilidadTexto.isEmpty(observacion.getFecha()))
									||(!UtilidadTexto.isEmpty(observacion.getHora()))
									||(!UtilidadTexto.isEmpty(observacion.getUsuario().getInformacionGeneralPersonalSalud()))){
								tempo=observacion.getValor()+" "+observacion.getFecha()+" - "+observacion.getHora()+"  "+observacion.getUsuario().getInformacionGeneralPersonalSalud();
								texto=cmp.text(tempo).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde)).setHorizontalAlignment(HorizontalAlignment.LEFT);
								itemComponent.add(texto);
							}
						} 
					}
				}
			}
		}
	}


	/**
	 * Se valida si existe observaciones pedriatia para mayor de 2 años 
	 * @param observacion
	 * @return Si existe nutrición mayor a 2 años
	 */
	public static  Boolean validacionObservacionesPedriatiaTipoNutricionMayor2Anos(ArrayList<DtoObservacionesPediatria> observacion){
		for (DtoObservacionesPediatria obsTmp : observacion) {
			if(obsTmp.getCodigoTipo().equals(ConstantesIntegridadDominio.acronimoValNutricionalMayor2Anios)){
				return true;
			}
		}
		return false;
	}

	/**
	 * Se valida si existe observaciones pedriatia para menor de 2 años 
	 * @param observacion
	 * @return Si existe nutrición menor a 2 años
	 */
	public static  Boolean validacionObservacionesPedriatiaTipoAlimentacion(ArrayList<DtoObservacionesPediatria> observacion){
		for (DtoObservacionesPediatria obsTmp : observacion) {
			if(obsTmp.getCodigoTipo().equals(ConstantesIntegridadDominio.acronimoTipoAlimentacion)){
				return true;
			}
		}
		return false;
	}

	/**
	 * Se valida si existe observaciones pedriatia para menor de 2 años 
	 * @param observacion
	 * @return Si existe nutrición menor a 2 años
	 */
	public static  Boolean validacionObservacionesPedriatia(ArrayList<DtoObservacionesPediatria> observacion){
		for (DtoObservacionesPediatria obsTmp : observacion) {
			if(obsTmp.getCodigoTipo().equals(ConstantesIntegridadDominio.acronimoValNutricionalMenor2Anios)){
				return true;
			}
		}
		return false;
	}




	public static  void crearComponenOftalmologia(HorizontalListBuilder itemComponent, DtoSeccionFija seccionFija,DtoOftalmologia oftalmologia, DtoElementoParam elemento,PersonaBasica paciente) 
	{
		TextFieldBuilder<String> texto=null;
		TextFieldBuilder<String> texto1=null;
		TextFieldBuilder<String> texto2=null;
		TextFieldBuilder<String> titulo=cmp.text("SECCION OFTALMOLOGÍA").setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeNegrilla)).setHorizontalAlignment(HorizontalAlignment.LEFT);
		itemComponent.newRow().add(titulo);

		//se valida el tamaño de los mapas para ser pintados 
		if(oftalmologia.getNumRegistroSintomas()>0 || oftalmologia.getNumRegistroOtrosSintomas()>0)
		{
			itemComponent.newRow();

			texto=cmp.text("Tipo Síntoma").setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeNegrilla)).setHorizontalAlignment(HorizontalAlignment.LEFT);
			itemComponent.add(texto);

			texto=cmp.text("Síntoma").setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeNegrilla)).setHorizontalAlignment(HorizontalAlignment.LEFT);
			itemComponent.add(texto);

			texto=cmp.text("Localización").setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeNegrilla)).setHorizontalAlignment(HorizontalAlignment.LEFT);
			itemComponent.add(texto);

			texto=cmp.text("Severidad").setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeNegrilla)).setHorizontalAlignment(HorizontalAlignment.LEFT);
			itemComponent.add(texto);

			for(int i=0;i<oftalmologia.getNumRegistroSintomas();i++)
			{
				itemComponent.newRow();

				texto=cmp.text(oftalmologia.getRegistroSintomas("nombreTipoSintoma_"+i)+"").setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde)).setHorizontalAlignment(HorizontalAlignment.LEFT);
				itemComponent.add(texto);

				texto=cmp.text(oftalmologia.getRegistroSintomas("nombreSintoma_"+i)+"").setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde)).setHorizontalAlignment(HorizontalAlignment.LEFT);
				itemComponent.add(texto);

				texto=cmp.text(oftalmologia.getRegistroSintomas("nombreLocalizacion_"+i)+"").setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde)).setHorizontalAlignment(HorizontalAlignment.LEFT);
				itemComponent.add(texto);

				texto=cmp.text(oftalmologia.getRegistroSintomas("nombreSeveridad_"+i)+"").setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde)).setHorizontalAlignment(HorizontalAlignment.LEFT);
				itemComponent.add(texto);
			}
			for(int i=0;i<oftalmologia.getNumRegistroOtrosSintomas();i++)
			{
				itemComponent.newRow();

				texto=cmp.text("Otro").setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde)).setHorizontalAlignment(HorizontalAlignment.LEFT);
				itemComponent.add(texto);

				texto=cmp.text(oftalmologia.getRegistroOtrosSintomas("nombreSintoma_"+i)+"").setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde)).setHorizontalAlignment(HorizontalAlignment.LEFT);
				itemComponent.add(texto);

				texto=cmp.text(oftalmologia.getRegistroOtrosSintomas("nombreLocalizacion_"+i)+"").setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde)).setHorizontalAlignment(HorizontalAlignment.LEFT);
				itemComponent.add(texto);

				texto=cmp.text(oftalmologia.getRegistroOtrosSintomas("nombreSeveridad_"+i)+"").setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde)).setHorizontalAlignment(HorizontalAlignment.LEFT);
				itemComponent.add(texto);
			}			
		}
		if(oftalmologia.isExisteExamenOftalmologico())
		{

			if(oftalmologia.isExisteAgudezaVisual())
			{
				titulo=cmp.text("Examen Oftalmológico").setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeNegrilla)).setHorizontalAlignment(HorizontalAlignment.LEFT);
				itemComponent.newRow().add(titulo);

				itemComponent.newRow();

				texto=cmp.text("AV").setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde)).setHorizontalAlignment(HorizontalAlignment.LEFT);
				itemComponent.add(texto);

				texto=cmp.text("s.c").setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde)).setHorizontalAlignment(HorizontalAlignment.LEFT);
				itemComponent.add(texto);

				if((!UtilidadTexto.isEmpty(oftalmologia.getNombreOjoDerechoLejosSinCorrecion().trim()))
						&&(!UtilidadTexto.isEmpty(oftalmologia.getNombreOjoIzquierdoLejosSinCorrecion().trim()))){
					texto1=cmp.text("OD "+oftalmologia.getNombreOjoDerechoLejosSinCorrecion()).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde)).setHorizontalAlignment(HorizontalAlignment.LEFT);
					texto2=cmp.text("OS "+oftalmologia.getNombreOjoIzquierdoLejosSinCorrecion()).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde)).setHorizontalAlignment(HorizontalAlignment.LEFT);
					itemComponent.add(cmp.verticalList(texto1,texto2));
				}

				texto=cmp.text("c.c").setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde)).setHorizontalAlignment(HorizontalAlignment.LEFT);
				itemComponent.add(texto);

				if((!UtilidadTexto.isEmpty(oftalmologia.getNombreOjoDerechoLejosConCorrecion().trim()))
						&&(!UtilidadTexto.isEmpty(oftalmologia.getNombreOjoIzquierdoLejosConCorrecion().trim()))){
					texto1=cmp.text("OD "+oftalmologia.getNombreOjoDerechoLejosConCorrecion()).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde)).setHorizontalAlignment(HorizontalAlignment.LEFT);
					texto2=cmp.text("OS "+oftalmologia.getNombreOjoIzquierdoLejosConCorrecion()).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde)).setHorizontalAlignment(HorizontalAlignment.LEFT);
					itemComponent.add(cmp.verticalList(texto1,texto2));
				}

				texto=cmp.text("Cerca s.c").setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde)).setHorizontalAlignment(HorizontalAlignment.LEFT);
				itemComponent.add(texto);


				if(!UtilidadTexto.isEmpty(oftalmologia.getNombreOjoDerechoCercaSinCorrecion().trim()) 
						&& !UtilidadTexto.isEmpty(oftalmologia.getNombreOjoIzquierdoCercaSinCorrecion().trim())){
					texto1=cmp.text("OD "+oftalmologia.getNombreOjoDerechoCercaSinCorrecion()).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde)).setHorizontalAlignment(HorizontalAlignment.LEFT);
					texto2=cmp.text("OS "+oftalmologia.getNombreOjoIzquierdoCercaSinCorrecion()).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde)).setHorizontalAlignment(HorizontalAlignment.LEFT);
					itemComponent.add(cmp.verticalList(texto1,texto2));
				}

				texto=cmp.text("Cerca c.c").setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde)).setHorizontalAlignment(HorizontalAlignment.LEFT);
				itemComponent.add(texto);

				if(!UtilidadTexto.isEmpty(oftalmologia.getNombreOjoDerechoCercaConCorrecion()) 
						&& !UtilidadTexto.isEmpty(oftalmologia.getNombreOjoIzquierdoCercaConCorrecion())){
					texto1=cmp.text("OD "+oftalmologia.getNombreOjoDerechoCercaConCorrecion()).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde)).setHorizontalAlignment(HorizontalAlignment.LEFT);
					texto2=cmp.text("OS "+oftalmologia.getNombreOjoIzquierdoCercaConCorrecion()).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde)).setHorizontalAlignment(HorizontalAlignment.LEFT);
					itemComponent.add(cmp.verticalList(texto1,texto2));
				}
				itemComponent.newRow();


			}
		}
		if(oftalmologia.isExisteRefraccion())
		{
			itemComponent.newRow();

			texto=cmp.text("REFRACCIÓN SK").setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde)).setHorizontalAlignment(HorizontalAlignment.LEFT);
			itemComponent.add(texto);

			if((!UtilidadTexto.isEmpty(oftalmologia.getOjoDerechoSK().trim()))
					&&(!UtilidadTexto.isEmpty(oftalmologia.getOjoIzquierdoSK().trim()))){
				texto1=cmp.text("OD "+oftalmologia.getOjoDerechoSK()).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde)).setHorizontalAlignment(HorizontalAlignment.LEFT);
				texto2=cmp.text("OS "+oftalmologia.getOjoIzquierdoSK()).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde)).setHorizontalAlignment(HorizontalAlignment.LEFT);
				itemComponent.add(cmp.verticalList(texto1,texto2));
			}


			texto=cmp.text("SK Ciclo").setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde)).setHorizontalAlignment(HorizontalAlignment.LEFT);
			itemComponent.add(texto);

			if((!UtilidadTexto.isEmpty(oftalmologia.getOjoDerechoSKCiclo().trim()))
					&&(!UtilidadTexto.isEmpty(oftalmologia.getOjoIzquierdoSKCiclo().trim()))){
				texto1=cmp.text("OD "+oftalmologia.getOjoDerechoSKCiclo()).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde)).setHorizontalAlignment(HorizontalAlignment.LEFT);
				texto2=cmp.text("OS "+oftalmologia.getOjoIzquierdoSKCiclo()).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde)).setHorizontalAlignment(HorizontalAlignment.LEFT);
				itemComponent.add(cmp.verticalList(texto1,texto2));
			}

			texto=cmp.text("SK Subj").setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde)).setHorizontalAlignment(HorizontalAlignment.LEFT);
			itemComponent.add(texto);

			if((!UtilidadTexto.isEmpty(oftalmologia.getOjoDerechoSKSubj()))
					&&(!UtilidadTexto.isEmpty(oftalmologia.getOjoIzquierdoSKSubj()))){
				texto1=cmp.text("OD "+oftalmologia.getOjoDerechoSKSubj()).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde)).setHorizontalAlignment(HorizontalAlignment.LEFT);
				texto2=cmp.text("OS "+oftalmologia.getOjoIzquierdoSKSubj()).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde)).setHorizontalAlignment(HorizontalAlignment.LEFT);
				itemComponent.add(cmp.verticalList(texto1,texto2));	
			}

		}
		if(oftalmologia.isExisteAddDip())
		{
			if((!UtilidadTexto.isEmpty(oftalmologia.getAdd().trim()))
					||(!UtilidadTexto.isEmpty(oftalmologia.getDip().trim()))){

				itemComponent.newRow();

				if(!UtilidadTexto.isEmpty(oftalmologia.getAdd().trim())){
					texto=cmp.text("Add "+oftalmologia.getAdd()).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde)).setHorizontalAlignment(HorizontalAlignment.LEFT);
					itemComponent.add(texto);
				}

				if(!UtilidadTexto.isEmpty(oftalmologia.getDip().trim())){
					texto=cmp.text("DIP "+oftalmologia.getDip()).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde)).setHorizontalAlignment(HorizontalAlignment.LEFT);
					itemComponent.add(texto);	
				}
			}

		}
		if(oftalmologia.isExisteQueratometria())
		{

			if((!UtilidadTexto.isEmpty(oftalmologia.getOjoDerechoQueratometria().trim()))
					||(!UtilidadTexto.isEmpty(oftalmologia.getOjoIzquierdoQueratometria().trim()))){
				itemComponent.newRow();

				texto=cmp.text("QUERATOMETRÍA").setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde)).setHorizontalAlignment(HorizontalAlignment.LEFT);
				itemComponent.add(texto);

				if(!UtilidadTexto.isEmpty(oftalmologia.getOjoDerechoQueratometria().trim())){
					texto=cmp.text("OD "+oftalmologia.getOjoDerechoQueratometria()).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde)).setHorizontalAlignment(HorizontalAlignment.LEFT);
					itemComponent.add(texto);	
				}

				if(!UtilidadTexto.isEmpty(oftalmologia.getOjoIzquierdoQueratometria().trim())){
					texto=cmp.text("Os "+oftalmologia.getOjoIzquierdoQueratometria()).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde)).setHorizontalAlignment(HorizontalAlignment.LEFT);
					itemComponent.add(texto);
				}
			}
		}
		if(oftalmologia.isExisteTonometria())
		{

			if((!UtilidadTexto.isEmpty(oftalmologia.getOjoDerechoTonometria().trim()))
					||(!UtilidadTexto.isEmpty(oftalmologia.getOjoIzquierdoTonometria().trim()))
					||(!UtilidadTexto.isEmpty(oftalmologia.getNombreEquipoTonometria().trim()))){

				itemComponent.newRow();

				texto=cmp.text("TONOMETRÍA").setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde)).setHorizontalAlignment(HorizontalAlignment.LEFT);
				itemComponent.add(texto);

				if(!UtilidadTexto.isEmpty(oftalmologia.getOjoDerechoTonometria().trim())){
					texto=cmp.text("OD "+oftalmologia.getOjoDerechoTonometria()).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde)).setHorizontalAlignment(HorizontalAlignment.LEFT);
					itemComponent.add(texto);	
				}

				if(!UtilidadTexto.isEmpty(oftalmologia.getOjoIzquierdoTonometria().trim())){
					texto=cmp.text("Os "+oftalmologia.getOjoIzquierdoTonometria()).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde)).setHorizontalAlignment(HorizontalAlignment.LEFT);
					itemComponent.add(texto);
				}

				if(!UtilidadTexto.isEmpty(oftalmologia.getNombreEquipoTonometria().trim())){
					itemComponent.newRow();
					texto=cmp.text("Equipo "+oftalmologia.getNombreEquipoTonometria()).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde)).setHorizontalAlignment(HorizontalAlignment.LEFT);
					itemComponent.add(texto);
				}
			}
		}
	}



	/**
	 * 
	 * @param itemComponent
	 * @param dto
	 * @param seccionFija
	 * @param mundoValoracion
	 * @param elemento
	 * @param paciente
	 */
	public static  void crearComponenGinecologia(HorizontalListBuilder itemComponent, DtoSeccionFija seccionFija,DtoHistoriaMenstrual historiaMenstrual, DtoElementoParam elemento,PersonaBasica paciente) 
	{
		TextFieldBuilder<String> texto=null;
		TextFieldBuilder<String> titulo=cmp.text("HISTORIA MENSTRUAL").setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeNegrilla)).setHorizontalAlignment(HorizontalAlignment.LEFT);
		itemComponent.newRow().add(titulo);
		if(historiaMenstrual.isExisteEdadMenopausia() && validacionExisteEdadMenopausia(historiaMenstrual) )
		{

			itemComponent.newRow();

			if(!UtilidadTexto.isEmpty(historiaMenstrual.getNombreEdadMenarquia().trim())){
				texto=cmp.text("Edad menarquia: " + historiaMenstrual.getNombreEdadMenarquia()+" años.").setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde)).setHorizontalAlignment(HorizontalAlignment.LEFT);
				itemComponent.add(texto);
			}

			if(!UtilidadTexto.isEmpty(historiaMenstrual.getOtraEdadMenarquia().trim())){
				texto=cmp.text("Cuál: " + historiaMenstrual.getOtraEdadMenarquia()).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde)).setHorizontalAlignment(HorizontalAlignment.LEFT);
				itemComponent.add(texto);
			}

			if(!UtilidadTexto.isEmpty(historiaMenstrual.getNombreEdadMenopausia().trim())){
				texto=cmp.text("Edad menopausia: " + historiaMenstrual.getNombreEdadMenopausia()+" años.").setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde)).setHorizontalAlignment(HorizontalAlignment.LEFT);
				itemComponent.add(texto);
			}

			if(!UtilidadTexto.isEmpty(historiaMenstrual.getOtraEdadMenopausia().trim())){
				texto=cmp.text("Cuál: " + historiaMenstrual.getOtraEdadMenopausia()).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde)).setHorizontalAlignment(HorizontalAlignment.LEFT);
				itemComponent.add(texto);
			}
		}

		if(historiaMenstrual.isExisteSubseccionMenstrual() && ExisteSubseccionMenstrual(historiaMenstrual))
		{
			itemComponent.newRow();

			if(!UtilidadTexto.isEmpty(historiaMenstrual.getCicloMenstrual().trim())){
				texto=cmp.text("Ciclo menstrual: " + historiaMenstrual.getCicloMenstrual()+" días").setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde)).setHorizontalAlignment(HorizontalAlignment.LEFT);
				itemComponent.add(texto);
			}

			if(!UtilidadTexto.isEmpty(historiaMenstrual.getDuracionMenstruacion().trim())){
				texto=cmp.text("Duración: " + historiaMenstrual.getDuracionMenstruacion()+" días").setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde)).setHorizontalAlignment(HorizontalAlignment.LEFT);
				itemComponent.add(texto);
			}

			if(!UtilidadTexto.isEmpty(historiaMenstrual.getFechaUltimaRegla().trim())){
				texto=cmp.text("FUR (dd/mm/aaaa): " + historiaMenstrual.getFechaUltimaRegla()).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde)).setHorizontalAlignment(HorizontalAlignment.LEFT);
				itemComponent.add(texto);
			}

			if(!UtilidadTexto.isEmpty(historiaMenstrual.getDolorMenstruacion().toString())){
				texto=cmp.text("Dolor: " + (historiaMenstrual.getDolorMenstruacion()?"Si":"No")).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde)).setHorizontalAlignment(HorizontalAlignment.LEFT);
				itemComponent.add(texto);
			}
		}

		if(!historiaMenstrual.getNombreConceptoMenstruacion().trim().equals("") && historiaMenstrual.getCodigoConceptoMenstruacion()>0)
		{
			if(!UtilidadTexto.isEmpty(historiaMenstrual.getNombreConceptoMenstruacion().trim())){			
				itemComponent.newRow();
				texto=cmp.text(historiaMenstrual.getNombreConceptoMenstruacion()+"").setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde)).setHorizontalAlignment(HorizontalAlignment.LEFT);
				itemComponent.add(texto);
			}
		}

		if(!historiaMenstrual.getObservacionesMenstruales().trim().equals(""))
		{
			if(!UtilidadTexto.isEmpty(historiaMenstrual.getObservacionesMenstruales().trim())){

				itemComponent.newRow();

				texto=cmp.text("Observaciones características menstruación").setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeNegrilla)).setHorizontalAlignment(HorizontalAlignment.LEFT);
				itemComponent.add(texto);

				texto=cmp.text(historiaMenstrual.getObservacionesMenstruales()+"").setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde)).setHorizontalAlignment(HorizontalAlignment.LEFT);
				itemComponent.newRow().add(texto);
			}
		}
	}


	/**
	 * Validad si en alguno de los campos a mostrar contiene información
	 * @param historiaMenstrual
	 * @return si hay información para esta seccion 
	 */
	public static  Boolean validacionExisteEdadMenopausia(DtoHistoriaMenstrual historiaMenstrual){
		if((!UtilidadTexto.isEmpty(historiaMenstrual.getNombreEdadMenarquia().trim()))
				||(!UtilidadTexto.isEmpty(historiaMenstrual.getOtraEdadMenarquia().trim()))
				||(!UtilidadTexto.isEmpty(historiaMenstrual.getNombreEdadMenopausia().trim()))
				||(!UtilidadTexto.isEmpty(historiaMenstrual.getOtraEdadMenopausia().trim()))){
			return true;
		}else{
			return false;
		}
	}



	/**
	 * Validad si en alguno de los campos a mostrar contiene información
	 * @param historiaMenstrual
	 * @return si hay información para esta seccion 
	 */
	public static Boolean ExisteSubseccionMenstrual(DtoHistoriaMenstrual historiaMenstrual){

		if((!UtilidadTexto.isEmpty(historiaMenstrual.getCicloMenstrual().trim()))
				||(!UtilidadTexto.isEmpty(historiaMenstrual.getDuracionMenstruacion().trim()))
				||(!UtilidadTexto.isEmpty( historiaMenstrual.getFechaUltimaRegla().trim()))
				||(!UtilidadTexto.isEmpty(historiaMenstrual.getDolorMenstruacion().toString()))){
			return true;
		}else{
			return false;
		}
	}


	/**
	 * 
	 * @param itemComponent
	 * @param dto
	 * @param seccionFija
	 * @param mundoValoracion
	 * @param elemento
	 * @param paciente
	 */
	public static  void crearComponenteSignosVitales(HorizontalListBuilder itemComponent, DtoSeccionFija seccionFija,ArrayList<SignoVital> signosVitalesArray, DtoElementoParam elemento,PersonaBasica paciente) 
	{
		if(signosVitalesArray.size()>0){
			itemComponent.newRow();
			for(SignoVital signo:signosVitalesArray)
			{
				if(!UtilidadTexto.isEmpty(signo.getValorSignoVital().trim()) && !UtilidadTexto.isEmpty(signo.getNombre().trim()) ){
					TextFieldBuilder<String> texto1=cmp.text(signo.getNombre()+"\n"+signo.getUnidadMedida()).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloNegrillaL).underline()).setHorizontalAlignment(HorizontalAlignment.CENTER);
					TextFieldBuilder<String> texto2=cmp.text(signo.getValorSignoVital()).setHorizontalAlignment(HorizontalAlignment.CENTER);
					itemComponent.add(cmp.verticalList(texto1,texto2)).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde));
				}
			}
		}
	}
	
	
	/**
	 * Pinta la seccion de curvas de crecimiento en el PDF
	 * @param itemComponent
	 * @param curvas
	 */
	private static void crearComponenteCurvaCrecimiento(HorizontalListBuilder itemComponent, List<HistoricoImagenPlantillaDto> curvas)
	{
		for (int i = 0; i < curvas.size(); i++) 
		{
			
			String dImg = PathSolver.getWEBINFPath() + "/" + System.getProperty("directorioImagenes");
			
			if(System.getProperty("os.name").toUpperCase().indexOf("WINDOWS") >= 0){
				dImg = PathSolver.getWEBINFPath() + "\\" + System.getProperty("directorioImagenes");
			}
			
			FileInputStream imgErr = null;
			FileInputStream imgBla = null;
			FileInputStream imgIzq;
			FileInputStream imgDer;
			FileInputStream imgCur;
			
			try 
			{
				imgErr = new FileInputStream(dImg + "img_cancelar.png");
				imgBla = new FileInputStream(dImg + "spacer.gif");
			}
			catch (FileNotFoundException e)
			{
				e.printStackTrace();
			}
			
			try 
			{
				imgIzq = new FileInputStream(dImg + curvas.get(i).getImagenIzquierda());
			}
			catch (FileNotFoundException e)
			{
				imgIzq = imgBla;
			}
			
			try 
			{
				imgDer = new FileInputStream(dImg + curvas.get(i).getImagenDerecha());
			}
			catch (FileNotFoundException e)
			{
				imgDer = imgBla;
			}
			
			try 
			{
				imgCur = new FileInputStream(dImg + curvas.get(i).getUrlImagen());
			}
			catch (FileNotFoundException e)
			{
				imgCur = imgErr;
			}
			
			
			itemComponent.newRow();
			itemComponent.add
			(
				cmp.verticalList
				(
					cmp.text(curvas.get(i).getTituloGrafica() + " " + curvas.get(i).getEdadInicial() + " - " + curvas.get(i).getEdadFinal() + " meses").setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde))
				)
				.add
				(
					cmp.horizontalList	
					(
						cmp.image(imgIzq).setWidth(100).setHeight(50).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde)),
						cmp.verticalList
						(
							cmp.text(curvas.get(i).getTituloGrafica()).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde)),
							cmp.text(curvas.get(i).getDescripcion()).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde))
						),
						cmp.image(imgDer).setWidth(100).setHeight(50).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde))
					)
				)
				.add
				(
					cmp.image(imgCur).setWidth(700).setHeight(500).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBorde))
				)
			);
		}
	}

	/**
	 * 
	 * @param dto
	 * @param seccionValor
	 * @param mundoValoracion
	 * @param paciente
	 * @return
	 */
	public static  ArrayList<HorizontalListBuilder> seccionesValorParametrizables( DtoSeccionParametrizable seccion, PersonaBasica paciente) 
	{
		ArrayList<HorizontalListBuilder> itemComponentArray=new ArrayList<HorizontalListBuilder>();

		TextFieldBuilder<String> texto;
		TextFieldBuilder<String> titulo;
		String tempo="";

		HorizontalListBuilder itemComponent=null;

		titulo=cmp.text((seccion.getDescripcion()+"").toUpperCase()).setStyle(stl.style(EstilosReportesDinamicosHistoriaClinica.estiloBordeNegrilla).setHorizontalAlignment(HorizontalAlignment.LEFT));
		itemComponent=cmp.horizontalList(titulo);

		mostrarInformacionSecciones(itemComponent,seccion);


		if(itemComponent!=null)
			itemComponentArray.add(cmp.horizontalList(itemComponent));

		return itemComponentArray;
	}




}
