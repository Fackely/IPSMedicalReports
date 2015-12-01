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

public class FichaSifilisForm extends ValidatorForm {

	private boolean activa;
	private String urlFichasAnteriores;
	
	private int codigoInstitucion;
	
	private boolean hayServicios;
    private boolean hayLaboratorios;
    
	private boolean notificar;
    private String loginUsuario;
    private int codigoFichaSifilis;
    private int codigoPaciente;
    private String codigoDiagnostico;
    private int codigoNotificacion;
    private int numeroSolicitud;
    private int estadoFicha;
    
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
    
    //**************************************
    // DATOS CARA B :
    
	private boolean controlPrenatal;
	private int edadGestacional;
	private int numeroControles;
	private int edadGestacionalSero1;
	private int edadGestacionalDiag1;
	private int edadGestacionalTrat;
	private int edadGestacionalParto;
	private int estadoNacimiento;
	private int recienNacido;
	private int lugarAtencionParto;
	private boolean recibioTratamiento;
	private String medicamentoAdmin;
	private int dosisAplicadas;
	private boolean tratamientoHospitalario;
	private boolean tratamientoAmbulatorio;
	private boolean diagnosticoContactos;
	private boolean tratamientoContactos;
	private boolean diagnosticoIts;
	private String cualesIts;
	private String observaciones;
	private int condicionMomentoDx;
	private int tipoTratamiento;
	private int otrasIts;
	private int esquemaCompleto;
	private int alergiaPenicilina;
	private int desensibilizaPenicilina;
	private String pais;
    private int areaProcedencia;
    private String grupoPoblacional;
	
	private HashMap signosRecienNacido;
	private HashMap datosLaboratorioSifilis;
	
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
    	controlPrenatal = false;
    	estadoNacimiento = 0;
    	recienNacido = 0;
    	lugarAtencionParto = 0;
    	medicamentoAdmin = "";
    	cualesIts = "";
    	observaciones = "";
    	
    	signosRecienNacido = new HashMap();
    	datosLaboratorioSifilis = new HashMap();
    	estaVivo = false;
    	
    	String ciudad = ValoresPorDefecto.getCiudadNacimiento(codigoInstitucion);
        String lugar = ciudad.split("-")[1]+"-"+ciudad.split("-")[0];
        
    	lugarNotifica = lugar;
        lugarProcedencia = lugar;
        
        fechaInicioSint = "";
        tipoCaso = 0;
        
    	edadGestacional = 0;
    	numeroControles = 0;
    	edadGestacionalSero1 = 0;
    	edadGestacionalDiag1 = 0;
    	edadGestacionalTrat = 0;
    	edadGestacionalParto = 0;
    	estadoNacimiento = 0;
    	recienNacido = 0;
    	lugarAtencionParto = 0;
    	recibioTratamiento = false;
    	medicamentoAdmin = "";
    	dosisAplicadas = 0;
    	tratamientoHospitalario = false;
    	tratamientoAmbulatorio = false;
    	diagnosticoContactos = false;
    	tratamientoContactos = false;
    	diagnosticoIts = false;
    	cualesIts = "";
    	observaciones = "";
    	
    	fechaConsultaGeneral = "";
        fechaInicioSint = "";
        tipoCaso = 0;
        hospitalizado = false;
        fechaHospitalizacion = "";
        estaVivo = false;
        fechaDefuncion = "";
        nombreProfesional = "";
        
        condicionMomentoDx = 0;
    	tipoTratamiento = 0;
    	otrasIts = 0;
    	esquemaCompleto = 0;
    	alergiaPenicilina = 0;
    	desensibilizaPenicilina = 0;
    	
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
        	
        	String fechaActual=UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual());
        	
            String fechaInicioSintomasTransformada=UtilidadFecha.conversionFormatoFechaABD(fechaInicioSint);
            String fechaHospitalizacionTransformada=UtilidadFecha.conversionFormatoFechaABD(fechaHospitalizacion);
            String fechaDefuncionTransformada=UtilidadFecha.conversionFormatoFechaABD(fechaDefuncion);
        	
            /*
            if (fechaInicioSint.trim().length()>0) {
            	
            	if (!UtilidadFecha.validarFecha(fechaInicioSint)) {
	                
	                errores.add("Campo Fecha de Inicio de Sintomas no valido", new ActionMessage("errors.formatoFechaInvalido","de Inicio de Sintomas"));
	            }
	            else if (fechaInicioSintomasTransformada.compareTo(fechaActual)>0) {
					
					errores.add("Campo Fecha de Inicio de Sintomas no valido", new ActionMessage("errors.fechaPosteriorIgualActual","de Inicio de Sintomas", "actual"));
				}
            }
            
            boolean haySignos = false;
        	
        	try {
	        	for (int i=1;i<signosRecienNacido.size()+1;i++) {
	        		
	        		String val = signosRecienNacido.get("signo_"+i).toString();
	        		
	        		if (val.equals("true")) {
	        			
	        			haySignos=true;
	        			break;
	        		}
	        	}
	        	
	        	if (!haySignos) {
	        		
	        		errores.add("No se agregaron signos de recien nacido", new ActionMessage("error.epidemiologia.signos"));
	        	}
        	}
        	catch (NullPointerException npe) {}
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
            
            
	        	for (int i=1;i<5;i++) {
	            	
	            	String fechaToma = datosLaboratorioSifilis.get("fechaToma"+i).toString();
	            	String fechaTomaTransformada = UtilidadFecha.conversionFormatoFechaABD(fechaToma);
	            	
	            	if (fechaToma.trim().length()>0) {
	                	
	    	            if (!UtilidadFecha.validarFecha(fechaToma)) {
	    	                
	    	                errores.add("Campo Fecha de Toma de Examen de Laboratorio "+Integer.toString(i)+" no valido", new ActionMessage("errors.formatoFechaInvalido","de Toma de Examen de Laboratorio "+Integer.toString(i)));
	    	            }
	    	            else if (fechaTomaTransformada.compareTo(fechaActual)>0) {
	    					
	    					errores.add("Campo Fecha de Toma de Examen de Laboratorio "+Integer.toString(i)+" no valido", new ActionMessage("errors.fechaPosteriorIgualActual","de Toma de Examen de Laboratorio "+Integer.toString(i), "actual"));
	    				}
	                }
	            	
	            	
	            	String fechaRecepcion = datosLaboratorioSifilis.get("fechaRecepcion"+i).toString();
	            	String fechaRecepcionTransformada = UtilidadFecha.conversionFormatoFechaABD(fechaRecepcion);
	            	
	            	if (fechaRecepcion.trim().length()>0) {
	                	
	    	            if (!UtilidadFecha.validarFecha(fechaRecepcion)) {
	    	                
	    	                errores.add("Campo Fecha de Recepcion de Examen de Laboratorio "+Integer.toString(i)+" no valido", new ActionMessage("errors.formatoFechaInvalido","de Recepcion de Examen de Laboratorio "+Integer.toString(i)));
	    	            }
	    	            else if (fechaRecepcionTransformada.compareTo(fechaActual)>0) {
	    					
	    					errores.add("Campo Fecha de Recepcion de Examen de Laboratorio "+Integer.toString(i)+" no valido", new ActionMessage("errors.fechaPosteriorIgualActual","de Recepcion de Examen de Laboratorio "+Integer.toString(i), "actual"));
	    				}
	                }
	            	
	            	
	            	String fechaResultado = datosLaboratorioSifilis.get("fechaResultado"+i).toString();
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
            catch (NullPointerException npe) {}
        }
        	
        
        if (estado.equals("validar")) {
        	
        	String fechaActual=UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual());
        	
            String fechaInicioSintomasTransformada=UtilidadFecha.conversionFormatoFechaABD(fechaInicioSint);
            String fechaHospitalizacionTransformada=UtilidadFecha.conversionFormatoFechaABD(fechaHospitalizacion);
            String fechaDefuncionTransformada=UtilidadFecha.conversionFormatoFechaABD(fechaDefuncion);
        	
            /*
        	boolean haySignos = false;
        	
        	try {
	        	for (int i=1;i<signosRecienNacido.size()+1;i++) {
	        		
	        		String val = signosRecienNacido.get("signo_"+i).toString();
	        		
	        		if (val.equals("true")) {
	        			
	        			haySignos=true;
	        			break;
	        		}
	        	}
	        	
	        	if (!haySignos) {
	        		
	        		errores.add("No se agregaron signos de recien nacido", new ActionMessage("error.epidemiologia.signos"));
	        	}
        	}
        	catch (NullPointerException npe) {}
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
	        	
	            boolean noHayDatosLab = false;
	            
	        	for (int i=1;i<5;i++) {
	            	
	            	String fechaToma = datosLaboratorioSifilis.get("fechaToma"+i).toString();
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
	            	
	            	
	            	String fechaRecepcion = datosLaboratorioSifilis.get("fechaRecepcion"+i).toString();
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
	            	
	            	
	            	String fechaResultado = datosLaboratorioSifilis.get("fechaResultado"+i).toString();
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
            }
            catch (NullPointerException npe) {}
        }
        
        return errores;
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


	public String getCodigoDiagnostico() {
		return codigoDiagnostico;
	}


	public void setCodigoDiagnostico(String codigoDiagnostico) {
		this.codigoDiagnostico = codigoDiagnostico;
	}


	public int getCodigoFichaSifilis() {
		return codigoFichaSifilis;
	}


	public void setCodigoFichaSifilis(int codigoFichaSifilis) {
		this.codigoFichaSifilis = codigoFichaSifilis;
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


	public boolean isControlPrenatal() {
		return controlPrenatal;
	}


	public void setControlPrenatal(boolean controlPrenatal) {
		this.controlPrenatal = controlPrenatal;
	}


	public String getCualesIts() {
		return cualesIts;
	}


	public void setCualesIts(String cualesIts) {
		this.cualesIts = cualesIts;
	}


	public HashMap getDatosLaboratorioSifilis() {
		return datosLaboratorioSifilis;
	}


	public void setDatosLaboratorioSifilis(HashMap datosLaboratorioSifilis) {
		this.datosLaboratorioSifilis = datosLaboratorioSifilis;
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


	public boolean isDiagnosticoContactos() {
		return diagnosticoContactos;
	}


	public void setDiagnosticoContactos(boolean diagnosticoContactos) {
		this.diagnosticoContactos = diagnosticoContactos;
	}


	public boolean isDiagnosticoIts() {
		return diagnosticoIts;
	}


	public void setDiagnosticoIts(boolean diagnosticoIts) {
		this.diagnosticoIts = diagnosticoIts;
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


	public int getDosisAplicadas() {
		return dosisAplicadas;
	}


	public void setDosisAplicadas(int dosisAplicadas) {
		this.dosisAplicadas = dosisAplicadas;
	}


	public String getEdad() {
		return edad;
	}


	public void setEdad(String edad) {
		this.edad = edad;
	}


	public int getEdadGestacional() {
		return edadGestacional;
	}


	public void setEdadGestacional(int edadGestacional) {
		this.edadGestacional = edadGestacional;
	}


	public int getEdadGestacionalDiag1() {
		return edadGestacionalDiag1;
	}


	public void setEdadGestacionalDiag1(int edadGestacionalDiag1) {
		this.edadGestacionalDiag1 = edadGestacionalDiag1;
	}


	public int getEdadGestacionalParto() {
		return edadGestacionalParto;
	}


	public void setEdadGestacionalParto(int edadGestacionalParto) {
		this.edadGestacionalParto = edadGestacionalParto;
	}


	public int getEdadGestacionalSero1() {
		return edadGestacionalSero1;
	}


	public void setEdadGestacionalSero1(int edadGestacionalSero1) {
		this.edadGestacionalSero1 = edadGestacionalSero1;
	}


	public int getEdadGestacionalTrat() {
		return edadGestacionalTrat;
	}


	public void setEdadGestacionalTrat(int edadGestacionalTrat) {
		this.edadGestacionalTrat = edadGestacionalTrat;
	}


	public String getEstado() {
		return estado;
	}


	public void setEstado(String estado) {
		this.estado = estado;
	}


	public String getEstadoCivil() {
		return estadoCivil;
	}


	public void setEstadoCivil(String estadoCivil) {
		this.estadoCivil = estadoCivil;
	}


	public int getEstadoFicha() {
		return estadoFicha;
	}


	public void setEstadoFicha(int estadoFicha) {
		this.estadoFicha = estadoFicha;
	}


	public int getEstadoNacimiento() {
		return estadoNacimiento;
	}


	public void setEstadoNacimiento(int estadoNacimiento) {
		this.estadoNacimiento = estadoNacimiento;
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


	public String getLoginUsuario() {
		return loginUsuario;
	}


	public void setLoginUsuario(String loginUsuario) {
		this.loginUsuario = loginUsuario;
	}


	public int getLugarAtencionParto() {
		return lugarAtencionParto;
	}


	public void setLugarAtencionParto(int lugarAtencionParto) {
		this.lugarAtencionParto = lugarAtencionParto;
	}


	public String getMedicamentoAdmin() {
		return medicamentoAdmin;
	}


	public void setMedicamentoAdmin(String medicamentoAdmin) {
		this.medicamentoAdmin = medicamentoAdmin;
	}


	public boolean isNotificar() {
		return notificar;
	}


	public void setNotificar(boolean notificar) {
		this.notificar = notificar;
	}


	public int getNumeroControles() {
		return numeroControles;
	}


	public void setNumeroControles(int numeroControles) {
		this.numeroControles = numeroControles;
	}


	public int getNumeroSolicitud() {
		return numeroSolicitud;
	}


	public void setNumeroSolicitud(int numeroSolicitud) {
		this.numeroSolicitud = numeroSolicitud;
	}


	public String getObservaciones() {
		return observaciones;
	}


	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
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


	public boolean isRecibioTratamiento() {
		return recibioTratamiento;
	}


	public void setRecibioTratamiento(boolean recibioTratamiento) {
		this.recibioTratamiento = recibioTratamiento;
	}


	public int getRecienNacido() {
		return recienNacido;
	}


	public void setRecienNacido(int recienNacido) {
		this.recienNacido = recienNacido;
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


	public HashMap getSignosRecienNacido() {
		return signosRecienNacido;
	}


	public void setSignosRecienNacido(HashMap signosRecienNacido) {
		this.signosRecienNacido = signosRecienNacido;
	}


	public String getTelefonoPaciente() {
		return telefonoPaciente;
	}


	public void setTelefonoPaciente(String telefonoPaciente) {
		this.telefonoPaciente = telefonoPaciente;
	}


	public boolean isTratamientoAmbulatorio() {
		return tratamientoAmbulatorio;
	}


	public void setTratamientoAmbulatorio(boolean tratamientoAmbulatorio) {
		this.tratamientoAmbulatorio = tratamientoAmbulatorio;
	}


	public boolean isTratamientoContactos() {
		return tratamientoContactos;
	}


	public void setTratamientoContactos(boolean tratamientoContactos) {
		this.tratamientoContactos = tratamientoContactos;
	}


	public boolean isTratamientoHospitalario() {
		return tratamientoHospitalario;
	}


	public void setTratamientoHospitalario(boolean tratamientoHospitalario) {
		this.tratamientoHospitalario = tratamientoHospitalario;
	}


	public String getSire() {
		return sire;
	}


	public void setSire(String sire) {
		this.sire = sire;
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


	public int getAlergiaPenicilina() {
		return alergiaPenicilina;
	}


	public void setAlergiaPenicilina(int alergiaPenicilina) {
		this.alergiaPenicilina = alergiaPenicilina;
	}


	public int getCondicionMomentoDx() {
		return condicionMomentoDx;
	}


	public void setCondicionMomentoDx(int condicionMomentoDx) {
		this.condicionMomentoDx = condicionMomentoDx;
	}


	public int getDesensibilizaPenicilina() {
		return desensibilizaPenicilina;
	}


	public void setDesensibilizaPenicilina(int desensibilizaPenicilina) {
		this.desensibilizaPenicilina = desensibilizaPenicilina;
	}


	public int getEsquemaCompleto() {
		return esquemaCompleto;
	}


	public void setEsquemaCompleto(int esquemaCompleto) {
		this.esquemaCompleto = esquemaCompleto;
	}


	public int getOtrasIts() {
		return otrasIts;
	}


	public void setOtrasIts(int otrasIts) {
		this.otrasIts = otrasIts;
	}


	public int getTipoTratamiento() {
		return tipoTratamiento;
	}


	public void setTipoTratamiento(int tipoTratamiento) {
		this.tipoTratamiento = tipoTratamiento;
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
