package com.princetonsa.actionform.odontologia;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadFecha;
import util.UtilidadTexto;

import com.princetonsa.dto.odontologia.DtoDescuentoOdontologicoAtencion;
import com.princetonsa.dto.odontologia.DtoDescuentosOdontologicos;
import com.princetonsa.dto.odontologia.DtoDetalleDescuentoOdontologico;
import com.princetonsa.dto.odontologia.DtoHistoricoDescuentoOdontologico;
import com.princetonsa.dto.odontologia.DtoHistoricoDescuentoOdontologicoAtencion;
import com.princetonsa.dto.odontologia.DtoHistoricoDetalleDescuentoOdontologico;
import com.princetonsa.dto.odontologia.DtoTiposDeUsuario;
import com.princetonsa.mundo.odontologia.DescuentoOdontologico;
import com.princetonsa.mundo.odontologia.DetalleDescuentoOdontologico;


public class DescuentoOdontologicoForm extends ValidatorForm  {

	
	
	
	/**
	 * 
	 */
	private String tipo;
	
	/**
	 * 
	 * 
	 */
	
	private boolean esConsulta ;
	/**
	 * 
	 * 
	 */
	
	private static Logger logger = Logger.getLogger(DescuentoOdontologicoForm.class);
	
  ///////////////////////PRIMERA PAGINA - ENCABEZADO DETALLE
	/**
	 * 
	 */
	private DtoDescuentosOdontologicos dtoDescuentoOdontologico;
	/**
	 * 
	 * 
	 */
	private DtoDescuentoOdontologicoAtencion dtoDescuentoOdontologicoAtencion;
///////////////////////PRIMERA PAGINA - ENCABEZADO DETALLE
	/**
	 * 
	 */
	private DtoHistoricoDescuentoOdontologico dtoHistoricoDescuentoOdontologico;
	
	/**
	 * 
	 */
	private DtoHistoricoDescuentoOdontologicoAtencion dtoHistoricoDescuentoOdontologicoAtencion;
	
	
	/**
	 * 
	 */
	private DtoDetalleDescuentoOdontologico dtoDetalleDescuentoOdontologico;
	
	
	/**
	 * 
	 */
	private DtoHistoricoDetalleDescuentoOdontologico dtoDetalleHistoricoDescuentoOdontologico;
	
	
	/**
	 * 
	 */
	/**
	 * 
	 */
	
	
	private HashMap centrosAtencion = new HashMap();
	
	/**
	 * 
	 */
	private ArrayList<DtoDescuentosOdontologicos> listaDescuentos = new ArrayList<DtoDescuentosOdontologicos>();
	
	/**
	 * 
	 */
	private ArrayList<DtoDescuentoOdontologicoAtencion> listaDescuentosAtencion = new ArrayList<DtoDescuentoOdontologicoAtencion>();
	
	 /**
     * 
     * 
     */
	
	private ArrayList<DtoTiposDeUsuario> listaTiposUsuario = new ArrayList<DtoTiposDeUsuario>();
	
	 /**
   * 
   * 
   */
	
	private ArrayList<DtoDetalleDescuentoOdontologico> listaDetalleDescuento = new ArrayList<DtoDetalleDescuentoOdontologico>();
	
	 /**
	   * 
	   * 
	   */
		
    private ArrayList<DtoHistoricoDescuentoOdontologico> listaHistoricoDescuento = new ArrayList<DtoHistoricoDescuentoOdontologico>();
   
   /**
	   * 
	   * 
	   */
		
     private ArrayList<DtoHistoricoDescuentoOdontologicoAtencion> listaHistoricoDescuentoAtencion = new ArrayList<DtoHistoricoDescuentoOdontologicoAtencion>();
		
		

		 /**
		   * 
		   * 
		   */
			
    private ArrayList<DtoHistoricoDetalleDescuentoOdontologico> listaHistoricoDetalleDescuento = new ArrayList<DtoHistoricoDetalleDescuentoOdontologico>();
			
	/**
    * 
    * 
    */
	private String estado;

	/***
	 * 
	 */
	private String patronOrdenar;

	/**
	 * 
	 */
	
	private String esDescendente;

	/**
	 * 
	 */
	private int posArray;

	/**
	 * 
	 */
	
	private int posArrayDetalle;

	/**
	 * 
	 */
	
	
	
	//////////////////////////FIN PRIMERA PAGINA
	
	///////////////////////SEGUNDA PAGINA - ENCABEZADO DETALLE
	
	public void reset() {
		this.tipo="";
		this.dtoDescuentoOdontologico = new DtoDescuentosOdontologicos();
		this.dtoDescuentoOdontologicoAtencion = new  DtoDescuentoOdontologicoAtencion();
		this.listaDescuentos = new ArrayList<DtoDescuentosOdontologicos>();		
		this.listaDescuentosAtencion = new ArrayList<DtoDescuentoOdontologicoAtencion>();
		this.posArray = ConstantesBD.codigoNuncaValido;
		this.posArrayDetalle = ConstantesBD.codigoNuncaValido;
		this.patronOrdenar = "";
		this.centrosAtencion = new HashMap();
		this.esDescendente = "";
		this.dtoDetalleDescuentoOdontologico = new DtoDetalleDescuentoOdontologico();
		this.listaDetalleDescuento = new ArrayList<DtoDetalleDescuentoOdontologico>();
		this.listaTiposUsuario = new ArrayList<DtoTiposDeUsuario>();
		this.dtoHistoricoDescuentoOdontologico = new DtoHistoricoDescuentoOdontologico();
		this.dtoHistoricoDescuentoOdontologicoAtencion = new DtoHistoricoDescuentoOdontologicoAtencion();
		this.dtoDetalleHistoricoDescuentoOdontologico = new DtoHistoricoDetalleDescuentoOdontologico();
		this.listaHistoricoDescuento = new ArrayList<DtoHistoricoDescuentoOdontologico>();
		this.listaHistoricoDescuentoAtencion = new ArrayList<DtoHistoricoDescuentoOdontologicoAtencion>();
		this.listaHistoricoDetalleDescuento = new ArrayList<DtoHistoricoDetalleDescuentoOdontologico>();
		
	}
	
	/**
	 * 
	 */
	public ActionErrors validate(ActionMapping mapping,
			HttpServletRequest request) 
	{
		ActionErrors errores = new ActionErrors();
		
		if(this.getEstado().equals("ingresar")) 
		{
		  
		  
			if(this.getDtoDescuentoOdontologico().getCentroAtencion().getCodigo() == ConstantesBD.codigoNuncaValido)
			{
				errores.add("", new ActionMessage("errors.required", "El centro de atencion"));
			}
			if(UtilidadTexto.isEmpty(this.getTipo()))
			{
				errores.add("", new ActionMessage("errors.required", "El Tipo de descuento"));
			}
		}
		else if(this.getEstado().equals("guardar")|| this.getEstado().equals("guardarModificar") )
		{
		  if(this.getTipo().equals(ConstantesIntegridadDominio.acronimoPorValorPresupuesto))
		   {
				if(UtilidadTexto.isEmpty(this.getDtoDescuentoOdontologico().getFechaFinVigencia()))
				{
					errores.add("", new ActionMessage("errors.required", "La fecha Final Vigencia"));
				}
				else
				{
					if(!UtilidadFecha.esFechaValidaSegunAp(this.getDtoDescuentoOdontologico().getFechaFinVigencia()))
					{
						errores.add("", new ActionMessage("errors.formatoFechaInvalido", "la fecha Fin Vigencia"));
					}
				}
				if(UtilidadTexto.isEmpty(this.getDtoDescuentoOdontologico().getFechaInicioVigencia()))
				{
					errores.add("", new ActionMessage("errors.required", "La fecha Inicio Vigencia"));
				}
				else
				{
					if(!UtilidadFecha.esFechaValidaSegunAp(this.getDtoDescuentoOdontologico().getFechaInicioVigencia())){
	
						errores.add("", new ActionMessage("errors.formatoFechaInvalido", "la fecha Inicio Vigencia"));
	
					}else{
	
						if(UtilidadFecha.esFechaMenorQueOtraReferencia(this.getDtoDescuentoOdontologico().getFechaFinVigencia(), this.getDtoDescuentoOdontologico().getFechaInicioVigencia()))
						{
	
							errores.add("", new ActionMessage("errors.fechaHoraAnteriorIgualActual", " de Fin Vigencia","Fecha Inicio"));
						}
	
					}
				}

				if(estado.equals("guardar"))
				{
					if(DescuentoOdontologico.existeCruceFechas(this.getDtoDescuentoOdontologico(), 0 , this.getDtoDescuentoOdontologico().getCentroAtencion().getCodigo()))
					{
						errores.add("", new ActionMessage("error.odontologia.errorCruceFechasDescuentos",this.getDtoDescuentoOdontologico().getFechaInicioVigencia(),this.getDtoDescuentoOdontologico().getFechaFinVigencia()));
					}
				}else
				{
					if(DescuentoOdontologico.existeCruceFechas(this.getDtoDescuentoOdontologico(), this.getDtoDescuentoOdontologico().getConsecutivo() , this.getDtoDescuentoOdontologico().getCentroAtencion().getCodigo()))
					{
						errores.add("", new ActionMessage("error.odontologia.errorCruceFechasDescuentos",this.getDtoDescuentoOdontologico().getFechaInicioVigencia(),this.getDtoDescuentoOdontologico().getFechaFinVigencia()));
					}
				}
		  }
		  else
		  {
			  if(this.getDtoDescuentoOdontologicoAtencion().getPorcentajeDescuento() <= 0)
				{
					errores.add("", new ActionMessage("errors.MayorQue", "El Porcentaje de descuento",0));
				}
			  
			    if(this.getDtoDescuentoOdontologicoAtencion().getDiasVigencia() <= 0)
				{
					errores.add("", new ActionMessage("errors.MayorQue", "Los dias de vigencia del decuento",0));
				}
				if(this.getDtoDescuentoOdontologicoAtencion().getNivelAutorizacion().getCodigo() == ConstantesBD.codigoNuncaValido)
				{
					errores.add("", new ActionMessage("errors.required", "El nivel de autorizacion"));
				}
				if(this.getDtoDescuentoOdontologicoAtencion().getPorcentajeDescuento() > 100)
				{
					errores.add("", new ActionMessage("errors.debeSerNumeroMenorIgual", "Porcentaje descuento","100"));
				}
			  
			  
		  }
		}
		else if(this.getEstado().equals("guardar_detalle") || this.getEstado().equals("guardarModificarDetalle") )
		{
    	 	if(this.getDtoDetalleDescuentoOdontologico().getValorMinimoPresupuesto() == 0)
			{
				errores.add("", new ActionMessage("errors.MayorQue", "El valor minimo de presupuesto",0));
			}
			if(this.getDtoDetalleDescuentoOdontologico().getValorMinimoPresupuesto()!= 0 &&  this.getDtoDetalleDescuentoOdontologico().getValorMaximoPresupuesto() < this.getDtoDetalleDescuentoOdontologico().getValorMinimoPresupuesto() )
			{
				errores.add("", new ActionMessage("errors.MayorQue", "El valor maximo  de presupuesto","El valor minimo de presupuesto"));
			}

			if(DetalleDescuentoOdontologico.existeRangoPresupuesto(this.getDtoDescuentoOdontologico().getConsecutivo(), this.getDtoDescuentoOdontologico().getCentroAtencion().getCodigo(), this.getDtoDetalleDescuentoOdontologico().getValorMinimoPresupuesto(), this.getDtoDetalleDescuentoOdontologico().getValorMaximoPresupuesto() ,this.getDtoDetalleDescuentoOdontologico().getCodigo() )){
				logger.info("Valores a Vildar "+this.getDtoDescuentoOdontologico().getConsecutivo()+ ""+ this.getDtoDescuentoOdontologico().getCentroAtencion().getCodigo()+ ""+ this.getDtoDetalleDescuentoOdontologico().getValorMinimoPresupuesto()+ ""+this.getDtoDetalleDescuentoOdontologico().getValorMaximoPresupuesto());
				errores.add("", new ActionMessage("error.rangoGenerico", "De valores de Presupuesto","El valor minimo de presupuesto","y El valor minimo de presupuesto","Los valores Existentes En BD"));
			}
			if(this.getDtoDetalleDescuentoOdontologico().getValorMaximoPresupuesto() == 0)
			{
				errores.add("", new ActionMessage("errors.MayorQue", "El valor maximo de presupuesto",0));
			}
			if(this.getDtoDetalleDescuentoOdontologico().getPorcentajeDescuento() == 0)
			{
				errores.add("", new ActionMessage("errors.MayorQue", "El Porcentaje de descuento",0));
			}
			if(this.getDtoDetalleDescuentoOdontologico().getDiasVigencia() == 0)
			{
				errores.add("", new ActionMessage("errors.MayorQue", "Los dias de vigencia del decuento",0));
			}
			if(Double.parseDouble(this.getDtoDetalleDescuentoOdontologico().getTipoUsuarioAutoriza().getCodigo()) == ConstantesBD.codigoNuncaValido)
			{
				errores.add("", new ActionMessage("errors.required", "El tipo de usuario que autoriza"));
			}
			if(this.getDtoDetalleDescuentoOdontologico().getPorcentajeDescuento() > 100)
			{
				errores.add("", new ActionMessage("errors.debeSerNumeroMenorIgual", "Porcentaje descuento","100"));
			}
    	  
		}
	   else if(this.getEstado().equals("eliminar"))
	   {
			
		   if(this.getTipo().equals(ConstantesIntegridadDominio.acronimoPorValorPresupuesto))
		   {
			 
		   DtoDescuentosOdontologicos dtoWhere= new DtoDescuentosOdontologicos();
			dtoWhere.setConsecutivo(this.getListaDescuentos().get(this.getPosArray()).getConsecutivo());
			
	  		DtoDetalleDescuentoOdontologico dtoDetalle = new DtoDetalleDescuentoOdontologico();
			dtoDetalle.setConsecutivoDescuento(dtoWhere.getConsecutivo());
			
			
			if(DetalleDescuentoOdontologico.cargar(dtoDetalle).size() > 0)
			{
			    
			     errores.add("", new ActionMessage("errors.existeDetalle", "Descuento Odontologico"));
			}
			
		   }
		}
        if(!errores.isEmpty())
		{
        	
        	
        	if(this.getEstado().equals("ingresar"))
				this.setEstado("mostrarErroresIngresar");
        	
        	if(this.getEstado().equals("eliminar"))
				this.setEstado("mostrarErroresEliminar");
			
        	
			if(this.getEstado().equals("guardar"))
				this.setEstado("mostrarErroresGuardar");
			
			if(this.getEstado().equals("guardarModificar"))
				this.setEstado("mostrarErroresGuardarModificar");
			
			if(this.getEstado().equals("guardar_detalle"))
				this.setEstado("mostrarErroresGuardarDetalle");
			
			if(this.getEstado().equals("guardarModificarDetalle"))
				this.setEstado("mostrarErroresGuardarModificarDetalle");
			
		}
		
        return errores;
	}


	/**
	 * @return the dtoDescuentoOdontologico
	 */
	public DtoDescuentosOdontologicos getDtoDescuentoOdontologico() {
		return dtoDescuentoOdontologico;
	}


	/**
	 * @param dtoDescuentoOdontologico the dtoDescuentoOdontologico to set
	 */
	public void setDtoDescuentoOdontologico(
			DtoDescuentosOdontologicos dtoDescuentoOdontologico) {
		this.dtoDescuentoOdontologico = dtoDescuentoOdontologico;
	}


	/**
	 * @return the listaDescuentos
	 */
	public ArrayList<DtoDescuentosOdontologicos> getListaDescuentos() {
		return listaDescuentos;
	}


	/**
	 * @param listaDescuentos the listaDescuentos to set
	 */
	public void setListaDescuentos(
			ArrayList<DtoDescuentosOdontologicos> listaDescuentos) {
		this.listaDescuentos = listaDescuentos;
	}


	/**
	 * @return the estado
	 */
	public String getEstado() {
		return estado;
	}


	/**
	 * @param estado the estado to set
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	}


	/**
	 * @return the patronOrdenar
	 */
	public String getPatronOrdenar() {
		return patronOrdenar;
	}


	/**
	 * @param patronOrdenar the patronOrdenar to set
	 */
	public void setPatronOrdenar(String patronOrdenar) {
		this.patronOrdenar = patronOrdenar;
	}


	/**
	 * @return the posArray
	 */
	public int getPosArray() {
		return posArray;
	}


	/**
	 * @param posArray the posArray to set
	 */
	public void setPosArray(int posArray) {
		this.posArray = posArray;
	}


	/**
	 * @return the centrosAtencion
	 */
	public HashMap getCentrosAtencion() {
		return centrosAtencion;
	}


	/**
	 * @param centrosAtencion the centrosAtencion to set
	 */
	public void setCentrosAtencion(HashMap centrosAtencion) {
		this.centrosAtencion = centrosAtencion;
	}


	/**
	 * @return the esDescendente
	 */
	public String getEsDescendente() {
		return esDescendente;
	}


	/**
	 * @param esDescendente the esDescendente to set
	 */
	public void setEsDescendente(String esDescendente) {
		this.esDescendente = esDescendente;
	}


	/**
	 * @return the dtoDetalleDescuentoOdontologico
	 */
	public DtoDetalleDescuentoOdontologico getDtoDetalleDescuentoOdontologico() {
		return dtoDetalleDescuentoOdontologico;
	}


	/**
	 * @param dtoDetalleDescuentoOdontologico the dtoDetalleDescuentoOdontologico to set
	 */
	public void setDtoDetalleDescuentoOdontologico(
			DtoDetalleDescuentoOdontologico dtoDetalleDescuentoOdontologico) {
		this.dtoDetalleDescuentoOdontologico = dtoDetalleDescuentoOdontologico;
	}


	/**
	 * @return the listaDetalleDescuento
	 */
	public ArrayList<DtoDetalleDescuentoOdontologico> getListaDetalleDescuento() {
		return listaDetalleDescuento;
	}


	/**
	 * @param listaDetalleDescuento the listaDetalleDescuento to set
	 */
	public void setListaDetalleDescuento(
			ArrayList<DtoDetalleDescuentoOdontologico> listaDetalleDescuento) {
		this.listaDetalleDescuento = listaDetalleDescuento;
	}


	/**
	 * @return the listaTiposUsuario
	 */
	public ArrayList<DtoTiposDeUsuario> getListaTiposUsuario() {
		return listaTiposUsuario;
	}


	/**
	 * @param listaTiposUsuario the listaTiposUsuario to set
	 */
	public void setListaTiposUsuario(ArrayList<DtoTiposDeUsuario> listaTiposUsuario) {
		this.listaTiposUsuario = listaTiposUsuario;
	}


	/**
	 * @return the posArrayDetalle
	 */
	public int getPosArrayDetalle() {
		return posArrayDetalle;
	}


	/**
	 * @param posArrayDetalle the posArrayDetalle to set
	 */
	public void setPosArrayDetalle(int posArrayDetalle) {
		this.posArrayDetalle = posArrayDetalle;
	}


	/**
	 * @return the dtoHistoricoDescuentoOdontologico
	 */
	public DtoHistoricoDescuentoOdontologico getDtoHistoricoDescuentoOdontologico() {
		return dtoHistoricoDescuentoOdontologico;
	}


	/**
	 * @param dtoHistoricoDescuentoOdontologico the dtoHistoricoDescuentoOdontologico to set
	 */
	public void setDtoHistoricoDescuentoOdontologico(
			DtoHistoricoDescuentoOdontologico dtoHistoricoDescuentoOdontologico) {
		this.dtoHistoricoDescuentoOdontologico = dtoHistoricoDescuentoOdontologico;
	}


	/**
	 * @return the dtoDetalleHistoricoDescuentoOdontologico
	 */
	public DtoHistoricoDetalleDescuentoOdontologico getDtoDetalleHistoricoDescuentoOdontologico() {
		return dtoDetalleHistoricoDescuentoOdontologico;
	}


	/**
	 * @param dtoDetalleHistoricoDescuentoOdontologico the dtoDetalleHistoricoDescuentoOdontologico to set
	 */
	public void setDtoDetalleHistoricoDescuentoOdontologico(
			DtoHistoricoDetalleDescuentoOdontologico dtoDetalleHistoricoDescuentoOdontologico) {
		this.dtoDetalleHistoricoDescuentoOdontologico = dtoDetalleHistoricoDescuentoOdontologico;
	}


	/**
	 * @return the listaHistoricoDescuento
	 */
	public ArrayList<DtoHistoricoDescuentoOdontologico> getListaHistoricoDescuento() {
		return listaHistoricoDescuento;
	}


	/**
	 * @param listaHistoricoDescuento the listaHistoricoDescuento to set
	 */
	public void setListaHistoricoDescuento(
			ArrayList<DtoHistoricoDescuentoOdontologico> listaHistoricoDescuento) {
		this.listaHistoricoDescuento = listaHistoricoDescuento;
	}


	/**
	 * @return the listaHistoricoDetalleDescuento
	 */
	public ArrayList<DtoHistoricoDetalleDescuentoOdontologico> getListaHistoricoDetalleDescuento() {
		return listaHistoricoDetalleDescuento;
	}


	/**
	 * @param listaHistoricoDetalleDescuento the listaHistoricoDetalleDescuento to set
	 */
	public void setListaHistoricoDetalleDescuento(
			ArrayList<DtoHistoricoDetalleDescuentoOdontologico> listaHistoricoDetalleDescuento) {
		this.listaHistoricoDetalleDescuento = listaHistoricoDetalleDescuento;
	}


	/**
	 * @return the esConsulta
	 */
	public boolean isEsConsulta() {
		return esConsulta;
	}


	/**
	 * @param esConsulta the esConsulta to set
	 */
	public void setEsConsulta(boolean esConsulta) {
		this.esConsulta = esConsulta;
	}

	/**
	 * @return the tipo
	 */
	public String getTipo()
	{
		return tipo;
	}

	/**
	 * @param tipo the tipo to set
	 */
	public void setTipo(String tipo)
	{
		this.tipo = tipo;
	}

	/**
	 * @return the dtoDescuentoOdontologicoAtencion
	 */
	public DtoDescuentoOdontologicoAtencion getDtoDescuentoOdontologicoAtencion() {
		return dtoDescuentoOdontologicoAtencion;
	}

	/**
	 * @param dtoDescuentoOdontologicoAtencion the dtoDescuentoOdontologicoAtencion to set
	 */
	public void setDtoDescuentoOdontologicoAtencion(
			DtoDescuentoOdontologicoAtencion dtoDescuentoOdontologicoAtencion) {
		this.dtoDescuentoOdontologicoAtencion = dtoDescuentoOdontologicoAtencion;
	}

	/**
	 * @return the dtoHistoricoDescuentoOdontologicoAtencion
	 */
	public DtoHistoricoDescuentoOdontologicoAtencion getDtoHistoricoDescuentoOdontologicoAtencion() {
		return dtoHistoricoDescuentoOdontologicoAtencion;
	}

	/**
	 * @param dtoHistoricoDescuentoOdontologicoAtencion the dtoHistoricoDescuentoOdontologicoAtencion to set
	 */
	public void setDtoHistoricoDescuentoOdontologicoAtencion(
			DtoHistoricoDescuentoOdontologicoAtencion dtoHistoricoDescuentoOdontologicoAtencion) {
		this.dtoHistoricoDescuentoOdontologicoAtencion = dtoHistoricoDescuentoOdontologicoAtencion;
	}

	/**
	 * @return the listaDescuentosAtencion
	 */
	public ArrayList<DtoDescuentoOdontologicoAtencion> getListaDescuentosAtencion() {
		return listaDescuentosAtencion;
	}

	/**
	 * @param listaDescuentosAtencion the listaDescuentosAtencion to set
	 */
	public void setListaDescuentosAtencion(
			ArrayList<DtoDescuentoOdontologicoAtencion> listaDescuentosAtencion) {
		this.listaDescuentosAtencion = listaDescuentosAtencion;
	}

	/**
	 * @return the listaHistoricoDescuentoAtencion
	 */
	public ArrayList<DtoHistoricoDescuentoOdontologicoAtencion> getListaHistoricoDescuentoAtencion() {
		return listaHistoricoDescuentoAtencion;
	}

	/**
	 * @param listaHistoricoDescuentoAtencion the listaHistoricoDescuentoAtencion to set
	 */
	public void setListaHistoricoDescuentoAtencion(
			ArrayList<DtoHistoricoDescuentoOdontologicoAtencion> listaHistoricoDescuentoAtencion) {
		this.listaHistoricoDescuentoAtencion = listaHistoricoDescuentoAtencion;
	}


	
	
	
	
	
	
}
