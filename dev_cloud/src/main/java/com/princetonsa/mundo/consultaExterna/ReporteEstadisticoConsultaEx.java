package com.princetonsa.mundo.consultaExterna;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMessage;

import com.princetonsa.action.salasCirugia.HojaAnestesiaAction;
import com.princetonsa.actionform.inventarios.MovimientosAlmacenesForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.TagDao;
import com.princetonsa.dao.UtilidadesBDDao;
import com.princetonsa.dao.consultaExterna.ReporteEstadisticoConsultaExDao;
import com.princetonsa.dao.salasCirugia.HojaAnestesiaDao;
import com.princetonsa.dao.salasCirugia.UtilidadesSalasDao;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.util.birt.reports.DesignEngineApi;
import com.princetonsa.util.birt.reports.ExtractCSV;
import com.princetonsa.util.birt.reports.ParamsBirtApplication;

import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.InfoDatosString;
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.UtilidadValidacion;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.consultaExterna.UtilidadesConsultaExterna;
import util.historiaClinica.UtilidadesHistoriaClinica;
import util.manejoPaciente.UtilidadesManejoPaciente;

/**
 * @author Jose Eduardo Arias Doncel
 * */
public class ReporteEstadisticoConsultaEx
{	
	
	Logger logger = Logger.getLogger(ReporteEstadisticoConsultaEx.class);
	
	//**********************************************************************
	
	/**
	 * Instancia el Dao
	 * */
	public static ReporteEstadisticoConsultaExDao getReporteEstadisticoConsultaExDao()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getReporteEstadisticoConsultaExDao();
	}
	
	//**********************************************************************	
	
	/**
	 * Valida los permisos del usuario para ingresar a la funcionalidad
	 * @param String loginUsuario
	 * @param int codigoFuncionalidad
	 * @param ActionErrors errores
	 * */
	public static ActionErrors metodoValidarUsuario(String loginUsuario, int codigoFuncionalidad, ActionErrors errores)
	{
		if(!Utilidades.tieneRolFuncionalidad(loginUsuario,codigoFuncionalidad))	
			errores.add("descripcion",new ActionMessage("errors.usuarioSinRolFuncionalidad",""," la Funcionalidad "));		
			
		return errores;
	}
	
	//**********************************************************************	
	
	/**
	 * Devuelve el mapa con los datos de busqueda dependiendo del tipo de reporte
	 * Keys del Mapa
	 * 
	 * existeReporte (indica si encontro el reporte)
	 * nombreReporte
	 * 
	 * estadoCentroAtencion
	 * estructuraCentroAtencion
	 * codigoCentroAtencion
	 * 
	 * estadoFechaInicial
	 * fechaInicial
	 * estadoFechaFinal
	 * fechaFinal 
	 * rangoMesesFecha
	 * 
	 * estadoEspecialidad
	 * estructuraEspecialidad
	 * 
	 * estadoTipoConsulta
	 * estructuraTipoConsulta
	 * codigoTipoConsulta
	 * 
	 * estadoCanceladaPor
	 * estructuraCanceladaPor
	 * codigoCanceladaPor 
	 * 
	 * estadoMotivoCancelacion
	 * codigoMotivoCancelacion
	 * 
	 * estadoProfesionalSalud
	 * estructuraProfesionalSalud
	 * codigoProfesionalSalud
	 * 
	 * @param Connection con
	 * @param UsuarioBasico usuario
	 * @param int codigoReporte
	 * */
	public static HashMap metodoActualizarMapaBusqueda(Connection con, UsuarioBasico usuario, int codigoReporte)
	{
		HashMap respuesta = new HashMap();
		HashMap tmp;
		
		//Indica si existe informacion del reporte
		respuesta.put("existeReporte",ConstantesBD.acronimoSi);
		
		//El centro de atencion es requerido para todos los reportes
		respuesta.put("estadoCentroAtencion",ConstantesIntegridadDominio.acronimoEstadoRequerido);
		respuesta.put("estructuraCentroAtencion",Utilidades.obtenerCentrosAtencion(usuario.getCodigoInstitucionInt()));
		respuesta.put("codigoCentroAtencion",usuario.getCodigoCentroAtencion()+ConstantesBD.separadorSplit+usuario.getCentroAtencion());
		
		//El Rango de Fechas es requerido para todos los reportes
		respuesta.put("estadoFechaInicial",ConstantesIntegridadDominio.acronimoEstadoRequerido);
		respuesta.put("fechaInicial",UtilidadFecha.getFechaActual());		
		respuesta.put("estadoFechaFinal",ConstantesIntegridadDominio.acronimoEstadoRequerido);
		respuesta.put("fechaFinal",UtilidadFecha.getFechaActual());
				
		//Validacion Reporte
		switch (codigoReporte) 
		{
			case ConstantesBD.codigoReporteIndicadoresGestionConsEx:
				respuesta.put("nombreReporte","Indicadores de Gestión Consulta Externa");
				respuesta.put("rangoMesesFecha",12);				
				respuesta.put("estadoEspecialidad",ConstantesIntegridadDominio.acronimoEstadoNoRequerido);
				
				tmp = new HashMap(); 
				tmp.put("numRegistros","0");
				respuesta.put("estructuraEspecialidad",tmp);
				
				respuesta.put("estadoTipoConsulta",ConstantesIntegridadDominio.acronimoEstadoNoAplica);				
				respuesta.put("estadoCanceladaPor",ConstantesIntegridadDominio.acronimoEstadoNoAplica);
				
				respuesta.put("estadoMotivoCancelacion",ConstantesIntegridadDominio.acronimoEstadoNoAplica);
				respuesta.put("estadoProfesionalSalud",ConstantesIntegridadDominio.acronimoEstadoNoAplica);
				
			break;
			
			//******************************************************************************************
			
			case ConstantesBD.codigoReporteOportuConsulExEspecialidad:
				respuesta.put("nombreReporte","Oportunidad Consulta Externa por Especialidad");
				respuesta.put("rangoMesesFecha",3);
				respuesta.put("estadoEspecialidad",ConstantesIntegridadDominio.acronimoEstadoNoRequerido);
				
				tmp = new HashMap();
				tmp.put("numRegistros","0");
				respuesta.put("estructuraEspecialidad",tmp);
				
				respuesta.put("estadoTipoConsulta",ConstantesIntegridadDominio.acronimoEstadoNoRequerido);
				respuesta.put("estructuraTipoConsulta",metodoArmarTiposConsulta());
				respuesta.put("codigoTipoConsulta",ConstantesIntegridadDominio.acronimoTodos);				
				
				respuesta.put("estadoCanceladaPor",ConstantesIntegridadDominio.acronimoEstadoNoAplica);				
				respuesta.put("estadoMotivoCancelacion",ConstantesIntegridadDominio.acronimoEstadoNoAplica);
				respuesta.put("estadoProfesionalSalud",ConstantesIntegridadDominio.acronimoEstadoNoAplica);
				
			break;
			
			//******************************************************************************************
			
			case ConstantesBD.codigoReporteOportuConsulExProfesionalSalud:
				respuesta.put("nombreReporte","Oportunidad Consulta Externa por Profesional de la Salud");
				respuesta.put("rangoMesesFecha",3);
				respuesta.put("estadoEspecialidad",ConstantesIntegridadDominio.acronimoEstadoNoAplica);
				
				respuesta.put("estadoTipoConsulta",ConstantesIntegridadDominio.acronimoEstadoNoRequerido);
				respuesta.put("estructuraTipoConsulta",metodoArmarTiposConsulta());
				respuesta.put("codigoTipoConsulta",ConstantesIntegridadDominio.acronimoTodos);
				
				respuesta.put("estadoCanceladaPor",ConstantesIntegridadDominio.acronimoEstadoNoAplica);				
				respuesta.put("estadoMotivoCancelacion",ConstantesIntegridadDominio.acronimoEstadoNoAplica);
								
				//respuesta.put("estadoProfesionalSalud",ConstantesIntegridadDominio.acronimoEstadoNoRequerido);
				//respuesta.put("estructuraProfesionalSalud",metodoArmarProfesionalSalud(con, usuario));	
				//respuesta.put("codigoProfesionalSalud",ConstantesBD.codigoNuncaValido+"");
				
				tmp = new HashMap();
				tmp.put("numRegistros","0");
				respuesta.put("estructuraProfesional",tmp);
				
			break;
			
			//******************************************************************************************
			
			case ConstantesBD.codigoReporteIndicadoresOportuEspecMes:
				respuesta.put("nombreReporte","Indicadores de Oportunidad por Especialidad y Mes");				
				respuesta.put("rangoMesesFecha",3);
				respuesta.put("estadoEspecialidad",ConstantesIntegridadDominio.acronimoEstadoNoRequerido);
				
				tmp = new HashMap();
				tmp.put("numRegistros","0");
				respuesta.put("estructuraEspecialidad",tmp);
				
				respuesta.put("estadoTipoConsulta",ConstantesIntegridadDominio.acronimoEstadoNoAplica);				
				respuesta.put("estadoCanceladaPor",ConstantesIntegridadDominio.acronimoEstadoNoAplica);
				respuesta.put("estadoMotivoCancelacion",ConstantesIntegridadDominio.acronimoEstadoNoAplica);
				respuesta.put("estadoProfesionalSalud",ConstantesIntegridadDominio.acronimoEstadoNoAplica);
				
			break;
			
			//******************************************************************************************
			
			case ConstantesBD.codigoReporteIndiceCancelacionCitaMes:
				respuesta.put("nombreReporte","Indice de Cancelación Cita por Mes");
				respuesta.put("rangoMesesFecha",6);
				respuesta.put("estadoEspecialidad",ConstantesIntegridadDominio.acronimoEstadoNoRequerido);
				
				tmp = new HashMap();
				tmp.put("numRegistros","0");
				respuesta.put("estructuraEspecialidad",tmp);				
				
				respuesta.put("estadoTipoConsulta",ConstantesIntegridadDominio.acronimoEstadoNoAplica);				
				
				respuesta.put("estadoCanceladaPor",ConstantesIntegridadDominio.acronimoEstadoRequerido);
				respuesta.put("estructuraCanceladaPor",metodoArmarCancelacionCita());			
				
				respuesta.put("estadoMotivoCancelacion",ConstantesIntegridadDominio.acronimoEstadoNoAplica);
				respuesta.put("estadoProfesionalSalud",ConstantesIntegridadDominio.acronimoEstadoNoAplica);
				
			break;
			
			//******************************************************************************************
			
			case ConstantesBD.codigoReporteMotivoCancelacionCitaPaciente:
				respuesta.put("nombreReporte","Motivo Cancelación Cita por Paciente");
				respuesta.put("rangoMesesFecha",12);
				respuesta.put("estadoEspecialidad",ConstantesIntegridadDominio.acronimoEstadoNoRequerido);
				
				tmp = new HashMap();
				tmp.put("numRegistros","0");
				respuesta.put("estructuraEspecialidad",tmp);
				
				respuesta.put("estadoTipoConsulta",ConstantesIntegridadDominio.acronimoEstadoNoAplica);
				
				respuesta.put("estadoCanceladaPor",ConstantesIntegridadDominio.acronimoEstadoRequerido);
				respuesta.put("estructuraCanceladaPor",metodoArmarCancelacionCita());
				
				respuesta.put("estadoMotivoCancelacion",ConstantesIntegridadDominio.acronimoEstadoNoRequerido);			
				respuesta.put("codigoMotivoCancelacion",ConstantesIntegridadDominio.acronimoTodos);
				
				respuesta.put("estadoProfesionalSalud",ConstantesIntegridadDominio.acronimoEstadoNoAplica);
				
				//**************CAMBIO POR LA TAREA 61450**************************
				respuesta.put("codigoCanceladaPor",ConstantesBD.codigoEstadoCitaCanceladaPaciente);
				respuesta.put("nombreCanceladaPor","Cancelación Paciente");
				//**************CAMBIO POR LA TAREA 61450**************************
			break;
			
			//******************************************************************************************
			
			case ConstantesBD.codigoReporteMotivoCancelacionCitaInstitucion:
				respuesta.put("nombreReporte","Motivo Cancelación Cita por Institución");
				respuesta.put("rangoMesesFecha",12);
				respuesta.put("estadoEspecialidad",ConstantesIntegridadDominio.acronimoEstadoNoAplica);
				
				respuesta.put("estadoTipoConsulta",ConstantesIntegridadDominio.acronimoEstadoNoAplica);
				
				respuesta.put("estadoCanceladaPor",ConstantesIntegridadDominio.acronimoEstadoRequerido);
				respuesta.put("estructuraCanceladaPor",metodoArmarCancelacionCita());
				
				respuesta.put("estadoMotivoCancelacion",ConstantesIntegridadDominio.acronimoEstadoNoRequerido);				
				respuesta.put("codigoMotivoCancelacion",ConstantesIntegridadDominio.acronimoTodos);
				
				respuesta.put("estadoProfesionalSalud",ConstantesIntegridadDominio.acronimoEstadoNoRequerido);
				
				tmp = new HashMap();
				tmp.put("numRegistros","0");
				respuesta.put("estructuraProfesional",tmp);
				
				//**************CAMBIO POR LA TAREA 61450**************************
				respuesta.put("codigoCanceladaPor",ConstantesBD.codigoEstadoCitaCanceladaInstitucion);
				respuesta.put("nombreCanceladaPor","Cancelación Institución");
				//**************CAMBIO POR LA TAREA 61450**************************
			break;
			
			//******************************************************************************************
			
			case ConstantesBD.codigoReporteMotivoCancelacionCitaMes:
				respuesta.put("nombreReporte","Motivo Cancelación Cita por Mes");
				respuesta.put("rangoMesesFecha",12);
				respuesta.put("estadoEspecialidad",ConstantesIntegridadDominio.acronimoEstadoNoAplica);
				
				respuesta.put("estadoTipoConsulta",ConstantesIntegridadDominio.acronimoEstadoNoAplica);
				
				respuesta.put("estadoCanceladaPor",ConstantesIntegridadDominio.acronimoEstadoRequerido);
				respuesta.put("estructuraCanceladaPor",metodoArmarCancelacionCita());
				
				respuesta.put("estadoMotivoCancelacion",ConstantesIntegridadDominio.acronimoEstadoNoRequerido);				
				respuesta.put("codigoMotivoCancelacion",ConstantesIntegridadDominio.acronimoTodos);
				
				respuesta.put("estadoProfesionalSalud",ConstantesIntegridadDominio.acronimoEstadoNoAplica);
				
			break;
			
			//******************************************************************************************
			
			default:
				respuesta.put("existeReporte",ConstantesBD.acronimoNo);
			break;
		}
		
		return respuesta;
	}
	
	//*******************************************************************************************************************
	
	/**
	 * Arma el mapa con los tipos de consulta
	 * */
	public static HashMap metodoArmarTiposConsulta()
	{
		HashMap respuesta = new HashMap();
		respuesta.put("codigo_0",ConstantesIntegridadDominio.acronimoTodos);
		respuesta.put("nombre_0",ValoresPorDefecto.getIntegridadDominio(ConstantesIntegridadDominio.acronimoTodos));		
		
		respuesta.put("codigo_1",ConstantesIntegridadDominio.acronimoPrimeraVez);
		respuesta.put("nombre_1",ValoresPorDefecto.getIntegridadDominio(ConstantesIntegridadDominio.acronimoPrimeraVez));		
		
		respuesta.put("codigo_2",ConstantesIntegridadDominio.acronimoControl);
		respuesta.put("nombre_2",ValoresPorDefecto.getIntegridadDominio(ConstantesIntegridadDominio.acronimoControl));
				
		respuesta.put("numRegistros","3");
		
		return respuesta;		
	}
	
	//*******************************************************************************************************************
	
	/**
	 * Arma el mapa con los estados de cancelacion de la cita
	 * */
	public static HashMap metodoArmarCancelacionCita()
	{
		HashMap respuesta = new HashMap();
		respuesta.put("codigo_0",ConstantesBD.codigoEstadoCitaCanceladaPaciente);
		respuesta.put("nombre_0","Cancelación Paciente");		
		
		respuesta.put("codigo_1",ConstantesBD.codigoEstadoCitaCanceladaInstitucion);
		respuesta.put("nombre_1","Cancelación Institución");
				
		respuesta.put("numRegistros","2");
		
		return respuesta;		
	}
	
	//*******************************************************************************************************************
	
	/**
	 * Arma el mapa con los estados de Motivos de Cancelacion 
	 * @param tipoReporte 
	 * @param Connection con
	 * */
	public static HashMap metodoArmarMotivosCancelacion(Connection con, String codigosInsertados, int tipoReporte)
	{	
		HashMap temporal = new HashMap();	
		MotivosCancelacionCita mundo = new MotivosCancelacionCita();
		//************INICIO SE MODIFICO POR LA TAREA 61450******************
		/*Sustenta que los motivos de cancelación se cargan según el tipo de
		reporte seleccionado
		temporal = mundo.consultarMotivosExistentes(con,codigosInsertados);*/	
		if(tipoReporte == ConstantesBD.codigoReporteMotivoCancelacionCitaPaciente)
			temporal = mundo.consultarMotivosExistentes(con,codigosInsertados,ConstantesBD.codigoEstadoCitaCanceladaPaciente+"");
		else if(tipoReporte == ConstantesBD.codigoReporteMotivoCancelacionCitaInstitucion)
			temporal = mundo.consultarMotivosExistentes(con,codigosInsertados,ConstantesBD.codigoEstadoCitaCanceladaInstitucion+"");
		else
			temporal = mundo.consultarMotivosExistentes(con,codigosInsertados);
		//***************FIN SE MODIFICO POR LA TAREA 61450******************
		
		return temporal;		
	}
	
	//*******************************************************************************************************************
	
	/**
	 * Arma el mapa con los profesionales de salud
	 * @param Connection con
	 * @param UsuarioBasico usuario
	 * */
	public static ArrayList metodoArmarProfesionalSalud(Connection con,UsuarioBasico usuario,String codigosInsertados)
	{				
		return UtilidadesManejoPaciente.obtenerProfesionales(
				con, 
				usuario.getCodigoInstitucionInt(),
				false,
				false,
				"",
				codigosInsertados);
	}
	
	//*****************************************************************************************************
	
	/**
	 * Arma la cadena de codigos insertados separos por coma
	 * @param HashMap mapa 
	 * */
	public static String metodoArmarCodigosInsertados(HashMap mapa)
	{
		String resultado = "";
		
		for(int i = 0; i < Utilidades.convertirAEntero(mapa.get("numRegistros").toString()); i++)
		{			
			if(mapa.get("activo_"+i).toString().equals(ConstantesBD.acronimoSi))	
				resultado += mapa.get("codigo_"+i).toString()+",";		
		}	
				
		return resultado;
	}	
	
	//*****************************************************************************************************
	
	/**
	 * metodo para insertar una dato de seleccion de un listado 
	 * @param HashMap mapa  
	 * @param String codigoElementodAdd
	 * @param String nombreElementoAdd
	 * */
	public static HashMap metodoInsertarDatosSeleccion(HashMap mapa,String codigoElementodAdd,String nombreElementoAdd)
	{		
		int pos = Utilidades.convertirAEntero(mapa.get("numRegistros").toString());
		
		if(!codigoElementodAdd.toString().equals("") 
				&& !nombreElementoAdd.toString().equals(""))
		{
			mapa.put("codigo_"+pos,codigoElementodAdd.toString());
			mapa.put("descripcion_"+pos,nombreElementoAdd.toString());
			mapa.put("activo_"+pos,ConstantesBD.acronimoSi);			
			mapa.put("numRegistros",(pos+1));
		}
		
		return mapa;	
	}
	
	//*****************************************************************************************************
	
	/**
	 * Metodo para la validacion de los datos requeridos para la generación del reporte
	 * @param HashMap mapa
	 * @param int codigoReporte
	 * @param ActionErrors errores
	 * */
	public static ActionErrors metodoValidacionGenerarReporte(HashMap mapa, int codigoReporte, ActionErrors errores)
	{				
		//El centro de atencion es requerido para todos los reportes
		if(mapa.get("estadoCentroAtencion").toString().equals(ConstantesIntegridadDominio.acronimoEstadoRequerido))
			if(mapa.get("codigoCentroAtencion").toString().equals(""))
				errores.add("descripcion",new ActionMessage("errors.required","El Centro de Atención "));
				
		//Validacion Reporte
		switch (codigoReporte) 
		{
			case ConstantesBD.codigoReporteIndicadoresGestionConsEx:
				
				//Validacion del rango de fechas
				if(mapa.get("estadoFechaInicial").toString().equals(ConstantesIntegridadDominio.acronimoEstadoRequerido))
					errores = metodoValidacionRangoFechas(mapa,12, errores);
			
			break;
			
			//******************************************************************************************
			
			case ConstantesBD.codigoReporteOportuConsulExEspecialidad:
				
				//Validacion del rango de fechas
				if(mapa.get("estadoFechaInicial").toString().equals(ConstantesIntegridadDominio.acronimoEstadoRequerido))
					errores = metodoValidacionRangoFechas(mapa,3, errores);
			
			break;
			
			//******************************************************************************************
			
			case ConstantesBD.codigoReporteOportuConsulExProfesionalSalud:
				
				//Validacion del rango de fechas
				if(mapa.get("estadoFechaInicial").toString().equals(ConstantesIntegridadDominio.acronimoEstadoRequerido))
					errores = metodoValidacionRangoFechas(mapa,3,errores);
							
			break;
			
			//******************************************************************************************
			
			case ConstantesBD.codigoReporteIndicadoresOportuEspecMes:
				
				//Validacion del rango de fechas
				if(mapa.get("estadoFechaInicial").toString().equals(ConstantesIntegridadDominio.acronimoEstadoRequerido))
					errores = metodoValidacionRangoFechas(mapa,3,errores);
							
			break;
			
			//******************************************************************************************
			
			case ConstantesBD.codigoReporteIndiceCancelacionCitaMes:
				
				//Validacion del rango de fechas
				if(mapa.get("estadoFechaInicial").toString().equals(ConstantesIntegridadDominio.acronimoEstadoRequerido))
					errores = metodoValidacionRangoFechas(mapa,6, errores);
			
			break;
			
			//******************************************************************************************
			
			case ConstantesBD.codigoReporteMotivoCancelacionCitaPaciente:
				
				//Validacion del rango de fechas
				if(mapa.get("estadoFechaInicial").toString().equals(ConstantesIntegridadDominio.acronimoEstadoRequerido))
					errores = metodoValidacionRangoFechas(mapa,12, errores);
								
			break;
			
			//******************************************************************************************
			
			case ConstantesBD.codigoReporteMotivoCancelacionCitaInstitucion:
				
				//Validacion del rango de fechas
				if(mapa.get("estadoFechaInicial").toString().equals(ConstantesIntegridadDominio.acronimoEstadoRequerido))
					errores = metodoValidacionRangoFechas(mapa,12, errores);
							
			break;
			
			//******************************************************************************************
			
			case ConstantesBD.codigoReporteMotivoCancelacionCitaMes:
				
				//Validacion del rango de fechas
				if(mapa.get("estadoFechaInicial").toString().equals(ConstantesIntegridadDominio.acronimoEstadoRequerido))
					errores = metodoValidacionRangoFechas(mapa,12, errores);
						
			break;
			
			//******************************************************************************************

			default:
				errores.add("descripcion",new ActionMessage("errors.notEspecific","No se encontro validación para el reporte"));
			break;
		}
		
		return errores;
	}
	
	
	//*****************************************************************************************************
	
	/**
	 * Metodo para la validación de las fechas 
	 * @param HashMap mapa
	 * @param int numeroMeses
	 * @param ActionErrors errores
	 * */
	public static ActionErrors metodoValidacionRangoFechas(HashMap mapa,int numeroMeses, ActionErrors errores)
	{
		//validación de la fecha y hora de ingreso a la sala 
		if(mapa.get("fechaInicial").toString().equals("") || mapa.get("fechaFinal").toString().equals(""))
		{
			errores.add("descripcion",new ActionMessage("errors.required","La Fecha Inicial y Fecha Final " ));			
			return errores;
		}	
				
		//Validación del formato de la fecha Inicial
		if(!UtilidadFecha.validarFecha(mapa.get("fechaInicial").toString()))
		{
			errores.add("descripcion",new ActionMessage("errors.formatoFechaInvalido"," Inicial "+mapa.get("fechaInicial").toString()));
			return errores;
		}
		
		//Validación del formato de la fecha Final
		if(!UtilidadFecha.validarFecha(mapa.get("fechaFinal").toString()))
		{
			errores.add("descripcion",new ActionMessage("errors.formatoFechaInvalido"," Inicial "+mapa.get("fechaFinal").toString()));
			return errores;
		}
		
		//Validación de fechas con la fecha actual 
		if(!UtilidadFecha.compararFechas(
				UtilidadFecha.getFechaActual(),
				UtilidadFecha.getHoraActual(),
				mapa.get("fechaFinal").toString(),
				UtilidadFecha.getHoraActual()).isTrue())
		{
			errores.add("descripcion",new ActionMessage("errors.fechaPosteriorIgualActual"," Final "+mapa.get("fechaFinal").toString()," Actual "+UtilidadFecha.getFechaActual()));
			return errores;
		}
		else
		{
			if(!UtilidadFecha.compararFechas(					
					mapa.get("fechaFinal").toString(),
					UtilidadFecha.getHoraActual(),
					mapa.get("fechaInicial").toString(),
					UtilidadFecha.getHoraActual()
					).isTrue())
			{
				errores.add("descripcion",new ActionMessage("errors.fechaPosteriorIgualActual"," Inicial "+mapa.get("fechaInicial").toString()," Final "+mapa.get("fechaFinal").toString()));
				return errores;
			}			
		}
		
		//Validacion para el rango de fechas
		if(UtilidadFecha.numeroMesesEntreFechasExacta(mapa.get("fechaInicial").toString(), mapa.get("fechaFinal").toString()) > numeroMeses)
			errores.add("descripcion",new ActionMessage("errors.notEspecific","El Rango permitido entre la Fecha Inicial y Final es de "+numeroMeses+" meses"));		
		
		return errores;								
	}
		
	//*****************************************************************************************************
	
	/**
	 * Genera el reporte
	 * @param Connection con
	 * @param int codigoReporte
	 * @param HashMap mapa 
	 * @param InstitucionBasica ins
	 * @param ActionErrors errors
	 * @param UsuarioBasico usuario
	 * */
	public static HashMap metodoGeneracionReporte(
			Connection con,
			int codigoReporte,
			HashMap mapa,
			InstitucionBasica ins,
			ActionErrors errores,
			UsuarioBasico usuario)
	{
		HashMap condicionesWhere;		
		HashMap result = new HashMap();
    			
		String viaIngreso="";
		String tipoPaciente="";
		Vector v = new Vector();
		
		//Parametros para la busqueda ***
        condicionesWhere = getCondicionesBuquedaReporte(codigoReporte,mapa);
        String nombreRptDesign = condicionesWhere.get("nombrearchivoreporte").toString();
		
		//***************** INFORMACIÓN DEL CABEZOTE
        DesignEngineApi comp; 
        comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"manejoPaciente/",nombreRptDesign);
         
        // Logo
        comp.insertImageHeaderOfMasterPage1(0, 2, ins.getLogoReportes());
        comp.insertGridHeaderOfMasterPage(0,1,1,4);
        v.add(ins.getRazonSocial());
        if(Utilidades.convertirAEntero(ins.getDigitoVerificacion()) != ConstantesBD.codigoNuncaValido)
        	v.add(Utilidades.getDescripcionTipoIdentificacion(con,ins.getTipoIdentificacion())+". "+ins.getNit()+" - "+ins.getDigitoVerificacion());
        else
        	v.add(Utilidades.getDescripcionTipoIdentificacion(con,ins.getTipoIdentificacion())+". "+ins.getNit());
        v.add(ins.getDireccion());
        v.add("Tels. "+ins.getTelefono());
        comp.insertLabelInGridOfMasterPage(0, 1, v);
        
        //Información del reporte
        comp.insertLabelInGridPpalOfHeader(1,0,mapa.get("nombreReporte").toString().toUpperCase());
              
        //Parametros de Busqueda
        comp.insertLabelInGridPpalOfHeader(3,0,"Parámetros de Búsqueda: "+condicionesWhere.get("parametrosbusqueda"));
        
        //Usuario        
        comp.insertLabelInGridPpalOfFooter(0,1,"Usuario: "+usuario.getLoginUsuario());
    	
        //Evalua el numero de dataset
        String newquery = "";
        if(!condicionesWhere.containsKey("numdataset"))
        {
        	comp.obtenerComponentesDataSet(condicionesWhere.get("dataset").toString());
            newquery=comp.obtenerQueryDataSet().replaceAll("1=2",condicionesWhere.get("where").toString());
            comp.modificarQueryDataSet(newquery);
        }
        else
        {
        	for(int i = 0; i < Utilidades.convertirAEntero(condicionesWhere.get("numdataset").toString()); i++)
        	{
        		comp.obtenerComponentesDataSet(condicionesWhere.get("dataset"+i).toString());
        		
        		if(condicionesWhere.containsKey("reemplazarTodo") 
        				&& condicionesWhere.get("reemplazarTodo").toString().equals(ConstantesBD.acronimoSi))        	
        		{
        	        comp.modificarQueryDataSet(condicionesWhere.get("where"+i).toString());
        		}
        		else
        		{
        			newquery = comp.obtenerQueryDataSet().replaceAll("1=2",condicionesWhere.get("where"+i).toString());
        			comp.modificarQueryDataSet(newquery);
        		}
        	}
        } 	          
        
        //debemos arreglar los alias para que funcione oracle, debido a que este los convierte a MAYUSCULAS
      	comp.lowerAliasDataSet();
		String newPathReport = comp.saveReport1(false);	
        comp.updateJDBCParameters(newPathReport);
	   
        result.put("descripcion",newPathReport);
        result.put("resultado",true);
        result.put("urlArchivoPlano","");
		result.put("pathArchivoPlano","");
        
        //la salida del archivo es cvs		
        if(mapa.get("formatoSalida").toString().equals(ConstantesIntegridadDominio.acronimoArchivoPlano))
        {        	       	
        	
        	ResultadoBoolean resultado = ExtractCSV.extraerArchivoCsvComprimido(
        			newPathReport,
        			UtilidadTexto.cambiarCaracteresEspeciales(mapa.get("nombreReporte").toString().trim().replace(" ","")));
        	if(resultado.isTrue())
        	{
        		//Se toman las rutas
        		String[] rutas = resultado.getDescripcion().split(ConstantesBD.separadorSplit);
        		result.put("urlArchivoPlano",rutas[0]);
        		result.put("pathArchivoPlano",rutas[1]);
        	}
        	else        	
        		result.put("resultado",false);       
        }  
	   
        Utilidades.imprimirMapa(condicionesWhere);
	          		
        return result;		
	}
		
	//*****************************************************************************************************
	
	/**
	 * Arma la sentencia del Where a partir de los filtros de la busqueda
	 * @param int codigoReporte
	 * @param HashMap parametros
	 * */
	public static HashMap getCondicionesBuquedaReporte(int codigoReporte, HashMap parametros)
	{
		parametros.put("codigoReporte",codigoReporte);
		
		//Añade un nuevo parametro de especialidades separadas por coma
		if(parametros.containsKey("estructuraEspecialidad"))
		{
			InfoDatosString respuesta = metodoDatosPorComa((HashMap)parametros.get("estructuraEspecialidad"));
			parametros.put("codigoEspecialidad",respuesta.getDescripcion());
			parametros.put("nombreEspecialidad",respuesta.getNombre());
		}
		
		//Añade un nuevo parametro de Motivos de Cancelación separados por coma
		if(parametros.containsKey("estructuraMotivoCancelacion"))
		{
			InfoDatosString respuesta = metodoDatosPorComa((HashMap)parametros.get("estructuraMotivoCancelacion"));
			parametros.put("codigoMotivoCancelacion",respuesta.getDescripcion());
			parametros.put("nombreMotivoCancelacion",respuesta.getNombre());
		}
		
		//Añade un nuevo parametro de Motivos de Cancelación separados por coma
		if(parametros.containsKey("estructuraProfesional"))
		{
			InfoDatosString respuesta = metodoDatosPorComa((HashMap)parametros.get("estructuraProfesional"));
			parametros.put("codigoProfesionalSalud",respuesta.getDescripcion());
			parametros.put("nombreProfesionalSalud",respuesta.getNombre());
		}
		
		Utilidades.imprimirMapa(parametros);		
		return getReporteEstadisticoConsultaExDao().getCondicionesBuquedaReporte(parametros);	
	}
	
	//*****************************************************************************************************
	
	/**
	 * @param HashMap Datos
	 * */
	public static InfoDatosString metodoDatosPorComa(HashMap estructura)
	{ 
		InfoDatosString respuesta = new InfoDatosString();
		String codigos = "",nombres = "";
		int numRegistros = Utilidades.convertirAEntero(estructura.get("numRegistros").toString());
		
		for(int i = 0; i < numRegistros; i++)
		{
			if(estructura.get("activo_"+i).toString().equals(ConstantesBD.acronimoSi))
			{
				codigos += estructura.get("codigo_"+i).toString()+",";
				nombres += estructura.get("descripcion_"+i).toString()+",";
			}
		}
				
		if(codigos.endsWith(","))
		{
			codigos = codigos.substring(0,codigos.length()-1);
			nombres = nombres.substring(0,nombres.length()-1);
		}
				
		respuesta.setDescripcion(codigos);
		respuesta.setNombre(nombres);
		return respuesta;
	}
	
	//*****************************************************************************************************
	
	/**
	 * @param Connection con
	 * @param String where
	 * @param boolean cargarProfesionales
	 * */
	public static HashMap getCargarDatosBasicosCancelacionCitas(Connection con, String where,String where0,boolean cargarProfesionales) 
	{
		HashMap parametros = new HashMap();
		parametros.put("where",where);
		parametros.put("where0",where0);
		parametros.put("conProfesionales",cargarProfesionales?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
		return getReporteEstadisticoConsultaExDao().getCargarDatosBasicosCancelacionCitas(con,parametros);	
	}	
	//*****************************************************************************************************
}