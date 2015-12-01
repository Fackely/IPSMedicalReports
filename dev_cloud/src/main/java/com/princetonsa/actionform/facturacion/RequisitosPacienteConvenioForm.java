/*
 * @(#)RequisitosPacienteConvenioForm.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_05
 *
 */

package com.princetonsa.actionform.facturacion;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.RespuestaHashMap;
import util.UtilidadBD;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.facturacion.UtilidadesFacturacion;

/**
 *
 * @version 1.0 Nov 22, 2004
 */
public class RequisitosPacienteConvenioForm  extends ValidatorForm
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
	 * Indice de un posible requisito a agregar
	 * (Se maneja el índice ya que la información
	 * esta presente en un mapa y es mucho más
	 * fácil encontrarla por el índice que por el
	 * código almacenado)
	 */
	private int indiceRequisitoAgregar=ConstantesBD.codigoNuncaValido;
	
	/**
	 * Número de requisitos no usados por el
	 * convenio
	 */
	private int numRequisitosNoUsados=0;
	
    /**
     * Atributo que me indica si estamos en
     * consulta o podemos modificar/ingresar
     */
    private boolean esSoloConsulta=true;
    
    /**
     * Entero que me indica el número de
     * elementos eliminados
     */
    private int numElementosEliminados=0;
    
    /**
	 * Indice de un posible requisito a eliminar
	 * (Se maneja el índice ya que la información
	 * esta presente en un mapa y es mucho más
	 * fácil encontrarla por el índice que por el
	 * código almacenado)
     */
    private int indiceRequisitoAEliminar=ConstantesBD.codigoNuncaValido;
    
	/**
	 * Colección que mantiene los elementos 
	 * presentes en la BD
	 */
	private Collection elementosPresentesBD=new ArrayList();
	
	/**
	 * Entero usado para manejar el caso de deseo de guardar,
	 * al cambiar el convenio (Para saber que convenio recargar)
	 */
	private int codigoConvenioDeseoCambio=ConstantesBD.codigoNuncaValido;
	
	/**
	 * HashMap con todos los elementos presentes
	 * en la BD
	 */
	private RespuestaHashMap hashPresentesBD=new RespuestaHashMap();
	
	/**
	 * Indica se el requisito es de paciente o de radicacion
	 */
	private String tipoRequisitos;
	
	/**
	 * 
	 */
	private String procesoRealizado;
	
	/**
	 * Arreglo que maneja las vías de ingreso
	 */
	private ArrayList viasIngreso = new ArrayList();	
	
	public void reset()
	{
	    this.estado="consultar";
	    this.accionAFinalizar="";
	    this.elementosPresentesBD=new ArrayList();
	    this.codigoConvenioSeleccionado=ConstantesBD.codigoNuncaValido;
	    this.indiceRequisitoAgregar=ConstantesBD.codigoNuncaValido;
	    this.indiceRequisitoAEliminar=ConstantesBD.codigoNuncaValido;
	    this.esSoloConsulta=true;
	    this.hashPresentesBD.getMapa().clear();
	    this.hashPresentesBD.setTamanio(0);
	    this.numRequisitosNoUsados=0;
	    this.numElementosEliminados=0;
	    this.codigoConvenioDeseoCambio=ConstantesBD.codigoNuncaValido;
	    this.viasIngreso = new ArrayList();
	    this.procesoRealizado = ConstantesBD.acronimoNo;
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
		ActionErrors errores = new ActionErrors();
		if(this.estado.equals("modificar") && this.accionAFinalizar.equals("guardar") && this.tipoRequisitos.equals("Paciente"))
		{
			int contador = 0;
			//*********	SE VALIDA QUE LA VÍA DE INGRESO SEA REQUERIDA******************************
			for(int i=0;i<this.getNumElementosMemoria();i++)
			{
				if(!UtilidadTexto.getBoolean(this.getUsoMapa("eliminar_"+i)+""))
				{
					contador ++;
					if(this.getUsoMapa("viaIngreso_"+i).toString().equals(""))
						errores.add("", new ActionMessage("errors.required","La Vía de Ingreso en el registro N°"+contador));
				}
			}
			//************************************************************************************
			//******** SE VALIDA QUE NO HAYA REQUISITO / VIA DE INGRESO REPETIDO********************
			HashMap datosRepetidos = new HashMap();
			for(int i=0; i<this.getNumElementosMemoria(); i++)
			{
				if(!UtilidadTexto.getBoolean(this.getUsoMapa("eliminar_"+i)+""))
				{
					for(int j=(this.getNumElementosMemoria()-1);j>i;j--)
					{
						if(!UtilidadTexto.getBoolean(this.getUsoMapa("eliminar_"+j)+""))
						{
							//se revisa si hay requisito y via de ingreso igual
							if(
								Utilidades.convertirAEntero(this.getUsoMapa("viaIngreso_"+i)+"")>0&&Utilidades.convertirAEntero(this.getUsoMapa("viaIngreso_"+j)+"")>0&&
								Integer.parseInt(this.getUsoMapa("codigoRequisito_"+j)+"")==Integer.parseInt(this.getUsoMapa("codigoRequisito_"+i)+"")&&
								Integer.parseInt(this.getUsoMapa("viaIngreso_"+j)+"")==Integer.parseInt(this.getUsoMapa("viaIngreso_"+i)+"")&&
								!datosRepetidos.containsValue(this.getUsoMapa("viaIngreso_"+j)+ConstantesBD.separadorSplit+this.getUsoMapa("codigoRequisito_"+j)))
							{
								int numRegistros = Utilidades.convertirAEntero(datosRepetidos.get("numRegistros")+"", true);
								datosRepetidos.put(numRegistros+"",this.getUsoMapa("viaIngreso_"+j)+ConstantesBD.separadorSplit+this.getUsoMapa("codigoRequisito_"+j));
								numRegistros++;
								datosRepetidos.put("numRegistros", numRegistros+"");
							}
						}
					}
				}
			}
			if(Utilidades.convertirAEntero(datosRepetidos.get("numRegistros")+"")>0)
			{
				Connection con = UtilidadBD.abrirConexion();
				for(int i=0;i<Integer.parseInt(datosRepetidos.get("numRegistros").toString());i++)
				{
					String[] vector = datosRepetidos.get(i+"").toString().split(ConstantesBD.separadorSplit);
					errores.add("", new ActionMessage("error.capitacion.yaExisteCodigo","requisito "+UtilidadesFacturacion.obtenerDescripcionRequisitoPaciente(con, Integer.parseInt(vector[1]))+" y la Vía de Ingreso "+Utilidades.obtenerNombreViaIngreso(con, Integer.parseInt(vector[0]))));
				}
				UtilidadBD.closeConnection(con);
			}
			//**************************************************************************************
			
		}
		
		return errores;
	}
	
	/**
	 * @return the viasIngreso
	 */
	public ArrayList getViasIngreso() {
		return viasIngreso;
	}

	/**
	 * @param viasIngreso the viasIngreso to set
	 */
	public void setViasIngreso(ArrayList viasIngreso) {
		this.viasIngreso = viasIngreso;
	}

	/**
	 * reset del tipo de requisito
	 *
	 */
	public void resetTipoRequisitos()
	{
	    this.tipoRequisitos="";
	}
	
	public void limpiezaCambioConvenio()
	{
	    this.indiceRequisitoAgregar=ConstantesBD.codigoNuncaValido;
	    this.indiceRequisitoAEliminar=ConstantesBD.codigoNuncaValido;
	    this.hashPresentesBD.getMapa().clear();
	    this.hashPresentesBD.setTamanio(0);
	    this.numRequisitosNoUsados=0;
	    this.numElementosEliminados=0;
	    this.codigoConvenioDeseoCambio=ConstantesBD.codigoNuncaValido;
	}
	
    /**
     * @return Retorna el/la accionAFinalizar.
     */
    public String getAccionAFinalizar() {
        return accionAFinalizar;
    }
    /**
     * @param accionAFinalizar El/La accionAFinalizar a establecer.
     */
    public void setAccionAFinalizar(String accionAFinalizar) {
        this.accionAFinalizar = accionAFinalizar;
    }
    /**
     * @return Retorna el/la estado.
     */
    public String getEstado() {
        return estado;
    }
    /**
     * @param estado El/La estado a establecer.
     */
    public void setEstado(String estado) {
        this.estado = estado;
    }
    /**
     * @return Retorna el/la elementosPresentesBD.
     */
    public Collection getElementosPresentesBD() {
        return elementosPresentesBD;
    }
    /**
     * @param elementosPresentesBD El/La elementosPresentesBD a establecer.
     */
    public void setElementosPresentesBD(Collection elementosPresentesBD) {
        this.elementosPresentesBD = elementosPresentesBD;
    }
    /**
     * @return Retorna el/la codigoConvenioSeleccionado.
     */
    public int getCodigoConvenioSeleccionado() {
        return codigoConvenioSeleccionado;
    }
    /**
     * @param codigoConvenioSeleccionado El/La codigoConvenioSeleccionado a establecer.
     */
    public void setCodigoConvenioSeleccionado(int codigoConvenioSeleccionado) {
        this.codigoConvenioSeleccionado = codigoConvenioSeleccionado;
    }
    
    /**
     * Método que asigna un nuevo número al número de
     * elementos presentes en el mapa
     * 
     * @param nuevoTamanio
     */
    public void setNumElementosHashMap(int nuevoTamanio)
    {
        this.hashPresentesBD.setTamanio(nuevoTamanio);
    }
    
    /**
     * Método que me dice el número de elementos
     * presentes en la BD. 
     * @return
     */
    public int getNumElementosMemoria ()
    {
        if (!this.esSoloConsulta)
        {
            return this.hashPresentesBD.getTamanio();
        }
        else
        {
            if (this.elementosPresentesBD!=null)
            {
                return this.elementosPresentesBD.size();
            }
            else
            {
                return 0;
            }
        }
    }
    /**
     * @return Retorna el/la esSoloConsulta.
     */
    public boolean getEsSoloConsulta() {
        return esSoloConsulta;
    }
    /**
     * El/La esSoloConsulta a establecer.
     * @param esSoloConsulta 
     */
    public void setEsSoloConsulta(boolean esSoloConsulta) {
        this.esSoloConsulta = esSoloConsulta;
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
     * @return Retorna el/la indiceRequisitoAgregar.
     */
    public int getIndiceRequisitoAgregar() {
        return indiceRequisitoAgregar;
    }
    /**
     * El/La indiceRequisitoAgregar a establecer.
     * @param indiceRequisitoAgregar 
     */
    public void setIndiceRequisitoAgregar(int codigoRequisitoAgregar) {
        this.indiceRequisitoAgregar = codigoRequisitoAgregar;
    }
    
    public void agregarElementoHashMap ()
    {
        //Primero tomamos el índice del elemento
        //seleccionado, este índice nos permitira
        int numElementos=this.hashPresentesBD.getTamanio();
        this.setProcesoRealizado(ConstantesBD.acronimoNo);
        hashPresentesBD.getMapa().put("codigoRequisito_"+ numElementos, hashPresentesBD.getMapa().get("codigoReqNoUsado_" + this.indiceRequisitoAgregar));
        hashPresentesBD.getMapa().put("requisito_" + numElementos, hashPresentesBD.getMapa().get("reqNoUsado_" + this.indiceRequisitoAgregar));
        hashPresentesBD.getMapa().put("tipoRequisito_" + numElementos, hashPresentesBD.getMapa().get("tipoReqNoUsado_" + this.indiceRequisitoAgregar));
        hashPresentesBD.getMapa().put("codigoConvenio_"+ numElementos, this.getCodigoConvenioSeleccionado()+""); 
        hashPresentesBD.getMapa().put("convenio_"+ numElementos, "Ag. Usuario");
        hashPresentesBD.getMapa().put("vieneBD_" + numElementos,  ConstantesBD.valorFalseEnString);
        hashPresentesBD.getMapa().put("eliminar_" + numElementos, ConstantesBD.valorFalseEnString);
        hashPresentesBD.getMapa().put("viaIngreso_" + numElementos, "");
        numElementos++;
        this.hashPresentesBD.setTamanio(numElementos);
        //Ya no se están quitando los requisitos al agregarlos, sino que se validan los repetidos en el validate cuando el tipo es paciente
        if(this.tipoRequisitos.equals("Radicacion"))
        	this.usarUnNuevoRequisito();
    }
    
    public void eliminarRequisito ()
    {
        int i=0;
        
        //Debemos bajar en 1 a todos los índices que
        //sigan despuès de eliminado
        
        this.setProcesoRealizado(ConstantesBD.acronimoNo);
        String codigoRequisitoAEliminar=(String)hashPresentesBD.getMapa().get("codigoRequisito_" + indiceRequisitoAEliminar );
        String codigoViaIngresoEliminar=(String)hashPresentesBD.getMapa().get("viaIngreso_" + indiceRequisitoAEliminar );
        String requisitoAEliminar=(String)hashPresentesBD.getMapa().get("requisito_" + indiceRequisitoAEliminar );
        String tipoRequisitoAEliminar=(String)hashPresentesBD.getMapa().get("tipoRequisito_" + indiceRequisitoAEliminar );
        String deboAgregarAListaEliminar=(String)hashPresentesBD.getMapa().get("vieneBD_" + indiceRequisitoAEliminar );
        
        for (i=indiceRequisitoAEliminar+1;i<this.hashPresentesBD.getTamanio();i++)
        {
            hashPresentesBD.getMapa().
            	put("codigoRequisito_"+ (i-1), 
            	    hashPresentesBD.getMapa().get("codigoRequisito_" + i ));
            hashPresentesBD.getMapa().
            	put("requisito_" + (i-1), 
            	    hashPresentesBD.getMapa().get("requisito_" + i ));
            hashPresentesBD.getMapa().
        	put("tipoRequisito_" + (i-1), 
        	    hashPresentesBD.getMapa().get("tipoRequisito_" + i ));
            hashPresentesBD.getMapa().
            	put("codigoConvenio_"+ (i-1), 
                	hashPresentesBD.getMapa().get("codigoConvenio_" + i ));
            hashPresentesBD.getMapa().
            	put("convenio_"+ (i-1), 
                	hashPresentesBD.getMapa().get("convenio_" + i ));
            hashPresentesBD.getMapa().
            	put("vieneBD_" + (i-1), 
                	hashPresentesBD.getMapa().get("vieneBD_" + i ));
            hashPresentesBD.getMapa().
            	put("eliminar_" + (i-1), 
                	hashPresentesBD.getMapa().get("eliminar_" + i ));
            hashPresentesBD.getMapa().
	        	put("viaIngreso_" + (i-1), 
	            	hashPresentesBD.getMapa().get("viaIngreso_" + i ));
        }
        hashPresentesBD.setTamanio(hashPresentesBD.getTamanio()-1);
        
        if (deboAgregarAListaEliminar.equals(ValoresPorDefecto.getValorTrueParaConsultas()))
        {
            this.hashPresentesBD.getMapa().put("codigoRequisitosEliminado_"+this.numElementosEliminados, codigoRequisitoAEliminar);
            this.hashPresentesBD.getMapa().put("viaIngresoEliminado_"+this.numElementosEliminados, codigoViaIngresoEliminar);
            this.setNumElementosEliminados(this.numElementosEliminados+1);
        }
        this.hashPresentesBD.getMapa().
    		put("codigoReqNoUsado_" + this.getNumRequisitosNoUsados(), codigoRequisitoAEliminar);
    
        this.hashPresentesBD.getMapa().
			put("reqNoUsado_" + this.getNumRequisitosNoUsados(), requisitoAEliminar);
        
        this.hashPresentesBD.getMapa().
			put("tipoReqNoUsado_" + this.getNumRequisitosNoUsados(), tipoRequisitoAEliminar);
        
        this.numRequisitosNoUsados++;
    }
    
    /**
     * Método privado que permite usar un requisito
     * El objetivo de este método es evitar que el
     * usuario tenga disponible este requisito para
     * futuras ocasiones, ya que ha sido usado 
     */
    private void usarUnNuevoRequisito ()
    {
        int i=0;
        
        for (i=this.indiceRequisitoAgregar+1;i<this.numRequisitosNoUsados;i++)
        {
            hashPresentesBD.getMapa().
            	put("codigoReqNoUsado_" + (i-1), 
            	        hashPresentesBD.getMapa().get("codigoReqNoUsado_" + i)
            	        );
            hashPresentesBD.getMapa().
            	put("reqNoUsado_" + (i-1),
            	    hashPresentesBD.getMapa().get("reqNoUsado_" + i)
            	    );
        }
        
        //Una vez usado, debemos bajar el número de 
        //requisitos no usados en 1
        this.numRequisitosNoUsados=this.numRequisitosNoUsados-1;
    }
    
    /**
     * @return Retorna el/la numRequisitosNoUsados.
     */
    public int getNumRequisitosNoUsados() {
        return numRequisitosNoUsados;
    }
    /**
     * El/La numRequisitosNoUsados a establecer.
     * @param numRequisitosNoUsados 
     */
    public void setNumRequisitosNoUsados(int numRequisitosNoUsados) {
        this.numRequisitosNoUsados = numRequisitosNoUsados;
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
     * @return Retorna el/la numElementosEliminados.
     */
    public int getNumElementosEliminados() {
        return numElementosEliminados;
    }
    /**
     * El/La numElementosEliminados a establecer.
     * @param numElementosEliminados 
     */
    public void setNumElementosEliminados(int numElementosEliminados) {
        this.numElementosEliminados = numElementosEliminados;
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
     * @return Returns the tipoRequisitos.
     */
    public String getTipoRequisitos() {
        return tipoRequisitos;
    }
    /**
     * @param tipoRequisitos The tipoRequisitos to set.
     */
    public void setTipoRequisitos(String tipoRequisitos) {
        this.tipoRequisitos = tipoRequisitos;
    }


	/**
	 * @return the procesoRealizado
	 */
	public String getProcesoRealizado() {
		return procesoRealizado;
	}


	/**
	 * @param procesoRealizado the procesoRealizado to set
	 */
	public void setProcesoRealizado(String procesoRealizado) {
		this.procesoRealizado = procesoRealizado;
	}
    
}
