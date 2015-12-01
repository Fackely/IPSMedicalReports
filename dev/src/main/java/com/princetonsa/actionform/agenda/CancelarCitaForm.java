/*
* @(#)CancelarCitaForm.java
*
* Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
*
* Lenguaje		: Java
* Compilador	: J2SDK 1.4.2_01
*/
package com.princetonsa.actionform.agenda;

import java.io.Serializable;

import java.lang.String;

import java.sql.Connection;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Iterator;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import java.util.HashMap;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.agenda.Cita;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadFecha;
import util.Utilidades;

/**
* ActionForm, tiene la función de bean dentro de la forma, que contiene todos los datos necesarios
* para la cancelación de una cita de consulta. Adicionalmente hace el manejo de limpieza de la forma
* y de validación de datos de entrada.
*
* @version 1.0, Abr 07, 2004
* @author <a href="mailto:edgar@PrincetonSA.com">Edgar Prieto</a>
*
* @see org.apache.struts.action.ActionForm#validate(ActionMapping, HttpServletRequest)
*/
public class CancelarCitaForm extends ValidatorForm implements Serializable
{
	/**
	* Manejador de logs
	*/
	private static Logger logger = Logger.getLogger(CancelarCitaForm.class);
	
	/** Código del ítem de agenda de consultas */
	private int ii_agenda;

	/** Código de la cita */
	private int ii_codigo;

	/** Código del consultorio sobre el cual realizar la búsqueda de las citas */
	private int ii_consultorio;

	/** Número de la cuenta del paciente */
	private int ii_cuenta;

	/** Código del estado de la cita */
	private int ii_estadoCita;

	/** Código del médico sobre el cual realizar la búsqueda de las citas */
	private int ii_medico;

	/** Código del paciente de la cita */
	private int ii_paciente;

	/** Indice del ítem seleccionado */
	private int ii_seleccionado;

	/** Número de ítems en la lista de citas a reprogramar */
	private int ii_seleccionados;

	/** Código de la unidad de consulta sobre la cual realizar la búsqueda de las citas */
	private int ii_unidadConsulta;

	/** Datos de los citas seleccionados para cancelación */
	private transient Map im_citas;

	/** Estado actual del flujo */
	private String is_estado;

	/** Estado de liquidación sobre el cual realizar la búsqueda de las citas */
	private String is_estadoLiquidacionCita;

	/** Fecha de finalización sobre la cual realizar la búsqueda de las citas */
	private String is_fechaFin;

	/** Fecha de inicio sobre la cual realizar la búsqueda de las citas */
	private String is_fechaInicio;

	/** Hora de finalización sobre la cual realizar la búsqueda de las citas */
	private String is_horaFin;

	/** Hora de inicio sobre la cual realizar la búsqueda de las cita */
	private String is_horaInicio;

	/**
	 * 
	 */
	private String centroAtencion;
	/**
	 * Resultado de citas canceladas
	 */
	private int citasCanceladas;

	/** Obtener el código del ítem de agenda de consulta */
	public int getCodigoAgenda()
	{
		return ii_agenda;
	}

	/** Obtener el código de la cita */
	public int getCodigoCita()
	{
		return ii_codigo;
	}

	/** Obtener el código del consultorio */
	public int getCodigoConsultorio()
	{
		return ii_consultorio;
	}

	/** Obtener código del estado de la cita */
	public int getCodigoEstadoCita()
	{
		return ii_estadoCita;
	}

	/** Obtener el código del médico */
	public int getCodigoMedico()
	{
		return ii_medico;
	}

	/** Obtener el código del paciente */
	public int getCodigoPaciente()
	{
		return ii_paciente;
	}

	/** Obtener la unidad de consulta */
	public int getCodigoUnidadConsulta()
	{
		return ii_unidadConsulta;
	}

	/** Obtener la cuenta del paciente */
	public int getCuentaPaciente()
	{
		return ii_cuenta;
	}

	/** Obtener el estado actual del flujo */
	public String getEstado()
	{
		return is_estado;
	}

	/** Obtener el estado de liquidación de la cita */
	public String getEstadoLiquidacionCita()
	{
		return is_estadoLiquidacionCita;
	}

	/** Obtener la fecha de finalización */
	public String getFechaFin()
	{
		return is_fechaFin;
	}

	/** Obtener la fecha de inicio */
	public String getFechaInicio()
	{
		return is_fechaInicio;
	}

	/** Obtener la hora de finalización */
	public String getHoraFin()
	{
		return is_horaFin;
	}

	/** Obtener la hora de inicio */
	public String getHoraInicio()
	{
		return is_horaInicio;
	}

	/** Obtener el índice del ítem seleccionado */
	public int getIndiceItemSeleccionado()
	{
		return ii_seleccionado;
	}

	/** Obtener un dato de un ítem de citas a cancelar */
	public Object getItemSeleccionado(String as_dato)
	{
		return im_citas.get(as_dato);
	}

	/** Obtener los datos de varios ítems de citas a cancelar */
	public Map getItemsSeleccionados()
	{
		return im_citas;
	}

	/** Obtener el número de citas seleccionadas para cancelación */
	public int getNumeroItemsSeleccionados()
	{
		return ii_seleccionados;
	}
	
	/**
	 * Listado de centros de atencion y unidades de agenda autorizados para el usuario
	 */
	private HashMap unidadAgendaMap;
	
	/**
	 * Centros de atencion validos para el usuario
	 */
	private HashMap centrosAtencionAutorizados;
	
	/**
	 * Unidades de agenda validas para el usuario
	 */
	private HashMap unidadesAgendaAutorizadas;

	/** Limpia la forma */
	public void reset()
	{
		ii_agenda			= -1;
		ii_codigo			= -1;
		ii_consultorio		= -1;
		ii_cuenta			= -1;
		ii_estadoCita		= -1;
		ii_medico			= -1;
		ii_paciente			= -1;
		ii_seleccionado		= -1;
		ii_seleccionados	= 0;
		ii_unidadConsulta	= -1;

		is_estado						= "";
		is_estadoLiquidacionCita		= "";
		is_fechaFin						= "";
		is_fechaInicio					= "";
		is_horaFin						= "";
		is_horaInicio					= "";

		im_citas = new HashMap();
		citasCanceladas=0;
		this.centroAtencion=ConstantesBD.codigoNuncaValido+"";
		
		this.unidadAgendaMap = new HashMap();
		this.unidadAgendaMap.put("numRegistros", "0");
		
		this.centrosAtencionAutorizados=new HashMap();
		this.centrosAtencionAutorizados.put("numRegistros", "0");
		this.unidadesAgendaAutorizadas=new HashMap();
		this.unidadesAgendaAutorizadas.put("numRegistros", "0");
	}

	/** Establecer el código del ítem de agenda */
	public void setCodigoAgenda(int ai_agenda)
	{
		ii_agenda = ai_agenda;
	}

	/** Establecer el código de la cita */
	public void setCodigoCita(int ai_codigo)
	{
		ii_codigo = ai_codigo;
	}

	/** Establecer el código del consultorio */
	public void setCodigoConsultorio(int ai_consultorio)
	{
		ii_consultorio = ai_consultorio;
	}

	/** Establecer el código del estado de la cita */
	public void setCodigoEstadoCita(int ai_estadoCita)
	{
		ii_estadoCita = ai_estadoCita;
	}

	/** Establecer el código del médico */
	public void setCodigoMedico(int ai_medico)
	{
		ii_medico = ai_medico;
	}

	/** Establecer el código del paciente */
	public void setCodigoPaciente(int ai_paciente)
	{
		ii_paciente = ai_paciente;
	}

	/** Establecer la unidad de consulta */
	public void setCodigoUnidadConsulta(int ai_unidadConsulta)
	{
		ii_unidadConsulta = ai_unidadConsulta;
	}

	/** Establecer la la cuenta del paciente */
	public void setCuentaPaciente(int ai_cuenta)
	{
		ii_cuenta = ai_cuenta;
	}

	/** Establecer el estado actual del flujo */
	public void setEstado(String as_estado)
	{
		if(as_estado != null)
			is_estado = as_estado.trim();
	}

	/** Establecer el estado de liquidación de la cita */
	public void setEstadoLiquidacionCita(String as_estadoLiquidacionCita)
	{
		if(as_estadoLiquidacionCita != null)
			is_estadoLiquidacionCita = as_estadoLiquidacionCita.trim();
	}

	/** Establecer la fecha de finalización */
	public void setFechaFin(String as_fechaFin)
	{
		if(as_fechaFin != null)
			is_fechaFin = as_fechaFin.trim();
	}

	/** Establecer la fecha de inicio */
	public void setFechaInicio(String as_fechaInicio)
	{
		if(as_fechaInicio != null)
			is_fechaInicio = as_fechaInicio.trim();
	}

	/** Establecer la hora de finalización */
	public void setHoraFin(String as_horaFin)
	{
		if(as_horaFin != null)
			is_horaFin = as_horaFin.trim();
	}

	/** Establecer la hora de inicio */
	public void setHoraInicio(String as_horaInicio)
	{
		if(as_horaInicio != null)
			is_horaInicio = as_horaInicio.trim();
	}

	/** Establecer el índice del ítem seleccionado */
	public void setIndiceItemSeleccionado(int ai_seleccionado)
	{
		ii_seleccionado = ai_seleccionado;
	}

	/** Adicionar un dato de un ítem de cita a la lista de citas a cancelar */
	public void setItemSeleccionado(String as_dato, Object ao_valor)
	{
		if(as_dato != null && as_dato.length() > 0 && ao_valor != null)
			im_citas.put(as_dato, ao_valor);
	}

	/** Adicionar los datos de varios ítems de cita a la lista citas a cancelar 
	 * @param con 
	 * @param usuarioActual */
	public void setItemsSeleccionados(Connection con, Collection ac_reservados, UsuarioBasico usuarioActual)
	{
		Iterator li_iterator;

		/* Eliminar los ítems seleccionados para reprogración existentes */
		getItemsSeleccionados().clear();
		setNumeroItemsSeleccionados(0);

		if(ac_reservados != null)
		{
			HashMap	ldb_reservado;
			Integer		li_estadoCita;
			String		ls_aux;
			String		ls_apellidosMedico;
			String		ls_estadoLiquidacion;
			String		ls_nombresMedico;

			li_iterator = ac_reservados.iterator();

			for(int li_i = 0; li_iterator.hasNext(); li_i++)
			{
				ldb_reservado			= (HashMap)li_iterator.next();
				ls_estadoLiquidacion	= (String)ldb_reservado.get("codigoestadoliquidacion");

				/* Obtener el nombre del médico de la cita */
				ls_apellidosMedico =
					(ls_aux = (String)ldb_reservado.get("primerapellidomedico") ) != null ?
						ls_aux.trim() : "";

				if( (ls_aux = (String)ldb_reservado.get("segundoapellidomedico") ) != null)
					ls_apellidosMedico = (ls_apellidosMedico + " " + ls_aux).trim();

				ls_nombresMedico =
					(ls_aux = (String)ldb_reservado.get("primernombremedico") ) != null ?
					ls_aux.trim() : "";

				if( (ls_aux = (String)ldb_reservado.get("segundonombremedico") ) != null)
					ls_nombresMedico = (ls_nombresMedico + " " + ls_aux).trim();

				if(!ls_apellidosMedico.equals("") && !ls_nombresMedico.equals("") )
					ls_apellidosMedico += ", ";

				/* Obtener el estado de la cita */
				li_estadoCita = Utilidades.convertirAEntero(ldb_reservado.get("codigoestadocita")+"");

				setItemSeleccionado(
						"nombreCentroAtencion_" + li_i, (String)ldb_reservado.get("nombrecentroatencion")
					);
					
				
				setItemSeleccionado(
					"codigoAgenda_" + li_i, Utilidades.convertirAEntero(ldb_reservado.get("codigoagenda")+"")
				);
				
				setItemSeleccionado(
					"codigoCita_" + li_i, Utilidades.convertirAEntero(ldb_reservado.get("codigo")+"")
				);
				setItemSeleccionado("codigoEstadoCita_" + li_i, li_estadoCita);
				setItemSeleccionado("nombreEstadoCita_" + li_i, Utilidades.getNombreEstadoCita(con, li_estadoCita));
				setItemSeleccionado(
					"codigoEstadoLiquidacion_" + li_i,
					ls_estadoLiquidacion
				);
				setItemSeleccionado(
						"nombreEstadoLiquidacion_" + li_i,
						Utilidades.getNombreEstadoLiquidacion(con, ls_estadoLiquidacion)
					);
				setItemSeleccionado(
					"codigoUnidadConsulta_" + li_i,
					Utilidades.convertirAEntero(ldb_reservado.get("codigounidadconsulta")+"")
				);
				setItemSeleccionado(
					"fecha_" + li_i,
					UtilidadFecha.conversionFormatoFechaAAp(
						(String)ldb_reservado.get("fecha")
					)
				);
				setItemSeleccionado("horaFin_" + li_i, (String)ldb_reservado.get("horafin") );
				setItemSeleccionado(
					"horaInicio_" + li_i, (String)ldb_reservado.get("horainicio")
				);
				setItemSeleccionado(
					"nombreConsultorio_" + li_i, (String)ldb_reservado.get("nombreconsultorio")
				);
				setItemSeleccionado(
					"nombreMedico_" + li_i, ls_apellidosMedico + ls_nombresMedico
				);
				setItemSeleccionado(
					"nombreUnidadConsulta_" + li_i,
					(String)ldb_reservado.get("nombreunidadconsulta")
				);
				
				setItemSeleccionado("cupoLibre_"+li_i, ConstantesBD.acronimoNo);

				/*if(ls_estadoLiquidacion.equals(ConstantesBD.codigoEstadoLiquidacionSinLiquidar) )
					setItemSeleccionado("numeroSolicitud_" + li_i, "");
				else
					setItemSeleccionado(
						"numeroSolicitud_" + li_i, (Integer)ldb_reservado.get("numerosolicitud")
					);
				setItemSeleccionado("consecutivoOrdenMedica_" + li_i,(Integer)ldb_reservado.get("consecutivoordenmedica"));
				setItemSeleccionado("observaciones_" + li_i,(String)ldb_reservado.get("observaciones"));*/
				
				//*************SE CARGAN LOS SERVICIOS DE LA CITA************************************
				
				HashMap serviciosCita = Cita.consultarServiciosCita(con, getItemSeleccionado("codigoCita_"+li_i).toString(), usuarioActual.getCodigoInstitucionInt());
				
				logger.info("mapa servicios cita=> "+serviciosCita);
				
				int contServicios = 0;
				for(int j=0;j<Integer.parseInt(serviciosCita.get("numRegistros").toString());j++)
					if(serviciosCita.get("estadoServicio_"+j).toString().equals(ConstantesIntegridadDominio.acronimoEstadoActivo))
					{
						setItemSeleccionado("numeroSolicitud_"+li_i+"_"+contServicios,serviciosCita.get("numeroSolicitud_"+j));
						setItemSeleccionado("nombreServicio_"+li_i+"_"+contServicios,serviciosCita.get("nombreServicio_"+j));
						setItemSeleccionado("observaciones_"+li_i+"_"+contServicios,serviciosCita.get("observaciones_"+j));
						setItemSeleccionado("consecutivoOrden_"+li_i+"_"+contServicios,serviciosCita.get("consecutivoOrden_"+j));
						contServicios++;
					}
				setItemSeleccionado("numServicios_"+li_i, contServicios+"");
				//***********************************************************************************
			}

			/* Establecer el número de items seleccionados para cancelación */
			setNumeroItemsSeleccionados(ac_reservados.size() );
		}
	}

	/** Establecer el número de citas seleccionadas para cancelación */
	private void setNumeroItemsSeleccionados(int ai_seleccionados)
	{
		ii_seleccionados = ai_seleccionados < 0 ? 0 : ai_seleccionados;
	}

	/**
	* Valida las propiedades que han sido establecidas para este request HTTP, y retorna un objeto
	* <code>ActionErrors</code> que encapsula los errores de validación encontrados. Si no se
	* encontraron errores de validación, retorna <code>null</code>.
	* @param mapping Mapa usado para elegir esta instancia
	* @param request <i>Servlet Request</i> que está siendo procesado en este momento
	* @return <code>ActionErrors</code> con los (posibles) errores encontrados al validar este
	* formulario, o <code>null</code> si no se encontraron errores.
	*/
	public ActionErrors validate(ActionMapping aam_m, HttpServletRequest hsr_r)
	{
		ActionErrors lae_errors;

		lae_errors = new ActionErrors();;

		lae_errors.add(super.validate(aam_m, hsr_r) );
		

		if(lae_errors.isEmpty() )
		{
			if(is_estado.equals("listar") )
			{
				/* Validar esto sí y solo si no se han encontrado errores */
				Date				ld_fechaInicio;
				Date				ld_fechaFin;
				SimpleDateFormat	lsdf_sdfFecha;

				/* Iniciar variables. El formato de fecha que se espera es dd/MM/yyyy */
				lsdf_sdfFecha	= new SimpleDateFormat("dd/MM/yyyy");
				ld_fechaInicio	= null;
				ld_fechaFin		= null;;

				/* Exije una interpretación estricta del formato de fecha esperado */
				lsdf_sdfFecha.setLenient(false);

				if(!is_fechaInicio.equals("") )
				{
					try
					{
						Calendar lc_cal;

						/* Obtener la fecha de inicio */
						ld_fechaInicio = lsdf_sdfFecha.parse(is_fechaInicio);

						lc_cal = new GregorianCalendar();
						lc_cal.set(Calendar.HOUR_OF_DAY, 0);
						lc_cal.set(Calendar.MILLISECOND, 0);
						lc_cal.set(Calendar.MINUTE, 0);
						lc_cal.set(Calendar.SECOND, 0);

						/*
							Verificar que el listado de citas solo traiga citas cuya fecha sea mayor
							o igual a la fecha actual
						*/
						if(lc_cal.getTime().after(ld_fechaInicio) )
							lae_errors.add(
								"fechaInicio",
								new ActionMessage(
									"errors.fechaAnteriorAOtraDeReferencia", "Inicial", "actual"
								)
							);
					}
					catch(ParseException lpe_fechaInicio)
					{
						lae_errors.add(
							"fechaInicio", new ActionMessage("errors.formatoFechaInvalido", "Inicial")
						);
						ld_fechaInicio = null;
					}
				}
				else
				{
					lae_errors.add("fechaInicio", new ActionMessage("errors.required", "Fecha Inicial"));
				}

				if(!is_fechaFin.equals("") )
				{
					try
					{
						/* Obtener la fecha de finalización */
						ld_fechaFin = lsdf_sdfFecha.parse(is_fechaFin);
					}
					catch(ParseException lpe_fechaFin)
					{
						lae_errors.add(
							"fechaFin", new ActionMessage("errors.formatoFechaInvalido", "Final")
						);
						ld_fechaFin = null;
					}
				}
				else
				{
					lae_errors.add("fechaFin", new ActionMessage("errors.required", "Fecha Final"));
				}

				if(
					ld_fechaFin != null		&&
					ld_fechaInicio != null	&&
					ld_fechaFin.before(ld_fechaInicio)
				)
				{
					/* La fecha de inicio debe ser anterior a la fecha de finalización */
					lae_errors.add(
						"fechaFin",
						new ActionMessage(
							"errors.fechaAnteriorAOtraDeReferencia", "Fecha Final", "Fecha Inicial"
						)
					);
				}

				/* Validar esto sí y solo si no se han encontrado errores */
				if(!is_horaInicio.equals("") || !is_horaFin.equals("") )
				{
					Date				ld_horaInicio;
					Date				ld_horaFin;
					SimpleDateFormat	lsdf_sdfHora;

					/* Iniciar variables. El formato de hora que se espera es HH:mm en 24 horas */
					lsdf_sdfHora	= new SimpleDateFormat("H:mm");
					ld_horaInicio	= null;
					ld_horaFin		= null;

					/* Exije una interpretación estricta del formato de hora esperado */
					lsdf_sdfHora.setLenient(false);

					if(!is_horaInicio.equals("") )
					{
						try
						{
							/* Obtener la hora de inicio */
							ld_horaInicio = lsdf_sdfHora.parse(is_horaInicio);
						}
						catch(ParseException lpe_horaInicio)
						{
							lae_errors.add(
								"horaInicio",
								new ActionMessage("errors.formatoHoraInvalido", "de inicio")
							);
							ld_horaInicio = null;
						}
					}

					if(!is_horaFin.equals("") )
					{
						try
						{
							/* Obtener la hora de finalización */
							ld_horaFin = lsdf_sdfHora.parse(is_horaFin);
						}
						catch(ParseException lpe_horaFin)
						{
							lae_errors.add(
								"horaFin",
								new ActionMessage("errors.formatoHoraInvalido", "de finalización")
							);
							ld_horaFin = null;
						}
					}

					/* La hora de inicio debe ser anterior a la hora de finalización */
					if(
						ld_horaFin != null &&
						ld_horaInicio != null &&
						ld_horaFin.before(ld_horaInicio)
					)
						lae_errors.add(
							"horaFin",
							new ActionMessage(
								"errors.fechaAnteriorAOtraDeReferencia", "final", "inicial"
							)
						);
				}
			}
			
			else if(is_estado.equals("cancelar") )
			{
				
				/*
					Validar que la fecha y hora de inicio de la agenda a reservar / asignar sea
					mayor que la fecha actual
				*/
				Date				ld_cita;
				SimpleDateFormat	lsdf_sdf;
				String				ls_fecha;
				String				ls_hora;
				Object				lo_o;

				/*
					Iniciar variables. El formato de tiempo que se espera esdd/MM/yyyy HH:mm en 24
					horas
				*/
				lsdf_sdf = new SimpleDateFormat("dd/MM/yyyy H:mm");

				/* Exije una interpretación estricta del formato de hora esperado */
				lsdf_sdf.setLenient(false);

				for(int li_i = 0, li_tam =getNumeroItemsSeleccionados(); li_i < li_tam; li_i++)
				{
					lo_o = getItemSeleccionado("codigoEstadoCita_" + li_i);
					
					try
					{
						ls_fecha	= (String)getItemSeleccionado("fecha_" + li_i);
						ls_hora		= (String)getItemSeleccionado("horaInicio_" + li_i);

						/* Obtener la fecha y hora de inicio */
						ld_cita = lsdf_sdf.parse(ls_fecha + " " + ls_hora);

						if(ld_cita.before(new Date() ) )
						{
							lae_errors.add(
								"",
								new ActionMessage(
									"error.cita.agendaVencida",
									(String)getItemSeleccionado("nombreUnidadConsulta_" + li_i),
									ls_fecha,
									ls_hora,
									"cancelar"
								)
							);

							setItemSeleccionado("codigoEstadoCita_" + li_i, new Integer(-1) );
							setItemSeleccionado("motivoCancelacion_" + li_i, "");
						}
					}
					catch(ParseException lpe_horaInicio)
					{
					}

					/* Convertir el estado de la cita en un objeto entero */
					if(lo_o instanceof String)
						setItemSeleccionado(
							"codigoEstadoCita_" + li_i, Integer.valueOf( (String)lo_o)
						);
					if(!(this.getItemSeleccionado("codigoEstadoCita_"+li_i)+"").equals(ConstantesBD.codigoNuncaValido+"")&&this.getItemSeleccionado("codigoMotivoCancelacion_"+li_i).equals(""))
					{
						lae_errors.add("fechaFin", new ActionMessage("errors.required", "El motivo de cancelacion "));
					}
				}
			}
		}

		/* Si no hay errores limpiar el contenedor */
		if(lae_errors.isEmpty() )
			lae_errors = null;

		return lae_errors;
	}
	
	/**
	 * @return Retorna el número de citas canceladas
	 */
	public int getCitasCanceladas()
	{
		return citasCanceladas;
	}

	/**
	 * @param Asigna el número de citas canceladas
	 */
	public void setCitasCanceladas(int i)
	{
		citasCanceladas = i;
	}

	public String getCentroAtencion() {
		return centroAtencion;
	}

	public void setCentroAtencion(String centroAtencion) {
		this.centroAtencion = centroAtencion;
	}

	/**
	 * @return the unidadAgendaMap
	 */
	public HashMap getUnidadAgendaMap() {
		return unidadAgendaMap;
	}

	/**
	 * @param unidadAgendaMap the unidadAgendaMap to set
	 */
	public void setUnidadAgendaMap(HashMap unidadAgendaMap) {
		this.unidadAgendaMap = unidadAgendaMap;
	}

	/**
	 * @return the unidadAgendaMap
	 */
	public Object getUnidadAgendaMap(String llave) {
		return unidadAgendaMap.get(llave);
	}

	/**
	 * @param unidadAgendaMap the unidadAgendaMap to set
	 */
	public void setUnidadAgendaMap(String llave, Object obj) {
		this.unidadAgendaMap.put(llave, obj);
	}
	
	/**
	 * @return the centrosAtencionAutorizados
	 */
	public HashMap getCentrosAtencionAutorizados() {
		return centrosAtencionAutorizados;
	}

	/**
	 * @param centrosAtencionAutorizados the centrosAtencionAutorizados to set
	 */
	public void setCentrosAtencionAutorizados(HashMap centrosAtencionAutorizados) {
		this.centrosAtencionAutorizados = centrosAtencionAutorizados;
	}
	
	/**
	 * @return the centrosAtencionAutorizados
	 */
	public Object getCentrosAtencionAutorizados(String llave) {
		return centrosAtencionAutorizados.get(llave);
	}

	/**
	 * @param centrosAtencionAutorizados the centrosAtencionAutorizados to set
	 */
	public void setCentrosAtencionAutorizados(String llave, Object obj) {
		this.centrosAtencionAutorizados.put(llave, obj);
	}

	/**
	 * @return the unidadesAgendaAutorizadas
	 */
	public HashMap getUnidadesAgendaAutorizadas() {
		return unidadesAgendaAutorizadas;
	}

	/**
	 * @param unidadesAgendaAutorizadas the unidadesAgendaAutorizadas to set
	 */
	public void setUnidadesAgendaAutorizadas(HashMap unidadesAgendaAutorizadas) {
		this.unidadesAgendaAutorizadas = unidadesAgendaAutorizadas;
	}

	/**
	 * @return the unidadesAgendaAutorizadas
	 */
	public Object getUnidadesAgendaAutorizadas(String llave) {
		return unidadesAgendaAutorizadas.get(llave);
	}

	/**
	 * @param unidadesAgendaAutorizadas the unidadesAgendaAutorizadas to set
	 */
	public void setUnidadesAgendaAutorizadas(String llave, Object obj) {
		this.unidadesAgendaAutorizadas.put(llave, obj);
	}
	
}