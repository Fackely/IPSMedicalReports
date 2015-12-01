/*
 * @author Jorge Armando Osorio Velasquez
 */
package com.princetonsa.actionform.historiaClinica;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;


import util.ConstantesBD;
import util.RespuestaValidacion;
import util.UtilidadFecha;
import util.UtilidadValidacion;
import util.Utilidades;
import util.historiaClinica.ConstantesBDHistoriaClinica;

/**
 * 
 * @author Jorge Armando Osorio Velasquez.
 *
 */
public class InformacionRecienNacidosForm extends ValidatorForm
{
	/**
	 * 
	 */
	private String estado;
	
	/**
	 * 
	 */
	private boolean insercionModificacionExitosa=false;

	/**
	 * codigo de la cirugia a consultar.
	 */
	private String codigoCirugia;
	
	
	/**
	 * Mapa que contiene todas las solicitudes-admision, relacionadas a una cirugia con de parto o cesaria.
	 */
	private HashMap solicitudes;
	
	/**
	 * Mapara para cargar los hijos de una determinada cirugia.
	 */
	private HashMap hijos;
	
	/**
	 * 
	 */
	private int maxPageItems;
	
	/**
	 * Variable para manejar el codigo de la cirugia a la cual se le quiere consultar el detalle.
	 */
	private String itemSeleccionado;
	
	/**
	 * Variable para manejar el numero del registro del hijo seleccionado para modificar.
	 */
	private String hijoSeleccionado;
	
	/**
	 * Fecha inicial para la busqued de rangos.
	 */
	private String fechaInicial;

	/**
	 * Fecha final para la busqueda por fechas.
	 */
	private String fechaFinal;
	
	/**
	 * Variable que me indica si se estï¿½modificando un registro, o se estï¿½insertando.
	 */
	private boolean modificacion;
	
	/**
	 * Variable usada para ocultar el encabezado
	 */
	private boolean ocultarEncabezado;
	
	
	///variables para mostrar las secciones que se estan mostrando en la pantalla
	private boolean secInfoGeneral;
	private boolean secEgreso;
	private boolean secANI;
	private boolean secEF;
	private boolean secRN;
	private boolean secSA;
	private boolean secAP;
	
	
	private String codigoEnfermedad;
	
	
	private ArrayList profesionalesInstitucion;
	
	
	
	
	
	
	
	///campos para el manejo de los datos de la funcionalidad
	private String fechaNacimiento;
	private String horaNacimiento;
	private String sexo;
	private String vivo;
	private String diagRN;
	private String cieDiagRN;
	private String nombreDiagRN;
	private String fechaMuerte;
	private String horaMuerte;
	private String diagMuerte;
	private String cieDiagMuerte;
	private String nombreDiagMuerte;
	private String momentoMuerte;
	private String falleceSalaParto;
	private String pesoEdadGestacion;
	private String vitaminaK;
	private String profilaxisOftalmico;
	private String hemoclasificacion;
	private String sensibilizado;
	private String defectosCongenitos;
	private String diagDefCong;
	private String cieDiagDefCong;
	private String nombreDiagDefCong;
	private String fechaEgreso;
	private String horaEgreso;
	private String condicionEgreso;
	private String lactancia;
	private String pesoEgreso;
	private String nuip;
	private String vacunaPolio;
	private String vacunaBCG;
	private String vacunaHepatitisB;
	private String sano;
	private String conductaSeguir;
	private String conductaSeguir_ani;
	private String codigoProfesionalAtendio;
	
	private String numeroCertificadoNacimiento;
	
	
	//
	//Unificacion de Codigo  y TipoCie Diagnostico 
	private String 	uniCodyCieDiagRN;
	private int numeroSolicitud = 0;
	private String ficha;
	private String fichaMte;
	private String 	uniCodyCieDiagMte;
	private String uniCodyCieDiagDefCon;
	//
	
	
	private String usuarioProceso;
	private String fechaProceso;
	private String horaProceso;
	
	private String codigo;
	
	private String edadGestacionExamen;
	
	private String observacionesEgreso;
	
	private String finalizada;

	private HashMap diagEgreso;
	
	private HashMap reanimacion;
	
	private HashMap tamizacionNeonatal;
	
	private HashMap secAdaptacionNeonatalInmediata;
	
	private HashMap secExamenesFisicos;
	
	private HashMap secDiagnosticoRecienNacido;
	
	
	private HashMap secSano;
	
	private HashMap secApgar;


	
	public void resetIndicadoresSecciones()
	{
		this.secInfoGeneral=false;
		this.secEgreso=false;
		this.secANI=false;
		this.secEF=false;
		this.secRN=false;
		this.secSA=false;
		this.secAP=false;
		
	}
	
	
	public void reset()
	{
		this.solicitudes=new HashMap();
		this.solicitudes.put("numRegistros", "0");
		
		this.hijos=new HashMap();
		this.hijos.put("numRegistros", "0");
		
		this.fechaInicial="";
		this.fechaFinal="";
		
		this.maxPageItems=0;
		
		this.itemSeleccionado="";
		
		this.hijoSeleccionado="";
		
		this.modificacion=false;
		
		this.finalizada=ConstantesBD.acronimoNo;
		
		this.resetIndicadoresSecciones();
				
		this.resetCamposHijos();
		
		this.profesionalesInstitucion=new ArrayList();
		
		this.codigoCirugia="";
		
		this.ocultarEncabezado = false;
		
		this.insercionModificacionExitosa=false;
	}

	
	public void resetCamposHijos()
	{
//		/campos para el manejo de los datos de la funcionalidad
		this.fechaNacimiento="";
		this.horaNacimiento="";
		this.sexo="";
		this.vivo="";
		this.diagRN="";
		this.cieDiagRN="";
		//
		this.uniCodyCieDiagRN= "";
		this.uniCodyCieDiagMte= "";
		this.uniCodyCieDiagDefCon ="";
		this.ficha = "";
		this.fichaMte = "";
		//		
		this.nombreDiagRN="";
		this.fechaMuerte="";
		this.horaMuerte="";
		this.diagMuerte="";
		this.cieDiagMuerte="";
		this.nombreDiagMuerte="";
		this.momentoMuerte="";
		this.falleceSalaParto="";
		this.pesoEdadGestacion="";
		this.vitaminaK="";
		this.profilaxisOftalmico="";
		this.hemoclasificacion="";
		this.sensibilizado="";
		this.defectosCongenitos="";
		this.diagDefCong="";
		this.cieDiagDefCong="";
		this.nombreDiagDefCong="";
		this.fechaEgreso="";
		this.horaEgreso="";
		this.condicionEgreso="";
		this.lactancia="";
		this.pesoEgreso="";
		this.nuip="";
		this.vacunaPolio="";
		this.vacunaBCG="";
		this.vacunaHepatitisB="";
		this.sano="";
		this.conductaSeguir="";
		this.conductaSeguir_ani="";
		this.codigoProfesionalAtendio="";
		this.usuarioProceso="";
		this.fechaProceso="";
		this.horaProceso="";
		this.codigo="";
		
		this.observacionesEgreso="";
		this.edadGestacionExamen="";
		

		this.diagEgreso=new HashMap();
		this.diagEgreso.put("numRegistros", "0");
		
		this.reanimacion=new HashMap();
		this.reanimacion.put("numRegistros", "0");
		
		this.tamizacionNeonatal=new HashMap();
		this.tamizacionNeonatal.put("numRegistros", "0");
		
		this.secAdaptacionNeonatalInmediata=new HashMap();
		this.secAdaptacionNeonatalInmediata.put("numRegistros", "0");
		
		this.secExamenesFisicos=new HashMap();
		this.secExamenesFisicos.put("numRegistros", "0");
		
		this.secDiagnosticoRecienNacido=new HashMap();
		this.secDiagnosticoRecienNacido.put("numRegistros", "0");
		
		this.secSano=new HashMap();
		this.secSano.put("numRegistros", "0");
		
		this.secApgar=new HashMap();
		this.secApgar.put("numRegistros", "0");
		
		this.numeroCertificadoNacimiento="";
		
		this.codigoEnfermedad="";
	}


	/**
	 * 
	 */
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
		ActionErrors errores=new ActionErrors();
		
		 
		///////new ini

		String[] uniCodCieDiagDefCon = new String[2];
		String[] uniCodCieDiagPrin = new String[2];
		String[] uniCodCieDiagMte = new String[2];
		String[] uniCodCieDiagEgreso = new String[2];
		
		if (this.uniCodyCieDiagDefCon != null)
		{
			 uniCodCieDiagDefCon = this.uniCodyCieDiagDefCon.split(ConstantesBD.separadorSplit);
			if (uniCodCieDiagDefCon.length >=2)
			{
				this.diagDefCong = uniCodCieDiagDefCon[0];
				this.cieDiagDefCong = uniCodCieDiagDefCon[1];
				this.nombreDiagDefCong = uniCodCieDiagDefCon[2];
			}
		}
		
		if (this.uniCodyCieDiagRN != null)
		{
			uniCodCieDiagPrin = this.uniCodyCieDiagRN.split(ConstantesBD.separadorSplit);
			if (uniCodCieDiagPrin.length >=2)
			{
				this.diagRN = uniCodCieDiagPrin[0];
				this.cieDiagRN = uniCodCieDiagPrin[1];
				this.nombreDiagRN = uniCodCieDiagPrin[2];
			}
		}

		if (this.uniCodyCieDiagMte != null)
		{		
			uniCodCieDiagMte = this.uniCodyCieDiagMte.split(ConstantesBD.separadorSplit);
			if (uniCodCieDiagMte.length >=2)
			{
				this.diagMuerte = uniCodCieDiagMte[0];
				this.cieDiagMuerte = uniCodCieDiagMte[1];
				this.nombreDiagMuerte = uniCodCieDiagMte[2];
			}		
		}
	
		if(estado.equals("guardar") && this.getDiagEgreso().containsKey("principal"))
		{
			uniCodCieDiagEgreso =  this.getDiagEgreso("principal").toString().split(ConstantesBD.separadorSplit);
			if (uniCodCieDiagEgreso.length >= 2)
			{
				this.setDiagEgreso("acronimoPrincipal", uniCodCieDiagEgreso[0]);
				this.setDiagEgreso("tipoCiePrincipal", uniCodCieDiagEgreso[1] ) ;
			}
		}
		
		if(this.estado.equals("consultarRango"))
		{
			if(!UtilidadFecha.esFechaValidaSegunAp(this.getFechaInicial()))
			{
				errores.add("",new ActionMessage("errors.required","La fecha Inicial"));
			}
			if(!UtilidadFecha.esFechaValidaSegunAp(this.getFechaFinal()))
			{
				errores.add("",new ActionMessage("errors.required","La fecha Final"));
			}
			if((UtilidadFecha.conversionFormatoFechaABD(this.getFechaInicial())).compareTo(UtilidadFecha.conversionFormatoFechaABD(this.getFechaFinal()))>0)
			{
				errores.add("", new ActionMessage("errors.fechaPosteriorAOtraDeReferencia", "Inicial", "Final"));
			}
			if((UtilidadFecha.conversionFormatoFechaABD(this.getFechaFinal())).compareTo(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()))>0)
			{
				errores.add("", new ActionMessage("errors.fechaPosteriorAOtraDeReferencia", "Final", "Actual"));
			}

		}
		else if(estado.equals("nuevoHijo"))
		{
			this.numeroSolicitud = Integer.parseInt(this.getSolicitudes("numerosolicitud_" + this.getItemSeleccionado())+"" );
			//forma.getSolicitudes("codigocirugia_"+forma.getItemSeleccionado())
		}
		else if(estado.equals("guardar") && this.finalizada.equals(ConstantesBD.acronimoSi))
		{
			
			if(this.uniCodyCieDiagRN.trim().equals("")||this.uniCodyCieDiagRN.trim().equals("@@@@@@@@@@") )
			{
				errores.add("",new ActionMessage("errors.required","El diagnostico de nacimiento"+" (En la seccion Examenes Fisicos)"));
			}
			else
			{
				uniCodCieDiagPrin = this.uniCodyCieDiagRN.split(ConstantesBD.separadorSplit);

				if (uniCodCieDiagPrin.length >=2)
				{
					this.diagRN = uniCodCieDiagPrin[0];
					this.cieDiagRN = uniCodCieDiagPrin[1];
					this.nombreDiagRN = uniCodCieDiagPrin[2];
				}
			}

			uniCodCieDiagEgreso =  this.getDiagEgreso("principal").toString().split(ConstantesBD.separadorSplit);

				if (uniCodCieDiagEgreso.length >= 2)
				{
					this.setDiagEgreso("acronimoPrincipal", uniCodCieDiagEgreso[0]);
					this.setDiagEgreso("tipoCiePrincipal", uniCodCieDiagEgreso[1] ) ;
				}
				
				if (uniCodCieDiagEgreso.length<2)
				{
					errores.add("",new ActionMessage("errors.required","El diagnostico de Egreso"+" (En la seccion Egreso)"));
				}
				
			///////////////////VALIDACIONES DE LA SECCION 	DIAGNOSTICO DEL RECIEN NACIDO/////////////////////			
			for(int i=0;i<Integer.parseInt(this.secDiagnosticoRecienNacido.get("numRegistros")+"");i++)
			{
				if(!secDiagnosticoRecienNacido.containsKey("valor_"+i)||(secDiagnosticoRecienNacido.get("valor_"+i)+"").trim().equals(""))
				{
					errores.add("",new ActionMessage("errors.required",secDiagnosticoRecienNacido.get("descripcioncampo_"+i)+" (En la seccion Identificacion de Riesgo)"));
				}
			}
			///////////////////FIN VALIDACIONES DE LA SECCION	DIAGNOSTICO DEL RECIEN NACIDO/////////////////////
			
			
			///////////////////VALIDACIONES DE LA SECCION ADAPTACION NEONATAL INMEDIATA/////////////////////
			boolean fechaHoraNacimientoCorrectas=true;
			boolean fechaHoraEgresoCorrectas=true;
			
			if(!UtilidadFecha.esFechaValidaSegunAp(this.getFechaNacimiento()))
			{
				fechaHoraNacimientoCorrectas=false;
				errores.add("",new ActionMessage("errors.required","La fecha de Nacimiento" +" (En la seccion Adaptacion Neonatal Inmediata)"));
			}
			else if((UtilidadFecha.conversionFormatoFechaABD(this.getFechaNacimiento())).compareTo(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()))>0)
			{
				fechaHoraNacimientoCorrectas=false;
				errores.add("", new ActionMessage("errors.fechaPosteriorIgualActual", "Nacimiento", "Actual" ));
			}
			else if((UtilidadFecha.conversionFormatoFechaABD(this.getFechaNacimiento())).compareTo(UtilidadFecha.conversionFormatoFechaABD(this.solicitudes.get("fechaadmision_"+this.itemSeleccionado)+""))<0)
			{
				fechaHoraNacimientoCorrectas=false;
				errores.add("", new ActionMessage("errors.fechaAnteriorIgualActual", "Nacimiento", "Admision ("+this.solicitudes.get("fechaadmision_"+this.itemSeleccionado)+")"));
			}
			
			
			
			RespuestaValidacion val=UtilidadFecha.validacionHora(this.horaNacimiento);
			if(this.horaNacimiento.trim().equals(""))
			{
				fechaHoraNacimientoCorrectas=false;
				errores.add("",new ActionMessage("errors.required","La Hora de Nacimiento" +" (En la seccion Adaptacion Neonatal Inmediata)"));
			}
			else if(!val.puedoSeguir)
			{
				fechaHoraNacimientoCorrectas=false;
				errores.add("error en la hora",new ActionMessage("error.errorEnBlanco","(Hora Nacimiento) "+val.textoRespuesta+""));
			}
			else
			{
				final SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm");
				if(this.getFechaNacimiento().equals(UtilidadFecha.getFechaActual()))
				{
					//valisdad q la hora no sea mayor
					try 
					{
						if(timeFormatter.parse(this.getHoraNacimiento()).compareTo(timeFormatter.parse(UtilidadFecha.getHoraActual()))>0)
						{
							fechaHoraNacimientoCorrectas=false;
							errores.add("horaEjecucion", new ActionMessage("errors.horaSuperiorA", "Nacimiento", "Actual"));
						}
					} 
					catch (ParseException e) 
					{
						fechaHoraNacimientoCorrectas=false;
						e.printStackTrace();
					}
				}
			}			
			
			for(int i=0;i<Integer.parseInt(this.secAdaptacionNeonatalInmediata.get("numRegistros")+"");i++)
			{
				if(!secAdaptacionNeonatalInmediata.containsKey("valor_"+i)||(secAdaptacionNeonatalInmediata.get("valor_"+i)+"").trim().equals(""))
				{
					errores.add("",new ActionMessage("errors.required",secAdaptacionNeonatalInmediata.get("descripcioncampo_"+i) +" (En la seccion Adaptacion Neonatal Inmediata)"));
				}
			}
			
			for(int i=0;i<Integer.parseInt(this.reanimacion.get("numRegistros")+"");i++)
			{
				if(!reanimacion.containsKey("valor_"+i)||(reanimacion.get("valor_"+i)+"").trim().equals(""))
				{
					errores.add("",new ActionMessage("errors.required",reanimacion.get("descripcioncampo_"+i)+" (En la seccion Adaptacion Neonatal Inmediata)"));
				}
			}
			
			for(int i=0;i<Integer.parseInt(this.secApgar.get("numRegistros")+"");i++)
			{
				if(!(secApgar.get("valor_"+i)+"").trim().equals(""))
				{
					if(!secApgar.containsKey("riesgo_"+i)||(secApgar.get("riesgo_"+i)+"").trim().equals(""))
					{
						errores.add("",new ActionMessage("errors.required","El Riesgo de "+ secApgar.get("descripcioncampo_"+i) +" (En la seccion Adaptacion Neonatal Inmediata)"));
					}
					if(Integer.parseInt(secApgar.get("valor_"+i)+"")>10)
					{
						errores.add("",new ActionMessage("errors.integerMenorIgualQue",secApgar.get("descripcioncampo_"+i) +" (En la seccion Adaptacion Neonatal Inmediata)","10"));						
					}
				}
				else
				{
					secApgar.put("riesgo_"+i,"");
				}
			}
			
			if(this.vitaminaK.trim().equals(""))
			{
				errores.add("",new ActionMessage("errors.required","Vitamina k" +" (En la seccion Adaptacion Neonatal Inmediata)"));
			}
			if(this.profilaxisOftalmico.trim().equals(""))
			{
				errores.add("",new ActionMessage("errors.required","Profilaxis Oftalmica" +" (En la seccion Adaptacion Neonatal Inmediata)"));
			}
			if(this.sensibilizado.trim().equals(""))
			{
				errores.add("",new ActionMessage("errors.required","Sesibilizado" +" (En la seccion Adaptacion Neonatal Inmediata)"));
			}
			if(this.falleceSalaParto.trim().equals(""))
			{
				errores.add("",new ActionMessage("errors.required","Fallece Sala Parto" +" (En la seccion Adaptacion Neonatal Inmediata)"));
			}
			else
			{
				if(this.falleceSalaParto.trim().equals(ConstantesBD.acronimoNo+"")&&this.conductaSeguir_ani.trim().equals(""))
				{
					errores.add("",new ActionMessage("errors.required","Conducta a seguir" +" (En la seccion Adaptacion Neonatal Inmediata)"));
				}
				if(falleceSalaParto.trim().equals(ConstantesBD.acronimoSi+""))
				{
					if(this.momentoMuerte.trim().equals(""))
					{
						errores.add("",new ActionMessage("errors.required","Momento Muerte" +" (En la seccion Adaptacion Neonatal Inmediata)"));
					}
				}
			}
			
///////////////////FIN VALIDACIONES DE LA SECCION ADAPTACION NEONATAL INMEDIATA/////////////////////			
			
///////////////////VALIDACIONES DE LA SECCION EXAMENES FISICOS/////////////////////
			if(this.sexo.trim().equals(""))
			{
				errores.add("",new ActionMessage("errors.required","El Sexo"+" (En la seccion Examenes Fisicos)"));
			}

			for(int i=0;i<Integer.parseInt(this.secExamenesFisicos.get("numRegistros")+"");i++)
			{
				if(Integer.parseInt(secExamenesFisicos.get("campo_"+i)+"")>=Integer.parseInt(ConstantesBDHistoriaClinica.codigoCampoExamenFisicoPeso)&&Integer.parseInt(secExamenesFisicos.get("campo_"+i)+"")<=Integer.parseInt(ConstantesBDHistoriaClinica.codigoCampoExamenFisicoPerimetroToraxico))
				{
					if((secExamenesFisicos.get("valor_"+i)+"").trim().equals(""))
					{
						errores.add("",new ActionMessage("errors.required",secExamenesFisicos.get("descripcioncampo_"+i)+" (En la seccion Examenes Fisicos)"));
					}
				}
				else if(Integer.parseInt(secExamenesFisicos.get("campo_"+i)+"")>=Integer.parseInt(ConstantesBDHistoriaClinica.codigoCampoExamenFisicoMalformaciones)&&Integer.parseInt(secExamenesFisicos.get("campo_"+i)+"")<=Integer.parseInt(ConstantesBDHistoriaClinica.codigoCampoExamenFisicoSialorrea)&&Integer.parseInt(secExamenesFisicos.get("campo_"+i)+"")!=Integer.parseInt(ConstantesBDHistoriaClinica.codigoCampoExamenFisicoMunionUmbilical))
				{
					if((secExamenesFisicos.get("valor_"+i)+"").trim().equals(""))
					{
						errores.add("",new ActionMessage("errors.required",secExamenesFisicos.get("descripcioncampo_"+i)+" (En la seccion Examenes Fisicos)"));
					}
					else if(((secExamenesFisicos.get("valor_"+i)+"").equals(ConstantesBD.acronimoSi) )&&(Integer.parseInt(secExamenesFisicos.get("campo_"+i)+"")==Integer.parseInt(ConstantesBDHistoriaClinica.codigoCampoExamenFisicoMalformaciones)||Integer.parseInt(secExamenesFisicos.get("campo_"+i)+"")==Integer.parseInt(ConstantesBDHistoriaClinica.codigoCampoExamenFisicoInfeccion)))
					{
						if((secExamenesFisicos.get("descripcionvalor_"+i)+"").trim().equals(""))
						{
							errores.add("",new ActionMessage("errors.required","Cuales de "+secExamenesFisicos.get("descripcioncampo_"+i)+" (En la seccion Examenes Fisicos)"));
						}	
					}
				}
				
			}
			
			if(this.pesoEdadGestacion.trim().equals(""))
			{
				errores.add("",new ActionMessage("errors.required","Peso Edad Gestacional"+" (En la seccion Examenes Fisicos)"));
			}

			if(this.edadGestacionExamen.trim().equals(""))
			{
				errores.add("",new ActionMessage("errors.required","Edad Gestacion Examen"+" (En la seccion Examenes Fisicos)"));
			}
			else
			{
				try
				{
					int edad=Integer.parseInt(this.edadGestacionExamen);
					if(edad<0 ||  edad > 42)
					{
						errores.add("",new ActionMessage("errors.range","Edad Gestacion Examen"+" (En la seccion Examenes Fisicos)","0","42"));
					}
				}
				catch(NumberFormatException e)
				{
					errores.add("",new ActionMessage("errors.integer","Edad Gestacion Examen"+" (En la seccion Examenes Fisicos)"));
				}
			}
			
			if(this.defectosCongenitos.trim().equals(""))
			{
				errores.add("",new ActionMessage("errors.required","Defectos Congenitos"+" (En la seccion Examenes Fisicos)"));
			}
			else if(this.defectosCongenitos.equalsIgnoreCase(ConstantesBD.acronimoNo)||this.defectosCongenitos.equalsIgnoreCase(""))
			{
				this.cieDiagDefCong="";
				this.diagDefCong="";
				this.nombreDiagDefCong="";
			}
			else
			{
				
				//new ini Asignar de la propiedad donde esta unificado el codigo y tipo cie del Diagnostico a las respectivas
				if(this.uniCodyCieDiagDefCon.trim().equals("")||this.uniCodyCieDiagDefCon.trim().equals("@@@@@@@@@@") )
				{
					errores.add("",new ActionMessage("errors.required","El diagnostico del defecto Congenito"+" (En la seccion Examenes Fisicos)"));
				}
				else
				{
					//String[] uniCodCieDiagDefCon = this.uniCodyCieDiagDefCon.split(ConstantesBD.separadorSplit);
						if (uniCodCieDiagDefCon.length >=2)
						{
							this.diagDefCong = uniCodCieDiagDefCon[0];
							this.cieDiagDefCong = uniCodCieDiagDefCon[1];
							this.nombreDiagDefCong = uniCodCieDiagDefCon[2];
						}
						
						//if (uniCodCieDiagDefCon.length ==0)
						//{
						//	errores.add("",new ActionMessage("errors.required","El diagnostico del defecto Congenito"+" (En la seccion Examenes Fisicos)"));
						//}
						
						
				}

			}
		
/////////////////FIN VALIDACIONES DE LA SECCION EXAMENES FISICOS/////////////////////			
			
			

///////////////////VALIDACIONES DE LA SECCION EGRESO/////////////////////
			if(this.observacionesEgreso.length()>255)
			{
				errores.add("",new ActionMessage("errors.maxlength","Las observaciones "+" (En la seccion Egreso)","255"));
			}
			if(this.codigoProfesionalAtendio.trim().equals(""))
			{
				errores.add("",new ActionMessage("errors.required","El profesional que Atendio"+" (En la seccion Egreso)"));
			}
			
			if(!UtilidadFecha.esFechaValidaSegunAp(this.getFechaEgreso()))
			{
				fechaHoraEgresoCorrectas=false;
				errores.add("",new ActionMessage("errors.required","La fecha de Egreso"));
			}
			else if((UtilidadFecha.conversionFormatoFechaABD(this.getFechaEgreso())).compareTo(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()))>0)
			{
				fechaHoraEgresoCorrectas=false;
				errores.add("", new ActionMessage("errors.fechaPosteriorIgualActual", "Egreso", "Actual"));
			}
			else if((UtilidadFecha.conversionFormatoFechaABD(this.getFechaEgreso())).compareTo(UtilidadFecha.conversionFormatoFechaABD(this.solicitudes.get("fechaadmision_"+this.itemSeleccionado)+""))<0)
			{
				fechaHoraEgresoCorrectas=false;
				errores.add("", new ActionMessage("errors.fechaAnteriorIgualActual", "Egreso",  "Admision ("+this.solicitudes.get("fechaadmision_"+this.itemSeleccionado)+")"));
			}
			val=UtilidadFecha.validacionHora(this.horaEgreso);
			if(this.horaEgreso.trim().equals(""))
			{
				fechaHoraEgresoCorrectas=false;
				errores.add("",new ActionMessage("errors.required","La Hora de Egreso"));
			}
			else if(!val.puedoSeguir)
			{
				fechaHoraEgresoCorrectas=false;
				errores.add("error en la hora",new ActionMessage("error.errorEnBlanco","(Hora Egreso) "+val.textoRespuesta+""));
			}
			else
			{
				final SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm");
				if(this.getFechaEgreso().equals(UtilidadFecha.getFechaActual()))
				{
					//valisdad q la hora no sea menor
					try 
					{
						if(timeFormatter.parse(this.getHoraEgreso()).compareTo(timeFormatter.parse(UtilidadFecha.getHoraActual()))>0)
						{
							fechaHoraEgresoCorrectas=false;
							errores.add("", new ActionMessage("errors.horaSuperiorA", "Egreso", "Actual"));
						}
					} 
					catch (ParseException e) 
					{
						fechaHoraEgresoCorrectas=false;
						e.printStackTrace();
					}
				}
			}
			
			if(fechaHoraNacimientoCorrectas&&fechaHoraEgresoCorrectas)
			{
				String fecha1=UtilidadFecha.conversionFormatoFechaABD(this.fechaNacimiento);
				String fecha2=UtilidadFecha.conversionFormatoFechaABD(this.fechaEgreso);
				int comparacion1=(fecha1).compareTo(fecha2);
				
				if(comparacion1>0)
				{
					errores.add("", new ActionMessage("errors.fechaAnteriorIgualActual", "Egreso ("+this.fechaEgreso+")",  "Nacimiento ("+this.fechaNacimiento+")"));
				}
				else if (comparacion1 == 0 && (this.horaNacimiento.compareTo(this.horaEgreso)>=0))
				{
					errores.add("fecha Invalida", new ActionMessage("errors.fechaHoraPosteriorOtraReferencia"," Nacimiento ("+this.fechaNacimiento+" "+ this.horaNacimiento+")", " Egreso  (" +this.fechaEgreso+" "+ this.horaEgreso+")"));
				}
			}
			if(this.condicionEgreso.trim().equals(""))
			{
				errores.add("",new ActionMessage("errors.required","Condicion Egreso"));
			}
			if(this.lactancia.trim().equals(""))
			{
				errores.add("",new ActionMessage("errors.required","Lactancia"+" (En la seccion Egreso)"));
			}
			if(this.pesoEgreso.trim().equals(""))
			{
				errores.add("",new ActionMessage("errors.required","Peso Egreso"+" (En la seccion Egreso)"));
			}
			
			if(this.vacunaPolio.trim().equals("")){
				errores.add("",new ActionMessage("errors.required","Vacuna Polio"+" (En la seccion Egreso)"));
			}
			if(this.vacunaBCG.trim().equals("")){
				errores.add("",new ActionMessage("errors.required","Vacuna BCG"+" (En la seccion Egreso)"));
			}
			if(this.vacunaHepatitisB.trim().equals("")){
				errores.add("",new ActionMessage("errors.required","Hepatitis B"+" (En la seccion Egreso)"));
			}
			if(this.sano.trim().equals(""))
			{
				errores.add("",new ActionMessage("errors.required","Sano/Enfermo"+" (En la seccion Egreso)"));
			}
			
			if(!diagEgreso.containsKey("acronimoPrincipal")||!diagEgreso.containsKey("tipoCiePrincipal"))
			{
				errores.add("",new ActionMessage("errors.required","El diagnostico Egreso"));	
			}
			//else if(!diagEgreso.containsKey("tipoDiagPrincipal"))             )
			else if ((diagEgreso.get("tipoDiagPrincipal")=="") || (!diagEgreso.containsKey("tipoDiagPrincipal")))
			{
				
				errores.add("",new ActionMessage("errors.required","El tipo de Diagnostico de Egreso"));
			}
			

			if(this.conductaSeguir.trim().equals(""))
			{
				errores.add("",new ActionMessage("errors.required","Conducta a seguir de Egreso"));
			}
			
			
			if(this.vivo.trim().equals(""))
			{
				errores.add("",new ActionMessage("errors.required","Vivo"));
			}	
			else if(this.vivo.equalsIgnoreCase("false"))
			{
				//Siempre se va a exigir el diagnostico de Nacimiento
				//this.diagRN="";
				//this.cieDiagRN="";
				//this.nombreDiagRN="";
				//this.uniCodyCieDiagRN="";
			
				if(!UtilidadFecha.esFechaValidaSegunAp(this.getFechaMuerte()))
				{
					errores.add("",new ActionMessage("errors.required","La fecha de Muerte"+" (En la seccion Egreso)"));
				}
				else if((UtilidadFecha.conversionFormatoFechaABD(this.getFechaMuerte())).compareTo(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()))>0)
				{
					errores.add("", new ActionMessage("errors.fechaPosteriorIgualActual", "Muerte", "Actual"));
				}
				else if((UtilidadFecha.conversionFormatoFechaABD(this.getFechaMuerte())).compareTo(UtilidadFecha.conversionFormatoFechaABD(this.getFechaNacimiento()))<0)
				{
					errores.add("", new ActionMessage("errors.fechaAnteriorIgualActual", "Muerte", "Nacimiento"));
				}
				else
				{
					if(UtilidadValidacion.tieneEgreso(this.solicitudes.get("cuenta_"+this.itemSeleccionado)+""))
					{
						if((UtilidadFecha.conversionFormatoFechaABD(this.getFechaMuerte())).compareTo(UtilidadFecha.conversionFormatoFechaABD(Utilidades.obtenerFechaEgreso(this.solicitudes.get("cuenta_"+this.itemSeleccionado)+"")))>0)
						{
							errores.add("", new ActionMessage("errors.fechaPosteriorIgualActual", "Muerte", "Egreso"));
						}
					}
				}
			
				
				val=UtilidadFecha.validacionHora(this.horaMuerte);
				if(this.horaMuerte.trim().equals(""))
				{
					errores.add("",new ActionMessage("errors.required","La Hora de Muerte"+" (En la seccion Egreso)"));
				}
				else if(!val.puedoSeguir)
				{
					errores.add("error en la hora",new ActionMessage("error.errorEnBlanco","(Hora Muerte) "+val.textoRespuesta+""));
				}
				else
				{
					final SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm");
					if(this.getFechaMuerte().equals(UtilidadFecha.getFechaActual()))
					{
						//valisdad q la hora no sea mayor
						try 
						{
							if(timeFormatter.parse(this.getHoraMuerte()).compareTo(timeFormatter.parse(UtilidadFecha.getHoraActual()))>0)
							{
								errores.add("horaEjecucion", new ActionMessage("errors.horaSuperiorA", "Muerte", "Actual"));
							}
						} 
						catch (ParseException e) 
						{
							e.printStackTrace();
						}
					}
				}
				
				if(this.falleceSalaParto.trim().equals(""))
				{
					errores.add("",new ActionMessage("errors.required","Fallece Sala Parto"+" (En la seccion Egreso)"));
				}
				
				//Asignar de la propiedad donde esta unificado el codigo y tipo cie del Diagnostico a las respectivas
				
				
				if( this.uniCodyCieDiagMte.trim().equals("")||this.uniCodyCieDiagMte.trim().equals("@@@@@@@@@@"))
				{
					errores.add("",new ActionMessage("errors.required","El diagnostico de Muerte"+" (En la seccion Egreso)"));
				}
				else
				{
					//String[] uniCodCieDiagMte = this.uniCodyCieDiagMte.split(ConstantesBD.separadorSplit);
					if (uniCodCieDiagMte.length >=2)
					{
						this.diagMuerte = uniCodCieDiagMte[0];
						this.cieDiagMuerte = uniCodCieDiagMte[1];
						this.nombreDiagMuerte = uniCodCieDiagMte[2];
					}
					
					
				}
				
				///
				/*
				LA VALIDACION DEL MOMENTOMUERTE NO SE DEBE VALIDAR EN EL DIAGNOSTICO.
				if(this.momentoMuerte.trim().equals(""))
				{
					errores.add("",new ActionMessage("errors.required","Momento Muerte"+" (En la seccion Egreso)"));
				}
				*/
			}
			else if(this.vivo.equalsIgnoreCase("true"))
			{
				this.uniCodyCieDiagMte="";
				this.cieDiagMuerte="";
				this.nombreDiagMuerte="";
				//
			}	//Siempre se va a exigir el diagnostico de Nacimiento
			//////Cod Diag Rec Nac
			
			
			
			
			
			///tamizacion
			
			for(int i=0;i<Integer.parseInt(this.tamizacionNeonatal.get("numRegistros")+"");i++)
			{
				if(!tamizacionNeonatal.containsKey("valor_"+i)||(tamizacionNeonatal.get("valor_"+i)+"").trim().equals(""))
				{
					errores.add("",new ActionMessage("errors.required",tamizacionNeonatal.get("descripcioncampo_"+i)+" (En la seccion Egreso)"));
				}
			}
			
			if(this.hemoclasificacion.trim().equals(""))
			{
				errores.add("",new ActionMessage("errors.required","Hemoclasificación"));
			}
			
			for(int i=0;i<Integer.parseInt(this.secSano.get("numRegistros")+"");i++)
			{
				if(!secSano.containsKey("valor_"+i)||(secSano.get("valor_"+i)+"").trim().equals(""))
				{
					errores.add("",new ActionMessage("errors.required",secSano.get("descripcioncampo_"+i)+" (En la seccion Egreso)"));
				}
			}
///////////////////FIN VALIDACIONES DE LA SECCION EGRESO/////////////////////
			
		}
		return errores;
	}

	public String getEstado()
	{
		return estado;
	}

	public void setEstado(String estado)
	{
		this.estado = estado;
	}


	public HashMap getSolicitudes()
	{
		return solicitudes;
	}


	public void setSolicitudes(HashMap solicitudes)
	{
		this.solicitudes = solicitudes;
	}
	public Object getSolicitudes(String key)
	{
		return solicitudes.get(key);
	}


	public void setSolicitudes(String key,Object value)
	{
		this.solicitudes.put(key, value);
	}


	public String getFechaFinal()
	{
		return fechaFinal;
	}


	public void setFechaFinal(String fechaFinal)
	{
		this.fechaFinal = fechaFinal;
	}


	public String getFechaInicial()
	{
		return fechaInicial;
	}


	public void setFechaInicial(String fechaInicial)
	{
		this.fechaInicial = fechaInicial;
	}


	public int getMaxPageItems()
	{
		return maxPageItems;
	}


	public void setMaxPageItems(int maxPageItems)
	{
		this.maxPageItems = maxPageItems;
	}



	public HashMap getHijos()
	{
		return hijos;
	}


	public void setHijos(HashMap hijos)
	{
		this.hijos = hijos;
	}



	public Object getHijos( String key)
	{
		return hijos.get(key);
	}


	public void setHijos(String key,Object value)
	{
		this.hijos.put(key, value);
	}

	public String getItemSeleccionado()
	{
		return itemSeleccionado;
	}


	public void setItemSeleccionado(String itemSeleccionado)
	{
		this.itemSeleccionado = itemSeleccionado;
	}


	public String getHijoSeleccionado()
	{
		return hijoSeleccionado;
	}


	public void setHijoSeleccionado(String hijoSeleccionado)
	{
		this.hijoSeleccionado = hijoSeleccionado;
	}


	public boolean isModificacion()
	{
		return modificacion;
	}


	public void setModificacion(boolean modificacion)
	{
		this.modificacion = modificacion;
	}
	
	public String getFechaNacimiento()
	{
		return fechaNacimiento;
	}

	public void setFechaNacimiento(String fechaNacimiento)
	{
		this.fechaNacimiento = fechaNacimiento;
	}

	public String getHoraNacimiento()
	{
		return horaNacimiento;
	}

	public void setHoraNacimiento(String horaNacimiento)
	{
		this.horaNacimiento = horaNacimiento;
	}

	public String getSexo()
	{
		return sexo;
	}

	public void setSexo(String sexo)
	{
		this.sexo = sexo;
	}

	public boolean isSecInfoGeneral()
	{
		return secInfoGeneral;
	}


	public void setSecInfoGeneral(boolean secInfoGeneral)
	{
		this.secInfoGeneral = secInfoGeneral;
	}


	public String getCieDiagDefCong()
	{
		return cieDiagDefCong;
	}


	public void setCieDiagDefCong(String cieDiagDefCong)
	{
		this.cieDiagDefCong = cieDiagDefCong;
	}


	public String getCieDiagMuerte()
	{
		return cieDiagMuerte;
	}


	public void setCieDiagMuerte(String cieDiagMuerte)
	{
		this.cieDiagMuerte = cieDiagMuerte;
	}


	public String getCieDiagRN()
	{
		return cieDiagRN;
	}


	public void setCieDiagRN(String cieDiagRN)
	{
		this.cieDiagRN = cieDiagRN;
	}


	public String getCodigoProfesionalAtendio()
	{
		return codigoProfesionalAtendio;
	}


	public void setCodigoProfesionalAtendio(String codigoProfesionalAtendio)
	{
		this.codigoProfesionalAtendio = codigoProfesionalAtendio;
	}


	public String getCondicionEgreso()
	{
		return condicionEgreso;
	}


	public void setCondicionEgreso(String condicionEgreso)
	{
		this.condicionEgreso = condicionEgreso;
	}


	public String getConductaSeguir()
	{
		return conductaSeguir;
	}


	public void setConductaSeguir(String conductaSeguir)
	{
		this.conductaSeguir = conductaSeguir;
	}


	public String getDefectosCongenitos()
	{
		return defectosCongenitos;
	}


	public void setDefectosCongenitos(String defectosCongenitos)
	{
		this.defectosCongenitos = defectosCongenitos;
	}


	public String getDiagDefCong()
	{
		return diagDefCong;
	}


	public void setDiagDefCong(String diagDefCong)
	{
		this.diagDefCong = diagDefCong;
	}


	public String getDiagMuerte()
	{
		return diagMuerte;
	}


	public void setDiagMuerte(String diagMuerte)
	{
		this.diagMuerte = diagMuerte;
	}


	public String getDiagRN()
	{
		return diagRN;
	}


	public void setDiagRN(String diagRN)
	{
		this.diagRN = diagRN;
	}

	public String getFalleceSalaParto()
	{
		return falleceSalaParto;
	}


	public void setFalleceSalaParto(String falleceSalaParto)
	{
		this.falleceSalaParto = falleceSalaParto;
	}


	public String getFechaEgreso()
	{
		return fechaEgreso;
	}


	public void setFechaEgreso(String fechaEgreso)
	{
		this.fechaEgreso = fechaEgreso;
	}


	public String getFechaMuerte()
	{
		return fechaMuerte;
	}


	public void setFechaMuerte(String fechaMuerte)
	{
		this.fechaMuerte = fechaMuerte;
	}


	public String getHemoclasificacion()
	{
		return hemoclasificacion;
	}


	public void setHemoclasificacion(String hemoclasificacion)
	{
		this.hemoclasificacion = hemoclasificacion;
	}


	public String getHoraEgreso()
	{
		return horaEgreso;
	}


	public void setHoraEgreso(String horaEgreso)
	{
		this.horaEgreso = horaEgreso;
	}


	public String getHoraMuerte()
	{
		return horaMuerte;
	}


	public void setHoraMuerte(String horaMuerte)
	{
		this.horaMuerte = horaMuerte;
	}


	public String getLactancia()
	{
		return lactancia;
	}


	public void setLactancia(String lactancia)
	{
		this.lactancia = lactancia;
	}


	public String getMomentoMuerte()
	{
		return momentoMuerte;
	}


	public void setMomentoMuerte(String momentoMuerte)
	{
		this.momentoMuerte = momentoMuerte;
	}


	public String getNombreDiagDefCong()
	{
		return nombreDiagDefCong;
	}


	public void setNombreDiagDefCong(String nombreDiagDefCong)
	{
		this.nombreDiagDefCong = nombreDiagDefCong;
	}


	public String getNombreDiagMuerte()
	{
		return nombreDiagMuerte;
	}


	public void setNombreDiagMuerte(String nombreDiagMuerte)
	{
		this.nombreDiagMuerte = nombreDiagMuerte;
	}


	public String getNombreDiagRN()
	{
		return nombreDiagRN;
	}


	public void setNombreDiagRN(String nombreDiagRN)
	{
		this.nombreDiagRN = nombreDiagRN;
	}


	public String getPesoEdadGestacion()
	{
		return pesoEdadGestacion;
	}


	public void setPesoEdadGestacion(String pesoEdadGestacion)
	{
		this.pesoEdadGestacion = pesoEdadGestacion;
	}


	public String getPesoEgreso()
	{
		return pesoEgreso;
	}


	public void setPesoEgreso(String pesoEgreso)
	{
		this.pesoEgreso = pesoEgreso;
	}


	public String getProfilaxisOftalmico()
	{
		return profilaxisOftalmico;
	}


	public void setProfilaxisOftalmico(String profilaxisOftalmico)
	{
		this.profilaxisOftalmico = profilaxisOftalmico;
	}


	public String getSano()
	{
		return sano;
	}


	public void setSano(String sano)
	{
		this.sano = sano;
	}




	public String getVacunaBCG()
	{
		return vacunaBCG;
	}


	public void setVacunaBCG(String vacunaBCG)
	{
		this.vacunaBCG = vacunaBCG;
	}


	public String getVacunaHepatitisB()
	{
		return vacunaHepatitisB;
	}


	public void setVacunaHepatitisB(String vacunaHepatitisB)
	{
		this.vacunaHepatitisB = vacunaHepatitisB;
	}


	public String getVacunaPolio()
	{
		return vacunaPolio;
	}


	public void setVacunaPolio(String vacunaPolio)
	{
		this.vacunaPolio = vacunaPolio;
	}


	public String getVitaminaK()
	{
		return vitaminaK;
	}


	public void setVitaminaK(String vitaminaK)
	{
		this.vitaminaK = vitaminaK;
	}


	public String getVivo()
	{
		return vivo;
	}


	public void setVivo(String vivo)
	{
		this.vivo = vivo;
	}


	public String getNuip()
	{
		return nuip;
	}


	public void setNuip(String nuip)
	{
		this.nuip = nuip;
	}


	public String getSensibilizado()
	{
		return sensibilizado;
	}


	public void setSensibilizado(String sensibilizado)
	{
		this.sensibilizado = sensibilizado;
	}

	public boolean isSecEgreso()
	{
		return secEgreso;
	}


	public void setSecEgreso(boolean secEgreso)
	{
		this.secEgreso = secEgreso;
	}


	public String getFechaProceso()
	{
		return fechaProceso;
	}


	public void setFechaProceso(String fechaProceso)
	{
		this.fechaProceso = fechaProceso;
	}


	public String getHoraProceso()
	{
		return horaProceso;
	}


	public void setHoraProceso(String horaProceso)
	{
		this.horaProceso = horaProceso;
	}


	public String getUsuarioProceso()
	{
		return usuarioProceso;
	}


	public void setUsuarioProceso(String usuarioProceso)
	{
		this.usuarioProceso = usuarioProceso;
	}


	public String getCodigo()
	{
		return codigo;
	}


	public void setCodigo(String codigo)
	{
		this.codigo = codigo;
	}


	public HashMap getDiagEgreso()
	{
		return diagEgreso;
	}


	public void setDiagEgreso(HashMap diagEgreso)
	{
		this.diagEgreso = diagEgreso;
	}
	

	public Object getDiagEgreso(String key)
	{
		return diagEgreso.get(key);
	}


	public void setDiagEgreso(String key,Object value)
	{
		this.diagEgreso.put(key, value);
	}


	public ArrayList getProfesionalesInstitucion()
	{
		return profesionalesInstitucion;
	}


	public void setProfesionalesInstitucion(ArrayList profesionalesInstitucion)
	{
		this.profesionalesInstitucion = profesionalesInstitucion;
	}


	public String getCodigoCirugia()
	{
		return codigoCirugia;
	}


	public void setCodigoCirugia(String codigoCirugia)
	{
		this.codigoCirugia = codigoCirugia;
	}


	public HashMap getReanimacion()
	{
		return reanimacion;
	}


	public void setReanimacion(HashMap reanimacion)
	{
		this.reanimacion = reanimacion;
	}
	
	public Object getReanimacion(String key)
	{
		return reanimacion.get(key);
	}


	public void setReanimacion(String key,Object value)
	{
		this.reanimacion.put(key, value);
	}


	public HashMap getTamizacionNeonatal()
	{
		return tamizacionNeonatal;
	}


	public void setTamizacionNeonatal(HashMap tamizacionNeonatal)
	{
		this.tamizacionNeonatal = tamizacionNeonatal;
	}
	public Object getTamizacionNeonatal(String key)
	{
		return tamizacionNeonatal.get(key);
	}


	public void setTamizacionNeonatal(String key,Object value)
	{
		this.tamizacionNeonatal.put(key, value);
	}
	
	public HashMap getSecAdaptacionNeonatalInmediata()
	{
		return this.secAdaptacionNeonatalInmediata;
	}
	
	public void setSecAdaptacionNeonatalInmediata(HashMap secAdaptacionNeonatalInmediata)
	{
		this.secAdaptacionNeonatalInmediata = secAdaptacionNeonatalInmediata;
	}
	
	public Object getSecAdaptacionNeonatalInmediata(String key)
	{
		return this.secAdaptacionNeonatalInmediata.get(key);
	}
	
	public void setSecAdaptacionNeonatalInmediata(String key,Object value)
	{
		this.secAdaptacionNeonatalInmediata.put(key, value);
	}	


	/**
	 * @return the ocultarEncabezado
	 */
	public boolean isOcultarEncabezado() {
		return ocultarEncabezado;
	}


	/**
	 * @param ocultarEncabezado the ocultarEncabezado to set
	 */
	public void setOcultarEncabezado(boolean ocultarEncabezado) {
		this.ocultarEncabezado = ocultarEncabezado;
	}


	public boolean isSecANI()
	{
		return secANI;
	}


	public void setSecANI(boolean secANI)
	{
		this.secANI = secANI;
	}


	public HashMap getSecExamenesFisicos()
	{
		return secExamenesFisicos;
	}


	public void setSecExamenesFisicos(HashMap secExamenesFisicos)
	{
		this.secExamenesFisicos = secExamenesFisicos;
	}


	public boolean isSecEF()
	{
		return secEF;
	}


	public void setSecEF(boolean secEF)
	{
		this.secEF = secEF;
	}


	public HashMap getSecDiagnosticoRecienNacido()
	{
		return secDiagnosticoRecienNacido;
	}


	public void setSecDiagnosticoRecienNacido(HashMap secDiagnosticoRecienNacido)
	{
		this.secDiagnosticoRecienNacido = secDiagnosticoRecienNacido;
	}

	public Object getSecDiagnosticoRecienNacido(String key)
	{
		return secDiagnosticoRecienNacido.get(key);
	}


	public void setSecDiagnosticoRecienNacido(String key,Object value)
	{
		this.secDiagnosticoRecienNacido.put(key, value);
	}

	public boolean isSecRN()
	{
		return secRN;
	}


	public void setSecRN(boolean secRN)
	{
		this.secRN = secRN;
	}


	public boolean isSecAP()
	{
		return secAP;
	}


	public void setSecAP(boolean secAP)
	{
		this.secAP = secAP;
	}


	public HashMap getSecApgar()
	{
		return secApgar;
	}


	public void setSecApgar(HashMap secApgar)
	{
		this.secApgar = secApgar;
	}
	
	public Object getSecApgar(String key)
	{
		return secApgar.get(key);
	}


	public void setSecApgar(String key,Object value)
	{
		this.secApgar.put(key, value);
	}


	public boolean isSecSA()
	{
		return secSA;
	}


	public void setSecSA(boolean secSA)
	{
		this.secSA = secSA;
	}


	public HashMap getSecSano()
	{
		return secSano;
	}


	public void setSecSano(HashMap secSano)
	{
		this.secSano = secSano;
	}

	public Object getSecSano(String key)
	{
		return secSano.get(key);
	}


	public void setSecSano(String key,Object value)
	{
		this.secSano.put(key, value);
	}


	public boolean isInsercionModificacionExitosa()
	{
		return insercionModificacionExitosa;
	}


	public void setInsercionModificacionExitosa(boolean insercionModificacionExitosa)
	{
		this.insercionModificacionExitosa = insercionModificacionExitosa;
	}


	public String getUniCodyCieDiagRN() {
		return uniCodyCieDiagRN;
	}


	public void setUniCodyCieDiagRN(String uniCodyCieDiagRN) {
		this.uniCodyCieDiagRN = uniCodyCieDiagRN;
	}


	public int getNumeroSolicitud() {
		return numeroSolicitud;
	}


	public void setNumeroSolicitud(int numeroSolicitud) {
		this.numeroSolicitud = numeroSolicitud;
	}


	public String getFicha() {
		return ficha;
	}


	public void setFicha(String ficha) {
		this.ficha = ficha;
	}


	public String getUniCodyCieDiagMte() {
		return uniCodyCieDiagMte;
	}


	public void setUniCodyCieDiagMte(String uniCodyCieDiagMte) {
		this.uniCodyCieDiagMte = uniCodyCieDiagMte;
	}


	public String getFichaMte() {
		return fichaMte;
	}


	public void setFichaMte(String fichaMte) {
		this.fichaMte = fichaMte;
	}


	public String getUniCodyCieDiagDefCon() {
		return uniCodyCieDiagDefCon;
	}


	public void setUniCodyCieDiagDefCon(String uniCodyCieDiagDefCon) {
		this.uniCodyCieDiagDefCon = uniCodyCieDiagDefCon;
	}


	public String getConductaSeguir_ani()
	{
		return conductaSeguir_ani;
	}


	public void setConductaSeguir_ani(String conductaSeguir_ani)
	{
		this.conductaSeguir_ani = conductaSeguir_ani;
	}


	public String getEdadGestacionExamen()
	{
		if(this.edadGestacionExamen.trim().equals("-1"))
			return "";
		return edadGestacionExamen;
	}


	public void setEdadGestacionExamen(String edadGestacionExamen)
	{
		this.edadGestacionExamen = edadGestacionExamen.trim().equals("")?"-1":edadGestacionExamen.trim();
	}


	public String getObservacionesEgreso()
	{
		return observacionesEgreso;
	}


	public void setObservacionesEgreso(String observacionesEgreso)
	{
		this.observacionesEgreso = observacionesEgreso;
	}


	public String getNumeroCertificadoNacimiento()
	{
		return numeroCertificadoNacimiento;
	}


	public void setNumeroCertificadoNacimiento(String numeroCertificadoNacimiento)
	{
		this.numeroCertificadoNacimiento = numeroCertificadoNacimiento;
	}


	public String getFinalizada()
	{
		return finalizada;
	}


	public void setFinalizada(String finalizada)
	{
		this.finalizada = finalizada;
	}


	public String getCodigoEnfermedad()
	{
		return codigoEnfermedad;
	}


	public void setCodigoEnfermedad(String codigoEnfermedad)
	{
		this.codigoEnfermedad = codigoEnfermedad;
	}


}
