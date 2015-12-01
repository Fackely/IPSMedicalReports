
package com.princetonsa.actionform;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.SimpleTimeZone;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.UtilidadFecha;
import util.UtilidadValidacion;
import util.ValoresPorDefecto;

/**
 * ActionForm, tiene la función de bean dentro de la forma, que contiene todos
 * los datos especificados para los antecedentes ginecoobstetricos, maneja tanto
 * códigos como nombres. Y adicionalmente hace el manejo de reset de la forma y
 * de validación de errores de datos de entrada.
 * @version 1.0, Abril 7, 2003
 * @author <a href="mailto:Liliana@PrincetonSA.com">Liliana Caballero</a>,
 * @author <a href="mailto:Camilo@PrincetonSA.com">Camilo Andr&eacute;s Camacho
 */
public class AntecedentesGinecoObstetricosForm extends ValidatorForm
{
	/**
	 * Dice si hay errores o no
	 */
	public boolean hayErrores = false;
	
	/**
	 * Código de la bd del rango de edad de la menarquia.
	 */
	public int rangoEdadMenarquia;
	
	/**
	 * Nombre del rango de edad de la menarquia que se ha seleccionado (Solo
	 * existe cuando ya ha sido cargado en la bd
	 */
	public String nombreRangoEdadMenarquia;
	
	/**
	 * Bandera que indica si existe un dato de rango de edad de la menarquia
	 * guardado en la base de datos para este paciente.
	 */
	public boolean existeRangoEdadMenarquia=false;
	
	/**
	 * Cadena con la especificación de la edad de la menarquia especificada.
	 */
	public String otraEdadMenarquia;
	
	/**
	 * Código de la bd del rango de edad de la menopausia.
	 */	
	public int rangoEdadMenopausia;

	/**
	 * Bandera que indica si existe un dato de rango de edad de la menarquia
	 * guardado en la base de datos para este paciente.
	 */
	public boolean existeRangoEdadMenopausia=false;

	
	/**
	 * Nombre del rango de edad de la menarquia que se ha seleccionado (Solo
	 * existe cuando se ha cargado de la bd
	 */
	public String nombreRangoEdadMenopausia;

	/**
	 * Cadena con la especificación de la edad de la menopausia especificada.
	 */
	public String otraEdadMenopausia;
	
	/**
	 * Número de dias de duración de la menstruación
	 */
	public String duracionMenstruacion;

	/**
	 * Número de dias del ciclo menstrual
	 */
	public String cicloMenstrual;
	
	/**
	 * Observaciones de las caracteristicas de la menstruacion
	 */				 
	public String observacionesMenstruacion;
					 
	/**
	 * Bandera si existe o no dolor durante la menstruación.
	 */
	public String dolorMenstruacion;
	
	/**
	 * Fecha de la última regla. Con formato dd/mm/aaaa
	 */
	public String fechaUltimaRegla;
	
	/**
	 * Caracteristicas de la menstruación (concepto)
	 */
	public int conceptoMenstruacion;
	
	/**
	 * Fecha de la última mamografía de la paciente, no tiene ningún formato.
	 */
	public String fechaUltimaMamografia;

	/**
	 * Descripción de la última mamografía de la paciente.
	 */	 
	public String descUltimaMamografia;
	
	/**
	 * Fecha de la última citlogía de la paciente, no tiene ningún formato.
	 */
	public String fechaUltimaCitologia;
	
	/**
	 * Descripción de la última citología de la paciente.
	 */	 
	public String descUltimaCitologia;	

	/**
	 * Fecha de la última ecografia de la paciente, no tiene ningún formato.
	 */
	public String fechaUltimaEcografia;
	
	/**
	 * Descripción de la última ecografia de la paciente.
	 */	 
	public String descUltimaEcografia;	
	
	/**
	 * Fecha de la última densimetria osea de la paciente, no tiene ningún
	 * formato.
	 */
	public String fechaUltimaDensimetriaOsea;
	
	/**
	 * Descripción de la última densimetria osea de la paciente.
	 */	 
	public String descUltimaDensimetriaOsea;	
	
	/**
	 * Descripción del último procedimiento o enfermedad ginecológica de la
	 * paciente.
	 */	 	
	public String descUltimoProcedimiento;
	
	/**
	 * Observaciones previamente grabadas en la bd
	 */
	public String observacionesViejas;
	
	/**
	 * Observaciones nuevas
	 */
	public String observacionesNuevas;
	
	/**
	 * Para manejar los campos dinamicos de la forma
	 */	
	private final Map valores = new HashMap();
		
	/**
	 * Número de métodos anticonceptivos predefinidos
	 */	
	private int numMetodosAnticonceptivos;
	
	/**
	 * Histórico de la duración de la menstruacion (Solo se usa en el caso de
	 * mostrar el resumen de todos los antecedentes ginecoobstetricos)
	 */
	private ArrayList historicos;
	
	/**
	 * Número de embarazos ingresados
	 */
	private int numeroEmbarazos;
	
	/**
	 * Estado en el cual se encuentra el proceso
	 */
	public String estado;
	
	/**
	 * Número de gestaciones 
	 */
	public int numGestaciones;
	
	/**
	 * Número de partos 
	 */
	public int numPartos;
	
	/**
	 * Número de partos ingresados anteriormente
	 */
	public int numPartosGrabados;
	
	/**
	 * Número de abortos 
	 */
	public int numAbortos;
	
	/**
	 * Número de abortos ingresados anteriormente
	 */
	public int numAbortosGrabados;
	
	/**
	 * Número de cesareas 
	 */
	public int numCesareas;
	
	/**
	 * Número de cesareas ingresadas anteriormente
	 */
	public int numCesareasGrabadas;
	
	/**
	 * Número de hijos nacidos vivos 
	 */
	public int numVivos;
	
	/**
	 * Número de hijos nacidos vivos ingresados anteriormente
	 */
	public int numVivosGrabados;
	
	/**
	 * Número de hijos nacidos muertos
	 */
	public int numMuertos;
	
	/**
	 * Número de hijos nacidos muertos ingresados anteriormente
	 */
	public int numMuertosGrabados;
	
	/**
	 * Edad en años en la que se empezó la vida sexual
	 */
	public String inicioVidaSexual;
	
	/**
	 * Edad en años en la que se empezó la vida obstétrica
	 */
	public String inicioVidaObstetrica;

	/**
	 * Información de embarazos, número de gestaciones
	 */
	public String gInfoEmbarazos;

	/**
	 * Información de embarazos, número de partos vaginales
	 */
	public String pInfoEmbarazos;
	
	/**
	 * Información de embarazos, número de abortos
	 */
	public String aInfoEmbarazos;

	/**
	 * Información de embarazos, número de cesáreas
	 */
	public String cInfoEmbarazos;

	/**
	 * Información de embarazos, número de hijos nacidos vivos
	 */
	public String vInfoEmbarazos;
	
	/**
	 * Información de embarazos, número de hijos nacidos muertos
	 */
	public String mInfoEmbarazos;
	
	private String ocultarCabezotes;
	
	/**
	 * Variable para saber si seleccionaron dolor menstruación
	*/
	
	private int selectDolorMenstruacion;
	
	/**
	 * Dice si hay error en inicio vida sexual o inicio vida obstetrica
	 */
	public boolean hayErrorVida = false;
	

	//-Ultimos Cambios 01/06/2005
	
	/**
	 * Campo para ingresar el rango para partos // menor  2500 gramos  
	 */
	public String p2500;

	/**
	 * Campo para ingresar el rango para partos // mayor  4000 gramos
	 */
	public String p4000;

	/**
	 *  Es para el campo de chequeo alfrente del campo de texto A (Abortos) 
	 */
	public String  mayorA2;

	/**
	 * Campo para indicar Fin Embarazo Antertior 
	 */
	public String  finEmbarazoAnterior;
	
	/**
	 * Es para el campo de chequeo de Mayor 5 o menor  a 1
	 * Que aparece  al frente de fin embarazo anterior 
	 */
	public String finEmbarazoMayor1o5;		
	
	/**
	 * Campo String de Prematuros 
	 */
	public String prematuros;
	
	/**
	 * Campo String de ectropicos 
	 */
	public String ectropicos;
	/**
	 * Campo String de multiples 
	 */
	public String multiples;
	
	//---------------------------------------------------------------------------------------------------------------------------------------------
	//---------------------------------------------------------------------------------------------------------------------------------------------
	//------------------------------Nuevos Campos para la seccion "Información Embarazos"
	//---------------------------------------------------------------------------------------------------------------------------------------------
	//---------------------------------------------------------------------------------------------------------------------------------------------
	/**
	 * -- Vaginal
	 */
	int vag;			 	  		
	
	
	/**
	 * -- Retencion Placentaria
	 */
	String retencionPlacentaria;
	
	/**
	 * -- Infeccion Postparto
	 */
	String infeccionPostparto;
	
	/**
	 * -- Malformaciones
	 */
	String malformacion;
	
	/**
	 * -- Muerte Perinatal
	 */
	String muertePerinatal;

	//--- Para la Sección de Historia Mestrual 
	String sangradoAnormal;	
	String flujoVaginal;	
	String enferTransSexual;	
	String cualEnferTransSex;	
	String cirugiaGineco;	
	String cualCirugiaGineco;	
	String historiaInfertilidad;	
	String cualHistoInfertilidad;	
	
	
	String tipoEmbarazo;
	String muertosAntes1Semana;
	String muertosDespues1Semana;
	String vivosActualmente;
	private String esProcesoExitoso; 
	
	
	public AntecedentesGinecoObstetricosForm()
	{
	}

	public void reset()
	{	
		this.tipoEmbarazo="";
		this.muertosAntes1Semana="";
		this.muertosDespues1Semana="";
		this.vivosActualmente="";
		this.gInfoEmbarazos = "";
		this.pInfoEmbarazos = "";
		this.aInfoEmbarazos = "";
		this.cInfoEmbarazos = "";
		this.vInfoEmbarazos = "";
		this.mInfoEmbarazos = "";
		this.p2500 = "";
		this.p4000 = "";
		this.mayorA2 = "";
		this.finEmbarazoAnterior = "";
		this.finEmbarazoMayor1o5 = "";
		this.prematuros = "";
		this.ectropicos = "";
		this.multiples = "";
		
		
		this.cicloMenstrual = "";
		this.observacionesMenstruacion = "";
		this.conceptoMenstruacion = -1;
		this.descUltimaCitologia = "";
		this.descUltimaEcografia = "";
		this.descUltimaMamografia = "";
		this.descUltimoProcedimiento = "";
		this.descUltimaDensimetriaOsea = "";
		this.dolorMenstruacion = "";
		this.duracionMenstruacion = "";
		this.fechaUltimaCitologia = "";
		this.fechaUltimaEcografia = "";
		this.fechaUltimaMamografia = "";
		this.fechaUltimaDensimetriaOsea = "";
		this.fechaUltimaRegla = "";
		this.observacionesNuevas = "";
		this.observacionesViejas = "";
		this.otraEdadMenarquia = "";
		this.otraEdadMenopausia = "";
		this.rangoEdadMenarquia = -1;
		this.nombreRangoEdadMenarquia = "";
		this.rangoEdadMenopausia = -1;
		this.nombreRangoEdadMenopausia = "";
		this.existeRangoEdadMenarquia = false;
		this.existeRangoEdadMenopausia=false;
		this.numMetodosAnticonceptivos = 0;
		this.numeroEmbarazos = 0;		
		this.valores.clear();
		
		this.inicioVidaSexual = "";
		this.inicioVidaObstetrica = "";
		
		this.hayErrores = false;
		this.numGestaciones = 0;
		this.numPartos = 0;
		this.numPartosGrabados = 0;
		this.numAbortos = 0;
		this.numAbortosGrabados = 0;
		this.numCesareas = 0;
		this.numCesareasGrabadas = 0;
		this.numMuertos = 0;
		this.numMuertosGrabados = 0;
		this.numVivos = 0;
		this.numVivosGrabados = 0;
		this.selectDolorMenstruacion=0;
		
		//-- Campos de Información Embarazos Nuevos
		//modificacion por la tarea 283881
		/*
		 En el módulo Historia Clínica, en la funcionalidad Antecedentes Gineco-obstétricos, en la sección Información Embarazos, los siguientes campos no deben tener ningún valor por defecto:
		- Ant Abor Esp
		- Infecc. Postparto
		- Malformación
		- Emb. Planeado/Deseado
		- Retención Placentaria
		Adicionalmente, al momento de guardar cualquier valor en algunos de estos campos, se debe mostrar en el resumen. 
		*/
		this.vag = 0;			 	  		
		this.retencionPlacentaria = "";
		this.infeccionPostparto = "";
		this.malformacion = "";
		this.muertePerinatal = "";
		
		this.sangradoAnormal = "N";	
		this.flujoVaginal = "N";	
		this.enferTransSexual = "N";	
		this.cualEnferTransSex = "";	
		this.cirugiaGineco = "N";	
		this.cualCirugiaGineco = "";	
		this.historiaInfertilidad = "N";	
		this.cualHistoInfertilidad = "";
		this.esProcesoExitoso = ConstantesBD.acronimoNo;
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
		this.hayErrores = false;
		this.hayErrorVida = false;
		// Perform validator framework validations
		ActionErrors errors = new ActionErrors();
		boolean errorFechaPosterior=false;

		if( !estado.equals("cancelar") )
		{
			//Se definio que si el rango era 0, era necesario validar que estuviera la opción
			//otros
	
			if (estado!=null&& (estado.equals("finalizar") ||  estado.equals("salir")))
			{
				errors = super.validate(mapping, request);
				
				if(this.pInfoEmbarazos!=null &&!this.pInfoEmbarazos.trim().equals("") && Integer.parseInt(this.pInfoEmbarazos)>0)
				{
					if (this.retencionPlacentaria.equals("")) 
					{
						errors.add("retencion_placentaria", new ActionMessage("errors.required"," Retención Placentaria de la Sección Información Embarazos"));	
					}
					if (this.infeccionPostparto.equals("")) 
					{
						errors.add("infeccion_postparto", new ActionMessage("errors.required"," Infección Postparto de la Sección Información Embarazos"));	
					}
					if (this.malformacion.equals("")) 
					{
						errors.add("malformacion", new ActionMessage("errors.required"," Malformación de la Sección Información Embarazos"));	
					}
					if (this.muertePerinatal.equals(""))
					{
						errors.add("muerte_perinatal", new ActionMessage("errors.required"," Muerte Perinatal de la Sección Información Embarazos"));	
					}
				}
				if (this.muertePerinatal.equals(ValoresPorDefecto.getValorTrueParaConsultas()) && (this.muertosAntes1Semana.trim().equals("")||this.muertosDespues1Semana.trim().equals("")))
				{
					errors.add("causamuerte_perinatal", new ActionMessage("errors.required"," Antes semana 1 y Despues semana 1"));	
				}
				
				if (this.enferTransSexual.equals(ConstantesBD.acronimoSi) && this.cualEnferTransSex.equals(""))
				{
					errors.add("enferTransSexual", new ActionMessage("errors.required"," Cual Enfermedad de Transmisión Sexual de la Sección Informacion General"));	
				}
				if (this.cirugiaGineco.equals(ConstantesBD.acronimoSi) && this.cualCirugiaGineco.equals(""))
				{
					errors.add("cirugiaGineco", new ActionMessage("errors.required"," Cual Cirugía Ginecológica de la Sección Informacion General"));	
				}
				/*if (this.historiaInfertilidad.equals(ConstantesBD.acronimoSi) && this.cualHistoInfertilidad.equals(""))
				{
					errors.add("historiaInfertilidad", new ActionMessage("errors.required"," Cual Historia de Infertilidad de la Sección Informacion General"));	
				}*/
					
			
				if(!this.finEmbarazoAnterior.trim().equals(""))
				{
					if (!UtilidadFecha.esFechaValidaSegunAp(this.finEmbarazoAnterior))
					{
						errors.add ("finEmbarazoAnterior", new ActionMessage("errors.formatoFechaInvalido", "Fecha Finalizacion")); 
					}
					else
					{
						String[] arregloFUR = this.finEmbarazoAnterior.split("/");
						if( arregloFUR.length == 3 )
						{
							if (!this.esFechaAnterior( Integer.parseInt(arregloFUR[0]), Integer.parseInt(arregloFUR[1]), Integer.parseInt(arregloFUR[2] ) ) )
							{
								errors.add ("errorFechaPosterior", new ActionMessage("errors.fechaPosteriorAOtraDeReferencia", "Fecha Finalizacion", "actual"));
							}
						}
					}
				}
				
				
				if( this.inicioVidaSexual != null && !this.inicioVidaSexual.equals("") )
				{
					try
					{
						int inicVS = Integer.parseInt(inicioVidaSexual);
					
						if( inicVS < 0 || inicVS >100 )
						{
							hayErrorVida=true;
							//this.inicioVidaSexual = "";							
							errors.add("inicioVidaSexual", new ActionMessage("errors.range", "Inicio vida sexual", "0", "100"));
						}
					}
					catch(Exception e)
					{
						hayErrorVida=true;
						//this.inicioVidaSexual = "";
						errors.add("inicioVidaSexual", new ActionMessage("errors.integer", "Inicio vida sexual"));
					}					
				}

				if( this.inicioVidaObstetrica != null && !this.inicioVidaObstetrica.equals("") )
				{
					try
					{
						int inicVO = Integer.parseInt(inicioVidaObstetrica);
										
						if( inicVO < 8 || inicVO >60 )
						{
							hayErrorVida=true;
							//this.inicioVidaObstetrica = "";							
							errors.add("inicioVidaObstetrica", new ActionMessage("errors.range", "Inicio vida obstétrica", "8", "60"));
						}
					}
					catch(Exception e)
					{
						hayErrorVida=true;
						//this.inicioVidaObstetrica = "";
						errors.add("inicioVidaObstetrica", new ActionMessage("errors.integer", "Inicio vida obstétrica"));
					}
					
				}
					
				if (this.rangoEdadMenarquia==0)
				{
					if (this.otraEdadMenarquia==null||this.otraEdadMenarquia.equals(""))
					{
						this.setRangoEdadMenarquia(-1);
						//errors.add("origen", new ActionMessage("errors.descripcionOtro", "edad menarquia"));
						
					}
					
				}
				
				if (this.rangoEdadMenopausia==0)
				{
					if (this.otraEdadMenopausia==null||this.otraEdadMenopausia.equals(""))
					{
				//		errors.add("origen", new ActionMessage("errors.descripcionOtro", "edad menopausia"));
						this.setRangoEdadMenopausia(-1);
					}
					
				}
				
				if(this.fechaUltimaRegla!=null)
				{
					if (this.fechaUltimaRegla.equals(""))
					{
						this.fechaUltimaRegla="";
					}
					else if (!UtilidadFecha.validarFecha(fechaUltimaRegla))
					{						
						errors.add ("descripcion", new ActionMessage("errors.notEspecific", "La Fecha de la última regla (FUR) no es una Fecha Válida. Debe estar en formato dd/mm/aaaa"));
					}
					else
					{
						String[] arregloFUR = this.getFechaUltimaRegla().split("/");
						if( arregloFUR.length == 3 )
						{
							if (!this.esFechaAnterior( Integer.parseInt(arregloFUR[0]), Integer.parseInt(arregloFUR[1]), Integer.parseInt(arregloFUR[2] ) ) )
							{
								errors.add ("errorFechaPosterior", new ActionMessage("errors.fechaPosteriorAOtraDeReferencia", "de la última regla", "actual"));
							}
						}
					}
				}
				
				if(this.fechaUltimaMamografia!=null)
				{
					if(!this.fechaUltimaMamografia.equals("") && 
							!UtilidadFecha.validarFecha(fechaUltimaMamografia))
						errors.add ("descripcion", new ActionMessage("errors.notEspecific", "La Fecha de la última Mamografia no es una Fecha Válida. Debe estar en formato dd/mm/aaaa"));
				}
				
				if(this.fechaUltimaCitologia!=null)
				{
					if(!this.fechaUltimaCitologia.equals("") && 
							!UtilidadFecha.validarFecha(fechaUltimaCitologia))
						errors.add ("descripcion", new ActionMessage("errors.notEspecific", "La Fecha de la última Citologia no es una Fecha Válida. Debe estar en formato dd/mm/aaaa"));
				}
				
				if(this.fechaUltimaEcografia!=null)
				{
					if(!this.fechaUltimaEcografia.equals("") && 
							!UtilidadFecha.validarFecha(fechaUltimaEcografia))
						errors.add ("descripcion", new ActionMessage("errors.notEspecific", "La Fecha de la última Ecografia no es una Fecha Válida. Debe estar en formato dd/mm/aaaa"));
				}
				
				if(this.fechaUltimaDensimetriaOsea!=null)
				{
					if(!this.fechaUltimaDensimetriaOsea.equals("") && 
							!UtilidadFecha.validarFecha(fechaUltimaDensimetriaOsea))
						errors.add ("descripcion", new ActionMessage("errors.notEspecific", "La Fecha de la última Densimetría Osea no es una Fecha Válida. Debe estar en formato dd/mm/aaaa"));
				}
					
				//En este punto vamos a validar los datos dinámicos
				//Primero preguntamos averiguamos por el número de embarazos
	
				int embarazosCargados;
					
				try
				{
					embarazosCargados=Integer.parseInt((String)request.getSession().getAttribute("numeroEmbarazos"));
				}
				catch (Exception e)
				{
					embarazosCargados=0;
				}
	
			
				if (numeroEmbarazos + embarazosCargados >0)
				{
					//Para que solo me aparezca el error una vez y no 
					//tantas veces como lo haya cometido el usuario
					//defino unos boolean que van a cambiar si encuentro
					//el error y al final solo se va a imprimir 1 vez
					boolean errorNumeroMesesGestacion=false;
					boolean errorRangoMesesGestacion=false;
				
					boolean errorFechaTerminacion=false;
	
					boolean faltaOtroTipoTrabajoParto=false, faltaOtraComplicacionEmbarazo=false;
				
					boolean faltaOtroTipoParto=false;
					
					boolean errorMinimoHijo=false;
				
					//Los contadores se definen localmente por eficiencia
					//y empiezan en 1 por convención (1er embarazo no
					//0avo embarazo)
				
					int numeroHijos=0;
				
					float mesesGestacionFloat=0;
					String fechaTerminacionGestacion="";
					String arregloFechaTerminacionGestacion[];
				
					int codigoTipoTrabajoParto=0, codigoComplicacionEmbarazo =0;
					String otroTipoTrabajoParto="", otraComplicacionEmbarazo="";
				
					//Hijos
					int codigoTipoParto=24;
				
					String temporal="";
	
					
					//Tengo que revisar todos, incluso los previamente cargados
					for (int i=1; i<=numeroEmbarazos + embarazosCargados; i++)
					{
						//Pongo un valor válido, ya que el error no es de entero
						//(caso en que no sea un entero)
					
						//NOTA: Cuando el usuario no llena nada sale una cadena vacia
						// LOS MESES DE GESTACION, SE CAMBIARON A SEMANAS
						// DE GESTACION, PERO BAJO EL MISMO NOMBRE!!!!!!!
					
						//Empieza validación de meses gestación
						mesesGestacionFloat=(float)0.0;
						try 
						{
							temporal=(String) valores.get("mesesGestacion_" + i);
							if (temporal!=null && !temporal.equals(""))
							{
								//Cuando no escriben nada no debo validar,
								//nos mantenemos en el valor 0 inicial
								mesesGestacionFloat=Float.parseFloat( temporal );
							}
						
						}
						catch (Exception e)
						{
							this.setValue("mesesGestacion_" + i, "");
							errorNumeroMesesGestacion=true;
						}
					
						if (mesesGestacionFloat<0.0||mesesGestacionFloat>50.0)
						{
							this.setValue("mesesGestacion_" + i, "");
							errorRangoMesesGestacion=true;
						}
					
						//Empieza validación de fecha de Terminación
						//Para evitar valores nulos
						fechaTerminacionGestacion="";
						fechaTerminacionGestacion=(String) valores.get("fechaTerminacion_" + i);
					
						if (fechaTerminacionGestacion!=null)
						{
							if (fechaTerminacionGestacion.equals(""))
							{
								fechaTerminacionGestacion=null;
							}
							else if (!UtilidadFecha.esFechaValidaSegunAp(fechaTerminacionGestacion))
							{
								errorFechaTerminacion=true;
							}
							else
							{
								arregloFechaTerminacionGestacion=fechaTerminacionGestacion.split("/");
								if (arregloFechaTerminacionGestacion.length!=3)
								{
									this.setValue("fechaTerminacion_" + i, "");
									errorFechaTerminacion=true;
								}
								else
								{
									//En este punto ya tenemos un arreglo con el dia, el mes y el año
									try
									{
										if ( !UtilidadValidacion.validacionFecha(Integer.parseInt(arregloFechaTerminacionGestacion[2]), Integer.parseInt(arregloFechaTerminacionGestacion[1]), Integer.parseInt(arregloFechaTerminacionGestacion[0]) ).puedoSeguir )
										{
											errorFechaTerminacion=true;
											this.setValue("fechaTerminacion_" + i, "");
										}
									
										//Es una fecha Valida, falta revisar que sea menor o igual a la actual
									
										if (!this.esFechaAnterior( Integer.parseInt(arregloFechaTerminacionGestacion[0]), Integer.parseInt(arregloFechaTerminacionGestacion[1]), Integer.parseInt(arregloFechaTerminacionGestacion[2] ) ) )
										{
											errorFechaPosterior=true;
											//this.setValue("fechaTerminacion_" + i, "");
										}
									}
									catch (Exception e)
									{
										//Si llegamos a esta excepción es porque alguno de los componentes de la fecha no es un entero
										errorFechaTerminacion=true;
										this.setValue("fechaTerminacion_" + i, "");
									}
								}
							}
						}//termina la validacion de la fecha terminacion
	
						//Empiezo la validacion de los select de embarazo
						//empiezo en valores grandes y no en 0 o -1 pues 
						//en los select estos valores tienen consecuencias
						codigoComplicacionEmbarazo=24;
						codigoTipoTrabajoParto=24;
						try
						{
							codigoComplicacionEmbarazo = Integer.parseInt((String)valores.get("complicacionEmbarazo_" + i));
						}
						catch (Exception e)
						{
							//Si entro a esta excepción es porque el usuario llego sin utilizar la aplicación
							//ya que los select SOLO dan códigos válidos (Este error si dejo que se repita
							//pues solo un usuario malintencionado lo puede cometer)
							this.setValue("complicacionEmbarazo_" + i, "-1");					
						}
	
						try
						{
							codigoTipoTrabajoParto= Integer.parseInt((String)valores.get("tipoTrabajoParto_" + i) );
						}
						catch (Exception e)
						{
							//Si entro a esta excepción es porque el usuario llego sin utilizar la aplicación
							//ya que los select SOLO dan códigos válidos (Este error si dejo que se repita
							//pues solo un usuario malintencionado lo puede cometer)
							this.setValue("tipoTrabajoParto_" + i, "");						
						}
	
	
					
						//Si el codigo es 0, DEBEN haber ingresado un valor en otros
						if (codigoTipoTrabajoParto==0)
						{
							otroTipoTrabajoParto=(String)valores.get("otroTipoTrabajoParto_" + i);
							if (otroTipoTrabajoParto==null||otroTipoTrabajoParto.equals(""))
							{
								this.setValue("tipoTrabajoParto_" + i, "-1");
								faltaOtroTipoTrabajoParto=true;
							}
						}
						if (codigoComplicacionEmbarazo ==0)
						{
							otraComplicacionEmbarazo=(String)valores.get("otraComplicacionEmbarazo_" + i);
							if (otraComplicacionEmbarazo==null||otraComplicacionEmbarazo.equals(""))
							{
								this.setValue("complicacionEmbarazo_" + i, "-1");
								faltaOtraComplicacionEmbarazo=true;
							}
						}
					
						//Aqui empieza la validacion de los hijos
					
						numeroHijos=0;
						try
						{
							numeroHijos=Integer.parseInt((String)valores.get("numeroHijos_" + i));
						
						}
						catch (Exception e)
						{
							//Si entro a esta excepción es porque el usuario llego sin utilizar la aplicación
							//ya que los select SOLO dan códigos válidos (Este error si dejo que se repita
							//pues solo un usuario malintencionado lo puede cometer)
						
							numeroHijos=0;
						}
	
						if (numeroHijos<1)
						{
							errorMinimoHijo=true;
						}
	
	
						for (int j=1;j<=numeroHijos;j++)
						{
							//Para los hijos lo único que tenemos que validar es que el tipo de parto
							//sea númerico, que si selecciona la opción otros llene el valor otro
							codigoTipoParto=24;
							try
							{
								codigoTipoParto=Integer.parseInt((String)valores.get("tiposParto_" + i + "_" +j));
																
								if (codigoTipoParto==0)
								{
									String otroTipoParto=(String)valores.get("otroTipoParto_" + i + "_" +j); 
									if (otroTipoParto==null||otroTipoParto.equals(""))
									{
										this.setValue("tiposParto_"+i+"_"+j, "-1");
										faltaOtroTipoParto=true;
									}
								}
							
							}
							catch (Exception e)
							{
								//Si entro a esta excepción es porque el usuario llego sin utilizar la aplicación
								//ya que los select SOLO dan códigos válidos (Este error si dejo que se repita
								//pues solo un usuario malintencionado lo puede cometer)
	
								this.setValue("tiposParto_"+i+"_"+j, "-1");
							}
						}
	
					}
				
				
					
					
					if (errorNumeroMesesGestacion)
					{
						errors.add ("errorFormatoSemanasGestacion", new ActionMessage("errors.float", "Semanas de gestación"));
					}
					if (errorRangoMesesGestacion)
					{
						errors.add ("errorRangoSemanasGestacion", new ActionMessage("errors.range", "Semanas de gestación", "0", "50"));
					}
					if (errorFechaTerminacion)
					{
						errors.add ("fechaterminaciongestacioninvalido", new ActionMessage("errors.formatoFechaInvalido", "Fecha de terminación del embarazo"));
					}
	
					if (faltaOtroTipoTrabajoParto)
					{
						errors.add("faltaOtroTipoTrabajoParto", new ActionMessage("errors.descripcionOtro", "tipo de trabajo de parto"));
					}
					if(faltaOtraComplicacionEmbarazo)
					{ 
						errors.add("faltaOtraComplicacionEmbarazo",  new ActionMessage("errors.descripcionOtro", "complicación del embarazo"));
					}
					if (faltaOtroTipoParto)
					{
						errors.add("faltaOtroTrabajoParto", new ActionMessage("errors.descripcionOtro", "tipo de parto"));
					}
					if (errorMinimoHijo)
					{
						errors.add ("errorMinimoHijo", new ActionMessage("error.antecedentes.ginecoobstetricos.minNumeroHijos"));
					}
					if (errorFechaPosterior)
					{
						errors.add ("errorFechaPosterior", new ActionMessage("errors.fechaPosteriorAOtraDeReferencia", "de terminación del embarazo", "actual"));
					}
				}
	
			}
		}
				
		if( !errors.isEmpty() )
			this.hayErrores = true;
		
		return errors;
	}
	
	public boolean esFechaAnterior ( int dia, int mes, int anio)
	{
		GregorianCalendar calendar = new GregorianCalendar(new SimpleTimeZone(-18000000, "America/Bogota"));

		int anioAct = calendar.get(Calendar.YEAR);
		int mesAct  = calendar.get(Calendar.MONTH)+1;
		int diaAct  = calendar.get(Calendar.DAY_OF_MONTH);

		if( anio > anioAct )
			return false;
		else
		if( anio == anioAct )
		{
			if( mes > mesAct )
				return false;
			else 
			if( mes == mesAct )
				if( dia > diaAct )
					return false;
		}
		return true;
	}

	
	/**
	 * Returns the rangoEdadMenarquia.
	 * @return String
	 */
	public int getRangoEdadMenarquia() 
	{
		return rangoEdadMenarquia;
	}

	/**
	 * Sets the rangoEdadMenarquia.
	 * @param rangoEdadMenarquia The rangoEdadMenarquia to set
	 */
	public void setRangoEdadMenarquia(int rangoEdadMenarquia) 
	{
		this.rangoEdadMenarquia = rangoEdadMenarquia;
	}

	/**
	 * Returns the otraEdadMenarquia.
	 * @return String
	 */
	public String getOtraEdadMenarquia()
	{
		return otraEdadMenarquia;
	}

	/**
	 * Sets the otraEdadMenarquia.
	 * @param otraEdadMenarquia The otraEdadMenarquia to set
	 */
	public void setOtraEdadMenarquia(String otraEdadMenarquia)
	{
		this.otraEdadMenarquia = otraEdadMenarquia;
	}

	/**
	 * Returns the rangoEdadMenopausia.
	 * @return String
	 */
	public int getRangoEdadMenopausia() 
	{
		return rangoEdadMenopausia;
	}

	/**
	 * Sets the rangoEdadMenopausia.
	 * @param rangoEdadMenopausia The rangoEdadMenopausia to set
	 */
	public void setRangoEdadMenopausia(int rangoEdadMenopausia) 
	{
		this.rangoEdadMenopausia = rangoEdadMenopausia;
	}

	/**
	 * Returns the otraEdadMenopausia.
	 * @return String
	 */
	public String getOtraEdadMenopausia()
	 {
		return otraEdadMenopausia;
	}

	/**
	 * Sets the otraEdadMenopausia.
	 * @param otraEdadMenopausia The otraEdadMenopausia to set
	 */
	public void setOtraEdadMenopausia(String otraEdadMenopausia) 
	{
		this.otraEdadMenopausia = otraEdadMenopausia;
	}

	/**
	 * Returns the duracionMenstruacion.
	 * @return int
	 */
	public String getDuracionMenstruacion() 
	{
		return duracionMenstruacion;
	}

	/**
	 * Sets the duracionMenstruacion.
	 * @param duracionMenstruacion The duracionMenstruacion to set
	 */
	public void setDuracionMenstruacion(String duracionMenstruacion)
	{
		this.duracionMenstruacion = duracionMenstruacion;
	}
	
	/**
	 * @return Returns the dolorMenstruacion.
	 */
	public String getDolorMenstruacion()
	{
		return dolorMenstruacion;
	}
	/**
	 * @param dolorMenstruacion The dolorMenstruacion to set.
	 */
	public void setDolorMenstruacion(String dolorMenstruacion)
	{
		this.dolorMenstruacion = dolorMenstruacion;
	}
	/**
	 * Returns the fechaUltimaRegla.
	 * @return String
	 */
	public String getFechaUltimaRegla() 
	{
		return fechaUltimaRegla;
	}

	/**
	 * Sets the fechaUltimaRegla.
	 * @param fechaUltimaRegla The fechaUltimaRegla to set
	 */
	public void setFechaUltimaRegla(String fechaUltimaRegla)
	{
		this.fechaUltimaRegla = fechaUltimaRegla;
	}

	/**
	 * Returns the conceptoMenstruacion.
	 * @return String
	 */
	public int getConceptoMenstruacion() 
	{
		return conceptoMenstruacion;
	}

	/**
	 * Sets the conceptoMenstruacion.
	 * @param conceptoMenstruacion The conceptoMenstruacion to set
	 */
	public void setConceptoMenstruacion(int conceptoMenstruacion)
	{
		this.conceptoMenstruacion = conceptoMenstruacion;
	}

	/**
	 * Returns the fechaUltimaMamografia.
	 * @return String
	 */
	public String getFechaUltimaMamografia() 
	{
		return fechaUltimaMamografia;
	}

	/**
	 * Sets the fechaUltimaMamografia.
	 * @param fechaUltimaMamografia The fechaUltimaMamografia to set
	 */
	public void setFechaUltimaMamografia(String fechaUltimaMamografia)
	{
		this.fechaUltimaMamografia = fechaUltimaMamografia;
	}

	/**
	 * Returns the descUltimaMamografia.
	 * @return String
	 */
	public String getDescUltimaMamografia()
	{
		return descUltimaMamografia;
	}

	/**
	 * Sets the descUltimaMamografia.
	 * @param descUltimaMamografia The descUltimaMamografia to set
	 */
	public void setDescUltimaMamografia(String descUltimaMamografia) 
	{
		this.descUltimaMamografia = descUltimaMamografia;
	}

	/**
	 * Returns the fechaUltimaCitologia.
	 * @return String
	 */
	public String getFechaUltimaCitologia()
	{
		return fechaUltimaCitologia;
	}

	/**
	 * Sets the fechaUltimaCitologia.
	 * @param fechaUltimaCitologia The fechaUltimaCitologia to set
	 */
	public void setFechaUltimaCitologia(String fechaUltimaCitologia) 
	{
		this.fechaUltimaCitologia = fechaUltimaCitologia;
	}

	/**
	 * Returns the descUltimaCitologia.
	 * @return String
	 */
	public String getDescUltimaCitologia() 
	{
		return descUltimaCitologia;
	}

	/**
	 * Sets the descUltimaCitologia.
	 * @param descUltimaCitologia The descUltimaCitologia to set
	 */
	public void setDescUltimaCitologia(String descUltimaCitologia)
	{
		this.descUltimaCitologia = descUltimaCitologia;
	}

	/**
	 * Returns the descUltimoProcedimiento.
	 * @return String
	 */
	public String getDescUltimoProcedimiento() 
	{
		return descUltimoProcedimiento;
	}

	/**
	 * Sets the descUltimoProcedimiento.
	 * @param descUltimoProcedimiento The descUltimoProcedimiento to set
	 */
	public void setDescUltimoProcedimiento(String descUltimoProcedimiento) 
	{
		this.descUltimoProcedimiento = descUltimoProcedimiento;
	}
	/**
	 * Returns the observacionesViejas.
	 * @return StringBuffer
	 */
	public String getObservacionesViejas()
	{
		return observacionesViejas;
	}

	/**
	 * Sets the observacionesViejas.
	 * @param observacionesViejas The observacionesViejas to set
	 */
	public void setObservacionesViejas(String observacionesViejas) 
	{
		this.observacionesViejas = observacionesViejas;
	}

	/**
	 * Returns the observacionesNuevas.
	 * @return StringBuffer
	 */
	public String getObservacionesNuevas() 
	{
		return observacionesNuevas;
	}

	/**
	 * Sets the observacionesNuevas.
	 * @param observacionesNuevas The observacionesNuevas to set
	 */
	public void setObservacionesNuevas(String observacionesNuevas) 
	{
		this.observacionesNuevas = observacionesNuevas;
	}
	
	/**
	 * Asigna un valor dinámico nuevo
	 */
	public void setValue(String key, Object value) 
	{
		valores.put(key, value);
	}


	/**
	 * Retorna el valor de un campo dado su nombre
	 */
	public Object getValue(String key) 
	{
		return valores.get(key);
	}
	

	/**
	 * Returns the estado.
	 * @return String
	 */
	public String getEstado()
	{
		return estado;
	}

	/**
	 * Sets the estado.
	 * @param estado The estado to set
	 */
	public void setEstado(String estado) 
	{
		this.estado = estado;
	}

	/**
	 * Returns the numMetodosAnticonceptivos.
	 * @return int
	 */
	public int getNumMetodosAnticonceptivos()
	{
		return numMetodosAnticonceptivos;
	}

	/**
	 * Sets the numMetodosAnticonceptivos.
	 * @param numMetodosAnticonceptivos The numMetodosAnticonceptivos to set
	 */
	public void setNumMetodosAnticonceptivos(int numMetodosAnticonceptivos)
	{
		this.numMetodosAnticonceptivos = numMetodosAnticonceptivos;
	}

	/**
	 * Returns the nombreRangoEdadMenarquia.
	 * @return String
	 */
	public String getNombreRangoEdadMenarquia() 
	{
		return nombreRangoEdadMenarquia;
	}

	/**
	 * Sets the nombreRangoEdadMenarquia.
	 * @param nombreRangoEdadMenarquia The nombreRangoEdadMenarquia to set
	 */
	public void setNombreRangoEdadMenarquia(String nombreRangoEdadMenarquia) 
	{
		this.nombreRangoEdadMenarquia = nombreRangoEdadMenarquia;
	}

	/**
	 * Returns the nombreRangoEdadMenopausia.
	 * @return String
	 */
	public String getNombreRangoEdadMenopausia() 
	{
		return nombreRangoEdadMenopausia;
	}

	/**
	 * Sets the nombreRangoEdadMenopausia.
	 * @param nombreRangoEdadMenopausia The nombreRangoEdadMenopausia to set
	 */
	public void setNombreRangoEdadMenopausia(String nombreRangoEdadMenopausia) 
	{
		this.nombreRangoEdadMenopausia = nombreRangoEdadMenopausia;
	}

	/**
	 * Returns the historicos.
	 * @return ArrayList
	 */
	public ArrayList getHistoricos() 
	{
		return historicos;
	}

	/**
	 * Sets the historicos.
	 * @param historicos The historicos to set
	 */
	public void setHistoricos(ArrayList historicos) 
	{
		this.historicos = historicos;
	}

	/**
	 * Returns the existeRangoEdadMenarquia.
	 * @return boolean
	 */
	public boolean isExisteRangoEdadMenarquia() 
	{
		return existeRangoEdadMenarquia;
	}

	/**
	 * Sets the existeRangoEdadMenarquia.
	 * @param existeRangoEdadMenarquia The existeRangoEdadMenarquia to set
	 */
	public void setExisteRangoEdadMenarquia(boolean existeRangoEdadMenarquia)
	{
		this.existeRangoEdadMenarquia = existeRangoEdadMenarquia;
	}

	/**
	 * Returns the existeRangoEdadMenopausia.
	 * @return boolean
	 */
	public boolean isExisteRangoEdadMenopausia() 
	{
		return existeRangoEdadMenopausia;
	}

	/**
	 * Sets the existeRangoEdadMenopausia.
	 * @param existeRangoEdadMenopausia The existeRangoEdadMenopausia to set
	 */
	public void setExisteRangoEdadMenopausia(boolean existeRangoEdadMenopausia) 
	{
		this.existeRangoEdadMenopausia = existeRangoEdadMenopausia;
	}

	/**
	 * Returns the numeroEmbarazos.
	 * @return int
	 */
	public int getNumeroEmbarazos() 
	{
		return numeroEmbarazos;
	}

	/**
	 * Sets the numeroEmbarazos.
	 * @param numeroEmbarazos The numeroEmbarazos to set
	 */
	public void setNumeroEmbarazos(int numeroEmbarazos) 
	{
		this.numeroEmbarazos = numeroEmbarazos;
	}

	/**
	 * Returns the valores.
	 * @return Map
	 */
	public Map getValores() 
	{
		return valores;
	}

	/**
	 * Retorna la Fecha de la última ecografia de la paciente, no tiene ningún
	 * formato.
	 * @return String
	 */
	public String getFechaUltimaEcografia() 
	{
		return fechaUltimaEcografia;
	}

	/**
	 * Asigna la Fecha de la última ecografia de la paciente, no tiene ningún
	 * formato.
	 * @param fechaUltimaEcografia The fechaUltimaEcografia to set
	 */
	public void setFechaUltimaEcografia(String fechaUltimaEcografia) 
	{
		this.fechaUltimaEcografia = fechaUltimaEcografia;
	}

	/**
	 * Retorna la Descripción de la última ecografia de la paciente.
	 * @return String
	 */
	public String getDescUltimaEcografia() 
	{
		return descUltimaEcografia;
	}

	/**
	 * Asigna la Descripción de la última ecografia de la paciente.
	 * @param descUltimaEcografia The descUltimaEcografia to set
	 */
	public void setDescUltimaEcografia(String descUltimaEcografia) 
	{
		this.descUltimaEcografia = descUltimaEcografia;
	}

	/**
	 * Retorna el número de dias del ciclo menstrual
	 * @return String
	 */
	public String getCicloMenstrual() 
	{
		return cicloMenstrual;
	}

	/**
	 * Asigna el Número de dias del ciclo menstrual
	 * @param cicloMenstrual The cicloMenstrual to set
	 */
	public void setCicloMenstrual(String cicloMenstrual) 
	{
		this.cicloMenstrual = cicloMenstrual;
	}

	/**
	 * Retorna las observaciones de las caracteristicas de la menstruacion
	 * @return String
	 */
	public String getObservacionesMenstruacion() 
	{
		return observacionesMenstruacion;
	}

	/**
	 * Asigna las observaciones de las caracteristicas de la menstruacion
	 * @param observacionesMenstruacion The observacionesMenstruacion to set
	 */
	public void setObservacionesMenstruacion(String observacionesMenstruacion) 
	{
		this.observacionesMenstruacion = observacionesMenstruacion;
	}

	/**
	 * Retorna el número de gestaciones 
	 * @return int
	 */
	public int getNumGestaciones() 
	{
		return numGestaciones;
	}

	/**
	 * Asigna el número de gestaciones 
	 * @param numGestaciones The numGestaciones to set
	 */
	public void setNumGestaciones(int numGestaciones) 
	{
		this.numGestaciones = numGestaciones;
	}

	/**
	 * Retorna el número de partos
	 * @return int
	 */
	public int getNumPartos() 
	{
		return numPartos;
	}

	/**
	 * Asigna el número de partos
	 * @param numPartos The numPartos to set
	 */
	public void setNumPartos(int numPartos) 
	{
		this.numPartos = numPartos;
	}

	/**
	 * Retorna el número de abortos 
	 * @return int
	 */
	public int getNumAbortos() 
	{
		return numAbortos;
	}

	/**
	 * Asigna el número de abortos 
	 * @param numAbortos The numAbortos to set
	 */
	public void setNumAbortos(int numAbortos) 
	{
		this.numAbortos = numAbortos;
	}

	/**
	 * Retorna el número de cesareas 
	 * @return int
	 */
	public int getNumCesareas() 
	{
		return numCesareas;
	}

	/**
	 * Asigna el número de cesareas 
	 * @param numCesareas The numCesareas to set
	 */
	public void setNumCesareas(int numCesareas) 
	{
		this.numCesareas = numCesareas;
	}

	/**
	 * Retorna el número de hijos nacidos vivos
	 * @return int
	 */
	public int getNumVivos() 
	{
		return numVivos;
	}

	/**
	 * Asigna el número de hijos nacidos vivos
	 * @param numVivos The numVivos to set
	 */
	public void setNumVivos(int numVivos) 
	{
		this.numVivos = numVivos;
	}

	/**
	 * Retorna el número de hijos nacidos muertos
	 * @return int
	 */
	public int getNumMuertos() 
	{
		return numMuertos;
	}

	/**
	 * Asigna el número de hijos nacidos muertos
	 * @param numMuertos The numMuertos to set
	 */
	public void setNumMuertos(int numMuertos) 
	{
		this.numMuertos = numMuertos;
	}

	/**
	 * Returns the hayErrores.
	 * @return boolean
	 */
	public boolean isHayErrores() 
	{
		return hayErrores;
	}

	/**
	 * Sets the hayErrores.
	 * @param hayErrores The hayErrores to set
	 */
	public void setHayErrores(boolean hayErrores) 
	{
		this.hayErrores = hayErrores;
	}

	/**
	 * Returns the numPartosAnteriores.
	 * @return int
	 */
	public int getNumPartosGrabados() 
	{
		return numPartosGrabados;
	}

	/**
	 * Sets the numPartosAnteriores.
	 * @param numPartosAnteriores The numPartosAnteriores to set
	 */
	public void setNumPartosGrabados(int numPartosAnteriores) 
	{
		this.numPartosGrabados = numPartosAnteriores;
	}

	/**
	 * Retorna el número de abortos ingresados anteriormente
	 * @return int
	 */
	public int getNumAbortosGrabados() 
	{
		return numAbortosGrabados;
	}

	/**
	 * Asigna el número de abortos ingresados anteriormente
	 * @param numAbortosAnteriores The numAbortosAnteriores to set
	 */
	public void setNumAbortosGrabados(int numAbortosAnteriores) 
	{
		this.numAbortosGrabados = numAbortosAnteriores;
	}

	/**
	 * Retorna el número de cesareas ingresadas anteriormente
	 * @return int
	 */
	public int getNumCesareasGrabadas()
	{
		return numCesareasGrabadas;
	}

	/**
	 * Asigna el número de cesareas ingresadas anteriormente
	 * @param numCesareasAnteriores The numCesareasAnteriores to set
	 */
	public void setNumCesareasGrabadas(int numCesareasAnteriores)
	{
		this.numCesareasGrabadas = numCesareasAnteriores;
	}

	/**
	 * Retorna el número de hijos nacidos vivos ingresados anteriormente
	 * @return int
	 */
	public int getNumVivosGrabados()
	{
		return numVivosGrabados;
	}

	/**
	 * Asigna el número de hijos nacidos vivos ingresados anteriormente
	 * @param numVivosGrabados The numVivosGrabados to set
	 */
	public void setNumVivosGrabados(int numVivosGrabados)
	{
		this.numVivosGrabados = numVivosGrabados;
	}

	/**
	 * Retorna el número de hijos nacidos muertos ingresados anteriormente
	 * @return int
	 */
	public int getNumMuertosGrabados()
	{
		return numMuertosGrabados;
	}

	/**
	 * Asigna el número de hijos nacidos muertos ingresados anteriormente
	 * @param numMuertosGrabados The numMuertosGrabados to set
	 */
	public void setNumMuertosGrabados(int numMuertosGrabados)
	{
		this.numMuertosGrabados = numMuertosGrabados;
	}

	/**
	 * Retorna la edad en años en la que se empezó la vida sexual
	 * @return String
	 */
	public String getInicioVidaSexual()
	{
		return inicioVidaSexual;
	}

	/**
	 * Asigna la edad en años en la que se empezó la vida sexual
	 * @param inicioVidaSexual The inicioVidaSexual to set
	 */
	public void setInicioVidaSexual(String inicioVidaSexual)
	{
		this.inicioVidaSexual = inicioVidaSexual;
	}

	/**
	 * Retorna la edad en años en la que se empezó la vida obstétrica
	 * @return String
	 */
	public String getInicioVidaObstetrica()
	{
		return inicioVidaObstetrica;
	}

	/**
	 * Asigna la edad en años en la que se empezó la vida obstétrica
	 * @param inicioVidaObstetrica The inicioVidaObstetrica to set
	 */
	public void setInicioVidaObstetrica(String inicioVidaObstetrica)
	{
		this.inicioVidaObstetrica = inicioVidaObstetrica;
	}

	/**
	 * Retorna la información de embarazos, número de gestaciones
	 * @return String
	 */
	public String getGInfoEmbarazos()
	{
		return gInfoEmbarazos;
	}

	/**
	 * Asigna la información de embarazos, número de gestaciones
	 * @param gInfoEmbarazos The gInfoEmbarazos to set
	 */
	public void setGInfoEmbarazos(String gInfoEmbarazos)
	{
		this.gInfoEmbarazos = gInfoEmbarazos;
	}

	/**
	 * Retorna la información de embarazos, número de partos vaginales
	 * @return String
	 */
	public String getPInfoEmbarazos()
	{
		return pInfoEmbarazos;
	}

	/**
	 * Asigna la información de embarazos, número de partos vaginales
	 * @param pInfoEmbarazos The pInfoEmbarazos to set
	 */
	public void setPInfoEmbarazos(String pInfoEmbarazos)
	{
		this.pInfoEmbarazos = pInfoEmbarazos;
	}

	/**
	 * Retorna la información de embarazos, número de abortos
	 * @return String
	 */
	public String getAInfoEmbarazos()
	{
		return aInfoEmbarazos;
	}

	/**
	 * Asigna la información de embarazos, número de abortos
	 * @param aInfoEmbarazos The aInfoEmbarazos to set
	 */
	public void setAInfoEmbarazos(String aInfoEmbarazos)
	{
		this.aInfoEmbarazos = aInfoEmbarazos;
	}

	/**
	 * Retorna la información de embarazos, número de cesáreas
	 * @return String
	 */
	public String getCInfoEmbarazos()
	{
		return cInfoEmbarazos;
	}

	/**
	 * Asigna la información de embarazos, número de cesáreas
	 * @param cInfoEmbarazos The cInfoEmbarazos to set
	 */
	public void setCInfoEmbarazos(String cInfoEmbarazos)
	{
		this.cInfoEmbarazos = cInfoEmbarazos;
	}

	/**
	 * Retorna la información de embarazos, número de hijos nacidos vivos
	 * @return String
	 */
	public String getVInfoEmbarazos()
	{
		return vInfoEmbarazos;
	}

	/**
	 * Asigna la información de embarazos, número de hijos nacidos vivos
	 * @param vInfoEmbarazos The vInfoEmbarazos to set
	 */
	public void setVInfoEmbarazos(String vInfoEmbarazos)
	{
		this.vInfoEmbarazos = vInfoEmbarazos;
	}

	/**
	 * Retorna la información de embarazos, número de hijos nacidos muertos
	 * @return String
	 */
	public String getMInfoEmbarazos()
	{
		return mInfoEmbarazos;
	}

	/**
	 * Asigna la información de embarazos, número de hijos nacidos muertos
	 * @param mInfoEmbarazos The mInfoEmbarazos to set
	 */
	public void setMInfoEmbarazos(String mInfoEmbarazos)
	{
		this.mInfoEmbarazos = mInfoEmbarazos;
	}

	/**
	 * Returns the descUltimaDensimetriaOsea.
	 * @return String
	 */
	public String getDescUltimaDensimetriaOsea()
	{
		return descUltimaDensimetriaOsea;
	}

	/**
	 * Returns the fechaUltimaDensimetriaOsea.
	 * @return String
	 */
	public String getFechaUltimaDensimetriaOsea()
	{
		return fechaUltimaDensimetriaOsea;
	}

	/**
	 * Sets the descUltimaDensimetriaOsea.
	 * @param descUltimaDensimetriaOsea The descUltimaDensimetriaOsea to set
	 */
	public void setDescUltimaDensimetriaOsea(String descUltimaDensimetriaOsea)
	{
		this.descUltimaDensimetriaOsea = descUltimaDensimetriaOsea;
	}

	/**
	 * Sets the fechaUltimaDensimetriaOsea.
	 * @param fechaUltimaDensimetriaOsea The fechaUltimaDensimetriaOsea to set
	 */
	public void setFechaUltimaDensimetriaOsea(String fechaUltimaDensimetriaOsea)
	{
		this.fechaUltimaDensimetriaOsea = fechaUltimaDensimetriaOsea;
	}
	
	/**
	 * @return Returns the ocultarCabezotes.
	 */
	public String getOcultarCabezotes() {
		return ocultarCabezotes;
	}
	/**
	 * @param ocultarCabezotes The ocultarCabezotes to set.
	 */
	public void setOcultarCabezotes(String ocultarCabezotes) {
		this.ocultarCabezotes = ocultarCabezotes;
	}
	/**
	 * @return Returns the selectDolorMenstruación.
	 */
	public int getSelectDolorMenstruacion()
	{
		return selectDolorMenstruacion;
	}
	/**
	 * @param selectDolorMenstruación The selectDolorMenstruación to set.
	 */
	public void setSelectDolorMenstruacion(int selectDolorMenstruacion)
	{
		this.selectDolorMenstruacion = selectDolorMenstruacion;
	}
	/**
	 * @return Returns the hayErrorVida.
	 */
	public boolean isHayErrorVida()
	{
		return hayErrorVida;
	}
	/**
	 * @param hayErrorVida The hayErrorVida to set.
	 */
	public void setHayErrorVida(boolean hayErrorVida)
	{
		this.hayErrorVida = hayErrorVida;
	}
	/**
	 * @return Retorna p2500.
	 */
	public String getP2500() {
		return p2500;
	}
	/**
	 * @param Asigna p2500.
	 */
	public void setP2500(String p2500) {
		this.p2500 = p2500;
	}
	/**
	 * @return Retorna p4000.
	 */
	public String getP4000() {
		return p4000;
	}
	/**
	 * @param Asigna p4000.
	 */
	public void setP4000(String p4000) {
		this.p4000 = p4000;
	}
	

	
	/**
	 * @return Retorna ectropicos.
	 */
	public String getEctropicos() {
		return ectropicos;
	}
	/**
	 * @param Asigna ectropicos.
	 */
	public void setEctropicos(String ectropicos) {
		this.ectropicos = ectropicos;
	}
	/**
	 * @return Retorna finEmbarazoAnterior.
	 */
	public String getFinEmbarazoAnterior() {
		return finEmbarazoAnterior;
	}
	/**
	 * @param Asigna finEmbarazoAnterior.
	 */
	public void setFinEmbarazoAnterior(String finEmbarazoAnterior) {
		this.finEmbarazoAnterior = finEmbarazoAnterior;
	}
	/**
	 * @return Retorna finEmbarazoMayor1o5.
	 */
	public String getFinEmbarazoMayor1o5() {
		return finEmbarazoMayor1o5;
	}
	/**
	 * @param Asigna finEmbarazoMayor1o5.
	 */
	public void setFinEmbarazoMayor1o5(String finEmbarazoMayor1o5) {
		this.finEmbarazoMayor1o5 = finEmbarazoMayor1o5;
	}
	/**
	 * @return Retorna mayorA2.
	 */
	public String getMayorA2() {
		return mayorA2;
	}
	/**
	 * @param Asigna mayorA2.
	 */
	public void setMayorA2(String mayorA2) {
		this.mayorA2 = mayorA2;
	}
	/**
	 * @return Retorna multiples.
	 */
	public String getMultiples() {
		return multiples;
	}
	/**
	 * @param Asigna multiples.
	 */
	public void setMultiples(String multiples) {
		this.multiples = multiples;
	}
	/**
	 * @return Retorna prematuros.
	 */
	public String getPrematuros() {
		return prematuros;
	}
	/**
	 * @param Asigna prematuros.
	 */
	public void setPrematuros(String prematuros) {
		this.prematuros = prematuros;
	}


	public String getInfeccionPostparto() {
		return infeccionPostparto;
	}

	public void setInfeccionPostparto(String infeccion_postparto) {
		this.infeccionPostparto = infeccion_postparto;
	}

	public String getMalformacion() {
		return malformacion;
	}

	public void setMalformacion(String malformacion) {
		this.malformacion = malformacion;
	}

	public String getMuertePerinatal() {
		return muertePerinatal;
	}

	public void setMuertePerinatal(String muerte_perinatal) {
		this.muertePerinatal = muerte_perinatal;
	}

	public String getRetencionPlacentaria() {
		return retencionPlacentaria;
	}

	public void setRetencionPlacentaria(String retencion_placentaria) {
		this.retencionPlacentaria = retencion_placentaria;
	}

	public int getVag() {
		return vag;
	}

	public void setVag(int vag) {
		this.vag = vag;
	}

	public String getCirugiaGineco() {
		return cirugiaGineco;
	}

	public void setCirugiaGineco(String cirugia_gineco) {
		this.cirugiaGineco = cirugia_gineco;
	}

	public String getCualCirugiaGineco() {
		return cualCirugiaGineco;
	}

	public void setCualCirugiaGineco(String cual_cirugia_gineco) {
		this.cualCirugiaGineco = cual_cirugia_gineco;
	}

	public String getCualEnferTransSex() {
		return cualEnferTransSex;
	}

	public void setCualEnferTransSex(String cual_enfer_trans_sex) {
		this.cualEnferTransSex = cual_enfer_trans_sex;
	}

	public String getCualHistoInfertilidad() {
		return cualHistoInfertilidad;
	}

	public void setCualHistoInfertilidad(String cual_histo_infertilidad) {
		this.cualHistoInfertilidad = cual_histo_infertilidad;
	}

	public String getEnferTransSexual() {
		return enferTransSexual;
	}

	public void setEnferTransSexual(String enfer_trans_sexual) {
		this.enferTransSexual = enfer_trans_sexual;
	}

	public String getFlujoVaginal() {
		return flujoVaginal;
	}

	public void setFlujoVaginal(String flujo_vaginal) {
		this.flujoVaginal = flujo_vaginal;
	}

	public String getHistoriaInfertilidad() {
		return historiaInfertilidad;
	}

	public void setHistoriaInfertilidad(String historia_infertilidad) {
		this.historiaInfertilidad = historia_infertilidad;
	}

	public String getSangradoAnormal() {
		return sangradoAnormal;
	}

	public void setSangradoAnormal(String sangrado_anormal) {
		this.sangradoAnormal = sangrado_anormal;
	}

	public String getMuertosAntes1Semana()
	{
		return muertosAntes1Semana;
	}

	public void setMuertosAntes1Semana(String muertosAntes1Semana)
	{
		this.muertosAntes1Semana = muertosAntes1Semana;
	}

	public String getMuertosDespues1Semana()
	{
		return muertosDespues1Semana;
	}

	public void setMuertosDespues1Semana(String muertosDespues1Semana)
	{
		this.muertosDespues1Semana = muertosDespues1Semana;
	}

	public String getVivosActualmente()
	{
		return vivosActualmente;
	}

	public void setVivosActualmente(String vivosActualmente)
	{
		this.vivosActualmente = vivosActualmente;
	}

	public String getTipoEmbarazo()
	{
		return tipoEmbarazo;
	}

	public void setTipoEmbarazo(String tipoEmbarazo)
	{
		this.tipoEmbarazo = tipoEmbarazo;
	}

	/**
	 * @return the esProcesoExitoso
	 */
	public String getEsProcesoExitoso() {
		return esProcesoExitoso;
	}

	/**
	 * @param esProcesoExitoso the esProcesoExitoso to set
	 */
	public void setEsProcesoExitoso(String esProcesoExitoso) {
		this.esProcesoExitoso = esProcesoExitoso;
	}
}
