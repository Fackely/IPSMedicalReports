
/*
 * @(#)AdminMedicamentosForm.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */
package com.princetonsa.actionform.medicamentos;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import util.ConstantesBD;
import util.ResultadoBoolean;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.UtilidadValidacion;
import util.Utilidades;
import util.ValoresPorDefecto;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import com.princetonsa.action.medicamentos.AdminMedicamentosAction;
import com.princetonsa.dto.enfermeria.DtoAdministracionMedicamentosBasico;
import com.princetonsa.dto.manejoPaciente.DtoCentroCostoViaIngreso;
import com.princetonsa.mundo.Camas1;
import com.princetonsa.mundo.CentrosCostoViaIngreso;
import com.princetonsa.mundo.manejoPaciente.Habitaciones;
import com.princetonsa.mundo.manejoPaciente.Pisos;
import com.princetonsa.mundo.medicamentos.AdminMedicamentos;
import com.servinte.axioma.orm.ViasIngreso;

/**
 * Form que contiene todos los datos específicos para generar 
 * la Admin de medicamentos
 * Y adicionalmente hace el manejo de reset de la forma y de
 * validación de errores de datos de entrada.
 * @version 1,0. Sept 16, 2004
 * @author <a href="mailto:joan@PrincetonSA.com">Joan López</a>
 * @author <a href="mailto:wilson@PrincetonSA.com">Wilson Ríos</a>
 */
public class AdminMedicamentosForm extends ValidatorForm
{
	/**
	 * Objeto para manejar los logs de esta clase
	*/
	private Logger logger = Logger.getLogger(AdminMedicamentosForm.class);
	/**
	* Estado en el que se encuentra el proceso.       
	*/
	private String estado;
	
	/**
	 * mapa con los medicamentos
	 */
	private HashMap medicamentosMap;
	
	/**
	 * mapa con los insumos
	 */
	private HashMap insumosMap;
	
	//-----------------LISTADOS Y ORDENAMIENTO
	
	/**
	 * Colección con los datos del listado 
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
	 * 
	 */
	private ArrayList<DtoAdministracionMedicamentosBasico> infoAdminSolicitud;
	
	//-------------------FIN LISTADOS Y ORDENAMIENTO
	
	
	//--------------------ENCABEZADO DE LA SOLICITUD
	
	/**
	 * Centro de costo seleccionado 
	 * para el filtro del listado de las 
	 * solicitudes
	 */
	private int codigoCentroCosto;
	
	/**
	 * Codigo del numero de la solicitud.
	 */
	private int numeroSolicitud;
	
	/**
	 * Fecha de la solicitud
	 */
	private String fechaSolicitud;

	/**
	 * Hora de solicitud
	 */
	private String horaSolicitud;
	
	/**
	 * medico que solicita
	 */
	private String medicoSolicitante;
	
	/**
	 * número de autorización
	 */
	//private String numeroAutorizacion;
	
	/**
	 * 
	 */
	private int codigoEstadoMedico;
	
	/**
	 * Estado Médico de la solicitud
	 */
	private String estadoMedico;
	
	/**
	 * Observaciones Generales de la solicitud
	 */
	private String observacionesGenerales;
	
	/**
	 * Código del paciente para cargarlo en session
	 */
	private int codigoPaciente;
	
	//-----------------------FIN ENCABEZADO DE LA SOLICITUD
	
	
	//-----------------------DEVOLUCIONES A FARMACIA
	/**
	 * Cuando se trata de finalizar la administración muchas veces quedan
	 * articulos sin administrar entonces se debe generar una devolución,  
	 * este hashMap contiene el código del artículo y su correspondiente
	 * cantidad para generar la devolución
	 */
	private  HashMap devolucionMap = new HashMap();
	
	/**
	 * Cuando se trata de finalizar la admin, entonces e 
	 * valida que no existan artículos con admin pendiente,
	 * en casio dew que suceda se muestra el mensaje
	 */
	private boolean mostrarMensajeSaldoPendienteAdministrar;
	
	/**
	 * 
	 */
	private boolean mostrarEncabezados=true;
	
	/**
	 * Observaciones de la devolución
	 */
	private String observacionesDevolucion;
    
    /**
     * motivo de la devolución
     */
    private String motivoDevolucion;
	
	///////------------------FIN DEVOLUCIONES A FARMACIA----
	
	/**
	 * Farmacia que realizo el despacho
	 */
	private int farmaciaDespacho;
	
	/**
	 * Código del consecutivo de orden médica
	 */
	private int orden;
	
	/**
	 * Prioridad de la solicitud
	 */
	private String prioridad;
	
	/**
	 * boton volver del resumen que contiene el
	 * estado del listado por area o por paciente
	 */
	private String botonVolverEstadoListado;
	
	/**
	 * check de finalizar o no insumos
	 */
	private String finalizarInsumos;
	
	/**
	 * mapa de resumen de la administracion
	 */
	private HashMap resumenAdminMap;
	
	/**
	 * 
	 */
	private int areaFiltro;
	
	/**
	 * 
	 */
	private int pisoFiltro;
	
	/**
	 * 
	 */
	private int habitacionFiltro;
	
	
	/**
	 * 
	 */
	private int camaFiltro;
	
	/**
	 * 
	 */
	private HashMap <String, Object> areasTagMap; 
	
	/**
	 * 
	 */
	private HashMap<String, Object> pisosTagMap;
	
	/**
	 * 
	 */
	private HashMap<String, Object> habitacionesTagMap;
	
	/**
	 * 
	 */
	private HashMap<Object, Object> camasTagMap;
	
	/**
	 * 
	 */
	private String fechaInicialFiltro;
	
	/**
	 * 
	 */
	private String fechaFinalFiltro;
	
	/**
	 * 
	 */
	private boolean entidadControlaDespachoSaldoMultidosis;
	
	/**Lista las vias de ingreso por la cual ingresa un paciente*/
	private ArrayList<ViasIngreso> listadoViasIngreso;
	
	/**Codigo de la via de ingreso para filtro de busqueda*/
	private int viaIngresoFiltro;
	
	/***/
	private ArrayList<DtoCentroCostoViaIngreso> listaCentroCostosViaIngreso;
	
	/**Bandera cuando todos los articulos de la solicitud esten finalizados*/
	private boolean articulosFinalizados;
	
	private HashMap<String, Object> infoLoteMedicamentosMap;
	
	private HashMap<String, Object> infoLoteInsumosMap;
	
	/**
	 * resetea los valores del form
	 *
	 */
	public void reset(int codigoInstitucion, int codigoCentroAtencion, boolean resetearColeccion)
	{
		this.codigoCentroCosto= ConstantesBD.codigoCentroCostoNoSeleccionado;
		this.devolucionMap=new HashMap();
		this.devolucionMap.put("numRegistros", "0");
		this.codigoCentroCosto=0;
		this.numeroSolicitud=0;
		this.fechaSolicitud="";
		this.horaSolicitud="";
		this.medicoSolicitante="";
		//this.numeroAutorizacion="";
		this.codigoEstadoMedico=0;
		this.infoAdminSolicitud=new ArrayList<DtoAdministracionMedicamentosBasico>();
		this.estadoMedico="";
		this.observacionesGenerales="";
		this.codigoPaciente=0;
		this.mostrarMensajeSaldoPendienteAdministrar=false;
		this.observacionesDevolucion="";
        this.motivoDevolucion="";
		this.farmaciaDespacho=0;
		this.orden=0;
		this.prioridad="";
		this.botonVolverEstadoListado="";
		
		this.medicamentosMap= new HashMap();
		this.medicamentosMap.put("numRegistros", "0");
		this.insumosMap= new HashMap();
		this.insumosMap.put("numRegistros", "0");
		this.finalizarInsumos=ConstantesBD.acronimoNo;
		this.resumenAdminMap=new HashMap();
		
		this.areaFiltro=ConstantesBD.codigoNuncaValido;
		this.pisoFiltro=ConstantesBD.codigoNuncaValido;
		this.habitacionFiltro=ConstantesBD.codigoNuncaValido;;
		this.camaFiltro= ConstantesBD.codigoNuncaValido;
		this.viaIngresoFiltro = ConstantesBD.codigoNuncaValido;
		
		this.fechaInicialFiltro=UtilidadFecha.incrementarDiasAFecha(UtilidadFecha.getFechaActual(), -1, false);
        this.fechaFinalFiltro=UtilidadFecha.getFechaActual();
        
		this.entidadControlaDespachoSaldoMultidosis=UtilidadTexto.getBoolean(ValoresPorDefecto.getEntidadControlaDespachoSaldosMultidosis(codigoInstitucion));

		this.listadoViasIngreso=new ArrayList<ViasIngreso>();
		this.listaCentroCostosViaIngreso=new ArrayList<DtoCentroCostoViaIngreso>();
		
		this.articulosFinalizados=false;
		
		this.infoLoteMedicamentosMap = new HashMap<String, Object>();
		this.infoLoteInsumosMap = new HashMap<String, Object>();
		
        if(resetearColeccion)
        {	
        	this.col= new ArrayList();
        }	
		inicializarTagsMap(codigoInstitucion, codigoCentroAtencion);
	}
	
	/**
	 * 
	 * @param codigoInstitucion
	 * @param codigoCentroAtencion
	 */
	public void inicializarTagsMap(int codigoInstitucion, int codigoCentroAtencion)
	{
		this.areasTagMap= CentrosCostoViaIngreso.consultarCentrosCostoViaIngreso(codigoInstitucion, codigoCentroAtencion);
		this.pisosTagMap= Pisos.pisosXCentroAtencionTipo(codigoCentroAtencion, codigoInstitucion);
		this.habitacionesTagMap= Habitaciones.habitacionesXCentroAtencionTipo(codigoCentroAtencion, codigoInstitucion);
		this.camasTagMap= Camas1.listadoCamas1(codigoInstitucion, codigoCentroAtencion);
		
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
		
		if(estado.equals("salirGuardar"))
		{
		    
			//UtilidadValidacion.validarNumeroAutorizacion(errores,numeroAutorizacion);//validar que el número de autorización este entre [0...9||a...z||A...Z||-]
		    
		    
		    /******VALIDACIONES DE LOS MEDICAMENTOS-----------------------------------------------------------***********/
		    
		    for(int j=0; j < Integer.parseInt(this.getMedicamentosMap("numRegistros").toString()); j ++ )
		    {
		    	//solo se valida para los medicamentos que tienen el centinela adminitrar en true
		    	if(this.getMedicamentosMap("administrar_"+j).toString().equals(ConstantesBD.acronimoSi))
		    	{	
		    		boolean fechaHoraAdminValida=true;
		    		
			    	if(this.getMedicamentosMap("fecha_"+j).toString().equals(""))
			        {
			            errores.add("campo requerido", new ActionMessage("errors.required","La fecha del medicamento "+this.getMedicamentosMap("codigo_"+j)+"-"+this.getMedicamentosMap("descripcion_"+j)));
			            fechaHoraAdminValida=false;
			        }
			        else if(!UtilidadFecha.validarFecha(this.getMedicamentosMap("fecha_"+j).toString()))
			        {
			        	errores.add("Fecha Admin.", new ActionMessage("errors.formatoFechaInvalido", " Administración del medicamento "+this.getMedicamentosMap("codigo_"+j)+"-"+this.getMedicamentosMap("descripcion_"+j)));
			        	fechaHoraAdminValida=false;
					}
			        else if( UtilidadFecha.esFechaMenorQueOtraReferencia(UtilidadFecha.getFechaActual(), this.getMedicamentosMap("fecha_"+j).toString()))
			        {
			        	errores.add("Fecha Admin", new ActionMessage("errors.fechaPosteriorIgualActual","Administración del medicamento "+this.getMedicamentosMap("codigo_"+j)+"-"+this.getMedicamentosMap("descripcion_"+j), "actual"));
			        	fechaHoraAdminValida=false;
			        }
			        else 
			        {
			        	 if(!this.getMedicamentosMap("hora_"+j).equals(""))
					     {
			        	 	if(UtilidadFecha.validacionHora(this.getMedicamentosMap("hora_"+j).toString()).puedoSeguir)
					        {
			        	 		//logger.info("FECHA ACTUAL: "+UtilidadFecha.getFechaActual());
			        	 		//logger.info("HORA ACTUAL: "+UtilidadFecha.getHoraActual());
			        	 		//logger.info("FECHA : "+this.getMedicamentosMap("fecha_"+j));
			        	 		//logger.info("HORA : "+this.getMedicamentosMap("hora_"+j));
			        	 		ResultadoBoolean resultado= UtilidadFecha.compararFechas(UtilidadFecha.getFechaActual(), UtilidadFecha.getHoraActual(),this.getMedicamentosMap("fecha_"+j).toString(),this.getMedicamentosMap("hora_"+j).toString());
			        	 		
			        	 		if(! resultado.isTrue() )
								{
			        	 			errores.add("hora Admin", new ActionMessage("errors.horaSuperiorA", "de Administración del medicamento "+this.getMedicamentosMap("codigo_"+j)+"-"+this.getMedicamentosMap("descripcion_"+j), " actual"));
			        	 			fechaHoraAdminValida=false;
								}
					        }
					     }
			        }
		         
			        if(this.getMedicamentosMap("hora_"+j).toString().equals(""))
			        {
			            errores.add("campo requerido", new ActionMessage("errors.required","La hora del medicamento "+this.getMedicamentosMap("codigo_"+j)+"-"+this.getMedicamentosMap("descripcion_"+j)));
			            fechaHoraAdminValida=false;
			        }
			        else if(!UtilidadFecha.validacionHora(this.getMedicamentosMap("hora_"+j).toString()).puedoSeguir)
			        {
			        	errores.add("Hora Admin", new ActionMessage("errors.formatoHoraInvalido", " Administración del medicamento "+this.getMedicamentosMap("codigo_"+j)+"-"+this.getMedicamentosMap("descripcion_"+j)));
			        	fechaHoraAdminValida=false;
			        }
			        
			        //si la fecha y la hora es valida entonces debemos validar que sea mayor igual a la fecha de solicitud
			        if(fechaHoraAdminValida)
			        {
			        	if(!UtilidadFecha.compararFechas( this.getMedicamentosMap("fecha_"+j).toString() , this.getMedicamentosMap("hora_"+j).toString(), this.getFechaSolicitud(), this.getHoraSolicitud()).isTrue())
			        	{
			        		//La fecha {0} debe ser posterior o igual a la fecha {1}. [aa-21]
			        		errores.add("", new ActionMessage("errors.fechaAnteriorIgualActual", "hora de Administración del medicamento "+this.getMedicamentosMap("codigo_"+j)+"-"+this.getMedicamentosMap("descripcion_"+j), " de solicitud "+this.getFechaSolicitud()+" - "+this.getHoraSolicitud()));
			        	}
			        }
			        
			        
			        //si tratan de administrar y el despacho es cero o vacio y no es traido por el paciente
			        //entonces se debe prohibir la administracion
			        if(	(this.getMedicamentosMap("traidopaciente_"+j).toString().equals(ConstantesBD.acronimoNo))
			    		&& (UtilidadTexto.isEmpty(this.getMedicamentosMap("unidadesdespachadas_"+j).toString()) 
			    		|| this.getMedicamentosMap("unidadesdespachadas_"+j).toString().equals("0") ))
			    	{
			    		errores.add("campo requerido", new ActionMessage("error.adminMedicamentos.noSeleccionTraidoPaciente","medicamento", this.getMedicamentosMap("codigo_"+j)+"-"+this.getMedicamentosMap("descripcion_"+j)));
			    	}
			    	
			        //para los articulos que tienen cantidad de dosis ordenadas mayor que cero y el nro dosis total mayor q cero 
			        //deben  validar el nro de dosis para administrar
			        if( Integer.parseInt(this.getMedicamentosMap("cantidadtotaldosisordenada_"+j).toString())>0
			        		&& Double.parseDouble(this.getMedicamentosMap("cantidadunidosisarticulo_"+j).toString())>0)
			        {
			        	//para mantener la integridad de los datos viejos se debe validar que la dosis sea decimal
			        	boolean errorXDosis=false;
			        	try
			        	{
			        		Double.parseDouble(this.getMedicamentosMap("dosis_"+j).toString());
			        	}
			        	catch(Exception e)
			        	{
			        		errorXDosis=true;
			        		errores.add("", new ActionMessage("errors.float", "La Dósis del medicamento "+this.getMedicamentosMap("codigo_"+j)));
			        	}
			        	
			        	if(!errorXDosis)
			        	{	
				        	double numeroAdminMax=0;
				        	int numeroAdminMaxAprox=0;
				        	//OJO SEGUN GERMAN Y NURY NO SE DEBE SOBREPASAR LO ORDENADO xplanner 37062
				        	/*if(this.getMedicamentosMap("multidosis_"+j).toString().equals(ConstantesBD.acronimoNo))*/
				        	{
				        		numeroAdminMax= Integer.parseInt(this.getMedicamentosMap("cantidadtotaldosisordenada_"+j).toString());
				        	}
				        	//OJO SEGUN GERMAN Y NURY NO SE DEBE SOBREPASAR LO ORDENADO xplanner 37062
				        	//EN ESTE CASO SE PERMITIA PEDIR HASTA QUE SE ACABARA LA UNIDAD COMPLETA xplanner 37062
				        	/*else if(this.getMedicamentosMap("multidosis_"+j).toString().equals(ConstantesBD.acronimoSi))
				        	{
				        		numeroAdminMax= (Integer.parseInt(this.getMedicamentosMap("cantidad_"+j).toString()) 
				        						* Double.parseDouble(this.getMedicamentosMap("cantidadunidosisarticulo_"+j).toString()))
				        						/ (Double.parseDouble(this.getMedicamentosMap("dosis_"+j).toString())) ;
				        	}*/
				        	
				        	numeroAdminMaxAprox= UtilidadTexto.aproximarAnteriorUnidad(numeroAdminMax+"");
				        	
				        	
				        	//se obtiene el numero de dosis administradas total
				        	//int numeroAdministracionesTotales=AdminMedicamentos.obtenerNumeroDosisAdministradas(numeroSolicitud, Integer.parseInt(this.getMedicamentosMap("codigo_"+j).toString()));
				        	String temporal=String.valueOf(medicamentosMap.get("codigosustitutoprincipal_"+j));
							String codigoArt=medicamentosMap.get("codigo_"+j).toString();
							if(!temporal.equals("vacio"))
							{
								codigoArt=temporal.split("@")[1];
							}
							
				        	int numeroAdministracionesTotales= Utilidades.convertirAEntero(this.getMedicamentosMap("numeroAdministracionesTotales_"+codigoArt)+"");
				        	
				        	//se incrementa en uno porque se debe contar la actual
				        	numeroAdministracionesTotales++;
				        	
				        	
				        	if(numeroAdministracionesTotales>numeroAdminMaxAprox)
				        	{
				        		//se saca el error de que no puede ser mayor
				        		errores.add("", new ActionMessage("errors.cantidadDosisMayor", this.getMedicamentosMap("codigo_"+j)));
				        	}
				        }
			        }	
		        	//en el caso de tener que leer las unidades consumidas entonces se debe validar que sean mayor que cero 
		        	boolean errorUnidadesConsumidas=false;
		        	if(this.getMedicamentosMap("leerunidadesconsumidas_"+j).toString().equals(ConstantesBD.acronimoSi))
		        	{
		        		try
					    {
		        			if(Integer.parseInt(this.getMedicamentosMap("unidadesconsumidas_"+j).toString()) < 0)
				        	{
		        				errores.add("numero menor/igual que 0", new ActionMessage("errors.integerMayorIgualQue","Las unidades consumidas del artículo "+this.getMedicamentosMap("codigo_"+j),"0"));
		        				errorUnidadesConsumidas=true;
				        	}
					    }
		        		catch(Exception e)
		        		{
		        			errores.add("numero menor/igual que 0", new ActionMessage("errors.integerMayorIgualQue","Las unidades consumidas del artículo "+this.getMedicamentosMap("codigo_"+j),"0"));
		        			errorUnidadesConsumidas=true;
		        		}
		        	}
		        	
		        	//se debe validar que no supere las unidades ordenadas por farmacia
		        	if(!errorUnidadesConsumidas)
		        	{
		        		int totalUnidadesConsumidas= Integer.parseInt(this.getMedicamentosMap("unidadesconsumidas_"+j).toString())
		        									+ Integer.parseInt(this.getMedicamentosMap("unidadesconsumidasxfarmacia_"+j).toString())
		        									+ Integer.parseInt(this.getMedicamentosMap("unidadesconsumidasxpaciente_"+j).toString());
		        			
		        		int totalUnidadesOrdenadas= Integer.parseInt(this.getMedicamentosMap("cantidad_"+j).toString());
		        			
		        		if(totalUnidadesConsumidas>totalUnidadesOrdenadas)
		        		{
		        			errores.add("noPuedeSerMayor", new ActionMessage("errors.unidadesOrdenadasMayor",this.getMedicamentosMap("cantidad_"+j).toString(), this.getMedicamentosMap("codigo_"+j).toString()));
		        		}
		        	}
			        
				}// fin centinela administrar
		    	
		    	//no se debe permitir finalizar administracion de articulos que tengan depacho total=0,  y numero dosis administradas= 0 en estado solicitada, es decir, que no tengan despacho final
		    	logger.info("codigoEstadoNedico: "+codigoEstadoMedico);
		    	if(codigoEstadoMedico==ConstantesBD.codigoEstadoHCSolicitada)
		    	{
		    		logger.info("********************VALIDACIONES SOLICITUD CRREADA*******************************************");
		    		//logger.info("finalizararticulo_"+j+": *"+this.getMedicamentosMap("finalizararticulo_"+j)+"*");
		    		//logger.info("traidopaciente_"+j+": *"+this.getMedicamentosMap("traidopaciente_"+j)+"*");
		    		//logger.info("administrar_"+j+": *"+this.getMedicamentosMap("administrar_"+j)+"*");
			    	if(this.getMedicamentosMap("finalizararticulo_"+j).toString().equals(ConstantesBD.acronimoSi))
			    	{
			    		/*if(UtilidadTexto.isEmpty(this.getMedicamentosMap("unidadesdespachadas_"+j).toString()))
			    		{
			    			errores.add("", new ActionMessage("error.adminMedicamentos.finalizarArticuloCero"));
			    		}*/
			    		//esta validacion no aplica cuando tiene sustitutos.
			    		if(Utilidades.convertirAEntero(this.getMedicamentosMap("tienesustituto_"+j).toString())<0)
			    		{
			    			//Valida cantidad solicitada del medicamento es distinta a la despachada (o sea, no ha habido despacho terminado del medicamento) 
			    			
			    		 if(!UtilidadTexto.isEmpty(this.getMedicamentosMap("unidadesdespachadas_"+j).toString())){	
			    			if(Integer.parseInt(this.getMedicamentosMap("cantidad_"+j).toString()) != Integer.parseInt(this.getMedicamentosMap("unidadesdespachadas_"+j).toString())){
			    				
			    				if(this.getMedicamentosMap("traidopaciente_"+j).toString().equals(ConstantesBD.acronimoNo)||
					    				this.getMedicamentosMap("administrar_"+j).toString().equals(ConstantesBD.acronimoNo))
			    				{
			    					errores.add("campo requerido", new ActionMessage("error.adminMedicamentos.finalizarArticuloSinAdmin",this.getMedicamentosMap("codigo_"+j)+"-"+this.getMedicamentosMap("descripcion_"+j)));
			    				}	
			    				
			    			}
			    		  }
			    			
			    		}
			    		
			    		//
			    		/*else if(Integer.parseInt(this.getMedicamentosMap("unidadesdespachadas_"+j).toString())<1)
			    		{
			    			int nroDosisAdministradas= Integer.parseInt(this.getMedicamentosMap("numerodosisadminfarmacia_"+j).toString())
			    										+Integer.parseInt(this.getMedicamentosMap("numerodosisadminpaciente_"+j).toString());
			    			if(nroDosisAdministradas<1)
			    			{
			    				errores.add("", new ActionMessage("error.adminMedicamentos.finalizarArticuloCero"));
			    			}
			    		}*/ //xplanner [id=11607]
			    	}
			    	logger.info("********************FIN VALIDACIONES SOLICITUD CRREADA*******************************************");
		    	}	
		    }    	   
		
		    /****** FIN VALIDACIONES DE LOS MEDICAMENTOS-----------------------------------------------------------***********/
		    
		    
		    /******VALIDACIONES DE LOS INSUMOS-----------------------------------------------------------***********/
		    
		    boolean errorConsumoInsumos=false;
		    for(int w=0; w<Integer.parseInt(this.getInsumosMap("numRegistros").toString());w++)
		    {
		    	try
		        {
		            if(Integer.parseInt(this.getInsumosMap("consumo_"+w).toString()) < 0)
		            {
		                errores.add("numero menor/igual que 0", new ActionMessage("errors.integerMayorIgualQue","El consumo de Insumos ","0"));
		                errorConsumoInsumos = true;
		            } 
		        }
		        catch(Exception e)
		        {
		            errores.add("numeroNoEntero", new ActionMessage("errors.integer", "El consumo del insumo " + this.getInsumosMap("codigo_"+w).toString()+"-"+this.getInsumosMap("descripcion_"+w).toString()));
		            errorConsumoInsumos = true;
		        }
		    }
		    
		    //SE VALIDA QUE EL CONSUMO NO SUPERE LA CANTIDAD DESPACHADA
		    
		    if(!errorConsumoInsumos)
		    {
		    	for(int w=0; w<Integer.parseInt(this.getInsumosMap("numRegistros").toString());w++)
		    	{
		    		if(	(Integer.parseInt(this.getInsumosMap("consumo_"+w).toString())>0) 
		    			&& (this.getInsumosMap("traidopacienteinsumo_"+w).toString().equals(ConstantesBD.acronimoNo))
		    			&& (Integer.parseInt(this.getInsumosMap("cantidaddespacho_"+w).toString()))==0)
		    		{
		    			errores.add("campo requerido", new ActionMessage("error.adminMedicamentos.noSeleccionTraidoPaciente","insumo", this.getInsumosMap("codigo_"+w)+"-"+this.getInsumosMap("descripcion_"+w)));
		    		}
		    		
		    		//si ya se administro todo lo despachado y no se selecciono traidoPaciente (insumos)
		    		if( (Integer.parseInt(this.getInsumosMap("consumofarmacia_"+w).toString())) >= 0 
		    			&& (Integer.parseInt(this.getInsumosMap("consumo_"+w).toString()) >=0 ) 
		    			&& (this.getInsumosMap("traidopacienteinsumo_"+w).toString().equals(ConstantesBD.acronimoNo))) 
		    		{
		    			int validarMed= Integer.parseInt( this.getInsumosMap("consumo_"+w).toString() ) + Integer.parseInt(this.getInsumosMap("consumofarmacia_"+w).toString());
		    			if(validarMed > Integer.parseInt( this.getInsumosMap("cantidaddespacho_"+w).toString()))	
		    			{
		    				errores.add("noPuedeSerMayor", new ActionMessage("errors.cantidadMayor","Para el articulo " + this.getInsumosMap("codigo_"+w)+"-"+this.getInsumosMap("descripcion_"+w)+", El consumo + Total Administrado Farmacia(F)"));  
						}
				    }
		    	}	
		    }
		    
		    
		    //AL MENOS DEBE ESTAR UN MEDICAMENTO O INSUMO CON EL CHECK ADMINISTRAR O FINALIZAR
		    boolean existeArtAdministradoOFinalizado=false;
		    /****PARTE DE MEDICAMENTOS****/
			for(int w=0; w<Integer.parseInt(this.getMedicamentosMap("numRegistros").toString()); w++)
			{
				if(this.getMedicamentosMap("finalizararticulo_"+w).toString().equals(ConstantesBD.acronimoSi) 
					|| this.getMedicamentosMap("administrar_"+w).toString().equals(ConstantesBD.acronimoSi))
				{
					existeArtAdministradoOFinalizado=true;
				}
			}
			/****PARET DE INSUMOS***/
			if(this.getFinalizarInsumos().equals(ConstantesBD.acronimoSi))
			{
				existeArtAdministradoOFinalizado=true;
			}
			for(int w=0; w<Integer.parseInt(this.getInsumosMap("numRegistros").toString());w++)
			{
				try
				{
					if(Integer.parseInt(this.getInsumosMap("consumo_"+w).toString())>0)
						existeArtAdministradoOFinalizado=true;
				}
				catch(Exception e)
				{
					this.setInsumosMap("consumo_"+w, "0");
				}
			}
		    if(!existeArtAdministradoOFinalizado)
		    {
		    	errores.add("error.adminMedicamentos.noArticulosAAdmin", new ActionMessage("error.adminMedicamentos.noArticulosAAdmin"));
		    }
		    
		    /*if(errores.isEmpty())
		    {
		    	//no se debe permitir finalizar administracion de articulos que tengan depacho total=0,  y numero dosis administradas= 0 en estado solicitada, es decir, que no tengan despacho final
		    	if(codigoEstadoMedico==ConstantesBD.codigoEstadoHCSolicitada)
		    	{
			    	boolean existeErrorFinalizarArticuloCero=false;
			    	if(this.getFinalizarInsumos().equals(ConstantesBD.acronimoSi))
					{
				    	for(int w=0; w<Integer.parseInt(this.getInsumosMap("numRegistros").toString());w++)
				    	{
				    		if(	(Integer.parseInt(this.getInsumosMap("consumo_"+w).toString())==0) 
				    			&& (Integer.parseInt(this.getInsumosMap("cantidaddespacho_"+w).toString()))==0)
				    		{
				    			existeErrorFinalizarArticuloCero=true;
				    		}
				    	}	
				    	if(existeErrorFinalizarArticuloCero)
				    	{	
				    		//este error toca ponerlo en el for y enviarle la desc del articulo
				    		errores.add("", new ActionMessage("error.adminMedicamentos.finalizarArticuloCero"));
				    	}
					}
				}		
		    }*/
		}
		return errores;
	}

	/**
	 * Retorna Colección para mostrar datos en el pager
	 * @return
	 */
	public Collection getCol() {
		if(this.col==null)
			return new ArrayList();
		return col;
	}
	/**
	 * Asigna Colección para mostrar datos en el pager
	 * @param collection
	 */
	public void setCol(Collection collection) {
		col = collection;
	}

	public int getColSize()
	{
		if(col!=null)
			return col.size();
		else
			return 0;
	}
	
	/**
	 * Returns the columna.
	 * @return String
	 */
	public String getColumna()
	{
		return columna;
	}
	/**
	 * Returns the ultimaPropiedad.
	 * @return String
	 */
	public String getUltimaPropiedad()
	{
		return ultimaPropiedad;
	}
	/**
	 * Sets the columna.
	 * @param columna The columna to set
	 */
	public void setColumna(String columna)
	{
		this.columna = columna;
	}
	/**
	 * Sets the ultimaPropiedad.
	 * @param ultimaPropiedad The ultimaPropiedad to set
	 */
	public void setUltimaPropiedad(String ultimaPropiedad)
	{
		this.ultimaPropiedad = ultimaPropiedad;
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
	 * @return Returns the codigoCentroCosto.
	 */
	public int getCodigoCentroCosto() {
		return codigoCentroCosto;
	}
	/**
	 * @param codigoCentroCosto The codigoCentroCosto to set.
	 */
	public void setCodigoCentroCosto(int codigoCentroCosto) {
		this.codigoCentroCosto = codigoCentroCosto;
	}
   
    /**
     * @return Retorna  numeroSolicitud.
     */
    public int getNumeroSolicitud()
    {
        return numeroSolicitud;
    }
    /**
     * @param numeroSolicitud asigna numeroSolicitud.
     */
    public void setNumeroSolicitud(int numeroSolicitud)
    {
        this.numeroSolicitud = numeroSolicitud;
    }
   
	
	/**
	 * @return Returns the estadoMedico.
	 */
	public String getEstadoMedico() {
		return estadoMedico;
	}
	/**
	 * @param estadoMedico The estadoMedico to set.
	 */
	public void setEstadoMedico(String estadoMedico) {
		this.estadoMedico = estadoMedico;
	}
	/**
	 * @return Returns the fechaSolicitud.
	 */
	public String getFechaSolicitud() {
		return fechaSolicitud;
	}
	/**
	 * @param fechaSolicitud The fechaSolicitud to set.
	 */
	public void setFechaSolicitud(String fechaSolicitud) {
		this.fechaSolicitud = fechaSolicitud;
	}
	/**
	 * @return Returns the horaSolicitud.
	 */
	public String getHoraSolicitud() {
		return horaSolicitud;
	}
	/**
	 * @param horaSolicitud The horaSolicitud to set.
	 */
	public void setHoraSolicitud(String horaSolicitud) {
		this.horaSolicitud = horaSolicitud;
	}
	/**
	 * @return Returns the medicoSolicitante.
	 */
	public String getMedicoSolicitante() {
		return medicoSolicitante;
	}
	/**
	 * @param medicoSolicitante The medicoSolicitante to set.
	 */
	public void setMedicoSolicitante(String medicoSolicitante) {
		this.medicoSolicitante = medicoSolicitante;
	}
	/**
	 * @return Returns the numeroAutorizacion.
	 */
	/*
	public String getNumeroAutorizacion() {
		return numeroAutorizacion;
	}
	*/
	/**
	 * @param numeroAutorizacion The numeroAutorizacion to set.
	 */
	/*
	public void setNumeroAutorizacion(String numeroAutorizacion) {
		this.numeroAutorizacion = numeroAutorizacion;
	}
	*/
	/**
	 * @return Returns the observacionesGenerales.
	 */
	public String getObservacionesGenerales() {
		return observacionesGenerales;
	}
	/**
	 * @param observacionesGenerales The observacionesGenerales to set.
	 */
	public void setObservacionesGenerales(String observacionesGenerales) {
		this.observacionesGenerales = observacionesGenerales;
	}
	/**
	 * @return Returns the codigoPaciente.
	 */
	public int getCodigoPaciente() {
		return codigoPaciente;
	}
	/**
	 * @param codigoPaciente The codigoPaciente to set.
	 */
	public void setCodigoPaciente(int codigoPaciente) {
		this.codigoPaciente = codigoPaciente;
	}
	
    /**
	 * Set del mapa de devolucion
	 * @param key
	 * @param value
	 */
	public void setDevolucionMap(String key, Object value) 
	{
		devolucionMap.put(key, value);
	}
	/**
	 * Get del mapa de devolucion
	 * Retorna el valor de un campo dado su nombre
	 */
	public Object getDevolucionMap(String key) 
	{
		return devolucionMap.get(key);
	}
	
	/**
	 * @return Returns the mostrarMensajeSaldoPendienteAdministrar.
	 */
	public boolean getMostrarMensajeSaldoPendienteAdministrar() {
		return mostrarMensajeSaldoPendienteAdministrar;
	}
	/**
	 * @param mostrarMensajeSaldoPendienteAdministrar The mostrarMensajeSaldoPendienteAdministrar to set.
	 */
	public void setMostrarMensajeSaldoPendienteAdministrar(
			boolean mostrarMensajeSaldoPendienteAdministrar) {
		this.mostrarMensajeSaldoPendienteAdministrar = mostrarMensajeSaldoPendienteAdministrar;
	}
	
	/**
	 * @return Returns the observacionesDevolucion.
	 */
	public String getObservacionesDevolucion() {
		return observacionesDevolucion;
	}
	/**
	 * @param observacionesDevolucion The observacionesDevolucion to set.
	 */
	public void setObservacionesDevolucion(String observacionesDevolucion) {
		this.observacionesDevolucion = observacionesDevolucion;
	}

	/**
	 * Resetea el hash map de devoluciones
	 *
	 */
	public void resetHashMapDevolucion()
	{
		this.devolucionMap.clear();
		this.devolucionMap.put("numRegistros", "0");
	}
	
	/**
	 * @return Returns the devolucionMap.
	 */
	public HashMap getDevolucionMap() {
		return devolucionMap;
	}
	/**
	 * @param devolucionMap The devolucionMap to set.
	 */
	public void setDevolucionMap(HashMap devolucionMap) {
		this.devolucionMap = devolucionMap;
	}
	/**
	 * @return Returns the farmaciaDespacho.
	 */
	public int getFarmaciaDespacho() {
		return farmaciaDespacho;
	}
	/**
	 * @param farmaciaDespacho The farmaciaDespacho to set.
	 */
	public void setFarmaciaDespacho(int farmaciaDespacho) {
		this.farmaciaDespacho = farmaciaDespacho;
	}
    /**
     * @return Retorna orden.
     */
    public int getOrden() {
        return orden;
    }
    /**
     * @param orden Asigna orden.
     */
    public void setOrden(int orden) {
        this.orden = orden;
    }
    /**
     * @return Returns the prioridad.
     */
    public String getPrioridad() {
        return prioridad;
    }
    /**
     * @param prioridad The prioridad to set.
     */
    public void setPrioridad(String prioridad) {
        this.prioridad = prioridad;
    }
    /**
     * @return Returns the motivoDevolucion.
     */
    public String getMotivoDevolucion() {
        return motivoDevolucion;
    }
    /**
     * @param motivoDevolucion The motivoDevolucion to set.
     */
    public void setMotivoDevolucion(String motivoDevolucion) {
        this.motivoDevolucion = motivoDevolucion;
    }

	/**
	 * @return Returns the botonVolverEstadoListado.
	 */
	public String getBotonVolverEstadoListado() {
		return botonVolverEstadoListado;
	}

	/**
	 * @param botonVolverEstadoListado The botonVolverEstadoListado to set.
	 */
	public void setBotonVolverEstadoListado(String botonVolverEstadoListado) {
		this.botonVolverEstadoListado = botonVolverEstadoListado;
	}

	/**
	 * @return the insumosMap
	 */
	public HashMap getInsumosMap() {
		return insumosMap;
	}

	/**
	 * @param insumosMap the insumosMap to set
	 */
	public void setInsumosMap(HashMap insumosMap) {
		this.insumosMap = insumosMap;
	}

	/**
	 * @return the medicamentosMap
	 */
	public HashMap getMedicamentosMap() {
		return medicamentosMap;
	}

	/**
	 * @param medicamentosMap the medicamentosMap to set
	 */
	public void setMedicamentosMap(HashMap medicamentosMap) {
		this.medicamentosMap = medicamentosMap;
	}
	
	/**
	 * @return the insumosMap
	 */
	public Object getInsumosMap(Object key) {
		return insumosMap.get(key);
	}

	/**
	 * @param insumosMap the insumosMap to set
	 */
	public void setInsumosMap(Object key, Object value) {
		this.insumosMap.put(key, value);
	}

	/**
	 * @return the medicamentosMap
	 */
	public Object getMedicamentosMap(Object key) {
		return medicamentosMap.get(key);
	}

	/**
	 * @param medicamentosMap the medicamentosMap to set
	 */
	public void setMedicamentosMap(Object key, Object value) {
		this.medicamentosMap.put(key, value);
	}

	/**
	 * @return the finalizarInsumos
	 */
	public String getFinalizarInsumos() {
		return finalizarInsumos;
	}

	/**
	 * @param finalizarInsumos the finalizarInsumos to set
	 */
	public void setFinalizarInsumos(String finalizarInsumos) {
		this.finalizarInsumos = finalizarInsumos;
	}

	/**
	 * @return the resumenAdminMap
	 */
	public HashMap getResumenAdminMap() {
		return resumenAdminMap;
	}

	/**
	 * @param resumenAdminMap the resumenAdminMap to set
	 */
	public void setResumenAdminMap(HashMap resumenAdminMap) {
		this.resumenAdminMap = resumenAdminMap;
	}

	/**
	 * @return the resumenAdminMap
	 */
	public Object getResumenAdminMap(Object key) {
		return resumenAdminMap.get(key);
	}

	/**
	 * @param resumenAdminMap the resumenAdminMap to set
	 */
	public void setResumenAdminMap(Object key, Object value) {
		this.resumenAdminMap.put(key, value);
	}
	
	/**
	 * @return the areaFiltro
	 */
	public int getAreaFiltro() {
		return areaFiltro;
	}

	/**
	 * @param areaFiltro the areaFiltro to set
	 */
	public void setAreaFiltro(int areaFiltro) {
		this.areaFiltro = areaFiltro;
	}

	/**
	 * @return the camaFiltro
	 */
	public int getCamaFiltro() {
		return camaFiltro;
	}

	/**
	 * @param camaFiltro the camaFiltro to set
	 */
	public void setCamaFiltro(int camaFiltro) {
		this.camaFiltro = camaFiltro;
	}

	/**
	 * @return the habitacionFiltro
	 */
	public int getHabitacionFiltro() {
		return habitacionFiltro;
	}

	/**
	 * @param habitacionFiltro the habitacionFiltro to set
	 */
	public void setHabitacionFiltro(int habitacionFiltro) {
		this.habitacionFiltro = habitacionFiltro;
	}

	/**
	 * @return the pisoFiltro
	 */
	public int getPisoFiltro() {
		return pisoFiltro;
	}

	/**
	 * @param pisoFiltro the pisoFiltro to set
	 */
	public void setPisoFiltro(int pisoFiltro) {
		this.pisoFiltro = pisoFiltro;
	}
	
	/**
	 * @return the areasTagMap
	 */
	public HashMap<String, Object> getAreasTagMap() {
		return areasTagMap;
	}

	/**
	 * @param areasTagMap the areasTagMap to set
	 */
	public void setAreasTagMap(HashMap<String, Object> areasTagMap) {
		this.areasTagMap = areasTagMap;
	}

	/**
	 * @return the camasTagMap
	 */
	public HashMap<Object, Object> getCamasTagMap() {
		return camasTagMap;
	}

	/**
	 * @param camasTagMap the camasTagMap to set
	 */
	public void setCamasTagMap(HashMap<Object, Object> camasTagMap) {
		this.camasTagMap = camasTagMap;
	}

	/**
	 * @return the habitacionesTagMap
	 */
	public HashMap<String, Object> getHabitacionesTagMap() {
		return habitacionesTagMap;
	}

	/**
	 * @param habitacionesTagMap the habitacionesTagMap to set
	 */
	public void setHabitacionesTagMap(HashMap<String, Object> habitacionesTagMap) {
		this.habitacionesTagMap = habitacionesTagMap;
	}

	/**
	 * @return the pisosTagMap
	 */
	public HashMap<String, Object> getPisosTagMap() {
		return pisosTagMap;
	}

	/**
	 * @param pisosTagMap the pisosTagMap to set
	 */
	public void setPisosTagMap(HashMap<String, Object> pisosTagMap) {
		this.pisosTagMap = pisosTagMap;
	}

////////
	
	/**
	 * @return the areasTagMap
	 */
	public Object getAreasTagMap(String key) {
		return areasTagMap.get(key);
	}

	/**
	 * @param areasTagMap the areasTagMap to set
	 */
	public void setAreasTagMap(String key, Object value) {
		this.areasTagMap.put(key, value);
	}

	/**
	 * @return the camasTagMap
	 */
	public Object getCamasTagMap(String key) {
		return camasTagMap.get(key);
	}

	/**
	 * @param camasTagMap the camasTagMap to set
	 */
	public void setCamasTagMap(String key, Object value) {
		this.camasTagMap.put(key, value);
	}

	/**
	 * @return the habitacionesTagMap
	 */
	public Object getHabitacionesTagMap(String key) {
		return habitacionesTagMap.get(key);
	}

	/**
	 * @param habitacionesTagMap the habitacionesTagMap to set
	 */
	public void setHabitacionesTagMap(String key, Object value) {
		this.habitacionesTagMap.put(key, value);
	}

	/**
	 * @return the pisosTagMap
	 */
	public Object getPisosTagMap(String key) {
		return pisosTagMap.get(key);
	}

	/**
	 * @param pisosTagMap the pisosTagMap to set
	 */
	public void setPisosTagMap(String key, Object value) {
		this.pisosTagMap.put(key, value);
	}

	/**
	 * @return the fechaFinalFiltro
	 */
	public String getFechaFinalFiltro() {
		return fechaFinalFiltro;
	}

	/**
	 * @param fechaFinalFiltro the fechaFinalFiltro to set
	 */
	public void setFechaFinalFiltro(String fechaFinalFiltro) {
		this.fechaFinalFiltro = fechaFinalFiltro;
	}

	/**
	 * @return the fechaInicialFiltro
	 */
	public String getFechaInicialFiltro() {
		return fechaInicialFiltro;
	}

	/**
	 * @param fechaInicialFiltro the fechaInicialFiltro to set
	 */
	public void setFechaInicialFiltro(String fechaInicialFiltro) {
		this.fechaInicialFiltro = fechaInicialFiltro;
	}

	/**
	 * @return the codigoEstadoMedico
	 */
	public int getCodigoEstadoMedico() {
		return codigoEstadoMedico;
	}

	/**
	 * @param codigoEstadoMedico the codigoEstadoMedico to set
	 */
	public void setCodigoEstadoMedico(int codigoEstadoMedico) {
		this.codigoEstadoMedico = codigoEstadoMedico;
	}

	public boolean isEntidadControlaDespachoSaldoMultidosis() {
		return entidadControlaDespachoSaldoMultidosis;
	}

	public void setEntidadControlaDespachoSaldoMultidosis(
			boolean entidadControlaDespachoSaldoMultidosis) {
		this.entidadControlaDespachoSaldoMultidosis = entidadControlaDespachoSaldoMultidosis;
	}

	public ArrayList<DtoAdministracionMedicamentosBasico> getInfoAdminSolicitud() {
		return infoAdminSolicitud;
	}

	public void setInfoAdminSolicitud(
			ArrayList<DtoAdministracionMedicamentosBasico> infoAdminSolicitud) {
		this.infoAdminSolicitud = infoAdminSolicitud;
	}

	public boolean isMostrarEncabezados() {
		return mostrarEncabezados;
	}

	public void setMostrarEncabezados(boolean mostrarEncabezados) {
		this.mostrarEncabezados = mostrarEncabezados;
	}

	public void setListadoViasIngreso(ArrayList<ViasIngreso> listadoViasIngreso) {
		this.listadoViasIngreso = listadoViasIngreso;
	}

	public ArrayList<ViasIngreso> getListadoViasIngreso() {
		return listadoViasIngreso;
	}

	public void setViaIngresoFiltro(int viaIngresoFiltro) {
		this.viaIngresoFiltro = viaIngresoFiltro;
	}

	public int getViaIngresoFiltro() {
		return viaIngresoFiltro;
	}

	public void setListaCentroCostosViaIngreso(
			ArrayList<DtoCentroCostoViaIngreso> listaCentroCostosViaIngreso) {
		this.listaCentroCostosViaIngreso = listaCentroCostosViaIngreso;
	}

	public ArrayList<DtoCentroCostoViaIngreso> getListaCentroCostosViaIngreso() {
		return listaCentroCostosViaIngreso;
	}

	public boolean isArticulosFinalizados() {
		return articulosFinalizados;
	}

	public void setArticulosFinalizados(boolean articulosFinalizados) {
		this.articulosFinalizados = articulosFinalizados;
	}

	public HashMap<String, Object> getInfoLoteMedicamentosMap() {
		return infoLoteMedicamentosMap;
	}

	public void setInfoLoteMedicamentosMap(
			HashMap<String, Object> infoLoteMedicamentosMap) {
		this.infoLoteMedicamentosMap = infoLoteMedicamentosMap;
	}

	/**
	 * @return the infoLoteMedicamentosMap
	 */
	public Object getInfoLoteMedicamentosMap(String key) {
		return infoLoteMedicamentosMap.get(key);
	}

	/**
	 * @param infoLoteMedicamentosMap the infoLoteMedicamentosMap to set
	 */
	public void setInfoLoteMedicamentosMap(String key, Object value) {
		this.infoLoteMedicamentosMap.put(key, value);
	}

	public HashMap<String, Object> getInfoLoteInsumosMap() {
		return infoLoteInsumosMap;
	}

	public void setInfoLoteInsumosMap(HashMap<String, Object> infoLoteInsumosMap) {
		this.infoLoteInsumosMap = infoLoteInsumosMap;
	}
	
	/**
	 * @return the infoLoteInsumosMap
	 */
	public Object getInfoLoteInsumosMap(String key) {
		return infoLoteInsumosMap.get(key);
	}

	/**
	 * @param infoLoteInsumosMap the infoLoteInsumosMap to set
	 */
	public void setInfoLoteInsumosMap(String key, Object value) {
		this.infoLoteInsumosMap.put(key, value);
	}

}