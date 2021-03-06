/**
 * 
 */
package com.servinte.axioma.cron;

import it.sauronsoftware.cron4j.Scheduler;

import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.ValoresPorDefecto;

import com.servinte.axioma.cron.task.ProcesoActivacionInactivacionPasswordUsuariosTask;
import com.servinte.axioma.mundo.fabrica.AdministracionFabricaMundo;
import com.servinte.axioma.mundo.interfaz.administracion.IProcesosInactivacionUsuarioCaducidadPassword;
import com.servinte.axioma.orm.LogProcesoCadupwdUsu;
import com.servinte.axioma.orm.LogProcesoCadupwdUsuHome;

/**
 * @author axioma
 *
 */
public class ProcesoActivacionInactivacionPasswordUsuariosCron 
{
	
	/**
	 * 
	 */
	public static String idProceso="";
	
	/**
	 * 
	 */
	public static Scheduler proceso=new Scheduler();
	
	
	/**
	 *
	 */
	public static boolean iniciarProceso(String schedulingPattern)
	{
		if(idProceso.isEmpty())
		{
			ProcesoActivacionInactivacionPasswordUsuariosTask tarea=new ProcesoActivacionInactivacionPasswordUsuariosTask();
			idProceso=proceso.schedule(schedulingPattern,tarea);
			Log4JManager.info("ProcesoActivacionInactivacionPasswordUsuariosCron PROCESO_ID "+idProceso);
			proceso.start();
			return proceso.isStarted();
		}
		else
		{
			Log4JManager.error("EL PROCESO YA SE ENCUENTRA EN EJECUCION");
		}
		return false;
	}
	
	/**
	 *
	 */
	public static void reprogramaProceso(String schedulingPattern)
	{
		if(!idProceso.isEmpty())
		{
			proceso.reschedule(idProceso,schedulingPattern);
		}
		else
		{
			Log4JManager.error("EL PROCESO AUN NO SE ESTA EJECUTANDO");
		}
	}
	
	/**
	 *
	 */
	public static boolean reinicarProceso(String schedulingPattern)
	{
		if(!idProceso.isEmpty())
		{
			if(pararProceso(idProceso))
			{
				return iniciarProceso(schedulingPattern);
			}
		}
		else
		{
			Log4JManager.error("EL PROCESO AUN NO SE ESTA EJECUTANDO");
			return iniciarProceso(schedulingPattern);
		}
		return false;
	}

	public static boolean pararProceso(String schedulingPattern)
	{
		if(!idProceso.isEmpty())
		{
			proceso.deschedule(idProceso);
			proceso.stop();
			if(!proceso.isStarted())
				idProceso="";
			return !proceso.isStarted();
		}
		else
		{
			Log4JManager.error("EL PROCESO AUN NO SE ESTA EJECUTANDO");
		}
		return false;
	}

	/***
	 * 
	 * @return
	 */
	public static void iniciarProceso(int institucion) 
	{
		String horaProc=ValoresPorDefecto.getHoraEjeProcesoCaduContraInacSistema(institucion);
		if(!UtilidadTexto.isEmpty(horaProc) && horaProc.indexOf(":")>0)
		{
			String[] horaTempo=horaProc.split(":");
			boolean iniciar=iniciarProceso(horaTempo[1]+" "+horaTempo[1]+" * * *");
			if (!iniciar) {
				Log4JManager.error("No inicio Proceso inactivaci?n. Patr?n: " + horaTempo[1]+" "+horaTempo[1]+" * * *");
			}
		}

	}

	/**
	 * 
	 * @param institucion
	 */
	public static void reinicarProceso(int institucion) 
	{
		String horaProc=ValoresPorDefecto.getHoraEjeProcesoCaduContraInacSistema(institucion);
		if(!UtilidadTexto.isEmpty(horaProc) && horaProc.indexOf(":")>0)
		{
			String[] horaTempo=horaProc.split(":");
			boolean iniciar=reinicarProceso(horaTempo[1]+" "+horaTempo[0]+" * * *");
			if (!iniciar) {
				Log4JManager.error("No reinicio proceso inactivaci?n. Patr?n: " + horaTempo[1]+" "+horaTempo[1]+" * * *");
			}
		}
		else
		{
			pararProceso(idProceso);
		}
	}
	
	/**
     * Interrumpe el proceso autom?tico
     */
    public static void interrumpirProceso()
    {
    	Log4JManager.info("ProcesoActivacionInactivacionPasswordCron interrumpirProceso() PROCESO_ID"+idProceso);
    	try{
	        if(proceso.isStarted())
	        {
	            proceso.stop();
	            proceso.deschedule(idProceso);
	        }
    	}
    	catch(Exception e){
    		Log4JManager.error("ERROR interrumpirProceso() ",e);
    	}
    }

}
