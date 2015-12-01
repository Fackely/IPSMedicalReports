package com.mercury.actionform.odontologia;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import util.UtilidadCadena;
import util.UtilidadFecha;

/**
 * ActionForm, tiene la función de bean dentro de la forma, que contiene todos
 * los datos de los antecedentes odontologia. Y adicionalmente hace el manejo
 * de reset de la forma y de validación de errores de datos de entrada.
 * @version 1.0, Septiembre 22 de 2005
 * @author <a href="mailto:alejodb@gmail.com">Alejandro Diaz Betancourt</a>,
 * @see org.apache.struts.action.ActionForm#validate(ActionMapping,
 * HttpServletRequest)
 */
public class AntecedentesOdontologiaForm extends ValidatorForm
{
    /**
     * Estado actual dentro del flujo de la funcionalidad
     */ 
    private String estado;

    /**
     * Observaciones generales previamente ingresadas
     */
    private String observacionesAnteriores;
    
    /**
     * Observaciones generales nuevas
     */
    private String observacionesNuevas;

    /**
     * Map para manejar los habitos
     */
    private final Map habitos = new HashMap(); 
    
    /**
     * Número de habitos almacenados en la base de datos
     */
    private int numHabitosBD;
    
    /**
     * Número total de habitos
     */
    private int numHabitos;
    
    /**
     * Map para manejar los habitos otros
     */
    private final Map habitosOtros = new HashMap(); 
    
    /**
     * Número de habitos otros almacenados en la base de datos
     */
    private int numHabitosOtrosBD;
    
    /**
     * Número total de habitos otros
     */
    private int numHabitosOtros;
    
    /**
     * Map para manejar los traumatismos
     */
    private final Map traumatismos = new HashMap(); 
    
    /**
     * Número de traumatismos almacenados en la base de datos
     */
    private int numTraumatismosBD;
    
    /**
     * Número total de traumatismos
     */
    private int numTraumatismos;
    
    /**
     * Map para manejar los traumatismos otros
     */
    private final Map traumatismosOtros = new HashMap(); 
    
    /**
     * Número de traumatismos otros almacenados en la base de datos
     */
    private int numTraumatismosOtrosBD;
    
    /**
     * Número total de traumatismos otros
     */
    private int numTraumatismosOtros;

    /**
     * Map para manejar los tratamientosPrevios
     */
    private final Map tratamientosPrevios = new HashMap(); 
    
    /**
     * Número de tratamientosPrevios almacenados en la base de datos
     */
    private int numTratamientosPreviosBD;
    
    /**
     * Número total de tratamientosPrevios
     */
    private int numTratamientosPrevios;
    
    private String ocultarCabezotes;
    
    /**
    * Valida  las propiedades que han sido establecidas para este request HTTP,
    * y retorna un objeto <code>ActionErrors</code> que encapsula los errores de
    * validación encontrados. Si no se encontraron errores de validación,
    * retorna <code>null</code>.
    * @param mapping el mapeado usado para elegir esta instancia
    * @param request el <i>servlet request</i> que está siendo procesado en este momento
    * @return un objeto <code>ActionErrors</code> con los (posibles) errores encontrados al validar este formulario,
    * o <code>null</code> si no se encontraron errores.
    */
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
    {
        ActionErrors errores = new ActionErrors();
        
        if(estado.equals("modificarTratamiento") || estado.equals("finalizar"))
        {
            for(int i=0; i<this.numTratamientosPrevios; i++)
            {
                String tratFecIni = (String)this.getTratamientoPrevio("tratFecIni_"+i);
                String tratFecFin = (String)this.getTratamientoPrevio("tratFecFin_"+i);
                if(UtilidadCadena.noEsVacio(tratFecIni))
                {
                    if(UtilidadFecha.esFechaValidaSegunAp(tratFecIni))
                    {
                        if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(tratFecIni, UtilidadFecha.getFechaActual()))
                        {
                            errores.add("La fecha inicial debe ser menor o igual que la fecha actual", new ActionMessage("errors.fechaPosteriorAOtraDeReferencia", "inicial", "actual"));
                        }
                    }
                    else
                        errores.add("La fecha inicial es invalida", new ActionMessage("errors.formatoFechaInvalido", "inicial"));
                }
                if(UtilidadCadena.noEsVacio(tratFecFin))
                {
                    if(UtilidadFecha.esFechaValidaSegunAp(tratFecFin))
                    {
                        if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(tratFecFin, UtilidadFecha.getFechaActual()))
                        {
                            errores.add("La fecha final debe ser menor o igual que la fecha actual", new ActionMessage("errors.fechaPosteriorAOtraDeReferencia", "final", "actual"));
                        }
                    }
                    else
                        errores.add("La fecha final es invalida", new ActionMessage("errors.formatoFechaInvalido", "final"));
                }
                if(UtilidadCadena.noEsVacio(tratFecIni) && UtilidadCadena.noEsVacio(tratFecFin))
                {
                    if(UtilidadFecha.esFechaValidaSegunAp(tratFecIni))
                    {
                        if(UtilidadFecha.esFechaValidaSegunAp(tratFecFin))
                        {
                            if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(tratFecIni, tratFecFin))
                                errores.add("La fecha inicial debe ser menor o igual que la fecha final", new ActionMessage("errors.fechaPosteriorAOtraDeReferencia", "inicial", "final"));
                        }
                        else
                            errores.add("La fecha final es invalida", new ActionMessage("errors.formatoFechaInvalido", "final"));
                    }
                    else
                        errores.add("La fecha inicial es invalida", new ActionMessage("errors.formatoFechaInvalido", "inicial"));
                }
            }
        }
        else if(estado.equals("adicionarTratamiento"))
        {
            int pos = this.getNumTratamientosPrevios()-1;
            String tratTip = (String)this.getTratamientoPrevio("tratTip_"+pos);
            

            if( tratTip == null || tratTip.equals("") )
            {
                errores.add("El tipo de tratamiento no puede ser vacio", new ActionMessage("errors.required", "El tipo de tratamiento"));
            }
            String tratFecIni = (String)this.getTratamientoPrevio("tratFecIni_"+pos);
            String tratFecFin = (String)this.getTratamientoPrevio("tratFecFin_"+pos);
            if(UtilidadCadena.noEsVacio(tratFecIni))
            {
                if(UtilidadFecha.esFechaValidaSegunAp(tratFecIni))
                {
                    if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(tratFecIni, UtilidadFecha.getFechaActual()))
                    {
                        errores.add("La fecha inicial debe ser menor o igual que la fecha actual", new ActionMessage("errors.fechaPosteriorAOtraDeReferencia", "inicial", "actual"));
                    }
                }
                else
                    errores.add("La fecha inicial es invalida", new ActionMessage("errors.formatoFechaInvalido", "inicial"));
            }
            if(UtilidadCadena.noEsVacio(tratFecFin))
            {
                if(UtilidadFecha.esFechaValidaSegunAp(tratFecFin))
                {
                    if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(tratFecFin, UtilidadFecha.getFechaActual()))
                    {
                        errores.add("La fecha final debe ser menor o igual que la fecha actual", new ActionMessage("errors.fechaPosteriorAOtraDeReferencia", "final", "actual"));
                    }
                }
                else
                    errores.add("La fecha final es invalida", new ActionMessage("errors.formatoFechaInvalido", "final"));
            }
            if(UtilidadCadena.noEsVacio(tratFecIni) && UtilidadCadena.noEsVacio(tratFecFin))
            {
                if(UtilidadFecha.esFechaValidaSegunAp(tratFecIni))
                {
                    if(UtilidadFecha.esFechaValidaSegunAp(tratFecFin))
                    {
                        if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(tratFecIni, tratFecFin))
                            errores.add("La fecha inicial debe ser menor o igual que la fecha final", new ActionMessage("errors.fechaPosteriorAOtraDeReferencia", "inicial", "final"));
                    }
                    else
                        errores.add("La fecha final es invalida", new ActionMessage("errors.formatoFechaInvalido", "final"));
                }
                else
                    errores.add("La fecha inicial es invalida", new ActionMessage("errors.formatoFechaInvalido", "inicial"));
            }
            if(!errores.isEmpty())
                this.numTratamientosPrevios=this.numTratamientosPrevios-1;
        }            
        return errores;
    }
    
    public void reset()
    {
        this.habitos.clear();
        this.numHabitosBD = 0;
        this.numHabitos = 0;
        this.habitosOtros.clear();
        this.numHabitosOtrosBD = 0;
        this.numHabitosOtros = 0;
        this.traumatismos.clear();
        this.numTraumatismosBD = 0;
        this.numTraumatismos = 0;
        this.traumatismosOtros.clear();
        this.numTraumatismosOtrosBD = 0;
        this.numTraumatismosOtros = 0;
        this.tratamientosPrevios.clear();
        this.numTratamientosPreviosBD = 0;
        this.numTratamientosPrevios = 0;
        this.observacionesAnteriores = "";
        this.observacionesNuevas = "";
        this.ocultarCabezotes="";
    }
    
    /**
     * Retorna el estado actual dentro del flujo de la funcionalidad
     * @return  String, estado.
     */
    public String getEstado()
    {
        return estado;
    }

    /**
     * Asigna el estado actual dentro del flujo de la funcionalidad
     * @param   String, estado.
     */
    public void setEstado(String estado)
    {
        this.estado = estado;
    }
    
    /**
     * Retorna el  habito dada su llave
     * @param String, llave bajo la cual se almaceno la información
     * @return Object, objeto almacenado en el map
     */
    public Object getHabito(String key)
    {
        return this.habitos.get(key);
    }
    
    /**
     * Almacena un valor de  habito en el map, bajo la llave dada.
     * @param Object, value
     * @param String, key
     */
    public void setHabito(String key, Object value)
    {
        this.habitos.put(key, value);      
    }   

    /**
     * Retorna el número de habitos almacenados en la base de datos
     * @return  int, número de habitos en la bd
     */
    public int getNumHabitosBD()
    {
        return numHabitosBD;
    }

    /**
     * Asigna el número de habitos almacenados en la base de datos
     * @param int, número de habitos en la bd
     */
    public void setNumHabitosBD(int numHabitosBD)
    {
        this.numHabitosBD = numHabitosBD;
    }

    /**
     * Retorna el número total de habitos
     * @return  int, número de habitos
     */
    public int getNumHabitos()
    {
        return numHabitos;
    }

    /**
     * Asigna el número total de habitos
     * @param int, número de habitos
     */
    public void setNumHabitos(int numHabitos)
    {
        this.numHabitos = numHabitos;
    }

    /**
     * Retorna el  habito otro dada su llave
     * @param String, llave bajo la cual se almaceno la información
     * @return Object, objeto almacenado en el map
     */
    public Object getHabitoOtro(String key)
    {
        return this.habitosOtros.get(key);
    }
    
    /**
     * Almacena un valor de habito otro en el map, bajo la llave dada.
     * @param Object, value
     * @param String, key
     */
    public void setHabitoOtro(String key, Object value)
    {
        this.habitosOtros.put(key, value);      
    }   

    /**
     * Retorna el número de habitos otros almacenados en la base de datos
     * @return  int, número de habitos otros en la bd
     */
    public int getNumHabitosOtrosBD()
    {
        return numHabitosOtrosBD;
    }

    /**
     * Asigna el número de habitos otros almacenados en la base de datos
     * @param int, número de habitos otros en la bd
     */
    public void setNumHabitosOtrosBD(int numHabitosOtrosBD)
    {
        this.numHabitosOtrosBD = numHabitosOtrosBD;
    }

    /**
     * Retorna el número total de habitos otros
     * @return  int, número de habitos otros
     */
    public int getNumHabitosOtros()
    {
        return numHabitosOtros;
    }

    /**
     * Asigna el número total de habitos otros
     * @param int, número de habitos otros
     */
    public void setNumHabitosOtros(int numHabitosOtros)
    {
        this.numHabitosOtros = numHabitosOtros;
    }

    /**
     * Retorna el  traumatismo dada su llave
     * @param String, llave bajo la cual se almaceno la información
     * @return Object, objeto almacenado en el map
     */
    public Object getTraumatismo(String key)
    {
        return this.traumatismos.get(key);
    }
    
    /**
     * Almacena un valor de  traumatismo en el map, bajo la llave dada.
     * @param Object, value
     * @param String, key
     */
    public void setTraumatismo(String key, Object value)
    {
        this.traumatismos.put(key, value);      
    }   

    /**
     * Retorna el número de traumatismos almacenados en la base de datos
     * @return  int, número de traumatismos en la bd
     */
    public int getNumTraumatismosBD()
    {
        return numTraumatismosBD;
    }

    /**
     * Asigna el número de traumatismos almacenados en la base de datos
     * @param int, número de traumatismos en la bd
     */
    public void setNumTraumatismosBD(int numTraumatismosBD)
    {
        this.numTraumatismosBD = numTraumatismosBD;
    }

    /**
     * Retorna el número total de traumatismos
     * @return  int, número de traumatismos
     */
    public int getNumTraumatismos()
    {
        return numTraumatismos;
    }

    /**
     * Asigna el número total de traumatismos
     * @param int, número de traumatismos
     */
    public void setNumTraumatismos(int numTraumatismos)
    {
        this.numTraumatismos = numTraumatismos;
    }

    /**
     * Retorna el  traumatismo otro dada su llave
     * @param String, llave bajo la cual se almaceno la información
     * @return Object, objeto almacenado en el map
     */
    public Object getTraumatismoOtro(String key)
    {
        return this.traumatismosOtros.get(key);
    }
    
    /**
     * Almacena un valor de traumatismo otro en el map, bajo la llave dada.
     * @param Object, value
     * @param String, key
     */
    public void setTraumatismoOtro(String key, Object value)
    {
        this.traumatismosOtros.put(key, value);      
    }   

    /**
     * Retorna el número de traumatismos otros almacenados en la base de datos
     * @return  int, número de traumatismos otros en la bd
     */
    public int getNumTraumatismosOtrosBD()
    {
        return numTraumatismosOtrosBD;
    }

    /**
     * Asigna el número de traumatismos otros almacenados en la base de datos
     * @param int, número de traumatismos otros en la bd
     */
    public void setNumTraumatismosOtrosBD(int numTraumatismosOtrosBD)
    {
        this.numTraumatismosOtrosBD = numTraumatismosOtrosBD;
    }

    /**
     * Retorna el número total de traumatismos otros
     * @return  int, número de traumatismos otros
     */
    public int getNumTraumatismosOtros()
    {
        return numTraumatismosOtros;
    }

    /**
     * Asigna el número total de traumatismos otros
     * @param int, número de traumatismos otros
     */
    public void setNumTraumatismosOtros(int numTraumatismosOtros)
    {
        this.numTraumatismosOtros = numTraumatismosOtros;
    }

    /**
     * Retorna el  tratamientoPrevio dado su indice
     * @param String, indice q se almaceno la información
     * @return Object, objeto almacenado en el map
     */
    public Object getTratamientoPrevio(String key)
    {
        return this.tratamientosPrevios.get(key);
    }
    
    /**
     * Almacena un valor de  tratamientoPrevio en el map, bajo la llave dada.
     * @param Object, value
     * @param String, key
     */
    public void setTratamientoPrevio(String key, Object value)
    {
        this.tratamientosPrevios.put(key, value);
    }   

    /**
     * Retorna el número de tratamientosPrevios almacenados en la base de datos
     * @return  int, número de tratamientosPrevios en la bd
     */
    public int getNumTratamientosPreviosBD()
    {
        return numTratamientosPreviosBD;
    }

    /**
     * Asigna el número de tratamientosPrevios almacenados en la base de datos
     * @param int, número de tratamientosPrevios en la bd
     */
    public void setNumTratamientosPreviosBD(int numTratamientosPreviosBD)
    {
        this.numTratamientosPreviosBD = numTratamientosPreviosBD;
    }

    /**
     * Retorna el número total de tratamientosPrevios
     * @return  int, número de tratamientosPrevios
     */
    public int getNumTratamientosPrevios()
    {
        return this.numTratamientosPrevios;
    }

    /**
     * Asigna el número total de tratamientosPrevios
     * @param int, número de tratamientosPrevios
     */
    public void setNumTratamientosPrevios(int numTratamientosPrevios)
    {
        this.numTratamientosPrevios = numTratamientosPrevios;
    }

    /**
     * Retorna las observaciones generales previamente ingresadas
     * @return   String, observaciones
     */
    public String getObservacionesAnteriores()
    {
        return observacionesAnteriores;
    }

    /**
     * Asigna las observaciones generales previamente ingresadas
     * @param   String, observaciones anteriores
     */
    public void setObservacionesAnteriores(String observacionesAnteriores)
    {
        this.observacionesAnteriores = observacionesAnteriores;
    }

    /**
     * Retorna las observaciones generales nuevas
     * @return  String, observaciones nuevas
     */
    public String getObservacionesNuevas()
    {
        return observacionesNuevas;
    }

    /**
     * Asgina las observaciones generales nuevas
     * @param   String, observaciones nuevas
     */
    public void setObservacionesNuevas(String observacionesNuevas)
    {
        this.observacionesNuevas = observacionesNuevas;
    }

    /**
     * @return Returns the ocultarCabezotes.
     */
    public String getOcultarCabezotes() {
        return ocultarCabezotes;
    }
    /**
     * @param ocultarCabezotes The ocultarCabezotes to set.
     */
    public void setOcultarCabezotes(String ocultarCabezotes) {
        this.ocultarCabezotes = ocultarCabezotes;
    }
}
