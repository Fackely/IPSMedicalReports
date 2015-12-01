/*
 * @(#)ConsultaPacientesTriage.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_04
 *
 */

package com.princetonsa.mundo.triage;


import java.sql.Connection;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import org.apache.log4j.Logger;
import util.ConstantesBD;
import util.UtilidadFecha;
import com.princetonsa.dao.ConsultaPacientesTriageDao;
import com.princetonsa.dao.DaoFactory;

/**
 * Objeto que maneja la interacción entre la capa
 * de control y el acceso a la información de 
 * Sistemas Motivo de COnsulta de Urgencias
 * @author <a href="mailto:cperalta@PrincetonSA.com">Carlos Peralta</a>
 *	@version 1.0, 31 /May/ 2006
 */
public class ConsultaPacientesTriage
{
	
	/**
	 * Para hacer logs de debug / warn / error de esta funcionalidad.
	 */
	private Logger logger = Logger.getLogger(ConsultaPacientesTriage.class);
	
	
    /**
     * Constructor del objeto
     * (Solo inicializa el acceso a la 
     * fuente de datos)
     */
    public ConsultaPacientesTriage()
    {
        this.init(System.getProperty("TIPOBD"));
    }
    
    /**
	 * El DAO usado por el objeto <code>ConsultaPacientesTriageDao</code> 
	 * para acceder a la fuente de datos. 
	 */
	private ConsultaPacientesTriageDao consultaPacientesTriageDao ;

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
			consultaPacientesTriageDao = myFactory.getConsultaPacientesTriageDao();
			wasInited = (consultaPacientesTriageDao != null);
		}

		return wasInited;
	}
	
	
	public HashMap consultarPacientesTriage(Connection con, int codigoCentroAtencion)
	{
		ResultSetDecorator rs = null;
		HashMap mapaPacientesTriage =  new HashMap();
		String[] fechaNac;
		String edad = "";
		String tiempoEspera = "";
		String[] temp;
		try
		{
			rs = consultaPacientesTriageDao.consultarPacientesTriage(con, codigoCentroAtencion);
			int i = 0;
			while(rs.next())
			{
				
				mapaPacientesTriage.put("codigo_"+i, rs.getInt("codigo"));
				mapaPacientesTriage.put("codigopaciente_"+i, rs.getInt("codigopaciente"));
				mapaPacientesTriage.put("nombrepaciente_"+i, rs.getString("nombrepaciente"));
				mapaPacientesTriage.put("tipoid_"+i, rs.getString("tipoid"));
				mapaPacientesTriage.put("numeroid_"+i, rs.getString("numeroid"));
				mapaPacientesTriage.put("nombretipoid_"+i, rs.getString("nombretipoid"));
				mapaPacientesTriage.put("esconsecutivo_"+i, rs.getString("esconsecutivo"));
				mapaPacientesTriage.put("descclatriage_"+i, rs.getString("descclatriage"));
				
				fechaNac = ((String)rs.getString("fechanacimiento")).split("-");
				edad = UtilidadFecha.calcularEdadDetallada(Integer.parseInt(fechaNac[0]), Integer.parseInt(fechaNac[1]), Integer.parseInt(fechaNac[2]),UtilidadFecha.getMesAnioDiaActual("dia"), UtilidadFecha.getMesAnioDiaActual("mes"),UtilidadFecha.getMesAnioDiaActual("anio"));
				mapaPacientesTriage.put("edad_"+i, edad);
		        if(rs.getInt("sexo") ==  ConstantesBD.codigoSexoMasculino)
		        {
		        	mapaPacientesTriage.put("sexo_"+i, "M");
		        }
		        else
		        {
		        	mapaPacientesTriage.put("sexo_"+i, "F");
		        }
				mapaPacientesTriage.put("fechaingreso_"+i, UtilidadFecha.conversionFormatoFechaAAp(rs.getString("fechaingreso")+""));
				temp = rs.getString("horaingreso").split(":");
				mapaPacientesTriage.put("horaingreso_"+i, temp[0]+":"+temp[1]);
				
				tiempoEspera = this.tiempoEspera(UtilidadFecha.conversionFormatoFechaAAp(rs.getString("fechaingreso")) ,rs.getString("horaingreso"), UtilidadFecha.getFechaActual(), UtilidadFecha.getHoraActual());
				mapaPacientesTriage.put("tiempoespera_"+i, tiempoEspera);
				logger.info(mapaPacientesTriage.get("tiempoespera_"+i)+"  --  "+tiempoEspera);
				i++;
			}
			mapaPacientesTriage.put("numRegistros", i);
			return mapaPacientesTriage;
		}
		catch(SQLException e)
		{
			logger.error("Error consultarPacientesTriage [ConsultaPacientesTriage.java]"+e+"\n\n");
			e.printStackTrace();
		}
		return mapaPacientesTriage;
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
			duracionFinal = calcularDuracionEntreFechas(fechaInicial, horaIni, fechaFin, horaFin);
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
		String[] fechaIni = fechaInicial.split("/");
		String[] fechaFin = fechaFinal.split("/");
		String[] horaIni = horaInicial.split(":");
		String[] horaFin = horaFinal.split(":");
		
		logger.info("La fecha inicial-->"+fechaInicial);
		logger.info("La fecha final-->"+fechaFinal);
		logger.info("La hora inicial-->"+horaInicial);
		logger.info("La hora final-->"+horaFinal);
		logger.info("Entro a calcular la duracion");
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
	
	/**
	 * Método implementado para cargar los datos de un paciente regitrado para triage
	 * y que se encuentre pendiente
	 * @param con
	 * @param codigoPaciente
	 * @param codigoCentroAtencion
	 * @return
	 */
	public HashMap consultarDatosPacienteTriage (Connection con,int codigoPaciente,int codigoCentroAtencion)
	{
		return consultaPacientesTriageDao.consultarDatosPacienteTriage(con,codigoPaciente,codigoCentroAtencion);
	}
	
	
}