
/*
 * Creado   29/11/2004
 *
 *Copyright Princeton S.A. &copy;&reg; 2004. Todos los Derechos Reservados.
 *
 * Lenguaje		:Java
 * Compilador	:J2SDK 1.4.2_04
 */
package com.princetonsa.actionform.pooles;

import java.util.Collection;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import util.ConstantesBD;
import util.UtilidadFecha;


/**
 * Contiene todos los datos asociados con médicos por pool. 
 * Y adicionalmente hace el manejo de reset dela forma y 
 * de validación de errores de datos de entrada.
 *
 * @version 1.0, 29/11/2004
 * @author <a href="mailto:joan@PrincetonSA.com">Joan L&oacute;pez</a>
 */
public class MedicosXPoolForm extends ActionForm 
{
    /**
	 * almacena el log
	 */
	private String log;
	
	/**
	* Estado en el que se encuentra el proceso.       
	*/
	private String estado;
	
	/**
	 * Almacena el código del pool.
	 */
	private int codigoPool;
	
	/**
	 * 
	 */
	private int codigoMedico;
	
	/**
	 * para la navegacion en el pager y mantener los datos
	 */
	private String linkSiguiente;
	
	/**
	 * contiene el numero de registros dentro del hashmap
	 */
	private int numeroElementos;
	
	/**
	 * contiene el numero de registros dentro del hashmap que 
	 * estan en la BD. 
	 */
	private int numElementosBD;

	/**
	 * Almacena la posición del elemento en el hashMap 
	 * que es ingresado como nuevo.
	 */
	private int posActual;
	
	/**
	 * Con los datos de la consulta
	 */
	private Collection colMedicosPool;
	
	/**
	 * Almacena los datos de la forma y BD
	 */
	private HashMap mapPool;
	
	/**
	 * Almacena los datos de la BD
	 */
	private HashMap mapInfo;
	
	/**
	 * Almacena datos generales
	 */
	private HashMap mapFiltros;
	
	
	
	public boolean esPrimero;
	
	
	private int offset ;
	
	private String accion;
	
	/**
	 * 
	 */
	private boolean busqueda;
	
	/**
	    * limpiar e inicializar atributos de la clase
	    *
	    */
	   public void reset ()
	   {
	       this.log = "";	 
	       this.codigoPool = -1;
	       this.codigoMedico = 0;
	       this.colMedicosPool = null;
	       this.linkSiguiente = "";
	       this.esPrimero = true;
	       this.offset=0;
	       this.accion = "";
	       this.busqueda=false;
	   }
	   
	   /**
	    * Inicializar los HashMaps
	    *
	    */
	   public void resetMaps (boolean restriccion)
	   {
	     this.mapPool = new HashMap();
	     this.mapInfo = new HashMap();
	     this.numeroElementos = 0;
	     this.numElementosBD = 0;
	     this.posActual = 0;
	     
	     if(restriccion)
	     {
	         this.mapFiltros = new HashMap();
	     }
	   }
	   
	   
	   /**
		 * Validate the properties that have been set from this HTTP request, and
		 * return an <code>ActionErrors</code> object that encapsulates any
		 * validation errors that have been found.  If no errors are found, return
		 * <code>null</code> or an <code>ActionErrors</code> object with no recorded
		 * error messages.
		 *
		 * @param mapping The mapping used to select this instance
		 * @param request The servlet request we are processing
		*/
	   public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	   {
	       ActionErrors errores= new ActionErrors();
	              
	       boolean hayError = false,hayErrorFecha = false,fechaMenor = false,fechaFormato = false;
	       boolean existeErrorPorcentaje = false, existeErrorFecha = false;
	       boolean noExisteErrorPorcentaje = true,noExisteErrorMedico=true;
	       int posNoComparar = 0;
	       String nombreMedico="";
	       
	       if(this.estado.equals("ingresarNuevo") || this.estado.equals("salirGuardar")) //validaciones de las fechas, formatos validos y que la fecha
	       {																			//de retiro sea posterior a la de ingreso
	           for(int i = 0; i < this.getNumeroElementos() ; i ++)
	           {
	               int registro = i;
	               registro ++;
	               
	               if((mapPool.get("fechaIngreso_"+i)+"").equals(""))
	               {
	                   errores.add("", new ActionMessage("errors.required","la fecha de ingreso para el registro "+registro));
	                   existeErrorFecha = true;
	               }
	              	               
	               if(!(mapPool.get("fechaIngreso_"+i)+"").equals(""))
	               {
	                  
	                   fechaFormato = UtilidadFecha.validarFecha(mapPool.get("fechaIngreso_"+i)+"");
	                   
	                   if(!fechaFormato)
			           {
			               errores.add("", new ActionMessage("errors.formatoFechaInvalido",mapPool.get("fechaIngreso_"+i)+""));
			               hayErrorFecha = true;
			               existeErrorFecha = true;
			           }
	               }
	               
	               if(!(mapPool.get("fechaRetiro_"+i)+"").equals(""))
	               {
	                   fechaFormato = UtilidadFecha.validarFecha(mapPool.get("fechaRetiro_"+i)+"");
	                   
	                   if(!fechaFormato)
			           {
			               errores.add("", new ActionMessage("errors.formatoFechaInvalido",mapPool.get("fechaRetiro_"+i)+"")); 
			               hayErrorFecha = true;
			               existeErrorFecha = true;
			           }
	               }
	               
	               if(!hayErrorFecha)
	               {
		               if(!(mapPool.get("fechaRetiro_"+i)+"").equals("") && !(mapPool.get("fechaIngreso_"+i)+"").equals(""))
		               {
			               fechaMenor = UtilidadFecha.esFechaMenorQueOtraReferencia(mapPool.get("fechaRetiro_"+i)+"",mapPool.get("fechaIngreso_"+i)+"");
			               
			               if(fechaMenor)
				           {
				               errores.add("", new ActionMessage("errors.fechaAnteriorAOtraDeReferencia",mapPool.get("fechaRetiro_"+i)+"",mapPool.get("fechaIngreso_"+i)+""));
				               existeErrorFecha = true;
				           }
		               }
	               }
	           }
	                 
	       }
	       
	      
	       if(this.estado.equals("salirGuardar") || this.estado.equals("ingresarNuevo") || this.estado.equals("busquedaAvanzada") ) 
	       {																		  
	          
	          if(this.getCodigoPool() == -1)
	           {
	               errores.add("", new ActionMessage("errors.required","El pool ")); 
	           }
	       }
	          
	       
	      if(this.estado.equals("salirGuardar") || this.estado.equals("ingresarNuevo") || this.estado.equals("busquedaAvanzada")) //validaciones de campos requeridos, formatos de los
	       {																		   //campos de porcentaje 
	           for(int k = 0; k < this.getNumeroElementos() ; k ++)
	           {
	               int registro = k;
	               registro ++;
	               
	               try
	               {
		               if(Integer.parseInt(mapPool.get("medico_"+k)+"") == -1)
			           {
			               errores.add("", new ActionMessage("errors.required","El médico para el registro " + registro)); 
			               noExisteErrorMedico=false;
			           }
	               }
	               catch(NumberFormatException e)
	               {
	                   errores.add("", new ActionMessage("errors.required","El médico para el registro " + registro));
	                   noExisteErrorMedico=false;
	               }
	               
	               if( String.valueOf(mapPool.get("porcentaje_"+k)).equals(""))
	               {
	                   errores.add("", new ActionMessage("errors.required","El porcentaje para el registro " + registro));
	                   hayError = true;
	                   existeErrorPorcentaje = true;
	                   noExisteErrorPorcentaje = false;
	               }
	               
	               if(!hayError)
	               {
		              try
		               {
		                   if(Integer.parseInt(mapPool.get("porcentaje_"+k)+"") < 0)
		                   {
		                      errores.add("numero menor/igual que 0", new ActionMessage("errors.integerMayorIgualQue","El porcentaje para el registro " + registro,"0"));
		                      existeErrorPorcentaje = true;
		                      noExisteErrorPorcentaje = false;
		                   }
		               }
		               catch(NumberFormatException e)
		               {
		                   errores.add("", new ActionMessage("errors.float","El porcentaje para el registro " + registro));
		                   existeErrorPorcentaje = true;
		                   noExisteErrorPorcentaje = false;
		               }               
		               
	               }
	               
	           }           
	          
	       }
	      
	      if( ( this.estado.equals("salirGuardar") || this.estado.equals("ingresarNuevo") ) && !existeErrorPorcentaje)
	      {																												 //validación del 100%		
	          double sumaValidacion =  0;
	          double sumaPorcentajesNew = 0,sumaPorcentajesOld = 0;
	          
	          if(this.getCodigoPool() != -1)
	          {
		          for(int l = 0; l < this.getNumeroElementos() ; l ++)//para validar que si se modifica un porcentaje se 
		          {													//sume el actual y se reste el anterior
		              if(! (mapPool.get("fechaRetiro_"+l)+"").equals("") )
	                  {
			              fechaMenor = UtilidadFecha.esFechaMenorQueOtraReferencia(UtilidadFecha.conversionFormatoFechaAAp(mapPool.get("fechaRetiro_"+l)+""),UtilidadFecha.getFechaActual());
		                  if( !fechaMenor )  
		                  {
				              if( (mapFiltros.get("tipoRegistro_"+l)+"").equals("BD") )  
				              {
					              /*if( !(mapInfo.get("porcentaje_"+l)+"").equals(mapPool.get("porcentaje_"+l)+"") )
						           {
					                  restaModificado = restaModificado + Double.parseDouble(mapInfo.get("porcentaje_"+l)+"");
					                  sumaModificado = sumaModificado + Double.parseDouble(mapPool.get("porcentaje_"+l)+"");
						           }
					              else*/
					               sumaPorcentajesOld = sumaPorcentajesOld + Double.parseDouble(mapPool.get("porcentaje_"+l)+"");
				              }
				              if( (mapFiltros.get("tipoRegistro_"+l)+"").equals("nuevo") && !(mapPool.get("porcentaje_"+l)+"").equals("") && !(mapPool.get("porcentaje_"+l)+"").equals("null"))  
				              {
				                  sumaPorcentajesNew = sumaPorcentajesNew +  Double.parseDouble(mapPool.get("porcentaje_"+l)+"");
				              }
		                  }
	                  }
		              else if( (mapPool.get("fechaRetiro_"+l)+"").equals("") )
		              {
			              if( (mapFiltros.get("tipoRegistro_"+l)+"").equals("BD") )  
			              {
				             /* if( !(mapInfo.get("porcentaje_"+l)+"").equals(mapPool.get("porcentaje_"+l)+"") )
					           {
				                  restaModificado = restaModificado + Double.parseDouble(mapInfo.get("porcentaje_"+l)+"");
				                  sumaModificado = sumaModificado + Double.parseDouble(mapPool.get("porcentaje_"+l)+"");
					           }
				              else*/
				               sumaPorcentajesOld = sumaPorcentajesOld + Double.parseDouble(mapPool.get("porcentaje_"+l)+"");
			              }
			              if( (mapFiltros.get("tipoRegistro_"+l)+"").equals("nuevo") && !(mapPool.get("porcentaje_"+l)+"").equals("") && !(mapPool.get("porcentaje_"+l)+"").equals("null"))  
			              {
			                  sumaPorcentajesNew = sumaPorcentajesNew +  Double.parseDouble(mapPool.get("porcentaje_"+l)+"");
			              }
		              }
		          }
		          
		          //sumaValidacion = sumaPorcentajesNew + sumaPorcentajesOld + sumaModificado - restaModificado;
		          sumaValidacion = sumaPorcentajesNew + sumaPorcentajesOld;
		          if(Math.abs(sumaValidacion) > 100)
		          {
		            errores.add("", new ActionMessage("error.pooles.porcentajeSuperior"));
		          }
	          }        
	       }
	      
	      if( this.estado.equals("ingresarNuevo") )	//validación para saber si el médico ya existe y no ha vencido
	       {										//la fecha de retiro ó si no tiene fecha de retiro, cada ves que se ingresa un nuevo registro
	          int posHash = 0;
	          
	          if(!(mapInfo.get("numRegistros")+"").equals("null"))
	          {
	             posHash=this.getPosActual();
	             
	          }
	          
	               if( !(mapPool.get("fechaIngreso_"+posHash)+"").equals("") )
	               {
	                 if(!(mapPool.get("fechaRetiro_"+posHash)+"").equals("null") && !(mapPool.get("fechaIngreso_"+posHash)+"").equals("null"))
	                 {
	                   this.setCodigoMedico(Integer.parseInt(mapPool.get("medico_"+posHash)+""));                   
	                   
	                   for(int i=0; i < posHash; i ++)
	                   {
	                    if( this.getCodigoMedico() == Integer.parseInt(mapPool.get("medico_"+i)+"") )
		                {
	                      fechaMenor = false;
	                      nombreMedico+=mapPool.get("primerApellido_"+i)+" "+mapPool.get("segundoApellido_"+i)+", "+mapPool.get("primerNombre_"+i)+" "+mapPool.get("segundoNombre_"+i);
			              if(!(mapPool.get("fechaRetiro_"+i)+"").equals(""))
			              {
			               fechaMenor = UtilidadFecha.esFechaMenorQueOtraReferencia(UtilidadFecha.conversionFormatoFechaAAp(mapPool.get("fechaRetiro_"+i)+""),UtilidadFecha.getFechaActual());
			               		               
			               if(!fechaMenor)
						   {
				            errores.add("", new ActionMessage("error.pooles.medicoRegistrado",nombreMedico+"")); 
						   }
			              }
			              if((mapPool.get("fechaRetiro_"+i)+"").equals(""))
			              {
			                  int numReg = i;
			                  numReg ++;
			                  errores.add("", new ActionMessage("error.pooles.medicoRegistrado",nombreMedico+"")); 
			              }
			               
		                }
	                   }
	                 }
	               }
	       }
	       
	      if( this.estado.equals("salirGuardar") )
	      {
	          double sumaPorcentajes = 0;
	          hayError = false;
	          int contProfesionalesRetirados=ConstantesBD.codigoNuncaValido;
	          if(noExisteErrorPorcentaje)//si no existen errores en el campo porcentaje, para la conversion a float
	          {
		          for(int l = 0; l < this.getNumeroElementos() ; l ++)//para validar que si se modifica un porcentaje se 
		          {		
		              if(! (mapPool.get("fechaRetiro_"+l)+"").equals("") )
	                  {
		                  fechaMenor = UtilidadFecha.esFechaMenorQueOtraReferencia(UtilidadFecha.conversionFormatoFechaAAp(mapPool.get("fechaRetiro_"+l)+""),UtilidadFecha.getFechaActual());
		                  if( !fechaMenor )
			                  sumaPorcentajes = sumaPorcentajes + Double.parseDouble(mapPool.get("porcentaje_"+l)+"");
                          else                             
                              contProfesionalesRetirados ++;//contar los profesionales retirados del pool                         
	                  }
		              else if( (mapPool.get("fechaRetiro_"+l)+"").equals("") )
		              {
		                  sumaPorcentajes = sumaPorcentajes + Double.parseDouble(mapPool.get("porcentaje_"+l)+"");
		              }
		          }
	          }
	          
	          if(noExisteErrorMedico && noExisteErrorPorcentaje && !existeErrorFecha)
	          {		          
                  if(contProfesionalesRetirados<this.getNumeroElementos()-1)//si todos los profesionales del pool fueron retirados, no realizar la validación del porcentaje del pool = 100%
                      if(sumaPorcentajes < 100)
    		          {
    		            errores.add("", new ActionMessage("error.pooles.porcentajeInferior"));
    		          }	   
	          }
	          
	          
	          
	          if(!existeErrorFecha)// si no hay errores de fechas, se realiza la validación, para evitar los null
	          {
		          for(int l = 0; l < this.getNumeroElementos() ; l ++)//para validar AL SALIR que no se repita un médico que aun este vigente
		          {		
		              this.setCodigoMedico(Integer.parseInt(mapPool.get("medico_"+l)+""));
		              posNoComparar = l;
		              
		              for(int k = 0; k < this.getNumeroElementos() ; k ++)
			          {		
		                  if( this.getCodigoMedico() == Integer.parseInt(mapPool.get("medico_"+k)+"") && posNoComparar != k)//que los codigos de los
			              {																									//medicos sean iguales y
		                      nombreMedico+=mapPool.get("primerApellido_"+k)+" "+mapPool.get("segundoApellido_"+k)+", "+mapPool.get("primerNombre_"+k)+" "+mapPool.get("segundoNombre_"+k);											
		                      if(! (mapPool.get("fechaRetiro_"+k)+"").equals("") )
		                      {
			                      fechaMenor = UtilidadFecha.esFechaMenorQueOtraReferencia(UtilidadFecha.conversionFormatoFechaAAp(mapPool.get("fechaRetiro_"+k)+""),UtilidadFecha.getFechaActual());
			                  
			                      if(!fechaMenor)
								   {
			                          errores.add("", new ActionMessage("error.pooles.medicoRegistrado",nombreMedico+""));
			                          hayError = true;
								   }
		                      }
		                      else if( (mapPool.get("fechaRetiro_"+k)+"").equals("") )
				              {
		                          errores.add("", new ActionMessage("error.pooles.medicoRegistrado",nombreMedico+""));
		                          hayError = true;
				              }	                     
			              }
		                  if( hayError )
	                          break ;
			          }		 
		              if( hayError )
                          break ;
		          }
	          }
	          
	          
	      }
	      
	      if(!errores.isEmpty())
	       {
	           this.setEstado("redireccion");
	       }
	      
	       return errores;	      
	   }
	   
	   
    /**
     * @return Retorna estado.
     */
    public String getEstado() {
        return estado;
    }
    /**
     * @param estado Asigna estado.
     */
    public void setEstado(String estado) {
        this.estado = estado;
    }
    /**
     * @return Retorna log.
     */
    public String getLog() {
        return log;
    }
    /**
     * @param log Asigna log.
     */
    public void setLog(String log) {
        this.log = log;
    }
    /**
     * @return Retorna codigoPool.
     */
    public int getCodigoPool() {
        return codigoPool;
    }
    /**
     * @param codigoPool Asigna codigoPool.
     */
    public void setCodigoPool(int codigoPool) {
        this.codigoPool = codigoPool;
    }
    /**
     * @return Retorna colMedicosPool.
     */
    public Collection getColMedicosPool() {
        return colMedicosPool;
    }
    /**
     * @param colMedicosPool Asigna colMedicosPool.
     */
    public void setColMedicosPool(Collection colMedicosPool) {
        this.colMedicosPool = colMedicosPool;
    }
    
    /**
     * @return Retorna codigoMedico.
     */
    public int getCodigoMedico() {
        return codigoMedico;
    }
    /**
     * @param codigoMedico Asigna codigoMedico.
     */
    public void setCodigoMedico(int codigoMedico) {
        this.codigoMedico = codigoMedico;
    }
    /**
     * @return Retorna mapPool.
     */
    public HashMap getMapPool() {
        return mapPool;
    }
    /**
     * @param mapPool Asigna mapPool.
     */
    public void setMapPool(HashMap mapPool) {
        this.mapPool = mapPool;
    }
    
    /**
	 * Set del medicos por pool
	 * @param key
	 * @param value
	 */

	public void setMapPool(String key, Object value) 
	{
	    mapPool.put(key, value);
	}

	/**
	 * Get del mapa de medicos por pool
	 * Retorna el valor de un campo dado su nombre
	 */
	public Object getMapPool(String key) 
	{
		return mapPool.get(key);
	}
    /**
     * @return Retorna linkSiguiente.
     */
    public String getLinkSiguiente() {
        return linkSiguiente;
    }
    /**
     * @param linkSiguiente Asigna linkSiguiente.
     */
    public void setLinkSiguiente(String linkSiguiente) {
        this.linkSiguiente = linkSiguiente;
    }
    /**
     * @return Retorna numeroElementos.
     */
    public int getNumeroElementos() {
        return numeroElementos;
    }
    /**
     * @param numeroElementos Asigna numeroElementos.
     */
    public void setNumeroElementos(int numeroElementos) {
        this.numeroElementos = numeroElementos;
    }
    /**
     * @return Retorna mapInfo.
     */
    public HashMap getMapInfo() {
        return mapInfo;
    }
    /**
     * @param mapInfo Asigna mapInfo.
     */
    public void setMapInfo(HashMap mapInfo) {
        this.mapInfo = mapInfo;
    }
    
    /**
	 * Set del medicos por pool
	 * @param key
	 * @param value
	 */

	public void setMapInfo(String key, Object value) 
	{
	    mapInfo.put(key, value);
	}

	/**
	 * Get del mapa de medicos por pool
	 * Retorna el valor de un campo dado su nombre
	 */
	public Object getMapInfo(String key) 
	{
		return mapInfo.get(key);
	}
    /**
     * @return Retorna numElementosBD.
     */
    public int getNumElementosBD() {
        return numElementosBD;
    }
    /**
     * @param numElementosBD Asigna numElementosBD.
     */
    public void setNumElementosBD(int numElementosBD) {
        this.numElementosBD = numElementosBD;
    }
    /**
     * @return Retorna mapFiltros.
     */
    public HashMap getMapFiltros() {
        return mapFiltros;
    }
    /**
     * @param mapFiltros Asigna mapFiltros.
     */
    public void setMapFiltros(HashMap mapFiltros) {
        this.mapFiltros = mapFiltros;
    }
    
    /**
	 * Set del medicos por pool
	 * @param key
	 * @param value
	 */

	public void setMapFiltros(String key, Object value) 
	{
	    mapFiltros.put(key, value);
	}

	/**
	 * Get del mapa de medicos por pool
	 * Retorna el valor de un campo dado su nombre
	 */
	public Object getMapFiltros(String key) 
	{
		return mapFiltros.get(key);
	}
    /**
     * @return Retorna posActual.
     */
    public int getPosActual() {
        return posActual;
    }
    /**
     * @param posActual Asigna posActual.
     */
    public void setPosActual(int posActual) {
        this.posActual = posActual;
    }
    /**
     * @return Retorna offset.
     */
    public int getOffset() {
        return offset;
    }
    /**
     * @param offset Asigna offset.
     */
    public void setOffset(int offset) {
        this.offset = offset;
    }
    /**
     * @return Retorna accion.
     */
    public String getAccion() {
        return accion;
    }
    /**
     * @param accion Asigna accion.
     */
    public void setAccion(String accion) {
        this.accion = accion;
    }

	public boolean getBusqueda() {
		return busqueda;
	}

	public void setBusqueda(boolean busqueda) {
		this.busqueda = busqueda;
	}
}
