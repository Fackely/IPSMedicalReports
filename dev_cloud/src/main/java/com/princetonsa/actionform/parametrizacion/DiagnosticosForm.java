/*
 * @(#)DiagnosticosForm.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_04
 *
 */


package com.princetonsa.actionform.parametrizacion;

import java.util.Collection;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.ResultadoBoolean;
import util.UtilidadValidacion;
import util.ValoresPorDefecto;


/**
 * Clase para manejar los atributos necesarios en vista
 * para la funcionalidad de control específicada en
 * DiagnosticosAction
 * 
 *	@version 1.0, 17/08/2004
 */
public class DiagnosticosForm extends ValidatorForm
{

    private String logHistorial="";
	/**
	 * Estado en el que se encuentra el proceso.
	 */
	private String estado = "";

	/**
	 * En el caso que el estado sea finalizar,
	 * hay que distinguir entre que se debe hacer.
	 * En esta clase esta por uniformidad y seguridad
	 * (cualquiera puede poner el parámetro en el path)
	 * Valores validos: insertar
	 * 
	 */
	private String accionAFinalizar="";
	
	/**
	 * Acronimo del diagnostico con el que se está
	 * trabajando
	 */
	private String acronimo="";
	
	/**
	 * Código del tipo CIE del diagnostico con el 
	 * que se está trabajando
	 */
	private int codigoTipoCie=ValoresPorDefecto.getCodigoTipoCieActual();
	
	/**
	 * Nombre del tipo CIE del diagnostico con el 
	 * que se está trabajando
	 */
	private String nombreTipoCie="";
	
	/**
	 * Descripción del diagnostico con el que se está
	 * trabajando
	 */
	private String descripcion="";
	
	/**
	 * Boolean que indica si se está trabajando con
	 * un diagnostico activo o no
	 */
	private boolean activo=true;
	
	/**
	 * Sexo 	 del diagnostico con el que se está
	 * trabajando
	 */
	private int sexo = 0;
	
	/**
	 * Edad Inicial 	 del diagnostico con el que se está
	 * trabajando
	*/

	private String edadInicial = "";
	
	/**
	 * Edad Final 	 del diagnostico con el que se está
	 * trabajando
	*/

	private String edadFinal = "";
	
	/**
	 * Es diagnostico principal
	 * 
	*/

	private String esPrincipal = "";
	
	/**
	 * Es diagnostico de Muerte
	*/

	private String esMuerte = "";
	/**
	 * Boolean que me indica si me encuentro
	 * en modifica o en algún otro estado
	 */
	private boolean enModificar=false;
	
	/**
	 * Colecion que almacena los datos de la consulta avanzada.
	 */
	private Collection coleccion;
	
	/**
	 * Almacena el dato del acronimo de la ultima busqueda avanzada.
	 */
	private String acronimoUltimaBusqueda;
	/**
	 * Almacena el dato del CIE de la ultima busqueda avanzada. 
	 */
	private int coditoTipoCieUltimaBusqueda;
	/**
	 * Almacena el dato de la descripcion de la ultima busqueda avanzada.
	 */
	private String descripcionUltimaBusqueda;

	/**
	 * Almacena el dato del sexo de la ultima busqueda avanzada.
	 */	
	private int sexoUltimaBusqueda ;

	/**
	 * Almacena el dato de la edad inicial de la ultima busqueda avanzada.
	 */	
	private String edadInicialUltimaBusqueda;
	
	/**
	 * Almacena el dato de la edad final de la ultima busqueda avanzada.
	 */	
	private String edadFinalUltimaBusqueda;

	/**
	 * Almacena el dato de Es principal de la ultima busqueda avanzada.
	 */	
	private String esPrincipalUltimaBusqueda;

	/**
	 * Almacena el dato de Es muerte de la ultima busqueda avanzada.
	 */	
	private String esMuerteUltimaBusqueda;	
	
	/**
	 * Variables para ordenamiento
	 */
	private String campo="";
	private String ultimaPropiedad="";
	/**
	 * Almacena el dato del estado de la Ultima Busqueda.
	 */
	private boolean activoUltimaBusqueda;
	
	/**
	 * Boolean utilizado en la consulta de todos los diagnosticos
	 * sin importar si son activos o no.
	 */
	private boolean consultarTodos=true;
	/**
	 * Reset NO estandar, para limpiar al terminar todo el proceso, NO al
	 * cambiar de página. Limpia TODOS los datos menos el estado y el
	 * mensaje de resumen
	 */
	
	//objetos de valores de seleccion
	private HashMap hsexo = new HashMap();
	private int numhSexo;	
	private String nomhSexo;
	
	
	/*
	 *variable para signar el mensaje 
	 */
	private ResultadoBoolean mensaje=new ResultadoBoolean(false);
	
	public void reset ()
	{
	    accionAFinalizar="";
		acronimo="";
		nombreTipoCie="";
		descripcion="";
		activo=true;
		consultarTodos=true;
		//codigoTipoCie=ValoresPorDefecto.getCodigoTipoCieActual();
		codigoTipoCie=ConstantesBD.codigoNuncaValido;
		sexo = 0;
		edadInicial = "";
		edadFinal = "";
		esPrincipal = "N";
		esMuerte = "N";
		this.hsexo = new HashMap();
		this.numhSexo = 0;		
		this.nomhSexo ="";
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
		if (estado!=null&&accionAFinalizar!=null&&estado.equals("salir")&&(accionAFinalizar.equals("ingresar")||accionAFinalizar.equals("modificar")))
		{
		    if (this.descripcion!=null&&descripcion.length()>4000)
		    {
		        errors.add("errors.maximoTamanioExcedido1", new ActionMessage("errors.maximoTamanioExcedido", 4000+"", "descripcion"));
		    }
		    if (this.acronimo==null||this.acronimo.equals(""))
		    {
		        errors.add("errors.required1", new ActionMessage("errors.required", "El código"));
		    }
		    if (this.codigoTipoCie==ConstantesBD.codigoNuncaValido||this.codigoTipoCie<=0)
		    {
		        errors.add("errors.seleccion1", new ActionMessage("errors.seleccion", "Tipo de Cie"));
		    }
		    if(this.descripcion==null||descripcion.equals(""))
		    {
		        errors.add("errors.required1", new ActionMessage("errors.required", "La descripción"));
		    }
		    if(UtilidadValidacion.diagnosticoExistente(this.acronimo,this.codigoTipoCie)&&accionAFinalizar.equals("ingresar"))
		    {
		    	errors.add("Diagnostico ya existe",new ActionMessage("errors.yaExiste","Un Diagnostico con ese código"));
		    }
		    //
		    //if (this.edadInicial==ConstantesBD.codigoNuncaValido||this.edadInicial<0)
		    
		    if (this.edadInicial==null||this.edadInicial.equals(""))
		    {
		        errors.add("errors.seleccion1", new ActionMessage("errors.seleccion", "Edad Inicial"));
		    }
		    if (this.edadFinal==null||this.edadFinal.equals(""))
		    {
		        errors.add("errors.seleccion1", new ActionMessage("errors.seleccion", "Edad Final"));
		    }
		    /////////////////
		    int intEdadFinal = 0;
		    int intEdadInicial = 0;
		    
		    
		    if (this.edadFinal!=null&&!this.edadFinal.equals(""))
		    {
		    	intEdadFinal = Integer.parseInt(this.edadFinal);
		    }
		    
		    if (this.edadInicial!=null&&!this.edadInicial.equals(""))
		    {
		    	intEdadInicial = Integer.parseInt(this.edadInicial);
		    }
		    
		    
			//if( this.edadFinal. <this.edadInicial&&this.edadFinal!=0)
		    if( intEdadFinal<intEdadInicial&&intEdadFinal!=0)
			{
				errors.add("edad final menor", new ActionMessage("errors.debeSerNumeroMayor", "La edad final ","la edad inicial "));
			}
			
			//if(this.edadFinal>54750)
		    if(intEdadFinal>54750)
			{
				errors.add("",new ActionMessage("errors.MenorIgualQue","La edad final","54750"));
			}
			
		    if (this.esPrincipal==null||this.esPrincipal.equals(""))
		    {
		        errors.add("errors.required1", new ActionMessage("errors.required", "Es Principal"));
		    }		    
		    if (this.esMuerte==null||this.esMuerte.equals(""))
		    {
		        errors.add("errors.required1", new ActionMessage("errors.required", "Es Muerte"));
		    }
		    
		    if(estado.equals("modificar"))
		    	enModificar=true;
		}
		return errors;
	}
	/**
     * @return Returns the accionAFinalizar.
     */
    public String getAccionAFinalizar()
    {
        return accionAFinalizar;
    }
    /**
     * @param accionAFinalizar The accionAFinalizar to set.
     */
    public void setAccionAFinalizar(String accionAFinalizar)
    {
        this.accionAFinalizar = accionAFinalizar;
    }
    /**
     * @return Returns the acronimo.
     */
    public String getAcronimo()
    {
        return acronimo;
    }
    /**
     * @param acronimo The acronimo to set.
     */
    public void setAcronimo(String acronimo)
    {
        this.acronimo = acronimo;
    }
    /**
     * @return Returns the activo.
     */
    public boolean getActivo()
    {
        return activo;
    }
    /**
     * @param activo The activo to set.
     */
    public void setActivo(boolean activo)
    {
        this.activo = activo;
    }

    /**
     * Método que me dice si debo buscar por acrónimo
     */
    public boolean getBuscarPorAcronimo()
    {
        if (this.acronimo==null||this.acronimo.equals(""))
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    /**
     * Método que me dice si debo buscar por descripción
     */
    public boolean getBuscarPorDescripcion()
    {
        if (this.descripcion==null||this.descripcion.equals(""))
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    /**
     * @return Returns the buscarPorTipoCie.
     */
    public boolean getBuscarPorTipoCie()
    {
        if (this.codigoTipoCie==ConstantesBD.codigoNuncaValido||this.codigoTipoCie<=0)
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    /**
     * @return Returns the buscar por activo.
     */
    public boolean getBuscarPorActivo()
    {
        if (this.consultarTodos)
        {
            return false;
        }
        else
        {
            return true;
        }
    }
    //////////
    /**
     * Método que me dice si debo buscar por sexo
     */    
    public boolean getBuscarPorSexo()
    {
        if (this.sexo==ConstantesBD.codigoNuncaValido||this.sexo<=0)
        {
            return false;
        }
        else
        {
            return true;
        }
    }    
    
    /**
     * Método que me dice si debo buscar por edad inicial
     */    
    public boolean getBuscarPorEdadInicial()
    {
    	//if (this.edadInicial==ConstantesBD.codigoNuncaValido||this.edadInicial<0)
	    if (this.edadInicial==null||this.edadInicial.equals(""))
        {
            return false;
        }
        else
        {
            return true;
        }
    }      
    
    /**
     * Método que me dice si debo buscar por edad final
     */    
    public boolean getBuscarPorEdadFinal()
    {
        //if (this.edadFinal==ConstantesBD.codigoNuncaValido||this.edadFinal<=0)
    	if (this.edadFinal==null||this.edadFinal.equals(""))
        {
            return false;
        }
        else
        {
            return true;
        }
    }    
    
    /**
     * Método que me dice si debo buscar por Es principal
     */    
    public boolean getBuscarPorEsPrincipal()
    {
        if (this.esPrincipal==null||this.esPrincipal.equals(""))
        {
            return false;
        }
        else
        {
            return true;
        }
    }
    
    /**
     * Método que me dice si debo buscar por Es Muerte
     */    
    public boolean getBuscarPorEsMuerte()
    {
        if (this.esMuerte==null||this.esMuerte.equals(""))
        {
            return false;
        }
        else
        {
            return true;
        }
    }
    
    /**
     * @return Returns the codigoTipoCie.
     */
    public int getCodigoTipoCie()
    {
        return codigoTipoCie;
    }
    /**
     * @param codigoTipoCie The codigoTipoCie to set.
     */
    public void setCodigoTipoCie(int codigoTipoCie)
    {
        this.codigoTipoCie = codigoTipoCie;
    }
    /**
     * @return Returns the descripcion.
     */
    public String getDescripcion()
    {
        return descripcion;
    }
    /**
     * @param descripcion The descripcion to set.
     */
    public void setDescripcion(String descripcion)
    {
        this.descripcion = descripcion;
    }
    /**
     * @return Returns the estado.
     */
    public String getEstado()
    {
        return estado;
    }
    /**
     * @param estado The estado to set.
     */
    public void setEstado(String estado)
    {
        this.estado = estado;
    }
    /**
     * @return Returns the nombreTipoCie.
     */
    public String getNombreTipoCie()
    {
        return nombreTipoCie;
    }
    /**
     * @param nombreTipoCie The nombreTipoCie to set.
     */
    public void setNombreTipoCie(String nombreTipoCie)
    {
        this.nombreTipoCie = nombreTipoCie;
    }
    /**
     * @return Returns the enModificar.
     */
    public boolean getEnModificar()
    {
        return enModificar;
    }
    /**
     * @param enModificar The enModificar to set.
     */
    public void setEnModificar(boolean enModificar)
    {
        this.enModificar = enModificar;
    }
    /**
     * @return Retorna el coleccion.
     */
    public Collection getColeccion() {
        return coleccion;
    }
    /**
     * @param coleccion Asigna el coleccion.
     */
    public void setColeccion(Collection coleccion) {
        this.coleccion = coleccion;
    }
    /**
     * @return Retorna el acronimoUltimaBusqueda.
     */
    public String getAcronimoUltimaBusqueda() {
        return acronimoUltimaBusqueda;
    }
    /**
     * @param acronimoUltimaBusqueda Asigna el acronimoUltimaBusqueda.
     */
    public void setAcronimoUltimaBusqueda(String acronimoUltimaBusqueda) {
        this.acronimoUltimaBusqueda = acronimoUltimaBusqueda;
    }
    /**
     * @return Retorna el activoUltimaBusqueda.
     */
    public boolean getActivoUltimaBusqueda() {
        return activoUltimaBusqueda;
    }
    /**
     * @param activoUltimaBusqueda Asigna el activoUltimaBusqueda.
     */
    public void setActivoUltimaBusqueda(boolean activoUltimaBusqueda) {
        this.activoUltimaBusqueda = activoUltimaBusqueda;
    }
    /**
     * @return Retorna el coditoTipoCieUltimaBusqueda.
     */
    public int getCoditoTipoCieUltimaBusqueda() {
        return coditoTipoCieUltimaBusqueda;
    }
    /**
     * @param coditoTipoCieUltimaBusqueda Asigna el coditoTipoCieUltimaBusqueda.
     */
    public void setCoditoTipoCieUltimaBusqueda(int coditoTipoCieUltimaBusqueda) {
        this.coditoTipoCieUltimaBusqueda = coditoTipoCieUltimaBusqueda;
    }
    /**
     * @return Retorna el descripcionUltimaBusqueda.
     */
    public String getDescripcionUltimaBusqueda() {
        return descripcionUltimaBusqueda;
    }
    /**
     * @param descripcionUltimaBusqueda Asigna el descripcionUltimaBusqueda.
     */
    public void setDescripcionUltimaBusqueda(String descripcionUltimaBusqueda) {
        this.descripcionUltimaBusqueda = descripcionUltimaBusqueda;
    }
    /**
     * @return Retorna el logHistorial.
     */
    public String getLogHistorial() {
        return logHistorial;
    }
    /**
     * @param logHistorial Asigna el logHistorial.
     */
    public void setLogHistorial(String logHistorial) {
        this.logHistorial = logHistorial;
    }
    
    /**
     * Método que permite conocer el número de
     * diagnósticos encontrados en una búsqueda
     * avanzada
     * 
     * @return
     */
    public int getNumeroDiagnosticos ()
    {
        if (this.coleccion==null)
        {
            return 0;
        }
        else
        {
            return this.coleccion.size();
        }
    }
    
    /**
     * Método que indica si se puede imprimir, para
     * esto se deben cumplir las siguientes condiciones
     * - No está en modificar
     * - Hay al menos 1 registro
     * - Hay máximo 8000 registros
     * @return
     */
    public boolean getPuedoImprimir ()
    {
        if (this.enModificar==false&&this.getNumeroDiagnosticos()>0&&this.getNumeroDiagnosticos()<=8000)
        {
            //Solo se puede imprimir si no se está en
            //modificar, si hay al menos 1 diagnóstico
            //y si son máximo 8000 registros (manejo
            //memoria PDF)
            return true;
        }
        else
        {
            return false;
        }
    }
	/**
	 * @return Returns the campo.
	 */
	public String getCampo() {
		return campo;
	}
	/**
	 * @param campo The campo to set.
	 */
	public void setCampo(String campo) {
		this.campo = campo;
	}
	/**
	 * @return Returns the ultimaPropiedad.
	 */
	public String getUltimaPropiedad() {
		return ultimaPropiedad;
	}
	/**
	 * @param ultimaPropiedad The ultimaPropiedad to set.
	 */
	public void setUltimaPropiedad(String ultimaPropiedad) {
		this.ultimaPropiedad = ultimaPropiedad;
	}
	/**
	 * @return Returns the consultarTodos.
	 */
	public boolean isConsultarTodos() {
		return consultarTodos;
	}
	/**
	 * @param consultarTodos The consultarTodos to set.
	 */
	public void setConsultarTodos(boolean consultarTodos) {
		this.consultarTodos = consultarTodos;
	}



	public String getEsMuerte() {
		return esMuerte;
	}

	public void setEsMuerte(String esMuerte) {
		this.esMuerte = esMuerte;
	}

	public String getEsPrincipal() {
		return esPrincipal;
	}

	public void setEsPrincipal(String esPrincipal) {
		this.esPrincipal = esPrincipal;
	}

	public int getSexo() {
		return sexo;
	}

	public void setSexo(int sexo) {
		this.sexo = sexo;
	}



	public String getEsMuerteUltimaBusqueda() {
		return esMuerteUltimaBusqueda;
	}

	public void setEsMuerteUltimaBusqueda(String esMuerteUltimaBusqueda) {
		this.esMuerteUltimaBusqueda = esMuerteUltimaBusqueda;
	}

	public String getEsPrincipalUltimaBusqueda() {
		return esPrincipalUltimaBusqueda;
	}

	public void setEsPrincipalUltimaBusqueda(String esPrincipalUltimaBusqueda) {
		this.esPrincipalUltimaBusqueda = esPrincipalUltimaBusqueda;
	}

	public int getSexoUltimaBusqueda() {
		return sexoUltimaBusqueda;
	}

	public void setSexoUltimaBusqueda(int sexoUltimaBusqueda) {
		this.sexoUltimaBusqueda = sexoUltimaBusqueda;
	}

	public HashMap getHsexo() {
		return hsexo;
	}

	public void setHsexo(HashMap hsexo) {
		this.hsexo = hsexo;
	}

	public int getNumhSexo() {
		return numhSexo;
	}

	public void setNumhSexo(int numhSexo) {
		this.numhSexo = numhSexo;
	}
	
	public Object getHsexo(String key) {
		return hsexo.get(key);
	}

	public void setHsexo(String key,Object obj) {
		this.hsexo.put(key,obj);
	}

	public String getNomhSexo() {
		return nomhSexo;
	}

	public void setNomhSexo(String nomhSexo) {
		this.nomhSexo = nomhSexo;
	}

	public String getEdadFinal() {
		return edadFinal;
	}

	public void setEdadFinal(String edadFinal) {
		this.edadFinal = edadFinal;
	}

	public String getEdadInicial() {
		return edadInicial;
	}

	public void setEdadInicial(String edadInicial) {
		this.edadInicial = edadInicial;
	}

	public String getEdadFinalUltimaBusqueda() {
		return edadFinalUltimaBusqueda;
	}

	public void setEdadFinalUltimaBusqueda(String edadFinalUltimaBusqueda) {
		this.edadFinalUltimaBusqueda = edadFinalUltimaBusqueda;
	}

	public String getEdadInicialUltimaBusqueda() {
		return edadInicialUltimaBusqueda;
	}

	public void setEdadInicialUltimaBusqueda(String edadInicialUltimaBusqueda) {
		this.edadInicialUltimaBusqueda = edadInicialUltimaBusqueda;
	}
	
	public ResultadoBoolean getMensaje() {
		return mensaje;
	}

	public void setMensaje(ResultadoBoolean mensaje) {
		this.mensaje = mensaje;
	}


}
