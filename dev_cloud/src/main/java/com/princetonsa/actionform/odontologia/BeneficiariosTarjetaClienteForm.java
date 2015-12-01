package com.princetonsa.actionform.odontologia;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.InfoDatosDouble;
import util.InfoDatosStr;
import util.UtilidadTexto;

import com.princetonsa.action.odontologia.BeneficiariosTarjetaClienteAction;
import com.princetonsa.dto.odontologia.DtoAliadoOdontologico;
import com.princetonsa.dto.odontologia.DtoBeneficiarioCliente;
import com.princetonsa.dto.odontologia.DtoParentesco;
import com.princetonsa.dto.odontologia.DtoTarjetaCliente;
import com.princetonsa.dto.odontologia.DtoVentaTarjetasCliente;
import com.princetonsa.enums.odontologia.TipoBusquedaAliado;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.odontologia.BeneficiariosTarjetaCliente;
import com.princetonsa.mundo.odontologia.VentasTarjetasCliente;


/**
 * 
 * @author axioma
 *
 */
public class BeneficiariosTarjetaClienteForm extends ValidatorForm {

	private Logger logger = Logger.getLogger(BeneficiariosTarjetaClienteAction.class);
	 
	private UsuarioBasico usuario;

	
	
	private String mensaje = "   Beneficiario Guardado con Exito !!!";
	 
	private String jsAliado;
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
	private String estado;
	/**
	 * 
	 */
	private String estadoAnterior;
	/**
	 * 
	 */
	private boolean esConsulta;
	/**
	 * @return the estado
	 */
	private DtoBeneficiarioCliente dtoBeneficiario = new DtoBeneficiarioCliente();
	/**
	 * 
	 */
	private ArrayList<DtoBeneficiarioCliente> listaBeneficiarios = new ArrayList<DtoBeneficiarioCliente>();
	/**
	 * 
	 */
	private ArrayList<DtoVentaTarjetasCliente> listaVentas = new ArrayList<DtoVentaTarjetasCliente>();
	
 	/**
	 * @return the estado
	 */
	private DtoBeneficiarioCliente dtoPrincipal = new DtoBeneficiarioCliente();
	/**
	 * 
	 */
	private DtoBeneficiarioCliente dtoBusquedaBeneficiario = new DtoBeneficiarioCliente();
	/**
	 * 
	 */
	private DtoBeneficiarioCliente dtoBusquedaPrincipal = new DtoBeneficiarioCliente();
	/**
	 * 
	 * 
	 */
	private ArrayList<DtoTarjetaCliente> arrayTiposTarjetas = new ArrayList<DtoTarjetaCliente>();
	/**
	 * 
	 * 
	 * 
	 */
	private ArrayList<DtoAliadoOdontologico> arrayAliados = new ArrayList<DtoAliadoOdontologico>();
	/**
	 * 
	 * 
	 * 
	 */
	private ArrayList<DtoBeneficiarioCliente> arrayResultadoBusqueda = new ArrayList<DtoBeneficiarioCliente>();
	
	/**
	 * 
	 * 
	 * 
	 */
	private ArrayList<DtoParentesco> arrayparentezco = new ArrayList<DtoParentesco>();
	/**
	 * 
	 * 
	 * 
	 */
	
	private  int posicionTarjeta;
	
	/**
	 * 
	 * 
	 * 
	 */
	private TipoBusquedaAliado tipoBusqueda;
	
	/**
	 * 
	 * 
	 * 
	 * 		
	 */
	 private ArrayList<HashMap<String, Object>> tiposIdentificacion = new ArrayList<HashMap<String,Object>>();
     /**
      * 
      * 
      * 
      */
	 
	 private InfoDatosStr datosComprador = new InfoDatosStr();
	 /***
	  * 
	  * 
	  * 
	  * 
	  */
	 private boolean primeraEntrada;
	 /**
	  * 
	  * 
	  * 
	  */
	 
	 private HashMap idBeneficiarios;
	 /**
	  * 
	  */
	 private boolean modificando;
	 /**
	  * 
	  */
	 private String tipoVenta;
	 private String indicativoPrincipal;
	 
	 
	 /**
	  * DTO TIPO DE TARJETA 
	  */
	 private DtoTarjetaCliente dtoTipoTarjeta;
	 /**
	  * manejo Serial tmp 
	  */
	 private String serial;
	 /**
	  * 
	  */
	 private InfoDatosDouble aliadoOdontologico;
	 
	 
	 /**
	  *	RECARGAR PAGINA CONTINUAR NO 
	  */
	 private  String recargaPaginaContinuarNo;
	 private  String recargaPaginaContinuarSi;
	 
	 
	 
	 /**
	  * 
	  */
	 private String codigoTipoIdentificacionB;
	 private String numeroIdentificacionB;
	 private String respuestaXml;
	 
	 
	 /*
	  * 
	  */
	public void reset() {
			this.primeraEntrada = true; 
			this.dtoBeneficiario = new DtoBeneficiarioCliente();
		    this.arrayTiposTarjetas = new ArrayList<DtoTarjetaCliente>();
		    this.posicionTarjeta = ConstantesBD.codigoNuncaValido;
			this.arrayAliados = new ArrayList<DtoAliadoOdontologico>();
			this.tiposIdentificacion= new ArrayList<HashMap<String,Object>>();
			this.dtoBusquedaBeneficiario = new DtoBeneficiarioCliente();
			this.arrayResultadoBusqueda =  new ArrayList<DtoBeneficiarioCliente>();
			this.modificando = false;
			this.patronOrdenar = "";
			this.esDescendente="";
			this.datosComprador = new InfoDatosStr();
			this.dtoBusquedaPrincipal = new DtoBeneficiarioCliente();
			this.arrayparentezco = new ArrayList<DtoParentesco>();
			this.dtoPrincipal = new DtoBeneficiarioCliente();
			this.estadoAnterior = "";
			this.jsAliado="accion("+this.dtoBeneficiario.getAlidadoOdontologico().getCodigo()+")";
			this.idBeneficiarios=new HashMap();
			this.listaBeneficiarios = new ArrayList<DtoBeneficiarioCliente>();
			this.listaVentas = new ArrayList<DtoVentaTarjetasCliente>();
			this.tipoVenta= "";
			this.indicativoPrincipal="";
			this.setDtoTipoTarjeta(new  DtoTarjetaCliente());
			this.serial="";
			this.aliadoOdontologico = new InfoDatosDouble();
			this.setRecargaPaginaContinuarNo(ConstantesBD.acronimoNo);
			this.setRecargaPaginaContinuarSi(ConstantesBD.acronimoNo);
			
			/*
			 *PARA CARGAR LA INFORMACION DEL BENEFICIARIO 
			 */
			this.codigoTipoIdentificacionB="";
			this.numeroIdentificacionB="";
			this.setRespuestaXml("");
		}
		
		public HashMap getIdBeneficiarios() {
			return idBeneficiarios;
		}

		public void setIdBeneficiarios(HashMap idBeneficiarios) {
			this.idBeneficiarios = idBeneficiarios;
		}

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
		 * @return the dtoBeneficiario
		 */
		public DtoBeneficiarioCliente getDtoBeneficiario() {
			return dtoBeneficiario;
		}

		/**
		 * @param dtoBeneficiario the dtoBeneficiario to set
		 */
		public void setDtoBeneficiario(DtoBeneficiarioCliente dtoBeneficiario) {
			this.dtoBeneficiario = dtoBeneficiario;
		}

		/**
		 * @return the arrayTiposTarjetas
		 */
		public ArrayList<DtoTarjetaCliente> getArrayTiposTarjetas() {
			return arrayTiposTarjetas;
		}

		/**
		 * @param arrayTiposTarjetas the arrayTiposTarjetas to set
		 */
		public void setArrayTiposTarjetas(
				ArrayList<DtoTarjetaCliente> arrayTiposTarjetas) {
			this.arrayTiposTarjetas = arrayTiposTarjetas;
		}

		/**
		 * @return the posicionTarjeta
		 */
		public int getPosicionTarjeta() {
			return posicionTarjeta;
		}

		/**
		 * @param posicionTarjeta the posicionTarjeta to set
		 */
		public void setPosicionTarjeta(int posicionTarjeta) {
			this.posicionTarjeta = posicionTarjeta;
		}

		/**
		 * @return the arrayAliados
		 */
		public ArrayList<DtoAliadoOdontologico> getArrayAliados() {
			return arrayAliados;
		}

		/**
		 * @param arrayAliados the arrayAliados to set
		 */
		public void setArrayAliados(ArrayList<DtoAliadoOdontologico> arrayAliados) {
			this.arrayAliados = arrayAliados;
		}

		/**
		 * @return the tipoBusqueda
		 */
		public TipoBusquedaAliado getTipoBusqueda() {
			return tipoBusqueda;
		}

		/**
		 * @param tipoBusqueda the tipoBusqueda to set
		 */
		public void setTipoBusqueda(TipoBusquedaAliado tipoBusqueda) {
			this.tipoBusqueda = tipoBusqueda;
		}

		/**
		 * @return the tiposIdentificacion
		 */
		public ArrayList<HashMap<String, Object>> getTiposIdentificacion() {
			return tiposIdentificacion;
		}

		/**
		 * @param tiposIdentificacion the tiposIdentificacion to set
		 */
		public void setTiposIdentificacion(
				ArrayList<HashMap<String, Object>> tiposIdentificacion) {
			this.tiposIdentificacion = tiposIdentificacion;
		}

		/**
		 * @return the modificando
		 */
		public boolean isModificando() {
			return modificando;
		}

		/**
		 * @param modificando the modificando to set
		 */
		public void setModificando(boolean modificando) {
			this.modificando = modificando;
		}

		
		
		
		/**
		 * 
		 */
		public ActionErrors validate(ActionMapping mapping,	HttpServletRequest request)  {
	     
			
			
		  ActionErrors errores = new ActionErrors();
		  HttpSession session=request.getSession();   
		  usuario = (UsuarioBasico)session.getAttribute("usuarioBasico");
		  
		  if(this.getEstado().equals("continuarSi"))
    		{
    			
			  	
			  	logger.info("\n\n\n\n\n FORM----------------- ESTADO CONTINUAR SI -\n\n");
			  	logger.info("EL SERIAL  ES =="+ this.getSerial());
    			
			  	
			  	
				DtoBeneficiarioCliente dtoWhere = new DtoBeneficiarioCliente();		  
				dtoWhere.setSerial(this.getSerial());		  
				dtoWhere.setIndicadorAlidado(ConstantesBD.acronimoSi);
				this.setListaBeneficiarios(BeneficiariosTarjetaCliente.cargar(dtoWhere));
				
				/**
				 * VOLVEMOS A VERIFICAR EXISTENCIA DE BENEFICIARIO
				 */
				if(this.getListaBeneficiarios().size()>0)
				{
					this.setSerial(this.getListaBeneficiarios().get(0).getSerial());
					this.setAliadoOdontologico(this.getListaBeneficiarios().get(0).getAlidadoOdontologico());
				}
			  	
			  	
			  	
			  	
			  	
    			if(this.getSerial().equals(""))
    			{
    				errores.add("", new ActionMessage("errors.required", "El Serial"));
    	    	}
    			
    			logger.info("EL ALIADO =="+ this.getAliadoOdontologico().getCodigo());
        		
    			
    			if(this.getAliadoOdontologico().getCodigo() == ConstantesBD.codigoNuncaValido)
    			{
        			errores.add("", new ActionMessage("errors.required", "El Aliado"));
    			}
        			
        		/*	 CAMIBIOS
        	    if(this.isEsConsulta()){
        	    	if(! VentasTarjetasCliente.existeRangoEnVenta( Double.parseDouble(dtoBeneficiario.getSerial()),Double.parseDouble( dtoBeneficiario.getSerial() ), usuario.getCodigoInstitucionInt() , ConstantesBD.acronimoSi )){
        				errores.add("", new ActionMessage("errors.noExiste", "El Serial Ya esta Registrado"));
        			}
        	    }*/
        	    
        	    /*
    			DtoBeneficiarioCliente dtoWhere = new DtoBeneficiarioCliente();		  
	      		dtoWhere.setSerial(this.getDtoBeneficiario().getSerial());		  
	      		dtoWhere.setIndicadorAlidado(ConstantesBD.acronimoSi);
	      		DtoTarjetaCliente dtoTarjeta = new DtoTarjetaCliente();
	      		dtoTarjeta.setAliado(ConstantesBD.acronimoSi);
	      		dtoWhere.setTipoTarjetaCliente(dtoTarjeta);
	      		  
	      		logger.info("\n\n\n\n VALIDAR SERIAL UN BENEFICIARIO \n\n\n"); 
      		   if(BeneficiariosTarjetaCliente.cargar(dtoWhere).size() > 0)
      		   {
            		errores.add("", new ActionMessage("errors.yaExiste", "El Serial para un beneficiario"));
      		   }*/
    		}
    		
    		
       if(this.getEstado().equals("continuarNo"))
    	{
      		
    	   logger.info("**********************************************************************************************");
    	   logger.info("------------------------EL SERIAL EN LA VALIDACI�N ES =="+ this.getSerial());
    	   logger.info("**********************************************************************************************");
    		if(UtilidadTexto.isEmpty(this.getSerial()))
    		{
    			errores.add("", new ActionMessage("errors.required", "El Serial"));
    	    }
    		else
    		{
    			if(! VentasTarjetasCliente.existeRangoEnVenta(Double.parseDouble(this.getSerial()),Double.parseDouble( this.getSerial() ), usuario.getCodigoInstitucionInt(), ConstantesBD.acronimoNo ) )
    			{
    				errores.add("", new ActionMessage("errors.notEspecific", "El Serial no Existe en Registro de Ventas "));
    			}
    		}
    	}
    		
    		
       if(this.getEstado().equals("Buscar") || this.getEstado().equals("BuscarNo"))
       {     		   
    	   if(
    		  (
    		  this.getDtoBeneficiario().getTipoVenta().equals(ConstantesIntegridadDominio.acronimoTipoBeneficiarioPersona)
    			   ||
    		  this.getDtoBeneficiario().getTipoVenta().equals(ConstantesIntegridadDominio.acronimoTipoBeneficiarioFamiliar)
    		  	   ||
    		  this.getDtoBeneficiario().getTipoVenta().equals(ConstantesIntegridadDominio.acronimoTipoBeneficiarioEmpresa)
    		  )
    			  &&
    			   (
	    			   UtilidadTexto.isEmpty(this.getDtoBusquedaBeneficiario().getDtoPersonas().getTipoIdentificacion()) 
	    			   && UtilidadTexto.isEmpty(this.getDtoBusquedaBeneficiario().getDtoPersonas().getNumeroIdentificacion()) 
	    			   && UtilidadTexto.isEmpty(this.getDtoBusquedaBeneficiario().getDtoPersonas().getPrimerNombre() ) 
	    			   && UtilidadTexto.isEmpty(this.getDtoBusquedaBeneficiario().getDtoPersonas().getSegundoNombre()) 
	    			   && UtilidadTexto.isEmpty(this.getDtoBusquedaBeneficiario().getDtoPersonas().getPrimerApellido()) 
	    			   && UtilidadTexto.isEmpty(this.getDtoBusquedaBeneficiario().getDtoPersonas().getSegundoApellido()) 
	    			)
    	   		)
    	   {
	    	  errores.add("", new ActionMessage("errors.required", "Al menos un campo en Beneficiarios"));
	       }
    	   if(this.getDtoBeneficiario().getTipoVenta().equals(ConstantesIntegridadDominio.acronimoTipoBeneficiarioFamiliar) &&
    			   (
	    			   UtilidadTexto.isEmpty(this.getDtoBusquedaPrincipal().getDtoPersonas().getTipoIdentificacion()) 
	    			   && UtilidadTexto.isEmpty(this.getDtoBusquedaPrincipal().getDtoPersonas().getNumeroIdentificacion()) 
	    			   && UtilidadTexto.isEmpty(this.getDtoBusquedaPrincipal().getDtoPersonas().getPrimerNombre() ) 
	    			   && UtilidadTexto.isEmpty(this.getDtoBusquedaPrincipal().getDtoPersonas().getSegundoNombre()) 
	    			   && UtilidadTexto.isEmpty(this.getDtoBusquedaPrincipal().getDtoPersonas().getPrimerApellido()) 
	    			   && UtilidadTexto.isEmpty(this.getDtoBusquedaPrincipal().getDtoPersonas().getSegundoApellido())
	    			  
	    			)
    	   		)
    	   {
	    	  errores.add("", new ActionMessage("errors.required", "Al menos un campo en Principal"));
	       }
    	   if (this.getDtoBeneficiario().getTipoVenta().equals(ConstantesIntegridadDominio.acronimoTipoBeneficiarioEmpresa) &&
				   (
		   			   UtilidadTexto.isEmpty(this.getDatosComprador().getCodigo())
		   			   &&UtilidadTexto.isEmpty(this.getDatosComprador().getNombre())
	   			   	) 
	   			   
				   )
	   			
	   {
    		   errores.add("", new ActionMessage("errors.required", "Al menos un campo en Comprador"));
	   	}
    }
    	
       
       
       
       if(this.getEstado().equals("GuardarSi") || this.getEstado().equals("GuardarNo") )
       {
    		   logger.info("*************************** ENTRE A GUARDAR X SI ************************************");
    		   logger.info("TIPO ID"+this.getDtoBeneficiario().getDtoPersonas().getTipoIdentificacion() + "    "+this.getDtoBeneficiario().getDtoPersonas().getNumeroIdentificacion()+"  "+this.getDtoBeneficiario().getDtoPersonas().getPrimerNombre()+this.getDtoBeneficiario().getDtoPersonas().getPrimerApellido());
    		   //VALIDAR INDICATIVO PERSONAL EN NO
    		  if( this.getDtoBeneficiario().getIndicativoPrincipal().equals(ConstantesBD.acronimoNo))
    		   {
    			  if(this.getDtoBeneficiario().getAlidadoOdontologico().getCodigo()>0)
    			  {
    				  errores.add("", new ActionMessage("errors.required", "El Aliado Odontologico "));
    			  }
    			  if(this.getDtoBeneficiario().getDtoPersonas().getTipoIdentificacion().equals(""))
    			  {
    				  errores.add("", new ActionMessage("errors.required", "El Tipo de identificaci�n"));
    			  }
    			  if(this.getDtoBeneficiario().getDtoPersonas().getNumeroIdentificacion().equals(""))
    			  {
    				  errores.add("", new ActionMessage("errors.required", "El Numero de identificaci�n"));
    			  }
    			  if(this.getDtoBeneficiario().getDtoPersonas().getPrimerNombre().equals(""))
    			  {
    				  errores.add("", new ActionMessage("errors.required", "El Primer Nombre"));
    			  }
    			  if(this.getDtoBeneficiario().getDtoPersonas().getPrimerApellido().equals(""))
    			  {
    				  errores.add("", new ActionMessage("errors.required", "El Primer Apellido"));
    			  }
    		   }	
    		  
    		  // VALIDAR INDICATIVO PERSONAL EN SI
    	if(this.getTipoVenta().equals(ConstantesIntegridadDominio.acronimoTipoBeneficiarioFamiliar) && this.getDtoBeneficiario().getIndicativoPrincipal().equals(ConstantesBD.acronimoSi))
    			{
    					logger.info("*************************** ENTRE A GUARDAR X SI ************************************");
    					logger.info("TIPO ID"+this.getDtoBeneficiario().getDtoPersonas().getTipoIdentificacion() + "    "+this.getDtoBeneficiario().getDtoPersonas().getNumeroIdentificacion()+"  "+this.getDtoBeneficiario().getDtoPersonas().getPrimerNombre()+this.getDtoBeneficiario().getDtoPersonas().getPrimerApellido());
    	    			
    					if(UtilidadTexto.isEmpty(this.getDtoPrincipal().getDtoPersonas().getTipoIdentificacion()))
    	    			{
    	    				errores.add("", new ActionMessage("errors.required", "El Tipo de identificaci�n del Principal"));
    	    			}
    	    			if(UtilidadTexto.isEmpty(this.getDtoPrincipal().getDtoPersonas().getNumeroIdentificacion()))
    	    			{
    	        			errores.add("", new ActionMessage("errors.required", "El Numero de identificaci�n del Principal"));
    	        		}
    	    			
    	    			if(UtilidadTexto.isEmpty(this.getDtoPrincipal().getDtoPersonas().getPrimerNombre()))
    	    			{
    	        			errores.add("", new ActionMessage("errors.required", "El Primer Nombre del Principal"));
    	        		}
    	    			
    	    			if(UtilidadTexto.isEmpty(this.getDtoPrincipal().getDtoPersonas().getPrimerApellido()))
    	    			{
    	        			errores.add("", new ActionMessage("errors.required", "El Primer Apellido del Principal"));
    	        		}
    	    	}
    			
        			
        		
    		}
    	
    		if(!errores.isEmpty())
			{
				if(this.getEstado().equals("continuarSi"))
					this.setEstado("mostrarErroresContinuarSi");
				
    		
	         }
    		
    		if(!errores.isEmpty())
			{
				if(this.getEstado().equals("continuarNo"))
					this.setEstado("mostrarErroresContinuarNo");
				
    		
	         }
    		
    		if(!errores.isEmpty())
			{
				if(this.getEstado().equals("Guardar"))
					this.setEstado("mostrarErroresGuardar");
				
    		
	         }
    		
    		if(!errores.isEmpty())
			{
				if(this.getEstado().equals("busquedaAvanzadaNo"))
					this.setEstado("mostrarErroresBusquedaNo");
				
    		
	         }
    		
    		if(!errores.isEmpty())
			{
				if(this.getEstado().equals("Buscar"))
					this.setEstado("busquedaAvanzadaSi");
				
				
    		
	         }
    		
    		
    		if(!errores.isEmpty())
			{
				if(this.getEstado().equals("BuscarNo"))
					this.setEstado("busquedaAvanzadaNo");
				
				
    		
	         }
    		
    		
    		if(!errores.isEmpty())
			{
				if(this.getEstado().equals("GuardarSi"))
					this.setEstado("mostrarErroresGuardarSi");
				
    		
	         }
    		
    		if(!errores.isEmpty())
			{
				if(this.getEstado().equals("GuardarNo"))
					this.setEstado("mostrarErroresGuardarNo");
				
    		
	         }
    		
    		return errores;
	      
	      }

		/**
		 * @return the dtoBusquedaBeneficiario
		 */
		public DtoBeneficiarioCliente getDtoBusquedaBeneficiario() {
			return dtoBusquedaBeneficiario;
		}

		/**
		 * @param dtoBusquedaBeneficiario the dtoBusquedaBeneficiario to set
		 */
		public void setDtoBusquedaBeneficiario(
				DtoBeneficiarioCliente dtoBusquedaBeneficiario) {
			this.dtoBusquedaBeneficiario = dtoBusquedaBeneficiario;
		}

		/**
		 * @return the arrayResultadoBusqueda
		 */
		public ArrayList<DtoBeneficiarioCliente> getArrayResultadoBusqueda() {
			return arrayResultadoBusqueda;
		}

		/**
		 * @param arrayResultadoBusqueda the arrayResultadoBusqueda to set
		 */
		public void setArrayResultadoBusqueda(
				ArrayList<DtoBeneficiarioCliente> arrayResultadoBusqueda) {
			this.arrayResultadoBusqueda = arrayResultadoBusqueda;
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
		 * @return the datosComprador
		 */
		public InfoDatosStr getDatosComprador() {
			return datosComprador;
		}

		/**
		 * @param datosComprador the datosComprador to set
		 */
		public void setDatosComprador(InfoDatosStr datosComprador) {
			this.datosComprador = datosComprador;
		}

		/**
		 * @return the dtoBusquedaPrincipal
		 */
		public DtoBeneficiarioCliente getDtoBusquedaPrincipal() {
			return dtoBusquedaPrincipal;
		}

		/**
		 * @param dtoBusquedaPrincipal the dtoBusquedaPrincipal to set
		 */
		public void setDtoBusquedaPrincipal(DtoBeneficiarioCliente dtoBusquedaPrincipal) {
			this.dtoBusquedaPrincipal = dtoBusquedaPrincipal;
		}

		/**
		 * @return the arrayparentezco
		 */
		public ArrayList<DtoParentesco> getArrayparentezco() {
			return arrayparentezco;
		}

		/**
		 * @param arrayparentezco the arrayparentezco to set
		 */
		public void setArrayparentezco(ArrayList<DtoParentesco> arrayparentezco) {
			this.arrayparentezco = arrayparentezco;
		}

		/**
		 * @return the dtoPrincipal
		 */
		public DtoBeneficiarioCliente getDtoPrincipal() {
			return dtoPrincipal;
		}

		/**
		 * @param dtoPrincipal the dtoPrincipal to set
		 */
		public void setDtoPrincipal(DtoBeneficiarioCliente dtoPrincipal) {
			this.dtoPrincipal = dtoPrincipal;
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
		 * @return the jsAliado
		 */
		public String getJsAliado() {
			return "accion("+this.dtoBeneficiario.getAlidadoOdontologico().getCodigo()+")";
		}

		/**
		 * @param jsAliado the jsAliado to set
		 */
		public void setJsAliado(String jsAliado) {
			this.jsAliado = jsAliado;
		}

		/**
		 * @return the primeraEntrada
		 */
		public boolean isPrimeraEntrada() {
			return primeraEntrada;
		}

		/**
		 * @param primeraEntrada the primeraEntrada to set
		 */
		public void setPrimeraEntrada(boolean primeraEntrada) {
			this.primeraEntrada = primeraEntrada;
		}

		/**
		 * @return the mensaje
		 */
		public String getMensaje() {
			return mensaje;
		}

		/**
		 * @param mensaje the mensaje to set
		 */
		public void setMensaje(String mensaje) {
			this.mensaje = mensaje;
		}

		public void setListaBeneficiarios(ArrayList<DtoBeneficiarioCliente> listaBeneficiarios) {
			this.listaBeneficiarios = listaBeneficiarios;
		}

		public ArrayList<DtoBeneficiarioCliente> getListaBeneficiarios() {
			return listaBeneficiarios;
		}

		public void setListaVentas(ArrayList<DtoVentaTarjetasCliente> listaVentas) {
			this.listaVentas = listaVentas;
		}

		public ArrayList<DtoVentaTarjetasCliente> getListaVentas() {
			return listaVentas;
		}

		public void setTipoVenta(String tipoVenta) {
			this.tipoVenta = tipoVenta;
		}

		public String getTipoVenta() {
			return tipoVenta;
		}

		public void setIndicativoPrincipal(String indicativoPrincipal) {
			this.indicativoPrincipal = indicativoPrincipal;
		}

		public String getIndicativoPrincipal() {
			return indicativoPrincipal;
		}

		public void setDtoTipoTarjeta(DtoTarjetaCliente dtoTipoTarjeta) {
			this.dtoTipoTarjeta = dtoTipoTarjeta;
		}

		public DtoTarjetaCliente getDtoTipoTarjeta() {
			return dtoTipoTarjeta;
		}

		public void setSerial(String serial) {
			this.serial = serial;
		}

		public String getSerial() {
			return serial;
		}

		public void setAliadoOdontologico(InfoDatosDouble aliadoOdontologico) {
			this.aliadoOdontologico = aliadoOdontologico;
		}

		public InfoDatosDouble getAliadoOdontologico() {
			return aliadoOdontologico;
		}
		
		
		
		public void setRecargaPaginaContinuarNo(String recargaPaginaContinuarNo) {
			this.recargaPaginaContinuarNo = recargaPaginaContinuarNo;
		}
		

		public String getRecargaPaginaContinuarNo() {
			return recargaPaginaContinuarNo;
		}

		public void setRecargaPaginaContinuarSi(String recargaPaginaContinuarSi) {
			this.recargaPaginaContinuarSi = recargaPaginaContinuarSi;
		}

		public String getRecargaPaginaContinuarSi() {
			return recargaPaginaContinuarSi;
		}

		public String getCodigoTipoIdentificacionB() {
			return codigoTipoIdentificacionB;
		}

		public String getNumeroIdentificacionB() {
			return numeroIdentificacionB;
		}

		public void setCodigoTipoIdentificacionB(String codigoTipoIdentificacionB) {
			this.codigoTipoIdentificacionB = codigoTipoIdentificacionB;
		}

		public void setNumeroIdentificacionB(String numeroIdentificacionB) {
			this.numeroIdentificacionB = numeroIdentificacionB;
		}

		public void setRespuestaXml(String respuestaXml) {
			this.respuestaXml = respuestaXml;
		}

		public String getRespuestaXml() {
			return respuestaXml;
		}

		
}
