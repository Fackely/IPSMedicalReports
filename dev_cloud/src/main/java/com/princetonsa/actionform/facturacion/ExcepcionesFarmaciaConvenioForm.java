/*
 * @(#)ExcepcionesFarmaciaConvenioForm.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_05
 *
 */

package com.princetonsa.actionform.facturacion;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.RespuestaHashMap;

/**
 * Forma para manejo presentación de la funcionalidad 
 * Excepciones Farmacia
 *
 * @version 1.0 Nov 29, 2004
 */
public class ExcepcionesFarmaciaConvenioForm   extends ValidatorForm
{
	/**
	 * Estado en el que se encuentra el proceso.
	 */
	private String estado = "";
	
	/**
	 * En el caso que el estado sea finalizar,
	 * hay que distinguir entre que se debe hacer
	 */
	private String accionAFinalizar="";
	
	/**
	 * Código con el convenio seleccionado
	 */
	private int codigoConvenioSeleccionado=ConstantesBD.codigoNuncaValido;
	
	/**
	 * HashMap con todos los elementos presentes
	 * en la BD
	 */
	private RespuestaHashMap hashPresentesBD=new RespuestaHashMap();
	
    /**
	 * Indice de un posible requisito a eliminar
	 * (Se maneja el índice ya que la información
	 * esta presente en un mapa y es mucho más
	 * fácil encontrarla por el índice que por el
	 * código almacenado)
     */
    private int indiceRequisitoAEliminar=ConstantesBD.codigoNuncaValido;
	
    /**
	 * Indice de un posible requisito a guardar
	 * (Se maneja el índice ya que la información
	 * esta presente en un mapa y es mucho más
	 * fácil encontrarla por el índice que por el
	 * código almacenado)
     */
    private int indiceRequisitoAGuardar=ConstantesBD.codigoNuncaValido;

    /**
     * Entero que me indica el número de requisitos
     * eliminados (Solo se tienen en cuenta los que
     * estaban previamente en la BD)
     */
    private int numeroRequisitosEliminados=0;
    
	/**
	 * Entero usado para manejar el caso de deseo de guardar,
	 * al cambiar el convenio (Para saber que convenio recargar)
	 */
	private int codigoConvenioDeseoCambio=ConstantesBD.codigoNuncaValido;
	
	/**
	 * String que nos indica la página siguiente a la que desea
	 * acceder el usuario, cuando sea necesario
	 */
	private String paginaSiguiente="";
	
	/**
	 * Código del convenio que se utiliza en la búsqueda
	 * avanzada
	 */
	private int codigoConvenioBusqueda=ConstantesBD.codigoNuncaValido;
	
	/**
	 * Código del centro de costo que se utiliza en la 
	 * búsqueda avanzada
	 */
	private int codigoCentroCostoBusqueda=ConstantesBD.codigoNuncaValido;

	/**
	 * Código del artículo que se utiliza en la búsqueda
	 * avanzada
	 */
	private int codigoArticuloBusqueda=ConstantesBD.codigoNuncaValido;
	
	/**
	 * Parte del nombre del artículo a buscar
	 */
	private String nombreArticuloBusqueda="";
	
	/**
	 * Método que limpia este form
	 *
	 */
	public void reset()
	{
	    this.estado="consultar";
	    this.accionAFinalizar="";
	    this.codigoConvenioSeleccionado=ConstantesBD.codigoNuncaValido;
	    this.hashPresentesBD.getMapa().clear();
	    this.hashPresentesBD.setTamanio(0);
	    this.indiceRequisitoAEliminar=ConstantesBD.codigoNuncaValido;
	    this.indiceRequisitoAGuardar=ConstantesBD.codigoNuncaValido;
	    this.numeroRequisitosEliminados=0;
	    this.codigoConvenioDeseoCambio=ConstantesBD.codigoNuncaValido;
	    this.paginaSiguiente="";
	    this.limpiarCriteriosBusqueda();
	}
	
	/**
	 * Método que limpia todos los posibles criterios de
	 * búsqueda
	 */
	public void limpiarCriteriosBusqueda()
	{
	    this.codigoConvenioBusqueda=ConstantesBD.codigoNuncaValido;
	    this.codigoCentroCostoBusqueda=ConstantesBD.codigoNuncaValido;
	    this.codigoArticuloBusqueda=ConstantesBD.codigoNuncaValido;
	    this.nombreArticuloBusqueda="";
	}
	
    /**
     * @return Retorna el/la estado.
     */
    public String getEstado() {
        return estado;
    }
    /**
     * El/La estado a establecer.
     * @param estado 
     */
    public void setEstado(String estado) {
        this.estado = estado;
    }
    /**
     * @return Retorna el/la accionAFinalizar.
     */
    public String getAccionAFinalizar() {
        return accionAFinalizar;
    }
    /**
     * El/La accionAFinalizar a establecer.
     * @param accionAFinalizar 
     */
    public void setAccionAFinalizar(String accionAFinalizar) {
        this.accionAFinalizar = accionAFinalizar;
    }
    /**
     * @return Retorna el/la codigoConvenioSeleccionado.
     */
    public int getCodigoConvenioSeleccionado() {
        return codigoConvenioSeleccionado;
    }
    /**
     * El/La codigoConvenioSeleccionado a establecer.
     * @param codigoConvenioSeleccionado 
     */
    public void setCodigoConvenioSeleccionado(int codigoConvenioSeleccionado) {
        this.codigoConvenioSeleccionado = codigoConvenioSeleccionado;
    }

    /**
     * Método que me devuelve todo el hash map
     * @return
     */
    public RespuestaHashMap obtenerMapa()
    {
        return hashPresentesBD;
    }
    
	/**
	 * Retorna un valor presente en el mapa dada
	 * su llave
	 */
	public Object getUsoMapa (String key)
	{
	    return hashPresentesBD.getMapa().get(key);
	}
	
	/**
	 * Agrega o actualiza un valor en el mapa
	 */
	public void setUsoMapa(String key, Object value) 
	{
	    hashPresentesBD.getMapa().put(key, value);
	}
	
	/**
	 * Por medio de este método, el sistema puede saber 
	 * cuantos elementos de excepciones hay en memoria 
	 * @return
	 */
	public int getNumExcepcionesEnMemoria ()
	{
	    return hashPresentesBD.getTamanio();
	}
	
	/**
	 * Por medio de este método se cambia el número de
	 * excepciones en memoria
	 * 
	 * @param nuevoTamanio Nuevo número de excepciones
	 * en memoria
	 */
	public void setNumExcepcionesEnMemoria (int nuevoTamanio)
	{
	    hashPresentesBD.setTamanio(nuevoTamanio);
	}
	
	/**
	 * Si se llama a este método, es porque ya se aumentó
	 * el número de excepciones en memoria SIN embargo
	 * todavía no se ha seleccionado articulo, ni se han
	 * puesto los datos necesarios en el mapa.
	 * (Por el momento solo existen los campos de centro
	 * de costo y de no cubierto)
	 *
	 */
	public void agregarExcepcionMemoria()
	{
	    
	    int codigoAAgregar=this.obtenerMapa().getTamanio();
	    
	    //Para saber cuando se inserta, se pone un código
	    //siempre falso (se debe insertar)
	    this.setUsoMapa("codigo_" + codigoAAgregar, "" + ConstantesBD.codigoNuncaValido);
	    this.setUsoMapa("codigoConvenio_" + codigoAAgregar, this.codigoConvenioSeleccionado+"");
	    this.setUsoMapa("convenio_" + codigoAAgregar, "Agregado por El usuario");
	    this.setUsoMapa("codigoCentroCosto_" + codigoAAgregar, "" + ConstantesBD.codigoNuncaValido);
	    this.setUsoMapa("centroCosto_" + codigoAAgregar, "Agregado por El usuario");
	    this.setUsoMapa("codigoArticulo_" + codigoAAgregar, "" + ConstantesBD.codigoNuncaValido);
	    this.setUsoMapa("articulo_" + codigoAAgregar, "Agregado por El usuario");
        this.setUsoMapa("noCubre_" + codigoAAgregar, "0.0");
        this.setUsoMapa("vieneBD_" + codigoAAgregar, ConstantesBD.valorFalseEnString);
        
        //Aumento el tamaño en 1
	    this.obtenerMapa().setTamanio(codigoAAgregar+1);
	}
	/**
	 * Este método se encarga de eliminar una excepción
	 * Este proceso consta de la eliminación como tal,
	 * pero antes de esto es necesario guardar las
	 * eliminaciones hechas, si estas estaban presentes
	 * en la BD. Este método se usaba cuando se eliminaba
	 * del mapa, ahora se elimina de la fuente de datos
	 */
	public void eliminarExcepcion ()
	{
	    //Solo nos interesa manejar los que estén en la BD
	    //el resto son temporales
	    if (ConstantesBD.valorTrueEnString.equals(   ((String)this.getUsoMapa("vieneBD_"+this.indiceRequisitoAEliminar))  ))
	    {
	        //Vamos a guardar los elementos con la misma sintaxis
	        //pero agregando el sufijo El (de eliminado)
	        this.setUsoMapa("codigoEl_" + this.numeroRequisitosEliminados,
	                this.getUsoMapa("codigo_"+this.indiceRequisitoAEliminar));
	        this.setUsoMapa("codigoConvenioEl_" + this.numeroRequisitosEliminados,
	                this.getUsoMapa("codigoConvenio_"+this.indiceRequisitoAEliminar));
	        this.setUsoMapa("codigoCentroCostoEl_"+this.numeroRequisitosEliminados,
	                this.getUsoMapa("codigoCentroCosto_"+this.indiceRequisitoAEliminar));
	        this.setUsoMapa("codigoArticuloEl_"+this.numeroRequisitosEliminados,
	                this.getUsoMapa("codigoArticulo_"+this.indiceRequisitoAEliminar));
	        
	        this.numeroRequisitosEliminados++;
	    }
	    
	    //Ahora debemos eliminar el elemento como tal 
	    //(mover desde la siguiente posición hasta el
	    //final)
	    
	    //Es necesario mover TODOS los atributos, incluidos 
	    //nombres y demás 
	    for (int i=this.indiceRequisitoAEliminar+1;i<this.obtenerMapa().getTamanio();i++)
	    {
		    this.setUsoMapa("codigo_"+(i-1),
		            this.getUsoMapa("codigo_"+i));
		    this.setUsoMapa("codigoConvenio_"+(i-1),
		            this.getUsoMapa("codigoConvenio_"+i));
		    this.setUsoMapa("convenio_"+(i-1),
		            this.getUsoMapa("convenio_"+i));
		    this.setUsoMapa("codigoCentroCosto_"+(i-1),
		            this.getUsoMapa("codigoCentroCosto_"+i));
		    this.setUsoMapa("centroCosto_"+(i-1),
		            this.getUsoMapa("centroCosto_"+i));
		    this.setUsoMapa("codigoArticulo_"+(i-1),
		            this.getUsoMapa("codigoArticulo_"+i));
		    this.setUsoMapa("articulo_"+(i-1),
		            this.getUsoMapa("articulo_"+i));
		    this.setUsoMapa("noCubre_"+(i-1),
		            this.getUsoMapa("noCubre_"+i));
		    this.setUsoMapa("vieneBD_"+(i-1),
		            this.getUsoMapa("vieneBD_"+i));
	    }
	    this.obtenerMapa().setTamanio(this.obtenerMapa().getTamanio()-1);
	    
	}
    /**
     * @return Retorna el/la indiceRequisitoAEliminar.
     */
    public int getIndiceRequisitoAEliminar() {
        return indiceRequisitoAEliminar;
    }
    /**
     * El/La indiceRequisitoAEliminar a establecer.
     * @param indiceRequisitoAEliminar 
     */
    public void setIndiceRequisitoAEliminar(int indiceRequisitoAEliminar) {
        this.indiceRequisitoAEliminar = indiceRequisitoAEliminar;
    }
    /**
     * @return Retorna el/la codigoConvenioDeseoCambio.
     */
    public int getCodigoConvenioDeseoCambio() {
        return codigoConvenioDeseoCambio;
    }
    /**
     * El/La codigoConvenioDeseoCambio a establecer.
     * @param codigoConvenioDeseoCambio 
     */
    public void setCodigoConvenioDeseoCambio(int codigoConvenioDeseoCambio) {
        this.codigoConvenioDeseoCambio = codigoConvenioDeseoCambio;
    }
    
    /**
     * Método que genera un backup de los datos
     * en el mapa necesarios en el caso modificar,
     * no se pierda la información original.
     * Se guarda copia de todos los datos previniendo
     * el futuro 
     */
    public void generarBackup ()
    {
        int i;
        for (i=0;i< this.getNumExcepcionesEnMemoria();i++)
        {
		    this.setUsoMapa("codigoBackup_"+i,
		            this.getUsoMapa("codigo_"+i));
		    this.setUsoMapa("codigoConvenioBackup_"+i,
		            this.getUsoMapa("codigoConvenio_"+i));
		    this.setUsoMapa("convenioBackup_"+i,
		            this.getUsoMapa("convenio_"+i));
		    this.setUsoMapa("codigoCentroCostoBackup_"+i,
		            this.getUsoMapa("codigoCentroCosto_"+i));
		    this.setUsoMapa("centroCostoBackup_"+i,
		            this.getUsoMapa("centroCosto_"+i));
		    this.setUsoMapa("codigoArticuloBackup_"+i,
		            this.getUsoMapa("codigoArticulo_"+i));
		    this.setUsoMapa("articuloBackup_"+i,
		            this.getUsoMapa("articulo_"+i));
		    this.setUsoMapa("noCubreBackup_"+i,
		            this.getUsoMapa("noCubre_"+i));
		    this.setUsoMapa("vieneBDBackup_"+i,
		            this.getUsoMapa("vieneBD_"+i));
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
		// Perform validator framework validations
		ActionErrors errors = super.validate(mapping, request);
		boolean existeErrorPorcentaje = false;
		
		//Para no tener que revisar siempre que estos 
		//datos no sean nulos
		if (accionAFinalizar==null&&estado==null)
		{
		    return errors;
		}
		if (accionAFinalizar.equals("busquedaAvanzada")&&(estado.equals("modificar")||estado.equals("consultar")))
		{
		    if (this.getCodigoConvenioBusqueda()==ConstantesBD.codigoNuncaValido)
		    {
		        errors.add("errors.required", new ActionMessage("errors.required", "El convenio"));
		    }
		}
		if (estado.equals("modificar")&&accionAFinalizar.equals("guardarRequisito"))
		{
		    //Se debe validar que el usuario haya seleccionado 
		    //valores válidos en artículo y centro de costo
		    
		    int posibleCodigoCentroCosto= Integer.parseInt((String)getUsoMapa("codigoCentroCosto_"+this.getIndiceRequisitoAGuardar()) );
		    int posibleCodigoArticulo= Integer.parseInt((String)getUsoMapa("codigoArticulo_"+this.getIndiceRequisitoAGuardar()) );
		    
		    if (posibleCodigoCentroCosto==ConstantesBD.codigoNuncaValido)
		    {
		        errors.add("centroCostoInvalido", new ActionMessage("errors.seleccion", "centro de costo"));
		    }
		    if (posibleCodigoArticulo==ConstantesBD.codigoNuncaValido)
		    {
		        errors.add("articuloInvalido", new ActionMessage("errors.seleccion", "artículo"));
		    }
		    		    
		    if(getUsoMapa("noCubre_"+this.getIndiceRequisitoAGuardar()).equals(""))
		    {
		        errors.add("errors.required", new ActionMessage("errors.required", "El porcentaje de excepción "));
		        existeErrorPorcentaje = true;
		    }
		    
		    if(!existeErrorPorcentaje)
		    {
//		      debo validar el índice seleccionado actualmente
		        try
		        {
		            double noCubreDado=Double.parseDouble((String)getUsoMapa("noCubre_"+this.getIndiceRequisitoAGuardar()));
		            if (noCubreDado<0||noCubreDado>100)
		            {
		                errors.add("errors.range", new ActionMessage("errors.range", "El porcentaje de excepción ", "0", "100"));
		            }
		                
		        }
		        catch (NumberFormatException e)
		        {
		            errors.add("errors.float", new ActionMessage("errors.float", "El porcentaje de excepción "));
		        }
		    }
		}
		return errors;
	}
    /**
     * @return Retorna el/la indiceRequisitoAGuardar.
     */
    public int getIndiceRequisitoAGuardar() {
        return indiceRequisitoAGuardar;
    }
    /**
     * El/La indiceRequisitoAGuardar a establecer.
     * @param indiceRequisitoAGuardar 
     */
    public void setIndiceRequisitoAGuardar(int indiceRequisitoAAgregar) {
        this.indiceRequisitoAGuardar = indiceRequisitoAAgregar;
    }
    /**
     * @return Retorna el/la paginaSiguiente.
     */
    public String getPaginaSiguiente() {
        return paginaSiguiente;
    }
    /**
     * El/La paginaSiguiente a establecer.
     * @param paginaSiguiente 
     */
    public void setPaginaSiguiente(String paginaSiguiente) {
        this.paginaSiguiente = paginaSiguiente;
    }
    /**
     * @return Retorna el/la codigoArticuloBusqueda.
     */
    public int getCodigoArticuloBusqueda() {
        return codigoArticuloBusqueda;
    }
    /**
     * El/La codigoArticuloBusqueda a establecer.
     * @param codigoArticuloBusqueda 
     */
    public void setCodigoArticuloBusqueda(int codigoArticuloBusqueda) {
        //Nunca voy a llenar un -1, solo en caso 
        //que el usuario me envie un texto y el
        //bean sea incapaz de convertirlo (lo
        //convierte en 0)
        if (codigoArticuloBusqueda!=0)
        {
            this.codigoArticuloBusqueda = codigoArticuloBusqueda;
        }
    }
    /**
     * @return Retorna el/la codigoCentroCostoBusqueda.
     */
    public int getCodigoCentroCostoBusqueda() {
        return codigoCentroCostoBusqueda;
    }
    /**
     * El/La codigoCentroCostoBusqueda a establecer.
     * @param codigoCentroCostoBusqueda 
     */
    public void setCodigoCentroCostoBusqueda(int codigoCentroCostoBusqueda) {
        this.codigoCentroCostoBusqueda = codigoCentroCostoBusqueda;
    }
    /**
     * @return Retorna el/la codigoConvenioBusqueda.
     */
    public int getCodigoConvenioBusqueda() {
        return codigoConvenioBusqueda;
    }
    /**
     * El/La codigoConvenioBusqueda a establecer.
     * @param codigoConvenioBusqueda 
     */
    public void setCodigoConvenioBusqueda(int codigoConvenioBusqueda) {
        this.codigoConvenioBusqueda = codigoConvenioBusqueda;
    }
    /**
     * @return Retorna el/la nombreArticuloBusqueda.
     */
    public String getNombreArticuloBusqueda() {
        return nombreArticuloBusqueda;
    }
    /**
     * El/La nombreArticuloBusqueda a establecer.
     * @param nombreArticuloBusqueda 
     */
    public void setNombreArticuloBusqueda(String nombreArticuloBusqueda) {
        this.nombreArticuloBusqueda = nombreArticuloBusqueda;
    }
}
