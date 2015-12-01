package com.princetonsa.actionform.inventarios;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;
import java.util.ArrayList;
import java.util.HashMap;
import util.ConstantesBD;
import util.ResultadoBoolean;
import util.UtilidadFecha;
import util.UtilidadTexto;

/**
 * @author Juan Alejandro Cardona
 * Fecha: Octubre de 2008
 */

public class MedicamentosControladosAdministradosForm extends ValidatorForm {

	/**	 * MANEJO DE LA FORMA	 */
		private String estado;		//Bandera para Manejar el estado de la Forma
		private ResultadoBoolean mensaje = new ResultadoBoolean(false);	//Mensaje que informa sobre la generacion de la aplicacion 

		
	/**	 * VALIDACIONES DE LA FORMA	 */
		private boolean errores; 	 // Bandera para el manejo de errores en el validate - sin
		private boolean zip;		// Valida si se genero el archivo .zip para informar al usuario

		
	    /** * PARA LA GENERACION DE LOS ARCHIVOS */
		private String pathArchivoTxt;	// Path completo del archivo generado
		private boolean archivo;		// Controla si se genera el archivo o no?
	    private String tipoSalida;		// Maneja el tipo de salida que se desea ejecutar (Pdf, Archivo Plano, Pantalla)
		
		
	/** * DATOS DE LA FORMA */
	    private HashMap frmCentroAtencion;		// HashMap para los Centros de Atencion
	    private String frmCodigoCentroAtencion;	// Codigo del Centro de Atencion seleccionado para realizar el filtro
	    
		private HashMap frmCentrosCosto;			// Mapa con la información de los centros de costo
		private String frmCentroCostoSeleccionado;	// para el centro de costo seleccionado 
	    
	    private String frmFechaInicial;			// Fecha Inicial administracion
	    private String frmFechaFinal;			// Fecha Final administracion

	    private ArrayList<HashMap<String, Object>> frmConvenios;     // Carga los datos del select de Convenios
	    private String frmConvenioSeleccionado;					     // Variable que maneja el codigo del convenio seleccionado
	    
	    private String frmDescArt;						// descripcion del articulo usado en la vista
	    private String frmArticulo;						// codigo del articulo usado en la vista
	    private int frmCategoriaArt;					// categoria del articulo solo se cargan los de control
	    private String frmTipoCodigo;					// codigo del articulo a mostrar - Control
	    
	    private ArrayList<HashMap<String, Object>> frmViaIngreso;     // Carga los datos del select de Via Ingreso X tipo paciente
	    private String frmViaIngresoSeleccionado;					   // Variable que maneja el codigo de la via de ingreso seleccionado
	    
		private HashMap frmListadoBusqueda;			//Para manejar los resultados de los valores de la busqueda 

		
    /**  * VARIABLES PARA EL PAGER Y EL ORDENAMIENTO */
		
	    private String patronOrdenar;		// String para ordenar el resultado de la busqueda por un nuevo patron
	    private String ultimoPatron;		// String que almacena el ordenamiento del ultimo patron ordenado
	    
    /** * VARIABLES PARA PAGER     */
	    private int currentPageNumber;	// Atributo para el manejo de la paginacion con memoria
	    private int offset;				// Para controlar la página actual del pager.
	    private String linkSiguiente;	// Para controlar el link siguiente del pager 
	    
	    
	/**	 * RESET DE LA FORMA */
	    public void reset() {
	    	this.frmCentroAtencion = new HashMap();
	    	this.frmCentroAtencion.put("numRegistros", "0");
	    	this.frmCodigoCentroAtencion = "";
			this.frmCentrosCosto=new HashMap();
			this.frmCentrosCosto.put("numRegistros", "0");
	    	this.frmFechaInicial = "";
	    	this.frmFechaFinal = "";
	    	this.frmConvenios = new ArrayList<HashMap<String,Object>>();
	    	this.frmConvenioSeleccionado = "";
	    	this.frmViaIngreso = new ArrayList<HashMap<String,Object>>();
	    	this.frmViaIngresoSeleccionado = "";
	    	this.frmDescArt = "";
	    	this.frmArticulo = "";
	    	this.frmCategoriaArt = ConstantesBD.codigoCategoriaArtControl;
	    	this.frmTipoCodigo = "";
	    	this.frmListadoBusqueda = new HashMap();
	    	this.frmListadoBusqueda.put("numRegistros", "0");


			this.patronOrdenar = "";
			this.ultimoPatron = "";

	        this.currentPageNumber = 1;  //0
	        this.linkSiguiente = "";
	        this.offset = 0;

	    	
	    	this.tipoSalida = "";
	    	this.pathArchivoTxt = "";
		 	this.archivo = false;
		 	this.errores = false;
		 	this.zip = false;
	    }
    
	    
    /**    VALIDACION DE CAMPOS + ERRORES */
	    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {

	    	ActionErrors errores = new ActionErrors();
	    	
	    	if(this.estado.equals("buscar") || this.estado.equals("imprimir")) {
				//Se Valida el centro de Atencion
	    		if(this.frmCodigoCentroAtencion.trim().equals("") || this.frmCodigoCentroAtencion.trim().equals(null)) {
					errores.add("Centro de Atención", new ActionMessage("errors.required","El Centro de Atención "));
					this.errores = true;
				}
				
	    		//Se valida la forma en que se va a mostrar la info sin en pdf, archivo plano o pantalla
//				if(this.tipoSalida.trim().equals("") || this.tipoSalida.trim().equals("null")) {
//					errores.add("Tipo Salida", new ActionMessage("errors.required","El Tipo de Salida "));
//					this.errores = true;
//				}
				
				//Se valida el Tipo de Codigo
				if(this.frmTipoCodigo.trim().equals("") || this.frmTipoCodigo.trim().equals("null")) {
					errores.add("codigo", new ActionMessage("errors.required","El Tipo de Código "));
					this.errores = true;
				}
				
				//Se valida la fecha inicial
				if(this.frmFechaInicial.trim().equals("") || this.frmFechaInicial.trim().equals("null")) {
					errores.add("codigo", new ActionMessage("errors.required","La Fecha Inicial "));
					this.errores = true;
				}

				//se valida la fecha final
				if(this.frmFechaFinal.trim().equals("") || this.frmFechaFinal.trim().equals("null")) {
					errores.add("codigo", new ActionMessage("errors.required","La Fecha Final "));
					this.errores = true;
				}
				
				
				// se valida la diferencia entre fechas ke sean correctas en el formato 
				if(!UtilidadTexto.isEmpty(this.frmFechaInicial) && !UtilidadTexto.isEmpty(this.frmFechaFinal)) {
					boolean centinelaErrorFechas = false;

					//la fecha inicial es valida segun la aplicacion
					if(!UtilidadFecha.esFechaValidaSegunAp(this.frmFechaInicial)) {
						errores.add("", new ActionMessage("errors.formatoFechaInvalido", "Inicial "+this.frmFechaInicial));
						centinelaErrorFechas=true;
						this.errores = true;
					}

					//la fecha final es valida segun la aplicacion
					if(!UtilidadFecha.esFechaValidaSegunAp(this.frmFechaFinal)) {
						errores.add("", new ActionMessage("errors.formatoFechaInvalido", "Final "+this.frmFechaFinal));
						centinelaErrorFechas=true;
						this.errores = true;
					}

					// si las fechas son validas 
					if(!centinelaErrorFechas) {
						
						//si la fecha inicial es menor ke la final 
						if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.frmFechaInicial, this.frmFechaFinal))	{
							errores.add("", new ActionMessage("errors.fechaPosteriorIgualActual", "Inicial "+this.frmFechaInicial, "Final "+this.frmFechaFinal));
							this.errores = true;
						}

						//si la fecha final es menor ke la del sistema  
						if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.frmFechaFinal, UtilidadFecha.getFechaActual().toString())) {
							errores.add("", new ActionMessage("errors.fechaPosteriorIgualActual", "Final "+this.frmFechaFinal, "Actual " + UtilidadFecha.getFechaActual()));
							this.errores = true;
						}
						
						//si la fecha inicial es menor ke la del sistema
						if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.frmFechaInicial, UtilidadFecha.getFechaActual().toString())) {
							errores.add("", new ActionMessage("errors.fechaPosteriorIgualActual", "Inicial "+this.frmFechaInicial, "Actual "+UtilidadFecha.getFechaActual()));
							this.errores = true;
						}
						
						//diferencia entre las fechas no es mayor a 90 dias
						if(UtilidadFecha.numeroMesesEntreFechasExacta(this.getFrmFechaInicial(), this.getFrmFechaFinal()) >= 3) {
							errores.add("", new ActionMessage("error.facturacion.maximoRangoFechas", "para consultar los Medicamentos Controlados Administrados", "3", "90"));
							this.errores = true;
						}
					}
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

		// dlel pathArchivoTxt	 
		public String getPathArchivoTxt() {		return pathArchivoTxt;		}
		public void setPathArchivoTxt(String pathArchivoTxt) {		this.pathArchivoTxt = pathArchivoTxt;	}

		
		//del archivo
		public boolean isArchivo() {	return archivo;	}
		public void setArchivo(boolean archivo) {	this.archivo = archivo;	}

		
		// De Errores
		public boolean isErrores() {	return errores;	}
		public void setErrores(boolean errores) {	this.errores = errores;		}

		
		// Del Zip 
		public boolean isZip() {	return zip;	}
		public void setZip(boolean zip) {	this.zip = zip;	}


		// Tipo Salida 
		public String getTipoSalida() {	return tipoSalida;		}
		public void setTipoSalida(String tipoSalida) {	this.tipoSalida = tipoSalida;	}


		// Del Mapa de Centro Atencion
		public HashMap getFrmCentroAtencion() { return frmCentroAtencion;	}
		public void setFrmCentroAtencion(HashMap frmCentroAtencion) {	this.frmCentroAtencion = frmCentroAtencion;	}
		public Object getFrmCentroAtencion(String key)	{	return frmCentroAtencion.get(key);	}
		public void setFrmCentroAtencion(String key, Object value)	{	this.frmCentroAtencion.put(key, value);	}


		// FechaInicial
		public String getFrmFechaInicial() {	return frmFechaInicial;	}
		public void setFrmFechaInicial(String frmFechaInicial) {	this.frmFechaInicial = frmFechaInicial;	}


		// FechaFinal
		public String getFrmFechaFinal() {	return frmFechaFinal;	}
		public void setFrmFechaFinal(String frmFechaFinal) {	this.frmFechaFinal = frmFechaFinal;	}


		 //CodigoCentroAtencion
		public String getFrmCodigoCentroAtencion() {	return frmCodigoCentroAtencion;	}
		public void setFrmCodigoCentroAtencion(String frmCodigoCentroAtencion) {	this.frmCodigoCentroAtencion = frmCodigoCentroAtencion;	}

		
		 // frmConvenioSeleccionado
		public String getFrmConvenioSeleccionado() { return frmConvenioSeleccionado;	}
		public void setFrmConvenioSeleccionado(String frmConvenioSeleccionado) {	this.frmConvenioSeleccionado = frmConvenioSeleccionado;	}
		

		 //Array List de Convenios
		public ArrayList<HashMap<String, Object>> getFrmConvenios() {	return frmConvenios;	}
		public void setFrmConvenios(ArrayList<HashMap<String, Object>> frmConvenios) {	this.frmConvenios = frmConvenios;	}


		 // frmTipoCodigo
		public String getFrmTipoCodigo() {	return frmTipoCodigo;	}
		public void setFrmTipoCodigo(String frmTipoCodigo) { this.frmTipoCodigo = frmTipoCodigo;	}


		 // frmDescArt
		public String getFrmDescArt() {	return frmDescArt;	}
		public void setFrmDescArt(String frmDescArt) {	this.frmDescArt = frmDescArt;	}


		 // frmArticulo
		public String getFrmArticulo() {	return frmArticulo;		}
		public void setFrmArticulo(String frmArticulo) {	this.frmArticulo = frmArticulo;		}

		
		 // centrosCosto
		public HashMap getFrmCentrosCosto() {	return frmCentrosCosto;	}
		public void setFrmCentrosCosto(HashMap frmCentrosCosto) {	this.frmCentrosCosto = frmCentrosCosto;	}


		 // frmCategoriaArt
		public int getFrmCategoriaArt() {	return frmCategoriaArt;		}
		public void setFrmCategoriaArt(int frmCategoriaArt) {	this.frmCategoriaArt = frmCategoriaArt;		}


		 // frmCentroCostoSeleccionado
		public String getFrmCentroCostoSeleccionado() {		return frmCentroCostoSeleccionado;		}
		public void setFrmCentroCostoSeleccionado(String frmCentroCostoSeleccionado) {	this.frmCentroCostoSeleccionado = frmCentroCostoSeleccionado;	}
		

		 //Array List de frmViaIngreso
		public ArrayList<HashMap<String, Object>> getFrmViaIngreso() {	return frmViaIngreso;	}
		public void setFrmViaIngreso(ArrayList<HashMap<String, Object>> frmViaIngreso) {	this.frmViaIngreso = frmViaIngreso;	}


		 // frmViaIngresoSeleccionado
		public String getFrmViaIngresoSeleccionado() {	return frmViaIngresoSeleccionado;	}
		public void setFrmViaIngresoSeleccionado(String frmViaIngresoSeleccionado) { this.frmViaIngresoSeleccionado = frmViaIngresoSeleccionado;	}
		

		// frmListadoBusqueda 
		public HashMap getFrmListadoBusqueda() {	return frmListadoBusqueda;		}
		public void setFrmListadoBusqueda(HashMap frmListadoBusqueda) {	this.frmListadoBusqueda = frmListadoBusqueda;	}
		public Object getFrmListadoBusqueda(String key) {	return frmListadoBusqueda.get(key);	}
		public void setFrmListadoBusqueda(String key,Object value) { this.frmListadoBusqueda.put(key, value);	}


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
		
}