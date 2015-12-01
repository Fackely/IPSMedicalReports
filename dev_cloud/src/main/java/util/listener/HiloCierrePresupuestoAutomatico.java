package util.listener;

import it.sauronsoftware.cron4j.Scheduler;

import java.util.HashMap;

import org.axioma.util.log.Log4JManager;

import util.UtilidadFecha;
import util.UtilidadTexto;
import util.ValoresPorDefecto;

public class HiloCierrePresupuestoAutomatico {
	   /**
     * Instancias singleton por institución
     */
    private static HashMap<Integer, HiloCierrePresupuestoAutomatico> instanciasSingleton=new HashMap<Integer, HiloCierrePresupuestoAutomatico>();
   
    /**
     * Institución
     */
    private int institucion;

    /**
     * Id del proceso
     */
    private static String processId;
   
    /**
     * Manejador de cron de proceso
     */
    public static Scheduler proceso=new Scheduler();

    /**
     * Constructor privado para evitar el instanciamiento de esta clase
     * Solamente se puede instanciar como un singleton por institución
     */
    private HiloCierrePresupuestoAutomatico()
    {
        // solamente se tiene constructor para hacer el singleton
    }
   
    /**
     * Inicia el proceso automático de actualización de los estados de la cita
     * @param institucion Institución donde se va a ejecutar el proceso
     * @return true en caso de habilitar satisfactoriamente el proceso
     */
    public boolean iniciarProceso(int institucion)
    {
    	String horaEjecucion=ValoresPorDefecto.getHoraProcesoCierreCapitacion(institucion);
        if(!UtilidadTexto.isEmpty(horaEjecucion) && UtilidadFecha.validacionHora(horaEjecucion).puedoSeguir)
        {
            this.institucion=institucion;
            String[] horaTempo=horaEjecucion.split(":");
            String cadenaPatronAgendamiento=horaTempo[1]+" "+horaTempo[0]+" * * *";
            ProcesoCierrePresupuestoAutomatico tarea=new ProcesoCierrePresupuestoAutomatico();
            tarea.setInstitucion(institucion);
            processId=proceso.schedule(cadenaPatronAgendamiento,tarea);
            Log4JManager.info("HiloCierrePresupuestoAutomatico PROCESO_ID "+processId);
            proceso.start();
            return proceso.isStarted();
        }
        else
        {
            Log4JManager.info("Verificar la hora de ejecución: "+horaEjecucion);
        }
        return false;
    }
   
    /**
     * Volver a programar un proceso automático
     * @param cadenaPatronAgendamiento
     */
    public void reprogramaProceso()
    {
        if(processId != null && !processId.isEmpty())
        {
            interrumpirProceso();
            iniciarProceso(institucion);
        }
        else
        {
            Log4JManager.error("El proceso aun no se está ejecutando");
        }
    }

    /**
     * Interrumpe el proceso automático
     */
    public void interrumpirProceso()
    {
    	Log4JManager.info("HiloCierrePresupuestoAutomatico interrumpirProceso() PROCESO_ID"+processId);
    	try{
	        if(proceso.isStarted())
	        {
	            proceso.stop();
	            proceso.deschedule(processId);
	        }
    	}
    	catch(Exception e){
    		Log4JManager.error("ERROR interrumpirProceso() ",e);
    	}
    }

    /**
     * Obtiene el valor del atributo institucion
     *
     * @return Retorna atributo institucion
     */
    public int getInstitucion()
    {
        return institucion;
    }

    /**
     * Establece el valor del atributo institucion
     *
     * @param valor para el atributo institucion
     */
    public void setInstitucion(int institucion)
    {
        this.institucion = institucion;
    }

    /**
     * Instancia para el manejo de los listeners
     * @param institucion Institución para almacenar la instancia
     * @return Instancia única por institución
     */
    public static HiloCierrePresupuestoAutomatico getInstancia(int institucion)
    {
    	HiloCierrePresupuestoAutomatico instancia=instanciasSingleton.get(institucion);
        if(instancia!=null)
        {
            return instancia;
        }
        else
        {
            instancia=new HiloCierrePresupuestoAutomatico();
            instanciasSingleton.put(institucion, instancia);
            return instancia;
        }
    }
}
