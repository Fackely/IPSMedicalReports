/*
 * Creado en Jun 14, 2005
 */
package com.princetonsa.actionform.hojaObstetrica;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.PersonaBasica;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;

/**
 * @author Andrés Mauricio Ruiz Vélez
 * Princeton S.A. (Parquesoft-Manizales)
 */
public class HojaObstetricaForm extends ValidatorForm implements Cloneable
{
	/**
	 * Para hacer logs de esta funcionalidad.
	 */	
	private Logger logger = Logger.getLogger(HojaObstetricaForm.class);
	
	/**
	 * Funcion Para poder pasar todos los valores de un Objeto Form a Otro 
	 */
	
	public Object clone()
	{
		HojaObstetricaForm obj=null;
		try
		{
			obj=(HojaObstetricaForm)super.clone();
		}
		catch(CloneNotSupportedException ex)
		{
			ex.printStackTrace();
		}
		//	---obj.origen=(Punto)obj.origen.clone();
		return obj;
	}
	
	/**
	 * Extensión para mostrar o ocultar encabezados
	 */
	private int cabezote=0;
	
	/**
	 * Este campo es para saber si esta consultando los embarazos anteriores
	 */
	private boolean consuHistoricos; 
	
	/**
	 * El numero del embarazo de la paciente
	 */
	private int numeroEmbarazo;
	
	/**
	 * Para saber cual embarazo consultar de todos los que tiene  1, 2 ó 3 etc 
	 */
	private int embarazo;
	
	  /**
	 * Manejo de estados para el flujo de la funcionalidad
	 */
	private String estado;
	
	/**
	 * Código de la hoja obstétrica anterior para la realización de la consulta
	 * de datos log
	 */
	private int codigoHojaObstetricaAnt;
	
	/**
	 * Campo de selección que activa o inactiva el campo número
	 * de embarazos.
	 */
	
	private boolean embarazada;
	
	
	/**
	  * Fecha de registro de la hoja obstétrica
	  */
	   private String fechaOrden;
	   
   /**
	  * Fecha de la última regla
	  */
	   private String fur;
	   
   /**
    * Fur temporal
    */
	   private String furTemp;
	   
   /**
	  * Fecha Probable del Parto
	  */
	   private String fpp;
	   
   /**
	  * Fecha de realización del Ultrasonido
	  */
	   private String fechaUltrasonido;
	   
   /**
	  * Fecha Probable de Parto de acuerdo al ultrasonido
	  */
	   private String fppUltrasonido;
	   
   /**
	  * Edad Gestacional del bebé
	  */
	   private int edadGestacional;
	   
   /**
	  * Edad al parte o del paciente 
	  */
	   private int edadParto;
	   
   /**
	  * Finalización del embarazo 
	  */
	   private boolean finalizacionEmbarazo;
	   
   /**
	  * Fecha de terminación
	  */
	   private String fechaTerminacion;
	   
	   /**
	    * Motivo de finalización que postula la información
	    * ingresada en el campo tipos partode antecedentes
	    * gineco obstetricos
	    */
	   private int motivoFinalizacion;
	   /**
	    *  Descripcion del motivo finalización
	    */
	   private String nombreMotivoFinalizacion;
	   
	   /**
	    * Campo para guardar el otro motivo finalización, cuando seleccionan
	    * otro en el motivo de finalización
	    */
	   private String otroMotivoFinalizacion;
	   
	   /**
	    * Campo que postula la información ingresada en el campo
	    * semanas de gestación del último registro en antecedentes
	    * gineco-obstétricos 
	    */
	   private int egFinalizar;
	   
	   //SECCION DE RESUMEN GESTACIONAL
	    
	   /**
	    * Se guardan los exámenes de laboratoriol de la hoja obstétrica
	    */
	   
	   private HashMap mapa=new HashMap();
	  
	   /** Edad Gestacional en el resumen  */
	   private int edadGestacionalResumen;   
	   
	   
	   /**
	    * Fecha del Resumen Gestacional
	    */
	   
	   private String fechaGestacional;
	   
	   /**
	    * Hora del Resumen Gestacional
	    */
	   
	   private String horaGestacional; 
	   
	   /**
		 * Manejo del histórico del resumen gestacional
		 */
		private Collection historicoResumenGestacional;
		
		/**
		 * Tipo de resumen gestacional parametrizados
		 */
		private Collection tiposResumenGestacional;
		
		/**
		 * Campo temporal para guardar la edad gestacional en el resumen
		 * para saber si se modificó o no
		 */
		private int edadGestResumenTemp;
		
		/**
		 * Campo temporal para guardar la altura uterina en el resumen
		 * para saber si se modificó o no
		 */
		private int alturaUterinaTemp;
		
		/**
	    * Campo para indicar que se va ha insertar en historico_examenes_lab
	    */
	   private boolean insercionExamen;
	   
	   /** Edad Gestacional en el exámen de laboratorio
		  */
		   private int edadGestacionalExamen;      
		  
		/**
		 * Edad Gestacional temporal en los exámenes de laboratorio
		 * para saber si modificaron 
		 */
		   private int edadGestExamenTemp;
		   
	   /**
	    * Campo para guardar las observaciones generales de la hoja obstétrica
	    */
	   
	   private String observacionesGralesNueva;
	   
	   /**
	    * Campo para guardar el nuevo registro de 
	    */
	   private String observacionesGrales;
	     
	   //*******************FIN DE LA SECCION RESUMEN GESTACIONAL *********************************//
	   
	  
	   //------------------------------CAMPOS PARA LA BUSQUEDA AVANZADA--------------------------------
	   /**
		 * Listado de las mujeres embarazadas
		 */
		private Collection listado;

		/***
		 * Para tener los nombres del paciente
		 */
		private String nombre;
	   
		/***
		 * Para tener los apellidos del pacientes
		 */
		private String apellido;		

		/***
		 * Para tener la cedula del paciente
		 */
		private String id;
		/**
		 * Tipo de identificacion 
		 */
		private String tipoId;
		
		/**
		 * Para tener edad del paciente 
		 */
		private int edad;
		/**
		 * Para tener el nombre del medico 
		 */
		private String nombreMedico;
		
		
		//----------------CAMPOS PARA REALIZAR LOS LOGS  DE MODIFICACIONES
		/**
		 * Contiene el log con info original para
		 * almacenar esta info en el log tipo Archivo
		 */
		private String logInfoOriginalHojaObstetrica;
		
		//------------------CAMPOS PARA EL ORDENAMIENTO DEL LISTADO DE EMBARAZADAS--------------
		
		private String ultimaPropiedad;
		private String propiedad;
		
		//SECCION PARA ADJUNTAR LOS DOCUMENTOS EN LOS EXAMENES DE LABORATORIO
		
		/**
		 * Colección con los nombres generados de los archivos adjuntos 
		 */
		private final Map documentosAdjuntosGenerados = new HashMap(); 
		
		/**
		 * Vector para guardar el número de documentos de los exámenes de laboratorio
		 */
		
		private Vector numDocumentosAdjuntos;
		
		/**
		 * Manejo de los examenes de laboratorio por embarazo incluyendo el nombre del tipo de embarazo
		 */
		private Collection examenesLaboratorioXEmbarazo;
		
		/**
		 * Fecha de los exámenes de laboratorio
		 */
		private String fechaExamen;
		
		/**
		 * Hora de los exámenes de laboratorio
		 */
		private String horaExamen;
				
		/**
		 * Manejo de los examenes de laboratorio por embarazo
		 */
		private Collection historicoExamenesLaboratorio;
		
		/**
		 * Histórico de los exámenes de laboratorio parametrizados
		 */
		private Collection historicoExamenesParam;
		
		/**
		 * Histórico de los nuevos exámes de laboratorio ingresados
		 */
		private Collection historicoNuevosExamenes;
		
		/**
		 * Es un string para saber desde que pagian se mando a cargar 
		 *
		 */
		private String pagina;
		
		/**
		 * Número de otros exámenes de laboratorio
		 */
		private int numeroOtros;
		
		/**
		 * Alerta a la seccion de examenes de laboratorio si hay error validate en la funcionalidad
		 */
		private boolean errorExamenes;
		
		/**
		 * Alerta a la seccion de ultrasonidos
		 */
		private boolean errorUltraSonido;
		
		//************************************SECCION DE ULTRASONIDOS*****************************************//
		
		/**
		 * Fecha del ultrasonido
		 */
		private String ultrasonidoFecha;
		
		/**
		 * Hora del ultrasonido
		 */
		private String ultrasonidoHora;

		/**
		 * Para el manejo de los tipo s de untrasonidos 
		 */
		private Collection tiposUltrasonido;

		/**
		 * Para el manejo de los tipo s de untrasonidos Historicos 
		 */
		private Collection historicoUltrasonido;
		
		/**
		 * Edad Gestacional temporal en ultrasonidos
		 * para saber si modificó el valor 
		 */
		   private int edadGestUltrasonidoTemp;
		  
	   /**
		 * Edad Gestacional Eco temporal en ultrasonidos
		 * para saber si modificó el valor 
		 */
		   private int edadGestEcoUltraTemp;
		
		
		   
		   private String temp="";
		   
	//************************************SECCION DE PLAN MANEJO*****************************************//
    /**
     * Campo para indicar en la información del embarazo si es o no confiable
     */
	 private String confiable;
	 
	 /**
	  * Campo de vigente antitetanica
	 */
	 private String vigenteAntitetanica;
	 
	 /**
	  * Campo de las dosis de antitetanica (select) 
	  */
	 private String dosisAntitetanica;
	 
	 /**
	  * Campo de los meses de gestacion de antitetanica
	  */
	 private String mesesGestacionAntitetanica;
	 
	 /**
	  * Campo para guardar la antirubeola (select)
	  */
	 private String antirubeola;
	 
	 /**
	  * Campo peso
	  */
	 private String peso;
	 
	 /**
	  * Campo talla
	  */
	 private String talla;
	 
	 /**
	  * Campo SI/NO embarazo deseado
	  */
	 private String embarazoDeseado;
	 
	 /**
	  * Campo SI/NO embarazo planeado
	  */
	 private String embarazoPlaneado;
	 
	 /**
	  * Mapa que consulta los tipos de plan de manejo parametrizados 
	  * para la institución
	  */
	 private HashMap mapaTiposPlanManejo;
	 
	 /**
	  * Mapa para guardar la información que se ingresa en la sección plan manejo
	  */
	 private HashMap mapaPlanManejo;
	 
	 /**
	  * Mapa que guarda la información historica de plan manejo consultada
	  */
	 private HashMap mapaHistoPlanManejo;
	 
	 /**
	 * Indice para el recorrido del juego de información de la seccion
	 * plan de manejo
	 */
	 private int indicePlanManejo;
	 
		   
		/**
		 * Este método inicializa en valores vacíos, -mas no nulos- los atributos de este objeto.
		 */
		public void reset() 
		{
			this.numeroEmbarazo=0;
			this.embarazada=false;
			this.fechaOrden=UtilidadFecha.getFechaActual();
			this.fur="";
			this.fpp="";
			this.fechaUltrasonido="";
			this.fppUltrasonido="";
			this.edadGestacional=0;
			this.edadParto=0;
			this.finalizacionEmbarazo=false;
			this.fechaTerminacion="";
			this.motivoFinalizacion=0;
			this.nombreMotivoFinalizacion="";
			this.otroMotivoFinalizacion="";
			this.egFinalizar=0;	
			this.edadGestacionalResumen=0;
			this.fechaGestacional=UtilidadFecha.getFechaActual();
			this.horaGestacional=UtilidadFecha.getHoraActual();
			this.insercionExamen=false;
			this.edadGestacionalExamen=0;
			this.mapa=new HashMap();
			this.observacionesGrales="";
			this.observacionesGralesNueva="";
			this.documentosAdjuntosGenerados.clear();
			this.numDocumentosAdjuntos=new Vector();
			this.consuHistoricos = false;    
			
			//-Campos de la busqueda avanzada
			this.nombre = "";
			this.apellido="";		
			this.id="";
			this.tipoId="";
			this.edad=0;
			this.nombreMedico="";		
			
			this.examenesLaboratorioXEmbarazo= new ArrayList();
			this.historicoExamenesLaboratorio=new ArrayList();
			this.historicoExamenesParam=new ArrayList();
			this.historicoNuevosExamenes=new ArrayList();
			this.historicoResumenGestacional=new ArrayList();
			this.fechaExamen=UtilidadFecha.getFechaActual();
			this.horaExamen=UtilidadFecha.getHoraActual();
			this.tiposResumenGestacional=new ArrayList();
			this.ultrasonidoFecha=UtilidadFecha.getFechaActual();
			this.ultrasonidoHora=UtilidadFecha.getHoraActual();
			this.historicoUltrasonido=new ArrayList();
			this.tiposUltrasonido=new ArrayList();
			this.edadGestResumenTemp=0;
			this.edadGestExamenTemp=0;
			this.alturaUterinaTemp=0;
			this.edadGestUltrasonidoTemp=0;
			this.edadGestEcoUltraTemp=0;
			this.errorExamenes = false;
			this.errorUltraSonido = false;
			this.furTemp="";
			this.numeroOtros=0;
			
			this.setMapa("codigosOtros","");
									
			//this.cabezote=0;
			pagina = "";
			
			//--------Sección Plan Manejo -----------------//
			this.confiable=ConstantesBD.acronimoSi;
			this.vigenteAntitetanica=ConstantesBD.acronimoSi;
			this.dosisAntitetanica="";
			this.mesesGestacionAntitetanica="";
			this.antirubeola="";
			this.peso = "";
			this.talla = "";
			this.embarazoDeseado = "";
			this.embarazoPlaneado = "";
			
			this.mapaTiposPlanManejo=new HashMap();
			this.mapaTiposPlanManejo.put("numRegistros","0");
			this.mapaPlanManejo=new HashMap();
			this.mapaPlanManejo.put("numRegistros","0");
			this.mapaHistoPlanManejo=new HashMap();
			this.mapaHistoPlanManejo.put("numRegistros","0");
			
			this.indicePlanManejo=0;
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
			logger.info("examenesLab=> "+this.mapa);
			if(estado.equals("salir") || estado.equals("insertado"))
			{
				errores=super.validate(mapping,request);
				int numEmbarazo=0;
				
				Connection con=null;
				try
				{
						con = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection();	
				}
				catch(SQLException e)
				{
						logger.warn("No se pudo abrir la conexión"+e.toString());
				}
				
				HttpSession session=request.getSession();
				PersonaBasica paciente = (PersonaBasica)session.getAttribute("pacienteActivo");
				

				String numEmbarazos = Utilidades.obtenerNrosEmbarazosInformacionParto(con, paciente.getCodigoPersona()); 

				//--Validar que la no tenga ese numero de embarazo en la funcionalidad informacion Partos.
				if (this.numeroEmbarazo>0 && !numEmbarazos.equals(""))
				{
					String partos [] = numEmbarazos.split(ConstantesBD.separadorSplit);
					for (int i = 0; i < partos.length; i++)
					{					
						if ( UtilidadCadena.vInt(partos[i]) == this.numeroEmbarazo )
						{
							errores.add("embarazoInformacion", new ActionMessage("errors.numeroEmbarazoRegistradoInfoParto"));
						}
					}
				}
				
				numEmbarazo=Utilidades.obtenerUltimoNumeroEmbarazo(con, paciente.getCodigoPersona());
				if (estado.equals("salir"))
				{
					//Validar que el número del embarazo sea mayor a cero
					if(numeroEmbarazo==0)
						{
						errores.add("Campo Número embarazo vacio", new ActionMessage("errors.required","El campo Número del embarazo"));
						}
					else
					{
						if(numeroEmbarazo <= numEmbarazo)
							{
								errores.add("Número del embarazo", new ActionMessage("errors.numeroEmbarazoMenor"));
							}
					}
					
					
				}


				
				//Fecha actual y patrón de fecha a utilizar en las validaciones
				final Date fechaActual = new Date();
				final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
				
				//VALIDACION DE LA FECHA ORDEN HOJA OBSTETRICA
				if(fechaOrden.trim().equals(""))
				{	
						errores.add("Campo Fecha Orden de la Hoja Obstétrica vacio", new ActionMessage("errors.required","El campo Fecha Orden de la Hoja Obstétrica"));
				}
				else
				{
						if(!UtilidadFecha.validarFecha(fechaOrden))
						{
								errores.add("Fecha Orden de la Hoja Obstetrica", new ActionMessage("errors.formatoFechaInvalido", " Orden de la Hoja Obstétrica"));							
						}
						else
						{
					
							boolean tieneErroresFecha=false;
							 
							//Fecha orden de la hoja obstétrica
							Date fechaOrden = null;
											
							try 
							{
								fechaOrden = dateFormatter.parse(this.fechaOrden);
							}	
							catch (java.text.ParseException e) 
							{
								tieneErroresFecha=true;
							}
							
							if (!tieneErroresFecha)
							{
								// Validar que la fecha orden hoja obstetrica no sea superior a la fecha actual
								if (fechaActual.compareTo(fechaOrden) < 0) 
								{
									errores.add("fechaOrden", new ActionMessage("errors.fechaPosteriorAOtraDeReferencia", "de la hoja obstétrica", "actual"));
								}
							}
						}
				   }//ELSE FECHA ORDEN
				
				//VALIDACION DE LA FECHA ULTIMA REGLA
				if(!fur.trim().equals(""))
				{	
					if(!UtilidadFecha.validarFecha(fur))
						{
								errores.add("Fecha Última Regla", new ActionMessage("errors.formatoFechaInvalido", " Última Regla"));							
						}
						else
						{
							boolean tieneErroresFecha=false;
							 
							//Fecha de la última regla
							Date fur = null;
											
							try 
							{
								fur = dateFormatter.parse(this.fur);
							}	
							catch (java.text.ParseException e) 
							{
								tieneErroresFecha=true;
							}
							
							if (!tieneErroresFecha)
							{
								// Validar que la fecha de la ultima regla no sea superior a la fecha actual
								if (fechaActual.compareTo(fur) < 0) 
								{
									errores.add("fur", new ActionMessage("errors.fechaPosteriorAOtraDeReferencia", "Última Regla", "actual"));
								}
							}
						}
				   }//IF FUR DIFERENTE DE VACIO
				
				if(!this.confiable.equals(ConstantesBD.acronimoSi)&&!this.confiable.equals(ConstantesBD.acronimoNo))
					errores.add("Vigente vacio", new ActionMessage("errors.required","El campo Confiable"));
				
				//VALIDACION DE LA FECHA PROBABLE DEL PARTO
				if(!fpp.trim().equals(""))
				{	
					if(!UtilidadFecha.validarFecha(fpp))
						{
								errores.add("Fecha Probable del Parto", new ActionMessage("errors.formatoFechaInvalido", " Probable del Parto"));							
						}	
				   }
				
				//VALIDACION DE LA FECHA DEL ULTRASONIDO
				logger.info("fechaUltrasonido=>"+fechaUltrasonido+"*");
				if(!fechaUltrasonido.trim().equals(""))
				{	
					if(!UtilidadFecha.validarFecha(fechaUltrasonido))
						{
								errores.add("Fecha Ultrasonido", new ActionMessage("errors.formatoFechaInvalido", " Ultrasonido"));							
						}
						else
						{
							boolean tieneErroresFecha=false;
							 
							//Fecha Ultrasonido
							Date fechaUltrasonido = null;
											
							try 
							{
								fechaUltrasonido = dateFormatter.parse(this.fechaUltrasonido);
							}	
							catch (java.text.ParseException e) 
							{
								tieneErroresFecha=true;
							}
							
							if (!tieneErroresFecha)
							{
								// Validar que la fecha del Ultrasonido no sea superior a la fecha actual
								if (fechaActual.compareTo(fechaUltrasonido) < 0) 
								{
									errores.add("fechaUltrasonido", new ActionMessage("errors.fechaPosteriorAOtraDeReferencia", "Ultrasonido", "actual"));
								}
							}
						}
				   }//IF FECHA ULTRASONIDO DIFERENTE DE VACIO
				
				//VALIDACION DE LA FPP ULTRASONIDO
				if(!fppUltrasonido.trim().equals(""))
				{	
					if(!UtilidadFecha.validarFecha(fppUltrasonido))
						{
								errores.add("FPP por Ultrasonido", new ActionMessage("errors.formatoFechaInvalido", " FPP por Ultrasonido"));							
						}	
				   }
				
				//VALIDACION DE LA FECHA DE TERMINACIÓN
				if(!fechaTerminacion.trim().equals(""))
				{	
					if(!UtilidadFecha.validarFecha(fechaTerminacion))
						{
								errores.add("Fecha de Terminación", new ActionMessage("errors.formatoFechaInvalido", " de Terminación"));							
						}
						else
						{
							boolean tieneErroresFecha=false;
							 
							//Fecha de Terminación
							Date fechaTerminacion = null;
											
							try 
							{
								fechaTerminacion = dateFormatter.parse(this.fechaTerminacion);
							}	
							catch (java.text.ParseException e) 
							{
								tieneErroresFecha=true;
							}
							
							if (!tieneErroresFecha)
							{
								// Validar que la fecha de Terminación no sea superior a la fecha actual
								if (fechaActual.compareTo(fechaTerminacion) < 0) 
								{
									errores.add("fechaTerminacion", new ActionMessage("errors.fechaPosteriorAOtraDeReferencia", "de Terminación", "actual"));
								}
							}
						}
				   }//IF FECHA DE TERMINACION DIFERENTE DE VACIO
				
				//VALIDACION DE LA FECHA EN EL RESUMEN GESTACIONAL
				if(fechaGestacional!=null && !fechaGestacional.trim().equals(""))
				{	
					if(!UtilidadFecha.validarFecha(fechaGestacional))
						{
								errores.add("Fecha Resumen Gestacional", new ActionMessage("errors.formatoFechaInvalido", "Resumen Gestacional"));							
						}
						else
						{
							boolean tieneErroresFecha=false;
							 
							//Fecha del exámen de laboratorio
							Date fechaGestacional = null;
											
							try 
							{
								fechaGestacional = dateFormatter.parse(this.fechaGestacional);
							}	
							catch (java.text.ParseException e) 
							{
								tieneErroresFecha=true;
							}
							
							if (!tieneErroresFecha)
							{
								// Validar que la fecha del exámen de laboratorio no sea superior a la fecha actual
								if (fechaActual.compareTo(fechaGestacional) < 0) 
								{
									errores.add("fechaGestacional", new ActionMessage("errors.fechaPosteriorAOtraDeReferencia", "Resumen Gestacional", "actual"));
								}
							}
						}
				   }//IF FECHA DE RESUMEN GESTACIONAL DIFERENTE DE VACIO
				
				//VALIDACION DE LA FECHA EN LOS EXAMENES DE LABORATORIO
				if(!fechaExamen.trim().equals(""))
				{	
					if(!UtilidadFecha.validarFecha(fechaExamen))
						{
								errores.add("Fecha Exámen de Laboratorio", new ActionMessage("errors.formatoFechaInvalido", "Exámen de Laboratorio"));							
						}
						else
						{
							boolean tieneErroresFecha=false;
							 
							//Fecha del exámen de laboratorio
							Date fechaExamen = null;
											
							try 
							{
								fechaExamen = dateFormatter.parse(this.fechaExamen);
							}	
							catch (java.text.ParseException e) 
							{
								tieneErroresFecha=true;
							}
							
							if (!tieneErroresFecha)
							{
								// Validar que la fecha del exámen de laboratorio no sea superior a la fecha actual
								if (fechaActual.compareTo(fechaExamen) < 0) 
								{
									errores.add("fechaExamen", new ActionMessage("errors.fechaPosteriorAOtraDeReferencia", "Exámen de Laboratorio", "actual"));
								}
							}
						}
				   }//IF FECHA DE EXÁMEN DE LABORATORIO DIFERENTE DE VACIO
				
				//VALIDACION DE LA FECHA EN LOS ULTRASONIDOS
				if(!ultrasonidoFecha.trim().equals(""))
				{	
					if(!UtilidadFecha.validarFecha(ultrasonidoFecha))
						{
								errores.add("Fecha del Ultrasonido", new ActionMessage("errors.formatoFechaInvalido", "Ultrasonido"));							
						}
						else
						{
							boolean tieneErroresFecha=false;
							 
							//Fecha del ultrasonido
							Date ultrasonidoFecha = null;
											
							try 
							{
								ultrasonidoFecha = dateFormatter.parse(this.ultrasonidoFecha);
							}	
							catch (java.text.ParseException e) 
							{
								tieneErroresFecha=true;
							}
							
							if (!tieneErroresFecha)
							{
								// Validar que la fecha del ultrasonido no sea superior a la fecha actual
								if (fechaActual.compareTo(ultrasonidoFecha) < 0) 
								{
									errores.add("ultrasonidoFecha", new ActionMessage("errors.fechaPosteriorAOtraDeReferencia", "Ultrasonido", "actual"));
								}
							}
						}
				   }//IF FECHA DE ULTRASONIDO DIFERENTE DE VACIO
				
				if(!this.vigenteAntitetanica.equals(ConstantesBD.acronimoSi)&&!this.vigenteAntitetanica.equals(ConstantesBD.acronimoNo))
					errores.add("Vigente vacio", new ActionMessage("errors.required","El campo Vigente"));
				
				//Verificar que hayan seleccionado una dosis de antitetanico
				if(!UtilidadCadena.noEsVacio(this.dosisAntitetanica)&&UtilidadTexto.getBoolean(this.vigenteAntitetanica))
				{
					errores.add("Dosis Antitetanico vacio", new ActionMessage("errors.required","El campo Dosis Antitetánica"));
				}
				
				//Verificar que hayan seleccionado el tipo de antirubeola
				if(!UtilidadCadena.noEsVacio(this.antirubeola))
				{
				errores.add("Antirubeola vacio", new ActionMessage("errors.required","El campo Antirubeola"));
				}
				
				//Validacion del peso
				if(this.peso.equals(""))
					errores.add("Peso requerido", new ActionMessage("errors.required","El peso"));
				else if(Integer.parseInt(this.peso)<=0)
					errores.add("Peso requerido", new ActionMessage("errors.integerMayorQue","El peso","0"));
				
				//Validacion de la talla
				if(this.talla.equals(""))
					errores.add("Talla requerida", new ActionMessage("errors.required","La talla"));
				else if(Integer.parseInt(this.talla)<=0)
					errores.add("Talla requerida", new ActionMessage("errors.integerMayorQue","La talla","0"));
				
				//Verificacion del campo embarazo deseado
				if(this.embarazoDeseado==null||this.embarazoDeseado.equals(""))
					errores.add("Embarazo deseado", new ActionMessage("errors.required","El campo Embarazo Deseado"));
				
				//Verificacion del campo embarazo planeadi
				if(this.embarazoPlaneado==null||this.embarazoPlaneado.equals(""))
					errores.add("Embarazo planeado", new ActionMessage("errors.required","El campo Embarazo Planeado"));

				try
				{
					UtilidadBD.cerrarConexion(con);
				}
				catch (SQLException e1)
				{
					logger.error("Error cerrando la conexión : "+e1);
				}
			} // IF ESTADO SALIR
			
			if(!errores.isEmpty())
			{
				if(estado.equals("salir"))
					this.setEstado("empezar");
				
				this.errorExamenes = true;
				this.errorUltraSonido = true;
				
			}
			else
			{
				this.errorExamenes = false;
				this.errorUltraSonido = false;
			}
			return errores;
			
		}
/**
 * @return Returns the edadGestacional.
 */
public int getEdadGestacional()
{
	return edadGestacional;
}
/**
 * @param edadGestacional The edadGestacional to set.
 */
public void setEdadGestacional(int edadGestacional)
{
	this.edadGestacional = edadGestacional;
}
/**
 * @return Returns the edadParto.
 */
public int getEdadParto()
{
	return edadParto;
}
/**
 * @param edadParto The edadParto to set.
 */
public void setEdadParto(int edadParto)
{
	this.edadParto = edadParto;
}
	/**
	 * @return Returns the egFinalizar.
	 */
	public int getEgFinalizar()
	{
		return egFinalizar;
	}
	/**
	 * @param egFinalizar The egFinalizar to set.
	 */
	public void setEgFinalizar(int egFinalizar)
	{
		this.egFinalizar = egFinalizar;
	}
	/**
	 * @return Returns the estado.
	 */
	public String getEstado()
	{
		return estado;
	}
	/**
	 * @param estado The estado to set.
	 */
	public void setEstado(String estado)
	{
		this.estado = estado;
	}
	/**
	 * @return Returns the fechaOrden.
	 */
	public String getFechaOrden()
	{
		return fechaOrden;
	}
	/**
	 * @param fechaOrden The fechaOrden to set.
	 */
	public void setFechaOrden(String fechaOrden)
	{
		this.fechaOrden = fechaOrden;
	}
/**
 * @return Returns the fechaTerminacion.
 */
public String getFechaTerminacion()
{
	return fechaTerminacion;
}
/**
 * @param fechaTerminacion The fechaTerminacion to set.
 */
public void setFechaTerminacion(String fechaTerminacion)
{
	this.fechaTerminacion = fechaTerminacion;
}
/**
 * @return Returns the fechaUltrasonido.
 */
public String getFechaUltrasonido()
{
	return fechaUltrasonido;
}
/**
 * @param fechaUltrasonido The fechaUltrasonido to set.
 */
public void setFechaUltrasonido(String fechaUltrasonido)
{
	this.fechaUltrasonido = fechaUltrasonido;
}
/**
 * @return Returns the finalizacionEmbarazo.
 */
public boolean isFinalizacionEmbarazo()
{
	return finalizacionEmbarazo;
}
/**
 * @param finalizacionEmbarazo The finalizacionEmbarazo to set.
 */
public void setFinalizacionEmbarazo(boolean finalizacionEmbarazo)
{
	this.finalizacionEmbarazo = finalizacionEmbarazo;
}
/**
 * @return Returns the fpp.
 */
public String getFpp()
{
	return fpp;
}
/**
 * @param fpp The fpp to set.
 */
public void setFpp(String fpp)
{
	this.fpp = fpp;
}
/**
 * @return Returns the fppUltrasonido.
 */
public String getFppUltrasonido()
{
	return fppUltrasonido;
}
/**
 * @param fppUltrasonido The fppUltrasonido to set.
 */
public void setFppUltrasonido(String fppUltrasonido)
{
	this.fppUltrasonido = fppUltrasonido;
}
/**
 * @return Returns the fur.
 */
public String getFur()
{
	return fur;
}
/**
 * @param fur The fur to set.
 */
public void setFur(String fur)
{
	this.fur = fur;
}
	/**
	 * @return Returns the motivoFinalizacion.
	 */
	public int getMotivoFinalizacion()
	{
		return motivoFinalizacion;
	}
	/**
	 * @param motivoFinalizacion The motivoFinalizacion to set.
	 */
	public void setMotivoFinalizacion(int motivoFinalizacion)
	{
		this.motivoFinalizacion = motivoFinalizacion;
	}
	/**
	 * @return Returns the otroMotivoFinalizacion.
	 */
	public String getOtroMotivoFinalizacion()
	{
		return otroMotivoFinalizacion;
	}
	/**
	 * @param otroMotivoFinalizacion The otroMotivoFinalizacion to set.
	 */
	public void setOtroMotivoFinalizacion(String otroMotivoFinalizacion)
	{
		this.otroMotivoFinalizacion = otroMotivoFinalizacion;
	}
	/**
	 * @return Returns the embarazada.
	 */
	public boolean isEmbarazada()
	{
		return embarazada;
	}
	/**
	 * @param embarazada The embarazada to set.
	 */
	public void setEmbarazada(boolean embarazada)
	{
		this.embarazada = embarazada;
	}
	
	/**
	 * @return Returns the fechaGestacional.
	 */
	public String getFechaGestacional()
	{
		return fechaGestacional;
	}
	/**
	 * @param fechaGestacional The fechaGestacional to set.
	 */
	public void setFechaGestacional(String fechaGestacional)
	{
		this.fechaGestacional = fechaGestacional;
	}
	/**
	 * @return Returns the horaGestacional.
	 */
	public String getHoraGestacional()
	{
		return horaGestacional;
	}
	/**
	 * @return Returns the insercionExamen.
	 */
	public boolean isInsercionExamen()
	{
		return insercionExamen;
	}
	/**
	 * @param insercionExamen The insercionExamen to set.
	 */
	public void setInsercionExamen(boolean insercionExamen)
	{
		this.insercionExamen = insercionExamen;
	}
	/**
	 * @return Returns the edadGestacionalExamen.
	 */
	public int getEdadGestacionalExamen()
	{
		return edadGestacionalExamen;
	}
	/**
	 * @param edadGestacionalExamen The edadGestacionalExamen to set.
	 */
	public void setEdadGestacionalExamen(int edadGestacionalExamen)
	{
		this.edadGestacionalExamen = edadGestacionalExamen;
	}
		/**
		 * @return Returns the fechaExamen.
		 */
		public String getFechaExamen()
		{
			return fechaExamen;
		}
		/**
		 * @param fechaExamen The fechaExamen to set.
		 */
		public void setFechaExamen(String fechaExamen)
		{
			this.fechaExamen = fechaExamen;
		}
		/**
		 * @return Returns the horaExamen.
		 */
		public String getHoraExamen()
		{
			return horaExamen;
		}
		/**
		 * @param horaExamen The horaExamen to set.
		 */
		public void setHoraExamen(String horaExamen)
		{
			this.horaExamen = horaExamen;
		}
	/**
	 * @param horaGestacional The horaGestacional to set.
	 */
	public void setHoraGestacional(String horaGestacional)
	{
		this.horaGestacional = horaGestacional;
	}
	
	/**
	 * @return Returns the mapa.
	 */
	public HashMap getMapaCompleto()
	{
		return mapa;
	}
	/**
	 * @param mapa The mapa to set.
	 */
	public void setMapaCompleto(HashMap examenesLaboratorio)
	{
		this.mapa = examenesLaboratorio;
	}
	/**
	 * @return Returna la propiedad del mapa mapa.
	 */
	public Object getMapa(String key)
	{
		return mapa.get(key);
	}
	/**
	 * @param Asigna la propiedad al mapa
	 */
	public void setMapa(String key, Object value)
	{
		this.mapa.put(key, value);
	}

	/**
	 * @return Returns the observacionesGrales.
	 */
	public String getObservacionesGrales()
	{
		return observacionesGrales;
	}
	/**
	 * @param observacionesGrales The observacionesGrales to set.
	 */
	public void setObservacionesGrales(String observacionesGrales)
	{
		this.observacionesGrales = observacionesGrales;
	}
	
		/**
	 * @return Returns the edadGestacionalResumen.
	 */
	public int getEdadGestacionalResumen()
	{
		return edadGestacionalResumen;
	}
	/**
	 * @param edadGestacionalResumen The edadGestacionalResumen to set.
	 */
	public void setEdadGestacionalResumen(int edadGestacionalResumen)
	{
		this.edadGestacionalResumen = edadGestacionalResumen;
	}
	
	/**
	 * @return Retorna listado.
	 */
	public Collection getListado() {
		return listado;
	}
	/**
	 * @param Asigna listado.
	 */
	public void setListado(Collection listado) {
		this.listado = listado;
	}
	
		/**
		 * @return Retorna apellido.
		 */
		public String getApellido() {
			return apellido;
		}
		/**
		 * @param Asigna apellido.
		 */
		public void setApellido(String apellido) {
			this.apellido = apellido;
		}
		/**
		 * @return Retorna edad.
		 */
		public int getEdad() {
			return edad;
		}
		/**
		 * @param Asigna edad.
		 */
		public void setEdad(int edad) {
			this.edad = edad;
		}
		/**
		 * @return Retorna id.
		 */
		public String getId() {
			return id;
		}
		/**
		 * @param Asigna id.
		 */
		public void setId(String id) {
			this.id = id;
		}
		/**
		 * @return Retorna nombre.
		 */
		public String getNombre() {
			return nombre;
		}
		/**
		 * @param Asigna nombre.
		 */
		public void setNombre(String nombre) {
			this.nombre = nombre;
		}
		/**
		 * @return Retorna nombreMedico.
		 */
		public String getNombreMedico() {
			return nombreMedico;
		}
		/**
		 * @param Asigna nombreMedico.
		 */
		public void setNombreMedico(String nombreMedico) {
			this.nombreMedico = nombreMedico;
		}
		
		/**
		 * @return Retorna propiedad.
		 */
		public String getPropiedad() {
			return propiedad;
		}
		/**
		 * @param Asigna propiedad.
		 */
		public void setPropiedad(String propiedad) {
			this.propiedad = propiedad;
		}
		/**
		 * @return Retorna ultimaPropiedad.
		 */
		public String getUltimaPropiedad() {
			return ultimaPropiedad;
		}
		/**
		 * @param Asigna ultimaPropiedad.
		 */
		public void setUltimaPropiedad(String ultimaPropiedad) {
			this.ultimaPropiedad = ultimaPropiedad;
		}
		
		
		/**
		 * Retorna el nombre generado del documento adjunto
		 * @param key
		 * @return Object
		 */
		public Object getDocumentoAdjuntoGenerado(String key)
		{
			return documentosAdjuntosGenerados.get(key);
		}
		
		/**
		 * Asigna el nombre generado del documento adjunto bajo la llave dada
		 * @param key
		 * @param value
		 */
		public void setDocumentoAdjuntoGenerado(String key, Object value) 
		{
			String val = (String) value;
			
			if (val != null) 
				val = val.trim();

			documentosAdjuntosGenerados.put(key, value);
		}	
		
		/**
		 * @return Returns the numDocumentosAdjuntos.
		 */
		public Vector getNumDocumentosAdjuntos()
		{
			return numDocumentosAdjuntos;
		}
		/**
		 * @param numDocumentosAdjuntos The numDocumentosAdjuntos to set.
		 */
		public void setNumDocumentosAdjuntos(Vector numDocumentosAdjuntos)
		{
			this.numDocumentosAdjuntos = numDocumentosAdjuntos;
		}
	
		/**
		 * @return Retorna logInfoOriginalHojaObstetrica.
		 */
		public String getLogInfoOriginalHojaObstetrica() {
			return logInfoOriginalHojaObstetrica;
		}
		/**
		 * @param Asigna logInfoOriginalHojaObstetrica.
		 */
		public void setLogInfoOriginalHojaObstetrica(
				String logInfoOriginalHojaObstetrica) {
			this.logInfoOriginalHojaObstetrica = logInfoOriginalHojaObstetrica;
		}
	/**
	 * @return Returns the codigoHojaObstetricaAnt.
	 */
	public int getCodigoHojaObstetricaAnt()
	{
		return codigoHojaObstetricaAnt;
	}
	/**
	 * @param codigoHojaObstetricaAnt The codigoHojaObstetricaAnt to set.
	 */
	public void setCodigoHojaObstetricaAnt(int codigoHojaObstetricaAnt)
	{
		this.codigoHojaObstetricaAnt = codigoHojaObstetricaAnt;
	}
	/**
	 * @return Retorna numeroEmbarazo.
	 */
	public int getNumeroEmbarazo() {
		return numeroEmbarazo;
	}
	/**
	 * @param Asigna numeroEmbarazo.
	 */
	public void setNumeroEmbarazo(int numeroEmbarazo) {
		this.numeroEmbarazo = numeroEmbarazo;
	}
	/**
	 * @return Retorna embarazo.
	 */
	public int getEmbarazo() {
		return embarazo;
	}
	/**
	 * @param Asigna embarazo.
	 */
	public void setEmbarazo(int embarazo) {
		this.embarazo = embarazo;
	}
	
	/**
	 * @return Retorna consuHistoricos.
	 */
	public boolean getConsuHistoricos() {
		return consuHistoricos;
	}
	/**
	 * @param Asigna consuHistoricos.
	 */
	public void setConsuHistoricos(boolean consuHistoricos) {
		this.consuHistoricos = consuHistoricos;
	}

	/**
	 * @return Retorna observacionesGralesNueva.
	 */
	public String getObservacionesGralesNueva() {
		return observacionesGralesNueva;
	}
	/**
	 * @param Asigna observacionesGralesNueva.
	 */
	public void setObservacionesGralesNueva(String observacionesGralesNueva) {
		this.observacionesGralesNueva = observacionesGralesNueva;
	}
	/**
	 * @return Retorna examenesLaboratorioXEmbarazo.
	 */
	public Collection getExamenesLaboratorioXEmbarazo()
	{
		return examenesLaboratorioXEmbarazo;
	}
	/**
	 * @param examenesLaboratorioXEmbarazo Asigna examenesLaboratorioXEmbarazo.
	 */
	public void setExamenesLaboratorioXEmbarazo(
			Collection examenesLaboratorioXEmbarazo)
	{
		this.examenesLaboratorioXEmbarazo = examenesLaboratorioXEmbarazo;
	}
		/**
		 * @return Returns the historicoExamenesLaboratorio.
		 */
		public Collection getHistoricoExamenesLaboratorio()
		{
			return historicoExamenesLaboratorio;
		}
		/**
		 * @param historicoExamenesLaboratorio The historicoExamenesLaboratorio to set.
		 */
		public void setHistoricoExamenesLaboratorio(
				Collection historicoExamenesLaboratorio)
		{
			this.historicoExamenesLaboratorio = historicoExamenesLaboratorio;
		}
		/**
		 * @return Returns the historicoExamenesParam.
		 */
		public Collection getHistoricoExamenesParam()
		{
			return historicoExamenesParam;
		}
		/**
		 * @param historicoExamenesParam The historicoExamenesParam to set.
		 */
		public void setHistoricoExamenesParam(Collection historicoExamenesParam)
		{
			this.historicoExamenesParam = historicoExamenesParam;
		}
		/**
		 * @return Returns the historicoNuevosExamenes.
		 */
		public Collection getHistoricoNuevosExamenes()
		{
			return historicoNuevosExamenes;
		}
		/**
		 * @param historicoNuevosExamenes The historicoNuevosExamenes to set.
		 */
		public void setHistoricoNuevosExamenes(
				Collection historicoNuevosExamenes)
		{
			this.historicoNuevosExamenes = historicoNuevosExamenes;
		}
	/**
	 * @return Returns the historicoResumenGestacional.
	 */
	public Collection getHistoricoResumenGestacional()
	{
		return historicoResumenGestacional;
	}
	/**
	 * @param historicoResumenGestacional The historicoResumenGestacional to set.
	 */
	public void setHistoricoResumenGestacional(
			Collection historicoResumenGestacional)
	{
		this.historicoResumenGestacional = historicoResumenGestacional;
	}
		/**
		 * @return Retorna tipoId.
		 */
		public String getTipoId() {
			return tipoId;
		}
		/**
		 * @param Asigna tipoId.
		 */
		public void setTipoId(String tipoId) {
			this.tipoId = tipoId;
		}
	/**
	 * @return Retorna extension.
	 */
	public int getCabezote()
	{
		return cabezote;
	}
	/**
	 * @param extension Asigna extension.
	 */
	public void setCabezote(int extension)
	{
		this.cabezote = extension;
	}
		/**
		 * @return Retorna pagina.
		 */
		public String getPagina() {
			return pagina;
		}
		/**
		 * @param Asigna pagina.
		 */
		public void setPagina(String pagina) {
			this.pagina = pagina;
		}
		
		/**
		 * @return Retorna nombreMotivoFinalizacion.
		 */
		public String getNombreMotivoFinalizacion() {
			return nombreMotivoFinalizacion;
		}
		/**
		 * @param Asigna nombreMotivoFinalizacion.
		 */
		public void setNombreMotivoFinalizacion(String nombreMotivoFinalizacion) {
			this.nombreMotivoFinalizacion = nombreMotivoFinalizacion;
		}
		/**
		 * @return Returns the ultrasonidoFecha.
		 */
		public String getUltrasonidoFecha()
		{
			return ultrasonidoFecha;
		}
		/**
		 * @param ultrasonidoFecha The ultrasonidoFecha to set.
		 */
		public void setUltrasonidoFecha(String ultrasonidoFecha)
		{
			this.ultrasonidoFecha = ultrasonidoFecha;
		}
		/**
		 * @return Returns the ultrasonidoHora.
		 */
		public String getUltrasonidoHora()
		{
			return ultrasonidoHora;
		}
		/**
		 * @param ultrasonidoHora The ultrasonidoHora to set.
		 */
		public void setUltrasonidoHora(String ultrasonidoHora)
		{
			this.ultrasonidoHora = ultrasonidoHora;
		}
		/**
		 * @return Retorna historicoUltrasonido.
		 */
		public Collection getHistoricoUltrasonido() {
			return historicoUltrasonido;
		}
		/**
		 * @param Asigna historicoUltrasonido.
		 */
		public void setHistoricoUltrasonido(Collection historicoUltrasonido) {
			this.historicoUltrasonido = historicoUltrasonido;
		}
		/**
		 * @return Retorna tiposResumenGestacional.
		 */
		public Collection getTiposResumenGestacional() {
			return tiposResumenGestacional;
		}
		/**
		 * @param Asigna tiposResumenGestacional.
		 */
		public void setTiposResumenGestacional(
				Collection tiposResumenGestacional) {
			this.tiposResumenGestacional = tiposResumenGestacional;
		}
		/**
		 * @return Retorna tiposUltrasonido.
		 */
		public Collection getTiposUltrasonido() {
			return tiposUltrasonido;
		}
		/**
		 * @param Asigna tiposUltrasonido.
		 */
		public void setTiposUltrasonido(Collection tiposUltrasonido) {
			this.tiposUltrasonido = tiposUltrasonido;
		}
		/**
		 * @return Returns the edadGestResumenTemp.
		 */
		public int getEdadGestResumenTemp()
		{
			return edadGestResumenTemp;
		}
		/**
		 * @param edadGestResumenTemp The edadGestResumenTemp to set.
		 */
		public void setEdadGestResumenTemp(int edadGestResumenTemp)
		{
			this.edadGestResumenTemp = edadGestResumenTemp;
		}
		/**
		 * @return Returns the edadGestExamenTemp.
		 */
		public int getEdadGestExamenTemp()
		{
			return edadGestExamenTemp;
		}
		/**
		 * @param edadGestExamenTemp The edadGestExamenTemp to set.
		 */
		public void setEdadGestExamenTemp(int edadGestExamenTemp)
		{
			this.edadGestExamenTemp = edadGestExamenTemp;
		}
		/**
		 * @return Returns the alturaUterinaTemp.
		 */
		public int getAlturaUterinaTemp()
		{
			return alturaUterinaTemp;
		}
		/**
		 * @param alturaUterinaTemp The alturaUterinaTemp to set.
		 */
		public void setAlturaUterinaTemp(int alturaUterinaTemp)
		{
			this.alturaUterinaTemp = alturaUterinaTemp;
		}
		/**
		 * @return Returns the edadGestUltrasonidoTemp.
		 */
		public int getEdadGestUltrasonidoTemp()
		{
			return edadGestUltrasonidoTemp;
		}
		/**
		 * @param edadGestUltrasonidoTemp The edadGestUltrasonidoTemp to set.
		 */
		public void setEdadGestUltrasonidoTemp(int edadGestUltrasonido)
		{
			this.edadGestUltrasonidoTemp = edadGestUltrasonido;
		}
	/**
	 * @return Returns the edadGestEcoUltraTemp.
	 */
	public int getEdadGestEcoUltraTemp()
	{
		return edadGestEcoUltraTemp;
	}
	/**
	 * @param edadGestEcoUltraTemp The edadGestEcoUltraTemp to set.
	 */
	public void setEdadGestEcoUltraTemp(int edadGestEcoUltraTemp)
	{
		this.edadGestEcoUltraTemp = edadGestEcoUltraTemp;
	}
/**
 * @return Returns the furTemp.
 */
public String getFurTemp()
{
	return furTemp;
}
/**
 * @param furTemp The furTemp to set.
 */
public void setFurTemp(String furTemp)
{
	this.furTemp = furTemp;
}


		/**
		 * @return Returns the temp.
		 */
		public String getTemp()
		{
			return temp;
		}
		/**
		 * @param temp The temp to set.
		 */
		public void setTemp(String temp)
		{
			this.temp= temp;
		}
		/**
		 * @return Returns the numeroOtros.
		 */
		public int getNumeroOtros()
		{
			return numeroOtros;
		}
		/**
		 * @param numeroOtros The numeroOtros to set.
		 */
		public void setNumeroOtros(int numeroOtros)
		{
			this.numeroOtros = numeroOtros;
		}


		public String getConfiable() {
			return confiable;
		}

		public void setConfiable(String confiable) {
			this.confiable = confiable;
		}

		public String getAntirubeola() {
			return antirubeola;
		}

		public void setAntirubeola(String antirubeola) {
			this.antirubeola = antirubeola;
		}

		public String getDosisAntitetanica() {
			return dosisAntitetanica;
		}

		public void setDosisAntitetanica(String dosisAntitetanica) {
			this.dosisAntitetanica = dosisAntitetanica;
		}

		public String getMesesGestacionAntitetanica() {
			return mesesGestacionAntitetanica;
		}

		public void setMesesGestacionAntitetanica(String mesesGestacionAntitetanica) {
			this.mesesGestacionAntitetanica = mesesGestacionAntitetanica;
		}

		public String getVigenteAntitetanica() {
			return vigenteAntitetanica;
		}

		public void setVigenteAntitetanica(String vigenteAntitetanica) {
			this.vigenteAntitetanica = vigenteAntitetanica;
		}

		public HashMap getMapaTiposPlanManejo() {
			return mapaTiposPlanManejo;
		}

		public void setMapaTiposPlanManejo(HashMap mapaTiposPlanManejo) {
			this.mapaTiposPlanManejo = mapaTiposPlanManejo;
		}
		
		/**
		 * @return Retorna mapaTiposPlanManejo.
		 */
		public Object getMapaTiposPlanManejo(String key)
		{
			return mapaTiposPlanManejo.get(key+"");
		}

		/**
		 * @param mapaTiposPlanManejo Asigna mapaTiposPlanManejo.
		 */
		public void setMapaTiposPlanManejo(String key, String valor)
		{
			this.mapaTiposPlanManejo.put(key,valor);
		}

		public HashMap getMapaPlanManejo() {
			return mapaPlanManejo;
		}

		public void setMapaPlanManejo(HashMap mapaPlanManejo) {
			this.mapaPlanManejo = mapaPlanManejo;
		}
		
		/**
		 * @return Retorna mapaPlanManejo.
		 */
		public Object getMapaPlanManejo(String key)
		{
			return mapaPlanManejo.get(key+"");
		}

		/**
		 * @param mapaPlanManejo Asigna mapaPlanManejo.
		 */
		public void setMapaPlanManejo(String key, String valor)
		{
			this.mapaPlanManejo.put(key,valor);
		}

		public HashMap getMapaHistoPlanManejo() {
			return mapaHistoPlanManejo;
		}

		public void setMapaHistoPlanManejo(HashMap mapaHistoPlanManejo) {
			this.mapaHistoPlanManejo = mapaHistoPlanManejo;
		}

		public int getIndicePlanManejo() {
			return indicePlanManejo;
		}

		public void setIndicePlanManejo(int indicePlanManejo) {
			this.indicePlanManejo = indicePlanManejo;
		}

		/**
		 * @return the errorExamenes
		 */
		public boolean isErrorExamenes() {
			return errorExamenes;
		}

		/**
		 * @param errorExamenes the errorExamenes to set
		 */
		public void setErrorExamenes(boolean errorExamenes) {
			this.errorExamenes = errorExamenes;
		}

		/**
		 * @return the embarazoDeseado
		 */
		public String getEmbarazoDeseado() {
			return embarazoDeseado;
		}

		/**
		 * @param embarazoDeseado the embarazoDeseado to set
		 */
		public void setEmbarazoDeseado(String embarazoDeseado) {
			this.embarazoDeseado = embarazoDeseado;
		}

		/**
		 * @return the embarazoPlaneado
		 */
		public String getEmbarazoPlaneado() {
			return embarazoPlaneado;
		}

		/**
		 * @param embarazoPlaneado the embarazoPlaneado to set
		 */
		public void setEmbarazoPlaneado(String embarazoPlaneado) {
			this.embarazoPlaneado = embarazoPlaneado;
		}

		/**
		 * @return the peso
		 */
		public String getPeso() {
			return peso;
		}

		/**
		 * @param peso the peso to set
		 */
		public void setPeso(String peso) {
			this.peso = peso;
		}

		/**
		 * @return the talla
		 */
		public String getTalla() {
			return talla;
		}

		/**
		 * @param talla the talla to set
		 */
		public void setTalla(String talla) {
			this.talla = talla;
		}

		/**
		 * @return the errorUltraSonido
		 */
		public boolean isErrorUltraSonido() {
			return errorUltraSonido;
		}

		/**
		 * @param errorUltraSonido the errorUltraSonido to set
		 */
		public void setErrorUltraSonido(boolean errorUltraSonido) {
			this.errorUltraSonido = errorUltraSonido;
		}
				
		
}
