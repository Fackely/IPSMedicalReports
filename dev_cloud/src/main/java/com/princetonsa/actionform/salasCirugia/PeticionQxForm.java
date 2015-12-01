/*
 * Creado el 27/10/2005
 * Juan David Ramírez López
 */
package com.princetonsa.actionform.salasCirugia; 

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;

import com.servinte.axioma.dto.manejoPaciente.DtoValidacionGeneracionAutorizacionCapitada;
import com.servinte.axioma.dto.ordenes.ProfesionalEspecialidadesDto;

public class PeticionQxForm extends ValidatorForm
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1911074468044249516L;

	/**
	 * Estado para el manejo del flujo de la funcionalidad
	 */
	private String estado;

	/**
	 * Campos para manejar el Origen desde donde
	 * se esta llamando la consulta de peticion o el listado de peticiones
	 * de un paciente.  
	 */
	
	public final static String origenPreanestesia = "preanestesia";
	public final static String origenModificar = "modificar";
	public final static String origenGenerar = "generar";

	/**
	 * Atributo que indica desde que funcionalidad se está accediendo al form
	 */
	private String origen;
	
	
	private boolean urgente;
	/**
	 * Para almacenar el estado anterior
	 */
	private String estadoAnterior;
	
	/**
	 * Número de filas existentes en el hashmap (este sirve para articulos)
	 */
	private int numeroFilasMateriales;
	
		/**
	 * Sexo del paciente
	 */
	private int sexo;
	
	
	//-----------------------------------Campos de la Consulta de Peticiones 
	/** 
	 * Mapa para manejar la consulta general y la consulta
	 * de peticiones por paciente  
	 */
	private HashMap mapaConsultaPeticiones;
	
	/**
	 * Mapas para almacenar la informacion del detalle de la peticion
	 */
	private HashMap mapaPeticionEncabezado;
	
	/**
	 * Mapa para almacenar la informacion de los profesionales asociados 
	 * a la peticion.
	 */
	private HashMap mapaPeticionProfesionales;
	
	/**
	 * Mapa para almacenar la informacion de los servicios asociados 
	 * a la peticion.
	 */
	private HashMap mapaPeticionServicios;
	
	/**
	 * Mapa para almacenar la informacion de los materiales asociados 
	 * a la peticion.
	 */
	private HashMap mapaPeticionMateriales;
	
	/**
	 * 
	 * */
	private HashMap mapaPeticionOtrosMateriales;


	private int ingresoId;
	/**
	 * Numero del Servicio inicial (consecutivo)
	 */
	private int nroIniServicio;

	/**
	 * Numero del Servicio Final (consecutivo)
	 */
	private int nroFinServicio;
	
	
	/**
	 * Fecha inicial peticion del servicio
	 */
	private String fechaIniPeticion;
	
	/**
	 * Fecha final de peticion del servicio 
	 */
	private String fechaFinPeticion;
	
	/**
	 * Fecha Inicial de Cirugia 
	 */
	private String fechaIniCirugia;

	/**
	 * Fecha Inicial de Cirugia 
	 */
	private String fechaFinCirugia;
	
	/**
	 * Medico que solicita la peticion 
	 */
	private int profesional;
	
	/**
	 * Estado de la peticion
	 */
	private int estadoPeticion;
	
	
	/**
	 * Atributo que me indica si la persona que se desea ingresar ya se encuentra en el sistema
	 */
	private boolean existePersonaEnSistema;
	
    /**
     * almacena el indice por el cual 
     * se va ordenar el HashMap
     */
    private String patronOrdenar;
    
    /**
     * almacena el ultimo indice por el 
     * cual se ordeno el HashMap
     */
    private String ultimoPatron;

    /**
     * el numero de registros por pager
     */
    private int maxPageItems;
    /**
     * Variable para manejar el paginador de la consulta de pèticiones
     */
    private int index;
	private int pager;
    private int offset;
    private String linkSiguiente;
    
    /**
     * Atributo para controlar la impresión de la autorización en el resumen de la petición
     */
    private boolean imprimirAutorizacion;
    
    
    /**
     * Atributo que representa el Dto con la lista de autorizaciones a imprimir
     */
    private DtoValidacionGeneracionAutorizacionCapitada validacionesAutorizaciones = new DtoValidacionGeneracionAutorizacionCapitada();
    
    
    /**	 * lista que contiene los nombres de los reportes de las autorzaciones **/
	private ArrayList<String> listaNombresReportes;
    
	/**
	 * Variable para almacenar el Centro de atencion.
	 */
    private int centroAtencion; 
    
    //------------------------------------------------------------------------------------------
    
	/**
	 * Mapa que almacena los tipos de anestesia
	 */
	private ArrayList<HashMap<String, Object>> tiposAnestesia = new ArrayList<HashMap<String,Object>>();
	/**
	 * 
	 */
	private String manejoProgramacionSalas;
	
	/**
	 * Nombre del archivo generado
	 * */
    private String nombreArchivoGenerado;
    /**
	 * Nombre copia del archivo generado
	 * */
    private String nombreArchivoCopia;
    
    /** 
	 * Mapa que contiene los datos iniciales de la petición
	 */
	private HashMap mapaDetallePeticionInicial;
    
	/**
	 * Lista almacena el codigo de los profesionales y las especialidades
	 */
	private ArrayList<ProfesionalEspecialidadesDto> profesionalEspecialidades;
	
	/**
	 * Método para limpiar la clase
	 */
	public void reset()
	{
		this.manejoProgramacionSalas="";
		

		this.nroIniServicio = 0;
		this.nroFinServicio = 0;
		this.fechaIniPeticion = "";
		this.fechaFinPeticion = "";
		this.fechaIniCirugia = "";
		this.fechaFinCirugia = ""; 
		this.profesional = -1;
		this.estadoPeticion = -1;
		
		this.mapaConsultaPeticiones = new HashMap();
		this.mapaPeticionOtrosMateriales = new HashMap();
		this.mapaPeticionOtrosMateriales.put("numRegOtrosMat","0");
		
		// Conservar el LOG desde el cargar
		if(this.mapaPeticionEncabezado!=null)
		{
			String log=(String)this.mapaPeticionEncabezado.get("log");
			this.mapaPeticionEncabezado  = new HashMap();
			this.mapaPeticionEncabezado.put("log", log);
		}
		else
		{
			this.mapaPeticionEncabezado = new HashMap();
		}
		this.mapaPeticionProfesionales = new HashMap();
		this.mapaPeticionServicios = new HashMap();
		this.mapaPeticionMateriales = new HashMap();
		patronOrdenar = "";
		ultimoPatron = "";
		
		
		maxPageItems = 0;
		
		/*
		 * En la generación aun no se sabe el número de la petición
		 */
		this.mapaPeticionEncabezado.put("numeroPeticion", "");
		this.mapaPeticionEncabezado.put("codigoEstadoPeticion", "0");
		
		/*
		 * El número de profesionales inicialmente es 0 (El solicitante se postula al iniciar)
		 * La postulación se hace a travez de un javascript en el JSP
		 */
	    this.mapaPeticionProfesionales.put("numeroProfesionales", "0");
		
	    /*
	     * El número de materiales inicialmente es 0, lo mismo el numero de otros materiales
	     */
		this.numeroFilasMateriales=0;
		this.mapaPeticionMateriales.put("numeroOtrosMateriales", "0");
		
		/*
		 * Inicialmente no tiene códigos de los servicios insertados
		 */
		this.mapaPeticionServicios.put("codigosServiciosInsertados","");
		this.mapaPeticionServicios.put("numeroFilasMapaServicios", "0");
		
		this.existePersonaEnSistema=false;

		this.centroAtencion = 0;
		
		this.tiposAnestesia = new ArrayList<HashMap<String,Object>>();
		this.ingresoId=ConstantesBD.codigoNuncaValido;
		
		this.nombreArchivoGenerado="";
		this.nombreArchivoCopia="";
		
		this.profesionalEspecialidades = new ArrayList<ProfesionalEspecialidadesDto>();
	}

	
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
		ActionErrors errores= new ActionErrors();
		
		//////////////////////////VALIDACIONES ARTICULOS
		if(estado.equals("guardar") || estado.equals("guardarModificar"))
		{
			String valorPropiedad=this.getMapaPeticionEncabezado("duracion");
			if(valorPropiedad!=null && !valorPropiedad.trim().equals(""))
			{
				if(!UtilidadFecha.validacionHora(valorPropiedad).puedoSeguir)
				{
					errores.add("hora", new ActionMessage("errors.formatoHoraInvalido", "Duración Aproximada "+valorPropiedad));
				}
				else if(valorPropiedad.compareTo("00:00")<=0)
				{
					errores.add("hora", new ActionMessage("errors.horaDebeSerMayor", "Duración Aproximada ", "00:00"));
				}
			}
				
			valorPropiedad=this.getMapaPeticionEncabezado("fechaEstimada");
			if(valorPropiedad!=null && !valorPropiedad.trim().equals(""))
			{
				if(!UtilidadFecha.validarFecha(valorPropiedad))
				{
					errores.add("fechaEstimada", new ActionMessage("errors.formatoFechaInvalido", "Fecha Estimada Cirugía "+valorPropiedad));
				}
				else if(UtilidadFecha.conversionFormatoFechaABD(valorPropiedad).compareTo(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()))<0)
				{
					errores.add("fechaEstimada", new ActionMessage("errors.fechaAnteriorIgualActual", "Fecha Estimada Cirugía ", "Actual"));
				}
			}
			valorPropiedad=this.getMapaPeticionEncabezado("solicitante");
			if(valorPropiedad!=null && valorPropiedad.trim().equals("noSeleccionado"))
			{
				errores.add("solicitante", new ActionMessage("errors.required", "Profesional que Solicita"));
			}
			valorPropiedad=this.getMapaPeticionEncabezado("tipoPaciente");
			
			//Validacion de los servicios de la peticion
			int numServicios = 0;
			for(int i=0;i<Utilidades.convertirAEntero(this.mapaPeticionServicios.get("numeroFilasMapaServicios")+"", true);i++)
				if(!UtilidadTexto.getBoolean(this.mapaPeticionServicios.get("fueEliminadoServicio_"+i)+""))
					numServicios++;
			
			if(numServicios<=0)
				errores.add("", new ActionMessage("errors.minimoCampos2","la selección de un servicio","la generación de la petición"));
			
			for(int i=0; i<this.getNumeroFilasMateriales(); i++)
			{
				String codigoArticuloTemp="";

				if(!(this.getMapaPeticionMateriales("fueEliminadoArticulo_"+i)+"").equals("true"))
				{    
					try
					{
						codigoArticuloTemp= this.getMapaPeticionMateriales("codigoArticulo_"+i)+"";
						Integer.parseInt(this.getMapaPeticionMateriales("cantidadDespachadaArticulo_"+i)+"");
						if(Integer.parseInt(this.getMapaPeticionMateriales("cantidadDespachadaArticulo_"+i)+"")==0)
						{
							errores.add("Campo cantidad", new ActionMessage("errors.integerMayorQue","El campo Cantidad para el Artículo "+codigoArticuloTemp, "0"));
						}
					}
					catch(NullPointerException ne)
					{
						errores.add("Campo Cantidad vacio", new ActionMessage("errors.invalid","El campo Cantidad para el Artículo "+codigoArticuloTemp));
					}
					catch(NumberFormatException nfe)
					{
						errores.add("Campo Cantidad vacio", new ActionMessage("errors.invalid","El campo Cantidad para el Artículo "+codigoArticuloTemp));
					}
				}
			}
			
			int numeroProfesionales=Integer.parseInt(this.mapaPeticionProfesionales.get("numeroProfesionales")+"");
			Vector codigos=new Vector();
			for(int i=0; i<numeroProfesionales;i++)
			{
				String valor=this.mapaPeticionProfesionales.get("profesional_"+i)+"";
				int tipoProfesional=Integer.parseInt(this.mapaPeticionProfesionales.get("tipo_participante_"+i)+"");
				String especialidadProf=this.mapaPeticionProfesionales.get("especialidades_"+i)+"";
				
				if (especialidadProf==null || especialidadProf.equals("null") || especialidadProf.trim().equals(""))
				{
					this.mapaPeticionProfesionales.put("especialidades_"+i, "-1");
				}
				
				if(codigos.contains(valor))
				{
					errores.add("Error Profesional Repetido", new ActionMessage("error.peticionesCirugias.profesionalRepetido"));
					break;
				}
				//Se verifica que ese haya seleccionado el tipo de profesional participante
				if(tipoProfesional==-1)
				{
					errores.add("tipo profesional", new ActionMessage("errors.required", "El Tipo de Profesional"));
					break;
				}
				codigos.add(valor);
			}
			
			
			
			this.mapaPeticionMateriales.put("numeroMateriales", new Integer(this.getNumeroFilasMateriales()));
		}
		else if(estado.equals("empezarContinuarServicio"))
		{
			int tempoNumServicios=Integer.parseInt(this.mapaPeticionServicios.get("numeroFilasMapaServicios")+"");
			this.mapaPeticionServicios.put("numeroFilasMapaServicios", (tempoNumServicios+1)+"");
		}
		return errores;
	}

	/**
	 * @return Retorna estado.
	 */
	public String getEstado()
	{
		return estado;
	}

	/**
	 * @param estado Asigna estado.
	 */
	public void setEstado(String estado)
	{
		this.estado = estado;
	}

	/**
	 * @return Retorna sexo.
	 */
	public int getSexo()
	{
		return sexo;
	}

	/**
	 * @param sexo Asigna sexo.
	 */
	public void setSexo(int sexo)
	{
		this.sexo = sexo;
	}

	/**
	 * @return Retorna mapaConsultaPeticiones.
	 */
	public HashMap getMapaConsultaPeticiones()
	{
		return mapaConsultaPeticiones;
	}

	/**
	 * @param mapaConsultaPeticiones Asigna mapaConsultaPeticiones.
	 */
	public void setMapaConsultaPeticiones(HashMap mapaConsultaPeticiones)
	{
		this.mapaConsultaPeticiones = mapaConsultaPeticiones;
	}
	
	/**
	 * @return Retorna mapaPeticionEncabezado.
	 */
	public HashMap getMapaPeticionEncabezado() {
		return mapaPeticionEncabezado;
	}
	
	/**
	 * Retorna una propiedad especifica del encabezado de la petición
	 * @param key Propiedad deseada
	 * @return String con el valor de la propiedad
	 */
	public String getMapaPeticionEncabezado(String key)
	{
		return (String)mapaPeticionEncabezado.get(key);
	}
	/**
	 * @param Asigna mapaPeticionEncabezado.
	 */
	public void setMapaPeticionEncabezado(HashMap mapaPeticionEncabezado) {
		this.mapaPeticionEncabezado = mapaPeticionEncabezado;
	}
	/**
	 * @return Retorna mapaPeticionServicios.
	 */
	public HashMap getMapaPeticionServicios() {
		return mapaPeticionServicios;
	}
	/**
	 * @param Asigna mapaPeticionServicios.
	 */
	public void setMapaPeticionServicios(HashMap mapaPeticionServicios) {
		this.mapaPeticionServicios = mapaPeticionServicios;
	}
	/**
	 * @return Retorna mapaPeticionMateriales.
	 */
	public HashMap getMapaPeticionMateriales() {
		return mapaPeticionMateriales;
	}
	/**
	 * Retorna una propiedad específica del mapa
	 * @param key id de la Propiedad
	 * @return String con el valor del HashMap
	 */
	public String getMapaPeticionMateriales(String key)
	{
		return (String)mapaPeticionMateriales.get(key);
	}
	/**
	 * @param Asigna mapaPeticionMateriales.
	 */
	public void setMapaPeticionMateriales(HashMap mapaPeticionMateriales) {
		this.mapaPeticionMateriales = mapaPeticionMateriales;
	}
	/**
	 * @return Retorna mapaPeticionProfesionales.
	 */
	public HashMap getMapaPeticionProfesionales() {
		return mapaPeticionProfesionales;
	}
	/**
	 * @param Asigna mapaPeticionProfesionales.
	 */
	public void setMapaPeticionProfesionales(HashMap mapaPeticionProfesionales) {
		this.mapaPeticionProfesionales = mapaPeticionProfesionales;
	}
	/**
	 * @return Retorna estadoPeticion.
	 */
	public int getEstadoPeticion() {
		return estadoPeticion;
	}
	/**
	 * @param Asigna estadoPeticion.
	 */
	public void setEstadoPeticion(int estadoPeticion) {
		this.estadoPeticion = estadoPeticion;
	}
	/**
	 * @return Retorna fechaFinCirugia.
	 */
	public String getFechaFinCirugia() {
		return fechaFinCirugia;
	}
	/**
	 * @param Asigna fechaFinCirugia.
	 */
	public void setFechaFinCirugia(String fechaFinCirugia) {
		this.fechaFinCirugia = fechaFinCirugia;
	}
	/**
	 * @return Retorna fechaFinPeticion.
	 */
	public String getFechaFinPeticion() {
		return fechaFinPeticion;
	}
	/**
	 * @param Asigna fechaFinPeticion.
	 */
	public void setFechaFinPeticion(String fechaFinPeticion) {
		this.fechaFinPeticion = fechaFinPeticion;
	}
	/**
	 * @return Retorna fechaIniCirugia.
	 */
	public String getFechaIniCirugia() {
		return fechaIniCirugia;
	}
	/**
	 * @param Asigna fechaIniCirugia.
	 */
	public void setFechaIniCirugia(String fechaIniCirugia) {
		this.fechaIniCirugia = fechaIniCirugia;
	}
	/**
	 * @return Retorna fechaIniPeticion.
	 */
	public String getFechaIniPeticion() {
		return fechaIniPeticion;
	}
	/**
	 * @param Asigna fechaIniPeticion.
	 */
	public void setFechaIniPeticion(String fechaIniPeticion) {
		this.fechaIniPeticion = fechaIniPeticion;
	}
	/**
	 * @return Retorna nroFinServicio.
	 */
	public int getNroFinServicio() {
		return nroFinServicio;
	}
	/**
	 * @param Asigna nroFinServicio.
	 */
	public void setNroFinServicio(int nroFinServicio) {
		this.nroFinServicio = nroFinServicio;
	}
	/**
	 * @return Retorna nroIniServicio.
	 */
	public int getNroIniServicio() {
		return nroIniServicio;
	}
	/**
	 * @param Asigna nroIniServicio.
	 */
	public void setNroIniServicio(int nroIniServicio) {
		this.nroIniServicio = nroIniServicio;
	}
	/**
	 * @return Retorna profesional.
	 */
	public int getProfesional() {
		return profesional;
	}
	/**
	 * @param Asigna profesional.
	 */
	public void setProfesional(int profesional) {
		this.profesional = profesional;
	}
	/**
	 * @return Retorna estadoAnterior.
	 */
	public String getEstadoAnterior() {
		return estadoAnterior;
	}
	/**
	 * @param Asigna estadoAnterior.
	 */
	public void setEstadoAnterior(String estadoAnterior) {
		this.estadoAnterior = estadoAnterior;
	}
	/**
	 * @return Retorna patronOrdenar.
	 */
	public String getPatronOrdenar() {
		return patronOrdenar;
	}
	/**
	 * @param Asigna patronOrdenar.
	 */
	public void setPatronOrdenar(String patronOrdenar) {
		this.patronOrdenar = patronOrdenar;
	}
	/**
	 * @return Retorna ultimoPatron.
	 */
	public String getUltimoPatron() {
		return ultimoPatron;
	}
	/**
	 * @param Asigna ultimoPatron.
	 */
	public void setUltimoPatron(String ultimoPatron) {
		this.ultimoPatron = ultimoPatron;
	}
	/**
	 * @return Retorna maxPageItems.
	 */
	public int getMaxPageItems() {
		return maxPageItems;
	}
	/**
	 * @param Asigna maxPageItems.
	 */
	public void setMaxPageItems(int maxPageItems) {
		this.maxPageItems = maxPageItems;
	}
	
	
	/**
	 * @return Retorna index.
	 */
	public int getIndex() {
		return index;
	}
	
	/**
	 * @param Asigna index.
	 */
	public void setIndex(int index) {
		this.index = index;
	}
	/**
	 * @return Retorna linkSiguiente.
	 */
	public String getLinkSiguiente() {
		return linkSiguiente;
	}
	/**
	 * @param Asigna linkSiguiente.
	 */
	public void setLinkSiguiente(String linkSiguiente) {
		this.linkSiguiente = linkSiguiente;
	}
	/**
	 * @return Retorna offset.
	 */
	public int getOffset() {
		return offset;
	}
	/**
	 * @param Asigna offset.
	 */
	public void setOffset(int offset) {
		this.offset = offset;
	}
	/**
	 * @return Retorna pager.
	 */
	public int getPager() {
		return pager;
	}
	/**
	 * @param Asigna pager.
	 */
	public void setPager(int pager) {
		this.pager = pager;
	}
	


	/**
	 * @return Retorna existePersonaEnSistema.
	 */
	public boolean getExistePersonaEnSistema()
	{
		return existePersonaEnSistema;
	}


	/**
	 * @param existePersonaEnSistema Asigna existePersonaEnSistema.
	 */
	public void setExistePersonaEnSistema(boolean existePersonaEnSistema)
	{
		this.existePersonaEnSistema = existePersonaEnSistema;
	}
	
	
	/**
	 * @return Retorna numeroFilasMateriales.
	 */
	public int getNumeroFilasMateriales() {
		return numeroFilasMateriales;
	}
	/**
	 * @param Asigna numeroFilasMateriales.
	 */
	public void setNumeroFilasMateriales(int numeroFilasMateriales) {
		this.numeroFilasMateriales = numeroFilasMateriales;
	}


		
	/**
	 * @return Retorna origen.
	 */
	public String getOrigen() {
		return origen;
	}
	/**
	 * @param Asigna origen.
	 */
	public void setOrigen(String origen) {
		this.origen = origen;
	}


	public int getCentroAtencion() {
		return centroAtencion;
	}


	public void setCentroAtencion(int centroAtencion) {
		this.centroAtencion = centroAtencion;
	}


	/**
	 * @return the manejoProgramacionSalas
	 */
	public String getManejoProgramacionSalas() {
		return manejoProgramacionSalas;
	}


	/**
	 * @param manejoProgramacionSalas the manejoProgramacionSalas to set
	 */
	public void setManejoProgramacionSalas(String manejoProgramacionSalas) {
		this.manejoProgramacionSalas = manejoProgramacionSalas;
	}


	/**
	 * @return the tiposAnestesia
	 */
	public ArrayList<HashMap<String, Object>> getTiposAnestesia() {
		return tiposAnestesia;
	}


	/**
	 * @param tiposAnestesia the tiposAnestesia to set
	 */
	public void setTiposAnestesia(ArrayList<HashMap<String, Object>> tiposAnestesia) {
		this.tiposAnestesia = tiposAnestesia;
	}


	/**
	 * @return the mapaPeticionOtrosMateriales
	 */
	public HashMap getMapaPeticionOtrosMateriales() {
		return mapaPeticionOtrosMateriales;
	}


	/**
	 * @param mapaPeticionOtrosMateriales the mapaPeticionOtrosMateriales to set
	 */
	public void setMapaPeticionOtrosMateriales(HashMap mapaPeticionOtrosMateriales) {
		this.mapaPeticionOtrosMateriales = mapaPeticionOtrosMateriales;
	}
	
	/**
	 * @return the mapaPeticionOtrosMateriales
	 */
	public Object getMapaPeticionOtrosMateriales(String key) {
		return mapaPeticionOtrosMateriales.get(key);
	}


	/**
	 * @param mapaPeticionOtrosMateriales the mapaPeticionOtrosMateriales to set
	 */
	public void setMapaPeticionOtrosMateriales(String key, Object value) {
		this.mapaPeticionOtrosMateriales.put(key, value);
	}


	/**
	 * @return the ingresoId
	 */
	public int getIngresoId() {
		return ingresoId;
	}


	/**
	 * @param ingresoId the ingresoId to set
	 */
	public void setIngresoId(int ingresoId) {
		this.ingresoId = ingresoId;
	}


	/**
	 * @return the urgente
	 */
	public boolean isUrgente() {
		return urgente;
	}


	/**
	 * @param urgente the urgente to set
	 */
	public void setUrgente(boolean urgente) {
		this.urgente = urgente;
	}


	/**
	 * @return the imprimirAutorizacion
	 */
	public boolean isImprimirAutorizacion() {
		return imprimirAutorizacion;
	}


	/**
	 * @param imprimirAutorizacion the imprimirAutorizacion to set
	 */
	public void setImprimirAutorizacion(boolean imprimirAutorizacion) {
		this.imprimirAutorizacion = imprimirAutorizacion;
	}


	/**
	 * @return the listaNombresReportes
	 */
	public ArrayList<String> getListaNombresReportes() {
		return listaNombresReportes;
	}


	/**
	 * @param listaNombresReportes the listaNombresReportes to set
	 */
	public void setListaNombresReportes(ArrayList<String> listaNombresReportes) {
		this.listaNombresReportes = listaNombresReportes;
	}


	/**
	 * @return the validacionesAutorizaciones
	 */
	public DtoValidacionGeneracionAutorizacionCapitada getValidacionesAutorizaciones() {
		return validacionesAutorizaciones;
	}


	/**
	 * @param validacionesAutorizaciones the validacionesAutorizaciones to set
	 */
	public void setValidacionesAutorizaciones(
			DtoValidacionGeneracionAutorizacionCapitada validacionesAutorizaciones) {
		this.validacionesAutorizaciones = validacionesAutorizaciones;
	}


	public String getNombreArchivoGenerado() {
		return nombreArchivoGenerado;
	}


	public void setNombreArchivoGenerado(String nombreArchivoGenerado) {
		this.nombreArchivoGenerado = nombreArchivoGenerado;
	}


	public String getNombreArchivoCopia() {
		return nombreArchivoCopia;
	}


	public void setNombreArchivoCopia(String nombreArchivoCopia) {
		this.nombreArchivoCopia = nombreArchivoCopia;
	}


	public HashMap getMapaDetallePeticionInicial() {
		return mapaDetallePeticionInicial;
	}


	public void setMapaDetallePeticionInicial(HashMap mapaDetallePeticionInicial) {
		this.mapaDetallePeticionInicial = mapaDetallePeticionInicial;
	}

	/**
	 * @return the profesionalEspecialidades
	 */
	public ArrayList<ProfesionalEspecialidadesDto> getProfesionalEspecialidades() {
		return profesionalEspecialidades;
	}

	/**
	 * @param profesionalEspecialidades the profesionalEspecialidades to set
	 */
	public void setProfesionalEspecialidades(
			ArrayList<ProfesionalEspecialidadesDto> profesionalEspecialidades) {
		this.profesionalEspecialidades = profesionalEspecialidades;
	}

}