package com.princetonsa.mundo.manejoPaciente;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMessage;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadFecha;
import util.Utilidades;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.manejoPaciente.ConsultaEnvioInconsistenciasenBDDao;
import com.princetonsa.dao.sqlbase.manejoPaciente.SqlBaseConsultaEnvioInconsistenciasenBDDao;
import com.princetonsa.dao.sqlbase.manejoPaciente.SqlBaseRegistroEnvioInformInconsisenBDDao;
import com.princetonsa.dto.manejoPaciente.DtoInformeAtencionIniUrg;
import com.princetonsa.dto.manejoPaciente.DtoInformeInconsisenBD;
import com.princetonsa.dto.manejoPaciente.DtoRegistroEnvioInformInconsisenBD;
import com.princetonsa.mundo.PersonaBasica;

public class ConsultaEnvioInconsistenciasenBD {

	public static ConsultaEnvioInconsistenciasenBDDao getConsultaEnvioInconsistenciasenBDDao()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConsultaEnvioInconsistenciasenBDDao();
	}
	
	
	
	public static ActionErrors validarPaciente(Connection con,PersonaBasica paciente, int institucion)
	{
		ActionErrors errores = new ActionErrors();
		
		if(paciente.getCodigoPersona()<1)
			errores.add("descripcion",new ActionMessage("errors.notEspecific","No hay ningún paciente cargado. Para acceder a esta funcionalidad por el Flujo de Paciente debe cargar un paciente"));

		return errores;
	}
	
	
	
	public static HashMap consultarIngresosPaciente (Connection con, int codigoInstitucion, int codigoPaciente)
	{
		return getConsultaEnvioInconsistenciasenBDDao().consultarIngresosPaciente(con, codigoInstitucion, codigoPaciente);
	}
	
	/**
	 * Realiza la consulta de un Informe de Incosistencias en BD asociado a un Ingreso y convenio Responsable
	 * devuelve el Dto de informe para ser cargado  
	 * @param con
	 * @param codIngreso
	 * @param codigoInstitucion
	 * @param codigoPaciente
	 * @param codConvenio
	 * @return
	 */
	public static  DtoInformeInconsisenBD cargarInformeIncosistencias(Connection con, String codIngreso, int codigoInstitucion, int codigoPaciente, String codConvenio,String codSubcuenta)
	{
		
		HashMap filtros=new HashMap();	
		filtros.put("codingreso", codIngreso);
		filtros.put("codpaciente", codigoPaciente);
		filtros.put("codconvenio", codConvenio);
		filtros.put("codinstitucion", codigoInstitucion);
		filtros.put("codSubcuenta", codSubcuenta);
		
		return getConsultaEnvioInconsistenciasenBDDao().cargarInformeIncosistencias(con,filtros);
	}
	
	
	/**
     * Realiza la consulta de los historicos ( Los envios que se han realizado a un informe) 
     * @param con
     * @param codInforme
     * @return
     */
	public ArrayList<DtoRegistroEnvioInformInconsisenBD> consultaHistoricosInforme(Connection con, int codInforme) {
		
		return SqlBaseConsultaEnvioInconsistenciasenBDDao.consultaHistoricosInforme(con,codInforme);
	}
	
	/**
	 * Realiza la insercion de los datos de envio del Informe
	 * @param con
	 * @param parametros
	 * @return
	 */
	public HashMap insertarEnvioInformeInconsistencias(Connection con,HashMap parametros)
	{
		return getConsultaEnvioInconsistenciasenBDDao().insertarEnvioInformeInconsistencias(con,parametros);
	}
	
	/**
	 * 
	 * @param con
	 * @param codInforme
	 */
	public void actualizarEstadoEnvioInforme(Connection con, int codInforme)
	{
		SqlBaseConsultaEnvioInconsistenciasenBDDao.actualizarEstadoEnvioInforme(con, codInforme);
	}
	
	/**
	 * Inicializa los parametros de busqueda por rango 
	 * */
	public static HashMap inicializarParametrosBusquedaRango()
	{
		HashMap parametros = new HashMap();
		parametros.put("fechaInicialGeneracion",UtilidadFecha.getFechaActual());
		parametros.put("fechaFinalGeneracion",UtilidadFecha.getFechaActual());
		parametros.put("fechaInicialEnvio",UtilidadFecha.getFechaActual());
		parametros.put("fechaFinalEnvio",UtilidadFecha.getFechaActual());
		parametros.put("estadoEnvio","");
		parametros.put("convenio", "");
		
		return parametros;
	}
	
	/**
	 * 
	 * @param con
	 * @return
	 */
	 public static HashMap consultaConvenios(Connection con)
	 {
		
		 return getConsultaEnvioInconsistenciasenBDDao().consultaConvenios(con);
	 }
	 
	 
	 
	 
	 /**
		 * Validaciones de busqueda por rango
		 * @param HashMap parametros
		 * */
		public static ActionErrors validacionBusquedaporRango(HashMap parametros)
		{
			Utilidades.imprimirMapa(parametros);
			ActionErrors errores = new ActionErrors();

			boolean infFechaGen = false;
			boolean infFechaEnv = false;		
					
	if(!parametros.get("estadoEnvio").toString().equals(""))
	  {
			
				parametros.put("fechaInicialEnvio","");
				parametros.put("fechaFinalEnvio","");
	  }
				
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
				
					
		        	if(errores.isEmpty() && 
							!infFechaGen)
					{
						if(!infFechaEnv)
							errores.add("descripcion",new ActionMessage("errors.required"," La Fecha de Envío "));
					}
	         
			
			//Convenio
			if(parametros.get("convenio").toString().equals(""))
				errores.add("descripcion",new ActionMessage("errors.required","El Convenio "));
			
			return errores;		
		}
	 
	 
		public static ArrayList<DtoInformeInconsisenBD> getListadoInformeInconsistencias(
				Connection con,
				String fechaInicialGeneracion,
				String fechaFinalGeneracion,
				String fechaInicialEnvio,
				String fechaFinalEnvio,			
				String estadoEnvio,
				String convenio,
				int institucion)
		{
			HashMap parametros = new HashMap();
			
			parametros.put("fechaInicialGeneracion",UtilidadFecha.conversionFormatoFechaABD(fechaInicialGeneracion));
			parametros.put("fechaFinalGeneracion",UtilidadFecha.conversionFormatoFechaABD(fechaFinalGeneracion));
			parametros.put("fechaInicialEnvio",UtilidadFecha.conversionFormatoFechaABD(fechaInicialEnvio));
			parametros.put("fechaFinalEnvio",UtilidadFecha.conversionFormatoFechaABD(fechaFinalEnvio));	
			parametros.put("estadoEnvio",estadoEnvio);
			parametros.put("convenio",convenio);		
			parametros.put("institucion",institucion);
			
			return getConsultaEnvioInconsistenciasenBDDao().getListadoInformeInconsistencias(con, parametros);
		}
	 
	 
	 
	 
	 
	
}
