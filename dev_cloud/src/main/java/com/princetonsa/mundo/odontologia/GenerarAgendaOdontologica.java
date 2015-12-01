package com.princetonsa.mundo.odontologia;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.InfoDatosString;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.Utilidades;
import util.clonacion.UtilidadClonacion;

import com.princetonsa.actionform.odontologia.GenerarAgendaOdontologicaForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.odontologia.GenerarAgendaOdontologicaDao;
import com.princetonsa.dto.consultaExterna.DtoConsultorios;
import com.princetonsa.dto.consultaExterna.DtoExcepcionesHorarioAtencion;
import com.princetonsa.dto.odontologia.DtoAgendaOdontologica;
import com.princetonsa.dto.odontologia.DtoExistenciaAgenOdon;
import com.princetonsa.dto.odontologia.DtoGenerarAgenda;
import com.princetonsa.dto.odontologia.DtoHorarioAtencion;
import com.servinte.axioma.mundo.fabrica.consultaexterna.ConsultaExternaFabricaMundo;
import com.servinte.axioma.mundo.interfaz.consultaexterna.IConsultoriosMundo;
 
/**
 * 
 * @author Víctor Hugo Gómez L.
 *
 */

public class GenerarAgendaOdontologica 
{
	private static enum ExcepcionAgenda{ 
								HORA_INICIO_IGUAL,
								HORA_FIN_IGUAL,
								CONTENIDA_TOTALMENTE,
								CONTENIDA_PARCIAL_HORA_INICIO,
								CONTENIDA_PARCIAL_HORA_FIN,
								CONTENIDA_PARCIAL_HORA_INICIO_Y_FIN,
								NO_CONTENIDA
								};

	private boolean esConsultorioOcupado = false;;
	
	private String numAgendasGenAntes="";
	
	private boolean horarioAtencion;
	
	/**
	 * dto generar agenda odontologica  
	 */
	private DtoGenerarAgenda generarAgenda;
	
	public GenerarAgendaOdontologica()
	{
		this.reset();
	}
	
	public void reset()
	{
		this.generarAgenda = new DtoGenerarAgenda();
	}
	/**
	 * Instancia DAO
	 * */
	public static GenerarAgendaOdontologicaDao getGenerarAgendaOdontologicaDao()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getGenerarAgendaOdontologicaDao();		
	}
	
	
	/**
	 * metodo que retorna un string xml de profesionales
	 * @param forma
	 * @return
	 */
	public static String generacionXMLProfesionales(GenerarAgendaOdontologicaForm forma, boolean tipoGeneAjax)
	{
		StringBuffer xml = new StringBuffer();
		String etiquetaAbrir = "", etiquetaCerrar = "";
				
		if(tipoGeneAjax){
			etiquetaAbrir = "%"; etiquetaCerrar = "@";
		}else{
			etiquetaAbrir = "<"; etiquetaCerrar = ">";
		}
		
		HashMap profesionales= new HashMap();
		int m=0;
		int k=0;
		for(DtoAgendaOdontologica elem: forma.getGenerarAgenda().getAgendaOdonGen())
		{
			profesionales.put("numRegistros", k);
			if(m!=0)
			{
				boolean insertar=true;
				int n=Utilidades.convertirAEntero(profesionales.get("numRegistros")+"");
				for(int j=0;j<n;j++)
				{
					if((Utilidades.convertirAEntero(profesionales.get("codigo_"+j)+"")) == elem.getCodigoMedico())
						insertar=false;
				}
				if(insertar)
				{
					profesionales.put("codigo_"+k, elem.getCodigoMedico());
					profesionales.put("convencion_"+k, elem.getConvencion());
					profesionales.put("nombre_"+k, elem.getNombreMedico());
					k++;
				}
			}
			else{
				profesionales.put("codigo_"+k, elem.getCodigoMedico());
				profesionales.put("convencion_"+k, elem.getConvencion());
				profesionales.put("nombre_"+k, elem.getNombreMedico());
				k++;
			}
			m++;
		}
		
		profesionales.put("numRegistros", k);
	
		m= Utilidades.convertirAEntero(profesionales.get("numRegistros")+"");
		
		if(m > 0)
		{
			xml.append(etiquetaAbrir+"Contenido"+etiquetaCerrar);		
			for(int j=0;j<m;j++)
			{
					xml.append(etiquetaAbrir+"Medico"+etiquetaCerrar);
					xml.append(etiquetaAbrir+"cod_medico"+etiquetaCerrar+profesionales.get("codigo_"+j)+etiquetaAbrir+"/cod_medico"+etiquetaCerrar);
					xml.append(etiquetaAbrir+"convencion"+etiquetaCerrar+profesionales.get("convencion_"+j)+etiquetaAbrir+"/convencion"+etiquetaCerrar);
					xml.append(etiquetaAbrir+"nom_medico"+etiquetaCerrar+profesionales.get("nombre_"+j)+etiquetaAbrir+"/nom_medico"+etiquetaCerrar);
					xml.append(etiquetaAbrir+"/Medico"+etiquetaCerrar);
			}
			xml.append(etiquetaAbrir+"/Contenido"+etiquetaCerrar);
		}
		
		
		return xml.toString();
	}
	
	/**
	 * metodo que retorna un string xml de unidades agenda
	 * @param forma
	 * @return
	 */
	public static String generacionXMLUnidadesAgenda(GenerarAgendaOdontologicaForm forma, boolean tipoGeneAjax)
	{
		StringBuffer xml = new StringBuffer();
		String etiquetaAbrir = "", etiquetaCerrar = "";
		String color="";
				
		if(tipoGeneAjax){
			etiquetaAbrir = "%"; etiquetaCerrar = "@";
		}else{
			etiquetaAbrir = "<"; etiquetaCerrar = ">";
		}
		
		HashMap unidades= new HashMap();
		int m=0;
		int k=0;
		for(DtoAgendaOdontologica elem: forma.getGenerarAgenda().getAgendaOdonGen())
		{
			unidades.put("numRegistros", k);
			if(m!=0)
			{
				boolean insertar=true;
				int n=Utilidades.convertirAEntero(unidades.get("numRegistros")+"");
				for(int j=0;j<n;j++)
				{
					if((Utilidades.convertirAEntero(unidades.get("codigo_"+j)+"")) == elem.getUnidadAgenda())
						insertar=false;
				}
				if(insertar)
				{
					unidades.put("codigo_"+k, elem.getUnidadAgenda());
					unidades.put("descripcion_"+k, elem.getDescripcionUniAgen());
					unidades.put("color_"+k, color);
					k++;
				}
			}
			else{
				unidades.put("codigo_"+k, elem.getUnidadAgenda());
				unidades.put("descripcion_"+k, elem.getDescripcionUniAgen());
				unidades.put("color_"+k, color);
				k++;
			}
			m++;
		}
		
		unidades.put("numRegistros", k);
		
		m=Utilidades.convertirAEntero(unidades.get("numRegistros")+"");
		
		if(m > 0)
		{
			xml.append(etiquetaAbrir+"Contenido"+etiquetaCerrar);
			for(int j=0;j<m;j++)
			{
					try{color=(unidades.get("color_"+j)+"").substring(1,(unidades.get("color_"+j)+"").length());}catch (Exception e) {color="";}
					
					xml.append(etiquetaAbrir+"Unidad_consulta"+etiquetaCerrar);
					xml.append(etiquetaAbrir+"cod_unidad_consulta"+etiquetaCerrar+unidades.get("codigo_"+j)+etiquetaAbrir+"/cod_unidad_consulta"+etiquetaCerrar);
					xml.append(etiquetaAbrir+"nom_unidad_consulta"+etiquetaCerrar+unidades.get("descripcion_"+j)+etiquetaAbrir+"/nom_unidad_consulta"+etiquetaCerrar);
					xml.append(etiquetaAbrir+"color"+etiquetaCerrar+color+etiquetaAbrir+"/color"+etiquetaCerrar);
					xml.append(etiquetaAbrir+"/Unidad_consulta"+etiquetaCerrar);
			}
			xml.append(etiquetaAbrir+"/Contenido"+etiquetaCerrar);
		}
		
		return xml.toString();
	}
	
	/**
	 * metodo que retorna un string xml de parametros para mostrar el horario
	 * @param forma
	 * @return
	 */
	public static String generacionXMLparametros(GenerarAgendaOdontologicaForm forma, boolean tipoGeneAjax)
	{
		StringBuffer xml = new StringBuffer();
		String etiquetaAbrir = "", etiquetaCerrar = "";
				
		if(tipoGeneAjax){
			etiquetaAbrir = "%"; etiquetaCerrar = "@";
		}else{
			etiquetaAbrir = "<"; etiquetaCerrar = ">";
		}

		if(forma.getGenerarAgenda().getAgendaOdonGen().size() > 0)
		{
			xml.append(etiquetaAbrir+"Contenido"+etiquetaCerrar);
		/*	xml.append(etiquetaAbrir+"Centro_atencion"+etiquetaCerrar);
			xml.append(etiquetaAbrir+"cod_centro_atencion"+etiquetaCerrar+forma.getGenerarAgenda().getAgendaOdonGen().get(0).getCentroAtencion()+etiquetaAbrir+"/cod_centro_atencion"+etiquetaCerrar);
			xml.append(etiquetaAbrir+"nom_centro_atencion"+etiquetaCerrar+forma.getGenerarAgenda().getAgendaOdonGen().get(0).getDescripcionCentAten()+etiquetaAbrir+"/nom_centro_atencion"+etiquetaCerrar);
			xml.append(etiquetaAbrir+"/Centro_atencion"+etiquetaCerrar);
			xml.append(etiquetaAbrir+"Fecha"+etiquetaCerrar+forma.getFechaInicial()+etiquetaAbrir+"/Fecha"+etiquetaCerrar);
		*/  xml.append(etiquetaAbrir+"Hora"+etiquetaCerrar);
			xml.append(etiquetaAbrir+"hora_ini_horarios"+etiquetaCerrar+Integer.valueOf(forma.getHoraIniAgenda())+etiquetaAbrir+"/hora_ini_horarios"+etiquetaCerrar);
			xml.append(etiquetaAbrir+"min_ini_horarios"+etiquetaCerrar+Integer.valueOf(forma.getMinIniAgenda())+etiquetaAbrir+"/min_ini_horarios"+etiquetaCerrar);
			xml.append(etiquetaAbrir+"hora_fin_horarios"+etiquetaCerrar+Integer.valueOf(forma.getHoraFinAgenda())+etiquetaAbrir+"/hora_fin_horarios"+etiquetaCerrar);
			xml.append(etiquetaAbrir+"min_fin_horarios"+etiquetaCerrar+Integer.valueOf(forma.getMinFinAgenda())+etiquetaAbrir+"/min_fin_horarios"+etiquetaCerrar);
			xml.append(etiquetaAbrir+"multiplo"+etiquetaCerrar+Integer.valueOf(forma.getMultiploMinGenCita())+etiquetaAbrir+"/multiplo"+etiquetaCerrar);
			xml.append(etiquetaAbrir+"/Hora"+etiquetaCerrar);
			xml.append(etiquetaAbrir+"/Contenido"+etiquetaCerrar);
		}

		
		return xml.toString();
	}
	
	/**
	 * metodo que retorna un string xml de agenda odontologica
	 * @param forma
	 * @return
	 */
	public static String generacionXMLAgendaOdon(GenerarAgendaOdontologicaForm forma, boolean tipoGeneAjax)
	{
		StringBuffer xml = new StringBuffer();
		String etiquetaAbrir = "", etiquetaCerrar = "";
		String[] horaInicio = {""};
		String[] horaFinal = {""};
		String horaFinMax="0",mintFinMax="0", horaIni="0", minIni="0", color="";
				
		if(tipoGeneAjax){
			etiquetaAbrir = "%"; etiquetaCerrar = "@";
		}else{
			etiquetaAbrir = "<"; etiquetaCerrar = ">";
		}
		
		if(forma.getGenerarAgenda().getAgendaOdonGen().size() > 0)
		{
			xml.append(etiquetaAbrir+"Contenido"+etiquetaCerrar);
			for(DtoAgendaOdontologica elem: forma.getGenerarAgenda().getAgendaOdonGen())
			{
					horaInicio = elem.getHoraInicio().split(":");
					horaFinal = elem.getHoraFin().split(":");
					try{color=elem.getColorUniAgen().substring(1,elem.getColorUniAgen().length());}catch (Exception e) {color="";}
					
					xml.append(etiquetaAbrir+"Agenda_odontologica"+etiquetaCerrar);
					xml.append(etiquetaAbrir+"codigo_agenda"+etiquetaCerrar+elem.getCodigoPk()+etiquetaAbrir+"/codigo_agenda"+etiquetaCerrar);
					xml.append(etiquetaAbrir+"hora_inicio"+etiquetaCerrar+Integer.valueOf(horaInicio[0])+etiquetaAbrir+"/hora_inicio"+etiquetaCerrar);
					xml.append(etiquetaAbrir+"min_inicio"+etiquetaCerrar+Integer.valueOf(horaInicio[1])+etiquetaAbrir+"/min_inicio"+etiquetaCerrar);
					xml.append(etiquetaAbrir+"hora_fin"+etiquetaCerrar+Integer.valueOf(horaFinal[0])+etiquetaAbrir+"/hora_fin"+etiquetaCerrar);
					xml.append(etiquetaAbrir+"min_fin"+etiquetaCerrar+Integer.valueOf(horaFinal[1])+etiquetaAbrir+"/min_fin"+etiquetaCerrar);
					xml.append(etiquetaAbrir+"convencion_profesional"+etiquetaCerrar+elem.getConvencion()+etiquetaAbrir+"/convencion_profesional"+etiquetaCerrar);
					xml.append(etiquetaAbrir+"color_uni_consulta"+etiquetaCerrar+color+etiquetaAbrir+"/color_uni_consulta"+etiquetaCerrar);
					xml.append(etiquetaAbrir+"Consulorio"+etiquetaCerrar);
					xml.append(etiquetaAbrir+"cod_consultorio"+etiquetaCerrar+elem.getConsultorio()+etiquetaAbrir+"/nombre_consultorio"+etiquetaCerrar);
					xml.append(etiquetaAbrir+"nombre_consultorio"+etiquetaCerrar+elem.getDescripcionConsultorio()+etiquetaAbrir+"/nombre_consultorio"+etiquetaCerrar);
					xml.append(etiquetaAbrir+"/Consultorio"+etiquetaCerrar);
					xml.append(etiquetaAbrir+"/Agenda_odontologica"+etiquetaCerrar);
					if(Utilidades.convertirAEntero(horaIni) == 0){
						horaIni= horaInicio[0];
						minIni= horaInicio[1];
					}					
					if(Utilidades.convertirAEntero(horaFinal[0])> Utilidades.convertirAEntero(horaFinMax)){
						horaFinMax = horaFinal[0];
						mintFinMax = horaFinal[1];
					}else{
						if(Utilidades.convertirAEntero(horaFinal[0]) == Utilidades.convertirAEntero(horaFinMax))
							if(Utilidades.convertirAEntero(horaFinal[1]) > Utilidades.convertirAEntero(mintFinMax))
								mintFinMax = horaFinal[1];
					}
			}
			xml.append(etiquetaAbrir+"/Contenido"+etiquetaCerrar);
		
			forma.setHoraIniAgenda(horaIni);
			forma.setMinIniAgenda(minIni);
			forma.setHoraFinAgenda(horaFinMax);
			forma.setMinFinAgenda(mintFinMax);
		}
		
		
		return xml.toString();
	}
	
	
	/**
	 * TODO ESTE ALGORITMO ESTA MALO TIENE FALLAS
	 * HAY QUE CONSTRUIRLO 
	 * 
	 * generar agenda odontologica
	 * Metodo que construye la agenda Odontologica
	 * @param con
	 * @param parametros
	 * @return
	 */
	public  DtoGenerarAgenda generarAgendaOdontologica(Connection con, DtoGenerarAgenda dto)
	{
		
		//ESTE ATRIBUTO SE UTILIZA PARA ALMACENAR LOS DIAS DE LA SEMANA DE LA UNIDAD DE AGENDA
		ArrayList<ArrayList<DtoHorarioAtencion>> horariosAten = new ArrayList<ArrayList<DtoHorarioAtencion>>();
		
		boolean band = false, enTransaccion = true;
		int diaGenAgenOdon = ConstantesBD.codigoNuncaValido;
		String fechaGenAgenOdon = dto.getFechaInicial();

		// Esta es la primera ves que se ejecuta, no se encuentran horarios de atención sin consultorio asignado
		if(dto.getRegenerarAgendaOdon().equals(""))
		{
			horariosAten = horariosAtencionXDias( obtenerHorariosAtencion(con, 
																			dto.getCentroAtencion()+"",
																			dto.getUnidadAgenda(), 
																			dto.getConsultorio(), 
																			dto.getDiaSemana(),
																			dto.getProfesionalSalud()));
		}
		else
		{
			// Para este caso los consultorios
			if(dto.getRegenerarAgendaOdon().equals(ConstantesBD.acronimoSi))
			{
				horariosAten = horariosAtencionXDias(
													obtenerHorariosAtencion(con,dto.getCodigosHorariosAtencion(),
																				dto.getAgendaOdonXGen()));
			
				if(!this.isEsConsultorioOcupado())
					dto.getAgendaOdonXGen().clear();
			}
		}
		for (int i=0; i<horariosAten.size(); i++) {
			if (!horariosAten.get(i).isEmpty()){
				horarioAtencion = true;
				break;
			} else {
				horarioAtencion = false;
			}
		}

		// 2. se realiza la inserción de las agendas odontológicas
		UtilidadBD.iniciarTransaccion(con);
		
		dto.setAgendaOdonConExcepciones(new ArrayList<DtoExcepcionesHorarioAtencion>());
		
		// EMPEZAR EL CICLO PARA ARMAR LA AGENDA ODONTOLOGICA.
		do{
			if(enTransaccion)
			{
				Log4JManager.info("fecha: "+fechaGenAgenOdon); // FECHA DE GENERACION INICIAL DE LA AGENDA 
				Log4JManager.info("fecha 1 : "+UtilidadFecha.conversionFormatoFechaAAp(fechaGenAgenOdon));
				
				//OBTIENE EL DIA DE LA SEMANA
				diaGenAgenOdon = UtilidadFecha.obtenerNumeroDiaSeman(UtilidadFecha.conversionFormatoFechaAAp(fechaGenAgenOdon));
				Log4JManager.info("dia: "+diaGenAgenOdon); // 
				// DtoAgendaOdontologica dtoAgenOdon = new DtoAgendaOdontologica(); 
				// este ciclo recorre el listado de horarios segun el dia obtenido de la fecha 
				// ademas de los horarios de atencion que no tienen dia asignado (los cuales aplica siu generaciï¿½n para todos los dias)
				
				
				//RECORREMOS LOS HORARIOS DE ATENCION PARA UN DIA
				// CONTENDOR DEL DIA--->diaGenAgenOdon
				
				for(int i=0;i<horariosAten.get(diaGenAgenOdon).size();i++)
				{		
					DtoHorarioAtencion dtoHA = (DtoHorarioAtencion) horariosAten.get(diaGenAgenOdon).get(i);
					DtoAgendaOdontologica dtoAgenOdon = new DtoAgendaOdontologica();
					// 2.1 se carga el dto de AgendaOdontologica
					dtoAgenOdon.setCentroAtencion(dtoHA.getCentroAtencion());
					dtoAgenOdon.setUnidadAgenda(dtoHA.getUnidadConsulta());
					dtoAgenOdon.setConsultorio(dtoHA.getConsultorio());
					dtoAgenOdon.setDia((diaGenAgenOdon+1));
					dtoAgenOdon.setFecha(fechaGenAgenOdon);
					dtoAgenOdon.setFechaInicio(fechaGenAgenOdon);
					dtoAgenOdon.setFechaFin(fechaGenAgenOdon);
					dtoAgenOdon.setHoraInicio(dtoHA.getHoraInicio());
					dtoAgenOdon.setHoraFin(dtoHA.getHoraFin());
					dtoAgenOdon.setCodigoMedico(dtoHA.getCodigoMedico());
					dtoAgenOdon.setCupos(dtoHA.getPacientesSesion());
					dtoAgenOdon.setUsuarioModifica(dto.getUsuarioModifica());
					
					
					// 2.2 verificar si exite una excepcion para el centro de costo en la fecha respectiva 
					if(verificarExcepcionCentroAtencion(con, fechaGenAgenOdon, dtoHA.getCentroAtencion()).equals(ConstantesBD.acronimoNo))
					{
						if(dtoHA.getConsultorio()!=ConstantesBD.codigoNuncaValido)
						{
							// 2.3 Verificar si la agenda ya se encuentra agendada
							
							ArrayList<DtoAgendaOdontologica> listaAgendas=new ArrayList<DtoAgendaOdontologica>();
							
							int resultado=generarExcepcionesCasoSiGenerar(con, dto, diaGenAgenOdon, dtoAgenOdon, listaAgendas);
							if(resultado<0)
							{
								enTransaccion = false; 
							}
							else if(resultado>0)
							{
								dto.setAgenGeneradas(dto.getAgenGeneradas()+resultado);
							}
							else
							{
								// Ahora se buscan las que son para no generar
								enTransaccion = generarExcepcionesCasoNoGenerar(con, dto, diaGenAgenOdon, dtoAgenOdon, listaAgendas);
							}
									
							
							if(listaAgendas.size()>0)
							{
								listadoAgendas: for (DtoAgendaOdontologica dtoAgendaOdontologica : listaAgendas)
								{
									String insertar=isInsertAgendaOdontologica(con, dtoAgendaOdontologica);
									if(insertar.split(ConstantesBD.separadorSplit)[0].equals(ConstantesBD.acronimoNo))
									{
										/*
										 * Como ya existe alguna generada en los rangos, no se genera
										 */
										continue listadoAgendas;
									}
									else
									{
										boolean ingresado=insertarAgenda(con, dto, dtoAgendaOdontologica);
										if(!ingresado)
										{
											Log4JManager.error("Error ingresando los datos de la agenda");
											enTransaccion=false;
											break listadoAgendas;
										}
									}
								}
							}//fin si ya existe agenda odontologica
						}//codigo basicos
						else
						{
							Log4JManager.info("esta entrando ha esta parte para llenar la lista parcial de agEneda de odontologia");
							if(dtoHA.getCentroAtencion()!=ConstantesBD.codigoNuncaValido
									&& dtoHA.getUnidadConsulta()!=ConstantesBD.codigoNuncaValido)
							{
								
								// los dto de aqui hacen parte del listado que se debe mostrar para ingresar nuevamente
								dtoAgenOdon.setCodigoHorarioAtencion(dtoHA.getCodigo());
								dtoAgenOdon = consultarHorarioAtencion(con, dtoAgenOdon);
								String insertar=isInsertAgendaOdontologica(con, dtoAgenOdon);
								if(insertar.split(ConstantesBD.separadorSplit)[0].equals(ConstantesBD.acronimoSi))
								{
									// Filtrar los consultorios
									GenerarAgendaOdontologica.listarConsultoriosParaGenerarAgendaSinConsultorio(dtoAgenOdon);

									// Adicionar la agenda a generar
									dto.getAgendaOdonXGen().add(dtoAgenOdon);
									dto.setAgenXGenerar(dto.getAgenXGenerar()+1);
								}
							}
							else
							{
								dtoAgenOdon.getExcepcionesCenAten().setFechaExcepcion(fechaGenAgenOdon);
								dtoAgenOdon.getExcepcionesCenAten().setCentroAtencion(dtoHA.getCentroAtencion());
								dtoAgenOdon.getExcepcionesCenAten().setMsgExcepcion("No se puede Generar ni Postular para una Regeneraciï¿½n de Agenda Odontolï¿½gica porque faltan datos Requeridos. ");
								dtoAgenOdon.getMsgErrorAgenOdon().add(new InfoDatosString(ConstantesBD.acronimoAgendaOdonError,dtoAgenOdon.getExcepcionesCenAten().getMsgExcepcion()));
								//dto.getAgendaOdonConExcepciones().add(dtoAgenOdon); FIXME JUANDA
								dto.setAgenOdonConExcep(dto.getAgenOdonConExcep()+1);
								Log4JManager.info("no se puede asignar ni postular para que adicion la nueva agenda");
							}

						}
					}//fin verificar existencia de centro de atencion
				}//fin Recorrer HORARIOS DE ATENCION
				
				// se avanza en el tiempo y se valida si el rango de fechas para la asignacion de la agenda
				// todavia se cumple
				fechaGenAgenOdon = UtilidadFecha.incrementarDiasAFecha(UtilidadFecha.conversionFormatoFechaABD(fechaGenAgenOdon), 1, true);
				
				
				
				//EVALUACION SI YA RECORRIMOS TODOS LOS DIAS
				if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(UtilidadFecha.conversionFormatoFechaAAp(fechaGenAgenOdon),
						UtilidadFecha.conversionFormatoFechaAAp(dto.getFechaFinal())))
				{
					band=true;
				}
					
			}//TRANSACCION
			else
			{
				band=true;	//VALIDACION DE LA TRANSACCION
			}
				
			
		}
		while(band==false);
		
		
		
		
		
		if(enTransaccion)
		{
			UtilidadBD.finalizarTransaccion(con);
		}
		else
		{
			UtilidadBD.abortarTransaccion(con);
			return dto;
		}
		
		return dto;
	}

	/**
	 * Generar agenda para el caso en que las excepciones se encuentran
	 * parametrizadas como Generar Agenda --> NO
	 * @param con Conexión con la BD
	 * @param dto DTO con los datos de la agenda en general
	 * @param diaGenAgenOdon Día en que se va a generar la agenda odontológica
	 * @param dtoAgenOdon DTO de la agenda que se desea generar
	 * @param listaAgendasExcepcion lista de las agendas que se van a generar
	 * @return true en caso de insertar correctamente, false de lo contrario
	 */
	private boolean generarExcepcionesCasoNoGenerar(Connection con, DtoGenerarAgenda dto, int diaGenAgenOdon,
			DtoAgendaOdontologica dtoAgenOdon, ArrayList<DtoAgendaOdontologica> listaAgendasExcepcion)
	{
		/*
		 * Se buscan las excepciones para los casos en que no se debe generar la agenda
		 */
		boolean esGenerar=false;
		
		ArrayList<DtoExcepcionesHorarioAtencion> listaExcepciones;
		listaExcepciones=cosultaExAgendaOdontologica(con, 
				dtoAgenOdon.getCentroAtencion()+"",
				dtoAgenOdon.getUnidadAgenda()+"",
				dtoAgenOdon.getConsultorio()+"",
				diaGenAgenOdon+"",
				dtoAgenOdon.getCodigoMedico()+"",
				dtoAgenOdon.getFecha(),
				dtoAgenOdon.getFecha(),
				esGenerar);
		if(listaExcepciones!=null && listaExcepciones.size()>0)
		{
			
			dto.getAgendaOdonConExcepciones().addAll(listaExcepciones);

			listaAgendasExcepcion.add(dtoAgenOdon);
			// Se ejecuta el ciclo hasta procesar todas las excepciones
			while(listaExcepciones.size()>0)
			{
				// Se toma la primera excepción
				DtoExcepcionesHorarioAtencion excepcion=listaExcepciones.get(0);
				procesarExcepcionAgenda(listaAgendasExcepcion, excepcion);
				listaExcepciones.remove(0);
			}
		}
		else
		{
			listaAgendasExcepcion.add(dtoAgenOdon);
		}
		return true;
	}

	private boolean insertarAgenda(Connection con, DtoGenerarAgenda dto, DtoAgendaOdontologica dtoAgenOdon)
	{
		int codigoPkAgenOdon=insertarAgendaOdontologica(con, dtoAgenOdon);
		if(codigoPkAgenOdon<0)
		{
			return false;
		}
		else{
			dtoAgenOdon.setCodigoPk(codigoPkAgenOdon);
			dtoAgenOdon = consultarAgendaOdontologica(con, dtoAgenOdon);
			dto.getAgendaOdonGen().add(dtoAgenOdon);
			dto.setAgenGeneradas(dto.getAgenGeneradas()+1);
		}
		return true;
	}

	/**
	 * Procesa las excepciones de la agenda
	 * @param listaAgendasExcepcion Lista de agendas odontológicas generadas al procesar los horarios
	 * @param excepcion {@link DtoExcepcionesHorarioAtencion}
	 */
	private void procesarExcepcionAgenda(ArrayList<DtoAgendaOdontologica> listaAgendasExcepcion, DtoExcepcionesHorarioAtencion excepcion)
	{
		for(int i=0; i<listaAgendasExcepcion.size(); i++)
		{
			DtoAgendaOdontologica agenda=listaAgendasExcepcion.get(i);
			ExcepcionAgenda resultado=esExcepcionEnAgenda(agenda, excepcion);
			switch (resultado)
			{
				case CONTENIDA_TOTALMENTE:
					listaAgendasExcepcion.remove(i);
					i--;
					
				break;
				case HORA_INICIO_IGUAL:
					agenda.setHoraInicio(excepcion.getHoraFin());
				break;
				case HORA_FIN_IGUAL:
					agenda.setHoraFin(excepcion.getHoraInicio());
				break;
				case CONTENIDA_PARCIAL_HORA_INICIO:
					agenda.setHoraFin(excepcion.getHoraInicio());
				break;
				case CONTENIDA_PARCIAL_HORA_FIN:
					agenda.setHoraInicio(excepcion.getHoraFin());
				break;
				case CONTENIDA_PARCIAL_HORA_INICIO_Y_FIN:
					// Al estar contenida parcialmente, la agenda se divide en 2
					DtoAgendaOdontologica agendaNueva=(DtoAgendaOdontologica) UtilidadClonacion.clonar(agenda);
					agenda.setHoraFin(excepcion.getHoraInicio());
					agendaNueva.setHoraInicio(excepcion.getHoraFin());
					listaAgendasExcepcion.add(agendaNueva);
				break;
				case NO_CONTENIDA:
					// La agenda no está contenida, por tal motivo no se hace nada
				break;
				default:
					Log4JManager.error("Caso excepción agenda desconocido");
				break;
			}
		}
	}


	/**
	 * Verifica si el horario de atención está contenido en una agenda
	 * @param agenda Agenda a comparar
	 * @param excepcion Excepción que se desea ingresar
	 * @return constante que indica el proceso a continuar con la agenda
	 */
	private ExcepcionAgenda esExcepcionEnAgenda(DtoAgendaOdontologica agenda, DtoExcepcionesHorarioAtencion excepcion)
	{
		/*
		 * Caso A
		 * agenda          |          |
		 * excepción       |          |
		 *
		 * Caso B
		 * agenda          |          |
		 * excepción     |              |
		 */
		if(
				excepcion.getHoraInicio().compareTo(agenda.getHoraInicio())<=0 
				&& 
				excepcion.getHoraFin().compareTo(agenda.getHoraFin())>=0
			)
		{
			return ExcepcionAgenda.CONTENIDA_TOTALMENTE;
		}
		/*
		 * agenda      |              |
		 * excepción       |      |
		 */
		else if(
				agenda.getHoraInicio().compareTo(excepcion.getHoraInicio())<=0 
				&& 
				agenda.getHoraFin().compareTo(excepcion.getHoraFin())>=0
			)
		{
			return ExcepcionAgenda.CONTENIDA_PARCIAL_HORA_INICIO_Y_FIN;
		}
		/*
		 * agenda      |              |
		 * excepción   |          |
		 */
		if(agenda.getHoraInicio().equals(excepcion.getHoraInicio()))
		{
			return ExcepcionAgenda.HORA_INICIO_IGUAL;
		}
		/*
		 * agenda      |              |
		 * excepción       |          |
		 */
		else if(agenda.getHoraFin().equals(excepcion.getHoraFin()))
		{
			return ExcepcionAgenda.HORA_FIN_IGUAL;
		}
		else
		{
			/*
			 * agenda      |          |
			 * excepción       |          |
			 */
			if(
					agenda.getFechaInicio().compareTo(excepcion.getFechaInicio())<0 
					&&
					agenda.getFechaFin().compareTo(excepcion.getFechaInicio())>0
					&&
					agenda.getFechaFin().compareTo(excepcion.getFechaFin())<0
				)
			{
				return ExcepcionAgenda.CONTENIDA_PARCIAL_HORA_INICIO;
			}
			/*
			 * agenda          |          |
			 * excepción   |          |
			 */
			if(
					agenda.getFechaInicio().compareTo(excepcion.getFechaInicio())>0
					&&
					agenda.getFechaInicio().compareTo(excepcion.getFechaFin())<0 
					&&
					agenda.getFechaFin().compareTo(excepcion.getFechaFin())>0
				)
			{
				return ExcepcionAgenda.CONTENIDA_PARCIAL_HORA_FIN;
			}
		}
		return ExcepcionAgenda.NO_CONTENIDA;
	}

	/**
	 * Genera las excepciones de horario de atención marcadas con Generar Agenda --> 'SI'
	 * que cumplen con los parámetros de la agenda a generar
	 * Se debe dar prioridad a las excepciones
	 * @param con Conexión con la BD
	 * @param dto DTO con las agendas generadas
	 * @param diaGenAgenOdon Día en el que se va a generar la agenda odontológica
	 * @param dtoAgenOdon DTO con los datos de la agenda a generar
	 * @param usuario Usuario que genera la agenda
	 * @param listaExcepcionesAGenerar Lista de las agendas que se van a generar por excepción
	 * @return entero con -1 en caso de error, 0 en caso de no generar ninguna excepción,
	 * en caso de generar agendas por excepción, retorna la cantidad de agendas generadas
	 */
	private int generarExcepcionesCasoSiGenerar(Connection con, DtoGenerarAgenda dto, int diaGenAgenOdon, DtoAgendaOdontologica dtoAgenOdon, ArrayList<DtoAgendaOdontologica> listaExcepcionesAGenerar)
	{
		// Primero verifico las excepciones para generar
		boolean esGenerar=true;
		// Se consultan las excepciones que cumplan con la agenda que se desea generar
		ArrayList<DtoExcepcionesHorarioAtencion> listaExcepciones=cosultaExAgendaOdontologica(con, 
				dtoAgenOdon.getCentroAtencion()+"",
				dtoAgenOdon.getUnidadAgenda()+"",
				dtoAgenOdon.getConsultorio()+"",
				diaGenAgenOdon+"",
				dtoAgenOdon.getCodigoMedico()+"",
				dtoAgenOdon.getFecha(),
				dtoAgenOdon.getFecha(),
				esGenerar);
		
		int cantidadGenerada=0;
		//si hay excepción de horario de atención
		if(listaExcepciones!=null && listaExcepciones.size()>0)
		{
			dto.getAgendaOdonConExcepciones().addAll(listaExcepciones);
			
			String fechaActual=UtilidadFecha.getFechaActual();
			String horaActual=UtilidadFecha.getHoraActual();
			// Se generan todas las excepciones
			for(DtoExcepcionesHorarioAtencion excepcion:listaExcepciones)
			{
				// Siempre se guarda con u cupo por ser excepción
				excepcion.setCupos(1);
				excepcion.setFecha(dtoAgenOdon.getFecha());
				excepcion.setUsuarioModifica(dtoAgenOdon.getUsuarioModifica());
				excepcion.setFechaModifica(fechaActual);
				excepcion.setHoraModifica(horaActual);
				excepcion.setDia(diaGenAgenOdon+1);
				listaExcepcionesAGenerar.add(excepcion);
			}
		}
		return cantidadGenerada;
	}

	/**
	 * se obtiene un listado de los horarios de atencion que cumple con los parametros 
	 * selecionados para la generacion de la agenda odontologica
	 * @param con
	 * @param centroAtencion
	 * @param unidadConsulta
	 * @param consultorio
	 * @param diaSemana
	 * @param codigoMedico
	 * @return
	 */
	public static ArrayList<DtoHorarioAtencion> obtenerHorariosAtencion(Connection con, 
			String centroAtencion, 
			String unidadConsulta, 
			String consultorio,
			String diaSemana,
			String codigoMedico)
	{
		return getGenerarAgendaOdontologicaDao().obtenerHorariosAtencion(con, centroAtencion, unidadConsulta, consultorio, diaSemana, codigoMedico);
	}
	
	/**
	 * esta consulta retorna una lista de listas de horarios de atencion por dias
	 * Este metodo Organiza los horarios de atencino por dias. y 
	 * @param dtoHA
	 * @return ArrayList<ArrayList<DtoHorarioAtencion>>
	 */
	public static ArrayList<ArrayList<DtoHorarioAtencion>> horariosAtencionXDias (ArrayList<DtoHorarioAtencion> dtoHA)
	{
		ArrayList<ArrayList<DtoHorarioAtencion>> arrayDtoXDias = new ArrayList<ArrayList<DtoHorarioAtencion>>();
		ArrayList<DtoHorarioAtencion> arrayLu = new ArrayList<DtoHorarioAtencion>();
		ArrayList<DtoHorarioAtencion> arrayMa = new ArrayList<DtoHorarioAtencion>();
		ArrayList<DtoHorarioAtencion> arrayMi = new ArrayList<DtoHorarioAtencion>();
		ArrayList<DtoHorarioAtencion> arrayJu = new ArrayList<DtoHorarioAtencion>();
		ArrayList<DtoHorarioAtencion> arrayVi = new ArrayList<DtoHorarioAtencion>();
		ArrayList<DtoHorarioAtencion> arraySa = new ArrayList<DtoHorarioAtencion>();
		ArrayList<DtoHorarioAtencion> arrayDo = new ArrayList<DtoHorarioAtencion>();
		//ArrayList<DtoHorarioAtencion> arrayNoAsig = new ArrayList<DtoHorarioAtencion>();
		for(int i=0;i<dtoHA.size();i++)
		{
			DtoHorarioAtencion dto = (DtoHorarioAtencion) dtoHA.get(i);  
			switch (dto.getDia()) {
			//lunes
			case 1:arrayLu.add(dto);break;
			//martes
			case 2:arrayMa.add(dto);break;
			//miercoles
			case 3:arrayMi.add(dto);break;
			//jueves
			case 4:arrayJu.add(dto);break;
			//viernes
			case 5:arrayVi.add(dto);break;
			//sabado
			case 6:arraySa.add(dto);break;
			//domingo
			case 7:arrayDo.add(dto);break;
			// los horarios de atencion que no tiene dia asignado
			// se les asigna a cada una de la lista de dias. Esto para realizar 
			// la generacion de la genda en un solo ciclo -que cada lista de dias contenga los horarios que se puedan asignar para ese dia-
			case ConstantesBD.codigoNuncaValido:
				arrayLu.add(dto);
				arrayMa.add(dto);
				arrayMi.add(dto);
				arrayJu.add(dto);
				arrayVi.add(dto);
				arraySa.add(dto);
				arrayDo.add(dto);
				break;
			}
		}
		arrayDtoXDias.add(arrayLu);
		arrayDtoXDias.add(arrayMa);
		arrayDtoXDias.add(arrayMi);
		arrayDtoXDias.add(arrayJu);
		arrayDtoXDias.add(arrayVi);
		arrayDtoXDias.add(arraySa);
		arrayDtoXDias.add(arrayDo);
		//arrayDtoXDias.add(arrayNoAsig);
		return arrayDtoXDias;
	}
	
	/**
	 * verifica las excepciones del centro de atencion para la generacion de la agenda
	 * @param con
	 * @param fecha
	 * @param centroAtencion
	 * @return
	 */
	public static String verificarExcepcionCentroAtencion(Connection con, String fecha, int centroAtencion)
	{
		return getGenerarAgendaOdontologicaDao().verificarExcepcionCentroAtencion(con, fecha, centroAtencion);
	}

	/**
	 * se verifica si la agenda odontologica ya fue generada 
	 * @param con
	 * @param centroAtencion
	 * @param unidadAgenda
	 * @param consultorio
	 * @param diaSemana
	 * @param fecha
	 * @param horaIni
	 * @param horaFin
	 * @return ArrayList<DtoExistenciaAgenOdon>
	 */
	public static ArrayList<DtoExistenciaAgenOdon> verificarExistenciaAgendaOdontologica(Connection con, 
			int centroAtencion,
			int unidadAgenda,
			int consultorio,
			int diaSemana,
			String fecha)
	{
		return getGenerarAgendaOdontologicaDao().verificarExistenciaAgendaOdontologica(con, centroAtencion, unidadAgenda, consultorio, diaSemana, fecha);
	}
	
	/**
	 * insertar agenda odontologica
	 * @param con
	 * @param dto
	 * @return
	 */
	public static int insertarAgendaOdontologica(Connection con, DtoAgendaOdontologica dto)
	{
		return getGenerarAgendaOdontologicaDao().insertarAgendaOdontologica(con, dto);
	}

	/**
	 * actualizar los tiempos de la agenda odontologica
	 * @param con
	 * @param dto
	 * @param horaIni
	 * @param horaFin
	 * @return
	 */
	public static boolean actualizarAgendaOdontologica(Connection con, DtoAgendaOdontologica dto, boolean horaIni, boolean horaFin)
	{
		return getGenerarAgendaOdontologicaDao().actualizarAgendaOdontologica(con, dto, horaIni, horaFin);
	}
	
	/**
	 * metodo que consulta los datos descriptivos de una agenda odontologica
	 * @param con
	 * @param dto
	 * @return
	 */
	public static DtoAgendaOdontologica consultarAgendaOdontologica(Connection con, DtoAgendaOdontologica dto)
	{
		return getGenerarAgendaOdontologicaDao().consultarAgendaOdontologica(con, dto);
	}

	/**
	 * metodo que consulta los datos descriptivos de una agenda odontologica pero a partir del horario
	 * @param con
	 * @param dto
	 * @return
	 */
	public static DtoAgendaOdontologica consultarHorarioAtencion(Connection con, DtoAgendaOdontologica dto)
	{
		return getGenerarAgendaOdontologicaDao().consultarHorarioAtencion(con, dto);
	}
	
	/**
	 * se obtiene un listado de los horarios de atencion para la regeneracion de agenda odontologica
	 * @param con
	 * @param codigoHorarioAtencion
	 * @return
	 */
	public  ArrayList<DtoHorarioAtencion> obtenerHorariosAtencion(Connection con, 
			String codigoHorarioAtencion,
			ArrayList<DtoAgendaOdontologica> dto)
	{
		ArrayList<DtoHorarioAtencion> dtoAux = new ArrayList<DtoHorarioAtencion>();
		dtoAux = getGenerarAgendaOdontologicaDao().obtenerHorariosAtencion(con, codigoHorarioAtencion);
		int consultorio = 0, dia=0, codigoCentroAtencion=0;
		boolean consultorioOcupado = false;
		String fecha="", horaInicio="", horaFin="";
		Log4JManager.info("tamano dtoAux: "+dtoAux.size());
		Log4JManager.info("tamano ArrayList: "+dto.size());
		for(int i=0;i<dtoAux.size();i++)
		{
			for(int j=0;j<dto.size();j++)
			{
				Log4JManager.info("Codigo HA: "+dtoAux.get(i).getCodigo());
				Log4JManager.info("Codigo lista: "+dto.get(j).getCodigoHorarioAtencion());
				
				if(dtoAux.get(i).getCodigo()==dto.get(j).getCodigoHorarioAtencion()){
					Log4JManager.info("Consultorio asignar: "+dto.get(j).getConsultorio());
					dtoAux.get(i).setConsultorio(dto.get(j).getConsultorio());
					
					
					consultorio = dto.get(j).getConsultorio();
					dia=dto.get(j).getDia();
					fecha=UtilidadFecha.conversionFormatoFechaABD(dto.get(j).getFecha());
					horaInicio=dto.get(j).getHoraInicio();
					horaFin=dto.get(j).getHoraFin();
					codigoCentroAtencion=dto.get(j).getCentroAtencion();
					Log4JManager.info(">>> Fecha ="+fecha+
							"\n>>> dia = "+dia+
							"\n>>> horaInicio = "+horaInicio+
							"\n>>> horaFin = " +horaFin+
							"\n>>> codigoCentroAtención = "+codigoCentroAtencion );
				}
			}
		}
		/*
		 * Caso 1: Sin seleccionar consultorio
		 */
		if(consultorio != 0)
		{
			Log4JManager.info(">>> Encontré Consultorio, Entré a validar consultorios");
			consultorioOcupado = getGenerarAgendaOdontologicaDao().existeConsultoriosAgendados(con, 
					consultorio, dia, fecha, horaInicio, horaFin, codigoCentroAtencion);
			Log4JManager.info(">>> Consultorio ocupado = "+consultorioOcupado);
		}
		
		Log4JManager.info("salir de la asignacion de consultorios");
		if(consultorioOcupado)
		{
			DtoAgendaOdontologica dtoAgenOdon = new DtoAgendaOdontologica();
			ArrayList<DtoHorarioAtencion> dtoAuxVacio = new ArrayList<DtoHorarioAtencion>();
			this.setEsConsultorioOcupado(Boolean.TRUE);
			return dtoAuxVacio;
		}
		else
		{
			return dtoAux;
		}
		

	}
	
	/**
	 * se verifica si la agenda odontologica ya fue generada 
	 * @param con
	 * @param centroAtencion
	 * @param unidadAgenda
	 * @param consultorio
	 * @param diaSemana
	 * @param fecha
	 * @param horaIni
	 * @param horaFin
	 * @return ArrayList<DtoExistenciaAgenOdon>
	 */
	public static String isInsertAgendaOdontologica(Connection con, DtoAgendaOdontologica dto)
	{
		return getGenerarAgendaOdontologicaDao().isInsertAgendaOdontologica(con, dto);
	}
	
	/**
	 * se obtiene un listado de los horarios de atencion que cumple con los parametros 
	 * selecionados para la generacion de la agenda odontologica
	 * @param con
	 * @param centroAtencion
	 * @param unidadConsulta
	 * @param consultorio
	 * @param diaSemana
	 * @param codigoMedico
	 * @return
	 */
	public ArrayList<DtoAgendaOdontologica> cosultaAgendaOdontologica(Connection con, 
			String centroAtencion, 
			String unidadConsulta, 
			String consultorio,
			String diaSemana,
			String codigoMedico,
			String fechaIni,
			String fechaFin)
	{
		return getGenerarAgendaOdontologicaDao().cosultaAgendaOdontologica(con, centroAtencion, unidadConsulta, consultorio, diaSemana, codigoMedico, fechaIni, fechaFin);
	}
	
	/**
	 * @return the generarAgenda
	 */
	public DtoGenerarAgenda getGenerarAgenda() {
		return generarAgenda;
	}

	/**
	 * @param generarAgenda the generarAgenda to set
	 */
	public void setGenerarAgenda(DtoGenerarAgenda generarAgenda) {
		this.generarAgenda = generarAgenda;
	}

	public ArrayList<DtoExcepcionesHorarioAtencion> cosultaExAgendaOdontologica(Connection con, String centroAtencion, String unidadAgenda,String consultorio, String diaSemana, String profesionalSalud,String fechaInicial, String fechaFinal, Boolean esGenerar) 
	{
		return getGenerarAgendaOdontologicaDao().cosultaExAgendaOdontologica(con, centroAtencion, unidadAgenda, consultorio, diaSemana, profesionalSalud, fechaInicial, fechaFinal, esGenerar);
	}

	public void setEsConsultorioOcupado(boolean esConsultorioOcupado) {
		this.esConsultorioOcupado = esConsultorioOcupado;
	}

	public boolean isEsConsultorioOcupado() {
		return esConsultorioOcupado;
	}

	public String getNumAgendasGenAntes() {
		return numAgendasGenAntes;
	}

	public void setNumAgendasGenAntes(String numAgendasGenAntes) {
		this.numAgendasGenAntes = numAgendasGenAntes;
	}

	public boolean isHorarioAtencion() {
		return horarioAtencion;
	}

	public void setHorarioAtencion(boolean horarioAtencion) {
		this.horarioAtencion = horarioAtencion;
	}

	/**
	 * Lista los consultorios asociados a los horarios de atención que
	 * concuerden con los parámetros de búsqueda enviados
	 * @param dtoAgenda DTO con los datos de la agenda
	 * @return Lista de consultorios que cumplan las condiciones
	 */
	public static ArrayList<DtoConsultorios> listarConsultoriosParaGenerarAgenda(DtoAgendaOdontologica dtoAgenda)
	{
		IConsultoriosMundo consultoriosMundo=ConsultaExternaFabricaMundo.crearConsultorioMundo();
		return consultoriosMundo.listarConsultoriosParaGenerarAgenda(dtoAgenda);
		
	}
	
	/**
	 * Lista los consultorios que no se encuentren asociados a los horarios de atención
	 * que concuerden con los parámetros de búsqueda enviados
	 * Los consultorios se asignan en el atributo listConsultoriosXGenerar
	 * @param dtoAgenda DTO con los datos de la agenda
	 */
	private static void listarConsultoriosParaGenerarAgendaSinConsultorio(DtoAgendaOdontologica dtoAgenda)
	{
		IConsultoriosMundo consultoriosMundo=ConsultaExternaFabricaMundo.crearConsultorioMundo();
		consultoriosMundo.listarConsultoriosParaGenerarAgendaSinConsultorio(dtoAgenda);
	}
	
	public boolean existeConsultoriosOcupados(Connection con, int consultorio, int dia, String fecha, String horaInicio,
			String horaFin, int codigoCentroAtencion){
		
		boolean consultorioOcupado = getGenerarAgendaOdontologicaDao().existeConsultoriosAgendados(con, 
				consultorio, dia, fecha, horaInicio, horaFin, codigoCentroAtencion);
		Log4JManager.info(">>> Consultorio ocupado = "+consultorioOcupado);
		
		return consultorioOcupado;
	}

}
