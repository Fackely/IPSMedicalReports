package com.sysmedica.actionform;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import util.UtilidadFecha;

import com.sysmedica.mundo.saludpublica.Paciente;

public class FichaInfeccionesForm extends ValidatorForm {

	private transient Logger logger=Logger.getLogger(FichaInfeccionesForm.class);
	
	private boolean activa;
	private String urlFichasAnteriores;
	
	private int codigoInstitucion;
	
	private boolean hayServicios;
    private boolean hayLaboratorios;
    
	private boolean notificar;
    private String loginUsuario;
    private int codigoFichaInfecciones;
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
    
    
    private String codCiudadResidencia;
    private String codDepResidencia;
    private String nombreMunProcedencia;
    private String nombreDepProcedencia;
    private String nombreInstitucionAtendio;
    private String nombreEnfermedadNotificable;
    private String nombreMunNotifica;
    private String nombreDepNotifica;
    private String primerNombreUsuario;
    private String segundoNombreUsuario;
    private String primerApellidoUsuario;
    private String segundoApellidoUsuario;
   
    
    private String estado;
    
    //*************************************
    // DATOS CARA B :
    
    private int numeroCama;
    private int servicio;
    private String fechaIngreso;
    private String fechaDxIh;
    private String fechaEgreso;
    private String fechaDxHisto;
    private String dxHospital;
    private String dxIh;
    private String dxEgreso;
    private String dxHisto;
    private int generoMicro;
    private String especieMicro;
    private String bioTipoMicro;
    private int tipoMuestra1;
    private int locAnatomica1;
    private String fechaToma1;
    private String fechaRemision1;
    private String medIdentificacion1;
    private String pruAdicionales1;
    private int tipoMuestra2;
    private int locAnatomica2;
    private String fechaToma2;
    private String fechaRemision2;
    private String medIdentificacion2;
    private String pruAdicionales2;
    private int antibiotico1;
    private String sensibilidad1;
    private String teDosis1;
    private String fechaInicioAntibiotico1;
    private String fechaFinAntibiotico1;
    private int antibiotico2;
    private String sensibilidad2;
    private String teDosis2;
    private String fechaInicioAntibiotico2;
    private String fechaFinAntibiotico2;
    private int antibiotico3;
    private String sensibilidad3;
    private String teDosis3;
    private String fechaInicioAntibiotico3;
    private String fechaFinAntibiotico3;
    private int clasificacionCaso;
    private int localizacionAnatomica;
    private String medicosTratantes;
    private String servicio2;
    
    private HashMap factoresRiesgo;
    
    //  Variables para las valoraciones
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
    
    private int numeroOtrosTipos;
    private int numeroOtrosFactores;
    
    private HashMap microorganismos;
    
    
    public void reset()
    {
    	sire="";
    	institucionAtendio = 0;
    	fechaInicioSint = "";
    	tipoCaso = 0;
    	hospitalizado = false;
    	fechaHospitalizacion = "";
    	estaVivo = false;
    	fechaDefuncion = "";
    	
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
    	
    	numeroCama = 0;
        servicio = 0;
        fechaIngreso = "";
        fechaDxIh = "";
        fechaEgreso = "";
        fechaDxHisto = "";
        dxHospital = "";
        dxIh = "";
        dxEgreso = "";
        dxHisto = "";
        generoMicro = 0;
        especieMicro = "";
        bioTipoMicro = "";
        tipoMuestra1 = 0;
        locAnatomica1 = 0;
        fechaToma1 = "";
        fechaRemision1 = "";
        medIdentificacion1 = "";
        pruAdicionales1 = "";
        tipoMuestra2 = 0;
        locAnatomica2 = 0;
        fechaToma2 = "";
        fechaRemision2 = "";
        medIdentificacion2 = "";
        pruAdicionales2 = "";
        antibiotico1 = 0;
        sensibilidad1 = "";
        teDosis1 = "";
        fechaInicioAntibiotico1 = "";
        fechaFinAntibiotico1 = "";
        antibiotico2 = 0;
        sensibilidad2 = "";
        teDosis2 = "";
        fechaInicioAntibiotico2 = "";
        fechaFinAntibiotico2 = "";
        antibiotico3 = 0;
        sensibilidad3 = "";
        teDosis3 = "";
        fechaInicioAntibiotico3 = "";
        fechaFinAntibiotico3 = "";
        clasificacionCaso = 0;
        localizacionAnatomica = 0;
        medicosTratantes = "";
        servicio2 = "";
        
        factoresRiesgo = new HashMap();
        
        numeroOtrosTipos = 0;
        numeroOtrosFactores = 0;
        
        microorganismos = new HashMap();
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
        	
        	String fechaIngresoTransformada = UtilidadFecha.conversionFormatoFechaABD(fechaIngreso);
            String fechaDxIhTransformada = UtilidadFecha.conversionFormatoFechaABD(fechaDxIh);
            String fechaEgresoTransformada = UtilidadFecha.conversionFormatoFechaABD(fechaEgreso);
            String fechaDxHistoTransformada = UtilidadFecha.conversionFormatoFechaABD(fechaDxHisto);
            String fechaToma1Transformada = UtilidadFecha.conversionFormatoFechaABD(fechaToma1);
            String fechaToma2Transformada = UtilidadFecha.conversionFormatoFechaABD(fechaToma2);
            String fechaRemision1Transformada = UtilidadFecha.conversionFormatoFechaABD(fechaRemision1);
            String fechaRemision2Transformada = UtilidadFecha.conversionFormatoFechaABD(fechaRemision2);
            String fechaInicioAntibiotico1Transformada = UtilidadFecha.conversionFormatoFechaABD(fechaInicioAntibiotico1);
            String fechaInicioAntibiotico2Transformada = UtilidadFecha.conversionFormatoFechaABD(fechaInicioAntibiotico2);
            String fechaInicioAntibiotico3Transformada = UtilidadFecha.conversionFormatoFechaABD(fechaInicioAntibiotico3);
            String fechaFinAntibiotico1Transformada = UtilidadFecha.conversionFormatoFechaABD(fechaFinAntibiotico1);
            String fechaFinAntibiotico2Transformada = UtilidadFecha.conversionFormatoFechaABD(fechaFinAntibiotico2);
            String fechaFinAntibiotico3Transformada = UtilidadFecha.conversionFormatoFechaABD(fechaFinAntibiotico3);
            
            String fechaActual=UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual());
            
            try {
	            if (fechaIngreso.trim().length()>0) {
		            if (!UtilidadFecha.validarFecha(fechaIngreso)) {
		                
		                errores.add("Campo Fecha de Ingreso no valido", new ActionMessage("errors.formatoFechaInvalido","de Ingreso"));
		            }
		            else if (fechaIngresoTransformada.compareTo(fechaActual)>0) {
						
						errores.add("Campo Fecha de Ingreso no valido", new ActionMessage("errors.fechaPosteriorIgualActual","de Ingreso", "actual"));
					}
	            }
	            
	            if (fechaDxIh.trim().length()>0) {
		            if (!UtilidadFecha.validarFecha(fechaDxIh)) {
		                
		                errores.add("Campo Fecha de Dx. IH no valido", new ActionMessage("errors.formatoFechaInvalido","de Dx. IH"));
		            }
		            else if (fechaDxIhTransformada.compareTo(fechaActual)>0) {
						
						errores.add("Campo Fecha de Dx. IH no valido", new ActionMessage("errors.fechaPosteriorIgualActual","de Dx. IH", "actual"));
					}
	            }
	            
	            if (fechaEgreso.trim().length()>0) {
		            if (!UtilidadFecha.validarFecha(fechaEgreso)) {
		                
		                errores.add("Campo Fecha de Egreso no valido", new ActionMessage("errors.formatoFechaInvalido","de Egreso"));
		            }
		            else if (fechaEgresoTransformada.compareTo(fechaActual)>0) {
						
						errores.add("Campo Fecha de Egreso no valido", new ActionMessage("errors.fechaPosteriorIgualActual","de Egreso", "actual"));
					}
	            }
	            
	            if (fechaDxHisto.trim().length()>0) {
		            if (!UtilidadFecha.validarFecha(fechaDxHisto)) {
		                
		                errores.add("Campo Fecha de Dx. Histop. no valido", new ActionMessage("errors.formatoFechaInvalido","de Dx. Histop."));
		            }
		            else if (fechaDxHistoTransformada.compareTo(fechaActual)>0) {
						
						errores.add("Campo Fecha de Dx. Histop. no valido", new ActionMessage("errors.fechaPosteriorIgualActual","de Dx. Histop.", "actual"));
					}
	            }
	            
	            if (fechaToma1.trim().length()>0) {
		            if (!UtilidadFecha.validarFecha(fechaToma1)) {
		                
		                errores.add("Campo Fecha de Toma de Primera Muestra no valido", new ActionMessage("errors.formatoFechaInvalido","de Toma de Primera Muestra"));
		            }
		            else if (fechaToma1Transformada.compareTo(fechaActual)>0) {
						
						errores.add("Campo Fecha de Toma de Primera Muestra no valido", new ActionMessage("errors.fechaPosteriorIgualActual","de Toma de Primera Muestra", "actual"));
					}
	            }
	            
	            if (fechaRemision1.trim().length()>0) {
		            if (!UtilidadFecha.validarFecha(fechaRemision1)) {
		                
		                errores.add("Campo Fecha de Remision de Primera Muestra no valido", new ActionMessage("errors.formatoFechaInvalido","de Remision de Primera Muestra"));
		            }
		            else if (fechaRemision1Transformada.compareTo(fechaActual)>0) {
						
						errores.add("Campo Fecha de Remision de Primera Muestra no valido", new ActionMessage("errors.fechaPosteriorIgualActual","de Remision de Primera Muestra", "actual"));
					}
	            }
	            
	            if (fechaToma2.trim().length()>0) {
		            if (!UtilidadFecha.validarFecha(fechaToma2)) {
		                
		                errores.add("Campo Fecha de Toma de Segunda Muestra no valido", new ActionMessage("errors.formatoFechaInvalido","de Toma de Segunda Muestra"));
		            }
		            else if (fechaToma2Transformada.compareTo(fechaActual)>0) {
						
						errores.add("Campo Fecha de Toma de Segunda Muestra no valido", new ActionMessage("errors.fechaPosteriorIgualActual","de Toma de Segunda Muestra", "actual"));
					}
	            }
	            
	            if (fechaRemision2.trim().length()>0) {
		            if (!UtilidadFecha.validarFecha(fechaRemision2)) {
		                
		                errores.add("Campo Fecha de Remision de Segunda Muestra no valido", new ActionMessage("errors.formatoFechaInvalido","de Remision de Segunda Muestra"));
		            }
		            else if (fechaRemision2Transformada.compareTo(fechaActual)>0) {
						
						errores.add("Campo Fecha de Remision de Segunda Muestra no valido", new ActionMessage("errors.fechaPosteriorIgualActual","de Remision de Segunda Muestra", "actual"));
					}
	            }
	            
	            if (fechaInicioAntibiotico1.trim().length()>0) {
		            if (!UtilidadFecha.validarFecha(fechaInicioAntibiotico1)) {
		                
		                errores.add("Campo Fecha de Inicio de Primer Antibiotico no valido", new ActionMessage("errors.formatoFechaInvalido","de Inicio de Primer Antibiotico"));
		            }
		            else if (fechaInicioAntibiotico1Transformada.compareTo(fechaActual)>0) {
						
						errores.add("Campo Fecha de Inicio de Primer Antibiotico no valido", new ActionMessage("errors.fechaPosteriorIgualActual","de Inicio de Primer Antibiotico", "actual"));
					}
	            }
	            
	            if (fechaFinAntibiotico1.trim().length()>0) {
		            if (!UtilidadFecha.validarFecha(fechaFinAntibiotico1)) {
		                
		                errores.add("Campo Fecha de Fin de Primer Antibiotico no valido", new ActionMessage("errors.formatoFechaInvalido","de Fin de Primer Antibiotico"));
		            }
		            else if (fechaFinAntibiotico1Transformada.compareTo(fechaActual)>0) {
						
						errores.add("Campo Fecha de Fin de Primer Antibiotico no valido", new ActionMessage("errors.fechaPosteriorIgualActual","de Fin de Primer Antibiotico", "actual"));
					}
	            }
	            
	            if (fechaInicioAntibiotico2.trim().length()>0) {
		            if (!UtilidadFecha.validarFecha(fechaInicioAntibiotico2)) {
		                
		                errores.add("Campo Fecha de Inicio de Segundo Antibiotico no valido", new ActionMessage("errors.formatoFechaInvalido","de Inicio de Segundo Antibiotico"));
		            }
		            else if (fechaInicioAntibiotico2Transformada.compareTo(fechaActual)>0) {
						
						errores.add("Campo Fecha de Inicio de Segundo Antibiotico no valido", new ActionMessage("errors.fechaPosteriorIgualActual","de Inicio de Segundo Antibiotico", "actual"));
					}
	            }
	            
	            if (fechaFinAntibiotico2.trim().length()>0) {
		            if (!UtilidadFecha.validarFecha(fechaFinAntibiotico2)) {
		                
		                errores.add("Campo Fecha de Fin de Segundo Antibiotico no valido", new ActionMessage("errors.formatoFechaInvalido","de Fin de Segundo Antibiotico"));
		            }
		            else if (fechaFinAntibiotico2Transformada.compareTo(fechaActual)>0) {
						
						errores.add("Campo Fecha de Fin de Segundo Antibiotico no valido", new ActionMessage("errors.fechaPosteriorIgualActual","de Fin de Segundo Antibiotico", "actual"));
					}
	            }
	            
	            if (fechaInicioAntibiotico3.trim().length()>0) {
		            if (!UtilidadFecha.validarFecha(fechaInicioAntibiotico3)) {
		                
		                errores.add("Campo Fecha de Inicio de Tercer Antibiotico no valido", new ActionMessage("errors.formatoFechaInvalido","de Inicio de Tercer Antibiotico"));
		            }
		            else if (fechaInicioAntibiotico3Transformada.compareTo(fechaActual)>0) {
						
						errores.add("Campo Fecha de Inicio de Tercer Antibiotico no valido", new ActionMessage("errors.fechaPosteriorIgualActual","de Inicio de Tercer Antibiotico", "actual"));
					}
	            }
	            
	            if (fechaFinAntibiotico3.trim().length()>0) {
		            if (!UtilidadFecha.validarFecha(fechaFinAntibiotico3)) {
		                
		                errores.add("Campo Fecha de Fin de Tercer Antibiotico no valido", new ActionMessage("errors.formatoFechaInvalido","de Fin de Tercer Antibiotico"));
		            }
		            else if (fechaFinAntibiotico3Transformada.compareTo(fechaActual)>0) {
						
						errores.add("Campo Fecha de Fin de Tercer Antibiotico no valido", new ActionMessage("errors.fechaPosteriorIgualActual","de Fin de Tercer Antibiotico", "actual"));
					}
	            }
            }
            catch (NullPointerException npe) {}
        }
        
        
        if (estado.equals("validar")) {
        	
        	String fechaIngresoTransformada = UtilidadFecha.conversionFormatoFechaABD(fechaIngreso);
            String fechaDxIhTransformada = UtilidadFecha.conversionFormatoFechaABD(fechaDxIh);
            String fechaEgresoTransformada = UtilidadFecha.conversionFormatoFechaABD(fechaEgreso);
            String fechaDxHistoTransformada = UtilidadFecha.conversionFormatoFechaABD(fechaDxHisto);
            String fechaToma1Transformada = UtilidadFecha.conversionFormatoFechaABD(fechaToma1);
            String fechaToma2Transformada = UtilidadFecha.conversionFormatoFechaABD(fechaToma2);
            String fechaRemision1Transformada = UtilidadFecha.conversionFormatoFechaABD(fechaRemision1);
            String fechaRemision2Transformada = UtilidadFecha.conversionFormatoFechaABD(fechaRemision2);
            String fechaInicioAntibiotico1Transformada = UtilidadFecha.conversionFormatoFechaABD(fechaInicioAntibiotico1);
            String fechaInicioAntibiotico2Transformada = UtilidadFecha.conversionFormatoFechaABD(fechaInicioAntibiotico2);
            String fechaInicioAntibiotico3Transformada = UtilidadFecha.conversionFormatoFechaABD(fechaInicioAntibiotico3);
            String fechaFinAntibiotico1Transformada = UtilidadFecha.conversionFormatoFechaABD(fechaFinAntibiotico1);
            String fechaFinAntibiotico2Transformada = UtilidadFecha.conversionFormatoFechaABD(fechaFinAntibiotico2);
            String fechaFinAntibiotico3Transformada = UtilidadFecha.conversionFormatoFechaABD(fechaFinAntibiotico3);
            
            String fechaActual=UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual());
            
            try {
	            if (fechaIngreso.trim().length()>0) {
		            if (!UtilidadFecha.validarFecha(fechaIngreso)) {
		                
		                errores.add("Campo Fecha de Ingreso no valido", new ActionMessage("errors.formatoFechaInvalido","de Ingreso"));
		            }
		            else if (fechaIngresoTransformada.compareTo(fechaActual)>0) {
						
						errores.add("Campo Fecha de Ingreso no valido", new ActionMessage("errors.fechaPosteriorIgualActual","de Ingreso", "actual"));
					}
	            }
	            
	            if (fechaDxIh.trim().length()>0) {
		            if (!UtilidadFecha.validarFecha(fechaDxIh)) {
		                
		                errores.add("Campo Fecha de Dx. IH no valido", new ActionMessage("errors.formatoFechaInvalido","de Dx. IH"));
		            }
		            else if (fechaDxIhTransformada.compareTo(fechaActual)>0) {
						
						errores.add("Campo Fecha de Dx. IH no valido", new ActionMessage("errors.fechaPosteriorIgualActual","de Dx. IH", "actual"));
					}
	            }
	            
	            if (fechaEgreso.trim().length()>0) {
		            if (!UtilidadFecha.validarFecha(fechaEgreso)) {
		                
		                errores.add("Campo Fecha de Egreso no valido", new ActionMessage("errors.formatoFechaInvalido","de Egreso"));
		            }
		            else if (fechaEgresoTransformada.compareTo(fechaActual)>0) {
						
						errores.add("Campo Fecha de Egreso no valido", new ActionMessage("errors.fechaPosteriorIgualActual","de Egreso", "actual"));
					}
	            }
	            
	            if (fechaDxHisto.trim().length()>0) {
		            if (!UtilidadFecha.validarFecha(fechaDxHisto)) {
		                
		                errores.add("Campo Fecha de Dx. Histop. no valido", new ActionMessage("errors.formatoFechaInvalido","de Dx. Histop."));
		            }
		            else if (fechaDxHistoTransformada.compareTo(fechaActual)>0) {
						
						errores.add("Campo Fecha de Dx. Histop. no valido", new ActionMessage("errors.fechaPosteriorIgualActual","de Dx. Histop.", "actual"));
					}
	            }
	            
	            if (fechaToma1.trim().length()>0) {
		            if (!UtilidadFecha.validarFecha(fechaToma1)) {
		                
		                errores.add("Campo Fecha de Toma de Primera Muestra no valido", new ActionMessage("errors.formatoFechaInvalido","de Toma de Primera Muestra"));
		            }
		            else if (fechaToma1Transformada.compareTo(fechaActual)>0) {
						
						errores.add("Campo Fecha de Toma de Primera Muestra no valido", new ActionMessage("errors.fechaPosteriorIgualActual","de Toma de Primera Muestra", "actual"));
					}
	            }
	            
	            if (fechaRemision1.trim().length()>0) {
		            if (!UtilidadFecha.validarFecha(fechaRemision1)) {
		                
		                errores.add("Campo Fecha de Remision de Primera Muestra no valido", new ActionMessage("errors.formatoFechaInvalido","de Remision de Primera Muestra"));
		            }
		            else if (fechaRemision1Transformada.compareTo(fechaActual)>0) {
						
						errores.add("Campo Fecha de Remision de Primera Muestra no valido", new ActionMessage("errors.fechaPosteriorIgualActual","de Remision de Primera Muestra", "actual"));
					}
	            }
	            
	            if (fechaToma2.trim().length()>0) {
		            if (!UtilidadFecha.validarFecha(fechaToma2)) {
		                
		                errores.add("Campo Fecha de Toma de Segunda Muestra no valido", new ActionMessage("errors.formatoFechaInvalido","de Toma de Segunda Muestra"));
		            }
		            else if (fechaToma2Transformada.compareTo(fechaActual)>0) {
						
						errores.add("Campo Fecha de Toma de Segunda Muestra no valido", new ActionMessage("errors.fechaPosteriorIgualActual","de Toma de Segunda Muestra", "actual"));
					}
	            }
	            
	            if (fechaRemision2.trim().length()>0) {
		            if (!UtilidadFecha.validarFecha(fechaRemision2)) {
		                
		                errores.add("Campo Fecha de Remision de Segunda Muestra no valido", new ActionMessage("errors.formatoFechaInvalido","de Remision de Segunda Muestra"));
		            }
		            else if (fechaRemision2Transformada.compareTo(fechaActual)>0) {
						
						errores.add("Campo Fecha de Remision de Segunda Muestra no valido", new ActionMessage("errors.fechaPosteriorIgualActual","de Remision de Segunda Muestra", "actual"));
					}
	            }
	            
	            if (fechaInicioAntibiotico1.trim().length()>0) {
		            if (!UtilidadFecha.validarFecha(fechaInicioAntibiotico1)) {
		                
		                errores.add("Campo Fecha de Inicio de Primer Antibiotico no valido", new ActionMessage("errors.formatoFechaInvalido","de Inicio de Primer Antibiotico"));
		            }
		            else if (fechaInicioAntibiotico1Transformada.compareTo(fechaActual)>0) {
						
						errores.add("Campo Fecha de Inicio de Primer Antibiotico no valido", new ActionMessage("errors.fechaPosteriorIgualActual","de Inicio de Primer Antibiotico", "actual"));
					}
	            }
	            
	            if (fechaFinAntibiotico1.trim().length()>0) {
		            if (!UtilidadFecha.validarFecha(fechaFinAntibiotico1)) {
		                
		                errores.add("Campo Fecha de Fin de Primer Antibiotico no valido", new ActionMessage("errors.formatoFechaInvalido","de Fin de Primer Antibiotico"));
		            }
		            else if (fechaFinAntibiotico1Transformada.compareTo(fechaActual)>0) {
						
						errores.add("Campo Fecha de Fin de Primer Antibiotico no valido", new ActionMessage("errors.fechaPosteriorIgualActual","de Fin de Primer Antibiotico", "actual"));
					}
	            }
	            
	            if (fechaInicioAntibiotico2.trim().length()>0) {
		            if (!UtilidadFecha.validarFecha(fechaInicioAntibiotico2)) {
		                
		                errores.add("Campo Fecha de Inicio de Segundo Antibiotico no valido", new ActionMessage("errors.formatoFechaInvalido","de Inicio de Segundo Antibiotico"));
		            }
		            else if (fechaInicioAntibiotico2Transformada.compareTo(fechaActual)>0) {
						
						errores.add("Campo Fecha de Inicio de Segundo Antibiotico no valido", new ActionMessage("errors.fechaPosteriorIgualActual","de Inicio de Segundo Antibiotico", "actual"));
					}
	            }
	            
	            if (fechaFinAntibiotico2.trim().length()>0) {
		            if (!UtilidadFecha.validarFecha(fechaFinAntibiotico2)) {
		                
		                errores.add("Campo Fecha de Fin de Segundo Antibiotico no valido", new ActionMessage("errors.formatoFechaInvalido","de Fin de Segundo Antibiotico"));
		            }
		            else if (fechaFinAntibiotico2Transformada.compareTo(fechaActual)>0) {
						
						errores.add("Campo Fecha de Fin de Segundo Antibiotico no valido", new ActionMessage("errors.fechaPosteriorIgualActual","de Fin de Segundo Antibiotico", "actual"));
					}
	            }
	            
	            if (fechaInicioAntibiotico3.trim().length()>0) {
		            if (!UtilidadFecha.validarFecha(fechaInicioAntibiotico3)) {
		                
		                errores.add("Campo Fecha de Inicio de Tercer Antibiotico no valido", new ActionMessage("errors.formatoFechaInvalido","de Inicio de Tercer Antibiotico"));
		            }
		            else if (fechaInicioAntibiotico3Transformada.compareTo(fechaActual)>0) {
						
						errores.add("Campo Fecha de Inicio de Tercer Antibiotico no valido", new ActionMessage("errors.fechaPosteriorIgualActual","de Inicio de Tercer Antibiotico", "actual"));
					}
	            }
	            
	            if (fechaFinAntibiotico3.trim().length()>0) {
		            if (!UtilidadFecha.validarFecha(fechaFinAntibiotico3)) {
		                
		                errores.add("Campo Fecha de Fin de Tercer Antibiotico no valido", new ActionMessage("errors.formatoFechaInvalido","de Fin de Tercer Antibiotico"));
		            }
		            else if (fechaFinAntibiotico3Transformada.compareTo(fechaActual)>0) {
						
						errores.add("Campo Fecha de Fin de Tercer Antibiotico no valido", new ActionMessage("errors.fechaPosteriorIgualActual","de Fin de Tercer Antibiotico", "actual"));
					}
	            }
            }
            catch (NullPointerException npe) {}
        }
        
        return errores;
    }
    
	public int getAntibiotico1() {
		return antibiotico1;
	}
	public void setAntibiotico1(int antibiotico1) {
		this.antibiotico1 = antibiotico1;
	}
	public int getAntibiotico2() {
		return antibiotico2;
	}
	public void setAntibiotico2(int antibiotico2) {
		this.antibiotico2 = antibiotico2;
	}
	public int getAntibiotico3() {
		return antibiotico3;
	}
	public void setAntibiotico3(int antibiotico3) {
		this.antibiotico3 = antibiotico3;
	}
	public int getAnyoSemanaEpi() {
		return anyoSemanaEpi;
	}
	public void setAnyoSemanaEpi(int anyoSemanaEpi) {
		this.anyoSemanaEpi = anyoSemanaEpi;
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
	public String getBioTipoMicro() {
		return bioTipoMicro;
	}
	public void setBioTipoMicro(String bioTipoMicro) {
		this.bioTipoMicro = bioTipoMicro;
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
	public int getClasificacionCaso() {
		return clasificacionCaso;
	}
	public void setClasificacionCaso(int clasificacionCaso) {
		this.clasificacionCaso = clasificacionCaso;
	}
	public String getCodCiudadResidencia() {
		return codCiudadResidencia;
	}
	public void setCodCiudadResidencia(String codCiudadResidencia) {
		this.codCiudadResidencia = codCiudadResidencia;
	}
	public String getCodDepResidencia() {
		return codDepResidencia;
	}
	public void setCodDepResidencia(String codDepResidencia) {
		this.codDepResidencia = codDepResidencia;
	}
	public String getCodigoDepProcedencia() {
		return codigoDepProcedencia;
	}
	public void setCodigoDepProcedencia(String codigoDepProcedencia) {
		this.codigoDepProcedencia = codigoDepProcedencia;
	}
	public String getCodigoDiagnostico() {
		return codigoDiagnostico;
	}
	public void setCodigoDiagnostico(String codigoDiagnostico) {
		this.codigoDiagnostico = codigoDiagnostico;
	}
	public int getCodigoFichaInfecciones() {
		return codigoFichaInfecciones;
	}
	public void setCodigoFichaInfecciones(int codigoFichaInfecciones) {
		this.codigoFichaInfecciones = codigoFichaInfecciones;
	}
	public String getCodigoMunProcedencia() {
		return codigoMunProcedencia;
	}
	public void setCodigoMunProcedencia(String codigoMunProcedencia) {
		this.codigoMunProcedencia = codigoMunProcedencia;
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
	public String getDepartamentoNacimiento() {
		return departamentoNacimiento;
	}
	public void setDepartamentoNacimiento(String departamentoNacimiento) {
		this.departamentoNacimiento = departamentoNacimiento;
	}
	public String getDepartamentoNotifica() {
		return departamentoNotifica;
	}
	public void setDepartamentoNotifica(String departamentoNotifica) {
		this.departamentoNotifica = departamentoNotifica;
	}
	public String getDepartamentoResidencia() {
		return departamentoResidencia;
	}
	public void setDepartamentoResidencia(String departamentoResidencia) {
		this.departamentoResidencia = departamentoResidencia;
	}
	public int getDesplazado() {
		return desplazado;
	}
	public void setDesplazado(int desplazado) {
		this.desplazado = desplazado;
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
	public String getDxEgreso() {
		return dxEgreso;
	}
	public void setDxEgreso(String dxEgreso) {
		this.dxEgreso = dxEgreso;
	}
	public String getDxHisto() {
		return dxHisto;
	}
	public void setDxHisto(String dxHisto) {
		this.dxHisto = dxHisto;
	}
	public String getDxHospital() {
		return dxHospital;
	}
	public void setDxHospital(String dxHospital) {
		this.dxHospital = dxHospital;
	}
	public String getDxIh() {
		return dxIh;
	}
	public void setDxIh(String dxIh) {
		this.dxIh = dxIh;
	}
	public String getEdad() {
		return edad;
	}
	public void setEdad(String edad) {
		this.edad = edad;
	}
	public String getEspecieMicro() {
		return especieMicro;
	}
	public void setEspecieMicro(String especieMicro) {
		this.especieMicro = especieMicro;
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
	public String getFechaDxHisto() {
		return fechaDxHisto;
	}
	public void setFechaDxHisto(String fechaDxHisto) {
		this.fechaDxHisto = fechaDxHisto;
	}
	public String getFechaDxIh() {
		return fechaDxIh;
	}
	public void setFechaDxIh(String fechaDxIh) {
		this.fechaDxIh = fechaDxIh;
	}
	public String getFechaEgreso() {
		return fechaEgreso;
	}
	public void setFechaEgreso(String fechaEgreso) {
		this.fechaEgreso = fechaEgreso;
	}
	public String getFechaFinAntibiotico1() {
		return fechaFinAntibiotico1;
	}
	public void setFechaFinAntibiotico1(String fechaFinAntibiotico1) {
		this.fechaFinAntibiotico1 = fechaFinAntibiotico1;
	}
	public String getFechaFinAntibiotico2() {
		return fechaFinAntibiotico2;
	}
	public void setFechaFinAntibiotico2(String fechaFinAntibiotico2) {
		this.fechaFinAntibiotico2 = fechaFinAntibiotico2;
	}
	public String getFechaFinAntibiotico3() {
		return fechaFinAntibiotico3;
	}
	public void setFechaFinAntibiotico3(String fechaFinAntibiotico3) {
		this.fechaFinAntibiotico3 = fechaFinAntibiotico3;
	}
	public String getFechaHospitalizacion() {
		return fechaHospitalizacion;
	}
	public void setFechaHospitalizacion(String fechaHospitalizacion) {
		this.fechaHospitalizacion = fechaHospitalizacion;
	}
	public String getFechaIngreso() {
		return fechaIngreso;
	}
	public void setFechaIngreso(String fechaIngreso) {
		this.fechaIngreso = fechaIngreso;
	}
	public String getFechaInicioAntibiotico1() {
		return fechaInicioAntibiotico1;
	}
	public void setFechaInicioAntibiotico1(String fechaInicioAntibiotico1) {
		this.fechaInicioAntibiotico1 = fechaInicioAntibiotico1;
	}
	public String getFechaInicioAntibiotico2() {
		return fechaInicioAntibiotico2;
	}
	public void setFechaInicioAntibiotico2(String fechaInicioAntibiotico2) {
		this.fechaInicioAntibiotico2 = fechaInicioAntibiotico2;
	}
	public String getFechaInicioAntibiotico3() {
		return fechaInicioAntibiotico3;
	}
	public void setFechaInicioAntibiotico3(String fechaInicioAntibiotico3) {
		this.fechaInicioAntibiotico3 = fechaInicioAntibiotico3;
	}
	public String getFechaInicioSint() {
		return fechaInicioSint;
	}
	public void setFechaInicioSint(String fechaInicioSint) {
		this.fechaInicioSint = fechaInicioSint;
	}
	public String getFechaNacimiento() {
		return fechaNacimiento;
	}
	public void setFechaNacimiento(String fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}
	public String getFechaNotificacion() {
		return fechaNotificacion;
	}
	public void setFechaNotificacion(String fechaNotificacion) {
		this.fechaNotificacion = fechaNotificacion;
	}
	public String getFechaRemision1() {
		return fechaRemision1;
	}
	public void setFechaRemision1(String fechaRemision1) {
		this.fechaRemision1 = fechaRemision1;
	}
	public String getFechaRemision2() {
		return fechaRemision2;
	}
	public void setFechaRemision2(String fechaRemision2) {
		this.fechaRemision2 = fechaRemision2;
	}
	public String getFechaToma1() {
		return fechaToma1;
	}
	public void setFechaToma1(String fechaToma1) {
		this.fechaToma1 = fechaToma1;
	}
	public String getFechaToma2() {
		return fechaToma2;
	}
	public void setFechaToma2(String fechaToma2) {
		this.fechaToma2 = fechaToma2;
	}
	public String getGenero() {
		return genero;
	}
	public void setGenero(String genero) {
		this.genero = genero;
	}
	public int getGeneroMicro() {
		return generoMicro;
	}
	public void setGeneroMicro(int generoMicro) {
		this.generoMicro = generoMicro;
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
	public int getLocAnatomica1() {
		return locAnatomica1;
	}
	public void setLocAnatomica1(int locAnatomica1) {
		this.locAnatomica1 = locAnatomica1;
	}
	public int getLocAnatomica2() {
		return locAnatomica2;
	}
	public void setLocAnatomica2(int locAnatomica2) {
		this.locAnatomica2 = locAnatomica2;
	}
	public String getLoginUsuario() {
		return loginUsuario;
	}
	public void setLoginUsuario(String loginUsuario) {
		this.loginUsuario = loginUsuario;
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
	public String getMedIdentificacion1() {
		return medIdentificacion1;
	}
	public void setMedIdentificacion1(String identificacion1) {
		medIdentificacion1 = identificacion1;
	}
	public String getMedIdentificacion2() {
		return medIdentificacion2;
	}
	public void setMedIdentificacion2(String identificacion2) {
		medIdentificacion2 = identificacion2;
	}
	public String getMunicipioNotifica() {
		return municipioNotifica;
	}
	public void setMunicipioNotifica(String municipioNotifica) {
		this.municipioNotifica = municipioNotifica;
	}
	public String getNombreDepNotifica() {
		return nombreDepNotifica;
	}
	public void setNombreDepNotifica(String nombreDepNotifica) {
		this.nombreDepNotifica = nombreDepNotifica;
	}
	public String getNombreDepProcedencia() {
		return nombreDepProcedencia;
	}
	public void setNombreDepProcedencia(String nombreDepProcedencia) {
		this.nombreDepProcedencia = nombreDepProcedencia;
	}
	public String getNombreEnfermedadNotificable() {
		return nombreEnfermedadNotificable;
	}
	public void setNombreEnfermedadNotificable(String nombreEnfermedadNotificable) {
		this.nombreEnfermedadNotificable = nombreEnfermedadNotificable;
	}
	public String getNombreInstitucionAtendio() {
		return nombreInstitucionAtendio;
	}
	public void setNombreInstitucionAtendio(String nombreInstitucionAtendio) {
		this.nombreInstitucionAtendio = nombreInstitucionAtendio;
	}
	public String getNombreMunNotifica() {
		return nombreMunNotifica;
	}
	public void setNombreMunNotifica(String nombreMunNotifica) {
		this.nombreMunNotifica = nombreMunNotifica;
	}
	public String getNombreMunProcedencia() {
		return nombreMunProcedencia;
	}
	public void setNombreMunProcedencia(String nombreMunProcedencia) {
		this.nombreMunProcedencia = nombreMunProcedencia;
	}
	public String getNombreProfesional() {
		return nombreProfesional;
	}
	public void setNombreProfesional(String nombreProfesional) {
		this.nombreProfesional = nombreProfesional;
	}
	public boolean isNotificar() {
		return notificar;
	}
	public void setNotificar(boolean notificar) {
		this.notificar = notificar;
	}
	public int getNumeroCama() {
		return numeroCama;
	}
	public void setNumeroCama(int numeroCama) {
		this.numeroCama = numeroCama;
	}
	public int getNumeroSolicitud() {
		return numeroSolicitud;
	}
	public void setNumeroSolicitud(int numeroSolicitud) {
		this.numeroSolicitud = numeroSolicitud;
	}
	public String getOcupacion() {
		return ocupacion;
	}
	public void setOcupacion(String ocupacion) {
		this.ocupacion = ocupacion;
	}
	public String getPrimerApellido() {
		return primerApellido;
	}
	public void setPrimerApellido(String primerApellido) {
		this.primerApellido = primerApellido;
	}
	public String getPrimerApellidoUsuario() {
		return primerApellidoUsuario;
	}
	public void setPrimerApellidoUsuario(String primerApellidoUsuario) {
		this.primerApellidoUsuario = primerApellidoUsuario;
	}
	public String getPrimerNombre() {
		return primerNombre;
	}
	public void setPrimerNombre(String primerNombre) {
		this.primerNombre = primerNombre;
	}
	public String getPrimerNombreUsuario() {
		return primerNombreUsuario;
	}
	public void setPrimerNombreUsuario(String primerNombreUsuario) {
		this.primerNombreUsuario = primerNombreUsuario;
	}
	public String getPruAdicionales1() {
		return pruAdicionales1;
	}
	public void setPruAdicionales1(String pruAdicionales1) {
		this.pruAdicionales1 = pruAdicionales1;
	}
	public String getPruAdicionales2() {
		return pruAdicionales2;
	}
	public void setPruAdicionales2(String pruAdicionales2) {
		this.pruAdicionales2 = pruAdicionales2;
	}
	public String getRegimenSalud() {
		return regimenSalud;
	}
	public void setRegimenSalud(String regimenSalud) {
		this.regimenSalud = regimenSalud;
	}
	public String getSegundoApellido() {
		return segundoApellido;
	}
	public void setSegundoApellido(String segundoApellido) {
		this.segundoApellido = segundoApellido;
	}
	public String getSegundoApellidoUsuario() {
		return segundoApellidoUsuario;
	}
	public void setSegundoApellidoUsuario(String segundoApellidoUsuario) {
		this.segundoApellidoUsuario = segundoApellidoUsuario;
	}
	public String getSegundoNombre() {
		return segundoNombre;
	}
	public void setSegundoNombre(String segundoNombre) {
		this.segundoNombre = segundoNombre;
	}
	public String getSegundoNombreUsuario() {
		return segundoNombreUsuario;
	}
	public void setSegundoNombreUsuario(String segundoNombreUsuario) {
		this.segundoNombreUsuario = segundoNombreUsuario;
	}
	public int getSemanaEpidemiologica() {
		return semanaEpidemiologica;
	}
	public void setSemanaEpidemiologica(int semanaEpidemiologica) {
		this.semanaEpidemiologica = semanaEpidemiologica;
	}
	public String getSensibilidad1() {
		return sensibilidad1;
	}
	public void setSensibilidad1(String sensibilidad1) {
		this.sensibilidad1 = sensibilidad1;
	}
	public String getSensibilidad2() {
		return sensibilidad2;
	}
	public void setSensibilidad2(String sensibilidad2) {
		this.sensibilidad2 = sensibilidad2;
	}
	public String getSensibilidad3() {
		return sensibilidad3;
	}
	public void setSensibilidad3(String sensibilidad3) {
		this.sensibilidad3 = sensibilidad3;
	}
	public int getServicio() {
		return servicio;
	}
	public void setServicio(int servicio) {
		this.servicio = servicio;
	}
	public String getSire() {
		return sire;
	}
	public void setSire(String sire) {
		this.sire = sire;
	}
	public String getTeDosis1() {
		return teDosis1;
	}
	public void setTeDosis1(String dosis1) {
		teDosis1 = dosis1;
	}
	public String getTeDosis2() {
		return teDosis2;
	}
	public void setTeDosis2(String dosis2) {
		teDosis2 = dosis2;
	}
	public String getTeDosis3() {
		return teDosis3;
	}
	public void setTeDosis3(String dosis3) {
		teDosis3 = dosis3;
	}
	public String getTelefonoPaciente() {
		return telefonoPaciente;
	}
	public void setTelefonoPaciente(String telefonoPaciente) {
		this.telefonoPaciente = telefonoPaciente;
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
	public int getTipoMuestra1() {
		return tipoMuestra1;
	}
	public void setTipoMuestra1(int tipoMuestra1) {
		this.tipoMuestra1 = tipoMuestra1;
	}
	public int getTipoMuestra2() {
		return tipoMuestra2;
	}
	public void setTipoMuestra2(int tipoMuestra2) {
		this.tipoMuestra2 = tipoMuestra2;
	}
	public String getZonaDomicilio() {
		return zonaDomicilio;
	}
	public void setZonaDomicilio(String zonaDomicilio) {
		this.zonaDomicilio = zonaDomicilio;
	}


	public HashMap getFactoresRiesgo() {
		return factoresRiesgo;
	}


	public void setFactoresRiesgo(HashMap factoresRiesgo) {
		this.factoresRiesgo = factoresRiesgo;
	}


	public int getLocalizacionAnatomica() {
		return localizacionAnatomica;
	}


	public void setLocalizacionAnatomica(int localizacionAnatomica) {
		this.localizacionAnatomica = localizacionAnatomica;
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




	public String getMedicosTratantes() {
		return medicosTratantes;
	}




	public void setMedicosTratantes(String medicosTratantes) {
		this.medicosTratantes = medicosTratantes;
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




	public String getServicio2() {
		return servicio2;
	}




	public void setServicio2(String servicio2) {
		this.servicio2 = servicio2;
	}




	public int getNumeroOtrosTipos() {
		return numeroOtrosTipos;
	}




	public void setNumeroOtrosTipos(int numeroOtrosTipos) {
		this.numeroOtrosTipos = numeroOtrosTipos;
	}




	public HashMap getMicroorganismos() {
		return microorganismos;
	}




	public void setMicroorganismos(HashMap microorganismos) {
		this.microorganismos = microorganismos;
	}




	public int getNumeroOtrosFactores() {
		return numeroOtrosFactores;
	}




	public void setNumeroOtrosFactores(int numeroOtrosFactores) {
		this.numeroOtrosFactores = numeroOtrosFactores;
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
