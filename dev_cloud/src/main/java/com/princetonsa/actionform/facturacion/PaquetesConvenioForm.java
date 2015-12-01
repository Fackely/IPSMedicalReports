package com.princetonsa.actionform.facturacion;


import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;

public class PaquetesConvenioForm extends ValidatorForm {
	
	/**
	 *  Estado en el que se encuentra el proceso
	 */
	
	private String estado="";
	
	
	/**
	 *  mapa de consultorios
	 */
	
	private HashMap paquetesConMap;
	
	
	/**
	 * clon de paquetesmap al momento de cargalos de la bd,
	 * utilizado para verificar si existieron modificaciones y para
	 * crear el log tipo archivo
	 */
	
	private HashMap paquetesConEliminadosMap;
	
	
	/**
	 * para la navegacion del pager, cuando se ingresa
	 * un registro nuevo.
	 */
	
	private String linkSiguiente;
	
	/**
	 *  Para la navegacion del pager, cuando se ingresa
	 *  un registro nuevo.
	 */
	private int maxPageItems;
	
	/**
	 * 
	 */
	private String patronOrdenar;
	
	
	/**
	 * 
	 */
	private String ultimoPatron;
	
	
	/**
	 *  para controlar la página actual
     *  del pager.
	 */
	
	 private int offset;
	 
	 
	/**
	 * Posicion del registro que se eliminara
	 */
	    
	 private int posEliminar;
	 
	 /**
	  * 
	  */
	 private ArrayList convenios;
	 
	 /**
	  * 
	  */
	 private ArrayList contratos;
	 
	 
	 private ResultadoBoolean mensaje=new ResultadoBoolean(false);
	 
	 /**
	  * 
	  */
	 private int codigoConvenio;
	 
	 /**
	  * 
	  */
	 private int codigoContrato;
	 
	/**
	* 
	*/
	private int posMapa;
	 
	 
	 
	 
	 private HashMap fechainicio = new HashMap();
	 
	 
	 private HashMap fechafinal = new HashMap();
	 
	 
	 
	 
	 
	 public HashMap getFechafinal() {
		return fechafinal;
	}



	public void setFechafinal(HashMap fechafinal) {
		this.fechafinal = fechafinal;
	}



	public HashMap getFechainicio() {
		return fechainicio;
	}



	public void setFechainicio(HashMap fechainicio) {
		this.fechainicio = fechainicio;
	}
	
	
	public Object getFechainicio(String key) {
		return fechainicio.get(key);
	}
	/**
	 * @param asigna un elemento al mapa ordenes.
	 */
	public void setFechainicio(String key,Object obj) {
		this.fechainicio.put(key,obj);
	}
	
	public Object getFechafinal(String key) {
		return fechafinal.get(key);
	}
	/**
	 * @param asigna un elemento al mapa ordenes.
	 */
	public void setFechafinal(String key,Object obj) {
		this.fechafinal.put(key,obj);
	}



	public ResultadoBoolean getMensaje() 
	 {
			return mensaje;
	 }



		public void setMensaje(ResultadoBoolean mensaje) {
			this.mensaje = mensaje;
		}
	 
	 
	 
	 
	 /**
	     * resetea los atributos del form
	     */
		 
	    public void reset()
	    {
	    	paquetesConMap=new HashMap();
	    	paquetesConMap.put("numRegistros","0");
	     	paquetesConEliminadosMap=new HashMap();
	    	paquetesConEliminadosMap.put("numRegistros", "0");
	      	linkSiguiente="";
	       	this.maxPageItems=20;
	    	this.patronOrdenar="";
	    	this.ultimoPatron="";
	    	this.offset=0;
	    	this.posEliminar=ConstantesBD.codigoNuncaValido;
	    	this.convenios=new ArrayList();
	    	this.codigoConvenio=ConstantesBD.codigoNuncaValido;
	    	this.codigoContrato=ConstantesBD.codigoNuncaValido;
	    	this.contratos=new ArrayList();
	    	this.fechainicio = new HashMap();
	    	this.fechafinal = new HashMap();
	    	this.posMapa = ConstantesBD.codigoNuncaValido;
	    }



	public String getEstado() {
		return estado;
	}



	public void setEstado(String estado) {
		this.estado = estado;
	}



	public String getLinkSiguiente() {
		return linkSiguiente;
	}



	public void setLinkSiguiente(String linkSiguiente) {
		this.linkSiguiente = linkSiguiente;
	}



	public int getMaxPageItems() {
		return maxPageItems;
	}



	public void setMaxPageItems(int maxPageItems) {
		this.maxPageItems = maxPageItems;
	}



	public int getOffset() {
		return offset;
	}



	public void setOffset(int offset) {
		this.offset = offset;
	}



	public HashMap getPaquetesConEliminadosMap() {
		return paquetesConEliminadosMap;
	}



	public void setPaquetesConEliminadosMap(HashMap paquetesConEliminadosMap) {
		this.paquetesConEliminadosMap = paquetesConEliminadosMap;
	}
	
	
	
	public Object getPaquetesConEliminadosMap(String key)
	{
		return paquetesConEliminadosMap.get(key);
	}

	
	
	/**
	 * 
	 * @param key
	 * @param value
	 */
	
	public void setPaquetesConEliminadosMap(String key,Object value)
	{
		this.paquetesConEliminadosMap.put(key, value);
	}



	public HashMap getPaquetesConMap() {
		return paquetesConMap;
	}



	public void setPaquetesConMap(HashMap paquetesConMap) {
		this.paquetesConMap = paquetesConMap;
	}
	
	
	
	public Object getPaquetesConMap(String key) {
		return paquetesConMap.get(key);
	}

	
	
	/**
	 * 
	 * @param paquetesMap the paquetesMap to set
	 */
	
	public void setPaquetesConMap(String key,Object value) {
		this.paquetesConMap.put(key, value);
	}
	



	public String getPatronOrdenar() {
		return patronOrdenar;
	}



	public void setPatronOrdenar(String patronOrdenar) {
		this.patronOrdenar = patronOrdenar;
	}



	public int getPosEliminar() {
		return posEliminar;
	}



	public void setPosEliminar(int posEliminar) {
		this.posEliminar = posEliminar;
	}



	public String getUltimoPatron() {
		return ultimoPatron;
	}



	public void setUltimoPatron(String ultimoPatron) {
		this.ultimoPatron = ultimoPatron;
	}
	    
	    
	    
	    
	    /**
		 *  Validate the properties that have been set from this HTTP request, and
		 *  return an <code>ActionErrors</code> object that encapsulates any 
		 *  validation errors that have been found. If no errors are found, return
		 *  <code>null</code> or an <code>ActionErrors</code> object with no recorded
		 *  error messages.
		 *  @param mapping The mapping used to select this instance
		 *  @param request The servlet request we are processing
		 */
	    
		public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
		{
			ActionErrors errores= new ActionErrors();
			
			Connection con=UtilidadBD.abrirConexion();
			
			if(this.estado.equals("guardar"))
			{
				int numReg=Integer.parseInt(this.paquetesConMap.get("numRegistros")+"");
				
				
				for(int i=0;i<numReg;i++)
				{
					
					if((this.paquetesConMap.get("paquete_"+i)+"").trim().equals(""))
					{
						errores.add("codigo", new ActionMessage("errors.required","El Paquete asociado del registro "+(i+1)));
					}
					
					if((this.paquetesConMap.get("viaingreso_"+i)+"").trim().equals(""))
					{
						errores.add("codigo", new ActionMessage("errors.required","La Via de Ingreso asociada del registro "+(i+1)));
					}
					
				
						if((this.paquetesConMap.get("viaingreso_"+i)+"").trim().equals("-1"))
						{
							for (int j=0;j<numReg;j++)
								if (j!=i && (this.paquetesConMap.get("paquete_"+i)+"").equals((this.paquetesConMap.get("paquete_"+j)+""))&&UtilidadFecha.existeTraslapeEntreFechas(this.getPaquetesConMap("fechainicialvenc_"+i)+"", this.getPaquetesConMap("fechafinalvenc_"+i)+"", this.getPaquetesConMap("fechainicialvenc_"+j)+"", this.getPaquetesConMap("fechafinalvenc_"+j)+""))
									errores.add("codigo", new ActionMessage("error.opcionIncluida"," Via de Ingreso","Paquete del registro "+(j+1),"del registro "+(i+1)));
							
						}
					
					
					
					if(!UtilidadTexto.isEmpty(this.getPaquetesConMap("fechainicialvenc_"+i).toString()) || !UtilidadTexto.isEmpty(this.getPaquetesConMap("fechafinalvenc_"+i).toString()))
					{
						boolean centinelaErrorFechas=false;
						if(!UtilidadFecha.esFechaValidaSegunAp(this.getPaquetesConMap("fechainicialvenc_"+i).toString()))
						{
							errores.add("", new ActionMessage("errors.formatoFechaInvalido", "Inicial "+this.getPaquetesConMap("fechainicialvenc_"+i)));
							centinelaErrorFechas=true;
						}
						if(!UtilidadFecha.esFechaValidaSegunAp(this.getPaquetesConMap("fechafinalvenc_"+i).toString()))
						{
							errores.add("", new ActionMessage("errors.formatoFechaInvalido", "Final "+this.getPaquetesConMap("fechafinalvenc_"+i)));
							centinelaErrorFechas=true;
						}
						
						if(!centinelaErrorFechas)
						{
							if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.getPaquetesConMap("fechainicialvenc_"+i).toString(), this.getPaquetesConMap("fechafinalvenc_"+i).toString()))
							{
								errores.add("", new ActionMessage("errors.fechaPosteriorIgualActual", "inicial "+this.getPaquetesConMap("fechainicialvenc_"+i), "final "+this.getPaquetesConMap("fechafinalvenc_"+i)));
							}
						}
					}
				}
				
				if(errores.isEmpty())
				{
					for(int i=0;i<numReg;i++)
					{
						String paquete=this.paquetesConMap.get("paquete_"+i).toString();
						String viaIngreso=this.paquetesConMap.get("viaingreso_"+i).toString();
						
						for(int j=(i+1); j<numReg; j++)
						{
							if(paquete.equals(this.paquetesConMap.get("paquete_"+j).toString())
								&& viaIngreso.equals(this.paquetesConMap.get("viaingreso_"+j).toString())&&UtilidadFecha.existeTraslapeEntreFechas(this.getPaquetesConMap("fechainicialvenc_"+i)+"", this.getPaquetesConMap("fechafinalvenc_"+i)+"", this.getPaquetesConMap("fechainicialvenc_"+j)+"", this.getPaquetesConMap("fechafinalvenc_"+j)+""))
							{
								errores.add("", new ActionMessage("errors.yaExiste", "El paquete "+this.paquetesConMap.get("descpaquete_"+i)+" y Vía Ingreso "+Utilidades.obtenerNombreViaIngreso(con, Utilidades.convertirAEntero(this.paquetesConMap.get("viaingreso_"+i)+""))));
							}
						}
						
					}	
				}
				
			}
			
			
			try {
				UtilidadBD.cerrarConexion(con);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return errores;
		}



		public ArrayList getConvenios() {
			return convenios;
		}



		public void setConvenios(ArrayList convenios) {
			this.convenios = convenios;
		}



		public int getCodigoContrato() {
			return codigoContrato;
		}



		public void setCodigoContrato(int codigoContrato) {
			this.codigoContrato = codigoContrato;
		}



		public int getCodigoConvenio() {
			return codigoConvenio;
		}



		public void setCodigoConvenio(int codigoConvenio) {
			this.codigoConvenio = codigoConvenio;
		}



		public ArrayList getContratos() {
			return contratos;
		}



		public void setContratos(ArrayList contratos) {
			this.contratos = contratos;
		}


		/**
		 * @return the posMapa
		 */
		public int getPosMapa() {
			return posMapa;
		}

		/**
		 * @param posMapa the posMapa to set
		 */
		public void setPosMapa(int posMapa) {
			this.posMapa = posMapa;
		}
}
