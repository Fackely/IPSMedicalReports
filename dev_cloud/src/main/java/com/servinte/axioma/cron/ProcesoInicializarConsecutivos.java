package com.servinte.axioma.cron;

import it.sauronsoftware.cron4j.Scheduler;

import org.axioma.util.log.Log4JManager;

import com.servinte.axioma.cron.task.ProcesoInicializarConsecutivosTask;

public class ProcesoInicializarConsecutivos {
	
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
	public static boolean iniciarProceso(int institucion, String schedulingPattern)
	{
		if(idProceso.isEmpty())
		{
			ProcesoInicializarConsecutivosTask tarea=new ProcesoInicializarConsecutivosTask();
			idProceso=proceso.schedule(schedulingPattern,tarea);
			Log4JManager.info("ProcesoInicializarConsecutivos PROCESO_ID "+idProceso);
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

	/**
     * Interrumpe el proceso automático
     */
    public static void interrumpirProceso()
    {
    	Log4JManager.info("ProcesoInicializarConsecutivos interrumpirProceso() PROCESO_ID"+idProceso);
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
