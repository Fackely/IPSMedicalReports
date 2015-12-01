/**
 * 
 */
package com.servinte.axioma.cron;

import util.listener.HiloCierrePresupuestoAutomatico;


/**
 * @author axioma
 *
 */
public class AdministradorCron 
{
	
	public static void iniciarProcesos(int institucion)
	{
		
		//proceso inactivacion de usuarios.
		ProcesoActivacionInactivacionUsuariosCron.iniciarProceso(institucion);
		
		
		//proceso inactivacion de password.
		ProcesoActivacionInactivacionPasswordUsuariosCron.iniciarProceso(institucion);
		
		//proceso cierre automático.
		iniciarProcesoCierrePresupuestoAutomatico(institucion);
		
		ProcesoInicializarConsecutivos.iniciarProceso(institucion, "59 23 * * *");
		
	}
	
	
	
	/**
	 * Inicia el proceso
	 * @param institucion
	 * @autor Cristhian Murillo
	 */
	public static void iniciarProcesoCierrePresupuestoAutomatico(int institucion)
    {
		HiloCierrePresupuestoAutomatico cierrePresupuesto=HiloCierrePresupuestoAutomatico.getInstancia(institucion);
        cierrePresupuesto.iniciarProceso(institucion);
    }
	
	
	/**
	 * Modifica la hora de ejecución del proceso
	 * @param institucion
	 * @autor Cristhian Murillo
	 */
    public static void modificarProcesoCierrePresupuestoAutomatico(int institucion)
    {
    	HiloCierrePresupuestoAutomatico cierrePresupuesto=HiloCierrePresupuestoAutomatico.getInstancia(institucion);
        cierrePresupuesto.reprogramaProceso();
    }
    
    
    public static void finalizarProcesos(int institucion)
	{
		
		//proceso inactivacion de usuarios.
		ProcesoActivacionInactivacionUsuariosCron.interrumpirProceso();
		
		
		//proceso inactivacion de password.
		ProcesoActivacionInactivacionPasswordUsuariosCron.interrumpirProceso();
		
		HiloCierrePresupuestoAutomatico cierrePresupuesto=HiloCierrePresupuestoAutomatico.getInstancia(institucion);
        cierrePresupuesto.interrumpirProceso();
		
	}
    
}
