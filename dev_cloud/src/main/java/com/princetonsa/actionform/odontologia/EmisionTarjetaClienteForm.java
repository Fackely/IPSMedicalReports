package com.princetonsa.actionform.odontologia;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;
import com.princetonsa.dto.odontologia.DtoDetalleEmisionesTarjetaCliente;
import com.princetonsa.dto.odontologia.DtoEmisionTarjetaCliente;
import com.princetonsa.dto.odontologia.DtoHistoricoDetalleEmisionTarjetaCliente;
import com.princetonsa.dto.odontologia.DtoHistoricoEmisionTarjetaCliente;
import com.princetonsa.dto.odontologia.DtoSuperficieDental;
import com.princetonsa.dto.odontologia.DtoTarjetaCliente;
import com.princetonsa.mundo.odontologia.DetalleEmisionTarjetaCliente;
import com.princetonsa.mundo.odontologia.EmisionTarjetaCliente;
import com.princetonsa.mundo.odontologia.VentasTarjetasCliente;
import util.ConstantesBD;
import util.InfoDatosStr;
import util.UtilidadTexto;

public class EmisionTarjetaClienteForm extends ValidatorForm {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @return the offset
	 */
	public int getOffset() {
		return offset;
	}

	/**
	 * @param offset the offset to set
	 */
	public void setOffset(int offset) {
		this.offset = offset;
	}

	private Logger logger = Logger.getLogger(EmisionTarjetaClienteForm.class);
	
	/**
	    * 
	    * 
	    */
		private String estado;
		
		/**
	    * 
	    * 
	    */
		private String estadoAnterior;

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
		private int posTarjeta;

		/**
		 * 
		 */
		
		private int posArrayDetalle;

		/**
		 * 
		 */
		
		private int posArrayConvenio;

		/**
		 * 
		 */
		
		private ArrayList<DtoTarjetaCliente> listTarjetasCliente = new ArrayList<DtoTarjetaCliente>();
		/**
		 * 
		 * 
		 * 
		 */
		private DtoEmisionTarjetaCliente dtoEmision = new DtoEmisionTarjetaCliente();
		/**
		 * 
		 * 
		 */
		private DtoHistoricoEmisionTarjetaCliente dtoHistoricoEmision = new DtoHistoricoEmisionTarjetaCliente();
		/**
		 * 
		 * 
		 */
		
		private DtoHistoricoDetalleEmisionTarjetaCliente dtoHistoricoDetalleEmision = new DtoHistoricoDetalleEmisionTarjetaCliente();
		/**
		 * 
		 * 
		 */
		private DtoDetalleEmisionesTarjetaCliente dtoDetalleEmision = new DtoDetalleEmisionesTarjetaCliente();
		/**
		 * 
		 * 
		 */
		private boolean esConsulta;
		/**
		 * 
		 */
		private ArrayList<DtoEmisionTarjetaCliente> listaEmisiones = new ArrayList<DtoEmisionTarjetaCliente>();
		/**
		 * 
		 */
		private ArrayList<DtoDetalleEmisionesTarjetaCliente> listaDetalleEmisiones = new ArrayList<DtoDetalleEmisionesTarjetaCliente>();
		
		/**
		 * 
		 */
		private ArrayList<InfoDatosStr> arrayUsuarios= new ArrayList<InfoDatosStr>();
		
       /**
        * 
        * 
        */		
		private HashMap centrosAtencion = new HashMap();
		
		/**
		 * */
		private String linkSiguiente;
		private String linkSiguienteDetalle;
		
		/**
		 * */
	    private int offset;
	    
	    /**
		 * */
	    private int currentPageNumber;
	    
	    
	    /**
	     * 
	     */
	    private String esModificableTarjeta;
		
		/**
		 * 
		 */
		//////////////////////////FIN PRIMERA PAGINA
		
		///////////////////////SEGUNDA PAGINA - ENCABEZADO DETALLE
		
	    
	    
	    /**
	     *EXISTE DETALLE 
	     */
	    private String existeDetalle;
	    
		public void reset() {
			
			this.posArray = ConstantesBD.codigoNuncaValido;
			this.posArrayDetalle = ConstantesBD.codigoNuncaValido;
			this.patronOrdenar = "";
			this.listTarjetasCliente = new  ArrayList<DtoTarjetaCliente>();
			this.dtoEmision = new DtoEmisionTarjetaCliente();
			this.listaEmisiones = new ArrayList<DtoEmisionTarjetaCliente>();
			this.listaDetalleEmisiones = new ArrayList<DtoDetalleEmisionesTarjetaCliente>();
			
			this.centrosAtencion = new HashMap();
			this.dtoDetalleEmision = new DtoDetalleEmisionesTarjetaCliente();
			this.dtoHistoricoEmision = new DtoHistoricoEmisionTarjetaCliente();
			this.dtoHistoricoDetalleEmision = new DtoHistoricoDetalleEmisionTarjetaCliente();
			this.posTarjeta = ConstantesBD.codigoNuncaValido;
			this.arrayUsuarios = new ArrayList<InfoDatosStr>();
			this.posArrayConvenio = ConstantesBD.codigoNuncaValido;
			this.estadoAnterior="";
			this.linkSiguiente = "";
			this.linkSiguienteDetalle = "";
			this.offset = 0;
			this.currentPageNumber = 0;
			this.esModificableTarjeta=ConstantesBD.acronimoSi;
			this.setExisteDetalle(ConstantesBD.acronimoNo);
		}
		
		public ActionErrors validate(ActionMapping mapping,
				HttpServletRequest request) 
	      {
	      
		  ActionErrors errores = new ActionErrors();
		  
		  

   		DtoDetalleEmisionesTarjetaCliente detEmision= new DtoDetalleEmisionesTarjetaCliente();
   		detEmision.setCodigoEmisiontargeta(this.getDtoEmision().getCodigo());
   		detEmision.setInstitucion(this.getDtoEmision().getInstitucion());
   		
    		
    		if(this.getEstado().equals("modificar"))
    		{
    		    DtoEmisionTarjetaCliente dtoWhere= new DtoEmisionTarjetaCliente();
 	      		dtoWhere.setCodigo(this.getListaEmisiones().get(this.getPosArray()).getCodigo());
 	      		DtoDetalleEmisionesTarjetaCliente dtoDetalle = new DtoDetalleEmisionesTarjetaCliente();
 	    		dtoDetalle.setCodigoEmisiontargeta(dtoWhere.getCodigo());
 	    		
 	    		
 	    		
    			
 	    		if(DetalleEmisionTarjetaCliente.cargar(dtoDetalle).size() > 0)
    			{
    				this.setEstado("modificar2");
    			}
    		} 
    		else if (this.getEstado().equals("guardarModificar2"))
    		{
    			
    			BigDecimal tmp= DetalleEmisionTarjetaCliente.cargarSerialMayor(detEmision);
    			logger.info("	 SERIAL FINAL "+this.getDtoEmision().getSerialFinal()+"     TMP--->"+tmp );
    			
    			if(this.getDtoEmision().getSerialFinal().doubleValue()<tmp.doubleValue())
    			{
    				errores.add("", new ActionMessage("errors.MayorQue", "El serial Final ","El serial Maximo Del Detalle"));
    			}
    				
    			logger.info("************************************************************************************************************************\n\n\n\n\n\n\n");
    			logger.info(" RANGO DE SERIALES GUARDAR MODIFICAR 2 ");
    			
    			
    			/*if(EmisionTargetaCliente.existeRangoSeriales(this.getDtoEmision().getCodigo(),this.getDtoEmision().getTipoTarjeta().getDescripcion(),  this.getDtoEmision().getSerialInicial().doubleValue(),  this.getDtoEmision().getSerialFinal().doubleValue()))
    			{
  	    		  logger.info("Validando Cruce "+this.getDtoEmision().getCodigo()+ this.getDtoEmision().getTipoTarjeta().getDescripcion() + this.getDtoEmision().getSerialInicial()+ this.getDtoEmision().getSerialFinal() );
  	    		  errores.add("", new ActionMessage("error.odontologia.errorSerialesEncabezado",  this.getDtoEmision().getSerialInicial() ,  this.getDtoEmision().getSerialFinal()));
  	    	     }*/
    					
    			
    			if(VentasTarjetasCliente.existeRangoEnVenta(this.getDtoEmision().getSerialInicial().doubleValue(), this.getDtoEmision().getSerialFinal().doubleValue(), this.getDtoEmision().getInstitucion(), ConstantesBD.acronimoNo))
  	    	     {    				 
   				  errores.add("", new ActionMessage("error.odontologia.errorSerialesEnVenta", this.getListaEmisiones().get(this.getPosArray()).getSerialInicial(),this.getListaEmisiones().get(this.getPosArray()).getSerialFinal()));
   			     }
    		}
    		
    		else if(this.getEstado().equals("guardar")|| this.getEstado().equals("guardarModificar") ){
	    	  
    			logger.info("	 SERIAL FINAL "+this.getDtoEmision().getSerialFinal() );
    			
    			if(Double.parseDouble(this.getDtoEmision().getTipoTarjeta().getCodigo()) < 1)
				{								
					errores.add("", new ActionMessage("errors.required", "El tipo de tarjeta"));
				}
	    	  
	    	/* if(EmisionTargetaCliente.existeRangoSeriales(this.getDtoEmision().getCodigo(),this.getDtoEmision().getTipoTarjeta().getDescripcion(),  this.getDtoEmision().getSerialInicial().doubleValue(),  this.getDtoEmision().getSerialFinal().doubleValue())){
	    		  logger.info("Validando Cruce "+this.getDtoEmision().getCodigo()+ this.getDtoEmision().getTipoTarjeta().getDescripcion() + this.getDtoEmision().getSerialInicial()+ this.getDtoEmision().getSerialFinal() );
	    		  errores.add("", new ActionMessage("error.odontologia.errorSerialesEncabezado",  this.getDtoEmision().getSerialInicial() ,  this.getDtoEmision().getSerialFinal()));
	    	  }*/
	    	  
		    	if(this.getDtoEmision().getSerialInicial().doubleValue() <= 0 )
		    	{
		    		errores.add("", new ActionMessage("errors.notEspecific", "El tipo de tarjeta seleccionado, No tiene 'Consecutivo Serial' parametrizado. NO se permite realizar Emisión de tarjetas. Por favor verifique"));
		    	}else
		    	{	
			    	  if(this.getDtoEmision().getSerialInicial().doubleValue() ==0 )
						{
							errores.add("", new ActionMessage("errors.required", "El Serial Inicial"));
						}
		    		  
			    	  if(this.getDtoEmision().getSerialInicial().doubleValue() >= this.getDtoEmision().getSerialFinal().doubleValue())
						{
			    		  /*
			    		   * Según Tarea 144816
			    		   */
			    		  errores.add("", new ActionMessage("errors.MayorQue", "Serial Final","Serial Inicial"));
						}
			    	  
			    	  if(this.getDtoEmision().getSerialFinal().doubleValue() < 1)
						{
										
							errores.add("", new ActionMessage("errors.required", "El Serial Final"));
						}
			    	  
			    	
			    	  if(VentasTarjetasCliente.existeRangoEnVenta(this.getDtoEmision().getSerialInicial().doubleValue(), this.getDtoEmision().getSerialFinal().doubleValue(), this.getDtoEmision().getInstitucion(), ConstantesBD.acronimoNo))
			    	    { 				 
		 				 errores.add("", new ActionMessage("error.odontologia.errorSerialesEnVenta", this.getDtoEmision().getSerialInicial(),this.getDtoEmision().getSerialFinal()));
		 			    }
		    	}
	    	  
		  }
		  
		  else if(this.getEstado().equals("guardarDetalle")|| this.getEstado().equals("guardarModificarDetalle") ){
	    	  
	    	 

	    	 
			  
			  if(this.getDtoDetalleEmision().getCentroAtencion().getCodigo() < 1)
				{
								
					errores.add("", new ActionMessage("errors.required", "El centro de atencion"));
				}
			  
			 
			
			  if(UtilidadTexto.isEmpty(this.getDtoDetalleEmision().getUsuarioResponsable()) || this.getDtoDetalleEmision().getUsuarioResponsable().equals("-1")){
				  
				 
				  errores.add("", new ActionMessage("errors.required", "El usuario responsable"));
			  }
			 
			  logger.info("************************************************************************************************************************\n\n\n\n\n\n\n");
			  logger.info(" RANGO DE SERIALES GUARDAR MODIFICAR  \n\n\n\n");
			  if(DetalleEmisionTarjetaCliente.existeRangoSeriales(this.getDtoDetalleEmision().getCodigo(), this.getDtoDetalleEmision().getInstitucion(),  this.getDtoDetalleEmision().getSerialInicial().doubleValue(),  this.getDtoDetalleEmision().getSerialFinal().doubleValue()))
			  {
				  errores.add("", new ActionMessage("error.odontologia.errorCruceSerialesDetalle", this.getDtoDetalleEmision().getSerialInicial(),this.getDtoDetalleEmision().getSerialFinal()));
	    	  }
	    	  
	    	  if(this.getDtoDetalleEmision().getSerialInicial().doubleValue() < 1)
	    	  {
				  errores.add("", new ActionMessage("errors.required", "El Serial Inicial"));
	    	  }
	    	  
	    	  

			  if((this.getDtoDetalleEmision().getSerialInicial().doubleValue() <   this.getListaEmisiones().get(this.getPosArray()).getSerialInicial().doubleValue()) || ( this.getDtoDetalleEmision().getSerialFinal().doubleValue() >  this.getListaEmisiones().get(this.getPosArray()).getSerialFinal().doubleValue()) )
			  {
				  logger.info(this.getEstado() + "Inicial .."+this.getDtoEmision().getSerialInicial());
				  errores.add("", new ActionMessage("error.odontologia.errorSerialesDetalleEmision", this.getDtoDetalleEmision().getSerialInicial(), this.getDtoDetalleEmision().getSerialFinal() ));
			  }
	    	  
			  if(this.getDtoDetalleEmision().getSerialInicial().doubleValue() >= this.getDtoDetalleEmision().getSerialFinal().doubleValue() )
			  {
				  errores.add("", new ActionMessage("errors.MayorQue", "El serial Final","El serial Inicial"));
			  }
	    	  
	    	  if(this.getDtoDetalleEmision().getSerialFinal().doubleValue() < 1)
				{
					errores.add("", new ActionMessage("errors.required", "El Serial Final"));
				}
		  }
 
          else if(this.getEstado().equals("eliminar")){
        	  
        	 DtoEmisionTarjetaCliente dtoWhere= new DtoEmisionTarjetaCliente();
      		dtoWhere.setCodigo(this.getListaEmisiones().get(this.getPosArray()).getCodigo());
      		
        	DtoDetalleEmisionesTarjetaCliente dtoDetalle = new DtoDetalleEmisionesTarjetaCliente();
      		dtoDetalle.setCodigoEmisiontargeta(dtoWhere.getCodigo());
      		
      		
      		if(DetalleEmisionTarjetaCliente.cargar(dtoDetalle).size() > 0){
      		    
        		
        		     this.setEstado("empezar");
      			errores.add("", new ActionMessage("errors.existeDetalle", "Emision Tarjeta Cliente"));
      		}
          }
		  
		  if(!errores.isEmpty())
			{
				if(this.getEstado().equals("guardar"))
					this.setEstado("mostrarErroresGuardar");
				
				if(this.getEstado().equals("guardarModificar"))
					this.setEstado("mostrarErroresGuardarModificar");
				
				if(this.getEstado().equals("guardarModificar2"))
					this.setEstado("mostrarErroresGuardarModificar2");
				
				if(this.getEstado().equals("guardarDetalle"))
					this.setEstado("mostrarErroresGuardarDetalle");
				
				if(this.getEstado().equals("guardarModificarDetalle"))
					this.setEstado("mostrarErroresGuardarModificarDetalle");
				
			}
		  
		  return errores;
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
		 * @return the listTarjetasCliente
		 */
		public ArrayList<DtoTarjetaCliente> getListTarjetasCliente() {
			return listTarjetasCliente;
		}

		/**
		 * @param listTarjetasCliente the listTarjetasCliente to set
		 */
		public void setListTarjetasCliente(
				ArrayList<DtoTarjetaCliente> listTarjetasCliente) {
			this.listTarjetasCliente = listTarjetasCliente;
		}

		

		/**
		 * @return the listaEmisiones
		 */
		public ArrayList<DtoEmisionTarjetaCliente> getListaEmisiones() {
			return listaEmisiones;
		}

		/**
		 * @param listaEmisiones the listaEmisiones to set
		 */
		public void setListaEmisiones(ArrayList<DtoEmisionTarjetaCliente> listaEmisiones) {
			this.listaEmisiones = listaEmisiones;
		}

		/**
		 * @return the dtoEmision
		 */
		public DtoEmisionTarjetaCliente getDtoEmision() {
			return dtoEmision;
		}

		/**
		 * @param dtoEmision the dtoEmision to set
		 */
		public void setDtoEmision(DtoEmisionTarjetaCliente dtoEmision) {
			this.dtoEmision = dtoEmision;
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
		 * @return the listaDetalleEmisiones
		 */
		public ArrayList<DtoDetalleEmisionesTarjetaCliente> getListaDetalleEmisiones() {
			return listaDetalleEmisiones;
		}

		/**
		 * @param listaDetalleEmisiones the listaDetalleEmisiones to set
		 */
		public void setListaDetalleEmisiones(
				ArrayList<DtoDetalleEmisionesTarjetaCliente> listaDetalleEmisiones) {
			this.listaDetalleEmisiones = listaDetalleEmisiones;
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
		 * @return the dtoDetalleEmision
		 */
		public DtoDetalleEmisionesTarjetaCliente getDtoDetalleEmision() {
			return dtoDetalleEmision;
		}

		/**
		 * @param dtoDetalleEmision the dtoDetalleEmision to set
		 */
		public void setDtoDetalleEmision(
				DtoDetalleEmisionesTarjetaCliente dtoDetalleEmision) {
			this.dtoDetalleEmision = dtoDetalleEmision;
		}

		/**
		 * @return the dtoHistoricoEmision
		 */
		public DtoHistoricoEmisionTarjetaCliente getDtoHistoricoEmision() {
			return dtoHistoricoEmision;
		}

		/**
		 * @param dtoHistoricoEmision the dtoHistoricoEmision to set
		 */
		public void setDtoHistoricoEmision(
				DtoHistoricoEmisionTarjetaCliente dtoHistoricoEmision) {
			this.dtoHistoricoEmision = dtoHistoricoEmision;
		}

		/**
		 * @return the dtoHistoricoDetalleEmision
		 */
		public DtoHistoricoDetalleEmisionTarjetaCliente getDtoHistoricoDetalleEmision() {
			return dtoHistoricoDetalleEmision;
		}

		/**
		 * @param dtoHistoricoDetalleEmision the dtoHistoricoDetalleEmision to set
		 */
		public void setDtoHistoricoDetalleEmision(
				DtoHistoricoDetalleEmisionTarjetaCliente dtoHistoricoDetalleEmision) {
			this.dtoHistoricoDetalleEmision = dtoHistoricoDetalleEmision;
		}

		/**
		 * @return the posTarjeta
		 */
		public int getPosTarjeta() {
			return posTarjeta;
		}

		/**
		 * @param posTarjeta the posTarjeta to set
		 */
		public void setPosTarjeta(int posTarjeta) {
			this.posTarjeta = posTarjeta;
		}

		/**
		 * @return the arrayUsuarios
		 */
		public ArrayList<InfoDatosStr> getArrayUsuarios() {
			return arrayUsuarios;
		}

		/**
		 * @param arrayUsuarios the arrayUsuarios to set
		 */
		public void setArrayUsuarios(ArrayList<InfoDatosStr> arrayUsuarios) {
			this.arrayUsuarios = arrayUsuarios;
		}

		/**
		 * @return the posArrayConvenio
		 */
		public int getPosArrayConvenio() {
			return posArrayConvenio;
		}

		/**
		 * @param posArrayConvenio the posArrayConvenio to set
		 */
		public void setPosArrayConvenio(int posArrayConvenio) {
			this.posArrayConvenio = posArrayConvenio;
		}

		/**
		 * @return the estadoAnterior
		 */
		public String getEstadoAnterior() {
			return estadoAnterior;
		}

		/**
		 * @param estadoAnterior the estadoAnterior to set
		 */
		public void setEstadoAnterior(String estadoAnterior) {
			this.estadoAnterior = estadoAnterior;
		}

		/**
		 * @return the linkSiguiente
		 */
		public String getLinkSiguiente() {
			return linkSiguiente;
		}

		/**
		 * @param linkSiguiente the linkSiguiente to set
		 */
		public void setLinkSiguiente(String linkSiguiente) {
			this.linkSiguiente = linkSiguiente;
		}

		public int getCurrentPageNumber() {
			return currentPageNumber;
		}

		public void setCurrentPageNumber(int currentPageNumber) {
			this.currentPageNumber = currentPageNumber;
		}

		/**
		 * @return the linkSiguienteDetalle
		 */
		public String getLinkSiguienteDetalle() {
			return linkSiguienteDetalle;
		}

		/**
		 * @param linkSiguienteDetalle the linkSiguienteDetalle to set
		 */
		public void setLinkSiguienteDetalle(String linkSiguienteDetalle) {
			this.linkSiguienteDetalle = linkSiguienteDetalle;
		}

		public void setEsModificableTarjeta(String esModificableTarjeta) {
			this.esModificableTarjeta = esModificableTarjeta;
		}

		public String getEsModificableTarjeta() {
			return esModificableTarjeta;
		}

		public void setExisteDetalle(String existeDetalle) {
			this.existeDetalle = existeDetalle;
		}

		public String getExisteDetalle() {
			return existeDetalle;
		}
	
}
