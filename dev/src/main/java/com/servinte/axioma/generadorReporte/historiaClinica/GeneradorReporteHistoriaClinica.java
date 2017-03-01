package com.servinte.axioma.generadorReporte.historiaClinica;

import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;
import static net.sf.dynamicreports.report.builder.DynamicReports.exp;
import static net.sf.dynamicreports.report.builder.DynamicReports.report;
import static net.sf.dynamicreports.report.builder.DynamicReports.stl;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.builder.component.VerticalListBuilder;
import net.sf.dynamicreports.report.constant.SplitType;

import org.apache.log4j.Logger;
import org.apache.struts.util.MessageResources;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.historiaClinica.UtilidadesHistoriaClinica;

import com.princetonsa.dao.sqlbase.SqlBaseResumenAtencionesDao;
import com.princetonsa.dto.manejoPaciente.DtoObservacionesGeneralesOrdenesMedicas;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.Paciente;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.historiaClinica.ImpresionResumenAtenciones;
import com.princetonsa.mundo.resumenAtenciones.ResumenAtenciones;
import com.princetonsa.mundo.solicitudes.Solicitud;
import com.servinte.axioma.generadorReporteHistoriaClinica.comun.IConstantesReporteHistoriaClinica;

public class GeneradorReporteHistoriaClinica {

	/**
	 * Objeto para manejar los logs de esta clase
	 */
	public static Logger logger = Logger.getLogger(GeneradorReporteHistoriaClinica.class);


	/**
	 * Mensajes parametrizados de los reportes 
	 */
	private static MessageResources messageResource = MessageResources.getMessageResources("com.servinte.mensajes.historiaclinica.impresionHistoriaClinica");

	/**
	 * Constructor de clase 
	 */
	public GeneradorReporteHistoriaClinica() {
	}


	/**
	 * Metodo que contiene los subreportes que forman la historia clínica y que asigna plantillas y estilos generales 
	 * @param params
	 * @param dtoResultado
	 * @param filtro
	 * @param encabezadoPaciente
	 * @param usuario
	 * @param paciente
	 * @param institucionActual
	 * @param idIngreso
	 * @return Reporte Global con subreportes que forman la historia clínica
	 */
	public JasperReportBuilder buildReportFormatoHc(
			Map<String, String> params,
			DtoImpresionHistoriaClinica dtoResultado,
			DtoFiltroImpresionHistoriaClinica filtro, HashMap encabezadoPaciente, UsuarioBasico usuario,
			PersonaBasica paciente, InstitucionBasica institucionActual,String idIngreso,Boolean imprimirSoloNotasAclaratorias,Boolean primerIngreso,Boolean varios) {



		//Inicialización del reporte
		JasperReportBuilder reportFormatoHc = report();
		try{
			//se cargan parametros del reporte de la parte del encabezado
			params=datosUsuario(encabezadoPaciente, dtoResultado, usuario, paciente,institucionActual,idIngreso);


			//Instancia de la clase que genera el diseño del reporte
			GeneradorDisenioReporteHistoriaClinica disenio = new GeneradorDisenioReporteHistoriaClinica();

			//Instancia de encabezado para la primera página
			SubReporteEncabezadoInformacionBasica subReporteEncabezadoInformacionBasica= new SubReporteEncabezadoInformacionBasica();

			//Instancia de encabezado para pagina 2 en adelante 
			GeneradorSubReporteEncabezadoFormatoSinPrimeraPagina generadorSubReporteEncabezadoFormatoSinPrimeraPagina = new GeneradorSubReporteEncabezadoFormatoSinPrimeraPagina();


			//set de encabezado para página 2 en adelante
			VerticalListBuilder pageHeader = cmp.verticalList(
					cmp.verticalList(
							cmp.subreport(generadorSubReporteEncabezadoFormatoSinPrimeraPagina.crearComponenteEncabezadoFormatoSinPrimeraPagina(params)))
			)
			.setPrintWhenExpression(exp.printNotInFirstPage());

			// adicion de template a reporte , margenes y encabezado de  primera página
			reportFormatoHc
			.setTemplate(disenio.crearPlantillaReporte()) 
			.setPageMargin(disenio.crearMagenesReporte())
			.pageHeader(pageHeader)
			.title(cmp.verticalList(
					cmp.verticalList(disenio.crearComponenteEncabezadoFormatoA(params),
							cmp.subreport(subReporteEncabezadoInformacionBasica.crearEncabezadoInformacionbasica(params)))
			));

			if(!imprimirSoloNotasAclaratorias){
				//Siempre se muestran de primeras los Antecedentes varios
				if (dtoResultado.getImprimirAntecedentes()) {
					//Instancia de generacion subreporte
					GeneradorSubReporteAntecentesVarios generadorSubReporteAntecentesVarios = new GeneradorSubReporteAntecentesVarios();

					//Generacion y adicion de subreporte al summary global
					reportFormatoHc.summary(cmp.subreport( generadorSubReporteAntecentesVarios.otrosAntecedentes(dtoResultado, usuario, paciente)));

					//Separador de reporte 
					reportFormatoHc.summary(cmp.text("").setHeight(5));
				}

				//hermorhu - MT6479
				//
				boolean observHojaNeurologicaSel = false;
				
				//se obtienen lista de elementos a agregar al reporte en orden cronológico
				ArrayList <DtoResultadoImpresionHistoriaClinica> resultado=dtoResultado.getResultadoSeccionEvolucion();
				for(int i=0;i<resultado.size();i++)
				{
					//dependiendo al if que entre se adiciona el subreporte del servicio en orden cornológico
					DtoResultadoImpresionHistoriaClinica dto=(DtoResultadoImpresionHistoriaClinica)resultado.get(i);

					if(dto.getCodigoTipoEvolucion()==ConstantesImpresionHistoriaClinica.tipoEvolucionValoracionUrgencias)
					{
						//reporte de valoracion de Urgencias
						GeneradorSubReporteValoracion generador=new GeneradorSubReporteValoracion(); 
						//List<JasperReportBuilder> subRValUrges = 
								generador.generarReporte(dto.getCodigoPk(),filtro,usuario,paciente,ConstantesBD.codigoViaIngresoUrgencias,reportFormatoHc, true);
						/*if(subRValUrges != null && !subRValUrges.isEmpty()){

							//se recorren lista de subreportes y se adicionan al summary del reporte global 
							for(JasperReportBuilder subRValUrge : subRValUrges){ 
								reportFormatoHc.summary(cmp.subreport(subRValUrge));
							}

							if(subRValUrges.size()>0){
								//espacio entre valoraciones 
								reportFormatoHc.summary(cmp.text("").setHeight(8));
							}
						}*/
					}
					else if(dto.getCodigoTipoEvolucion()==ConstantesImpresionHistoriaClinica.tipoEvolucionValoracionHospitalizacion)
					{
						ResumenAtenciones resumenAtencionesMundo = new ResumenAtenciones();
						
						//MT-5571
						if(!SqlBaseResumenAtencionesDao.valoracionTieneInterpretacionAuto(null,dto.getCodigoPk()))
						{
							//reporte de valoracion de Hospitalización 
							GeneradorSubReporteValoracion generador=new GeneradorSubReporteValoracion();
							//List<JasperReportBuilder> subRValHospital = 
									generador.generarReporte(dto.getCodigoPk(),filtro,usuario,paciente,ConstantesBD.codigoViaIngresoHospitalizacion,reportFormatoHc, true);
							/*if(subRValHospital != null && !subRValHospital.isEmpty()){
	
								//se recorren lista de subreportes y se adicionan al summary del reporte global 
								for(JasperReportBuilder subRValHosp : subRValHospital){ 
									reportFormatoHc.summary(cmp.subreport(subRValHosp));
								}
	
								if(subRValHospital.size()>0){
									//espacio entre valoraciones 
									reportFormatoHc.summary(cmp.text("").setHeight(8));
								}
							}*/
						} else {
							if(!resumenAtencionesMundo.numeroObservacionesSolicitudesIgualesValUrgHosp(idIngreso)){
								//reporte de valoracion de Hospitalización 
								GeneradorSubReporteValoracion generador=new GeneradorSubReporteValoracion();
								//List<JasperReportBuilder> subRValHospital = 
										generador.generarReporte(dto.getCodigoPk(),filtro,usuario,paciente,ConstantesBD.codigoViaIngresoHospitalizacion,reportFormatoHc, false);
								/*if(subRValHospital != null && !subRValHospital.isEmpty()){
		
									//se recorren lista de subreportes y se adicionan al summary del reporte global 
									for(JasperReportBuilder subRValHosp : subRValHospital){ 
										reportFormatoHc.summary(cmp.subreport(subRValHosp));
									}
		
									if(subRValHospital.size()>0){
										//espacio entre valoraciones 
										reportFormatoHc.summary(cmp.text("").setHeight(8));
									}
								}*/
							}
						}
					}
					else if(dto.getCodigoTipoEvolucion()==ConstantesImpresionHistoriaClinica.tipoEvolucionEvoluciones)
					{
						//reporte de evoluciones
						GeneradorSubReporteEvoluciones generador=new GeneradorSubReporteEvoluciones(); 
						//List<JasperReportBuilder> subREvoluciones = 
								generador.generarReporte(usuario,paciente,dto.getCodigoPk(),reportFormatoHc);
						/*if(subREvoluciones != null && !subREvoluciones.isEmpty()){

							//se recorren lista de subreportes y se adicionan al summary del reporte global 
							for(JasperReportBuilder subREvo : subREvoluciones){ 
								reportFormatoHc.summary(cmp.subreport(subREvo));
							}

							//espacio entre subreportes 
							reportFormatoHc.summary(cmp.text("").setHeight(5));
						}*/

					}
					else if(dto.getCodigoTipoEvolucion()==ConstantesImpresionHistoriaClinica.tipoEvolucionRespuestaInterpretacionInterconsulta)
					{
						//reporte de interprestacion
						GeneradorSubReporteRespuestaInterconsultas generador=new GeneradorSubReporteRespuestaInterconsultas(); 
						//List<ComponentBuilder> subRInterpres = 
								generador.generarReporte(dto.getCodigoPk(),filtro,usuario,paciente, reportFormatoHc);
						/*if(subRInterpres != null && !subRInterpres.isEmpty()){

							//se recorren lista de subreportes y se adicionan al summary del reporte global 
							for(ComponentBuilder subRInterp : subRInterpres){ 
								reportFormatoHc.summary(subRInterp);
							} 
						}*/
						//IMPRIMIR LA intepretacion. INTERCONSULTA
						String interpretacion=Solicitud.obtenerInterpretacionSolicitud(dto.getCodigoPk());
						if(!UtilidadTexto.isEmpty(interpretacion+""))
						{
							//seccion final del reporte 
							GeneradorSubReporteIterpretacionInterconsultas generadorInterPretacion=new GeneradorSubReporteIterpretacionInterconsultas(); 
							reportFormatoHc.summary(cmp.subreport(generadorInterPretacion.generarReporte(interpretacion+"")));

						}

						//espacio entre reportes para que no queden pegados 
						reportFormatoHc.summary(cmp.text("").setHeight(5));

					}
					else if(dto.getCodigoTipoEvolucion()==ConstantesImpresionHistoriaClinica.tipoEvolucionValoracionConsultaExterna)
					{
						//reporte de valoración de consulta externa
						GeneradorSubReporteValoracionesConsultaExterna generador=new GeneradorSubReporteValoracionesConsultaExterna(); 
						//List<JasperReportBuilder> subRValConsExt = 
								generador.generarReporte(dto.getCodigoPk(),filtro,usuario,paciente,reportFormatoHc);
						/*if(subRValConsExt != null && !subRValConsExt.isEmpty()){

							//se recorren lista de subreportes y se adicionan al summary del reporte global
							for(JasperReportBuilder subRValConE : subRValConsExt){ 
								reportFormatoHc.summary(cmp.subreport(subRValConE));
							}

							//espacio entre reportes para que no queden pegados 
							reportFormatoHc.summary(cmp.text("").setHeight(5));
						}*/

					}
					else if(dto.getCodigoTipoEvolucion()==ConstantesImpresionHistoriaClinica.tipoEvolucionSolicitudesProcedimientos)
					{
						// ordenes de procedimiento
						ArrayList<HashMap> listadoordenes = new ArrayList<HashMap>();
						GeneradorSubReporteProcedimientosOrdenes generadorSubReporteProcedimientosOrdenes= new GeneradorSubReporteProcedimientosOrdenes();
						HashMap mapaProc = ImpresionResumenAtenciones.consultarSolicitudesProcedimientos(dto.getCodigoPk());
						Integer tamanio=Utilidades.convertirAEntero(String.valueOf(mapaProc.get("numRegistros")));

						//se agrupan las ordenes por hora y por medico 
						if(tamanio>0){
							listadoordenes.add(mapaProc);
							Boolean flag = true;
							for (int j = i; j <resultado.size() && flag; j++) {
								if(i+1<resultado.size()){
									//se agrupan las ordenes de procedimiento
									HashMap tmp = ordenesProcedimientoAgrupadosPorHora(i+1, resultado,dto.getFecha()+dto.getHora());

									//cuando se retorne null indica que se termino el agrupamiento
									if(tmp!=null){
										listadoordenes.add(tmp);

										// se adelanta el pivote del ciclo de orden cronológico
										i=i+1;
									}else{
										flag = false;
									}
								}

							}

							if(listadoordenes.size()>0){
								//generacion del reporte 
								//List<JasperReportBuilder> subReports=
										generadorSubReporteProcedimientosOrdenes.procedimientosOrdenesTotales(listadoordenes,usuario,paciente,reportFormatoHc);
								/*if(subReports != null &&
										!subReports.isEmpty()){

									// se recorre lista con los subreportes generados 
									for (int cont = 0; cont <subReports.size(); cont++) {
										reportFormatoHc.summary(cmp.subreport(subReports.get(cont)));
									}
									if(subReports.size()>0){

										// se adiciona espacio entre subreportes 
										reportFormatoHc.summary(cmp.text("").setHeight(5));
									}

								}*/
							}
						}
					}
					else if(dto.getCodigoTipoEvolucion()==ConstantesImpresionHistoriaClinica.tipoEvolucionSolicitudesMedicamentos)
					{
						//ordenes medicamentos 
						GeneradorSubReporteOrdenesMedicamentos generadorSubReporteOrdenesMedicamentos= new GeneradorSubReporteOrdenesMedicamentos();
						HashMap mapaOrdenesMedicamentos = new HashMap();
						mapaOrdenesMedicamentos = ImpresionResumenAtenciones.consultarSolicitudesMedicamentoInsumos(dto.getCodigoPk());
						Integer tamanioOrdenesMedicamentos=Utilidades.convertirAEntero(String.valueOf(mapaOrdenesMedicamentos.get("numRegistros")));
						mapaOrdenesMedicamentos.put("codigoInstitucion", usuario.getCodigoInstitucionInt());
						//se recorren lista de subreportes y se adicionan al summary del reporte global
						for (int j = 0; j < tamanioOrdenesMedicamentos; j++) {
							reportFormatoHc.summary(cmp.subreport(generadorSubReporteOrdenesMedicamentos.ordenesMedicamentos(mapaOrdenesMedicamentos, usuario, paciente,j)));
							reportFormatoHc.summary(cmp.subreport(generadorSubReporteOrdenesMedicamentos.valorPos(mapaOrdenesMedicamentos, usuario, paciente, j)));
						}
						if(tamanioOrdenesMedicamentos>0){

							//espacio para que no quede unido al siguiente subreporte
							reportFormatoHc.summary(cmp.text("").setHeight(5));
						}


					}
					else if(dto.getCodigoTipoEvolucion()==ConstantesImpresionHistoriaClinica.tipoEvolucionSolicitudesCirugias)
					{
						// ordenes cirugia
						HashMap cx = ImpresionResumenAtenciones.consultarOrdenesCirugias(dto.getCodigoPk());
						GeneradorSubReporteOrdenesCirugia generadorSubReporteOrdenesCirugia = new GeneradorSubReporteOrdenesCirugia();
						Integer tamOrdenesCirugia = Utilidades.convertirAEntero(String.valueOf(cx.get("numRegistros")));

						if(tamOrdenesCirugia>0){
							// adicion de subreporte 
							reportFormatoHc.summary(cmp.subreport(generadorSubReporteOrdenesCirugia.ordenesCirugias(cx, usuario, paciente,dto.getCodigoPk())));
						}
						if(cx.size()>0){
							//espacio separador entre subreporte
							reportFormatoHc.summary(cmp.text("").setHeight(5));
						}

					}
					else if(dto.getCodigoTipoEvolucion()==ConstantesImpresionHistoriaClinica.tipoEvolucionSolicitudesInterconsultas)
					{
						//interconsulta 
						HashMap interConsultamap = ImpresionResumenAtenciones.consultarSolicitudesInterconsultas(dto.getCodigoPk());
						GeneradorSubReporteOrdenesInterconsulta generadorSubReporteOrdenesInterconsulta = new GeneradorSubReporteOrdenesInterconsulta();
						Integer tamanio=Utilidades.convertirAEntero(String.valueOf(interConsultamap.get("numRegistros")));
						if(tamanio>0){
							//se adiciona el subreporte de interconsulta
							reportFormatoHc.summary(cmp.subreport(generadorSubReporteOrdenesInterconsulta.ordenesInterconsulta(interConsultamap, usuario, paciente,dto.getCodigoPk())));
						}
						if(interConsultamap.size()>0){
							//espacio separador entre subreporte
							reportFormatoHc.summary(cmp.text("").setHeight(5));
						}
					}
					else if(dto.getCodigoTipoEvolucion()==ConstantesImpresionHistoriaClinica.tipoEvolucionOrdenesMedicas)
					{
						//ordenes medicas 
						HashMap orden=ImpresionResumenAtenciones.consultarOrdenMedica(dto.getCodigoPk(),filtro.getIngreso(),Integer.valueOf(dtoResultado.getCuenta()));
						List<DtoObservacionesGeneralesOrdenesMedicas> observaciones = ImpresionResumenAtenciones. consultarOrdenMedica(dto.getCodigoPk());
						Integer tamanio=Integer.parseInt(orden.get("numRegistros")+"");
						GeneradorSubReporteOrdenesMedicas generadorSubReporteOrdenesMedicas = new GeneradorSubReporteOrdenesMedicas();

						for (int cont = 0; cont < tamanio; cont++) {
							//se adiciona el reporte de ordenes medicas
							reportFormatoHc.summary(cmp.subreport(generadorSubReporteOrdenesMedicas.ordenesMedicamentos(orden, usuario, paciente, cont,dtoResultado.getCuenta())));
						}

						if (tamanio>0) 
						{
							// si existen ordenes medicas se adiciona la sigueinte seccion
							Integer tamanioOrden=Integer.parseInt(orden.get("numRegistros")+"");
							GeneradorSubReporteProcedimientosOrdenes generadorSubReporteProcedimientosOrdenes= new GeneradorSubReporteProcedimientosOrdenes();
							if(tamanioOrden>0 || !String.valueOf(orden.get("observacioneshoja_"+0)).equals("") || !String.valueOf(orden.get("fechasuspencionhoja_"+0)).equals("")){

								//hermorhu - MT6479 
								//Si las observaciones de Hoja Neurologica ya fueron impresas no se vuelven a imprimir
								
									//subreporte de observaciones
									reportFormatoHc.summary(cmp.subreport(generadorSubReporteProcedimientosOrdenes
											.ordenesMedicasHojaObservaciones(orden, usuario, paciente,/*generadorSubReporteOrdenesMedicas.getFlagAdicionDatos()*/1,dto,observaciones, observHojaNeurologicaSel)));
	                                //Mt7550 - leoquico 
									if(observaciones.isEmpty()) {
										observHojaNeurologicaSel = true;
									}
									
									//seccion de separador 
									reportFormatoHc.summary(cmp.text("").setHeight(3));
								
								if(generadorSubReporteOrdenesMedicas.getFlagAdicionDatos() > 0 || observHojaNeurologicaSel) {
									//seccion de medico responsable
									reportFormatoHc.summary(cmp.subreport(generadorSubReporteProcedimientosOrdenes.ordenesMedicasMedicoResponsable(orden, usuario, paciente,generadorSubReporteOrdenesMedicas.getFlagAdicionDatos())));

									//seccion de separación
									reportFormatoHc.summary(cmp.text("").setHeight(5));
									
								}

							}else{

								if(generadorSubReporteOrdenesMedicas.getFlagAdicionDatos()>0){
									//seccion de medico responsable
									reportFormatoHc.summary(cmp.subreport(generadorSubReporteProcedimientosOrdenes.ordenesMedicasMedicoResponsable(orden, usuario, paciente,generadorSubReporteOrdenesMedicas.getFlagAdicionDatos())));

									//seccion de separación
									reportFormatoHc.summary(cmp.text("").setHeight(5));
								}
							}
						}
					}
					else if(dto.getCodigoTipoEvolucion()==ConstantesImpresionHistoriaClinica.codigoSeccionCirugias){
						GeneradorSubReporteHojaQuirurgica generadorSubReporteHojaQuirurgica= new GeneradorSubReporteHojaQuirurgica();
						if(dtoResultado.getImprimirInformacionCirugia()){

							if(!UtilidadTexto.isEmpty(String.valueOf(String.valueOf( dto.getHojaQuirurgica().getMapaHojaQuirur().get("enca_solicitud_0"))))){
								//List<JasperReportBuilder> listaTmp=
										generadorSubReporteHojaQuirurgica.reporteHojaQuirurgica(dto.getHojaQuirurgica(), usuario, paciente,Integer.valueOf(String.valueOf( dto.getHojaQuirurgica().getMapaHojaQuirur().get("enca_solicitud_0"))),reportFormatoHc);

								/*for (int j = 0; j < listaTmp.size(); j++) {
									reportFormatoHc.summary(cmp.subreport(listaTmp.get(j)));

								}

								//seccion de separación
								reportFormatoHc.summary(cmp.text("").setHeight(5));
								*/
							}
						}

					}
				}


				/*if(dtoResultado.getImprimirNotasRecuperacion()){

					//se obtienen lista de elementos a agregar al reporte en orden cronológico
					ArrayList <DtoResultadoImpresionHistoriaClinica> resultadoNotas=dtoResultado.getResultadoSeccionEvolucion();
					ArrayList <DtoHojaQuirurgicaAnestesia> datosCirugia = new ArrayList<DtoHojaQuirurgicaAnestesia>();
					for(int k=0;k<resultadoNotas.size();k++)
					{
						//dependiendo al if que entre se adiciona el subreporte del servicio en orden cornológico
						DtoResultadoImpresionHistoriaClinica dtoNotas=(DtoResultadoImpresionHistoriaClinica)resultado.get(k);

						if(dtoNotas.getCodigoTipoEvolucion()==ConstantesImpresionHistoriaClinica.codigoSeccionCirugias){
							datosCirugia.add(dtoNotas.getHojaQuirurgica());
						}

					}

					GeneradorSubReporteNotasRecuperacionHojaQuirurgica generadorSubReporteNotasRecuperacionHojaQuirurgica = new GeneradorSubReporteNotasRecuperacionHojaQuirurgica();

					for (int t = 0; t < datosCirugia.size(); t++) {
						
						//hermorhu - MT4519
						if(datosCirugia.get(t).getMapaNotasRecuperacion().get("hayNotasRecuperacion").toString().equals("SI")) {
							JasperReportBuilder tmp = generadorSubReporteNotasRecuperacionHojaQuirurgica.reporteNotasRecuperacionCirugia(datosCirugia.get(t));
	
	//						if(generadorSubReporteNotasRecuperacionHojaQuirurgica.getCantidadRegistros()!=18){
								reportFormatoHc.summary(cmp.subreport(tmp));
	//						}
	
							//seccion de separación
							reportFormatoHc.summary(cmp.text("").setHeight(8));
						}
				}*/
				if(dtoResultado.isImprimirValoracionesEnfermeria())
				{
					//Valoración de enfermerias
					HashMap mapa=dtoResultado.getValoracionesEnfermeria();
					int tamanioSeccion=Utilidades.convertirAEntero(mapa.get("numRegistros")+"");
					if(tamanioSeccion>0)
					{
						// adicion de reporte a summary de repeorte general
						GeneradorSubReporteValoracionEnfermeria generador=new GeneradorSubReporteValoracionEnfermeria(); 
						reportFormatoHc.summary(cmp.subreport( generador.crearEncabezados()).setDataSource(generador.createDataSource(dtoResultado)));
					}
				}
				if(dtoResultado.isImprimirSignosVitales())
				{
					//seccion de signos vitales
					GeneradorSubReporteSignosVitales generador=new GeneradorSubReporteSignosVitales();

					//adicion a summary de reporte general
					reportFormatoHc.summary(cmp.subreport( generador.generarReporteSignosVitales(dtoResultado.getSignosVitales())));

					reportFormatoHc.summary(cmp.text("").setHeight(8));
				}
				if(dtoResultado.isImprimirSoporteRespiratorio())
				{
					//soporte respiratorio
					HashMap mapaTemp=(HashMap)dtoResultado.getSoporteRespiratorio().get("soportes_0");
					if(mapaTemp!=null)
					{
						//se arma las columans de la tabla, se debe recorrer todos los soportes, ya que en cualquier momento se pudieron parametrizar nuevos.
						ArrayList columnas=new ArrayList();

						HashMap mapa=dtoResultado.getSoporteRespiratorio();
						int tamanio=Utilidades.convertirAEntero(mapa.get("numRegistros")+"");
						if (tamanio>0) {

							//adicion de columnas 
							columnas.add("fechahora");
							for(int i=0;i<tamanio;i++)
							{
								mapaTemp=(HashMap)mapa.get("soportes_"+i);
								int numTemp = Utilidades.convertirAEntero(mapaTemp.get("numRegistros")+"");
								for(int j=0;j<numTemp;j++)
								{
									String colStr=mapaTemp.get("parametro_"+j)+"";
									if(!columnas.contains(colStr))
									{
										columnas.add(colStr);
									}
								}
							}
							//columnas fijas y finales
							columnas.add("observaciones");
							columnas.add("usuario");

							//generacion del reporte de soporte respiratorio y adicion al summary del reporte general
							GeneradorSubReporteSoporteRespiratorio generador=new GeneradorSubReporteSoporteRespiratorio(); 
							reportFormatoHc.summary(cmp.subreport( generador.crearEncabezados(dtoResultado,columnas)).setDataSource(generador.createDataSource(dtoResultado,columnas)));
						}
					}
				}



				if(dtoResultado.isImprimirControlLiquidos())
				{
					//seccion de control de liquidos
					GeneradorSubReporteControlLiquidos generador=new GeneradorSubReporteControlLiquidos(); 

					//generación y adicion de reporte a summary general
					reportFormatoHc.summary(cmp.subreport( generador.generarReporte(dtoResultado)));
				}
				if (dtoResultado.isImprimirCateteresSondas()) {
					//seccion de cateteres y sondas
					Integer tamanio=Utilidades.convertirAEntero(dtoResultado.getCateteresSondas().get("numRegistros").toString());
					if (tamanio>0) {

						//Creación de instancia de subreporte
						GeneradorSubReporteCateteresSondasHC generadorSubReporteCateteresSondas= new GeneradorSubReporteCateteresSondasHC();

						//creacion y adicion al summary general del subreporte
						reportFormatoHc.summary(cmp.subreport(generadorSubReporteCateteresSondas.generarReporteSeccionCateteresSondas(dtoResultado, usuario, paciente)));
					}
				}

				if (dtoResultado.isImprimirCuidadosEspeciales()) {
					// seccion de cuidados especiales
					Integer tamanio=Utilidades.convertirAEntero(dtoResultado.getCuidadosEspeciales().get("numRegistros").toString());
					if (tamanio>0) {
						//Instancia de generacion de subreporte
						GeneradorSubReporteCuidadosEspecialesHC generadorSubReporteCuidadosEspeciales= new GeneradorSubReporteCuidadosEspecialesHC();

						//generacion de subreporte y adicion al summary del reporte global
						reportFormatoHc.summary(cmp.subreport(generadorSubReporteCuidadosEspeciales.generarReporteSeccionCuidadosEspeciales(dtoResultado, usuario, paciente)));
					}
				}
				
				if (dtoResultado.getImprimirEscalaGlasgow()) {
					if (!dtoResultado.getHistoricoEscalaGlasgowList().isEmpty()) {
						GeneradorSubReporteEscalaGlasgow generadorSubReporteEscalaGlasgow = new GeneradorSubReporteEscalaGlasgow();
						reportFormatoHc.summary(cmp.subreport(generadorSubReporteEscalaGlasgow.generarSubReporteEscalaGlasgow(dtoResultado, usuario, paciente)));
						
					}
				}
				
				if (dtoResultado.getImprimirHojaNeurologica()) {
					if (dtoResultado.getPupilaDerechaList().size() > 0 || dtoResultado.getPupilaIzquierdaList().size() > 0 
				 			|| dtoResultado.getConvulsiones().size() > 0 
				 			|| dtoResultado.getControlEsfinteresList().size() > 0 
				 			|| dtoResultado.getFuerzaMuscularList().size() > 0) {
						
						GeneradorSubReporteHojaNeurologica generadorSubReporteHojaNeurologica = new GeneradorSubReporteHojaNeurologica();
						reportFormatoHc.summary(cmp.subreport(generadorSubReporteHojaNeurologica.generarSubReporteHojaNeurologica(dtoResultado, usuario, paciente)));
						
					}
				}
				
				if(dtoResultado.isImprimirNotasEnfermeria())
				{
					// seccion de notas de enfermeria
					if(dtoResultado.getNotasEnfermeria().size()>0){

						//Instancia de generacion subreporte
						GeneradorSubReporteNotasEnfermeria generadorSubReporteNotasEnfermeria = new GeneradorSubReporteNotasEnfermeria();

						//generacion y adicion de subreporte a summary golbal
						reportFormatoHc.summary(cmp.subreport(generadorSubReporteNotasEnfermeria.notasEnfermiria(dtoResultado, usuario, paciente)));
					}

				}

				if(dtoResultado.isImprimirAdminMedicamentos())
				{
					// Seccion de administracion de medicamentos
					Integer tamanio=Utilidades.convertirAEntero(String.valueOf(dtoResultado.getAdminMedicamente().get("numRegistros")));
					if (tamanio>0) {
						//Instancia de generacion de subreporte
						GeneradorSubReporteAdministracionMedicamentosHojaAdministracionMedicamentos generadorSubReporteHojaAdministracionMedicamentos = new GeneradorSubReporteAdministracionMedicamentosHojaAdministracionMedicamentos();

						//adicion de subreporte a summary 
						reportFormatoHc.summary(cmp.subreport(generadorSubReporteHojaAdministracionMedicamentos.generarReporteSeccionHojaAdministracionMedicamentos(dtoResultado,usuario,paciente)));
					}
				}
				if (dtoResultado.getImprimirConsumosInsumos()) {
					//Seccion de consumos insumos
					Integer tamanio=Utilidades.convertirAEntero(dtoResultado.getConsumosInsumos().get("numRegistros")+"");
					if (tamanio>0) {
						//Instancia de generacion de reporte
						GeneradorSubReporteAdministracionMedicamentosConsumoInsumos generadorReporteConsumosInsumos = new GeneradorSubReporteAdministracionMedicamentosConsumoInsumos();

						//generacion de reporte y adicion al summary 
						reportFormatoHc.summary(cmp.subreport(generadorReporteConsumosInsumos.generarReporteSeccionConsumoInsumos(dtoResultado,usuario,paciente)));
					}
				}






				if(dtoResultado.isImprimirOrdenaAmbulatorias())
				{
					//Seccion de ordenes ambulatorias - Instancia de generacion de subreporte
					GeneradorSubReporteOrdenaAmbulatorias generador=new GeneradorSubReporteOrdenaAmbulatorias();

					//Generación y Adicion de subreportea summary general  
					reportFormatoHc.summary(cmp.subreport( generador.generarReporte(dtoResultado)));
				}

				//notas generales 
				/*if(dtoResultado.getImprimirNotasGeneralesCirugia()){

					//se obtienen lista de elementos a agregar al reporte en orden cronológico
					ArrayList <DtoResultadoImpresionHistoriaClinica> resultadoNotas=dtoResultado.getResultadoSeccionEvolucion();
					ArrayList <DtoHojaQuirurgicaAnestesia> datosCirugia = new ArrayList<DtoHojaQuirurgicaAnestesia>();
					for(int k=0;k<resultadoNotas.size();k++)
					{
						//dependiendo al if que entre se adiciona el subreporte del servicio en orden cornológico
						DtoResultadoImpresionHistoriaClinica dtoNotas=(DtoResultadoImpresionHistoriaClinica)resultado.get(k);

						if(dtoNotas.getCodigoTipoEvolucion()==ConstantesImpresionHistoriaClinica.codigoSeccionCirugias){
							datosCirugia.add(dtoNotas.getHojaQuirurgica());
						}

					}

					GeneradorSubReporteNotasGeneralesHojaQuirurgica generadorSubReporteNotasGeneralesHojaQuirurgica = new GeneradorSubReporteNotasGeneralesHojaQuirurgica();

					for (int t = 0; t < datosCirugia.size(); t++) {
						reportFormatoHc.summary(cmp.subreport(generadorSubReporteNotasGeneralesHojaQuirurgica.reporteNotasGeneralesCirugia(datosCirugia.get(t))));

						//seccion de separación
						reportFormatoHc.summary(cmp.text("").setHeight(5));
					}
				}*/





				if(dtoResultado.getImprimirInterpretacionRespuesta())
				{
					//Seccion de Interpretacion respuesta - Instancia de generacion subreporte
					GeneradorSubReporteRespuestaInterpretacionSolicitudes generadorRespuestaInterpretacionSolicitudes = new GeneradorSubReporteRespuestaInterpretacionSolicitudes();

					//Generacion y adicion del subreporte al summary global 
					reportFormatoHc.summary(cmp.subreport(generadorRespuestaInterpretacionSolicitudes.respuestaInterpretacion(dtoResultado, usuario, paciente)));
				}

				if(dtoResultado.getHayResumenParcial()){
					GeneradorSubResumenParcialHC generadorSubResumenParcialHC=new GeneradorSubResumenParcialHC();
					JasperReportBuilder reportResumenParcialHC=generadorSubResumenParcialHC.reporteResumenParcialHC(dtoResultado.getResumenParcialHc(), usuario, paciente);

					if(reportResumenParcialHC!=null){
						//espacio separador entre subreporte
						reportFormatoHc.summary(cmp.text("").setHeight(5));

						//reporte de resumen parcial de HC
						reportFormatoHc.summary(cmp.subreport(reportResumenParcialHC));

						//espacio separador entre subreporte
						reportFormatoHc.summary(cmp.text("").setHeight(5));
					}

				}


				if(dtoResultado.getImprimirNotasAclaratorias())
				{
					GeneradorSubReporteNotasAclaratorias generadorSubReporteNotasAclaratorias = new GeneradorSubReporteNotasAclaratorias();
					reportFormatoHc.summary(cmp.subreport(generadorSubReporteNotasAclaratorias.reporteNotasAclaratorias(dtoResultado.getListaNotasAclaratorias(), usuario, paciente)));
				}

			}else{

				GeneradorSubReporteNotasAclaratorias generadorSubReporteNotasAclaratorias = new GeneradorSubReporteNotasAclaratorias();
				reportFormatoHc.summary(cmp.subreport(generadorSubReporteNotasAclaratorias.reporteNotasAclaratorias(dtoResultado.getListaNotasAclaratorias(), usuario, paciente)));

			}


			//separador para mensaje final
			reportFormatoHc.summary(cmp.text("").setHeight(20));

			reportFormatoHc.summary(cmp.text(IConstantesReporteHistoriaClinica.constanteMensajeFinalHC).setStyle(stl.style().setFontSize(7)));

			//Obliga a que todos los subreportes muestren el page Header/Footer
			reportFormatoHc.setSummaryWithPageHeaderAndFooter(true);
			

			if(!varios){
				//Se le agrega el componente Footer de todo el reporte
//				reportFormatoHc.pageFooter(disenio.crearcomponentePiePagina(params));
			}
		

			
			//atributo para separar el reporte en cada salto de página 
			reportFormatoHc.setSummarySplitType(SplitType.IMMEDIATE);
			
			//se compila el reporte
			reportFormatoHc.build();
		}//control de errores
		catch(Exception e){
			e.printStackTrace();
			logger.error("Se presento un problema al generar el reporte de Historia Clínica"+e.getMessage());
		}

		//Se retorna el reporta el reporte completo con los subreportes correpondientes
		return reportFormatoHc;
	}



	/**
	 * método para obtener procedimientos agrupados por hora y que sean seguidos
	 * @param i
	 * @param resultado
	 * @param fechaHora
	 * @return Mapa con que cumple la condicion de agrupación
	 */
	public HashMap ordenesProcedimientoAgrupadosPorHora(Integer i,ArrayList <DtoResultadoImpresionHistoriaClinica> resultado,String fechaHora){
		HashMap mapaValores = new HashMap();
		try {

			//Se obtiene el dto de impresion de historia clínica
			DtoResultadoImpresionHistoriaClinica dto=(DtoResultadoImpresionHistoriaClinica)resultado.get(i);

			//Se valida que sea de tipo procedimiento 
			if(dto.getCodigoTipoEvolucion()==ConstantesImpresionHistoriaClinica.tipoEvolucionSolicitudesProcedimientos){
				mapaValores = ImpresionResumenAtenciones.consultarSolicitudesProcedimientos(dto.getCodigoPk());
				Integer tamanio=Utilidades.convertirAEntero(String.valueOf(mapaValores.get("numRegistros")));

				//se retorna mapa que cumple con la codincion puesta 
				if((tamanio>0) &&((dto.getFecha()+dto.getHora()).equals(fechaHora))){

					//mapa con valores
					return mapaValores;
				}

			}

		}//control de errores 
		catch (Exception e) {
			logger.error("error consultando procedimientos a agrupar pro hora "+e.getMessage());
		}

		//Retornal mull si no se cumplen la condiciones
		return null;
	}



	/**
	 * El mapa con key,valor perteneciente a el encabezado de las páginas
	 * @param encabezadoPaciente
	 * @param dto
	 * @param usuario
	 * @param paciente
	 * @param institucionUsuario
	 * @param idIngreso
	 * @return mapa con los valores de los parametros a imprimir en el reporte  (encabezado)
	 */
	public Map<String , String> datosUsuario(HashMap encabezadoPaciente,DtoImpresionHistoriaClinica dto
			,UsuarioBasico usuario, PersonaBasica paciente,InstitucionBasica institucionUsuario,String idIngreso) throws Exception{

		//variables locales
		HashMap encabezadoImpresion = new HashMap();
		HashMap acompananteYResponsable = new HashMap();
		PersonaBasica personaBasica = new PersonaBasica();
		String fechaHoraIngreso="";
		String centroAtencionPaciente="";
		String fechaHoraIngresoPaciente="";
		String fechaMuerte="";

		try {

			//Consulta de paciente 
			Connection con = UtilidadBD.abrirConexion();
			personaBasica.cargar(con, usuario.getCodigoPersona());
			fechaHoraIngresoPaciente = personaBasica.consultarFechaHoraIngreso(con,String.valueOf(paciente.getCodigoPersona()), idIngreso);
			personaBasica.cargar(con,  paciente.getCodigoPersona());
			Paciente pacienteMundo = new Paciente();

			//consulta de la fecha de ingreso
			fechaHoraIngreso=pacienteMundo.obtenerFechaHoraIngreso(con, String.valueOf(paciente.getCodigoPersona()));

			//consulta del centro de atencion del paciente 
			centroAtencionPaciente=pacienteMundo.consultarCentroAtencionPaciente(con, String.valueOf(paciente.getCodigoPersona()));

			fechaMuerte=UtilidadesHistoriaClinica.obtenerFechaMuerte(con,String.valueOf( paciente.getCodigoPersona()));
			
			UtilidadBD.closeConnection(con);

		}//control de errores 
		catch (SQLException e) {
			logger.error("error consultando paciente "+e.getMessage());
		}


		//se llena el mapa con key - valor para el encabezado
		encabezadoImpresion=encabezadoPaciente;
		Map<String , String> parametros = new HashMap<String, String>();
		parametros.put(IConstantesReporteHistoriaClinica.ubicacionLogo, institucionUsuario.getUbicacionLogo());
		parametros.put(IConstantesReporteHistoriaClinica.nombreInstitucion, institucionUsuario.getRazonSocial());
		parametros.put(IConstantesReporteHistoriaClinica.nitInstitucion, ConstantesBD.reporteLabelnit + institucionUsuario.getNit());
		parametros.put(IConstantesReporteHistoriaClinica.actividadEconomica, institucionUsuario.getActividadEconomica());
		parametros.put(IConstantesReporteHistoriaClinica.direccion, messageResource.getMessage("historia_clinica_lable_direccion") + institucionUsuario.getDireccion() 
				+ ConstantesBD.telefono
				+ institucionUsuario.getTelefono());
		parametros.put(IConstantesReporteHistoriaClinica.centroAtencion, messageResource.getMessage("historia_clinica_lable_centro_de_atencion") + centroAtencionPaciente);
		parametros.put(IConstantesReporteHistoriaClinica.tipoConsulta, "IMPRESIÓN HISTORIA CLÍNICA");
		SimpleDateFormat sdf= new SimpleDateFormat("dd/MM/yyyy");
		parametros.put(IConstantesReporteHistoriaClinica.fecha,sdf.format(new Date(System.currentTimeMillis())) );
		parametros.put(IConstantesReporteHistoriaClinica.usuarioProceso,
				usuario.getNombreUsuario() + " ("
				+ usuario.getLoginUsuario() + ")");


		// Ubicacion Logo IConstantesReporte.rutaLogo
		parametros.put(IConstantesReporteHistoriaClinica.rutaLogo,institucionUsuario.getLogoReportes());

		//Se cambian las tildes en html que están en la base de datos por tildes normales Minúsculas
		String TipoDocumentoTildes=paciente.getCodigoTipoIdentificacionPersona()
		.replace("&aacute;", "á")
		.replace("&eacute;", "é")
		.replace("&iacute;", "í")
		.replace("&oacute;", "ó")
		.replace("&uacute;", "ú")
		.replace("&Ntilde;", "Ñ")
		.replace("&ntilde;", "ñ")
		.replace("&Aacute;", "Á")
		.replace("&Eacute;", "É")
		.replace("&Iacute;", "Í")
		.replace("&Oacute;", "Ó")
		.replace("&Uacute;", "Ú");

		//Se cambian las tildes en html que están en la base de datos por tildes normales Mayúsculas
		String nombhreApellidosLimpios=paciente.getApellidosNombresPersona().replace("&ntilde;","ñ")
		.replace("&Ntilde;", "Ñ")
		.replace("&aacute;", "á")
		.replace("&eacute;", "é")
		.replace("&iacute;", "í")
		.replace("&oacute;", "ó")
		.replace("&uacute;", "ú")
		.replace("&Aacute;", "Á")
		.replace("&Eacute;", "É")
		.replace("&Iacute;", "Í")
		.replace("&Oacute;", "Ó")
		.replace("&Uacute;", "Ú");

		//Adición parametros key - valor
		parametros.put(IConstantesReporteHistoriaClinica.nombrepaciente,nombhreApellidosLimpios);
		parametros.put(IConstantesReporteHistoriaClinica.fechaNacimiento,paciente.getFechaNacimiento());
		parametros.put(IConstantesReporteHistoriaClinica.estadoCivil,String.valueOf(encabezadoImpresion.get("estadocivil_0"))==null?"":String.valueOf(encabezadoImpresion.get("estadocivil_0")));
		parametros.put(IConstantesReporteHistoriaClinica.residencia,String.valueOf(encabezadoImpresion.get("residencia_0"))==null?"":String.valueOf(encabezadoImpresion.get("residencia_0")));	
		parametros.put(IConstantesReporteHistoriaClinica.fechahoraingreso,fechaHoraIngresoPaciente);
		parametros.put(IConstantesReporteHistoriaClinica.telefonoPaciente,String.valueOf(encabezadoImpresion.get("telefono_0")));
		parametros.put(IConstantesReporteHistoriaClinica.convenioPaciente,String.valueOf(encabezadoImpresion.get("convenio_0"))==null?"":String.valueOf(encabezadoImpresion.get("convenio_0"))); 
		parametros.put(IConstantesReporteHistoriaClinica.tipoAfiliado,String.valueOf(encabezadoImpresion.get("tipoafiliado_0")));
		parametros.put(IConstantesReporteHistoriaClinica.sexo,paciente.getSexo());
		parametros.put(IConstantesReporteHistoriaClinica.regimen,paciente.getNombreTipoRegimen());

		parametros.put(IConstantesReporteHistoriaClinica.ocupacion,String.valueOf(encabezadoImpresion.get("ocupacion_0")));
		parametros.put(IConstantesReporteHistoriaClinica.tipoNumeroID,TipoDocumentoTildes+" "+paciente.getNumeroIdentificacionPersona());
		parametros.put(IConstantesReporteHistoriaClinica.fechahoraEgreso,dto.getFechaEgreso());
		
		int diaComp = 0;                                                                                                                   
		int mesComp = 0;
		int anioComp = 0;
		
		int  anioNac = 0;
		int mesNac = 0;
		int  diaNac= 0;
		
		
		String edad = "";
		
		boolean utilizarEdadActual=false;
		
		if (!UtilidadTexto.isEmpty(fechaMuerte)){
			diaComp = UtilidadFecha.getMesAnoDiaFecha("dia", paciente.getFechaNacimiento());                                                                                                                   
			mesComp = UtilidadFecha.getMesAnoDiaFecha("mes", paciente.getFechaNacimiento());
			anioComp = UtilidadFecha.getMesAnoDiaFecha("anio", paciente.getFechaNacimiento());
			
			anioNac = UtilidadFecha.getMesAnoDiaFecha("dia", fechaMuerte.split(" ")[0].replace("-", "/"));
			mesNac = UtilidadFecha.getMesAnoDiaFecha("mes", fechaMuerte.split(" ")[0].replace("-", "/"));
			diaNac= UtilidadFecha.getMesAnoDiaFecha("anio", fechaMuerte.split(" ")[0].replace("-", "/"));
			
		}else{
			if(!UtilidadTexto.isEmpty(dto.getFechaEgreso())){
				diaComp = UtilidadFecha.getMesAnoDiaFecha("dia", paciente.getFechaNacimiento());                                                                                                                   
				mesComp = UtilidadFecha.getMesAnoDiaFecha("mes", paciente.getFechaNacimiento());
				anioComp = UtilidadFecha.getMesAnoDiaFecha("anio", paciente.getFechaNacimiento());
				
				anioNac = UtilidadFecha.getMesAnoDiaFecha("dia", dto.getFechaEgreso().split(" ")[0].replace("-", "/"));
				mesNac = UtilidadFecha.getMesAnoDiaFecha("mes", dto.getFechaEgreso().split(" ")[0].replace("-", "/"));
				diaNac= UtilidadFecha.getMesAnoDiaFecha("anio", dto.getFechaEgreso().split(" ")[0].replace("-", "/"));
				
			}else{
				parametros.put(IConstantesReporteHistoriaClinica.edad,paciente.getEdad()+" años");
				utilizarEdadActual=true;
			}
		}
		
		//calcularEdadDetalladaCompleta
		if(!utilizarEdadActual){
			edad = UtilidadFecha.calcularEdadDetalladaCompleta( anioComp , mesComp ,   diaComp,diaNac, mesNac,anioNac);
			String[]stringEdad=edad.split("y");
			int[] vectorEdad = UtilidadFecha.calcularVectorEdad(anioComp , mesComp ,   diaComp,diaNac, mesNac,anioNac);
			if(vectorEdad[0]>5){
				parametros.put(IConstantesReporteHistoriaClinica.edad,edad.split("y")[0]);
			}else{
				if(vectorEdad[0]>0&&vectorEdad[0]<6){
					if(stringEdad.length>1){
						parametros.put(IConstantesReporteHistoriaClinica.edad,stringEdad[0]+" y "+edad.split("y")[1]);
					}else{
						parametros.put(IConstantesReporteHistoriaClinica.edad,edad.split("y")[0]);
					}
				}else{
					if(vectorEdad[1]>0&&vectorEdad[1]<13){
						if(stringEdad.length==1){
							parametros.put(IConstantesReporteHistoriaClinica.edad,stringEdad[0]);
						}else{
							if(stringEdad.length==2){
								parametros.put(IConstantesReporteHistoriaClinica.edad,stringEdad[0]+" y "+stringEdad[1]);
							}
						}
					}else{
						if(vectorEdad[2]<31){
							if(stringEdad.length==1){
								parametros.put(IConstantesReporteHistoriaClinica.edad,stringEdad[0]);
							}else{
								if(stringEdad.length==2){
									parametros.put(IConstantesReporteHistoriaClinica.edad,stringEdad[1]);
								}
							}
						}
					}
				}
			}
		}
		
		
		
		
		
		if(paciente.getUltimaViaIngreso()!=null && !paciente.getUltimaViaIngreso().equals("")){
			parametros.put(IConstantesReporteHistoriaClinica.viaEgreso,dto.getViaEgreso());
		}else{
			parametros.put(IConstantesReporteHistoriaClinica.viaEgreso,"");
		}
		if(dto.getCodigoViaIngreso()!=null && !dto.getCodigoViaIngreso().equals("")){
			parametros.put(IConstantesReporteHistoriaClinica.viaIngreso,dto.getCodigoViaIngreso());
		}else{
			parametros.put(IConstantesReporteHistoriaClinica.viaIngreso,"");
		}
		//Se obtiene información del acompañante
		acompananteYResponsable= (HashMap) encabezadoImpresion.get("infoAcompananteMap");

		if (acompananteYResponsable!=null) {
			Integer tam =  Integer.parseInt(acompananteYResponsable.get("numRegistros").toString());
			if (tam>0) {

				parametros.put(IConstantesReporteHistoriaClinica.acompanantePaciente,String.valueOf(acompananteYResponsable.get("nombreapellidoacompanante_0")));
				parametros.put(IConstantesReporteHistoriaClinica.telParentescoUno,String.valueOf(acompananteYResponsable.get("telefonoacomp_0")));
				parametros.put(IConstantesReporteHistoriaClinica.nombreParentescoUno,String.valueOf(acompananteYResponsable.get("parentesco_0")));

				parametros.put(IConstantesReporteHistoriaClinica.responsablePaciente,String.valueOf(acompananteYResponsable.get("nombreapellidoacompanante_0")));
				parametros.put(IConstantesReporteHistoriaClinica.telParentescoDos,String.valueOf(acompananteYResponsable.get("telefonoacomp_0")));
				parametros.put(IConstantesReporteHistoriaClinica.nombreparentescoDos,String.valueOf(acompananteYResponsable.get("parentesco_0")));

			}else{
				parametros.put(IConstantesReporteHistoriaClinica.acompanantePaciente,"");
				parametros.put(IConstantesReporteHistoriaClinica.telParentescoUno,"");
				parametros.put(IConstantesReporteHistoriaClinica.nombreParentescoUno,"");

				parametros.put(IConstantesReporteHistoriaClinica.responsablePaciente,"");
				parametros.put(IConstantesReporteHistoriaClinica.telParentescoDos,"");
				parametros.put(IConstantesReporteHistoriaClinica.nombreparentescoDos,"");

			}
		}

		//Se retorna el mapa con los parámetros del encabezado
		return parametros;
	}
	
	
	
	

}

