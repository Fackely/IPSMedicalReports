
/*
 * Creado   8/12/2005
 *
 *Copyright Princeton S.A. &copy;&reg; 2004. Todos los Derechos Reservados.
 *
 * Lenguaje		:Java
 * Compilador	:J2SDK 1.5.0_03
 */
package com.princetonsa.actionform.inventarios;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.inventarios.ConstantesBDInventarios;

import com.princetonsa.mundo.inventarios.RegistroTransacciones;

/**
 * 
 *
 * @version 1.0, 8/12/2005
 * @author <a href="mailto:joan@PrincetonSA.com">Joan L&oacute;pez</a>
 */
public class RegistroTransaccionesForm extends ActionForm 
{
    private RegistroTransacciones registroTransacciones= new RegistroTransacciones();
	
	/** 
     * estado del workflow
     */
    private String estado;
    private String accion;
    /**
     * código del almacen
     */
    private int codAlmacen;
    
    /**
    * 
    */
   private String parejasClaseGrupo; 
    /**
     * nombre del almacen
     */
    private String nombreAlmacen;
    /**
     * código de la transacción valida
     * por centro de costo
     */
    private int codTransaccion;    
    /**
     * nombre del tipo de transaccion
     */
    private String nombreTransaccion;
    /**
     * Indicativo de consignacion
     */
    private String indicativo_consignacion;
    /**
     * fecha de elaboración de la transacción
     */
    private String fechaElaboracion;
    /**
     * true si la fecha de elaboración
     * se puede modificar
     */
    private String esFechaModificable;
    /**
     * almacena el login del usuario
     */
    private String usuarioElabora;    
    /**
     * contiene el login del usuario
     */
    private String loginUsuario;   
    /**
     * Codigo del estado de la transaccion.
     */
    private int codEstadoTrans;   
    /**
     * observaciones
     */
    private String observaciones;
    /**
     * observaciones
     */
    private String observacionesOld;
    
    /**
     * 
     */
    private HashMap entidades;
    /**
     * almacena el estado del detalle
     */
    private String cargarDetalle;
    /**
     * numero de registros del mapa de articulos
     */
    private int numeroFilasMapa;
    /**
     * almacena el listado de los articulos
     * consultados
     */
    private HashMap mapaArticulos;
    /**
     * almacena datos que se necesitan conservar
     * para trabajar en la forma, y asi no declarar
     * atributos por cada uno
     */
    private HashMap mapaAtributos;
    /**
     * mapa que contiene el listado de almacenes validos
     */
    private HashMap mapAlmacenes;
    /**
     * mapa que contiene el listado de transacciones 
     * validas por centro de costo
     */
    private HashMap mapTransXCC;
    /**
     * mapa que contiene el listado de transacciones 
     * validas por centro de costo para la busqueda
     */
    private HashMap mapTransXCCBusqueda;
    /**
     * Mapa que contien el listado de transacciones arrojado en la busqueda.
     */
    private HashMap listadoTrans;
    /**
     * para manejar el pager
     */
    private int offset;
    /**
     * almacena mensajes de advertencias
     */
    private HashMap warnings;
    /**
     * almacena la connection de la transacción
     * general
     */
    private Connection conTrans;
    /**
     * estado de la trnasaccion para el
     * detalle y sus validaciones.
     */
    private boolean enTransaccion;
    
    /**
     * 
     */
    private String centroAtencion;
    /**
     * indicativo consignacion de almacen
     */
    private String tipo_consignac;
    
    
    /*************PARAMETROS BUSQUEDA AVANZADA**************/
    /**
     * El numero de la transaccion inicial para la busqueda.
     */
    private int numTransInicial;
    /**
     * El numero de la transaccion final para la busqueda.
     */
    private int numTransFinal;
    /**
     * Fecha elaboracion Inicial.
     */
    private String fechaElaboraInicial;
    /**
     * Fecah elaboracion Final.
     */
    private String fechaElaboraFinal;
    /**
     * Fecha de cierre inicial.
     */
    private String fechaCierreInicial;
    /**
     * Fecha de cierre Final.
     */
    private String fechaCierreFinal;
    /**
     * Usuario que cierra la transaccion
     */
    private String usuarioCierra;
    
    /**
     * Variable para maneja el numero de registros que se muestran en el pager.
     */
    private int maxPageItem;
    
    
    /**
     * Patron por el que se desea ordenar el listado
     */
    private String patronOrdenar;
    
    /**
     * Ultimo Patron por el que se ordena.
     */
    private String ultimoPatron;
    
    /**
     * El indice de la transaccion del listado
     * de transacciones de la cual se desea
     * conocer el detalle.
     */
    private int indiceTrans;
    
    
    /**
     * 
     */
    private ArrayList articulosEliminados;
    
    /**
     * Boolean que me indica si es cierre de transaccion . solo se esta usando en el resumen para mirar si se imprime o no.
     */
    private boolean cerrarRegistroTransaccion;
    
    /**
     * Almacena el numero de la transaccion que se acaba de generar, solo se usa para la impresion del resumen de la generacion.
     */
    private String numeroTransaccion;
    
    /**
     * 
     */
    private boolean procesoExitoso;
    
    /**
     * 
     */
    private String numeroTransaccionResumen;
    
    /**
     * PARAMETROS PARA LA BUSQUEDA DE LA ENTIDAD EN LA TABLA TERCEROS
     */
    /**
     * 
     */
    private String codigoEntidad;
    /**
     * 
     */
    private String descripcionEntidad;
    /**
     * 
     */
    private Collection resultados;
    /**
     * codigo del tipo de transaccion para buscar en la tabla tipos_conceptos_inv
     */
    private String codTipo;
    /**
     * descripcion del tipo de transaccion para buscar en la tabla tipos_conceptos_inv
     */
    private String descTipo;
    
    /**
     * variable para alojar el criterio de busqueda de articulo
     */
    private String criterioBusqueda;
    
    /**
     * indice que maneja los tipos de transacciones
     */
    private String indice;
    
    /**
     * Variable en donde se almacenan los valores escogidos en el select de transacción
     */
    private String selectTransaccion ="";
    
    /**
     * Variable que indica si viene desde el flujo de la busqueda de transacciones
     */
    private boolean desdeBusqueda;
    
    /**
     * inicializar atributos de esta forma     
     */
    public void reset ()
    {   
       this.codAlmacen=ConstantesBD.codigoNuncaValido; 
       this.nombreAlmacen="";
       this.parejasClaseGrupo="";
       this.mapAlmacenes=new HashMap();
       this.mapTransXCC=new HashMap();       
       this.fechaElaboracion="";
       this.usuarioElabora="";       
       this.esFechaModificable="";
       this.loginUsuario="";
       this.listadoTrans=new HashMap();
       this.listadoTrans.put("numRegistros","0");
       this.entidades=new HashMap();
       this.entidades.put("numRegistros","0");
       this.codEstadoTrans=ConstantesBD.codigoNuncaValido;       
       this.numTransInicial=0;
       this.numTransFinal=0;
       this.fechaElaboraInicial=UtilidadFecha.getFechaActual();
       this.fechaElaboraFinal=UtilidadFecha.getFechaActual();
       this.fechaCierreInicial=UtilidadFecha.getFechaActual();
       this.fechaCierreFinal=UtilidadFecha.getFechaActual();
       this.usuarioCierra="";       
       this.observaciones="";
       this.observacionesOld="";
       this.cargarDetalle="hidden";
       this.numeroFilasMapa=0;
       this.mapaArticulos=new HashMap();              
       //this.codTransaccion=ConstantesBD.codigoNuncaValido;
       //this.nombreTransaccion="Seleccione";
       this.tipo_consignac="";
       this.indicativo_consignacion="";
       this.mapaAtributos=new HashMap();
       this.resetMapAtributos();
       this.maxPageItem=20;
       this.patronOrdenar="";
       this.ultimoPatron="";
       this.offset=0;
       this.indiceTrans=ConstantesBD.codigoNuncaValido;
       this.mapTransXCCBusqueda=new HashMap();
       UtilidadBD.closeConnection(this.conTrans);
       this.conTrans=null;
       this.enTransaccion=true;
       this.articulosEliminados=new ArrayList();
       this.cerrarRegistroTransaccion=false;
       this.numeroTransaccion="";
       this.procesoExitoso=false;
       this.numeroTransaccionResumen="";
       this.centroAtencion="";
       this.codigoEntidad="";
       this.descripcionEntidad="";
       this.resultados=new ArrayList();
       this.codTipo="";
       this.descTipo="";
       this.criterioBusqueda="";
       this.setIndice("");
       this.desdeBusqueda=false;
    }
    
    /**
     * Inicializar atributos de la transacción
     */
    public void resetTransaccion()
    {
    	this.codTransaccion=ConstantesBD.codigoNuncaValido;
        this.nombreTransaccion="Seleccione";
    }
    /**
     * inicializar atributos del mapa
     **/
    private void resetMapAtributos()
    {
        this.mapaAtributos.put("esCerrarTransaccion","false");
        this.mapaAtributos.put("linkSiguiente","");
        this.mapaAtributos.put("posArticuloEliminar","-1");  
        this.mapaAtributos.put("existeError","false");
        this.mapaAtributos.put("indicativo_consignacion","");
        this.mapaAtributos.put("tipo_consignac","");
        this.mapaAtributos.put("codAlmacenBusqueda",ConstantesBD.codigoNuncaValido+"");
        this.mapaAtributos.put("nomAlmacenBusqueda","");
        this.mapaAtributos.put("nombreEntidad","Seleccione");
        this.mapaAtributos.put("codigoEntidad","-1");
        /**motivo de la anulacion de la transacción*/
        this.mapaAtributos.put("motivoAnulacion","");
        /**almacena un estado para saber si la transaccion se puede modificar*/
        this.mapaAtributos.put("transaccionModificable","modificable");
        /**almacena el codigo de la transacción insertada*/
        this.mapaAtributos.put("codigoPKTransaccion","");
        /**almacena el valor del consecutivo*/
        this.mapaAtributos.put("valorConsecutivo",ConstantesBD.codigoNuncaValido+"");
        /**almacena el codigo de la transaccion valida por centro de costo*/
        this.mapaAtributos.put("codigoTransValidaXCC",ConstantesBD.codigoNuncaValido+"");
        /**código del concepto del tipo de transacción(Entrada-Salida)*/
        this.mapaAtributos.put("codigoTipoConceptoTransaccion",ConstantesBD.codigoNuncaValido+"");
        /**nombre del concepto del tipo de transacción(Entrada-Salida)*/
        this.mapaAtributos.put("nombreTipoConceptoTransaccion","");
        /**almacena el estado para mostrar los mensajes de advertencia de valor costo de articulos*/
        this.mapaAtributos.put("existeWarningValorCosto","false");
        /**validacion de existencias*/
        this.mapaAtributos.put("validacionUnoRealizada","false");
        /**validacion asignacion de costos*/
        this.mapaAtributos.put("validacionDosRealizada","false");
        /**validacion generación de costo promedio*/
        this.mapaAtributos.put("validacionTresRealizada","false");
        /**validacion de la fecha de cierre de inventarios*/
        this.mapaAtributos.put("validacionCuatroRealizada","false");
        /**validacion del stock minimo,stock maximo,punto de pedido*/
        this.mapaAtributos.put("validacionCincoRealizada","false");
    }    
    /**
     * reset para objectos que se deben inicializar entre operaciones del workflow
     **/
    public void resetObjetosTransaccionales(boolean limpiarWarning)
    {
        this.mapTransXCCBusqueda=new HashMap(); 
        this.listadoTrans=new HashMap();
        this.mapTransXCC=new HashMap();  
        this.mapaArticulos=new HashMap();
        UtilidadBD.closeConnection(this.conTrans);
        this.conTrans=null;
        if(limpiarWarning)
            this.warnings=new HashMap();            
    }    
    /**
	 * Metodo de validación
	 * @param mapping
	 * @param request
	 * @return errores ActionError, especifica los errores.
	 */
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request,ActionForm form) 
	{	
	    ActionErrors errores = new ActionErrors();
	    boolean existeError=false;
	    //por alguna razon en suba el estado esta llegando null.
	    if(this.estado==null)
	    {
	    	this.estado="";
	    }
	    if(this.estado.equals("continuarTransaccion"))
	    {
	        if(this.codTransaccion==ConstantesBD.codigoNuncaValido)
	        {
	            errores.add("falta campo", new ActionMessage("errors.required","LA TRANSACCION"));
	        }
	        
	        if(this.selectTransaccion.equals(ConstantesBD.codigoNuncaValido+"") && !desdeBusqueda)
	        {
	        	errores.add("falta campo", new ActionMessage("errors.required","La transacción "));
	        }
	        if(this.fechaElaboracion.equals(""))
	        {
	            errores.add("falta campo", new ActionMessage("errors.required","LA FECHA DE ELABORACION"));
	        }
	        if(!UtilidadFecha.validarFecha(this.fechaElaboracion))
	        {
	            errores.add("formato fecha invalido", new ActionMessage("errors.formatoFechaInvalido","de elaboración"));
	            existeError=true;
	        }
	        if(!existeError)
		        if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.fechaElaboracion,UtilidadFecha.getFechaActual()))
		        {
		            errores.add("fecha anterior", new ActionMessage("errors.fechaPosteriorIgualActual","de elaboración "+this.fechaElaboracion,UtilidadFecha.getFechaActual()));
		        }
	    }
	    if(this.estado.equals("generarDetalleTransaccion"))
	    {	
	      for(int k=0;k<this.numeroFilasMapa;k++)
	      {
	    	  boolean tieneValorUnitario=true,tieneCantidad=true;
	         if(Integer.parseInt(this.mapaAtributos.get("tipoCosto")+"")==ConstantesBDInventarios.codigoTipoCostoInventarioManual)
	         {
	        	try
	        	{
			         if((this.mapaArticulos.get("valorUnitarioArticulo_"+k)).equals(""))
			         {
			        	 tieneValorUnitario=false;
			             errores.add("falta campo", new ActionMessage("errors.required","El Valor Unitario del Articulo "+this.mapaArticulos.get("descripcionArticulo_"+k)));   
			         }
	        	}
			    catch (Exception e) 
			    {
			    	errores.add("Numero invalido",new ActionMessage("errors.invalid",this.mapaArticulos.get("valorUnitarioArticulo_"+k)));
				}
	          if((this.mapaArticulos.get("cantidadArticulo_"+k)).equals(""))
	          {
	        	  tieneCantidad=false;
	              errores.add("falta campo", new ActionMessage("errors.required","La cantidad del Articulo "+this.mapaArticulos.get("descripcionArticulo_"+k)));   
	          }
	          if(tieneValorUnitario&&tieneCantidad)
	          {
		          if(Integer.parseInt(this.mapaArticulos.get("cantidadArticulo_"+k).toString())*Double.parseDouble(this.mapaArticulos.get("valorUnitarioArticulo_"+k).toString())<=0)
		          {
		              errores.add("mayor a 0", new ActionMessage("errors.MayorQue","El valor total del articulo "+this.mapaArticulos.get("descripcionArticulo_"+k),"0"));   
		          }
	          }
	         }
	         else
	         {
	        	 //verificar que la cantidad sea mayor a 0
		          if((this.mapaArticulos.get("cantidadArticulo_"+k)).equals(""))
		          {
		        	  tieneCantidad=false;
		              errores.add("falta campo", new ActionMessage("errors.required","La cantidad del Articulo "+this.mapaArticulos.get("descripcionArticulo_"+k)));   
		          }
		          if(tieneCantidad)
		          {
			          if(Integer.parseInt(this.mapaArticulos.get("cantidadArticulo_"+k).toString())<=0)
			          {
			              errores.add("mayor a 0", new ActionMessage("errors.MayorQue","La cantidad del articulo "+this.mapaArticulos.get("descripcionArticulo_"+k),"0"));   
			          }
		          }
	         }
	         if(Integer.parseInt(this.getMapaAtributos("codigoTipoConceptoTransaccion")+"")==ConstantesBDInventarios.codigoTipoConceptoInventarioEntrada)
	         {	        
			      if(UtilidadTexto.getBoolean(this.mapaArticulos.get("manejaLoteArticulo_"+k)+"") && (this.mapaArticulos.get("loteArticulo_"+k)+"").trim().equals(""))
			      {
			    	  errores.add("falta campo", new ActionMessage("errors.required","El lote del Articulo "+this.mapaArticulos.get("descripcionArticulo_"+k)));
			      }
			      if(UtilidadTexto.getBoolean(this.mapaArticulos.get("manejaFechaVencimientoArticulo_"+k)+""))
			      {
			    	  if((this.mapaArticulos.get("fechaVencimientoArticulo_"+k)+"").trim().equals(""))
				      {
				    	  errores.add("falta campo", new ActionMessage("errors.required","La fecha de Vencimiento del Articulo "+this.mapaArticulos.get("descripcionArticulo_"+k)));
				      }
			    	  else if(!UtilidadFecha.validarFecha(this.mapaArticulos.get("fechaVencimientoArticulo_"+k)+""))
				      {
				            errores.add("formato fecha invalido", new ActionMessage("errors.formatoFechaInvalido","La fecha de Vencimiento del Articulo "+this.mapaArticulos.get("descripcionArticulo_"+k)));
				      }
			      }
	         }
	         else
	         {	        
			      if(UtilidadTexto.getBoolean(this.mapaArticulos.get("manejaLoteArticulo_"+k)+"") && (this.mapaArticulos.get("loteArticulo_"+k)+"").equals(""))
			      {
			    	  errores.add("falta campo", new ActionMessage("errors.required","El lote del Articulo "+this.mapaArticulos.get("descripcionArticulo_"+k)));
			      }
			      if(UtilidadTexto.getBoolean(this.mapaArticulos.get("manejaFechaVencimientoArticulo_"+k)+""))
			      {
			    	  if(!(this.mapaArticulos.get("fechaVencimientoArticulo_"+k)+"").trim().equals(""))
				      {
			    		  if(!UtilidadFecha.validarFecha(this.mapaArticulos.get("fechaVencimientoArticulo_"+k)+""))
			    		  {
				            errores.add("formato fecha invalido", new ActionMessage("errors.formatoFechaInvalido","La fecha de Vencimiento del Articulo "+this.mapaArticulos.get("descripcionArticulo_"+k)));
			    		  }
				      }
			      }
	         }

		     for(int cont=0;cont < k; cont++)
		     {
		    	 String lote=this.mapaArticulos.get("loteArticulo_"+k)+"";
		    	 String fecha=this.mapaArticulos.get("fechaVencimientoArticulo_"+k)+"";
		    	 String articulo=this.mapaArticulos.get("codigoArticulo_"+k)+"";

		    	 String lote_1=this.mapaArticulos.get("loteArticulo_"+cont)+"";
		    	 String fecha_1=this.mapaArticulos.get("fechaVencimientoArticulo_"+cont)+"";
		    	 String articulo_1=this.mapaArticulos.get("codigoArticulo_"+cont)+"";
		    	 
		    	 if(lote.trim().equals(lote_1.trim())&&fecha.trim().equals(fecha_1.trim())&&articulo.trim().equals(articulo_1.trim()))
		    	 {
		    		 String cadena="";
		    		 if(UtilidadTexto.getBoolean(this.mapaArticulos.get("manejaLoteArticulo_"+k)+""))
		    		 {
		    			 cadena+=" Lote "+lote;
		    		 }
		    		 if(UtilidadTexto.getBoolean(this.mapaArticulos.get("manejaFechaVencimientoArticulo_"+k)+""))
		    		 {
		    			cadena+=" Fecha Vencimiento "+fecha; 
		    		 }
		    		 errores.add("YA EXISTE EL REGISTRO.", new ActionMessage("errors.yaExiste","El Articulo "+this.mapaArticulos.get("descripcionArticulo_"+k) +cadena));
		    	 }
		    	 
		    	 
		     }
		     //articulos repetidos
		     for(int j=0;j<k;j++)
		     {
		    	 int codArt1=Utilidades.convertirAEntero(this.mapaArticulos.get("codigoArticulo_"+k)+"");
		    	 int codArt2=Utilidades.convertirAEntero(this.mapaArticulos.get("codigoArticulo_"+j)+"");
		    	if(codArt1==codArt2 && 
			    		(this.getMapaArticulos().get("loteArticulo_"+k)+"").equals(this.getMapaArticulos().get("loteArticulo_"+j)+"")	&&
			    		(this.getMapaArticulos().get("fechaVencimientoArticulo_"+k)+"").equals(this.getMapaArticulos().get("fechaVencimientoArticulo_"+j)+"")
			    		)
		    	 {
		    		String mensaje=this.getMapaArticulos().get("descripcionArticulo_"+k)+"";
		    		errores.add("YA EXISTE EL REGISTRO.", new ActionMessage("errors.yaExiste","El Articulo "+mensaje));     
		    	 }
		     } 
	      }
	      
	      if(this.numeroFilasMapa==0)
	      {
	          errores.add("falta información", new ActionMessage("error.inventarios.faltaInfoDetalleParaGenerarlo"));
	      }
	      if((this.mapaAtributos.get("esCerrarTransaccion")+"").equals("true") && this.numeroFilasMapa==0)
	      {
	          errores.add("falta información", new ActionMessage("error.inventarios.faltaInformacionDetalleTransaccion")); 
	      }
	    }
	    if(this.estado.equals("ejercutarBusquedaAvanzada"))
	    {
	        if(!this.fechaElaboraInicial.equals(""))
	        {
	            if(!UtilidadFecha.validarFecha(this.fechaElaboraInicial))
		        {
		            errores.add("formato fecha invalido", new ActionMessage("errors.formatoFechaInvalido","de elaboración"));
		        }
	            if(this.fechaElaboraFinal.trim().equals(""))
	            {
	                this.fechaElaboraFinal=this.fechaElaboraInicial;
	            }
	        }
	        if(!this.fechaElaboraFinal.equals(""))
	        {
	            if(!UtilidadFecha.validarFecha(this.fechaElaboraFinal))
		        {
		            errores.add("formato fecha invalido", new ActionMessage("errors.formatoFechaInvalido","de elaboración"));
		        }
	            if(this.fechaElaboraInicial.trim().equals(""))
	            {
	                this.fechaElaboraInicial=this.fechaElaboraFinal;
	            }
	        }
	        if(!this.fechaCierreInicial.equals(""))
	        {
	            if(!UtilidadFecha.validarFecha(this.fechaCierreInicial))
		        {
		            errores.add("formato fecha invalido", new ActionMessage("errors.formatoFechaInvalido","de elaboración"));
		        }
	            if(this.fechaCierreFinal.trim().equals(""))
	            {
	                this.fechaCierreFinal=this.fechaCierreInicial;
	            }
	        }
	        if(!this.fechaCierreFinal.equals(""))
	        {
	            if(!UtilidadFecha.validarFecha(this.fechaCierreFinal))
		        {
		            errores.add("formato fecha invalido", new ActionMessage("errors.formatoFechaInvalido","de elaboración"));
		        }
	            if(this.fechaCierreInicial.trim().equals(""))
	            {
	                this.fechaCierreInicial=this.fechaCierreFinal;
	            }
	        }
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
     * @return Retorna codAlmacen.
     */
    public int getCodAlmacen() {
        return codAlmacen;
    }
    /**
     * @param codAlmacen Asigna codAlmacen.
     */
    public void setCodAlmacen(int codAlmacen) {
        this.codAlmacen = codAlmacen;
    }
    /**
     * @return Retorna nombreAlmacen.
     */
    public String getNombreAlmacen() {
        return nombreAlmacen;
    }
    /**
     * @param nombreAlmacen Asigna nombreAlmacen.
     */
    public void setNombreAlmacen(String nombreAlmacen) {
        this.nombreAlmacen = nombreAlmacen;
    }
    /**
     * @return Retorna mapAlmacenes.
     */
    public HashMap getMapAlmacenes() {
        return mapAlmacenes;
    }
    /**
     * @param mapAlmacenes Asigna mapAlmacenes.
     */
    public void setMapAlmacenes(HashMap mapAlmacenes) {
        this.mapAlmacenes = mapAlmacenes;
    }
    /**
     * @return Retorna mapAlmacenes.
     */
    public Object getMapAlmacenes(String key) {
        return mapAlmacenes.get(key);
    }
    /**
     * @param mapAlmacenes Asigna mapAlmacenes.
     */
    public void setMapAlmacenes(String key,Object value) {
        this.mapAlmacenes.put(key, value);
    }
    /**
     * @return Retorna mapTransXCC.
     */
    public HashMap getMapTransXCC() {
        return mapTransXCC;
    }
    /**
     * @param mapTransXCC Asigna mapTransXCC.
     */
    public void setMapTransXCC(HashMap mapTransXCC) {
        this.mapTransXCC = mapTransXCC;
    }
    /**
     * @return Retorna mapTransXCC.
     */
    public Object getMapTransXCC(String key) {
        return mapTransXCC.get(key);
    }
    /**
     * @param mapTransXCC Asigna mapTransXCC.
     */
    public void setMapTransXCC(String key,Object value) {
        this.mapTransXCC.put(key, value);
    }
    /**
     * @return Retorna codTransaccion.
     */
    public int getCodTransaccion() {
        return codTransaccion;
    }
    /**
     * @param codTransaccion Asigna codTransaccion.
     */
    public void setCodTransaccion(int codTransaccion) {
        this.codTransaccion = codTransaccion;
    }
    /**
     * @return Retorna esFechaModificable.
     */
    public String getEsFechaModificable() {
        return esFechaModificable;
    }
    /**
     * @param esFechaModificable Asigna esFechaModificable.
     */
    public void setEsFechaModificable(String esFechaModificable) {
        this.esFechaModificable = esFechaModificable;
    }
    /**
     * @return Retorna fechaElaboracion.
     */
    public String getFechaElaboracion() {
        return fechaElaboracion;
    }
    /**
     * @param fechaElaboracion Asigna fechaElaboracion.
     */
    public void setFechaElaboracion(String fechaElaboracion) {
        this.fechaElaboracion = fechaElaboracion;
    }
    /**
     * @return Retorna usuarioElabora.
     */
    public String getUsuarioElabora() {
        return usuarioElabora;
    }
    /**
     * @param usuarioElabora Asigna usuarioElabora.
     */
    public void setUsuarioElabora(String usuarioElabora) {
        this.usuarioElabora = usuarioElabora;
    }
    /**
     * @return Retorna loginUsuario.
     */
    public String getLoginUsuario() {
        return loginUsuario;
    }
    /**
     * @param loginUsuario Asigna loginUsuario.
     */
    public void setLoginUsuario(String loginUsuario) {
        this.loginUsuario = loginUsuario;
    }
    /**
     * @return Returns the listadoTrans.
     */
    public HashMap getListadoTrans()
    {
        return listadoTrans;
    }
    /**
     * @param listadoTrans The listadoTrans to set.
     */
    public void setListadoTrans(HashMap listadoTrans)
    {
        this.listadoTrans = listadoTrans;
    } 
    /**
     * @return Returns the listadoTrans.
     */
    public Object getListadoTrans(String key)
    {
        return listadoTrans.get(key);
    }
    /**
     * @param listadoTrans The listadoTrans to set.
     */
    public void setListadoTrans(String key,Object value)
    {
        this.listadoTrans.put(key, value);
    }
    /**
     * @return Returns the codEstadoTrans.
     */
    public int getCodEstadoTrans()
    {
        return codEstadoTrans;
    }
    /**
     * @param codEstadoTrans The codEstadoTrans to set.
     */
    public void setCodEstadoTrans(int codEstadoTrans)
    {
        this.codEstadoTrans = codEstadoTrans;
    }    
    /**
     * @return Returns the fechaCierreFinal.
     */
    public String getFechaCierreFinal()
    {
        return fechaCierreFinal;
    }
    /**
     * @param fechaCierreFinal The fechaCierreFinal to set.
     */
    public void setFechaCierreFinal(String fechaCierreFinal)
    {
        this.fechaCierreFinal = fechaCierreFinal;
    }
    /**
     * @return Returns the fechaCierreInicial.
     */
    public String getFechaCierreInicial()
    {
        return fechaCierreInicial;
    }
    /**
     * @param fechaCierreInicial The fechaCierreInicial to set.
     */
    public void setFechaCierreInicial(String fechaCierreInicial)
    {
        this.fechaCierreInicial = fechaCierreInicial;
    }
    /**
     * @return Returns the fechaElaboraFinal.
     */
    public String getFechaElaboraFinal()
    {
        return fechaElaboraFinal;
    }
    /**
     * @param fechaElaboraFinal The fechaElaboraFinal to set.
     */
    public void setFechaElaboraFinal(String fechaElaboraFinal)
    {
        this.fechaElaboraFinal = fechaElaboraFinal;
    }
    /**
     * @return Returns the fechaElaboraInicial.
     */
    public String getFechaElaboraInicial()
    {
        return fechaElaboraInicial;
    }
    /**
     * @param fechaElaboraInicial The fechaElaboraInicial to set.
     */
    public void setFechaElaboraInicial(String fechaElaboraInicial)
    {
        this.fechaElaboraInicial = fechaElaboraInicial;
    }
    /**
     * @return Returns the numTransFinal.
     */
    public int getNumTransFinal()
    {
        return numTransFinal;
    }
    /**
     * @param numTransFinal The numTransFinal to set.
     */
    public void setNumTransFinal(int numTransFinal)
    {
        this.numTransFinal = numTransFinal;
    }
    /**
     * @return Returns the numTransInicial.
     */
    public int getNumTransInicial()
    {
        return numTransInicial;
    }
    /**
     * @param numTransInicial The numTransInicial to set.
     */
    public void setNumTransInicial(int numTransInicial)
    {
        this.numTransInicial = numTransInicial;
    }
    /**
     * @return Returns the usuarioCierra.
     */
    public String getUsuarioCierra()
    {
        return usuarioCierra;
    }
    /**
     * @param usuarioCierra The usuarioCierra to set.
     */
    public void setUsuarioCierra(String usuarioCierra)
    {
        this.usuarioCierra = usuarioCierra;
    }    
    /**
     * @return Retorna observaciones.
     */
    public String getObservaciones() {
        return observaciones;
    }
    /**
     * @param observaciones Asigna observaciones.
     */
    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
        this.setObservacionesOld(observaciones);
    }
    /**
     * @return Retorna cargarDetalle.
     */
    public String getCargarDetalle() {
        return cargarDetalle;
    }
    /**
     * @param cargarDetalle Asigna cargarDetalle.
     */
    public void setCargarDetalle(String cargarDetalle) {
        this.cargarDetalle = cargarDetalle;
    }
    /**
     * @return Retorna numeroFilasMapa.
     */
    public int getNumeroFilasMapa() {
        	//machetazo de joan, devide el numero de atributos del mapa, por el numero de columnas para saber cuantos registro son.
        	//si me queda tiempo lo cambio.
        	for(int i=0;i<this.mapaArticulos.size();i++)
        	{
        		if(!this.mapaArticulos.containsKey("codigoArticulo_"+i))
        		{
        			return i;
        		}
        	}
            return 0;
    }
    /**
     * @param numeroFilasMapa Asigna numeroFilasMapa.
     */
    public void setNumeroFilasMapa(int numeroFilasMapa) {
        this.numeroFilasMapa = numeroFilasMapa;
    }
    /**
     * @return Retorna mapaArticulos.
     */
    public HashMap getMapaArticulos() {
        return mapaArticulos;
    }
    /**
     * @param mapaArticulos Asigna mapaArticulos.
     */
    public void setMapaArticulos(HashMap mapaArticulos) {
        this.mapaArticulos = mapaArticulos;
    }
    /**
     * @return Retorna mapaArticulos.
     */
    public Object getMapaArticulos(String key) {
        return mapaArticulos.get(key);
    }
    /**
     * @param mapaArticulos Asigna mapaArticulos.
     */
    public void setMapaArticulos(String key,Object value) {
        this.mapaArticulos.put(key, value);
    }    
    /**
     * @return Retorna nombreTransaccion.
     */
    public String getNombreTransaccion() {
        return nombreTransaccion;
    }
    /**
     * @param nombreTransaccion Asigna nombreTransaccion.
     */
    public void setNombreTransaccion(String nombreTransaccion) {
        this.nombreTransaccion = nombreTransaccion;
    }
    /**
     * @return Retorna mapaAtributos.
     */
    public HashMap getMapaAtributos() {
        return mapaAtributos;
    }
    /**
     * @param mapaAtributos Asigna mapaGeneral.
     */
    public void setMapaAtributos(HashMap mapaAtributos) {
        this.mapaAtributos = mapaAtributos;
    }
    /**
     * @return Retorna mapaAtributos.
     */
    public Object getMapaAtributos(String key) {
        return mapaAtributos.get(key);
    }
    /**
     * @param mapaAtributos Asigna mapaGeneral.
     */
    public void setMapaAtributos(String key,Object value) {
        this.mapaAtributos.put(key, value);
    }
    /**
     * @return Returns the maxPageItem.
     */
    public int getMaxPageItem()
    {
        return maxPageItem;
    }
    /**
     * @param maxPageItem The maxPageItem to set.
     */
    public void setMaxPageItem(int maxPageItem)
    {
        this.maxPageItem = maxPageItem;
    }
    /**
     * @return Returns the patronOrdenar.
     */
    public String getPatronOrdenar()
    {
        return patronOrdenar;
    }
    /**
     * @param patronOrdenar The patronOrdenar to set.
     */
    public void setPatronOrdenar(String patronOrdenar)
    {
        this.patronOrdenar = patronOrdenar;
    }
    /**
     * @return Returns the ultimoPatron.
     */
    public String getUltimoPatron()
    {
        return ultimoPatron;
    }
    /**
     * @param ultimoPatron The ultimoPatron to set.
     */
    public void setUltimoPatron(String ultimoPatron)
    {
        this.ultimoPatron = ultimoPatron;
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
     * @return Returns the indiceTrans.
     */
    public int getIndiceTrans()
    {
        return indiceTrans;
    }
    /**
     * @param indiceTrans The indiceTrans to set.
     */
    public void setIndiceTrans(int indiceTrans)
    {
        this.indiceTrans = indiceTrans;
    }
    /**
     * @return Retorna mapTransXCCBusqueda.
     */
    public HashMap getMapTransXCCBusqueda() {
        return mapTransXCCBusqueda;
    }
    /**
     * @param mapTransXCCBusqueda Asigna mapTransXCCBusqueda.
     */
    public void setMapTransXCCBusqueda(HashMap mapTransXCCBusqueda) {
        this.mapTransXCCBusqueda = mapTransXCCBusqueda;
    }
    /**
     * @return Retorna mapTransXCCBusqueda.
     */
    public Object getMapTransXCCBusqueda(String key) {
        return mapTransXCCBusqueda.get(key);
    }
    /**
     * @param mapTransXCCBusqueda Asigna mapTransXCCBusqueda.
     */
    public void setMapTransXCCBusqueda(String key,Object value) {
        this.mapTransXCCBusqueda.put(key, value);
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
    /**
     * @return Retorna warnings.
     */
    public HashMap getWarnings() {
        return warnings;
    }
    /**
     * @param warnings Asigna warnings.
     */
    public void setWarnings(HashMap warnings) {
        this.warnings = warnings;
    }
    /**
     * @return Retorna warnings.
     */
    public Object getWarnings(String key) {
        return warnings.get(key);
    }
    /**
     * @param warnings Asigna warnings.
     */
    public void setWarnings(String key,Object value) {
        this.warnings.put(key, value);
    }
    /**
     * @return Retorna conTrans.
     */
    public Connection getConTrans() {
        return conTrans;
    }
    /**
     * @param conTrans Asigna conTrans.
     */
    public void setConTrans(Connection conTrans) {
        this.conTrans = conTrans;
    }
    /**
     * @return Retorna enTransaccion.
     */
    public boolean isEnTransaccion() {
        return enTransaccion;
    }
    /**
     * @param enTransaccion Asigna enTransaccion.
     */
    public void setEnTransaccion(boolean enTransaccion) {
        this.enTransaccion = enTransaccion;
    }
	public ArrayList getArticulosEliminados() {
		return articulosEliminados;
	}
	public void setArticulosEliminados(ArrayList articulosEliminados) {
		this.articulosEliminados = articulosEliminados;
	}
	public boolean isCerrarRegistroTransaccion() {
		return cerrarRegistroTransaccion;
	}
	public void setCerrarRegistroTransaccion(boolean cerrarRegistroTransaccion) {
		this.cerrarRegistroTransaccion = cerrarRegistroTransaccion;
	}
	public String getNumeroTransaccion() {
		return numeroTransaccion;
	}
	public void setNumeroTransaccion(String numeroTransaccion) {
		this.numeroTransaccion = numeroTransaccion;
	}
	public boolean isProcesoExitoso() {
		return procesoExitoso;
	}
	public void setProcesoExitoso(boolean procesoExitoso) {
		this.procesoExitoso = procesoExitoso;
	}
	public String getNumeroTransaccionResumen() {
		return numeroTransaccionResumen;
	}
	public void setNumeroTransaccionResumen(String numeroTransaccionResumen) {
		this.numeroTransaccionResumen = numeroTransaccionResumen;
	}
	public String getObservacionesOld() {
		return observacionesOld;
	}
	public void setObservacionesOld(String observacionesOld) {
		this.observacionesOld = observacionesOld;
	}
	public String getCentroAtencion() {
		return centroAtencion;
	}
	public void setCentroAtencion(String centroAtencion) {
		this.centroAtencion = centroAtencion;
	}

	public HashMap getEntidades()
	{
		return entidades;
	}

	public void setEntidades(HashMap entidades)
	{
		this.entidades = entidades;
	}
	
	public Object getEntidades(String key)
	{
		return entidades.get(key);
	}
	
	public void setEntidades(String key,Object value)
	{
		entidades.put(key,value);
	}

	public String getParejasClaseGrupo()
	{
		return parejasClaseGrupo;
	}

	public void setParejasClaseGrupo(String parejasClaseGrupo)
	{
		this.parejasClaseGrupo = parejasClaseGrupo;
	}

	public String getIndicativo_consignacion() {
		return indicativo_consignacion;
	}

	public void setIndicativo_consignacion(String indicativo_consignacion) {
		this.indicativo_consignacion = indicativo_consignacion;
	}

	public String getTipo_consignac() {
		return tipo_consignac;
	}

	public void setTipo_consignac(String tipo_consignac) {
		this.tipo_consignac = tipo_consignac;
	}


	public String getDescripcionEntidad() {
		return descripcionEntidad;
	}

	public void setDescripcionEntidad(String descripcionEntidad) {
		this.descripcionEntidad = descripcionEntidad;
	}

	public Collection getResultados() {
		return resultados;
	}

	public void setResultados(Collection resultados) {
		this.resultados = resultados;
	}

	public String getCodigoEntidad() {
		return codigoEntidad;
	}

	public void setCodigoEntidad(String codigoEntidad) {
		this.codigoEntidad = codigoEntidad;
	}

	public String getCodTipo() {
		return codTipo;
	}

	public void setCodTipo(String codTipo) {
		this.codTipo = codTipo;
	}

	public String getDescTipo() {
		return descTipo;
	}

	public void setDescTipo(String descTipo) {
		this.descTipo = descTipo;
	}

	public String getCriterioBusqueda() {
		return criterioBusqueda;
	}

	public void setCriterioBusqueda(String criterioBusqueda) {
		this.criterioBusqueda = criterioBusqueda;
	}

	public void setIndice(String indice) {
		this.indice = indice;
	}

	public String getIndice() {
		return indice;
	}

	/**
	 * Método setSelectTransaccion
	 * @param selectTransaccion
	 */
	public void setSelectTransaccion(String selectTransaccion) {
		this.selectTransaccion = selectTransaccion;
	}

	/**
	 * Método getSelectTransaccion
	 * @return String selectTransaccion
	 */
	public String getSelectTransaccion() {
		return selectTransaccion;
	}

	/**
	 * @return the desdeBusqueda
	 */
	public boolean isDesdeBusqueda() {
		return desdeBusqueda;
	}

	/**
	 * @param desdeBusqueda the desdeBusqueda to set
	 */
	public void setDesdeBusqueda(boolean desdeBusqueda) {
		this.desdeBusqueda = desdeBusqueda;
	}
	
	

}
