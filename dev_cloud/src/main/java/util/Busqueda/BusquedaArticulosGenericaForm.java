/*
 * @(#)BusquedaArticulosGenericaForm.java
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

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.UtilidadTexto;

/**
 * Form que contiene todos los datos específicos para generar la
 * busqueda de articulos
 * 
 * Y adicionalmente hace el manejo de reset de la forma y de
 * validación de errores de datos de entrada.
 * @version 1,0. Dic 14 , 2005
 * @author <a href="mailto:wilson@PrincetonSA.com">Wilson Ríos</a>
 */
public class BusquedaArticulosGenericaForm extends ValidatorForm
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
     * criterio de busqueda
     */
    private String criterioBusqueda;
    
    /**
     * indica si la busqueda es por nombre 
     * o por codigo
     */
    private boolean esBusquedaPorNombre;
    
    /**
     * codigos de los articulos insertados para no repetirlos
     * en la busqueda avanzada de articulos
     */
    private String codigosArticulosInsertados;

    /**
     * boolean que indica si las restricciones van por inventarios 
     * o no.
     */
    private boolean filtrarXInventarios;
    
    /**
     * codigo del centro de costo en caso de que se tenga que filtrar por
     * inventarios
     */
    private String codigoAlmacen;
    
    /**
     * codigo de la transaccion 
     */
    private String codigoTransaccion;
    
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
      * Combinaciones clase-grupo válidas para cosnulta de articulo
      * formato=> 'clase-grupo','clase-grupo',...
      */
     private String parejasClaseGrupo ;
     
     /**
      * filtro, en caso de que sea true solo trae los medicamento
      * caso false insumos
      * caso vacio todos
      */
     private String esMedicamento;
     
     
     
     
     /**
      * filtro, en caso de que sea true solo trae los medicamento POS
      * 
      */
     private String esPos;
     
     
     
     /**
      * Tipo de consignacion para las transacciones
      */
     private String tipoConsignac;
     
     /**
      * Indicador para saber si se debe filtrar por subseccion o seccion 
      */
     private boolean filtrarXSeccionSubseccion;
     
     /**
      * Codigos de las secciones a filtrar
      */
     private String codigosSecciones; //codigos secciones separadas por comas
     
     /**
      * Codigo de la subseccion
      */
     private String codigoSubseccion; //codigo de la subseccion
     /**
      * Codigo de la seccion
      */
     private String codigoSeccion; //codigo de la seccion
     
    /**
     * 
     */
     private boolean filtrarXClaseGrupoSub;
     
     /**
     * Codigo de la Clase
     */
     
     private String codigoClase;
     
     /**
      * Codigo del grupo
      */
     
     private String codigoGrupo;
     
     /**
      * Codigo Subgrupo
      */
     private String codigoSubgrupo;
     
     
     /**
      * Filtra por los articulos que tengan preparacion pendiente
      */
      private boolean filtrarXPrepPen;
      
      /**
       * Filtra por los parametros requeridos para realizar un ajuste de inventario físico
       */
     private boolean filtrarXAjusteInvFis;
     
     /**
      * 
      */
     private String tipoDispositivo;
     
     /**
      * 
      */
     private String tipoAccesoVascular;
     
     /**
      * 
      */
     private String mostrarPopupBusquedaAvanzada;
     
     /**
      * Atributo que permite diferenciar si se quiere filtrar por seccion, subseccion, almacen (articulos con ubicacion) o solo por almacen(articulos sin ubicacion). 
      */
     private boolean soloAlmacen=false;
     
     /**
      * Atributo para filtrar por la categoria del articulo
      */
     private int categoria;
     
     /**
      * Atributo para filtrar por la Forma Farmaceutica del articulo
      */
     private String formaFarmaceutica;
     
     /**
      * Atributo para filtrar por Unidad de Medida del articulo
      */
     private String unidadMedida;
     
     /**
      * Boolean utilizado que indica si se usa el párametro por 
      * defecto Clases de Inventario o no en la búsqueda
      */
     private boolean valDefClasesInv; 
     
     /**
      * 
      */
     private String idTercero;
     
     /**
      *  Identifica si la búsuqeda se debe filtrar por medicamentos de control especial
      */
     private String esMedicamentoControlEspecial;
     
     /**
      * Adiciona a la consulta los equivalentes de los articulos filtrados
      */
     private boolean cargarEquivalentes;
     
     /**
      * Agregado por anexo 951 para artculos de atencion odontologica
      */
     private boolean atencionOdontologica;
     
     /**
      * Indica si debe aplicar o no para cargos directos
      */
     private boolean aplicaCargosDirectos;
     
     /**
      * resetea los valores de la forma
      */
     public void reset()
     {
          this.soloAlmacen=false;
          this.criterioBusqueda="";
          this.esBusquedaPorNombre=false;
          this.filtrarXInventarios=false;
          this.codigoAlmacen="";
          this.codigoTransaccion="";
          this.parejasClaseGrupo = "";
          this.esMedicamento="";
          this.tipoConsignac="";
          this.filtrarXSeccionSubseccion = false;
          this.codigosSecciones = "";
          this.codigoSubseccion = "";
          this.codigoSeccion = "";
          this.codigoClase="";
          this.codigoGrupo="";
          this.codigoSubgrupo="";
          this.filtrarXClaseGrupoSub=false;
          this.filtrarXPrepPen=false;
          this.filtrarXAjusteInvFis=false;
          this.mostrarPopupBusquedaAvanzada="";
          this.categoria=0;
          this.formaFarmaceutica="";
          this.unidadMedida="";
          this.valDefClasesInv = false;
          this.idTercero="";
          this.esMedicamentoControlEspecial="";
          this.cargarEquivalentes=false;
          this.atencionOdontologica=false;
          this.aplicaCargosDirectos=false;
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
          
          if(this.estado.equals("busquedaAvanzada"))
          {
              errores=super.validate(mapping,request);
              if(this.criterioBusqueda.trim().equals("") && !this.esBusquedaPorNombre)
                  errores.add("Campo Criterio Busqueda", new ActionMessage("errors.required","El Criterio de Búsqueda por Código"));
              /*else if(this.getCriterioBusqueda().length()<3 && this.esBusquedaPorNombre)
                  errores.add("Campo Criterio Busqueda", new ActionMessage("errors.criterioBusquedaNombre"));*/
              if(!this.esBusquedaPorNombre)
              {
                  try
                  {
                      Integer.parseInt(this.getCriterioBusqueda());
                  }catch(NumberFormatException ne)
                  {
                      errores.add("Campo Criterio Busqueda", new ActionMessage("errors.integer", "Si realiza Búsqueda por Código entonces el criterio "));
                  }
              }
          }
          return errores;
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
     * @return Returns the criterioBusqueda.
     */
    public String getCriterioBusqueda() {
        return criterioBusqueda;
    }
    /**
	 * @return the idTercero
	 */
	public String getIdTercero() {
		return idTercero;
	}

	/**
	 * @param idTercero the idTercero to set
	 */
	public void setIdTercero(String idTercero) {
		this.idTercero = idTercero;
	}

	/**
     * @param criterioBusqueda The criterioBusqueda to set.
     */
    public void setCriterioBusqueda(String criterioBusqueda) {
        this.criterioBusqueda = criterioBusqueda;
    }
    /**
     * @return Returns the esBusquedaPorNombre.
     */
    public boolean getEsBusquedaPorNombre() {
        return esBusquedaPorNombre;
    }
    /**
     * @param esBusquedaPorNombre The esBusquedaPorNombre to set.
     */
    public void setEsBusquedaPorNombre(boolean esBusquedaPorNombre) {
        this.esBusquedaPorNombre = esBusquedaPorNombre;
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
     * @return Returns the codigosArticulosInsertados.
     */
    public String getCodigosArticulosInsertados() {
        return codigosArticulosInsertados;
    }
    /**
     * @param codigosArticulosInsertados The codigosArticulosInsertados to set.
     */
    public void setCodigosArticulosInsertados(String codigosArticulosInsertados) {
        this.codigosArticulosInsertados = codigosArticulosInsertados;
    }
    /**
     * @return Returns the codigoAlmacen.
     */
    public String getCodigoAlmacen() {
        return codigoAlmacen;
    }
    /**
     * @param codigoAlmacen The codigoAlmacen to set.
     */
    public void setCodigoAlmacen(String codigoAlmacen) {
        this.codigoAlmacen = codigoAlmacen;
    }
    /**
     * @return Returns the filtrarXInventarios.
     */
    public boolean getFiltrarXInventarios() {
        return filtrarXInventarios;
    }
    /**
     * @param filtrarXInventarios The filtrarXInventarios to set.
     */
    public void setFiltrarXInventarios(boolean filtrarXInventarios) {
        this.filtrarXInventarios = filtrarXInventarios;
    }
    /**
     * @return Returns the codigoTransaccion.
     */
    public String getCodigoTransaccion() {
        return codigoTransaccion;
    }
    /**
     * @param codigoTransaccion The codigoTransaccion to set.
     */
    public void setCodigoTransaccion(String codigoTransaccion) {
        this.codigoTransaccion = codigoTransaccion;
    }
	/**
	 * @return Returns the parejasClaseGrupo.
	 */
	public String getParejasClaseGrupo() {
		return parejasClaseGrupo;
	}
	/**
	 * @param parejasClaseGrupo The parejasClaseGrupo to set.
	 */
	public void setParejasClaseGrupo(String parejasClaseGrupo) {
		this.parejasClaseGrupo = parejasClaseGrupo;
	}

	public String getTipoConsignac() 
	{
		if(UtilidadTexto.isEmpty(tipoConsignac))
			return "";
		return tipoConsignac;
	}

	public void setTipoConsignac(String tipoConsignac) {
		this.tipoConsignac = tipoConsignac;
	}

	/**
	 * @return the esMedicamento
	 */
	public String getEsMedicamento() 
	{
		if(UtilidadTexto.isEmpty(this.esMedicamento))
			return "";
		return esMedicamento;
	}

	/**
	 * @param esMedicamento the esMedicamento to set
	 */
	public void setEsMedicamento(String esMedicamento) {
		this.esMedicamento = esMedicamento;
	}
	
	
	
	
	/**
	 * @return the esPos
	 */
	public String getEsPos() {
		if(UtilidadTexto.isEmpty(this.esPos))
			return "";
		return esPos;
	}

	/**
	 * @param esPos the esPos to set
	 */
	public void setEsPos(String esPos) {
		this.esPos = esPos;
	}
	
	
	
	
	

	/**
	 * @return the codigoSeccion
	 */
	public String getCodigoSeccion() {
		return codigoSeccion;
	}

	/**
	 * @param codigoSeccion the codigoSeccion to set
	 */
	public void setCodigoSeccion(String codigoSeccion) {
		this.codigoSeccion = codigoSeccion;
	}

	/**
	 * @return the codigosSecciones
	 */
	public String getCodigosSecciones() {
		return codigosSecciones;
	}

	/**
	 * @param codigosSecciones the codigosSecciones to set
	 */
	public void setCodigosSecciones(String codigosSecciones) {
		this.codigosSecciones = codigosSecciones;
	}

	/**
	 * @return the codigoSubseccion
	 */
	public String getCodigoSubseccion() {
		return codigoSubseccion;
	}

	/**
	 * @param codigoSubseccion the codigoSubseccion to set
	 */
	public void setCodigoSubseccion(String codigoSubseccion) {
		this.codigoSubseccion = codigoSubseccion;
	}

	/**
	 * @return the filtrarXSeccionSubseccion
	 */
	public boolean isFiltrarXSeccionSubseccion() {
		return filtrarXSeccionSubseccion;
	}

	/**
	 * @param filtrarXSeccionSubseccion the filtrarXSeccionSubseccion to set
	 */
	public void setFiltrarXSeccionSubseccion(boolean filtrarXSeccionSubseccion) {
		this.filtrarXSeccionSubseccion = filtrarXSeccionSubseccion;
	}

	/**
	 * @return the codigoClase
	 */
	public String getCodigoClase() {
		return codigoClase;
	}

	/**
	 * @param codigoClase the codigoClase to set
	 */
	public void setCodigoClase(String codigoClase) {
		this.codigoClase = codigoClase;
	}

	/**
	 * @return the codigoGrupo
	 */
	public String getCodigoGrupo() {
		return codigoGrupo;
	}

	/**
	 * @param codigoGrupo the codigoGrupo to set
	 */
	public void setCodigoGrupo(String codigoGrupo) {
		this.codigoGrupo = codigoGrupo;
	}

	/**
	 * @return the codigoSubgrupo
	 */
	public String getCodigoSubgrupo() {
		return codigoSubgrupo;
	}

	/**
	 * @param codigoSubgrupo the codigoSubgrupo to set
	 */
	public void setCodigoSubgrupo(String codigoSubgrupo) {
		this.codigoSubgrupo = codigoSubgrupo;
	}

	/**
	 * @return the filtrarXClaseGrupoSub
	 */
	public boolean isFiltrarXClaseGrupoSub() {
		return filtrarXClaseGrupoSub;
	}

	/**
	 * @param filtrarXClaseGrupoSub the filtrarXClaseGrupoSub to set
	 */
	public void setFiltrarXClaseGrupoSub(boolean filtrarXClaseGrupoSub) {
		this.filtrarXClaseGrupoSub = filtrarXClaseGrupoSub;
	}

	/**
	 * @return the filtrarXPrepPen
	 */
	public boolean isFiltrarXPrepPen() {
		return filtrarXPrepPen;
	}

	/**
	 * @param filtrarXPrepPen the filtrarXPrepPen to set
	 */
	public void setFiltrarXPrepPen(boolean filtrarXPrepPen) {
		this.filtrarXPrepPen = filtrarXPrepPen;
	}

	/**
	 * @return the filtrarXAjusteInvFis
	 */
	public boolean isFiltrarXAjusteInvFis() {
		return filtrarXAjusteInvFis;
	}

	/**
	 * @param filtrarXAjusteInvFis the filtrarXAjusteInvFis to set
	 */
	public void setFiltrarXAjusteInvFis(boolean filtrarXAjusteInvFis) {
		this.filtrarXAjusteInvFis = filtrarXAjusteInvFis;
	}

	/**
	 * @return the tipoDispositivo
	 */
	public String getTipoDispositivo() 
	{
		if(UtilidadTexto.isEmpty(tipoDispositivo))
			return "";
		return tipoDispositivo;
	}

	/**
	 * @param tipoDispositivo the tipoDispositivo to set
	 */
	public void setTipoDispositivo(String tipoDispositivo) {
		this.tipoDispositivo = tipoDispositivo;
	}

	/**
	 * @return the tipoAccesoVascular
	 */
	public String getTipoAccesoVascular() 
	{
		if(UtilidadTexto.isEmpty(tipoAccesoVascular))
			return "";
		return tipoAccesoVascular;
	}

	/**
	 * @param tipoAccesoVascular the tipoAccesoVascular to set
	 */
	public void setTipoAccesoVascular(String tipoAccesoVascular) {
		this.tipoAccesoVascular = tipoAccesoVascular;
	}

	/**
	 * @return the mostrarPopupBusquedaAvanzada
	 */
	public String getMostrarPopupBusquedaAvanzada() {
		return mostrarPopupBusquedaAvanzada;
	}

	/**
	 * @param mostrarPopupBusquedaAvanzada the mostrarPopupBusquedaAvanzada to set
	 */
	public void setMostrarPopupBusquedaAvanzada(String mostrarPopupBusquedaAvanzada) {
		this.mostrarPopupBusquedaAvanzada = mostrarPopupBusquedaAvanzada;
	}

	public boolean isSoloAlmacen() {
		return soloAlmacen;
	}

	public void setSoloAlmacen(boolean soloAlmacen) {
		this.soloAlmacen = soloAlmacen;
	}

	public int getCategoria() {
		return categoria;
	}

	public void setCategoria(int categoria) {
		this.categoria = categoria;
	}

	public String getFormaFarmaceutica() {
		if(UtilidadTexto.isEmpty(formaFarmaceutica))
			return "";
		return formaFarmaceutica;
	}

	public void setFormaFarmaceutica(String formaFarmaceutica) {
		this.formaFarmaceutica = formaFarmaceutica;
	}

	public String getUnidadMedida() {
		if(UtilidadTexto.isEmpty(unidadMedida))
			return "";
		return unidadMedida;
	}

	public void setUnidadMedida(String unidadMedida) {
		this.unidadMedida = unidadMedida;
	}

	/**
	 * @return the valDefClasesInv
	 */
	public boolean isValDefClasesInv() {
		return valDefClasesInv;
	}

	/**
	 * @param valDefClasesInv the valDefClasesInv to set
	 */
	public void setValDefClasesInv(boolean valDefClasesInv) {
		this.valDefClasesInv = valDefClasesInv;
	}

	public String getEsMedicamentoControlEspecial() {
		return esMedicamentoControlEspecial;
	}

	public void setEsMedicamentoControlEspecial(String esMedicamentoControlEspecial) {
		this.esMedicamentoControlEspecial = esMedicamentoControlEspecial;
	}

	public boolean isAtencionOdontologica() {
		return atencionOdontologica;
	}

	public void setAtencionOdontologica(boolean atencionOdontologica) {
		this.atencionOdontologica = atencionOdontologica;
	}
	
	/**
	 * @return the cargarEquivalentes
	 */
	public boolean isCargarEquivalentes() {
		return cargarEquivalentes;
	}

	/**
	 * @param cargarEquivalentes the cargarEquivalentes to set
	 */
	public void setCargarEquivalentes(boolean cargarEquivalentes) {
		this.cargarEquivalentes = cargarEquivalentes;
	}

	/**
	 * @return the aplicaCargosDirectos
	 */
	public boolean isAplicaCargosDirectos() {
		return aplicaCargosDirectos;
	}

	/**
	 * @param aplicaCargosDirectos the aplicaCargosDirectos to set
	 */
	public void setAplicaCargosDirectos(boolean aplicaCargosDirectos) {
		this.aplicaCargosDirectos = aplicaCargosDirectos;
	}

	
}