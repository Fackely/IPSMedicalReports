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

import com.sysmedica.mundo.saludpublica.Paciente;

public class FichaDengueForm extends ValidatorForm {

	private transient Logger logger=Logger.getLogger(FichaVIHForm.class);
	
	private boolean activa;
	private String urlFichasAnteriores;
	
	private int codigoInstitucion;
	
	private boolean hayServicios;
    private boolean hayLaboratorios;
		
	private String loginUsuario;
    private int codigoFichaDengue;
    private int codigoPaciente;
    private String codigoDiagnostico;
    private int codigoNotificacion;
    private int numeroSolicitud;
    private int estadoFicha;
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
    
	private int vacunaFiebreAmarilla;
	private String fechaAplicacionVacunaFiebre;
	private int vacunaHepatitisBDosis1;
	private int vacunaHepatitisBDosis2;
	private int vacunaHepatitisBDosis3;
	private String fechaVacunaHepaDosis1;
	private String fechaVacunaHepaDosis2;
	private String fechaVacunaHepaDosis3;
	private int vacunaHepatitisADosis1;
	private String fechaVacunaHepatADosis1;
	private String observaciones;
	private boolean desplazamiento;
	private String fechaDesplazamiento;
	private String lugarDesplazamiento;
	private String codigoMunicipio;
	private String codigoDepartamento;
	private int casoFiebreAmarilla;
	private int casoEpizootia;
	private String direccionSitio;
	private int presenciaAedes;
	private HashMap hallazgosSemiologicos;
	private HashMap datosLaboratorio;
	private String pais;
    private int areaProcedencia;
    private String grupoPoblacional;
	
	private String estado;
	
	//	 Variables para las valoraciones
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
		hallazgosSemiologicos = new HashMap();
    	datosLaboratorio = new HashMap();
    	vacunaFiebreAmarilla = 0;
    	fechaAplicacionVacunaFiebre = "";
    	vacunaHepatitisBDosis1 = 0;
    	vacunaHepatitisBDosis2 = 0;
    	vacunaHepatitisBDosis3 = 0;
    	fechaVacunaHepaDosis1 = "";
    	fechaVacunaHepaDosis2 = "";
    	fechaVacunaHepaDosis3 = "";
    	vacunaHepatitisADosis1 = 0;
    	fechaVacunaHepatADosis1 = "";
    	observaciones = "";
    	desplazamiento = false;
    	fechaDesplazamiento = "";
    	lugarDesplazamiento = "";
    	casoFiebreAmarilla = 0;
    	casoEpizootia = 0;
    	direccionSitio = "";
    	presenciaAedes = 0;
    	estaVivo = false;
    	
    	String ciudad = ValoresPorDefecto.getCiudadNacimiento(codigoInstitucion);
        String lugar = ciudad.split("-")[1]+"-"+ciudad.split("-")[0];
        
    	lugarNotifica = lugar;
        lugarProcedencia = lugar;
        
        fechaInicioSint = "";
        tipoCaso = 0;
        
        fechaConsultaGeneral = "";
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
        	
        	String fechaVacFiebreTransformada=UtilidadFecha.conversionFormatoFechaABD(fechaAplicacionVacunaFiebre);
        	String fechaVacHepDosis1Transformada=UtilidadFecha.conversionFormatoFechaABD(fechaVacunaHepaDosis1);
        	String fechaVacHepDosis2Transformada=UtilidadFecha.conversionFormatoFechaABD(fechaVacunaHepaDosis2);
        	String fechaVacHepDosis3Transformada=UtilidadFecha.conversionFormatoFechaABD(fechaVacunaHepaDosis3);
        	String fechaVacHepATransformada = UtilidadFecha.conversionFormatoFechaABD(fechaVacunaHepatADosis1);
        	String fechaDespTransformada = UtilidadFecha.conversionFormatoFechaABD(fechaDesplazamiento);
            String fechaInicioSintomasTransformada=UtilidadFecha.conversionFormatoFechaABD(fechaInicioSint);
            String fechaHospitalizacionTransformada=UtilidadFecha.conversionFormatoFechaABD(fechaHospitalizacion);
            String fechaDefuncionTransformada=UtilidadFecha.conversionFormatoFechaABD(fechaDefuncion);
        	
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
	        	
	        	if (fechaAplicacionVacunaFiebre.trim().length()>0) {
	            	
		            if (!UtilidadFecha.validarFecha(fechaAplicacionVacunaFiebre)) {
		                
		                errores.add("Campo Fecha de Aplicacion de Vacuna contra Fiebre Amarilla no valido", new ActionMessage("errors.formatoFechaInvalido","de Aplicacion de Vacuna contra Fiebre Amarilla"));
		            }
		            else if (fechaVacFiebreTransformada.compareTo(fechaActual)>0) {
						
						errores.add("Campo Fecha de Aplicacion de Vacuna contra Fiebre Amarilla no valido", new ActionMessage("errors.fechaPosteriorIgualActual","de Aplicacion de Vacuna contra Fiebre Amarilla", "actual"));
					}
	            }
	        	
	        	if (fechaVacunaHepaDosis1.trim().length()>0) {
	            	
		            if (!UtilidadFecha.validarFecha(fechaVacunaHepaDosis1)) {
		                
		                errores.add("Campo Fecha de Aplicacion de Primera Dosis de Vacuna contra Hepatitis B no valido", new ActionMessage("errors.formatoFechaInvalido","de Aplicacion de Primera Dosis de Vacuna contra Hepatitis B"));
		            }
		            else if (fechaVacHepDosis1Transformada.compareTo(fechaActual)>0) {
						
						errores.add("Campo Fecha de Aplicacion de Primera Dosis de Vacuna contra Hepatitis B no valido", new ActionMessage("errors.fechaPosteriorIgualActual","de Aplicacion de Primera Dosis de Vacuna contra Hepatitis B", "actual"));
					}
	            }
	        	
	        	if (fechaVacunaHepaDosis2.trim().length()>0) {
	            	
		            if (!UtilidadFecha.validarFecha(fechaVacunaHepaDosis2)) {
		                
		                errores.add("Campo Fecha de Aplicacion de Segunda Dosis de Vacuna contra Hepatitis B no valido", new ActionMessage("errors.formatoFechaInvalido","de Aplicacion de Segunda Dosis de Vacuna contra Hepatitis B"));
		            }
		            else if (fechaVacHepDosis2Transformada.compareTo(fechaActual)>0) {
						
						errores.add("Campo Fecha de Aplicacion de Segunda Dosis de Vacuna contra Hepatitis B no valido", new ActionMessage("errors.fechaPosteriorIgualActual","de Aplicacion de Segunda Dosis de Vacuna contra Hepatitis B", "actual"));
					}
	            }
	        	
	        	if (fechaVacunaHepaDosis3.trim().length()>0) {
	            	
		            if (!UtilidadFecha.validarFecha(fechaVacunaHepaDosis3)) {
		                
		                errores.add("Campo Fecha de Aplicacion de Tercera Dosis de Vacuna contra Hepatitis B no valido", new ActionMessage("errors.formatoFechaInvalido","de Aplicacion de Tercera Dosis de Vacuna contra Hepatitis B"));
		            }
		            else if (fechaVacHepDosis3Transformada.compareTo(fechaActual)>0) {
						
						errores.add("Campo Fecha de Aplicacion de Tercera Dosis de Vacuna contra Hepatitis B no valido", new ActionMessage("errors.fechaPosteriorIgualActual","de Aplicacion de Tercera Dosis de Vacuna contra Hepatitis B", "actual"));
					}
	            }
	        	
	        	if (fechaVacunaHepatADosis1.trim().length()>0) {
	            	
		            if (!UtilidadFecha.validarFecha(fechaVacunaHepatADosis1)) {
		                
		                errores.add("Campo Fecha de Aplicacion de Vacuna contra Hepatitis A no valido", new ActionMessage("errors.formatoFechaInvalido","de Aplicacion de Vacuna contra Hepatitis A"));
		            }
		            else if (fechaVacHepATransformada.compareTo(fechaActual)>0) {
						
						errores.add("Campo Fecha de Aplicacion de Vacuna contra Hepatitis A no valido", new ActionMessage("errors.fechaPosteriorIgualActual","de Aplicacion de Vacuna contra Hepatitis A", "actual"));
					}
	            }
	        	
	        	if (fechaDesplazamiento.trim().length()>0) {
	            	
		            if (!UtilidadFecha.validarFecha(fechaDesplazamiento)) {
		                
		                errores.add("Campo Fecha de Desplazamiento no valido", new ActionMessage("errors.formatoFechaInvalido","de Desplazamiento"));
		            }
		            else if (fechaDespTransformada.compareTo(fechaActual)>0) {
						
						errores.add("Campo Fecha de Desplazamiento no valido", new ActionMessage("errors.fechaPosteriorIgualActual","de Desplazamiento", "actual"));
					}
	            }
            }
            catch (NullPointerException npe) {}
        	
        	try {
	        	for (int i=1;i<6;i++) {
	            	
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
        	
        	String fechaVacFiebreTransformada=UtilidadFecha.conversionFormatoFechaABD(fechaAplicacionVacunaFiebre);
        	String fechaVacHepDosis1Transformada=UtilidadFecha.conversionFormatoFechaABD(fechaVacunaHepaDosis1);
        	String fechaVacHepDosis2Transformada=UtilidadFecha.conversionFormatoFechaABD(fechaVacunaHepaDosis2);
        	String fechaVacHepDosis3Transformada=UtilidadFecha.conversionFormatoFechaABD(fechaVacunaHepaDosis3);
        	String fechaVacHepATransformada = UtilidadFecha.conversionFormatoFechaABD(fechaVacunaHepatADosis1);
        	String fechaDespTransformada = UtilidadFecha.conversionFormatoFechaABD(fechaDesplazamiento);
            String fechaInicioSintomasTransformada=UtilidadFecha.conversionFormatoFechaABD(fechaInicioSint);
            String fechaHospitalizacionTransformada=UtilidadFecha.conversionFormatoFechaABD(fechaHospitalizacion);
            String fechaDefuncionTransformada=UtilidadFecha.conversionFormatoFechaABD(fechaDefuncion);
        	
        	String fechaActual=UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual());
        	
        	boolean hayHallazgos = false;
        	
        	try {
	        	for (int i=1;i<hallazgosSemiologicos.size()+1;i++) {
	        		
	        		String val = hallazgosSemiologicos.get("hallazgo_"+i).toString();
	        		
	        		if (val.equals("true")) {
	        			
	        			hayHallazgos=true;
	        			break;
	        		}
	        	}
	        	
	        	if (!hayHallazgos) {
	        		
	        		errores.add("No se agregaron hallazgos semiologicos", new ActionMessage("error.epidemiologia.hallazgos"));
	        	}
        	}
        	catch (NullPointerException npe) {}
        	
        	
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
	        	
	        	if (fechaAplicacionVacunaFiebre.trim().length()>0) {
	            	
		            if (!UtilidadFecha.validarFecha(fechaAplicacionVacunaFiebre)) {
		                
		                errores.add("Campo Fecha de Aplicacion de Vacuna contra Fiebre Amarilla no valido", new ActionMessage("errors.formatoFechaInvalido","de Aplicacion de Vacuna contra Fiebre Amarilla"));
		            }
		            else if (fechaVacFiebreTransformada.compareTo(fechaActual)>0) {
						
						errores.add("Campo Fecha de Aplicacion de Vacuna contra Fiebre Amarilla no valido", new ActionMessage("errors.fechaPosteriorIgualActual","de Aplicacion de Vacuna contra Fiebre Amarilla", "actual"));
					}
	            }
	        	
	        	if (fechaVacunaHepaDosis1.trim().length()>0) {
	            	
		            if (!UtilidadFecha.validarFecha(fechaVacunaHepaDosis1)) {
		                
		                errores.add("Campo Fecha de Aplicacion de Primera Dosis de Vacuna contra Hepatitis B no valido", new ActionMessage("errors.formatoFechaInvalido","de Aplicacion de Primera Dosis de Vacuna contra Hepatitis B"));
		            }
		            else if (fechaVacHepDosis1Transformada.compareTo(fechaActual)>0) {
						
						errores.add("Campo Fecha de Aplicacion de Primera Dosis de Vacuna contra Hepatitis B no valido", new ActionMessage("errors.fechaPosteriorIgualActual","de Aplicacion de Primera Dosis de Vacuna contra Hepatitis B", "actual"));
					}
	            }
	        	
	        	if (fechaVacunaHepaDosis2.trim().length()>0) {
	            	
		            if (!UtilidadFecha.validarFecha(fechaVacunaHepaDosis2)) {
		                
		                errores.add("Campo Fecha de Aplicacion de Segunda Dosis de Vacuna contra Hepatitis B no valido", new ActionMessage("errors.formatoFechaInvalido","de Aplicacion de Segunda Dosis de Vacuna contra Hepatitis B"));
		            }
		            else if (fechaVacHepDosis2Transformada.compareTo(fechaActual)>0) {
						
						errores.add("Campo Fecha de Aplicacion de Segunda Dosis de Vacuna contra Hepatitis B no valido", new ActionMessage("errors.fechaPosteriorIgualActual","de Aplicacion de Segunda Dosis de Vacuna contra Hepatitis B", "actual"));
					}
	            }
	        	
	        	if (fechaVacunaHepaDosis3.trim().length()>0) {
	            	
		            if (!UtilidadFecha.validarFecha(fechaVacunaHepaDosis3)) {
		                
		                errores.add("Campo Fecha de Aplicacion de Tercera Dosis de Vacuna contra Hepatitis B no valido", new ActionMessage("errors.formatoFechaInvalido","de Aplicacion de Tercera Dosis de Vacuna contra Hepatitis B"));
		            }
		            else if (fechaVacHepDosis3Transformada.compareTo(fechaActual)>0) {
						
						errores.add("Campo Fecha de Aplicacion de Tercera Dosis de Vacuna contra Hepatitis B no valido", new ActionMessage("errors.fechaPosteriorIgualActual","de Aplicacion de Tercera Dosis de Vacuna contra Hepatitis B", "actual"));
					}
	            }
	        	
	        	if (fechaVacunaHepatADosis1.trim().length()>0) {
	            	
		            if (!UtilidadFecha.validarFecha(fechaVacunaHepatADosis1)) {
		                
		                errores.add("Campo Fecha de Aplicacion de Vacuna contra Hepatitis A no valido", new ActionMessage("errors.formatoFechaInvalido","de Aplicacion de Vacuna contra Hepatitis A"));
		            }
		            else if (fechaVacHepATransformada.compareTo(fechaActual)>0) {
						
						errores.add("Campo Fecha de Aplicacion de Vacuna contra Hepatitis A no valido", new ActionMessage("errors.fechaPosteriorIgualActual","de Aplicacion de Vacuna contra Hepatitis A", "actual"));
					}
	            }
	        	
	        	if (fechaDesplazamiento.trim().length()>0) {
	            	
		            if (!UtilidadFecha.validarFecha(fechaDesplazamiento)) {
		                
		                errores.add("Campo Fecha de Desplazamiento no valido", new ActionMessage("errors.formatoFechaInvalido","de Desplazamiento"));
		            }
		            else if (fechaDespTransformada.compareTo(fechaActual)>0) {
						
						errores.add("Campo Fecha de Desplazamiento no valido", new ActionMessage("errors.fechaPosteriorIgualActual","de Desplazamiento", "actual"));
					}
	            }
            }
            catch (NullPointerException npe) {}
	   /*     	
            boolean noHayDatosLab = false;
            
        	for (int i=1;i<6;i++) {
            	
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
        	
        	if (!noHayDatosLab) {
            	errores.add("No se agregaron datos de laboratorio. Guarde la ficha como Pendiente.", new ActionMessage("error.epidemiologia.nohaylaboratorios"));
            }
       */
        }
        
        return errores;
    }


	public int getCasoEpizootia() {
		return casoEpizootia;
	}


	public void setCasoEpizootia(int casoEpizootia) {
		this.casoEpizootia = casoEpizootia;
	}


	public int getCasoFiebreAmarilla() {
		return casoFiebreAmarilla;
	}


	public void setCasoFiebreAmarilla(int casoFiebreAmarilla) {
		this.casoFiebreAmarilla = casoFiebreAmarilla;
	}


	public String getCodigoDepartamento() {
		return codigoDepartamento;
	}


	public void setCodigoDepartamento(String codigoDepartamento) {
		this.codigoDepartamento = codigoDepartamento;
	}


	public String getCodigoMunicipio() {
		return codigoMunicipio;
	}


	public void setCodigoMunicipio(String codigoMunicipio) {
		this.codigoMunicipio = codigoMunicipio;
	}


	public HashMap getDatosLaboratorio() {
		return datosLaboratorio;
	}


	public void setDatosLaboratorio(HashMap datosLaboratorio) {
		this.datosLaboratorio = datosLaboratorio;
	}


	public boolean isDesplazamiento() {
		return desplazamiento;
	}


	public void setDesplazamiento(boolean desplazamiento) {
		this.desplazamiento = desplazamiento;
	}


	public String getDireccionSitio() {
		return direccionSitio;
	}


	public void setDireccionSitio(String direccionSitio) {
		this.direccionSitio = direccionSitio;
	}


	public String getFechaAplicacionVacunaFiebre() {
		return fechaAplicacionVacunaFiebre;
	}


	public void setFechaAplicacionVacunaFiebre(String fechaAplicacionVacunaFiebre) {
		this.fechaAplicacionVacunaFiebre = fechaAplicacionVacunaFiebre;
	}


	public String getFechaDesplazamiento() {
		return fechaDesplazamiento;
	}


	public void setFechaDesplazamiento(String fechaDesplazamiento) {
		this.fechaDesplazamiento = fechaDesplazamiento;
	}


	public String getFechaVacunaHepaDosis1() {
		return fechaVacunaHepaDosis1;
	}


	public void setFechaVacunaHepaDosis1(String fechaVacunaHepaDosis1) {
		this.fechaVacunaHepaDosis1 = fechaVacunaHepaDosis1;
	}


	public String getFechaVacunaHepaDosis2() {
		return fechaVacunaHepaDosis2;
	}


	public void setFechaVacunaHepaDosis2(String fechaVacunaHepaDosis2) {
		this.fechaVacunaHepaDosis2 = fechaVacunaHepaDosis2;
	}


	public String getFechaVacunaHepaDosis3() {
		return fechaVacunaHepaDosis3;
	}


	public void setFechaVacunaHepaDosis3(String fechaVacunaHepaDosis3) {
		this.fechaVacunaHepaDosis3 = fechaVacunaHepaDosis3;
	}


	public String getFechaVacunaHepatADosis1() {
		return fechaVacunaHepatADosis1;
	}


	public void setFechaVacunaHepatADosis1(String fechaVacunaHepatADosis1) {
		this.fechaVacunaHepatADosis1 = fechaVacunaHepatADosis1;
	}


	public HashMap getHallazgosSemiologicos() {
		return hallazgosSemiologicos;
	}


	public void setHallazgosSemiologicos(HashMap hallazgosSemiologicos) {
		this.hallazgosSemiologicos = hallazgosSemiologicos;
	}


	public String getLugarDesplazamiento() {
		return lugarDesplazamiento;
	}


	public void setLugarDesplazamiento(String lugarDesplazamiento) {
		this.lugarDesplazamiento = lugarDesplazamiento;
	}


	public String getObservaciones() {
		return observaciones;
	}


	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}


	public int getPresenciaAedes() {
		return presenciaAedes;
	}


	public void setPresenciaAedes(int presenciaAedes) {
		this.presenciaAedes = presenciaAedes;
	}


	public String getSire() {
		return sire;
	}


	public void setSire(String sire) {
		this.sire = sire;
	}


	public int getVacunaFiebreAmarilla() {
		return vacunaFiebreAmarilla;
	}


	public void setVacunaFiebreAmarilla(int vacunaFiebreAmarilla) {
		this.vacunaFiebreAmarilla = vacunaFiebreAmarilla;
	}


	public int getVacunaHepatitisADosis1() {
		return vacunaHepatitisADosis1;
	}


	public void setVacunaHepatitisADosis1(int vacunaHepatitisADosis1) {
		this.vacunaHepatitisADosis1 = vacunaHepatitisADosis1;
	}


	public int getVacunaHepatitisBDosis1() {
		return vacunaHepatitisBDosis1;
	}


	public void setVacunaHepatitisBDosis1(int vacunaHepatitisBDosis1) {
		this.vacunaHepatitisBDosis1 = vacunaHepatitisBDosis1;
	}


	public int getVacunaHepatitisBDosis2() {
		return vacunaHepatitisBDosis2;
	}


	public void setVacunaHepatitisBDosis2(int vacunaHepatitisBDosis2) {
		this.vacunaHepatitisBDosis2 = vacunaHepatitisBDosis2;
	}


	public int getVacunaHepatitisBDosis3() {
		return vacunaHepatitisBDosis3;
	}


	public void setVacunaHepatitisBDosis3(int vacunaHepatitisBDosis3) {
		this.vacunaHepatitisBDosis3 = vacunaHepatitisBDosis3;
	}


	public int getEstadoFicha() {
		return estadoFicha;
	}


	public void setEstadoFicha(int estadoFicha) {
		this.estadoFicha = estadoFicha;
	}


	public int getCodigoFichaDengue() {
		return codigoFichaDengue;
	}


	public void setCodigoFichaDengue(int codigoFichaDengue) {
		this.codigoFichaDengue = codigoFichaDengue;
	}


	public String getEstado() {
		return estado;
	}


	public void setEstado(String estado) {
		this.estado = estado;
	}


	public String getCodigoDiagnostico() {
		return codigoDiagnostico;
	}


	public void setCodigoDiagnostico(String codigoDiagnostico) {
		this.codigoDiagnostico = codigoDiagnostico;
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


	public String getLoginUsuario() {
		return loginUsuario;
	}


	public void setLoginUsuario(String loginUsuario) {
		this.loginUsuario = loginUsuario;
	}


	public int getNumeroSolicitud() {
		return numeroSolicitud;
	}


	public void setNumeroSolicitud(int numeroSolicitud) {
		this.numeroSolicitud = numeroSolicitud;
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


	public Logger getLogger() {
		return logger;
	}


	public void setLogger(Logger logger) {
		this.logger = logger;
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
