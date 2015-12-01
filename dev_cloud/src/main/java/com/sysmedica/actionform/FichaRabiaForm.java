/*
 * Creado en 14-jul-2005
 * 
 * Autor Santiago Salamanca
 * SysMédica
 */
package com.sysmedica.actionform;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import util.UtilidadFecha;
import util.ValoresPorDefecto;

import com.sysmedica.mundo.saludpublica.Animal;
import com.sysmedica.mundo.saludpublica.TratamientoAntirrabico;
import com.sysmedica.mundo.saludpublica.Paciente;

/**
 * @author santiago
 *
 */
public class FichaRabiaForm extends ValidatorForm {
    
    private transient Logger logger=Logger.getLogger(FichaRabiaForm.class);
    
    private boolean activa;
    
    private String estado;
    private boolean notificar;
    
    private int codigoInstitucion;
    
    private int codigoFichaRabia;
    private int codigoNotificacion;
    private int codigoPaciente;
    private String codigoDiagnostico;
    private int codigoConvenio;
    
    private String valorDivDx;
    
    private int codigoEnfNotificable;
    
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
    /*
    private String primerNombreUsuario;
    private String segundoNombreUsuario;
    private String primerApellidoUsuario;
    private String segundoApellidoUsuario;
    private String identificacionUsuario;
    */
    
    
    //**************************************
    // DATOS CARA B :
    private int estadoFicha;
    private int sueroAntirrabico;
    private int tipoSuero;
    private int cantidadAplicada;
    private String fechaAplicacion;
    private int tipoVacunaSeguimiento;
    private int vacunaAntirrabica;
    private int tipoVacuna;
    private int dosisAplicadas;
    private String fechaUltimaDosis;
    private int tipoAgresion;
    private boolean provocada;
    private int tipoLesion;
    private boolean cabeza;
    private boolean manos;
    private boolean tronco;
    private boolean extsuperiores;
    private boolean extinferiores;
    private HashMap localizacionesAnatomicas;
    private int tipoExposicion;
    private String observacion;
    private String codigoMunicipioAgresion;
    private String codigoDepartamentoAgresion;
    private String procedencia;
    private String direccion;
    private String fechaConsulta;
    private String fechaAgresion;
    private String lugarAgresion;
    private int profundidadLesion;
    private int confDiagnosticaCasoRabia;
    private String fechaMuestraCasoRabia;
    private String pais;
    private int areaProcedencia;
    private String grupoPoblacional;
    
    private Paciente paciente;
    private TratamientoAntirrabico tratamientoAntirrabico;
    private Animal animal;
    private HashMap dosisRabia;
    
   
    
    /*
     * Atributos del Animal
     */
    private int especie;
    private String fechaInicioSintomas;
    private String fechaMuerte;
    private String fechaTomaMuestra;
    private int fuenteInformacionLaboratorio;
    private boolean vacunado;
    private String fechaUltimaDosisAnimal;
    private String nombrePropietario;
    private String direccionPropietario;
    private int estadoMomentoAgresion;
    private int ubicacionAnimal;
    private int numeroDiasObserva;
    private int lugarObservacion;
    private int estadoAnimalObserva;
    private int confirmacionDiagnosticaAnimal;
    
    
    
    /*
     * Atributos del Tratamiento
     */
    private boolean lavadoHerida;
    private boolean suturaHerida;
    private boolean aplicacionSuero;
    private String fechaAplicacionSuero;
    private int tipoSueroTratamiento;
    private int cantidadSueroGlutea;
    private int cantidadSueroHerida;
    private String numeroLote;
    private String laboratorioProductor;
    private boolean aplicarVacuna;
    private int numeroDosisTratamiento;
    private int tipoVacunaTratamiento;
    private String fechaVacunaDosis1;
    private String fechaVacunaDosis2;
    private String fechaVacunaDosis3;
    private String fechaVacunaDosis4;
    private String fechaVacunaDosis5;
    private boolean suspensionTratamiento;
    private int razonSuspension;
    private String fechaTomaMuestraMuerte;
    private boolean confirmacionDiagnostica;
    private int pruebasLaboratorio;
    private int reaccionesVacunaSuero;
    private int evolucionPaciente;
    
    
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
    
    private boolean hayServicios;
    private boolean hayLaboratorios;
    
    
    /**
     * Metodo que resetea los campos
     *
     */
    public void reset() {
       
        /*
         * Reset de los atributos de la ficha
         */
    	sire="";
        sueroAntirrabico = 0;
        tipoSuero = 0;
        cantidadAplicada = 0;
        fechaAplicacion = "";
        tipoVacunaSeguimiento = 0;
        vacunaAntirrabica = 0;
        tipoVacuna = 0;
        dosisAplicadas = 0;
        fechaUltimaDosis = "";
        tipoAgresion = 0;
        tipoLesion = 0;
        localizacionesAnatomicas = new HashMap();
        localizacionesAnatomicas.clear();
        tipoExposicion = 0;
        observacion = "";
        codigoMunicipioAgresion = "021";
        codigoDepartamentoAgresion = "11";
        procedencia = "";
        direccion = "";
        fechaConsulta = "";
        fechaAgresion = "";   
        estaVivo = false;
        fechaNotificacion = "";
        fechaInicioSint = "";
        hospitalizado = false;
        provocada = false;
        pruebasLaboratorio = 0;
        profundidadLesion = 0;
        confDiagnosticaCasoRabia = 0;
        fechaMuestraCasoRabia = "";
        
        
        /*
         * Atributos del Animal
         */
        especie = 0;
        fuenteInformacionLaboratorio = 0;
        vacunado = false;
        fechaUltimaDosisAnimal = "";
        fechaInicioSintomas = "";
        
        fechaMuerte = "";
        fechaTomaMuestra = "";
        
        nombrePropietario = "";
        direccionPropietario = "";
        estadoMomentoAgresion = 0;
        ubicacionAnimal = 0;
        numeroDiasObserva = 0;
        lugarObservacion = 0;
        estadoAnimalObserva = 0;
        confirmacionDiagnosticaAnimal = 0;
        
        /*
         * Atributos del Tratamiento
         */
        
        tipoSueroTratamiento = 0;
       
        aplicarVacuna = false;
        numeroDosisTratamiento = 0;
        
        lavadoHerida = false;
        suturaHerida = false;
        aplicacionSuero = false;
        
        String ciudad = ValoresPorDefecto.getCiudadNacimiento(codigoInstitucion);
        String lugar = ciudad.split("-")[1]+"-"+ciudad.split("-")[0];
        
        lugarNotifica = lugar;
        lugarProcedencia = lugar;
        
        tipoCaso = 0;
        
        cabeza = false;
        manos = false;
        tronco = false;
        extsuperiores = false;
        extinferiores = false;
        
        fechaHospitalizacion = "";
        fechaDefuncion = "";
        nombreProfesional = "";
        
        lavadoHerida = false;
        suturaHerida = false;
        aplicacionSuero = false;
        fechaAplicacionSuero = "";
        tipoSueroTratamiento = 0;
        cantidadSueroGlutea = 0;
        cantidadSueroHerida = 0;
        numeroLote = "";
        laboratorioProductor = "";
        aplicarVacuna = false;
        numeroDosisTratamiento = 0;
        tipoVacunaTratamiento = 0;
        fechaVacunaDosis1 = "";
        fechaVacunaDosis2 = "";
        fechaVacunaDosis3 = "";
        fechaVacunaDosis4 = "";
        fechaVacunaDosis5 = "";
        suspensionTratamiento = false;
        razonSuspension = 0;
        fechaTomaMuestraMuerte = "";
        confirmacionDiagnostica = false;
        pruebasLaboratorio = 0;
        reaccionesVacunaSuero = 0;
        evolucionPaciente = 0;
        
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
        	
        	String fechaAgresionTransformada=UtilidadFecha.conversionFormatoFechaABD(fechaAgresion);
            String fechaAplicacionTransformada=UtilidadFecha.conversionFormatoFechaABD(fechaAplicacion);
            String fechaUltimaDosisTransformada=UtilidadFecha.conversionFormatoFechaABD(fechaUltimaDosis);
            String fechaActual=UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual());
            String fechaConsultaTransformada=UtilidadFecha.conversionFormatoFechaABD(fechaConsultaGeneral);
            String fechaInicioSintomasTransformada=UtilidadFecha.conversionFormatoFechaABD(fechaInicioSint);
            String fechaHospitalizacionTransformada=UtilidadFecha.conversionFormatoFechaABD(fechaHospitalizacion);
            String fechaDefuncionTransformada=UtilidadFecha.conversionFormatoFechaABD(fechaDefuncion);
            
            
            
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
            
            if (fechaAgresion.trim().length()>0) {
            	
	            if (!UtilidadFecha.validarFecha(fechaAgresion)) {
	                
	                errores.add("Campo Fecha de Agresion no valido", new ActionMessage("errors.formatoFechaInvalido","de Agresion"));
	            }
	            else if (fechaAgresionTransformada.compareTo(fechaActual)>0) {
					
					errores.add("Campo Fecha de Agresion no valido", new ActionMessage("errors.fechaPosteriorIgualActual","de Agresion", "actual"));
				}
            }
            
            if (sueroAntirrabico==2&&fechaAplicacion.length()>0) {
	            if (!UtilidadFecha.validarFecha(fechaAplicacion)) {
	                
	                errores.add("Campo Fecha de Aplicacion de suero no valido", new ActionMessage("errors.formatoFechaInvalido","de Aplicacion"));
	            }
	            else if (fechaAplicacionTransformada.compareTo(fechaActual)>0) {
					
					errores.add("Campo Fecha de Aplicacion de suero no valido", new ActionMessage("errors.fechaPosteriorIgualActual","de Aplicacion", "actual"));
				}
            }
            
            
            if (vacunaAntirrabica==2&&fechaUltimaDosis.length()>0) {
	            if (!UtilidadFecha.validarFecha(fechaUltimaDosis)) {
	                
	                errores.add("Campo Fecha de ultima dosis de vacunacion no valido", new ActionMessage("errors.formatoFechaInvalido","de ultima dosis de vacunacion"));
	            }
	            else if (fechaUltimaDosisTransformada.compareTo(fechaActual)>0) {
					
					errores.add("Campo Fecha de ultima dosis de vacunacion no valido", new ActionMessage("errors.fechaPosteriorIgualActual","de ultima dosis de vacunacion", "actual"));
				}
            }
            
            
            if (aplicacionSuero) {
	            if (!UtilidadFecha.validarFecha(fechaAplicacionSuero)) {
	                
	                errores.add("Campo Fecha de aplicacion de suero no valido", new ActionMessage("errors.formatoFechaInvalido","de aplicacion de suero"));
	            }
            }
        }
        
        if (estado.equals("validar")) {
            
            String fechaAgresionTransformada=UtilidadFecha.conversionFormatoFechaABD(fechaAgresion);
            String fechaAplicacionTransformada=UtilidadFecha.conversionFormatoFechaABD(fechaAplicacion);
            String fechaUltimaDosisTransformada=UtilidadFecha.conversionFormatoFechaABD(fechaUltimaDosis);
            String fechaActual=UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual());
            String fechaConsultaTransformada=UtilidadFecha.conversionFormatoFechaABD(fechaConsultaGeneral);
            String fechaInicioSintomasTransformada=UtilidadFecha.conversionFormatoFechaABD(fechaInicioSint);
            String fechaHospitalizacionTransformada=UtilidadFecha.conversionFormatoFechaABD(fechaHospitalizacion);
            String fechaDefuncionTransformada=UtilidadFecha.conversionFormatoFechaABD(fechaDefuncion);
            String fechaAplicacionSueroTransformada=UtilidadFecha.conversionFormatoFechaABD(fechaAplicacionSuero);
            String fechaVacunaDosis1Transformada=UtilidadFecha.conversionFormatoFechaABD(fechaVacunaDosis1);
            String fechaVacunaDosis2Transformada=UtilidadFecha.conversionFormatoFechaABD(fechaVacunaDosis2);
            String fechaVacunaDosis3Transformada=UtilidadFecha.conversionFormatoFechaABD(fechaVacunaDosis3);
            String fechaVacunaDosis4Transformada=UtilidadFecha.conversionFormatoFechaABD(fechaVacunaDosis4);
            String fechaVacunaDosis5Transformada=UtilidadFecha.conversionFormatoFechaABD(fechaVacunaDosis5);
            
            /*
            if (sire.trim().length()==0) {
            	
            	errores.add("Valor de SIRE no valido", new ActionMessage("error.epidemiologia.sire"));
            }
            */
            
            if (cabeza==false&&manos==false&&tronco==false&&extsuperiores==false&&extinferiores==false) {
            	
            	errores.add("No se agregaron localizaciones anatomicas de la agresion", new ActionMessage("error.epidemiologia.localizacionAnatomicaAgresion"));
            }
            
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
            
            
            
            if (fechaAgresion.trim().length()>0) {
	            if (!UtilidadFecha.validarFecha(fechaAgresion)) {
	                
	                errores.add("Campo Fecha de Exposicion no valido", new ActionMessage("errors.formatoFechaInvalido","de Exposicion"));
	            }
	            else if (fechaAgresionTransformada.compareTo(fechaActual)>0) {
					
					errores.add("Campo Fecha de Exposicion no valido", new ActionMessage("errors.fechaPosteriorIgualActual","de Exposicion", "actual"));
				}
	            else if (fechaAgresionTransformada.compareTo(fechaInicioSintomasTransformada)>0) {
					
					errores.add("Campo Fecha de Exposicion no valido", new ActionMessage("errors.fechaPosteriorIgualAOtraDeReferencia","de Exposicion", "de Inicio de Sintomas"));
				}
            }
            else {
            	errores.add("El campo Fecha de Exposicion es Requerido", new ActionMessage("errors.required","El campo fecha de Exposicion"));
            }
            
            if (sueroAntirrabico==2&&fechaAplicacion.length()>0) {
	            if (!UtilidadFecha.validarFecha(fechaAplicacion)) {
	                
	                errores.add("Campo Fecha de Aplicacion de suero no valido", new ActionMessage("errors.formatoFechaInvalido","de Aplicacion"));
	            }
	            else if (fechaAplicacionTransformada.compareTo(fechaActual)>0) {
					
					errores.add("Campo Fecha de Aplicacion de suero no valido", new ActionMessage("errors.fechaPosteriorIgualActual","de Aplicacion", "actual"));
				}
            }
            
            
            if (vacunaAntirrabica==2&&fechaUltimaDosis.length()>0) {
	            if (!UtilidadFecha.validarFecha(fechaUltimaDosis)) {
	                
	                errores.add("Campo Fecha de ultima dosis de vacunacion no valido", new ActionMessage("errors.formatoFechaInvalido","de ultima dosis de vacunacion"));
	            }
	            else if (fechaUltimaDosisTransformada.compareTo(fechaActual)>0) {
					
					errores.add("Campo Fecha de ultima dosis de vacunacion no valido", new ActionMessage("errors.fechaPosteriorIgualActual","de ultima dosis de vacunacion", "actual"));
				}
            }
            
            
            if (aplicacionSuero) {
	            if (!UtilidadFecha.validarFecha(fechaAplicacionSuero)) {
	                
	                errores.add("Campo Fecha de aplicacion de suero no valido", new ActionMessage("errors.formatoFechaInvalido","de aplicacion de suero"));
	            }
	            else if (fechaAgresionTransformada.compareTo(fechaAplicacionSueroTransformada)>0) {
	            	
	            	errores.add("Fecha de Aplicacion de Suero debe ser Posterior o Igual a la Fecha de Exposicion", new ActionMessage("errors.fechaAnteriorIgualAOtraDeReferencia","de aplicacion de suero","de exposicion"));
	            }
            }
            
            if (aplicarVacuna) {
            	if (fechaVacunaDosis1.trim().length()>0) {
            		if (!UtilidadFecha.validarFecha(fechaVacunaDosis1)) {
            			
            			errores.add("Campo Fecha de aplicacion de vacuna (dosis 1) no valido", new ActionMessage("errors.formatoFechaInvalido","de aplicacion de vacuna - dosis 1"));
            		}
            		else if (fechaAgresionTransformada.compareTo(fechaVacunaDosis1Transformada)>0) {
            			
            			errores.add("Fecha de Aplicacion de Vacuna (dosis 1) debe ser Posterior o Igual a la Fecha de Exposicion", new ActionMessage("errors.fechaAnteriorIgualAOtraDeReferencia","de aplicacion de vacuna (dosis 1)","de exposicion"));
            		}
            	}
            	
            	if (fechaVacunaDosis2.trim().length()>0) {
            		if (!UtilidadFecha.validarFecha(fechaVacunaDosis2)) {
            			
            			errores.add("Campo Fecha de aplicacion de vacuna (dosis 2) no valido", new ActionMessage("errors.formatoFechaInvalido","de aplicacion de vacuna - dosis 2"));
            		}
            		else if (fechaAgresionTransformada.compareTo(fechaVacunaDosis2Transformada)>0) {
            			
            			errores.add("Fecha de Aplicacion de Vacuna (dosis 2) debe ser Posterior o Igual a la Fecha de Exposicion", new ActionMessage("errors.fechaAnteriorIgualAOtraDeReferencia","de aplicacion de vacuna (dosis 2)","de exposicion"));
            		}
            	}
            	
            	if (fechaVacunaDosis3.trim().length()>0) {
            		if (!UtilidadFecha.validarFecha(fechaVacunaDosis3)) {
            			
            			errores.add("Campo Fecha de aplicacion de vacuna (dosis 3) no valido", new ActionMessage("errors.formatoFechaInvalido","de aplicacion de vacuna - dosis 3"));
            		}
            		else if (fechaAgresionTransformada.compareTo(fechaVacunaDosis3Transformada)>0) {
            			
            			errores.add("Fecha de Aplicacion de Vacuna (dosis 3) debe ser Posterior o Igual a la Fecha de Exposicion", new ActionMessage("errors.fechaAnteriorIgualAOtraDeReferencia","de aplicacion de vacuna (dosis 3)","de exposicion"));
            		}
            	}
            	
            	if (fechaVacunaDosis4.trim().length()>0) {
            		if (!UtilidadFecha.validarFecha(fechaVacunaDosis4)) {
            			
            			errores.add("Campo Fecha de aplicacion de vacuna (dosis 4) no valido", new ActionMessage("errors.formatoFechaInvalido","de aplicacion de vacuna - dosis 4"));
            		}
            		else if (fechaAgresionTransformada.compareTo(fechaVacunaDosis4Transformada)>0) {
            			
            			errores.add("Fecha de Aplicacion de Vacuna (dosis 4) debe ser Posterior o Igual a la Fecha de Exposicion", new ActionMessage("errors.fechaAnteriorIgualAOtraDeReferencia","de aplicacion de vacuna (dosis 4)","de exposicion"));
            		}
            	}
            	
            	if (fechaVacunaDosis5.trim().length()>0) {
            		if (!UtilidadFecha.validarFecha(fechaVacunaDosis5)) {
            			
            			errores.add("Campo Fecha de aplicacion de vacuna (dosis 5) no valido", new ActionMessage("errors.formatoFechaInvalido","de aplicacion de vacuna - dosis 5"));
            		}
            		else if (fechaAgresionTransformada.compareTo(fechaVacunaDosis5Transformada)>0) {
            			
            			errores.add("Fecha de Aplicacion de Vacuna (dosis 5) debe ser Posterior o Igual a la Fecha de Exposicion", new ActionMessage("errors.fechaAnteriorIgualAOtraDeReferencia","de aplicacion de vacuna (dosis 5)","de exposicion"));
            		}
            	}
            }
        }
        
        if (estado.equals("guardarNuevo")) {
           
        }
        
        return errores;
    }
    
    
    
    
    
    
    /**
     * Metodo que inserta cada objeto al hashMap
     * @param key Identificador
     * @param valor Valor del objeto
     */
    /*
    public Object getLocalizacionAnatomica(String key) {
    
        Object retorno=null;
        
        try{
            retorno=localizacionesAnatomicas.get(key);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return retorno;
    }
    
    public void setLocalizacionAnatomica(String key, Object value) {
        
        this.localizacionesAnatomicas.put(key, value);
    }
    */
    /**
     * @return Returns the animal.
     */
    public Animal getAnimal() {
        return animal;
    }
    /**
     * @param animal The animal to set.
     */
    public void setAnimal(Animal animal) {
        this.animal = animal;
    }
    /**
     * @return Returns the aplicacionSuero.
     */
    public boolean isAplicacionSuero() {
        return aplicacionSuero;
    }
    /**
     * @param aplicacionSuero The aplicacionSuero to set.
     */
    public void setAplicacionSuero(boolean aplicacionSuero) {
        this.aplicacionSuero = aplicacionSuero;
    }
    
    /**
     * @return Returns the aplicarVacuna.
     */
    public boolean isAplicarVacuna() {
        return aplicarVacuna;
    }
    /**
     * @param aplicarVacuna The aplicarVacuna to set.
     */
    public void setAplicarVacuna(boolean aplicarVacuna) {
        this.aplicarVacuna = aplicarVacuna;
    }
    
    /**
     * @return Returns the cantidadAplicada.
     */
    public int getCantidadAplicada() {
        return cantidadAplicada;
    }
    /**
     * @param cantidadAplicada The cantidadAplicada to set.
     */
    public void setCantidadAplicada(int cantidadAplicada) {
        this.cantidadAplicada = cantidadAplicada;
    }
    
    /**
     * @return Returns the codigoDepartamentoAgresion.
     */
    public String getCodigoDepartamentoAgresion() {
        return codigoDepartamentoAgresion;
    }
    /**
     * @param codigoDepartamentoAgresion The codigoDepartamentoAgresion to set.
     */
    public void setCodigoDepartamentoAgresion(String codigoDepartamentoAgresion) {
        this.codigoDepartamentoAgresion = codigoDepartamentoAgresion;
    }
    /**
     * @return Returns the codigoDiagnostico.
     */
    public String getCodigoDiagnostico() {
        return codigoDiagnostico;
    }
    /**
     * @param codigoDiagnostico The codigoDiagnostico to set.
     */
    public void setCodigoDiagnostico(String codigoDiagnostico) {
        this.codigoDiagnostico = codigoDiagnostico;
    }
    /**
     * @return Returns the codigoFichaRabia.
     */
    public int getCodigoFichaRabia() {
        return codigoFichaRabia;
    }
    /**
     * @param codigoFichaRabia The codigoFichaRabia to set.
     */
    public void setCodigoFichaRabia(int codigoFichaRabia) {
        this.codigoFichaRabia = codigoFichaRabia;
    }
    /**
     * @return Returns the codigoMunicipioAgresion.
     */
    public String getCodigoMunicipioAgresion() {
        return codigoMunicipioAgresion;
    }
    /**
     * @param codigoMunicipioAgresion The codigoMunicipioAgresion to set.
     */
    public void setCodigoMunicipioAgresion(String codigoMunicipioAgresion) {
        this.codigoMunicipioAgresion = codigoMunicipioAgresion;
    }
    /**
     * @return Returns the codigoNotificacion.
     */
    public int getCodigoNotificacion() {
        return codigoNotificacion;
    }
    /**
     * @param codigoNotificacion The codigoNotificacion to set.
     */
    public void setCodigoNotificacion(int codigoNotificacion) {
        this.codigoNotificacion = codigoNotificacion;
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
     * @return Returns the direccion.
     */
    public String getDireccion() {
        return direccion;
    }
    /**
     * @param direccion The direccion to set.
     */
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
    
    /**
     * @return Returns the dosisAplicadas.
     */
    public int getDosisAplicadas() {
        return dosisAplicadas;
    }
    /**
     * @param dosisAplicadas The dosisAplicadas to set.
     */
    public void setDosisAplicadas(int dosisAplicadas) {
        this.dosisAplicadas = dosisAplicadas;
    }
    /**
     * @return Returns the dosisRabia.
     */
    public HashMap getDosisRabia() {
        return dosisRabia;
    }
    /**
     * @param dosisRabia The dosisRabia to set.
     */
    public void setDosisRabia(HashMap dosisRabia) {
        this.dosisRabia = dosisRabia;
    }
    /**
     * @return Returns the especie.
     */
    public int getEspecie() {
        return especie;
    }
    /**
     * @param especie The especie to set.
     */
    public void setEspecie(int especie) {
        this.especie = especie;
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
     * @return Returns the estadoFicha.
     */
    public int getEstadoFicha() {
        return estadoFicha;
    }
    /**
     * @param estadoFicha The estadoFicha to set.
     */
    public void setEstadoFicha(int estadoFicha) {
        this.estadoFicha = estadoFicha;
    }
    
    /**
     * @return Returns the fechaAgresion.
     */
    public String getFechaAgresion() {
        return fechaAgresion;
    }
    /**
     * @param fechaAgresion The fechaAgresion to set.
     */
    public void setFechaAgresion(String fechaAgresion) {
        this.fechaAgresion = fechaAgresion;
    }
    /**
     * @return Returns the fechaAplicacion.
     */
    public String getFechaAplicacion() {
        return fechaAplicacion;
    }
    /**
     * @param fechaAplicacion The fechaAplicacion to set.
     */
    public void setFechaAplicacion(String fechaAplicacion) {
        this.fechaAplicacion = fechaAplicacion;
    }
    /**
     * @return Returns the fechaConsulta.
     */
    public String getFechaConsulta() {
        return fechaConsulta;
    }
    /**
     * @param fechaConsulta The fechaConsulta to set.
     */
    public void setFechaConsulta(String fechaConsulta) {
        this.fechaConsulta = fechaConsulta;
    }
    
    /**
     * @return Returns the fechaUltimaDosis.
     */
    public String getFechaUltimaDosis() {
        return fechaUltimaDosis;
    }
    /**
     * @param fechaUltimaDosis The fechaUltimaDosis to set.
     */
    public void setFechaUltimaDosis(String fechaUltimaDosis) {
        this.fechaUltimaDosis = fechaUltimaDosis;
    }
    /**
     * @return Returns the fechaUltimaDosisAnimal.
     */
    public String getFechaUltimaDosisAnimal() {
        return fechaUltimaDosisAnimal;
    }
    /**
     * @param fechaUltimaDosisAnimal The fechaUltimaDosisAnimal to set.
     */
    public void setFechaUltimaDosisAnimal(String fechaUltimaDosisAnimal) {
        this.fechaUltimaDosisAnimal = fechaUltimaDosisAnimal;
    }
    /**
     * @return Returns the fuenteInformacionLaboratorio.
     */
    public int getFuenteInformacionLaboratorio() {
        return fuenteInformacionLaboratorio;
    }
    /**
     * @param fuenteInformacionLaboratorio The fuenteInformacionLaboratorio to set.
     */
    public void setFuenteInformacionLaboratorio(int fuenteInformacionLaboratorio) {
        this.fuenteInformacionLaboratorio = fuenteInformacionLaboratorio;
    }
    /**
     * @return Returns the lavadoHerida.
     */
    public boolean isLavadoHerida() {
        return lavadoHerida;
    }
    /**
     * @param lavadoHerida The lavadoHerida to set.
     */
    public void setLavadoHerida(boolean lavadoHerida) {
        this.lavadoHerida = lavadoHerida;
    }
    /**
     * @return Returns the localizacionesAnatomicas.
     */
    public HashMap getLocalizacionesAnatomicas() {
        return localizacionesAnatomicas;
    }
    /**
     * @param localizacionesAnatomicas The localizacionesAnatomicas to set.
     */
    public void setLocalizacionesAnatomicas(HashMap localizacionesAnatomicas) {
        this.localizacionesAnatomicas = localizacionesAnatomicas;
    }
   
    /**
     * @return Returns the logger.
     */
    public Logger getLogger() {
        return logger;
    }
    /**
     * @param logger The logger to set.
     */
    public void setLogger(Logger logger) {
        this.logger = logger;
    }
    /**
     * @return Returns the notificar.
     */
    public boolean isNotificar() {
        return notificar;
    }
    /**
     * @param notificar The notificar to set.
     */
    public void setNotificar(boolean notificar) {
        this.notificar = notificar;
    }
    /**
     * @return Returns the numeroDosisTratamiento.
     */
    public int getNumeroDosisTratamiento() {
        return numeroDosisTratamiento;
    }
    /**
     * @param numeroDosisTratamiento The numeroDosisTratamiento to set.
     */
    public void setNumeroDosisTratamiento(int numeroDosisTratamiento) {
        this.numeroDosisTratamiento = numeroDosisTratamiento;
    }
    /**
     * @return Returns the observacion.
     */
    public String getObservacion() {
        return observacion;
    }
    /**
     * @param observacion The observacion to set.
     */
    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }
    /**
     * @return Returns the procedencia.
     */
    public String getProcedencia() {
        return procedencia;
    }
    /**
     * @param procedencia The procedencia to set.
     */
    public void setProcedencia(String procedencia) {
        this.procedencia = procedencia;
    }
    /**
     * @return Returns the sueroAntirrabico.
     */
    public int getSueroAntirrabico() {
        return sueroAntirrabico;
    }
    /**
     * @param sueroAntirrabico The sueroAntirrabico to set.
     */
    public void setSueroAntirrabico(int sueroAntirrabico) {
        this.sueroAntirrabico = sueroAntirrabico;
    }
    /**
     * @return Returns the suturaHerida.
     */
    public boolean isSuturaHerida() {
        return suturaHerida;
    }
    /**
     * @param suturaHerida The suturaHerida to set.
     */
    public void setSuturaHerida(boolean suturaHerida) {
        this.suturaHerida = suturaHerida;
    }
    /**
     * @return Returns the tipoAgresion.
     */
    public int getTipoAgresion() {
        return tipoAgresion;
    }
    /**
     * @param tipoAgresion The tipoAgresion to set.
     */
    public void setTipoAgresion(int tipoAgresion) {
        this.tipoAgresion = tipoAgresion;
    }
    /**
     * @return Returns the tipoExposicion.
     */
    public int getTipoExposicion() {
        return tipoExposicion;
    }
    /**
     * @param tipoExposicion The tipoExposicion to set.
     */
    public void setTipoExposicion(int tipoExposicion) {
        this.tipoExposicion = tipoExposicion;
    }
    /**
     * @return Returns the tipoLesion.
     */
    public int getTipoLesion() {
        return tipoLesion;
    }
    /**
     * @param tipoLesion The tipoLesion to set.
     */
    public void setTipoLesion(int tipoLesion) {
        this.tipoLesion = tipoLesion;
    }
    /**
     * @return Returns the tipoSuero.
     */
    public int getTipoSuero() {
        return tipoSuero;
    }
    /**
     * @param tipoSuero The tipoSuero to set.
     */
    public void setTipoSuero(int tipoSuero) {
        this.tipoSuero = tipoSuero;
    }
    /**
     * @return Returns the tipoSueroTratamiento.
     */
    public int getTipoSueroTratamiento() {
        return tipoSueroTratamiento;
    }
    /**
     * @param tipoSueroTratamiento The tipoSueroTratamiento to set.
     */
    public void setTipoSueroTratamiento(int tipoSueroTratamiento) {
        this.tipoSueroTratamiento = tipoSueroTratamiento;
    }
    /**
     * @return Returns the tipoVacuna.
     */
    public int getTipoVacuna() {
        return tipoVacuna;
    }
    /**
     * @param tipoVacuna The tipoVacuna to set.
     */
    public void setTipoVacuna(int tipoVacuna) {
        this.tipoVacuna = tipoVacuna;
    }
    /**
     * @return Returns the tipoVacunaSeguimiento.
     */
    public int getTipoVacunaSeguimiento() {
        return tipoVacunaSeguimiento;
    }
    /**
     * @param tipoVacunaSeguimiento The tipoVacunaSeguimiento to set.
     */
    public void setTipoVacunaSeguimiento(int tipoVacunaSeguimiento) {
        this.tipoVacunaSeguimiento = tipoVacunaSeguimiento;
    }
    /**
     * @return Returns the tratamientoAntirrabico.
     */
    public TratamientoAntirrabico getTratamientoAntirrabico() {
        return tratamientoAntirrabico;
    }
    /**
     * @param tratamientoAntirrabico The tratamientoAntirrabico to set.
     */
    public void setTratamientoAntirrabico(
            TratamientoAntirrabico tratamientoAntirrabico) {
        this.tratamientoAntirrabico = tratamientoAntirrabico;
    }
    /**
     * @return Returns the vacunaAntirrabica.
     */
    public int getVacunaAntirrabica() {
        return vacunaAntirrabica;
    }
    /**
     * @param vacunaAntirrabica The vacunaAntirrabica to set.
     */
    public void setVacunaAntirrabica(int vacunaAntirrabica) {
        this.vacunaAntirrabica = vacunaAntirrabica;
    }
    /**
     * @return Returns the vacunado.
     */
    public boolean getVacunado() {
        return vacunado;
    }
    /**
     * @param vacunado The vacunado to set.
     */
    public void setVacunado(boolean vacunado) {
        this.vacunado = vacunado;
    }
    /**
     * @return Returns the ciudadNacimiento.
     */
    public String getCiudadNacimiento() {
        return ciudadNacimiento;
    }
    /**
     * @param ciudadNacimiento The ciudadNacimiento to set.
     */
    public void setCiudadNacimiento(String ciudadNacimiento) {
        this.ciudadNacimiento = ciudadNacimiento;
    }
    /**
     * @return Returns the ciudadResidencia.
     */
    public String getCiudadResidencia() {
        return ciudadResidencia;
    }
    /**
     * @param ciudadResidencia The ciudadResidencia to set.
     */
    public void setCiudadResidencia(String ciudadResidencia) {
        this.ciudadResidencia = ciudadResidencia;
    }
    /**
     * @return Returns the departamentoNacimiento.
     */
    public String getDepartamentoNacimiento() {
        return departamentoNacimiento;
    }
    /**
     * @param departamentoNacimiento The departamentoNacimiento to set.
     */
    public void setDepartamentoNacimiento(String departamentoNacimiento) {
        this.departamentoNacimiento = departamentoNacimiento;
    }
    /**
     * @return Returns the departamentoResidencia.
     */
    public String getDepartamentoResidencia() {
        return departamentoResidencia;
    }
    /**
     * @param departamentoResidencia The departamentoResidencia to set.
     */
    public void setDepartamentoResidencia(String departamentoResidencia) {
        this.departamentoResidencia = departamentoResidencia;
    }
    /**
     * @return Returns the direccionPaciente.
     */
    public String getDireccionPaciente() {
        return direccionPaciente;
    }
    /**
     * @param direccionPaciente The direccionPaciente to set.
     */
    public void setDireccionPaciente(String direccionPaciente) {
        this.direccionPaciente = direccionPaciente;
    }
    /**
     * @return Returns the documento.
     */
    public String getDocumento() {
        return documento;
    }
    /**
     * @param documento The documento to set.
     */
    public void setDocumento(String documento) {
        this.documento = documento;
    }
    /**
     * @return Returns the edad.
     */
    public String getEdad() {
        return edad;
    }
    /**
     * @param edad The edad to set.
     */
    public void setEdad(String edad) {
        this.edad = edad;
    }
    /**
     * @return Returns the estadoCivil.
     */
    public String getEstadoCivil() {
        return estadoCivil;
    }
    /**
     * @param estadoCivil The estadoCivil to set.
     */
    public void setEstadoCivil(String estadoCivil) {
        this.estadoCivil = estadoCivil;
    }
    /**
     * @return Returns the fechaNacimiento.
     */
    public String getFechaNacimiento() {
        return fechaNacimiento;
    }
    /**
     * @param fechaNacimiento The fechaNacimiento to set.
     */
    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }
    /**
     * @return Returns the genero.
     */
    public String getGenero() {
        return genero;
    }
    /**
     * @param genero The genero to set.
     */
    public void setGenero(String genero) {
        this.genero = genero;
    }
    /**
     * @return Returns the paciente.
     */
    public Paciente getPaciente() {
        return paciente;
    }
    /**
     * @param paciente The paciente to set.
     */
    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }
    /**
     * @return Returns the primerApellido.
     */
    public String getPrimerApellido() {
        return primerApellido;
    }
    /**
     * @param primerApellido The primerApellido to set.
     */
    public void setPrimerApellido(String primerApellido) {
        this.primerApellido = primerApellido;
    }
    /**
     * @return Returns the primerNombre.
     */
    public String getPrimerNombre() {
        return primerNombre;
    }
    /**
     * @param primerNombre The primerNombre to set.
     */
    public void setPrimerNombre(String primerNombre) {
        this.primerNombre = primerNombre;
    }
    /**
     * @return Returns the segundoApellido.
     */
    public String getSegundoApellido() {
        return segundoApellido;
    }
    /**
     * @param segundoApellido The segundoApellido to set.
     */
    public void setSegundoApellido(String segundoApellido) {
        this.segundoApellido = segundoApellido;
    }
    /**
     * @return Returns the segundoNombre.
     */
    public String getSegundoNombre() {
        return segundoNombre;
    }
    /**
     * @param segundoNombre The segundoNombre to set.
     */
    public void setSegundoNombre(String segundoNombre) {
        this.segundoNombre = segundoNombre;
    }
    /**
     * @return Returns the telefonoPaciente.
     */
    public String getTelefonoPaciente() {
        return telefonoPaciente;
    }
    /**
     * @param telefonoPaciente The telefonoPaciente to set.
     */
    public void setTelefonoPaciente(String telefonoPaciente) {
        this.telefonoPaciente = telefonoPaciente;
    }
    

	public boolean isCabeza() {
		return cabeza;
	}

	public void setCabeza(boolean cabeza) {
		this.cabeza = cabeza;
	}

	public boolean isExtinferiores() {
		return extinferiores;
	}

	public void setExtinferiores(boolean extinferiores) {
		this.extinferiores = extinferiores;
	}

	public boolean isExtsuperiores() {
		return extsuperiores;
	}

	public void setExtsuperiores(boolean extsuperiores) {
		this.extsuperiores = extsuperiores;
	}

	public boolean isManos() {
		return manos;
	}

	public void setManos(boolean manos) {
		this.manos = manos;
	}

	public boolean isTronco() {
		return tronco;
	}

	public void setTronco(boolean tronco) {
		this.tronco = tronco;
	}

	public String getLugarAgresion() {
		
		this.lugarAgresion = this.codigoMunicipioAgresion+"-"+this.codigoDepartamentoAgresion;
		
		
		return lugarAgresion;
	}

	public void setLugarAgresion(String lugarAgresion) {
		this.lugarAgresion = lugarAgresion;
		
		String codigoMun, codigoDep;
		
		codigoMun = lugarAgresion.split("-")[0];
		codigoDep = lugarAgresion.split("-")[1];
				
		this.codigoMunicipioAgresion = codigoMun;
		this.codigoDepartamentoAgresion = codigoDep;
		
	}

	public boolean isProvocada() {
		return provocada;
	}

	public void setProvocada(boolean provocada) {
		this.provocada = provocada;
	}

	public String getFechaInicioSintomas() {
		return fechaInicioSintomas;
	}

	public void setFechaInicioSintomas(String fechaInicioSintomas) {
		this.fechaInicioSintomas = fechaInicioSintomas;
	}

	public String getFechaMuerte() {
		return fechaMuerte;
	}

	public void setFechaMuerte(String fechaMuerte) {
		this.fechaMuerte = fechaMuerte;
	}

	public String getFechaTomaMuestra() {
		return fechaTomaMuestra;
	}

	public void setFechaTomaMuestra(String fechaTomaMuestra) {
		this.fechaTomaMuestra = fechaTomaMuestra;
	}

	public int getCantidadSueroGlutea() {
		return cantidadSueroGlutea;
	}

	public void setCantidadSueroGlutea(int cantidadSueroGlutea) {
		this.cantidadSueroGlutea = cantidadSueroGlutea;
	}

	public int getCantidadSueroHerida() {
		return cantidadSueroHerida;
	}

	public void setCantidadSueroHerida(int cantidadSueroHerida) {
		this.cantidadSueroHerida = cantidadSueroHerida;
	}

	public boolean isConfirmacionDiagnostica() {
		return confirmacionDiagnostica;
	}

	public void setConfirmacionDiagnostica(boolean confirmacionDiagnostica) {
		this.confirmacionDiagnostica = confirmacionDiagnostica;
	}

	public String getFechaAplicacionSuero() {
		return fechaAplicacionSuero;
	}

	public void setFechaAplicacionSuero(String fechaAplicacionSuero) {
		this.fechaAplicacionSuero = fechaAplicacionSuero;
	}

	public String getFechaTomaMuestraMuerte() {
		return fechaTomaMuestraMuerte;
	}

	public void setFechaTomaMuestraMuerte(String fechaTomaMuestraMuerte) {
		this.fechaTomaMuestraMuerte = fechaTomaMuestraMuerte;
	}

	public String getFechaVacunaDosis1() {
		return fechaVacunaDosis1;
	}

	public void setFechaVacunaDosis1(String fechaVacunaDosis1) {
		this.fechaVacunaDosis1 = fechaVacunaDosis1;
	}

	public String getFechaVacunaDosis2() {
		return fechaVacunaDosis2;
	}

	public void setFechaVacunaDosis2(String fechaVacunaDosis2) {
		this.fechaVacunaDosis2 = fechaVacunaDosis2;
	}

	public String getFechaVacunaDosis3() {
		return fechaVacunaDosis3;
	}

	public void setFechaVacunaDosis3(String fechaVacunaDosis3) {
		this.fechaVacunaDosis3 = fechaVacunaDosis3;
	}

	public String getFechaVacunaDosis4() {
		return fechaVacunaDosis4;
	}

	public void setFechaVacunaDosis4(String fechaVacunaDosis4) {
		this.fechaVacunaDosis4 = fechaVacunaDosis4;
	}

	public String getFechaVacunaDosis5() {
		return fechaVacunaDosis5;
	}

	public void setFechaVacunaDosis5(String fechaVacunaDosis5) {
		this.fechaVacunaDosis5 = fechaVacunaDosis5;
	}

	public String getLaboratorioProductor() {
		return laboratorioProductor;
	}

	public void setLaboratorioProductor(String laboratorioProductor) {
		this.laboratorioProductor = laboratorioProductor;
	}

	public String getNumeroLote() {
		return numeroLote;
	}

	public void setNumeroLote(String numeroLote) {
		this.numeroLote = numeroLote;
	}

	public int getPruebasLaboratorio() {
		return pruebasLaboratorio;
	}

	public void setPruebasLaboratorio(int pruebasLaboratorio) {
		this.pruebasLaboratorio = pruebasLaboratorio;
	}

	public int getRazonSuspension() {
		return razonSuspension;
	}

	public void setRazonSuspension(int razonSuspension) {
		this.razonSuspension = razonSuspension;
	}

	public boolean isSuspensionTratamiento() {
		return suspensionTratamiento;
	}

	public void setSuspensionTratamiento(boolean suspensionTratamiento) {
		this.suspensionTratamiento = suspensionTratamiento;
	}

	public int getTipoVacunaTratamiento() {
		return tipoVacunaTratamiento;
	}

	public void setTipoVacunaTratamiento(int tipoVacunaTratamiento) {
		this.tipoVacunaTratamiento = tipoVacunaTratamiento;
	}

	public String getSire() {
		return sire;
	}

	public void setSire(String sire) {
		this.sire = sire;
	}

	public boolean isEstaVivo() {
		return estaVivo;
	}

	public void setEstaVivo(boolean estaVivo) {
		this.estaVivo = estaVivo;
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

	public int getTipoCaso() {
		return tipoCaso;
	}

	public void setTipoCaso(int tipoCaso) {
		this.tipoCaso = tipoCaso;
	}

	public String getAseguradora() {
		return aseguradora;
	}

	public void setAseguradora(String aseguradora) {
		this.aseguradora = aseguradora;
	}

	public int getDesplazado() {
		return desplazado;
	}

	public void setDesplazado(int desplazado) {
		this.desplazado = desplazado;
	}

	public String getTipoId() {
		return tipoId;
	}

	public void setTipoId(String tipoId) {
		this.tipoId = tipoId;
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

	public String getLugarProcedencia() {
		return lugarProcedencia;
	}

	public void setLugarProcedencia(String lugarProcedencia) {
		this.lugarProcedencia = lugarProcedencia;
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

	public String getZonaDomicilio() {
		return zonaDomicilio;
	}

	public void setZonaDomicilio(String zonaDomicilio) {
		this.zonaDomicilio = zonaDomicilio;
	}

	public String getDepartamentoNotifica() {
		return departamentoNotifica;
	}

	public void setDepartamentoNotifica(String departamentoNotifica) {
		this.departamentoNotifica = departamentoNotifica;
	}

	public String getLugarNotifica() {
		return lugarNotifica;
	}

	public void setLugarNotifica(String lugarNotifica) {
		this.lugarNotifica = lugarNotifica;
	}

	public String getMunicipioNotifica() {
		return municipioNotifica;
	}

	public void setMunicipioNotifica(String municipioNotifica) {
		this.municipioNotifica = municipioNotifica;
	}

	public String getFechaNotificacion() {
		return fechaNotificacion;
	}

	public void setFechaNotificacion(String fechaNotificacion) {
		this.fechaNotificacion = fechaNotificacion;
	}

	public String getNombreProfesional() {
		return nombreProfesional;
	}

	public void setNombreProfesional(String nombreProfesional) {
		this.nombreProfesional = nombreProfesional;
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

	public int getCodigoEnfNotificable() {
		return codigoEnfNotificable;
	}

	public void setCodigoEnfNotificable(int codigoEnfNotificable) {
		this.codigoEnfNotificable = codigoEnfNotificable;
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

	public boolean isHayServicios() {
		return hayServicios;
	}

	public void setHayServicios(boolean hayServicios) {
		this.hayServicios = hayServicios;
	}

	public boolean isHayLaboratorios() {
		return hayLaboratorios;
	}

	public void setHayLaboratorios(boolean hayLaboratorios) {
		this.hayLaboratorios = hayLaboratorios;
	}

	public boolean isActiva() {
		return activa;
	}

	public void setActiva(boolean activa) {
		this.activa = activa;
	}

	public int getCodigoInstitucion() {
		return codigoInstitucion;
	}

	public void setCodigoInstitucion(int codigoInstitucion) {
		this.codigoInstitucion = codigoInstitucion;
	}

	public int getProfundidadLesion() {
		return profundidadLesion;
	}

	public void setProfundidadLesion(int profundidadLesion) {
		this.profundidadLesion = profundidadLesion;
	}

	public int getConfirmacionDiagnosticaAnimal() {
		return confirmacionDiagnosticaAnimal;
	}

	public void setConfirmacionDiagnosticaAnimal(int confirmacionDiagnosticaAnimal) {
		this.confirmacionDiagnosticaAnimal = confirmacionDiagnosticaAnimal;
	}

	public String getDireccionPropietario() {
		return direccionPropietario;
	}

	public void setDireccionPropietario(String direccionPropietario) {
		this.direccionPropietario = direccionPropietario;
	}

	public int getEstadoAnimalObserva() {
		return estadoAnimalObserva;
	}

	public void setEstadoAnimalObserva(int estadoAnimalObserva) {
		this.estadoAnimalObserva = estadoAnimalObserva;
	}

	public int getEstadoMomentoAgresion() {
		return estadoMomentoAgresion;
	}

	public void setEstadoMomentoAgresion(int estadoMomentoAgresion) {
		this.estadoMomentoAgresion = estadoMomentoAgresion;
	}

	public int getLugarObservacion() {
		return lugarObservacion;
	}

	public void setLugarObservacion(int lugarObservacion) {
		this.lugarObservacion = lugarObservacion;
	}

	public String getNombrePropietario() {
		return nombrePropietario;
	}

	public void setNombrePropietario(String nombrePropietario) {
		this.nombrePropietario = nombrePropietario;
	}

	public int getNumeroDiasObserva() {
		return numeroDiasObserva;
	}

	public void setNumeroDiasObserva(int numeroDiasObserva) {
		this.numeroDiasObserva = numeroDiasObserva;
	}

	public int getUbicacionAnimal() {
		return ubicacionAnimal;
	}

	public void setUbicacionAnimal(int ubicacionAnimal) {
		this.ubicacionAnimal = ubicacionAnimal;
	}

	public int getEvolucionPaciente() {
		return evolucionPaciente;
	}

	public void setEvolucionPaciente(int evolucionPaciente) {
		this.evolucionPaciente = evolucionPaciente;
	}

	public int getReaccionesVacunaSuero() {
		return reaccionesVacunaSuero;
	}

	public void setReaccionesVacunaSuero(int reaccionesVacunaSuero) {
		this.reaccionesVacunaSuero = reaccionesVacunaSuero;
	}

	public int getConfDiagnosticaCasoRabia() {
		return confDiagnosticaCasoRabia;
	}

	public void setConfDiagnosticaCasoRabia(int confDiagnosticaCasoRabia) {
		this.confDiagnosticaCasoRabia = confDiagnosticaCasoRabia;
	}

	public String getFechaMuestraCasoRabia() {
		return fechaMuestraCasoRabia;
	}

	public void setFechaMuestraCasoRabia(String fechaMuestraCasoRabia) {
		this.fechaMuestraCasoRabia = fechaMuestraCasoRabia;
	}

	public int getAreaProcedencia() {
		return areaProcedencia;
	}

	public void setAreaProcedencia(int areaProcedencia) {
		this.areaProcedencia = areaProcedencia;
	}

	public String getPais() {
		return pais;
	}

	public void setPais(String pais) {
		this.pais = pais;
	}

	public String getGrupoPoblacional() {
		return grupoPoblacional;
	}

	public void setGrupoPoblacional(String grupoPoblacional) {
		this.grupoPoblacional = grupoPoblacional;
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
