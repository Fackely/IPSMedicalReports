package com.princetonsa.actionform.salasCirugia;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.ResultadoBoolean;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.UtilidadValidacion;
import util.Utilidades;

/**
 * 
 * @author Andr&eacute;s Mauricio Guerrero Toro
 *
 */
public class AsociosXUvrForm extends ValidatorForm {
	
	/**
	 * Estado en el que se encuetra el proceso
	 */
	private String estado;
	
	/**
	 * Mapa que almacena el encabezado por tipo de asocio y tipo de sala segun el convenio o esquema tarifario
	 */
	private HashMap asociosUvrTipoSalaMap;
	
	private HashMap asociosUvrTipoSalaMapClone;
	/**
	 * 
	 */
	private HashMap asociosUvrTipoSalaEliminadosMap;
	
	/**
	 * Mapa que almacena el detalle de los asocios por uvr sea por convenio o esquema tarifario
	 */
	private HashMap asociosUvrDetalleMap;
	
	/**
	 * Mapa que almacena las vigencias segun el convenio
	 */
	private HashMap vigenciasConvenioMap;
	
	/**
	 * 
	 */
	private HashMap vigenciasConvenioEliminadosMap;
	
	/**
	 * Mapa que almacena los registros eliminados del detalle
	 */
	private HashMap asociosUvrDetalleEliminadosMap;
	
	/**
	 * 
	 */
	private ArrayList especialidades;
	
	
	/**
	 * Indice del mapa que se desea eliminar
	 */
	private int indiceEliminado;
	
	/**
	 * Variable que almacen la posicion seleccionada del mapa
	 */
	private int indexSeleccionado;
	
	/**
	 * 
	 */
	private String patronOrdenar;
	
	/**
	 * 
	 */
	private String ultimoPatron;
	
	/**
	 * 
	 */
	private ResultadoBoolean mensaje=new ResultadoBoolean(false);
	
	/**
	 * Almacena el codigo del equema tarifario escogido para la navegacion en la funcionalidad
	 */
	int esquemaTarifario;
	
	/**
	 * Almacena el codigo del convenio escogido para la navegacion en la funcionalidad
	 */
	int convenio;
	
	/**
	 * Almacena el codigo del encabezado maestro para realizar consultas
	 */
	int codigoAsocioUvr;
	
	/**
	 * Almacena el codigo del tipo de asocio
	 */
	int tipoAsocio;
	
	/**
	 * Almacena el codigo del tipo de sala seleccionado
	 */
	int tipoSala;
	
	/**
	 * Almacena el codigo del tipo de liquidacion seleccionado
	 */
	int tipoLiquidacion;
	
	/**
	 * 
	 */
	String tipoServicio;
	
	/**
	 * 
	 */
	int tipoAnestesia;
	
	/**
	 * 
	 */	
	String ocupacion;
	
	/**
	 * 
	 */
	String especialidad;
	
	/**
	 * 
	 */
	String tipoEspecialista;
	
	/**
	 * 
	 */
	private ArrayList convenios;
	
	/**
	 * 
	 */
	private ArrayList tiposServicio;
	
	/**
	 * 
	 */
	public ArrayList tarifariosOficiales;
	
	/**
	 * 
	 */
	public ArrayList tiposLiquidacion;
	
	/**
	 * 
	 */
	public ArrayList esquemasTatifarios;
	
	/**
	 * 
	 */
	public ArrayList tiposAsocio;
	
	/**
	 * 
	 */
	public ArrayList tiposSala;
	
	/**
	 * 
	 */
	public ArrayList ocupacionesMedicas;
	
	/**
	 * 
	 */
	public ArrayList tiposAnestesia;
	
	/**
	 * 
	 */
	private String nombreTipoAsocio;
	
	/**
	 * 
	 */
	private String nombreTipoSala;
	
	/**
	 * 
	 */
	private String esConsulta;
	
	/**
	 * atributo para obtener la cantidad del esquema seleccionado
	 */
	private String cantidadesquematarifario;
	
	
	private ArrayList<HashMap<String, Object>> tiposLiq = new ArrayList<HashMap<String,Object>>();
	

	private String index = ""; 
	
	/***************************************
	 * ATRIBUTOS PARA EL PAGER
	 ***************************************/
	
		/**
		 * Se utiliza para la navegacion de pager
		 */
		private String linkSiguiente;
	   /**
	    * Atributo para el manejo de la paginacion con memoria
	    */
	   private int currentPageNumber; 
	
	   /**
	    * Para controlar la página actual del pager.
	    */
	   private int offset; 
	/***************************************
	 * FIN ATRIBUTOS PARA EL PAGER
	 ***************************************/
	
	
	public void reset_pager ()
	{
		this.linkSiguiente="";
		this.indiceEliminado=ConstantesBD.codigoNuncaValido;
    	this.patronOrdenar="";
    	this.ultimoPatron="";
    	this.indexSeleccionado=ConstantesBD.codigoNuncaValido;
    	this.currentPageNumber=1;
    	this.offset=0;
	}
	
	
	public void reset_eliminados ()
	{
		this.asociosUvrTipoSalaEliminadosMap=new HashMap();
		this.asociosUvrTipoSalaEliminadosMap.put("numRegistros", "0");
		
	}
	/**
	 * Resetea los atributos del form
	 */
	public void reset()
	{
		this.asociosUvrTipoSalaMap=new HashMap();
		this.asociosUvrTipoSalaMap.put("numRegistros", "0");
		/////////////////////////////////////////////////////////////////////////////
		// modificado por tarea 1278
		this.asociosUvrTipoSalaMapClone = new HashMap ();
		this.asociosUvrTipoSalaMapClone.put("numRegistros", 0);
		/////////////////////////////////////////////////////////////////////////////
		
		
		this.asociosUvrTipoSalaEliminadosMap=new HashMap();
		this.asociosUvrTipoSalaEliminadosMap.put("numRegistros", "0");
		
		this.asociosUvrDetalleMap=new HashMap();
		this.asociosUvrDetalleMap.put("numRegistros", "0");
		
		this.asociosUvrDetalleEliminadosMap=new HashMap();
		this.asociosUvrDetalleEliminadosMap.put("numRegistros", "0");
		
		this.vigenciasConvenioMap=new HashMap();
		this.vigenciasConvenioMap.put("numRegistros", "0");
		
		this.vigenciasConvenioEliminadosMap=new HashMap();
		this.vigenciasConvenioEliminadosMap.put("numRegistros", "0");
	
		
		this.linkSiguiente="";
    	this.indiceEliminado=ConstantesBD.codigoNuncaValido;
    	this.patronOrdenar="";
    	this.ultimoPatron="";
    	
    	this.esquemaTarifario=ConstantesBD.codigoNuncaValido;
    	this.convenio=ConstantesBD.codigoNuncaValido;
    	this.codigoAsocioUvr=ConstantesBD.codigoNuncaValido;
    	this.tipoAsocio=ConstantesBD.codigoNuncaValido;
    	this.tipoSala=ConstantesBD.codigoNuncaValido;
    	this.tipoLiquidacion=ConstantesBD.codigoNuncaValido;
    	this.tipoServicio="";
    	this.tipoAnestesia=ConstantesBD.codigoNuncaValido;
    	this.indexSeleccionado=ConstantesBD.codigoNuncaValido;
    	this.ocupacion="";
    	this.especialidad="";
    	this.tipoEspecialista="";
    	this.nombreTipoAsocio="";
    	this.nombreTipoSala="";
    	
    	this.convenios=new ArrayList();
	    this.tiposServicio=new ArrayList();
	    this.tiposLiquidacion=new ArrayList();
	    this.esquemasTatifarios=new ArrayList();
	    this.tiposAsocio=new ArrayList();
	    this.tiposSala=new ArrayList();
	    this.ocupacionesMedicas=new ArrayList();
	    this.tiposAnestesia=new ArrayList();
	    this.especialidades=new ArrayList();
		this.tarifariosOficiales=new ArrayList();
		
		this.esConsulta=ConstantesBD.acronimoNo;
		
		
		this.cantidadesquematarifario = "";
		
		this.tiposLiq = new ArrayList<HashMap<String,Object>>();
		
		this.index="";
	}
	
	public String getIndex() {
		return index;
	}


	public void setIndex(String index) {
		this.index = index;
	}


	public ActionErrors validate(ActionMapping mapping,HttpServletRequest request)
	{
		ActionErrors errores=new ActionErrors();
		if(this.estado.equals("guardarXTipoAsocio"))
		{
			int numReg=Utilidades.convertirAEntero(this.asociosUvrTipoSalaMap.get("numRegistros")+"");
			for(int i=0;i<numReg;i++)
			{
				if(Utilidades.convertirAEntero(this.asociosUvrTipoSalaMap.get("codigotipoasocio_"+i)+"")<0)
				{
					errores.add("codigo",new ActionMessage("errors.required","El codigo del Tipo de Asocio "+(i+1)));
				}
				
				if(!UtilidadCadena.noEsVacio(this.asociosUvrTipoSalaMap.get("liquidarpor_"+i)+""))
				{
					errores.add("codigo",new ActionMessage("errors.required","Liquidar por "+(i+1)));
				}
				
				for(int j=0;j<i;j++)
				{
					if(this.asociosUvrTipoSalaMap.get("codigotipoasocio_"+i).equals(this.asociosUvrTipoSalaMap.get("codigotipoasocio_"+j))&&this.asociosUvrTipoSalaMap.get("codigotiposala_"+i).equals(this.asociosUvrTipoSalaMap.get("codigotiposala_"+j)))
					{
						errores.add("", new ActionMessage("errors.yaExiste","La combinación Tipo Asocio "+this.asociosUvrTipoSalaMap.get("codigotipoasocio_"+i)+" y Tipo Sala "+this.asociosUvrTipoSalaMap.get("codigotiposala_"+i)));
					}
				}
			}
		}
		else if(this.estado.equals("guardarVigencias"))
	    {
	    	for(int a=0;a<Utilidades.convertirAEntero(this.vigenciasConvenioMap.get("numRegistros")+"");a++)
	    	{
	    		if(UtilidadTexto.isEmpty(this.vigenciasConvenioMap.get("fechainicial_"+a).toString().trim()))
	    		{
	    			errores.add("fechainicial", new ActionMessage("errors.required","La Fecha inicial del registro "+(a+1)));
	    		}
	    		if(UtilidadTexto.isEmpty(this.vigenciasConvenioMap.get("fechafinal_"+a).toString().trim()))
	    		{
	    			errores.add("fechafinal", new ActionMessage("errors.required","La Fecha final del registro "+(a+1)));
	    		}
	    		if(!UtilidadTexto.isEmpty(this.vigenciasConvenioMap.get("fechainicial_"+a).toString().trim())&&!UtilidadTexto.isEmpty(this.vigenciasConvenioMap.get("fechafinal_"+a).toString().trim()))
	    		{
		    		if(UtilidadFecha.esFechaMenorIgualQueOtraReferencia(UtilidadFecha.conversionFormatoFechaAAp(this.vigenciasConvenioMap.get("fechafinal_"+a)+""), UtilidadFecha.conversionFormatoFechaAAp(this.vigenciasConvenioMap.get("fechainicial_"+a)+"")))
		    		{
		    			errores.add("fechafinal", new ActionMessage("errors.debeSerNumeroMayor", "La Fecha Final debe ser Mayor a la Inicial ",UtilidadFecha.conversionFormatoFechaAAp(this.vigenciasConvenioMap.get("fechainicial_"+a)+"")));
		    		}
	    		}
	    		if(this.vigenciasConvenioMap.get("estabd_"+a).equals(ConstantesBD.acronimoNo))
	    		{
	    			if(!UtilidadTexto.isEmpty(this.vigenciasConvenioMap.get("fechainicial_"+a).toString().trim())&&!UtilidadTexto.isEmpty(this.vigenciasConvenioMap.get("fechafinal_"+a).toString().trim()))
		    		{
			    		if(UtilidadFecha.esFechaMenorQueOtraReferencia(UtilidadFecha.conversionFormatoFechaAAp(this.vigenciasConvenioMap.get("fechafinal_"+a)+""), UtilidadFecha.conversionFormatoFechaAAp(UtilidadFecha.getFechaActual())))
			    		{
			    			errores.add("fechafinal", new ActionMessage("errors.debeSerNumeroMayor", "La Fecha Final debe ser Mayor a la Fecha del Sistema ",UtilidadFecha.conversionFormatoFechaAAp(UtilidadFecha.getFechaActual())));
			    		}
		    		}
	    		}
	    		for(int m=a;m<Utilidades.convertirAEntero(this.vigenciasConvenioMap.get("numRegistros")+"");m++)
	    		{
	    			if(m>a&&UtilidadFecha.existeTraslapeEntreFechas(this.vigenciasConvenioMap.get("fechainicial_"+a)+"", this.vigenciasConvenioMap.get("fechafinal_"+a)+"", this.vigenciasConvenioMap.get("fechainicial_"+m)+"", this.vigenciasConvenioMap.get("fechafinal_"+m)+""))
	    			{
	    				errores.add("", new ActionMessage("error.rangoFechasInvalido",this.vigenciasConvenioMap.get("fechainicial_"+a),this.vigenciasConvenioMap.get("fechafinal_"+a)+" del registro "+(a+1),this.vigenciasConvenioMap.get("fechainicial_"+m),this.vigenciasConvenioMap.get("fechafinal_"+m)+" del registro "+(m+1)));
	    				m=Utilidades.convertirAEntero(this.vigenciasConvenioMap.get("numRegistros")+"");
	    			}
	    			
	    		}
	    	}
	    }
		else if(this.estado.equals("guardarDetalle"))
		{
			for(int i=0;i<Utilidades.convertirAEntero(this.asociosUvrDetalleMap.get("numRegistros")+"");i++)
			{
				int rango1=Utilidades.convertirAEntero(this.asociosUvrDetalleMap.get("rango1_"+i)+"");
				int rango2=Utilidades.convertirAEntero(this.asociosUvrDetalleMap.get("rango2_"+i)+"");
				if(rango1>rango2)
				{
					errores.add("", new ActionMessage("errors.MenorIgualQue", "Rango Inicial="+rango1, "Rango Final="+rango2+" en el registro "+(i+1)));
				}
				else
				{
					for(int j=0;j<Utilidades.convertirAEntero(this.asociosUvrDetalleMap.get("numRegistros")+"");j++)
					{
						if(i!=j)
						{
							if(this.asociosUvrDetalleMap.get("codigotiposervicio_"+i).equals(this.asociosUvrDetalleMap.get("codigotiposervicio_"+j))&&this.asociosUvrDetalleMap.get("codigotipoanestesia_"+i).equals(this.asociosUvrDetalleMap.get("codigotipoanestesia_"+j))&&this.asociosUvrDetalleMap.get("codigoespecialidad_"+i).equals(this.asociosUvrDetalleMap.get("codigoespecialidad_"+j))&&this.asociosUvrDetalleMap.get("codigoocupacion_"+i).equals(this.asociosUvrDetalleMap.get("codigoocupacion_"+j))&&this.asociosUvrDetalleMap.get("codigotipoespecialista_"+i).equals(this.asociosUvrDetalleMap.get("codigotipoespecialista_"+j)))
		            		{
								int rango3=Utilidades.convertirAEntero(this.asociosUvrDetalleMap.get("rango1_"+j)+"");
								int rango4=Utilidades.convertirAEntero(this.asociosUvrDetalleMap.get("rango2_"+j)+"");
								String[] mensaje={"",rango3+"",rango4+" del registro "+(j+1),"",rango1+"",rango2+" del registro "+(i+1)}; 
								if(UtilidadValidacion.hayCruceNumeros(rango1, rango2, rango3, rango4))
									errores.add("descripcion",new ActionMessage("error.rangoGenerico",mensaje));
		            		}
		            	}
					}
				}
				
				if(this.asociosUvrDetalleMap.get("codigotiposervicio_"+i).equals(""))
					errores.add("codigo",new ActionMessage("errors.required","El Tipo Servicio del registro "+(i+1)));
				
				if(this.asociosUvrDetalleMap.get("rango1_"+i).equals(""))
					errores.add("codigo",new ActionMessage("errors.required","El rango inicial del registro "+(i+1)));
				
				if(this.asociosUvrDetalleMap.get("rango2_"+i).equals(""))
					errores.add("codigo",new ActionMessage("errors.required","El rango final del registro "+(i+1)));

				if((this.asociosUvrDetalleMap.get("codigotipoliquidacion_"+i)+"").equals(ConstantesBD.codigoNuncaValido+""))
					errores.add("codigo",new ActionMessage("errors.required","El tipo de liquidacion del registro "+(i+1)));

				////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
				//se evalua que tipo de liquidacion para dfinir cuales datos son requeridos.
				if ((this.asociosUvrDetalleMap.get("codigotipoliquidacion_"+i)+"").equals(ConstantesBD.codigoTipoLiquidacionSoatUnidades+""))
				{
					if((this.asociosUvrDetalleMap.get("unidades_"+i)+"").equals(""))
						errores.add("codigo",new ActionMessage("errors.required","Las Unidades del registro "+(i+1)));
					
					if((this.asociosUvrDetalleMap.get("valor_"+i)+"").equals(""))
						errores.add("codigo",new ActionMessage("errors.required","El valor del registro "+(i+1)));
					
					if((this.asociosUvrDetalleMap.get("valorunidades_"+i)+"").equals(""))
						errores.add("codigo",new ActionMessage("errors.required","El valor unidad del registro "+(i+1)));
				}
				else
					if ((this.asociosUvrDetalleMap.get("codigotipoliquidacion_"+i)+"").equals(ConstantesBD.codigoTipoLiquidacionSoatValor+""))
					{
						if((this.asociosUvrDetalleMap.get("valor_"+i)+"").equals(""))
							errores.add("codigo",new ActionMessage("errors.required","El valor del registro "+(i+1)));
					}
					else
						if ((this.asociosUvrDetalleMap.get("codigotipoliquidacion_"+i)+"").equals(ConstantesBD.codigoTipoLiquidacionValorUnidades+""))
							if((this.asociosUvrDetalleMap.get("unidades_"+i)+"").equals(""))
								errores.add("codigo",new ActionMessage("errors.required","Las Unidades del registro "+(i+1)));
				//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
				
				
				if(((UtilidadTexto.isEmpty(this.asociosUvrDetalleMap.get("valortarifario_"+i+"_"+0)+"")) || (this.asociosUvrDetalleMap.get("valortarifario_"+i+"_"+0)+"").trim().equals("") ))
	                errores.add("El codigo cups requerido", new ActionMessage("errors.required","El campo Código CUPS "));

				for(int j=0;j<Utilidades.convertirAEntero(this.asociosUvrDetalleMap.get("numRegistros")+"");j++)
				{
					if(i!=j)
					{
						if(this.asociosUvrDetalleMap.get("codigotiposervicio_"+i).equals(this.asociosUvrDetalleMap.get("codigotiposervicio_"+j))&&this.asociosUvrDetalleMap.get("codigotipoanestesia_"+i).equals(this.asociosUvrDetalleMap.get("codigotipoanestesia_"+j))&&this.asociosUvrDetalleMap.get("codigoespecialidad_"+i).equals(this.asociosUvrDetalleMap.get("codigoespecialidad_"+j))&&this.asociosUvrDetalleMap.get("codigoocupacion_"+i).equals(this.asociosUvrDetalleMap.get("codigoocupacion_"+j))&&this.asociosUvrDetalleMap.get("codigotipoespecialista_"+i).equals(this.asociosUvrDetalleMap.get("codigotipoespecialista_"+j))&&this.asociosUvrDetalleMap.get("rango1_"+i).equals(this.asociosUvrDetalleMap.get("rango1_"+j))&&this.asociosUvrDetalleMap.get("rango2_"+i).equals(this.asociosUvrDetalleMap.get("rango2_"+j)))
	            		{
	            			errores.add("actualización Grupos", new ActionMessage("error.salasCirugia.yaExisteUniqueTipoServicio","los datos de grupos quirúrgicos que se encuentran asociados a los esquemas tarifarios basados en SOAT (Transacción)"));
	            		}
					}
				}
			}
		}
		return errores;
	}

	/**
	 * @return the asociosUvrDetalleEliminadosMap
	 */
	public HashMap getAsociosUvrDetalleEliminadosMap() {
		return asociosUvrDetalleEliminadosMap;
	}

	/**
	 * @param asociosUvrDetalleEliminadosMap the asociosUvrDetalleEliminadosMap to set
	 */
	public void setAsociosUvrDetalleEliminadosMap(
			HashMap asociosUvrDetalleEliminadosMap) {
		this.asociosUvrDetalleEliminadosMap = asociosUvrDetalleEliminadosMap;
	}

	/**
	 * @return the asociosUvrDetalleMap
	 */
	public HashMap getAsociosUvrDetalleMap() {
		return asociosUvrDetalleMap;
	}

	/**
	 * @param asociosUvrDetalleMap the asociosUvrDetalleMap to set
	 */
	public void setAsociosUvrDetalleMap(HashMap asociosUvrDetalleMap) {
		this.asociosUvrDetalleMap = asociosUvrDetalleMap;
	}

	/**
	 * @return the asociosUvrTipoSalaMap
	 */
	public HashMap getAsociosUvrTipoSalaMap() {
		return asociosUvrTipoSalaMap;
	}

	/**
	 * @param asociosUvrTipoSalaMap the asociosUvrTipoSalaMap to set
	 */
	public void setAsociosUvrTipoSalaMap(HashMap asociosUvrTipoSalaMap) {
		this.asociosUvrTipoSalaMap = asociosUvrTipoSalaMap;
	}

	/**
	 * @return the codigoAsocioUvr
	 */
	public int getCodigoAsocioUvr() {
		return codigoAsocioUvr;
	}

	/**
	 * @param codigoAsocioUvr the codigoAsocioUvr to set
	 */
	public void setCodigoAsocioUvr(int codigoAsocioUvr) {
		this.codigoAsocioUvr = codigoAsocioUvr;
	}

	/**
	 * @return the convenio
	 */
	public int getConvenio() {
		return convenio;
	}

	/**
	 * @param convenio the convenio to set
	 */
	public void setConvenio(int convenio) {
		this.convenio = convenio;
	}

	/**
	 * @return the esquemaTarifario
	 */
	public int getEsquemaTarifario() {
		return esquemaTarifario;
	}

	/**
	 * @param esquemaTarifario the esquemaTarifario to set
	 */
	public void setEsquemaTarifario(int esquemaTarifario) {
		this.esquemaTarifario = esquemaTarifario;
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
	 * @return the indiceEliminado
	 */
	public int getIndiceEliminado() {
		return indiceEliminado;
	}

	/**
	 * @param indiceEliminado the indiceEliminado to set
	 */
	public void setIndiceEliminado(int indiceEliminado) {
		this.indiceEliminado = indiceEliminado;
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


	/**
	 * @return the mensaje
	 */
	public ResultadoBoolean getMensaje() {
		return mensaje;
	}

	/**
	 * @param mensaje the mensaje to set
	 */
	public void setMensaje(ResultadoBoolean mensaje) {
		this.mensaje = mensaje;
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
	 * @return the tipoAsocio
	 */
	public int getTipoAsocio() {
		return tipoAsocio;
	}

	/**
	 * @param tipoAsocio the tipoAsocio to set
	 */
	public void setTipoAsocio(int tipoAsocio) {
		this.tipoAsocio = tipoAsocio;
	}

	/**
	 * @return the ultimoPatron
	 */
	public String getUltimoPatron() {
		return ultimoPatron;
	}

	/**
	 * @param ultimoPatron the ultimoPatron to set
	 */
	public void setUltimoPatron(String ultimoPatron) {
		this.ultimoPatron = ultimoPatron;
	}

	/**
	 * @return the vigenciasConvenioMap
	 */
	public HashMap getVigenciasConvenioMap() {
		return vigenciasConvenioMap;
	}

	/**
	 * @param vigenciasConvenioMap the vigenciasConvenioMap to set
	 */
	public void setVigenciasConvenioMap(HashMap vigenciasConvenioMap) {
		this.vigenciasConvenioMap = vigenciasConvenioMap;
	}

	/**
	 * @return the convenios
	 */
	public ArrayList getConvenios() {
		return convenios;
	}

	/**
	 * @param convenios the convenios to set
	 */
	public void setConvenios(ArrayList convenios) {
		this.convenios = convenios;
	}

	/**
	 * @return the tiposLiquidacion
	 */
	public ArrayList getTiposLiquidacion() {
		return tiposLiquidacion;
	}

	/**
	 * @param tiposLiquidacion the tiposLiquidacion to set
	 */
	public void setTiposLiquidacion(ArrayList tiposLiquidacion) {
		this.tiposLiquidacion = tiposLiquidacion;
	}

	/**
	 * @return the tiposServicio
	 */
	public ArrayList getTiposServicio() {
		return tiposServicio;
	}

	/**
	 * @param tiposServicio the tiposServicio to set
	 */
	public void setTiposServicio(ArrayList tiposServicio) {
		this.tiposServicio = tiposServicio;
	}

	/**
	 * @return the esquemasTatifarios
	 */
	public ArrayList getEsquemasTatifarios() {
		return esquemasTatifarios;
	}

	/**
	 * @param esquemasTatifarios the esquemasTatifarios to set
	 */
	public void setEsquemasTatifarios(ArrayList esquemasTatifarios) {
		this.esquemasTatifarios = esquemasTatifarios;
	}

	/**
	 * @return the tiposAsocio
	 */
	public ArrayList getTiposAsocio() {
		return tiposAsocio;
	}

	/**
	 * @param tiposAsocio the tiposAsocio to set
	 */
	public void setTiposAsocio(ArrayList tiposAsocio) {
		this.tiposAsocio = tiposAsocio;
	}

	/**
	 * @return the tiposSala
	 */
	public ArrayList getTiposSala() {
		return tiposSala;
	}

	/**
	 * @param tiposSala the tiposSala to set
	 */
	public void setTiposSala(ArrayList tiposSala) {
		this.tiposSala = tiposSala;
	}

	/**
	 * @return the tipoLiquidacion
	 */
	public int getTipoLiquidacion() {
		return tipoLiquidacion;
	}

	/**
	 * @param tipoLiquidacion the tipoLiquidacion to set
	 */
	public void setTipoLiquidacion(int tipoLiquidacion) {
		this.tipoLiquidacion = tipoLiquidacion;
	}

	/**
	 * @return the tipoSala
	 */
	public int getTipoSala() {
		return tipoSala;
	}

	/**
	 * @param tipoSala the tipoSala to set
	 */
	public void setTipoSala(int tipoSala) {
		this.tipoSala = tipoSala;
	}

	/**
	 * @return the tipoAnestesia
	 */
	public int getTipoAnestesia() {
		return tipoAnestesia;
	}

	/**
	 * @param tipoAnestesia the tipoAnestesia to set
	 */
	public void setTipoAnestesia(int tipoAnestesia) {
		this.tipoAnestesia = tipoAnestesia;
	}

	/**
	 * @return the ocupacionesMedicas
	 */
	public ArrayList getOcupacionesMedicas() {
		return ocupacionesMedicas;
	}

	/**
	 * @param ocupacionesMedicas the ocupacionesMedicas to set
	 */
	public void setOcupacionesMedicas(ArrayList ocupacionesMedicas) {
		this.ocupacionesMedicas = ocupacionesMedicas;
	}

	/**
	 * @return the tipoEspecialista
	 */
	public String getTipoEspecialista() {
		return tipoEspecialista;
	}

	/**
	 * @param tipoEspecialista the tipoEspecialista to set
	 */
	public void setTipoEspecialista(String tipoEspecialista) {
		this.tipoEspecialista = tipoEspecialista;
	}
	
	public Object getAsociosUvrTipoSalaMap(String key)
	{
		return asociosUvrTipoSalaMap.get(key);
	}
	
	public void setAsociosUvrTipoSalaMap(String key,Object value)
	{
		this.asociosUvrTipoSalaMap.put(key,value);
	}
	
	public Object getAsociosUvrDetalleMap(String key)
	{
		return asociosUvrDetalleMap.get(key);
	}
	
	public void setAsociosUvrDetalleMap(String key,Object value)
	{
		this.asociosUvrDetalleMap.put(key,value);
	}

	/**
	 * @return the especialidades
	 */
	public ArrayList getEspecialidades() {
		return especialidades;
	}

	/**
	 * @param especialidades the especialidades to set
	 */
	public void setEspecialidades(ArrayList especialidades) {
		this.especialidades = especialidades;
	}

	/**
	 * @return the tarifariosOficiales
	 */
	public ArrayList getTarifariosOficiales() {
		return tarifariosOficiales;
	}

	/**
	 * @param tarifariosOficiales the tarifariosOficiales to set
	 */
	public void setTarifariosOficiales(ArrayList tarifariosOficiales) {
		this.tarifariosOficiales = tarifariosOficiales;
	}

	/**
	 * @return the especialidad
	 */
	public String getEspecialidad() {
		return especialidad;
	}

	/**
	 * @param especialidad the especialidad to set
	 */
	public void setEspecialidad(String especialidad) {
		this.especialidad = especialidad;
	}

	/**
	 * @return the ocupacion
	 */
	public String getOcupacion() {
		return ocupacion;
	}

	/**
	 * @param ocupacion the ocupacion to set
	 */
	public void setOcupacion(String ocupacion) {
		this.ocupacion = ocupacion;
	}

	/**
	 * @return the tiposAnestesia
	 */
	public ArrayList getTiposAnestesia() {
		return tiposAnestesia;
	}

	/**
	 * @param tiposAnestesia the tiposAnestesia to set
	 */
	public void setTiposAnestesia(ArrayList tiposAnestesia) {
		this.tiposAnestesia = tiposAnestesia;
	}

	/**
	 * @return the asociosUvrTipoSalaEliminadosMap
	 */
	public HashMap getAsociosUvrTipoSalaEliminadosMap() {
		return asociosUvrTipoSalaEliminadosMap;
	}

	/**
	 * @param asociosUvrTipoSalaEliminadosMap the asociosUvrTipoSalaEliminadosMap to set
	 */
	public void setAsociosUvrTipoSalaEliminadosMap(
			HashMap asociosUvrTipoSalaEliminadosMap) {
		this.asociosUvrTipoSalaEliminadosMap = asociosUvrTipoSalaEliminadosMap;
	}

	/**
	 * @return the indexSeleccionado
	 */
	public int getIndexSeleccionado() {
		return indexSeleccionado;
	}

	/**
	 * @param indexSeleccionado the indexSeleccionado to set
	 */
	public void setIndexSeleccionado(int indexSeleccionado) {
		this.indexSeleccionado = indexSeleccionado;
	}

	/**
	 * @return the nombreTipoAsocio
	 */
	public String getNombreTipoAsocio() {
		return nombreTipoAsocio;
	}

	/**
	 * @param nombreTipoAsocio the nombreTipoAsocio to set
	 */
	public void setNombreTipoAsocio(String nombreTipoAsocio) {
		this.nombreTipoAsocio = nombreTipoAsocio;
	}

	/**
	 * @return the nombreTipoSala
	 */
	public String getNombreTipoSala() {
		return nombreTipoSala;
	}

	/**
	 * @param nombreTipoSala the nombreTipoSala to set
	 */
	public void setNombreTipoSala(String nombreTipoSala) {
		this.nombreTipoSala = nombreTipoSala;
	}
	
	public Object getVigenciasConvenioMap(String key)
	{
		return vigenciasConvenioMap.get(key);
	}
	
	public void setVigenciasConvenioMap(String key,Object value)
	{
		this.vigenciasConvenioMap.put(key,value);
	}

	/**
	 * @return the vigenciasConvenioEliminadosMap
	 */
	public HashMap getVigenciasConvenioEliminadosMap() {
		return vigenciasConvenioEliminadosMap;
	}

	/**
	 * @param vigenciasConvenioEliminadosMap the vigenciasConvenioEliminadosMap to set
	 */
	public void setVigenciasConvenioEliminadosMap(
			HashMap vigenciasConvenioEliminadosMap) {
		this.vigenciasConvenioEliminadosMap = vigenciasConvenioEliminadosMap;
	}

	/**
	 * @return the esConsulta
	 */
	public String getEsConsulta() {
		return esConsulta;
	}

	/**
	 * @param esConsulta the esConsulta to set
	 */
	public void setEsConsulta(String esConsulta) {
		this.esConsulta = esConsulta;
	}

	/**
	 * @return the tipoServicio
	 */
	public String getTipoServicio() {
		return tipoServicio;
	}

	/**
	 * @param tipoServicio the tipoServicio to set
	 */
	public void setTipoServicio(String tipoServicio) {
		this.tipoServicio = tipoServicio;
	}

	
	
	
	
	///**************************************** ADICIONADO PARA OBTENER LA CANTIDAD DEL ESQUEMA SELECCIONADO
	
	
	/**
	 * @return the cantidadesquematarifario
	 */
	public String getCantidadesquematarifario() {
		return cantidadesquematarifario;
	}

	/**
	 * @param cantidadesquematarifario the cantidadesquematarifario to set
	 */
	public void setCantidadesquematarifario(String cantidadesquematarifario) {
		this.cantidadesquematarifario = cantidadesquematarifario;
	}

	public HashMap getAsociosUvrTipoSalaMapClone() {
		return asociosUvrTipoSalaMapClone;
	}

	public void setAsociosUvrTipoSalaMapClone(HashMap asociosUvrTipoSalaMapClone) {
		this.asociosUvrTipoSalaMapClone = asociosUvrTipoSalaMapClone;
	}

	public Object getAsociosUvrTipoSalaMapClone(String key) {
		return asociosUvrTipoSalaMapClone.get(key);
	}

	public void setAsociosUvrTipoSalaMapClone(String key, Object value) {
		this.asociosUvrTipoSalaMapClone.put(key, value);
	}
	
//--------------------------------------------------------------------------
	public ArrayList<HashMap<String, Object>> getTiposLiq() {
		return tiposLiq;
	}
	public void setTiposLiq(ArrayList<HashMap<String, Object>> tiposLiq) {
		this.tiposLiq = tiposLiq;
	}
	//------------------------------------------------------------------------
	
	//--------------------------------------------------------
	
	   public int getCurrentPageNumber() {
			return currentPageNumber;
		}


		public void setCurrentPageNumber(int currentPageNumber) {
			this.currentPageNumber = currentPageNumber;
		}


		public int getOffset() {
			return offset;
		}


		public void setOffset(int offset) {
			this.offset = offset;
		}
		//----------------------------
	
}
