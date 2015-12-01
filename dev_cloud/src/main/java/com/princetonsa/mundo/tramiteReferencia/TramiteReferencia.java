package com.princetonsa.mundo.tramiteReferencia;

import java.sql.Connection;
import java.util.GregorianCalendar;
import java.util.HashMap;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.sqlbase.tramiteReferencia.SqlBaseTramiteReferenciaDao;
import com.princetonsa.dao.tramiteReferencia.TramiteReferenciaDao;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.historiaClinica.Referencia;
import org.apache.log4j.Logger;

import util.ValoresPorDefecto;
import util.ConstantesBD;

import util.UtilidadFecha;
import util.historiaClinica.UtilidadesHistoriaClinica;


/**
 * @author Jose Eduardo Arias Doncel
 * jearias@princetonsa.com
 * */
public class TramiteReferencia
{
	//------------------------Atributos
	
	private static TramiteReferenciaDao tramiteReferenciaDao;
	private static Logger logger = Logger.getLogger(TramiteReferencia.class); 
		
	//------------------------Fin Atributos
	
	//------------------------Metodos
	
	
	/**
	 * Constructor de la clase
	 * */
	public void TramiteReferencia()
	{
		DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));		
		tramiteReferenciaDao = myFactory.getTramiteReferenciaDao();
	}
	
	/**
	 * Instancia el DAO
	 * */
	public static TramiteReferenciaDao tramiteReferenciaDao()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getTramiteReferenciaDao();
	}
	
	
	/**
	 * Consultar Tramite
	 * @param Connection 
	 * @param HashMap parametrosBusqueda (institucion,referencia,estado1,estado2)
	 * */
	public static HashMap consultarListadoReferencia(Connection con, HashMap parametrosBusqueda)
	{	
		UsuarioBasico usuario = new UsuarioBasico();
		HashMap mapa = new HashMap();		
					
		mapa = tramiteReferenciaDao().consultarListadoReferencia(con, parametrosBusqueda);	
		
		for(int i=0; i<Integer.parseInt(mapa.get("numRegistros").toString());i++)
		{
			mapa.put("tiempoespera_"+i,calcularTiempo(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()),UtilidadFecha.getHoraActual(),mapa.get("fechareferencia_"+i).toString(),mapa.get("horareferencia_"+i).toString()));
			mapa.put("fechareferencia_"+i,UtilidadFecha.conversionFormatoFechaAAp(mapa.get("fechareferencia_"+i).toString()));
			mapa.put("tiporeferencianom_"+i,ValoresPorDefecto.getIntegridadDominio(mapa.get("tiporeferencia_"+i).toString()));
			mapa.put("tipoatencionnom_"+i,ValoresPorDefecto.getIntegridadDominio(mapa.get("tipoatencion_"+i).toString()));
		}				
		return mapa;
	}		

	
		
	/**
	 * Calcula la direferencia entre dos fechas y devuelve el valor en hh:mm
	 * @param String fechaMayor
	 * @param String horaMayor
	 * @param String fechaMayor
	 * @param String horaMayor 
	 * */	
	public static String calcularTiempo(String fechaMayor, String horaMayor, String fechaMenor, String horaMenor)
	{
		GregorianCalendar calendario=new GregorianCalendar();
		
		String horasSirc="";
		String minutosSirc="";		
		
		long fechaInMillisMayor;
		long fechaInMillisMenor;		
		long segundos;
		long minutos;
		long horas;				
		
		calendario.set(Integer.parseInt(fechaMayor.split("-")[0]),Integer.parseInt(fechaMayor.split("-")[1]),Integer.parseInt(fechaMayor.split("-")[2]),Integer.parseInt(horaMayor.split(":")[0]),Integer.parseInt(horaMayor.split(":")[1]));		
		fechaInMillisMayor = calendario.getTimeInMillis();		
		calendario.set(Integer.parseInt(fechaMenor.split("-")[0]),Integer.parseInt(fechaMenor.split("-")[1]),Integer.parseInt(fechaMenor.split("-")[2]),Integer.parseInt(horaMenor.split(":")[0]),Integer.parseInt(horaMenor.split(":")[1]));	
		fechaInMillisMenor = calendario.getTimeInMillis();
		
		segundos = (fechaInMillisMayor - fechaInMillisMenor) / 1000;		
		horas = segundos / 3600 ;
		segundos -= horas*3600; // se calcula de nuevo debido a la perdida de decimales		
		minutos  = segundos / 60;
				
		if(horas<10)
			horasSirc = "0";		
		if(minutos<10)
			minutosSirc = "0";		
		
		return horasSirc+""+horas+":"+minutosSirc+""+minutos;	
	}
	
	/**
	 * Administra la cadena de codigos insertados desde la busqueda avanzada
	 * @param String cadena 
	 * @param int operacion
	 * @param String cadenaOperacion
	 * */
	public String CodigosInsertados(String cadena, int operacion, String cadenaOperacion)
	{
		if(!cadenaOperacion.equals(""))
		{
			if(operacion == 1) //adiccionar		
				cadena+=cadenaOperacion+",";	
		
			else if(operacion == 2) //eliminar
			{	
				if(cadena.equals(cadenaOperacion+","))
					cadena="";
				else	
					cadena = cadena.replaceAll(cadenaOperacion+",","");				
			}
		}		
		return cadena;
	}
	
	/**
	 * Metodo llamado consulta de la referencia
	 * @param Connection con
	 * @param int codigoPaciente
	 * @param idIngreso
	 * @param String tipoReferencia
	 * */
	public static HashMap cargarReferencia(Connection con, int codigoPaciente, int idIngreso, String tipoReferencia)
	{
		Referencia referencia = new Referencia();	
		return referencia.cargar(con, codigoPaciente, idIngreso, tipoReferencia,"");		
	}	
	
	
	/**
	 * verifica si el registro se ha modificado
	 * @param con
	 * @param HashMap mapaEvaluar 
 	 * @param HashMap parametros
	 * @param int pos
	 * @param int opcion
	 * */
	public static boolean existeModificacion(Connection con,HashMap mapaEvaluar, HashMap parametros, String pos, int opcion)
	{
		HashMap temp = new HashMap();		 
		String[] indices = (String[])mapaEvaluar.get("INDICES_MAPA");
		String indicador="0";		
		
		switch (opcion) 
		{			
			//Tramite Referencia
			case 1: 
				temp = consultarTramiteReferencia(con,Integer.parseInt(parametros.get("numeroreferenciatramite").toString()));				
				indicador=""; //la consulta devuelve un mapa indices sin _, no existe posicion 
				pos="";
			break;	
			
			//Instituciones Tramite Referencia
			case 2:
				temp = consultarInstitucionesTramiteReferencia(con, parametros);				
				indicador="0"; //la consulta devuelve un mapa indices con _, existe posicion
			break;	
			
			//Servicios Instituciones Referencia
			case 3:
				temp = consultarServiciosInstitucionesReferencia(con, parametros);				
				indicador="0";//la consulta devuelve un mapa indices con _, existe posicion
			break;
			
			//Traslado Paciente
			case 4:
				temp = consultarTrasladoPaciente(con, parametros);
				indicador="0";
			break;
		}	 		
						
		
		for(int i=0;i<indices.length;i++)
		{			
			if(temp.containsKey(indices[i]+indicador)&&mapaEvaluar.containsKey(indices[i].split("_")[0]+pos))
			{			
				if(!((temp.get(indices[i]+indicador)+"").trim().equals((mapaEvaluar.get(indices[i].split("_")[0]+pos)).toString().trim())))
				{					
					return true;
				}				
			}
		}	
		return false;		
	}
	
	/**
	 * Verifica si existe un dato en un HashMap
	 * @param String value valor a buscar
	 * @param String key, llave a buscar
	 * @param HashMap mapa
	 * @param int tamano, tamaño del mapa
	 * */
	public static int existeDato(String value, String key, HashMap mapa, int tamano)
	{
		if(tamano > 0)
		{
			for(int i=0;i<tamano;i++)
			{				
				if(mapa.get(key+""+i).toString().equals(value))
					return i;			
			}			
		}
		
		return ConstantesBD.codigoNuncaValido;
	}
	
	/**
	 * Carga los valores de Motivo SIRC 
	 * @param Connection con
	 * @param int institucion
	 * @param String tipoMotivo
	 * @param String activo
	 *  **/
	public static HashMap cargarMotivosSirc(Connection con, int institucion, String tipoMotivo, String activo)
	{
		return UtilidadesHistoriaClinica.obtenerMotivosSirc(con,institucion,tipoMotivo,activo);		
	}
	/***************************************************************
	/****************************Grupo de Metodos Tramite Referencia
	 
	 
	/**
	 * Consultar Tramite Referencia
	 * @param Connection 
	 * @param int numero de referencia tramite  
	 * */
	public static HashMap consultarTramiteReferencia(Connection con, int numeroReferenciaTramite)
	{
		return tramiteReferenciaDao().consultarTramiteReferencia(con, numeroReferenciaTramite);
	}
	
	/**
	 * Actualiza Tramite Referencia
	 * @param Connection 
	 * @param HashMap tramiteReferencia  
	 * */
	public static boolean actualizarTramiteReferencia(Connection con, HashMap tramiteReferencia)
	{
		return tramiteReferenciaDao().actualizarTramiteReferencia(con, tramiteReferencia);		
	}
	
	/**
	 * Inserta un registro en la tabla tramite referencia 
	 * Connection con
	 * HashMap tramiteReferencia 
	 * */
	public static boolean insertarTramiteReferencia(Connection con, HashMap tramiteReferencia)
	{
		return tramiteReferenciaDao().insertarTramiteReferencia(con, tramiteReferencia);
	}
	
	
	/*****************************************************************************
	/****************************Grupo de Metodos Instituciones Tramite Referencia
	
	/**
	 * Consulta instituciones tramite referencia
	 * @param Connection con
	 * @param HashMap parametros	 
	 * */
	public static HashMap consultarInstitucionesTramiteReferencia(Connection con, HashMap parametros)
	{
		HashMap mapa = new HashMap();
		mapa = tramiteReferenciaDao().consultarInstitucionesTramiteReferencia(con, parametros);
		String cadena="";
		
		for(int i=0; i<Integer.parseInt(mapa.get("numRegistros").toString());i++)
			cadena+="'"+mapa.get("institucionreferir_"+i)+"',";
		
		mapa.put("codigosInstitucionesInsertados",cadena);
		return mapa;
	}
	
	/**
	 * Actualiza un registro en instituciones tramite referencia 
	 * @param Connection con
	 * @param HashMap Parametros 
	 * */
	public static  boolean actualizarInstitucionesTramiteReferencia(Connection con, HashMap parametros)
	{
		return tramiteReferenciaDao().actualizarInstitucionesTramiteReferencia(con, parametros);
	}
	
	
	/**
	 * Inserta un registro en instituciones tramite referencia
	 * @param Connection 
	 * @param HashMap parametros
	 * */
	public static boolean insertarInstitucionesTramiteReferencia(Connection con, HashMap parametros)
	{
		return tramiteReferenciaDao().insertarInstitucionesTramiteReferencia(con, parametros);		
	}
		
	/*******************************************************************************
	/****************************Grupo de Funciones Servicios Institucion Referencia
	 
	 /**
	  * Consultar Servicios Instituciones Referencia 
	  * @param Connection con
	  * @param HashMap parametros
	  * */
	public static HashMap consultarServiciosInstitucionesReferencia(Connection con, HashMap parametros)
	{		
		return tramiteReferenciaDao().consultarServiciosInstitucionesReferencia(con, parametros);	
	}
	
	/**
	 * Actualizar Servicios Instituciones Referencia
	 * @param Connection con
	 * @param HashMap parametros 
	 * */
	public static boolean actualizarServiciosInstitucionesReferencia(Connection con, HashMap parametros)
	{
		return tramiteReferenciaDao().actualizarServiciosInstitucionesReferencia(con, parametros);
	}
	
	/**
	 * Insertar Servicios Instituciones Referencia
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public static boolean insertarServiciosInstitucionesReferencia(Connection con, HashMap parametros)
	{
		return tramiteReferenciaDao().insertarServiciosInstitucionesReferencia(con, parametros);	
	}	
	
	
	/**
	  * Consultar Historial Servicios Instituciones Referencia 
	 * @param numeroReferencia 
	  * @param Connection con
	  * @param HashMap parametros
	  * */
	public static HashMap consultarHistorialServiciosInstitucionesReferencia(Connection con, String codigoInstitucionSirc,int codigoInstitucion, String numeroReferencia)
	{
		HashMap parametros = new HashMap();
		parametros.put("institucionSIRC", codigoInstitucionSirc+"");
		parametros.put("institucion", codigoInstitucion+"");
		parametros.put("numeroReferencia", numeroReferencia+"");
		
		return tramiteReferenciaDao().consultarHistorialServiciosInstitucionesReferencia(con, parametros);			
	}
	
	/*******************************************************************************
	/****************************Grupo de Funciones Traslado Paciente
	
	/**
	  * Consultar Traslado Paciente 
	  * @param Connection con
	  * @param HashMap parametros
	  * */
	public static HashMap consultarTrasladoPaciente(Connection con, HashMap parametros)
	{
		HashMap mapa = new HashMap();
		mapa = tramiteReferenciaDao().consultarTrasladoPaciente(con, parametros);
		String cadena="";
		
		for(int i=0; i<Integer.parseInt(mapa.get("numRegistros").toString());i++)
			cadena+="'"+mapa.get("institucionsirc_"+i)+"',";
		
		mapa.put("codigosInsTransladosInsertados",cadena);
		return mapa;
		
	}
	
	/**
	 * Actualizar Traslado Paciente 
	 * @param Connection con
	 * @param HashMap parametros 
	 * */
	public static boolean actualizarTrasladoPaciente(Connection con, HashMap parametros)
	{
		return tramiteReferenciaDao().actualizarTrasladoPaciente(con, parametros);
	}
	
	/**
	 * Insertar Traslado Paciente
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public static boolean insertarTrasladoPaciente(Connection con, HashMap parametros)
	{
		return tramiteReferenciaDao().insertarTrasladoPaciente(con, parametros);
	}
	
	/**
	  * Consultar Historial Traslado Paciente 
	  * @param Connection con
	  * @param int numeroreferenciatramite
	  * @param int institucionsirc
	  * @param int institucion
	  * */
	public  static HashMap consultarHistorialTrasladoPaciente(Connection con, int numeroreferenciatramite, String institucionsirc, int institucion)
	{
		return tramiteReferenciaDao().consultarHistorialTrasladoPaciente(con,numeroreferenciatramite,institucionsirc, institucion); 
	}
	
	//------------------------Fin Metodos
}