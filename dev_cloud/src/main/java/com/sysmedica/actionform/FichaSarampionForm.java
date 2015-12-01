/*
 * Creado en 14-jul-2005
 * 
 * Autor Santiago Salamanca
 * SysMédica
 */
package com.sysmedica.actionform;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import util.UtilidadFecha;
import util.ValoresPorDefecto;

/**
 * @author santiago
 *
 */
public class FichaSarampionForm extends ValidatorForm {
	
	private transient Logger logger=Logger.getLogger(FichaSarampionForm.class);
	
	private boolean activa;
	private String urlFichasAnteriores;
	
	private int codigoInstitucion;
	
	private boolean hayServicios;
    private boolean hayLaboratorios;
    
	private String estado;
	private boolean notificar;

	private int codigoFichaSarampion;
	private int codigoPaciente;
    private String codigoDiagnostico;
    private int codigoNotificacion;
    private int numeroSolicitud;
    private int codigoEnfNotificable;
    
    private int codigoConvenio;
    
    private String valorDivDx;
    
    private boolean fichamodulo;
    private String esPrimeraVez; 
    private boolean vieneDeFichasAnteriores;
    
    //*************************************
    // DATOS CARA A :
        
    // Informacion general
    private String sire;
    private String municipioNotifica;
    private String departamentoNotifica;
    private String lugarNotifica;
    private int institucionAtendio;
    private String fechaNotificacion;
    private int semanaEpidemiologica;
    private int anyoSemanaEpi;
    
    // Identificacion del paciente
    private String primerNombre;
    private String segundoNombre;
    private String primerApellido;
    private String segundoApellido;
    private String departamentoNacimiento;
    private String ciudadNacimiento;
    private String departamentoResidencia;
    private String ciudadResidencia;
    private String direccionPaciente;
    private String telefonoPaciente;
    private String fechaNacimiento;
    private String edad;
    private String genero;
    private String estadoCivil;
    private String documento;
    private String barrioResidencia;
    private String zonaDomicilio;
    private String ocupacion;
    private String aseguradora;
    private String regimenSalud;
    private String etnia;
    private int desplazado;
    private String tipoId;
    private String paisExpedicion;
    private String paisNacimiento;
    private String paisResidencia;
        
    
    // Notificacion
    private String fechaConsultaGeneral;
    private String codigoMunProcedencia;
    private String codigoDepProcedencia;
    private String lugarProcedencia;
    private String fechaInicioSint;
    private int tipoCaso;
    private boolean hospitalizado;
    private String fechaHospitalizacion;
    private boolean estaVivo;
    private String fechaDefuncion;
    private String nombreProfesional;
    
    
    //**************************************
    // DATOS CARA B :    
	
    private String loginUsuario;
    private int estadoFicha;
    private String nombrePadre;
    private String ocupacionPadre;
    private String direccionTrabajoPadre;
    private String fechaVisita1;
    private int fuenteNotificacion;
    private int vacunaSarampion;
    private int numeroDosisSarampion;
    private String fechaUltimaDosisSarampion;
    private int fuenteDatosSarampion;
    private int vacunaRubeola;
    private int numeroDosisRubeola;
    private String fechaUltimaDosisRubeola;
    private int fuenteDatosRubeola;
    
    private String fechaVisitaDomiciliaria;
    private int fiebre;
    private String fechaInicioFiebre;
    private int tipoErupcion;
    private String fechaInicioErupcion;
    private int duracionErupcion;
    private int tos;
    private int coriza;
    private int conjuntivitis;
    private int adenopatia;
    private int artralgia;
    private int embarazada;
    private int numeroSemanas;
    private String lugarParto;
    private String municipioParto;
    private String departamentoParto;
    
    private int huboContacto;
    private int huboCasoConfirmado;
    private int huboViaje;
    private String lugarViaje;
    private String municipioViaje;
    private String departamentoViaje;
    private int huboContactoEmbarazada;
    private int diagnosticoFinal;
    private String nombreInvestigador;
    private String telefonoInvestigador;
    private String pais;
    private int areaProcedencia;
    private String grupoPoblacional;
    
    private HashMap datosLaboratorio;
    /*
    private String primerNombreUsuario;
    private String segundoNombreUsuario;
    private String primerApellidoUsuario;
    private String segundoApellidoUsuario;
    private String identificacionUsuario;
    */
    
    // Variables para las valoraciones
    private String valorDiagnostico;
    private String idDiagnostico;
    private String propiedadDiagnostico;
    private String idDiv;
    private String idCheckBox;
    private String idHiddenCheckBox;
    private String propiedadHiddenCheckBox;
    private int tipoDiagnostico;
    private int numero;
    private String idNumero;
    private String diagnosticosSeleccionados;
    private String idDiagSeleccionados;
    private String idValorFicha;
    private boolean epidemiologia;
    
    
    public void reset() {
    	
    	sire="";
        nombrePadre = "";
        ocupacionPadre = "";
        direccionTrabajoPadre = "";
        fechaVisita1 = "";
        fuenteNotificacion = 0;
        vacunaSarampion = 0;
        numeroDosisSarampion = 0;
        fechaUltimaDosisSarampion = "";
        fuenteDatosSarampion = 0;
        vacunaRubeola = 0;
        numeroDosisRubeola = 0;
        fechaUltimaDosisRubeola = "";
        fuenteDatosRubeola = 0;
        
        fechaVisitaDomiciliaria = "";
        fiebre = 0;
        fechaInicioFiebre = "";
        tipoErupcion = 0;
        fechaInicioErupcion = "";
        duracionErupcion = 0;
        tos = 0;
        coriza = 0;
        conjuntivitis = 0;
        adenopatia = 0;
        artralgia = 0;
        embarazada = 0;
        numeroSemanas = 0;
        lugarParto = "";
        
        huboContacto = 0;
        huboCasoConfirmado = 0;
        huboViaje = 0;
        lugarViaje = "";
        huboContactoEmbarazada = 0;
        diagnosticoFinal = 0;
        
        datosLaboratorio = new HashMap();
        estaVivo = false;
        
        nombreInvestigador = "";
        telefonoInvestigador = "";
        
        String ciudad = ValoresPorDefecto.getCiudadNacimiento(codigoInstitucion);
        String lugar = ciudad.split("-")[1]+"-"+ciudad.split("-")[0];
        
        lugarNotifica = lugar;
        lugarProcedencia = lugar;
        
        fechaInicioSint = "";
        tipoCaso = 0;
        
        fechaConsultaGeneral = "";
        codigoMunProcedencia = "";
        codigoDepProcedencia = "";
        lugarProcedencia = "";
        fechaInicioSint = "";
        tipoCaso = 0;
        hospitalizado = false;
        fechaHospitalizacion = "";
        estaVivo = false;
        fechaDefuncion = "";
        nombreProfesional = "";
        
        pais = "COLOMBIA";
        areaProcedencia = 0;
        grupoPoblacional = "";
    }
    
    
    /**
     * Metodo para validar los atributos provenientes del formulario
     */
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        
        ActionErrors errores = new ActionErrors();
        
        if (estado.equals("empezar")) {
            
			reset();
		}
        
        if (estado.equals("actualizar")) {
        
        	String fechaPrimeraVisitaTransformada=UtilidadFecha.conversionFormatoFechaABD(fechaVisita1);
        	String fechaVisitaDomiciliariaTransformada = UtilidadFecha.conversionFormatoFechaABD(fechaVisitaDomiciliaria);
        	String fechaInicioFiebreTransformada = UtilidadFecha.conversionFormatoFechaABD(fechaInicioFiebre);
        	String fechaInicioErupcionTransformada = UtilidadFecha.conversionFormatoFechaABD(fechaInicioErupcion);
        	String fechaConsultaTransformada=UtilidadFecha.conversionFormatoFechaABD(fechaConsultaGeneral);
            String fechaInicioSintomasTransformada=UtilidadFecha.conversionFormatoFechaABD(fechaInicioSint);
            String fechaHospitalizacionTransformada=UtilidadFecha.conversionFormatoFechaABD(fechaHospitalizacion);
            String fechaDefuncionTransformada=UtilidadFecha.conversionFormatoFechaABD(fechaDefuncion);
            String fechaUltimaDosisSarampionTransformada = UtilidadFecha.conversionFormatoFechaABD(fechaUltimaDosisSarampion);
            String fechaUltimaDosisRubeolaTransformada = UtilidadFecha.conversionFormatoFechaABD(fechaUltimaDosisRubeola);
        	
            String fechaActual=UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual());
            /*
            if (fechaInicioSint.trim().length()>0) {
            	
            	if (!UtilidadFecha.validarFecha(fechaInicioSint)) {
	                
	                errores.add("Campo Fecha de Inicio de Sintomas no valido", new ActionMessage("errors.formatoFechaInvalido","de Inicio de Sintomas"));
	            }
	            else if (fechaInicioSintomasTransformada.compareTo(fechaActual)>0) {
					
					errores.add("Campo Fecha de Inicio de Sintomas no valido", new ActionMessage("errors.fechaPosteriorIgualActual","de Inicio de Sintomas", "actual"));
				}
            }
            */
            
            if (fechaInicioSint.trim().length()>0) {
	            if (!UtilidadFecha.validarFecha(fechaInicioSint)) {
	                
	                errores.add("Campo Fecha de Inicio de Sintomas no valido", new ActionMessage("errors.formatoFechaInvalido","de Inicio de Sintomas"));
	            }
	            else if (fechaInicioSintomasTransformada.compareTo(fechaActual)>0) {
					
					errores.add("Campo Fecha de Inicio de Sintomas no valido", new ActionMessage("errors.fechaPosteriorIgualActual","de Inicio de Sintomas", "actual"));
				}
            }
            else {
            	errores.add("El campo Fecha de Inicio de Sintomas es Requerido", new ActionMessage("errors.required","El campo fecha de inicio de Sintomas"));
            }

            try {
	            if (fechaHospitalizacion.trim().length()>0) {
	            	
	            	if (!UtilidadFecha.validarFecha(fechaHospitalizacion)) {
		                
		                errores.add("Campo Fecha de Hospitalizacion no valido", new ActionMessage("errors.formatoFechaInvalido","de Hospitalizacion"));
		            }
		            else if (fechaHospitalizacionTransformada.compareTo(fechaActual)>0) {
						
						errores.add("Campo Fecha de Hospitalizacion no valido", new ActionMessage("errors.fechaPosteriorIgualActual","de Hospitalizacion", "actual"));
					}
	            }
	            
	            if (fechaDefuncion.trim().length()>0) {
	            	
	            	if (!UtilidadFecha.validarFecha(fechaDefuncion)) {
		                
		                errores.add("Campo Fecha de Defuncion no valido", new ActionMessage("errors.formatoFechaInvalido","de Defuncion"));
		            }
		            else if (fechaDefuncionTransformada.compareTo(fechaActual)>0) {
						
						errores.add("Campo Fecha de Defuncion no valido", new ActionMessage("errors.fechaPosteriorIgualActual","de Defuncion", "actual"));
					}
	            }
	            
	            if (fechaVisita1.trim().length()>0) {
	            	
		            if (!UtilidadFecha.validarFecha(fechaVisita1)) {
		                
		                errores.add("Campo Fecha de Primera Visita no valido", new ActionMessage("errors.formatoFechaInvalido","de Primera Visita"));
		            }
		            else if (fechaPrimeraVisitaTransformada.compareTo(fechaActual)>0) {
						
						errores.add("Campo Fecha de Primera Visita no valido", new ActionMessage("errors.fechaPosteriorIgualActual","de Primera Visita", "actual"));
					}
	            }
	            
	            if (vacunaSarampion==2) {
	            	
	            	if (!UtilidadFecha.validarFecha(fechaUltimaDosisSarampion)) {
		                
		                errores.add("Campo Fecha de Ultima Dosis de Vac. contra Sarampion no valido", new ActionMessage("errors.formatoFechaInvalido","de Ultima Dosis de Vac. contra Sarampion"));
		            }
		            else if (fechaUltimaDosisSarampionTransformada.compareTo(fechaActual)>0) {
						
						errores.add("Campo Fecha de Ultima Dosis de Vac. contra Sarampion no valido", new ActionMessage("errors.fechaPosteriorIgualActual","de Ultima Dosis de Vac. contra Sarampion", "actual"));
					}
	            }
	            
	            if (vacunaRubeola==2) {
	            	
	            	if (!UtilidadFecha.validarFecha(fechaUltimaDosisRubeola)) {
		                
		                errores.add("Campo Fecha de Ultima Dosis de Vac. contra Rubeola no valido", new ActionMessage("errors.formatoFechaInvalido","de Ultima Dosis de Vac. contra Rubeola"));
		            }
		            else if (fechaUltimaDosisRubeolaTransformada.compareTo(fechaActual)>0) {
						
						errores.add("Campo Fecha de Ultima Dosis de Vac. contra Rubeola no valido", new ActionMessage("errors.fechaPosteriorIgualActual","de Ultima Dosis de Vac. contra Rubeola", "actual"));
					}
	            }
	            
	            if (fechaVisitaDomiciliaria.trim().length()>0) {
	            	
		            if (!UtilidadFecha.validarFecha(fechaVisitaDomiciliaria)) {
		                
		                errores.add("Campo Fecha de Visita Domiciliaria no valido", new ActionMessage("errors.formatoFechaInvalido","de Visita Domiciliaria"));
		            }
	            }
	            
	            if (fechaInicioFiebre.trim().length()>0) {
	            	
		            if (!UtilidadFecha.validarFecha(fechaInicioFiebre)) {
		                
		                errores.add("Campo Fecha de Inicio de Fiebre no valido", new ActionMessage("errors.formatoFechaInvalido","de Inicio de Fiebre"));
		            }
		            else if (fechaInicioFiebreTransformada.compareTo(fechaActual)>0) {
						
						errores.add("Campo Fecha de Inicio de Fiebre no valido", new ActionMessage("errors.fechaPosteriorIgualActual","de Inicio de Fiebre", "actual"));
					}
	            }
	            
	            if (fechaInicioErupcion.trim().length()>0) {
	            	
		            if (!UtilidadFecha.validarFecha(fechaInicioErupcion)) {
		                
		                errores.add("Campo Fecha de Inicio de Erupcion no valido", new ActionMessage("errors.formatoFechaInvalido","de Inicio de Erupcion"));
		            }
		            else if (fechaInicioErupcionTransformada.compareTo(fechaActual)>0) {
						
						errores.add("Campo Fecha de Inicio de Erupcion no valido", new ActionMessage("errors.fechaPosteriorIgualActual","de Inicio de Erupcion", "actual"));
					}
	            }
	            else {
	            	errores.add("El campo Fecha de Inicio de Erupcion es Requerido", new ActionMessage("errors.required","El campo fecha de inicio de Erupcion"));
	            }
            }
            catch (NullPointerException npe) {}
            
            
            try {
	            for (int i=1;i<5;i++) {
	            	
	            	String fechaToma = datosLaboratorio.get("fechaToma"+i).toString();
	            	String fechaTomaTransformada = UtilidadFecha.conversionFormatoFechaABD(fechaToma);
	            	
	            	if (fechaToma.trim().length()>0) {
	                	
	    	            if (!UtilidadFecha.validarFecha(fechaToma)) {
	    	                
	    	                errores.add("Campo Fecha de Toma de Examen de Laboratorio "+Integer.toString(i)+" no valido", new ActionMessage("errors.formatoFechaInvalido","de Toma de Examen de Laboratorio "+Integer.toString(i)));
	    	            }
	    	            else if (fechaTomaTransformada.compareTo(fechaActual)>0) {
	    					
	    					errores.add("Campo Fecha de Toma de Examen de Laboratorio "+Integer.toString(i)+" no valido", new ActionMessage("errors.fechaPosteriorIgualActual","de Toma de Examen de Laboratorio "+Integer.toString(i), "actual"));
	    				}
	                }
	            	
	            	
	            	String fechaRecepcion = datosLaboratorio.get("fechaRecepcion"+i).toString();
	            	String fechaRecepcionTransformada = UtilidadFecha.conversionFormatoFechaABD(fechaRecepcion);
	            	
	            	if (fechaRecepcion.trim().length()>0) {
	                	
	    	            if (!UtilidadFecha.validarFecha(fechaRecepcion)) {
	    	                
	    	                errores.add("Campo Fecha de Recepcion de Examen de Laboratorio "+Integer.toString(i)+" no valido", new ActionMessage("errors.formatoFechaInvalido","de Recepcion de Examen de Laboratorio "+Integer.toString(i)));
	    	            }
	    	            else if (fechaRecepcionTransformada.compareTo(fechaActual)>0) {
	    					
	    					errores.add("Campo Fecha de Recepcion de Examen de Laboratorio "+Integer.toString(i)+" no valido", new ActionMessage("errors.fechaPosteriorIgualActual","de Recepcion de Examen de Laboratorio "+Integer.toString(i), "actual"));
	    				}
	                }
	            	
	            	
	            	String fechaResultado = datosLaboratorio.get("fechaResultado"+i).toString();
	            	String fechaResultadoTransformada = UtilidadFecha.conversionFormatoFechaABD(fechaResultado);
	            	
	            	if (fechaResultado.trim().length()>0) {
	                	
	    	            if (!UtilidadFecha.validarFecha(fechaResultado)) {
	    	                
	    	                errores.add("Campo Fecha de Resultado de Examen de Laboratorio "+Integer.toString(i)+" no valido", new ActionMessage("errors.formatoFechaInvalido","de Resultado de Examen de Laboratorio "+Integer.toString(i)));
	    	            }
	    	            else if (fechaResultadoTransformada.compareTo(fechaActual)>0) {
	    					
	    					errores.add("Campo Fecha de Resultado de Examen de Laboratorio "+Integer.toString(i)+" no valido", new ActionMessage("errors.fechaPosteriorIgualActual","de Resultado de Examen de Laboratorio "+Integer.toString(i), "actual"));
	    				}
	                }
	            	
	            	if (fechaTomaTransformada.compareTo(fechaRecepcionTransformada)>0) {
	            		errores.add("Fecha de Toma de Examen "+Integer.toString(i)+" no valida",new ActionMessage("errors.fechaPosteriorIgualActual","de Toma de Examen "+Integer.toString(i),"de Recepcion "+Integer.toString(i)));
	            	}
	            	if (fechaRecepcionTransformada.compareTo(fechaResultadoTransformada)>0) {
	            		errores.add("Fecha de Recepcion de Examen "+Integer.toString(i)+" no valida", new ActionMessage("errors.fechaPosteriorIgualActual","de Recepcion de Examen "+Integer.toString(i),"de Resultado "+Integer.toString(i)));
	            	}
	            }
            }
            catch (NullPointerException npe) {
            	
            }
        }
        
        if (estado.equals("validar")) {
        	
        	String fechaPrimeraVisitaTransformada=UtilidadFecha.conversionFormatoFechaABD(fechaVisita1);
        	String fechaVisitaDomiciliariaTransformada = UtilidadFecha.conversionFormatoFechaABD(fechaVisitaDomiciliaria);
        	String fechaInicioFiebreTransformada = UtilidadFecha.conversionFormatoFechaABD(fechaInicioFiebre);
        	String fechaInicioErupcionTransformada = UtilidadFecha.conversionFormatoFechaABD(fechaInicioErupcion);
        	String fechaConsultaTransformada=UtilidadFecha.conversionFormatoFechaABD(fechaConsultaGeneral);
            String fechaInicioSintomasTransformada=UtilidadFecha.conversionFormatoFechaABD(fechaInicioSint);
            String fechaHospitalizacionTransformada=UtilidadFecha.conversionFormatoFechaABD(fechaHospitalizacion);
            String fechaDefuncionTransformada=UtilidadFecha.conversionFormatoFechaABD(fechaDefuncion);
            String fechaUltimaDosisSarampionTransformada = UtilidadFecha.conversionFormatoFechaABD(fechaUltimaDosisSarampion);
            String fechaUltimaDosisRubeolaTransformada = UtilidadFecha.conversionFormatoFechaABD(fechaUltimaDosisRubeola);
        	
        	
            String fechaActual=UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual());
            
            if (fechaInicioSint.trim().length()>0) {
	            if (!UtilidadFecha.validarFecha(fechaInicioSint)) {
	                
	                errores.add("Campo Fecha de Inicio de Sintomas no valido", new ActionMessage("errors.formatoFechaInvalido","de Inicio de Sintomas"));
	            }
	            else if (fechaInicioSintomasTransformada.compareTo(fechaActual)>0) {
					
					errores.add("Campo Fecha de Inicio de Sintomas no valido", new ActionMessage("errors.fechaPosteriorIgualActual","de Inicio de Sintomas", "actual"));
				}
            }
            else {
            	errores.add("El campo Fecha de Inicio de Sintomas es Requerido", new ActionMessage("errors.required","El campo fecha de inicio de Sintomas"));
            }
            
            try {
	            if (fechaHospitalizacion.trim().length()>0) {
	            	
	            	if (!UtilidadFecha.validarFecha(fechaHospitalizacion)) {
		                
		                errores.add("Campo Fecha de Hospitalizacion no valido", new ActionMessage("errors.formatoFechaInvalido","de Hospitalizacion"));
		            }
		            else if (fechaHospitalizacionTransformada.compareTo(fechaActual)>0) {
						
						errores.add("Campo Fecha de Hospitalizacion no valido", new ActionMessage("errors.fechaPosteriorIgualActual","de Hospitalizacion", "actual"));
					}
	            }
	            
	            if (fechaDefuncion.trim().length()>0) {
	            	
	            	if (!UtilidadFecha.validarFecha(fechaDefuncion)) {
		                
		                errores.add("Campo Fecha de Defuncion no valido", new ActionMessage("errors.formatoFechaInvalido","de Defuncion"));
		            }
		            else if (fechaDefuncionTransformada.compareTo(fechaActual)>0) {
						
						errores.add("Campo Fecha de Defuncion no valido", new ActionMessage("errors.fechaPosteriorIgualActual","de Defuncion", "actual"));
					}
	            }
	            
	            if (fechaVisita1.trim().length()>0) {
	            	
		            if (!UtilidadFecha.validarFecha(fechaVisita1)) {
		                
		                errores.add("Campo Fecha de Primera Visita no valido", new ActionMessage("errors.formatoFechaInvalido","de Primera Visita"));
		            }
		            else if (fechaPrimeraVisitaTransformada.compareTo(fechaActual)>0) {
						
						errores.add("Campo Fecha de Primera Visita no valido", new ActionMessage("errors.fechaPosteriorIgualActual","de Primera Visita", "actual"));
					}
	            }
	            

	            if (vacunaSarampion==2) {
	            	
	            	if (!UtilidadFecha.validarFecha(fechaUltimaDosisSarampion)) {
		                
		                errores.add("Campo Fecha de Ultima Dosis de Vac. contra Sarampion no valido", new ActionMessage("errors.formatoFechaInvalido","de Ultima Dosis de Vac. contra Sarampion"));
		            }
		            else if (fechaUltimaDosisSarampionTransformada.compareTo(fechaActual)>0) {
						
						errores.add("Campo Fecha de Ultima Dosis de Vac. contra Sarampion no valido", new ActionMessage("errors.fechaPosteriorIgualActual","de Ultima Dosis de Vac. contra Sarampion", "actual"));
					}
	            }
	            
	            if (vacunaRubeola==2) {
	            	
	            	if (!UtilidadFecha.validarFecha(fechaUltimaDosisRubeola)) {
		                
		                errores.add("Campo Fecha de Ultima Dosis de Vac. contra Rubeola no valido", new ActionMessage("errors.formatoFechaInvalido","de Ultima Dosis de Vac. contra Rubeola"));
		            }
		            else if (fechaUltimaDosisRubeolaTransformada.compareTo(fechaActual)>0) {
						
						errores.add("Campo Fecha de Ultima Dosis de Vac. contra Rubeola no valido", new ActionMessage("errors.fechaPosteriorIgualActual","de Ultima Dosis de Vac. contra Rubeola", "actual"));
					}
	            }
	            
	            
	            if (fechaVisitaDomiciliaria.trim().length()>0) {
	            	
		            if (!UtilidadFecha.validarFecha(fechaVisitaDomiciliaria)) {
		                
		                errores.add("Campo Fecha de Visita Domiciliaria no valido", new ActionMessage("errors.formatoFechaInvalido","de Visita Domiciliaria"));
		            }
	            }
	            
	            if (fechaInicioFiebre.trim().length()>0) {
	            	
		            if (!UtilidadFecha.validarFecha(fechaInicioFiebre)) {
		                
		                errores.add("Campo Fecha de Inicio de Fiebre no valido", new ActionMessage("errors.formatoFechaInvalido","de Inicio de Fiebre"));
		            }
		            else if (fechaInicioFiebreTransformada.compareTo(fechaActual)>0) {
						
						errores.add("Campo Fecha de Inicio de Fiebre no valido", new ActionMessage("errors.fechaPosteriorIgualActual","de Inicio de Fiebre", "actual"));
					}
	            }
	            
	            if (fechaInicioErupcion.trim().length()>0) {
	            	
		            if (!UtilidadFecha.validarFecha(fechaInicioErupcion)) {
		                
		                errores.add("Campo Fecha de Inicio de Erupcion no valido", new ActionMessage("errors.formatoFechaInvalido","de Inicio de Erupcion"));
		            }
		            else if (fechaInicioErupcionTransformada.compareTo(fechaActual)>0) {
						
						errores.add("Campo Fecha de Inicio de Erupcion no valido", new ActionMessage("errors.fechaPosteriorIgualActual","de Inicio de Erupcion", "actual"));
					}
	            }
	            else {
	            	errores.add("El campo Fecha de Inicio de Erupcion es Requerido", new ActionMessage("errors.required","El campo fecha de inicio de Erupcion"));
	            }
            }
            catch (NullPointerException npe) {}
            
            boolean noHayDatosLab = false;
                
            try {
	            for (int i=1;i<5;i++) {
	            	
	            	String fechaToma = datosLaboratorio.get("fechaToma"+i).toString();
	            	String fechaTomaTransformada = UtilidadFecha.conversionFormatoFechaABD(fechaToma);
	            	
	            	if (fechaToma.trim().length()>0) {
	                	
	            		noHayDatosLab = true;
	            		
	    	            if (!UtilidadFecha.validarFecha(fechaToma)) {
	    	                
	    	                errores.add("Campo Fecha de Toma de Examen de Laboratorio "+Integer.toString(i)+" no valido", new ActionMessage("errors.formatoFechaInvalido","de Toma de Examen de Laboratorio "+Integer.toString(i)));
	    	            }
	    	            else if (fechaTomaTransformada.compareTo(fechaActual)>0) {
	    					
	    					errores.add("Campo Fecha de Toma de Examen de Laboratorio "+Integer.toString(i)+" no valido", new ActionMessage("errors.fechaPosteriorIgualActual","de Toma de Examen de Laboratorio "+Integer.toString(i), "actual"));
	    				}
	                }
	            	
	            	
	            	String fechaRecepcion = datosLaboratorio.get("fechaRecepcion"+i).toString();
	            	String fechaRecepcionTransformada = UtilidadFecha.conversionFormatoFechaABD(fechaRecepcion);
	            	
	            	if (fechaRecepcion.trim().length()>0) {
	                	
	            		noHayDatosLab = true;
	            		
	    	            if (!UtilidadFecha.validarFecha(fechaRecepcion)) {
	    	                
	    	                errores.add("Campo Fecha de Recepcion de Examen de Laboratorio "+Integer.toString(i)+" no valido", new ActionMessage("errors.formatoFechaInvalido","de Recepcion de Examen de Laboratorio "+Integer.toString(i)));
	    	            }
	    	            else if (fechaRecepcionTransformada.compareTo(fechaActual)>0) {
	    					
	    					errores.add("Campo Fecha de Recepcion de Examen de Laboratorio "+Integer.toString(i)+" no valido", new ActionMessage("errors.fechaPosteriorIgualActual","de Recepcion de Examen de Laboratorio "+Integer.toString(i), "actual"));
	    				}
	                }
	            	
	            	
	            	String fechaResultado = datosLaboratorio.get("fechaResultado"+i).toString();
	            	String fechaResultadoTransformada = UtilidadFecha.conversionFormatoFechaABD(fechaResultado);
	            	
	            	if (fechaResultado.trim().length()>0) {
	                	
	            		noHayDatosLab = true;
	            		
	    	            if (!UtilidadFecha.validarFecha(fechaResultado)) {
	    	                
	    	                errores.add("Campo Fecha de Resultado de Examen de Laboratorio "+Integer.toString(i)+" no valido", new ActionMessage("errors.formatoFechaInvalido","de Resultado de Examen de Laboratorio "+Integer.toString(i)));
	    	            }
	    	            else if (fechaResultadoTransformada.compareTo(fechaActual)>0) {
	    					
	    					errores.add("Campo Fecha de Resultado de Examen de Laboratorio "+Integer.toString(i)+" no valido", new ActionMessage("errors.fechaPosteriorIgualActual","de Resultado de Examen de Laboratorio "+Integer.toString(i), "actual"));
	    				}
	                }
	            }
            }
            catch (NullPointerException npe) {
            	
            }
            /*
            if (!noHayDatosLab) {
            	errores.add("No se agregaron datos de laboratorio. Guarde la ficha como Pendiente.", new ActionMessage("error.epidemiologia.nohaylaboratorios"));
            }
            */
        }
        
        return errores;
    }

	public int getAdenopatia() {
		return adenopatia;
	}

	public void setAdenopatia(int adenopatia) {
		this.adenopatia = adenopatia;
	}

	public int getArtralgia() {
		return artralgia;
	}

	public void setArtralgia(int artralgia) {
		this.artralgia = artralgia;
	}

	public String getCodigoDiagnostico() {
		return codigoDiagnostico;
	}

	public void setCodigoDiagnostico(String codigoDiagnostico) {
		this.codigoDiagnostico = codigoDiagnostico;
	}

	public int getCodigoFichaSarampion() {
		return codigoFichaSarampion;
	}

	public void setCodigoFichaSarampion(int codigoFichaSarampion) {
		this.codigoFichaSarampion = codigoFichaSarampion;
	}

	public int getCodigoNotificacion() {
		return codigoNotificacion;
	}

	public void setCodigoNotificacion(int codigoNotificacion) {
		this.codigoNotificacion = codigoNotificacion;
	}

	public int getCodigoPaciente() {
		return codigoPaciente;
	}

	public void setCodigoPaciente(int codigoPaciente) {
		this.codigoPaciente = codigoPaciente;
	}

	public int getConjuntivitis() {
		return conjuntivitis;
	}

	public void setConjuntivitis(int conjuntivitis) {
		this.conjuntivitis = conjuntivitis;
	}

	public int getCoriza() {
		return coriza;
	}

	public void setCoriza(int coriza) {
		this.coriza = coriza;
	}

	public HashMap getDatosLaboratorio() {
		return datosLaboratorio;
	}

	public void setDatosLaboratorio(HashMap datosLaboratorio) {
		this.datosLaboratorio = datosLaboratorio;
	}

	public int getDiagnosticoFinal() {
		return diagnosticoFinal;
	}

	public void setDiagnosticoFinal(int diagnosticoFinal) {
		this.diagnosticoFinal = diagnosticoFinal;
	}

	public String getDireccionTrabajoPadre() {
		return direccionTrabajoPadre;
	}

	public void setDireccionTrabajoPadre(String direccionTrabajoPadre) {
		this.direccionTrabajoPadre = direccionTrabajoPadre;
	}

	public int getDuracionErupcion() {
		return duracionErupcion;
	}

	public void setDuracionErupcion(int duracionErupcion) {
		this.duracionErupcion = duracionErupcion;
	}

	public int getEmbarazada() {
		return embarazada;
	}

	public void setEmbarazada(int embarazada) {
		this.embarazada = embarazada;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getFechaInicioErupcion() {
		return fechaInicioErupcion;
	}

	public void setFechaInicioErupcion(String fechaInicioErupcion) {
		this.fechaInicioErupcion = fechaInicioErupcion;
	}

	public String getFechaInicioFiebre() {
		return fechaInicioFiebre;
	}

	public void setFechaInicioFiebre(String fechaInicioFiebre) {
		this.fechaInicioFiebre = fechaInicioFiebre;
	}

	public String getFechaUltimaDosisRubeola() {
		return fechaUltimaDosisRubeola;
	}

	public void setFechaUltimaDosisRubeola(String fechaUltimaDosisRubeola) {
		this.fechaUltimaDosisRubeola = fechaUltimaDosisRubeola;
	}

	public String getFechaUltimaDosisSarampion() {
		return fechaUltimaDosisSarampion;
	}

	public void setFechaUltimaDosisSarampion(String fechaUltimaDosisSarampion) {
		this.fechaUltimaDosisSarampion = fechaUltimaDosisSarampion;
	}

	public String getFechaVisita1() {
		return fechaVisita1;
	}

	public void setFechaVisita1(String fechaVisita1) {
		this.fechaVisita1 = fechaVisita1;
	}

	public String getFechaVisitaDomiciliaria() {
		return fechaVisitaDomiciliaria;
	}

	public void setFechaVisitaDomiciliaria(String fechaVisitaDomiciliaria) {
		this.fechaVisitaDomiciliaria = fechaVisitaDomiciliaria;
	}

	public int getFiebre() {
		return fiebre;
	}

	public void setFiebre(int fiebre) {
		this.fiebre = fiebre;
	}

	public int getFuenteDatosRubeola() {
		return fuenteDatosRubeola;
	}

	public void setFuenteDatosRubeola(int fuenteDatosRubeola) {
		this.fuenteDatosRubeola = fuenteDatosRubeola;
	}

	public int getFuenteDatosSarampion() {
		return fuenteDatosSarampion;
	}

	public void setFuenteDatosSarampion(int fuenteDatosSarampion) {
		this.fuenteDatosSarampion = fuenteDatosSarampion;
	}

	public int getFuenteNotificacion() {
		return fuenteNotificacion;
	}

	public void setFuenteNotificacion(int fuenteNotificacion) {
		this.fuenteNotificacion = fuenteNotificacion;
	}

	public int getHuboCasoConfirmado() {
		return huboCasoConfirmado;
	}

	public void setHuboCasoConfirmado(int huboCasoConfirmado) {
		this.huboCasoConfirmado = huboCasoConfirmado;
	}

	public int getHuboContacto() {
		return huboContacto;
	}

	public void setHuboContacto(int huboContacto) {
		this.huboContacto = huboContacto;
	}

	public int getHuboContactoEmbarazada() {
		return huboContactoEmbarazada;
	}

	public void setHuboContactoEmbarazada(int huboContactoEmbarazada) {
		this.huboContactoEmbarazada = huboContactoEmbarazada;
	}

	public int getHuboViaje() {
		return huboViaje;
	}

	public void setHuboViaje(int huboViaje) {
		this.huboViaje = huboViaje;
	}

	public Logger getLogger() {
		return logger;
	}

	public void setLogger(Logger logger) {
		this.logger = logger;
	}

	public String getLoginUsuario() {
		return loginUsuario;
	}

	public void setLoginUsuario(String loginUsuario) {
		this.loginUsuario = loginUsuario;
	}

	public String getLugarParto() {
		return lugarParto;
	}

	public void setLugarParto(String lugarParto) {
		this.lugarParto = lugarParto;
	}

	public String getLugarViaje() {
		return lugarViaje;
	}

	public void setLugarViaje(String lugarViaje) {
		this.lugarViaje = lugarViaje;
	}

	public String getNombrePadre() {
		return nombrePadre;
	}

	public void setNombrePadre(String nombrePadre) {
		this.nombrePadre = nombrePadre;
	}

	public boolean isNotificar() {
		return notificar;
	}

	public void setNotificar(boolean notificar) {
		this.notificar = notificar;
	}

	public int getNumeroDosisRubeola() {
		return numeroDosisRubeola;
	}

	public void setNumeroDosisRubeola(int numeroDosisRubeola) {
		this.numeroDosisRubeola = numeroDosisRubeola;
	}

	public int getNumeroDosisSarampion() {
		return numeroDosisSarampion;
	}

	public void setNumeroDosisSarampion(int numeroDosisSarampion) {
		this.numeroDosisSarampion = numeroDosisSarampion;
	}

	public int getNumeroSemanas() {
		return numeroSemanas;
	}

	public void setNumeroSemanas(int numeroSemanas) {
		this.numeroSemanas = numeroSemanas;
	}

	public int getNumeroSolicitud() {
		return numeroSolicitud;
	}

	public void setNumeroSolicitud(int numeroSolicitud) {
		this.numeroSolicitud = numeroSolicitud;
	}

	public String getOcupacionPadre() {
		return ocupacionPadre;
	}

	public void setOcupacionPadre(String ocupacionPadre) {
		this.ocupacionPadre = ocupacionPadre;
	}

	public int getTipoErupcion() {
		return tipoErupcion;
	}

	public void setTipoErupcion(int tipoErupcion) {
		this.tipoErupcion = tipoErupcion;
	}

	public int getTos() {
		return tos;
	}

	public void setTos(int tos) {
		this.tos = tos;
	}

	public int getVacunaRubeola() {
		return vacunaRubeola;
	}

	public void setVacunaRubeola(int vacunaRubeola) {
		this.vacunaRubeola = vacunaRubeola;
	}

	public int getVacunaSarampion() {
		return vacunaSarampion;
	}

	public void setVacunaSarampion(int vacunaSarampion) {
		this.vacunaSarampion = vacunaSarampion;
	}


	public int getEstadoFicha() {
		return estadoFicha;
	}


	public void setEstadoFicha(int estadoFicha) {
		this.estadoFicha = estadoFicha;
	}



	public String getCiudadNacimiento() {
		return ciudadNacimiento;
	}


	public void setCiudadNacimiento(String ciudadNacimiento) {
		this.ciudadNacimiento = ciudadNacimiento;
	}


	public String getCiudadResidencia() {
		return ciudadResidencia;
	}


	public void setCiudadResidencia(String ciudadResidencia) {
		this.ciudadResidencia = ciudadResidencia;
	}


	public String getDepartamentoNacimiento() {
		return departamentoNacimiento;
	}


	public void setDepartamentoNacimiento(String departamentoNacimiento) {
		this.departamentoNacimiento = departamentoNacimiento;
	}


	public String getDepartamentoResidencia() {
		return departamentoResidencia;
	}


	public void setDepartamentoResidencia(String departamentoResidencia) {
		this.departamentoResidencia = departamentoResidencia;
	}


	public String getDireccionPaciente() {
		return direccionPaciente;
	}


	public void setDireccionPaciente(String direccionPaciente) {
		this.direccionPaciente = direccionPaciente;
	}


	public String getDocumento() {
		return documento;
	}


	public void setDocumento(String documento) {
		this.documento = documento;
	}


	public String getEdad() {
		return edad;
	}


	public void setEdad(String edad) {
		this.edad = edad;
	}


	public String getEstadoCivil() {
		return estadoCivil;
	}


	public void setEstadoCivil(String estadoCivil) {
		this.estadoCivil = estadoCivil;
	}


	public String getFechaNacimiento() {
		return fechaNacimiento;
	}


	public void setFechaNacimiento(String fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}


	public String getGenero() {
		return genero;
	}


	public void setGenero(String genero) {
		this.genero = genero;
	}


	public String getPrimerApellido() {
		return primerApellido;
	}


	public void setPrimerApellido(String primerApellido) {
		this.primerApellido = primerApellido;
	}


	public String getPrimerNombre() {
		return primerNombre;
	}


	public void setPrimerNombre(String primerNombre) {
		this.primerNombre = primerNombre;
	}


	public String getSegundoApellido() {
		return segundoApellido;
	}


	public void setSegundoApellido(String segundoApellido) {
		this.segundoApellido = segundoApellido;
	}


	public String getSegundoNombre() {
		return segundoNombre;
	}


	public void setSegundoNombre(String segundoNombre) {
		this.segundoNombre = segundoNombre;
	}

	
	public String getTelefonoPaciente() {
		return telefonoPaciente;
	}


	public void setTelefonoPaciente(String telefonoPaciente) {
		this.telefonoPaciente = telefonoPaciente;
	}


	public String getSire() {
		return sire;
	}


	public void setSire(String sire) {
		this.sire = sire;
	}


	public String getDepartamentoParto() {
		return departamentoParto;
	}


	public void setDepartamentoParto(String departamentoParto) {
		this.departamentoParto = departamentoParto;
	}


	public String getDepartamentoViaje() {
		return departamentoViaje;
	}


	public void setDepartamentoViaje(String departamentoViaje) {
		this.departamentoViaje = departamentoViaje;
	}


	public String getMunicipioParto() {
		return municipioParto;
	}


	public void setMunicipioParto(String municipioParto) {
		this.municipioParto = municipioParto;
	}


	public String getMunicipioViaje() {
		return municipioViaje;
	}


	public void setMunicipioViaje(String municipioViaje) {
		this.municipioViaje = municipioViaje;
	}


	public String getAseguradora() {
		return aseguradora;
	}


	public void setAseguradora(String aseguradora) {
		this.aseguradora = aseguradora;
	}


	public String getBarrioResidencia() {
		return barrioResidencia;
	}


	public void setBarrioResidencia(String barrioResidencia) {
		this.barrioResidencia = barrioResidencia;
	}


	public String getCodigoDepProcedencia() {
		return codigoDepProcedencia;
	}


	public void setCodigoDepProcedencia(String codigoDepProcedencia) {
		this.codigoDepProcedencia = codigoDepProcedencia;
	}


	public String getCodigoMunProcedencia() {
		return codigoMunProcedencia;
	}


	public void setCodigoMunProcedencia(String codigoMunProcedencia) {
		this.codigoMunProcedencia = codigoMunProcedencia;
	}


	public String getDepartamentoNotifica() {
		return departamentoNotifica;
	}


	public void setDepartamentoNotifica(String departamentoNotifica) {
		this.departamentoNotifica = departamentoNotifica;
	}


	public int getDesplazado() {
		return desplazado;
	}


	public void setDesplazado(int desplazado) {
		this.desplazado = desplazado;
	}


	public boolean isEstaVivo() {
		return estaVivo;
	}


	public void setEstaVivo(boolean estaVivo) {
		this.estaVivo = estaVivo;
	}


	public String getEtnia() {
		return etnia;
	}


	public void setEtnia(String etnia) {
		this.etnia = etnia;
	}


	public String getFechaConsultaGeneral() {
		return fechaConsultaGeneral;
	}


	public void setFechaConsultaGeneral(String fechaConsultaGeneral) {
		this.fechaConsultaGeneral = fechaConsultaGeneral;
	}


	public String getFechaDefuncion() {
		return fechaDefuncion;
	}


	public void setFechaDefuncion(String fechaDefuncion) {
		this.fechaDefuncion = fechaDefuncion;
	}


	public String getFechaHospitalizacion() {
		return fechaHospitalizacion;
	}


	public void setFechaHospitalizacion(String fechaHospitalizacion) {
		this.fechaHospitalizacion = fechaHospitalizacion;
	}


	public String getFechaInicioSint() {
		return fechaInicioSint;
	}


	public void setFechaInicioSint(String fechaInicioSint) {
		this.fechaInicioSint = fechaInicioSint;
	}


	public String getFechaNotificacion() {
		return fechaNotificacion;
	}


	public void setFechaNotificacion(String fechaNotificacion) {
		this.fechaNotificacion = fechaNotificacion;
	}


	public boolean isHospitalizado() {
		return hospitalizado;
	}


	public void setHospitalizado(boolean hospitalizado) {
		this.hospitalizado = hospitalizado;
	}


	public int getInstitucionAtendio() {
		return institucionAtendio;
	}


	public void setInstitucionAtendio(int institucionAtendio) {
		this.institucionAtendio = institucionAtendio;
	}


	public String getLugarNotifica() {
		return lugarNotifica;
	}


	public void setLugarNotifica(String lugarNotifica) {
		this.lugarNotifica = lugarNotifica;
	}


	public String getLugarProcedencia() {
		return lugarProcedencia;
	}


	public void setLugarProcedencia(String lugarProcedencia) {
		this.lugarProcedencia = lugarProcedencia;
	}


	public String getMunicipioNotifica() {
		return municipioNotifica;
	}


	public void setMunicipioNotifica(String municipioNotifica) {
		this.municipioNotifica = municipioNotifica;
	}


	public String getNombreProfesional() {
		return nombreProfesional;
	}


	public void setNombreProfesional(String nombreProfesional) {
		this.nombreProfesional = nombreProfesional;
	}


	public String getOcupacion() {
		return ocupacion;
	}


	public void setOcupacion(String ocupacion) {
		this.ocupacion = ocupacion;
	}


	public String getRegimenSalud() {
		return regimenSalud;
	}


	public void setRegimenSalud(String regimenSalud) {
		this.regimenSalud = regimenSalud;
	}


	public int getTipoCaso() {
		return tipoCaso;
	}


	public void setTipoCaso(int tipoCaso) {
		this.tipoCaso = tipoCaso;
	}


	public String getTipoId() {
		return tipoId;
	}


	public void setTipoId(String tipoId) {
		this.tipoId = tipoId;
	}


	public String getZonaDomicilio() {
		return zonaDomicilio;
	}


	public void setZonaDomicilio(String zonaDomicilio) {
		this.zonaDomicilio = zonaDomicilio;
	}


	public int getAnyoSemanaEpi() {
		return anyoSemanaEpi;
	}


	public void setAnyoSemanaEpi(int anyoSemanaEpi) {
		this.anyoSemanaEpi = anyoSemanaEpi;
	}


	public int getSemanaEpidemiologica() {
		return semanaEpidemiologica;
	}


	public void setSemanaEpidemiologica(int semanaEpidemiologica) {
		this.semanaEpidemiologica = semanaEpidemiologica;
	}


	public String getNombreInvestigador() {
		return nombreInvestigador;
	}


	public void setNombreInvestigador(String nombreInvestigador) {
		this.nombreInvestigador = nombreInvestigador;
	}


	public String getTelefonoInvestigador() {
		return telefonoInvestigador;
	}


	public void setTelefonoInvestigador(String telefonoInvestigador) {
		this.telefonoInvestigador = telefonoInvestigador;
	}


	public int getCodigoEnfNotificable() {
		return codigoEnfNotificable;
	}


	public void setCodigoEnfNotificable(int codigoEnfNotificable) {
		this.codigoEnfNotificable = codigoEnfNotificable;
	}


	public int getCodigoConvenio() {
		return codigoConvenio;
	}


	public void setCodigoConvenio(int codigoConvenio) {
		this.codigoConvenio = codigoConvenio;
	}


	public String getValorDivDx() {
		return valorDivDx;
	}


	public void setValorDivDx(String valorDivDx) {
		this.valorDivDx = valorDivDx;
	}


	public String getEsPrimeraVez() {
		return esPrimeraVez;
	}


	public void setEsPrimeraVez(String esPrimeraVez) {
		this.esPrimeraVez = esPrimeraVez;
	}


	public boolean isFichamodulo() {
		return fichamodulo;
	}


	public void setFichamodulo(boolean fichamodulo) {
		this.fichamodulo = fichamodulo;
	}


	public boolean isVieneDeFichasAnteriores() {
		return vieneDeFichasAnteriores;
	}


	public void setVieneDeFichasAnteriores(boolean vieneDeFichasAnteriores) {
		this.vieneDeFichasAnteriores = vieneDeFichasAnteriores;
	}


	public String getDiagnosticosSeleccionados() {
		return diagnosticosSeleccionados;
	}


	public void setDiagnosticosSeleccionados(String diagnosticosSeleccionados) {
		this.diagnosticosSeleccionados = diagnosticosSeleccionados;
	}


	public boolean isEpidemiologia() {
		return epidemiologia;
	}


	public void setEpidemiologia(boolean epidemiologia) {
		this.epidemiologia = epidemiologia;
	}


	public String getIdCheckBox() {
		return idCheckBox;
	}


	public void setIdCheckBox(String idCheckBox) {
		this.idCheckBox = idCheckBox;
	}


	public String getIdDiagnostico() {
		return idDiagnostico;
	}


	public void setIdDiagnostico(String idDiagnostico) {
		this.idDiagnostico = idDiagnostico;
	}


	public String getIdDiagSeleccionados() {
		return idDiagSeleccionados;
	}


	public void setIdDiagSeleccionados(String idDiagSeleccionados) {
		this.idDiagSeleccionados = idDiagSeleccionados;
	}


	public String getIdDiv() {
		return idDiv;
	}


	public void setIdDiv(String idDiv) {
		this.idDiv = idDiv;
	}


	public String getIdHiddenCheckBox() {
		return idHiddenCheckBox;
	}


	public void setIdHiddenCheckBox(String idHiddenCheckBox) {
		this.idHiddenCheckBox = idHiddenCheckBox;
	}


	public String getIdNumero() {
		return idNumero;
	}


	public void setIdNumero(String idNumero) {
		this.idNumero = idNumero;
	}


	public String getIdValorFicha() {
		return idValorFicha;
	}


	public void setIdValorFicha(String idValorFicha) {
		this.idValorFicha = idValorFicha;
	}


	public int getNumero() {
		return numero;
	}


	public void setNumero(int numero) {
		this.numero = numero;
	}


	public String getPropiedadDiagnostico() {
		return propiedadDiagnostico;
	}


	public void setPropiedadDiagnostico(String propiedadDiagnostico) {
		this.propiedadDiagnostico = propiedadDiagnostico;
	}


	public String getPropiedadHiddenCheckBox() {
		return propiedadHiddenCheckBox;
	}


	public void setPropiedadHiddenCheckBox(String propiedadHiddenCheckBox) {
		this.propiedadHiddenCheckBox = propiedadHiddenCheckBox;
	}


	public int getTipoDiagnostico() {
		return tipoDiagnostico;
	}


	public void setTipoDiagnostico(int tipoDiagnostico) {
		this.tipoDiagnostico = tipoDiagnostico;
	}


	public String getValorDiagnostico() {
		return valorDiagnostico;
	}


	public void setValorDiagnostico(String valorDiagnostico) {
		this.valorDiagnostico = valorDiagnostico;
	}


	public boolean isHayLaboratorios() {
		return hayLaboratorios;
	}


	public void setHayLaboratorios(boolean hayLaboratorios) {
		this.hayLaboratorios = hayLaboratorios;
	}


	public boolean isHayServicios() {
		return hayServicios;
	}


	public void setHayServicios(boolean hayServicios) {
		this.hayServicios = hayServicios;
	}


	public boolean isActiva() {
		return activa;
	}


	public void setActiva(boolean activa) {
		this.activa = activa;
	}


	public String getUrlFichasAnteriores() {
		return urlFichasAnteriores;
	}


	public void setUrlFichasAnteriores(String urlFichasAnteriores) {
		this.urlFichasAnteriores = urlFichasAnteriores;
	}


	public int getCodigoInstitucion() {
		return codigoInstitucion;
	}


	public void setCodigoInstitucion(int codigoInstitucion) {
		this.codigoInstitucion = codigoInstitucion;
	}


	public int getAreaProcedencia() {
		return areaProcedencia;
	}


	public void setAreaProcedencia(int areaProcedencia) {
		this.areaProcedencia = areaProcedencia;
	}


	public String getGrupoPoblacional() {
		return grupoPoblacional;
	}


	public void setGrupoPoblacional(String grupoPoblacional) {
		this.grupoPoblacional = grupoPoblacional;
	}


	public String getPais() {
		return pais;
	}


	public void setPais(String pais) {
		this.pais = pais;
	}


	public String getPaisExpedicion() {
		return paisExpedicion;
	}


	public void setPaisExpedicion(String paisExpedicion) {
		this.paisExpedicion = paisExpedicion;
	}


	public String getPaisNacimiento() {
		return paisNacimiento;
	}


	public void setPaisNacimiento(String paisNacimiento) {
		this.paisNacimiento = paisNacimiento;
	}


	public String getPaisResidencia() {
		return paisResidencia;
	}


	public void setPaisResidencia(String paisResidencia) {
		this.paisResidencia = paisResidencia;
	}


}
