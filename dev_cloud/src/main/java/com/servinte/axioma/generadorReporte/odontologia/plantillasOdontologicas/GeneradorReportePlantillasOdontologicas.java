package com.servinte.axioma.generadorReporte.odontologia.plantillasOdontologicas;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import util.ConstantesBD;
import util.ConstantesCamposParametrizables;
import util.ConstantesIntegridadDominio;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.odontologia.InfoDetallePlanTramiento;
import util.odontologia.InfoHallazgoSuperficie;
import util.odontologia.InfoOdontograma;
import util.odontologia.InfoProgramaServicioPlan;
import util.odontologia.InfoServicios;
import util.reportes.GeneradorReporte;

import com.princetonsa.actionform.odontologia.EvolucionOdontologicaForm;
import com.princetonsa.actionform.odontologia.ValoracionOdontologicaForm;
import com.princetonsa.dto.historiaClinica.parametrizacion.DtoCampoParametrizable;
import com.princetonsa.dto.historiaClinica.parametrizacion.DtoComponente;
import com.princetonsa.dto.historiaClinica.parametrizacion.DtoElementoParam;
import com.princetonsa.dto.historiaClinica.parametrizacion.DtoOpcionCampoParam;
import com.princetonsa.dto.historiaClinica.parametrizacion.DtoPlantilla;
import com.princetonsa.dto.historiaClinica.parametrizacion.DtoSeccionFija;
import com.princetonsa.dto.historiaClinica.parametrizacion.DtoSeccionParametrizable;
import com.princetonsa.dto.odontologia.DtoComponenteIndicePlaca;
import com.princetonsa.dto.odontologia.DtoTratamientoExterno;
import com.princetonsa.dto.odontologia.DtoTratamientoInterno;
import com.princetonsa.dto.odontologia.DtoValDiagnosticosOdo;
import com.princetonsa.mundo.odontologia.ComponenteOdontograma;
import com.servinte.axioma.generadorReporte.odontologia.dtoReportes.DtoFormatoPlantillaDetalleHallazgosPlanTratamientoInicial;
import com.servinte.axioma.generadorReporte.odontologia.dtoReportes.DtoFormatoPlantillaHallazgosPiezasDentales;
import com.servinte.axioma.generadorReporte.odontologia.dtoReportes.DtoFormatoPlantillaOdontologica;
import com.servinte.axioma.generadorReporte.odontologia.dtoReportes.DtoFormatoPlantillaOrdenarColumnasPDF;
import com.servinte.axioma.generadorReporte.odontologia.dtoReportes.DtoFormatoPlantillaProgServCitaAsignada;

/**
 * Esta clase se encarga de organizar la información de las plantillas odontologicas 
 * de una cita para generar el reporte en formato pdf
 * @author Fabian Becerra
 *
 */
public class GeneradorReportePlantillasOdontologicas extends GeneradorReporte{
Map<String, Object> parametrosReporte = new HashMap<String, Object>();
	
	private ValoracionOdontologicaForm formaValoracion;
	private EvolucionOdontologicaForm formaEvolucion;
	private ArrayList<DtoPlantilla> plantillas;
	private static String RUTA_REPORTE_GENERAL = "com/servinte/axioma/generadorReporte/odontologia/plantillasOdontologicas/reportePlantillaOdontologica.jasper";
	private static String RUTA_SUBREPORTE_RESULTADOCONSULTA = "com/servinte/axioma/generadorReporte/odontologia/plantillasOdontologicas/subReportePlantillaOdontologica.jasper";
	private static String NOMBRE_SUBREPORTE_RESULTADOCONSULTA = "subReportePlantillaOdontologica";
	private static String RUTA_SUBREPORTE_SECCIONFIJA = "com/servinte/axioma/generadorReporte/odontologia/plantillasOdontologicas/subReporteSeccionesFijas.jasper";
	private static String NOMBRE_SUBREPORTE_SECCIONFIJA = "subReporteSeccionesFijas";
	private static String RUTA_SUBREPORTE_HALLAZGOS = "com/servinte/axioma/generadorReporte/odontologia/plantillasOdontologicas/subReporteDetalleDeHallazgos.jasper";
	private static String NOMBRE_SUBREPORTE_HALLAZGOS="subReporteDetalleDeHallazgos";
	private static String RUTA_SUBREPORTE_HALLAZGOS_PIEZAS_DENTALES = "com/servinte/axioma/generadorReporte/odontologia/plantillasOdontologicas/subReporteHallazgosPiezasDentales.jasper";
	private static String NOMBRE_SUBREPORTE_HALLAZGOS_PIEZAS_DENTALES="subReporteHallazgosPiezasDentales";
	private static String RUTA_SUBREPORTE_HALLAZGOS_BOCA = "com/servinte/axioma/generadorReporte/odontologia/plantillasOdontologicas/subReporteHallazgosBoca.jasper";
	private static String NOMBRE_SUBREPORTE_HALLAZGOS_BOCA="subReporteHallazgosBoca";
	private static String RUTA_SUBREPORTE_TRATAMIENTOS_INTERNOS = "com/servinte/axioma/generadorReporte/odontologia/plantillasOdontologicas/subReporteTratamientosInternos.jasper";
	private static String NOMBRE_SUBREPORTE_TRATAMIENTOS_INTERNOS="subReporteTratamientosInternos";
	private static String RUTA_SUBREPORTE_TRATAMIENTOS_EXTERNOS = "com/servinte/axioma/generadorReporte/odontologia/plantillasOdontologicas/subReporteTratamientosExternos.jasper";
	private static String NOMBRE_SUBREPORTE_TRATAMIENTOS_EXTERNOS="subReporteTratamientosExternos";
	private static String RUTA_SUBREPORTE_PROG_SERV_CITA_ASIGN = "com/servinte/axioma/generadorReporte/odontologia/plantillasOdontologicas/subReporteProgramaServicioCitaAsignada.jasper";
	private static String NOMBRE_SUBREPORTE_PROG_SERV_CITA_ASIGN="subReporteProgramaServicioCitaAsignada";
	private static String RUTA_SUBREPORTE_ANTE_ODONTO = "com/servinte/axioma/generadorReporte/odontologia/plantillasOdontologicas/subReporteCreadorColumnas.jasper";
	private static String NOMBRE_SUBREPORTE_ANTE_ODONTO="subReporteCreadorColumnas";
	
	
	@SuppressWarnings("unused")
	private String RUTA_LOGO = "";
	
	@SuppressWarnings("unused")
	private  String NOMBRE_LOGO = "nombreLogo";
	
	/**
	 * 
	 * Método constructor de la clase 
	 *
	 * @author Fabian Becerra
	 */
	public GeneradorReportePlantillasOdontologicas() {
	}
	
	/**
	 * Método constructor de la clase 
	 * @param forma de valoracion odontologica
	 * @param plantillas de la cita
	 *
	 * @author Fabian Becerra
	 */
	public GeneradorReportePlantillasOdontologicas(ValoracionOdontologicaForm forma, ArrayList<DtoPlantilla> plantillas){	
		this.plantillas = plantillas;
		this.formaValoracion=forma;
	}
	
	/**
	 * Método constructor de la clase 
	 * @param forma de evolucion odontologica
	 * @param plantillas de la cita
	 *
	 * @author Fabian Becerra
	 */
	public GeneradorReportePlantillasOdontologicas(EvolucionOdontologicaForm forma, ArrayList<DtoPlantilla> plantillas){	
		this.plantillas = plantillas;
		this.formaEvolucion=forma;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Collection obtenerDatosReporte() {
		Collection<DtoFormatoPlantillaOdontologica>  collectionDTOGeneral= new ArrayList();
		DtoFormatoPlantillaOdontologica dtoGeneral = new  DtoFormatoPlantillaOdontologica();
		
		
		if(formaEvolucion!=null){
		
			RUTA_LOGO = "../"+ formaEvolucion.getInstitucionBasica().getLogoJsp();
					
					//PARAMETROS DEL ENCABEZADO Y PIE DE PAGINA DEL REPORTE
					String ubicacionLogo = formaEvolucion.getInstitucionBasica().getUbicacionLogo();
					String rutaLogo = "../"+formaEvolucion.getInstitucionBasica().getLogoJsp();
			
					dtoGeneral.setUsuarioProcesa(formaEvolucion.getUsuarioResumen().getNombreUsuario());
					dtoGeneral.setRazonSocial(formaEvolucion.getInstitucionBasica().getRazonSocial());
					String centrofechaHora=formaEvolucion.getPaciente().getNombreCentroAtencion()+" "+UtilidadFecha.conversionFormatoFechaDescripcionMesCompleto(UtilidadFecha.getFechaActual())+" "+UtilidadFecha.getHoraActual();
					dtoGeneral.setCentroFechaHoraActual(centrofechaHora);
					dtoGeneral.setNit(formaEvolucion.getInstitucionBasica().getNit());
					dtoGeneral.setDireccion(formaEvolucion.getInstitucionBasica().getDireccion());
					dtoGeneral.setTelefono(formaEvolucion.getInstitucionBasica().getTelefono());
					dtoGeneral.setDescripcionCentroAtencion(formaEvolucion.getPaciente().getNombreCentroAtencion());
					dtoGeneral.setTitulo(formaEvolucion.getTitulo());
					dtoGeneral.setTituloTipoCita("CITA "+formaEvolucion.getTipoCitaFormateado()+" - "+formaEvolucion.getPlantillaBase());
					dtoGeneral.setPiePaginaHistoriaClinica(formaEvolucion.getInstitucionBasica().getPieHistoriaClinica());
					dtoGeneral.setDatosProfesional(formaEvolucion.getUsuarioResumen().getNombreUsuario());
					
					//PARAMETROS DEL PACIENTE
					dtoGeneral.setFechaNacimientoPaciente(formaEvolucion.getFechaNacimientoPacienteFormateado());
					dtoGeneral.setNombreCompletoPaciente(formaEvolucion.getPaciente().getNombreCompleto());
					dtoGeneral.setCiudadResidenciaPaciente(formaEvolucion.getPaciente().getNombreCiudadResidencia());
					dtoGeneral.setTelefonoFijoPaciente(formaEvolucion.getPaciente().getTelefonoFijo());
					dtoGeneral.setFechaInicioTratamientoPaciente(formaEvolucion.getPaciente().getFechaIngreso());
					dtoGeneral.setServicio(formaEvolucion.getPaciente().getNombreViaIngreso());
					dtoGeneral.setResponsable(formaEvolucion.getPaciente().getNombresResponsables());
					dtoGeneral.setIdentificacionPaciente(formaEvolucion.getPaciente().getTipoIdentificacion()+". "+formaEvolucion.getPaciente().getNumeroIdentificacion());
					dtoGeneral.setEdadPaciente(formaEvolucion.getPaciente().getEdadPaciente());
					dtoGeneral.setSexoPaciente(formaEvolucion.getPaciente().getSexo());
					dtoGeneral.setTelefonoCelularPaciente(formaEvolucion.getPaciente().getTelefonoCelular());
					dtoGeneral.setFechaFinTratamientoPaciente(formaEvolucion.getPaciente().getFechaEgreso());
					dtoGeneral.setDireccionPaciente(formaEvolucion.getPaciente().getDireccion());
					
					boolean existeLogo = existeLogo(rutaLogo);
					
					if (existeLogo) {
						if (ubicacionLogo.equals(ConstantesIntegridadDominio.acronimoUbicacionDerecha)) {
							dtoGeneral.setLogoDerecha(rutaLogo);
							dtoGeneral.setLogoIzquierda(null);
						} else if (ubicacionLogo.equals(ConstantesIntegridadDominio.acronimoUbicacionIzquierda)) {
							dtoGeneral.setLogoIzquierda(rutaLogo);
							dtoGeneral.setLogoDerecha(null);
						}
					}else{
						dtoGeneral.setLogoDerecha(null);
						dtoGeneral.setLogoIzquierda(null);
					}
		}else{
				
			RUTA_LOGO = "../"+ formaValoracion.getInstitucionBasica().getLogoJsp();
					
					//PARAMETROS DEL ENCABEZADO Y PIE DE PAGINA DEL REPORTE
					String ubicacionLogo = formaValoracion.getInstitucionBasica().getUbicacionLogo();
					String rutaLogo = "../"+formaValoracion.getInstitucionBasica().getLogoJsp();
			
					dtoGeneral.setUsuarioProcesa(formaValoracion.getUsuarioResumen().getNombreUsuario());
					dtoGeneral.setRazonSocial(formaValoracion.getInstitucionBasica().getRazonSocial());
					String centrofechaHora=formaValoracion.getPaciente().getNombreCentroAtencion()+" "+UtilidadFecha.conversionFormatoFechaDescripcionMesCompleto(UtilidadFecha.getFechaActual())+" "+UtilidadFecha.getHoraActual();
					dtoGeneral.setCentroFechaHoraActual(centrofechaHora);
					dtoGeneral.setNit(formaValoracion.getInstitucionBasica().getNit());
					dtoGeneral.setDireccion(formaValoracion.getInstitucionBasica().getDireccion());
					dtoGeneral.setTelefono(formaValoracion.getInstitucionBasica().getTelefono());
					dtoGeneral.setDescripcionCentroAtencion(formaValoracion.getPaciente().getNombreCentroAtencion());
					dtoGeneral.setTitulo(formaValoracion.getTitulo());
					dtoGeneral.setTituloTipoCita("CITA "+formaValoracion.getTipoCitaFormateado()+" - "+formaValoracion.getPlantillaBase());
					
					dtoGeneral.setPiePaginaHistoriaClinica(formaValoracion.getInstitucionBasica().getPieHistoriaClinica());
					dtoGeneral.setDatosProfesional(formaValoracion.getUsuarioResumen().getNombreUsuario());
					
					//PACIENTE
					dtoGeneral.setFechaNacimientoPaciente(formaValoracion.getFechaNacimientoPacienteFormateado());
					dtoGeneral.setNombreCompletoPaciente(formaValoracion.getPaciente().getNombreCompleto());
					dtoGeneral.setCiudadResidenciaPaciente(formaValoracion.getPaciente().getNombreCiudadResidencia());
					dtoGeneral.setTelefonoFijoPaciente(formaValoracion.getPaciente().getTelefonoFijo());
					dtoGeneral.setFechaInicioTratamientoPaciente(formaValoracion.getPaciente().getFechaIngreso());
					dtoGeneral.setServicio(formaValoracion.getPaciente().getNombreViaIngreso());
					dtoGeneral.setResponsable(formaValoracion.getPaciente().getNombresResponsables());
					dtoGeneral.setIdentificacionPaciente(formaValoracion.getPaciente().getTipoIdentificacion()+". "+formaValoracion.getPaciente().getNumeroIdentificacion());
					dtoGeneral.setEdadPaciente(formaValoracion.getPaciente().getEdadPaciente());
					dtoGeneral.setSexoPaciente(formaValoracion.getPaciente().getSexo());
					dtoGeneral.setTelefonoCelularPaciente(formaValoracion.getPaciente().getTelefonoCelular());
					dtoGeneral.setFechaFinTratamientoPaciente(formaValoracion.getPaciente().getFechaEgreso());
					dtoGeneral.setDireccionPaciente(formaValoracion.getPaciente().getDireccion());
					
					boolean existeLogo = existeLogo(rutaLogo);
					
					if (existeLogo) {
						if (ubicacionLogo.equals(ConstantesIntegridadDominio.acronimoUbicacionDerecha)) {
							dtoGeneral.setLogoDerecha(rutaLogo);
							dtoGeneral.setLogoIzquierda(null);
						} else if (ubicacionLogo.equals(ConstantesIntegridadDominio.acronimoUbicacionIzquierda)) {
							dtoGeneral.setLogoIzquierda(rutaLogo);
							dtoGeneral.setLogoDerecha(null);
						}
					}else{
						dtoGeneral.setLogoDerecha(null);
						dtoGeneral.setLogoIzquierda(null);
					}
		}
		
		
			
		String nombreContexto="";
			if(formaValoracion!=null)									
				nombreContexto=formaValoracion.getNombreContexto();
			else
				nombreContexto=formaEvolucion.getNombreContexto();
			
			/*SE RECORREN LAS PLANTILLAS Y SE ORGANIZA LA INFORMACION A MOSTRAR EN CADA SECCION*/
			for(DtoPlantilla dtoPlantilla :  plantillas){
				/*                SE ITERAN LAS SECCIONES FIJAS DE LA PLANTILLA               */
				for(DtoSeccionFija dtoSecciones :  dtoPlantilla.getSeccionesFijas()){
					dtoSecciones.setNombreS(dtoSecciones.getNombreSeccion());
					if(dtoSecciones.getCodigoSeccion()==ConstantesCamposParametrizables.seccionFijaInformacionGeneral){
						String detalle="Fecha de Consulta: "+dtoPlantilla.getDtoValoracionOdo().getFechaConsulta()
						+"     Hora de Consulta: "+dtoPlantilla.getDtoValoracionOdo().getHoraConsulta()
						+"     Edad del Paciente: "+dtoPlantilla.getDtoValoracionOdo().getEdadPaciente()+" Años";
						dtoSecciones.setDetalle(detalle);
					}else
						if(dtoSecciones.getCodigoSeccion()==ConstantesCamposParametrizables.seccionFijaMotivoConsulta){
							dtoSecciones.setDetalle(dtoPlantilla.getDtoValoracionOdo().getMotivoConsulta());
						}else
							if(dtoSecciones.getCodigoSeccion()==ConstantesCamposParametrizables.seccionFijaEnfermedadActual){
								dtoSecciones.setDetalle(dtoPlantilla.getDtoValoracionOdo().getEnfermedadActual());
							}
							else
								if(dtoSecciones.getCodigoSeccion()==ConstantesCamposParametrizables.seccionFijaFinalidadConsulta){
									dtoSecciones.setDetalle(dtoPlantilla.getDtoValoracionOdo().getNombreFinalidadConsulta());
								}else
									if(dtoSecciones.getCodigoSeccion()==ConstantesCamposParametrizables.seccionFijaObservaciones){
										if(!UtilidadTexto.isEmpty(dtoPlantilla.getDtoValoracionOdo().getObservaciones()))
											dtoSecciones.setDetalle(dtoPlantilla.getDtoValoracionOdo().getObservaciones());
										else
											dtoSecciones.setDetalle("");
									}
									else
										if(dtoSecciones.getCodigoSeccion()==ConstantesCamposParametrizables.seccionFijaCausaExterna){
											dtoSecciones.setDetalle(dtoPlantilla.getDtoValoracionOdo().getNombreCausaExterna());
										}
										else
											if(dtoSecciones.getCodigoSeccion()==ConstantesCamposParametrizables.seccionFijaDiagnosticos){
												String detalle="";
												int contDiagnostico=0;
												int k=0;
												for(DtoValDiagnosticosOdo dtoDiagnosticos :  dtoPlantilla.getDtoValoracionOdo().getDiagnosticos()){
													
													if(UtilidadTexto.getBoolean(dtoDiagnosticos.getPrincipal()))
													{
														detalle=detalle+"Dx. Principal: ";
													}
													else
													{
														contDiagnostico++;
														detalle=detalle+"Dx. Relacionado N° "+contDiagnostico+ ": ";
													}
													detalle=detalle+"("+dtoDiagnosticos.getAcronimoDiagnostico()+"-"+dtoDiagnosticos.getTipoCleDiagnostico()+") "+dtoDiagnosticos.getNombreDiagnostico()+"\n";
													if(UtilidadTexto.getBoolean(dtoDiagnosticos.getPrincipal())){
														detalle=detalle+"Tipo Diagnóstico: "+dtoPlantilla.getDtoValoracionOdo().getNombreTipoDiagnostico();
													}
													if(k!=dtoPlantilla.getDtoValoracionOdo().getDiagnosticos().size()-1&&UtilidadTexto.getBoolean(dtoDiagnosticos.getPrincipal()))
														detalle=detalle+"\n";
													k++;
												}
												dtoSecciones.setDetalle(detalle);
											}
											else{
												//SI ES OTRA SECCION SE RECORRE LOS ELEMENTOS Y SE CREA EL DETALLE DE LA SECCION
												String detalle="";
												String rutaImagen=null;
												ArrayList<DtoFormatoPlantillaDetalleHallazgosPlanTratamientoInicial> dtohallazgos= null;
												ArrayList<DtoFormatoPlantillaHallazgosPiezasDentales> dtoHallazgosPiezas= null;
												ArrayList<DtoFormatoPlantillaHallazgosPiezasDentales> dtoHallazgosBoca= null;
												ArrayList<DtoTratamientoInterno> dtoTratamientoInterno= null;
												ArrayList<DtoTratamientoExterno> dtoTratamientoExterno= null;
												ArrayList<DtoFormatoPlantillaOrdenarColumnasPDF> dtoOrdenarColumnas= null;
												ArrayList<DtoFormatoPlantillaProgServCitaAsignada> dtoProgServCitaAsign= null;
												DtoFormatoPlantillaOrdenarColumnasPDF dtoOrdenar=null;
												if(!Utilidades.isEmpty(dtoSecciones.getElementos()))
												{
													//variable que indica si la seccion tiene valores y se debe mostrar o no en el reporte
													boolean imprime=false;
													for(DtoElementoParam dtoElementos :  dtoSecciones.getElementos()){
														
														//SI EL ELEMENTO ES SECCION SE GUARDAN SUS CAMPOS EN UN STRING 
														if(dtoElementos.isSeccion()==true){
															DtoSeccionParametrizable seccion = (DtoSeccionParametrizable)dtoElementos;
															int columnasSeccion=seccion.getColumnasSeccion();
															int contador=0;
															if(seccion.isVisible())
															{
																imprime=true;
															}
															
														//SE RECORREN LOS CAMPOS DEL ELEMENTO SECCION Y SE CONCATENAN EN UN SOLO STRING DETALLE	
														for(DtoCampoParametrizable dtoCampos :  dtoElementos.getCampos()){
																if(dtoCampos.isUnicoXFila()||dtoCampos.getColumnasOcupadas()==columnasSeccion)
																	detalle=detalle+"\n\n";
																detalle=detalle+"<b>"+dtoCampos.getEtiqueta()+"</b>";
																if(!UtilidadTexto.isEmpty(dtoCampos.getValor())){
																	
																	if(dtoCampos.getTipoHtml().equals(ConstantesCamposParametrizables.campoTipoCheckBox)||dtoCampos.getTipoHtml().equals(ConstantesCamposParametrizables.campoTipoSelect)){
																		String valoresCamposCheck="";
																		for(DtoOpcionCampoParam dtoOpciones :  dtoCampos.getOpciones()){
																			if(dtoOpciones.getSeleccionado().equals(ConstantesBD.acronimoSi)){
																				imprime=true;
																				valoresCamposCheck=valoresCamposCheck+dtoOpciones.getValor()+" ";
																			}
																		}
																		if(UtilidadTexto.isEmpty(dtoCampos.getEtiqueta())){
																			detalle=detalle+valoresCamposCheck;
																		}else{
																			detalle=detalle+": "+valoresCamposCheck;
																		}
																		
																	}else{
																		if(dtoCampos.getValor().contains("@")){
																			String[] valor=dtoCampos.getValor().split("@");
																			if(UtilidadTexto.isEmpty(dtoCampos.getEtiqueta())){
																				detalle=detalle+valor[valor.length-1];
																			}else{
																				detalle=detalle+"<b>:</b> "+valor[valor.length-1];
																			}
																		}else{
																			if(UtilidadTexto.isEmpty(dtoCampos.getEtiqueta())){
																				detalle=detalle+dtoCampos.getValor();
																			}else{
																				detalle=detalle+"<b>:</b> "+dtoCampos.getValor();
																			}
																		}
																		imprime=true;
																	}
																	
																}
																if(dtoCampos.getColumnasOcupadas()==columnasSeccion
																		||dtoCampos.getColumnasOcupadas()==0||dtoCampos.isUnicoXFila()){
																	detalle=detalle+"\n";
																	contador=0;
																}else{
																	contador+=dtoCampos.getColumnasOcupadas();
																	if(contador>=columnasSeccion){
																		detalle=detalle+"\n";
																		contador=0;
																	}
																	else
																		detalle=detalle+"* ";
																}
																
															}
															
															
															if(!Utilidades.isEmpty(dtoElementos.getSecciones())){
																for(DtoSeccionParametrizable dtoSeccionParametrizable :  dtoElementos.getSecciones()){
																	detalle=detalle+dtoSeccionParametrizable.getDescripcion()+"\n";
																	if(!Utilidades.isEmpty(dtoSeccionParametrizable.getCampos())){
																		for(DtoCampoParametrizable dtoCampoParametrizable :  dtoSeccionParametrizable.getCampos()){
																			detalle=detalle+dtoCampoParametrizable.getEtiqueta();
																			
																			if(!UtilidadTexto.isEmpty(dtoCampoParametrizable.getValor())){
																				imprime=true;
																				if(dtoCampoParametrizable.getValor().contains("@")){
																					String[] valor=dtoCampoParametrizable.getValor().split("@");
																					if(UtilidadTexto.isEmpty(dtoCampoParametrizable.getEtiqueta())){
																						detalle=detalle+valor[valor.length-1];
																					}else{
																						detalle=detalle+": "+valor[valor.length-1];
																					}
																				}else{
																					if(UtilidadTexto.isEmpty(dtoCampoParametrizable.getEtiqueta())){
																						detalle=detalle+dtoCampoParametrizable.getValor();
																					}else{
																						detalle=detalle+": "+dtoCampoParametrizable.getValor();
																					}
																				}
																				
																			}
																			if(dtoCampoParametrizable.getColumnasOcupadas()==columnasSeccion
																					||dtoCampoParametrizable.getColumnasOcupadas()==0){
																				detalle=detalle+"\n";
																			}else{
																				contador+=dtoCampoParametrizable.getColumnasOcupadas();
																				if(contador>=columnasSeccion){
																					detalle=detalle+"\n";
																					contador=0;
																				}
																				else
																					detalle=detalle+"* ";
																			}
																		}
																	}
																	
																}
															}
															
															
															//SE ORGANIZA POR COLUMNAS EL STRING CREADO ANTERIORMENTE, SI SE AGREGAN MAS DE
															//8 COLUMNAS SOLO SE DEBE AGREGAR OTRA VALIDACION COMO LA SIGUIENTE
															dtoOrdenar = new DtoFormatoPlantillaOrdenarColumnasPDF();
															dtoOrdenarColumnas=new ArrayList<DtoFormatoPlantillaOrdenarColumnasPDF>();
															
															String[] ordenFilas=detalle.split("\n");
															for(int i=0;i<ordenFilas.length;i++)
															{
																dtoOrdenar = new DtoFormatoPlantillaOrdenarColumnasPDF();
																dtoOrdenar.setTamanoSeccion(Integer.toString(columnasSeccion));	
																String ordenF=ordenFilas[i];
																
																if(ordenFilas[i].contains("*")){
																	
																	String[] ordenColumnas=ordenF.split("\\*");
																	if(ordenColumnas.length==1)
																	{
																		dtoOrdenar.setColumnauno(ordenColumnas[0]);
																	}else
																	if(ordenColumnas.length==2)	
																	{
																		if(columnasSeccion>2){
																			dtoOrdenar.setColumnadobleuno(ordenColumnas[0]);
																			dtoOrdenar.setColumnadobledos(ordenColumnas[1]);
																		}else{
																			dtoOrdenar.setColumnauno(ordenColumnas[0]);
																			dtoOrdenar.setColumnados(ordenColumnas[1]);
																		}
																	}
																	else
																	if(ordenColumnas.length==3)	
																	{
																		if(columnasSeccion>3){
																			dtoOrdenar.setColumnatripleuno(ordenColumnas[0]);
																			dtoOrdenar.setColumnatripledos(ordenColumnas[1]);
																			dtoOrdenar.setColumnatripletres(ordenColumnas[2]);
																		}else{
																			dtoOrdenar.setColumnauno(ordenColumnas[0]);
																			dtoOrdenar.setColumnados(ordenColumnas[1]);
																			dtoOrdenar.setColumnatres(ordenColumnas[2]);
																		}
																	}
																	else
																	if(ordenColumnas.length==4)	
																	{
																		dtoOrdenar.setColumnauno(ordenColumnas[0]);
																		dtoOrdenar.setColumnados(ordenColumnas[1]);
																		dtoOrdenar.setColumnatres(ordenColumnas[2]);
																		dtoOrdenar.setColumnacuatro(ordenColumnas[3]);
																	}
																	else
																	if(ordenColumnas.length==5)	
																	{
																		dtoOrdenar.setColumnauno(ordenColumnas[0]);
																		dtoOrdenar.setColumnados(ordenColumnas[1]);
																		dtoOrdenar.setColumnatres(ordenColumnas[2]);
																		dtoOrdenar.setColumnacuatro(ordenColumnas[3]);
																		dtoOrdenar.setColumnacinco(ordenColumnas[4]);
																	}
																	else
																	if(ordenColumnas.length==6)	
																	{
																		dtoOrdenar.setColumnauno(ordenColumnas[0]);
																		dtoOrdenar.setColumnados(ordenColumnas[1]);
																		dtoOrdenar.setColumnatres(ordenColumnas[2]);
																		dtoOrdenar.setColumnacuatro(ordenColumnas[3]);
																		dtoOrdenar.setColumnacinco(ordenColumnas[4]);
																		dtoOrdenar.setColumnaseis(ordenColumnas[5]);
																	}
																	else
																	if(ordenColumnas.length==7)	
																	{
																		dtoOrdenar.setColumnauno(ordenColumnas[0]);
																		dtoOrdenar.setColumnados(ordenColumnas[1]);
																		dtoOrdenar.setColumnatres(ordenColumnas[2]);
																		dtoOrdenar.setColumnacuatro(ordenColumnas[3]);
																		dtoOrdenar.setColumnacinco(ordenColumnas[4]);
																		dtoOrdenar.setColumnaseis(ordenColumnas[5]);
																		dtoOrdenar.setColumnasiete(ordenColumnas[6]);
																	}
																	else
																	if(ordenColumnas.length==8)	
																	{
																		dtoOrdenar.setColumnauno(ordenColumnas[0]);
																		dtoOrdenar.setColumnados(ordenColumnas[1]);
																		dtoOrdenar.setColumnatres(ordenColumnas[2]);
																		dtoOrdenar.setColumnacuatro(ordenColumnas[3]);
																		dtoOrdenar.setColumnacinco(ordenColumnas[4]);
																		dtoOrdenar.setColumnaseis(ordenColumnas[5]);
																		dtoOrdenar.setColumnasiete(ordenColumnas[6]);
																		dtoOrdenar.setColumnaocho(ordenColumnas[7]);
																	}
																}else{
																	dtoOrdenar.setColumnaunica(ordenF.toString());
																}
																dtoOrdenarColumnas.add(dtoOrdenar);
															}
															
															detalle="";	
															if(imprime==true)
																dtoSecciones.setDtoOrdenarColumnas(dtoOrdenarColumnas);
															else
																dtoSecciones.setDtoOrdenarColumnas(null);
														}
														
														//SI EL ELEMENTO ES COMPONENTE SE VALIDA EL TIPO DE COMPONENTE
														else
															if(dtoElementos.isComponente()==true){
																
																DtoComponente componente = (DtoComponente)dtoElementos;
																String imagen=null;
																
																switch(componente.getCodigoTipo())
																{
																	case ConstantesCamposParametrizables.tipoComponenteIndicePlaca:
																		dtoSecciones.setNombreS(dtoElementos.getDescripcion());
																		//VALIDAR SI SON DE EVOLUCION O DE VALORACION
																		if(formaEvolucion!=null){
																			DtoComponenteIndicePlaca dcip=dtoPlantilla.getDtoEvolucionOdo().getDtoIndicePlaca();
																			imagen=dcip.getImagen();
																			
																		}else{
																			DtoComponenteIndicePlaca dcip=dtoPlantilla.getDtoValoracionOdo().getDtoIndicePlaca();
																			imagen=dcip.getImagen();
																			
																		}
																		
																		if(!UtilidadTexto.isEmpty(imagen)){
																			rutaImagen=nombreContexto+"/imagenesOdontologia/indicePlaca/"+imagen;
																			BufferedImage img = null;
																		      try {
																		        img = ImageIO.read(new File(rutaImagen));
																		      } catch (IOException ex) {
																		    	  detalle="No se pudo leer la imagen";
																		    	  imprime=true;
																		    	  rutaImagen=null;
																		      }
																		}
																		else{
																			detalle="No se ingresó Indice de placa";
																			imprime=true;
																			rutaImagen=null;
																		}
																		
																		
																		
																	break;
																	case ConstantesCamposParametrizables.tipoComponenteAntecendentesOdontologicos:
																		dtoSecciones.setNombreS(dtoElementos.getDescripcion());
																		
																		//CARGAR TRATAMIENTOS INTERNOS Y EXTERNOS
																		dtoTratamientoInterno = new ArrayList<DtoTratamientoInterno>();
																		DtoTratamientoInterno dtoTraIntern=null;
																		
																		dtoTratamientoExterno = new ArrayList<DtoTratamientoExterno>();
																		DtoTratamientoExterno dtoTraExtern=null;
																		
																		//VALIDAR SI SON DE EVOLUCION O DE VALORACION
																		if(formaEvolucion!=null){
																			
																			//tratamiento interno plantilla evolucion
																			for(DtoTratamientoInterno dtoTrataInterno :  dtoPlantilla.getDtoEvolucionOdo().getInfoAntecedentesOdo().getAntecedenteOdon().getTratamientosInternos()){
																				dtoTraIntern=new DtoTratamientoInterno();
																				dtoTraIntern.setFechaInicio(dtoTrataInterno.getFechaInicio());
																				dtoTraIntern.setFechaFinal(dtoTrataInterno.getFechaFinal());
																				if(dtoTrataInterno.getNombrePrograma().equals(""))
																					dtoTraIntern.setNombrePrograma(dtoTrataInterno.getNombreServicio());
																				else
																					dtoTraIntern.setNombrePrograma(dtoTrataInterno.getNombrePrograma());
																				
																				if(dtoTrataInterno.getDescripcionPiezaDen().equals(""))
																					dtoTraIntern.setDescripcionPiezaDen("");
																				else
																					dtoTraIntern.setDescripcionPiezaDen(dtoTrataInterno.getDescripcionPiezaDen());
																				
																				dtoTraIntern.setDescripcionEsp(dtoTrataInterno.getDescripcionEsp());
																				dtoTratamientoInterno.add(dtoTraIntern);
																			}

																			//tratamiento externo plantilla evolucion
																			for(DtoTratamientoExterno dtoTrataExterno :  dtoPlantilla.getDtoEvolucionOdo().getInfoAntecedentesOdo().getAntecedenteOdon().getTratamientosExternos()){
																				dtoTraExtern=new DtoTratamientoExterno();
																				dtoTraExtern.setFechaInicio(dtoTrataExterno.getFechaInicio());
																				dtoTraExtern.setFechaFinal(dtoTrataExterno.getFechaFinal());
																				dtoTraExtern.setProgramaServicio(dtoTrataExterno.getProgramaServicio());
																				if(dtoTrataExterno.getDescripcionPiezaDen().equals(""))
																					dtoTraExtern.setDescripcionPiezaDen("");
																				else
																					dtoTraExtern.setDescripcionPiezaDen(dtoTrataExterno.getDescripcionPiezaDen());
																				
																				dtoTraExtern.setDescripcionEsp(dtoTrataExterno.getDescripcionEsp());
																				dtoTratamientoExterno.add(dtoTraExtern);
																			}
																		}else
																		{
																			//tratamiento interno plantilla valoracion
																			for(DtoTratamientoInterno dtoTrataInterno :  dtoPlantilla.getDtoValoracionOdo().getInfoAntecedentesOdo().getAntecedenteOdon().getTratamientosInternos()){
																				dtoTraIntern=new DtoTratamientoInterno();
																				dtoTraIntern.setFechaInicio(dtoTrataInterno.getFechaInicio());
																				dtoTraIntern.setFechaFinal(dtoTrataInterno.getFechaFinal());
																				if(dtoTrataInterno.getNombrePrograma().equals(""))
																					dtoTraIntern.setNombrePrograma(dtoTrataInterno.getNombreServicio());
																				else
																					dtoTraIntern.setNombrePrograma(dtoTrataInterno.getNombrePrograma());
																				
																				if(dtoTrataInterno.getDescripcionPiezaDen().equals(""))
																					dtoTraIntern.setDescripcionPiezaDen("");
																				else
																					dtoTraIntern.setDescripcionPiezaDen(dtoTrataInterno.getDescripcionPiezaDen());
																				
																				dtoTraIntern.setDescripcionEsp(dtoTrataInterno.getDescripcionEsp());
																				dtoTratamientoInterno.add(dtoTraIntern);
																			}
																			
																			//tratamiento externo plantilla valoracion
																			for(DtoTratamientoExterno dtoTrataExterno :  dtoPlantilla.getDtoValoracionOdo().getInfoAntecedentesOdo().getAntecedenteOdon().getTratamientosExternos()){
																				dtoTraExtern=new DtoTratamientoExterno();
																				dtoTraExtern.setFechaInicio(dtoTrataExterno.getFechaInicio());
																				dtoTraExtern.setFechaFinal(dtoTrataExterno.getFechaFinal());
																				dtoTraExtern.setProgramaServicio(dtoTrataExterno.getProgramaServicio());
																				if(dtoTrataExterno.getDescripcionPiezaDen().equals(""))
																					dtoTraExtern.setDescripcionPiezaDen("");
																				else
																					dtoTraExtern.setDescripcionPiezaDen(dtoTrataExterno.getDescripcionPiezaDen());
																				
																				dtoTraExtern.setDescripcionEsp(dtoTrataExterno.getDescripcionEsp());
																				dtoTratamientoExterno.add(dtoTraExtern);
																			}
																		}
																		
																		dtoSecciones.setDtoTratamientosInternos(dtoTratamientoInterno);
																		dtoSecciones.setDtoTratamientosExternos(dtoTratamientoExterno);
																		
																		
																		//ORDENAR ANTECEDENTES ODONTOLOGICOS
																		dtoOrdenarColumnas = new ArrayList<DtoFormatoPlantillaOrdenarColumnasPDF>();
																		int contador=0;
																		
																		for(DtoElementoParam dtoElemenPrimario :  dtoSecciones.getElementos()){
																			for(DtoElementoParam dtoElementosSecund :  dtoElemenPrimario.getElementos()){
																				int columnasSeccion=0;
																				if(dtoElementosSecund.isSeccion()){
																					DtoSeccionParametrizable seccion = (DtoSeccionParametrizable)dtoElementosSecund;
																					columnasSeccion=seccion.getColumnasSeccion();
																				}
																				for(DtoCampoParametrizable dtoCampos :  dtoElementosSecund.getCampos()){
																					if(dtoCampos.isUnicoXFila()||dtoCampos.getColumnasOcupadas()==columnasSeccion)
																						detalle=detalle+" \n";
																					detalle=detalle+"<b>"+dtoCampos.getEtiqueta()+"</b>";
																					if(!UtilidadTexto.isEmpty(dtoCampos.getValor())){
																						imprime=true;
																						if(dtoCampos.getValor().contains("@")){
																							String[] valor=dtoCampos.getValor().split("@");
																							if(UtilidadTexto.isEmpty(dtoCampos.getEtiqueta())){
																								detalle=detalle+valor[valor.length-1];
																							}else{
																								detalle=detalle+"<b>:</b> "+valor[valor.length-1];
																							}
																						}else{
																							if(UtilidadTexto.isEmpty(dtoCampos.getEtiqueta())){
																								detalle=detalle+dtoCampos.getValor();
																							}else{
																								detalle=detalle+"<b>:</b> "+dtoCampos.getValor();
																							}
																						}
																						
																					}
																					if(dtoCampos.getColumnasOcupadas()==columnasSeccion
																							||dtoCampos.getColumnasOcupadas()==0||dtoCampos.isUnicoXFila()){
																						detalle=detalle+"\n";
																						contador=0;
																					}else{
																						contador+=dtoCampos.getColumnasOcupadas();
																						if(contador>=columnasSeccion){
																							detalle=detalle+"\n";
																							contador=0;
																						}
																						else
																							detalle=detalle+"*";
																					}
																					
																				}
																				
																				dtoOrdenar = new DtoFormatoPlantillaOrdenarColumnasPDF();
																																							
																				String[] ordenFilas=detalle.split("\n");
																				for(int i=0;i<ordenFilas.length;i++)
																				{
																					dtoOrdenar = new DtoFormatoPlantillaOrdenarColumnasPDF();
																					dtoOrdenar.setTamanoSeccion(Integer.toString(columnasSeccion));	
																					String ordenF=ordenFilas[i];
																					
																					if(ordenFilas[i].contains("*")){
																						
																						String[] ordenColumnas=ordenF.split("\\*");
																						if(ordenColumnas.length==1)
																						{
																							dtoOrdenar.setColumnauno(ordenColumnas[0]);
																						}else
																						if(ordenColumnas.length==2)	
																						{
																							dtoOrdenar.setColumnauno(ordenColumnas[0]);
																							dtoOrdenar.setColumnados(ordenColumnas[1]);
																						}
																					}else{
																						dtoOrdenar.setColumnauno(ordenF.toString());
																					}
																					dtoOrdenarColumnas.add(dtoOrdenar);
																				}
																				
																				detalle="";																																							
																			}
																			
																		}
																		detalle="";	
																		if(imprime==true)
																			dtoSecciones.setDtoOrdenarColumnas(dtoOrdenarColumnas);
																		else
																			dtoSecciones.setDtoOrdenarColumnas(null);
																		
																		
																	break;
																	case ConstantesCamposParametrizables.tipoComponenteOdontogramaDiag:
																		dtoSecciones.setNombreS(dtoElementos.getDescripcion());
																		InfoOdontograma io=dtoPlantilla.getDtoValoracionOdo().getInfoOdontograma();
																		imagen=io.getInfoPlanTrata().getImagen();
																	
																		/*Se crea el dto para enviar a ireport de hallazgos de la tabla Detalle De Hallazgos y Plan de 
																		  Tratamiento Inicial del componente odontograma de diagnostico*/
																		DtoFormatoPlantillaDetalleHallazgosPlanTratamientoInicial dto=null;
																		dtohallazgos=new ArrayList<DtoFormatoPlantillaDetalleHallazgosPlanTratamientoInicial>();
																		for(InfoDetallePlanTramiento dtoInfoDetallePlanTrata :  io.getInfoPlanTrata().getSeccionHallazgosDetalle()){
																			for(InfoHallazgoSuperficie dtoinfoHallazgo :  dtoInfoDetallePlanTrata.getDetalleSuperficie()){
																				dto=new DtoFormatoPlantillaDetalleHallazgosPlanTratamientoInicial();
																				dto.setPiezaDental(dtoInfoDetallePlanTrata.getPieza().getCodigo());
																				dto.setFechaHora(dtoinfoHallazgo.getInfoRegistroHallazgo().getFechaModifica()+" "+dtoinfoHallazgo.getInfoRegistroHallazgo().getHoraModifica());
																				dto.setHallazgo(dtoinfoHallazgo.getHallazgoREQUERIDO().getNombre());
																				if(UtilidadTexto.isEmpty(dtoinfoHallazgo.getSuperficieOPCIONAL().getNombre()))
																				{
																					dto.setSuperficie("Todas las superficies");
																				}
																				else
																				{
																					dto.setSuperficie(dtoinfoHallazgo.getSuperficieOPCIONAL().getNombre());
																				}
																				
																				//Recorre los programas o servicios del plan de tratamiento en el componente odontograma diagnostico
																				String programas="";
																				for(InfoProgramaServicioPlan dtoinfoProgramaServicio :  dtoinfoHallazgo.getProgramasOservicios()){
																					if(dtoinfoProgramaServicio.getCodigoPkProgramaServicio().intValue() > 0)
																					{
																						if(dtoinfoProgramaServicio.getExisteBD().isActivo())
																						{
																							programas=programas+"\n"+dtoinfoProgramaServicio.getNombreProgramaServicio();
																						}
																					}else
																					{
																						//Recorrer Servicios de Cada Programa
																						for(InfoServicios dtoinfoServicio :  dtoinfoProgramaServicio.getListaServicios()){
																							programas=programas+"\n"+dtoinfoServicio.getCodigoMostrar()+" "+dtoinfoServicio.getServicio().getNombre();
																						}
																					}
																					
																				}
																				
																				dto.setProgramaServicio(programas);
																				dto.setClasificacion(ValoresPorDefecto.getIntegridadDominio(dtoinfoHallazgo.getClasificacion().getValue()).toString());
																				dtohallazgos.add(dto);
																			}
																		}
																		dtoSecciones.setDtohallazgos(dtohallazgos);
																		
																		/*Se crea el dto para enviar a ireport para la tabla Hallazgos en Piezas Dentales
																			del componente odontograma de diagnostico*/
																		DtoFormatoPlantillaHallazgosPiezasDentales dtoPiezasDentales=null;
																		dtoHallazgosPiezas=new ArrayList<DtoFormatoPlantillaHallazgosPiezasDentales>();
																		int reg=1;
																		for(InfoDetallePlanTramiento dtoInfoDetallePlanTrata :  io.getInfoPlanTrata().getSeccionOtrosHallazgos()){
																			for(InfoHallazgoSuperficie dtoDetalleSuperficie :  dtoInfoDetallePlanTrata.getDetalleSuperficie()){
																				dtoPiezasDentales=new DtoFormatoPlantillaHallazgosPiezasDentales();
																				dtoPiezasDentales.setNumero(reg);
																				dtoPiezasDentales.setPiezaDental(dtoInfoDetallePlanTrata.getPieza().getCodigo());
																				dtoPiezasDentales.setFechaHora(dtoDetalleSuperficie.getInfoRegistroHallazgo().getFechaModifica()+" "+dtoDetalleSuperficie.getInfoRegistroHallazgo().getHoraModifica());
																				dtoPiezasDentales.setHallazgo(dtoDetalleSuperficie.getHallazgoREQUERIDO().getNombre());
																				if(dtoDetalleSuperficie.getHallazgoREQUERIDO().getCodigo2() == ComponenteOdontograma.codigoTipoHallazgoSuper)
																				{
																					dtoPiezasDentales.setSuperficie(dtoDetalleSuperficie.getSuperficieOPCIONAL().getNombre());
																				}else
																				{
																					dtoPiezasDentales.setSuperficie("Todas las superficies");
																				}
																				
																				//Recorre los programas o servicios de hallazgos en piezas dentales en el componente odontograma diagnostico
																				String programas="";
																				for(InfoProgramaServicioPlan dtoinfoProgramaServicio :  dtoDetalleSuperficie.getProgramasOservicios()){
																					if(dtoinfoProgramaServicio.getCodigoPkProgramaServicio().intValue() > 0)
																					{
																						if(dtoinfoProgramaServicio.getExisteBD().isActivo())
																						{
																							programas=programas+"\n"+dtoinfoProgramaServicio.getNombreProgramaServicio();
																						}
																					}else
																					{
																						//Recorrer Servicios de Cada Programa
																						for(InfoServicios dtoinfoServicio :  dtoinfoProgramaServicio.getListaServicios()){
																							programas=programas+"\n"+dtoinfoServicio.getCodigoMostrar()+" "+dtoinfoServicio.getServicio().getNombre();
																						}
																					}
																					
																				}
																				dtoPiezasDentales.setProgramaServicio(programas);
																				dtoHallazgosPiezas.add(dtoPiezasDentales);
																			}
																			reg++;
																		}
																		
																		dtoSecciones.setDtohallazgosPiezasDentales(dtoHallazgosPiezas);
																		
																		/*Se crea el dto para enviar a ireport para la tabla Hallazgos en Boca
																		del componente odontograma de diagnostico*/
																		DtoFormatoPlantillaHallazgosPiezasDentales dtoBoca=null;
																		dtoHallazgosBoca=new ArrayList<DtoFormatoPlantillaHallazgosPiezasDentales>();
																		int regboca=1;
																		
																		for(InfoHallazgoSuperficie dtoInfoHallazgoSuperficie :  io.getInfoPlanTrata().getSeccionHallazgosBoca()){
																			
																				dtoBoca=new DtoFormatoPlantillaHallazgosPiezasDentales();
																				dtoBoca.setNumero(regboca);
																				dtoBoca.setFechaHora(dtoInfoHallazgoSuperficie.getInfoRegistroHallazgo().getFechaModifica()+" "+dtoInfoHallazgoSuperficie.getInfoRegistroHallazgo().getHoraModifica());
																				dtoBoca.setHallazgo(dtoInfoHallazgoSuperficie.getHallazgoREQUERIDO().getNombre());
																				
																				//Recorre los programas o servicios de hallazgos en piezas dentales en el componente odontograma diagnostico
																				String programas="";
																				for(InfoProgramaServicioPlan dtoinfoProgramaServicio :  dtoInfoHallazgoSuperficie.getProgramasOservicios()){
																					if(dtoinfoProgramaServicio.getCodigoPkProgramaServicio().intValue() > 0)
																					{
																						if(dtoinfoProgramaServicio.getExisteBD().isActivo())
																						{
																							programas=programas+"\n"+dtoinfoProgramaServicio.getNombreProgramaServicio();
																						}
																					}else
																					{
																						//Recorrer Servicios de Cada Programa
																						for(InfoServicios dtoinfoServicio :  dtoinfoProgramaServicio.getListaServicios()){
																							programas=programas+"\n"+dtoinfoServicio.getCodigoMostrar()+" "+dtoinfoServicio.getServicio().getNombre();
																						}
																					}
																					
																				}
																				
																				dtoBoca.setProgramaServicio(programas);
																				dtoHallazgosBoca.add(dtoBoca);
																				regboca++;
																		
																		}
																			
																		
																		dtoSecciones.setDtohallazgosBoca(dtoHallazgosBoca);
																		
																		if(!UtilidadTexto.isEmpty(imagen)){
																			rutaImagen=nombreContexto+"/imagenesOdontologia/odontoValo/"+imagen;
																			BufferedImage img = null;
																		      try {
																		        img = ImageIO.read(new File(rutaImagen));
																		      } catch (IOException ex) {
																		    	  detalle="No se pudo leer la imagen";
																		    	  imprime=true;
																		    	  rutaImagen=null;
																		      }
																		}
																		else{
																			imprime=true;
																			detalle="No se ingresó Odontograma";
																			rutaImagen=null;
																		}
																		
																	break;
																	case ConstantesCamposParametrizables.tipoComponenteOdontogramaEvol:
																		dtoSecciones.setNombreS(dtoElementos.getDescripcion());
																		InfoOdontograma ioe=dtoPlantilla.getDtoEvolucionOdo().getInfoOdontograma();
																		imagen=ioe.getInfoPlanTrata().getImagen();
																		String estadoPlanT="";
																		
																		//VALIDAR SI SON DE EVOLUCION O DE VALORACION
																		if(formaEvolucion!=null){
																			if(!UtilidadTexto.isEmpty(formaEvolucion.getInfoCompOdont().getDtoInfoPlanTratamiento().getEstado())){
																				estadoPlanT=ValoresPorDefecto.getIntegridadDominio(formaEvolucion.getInfoCompOdont().getDtoInfoPlanTratamiento().getEstado()).toString();
																			}
																			
																		}else{
																			if(!UtilidadTexto.isEmpty(formaValoracion.getInfoCompOdont().getDtoInfoPlanTratamiento().getEstado())){
																				estadoPlanT=ValoresPorDefecto.getIntegridadDominio(formaValoracion.getInfoCompOdont().getDtoInfoPlanTratamiento().getEstado()).toString();
																			}
																			
																		}
																		
																			
																		DtoFormatoPlantillaProgServCitaAsignada dtoProgServC=null;
																		dtoProgServCitaAsign=new ArrayList<DtoFormatoPlantillaProgServCitaAsignada>();
																		for(InfoDetallePlanTramiento dtoinfoDetallePlanT :  ioe.getInfoPlanTrata().getSeccionProgServCita()){
																			for(InfoHallazgoSuperficie dtoinfoHallazgoSuperficie :  dtoinfoDetallePlanT.getDetalleSuperficie()){
																				
																					dtoProgServC=new DtoFormatoPlantillaProgServCitaAsignada();
																					dtoProgServC.setEstadoPlanT(estadoPlanT);
																					dtoProgServC.setPiezaDental(dtoinfoDetallePlanT.getPieza().getCodigo());
																					if(UtilidadTexto.isEmpty(dtoinfoHallazgoSuperficie.getSuperficieOPCIONAL().getNombre()))
																					{
																						dtoProgServC.setSuperficie("Todas las superficies");
																					}
																					else
																					{
																						dtoProgServC.setSuperficie(dtoinfoHallazgoSuperficie.getSuperficieOPCIONAL().getNombre());	
																					}
																					dtoProgServC.setHallazgo(dtoinfoHallazgoSuperficie.getHallazgoREQUERIDO().getNombre());
																					
																					String progServ="";
																					for(InfoProgramaServicioPlan dtoinfoProgServPlan :  dtoinfoHallazgoSuperficie.getProgramasOservicios()){
																						if(dtoinfoProgServPlan.getCodigoPkProgramaServicio().intValue() > 0)
																						{
																							if(dtoinfoProgServPlan.getExisteBD().isActivo())
																							{
																								progServ=progServ+dtoinfoProgServPlan.getNombreProgramaServicio()+"\n";
																								dtoProgServC.setEstado(ValoresPorDefecto.getIntegridadDominio(dtoinfoProgServPlan.getEstadoPrograma()).toString());
																							}
																						}else
																						{
																						
																							for(InfoServicios dtoinfoServicios :  dtoinfoProgServPlan.getListaServicios()){
																								if(dtoinfoServicios.getExisteBD().isActivo())
																								{
																									progServ=progServ+dtoinfoServicios.getCodigoMostrar()+" "+dtoinfoServicios.getServicio().getNombre();
																									dtoProgServC.setEstado(dtoinfoServicios.getEstadoServicio());
																								}
																							}
																							
																						}
																					}
																					dtoProgServC.setProgramaServicio(progServ);
																					dtoProgServCitaAsign.add(dtoProgServC);
																					
																			}
																		}
																		
																		
																		if(!UtilidadTexto.isEmpty(imagen)){
																			rutaImagen=nombreContexto+"/imagenesOdontologia/odontoEvo/"+imagen;
																			BufferedImage img = null;
																		      try {
																		        img = ImageIO.read(new File(rutaImagen));
																		      } catch (IOException ex) {
																		    	  detalle="No se pudo leer la imagen";
																		    	  imprime=true;
																		    	  rutaImagen=null;
																		      }
																		}
																		else{
																			imprime=true;
																			detalle="No se ingresó Odontograma";
																			rutaImagen=null;
																		}
																		
																		dtoSecciones.setDtoProgServCitaAsignada(dtoProgServCitaAsign);
																	break;
																}
																
															}else
																if(dtoElementos.isEscala()==true){
																	dtoSecciones.setNombreS(dtoElementos.getDescripcion());
																}
													}
													if(imprime==false)
														dtoSecciones.setDetalle("");
													else
														dtoSecciones.setDetalle(detalle);
													dtoSecciones.setImagen(rutaImagen);
													
												}
											}
					
				}
			}
			
			ArrayList<DtoPlantilla> listaPlantilla = new ArrayList<DtoPlantilla>();
			
			
			//SE ASIGNAN LOS DATASOURCE PARA ENVIAR A IREPORT, DEPENDIENDO SI SE HAN CARGADO EN ALGUNA SECCION
			for(DtoPlantilla dtoPlantilla :  plantillas){	
				
				for( DtoSeccionFija registroSeccionFija : dtoPlantilla.getSeccionesFijas()){	
					if(registroSeccionFija.getDtohallazgos()!=null && registroSeccionFija.getDtohallazgos().size()>0){
						registroSeccionFija.setDsHallazgos(new JRBeanCollectionDataSource(registroSeccionFija.getDtohallazgos()));
					}
					if(registroSeccionFija.getDtohallazgosPiezasDentales()!=null && registroSeccionFija.getDtohallazgosPiezasDentales().size()>0){
						registroSeccionFija.setDsHallazgosPiezasDentales(new JRBeanCollectionDataSource(registroSeccionFija.getDtohallazgosPiezasDentales()));
					}
					if(registroSeccionFija.getDtohallazgosBoca()!=null && registroSeccionFija.getDtohallazgosBoca().size()>0){
						registroSeccionFija.setDsHallazgosBoca(new JRBeanCollectionDataSource(registroSeccionFija.getDtohallazgosBoca()));
					}
					if(registroSeccionFija.getDtoTratamientosInternos()!=null && registroSeccionFija.getDtoTratamientosInternos().size()>0){
						registroSeccionFija.setDsTratamientosInternos(new JRBeanCollectionDataSource(registroSeccionFija.getDtoTratamientosInternos()));
					}
					if(registroSeccionFija.getDtoTratamientosExternos()!=null && registroSeccionFija.getDtoTratamientosExternos().size()>0){
						registroSeccionFija.setDsTratamientosExternos(new JRBeanCollectionDataSource(registroSeccionFija.getDtoTratamientosExternos()));
					}
					if(registroSeccionFija.getDtoProgServCitaAsignada()!=null && registroSeccionFija.getDtoProgServCitaAsignada().size()>0){
						registroSeccionFija.setDsProgServCitaAsign(new JRBeanCollectionDataSource(registroSeccionFija.getDtoProgServCitaAsignada()));
					}
					if(registroSeccionFija.getDtoOrdenarColumnas()!=null && registroSeccionFija.getDtoOrdenarColumnas().size()>0){
						registroSeccionFija.setDsOrdenar(new JRBeanCollectionDataSource(registroSeccionFija.getDtoOrdenarColumnas()));
					}
					
				}				
				dtoPlantilla.setDsSeccionesFijas(new JRBeanCollectionDataSource(dtoPlantilla.getSeccionesFijas()));
				listaPlantilla.add(dtoPlantilla);
			}
			
			
			dtoGeneral.setDsPlantillaOdonto(new JRBeanCollectionDataSource(listaPlantilla));
			collectionDTOGeneral.add(dtoGeneral);
				
		return collectionDTOGeneral;
	}
	
	@SuppressWarnings("unused")
	@Override
	public Map<String, Object> obtenerDatosAdicionalesReporte() {
		ClassLoader loader = this.getClass().getClassLoader();
		InputStream myInFile = null;
        

        try {
	        myInFile = loader.getResourceAsStream(RUTA_SUBREPORTE_RESULTADOCONSULTA);
	        if (myInFile == null) {
	           	myInFile = new FileInputStream(RUTA_SUBREPORTE_RESULTADOCONSULTA);
	           	
			}else if (myInFile != null) {
	            Object mySubreportObj = JRLoader.loadObject(myInFile);
	            parametrosReporte.put(NOMBRE_SUBREPORTE_RESULTADOCONSULTA, mySubreportObj);
	            myInFile.close();
	            myInFile=null;
	        }
	        
	        myInFile = loader.getResourceAsStream(RUTA_SUBREPORTE_SECCIONFIJA);
	        if (myInFile == null) {
	           	myInFile = new FileInputStream(RUTA_SUBREPORTE_SECCIONFIJA);
	           	
			}else if (myInFile != null) {
	            Object mySubreportObj = JRLoader.loadObject(myInFile);
	            parametrosReporte.put(NOMBRE_SUBREPORTE_SECCIONFIJA, mySubreportObj);
	            myInFile.close();
	            myInFile=null;
	        }
	              
	        myInFile = loader.getResourceAsStream(RUTA_SUBREPORTE_HALLAZGOS);
	        if (myInFile == null) {
	           	myInFile = new FileInputStream(RUTA_SUBREPORTE_HALLAZGOS);
	           	
			}else if (myInFile != null) {
	            Object mySubreportObj = JRLoader.loadObject(myInFile);
	            parametrosReporte.put(NOMBRE_SUBREPORTE_HALLAZGOS, mySubreportObj);
	            myInFile.close();
	            myInFile=null;
	        }
	        
	        myInFile = loader.getResourceAsStream(RUTA_SUBREPORTE_HALLAZGOS_PIEZAS_DENTALES);
	        if (myInFile == null) {
	           	myInFile = new FileInputStream(RUTA_SUBREPORTE_HALLAZGOS_PIEZAS_DENTALES);
	           	
			}else if (myInFile != null) {
	            Object mySubreportObj = JRLoader.loadObject(myInFile);
	            parametrosReporte.put(NOMBRE_SUBREPORTE_HALLAZGOS_PIEZAS_DENTALES, mySubreportObj);
	            myInFile.close();
	            myInFile=null;
	        }
	        
	        myInFile = loader.getResourceAsStream(RUTA_SUBREPORTE_HALLAZGOS_BOCA);
	        if (myInFile == null) {
	           	myInFile = new FileInputStream(RUTA_SUBREPORTE_HALLAZGOS_BOCA);
	           	
			}else if (myInFile != null) {
	            Object mySubreportObj = JRLoader.loadObject(myInFile);
	            parametrosReporte.put(NOMBRE_SUBREPORTE_HALLAZGOS_BOCA, mySubreportObj);
	            myInFile.close();
	            myInFile=null;
	        }
	        myInFile = loader.getResourceAsStream(RUTA_SUBREPORTE_TRATAMIENTOS_INTERNOS);
	        if (myInFile == null) {
	           	myInFile = new FileInputStream(RUTA_SUBREPORTE_TRATAMIENTOS_INTERNOS);
	           	
			}else if (myInFile != null) {
	            Object mySubreportObj = JRLoader.loadObject(myInFile);
	            parametrosReporte.put(NOMBRE_SUBREPORTE_TRATAMIENTOS_INTERNOS, mySubreportObj);
	            myInFile.close();
	            myInFile=null;
	        }
	        myInFile = loader.getResourceAsStream(RUTA_SUBREPORTE_TRATAMIENTOS_EXTERNOS);
	        if (myInFile == null) {
	           	myInFile = new FileInputStream(RUTA_SUBREPORTE_TRATAMIENTOS_EXTERNOS);
	           	
			}else if (myInFile != null) {
	            Object mySubreportObj = JRLoader.loadObject(myInFile);
	            parametrosReporte.put(NOMBRE_SUBREPORTE_TRATAMIENTOS_EXTERNOS, mySubreportObj);
	            myInFile.close();
	            myInFile=null;
	        }
	        
	        myInFile = loader.getResourceAsStream(RUTA_SUBREPORTE_PROG_SERV_CITA_ASIGN);
	        if (myInFile == null) {
	           	myInFile = new FileInputStream(RUTA_SUBREPORTE_PROG_SERV_CITA_ASIGN);
	           	
			}else if (myInFile != null) {
	            Object mySubreportObj = JRLoader.loadObject(myInFile);
	            parametrosReporte.put(NOMBRE_SUBREPORTE_PROG_SERV_CITA_ASIGN, mySubreportObj);
	            myInFile.close();
	            myInFile=null;
	        }
	        
	        myInFile = loader.getResourceAsStream(RUTA_SUBREPORTE_ANTE_ODONTO);
	        if (myInFile == null) {
	           	myInFile = new FileInputStream(RUTA_SUBREPORTE_ANTE_ODONTO);
	           	
			}else if (myInFile != null) {
	            Object mySubreportObj = JRLoader.loadObject(myInFile);
	            parametrosReporte.put(NOMBRE_SUBREPORTE_ANTE_ODONTO, mySubreportObj);
	            myInFile.close();
	            myInFile=null;
	        }
	        
		}catch (Exception e) {
			e.printStackTrace();
		}
		
        return parametrosReporte;
	}

	@Override
	public String obtenerRutaPlantilla() {
		return RUTA_REPORTE_GENERAL;
	}
	
}
