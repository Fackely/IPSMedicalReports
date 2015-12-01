package com.princetonsa.mundo.manejoPaciente;
  
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import util.ConstantesBD;
import util.InfoDatosString;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.manejoPaciente.RegistroEnvioInfAtencionIniUrgDao;
import com.princetonsa.dao.sqlbase.manejoPaciente.SqlBaseRegistroEnvioInfAtencionIniUrgDao;
import com.princetonsa.dto.manejoPaciente.DtoInformeAtencionIniUrg;
import com.princetonsa.mundo.PersonaBasica;

public class RegistroEnvioInfAtencionIniUrg
{
	/**
	* Objeto para manejar los logs de esta clase
	*/
	private static Logger logger = Logger.getLogger(RegistroEnvioInfAtencionIniUrg.class);	
	
	/**
	* 
	* */
	public static RegistroEnvioInfAtencionIniUrgDao getRegistroEnvioInfAtencionIniUrgDao()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getRegistroEnvioInfAtencionIniUrgDao();
	}
	
	/**
	 * Listado de Informe Atencion Inicial de Urgencias
	 * @param Connection con
	 * @param String fechaInicialValoracion
	 * @param String fechaFinalValoracion
	 * @param String fechaInicialGeneracion
	 * @param String fechaFinalGeneracion
	 * @param String fechaInicialEnvio
	 * @param String fechaFinalEnvio
	 * @param String estadoEnvio
	 * @param String convenio
	 * @param String informesNoGen
	 * */
	public static ArrayList<DtoInformeAtencionIniUrg> getListadoInformeInicUrge(
			Connection con,
			String fechaInicialValoracion,
			String fechaFinalValoracion,
			String fechaInicialGeneracion,
			String fechaFinalGeneracion,
			String fechaInicialEnvio,
			String fechaFinalEnvio,			
			String estadoEnvio,
			String convenio,
			String informesNoGen,
			int institucion)
	{
		HashMap parametros = new HashMap();
		parametros.put("fechaInicialValoracion",UtilidadFecha.conversionFormatoFechaABD(fechaInicialValoracion));
		parametros.put("fechaFinalValoracion",UtilidadFecha.conversionFormatoFechaABD(fechaFinalValoracion));
		
		parametros.put("fechaInicialGeneracion",UtilidadFecha.conversionFormatoFechaABD(fechaInicialGeneracion));
		parametros.put("fechaFinalGeneracion",UtilidadFecha.conversionFormatoFechaABD(fechaFinalGeneracion));
		
		parametros.put("fechaInicialEnvio",UtilidadFecha.conversionFormatoFechaABD(fechaInicialEnvio));
		parametros.put("fechaFinalEnvio",UtilidadFecha.conversionFormatoFechaABD(fechaFinalEnvio));
		
		parametros.put("estadoEnvio",estadoEnvio);
		parametros.put("convenio",convenio);
		parametros.put("informesNoGen",informesNoGen);		
		parametros.put("institucion",institucion);
		
		return getRegistroEnvioInfAtencionIniUrgDao().getListadoInformeInicUrge(con, parametros);
	}
	
	/**
	 * Inicializa los parametros de busqueda por rango 
	 * */
	public static HashMap inicializarParametrosBusquedaRango()
	{
		HashMap parametros = new HashMap();
		parametros.put("informesNoGen",ConstantesBD.acronimoNo);
		parametros.put("fechaInicialValoracion",UtilidadFecha.getFechaActual());
		parametros.put("fechaFinalValoracion",UtilidadFecha.getFechaActual());
		parametros.put("fechaInicialGeneracion",UtilidadFecha.getFechaActual());
		parametros.put("fechaFinalGeneracion",UtilidadFecha.getFechaActual());
		parametros.put("fechaInicialEnvio",UtilidadFecha.getFechaActual());
		parametros.put("fechaFinalEnvio",UtilidadFecha.getFechaActual());
		parametros.put("estadoEnvio","");
		
		return parametros;
	}
	
	/**
	 * Validaciones de busqueda por rango
	 * @param HashMap parametros
	 * */
	public static ActionErrors validacionBusquedaXRango(HashMap parametros)
	{
		Utilidades.imprimirMapa(parametros);
		ActionErrors errores = new ActionErrors();
		boolean infFechaVal = false;
		boolean infFechaGen = false;
		boolean infFechaEnv = false;		
				
		//Fecha Valoración
		if((!parametros.get("fechaInicialValoracion").toString().equals("") 
				&& parametros.get("fechaFinalValoracion").toString().equals("")) || 
					(!parametros.get("fechaFinalValoracion").toString().equals("")
						&& parametros.get("fechaInicialValoracion").toString().equals("")))
		{
			if(parametros.get("fechaInicialValoracion").toString().equals(""))
				errores.add("descripcion",new ActionMessage("errors.required","La Fecha Inicial de Valoración "));
			
			if(parametros.get("fechaFinalValoracion").toString().equals(""))
				errores.add("descripcion",new ActionMessage("errors.required","La Fecha Final de Valoración "));
		}
		else if(!parametros.get("fechaInicialValoracion").toString().equals("") && 
					!parametros.get("fechaFinalValoracion").toString().equals(""))
		{
			if(!UtilidadFecha.validarFecha(parametros.get("fechaInicialValoracion").toString()))
				errores.add("descripcion",new ActionMessage("errors.formatoFechaInvalido"," Inicial de Valoración "));
			
			if(!UtilidadFecha.validarFecha(parametros.get("fechaFinalValoracion").toString()))
				errores.add("descripcion",new ActionMessage("errors.formatoFechaInvalido"," Final de Valoración "));
			
			if(UtilidadFecha.validarFecha(parametros.get("fechaInicialValoracion").toString()) && 
					UtilidadFecha.validarFecha(parametros.get("fechaFinalValoracion").toString()))			
			{
				if(UtilidadFecha.esFechaMenorQueOtraReferencia(parametros.get("fechaFinalValoracion").toString(), parametros.get("fechaInicialValoracion").toString()))
				{
					errores.add("descripcion",new ActionMessage("errors.fechaAnteriorIgualActual"," Final de Valoración "," Inicial de Valoración"));
				}
				else if(UtilidadFecha.numeroDiasEntreFechas(parametros.get("fechaInicialValoracion").toString(),parametros.get("fechaFinalValoracion").toString()) > 31)
					errores.add("descripcion",new ActionMessage("errors.fechaSuperaOtraPorDias"," Final de Valoración ","30"," Inicial de Valoración "));							
			}
			
			if(errores.isEmpty())
				infFechaVal = true;
		}
		
		if(parametros.get("informesNoGen").toString().equals(ConstantesBD.acronimoSi))
		{
			parametros.put("fechaInicialGeneracion","");
			parametros.put("fechaFinalGeneracion","");
			parametros.put("fechaInicialEnvio","");
			parametros.put("fechaFinalEnvio","");
			parametros.put("estadoEnvio","");			
		}
		else
		{
			//Fecha de Generacion
			if((!parametros.get("fechaInicialGeneracion").toString().equals("") 
					&& parametros.get("fechaFinalGeneracion").toString().equals("")) || 
						(parametros.get("fechaInicialGeneracion").toString().equals("")
							&& !parametros.get("fechaFinalGeneracion").toString().equals("")))
			{
				if(parametros.get("fechaInicialGeneracion").toString().equals(""))
					errores.add("descripcion",new ActionMessage("errors.required","La Fecha Inicial de Generación Informe"));
				
				if(parametros.get("fechaFinalGeneracion").toString().equals(""))
					errores.add("descripcion",new ActionMessage("errors.required","La Fecha Final de Generación Informe"));
			}
			else if(!parametros.get("fechaInicialGeneracion").toString().equals("") && 
						!parametros.get("fechaFinalGeneracion").toString().equals(""))
			{
				if(!UtilidadFecha.validarFecha(parametros.get("fechaInicialGeneracion").toString()))
					errores.add("descripcion",new ActionMessage("errors.formatoFechaInvalido"," Inicial de Generación Informe"));
				
				if(!UtilidadFecha.validarFecha(parametros.get("fechaFinalGeneracion").toString()))
					errores.add("descripcion",new ActionMessage("errors.formatoFechaInvalido"," Final de Generación Informe"));
				
				if(UtilidadFecha.validarFecha(parametros.get("fechaInicialGeneracion").toString()) && 
						UtilidadFecha.validarFecha(parametros.get("fechaFinalGeneracion").toString()))
				{
					if(UtilidadFecha.esFechaMenorQueOtraReferencia(parametros.get("fechaFinalGeneracion").toString(), parametros.get("fechaInicialGeneracion").toString()))
					{
						errores.add("descripcion",new ActionMessage("errors.fechaAnteriorIgualActual"," Final de Generación Informe "," Inicial de Generación Informe"));
					}
					else if(UtilidadFecha.numeroDiasEntreFechas(parametros.get("fechaInicialGeneracion").toString(),parametros.get("fechaFinalGeneracion").toString()) > 31)
						errores.add("descripcion",new ActionMessage("errors.fechaSuperaOtraPorDias"," Final de Generación Informe ","30"," Inicial de Generación Informe "));
					
					if(infFechaVal)
					{
						if(UtilidadFecha.esFechaMenorQueOtraReferencia(parametros.get("fechaInicialGeneracion").toString(), parametros.get("fechaInicialValoracion").toString()))					
							errores.add("descripcion",new ActionMessage("errors.fechaAnteriorIgualActual"," Final de Generación Informe "," Inicial de Valoración"));					
					}
				}			

				if(errores.isEmpty())
					infFechaGen = true;				
			}
			
			//Evalua la información del estado de envio
			if(parametros.get("estadoEnvio").equals(""))
			{
				//Fecha de Envio
				if((!parametros.get("fechaInicialEnvio").toString().equals("") 
						&& parametros.get("fechaFinalEnvio").toString().equals("")) || 
							(parametros.get("fechaInicialEnvio").toString().equals("")
								&& !parametros.get("fechaFinalEnvio").toString().equals("")))
				{
					if(parametros.get("fechaInicialEnvio").toString().equals(""))
						errores.add("descripcion",new ActionMessage("errors.required","La Fecha Inicial de Envío"));
					
					if(parametros.get("fechaFinalEnvio").toString().equals(""))
						errores.add("descripcion",new ActionMessage("errors.required","La Fecha Final de Envío"));
				}
				else if(!parametros.get("fechaInicialEnvio").toString().equals("") && 
							!parametros.get("fechaFinalEnvio").toString().equals(""))
				{
					if(!UtilidadFecha.validarFecha(parametros.get("fechaInicialEnvio").toString()))
						errores.add("descripcion",new ActionMessage("errors.formatoFechaInvalido"," Inicial de Envío"));
					
					if(!UtilidadFecha.validarFecha(parametros.get("fechaFinalEnvio").toString()))
						errores.add("descripcion",new ActionMessage("errors.formatoFechaInvalido"," Final de Envío"));
					
					if(UtilidadFecha.validarFecha(parametros.get("fechaInicialEnvio").toString()) && 
						UtilidadFecha.validarFecha(parametros.get("fechaFinalEnvio").toString()))					
					{
						if(UtilidadFecha.esFechaMenorQueOtraReferencia(parametros.get("fechaFinalEnvio").toString(), parametros.get("fechaInicialEnvio").toString()))
						{
							errores.add("descripcion",new ActionMessage("errors.fechaAnteriorIgualActual"," Final de Envío "," Inicial de Envío "));
						}
						else if(UtilidadFecha.numeroDiasEntreFechas(parametros.get("fechaInicialEnvio").toString(),parametros.get("fechaFinalEnvio").toString()) > 31)
							errores.add("descripcion",new ActionMessage("errors.fechaSuperaOtraPorDias"," Final de Envío ","30"," Inicial de Envío "));
						
						if(infFechaGen)
						{
							if(UtilidadFecha.esFechaMenorQueOtraReferencia(parametros.get("fechaInicialEnvio").toString(), parametros.get("fechaInicialGeneracion").toString()))					
								errores.add("descripcion",new ActionMessage("errors.fechaAnteriorIgualActual"," Inicial de Envío "," Inicial de Generación Informe "));					
						}
					}					
					
					if(errores.isEmpty())
						infFechaEnv = true;		
				}
			}
			else
			{
				parametros.put("fechaInicialEnvio","");
				parametros.put("fechaFinalEnvio","");					
			}
			
			//En el caso de no existir fecha de generación es requerido la fecha de envío
			if(errores.isEmpty() && 
					!infFechaGen)
			{
				if(!infFechaEnv)
					errores.add("descripcion",new ActionMessage("errors.required"," La Fecha Generación de Informe o la Fecha de Envío "));
			}						
		}
		
		//Convenio
		if(parametros.get("convenio").toString().equals(""))
			errores.add("descripcion",new ActionMessage("errors.required","El Convenio "));
		
		return errores;		
	}
	
	/**
	 * Validaciones de busqueda por rango
	 * @param HashMap parametros
	 * */
	public static ActionErrors validacionConsultaBusquedaXRango(HashMap parametros)
	{
		Utilidades.imprimirMapa(parametros);
		ActionErrors errores = new ActionErrors();		
		boolean infFechaGen = false;
		boolean infFechaEnv = false;		
		
		//Fecha de Generacion
		if((!parametros.get("fechaInicialGeneracion").toString().equals("") 
				&& parametros.get("fechaFinalGeneracion").toString().equals("")) || 
					(parametros.get("fechaInicialGeneracion").toString().equals("")
						&& !parametros.get("fechaFinalGeneracion").toString().equals("")))
		{
			if(parametros.get("fechaInicialGeneracion").toString().equals(""))
				errores.add("descripcion",new ActionMessage("errors.required","La Fecha Inicial de Generación Informe"));
			
			if(parametros.get("fechaFinalGeneracion").toString().equals(""))
				errores.add("descripcion",new ActionMessage("errors.required","La Fecha Final de Generación Informe"));
		}
		else if(!parametros.get("fechaInicialGeneracion").toString().equals("") && 
					!parametros.get("fechaFinalGeneracion").toString().equals(""))
		{
			if(!UtilidadFecha.validarFecha(parametros.get("fechaInicialGeneracion").toString()))
				errores.add("descripcion",new ActionMessage("errors.formatoFechaInvalido"," Inicial de Generación Informe"));
			
			if(!UtilidadFecha.validarFecha(parametros.get("fechaFinalGeneracion").toString()))
				errores.add("descripcion",new ActionMessage("errors.formatoFechaInvalido"," Final de Generación Informe"));
			
			if(UtilidadFecha.validarFecha(parametros.get("fechaInicialGeneracion").toString()) && 
					UtilidadFecha.validarFecha(parametros.get("fechaFinalGeneracion").toString()))
			{
				if(UtilidadFecha.esFechaMenorQueOtraReferencia(parametros.get("fechaFinalGeneracion").toString(), parametros.get("fechaInicialGeneracion").toString()))
				{
					errores.add("descripcion",new ActionMessage("errors.fechaAnteriorIgualActual"," Final de Generación Informe "," Inicial de Generación Informe"));
				}
				else if(UtilidadFecha.numeroDiasEntreFechas(parametros.get("fechaInicialGeneracion").toString(),parametros.get("fechaFinalGeneracion").toString()) > 31)
					errores.add("descripcion",new ActionMessage("errors.fechaSuperaOtraPorDias"," Final de Generación Informe ","30"," Inicial de Generación Informe "));				
			}			

			if(errores.isEmpty())
				infFechaGen = true;				
		}
			
		
		if(parametros.get("informesNoGen").toString().equals(ConstantesBD.acronimoSi))
		{
			parametros.put("fechaInicialGeneracion","");
			parametros.put("fechaFinalGeneracion","");
			parametros.put("fechaInicialEnvio","");
			parametros.put("fechaFinalEnvio","");
			parametros.put("estadoEnvio","");			
		}
		else
		{
			//Evalua la información del estado de envio
			if(parametros.get("estadoEnvio").equals(""))
			{
				//Fecha de Envio
				if((!parametros.get("fechaInicialEnvio").toString().equals("") 
						&& parametros.get("fechaFinalEnvio").toString().equals("")) || 
							(parametros.get("fechaInicialEnvio").toString().equals("")
								&& !parametros.get("fechaFinalEnvio").toString().equals("")))
				{
					if(parametros.get("fechaInicialEnvio").toString().equals(""))
						errores.add("descripcion",new ActionMessage("errors.required","La Fecha Inicial de Envío"));
					
					if(parametros.get("fechaFinalEnvio").toString().equals(""))
						errores.add("descripcion",new ActionMessage("errors.required","La Fecha Final de Envío"));
				}
				else if(!parametros.get("fechaInicialEnvio").toString().equals("") && 
							!parametros.get("fechaFinalEnvio").toString().equals(""))
				{
					if(!UtilidadFecha.validarFecha(parametros.get("fechaInicialEnvio").toString()))
						errores.add("descripcion",new ActionMessage("errors.formatoFechaInvalido"," Inicial de Envío"));
					
					if(!UtilidadFecha.validarFecha(parametros.get("fechaFinalEnvio").toString()))
						errores.add("descripcion",new ActionMessage("errors.formatoFechaInvalido"," Final de Envío"));
					
					if(UtilidadFecha.validarFecha(parametros.get("fechaInicialEnvio").toString()) && 
						UtilidadFecha.validarFecha(parametros.get("fechaFinalEnvio").toString()))					
					{
						if(UtilidadFecha.esFechaMenorQueOtraReferencia(parametros.get("fechaFinalEnvio").toString(), parametros.get("fechaInicialEnvio").toString()))
						{
							errores.add("descripcion",new ActionMessage("errors.fechaAnteriorIgualActual"," Final de Envío "," Inicial de Envío "));
						}
						else if(UtilidadFecha.numeroDiasEntreFechas(parametros.get("fechaInicialEnvio").toString(),parametros.get("fechaFinalEnvio").toString()) > 31)
							errores.add("descripcion",new ActionMessage("errors.fechaSuperaOtraPorDias"," Final de Envío ","30"," Inicial de Envío "));
						
						if(infFechaGen)
						{
							if(UtilidadFecha.esFechaMenorQueOtraReferencia(parametros.get("fechaInicialEnvio").toString(), parametros.get("fechaInicialGeneracion").toString()))					
								errores.add("descripcion",new ActionMessage("errors.fechaAnteriorIgualActual"," Inicial de Envío "," Inicial de Generación Informe "));					
						}
					}					
					
					if(errores.isEmpty())
						infFechaEnv = true;		
				}
			}
			else
			{
				parametros.put("fechaInicialEnvio","");
				parametros.put("fechaFinalEnvio","");					
			}
			
			//En el caso de no existir fecha de generación es requerido la fecha de envío
			if(errores.isEmpty() && 
					!infFechaGen)
			{
				if(!infFechaEnv)
					errores.add("descripcion",new ActionMessage("errors.required"," La Fecha Generación de Informe o la Fecha de Envío "));
			}
		}							
				
		//Convenio
		if(parametros.get("convenio").toString().equals(""))
			errores.add("descripcion",new ActionMessage("errors.required","El Convenio "));
		
		return errores;		
	}
	
	
	/**
	 * Carga la informacion basica del paciente
	 * @param Connection con
	 * @param String ingreso
	 * @param String cuenta
	 * @param String codigoPkInforme
	 * @param String fechaInicialEnvio
	 * @param String fechaFinalEnvio
	 * */
	public static DtoInformeAtencionIniUrg cargarInfoPaciente(
			Connection con,
			int institucion,
			String ingreso,
			String cuenta,
			String convenio,
			String codigoPkInforme,
			String fechaInicialEnvio,
			String fechaFinalEnvio,
			boolean cargarConvenio)
	{
		HashMap parametros = new HashMap();
		parametros.put("ingreso",ingreso);
		parametros.put("institucion",institucion);
		parametros.put("cuenta",cuenta);
		parametros.put("convenio",convenio);
		parametros.put("codigoPkInforme",codigoPkInforme);
		parametros.put("fechaInicialEnvio",fechaInicialEnvio);
		parametros.put("fechaFinalEnvio",fechaFinalEnvio);
		parametros.put("cargarConvenio",cargarConvenio?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
		
		return getRegistroEnvioInfAtencionIniUrgDao().cargarInfoPaciente(con, parametros);
	}
	
	/**
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public static ArrayList<DtoInformeAtencionIniUrg> getConveniosPacienteReporte(Connection con, String cuenta,String ingreso)	
	{
		HashMap parametros = new HashMap();
		parametros.put("ingreso",ingreso);
		parametros.put("cuenta",cuenta);
		
		return getRegistroEnvioInfAtencionIniUrgDao().getConveniosPacienteReporte(con, parametros);
	}
	
	/**
	 * Inserta informacion del informe atencion inicial de urgencias
	 * @param Connection con
	 * @param String ingreso
	 * @param String cuenta
	 * @param int institucion
	 * @param String usuarioLogin
	 * @return Retorna las llaves de [error] objeto ActionErrors y [codigoPk] consecutivo de reporte generado
	 * */
	public static HashMap insertarInformeAtencionIniUrg(Connection con,String ingreso,String cuenta,String codigoConvenio,int institucion,String usuarioLogin)
	{
		HashMap parametros = new HashMap();
		parametros.put("ingreso", ingreso);
		parametros.put("cuenta", cuenta);
		parametros.put("convenio", codigoConvenio);
		parametros.put("institucion", institucion);
		parametros.put("usuarioLogin", usuarioLogin);		
		return getRegistroEnvioInfAtencionIniUrgDao().insertarInformeAtencionIniUrg(con, parametros);
	}
	
	/**
	 * Inserta informacion del envio del informe de atencion inicial de urgencias
	 * @param Connection con
	 * @param int infoAtencionIniUrg
	 * @param String entidadEnvio
	 * @param String medioEnvio
	 * @param String convenioEnvio 
	 * */
	public static int insertarEnvioInformeAtencionIniUrg(
			Connection con,
			int infoAtencionIniUrg,
			String convenioEnvio,
			String medioEnvio,
			String entidadEnvio,
			String usuarioEnvio,
			String pathArchivo)
	{
		HashMap parametros = new HashMap();
		parametros.put("infoAtencionIniUrg", infoAtencionIniUrg);
		parametros.put("entidadEnvio", entidadEnvio);
		parametros.put("medioEnvio", medioEnvio);
		parametros.put("convenioEnvio", convenioEnvio);		
		parametros.put("usuarioEnvio", usuarioEnvio);
		parametros.put("pathArchivo",pathArchivo);
		return getRegistroEnvioInfAtencionIniUrgDao().insertarEnvioInformeAtencionIniUrg(con, parametros);
	}
	
	/**
	 * Verifica si el paciente posee ingresos con valoraciones de urgencias
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public static InfoDatosString tienePacienteIngresoValoracionUrg(Connection con,int institucion, int codigoPaciente)
	{
		HashMap parametros = new HashMap();
		parametros.put("institucion", institucion);
		parametros.put("codigoPaciente",codigoPaciente);
		
		return getRegistroEnvioInfAtencionIniUrgDao().tienePacienteIngresoValoracionUrg(con, parametros);
	}
	
	/**
	 * Valida si el paciente es valido
	 * @param Connection con
	 * @param PersonaBasica paciente
	 * */
	public static ActionErrors validarPaciente(Connection con,PersonaBasica paciente, int institucion)
	{
		ActionErrors errores = new ActionErrors();
		
		if(paciente.getCodigoPersona()<1)
			errores.add("descripcion",new ActionMessage("errors.notEspecific","No hay ningún paciente cargado. Para acceder a esta funcionalidad por el Flujo de Paciente debe cargar un paciente"));
		else
		{
			InfoDatosString info = tienePacienteIngresoValoracionUrg(con, institucion,paciente.getCodigoPersona());
			
			if(!UtilidadTexto.getBoolean(info.getCodigo()))
				errores.add("descripcion",new ActionMessage("errors.notEspecific","El Paciente Cargado no Posee Ingresos de Urgencias con Valoraciones Generadas."));
			else if(!UtilidadTexto.getBoolean(info.getNombre()))
				errores.add("descripcion",new ActionMessage("errors.notEspecific","El Paciente Cargado no Posee Convenios Activos para la Generación del Reporte Incial de Urgencias."));			
		}	
		
		return errores;
	}
	
	/**
	 * Listado de Informe Atencion Inicial de Urgencias
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public static ArrayList<DtoInformeAtencionIniUrg> getListadoInformeInicUrgeXPaciente(
			Connection con,
			int institucion,
			int codigoPaciente,
			boolean soloConInforme,
			boolean soloInformesNoGenerados)
	{
		HashMap parametros = new HashMap();
		parametros.put("institucion", institucion);
		parametros.put("codigoPaciente", codigoPaciente);
		parametros.put("soloConInforme", soloConInforme?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
		parametros.put("informesNoGen", soloInformesNoGenerados?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
		
		return SqlBaseRegistroEnvioInfAtencionIniUrgDao.getListadoInformeInicUrgeXPaciente(con, parametros);		
	}
	
	/**
	 * Listado de Informe Atencion Inicial de Urgencias
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public static ArrayList<DtoInformeAtencionIniUrg> getListadoInformeInicUrgeXPacienteIngresos(
			Connection con,
			int institucion,
			int codigoPaciente)
	{
		HashMap parametros = new HashMap();
		parametros.put("institucion", institucion);
		parametros.put("codigoPaciente", codigoPaciente);
		return SqlBaseRegistroEnvioInfAtencionIniUrgDao.getListadoInformeInicUrgeXPacienteIngreso(con, parametros);		
	}

	
	public static DtoInformeAtencionIniUrg getInformeInicUrge(Connection con, int cod_informe)
	{
		HashMap parametros = new HashMap<String,Object>();
		parametros.put("cod_informe", cod_informe);
		return SqlBaseRegistroEnvioInfAtencionIniUrgDao.getInformeInicUrge(con, parametros);
	}
}