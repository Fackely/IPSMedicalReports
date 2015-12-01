/*
 * Creado  17/08/2004
 *
 *Copyright Princeton S.A. &copy;&reg; 2004. Todos los Derechos Reservados.
 *
 * Lenguaje		:Java
 * Compilador	:J2SDK 1.4.2_04
 */
package com.princetonsa.actionform.parametrizacion;


import java.util.Collection;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import util.ConstantesBD;


/**
 * Clase para manejar
 *
 * @version 1.0, 17/08/2004
 * @author <a href="mailto:joan@PrincetonSA.com">Joan Lopez</a>
 */
public class RegUnidadesConsultaForm extends ActionForm
{
	
	 private Logger logger=Logger.getLogger(RegUnidadesConsultaForm.class);
	
	/**
	 * Almacena el log generado por la modificacion o eliminacion de unidad
	 * de consulta. 
	 */
	private String Log;
	
	/**
	 * Collection general.
	 */
	private Collection coleccion;
	
	/**
	 * Collection para almacenar el resultado de la consulta del
	 * CUPS y el Servicio.
	 */
	private Collection coleccionCups;
	
	/**
	 * Atributo para almacenar el estado activo/inactivo
	 */
	private String checkEstado;
		
	/**
	 * Atributo para almacenar el estado activo/inactivo del Codigo
	 */
	private String checkCodigo;
	    	
	/**
	 * Atributo para almacenar el estado activo/inactivo de la Descripcion
	 */
	private String checkDescripcion;
	
	
	/**
	 * Atributo para almacenar el estado activo/inactivo de la Descripcion
	 */
	private String checkEspecialidad;
	    
	/**
	 * Atributo para almacenar el estado activo/inactivo del Codigo Axioma
	 */
	private String checkServicio;    
	    
	/**
	 * Atributo para almacenar el estado activo/inactivo del Estado
	 */
	private String checkActivo;
	
	/**
	 * para almacenar el estado real del checkbox en la vista
	 */
	private String checkTemp;

	
	/**
	* Campo para capturar la descripcion de la unidad de 
	* consulta.
	*/
	private String Descripcion;
	
	/**
	* Campo para capturar la descripcion de la unidad de 
	* consulta.
	*/
	private String descripcionAntiguo;
	
	/**
	* Campo para capturar el codServicio asociado a la
	* unidad de consulta de la tabla servicios del campo codigo.
	*/
	private int codServicio;
	
	/**
	 * Campo que permite Activar/Inactivar las unidades de 
	 * consulta.  
	 */
	private boolean Activo;
	
	/**
	* Atributo para manejar los estados del workFlow
	*/
    private String estado;
	    
	/**
	 * Atributo para capturar el codigo de la especialidad
	 * de la tabla Servicios del campo especialidad. 
	 */
	private int codigoEspecialidad;
	
	/**
	 * Capturar codigo Unidad de Consulta
	 */
	private int codigoT;
	   
	/**
	 * Verificar estado Activo
	 */
	private String checkAct;
	   
	/**
	 * verificar estado inactivo
	 */
	private String checkIna;
	   
	/**
	 * Para manipular el estado Activo o Inactivo de la Unidad de 
	 * Consulta en la Busqueda.
	 */
	private String Temp;
	
	private int index;
	
	/**
	 * almacena la descripcion del servicio
	 */
	private String descripcionCUPS;
	
	/**
	 * HashMap para almacenar los servicios de una Unidad de Consulta.
	 */
	private HashMap servicios;

	/**
	 * HashMap para almacenar los servicios de una Unidad de Consulta.
	 */
	private HashMap serviciosBD;	
	
	private int registrosNuevos;
	
	/**
	 * codigos de los servicios insertados para no repetirlos
	 * en la busqueda avanzada de servicios
	 */
	private String codigosServiciosInsertados;
	
	
	/**
	 * HashMap de especialidades
	 * */
	private HashMap especialidadesMap;
	
	/**
	 * String indicador del servicio
	 * */
	private String indexServicio;
	
	/**
	 * Indicador de Operaciones
	 * */
	private String indicadorOperaciones;
	
	
	//Cambiosfuncionalidades Consulta externa Anexo 810
	//------------------------------------------------
	private String especialidadUniAgenda; 
	private String nomEspecialidadUniAgenda;

	//------------------------------------------------

	private boolean tieneHorarioAsignado;

	/*
	 * Atributo adicionado por documento 869 
	 */
	private String tiposAtencion;
	
	/*
	 * Atributo para almacenar el color seleccionado en la paleta de colores 
	 */
	private String colorFondo;
	
	/**
	 * Atributo que indica si la instituci&oacute;n utiliza programas odontol&oacute;gicos.
	 */
	private String utilizaProgramasOdonto; 
	
	/**
	 *Atributo para buscar el servicio por codigoPK 
	 */
	private String codigoPrograma;
	
	/**
	 *Atributo para mostrar el nombre del servicio 
	 */
	private String nombrePrograma;
	
	/**
	 * Atributo que almacena el código de programa que es visualizado por el usuario.
	 */
	private String codigoProgramaMostrar;
	
	/**
	 * LISTA CODIGOS PROGRAMAS SERVICIOS
	 */
	private String listaCodigoProgramaServicios;
	
	/**
	 * reset de los datos de la forma
	 *
	 */
	public void reset()
	{
		this.index=ConstantesBD.codigoNuncaValido;
		this.registrosNuevos=0;
	    this.servicios=new HashMap();
	    this.servicios.put("numeroServicios","0");
	    this.serviciosBD=new HashMap();
	    this.serviciosBD.put("numeroServicios","0");
	    this.codigosServiciosInsertados="";
	    this.especialidadesMap = new HashMap();
	    this.indicadorOperaciones ="";
	    
	    //Cambiosfuncionalidades Consulta externa Anexo 810
		//------------------------------------------------
		this.especialidadUniAgenda = "";
		//------------------------------------------------
		
		this.tiposAtencion="";
		this.colorFondo="";
		this.utilizaProgramasOdonto = "";
		this.codigoPrograma = "";
		this.nombrePrograma = "";
		this.codigoProgramaMostrar = "";
		this.listaCodigoProgramaServicios = "";
	}

	public String getColorFondo() {
		return colorFondo;
	}

	public void setColorFondo(String colorFondo) {
		this.colorFondo = colorFondo;
	}

	public String getTiposAtencion() {
		return tiposAtencion;
	}

	public void setTiposAtencion(String tiposAtencion) {
		this.tiposAtencion = tiposAtencion;
	}

	/**
     * @return Retorna  descripcion.
     */
    public String getDescripcion()
    {
        return Descripcion;
    }
    /**
     * @param descripcion asigna descripcion.
     */
    public void setDescripcion(String descripcion)
    {
        this.Descripcion = descripcion;
    }
    /**
     * @return Retorna  estado.
     */
    public boolean getActivo()
    {
        return Activo;
    }
    /**
     * @param estado asigna estado.
     */
    public void setActivo(boolean activo)
    {
        this.Activo = activo;
    }
    /**
     * @return Retorna  codServicio.
     */
    public int getCodServicio()
    {
        return codServicio;
    }
    /**
     * @param codServicio asigna codServicio.
     */
    public void setCodServicio(int codServicio)
    {
        this.codServicio = codServicio;
    }
    
    /**
     * @return Retorna  estado.
     */
    public String getEstado()
    {
        return estado;
    }
    /**
     * @param estado asigna estado.
     */
    public void setEstado(String estado)
    {
        this.estado = estado;
    }
        
    /**
     * @return Retorna  codigoEspecialidad.
     */
    public int getCodigoEspecialidad()
    {
        return codigoEspecialidad;
    }
    /**
     * @param codigoEspecialidad asigna codigoEspecialidad.
     */
    public void setCodigoEspecialidad(int codigoEspecialidad)
    {
        this.codigoEspecialidad = codigoEspecialidad;
    }
    
    /**
     * @return Retorna  checkEstado.
     */
    public String getCheckEstado()
    {
        return checkEstado;
    }
    /**
     * @param checkEstado asigna checkEstado.
     */
    public void setCheckEstado(String checkEstado)
    {
        this.checkEstado = checkEstado;
    }
    
    
    /**
     * @return Retorna  checkActivo.
     */
    public String getCheckActivo()
    {
        return checkActivo;
    }
    /**
     * @param checkActivo asigna checkActivo.
     */
    public void setCheckActivo(String checkActivo)
    {
        this.checkActivo = checkActivo;
    }
   
    /**
     * @return Retorna  checkCodigo.
     */
    public String getCheckCodigo()
    {
        return checkCodigo;
    }
    /**
     * @param checkCodigo asigna checkCodigo.
     */
    public void setCheckCodigo(String checkCodigo)
    {
        this.checkCodigo = checkCodigo;
    }
    
    /**
     * @return Retorna  checkDescripcion.
     */
    public String getCheckDescripcion()
    {
        return checkDescripcion;
    }
    /**
     * @param checkDescripcion asigna checkDescripcion.
     */
    public void setCheckDescripcion(String checkDescripcion)
    {
        this.checkDescripcion = checkDescripcion;
    }
        
    /**
     * @return Retorna  codigoT.
     */
    public int getCodigoT()
    {
        return codigoT;
    }
    /**
     * @param codigoT asigna codigoT.
     */
    public void setCodigoT(int codigoT)
    {
        this.codigoT = codigoT;
    }
    
    /**
     * @return Retorna  checkServicio.
     */
    public String getCheckServicio()
    {
        return checkServicio;
    }
    /**
     * @param checkServicio asigna checkServicio.
     */
    public void setCheckServicio(String checkServicio)
    {
        this.checkServicio = checkServicio;
    }
    
    
    /**
     * @return Retorna  coleccion.tiposervicio_
     */
    public Collection getColeccion()
    {
        return coleccion;
    }
    /**
     * @param coleccion asigna coleccion.
     */
    public void setColeccion(Collection coleccion)
    {
        this.coleccion = coleccion;
    }
    
    
    /**
     * @return Retorna  checkAct.
     */
    public String getCheckAct()
    {
        return checkAct;
    }
    /**
     * @param checkAct asigna checkAct.
     */
    public void setCheckAct(String checkAct)
    {
        this.checkAct = checkAct;
    }
    /**
     * @return Retorna  checkIna.
     */
    public String getCheckIna()
    {
        return checkIna;
    }
    /**
     * @param checkIna asigna checkIna.
     */
    public void setCheckIna(String checkIna)
    {
        this.checkIna = checkIna;
    }
    
   /**
     * @return Retorna  temp.
     */
    public String getTemp()
    {
        return Temp;
    }
    /**
     * @param temp asigna temp.
     */
    public void setTemp(String temp)
    {
        Temp = temp;
    }
    
    /**
     * @return Retorna  log.
     */
    public String getLog()
    {
        return Log;
    }
    /**
     * @param log asigna log.
     */
    public void setLog(String log)
    {
        Log = log;
    }
    
    /**
     * @return Retorna  coleccionCups.
     */
    public Collection getColeccionCups()
    {
        return coleccionCups;
    }
    /**
     * @param coleccionCups asigna coleccionCups.
     */
    public void setColeccionCups(Collection coleccionCups)
    {
        this.coleccionCups = coleccionCups;
    }
    
    /**
     * Metodo para limpiar los atributos.
     */
    	public void clean ()
    	{
    	    this.Descripcion="";
    	    this.descripcionAntiguo="";
    	    this.codServicio=0;
    	    this.Activo=false;
    	    this.codigoEspecialidad=-2;
    	    this.codigoT=-1;
    	    this.Temp="";
    	    this.descripcionCUPS ="";   
    	    this.reset();
    	}
    /**
     * Metodo para limpiar todos los Checkbox.
     *
     */	
    	public void cleanChecks ()
    	{
    	    this.checkAct="";
    	    this.checkActivo="";
    	    this.checkCodigo="";
    	    this.checkDescripcion="";
    	    this.checkEspecialidad="";
    	    this.checkIna="";
    	    this.checkServicio="";
    	    this.checkEstado = "";
    	    this.checkTemp = "";
    	}
    	
    	 /**
         * Metodo para validar todos los posibles eerores presentes en el 
         * <code>Form</code>.
         */
    	 public ActionErrors validate (ActionMapping mapping, HttpServletRequest request)
    	 {

                        
            ActionErrors errores= new ActionErrors();
            
            if(estado.equals("insercion"))
            {
                clean();
            }
            if(estado.equals("empezar"))
            {
               return null;
            }
            if(estado.equals("guardar"))
            {
            	int numRegistros = Integer.parseInt(servicios.get("numeroServicios").toString());
            	
                if(this.Descripcion.equals(""))
                {
                    errores.add("Campo Descripcion vacio", new ActionMessage("errors.required","El campo Descripción"));
                }
                
                if(this.especialidadUniAgenda.equals(ConstantesBD.codigoNuncaValido+""))
                {
                    errores.add("Campo Especialidad no Seleccionado", new ActionMessage("errors.required","El campo Especialidad"));
                }
                /*for(int i = 0; i < numRegistros; i++)
                {           		
                	if(servicios.get("tiposervicio_"+i).toString().equals(ConstantesBD.codigoServicioCargosConsultaExterna+"") 
                			&& (servicios.get("especialidad_"+i).toString().equals("") || servicios.get("especialidad_"+i).toString().equals("0")))
                	{
                		errores.add("Campo especialidad", new ActionMessage("errors.required","El campo Especialidad No. "+i ));                		
                	}
                }*/                
                
                if(Integer.parseInt(servicios.get("numeroServicios")+"")==0)
                {
                    errores.add("Campo Servicio vacio", new ActionMessage("errors.required","El Servicio"));
                }    
                
                if(this.tiposAtencion.equals("-1"))
                {
                	 errores.add("descripcion", new ActionMessage("errors.required","El Tipo de Atención "));
                }
            }           
                     
            
    	    return errores;
    	     
    	 }
        
    /**
     * @return Retorna descripcionCUPS.
     */
    public String getDescripcionCUPS() {
        return descripcionCUPS;
    }
    /**
     * @param descripcionCUPS Asigna descripcionCUPS.
     */
    public void setDescripcionCUPS(String descripcionCUPS) {
        this.descripcionCUPS = descripcionCUPS;
    }
    /**
     * @return Retorna checkTemp.
     */
    public String getCheckTemp() {
        return checkTemp;
    }
    /**
     * @param checkTemp Asigna checkTemp.
     */
    public void setCheckTemp(String checkTemp) {
        this.checkTemp = checkTemp;
    }
    
	/**
	 * @return Returns the servicios.
	 */
	public HashMap getServicios() {
		return servicios;
	}
	/**
	 * @param servicios The servicios to set.
	 */
	public void setServicios(HashMap servicios) {
		this.servicios = servicios;
	}
	/**
	 * @return Returns the servicios.
	 */
	public Object getServicios(String key) {
		return servicios.get(key);
	}
	/**
	 * @param servicios The servicios to set.
	 */
	public void setServicios(String key,Object value) {
		this.servicios.put(key,value);
	}
	
	/**
	 * @return Returns the index.
	 */
	public int getIndex() {
		return index;
	}
	/**
	 * @param index The index to set.
	 */
	public void setIndex(int index) {
		this.index = index;
	}
	/**
	 * @return Returns the descripcionAntiguo.
	 */
	public String getDescripcionAntiguo() {
		return descripcionAntiguo;
	}
	/**
	 * @param descripcionAntiguo The descripcionAntiguo to set.
	 */
	public void setDescripcionAntiguo(String descripcionAntiguo) {
		this.descripcionAntiguo = descripcionAntiguo;
	}
	/**
	 * @return Returns the serviciosBD.
	 */
	public HashMap getServiciosBD() {
		return serviciosBD;
	}
	/**
	 * @param serviciosBD The serviciosBD to set.
	 */
	public void setServiciosBD(HashMap serviciosBD) {
		this.serviciosBD = serviciosBD;
	}
	public Object getServiciosBD(String key) {
		return serviciosBD.get(key);
	}
	/**
	 * @param servicios The servicios to set.
	 */
	public void setServiciosBD(String key,Object value) {
		this.serviciosBD.put(key,value);
	}
	/**
	 * @return Returns the registrosNuevos.
	 */
	public int getRegistrosNuevos() {
		return registrosNuevos;
	}
	/**
	 * @param registrosNuevos The registrosNuevos to set.
	 */
	public void setRegistrosNuevos(int registrosNuevos) {
		this.registrosNuevos = registrosNuevos;
	}

	public String getCheckEspecialidad() {
		return checkEspecialidad;
	}

	public void setCheckEspecialidad(String checkEspecialidad) {
		this.checkEspecialidad = checkEspecialidad;
	}
	 /**
     * @return Returns the codigosServiciosInsertados.
     */
    public String getCodigosServiciosInsertados() {
        return codigosServiciosInsertados;
    }
    /**
     * @param codigosServiciosInsertados The codigosServiciosInsertados to set.
     */
    public void setCodigosServiciosInsertados(String codigosServiciosInsertados) {
        this.codigosServiciosInsertados = codigosServiciosInsertados;
    }

	/**
	 * @return the especialidadesMap
	 */
	public HashMap getEspecialidadesMap() {
		return especialidadesMap;
	}

	/**
	 * @param especialidadesMap the especialidadesMap to set
	 */
	public void setEspecialidadesMap(HashMap especialidadesMap) {
		this.especialidadesMap = especialidadesMap;
	}  
	
	
	/**
	 * @return the especialidadesMap
	 */
	public Object getEspecialidadesMap(String key) {
		return especialidadesMap.get(key);
	}

	/**
	 * @param especialidadesMap the especialidadesMap to set
	 */
	public void setEspecialidadesMap(String key, Object value) {
		this.especialidadesMap.put(key, value);
	}

	/**
	 * @return the indexServicio
	 */
	public String getIndexServicio() {
		return indexServicio;
	}

	/**
	 * @param indexServicio the indexServicio to set
	 */
	public void setIndexServicio(String indexServicio) {
		this.indexServicio = indexServicio;
	}

	/**
	 * @return the indicadorOperaciones
	 */
	public String getIndicadorOperaciones() {
		return indicadorOperaciones;
	}

	/**
	 * @param indicadorOperaciones the indicadorOperaciones to set
	 */
	public void setIndicadorOperaciones(String indicadorOperaciones) {
		this.indicadorOperaciones = indicadorOperaciones;
	}

	/**
	 * @return the especialidadUniAgenda
	 */
	public String getEspecialidadUniAgenda() {
		return especialidadUniAgenda;
	}

	/**
	 * @param especialidadUniAgenda the especialidadUniAgenda to set
	 */
	public void setEspecialidadUniAgenda(String especialidadUniAgenda) {
		this.especialidadUniAgenda = especialidadUniAgenda;
	}

	/**
	 * @return the nomEspecialidadUniAgenda
	 */
	public String getNomEspecialidadUniAgenda() {
		return nomEspecialidadUniAgenda;
	}

	/**
	 * @param nomEspecialidadUniAgenda the nomEspecialidadUniAgenda to set
	 */
	public void setNomEspecialidadUniAgenda(String nomEspecialidadUniAgenda) {
		this.nomEspecialidadUniAgenda = nomEspecialidadUniAgenda;
	}

	public void setTieneHorarioAsignado(boolean tieneHorarioAsignado) {
		this.tieneHorarioAsignado=tieneHorarioAsignado;
	}

	public boolean isTieneHorarioAsignado() {
		return tieneHorarioAsignado;
	}

	/**
	 * Método que se encarga de obtener el 
	 * valor del atributo  utilizaProgramasOdonto
	 *
	 * @return retorna la variable utilizaProgramasOdonto
	 */
	public String getUtilizaProgramasOdonto() {
		return utilizaProgramasOdonto;
	}

	/**
	 * Método que se encarga de establecer el valor
	 * del atributo utilizaProgramasOdonto
	 * @param utilizaProgramasOdonto es el valor para el atributo utilizaProgramasOdonto 
	 */
	public void setUtilizaProgramasOdonto(String utilizaProgramasOdonto) {
		this.utilizaProgramasOdonto = utilizaProgramasOdonto;
	}

	/**
	 * Método que se encarga de obtener el 
	 * valor del atributo  codigoPrograma
	 *
	 * @return retorna la variable codigoPrograma
	 */
	public String getCodigoPrograma() {
		return codigoPrograma;
	}

	/**
	 * Método que se encarga de establecer el valor
	 * del atributo codigoPrograma
	 * @param codigoPrograma es el valor para el atributo codigoPrograma 
	 */
	public void setCodigoPrograma(String codigoPrograma) {
		this.codigoPrograma = codigoPrograma;
	}

	/**
	 * Método que se encarga de obtener el 
	 * valor del atributo  nombrePrograma
	 *
	 * @return retorna la variable nombrePrograma
	 */
	public String getNombrePrograma() {
		return nombrePrograma;
	}

	/**
	 * Método que se encarga de establecer el valor
	 * del atributo nombrePrograma
	 * @param nombrePrograma es el valor para el atributo nombrePrograma 
	 */
	public void setNombrePrograma(String nombrePrograma) {
		this.nombrePrograma = nombrePrograma;
	}

	/**
	 * Método que se encarga de obtener el 
	 * valor del atributo  codigoProgramaMostrar
	 *
	 * @return retorna la variable codigoProgramaMostrar
	 */
	public String getCodigoProgramaMostrar() {
		return codigoProgramaMostrar;
	}

	/**
	 * Método que se encarga de establecer el valor
	 * del atributo codigoProgramaMostrar
	 * @param codigoProgramaMostrar es el valor para el atributo codigoProgramaMostrar 
	 */
	public void setCodigoProgramaMostrar(String codigoProgramaMostrar) {
		this.codigoProgramaMostrar = codigoProgramaMostrar;
	}

	/**
	 * Método que se encarga de obtener el 
	 * valor del atributo  listaCodigoProgramaServicios
	 *
	 * @return retorna la variable listaCodigoProgramaServicios
	 */
	public String getListaCodigoProgramaServicios() {
		return listaCodigoProgramaServicios;
	}

	/**
	 * Método que se encarga de establecer el valor
	 * del atributo listaCodigoProgramaServicios
	 * @param listaCodigoProgramaServicios es el valor para el atributo listaCodigoProgramaServicios 
	 */
	public void setListaCodigoProgramaServicios(String listaCodigoProgramaServicios) {
		this.listaCodigoProgramaServicios = listaCodigoProgramaServicios;
	}
	
}