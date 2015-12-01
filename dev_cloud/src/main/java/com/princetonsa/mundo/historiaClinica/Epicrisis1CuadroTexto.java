package com.princetonsa.mundo.historiaClinica;

import java.util.ArrayList;
import java.util.HashMap;

import util.ConstantesBD;
import util.ConstantesCamposParametrizables;
import util.ConstantesIntegridadDominio;
import util.UtilidadTexto;
import util.ValoresPorDefecto;

import com.princetonsa.dto.epicrisis.DtoCirugiaEpicrisis;
import com.princetonsa.dto.epicrisis.DtoEventoAdversoEpicrisis;
import com.princetonsa.dto.epicrisis.DtoInterpretacionSolicitud;
import com.princetonsa.dto.epicrisis.DtoMedicamentoEpicrisis;
import com.princetonsa.dto.epicrisis.DtoMedicamentosAdminEpicrisis;
import com.princetonsa.dto.epicrisis.DtoNotasCirugiaEpicrisis;
import com.princetonsa.dto.epicrisis.DtoProcedimientosEpicrisis;
import com.princetonsa.dto.epicrisis.DtoServiciosCirugiaEpicrisis;
import com.princetonsa.dto.historiaClinica.DtoEvolucion;
import com.princetonsa.dto.historiaClinica.DtoRevisionSistema;
import com.princetonsa.dto.historiaClinica.DtoValoracion;
import com.princetonsa.dto.historiaClinica.DtoValoracionHospitalizacion;
import com.princetonsa.dto.historiaClinica.DtoValoracionObservaciones;
import com.princetonsa.dto.historiaClinica.DtoValoracionUrgencias;
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
import com.princetonsa.mundo.atencion.Diagnostico;
import com.princetonsa.mundo.atencion.SignoVital;

/**
 * 
 * @author wilson
 *
 */
public class Epicrisis1CuadroTexto 
{
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public static String armarContenidoEventosAdversos(DtoEventoAdversoEpicrisis dto)
	{
		if(dto==null)
			return "";
		String contenido=	"EVENTO \n"+
							(UtilidadTexto.isEmpty(dto.getEvento())?"":dto.getEvento().toUpperCase()+"\n\n") +
							"CLASIFICACION \n" +
							(UtilidadTexto.isEmpty(dto.getClasificacion())?"":dto.getClasificacion().toUpperCase()+"\n\n") +
							"OBSERVACIONES \n"+
							(UtilidadTexto.isEmpty(dto.getObservaciones())?"":dto.getObservaciones().toUpperCase()+"\n\n");
		return contenido;
	}
	
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public static String armarContenidoProcedimientos(DtoProcedimientosEpicrisis dto)
	{
		String contenido=	dto.getServicio().toUpperCase()+" INTERPRETACION DEL PROCEDIMIENTO: \n"+
							dto.getInterpretacion().toUpperCase()+"\n\n";
		return contenido;
	}
	
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public static String armarContenidoAdminMed(DtoMedicamentosAdminEpicrisis dto)
	{
		String contenido="";
		for(int w=0; w<dto.getMedicamentos().size();w++)
		{
			contenido+=armarContenidoMedicamento(dto.getMedicamentos().get(w));
		}
		return contenido;
	}
	
	
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public static String armarContenidoMedicamento(DtoMedicamentoEpicrisis dto)
	{
		String contenido= 	"MEDICAMENTO ADMINISTRADO: "+dto.getDescripcionMedicamento().toUpperCase()+" \n" +
							"DOSIS: "+dto.getDosis().toUpperCase()+" \n"+
							"FRECUENCIA: "+dto.getFrecuencia().toUpperCase()+" \n"+
							"VIA: "+dto.getVia().toUpperCase()+" \n"+
							"DOSIS ADMINISTRADA: "+dto.getDosisAdmin().toUpperCase()+" \n"+
							"UNIDADES CONSUMIDAS: "+dto.getUnidadesConsumidas().toUpperCase()+" \n\n";
		
		return contenido;
	}
	
	/**
	 * 
	 * @param dto
	 */
	public static String armarContenidoCxHojaQx(DtoServiciosCirugiaEpicrisis dto)
	{
		String contenido= 	"SERVICIO: "+dto.getServicio().toUpperCase()+" \n\n";
		contenido+=(!UtilidadTexto.isEmpty(dto.getJustificacionNoPos()))?"RESUMEN DE HISTORIA CLINICA QUE JUSTIFIQUE EL SERVICIO NO POS:"+dto.getJustificacionNoPos().toUpperCase()+"\n\n":"";						
		contenido+=			"DESCRIPCION QUIRURGICA DEL SERVICIO: "+dto.getDescripcionQx().toUpperCase()+" \n\n"+
							"DIAGNOSTICO POSTOPERATORIO DEL SERVICIO: "+dto.getServicio().toUpperCase()+"\n" +
							"DIAGNOSTICO PRINCIPAL: "+dto.getDxPrincipalPostopertorio().toUpperCase()+" \n" +
							"DIAGNOSTICOS RELACIONADOS: "+dto.getDxRelacionadosPostoperatorio().toUpperCase()+" \n" +
							"DIAGNOSTICO COMPLICACION: "+dto.getDxComplicacionPostoperatorio().toUpperCase()+"\n\n";
		return contenido;
	}
	
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public static String armarContenidoCxDescQx(DtoCirugiaEpicrisis dto)
	{
		String contenido= 	"DESCRIPCION QUIRURGICA \n" +
							dto.getDescripcionQx().toUpperCase()+" \n\n";
		
		return contenido;
	}
	
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public static String armarContenidoCxSalidaSalaPaciente(DtoCirugiaEpicrisis dto)
	{
		String contenido= 	"SALIDA DE LA SALA DEL PACIENTE \n" +
							"FECHA/HORA INGRESO SALA: "+dto.getFechaIngresoSala().toUpperCase()+"\n" +
							"FECHA/HORA SALIDA SALA: "+dto.getFechaSalidaSala().toUpperCase()+"\n" +
							"SALIDA DE LA SALA DEL PACIENTE: "+dto.getSalidaSalaPaciente().toUpperCase()+"\n\n";
		
		return contenido;
	}
	
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public static String armarContenidoNotasEnfermeria(DtoNotasCirugiaEpicrisis dto)
	{
		String contenido=	"NOTAS DE ENFERMERIA \n" +
							dto.getDescripcion().toUpperCase()+"\n\n";
		return contenido;
	}
	
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public static String armarContenidoNotasRecuperacion(DtoNotasCirugiaEpicrisis dto)
	{
		String contenido=	"NOTAS DE RECUPERACION \n" +
							dto.getDescripcion().toUpperCase()+"\n\n";
		return contenido;
	}

	/**
	 * 
	 * @param seccionFija
	 * @param dtoValoracion
	 * @return
	 */
	public static String armarContenidoInterconsultaInformacionGeneral(DtoSeccionFija seccionFija, DtoValoracion dtoValoracion) 
	{
		String consulta="CONTROL";
		if(dtoValoracion.getValoracionConsulta().getPrimeraVez().booleanValue())
		{
			consulta+="PRIMERA VEZ";
		}
		
		String contenido= 	seccionFija.getNombreSeccion()+" \n" +
							"TIPO RECARGO: "+dtoValoracion.getValoracionConsulta().getNombreTipoRecargo()+" \n" +
							"EDAD: "+dtoValoracion.getEdad()+" \n" +
							"FUE ACCIDENTE TRABAJO: "+UtilidadTexto.imprimirSiNo(dtoValoracion.getEventoFueAccidenteTrabajo()+"")+" \n" +
							"CONSULTA DE "+consulta+"\n\n";
		
		for(int w=0; w<dtoValoracion.getObservaciones().size(); w++)
		{
			DtoValoracionObservaciones motivoConsulta= dtoValoracion.getObservaciones().get(w);
			if(motivoConsulta.getTipo().equals(ConstantesIntegridadDominio.acronimoMotivoConsulta))
			{
				contenido+= motivoConsulta.getLabel()+" \n" +
							motivoConsulta.getValor()+" \n\n";
			}
		}
		
		for(int w=0; w<dtoValoracion.getObservaciones().size(); w++)
		{
			DtoValoracionObservaciones motivoConsulta= dtoValoracion.getObservaciones().get(w);
			//Se busca la observación que sea de tipo motivo consulta
			if(motivoConsulta.getTipo().equals(ConstantesIntegridadDominio.acronimoEnfermedadActual))
			{
				contenido+= motivoConsulta.getLabel()+" \n" +
				motivoConsulta.getValor()+" \n\n";
			}
		}	
		
		return contenido;
	}

	/**
	 * 
	 * @param seccionFija
	 * @param dtoValoracion
	 * @return
	 */
	public static String armarContenidoInterconsultaRevisionSistemas(DtoSeccionFija seccionFija, DtoValoracion dtoValoracion) 
	{
		String contenido=	seccionFija.getNombreSeccion()+" \n\n";
		
		for(int w=0; w<dtoValoracion.getRevisionesSistemas().size(); w++)
		{
			DtoRevisionSistema revisionSistema= dtoValoracion.getRevisionesSistemas().get(w);
			contenido+= revisionSistema.getNombre()+" -------- ";
			if(revisionSistema.isMultiple()) 
			{
				contenido+=" ES MULTIPLE -------- ";
				for(int i=0;i<revisionSistema.getOpciones().size();i++)
				{
					contenido+=revisionSistema.getOpciones().get(i).getNombre()+" - ";	
				}
			}
			else
			{
				contenido+= revisionSistema.getValor()+" "+revisionSistema.getUnidadMedida()+"   ";
			}
			if(!UtilidadTexto.isEmpty(revisionSistema.getEstadoNormal()+""))
			{
				if(revisionSistema.getEstadoNormal())
				{
					contenido+=" -------- "+ revisionSistema.getValorVerdadero()+" ";
				}	
				else
				{
					contenido+=" -------- "+ revisionSistema.getValorFalso()+" ";
				}
			}	
			
			contenido+=" -------- DESCRIPCION: "+revisionSistema.getDescripcion()+" \n\n";
		}
		
		return contenido;
	}

	/**
	 * 
	 * @param seccionFija
	 * @param dtoValoracion
	 * @return
	 */
	public static String armarContenidoInterconsultaCausaExterna(DtoSeccionFija seccionFija, DtoValoracion dtoValoracion) 
	{
		String contenido= 	seccionFija.getNombreSeccion()+"\n" +
							dtoValoracion.getNombreCausaExterna()+"\n\n";
		return contenido;
	}

	/**
	 * 
	 * @param seccionFija
	 * @param dtoValoracion
	 * @return
	 */
	public static String armarContenidoInterconsultaFinalidadConsulta(DtoSeccionFija seccionFija, DtoValoracion dtoValoracion) 
	{
		String contenido= 	seccionFija.getNombreSeccion()+"\n" +
							dtoValoracion.getValoracionConsulta().getNombreFinalidadConsulta()+"\n\n";
		return contenido;
	}

	/**
	 * 
	 * @param seccionFija
	 * @param dtoValoracion
	 * @return
	 */
	public static String armarContenidoInterconsultasDx(DtoSeccionFija seccionFija, DtoValoracion dtoVal) 
	{
		String contenido=	seccionFija.getNombreSeccion()+"\n" +
							"DX. PRINCIPAL: ";
		
		if(!UtilidadTexto.isEmpty(dtoVal.getDiagnosticos().get(0).getValor()))
		{
			contenido+=	dtoVal.getDiagnosticos().get(0).getAcronimo()+" - "+dtoVal.getDiagnosticos().get(0).getTipoCIE()+" "+dtoVal.getDiagnosticos().get(0).getNombre()+"\n";
		}
		contenido+=	"TIPO DX PRINCIPAL: "+dtoVal.getValoracionConsulta().getNombreTipoDiagnostico()+"\n";
		
		if(dtoVal.getDiagnosticos().size()>1)
		{
			contenido+="DX. RELACIONADOS \n";
			for(int i=1;i<dtoVal.getDiagnosticos().size();i++)
			{
				contenido+="DX. RELACIONADO No. "+i+": "+dtoVal.getDiagnosticos().get(i).getAcronimo()+" - "+dtoVal.getDiagnosticos().get(i).getTipoCIE()+" "+dtoVal.getDiagnosticos().get(i).getNombre();
			}
		}
		
		return contenido;
	}

	/**
	 * 
	 * @param seccionFija
	 * @param dtoValoracion
	 * @return
	 */
	public static String armarContenidoInterconsultasConceptoConsulta(DtoSeccionFija seccionFija, DtoValoracion dtoVal) 
	{
		String contenido=	seccionFija.getNombreSeccion()+"\n\n";
		for(int w=0; w<dtoVal.getObservaciones().size(); w++)
		{
			DtoValoracionObservaciones conceptoConsulta= dtoVal.getObservaciones().get(w);
			//Se busca la observación que sea de tipo concepto consulta
			if(conceptoConsulta.getTipo().equals(ConstantesIntegridadDominio.acronimoConceptoConsulta))
			{
				contenido+=	conceptoConsulta.getLabel()+"\n" +
							conceptoConsulta.getValor()+"\n\n";
			}
		}
		return contenido;
	}

	/**
	 * 
	 * @param seccionFija
	 * @param dtoValoracion
	 * @return
	 */
	public static String armarContenidoValoracionesIncapacidadFuncional(DtoSeccionFija seccionFija, DtoValoracion dtoVal) 
	{
		String contenido= 	seccionFija.getNombreSeccion()+"\n" +
							"NUMERO DE DIAS: "+dtoVal.getValoracionConsulta().getNumeroDiasIncapacidad()+"\n";
		
		if(!UtilidadTexto.isEmpty(dtoVal.getValoracionConsulta().getObservacionesIncapacidad()))
		{
			contenido+= dtoVal.getValoracionConsulta().getObservacionesIncapacidad()+"\n\n";
		}
		return contenido;
	}

	/**
	 * 
	 * @param seccionFija
	 * @param dtoValoracion
	 * @return
	 */
	public static String armarContenidoInterconsultasObservaciones(DtoSeccionFija seccionFija, DtoValoracion dtoVal) 
	{
		String contenido= 	seccionFija.getNombreSeccion()+"\n\n";
		
		contenido+=	ValoresPorDefecto.getIntegridadDominio(ConstantesIntegridadDominio.acronimoPlanDiagnosticoTerapeutico)+":\n";
					
		for(int w=0; w<dtoVal.getObservaciones().size(); w++)
		{
			DtoValoracionObservaciones observacion= dtoVal.getObservaciones().get(w);
			//Se busca la observación que sea de tipo plan diagnostico y terapeutico
			if(observacion.getTipo().equals(ConstantesIntegridadDominio.acronimoPlanDiagnosticoTerapeutico))
			{
				if(!UtilidadTexto.isEmpty(observacion.getValor()))
				{
					contenido+= observacion.getValor()+"\n\n";
				}
			}
		}
		
		contenido+= ValoresPorDefecto.getIntegridadDominio(ConstantesIntegridadDominio.acronimoComentariosGenerales)+": \n";
		
		for(int w=0; w<dtoVal.getObservaciones().size(); w++)
		{
			DtoValoracionObservaciones observacion= dtoVal.getObservaciones().get(w);
			//Se busca la observación que sea de tipo comentarios generales
			if(observacion.getTipo().equals(ConstantesIntegridadDominio.acronimoComentariosGenerales))
			{
				//Si ya tenía valor se muestra a manera de resumen
				if(!observacion.getValor().equals(""))
				{
					contenido+=observacion.getValor()+"\n\n";
				}
			} 
		}
		
		contenido+=ValoresPorDefecto.getIntegridadDominio(ConstantesIntegridadDominio.acronimoPronostico)+": \n";
		
		for(int w=0; w<dtoVal.getObservaciones().size(); w++)
		{
			DtoValoracionObservaciones observacion= dtoVal.getObservaciones().get(w);
			//Se busca la observación que sea de tipo comentarios generales
			if(observacion.getTipo().equals(ConstantesIntegridadDominio.acronimoPronostico))
			{
				//Si ya tenía valor se muestra a manera de resumen
				if(!observacion.getValor().equals(""))
				{
					contenido+=observacion.getValor()+"\n\n";
				}
			} 
		}
		
		return contenido;
	}

	/**
	 * 
	 * @param seccionFija
	 * @param dtoValoracion
	 * @return
	 */
	public static String armarContenidoInterconsultasFechaProximoControl(DtoSeccionFija seccionFija, DtoValoracion dtoVal) 
	{
		String contenido= 	seccionFija.getNombreSeccion()+"\n" +
							dtoVal.getValoracionConsulta().getFechaProximoControl()+"\n\n";
		return contenido;
	}
	
	/**
	 * 
	 * @param seccionFija
	 * @param dtoVal
	 * @return
	 */
	public static String armarContenidoSeccionesParametrizablesInterconsultas(DtoSeccionFija seccionFija, DtoValoracion dtoVal)
	{
		String contenido="";
		if(seccionFija.getNumElementosVisibles()>0)
		{
			for(int w=0; w<seccionFija.getElementos().size(); w++)
			{
				DtoElementoParam elemento=seccionFija.getElementos().get(w);
				if(elemento.isVisible())
				{
					if(!elemento.isSeccion()||(elemento.isSeccion()&&!elemento.getCodigo().equals("")&&!elemento.getDescripcion().equals(""))) 
					{
						contenido+= elemento.getDescripcion();
					}
					
					if(elemento.isComponente()) 
					{
						DtoComponente componente = (DtoComponente)elemento;
						switch(componente.getCodigoTipo())
						{
							//Componente de signos vitales
							case ConstantesCamposParametrizables.tipoComponenteSignosVitales:
								contenido+= armarContenidoSignosVitales(dtoVal.getSignosVitales());
							break;
							case ConstantesCamposParametrizables.tipoComponenteGinecologia:
								contenido+= armarContenidoGinecologia(dtoVal.getHistoriaMenstrual());
							break;
							case ConstantesCamposParametrizables.tipoComponenteOftalmologia:
								contenido+= armarContenidoOftalmologia(dtoVal.getOftalmologia());	
							break;
							case ConstantesCamposParametrizables.tipoComponentePediatria:
								contenido+= armarContenidoPediatria(dtoVal.getPediatria());
							break;
						}
						
						for(int a=0;a<componente.getElementos().size();a++)
						{
							if(componente.getElementos().get(a).isVisible())
							{
								if(componente.getElementos().get(a).isEscala())
								{
									DtoEscala escala = (DtoEscala)componente.getElementos().get(a);
									contenido+=armarContenidoEscala(escala);
								}
								else if(componente.getElementos().get(a).isSeccion())
								{
									DtoSeccionParametrizable seccion = (DtoSeccionParametrizable)componente.getElementos().get(a);
									contenido+=armarContenidoSeccion(seccion);
								}
							}
						}
					}	
					else if(elemento.isEscala())
					{
						DtoEscala escala = (DtoEscala)elemento;
						contenido+= armarContenidoEscala(escala);
					}
					else if(elemento.isSeccion())
					{
						DtoSeccionParametrizable seccion = (DtoSeccionParametrizable)elemento;
						contenido+=armarContenidoSeccion(seccion);
					}
				}
			}	
		}
		return contenido;
	}

	/**
	 * 
	 * @param signosVitales
	 * @return
	 */
	public static String armarContenidoSignosVitales(ArrayList<SignoVital> arraySignosVitales) 
	{
		String contenido="\n";
		for(int w=0; w<arraySignosVitales.size(); w++)
		{
			SignoVital signoVital= arraySignosVitales.get(w);
			contenido+= signoVital.getValorSignoVital().equals("")?"":UtilidadTexto.formatearValores(signoVital.getValorSignoVital(),"0.00")+" "+signoVital.getUnidadMedida()+"\n";
		}
		return contenido;
	}
	
	/**
	 * 
	 * @param historiaMenstrual
	 * @return
	 */
	public static String armarContenidoGinecologia(DtoHistoriaMenstrual dtoHM) 
	{
		String contenido=	"HISTORIA MENSTRUAL \n\n" +
							"EDAD MENARQUIA: "+dtoHM.getNombreEdadMenarquia()+"\n" +
							"CUAL? "+dtoHM.getOtraEdadMenarquia()+"\n" +
							"EDAD MENOPAUSIA: "+dtoHM.getNombreEdadMenopausia()+"\n" +
							"CUAL? "+dtoHM.getOtraEdadMenopausia()+"\n" +
							"";
		
		if(!UtilidadTexto.isEmpty(dtoHM.getCicloMenstrual()))
		{
			contenido+=	"CICLO MENSTRUAL \n" +
						dtoHM.getCicloMenstrual()+" DIAS \n";
		}
		
		if(!UtilidadTexto.isEmpty(dtoHM.getDuracionMenstruacion()))
		{
			contenido+="DURACION: "+dtoHM.getDuracionMenstruacion()+" \n";
		}
		
		if(!UtilidadTexto.isEmpty(dtoHM.getFechaUltimaRegla()))
		{
			contenido+="FUR(dd/mm/aaaa) "+dtoHM.getFechaUltimaRegla()+"\n";
		}
		
		if(UtilidadTexto.getBoolean(dtoHM.getDuracionMenstruacion()))
		{
			contenido+="DOLOR: SI\n\n";
		}
		else
		{
			contenido+="DOLOR: NO\n\n";
		}
		
		if(!UtilidadTexto.isEmpty(dtoHM.getNombreConceptoMenstruacion()))
		{
			contenido+="CONCEPTO: "+dtoHM.getNombreConceptoMenstruacion()+"\n\n";
		}
		
		if(!UtilidadTexto.isEmpty(dtoHM.getObservacionesMenstruales()))
		{
			contenido+="OBSERVACIONES CARACTERISTICAS MENSTRUACION: "+dtoHM.getObservacionesMenstruales()+"\n\n";
		}
		
		return contenido;
	}
	
	/**
	 * 
	 * @param oftalmologia
	 * @return
	 */
	public static String armarContenidoOftalmologia(DtoOftalmologia dtoOftal) 
	{
		String contenido="";
		if(dtoOftal.getNumRegistroSintomas()>0|| dtoOftal.getNumRegistroOtrosSintomas()>0)
		{
			contenido+="SINTOMAS MOTIVO DE LA CONSULTA \n\n";
			
			for(int i=0;i<dtoOftal.getNumRegistroSintomas();i++)
			{
				contenido+=	"TIPO SINTOMA: "+dtoOftal.getRegistroSintomas("nombreTipoSintoma_"+i)+"  " +
							"SINTOMA: "+dtoOftal.getRegistroSintomas("nombreSintoma_"+i)+"   " +
							"LOCALIZACION: "+dtoOftal.getRegistroSintomas("nombreSintoma_"+i)+"   " +
							"SEVERIDAD: "+dtoOftal.getRegistroSintomas("nombreSeveridad_"+i)+"\n\n";
			}
			
			for(int i=0;i<dtoOftal.getNumRegistroOtrosSintomas();i++)
			{
				contenido+="OTRO: "+dtoOftal.getRegistroOtrosSintomas("nombreSintoma_"+i)+" - "+dtoOftal.getRegistroOtrosSintomas("nombreLocalizacion_"+i)+" - "+dtoOftal.getRegistroOtrosSintomas("nombreSeveridad_"+i)+"\n\n";
			}
		}
		
		contenido+="EXAMEN OFTALMOLOGICO: \n\n";
		contenido+="LEJOS AV S.C  O.D: "+dtoOftal.getNombreOjoDerechoLejosSinCorrecion()+"\n";
		contenido+="LEJOS AV C.C  O.D: "+dtoOftal.getNombreOjoDerechoLejosConCorrecion()+"\n";
		contenido+="CERCA AV S.C  O.D: "+dtoOftal.getNombreOjoDerechoCercaSinCorrecion()+"\n";
		contenido+="CERCA AV C.C  O.D: "+dtoOftal.getNombreOjoDerechoCercaConCorrecion()+"\n\n";
		
		contenido+="LEJOS AV S.C  O.S: "+dtoOftal.getNombreOjoIzquierdoLejosSinCorrecion()+"\n";
		contenido+="LEJOS AV C.C  O.S: "+dtoOftal.getNombreOjoIzquierdoLejosConCorrecion()+"\n";
		contenido+="CERCA AV S.C  O.S: "+dtoOftal.getNombreOjoIzquierdoCercaSinCorrecion()+"\n";
		contenido+="CERCA AV C.C  O.S: "+dtoOftal.getNombreOjoIzquierdoCercaConCorrecion()+"\n\n";
		
		contenido+="REFRACCIÓN: \n\n";
		contenido+="SK \n";
		contenido+="O.D "+dtoOftal.getOjoDerechoSK()+"\n";
		contenido+="SK CICLO \n";
		contenido+="O.D "+dtoOftal.getOjoDerechoSKCiclo()+"\n";
		contenido+="SK Subj \n";
		contenido+="O.D "+dtoOftal.getOjoDerechoSKSubj()+"\n\n";
		
		contenido+="SK \n";
		contenido+="O.S "+dtoOftal.getOjoIzquierdoSK()+"\n";
		contenido+="SK CICLO \n";
		contenido+="O.S "+dtoOftal.getOjoIzquierdoSKCiclo()+"\n";
		contenido+="SK Subj \n";
		contenido+="O.S "+dtoOftal.getOjoIzquierdoSKSubj()+"\n\n";
		
		contenido+="Add "+dtoOftal.getAdd()+"\n";
		contenido+="DIP "+dtoOftal.getDip()+"\n\n";
		
		contenido+="QUERATOMETRÍA \n";
		contenido+="O.D "+dtoOftal.getOjoDerechoQueratometria()+"\n";
		contenido+="O.S "+dtoOftal.getOjoIzquierdoQueratometria()+"\n\n";
		
		contenido+="TONOMETRÍA \n";
		contenido+="O.D "+dtoOftal.getOjoDerechoTonometria()+"\n";
		contenido+="O.S "+dtoOftal.getOjoIzquierdoTonometria()+"\n";
		contenido+="EQUIPO: "+dtoOftal.getNombreEquipoTonometria()+"\n\n";
		
		return contenido;
	}

	/**
	 * 
	 * @param pediatria
	 * @return
	 */
	private static String armarContenidoPediatria(DtoPediatria dtoPed) 
	{
		String contenido="DESARROLLO PSICOMOTOR\n\n";
		
		for(int w=0; w<dtoPed.getDesarrollos().size();w++)
		{
			DtoDesarrolloPediatria desarrollo=dtoPed.getDesarrollos().get(w);
			if(desarrollo.getCodigoTipo()==ConstantesBD.codigoTipoDesarrolloPsicomotor)
			{
				contenido+=desarrollo.getNombre()+" A LA EDAD DE "+desarrollo.getEdadTexto()+" DESCRIPCION: "+desarrollo.getDescripcion()+"\n ";
			}
		}	
		contenido+="\n";
		
		contenido+=ValoresPorDefecto.getIntegridadDominio(ConstantesIntegridadDominio.acronimoObservacionesDesarrollo);
		for(int w=0; w<dtoPed.getObservaciones().size();w++)
		{
			DtoObservacionesPediatria observacion=dtoPed.getObservaciones().get(w);	//Se busca la observación que sea de tipo observacion desarrollo
			if(observacion.getCodigoTipo().equals(ConstantesIntegridadDominio.acronimoObservacionesDesarrollo))
			{
				//Si ya tenía valor se muestra a manera de resumen
				if(!observacion.getValor().equals(""))
				{
					contenido+=observacion.getValor()+"\n";
				}
			}
		}
		contenido+="\n";
		
		contenido+=ValoresPorDefecto.getIntegridadDominio(ConstantesIntegridadDominio.acronimoSueniosHabitos);
		
		for(int w=0; w<dtoPed.getObservaciones().size();w++)
		{
			DtoObservacionesPediatria observacion=dtoPed.getObservaciones().get(w);	//Se busca la observación que sea de tipo observacion desarrollo
			if(observacion.getCodigoTipo().equals(ConstantesIntegridadDominio.acronimoSueniosHabitos))
			{
				//Si ya tenía valor se muestra a manera de resumen
				if(!observacion.getValor().equals(""))
				{
					contenido+=observacion.getValor()+"\n";
				}
			}
		}
		
		contenido+="\n";
		contenido+="VALORACION NUTRICIONAL: \n";
		if(dtoPed.getEdadPaciente()<=2)
		{
			contenido+="LACTANCIA MATERNA: "+UtilidadTexto.imprimirSiNo(dtoPed.getLactanciaMaterna()+"")+"\n";
			contenido+="OTRAS LECHES: "+UtilidadTexto.imprimirSiNo(dtoPed.getOtrasLeches()+"")+"\n";
			contenido+="CUAL? "+dtoPed.getDescripcionOtrasLeches()+"\n";
			contenido+="ALIMENTACION COMPLEMENTARIA: "+UtilidadTexto.imprimirSiNo(dtoPed.getAlimentacionComplementaria()+"")+"\n";
			contenido+="CUAL? "+dtoPed.getAlimentacionComplementaria()+"\n\n";
		}
		else
		{
			for(int w=0; w<dtoPed.getEdadesAlimentacion().size(); w++)
			{
				DtoEdadAlimentacionPediatria edad= dtoPed.getEdadesAlimentacion().get(w);
				contenido+=edad.getNombreEdad()+" A LA EDAD DE "+edad.getEdadTexto()+"\n";
			}
		}
		
		contenido+="\n";
		contenido+="ESTADO NUTRICIONAL: \n";
		for(int w=0; w<dtoPed.getEstadosNutricionales().size(); w++)
		{
			HashMap elemento= dtoPed.getEstadosNutricionales().get(w);
			contenido+=elemento.get("codigo")+" "+elemento.get("nombre")+"\n";
		}	
		
		if(dtoPed.getEdadPaciente()<=2)
		{
			contenido+=ValoresPorDefecto.getIntegridadDominio(ConstantesIntegridadDominio.acronimoValNutricionalMenor2Anios)+"\n";
			for(int w=0; w<dtoPed.getObservaciones().size();w++)
			{
				DtoObservacionesPediatria observacion= dtoPed.getObservaciones().get(w);
				//Se busca la observación que sea de tipo valoracion nutricional menor de 2 años
				if(observacion.getCodigoTipo().equals(ConstantesIntegridadDominio.acronimoValNutricionalMenor2Anios))
				{
					//Si ya tenía valor se muestra a manera de resumen
					if(!observacion.getValor().equals(""))
					{
						contenido+= observacion.getValor()+"\n";
					}
				}	
			}
		}
		else
		{
			contenido+="\n";
			contenido+=ValoresPorDefecto.getIntegridadDominio(ConstantesIntegridadDominio.acronimoTipoAlimentacion)+"\n";
			
			for(int w=0; w<dtoPed.getObservaciones().size(); w++)
			{
				DtoObservacionesPediatria observacion= dtoPed.getObservaciones().get(w);
				//Se busca la observación que sea de tipo alimentacion
				if(observacion.getCodigoTipo().equals(ConstantesIntegridadDominio.acronimoTipoAlimentacion))
				{
					//Si ya tenía valor se muestra a manera de resumen
					if(!observacion.getValor().equals(""))
					{
						contenido+=observacion.getValor()+"\n";
					}
				}	
			}
			
			contenido+="\n";
			contenido+=ValoresPorDefecto.getIntegridadDominio(ConstantesIntegridadDominio.acronimoValNutricionalMayor2Anios)+"\n";
			for(int w=0; w<dtoPed.getObservaciones().size(); w++)
			{
				DtoObservacionesPediatria observacion= dtoPed.getObservaciones().get(w);
				//Se busca la observación que sea de tipo alimentacion
				if(observacion.getCodigoTipo().equals(ConstantesIntegridadDominio.acronimoValNutricionalMayor2Anios))
				{
					//Si ya tenía valor se muestra a manera de resumen
					if(!observacion.getValor().equals(""))
					{
						contenido+= observacion.getValor()+"\n";
					}
				}	
			} 
		}
		return contenido;
	}
	
	/**
	 * 
	 * @param escala
	 * @return
	 */
	public static String armarContenidoEscala(DtoEscala escala) 
	{
		String contenido=escala.getDescripcion()+"\n\n";
		
		for(int i=0;i<escala.getSecciones().size();i++)
		{
			DtoSeccionParametrizable seccion = escala.getSecciones().get(i);
			if(seccion.isVisible())
			{
				contenido+= seccion.getDescripcion()+"\n";
				if(seccion.getCampos().size()>0)
				{
					for(int j=0;j<seccion.getCampos().size();j++)
					{
						DtoCampoParametrizable campo = seccion.getCampos().get(j);
						contenido+=campo.getEtiqueta()+"  VALOR: "+campo.getValor()+"  OBSERVACIONES: "+campo.getObservaciones()+"\n";
					}
				}	
			}
		}
		
		contenido+="CLASIFICACION TOTAL: "+escala.getTotalEscala()+"   FACTOR PREDICCION: "+escala.getNombreFactorPrediccion()+"\n\n";
		
		if(!UtilidadTexto.isEmpty(escala.getObservaciones()))
		{
			contenido+="OBSERVACIONES: "+escala.getObservaciones()+"\n\n";
		}
		return contenido;
	}
	
	/**
	 * 
	 * @param seccion
	 * @return
	 */
	public static String armarContenidoSeccion(DtoSeccionParametrizable seccion) 
	{
		String contenido="\n";
		for(int i=0;i<seccion.getCampos().size();i++)
		{
			DtoCampoParametrizable campo = seccion.getCampos().get(i);
			contenido+=armarCampo(campo);
		}
		
		for(int i=0;i<seccion.getSecciones().size();i++)
		{
			DtoSeccionParametrizable subseccion2 = seccion.getSecciones().get(i);
			if(!subseccion2.getDescripcion().equals(""))
			{
				contenido+=subseccion2.getDescripcion()+"\n";
			}
			
			for(int j=0;j<subseccion2.getCampos().size();j++)
			{
				DtoCampoParametrizable campo = subseccion2.getCampos().get(j);
				contenido+=armarCampo(campo);
			}
			
			for(int j=0;j<subseccion2.getSecciones().size();j++)
			{
				DtoSeccionParametrizable subseccion3 = subseccion2.getSecciones().get(j);
				if(!subseccion3.getDescripcion().equals(""))
				{
					contenido+=subseccion3.getDescripcion()+"\n";
				}
				for(int k=0;k<subseccion3.getCampos().size();k++)
				{
					DtoCampoParametrizable campo = subseccion3.getCampos().get(k);
					contenido+=armarCampo(campo);
				}
			}	
		}	
		return contenido;
	}
	
	/**
	 * 
	 * @param campo
	 * @return
	 */
	public static String armarCampo(DtoCampoParametrizable campo)
	{
		String contenido="\n";
		if(campo.isMostrar())
		{
			//el unico que va ha cambiar es el de tipo checkbox
			if(campo.getTipoHtml().equals(ConstantesCamposParametrizables.campoTipoCheckBox))
			{
				contenido+=campo.getEtiqueta()+"---->";
				for(int i=0;i<campo.getOpciones().size();i++)
				{
					if(UtilidadTexto.getBoolean(campo.getOpciones().get(i).getSeleccionado()))
					{
						contenido+=campo.getOpciones().get(i).getValor()+" , ";
					}
				}
				contenido+="\n";
			}
			else
			{
				contenido+=campo.getEtiqueta()+": "+campo.getValor()+"\n";
			}
		}
		return contenido;
	}
	
	/**
	 * 
	 * @param seccionFija
	 * @param dtoValoracion
	 * @return
	 */
	public static String armarContenidoValoracionInformacionGeneral(DtoSeccionFija seccionFija, DtoValoracionHospitalizacion dtoValoracion, int tipoEvolucion) 
	{
		String contenido= 	seccionFija.getNombreSeccion()+" \n" +
							"TIPO RECARGO: "+dtoValoracion.getValoracionConsulta().getNombreTipoRecargo()+" \n" +
							"EDAD: "+dtoValoracion.getEdad()+" \n" +
							"FUE ACCIDENTE TRABAJO: "+UtilidadTexto.imprimirSiNo(dtoValoracion.getEventoFueAccidenteTrabajo()+"")+" \n" +
							"ORIGEN DE LA ADMISION: "+dtoValoracion.getNombreOrigenAdmision()+"\n";
		
		if(dtoValoracion.getCodigoOrigenAdmision()!=ConstantesBD.codigoOrigenAdmisionHospitalariaEsUrgencias)
		{
			contenido+="DESCRIPCION: "+dtoValoracion.getTextoOrigenNoUrgencias()+"\n";
		}
		
		for(int w=0; w<dtoValoracion.getObservaciones().size(); w++)
		{
			DtoValoracionObservaciones motivoConsulta= dtoValoracion.getObservaciones().get(w);
			
			if(motivoConsulta.getTipo().equals(ConstantesIntegridadDominio.acronimoMotivoConsulta))
			{
				//para las valoraciones iniciales debe enviarse de forma automática a la consulta de epicrisis y no dejarla editar en el cuadro de texto
				if(tipoEvolucion!=ConstantesBD.codigoTipoEvolucionEpicrisisValoracionInicialHosp)
				{
					contenido+= "\n"+motivoConsulta.getLabel()+" \n" +
								motivoConsulta.getValor()+" \n\n";
				}	
			}
		}
		
		for(int w=0; w<dtoValoracion.getObservaciones().size(); w++)
		{
			DtoValoracionObservaciones motivoConsulta= dtoValoracion.getObservaciones().get(w);
			//Se busca la observación que sea de tipo motivo consulta
			if(motivoConsulta.getTipo().equals(ConstantesIntegridadDominio.acronimoEnfermedadActual))
			{
				contenido+= motivoConsulta.getLabel()+" \n" +
				motivoConsulta.getValor()+" \n\n";
			}
		}
		
		return contenido;
	}
	
	
	/**
	 * 
	 * @param seccionFija
	 * @param dtoValoracion
	 * @return
	 */
	public static String armarContenidoValoracionUrgInformacionGeneral(DtoSeccionFija seccionFija, DtoValoracionUrgencias dtoValoracion) 
	{
		String contenido= 	seccionFija.getNombreSeccion()+" \n" +
							"TIPO RECARGO: "+dtoValoracion.getValoracionConsulta().getNombreTipoRecargo()+" \n" +
							"FECHA: "+dtoValoracion.getFechaValoracion()+" \n" +
							"HORA: "+dtoValoracion.getHoraValoracion()+" \n" +
							"FUE ACCIDENTE TRANSITO?: "+UtilidadTexto.imprimirSiNo(dtoValoracion.getEventoFueAccidenteTrabajo()+"")+" \n" +
							"EDAD: "+dtoValoracion.getEdad()+" \n";
		
		if(!UtilidadTexto.isEmpty(dtoValoracion.getEstadoLlegada()+""))
		{
			contenido+= "EL PACIENTE LLEGÓ POR SUS PROPIOS MEDIOS? "+UtilidadTexto.imprimirSiNo(dtoValoracion.getEstadoLlegada()+"")+" \n";
			if(!UtilidadTexto.isEmpty(dtoValoracion.getDescripcionEstadoLlegada()))
			{
				contenido+="¿CUAL?: "+dtoValoracion.getDescripcionEstadoLlegada()+"\n";
			}
		}
		
		if(!UtilidadTexto.isEmpty(dtoValoracion.getEstadoEmbriaguez()+""))
		{
			contenido+= "ESTADO EMBRIAGUEZ? "+UtilidadTexto.imprimirSiNo(dtoValoracion.getEstadoEmbriaguez()+"")+" \n";
			if(dtoValoracion.getEstadoEmbriaguez())
			{
				contenido+="TOMAR MUESTRA PARA ALCOHOLEMIA U OTRAS DROGAS\n";
			}
		}
		
		/*LA INFORMACION DEL MOTIVO DE LA CONSULTA SE ENVIA AUTOMATICA
		 * for(int w=0; w<dtoValoracion.getObservaciones().size(); w++)
		{
			DtoValoracionObservaciones motivoConsulta= dtoValoracion.getObservaciones().get(w);
			
			if(motivoConsulta.getTipo().equals(ConstantesIntegridadDominio.acronimoMotivoConsulta))
			{
				//para las valoraciones iniciales debe enviarse de forma automática a la consulta de epicrisis y no dejarla editar en el cuadro de texto
				contenido+= "\n"+motivoConsulta.getLabel()+" \n" +
							motivoConsulta.getValor()+" \n\n";
			}
		}*/
		
		for(int w=0; w<dtoValoracion.getObservaciones().size(); w++)
		{
			DtoValoracionObservaciones motivoConsulta= dtoValoracion.getObservaciones().get(w);
			//Se busca la observación que sea de tipo motivo consulta
			if(motivoConsulta.getTipo().equals(ConstantesIntegridadDominio.acronimoEnfermedadActual))
			{
				contenido+= motivoConsulta.getLabel()+" \n" +
				motivoConsulta.getValor()+" \n\n";
			}
		}
		
		return contenido;
	}
	
	
	
	
	
	
	
	
	/**
	 * 
	 * @param seccionFija
	 * @param dtoValHospitalizacion
	 * @return
	 */
	public static String armarInfoAutomaticaValoraciones(DtoSeccionFija seccionFija,DtoValoracionHospitalizacion dtoValoracion) 
	{
		String infoAutomatica="";
		for(int w=0; w<dtoValoracion.getObservaciones().size(); w++)
		{
			DtoValoracionObservaciones observacion= dtoValoracion.getObservaciones().get(w);
			
			if(observacion.getTipo().equals(ConstantesIntegridadDominio.acronimoMotivoConsulta))
			{
				//para las valoraciones iniciales debe enviarse de forma automática a la consulta de epicrisis y no dejarla editar en el cuadro de texto
				if(!UtilidadTexto.isEmpty(observacion.getValor()))
				{
					infoAutomatica+= "<br>"+observacion.getLabel()+": "+observacion.getValor();
				}	
			}
			//Se busca la observación que sea de tipo plan diagnostico y terapeutico
			if(observacion.getTipo().equals(ConstantesIntegridadDominio.acronimoPlanDiagnosticoTerapeutico))
			{
				//para las valoraciones iniciales debe enviarse de forma automática a la consulta de epicrisis y no dejarla editar en el cuadro de texto
				if(!UtilidadTexto.isEmpty(observacion.getValor()))
				{
					infoAutomatica+= "<br>"+observacion.getLabel()+": "+observacion.getValor();
				}
			}
		}
		
		infoAutomatica+=armarContenidoValoracionesDx(seccionFija, dtoValoracion, "<br>");
		
		return infoAutomatica;
	}
	
	
	/**
	 * 
	 * @param seccionFija
	 * @param dtoValUrgencias
	 * @return
	 */
	public static String armarInfoAutomaticaValoracionesUrgencias(DtoSeccionFija seccionFija,DtoValoracionUrgencias dtoValoracion) 
	{
		String infoAutomatica="";
		for(int w=0; w<dtoValoracion.getObservaciones().size(); w++)
		{
			DtoValoracionObservaciones observacion= dtoValoracion.getObservaciones().get(w);
			
			if(observacion.getTipo().equals(ConstantesIntegridadDominio.acronimoMotivoConsulta))
			{
				//para las valoraciones iniciales debe enviarse de forma automática a la consulta de epicrisis y no dejarla editar en el cuadro de texto
				if(!UtilidadTexto.isEmpty(observacion.getValor()))
				{
					infoAutomatica+= "<br>"+observacion.getLabel()+": "+observacion.getValor();
				}	
			}
			//Se busca la observación que sea de tipo plan diagnostico y terapeutico
			if(observacion.getTipo().equals(ConstantesIntegridadDominio.acronimoPlanDiagnosticoTerapeutico))
			{
				//para las valoraciones iniciales debe enviarse de forma automática a la consulta de epicrisis y no dejarla editar en el cuadro de texto
				if(!UtilidadTexto.isEmpty(observacion.getValor()))
				{
					infoAutomatica+= "<br>"+observacion.getLabel()+": "+observacion.getValor();
				}
			}
		}
		
		infoAutomatica+=armarContenidoValoracionesUrgDx(seccionFija, dtoValoracion, "<br>");
		
		return infoAutomatica;
	}
	
	
	/**
	 * 
	 * @param seccionFija
	 * @param dtoValoracion
	 * @return
	 */
	public static String armarContenidoValoracionRevisionSistemas(DtoSeccionFija seccionFija, DtoValoracionHospitalizacion dtoValoracion) 
	{
		String contenido=	seccionFija.getNombreSeccion()+" \n\n";
		
		for(int w=0; w<dtoValoracion.getRevisionesSistemas().size(); w++)
		{
			DtoRevisionSistema revisionSistema= dtoValoracion.getRevisionesSistemas().get(w);
			contenido+= revisionSistema.getNombre()+" -------- ";
			if(revisionSistema.isMultiple()) 
			{
				contenido+=" ES MULTIPLE -------- ";
				for(int i=0;i<revisionSistema.getOpciones().size();i++)
				{
					contenido+=revisionSistema.getOpciones().get(i).getNombre()+" - ";	
				}
			}
			else
			{
				contenido+= revisionSistema.getValor()+" "+revisionSistema.getUnidadMedida()+"   ";
			}
			if(!UtilidadTexto.isEmpty(revisionSistema.getEstadoNormal()+""))
			{	
				if(revisionSistema.getEstadoNormal())
				{
					contenido+=" -------- "+ revisionSistema.getValorVerdadero()+" ";
				}	
				else
				{
					contenido+=" -------- "+ revisionSistema.getValorFalso()+" ";
				}
			}	
			
			contenido+=" -------- DESCRIPCION: "+revisionSistema.getDescripcion()+" \n\n";
		}
		
		return contenido;
	}
	
	/**
	 * 
	 * @param seccionFija
	 * @param dtoValoracion
	 * @return
	 */
	public static String armarContenidoValoracionUrgRevisionSistemas(DtoSeccionFija seccionFija, DtoValoracionUrgencias dtoValoracion) 
	{
		String contenido=	seccionFija.getNombreSeccion()+" \n\n";
		
		for(int w=0; w<dtoValoracion.getRevisionesSistemas().size(); w++)
		{
			DtoRevisionSistema revisionSistema= dtoValoracion.getRevisionesSistemas().get(w);
			contenido+= revisionSistema.getNombre()+" -------- ";
			if(revisionSistema.isMultiple()) 
			{
				contenido+=" ES MULTIPLE -------- ";
				for(int i=0;i<revisionSistema.getOpciones().size();i++)
				{
					contenido+=revisionSistema.getOpciones().get(i).getNombre()+" - ";	
				}
			}
			else
			{
				contenido+= revisionSistema.getValor()+" "+revisionSistema.getUnidadMedida()+"   ";
			}
			if(!UtilidadTexto.isEmpty(revisionSistema.getEstadoNormal()+""))
			{	
				if(revisionSistema.getEstadoNormal())
				{
					contenido+=" -------- "+ revisionSistema.getValorVerdadero()+" ";
				}	
				else
				{
					contenido+=" -------- "+ revisionSistema.getValorFalso()+" ";
				}
			}	
			
			contenido+=" -------- DESCRIPCION: "+revisionSistema.getDescripcion()+" \n\n";
		}
		
		return contenido;
	}
	
	/**
	 * 
	 * @param seccionFija
	 * @param dtoValoracion
	 * @return
	 */
	public static String armarContenidoValoracionesCausaExterna(DtoSeccionFija seccionFija, DtoValoracionHospitalizacion dtoValoracion) 
	{
		String contenido= 	seccionFija.getNombreSeccion()+"\n" +
							dtoValoracion.getNombreCausaExterna()+"\n\n";
		return contenido;
	}
	
	/**
	 * 
	 * @param seccionFija
	 * @param dtoValoracion
	 * @return
	 */
	public static String armarContenidoValoracionesUrgCausaExterna(DtoSeccionFija seccionFija, DtoValoracionUrgencias dtoValoracion) 
	{
		String contenido= 	seccionFija.getNombreSeccion()+"\n" +
							dtoValoracion.getNombreCausaExterna()+"\n\n";
		return contenido;
	}
	
	/**
	 * 
	 * @param seccionFija
	 * @param dtoValoracion
	 * @return
	 */
	public static String armarContenidoValoracionesFinalidadConsulta(DtoSeccionFija seccionFija, DtoValoracionHospitalizacion dtoValoracion) 
	{
		String contenido= 	seccionFija.getNombreSeccion()+"\n" +
							dtoValoracion.getValoracionConsulta().getNombreFinalidadConsulta()+"\n\n";
		return contenido;
	}
	
	/**
	 * 
	 * @param seccionFija
	 * @param dtoValoracion
	 * @return
	 */
	public static String armarContenidoValoracionesUrgFinalidadConsulta(DtoSeccionFija seccionFija, DtoValoracionUrgencias dtoValoracion) 
	{
		String contenido= 	seccionFija.getNombreSeccion()+"\n" +
							dtoValoracion.getValoracionConsulta().getNombreFinalidadConsulta()+"\n\n";
		return contenido;
	}
	
	/**
	 * 
	 * @param seccionFija
	 * @param dtoValoracion
	 * @return
	 */
	public static String armarContenidoValoracionesDx(DtoSeccionFija seccionFija, DtoValoracionHospitalizacion dtoVal, String saltoLinea) 
	{
		String contenido=	saltoLinea;
							
		if(!UtilidadTexto.isEmpty(dtoVal.getDiagnosticoIngreso().getValor()))
		{
			contenido+="DX. INGRESO: "+dtoVal.getDiagnosticoIngreso().getAcronimo()+"-"+dtoVal.getDiagnosticoIngreso().getTipoCIE()+" "+dtoVal.getDiagnosticoIngreso().getNombre()+saltoLinea;
		}
		
		if(!UtilidadTexto.isEmpty(dtoVal.getDiagnosticos().get(0).getValor()))
		{
			contenido+=	"DX. PRINCIPAL: ";
			contenido+=	dtoVal.getDiagnosticos().get(0).getAcronimo()+" - "+dtoVal.getDiagnosticos().get(0).getTipoCIE()+" "+dtoVal.getDiagnosticos().get(0).getNombre()+saltoLinea;
		}
		contenido+=	"TIPO DX PRINCIPAL: "+dtoVal.getValoracionConsulta().getNombreTipoDiagnostico()+saltoLinea;
		
		if(dtoVal.getDiagnosticos().size()>1)
		{
			contenido+="DX. RELACIONADOS ";
			for(int i=1;i<dtoVal.getDiagnosticos().size();i++)
			{
				contenido+=saltoLinea+"DX. RELACIONADO No. "+i+": "+dtoVal.getDiagnosticos().get(i).getAcronimo()+" - "+dtoVal.getDiagnosticos().get(i).getTipoCIE()+" "+dtoVal.getDiagnosticos().get(i).getNombre();
			}
		}
		
		return contenido;
	}
	
	/**
	 * 
	 * @param seccionFija
	 * @param dtoValoracion
	 * @return
	 */
	public static String armarContenidoValoracionesUrgDx(DtoSeccionFija seccionFija, DtoValoracionUrgencias dtoVal, String saltoLinea) 
	{
		String contenido=	saltoLinea+seccionFija.getNombreSeccion()+saltoLinea;
		contenido+=	"DX. PRINCIPAL: ";
		
		if(!UtilidadTexto.isEmpty(dtoVal.getDiagnosticos().get(0).getValor()))
		{
			contenido+=	dtoVal.getDiagnosticos().get(0).getAcronimo()+" - "+dtoVal.getDiagnosticos().get(0).getTipoCIE()+" "+dtoVal.getDiagnosticos().get(0).getNombre()+saltoLinea;
		}
		contenido+=	"TIPO DX PRINCIPAL: "+dtoVal.getValoracionConsulta().getNombreTipoDiagnostico()+saltoLinea;
		
		if(dtoVal.getDiagnosticos().size()>1)
		{
			contenido+="DX. RELACIONADOS "+saltoLinea;
			for(int i=1;i<dtoVal.getDiagnosticos().size();i++)
			{
				contenido+="DX. RELACIONADO No. "+i+": "+dtoVal.getDiagnosticos().get(i).getAcronimo()+" - "+dtoVal.getDiagnosticos().get(i).getTipoCIE()+" "+dtoVal.getDiagnosticos().get(i).getNombre();
			}
		}
		
		return contenido;
	}
	
	
	/**
	 * 
	 * @param seccionFija
	 * @param dtoValoracion
	 * @return
	 */
	public static String armarContenidoValoracionesObservaciones(DtoSeccionFija seccionFija, DtoValoracionHospitalizacion dtoVal, int tipoEvolucion) 
	{
		String contenido= 	seccionFija.getNombreSeccion()+"\n\n";
		
		for(int w=0; w<dtoVal.getObservaciones().size(); w++)
		{
			DtoValoracionObservaciones observacion= dtoVal.getObservaciones().get(w);
			//Se busca la observación que sea de tipo plan diagnostico y terapeutico
			if(observacion.getTipo().equals(ConstantesIntegridadDominio.acronimoPlanDiagnosticoTerapeutico))
			{
				//para las valoraciones iniciales debe enviarse de forma automática a la consulta de epicrisis y no dejarla editar en el cuadro de texto
				if(tipoEvolucion!=ConstantesBD.codigoTipoEvolucionEpicrisisValoracionInicialHosp)
				{
					if(!UtilidadTexto.isEmpty(observacion.getValor()))
					{
						contenido+=	ValoresPorDefecto.getIntegridadDominio(ConstantesIntegridadDominio.acronimoPlanDiagnosticoTerapeutico)+":\n";
						contenido+= observacion.getValor()+"\n\n";
					}
				}	
			}
		}
		
		for(int w=0; w<dtoVal.getObservaciones().size(); w++)
		{
			DtoValoracionObservaciones observacion= dtoVal.getObservaciones().get(w);
			//Se busca la observación que sea de tipo comentarios generales
			if(observacion.getTipo().equals(ConstantesIntegridadDominio.acronimoComentariosGenerales))
			{
				//Si ya tenía valor se muestra a manera de resumen
				if(!observacion.getValor().equals(""))
				{
					contenido+= ValoresPorDefecto.getIntegridadDominio(ConstantesIntegridadDominio.acronimoComentariosGenerales)+": \n";
					contenido+=observacion.getValor()+"\n\n";
				}
			} 
		}
		
		for(int w=0; w<dtoVal.getObservaciones().size(); w++)
		{
			DtoValoracionObservaciones observacion= dtoVal.getObservaciones().get(w);
			//Se busca la observación que sea de tipo comentarios generales
			if(observacion.getTipo().equals(ConstantesIntegridadDominio.acronimoPronostico))
			{
				//Si ya tenía valor se muestra a manera de resumen
				if(!observacion.getValor().equals(""))
				{
					contenido+=ValoresPorDefecto.getIntegridadDominio(ConstantesIntegridadDominio.acronimoPronostico)+": \n";
					contenido+=observacion.getValor()+"\n\n";
				}
			} 
		}
		return contenido;
	}
	
	/**
	 * 
	 * @param seccionFija
	 * @param dtoValoracion
	 * @return
	 */
	public static String armarContenidoValoracionesUrgObservaciones(DtoSeccionFija seccionFija, DtoValoracionUrgencias dtoVal) 
	{
		String contenido= 	seccionFija.getNombreSeccion()+"\n\n";
		
		for(int w=0; w<dtoVal.getObservaciones().size(); w++)
		{
			DtoValoracionObservaciones observacion= dtoVal.getObservaciones().get(w);
			//Se busca la observación que sea de tipo comentarios generales
			if(observacion.getTipo().equals(ConstantesIntegridadDominio.acronimoComentariosGenerales))
			{
				//Si ya tenía valor se muestra a manera de resumen
				if(!observacion.getValor().equals(""))
				{
					contenido+= ValoresPorDefecto.getIntegridadDominio(ConstantesIntegridadDominio.acronimoComentariosGenerales)+": \n";
					contenido+=observacion.getValor()+"\n\n";
				}
			} 
		}
		
		for(int w=0; w<dtoVal.getObservaciones().size(); w++)
		{
			DtoValoracionObservaciones observacion= dtoVal.getObservaciones().get(w);
			//Se busca la observación que sea de tipo comentarios generales
			if(observacion.getTipo().equals(ConstantesIntegridadDominio.acronimoPronostico))
			{
				//Si ya tenía valor se muestra a manera de resumen
				if(!observacion.getValor().equals(""))
				{
					contenido+=ValoresPorDefecto.getIntegridadDominio(ConstantesIntegridadDominio.acronimoPronostico)+": \n";
					contenido+=observacion.getValor()+"\n\n";
				}
			} 
		}
		return contenido;
	}
	
	/**
	 * 
	 * @param seccionFija
	 * @param dtoVal
	 * @return
	 */
	public static String armarContenidoSeccionesParametrizablesValoraciones(DtoSeccionFija seccionFija, DtoValoracionHospitalizacion dtoVal)
	{
		String contenido="";
		if(seccionFija.getNumElementosVisibles()>0)
		{
			for(int w=0; w<seccionFija.getElementos().size(); w++)
			{
				DtoElementoParam elemento=seccionFija.getElementos().get(w);
				if(elemento.isVisible())
				{
					if(!elemento.isSeccion()||(elemento.isSeccion()&&!elemento.getCodigo().equals("")&&!elemento.getDescripcion().equals(""))) 
					{
						contenido+= elemento.getDescripcion();
					}
					
					if(elemento.isComponente()) 
					{
						DtoComponente componente = (DtoComponente)elemento;
						switch(componente.getCodigoTipo())
						{
							//Componente de signos vitales
							case ConstantesCamposParametrizables.tipoComponenteSignosVitales:
								contenido+= armarContenidoSignosVitales(dtoVal.getSignosVitales());
							break;
							case ConstantesCamposParametrizables.tipoComponenteGinecologia:
								contenido+= armarContenidoGinecologia(dtoVal.getHistoriaMenstrual());
							break;
							case ConstantesCamposParametrizables.tipoComponenteOftalmologia:
								contenido+= armarContenidoOftalmologia(dtoVal.getOftalmologia());	
							break;
							case ConstantesCamposParametrizables.tipoComponentePediatria:
								contenido+= armarContenidoPediatria(dtoVal.getPediatria());
							break;
						}
						
						for(int a=0;a<componente.getElementos().size();a++)
						{
							if(componente.getElementos().get(a).isVisible())
							{
								if(componente.getElementos().get(a).isEscala())
								{
									DtoEscala escala = (DtoEscala)componente.getElementos().get(a);
									contenido+=armarContenidoEscala(escala);
								}
								else if(componente.getElementos().get(a).isSeccion())
								{
									DtoSeccionParametrizable seccion = (DtoSeccionParametrizable)componente.getElementos().get(a);
									contenido+=armarContenidoSeccion(seccion);
								}
							}
						}
					}	
					else if(elemento.isEscala())
					{
						DtoEscala escala = (DtoEscala)elemento;
						contenido+= armarContenidoEscala(escala);
					}
					else if(elemento.isSeccion())
					{
						DtoSeccionParametrizable seccion = (DtoSeccionParametrizable)elemento;
						contenido+=armarContenidoSeccion(seccion);
					}
				}
			}	
		}
		return contenido;
	}
	
	/**
	 * 
	 * @param seccionFija
	 * @param dtoVal
	 * @return
	 */
	public static String armarContenidoSeccionesParametrizablesValoracionesUrg(DtoSeccionFija seccionFija, DtoValoracionUrgencias dtoVal)
	{
		String contenido="";
		if(seccionFija.getNumElementosVisibles()>0)
		{
			for(int w=0; w<seccionFija.getElementos().size(); w++)
			{
				DtoElementoParam elemento=seccionFija.getElementos().get(w);
				if(elemento.isVisible())
				{
					if(!elemento.isSeccion()||(elemento.isSeccion()&&!elemento.getCodigo().equals("")&&!elemento.getDescripcion().equals(""))) 
					{
						contenido+= elemento.getDescripcion();
					}
					
					if(elemento.isComponente()) 
					{
						DtoComponente componente = (DtoComponente)elemento;
						switch(componente.getCodigoTipo())
						{
							//Componente de signos vitales
							case ConstantesCamposParametrizables.tipoComponenteSignosVitales:
								contenido+= armarContenidoSignosVitales(dtoVal.getSignosVitales());
							break;
							case ConstantesCamposParametrizables.tipoComponenteGinecologia:
								contenido+= armarContenidoGinecologia(dtoVal.getHistoriaMenstrual());
							break;
							case ConstantesCamposParametrizables.tipoComponenteOftalmologia:
								contenido+= armarContenidoOftalmologia(dtoVal.getOftalmologia());	
							break;
							case ConstantesCamposParametrizables.tipoComponentePediatria:
								contenido+= armarContenidoPediatria(dtoVal.getPediatria());
							break;
						}
						
						for(int a=0;a<componente.getElementos().size();a++)
						{
							if(componente.getElementos().get(a).isVisible())
							{
								if(componente.getElementos().get(a).isEscala())
								{
									DtoEscala escala = (DtoEscala)componente.getElementos().get(a);
									contenido+=armarContenidoEscala(escala);
								}
								else if(componente.getElementos().get(a).isSeccion())
								{
									DtoSeccionParametrizable seccion = (DtoSeccionParametrizable)componente.getElementos().get(a);
									contenido+=armarContenidoSeccion(seccion);
								}
							}
						}
					}	
					else if(elemento.isEscala())
					{
						DtoEscala escala = (DtoEscala)elemento;
						contenido+= armarContenidoEscala(escala);
					}
					else if(elemento.isSeccion())
					{
						DtoSeccionParametrizable seccion = (DtoSeccionParametrizable)elemento;
						contenido+=armarContenidoSeccion(seccion);
					}
				}
			}	
		}
		return contenido;
	}
	
	/**
	 * 
	 * @param seccionFija
	 * @param dto
	 * @return
	 */
	public static String armarContenidoEvolucionInformacionGeneral(DtoSeccionFija seccionFija, DtoEvolucion evolucion) 
	{
		String contenido= 	seccionFija.getNombreSeccion()+" \n" +
							"FECHA: "+evolucion.getFechaEvolucion()+" \n" +
							"HORA: "+evolucion.getHoraEvolucion()+" \n" +
							"COBRABLE: "+UtilidadTexto.imprimirSiNo(evolucion.isCobrable()+"")+" \n\n";
		
		return contenido;
	}
	
	/**
	 * 
	 * @param seccionFija
	 * @param dtoValoracion
	 * @return
	 */
	public static String armarContenidoEvolucionesDx(DtoSeccionFija seccionFija, DtoEvolucion evolucion) 
	{
		String contenido=	seccionFija.getNombreSeccion()+"\n";
		
		if(!UtilidadTexto.isEmpty(evolucion.getDiagnosticoComplicacion1().getValor()))
		{
			contenido+="DX. COMPLICACION: "+evolucion.getDiagnosticoComplicacion1().getAcronimo()+"-"+evolucion.getDiagnosticoComplicacion1().getTipoCIE()+" "+evolucion.getDiagnosticoComplicacion1().getNombre()+"\n";
		}
		
		if(!UtilidadTexto.isEmpty(evolucion.getDiagnosticoPrincipal().getValor()))
		{
			contenido+="DX. PRINCIPAL: "+evolucion.getDiagnosticoPrincipal().getAcronimo()+"-"+evolucion.getDiagnosticoPrincipal().getTipoCIE()+" "+evolucion.getDiagnosticoPrincipal().getNombre()+"\n";
		}
		
		contenido+="TIPO DX. PRINCIPAL: "+evolucion.getNombreTipoDiagnosticoPrincipal()+"\n";
		
		for(int j=1;j<evolucion.getDiagnosticos().size();j++)
		{
			Diagnostico diagRel= evolucion.getDiagnosticos().get(j);
			contenido+="DIAGNOSTICO RELACIONADO No."+j+" "+diagRel.getAcronimo()+" - "+diagRel.getTipoCIE()+" - "+diagRel.getNombre()+"\n";
		}
		
		return contenido+"\n";
	}

	
	/**
	 * 
	 * @param seccionFija
	 * @param dtoEvolucion
	 * @return
	 */
	public static String armarContenidoEvolucionesDatosSubjetivos(DtoSeccionFija seccionFija, DtoEvolucion evolucion) 
	{
		String contenido="";
		if(!UtilidadTexto.isEmpty(evolucion.getInformacionDadaPaciente()))
		{	
			contenido= seccionFija.getNombreSeccion()+"\n";
			contenido+=evolucion.getInformacionDadaPaciente()+"\n\n";
		}	
		return contenido;
	}

	/**
	 * 
	 * @param seccionFija
	 * @param dtoEvolucion
	 * @return
	 */
	public static String armarContenidoEvolucionesHallazgosImportantes(DtoSeccionFija seccionFija, DtoEvolucion evolucion) 
	{
		String contenido="";
		if(!UtilidadTexto.isEmpty(evolucion.getHallazgosImportantes()))
		{	
			contenido= seccionFija.getNombreSeccion()+"\n";
			contenido+=evolucion.getHallazgosImportantes()+"\n\n";
		}	
		return contenido;
	}

	/**
	 * 
	 * @param seccionFija
	 * @param dtoEvolucion
	 * @return
	 */
	public static String armarContenidoEvolucionesAnalisis(DtoSeccionFija seccionFija, DtoEvolucion evolucion) 
	{
		String contenido="";
		if(!UtilidadTexto.isEmpty(evolucion.getDescComplicacion()))
		{	
			contenido= seccionFija.getNombreSeccion()+"\n";
			contenido+=evolucion.getDescComplicacion()+"\n\n";
		}	
		return contenido;
	}

	/**
	 * 
	 * @param seccionFija
	 * @param dtoEvolucion
	 * @return
	 */
	public static String armarContenidoEvolucionesPlanManejo(DtoSeccionFija seccionFija, DtoEvolucion evolucion) 
	{
		String contenido="";
		if(!UtilidadTexto.isEmpty(evolucion.getPronostico()))
		{	
			contenido= seccionFija.getNombreSeccion()+"\n";
			contenido+=evolucion.getPronostico()+"\n\n";
		}	
		return contenido;
	}

	/**
	 * 
	 * @param seccionFija
	 * @param dtoEvolucion
	 * @return
	 */
	public static String armarContenidoEvolucionesConductaSeguir(DtoSeccionFija seccionFija, DtoEvolucion evolucion) 
	{
		String contenido=	seccionFija.getNombreSeccion()+": "+evolucion.getNombreConductaSeguir()+"\n";
		if(evolucion.getCodigoConductaSeguir()==ConstantesBD.codigoConductaASeguirRemitirEvolucion)
		{
			contenido+="TIPO REFERENCIA: "+evolucion.getTipoReferencia()+"\n";
		}
		if(!UtilidadTexto.isEmpty(evolucion.getDiagnosticoMuerte().getValor()))
		{
			contenido+="DX. DE MUERTE: "+evolucion.getDiagnosticoMuerte().getAcronimo()+"-"+evolucion.getDiagnosticoMuerte().getTipoCIE()+" "+evolucion.getDiagnosticoMuerte().getNombre()+"\n";
		}
		
		return contenido+"\n";
	}
	
	/**
	 * 
	 * @param seccionFija
	 * @param dtoVal
	 * @return
	 */
	public static String armarContenidoSeccionesParametrizablesEvoluciones(DtoSeccionFija seccionFija, DtoEvolucion dtoVal)
	{
		String contenido="";
		if(seccionFija.getNumElementosVisibles()>0)
		{
			for(int w=0; w<seccionFija.getElementos().size(); w++)
			{
				DtoElementoParam elemento=seccionFija.getElementos().get(w);
				if(elemento.isVisible())
				{
					if(!elemento.isSeccion()||(elemento.isSeccion()&&!elemento.getCodigo().equals("")&&!elemento.getDescripcion().equals(""))) 
					{
						contenido+= elemento.getDescripcion()+"\n";
					}
					
					if(elemento.isComponente()) 
					{
						DtoComponente componente = (DtoComponente)elemento;
						switch(componente.getCodigoTipo())
						{
							//Componente de signos vitales
							case ConstantesCamposParametrizables.tipoComponenteSignosVitales:
								contenido+= armarContenidoSignosVitales(dtoVal.getSignosVitales());
							break;
							//@todo colocar el de balance de liquidos cuando Mario la haga
						}
						
						for(int a=0;a<componente.getElementos().size();a++)
						{
							if(componente.getElementos().get(a).isVisible())
							{
								if(componente.getElementos().get(a).isEscala())
								{
									DtoEscala escala = (DtoEscala)componente.getElementos().get(a);
									contenido+=armarContenidoEscala(escala);
								}
								else if(componente.getElementos().get(a).isSeccion())
								{
									DtoSeccionParametrizable seccion = (DtoSeccionParametrizable)componente.getElementos().get(a);
									contenido+=armarContenidoSeccion(seccion);
								}
							}
						}
					}	
					else if(elemento.isEscala())
					{
						DtoEscala escala = (DtoEscala)elemento;
						contenido+= armarContenidoEscala(escala);
					}
					else if(elemento.isSeccion())
					{
						DtoSeccionParametrizable seccion = (DtoSeccionParametrizable)elemento;
						contenido+=armarContenidoSeccion(seccion);
					}
				}
			}	
		}
		return contenido;
	}

	/**
	 * 
	 * @param dtoInterpretacion
	 * @return
	 */
	public static String armarContenidoInterpretacion(DtoInterpretacionSolicitud dtoInterpretacion) 
	{
		String contenido="INTERPRETACION: \n"+dtoInterpretacion.getInterpretacion()+"\n\n";
		return contenido;
	}

	/**
	 * 
	 * @param seccionFija
	 * @param dtoEvolucion
	 * @return
	 */
	public static String armarContenidoEvolucionesComentariosGenerales(	DtoSeccionFija seccionFija, DtoEvolucion evolucion) 
	{
		String contenido="";
		if(evolucion.getComentarios().size()>0)
		{	
			for(int w=0; w<evolucion.getComentarios().size();w++)
			{	
				contenido+= evolucion.getComentarios().get(w).getValor()+"\n";
			}
			contenido+="\n";
		}	
		return contenido;
	}

	/**
	 * 
	 * @param seccionFija
	 * @param dtoValUrgencias
	 * @return
	 */
	public static String armarContenidoValoracionUrgExamenFisico(DtoSeccionFija seccionFija, DtoValoracionUrgencias dtoVal) 
	{
		String contenido=	seccionFija.getNombreSeccion()+" \n\n";
		if(dtoVal.isExisteInformacionExamenFisico())
		{
			if(dtoVal.isExisteInformacionEstadoConciencia())
			{	
				contenido+="ESTADO DE CONCIENCIA: "+dtoVal.getNombreEstadoConciencia()+"\n";
				contenido+="DESCRIPCION: "+dtoVal.getDescripcionEstadoConciencia()+"\n";
			}
			if(!UtilidadTexto.isEmpty(dtoVal.getClasificacionTriage()))
			{
				contenido+="CALIFICACION TRIAGE: "+dtoVal.getClasificacionTriage()+"\n";
				contenido+="OBSERVACIONES:"+dtoVal.getObservacionesClasificacionTriage()+"\n";
			}
		}
		return contenido;
	}

	/**
	 * 
	 * @param seccionFija
	 * @param dtoValUrgencias
	 * @return
	 */
	public static String armarContenidoValoracionesUrgConductaSeguir(DtoSeccionFija seccionFija, DtoValoracionUrgencias dtoValUrgencias) 
	{
		String contenido=	seccionFija.getNombreSeccion()+" \n\n";
		contenido+=dtoValUrgencias.getNombreConductaValoracion()+"\n";
		if(!UtilidadTexto.isEmpty(dtoValUrgencias.getDescripcionConductaValoracion()))
		{	
			contenido+="DESCRIPCION: "+dtoValUrgencias.getDescripcionConductaValoracion()+"\n";
		}
		if(!UtilidadTexto.isEmpty(dtoValUrgencias.getNombreAutorizador()))
		{	
			contenido+=	"CON LA APROBACION DE: \n" +
						"NOMBRE: "+dtoValUrgencias.getNombreAutorizador()+"  RELACION: "+dtoValUrgencias.getRelacionAutorizador()+"\n";
		}	
		return contenido;
	}

}
