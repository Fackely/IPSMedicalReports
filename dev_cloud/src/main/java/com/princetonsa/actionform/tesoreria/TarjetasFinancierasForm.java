
/*
 * Creado   20/09/2005
 *
 *Copyright Princeton S.A. &copy;&reg; 2004. Todos los Derechos Reservados.
 *
 * Lenguaje		:Java
 * Compilador	:J2SDK 1.5.0_03
 */
package com.princetonsa.actionform.tesoreria;


import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ResultadoBoolean;
import util.UtilidadTexto;

import com.princetonsa.dto.comun.DtoCheckBox;
import com.princetonsa.dto.tesoreria.DtoEntidadesFinancieras;
import com.servinte.axioma.orm.CentroAtencion;
import com.servinte.axioma.orm.ComisionXCentroAtencion;
import com.servinte.axioma.orm.Instituciones;
import com.servinte.axioma.orm.TarjetaFinancieraComision;
import com.servinte.axioma.orm.TarjetaFinancieraReteica;
import com.servinte.axioma.orm.TarjetasFinancieras;
import com.servinte.axioma.orm.TiposTarjetaFinanciera;

/**
 * 
 *
 * @version 1.0, 20/09/2005
 * @author <a href="mailto:joan@PrincetonSA.com">Joan L&oacute;pez</a>
 * 
 * 
 * Desarrollada nuevamente el 15 de abril de 2010
 * @author armando
 */
public class TarjetasFinancierasForm extends ActionForm 
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
     * estado del workflow
     */
    private String estado;
    
    private TarjetasFinancieras tarjetaFinanciera=new TarjetasFinancieras();
    
    private ArrayList<TarjetaFinancieraComision> comision=new ArrayList<TarjetaFinancieraComision>();

    private ArrayList<TarjetaFinancieraReteica> reteica=new ArrayList<TarjetaFinancieraReteica>();
     
    private ArrayList<TarjetaFinancieraComision> entidadesEliminadas=new ArrayList<TarjetaFinancieraComision>();

    private ArrayList<TarjetaFinancieraReteica> centroAtencionEliminado=new ArrayList<TarjetaFinancieraReteica>();
    
    private ArrayList<CentroAtencion> listadoCentrosAtencion=new ArrayList<CentroAtencion>();
    
    private int tarjetaFinancieraSeleccionada;
    
    private int registroSeleccionadoEliminacion;
    
    private ArrayList<DtoEntidadesFinancieras> entidades=new ArrayList<DtoEntidadesFinancieras>();
    
	private ArrayList<TarjetasFinancieras> tarjetasFinancieras=new ArrayList<TarjetasFinancieras>();
	
	private ArrayList<TiposTarjetaFinanciera> tiposTarjetaFinanciera=new ArrayList<TiposTarjetaFinanciera>();
	
    private int registroSeleccionado;
    
    private int registroSeleccionadoCA;
    
	/**
     * Variable para manejar el mensaje de Operación realizada con exito
     */
    private ResultadoBoolean mensaje = new ResultadoBoolean(false);
    
    /**
     * Atributo que indica si se muestra o no desplegada la secci&oacute;n Retención ICA
     */
    private boolean mostrarReteICA;
    
    /**
     * Atributo que indica si se muestra o no desplegada la secci&oacute;n Comisión
     */
    private boolean mostrarComision;
    
    /**
     * Atributo que indica si la institución es multiempresa o no.
     */
    private boolean institucionMultiEmpresa;
    
    
    /**
     * Listado con las Instituciones registradas en el sistema
     */
    private ArrayList<Instituciones> instituciones;
    
    /**
     * Código de la institución seleccionada cuando el parámetro Institución es Multiempresa
     * se encuentra en 'SI'. Si el parámetro esta en 'NO', contiene el código de la institución
     * cargada.
     */
    private int codigoInstitucionSeleccionada;
    
    
    
    // COMISION POR CENTRO DE ATENCIÓN
    
    /** * Posición TarjetaFinancieraComision seleccionada de la seccion de comisión */
    private int registroSeleccionadoComisionCentroAten;
    
    /** * TarjetaFinancieraComision seleccionada de la seccion de comisión */
    private TarjetaFinancieraComision tarjetaFinancieraComision = new TarjetaFinancieraComision();
    
    /** * Posición del Centro de atención seleccionado para ingresar en la parametrica de Comisión por Centro de Atención */
    private int registroSeleccionadoCAcomi;
    
    /** * Lista de registro de la parametrica de Comisión por Centro de Atención */
    private ArrayList<ComisionXCentroAtencion> listaComisionXCentroAtencion = new ArrayList<ComisionXCentroAtencion>();
    
    /** * Lista de registro de la parametrica de Comisión por Centro de Atención que serán Eliminados */
    private ArrayList<ComisionXCentroAtencion> listaComisionXCentroAtencionEliminar = new ArrayList<ComisionXCentroAtencion>();
    
    /** * Posición de Comisión por Centro de Atención que serán Eliminados */
    private int posComisionCentroAtencionEliminar;
    
    /** * Indica si se debe mostrar el mensaje de error */
    private boolean errorCargarEntidadComision;
    
    /** * Atributo que almacena el nombre de la columna por la cual deben ser ordenados los registros encontrados. */
	private String patronOrdenar;
   
	/** * Atributo usado para ordenar descendentemente. */
	private String esDescendente;
	
	 /** * Indica que se guardo el registro de la parametrica exitosamente*/
    private boolean guardadoExitoso;
    
    /** * Contiene los codigos de als tarjetas e indica cuales son eliminables */
    private ArrayList<DtoCheckBox> listaTarjetasEliminables;
    
    
    
    
    
    /****************************************
     * ATRIBUTOS PARA EL PAGER
     ****************************************/
	
    /**
     * Para controlar la página actual del pager.
     */
    private int offset;
    
    /**
     * El numero de registros por pager
     */
    private int maxPageItems;
    
    /**
     * Para controlar el link siguiente del pager 
     */
    private String linkSiguiente;
    
    /**
     * para controlar el numero de registros del
     * HashMap.
     */
    private int numRegistros;
    
    
	
	
    public void reset ()
    {
    	tarjetasFinancieras=new ArrayList<TarjetasFinancieras>();
    	tiposTarjetaFinanciera=new ArrayList<TiposTarjetaFinanciera>();
    	this.registroSeleccionado=ConstantesBD.codigoNuncaValido;
    	this.entidadesEliminadas=new ArrayList<TarjetaFinancieraComision>();
    	this.comision=new ArrayList<TarjetaFinancieraComision>();
    	this.reteica=new ArrayList<TarjetaFinancieraReteica>();
    	this.tarjetaFinancieraSeleccionada=ConstantesBD.codigoNuncaValido;
    	this.registroSeleccionadoEliminacion=ConstantesBD.codigoNuncaValido;
    	this.listadoCentrosAtencion=new ArrayList<CentroAtencion>();
    	this.centroAtencionEliminado=new ArrayList<TarjetaFinancieraReteica>();
    	this.registroSeleccionadoCA=ConstantesBD.codigoNuncaValido;
    	this.setMostrarReteICA(false);
    	this.setMostrarComision(false);
    	this.setInstitucionMultiEmpresa(false);
    	this.setInstituciones(new ArrayList<Instituciones>());
    	this.setCodigoInstitucionSeleccionada(ConstantesBD.codigoNuncaValido);
    	
    	this.registroSeleccionadoComisionCentroAten	=ConstantesBD.codigoNuncaValido;
    	this.tarjetaFinancieraComision 				= new TarjetaFinancieraComision();
    	this.registroSeleccionadoCAcomi				= ConstantesBD.codigoNuncaValido;
    	this.listaComisionXCentroAtencion 			= new ArrayList<ComisionXCentroAtencion>();
    	this.listaComisionXCentroAtencionEliminar	= new ArrayList<ComisionXCentroAtencion>();
    	this.posComisionCentroAtencionEliminar		= ConstantesBD.codigoNuncaValido;
    	this.errorCargarEntidadComision				= false;
    	this.setPatronOrdenar("");
    	this.setEsDescendente("");
    	this.guardadoExitoso						= false;
    	this.listaTarjetasEliminables				= new ArrayList<DtoCheckBox>();
    	
        this.patronOrdenar = "";
        this.numRegistros=0;
        this.linkSiguiente = "";
        this.offset = 0;
        this.maxPageItems = 20;
        
    }
    
    

    /**
     * Método que resetea los atributos
     * utilizados para el pager
     */
    public void resetPager()
    {
    	this.maxPageItems = 20;
    	this.linkSiguiente = "";
        this.offset = 0;
    }
    
    
    
    
    
    /**
	 * Metodo de validación
	 * @param mapping
	 * @param request
	 * @return errores ActionError, especifica los errores.
	 */
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
		ActionErrors errores= new ActionErrors();
		if(estado.equals("guardar"))
		{
			for(int i=0;i<tarjetasFinancieras.size();i++)
			{
				TarjetasFinancieras tarjeta=tarjetasFinancieras.get(i);
				if(tarjeta.getCodigo().isEmpty())
				{
					errores.add("codigo", new ActionMessage("errors.required","El Código de la Tarjeta "+(i+1)));
				}
				else
				{
					for(int j=0;j<i;j++)
					{
						TarjetasFinancieras tarjetaInterna=tarjetasFinancieras.get(j);
						if(tarjeta.getCodigo().equals(tarjetaInterna.getCodigo()))
						{
							errores.add("", new ActionMessage("errors.yaExiste","El Código de la Tarjeta "+tarjeta.getCodigo()));
						}
					}
				}
				if(tarjeta.getDescripcion().isEmpty())
				{
					errores.add("descripcion", new ActionMessage("errors.required","La Descripción de la Tarjeta "+(i+1)));
				}
				if(UtilidadTexto.isEmpty(tarjeta.getTiposTarjetaFinanciera().getCodigo()+""))
				{
					errores.add("codigo", new ActionMessage("errors.required","El Tipo de Tarjeta "+(i+1)));
				}
			}
		}
		else if(estado.equals("guardarDetalle"))
		{
			Log4JManager.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> "+this.getTarjetaFinanciera().getCuentaContableReteica());
			
			if(this.tarjetaFinanciera.getBaseRete()<0||this.tarjetaFinanciera.getBaseRete()>100)
			{
				errores.add("errors.range", new ActionMessage("errors.range","Base Rete","0","100, incluyendo decimales "));
			}
			if(this.tarjetaFinanciera.getRetefte()<0||this.tarjetaFinanciera.getRetefte()>100)
			{
				errores.add("errors.range", new ActionMessage("errors.range","% Rete Fte","0","100, incluyendo decimales "));
			}
			if(this.tarjetaFinanciera.getReteiva()==null || this.tarjetaFinanciera.getReteiva().doubleValue()<0||this.tarjetaFinanciera.getReteiva().doubleValue()>100)
			{
				errores.add("errors.range", new ActionMessage("errors.range","% Rete Iva","0","100, incluyendo decimales"));
			}
			for(TarjetaFinancieraComision comisionTempo:comision)
			{
				if(comisionTempo.getComision().doubleValue()<0||comisionTempo.getComision().doubleValue()>100)
				{
					errores.add("errors.range", new ActionMessage("errors.range","Comision para "+comisionTempo.getEntidadesFinancieras().getTerceros().getDescripcion(),"0","100"));
				}
			}
			for(TarjetaFinancieraReteica reteicaTempo:reteica)
			{
				if(reteicaTempo.getReteica().doubleValue()<0||reteicaTempo.getReteica().doubleValue()>100)
				{
					errores.add("errors.range", new ActionMessage("errors.range","% Rete ICA de "+reteicaTempo.getCentroAtencion().getDescripcion(),"0","100, incluyendo decimales "));
				}
			}
			
		}
		/*
		else if(estado.equals("cargarEntidad")){
			
			
			if(getCodigoInstitucionSeleccionada()==ConstantesBD.codigoNuncaValido || getRegistroSeleccionado()==ConstantesBD.codigoNuncaValido){
				
				errores.add("valor requerido Comision", new ActionMessage("error.tarjetaFinanciera.valorRequeridoComision"));
			}
			
			mapping.findForward("seccionComision");
			
		}
		*/
		
		return errores;

	}
	
	public String getEstado() {
		return estado;
	}
	
	public void setEstado(String estado) {
		this.estado = estado;
	}

	public ArrayList<DtoEntidadesFinancieras> getEntidades() {
		return entidades;
	}

	public void setEntidades(ArrayList<DtoEntidadesFinancieras> entidades) {
		this.entidades = entidades;
	}

	public ArrayList<TarjetaFinancieraComision> getEntidadesEliminadas() {
		return entidadesEliminadas;
	}

	public void setEntidadesEliminadas(ArrayList<TarjetaFinancieraComision> entidadesEliminadas) {
		this.entidadesEliminadas = entidadesEliminadas;
	}


	public ArrayList<TarjetaFinancieraComision> getComision() {
		return comision;
	}


	public void setComision(ArrayList<TarjetaFinancieraComision> comision) {
		this.comision = comision;
	}


	public int getTarjetaFinancieraSeleccionada() {
		return tarjetaFinancieraSeleccionada;
	}


	public void setTarjetaFinancieraSeleccionada(int tarjetaFinancieraSeleccionada) {
		this.tarjetaFinancieraSeleccionada = tarjetaFinancieraSeleccionada;
	}


	public int getRegistroSeleccionadoEliminacion() {
		return registroSeleccionadoEliminacion;
	}


	public void setRegistroSeleccionadoEliminacion(
			int registroSeleccionadoEliminacion) {
		this.registroSeleccionadoEliminacion = registroSeleccionadoEliminacion;
	}


	public ArrayList<TarjetaFinancieraReteica> getReteica() {
		return reteica;
	}


	public void setReteica(ArrayList<TarjetaFinancieraReteica> reteica) {
		this.reteica = reteica;
	}


	public ArrayList<TarjetaFinancieraReteica> getCentroAtencionEliminado() {
		return centroAtencionEliminado;
	}


	public void setCentroAtencionEliminado(
			ArrayList<TarjetaFinancieraReteica> centroAtencionEliminado) {
		this.centroAtencionEliminado = centroAtencionEliminado;
	}



	public int getRegistroSeleccionadoCA() {
		return registroSeleccionadoCA;
	}


	public void setRegistroSeleccionadoCA(int registroSeleccionadoCA) {
		this.registroSeleccionadoCA = registroSeleccionadoCA;
	}


	public ArrayList<CentroAtencion> getListadoCentrosAtencion() {
		return listadoCentrosAtencion;
	}


	public void setListadoCentrosAtencion(
			ArrayList<CentroAtencion> listadoCentrosAtencion) {
		this.listadoCentrosAtencion = listadoCentrosAtencion;
	}
	
	public TarjetasFinancieras getTarjetaFinanciera() {
		return tarjetaFinanciera;
	}

	public void setTarjetaFinanciera(TarjetasFinancieras tarjetaFinanciera) {
		this.tarjetaFinanciera = tarjetaFinanciera;
	}
	
	public int getRegistroSeleccionado() {
		return registroSeleccionado;
	}

	public void setRegistroSeleccionado(int registroSeleccionado) {
		this.registroSeleccionado = registroSeleccionado;
	}

    
	public ArrayList<TiposTarjetaFinanciera> getTiposTarjetaFinanciera() {
		return tiposTarjetaFinanciera;
	}

	public void setTiposTarjetaFinanciera(
			ArrayList<TiposTarjetaFinanciera> tiposTarjetaFinanciera) {
		this.tiposTarjetaFinanciera = tiposTarjetaFinanciera;
	}

	public ArrayList<TarjetasFinancieras> getTarjetasFinancieras() {
		return tarjetasFinancieras;
	}

	public void setTarjetasFinancieras(
			ArrayList<TarjetasFinancieras> tarjetasFinancieras) {
		this.tarjetasFinancieras = tarjetasFinancieras;
	}

	public ResultadoBoolean getMensaje() {
		return mensaje;
	}

	public void setMensaje(ResultadoBoolean mensaje) {
		this.mensaje = mensaje;
	}
	
	/**
	 * @param mostrarReteICA the mostrarReteICA to set
	 */
	public void setMostrarReteICA(boolean mostrarReteICA) {
		this.mostrarReteICA = mostrarReteICA;
	}


	/**
	 * @return the mostrarReteICA
	 */
	public boolean isMostrarReteICA() {
		return mostrarReteICA;
	}


	/**
	 * @param mostrarComision the mostrarComision to set
	 */
	public void setMostrarComision(boolean mostrarComision) {
		this.mostrarComision = mostrarComision;
	}


	/**
	 * @return the mostrarComision
	 */
	public boolean isMostrarComision() {
		return mostrarComision;
	}


	/**
	 * @param institucionMultiEmpresa the institucionMultiEmpresa to set
	 */
	public void setInstitucionMultiEmpresa(boolean institucionMultiEmpresa) {
		this.institucionMultiEmpresa = institucionMultiEmpresa;
	}


	/**
	 * @return the institucionMultiEmpresa
	 */
	public boolean isInstitucionMultiEmpresa() {
		return institucionMultiEmpresa;
	}


	/**
	 * @param instituciones the instituciones to set
	 */
	public void setInstituciones(ArrayList<Instituciones> instituciones) {
		this.instituciones = instituciones;
	}


	/**
	 * @return the instituciones
	 */
	public ArrayList<Instituciones> getInstituciones() {
		return instituciones;
	}


	/**
	 * @param codigoInstitucionSeleccionada the codigoInstitucionSeleccionada to set
	 */
	public void setCodigoInstitucionSeleccionada(
			int codigoInstitucionSeleccionada) {
		this.codigoInstitucionSeleccionada = codigoInstitucionSeleccionada;
	}


	/**
	 * @return the codigoInstitucionSeleccionada
	 */
	public int getCodigoInstitucionSeleccionada() {
		return codigoInstitucionSeleccionada;
	}


	public int getRegistroSeleccionadoComisionCentroAten() {
		return registroSeleccionadoComisionCentroAten;
	}


	public void setRegistroSeleccionadoComisionCentroAten(
			int registroSeleccionadoComisionCentroAten) {
		this.registroSeleccionadoComisionCentroAten = registroSeleccionadoComisionCentroAten;
	}


	public TarjetaFinancieraComision getTarjetaFinancieraComision() {
		return tarjetaFinancieraComision;
	}


	public void setTarjetaFinancieraComision(
			TarjetaFinancieraComision tarjetaFinancieraComision) {
		this.tarjetaFinancieraComision = tarjetaFinancieraComision;
	}


	public int getRegistroSeleccionadoCAcomi() {
		return registroSeleccionadoCAcomi;
	}


	public void setRegistroSeleccionadoCAcomi(int registroSeleccionadoCAcomi) {
		this.registroSeleccionadoCAcomi = registroSeleccionadoCAcomi;
	}


	public ArrayList<ComisionXCentroAtencion> getListaComisionXCentroAtencion() {
		return listaComisionXCentroAtencion;
	}


	public void setListaComisionXCentroAtencion(
			ArrayList<ComisionXCentroAtencion> listaComisionXCentroAtencion) {
		this.listaComisionXCentroAtencion = listaComisionXCentroAtencion;
	}


	public ArrayList<ComisionXCentroAtencion> getListaComisionXCentroAtencionEliminar() {
		return listaComisionXCentroAtencionEliminar;
	}


	public void setListaComisionXCentroAtencionEliminar(
			ArrayList<ComisionXCentroAtencion> listaComisionXCentroAtencionEliminar) {
		this.listaComisionXCentroAtencionEliminar = listaComisionXCentroAtencionEliminar;
	}


	public int getPosComisionCentroAtencionEliminar() {
		return posComisionCentroAtencionEliminar;
	}


	public void setPosComisionCentroAtencionEliminar(
			int posComisionCentroAtencionEliminar) {
		this.posComisionCentroAtencionEliminar = posComisionCentroAtencionEliminar;
	}


	public boolean isErrorCargarEntidadComision() {
		return errorCargarEntidadComision;
	}


	public void setErrorCargarEntidadComision(boolean errorCargarEntidadComision) {
		this.errorCargarEntidadComision = errorCargarEntidadComision;
	}


	public void setPatronOrdenar(String patronOrdenar) {
		this.patronOrdenar = patronOrdenar;
	}


	public String getPatronOrdenar() {
		return patronOrdenar;
	}


	public void setEsDescendente(String esDescendente) {
		this.esDescendente = esDescendente;
	}


	public String getEsDescendente() {
		return esDescendente;
	}


	public boolean isGuardadoExitoso() {
		return guardadoExitoso;
	}


	public void setGuardadoExitoso(boolean guardadoExitoso) {
		this.guardadoExitoso = guardadoExitoso;
	}


	public ArrayList<DtoCheckBox> getListaTarjetasEliminables() {
		return listaTarjetasEliminables;
	}

	public void setListaTarjetasEliminables(ArrayList<DtoCheckBox> listaTarjetasEliminables) {
		this.listaTarjetasEliminables = listaTarjetasEliminables;
	}



	public int getOffset() {
		return offset;
	}



	public void setOffset(int offset) {
		this.offset = offset;
	}



	public int getMaxPageItems() {
		return maxPageItems;
	}



	public void setMaxPageItems(int maxPageItems) {
		this.maxPageItems = maxPageItems;
	}



	public String getLinkSiguiente() {
		return linkSiguiente;
	}



	public void setLinkSiguiente(String linkSiguiente) {
		this.linkSiguiente = linkSiguiente;
	}



	public int getNumRegistros() {
		return numRegistros;
	}



	public void setNumRegistros(int numRegistros) {
		this.numRegistros = numRegistros;
	}
}