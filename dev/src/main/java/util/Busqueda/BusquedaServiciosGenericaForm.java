/*
 * @(#)BusquedaServiciosGenericaForm.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */
package util.Busqueda;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import com.servinte.axioma.orm.RecomSerproSerpro;

import util.ConstantesBD;
import util.UtilidadTexto;

/**
 * Form que contiene todos los datos específicos para generar la
 * busqueda de servicios
 * 
 * Y adicionalmente hace el manejo de reset de la forma y de
 * validación de errores de datos de entrada.
 * @version 1,0. Oct 31 , 2005
 * @author <a href="mailto:wilson@PrincetonSA.com">Wilson Ríos</a>
 */
public class BusquedaServiciosGenericaForm extends ValidatorForm
{
	/**
	 * codigo tarifario de la busqueda
	 */
   private String codigo;
   
   /**
    * 
    */
   private String codigoAxioma;
   
   /**
    * descripcion de la busqueda
    */
   private String descripcionServicio;
   
   /**
    * codigos de los servicios insertados para no repetirlos
    * en la busqueda avanzada de servicios () separados por comas)
    */
   private String codigosServiciosInsertados;
   
   /**
    * Filtrado por el sexo del servicio
    */
   private int codigoSexo;
   
	/**
	 * Cadena que tiene los tipos de servicios por los que se filtrará
	 * la busqueda de servicios, estan separados por guion (-)
	 */
   private String filtrarTipoServicio;
   
   /**
	 * Cadena que tiene las naturalezas de servicios por los que se filtrará
	 * la busqueda de servicios, estan separados por guion (-)
	 */
   private String filtrarNaturalezaServicio;
   
   /**
    * estado de la accion
    */
   private String estado;
   
   /**
     * Colección con los datos del listado, ya sea para consulta,
     * como también para búsqueda avanzada (pager)
     */
    private Collection col=null;
    
    /**
     * columna por la cual se quiere ordenar
     */
    private String columna;
    
    /**
     * ultima columna por la cual se ordeno
     */
    private String ultimaPropiedad;
   
    /**
     * 
     */
    private String tipoTarifario;
    
    /**
     * 
     */
    private String mostrarDescripcionCups;
    
    /**
     * Indica si se muestran solo los articulos no pos en (true)
     */
    private boolean filtrarNopos=false;
    
    /**
     * Codigo formulario
     */
    private int codigoFormulario;
    
    /**
     * filtro atencion odontologica
     */
    private boolean atencionOdontologica=false;
    
    /**
     * Variable para validar el tipo de Atencion
     */
    private String tipoAtencion;
    private String nombreForma;
    
    /**
	 * Atributo codigoPrograma que permite filtrar la busqueda. :)
	 */
	private String codigoPrograma;
    
   /**
    * resetea los valores de la forma
    */
   public void reset()
   {
        this.codigo="";
        this.codigoAxioma="";
        this.descripcionServicio="";
        //NO SE PUEDE RESETEAR this.codigosServiciosInsertados="";
        this.codigoSexo=0;
 		//this.filtrarTipoServicio="";
        this.tipoTarifario="";    
        this.codigoFormulario = ConstantesBD.codigoNuncaValido;
        this.filtrarNaturalezaServicio="";
        this.tipoAtencion="";
        this.nombreForma="";
        this.codigoPrograma="";
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
        return errores;
    }
    
    
    public String getNombreForma() {
	return nombreForma;
}

public void setNombreForma(String nombreForma) {
	this.nombreForma = nombreForma;
}

	public String getTipoAtencion() {
	return tipoAtencion;
}

public void setTipoAtencion(String tipoAtencion) {
	this.tipoAtencion = tipoAtencion;
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
     * @return Returns the descripcionServicio.
     */
    public String getDescripcionServicio() {
        return descripcionServicio;
    }
    /**
     * @param descripcionServicio The descripcionServicio to set.
     */
    public void setDescripcionServicio(String descripcionServicio) {
        this.descripcionServicio = descripcionServicio;
    }
    /**
     * @return Returns the estado.
     */
    public String getEstado() {
        return estado;
    }
    /**
     * @param estado The estado to set.
     */
    public void setEstado(String estado) {
        this.estado = estado;
    }
    
    /**
     * Retorna Colección para mostrar datos en el pager
     * @return
     */
    public Collection getCol() {
        return col;
    }
    /**
     * Asigna Colección para mostrar datos en el pager
     * @param collection
     */
    public void setCol(Collection collection) {
        col = collection;
    }
    /**
     * Tamanio de la coleccion
     * @return
     */
    public int getColSize()
    {
        if(col!=null)
            return col.size();
        else
            return 0;
    }
    /**
     * @return Returns the columna.
     */
    public String getColumna() {
        return columna;
    }
    /**
     * @param columna The columna to set.
     */
    public void setColumna(String columna) {
        this.columna = columna;
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
	 * @return Retorna codigoSexo.
	 */
	public int getCodigoSexo()
	{
		return codigoSexo;
	}

	/**
	 * @param codigoSexo Asigna codigoSexo.
	 */
	public void setCodigoSexo(int cosigoSexo)
	{
		this.codigoSexo = cosigoSexo;
	}
	
	/**
	 * @return Returns the filtrarTipoServicio.
	 */
	public String getFiltrarTipoServicio()
	{
		return filtrarTipoServicio;
	}
	/**
	 * @param filtrarTipoServicio The filtrarTipoServicio to set.
	 */
	public void setFiltrarTipoServicio(String filtrarTipoServicio)
	{
		this.filtrarTipoServicio = filtrarTipoServicio;
	}

	public String getTipoTarifario() {
		return tipoTarifario;
	}

	public void setTipoTarifario(String tipoTarifario) {
		this.tipoTarifario = tipoTarifario;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	/**
	 * @return the codigoAxioma
	 */
	public String getCodigoAxioma() 
	{
		if(UtilidadTexto.isEmpty(this.codigoAxioma))
			return "";
		return codigoAxioma;
	}

	/**
	 * @param codigoAxioma the codigoAxioma to set
	 */
	public void setCodigoAxioma(String codigoAxioma) {
		this.codigoAxioma = codigoAxioma;
	}

	public String getMostrarDescripcionCups() 
	{
		if(UtilidadTexto.isEmpty(this.mostrarDescripcionCups))
			return ConstantesBD.acronimoNo;
		return mostrarDescripcionCups;
	}

	public void setMostrarDescripcionCups(String mostrarDescripcionCups) 
	{
		this.mostrarDescripcionCups = mostrarDescripcionCups;
	}
	
	/**
	 * @return the filtrarNopos
	 */
	public boolean isFiltrarNopos() {
		return filtrarNopos;
	}

	/**
	 * @param filtrarNopos the filtrarNopos to set
	 */
	public void setFiltrarNopos(boolean filtrarNopos) {
		this.filtrarNopos = filtrarNopos;
	}

	/**
	 * @return the codigoFormulario
	 */
	public int getCodigoFormulario() {
		return codigoFormulario;
	}

	/**
	 * @param codigoFormulario the codigoFormulario to set
	 */
	public void setCodigoFormulario(int codigoFormulario) {
		this.codigoFormulario = codigoFormulario;
	}

	/**
	 * @return the atencionOdontologica
	 */
	public boolean isAtencionOdontologica() {
		return atencionOdontologica;
	}

	/**
	 * @param atencionOdontologica the atencionOdontologica to set
	 */
	public void setAtencionOdontologica(boolean atencionOdontologica) {
		this.atencionOdontologica = atencionOdontologica;
	}


	public void setCodigoPrograma(String codigoPrograma) {
		this.codigoPrograma = codigoPrograma;
	}

	public String getCodigoPrograma() {
		return codigoPrograma;
	}

	public void setFiltrarNaturalezaServicio(String filtrarNaturalezaServicio) {
		this.filtrarNaturalezaServicio = filtrarNaturalezaServicio;
	}

	public String getFiltrarNaturalezaServicio() {
		return filtrarNaturalezaServicio;
	}

}