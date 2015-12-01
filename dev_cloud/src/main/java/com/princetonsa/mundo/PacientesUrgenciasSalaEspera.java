/*
 * @(#)PacientesUrgenciasSalaEspera.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_01
 *
 */
package com.princetonsa.mundo;

import java.sql.Connection;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import org.apache.log4j.Logger;
import util.UtilidadFecha;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.PacientesUrgenciasSalaEsperaDao;

public class PacientesUrgenciasSalaEspera 
{
	 /**
	 * DAO utilizado por el objeto para acceder a la fuente de datos
	 */
	private static PacientesUrgenciasSalaEsperaDao pacientesUrgenciasSalaEsperaDao = null;
	
		/**
	 * Para hacer logs de debug / warn / error de esta funcionalidad.
	 */
	private Logger logger = Logger.getLogger(PacientesUrgenciasSalaEspera.class);

	/**
	 * Inicializa el acceso a bases de datos de este objeto, obteniendo su respectivo DAO.
	 * @param tipoBD el tipo de base de datos que va a usar este objeto
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores válidos para tipoBD
	 * son los nombres y constantes definidos en <code>DaoFactory</code>.
	 * @return <b>true</b> si la inicialización fue exitosa, <code>false</code> si no.
	 */
	public boolean init(String tipoBD)
	{
		boolean wasInited = false;
		DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
	
		if (myFactory != null)
		{
			pacientesUrgenciasSalaEsperaDao = myFactory.getPacientesUrgenciasSalaEsperaDao();
			wasInited = (pacientesUrgenciasSalaEsperaDao != null);
		}
		return wasInited;
	}
	
	/**
	 * Constructor vacio de la clase
	 *
	 */
	public PacientesUrgenciasSalaEspera()
	{
	    this.init (System.getProperty("TIPOBD"));
	}
	
	/**
	 * Método para consultar los datos necesarios para saber los pacientes de via de urgencias
	 * a los que se les definio en conducta a seguir "Sala de Espera"
	 * @param con
	 * @param codigoCentroAtencion
	 * @param institucion
	 * @return
	 */
	public HashMap consultarPacientesUrgSalaEspera(Connection con, int codigoCentroAtencion, int institucion)
	{
		ResultSetDecorator rs = null;
		HashMap mapaPacientes = new HashMap();
		mapaPacientes.put("numRegistros", "0");
		String tiempoEspera = "";
		String[] temp;
		try
		{
			rs = pacientesUrgenciasSalaEsperaDao.consultarPacientesUrgSalaEspera(con, codigoCentroAtencion, institucion);
			int i = 0;
			while(rs.next())
			{
				
				mapaPacientes.put("codigoadmision_"+i, rs.getInt("codigoadmision"));
				mapaPacientes.put("fechaadmision_"+i, rs.getString("fechaadmision"));
				temp = rs.getString("horaadmision").split(":");
				mapaPacientes.put("horaadmision_"+i, temp[0]+":"+temp[1]);
				mapaPacientes.put("fechahoraadmision_"+i, rs.getString("fechahoraadmision"));
				mapaPacientes.put("codigopaciente_"+i, rs.getInt("codigopaciente"));
				mapaPacientes.put("idcuenta_"+i, rs.getInt("idcuenta"));
				mapaPacientes.put("nombrepaciente_"+i, rs.getString("nombrepaciente"));
				mapaPacientes.put("tipoid_"+i, rs.getString("tipoid"));
				mapaPacientes.put("numeroid_"+i, rs.getString("numeroid"));
				mapaPacientes.put("calificaciontriage_"+i, rs.getString("calificaciontriage"));
				mapaPacientes.put("nombrecolor_"+i, rs.getString("nombrecolor"));

				//69474
				mapaPacientes.put("descripcion_"+i, rs.getString("descripcion"));

				//134568
				mapaPacientes.put("nombremedico_"+i, rs.getString("nombremedico"));

				mapaPacientes.put("tiemposalaespera_"+i, rs.getString("tiemposalaespera"));
				
				//Calculamos el tiempo que lleva el paciente en sala de espera
				//tiempoEspera = this.tiempoEspera(rs.getString("fechaadmisionapp") , temp[0]+":"+temp[1] , UtilidadFecha.getFechaActual(), UtilidadFecha.getHoraActual());
				//mapaPacientes.put("tiemposalaespera_"+i, tiempoEspera);
				//logger.info(mapaPacientes.get("tiemposalaespera_"+i)+"  --  "+tiempoEspera);
				i++;
			}
			mapaPacientes.put("numRegistros", i);
			return mapaPacientes;
		}
		catch(SQLException e)
		{
			logger.error("Error consultarPacientesUrgenciasSalaEspera [consultarPacientesUrgenciasSalaEspera.java]"+e+"\n\n");
			e.printStackTrace();
		}
		return mapaPacientes;
	}
	
	/**
	 * Método para consultar los datos necesarios para saber los pacientes de via de urgencias
	 * a los que se les definio en conducta a seguir "Sala de Espera"
	 * @param con
	 * @param codigoCentroAtencion
	 * @param institucion
	 * @return
	 */
	public HashMap consultarPacientesUrgSalaEsperaOrdenamiento(Connection con, int codigoCentroAtencion, int institucion,Integer orderBy)
	{
		ResultSetDecorator rs = null;
		HashMap mapaPacientes = new HashMap();
		mapaPacientes.put("numRegistros", "0");
		String tiempoEspera = "";
		String[] temp;
		try
		{
			rs = pacientesUrgenciasSalaEsperaDao.consultarPacientesUrgSalaEsperaOrdenamientoPorTiempoSalaEspera(con, codigoCentroAtencion, institucion,orderBy);
			int i = 0;
			while(rs.next())
			{
				
				mapaPacientes.put("codigoadmision_"+i, rs.getInt("codigoadmision"));
				mapaPacientes.put("fechaadmision_"+i, rs.getString("fechaadmision"));
				temp = rs.getString("horaadmision").split(":");
				mapaPacientes.put("horaadmision_"+i, temp[0]+":"+temp[1]);
				mapaPacientes.put("fechahoraadmision_"+i, rs.getString("fechahoraadmision"));
				mapaPacientes.put("codigopaciente_"+i, rs.getInt("codigopaciente"));
				mapaPacientes.put("idcuenta_"+i, rs.getInt("idcuenta"));
				mapaPacientes.put("nombrepaciente_"+i, rs.getString("nombrepaciente"));
				mapaPacientes.put("tipoid_"+i, rs.getString("tipoid"));
				mapaPacientes.put("numeroid_"+i, rs.getString("numeroid"));
				mapaPacientes.put("calificaciontriage_"+i, rs.getString("calificaciontriage"));
				mapaPacientes.put("nombrecolor_"+i, rs.getString("nombrecolor"));

				//69474
				mapaPacientes.put("descripcion_"+i, rs.getString("descripcion"));

				//134568
				mapaPacientes.put("nombremedico_"+i, rs.getString("nombremedico"));

				mapaPacientes.put("tiemposalaespera_"+i, rs.getString("tiemposalaespera"));
				
				//Calculamos el tiempo que lleva el paciente en sala de espera
				//tiempoEspera = this.tiempoEspera(rs.getString("fechaadmisionapp") , temp[0]+":"+temp[1] , UtilidadFecha.getFechaActual(), UtilidadFecha.getHoraActual());
				//mapaPacientes.put("tiemposalaespera_"+i, tiempoEspera);
				//logger.info(mapaPacientes.get("tiemposalaespera_"+i)+"  --  "+tiempoEspera);
				i++;
			}
			mapaPacientes.put("numRegistros", i);
			return mapaPacientes;
		}
		catch(SQLException e)
		{
			logger.error("Error consultarPacientesUrgenciasSalaEspera [consultarPacientesUrgenciasSalaEspera.java]"+e+"\n\n");
			e.printStackTrace();
		}
		return mapaPacientes;
	}
	
	/**
	 * Método para calcular el tiempo de espera del paciente
	 * desde su hora de llegada
	 * @param horaIni
	 * @param horaFin
	 * @return
	 */
	private String tiempoEspera(String fechaInicial, String horaIni, String fechaFin, String horaFin)
	{
		String duracionFinal = "";
		if(fechaInicial.trim()!="" && horaIni.trim()!="" && fechaFin.trim()!="" && horaFin.trim()!="")
		{
			duracionFinal = calcularDuracionEntreFechas(fechaInicial.trim(), horaIni, fechaFin, horaFin);
		}
		return duracionFinal;
	 }
	
	/**
	 * Método para clacular la duracion exacta entre dos horas determinadas evaluando
	 * las fechas de cada hora
	 * @param fechaInicial
	 * @param horaInicial
	 * @param fechaFinal
	 * @param horaFinal
	 * @return
	 */
	private String calcularDuracionEntreFechas(String fechaInicial, String horaInicial, String fechaFinal, String horaFinal)
	{
		logger.info("calcularDuracionEntreFechas--> fi:"+fechaInicial+" hi:"+horaInicial+" ff:"+fechaFinal+" hf:"+horaFinal);
		
		String[] fechaIni = fechaInicial.split("/");
		String[] fechaFin = fechaFinal.split("/");
		String[] horaIni = horaInicial.split(":");
		String[] horaFin = horaFinal.split(":");
		
		/*logger.info("La fecha inicial-->"+fechaInicial);
		logger.info("La fecha final-->"+fechaFinal);
		logger.info("La hora inicial-->"+horaInicial);
		logger.info("La hora final-->"+horaFinal);
		logger.info("Entro a calcular la duracion");
		*/
		// Los meses en Java empiezan en 0
		int mesJava1 = Integer.parseInt(fechaIni[1]) - 1;
		int mesJava2 = Integer.parseInt(fechaFin[1]) - 1; 
		int nroHoras = 0;
		String tiempoEspera ="";
		String temp = "";

		// Fecha Inicial
		GregorianCalendar calendarioFechaIni = new GregorianCalendar();
		calendarioFechaIni.clear();
		calendarioFechaIni.set(Integer.parseInt(fechaIni[2]), mesJava1, Integer.parseInt(fechaIni[0]), Integer.parseInt(horaIni[0]), Integer.parseInt(horaIni[1]));
		Date fechaInicialDate = calendarioFechaIni.getTime();
		
		logger.info("\n La fecha inicial date->"+fechaInicialDate);

		//Fecha Final
		GregorianCalendar calendarioFechaFin = new GregorianCalendar();
		calendarioFechaFin.clear();
		calendarioFechaFin.set(Integer.parseInt(fechaFin[2]), mesJava2, Integer.parseInt(fechaFin[0]), Integer.parseInt(horaFin[0]), Integer.parseInt(horaFin[1]));
		Date fechaFinalDate = calendarioFechaFin.getTime();
		
		
		logger.info("La fecha finaldate->"+fechaFinalDate);
		
		if(fechaFinalDate.compareTo(fechaInicialDate) < 0)
		{
			logger.info("Entro cuando la inicial es menor que la inicial");
			//Esto pasa si la fecha Inicial es mayor que la Final
			return tiempoEspera;
		}
		while(fechaInicialDate.compareTo(fechaFinalDate) < 0)
		{
			//Aumentamos el número de horas
			nroHoras++;
			calendarioFechaIni.add(Calendar.HOUR, 1);
			fechaInicialDate = calendarioFechaIni.getTime();
		}
		//Si son iguales tengo la hora exacta
		if(fechaInicialDate.compareTo(fechaFinalDate) == 0)
		{
			tiempoEspera = nroHoras+":00";
		}
		else
		{
			calendarioFechaIni.add(Calendar.HOUR, -1);
			fechaInicialDate = calendarioFechaIni.getTime();
			int nroMinutos = 0;
			while(fechaInicialDate.compareTo(fechaFinalDate) < 0)
			{
				//Aumentamos el número de minutoss
				nroMinutos++;
				calendarioFechaIni.add(Calendar.MINUTE, 1);
				fechaInicialDate = calendarioFechaIni.getTime();
			}
			nroHoras = nroHoras -1;
			if(nroHoras < 10 && nroMinutos < 10)
			{
				temp = "0"+nroHoras+":0"+nroMinutos;
			}
			if(nroHoras < 10 && nroMinutos > 10)
			{
				temp = "0"+nroHoras+":"+nroMinutos;
			}
			if(nroHoras > 10 && nroMinutos < 10)
			{
				temp = nroHoras+":0"+nroMinutos;
			}
			if(nroHoras > 10 && nroMinutos > 10)
			{
				temp = nroHoras+":"+nroMinutos;
			}
			tiempoEspera = temp;
		}
		return tiempoEspera;
	}
	
	
}