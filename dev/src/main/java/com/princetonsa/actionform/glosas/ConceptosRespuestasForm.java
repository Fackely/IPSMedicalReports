package com.princetonsa.actionform.glosas;

/*
 * @author: Juan Alejandro Cardona
 * date: Octubre 2008
 */

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import util.ResultadoBoolean;



public class ConceptosRespuestasForm extends ValidatorForm {

	
	/**	 * MANEJO DE LA FORMA	 */
		private String estado;		//Bandera para Manejar el estado de la Forma
		private ResultadoBoolean mensaje = new ResultadoBoolean(false);	//Mensaje que informa sobre la generacion de la aplicacion 

	
	/**	 * VALIDACIONES DE LA FORMA	 */
		private boolean errores; 	 // Bandera para el manejo de errores en el validate - sin
	

    /**  * VARIABLES PARA EL PAGER Y EL ORDENAMIENTO */
	    private String patronOrdenar;		// String para ordenar el resultado de la busqueda por un nuevo patron
	    private String ultimoPatron;		// String que almacena el ordenamiento del ultimo patron ordenado
	    
    /** * VARIABLES PARA PAGER     */
	    private int currentPageNumber;	// Atributo para el manejo de la paginacion con memoria
	    private int offset;				// Para controlar la página actual del pager.
	    private String linkSiguiente;	// Para controlar el link siguiente del pager 
		
		
	/**	 * VARIABLES DE LA FORMA	 */
	    private String codigoConcepto;	// código del concepto (alfanumerico)
	    private String descripcion;		// Descripción del concepto	
	    private String tipoConcepto;	// Tipo Concepto (Glosa, Devolucion, Respuesta)
		private String conceptoGlosa;	// conceptos de glosa
		private String conceptoAjuste;	// conceptos de ajuste
		
		private HashMap mapConceptoGlosa;	// Mapa para cargar los datos de conceptos glosas
		private HashMap mapConceptoAjuste;	// Mapa para cargar los datos de conceptos ajustes
		
	    

		
	/** *DATOS QUE ESTABAN EN EL FORM ANTERIOR - Cartera */
	    private HashMap mapConceptos; 	// para almacenar todos los datos correspondientes al formulario estados de los datos
	    private HashMap mapConceptosBD;	// para almacenar los datos que provienen de la BD, a los cuales no se les realizaran modificaciones.
	    private String accion;					// almacenar acciones, eventos.
	    private int maxPageItems;				// el numero de registros por pager
	    private int index;						// Variable para manejar el index
	    private int numRegistros;				// para controlar el numero de registros del HashMap
	    
	    
	
	/**	 * RESET DE LA FORMA */
		public void reset() {
		 	this.codigoConcepto = "";
		 	this.descripcion = "";
		 	this.tipoConcepto = "";
		 	this.conceptoAjuste = "";
		 	this.conceptoGlosa = "";
		 	
		 	this.mapConceptoGlosa = new HashMap ();
		 	this.mapConceptoAjuste = new HashMap ();
		 	
		 	
		 	this.mapConceptos = new HashMap ();
		 	this.mapConceptosBD = new HashMap ();
		 	this.accion = "";
		 	this.maxPageItems = 10;
		 	this.index=0;
		 	this.numRegistros = 0;

		 	
			this.patronOrdenar = "";
			this.ultimoPatron = "";
	        this.currentPageNumber = 1;  //0
	        this.linkSiguiente = "";
	        this.offset = 0;
	    	
		 	this.errores = false;
		}

		
		public void resetMsg() {
			this.mensaje = new ResultadoBoolean(false);
		}
		
		
    /**    VALIDACION DE CAMPOS + ERRORES */
	    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {

	    	ActionErrors errores = new ActionErrors();
		    String codigo = "";

	    	
			if(estado.equals("GuardarConceptoRespuestasGlosas")) {
				codigo = "";

				for(int k=0;k<Integer.parseInt(this.getMapConceptos("numRegistros")+"");k++) {
			    
					//El código es requerido
					if((this.getMapConceptos("codigoRespuestaConcepto_"+k)+"").trim().equals("")) {
			          errores.add("CODIGO REQUERIDO", new ActionMessage("errors.required","El código para el registro "+(k+1)));  
					}
			      
					//La descripción es requerida
					if((this.getMapConceptos("descripcion_"+k)+"").equals("")) {
			          errores.add("DESCRIPCION REQUERIDO", new ActionMessage("errors.required","La descripción para el registro "+(k+1)));  
					}
					
					//El Tipo Concepto es Requerido
					if((this.getMapConceptos("tipoConcepto_"+k)+"").equals("")) {
			          errores.add("TIPO CONCEPTO REQUERIDO", new ActionMessage("errors.required","El Tipo Concepto para el registro "+(k+1)));  
					}
					

			      //validar que el codigo no exista
			      codigo = this.getMapConceptos("codigoRespuestaConcepto_"+k)+"";
			      for(int l=0;l<k;l++)  {		         
		             if(codigo.equals(this.getMapConceptos("codigoRespuestaConcepto_"+l)+"") && !(this.getMapConceptos("codigoRespuestaConcepto_"+l)+"").equals("")) {		                  		                  
		                  errores.add("YA EXISTE EL REGISTRO.", new ActionMessage("errors.yaExiste","El código "+codigo));                 
		             }
				  }
			      codigo="";
			    }
			}
	    	
	    
			if(errores.isEmpty()) {
				this.errores = false;
			}

	    	return errores;
	    }

	    
	    
	/** * GET'S & SET'S */
		//Del Estado 
		public String getEstado() {		return estado;		}
		public void setEstado(String estado) {	this.estado = estado;	}
		
	
		// del Mensaje 
		public ResultadoBoolean getMensaje() {	return mensaje;		}
		public void setMensaje(ResultadoBoolean mensaje) {	this.mensaje = mensaje; 	}

		
		// De Errores
		public boolean isErrores() {	return errores;	}
		public void setErrores(boolean errores) {	this.errores = errores;		}
	    
	
		 // patronOrdenar
		public String getPatronOrdenar() {	return patronOrdenar;	}
		public void setPatronOrdenar(String patronOrdenar) {	this.patronOrdenar = patronOrdenar;	}


		 //ultimoPatron
		public String getUltimoPatron() {	return ultimoPatron;	}
		public void setUltimoPatron(String ultimoPatron) {	this.ultimoPatron = ultimoPatron;	}


		 // currentPageNumber
		public int getCurrentPageNumber() {	return currentPageNumber;	}
		public void setCurrentPageNumber(int currentPageNumber) {	this.currentPageNumber = currentPageNumber;	}


		 // offset
		public int getOffset() {	return offset;	}
		public void setOffset(int offset) {	this.offset = offset;	}


		 // linkSiguiente
		public String getLinkSiguiente() {	return linkSiguiente;	}
		public void setLinkSiguiente(String linkSiguiente) {	this.linkSiguiente = linkSiguiente;	}


		 //codigoConcepto
		public String getCodigoConcepto() {		return codigoConcepto;		}
		public void setCodigoConcepto(String codigoConcepto) {	this.codigoConcepto = codigoConcepto;	}


		 //descripcion
		public String getDescripcion() {	return descripcion;		}
		public void setDescripcion(String descripcion) {	this.descripcion = descripcion;	}


		 //tipoConcepto
		public String getTipoConcepto() {	return tipoConcepto;	}
		public void setTipoConcepto(String tipoConcepto) {	this.tipoConcepto = tipoConcepto;	}


		 //conceptoGlosa
		public String getConceptoGlosa() {	return conceptoGlosa;	}
		public void setConceptoGlosa(String conceptoGlosa) {	this.conceptoGlosa = conceptoGlosa;		}


		 //conceptoAjuste 
		public String getConceptoAjuste() {		return conceptoAjuste;	}
		public void setConceptoAjuste(String conceptoAjuste) {		this.conceptoAjuste = conceptoAjuste;		}

		
		
	     //mapConceptoGlosa
	    public HashMap getMapConceptoGlosa() {  return mapConceptoGlosa;   }
	    public void setMapConceptoGlosa(HashMap mapConceptoGlosa) {   this.mapConceptoGlosa = mapConceptoGlosa;    }
	    public Object getMapConceptoGlosa(String key) {     return mapConceptoGlosa.get(key);   }
	    public void setMapConceptoGlosa(String key,Object value) {   this.mapConceptoGlosa.put(key,value);   }
		
		
	     //mapConceptoAjuste
	    public HashMap getMapConceptoAjuste() {  return mapConceptoAjuste;   }
	    public void setMapConceptoAjuste(HashMap mapConceptoAjuste) {   this.mapConceptoAjuste = mapConceptoAjuste;    }
	    public Object getMapConceptoAjuste(String key) {     return mapConceptoAjuste.get(key);   }
	    public void setMapConceptoajuste(String key,Object value) {   this.mapConceptoAjuste.put(key,value);   }
		
		
	     //mapConceptos.
	    public HashMap getMapConceptos() {  return mapConceptos;   }
	    public void setMapConceptos(HashMap mapConceptos) {   this.mapConceptos = mapConceptos;    }
	    public Object getMapConceptos(String key) {     return mapConceptos.get(key);   }
	    public void setMapConceptos(String key,Object value) {   this.mapConceptos.put(key,value);   }

	    
	     // mapConceptosBD
	    public HashMap getMapConceptosBD() {	return mapConceptosBD;	}
	    public void setMapConceptosBD(HashMap mapConceptosBD) {   this.mapConceptosBD = mapConceptosBD;    }
	    public Object getMapConceptosBD(String key) {     return mapConceptosBD.get(key);    }
	    public void setMapConceptosBD(String key, Object value) {    this.mapConceptosBD.put(key,value);   }
	    

		 //accion
		public String getAccion() {		return accion;	}
		public void setAccion(String accion) {		this.accion = accion;	}


		 // maxPageItems
		public int getMaxPageItems() {	return maxPageItems;	}
		public void setMaxPageItems(int maxPageItems) {		this.maxPageItems = maxPageItems;		}


		 //index
		public int getIndex() {	return index;	}
		public void setIndex(int index) {	this.index = index;		}


		 // numRegistros
		public int getNumRegistros() {	return numRegistros;	}
		public void setNumRegistros(int numRegistros) {	this.numRegistros = numRegistros;	}
		
}